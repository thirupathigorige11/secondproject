 <!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>

<!-- <title>Approve RA Bill</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">


<jsp:include page="./../CacheClear.jsp" />  

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
</head>

<style>
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new tbody tr td:first-child{min-width:20px;text-align:center;width:50px;}
.table-new tbody tr td {border-top: 0px !important;}
/*css code for print */
@media print{
.nav_menu, .print_hide{
display:none;
}
.modal_recovery_class{display:none;}
.border-none{border:0px !important;}
.btn-print-row{
display:none;}
thead {
display: table-row-group;
}
}
/*css code for print */
/* fixed header table css for modal*/
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:70px;
}
/* .scroll-rabill-tbl1{
 width: calc(100% - 17px) !important;
 height:70px;
} */
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }
 
 
/* fixed header table css for modal*/
.modal-body-scroll{
max-height:450px;
overflow-y:auto;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}
.scroll-rabill-tbl>table>thead>tr>th{
border:1px solid #000;
background-color:#ccc;
color:#000;
border-top:1px solid #000 !important;
text-align:center;
}
.scroll-rabill-tbl>table>tbody>tr>td{
border:1px solid #000;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}

.rabill-class-select{
width:130px;
}
.mrg-Top{
margin-top:5px;
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
/* written by thirupathi */
.deduction{
	width: 200px;
	 text-align:center;
	/*  padding-left:8%; */
	 margin-left:30%;
}
.deduction .lst{
  text-align:center;
}
.hidebtn1{
    visibility: hidden;
}
.deletebtn{
	margin-left:2%;
}
.heightthirty{
  height:30px;
}
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("approveContractorBills.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>

<body class="nav-md">
	 <div class="overlay_ims"></div>
	 <div class="loader-ims" id="loaderId"> <!--  -->
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
					<li class="breadcrumb-item active">Certificate Of Payment</li>
				</ol>
			</div>
       <form:form   modelAttribute="billBean"   id="contractorRABill" class="form-horizontal" >
						<div class="col-md-12">
      <div class="table-responsive">
	  <!--Header part of table start--->
        <table style="margin-bottom: -1px;border:1px solid #000;width:100%;"> <!-- class="table table-border-certificateofpayment" -->
          <tbody style="border:1px solid #000;">
            <tr>
              <td rowspan="2" style="width:20%;padding:6px;border:1px solid #000;"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;" /></td>
              <td  style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4></td>
            </tr>
            <tr><td colspan="2" style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h5><strong>Certificate Of Payment</strong></h5></td></tr>
         	</tbody>
          
          </table>
		   <!---header part of table end--->
		  <!--details part of table start-->
        <!-- <table class="table table-border-certificateofpayment tbl-border-top tbl-border-bottom"style="margin-bottom: 0px;" > -->
        <table style="margin-bottom: -1px;margin-top: 0px;border:1px solid #000;width:100%;font-weight: bold;" >
          <tbody>
            <tr>
              <td style="width:50%;padding:15px;border:1px solid #000;">
              <div style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Contractor&nbsp;Name</strong></div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div class="" style="width:45%;float:left;"><input type="text" class="heightthirty border-none" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" readonly="readonly" style="border:none;width:100%;"  onkeyup="return populateData(this, event);"  autocomplete="off"   /><input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   /></div></div>
			  <div class="clearfix"></div>
			   <div class="mrg-Top" style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class=""style="width:10%;float:left;"><strong>:</strong></div><div  class="heightthirty"  style="width:45%;float:left;">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  
			  <div class="mrg-Top"style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Account No</strong> </div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div  class="heightthirty"  style="width:45%;float:left;">${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top"style="width:100%;"><div class="" style="width:45%;float:left;"><strong>IFSC & Bank Name</strong>  </div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div  class="heightthirty"  style="width:45%;float:left;">
              
              
            <c:set value="-" var="delimiter"></c:set>
              
              <c:choose>
              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
              </c:choose>
              
              
              </div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top"style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Mobile No  </strong></div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div  class="heightthirty"  style="width:45%;float:left;"> ${billBean.contractorPhoneNo}</div></div>
			  <div class="clearfix"></div> 
             	
              </td>
              <td style="width:50%;padding:5px;">
             
              <div class="" style="width:100%;">
			  <div class=""style="width:45%;float:left;">Project</div><div class=""style="width:10%;float:left;">:</div><div class=""style="width:45%;float:left;"><input type="text" value="${SiteName}" class="" style="border:none;" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			    	<!-- <div  class="col-md-4"></div>	<div class="col-md-1"></div>	<div class="col-md-6"> -->
			 	<%-- <input type="hidden" name="workDesc" id="workDesc" value="${billBean.typeOfWork}" readonly="readonly"><br> --%>	
			 </div>
			     <div class="mrg-Top" style="width:100%;">
			    <div class="mrg-Top"style="width:45%;float:left;">Work order No</div><div class="mrg-Top "style="width:10%;float:left;">:</div><div class=""style="width:45%;float:left;">
			    <!-- <input type="text" id = "comboboxworkOrderNo"  /> -->
			    <input type="hidden" name="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			    <input type="hidden" value="${billBean.permanentBillNo }" name="permanentBillNo" id="permanentBillNo">
			    <input type="hidden" name="approvePage" id="approvePage" value="true">
			    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
			    <input type="hidden" name="advPage" id="advPage" value="false">
			   	<%-- <input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }"> --%>
			    <input type="hidden" name="billType" id="billType" value="RA">
			   	<input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
			    <input type="hidden" name="billType" id="billType" value="${billType}">
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }" >
			    <input type="hidden" name="typeOfWork" id="typeOfWork" value="${billBean.typeOfWork}">
			    <input type="hidden" name="isCommonApproval" id="isCommonApproval" value="${isCommonApproval}">			   
			    <input type="text"  name="workOrderNo" class="heightthirty border-none" id="workOrderNo" readonly="readonly" style="border:none;width: 100%;"  value="${billBean.workOrderNo}">
			    </div>
			    </div>              	
              	<div class="" style="width:100%;">
              	<div class=" mrg-Top"style="width:45%;float:left;">Type Of Work</div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;"><p id="paymentTypeOfWork" style="height:20px;word-break: break-all;"></p></div>
              	</div>
              	 <div class="mrg-Top" style="width:100%;">
					<div class="mrg-Top"style="width:45%;float:left;">Bill No </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class=""style="width:45%;float:left;"><input type="text" name="raBillNo" class="heightthirty border-none" id="raBillNo" style="display:inline;border:none;"  value="${billBean.permanentBillNo}"   readonly="readonly"/></div>
              	 </div>
              	 <!-- <div class="clearfix"></div> -->
              	 <div class="mrg-Top" style="width:100%;">
					<div class="mrg-Top"style="width:45%;float:left;">Invoice No</div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class=""style="width:45%;float:left;"><input type="text" name="billInvoiceNo" class="heightthirty border-none" id="billInvoiceNo" style="display:inline;border:none;"  value="${billBean.billInvoiceNumber}"   readonly="readonly"/></div>
              	 </div>
              	 <div class="" style="width:100%;">
              	<div class="mrg-Top"style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;"><input type="text" name="totalAmtToPay" class="heightthirty border-none" id="totalAmtToPay" value="${billBean.totalAmount}" style="display:inline;border:none;"  readonly="readonly" /></div>
              </div>
               <div class="" style="width:100%;">
              <div class="mrg-Top"style="width:45%;float:left;">Bill Date</div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;"><input  type="text" value="${billBean.paymentReqDate}" class="heightthirty border-none" name="billDate" id="billDate" style="display:inline;border:none;"  readonly="readonly" /></div>
           </div>
         </td>
        </tr>
      </tbody>
     </table>
		<!--details part of table end-->
		<!--information table start--->
       <!--  <table class="table table-border-certificateofpayment tbl-border-top" style="margin-bottom:25px;"> -->
       <table  style="margin-bottom:25px;border:1px solid #000;width:100%;font-weight: bold;">
          <thead class="thead-color-certificateofpayment">
            <tr>
              <th rowspan="2" class="text-center vertical-alignment" style="width:5%;border:1px solid #000;padding:5px;">S.NO</th>
              <th rowspan="2" class="text-center vertical-alignment" style="width:45%;border:1px solid #000;padding:5px;">Description</th>
              <th colspan="3" class="text-center" style="width:50%;border:1px solid #000;padding:5px;">Amount</th>
            </tr>
            <tr>
              <th class="text-center"style="border:1px solid #000;padding:5px;">Cumulative Certified </th>
              <th class="text-center"style="border:1px solid #000;padding:5px;">Previous Certified</th>
              <th class="text-center"style="border:1px solid #000;padding:5px;">Current Certified</th>
            </tr>
          </thead>
          <tbody>
            <tr style="font-size: 15px;">
              <td class="text-center"style="border:1px solid #000;padding:5px;"><strong>I</strong></td>
              <td class="text-center td-active border-none input-outline"style="border:1px solid #000;padding:5px;"><span  name="" id="" >Total Value of Work Completed / Certified</span></td>
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  name="raCc" id="raCc" style="border:none; text-align:center;" value="" readonly/></td><!-- ${billBean.totalCurrentCerti}  -->
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  name="raPc" id="raPc" style="border:none; text-align:center;" value="" readonly/></td><!-- ${billBean.totalCurrentCerti} -->
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"> <input name="ActualCertified" id="ActualCertified" type="hidden" value="${billBean.totalCurrentCerti}"><input name="ACTUUAl" type="hidden" value="${billBean.paybleAmt}">  <input type="text"  name="raAmountToPay" id="raAmountToPay" onkeyup="CurrentCertifiedamnt()" readonly  style="border:none; text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td><!--  amount to pay onkeyup="CurrentCertifiedamnt()"-->
            </tr>
           
             <tr>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
            </tr>
         <tr style="font-size: 15px;">
              <td class="text-center"style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (A)</span></td>
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalCc" name="raTotalCc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalPc" name="raTotalPc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline"style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualRaAmountToPay" id="actualRaAmountToPay" value="${billBean.totalCurrentCerti }">  <input type="text" id="totalCc" name="totalCc"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td>
           <c:set value="ADV" var="ADV"></c:set>
           <c:set value="SEC" var="SEC"></c:set>
           <c:set value="PETTY" var="PETTY"></c:set>
           <c:set value="OTHER" var="OTHER"></c:set>
           <c:set value="RECOVERY" var="RECOVERY"></c:set>
           
            <c:forEach items="${deductionList }" var="deducList">
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq ADV}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="raDeductionAmt"></c:set>
                  <input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt">
            </c:if>
              <c:if test="${deducList.TYPE_OF_DEDUCTION eq SEC}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="secDepositCurrentCerti"></c:set>
                  <input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" >

            </c:if>
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq PETTY}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="pettyExpensesCurrentCerti"></c:set>
                  <input type="hidden" value="${secDepositCurrentCerti}" name="changedsecpettyExpensesCurrentCerti" id="changedsecpettyExpensesCurrentCerti" >
		    </c:if>
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq OTHER}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="other"></c:set>
                      <input type="hidden" value="${secDepositCurrentCerti}" name="changedOther" id="changedOther" >
      		   </c:if>
               <c:if test="${deducList.TYPE_OF_DEDUCTION eq RECOVERY}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="recoveryAmount"></c:set>
            </c:if>
            </c:forEach> 
            </tr>
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
              <td class="text-center"  style="border:1px solid #000;padding:5px;">II</td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;"><span name="" id="" >Paid Amount</span></td>              
              <td class="text-center"  style="border:1px solid #000;padding:5px;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"  style="border:1px solid #000;padding:5px;"><input type="text" class="raPaidAmnt" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
            
           
           <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">a)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;width: 40%;"><span name="" id="">Advance Deduction</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raCcDeductionAmt"  class="CcAmnt" id="raCcDeductionAmt"  style="border:none;text-align:center;" value="" readonly/> <input type="hidden" id="raCcDeductionAmthidden" value=""></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raePrevductionCumulative" class="PcAmnt" id="raPrevDeductionAmt"  style="border:none;text-align:center;" value="${raeductionPrevCumulative}" readonly/> </td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt"> <input type="text"  name="raDeductionAmt" class="form-control heightthirty raDeductionAmt" id="raDeductionAmt" autocomplete="off"  onkeyup="caldeductionamt('ra')"   onkeypress="return isNumber(this, event)" style="text-align:center !important;" value="${raDeductionAmt }"/></td><!-- deduction amt here -->
            </tr>
           
			<tr><%-- ${raeductionPrevCumulative } --%>
              <td class="text-center"style="border:1px solid #000;padding:5px;">b)</td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><span>Security Deposit(E)</span>
              <select name="securityPer" id="securityPer" class="border-none"onchange="securityperchange()">
              <option value="0">0%</option><option value="1">1%</option><option value="2">2%</option> <option value="3">3%</option><option value="4">4%</option><option value="5">5%</option>
                <option value="6">6%</option><option value="7">7%</option><option value="8">8%</option> <option value="9">9%</option><option value="10">10%</option>

              </select></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositCumulative" class="CcAmnt" id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositPrevCerti" id="secDepositPrevCerti" class="PcAmnt" style="border:none;text-align:center;" value="${secDepositPrevCerti }" readonly/></td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" > <input type="text" class="raDeductionAmt" name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="${secDepositCurrentCerti}" readonly/></td>
            </tr>
			<tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;">c)</td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><span  name="">Petty Expenses</span></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt" id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="${pettyExpensesPrevCerti }" readonly/></td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="text"  class="form-control heightthirty raDeductionAmt" name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off"  style="text-align:center !important;" value="${pettyExpensesCurrentCerti }" onkeyup="caldeductionamt('pe')" onkeypress="return isNumber(this, event)" />
              <input type="hidden" name="actualPettyExpensesCurrentCerti" value="${pettyExpensesCurrentCerti }"> </td>
            </tr>
             <tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center  input-outline"style="border:1px solid #000;padding:5px;"><span  name="" id="">Others</span></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtCumulative"  class="CcAmnt" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="${otherAmtPrevCerti }" readonly/></td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="text" class="form-control heightthirty raDeductionAmt" name="other" id="other"  autocomplete="off"  style="text-align:center !important;" value="${other}" onkeyup="caldeductionamt('ot')" onkeypress="return isNumber(this, event)"/>
              <input type="hidden" id="actualOtherAmt" name="actualOtherAmt" value="${other}">  </td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;"><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;" ><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" id="currentRecoveryAmount" class="raDeductionAmt" value="${recoveryAmount}"  style="border:none;text-align:center;width:20%;"  readonly="readonly"  > <input  type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount}"><input type="hidden" name="actualrecoverycurrentAmount" id="actualrecoverycurrentAmount" value="${recoveryAmount}"> <a class="modal_recovery_class" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
			  </tr>
           <tr style="font-size: 15px;">
              <td class="text-center"style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline"style="border:1px solid #000;padding:5px;"><span>Total Amount (B)</span></td>
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulative" id="totalDeductionAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtPrevCerti" id="totalDeductionAmtPrevCerti"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline"style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti+raDeductionAmt}" name="actualTotalDeductAmt" id="actualTotalAmtCurrnt"> <input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc"  style="border:none;text-align:center;" value="${secDepositCurrentCerti+raDeductionAmt+other+pettyExpensesCurrentCerti+recoveryAmount  }" /></td>
    		  </tr>

			<tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;">III</td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><span>Outstanding advance (F)</span></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvCurrent" id=outstandingAdvCurrent  autocomplete="off"  style="border:none;text-align:center;" value=""/></td>     
            </tr>
			<tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;">IV</td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><span>Release advance (G)</span></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"style="border:1px solid #000;padding:5px;"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount"  autocomplete="off"  style="border:none;text-align:center;" value="" onfocusout="calculateAdvBillAmt(this.value)"/></td>
            </tr>
			<tr>
			<td style="padding:15px;border:1px solid #000;"></td>
			<td style="padding:15px;border:1px solid #000;"></td>
			<td style="padding:15px;border:1px solid #000;"></td>
			<td style="padding:15px;border:1px solid #000;"></td>
			<td style="padding:15px;border:1px solid #000;"></td>
			</tr>
			<tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"style="border:1px solid #000;padding:5px;"><span>Net Payable Amount (A - B + G) = C</span></td>
			 <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${billBean.paybleAmt}" id="actualFinalAmt" name="actualFinalAmt">  <input type="text" name="finalAmt" id="finalAmt"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.paybleAmt}" readonly></td>
			</tr>
			<tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"style="border:1px solid #000;padding:5px;"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords"style="border:1px solid #000;padding:5px;"></td>			
			</tr>			
			<tr>
			<td colspan="2" class="text-center" style="border:1px solid #000;padding:5px;">Payment Area</td>
			<td colspan="3" class="text-center" style="border:1px solid #000;padding:5px;"><a class="certificate-link" href="javascript:void(0);" id="certificate-id" data-toggle="modal" data-target="#modal-approverabill">Abstract</a></td>
			</tr>
		 
          </tbody>
        </table>
       </div>
    </div>    
     <div class="col-md-12 text-center center-block btn-print-row">
     <!-- <input type="button" name="printbtn" id="printBtnapproveRa" class="btn btn-warning btn-custom-width" id="PrintBtnApproveRA"	value="Print" onclick="myPrint();"/> -->
	   	<input type="button" name="submitBtn" id="approveRABtn" onclick="return approveRABill()"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Approve" />
	   	<input type="button" name="submitBtn" id="rejectRABtn" onclick="return rejectRABill()" class="btn btn-warning btn-custom-width" id="closeBtn"	value="Reject" />
    </div> 
    <br>
   <div class="print_hide">
   <div style="width:100%;" id="showHideRemarks">
     <div class="col-md-6 Mrgtop10" style="width:50%;">
			<label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
			<div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
			<input type="hidden" name="actualremarks" value="${woLevelPurpose}">
		 		<textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title=" <c:out value='${woLevelPurpose}'></c:out>" placeholder=" <c:out value='${woLevelPurpose}'></c:out>"  id="NoteId"  autocomplete="off"></textarea> 
			</div>
		</div>
	
   </div>

		
	<div class="col-md-12 Mrgtop10" id="ModificationDetails">
	<c:if test="${changedListDtls.size()!=0 }">
			<label class="control-label col-md-2" >Modification Details : </label>
			</c:if>
		<div class="col-md-6" style="    position: relative;margin-bottom: 10px;" >
			<c:forEach items="${changedListDtls}" var="chngedDtls">
			${chngedDtls.REMARKS}	
			</c:forEach>
		</div>
	</div>
	
	<!-- this is common file for showing images  -->
	<%@include file="ImgPdfCommonJsp.jsp" %>
	 
    <div id="appendActualAreaDetails"></div> 
    <div id="appendWorkAreaId"></div>
    <div id="recoveryAmountDetails"></div>
