<%@ page import="java.util.ArrayList" %>
<%@ page import="MS_Quokka.Domain.SellerGroup" %>
<%@ page import="java.util.Enumeration" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<html>
<head>
    <title>Manage SellerGroup</title>
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
<h1><%= "Manage SellerGroup" %>
</h1>

<%
//    Enumeration<String> allAttributes = request.getAttributeNames();
//    while (allAttributes.hasMoreElements()) {
//        System.out.println("The Attribute is: " + allAttributes.nextElement());
//    }
//    if (!allAttributes.hasMoreElements()){
//        System.out.println("No more attributes!");
//    }

    // Display all Seller Groups on the screen
    if (request.getAttribute("allSellerGroups") != null) {
        ArrayList<SellerGroup> allSellerGroups = (ArrayList<SellerGroup>) request.getAttribute("allSellerGroups");
        if (allSellerGroups.isEmpty()) {
%>
            <h2><%= "No Seller Groups exist in the system" %></h2>
<%      }
        else {
            for (SellerGroup sellerGroup: allSellerGroups) {
%>              <div class="block">
                    <div>
                        <label>Id:
                            <%=sellerGroup.getId()%>
                        </label>
                    </div>
                    <div>
                        <label>Name:
                            <%=sellerGroup.getName()%>
                        </label>
                    </div>
                    <a href="${pageContext.request.contextPath}/sellerGroup?id=<%=sellerGroup.getId()%>&name=<%=sellerGroup.getName()%>">
                        <button>Edit</button>
                    </a>

                </div>
            <%}
            }
        }
    else {
        System.out.println("request.getAttribute(\"allSellerGroups\") is null");
    }
%>


<form action="sellerGroup" method="post" class="form">
    <div>
        <label for="newName">Enter the name for a new sellerGroup: </label>
        <input type="text" id="newName" name="name">
    </div>
    <div>
        <button>Submit</button>
    </div>
    <input type="hidden" name="_method" value="POST">
</form>


</body>
</html>
