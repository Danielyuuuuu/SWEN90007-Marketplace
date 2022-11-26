<%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 17/9/2022
  Time: 5:23 pm
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title> User Header </title>
</head>
<style>

    ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
        overflow: hidden;
        background-color: #333;
        height: 46px;
    }
    li {
        float: left;
    }

    li a {
        display: block;
        color: white;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
    }
    li a:hover {
        background-color: #111;
    }
    .input {
        display: block;
        text-align: center;
        padding: 14px 16px;
        background-color: #333;
        color: white;
        border: 0px;
        cursor: pointer;
        font-family: "Times New Roman";
        font-size: 12pt;
    }
    .input:hover {
        background-color: #111;
    }

</style>
<body>

<h1>
    User Dashboard
</h1>
<ul>
    <li><a href="user">User Profile</a></li>
    <li><a href="listing?page=viewListings">Listings</a></li>
    <li><a href="purchases">Purchases</a></li>
    <sec:authorize access="hasRole('SELLER')">
        <li><a href="listing?page=createListing">Create New listing</a><li/>
        <li><a href="listing?page=manageListings">Manage Listings</a></li>
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        <li>
            <form action="logout" method="post">
                <sec:csrfInput />
                <input class="input" type="submit" value="Sign Out" />
            </form>
        </li>
    </sec:authorize>
</ul>

</body>
</html>
