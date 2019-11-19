<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.WorkOrderBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<%log("Print Page"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/loader.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>

		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<script src="//code.jquery.com/jquery.min.js"></script>
		<script src="jquery.stickytable.min.js"></script>
		  		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
	
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="./../CacheClear.jsp" /> 
	
<style>
/* style for hover button on pdf pages */
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}

.middle {
    position: absolute;
    right: 30px;
    bottom: 16px;
    width: 100% !important;
}
.btn-fullwidth:hover{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent;
    background-color: transparent;
    border-color: transparent;
}
 .btn-fullwidth:active{
 color: transparent;
    background-color: transparent;
    border-color: transparent;
 }
 .btn-fullwidth.focus, .btn-fullwidth:focus {
    color: transparent;
    background-color: transparent;
    border-color: transparent;
}
/* style for hover button on pdf pages */


		 /* .NMRBillstableId thead tr td{border:1px solid #000;} */	
	    table.dataTable {border-collapse: collapse !important;}
		.tbl-td{
		border-top:1px solid #fff !important;
		padding:3px;
		}
		.tbl-td-bottom{
		border-bottom:1px solid #fff !important;
		padding:3px;
		}
	.conatianer1{
	    float: left;
	}
	 body{
  font-weight:600;
  }
    table, th, td , tbody {
    border: 1px solid #000 !important;
    border-collapse: collapse;
}
thead{
background-color:#ccc;
}
.page-body{
  padding: 20px;
}
.tbl-border-top{
  border-bottom: none !important;
  margin-top:-1px;
}
.tbl-border-top{

  margin-bottom:0px;
}
.tbl-border-top tr .td-top{
  border-top:none !important;
}
.vertical-alignment { 
  vertical-align: middle !important;
}
.bck-ground{
background-color:#ccc;
}
.td-active{
background-color:#dbecf6;
}
.td-active input{
background-color:#dbecf6;
}
.td-background{
background-color:#dbecf6;
}
.td-active input:focus{

}
.td-active input{
font-weight:bold;
}
.border-none input{
border:none !important;
}
.input-outline input{
outline:none;
}
.tbl-corner-right{
height:73px;
margin-right:-16px;
width: 300px;
text-align: center;
}

.widthhundreadper{width:100%;}
@media print {
.btn-warning, .hideInPrint, #showHideRemarks, #ModificationDetails, #recovery, .hideinPrint, #paymentLedgerTable, #appendBillDetailsTotal, #PDFView, #IMAGEView, .donotprint{
display:none;
}
#amountInwordsInPrint{
 display:block !important;
}
.print-size{width:100% !important;}

/* .widthhundreadper{
width:104.5% !important;
} */
/* @page {size: landscape} */
.breadcrumb, #amountInWords{
display:none;
}
.left_col{
display:none;
}
.nav_menu{
display:none;
}
thead {
    display: table-row-group;
}
.img-workorder-logo{
margin-top:5px;
}
.head-logo-workorder{
width:350px;
float:left;
}
.tbl-corner-right{
height:90px;
margin-right:-16px;
width: 250px;
text-align: center;
}
.border-print{
border-top:1px solid #000;
}
.print-tblheight{height:100px;}
}

/* for loader */
/* Absolute Center Spinner */
.loading {
  position: fixed;
  z-index: 999;
  height: 2em;
  width: 2em;
  overflow: show;
  margin: auto;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}

/* Transparent Overlay */
.loading:before {
  content: '';
  display: block;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
    background: radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0, .8));

  background: -webkit-radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0,.8));
}

/* :not(:required) hides these rules from IE9 and below */
.loading:not(:required) {
  /* hide "loading..." text */
  font: 0/0 a;
  color: transparent;
  text-shadow: none;
  background-color: transparent;
  border: 0;
}

.loading:not(:required):after {
  content: '';
  display: block;
  font-size: 10px;
  width: 1em;
  height: 1em;
  margin-top: -0.5em;
  -webkit-animation: spinner 1500ms infinite linear;
  -moz-animation: spinner 1500ms infinite linear;
  -ms-animation: spinner 1500ms infinite linear;
  -o-animation: spinner 1500ms infinite linear;
  animation: spinner 1500ms infinite linear;
  border-radius: 0.5em;
  -webkit-box-shadow: rgba(255,255,255, 0.75) 1.5em 0 0 0, rgba(255,255,255, 0.75) 1.1em 1.1em 0 0, rgba(255,255,255, 0.75) 0 1.5em 0 0, rgba(255,255,255, 0.75) -1.1em 1.1em 0 0, rgba(255,255,255, 0.75) -1.5em 0 0 0, rgba(255,255,255, 0.75) -1.1em -1.1em 0 0, rgba(255,255,255, 0.75) 0 -1.5em 0 0, rgba(255,255,255, 0.75) 1.1em -1.1em 0 0;
box-shadow: rgba(255,255,255, 0.75) 1.5em 0 0 0, rgba(255,255,255, 0.75) 1.1em 1.1em 0 0, rgba(255,255,255, 0.75) 0 1.5em 0 0, rgba(255,255,255, 0.75) -1.1em 1.1em 0 0, rgba(255,255,255, 0.75) -1.5em 0 0 0, rgba(255,255,255, 0.75) -1.1em -1.1em 0 0, rgba(255,255,255, 0.75) 0 -1.5em 0 0, rgba(255,255,255, 0.75) 1.1em -1.1em 0 0;
}

