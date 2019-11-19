<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<!-- <title>RA/ADV Bill </title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/WorkOrder/ShowCompletedRAAdv.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>

</head>

<style>
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
/* .table-new thead tr th:first-child, .table-new tbody tr td:first-child{min-width:20px;text-align:center;width:50px;} */
.table-new tbody tr td {border-top: 0px !important;}
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
/* fixed header table css for modal*/
.show_complete thead tr th{border:1px solid #000;padding:5px;background-color:#ccc;font-size:15px;font-weight:bold;text-align: center;}
.tbl-rabill thead tr th{font-weight:bold;font-size:15px;}
#paymentByArea{font-weight:bold;}
.scroll-rabill-tbl{
 width: calc(100% - 19px) !important;
 height:77px;
 margin-bottom:-2px;
}
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }
 
.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
.inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

}
.media-style{
width:39% !important;

}
.submitstyle{
margin-top: 20px;
width: 23% !important;
}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}
.td-active input{
background-color:#dbecf6;
}
.td-active{
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
.mrg-Top20{margin-top:20px;}
.input-outline input{
outline:none;
}
.table-border-certificateofpayment thead tr th, .table-border-certificateofpayment tbody tr td{
 border:1px solid #000;
 font-weight: bold;
}
.table-border-certificateofpayment{
border:1px solid #000;

}
.mrg-Top{
margin-top:5px;
}
@media print{
.nav_menu, .print_hide, .breadcrumb,#remarksDiv, .donotprint{
display:none;
} 

.print-tnlrowcolor{background-color:#dbecf6 !important;}
/* @page {size: landscape} */

.showraPrint{
display:none;
}
.scroll-rabill-tbl1{
display:none;
}
.tbl-payment-row{
display:none;
}
thead {
display: table-row-group;
}
.border-none{
border:none;
}

}/* @media print */


 
  /* css for iframe modal popup */
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}

.iframe-pdf {
  opacity: 1;
  display: block;
  width: 100%;
  height: auto;
  transition: .5s ease;
  backface-visibility: hidden;
}

.middle {
  transition: .5s ease;
  opacity: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  width:100%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
}

