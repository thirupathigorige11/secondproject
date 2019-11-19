<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
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
<link href="css/loader.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="js/WorkOrder/ShowRaAdvStatus.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
</head>

<style>
td {
    font-weight:bold;
}
.workorder-class-select{
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
.border-none{border:none;}
.ui-icon{
width:100%;
height:100%;
}
.custom-combobox-toggle{
 position:absolute;
 height:25px;
}
/* print styles */
@media print{ 
  .nav_menu, .print_hide, #printBtn,  #closeBtn{ display:none; }
  .border-none, .print_hide_border{border:none;}
  .print-tnlrowcolor{background-color:#dbecf6 !important;}
   thead {display: table-row-group;}
}
</style>
<body class="nav-md">
<div class="overlay_ims" ></div>
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
			<div class="right_col" role="main">
		 <c:set value="ADV" var="ADV"></c:set>
       <form:form modelAttribute="indentReceiveModelForm" id="contractorBill" class="form-horizontal" action="generateAdvancePaymentBill.spring">
		<div class="col-md-12">
        <table  style="width:100%;margin-bottom: -1px;border:1px solid #000;"> 
          <tbody>
            <tr>
              <td style="border:1px solid #000;" class="text-center bck-ground"><h4><strong>${billBean.contractorName} </strong></h4></td>
            </tr>
            <tr>
             <td style="border:1px solid #000 !important;border-top:1px solid #000 !important;" colspan="2" style="width:80%" class="text-center bck-ground">
               <h5><strong>Bill</strong></h5>
             </td>
            </tr>
          </tbody>
          </table>
         <table style="border:1px solid #000 !important;margin-bottom: -1px;width:100%;" > <!-- class="table table-border-certificateofpayment tbl-border-top tbl-border-bottom "-->
          <tbody>
            <tr>
              <td style="width:50%;border:1px solid #000 !important;padding:5px;">
              <div style="width:100%;"class=""><div class=""><strong>To,</strong></div></div>
			  <div class="clearfix"></div>
			   <div style="width:100%;"class=""><div class=""><strong>Sumadhura Infracon Pvt Ltd</strong></div></div>
			  <div class="clearfix"></div>
			  <div style="width:100%;" class=""><div class="">
			  <strong>
			   			  <c:forEach items="${SiteAddress }" var="siteAddr">
							<p> <strong> ${siteAddr}</strong></p>
						  </c:forEach>
						</strong>
			  
			  </div></div>
			  <div class="clearfix"></div>
			  <div class="clearfix"></div>
			  <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   />
			  <input type="hidden" class="border-none" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" readonly="readonly"  onkeyup="return populateData(this, event);"  autocomplete="off"   />
			  <div style="width:100%;" class="mrg-Top"><div style="width:45%;float:left;" class=" mrg-Top"><strong>Pan Card No</strong> </div><div style="width:10%;float:left;" class="mrg-Top"><strong>:</strong></div><div style="width:45%;float:left;"class="mrg-Top">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  <div style="width:100%;" class=" mrg-Top"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Account No</strong> </div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;">${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div style="width:100%;"class=" mrg-Top"><div style="width:45%;float:left;" class="  mrg-Top"><strong>IFSC & Bank Name</strong>  </div><div style="width:10%;float:left;" class=" mrg-Top"><strong>:</strong></div><div style="width:45%;float:left;"class=" mrg-Top">
                <c:set value="-" var="delimiter"></c:set>
              
              <c:choose>
              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
              </c:choose></div></div>
			  <div class="clearfix"></div>
              <div style="width:100%;"class=" mrg-Top"><div style="width:45%;float:left;"class="  mrg-Top"><strong>Mobile No  </strong></div><div style="width:10%;float:left;" class=" mrg-Top"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;"> ${billBean.contractorPhoneNo}</div></div>
			  <div class="clearfix"></div> 
             
             
              </td>
              <td style="width:50%;border:1px solid #000 !important;padding:5px;">
             <!--  <div class="" style="height:50px;"></div> -->
              <div class="" style="width:100%;margin-top: 10px;">
			  <div class=""style="width:45%;float:left;"><strong>Project</strong></div><div style="width:10%;float:left;"class="">:</div><div class=""style="width:45%;float:left;"><strong> ${SiteName}</strong></div>
			  	 </div>
			  	 <div class="clearfix"></div> 
			  	  <div class="" style="width:100%;margin-top: 10px;">
			  	<div  class=""style="width:45%;float:left;"><strong>Type of work</strong></div><div style="width:10%;float:left;"class="">:</div><div style="width:45%;float:left;" class="" id="typeOfWork">NMR Works</div>
			   </div>
			   <div class="clearfix"></div> 
			    <div class="" style="width:100%;margin-top: 10px;">
			    <div class=""style="width:45%;float:left;"><strong>Work order No</strong></div><div style="width:10%;float:left;"class="">:</div><div class="" style="width:45%;float:left;"> ${billBean.workOrderNo}
			    <input type="hidden"  name="workOrderNo" id="workOrderNo" readonly="readonly"  value="${billBean.workOrderNo}">
			   	<input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			    <input type="hidden" name="approvePage" id="approvePage" value="${approvePage}">
			    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
			    <input type="hidden" name="advPage" id="advPage" value="false">
			    <input type="hidden" name="billType" id="billType" value="${billType}">
			    <input type="hidden" id="billDate" value="${billBean.paymentReqDate}">
			    <input type="hidden" name="billNo" id="billNo" value="${billBean.billNo}">
			    <input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${billBean.tempBillNo } " readonly="readonly">
			    <input type="hidden" name="isRAStatusPage" id="isRAStatusPage" value=" ${isRAStatusPage} " readonly="readonly">
			    <input type="hidden" name="isAdvStatusPage" id="isAdvStatusPage" value=" ${isAdvStatusPage} " readonly="readonly">
			     <div class="clearfix"></div> 
			    </div>
			    </div>
               <div class="clearfix"></div>
               <div class="" style="width:100%;margin-top: 10px;">
              	<div style="width:45%;float:left;" class="">Bill No </div><div class="" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class=""> ${billBean.permanentBillNo }<input type="hidden"  class="border-none" style="display:inline;"  value=" ${billBean.permanentBillNo }" readonly="readonly"/></div>
              </div>
               	<div class="" style="width:100%;">
              	<div class=" mrg-Top" style="width:45%;float:left;">Invoice No </div><div class=" mrg-Top" style="width:10%;float:left;">:</div><div class=" mrg-Top" style="width:45%;float:left;">${billBean.billInvoiceNumber}<input type="hidden"  class="heightthirty border-none" style="display:inline;border:none;"  value=" ${billBean.permanentBillNo.trim() }" readonly="readonly"/></div>
              	</div>
                <div class="" style="width:100%;">
	              	<div class="mrg-Top" style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;"  id="workOrderAMount">${billBean.totalAmount}</div>
	           </div>
              	 <div class="clearfix"></div>
                  <div class=" " style="width:100%;margin-top: 10px;">
                     <div style="width:45%;float:left;"class=""><strong>Bill Date</strong></div><div style="width:10%;float:left;"class="">:</div><div style="width:45%;float:left;"class="">${billBean.paymentReqDate}</div>
                 </div>
              </td>
            </tr>
          </tbody>
        </table>
        <table style="margin-bottom:25px;border:1px solid #000;width:100%;font-weight:bold;"> <!-- class="table table-border-certificateofpayment tbl-border-top" --> 
         <thead class="thead-color-certificateofpayment">
            <tr>
              <th rowspan="2" class="text-center vertical-alignment" style="width:5%;border:1px solid #000;padding:5px;">S.NO</th>
              <th rowspan="2" class="text-center vertical-alignment" style="width:45%;border:1px solid #000;padding:5px;">Description</th>
              <th colspan="3" class="text-center" style="width:50%;border:1px solid #000;padding:5px;">Amount</th>
            </tr>
            <tr>
              <th class="text-center" style="border:1px solid #000;padding:5px;">Cumulative Certified</th>
              <th class="text-center" style="border:1px solid #000;padding:5px;">Previous Certified</th>
              <th class="text-center" style="border:1px solid #000;padding:5px;">Current Certified</th>
            </tr>
          </thead>
          <tbody>
              <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"><strong>I</strong></td>
              <td class="text-center td-active border-none input-outline" style="border:1px solid #000;padding:5px;"><span>Total Value of Work Completed / Certified</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raCc" id="raCc" style="border:none; text-align:center;" value="${billBean.totalCurrentCerti}" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raPc" id="raPc" style="border:none; text-align:center;" value="0" readonly/></td><%-- ${billBean.totalCurrentCerti} --%>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;" > <input name="ACTUUAl" type="hidden" value="${billBean.paybleAmt}">  <input type="text"  name="raAmountToPay" id="raAmountToPay"   style="border:none; text-align:center;" value=" ${billBean.totalCurrentCerti}" readonly></td><!--  ${billBean.totalCurrentCerti}amount to pay-->
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
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalCc" name="raTotalCc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalPc" name="raTotalPc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualRaAmountToPay" id="actualRaAmountToPay" value="${billBean.totalCurrentCerti }">  <input type="text" id="totalCc" name="totalCc"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td> <!-- ${billBean.totalCurrentCerti} -->
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
              
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" class="raPaidAmnt" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
           <tr>
           
              <td class="text-center" style="border:1px solid #000;padding:5px;">a)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Advance Deduction</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raeductionCumulative" id="raeductionCumulative"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raePrevductionCumulative" id="raeductionPrevCumulative"  style="border:none;text-align:center;" value="${raeductionPrevCumulative }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt"> <input type="text"  name="raDeductionAmt" id="raDeductionAmt" autocomplete="off"  onfocusout="raCalcAmountToPay(contractorRABill.raAmountToPay.value,2)"  style="border:none;text-align:center;" value="${raDeductionAmt }" readonly/></td><!-- deduction amt here -->
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">b)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Security Deposit(E)</span><span id="securityPer"></span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositCumulative" id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositPrevCerti" id="secDepositPrevCerti" style="border:none;text-align:center;" value="${secDepositPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" > <input type="text"  name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="${secDepositCurrentCerti}" readonly/></td>
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">c)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Petty Expenses</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCumulative" id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesPrevCerti" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="${pettyExpensesPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off"   style="border:none;text-align:center;" value="${pettyExpensesCurrentCerti }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Others</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtCumulative" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtPrevCerti" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="${otherAmtPrevCerti}" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="other" id="other"  autocomplete="off"  style="border:none;text-align:center;" value="${other }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;" ><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="sumOfCumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;" ><input type="text"  name="previousRecovery" class="PcAmnt" id="sumOfPreviousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span id="currentRecoveryAmount">${recoveryAmount}</span>   <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount}"> <a class="print_hide" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
			 </tr>
               <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (B)</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulative" id="totalAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtPrevCerti" id="totalAmtPrevCerti"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti+raDeductionAmt}" name="actualTotalDeductAmt" id="actualTotalAmtCurrnt"> <input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc"  style="border:none;text-align:center;" value="${secDepositCurrentCerti+raDeductionAmt+other+pettyExpensesCurrentCerti+recoveryAmount}" readonly/></td>
    		 </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">III</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Outstanding advance (F)</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvCurrent" id=outstandingAdvCurrent  autocomplete="off"  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">IV</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Release advance (G)</span> </td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount"  autocomplete="off"  style="border:none;text-align:center;" value="" onfocusout="calculateAdvBillAmt(this.value)" readonly/></td>
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
			 <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${billBean.paybleAmt}" id="actualFinalAmt" name="actualFinalAmt">  <input type="text" name="finalAmt" id="finalAmt"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.paybleAmt}" readonly></td>
			</tr>
        	  <tr style="font-size: 15px;" class="print-tnlrowcolor">
			 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords" style="border:1px solid #000;padding:5px;"></td>
			</tr>			
		    <tr> 
              <td colspan="5" style="border:1px solid #000;padding:5px;">
               <div style="width:100%;margin-top:50px;"></div>
               <div style="width:100%;">
               <div style="width:33.3%;float:right;"></div>
               <div style="width:33.3%;float:right;"></div>
               <div style="width:33.3%;float:right;text-align:center;">Contractor Sign</div>
               </div>            
              </td>
            </tr>
          </tbody>
        </table>
    </div>    
    <div class="col-md-12 text-center center-block">
   	 <input type="button" name="submitBtn"   class="btn btn-warning btn-custom-width" onclick="printPage()" 	id="printBtn" value="Print" />
    </div> 
   <div id="modal-recovery-click" class="modal fade" role="dialog">
    <div class="modal-dialog modal-lg modal-rabill-customwidth">
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
          <th>Total qty Consumed</th>
          <th>Rate</th>
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
         <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</div>
</form:form>
</div>
</div>
</div>
</body>

<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>
<script type="text/javascript">
	function closeView(){
		goBack() ;
	}
	function goBack() {
	    window.history.back();
	}
	function printPage(){
		window.print();
	}
	
	//this code for download server images	
	//this code for to active the side menu 
	  var referrer="viewContractorBillStatus.spring";
		 $SIDEBAR_MENU.find('a').filter(function () {
	        var urlArray=this.href.split( '/' );
	        for(var i=0;i<urlArray.length;i++){
		     	 if(urlArray[i]==referrer) {
		     		 return this.href;
		     	 }
	        }
		 }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
	
	$(document).ready(function() {
		var billType=$("#billType").val();
		if(billType=="ADV"){
			$("#raPc").text("");
			$("#raTotalPc").text("");
			$("#raCc").text("");
			$("#raAmountToPay").text("");
			$("#totalCc").text("");
			$("#totalActualDeductAmt").text("");
		}
	});

</script>
			

</html>
