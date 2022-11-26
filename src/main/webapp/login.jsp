<%--
  Created by IntelliJ IDEA.
  User: yifei
  Date: 3/9/2022
  Time: 12:10 pm
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login page</title>
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
        form {
            border: 3px solid black;
            border-radius: 4px;
            box-sizing: border-box;
            width: 20%;
            display: block;
            text-align: center;
            padding: 2%;
            margin: auto
        }
        .center {
            text-align: center;
        }
        .text_box{
            margin-bottom: 2px;
        }
    </style>
</head>
<body>
<h1 class="center">Login page</h1>
<% if(request.getParameter("alertMsg") != null){ %>
<script type="text/javascript">
    const msg = "Email or password is incorrect";
    alert(msg);
</script>
<% } %>
<form th:action="@{/login.jsp}" method="post">
    <div class="text_box">
        <label for="username">Email:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
        <input type="text" id="username" name="username" autofocus="autofocus" /> <br />
    </div>
    <div class="text_box">
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" autofocus="autofocus" /> <br />
    </div>
    <br/>
    <input type="submit" value="Log in" />
</form>
<br/>
<a href="register">Register</a>
</body>
</html>

