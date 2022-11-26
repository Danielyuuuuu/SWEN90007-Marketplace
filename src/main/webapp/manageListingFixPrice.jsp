<%@ page import="MS_Quokka.Domain.FixPriceListing" %>
<%@ page import="MS_Quokka.Utils.Role" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 18/9/2022
  Time: 4:44 am
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>


<% FixPriceListing fixPriceListing = (FixPriceListing) request.getAttribute("fixPriceListing"); %>
<% Role userRole = (Role) request.getAttribute("userRole"); %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<style>
    table, th, td {
        border:1px solid black;
    }
</style>
<head>
    <title><%=fixPriceListing.getListingTitle()%></title>

    <sec:authorize access="hasRole('SELLER')">
        <%@include file="userHeader.jsp"%>
    </sec:authorize>

    <sec:authorize access="hasRole('ADMIN')">
        <%@include file="adminHeader.jsp"%>
    </sec:authorize>

</head>
<body>
<h1><%=fixPriceListing.getListingTitle()%></h1>
<table style="width: 50%">
    <tr>
        <th>Item</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>ID</td>
        <td><%=fixPriceListing.getId()%></td>
    </tr>
    <tr>
        <td>Quantity</td>
        <td><%=fixPriceListing.getQuantity()%></td>
    </tr>
    <tr>
        <td>Price</td>
        <td><%=fixPriceListing.getPrice()%></td>
    </tr>
    <tr>
        <td>Description</td>
        <td><%=fixPriceListing.getDescription()%></td>
    </tr>
    <tr>
        <td>SellerGroup ID</td>
        <td><%=fixPriceListing.getSellerGroup().getId()%></td>
    </tr>
    <tr>
        <td>SellerGroup</td>
        <td><%=fixPriceListing.getSellerGroup().getName()%></td>
    </tr>
    <tr>
        <td>Product ID</td>
        <td><%=fixPriceListing.getProduct().getId()%></td>
    </tr>
    <tr>
        <td>Product</td>
        <td><%=fixPriceListing.getProduct().getName()%></td>
    </tr>
    <tr>
        <td>Category Id</td>
        <td><%=fixPriceListing.getProduct().getCategory().getId()%></td>
    </tr>
    <tr>
        <td>Category</td>
        <td><%=fixPriceListing.getProduct().getCategory().getName()%></td>
    </tr>
    <% if(userRole == Role.ADMIN) {%>
        <tr>
            <td>Archive</td>
            <td><%=fixPriceListing.getArchive()%></td>
        </tr>
    <%}%>
</table>
<% if(userRole == Role.ADMIN && !fixPriceListing.getArchive()) {%>
    <form action="listing" method="post">
        <input type="hidden" name="id" value=<%=fixPriceListing.getId()%>>
        <input type="hidden" name="postType" value="archiveFixPrice">
        <input type="submit" value="Archive" />
    </form>
<%}%>
</body>
</html>
