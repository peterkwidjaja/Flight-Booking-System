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
    <%
        String currentUser = (String)request.getAttribute("user");
        boolean isLoggedIn = !currentUser.equals("");
    %>
    <nav class="navbar navbar-default">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="#" class="navbar-brand">Flight Booking System</a>
            </div>
            <div class="collapse navbar-collapse">
                <ul class="nav nav-pills navbar-nav">
                    <%
                        if(isLoggedIn){
                    %>
                    <li><a href="search" title="">Search</a></li>
                    <li><a href="view" title="">Booking</a></li>
                    <li><a href="payment" title="">Payment</a></li>
                    <li><a href="request" title="">Request</a></li>
                    <%
                        }
                    %>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <%
                        if(isLoggedIn){
                    %>
                    
                    <li class="dropdown">
                        <a data-target="#" id="user-menu" data-toggle="dropdown" role="button" class="dropdown-toggle"><%= currentUser %><span class="caret"></span></a>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="user-menu">
                            <li role="presentation">
                                <a href="update" role="menuitem" tabindex="-1">Update</a>
                            </li>

                            <li role="presentation">
                            <a href="logout" role="menuitem" tabindex="-1">Sign out</a>
                            </li>
                        </ul>
                    </li>
                    <%
                        }
                        else{
                    %>

                    <li>
                        <a href="login">Login</a>
                    </li>
                    <%
                        }
                    %>
                </ul>                
            </div>

        </div>
    </nav>
    <div class="container">
        

        <div class = "jumbotron">
            <div class="container">
            <center>
                
                <%
                    if(!isLoggedIn){
                %>
                <h1>Welcome to Flight Booking System</h1>
                <a href="register" class="btn btn-primary btn-lg">Register</a>
                <a href="login" class="btn btn-primary btn-lg">Login</a>
                <%
                    }
                    else{
                %>
                <p>Hello, <%= currentUser %></p>
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
