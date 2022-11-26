<%@ page import="MS_Quokka.Domain.SellerGroup" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Domain.Product" %><%--
  Created by IntelliJ IDEA.
  User: raychen
  Date: 12/9/2022
  Time: 1:10 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Listing - Fix Price</title>
    <%@include file="userHeader.jsp"%>
</head>
<body>
    <h1>Create Listing</h1>
    <form action="listing" method="post" name="createListing">
        <table style="width: 20%">
            <tr>
                <td>Seller Group</td>
                <td>
                    <select name="sellerGroupId">
                    <% ArrayList<SellerGroup> sellerGroups = (ArrayList<SellerGroup>) request.getAttribute("sellerGroups"); %>
                    <%
                        for (SellerGroup sellerGroup: sellerGroups) {
                    %>
                            <option value = <%=sellerGroup.getId()%> ><%=sellerGroup.getName()%></option>
                    <%
                        }
                    %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Listing Type</td>
                <td><select name="listingType">
                    <option value="fixPrice">Fix Price</option>
                    <option value="auction">Auction</option>
                </select>
                </td>
            </tr>
            <tr>
                <td>Listing Title</td>
                <td><input type="text" name="listingTitle" /></td>
            </tr>
            <tr>
                <td>Product</td>
                <td>
                    <select name="productId">
                        <% ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products"); %>
                        <%
                            for (Product product: products) {
                        %>

                        <option value= <%=product.getId()%> ><%=product.getName()%></option>

                        <%
                            }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td><input type="number" name="quantity" min="1" max="999"></td>
            </tr>
            <tr>
                <td>Price</td>
                <td><input type="text" name="price" /></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><textarea name="description" rows="10" cols="70"></textarea></td>
            </tr>
        </table>
        <input name="postType" value="createListing" type="hidden">
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
