<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.WorkOrderBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<%log("Print Page"); %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">	
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/loader.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<script src="//code.jquery.com/jquery.min.js"></script>
		<script src="jquery.stickytable.min.js"></script>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
		<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />
		<jsp:include page="./../CacheClear.jsp" /> 
        <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
		<style>
		  .NMRBillstableId thead tr td{border:1px solid #000;}	
          table.dataTable {border-collapse: collapse !important;}
		  .tbl-td{border-top:1px solid #fff !important;padding:3px;	}
		  .tbl-td-bottom{border-bottom:1px solid #fff !important;padding:3px;}
	      .conatianer1{float: left;}
	      body{font-weight:600;}
          .bordsertable th, .bordsertable td, .bordsertable tbody {border: 1px solid #000 !important;border-collapse: collapse;}
          .bordsertable thead{background-color:#ccc;} 
          .page-body{padding: 20px;}
          .tbl-border-top{border-bottom: none !important;margin-top:-1px;}
		  .tbl-border-top{margin-bottom:0px;}
          .tbl-border-top tr .td-top{border-top:none !important;}
		  .vertical-alignment {vertical-align: middle !important;}
		  .bck-ground{background-color:#ccc;}
		  .td-active{background-color:#dbecf6;}
		  .td-active input{background-color:#dbecf6;}
		  .td-background{background-color:#dbecf6;}
		  .td-active input:focus{}
		  .td-active input{font-weight:bold;}
		  .border-none input{border:none !important;}
		  .input-outline input{outline:none;}
		  .tbl-corner-right{height:73px;margin-right:-16px;width: 300px;text-align: center;}
		  @media print{
		   .btn-warning{display:none;}
		   .breadcrumb{display:none;}
		   .left_col{display:none;}
		   .nav_menu{display:none;}
		   thead {display: table-row-group;}
		   .img-workorder-logo{margin-top:5px;}
		   .head-logo-workorder{width:350px;float:left;}
		   .tbl-corner-right{height:90px;margin-right:-16px;width: 250px;text-align: center;}
		   .border-print{border-top:1px solid #000;}
		   .print-tblheight{height:100px;}
		  }
		  .mrg-Top {margin-top: 5px;}
		  .marginbottom{margin-bottom:30px;}
		  .custom-combobox {position: relative;display: inline-block;}
		  .custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;}
		  .custom-combobox-input {margin: 0;padding: 5px 10px;}
		  .ui-autocomplete-input{width:200px !important;height:30px;}
		  .ui-autocomplete:hover{font-size:14px;font-weight:700;}
		  .ui-widget:hover {
  				     font-family: unset !important;
   					 font-size: 14px !important;
			}
			.ui-widget {
  				     font-family: unset !important;
   					 font-size: 14px !important;
			}
			#datepickerIcon{
				float: left;
	   		    padding-right: 25px;
	    		border: 1px solid #ccc !important;
	    		margin-left: -2px !important;
	    		height: 30px;
			}
			input{height:30px}
			.Date{
				width:70%;
				float:left;
			}
			
			
.red-tooltip  {
  position: relative;
  display: inline-block;
  opacity: 1 !important;
  float:right;
  top: 5px;
  font-size: 20px;

}

.ui-tooltip{
    max-width: 100%;
}
.WDDwtailsTablecls{
	background-color:#FFF;
	color:#000;
	border:1px solid #000;	
	overflow:hidden;
}
.WDDwtailsTablecls thead th{
	background-color:#ccc;
	color:#000;
	border:1px solid #000 !important;
	font-weight:bold;	
}
.WDDwtailsTablecls tbody tr td{
	background-color:#fff;
	color:#000;
	border:1px solid #000;	
}
</style>		
</head>
<body class="nav-md">
	<noscript>
		<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	</noscript>
 <form action="generateNMRBill.spring" method="POST" >
	<div class="container body" id="mainDivId">
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
		       <table class="table table-border-certificateofpayment bordsertable" style="margin-bottom: -1px;">
         		 <tbody>
            	  <tr>
              		<td rowspan="2" style="width:20%"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 90px;" /></td>
             		<td  style="width:80%" class="text-center bck-ground">
              		<h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4>
              		<input type="hidden" name="nextLevelApproverEmpID" id="nextLevelApproverEmpID" value="${nextLevelApproverEmpID}">
					<input type="hidden" name="noOfRowsToIterate" id="noOfRowsToIterate">
					<input type="hidden" name="noOfWDToIterate" id="noOfWDToIterate">
					<input type="hidden" name="approvePage" id="approvePage" value="false">
					<input type="hidden" name="siteId" id="siteId" value="${siteId}">
				   	<input type="hidden" name="billType" id="billType" value="NMR">
					<input type="hidden" name="site_id" id="site_id" value="${SiteId}">			
                    <input type="hidden"  id="ContractorId" name="ContractorId"    />
                    <input type="hidden" id="isPrevBillIsRunning" name="isPrevBillIsRunning">              
              		</td>
            	 </tr>
                 <tr>
                   <td colspan="2" style="width:80%" class="text-center bck-ground"><h5><strong>Abstract</strong></h5></td>
                 </tr>           
         	   </tbody>
         	</table>
         	
		    <table style="border:1px solid #000 !important;width:100%;margin-top:-1px;" class="bordsertable">
               <tbody>
                  <tr>
                     <td style="width:50%;padding: 10px 0px 0px 25px;">
                          <div style="width:100%" class="mrg-Top">
                               <div style="width:30%;float:left;"><strong>Name of the project </strong></div>
                               <div style="width:10%;float:left;"><strong>:</strong></div>
                               <div style="width:60%;float:left;margin-bottom: 17px;"><strong>${SiteName}</strong></div>
                           </div>
                           <div style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Name of the Contractor </strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 10px;"><input type="text" class="form-control border-none" id="ContractorName" name="ContractorName" style="width:  80% !important;height:30px;"  autocomplete="off"   /></div>
                            </div>
							                              
                               <input type="hidden"  id="ContractorId" name="ContractorId" />
                               <input type="hidden" id="contractorGSTIN" name="contractorGSTIN" readonly />
                            <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"> <strong>Work Order/Ref. No </strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 10px;"><select id="workOrderNo" name="workOrderNo" style="width: 80%;height:30px;padding: 0px;" class="form-control"></select></div>
                             </div>                              
                             <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Block</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 10px;">
                                
                                  <select id="BlockID" name="Blocks"  style="width:  80%;" multiple="multiple">
	                               <c:forEach items="${blocksMap }" var="block">
	                           	   <option value="${block.key}@@${block.value}">${block.value}</option>
	                               </c:forEach>
	                              </select>
                                </div>
                             </div>                               
                       </td>
                       <td colspan="5" style="width:50%;padding: 10px 0px 0px 25px;">
                              <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Bill Date</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;" class="input-group">
                                   <input  type="text" name="billDate" id="billDate"  autocomplete="off" class="form-control rabill-class-select" style="display:inline;height:30px;width:35%;" onkeydown="return false;"/>
                                   <label class="input-group-addon btn datepicker-paymentreq-fromdate" id="datepickerIcon" onclick="openCalender()" for="fromDate">
										<span class="fa fa-calendar"></span>
								   </label>
								</div>
                              </div>
                              <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Type of work</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 12px;"><strong>Labour Supply</strong></div>
                              </div>
                              <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Work order No</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 12px;"><strong id="printWorkOrderNo">Under Progress</strong></div>
                              </div>
                              <div  style="width:100%" class="">
                                <div style="width:30%;float:left;"><strong>Bill Type</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;">
                                 <select name="isOldOrNewBill" id="isOldOrNewBill" class="form-control workorder-class-select mrg-Top" style="width:50%;height:28px;padding:0px !important;">
									<option value="0">----Select Type----</option>
									<option value="Old">Previous Bill</option>
									<option value="New">Current Bill</option>
									</select>                                
                                </div>
                              </div>
                               
                              <div  style="width:100%;display: none;" class="mrg-Top" id="showBillText">
                                <div style="width:30%;float:left;"><strong>Enter Old Bill No</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;">
                                <input type="text" name="oldBillNo" id="oldBillNo" value="" placeholder="e.g. NMR/10" style="display:inline;width:50%;height:28px;" class="form-control workorder-class-select mrg-Top" autocomplete="off" /></div>
                              </div>
                              <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong> Bill No</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 10px;">
                                <strong id="printnmrBillNo">Under Progress</strong>
                                <input type="hidden" name="NMRBillNo" id="NMRBillNo">
                                <input type="hidden" name="actualBillNumber" id="actualBillNumber">
                                </div>
                              </div>
                              
                               <div  style="width:100%" class="mrg-Top">
                                <div style="width:30%;float:left;"><strong>Invoice No</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong></div>
                                <div style="width:60%;float:left;margin-bottom: 10px;">
                               <!--  <strong id="printnmrInvoiceBillNo">Under Progress</strong> -->
                                
                                <input type="text" name="billInvoiceNo" id="billInvoiceNo" style="display:inline;width:50%;height:28px;" class="form-control workorder-class-select mrg-Top" autocomplete="off" />
                                <input type="hidden" name="actualBillInvoiceNo" id="actualBillInvoiceNo">
                                </div>
                              </div>
                               <div  style="width:100%;" class="mrg-Top">
                                <div style="width:30%;float:left;margin-bottom: 10px;"><strong>WO Amount</strong></div>
                                <div style="width:10%;float:left;"><strong>:</strong><input type="hidden" name="totalAmtToPay" id="totalAmtToPay" /></div>
                                <div style="width:60%;float:left;" id="printtotalAmtToPay"></div>
                              </div>                             
                        </td>
                      </tr>
                  </tbody>
			</table>		 
				<input type="hidden"  id="hiddenpreviousPettyExpences" >
				<input type="hidden"  id="hiddenpreviousotherAmount" >	  
				<input type="hidden" id="hiddenRecoveryAmount">
		   <table style="border:1px solid #000;width:100%;margin-bottom:15px;margin-top:-1px;" class="bordsertable">
	             <thead style="font-size: 16px;">
	                  <tr>
                            <td colspan="5"></td>
                           	<td style="text-align:center;width:10%;" colspan="2">Bill Date</td>
                           	<td style="text-align:center;width:10%;">From</td>
                           	<td style="text-align:center;"><span id="fromDate1"></span>  </td>
                           	<td style="text-align:center;width:10%;">To</td>
                            <td style="text-align:center;width: 15%;"><span id="toDate1"></span> </td>
                       </tr>
		       <tr>
    	                   <th rowspan="2" style="padding:8px;text-align:center;width:5%;">S.NO</th>
                           <th rowspan="2" style="padding:8px;text-align:center;">Description</th>
                           <th rowspan="2" style="padding:8px;text-align:center;">Total Qty</th>
                           <th rowspan="2" style="padding:8px;text-align:center;">Avg Rate</th>
                           <th rowspan="2" style="padding:8px;text-align:center;">Unit</th>
                           <th colspan="2" style="padding:8px;text-align:center;">Cumulative Certified</th>
                           <th colspan="2" style="padding:8px;text-align:center;">Previous Certified</th>
                           <th colspan="2" style="padding:8px;text-align:center;">Current Certified</th>
                       </tr>
                       <tr>
                            <th style="padding:8px;text-align:center;">Qty</th>
                            <th style="padding:8px;text-align:center;">Amount</th>                         
                            <th style="padding:8px;text-align:center;">Qty</th>
                            <th style="padding:8px;text-align:center;">Amount</th>                         
                            <th style="padding:8px;text-align:center;">Qty</th>
                            <th style="padding:8px;text-align:center;">Amount</th>
                       </tr>
		     </thead>
		   <tbody id="NMRAbstractTableData">
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>					      
					   </tr>
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>					      
					   </tr>
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>					      
					   </tr>
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>					      
					   </tr>
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>					      
					    </tr> 
					     <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">Petty Expenses</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>					      
					   </tr>
					   <tr>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">Others</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>
					       <td style="text-align:center;padding:13px;"></td>
					       <td style="text-align:center;padding:13px;">0</td>					      
					    </tr> 
					    <tr>
				            <td class="text-center"></td> 	
				           	<td class="text-center"><span>Recovery</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>
				           	<td style="text-align:center;padding:13px;"></td>
					       	<td style="text-align:center;padding:13px;"></td>
					       	<td style="text-align:center;padding:13px;"></td>
				            <td style="text-align:center;padding:13px;"></td>
					       	<td style="text-align:center;padding:13px;">0</td>
				            <td style="text-align:center;padding:13px;"></td>
				            <td class="text-center"  ><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
				            <td class="text-center"   ><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="" readonly/></td>
				            <td class="text-center"><input type="text" id="currentRecoveryAmount" name="currentRecoveryAmount" class="raDeductionAmt" value="0"  style="border:none;width:20%;text-align:right;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount"> <a class="" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td> <!-- ${recoveryAmount} -->
						</tr>
						<tr>
					       <td style="text-align:center;padding:5px;"><h5><strong></strong></h5></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>					       
					    </tr>
					    <tr>
					       <td style="text-align:center;padding:5px;"><h5><strong></strong></h5></td>
					       <td style="text-align:center;padding:5px;"><strong>Total Amount</strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>
					       <td style="text-align:center;padding:5px;"><strong></strong></td>					       
					    </tr>
					    <tr>
					       <td style="text-align:center;padding:5px;" colspan="6"><strong></strong></td>					
					       <td style="text-align:center;padding:5px;" colspan="5"><strong><a href="javascript:void(0);" onclick="openAbstract()" id="NmrAbstract">NMR Details</a></strong></td>
					    </tr>
					    <tr>
					       <td colspan="2" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>QS</strong></td>
					       <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>Sr.QS</strong></td>
					       <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>AGM/PM</strong></td>
					       <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>Project Incharge</strong></td>
					    </tr>
				</tbody>
			 </table> 			
     		 <div class="col-md-6 Mrgtop10" style="width:50%;">
			      <label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
				  <div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
				    <textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title="${woLevelPurpose}" placeholder="${woLevelPurpose}"  id="NoteId"  autocomplete="off" placeholder="${woLevelPurpose}"></textarea> 
				  </div>
		     </div>
			 <div class="col-md-12 text-center center-block">
				  <button class="btn btn-warning marginbottom" type="submit" id="formSubmitBtn" onclick="return createNMRBill();" >Submit</button>
			 </div>		
		    <!-- Ledger -->
			<div class="col-md-12">
			  <table style="border:1px solid #000;width:100%;margin-bottom:30px;margin-top:30px;display:none;" class="bordsertable"  id="paymentLedgerTable">
	               <thead>
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
      </div>
	</div>
 </div>  
<!-- modal popup for recovery click action start-->
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

        <tr>
        <td></td>
         <td class="text-right"><h4><strong>Total Amount</strong></h4></td>
         <td></td>
         <td></td>
         <td></td>
         <td><strong>0</strong></td>
         <td><strong>0</strong></td>
         <td><strong>0</strong></td>
        </tr>
        </tbody>
         </table>
        </div>
      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" class="btn btn-warning" id="recoverySubmitBtnID" onclick="recoverySubmitButton()">Submit</button>
        </div>
      </div>
    </div>
  </div>
</div>
 <!-- modal popup for recovery click action end -->
  <!-- recovery amount details Start -->              
  <div id="recoveryAmountDetails"> </div> 
   <!-- recovery amount details end -->
  <!-- NMR Detail table popup start -->
  <div id="NMRBillTableModal" class="modal fade" role="dialog">
	 <div class="modal-dialog" style="width:95%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title text-center" style="font-weight:1000;">NMR Details</h4>
				</div>
				<div class="modal-body">
				       <div class="overlay_ims"  style="display: none;"></div> 
					   <div class="loader-ims" id="loaderId" style="display: none;">
							<div class="lds-ims">
								<div></div><div></div><div></div><div></div><div></div><div></div></div>
							<div id="loadingimsMessage">Loading...</div>
						</div>
				       <div class="table-responsive">
	                      <table style="border:1px solid #000;width:2050px;margin-bottom:15px;" id="NMRBillstableId"  class="bordsertable">
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
						    <button type="button" class="btn btn-warning" id="NMRCreateBtn" onclick="NMRCreateTableSubmit()">Submit</button>
						</div>
				</div>
			</div>
		</div>
	</div>
	<!-- NMR Detail table popup end -->
	<input type="hidden" id="statusOfPage" value="NMRcreatePage">
 </form>
	    <script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/WorkOrder/NMRTable.js"></script>
		<script src="js/WorkOrder/NMRBills.js"></script>
		<script type="text/javascript">
		
		$(document).ready(function() {
			/* $('body').bind('paste', function (e) {
				e.preventDefault();
			}); */
			//Bill type change
			$("#isOldOrNewBill").on("change",function(){
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();				
				if(ContractorId.length==0){					
					alert("Please enter contractor name.");
					//$('#NMRBillTableModal').modal('show');
					$("#ContractorName").focus();
					return false;
				}				
				if(workOrderNo.length==0){
					alert("Please select work order no.");				
					//$('#NMRBillTableModal').modal('show'); 
					$("#workOrderNo").focus();
					return false;
				}				
				var isOldOrNewBill=$(this).val();
				var isPrevBillIsRunning=$("#isPrevBillIsRunning").val();
				if(isPrevBillIsRunning=="true"){
				     alert("Please check the previous running account bills.");
					return false;
				}
				if(isOldOrNewBill=="Old"){
					$("#showBillText").show();
				}else{
					$("#showBillText").hide();
					$("#oldBillNo").val('');
					$('#formSubmitBtn').attr('disabled',false);
				}
			});
			 
			//old bill blur event
			$("#oldBillNo").on("blur",function(){
				var oldBillNo=$(this).val();				
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();				
				if(ContractorId.length==0){
					alert("Please enter contractor name.");
					$("#ContractorName").focus();
					return false;
				}				
				if(workOrderNo.length==0){
					alert("Please select work order no.");				
				
					$("#workOrderNo").focus();
					return false;
				}
				if(oldBillNo.length<=2){
					return false;
				}
				if(oldBillNo.length<=3){
					alert("Please enter correct format of Bill Number.");
					$("#oldBillNo").val('');
					$('#formSubmitBtn').attr('disabled',true);
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}
				var array=new Array();
				array=oldBillNo.split("/");
				var billType=$("#billType").val();
				
				if(array[0]!=billType){
					alert("Please enter correct format of Bill Number.");
					$('#formSubmitBtn').attr('disabled',true);
					$("#oldBillNo").val('');
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}
				if(!isNum(array[1])){
					alert("Please enter correct format of Bill Number.");
					$('#formSubmitBtn').attr('disabled',true);
					$("#oldBillNo").val('');
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}
				if(array[1]=="0"){
					alert("Please enter correct format of Bill Number.");
					$('#formSubmitBtn').attr('disabled',true);
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}				
				var ContractorId=$("#ContractorId").val();
				var workOrderNo=$("#workOrderNo").val();
				var isInvoiceOrBillNumberValidation="billNumber";
				//validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation);
				 var url = "checkBillNoExistsOrNot.spring";
				 $.ajax({
					  url : url,
					  type : "get",
					 data:{
						 workOrderNo:workOrderNo,
						 ContractorId:ContractorId	,
						 oldBillNo:oldBillNo
					 },
					  contentType : "application/json",
					  success : function(data) {					
							if(data!="true"){
								$('#formSubmitBtn').attr('disabled',false);	
							}else{
								alert("Bill number already exists, please enter another bill number.");								
								$('#formSubmitBtn').attr('disabled',true);
							}
					  }
				});
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
			
			
		});
		
		
		function validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation){
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
							$('#formSubmitBtn').attr('disabled',false);
							 	
						}else{
							alert("Bill number already exists, please enter another bill number.");								
							$('#formSubmitBtn').attr('disabled',true);
							
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
		
		function validateInvoiceBillNumber(billInvoiceNo,actualBillInvoiceNo){
			debugger;
			var error=true;
			var actualarray=actualBillInvoiceNo.split("/")
			
			var array=new Array();
			array=billInvoiceNo.split("/");
			
			if(billInvoiceNo.length==0){
				alert("Please enter invoice number.");
				$("#billInvoiceNo").focus();
				return false;
			}else{
				if(actualarray.length!=array.length){
					 alert("Please enter correct format number of bill invoice.");
					 $('#formSubmitBtn').attr('disabled',true);
					return false;
				}
				if(!isNum(array[array.length-1])){
					 alert("Please enter correct format number of bill invoice.");
					 $('#formSubmitBtn').attr('disabled',true);
					return false;
				}
				
				for (var index = 0; index < actualarray.length-1; index++) {
					 if(actualarray[index]!=array[index]){
						 alert("Please enter correct format number of bill invoice.");
						 	$('#formSubmitBtn').attr('disabled',true);
							$("#billInvoiceNo").val(actualBillInvoiceNo);
						return error=false;
					 }
				}
			}
			return error;
		}
		
		//creating NMR bill
		function createNMRBill(){
			var error=true;
			var isOldOrNewBill =$("#isOldOrNewBill").val();
			if(isOldOrNewBill=="Old"){
				var oldBillNo=$("#oldBillNo").val();
				if(oldBillNo==""){
					$("#oldBillNo").focus();
					alert("Please enter correct format of Bill Number.");
					return false;
				}
				
				var array=new Array();
				array=oldBillNo.split("/");
				var billType=$("#billType").val();
				
				if(array[0]!=billType){
					alert("Please enter correct format of Bill Number.");
					$('#formSubmitBtn').attr('disabled',true);
					$("#oldBillNo").val('');
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}
				
				if(!isNum(array[1])){
					alert("Please enter correct format of Bill Number.");
					$('#formSubmitBtn').attr('disabled',true);
					$("#oldBillNo").val('');
					return false;
				}else{
					$('#formSubmitBtn').attr('disabled',false);
				}
				
			}			
			var billDate=$("#billDate").val();
			var ContractorId=$("#ContractorId").val();
			var workOrderNo=$("#workOrderNo").val();
			var billInvoiceNo=$("#billInvoiceNo").val()==undefined?"":$("#billInvoiceNo").val().trim();
			var actualBillInvoiceNo=$("#actualBillInvoiceNo").val();
			
			error=validateInvoiceBillNumber(billInvoiceNo,actualBillInvoiceNo);
			if(error==false){
				return false;
			}else{
				$('#rasubmitBtn').attr('disabled',false);
				$('#submitBtn').attr('disabled',false);
			}
			
			var isInvoiceOrBillNumberValidation="invoiceNumber";
			var status=validateBillInvoiceNumber(workOrderNo,ContractorId,billInvoiceNo,isInvoiceOrBillNumberValidation);
			if(status==false){
				return false;
			}
			
			
			if(billDate==""||billDate==null){
				alert("Please Select Date..");
				$("#billDate").focus();
				return false;
			}			
			if(ContractorId.length==0){
				alert("Please enter contractor name.");
				$("#ContractorName").focus();
				return false;
			}			
			if(workOrderNo.length==0){
				alert("Please select work order number.");	
				$("#workOrderNo").focus();
				return false;
			}
			var TotalACCAmt = parseFloat($("#TotalACCAmt").text());
			if (TotalACCAmt == '0') {
				alert("Please enter abstract.");
				$("#NMRBillTableModal").modal();
				return false;
			}
			if($("#CCToatalAmount").text().replace(/,/g,'')<0){
				alert("Amount going negative please check.");
				return false;
			}
			debugger;
			try{
				var totalAmtToPay=$("#totalAmtToPay").val()==""?0:parseFloat($("#totalAmtToPay").val().replace(/,/g,''));
				var CBToatalAmount=$("#CBToatalAmount").text()==""?0:parseFloat($("#CBToatalAmount").text().replace(/,/g,''));
				if(CBToatalAmount>totalAmtToPay){
					alert("Initiated amount is greater than work order amount.");
					return false;
				}	
			}catch(e){
				console.log(e);
			}
			
			
			//Calculating no Of rows in Create NMR Bill
			var RowsCount=[];			
			var tableRowsLength=$('.tablerowcls').length;		
				$('.tablerowcls').each(function(){
					var getRowId=$(this).attr('id');	
					var res = getRowId.split("tablerow")[1];
					RowsCount.push(res);
				})				
				$("#noOfRowsToIterate").val(RowsCount);
				var str="";
				var rowsToIterate=$("#rowsToIterate").val();
				$("#recoveryAmountDetails").append("");
				var row1=0;
				for (var i = 1; i <= rowsToIterate; i++) {
					var htmlData="";
					var child_product_id=$("#childId"+i).val();
					var recovery_quantity=$("#"+child_product_id+"totalRecoveryAmount").text();
					var issuedQty=$("#"+child_product_id+"totalRecoveryAmount").text();
					var recovery_amount=$("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val();
					var mesurment_id=$("#"+child_product_id+"mesurment_id").val();
					if(recovery_amount!="0"){
						row1++;
						htmlData+="<input type='hidden' name='child_product_id"+row1+"' id='child_product_id"+i+"' value='"+child_product_id+"'>";
						htmlData+="<input type='hidden' name='measurement_id"+row1+"' id='measurement_id"+i+"' value='"+mesurment_id+"'>";
						htmlData+="<input type='hidden' name='recovery_amount"+row1+"' id='recovery_amount"+i+"' value='"+(recovery_amount)+"'>";
						htmlData+="<input type='hidden' name='recovery_quantity"+row1+"' id='recovery_quantity"+i+"' value='"+(issuedQty)+"'>";
					  $("#recoveryAmountDetails").append(htmlData);
					}
				}
				
			  $("#recoveryAmountDetails").append("<input type='hidden' name='rowsToIterate1' id='rowsToIterate1' value='"+row1+"'>");
			  var canISubmit = window.confirm("Do you want to Submit?");	     
			  if(canISubmit == false) { 
			        return false;
			  }
			  $(".overlay_ims").show();	
			  $(".loader-ims").show();  
			  $("#rowsToIterate").val(row1);
		}
		//valdating billdate, contractor name and  work order number
		function validateForm(){
			var billDate=$("#billDate").val();
			var ContractorId=$("#ContractorId").val();
			var workOrderNo=$("#workOrderNo").val();
			if(ContractorId.length==0){
				alert("Please enter contractor name.");
				$("#ContractorName").focus();
				return false;
			}			
			if(workOrderNo.length==0){
				alert("Please select work order no.");						
				$("#workOrderNo").focus();
				return false;
			}
			if(billDate==""||billDate==null){
				alert("Please Select Date..");
				$("#billDate").focus();
				return false;
				
			}
			var TotalACCAmt = parseFloat($("#TotalACCAmt").text());
			if (TotalACCAmt == '0') {
				alert("Please enter abstract.");
				$("#NMRBillTableModal").modal();
				return false;
			}
		}
    </script>
	<script>
		//checking contractor name and work order number and date 
		function openAbstract(){ 
			var NameoftheContractor=$("#ContractorName").val(); 
			var WorkOrderRefNo=$("#workOrderNo").val(); 
			var date=$("#billDate").val(); 
			if(NameoftheContractor==""){
				alert("Please enter contractor name");
				$("#ContractorName").focus();
				return false;
			}
			if(WorkOrderRefNo==""){
				alert("Please select work order reference number");
				$("#workOrderNo").focus();
				return false;
			}
			if(date==""){
				alert("Please select date");
				$("#billDate").focus();
				return false;
			}
			$("#NmrAbstract").attr("data-toggle", "modal");
			$("#NmrAbstract").attr("data-target", "#NMRBillTableModal");
		}
				
	</script>
	<script>						
		$(function(){
			  $("#billDate").datepicker({ dateFormat: 'dd-mm-yy'});
		});
		
		//checking number number 
		function isNumberCheck(el, evt) {
			    var charCode = (evt.which) ? evt.which : event.keyCode;
			    var num=el.value;
			    var number = el.value.split('.');
			    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) || charCode == 13) { return false;   }
			    //just one dot
			    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {return false;  }			   
			    //get the carat position
			    var caratPos = getSelectionStart(el);
			    var dotPos = el.value.indexOf(".");
			    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
			        return false;
			    }
			    return true;
		}
		function getSelectionStart(o) {
			if (o.createTextRange) {
					var r = document.selection.createRange().duplicate();
					r.moveEnd('character', o.value.length);
					if (r.text == '') return o.value.length;
					return o.value.lastIndexOf(r.text);
			}else return o.selectionStart;
		}	
		//checking number number 
		function HoursNumberCheck(el, evt) {
			    var charCode = (evt.which) ? evt.which : event.keyCode;
			    var num=el.value;
			    var number = el.value.split('.');
			    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {return false;}
			    //just one dot
			    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)  ) { //|| (el.value.split('.')[1] >= 6 && (charCode > 48 || charCode < 57)) ||  ((el.value.split('.')[1] =="" &&  (charCode < 48 && charCode > 55))) &&  (charCode > 48 || charCode < 55))
			         return false;
			    }
			   
			    //get the carat position
			    var caratPos = getSelectionStart(el);
			    var dotPos = el.value.indexOf(".");
			    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
			        return false;
			    }
			    return true;
		}
		//calucating hours with validating
	    function calHours(id){
				var start = parseFloat($("#From"+id).val());
				var end =  parseFloat($("#To"+id).val());
				if($("#From"+id).val()==""){ start=0.00;}
				if($("#To"+id).val()=="" || $("#To"+id).val()=="0"){
					end=0.00;
					return false;
				}
				if(end>24){
					$("#To"+id).val('0');
					alert("Please enter valid time");
					return false;					
				}
				if(start>24){
					$("#From"+id).val('0');
					alert("Please enter valid time");
					return false;
				}
				var start1 = parseFloat($("#FromHidden"+id).val());
				var end1 =  parseFloat($("#ToHidden"+id).val());
				if(start>end && $("#To"+id).val()!=''){
					//end=24+end;
					$("#From"+id).val('0');
					$("#To"+id).val('0');
					$("#Noofhours"+id).val('0');					
					alert("You are giving next day time.");
					return false;
				}				
				start=start.toFixed(2);
				end=end.toFixed(2);
				start1=start1.toFixed(2);
				end1=end1.toFixed(2);
				s = start.split('.');
				e = end.split('.');
				if(start.split('.')[1]>59){
					alert("Please enter valid time");
					$("#From"+id).val('0');
					return false;
				}
				if(end.split('.')[1]>59){
					alert("Please enter valid time");
					$("#To"+id).val('0');
					return false;
				}
				min=00;
				min = e[1]-s[1];				
				hour_carry = 0;
				if(min < 0){
					min += 60;
					hour_carry += 1;
				}
				hour = e[0]-s[0]-hour_carry; 
				diff = hour + "." + min;
				if(start!=start1 || end!=end1){
					getWorkDesc(id);
					calculateBtlClick();
				}
				$("#FromHidden"+id).val($("#From"+id).val());
				$("#ToHidden"+id).val($("#To"+id).val());				
		   }
		 //resetting row values when you change from to duration
		 function fromToDurationKeyUp(id){
			    $("#Noofhours"+id).val('0');
			    //this method will reset the row
			    getWorkDesc(id);
		 } 
	</script>
</body>
</html>