.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.modal-lg-width{
width:95%;
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
/*css for iframe modal popup*/
.heightthirty{
  height:30px;
}

</style>
<script>
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

<body class="nav-md">

 <div class="overlay_ims"></div>
	 <div class="loader-ims" id="loaderId" > <!--  -->
		<div class="lds-ims">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingimsMessage">Loading...</div>
	</div>
	
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
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item"><a href="#">View Contractor Bill </a></li>
						<li class="breadcrumb-item active">Certificate Of Payment</li>
					</ol>
				</div>
				<!-- Loader -->
				<div class="overlay_ims"></div>
			   <div class="loader-ims" id="loaderId" > <!--  -->
					<div class="lds-ims">
						<div></div><div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingimsMessage">Loading...</div>
			  </div>
				
       <form:form   modelAttribute="billBean"   id="contractorRABill" class="form-horizontal"  enctype="multipart/form-data" >
						<div class="col-md-12">
      <div class="table-responsive">
	  <!--Header part of table start--->
       <!--  <table class="table table-border-certificateofpayment" style="margin-bottom: -1px;"> -->
       <table  style="margin-bottom: -1px;border:1px solid #000;width:100%;" id="EmployeeTable1">
          <tbody>
            <tr>
              <td rowspan="2" style="width:20%;border:1px solid #000;padding:5px;"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;" /></td>
              <td  style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4></td>
            </tr>
             <c:set value="ADV" var="ADV"></c:set>
            <tr><td colspan="2" style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h5><strong>
       
            Certificate Of Payment
           </strong></h5></td></tr>
         	</tbody>
          
          </table>
		   <!---header part of table end--->
		  <!--details part of table start-->
     
          <table style="margin-bottom: -1px;border:1px solid #000;width:100%;font-weight:bold;"  id="EmployeeTable2">
          <tbody>
            <tr>
              <td style="width:50%;border:1px solid #000;padding:5px;">
              <div class="mrg-Top" style="width:100%;"><div class="mrg-Top" style="width:45%;float:left;"><strong>Contractor&nbsp;Name</strong></div><div class="mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top"style="width:45%;float:left;">${billBean.contractorName}<input type="hidden" class="heightthirty border-none" style="border:none;width: 100%" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" /></div></div>
			  <div class="clearfix"></div>
			  <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   />
			   <div class="mrg-Top" style="width:100%;"><div class="mrg-Top"style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class="mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  
			  <div class="mrg-Top" style="width:100%;"><div class="mrg-Top"style="width:45%;float:left;"><strong>Account No</strong> </div><div class="mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;">${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div class="mrg-Top" style="width:100%;"><div class="mrg-Top"style="width:45%;float:left;"><strong>IFSC & Bank Name</strong>  </div><div class="mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;"> 
              <c:set value="-" var="delimiter"></c:set>
              
              <c:choose>
              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
              </c:choose>
             
              
             </div></div>
			  
			  <div class="clearfix"></div>
              <div class="mrg-Top" style="width:100%;"><div class="mrg-Top"style="width:45%;float:left;"><strong>Mobile No  </strong></div><div class="mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class="heightthirty mrg-Top"style="width:45%;float:left;"> ${billBean.contractorPhoneNo}</div></div>
			  <div class="clearfix"></div> 
             
              </td>
              <td style="width:50%;border:1px solid #000;padding:5px;">
              
              <div class=""style="width:100%;">
              
			  <div class=" mrg-Top"style="width:45%;float:left;">Project</div><div class=" mrg-Top"style="width:10%;float:left;">:</div><div class=" mrg-Top"style="width:45%;float:left;">${billBean.siteName}<input type="hidden" class="heightthirty" value="${billBean.siteName}" style="border:none;" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			  </div>
			  <c:if test="${ not empty billBean.oldWorkOrderNo}">
			  <div  style="width:100%;"  class="donotprint">
			    <div class=" mrg-Top"style="width:45%;float:left;">Old Work Order No</div><div class=" mrg-Top"style="width:10%;float:left;">:</div><div class=" mrg-Top"style="width:45%;float:left;">
				${billBean.oldWorkOrderNo}
				</div>
				</div>
			  	</c:if>
			  	<div class=""style="width:100%;">
			    <div class=" mrg-Top"style="width:45%;float:left;">Work Order No</div><div class=" mrg-Top"style="width:10%;float:left;">:</div><div class=" mrg-Top"style="width:45%;float:left;">
			
			     <input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			     <input type="hidden" name="approvePage" id="approvePage" value="${approvePage}">
			     <input type="hidden" name="raPage" id="raPage" value="${raPage}">
			     <input type="hidden" name="permanentBillNo" id="permanentBillNo" value="${billBean.billNo}">
			     <input type="hidden" name="advPage" id="advPage" value="false">
			     <input type="hidden" name="billType" id="billType" value="${billType}">
			     <input type="hidden" name="BillNo" id="billNo" value="${billBean.billNo}">
				 <input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
				 <form:input type="hidden" path="isSiteWiseStatusPage" id="isSiteWiseStatusPage" value="${isSiteWiseStatusPage}"/>
				 <form:input path="approverEmpId"   type="hidden"  value="${nextLevelApprovedId }"/>
				 <form:input   type="hidden"  path="siteName" id="siteName"/>
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
				
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${billBean.tempBillNo } " readonly="readonly">
			    <input type="hidden" name="isRAStatusPage" id="isRAStatusPage" value=" ${isRAStatusPage} " readonly="readonly">
			    <input type="hidden" name="isAdvStatusPage" id="isAdvStatusPage" value=" ${isAdvStatusPage} " readonly="readonly">
			    <input type="hidden" name="userId" id="userId" value="${UserId}">
			 	<input type="hidden"  name="workOrderNo" id="workOrderNo" class="heightthirty border-none" readonly="readonly"  style="border:none;width: 100%" value="${billBean.workOrderNo}">
			 	${billBean.workOrderNo}
			    </div>
			    </div>
			
              	<div class="" style="width:100%;">
              	<div style="width:45%;float:left;"class="mrg-Top">Type Of Work</div><div style="width:10%;float:left;"class="mrg-Top">:</div><div style="width:45%;float:left;word-break: break-all;" class="mrg-Top"  id="typeOfWork"></div>
              	</div>
              	<div class="clearfix"></div>
              	<div class=""style="width:100%;">
              		<div style="width:45%;float:left;" class=" mrg-Top">Bill No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top"> ${billBean.billNo}</div>
              	</div>
              	<div class="clearfix"></div>
              	 <div class=""style="width:100%;">
              		<div style="width:45%;float:left;" class=" mrg-Top">Invoice No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top"> ${billBean.billInvoiceNumber}</div>
              	</div>
              	<div class="clearfix"></div>
              	 <c:if test="${ not empty billBean.oldWorkOrderNo}">
	              	<div  style="width:100%;" class="donotprint">
		              	<div class="mrg-Top" style="width:45%;float:left;">Old WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;"  id="workOrderAMount1">${billBean.oldWorkOrderTotalAmount}</div>
		            </div>
	            </c:if>
	            <div class="" style="width:100%;">
	              	<div class="mrg-Top" style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;"  id="workOrderAMount">${billBean.totalAmount}</div>
	            </div>
              <div class="" style="width:100%;">
             <div class=" mrg-Top"style="width:45%;float:left;">Bill Date</div><div class=" mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;">${billBean.paymentReqDate}<input  type="hidden" class="heightthirty border-none" value="${billBean.paymentReqDate}" name="billDate" id="billDate" style="border:none;display:inline;"  readonly="readonly" /></div>
           
              </div>
              </td>
            </tr>
          </tbody>
        </table>
        
        <table  style="margin-bottom:25px;width:100%;border:1px solid #000;font-weight:bold;">
          <thead class="thead-color-certificateofpayment">
            <tr>
              <th rowspan="2" class="text-center vertical-alignment" style="width:5%;border:1px solid #000;">S.NO</th>
              <th rowspan="2" class="text-center vertical-alignment" style="width:45%;border:1px solid #000;">Description</th>
              <th colspan="3" class="text-center" style="width:50%;border:1px solid #000;">Amount</th>
            </tr>
            <tr>
              <th class="text-center"style="border:1px solid #000;">Cumulative Certified </th>
              <th class="text-center"style="border:1px solid #000;">Previous Certified</th>
              <th class="text-center"style="border:1px solid #000;">Current Certified</th>
            </tr>
          </thead>
          <tbody>
            <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"><strong>I</strong></td>
              <td class="text-center td-active border-none input-outline" style="border:1px solid #000;padding:5px;"><span>Total Value of Work Completed / Certified</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raCc" id="raCc" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;" value="${billBean.totalCurrentCerti}" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raPc" id="raPc" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;" value="0" readonly/></td><%-- ${billBean.totalCurrentCerti} --%>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;" > <input name="ACTUUAl" type="hidden" value="${billBean.paybleAmt}">  <input type="text"  name="raAmountToPay" id="raAmountToPay" class="addFractionAndMakeInrFormat"   style="border:none; text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td><!--  ${billBean.totalCurrentCerti}amount to pay-->
            </tr>
            <tr>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
            </tr>

          <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (A)</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalCc" name="raTotalCc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalPc" name="raTotalPc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualRaAmountToPay" id="actualRaAmountToPay" value="${billBean.totalCurrentCerti }">  <input type="text" id="totalCc" name="totalCc" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td> <!-- ${billBean.totalCurrentCerti} -->
            </tr>
           <tr>
           <c:set value="ADV" var="ADV"></c:set>
           <c:set value="SEC" var="SEC"></c:set>
           <c:set value="PETTY" var="PETTY"></c:set>
           <c:set value="OTHER" var="OTHER"></c:set>
              <c:set value="RECOVERY" var="RECOVERY"></c:set>
              <c:forEach items="${deductionList }" var="deducList">
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq ADV}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="raDeductionAmt"></c:set>
            </c:if>
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq SEC}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="secDepositCurrentCerti"></c:set>
            </c:if>
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
       
             <c:forEach items="${previousPaidAmount}" var="prevPaidAmt">
                  <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq ADV}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="raeductionPrevCumulative"></c:set>
            </c:if>
            <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq SEC}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="secDepositPrevCerti"></c:set>
            </c:if>
             <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq PETTY}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="pettyExpensesPrevCerti"></c:set>
            </c:if>
            <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq OTHER}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="otherAmtPrevCerti"></c:set>
            </c:if>
            </c:forEach>
              <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">II</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Paid Amount</span></td>
              
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" class="raPaidAmnt addFractionAndMakeInrFormat" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
           <tr>
           
              <td class="text-center" style="border:1px solid #000;padding:5px;">a)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Advance Deduction</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raeductionCumulative" id="raeductionCumulative" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raePrevductionCumulative" id="raeductionPrevCumulative" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="${raeductionPrevCumulative }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt"> <input type="text"  name="raDeductionAmt" id="raDeductionAmt" class="addFractionAndMakeInrFormat" autocomplete="off"  onfocusout="raCalcAmountToPay(contractorRABill.raAmountToPay.value,2)"  style="border:none;text-align:center;" value="${raDeductionAmt }" readonly/></td><!-- deduction amt here -->
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">b)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Security Deposit(E)</span><span id="securityPer"></span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositCumulative" id="secDepositCumulative" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositPrevCerti" id="secDepositPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="${secDepositPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" > <input type="text"  name="secDepositCurrentCerti" class="addFractionAndMakeInrFormat" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="${secDepositCurrentCerti}" readonly/></td>
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">c)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Petty Expenses</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCumulative" id="pettyExpensesCumulative" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesPrevCerti" id="pettyExpensesPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="${pettyExpensesPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti" class="addFractionAndMakeInrFormat"  autocomplete="off"   style="border:none;text-align:center;" value="${pettyExpensesCurrentCerti }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Others</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtCumulative" id="otherAmtCumulative" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtPrevCerti" id="otherAmtPrevCerti" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="${otherAmtPrevCerti}" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="other" id="other"  autocomplete="off" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="${other }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;" ><input type="text"  name="cumulativeRecovery" class="CcAmnt addFractionAndMakeInrFormat" id="sumOfCumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;" ><input type="text"  name="previousRecovery" class="PcAmnt addFractionAndMakeInrFormat" id="sumOfPreviousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span id="currentRecoveryAmount">${recoveryAmount}</span>   <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount}"> <a class="print_hide" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
			 </tr>			
             <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (B)</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulative" id="totalAmtCumulative" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtPrevCerti" id="totalAmtPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti+raDeductionAmt}" name="actualTotalDeductAmt" id="actualTotalAmtCurrnt"> <input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="${secDepositCurrentCerti+raDeductionAmt+other+pettyExpensesCurrentCerti+recoveryAmount}" readonly/></td>
    		 </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">III</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Outstanding advance (F)</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvCurrent" id="outstandingAdvCurrent" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">IV</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Release advance (G)</span> </td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt" class="addFractionAndMakeInrFormat"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount" class="addFractionAndMakeInrFormat" autocomplete="off"  style="border:none;text-align:center;" value="" onfocusout="calculateAdvBillAmt(this.value)" readonly/></td>
            </tr>
			<tr>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
            </tr>
			<tr style="font-size: 15px;" class="print-tnlrowcolor">
			 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Net Payable Amount (A - B + G) = C</span></td>
			 <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" class="addFractionAndMakeInrFormat" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${billBean.paybleAmt}" id="actualFinalAmt" name="actualFinalAmt">  <input type="text" name="finalAmt" id="finalAmt" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.paybleAmt}" readonly></td>
			</tr>
			<tr style="font-size: 15px;" class="print-tnlrowcolor">
			 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords" style="border:1px solid #000;padding:5px;"></td>
			 <!-- <td  class="text-center td-active"></td>
			 <td  class="text-center td-active"></td> -->
			</tr>
			<tr class="payment-areaprint donotprint">
			<td colspan="2" class="text-center" style="border:1px solid #000;padding:5px;">Payment Area</td>
			<td colspan="3" class="text-center" style="border:1px solid #000;padding:5px;"><a class="certificate-link" href="javascript:void(0);" id="certificate-id"  data-toggle="modal" data-target="#modal-approverabill" >Abstract</a></td><!-- data-toggle="modal" data-target="#modal-showraadvbill" -->
			</tr>
			<tr id="EmployeeSignRow" style="">
			<td colspan="5" class="text-center" style="border:1px solid #000;padding:5px;">
				 <div style="width:100%;height:50px;">
				<div style="width:33.3%;float:left;">
				 </div>
				 <div style="width:33.3%;float:left;">
				
				 </div>
				 <div style="width:33.3%;float:left;">
				
				 </div>
				 </div>
				 <div style="width:100%;">
					 <!--  <div style="width:33.3%;float:left;"><strong>QS</strong>
					  </div>
					  <div style="width:33.3%;float:left;"><strong>Project Manager</strong>
					  </div>
					  <div style="width:33.3%;float:left;"> <strong>Site Incharge</strong>
					  </div> -->
					
					  <c:set value="1" var="countVerifiedEmpNames"></c:set>
					   <c:set value="END" var="workOrderStatus"/>
					   <c:set value="${100/listOfVerifiedEmpNames.size()}" var="dynamicWidth"></c:set>
					   <c:if test="${listOfVerifiedEmpNames.size()==1}">
						   <c:set value="${100/2}" var="dynamicWidth"></c:set>
					   </c:if>
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
						    <c:when test="${countVerifiedEmpNames eq listOfVerifiedEmpNames.size() && nextLevelApprovedId eq workOrderStatus}">
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
			</td>
			</tr>
			<!--  <tr id="ContractorSignRow" style="display: none;">  
               <td colspan="5" style="border:1px solid #000;padding:5px;">
               <div style="width:100%;margin-top:50px;"></div>
               <div style="width:100%;">
               <div style="width:33.3%;float:right;"></div>
               <div style="width:33.3%;float:right;"></div>
               <div style="width:33.3%;float:right;text-align:center;">Contractor Sign</div>
               </div>
              </td>
            </tr>-->
          </tbody>
        </table>
  </div>
    </div>    
     <div class="col-md-12 text-center center-block showraPrint">
      <c:if test="${isBillUpdatePageRequest ne true }">
        <input type="button" id="printshowRa" class="btn btn-warning" value="Print" onclick="showraPrint()">
    	<input type="button" name="contractorCopy" id="contractorCopy"   class="btn btn-warning btn-custom-width"  value="Contractor Copy" />
    	<input type="button" name="abstractPrint" id="abstractPrintBtn" class="btn btn-warning"  value="Print Abstract"/>
	   	<input type="button" name="billLedger" id="billLedger"   class="btn btn-warning btn-custom-width"  value="Ledger" />
	   	<input type="button" name="submitBtn"  onclick="return closeView()"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
	 	
	 	<input type="hidden" value="viewCompletedBills.spring" id="pageType">
	 </c:if>
	 <c:if test="${isBillUpdatePageRequest eq true }">
	 	<input type="hidden" value="updateContractorBills.spring" id="pageType">
	</c:if>
	</div> 
    <br>
	<!-- ***********************************************this is for pdf file download start******************************************************** -->

      <div class="col-md-6 Mrgtop10" style="width:50%;" id="remarksDiv">
			<label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
			<div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
			<input type="hidden" name="actualremarks" value="${woLevelPurpose}"> 
		 		<textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title="${woLevelPurpose}"  autocomplete="off" placeholder="<c:out value='${woLevelPurpose}'></c:out>"></textarea> 
			</div>
		</div>
 

