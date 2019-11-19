<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
</head>

<style>
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new tbody tr td:first-child{min-width:20px;text-align:center;width:50px;}
.table-new tbody tr td {border-top: 0px !important;}
.scroll-rabill-tbl {overflow-y: auto;height: 400px;} 
.tbl-rabill thead tr th{font-weight:bold;font-size:15px;}
.tbl-rabill{font-weight:bold;}
.control_width{width:100% !important;}
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
	<div class="container body">
	<!--  loader  -->
	 <div class="overlay_ims" ></div>
	  <div class="loader-ims" id="loaderId">
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	  </div>
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
<form:form modelAttribute="billBean" id="apprRejFormId" class="form-horizontal" enctype="multipart/form-data" action="generateAdvancePaymentBill.spring">
						<div class="col-md-12">
      <div class="table-responsive">
	  <!--Header part of table start--->
        <table class="table table-border-certificateofpayment" style="margin-bottom: -1px;">
          <tbody>
            <tr>
              <td rowspan="2" style="width:20%"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;" /></td>
              <td  style="width:80%" class="text-center bck-ground"><h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4></td>
            
            </tr>
            <tr><td colspan="2" style="width:80%" class="text-center bck-ground"><h5><strong>Certificate Of Payment</strong></h5></td></tr>
          </tbody>
          
          </table>
		  <!---header part of table end--->
		  <!--details part of table start-->
        <table class="table table-border-certificateofpayment tbl-border-top tbl-border-bottom"style="margin-bottom: -1px;" >
          <tbody>
            <tr>
              <td style="width:50%;">
              <div class="col-md-12"><div class="col-md-4 col-xs-8 mrg-Top"><strong>Contractor&nbsp;Name</strong></div><div class="col-md-1 mrg-Top"><strong>:</strong></div><div class="col-md-6 mrg-Top"><input type="text" class="border-none control_width" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" readonly="readonly"  onkeyup="return populateData(this, event);"  autocomplete="off"  style="border:none;" /><input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   /></div></div>
			  <div class="clearfix"></div>
			   <div class="col-md-12 mrg-Top"><div class="col-md-4 col-xs-8 mrg-Top"><strong>Pan Card No</strong> </div><div class="col-md-1 mrg-Top"><strong>:</strong></div><div class="col-md-6 mrg-Top">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  
			  <div class="col-md-12 mrg-Top"><div class="col-md-4 col-xs-8 mrg-Top"><strong>Account No</strong> </div><div class="col-md-1 mrg-Top"><strong>:</strong></div><div class="col-md-6 mrg-Top">${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div class="col-md-12 mrg-Top"><div class="col-md-4 col-xs-8 mrg-Top"><strong>IFSC & Bank Name</strong>  </div><div class="col-md-1 mrg-Top"><strong>:</strong></div><div class="col-md-6 mrg-Top">
               <c:set value="-" var="delimiter"></c:set>
                  <c:choose>
	              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
	              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
	              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
	              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
	              </c:choose>
              </div></div>
			  <div class="clearfix"></div>
              <div class="col-md-12 mrg-Top"><div class="col-md-4 col-xs-8 mrg-Top"><strong>Mobile No  </strong></div><div class="col-md-1 mrg-Top"><strong>:</strong></div><div class="col-md-6 mrg-Top"> ${billBean.contractorPhoneNo}</div></div>
			  <div class="clearfix"></div> 
             
              </td>
              <td style="width:50%;">
              <div class="col-md-12">
			  <div class="col-md-5 mrg-Top">Project</div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input type="text" value="${SiteName}" style="border:none;" class="heightthirty control_width" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			  	 <!-- <div  class="col-md-4 mrg-Top">	 </div>	<div class="col-md-1 mrg-Top"></div>	<div class="col-md-6 mrg-Top"> -->
			  	<input type="hidden" class="control_width"name="workDesc" id="workDesc" value="${billBean.typeOfWork}" readonly="readonly"><br>	
				 	
			    <div class="col-md-5 mrg-Top">Work order No</div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6">
			   
			    <input type="hidden" name="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			    <input type="hidden" name="approvePage" id="approvePage" value="true">
			    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
			    <input type="hidden" value="${billBean.permanentBillNo }" name="permanentBillNo" id="permanentBillNo">
			    <input type="hidden" name="billType" id="billType" value="ADV">
			    <input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${billBean.tempBillNo.trim() }">
			    <input type="hidden" name="typeOfWork" id="typeOfWork" value="${billBean.typeOfWork}">
			    <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="0"> 
			    <input type="hidden" name="isCommonApproval" id="isCommonApproval" value="${isCommonApproval}">
			    <input type="text"  name="workOrderNo" id="workOrderNo" readonly="readonly" class="heightthirty control_width" style="border:none;width: 100%;" value="${billBean.workOrderNo}">
			    </div>
			            <div class="clearfix"></div>
		    	 <div class="col-md-5 mrg-Top">Temp Bill No </div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input type="text" class="heightthirty control_width" style="display:inline;border:none;"  value=" ${billBean.tempBillNo.trim() }" readonly="readonly"/></div>
		    	         <div class="clearfix"></div>
		    	 <div class="col-md-5 mrg-Top">Bill No </div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input type="text"  class="heightthirty control_width" style="display:inline;border:none;"   value=" ${billBean.permanentBillNo.trim() }" readonly="readonly"/></div>
		    	         <div class="clearfix"></div>
		    	 <div class="col-md-5 mrg-Top">Invoice No </div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input type="text"  class="heightthirty control_width" style="display:inline;border:none;"   value=" ${billBean.billInvoiceNumber.trim() }" readonly="readonly"/></div> 
                        <div class="clearfix"></div>
                 <div class="col-md-5 mrg-Top">Type Of Work</div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6 mrg-Top" style="word-break: break-all;margin-bottom:5px;" id="paymentTypeOfWork"></div> 	
                         <div class="clearfix"></div>
                 <div class="col-md-5 mrg-Top">WO Amount </div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input type="text" class="heightthirty control_width" name="totalAmtToPay" id="totalAmtToPay" value="${ billBean.totalAmount}" style="display:inline;border:none;" readonly="readonly" /></div>
                         <div class="clearfix"></div>
                 <div class="col-md-5 mrg-Top">Bill Date</div><div class="col-md-1 mrg-Top">:</div><div class="col-md-6"><input  type="text" class="heightthirty control_width" value="${billBean.paymentReqDate}" name="billDate" id="billDate" style="display:inline;border:none;" readonly="readonly"  /></div>
              </div>
              </td>
            </tr>
          </tbody>
        </table>
		<!--details part of table end-->
		<!--information table start--->
        <table class="table table-border-certificateofpayment tbl-border-top" style="margin-bottom:25px;">
          <thead class="thead-color-certificateofpayment">
            <tr>
              <th rowspan="2" class="text-center vertical-alignment" style="width:5%;">S.NO</th>
              <th rowspan="2" class="text-center vertical-alignment" style="width:44.9%;">Description</th>
              <th colspan="3" class="text-center" style="width:50%;">Amount</th>
            </tr>
            <tr>
              <th class="text-center">Cumulative Certified </th>
              <th class="text-center">Previous Certified</th>
              <th class="text-center">Current Certified</th>
            </tr>
          </thead>
          <tbody>
          <tr style="font-size: 15px;">
              <td class="text-center"><strong>I</strong></td>
              <td class="text-center td-active border-none input-outline"><span>Total Value of Work Completed / Certified</span></td>
              <td class="text-center td-active"><input type="text"  name="raCc" id="raCc" style="border:none; text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"><input type="text"  name="raPc" id="raPc" style="border:none; text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline" ><input type="text"  name="raAmountToPay" id="raAmountToPay" style="border:none; text-align:center;" value="" readonly="readonly"/></td><!--  amount to pay-->
            </tr>
            <tr>
              <td style="padding:18px;"></td>
              <td style="padding:18px;"></td>
              <td style="padding:18px;"></td>
              <td style="padding:18px;"></td>
              <td style="padding:18px;"></td>
            </tr>
            
         <tr style="font-size: 15px;">
              <td class="text-center"></td>
              <td class="text-center td-active input-outline"><span>Total Amount (A)</span></td>
              <td class="text-center td-active"><input type="text"  id="raTotalCc" name="raTotalCc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active"><input type="text"  id="raTotalPc" name="raTotalPc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline">
                 <input type="text" id="totalCc" name="totalCc"  autocomplete="off"  style="border:none;text-align:center;" value="0" readonly>
              </td>
            </tr>
			 <tr>
              <td class="text-center" style="border:1px solid #000;">II</td>
              <td class="text-center" style="border:1px solid #000;"><span>Paid Amount</span></td>
              <td class="text-center" style="border:1px solid #000;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;"><input type="text" class="raPaidAmnt" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
            <tr>
              <td class="text-center">a)</td>
              <td class="text-center"><span>Advance Deduction</span></td>
              <td class="text-center"><input type="text" name="raCcDeductionAmt" id="raCcDeductionAmt" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center"><input type="text"  name="raPevDeductionAmt" id="raPrevDeductionAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="raDeductionAmt" name="raDeductionAmt" id="raDeductionAmt" autocomplete="off"  onkeyup="caldeductionamt('ra')" onkeypress="return isNumber(this, event)" style="border:none;text-align:center;" value="0"/></td> 
            </tr>
			<tr>
              <td class="text-center">b)</td>
              <td class="text-center input-outline"><span>Security Deposit(E)</span>
              <select name="securityPer" id="securityPer" onchange="securityperchange()" disabled>
                 <option value="0">0%</option><option value="1">1%</option><option value="2">2%</option> <option value="3">3%</option><option value="4">4%</option><option value="5">5%</option>
                <option value="6">6%</option><option value="7">7%</option><option value="8">8%</option> <option value="9">9%</option><option value="10">10%</option>
           
              </select></td>
              <td class="text-center"><input type="text"  name="secDepositCumulative" class="CcAmnt"  id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="secDepositPrevCerti" class="PcAmnt" id="secDepositPrevCerti" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="raDeductionAmt" name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="0" readonly/></td>
            </tr>
			<tr>
              <td class="text-center">c)</td>
              <td class="text-center input-outline"><span>Petty Expenses</span></td>
              <td class="text-center"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt"  id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="raDeductionAmt" onkeypress="return isNumber(this, event)" name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off"   style="border:none;text-align:center;" value="0" onkeyup="caldeductionamt('pe')" readonly/></td>
            </tr>
        
			 <tr>
              <td class="text-center"></td>
              <td class="text-center input-outline"><span>Others</span></td>
              <td class="text-center"><input type="text"  name="otherAmtCumulative" class="CcAmnt" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="raDeductionAmt" name="other" id="other" onkeypress="return isNumber(this, event)"  autocomplete="off"  style="border:none;text-align:center;" value="0" onkeyup="caldeductionamt('ot')" readonly/></td>
            </tr> 
              <tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
             <td class="text-center"><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text" class="raDeductionAmt" name="" id="showingRecoveryinADV"  autocomplete="off"   style="border:none;text-align:center;" value="0.00" readonly/></td>
			  </tr>
          <tr style="font-size: 15px;">
              <td class="text-center"></td>
              <td class="text-center td-active input-outline"><span>Total Amount (B)</span></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtCumulative" id="totalDeductionAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtPrevCerti" id="totalDeductionAmtPrevCerti"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center td-active input-outline"><input type="text" name="totalAmtCurrnt" id="totalAmtCurntDeduc"  autocomplete="off"   style="border:none;text-align:center;" value="" readonly/></td>
            </tr>
			<tr>
              <td class="text-center">III</td>
              <td class="text-center input-outline"><span>Outstanding advance (F)</span> </td>
              <td class="text-center"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="outstandingAdvTotalAmthidden" value="0"></td>
              <td class="text-center"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  name="outstandingAdvCurrent" id=outstandingAdvCurrent  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
            
			<tr>
              <td class="text-center">IV</td>
              <td class="text-center input-outline"> <input type="hidden" id="actualreleaseAdvTotalAmt">  <span>Release advance (G)</span> </td>
              <td class="text-center"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" name="advanceCurrAmount"  id="advanceCurrAmount" style="border:none;text-align:center;" value="${billBean.paybleAmt}" onkeyup="calreleaseAmnt()" readonly/>
              <input type="hidden" name="advanceCurrAmount1"  id="advanceCurrAmount1" style="border:none;text-align:center;"/>
              </td>
            </tr>
			
			<tr>
				<td style="padding:18px"></td>
				<td style="padding:18px"></td>
				<td style="padding:18px"></td>
				<td style="padding:18px"></td>
				<td style="padding:18px"></td>
			</tr>
			<tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"><span>Net Payable Amount (A - B + G) = C</span></td>
			 <td class="text-center td-active"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" style="border:none;text-align:center;" value="0.0" readonly></td>
			 <td  class="text-center td-active"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline"><input type="text" name="finalAmt"  id="finalAmt" style="border:none;text-align:center;" value="${billBean.paybleAmt}" readonly></td>
			</tr>
		    <tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords"></td>			
			</tr>
			<tr>
			<td colspan="2" class="text-center">Payment Area</td>
			<td colspan="3" class="text-center"><a class="certificate-link"  id="certificate-id" data-toggle="modal" data-target="#modal-approveCOPF">Abstract</a></td> 
			</tr>
          </tbody>
        </table>
        <!--information table end--->
         <!-- checkboxes table start --> 

         <!-- checkboxes table end --> 
      </div>
    </div>    
    <div class="col-md-12 text-center center-block">
   	<input type="button" name="submitBtn" id="approveBtn" onclick="return approveAdvBill()"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Approve" />
   	<input type="button" name="submitBtn" id="rejectBtn" onclick="return rejectAdvBill()" class="btn btn-warning btn-custom-width" id="closeBtn"	value="Reject" />
    </div> 
    <br>
     <div class="col-md-12 Mrgtop10">
			<label class="control-label col-md-2" >Remarks : </label>
			<div class="col-md-4" style="    position: relative;margin-bottom: 10px;" >
			<input type="hidden" name="actualremarks" value="${billBean.remarks}"> 
		 		<textarea name="remarks" id="remarks" href="#" data-toggle="tooltip" title="${billBean.remarks}" placeholder="<c:out value='${billBean.remarks}'></c:out>"  id="NoteId" class="form-control" autocomplete="off"></textarea> 
			</div>
		</div>
	 <div class="col-md-12 Mrgtop10">
	 <c:if test="${changedListDtls.size()!=0 }">
			<label class="control-label col-md-2" >Modification Details : </label>
	</c:if>
			<div class="col-md-4" style="    position: relative;margin-bottom: 10px;" >
			<c:forEach items="${changedListDtls }" var="chngedDtls">
			<p>${chngedDtls.REMARKS}</p>	
			</c:forEach>
		</div>
		</div>
		<!-- this is common file for showing images  -->
		<%@include file="ImgPdfCommonJsp.jsp" %>
		
		
		<div id="appendActualAreaDetails">
		
		</div>
		<div id="appendBillDetails1">
		</div>
    <div id="appendWorkAreaId">
    
    </div>
   
    
