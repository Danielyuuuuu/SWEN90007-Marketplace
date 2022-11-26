<%@ page import="java.util.List" %>
<%@ page import="MS_Quokka.Domain.Purchase" %>
<%--
  Created by IntelliJ IDEA.
  User: Edward
  Date: 30/08/2022
  Time: 11:27 pm
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Purchases</title>
    <style>
        .block{
            block-size: fit-content;
            color: black;
            background-color: white;
            text-align: left;
            font-size: large;
            border: 3px solid black;
            box-sizing: border-box;
            display: block;
            width: 40%;
            padding: 2%;
            margin: 2%
        }
        .form {
            width: 30%;
            border: 3px solid black;
            border-radius: 4px;
            box-sizing: border-box;
            display: block;
            padding: 2%;
            margin-right: 5%;
            float: right;
            position: fixed;
            top: 30%;
            left: 60%;
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
        List<Purchase> purchases = (List<Purchase>) request.getAttribute("purchases");
        List<Purchase> sales = (List<Purchase>) request.getAttribute("sales");
    %>
    <% if (!purchases.isEmpty()) { %>
        <h1>Purchases</h1>
        <%
            for (Purchase purchase: purchases) {
        %>
                <div class="block">
                    <div>
                        <label>Product:
                            <%=purchase.getProduct().getName()%>
                        </label>
                    </div>
                    <div>
                        <label>Seller:
                            <%=purchase.getSeller().getName()%>
                        </label>
                    </div>
                    <div>
                        <label>Quantity:
                            <%=purchase.getQuantity()%>
                        </label>
                    </div>
                    <div>
                        <label>Price:
                            $<%=purchase.getPrice()%>
                        </label>
                    </div>
                    <div>
                        <label>Buyer:
                            <%=purchase.getBuyer().getEmail()%>
                        </label>
                    </div>
                    <div>
                        <label>Status:
                            <%=purchase.getStatus()%>
                        </label>
                    </div>
                    <a href="${pageContext.request.contextPath}/purchase?id=<%=purchase.getId()%>">
                        <button>Edit</button>
                    </a>
                </div>
        <%
            }
        %>
    <% } else { %>
        <h2>You haven't made any purchases</h2>
    <% } %>
    <% if (sales != null && !sales.isEmpty()) { %>
        <h1>Sales</h1>
        <%
            for (Purchase sale: sales) {
        %>
        <div class="block">
            <div>
                <label>Product:
                    <%=sale.getProduct().getName()%>
                </label>
            </div>
            <div>
                <label>Seller:
                    <%=sale.getSeller().getName()%>
                </label>
            </div>
            <div>
                <label>Quantity:
                    <%=sale.getQuantity()%>
                </label>
            </div>
            <div>
                <label>Price:
                    $<%=sale.getPrice()%>
                </label>
            </div>
            <div>
                <label>Buyer:
                    <%=sale.getBuyer().getEmail()%>
                </label>
            </div>
            <div>
                <label>Status:
                    <%=sale.getStatus()%>
                </label>
            </div>
            <a href="${pageContext.request.contextPath}/purchase?id=<%=sale.getId()%>">
                <button>Edit</button>
            </a>
        </div>
        <%
            }
        %>
    <% } %>

</body>
</html>
