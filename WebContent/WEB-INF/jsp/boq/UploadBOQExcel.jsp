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
<jsp:include page="../CacheClear.jsp" />
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
<title>Upload Site Wise BOQ</title>
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
	font-size: 14px;
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
		$(".overlay_ims").show();
		$(".loader-ims").show();
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

					<jsp:include page="../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="../TopMenu.jsp" />  

			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">BOQ</a></li>
						<li class="breadcrumb-item active">Upload Excel</li>
					</ol>
				</div>
			    <!-- <div class="loader-sumadhura" style="display: none;z-index:9999;">
							<div class="lds-facebook">
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
							</div>
						<div id="loadingMessage">Loading...</div>
				</div> -->
				<div class="overlay_ims" style="display:none;"></div>
					 <div class="loader-ims" id="loaderId" style="display:none;z-index:999999;"> 
						<div class="lds-ims">
							<div></div><div></div><div></div><div></div><div></div><div></div></div>
						<div id="loadingimsMessage">Loading...</div>
					</div>
				<div class="col-md-12">
					<div class="container border-inwards-box">
						<label class="success"><c:out
								value="${requestScope['succMessage']}"></c:out> </label>
						<form class="form-horizontal" action="SaveBOQExcelData.spring" method="POST"  enctype="multipart/form-data">
							 	<!-- Site Selection START -->
							 	              <div class="col-md-4 col-md-offset-4 col-sm-9 col-sm-offset-2">
							 	               <c:if test="${showSiteSelection}">
											    <div class="col-md-12 col-sm-12"><div class="form-group">
														<label class="col-md-5 col-sm-4 text-left">Site :</label>
														<div class="col-md-7 col-sm-4"><select id="dropdown_SiteId" name="dropdown_SiteId" onchange="setSiteId()"	class="custom-combobox form-control">
														</select></div>
	 					    						</div></div>
	 					    						</c:if>
											     <div class="col-md-12 col-sm-12">
											      <div class="form-group">
										         <label for="todate" class="col-md-5 col-sm-4 text-left">Upload BOQ :</label>
										         <div class="col-md-7 col-sm-4">
											     <input type="file" name="file" >
										         </div>
										        </div>
											     </div>
					 						</div>
								<div class="col-md-12 col-xs-12 col-sm-12 text-center center-block">
									<button type="submit" value="Submit" id="saveBtnId"
										class="btn btn-warning btn-sub-inwards"
										onclick="return validate();">Upload</button>
									<div><font color="red" size="3"><span id="displayErrMsg">${displayErrMsg}</span></font></div>
								</div>
								<input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">
								
							
						</form>

				<c:choose>
			 		<c:when test = "${showSiteSelection}">
						<a  id="anchor_Viewblocks" style="text-decoration: underline;color: blue;" >View All Blocks, Floors & Flats</a>
			 		</c:when> 
					<c:otherwise>
						<a href="viewAllBlocksFloorsFlats.spring" style="text-decoration: underline;color: blue;" >View All Blocks, Floors & Flats</a>
					</c:otherwise>
				</c:choose>

					</div>
				</div>
				
			</div>
			<!-- /page content -->
			
		</div>
	</div>

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
	//setting siteid
	function setSiteId(){debugger;
		var siteId=$("#dropdown_SiteId").val();
		var href="viewAllBlocksFloorsFlats.spring?siteId="+siteId;
		$("#anchor_Viewblocks").attr("href", href);
	}
	
	//this code for to active the side menu 									
	var referrer=$("#pageType").val();
	if(referrer==''||referrer==null){referrer="empty";}
	$SIDEBAR_MENU.find('a').filter(function () {
	var urlArray=this.href.split( '/' );
	for(var i=0;i<urlArray.length;i++){
	if(urlArray[i]==referrer) {
	return this.href;
	}
	}
	}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');




	</script>
</body>
</html>
