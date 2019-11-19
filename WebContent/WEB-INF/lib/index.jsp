<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<!-- <a href="getEmployeedtls.spring">Click here</a> -->

<form action="getEmployeedtls.spring">
<select name="option">
<option value="empid">EmpId</option>
<option value="firstname">FirstName</option>
<option value="lastname">LastName</option>
<option value="email">Email</option>
<option value="mobileno">MobileNo</option>
</select>
<input type="text">
<input type="submit" value="submit">
</form>
</body>
</html>