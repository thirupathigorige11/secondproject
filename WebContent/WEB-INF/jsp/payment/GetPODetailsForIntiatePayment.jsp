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
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<style>
 #printPageButton {margin-left: 437px !important;} 
.amount-payments-sum, .amount-payments-sum-right{width:33.3% !important;}
.selectAllchk .selectAllchkinput {height: 22px;width: 22px;vertical-align: bottom;}
.selectAllchk span {font-size: 16px;font-weight: bold;}
.form-control {border:1px solid #000;}
.button-style {
	margin-top: 36px;
	margin-left: 100px;
	width: 156px;
}

.tbl-data-align td {
	vertical-align: middle !important;
}

@media only screen and (min-width:1200px) and (max-width:1440px) {
	.custom-modal-lg {
		width: 95%;
		height: auto;
		overflow: hidden;
	}
}
@media only screen and (min-width:320px) and (max-width:767px){
.amount-payments-sum, .amount-payments-sum-right{width:100% !important}
}

.img-adjust {
	margin-bottom: 50px;
}

.modal-footer {
	text-align: center !important;
	padding: 7px;
}

.cust-modal-body {
	height: 540px;
	overflow-y: auto;
}

.modal {
	top: -30px;
}

.modal-header {
	background-color: #ccc;
	border-radius: 5px 5px 0px 0px;
	padding: 7px;
}
</style>
<!-- jQuery -->
<script src="js/jquery.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<!-- Bootstrap -->

<script src="js/stacktable.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<!-- <script src="js/payment.js" type="text/javascript"></script> -->

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
							<li class="breadcrumb-item active">Initiate Payment For PO</li>
						</ol>
					</div>
					<div class="loader-sumadhura" style="display: none;z-index:99999;">
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
					<!-- Main Content of The Page Starts from Here -->
					<div class="col-md-12" id="hide-show-iframe">
						<div class="invoice-payment-heading">
							<h4 class="text-center">PURCHASE ORDER</h4>
						</div>
						
						<div class="body-invoice-payment">
							<form class="form-horizontal"  action="IntiatePaymentFromPO.spring">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-xs-12 Mrgtop10">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>PO From :</strong></label>
											<div class="col-md-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-paymentreq-fromdate" name="fromDate"
													value="${fromDate}" autocomplete="off"  readonly="true"/>
													 <label class="input-group-addon btn" for="datepicker-paymentreq-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12 Mrgtop10">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>PO To :</strong></label>
											<div class="col-md-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-paymentreq-todate" name="toDate"
													value="${toDate}" autocomplete="off" readonly="true"/>
													 <label class="input-group-addon btn" for="datepicker-paymentreq-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>PO
													Number :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height"
													name="PONumber" id="PONumber" value="${poNumber}" />
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>Vendor
													Name :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height"
													id="VendorNameId" name="vendorName" value="${vendorName}" />
											</div>
										</div>
									</div>
									<c:if test="${showDropdownSite}">
										<div class="col-md-6 col-xs-12">
											<div class="form-group">
												<label class="col-md-5 col-xs-12">Site/Department :</label>
												<div class="col-md-6 col-xs-12">
													<%
														List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request
																	.getAttribute("allSitesList");
															String strSiteId = "";
															String strSiteName = "";
													%>
													<select id="dropdown_SiteId" name="siteIdAndName"
														class="custom-combobox form-control"> <!-- indentavailselect -->
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
									</c:if>
									<div class="col-md-12 text-center selectAllchk">
									  <input type="checkbox" class="selectAllchkinput" id="selectAll" name="selectAll" value="" /><span>&nbsp;Select All</span>
									  <input type="hidden" id="hiddenSelectAll" name="hiddenSelectAll" value="" />
									
									</div>
									<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12 text-center center-block Mrgtop10">
										<button class="btn btn-warning submit-form-po" onclick="return validate()">Submit</button>
									</div>

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

						<%
							String isShowGrid = request.getAttribute("showGridForPO") == null
									? ""
									: request.getAttribute("showGridForPO").toString();
							if (isShowGrid.equals("true")) {
								int intTotalPOs = Integer.parseInt(request.getAttribute("listTotalPOsSize") == null
										? ""
										: request.getAttribute("listTotalPOsSize").toString());
						%>
						<div class="clearfix"></div>
						<div class="col-md-12 no-padding-left no-padding-right">
							<div class="col-md-4 Mrg-total no-padding-left amount-payments-sum">
								<div class="col-md-12 no-padding-left text-center">
									<h4><strong>Total&nbsp;PO&nbsp;Amount</strong></h4>
								</div>
								<div class="col-md-12 text-center"><strong>${TotalPOAmount}</strong></div>
							</div>
							<div class="col-md-4  Mrg-total amount-payments-sum">
								<div class="col-md-12 text-center">
									<h4><strong>Till Req Amount</strong></h4>
								</div>
								<div class="col-md-12 text-center"><strong>${TotalTillReqAmount}</strong></div>
							</div>
							<div class="col-md-4 Mrg-total no-padding-right amount-payments-sum-right">
								<div class="col-md-12 text-center">
									<h4><strong>Paid Amount</strong></h4>
								</div>
								<div class="col-md-12 text-center"><strong>${TotalPaidAmount}</strong></div>
							</div>
						</div>
						<div class="clearfix"></div>
						<form class="form-horizontal" id="paymentIntiateFormId">
							<c:forEach items="${listTotalPOs}" var="element">
								<!-- form items data start -->
								<div class="form-invoice-data" id="slide-form-fileds">
									<%-- <div class="" style="position:relative;"><input type="checkbox" class="check-image" style="position:absolute;z-index:9;"
															name="checkboxname${element.intSerialNo}" value="checked"></div> --%>
									<%-- <label class="check-container"> <input type="checkbox"
										name="checkboxname${element.intSerialNo}" value="checked">
										<span class="chec
										kmark"></span>
									</label> --%>
									 <label class="" style="z-index:1;"> 
									 <input type="checkbox" style="height:20px;width:20px;margin-top:0px;" id="chbox${element.intSerialNo}"	name="checkboxname${element.intSerialNo}" value="checked">
										
									</label>
									<div class="col-md-12">
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">S.No :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<strong>${element.intSerialNo}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">PO Date :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<strong>${element.strPODate}</strong>
													</p>
												</div>
											</div>
										</div>

										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">PO Number :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<a
															style="text-decoration: underline; color: blue; word-break: break-word;"
															href="#"
															onclick="SetPageImage('<%=url%>/getPoDetailsList.spring?poNumber=${element.strPONo}&siteId=${element.strSiteId}&imageClick=true')"
															class="ponumber-anchor">${element.strPONo}</a>
													</p>
												</div>
											</div>
										</div>
										
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Vendor Name :</label>
												<div class="col-lg-6 col-md-5">
													<p style="word-break: break-word;">
														<strong>${element.strVendorName}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">PO Amount :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<strong>${element.poAmount_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>

										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Till Req Amount :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<strong>${element.paymentRequestedUpto_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Paid Amount :</label>
												<div class="col-lg-6 col-md-5">
													<p>
														<strong>${element.paymentDoneUpto_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Payment Req Date :</label>
												<div class="col-lg-6 col-md-5 input-group">
													 <input type="text" class="form-control control-text-height datepicker-paymentreq-class readonly-color" name="PaymentreqDateId${element.intSerialNo}" id="PaymentreqDateId${element.intSerialNo}" autocomplete="off" readonly="true"/>
												     <label class="input-group-addon btn" for="PaymentreqDateId${element.intSerialNo}"> <span class="fa fa-calendar"></span></label>
												</div>
											</div>
										</div>

										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Amount to be released :</label>
												<div class="col-lg-6 col-md-5">
													<input type="text" class="form-control control-text-height amountTobeRelease" name="AmountToBeReleased${element.intSerialNo}" id="AmountToBeReleased${element.intSerialNo}"  onkeypress="return isNumberCheck(this, event)" onblur="validateAmountToBeReleased(${element.intSerialNo})" autocomplete="off"/>
												</div>
											</div>
										</div>
										
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-lg-6 col-md-7">Remarks :</label>
												<div class="col-lg-6 col-md-5">
													<textarea class="form-control textarea-control" name="remarks${element.intSerialNo}"></textarea>
												</div>
											</div>
										</div>
									</div>

								</div>
								<!-- Hidden Fields Start -->
								<input type="hidden" name="paymentType${element.intSerialNo}"
									value="ADVANCE">
								<input type="hidden" name="invoiceDate${element.intSerialNo}"
									value="-">
								<input type="hidden" name="invoiceNo${element.intSerialNo}"
									value="${element.strInvoiceNo}">
								<input type="hidden" name="vendorId${element.intSerialNo}"
									value="${element.strVendorId}">
								<input type="hidden" name="vendorName${element.intSerialNo}"
									value="${element.strVendorName}">
								<input type="hidden" name="paymentSeqId${element.intSerialNo}"
									value="${element.intPaymentId}">
								<input type="hidden" name="invoiceAmount${element.intSerialNo}"
									value="${element.doubleInvoiceAmount}">
								<input type="hidden" name="siteId${element.intSerialNo}"
									value="${element.strSiteId}">
								<input type="hidden"
									name="paymentDoneUpto${element.intSerialNo}" id="paymentDoneUpto${element.intSerialNo}" 
									value="${element.doublePaymentDoneUpto}">
								<input type="hidden" name="dcNo${element.intSerialNo}"
									value="${element.strDCNo}">
								<input type="hidden" name="paymentReqUpto${element.intSerialNo}" id="paymentReqUpto${element.intSerialNo}" 
									value="${element.doublePaymentRequestedUpto}">
								<input type="hidden" name="poEntryId${element.intSerialNo}"
									value="${element.strPoEntryId}">
								<input type="hidden" name="poNo${element.intSerialNo}"
									value="${element.strPONo}">
								<input type="hidden" name="poAmount${element.intSerialNo}" id="poAmount${element.intSerialNo}" 
									value="${element.doublePOTotalAmount}">
								<input type="hidden" name="poDate${element.intSerialNo}"
									value="${element.strPODate}">
								<input type="hidden"
									name="invoiceReceivedDate${element.intSerialNo}" value="-">
								<input type="hidden" name="dcDate${element.intSerialNo}"
									value="-">
								<input type="hidden"
									name="AdjustAmountFromAdvance${element.intSerialNo}"
									value="${element.doubleAdjustAmountFromAdvance}" />
								<input type="hidden" name = "remainingPOAdvance${element.intSerialNo}"  value = "${element.doublePOAdvancePayment}">
								
								<!-- Hidden Fields End -->

								<!-- form items data end -->
							</c:forEach>
							<input type="hidden" name="noOfRecords" value="<%=intTotalPOs%>" id="noOfRecords">
							<div class="col-md-12 text-center center-block">
								<input class="btn btn-warning" type="button" value="Submit" id="paymentIntiateButton" style="width: 141px; margin: 10px;" onclick="saveRecords()">
							</div>
						</form>
						<%
							}
							else{
						%>
						<c:if test="${not firstRequest}">
							<div class="col-md-offset-3 col-md-6 text-center"><font color="red" size="5" >Data not available.</font></div>
						</c:if>
						<%
							}
						%>
					</div>
					<!-- Main Content of The Page end  -->
					<!-- iFrame Start -->
					
					<div class="cancel-iframe" id="" style="display: none;">
						<iframe src="" id="myframe"></iframe>
						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn">Close</button>
						</div>
					</div>
					<!-- iFrame End -->
					<!-- iFrame for already loaded images start-->
					<div class="cancel-image-iframe" id="" style="display: none;">
						<iframe id="myframe-1" src="" style="padding-bottom: 50px;">
						</iframe>


						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn-1">Close</button>
						</div>
					</div>
					<!-- iFrame for already loaded images end -->

				</div>
			</div>
		</div>
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.simpleTableSort.js" type="text/javascript"></script>
		<script>
			$(function() {
				$('#VendorNameId').keypress(function() {
					debugger;
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
						error : function(data, status, er) {
							//alert(data+"_"+status+"_"+er);
						}
					});
				});
				$('#VendorNameId').on('change', function() {
					var value = $(this).val();

					value = value.replace("&", "$$$");
					//alert(value);

					setVendorData(value); //pass the value as paramter
				});
			});

			function setVendorData(vName) {

				var url = "loadAndSetVendorInfo.spring?vendName=" + vName;

				if (window.XMLHttpRequest) {
					request = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				try {
					request.onreadystatechange = setVedData;
					request.open("POST", url, true);
					request.send();
				} catch (e) {
					alert("Unable to connect to server!");
				}
			}

			function setVedData() {
				if (request.readyState == 4 && request.status == 200) {
					var resp = request.responseText;
					resp = resp.trim();
					//alert(resp);
					var vendorId = resp.split("|")[0];
					var vendorAddress = resp.split("|")[1];
					var vendorGsinNo = resp.split("|")[2];

					$("#vendorIdId").val(vendorId);
					$("#vendorAddress").val(vendorAddress);
				}
			}

			$(document).ready(function() {
				$(".vendName").text().toLowerCase();
				$(".up_down").click(function() {
						$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
						$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
			});
			
			/*script for datepickers*/
			/* ${element.intSerialNo} */
			$(document).ready(function() {
				var clscount = document.getElementsByClassName("form-invoice-data");
				var countlength = clscount.length;
				for (var i = 0; i < countlength - 1; i++) {
						$("#datepicker-paymentreq" + (i + 1)).datepicker({
							dateFormat : "dd-M-y"
						});
				}
			});

			$(function() {

				$(".datepicker-paymentreq-class").datepicker({
					changeMonth: true,
				    changeYear: true,
				    minDate:0,
					dateFormat : "dd-M-y"
				});
				$("#datepicker-paymentreq-fromdate").datepicker({
					changeMonth: true,
				    changeYear: true,
					maxDate : new Date(),
					dateFormat : "dd-M-y",
					onSelect: function(selected) {
		     	        $("#datepicker-paymentreq-todate").datepicker("option","minDate", selected)
		     	        }
				});
				$("#datepicker-paymentreq-todate").datepicker({
					maxDate : new Date(),
					changeMonth: true,
				      changeYear: true,
					dateFormat : "dd-M-y",
					onSelect: function(selected) {
		            	$("#datepicker-paymentreq-fromdate").datepicker("option","maxDate", selected)
		            	        }
				});
			});
			/*script for datepickers*/

			function convertDate(d) {
				var p = d.split("/");
				return +(p[2] + p[1] + p[0]);
			}

			$(function() {
				$(".ReqDateId1").datepicker({
					changeMonth: true,
				      changeYear: true,
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				$(".ReqDateId2").datepicker({
					changeMonth: true,
				      changeYear: true,
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId1").datepicker({
					changeMonth: true,
				      changeYear: true,
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId2").datepicker({
					changeMonth: true,
				      changeYear: true,
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				return false;
			});

			/* 				$(document).ready(function() {	
			 $(".up_down").click(function(){ 
			 $(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
			 $(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			 }); 
			
			 }); */
			/* $('#tblnotification').DataTable(); */

			/* 					$(function(){
			 var div1 = $(".right_col").height();
			 var div2 = $(".left_col").height();
			 var div3 = Math.max(div1,div2);
			 $(".right_col").css('max-height', div3);
			 $(".left_col").css('min-height', $(document).height()-65+"px");
			 }); */
			/* $('#tblnotification').stacktable({myClass:'stacktable small-only'}); */

			/* *************** calculations for filter data***********************	 */

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

						$('#delete-row').on('click', function() {
							$('#paymentTable').find('tbody').children().first().eq(0).remove();
						});
					});
			
			
			function saveRecords() {
				debugger;
				$('.loader-sumadhura').show();
				if ($('input[type=checkbox]:checked').length == 0) {
					alert('Please select atleast one vendor.');
					$('.loader-sumadhura').hide();
					return false;
				}

				if ($('#myMessage').val() == '') {
					alert('Input can not be left blank');
				}

				var amntTObeReleased = $("#")
				
                var status = validateRowData();
				
				/* alert("status "+status); */
				
				if(!status){
					$('.loader-sumadhura').hide();
				     return status;
				
				}
				//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;

				var canISubmit = window.confirm("Do you want to Submit?");

				if (canISubmit == false) {
					$('.loader-sumadhura').hide();
					return false;
				}
                $("#paymentIntiateButton").attr("disabled", true);
				document.getElementById("paymentIntiateButton").disabled = true;
				document.getElementById("paymentIntiateFormId").action = "savePaymentIntiateDetailsOnPOAdvance.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
			}
			
			/* **************** Valdiate from date and Todate************ */

			function validateRowData() {
				debugger;
				var noOfRecords= $('#noOfRecords').val();
				//alert("noOfPendingPayments  "+noOfRecords); 
				
				var AmnttoBeRelsed = "";
				var Amntreqdate = "";
				var isSelectAnyone = ""; 
				var paymentReqUpto = "";
				var paymentDoneUpto = "";
				var poAmount = ""; 
				for (i = 1; i <= noOfRecords; i++) { 

					
					var checkedValue = $('#chbox'+i+':checked').val();
					
					 AmnttoBeRelsed = $("#AmountToBeReleased" + i).val(); 
					 Amntreqdate = $("#PaymentreqDateId" + i).val();
					 paymentReqUpto = $("#paymentReqUpto" + i).val();
					 paymentDoneUpto = $("#paymentDoneUpto" + i).val();
					 poAmount = $("#poAmount" + i).val();
					
			      if(checkedValue == "checked" ) {
					 if(AmnttoBeRelsed == "" || AmnttoBeRelsed == null || AmnttoBeRelsed == '') {
				          alert("Please enter Amount to be released in request S.No:" +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;

			          }
					 
					 if(Amntreqdate == "" || Amntreqdate == null || Amntreqdate == '') {
				          alert("Please enter payment request date in request S.No:" +i);
				          document.getElementById("PaymentreqDateId"+i).focus(); 
				          return false;

			          }
					 var payingAmt = (+paymentReqUpto + +paymentDoneUpto + +AmnttoBeRelsed);// used the unary plus operator to convert them to numbers first.
					 if(+payingAmt > +poAmount){
						  alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;
					 }
					 
			 		}
					 //end
				}
				return true;
			}
			//**********Method for validation for PO Number*************
			function validate() {
	debugger;
	var POFrom=$("#datepicker-paymentreq-fromdate").val();
    var POTo =$("#datepicker-paymentreq-todate").val();
    var PONumber =$("#PONumber").val();
    var VendorName=$("#VendorNameId").val();
    var SelectAll =$("#selectAll").prop("checked"); 
    
    $("#hiddenSelectAll").val(SelectAll);
 
	
    //trim method 
    var POFromdate=POFrom.trim();
    var POTodate=POTo.trim();
    var PONumberId=PONumber.trim();
    var VendorNameId=VendorName.trim();
  
	if (!POFromdate && !POTodate && !PONumberId && !VendorNameId && SelectAll==false) {
		alert("Atleast one filed is required.");
		return false;
	}
	else{
		debugger;
		$('.loader-sumadhura').show();
		return true;
	}

			}

			/* ********** Method for sorting table************ */

			//**********Method for validation for PO Invoice*************  
			function validate2() {

				var InvfrmDate = $("#invoiceReqDateId1").val();
				var InvtoDate = $("#invoiceReqDateId2").val();

				if (InvfrmDate == "" || InvfrmDate == null || InvfrmDate == '' || InvtoDate == "" || InvtoDate == null 	|| InvtoDate == '') {
					alert("Please enter from date and to date");

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
				$('table tbody tr td:contains("' + value + '")').each(function(index, item) {
							$(this).closest('tr').show();
							var reqamount = $(this).closest('tr').find('.poAmnt').text();
							sum = sum + parseFloat(reqamount);
							$("#subtotalpoAmount1").text(sum);
							$(".info").show();

							var reqamount1 = $(this).closest('tr').find('.Tillamnt').text();
							tquantity = tquantity + parseFloat(reqamount1);
							$("#subtotalpoAmount2").text(tquantity);
							$(".info").show();

							var reqamount2 = $(this).closest('tr').find('.paidnmnt').text();
							PaidAmnt = PaidAmnt + parseFloat(reqamount2);
							$("#subtotalpoAmount3").text(PaidAmnt);
							$(".info").show();
			 });

			};
		</script>
		<script>
			/* url sending to page start and hide and show iframe */
			function SetPage(url) {
				var z = document.getElementById("myframe").setAttribute("src", url);
				$('.loader-sumadhura').show();
			}
			$('#myframe').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".anchor-invoice").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-iframe").show();
			});
			$("#cancel-btn").click(function() {
				$("#hide-show-iframe").show();
				$(".cancel-iframe").hide();
				var z = document.getElementById("myframe").setAttribute("src", '');
			});
			/* url sending to page end and hide and show iframe */
			/* full image sending to page start and hide and show iframe */
			function SetPageImage(url) {
				var f = document.getElementById("myframe-1").setAttribute("src", url);
				$('.loader-sumadhura').show();
			}
			$('#myframe-1').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".ponumber-anchor").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-image-iframe").show();

			});
			$("#cancel-btn-1").click(function() {
				$("#hide-show-iframe").show();
				$(".cancel-image-iframe").hide();
				var f = document.getElementById("myframe-1").setAttribute("src", '');
			});
			/* full image sending to page end and hide and show iframe */
			
			
			//checking number number 
			function isNumberCheck(el, evt) {
				 var charCode = (evt.which) ? evt.which : event.keyCode;
				 var num=el.value;
				 var number = el.value.split('.');
				 if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
				      return false;
				 }
				  //just one dot
				 if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {
				       return false;
				 }
				 //get the carat position
				 var caratPos = getSelectionStart(el);
				 var dotPos = el.value.indexOf(".");
				 if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
				     return false;
				 }
				   return true;
			}
			function getSelectionStart(o) {
				if (o.createTextRange) {
					var r = document.selection.createRange().duplicate();
					r.moveEnd('character', o.value.length);
					if (r.text == '') return o.value.length;
						return o.value.lastIndexOf(r.text);
					} else return o.selectionStart;
			}
			
			function validateAmountToBeReleased(id){
				var amountTpBeReleased=$("#AmountToBeReleased"+id).val();
				if(isNaN(amountTpBeReleased)){
					alert("Please enter valid data.");
					$("#AmountToBeReleased"+id).val("");
					$("#AmountToBeReleased"+id).focus();
					return false;				
				}
			}
			
			
			/* Reload the site when reached via browsers back button */
			if(!!window.performance && window.performance.navigation.type == 2)
			{
			    window.location.reload();
			}
		    /* Reload the site when reached via browsers back button */
			
		</script>
		
</body>
</html>
