
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
	<title>Flight Booking System - Payment</title>
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

    <%
    	if(!isLoggedIn){
    %>
    <script type="javascript">
		window.location.replace("index");
    </script>

    <% } else{ %>
	<div class="container">
		<h1>
			Payment
		</h1>
		<%
			ArrayList<Vector> list = (ArrayList) request.getAttribute("data");
                        if(list!=null){
                            for(Vector v: list){
                               
		%>
		<form action="makePayment" method="GET" class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-4 control-label">Booking Number:</label>
				<div class="col-sm-8">
					<input type="hidden" name="bookingNo" value="<%= v.get(0)%>">
					<p class="form-control-static"><%= v.get(0) %></p>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">Booking Time:</label>
				<div class="col-sm-8">
					<p class="form-control-static"><%= v.get(1) %></p>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">Route:</label>
				<div class="col-sm-8">
					<p class="form-control-static"><%= v.get(2) %></p>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-4 control-label">Number of Passengers:</label>
				<div class="col-sm-8">
					<p class="form-control-static"><%= v.get(3) %></p>
				</div>
			</div>						
			<div class="form-group">
				<label class="col-sm-4 control-label"><b>Total Amount</b></label>
				<div class="col-sm-8">
					<p class="form-control-static"><%= v.get(4) %></p>
				</div>
			</div>				
            <div class="form-group">
                <div class="col-sm-offset-4 col-sm-8">
                    <button type="submit" class="btn btn-default">Make Payment</button>
                </div>
            </div>			                  
		</form>
                <%
                            }
                        }
                %>
	</div>

	<% } %>
</body>
</html>
