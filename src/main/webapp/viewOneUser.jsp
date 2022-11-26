<%@ page import="MS_Quokka.Domain.User" %>
<%@ page import="MS_Quokka.Utils.Role" %><%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 18/9/2022
  Time: 11:58 am
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<html>
<head>
    <title>Admin Dashboard</title>
    <%@include file="adminHeader.jsp"%>

    <style>
        .block{
            color: black;
            background-color: white;
            text-align: left;
            font-size: large;
            border: 3px solid black;
            box-sizing: border-box;
            display: block;
            width: 35%;
            padding: 2%;
            margin: 2%
        }
        .form {
            width: 40%;
            border: 3px solid black;
            border-radius: 4px;
            box-sizing: border-box;
            display: block;
            padding: 2%;
            margin-right: 5%;
            float: right;
            position: fixed;
            top: 20%;
            left: 55%;
        }
        .center {
            text-align: center;
        }
    </style>

</head>
<body>

<% User user = (User) request.getAttribute("user"); %>
<%
    if (user != null){
%>
    <div class="block">
        <div>
            <label>
                Email: <%=user.getEmail()%>
            </label>
        </div>
        <div>
            <label>
                Firstname: <%=user.getFirstname()%>
            </label>
        </div>
        <div>
            <label>
                Lastname: <%=user.getLastname()%>
            </label>
        </div>
        <div>
            <label>
                Shipping Address: <%=user.getShippingAddress()%>
            </label>
        </div>
        <% if (user.getRole() == Role.SELLER) { %>
            <div>
                <label>
                    Seller Group Name: <%=user.getSellerGroup().getName()%>
                </label>
            </div>
        <% } %>
    </div>
<% } %>

</body>
</html>
