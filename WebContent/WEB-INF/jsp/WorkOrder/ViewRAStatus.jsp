<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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

<jsp:include page="./../CacheClear.jsp" />  
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/loader.css" rel="stylesheet">
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
anchorblue{color:blue;}
table.dataTable {border-collapse:collapse !important;font-weight: bold;text-align: center;}
.table>thead:first-child>tr:first-child>th{font-size:15px;font-weight: bold;text-align: center;}
.table-bordered>thead>tr>th {border:1px solid #000 !important}
.table-bordered>tbody>tr>td {border:1px solid #000 !important}
.tblheaderall{background-color:#ccc !important;color:#000 !important;}
table.dataTable {border-collapse: collapse!important;}
.anchorblue{color:blue;}
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
</style>
<script>
if (typeof(Storage) !== "undefined") {
	debugger;
    sessionStorage.setItem("${UserId}tempRowsIncre12",1);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>

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


			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<c:choose>
						<c:when test="${isTempBillUpdatePage==true }">
						<li class="breadcrumb-item active">Update RA/ADV Bills </li>
						</c:when>
						<c:otherwise><li class="breadcrumb-item active"> ADV/RA/NMR Bills Status</li></c:otherwise>
						</c:choose>
						
					</ol>
				</div>
				<!-- loader -->
				<div class="overlay_ims"></div> <!-- -->
			    <div class="loader-ims" id="loaderId"> <!--   -->
				 <div class="lds-ims">
					<div></div><div></div><div></div><div></div><div></div><div></div>
				 </div>
					<div id="loadingimsMessage">Loading...</div>
				</div>
		
				<div class="text-center" > <h4 style="color:green;"> ${responseMessage}</h4> </div>
			
				<div class="text-center" ><h4 style="color:red;"> ${responseMessage1}</h4></div>
				
                  

				<div class="col-md-12">
				<div class="container viewpaymentpending-container">
					 <font size="4" color="red" face="verdana">${errorMessage}</font>
					 <form name="myForm" id="myForm"  class="form-inline text-center" action="showWOBillsDetails.spring"> <!-- showBillCreationDetails.spring -->
				     
				    <div class="form-group MrgRight20">
				 <!-- <div class="inwardInvoice" style="margin-top: 20px;"> -->
					<label>Temp Bill Number :</label>
				     </div>
				     <div class="form-group MrgRight20">
				     	 <input type="text" class="form-control" name="tmpBillNo" id="tmpBillNo" autocomplete="off" />
				     	 <input type="hidden" value="${billType }" name="billType">
				     	 <input type="hidden" value="true" name="status">
 			 			 <input type="hidden" value="${SiteId}" name="site_id" id="site_id">
 			 		 	 <input type="hidden" name="isBillUpdatePage" value="${isTempBillUpdatePage}">
 			 		 	 <input type="hidden" name="isSiteWiseStatusPage" id="isSiteWiseStatusPage" value="${isSiteWiseStatusPage}">
						 <input type="hidden" class="form-control" name="" id="workOrderNumber" autocomplete="off" value=""/>
				     </div>
				     <div id="showHideSite"  class="form-group">
							<label>Site :</label>
							<select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control indentavailselect">	</select>
					 </div>
				     <div class="form-group">
				        <input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="submit" onclick="return validate()">
				     </div>
				  </form>
				</div>
	        	<div class="container Mrgtop10">

			    <table id="tblnotification" class="table table-striped table-bordered " cellspacing="0">
				<thead style="color: black;">
					<tr class="tblheaderall">
						<th>Create Date</th>
						<th>Bill Date</th>
						<th>Temp Bill Number</th>
						<th>Bill Number</th>
						<th>Payment Type</th>
						<th>Work Order Number</th>
						<th>Work Order Name</th>
						<th>Status</th>
						<th>Contractor Name</th>
						<th>Pending Emp Name</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${workOrderList }" var="workorder">
						<tr>
							<td>${workorder.entryDate }</td>
							<td>${workorder.paymentReqDate }</td>
							<td><a class="anchorblue" href="showWOBillsDetails.spring?tmpBillNo= ${workorder.tempBillNo}&billType=${workorder.paymentType }&site_id=${workorder.siteId}&isBillUpdatePage=${isTempBillUpdatePage}&status=true&isSiteWiseStatusPage=${isSiteWiseStatusPage}">${workorder.tempBillNo}</a></td>
							<td>${workorder.permanentBillNo }</td>
							<td>${workorder.paymentType }</td>
							<td><a class="anchorblue" target="_blank" href="getMyCompletedWorkOrder.spring?WorkOrderNo=${workorder.workOrderNo }&workOrderIssueNo=${workorder.workOrderIssueId }&site_id=${workorder.siteId}&operType=1&isUpdateWOPage=false">${workorder.workOrderNo }</a></td>
							<td>${workorder.workOrderName}</td>
							<td>${workorder.status}</td>
							<td>${workorder.contractorName }</td>
							<td> ${workorder.pendingEmpId}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
   </div>
</body>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
	<script>
		function validate() {
			var tmpBillNo=$("#tmpBillNo").val();
			var dropdown_SiteId=$("#dropdown_SiteId").val();
			var isSiteWiseStatusPage="${isSiteWiseStatusPage}";
			if(tmpBillNo.length==0){
				alert("Please enter bill number.");
				$("#tmpBillNo").focus();
				return false;
			}
			if (!isNum(tmpBillNo)){
				  alert("Please enter only number.");
				  $("#tmpBillNo").focus();
				  $("#tmpBillNo").val("");
				  return false;
			  }
			if(isSiteWiseStatusPage=="true"){
				if(dropdown_SiteId.length==0){
					alert("Please select site name.");
					return false;
				}
			}
		}
		
		function isNum(value) {
			  var numRegex=/^[0-9]+$/;
			  return numRegex.test(value)
		}
		//this code for to active the side menu 
		var referrer="";//"viewContractorBillStatus.spring";
		var isSiteWiseStatusPage="${isSiteWiseStatusPage}";
		if(isSiteWiseStatusPage=="true"){
			referrer="siteWiseContractorBillStatus.spring";
		}
		if(referrer.length!=0){
			 $SIDEBAR_MENU.find('a').filter(function () {
		           var urlArray=this.href.split( '/' );
		           for(var i=0;i<urlArray.length;i++){
		        	 if(urlArray[i]==referrer) {
		        		 return this.href;
		        	 }
		           }
		    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
		}
		$(document).ready(function() {
				
			
			if("${isSiteWiseStatusPage}"=="true"){	
				$("#showHideTempWO").show();
			$.ajax({
				  url : "./loadAllSites.spring",
				  type : "GET",
				  dataType : "json",
				  success : function(resp) {
				 
					var data="";	 
					data+="<option value=''></option>";
					$.each(resp,function(index,value){
						if("${strSiteId}"==value.SITE_ID){
							data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
						}else{
							data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
						}
					});  
					 $("#dropdown_SiteId").html(data);		
				  },
				  error:  function(data, status, er){
				
					  }
			 });
			}else{
				$("#showHideSite").hide();
			}
			
			$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
				$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		});
		//to ditroy loaders
		$(window).load(function() {debugger;
			 $(".overlay_ims").hide();	
			 $(".loader-ims").hide();
		});
	</script>
</html>