/* Animation */

@-webkit-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-moz-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@-o-keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}
@keyframes spinner {
  0% {
    -webkit-transform: rotate(0deg);
    -moz-transform: rotate(0deg);
    -ms-transform: rotate(0deg);
    -o-transform: rotate(0deg);
    transform: rotate(0deg);
  }
  100% {
    -webkit-transform: rotate(360deg);
    -moz-transform: rotate(360deg);
    -ms-transform: rotate(360deg);
    -o-transform: rotate(360deg);
    transform: rotate(360deg);
  }
}

	</style>
		<script type="text/javascript">
		if (typeof (Storage) !== "undefined") {
			debugger;
		var isUpdatePage="${isBillUpdatePageRequest}";
			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				if(isUpdatePage=="true"){
					window.location.assign("updateContractorBills.spring");
				}else{
					window.location.assign("viewCompletedBills.spring");
				}
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
		</script>
</head>
<body class="nav-md">

   <div class="overlay_ims"></div> <!-- -->
   <div class="loader-ims" id="loaderId"> <!--   -->
	 <div class="lds-ims">
		<div></div><div></div><div></div><div></div><div></div><div></div>
	</div>
	<div id="loadingimsMessage">Loading...</div>
</div>


<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
</noscript>

<form:form modelAttribute="billBean"  action="approveNMRBill.spring" method="POST" id="approveNMRBillFormId"  enctype="multipart/form-data" >
<div class="container body" id="mainDivId">
<div class="loading" id="LoadingId" style="display:none;">Loading&#8230;</div>

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
							<li class="breadcrumb-item active">NMR Bills</li>
						</ol>
					</div>
					<div class="col-md-12">
					<input type="hidden" name="nextLevelApproverEmpID" id="nextLevelApproverEmpID" value="${nextLevelApprovedId}">
					<input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
					<input type="hidden" name="noOfRowsToIterate" id="noOfRowsToIterate">
					<input type="hidden" name="approvePage" id="approvePage" value="true">
					<input type="hidden" name="siteId" id="site_id" value="${billBean.siteId }">
					<input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }">
					<input type="hidden" name="BillNo" id="billNo" value="${billBean.billNo}">
					<input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
					
					<input type="hidden" id="workOrderNo" name="WorkOrderNo" value="${billBean.workOrderNo}">
				<%-- 	<input type="hidden" name="workOrderNo" value="${billBean.workOrderNo}"> --%>
					<input type="hidden" name="billType" id="billType" value="NMR">
					<input type="hidden" name="printName" id="printName">
					
				 <form:input path="approverEmpId" type="hidden" value="${nextLevelApprovedId}"/>
				 <form:input path="permanentBillNo"   id="permanentBillNo" type="hidden"/>
				 <form:input path="paymentType" type="hidden"/>
				 <form:input path="totalAmount" type="hidden"/>
				 <form:input path="contractorId"   type="hidden" />
				 <form:input path="contractorName"   type="hidden" />
				 <form:input path="contractorAddress"   type="hidden" />
				 <form:input path="contractorPhoneNo"   type="hidden" />
				 <form:input path="contractorPanNo"   type="hidden" />
				 <form:input path="contractorBankAccNumber"   type="hidden" />
				 <form:input path="contractorIFSCCode"   type="hidden" />
				 <form:input path="contractorBankName"   type="hidden" />
				 <form:input path="GSTIN"   type="hidden" />
					
		<table style="border:1px solid #000 !important;width:100%;" class="widthhundreadpers">
			   <tbody>
					    <tr>
                               <td rowspan="2" style="border:1px solid #000 !important;width:15%;text-align:center;"><img src="images/SumadhuraLogo2015.png" /></td>
                               <td style="width:85%;text-align:center;"><h4><strong>SUMADHURA INFRACON PVT LTD</strong></h4></td>
                       </tr>
                       <tr style="text-align:center;">
                               <td style="padding:18px;font-size: 16px;">Abstract</td>
                       </tr>
                       </tbody>
                       </table>
                    
           <c:set value="PETTY" var="PETTY"></c:set>
           <c:set value="OTHER" var="OTHER"></c:set>
           <c:set value="RECOVERY" var="RECOVERY"></c:set>
           
           <c:forEach items="${deductionList }" var="deducList">
	           <c:if test="${deducList.TYPE_OF_DEDUCTION eq PETTY}">
	                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="pettyExpensesCurrentCerti"></c:set>
	            </c:if>
	            <c:if test="${deducList.TYPE_OF_DEDUCTION eq OTHER}">
	                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="other"></c:set>
	            </c:if>
	              <c:if test="${deducList.TYPE_OF_DEDUCTION eq RECOVERY}">
	                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="recoveryAmount"></c:set>
	            </c:if>
            </c:forEach> 
                         <input type="hidden" id="hiddenpettyExpensesCurrentCerti" value="${pettyExpensesCurrentCerti}">
         				   <input type="hidden" id="hiddenother" value="${other}">
         				   
                       <table style="border:1px solid #000 !important;margin-top:-1px;width:100%;" class="widthhundreadpers">
                       <tbody>
                       <tr>
                           <td style="width:50%;padding:7px;">
                           		<div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Name of the Project</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.siteName}</strong></h5></div>
                               </div> 
                                <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Block</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>  <c:set var="blockNames" value="${fn:split(billBean.NMRBillBlocks, '@#')}" /></strong>
                                 <h5>
                                 <strong>
	                                <c:forEach var="block" items="${blockNames}">
	                        				<p style="word-break: break-all;">${block}</p>
	                             	</c:forEach>   
                            
                                	</strong></h5>
                                	 <h5><c:if test="${empty billBean.NMRBillBlocks}">
	                             	     <p>
	                             	     -
	                             	     </p>
	                             	    </c:if>
	                             </h5>
                                	
                                	</div>
                               </div> 
                           </td>
                           <td colspan="5" style="width:50%;padding-top:7px;padding-right:7px;padding-left:15px;">
                           
                              <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Bill Date</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong><input  type="text" name="billDate" id="billDate" value="${billBean.paymentReqDate }" class="rabill-class-select" style="display:inline;border:none;"  readonly="readonly"/></strong></h5></div>
                               </div> 
                                 <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong> Contractor Name </strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.contractorName}</strong></h5></div>
                               </div> 
                               
                                 <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Type of work</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>Labour Supply</strong></h5></div>
                               </div> 
                               
                                 <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>RA Bill No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.billNo}</strong></h5></div>
                               </div>
                                <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Invoice No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.billInvoiceNumber}</strong></h5></div>
                               </div> 
                               <div class="clearfix"></div>
                               <c:if test="${ not empty billBean.oldWorkOrderNo}">
									 
								<div style="width:100%;"   class="donotprint">
                                 <div style="width:40%;float:left;"><h5><strong>Old Work Order No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong id="printWorkOrderNo">${billBean.oldWorkOrderNo}</strong></h5></div>
                               </div> 
										
			 				 	</c:if>
                               
                               <c:if test="${ not empty billBean.oldWorkOrderNo}">
				                <div style="width:100%;"   class="donotprint">
                                 <div style="width:40%;float:left;"><h5><strong>Old WO Amount </strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong id="printWorkOrderNo">${billBean.oldWorkOrderTotalAmount}</strong></h5></div>
                               </div> 
				            </c:if>
                               
                               
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Work Order No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong id="printWorkOrderNo">${billBean.workOrderNo}</strong></h5></div>
                               </div> 
                               <div class="" style="width:100%;">
							    <div class="mrg-Top" style="width:40%;float:left;"><h5><strong>WO Amount</strong></h5> </div>
							   <div class="mrg-Top"style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
							  <div class="mrg-Top" style="width:50%;float:left;"><h5><strong  id="printtotalAmtToPay">${billBean.totalAmount}</strong> <input type="hidden" name="totalAmtToPay" id="totalAmtToPay" value="${billBean.totalAmount}"   readonly="readonly" /></h5></div>
							</div>
                           </td>
                       </tr>
                    <!--    <tr>
                           <td style="width:50%;padding:5px;"> </td>
                           <td style="width:14%;padding:5px;">Bill Date : </td>
                           <td style="width:5%;padding:5px;">From : </td>
                           <td style="width:14%;padding:5px;"><span id="fromDate1"></span></td>
                           <td style="width:5%;padding:5px;">To: </td>
                           <td style="width:14%;padding:5px;"><span id="toDate1"></span></td>
                       </tr> -->
					   </tbody>
					  </table>
					 
					
					   <table class="print-size" style="border:1px solid #000;margin-top:-1px;width:100%;" >
					   <thead>
					    <tr style="background-color:snow;font-size:14px;">
                            <td colspan="5"></td>
                           	<td style="text-align:center;" colspan="2">Bill Date</td>
                           	<!-- <td></td> -->
                           	<td style="text-align:center;">From</td>
                           	<td style="text-align:center;"><span id="fromDate1"></span>  </td>
                           	<td style="text-align:center;">To</td>
                            <td style="text-align:center;"><span id="toDate1"></span> </td>
                       </tr>
					   <tr style="font-size:16px;">
                           <th rowspan="2" style="padding:2px;text-align:center;">S.NO</th>
                           <th rowspan="2" style="padding:2px;text-align:center;">Description</th>
                           <th rowspan="2" style="padding:2px;text-align:center;">Total Qty</th>
                           <th rowspan="2" style="padding:2px;text-align:center;">Avg Rate</th>
                           <th rowspan="2" style="padding:2px;text-align:center;">Unit</th>
                           <th colspan="2" style="padding:2px;text-align:center;">Cumulative Certified</th>
                           <th colspan="2" style="padding:2px;text-align:center;">Previous Certified</th>
                           <th colspan="2" style="padding:2px;text-align:center;">Current Certified</th>
                        </tr>
                        <tr style="font-size:16px;">
                            <th style="padding:2px;text-align:center;">Qty</th>
                            <th style="padding:2px;text-align:center;">Amount</th>
                         
                            <th style="padding:2px;text-align:center;">Qty</th>
                            <th style="padding:2px;text-align:center;">Amount</th>
                         
                            <th style="padding:2px;text-align:center;">Qty</th>
                            <th style="padding:2px;text-align:center;">Amount</th>
                         </tr>
					   </thead>
						<tbody id="NMRAbstractTableData" >
						  
						</tbody>
						<tbody>
							<tr class="hideInPrint">
								<td class="text-center"></td> 	
								<td class="text-center"><span>Petty Expenses</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt CumilativeCAmount" id="pettyExpensesCumulative"  style="border:none;text-align:center;" value="0.00" readonly/></td>
								<td style="text-align:center;padding:3px;"></td>
								<td class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt PreviousCAmnt" id="pettyExpensesPrevCerti"  style="border:none;text-align:center;" value="0.00" readonly/></td>
								<td class="text-center"></td>
								<td class="text-center"><input type="text"  id="pettyExpensesCurrentCerti" name="pettyExpensesCurrentCerti" class="raDeductionAmt currentCAmnt" style="border:none;width:100%;text-align:center;" value="${pettyExpensesCurrentCerti}" readonly> <input type="hidden" name="actualPettyExpensesCurrentCerti"  value="${pettyExpensesCurrentCerti}" > <br></td>
							</tr>
							<tr class="hideInPrint">
								<td class="text-center"></td> 	
								<td class="text-center"><span>Others</span></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"></td>
								<td style="text-align:center;padding:3px;"><input type="text"  name="otherAmtCumulative" class="CcAmnt CumilativeCAmount" id="otherAmtCumulative"  style="border:none;text-align:center;" value="0.00" readonly/></td>
								<td style="text-align:center;padding:3px;"></td>
								<td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt PreviousCAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="0.00" readonly/></td>
								<td class="text-center"></td>
								<td class="text-center"><input type="text"  id="other" name="other" class="raDeductionAmt currentCAmnt" style="border:none;width:100%;text-align:center;" value="${other}" readonly><input type="hidden" id="actualOtherAmt" name="actualOtherAmt" value="${other}">  <br></td>
							</tr>
							<tr class="hideInPrint">
								<td class="text-center"></td> 	
								<td class="text-center"><span>Recovery</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"><input type="text"  name="cumulativeRecovery" class="CcAmnt CumilativeCAmount" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
								<td style="text-align:center;padding:1px;"></td>
								<td class="text-center"><input type="text"  name="previousRecovery" class="PcAmnt PreviousCAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
								<td class="text-center"></td>
								<td class="text-center"><input type="text"  id="currentRecoveryAmount" name="currentRecoveryAmount" class="raDeductionAmt currentCAmnt" value="${recoveryAmount }"  style="border:none;width:100%;text-align:center;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount }"> <br><a class="" href="#" data-toggle="modal" data-target="#modal-recovery-click" id="recovery">Click Here</a></td>
							</tr>
								
							<tr class="hideInPrint">
								<td class="text-center"></td>
								<td class="text-center">Total Amount(B)</td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"><input type="text" name="totalAmtCumulative" id="totalAmtCumulative" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly=""></td>
								<td style="text-align:center;padding:1px;"></td>
								<td class="text-center"><input type="text" name="totalAmtPrevCerti" id="totalAmtPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly=""></td>
								<td class="text-center"></td>
								<td class="text-center"><input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="6" readonly=""></td>			
							</tr>
							
							<tr class="hideInPrint">
								<td class="text-center"></td>
								<td class="text-center"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td style="text-align:center;padding:13px;"></td>
								<td class="text-center"></td>
								<td class="text-center"></td>
								<td class="text-center"></td>			
							</tr>
								
								
							<tr class="hideInPrint" style="background-color: #ccc;">
								<td style="text-align:center;padding:1px;"><h5><strong></strong></h5></td>
								<td style="text-align:center;padding:1px;"><strong>Net Payable Amount (A - B)</td>
								<td style="text-align:center;padding:1px;"><strong></strong></td>
								<td style="text-align:center;padding:1px;"><strong></strong></td>
								<td style="text-align:center;padding:1px;"><strong></strong></td>
								<td style="text-align:center;padding:1px;"><strong id="CBTotalQty">0</strong></td>
								<td style="text-align:center;padding:1px;"><strong id="CBToatalAmount">0</strong></td>
								<td style="text-align:center;padding:1px;"><strong id="PCTotalQty">0</strong></td>
								<td style="text-align:center;padding:1px;"><strong id="PCToatalAmount" >0</strong></td>
								<td style="text-align:center;padding:1px;"><strong id="CCTotalQty">0</strong> <input type="hidden"  name="actualPaybleAmount" id="actualPaybleAmount"></td>
								<td style="text-align:center;padding:1px;"><strong id="CCToatalAmount">0</strong><input type="hidden" name="finalAmt" id="finalAmt"> </td>				       
							</tr>
								
							<tr>
								<td class="text-center" colspan="5" style="padding:1px;">Amount in Words</td>
								<td class="text-center" colspan="6" style="padding:1px;"><span  id="amountInWords"></span><span style="display:none;" id="amountInwordsInPrint"></span></td>								
							</tr>
								
							<tr class="hideinPrint">
								<td class="text-center" colspan="5" style="padding:1px;"><strong></strong></td>
								<td class="text-center" colspan="6" style="padding:1px;"><strong><a href="javascript:void(0);" onclick="openAbstract()" id="NmrAbstract">NMR Details</a></strong></td>
							</tr> 
						  </tbody>
					   </table> 
					    <c:set value="1" var="countVerifiedEmpNames"></c:set>
					   <c:set value="END" var="workOrderStatus"/>
					   <c:set value="${100/listOfVerifiedEmpNames.size()}" var="dynamicWidth"></c:set>
					     <c:if test="${listOfVerifiedEmpNames.size()==1}">
						   <c:set value="${100/2}" var="dynamicWidth"></c:set>
					   </c:if>
					   <div style="border: 1px solid #000;margin-top: -1px;overflow: hidden;">
					   <c:forEach items="${listOfVerifiedEmpNames}" var="verifiedEmpNames">
						  <c:choose>
						  	<c:when test="${countVerifiedEmpNames eq 1 }">
							  	<div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
								    <h5>
								      <strong style="text-align: center;">Prepared By </strong><br><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
								  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</span><br>
								    </h5>
							    </div>
						  	</c:when>
						    <c:when test="${countVerifiedEmpNames eq listOfVerifiedEmpNames.size() }">
								  	 <div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
									    <h5>
									      <strong style="text-align: center;">Approved By </strong><br><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
									  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</span><br>
									    </h5>
								    </div>
							  	</c:when>
						  	<c:otherwise>
							  	<div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
								    <h5>
								 	  <strong style="text-align: center;">Verified By </strong><br><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
								  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</span><br>
								    </h5>
							    </div>
						  	</c:otherwise>
						  </c:choose>
						
						   <c:set value="${countVerifiedEmpNames+1 }" var="countVerifiedEmpNames"></c:set>
					   </c:forEach>
					</div>
 <div style="width:100%;" id="showHideRemarks">
     <div class="col-md-6 Mrgtop10" style="width:50%;">
			<label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
			<div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
			<input type="hidden" name="actualremarks" value="${woLevelPurpose}">
		 		<textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title=" <c:out value='${woLevelPurpose}'></c:out>" placeholder=" <c:out value='${woLevelPurpose}'></c:out>"  id="NoteId"  autocomplete="off"></textarea> 
			</div>
		</div>
 </div>
