<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="./CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<c:if test="${isContractorEditable==false}">
<title>View Contractor Master</title>
</c:if>
<c:if test="${isContractorEditable!=false}">
<title>Update/Delete Contractor Master</title>
</c:if>
<script type="text/javascript">
if (typeof(Storage) !== "undefined") {
	debugger;
    sessionStorage.setItem("${UserId}tempRowsIncre12",1);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>
<style>
.sorting_1 a{color: #4169e1;text-decoration: none;font-size: 14px;font-weight: bold;}
.sorting_1 a:hover{color:#0000ff;text-decoration: none;font-size: 14px;font-weight: bold;}
@media only screen and (min-width:320px) and (max-width:767px){.dataTables_paginate {display:block !important;}.dataTables_info {display: block;}}
table.dataTable {border-collapse:collapse !important;}
.table-bordered thead th{
background-color:#ddd !important;
border:0.5px solid #000 !important;
font-weight:800;
}
.table-bordered tbody tr td{
border:0.5px solid #000 !important;
color:#000;
text-align:center !important; 
}
table>thead>tr>th{
text-align:center !important; 
}
.success,.error {
	text-align: center;
	font-size: 14px;
}
.form-control1{
width:168px!important;
height: 40px!important;
}
.form-control2{
width:120px!important;
height: 40px!important;
}
.popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* The actual popup */
.popup .popuptext {
	visibility: hidden;
	width: 160px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 8px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@
-webkit-keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}

}
@
keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}
</style>
</head>
<body class="nav-md">
	<div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="./SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="./TopMenu.jsp" />  

			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Masters</a></li>
												<c:if test="${isContractorEditable==false}">
					
					<li class="breadcrumb-item active">View Contractor</li>
						</c:if>
						<c:if test="${isContractorEditable!=false}">
					<li class="breadcrumb-item active">Update/Delete Contractor Details</li>
						</c:if>
						
					</ol>
				</div>
<div class="col-md-12">
<label class="success" id="succMsg" style="font-size: 15px;text-align: center;"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
<c:set var="succMessage" value=" " scope="request"></c:set>
<div class="table-responsive">
	<table id="contractorTableData" class="table table-striped table-bordered " cellspacing="0">
	<thead>
						<tr>
							<th><strong>Contractor Name</strong></th>
							<th><strong>Address</strong></th>
							<th><strong>Contact Number</strong></th>
							<th><strong>Alternate Contact Number</strong></th>
							<th><strong>E-mail</strong></th>
							<th><strong>GSTIN</strong></th>
							<th><strong>Pan Number</strong></th>
							
						</tr>

					</thead>
	
<c:forEach items="${contractorList}" var="item">
						<tr>
							<td><a href="getContractorDetails.spring?contractorId=${item.contractor_id}&isContractorEditable=${isContractorEditable}"> ${item.contractor_name} </a></td>
							<td>${item.address}</td>
							<td>${item.mobile_number}</td>
							<td>${item.alternate_mobile_number}</td>
							<td>${item.email}</td>
							<td>${item.GSTIN}</td>
							<td>${item.pan_number}</td>
						
						</tr>
					</c:forEach>
	</tbody>
	</table>
	</div>
	  </div>  
				<!-- /page content -->
			</div>
		</div>
	</div>
	<c:if test="${!empty succMessage }">
		<c:set var="succMessage" value=" " scope="request"></c:set>
</c:if>
	
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script>

		$(document).ready(function() {
			setTimeout(function(){
			//	$("#succMsg").text("");
				<c:set var="succMessage" value=" " scope="request"></c:set>
			}, 3000);
			$("myForm").submit(function() {
			    $(this).submit(function() {
			        return false;
			    });
			  //  return true;
			});
			$(".up_down").click(function() {
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
				$('#contractorTableData').DataTable();
			});

		/* $(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		}); */
		
		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