<div class="col-md-12 print_hide" style="margin-top: 10px;">
	<!-- <h3>You can see the PDF</h3> -->
<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
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
				<div class="col-md-12 Mrgtop20 print_hide">
	
			<!-- 	<h3>You can see the Images</h3> -->
						<%
										int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
										int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
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
							<input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfCount%>">
							
							<!--if isBillUpdatePage equal true then this page for Update Permanent bills  -->
							<c:if test="${isBillUpdatePageRequest eq true }">
							
								<div class="col-md-12">
										<div class="file-upload color-head-file" id="ishidden">
												<c:if test="${(imagecount+pdfCount)<8}"><h3>You can upload Images/PDFs here :</h3>
										</c:if>
											<%
												for (int i = 0; i < imagecount; i++) {
															out.println("<div id=\"fileupload" + i
																	+ "\" style=\"display:none;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'   name=\"file\"></div>");
														}
											%>
											<%
												for (int i = (imagecount+pdfCount); i < 8; i++) {
															out.println("<div id=\"fileupload" + i
																	+ "\" style=\"display:block;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'  name=\"file\"></div>");
														}
											%>
										</div>
									</div>
									
									</c:if>
		
		</div>
		
		
<c:if test="${isBillUpdatePageRequest eq true }">
<div class="col-md-12 text-center center-block">
   	
    <input type="button" name="updateBill" id="updateBill" class="btn btn-warning"  value="Update Bill"/>
    <input type="button" name="submitBtn"  onclick="return closeView()"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
 </div>
 
 </c:if>
		
		
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
          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  <%} %>
		<!-- modal popup for invoice image end --> 
		

   <div class="col-md-12 mrg-Top20" style="display:none;" id="hideIt">
  <div class="table-responsive scroll-rabill-tbl1">
   <!-- <table class="table table-bordered tbl-rabill"> -->
   <table style="border:1px solid #000;width:100%;text-align:center;" class="show_complete">
   <thead>
  <tr>
			 <th>Date</th>
		     <th>Particular</th>
		     <th>Bill Amount</th>
		     <th>Advance</th>
		     <th>Advance Deduction</th>
		     <th>SD Release</th>
		     <th>Material Deductions</th>
		     <th>Petty Expenses</th>
		     <th>Other Amount</th>
		     <th>Hold Amount</th>
		     <th>Hold Release</th>
		     <th>SD 5%</th>
		     <th>Amount Paid</th>
		     <th>Cumulative Total</th>
		</tr>
   </thead>
   <tbody id="appendBillDetails" style="border:1px solid #000;font-weight:bold;">
  
   </tbody>
   <tfoot style="background-color: #ccc;border: 1px solid #000;text-align: center;font-weight:bold;" id="appendBillDetailsFooter">
    
   </tfoot>
   
   </table>
  </div>
 </div>
   <div id="appendBillDetailsTotal" class="print_hide" style="float:  right;margin-bottom: 30px;"></div>
 
 <div id="appendWorkAreaId">
    
 </div>
  
  
    
