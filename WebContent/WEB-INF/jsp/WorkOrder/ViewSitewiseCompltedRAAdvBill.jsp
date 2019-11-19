
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
.anchorblue{color:blue;cursor: pointer;}
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
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Work Order</a></li>
						<li class="breadcrumb-item active">View ADV/RA/NMR Bills</li>
					</ol>
				</div>
		
				<div class="text-center" > <h4 style="color:green;"> ${responseMessage}</h4> </div>
			
				<div class="text-center" ><h4 style="color:red;"> ${responseMessage1}</h4></div>
					<div class="col-md-12 border-inwards-box">
					 <div class="col-md-8 col-md-offset-2">
					  <form class="form-inline" name="myForm" action="viewSitewiseCompletedBills.spring"> 
	  					    <div class="form-group">
								<label>Site :</label>
								<select id="dropdown_SiteId" name="dropdown_SiteId" 	class="custom-combobox form-control indentavailselect">
								</select>
								<input type="hidden" name="isSiteWiseStatusPage" id="isSiteWiseStatusPage" value="${isSiteWiseStatusPage}">
	 					    </div>
	  						<input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="submit" >
	 					
					</form>
					</div>
					 </div>
				<div class="clearfix"></div>                  

				<div class="col-md-12">
	
			<div class="container Mrgtop10">
	
				<table id="tblnotification"
					class="table table-striped table-bordered " cellspacing="0">
					<thead style="color: black;">
						<tr class="tblheaderall">
							<th>Create Date</th>
							<th>Bill Date</th>
							<th>Site Name</th>
							<th>Bill Number</th>
							
							<th>Work Order Number</th>
							<th>Payment Type</th>
							<th>Work Order Name</th>
							<th>Contractor Name</th>
							<th>Pending Emp Name</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${workOrderList }" var="workorder">
							<tr>
								<td>${workorder.entryDate }</td>
								<td>${workorder.paymentReqDate }</td>
								<td>${workorder.siteName }</td>
								<td><a class="anchorblue" href="showWOCompltedBillsDetails.spring?BillNo= ${workorder.billNo }&WorkOrderNo=${workorder.workOrderNo }&billType=${workorder.paymentType }&site_id=${workorder.siteId}&isBillUpdatePage=${isCompletedRABillUpdatePage}&status=true&isSiteWiseStatusPage=${isSiteWiseStatusPage}"> ${workorder.billNo }</a> </td>
								<td><a class="anchorblue" href="getMyCompletedWorkOrder.spring?WorkOrderNo=${workorder.workOrderNo }&workOrderIssueNo=${workorder.workOrderIssueId }&site_id=${workorder.siteId}&operType=1&isUpdateWOPage=false">${workorder.workOrderNo }</a></td>
								<td>${workorder.paymentType }</td>
								<td>${workorder.workOrderName }</td>
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
	//validating bill number and work order number
		function validate() {
			var tmpBillNo=document.getElementById("tmpBillNo").value;
			var WorkOrderNo=document.getElementById("WorkOrderNo").value;
			if(tmpBillNo.length==0){
				alert("Please enter bill number.");
				return false;
			}
			if(WorkOrderNo.length==0){
				alert("Please enter work order number.");
				return false;
			}
		}
					
		$(document).ready(function() {
			$(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			 });
			 $('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		});

		$(document).ready(function() {
			//ajax call for site id
			$.ajax({
				  url : "./loadAllSites.spring",
				  type : "GET",
				 dataType : "json",
				  success : function(resp) {
						var siteId=sessionStorage.getItem("siteIdAfterRefresh");
						var data="";	 
						data+="<option value=''></option>";
						data+="<option value='ALL'>ALL</option>";// this one is extra. to display ALL option. if u dont need this remove this line.
							 
						$.each(resp,function(index,value){
							if("${strSiteId}"==value.SITE_ID){
								data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
							}else{
								data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
							}
						});
						$("#dropdown_SiteId").html(data);	
					}
				 });
			 });
	</script>
</html>
