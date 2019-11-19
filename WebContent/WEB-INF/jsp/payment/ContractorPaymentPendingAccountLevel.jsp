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
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
 
<script src="js/jquery.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>	
<script src="js/sidebar-resp.js" type="text/javascript"></script>	
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<jsp:include page="../CacheClear.jsp" />

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
/* .inward-invoice{
	color: #726969;
   /*  margin-left: 377px; */
/* font-size:24px;


} */
.form-content {
	margin-top: 30px;
}

.viewPaymentsSiteLevel {
	position: absolute;
}

.bottom-class {
	margin-top: 22px;
}
/* .withdata{
    margin-top: 32px;
	border: 1px solid;
    height: 134px;
    width: 544px;
} */
.formShow {
	border: 1px solid #e4e2e2;
	margin-top: 35px;
	/*     width: 1052px; */
	background: #fff;
	/* overflow: scroll;
    background: #ffffff; */
}

.container { /*     	background: rgba(146, 140, 132, 0.2); */
	/*     	padding: 10px; */
	
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
	width: 100%;
}

.form-control1 {
	/* width: 129px; */
	width: 100%;
	background: white;
	/* display: block; */
	/* width: 100%; */
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
	background-color: #fff;
	background-image: none;
	border: 1px solid #868585;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
	-webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow
		ease-in-out .15s;
	-o-transition: border-color ease-in-out .15s, box-shadow ease-in-out
		.15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	height: 32px;
	color: #000;
	font-weight: bold;
}

.view-payment-image {
	height: 20px;
	width: 20px;
	float: right;
	margin-top: 5px;
}

.img-adjust {
	margin-bottom: 50px;
}

.modal-footer {
	text-align: center !important;
}
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
<!-- <script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js">
src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js">
	