</form:form>

				</div>
                  
  </div>
  </div>
 
<div id="modal-approverabill" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg modal-rabill-customwidth">
	  <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center" style="font-weight:1000;">Abstract</h4>
      </div>
     
      <div class="modal-body "> <!-- modal-body-scroll -->
       <div class="table-responsive">  <!-- scroll-rabill-tbl -->
        <table class="table table-new">  <!-- tbl-rabill    style="margin-bottom:-1px;" -->
        <thead class="cal-thead-inwards">
         <tr>
         	  <th rowspan="2" class="vertical-alignment text-center" style="width:271px;">Description of Work</th>
              <th rowspan="2" class="vertical-alignment text-center">Total Quantity</th>
              <th rowspan="2" class="vertical-alignment text-center">Rate</th>
              <th rowspan="2" class="vertical-alignment text-center">Unit</th>
              <th colspan="2" class="text-center">Cumulative Certified</th>
              <th colspan="2" class="text-center">Previous Certified</th>
              <th colspan="2" class="text-center">Current Certified</th>
            </tr>
            <tr>
              <th class="text-center">Quantity </th>
              <th class="text-center">Amount</th>
              <th class="text-center">Quantity </th>
              <th class="text-center">Amount</th>
              <th class="text-center">Quantity </th>
              <th class="text-center">Amount</th>
            </tr>
        </thead>
      <!--   </table>
       </div>
       <div style="height: auto;max-height: 300px;overflow-y:scroll;">
       <table class="fixed-header-tbl"> -->
        <tbody id="paymentByArea" class="tbl-fixedheader-tbody">

        </tbody>
        </table>
        
       </div>
      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>

  </div>