<c:if test="${changedListDtls.size()!=0}">
<%-- <div class="col-md-12 Mrgtop10" id="ModificationDetails">
		<label class="control-label col-md-2" >Modification Details : </label>
		<div class="col-md-6" style="    position: relative;margin-bottom: 10px;" >
			<c:forEach items="${changedListDtls}" var="chngedDtls">
			${chngedDtls.REMARKS}	
			</c:forEach>
		</div>
</div> --%>
</c:if>
					
					
					
					 
					  <!-- <button class="btn btn-warning" onclick="window.print()">Print</button> -->
					  <div class="col-md-12 text-center center-block" style="margin-top: 40px;">
					  <!-- <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#NMRBillTableModal">NMR Abstract</button> -->
					
					  <c:if test="${status eq true }">
					  <c:if test="${isBillUpdatePageRequest ne true }">
							<input type="button" name="closeBtn" id="closeBtn" onclick="return closeView()" class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
							<input type="button" name="submitBtn"   class="btn btn-warning btn-custom-width" onclick="printPage()" 	value="Print Abstract" />
						 	<input type="button" name="sumadhuraCopy" id="sumadhuraCopy" onclick="return getSumadhuraCopy()" class="btn btn-warning btn-custom-width"value="Sumadhura Copy" />					 	
						 	<input type="button" name="contractorCopy" id="contractorCopy" onclick="return getContractorCopy()" class="btn btn-warning btn-custom-width"value="Contractor Copy" />
						 	<input type="button" name="nmrDetails" id="nmrDetails" onclick="return nmrDetailsPrint()" class="btn btn-warning btn-custom-width"value="NMR Details" />
					 		<input type="button" name="billLedger" id="billLedger"   class="btn btn-warning btn-custom-width"  value="Ledger" />
					 		<input type="hidden" value="viewCompletedBills.spring" id="pageType">
					 	</c:if>
					  </c:if>
					  </div>
					  
					  <!-- Ledger -->
			<div class="col-md-12">
			  <table style="border:1px solid #000;width:100%;margin-bottom:30px;margin-top:30px;" class="bordsertable"  id="paymentLedgerTable">
	                   <thead>
		                <!--  <tr>
    	                   <th style="padding:8px;text-align:center;width:5%;">Date</th>
                           <th style="padding:8px;text-align:center;">Particular</th>
                           <th style="padding:8px;text-align:center;">Bill amount</th>
                           <th style="padding:8px;text-align:center;">NMR Bill</th>
                           <th style="padding:8px;text-align:center;">Material Deductions</th>
                           <th style="padding:8px;text-align:center;">Hold Amont</th>
                           <th style="padding:8px;text-align:center;">Hold Release</th>
                           <th style="padding:8px;text-align:center;">Recovery Amount</th>
                           <th style="padding:8px;text-align:center;">Amount Paid</th>
                           <th style="padding:8px;text-align:center;">Cumulative Total</th>
                        </tr>              -->
                         <tr>           
					             <th  style="padding:8px;text-align:center;">Date</th>
							     <th style="padding:8px;text-align:center;">Particular</th>
							     <th style="padding:8px;text-align:center;">Bill Amount</th>
							     <th style="padding:8px;text-align:center;">Advance</th>
							     <th style="padding:8px;text-align:center;">Advance Deduction</th>
							     <th style="padding:8px;text-align:center;">SD Release</th>
							     <th style="padding:8px;text-align:center;">Material Deductions</th>
							     <th style="padding:8px;text-align:center;">Petty Expenses</th>
							     <th style="padding:8px;text-align:center;">Other Amount</th>
							     <th style="padding:8px;text-align:center;">Hold Amount</th>
							     <th style="padding:8px;text-align:center;">Hold Release</th>
							     <th style="padding:8px;text-align:center;">SD 5%</th>
							     <th style="padding:8px;text-align:center;">Amount Paid</th>
							     <th style="padding:8px;text-align:center;">Cumulative Total</th>   
						     </tr>                    
					   </thead>
					  <tbody id="ledger">
					  </tbody>
			         </table>
			         </div>
					  	              
                   <div id="appendBillDetailsTotal"  style="float:  right;margin-bottom:30px;"></div>
					  
					  </div>
					

 <!-- </div> -->


