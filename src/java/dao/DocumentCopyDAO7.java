package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Author7;
import model.Document7;
import model.DocumentAuthor7;
import model.DocumentCopy7;
import model.DocumentType7;
import model.Shelf7;

// Truy vấn các bản tài liệu theo đầu tài liệu hoặc mã vạch để xem chi tiết và lập phiếu mượn.
public class DocumentCopyDAO7 extends DAO {
    public DocumentCopyDAO7() {
        super();
    }

    /**
     * Lay cac ban cua mot dau tai lieu, kem ke dat tung ban (View Document Details 7).
     * @param idDocument id cua tai lieu
     * @return danh sach ban tai lieu, SQLException neu loi
     */
    public ArrayList<DocumentCopy7> getCopiesOfDocument(int idDocument) throws SQLException {
        ArrayList<DocumentCopy7> kq = new ArrayList<DocumentCopy7>();
        // LEFT JOIN kệ để vẫn lấy được bản tài liệu chưa được xếp kệ.
        String sql = "SELECT c.*, s.code, s.room, s.floor, s.description AS shelfDescription "
                + "FROM tblDocumentCopy7 c LEFT JOIN tblShelf7 s ON s.id = c.tblShelf7id "
                + "WHERE c.tblDocument7id = ? ORDER BY c.barcode";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDocument);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DocumentCopy7 c = new DocumentCopy7();
                    c.setId(rs.getInt("id"));
                    c.setBarcode(rs.getString("barcode"));
                    c.setCondition(rs.getString("condition"));
                    c.setStatus(rs.getString("status"));
                    Document7 d = new Document7();
                    d.setId(idDocument);
                    c.setDocument(d);
                    // ke (co the chua xep ke)
                    if (rs.getInt("tblShelf7id") > 0) {
                        Shelf7 s = new Shelf7();
                        s.setId(rs.getInt("tblShelf7id"));
                        s.setCode(rs.getString("code"));
                        s.setRoom(rs.getString("room"));
                        s.setFloor(rs.getInt("floor"));
                        s.setDescription(rs.getString("shelfDescription"));
                        c.setShelf(s);
                    }
                    kq.add(c);
                }
            }
        }
        return kq;
    }

    /**
     * Tim ban tai lieu theo ma vach quet/nhap (Select Document Copy 7).
     * @param barcode ma vach
     * @return ban tai lieu kem dau tai lieu (loai, tac gia) va ke; null neu khong ton tai
     */
    public DocumentCopy7 getCopyByBarcode(String barcode) throws SQLException {
        DocumentCopy7 kq = null;
        String sql = "SELECT c.*, s.code, s.room, s.floor, s.description AS shelfDescription, "
                + "d.title, d.publisher, d.publicationYear, d.coverPrice, d.description, "
                + "t.id AS idType, t.name AS typeName, t.description AS typeDescription, "
                + "da.id AS idDA, da.role, a.id AS idAuthor, a.name AS authorName, a.description AS authorDescription "
                + "FROM tblDocumentCopy7 c JOIN tblDocument7 d ON d.id = c.tblDocument7id "
                + "JOIN tblDocumentType7 t ON t.id = d.tblDocumentType7id "
                + "LEFT JOIN tblShelf7 s ON s.id = c.tblShelf7id "
                + "LEFT JOIN tblDocumentAuthor7 da ON da.tblDocument7id = d.id "
                + "LEFT JOIN tblAuthor7 a ON a.id = da.tblAuthor7id "
                + "WHERE c.barcode = ? ORDER BY da.id";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, barcode.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Một bản có thể có nhiều dòng do JOIN tác giả; chỉ khởi tạo bản và đầu tài liệu một lần.
                    if (kq == null) {
                        kq = new DocumentCopy7();
                        kq.setId(rs.getInt("id"));
                        kq.setBarcode(rs.getString("barcode"));
                        kq.setCondition(rs.getString("condition"));
                        kq.setStatus(rs.getString("status"));
                        // dau tai lieu
                        Document7 d = new Document7();
                        d.setId(rs.getInt("tblDocument7id"));
                        d.setTitle(rs.getString("title"));
                        d.setPublisher(rs.getString("publisher"));
                        d.setPublicationYear(rs.getInt("publicationYear"));
                        d.setCoverPrice(rs.getFloat("coverPrice"));
                        d.setDescription(rs.getString("description"));
                        DocumentType7 t = new DocumentType7();
                        t.setId(rs.getInt("idType"));
                        t.setName(rs.getString("typeName"));
                        t.setDescription(rs.getString("typeDescription"));
                        d.setDocumentType(t);
                        d.setDocumentAuthors(new ArrayList<DocumentAuthor7>());
                        kq.setDocument(d);
                        // ke
                        if (rs.getInt("tblShelf7id") > 0) {
                            Shelf7 s = new Shelf7();
                            s.setId(rs.getInt("tblShelf7id"));
                            s.setCode(rs.getString("code"));
                            s.setRoom(rs.getString("room"));
                            s.setFloor(rs.getInt("floor"));
                            s.setDescription(rs.getString("shelfDescription"));
                            kq.setShelf(s);
                        }
                    }
                    if (rs.getInt("idDA") > 0) {
                        Author7 a = new Author7();
                        a.setId(rs.getInt("idAuthor"));
                        a.setName(rs.getString("authorName"));
                        a.setDescription(rs.getString("authorDescription"));
                        DocumentAuthor7 da = new DocumentAuthor7();
                        da.setId(rs.getInt("idDA"));
                        da.setRole(rs.getString("role"));
                        da.setAuthor(a);
                        kq.getDocument().addDocumentAuthor(da);
                    }
                }
            }
        }
        return kq;
    }
}
