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
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">
<jsp:include page="./../CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script>
</head>

<style>
/* fixed header table css for modal*/
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new tbody tr td:first-child{min-width:20px;text-align:center;width:50px;}
.table-new tbody tr td {border-top: 0px !important;}
.tbl-rabill thead tr th{font-size:15px;font-weight:700}
.tbl-rabill tbody tr td{font-weight:700}
.recovery-popup-table thead tr th{font-size:15px;font-weight:700}
.recovery-popup-table tbody tr td{font-weight:700}
#paymentByArea tr td{font-weight:bold;}
.scroll-rabill-tbl{width: calc(100% - 17px) !important;height:70px;}
.scroll-rabill-tbl1{width: calc(100% - 0px) !important;height:200px;}
 .fixed-tbl-rabill{width:100%;border:1px solid #000;}
 .workorder-class-select{width:100%;}
.fixed-tbl-rabill tbody tr td{border:1px solid #000;}
/* fixed header table css for modal*/
/* .modal-body-scroll{
height:500px;
overflow-y:auto;
} */
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
width:100%;
height:25px;
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
.ui-button{
position:absolute;
height:25px;
}
.deduction{
	width: 200px;
	text-align:center;
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

/* #billDate{
    display: inline;
    width: 84%;
    float: left;
    border-radius: 5px 0px 0px 5px;
} */
.input-group-addon {
    border: 1px solid #ccc !important;
    border-left-width: 0 !important;
}
.form-group {
    margin-bottom: 0px;
}
#datepickerIcon{
    height: 30px;
    float: left;
    border: 1px solid #ccc !important;
    background-color:#ddd;
    border-radius: 0px 5px 5px 0px;
}
</style>
<script type="text/javascript">
	if (typeof (Storage) !== "undefined") {
		var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
		if (i == 2) {
			sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
			window.location.reload();
		}
	} else {
		alert("Sorry, your browser does not support Web Storage...");
	};
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
<body class="nav-md">

  <div class="overlay_ims" style="display: none;"></div>
	 <div class="loader-ims" id="loaderId" style="display: none;"> <!--  -->
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
					    <li class="breadcrumb-item"><a href="#">Work Order</a></li>
						<li class="breadcrumb-item active">Certificate Of Payment</li>
					</ol>
			   </div>
       <form:form  id="contractorRABill" class="form-horizontal" action="generateRAPaymentBill.spring">
	    <div class="col-md-12">
         <div class="table-responsive">
	     <!--Header part of table start--->
         <table class="table table-border-certificateofpayment" style="margin-bottom: -1px;">
          <tbody>
            <tr>
              <td rowspan="2" style="width:20%"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 90px;" /></td>
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
              <div class="col-md-12 mrg-Top">
              <div class="form-group">
              <div class="col-md-4 col-xs-8 control-label"><strong>Contractor&nbsp;Name</strong></div><div class="col-md-1 control-label"><strong>:</strong></div><div class="col-md-6"><input type="text"  class="form-control border-none" id="ContractorName" style="width:100%;" name="ContractorName"  onkeyup="return populateData(this, event);"  autocomplete="off"   /><input type="hidden"  id="ContractorId" name="ContractorId"    /></div></div>
			  </div>
			  <div class="clearfix"></div>
			   <div class="col-md-12 mrg-Top">
			    <div class="form-group">
			   <div class="col-md-4 col-xs-8 control-label"><strong>Pan Card No</strong> </div><div class="col-md-1 control-label"><strong>:</strong></div><div class="col-md-6">
			   		<input type="text" class="heightthirty" id="PanCardNo" name="PanCardNo"   style="border:none;width:100%;" readonly />
			   		<input type="hidden" class="heightthirty" id="contractorGSTIN" name="contractorGSTIN" readonly />
			   </div></div>
			   </div>
			  <div class="clearfix"></div>
			  
			  <div class="col-md-12 mrg-Top">
			   <div class="form-group">
			  <div class="col-md-4 col-xs-8 control-label"><strong>Account No</strong> </div><div class="col-md-1 control-label"><strong>:</strong></div><div class="col-md-6"><input type="text"  class="heightthirty" id="AccountNo" name="AccountNo"    style="border:none;width:100%;" readonly /></div></div>
			  </div>
			  <div class="clearfix"></div>
              <div class="col-md-12 mrg-Top">
               <div class="form-group">
              <div class="col-md-4 col-xs-8 control-label"><strong>IFSC & Bank Name</strong>  </div><div class="col-md-1 control-label"><strong>:</strong></div><div class="col-md-6"><input type="text"  class="heightthirty"  id="ifscCode" name="ifscCode"   style="border:none;width:100%;" readonly /></div></div>
			  </div>
			  <div class="clearfix"></div>
              <div class="col-md-12 mrg-Top">
               <div class="form-group">
              <div class="col-md-4 col-xs-8 control-label"><strong>Mobile No  </strong></div><div class="col-md-1 control-label"><strong>:</strong></div><div class="col-md-6"><input type="text"  class="heightthirty" id="MobileNo" name="MobileNo"   style="border:none;width:100%;" readonly /></div></div>
			  </div>
			  <div class="clearfix"></div>            
             
              </td>
              <td style="width:50%;">
              <div class="col-md-12">
               <div class="form-group">
               <div class="col-md-5 control-label">Project</div><div class="col-md-1 control-label">:</div>
			   <div class="col-md-6"><input type="text" value="${SiteName}" style="border:none;" class="form-control" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			   </div>
			 </div>
			 <div class="col-md-12 mrg-Top" >
			     <div class="form-group">			 
				    <div class="col-md-5 control-label">Work order No</div><div class="col-md-1 control-label">:</div>
				    <div class="col-md-6 ">
					    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
					    <input type="hidden" id="pagename" value="initiatera">			    
					    <input type="hidden" name="billType" id="billType" value="RA">
		                <input type="hidden" name="site_id" id="site_id" value="${SiteId}">
		                <input type="hidden" name="typeOfWork" id="typeOfWork" value="${WorkOrderBean.typeOfWork}">	
					    <input type="hidden" name="nextLevelApprovelEmpID" value="${WorkOrderBean.approverEmpId}">
					    <input type="hidden" name="approvePage" id="approvePage" value="false">
					    <select  name="workOrderNo" id="workOrderNo" class="form-control rabill-class-select heightthirty"  style="padding:0px;"> <!-- id="comboboxworkOrderNo" -->
					    </select>
				    </div>
			    </div>
			 </div>
			 <div class="col-md-12 mrg-Top">
			    <div class="form-group">
				<div class="col-md-5 control-label"> Bill Type </div><div class="col-md-1 control-label">:</div><div class="col-md-6 ">
				<select name="isOldOrNewBill" id="isOldOrNewBill" class="form-control rabill-class-select  heightthirty" style="padding:0px;">
						<option value="0">Select Type</option>
						<option value="Old">Previous Bill</option>
						<option value="New">Current Bill</option>
				</select>
				</div>
				</div>
			 </div>
			 <div class="col-md-12 mrg-Top">
			     <div class="form-group">
			 	<div id="showBillText" style="display: none;"><div  class="col-md-5 control-label" >Old Bill No</div><div class="col-md-1 control-label">:</div><div  class="col-md-6 "><input type="text" name="oldBillNo" id="oldBillNo" value="" placeholder="e.g. RA/10" style="display:inline;"  class="form-control rabill-class-select heightthirty"/></div></div>
			  </div>
			 </div>
			 <div class="col-md-12 mrg-Top">
			  <div class="form-group">
			 	<div class="col-md-5 control-label">RA Bill No </div><div class="col-md-1 control-label">:</div><div class="col-md-6 "><input type="text" class="form-control rabill-class-select  heightthirty" name="raBillNo" id="raBillNo" style="display:inline;"  value=""   readonly="readonly"/></div> 
			  </div>
			 </div>
			  <div class="col-md-12 mrg-Top">
			  <div class="form-group">
			 	<div class="col-md-5 control-label">Invoice Number</div><div class="col-md-1 control-label">:</div><div class="col-md-6 ">
			 		<input type="text" class="form-control rabill-class-select  heightthirty" name="billInvoiceNo" id="billInvoiceNo" onkeyup="return validateBillInvoiceNumber()" style="display:inline;" value="" autocomplete="off"/>
			 		<input type="hidden" name="actualBillInvoiceNo" id="actualBillInvoiceNo" autocomplete="off" >
			 	</div> 
			  </div>
			 </div>
			 <div class="col-md-12 mrg-Top">
			   <div class="form-group">
			 	<div class="col-md-5 control-label">Type of work</div><div class="col-md-1 control-label">:</div><div class="col-md-6 "> <div class="selectRow"> <select   name="paymentTypeOfWork"  id="typeofworkselect" data-placeholder="Select an option" class="select2-offscreen" multiple style="width:100%;">
	           	</select></div></div>
	           </div>
			 </div>
			 <div class="col-md-12 mrg-Top">
			  <div class="form-group">
			 	<div class="col-md-5 control-label">WO Amount </div><div class="col-md-1 control-label">:</div><div class="col-md-6 "><input type="text" name="totalAmtToPay" id="totalAmtToPay" value="" class="form-control rabill-class-select  heightthirty" style="display:inline;" readonly="readonly" /></div>
			   </div>
			 </div>
              <div class="col-md-12 mrg-Top">
                <div class="form-group">
				<!-- <div class="row"><div class="col-md-5 mrg-Top" style="padding-left: 26px;">Type of work</div><div class="col-md-1 mrg-Top">:</div><div class="col-md-5 mrg-Top"><p id="typeOfWork"></p></div></div> -->
				 <div class="col-md-5 control-label">Bill Date</div><div class="col-md-1 control-label">:</div><div class="col-md-6 input-group"><input  type="text" class="form-control rabill-class-select  heightthirty" name="billDate" id="billDate" onkeydown="return false" placeholder="dd-mm-yyyy" autocomplete="off"/>
				 <label class="input-group-addon btn" for="billDate">
						<span class="fa fa-calendar"></span>
				    </label></div>
				
				 </div>
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
              <th rowspan="2" class="text-center vertical-alignment" style="width:45%;">Description</th>
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
              <td class="text-center td-active"><input type="text"  name="raCc" id="raCc" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"><input type="text"  name="raPc" id="raPc" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline" ><input type="text"  name="raAmountToPay" id="raAmountToPay" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;" value="" readonly="readonly"/></td><!--  amount to pay-->
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
              <td class="text-center td-active"><input type="text"  id="raTotalCc" name="raTotalCc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active"><input type="text"  id="raTotalPc" name="raTotalPc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline">
               <input type="text" id="totalCc" name="totalCc" class="addFractionAndMakeInrFormat" autocomplete="off"  style="border:none;text-align:center;" value="0" readonly>                
              </td>
            </tr>
            <tr>
              <td class="text-center">II</td>
              <td class="text-center"><span>Paid Amount</span></td>              
              <td class="text-center"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="raPaidAmnt addFractionAndMakeInrFormat" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0.00" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
             <tr>
              <td class="text-center">a)</td>
              <td class="text-center"><span>Advance Deduction</span></td>              
              <td class="text-center"><input type="text" name="raCcDeductionAmt" id="raCcDeductionAmt" class="CcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center"><input type="text"  name="raPevDeductionAmt" id="raPrevDeductionAmt" class="PcAmnt addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="form-control raDeductionAmt addFractionAndMakeInrFormat" name="raDeductionAmt" id="raDeductionAmt" autocomplete="off" onkeypress="return isNumber(this, event)"  onkeyup="caldeductionamt('ra')"  style="text-align:center !important;width:100%;" value="0.00" readonly="true"/></td> 
            </tr>
			<tr>
              <td class="text-center">b)</td>
              <td class="text-center input-outline"><span>Security Deposit(E)</span>
              <select name="securityPer" id="securityPer" onchange="securityperchange()">
              <option value="0">0%</option><option value="1">1%</option><option value="2">2%</option> <option value="3">3%</option><option value="4">4%</option><option value="5">5%</option>
                <option value="6">6%</option><option value="7">7%</option><option value="8">8%</option> <option value="9">9%</option><option value="10">10%</option>
              </select></td>
              <td class="text-center"><input type="text"  name="secDepositCumulative" class="CcAmnt addFractionAndMakeInrFormat"  id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="secDepositPrevCerti" class="PcAmnt addFractionAndMakeInrFormat" id="secDepositPrevCerti" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="raDeductionAmt addFractionAndMakeInrFormat" name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="0.00" readonly/></td>
            </tr>
			<tr>
              <td class="text-center">c)</td>
              <td class="text-center input-outline"><span>Petty Expenses</span></td>
              <td class="text-center"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt addFractionAndMakeInrFormat"  id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt addFractionAndMakeInrFormat" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="form-control raDeductionAmt addFractionAndMakeInrFormat" name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off" onkeypress="return isNumber(this, event)"  style="text-align:center !important;width:100%;" value="0.00" onkeyup="caldeductionamt('pe')"  readonly="true"/></td>
            </tr>
			 <tr>
              <td class="text-center"></td>
              <td class="text-center input-outline"><span>Others</span></td>
              <td class="text-center"><input type="text"  name="otherAmtCumulative" class="CcAmnt addFractionAndMakeInrFormat" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt addFractionAndMakeInrFormat" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="form-control raDeductionAmt addFractionAndMakeInrFormat" name="other" id="other"  autocomplete="off"  style="text-align:center !important;width:100%;" value="0.00" onkeypress="return isNumber(this, event)" onkeyup="caldeductionamt('ot')"  readonly="true"/></td>
            </tr> 
            <tr>
              <td class="text-center">d)</td>
              <td class="text-center"><span>Recovery</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>
              <td class="text-center"  ><input type="text"  name="cumulativeRecovery" class="CcAmnt addFractionAndMakeInrFormat" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"   ><input type="text"  name="previousRecovery" class="PcAmnt addFractionAndMakeInrFormat" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"><input type="text" id="currentRecoveryAmount" class="raDeductionAmt addFractionAndMakeInrFormat" value="${recoveryAmount}"  style="border:none;width:20%;text-align:right;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount"> <a class="" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
			</tr>
			<tr style="font-size: 15px;">
              <td class="text-center"></td>
              <td class="text-center td-active input-outline"><span>Total Amount (B)</span></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtCumulative" id="totalDeductionAmtCumulative" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtPrevCerti" id="totalDeductionAmtPrevCerti" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center td-active input-outline"><input type="text" name="totalAmtCurrnt" id="totalAmtCurntDeduc" class="addFractionAndMakeInrFormat"  autocomplete="off"   style="border:none;text-align:center;" value=""/></td>
            </tr>
			<tr>
              <td class="text-center">III</td>
              <td class="text-center input-outline"><span>Outstanding advance (F)</span> </td>
              <td class="text-center"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  name="outstandingAdvCurrent" id="outstandingAdvCurrent"  autocomplete="off"  style="border:none;text-align:center;" value=""  readonly="true"/></td>     
            </tr>
			<tr>
              <td class="text-center" >IV</td>
              <td class="text-center input-outline"><span>Release advance (G)</span> </td>
              <td class="text-center"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt">  <input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt" class="addFractionAndMakeInrFormat"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="" onkeyup="caldeductionamt('btn')" readonly/></td>
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
			 <td class="text-center td-active"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" class="addFractionAndMakeInrFormat" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline"><input type="text" name="finalAmt" id="finalAmt" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="0" ></td>
			</tr>
			<tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords"></td>			
			</tr>
			<tr>
			<td colspan="2" class="text-center">Payment Area</td>
			<td colspan="3" class="text-center"><a class="certificate-link" href="javascript:void(0);" id="certificate-id" data-toggle="modal" data-target="#modal-certificatepaymentra-click">Abstract</a></td>
			</tr>
          </tbody>
        </table>
      </div>
    </div>    
    <div class="col-md-12 text-center center-block">
   	<input type="submit" name="rasubmitBtn" id="rasubmitBtn"  class="btn btn-warning btn-custom-width" value="Submit" />
    </div> 
    <div id="appendWorkAreaId"> </div>
    <div id="appendPrevDeductVal"> </div>
    <div id="recoveryAmountDetails"> </div>

       <div class="col-md-6 Mrgtop10" style="width:50%;">
			<label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
			<div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
		 		<textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title="${woLevelPurpose}" placeholder="${woLevelPurpose}"  id="NoteId"  autocomplete="off" placeholder="${woLevelPurpose}"></textarea> 
			</div>
		</div>
      
  <div class="col-md-12 mrg-Top" style="" id="hideIt">
  <div class="table-responsive scroll-rabill-tbl1">
   <table class="table table-bordered tbl-rabill">
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
     <div id="appendBillDetailsTotal"  style="float:  right;margin-bottom:30px;"></div>
