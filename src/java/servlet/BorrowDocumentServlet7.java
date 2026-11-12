package servlet;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import dao.DocumentCopyDAO7;
import model.DocumentCopy7;
import model.Librarian7;
import model.Loan7;
import model.LoanItem7;
import model.ReaderCard7;
import util.BorrowingRules7;
import util.View7;

// Điều khiển lượt mượn: tạo phiếu tạm, thêm/bỏ bản tài liệu, hủy hoặc hoàn tất.
@WebServlet("/librarian/BorrowDocumentServlet7")
public class BorrowDocumentServlet7 extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(BorrowDocumentServlet7.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("librarian") == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout"); return;
        }
        synchronized (session) {
            // Phân biệt thao tác bằng action: new, add, remove, cancel hoặc finish.
            String action = req.getParameter("action");
            ReaderCard7 card = (ReaderCard7) session.getAttribute("readerCard");
            Loan7 loan = (Loan7) session.getAttribute("loan");
            // Tạo phiếu tạm cho bạn đọc vừa xác định và sinh token phân biệt lượt mượn này với các form cũ.
            if ("new".equals(action)) {
                if (card == null || !Integer.toString(card.getId()).equals(req.getParameter("readerCardId"))) {
                    resp.sendError(409, "Thông tin thẻ đã thay đổi. Vui lòng xác định lại bạn đọc."); return;
                }
                loan = new Loan7(); loan.setReader(card.getReader());
                loan.setLibrarian((Librarian7) session.getAttribute("librarian"));
                session.setAttribute("loan", loan); session.setAttribute("loanToken", UUID.randomUUID().toString());
                session.removeAttribute("borrowMessage");
                resp.sendRedirect("BorrowDocumentServlet7"); return;
            }
            // Từ chối form cũ hoặc form thuộc lượt mượn khác khi token không trùng session hiện tại.
            String token = (String) session.getAttribute("loanToken");
            if (loan == null || token == null || !token.equals(req.getParameter("loanToken"))) {
                resp.sendError(409, "Phiếu mượn đã thay đổi. Vui lòng mở lại lượt mượn hiện tại."); return;
            }
            // Hoàn tất phiếu đã lưu hoặc hủy phiếu chưa lưu: dọn session rồi về trang chính, không xóa phiếu trong CSDL.
            if ("finish".equals(action) && loan.isSaved() || "cancel".equals(action) && !loan.isSaved()) {
                clearDraft(session); resp.sendRedirect("gdMainLibrarian7.jsp"); return;
            }
            if (loan.isSaved()) { resp.sendRedirect("gdLoanSlip7.jsp"); return; }
            if (card == null || card.getReader().getId() != loan.getReader().getId()) {
                clearDraft(session); resp.sendRedirect("IdentifyReaderServlet7"); return;
            }
            String message;
            // Thêm bản bằng mã vạch; thao tác remove bên dưới chỉ bỏ bản khỏi danh sách phiếu tạm.
            if ("add".equals(action)) {
                try {
                    message = addCopy(loan, card, View7.list(session.getAttribute("listCurrentLoan"), LoanItem7.class), req.getParameter("txtBarcode"));
                } catch (SQLException e) {
                    LOG.log(Level.WARNING, "Cannot select document copy", e);
                    message = "Không thể tải bản tài liệu, vui lòng thử lại";
                }
            } else if ("remove".equals(action)) {
                try {
                    message = loan.removeLoanItem(Integer.parseInt(req.getParameter("idCopy"))) ? null : "Bản không còn trong danh sách";
                } catch (NumberFormatException e) { resp.sendError(400, "Mã bản không hợp lệ"); return; }
            } else { resp.sendError(400, "Thao tác không hợp lệ"); return; }
            // Sau POST chuyển hướng về GET để tải lại trang mà không gửi lại thao tác khi người dùng refresh.
            session.setAttribute("borrowMessage", message);
            resp.sendRedirect("BorrowDocumentServlet7");
        }
    }

    @Override
    // Hiển thị phiếu tạm hiện tại; phiếu đã lưu được chuyển sang màn hình phiếu mượn.
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("librarian") == null) {
            resp.sendRedirect("../gdLogin7.jsp?err=timeout"); return;
        }
        if (req.getParameter("action") != null) { resp.sendError(405, "Thao tác này cần POST"); return; }
        synchronized (session) {
            Loan7 loan = (Loan7) session.getAttribute("loan");
            if (loan == null) { resp.sendRedirect("IdentifyReaderServlet7"); return; }
            if (loan.isSaved()) { resp.sendRedirect("gdLoanSlip7.jsp"); return; }
            req.setAttribute("message", session.getAttribute("borrowMessage")); session.removeAttribute("borrowMessage");
            req.getRequestDispatcher("gdBorrowDocument7.jsp").forward(req, resp);
        }
    }

    // Trả null khi thêm thành công; trả chuỗi thông báo khi mã vạch hoặc điều kiện mượn không hợp lệ.
    private String addCopy(Loan7 loan, ReaderCard7 card, List<LoanItem7> current, String barcode) throws SQLException {
        if (barcode == null || barcode.isBlank()) return "Vui lòng quét hoặc nhập mã vạch";
        String validation = BorrowingRules7.validate(card, current, loan.getLoanItems().size() + 1, BorrowingRules7.today());
        if (validation != null) return validation;
        for (LoanItem7 item : loan.getLoanItems())
            if (item.getDocumentCopy().getBarcode().equalsIgnoreCase(barcode.trim())) return "Bản đã có trong danh sách mượn";
        // Đọc bản từ CSDL và chỉ cho chọn bản đang Sẵn có; trạng thái vẫn được kiểm tra lại lúc lưu.
        DocumentCopy7 copy = new DocumentCopyDAO7().getCopyByBarcode(barcode.trim());
        if (copy == null) return "Không tìm thấy bản tài liệu có mã " + barcode.trim();
        if (!"Sẵn có".equals(copy.getStatus())) return "Bản " + copy.getBarcode() + " không ở trạng thái sẵn có";
        LoanItem7 item = new LoanItem7(); item.setDocumentCopy(copy);
        item.setDueDate(BorrowingRules7.dueDate(new Date(), card.getReader().getReaderType().getLoanDays()));
        loan.addLoanItem(item);
        return null;
    }

    // Chỉ xóa dữ liệu lượt mượn trong session, giữ phiên đăng nhập và lịch sử đã lưu trong CSDL.
    private void clearDraft(HttpSession session) {
        for (String key : new String[]{"loan", "loanToken", "readerCard", "listCurrentLoan", "borrowMessage"}) session.removeAttribute(key);
    }
}
