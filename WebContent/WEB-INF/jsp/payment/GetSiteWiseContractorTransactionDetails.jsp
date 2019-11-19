<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<jsp:include page="../CacheClear.jsp" />  
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%-- <jsp:include page="CacheClear.jsp" /> --%>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>

.table thead tr th{
border:1px solid #000;
background-color:#ccc;
color:#000;
}
.table tbody tr td{
border:1px solid #000;
}
.form-control{
border:1px solid #000 !important;
}
table.dataTable {border-collapse:collapse !important;}
</style>
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
						<li class="breadcrumb-item"><a href="#">Payment</a></li>
						<li class="breadcrumb-item active">View Transaction Details</li>
					</ol>
				</div>
<div class="col-md-12">
<div class="invoice-payment-heading">
 <h4 class="text-center">TRANSACTION DETAILS</h4>
</div>
</div>
<div class="col-md-12">
<div class="container container-viewmypayment-box-1">
<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
				
	<form class="form-horizontal"  action="viewSiteWiseContractorTransactionDetails.spring" style="margin-top: 30px;"  >
	
	
   <div class="col-md-12">
	     <div class="col-md-6">
		     <div class="form-group">
			    <label for="date" class="col-md-6 col-xs-12"><strong>Payment Done From <span class="color-required">*</span> :</strong></label>
			  <div class="col-md-6 col-xs-12"> <input type="text" id="ReqDateId1" class="form-control" name="fromDate" value="${fromDate}" autocomplete="off"></div>
		  	</div>
	  	</div>
	  	 <div class="col-md-6">
		  	 <div class="form-group">
			    <label for="todate" class="col-md-6 col-xs-12">Payment Done To <span class="color-required">*</span> :</label>
			    <div class="col-md-6 col-xs-12"><input type="text" id="ReqDateId2" class="form-control" name="toDate" value="${toDate}" autocomplete="off"></div>
		  	</div>
	  	</div>

  	<div class="col-md-6">
	  	<div class="form-group">
		    <label for="invoiceNumber" class="col-md-6 col-xs-12">Work Order Number :</label>
		   <div class="col-md-6 col-xs-12">
		    <input type="text" id="workOrderNo" class="form-control"  name="workOrderNo" value="${workOrderNo}" autocomplete="off">
		    </div>
	  	</div>
  	</div>
  	<%-- <div class="col-md-6">
  	
  	 <div class="form-group">
	    <label for="poNumber" class="col-md-6 col-xs-12">PO Number :</label>
	   <div class="col-md-6 col-xs-12"><input type="text" id="poNumber" class="form-control"  name="poNumber" value="${poNumber}" autocomplete="off"></div>
  	</div>
  	</div> --%>
  	<div class="col-md-6">
  	   <div class="form-group">
	      <label for="" class="col-md-6">Contractor Name :</label>
		   <div class="col-md-6"> 
			    <input type="text" class="form-control control-text-height" id="ContractorName" name="contractorName" value="${contractorName}" autocomplete="off"/>
				<input type="hidden" class="form-control control-text-height" id="ContractorId" name="contractorId" value="${contractorId}" autocomplete="off"/>
			</div>
		</div>
	</div>
  	  	 <div class="col-md-6">
  	 <div class="form-group">
  	  <label class="col-md-6 col-xs-12">Site :</label> <!-- removed class commlbl  -->
	  <div class="col-md-6 col-xs-12">
	  <%
											List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
																				String strSiteId = "";
																				String strSiteName = "";
										%> <select id="dropdown_SiteId" name="siteIdAndName"
										class="custom-combobox form-control"><!-- /* indentavailselect*/ removed -->
											<option value="${site_id}@@${site_name}">${site_name}</option>
											<option value="">--None--</option>
											<%
												for (Map siteList : totalSiteList) {
																						strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
																						strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
											%>

											<option value="<%=strSiteId%>@@<%=strSiteName%>"><%=strSiteName%></option>
											<%
												}
											%>

									</select>
	  </div>
									
  	 									
  	 </div>
  	</div>	
  		
  							
											
									

  	</div>
    <div class="form-group">        
      <div class="col-md-12 col-xs-12 col-sm-12 text-center center-block">
        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning media-style"  onclick="return validate();">Submit</button>
        <div style="display: none;">${displayErrMsg}</div>
      </div>
    </div>
  </form>
				
				
				
				
				
				
				
