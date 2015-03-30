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
