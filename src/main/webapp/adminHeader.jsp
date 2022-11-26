<%--
  Created by IntelliJ IDEA.
  User: haoxi
  Date: 10/09/2022
  Time: 7:56 pm
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
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
  Admin Dashboard
</h1>
<ul>
  <li><a href="sellerGroup">Seller Groups</a></li>
  <li><a href="purchases">Purchases</a></li>
  <li><a href="admin?action=users">Users</a></li>
  <li><a href="listing?page=viewListings">Listings</a></li>
  <li><a href="listing?page=manageListings">Manage Listings</a></li>
  <sec:authorize access="isAuthenticated()">
    <li>
      <form action="logout" method="post">
        <sec:csrfInput />
        <input class="input" type="submit" value="Sign Out"/>
      </form>
    </li>
  </sec:authorize>
</ul>

</body>

<%--    <li><a href="nonAdminUsers.jsp">Users</a></li>--%>
<%--    <li><a href="listings.jsp">Listings</a> </li>--%>