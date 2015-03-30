<%-- 
    Document   : book
    Created on : Mar 27, 2015, 1:55:01 PM
    Author     : Peter K W
--%>

<%@page import="java.util.ArrayList"%>
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
        <%-- CHECK FOR LOGIN --%>
        <%
            if(request.getAttribute("data")==null){
        %>
        <script>
            window.location.replace("index");
        </script>
        <%
            }
        %>
        <div class="container">
            <h1>Passenger Details</h1>
            <form action="confirmBook" method="POST">

                <%
                    ArrayList<Object> list = (ArrayList) request.getAttribute("data");
                    int seats = (Integer) list.get(0);
                    String schedule = (String) list.get(1);

                %>
                <input type="hidden" name="schedule" value="<%= schedule%>">
                <input type="hidden" name="seats" value="<%= seats%>">
                <%
                    for(int i=1; i<=seats; i++){
                %>
                
                <h3>Passenger <%= i%></h3>
                <div class="form-group">
                    <label for="name<%= i%>">Name</label>
                    <input type="text" class="form-control" name="name<%= i%>" id="name<%= i%>" placeholder="Enter passenger name">
                </div>
                <div class="form-group">
                    <label for="passport<%= i%>">Passport Number</label>
                    <input type="text" class="form control" name="passport<%= i%>" id="passport<%= i%>">
                </div>
                <div class="radio">
                    <label class="radio-inline">
                        <input type="radio" name="gender<%= i%>" value="male">Male 
                    </label>
                    <label>
                        <input type="radio" name="gender<%= i%>" value="female">Female
                    </label>
                </div>
                <div class="form-group">
                    <label for="dob<%= i%>">Date of Birth</label>
                    <input type="date" class="form-control" name="dob<%= i%>" id="dob<%= i%>">
                </div>
                
                <%
                    }
                %>
                <button type="submit" class="btn btn-default">Submit</button>
            </form> 
        </div>
    </body>
</html>
