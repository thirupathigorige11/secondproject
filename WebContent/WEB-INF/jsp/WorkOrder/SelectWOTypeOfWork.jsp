<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="./../CacheClear.jsp" />  
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">


<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

</head>

<style>
.anchorblue{color:blue;}
table.dataTable {border-collapse:collapse !important;font-weight: bold;text-align: center;}
.table>thead:first-child>tr:first-child>th{font-size:15px;font-weight: bold;text-align: center;}
.btn-inline{padding:4px 12px;}
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
</style>

<body class="nav-md">
	<div class="container body">
	<div class="">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">
					<div class="clearfix"></div>
					<jsp:include page="./../SideMenu.jsp" />
				</div>
			</div>
			<jsp:include page="./../TopMenu.jsp" />
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">${pageName}</li>
					</ol>
				</div>
		
		<p class="text-center">${responseMessage}</p>
		<p class="text-center">${responseMessage1}</p>
		
		<div class="col-md-12">
		 <div class="container viewpaymentpending-container">
			 <font size="4" color="red" face="verdana">${errorMessage}</font>
			 <div class="col-md-12">
			
				 <form class="form-inline" name="myForm" action="${urlForActionTag}"> 
				 <div  class="form-group">
				 <label>Type Of Work Order :</label>
				 <select id="woTypeOfWork" name="woTypeOfWork" class="custom-combobox form-control indentavailselect">	
					 <option value="">--Select--</option>
 					 ${workOrderType}
				 </select>
			 </div>
			  <input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="Submit" onclick="return validate()">
		</form>
		</div>
		</div>
  <div class="clearfix"></div>
 
</div>
</body>
   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>
	 
		
		$(document).ready(function() {
			   $(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
 		 });
		function validate() {
			var siteId = $('#woTypeOfWork').val();
			if(siteId == ""){
				alert("Please select work order type.");
				$('#woTypeOfWork').focus();
				return false;
			}
			$('.loader-sumadhura').show();
		}
   </script>
</html>