</div>
</div>
<!-- <div class="loader"></div> -->	
<div class="loader-purchase" style="display: none;">
						<div class="lds-facebook">
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>	
					<div class="clearfix"></div>
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="col-md-12">
				<div class="table-responsive Mrgtop10"> <!-- container1 -->
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>
							<tr class="">
								<th>Payment Done Date</th>
								<th>Payment Id</th>		
			    				<th>Contractor</th>
			    				<th>Bill Number</th>
			    				<th>Amount Paid</th>
			    				<th>UTR Cheque No</th>
			    				<th>Site</th>
			    				<th>WorkOrder No</th>
			    				<th>WorkOrder Date</th>
			    				<th>Remarks</th>
				           </tr>
						</thead>
						<tbody>
							<c:forEach items="${listOfPayments}" var="element">  
							<tr>
								<td>${element.strPaymentDate}</td>
							    <td>${element.intCntPaymentId}</td>
								<td>${element.strContractorName}</td>
								<td class="myborder  border-class">
																	<c:if test="${element.paymentType=='RA'}">
																	<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo= ${element.strBillId}&WorkOrderNo=${element.workOrderNo }&billType=${element.paymentType }&site_id=${element.strSiteId}&status=true" target="_blank">${element.strBillId}</a>
																	</c:if>
																	<c:if test="${element.paymentType=='ADV'}">
																	<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo= ${element.strBillId}&WorkOrderNo=${element.workOrderNo }&billType=${element.paymentType }&site_id=${element.strSiteId}&isBillUpdatePage=false&status=true" target="_blank">${element.strBillId}</a>
																	</c:if>
																	<c:if test="${element.paymentType=='SEC'}">${element.strBillId}</c:if>
																	<c:if test="${element.paymentType=='NMR'}">
																	<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo= ${element.strBillId}&WorkOrderNo=${element.workOrderNo }&billType=${element.paymentType }&site_id=${element.strSiteId}&isBillUpdatePage=false&status=true" target="_blank">${element.strBillId}</a>
																	</c:if>
								</td>
								<td class="valor2">${element.doublePaidAmount}</td>
								<td>${element.utrChequeNo}</td>
								<td>${element.strSiteName}</td>
								<td>${element.workOrderNo}</td>
								<td>${element.strWorkOrderDate}</td>
								<td>
								 <input id="remarks" style="border: none; width:100px;word-wrap: break-word;" readonly="true" data-toggle="tooltip"
										title="${element.strRemarksForTitle}" value="${element.strRemarksForView}" />
								</td>
								
							</tr>
							</c:forEach>
				     </tbody>
					<tr class="info">
			            <td colspan="4" class="text-right subTtl" style="display:none;">SUB TOTAL:</td>
			            <td class="total text-right subTtl" style="display:none;"></td>
			            
			        </tr>
					</table>
				</div>
           <%
				   }
           %>
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
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script src="js/indentReceive.js" type="text/javascript"></script>
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			maxDate: new Date(),
			changeMonth: true,
		      changeYear: true
		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			  changeMonth: true,
		      changeYear: true
		  });
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
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		$(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		});
		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
		/* *************** calculations for filter data***********************	 */
		

	/* *************** calculations for filter data***********************	 */
		

	
		$(document).ready(function () {
			$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 $(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(tAmount.toFixed(2));
			});
		   
		});
		
		/* *************** Paginataion Total********* */
		
				$(document).ready(function () {
			$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 $(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("click", " .pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(tAmount.toFixed(2));
			});
		   
		});
	
				/* *************** Paginataion Quantity Total********* */
				
						$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 $(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("click", ".pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		   
		});
						/* *************** Show Total********* */
						
						$(document).ready(function () {
					$(".total").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tAmount = 0;
					 $(".total").text(0);
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
				    });
				    $(".subTtl").hide();	
				 	$(document).on("change", " .dataTables_length",function(){
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tAmount = 0;
						 $(".total").text(0);
					    jQuery.each(val,function(index,item){
					    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					    });
					 
						$(".total").text(tAmount.toFixed(2));
					});
				   
				});

						/* *************** show Sub Total********* */
						
								$(document).ready(function () {debugger;
					$(".quantity").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tquantity = 0;
					 $(".quantity").text(0);
				    jQuery.each(val,function(index,item){
				    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
				    });
				    $(".subTtl").hide();	
				 	$(document).on("change", ".dataTables_length",function(){debugger;
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tquantity = 0;
						 $(".quantity").text(0);
					    jQuery.each(val,function(index,item){
					    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
					    });
					 
						$(".quantity").text(tquantity.toFixed(2));
					});
				   
				});
		
								/* *************** Method for load the vendors*************	 */ 
								  /* $(function() {
								  	$('#VendorNameId').keypress(function () {
									  $.ajax({
									  url : "./getVendorDetails.jsp",
									  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
									  type : "get",
									  data : "",
									  contentType : "application/json",
									  success : function(data) {
									  		$("#VendorNameId").autocomplete({
										  		source : data
										          
								         	//$('#backend').val(ui.item.backend);
										  	});
									  }

									  });
								  	});
								  	$('#VendorNameId').on('change', function(){
								  		var value = $(this).val();
								  		
								  		value = value.replace("&", "$$$");
								  		//alert(value);
								  		
								  		//setVendorData (value); //pass the value as paramter
									 });
								  }); */
								/*===========================*/
								
  							  
		   
		
		$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 $(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		   
		});
		
	</script>