<!-- Starting Images From Here -->



<div class="col-md-12 print_hide" style="margin-top: 10px;" id="PDFView">
	<!-- <h3>You can see the PDF</h3> -->
<%
int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
if(pdfcount==0){
	%>
<!-- 	<h3>No PDF</h3> -->
	<%
}else{
	%>
<h3 style="color: #ffa500;">You can see the PDF(s) below :</h3>	
	<%
}	
for(int i=0;i<pdfcount;i++){
  	String pdfName="pdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
<%
		if(request.getAttribute(pdfName)!=null){
%>
			 <div class="col-md-3 pdfcount pdf-delete<%=i%>"  id="pdfDivHideShow<%=pdfName %>">
			  <div class="pdf-cls" style="margin-bottom:15px;"> 
			  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
			  <iframe class="iframe-pdf" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
			  <div class="middle">
				<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf<%=i%>"><i class="fa fa-close"></i></button>
			</div>
			</div>
			
			 </div>
<%} %>
<%} %>
</div>
 
 		<!-- ***********************************************this is for pdf download end************************************************** -->		
				<div class="col-md-12 Mrgtop20 print_hide"  style="" id="IMAGEView">
	
			<!-- 	<h3>You can see the Images</h3> -->
						<%
										int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
										//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
							 				if(imagecount==0){
							 					%>
							 					<!-- <h3>No Images</h3> -->
							 					<%
							 				}else{
							 				%>
							 					<h3 style="color: #ffa500;">You can see the Images below :</h3>
							 				<%
							 				}		
 									for (int i = 0; i < imagecount; i++) {
												String imageB64="image"+i;	
												String deleteB64="delete"+i;
												log(imageB64);
												out.print("<div class='col-md-4 Mrgtop20'>");
									%>
									       <c:set value="<%=imageB64 %>" var="index"></c:set>
									        <c:set value="<%=deleteB64 %>" var="delete"></c:set>
																		<%
									//	if (i == 0) {
										if(request.getAttribute(imageB64)!=null){
									%>
									 
									 <div class="container-1" id="imageDivHideShow<%=imageB64 %>">
													<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
													 <div class="middle-1">
													<div class="columns download">
										           <p>
										             <a class="button btn-dwn btn-success  btn-small" download onclick="toDataURL('${requestScope[index]}',this)"><i class="fa fa-download"></i>&nbsp;Download</a>
										       	 <c:if test="${isBillUpdatePageRequest eq true }">
										             <button onclick="deleteWOImageFile('<%=imageB64%>','${requestScope[delete]}')" type="button" class="button btn-dwn btn-danger  btn-small1"><i class="fa fa-remove"></i> &nbsp;Delete</button>
										       	</c:if>
										          </p>
										       </div>
										       </div>
										       </div>
									<%
										}
									//	}
									%>
								<%
									out.print("</div>");
								%>
								<%}%>
							
							<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent" value="<%=imagecount%>" />
							<input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfcount%>">
							
							
							<!--if isBillUpdatePage equal true then this page for Update Permanent bills  -->
							<c:if test="${isBillUpdatePageRequest eq true }">
							
								<div class="col-md-12">
										<div class="file-upload color-head-file" id="ishidden">
												<c:if test="${(imagecount+pdfcount)<8}">
													<h3>You can upload Images/PDF(s) here :</h3>
												</c:if>
											<%
												for (int i = 0; i < imagecount; i++) {
															out.println("<div id=\"fileupload" + i
																	+ "\" style=\"display:none;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'   name=\"file\"></div>");
														}
											%>
											<%
												for (int i = (imagecount+pdfcount); i < 8; i++) {
															out.println("<div id=\"fileupload" + i
																	+ "\" style=\"display:block;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'  name=\"file\"></div>");
														}
											%>
										</div>
									</div>
									
									</c:if>
		
		</div>
		</div>
					</div>
