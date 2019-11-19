<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page isELIgnored="false"%>
<%@page import="java.util.List,com.sumadhura.bean.PaymentBean"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">

<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/numberCheck.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>		
<jsp:include page="../CacheClear.jsp" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
.body-invoice-payment{margin-bottom:15px;}
.selectAllchk .selectAllchkinput{height:22px;width:22px;vertical-align: bottom;}
.selectAllchk span{font-size:16px;font-weight:bold;}
.container1 {display: none;}
.media-style {width: 39% !important;}
@media screen and (min-width: 480px) {.media-style {width: auto;}
.submitstyle {margin-top: 20px;width: 100% !important;}}
.form-content {margin-top: 30px;}
.viewPaymentsSiteLevel {position: absolute;}
.bottom-class {margin-top: 22px;}
.formShow {border: 1px solid #e4e2e2;margin-top: 35px;background: #fff;}
.DCNumber {color: black;font-weight: bold;}
hr {display: block;margin-top: 0.5em;margin-bottom: 0.5em;margin-left: auto;margin-right: auto;border-style: inset;border-width: 2px;}
.full-width {width: 100%;}
.icons-bg {padding: 3px 10px;}
.pricing-box {background: rgba(33, 32, 31, 0.2);display: flow-root;padding: 10px;border: solid 1px white;width: 100%;}
.form-control{border:1px solid #000;}
.view-payment-image {height: 20px;width: 20px;float: right;margin-top: 5px;}
.img-adjust {margin-bottom: 50px;}
.modal-footer {text-align: center !important;}
@media only screen and (min-width:320px) and (max-width:1023px){
.anchor-invoice { position: relative;}
.toggle {float: right;margin: 0;padding-top: 13px;padding-bottom: 12px;margin-right: 10px;}}
.loader-sumadhuraforsubmit{ position: absolute;left:58%;top:50%;}
 @media only screen and (min-width:320px) and (max-width:767px){.loader-sumadhuraforsubmit {position: absolute;left: 40%;top: 75%;}}

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
			<!-- page content -->
			<div class="right_col" role="main">
				<div id="mydiv" class="hide-show-iframe">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">View Payment Request</li>
						</ol>
					</div>
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
					<!-- start of searching options -->
					<div class="invoice-payment-heading"><h4 class="text-center">VIEW PAYMENT REQUEST</h4></div>
					<div class="body-invoice-payment">
							<form class="form-horizontal"  action="getAccountDeptPendingRequests.spring">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12 col-sm-12 control-label">Payment Requested Date From :</label>
											<div class="col-md-6 col-xs-12 col-sm-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-paymentreq-fromdate" name="fromDate"
													value="${fromDate}"  autocomplete="off" readonly/>
													 <label class="input-group-addon btn" for="datepicker-paymentreq-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12 col-sm-12 control-label">Payment Requested Date To :</label>
											<div class="col-md-6 col-xs-12 col-sm-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color" id="datepicker-paymentreq-todate" name="toDate" value="${toDate}"  autocomplete="off" readonly/>
												<label class="input-group-addon btn" for="datepicker-paymentreq-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>

									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12"><strong>Vendor Name :</strong></label>
											<div class="col-md-6 col-xs-12">
												  <input type="text" class="form-control control-text-height" id="VendorNameId" name="vendorName" onkeyup="getVendorId()" value="${vendorName}" autocomplete="off"/>
												  <input type="hidden" readonly="true" name="vendorId" value="${vendorId}" id="vendorIdId" autocomplete="off"/>
											</div>
										</div>
									</div>

									<div class="col-md-6 col-xs-12" style="display:none;">
										<div class="form-group">
											<label class="col-md-6 col-xs-12"><strong>Vendor Address :</strong></label>
											<div class="col-md-6 col-xs-12">
												 <input type="text" class="form-control control-text-height" id="vendorAddress" value="${vendorAddress}" autocomplete="off"/>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12"><strong>Invoice Number :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height" name="invoiceNumber" id="invoiceNumber" value="${invoiceNumber}" autocomplete="off"/>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12"><strong>PO Number :</strong></label>
											<div class="col-md-6 col-xs-12">
												<input type="text" class="form-control control-text-height" name="poNumber"  id="poNumber" value="${poNumber}" autocomplete="off"/>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-xs-12">Site/Department :</label>
											<div class="col-md-6 col-xs-12">												
												<select id="dropdown_SiteId" name="dropdown_SiteId"
												class="custom-combobox form-control "> <!-- indentavailselect -->
												</select>
											</div>


										</div>
									</div>
									<div class="col-md-12 no-padding-left no-padding-right">
										 <div class="col-md-5 col-sm-5 col-xs-5 no-padding-left no-padding-right"><hr class="hr-line"></div>
										 <div class="col-md-2 col-sm-2 col-xs-2 text-center"><strong>(Or)</strong></div>
										 <div class="col-md-5 col-sm-5 col-xs-5 no-padding-left no-padding-right"><hr class="hr-line"></div>
									</div>
									<div class="col-md-12 col-sm-12 col-xs-12 text-center selectAllchk">
									  <input type="checkbox" class="selectAllchkinput" id="selectAll" name="selectAll" value="" /><span>&nbsp;Select All</span>
									  <input type="hidden" id="hiddenSelectAll" name="hiddenSelectAll" value="" />
									
									</div>
									<div
										class="col-md-12 col-xs-12 col-sm-12 col-lg-12 text-center center-block Mrgtop20">
										<button class="btn btn-warning btn-updatepo" onclick="return validate()">Submit</button>
									</div>
								</div>
							</form>

						</div>
					<!--  end of search options-->
					<form  id="paymentApprovalform" action="" method="POST">
						<%
							int intTotalInvoices = Integer.parseInt(request.getAttribute("listTotalInvoicesSize") == null
									? ""
									: request.getAttribute("listTotalInvoicesSize").toString());
						%>

						<%
							int headerNo = 0;
							List<PaymentBean> listTotalInvoices = (List<PaymentBean>) request.getAttribute("listTotalInvoices");
							for (PaymentBean element : listTotalInvoices) {
								if (element.isVendorHeader()) {
									headerNo++;
						%>
						<%
							if (headerNo != 1) {
						%>
					
				</div>
				<!-- form -->




				<!-- ********* Second one********* -->

			</div>
		</div>
		<!-- form -->

	</div>


	</div>
	</div>
	<!--  added -->
	</div>
	<!--  added -->

	<%
		}
	%>
	<div class="paymentInvoice">
          
		<div class="container center-block" align="">

			<!--  Start section here for radio buttons inputs -->
			<!-- ********** Temporary service not avilable******* -->


			<div id="PaymentsData" style="margin-bottom: 10px;">
			<div id="maintable" cellpadding="0" cellspacing="0"
					class="pdzn_tbl1">
					<div id="payements_container">
						<div style="position: relative">
							<span
								class="glyphicon glyphicon-triangle-bottom viewPaymentsSiteLevel"
								style="font-size: 1.2em; margin-top: 14px; margin-left: 5px; z-index: 1;"></span>
						</div>
						<div class="withoutPricing-class pricing-box container">
							<div
								class="col-md-12 form-horizontal">
								<div class="col-md-3">
									<div class="form-group">
										<label for="email" class="col-md-6">Status :</label>
										<div class="col-md-6">
											<select
												id="selectStatus<%=element.getVendorGroupSerialNo()%>"
												onchange="changeStatus(<%=element.getVendorGroupSerialNo()%>)"
												class="form-control">
												<option value="$Select">--Select--</option>
												<option value="15$Approve">Approve</option>
												<!-- change value -->
												<option value="$Rejected">Reject</option>
											</select>
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6">Vendor Name :</label>
										<div class="col-md-6">
											<input type="text" class="form-control" id="VendName"
												data-toggle="tooltip" data-placement="top"
												title="<%=element.getStrVendorName()%>"
												value="<%=element.getStrVendorName()%>" name="VendName"
												readonly="true">
										</div>
									</div>

								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6">Total Amount :</label>
										<div class="col-md-6">
											<input type="text" id="TotalAmnt" class="form-control"
												value="<%out.println(element.getDoubleInvoiceAmount() == 0.0
							? element.getPoAmount_WithCommas()
							: element.getInvoiceAmount_WithCommas());%>"
												name="TotalAmnt" readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6">Requested Amount :</label>
										<div class="col-md-6">
											<input type="text" class="form-control"
												id="RequestedAmnnt<%=element.getVendorGroupSerialNo()%>"
												value="<%=element.getRequestedAmount_WithCommas()%>"
												name="RequestedAmnt" readonly="true" />
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6">No of Payments:</label>
										<div class="col-md-6">
											<input type="text" class="form-control" Id="noofPayments"
												value="<%=element.getIntNoofPaymentsVendorWise()%>"
												name="noofPayments" readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6">Site/Department :</label>
										<div class="col-md-6">
											<input type="text" class="form-control" id="SiteName"
												value="<%=element.getStrSiteName()%>" name="SiteName"
												readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label id="TotalBalance" for="" class="col-md-6">Total
											Balance Amount :</label>
										<div class="col-md-6">
											<input type="text" class="form-control" id="TotalBalance1"
												value="<%=element.getBalanceAmount_WithCommas()%>"
												name="SiteName" readonly="true">

										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="modeofpayment col-md-6" id="modeofpayment">Mode
											Of Payment:</label>
										<div class="col-md-6 ">
											<select id="modeofpayment1<%=element.getVendorGroupSerialNo()%>" class="form-control "
												onchange="changeStatusOfModeOfPayment(<%=element.getVendorGroupSerialNo()%>)"
												name="paymentMode<%=element.getIntSerialNo()%>">
												<option value="Select_None">--Select--</option>
												<option value="CASH_Pay">CASH</option>
												<option value="CHEQUE_Pay">CHEQUE</option>
												<option value="ONLINE_Pay">ONLINE</option>
											</select>
										</div>

									</div>
								</div>
							</div>
						</div>

						<input type="hidden" name="numbeOfRowsToBeProcessed" value="1"
							id="rowId">
						<!-- ************* Data show*************	 -->
						<div align="" class="formShow" style="display: none;">
							<div id="PaymentsShowData" style="margin-bottom: 20px;">
								<!-- form -->


								<%
									}

										else {
								%>



								<br />
								<div class="col-md-12">
								<div class="form-horizontal">
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label class="col-md-6">Payment
											Status:
										</label> <div class="col-md-6 "><select name="paymentIntiateType<%=element.getIntSerialNo()%>"
											id="statusSitelevel<%=element.getIntSerialNo()%>"
											class="statusSitelevel<%=element.getVendorGroupSerialNo()%> form-control">
											<option value="">--Select--</option>
											<option value="Approved">Approve</option>
											<option value="Rejected">Reject</option>
										</select></div>
									</div>
									</div>
                                    <div class="col-md-3 Mrg-bottom">
                                     <div class="form-group" >
										<label for="" class="col-md-6">Payment
											Details ID:
										</label> <div class="col-md-6"><input type="text" class="form-control"
											name="paymentDetailsId<%=element.getIntSerialNo()%>"
											value="<%=element.getIntPaymentDetailsId()%>" readonly="true"></div>
									</div>
                                    </div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group" >
										<label for="" class="col-md-6">Invoice
											Date:</span>
										</label> <div class="col-md-6"><input type="text" class="form-control"
											value="<%=element.getStrInvoiceDate()%>" readonly="true"></div>
									</div>
									</div>
                                    <div class="col-md-3 Mrg-bottom">
                                     <div class="form-group" >
										<label for="" class="col-md-6">Invoice No:
										</label> 
										<div class="col-md-6">
										<div class="col-md-10 no-padding-left">
										 <a class="anchor-invoice"
											href="getInvoiceDetails.spring?invoiceNumber=<%=element.getStrInvoiceNo()%>&vendorName=<%=element.getStrVendorName()%>&vendorId=<%=element.getStrVendorId()%>&IndentEntryId=<%=element.getIntIndentEntryId()%>&SiteId=<%=element.getStrSiteId()%>&invoiceDate=<%=element.getStrInvoiceDate()%>"
											target="_blank"><%=element.getStrInvoiceNo()%></a>
										</div>
										<div class="col-md-2 " style="float:right;">
										 <%
											if (element.isHasImage()) {
										%>
										<%-- <span><img src="data:image/jpeg;base64,<%=element.getInvoiceImage0()%>" class="img-responsive<%=element.getIntSerialNo()%> view-payment-image" data-toggle="modal" data-target="#view-payment-img<%=element.getIntSerialNo()%>" /></span> --%>
										<span data-toggle="modal"  href="#"
											class="ponumber-anchor"
											onclick="SetPage('<%=url%>/getInvoiceDetails.spring?invoiceNumber=<%=element.getStrInvoiceNo()%>&vendorName=<%=element.getStrVendorName()%>&vendorId=<%=element.getStrVendorId()%>&IndentEntryId=<%=element.getIntIndentEntryId()%>&SiteId=<%=element.getStrSiteId()%>&invoiceDate=<%=element.getStrInvoiceDate()%>&imageClick=true')"><i
											class="fa fa-file-image-o check-image"></i></span>

										<%
											}
										%>
										</div>
										



										</div>
									</div>
                                    
                                    </div>
									
									
									</div>
								</div>
								<div class="col-md-12">
								<div class="form-horizontal">
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">Payable Invoice
											Amount:
										</label> <div class="col-md-6"><input style="color: blue; border: none;"
											type="text" class="form-control"
											name="invoiceAmt<%=element.getIntSerialNo()%>"
											value="<%=element.getInvoiceAmount_WithCommas()%>" readonly="true" /></div>
									</div>
									</div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">Credit
											Note No:
										</label><div class="col-md-6"> <input
											style=" color: blue; border: none; text-decoration: underline;"
											type="text" class=""
											value="<%=element.getStrCreditNoteNumber()%>"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6" >Credit
											Amount:
										</label><div class="col-md-6"> <input
											style=" color: blue; border: none;"
											type="text" class="form-control"
											value="<%=element.getCreditTotalAmount_WithCommas()%>"
											readonly="true"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">Receive
											Date:
										</label><div class="col-md-6"> <input
											style="border: none;"
											type="text" class="form-control"
											value="<%=element.getStrInvoiceReceivedDate()%>"
											readonly="true"></div>
									</div></div>
								</div>
                               </div>

								<div class="col-md-12">
								   <div class="form-horizontal">
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">PO Date:
										</label><div class="col-md-6"> <input
											style="border: none;"
											type="text" class="form-control" value="<%=element.getStrPODate()%>"
											readonly="true" /></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">PO Number:
										</label>
										<%-- <a style="font-size: 11px;margin-left: 150px;border: none;text-decoration:underline;font-weight: 900;color: blue;display:inline-block;width: 100px;word-wrap: break-word;" target="_blank" href="getPoDetailsList.spring?poNumber=<%=element.getStrPONo()%>&siteId=<%=element.getStrSiteId()%>" style="text-decoration: underline;color: blue;display: inline-block;width: 100px;word-wrap: break-word"> <%=element.getStrPONo()%> </a>
							     --%>
										<div class="col-md-6"><a
											style="text-decoration: underline; font-weight: 900; color: blue; display: inline-block;width:120px; word-wrap: break-word;"
											href="#" class="ponumber-anchor"
											onclick="SetPage('<%=url%>/getPoDetailsList.spring?poNumber=<%=element.getStrPONo()%>&siteId=<%=element.getStrSiteId()%>&imageClick=true') "
											style="text-decoration: underline;color: blue;display: inline-block;width: 100px;word-wrap: break-word">
											<%=element.getStrPONo()%>
										</a></div>
									</div>
									
									</div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">PO Amount:
										</label> <div class="col-md-6"><input style="border: none; color: blue;"
											type="text" class=""
											value="<%=element.getPoAmount_WithCommas()%>" readonly="true"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">Vendor
											Name:
										</label> 
										<div class="col-md-6"><input
											style="border: none;margin-top: -5px;"
											type="text" class="form-control" data-toggle="tooltip"
											data-placement="top" title="<%=element.getStrVendorName()%>"
											value="<%=element.getStrVendorName()%>" readonly=true></div>
									</div>
									</div>
									</div>
								</div>
								<div class="col-md-12">
								<div class="form-horizontal">
								 <div class="col-md-3 Mrg-bottom">	<div class="form-group">
										<label for="" class="col-md-6">Payment
											Against:
										</label> <div class="col-md-6"><input
											style=" border: none;"
											type="text" class="" value="<%=element.getPaymentType().equals("DIRECT")?"Invoice":"PO"%>"
											readonly="true" /></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									<div class="form-group">
										<label for="" class="col-md-6">Comments:</label> 
										<div class="col-md-6"><input
											style="border: none; word-wrap: break-word;"
											type="text" placeholder="Enter here"
											name="comments<%=element.getIntSerialNo()%>"
											data-toggle="tooltip" title="" class="cmnts" id="commentId" /></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label class="col-md-6"for="">Req
											Amount:
										</label><div class="col-md-6"><input
											style="color: #2dcc10; border: none;"
											type="text"
											class="price<%=element.getVendorGroupSerialNo()%> "
											id="RequestedAmnt<%=element.getVendorGroupSerialNo()%>"
											onblur="calculateSum(<%=element.getVendorGroupSerialNo()%>, this.id)"
											value=<%=element.getDoubleAmountToBeReleased()%> onkeypress="return isNumberCheck(this, event)"
											name="requestAmount<%=element.getIntSerialNo()%>"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group" >
										<label for="" class="col-md-6">Requested
											Date:
										</label> <div class="col-md-6"><input
											style="border: none;"
											id="RequestedDate<%=element.getIntSerialNo()%>" class=""
											onclick="datePicker(<%=element.getIntSerialNo()%>);"
											name="RequestedDate<%=element.getIntSerialNo()%>"
											data-toggle="tooltip"
											title="<%=element.getStrPaymentReqDate()%>"
											value="<%=element.getStrPaymentReqDate()%>" readonly="true" /></div>
									</div></div>
								</div>
								</div>


								<div class="col-md-12">
								<div class="form-horizontal">
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">Adjusted Amount from Advance:</label> 
											<div class="col-md-6"><input
											style="color: #2dcc10; border: none;"
											name="AdjustAmountFromAdvance<%=element.getIntSerialNo()%>"
											class="" data-toggle="tooltip"
											title="${element.doubleAdjustAmountFromAdvance}"
											value="<%=element.getDoubleAdjustAmountFromAdvance()%>" /></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">PO Advance:
										</label> <div class="col-md-6"><input
											style="color: blue; border: none;"
											type="text" class=""
											value="<%=element.getPoAdvancePayment_WithCommas()%>"
											readonly="true"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">Remarks:</label>
										<div class="col-md-6"><input id="remarks"
											style="border: none; word-wrap: break-word;"
											name="remarksForView<%=element.getIntSerialNo()%>" class=""
											readonly="true" data-toggle="tooltip"
											title="<%=element.getStrRemarksForView()%>"
											value="<%=element.getStrRemarksForView()%>" /></div>
									</div>
									
									</div>
									<div class="col-md-3 Mrg-bottom"><div class="form-group" >
										<label for="" class="col-md-6">Security Deposit :</label>
										 <div class="col-md-6"><input
											style="color: blue; border: none;"
											type="text" value="<%=element.getSecurityDeposit_WithCommas()%>"
											class="" readonly="true" /></div>
									</div></div>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-horizontal">
									  <div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">Paid
											Amount:
										</label> <div class="col-md-6"><input
											style="border: none;"
											type="text" class=""
											value="<%=element.getPaidAmount_WithCommas()%>"
											readonly="true"></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">Balance Amount:
										</label> <div class="col-md-6"><input
											style="border: none;"
											type="text" class=""
											value="<%=element.getBalanceAmount_WithCommas()%>" readonly="true"></div>
									</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6">Till Requested Amount:
										</label>
										<div class="col-md-6"> <input
											style="color: blue; border: none;"
											type="text" class=""
											value="<%=element.getPaymentRequestedUpto_WithCommas()%>"
											readonly="true" /></div>
									</div>
									
									</div>
									<div class="col-md-3 Mrg-bottom">
									  <div class="form-group">
										<label class="col-md-6" for="">Mode Of Payment:
										</label>
										<div class="col-md-6">
										  <select
											style="border: 1px solid #000;color:#000;"
											class="form-control sectionModeOfPayment<%=element.getVendorGroupSerialNo()%>"
											name="paymentMode<%=element.getIntSerialNo()%>">
											<option value="">--SELECT--</option>
											<c:forEach items="${PaymentModes}" var="element1">
												<option value="${element1.value}">${element1.name}</option>
											</c:forEach>
										</select>
										</div>
									</div>
									  
									</div>
									
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-horizontal">
									 <div class="col-md-3 Mrg-bottom"><div class="form-group">
										<label for="" class="col-md-6">UTR No / Cheque No:
										</label>
										 <div class="col-md-6"><input
											style="color: #2dcc10; border: none;"
											name="utrOrChqNo<%=element.getIntSerialNo()%>" class=""
											autocomplete="off" placeholder="Enter here" value="" /></div>
									</div></div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6"
											style=" word-wrap: break-word;">Payment
											Initiated date:</label>
											<div class="col-md-6"><span
											style="position: absolute; word-wrap: break-word;"><%=element.getStrPaymentRequestReceivedDate()%></span>
										<!-- <input style="margin-left:100px;" type="text" class="form-control" value = "" readonly="true"> -->
									</div> </div>
									</div>
									<div class="col-md-3 Mrg-bottom">
									 <div class="form-group">
										<label for="" class="col-md-6"
											style=" word-wrap: break-word;">Total Invoice Amount:</label>
											<div class="col-md-6"><span
											style="position: absolute; word-wrap: break-word;"><%=element.getVendorInvoiceTotalAmount_WithCommas()%></span>
										<!-- <input style="margin-left:100px;" type="text" class="form-control" value = "" readonly="true"> -->
									</div> </div>
									</div>
									
									</div>
								</div>


								<input type="hidden"
									name="paymentType<%=element.getIntSerialNo()%>"
									value="<%=element.getPaymentType()%>">
									
									<input type="hidden" name="indentEntryId<%=element.getIntSerialNo()%>"
												value="<%=element.getIntIndentEntryId()%>"> 
									
									<input 	type="hidden" name="invoiceDate<%=element.getIntSerialNo()%>"
									value="<%=element.getStrInvoiceDate()%>"> <input
									type="hidden" name="invoiceNo<%=element.getIntSerialNo()%>"
									value="<%=element.getStrInvoiceNo()%>"> <input
									type="hidden" name="vendorId<%=element.getIntSerialNo()%>"
									value="<%=element.getStrVendorId()%>"> <input
									type="hidden" name="vendorName<%=element.getIntSerialNo()%>"
									value="<%=element.getStrVendorName()%>"> <input
									type="hidden" name="paymentSeqId<%=element.getIntSerialNo()%>"
									value="<%=element.getIntPaymentId()%>"> <input
									type="hidden" name="invoiceAmount<%=element.getIntSerialNo()%>"
									value="<%=element.getDoubleInvoiceAmount()%>"> <input
									type="hidden" name="siteId<%=element.getIntSerialNo()%>"
									value="<%=element.getStrSiteId()%>"><input
									type="hidden" name="siteName<%=element.getIntSerialNo()%>"
									value="<%=element.getStrSiteName()%>"> <input
									type="hidden"
									name="paymentDoneUpto<%=element.getIntSerialNo()%>"
									value="<%=element.getDoublePaymentDoneUpto()%>"> <input
									type="hidden" name="dcNo<%=element.getIntSerialNo()%>"
									value="<%=element.getStrDCNo()%>"> <input type="hidden"
									name="paymentReqUpto<%=element.getIntSerialNo()%>"
									value="<%=element.getDoublePaymentRequestedUpto()%>"> <input
									type="hidden" name="poNo<%=element.getIntSerialNo()%>"
									value="<%=element.getStrPONo()%>"> <input type="hidden"
									name="poAmount<%=element.getIntSerialNo()%>"
									value="<%=element.getDoublePOTotalAmount()%>"> <input
									type="hidden"
									name="accDeptPmtProcessId<%=element.getIntSerialNo()%>"
									value="<%=element.getIntAccDeptPaymentProcessId()%>"> <input
									type="hidden"
									name="paymentDetailsId<%=element.getIntSerialNo()%>"
									value="<%=element.getIntPaymentDetailsId()%>"> <input
									type="hidden"
									name="siteWisePaymetId<%=element.getIntSerialNo()%>"
									value="<%=element.getIntSiteWisePaymentId()%>"> <input
									type="hidden"
									name="paymentRequestDate<%=element.getIntSerialNo()%>"
									value="<%=element.getStrPaymentReqDate()%>" /> <input
									type="hidden" id="AdjustAmountFromAdvance"
									name="actualAdjustAmountFromAdvance<%=element.getIntSerialNo()%>"
									class="form-control" readonly="true"
									value="<%=element.getDoubleAdjustAmountFromAdvance()%>" /> <input
									type="hidden" id="RequestedAmt" class="form-control"
									name="actualRequestedAmt<%=element.getIntSerialNo()%>"
									value="<%=element.getDoubleAmountToBeReleased()%>" /> <input
									type="hidden"
									name="requestReceiveFrom<%=element.getIntSerialNo()%>"
									value="<%=element.getRequestReceiveFrom()%>"> <input
									type="hidden" name="remarks<%=element.getIntSerialNo()%>"
									value="<c:out value = "<%=element.getStrRemarks()%>"/>" />
									<input type="hidden" name = "remainingPOAdvance<%=element.getIntSerialNo()%>"  value = "<%=element.getDoublePOAdvancePayment()%>">
									<input type="hidden" name = "payBalanceInPo<%=element.getIntSerialNo()%>"  value = "<%=element.getPayBalanceInPo()%>">
									<input type="hidden" name = "paymentDoneOnMultipleInvoices<%=element.getIntSerialNo()%>"  value = "<%=element.getPaymentDoneOnMultipleInvoices()%>">
									<input type="hidden" name = "adjustedAmountFromPo<%=element.getIntSerialNo()%>"  value = "<%=element.getAdjustedAmountFromPo()%>">
									<!-- code for modal popup start-->

								<div id="view-payment-img<%=element.getIntSerialNo()%>"
									class="modal fade custmodal" role="dialog">
									<div class="modal-dialog custom-modal-lg">

										<!-- Modal content-->
										<div class="modal-content">
											<div class="modal-header cust-modal-header">
												<button type="button" class="close" data-dismiss="modal">&times;</button>
												<h4 class="modal-title text-center">View Payment
													request</h4>
											</div>
											<div class="modal-body cust-modal-body">
												<img
													src="data:image/jpeg;base64,<%=element.getInvoiceImage0()%>"
													class="img-responsive<%=element.getIntSerialNo()%> center-block" />
												<img
													src="data:image/jpeg;base64,<%=element.getInvoiceImage1()%>"
													class="img-responsive<%=element.getIntSerialNo()%> center-block" />
												<img
													src="data:image/jpeg;base64,<%=element.getInvoiceImage2()%>"
													class="img-responsive<%=element.getIntSerialNo()%> center-block" />
												<img
													src="data:image/jpeg;base64,<%=element.getInvoiceImage3()%>"
													class="img-responsive<%=element.getIntSerialNo()%> center-block" />

											</div>
											<div class="modal-footer cust-modal-footer">
												<button type="button" class="btn btn-danger"
													data-dismiss="modal">Close</button>
											</div>
										</div>

									</div>
								</div>

								<!-- code for modal popup end -->
								<hr style="margin-top: 20px;">



								<%
									}
									}
								%>




								<%
									if (headerNo != 0) {
								%>



								<%
									}
								%>

							</div>

						</div>
						<div class="paymentInvoice">



							<input type="hidden" name="numbeOfRowsToBeProcessed" value=""
								id="countOfRows"> <input type="hidden"
								name="isSaveBtnClicked" value="" id="hiddenSaveBtnId"> <input
								type="hidden" name="ttlAmntForIncentEntry" value=""
								id="ttlAmntForIncentEntryId"> <input type="hidden"
								name="VendorId" value="" id="vendorIdId"> <input
								type="hidden" name="noOfPendingPayments" id="noOfPendingPayments"
								value="<%=intTotalInvoices%>">

						</div>


					</div>

				</div>

			</div>




			<div class="btn-submit col-md-12 text-center center-block">
			<c:choose>
			<c:when test="${showSubmit}">
				<button class="btn btn-warning  "
					id="btn-submitt" type="button"
					style="margin-top: 21px; margin-bottom: 20px;" onclick="validateformData()">Submit</button>
			</c:when> 
			<c:otherwise>
				<c:if test="${not firstRequest}">
					<div class="col-md-offset-3 col-md-9 text-center"><font color="red" size="5" >Data not available.</font></div>
				</c:if>
			</c:otherwise>	 
			</c:choose>
			</div>
			</form>

			<!-- Section start for While searching the DC number  -->



		</div>
	</div>

	<!-- /page content -->
	</div>

	<!-- iframe start -->
	<div id="cancel-iframe" style="display: none; position: relative;">
		<!-- /*css loader here*/ -->
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
		<!-- /*css loader here*/ -->
		<iframe id="myframe" style="display: none;"></iframe>

		<button id="cancel-btn"
			class="btn btn-warning text-center center-block"
			style="margin-top: 20px;">Close</button>
	</div>
	<!-- iframe end -->
	</div>

		<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- <script src="js/CovertDCTOInvoice.js" type="text/javascript"></script> -->

	<script>
		$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
										});

						
		
					 
						
