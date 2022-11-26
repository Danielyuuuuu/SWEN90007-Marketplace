<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
    <title>Register user</title>
</head>
<body>
<h1><%= "Register user!" %></h1>
<% if(request.getAttribute("alertMsg") != null){ %>
    <script type="text/javascript">
        const msg = "<%=request.getAttribute("alertMsg")%>";
        alert(msg);
    </script>
<% } %>
<form action="register" method="post">
    <table style="width: 20%">
        <tr>
            <td>Email</td>
            <td><input type="text" name="email" /></td>
        </tr>
        <tr>
            <td>First Name</td>
            <td><input type="text" name="firstname" /></td>
        </tr>
        <tr>
            <td>Last Name</td>
            <td><input type="text" name="lastname" /></td>
        </tr>
        <tr>
            <td>Shipping Address</td>
            <td><input type="text" name="shipping_address" /></td>
        </tr>
        <tr>
            <td>Password</td>
            <td><input type="password" name="password" /></td>
        </tr>
    </table>
    <input type="submit" value="Submit" />
</form>
<br/>
<a href="login.jsp">Login</a>
</body>
</html>