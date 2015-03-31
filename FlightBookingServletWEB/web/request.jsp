

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
    <title>Flight Booking System - Request</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
</head>
<body>

    <%
        ArrayList<Vector> list = (ArrayList) request.getAttribute("data");
        if(list!=null){
            
    %>
    <div class="container">
        <h1>Request:</h1>
        <table class="table">
            <caption>Request result:</caption>
            <thead>
                <tr>
                    <th>Request ID</th>
                    <th>Time</th>
                    <th>Content</th>
                    <th>Status</th>
                    <th>Comment</th>
                </tr>
            </thead>
            <tbody>
                <%
                for(Vector v: list){
                    
                %>
                <tr>
                    <td><%= v.get(0) %></td>
                    <td><%= v.get(1) %></td>
                    <td><%= v.get(2) %></td>
                    <td><%= v.get(3) %></td>
                    <td><%= v.get(4) %></td>
                </tr>
                <%
                }
                %>
                
            </tbody>
        </table>
    </div>
    <%        
        }
    %>
</body>
</html>
