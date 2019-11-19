<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
<!-- <title>Create Adv Bill</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


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
</head>

<style>
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new tbody tr td:first-child{min-width:20px;text-align:center;width:45px;}
.table-new tbody tr td {border-top: 0px !important;}
.tbl-rabill thead tr th{font-size:15px;font-weight:700}
.tbl-rabill tbody tr td{font-weight:700}
#paymentByArea tr td{border:1px solid #000;font-weight:bold;}
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:76px;
}
.scroll-rabill-tbl1{
 width: calc(100% - 0px) !important;
 height:250px;
}
.workorder-class-select{
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
.ui-icon{
width:100%;
height:100%;
}
.custom-combobox-toggle{
 position:absolute;
 height:25px;
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
.input-group-addon{border:1px solid #ccc !important;height:34px;}
.form-group{margin-bottom:5px !important;}
.form-control{height:34px;}


</style>

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
				
       <form:form modelAttribute="indentReceiveModelForm" id="contractorBill" class="form-horizontal" action="generateAdvancePaymentBill.spring">
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
             <!--  <div style="width:20%;"></div> -->
              <div class="col-md-12">
                <div class="form-group">
                   <label class="control-label col-md-5">Contractor&nbsp;Name</label>
              <label class="control-label col-md-1">:</label>
              <div class="col-md-6"><input type="text" class="form-control border-none" id="ContractorName" name="ContractorName"  onkeyup="return populateData(this, event);"  autocomplete="off" /><input type="hidden"  id="ContractorId" name="ContractorId"    /></div>
                </div>
              </div>
			  <div class="clearfix"></div>
			   <div class="col-md-12"> 
			    <div class="form-group">
			   <label class="control-label col-md-5">Pan Card No</label> <label class="control-label col-md-1">:</label><div class="col-md-6" ><input type="text"  class="heightthirty" id="PanCardNo" name="PanCardNo"   style="border:none;width:100%;;" readonly /></div></div>
			    </div>
			  <div class="clearfix"></div>
			  
			  <div class="col-md-12">
			    <div class="form-group">
			 	 <label class="control-label col-md-5">Account No</label><label class="control-label col-md-1">:</strong></label><div class="col-md-6">
				  <input type="text"  class="heightthirty" id="AccountNo" name="AccountNo"    style="border:none;width:100%;;" readonly />
				  <input type="hidden" class="heightthirty" id="contractorGSTIN" name="contractorGSTIN"   style="border:none;width:100%;" readonly />
			  </div></div>
			  </div>
			  <div class="clearfix"></div>
              <div class="col-md-12">
               <div class="form-group">
              <label class="control-label col-md-5">IFSC & Bank Name</label><label class="control-label col-md-1">:</label><div class="col-md-6" ><input type="text"  class="heightthirty" id="ifscCode" name="ifscCode"   style="border:none;width:100%;" readonly /></div></div>
			  </div>
			  <div class="clearfix"></div>
              <div class="col-md-12">
               <div class="form-group">
              <label class="control-label col-md-5">Mobile No  </label><label class="control-label col-md-1">:</label><div class="col-md-6"><input type="text"  class="heightthirty" id="MobileNo" name="MobileNo"   style="border:none;width:100%;" readonly /></div></div>
			  </div>
			  <div class="clearfix"></div>             
              </td>
              <td style="width:50%;">
             
              <div class="col-md-12">
               <div class="form-group">
			  <label class="col-md-5 control-label">Project</label><label class="col-md-1 control-label">:</label><div class="col-md-6"><input type="text" value="${SiteName}" class="form-control heightthirty"  style="border:none;" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			  </div>
			  </div>
			  <div class="clearfix"></div>
			  <div class="col-md-12">
			 <!--  <div style="width:45%;float:left;">	Type of work </div>	<div style="width:10%;float:left;"></div>	<div style="width:45%;float:left;">	
				</div> -->
				<div class="form-group">
				    <label class="col-md-5 control-label">Work order No</label><label class="col-md-1 control-label">:</label>
				   <div class="col-md-6">
				    <input type="hidden" name="site_id" id="site_id" value="${SiteId}">
				    <input type="hidden" name="billType" id="billType" value="ADV">
				    <input type="hidden" name="advPage" id="advPage" value="true">
				    <input type="hidden" name="raPage" id="raPage" value="false">
				    <input type="hidden" name="approvePage" id="approvePage" value="false">
				    <input type="hidden" name="typeOfWork" id="typeOfWork" value="${WorkOrderBean.typeOfWork}">		
				    <input type="hidden" name="nextLevelApprovelEmpID" value="${WorkOrderBean.approverEmpId}">
				    <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="0"> 
				    <select  name="workOrderNo" id="workOrderNo" class="form-control workorder-class-select mrg-Top heightthirty" style="padding:0px;">
				
				    </select>
				    </div>
			    </div>
			    </div>
			    <div class="clearfix"></div>
			     <div class="col-md-12">
				   <div class="form-group">
				     <label class="control-label col-md-5"> Bill Type </label><label class="col-md-1 control-label">:</label><div class="col-md-6" >
					<select name="isOldOrNewBill" id="isOldOrNewBill" class="form-control workorder-class-select  heightthirty"> 
					<option value="0">----Select Type----</option>
					<option value="Old">Previous Bill</option>
					<option value="New">Current Bill</option>
					</select>
				    </div>
				   </div>
				</div>
				<div class="clearfix"></div>
				<div class="col-md-12">
				  <div id="showBillText" class="form-group" style="display: none;"><label class="col-md-5 control-label">Enter Old Bill No</label><label class="col-md-1 control-label">:</label><div class="col-md-6"><input type="text" name="oldBillNo" id="oldBillNo" value="" placeholder="e.g. ADV/10" class="form-control workorder-class-select heightthirty" /></div></div>
				</div>
				<div class="clearfix"></div>
				<div class="col-md-12">
					<div class="form-group">
						<label class="col-md-5 control-label">Adv Bill No</label><label class="col-md-1 control-label">:</label><div class="col-md-6"><input type="text" class="form-control workorder-class-select" name="advBillNo" id="advBillNo"   value=""   readonly="readonly"/></div><!-- ${billBean.raBillNo} -->
	              	</div>
              	</div>
              	<div class="col-md-12">
					<div class="form-group">
						<label class="col-md-5 control-label">Invoice No</label><label class="col-md-1 control-label">:</label><div class="col-md-6">
							<input type="text" class="form-control workorder-class-select" name="billInvoiceNo" id="billInvoiceNo"   value="" autocomplete="off"/>
							<input type="hidden" name="actualBillInvoiceNo" id="actualBillInvoiceNo" >
						</div>
	              	</div>
              	</div>
              	<div class="clearfix"></div>
              	<div class="col-md-12">
              	 <div class="form-group">
              	   <label class="col-md-5 control-label">Type of work</label><label class="col-md-1 control-label">:</label>
              	<div class="col-md-6">
				<div class="selectRow"><select  name="paymentTypeOfWork"  id="typeofworkselect" data-placeholder="Select an option" class="form-control select2-offscreen" multiple>
        		</select>
        		</div>
        		</div>
              	 </div>
        		</div>
				
				<div class="clearfix"></div>
				<div class="col-md-12">
				<div class="form-group">
               <label class="col-md-5 control-label">WO Amount</label><label class="col-md-1 control-label">:</label><div class="col-md-6"><input type="text" name="totalAmtToPay" id="totalAmtToPay" class="form-control workorder-class-select mrg-Top heightthirty" value="" style="display:inline;"  readonly="readonly" /></div>
               </div>
               </div>
               <div class="clearfix"></div>
               <div class="col-md-12">
               <div class="form-group">
               <label class="col-md-5 control-label">Bill Date</label><label class="col-md-1 control-label">:</label>
               <div class="col-md-6 input-group">
               <input  type="text" name="billDate" id="billDate" class="form-control workorder-class-select" class="workorder-class-select" onkeydown="return false" placeholder="dd-mm-yyyy" autocomplete="off"/>
               <label class='btn input-group-addon'  for='billDate'><span class='fa fa-calendar'></span></label></div>
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
                 <input type="text" id="totalCc" name="totalCc"  autocomplete="off"  style="border:none;text-align:center;" value="" readonly>
              </td>
            </tr>
			 <tr>
              <td class="text-center" style="border:1px solid #000;">II</td>
              <td class="text-center" style="border:1px solid #000;"><span>Paid Amount</span></td>              
              <td class="text-center" style="border:1px solid #000;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;"><input type="text" class="raPaidAmnt" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
            <tr>
              <td class="text-center">a)</td>
              <td class="text-center"><span>Advance Deduction</span></td>
              
              <td class="text-center"><input type="text" name="raCcDeductionAmt" id="raCcDeductionAmt" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center"><input type="text"  name="raPevDeductionAmt" id="raPrevDeductionAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="raDeductionAmt" name="raDeductionAmt" id="raDeductionAmt" autocomplete="off"  onkeyup="caldeductionamt('ra')"  onkeypress="return isNumber(this, event)" style="border:none;text-align:center;" value="" readonly/></td> 
            </tr>
			<tr>
              <td class="text-center">b)</td>
              <td class="text-center input-outline"><span>Security Deposit(E)</span>
              <select name="securityPer" id="" onchange="" disabled>
              <option value="0">0%</option><option value="1">1%</option><option value="2">2%</option> <option value="3">3%</option><option value="4">4%</option><option value="5">5%</option>
                <option value="6">6%</option><option value="7">7%</option><option value="8">8%</option> <option value="9">9%</option><option value="10">10%</option>
              </select>
              </td>
              <td class="text-center"><input type="text"  name="secDepositCumulative" class="CcAmnt"  id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="secDepositPrevCerti" class="PcAmnt" id="secDepositPrevCerti" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="raDeductionAmt" name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off" onkeypress="return isNumber(this, event)" style="border:none;text-align:center;" value="" readonly/></td>
            </tr>
			<tr>
              <td class="text-center">c)</td>
              <td class="text-center input-outline"><span>Petty Expenses</span></td>
              <td class="text-center"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt"  id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text" class="raDeductionAmt" name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off"  onkeypress="return isNumber(this, event)"  style="border:none;text-align:center;" value="" onkeyup="caldeductionamt('pe')" readonly/></td>
            </tr>
	 <tr>
              <td class="text-center"></td>
              <td class="text-center input-outline"><span>Others</span></td>
              <td class="text-center"><input type="text"  name="otherAmtCumulative" class="CcAmnt" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  class="raDeductionAmt" name="other" id="other"  autocomplete="off"  style="border:none;text-align:center;" value="" onkeypress="return isNumber(this, event)" onkeyup="caldeductionamt('ot')" readonly/></td>
            </tr> 
            <tr>
              <td class="text-center"style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center"  ><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"   ><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><input type="text" class="raDeductionAmt" name="" id=""  autocomplete="off"   style="border:none;text-align:center;" value="" readonly/></td>
        </tr>
            <tr style="font-size: 15px;">
              <td class="text-center"></td>
              <td class="text-center td-active input-outline"><span>Total Amount (B)</span></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtCumulative" id="totalDeductionAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active"><input type="text"  name="totalDeductionAmtPrevCerti" id="totalDeductionAmtPrevCerti"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline"><input type="text" name="totalAmtCurrnt" id="totalAmtCurntDeduc"  autocomplete="off"   style="border:none;text-align:center;" value="" readonly/></td>
            </tr>
			<tr>
              <td class="text-center">III</td>
              <td class="text-center input-outline"><span>Outstanding advance (F)</span></td>
              <td class="text-center"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="outstandingAdvTotalAmthidden" value="0"></td>
              <td class="text-center"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline"><input type="text"  name="outstandingAdvCurrent" id=outstandingAdvCurrent  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
			<tr>
              <td class="text-center">IV</td>
              <td class="text-center input-outline"><span>Release advance (G)</span> </td>
              <td class="text-center"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt">  <input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt"   style="border:none;text-align:center;" value="" readonly/></td>            
              <td class="text-center input-outline"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount" style="border:none;text-align:center;" value="" onkeyup="calreleaseAmnt()" readonly/></td>
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
			 <td class="text-center td-active"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline"><input type="text" name="finalAmt" id="finalAmt" style="border:none;text-align:center;" value="" readonly></td>
			</tr>			
			<tr style="font-size: 15px;">
			 <td colspan="2" class="text-center td-active input-outline"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords"></td>
			 <!-- <td  class="text-center td-active"></td>
			 <td  class="text-center td-active"></td> -->
			</tr>
			
			<tr>
			<td colspan="2" class="text-center">Payment Area</td>
			<td colspan="3" class="text-center"><a class="certificate-link" href="javascript:void(0);" id="certificate-id" data-toggle="modal" data-target="#modal-certificatepayment-adv">Abstract</a></td>
			</tr>
          </tbody>
        </table>
      </div>
    </div>    
    <div class="col-md-12 text-center center-block">
   	<input type="submit" name="submitBtn" id="submitBtn"  class="btn btn-warning btn-custom-width" id="closeBtn"	value="Submit" />
    </div> 
    <div id="appendBillDetails1">
  
  	</div>
    <div id="appendWorkAreaId">
    
    </div>
    
    <div class="col-md-12"><label>Remarks : </label><br>
      <textarea rows="5" cols="40" name="remarks">
    
    </textarea>
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
   <div id="appendBillDetailsTotal"  style="float:  right;margin-bottom:30px;"></div>

