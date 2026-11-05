package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Document7;
import model.DocumentCopy7;
import model.LoanItem7;

public class LoanItemDAO extends DAO {
    public LoanItemDAO() {
        super();
    }

    /**
     * Lay cac dong muon chua tra cua ban doc (hien thi tai lieu dang muon).
     * Dong muon chua tra = dong chua co dong tra tuong ung trong tblReturnItem7.
     * @param idReader id cua ban doc
     * @return danh sach dong muon chua tra, sap theo han tra; SQLException neu loi
     */
    public ArrayList<LoanItem7> getUnreturnedItems(int idReader) throws SQLException {
        try (Connection con = getConnection()) { return getUnreturnedItems(con, idReader); }
    }

    ArrayList<LoanItem7> getUnreturnedItems(Connection con, int idReader) throws SQLException {
        ArrayList<LoanItem7> kq = new ArrayList<LoanItem7>();
        String sql = "SELECT li.id, li.dueDate, c.id AS idCopy, c.barcode, c.condition, c.status, "
                + "d.id AS idDocument, d.title "
                + "FROM tblLoanItem7 li JOIN tblLoan7 l ON l.id = li.tblLoan7id "
                + "JOIN tblDocumentCopy7 c ON c.id = li.tblDocumentCopy7id "
                + "JOIN tblDocument7 d ON d.id = c.tblDocument7id "
                + "WHERE l.tblReader7id = ? "
                + "AND NOT EXISTS (SELECT 1 FROM tblReturnItem7 ri WHERE ri.tblLoanItem7id = li.id) "
                + "ORDER BY li.dueDate";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReader);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoanItem7 li = new LoanItem7();
                    li.setId(rs.getInt("id"));
                    li.setDueDate(rs.getDate("dueDate"));
                    DocumentCopy7 c = new DocumentCopy7();
                    c.setId(rs.getInt("idCopy"));
                    c.setBarcode(rs.getString("barcode"));
                    c.setCondition(rs.getString("condition"));
                    c.setStatus(rs.getString("status"));
                    Document7 d = new Document7();
                    d.setId(rs.getInt("idDocument"));
                    d.setTitle(rs.getString("title"));
                    c.setDocument(d);
                    li.setDocumentCopy(c);
                    kq.add(li);
                }
            }
        }
        return kq;
    }
}
