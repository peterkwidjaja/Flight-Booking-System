

<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Flight Booking System - Bookings</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">  
</head>
<body>
    <%
        String currentUser = (String)request.getAttribute("user");
        boolean isLoggedIn = !currentUser.equals("");
        if(!isLoggedIn){
    %>
    <script type="javascript">
        window.location.replace("index");
    </script>
    <%
        }
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
                <a href="index" class="navbar-brand">Flight Booking System</a>
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
        <h1>Bookings</h1>
        <%
            ArrayList<Vector> list = (ArrayList) request.getAttribute("data");
            for(int i=1; i<=list.size(); i++){
                Vector v = list.get(i-1);            
        %>
        <div>
            <h3>Booking <%= i%></h3>
            <p>Booking Number: F<%= v.get(0) %></p>
            <p>Booking Time: <%= v.get(1) %></p>
            <div class="row">
                <h4>Flight(s):</h4>
                <%
                ArrayList<Vector> schedules = (ArrayList) v.get(2);
                for(Vector flight: schedules){
                %>
                <div class="col-sm-5">
                    <p>Flight Number: <%= flight.get(0) %></p>
                    <p>Departure: <%= flight.get(1) %></p>
                    <p>Arrival: <%= flight.get(2) %></p>           
                </div>
                <%
                }
                %>
            </div>
            <h4>Passenger(s):</h4>
            <div class="carousel slide" data-ride="carousel" id="carousel-pass<%= i%>" data-interval="false">
                <ol class="carousel-indicators">
                       <li data-target="#carousel-pass<%= i%>" data-slide-to="0"></li>
                </ol>
                <div class="carousel-inner" role="listbox">
                    <%
                ArrayList<Vector> passengers = (ArrayList) v.get(3);
                int count = 1;
                for(Vector pass: passengers){
                    if(count==1){
                %>
                    <div class="item active">
                        <%
                    }
                    else{
                        %>
                    <div class="item">
                        <%
                    }
                    %>
                        <center><div class="container">
                            <h4>Passenger <%= count %></h4>
                            <p>Name: <%= pass.get(0) %></p>
                            <p>Passport: <%= pass.get(1) %></p>
                            <p>Gender: <%= pass.get(2) %></p>
                            <p>Date of Birth: <%= pass.get(3) %></p>
                        </div></center>
                    </div>
                <%
                    count++;
                }
                %>
                </div>
                <a class="left carousel-control" href="#carousel-pass<%= i%>" role="button" data-slide="prev">
                   <span class="glyphicon glyphicon-chevron-left">
                </a>
                <a class="right carousel-control" href="#carousel-pass<%= i%>" role="button" data-slide="next">
                   <span class="glyphicon glyphicon-chevron-right">
                </a>                   
            </div>
            <p>Total Amount: <%= v.get(4) %></p>
            <p>Payment Status:  <%= v.get(5) %> </p>
            <%
            }
            %>
        </div>

    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>