<c:if test="${isBillUpdatePageRequest eq true }">
<div class="col-md-12 text-center center-block">
   	
    <input type="button" name="updateBill" id="updateBill" class="btn btn-warning"  value="Update Bill"/>
    <input type="button" name="submitBtn" id="viewBill" onclick="return closeView()"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
    <input type="hidden" value="updateContractorBills.spring" id="pageType">
 </div>
 
 </c:if>
	
	
	</div>	
 	</div>
 	<!-- model popup for pdf start  -->
	<%
	 pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
	for(int i=0;i<pdfcount;i++){
  	String pdfName="pdf"+i;
  	String PathdeletePdf="PathdeletePdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
 <%
		if(request.getAttribute(pdfName)!=null){
%>
			<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i+1 %></strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="${requestScope[pdfBase64]}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
	 <c:if test="${isBillUpdatePageRequest eq true }">
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button>
	 </c:if>
	   </p>
      </div>
    </div>

  </div>
</div>
<%} %>
<%} %>


<!-- pdf model popup end  -->
	
	
<!-- modal popup for image pop start-->
<!-- Modal -->
	 <!-- Modal -->
	<%	  imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			for (int i = 0; i < imagecount; i++) { 
		String index="image"+i;				
		log(index);
		%>
	  <div class="modal fade custmodal" id="uploadinvoice-img<%=i %>" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          
        </div>
        <div class="modal-body cust-modal-body">
    <c:set value="<%=index %>" var="i"></c:set>
    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="${requestScope[i]}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  <%} %>
		<!-- modal popup for invoice image end --> 
						
<!-- Images and Pdf's Code Completed -->




	              
<!-- modal popup for recovery click action start-->
 <!-- Modal -->
<div id="modal-recovery-click" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg modal-rabill-customwidth">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Recovery Statement </strong></h4>
      </div>
      <div class="modal-body">
        <div class="table-responsive">
         <table class="table table-bordered recovery-popup-table">
         <thead>
         <tr> 
          <th>S.NO</th>
          <th>Description Of material</th>
          <th>Total qty Consumed</th>
          <th>Total Amount</th>
          <th>Unit</th>
          <th>Cumulative Recovered</th>
          <th>Previous Recovered</th>
          <th>Current Recovered</th>
         </tr>
        </thead>
        <tbody id="RecoveryStatement">

        </tbody>
         </table>
        </div>
      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" class="btn btn-warning" id="recoverySubmitBtnID" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
<!-- <input type="text" style=""> -->
  </div>
</div>





 <!-- modal popup for recovery click action end -->
	              
				<div id="NMRBillTableModal" class="modal fade" role="dialog">
					<div class="modal-dialog" style="width:95%;">
			
						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title text-center" style="font-weight:1000">NMR Details</h4>
							</div>
							<div class="modal-body">
								 <div class="table-responsive">
								 <input type="hidden" id="workOrderNo" name="workOrderNo" value="${billBean.workOrderNo}">
								   <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId}"   />
								
                          <%--     <strong>VENDOR : Labour supply<input type="text" class="form-control border-none" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" style="width: 300Px;"   autocomplete="off"  readonly="readonly"  /></strong>
							   <input type="hidden" name="siteId" id="siteId" value="${siteId}">
                               <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId}"   />
				       			<strong>Work Order/Ref. No : </strong>
                                <select id="workOrderNo" name="workOrderNo"> 
                      				 <option value="${billBean.workOrderNo}">${billBean.workOrderNo }</option>
                               </select>  
                               <strong>6/8/2018 </strong> --%>
                      
								 
								 
                       <table style="border:1px solid #000;width:1500px;margin-bottom:15px;" id="NMRBillstableId" >
					   <thead  id="NMRHeadData">		  

					   </thead>
					  
					  <tbody id="NMRBillstableIdfirstRow">

					     </tbody>
					     </table>					    
					     </div>
							</div>
							<div class="modal-footer">
								<div class="text-center center-block">
								  <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
								 <c:if test="${status ne true}">
									<button type="button" class="btn btn-warning" id="NMRCreateBtn" onclick="NMRCreateTableSubmit()">Submit</button>  <!-- data-dismiss="modal" -->
								</c:if>
								</div>
							</div>
						</div>
			
					</div>
				</div>
