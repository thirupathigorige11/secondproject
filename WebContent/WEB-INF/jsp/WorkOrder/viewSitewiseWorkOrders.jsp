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
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
table.dataTable {border-collapse:collapse !important;font-weight: bold;text-align: center;}
.st-table>thead:first-child>tr:first-child>th{font-size:15px;font-weight: bold;text-align: center;}
.success,.error {
	text-align: center;
	font-size: 14px;
	color:red;
}
.btn{padding: 4px 12px;}
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
.amountTotal{font-size:16px;color:orange;font-weight:bold;text-align:left;}
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
 		debugger;
 		var siteName=$("#dropdown_SiteId").val();
		var workOrderNumber = $("#WorkOrderNo").val();
		var ReqDateId1=$("#ReqDateId1").val();
		var ReqDateId2=$("#ReqDateId2").val();
		var ContracterName=$("#ContracterName").val();
		
		if(siteName.length==0){
			alert("Please select site name.");
			return false;
		}
		
		/* if (siteName==""&&ReqDateId1==""&&ReqDateId2==""&&workOrderNumber.length==0&&ContracterName.length==0) {
			alert("Please enter at least one field with site name!");
			return false;
		} */
		return true;
	}
</script>
</head>
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
						<li class="breadcrumb-item active">View Site Wise Work Order </li>
					</ol>
				</div>
				<form name="myForm" action="viewSitewiseWorkOrders.spring">
				 	<div class="col-md-12 border-inwards-box">
					<%--  <div class="col-md-6 col-md-offset-2">
					   
	  					    <div class="form-group" style="text-align: center; margin-bottom: 10px;margin-left: 65px;width: 100%;">
							<label  for="date" class="col-md-4 control-label">Site :</label>
								<select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control indentavailselect">	</select>
	 					    </div>
	 				  </form>
					</div> --%>
				
					
					<div class="col-md-6">
									<div class="form-group">
										<label for="date" class="col-md-4 control-label">From :</label>
										<div class="col-md-6 input-group">
											<input type="text" id="ReqDateId1" class="form-control readonly-color"
												name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
												<label class="input-group-addon btn" for="ReqDateId1"> 	<span class="fa fa-calendar"></span></label>
										</div>
									</div>
					 </div>			
					 <div class="col-md-6">
									<div class="form-group">
										<label for="todate" class="col-md-4 control-label">To :</label>
										<div class="col-md-6 input-group">
										  <input type="text"
											id="ReqDateId2" class="form-control readonly-color" name="toDate" 
											value="${toDate}" autocomplete="off" readonly="true"><!--  onkeydown="return false" -->
											<label class="input-group-addon btn" for="ReqDateId2"> 	<span class="fa fa-calendar"></span></label>
										</div>
									</div>
					</div>
							
								<div class="col-md-6">
								    <div class="form-group">
								    	<label for="todate" class="col-md-4 control-label">Work Order Number :<%-- ${isUpdateWOPage} --%></label>
								    	<div class="col-md-6">
										 	<input type="text" id="WorkOrderNo" class="form-control" name="siteWiseWorkOrderNo" value="${workOrderNumber}" autocomplete="off">
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
									       <input type="hidden" name="contractorId" id="contractorId" value="${contractorId}">
										   <input type="text" name="ContracterName" id="ContracterName" value="${ContracterName}" onkeyup="populateContractor()" autocomplete="off" class="form-control"/>
										   </div>
									 </div>
								 </div>
								  <div class="clearfix"></div>
								  <div class="clearfix"></div>
					<div class="col-md-6" style="margin-top: 10px;">
						<div class="form-group">
										<label for="date" class="col-md-4 control-label">Site <span style="color: red; font-size: 14px;">*</span> :</label>
										<div class="col-md-6 input-group">
											<select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control indentavailselect">	</select>
										</div>
									</div>
					 </div>		
								  <div class="col-md-12 text-center center-block">
					 				<input type="submit" class="btn btn-warning btn-md" name="submit" value="Submit" onclick="return validate();">
					 			  </div>
				</div>
					 </form>
				<div class="clearfix"></div>

				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
			<div class="clearfix"></div>
			<div class="col-md-12">
			<div class="table-responsive Mrgtop10">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">
							<tr>
								<th>Creation Date</th>
								<th>Site Name</th>
								<th>Work Order Date</th>
								<th>Work Order Number</th>
								<th>Status</th>
								<th>Work Order Name</th>
								<th>Work Order Amount</th>
								<th>WO Billed Amount</th>
								<th>WO Paid Amount</th>
								<th>Type Of Work</th>
								<th>Contractor Name</th>
							</tr>
						</thead>
				<tbody>
				<c:set value="0" var="totalAmount"></c:set>
				<c:set value="0" var="woBilledmount"></c:set>
				<c:set value="0" var="woPaidAmount"></c:set>
				<c:forEach items="${listOfCompletedWorkOrder}" var="element">  
				<tr>
				    <td style="color: black;">${element.workOrderDate}</td>
					<td style="color: black;">${element.siteName}</td>
					<td style="color: black;">${element.workOrderCreadeDate}</td>
					<td><a href="getMyCompletedWorkOrder.spring?WorkOrderNo=${element.workOrderNo}&workOrderIssueNo=${element.QS_Temp_Issue_Id}&site_id=${element.siteId}&operType=${operType}&isUpdateWOPage=${isUpdateWOPage}" style="text-decoration: underline;color: blue;">${element.workOrderNo}</a></td>
					<td style="color: black;">${element.workOrderStatus}</td>
					<td style="color: black;">${element.workOrderName}</td>
					<td class="totalWoAmount">${element.totalWoAmount}<c:set value="${totalAmount+element.totalWoAmount}" var="totalAmount"></c:set></td>
					<td class="totalWoAmount">${element.woBillBilledAmount}<c:set value="${woBilledmount+element.woBillBilledAmount}" var="woBilledmount"></c:set></td>
					<td class="totalWoAmount">${element.woBillPaidAmount}<c:set value="${woPaidAmount+element.woBillPaidAmount}" var="woPaidAmount"></c:set></td>
					<td style="color: black;">${element.typeOfWork}</td>
					<td style="color: black;">${element.contractorName}</td>
				</tr>
				</c:forEach>			
			 	</tbody>
			 		<tbody>
				<tr>
				<td  colspan="6" class="text-right">Sub Amount</td>
					<td colspan=""  class="total totalWoAmount subTtl " id="subTotalWOAmount"></td>
					<td class="totalWoAmount"  id="subTotalBilledAmount"></td>
					<td class="totalWoAmount"   id="subTotalPaidAmount"></td>
					<td>&emsp;</td>
					<td>&emsp;</td>
				</tr>
				<%-- <tr>
				    <td colspan="5" class="text-right">Total Amount</td>
				    <td colspan=""  class="totalWoAmount">${totalAmount}</td>
				    <td class="totalWoAmount">${woBilledmount }</td>
					<td class="totalWoAmount">${woPaidAmount }</td>
					<td>&emsp;</td>
					<td>&emsp;</td>
				</tr> --%>
				</tbody>
				</table>
				</div>
				</div>
				
			<div class="col-md-12 text-right Mrgtop10">
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>Total Amount:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOAmount" class="totalWoAmount">${totalAmount}</span></div></div>
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>WO Billed Amount:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOBilledAmount" class="totalWoAmount">${woBilledmount}</span></div></div>
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>WO Paid Amount:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOPaidAmount" class="totalWoAmount">${woPaidAmount }</span></div></div>
			</div>
           <%
				   }
           %>
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
	<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
	<script>
	
	
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
		
	$(document).ready(function() {
			$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
			var loadTime=0;
			$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
				"footerCallback": function ( row, data, start, end, display ) {
		            var api = this.api(), data;
					 debugger;
					 loadTime++;
		            // Remove the formatting to get integer data for summation
		            var intVal = function ( i ) {
		                return typeof i === 'string' ?
		                    i.replace(/[\$,]/g, '')*1 :
		                    typeof i === 'number' ?
		                        i : 0;
		            };
		            debugger;
		            // Total over all pages
		            totalWOAmount = api
		                .column(6)
		                .data()
		                .reduce( function (a, b) {
		                  debugger;
		                	return intVal(a) + intVal(b);
		            }, 0 );
		            
		            totalWOBilledAmount = api
	                .column(7)
	                .data()
	                .reduce( function (a, b) {
	                  debugger;
	                	return intVal(a) + intVal(b);
	                }, 0 );
		            
		            totalWOPaidAmount = api
	                .column(8)
	                .data()
	                .reduce( function (a, b) {
	                  debugger;
	                	return intVal(a) + intVal(b);
	                }, 0 );
		            
		            
					 debugger;
		            // Total over this page
		            subTotalWOAmountPageTotal = api
		                .column( 6, { page: 'current'} )
		                .data()
		                .reduce( function (a, b) {
		                	debugger;
		                	return intVal(a) + intVal(b);
		                }, 0 );
		            
		            subTotalBilledAmountPageTotal = api
	                .column( 7, { page: 'current'} )
	                .data()
	                .reduce( function (a, b) {
	                	debugger;
	                	return intVal(a) + intVal(b);
	                }, 0 );
		            
		            subTotalPaidAmountPageTotal = api
	                .column( 8, { page: 'current'} )
	                .data()
	                .reduce( function (a, b) {
	                	debugger;
	                	return intVal(a) + intVal(b);
	                }, 0 );
		            $("#totalWOAmount").html(totalWOAmount.toFixed(2));
		            $("#totalWOBilledAmount").html(totalWOBilledAmount.toFixed(2));
		            $("#totalWOPaidAmount").html(totalWOPaidAmount.toFixed(2));
		            
		            
		            //subTotalWOAmount,subTotalBilledAmount,subTotalPaidAmount
		            debugger;
		            // Update footer
		            if(loadTime==1){
		            	$("#subTotalWOAmount").html((subTotalWOAmountPageTotal.toFixed(2)));
			            $("#subTotalBilledAmount").html((subTotalBilledAmountPageTotal.toFixed(2)));
			            $("#subTotalPaidAmount").html((subTotalPaidAmountPageTotal.toFixed(2)));
		            }else{
			   /*       $("#subTotalWOAmount").html(inrFormat(subTotalWOAmountPageTotal.toFixed(2)));
			            $("#subTotalBilledAmount").html(inrFormat(subTotalBilledAmountPageTotal.toFixed(2)));
			            $("#subTotalPaidAmount").html(inrFormat(subTotalPaidAmountPageTotal.toFixed(2))); */
			            
			         	$("#subTotalWOAmount").html((subTotalWOAmountPageTotal.toFixed(2)));
			            $("#subTotalBilledAmount").html((subTotalBilledAmountPageTotal.toFixed(2)));
			            $("#subTotalPaidAmount").html((subTotalPaidAmountPageTotal.toFixed(2)));
		       
			            
			        	$(".totalWoAmount").each(function(){
							var currentVal=$(this).text()==""?0:parseFloat($(this).text().replace(/,/g,''));
							//$(this).text((currentVal.toFixed(2)));
							$(this).text(inrFormat(currentVal.toFixed(2)));
							
							debugger;
						});
		            }
		          //$( api.column(5).footer() ).html(''+pageTotal.toFixed(2) +' ('+ total.toFixed((2)) +' total)');
		        }	
			
			});
	});

	//to ditroy loaders
	$(window).load(function() {
		 $(".overlay_ims").hide();	
		 $(".loader-ims").hide();
		 
		 	var totalAmount=0.0;
			$(".totalWoAmount").each(function(){
				var currentVal=$(this).text()==""?0:parseFloat($(this).text().replace(/,/g,''));
				//$(this).text((currentVal.toFixed(2)));
				$(this).text(inrFormat(currentVal.toFixed(2)));
				debugger;
			});
	});
	
	$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
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
					if("${strSiteId}"=='ALL'){
					 data+="<option value='ALL' selected>ALL</option>";// this one is extra. to display ALL option. if u dont need this remove this line.
					}else{
						data+="<option value='ALL' >ALL</option>";// this one is extra. to display ALL option. if u dont need this remove this line.
					}
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

</body>
</html>