</div>	
    
</form:form>
<div class="print_hide">
<div class="clearfix"></div>
 <div class="col-md-12 mrg-Top" id="hideIt">
  <div class="table-responsive scroll-rabill-tbl1">
   <!-- <table class="table table-bordered tbl-rabill"> -->
   <table style="border:1px solid #000;width:100%;padding:5px;">
   <thead>
   <tr>
			 <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Date</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Particular</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Bill Amount</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Advance</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Advance Deduction</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">SD Release</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Material Deductions</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Petty Expenses</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Other Amount</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Hold Amount</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Hold Release</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">SD 5%</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Amount Paid</th>
		     <th style="border:1px solid #000;padding:10px;background-color:#ccc;">Cumulative Total</th>
		</tr>
   </thead>
   <tbody id="appendBillDetails" style="border:1px solid #000;">
  
   </tbody>
   </table>
  </div>
 </div>
  
  <div id="appendBillDetailsTotal"  style="float:  right;"></div>
 </div>
 
 
 

 
 

 
<!-- modal popup for approve RABill start -->
 
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
          <th>Toatal Amount</th>
          <th>Unit</th>
          <th>Cumulative Recovered</th>
          <th>Previous Recovered</th>
          <th>Current Recovered</th>
         </tr>
        </thead>
        <tbody id="RecoveryStatement">
        <tr>
         <td>1</td>
         <td><strong>Safety (Helmet/Reflective Jacket/Goggles)</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>3,123.00</td>
         <td>3,123.00</td>
         <td>-</td>
        </tr>
         <tr>
         <td>2</td>
         <td><strong>Food expences/Labour Photos</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>-</td>
         <td>-</td>
         <td>-</td>
        </tr>
        <tr>
        <td>3</td>
         <td><strong>Aluminium Fanti-6'/Marble cutting blade(290+480)</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>770.00</td>
         <td>770.00</td>
         <td>-</td>
        </tr>
        <tr>
        <td>4</td>
         <td><strong>Adhesive Mixing Machine Supply</strong></td>
         <td>1</td>
         <td>2,700.00</td>
         <td>Nos</td>
         <td>2,700.00</td>
         <td>2,700.00</td>
         <td>-</td>
        </tr>
         <tr>
        <td>5</td>
         <td><strong>Gum boot supply (11 nos)</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>-</td>
         <td>-</td>
         <td>-</td>
        </tr>
         <tr>
        <td>6</td>
         <td><strong>Rubber hammer, 15mtr measurement tape</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>590.00</td>
         <td>590.00</td>
         <td>-</td>
        </tr>
         <tr>
        <td>7</td>
         <td><strong>Hacking hammer, Water level tube, Rubber hammer,</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>1,778.00</td>
         <td>1,778.00</td>
         <td>-</td>
        </tr>
         <tr>
        <td>8</td>
         <td><strong>14" Disc cutting blade & Stusco Machine</strong></td>
         <td></td>
         <td>11,869.00</td>
         <td></td>
         <td>11,869.00</td>
         <td>7,912.67</td>
         <td>3,956.33</td>
        
        </tr>
         <tr>
        <td>9</td>
         <td><strong>Other Deductions</strong></td>
         <td></td>
         <td></td>
         <td></td>
         <td>-</td>
         <td>-</td>
         <td>-</td>
        </tr>
        <tr>
        <td></td>
         <td class="text-right"><h4><strong>Total Amount</strong></h4></td>
         <td></td>
         <td></td>
         <td></td>
         <td><strong>20,830.00</strong></td>
         <td><strong>16,873.67</strong></td>
         <td><strong>3,956.33</strong></td>
        </tr>
        </tbody>
         </table>
        </div>
      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="recoverySubmit()">submit</button>
        </div>
      </div>
    </div>
  </div>
