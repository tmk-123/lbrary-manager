<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.*,model.*,util.View7"%>
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
	List<LoanItem7> listCurrentLoan = View7.list(session.getAttribute("listCurrentLoan"), LoanItem7.class);
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
	if (loan != null && loan.isSaved()) { response.sendRedirect("gdLoanSlip7.jsp"); return; }
    if (loan == null) {
		response.sendRedirect("gdIdentifyReader7.jsp");
		return;
	}
	String loanToken = (String) session.getAttribute("loanToken");
    String message = (String) request.getAttribute("message");
	FullName7 fn = loan.getReader().getFullName();
	String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<body>
<h2> Cho mượn tài liệu - Danh sách mượn </h2>
<p>Bạn đọc: <%=View7.html(name)%> - Thẻ <%=View7.html(readerCard.getCardNumber())%> - Đang mượn: <%=View7.html(currentCount)%>/<%=View7.html(readerType.getMaxBooks())%> cuốn</p>
<form name="themban" action="BorrowDocumentServlet7" method="post">
	<input type="hidden" name="action" value="add" />
    <input type="hidden" name="loanToken" value="<%=View7.html(loanToken)%>" />
	Mã vạch bản tài liệu:
	<input type="text" name="txtBarcode" autofocus />
	<input type="submit" name="btnAdd" value="Thêm" />
</form>
<% if (message != null) { %><p class="err"><%=View7.html(message)%></p><% } %>
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
		<td><%=View7.html((i + 1))%></td>
		<td><%=View7.html(li.getDocumentCopy().getBarcode())%></td>
		<td><%=View7.html(d.getTitle())%></td>
		<td><%=View7.html(authors)%></td>
		<td><%=View7.html(df.format(li.getDueDate()))%></td>
		<td><form action="BorrowDocumentServlet7" method="post"><input type="hidden" name="action" value="remove"/><input type="hidden" name="loanToken" value="<%=View7.html(loanToken)%>"/><input type="hidden" name="idCopy" value="<%=View7.html(li.getDocumentCopy().getId())%>"/><button type="submit" name="remove">Bỏ</button></form></td>
	</tr>
<%	} %>
</table>
<p>Tổng số bản mượn: <b><%=View7.html(loan.getLoanItems().size())%></b></p>
<form name="luuphieu" action="SaveLoanServlet7" method="post" style="display:inline">
	<input type="hidden" name="loanToken" value="<%=View7.html(loanToken)%>" />
	<!-- -btnSubmit : submit -->
	<input type="submit" name="btnSubmit" value="Lưu" />
</form>
<!-- -btnCancel : submit -->
<form action="BorrowDocumentServlet7" method="post" style="display:inline" onsubmit="return confirm('Hủy danh sách mượn đang lập?')"><input type="hidden" name="action" value="cancel"/><input type="hidden" name="loanToken" value="<%=View7.html(loanToken)%>"/><button type="submit" name="btnCancel">Hủy</button></form>
</body>
</html>
