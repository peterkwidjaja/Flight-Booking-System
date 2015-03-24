<%-- 
    Document   : login
    Created on : Mar 24, 2015, 2:54:20 PM
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
            
            
            <h1>Login</h1>
            <form class="form-inline" action="loginStatus" method="POST">
               <div class="form-group">
                   <label for="username">Username</label>
                   <input type="text" class="form-control" name="username" id="username" placeholder="Enter username">
               </div>
               <div class="form-group">
                   <label for="password">Password</label>
                   <input type="password" class="form-control" name="password" id="password" placeholder="Enter password">
               </div>
               <button type="submit" class="btn btn-default">Submit</button>
           </form>

       </div>
       <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
       <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
   </body>
   </html>
