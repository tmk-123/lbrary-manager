<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,java.text.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Phieu muon</title>
</head>
<%
	Librarian7 librarian = (Librarian7) session.getAttribute("librarian");
	if (librarian == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	// -loan : Loan7 (phieu vua luu, da co id), -readerCard : ReaderCard7
	Loan7 loan = (Loan7) session.getAttribute("loan");
	ReaderCard7 readerCard = (ReaderCard7) session.getAttribute("readerCard");
	if (loan == null || loan.getId() == 0) {
		response.sendRedirect("gdMainLibrarian7.jsp");
		return;
	}
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat dtf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	String slipNo = "PM" + new SimpleDateFormat("yyyy-MMdd").format(loan.getBorrowedAt()) + String.format("-%03d", loan.getId());
	FullName7 lf = loan.getLibrarian().getFullName();
	FullName7 rf = loan.getReader().getFullName();
	String libName = lf.getLastName() + " " + (lf.getMiddleName() == null ? "" : lf.getMiddleName() + " ") + lf.getFirstName();
	String readerName = rf.getLastName() + " " + (rf.getMiddleName() == null ? "" : rf.getMiddleName() + " ") + rf.getFirstName();
%>
<body>
<p class="ok">Cho mượn thành công!</p>
<h2> PHIẾU MƯỢN TÀI LIỆU - Số: <%=slipNo %> </h2>
<p>Ngày mượn: <%=dtf.format(loan.getBorrowedAt()) %> - Nhân viên: <%=libName %></p>
<p>Bạn đọc: <%=readerName %> - Thẻ <%=readerCard == null ? "" : readerCard.getCardNumber() %></p>
<!-- -tblLoanItem : Table -->
<table class="grid">
	<tr><th>STT</th><th>Mã vạch</th><th>Tên tài liệu</th><th>Hạn trả</th></tr>
<%
	for (int i = 0; i < loan.getLoanItems().size(); i++) {
		LoanItem7 li = loan.getLoanItems().get(i);
%>
	<tr>
		<td><%=(i + 1) %></td>
		<td><%=li.getDocumentCopy().getBarcode() %></td>
		<td><%=li.getDocumentCopy().getDocument().getTitle() %></td>
		<td><%=df.format(li.getDueDate()) %></td>
	</tr>
<%	} %>
</table>
<p>Tổng số bản: <b><%=loan.getLoanItems().size() %></b></p>
<!-- -btnPrint : button (in bang chuc nang in cua trinh duyet, khong gui len server) -->
<button onclick="window.print()">In</button>
<!-- -btnFinish : link -->
<button onclick="openPage('gdMainLibrarian7.jsp')">Hoàn tất</button>
</body>
</html>
