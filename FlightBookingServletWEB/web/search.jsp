
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
                    <input type="radio" name="optionRadios" value="<%= v.get(1) %>">
                    <div class="row">
                        <div class="col-sm-4">
                            <p><%= v.get(2) %></p>
                            <p><%= v.get(3) %></p>
                            <p><%= v.get(4) %></p>
                            <p><%= v.get(5) %></p>
                            <p><%= v.get(6) %></p>
                            <p><%= v.get(7) %></p>
                        </div>
                        <div class="col-sm-4">
                            <p><%= v.get(8) %></p>
                            <p><%= v.get(9) %></p>
                            <p><%= v.get(10) %></p>
                            <p><%= v.get(11) %></p>
                            <p><%= v.get(12) %></p>
                            <p><%= v.get(13) %></p>
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
