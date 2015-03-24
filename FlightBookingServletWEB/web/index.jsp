<%-- 
    Document   : index
    Created on : Mar 24, 2015, 2:47:08 PM
    Author     : Peter K W
    --%>

    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Flight Booking System</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

    </head>

    <body>
    <div class="container">
        
        <%
        String currentUser = (String)request.getAttribute("user");
        %>
        <div class = "jumbotron">
            <div class="container">
            <center>
                
                <%
                    if(currentUser.equals("")){
                %>
                <h1>Welcome to Flight Booking System</h1>
                <a href="register" class="btn btn-primary btn-lg">Register</a>
                <a href="login" class="btn btn-primary btn-lg">Login</a>
                <%
                    }
                    else{
                %>
                <p>Hello, <%= (String)request.getAttribute("user") %></p>
                <h1>Welcome to Flight Booking System</h1>
                <a href="update" class="btn btn-primary btn-lg">Update</a>
                <a href="search" class="btn btn-primary btn-lg">Search</a>
                <% } %>
                </center>
            </div>

        </div>
        
     </div>   

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    </body>
    </html>
