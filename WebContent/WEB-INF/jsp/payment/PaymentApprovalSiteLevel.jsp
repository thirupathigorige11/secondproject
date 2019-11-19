<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page isELIgnored="false"%>
<%@page import="java.util.List,com.sumadhura.bean.PaymentBean"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">

<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<script src="js/jquery.min.js"></script>

<script src="js/jquery-ui.js" type="text/javascript"></script>		
<jsp:include page="../CacheClear.jsp" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.container1 {
	display: none;
}

.media-style {
	width: 39% !important;
}

@media screen and (min-width: 480px) {
	.media-style {
		width: auto;
	}
	.submitstyle {
		margin-top: 20px;
		width: 100% !important;
	}
}

.abc {
	color: red;
}

.btn-ward {
	height: 29px;
	width: 121px;
	color: white;
	background-color: #ef7e2d;
	position: absolute;
	margin-left: 465px;
	margin-top: 48px;
}

.form-content {
	margin-top: 30px;
}

.bottom-class {
	margin-top: 22px;
}

.formShow {
	border: 1px solid #e4e2e2;
	margin-top: 43px;
	background: #fff;
	
}

.DCNumber {
	color: black;
	font-weight: bold;
}

hr {
	display: block;
	margin-top: 0.5em;
	margin-bottom: 0.5em;
	margin-left: auto;
	margin-right: auto;
	border-style: inset;
	border-width: 2px;
}

.full-width {
	width: 100%;
}

.icons-bg {
	padding: 3px 10px;
}

