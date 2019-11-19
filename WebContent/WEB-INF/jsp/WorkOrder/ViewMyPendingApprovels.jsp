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
<script>

if (typeof(Storage) !== "undefined") {
	debugger;
    sessionStorage.setItem("${UserId}tempRowsIncre12",1);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>
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
						<li class="breadcrumb-item active">Pending Work Order For Approval</li>
					</ol>
				</div>
		
		<p class="text-center">${responseMessage}</p>
		<p class="text-center">${responseMessage1}</p>
		
		<div class="col-md-12">
		   <c:if test="${isCommonApproval ne true}">
		 <div class="container viewpaymentpending-container">
			 <font size="4" color="red" face="verdana">${errorMessage}</font>
			 <div class="col-md-12">
				 <c:set value="ADMIN" var="admin"></c:set>
			<%-- <c:if test="${SEARCHTYPE eq admin}">
				 	 <form class="form-inline" name="myForm" action="viewWOforApproval.spring"> 
			</c:if> --%>
		<%-- 	<c:if test="${SEARCHTYPE ne admin}"> --%>
			 <form class="form-inline" name="myForm" action="showWorkOrderCreationDetails.spring"> 
			<%--   </c:if> --%>
	 
			  <div class="form-group MrgRight20" id="showHideTempWO" style="display: none;">
			    <label >Temp WO Number:</label>
			  </div>
			  <div class="form-group MrgRight20" style="display: none;" id="showHideTempWO1" >
			  <input type="text" name="siteWiseWorkOrderNo" id="siteWiseWorkOrderNo" class="form-control" autocomplete="off" />
				<input type="hidden" name="tempWorkOrderIssueNo" id="tempWorkOrderIssueNo" autocomplete="off" value=""/>
				<input type="hidden" name="statusType" id="statusType" value="${statusType}">
				<input type="hidden" name="isCommonApproval" id="isCommonApproval" value="${isCommonApproval}">
			 </div>
	 
			 <div id="showHideSite"  class="form-group">
				<label>Site :</label>
				<select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control indentavailselect">	</select>
			 </div>
			  		<input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="Submit" onclick="return validate()">
		</form>
		</div>
		</div>
		</c:if>
		
  <div class="clearfix"></div>
  <c:set value="true" var="ShowTable"></c:set>
  <%-- <c:if test="${ShowDataTable ne ShowTable}"> --%>
		<div class="" style="max-width: 100%; margin-right: auto;">
			<div class="table-responsive">
			 <table id="tblnotification" class="table table-striped table-bordered table-viewmypending" cellspacing="0" style="width:100%;">
				<thead style="color: black;">
					<tr class="">
						<th>Create Date</th>
						<th>Site Name</th>
						<th>Work Order Date</th>
						<th>Temp Work Order Number</th>
						<th>Work Order Number</th>
						<th>Work Order Name</th>
						<th>Type Of Work</th>
						<th>Contractor Name</th>						
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${workOrderList }" var="workorder">
						<tr>
							<td>${workorder.workOrderCreadeDate }</td>
							<td>${workorder.siteName }</td>
							<td>${workorder.workOrderDate }</td>
							<td><a class="anchorblue" style="cursor: pointer;" href="showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo=${workorder.siteWiseWONO}&site_id=${workorder.siteId}&tempWorkOrderIssueNo=${workorder.QS_Temp_Issue_Id}&status=false&statusType=${statusType}&isCommonApproval=${isCommonApproval}" title="click here to see work order details"> ${workorder.siteWiseWONO }</a> </td>
							<td>${workorder.workOrderNo}</td>
							<td>${workorder.workOrderName}</td>
							<td>${workorder.typeOfWork}</td>							
							<td>${workorder.contractorName }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
		<%-- </c:if> --%>
</div>
</body>
   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>
		function validate() {
			 var searchTyp=sessionStorage.getItem("SEARCHTYPE");
/* 			 if(searchTyp=="ADMIN"){
				  var siteWiseWorkOrderNo=document.getElementById("siteWiseWorkOrderNo").value;
				  if(siteWiseWorkOrderNo.length==0&&"${ShowHideTempWO}"=="false"){
					  alert("Please enter temp work order number.");
					  document.getElementById("siteWiseWorkOrderNo").focus();
					  return false;
				  }
				  if(isNaN(siteWiseWorkOrderNo)){
					  alert("Please enter correct temp work order number.");
					  return false;
				  }
				 
				  var siteId=document.getElementById("dropdown_SiteId").value;
				  if((siteId==""||siteId==null)&&"${ShowHideTempWO}"!="false"){
					   alert("Please select the Site.");
					   return false;	
				}
			}else{ */
				var siteWiseWorkOrderNo=document.getElementById("siteWiseWorkOrderNo").value;
				if(siteWiseWorkOrderNo.length==0){
					alert("Please enter temp work order number.");
					document.getElementById("siteWiseWorkOrderNo").focus();
					return false;
				}
				if(isNaN(siteWiseWorkOrderNo)){
					alert("Please enter correct temp work order number.");
					return false;
				}
			//}
    	}
		
		$(document).ready(function() {
				
				var searchType="${SEARCHTYPE}";
			    var searchTyp=sessionStorage.getItem("SEARCHTYPE");
				if(searchType=="ADMIN"||searchTyp=="ADMIN"&&"${isCommonApproval}"=="true"){				    
					sessionStorage.setItem("SEARCHTYPE", "ADMIN");
				
				if("${ShowHideTempWO}"=="false"||"${errorMessage}"!=""){
					$("#showHideTempWO1").show();
					$("#showHideTempWO").show();
				}
			
				/* $.ajax({
					  url : "./loadAllSites.spring",
					  type : "GET",
					  dataType : "json",
					  success : function(resp) {
						var siteId=sessionStorage.getItem("siteIdAfterRefresh");
						var data="";	 
						data+="<option value=''></option>";
						$.each(resp,function(index,value){
							if("${strSiteId}"==value.SITE_ID){
								data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
							}else{
								data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
							}
						});  
						var searchType="${SEARCHTYPE}";
						 if(searchTyp=="ADMIN"&&"${ShowHideTempWO}"=="false"){
							$("#showHideTempWO1").show();
							$("#showHideTempWO").show();
						} 
						
						 $("#dropdown_SiteId").html(data);		
						
					  },
					  error:  function(data, status, er){
					
						  }
				 });   */
				
				}else{
					sessionStorage.setItem("SEARCHTYPE", "");
					$("#showHideSite").hide();
					$("#showHideTempWO1").show();
					$("#showHideTempWO").show();
				}
				
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		 });
   </script>
</html>
