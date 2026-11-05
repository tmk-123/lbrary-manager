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
import dao.LoanDAO;
import model.Librarian7;
import model.Loan7;
import model.ReaderCard7;

@WebServlet("/librarian/SaveLoanServlet7")
public class SaveLoanServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SaveLoanServlet7.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("librarian") == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout"); return;
        }
        synchronized (session) {
            Loan7 loan = (Loan7) session.getAttribute("loan");
            ReaderCard7 card = (ReaderCard7) session.getAttribute("readerCard");
            Librarian7 librarian = (Librarian7) session.getAttribute("librarian");
            String token = (String) session.getAttribute("loanToken");
            if (loan == null || card == null || token == null || !token.equals(req.getParameter("loanToken"))
                    || loan.getReader().getId() != card.getReader().getId()
                    || loan.getLibrarian().getId() != librarian.getId()) {
                resp.sendError(409, "Thông tin phiếu đã thay đổi. Vui lòng xác định lại bạn đọc."); return;
            }
            if (loan.isSaved()) { resp.sendRedirect("gdLoanSlip7.jsp"); return; }
            String message = "Chưa có tài liệu nào trong danh sách mượn";
            if (!loan.getLoanItems().isEmpty()) {
                try {
                    if (new LoanDAO().addLoan(loan, card)) { resp.sendRedirect("gdLoanSlip7.jsp"); return; }
                    message = "Điều kiện mượn hoặc trạng thái bản đã thay đổi. Vui lòng kiểm tra lại bạn đọc và tài liệu.";
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, "Cannot store loan", e);
                    // A Connection.close error after commit must not report a committed loan as failed.
                    if (loan.isSaved()) { resp.sendRedirect("gdLoanSlip7.jsp"); return; }
                    message = "Không thể ghi nhận phiếu mượn, vui lòng thử lại";
                }
            }
            session.setAttribute("borrowMessage", message);
            resp.sendRedirect("BorrowDocumentServlet7");
        }
    }
}
