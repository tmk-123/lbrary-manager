package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Address7;
import model.FullName7;
import model.Reader7;
import model.ReaderCard7;
import model.ReaderType7;

public class ReaderCardDAO extends DAO {
    public ReaderCardDAO() {
        super();
    }

    /**
     * Tim the ban doc theo so the quet/nhap (Identify Reader 7).
     * @param cardNumber so the
     * @return the kem ban doc (ho ten, dia chi, loai ban doc); null neu khong ton tai
     */
    public ReaderCard7 getReaderCardByNumber(String cardNumber) throws SQLException {
        try (Connection con = getConnection()) { return getReaderCardByNumber(con, cardNumber); }
    }

    ReaderCard7 getReaderCardByNumber(Connection con, String cardNumber) throws SQLException {
        ReaderCard7 kq = null;
        String sql = "SELECT c.*, t.id AS idType, t.name AS typeName, t.maxBooks, t.loanDays, t.description AS typeDescription, m.username, m.lastName, m.middleName, m.firstName, "
                + "m.dateOfBirth, m.phone, m.email, a.id AS idAddress, a.houseNumber, a.street, a.ward, a.province "
                + "FROM tblReaderCard7 c JOIN tblReader7 r ON r.tblMember7id = c.tblReader7id "
                + "JOIN tblReaderType7 t ON t.id = r.tblReaderType7id "
                + "JOIN tblMember7 m ON m.id = r.tblMember7id "
                + "LEFT JOIN tblAddress7 a ON a.id = m.tblAddress7id "
                + "WHERE c.cardNumber = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cardNumber.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kq = new ReaderCard7();
                    kq.setId(rs.getInt("id"));
                    kq.setCardNumber(rs.getString("cardNumber"));
                    kq.setIssuedAt(rs.getDate("issuedAt"));
                    kq.setExpiredAt(rs.getDate("expiredAt"));
                    kq.setStatus(rs.getString("status"));
                    // ban doc
                    Reader7 r = new Reader7();
                    r.setId(rs.getInt("tblReader7id"));
                    // loai ban doc (quy dinh so cuon toi da, thoi han muon)
                    ReaderType7 t = new ReaderType7();
                    t.setId(rs.getInt("idType"));
                    t.setName(rs.getString("typeName"));
                    t.setMaxBooks(rs.getInt("maxBooks"));
                    t.setLoanDays(rs.getInt("loanDays"));
                    t.setDescription(rs.getString("typeDescription"));
                    r.setReaderType(t);
                    r.setUsername(rs.getString("username"));
                    r.setDateOfBirth(rs.getDate("dateOfBirth"));
                    r.setPhone(rs.getString("phone"));
                    r.setEmail(rs.getString("email"));
                    FullName7 fn = new FullName7();
                    fn.setId(r.getId());
                    fn.setLastName(rs.getString("lastName"));
                    fn.setMiddleName(rs.getString("middleName"));
                    fn.setFirstName(rs.getString("firstName"));
                    r.setFullName(fn);
                    if (rs.getInt("idAddress") > 0) {
                        Address7 ad = new Address7();
                        ad.setId(rs.getInt("idAddress"));
                        ad.setHouseNumber(rs.getString("houseNumber"));
                        ad.setStreet(rs.getString("street"));
                        ad.setWard(rs.getString("ward"));
                        ad.setProvince(rs.getString("province"));
                        r.setAddress(ad);
                    }
                    kq.setReader(r);
                }
            }
        }
        return kq;
    }
}
