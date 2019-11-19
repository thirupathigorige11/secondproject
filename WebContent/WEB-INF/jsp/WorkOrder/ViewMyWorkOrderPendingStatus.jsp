<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="./../CacheClear.jsp" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
.anchorblue{color:blue;}
table.dataTable {border-collapse:collapse !important;font-weight: bold;text-align: center;}
.st-table>thead:first-child>tr:first-child>th{font-size:15px;font-weight: bold;text-align: center;}
.success, .error {
	text-align: center;
	font-size: 14px;
	color:red;
}
.form-control{height:34px;}
/* .popup {
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
} */
</style>

<script type="text/javascript">
//validating from date and to date and also temp work order number
	function validate() {
		debugger;	
		/* var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value; */
		var workOrderNumber = document.getElementById("siteWiseWorkOrderNo").value;

		/* if (from == "" && to == "" && workOrderNumber == "") {
			alert("Please select from (or) to date (or) Enter work order number!");
			return false;
		} */
		
		if(isNaN(workOrderNumber)||workOrderNumber==""){
			alert("Please enter valid temp work order number!");
			document.getElementById("siteWiseWorkOrderNo").focus;
			return false;
		}
	}
</script>
</head>
<body class="nav-md">
	<div class="container body">
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
				<label class="success text-center center-block"><c:out value="${requestScope['succMessage']}"></c:out> </label>
				<div class="col-md-12">
					<div class="container viewpaymentpending-container">					
						 <form class="form-inline" action="${pageAction}">
							<div class="col-md-12">
								<!-- <div class="col-md-12"> -->
								<%--  <div class="col-md-5">
									<div class="form-group">
										<label for="date" class="col-md-3">From :</label>
										<div class="col-md-9">
											<input type="text" id="ReqDateId1" class="form-control"
												name="fromDate" value="${fromDate}" autocomplete="off" placeholder="dd-mm-yy" onkeydown="return false">
										</div>
									</div>
								</div> --%>
								<!-- <div class="col-md-2"></div> -->
								<%-- <div class="col-md-5">
									<div class="form-group">
										<label for="todate" class="col-md-3">To:</label>
										<div class="col-md-9">
										  <input type="text"
											id="ReqDateId2" class="form-control" name="toDate" placeholder="dd-mm-yy"
											value="${toDate}" autocomplete="off" onkeydown="return false">
										</div>
									</div>
								</div> --%>
								<!-- </div> -->
							
							<!-- <div class="clearfix"></div> -->
							<!-- <div class="col-md-12">
							 <div class="col-md-5 col-xs-5 ">
								<hr>
							</div>
							<div class="col-md-1 col-xs-2 text-center Mrgtop10">(Or)</div>
							<div class="col-md-6  col-xs-5 no-padding-right">
								<hr>
							</div>
							 
							</div> -->
							<!-- <hr><label class="or-class" style="position: absolute; margin-top: -33px;" for="todate">(Or)</label> -->



							<div class="col-md-12" style="margin-top: 10px;">
								<div class="form-group">
									<label for="todate" class="col-md-6 Mrgtop10">Temp Work Order Number :</label>
									<div class="col-md-6">
										<input type="text" id="siteWiseWorkOrderNo" class="form-control" name="siteWiseWorkOrderNo" value="${workOrderNumber}" autocomplete="off">
									
								</div>
								<!--<div class="form-group">
									<label for="todate">Select :</label>
								
										<select id="typeOfSelection" name="typeOfSelection" class="form-control" >
											${selectOption}
										</select>
									
								</div>-->
								</div>
							  <button type="submit" value="Submit" id="saveBtnId" class="btn btn-warning media-style " onclick="return validate();">Submit</button>
							<!-- </div> -->
							</div>
							
						</form>
						</div>
				</div>

				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="clearfix"></div>
				<div class="col-md-12 Mrgtop20">
			<div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">

							<tr>
							<th>Creation Date</th>
								<th>Work Order Date</th>
								<th>Temp Work Order Number</th>
								<th> Work Order Number</th>
								<th>Status</th>
								<th>Work Order Name</th>
								<th>Type Of Work</th>
								<th>Contractor Name</th>
								<th>Pending Dept/Emp Name</th>
							</tr>
						</thead>
						<tbody>
				<c:forEach items="${listOfWoPendingStatus}" var="element">  
				<tr>
				    <td style="color: black;">${element.workOrderCreadeDate}</td>
				    <td style="color: black;">${element.workOrderDate}</td>
					<td><a class="anchorblue" href="showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo=${element.siteWiseWONO}&tempWorkOrderIssueNo=${element.QS_Temp_Issue_Id}&siteId=${element.siteId}&status=true" title="click here to see work order details"> ${element.siteWiseWONO }</a> </td>
					<td style="color: black;">${element.workOrderNo}</td>
					<td style="color: black;">${element.workOrderStatus}</td>
					<td>${element.workOrderName}</td>
					<td>${element.typeOfWork}</td>
					<td style="color: black;">${element.contractorName}</td>
					<td style="color: black;">${element.pendingEmpId}</td>					
				</tr>
				</c:forEach>			
				<tbody>				
				</tbody>
					</table>
				</div>
				</div>
           <%
				   }
           %>				
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date()
		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date()
		  });
		  return false;
	});
		$(document).ready(function() {
				$(".up_down").click(function() {
						$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
						$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
				$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		});

		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>
</body>
</html>