</form:form>

	</div>
                  
  </div>
  </div>
 <!-- certificate of payment ra bill modal popup start -->
  
<!-- Modal -->
<div id="modal-certificatepaymentra-click" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg modal-rabill-customwidth">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Certificate Of Payment</strong></h4>
      </div>
      <div class="modal-body modal-body-scroll">
       <div class="table-responsive" style="overflow:hidden;">  <!-- scroll-rabill-tbl -->
        <table class="table table-new"style="margin-bottom:-1px;"><!--  tbl-rabill -->
        <thead class="cal-thead-inwards">
         <tr>
              <th rowspan="2" class=" vertical-alignment"><input type="checkbox" name="checkAll" id="checkAll" style="width: 100%;height: 16px;cursor: pointer;"></th>
              <th rowspan="2" class="vertical-alignment">Description of Work</th>
              <th rowspan="2" class="vertical-alignment">Total Quantity</th>
              <th rowspan="2" class="vertical-alignment">Rate</th>
              <th rowspan="2" class="vertical-alignment">Unit</th>
              <th colspan="2" >Cumulative Certified</th>
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
       <div style="height:auto;overflow-y:scroll;max-height:400px;">
       <table class="table table-bordered fixed-tbl-rabill"> -->
         <tbody id="paymentByArea" class="tbl-fixedheader-tbody">
          <tr>
         <td class="text-center"style="width:5.09%;"><input type="checkbox"/></td>
         <td class="text-left"style="width:9.09%;">NMR-1(20.09.2018-15-10.2018)</td>
         <td class="text-right"style="width:9.09%;">Total</td>
         <td class="text-right"style="width:9.09%;">Rate</td>
         <td class="text-right"style="width:9.09%;">Unit</td>
         <td class="text-right"style="width:9.09%;">-</td>
         <td class="text-right"style="width:9.09%;">99,982.73</td>
         <td class="text-right"style="width:9.09%;"></td>
         <td class="text-right"style="width:9.09%;">99,982.73</td>
         <td class="text-right"style="width:9.09%;"></td>
         <td class="text-right"style="width:9.09%;"></td>
          </tr>
         
         <tr>
          <td></td>
          <td><h5><strong>NMR WORKS</strong></h5></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          </tr>
          <tr>
          <td></td>
          <td><strong>Hole Packing</strong></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          </tr>
          <tr>
          <td></td>
          <td><h6><strong>Hole Packing</strong></h6></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          </tr>
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
</div>
 <!-- certificate of payment ra bill modal popup end -->
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
          <th>Total Amount</th>
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
         <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="recoverySubmit()">Submit</button>
        </div>
      </div>
    </div>
  </div>
