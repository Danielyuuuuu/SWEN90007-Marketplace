<%@ page import="MS_Quokka.Domain.User" %>
<%@ page import="MS_Quokka.Utils.Role" %><%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 7/9/2022
  Time: 2:35 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Title</title>
    <%@include file="userHeader.jsp"%>
    <style>
        .block{
            color: black;
            background-color: white;
            text-align: left;
            font-size: large;
            border: 3px solid black;
            box-sizing: border-box;
            display: block;
            width: 30%;
            padding: 2%;
            margin: 2%
        }
        .form {
            border: 3px solid black;
            border-radius: 4px;
            box-sizing: border-box;
            display: block;
            width: 30%;
            padding: 2%;
            margin: 2%
            /*padding: 2%;*/
            /*margin-right: 5%;*/
            /*float: right;*/
            /*position: fixed;*/
            /*top: 20%;*/
            /*left: 55%;*/
        }
    </style>
</head>
<body>
<h2><%= "User Profile" %></h2>
<% if(request.getAttribute("user") != null){ %>
    <% User user = (User) request.getAttribute("user"); %>

        <div class="block">
            <div>
                <label>
                    User ID: <%=user.getId()%>
                </label>
            </div>
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
        <form action="user" method="post" class="form">
            <div>
                <label for="changeFirstname">Change firstname: </label>
                <input type="text" id="changeFirstname" name="changeFirstname">
                <input type="hidden" name="id" value="<%=user.getId()%>" />
            </div>
            <div>
                <label for="changeLastname">Change lastname: </label>
                <input type="text" id="changeLastname" name="changeLastname">
                <input type="hidden" name="id" value="<%=user.getId()%>" />
            </div>
            <div>
                <label for="changeShippingAddress">Change shipping address: </label>
                <input type="text" id="changeShippingAddress" name="changeShippingAddress">
                <input type="hidden" name="id" value="<%=user.getId()%>" />
            </div>
            <div>
                <label for="changePassword">Change password: </label>
                <input type="text" id="changePassword" name="changePassword">
                <input type="hidden" name="id" value="<%=user.getId()%>" />
            </div>
            <div>
                <button>Submit</button>
            </div>
        </form>
    <br/>
<% } %>
</body>
</html>
