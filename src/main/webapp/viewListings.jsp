<%@ page import="MS_Quokka.Domain.Listing" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Domain.AuctionListing" %>
<%@ page import="MS_Quokka.Domain.FixPriceListing" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 13/9/2022
  Time: 2:18 am
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

<sec:authorize access="hasRole('ADMIN')">
    <%@include file="adminHeader.jsp"%>
</sec:authorize>

<sec:authorize access="hasRole('USER') or hasRole('SELLER')">
    <%@include file="userHeader.jsp"%>
</sec:authorize>

<head>
    <title>MS Quokka Market Place</title>
</head>
<body>
    <h1>View all the goods</h1>
    <form action="listing" method="get">
        <input name="page" type='hidden' value="viewListings">
        <input type="text" name="search" />
        <input type="submit" value="search" />
    </form>
    <table style="width: 50%">
        <tr>
            <th>Title</th>
            <th>Price $AUD</th>
            <th>Listing Type</th>
            <th>View More</th>
        </tr>

        <!-- fixPrice listing -->
        <% ArrayList<FixPriceListing> fixPriceListings = (ArrayList<FixPriceListing>) request.getAttribute("fixPriceListings"); %>
        <%
            for (Listing listing: fixPriceListings) {
        %>
            <tr>
                <td> <%=listing.getListingTitle()%></td>
                <td><%=listing.getPrice()%></td>
                <td><%="fixPrice"%></td>
                <td>
                    <a href="${pageContext.request.contextPath}/listing?page=viewListing&listingType=<%="fixPrice"%>&id=<%=listing.getId()%>">
                        <button>view</button>
                    </a>
                </td>
            </tr>
        <%
            }
        %>

        <!-- auction listing -->
        <% ArrayList<AuctionListing> auctionListings = (ArrayList<AuctionListing>) request.getAttribute("auctionListings"); %>
        <%
            for (Listing listing: auctionListings) {
        %>
        <tr>
            <td> <%=listing.getListingTitle()%></td>
            <td><%=listing.getPrice()%></td>
            <td><%="auction"%></td>
            <td>
                <a href="${pageContext.request.contextPath}/listing?page=viewListing&listingType=<%="auction"%>&id=<%=listing.getId()%>">
                    <button>view</button>
                </a>
            </td>
        </tr>
        <%
            }
        %>
    </table>
</body>
</html>
