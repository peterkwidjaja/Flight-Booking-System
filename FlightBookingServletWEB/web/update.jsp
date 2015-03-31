<%-- 
    Document   : update
    Created on : Mar 24, 2015, 2:54:33 PM
    Author     : Peter K W
    --%>

    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <%@page import="java.util.ArrayList" %>
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
            <%
            ArrayList data = (ArrayList) request.getAttribute("data");
            if(data!=null){
            %>

            <h1>Update Profile</h1>
            <div id="password-form">
                <form class="form-horizontal" action="changePass" method="POST">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">Username</label>
                        <div class="col-sm-10">
                            <p class="form-control-static"><%= data.get(0) %></p>
                        </div>
                    </div>
                    <%
                        Integer resultPass = (Integer) request.getAttribute("resultPass");
                        if(resultPass!=null && resultPass==1){
                    
                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="oldPass" class="col-sm-2 control-label">Old Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="oldPass" id="oldPass" placeholder="Enter old password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                        </div>
                    </div>
                    <%
                        }
                        else{
                    %>
                    <div class="form-group">
                        <label for="oldPass" class="col-sm-2 control-label">Old Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="oldPass" id="oldPass" placeholder="Enter old password" required>
                        </div>
                    </div>
                    <%
                        }
                        if(resultPass!=null && resultPass==2){
                    %>

                    <div class="form-group has-error has-feedback">
                        <label for="newPass" class="col-sm-2 control-label">New Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass" id="newPass" placeholder="Enter new password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                        </div>
                    </div>
                    <%
                        } else{
                    %>

                    <div class="form-group">
                        <label for="newPass" class="col-sm-2 control-label">New Password</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass" id="newPass" placeholder="Enter new password" required>
                        </div>
                    </div>
                    <% }
                        if(resultPass!=null && resultPass==3){
                    %>
                    <div class="form-group has-error has-feedback">
                        <label for="newPass2" class="col-sm-2 control-label">New Password Again</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass2" id="newPass2" placeholder="Re-enter new password" aria-describedby="inputError" required>
                            <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                            <span id="inputError" class="sr-only">(error)</span>
                        </div>
                    </div>
                    <%
                        } else{
                    %>
                    <div class="form-group">
                        <label for="newPass2" class="col-sm-2 control-label">New Password Again</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" name="newPass2" id="newPass2" placeholder="Re-enter new password" required>
                        </div>
                    </div>
                    <% } %>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <div id="details-form">
                <form class="form-horizontal" action="updateDetails" method="POST">   
                    <div class="form-group">
                        <label for="contactNo" class="col-sm-2 control-label">Contact Number</label>
                        <div class="col-sm-10">
                            <input type="number" class="form-control" name="contactNo" id="contactNo" placeholder="Enter contact number" value="<%= data.get(1) %>" required>
                        </div>

                    </div>
                    <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">Email Address</label>
                        <div class="col-sm-10">
                            <input type="email" class="form-control" name="email" id="email" placeholder="Enter email address" value="<%= data.get(2) %>" required>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-default">Submit</button>
                </form>
            </div>
            <%
        }
        else{
        %>
        <h1>You need to login first!</h1>
        <% } %>
    </div>
</body>
</html>
