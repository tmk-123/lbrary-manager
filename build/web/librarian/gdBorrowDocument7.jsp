<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Lap danh sach muon</title>
</head>
<%
	Librarian7 librarian = (Librarian7) session.getAttribute("librarian");
	if (librarian == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	// -readerCard : ReaderCard7, -listCurrentLoan : LoanItem7[] (lay tu trang xac dinh ban doc)
	ReaderCard7 readerCard = (ReaderCard7) session.getAttribute("readerCard");
	ArrayList<LoanItem7> listCurrentLoan = (ArrayList<LoanItem7>) session.getAttribute("listCurrentLoan");
	if (readerCard == null) {
		response.sendRedirect("gdIdentifyReader7.jsp");
		return;
	}
	// quy dinh muon theo loai ban doc: so cuon toi da, so ngay duoc muon
	ReaderType7 readerType = readerCard.getReader().getReaderType();
	int currentCount = (listCurrentLoan == null) ? 0 : listCurrentLoan.size();
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	// trang chi hien thi: BorrowDocumentServlet7 tao phieu, them/bo dong roi forward ve day
	// -loan : Loan7 (phieu dang lap, danh sach muon tam = loan.loanItems)
	Loan7 loan = (Loan7) session.getAttribute("loan");
	if (loan == null) {
		response.sendRedirect("gdIdentifyReader7.jsp");
		return;
	}
	String message = (String) request.getAttribute("message");
	FullName7 fn = loan.getReader().getFullName();
	String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<body>
<h2> Cho mượn tài liệu - Danh sách mượn </h2>
<p>Bạn đọc: <%=name %> - Thẻ <%=readerCard.getCardNumber() %> - Đang mượn: <%=currentCount %>/<%=readerType.getMaxBooks() %> cuốn</p>
<form name="themban" action="BorrowDocumentServlet7" method="post">
	<input type="hidden" name="action" value="add" />
	Mã vạch bản tài liệu:
	<input type="text" name="txtBarcode" autofocus />
	<input type="submit" name="btnAdd" value="Thêm" />
</form>
<% if (message != null) { %><p class="err"><%=message %></p><% } %>
<!-- -tblBorrowList : Table -->
<table class="grid">
	<tr><th>TT</th><th>Mã vạch</th><th>Tên tài liệu</th><th>Tác giả</th><th>Hạn trả</th><th>Bỏ</th></tr>
<%
	for (int i = 0; i < loan.getLoanItems().size(); i++) {
		LoanItem7 li = loan.getLoanItems().get(i);
		Document7 d = li.getDocumentCopy().getDocument();
		String authors = "";
		for (DocumentAuthor7 da : d.getDocumentAuthors())
			authors += (authors.length() > 0 ? ", " : "") + da.getAuthor().getName();
%>
	<tr>
		<td><%=(i + 1) %></td>
		<td><%=li.getDocumentCopy().getBarcode() %></td>
		<td><%=d.getTitle() %></td>
		<td><%=authors %></td>
		<td><%=df.format(li.getDueDate()) %></td>
		<td><a href="BorrowDocumentServlet7?action=remove&idCopy=<%=li.getDocumentCopy().getId() %>">Bỏ</a></td>
	</tr>
<%	} %>
</table>
<p>Tổng số bản mượn: <b><%=loan.getLoanItems().size() %></b></p>
<form name="luuphieu" action="SaveLoanServlet7" method="post" style="display:inline">
	<!-- -btnSubmit : submit -->
	<input type="submit" name="btnSubmit" value="Lưu" />
</form>
<!-- -btnCancel : link -->
<button onclick="if (confirm('Hủy danh sách mượn đang lập?')) openPage('BorrowDocumentServlet7?action=cancel')">Hủy</button>
</body>
</html>