<!-- script for loader start-->

<script type="text/javascript">

function validate() {
	
	var PaymentInitiatedFrom=$("#ReqDateId1").val();
    var PaymentInitiatedTo =$("#ReqDateId2").val();
    var WorkOrderNo=$("#workOrderNo").val();
    var ContractorName=$("#ContractorName").val();
    var dropdown_SiteId=$("#dropdown_SiteId").val();
    //alert(dropdown_SiteId);
    if(dropdown_SiteId=='@@'){
    	dropdown_SiteId="";
    }
    /*alert(dropdown_SiteId+","+PaymentInitiatedFrom+","+PaymentInitiatedTo+","+WorkOrderNo+","+ContractorName);*/
    
    
	
    //trim method 
    var PaymentInitiatedFromdate=PaymentInitiatedFrom.trim();
    var PaymentInitiatedTodate=PaymentInitiatedTo.trim();
    var WorkOrderNoId=WorkOrderNo.trim();
    var ContractorNameId=ContractorName.trim();
    var dropdown_SiteId=dropdown_SiteId.trim();
    
	if (!PaymentInitiatedFromdate && !PaymentInitiatedTodate && !WorkOrderNoId && !ContractorNameId && !dropdown_SiteId) {
		alert("Atleast one filed is required.");
		return false;
	}
	else{
		return true;
	}
}

$("#ContractorName").keyup(function () {
 	debugger;
	var contractorName=$("#ContractorName").val();

	 var url = "loadAndSetContractorInfo.spring";
    		$("#ContractorName").autocomplete({
    			source : function(request, response) {
    				$.ajax({
    					  url :url,
    					  type : "GET",
    					  data:{
    						  contractorName:contractorName
    					  },
    					//dataType : "json",
    					contentType : "application/json",
    					success : function(data) {
    						debugger;
    						response(data.split("@@"));
    					}
    				});
    			},change: function (event, ui) {
		                if(!ui.item){
		                    // The item selected from the menu, if any. Otherwise the property is null
		                    //so clear the item for force selection
		                    $("#ContractorName").val("");
		                }
		            },autoFocus:true,
			  		 select: function (event, ui) {
				            	AutoCompleteSelectHandler(event, ui);
				    }
		            
    		});
	});

//code for selected text
function AutoCompleteSelectHandler(event, ui)
{               
	debugger;
 var selectedObj = ui.item;       
 isTextSelect="true"; 
 $("#workOrderNo").val("");
 $("#totalAmtToPay").val("");
var contractorName=selectedObj.value;

	 var url = "loadAndSetVendorInfoForWO.spring";
	 $.ajax({
		  url : url,
		  type : "get",
		 data:{
			 contractorName:contractorName,
			 loadcontractorData:true	 
		 },
		  contentType : "application/json",
		  success : function(data) {
			debugger;
			  $("#contractorName").val(contractorName);
			  if(data!=""||data!="null"){

				var contractorData=data[0].split("@@");
				var contractorId=contractorData[0];
			
				$("#ContractorId").val(contractorId);
				$("#MobileNo").val(contractorData[3]);
				$("#PanCardNo").val(contractorData[4]);
				$("#AccountNo").val(contractorData[5]);
				$("#ifscCode").val(contractorData[6]);
				$("#printContractorName").text(contractorName);
				loadWorkOrderNo();
			  }
			
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
}

</script>


<!-- script for loader end -->
</body>
</html>