/* Method for toggle the search button */	
	$(document).on("click", ".viewPaymentsSiteLevel",function(){
	  	/*  $(".viewPaymentsSiteLevel").parent().parent().find(".formShow").toggle();   */
 	 $(this).parent().parent().find(".formShow").slideToggle('slow' );  
		
 });
/* ******** Method for calculation for Total amount********* */
/* 	$('.price').blur(function () {;
	    var sum = 0;
	    $('.price').each(function() {
	        sum += Number($(this).val());
	    });
$("#RequestedAmnnt").val(sum);
	    // here, you have your sum
	}); */
	
	function calculateSum(rowId, id){debugger;
		var reqAmnt=$("#"+id).val();
		if(isNaN(reqAmnt)){
			alert("Please enter valid data.");
			$("#"+id).val("");
			$("#"+id).focus();
			return false;
		}
		 var sum = 0;
		    $('.price'+rowId).each(function() {
		        sum += Number($(this).val());
		    });
	$("#RequestedAmnnt"+rowId).val(sum);
	};
/* ************Method for changing the dropdown values************** */

function changeStatus(rowNum){debugger;

	var Status= $("#selectStatus"+ rowNum).val();


    
    if(Status == "15$Approve"){
    	 $('.statusSitelevel'+rowNum).html("<option value='' >--Select--</option><option value='Approved'  selected='selected'>Approve</option><option value='Rejected' >Reject</option>");

    }if  (Status == "$Rejected" ){
    	 $('.statusSitelevel'+rowNum).html("<option value='' >--Select--</option><option value='Approved' >Approve</option><option value='Rejected'  selected='selected'>Reject</option>");
	}if(Status == "$Select"){
		$('.statusSitelevel'+rowNum).html("<option value=''  selected='selected'>--Select--</option><option value='Approved' >Approve</option><option value='Rejected' >Reject</option>");
	}
    
    
    
    
}
/* ************Method for changing the Mode of Payment************** */

