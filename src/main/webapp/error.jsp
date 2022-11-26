<%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 18/9/2022
  Time: 6:06 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Title</title>
    <sec:authorize access="hasRole('SELLER') or hasRole('USER')">
        <%@include file="userHeader.jsp"%>
    </sec:authorize>

    <sec:authorize access="hasRole('ADMIN')">
        <%@include file="adminHeader.jsp"%>
    </sec:authorize>
</head>
<body>
<h1>Error Page</h1>
<br>
<%String errorMessage = (String) request.getAttribute("errorMessage"); %>

<h1><%= errorMessage %></h1>

</body>
</html>