<!-- modal popup for certificateofpayment adv start -->

<div id="modal-certificatepayment-adv" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg modal-rabill-customwidth">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Certificate Of Payment</strong></h4>
      </div>
      <div class="modal-body modal-body-scroll">
       <div class="table-responsive"> <!-- scroll-rabill-tbl -->
        <table class="table table-bordered table-new" style="margin-bottom:-1px;"><!--  tbl-rabill -->
        <thead class="cal-thead-inwards">
         <tr>
              <th rowspan="2" class=" vertical-alignment"><input type="checkbox" name="checkAll" id="checkAll"  style="width: 100%;height: 16px;cursor: pointer;"></th>
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
        <!--</table>
	 </div>
       <div>style="height:auto;overflow-y:scroll;max-height:400px;"
       <table class="table table-bordered  table-new"> fixed-tbl-rabill -->
         <tbody id="paymentByArea" class="tbl-fixedheader-tbody">
          <tr>
         <td class="text-center"><input  type="checkbox"  style="width: 100%;height: 16px;cursor: pointer;"/></td>
         <td class="text-left">NMR-1(20.09.2018-15-10.2018)</td>
         <td class="text-right">Total</td>
         <td class="text-right">Rate</td>
         <td class="text-right">Unit</td>
         <td class="text-right">-</td>
         <td class="text-right">99,982.73</td>
         <td class="text-right"></td>
         <td class="text-right">99,982.73</td>
         <td class="text-right"></td>
         <td class="text-right"></td>
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
         <button type="button" class="btn btn-warning" onclick="return paymentAreaBtn()">Submit</button>
        </div>
      </div>
      </div>
      
    </div>
  </div>
