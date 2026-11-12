package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Author7;
import model.Document7;
import model.DocumentAuthor7;
import model.DocumentType7;

// Truy vấn đầu tài liệu và đóng gói kết quả kèm loại tài liệu, tác giả.
public class DocumentDAO7 extends DAO {
    public DocumentDAO7() {
        super();
    }

    /**
     * Tim cac dau tai lieu co ten chua tu khoa (Search Document 7).
     * Moi Document7 tra ve da co loai tai lieu va danh sach tac gia kem vai tro.
     * @param title ten hoac mot phan ten tai lieu
     * @return danh sach tai lieu, rong neu khong co ket qua, SQLException neu loi
     */
    public ArrayList<Document7> searchDocumentByTitle(String title) throws SQLException {
        ArrayList<Document7> kq = new ArrayList<Document7>();
        String sql = "SELECT d.*, t.id AS idType, t.name AS typeName, t.description AS typeDescription, "
                + "da.id AS idDA, da.role, a.id AS idAuthor, a.name AS authorName, a.description AS authorDescription "
                + "FROM tblDocument7 d JOIN tblDocumentType7 t ON t.id = d.tblDocumentType7id "
                + "LEFT JOIN tblDocumentAuthor7 da ON da.tblDocument7id = d.id "
                + "LEFT JOIN tblAuthor7 a ON a.id = da.tblAuthor7id "
                + "WHERE d.title LIKE ? ORDER BY d.title, d.id, da.id";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            // Tìm gần đúng theo tên; PreparedStatement truyền từ khóa như dữ liệu, không ghép vào câu SQL.
            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                Document7 d = null;
                while (rs.next()) {
                    // JOIN tác giả có thể tạo nhiều dòng cho một tài liệu; chỉ tạo Document7 mới khi ID thay đổi.
                    if (d == null || d.getId() != rs.getInt("id")) {
                        d = new Document7();
                        d.setId(rs.getInt("id"));
                        d.setTitle(rs.getString("title"));
                        d.setPublisher(rs.getString("publisher"));
                        d.setPublicationYear(rs.getInt("publicationYear"));
                        d.setCoverPrice(rs.getFloat("coverPrice"));
                        d.setDescription(rs.getString("description"));
                        // loai tai lieu
                        DocumentType7 t = new DocumentType7();
                        t.setId(rs.getInt("idType"));
                        t.setName(rs.getString("typeName"));
                        t.setDescription(rs.getString("typeDescription"));
                        d.setDocumentType(t);
                        d.setDocumentAuthors(new ArrayList<DocumentAuthor7>());
                        kq.add(d);
                    }
                    // tac gia (moi dong cua ket qua la mot tac gia cua tai lieu)
                    if (rs.getInt("idDA") > 0) {
                        Author7 a = new Author7();
                        a.setId(rs.getInt("idAuthor"));
                        a.setName(rs.getString("authorName"));
                        a.setDescription(rs.getString("authorDescription"));
                        DocumentAuthor7 da = new DocumentAuthor7();
                        da.setId(rs.getInt("idDA"));
                        da.setRole(rs.getString("role"));
                        da.setAuthor(a);
                        d.addDocumentAuthor(da);
                    }
                }
            }
        }
        return kq;
    }

    /**
     * Lay day du thong tin mot dau tai lieu (View Document Details 7).
     * @param idDocument id cua tai lieu duoc chon
     * @return Document7 kem loai va cac tac gia; null neu khong ton tai
     */
    public Document7 getDocumentDetail(int idDocument) throws SQLException {
        Document7 kq = null;
        String sql = "SELECT d.*, t.id AS idType, t.name AS typeName, t.description AS typeDescription, "
                + "da.id AS idDA, da.role, a.id AS idAuthor, a.name AS authorName, a.description AS authorDescription "
                + "FROM tblDocument7 d JOIN tblDocumentType7 t ON t.id = d.tblDocumentType7id "
                + "LEFT JOIN tblDocumentAuthor7 da ON da.tblDocument7id = d.id "
                + "LEFT JOIN tblAuthor7 a ON a.id = da.tblAuthor7id "
                + "WHERE d.id = ? ORDER BY da.id";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDocument);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Chỉ tạo đối tượng tài liệu một lần; các dòng tiếp theo bổ sung tác giả cho cùng tài liệu.
                    if (kq == null) {
                        kq = new Document7();
                        kq.setId(rs.getInt("id"));
                        kq.setTitle(rs.getString("title"));
                        kq.setPublisher(rs.getString("publisher"));
                        kq.setPublicationYear(rs.getInt("publicationYear"));
                        kq.setCoverPrice(rs.getFloat("coverPrice"));
                        kq.setDescription(rs.getString("description"));
                        DocumentType7 t = new DocumentType7();
                        t.setId(rs.getInt("idType"));
                        t.setName(rs.getString("typeName"));
                        t.setDescription(rs.getString("typeDescription"));
                        kq.setDocumentType(t);
                        kq.setDocumentAuthors(new ArrayList<DocumentAuthor7>());
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
                        kq.addDocumentAuthor(da);
                    }
                }
            }
        }
        return kq;
    }
}
