<%@ page import="MS_Quokka.Domain.AuctionListing" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 16/9/2022
  Time: 1:34 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% AuctionListing auctionListing = (AuctionListing) request.getAttribute("auctionListing"); %>
<html>
<style>
    table, th, td {
        border:1px solid black;
    }
</style>
<head>
    <title><%=auctionListing.getListingTitle()%></title>
    <%@include file="userHeader.jsp"%>
</head>
<body>
<h1><%=auctionListing.getListingTitle()%></h1>
<table style="width: 50%">
    <tr>
        <th>Item</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>Price</td>
        <td><%=auctionListing.getPrice()%></td>
    </tr>
    <tr>
        <td>Description</td>
        <td><%=auctionListing.getDescription()%></td>
    </tr>
    <tr>
        <td>SellerGroup</td>
        <td><%=auctionListing.getSellerGroup().getName()%></td>
    </tr>
    <tr>
        <td>Product</td>
        <td><%=auctionListing.getProduct().getName()%></td>
    </tr>
    <tr>
        <td>Category</td>
        <td><%=auctionListing.getProduct().getCategory().getName()%></td>
    </tr>
    <tr>
        <td>Start Time</td>
        <td><%=auctionListing.getStartTime()%></td>
    </tr>
    <tr>
        <td>End Time</td>
        <td><%=auctionListing.getEndTime()%></td>
    </tr>
    <tr>
        <td>Bidder Counter</td>
        <td><%=auctionListing.getBidCounter()%></td>
    </tr>
</table>
<form action="listing" method="post">
    <table style="width: 20%">
        <tr>
            <td>Price</td>
            <td><input type="text" name="price" /></td>
        </tr>
    </table>
    <input type="hidden" name="id" value=<%=auctionListing.getId()%>>
    <input type="hidden" name="postType" value="placeBid">

    <input type="submit" value="Place bid" />
</form>
</body>
</html>

