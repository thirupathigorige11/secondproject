<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<title>Indent Receive</title>
</head>
<body>
	<div align="center" style="color: green;">
		<c:choose>
  			<c:when test="${empty Message}">
	    		Indent sending Successfully...
	  		</c:when>
	 		<c:otherwise>
	    		<span style="color: red;"> ${Message} </span><br/>
	    		
	  		</c:otherwise>
		</c:choose>
	</div>	
	
	
	
	
</body>
</html>