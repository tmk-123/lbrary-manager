package servlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.MemberDAO7;
import model.Librarian7;
import model.Member7;
import model.Reader7;

// Xác thực tài khoản, tạo phiên đăng nhập và chuyển đến giao diện theo vai trò.
@WebServlet("/LoginServlet7")
public class LoginServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(LoginServlet7.class.getName());
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Nhận thông tin đăng nhập và từ chối dữ liệu bỏ trống trước khi truy vấn.
        String username = req.getParameter("username"), password = req.getParameter("password");
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            resp.sendRedirect("gdLogin7.jsp?err=fail"); return;
        }
        try {
            Member7 member = new MemberDAO7().checkLogin(username.trim(), password);
            if (member == null) { resp.sendRedirect("gdLogin7.jsp?err=fail"); return; }
            // Hủy phiên cũ sau khi xác thực thành công, rồi tạo session mới theo vai trò.
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) oldSession.invalidate();
            if (member instanceof Reader7) {
                req.getSession(true).setAttribute("reader", member);
                resp.sendRedirect("reader/gdMainReader7.jsp");
            } else if (member instanceof Librarian7) {
                req.getSession(true).setAttribute("librarian", member);
                resp.sendRedirect("librarian/gdMainLibrarian7.jsp");
            } else resp.sendRedirect("gdLogin7.jsp?err=role");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Cannot authenticate member", e);
            resp.sendRedirect("gdLogin7.jsp?err=data");
        }
    }
    @Override
    // Truy cập Servlet bằng GET chỉ đưa về trang đăng nhập.
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("gdLogin7.jsp");
    }
}
