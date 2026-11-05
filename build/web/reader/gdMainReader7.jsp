<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="model.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Trang chu ban doc</title>
</head>
<body>
<%
	// -reader : Reader7 (thuoc tinh an, lay tu session sau khi dang nhap)
	Reader7 reader = (Reader7) session.getAttribute("reader");
	if (reader == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	FullName7 fn = reader.getFullName();
	String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<h2> Trang chủ bạn đọc </h2>
<p>Bạn đọc: <%=name %></p>
<!-- -searchDocument : link -->
<a href="gdSearchDocument7.jsp">Tìm tài liệu</a>
<br><br>
<!-- -logout : link -->
<button onclick="openPage('../LogoutServlet7')">Đăng xuất</button>
</body>
</html>
