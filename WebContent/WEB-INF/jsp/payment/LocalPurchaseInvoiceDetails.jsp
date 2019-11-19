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
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />

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
<script src="//code.jquery.com/jquery.min.js"></script>
<!-- 	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script> -->
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">


<style>
.loader-sumadhuraforsubmit{ 
	position: absolute;
   left:58%;
   top:50%;
   }
    @media only screen and (min-width:320px) and (max-width:767px){
    .loader-sumadhuraforsubmit {
    position: absolute;
    left: 40%;
    top: 75%;
}
   }
   
.form-control {border:1px solid #000 !important;}

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
@media only screen and (min-width: 769px){
.amount-payments-sum, .amount-payments-sum-right {
    width: 33.3% !important;
}
}
.selectAllchk span { font-size: 16px; font-weight: bold;}
.selectAllchk .selectAllchkinput {height: 22px;width: 22px;vertical-align: bottom;}
</style>
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
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">Update Local Payments</li>
						</ol>
					</div>
					<div class="loader-sumadhura" style="z-index:99;display:none;">
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
					<!-- This loader for onclick on submit button -->
					<div class="loader-sumadhuraforsubmit" style="z-index:999;">
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
							<h4 class="text-center">INVOICE</h4>
						</div>
						<div class="body-invoice-payment">
							<form class="form-horizontal" action="getLocalPurchaseInvoiceDetails.spring">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>Invoice From :</strong></label>
											<div class="col-md-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color" id="datepicker-paymentreq-fromdate" name="fromDate"	value="${fromDate}" autocomplete="off" readonly/>
												<label class="input-group-addon btn" for="datepicker-paymentreq-fromdate"><span class="fa fa-calendar"></span></label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>Invoice To :</strong></label>
											<div class="col-md-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color" id="datepicker-paymentreq-todate" name="toDate"	value="${toDate}" autocomplete="off" readonly/>
												<label class="input-group-addon btn" for="datepicker-paymentreq-todate"><span class="fa fa-calendar"></span></label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>Invoice Number :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height" name="InvoiceNumber" id="InvoiceNumber" value="${invoiceNumber}" autocomplete="off"/>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-5 col-xs-12"><strong>Vendor Name :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height" id="VendorNameId" name="vendorName" value="${vendorName}" autocomplete="off"/>
											</div>
										</div>
									</div>
									<%-- <c:if test="${showDropdownSite}">
									<div class="col-md-6 col-xs-12">
  	 									<div class="form-group">
  	  										<label class="col-md-5 col-xs-12">Site :</label>
	  											<div class="col-md-6 col-xs-12">
	  												<%
														List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
														String strSiteId = "";
														String strSiteName = "";
													%> <select id="dropdown_SiteId" name="siteIdAndName"
															class="custom-combobox form-control indentavailselect">
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
  									</c:if> --%>
  									<div class="col-md-12 text-center selectAllchk">
									  <input type="checkbox" class="selectAllchkinput" id="selectAll" name="selectAll" value="" /><span>&nbsp;Select All</span>
									  <input type="hidden" id="hiddenSelectAll" name="hiddenSelectAll" value="" />									
									</div>
									<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12 text-center center-block">
										<button class="btn btn-warning submit-form-invoice Mrgtop10" onclick="return validate()">Submit</button>
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
						<br></br>
						<%
							String isShowGrid = request.getAttribute("showGrid") == null
									? ""
									: request.getAttribute("showGrid").toString();
							if (isShowGrid.equals("true")) {
								int intTotalInvoices = Integer.parseInt(request.getAttribute("listTotalInvoicesSize") == null
										? ""
										: request.getAttribute("listTotalInvoicesSize").toString());
						%>
						<div class="clearfix"></div>
						<div class="col-md-12 no-padding-left no-padding-right">
							<div class="col-md-4 Mrg-total no-padding-left amount-payments-sum">
								<div class="col-md-12 no-padding-left text-center">
									<strong>Total&nbsp;Invoice&nbsp;Amount </strong>
								</div>
								<div class="col-md-12 text-center"><h4><strong>${TotalInvoiceAmount}</strong></h4></div>
							</div>
							<div class="col-md-4  Mrg-total amount-payments-sum">
								<div class="col-md-12 text-center">
									<strong>Till Req Amount </strong>
								</div>
								<div class="col-md-12 text-center"><h4><strong>${TotalTillReqAmount}</strong></h4></div>
							</div>
							<div
								class="col-md-4 Mrg-total no-padding-right amount-payments-sum-right">
								<div class="col-md-12 text-center">									
									<strong>Paid Amount</strong>									
								</div>
								<div class="col-md-12 text-center"><h4><strong>${TotalPaidAmount}</strong></h4></div>
							</div>
						</div>
						<div class="clearfix"></div>
						<form class="form-horizontal" id="paymentIntiateFormId">
							<c:forEach items="${listTotalInvoices}" var="element">
								<!-- form items data start -->
								<div class="form-invoice-data" id="slide-form-fileds">
									<label class="" style="z-index:1;"> <input type="checkbox" style="height:20px;width:20px;margin-top:0px;" name="checkboxname${element.intSerialNo}" id="chbox${element.intSerialNo}" value="checked"></label>
									<div class="col-md-12">
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">S.No :</label>
												<div class="col-md-6">
													 <p>
														<strong>${element.intSerialNo}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Invoice Date :</label>
												<div class="col-md-6">
													<p>
														<strong>${element.strInvoiceDate}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Invoice Number :</label>
												<div class="col-md-6">
													<div class="col-md-10 no-padding-left">
														<span><a href="getInvoiceDetails.spring?invoiceNumber=${element.strInvoiceNo}&vendorName=${element.strVendorName}&vendorId=${element.strVendorId}&IndentEntryId=${element.intIndentEntryId}&SiteId=${element.strSiteId}&invoiceDate=${element.strInvoiceDate}" target="_blank" class="" style="color:blue;font-weight:bold;word-break: break-word;">${element.strInvoiceNo}</a></span>
													</div>
													<div class="col-md-2 no-padding-right">
														<c:if test="${element.hasImage}">
														<span style="float: right;" href="#" onclick="SetPageImage('<%=url%>/getInvoiceDetails.spring?invoiceNumber=${element.strInvoiceNo}&vendorName=${element.strVendorName}&vendorId=${element.strVendorId}&IndentEntryId=${element.intIndentEntryId}&SiteId=${element.strSiteId}&invoiceDate=${element.strInvoiceDate}&imageClick=true')" class="ponumber-anchor" ><i	class="fa fa-file-image-o check-image"></i></span>
														</c:if>
													</div>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">PO Number :</label>
												<div class="col-md-6">
													<p class="color-blue">
													<a style="text-decoration: underline;color:blue;word-break: break-word;" href="#" onclick="SetPageImage('<%=url%>/getPoDetailsList.spring?poNumber=${element.strPONo}&siteId=${element.strSiteId}&imageClick=true')" class="ponumber-anchor" >${element.strPONo}</a>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Vendor Name :</label>
												<div class="col-md-6">
													<p style="word-break: break-word;">
														<strong>${element.strVendorName}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Payable Invoice Amount :</label>
												<div class="col-md-6">
													<p class="color-blue">
														<strong>${element.invoiceAmount_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Till Req Amount :</label>
												<div class="col-md-6">
													<p style="color:blue;">
														<strong>${element.paymentRequestedUpto_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Paid Amount :</label>
												<div class="col-md-6">
													<p>
														<strong>${element.paymentDoneUpto_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Credit Note Number :</label>
												<div class="col-md-6">
													<p class="color-blue">
														<strong>${element.strCreditNoteNumber}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Credit Amount :</label>
												<div class="col-md-6">
													<p class="color-blue">
														<strong>${element.creditTotalAmount_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Received Date :</label>
												<div class="col-md-6">
													<p>
														<strong>${element.strInvoiceReceivedDate}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Security deposit at vendor :</label>
												<div class="col-md-6">
													<p>
														<strong>${element.securityDeposit_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Payment Done Date :</label>
												<div class="col-md-6 input-group">
													<input type="text" class="form-control control-text-height readonly-color" name="paymentDate${element.intSerialNo}" id="paymentDate${element.intSerialNo}" value=""  autocomplete="off"/>
													<label class="input-group-addon btn" for="paymentDate${element.intSerialNo}" onclick="paymentDoneCalender(${element.intSerialNo})"><span class="fa fa-calendar"></span></label>
												</div>
											</div>
										</div>
										
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">UTR/Check No :</label>
												<div class="col-md-6">
													<input type="text" class="form-control control-text-height"
														name="utrChequeNo${element.intSerialNo}" id="utrChequeNo${element.intSerialNo}" 
														value="" autocomplete="off"/>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Mode of Payment :</label>
												<div class="col-md-6">
													<select class="form-control" id="paymentMode${element.intSerialNo}" name="paymentMode${element.intSerialNo}">
														<option value="">--SELECT--</option>
														<option value="CASH">CASH</option>
														<option value="CHEQUE">CHEQUE</option>
														<option value="ONLINE">ONLINE</option>
													</select>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Amount to be released :</label>
												<div class="col-md-6">
													<input type="text" class="form-control control-text-height" name="AmountToBeReleased${element.intSerialNo}" id="AmountToBeReleased${element.intSerialNo}" onkeypress="return isNumberCheck(this, event)"  onfocusout="checkingNumberOnPaste(this.id)"  required="required"/>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Adjust Amount form Advance :</label>
												<div class="col-md-6">
													<input type="text" class="form-control control-text-height" name="AdjustAmountFromAdvance${element.intSerialNo}" id="AdjustAmountFromAdvance${element.intSerialNo}" value="${element.doubleAdjustAmountFromAdvance}" onkeypress="return isNumberCheck(this, event)"/>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">PO Advance :</label>
												<div class="col-md-6">
													<p>
														<strong>${element.poAdvancePayment_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix"></div>
										
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Remarks :</label>
												<div class="col-md-6">
													<textarea class="form-control textarea-control" name="remarks${element.intSerialNo}"></textarea>
												</div>
											</div>
										</div>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Note :</label>
												<div class="col-md-6">
													<textarea class="form-control textarea-control" readonly="true">${element.strRemarks}</textarea>
												</div>
											</div>
										</div>
										<%-- <div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Total Invoice Amount :</label>
												<div class="col-md-6">
													<textarea class="form-control textarea-control" readonly="true">${element.vendorInvoiceTotalAmount_WithCommas}</textarea>
												</div>
											</div>
										</div> --%>
										<div class="col-md-4">
											<div class="form-group">
												<label class="col-md-6">Total Invoice Amount :</label>
												<div class="col-md-6">
													<p class="color-blue">
														<strong>${element.vendorInvoiceTotalAmount_WithCommas}</strong>
													</p>
												</div>
											</div>
										</div>
									</div>

								</div>
								<!-- Hidden Fields Start -->
								<input type="hidden" name = "paymentType${element.intSerialNo}"  value = "DIRECT">
						     	<input type="hidden" name = "invoiceDate${element.intSerialNo}"  value = "${element.strInvoiceDate}">
							 	<input type="hidden" name = "invoiceNo${element.intSerialNo}"  value = "${element.strInvoiceNo}">
							 	<input type="hidden" name = "invoiceNoInAP${element.intSerialNo}"  value = "${element.strInvoiceNoInAP}">
							 	<input type="hidden" name = "vendorId${element.intSerialNo}"  value = "${element.strVendorId}">
							 	<input type="hidden" name = "vendorName${element.intSerialNo}"  value = "${element.strVendorName}">
							 	<input type="hidden" name = "paymentSeqId${element.intSerialNo}"  value = "${element.intPaymentId}">
							 	<input type="hidden" name = "invoiceAmount${element.intSerialNo}" id = "invoiceAmount${element.intSerialNo}"  value = "${element.doubleInvoiceAmount}">
							 	<input type="hidden" name = "siteId${element.intSerialNo}"  value = "${element.strSiteId}">
							 	<input type="hidden" name = "paymentDoneUpto${element.intSerialNo}" id = "paymentDoneUpto${element.intSerialNo}"  value = "${element.doublePaymentDoneUpto}">
							 	<input type="hidden" name = "dcNo${element.intSerialNo}"  value = "${element.strDCNo}">
							 	<input type="hidden" name = "paymentReqUpto${element.intSerialNo}" id = "paymentReqUpto${element.intSerialNo}"  value = "${element.doublePaymentRequestedUpto}">
		                     	<input type="hidden" name = "poNo${element.intSerialNo}"  value = "${element.strPONo}">
		                     	<input type="hidden" name = "poAmount${element.intSerialNo}"  value = "${element.doublePOTotalAmount}">
			                   	<input type="hidden" name = "poDate${element.intSerialNo}"  value = "${element.strPODate}">
		                     	<input type="hidden" name = "invoiceReceivedDate${element.intSerialNo}"  value = "${element.strInvoiceReceivedDate}">
		                     	<input type="hidden" name = "dcDate${element.intSerialNo}"  value = "-">
		                     	<input type="hidden" name = "indentEntryId${element.intSerialNo}"  value = "${element.intIndentEntryId}">
								<input type="hidden" name = "creditTotalAmount${element.intSerialNo}"  value = "${element.doubleCreditTotalAmount}">
								<input type="hidden" name = "creditNoteNumber${element.intSerialNo}"  value = "${element.strCreditNoteNumber}">
								<input type="hidden" name = "remainingPOAdvance${element.intSerialNo}"  value = "${element.doublePOAdvancePayment}">
								<input type="hidden" name = "payBalanceInPo${element.intSerialNo}"  value = "${element.payBalanceInPo}">
								<input type="hidden" name = "paymentDoneOnMultipleInvoices${element.intSerialNo}"  value = "${element.paymentDoneOnMultipleInvoices}">
								<input type="hidden" name = "adjustedAmountFromPo${element.intSerialNo}"  value = "${element.adjustedAmountFromPo}">
								<!-- Hidden Fields End -->
								
								<!-- form items data end -->
							</c:forEach>
							<input type="hidden" name="noOfRecords" id= "noOfRecords" value="<%=intTotalInvoices%>">
							<div class="text-center center-block col-md-12">
							 <input class="btn btn-warning btn-margin-top-bottom" type="button" value="Save" id="paymentIntiateButton" onclick="saveRecords()">
							</div>
						</form>
						<%
							}
							else{
						%>
						<c:if test="${not firstRequest}">
							<div class="col-md-12 text-center"><font color="red" size="5" >Data not available.</font></div>
						</c:if>
						<% } %>
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
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/numberCheck.js" type="text/javascript"></script>
		<script src="js/jquery.simpleTableSort.js" type="text/javascript"></script>
		<script src="js/sidebar-resp.js" type="text/javascript"></script>
		<script>
		
		$(function() {
		  	$('#VendorNameId').keypress(function () {debugger;
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
		  	});
		  	$('#VendorNameId').on('change', function(){
		  		var value = $(this).val();
		  		
		  		value = value.replace("&", "$$$");
		  		//alert(value);
		  		
		  		setVendorData (value); //pass the value as paramter
			 });
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
				$(".datepicker-paymentdonedate-class").datepicker({
					changeMonth: true,
				    changeYear: true,
					dateFormat : "dd-M-y",
					maxDate : new Date() 
				}); 
				$("#datepicker-paymentreq-fromdate").datepicker({
					changeMonth: true,
				    changeYear: true,
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					onSelect: function(selected) {
				       $("#datepicker-paymentreq-todate").datepicker("option","minDate", selected)
				    }

				});
				
				$("#datepicker-paymentreq-todate").datepicker({
					changeMonth: true,
				    changeYear: true,
					dateFormat : "dd-M-y",
					maxDate : new Date(),
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
				if ($('input[type=checkbox]:checked').length == 0) {
					alert('Please select atleast one payment to update.');
					return false;
				}

				if ($('#myMessage').val() == '') {
					alert('Input can not be left blank');
				}

				var amntTObeReleased = $("#")
				
				
				var status = validateRowData();
				
				/* alert("status "+status); */
				
				if(!status){
				
				     return status;
				
				}
				//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;

				var canISubmit = window.confirm("Do you want to Save?");

				if (canISubmit == false) {
					return false;
				}

				///$("#paymentIntiateButton").attr("disabled", true);
				
				document.getElementById("paymentIntiateButton").disabled = true;
				document.getElementById("paymentIntiateFormId").action = "updateLocalPurchasePayments.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
			}
			
			/* **************** Valdiate from date and Todate************ */

			function validateRowData() {
			
				var noOfRecords= $('#noOfRecords').val();
				//alert("noOfPendingPayments  "+noOfRecords); 
				
				var paymentMode = "";
				var paymentDate = "";
				var utrChequeNo = "";
				var AmountToBeReleased = "";
				var isSelectAnyone = ""; 
				var invoiceAmount = "";
				//--
				var paymentReqUpto = "";
				var paymentDoneUpto = "";
				var AdjustAmountFromAdvance = "";
				var invoiceAmount = "";
				var remainingPOAdvance = "";
			    var payBalanceInPo = "";
			    var paymentDoneOnMultipleInvoices = "";
			    var adjustedAmountFromPo = "";
			    
			    for (i = 1; i <= noOfRecords; i++) { 

					
					var checkedValue = $('#chbox'+i+':checked').val();
					
					paymentMode = $("#paymentMode" + i).val(); 
					paymentDate = $("#paymentDate" + i).val();
					utrChequeNo = $("#utrChequeNo" + i).val();
					AmountToBeReleased = $("#AmountToBeReleased" + i).val();
					invoiceAmount = $("#invoiceAmount" + i).val();
					//----
					paymentReqUpto = $("#paymentReqUpto" + i).val();
					paymentDoneUpto = $("#paymentDoneUpto" + i).val();
					AdjustAmountFromAdvance = $("#AdjustAmountFromAdvance" + i).val(); 
					remainingPOAdvance = $('input[name="remainingPOAdvance'+i+'"]').val();
					payBalanceInPo = $('input[name="payBalanceInPo'+i+'"]').val();
					paymentDoneOnMultipleInvoices = $('input[name="paymentDoneOnMultipleInvoices'+i+'"]').val();
					adjustedAmountFromPo = $('input[name="adjustedAmountFromPo'+i+'"]').val();
					 
			      if(checkedValue == "checked" ) {debugger;
					 if(paymentMode == "" || paymentMode == null || paymentMode == '') {
				          alert("Please select  Payment Mode in request SR.No " +i);
				          document.getElementById("paymentMode"+i).focus(); 
				          return false;

			          }
					 else if(utrChequeNo == "" || utrChequeNo == null || utrChequeNo == '') {
				          alert("Please enter UTR Cheque No in request SR.No " +i);
				          document.getElementById("utrChequeNo"+i).focus(); 
				          return false;

			          }
					 else if(paymentDate == "" || paymentDate == null || paymentDate == '') {
				          alert("Please enter payment date in request SR.No " +i);
				          document.getElementById("paymentDate"+i).focus(); 
				          return false;

			          }
					 else if(AmountToBeReleased == "" || AmountToBeReleased == null || AmountToBeReleased == '') {
				          alert("Please enter Amount to be released  in request SR.No " +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;

			          }
					 else if(+AmountToBeReleased > +invoiceAmount) {
				          alert("Amount to be released is greater than invoice amount in request SR.No " +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;

			          }
					 //--amount validations
					 var payingAmt = (+paymentReqUpto + +paymentDoneUpto + +AmountToBeReleased + +AdjustAmountFromAdvance);// used the unary plus operator to convert them to numbers first.
					 if(+payingAmt > +invoiceAmount){
						  alert("Total of Intiated Amount & Paid Amount is greater than Invoice Amount in S.No:" +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;
					 }
					 if(+AdjustAmountFromAdvance > +remainingPOAdvance){
							alert("Adjust Amount is greater than Advance in S.No:" +i);
							$('input[name="AdjustAmountFromAdvance'+i+'"]').focus();
			          		return false;
					 }
					 if(payBalanceInPo != 'NO_ADVANCE'){
					 	if(+AmountToBeReleased > +payBalanceInPo){
							alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
							$('input[name="AmountToBeReleased'+i+'"]').focus();
			          		return false;
					 	}
					 	if(+AmnttoBeRelsed > +payBalanceInPo - (+paymentDoneOnMultipleInvoices - +adjustedAmountFromPo)){
							alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
							$('input[name="AmountToBeReleased'+i+'"]').focus();
			          		return false;
					 	}
					 }
					 
					 
			 		}
					 //end
				}
				return true;
				
		       /* if(isSelectAnyone !== "true"){
					
					alert("You are not approving or rejecting any payment request");
					return false;
				} */
				
			 
				/* var AmnttoBeRelsed = $("#AmountToBeReleased" + rowId).val();
				var Amntreqdate = $("#PaymentreqDateId" + rowId).val();

				 if(AmnttoBeRelsed == "" || AmnttoBeRelsed == null || AmnttoBeRelsed == ''|| Amntreqdate == "" || Amntreqdate == null || Amntreqdate == '') {
				 alert("Please enter Amount to be released and date" +rowId);
				 document.getElementById("AmnttoBeRelsed"+rowId).focus(); 

			      } */
			}
			//**********Method for validation for PO Number*************
			function validate() {
	
				 var InvoiceFrom=$("#datepicker-paymentreq-fromdate").val();
			     var InvoiceTo=$("#datepicker-paymentreq-todate").val();
			     var InvoiceNumber=$("#InvoiceNumber").val();
			     var VendorName=$("#VendorNameId").val();
			     var SelectAll =$("#selectAll").prop("checked"); 
				    
				 $("#hiddenSelectAll").val(SelectAll);
				    
			     //trim method 
			      var InvoiceFromdate=InvoiceFrom.trim();
			     var InvoiceTodate=InvoiceTo.trim();
			     var InvoiceNumberId=InvoiceNumber.trim();
			     var VendorNameId=VendorName.trim();
			   
				if (!InvoiceFromdate && !InvoiceTodate && !InvoiceNumberId && !VendorNameId && SelectAll==false ) {
					alert("Atleast one filed is required.");
					return false;
				}
				else{
					$(".loader-sumadhura").show();
					return true;
				}

			}

			/* ********** Method for sorting table************ */

			//**********Method for validation for PO Invoice*************  
		/* 	function validate2() {

				var InvoiceFrom = $("#invoiceReqDateId1").val();
				var InvoiceTo = $("#invoiceReqDateId2").val();		

				if (InvfrmDate == "" || InvfrmDate == null || InvfrmDate == ''
						|| InvtoDate == "" || InvtoDate == null
						|| InvtoDate == '') {
					alert("Please enter from date and to date");

				}

			} */
		/* 	$(function() {
				$('#VendorNameId').keypress(function() {
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
							alert(data + "_" + status + "_" + er);
						}
					});
				});
				$('#VendorNameId').on('change', function() {
					var value = $(this).val();

					value = value.replace("&", "$$$");
					//alert(value);

					setVendorData1(value); //pass the value as paramter
				});
			});

			function setVendorData1(vName) {

				var url = "loadAndSetVendorInfo.spring?vendName=" + vName;

				if (window.XMLHttpRequest) {
					request = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				try {
					request.onreadystatechange = setVedData1;
					request.open("POST", url, true);
					request.send();
				} catch (e) {
					alert("Unable to connect to server!");
				}
			}

			function setVedData1() {
				if (request.readyState == 4 && request.status == 200) {
					var resp = request.responseText;
					resp = resp.trim();
					alert(resp);
					var vendorId = resp.split("|")[0];
					var vendorAddress = resp.split("|")[1];
					var vendorGsinNo = resp.split("|")[2];

					$("#vendorIdId1").val(vendorId);

				}
			} */

			/* $(function() {
				$('#VendorNameId-Numberwise').keypress(function() {debugger;
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
							alert(data + "_" + status + "_" + er);
						}
					});
				});
				$('#VendorNameId-Numberwise').on('change', function() {
					var value = $(this).val();

					value = value.replace("&", "$$$");
					//alert(value);

					setVendorData2(value); //pass the value as paramter
				});
			}); */

			/* function setVendorData2(vName) {

				var url = "loadAndSetVendorInfo.spring?vendName=" + vName;

				if (window.XMLHttpRequest) {
					request = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				try {
					request.onreadystatechange = setVedData2;
					request.open("POST", url, true);
					request.send();
				} catch (e) {
					alert("Unable to connect to server!");
				}
			}

			function setVedData2() {
				if (request.readyState == 4 && request.status == 200) {
					var resp = request.responseText;
					resp = resp.trim();
					alert(resp);
					var vendorId = resp.split("|")[0];
					var vendorAddress = resp.split("|")[1];
					var vendorGsinNo = resp.split("|")[2];

					$("#vendorIdId2").val(vendorId);

				}
			}
 */
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
		
		 $(".submit-form-invoice").click(function(){
			 var a=$("#datepicker-paymentreq-fromdate").val();
			 var b=$("#datepicker-paymentreq-todate").val();			 
		});
		//to distroy loader
		 $(window).load(function () {
			 $(".loader-sumadhuraforsubmit").hide();
		 });
		 //Payment Done Date date picker
         function paymentDoneCalender(id){
        	 $("#paymentDate"+id).datepicker({
        		 dateFormat : 'dd-M-y',
				 maxDate : 0,
				 changeMonth : true,
				 changeYear : true
        	 });
         }
		 
         /* Reload the site when reached via browsers back button */
     	if(!!window.performance && window.performance.navigation.type == 2)
     	{
     	    window.location.reload();
     	}
         /* Reload the site when reached via browsers back button */
     	
		</script>
		<!-- script for validation -->
		
</body>
</html>
