package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import dao.DocumentCopyDAO;
import dao.DocumentDAO;
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
        String keyword = (String) session.getAttribute("keyword");
        if (keyword == null) keyword = "";
        int idDocument;
        try {
            idDocument = Integer.parseInt(req.getParameter("idDocument"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("gdSearchDocument7.jsp");
            return;
        }
        try {
        Document7 document = (new DocumentDAO()).getDocumentDetail(idDocument);
        if (document == null) {
            resp.sendRedirect("SearchDocumentServlet7?err=notfound&txtTitle=" + java.net.URLEncoder.encode(keyword, "UTF-8"));
            return;
        }
        ArrayList<DocumentCopy7> listCopy = (new DocumentCopyDAO()).getCopiesOfDocument(idDocument);
        
        req.setAttribute("document", document);
        req.setAttribute("listCopy", listCopy);
        req.getRequestDispatcher("gdDocumentDetail7.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "Cannot load document details", e);
            resp.sendRedirect("SearchDocumentServlet7?err=data&txtTitle=" + java.net.URLEncoder.encode(keyword, "UTF-8"));
        }
    }
}
