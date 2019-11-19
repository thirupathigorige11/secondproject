<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>


<html lang="en">
<head>


<jsp:include page="../CacheClear.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>

<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.form-control {border:1px solid #000 !important;}
table.dataTable {border-collapse:collapse !important;}
.success{font-size:18px;}




  div.dataTables_scrollBody table { border-top: none;  margin-top: -2px !important; margin-bottom: 0 !important;}
  div.dataTables_wrapper div.dataTables_paginate { margin: 7px 0px;white-space: nowrap;text-align: right;}
   div.dataTables_wrapper {width: 1800px;margin: 0 auto;}
 .table>tbody+tbody {border-top: 1px solid #000 !important;}
 .border-inwards-box {text-align:left !important;}	
 .input-group-addon{border:1px solid #ccc !important;}
 .form-control{height:34px;border:1px solid #ccc !important;} 
 .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default{border:1px solid #ccc !important;}
 .custom-combobox input{height:34px;width:86% !important;border:1px solid #ccc !important;}
 .chexkbox_siteall {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
 .chexkbox_site {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;}
 .checkbox_sitelabel {margin-top: 6px;margin-right: 15px;float: left;font-size: 15px;}
 .display_flex {display: inline-flex;word-break: break-word;}
 .breadcrumb {background: #eaeaea !important;}
 #btn-search{padding:6px 12px;width:100px;}
 table.dataTable{border-collapse: collapse !important;}
 label {text-align:left !important;}
.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;}
.custom-combobox-input {width: 80%;height: 30px;border-radius: 3px;border: 1px solid #ccc;padding: 5px;}
 #dropdown {width: 88%;padding: 3px;border-color: rgb(169, 169, 169);} 




.table-responsive tr td {
    white-space: nowrap;
}


</style>


</head>
<body class="nav-md">
	<noscript>
		<h3 align="center" style="font-weight: bold;">JavaScript is
			turned off in your web browser. Turn it on and then refresh the page.</h3>
		<style>
#mainDivId {
	display: none;
}
</style>
	</noscript>
	<div class="container body" id="mainDivId">
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
				<div id="mydiv" class="hide-show-iframe">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">Purchase Tax Report</li>
						</ol>
					</div>
					<!-- loader -->
					<div class="loader-sumadhura" style="display: none;z-index:999;">
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
					<!-- loader -->
					<!-- Main Content of The Page Starts from Here -->
					<div class="col-md-12" id="hide-show-iframe">
						<div class="invoice-payment-heading">
							<h4 class="text-center">PURCHASE TAX REPORT</h4>
						</div>
						<div class="body-invoice-payment">
						    <div id = "resp-msg">
						    <center><label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label></center> 
						    </div><br>
							<form class="form-horizontal" id="paymentIntiateFormId">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>Invoice Date From  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-invoice-fromdate" name="invoiceFromDate"
													value=""  autocomplete="off" readonly="true"/>      <%-- ${invoiceFromDate} --%>
													<label class="input-group-addon btn" for="datepicker-invoice-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>Invoice Date To  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-invoice-todate" name="invoiceToDate"
													value=""  autocomplete="off" readonly="true"/>    <%-- ${invoiceToDate} --%>
													<label class="input-group-addon btn" for="datepicker-invoice-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>GRN Date From  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-grn-fromdate" name="grnFromDate"
													value=""  autocomplete="off" readonly="true"/>   <%-- ${grnFromDate} --%>
													<label class="input-group-addon btn" for="datepicker-grn-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>GRN Date To  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-grn-todate" name="grnToDate"
													value=""  autocomplete="off" readonly="true"/>  <%-- ${grnToDate} --%>
													<label class="input-group-addon btn" for="datepicker-grn-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>Vendor Name :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												  <input type="text" class="form-control control-text-height" id="VendorNameId" name="vendorName" onkeyup="getVendorId()" value="" autocomplete="off"/>  <%-- value="${vendorName}" --%>
												  <input type="hidden" readonly="true" name="vendorId" value="" id="vendorIdId" autocomplete="off"/>   <%-- value="${vendorId}" --%>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-md-12 col-sm-12 no-padding-left">
							 		<div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class="chexkbox_siteall" /><label class="checkbox_sitelabel "><strong>Show Sites</strong></label></div></div>
									<div class="col-md-12 text-left hide_select_site" style="display:none;">
										<c:forEach items="${siteDetails}" var="element">
                               			<div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" data-value1="${element.siteId}" data-value2="${element.address}"></div><div class="col-md-11"><label class="checkbox_sitelabel">${element.siteName}</label></div></div>
								   		</c:forEach>
									</div>
							  	</div>
				     			<input type="hidden" class="form-control" id="siteNames" name="siteIds"  value="" autocomplete="off"/>
								<input type="hidden" class="form-control" id="siteAddress" name="siteAddress"  value="" autocomplete="off"/>
								
								<div class="col-md-12 col-xs-12 col-sm-12 text-center center-block Mrgtop20">
									<input class="btn btn-warning" type="button" value="View" id="paymentIntiateButton1"  onclick="saveRecords('SaveClicked')">
									<!-- <input class="btn btn-warning" type="button" value="Download Excel" id="paymentIntiateButton2"  onclick="saveRecords1('SaveClicked')"> -->
							   </div>
							</form>

						</div>
						<%
							String url = "";
							if (request.getLocalPort() == 8078) {
								url = "http://129.154.74.18:8078/Sumadhura_UAT";
							} else if (request.getLocalPort() == 8079) {
								url = "http://129.154.74.18:8079/Sumadhura_CUG";
							} else if (request.getLocalPort() == 80) {
								url = "http://129.154.74.18:80/Sumadhura";
							} else if (request.getLocalPort() == 8027) {
								url = "http://localhost:8027/Sumadhura";
							}
						%>
						<br></br>
						<%
							String isShowGrid = request.getAttribute("showGrid") == null
									? ""
									: request.getAttribute("showGrid").toString();
							if (isShowGrid.equals("true")) {
								int intPendingPayments = Integer.parseInt(request.getAttribute("listTotalPaymentReqSize") == null
										? "0"
										: request.getAttribute("listTotalPaymentReqSize").toString());
						%>
						<div class="table-responsive">
						<table class="table table-new" id="tblnotification">
						<thead>
							<tr>
								<th>Project</th>
								<th>GRN. No.</th>
								<th>GRN. Date</th>
								<th>PO No.</th>
								<th>PO Date</th>
								<th>Type of purchase</th>
								<th>Name of Supplier</th>
								<th>Supplier's Address</th>
								<th>Supplier's State</th>
								<th>Supplier's GST No</th>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>HSN CODE</th>
								<th>UOM</th>
								<th>Qty</th>
								<th>Rate</th>
								<th>Invoice No.</th>
								<th>Invoice Date</th>
								<th>Basic</th>
								<th>Conveyance</th>
								<th>CGST</th>
								<th>SGST</th>
								<th>IGST</th>
								<th>Total Tax</th>
								<th>Gross Amount</th> 
								
								
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${list}" var="element">
							<tr>
<td>${element.siteName}</td>
<td>${element.grnNo}</td>
<td>${element.receivedOrIssuedDate}</td>
<td>${element.poId}</td>
<td>${element.podate}</td>
<td>${element.typeOfPurchase}</td>
<td>${element.vendorName}</td>
<td>${element.address}</td>
<td>${element.state}</td>
<td>${element.gsinNumber}</td>
<td>${element.productName}</td>
<td>${element.subProductName}</td>
<td>${element.childProductName}</td>
<td>${element.hsnCode}</td>
<td>${element.measurMntName}</td>
<td>${element.recevedQty}</td>
<td>${element.amountPerUnitAfterTaxes}</td>
<td>${element.invoiceId}</td>
<td>${element.invoiceDate}</td>
<td>${element.basicAmount}</td>
<td>${element.otherCharges}</td>
<td>${element.cgst}</td>
<td>${element.sgst}</td>
<td>${element.igst}</td>
<td>${element.totalTax}</td>
<td>${element.totalAmount}</td>

								
							</tr>
						</c:forEach>
						</tbody>
						</table>
						</div>
						<%
							}
						%>

					</div>
					<!-- Main Content of The Page end  -->
					<!-- iFrame Start -->
					<div class="loader-sumadhura" style="display: none;">
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
					<div class="cancel-iframe" id="" style="display: none;">
						<iframe src="" id="myframe"></iframe>
						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn">Cancel</button>
						</div>
					</div>
					<!-- iFrame End -->
					<!-- iFrame for already loaded images start-->
					<div class="cancel-image-iframe" id="" style="display: none;">
						<iframe id="myframe-1" src="" style="padding-bottom: 50px;">
						</iframe>


						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn-1">Cancel</button>
						</div>
					</div>
					<!-- iFrame for already loaded images end -->

				</div>
			</div>
		</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.simpleTableSort.js" type="text/javascript"></script>
		<script src="js/jquery.dataTables.min.js"></script>
        <script src="js/dataTables.bootstrap.min.js"></script> 
         <script src="js/sidebar-resp.js"></script>
		<script>
		$(function() {
			$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
			
		  	/*  $('#VendorNameId').keypress(function () {debugger;
			  $.ajax({
			  url : "./getVendorDetails.jsp",
			  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
			  type : "get",
			  data : "",
			  contentType : "application/json",
			  success : function(data) {
			  		$("#VendorNameId").autocomplete({
				  		source : data
				  	});
			  },
			  error:  function(data, status, er){
				  //alert(data+"_"+status+"_"+er);
				  }
			  });
		  	}); */
		  	/* $('#VendorNameId').on('change', function(){
		  		var value = $(this).val();
		  		$('.loader-sumadhura').show();
		  		value = value.replace("&", "$$$");
		  		//alert(value);
		  		
		  		setVendorData (value); //pass the value as paramter
			 }); */
		  });
		  
		
		  /* function setVendorData(vName) {
				
			var url = "loadAndSetVendorInfo.spring?vendName="+vName;
			  
			if(window.XMLHttpRequest) {
				request = new XMLHttpRequest();	  
			}  
			else if(window.ActiveXObject) {
				request = new ActiveXObject("Microsoft.XMLHTTP");  
			}	  
			try {
				request.onreadystatechange = setVedData;
				request.open("POST", url, true);
				request.send();  
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}

		function setVedData() {
			if(request.readyState == 4 && request.status == 200) {
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				var vendorId = resp.split("|")[0];
				var vendorAddress = resp.split("|")[1];
				var vendorGsinNo = resp.split("|")[2];
				
				$("#vendorIdId").val(vendorId);
				$("#vendorAddress").val(vendorAddress);
				$('.loader-sumadhura').hide();
			}
		} */
 		$(document)
					.ready(
							function() {
								$(".vendName").text().toLowerCase();
								$(".up_down")
										.click(
												function() {
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-up fa-chevron-down');
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-right fa-chevron-left');
												});

							});
			
			/*script for datepickers*/
			$(function() {
				$('.datepicker-paymentreq-class').each(function(){
				    $(this).datepicker({
						dateFormat : "dd-M-y",
						/* maxDate : new Date(), */
						changeMonth: true,
					      changeYear: true
					     

					});
				});
				$("#datepicker-invoice-fromdate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			     	        $("#datepicker-invoice-todate").datepicker("option","minDate", selected)
			     	        }
				});
				$("#datepicker-invoice-todate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			            	$("#datepicker-invoice-fromdate").datepicker("option","maxDate", selected)
			            	        }

				});
				
				$("#datepicker-grn-fromdate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			     	        $("#datepicker-grn-todate").datepicker("option","minDate", selected)
			     	        }
				});
				$("#datepicker-grn-todate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			            	$("#datepicker-grn-fromdate").datepicker("option","maxDate", selected)
			            	        }

				});
			});
			/*script for datepickers*/
			
			 $('.chexkbox_siteall').click(function(){
            if($(this).prop("checked") == true){
            	$(".hide_select_site").toggle(500);
            }
            else if($(this).prop("checked") == false){
            	$(".hide_select_site").hide(500);
            }
       		 });
        

			function convertDate(d) {
				var p = d.split("/");
				return +(p[2] + p[1] + p[0]);
			}

			$(function() {
				$(".ReqDateId1").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				$(".ReqDateId2").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId1").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId2").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				return false;
			});

			

			$(function() {
				$(".PaymentDateId").datepicker({
					dateFormat : 'dd-M-y',
					minDate : 0,
					changeMonth : true,
					changeYear : true
				//   showButtonPanel: true,
				// maxDate: '@maxDate',
				//   minDate: '@minDate'  
				}).attr('readonly', 'readonly');

			});

			$(document).ready(function() {
				$("#PONumberConatiner").show();
				$("#PONumber").css("background-color", "white");
				$("#PONumber").click(function() {
					$("#PONumber").css("background-color", "white");
					$("#POInvoice").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").hide();
					//$("#temporary-class").show();
					$("#PONumberConatiner").show();
					// $("#paymentIntiateButton").show();

				});
				$("#POInvoice").click(function() {
					$("#POInvoice").css("background-color", "white");
					$("#PONumber").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").show();
					//$('#withoutpricingvendorwise').show();
					$("#PONumberConatiner").hide();
					$("#container1").hide();
					$("#paymentIntiateButton").hide();
				});

			});

			$(document).ready(function() {
				$("#saveId").click(function() {
					$("#container1").show();
				});
			});

			$(document).ready(
					function() {
						debugger;
						$("#tblnotification").simpleTableSort({
							order : 'asc',
							excludeSortColumns : [ 1 ],
							dynamic : true,
							multiSortStates : false,

							onBeforeSort : function(index) {
								$('#sort-loading').show();
							},

							onAfterSort : function(index) {
								$('#sort-loading').animate({
									opacity : 0
								}, 100, function() {
									$(this).css({
										display : 'none',
										opacity : .5
									});
								});
							},

							sortMethods : {
								customMail : function(a, b) {
									a = a.split('@')[1];
									b = b.split('@')[1];

									return a > b ? 1 : -1;
								}
							}
						});

						$('#desc-link').on('click', function() {
							var pre = $('#desc-link').find('.pre');
							$('#desc').slideToggle(function() {
								pre.text(pre.text() == '+' ? '-' : '+');
							});
						});

						$('#delete-row').on(
								'click',
								function() {
									$('#paymentTable').find('tbody').children()
											.first().eq(0).remove();
								});
					}); 
			function saveRecords(saveClicked, rowId) {
				var flag = validate();
				if (flag == false) {
					return false;
				}
				$(".loader-sumadhura").show();
				document.getElementById("resp-msg").style.display = "none";			   
				document.getElementById("paymentIntiateFormId").action = "viewPurchaseTaxReport.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
			}

			/* function saveRecords1(saveClicked, rowId) {
				var flag = validate();
				if (flag == false) {
					return false;
				}
                $(".loader-sumadhura").show();
				document.getElementById("resp-msg").style.display = "none";
				   
				var canISubmit = window.confirm("Do you want to download?");

				if (canISubmit == false) {
					return false;
				}
				$(".loader-sumadhura").hide(); 
				document.getElementById("paymentIntiateFormId").action = "downloadPurchaseTaxReport.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
				
			} */
			/* **************** Valdiate from date and Todate************ */

			function validateRowData(rowId) {
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowId).val();
				var Amntreqdate = $("#PaymentreqDateId" + rowId).val();

				/* if(AmnttoBeRelsed == "" || AmnttoBeRelsed == null || AmnttoBeRelsed == ''|| Amntreqdate == "" || Amntreqdate == null || Amntreqdate == '') {
				 alert("Please enter Amount to be released and date");
				 document.getElementById("AmnttoBeRelsed").focus(); */

			}
			//**********Method for validation for PO Number*************
			
			Array.prototype.allValuesSame = function() {
				for(var i = 1; i < this.length; i++)
    			{
        			if(this[i] !== this[0])
            		return false;
    			}
				return true;
			}
			
			function validate() {debugger;

				document.getElementById("resp-msg").style.display = "none";
			   

				var InvoiceFrom=$("#datepicker-invoice-fromdate").val();
			    var InvoiceTo=$("#datepicker-invoice-todate").val();
			    var GrnFrom=$("#datepicker-grn-fromdate").val();
			    var GrnTo=$("#datepicker-grn-todate").val();
			    var VendorName=$("#VendorNameId").val();
			    var VendorId=$("#vendorIdId").val();
			    var SiteData=[];
			    var SiteAddressData=[];
		        $(".chexkbox_site").each(function(){
		        	//debugger;
		        	if($(this).prop("checked") == true){
		        	var currentSite=$(this).data("value1");
		        	var currentSiteAddress=$(this).data("value2");
		        	SiteData.push(currentSite);
		        	SiteAddressData.push(currentSiteAddress);
		        	}
		        });
		        
				if(SiteData.length == 0){
		        	alert("Select atleast one site.");
					return false;
		        }
		        
		        if(SiteAddressData.allValuesSame() == false){
		        	alert("Select sites of same address (HYDERABAD or BANGLORE).");
					return false;
		        }
		        
			    
		        console.log("SiteData: "+SiteData);
		        console.log("SiteData: "+SiteData.length);
		        $('#siteNames').val(SiteData);
		        $('#siteAddress').val(SiteAddressData[0]);
			    
				
			    //trim method 
			    var InvoiceFromdate=InvoiceFrom.trim();
			    var InvoiceTodate=InvoiceTo.trim();
			    var GrnFromdate=GrnFrom.trim();
			    var GrnTodate=GrnTo.trim();
			    var VendorNameId=VendorName.trim();
			    var VendorIdId=VendorId.trim();
			    
			    if ((InvoiceFromdate || InvoiceTodate) && (GrnFromdate || GrnTodate)) {
		        	alert("Select Invoice date (or) Grn date ");
					return false;
		        }
			    else if (!InvoiceFromdate && !InvoiceTodate && !GrnFromdate && !GrnTodate && (!VendorNameId || !VendorIdId) ) {
					alert("Atleast one filed is required.");
					return false;
				}
				else{
					return true;
				}

}
			/* ********** Method for sorting table************ */

			//**********Method for validation for PO Invoice*************  
			function validate2() {

				var InvfrmDate = $("#invoiceReqDateId1").val();
				var InvtoDate = $("#invoiceReqDateId2").val();

				if (InvfrmDate == "" || InvfrmDate == null || InvfrmDate == ''
						|| InvtoDate == "" || InvtoDate == null
						|| InvtoDate == '') {
					alert("Please enter from date and to date");

				}

			}

			function getVendorId(){debugger;
			  $.ajax({
			  url : "./getVendorDetails.jsp",
			  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
			  type : "get",
			  data : "",
			  contentType : "application/json",
			  success : function(data) {
			  		$("#VendorNameId").autocomplete({
				  		source : data,
	    		  		select: function( event, ui ) { 
	    		  			$('.loader-sumadhura').show();
	    	    			var value = ui.item.value;
							//alert("value: "+value);
	    			  		value = value.replace("&", "$$$");
	    			  		setVendorData (value);
	    	    			
	    		  		 }
				  	});
			  },
			  error:  function(data, status, er){
				  //alert(data+"_"+status+"_"+er);
				  }
			  });
		}
		  
			$('#VendorNameId').on('change', function(){
		  		var value = $(this).val();
		  		//$('.loader-sumadhura').show();
		  		value = value.replace("&", "$$$");
		  		//alert(value);
		  		
		  		setVendorData (value); //pass the value as paramter
			 });
			
		  function setVendorData(vName) {
				
			var url = "loadAndSetVendorInfo.spring?vendName="+vName;
			  
			if(window.XMLHttpRequest) {
				request = new XMLHttpRequest();	  
			}  
			else if(window.ActiveXObject) {
				request = new ActiveXObject("Microsoft.XMLHTTP");  
			}	  
			try {
				request.onreadystatechange = setVedData;
				request.open("POST", url, true);
				request.send();  
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}

		function setVedData() {
			if(request.readyState == 4 && request.status == 200) {
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				var vendorId = resp.split("|")[0];
				var vendorAddress = resp.split("|")[1];
				var vendorGsinNo = resp.split("|")[2];
				
				$("#vendorIdId").val(vendorId);
				$("#vendorAddress").val(vendorAddress);
				$('.loader-sumadhura').hide();
			}
		} 
			/* ****** Method for validating the amount requested amnt to PO Amont******** */

			function validateAmnt(rowNum) {
				debugger;
				var OriganlaPoamnt = $("#InvAmnt" + rowNum).text();
				var OringinalPOAnt = parseInt(OriganlaPoamnt);
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowNum).val();
				var compareVal = parseInt(AmnttoBeRelsed);

				if (compareVal > OringinalPOAnt) {
					alert('Please enter lesser amont');
					$("#AmountToBeReleased" + rowNum).val('');
					document.getElementById("AmnttoBeRelsed").focus();
				}
			};

			/* ****** Method for validating the amount requested amnt to POINvoice******** */

			function validateAmnt(rowNum) {
				debugger;
				var OriganlaPoamnt = $("#POAmnt" + rowNum).text();
				var OringinalPOAnt = parseInt(OriganlaPoamnt);
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowNum).val();
				var compareVal = parseInt(AmnttoBeRelsed);

				if (compareVal > OringinalPOAnt) {
					alert('Please enter lesser amont');
					$("#AmountToBeReleased" + rowNum).val('');
					document.getElementById("AmnttoBeRelsed").focus();
				}
			};

			/* =========================== */
			//To select country name
			function selectCountry(val) {
				$("#search-box").val(val);
				$("#suggesstion-box").hide();
			}
			/* ********* Method for Filer the table**************** */

			function tableSearch1() {
				debugger;

				var value = $("#myInput1").val();
				$('table tbody tr').hide();
				var sum = 0;
				var tquantity = 0;
				var PaidAmnt = 0;
				var POAount = 0;
				$('table tbody tr td:contains("' + value + '")').each(
						function(index, item) {
							$(this).closest('tr').show();
							var reqamount = $(this).closest('tr').find(
									'.poAmnt').text();
							sum = sum + parseFloat(reqamount);
							$("#subtotalpoAmount1").text(sum);
							$(".info").show();

							var reqamount1 = $(this).closest('tr').find(
									'.Tillamnt').text();
							tquantity = tquantity + parseFloat(reqamount1);
							$("#subtotalpoAmount2").text(tquantity);
							$(".info").show();

							var reqamount2 = $(this).closest('tr').find(
									'.paidnmnt').text();
							PaidAmnt = PaidAmnt + parseFloat(reqamount2);
							$("#subtotalpoAmount3").text(PaidAmnt);
							$(".info").show();
						});

			};
		</script>
		<script>
			/* url sending to page start and hide and show iframe */
			function SetPage(url) {
				var z = document.getElementById("myframe").setAttribute("src",
						url);
				$('.loader-sumadhura').show();
			}
			$('#myframe').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".anchor-invoice").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-iframe").show();
			});
			$("#cancel-btn").click(
					function() {
						$("#hide-show-iframe").show();
						$(".cancel-iframe").hide();
						var z = document.getElementById("myframe")
								.setAttribute("src", '');
					});
			/* url sending to page end and hide and show iframe */
			/* full image sending to page start and hide and show iframe */
			function SetPageImage(url) {
				var f = document.getElementById("myframe-1").setAttribute(
						"src", url);
				$('.loader-sumadhura').show();
			}
			$('#myframe-1').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".ponumber-anchor").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-image-iframe").show();

			});
			$("#cancel-btn-1").click(
					function() {
						$("#hide-show-iframe").show();
						$(".cancel-image-iframe").hide();
						var f = document.getElementById("myframe-1")
								.setAttribute("src", '');
					});
			/* full image sending to page end and hide and show iframe */
			
			/* for sidemodule highlight */
			var referrer="beforeDownloadExcel.spring";
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
