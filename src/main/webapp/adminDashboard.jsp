<%@ page import="MS_Quokka.Domain.User" %><%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 17/9/2022
  Time: 4:34 pm
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<html>
<head>
  <title>Admin Dashboard</title>
  <%@include file="adminHeader.jsp"%>

  <style>
    .center {
      text-align: center;
    }
  </style>

</head>
<body>

<% User admin = (User) request.getAttribute("admin"); %>
<%
  if (admin != null){
%>
<br />
<div class="center">
  <h2>
    Welcome, <%=admin.getFirstname()%>!
  </h2>
</div>
<% } %>

</body>
</html>