</form:form>
 <div class="col-md-12 mrg-Top" style="display:none;" id="hideIt">
  <div class="table-responsive scroll-rabill-tbl1">
   <table class="table table-bordered tbl-rabill" >
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
   <tbody id="appendBillDetails">
  
   </tbody>
   </table>
  </div>
 </div>
</div>
         <div id="appendBillDetailsTotal"  style="float:  right;"></div>          
  </div>
  </div>
 <!-- Moda Popup for modal-approveCOPF start -->
 <div id="modal-approveCOPF" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:90%;">

    <!-- Modal content -->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Certificate Of Payment</strong></h4>
      </div>
      <div class="modal-body">
       <div class="table-responsive "><!--  scroll-rabill-tbl -->
        <table class="table table-bordered table-new"> <!-- tbl-rabill -->
                <thead class="cal-thead-inwards">
         <tr>
              <th rowspan="2" class="vertical-alignment text-center"><input type="checkbox" name="checkAll" id="checkAll"  style="width: 100%;height: 16px;cursor: pointer;"></th>
              <th rowspan="2" class="vertical-alignment text-center">Description of Work</th>
              <th rowspan="2" class="vertical-alignment text-center">Total Quantity</th>
              <th rowspan="2" class="vertical-alignment text-center">Rate</th>
              <th rowspan="2" class="vertical-alignment text-center">Unit</th>
              <th colspan="2" class="text-center">Cumulative Certified</th>
              <th colspan="2" class=" text-center">Previous Certified</th>
              <th colspan="2" class=" text-center">Current Certified</th>
            </tr>
            <tr>
              <th class=" text-center">Quantity </th>
              <th class=" text-center">Amount</th>            
              <th class=" text-center">Quantity </th>
              <th class=" text-center">Amount</th>            
              <th class=" text-center">Quantity </th>
              <th class=" text-center">Amount</th>
            </tr>
        </thead>
        <tbody id="paymentByArea" class="tbl-fixedheader-tbody">
        
        </tbody >
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
 <input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
  </body>

<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>					
<script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script>
<script type="text/javascript">
	$(".overlay_ims").show();	
	$(".loader-ims").show();
		
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
	
   
	//for abstract open

	$("#certificate-id").click(function(){
		var ContractorId=$("#ContractorId").val();
		var workOrderNo=$("#workOrderNo").val();
		if(ContractorId.length==0){
			alert("Please enter contractor name.");
			return false;
		}
		if(workOrderNo.length==0){
			alert("Please select work order number.");
			return false;
		}
	});
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
	

	
	$(window).load(function () {debugger;
	  //alert("Window Loaded");
	  $("#totalAmtToPay").val(inrFormat(parseFloat($("#totalAmtToPay").val()).toFixed(2)));
	});
	
	
  </script>
</html>