</script> -->
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
							<li class="breadcrumb-item active">Payment For Approval</li>
						</ol>
					</div>
					<form id="paymentApprovalform" >
						<%
							int intTotalInvoices = Integer.parseInt(request.getAttribute("listTotalInvoicesSize") == null
									? ""
									: request.getAttribute("listTotalInvoicesSize").toString());
						%>

						<%
							int headerNo = 0;
							List<PaymentBean> listTotalInvoices = (List<PaymentBean>) request.getAttribute("listTotalInvoices");
							for (PaymentBean element : listTotalInvoices) {
								if (element.isContractorHeader()) {
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


			<div id="PaymentsData" class="Mrgtop10">
			<div id="maintable" cellpadding="0" cellspacing="0"
					class="col-md-12 pdzn_tbl1">
					<div id="payements_container">
						<div style="position: relative">
							<span
								class="glyphicon glyphicon-triangle-bottom viewPaymentsSiteLevel"
								style="font-size: 1.2em; margin-top: 14px; margin-left: 5px; z-index: 9999;"></span>
						</div>
						<div class="withoutPricing-class pricing-box container">
							<div
								class="col-md-12 form-horizontal">
								<div class="col-md-3">
									<div class="form-group">
										<label for="email" class="col-md-6 no-padding-label">Status:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<select
												id="selectStatus<%=element.getContractorGroupSerialNo()%>"
												onchange="changeStatus(<%=element.getContractorGroupSerialNo()%>)"
												class="form-control1">
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
										<label for="" class="col-md-6 no-padding-label">Contractor Name:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" class="form-control1" id="VendName"
												data-toggle="tooltip" data-placement="top"
												title="<%=element.getStrContractorName()%>"
												value="<%=element.getStrContractorName()%>" name="VendName"
												readonly="true">
										</div>
									</div>

								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6 no-padding-label">Total Amount:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" id="TotalAmnt" class="form-control1"
												value="<%out.println(element.getStrWorkOrderAmount());%>"
												name="TotalAmnt" readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6 no-padding-label">Requested Amount:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" class="form-control1"
												id="RequestedAmnnt<%=element.getContractorGroupSerialNo()%>"
												value="<%=element.getRequestedAmount()%>"
												name="RequestedAmnt" readonly="true" />
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6 no-padding-label">No of Payments:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" class="form-control1" Id="noofPayments"
												value="<%=element.getIntNoofPaymentsVendorWise()%>"
												name="noofPayments" readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label for="" class="col-md-6 no-padding-label">Site Name:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" class="form-control1" id="SiteName"
												value="<%=element.getStrSiteName()%>" name="SiteName"
												readonly="true">
										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label id="TotalBalance" for="" class="col-md-6 no-padding-label">Total
											Balance Amount:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<input type="text" class="form-control1" id="TotalBalance1"
												value="<%=element.getDoubleBalanceAmount()%>"
												name="SiteName" readonly="true">

										</div>
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<label class="modeofpayment col-md-6 no-padding-label" id="modeofpayment">Mode
											Of Payment:</label>
										<div class="col-md-6 no-padding-right no-padding-left">
											<select id="modeofpayment1<%=element.getContractorGroupSerialNo()%>" class="form-control1 "
												onchange="changeStatusOfModeOfPayment(<%=element.getContractorGroupSerialNo()%>)"
												name="paymentMode<%=element.getIntSerialNo()%>">
												<option value="">--Select--</option>
												<option value="CASH">CASH</option>
												<option value="CHEQUE">CHEQUE</option>
												<option value="ONLINE">ONLINE</option>
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
							<div id="PaymentsShowData" class="Mrgtop20">
								<!-- form -->


								<%
									}

										else {
								%>



								
							<div class="col-md-12">
								<div class="form-horizontal">
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label class="col-md-6">Payment Status:</label> 
											<div class="col-md-6 ">
												<select name="paymentIntiateType<%=element.getIntSerialNo()%>"
													id="statusSitelevel<%=element.getIntSerialNo()%>"
													class="statusSitelevel<%=element.getContractorGroupSerialNo()%> form-control1">
													<option value="">--Select--</option>
													<option value="Approved">Approve</option>
													<option value="Rejected">Reject</option>
												</select>
											</div>
										</div>
									</div>
                                    <div class="col-md-3 Mrg-bottom">
	                                     <div class="form-group" >
											<label for="" class="col-md-6">Payment Id:</label> 
											<div class="col-md-6">
											<%-- <input type="text" class="form-control1"
												name="cntPaymentId<%=element.getIntSerialNo()%>"
												value="<%=element.getIntCntPaymentId()%>" readonly="true"> --%>
												<div><strong><%=element.getIntCntPaymentId()%></strong></div>
											</div>
										</div>
                                    </div>
                                    <div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">Contractor Name:</label> 
											<div class="col-md-6">
												<div style="width:100px;word-break:break-word;"><strong><%=element.getStrContractorName()%></strong></div>
											</div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										  <div class="form-group">
											<label class="col-md-6" for="">Mode Of Payment:</label>
											<div class="col-md-6">
											  <select
												style="border: 1px solid #000;color:#000;"
												class="form-control1 sectionModeOfPayment<%=element.getContractorGroupSerialNo()%>"
												name="paymentMode<%=element.getIntSerialNo()%>">
												<option value="">--SELECT--</option>
												<c:forEach items="${PaymentModes}" var="element1">
													<option value="${element1.value}">${element1.name}</option>
												</c:forEach>
											</select>
											</div>
										  </div>
									</div>
									<div class="clearfix"></div>
									<div class="col-md-3 Mrg-bottom">
										<div class="form-group">
											<label for="" class="col-md-6">WorkOrder Date:</label>
											<div class="col-md-6"> 
												<div><strong><%=element.getStrWorkOrderDate()%></strong></div>
											</div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">WorkOrder No:</label>
											<div class="col-md-6">
												<div style="width: 100px; word-wrap: break-word;"><strong>
												<a class="anchor-invoice"
													href="getMyCompletedWorkOrder.spring?WorkOrderNo=<%=element.getWorkOrderNo()%>&workOrderIssueNo=<%=element.getQsWorkorderIssueId()%>&site_id=<%=element.getStrSiteId()%>&operType=1&isUpdateWOPage=false"
													target="_blank"><%=element.getWorkOrderNo()%></a>
												</strong>
												</div>
											</div>
										</div>
									
									</div>
									<div class="col-md-3 Mrg-bottom">
										<div class="form-group">
											<label for="" class="col-md-6">WorkOrder Amount:</label>
											 <div class="col-md-6"><input style="border: none; color: blue;"type="text" class=""
												value="<%=element.getStrWorkOrderAmount()%>" readonly="true">
											 </div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">UTR No / Cheque No:</label>
											 <div class="col-md-6"><input
												style="color: #2dcc10; border: none;"
												name="utrOrChqNo<%=element.getIntSerialNo()%>" class=""
												autocomplete="off" placeholder="Enter here" value="" />
											</div>
										</div>
									</div>
									<div class="clearfix"></div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group" >
											<label for="" class="col-md-6">Bill Date:</label>
											 <div class="col-md-6">
											    <div><strong><%=element.getStrBillDate()%></strong></div>
											</div>
										</div>
									</div>
                                    <div class="col-md-3 Mrg-bottom">
	                                     <div class="form-group" >
											<label for="" class="col-md-6">Bill Number:	</label> 
											<div class="col-md-6">
											<div class="col-md-10">
												<div><strong>
												<c:if test="<%=element.getPaymentType().equals(\"RA\")%>">
												<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo=<%=element.getStrBillId()%>&WorkOrderNo=<%=element.getWorkOrderNo()%>&billType=<%=element.getPaymentType()%>&site_id=<%=element.getStrSiteId()%>&status=true" target="_blank"><%=element.getStrBillId()%></a>
												</c:if>
												<c:if test="<%=element.getPaymentType().equals(\"ADV\")%>">
												<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo=<%=element.getStrBillId()%>&WorkOrderNo=<%=element.getWorkOrderNo()%>&billType=<%=element.getPaymentType()%>&site_id=<%=element.getStrSiteId()%>&isBillUpdatePage=false&status=true" target="_blank"><%=element.getStrBillId()%></a>
												</c:if>
												<c:if test="<%=element.getPaymentType().equals(\"SEC\")%>"><%=element.getStrBillId()%></c:if>
												<c:if test="<%=element.getPaymentType().equals(\"NMR\")%>">
												<a class="anchor-invoice" href="showWOCompltedBillsDetails.spring?BillNo=<%=element.getStrBillId()%>&WorkOrderNo=<%=element.getWorkOrderNo()%>&billType=<%=element.getPaymentType()%>&site_id=<%=element.getStrSiteId()%>&isBillUpdatePage=false&status=true" target="_blank"><%=element.getStrBillId()%></a>
												</c:if>
												</strong></div>
											</div>
											</div>
										</div>
                                    </div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">Bill Amount: </label>
											 <div class="col-md-6">
											   <input style="border: none; color: blue;" type="text" class="" value="<%=element.getStrBillAmount()%>" readonly="true">
											 </div>
										 </div>
									</div>
									 <div class="col-md-3 Mrg-bottom">	
									   <div class="form-group">
											<label for="" class="col-md-6">Payment Type:</label> 
											<div class="col-md-6">
												<c:if test="<%=element.getPaymentType().equals(\"RA\")%>"><input style=" border: none;" type="text" class="" value="RA BILL" readonly="true" /></c:if>
												<c:if test="<%=element.getPaymentType().equals(\"ADV\")%>"><input style=" border: none;" type="text" class="" value="ADVANCE" readonly="true" /></c:if>
												<c:if test="<%=element.getPaymentType().equals(\"SEC\")%>"><input style=" border: none;" type="text" class="" value="SECURITY DEPOSIT" readonly="true" /></c:if>
												<c:if test="<%=element.getPaymentType().equals(\"NMR\")%>"><input style=" border: none;" type="text" class="" value="NMR" readonly="true" /></c:if>
											</div>
										</div>
									</div>
									<div class="clearfix"></div>
									<div class="col-md-3 Mrg-bottom">
										<div class="form-group">
											<label class="col-md-6"for="">Req Amount:</label>
											<div class="col-md-6"><input style="color: #2dcc10; border: none;"
												type="text"
												class="price<%=element.getContractorGroupSerialNo()%> "
												id="RequestedAmnt<%=element.getContractorGroupSerialNo()%>"
												onblur="calculateSum(<%=element.getContractorGroupSerialNo()%>)"
												value=<%=element.getDoubleAmountToBeReleased()%>
												name="requestAmount<%=element.getIntSerialNo()%>">
											</div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										<div class="form-group" >
											<label for="" class="col-md-6">Deduction Amount </label>
											 <div class="col-md-6"><input
												style="color: blue; border: none;"
												type="text" value="<%=element.getDoubleDeductionAmount()%>"
												class="" readonly="true" />
											 </div>
										 </div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">Balance Amount:</label> 
											<div class="col-md-6"><input
												style="border: none;"
												type="text" class=""
												value="<%=element.getDoubleBalanceAmount()%>" readonly="true">
											</div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6"style=" word-wrap: break-word;">Payment Initiated date:</label>
											<div class="col-md-6">
											  <span style="position: absolute; word-wrap: break-word;"><%=element.getStrPaymentRequestReceivedDate()%></span>
										    </div> 
										</div>
									</div>
									<div class="clearfix"></div>
									<div class="col-md-3 Mrg-bottom">
									<div class="form-group" >
										<label for="" class="col-md-6">Requested Date:</label> 
										<div class="col-md-6">
											<div><strong><%=element.getStrPaymentReqDate()%></strong></div>
										</div>
									</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										<div class="form-group">
												<label for="" class="col-md-6">Comments:</label> 
												<div class="col-md-6"><input
													style="border: none; word-wrap: break-word;"
													type="text" placeholder="Enter here"
													name="comments<%=element.getIntSerialNo()%>"
													data-toggle="tooltip" title="" class="cmnts" id="commentId" />
												</div>
										</div>
									</div>
									<div class="col-md-3 Mrg-bottom">
										 <div class="form-group">
											<label for="" class="col-md-6">Remarks:</label>
											<div class="col-md-6"><input id="remarks"
												style="border: none;width:100px; word-wrap: break-word;"
												 class=""
												readonly="true" data-toggle="tooltip"
											   title="<%=element.getStrRemarksForTitle()%>"
												value="<%=element.getStrRemarksForView()%>" />
											</div>
										</div>
									</div>
									
									
									<div class="clearfix"></div>
									
									 
									
								</div>
							</div>


								
								<input type="hidden" name="cntPaymentId<%=element.getIntSerialNo()%>" value="<%=element.getIntCntPaymentId()%>">
								<input type="hidden" name="paymentType<%=element.getIntSerialNo()%>" value="<%=element.getPaymentType()%>">
								<input type="hidden" name="siteId<%=element.getIntSerialNo()%>" value="<%=element.getStrSiteId()%>">
								<input type="hidden" name="siteName<%=element.getIntSerialNo()%>" value="<%=element.getStrSiteName()%>"> 
								<input type="hidden" name="contractorId<%=element.getIntSerialNo()%>" value="<%=element.getStrContractorId()%>">
								<input type="hidden" name="contractorName<%=element.getIntSerialNo()%>" value="<%=element.getStrContractorName()%>"> 
								<input type="hidden" name="accDeptPmtProcessId<%=element.getIntSerialNo()%>" value="<%=element.getIntAccDeptPaymentProcessId()%>">
								<input type="hidden" name="siteWisePaymentId<%=element.getIntSerialNo()%>" value="<%=element.getIntSiteWisePaymentId()%>"> 
								<input type="hidden" name="requestReceiveFrom<%=element.getIntSerialNo()%>" value="<%=element.getRequestReceiveFrom()%>"> 
								<input type="hidden" name="remarks<%=element.getIntSerialNo()%>" value="<c:out value = "<%=element.getStrRemarks()%>"/>" />
								<input type="hidden" name="workOrderNo<%=element.getIntSerialNo()%>" value="<%=element.getWorkOrderNo()%>">
								<input type="hidden" name="raBillNo<%=element.getIntSerialNo()%>" value="<%=element.getRaBillNo()%>">
								<input type="hidden" name="workOrderAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleWorkOrderAmount()%>">
								<input type="hidden" name="raBillAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleRaBillAmount()%>">
								<input type="hidden" name="billId<%=element.getIntSerialNo()%>" value="<%=element.getStrBillId()%>">
								<input type="hidden" name="tempBillId<%=element.getIntSerialNo()%>" value="<%=element.getStrTempBillId()%>">
								<input type="hidden" name="advBillAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleAdvBillAmount()%>">
								<input type="hidden" name="advBillNo<%=element.getIntSerialNo()%>" value="<%=element.getStrAdvBillNo()%>">
								<input type="hidden" name="sdBillAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleSdBillAmount()%>">
								<input type="hidden" name="sdBillNo<%=element.getIntSerialNo()%>" value="<%=element.getStrSdBillNo()%>">
								<input type="hidden" name="deductionAmount<%=element.getIntSerialNo()%>" value="<%=element.getDoubleDeductionAmount()%>">
								<input type="hidden" name="raBillDate<%=element.getIntSerialNo()%>" value="<%=element.getStrRaBillDate()%>">
								<input type="hidden" name="workOrderDate<%=element.getIntSerialNo()%>" value="<%=element.getStrWorkOrderDate()%>">
								<input type="hidden" name="advBillDate<%=element.getIntSerialNo()%>" value="<%=element.getStrAdvBillDate()%>">
								<input type="hidden" name="sdBillDate<%=element.getIntSerialNo()%>" value="<%=element.getStrSdBillDate()%>">
								<input type="hidden" name="paymentRequestDate<%=element.getIntSerialNo()%>" value="<%=element.getStrPaymentReqDate()%>">
								<input type="hidden" name="paymentRequestReceivedDate<%=element.getIntSerialNo()%>" value="<%=element.getStrPaymentRequestReceivedDate()%>">
								<input type="hidden" name="qsWorkorderIssueId<%=element.getIntSerialNo()%>" value="<%=element.getQsWorkorderIssueId()%>">
								
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



			<c:choose>
			<c:when test="${not empty showGrid}">
				<div class="btn-submit col-md-12 text-center center-block">
					<button class="btn btn-warning  " id="btn-submitt" type="button" onclick="validateformData()" style="margin-top: 21px; margin-bottom: 20px;">SUBMIT</button>
				</div>
			</c:when>
			<c:otherwise>
				<div class="col-md-offset-3 col-md-8 text-center"><font color="red" size="5" >No payments for approval</font></div>
			</c:otherwise>
			</c:choose>
			
			
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
			style="margin-top: 20px;">Cancel</button>
	</div>
	<!-- iframe end -->
	</div>

		<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/CovertDCTOInvoice.js" type="text/javascript"></script>

	<script>
		$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
										});

						$(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
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
		console.log(rowId);
		
		 var sum = 0;
		    $('.price'+rowId).each(function() {
		        sum += Number($(this).val());
		    });
	$("#RequestedAmnnt"+rowId).val(sum);
	};
