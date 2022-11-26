<%@ page import="MS_Quokka.Domain.Purchase" %>
<%--
  Created by IntelliJ IDEA.
  User: Edward
  Date: 10/09/2022
  Time: 2:17 am
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Purchase</title>
    <style>
        .block{
        block-size: fit-content;
        color: black;
        background-color: white;
        text-align: left;
        font-size: large;
        border: 3px solid black;
        border-radius: 20px;
        box-sizing: border-box;
        display: block;
        width: 30%;
        padding: 2%;
        margin: 2%;
        top: 20%;
        }
        .formBlock {
        width: 40%;
        border: 3px solid black;
        border-radius: 20px;
        box-sizing: border-box;
        display: block;
        padding: 2%;
        margin-right: 5%;
        float: right;
        position: fixed;
        top: 20%;
        left: 55%;
        }
    </style>
</head>
<body>
    <sec:authorize access="hasRole('SELLER') or hasRole('USER')">
        <%@include file="userHeader.jsp"%>
    </sec:authorize>

    <sec:authorize access="hasRole('ADMIN')">
        <%@include file="adminHeader.jsp"%>
    </sec:authorize>
    <%
        Purchase purchase = (Purchase) request.getAttribute("purchase");
        boolean displayConfirmButton = (boolean) request.getAttribute("displayConfirmButton");
    %>
    <h2>Single Purchase</h2>
    <div class="block">
        <div>
            <label>Product:
                <%= purchase.getProduct().getName() %>
            </label>
        </div>
        <div>
            <label>Seller:
                <%= purchase.getSeller().getName() %>
            </label>
        </div>
        <div>
            <label>Quantity:
                <%= purchase.getQuantity() %>
            </label>
        </div>
        <div>
            <label>Price:
                $<%= purchase.getPrice() %>
            </label>
        </div>
        <div>
            <label>Buyer:
                <%= purchase.getBuyer().getEmail() %>
            </label>
        </div>
        <div>
            <label>Status:
                <%= purchase.getStatus() %>
            </label>
        </div>
    </div>

    <% if (purchase.getStatus() != Purchase.Status.cancelled) { %>
    <div class="formBlock">
        <% if (purchase.getStatus() == Purchase.Status.placed) { %>
            <form method="post" action="purchase">
                <input hidden name="id" value=<%=purchase.getId()%> />
                <input hidden name="originalQuantity" value=<%=purchase.getQuantity()%> />
                <label>Update Quantity:
                    <input name="quantity">
                </label>
                <input type="submit" value="Submit">
            </form>
        <% } else if (purchase.getStatus() == Purchase.Status.processed) {%>
            <sec:authorize access="hasRole('SELLER')">
                <form method="post" action="purchase">
                    <input hidden name="id" value=<%=purchase.getId()%> />
                    <input hidden name="originalQuantity" value=<%=purchase.getQuantity()%> />
                            <label>Update Quantity:
                    <input name="quantity">
                    </label>
                    <input type="submit" value="Submit">
                </form>
            </sec:authorize>
        <% } %>

<%--    if buyer and status placed (or processed?) or seller and status not fulfilled    --%>
        <% if (purchase.getStatus() != Purchase.Status.fulfilled) { %>
        <form method="post" action="purchase">
            <input hidden name="id" value=<%=purchase.getId()%> />
            <input hidden name="status" value="cancelled" />
            <button type="submit">Cancel Purchase</button>
        </form>
        <% } %>

        <% if (purchase.getStatus() == Purchase.Status.placed && displayConfirmButton) { %>
<%--    if buyer and status placed    --%>
        <form method="post" action="purchase">
            <input hidden name="id" value=<%=purchase.getId()%> />
            <input hidden name="status" value="processed" />
            <input hidden name="originalQuantity" value=<%=purchase.getQuantity()%> />
            <button type="submit">Confirm Purchase</button>
        </form>
        <% } %>

        <sec:authorize access="hasRole('SELLER')">
            <% if (purchase.getStatus() == Purchase.Status.processed) { %>
    <%--    if seller and status processed    --%>
                <form method="post" action="purchase">
                    <input hidden name="id" value=<%=purchase.getId()%> />
                    <input hidden name="status" value="fulfilled" />
                    <input hidden name="originalQuantity" value=<%=purchase.getQuantity()%> />
                    <button type="submit">Complete Purchase</button>
                </form>
            <% } %>
        </sec:authorize>
    </div>
    <% } %>
</body>
</html>
