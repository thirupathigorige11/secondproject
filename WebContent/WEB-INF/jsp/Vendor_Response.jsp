<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>

</head>

<style>
.btn-check{
border-radius:50%;
background-color:#008000;
height:50px;
width:50px;
border:1px solid #008000;
}
.btn-check i{
font-size: 25px;
color: #fff;
}
.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
.inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

}
.media-style{
width:39% !important;

}
.submitstyle{
margin-top: 20px;
width: 23% !important;
}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>

<body class="nav-md">
	<div class="container body">
		<div class="main_container" id="main_container">
<%-- 			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="SideMenu.jsp" />

				</div>
			</div> --%>
				<jsp:include page="Vendor_TopMeneu.jsp" />  


			<!-- page content -->
		<!-- 	<div class="right_col" role="main"> -->
	<div class="Mrgtop10">	
	<div class="text-center center-block">
	 <button class="btn btn-check"><i class="fa fa-check" aria-hidden="true"></i></button>
	</div>	
	<h3 style=color:green;text-align:center;> <c:out value="${requestScope['message']}"></c:out></h3>
	<h3 style=color:red;text-align:center;> <c:out value="${requestScope['message1']}"></c:out></h3>
	<h3 style=color:green;text-align:center;> <c:out value="${requestScope['response']}"></c:out></h3>
	<h3 style=color:red;text-align:center;> <c:out value="${requestScope['response1']}"></c:out></h3>
	<h3 style=color:green;text-align:center;> <c:out value="${requestScope['msg']}"></c:out></h3>
 
  <%-- <center><font color="red" size="5"><c:out value="${requestScope['message1']}"></c:out> </font></center>

<center><font color="green" size="5"><c:out value="${requestScope['response']}"></c:out> </font></center>
 
  <center><font color="red" size="5"><c:out value="${requestScope['response1']}"></c:out> </font></center> --%>




				</div>
                  
</div></div>
 


  

<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>

<script>		
 history.pushState(null, null, location.href);
	window.onpopstate = function () {
	history.go(1);
	}; 
</script>
</body>
</html>
