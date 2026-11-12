package servlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.LoanItemDAO7;
import dao.ReaderCardDAO7;
import model.LoanItem7;
import model.ReaderCard7;
import util.BorrowingRules7;

// Xác định bạn đọc qua số thẻ và kiểm tra điều kiện trước khi mở lượt mượn.
@WebServlet("/librarian/IdentifyReaderServlet7")
public class IdentifyReaderServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(IdentifyReaderServlet7.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Chỉ tiếp tục nếu phiên đăng nhập còn tồn tại và người dùng là nhân viên thư viện.
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("librarian") == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout"); return;
        }
        // Xử lý tuần tự các yêu cầu trên cùng session để tránh hai tab thay đổi lượt mượn cùng lúc.
        synchronized (session) {
            // A newly selected reader invalidates all forms belonging to the old draft.
            session.removeAttribute("loan"); session.removeAttribute("loanToken"); session.removeAttribute("borrowMessage");
            session.removeAttribute("readerCard"); session.removeAttribute("listCurrentLoan");
            String cardNumber = req.getParameter("txtCardNumber");
            ReaderCard7 card = null;
            ArrayList<LoanItem7> current = new ArrayList<>();
            String message = null;
            boolean canContinue = false;
            if (cardNumber == null || cardNumber.isBlank()) message = "Vui lòng quét hoặc nhập số thẻ";
            else {
                try {
                    card = new ReaderCardDAO7().getReaderCardByNumber(cardNumber.trim());
                    if (card == null) message = "Không tìm thấy bạn đọc có số thẻ " + cardNumber.trim();
                    else {
                        // Lấy các bản chưa trả và kiểm tra hiệu lực thẻ, quá hạn, hạn mức mượn.
                        current = new LoanItemDAO7().getUnreturnedItems(card.getReader().getId());
                        message = BorrowingRules7.validate(card, current, 1, BorrowingRules7.today());
                        canContinue = message == null;
                    }
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, "Cannot identify reader", e);
                    message = "Không thể kiểm tra dữ liệu mượn, vui lòng thử lại";
                }
            }
            // Chỉ giữ bạn đọc đủ điều kiện trong session để màn hình tiếp theo dùng.
            if (canContinue) {
                session.setAttribute("readerCard", card); session.setAttribute("listCurrentLoan", current);
            }
            // Dữ liệu request dùng để hiển thị lại kết quả kiểm tra và thông báo trên cùng màn hình.
            req.setAttribute("txtCardNumber", cardNumber); req.setAttribute("readerCard", card);
            req.setAttribute("listCurrentLoan", current); req.setAttribute("message", message);
            req.setAttribute("canContinue", canContinue);
            req.getRequestDispatcher("gdIdentifyReader7.jsp").forward(req, resp);
        }
    }

    @Override
    // GET chỉ mở màn hình xác định bạn đọc; thao tác tìm thẻ được xử lý bằng POST.
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("librarian") == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout"); return;
        }
        req.getRequestDispatcher("gdIdentifyReader7.jsp").forward(req, resp);
    }
}