<!-- modal popup for certificateofpayment adv end -->
 </div>
                  
  </div>
  </div>
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
					if(oldBillNo.length<=3){
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
					validateBillInvoiceNumber(workOrderNo,ContractorId,billInvoiceNo,isInvoiceOrBillNumberValidation);
					//}
				});		
			 	$("#hideIt").hide();
				$(".up_down").click(  function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});

			});
/* 		function validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation){
			debugger;
			var error=true;
			var url = "checkBillNoExistsOrNot.spring";
			 $.ajax({
				  url : url,
				  type : "get",
				  async: false,
				 data:{
					 workOrderNo:workOrderNo,
					 ContractorId:ContractorId	,
					 oldBillNo:oldBillNo,
					 isInvoiceOrBillNumberValidation:isInvoiceOrBillNumberValidation
				 },
				  contentType : "application/json",
				  success : function(data) {
						if(data!="true"){
							 $('#rasubmitBtn').attr('disabled',false);
							 $('#submitBtn').attr('disabled',false);	
						}else{
							alert("Bill number already exists, please enter another bill number.");								
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
		 }
		 */
		
			//opening abstract popup
			$("#certificate-id").click(function(){						
				var flag=validateForm();
				if(flag==false){return false;}
					$("#tbl-2").show();
			});
			//select all checkbox 
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
				//for date picker 
				$(function(){
				   $("#billDate").datepicker({ dateFormat: 'dd-mm-yy' });
				});
			</script>

</html>
