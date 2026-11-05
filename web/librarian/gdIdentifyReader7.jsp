<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.*,model.*,util.View7"%>
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
	List<LoanItem7> listCurrentLoan = View7.list(request.getAttribute("listCurrentLoan"), LoanItem7.class);
	String message = (String) request.getAttribute("message");
	boolean canContinue = Boolean.TRUE.equals(request.getAttribute("canContinue"));
%>
<body>
<h2> Cho mượn tài liệu - Xác định bạn đọc </h2>
<form name="timbandoc" action="IdentifyReaderServlet7" method="post">
	Số thẻ bạn đọc:
	<input type="text" name="txtCardNumber" value="<%=View7.html(cardNumber == null ? "" : cardNumber)%>" autofocus />
	<input type="submit" name="btnSearch" value="Tìm" />
</form>
<% if (message != null) { %><p class="err"><%=View7.html(message)%></p><% } %>
<%
	if (readerCard != null) {
		Reader7 r = readerCard.getReader();
		FullName7 fn = r.getFullName();
		String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<table border="0">
	<tr><td>Họ tên:</td><td><%=View7.html(name)%></td></tr>
	<tr><td>Loại bạn đọc:</td><td><%=View7.html(r.getReaderType().getName())%> (tối đa <%=View7.html(r.getReaderType().getMaxBooks())%> cuốn, mượn <%=View7.html(r.getReaderType().getLoanDays())%> ngày)</td></tr>
	<tr><td>Đang mượn:</td><td><%=View7.html(listCurrentLoan == null ? 0 : listCurrentLoan.size())%>/<%=View7.html(r.getReaderType().getMaxBooks())%> cuốn</td></tr>
	<tr><td>Ngày sinh:</td><td><%=View7.html(r.getDateOfBirth() == null ? "" : df.format(r.getDateOfBirth()))%></td></tr>
	<tr><td>Điện thoại:</td><td><%=View7.html(r.getPhone())%></td></tr>
	<tr><td>Thẻ:</td><td><%=View7.html(readerCard.getCardNumber())%>, hiệu lực đến <%=View7.html(readerCard.getExpiredAt() == null ? "" : df.format(readerCard.getExpiredAt()))%></td></tr>
</table>
<h3>Tài liệu đang mượn</h3>
<!-- -tblCurrentLoan : Table -->
<table class="grid">
	<tr><th>Mã vạch</th><th>Tên tài liệu</th><th>Hạn trả</th></tr>
<%		if (listCurrentLoan != null)
			for (LoanItem7 li : listCurrentLoan) { %>
	<tr>
		<td><%=View7.html(li.getDocumentCopy().getBarcode())%></td>
		<td><%=View7.html(li.getDocumentCopy().getDocument().getTitle())%></td>
		<td><%=View7.html(df.format(li.getDueDate()))%></td>
	</tr>
<%			} %>
</table>
<%	} %>
<p/>
<form name="tieptuc" action="BorrowDocumentServlet7" method="post" style="display:inline">
	<input type="hidden" name="action" value="new" />
    <input type="hidden" name="readerCardId" value="<%=View7.html(readerCard == null ? 0 : readerCard.getId())%>" />
	<!-- -btnContinue : submit (chi bat khi ban doc hop le) -->
	<input type="submit" name="btnContinue" value="Tiếp tục" <%=View7.html(canContinue ? "" : "disabled")%> />
</form>
<!-- -btnBack : link -->
<button onclick="openPage('gdMainLibrarian7.jsp')">Quay lại</button>
</body>
</html>
