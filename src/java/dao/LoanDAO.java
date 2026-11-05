package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Loan7;
import model.LoanItem7;
import model.ReaderCard7;
import util.BorrowingRules7;

public class LoanDAO extends DAO {
    public LoanDAO() { super(); }

    /** false: business conditions changed; SQLException: storage failed. */
    public boolean addLoan(Loan7 loan, ReaderCard7 readerCard) throws SQLException {
        if (loan == null || loan.isSaved() || loan.getReader() == null || loan.getLibrarian() == null
                || readerCard == null || readerCard.getReader() == null || loan.getLoanItems().isEmpty()
                || loan.getReader().getId() != readerCard.getReader().getId()) return false;
        List<LoanItem7> items = new ArrayList<>(loan.getLoanItems());
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            try {
                // Serialize borrowing transactions for this reader, even for different copies.
                if (!lock(con, "SELECT tblMember7id FROM tblReader7 WHERE tblMember7id = ? FOR UPDATE", loan.getReader().getId())
                        || !lock(con, "SELECT id FROM tblReaderCard7 WHERE id = ? FOR UPDATE", readerCard.getId())) {
                    con.rollback(); return false;
                }
                ReaderCard7 fresh = new ReaderCardDAO().getReaderCardByNumber(con, readerCard.getCardNumber());
                if (fresh == null || fresh.getId() != readerCard.getId() || fresh.getReader().getId() != loan.getReader().getId()
                        || BorrowingRules7.validate(fresh, new LoanItemDAO().getUnreturnedItems(con, loan.getReader().getId()),
                                items.size(), BorrowingRules7.today()) != null) {
                    con.rollback(); return false;
                }
                Date borrowedAt = new Date();
                Date dueDate = BorrowingRules7.dueDate(borrowedAt, fresh.getReader().getReaderType().getLoanDays());
                int loanId;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO tblLoan7(borrowedAt, note, tblReader7id, tblStaff7id) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setTimestamp(1, new Timestamp(borrowedAt.getTime())); ps.setString(2, loan.getNote());
                    ps.setInt(3, loan.getReader().getId()); ps.setInt(4, loan.getLibrarian().getId());
                    ps.executeUpdate(); loanId = generatedKey(ps);
                }
                Map<Integer, Integer> itemIds = new HashMap<>();
                // Lock copies in a fixed order to reduce deadlocks between readers.
                List<LoanItem7> ordered = new ArrayList<>(items);
                ordered.sort(Comparator.comparingInt(item -> item.getDocumentCopy().getId()));
                for (LoanItem7 item : ordered) {
                    int idCopy = item.getDocumentCopy().getId();
                    try (PreparedStatement ps = con.prepareStatement(
                            "UPDATE tblDocumentCopy7 SET status = 'Đang mượn' WHERE id = ? AND status = 'Sẵn có'")) {
                        ps.setInt(1, idCopy);
                        if (ps.executeUpdate() != 1) { con.rollback(); return false; }
                    }
                    try (PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO tblLoanItem7(dueDate, tblLoan7id, tblDocumentCopy7id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                        ps.setDate(1, java.sql.Date.valueOf(BorrowingRules7.date(dueDate))); ps.setInt(2, loanId); ps.setInt(3, idCopy);
                        ps.executeUpdate(); itemIds.put(idCopy, generatedKey(ps));
                    }
                }
                con.commit();
                // Publish IDs and dates only after commit; rollback never changes the draft.
                loan.setBorrowedAt(borrowedAt);
                for (LoanItem7 item : items) {
                    item.setId(itemIds.get(item.getDocumentCopy().getId())); item.setDueDate(dueDate);
                    item.getDocumentCopy().setStatus("Đang mượn");
                }
                loan.setId(loanId);
                return true;
            } catch (SQLException | RuntimeException e) {
                try { con.rollback(); } catch (SQLException rollbackError) { e.addSuppressed(rollbackError); }
                throw e;
            }
        }
    }

    private boolean lock(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private int generatedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (!rs.next()) throw new SQLException("Không nhận được ID sau khi thêm dữ liệu");
            return rs.getInt(1);
        }
    }
}
