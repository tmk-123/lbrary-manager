<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,model.*,util.View7"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Chi tiet tai lieu</title>
</head>
<%
	Reader7 reader = (Reader7) session.getAttribute("reader");
	if (reader == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	// -keyword : String (thuoc tinh an, de quay lai dung danh sach ket qua)
	String keyword = (String) session.getAttribute("keyword");
	if (keyword == null) keyword = "";
	// trang chi hien thi: DocumentDetailServlet7 goi DAO va dat ket qua vao request roi forward ve day
	// -document : Document7, -listCopy : DocumentCopy7[]
	Document7 document = (Document7) request.getAttribute("document");
	List<DocumentCopy7> listCopy = View7.list(request.getAttribute("listCopy"), DocumentCopy7.class);
	if (document == null || listCopy == null) {
		response.sendRedirect("gdSearchDocument7.jsp");
		return;
	}
	int available = 0;
	for (DocumentCopy7 c : listCopy)
		if ("Sẵn có".equals(c.getStatus())) available++;
	String authors = "";
	for (DocumentAuthor7 da : document.getDocumentAuthors())
		authors += (authors.length() > 0 ? ", " : "") + da.getAuthor().getName() + " (" + da.getRole().toLowerCase() + ")";
%>
<body>
<h2> Chi tiết tài liệu </h2>
<table border="0">
	<tr><td>Mã tài liệu:</td><td><%=View7.html(String.format("TL%04d", document.getId()))%></td></tr>
	<tr><td>Tên tài liệu:</td><td><%=View7.html(document.getTitle())%></td></tr>
	<tr><td>Loại tài liệu:</td><td><%=View7.html(document.getDocumentType().getName())%></td></tr>
	<tr><td>Tác giả:</td><td><%=View7.html(authors)%></td></tr>
	<tr><td>Nhà xuất bản:</td><td><%=View7.html(document.getPublisher() == null ? "" : document.getPublisher())%></td></tr>
	<tr><td>Năm xuất bản:</td><td><%=View7.html(document.getPublicationYear())%></td></tr>
	<tr><td>Giá bìa:</td><td><%=View7.html(String.format("%,.0f VNĐ", document.getCoverPrice()))%></td></tr>
	<tr><td>Mô tả:</td><td><%=View7.html(document.getDescription() == null ? "" : document.getDescription())%></td></tr>
</table>
<h3>Các bản tài liệu (sẵn có <%=View7.html(available)%>/<%=View7.html(listCopy.size())%>)</h3>
<!-- -tblCopy : Table -->
<table class="grid">
	<tr><th>TT</th><th>Mã vạch</th><th>Vị trí kệ</th><th>Tình trạng</th><th>Trạng thái</th></tr>
<%
	for (int i = 0; i < listCopy.size(); i++) {
		DocumentCopy7 c = listCopy.get(i);
		Shelf7 s = c.getShelf();
		String shelf = (s == null) ? "Chưa xếp kệ" : "Kệ " + s.getCode() + " - Tầng " + s.getFloor() + " (" + s.getRoom() + ")";
%>
	<tr>
		<td><%=View7.html((i + 1))%></td>
		<td><%=View7.html(c.getBarcode())%></td>
		<td><%=View7.html(shelf)%></td>
		<td><%=View7.html(c.getCondition())%></td>
		<td><%=View7.html(c.getStatus())%></td>
	</tr>
<%	} %>
</table>
<p/>
<!-- -btnBackToResults : link -->
<button onclick="openPage('SearchDocumentServlet7?txtTitle=<%=View7.html(java.net.URLEncoder.encode(keyword, "UTF-8").replace("'", "%27"))%>')">Quay lại kết quả</button>
</body>
</html>