</form:form>
	<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
       <script src="js/WorkOrder/NMRTable.js"></script>
		<script src="js/WorkOrder/NMRBills.js"></script>
		<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
		<script type="text/javascript" src="js/WorkOrder/NMRCompleted.js"></script>
		<script type="text/javascript">
		//$("#LoadingId").show();
		 setTimeout(function(){
			 loadNMRBillDataForApprovel();
			 loadNMRCompletedBillData();
		 }, 500); 
		 
		 var statusOfPage="${status}";
		 
		 function closeView(){
				goBack() ;
			}
			function goBack() {
			    window.history.back();
			}
			if(window.history.length==1){
				$("#closeBtn").hide();
			}
			function getSumadhuraCopy(){
				debugger;
				$("#printName").val("Sumadhura Copy");
				var url="nmrContractorCopy.spring";
				  $('#approveNMRBillFormId').attr('target', '_blank');
			      document.getElementById("approveNMRBillFormId").action =url;
			      document.getElementById("approveNMRBillFormId").method = "POST";
			      document.getElementById("approveNMRBillFormId").submit();
			}
			function nmrDetailsPrint(){
				debugger;
				 $("#printName").val("NMR Details");
				  var url="nmrContractorCopy.spring";
				  $('#approveNMRBillFormId').attr('target', '_blank');
			      document.getElementById("approveNMRBillFormId").action =url;
			      document.getElementById("approveNMRBillFormId").method = "POST";
			      document.getElementById("approveNMRBillFormId").submit();
			}
			
			 function getContractorCopy(){
				 debugger;
				 $("#printName").val("Contractor Copy");
		     		  var url="nmrContractorCopy.spring";
					  $('#approveNMRBillFormId').attr('target', '_blank');
				      document.getElementById("approveNMRBillFormId").action =url;
				      document.getElementById("approveNMRBillFormId").method = "POST";
				      document.getElementById("approveNMRBillFormId").submit();
			}
	
			
			$("#updateBill").on("click",function(){
				 var contractorName=$("#ContractorName").val();
				 var contractorId=$("#ContractorId").val();
				
				debugger;
				 var result=validateFileExtention();
				 if(result==false){
					 return false;
				 }
				 debugger;
				 
				 var canISubmit = window.confirm("Do you want to update bill?");
				
				 if(canISubmit == false) {
				        return false;
				 }
				 excuteDeleteFunction();
				  sessionStorage.setItem($("#userId").val()+"deleteUploadedFiles","");
				  
			});

			function deleteFile(divId,url){
				debugger;
				 $.ajax({
					  url : url,
					  type : "get"/* ,
					  success : function(data) {
						console.log(data);
					
					  },
					  error:  function(data, status, er){
						//  alert(data+"_"+status+"_"+er);
						} */
					  });
			}

			var deleteUploadedimageFiles=new Array();
			var deleteUploadedPDFFiles=new Array();

			function deleteWOImageFile(divId,imagePath){
				 debugger;
				 
			 var canISubmit = window.confirm("Do you want to delete image?");
			     
			 if(canISubmit == false) {
			        return false;
			 }
			 $("#imageDivHideShow"+divId).remove();
			 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");		 
			 // deleteFile(divId,imagePath);
			 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
			 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
			 var url="deleteWOBillsImage.spring?imagePath="+imagePath+"&workOrderNo=${billBean.workOrderNo}&tempBillNo=${billBean.billNo}&siteId=${billBean.siteId}";

			 //Stroing files url for deleting files after update button press
			 deleteUploadedimageFiles.push({"imageNo":divId,"url":url});
				
			 return false;
			}

			function deletepdf(divId,imagePath){
			  debugger;
				 var canISubmit = window.confirm("Do you want to delete PDF?");
				 if(canISubmit == false) {
				        return false;
				 }
				 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
				 $("#pdfAlreadyPresent").val(pdfAlreadyPresent-1);
				 var url="deleteWOBillsImage.spring?imagePath="+imagePath+"&workOrderNo=${billBean.workOrderNo}&tempBillNo=${billBean.billNo}&siteId=${billBean.siteId}";
				 deleteUploadedPDFFiles.push({"pdfNo":divId,"url":url});
				 $("#pdfDivHideShow"+divId).remove();
				 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");
				 return false;
			}

			function excuteDeleteFunction(){
			debugger;
			 deleteUploadedimageFiles.sort(sortUrlData);
				 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedimageFiles.length; imgPdfIndex++) {
						if(deleteUploadedimageFiles[imgPdfIndex].length!=0)
						deleteFile(imgPdfIndex,deleteUploadedimageFiles[imgPdfIndex].url);
					} 
				 deleteUploadedPDFFiles.sort(sortPdfData);
				 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedPDFFiles.length; imgPdfIndex++) {
						if(deleteUploadedPDFFiles[imgPdfIndex].length!=0)
						deleteFile(imgPdfIndex,deleteUploadedPDFFiles[imgPdfIndex].url);
					}
				 debugger;
				var len=deleteUploadedimageFiles.length;
				len=len+deleteUploadedPDFFiles.length;
				var timeOut=len*250;
				setTimeout(function(){
					 var url="updateContractorBill.spring";
					 document.getElementById("approveNMRBillFormId").action =url;
				      document.getElementById("approveNMRBillFormId").method = "POST";
				      document.getElementById("approveNMRBillFormId").submit();
				}, timeOut)    
			      
			}
			function sortPdfData(a,b){
			debugger;
				
				var	pdf=a.pdfNo.split("pdf")[1];
				var	pdf1=b.pdfNo.split("pdf")[1];
				
				
				 if(pdf < pdf1) {
					    return 1;
				 }else if (pdf1 < pdf) {
					    return -1;
				 }else{ 
					return 0;	  
				  }
			}

			function sortUrlData(a, b) {
				debugger;
				
				var	imageNo=a.imageNo.split("image")[1];
				var	imageNo1=b.imageNo.split("image")[1];
				
				
				 if(imageNo < imageNo1) {
					    return 1;
				 }else if (imageNo1 < imageNo) {
					    return -1;
				 }else{ 
					return 0;	  
				  }
			} 
			

			function printPage(){
				var amountInWords1=	convertNumberToWords($("#TotalACCAmt").text().replace(/,/g,''));
				$("#amountInwordsInPrint").text(amountInWords1);
				window.print();
			}
			function openAbstract(){ 				
				$("#NmrAbstract").attr("data-toggle", "modal");
				$("#NmrAbstract").attr("data-target", "#NMRBillTableModal");
			}
			
			//this code for download server images
			function toDataURL(url, current) {
				debugger;
			    var httpRequest = new XMLHttpRequest();
			    httpRequest.onload = function() {
			       var fileReader = new FileReader();
			          fileReader.onloadend = function() {
			             console.log("File : "+fileReader.result);
							 $(current).removeAttr("onclick");
							 $(current).attr("href", fileReader.result);
							 $(current)[0].click();
							 $(current).removeAttr("href");
							 $(current).attr("onclick", "toDataURL('"+url+"', this)");
			          }
			          fileReader.readAsDataURL(httpRequest.response);
			    };
			    httpRequest.open('GET', url);
			    httpRequest.responseType = 'blob';
			    httpRequest.send();
			 }
			//this code for download server images	
			
			//this code for to active the side menu 
			var referrer=$("#pageType").val();
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
