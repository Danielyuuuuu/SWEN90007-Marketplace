<%@ page import="MS_Quokka.Domain.SellerGroup" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@include file="adminHeader.jsp"%>
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
    </style>
</head>
<body>
<%
    // Display the Seller Group's information on the screen
    SellerGroup sellerGroup = null;
    ArrayList<User> usersInSellerGroup = new ArrayList<>();
    if (request.getAttribute("sellerGroup") != null) {
        sellerGroup = (SellerGroup) request.getAttribute("sellerGroup");

%>      <h2> Seller Group Name: <%=sellerGroup.getName()%></h2>
        <h2> Seller Group Id: <%=sellerGroup.getId()%></h2>
<%}
    else {
%>
        <h2><%= "Something went wrong." %></h2>
<%}
%>


<%
    // Display all the users who belong to the Seller Group on the screen
    if (request.getAttribute("usersInSellerGroup") != null) {
        usersInSellerGroup = (ArrayList<User>) request.getAttribute("usersInSellerGroup");
        if (usersInSellerGroup.isEmpty()) {
%>
            <h2><%= "No Seller exist in this Seller Group" %></h2>
<%      }
        else {
            for (User user: usersInSellerGroup) {
%>              <div class="block">
                    <div>purchase
                        <label>Firstname:
                            <%=user.getFirstname()%>
                        </label>
                    </div>
                    <div>
                        <label>Lastname:
                            <%=user.getLastname()%>
                        </label>
                    </div>
                    <div>
                        <label>Email:
                            <%=user.getEmail()%>
                        </label>
                    </div>





</div>
<%          }
        }
    }
    else {
        System.out.println("request.getAttribute(\"usersInSellerGroup\") is null");
    }
%>



<form action="sellerGroup" method="post" class="form">
    <div>
        <div>
            <label for="newName">New name for the Seller Group: </label>
            <input type="text" id="newName" name="newName">
        </div>
        <div>
            <label for="newSeller">Onboard a new seller into the Seller Group. Enter their email: </label>
            <input type="text" id="newSeller" name="newSellerEmail">
        </div>
        <div>
            <label for="removeSellers">Remove these users from Seller Group </label>
            <select id="removeSellers" name="removeSellerId">
                <%
                // In the drop down list, display all the users in this group (already in memory)
                if (!usersInSellerGroup.isEmpty()) {
                    for (User user: usersInSellerGroup) {
                        String fullName = user.getFirstname() + " " + user.getLastname();
                        %>
                            <option value=<%=user.getId()%>> <%=fullName%></option>
                    <%}
                }
                %>
                <option value="" selected>None</option>
            </select>
            <br><br>
        </div>
        <div>
            <button>Submit</button>
        </div>
    </div>
    <input type="hidden" name="_originalName" value=<%=sellerGroup.getName()%>>
    <input type="hidden" name="_id" value=<%=sellerGroup.getId()%>>
    <input type="hidden" name="_method" value="PUT">
</form>

<a href="${pageContext.request.contextPath}/sellerGroup">
    <button>Go Back</button>
</a>

</body>
</html>