function changeStatusOfModeOfPayment(rowNum){debugger;


	var ModeOfPay= $('#modeofpayment1'+rowNum).val();

	if( ModeOfPay == "CASH_Pay"){
    	$('.sectionModeOfPayment'+ rowNum).html("<option value=''>--Select--</option><option value='CASH' selected='selected'>CASH</option><option value='CHEQUE'>CHEQUE</option><option value='ONLINE'>ONLINE</option>");

	}if  (ModeOfPay == "ONLINE_Pay" ){
    	$('.sectionModeOfPayment'+rowNum).html("<option value=''>--Select--</option><option value='CASH' >CASH</option><option value='CHEQUE'>CHEQUE</option><option value='ONLINE' selected='selected'>ONLINE</option>");
	}if(ModeOfPay == "CHEQUE_Pay" ){
		$('.sectionModeOfPayment'+rowNum).html("<option value=''>--Select--</option><option value='CASH' >CASH</option><option value='CHEQUE' selected='selected'>CHEQUE</option><option value='ONLINE' >ONLINE</option>");
	}
	if( ModeOfPay == "Select_None"){
	    $('.sectionModeOfPayment'+ rowNum).html("<option value='' selected='selected'>--Select--</option><option value='CASH' >CASH</option><option value='CHEQUE'>CHEQUE</option><option value='ONLINE'>ONLINE</option>");

	}
 

}