</div> 
 <input type="hidden" id="deductionrowlength" name="deductionrowlength">
 
 <!-- Modal -->
<div id="modal-approverabill" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg modal-rabill-customwidth">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Approve RA Bill</strong></h4>
      </div>

      <div class="modal-body modal-body-scroll">
       <div class="table-responsive" style="overflow:hidden;">
        <table class="table table-new "style="margin-bottom:-1px;"> <!-- tbl-rabill -->
        <thead class="cal-thead-inwards">
         <tr>
              <th rowspan="2" class=" vertical-alignment"><input type="checkbox" name="checkAll" id="checkAll"  style="width: 100%;height: 16px;cursor: pointer;"></th>
              <th rowspan="2" class="vertical-alignment">Description of Work</th>
              <th rowspan="2" class="vertical-alignment">Total Quantity</th>
              <th rowspan="2" class="vertical-alignment">Rate</th>
              <th rowspan="2" class="vertical-alignment">Unit</th>
              <th colspan="2" >Cumulative Bill</th>
              <th colspan="2" >Previous Certified</th>
              <th colspan="2">Current Certified</th>
            </tr>
            <tr>
              <th>Quantity </th>
              <th>Amount</th>            
              <th>Quantity </th>
              <th>Amount</th>            
              <th>Quantity </th>
              <th>Amount</th>
            </tr>
        </thead>
      <!--   </table>
       </div>
       <div style="height: auto;max-height: 300px;overflow-y:scroll;">
       <table class="fixed-header-tbl">
 -->        <tbody id="paymentByArea" class="tbl-fixedheader-tbody">

        </tbody>
        </table>
        
       </div>
      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" class="btn btn-warning" onclick="paymentAreaBtn()">Submit</button>
        </div>
      </div>
    </div>

  </div>
