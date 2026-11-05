package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.FullName7;
import model.Librarian7;
import model.Manager7;
import model.Member7;
import model.Reader7;
import model.ReaderType7;

public class MemberDAO extends DAO {
    public MemberDAO() {
        super();
    }

    /**
     * Kiem tra dang nhap (Login 7, dung chung cho cac module).
     * Vai tro xac dinh theo bang con: co dong trong tblReader7 la ban doc;
     * co dong trong tblStaff7 thi theo position la nhan vien thu vien hoac quan li.
     * @param username ten dang nhap
     * @param password mat khau
     * @return Reader7 / Librarian7 / Manager7 tuong ung; null neu sai thong tin
     */
    public Member7 checkLogin(String username, String password) throws SQLException {
        Member7 kq = null;
        String sql = "SELECT m.*, r.tblReaderType7id, s.position FROM tblMember7 m "
                + "LEFT JOIN tblReader7 r ON r.tblMember7id = m.id "
                + "LEFT JOIN tblStaff7 s ON s.tblMember7id = m.id "
                + "WHERE m.username = ? AND m.password = ?";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getObject("tblReaderType7id") != null) {
                        Reader7 r = new Reader7();
                        ReaderType7 t = new ReaderType7();
                        t.setId(rs.getInt("tblReaderType7id"));
                        r.setReaderType(t);
                        kq = r;
                    } else if ("Nhân viên thư viện".equalsIgnoreCase(rs.getString("position"))) {
                        Librarian7 l = new Librarian7();
                        l.setPosition(rs.getString("position"));
                        kq = l;
                    } else if (rs.getString("position") != null) {
                        Manager7 mg = new Manager7();
                        mg.setPosition(rs.getString("position"));
                        kq = mg;
                    } else {
                        return null;
                    }
                    kq.setId(rs.getInt("id"));
                    kq.setUsername(rs.getString("username"));
                    kq.setDateOfBirth(rs.getDate("dateOfBirth"));
                    kq.setPhone(rs.getString("phone"));
                    kq.setEmail(rs.getString("email"));
                    FullName7 fn = new FullName7();
                    fn.setId(kq.getId());
                    fn.setLastName(rs.getString("lastName"));
                    fn.setMiddleName(rs.getString("middleName"));
                    fn.setFirstName(rs.getString("firstName"));
                    kq.setFullName(fn);
                }
            }
        }
        return kq;
    }
}
