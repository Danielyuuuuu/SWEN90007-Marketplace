<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Domain.User" %><%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 7/9/2022
  Time: 6:26 pm
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
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
            width: 30%;
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
<h1><%= "All Users" %></h1>
<% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users"); %>
<%
    if (users.size() > 0){
        int count = 0;
        for (User user: users) {
            count++;
%>
            <div class="block">
                <div class="center">
                    <label>
                        User <%=count%>
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
                <% System.out.println("In jsp: " + user.getShippingAddress());%>
                <div>
                    <form action="admin" name="viewAllInfo" METHOD="POST">
                        <input TYPE="hidden" value=<%=user.getId()%> name="userID" >
                        <input TYPE="hidden" value=<%=user.getEmail()%> name="email" >
                        <input TYPE="hidden" value=<%=user.getFirstname()%> name="firstname" >
                        <input TYPE="hidden" value=<%=user.getLastname()%> name="lastname" >
                        <input TYPE="hidden" value="<%=user.getShippingAddress()%>" name="shippingAddress" >
                        <input TYPE="hidden" value=<%=user.getRole().toString()%> name="role" >
                        <input TYPE="hidden" value=<%=user.getSellerGroupID()%> name="sellerGroupID" >
                        <input TYPE="submit" value="View All Information" name="viewAllInfo">
                    </form>
                </div>
            </div>
        <% } %>
<%
    } else {
%>
        <h2><%= "The system does not have any users" %></h2>
<%
    }
%>
</body>
</html>