</div>
<!-- Modal popup for approve RABill End -->
<input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
  </body>

   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>
   $(".copyPasteRestricted").bind('paste', function (e) {
		e.preventDefault();
	});			
		$(".overlay_ims").show();
		$(".loader-ims").show();
		//loading recovery data
		function loadRARecovery(){
			var ContractorId=$("#ContractorId").val();
			var ContractorName=$("#ContractorName").val();
			var workOrderNo=$("#workOrderNo").val();
			var approvePage=$("#approvePage").val();
			var billType=$("#billType").val();
			var tempBillNo=$("#tempBillNo").val();
			var site_id=$("#site_id").val();
			var result=validateForm();
			if(result==false){
				return false;
			}
			var str="";
			var str1="";
			var htmlData="";
			$.ajax({
				url : "loadRecoveryAreaDetails.spring",
				type : "GET",
				data : {
					contractorId : ContractorId,
					workOrderNo:workOrderNo,
					approvePage:approvePage,
					billType:billType,
					tempBillNo:tempBillNo,
					site_id:site_id
				},
				success : function(data) {
		     		var i=0;
					var sumOfRecovery=0;
					var sumOfCumulative=0;
					var sumOfPrev=0;
					var childProductName="";
					var temprecoveryAmount="";
					var tempcurrentrecovery=0;
					$("#RecoveryStatement").html("");
					$("#ViewRecovery").html("");
					$.each(data,function(key,value){
						if(value.ISSUED_QTY=="0"){
							return ;
						}
						var issuedQty=parseInt(value.ISSUED_QTY);
						var currentRecoveryAmount=0;
						var currentRecoveryAmount=value.CURRENTRECOVERY==null?0:parseFloat(value.CURRENTRECOVERY);
						var recoveryAmount=value.RECOVERY_AMOUNT==null?0:parseFloat(value.RECOVERY_AMOUNT);
						temprecoveryAmount=value.RECOVERY_AMOUNT;
						var recoveryCurrentAmount=parseFloat(value.TOTAL_AMOUNT)-recoveryAmount;
						var totalAmount=value.TOTAL_AMOUNT==null?"":value.TOTAL_AMOUNT;
						let tempCumulative=(recoveryAmount+currentRecoveryAmount);
						var child_product_id=value.CHILD_PRODUCT_ID;
						if(childProductName!=value.CHILD_PROD_NAME){
							childProductName=value.CHILD_PROD_NAME;
							i++;
							str="<tr> <td > "+i+"<input type='hidden' id='childId"+i+"' value='"+child_product_id+"'> </td>" +
						    "<td><strong>"+value.CHILD_PROD_NAME+"</strong></td>" +
							"<td id='"+child_product_id+"issuedQty'>"+issuedQty+"</td>" +
							"<td id='"+child_product_id+"totalRecoveryAmount'>"+totalAmount.toFixed(2)+"</td>" +
							"<td  id='"+child_product_id+"mesurment_name'>"+value.MESURMENT_NAME+" <input type='hidden'  id='"+child_product_id+"mesurment_id' value='"+value.UNITS_OF_MEASUREMENT+"'></td>" +
							"<td id='"+child_product_id+"cumulative'>"+tempCumulative+"</td>" +
							"<td id='"+child_product_id+"previous'>"+recoveryAmount+"</td>" +
							"<td> <input type='text' class='form-control' id='"+child_product_id+"currentAmount' name='recoverycurrentAmount' value='"+(currentRecoveryAmount)+"' onkeyup='calculateRecoveryAmount("+i+");'  onkeypress='return isNumber(this, event)'>  <input type='hidden' class='form-control' id='"+child_product_id+"currentAmount1' value='"+(currentRecoveryAmount)+"'>   </td>" +
							"</tr>";
							
							$("#RecoveryStatement").append(str);
						}else{
							var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
							cumulative=parseFloat(cumulative);
							var previous=$("#"+child_product_id+"previous").text();//getting previous value using child id 
							previous=parseFloat(previous);
							var  tempIssuedQty =$("#"+child_product_id+"issuedQty").text();
							tempIssuedQty=parseFloat(tempIssuedQty);
							var before_taxes =$("#"+child_product_id+"totalRecoveryAmount").text();
							before_taxes=parseFloat(before_taxes);
							var currentAmount=parseFloat($("#"+child_product_id+"currentAmount").val());
							if(value.RECOVERY_AMOUNT==null){/*||temprecoveryAmount!=value.RECOVERY_AMOUNT*/
								$("#"+child_product_id+"previous").text(recoveryAmount+previous);
							}
							$("#"+child_product_id+"issuedQty").text((tempIssuedQty+issuedQty).toFixed(2));
							$("#"+child_product_id+"totalRecoveryAmount").text((before_taxes+totalAmount).toFixed(2));
						}
					});
					
					for (var i1 = 1; i1 <= i; i1++) { 
						var child_product_id=$("#childId"+i1).val();
						var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
						var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
						cumulative=parseFloat(cumulative);
						var previous=$("#"+child_product_id+"previous").text();//getting previous value using child id 
						previous=parseFloat(previous);
						sumOfCumulative+=+cumulative;
						sumOfPrev+=+previous;
						sumOfRecovery+=(recovery_amount);
					}
					$("#previousRecovery").html(sumOfPrev.toFixed(2));
					htmlData+="<input type='hidden' name='rowsToIterate' id='rowsToIterate' value='"+i+"'>";
					var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
					var temp=" <tr>" +
				    	"<td></td>" +
						"<td class='text-right'><h4><strong>Total Amount</strong></h4></td>" +
						"<td contenteditable='true'></td>" +
					    "<td contenteditable='true'></td><td contenteditable='true'></td>" +
						"<td id='sumOFRecoveryCumulativce'><strong>"+(sumOfCumulative.toFixed(2))+"</strong></td>" +
						"<td ><strong>"+(sumOfPrev.toFixed(2))+"</strong></td>" +
						"<td ><strong id='sumOfCurrentAmount'>"+(sumOfRecovery.toFixed(2))+"</strong></td></tr>";	
						
						$("#RecoveryStatement").append(temp);
						$("#RecoveryStatement").append(htmlData);	
						caldeductionamt();
					}			
				});
		    }
			//calculating recovery amount and printing in certified table
			var sumOfRecovery=0;
			var sumOfAllCumulative=0;
		    function calculateRecoveryAmount(childId){
				var i=parseInt($("#rowsToIterate").val());
				    sumOfRecovery=0;
			    var child_product_id=$("#childId"+childId).val();
				var totalAmount=$("#"+child_product_id+"totalRecoveryAmount").text();
				totalAmount=parseFloat(totalAmount);
				var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
				if(recovery_amount==0){
				//	$("#"+child_product_id+"currentAmount").val('0');
				}
					
				    sumOfAllCumulative=0;
				for (var i1 = 1; i1 <= i; i1++) {
					var temp_child_product_id=$("#childId"+i1).val();
					var recovery_amount=parseFloat($("#"+temp_child_product_id+"currentAmount").val()==""?0:$("#"+temp_child_product_id+"currentAmount").val());
					var previous=$("#"+temp_child_product_id+"previous").text();//getting previous value using child id 
				    previous=parseFloat(previous);
					sumOfRecovery+=(recovery_amount);
					$("#"+temp_child_product_id+"cumulative").text(previous+recovery_amount);
					sumOfAllCumulative+=previous+recovery_amount;
				 }
				sumOfRecovery=sumOfRecovery.toFixed(2);
				var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
				cumulative=parseFloat(cumulative);
				$("#sumOFRecoveryCumulativce").text(sumOfAllCumulative);
				//$("#currentRecoveryAmount").val(sumOfRecovery);
				$("#sumOfCurrentAmount").text(sumOfRecovery);
				//$("#cumulativeRecovery").val(sumOfAllCumulative);
				
				//$("#recoverycurrentAmount").val(sumOfRecovery);
			}
			
		    function recoverySubmit(){
		    	$("#currentRecoveryAmount").val(sumOfRecovery);
		    	$("#recoverycurrentAmount").val(sumOfRecovery);	
		    	$("#cumulativeRecovery").val((sumOfAllCumulative).toFixed(2));  //+previouscumilativeRecovery
		    	//caldeductionamt();
		    	caldeductionamt("recovery");
		    }
					
			$(document).ready(function() {				
				$(".up_down").click(  function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
				//this code for to active the side menu 
				var referrer="approveContractorBills.spring";
				var isCommonApproval="${isCommonApproval}";
				if(isCommonApproval=="true"){
					referrer="siteWiseApproveContractorBills.spring";
				}
				$SIDEBAR_MENU.find('a').filter(function () {
			           var urlArray=this.href.split( '/' );
			           for(var i=0;i<urlArray.length;i++){
			        	 if(urlArray[i]==referrer) {
			        		 return this.href;
			        	 }
			           }
			    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
			});

			/* $(function() {
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1, div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height',
				$(document).height() - 65 + "px");
			}); */
			//for abstract open
			$("#certificate-id").click(function(){
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();
				if(ContractorId.length==0){
					$("#ContractorName").focus();
					alert("Please enter contractor name.");
					return false;
				}
				if(workOrderNo.length==0){
					alert("Please select work order no.");
					return false;
				}
			 $("#tbl-2").show();
		   });
			
		  /*for print */
			function  myPrint(){
				window.print();
			}
			//select all checkbox in abstract table
			$("#checkAll").change(function () {
			    debugger;
				$('input:checkbox').not(this).prop('checked', this.checked);
				$.each($("input[name='chk1']:checked"), function(){         
					var thisVal=$(this).val();
					var workAreaId=$("#workAreaId"+thisVal).val();
					var len = $("input[name='chk1']:checked").length;
					var area=$("#CCQty"+thisVal).val()==""?0:$("#CCQty"+thisVal).val();    
					if(area==0){
						$(this).prop("checked", false);	
					}
					var actualQty=$("#TotalQty"+thisVal).text();
					var initiatedQunatity=$("#CBQty"+thisVal).text();
			  	   if(area==0&&(initiatedQunatity!=actualQty)){
			           	alert("Please enter quantity.");
			           	$("#CCQty"+thisVal).focus();
					    $(this).prop("checked", false);	
					    $("#checkAll").prop("checked",false);
					    $('input:checkbox').not(this).prop('checked',false);
					    return false;
					}
				 });
				sumOfArea();
		  });
		 
		var raAmountToPay=	parseInt($("#raAmountToPay").val());
		var secDepositCurrentCerti=$("#secDepositCurrentCerti").val();
		var num=secDepositCurrentCerti/raAmountToPay*(100);
		num=num.toFixed(2);
		if(num<=0){
		   var html="<option value='' selected='selected'>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=1){
			var html="<option value=''>0%</option><option selected='selected'>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";	
	    }else if(num<=2){
			var html="<option value=''>0%</option><option>1%</option><option selected='selected'>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=3){
			var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option selected='selected'>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=4){
			var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option selected='selected'>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=5){
		   var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option selected='selected'>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=6){
		  var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option  selected='selected' value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=7){
          var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option >5%</option><option  value='6'>6%</option><option  selected='selected' value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=8){
		  var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option  selected='selected' value='8'>8%</option> <option value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=9){
		  var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option   selected='selected' value='9'>9%</option><option value='10'>10%</option>";
		}else if(num<=10){
		  var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option   selected='selected' value='10'>10%</option>";
		}
		else if(num>=10){
		  var html="<option value=''>0%</option><option>1%</option><option>2%</option> <option>3%</option><option>4%</option><option>5%</option><option value='6'>6%</option><option value='7'>7%</option><option value='8'>8%</option> <option value='9'>9%</option><option  selected='selected' value='10'>10%</option>";
		}
		setTimeout(function(){
			$("#securityPer").html(html);	
		}, 1000);
		
		$(window).load(function () {
	 		 $("#totalAmtToPay").val(inrFormat(parseFloat($("#totalAmtToPay").val()).toFixed(2)));
		});

   </script>
</html>
