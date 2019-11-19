<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<title>Closing Balance Details</title>
<style>
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

<script type="text/javascript">


	function validate() {
		var to = document.getElementById("ReqDateId2").value;
		var siteId = document.getElementById("site").value;
		 if (siteId == "") {
				alert("Please select Site !");
				return false;
		}
		if (to == "") {
			alert("Please selectTo Date !");
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

					<jsp:include page="SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="TopMenu.jsp" />  

			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Library</li>
					</ol>
				</div>

<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
		<form:form  id="indentIssueFormId" action="getClosingBalanceDetails.spring" class="form-horizontal">
		<div class="form-group">
			<label class="control-label col-sm-1">To Date :</label>
			<div class="col-sm-3">
				<input id="ReqDateId2" type="text" name="toDate" value="${toDate}" class="form-control" autocomplete="off">
			</div>
			<div class="col-sm-2">
				<input type="submit" value="Submit" id="saveBtnId" onclick="return validate();" class="btn btn-primary">
			</div>
		</div>
				
							<br/><br/><br/>

<div>${displayErrMsg}</div>
</form:form>

				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered" cellspacing="0">
						<thead>

							<tr>
    				<th>Product Name</th>
    				<th>Sub Product Name</th>
    				<th>Child Product Name</th>
    				<th>Measurement</th>
    				<th>Quantity</th>
    				<th>Total Balance</th>
    				<th>Site</th>
    				<th>Date Time</th>
    				
				</tr>

						</thead>
						<tbody>
				<c:forEach items="${closingBalanceDts}" var="element">  
				<tr>
					<td>${element.productName}</td>
					<td>${element.subProductName}</td>
					<td>${element.childproductName}</td>
					<td>${element.measurementid}</td>
					<td>${element.quantity}</td>
					<td>${element.totalAmt}</td>
					<td>${element.siteName}</td>
					<td>${element.date}</td>
				</tr>
				</c:forEach>
				</tbody>
					</table>
										<div class="elements"> Grand Total Amount : <input
						type="text" style="text-align: right" name="grandTotalAmt"
						value="${requestScope['grandTotalAmount']}" readonly />
					</div>
				</div>
           <%
				   }
           %>
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({dateFormat: 'dd-M-y'});
		  $("#ReqDateId2").datepicker({dateFormat: 'dd-M-y'});
		  return false;
	});
		$(document).ready(
				
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable();
				});

		$(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		});
	</script>

</body>
</html>