/* ************** Tooltip Code********** */
 	$(document).ready(function(){
			  $('.cmnts').keyup(function(){
			    $(this).attr('title',$(this).val());
			  });
			});
</script>
	<script>
function SetPage(url)
{
 var x = document.getElementById("myframe").style="display:block";
 var y=document.getElementById("cancel-iframe").style="display:block";
 var y=document.getElementById("mydiv").style="display:none";
 var z= document.getElementById("myframe").setAttribute("src", url);

//$("#loader-payment").fadeOut("slow");
 
 
	    $('.loader-sumadhura').show();
	  
	    
	
	

}
$('#myframe').load(function () {
    $('.loader-sumadhura').hide();
   
    
});



	
	$("#cancel-btn").click(function(){	
	$("#cancel-iframe").hide();
	
	$(".hide-show-iframe").show();
	var z= document.getElementById("myframe").setAttribute("src", ""); 
	
	 
	
		});
	
//validateRequestedAmounts() - Rafi
function validateRequestedAmounts() {debugger;
	
	var noOfRecords= $('#noOfPendingPayments').val();	
	var statusSitelevel1 = "";
	var paymentReqUpto = "";
	var paymentDoneUpto = "";
	var RequestedAmt = "";
	var actualRequestedAmt = "";
	var AdjustAmountFromAdvance = "";
	var actualAdjustAmountFromAdvance = "";
	var invoiceAmount = "";
	var poAmount = "";
	var paymentType = "";
	var paymentDetailsId = "";
	var remainingPOAdvance = "";
	var payBalanceInPo = "";
	var paymentDoneOnMultipleInvoices = "";
	var adjustedAmountFromPo = "";
    
	for (i = 1; i <= noOfRecords; i++) { 					
		
		statusSitelevel1 = $('#statusSitelevel'+i).val();
		paymentReqUpto = $('input[name="paymentReqUpto'+i+'"]').val();
		paymentDoneUpto = $('input[name="paymentDoneUpto'+i+'"]').val();
		RequestedAmt = $('input[name="requestAmount'+i+'"]').val();
		actualRequestedAmt = $('input[name="actualRequestedAmt'+i+'"]').val();
		AdjustAmountFromAdvance = $('input[name="AdjustAmountFromAdvance'+i+'"]').val();
		actualAdjustAmountFromAdvance = $('input[name="actualAdjustAmountFromAdvance'+i+'"]').val();
		invoiceAmount = $('input[name="invoiceAmount'+i+'"]').val();
		poAmount = $('input[name="poAmount'+i+'"]').val();
		paymentType = $('input[name="paymentType'+i+'"]').val();
		paymentDetailsId = $('input[name="paymentDetailsId'+i+'"]').val();
		remainingPOAdvance = $('input[name="remainingPOAdvance'+i+'"]').val();
		payBalanceInPo = $('input[name="payBalanceInPo'+i+'"]').val();
		paymentDoneOnMultipleInvoices = $('input[name="paymentDoneOnMultipleInvoices'+i+'"]').val();
		adjustedAmountFromPo = $('input[name="adjustedAmountFromPo'+i+'"]').val();
		 
		if(statusSitelevel1 == 'Approved' || statusSitelevel1 == 'Rejected'){
			debugger;
			var payingAmt = (+paymentReqUpto + +paymentDoneUpto + +RequestedAmt - +actualRequestedAmt + +AdjustAmountFromAdvance - +actualAdjustAmountFromAdvance); // used the unary plus operator to convert them to numbers first.
			if(paymentType == 'DIRECT'){
				if(+payingAmt > +invoiceAmount){
	          		alert("Total of Intiated Amount & Paid Amount is greater than Invoice Amount for Payment Details ID: "+paymentDetailsId);
	          		$('input[name="requestAmount'+i+'"]').focus();
	          		return false;
				}
			}
			if(paymentType == 'ADVANCE'){
					if(+payingAmt > +poAmount){
	          		alert("Total of Intiated Amount & Paid Amount is greater than PO Amount for Payment Details ID: "+paymentDetailsId);
	          		$('input[name="requestAmount'+i+'"]').focus();
	          		return false;
				}
			}
			if((+AdjustAmountFromAdvance - +actualAdjustAmountFromAdvance) > +remainingPOAdvance){
				alert("Adjust Amount is greater than Advance for Payment Details ID: "+paymentDetailsId);
				$('input[name="requestAmount'+i+'"]').focus();
          		return false;
			}
			if(paymentType == 'DIRECT'){
				if(payBalanceInPo != 'NO_ADVANCE'){
				 	if(+RequestedAmt > +payBalanceInPo){
						alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
						$('input[name="requestAmount'+i+'"]').focus();
		          		return false;
				 	}
				 	if(+RequestedAmt > +payBalanceInPo - (+paymentDoneOnMultipleInvoices - +adjustedAmountFromPo)){
						alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
						$('input[name="AmountToBeReleased'+i+'"]').focus();
		          		return false;
				 	}
				}
			}
		}
		 
	}	
	 return true;
}
	
	function validateformData(){
		
		//alert("validate form data ");
		
		var noOfPendingPayments= $('#noOfPendingPayments').val();
		//alert("noOfPendingPayments  "+noOfPendingPayments); 
		
		var statusSitelevel1 = "";
		var isSelectAnyone = ""; debugger;
		for (i = 1; i <= noOfPendingPayments; i++) { 

             statusSitelevel1 = $('#statusSitelevel'+i).val();
			 
			// alert("statusSitelevel1  "+statusSitelevel1); 
			
			if(statusSitelevel1 !== 'Select' && statusSitelevel1 !== ''){
				isSelectAnyone = "true";
			
				}	
		}
		
       if(isSelectAnyone !== "true"){
			
			alert("Please select atleast one payment request.");
			return false;
		}
       
       var valid_amount_status = validateRequestedAmounts();
 		if(!valid_amount_status){
	    	 return valid_amount_status;				
		}
 		debugger;
  	 	var canISubmit = window.confirm("Do you want to Submit?");	     
	   	if(canISubmit == false) { 
	           return false;
	   	}
       $("#btn-submitt").attr("disabled", true);
         document.getElementById("paymentApprovalform").action = "createAccDeptPaymentTransaction.spring";
     	 document.getElementById("paymentApprovalform").method = "POST";
     	 document.getElementById("paymentApprovalform").submit()
      
	}
	
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
				 //data+="<option value='ALL'>ALL</option>";// this one is extra. to display ALL option. if u dont need this remove this line.
				 
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
	
	/*script for datepickers*/
	$(function() {
		
		$("#datepicker-paymentreq-fromdate").datepicker({
			dateFormat : "dd-M-y",			
			changeMonth: true,
		    changeYear: true,
		    onSelect: function(selected) {
     	        $("#datepicker-paymentreq-todate").datepicker("option","minDate", selected)
     	        }

		      
		});
		$("#datepicker-paymentreq-todate").datepicker({
			dateFormat : "dd-M-y",			
			changeMonth: true,
		    changeYear: true,
		    onSelect: function(selected) {
            	$("#datepicker-paymentreq-fromdate").datepicker("option","maxDate", selected)
            	        }

		});
	});
	
	
	function getVendorId(){
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
	
	function validate() {
		debugger;

		var PaymentDoneFrom=$("#datepicker-paymentreq-fromdate").val();
	    var PaymentDoneTo=$("#datepicker-paymentreq-todate").val();
	    var InvoiceNumber=$("#invoiceNumber").val();
	    var PONumber=$("#poNumber").val();
	    var VendorName=$("#VendorNameId").val();
	    var Site =$("#dropdown_SiteId").val(); 
	    var SelectAll =$("#selectAll").prop("checked"); 
	    
	    $("#hiddenSelectAll").val(SelectAll);
	    //trim method 
	    var PaymentDoneFromdate=PaymentDoneFrom.trim();
	    var PaymentDoneTodate=PaymentDoneTo.trim();
	    var InvoiceNumberId=InvoiceNumber.trim();
	    var VendorNameId=VendorName.trim();
	    var PONumberId=PONumber.trim();
	    var SiteVal=Site.trim();
	   /*  var SelectAllVal=SelectAll.trim();
	    alert(SelectAllVal); */
	  	
		if (!PaymentDoneFromdate && !PaymentDoneTodate && !InvoiceNumberId && !PONumberId && !VendorNameId && !SiteVal && SelectAll==false) {
			alert("Atleast one field is required.");
			return false;
		}
		else{
			$(".loader-sumadhura").show();
			return true;
			
		}

}
	//to distroy loader
	 $(window).load(function () {
		 $(".loader-sumadhuraforsubmit").hide();
	 });

	   /* Reload the site when reached via browsers back button */
		if(!!window.performance && window.performance.navigation.type == 2)
		{
		    window.location.reload();
		}
	   /* Reload the site when reached via browsers back button */
		
	</script>
</body>
</html>
