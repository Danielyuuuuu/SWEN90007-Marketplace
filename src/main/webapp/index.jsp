<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<h1><%= "Welcome to MS-Quokka Marketplace System!" %></h1>
<br/>
<sec:authorize access="!isAuthenticated()">
    <a href="login.jsp">Login</a>
    <br/>
    <a href="register">Register</a>
    <br/>
</sec:authorize>
<sec:authorize access="hasRole('SELLER')">
    <%
        response.sendRedirect("user");
    %>
</sec:authorize>
<sec:authorize access="hasRole('USER')">
    <%
        response.sendRedirect("user");
    %>
</sec:authorize>
<sec:authorize access="hasRole('ADMIN')">
    <%
        response.sendRedirect("admin");
    %>
</sec:authorize>
</body>
</html>
