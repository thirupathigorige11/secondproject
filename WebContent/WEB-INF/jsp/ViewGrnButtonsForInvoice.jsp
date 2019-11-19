<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="func"%>
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="CacheClear.jsp" />
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<!-- //ACP -->
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.table>tbody+tbody {
    border-top: 1px solid #000;
}
table.dataTable {border-collapse:collapse !important;}
.pagination>.active>a, .pagination>.active>a:focus, .pagination>.active>a:hover,
	.pagination>.active>span, .pagination>.active>span:focus, .pagination>.active>span:hover
	{
	z-index: 3;
	color: #000;
	cursor: default;
	background-color: #ddd;
	border-color: #000;
}

.pagination>li>a, .pagination>li>span {
	position: relative;
	float: left;
	padding: 6px 12px;
	margin-left: -1px;
	line-height: 1.42857143;
	color: #000;
	text-decoration: none;
	background-color: #fff;
	border: 1px solid #000;
}

.table>thead>tr>th {
	background-color: #ccc;
	border: 1px solid #000;
	color: #000;
}

.table>tbody>tr>td {
	border: 1px solid #000;
}

.form-control {
	height: 34px;
}

.success, .error {
	text-align: center;
	font-size: 24px;
    color: #087708;
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
.border-inwards-box {
    background-color: #fff !important;
    border:1px solid #000;
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
		$("#displayErrMsg").hide();
	
		/* var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;

		if (from == "" && to == "") {
			alert("Please select From Date or To Date !");
			return false;
		}

		var siteId = document.getElementById("dropdown_SiteId").value;
		if (siteId == "" || siteId == null) {
			alert("Please select the Site !");
			return false;
		} */

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
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Inwards From PO</li>
					</ol>
				</div>
				<div class="col-md-12">
					<div class="container border-inwards-box">
					    <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
					    	 <button class="btn btn-check"><i class="fa fa-check" aria-hidden="true"></i></button>
					    </div>
					   <div class="col-md-12 text-center center-block" style="margin-top: 20px;margin-bottom: 40px;">
					    	<label class="success"><c:out value="Inwards done Successfully"></c:out> </label>
					    </div>
						
						<form id="ViewGrnButtonsPageId">							 	             
							<input type="hidden" name="invoiceNumber" value="${invoiceNumber}"/>
							<input type="hidden" name="vendorId" value="${vendorId}"/>
							<input type="hidden" name="invoiceDate" value="${invoiceDate}"/>
							<input type="hidden" name="indentEntryId" value="${indentEntryId}"/>
							<input type="hidden" name="siteId" value="${siteId}"/>
							<input type="hidden" name="poEntryId" value="${poEntryId}"/>
							<input type="hidden" name="urlName" value="${urlName}"/> 
							<input type="hidden" name="fromGrnButtonsPage" value="true"/> 
							<div class="col-md-12 col-xs-12 col-sm-12 text-center center-block" style="margin-bottom: 40px;">
								<button type="button" value="View GRN" id="saveBtnId" class="btn btn-warning btn-sub-inwards" onclick="viewGrn()">View GRN</button>
								<button type="button" value="View Credit Note" id="saveBtnId" class="btn btn-warning btn-sub-inwards" onclick="viewCreditNoteGrn()">View Debit Note</button>
							</div>							
						</form>
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
	$(document).ready(function() {
		
		debugger;
		//ajax call for site id
		$.ajax({
			  url : "./loadAllSites.spring",
			  type : "GET",
			 dataType : "json",
			  success : function(resp) {
				  debugger;
				var siteId=sessionStorage.getItem("siteIdAfterRefresh");
				 var data="";	 
				 data+="<option value=''></option>";
				 
				$.each(resp,function(index,value){
				debugger;
				if("${strSiteId}"==value.SITE_ID){
					debugger;
					data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
				}else{
					data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
				}
					});
				$("#dropdown_SiteId").html(data);	
			  }
			  });
		 });
	
	
	function viewGrn(){
		document.getElementById("ViewGrnButtonsPageId").action = "getGrnDetails.spring";
		document.getElementById("ViewGrnButtonsPageId").method = "POST";
		document.getElementById("ViewGrnButtonsPageId").submit();
	}
	
	function viewCreditNoteGrn(){
		document.getElementById("ViewGrnButtonsPageId").action = "getCreditNoteGrn.spring";
		document.getElementById("ViewGrnButtonsPageId").method = "POST";
		document.getElementById("ViewGrnButtonsPageId").submit();
	}
	
	history.pushState(null, null, location.href);
	window.onpopstate = function () {
	history.go(1);
	};
	</script>
</body>
</html>
