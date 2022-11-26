<%@ page import="MS_Quokka.Domain.AuctionListing" %>
<%@ page import="MS_Quokka.Utils.Role" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 18/9/2022
  Time: 4:44 am
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>

<% AuctionListing auctionListing = (AuctionListing) request.getAttribute("auctionListing"); %>
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
    <title><%=auctionListing.getListingTitle()%></title>

    <sec:authorize access="hasRole('SELLER')">
        <%@include file="userHeader.jsp"%>
    </sec:authorize>

    <sec:authorize access="hasRole('ADMIN')">
        <%@include file="adminHeader.jsp"%>
    </sec:authorize>

</head>
<body>
<h1><%=auctionListing.getListingTitle()%></h1>
<table style="width: 50%">
    <tr>
        <th>Item</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>ID</td>
        <td><%=auctionListing.getId()%></td>
    </tr>
    <tr>
        <td>Quantity</td>
        <td><%=auctionListing.getQuantity()%></td>
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
        <td>Price</td>
        <td><%=auctionListing.getPrice()%></td>
    </tr>
    <tr>
        <td>Description</td>
        <td><%=auctionListing.getDescription()%></td>
    </tr>
    <tr>
        <td>Bid Counter</td>
        <td><%=auctionListing.getBidCounter()%></td>
    </tr>
    <tr>
        <td>SellerGroup ID</td>
        <td><%=auctionListing.getSellerGroup().getId()%></td>
    </tr>
    <tr>
        <td>SellerGroup</td>
        <td><%=auctionListing.getSellerGroup().getName()%></td>
    </tr>
    <tr>
        <td>Product ID</td>
        <td><%=auctionListing.getProduct().getId()%></td>
    </tr>
    <tr>
        <td>Product</td>
        <td><%=auctionListing.getProduct().getName()%></td>
    </tr>
    <tr>
        <td>Category Id</td>
        <td><%=auctionListing.getProduct().getCategory().getId()%></td>
    </tr>
    <tr>
        <td>Category</td>
        <td><%=auctionListing.getProduct().getCategory().getName()%></td>
    </tr>

    <% if(auctionListing.getBidCounter() == 0) {%>
        <tr>
            <td>Highest Bidder Id</td>
            <td><%="null"%></td>
        </tr>
        <tr>
            <td>Highest Bidder Name</td>
            <td><%="-"%></td>
        </tr>
    <%}else{%>
        <tr>
            <td>Highest Bidder Id</td>
            <td><%=auctionListing.getHighestBidder().getId()%></td>
        </tr>

        <tr>
            <td>Highest Bidder Name</td>
            <td><%=auctionListing.getHighestBidder().getFirstname() + " " +auctionListing.getHighestBidder().getLastname()%></td>
        </tr>
    <%}%>
    <tr>
        <td>Is Ended</td>
        <td><%=auctionListing.isExpire()%></td>
    </tr>
    <tr>
        <td>Is Order Created</td>
        <td><%=auctionListing.isOrderCreate()%></td>
    </tr>
    <% if(userRole == Role.ADMIN) {%>
        <tr>
            <td>Archive</td>
            <td><%=auctionListing.getArchive()%></td>
        </tr>
    <%}%>
</table>


<% if(userRole == Role.ADMIN && !auctionListing.getArchive()) {%>
    <form action="listing" method="post">
        <input type="hidden" name="id" value=<%=auctionListing.getId()%>>
        <input type="hidden" name="postType" value="archiveAuction">
        <input type="submit" value="Archive" />
    </form>
<%}%>

<% if(userRole == Role.SELLER && !auctionListing.isOrderCreate() && !auctionListing.isVisibleForBuyer() && auctionListing.getHighestBidder() != null) {%>
    <form action="purchases" method="post">
        <input type="hidden" name="id" value=<%=auctionListing.getId()%>>
        <input type="hidden" name="listingType" value="auction">
        <input type="hidden" name="quantity" value="1">
        <input type="hidden" name="buyer" value=<%= auctionListing.getHighestBidder().getId() %>>
        <input type="submit" value="Create Order" />
    </form>
<%}%>
    <a href="${pageContext.request.contextPath}/listing?page=manageAuctionBids&listing&id=<%=auctionListing.getId()%>">
        <button>view all bids</button>
    </a>
</body>
</html>

