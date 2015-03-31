
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

    <div class="jumbotron">
        <div class="container">
            <h1>Search</h1>
            <form class="form-horizontal" action="search" method="POST">
                <div class="form-group">
                    <label for="depart-date" class="col-sm-2 control-label">Departure Date</label>
                    <div class="col-sm-10">
                        <input type="date" class="form-control" name="departDate" id="depart-date" placeholder="Enter departure date">
                    </div>
                </div>
                <div class="form-group">
                    <label for="depart-city" class="col-sm-2 control-label">Departure City</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="departCity" id="depart-city" placeholder="Enter departure city">
                    </div>
                </div>
                <div class="form-group">
                    <label for="arriv-city" class="col-sm-2 control-label">Arrival City</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="arrivCity" id="arriv-city" placeholder="Enter arrival city">
                    </div>
                </div>
                <div class="form-group">
                    <label for="passenger" class="col-sm-2 control-label">No of Passengers</label>
                    <div class="col-sm-10">
                        <input type="number" class="form-control" name="passenger" id="passenger">
                    </div>
                </div>             
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">Search</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <%
        ArrayList<Vector> list = (ArrayList)request.getAttribute("data");
        if(list!=null){
    %>

    <div class="container">
        <h2>Search Result</h2>
        <form action="book" method="POST" >
            <input type="hidden" name="seats" value="<%= request.getAttribute("seats") %>" >

            <%
                for(Vector v: list){
                    if(!(Boolean)v.get(0)){
            %>
            <div class="radio">
                <label>
                    <input type="radio" name="optionRadios" value="<%= v.get(1) %>">
                    <p><%= v.get(2) %></p>
                    <p><%= v.get(3) %></p>
                    <p><%= v.get(4) %></p>
                    <p><%= v.get(5) %></p>
                    <p><%= v.get(6) %></p>
                    <p><%= v.get(7) %></p>
                </label>
            </div>
            <%
                    }
                    else{
            %>
            <div class="radio">
                <label>
                    <input type="radio" name="optionRadios" value="<%= v.get(1) +" "+ v.get(8)%>">
                    <div class="row">
                        <div class="col-sm-6">
                            <p><%= v.get(2) %></p>
                            <p><%= v.get(3) %></p>
                            <p><%= v.get(4) %></p>
                            <p><%= v.get(5) %></p>
                            <p><%= v.get(6) %></p>
                            <p><%= v.get(7) %></p>
                        </div>
                        <div class="col-sm-6">
                            <p><%= v.get(9) %></p>
                            <p><%= v.get(10) %></p>
                            <p><%= v.get(11) %></p>
                            <p><%= v.get(12) %></p>
                            <p><%= v.get(13) %></p>
                            <p><%= v.get(14) %></p>
                        </div>                        
                    </div>
                </label>
            </div>
            <%
                    }
                }
            %>
            <button type="submit" class="btn btn-default">Book</button>
        </form>    
    </div>
    
    <%
        }
    %>
</body>
</html>
