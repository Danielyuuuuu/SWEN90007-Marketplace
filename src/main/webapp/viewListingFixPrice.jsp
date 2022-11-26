<%@ page import="MS_Quokka.Domain.FixPriceListing" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 13/9/2022
  Time: 4:46 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% FixPriceListing fixPriceListing = (FixPriceListing) request.getAttribute("fixPriceListing"); %>
<html>
<style>
    table, th, td {
        border:1px solid black;
    }
</style>
<head>
    <title><%=fixPriceListing.getListingTitle()%></title>
    <%@include file="userHeader.jsp"%>
</head>
<body>
    <h1><%=fixPriceListing.getListingTitle()%></h1>
    <table style="width: 50%">
        <tr>
            <th>Item</th>
            <th>Description</th>
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
            <td>SellerGroup</td>
            <td><%=fixPriceListing.getSellerGroup().getName()%></td>
        </tr>
        <tr>
            <td>Product</td>
            <td><%=fixPriceListing.getProduct().getName()%></td>
        </tr>
        <tr>
            <td>Category</td>
            <td><%=fixPriceListing.getProduct().getCategory().getName()%></td>
        </tr>
        <tr>
            <td>Quantity available</td>
            <td><%=fixPriceListing.getQuantity()%></td>
        </tr>
    </table>
    <form method="post" action="purchases">
        <input hidden name="id" value=<%=fixPriceListing.getId()%>>
        <input hidden name="listingType" value="fixedPrice">
        <label>Purchase Quantity:
            <input name="quantity">
        </label>
        <input type="submit" value="Buy">
    </form>
</body>
</html>