/* ************Method for changing the dropdown values************** */

function changeStatus(rowNum){debugger;
/* $('#selectStatus'+rowNum).on('change', function(){ */
$(".statusSitelevel"+rowNum).html('');

var Status= $("#selectStatus"+ rowNum).val();

/*     if($("#selectStatus"+rowNum).val()=="15$Approve"){
        $('.statusSitelevel'+rowNum).append('<option value="Approved">Approved</option>'+'<option value="Rejected">Rejected</option>');

    }else{
        $('.statusSitelevel'+rowNum).append('<option value="Rejected">Rejected</option>'+'<option value="Approved">Approved</option>');

    } */


if(Status == "15$Approve"){
	 $('.statusSitelevel'+rowNum).append("<option value=''>--Select--</option>"+"<option value='Approved'  selected='selected'>Approve</option>"+"<option value='Rejected'>Reject</option>");

}if  (Status == "$Rejected" ){
	 $('.statusSitelevel'+rowNum).append("<option value=''>--Select--</option>"+"<option value='Approved'>Approve</option>"+"<option value='Rejected'  selected='selected'>Reject</option>");
}if(Status == "$Select"){
	$('.statusSitelevel'+rowNum).append("<option value=''  selected='selected'>--Select--</option>"+"<option value='Approved'>Approve</option>"+"<option value='Rejected'>Reject</option>");
}
    
    
    

}
/* ************Method for changing the Mode of Payment************** */