</div>

 <!--duduction row lwngth --> 
 <input type="hidden" id="deductionrowlength" name="deductionrowlength">
 <input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
 
 
</body>

   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>
	   $(document).ready(function() {
		 //checking previous bill and bill number format
			$("#oldBillNo").on("blur",function(){
				var oldBillNo=$(this).val();
				var flag=validateForm();
				if(flag==false){return false;}
				if(oldBillNo.length<=2){
					return false;
				}
				var array=new Array();
				array=oldBillNo.split("/");
				var billType=$("#billType").val();
				if(array[0]!=billType){
					alert("please enter correct format of Bill Number.");
					$("#oldBillNo").val('');
					return false;
				}
				if(!isNum(array[1])){
					alert("please enter correct format of Bill Number.");
					$("#oldBillNo").val('');
					return false;
				}
				if(array[1]=="0"){
					alert("please enter correct format of Bill Number.");
					return false;
				}
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();
				var isInvoiceOrBillNumberValidation="billNumber";
				validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation);
			});
		 
			$("#billInvoiceNo").on("blur",function(){
				debugger;
				var actualBillInvoiceNo=$("#actualBillInvoiceNo").val();
				var billInvoiceNo=$(this).val();
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();
				//if(actualBillInvoiceNo!=billInvoiceNo){
					var isInvoiceOrBillNumberValidation="invoiceNumber";
					validateBillInvoiceNumber(workOrderNo,ContractorId,billInvoiceNo,isInvoiceOrBillNumberValidation)
				//}
			});
		 
/* 		 function validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation){
			 debugger;
			 var url = "checkBillNoExistsOrNot.spring";
			 $.ajax({
				  url : url,
				  type : "get",
				  async: false,
				 data:{
					 workOrderNo:workOrderNo,
					 ContractorId:ContractorId,
					 oldBillNo:oldBillNo,
					 isInvoiceOrBillNumberValidation:isInvoiceOrBillNumberValidation
				 },
				  contentType : "application/json",
				  success : function(data) {
						if(data!="true"){
							 $('#rasubmitBtn').attr('disabled',false);
							 $('#submitBtn').attr('disabled',false);	
						}else{
							alert("This bill no already used please enter another bill no."); 
							$('#rasubmitBtn').attr('disabled',true);
							$('#submitBtn').attr('disabled',true);
							if(isInvoiceOrBillNumberValidation=="invoiceNumber"){
								$("#billInvoiceNo").val("");
								$("#billInvoiceNo").focus();
							}
							return error=false;
						}
				  }
			});
				return error;
		 } */
		 
		 
			$(".up_down").click(  function() {
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
		});
    
	    $("#hideIt").hide();
	  //opening abstract popup
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
		/*  <!--Checkbox select all code-->  */
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
					     $('input:checkbox').not(this).prop('checked',false);
					     return false;
					 }
		  });
			sumOfArea();    
		});
		//for date picker 
		$(function(){
			$("#billDate").datepicker({ dateFormat: 'dd-mm-yy' });
		 });						
	</script>
</html>
