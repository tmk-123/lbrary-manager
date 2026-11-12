<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="model.*,util.View7"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="../header.jsp" %>
<title>Trang chu nhan vien thu vien</title>
</head>
<body>
<%
	// -librarian : Librarian7 (thuoc tinh an, lay tu session sau khi dang nhap)
	Librarian7 librarian = (Librarian7) session.getAttribute("librarian");
	if (librarian == null) {
		response.sendRedirect("../gdLogin7.jsp?err=timeout");
		return;
	}
	FullName7 fn = librarian.getFullName();
	String name = fn.getLastName() + " " + (fn.getMiddleName() == null ? "" : fn.getMiddleName() + " ") + fn.getFirstName();
%>
<h2> Trang chủ nhân viên thư viện </h2>
<p>Nhân viên: <%=View7.html(name)%></p>
<!-- -borrowDocument : link -->
<a href="IdentifyReaderServlet7">Cho mượn tài liệu</a>
<br><br>
<!-- -logout : submit -->
<form action="../LogoutServlet7" method="post"><button type="submit" name="logout">Đăng xuất</button></form>
</body>
</html>