function changeStatusOfModeOfPayment(rowNum){debugger;
/* $('#selectStatus'+rowNum).on('change', function(){ */

$('.sectionModeOfPayment'+ rowNum).html('');

var ModeOfPay= $('#modeofpayment1'+rowNum).val();

    if( ModeOfPay == "CASH"){
        $('.sectionModeOfPayment'+ rowNum).append("<option value='' >--Select--</option>"+"<option value='CASH' selected='selected'>CASH</option>"+"<option value='CHEQUE' >CHEQUE</option>"+"<option value='ONLINE'>ONLINE</option>");

    }if  (ModeOfPay == "ONLINE" ){
        $('.sectionModeOfPayment'+rowNum).append("<option value='' >--Select--</option>"+"<option value='CASH'>CASH</option>"+"<option value='CHEQUE' >CHEQUE</option>"+"<option value='ONLINE' selected='selected'>ONLINE</option>");
	}if(ModeOfPay == "CHEQUE" ){
    $('.sectionModeOfPayment'+rowNum).append("<option value='' >--Select--</option>"+"<option value='CASH'>CASH</option>"+"<option value='CHEQUE'  selected='selected'>CHEQUE</option>"+"<option value='ONLINE'>ONLINE</option>");
    }if(ModeOfPay == ""){
    $('.sectionModeOfPayment'+rowNum).append("<option value='' selected='selected' >--Select--</option>"+"<option value='CASH'>CASH</option>"+"<option value='CHEQUE' >CHEQUE</option>"+"<option value='ONLINE'>ONLINE</option>");
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
			
			alert("You are not approving or rejecting any payment request");
			return false;
		}
		
       var canISubmit = window.confirm("Do you want to Submit?");	     
	   	if(canISubmit == false) { 
	           return false;
	   	}
      $("#btn-submitt").attr("disabled", true);
        document.getElementById("paymentApprovalform").action = "createCntAccDeptPaymentTransaction.spring";
    	 document.getElementById("paymentApprovalform").method = "POST";
    	 document.getElementById("paymentApprovalform").submit()
      
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
