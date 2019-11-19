<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>


<html  oncontextmenu="return false">
<head>  

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
		<jsp:include page="../CacheClear.jsp" />  
	 <style>
		@media print{
			.btn-warning, .breadcrumb, .left_col, .nav_menu{
				display:none;
			}
			#taxInvoiceTable{
				margin-top:-100px !important;
			}
		}
    </style>
</head>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
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
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Create PO</li>
					</ol>
				</div>
				<div>
					<div class="col-md-12">
						<table style="border:1px solid #000;width:100%;" id="taxInvoiceTable">
							<tbody>
								<tr>
									<td colspan="3" style="text-align:center;font-size: 20px;font-weight: bold;padding: 30px;">TAX INVOICE</td>
								</tr>
								<tr>
									<td colspan="2" style="width:50%;">
										<span style="font-size:14px;font-weight:bold;">Contractor Details</span><br>
										<span style="width:28%;float:left;">Address  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.contractorAddress }</span><br>
										<span style="width:28%;float:left;">GSTIN   </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.GSTIN }</span><br>
										<span style="width:28%;float:left;">State Name  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.contractorStateName }</span><br>
										<!-- <span style="width:28%;float:left;">Code  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">5000000</span><br> -->
									</td>
									<td rowspan="3" style="width:50%;">
									<div style="position: absolute;top: 0px;width: 100%;">
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">Project  </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;">${billBean.siteName}</span><br>
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">Type of Work   </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;"><c:choose><c:when test="${billBean.paymentType eq 'NMR'}">Labor Supply</c:when><c:otherwise>Piece Work</c:otherwise> </c:choose></span><br>
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">Work Order No  </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;">${billBean.workOrderNo}</span><br>
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">Invoice Amount  </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;">${billBean.totalCurrentCerti}</span><br>
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">WO Amount   </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;">${billBean.totalAmount}</span><br>
										<span style="width:30%;float:left;    padding: 5px 0px 5px 0px;">Invoice Date   </span><span style="width:2%;float:left;    padding: 5px 0px 5px 0px;">:  </span><span style="width:68%;float:left;    padding: 5px 0px 5px 0px;">${billBean.paymentReqDate}</span><br>
									</div>
									</td>
								</tr>
								<tr>
								
								<c:forEach items="${PlaceOfSupplySiteDetails }" var="siteDetails">
									<td colspan="2" >
										<span style="font-size:14px;font-weight:bold;">Place of Supply</span><br>
										<span style="width:28%;float:left;">Project Name  </span><span style="width:2%;float:left;">:</span>${billBean.siteName}<span style="width:70%;float:left;"></span><br>
										<span style="width:28%;float:left;">Address  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${siteDetails.ADDRESS }</span><br>
										<span style="width:28%;float:left;">GSTIN   </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${siteDetails.GSIN_NUMBER }</span><br>
										<span style="width:28%;float:left;">State Name  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${siteDetails.STATE }</span><br>
										<span style="width:28%;float:left;">Code  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${siteDetails.STATE_CODE}</span><br>
									</td>
									<!-- <td rowspan="1" style="width:50%;border-bottom: 1px solid #fff;"></td> -->
									</c:forEach>
								</tr>
								<tr>
									<td colspan="2">
										<span style="font-size:14px;font-weight:bold;">Billing Address</span><br>
										<span style="width:100%;float:left;">${BillingSiteAddress['billingSiteName']}</span><br>
										<span style="width:28%;float:left;">Address  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;"> ${BillingSiteAddress['billingAddress']}</span><br>
										<span style="width:28%;float:left;">GSTIN   </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;"> ${BillingSiteAddress['GSTIN']}</span><br>
										<span style="width:28%;float:left;">State Name  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${BillingSiteAddress['billingState']}</span><br>
										<span style="width:28%;float:left;">Code  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${BillingSiteAddress['billingStateCode']}</span><br>
									</td>
									<!-- <td rowspan="1" style="width:50%;"></td> -->
								</tr>
								<tr>
									<td style="width:5%;text-align:center;font-size: 16px;font-weight: bold;">S.No</td>
									<td style="text-align:center;font-size: 16px;font-weight: bold;">Description</td>
									<td style="text-align:center;font-size: 16px;font-weight: bold;">Amount</td>
								</tr>
								<tr>
									<td style="text-align:center;">1</td>
									<td style="text-align:center;">Towards Work Billing Amount</td>
									<td style="text-align:center;">${billBean.billingAmount}</td>
								</tr>
								
								<tr>
									<td colspan="3" style="padding:15px"></td>
								</tr>
								<tr>
									<td colspan="2" style="text-align:center;">CGST@${billBean.CGSTPercentage }%</td>
									<td style="text-align:center;">
										<input type="text" style="border:none;text-align:center;" value="${billBean.CGSTAmount}" readonly>
									
									</td>
								</tr>
								<tr>
									<td colspan="2" style="text-align:center;">SGST@${billBean.SGSTPercentage }%</td>
									<td style="text-align:center;">
									<input type="text" style="border:none;text-align:center;" value="${billBean.SGSTAmount}" readonly>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="text-align:center;">IGST@${billBean.gstPercentage }</td>
									<td style="text-align:center;">
									<input type="text" style="border:none;text-align:center;" value="${billBean.IGSTAmount}" readonly>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="text-align:center;">TDS@${billBean.tdsPercentage }%</td>
									<td style="text-align:center;">
									<input type="text" style="border:none;text-align:center;" value="${billBean.TDSAmount}" readonly>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="padding:15px"></td>
								</tr>
								<tr>
									<td colspan="2" style="text-align:center;font-size: 16px;font-weight: bold;">Total</td>
									<td style="text-align:center;font-size: 16px;font-weight: bold;">
									<input type="text" style="border:none;text-align:center;" value="${(billBean.billingAmount-billBean.TDSAmount)+billBean.IGSTAmount+billBean.SGSTAmount+billBean.CGSTAmount}" readonly>
										<%-- ${(billBean.billingAmount-billBean.TDSAmount)+billBean.IGSTAmount+billBean.SGSTAmount+billBean.CGSTAmount} --%>
									</td>
								</tr>
								<tr>
									<td colspan="2" style="width:50%;">
										<span style="width:28%;float:left;">Pan Card No  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.contractorPanNo}</span><br>
										<span style="width:28%;float:left;">Account No  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.contractorBankAccNumber}</span><br>
										<span style="width:28%;float:left;">IFSC & Bank Name  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">
				
										 <c:set value="-" var="delimiter"></c:set>
              				              <c:choose>
							              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
							              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
							              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
							              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
							              </c:choose>
										
										
										</span><br>
										<span style="width:28%;float:left;">Mobile No  </span><span style="width:2%;float:left;">:  </span><span style="width:70%;float:left;">${billBean.contractorPhoneNo}</span>
									</td>
									<td style="width:50%;text-align:center;">
										<span>${billBean.contractorName}</span><br>
										<span style="position: absolute;bottom: 0px;left: 40%;">Authorised Signatory</span>
									</td>
								</tr>
								<tr>
									<td colspan="3" style="text-align:center;">This is Computer a Genereted Invoice</td>
								</tr>				
							</tbody>
						</table>
						<div class="col-md-12 text-center center-block" style="margin:30px 0px 30px 0px;">
       					   <button type="button" class="btn btn-warning" id="createPoSubmit" onclick="printPage()">Print</button>
    				   	</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/marketing/jquery.timepicker.min.js"></script>       
    <script src="js/marketing/marketing_CreatePO.js" type="text/javascript"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script> 
	<script>
		function printPage(){
			window.print();
		}
/* 		$(document).keydown(function (event) {
		    if (event.keyCode == 123) { // Prevent F12
		        return false;
		    } else if (event.ctrlKey && event.shiftKey && event.keyCode == 73) { // Prevent Ctrl+Shift+I        
		        return false;
		    }
		}); */
	</script>	
</body>
</html>