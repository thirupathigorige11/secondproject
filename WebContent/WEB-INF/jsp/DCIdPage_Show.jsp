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

<jsp:include page="CacheClear.jsp" /> 
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/issueToOtherSite_NonReturnableView.js" type="text/javascript"></script>

</head>

<style>
.abc {
	color: red;
}
.btn-ward{
  	height: 34px;
    width: 136px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
/* .inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

} */
</style>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js">

  function validate() {
	  alert('abm' );
	var requestId = document.getElementById("RequestId").value;
	if (requestId == "") {
		alert("Please select RequestId !");
		return false;
	}
}


</script>

<body class="nav-md">
	<div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="SideMenu.jsp" />

				</div>
			</div>
			<jsp:include page="TopMenu.jsp" />


			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Convert DC to Invoice</li>
								<li class="breadcrumb-item active">with price</li>
					</ol>
				</div>


				</div>
<!-- //******************************************************		 -->		
				
			<div class="container" style="background-color: #ccc; width: 39%; margin: 9% auto; padding: 2px; text-align: center; font-size: 20px; height: 200px; border: 1px solid gray; box-shadow: 2px 3px;">
					<form name="myForm" action="InwardsFromDcForm.spring">
						<div class="inwardInvoice" style="margin-top: 55px">
							<span class="alert-msg"><c:out value="${requestScope['Message']}"></c:out> </span>
							<div class="inward-dialogbox">
								<label class="inward-invoice">Invoice ID:</label> 
								<input type="text" name="DCNumber" id="DCNumber" autocomplete="off" /><br />
								
								
								<button type="submit" value="submit" class="btn btn-warning btn-lg" onclick="return validate()" style="margin-top: 20px; width: 23%;">
									Submit
								</button>
							</div>
					</form>
				</div>
				
				
				
				
<!--   //*****************************************************  -->               
<!-- 	<div class="container" style="background-color: #ccc; width: 39%; margin: 10% auto; padding: 2px; text-align: center; font-size: 20px; height: 200px; border: 1px solid gray; box-shadow: 2px 3px;">
 <form name="myForm" action="InwardsFromDcForm.spring"> 
<div class="inwardInvoice" >
	<lable class="inward-invoice">Dc Form ID:</lable>
		<input type="text" name="DCNumber" id="DCNumber" autocomplete="off" /></br>
		<input type="submit" value="submit" class="btn-ward btn btn-warning"  onclick="return validate()" class="btn btn-primary">
</div>
</form>
</div> -->
</div>
</div>
  </body>

<script src="js/jquery-ui.js"></script>

					<script src="js/custom.js"></script>

					<script>
			$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});

										});

						$(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
						});
					</script>

</html>
