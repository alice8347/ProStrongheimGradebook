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
<li><a href="Input">Input Form</a></li>
<li class="active"><a href="Request">Search</a>
</ul>
</div>
</div>
</nav>

<div class="container">
<h3>Assignment weights</h3>
<br />
<script type="text/javascript">
	function validateForm() {
		var a = document.forms["weightInput"]["homework"].value;
		var b = document.forms["weightInput"]["quiz"].value;
		var c = document.forms["weightInput"]["test"].value;
		var d = document.forms["weightInput"]["project"].value;
		if (a == null || a == "" || b == null || b == "" || c == null || c == "" || d == null || d == "") {
			alert("Please fill all weights.");
			return false;
		}
		
		var sum = parseFloat(a) + parseFloat(b) + parseFloat(c) + parseFloat(d);
		if (sum != 1) {
			alert("The total weights should be 1.");
			return false;
		}
	}
</script>
<form class="form-inline" name="weightInput" id="weightInput" role="form" onsubmit="return validateForm()" action="Request" method="post">
<div class="form-group">
<label for="studentID">Homework: </label>
<input type="text" class="form-control" name="homework" id="homework" placeholder="Enter weight">
</div>

<div class="form-group">
<label for="studentID">Quiz: </label>
<input type="text" class="form-control" name="quiz" id="quiz" placeholder="Enter weight">
</div>

<div class="form-group">
<label for="studentID">Test: </label>
<input type="text" class="form-control" name="test" id="test" placeholder="Enter weight">
</div>

<div class="form-group">
<label for="studentID">Project: </label>
<input type="text" class="form-control" name="project" id="project" placeholder="Enter weight">
</div>

<div class="form-group"> 
<button type="submit" class="btn btn-default" name="weightSub" id="weightSub">Submit</button>
</div>
</form>
</div>
<br />
<div class="container">
<h3>Input filters or leave them blank to get all records</h3>
<br />
<form class="form-inline" name="search" id="search" role="form" action="Request" method="post">
<div class="form-group">
<label for="classID">Class ID:</label>
<input type="text" class="form-control" name="classID" id="classID" placeholder="Enter class ID">
</div>
<div class="form-group">
<label for="studentID">Student ID:</label>
<input type="text" class="form-control" name="studentID" id="studentID" placeholder="Enter student ID">
</div>
<div class="form-group">
<label for="type">Type:</label>
<select class="form-control" name="type" id="type">
<option data-hidden="true">Select Type</option>
<option>Homework</option>
<option>Quiz</option>
<option>Test</option>
<option>Project</option>
</select>
</div>

<div class="form-group"> 
<button type="submit" class="btn btn-default" name="allAssi" id="allAssi">All assignments</button>
<button type="submit" class="btn btn-default" name="average" id="average">Average</button>
<button type="submit" class="btn btn-default" name="maxMin" id="maxMin">Highest and Lowest</button>
</div>
</form>
</div>
<br />
<div class="container">
${content}
</div>

</body>
</html>