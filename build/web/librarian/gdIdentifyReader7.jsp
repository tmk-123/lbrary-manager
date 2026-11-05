<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Xac dinh ban doc</title>
</head>
<%
	Librarian7 librarian = (Librarian7) session.getAttribute("librarian");
	if (librarian == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	// trang chi hien thi: IdentifyReaderServlet7 tim the, kiem tra va dat ket qua vao request roi forward ve day
	// (mo truc tiep trang thi chua co ket qua: chi co o nhap so the, nut Tiep tuc bi mo)
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	// -txtCardNumber : Text
	String cardNumber = (String) request.getAttribute("txtCardNumber");
	// -readerCard : ReaderCard7, -listCurrentLoan : LoanItem7[]
	ReaderCard7 readerCard = (ReaderCard7) request.getAttribute("readerCard");
	ArrayList<LoanItem7> listCurrentLoan = (ArrayList<LoanItem7>) request.getAttribute("listCurrentLoan");
	String message = (String) request.getAttribute("message");
	boolean canContinue = Boolean.TRUE.equals(request.getAttribute("canContinue"));
%>
<body>
<h2> Cho mượn tài liệu - Xác định bạn đọc </h2>
<form name="timbandoc" action="IdentifyReaderServlet7" method="post">
	Số thẻ bạn đọc:
	<input type="text" name="txtCardNumber" value="<%=cardNumber == null ? "" : cardNumber %>" autofocus />
	<input type="submit" name="btnSearch" value="Tìm" />
</form>
<% if (message != null) { %><p class="err"><%=message %></p><% } %>
<%
	if (readerCard != null) {
		Reader7 r = readerCard.getReader();
		FullName7 fn = r.getFullName();
		String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<table border="0">
	<tr><td>Họ tên:</td><td><%=name %></td></tr>
	<tr><td>Loại bạn đọc:</td><td><%=r.getReaderType().getName() %> (tối đa <%=r.getReaderType().getMaxBooks() %> cuốn, mượn <%=r.getReaderType().getLoanDays() %> ngày)</td></tr>
	<tr><td>Đang mượn:</td><td><%=listCurrentLoan == null ? 0 : listCurrentLoan.size() %>/<%=r.getReaderType().getMaxBooks() %> cuốn</td></tr>
	<tr><td>Ngày sinh:</td><td><%=r.getDateOfBirth() == null ? "" : df.format(r.getDateOfBirth()) %></td></tr>
	<tr><td>Điện thoại:</td><td><%=r.getPhone() %></td></tr>
	<tr><td>Thẻ:</td><td><%=readerCard.getCardNumber() %>, hiệu lực đến <%=readerCard.getExpiredAt() == null ? "" : df.format(readerCard.getExpiredAt()) %></td></tr>
</table>
<h3>Tài liệu đang mượn</h3>
<!-- -tblCurrentLoan : Table -->
<table class="grid">
	<tr><th>Mã vạch</th><th>Tên tài liệu</th><th>Hạn trả</th></tr>
<%		if (listCurrentLoan != null)
			for (LoanItem7 li : listCurrentLoan) { %>
	<tr>
		<td><%=li.getDocumentCopy().getBarcode() %></td>
		<td><%=li.getDocumentCopy().getDocument().getTitle() %></td>
		<td><%=df.format(li.getDueDate()) %></td>
	</tr>
<%			} %>
</table>
<%	} %>
<p/>
<form name="tieptuc" action="BorrowDocumentServlet7" method="post" style="display:inline">
	<input type="hidden" name="action" value="new" />
	<!-- -btnContinue : submit (chi bat khi ban doc hop le) -->
	<input type="submit" name="btnContinue" value="Tiếp tục" <%=canContinue ? "" : "disabled" %> />
</form>
<!-- -btnBack : link -->
<button onclick="openPage('gdMainLibrarian7.jsp')">Quay lại</button>
</body>
</html>