.pricing-box {
	background: rgba(33, 32, 31, 0.2);
	display: flow-root;
	padding: 10px;
	border: solid 1px white;
	width: 1250px;
}
.form-control{border:1px solid #000 !important;}


.img-adjust {
	margin-bottom: 50px;
}

.modal-footer {
	text-align: center !important;
}

.no-padding-left {
	padding-left: 0px;
}

.no-padding-right {
	padding-right: 0px;
}
/*changes for responsive*/
@media only screen and (min-width:320px) and (max-width:1023px){
/* .nav_menu{
height:202px !important;
} */
.anchor-invoice {
    position: relative;
    }
    .toggle {
    float: right;
    margin: 0;
    padding-top: 13px;
    padding-bottom: 12px;
    margin-right: 10px;
}

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
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">Payment Approval Site Level</li>
						</ol>
					</div>
					<form id="paymentApprovalform" action="" method="POST">
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
	</div>
	</div>

	<%
		}
	%>
	<div class="paymentInvoice">

		<div class="container center-block" align="">
			<div id="PaymentsData">
				<!-- form -->
				<form class="form-horizontal">
					<div id="maintable" cellpadding="0" cellspacing="0"
						class="pdzn_tbl1">
						<div id="payements_container">
							<div class="withoutPricing-class pricing-box container" style="width: 100%;">
								<div class="col-md-1">
									<span class="glyphicon glyphicon-triangle-bottom viewPaymentsSiteLevel col-md-1" style="font-size: 1.2em; margin-top: 14px; display: inline-block; margin-right: 19px;"></span>
								</div>
								<div class="col-md-11 no-padding-left no-padding-right">
									<div class="col-md-3">
										<div class="form-group">
											<label for="email" class="col-md-6 no-padding-left" style="margin-top:5px;">Status :</label>
											<div class="col-md-6 no-padding-left no-padding-right">
												<select id="selectStatus<%=element.getVendorGroupSerialNo()%>" onchange="changeStatus(<%=element.getVendorGroupSerialNo()%>)" class="form-control">
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
											<label for="" class="col-md-6 no-padding-left" style="margin-top:5px;">Vendor Name :</label>
											<div class="col-md-6 no-padding-left no-padding-right">
												<input type="text" class="form-control" id="VendName" value="<%=element.getStrVendorName()%>" title="<%=element.getStrVendorName()%>" name="VendName" readonly="true">
											</div>
										</div>
									</div>
									<div class="col-md-3 ">
										<div class="form-group">
											<label for="" class="col-md-6 no-padding-left" style="margin-top:5px;">Total Amount :</label>
											<div class="col-md-6 no-padding-left no-padding-right">
												<input type="text" id="TotalAmnt" class="form-control" value="<%out.println(element.getDoubleInvoiceAmount() == 0.0? element.getPoAmount_WithCommas(): element.getInvoiceAmount_WithCommas());%>" name="TotalAmnt" readonly="true">
											</div>
										</div>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<label for="" class="col-md-6 no-padding-left">Requested Amount :</label>
											<div class="col-md-6 no-padding-left no-padding-right">
												<input type="text" class="form-control" id="RequestedAmnnt<%=element.getVendorGroupSerialNo()%>" value="<%=element.getRequestedAmount_WithCommas()%>" name="RequestedAmnt" readonly="true">
											</div>
										</div>
									</div>
								</div>

								<input type="hidden" name="numbeOfRowsToBeProcessed" value="1" id="rowId">
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
														<label class="col-md-6">Payment Status:</label>
														<div class="col-md-6">
															<select name="paymentIntiateType<%=element.getIntSerialNo()%>"	id="statusSitelevel<%=element.getIntSerialNo()%>" class="statusSitelevel<%=element.getVendorGroupSerialNo()%> form-control">
																<option value="">--Select--</option>
																<option value="Approved">Approve</option>
																<option value="Rejected">Reject</option>
															</select>
														</div>
													</div>
												</div>
												<div class="col-md-3 Mrg-bottom">
													<div class="form-group">
														<label for="" class="col-md-6">Payment&nbsp;Details&nbsp;ID:</label>
														<div class="col-md-6">
															<input type="text" class="form-control" name="paymentDetailsId<%=element.getIntSerialNo()%>" value="<%=element.getIntPaymentDetailsId()%>"	readonly="true">
														</div>
													</div>
												</div>
												<div class="col-md-3 Mrg-bottom">
												 <div class="form-group">
													<label for="" class="col-md-6">Invoice Date:</label>
													<div class="col-md-6">
														<input type="text" class="form-control" value="<%=element.getStrInvoiceDate()%>" readonly="true">
													</div>
												</div>
												</div>
												<div class="col-md-3 Mrg-bottom">
												 <div class="form-group" >
													<label for="" class="col-md-6">Invoice No:</label> 
														<div class="col-md-6">
														 <a class="anchor-invoice" style="" href="${INVOICEPATH}invoiceNumber=<%=element.getStrInvoiceNo()%>&vendorName=<%=element.getStrVendorName()%>&vendorId=<%=element.getStrVendorId()%>&IndentEntryId=<%=element.getIntIndentEntryId()%>&SiteId=<%=element.getStrSiteId()%>&invoiceDate=<%=element.getStrInvoiceDate()%>" target="_blank"><%=element.getStrInvoiceNo()%></a>
													<%-- <% if(element.isHasImage()){%>
							      <span><img src="images/logo.png" style="width:30px;height:30px;float:right;border: 1px solid #000;" alt="img"  href="#" class="ponumber-anchor" onclick="SetPage('<%=url%>/Sumadhura/getInvoiceDetails.spring?invoiceNumber=<%=element.getStrInvoiceNo()%>&vendorName=<%=element.getStrVendorName()%>&vendorId=<%=element.getStrVendorId()%>&IndentEntryId=<%=element.getIntIndentEntryId()%>&SiteId=<%=element.getStrSiteId()%>&invoiceDate=<%=element.getStrInvoiceDate()%>&imageClick=true')" /></span>
							 	  <%} %>  --%>

													<%
														if (element.isHasImage()) {
													%>
													<span style="float: right;"
														data-toggle="modal" href="#" class="ponumber-anchor" onclick="SetPage('${INVOICEPATH}invoiceNumber=<%=element.getStrInvoiceNo()%>&vendorName=<%=element.getStrVendorName()%>&vendorId=<%=element.getStrVendorId()%>&IndentEntryId=<%=element.getIntIndentEntryId()%>&SiteId=<%=element.getStrSiteId()%>&invoiceDate=<%=element.getStrInvoiceDate()%>&imageClick=true')"><i class="fa fa-file-image-o check-image"></i></span>
													<%-- <span><img class="img-responsive custom-img <%=element.getIntSerialNo()%> " src="data:image/jpeg;base64,<%=element.getInvoiceImage0()%>" data-toggle="modal" data-target="#custom-img-voice<%=element.getIntSerialNo()%>"/></span>
							  --%>
													<%
														}
													%>
														</div>
												</div>
												</div>
											</div>


										</div>
										<div class="col-md-12">
										<div class="form-horizontal">
										<div class="col-md-3 Mrg-bottom">
											<div class="form-group ">
												<label for="" style="margin-top: 4px;" class="col-md-6">Payable Invoice Amount:</label>
												<div class="col-md-6 ">
												   <input style="color: blue" type="text" class="form-control" name="invoiceAmt<%=element.getIntSerialNo()%>" value="<%=element.getInvoiceAmount_WithCommas()%>" readonly="true" >
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group" >
												<label for=""  style="margin-top: 4px;" class="col-md-6">Credit Note No:</label> 
												<div class="col-md-6 ">
													<input style="color: blue; text-decoration: underline;margin-top: 5px;" type="text" class="form-control" value="<%=element.getStrCreditNoteNumber()%>">
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" class="col-md-6" style="margin-top: 5px;">Credit Amount:</label>
												<div class="col-md-6">
													  <input style="color: blue" type="text" class="form-control" value="<%=element.getCreditTotalAmount_WithCommas()%>" readonly="true">
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Received Date:</label> 
												<div class="col-md-6">
													<input type="text" class="form-control"	value="<%=element.getStrReceiveDate()%>" readonly="true">
												</div>
											</div>
											</div>
										</div>
                                       </div>

										<div class="col-md-12">
										 <div class="form-horizontal">
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group" >
												<label for="" class="col-md-6" style="margin-top: 5px;">PO Date:</label> 
												<div class="col-md-6 ">
													 <input  type="text" class="form-control" value="<%=element.getStrPODate()%>" readonly="true">
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" class="col-md-6" style="margin-top: 5px;">PO Number:</label>
												<div class="col-md-6">
												  <a style="text-decoration: underline; font-weight: 900; color: blue; width: 114px; word-wrap: break-word;" href="#" class="ponumber-anchor"  onclick="SetPage('${POPATH}poNumber=<%=element.getStrPONo()%>&siteId=<%=element.getStrSiteId()%>&imageClick=true') " style="text-decoration: underline;color: blue;display: inline-block;width: 100px;word-wrap: break-word"><%=element.getStrPONo()%></a>
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" class="col-md-6" style="margin-top: 5px;">PO Amount:</label> 
												<div class="col-md-6">
												  <input style="color: blue;" type="text" class="form-control" value="<%=element.getPoAmount_WithCommas()%>"readonly="true">
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for=""  class="col-md-6" style="margin-top: 5px;">Vendor Name:</label>
												<div class="col-md-6">
													 <input  type="text" class="form-control" value="<%=element.getStrVendorName()%>" title="<%=element.getStrVendorName()%>" readonly=true>
												</div>
											</div>
											</div>
											</div>
										</div>
										<div class="col-md-12">
										<div class="form-horizontal">
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Payment Against:</label> 
												<div class="col-md-6">
												   <input type="text" class="form-control" value="<%=element.getPaymentType().equals("DIRECT")?"Invoice":"PO"%>" readonly="true" />
												</div>	
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Comments:</label>
												<div class="col-md-6 ">
													<input type="text" 	name="comment<%=element.getIntSerialNo()%>" data-toggle="tooltip" class="form-control" id="commentId" />
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Req Amount:</label> 
												<div class="col-md-6">
													<input style="color: #2dcc10;" type="text" class="price<%=element.getVendorGroupSerialNo()%> form-control"	id="RequestedAmnt<%=element.getVendorGroupSerialNo()%>"	onkeypress="return isNumberCheck(this, event)"  onblur="calculateSum(<%=element.getVendorGroupSerialNo()%>)"  value=<%=element.getRequestedAmount()%> name="RequestedAmt<%=element.getIntSerialNo()%>">
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom"><div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Requested Date:</label> 
													<div class="col-md-6 input-group">
													<input id="RequestedDate<%=element.getIntSerialNo()%>" class="form-control" onclick="datePicker(<%=element.getIntSerialNo()%>);" name="RequestedDate<%=element.getIntSerialNo()%>" data-toggle="tooltip" title="<%=element.getRequestedDate()%>" value="<%=element.getRequestedDate()%>" />
													<label class="input-group-addon btn RequestedDate<%=element.getIntSerialNo()%>" for="RequestedDate<%=element.getIntSerialNo()%>">
												       <span class="fa fa-calendar"></span>
												    </label>
													
													</div>
											</div></div>
											</div>
										</div>


										<div class="col-md-12">
										  <div class="form-horizontal">
										   <div class="col-md-3 Mrg-bottom">
											<div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Adjusted Amount from Advance:</label> 
												<div class="col-md-6">
													<input style="color: #2dcc10;" name="AdjustAmountFromAdvance<%=element.getIntSerialNo()%>" id="AdjustAmountFromAdvance<%=element.getIntSerialNo()%>"	class="form-control" onkeypress="return isNumberCheck(this, event)" onfocusout="checkingNumberOnPaste(this.id)" data-toggle="tooltip"	title="${element.doubleAdjustAmountFromAdvance}" value="<%=element.getDoubleAdjustAmountFromAdvance()%>" />
												</div>
											</div>
										   </div>
											<div class="col-md-3 Mrg-bottom"><div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">PO Advance:</label>
												<div class="col-md-6 ">
												  <input style="color: blue;" type="text" class="form-control" value="<%=element.getPoAdvancePayment_WithCommas()%>" readonly="true" style="margin-left: 10px;">
												 </div>
											</div></div>
											<div class="col-md-3 Mrg-bottom">
											 <div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Remarks:</label>
												<div class="col-md-6">
												  <input id="remarks" name="remarksForView<%=element.getIntSerialNo()%>" class="form-control" readonly="true" data-toggle="tooltip" title="<%=element.getStrRemarksForView()%>"	value="<%=element.getStrRemarksForView()%>" />
												</div>
											</div>
											</div>
											<div class="col-md-3 Mrg-bottom">
											<div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Security Deposit:</label>
												<div class="col-md-6">
													  <input style="color: blue;" type="text" value="<%=element.getSecurityDeposit_WithCommas()%>" class="form-control" readonly="true">
												</div>
											</div></div>
											</div>
										</div>

										<div class="col-md-12">
										<div class="form-horizontal">
											<div class="col-md-3 Mrg-bottom">
											<div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Paid Amount:</label>
												<div class="col-md-6 ">
												    <input style="color: blue;" type="text" class="form-control" value="<%=element.getPaidAmount_WithCommas()%>" readonly="true" style="margin-left: 10px;">
												</div>
											</div>
											</div>
											
											<div class="col-md-3 Mrg-bottom">
											<div class="form-group">
												<label for="" style="margin-top: 5px;" class="col-md-6">Total Invoice Amount:</label>
												<div class="col-md-6 ">
												    <input style="color: blue;" type="text" class="form-control" value="<%=element.getVendorInvoiceTotalAmount_WithCommas()%>" readonly="true" style="margin-left: 10px;">
												</div>
											</div>
											</div>
											</div>
											</div>

										<input type="hidden" name="invoiceDate<%=element.getIntSerialNo()%>" value="<%=element.getStrInvoiceDate()%>">
										<input type="hidden" name="invoiceNo<%=element.getIntSerialNo()%>"	value="<%=element.getStrInvoiceNo()%>">
										<%-- <input type="hidden" name = "paymentDetailsId<%=element.getIntSerialNo()%>"  value = "<%=element.getIntPaymentDetailsId()%>"> --%>
										<input type="hidden" name="PaymentId<%=element.getIntSerialNo()%>"	value="<%=element.getIntPaymentId()%>"> 
										<input type="hidden" id="actualRequestedAmt<%=element.getIntSerialNo()%>" class="form-control"	name="actualRequestedAmt<%=element.getIntSerialNo()%>"	value="<%=element.getRequestedAmount()%>" />
										<input type="hidden" id="RequestedDate" class="form-control reqDate" name="actualRequestedDate<%=element.getIntSerialNo()%>" value="<%=element.getRequestedDate()%>" /> 
										<input type="hidden" id="siteId" class="form-control reqDate" name="siteId<%=element.getIntSerialNo()%>" value="<%=element.getStrSiteId()%>" /> 
										<input type="hidden" name="receivedDate$<%=element.getIntSerialNo()%>" value="<%=element.getStrReceiveDate()%>" /> 
										<input type="hidden" name="remarks<%=element.getIntSerialNo()%>" value="<%=element.getStrRemarks()%>" /> 
										<input type="hidden" name="actualAdjustAmountFromAdvance<%=element.getIntSerialNo()%>" value="<%=element.getDoubleAdjustAmountFromAdvance()%>" /> 
										<input type="hidden" name="paymentType<%=element.getIntSerialNo()%>" value="<%=element.getPaymentType()%>">
										<input type="hidden" name="invoiceNo$<%=element.getIntSerialNo()%>" value="<%=element.getStrInvoiceNo()%>"> 
										<input type="hidden" name="poNo<%=element.getIntSerialNo()%>" value="<%=element.getStrPONo()%>"> 
										<input type="hidden" name="invoiceAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleInvoiceAmount()%>"> 
										<input type="hidden" name="poAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoublePOTotalAmount()%>"> 
										<input type="hidden" name="paymentDoneUpto<%=element.getIntSerialNo()%>" id="paymentDoneUpto<%=element.getIntSerialNo()%>" value="<%=element.getDoublePaymentDoneUpto()%>">
									    <input type="hidden" name="paymentReqUpto<%=element.getIntSerialNo()%>" id="paymentReqUpto<%=element.getIntSerialNo()%>"	value="<%=element.getDoublePaymentRequestedUpto()%>">
										<input type="hidden" name="remarks<%=element.getIntSerialNo()%>" value="<%=element.getStrRemarks()%>" /> 
										<input type="hidden" name="vendorName<%=element.getIntSerialNo()%>" value="<%=element.getStrVendorName()%>" />
										<input type="hidden" name="indentEntryId<%=element.getIntSerialNo()%>" value="<%=element.getIntIndentEntryId()%>" />
										<input type="hidden" name = "remainingPOAdvance<%=element.getIntSerialNo()%>"  value = "<%=element.getDoublePOAdvancePayment()%>">
										<input type="hidden" name = "payBalanceInPo<%=element.getIntSerialNo()%>"  value = "<%=element.getPayBalanceInPo()%>">
										<input type="hidden" name = "paymentDoneOnMultipleInvoices<%=element.getIntSerialNo()%>"  value = "<%=element.getPaymentDoneOnMultipleInvoices()%>">
										<input type="hidden" name = "adjustedAmountFromPo<%=element.getIntSerialNo()%>"  value = "<%=element.getAdjustedAmountFromPo()%>">
										<!-- code for modal popup start -->
										<div id="custom-img-voice<%=element.getIntSerialNo()%>"
											class="modal fade custmodal " role="dialog">
											<div class="modal-dialog custom-modal-lg">
												<div class="modal-content">
													<div class="modal-header cust-modal-header">
														<button type="button" class="close" data-dismiss="modal">&times;</button>
														<h4 class="modal-title text-center">Payment For Approval</h4>
													</div>
													<div class="modal-body cust-modal-body">
														<img src="data:image/jpeg;base64,<%=element.getInvoiceImage0()%>"  class="img-responsive img-adjust <%=element.getIntSerialNo()%> center-block" />
														<img src="data:image/jpeg;base64,<%=element.getInvoiceImage1()%>" class="img-responsive img-adjust <%=element.getIntSerialNo()%> center-block" />
														<img src="data:image/jpeg;base64,<%=element.getInvoiceImage2()%>" class="img-responsive img-adjust <%=element.getIntSerialNo()%> center-block" />
														<img src="data:image/jpeg;base64,<%=element.getInvoiceImage3()%>" class="img-responsive img-adjust <%=element.getIntSerialNo()%> center-block" />
													</div>
													<div class="modal-footer cust-modal-footer">
														<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
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
									<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows"> <input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
									<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId"> 
									<input type="hidden" name="VendorId" value="" id="vendorIdId"> 
									<input type="hidden" name="listTotalInvoices" id="listTotalInvoices" value="<%=intTotalInvoices%>">
								</div>
							</div>
						</div>
					</div>
				</form>
				<c:choose>
				<c:when test="${not empty showGrid}">
					<div class="btn-submit col-md-12 text-center center-block">
						<button class="btn btn-warning" id="btn-submitt" type="button" style="margin-top: 21px; margin-bottom: 20px;" onclick="validateformData()">SUBMIT</button>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col-md-offset-3 col-md-8 text-center"><font color="red" size="5" >No payments for approval</font></div>
				</c:otherwise>
				</c:choose>
				
			</form>
		</div>
	</div>
	</div>
	</div>
	<!-- iframe start -->
	<div id="cancel-iframe" style="display: none; position: relative;">
		<!-- /*css loader here*/ -->
		<div class="loader-sumadhura" >
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
		<button id="cancel-btn" class="btn btn-warning text-center center-block Mrgtop20">Close</button>
	</div>
	<!-- iframe end -->

		<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/numberCheck.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/CovertDCTOInvoice.js" type="text/javascript"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>

	<script>
		$(document).ready(function() {
			$(".up_down").click(function() {
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
	
	function calculateSum(rowId){debugger;
		var reqAmnt=$("#RequestedAmnt"+rowId).val();
		if(isNaN(reqAmnt)){
			alert("Please enter valid data.");
			$("#RequestedAmnt"+rowId).val("");
			$("#RequestedAmnt"+rowId).focus();
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
	   /*  $('#loadingMessage').show(); */
	    
	
	

}
$('#myframe').load(function () {
    $('.loader-sumadhura').hide();
  /*   $('#loadingMessage').hide(); */
    
});


</script>
	<script>
	
	$("#cancel-btn").click(function(){	
	$("#cancel-iframe").hide();
	
	$(".hide-show-iframe").show();
	var z= document.getElementById("myframe").setAttribute("src", ""); 
	
	 
	
		});
	
	$(".requsted-date-datepicker").datepicker({
		changeMonth: true,
	      changeYear: true,
		dateFormat : "dd/mm/yy"
	
	}); 
	/*validation*/
	//validateRequestedAmounts() - Rafi
	function validateRequestedAmounts() {debugger;
		
		var noOfRecords= $('#listTotalInvoices').val();	
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
			RequestedAmt = $('input[name="RequestedAmt'+i+'"]').val();
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
		          		$('input[name="RequestedAmt'+i+'"]').focus();
		          		return false;
					}
				}
				if(paymentType == 'ADVANCE'){
						if(+payingAmt > +poAmount){
		          		alert("Total of Intiated Amount & Paid Amount is greater than PO Amount for Payment Details ID: "+paymentDetailsId);
		          		$('input[name="RequestedAmt'+i+'"]').focus();
		          		return false;
					}
				}
				if((+AdjustAmountFromAdvance - +actualAdjustAmountFromAdvance) > +remainingPOAdvance){
					alert("Adjust Amount is greater than Advance for Payment Details ID: "+paymentDetailsId);
					$('input[name="AdjustAmountFromAdvance'+i+'"]').focus();
	          		return false;
				}
				if(paymentType == 'DIRECT'){
				  if(payBalanceInPo != 'NO_ADVANCE'){
				 	if(+RequestedAmt > +payBalanceInPo){
						alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
						$('input[name="AmountToBeReleased'+i+'"]').focus();
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
		
		var listTotalInvoices= $('#listTotalInvoices').val();
		//alert("noOfPendingPayments  "+noOfPendingPayments); 
		
		var statusSitelevel1 = "";
		var isSelectAnyone = ""; debugger;
		for (i = 1; i <= listTotalInvoices; i++) { 

			statusSitelevel1 = $('#statusSitelevel'+i).val();
            /*  alert("statusSitelevel1  "+statusSitelevel1);  */
			
			if(statusSitelevel1 != 'Select' && statusSitelevel1 != '' && statusSitelevel1 != undefined){
				isSelectAnyone = "true";
			}	
		}
		
       if(isSelectAnyone != "true"){
			
			alert("Please select atleast one payment request");
			return false;
		}
		
       var valid_amount_status = validateRequestedAmounts();
  		if(!valid_amount_status){
	    	 return valid_amount_status;				
		}
  		
       $("#btn-submitt").attr("disabled", true);
       document.getElementById("paymentApprovalform").action = "savePaymentApprovalDetails.spring";
   	 document.getElementById("paymentApprovalform").method = "POST";
   	 document.getElementById("paymentApprovalform").submit();
      
	}
	/*validation*/
	
	/*code for datepicker calendar*/
	function datePicker(dateid){		
		$("#RequestedDate"+dateid).datepicker();
		$("#RequestedDate"+dateid).focus();		
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
