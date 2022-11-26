<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 20/9/2022
  Time: 1:11 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<style>
    table, th, td {
        border:1px solid black;
    }
</style>
<head>
    <title>MS Quokka Market Place</title>
    <sec:authorize access="hasRole('SELLER')">
        <%@include file="userHeader.jsp"%>
    </sec:authorize>

    <sec:authorize access="hasRole('ADMIN')">
        <%@include file="adminHeader.jsp"%>
    </sec:authorize>
</head>
<body>
<h1>View all the bids!!!</h1>
<table style="width: 50%">
    <tr>
        <th>Bids - User</th>
    </tr>
    <% ArrayList<String> bids = (ArrayList<String>) request.getAttribute("allBids"); %>
    <%
        for (String bid: bids) {
    %>
    <tr>
        <td> <%=bid%></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
