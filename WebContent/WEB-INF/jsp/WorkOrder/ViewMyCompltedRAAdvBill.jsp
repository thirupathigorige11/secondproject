
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
.anchorblue{color:blue;}
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
.fieldError{
color: red;
}

</style>
<script>
if (typeof(Storage) !== "undefined") {
    sessionStorage.setItem("${UserId}tempRowsIncre12",1);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>

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
				<div class="overlay_ims"></div> <!-- -->
			   <div class="loader-ims" id="loaderId"> <!--   -->
				 <div class="lds-ims">
					<div></div><div></div><div></div><div></div><div></div><div></div>
				 </div>
					<div id="loadingimsMessage">Loading...</div>
				</div>
				<div class="text-center" > <h4 style="color:green;"> ${responseMessage}</h4> </div>			
				<div class="text-center" ><h4 style="color:red;"> ${responseMessage1}</h4></div>			
			  
			  
			  <div>
				<div class="container border-inwards-box">
				  <div class="col-md-12">
				     <label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
				     
					<form:form  modelAttribute="ContractorQSBillBean" class="form-horizontal"  action="${urlForFormTag}" method="GET" >
						   <div class="col-md-12">
		   						<div class="col-md-6">
									<div class="form-group">
										<label for="date" class="col-md-4 control-label">From :</label>
										<div class="col-md-6 input-group">
											<form:input path="fromDate" type="text" id="ReqDateId1" class="form-control readonly-color"
												value="${fromDate}" autocomplete="off" readonly="true"/>
											<%-- <form:errors path="toDate" cssClass="fieldError"></form:errors> --%>
											<label class="input-group-addon btn" for="ReqDateId1"> 	<span class="fa fa-calendar"></label>
										</div>
									</div>
								 </div>			
								 <div class="col-md-6">
									<div class="form-group">
										<label for="todate" class="col-md-4 control-label">To:</label>
										<div class="col-md-6 input-group">
											<form:input path="toDate"
											id="ReqDateId2" class="form-control readonly-color"  
											value="${toDate}" autocomplete="off" readonly="true"/>
											<%-- <form:errors path="toDate" cssClass="fieldError"></form:errors> --%>
											<label class="input-group-addon btn" for="ReqDateId2"> 	<span class="fa fa-calendar"></span></label>
										</div>
									</div>
								 </div>			
								<div class="col-md-6">
								    <div class="form-group">
								    	<label for="todate" class="col-md-4 control-label">Work Order Number :<%-- ${isUpdateWOPage} --%></label>
								    	<div class="col-md-6">
										 	<form:input path="workOrderNo" type="text" id="WorkOrderNo" class="form-control" value="${workOrderNumber}" autocomplete="off"/>
										    <input type="hidden" id="operType" class="form-control" name="operType" value="${operType}">
										  	<input type="hidden" name="isUpdateWOPage" value="${isUpdateWOPage}">
										    <input type="hidden" value="${SiteId}" name="site_id" id="site_id">
											<input type="hidden" class="form-control" name="workOrderIssueNo" id="workOrderIssueNo" autocomplete="off" value=""/>
									    </div>
								    </div>
								 </div>
								 <div class="col-md-6">
									 <div class="form-group">
									       <label for="todate" class="col-md-4 control-label">Contractor Name :<%-- ${isUpdateWOPage} --%></label>
									       <div class="col-md-6">
									       <form:input path="contractorId" type="hidden"  id="contractorId" value="${contractorId}"/>
										 <form:input path="contractorName" type="text" id="ContracterName" value="${ContracterName}" onkeyup="populateContractor()" autocomplete="off" class="form-control"/>
										   </div>
									 </div>
								 </div>
								 <div class="col-md-12 text-center center-block">
							  		<button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning Mrgtop10" onclick="return validate();">Submit</button>
							     </div>
						        <div>${displayErrMsg}</div>
						   </div>
					</form:form>
                 </div>
            </div>
         </div>
			  
<c:if test="${showTable eq true }">			  
	 <div class="col-md-12">
		   <div class="container Mrgtop10">
			<table id="tblnotification" class="table table-striped table-bordered " cellspacing="0">
				<thead style="color: black;">
					<tr class="tblheaderall">
						<th>Create Date</th>
						<th>Bill Date</th>
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
							<td><a class="anchorblue" href="showWOCompltedBillsDetails.spring?BillNo= ${workorder.billNo }&WorkOrderNo=${workorder.workOrderNo }&billType=${workorder.paymentType }&site_id=${workorder.siteId}&isBillUpdatePage=${isCompletedRABillUpdatePage}&status=true"> ${workorder.billNo }</a> </td>
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
	</c:if>
  </div>
</body>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
	<script>
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
				
		
		function populateContractor() {
			
			debugger;
			var contractorName=$("#ContracterName").val();
		var nameRegex=/^[a-zA-z ]+$/;
		if(contractorName.length>0){

		if(!nameRegex.test(contractorName)){
			alert("please enter Contractor name.");
			//$("#contractorName").val("");
			return false;
		}
		}else{
			return false;
		}
			 var url = "loadAndSetVendorInfoForWO.spring";
			 debugger;
		  $.ajax({
			  url : url,
			  type : "get",
			data:{
				contractorName:contractorName,
				indentReturns:'autoCompleteFunction',
				loadcontractorData:false		
			},
			  contentType : "application/json",
			  success : function(data) {
			
			  		$("#ContracterName").autocomplete({
				  		source : data,
				  		change: function (event, ui) {
			                if(!ui.item){
			               	//if list item not selected then make the text box null	
			               	 $("#ContracterName").val("");
			                }
			              },
				  		select: function (event, ui) {
			                AutoCompleteSelectHandler(event, ui);
			            }
				  	});
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
		}
		
		//code for selected text
		function AutoCompleteSelectHandler(event, ui)
		{               
		    var selectedObj = ui.item;       
		    isTextSelect="true";
		  
		  var contractorName=selectedObj.value;
		 
			 var url = "loadAndSetVendorInfoForWO.spring";
			 $.ajax({
				  url : url,
				  type : "get",
				 data:{
					 contractorName:contractorName,
					 indentReturns:'getContractorId',
					 loadcontractorData:true	 
				 },
				  contentType : "application/json",
				  success : function(data) {
					  $("#contractorName").val(contractorName);
					  if(data!=""||data!="null"){

						var contractorData=data[0].split("@@");
						var contractorId=contractorData[0];
						
						$("#contractorId").val(contractorId);
						}
				  },
				  error:  function(data, status, er){
					  alert(data+"_"+status+"_"+er);
					  }
				  });
		}
		
		
		$(document).ready(function() {
			$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
			
			$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		});
		$(function() {
			  $("#ReqDateId1").datepicker({
				dateFormat: 'dd-mm-yy',
				maxDate: new Date(),
				 onSelect: function(selected) {
		     	        $("#ReqDateId2").datepicker("option","minDate", selected)
		     	        }
			  });
			  $("#ReqDateId2").datepicker({
				dateFormat: 'dd-mm-yy',
				maxDate: new Date(),
				 onSelect: function(selected) {
		     	        $("#ReqDateId2").datepicker("option","minDate", selected)
		     	        }
			  });
			  return false;
		});
		//to ditroy loaders
		$(window).load(function() {debugger;
			 $(".overlay_ims").hide();	
			 $(".loader-ims").hide();
		});
    </script>

</html>