</div>
<!-- Modal popup for approve RABill End -->

 
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
          <th>S.No</th>
          <th>Description Of material</th>
          <th>Total Quantity Consumed</th>
          <th>Rate</th>
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
         <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>

  </div>
</div>
 <!-- modal popup for recovery click action end -->
  </body>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
    <script>
					
			$(document).ready(function() {
				
				if(window.history.length==1){
					$("#closeBtn").hide();
				}
				var billType=$("#billType").val();
				if(billType=="ADV"){
					$("#raPc").val("");
					$("#raTotalPc").val("");
					$("#raCc").val("");
					$("#raAmountToPay").val("");
					$("#totalCc").val("");
					$("#totalActualDeductAmt").val("");
				}
				$(".up_down").click(  function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});

				});

					$("#certificate-id").click(function(){
						var ContractorId=$("#ContractorId").val();
						var workOrderNo=$("#workOrderNo").val();
					
						if(ContractorId.length==0){
							$("#ContractorName").focus();
							alert("Please enter contractor name.");
							return false;
						}
				
						if(workOrderNo.length==0){
							alert("Please select work order number.");
							return false;
						}
						
						 $("#tbl-2").show();
					 });
					
					$("#checkAll").change(function () {
					    $("input:checkbox").prop('checked', $(this).prop("checked"));
					});
					
					var billType=$("#billType").val();

					function showraPrint(){
						window.print();
						
					}
					//here just sending url for deleting
					function deleteFile(divId,url){
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
					//deleting Work order image and file
					function deleteWOImageFile(divId,imagePath){	 
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
					//deleting pdf file
					function deletepdf(divId,imagePath){
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
					//iterating deleted  file
					function excuteDeleteFunction(){
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
						 
						var len=deleteUploadedimageFiles.length;
						len=len+deleteUploadedPDFFiles.length;
						var timeOut=len*250;
						setTimeout(function(){
							 var url="updateContractorBill.spring";
							 document.getElementById("contractorRABill").action =url;
						     document.getElementById("contractorRABill").method = "POST";
						     document.getElementById("contractorRABill").submit();	
						}, timeOut) 
					}
					//sorting data by number's if String having pdf1,pdf0 spliting with pdf as delimeter
					function sortPdfData(a,b){
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
					//sorting images 
					function sortUrlData(a, b) {
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
					
					//to ditroy loaders
					$(window).load(function() {debugger;
						 //$(".overlay_ims").hide();	
						 //$(".loader-ims").hide();
					});
					
					//this code for to active the side menu 
					  var referrer=$("#pageType").val();
					  var isSiteWiseStatusPage="${billBean.isSiteWiseStatusPage}";
						if(isSiteWiseStatusPage=="true"){
							referrer="viewSitewiseCompletedBills.spring";
						}
						 $SIDEBAR_MENU.find('a').filter(function () {
					        var urlArray=this.href.split( '/' );
					        for(var i=0;i<urlArray.length;i++){
						     	 if(urlArray[i]==referrer) {
						     		 return this.href;
						     	 }
					        }
						 }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
</script>
</html>
