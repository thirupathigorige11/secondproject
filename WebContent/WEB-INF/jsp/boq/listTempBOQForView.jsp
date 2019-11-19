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
<jsp:include page="../CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pending For Approval BOQ</title>
<style>
table.dataTable{border-collapse: collapse !important;
}

.success,.error {
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
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;

		if (from == "" && to == "") {
			alert("Please select From Date or To Date !");
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

					<jsp:include page="../SideMenu.jsp" />  
						
					</div>
					</div>
					<jsp:include page="../TopMenu.jsp" />  

			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Pending For Approval BOQ</li>
					</ol>
				</div>
				<div class="col-md-12">
				<center><span><font color=green size=4 face="verdana">${responseMessage}</font></span></center>
				<center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center>
				 <!-- Site Selection START -->
				 <c:if test="${showSiteSelection}">
					<div class="col-md-12 border-inwards-box">
					 <div class="col-md-8 col-md-offset-2">
					  <form class="form-inline" name="myForm" action="viewSiteWiseTempBOQ.spring"> 
	  					    <div class="form-group">
							<label>Site :</label>
							<select id="dropdown_SiteId" name="dropdown_SiteId"
							class="custom-combobox form-control indentavailselect">
							</select>
	 					    </div>
	  						<input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="submit" >
	 					
					</form>
					</div>
					 </div>
				</c:if>
				<!-- Site Selection END -->
				
				
				<div class="clearfix"></div>
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered st-table table-new" cellspacing="0">
						<thead >

					  <tr class="tblheaderall">
							<th>Created Date</th>		
		    				<th>Temp BOQ Number</th>
		    				<th>Created Employee</th>
		    				<th>Site Name</th>
		    				<th>Type of Work</th>
		    				<th>BOQ Type</th>
    				   </tr>

						</thead>
						<tbody>
				<c:forEach items="${listofPendingBOQForApproval}" var="element">  
				<tr class="tablerow">
				    <td style="color: black;">${element.strTemBOQCreatedDate}</td>
				    <c:choose>
				    <c:when test="${element.boqType.equals('REVISED')}">
					<td><a href="#" onclick="ApprovalBOQDetails(${element.intTempBOQNo}, ${element.strSiteId}, '${element.boqType}', ${element.strSerialNumber}, 'revise', '${pageHighlightURL}')" style="text-decoration: underline;color: blue;">${element.intTempBOQNo}</a><img src="images/spinner.gif" class="spinnercls${element.strSerialNumber}" id="spinner${element.strSerialNumber}" style="display:none; height: 40px;position: absolute; top: 0px;right:50%"></img></td>
					</c:when>
					<c:otherwise>
					<td><a href="#" onclick="ApprovalBOQDetails(${element.intTempBOQNo}, ${element.strSiteId}, '${element.boqType}', ${element.strSerialNumber}, 'normal', '${pageHighlightURL}')" style="text-decoration: underline;color: blue;">${element.intTempBOQNo}</a><img src="images/spinner.gif" class="spinnercls${element.strSerialNumber}" id="spinner${element.strSerialNumber}" style="display:none; height: 40px;position: absolute; top: 0px;right:50%"></img></td>
					</c:otherwise>	
					</c:choose> 
					<td style="color: black;">${element.strTemBOQCreatedEmployeeName}</td>
					<td style="color: black;">${element.strSiteName}</td>
					<td style="color: black;">${element.typeOfWork}</td>
					<td style="color: black;">${element.boqType}</td>
				</tr>
				</c:forEach>
				</tbody>
					</table>
				</div>
				<!-- /page content -->
				</div>
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
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
		//this function for redirect to page with loaders
		function ApprovalBOQDetails(TempBOQNo, SiteId, boqType, serialNo, type, pageHighlightURL){debugger;
		    $(".spinnercls"+serialNo).show();
		    if(type=="revise"){
		    	window.location.href="getPendingForApprovalReviseBOQMajorHeads.spring?tempBOQNo="+TempBOQNo+"&siteId="+SiteId+"&boqType="+boqType+"&pageHighlightURL="+pageHighlightURL+"&viewTempBoq=true";
		    }else{
		    	window.location.href="getPendingForApprovalBOQDetails.spring?tempBOQNo="+TempBOQNo+"&siteId="+SiteId+"&boqType="+boqType+"&pageHighlightURL="+pageHighlightURL+"&viewTempBoq=true";
		    }
			for(var i=0; i<$(".tablerow").length;i++){
				if((i+1)!=serialNo){
					 $(".spinnercls"+(i+1)).hide();
				}
			}
		}
		
		$(document).ready(function() {
					$(".up_down").click(function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
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
		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
		$(document).ready(function(){
			  $('.SubProduct1').keyup(function(){
			    $(this).attr('title',$(this).val());
			  });
		});
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
