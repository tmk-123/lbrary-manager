package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import dao.DocumentDAO7;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Document7;
import model.Reader7;

/**
 * Tim tai lieu theo ten (Search Document 7).
 * Nhan txtTitle tu gdSearchDocument7.jsp (btnSearch) hoac tu nut "Quay lai ket qua" cua trang chi tiet,
 * goi DocumentDAO.searchDocumentByTitle() roi chuyen ket qua ve gdSearchDocument7.jsp de hien thi.
 */
@WebServlet("/reader/SearchDocumentServlet7")
public class SearchDocumentServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SearchDocumentServlet7.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        Reader7 reader = session == null ? null : (Reader7) session.getAttribute("reader");
        if (reader == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout");
            return;
        }
        // Đọc txtTitle từ form tìm kiếm hoặc URL quay lại kết quả của trang chi tiết.
        String title = req.getParameter("txtTitle");
        ArrayList<Document7> listDocument = null;
        String message = null;
        if (title == null || title.trim().length() == 0) {
            message = "Vui lòng nhập tên tài liệu";
        } else {
            try {
                listDocument = new DocumentDAO7().searchDocumentByTitle(title.trim());
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Cannot search documents", e);
                message = "Không thể tải dữ liệu, vui lòng thử lại";
            }
            if (listDocument == null) {
                message = "Không thể tải dữ liệu, vui lòng thử lại";
            } else if (listDocument.size() == 0) {
                message = "Không tìm thấy tài liệu có tên chứa '" + title.trim() + "'";
            }
            // giu tu khoa de trang chi tiet quay lai dung ket qua
            session.setAttribute("keyword", title.trim());
        }
        if ("data".equals(req.getParameter("err"))) {
            message = "Không thể tải chi tiết tài liệu, vui lòng thử lại";
        } else if ("notfound".equals(req.getParameter("err"))) {
            message = "Tài liệu không còn tồn tại";
        }
        // Giữ danh sách trong session, còn ô nhập và thông báo được truyền qua request cho JSP.
        session.setAttribute("listDocument", listDocument);
        req.setAttribute("txtTitle", title == null ? "" : title);
        req.setAttribute("message", message);
        req.getRequestDispatcher("gdSearchDocument7.jsp").forward(req, resp);
    }

    @Override
    // POST dùng lại luồng GET để hai cách gửi yêu cầu có cùng xử lý tìm kiếm.
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
