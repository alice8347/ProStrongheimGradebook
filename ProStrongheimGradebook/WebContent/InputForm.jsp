<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<title>Professor Strongheim's Gradebook</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-default">
<div class="container-fluid">
<div class="navbar-header">
<a class="navbar-brand" href="Input">Professor Strongheim's Gradebook</a>
</div>
<div>
<ul class="nav navbar-nav">
<li class="active"><a href="Input">Input Form</a></li>
<li><a href="Request">Search</a>
</ul>
</div>
</div>
</nav>

<div class="container">
<h3>Student record</h3>
<br />
<form class="form-horizontal" name="recordInput" id="recordInput" role="form" action="Input" method="post">
<div class="form-group">
<label class="control-label col-sm-2" for="classID">Class ID: </label>
<div class="col-sm-10">
<input type="text" class="form-control" name="classID" id="classID" placeholder="Enter class ID">
</div>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="studentID">Student ID: </label>
<div class="col-sm-10">
<input type="text" class="form-control" name="studentID" id="studentID" placeholder="Enter student ID">
</div>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="assignment">Assignment Name: </label>
<div class="col-sm-10">
<input type="text" class="form-control" name="assignment" id="assignment" placeholder="Enter assignment name">
</div>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="type">Type: </label>
<div class="col-sm-10">
<select class="form-control" name="type" id="type">
<option data-hidden="true">Select Type</option>
<option>Homework</option>
<option>Quiz</option>
<option>Test</option>
<option>Project</option>
</select>
</div>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="date">Date (YYYY-MM-DD): </label>
<div class="col-sm-10">
<input type="text" class="form-control" name="date" id="date" placeholder="Enter date">
</div>
</div>

<div class="form-group">
<label class="control-label col-sm-2" for="grade">Grade (0-100): </label>
<div class="col-sm-10">
<input type="text" class="form-control" name="grade" id="grade" placeholder="Enter grade">
</div>
</div>

<div class="form-group"> 
<div class="col-sm-offset-2 col-sm-10">
<button type="submit" class="btn btn-default" name="record" id="record">Submit</button>
</div>
</div>
</form>
</div>

</body>
</html>