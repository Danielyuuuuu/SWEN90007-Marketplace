<%@ page import="MS_Quokka.Domain.Listing" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Utils.Role" %>
<%@ page import="MS_Quokka.Domain.FixPriceListing" %>
<%@ page import="MS_Quokka.Domain.AuctionListing" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 16/9/2022
  Time: 5:15 pm
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
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


<h1>Manage Listings - Fix Price</h1>
<table style="width: 50%">
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Is Visible For Buyer</th>
        <% Role userRole = (Role) request.getAttribute("userRole"); %>
        <% if(userRole == Role.ADMIN) {%>
            <th>Archive</th>
        <%}%>
        <th>Manage</th>
    </tr>
    <% ArrayList<FixPriceListing> fixPriceListings = (ArrayList<FixPriceListing>) request.getAttribute("fixPriceListings"); %>
    <%
        for (FixPriceListing listing: fixPriceListings) {
    %>
    <tr>
        <td> <%=listing.getListingTitle()%></td>
        <td><%=listing.getDescription()%></td>
        <td><%=listing.isVisibleForBuyer()%></td>
        <% if(userRole == Role.ADMIN) {%>
            <td><%=listing.getArchive()%></td>
        <%}%>
        <td>
            <a href="${pageContext.request.contextPath}/listing?page=manageListing&listingType=fixPrice&id=<%=listing.getId()%>">
                <button>manage</button>
            </a>
        </td>
    </tr>
    <%
        }
    %>
</table>

<h1>Manage Listings - Auction</h1>
<table style="width: 50%">
    <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Is Ended</th>
        <th>Is Order Create</th>
        <% if(userRole == Role.SELLER) {%>
            <th>Is Ready for Creating Order</th>
        <%}%>
        <% if(userRole == Role.ADMIN) {%>
            <th>Archive</th>
        <%}%>
        <th>Manage</th>
    </tr>
    <% ArrayList<AuctionListing> auctionListings = (ArrayList<AuctionListing>) request.getAttribute("auctionListings"); %>
    <%
        for (AuctionListing listing: auctionListings) {
    %>
    <tr>
        <td> <%=listing.getListingTitle()%></td>
        <td><%=listing.getDescription()%></td>
        <td><%=listing.isExpire()%></td>

        <td><%=listing.isOrderCreate()%></td>

        <% if(userRole == Role.SELLER) {%>
            <td><%=(!listing.isOrderCreate() && !listing.isVisibleForBuyer() && listing.getHighestBidder() != null)%></td>
        <%}%>

        <% if(userRole == Role.ADMIN) {%>
            <td><%=listing.getArchive()%></td>
        <%}%>
        <td>
            <a href="${pageContext.request.contextPath}/listing?page=manageListing&listingType=auction&id=<%=listing.getId()%>">
                <button>manage</button>
            </a>
        </td>
    </tr>
    <%
        }
    %>
</table>

</body>
</html>