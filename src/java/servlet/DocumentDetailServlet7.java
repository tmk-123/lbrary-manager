package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import dao.DocumentCopyDAO7;
import dao.DocumentDAO7;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Document7;
import model.DocumentCopy7;
import model.Reader7;

/**
 * Xem chi tiet tai lieu (View Document Details 7).
 * Nhan idDocument tu link "Chi tiet" cua gdSearchDocument7.jsp, goi DocumentDAO.getDocumentDetail()
 * va DocumentCopyDAO.getCopiesOfDocument() roi chuyen sang gdDocumentDetail7.jsp de hien thi.
 */
@WebServlet("/reader/DocumentDetailServlet7")
public class DocumentDetailServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(DocumentDetailServlet7.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Reader7 reader = session == null ? null : (Reader7) session.getAttribute("reader");
        if (reader == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout");
            return;
        }
        // Giữ từ khóa trong session để nút quay lại phục hồi đúng kết quả tìm kiếm.
        String keyword = (String) session.getAttribute("keyword");
        if (keyword == null) keyword = "";
        int idDocument;
        try {
            // Chỉ nhận mã tài liệu là số; mã sai định dạng được đưa về màn hình tìm kiếm.
            idDocument = Integer.parseInt(req.getParameter("idDocument"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("gdSearchDocument7.jsp");
            return;
        }
        try {
        // Lấy đầu tài liệu trước, sau đó lấy các bản vật lý để hiển thị mã vạch, trạng thái và vị trí.
        Document7 document = (new DocumentDAO7()).getDocumentDetail(idDocument);
        if (document == null) {
            // Tài liệu không tồn tại → chuyển tới SearchDocumentServlet7?err=notfound&txtTitle=....
            resp.sendRedirect("SearchDocumentServlet7?err=notfound&txtTitle=" + java.net.URLEncoder.encode(keyword, "UTF-8"));
            return;
        }
        ArrayList<DocumentCopy7> listCopy = (new DocumentCopyDAO7()).getCopiesOfDocument(idDocument);
        
        // Truyền dữ liệu qua request rồi forward sang JSP; JSP chỉ hiển thị kết quả.
        req.setAttribute("document", document);
        req.setAttribute("listCopy", listCopy);
        req.getRequestDispatcher("gdDocumentDetail7.jsp").forward(req, resp);
        } catch (SQLException e) {
            // Lỗi CSDL → chuyển tới SearchDocumentServlet7?err=data&txtTitle=....
            // SearchDocumentServlet7 sẽ tìm lại theo từ khóa, đặt thông báo lỗi rồi chuyển sang JSP hiển thị.
            LOG.log(Level.WARNING, "Cannot load document details", e);
            resp.sendRedirect("SearchDocumentServlet7?err=data&txtTitle=" + java.net.URLEncoder.encode(keyword, "UTF-8"));
        }
    }
}
