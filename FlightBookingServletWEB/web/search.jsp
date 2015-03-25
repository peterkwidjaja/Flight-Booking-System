
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
</body>
</html>
