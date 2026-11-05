<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*,model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Tim tai lieu</title>
</head>
<%
	// -reader : Reader7
	Reader7 reader = (Reader7) session.getAttribute("reader");
	if (reader == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	// trang chi hien thi: SearchDocumentServlet7 tim va dat ket qua vao request/session roi forward ve day
	// -txtTitle : Text
	String title = (String) request.getAttribute("txtTitle");
	// -listDocument : Document7[] (chi co khi vua tim; mo truc tiep trang thi bang rong)
	ArrayList<Document7> listDocument = (title == null) ? null : (ArrayList<Document7>) session.getAttribute("listDocument");
	String message = (String) request.getAttribute("message");
%>
<body>
<h2> Tìm tài liệu </h2>
<form name="timtailieu" action="SearchDocumentServlet7" method="post">
	Tên tài liệu:
	<input type="text" name="txtTitle" value="<%=title == null ? "" : title %>" placeholder="nhập tên hoặc một phần tên" size="40" />
	<input type="submit" name="btnSearch" value="Tìm kiếm" />
</form>
<% if (message != null) { %><p class="err"><%=message %></p><% } %>

<h3>Kết quả tìm kiếm</h3>
<!-- -tblDocument : Table -->
<table class="grid">
	<tr><th>TT</th><th>Mã tài liệu</th><th>Tên tài liệu</th><th>Tác giả</th><th>Năm XB</th><th>Loại</th><th>Chi tiết</th></tr>
<%
	if (listDocument != null)
		for (int i = 0; i < listDocument.size(); i++) {
			Document7 d = listDocument.get(i);
			String authors = "";
			for (DocumentAuthor7 da : d.getDocumentAuthors())
				authors += (authors.length() > 0 ? ", " : "") + da.getAuthor().getName();
%>
	<tr>
		<td><%=(i + 1) %></td>
		<td><%=String.format("TL%04d", d.getId()) %></td>
		<td><%=d.getTitle() %></td>
		<td><%=authors %></td>
		<td><%=d.getPublicationYear() %></td>
		<td><%=d.getDocumentType().getName() %></td>
		<!-- -viewDetail : link -->
		<td><a href="DocumentDetailServlet7?idDocument=<%=d.getId() %>">Chi tiết</a></td>
	</tr>
<%		} %>
</table>
<p/>
<!-- -btnBack : link -->
<button onclick="openPage('gdMainReader7.jsp')">Quay lại</button>
</body>
</html>
