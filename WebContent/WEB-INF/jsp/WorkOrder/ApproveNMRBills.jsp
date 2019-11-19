<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.WorkOrderBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
<%log("Approve NMR Bill Page");%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
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
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
	    <script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="./../CacheClear.jsp" /> 
	
		<style>
	
			/* style for hover button on pdf pages */
	 	    .middle{width: 100% !important;}
			.btn-small{padding: 5px;border-radius: 5px;}
			.btn-small1{padding: 3px;border-radius: 5px;}
			.btn-fullwidth:hover{background-color:transparent;border-color:transparent;color:transparent;height:200px;width:100%;margin-top:-45px;}
            .btn-fullwidth{background-color:transparent;border-color:transparent;color:transparent;height:200px;width:100%;margin-top:-45px;}
            .btn-fullwidth:active:focus, .btn-fullwidth:active:hover{color: transparent;background-color: transparent;border-color: transparent;}
            .btn-fullwidth:active{ color: transparent;background-color: transparent;border-color: transparent;}
            .btn-fullwidth.focus, .btn-fullwidth:focus {color: transparent; background-color: transparent;border-color: transparent;}
            /* style for hover button on pdf pages */
		 	
		    /* .NMRBillstableId thead tr td{border:1px solid #000;} */	
	        table.dataTable {border-collapse: collapse !important;}
		   .tbl-td{border-top:1px solid #fff !important;padding:3px;}
		   .tbl-td-bottom{border-bottom:1px solid #fff !important;padding:3px;}
	       .conatianer1{float: left;}
	       body{font-weight:600;}
    	   table, th, td , tbody {border: 1px solid #000 !important;border-collapse: collapse;}
           thead{background-color:#ccc;}
           .page-body{padding: 20px;}
           .tbl-border-top{border-bottom: none !important;margin-top:-1px;}
		   .tbl-border-top{margin-bottom:0px;}
		   .tbl-border-top tr .td-top{border-top:none !important;}
           .vertical-alignment {vertical-align: middle !important;}
           .bck-ground{background-color:#ccc;}
           .td-active{background-color:#dbecf6;}
           .td-active input{background-color:#dbecf6;}
           .td-background{background-color:#dbecf6;}
           .td-active input{font-weight:bold;}
			.border-none input{border:none !important;}
			.input-outline input{outline:none;}
			.tbl-corner-right{height:73px;margin-right:-16px;width: 300px;text-align: center;}
			.widthhundreadper{width:100% !important;}
			@media print {
				.btn-warning, #showHideRemarks, #donotprint , .hideRecovery, #NMRHideprint, .hideinPrint, #ModificationDetails, #paymentLedgerTable , #appendBillDetailsTotal, #PDFView, #IMAGEView{display:none !important;}
				.showtextprint, #printfinalTotalWorkOrderAmountInWords {display:block !important;}
				.breadcrumb, #finalTotalWorkOrderAmountInWords, #amountInWords, .hideInPrint{display:none;}
				.left_col{display:none;}
				.nav_menu{display:none;}
				thead { display: table-row-group;}
				.img-workorder-logo{margin-top:5px;}
				.head-logo-workorder{width:350px;float:left;}
				.tbl-corner-right{height:90px;margin-right:-16px;width: 250px;text-align: center;}
				.border-print{border-top:1px solid #000;}
				.print-tblheight{height:100px;}
				#forPrintPage{display: block !important;}
			}
			
			/* for loader */
			/* Absolute Center Spinner */
			.loading {position: fixed; z-index: 999;height: 2em;width: 2em;overflow: show;margin: auto;top: 0;left: 0;bottom: 0;right: 0;}
			
			/* Transparent Overlay */
			.loading:before {content: '';display: block;position: fixed;top: 0;left: 0;width: 100%;height: 100%;background: radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0, .8));background: -webkit-radial-gradient(rgba(20, 20, 20,.8), rgba(0, 0, 0,.8));}
			
			/* :not(:required) hides these rules from IE9 and below */
			.loading:not(:required) {font: 0/0 a;color: transparent;text-shadow: none;background-color: transparent;border: 0;}
			
			.loading:not(:required):after {content: '';display: block;font-size: 10px;width: 1em;height: 1em;margin-top: -0.5em;
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
            /*fixed header */
				 #NMRBillstableId thead, #NMRBillstableId tbody tr{table-layout:fixed;display:table;width:100%;}
				 #NMRBillstableId thead tr th:first-child, #NMRBillstableId tbody tr td:first-child{width:52px;text-align: center;}				 			 
				 #NMRBillstableId tbody tr td{border-top:0px !important;border:1px solid #000;}
				 #NMRBillstableId tbody {border:0px solid #000 !important;}
				 NMRBillstableId thead tr th{border:1px solid #000;}
				 #NMRBillstableId{border:0px !important;}
          /*fixed header*/
	</style>
    <script type="text/javascript">
		
		if (typeof (Storage) !== "undefined") {
			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			var isBillUpdatePage="${isBillUpdatePage}";
			var status="${status}";
			//if isBillUpdatePage if contains value true then it's update Temp Contractor Bills
			//if status if contains value true then it's  Temp Contractor Bills status
			//if status if contains value false then it's  Approve Contractor Bills 
				var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
				if (i == 2) {
					sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
					if(isBillUpdatePage=="true"){
						window.location.assign("updateTempContractorBills.spring");
					}else if(status=="true"){
						window.location.assign("viewContractorBillStatus.spring");
					}else{
						window.location.assign("approveContractorBills.spring");
					}
				}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
	</script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
</head>
<body class="nav-md">
	<!-- loader -->
   <div class="overlay_ims"></div> 
   <div class="loader-ims" id="loaderId">
	 <div class="lds-ims">
		<div></div><div></div><div></div><div></div><div></div><div></div>
	</div>
	<div id="loadingimsMessage">Loading...</div>
   </div>
   <noscript>
	  <h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
   </noscript>

  <form:form modelAttribute="billBean" action="approveNMRBill.spring" method="POST" id="approveNMRBillFormId"  enctype="multipart/form-data" >
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
					<input type="hidden" name="siteId" id="siteId" value="${billBean.siteId }">
					<input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }">
					<input type="hidden" name="NMRBillNo" id="billNo" value="${billBean.permanentBillNo}">
					<input type="hidden" name="PageName" id="PageName" value="ApproveNMRBill">
					<input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
					<input type="hidden" id="workOrderNo" name="WorkOrderNo" value="${billBean.workOrderNo}">
					<input type="hidden" name="workOrderNo" value="${billBean.workOrderNo}">
					<input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId}"/>
					<input type="hidden" name="billType" id="billType" value="NMR">
					<input type="hidden" name="statusOfPage" id="statusOfPage" value="${status}">
					<input type="hidden" name="printName" id="printName">
					<input type="hidden" name="isCommonApproval" id="isCommonApproval" value="${isCommonApproval}">
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
                       	<tr style="text-align:center;"> <td style="padding:18px;font-size: 15px;">Abstract</td> </tr>
                       </tbody>
                      </table>
                      <table style="border:1px solid #000 !important;margin-top:-1px;width:100%;" class="widthhundreadpers">
                       <tbody>
                        <tr>
                           <td style="width:50%;padding:5px;">
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Name of the Project</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.siteName}</strong></h5></div>
                               </div>     
                                <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Block</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;" id="donotprint">
                                  <c:set var="blockNames" value="${fn:split(billBean.NMRBillBlocks, '@#')}" />
                                
                                  <c:if test="${status ne true }">
	                                 <select id="BlockID" name="Blocks"  style="width: 80%;" multiple="multiple" >
			                             <c:forEach items="${blocksMap}" var="block">
			                           	  <option value="${block.key}@@${block.value}">${block.value}</option>
			                             </c:forEach>
			                            <c:forEach var="block" items="${blockNames}">
	                        				<c:if test="${not empty block.trim()}">
												<option value="00@@${block}" selected>${block}</option>
											</c:if>
	                             	    </c:forEach>   
		                             </select>
                                 </c:if>
                                 </div>
                                 <div  id="forPrintPage" style="width:50%;float:left;display: none;">
                                  <h5><strong>
	                                    <c:forEach var="block" items="${blockNames}">
	                        			<p style="word-break: break-all;">${block}</p>
	                             	    </c:forEach>
	                             		</strong>
	                             </h5>
	                             <h5><c:if test="${empty billBean.NMRBillBlocks}">
	                             	     <p>
	                             	     -
	                             	     </p>
	                             	    </c:if>
	                             </h5>
                                 </div>
                               </div>    
                           </td>
                           <td colspan="5" style="width:50%;padding-top:5px;padding-right:5px;padding-left:15px;">
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Bill Date</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><input  type="text" name="billDate" id="billDate" value="${billBean.paymentReqDate }" class="rabill-class-select" style="display:inline;border:none;padding-top:3%;"  readonly="readonly"/></div><div class="clearfix"></div>
                               </div> 
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Contractor Name</strong></h5></div>
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
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.permanentBillNo}</strong></h5></div>
                               </div> 
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Invoice No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong>${billBean.billInvoiceNumber}</strong></h5></div>
                               </div> 
                               <div style="width:100%;">
                                 <div style="width:40%;float:left;"><h5><strong>Work order No</strong></h5></div>
                                 <div style="width:10%;float:left;"><h5><strong>:</strong></h5></div>
                                 <div style="width:50%;float:left;"><h5><strong id="printWorkOrderNo">${billBean.workOrderNo}</strong></h5></div>
                               </div> 
                               	<div class="" style="width:100%;">
				              	<div class=" mrg-Top"  style="width:40%;float:left;">WO Amount </div>
				              	<div class=" mrg-Top"  style="width:10%;float:left;">:</div>
				              	<div class=" mrg-Top"  style="width:50%;float:left;"> <strong  id="printtotalAmtToPay">${billBean.totalAmount}</strong> <input type="hidden" class="heightthirty border-none" name="totalAmtToPay" id="totalAmtToPay" value="${billBean.totalAmount}" style="display:inline;border:none;"  readonly="readonly" /></div>
				                </div>
                             </td>
                       </tr>                   
					  </tbody>
					  </table>
					  	
					<!-- <input type="hidden"  id="hiddenpreviousPettyExpences" >
					<input type="hidden"  id="hiddenpreviousotherAmount" >	  
					 -->
					         
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
					
				
					  <table style="border:1px solid #000;width:100%;margin-top:-1px;">
					   <thead>
					     <tr style="font-size:14px;">
                            <th colspan="5"></td>
                           	<th style="text-align:center;" colspan="2">Bill Date</th>
                           	<!-- <td></td> -->
                           	<th style="text-align:center;">From</th>
                           	<th style="text-align:center;"><span id="fromDate1"></span>  </th>
                           	<th style="text-align:center;">To</th>
                            <th style="text-align:center;"><span id="toDate1"></span> </th>
                        </tr>
					    <tr style="font-size:14px;">
                           <th rowspan="2" style="padding:3px;text-align:center;">S .NO</th>
                           <th rowspan="2" style="padding:3px;text-align:center;">Description</th>
                           <th rowspan="2" style="padding:3px;text-align:center;">Total Qty</th>
                           <th rowspan="2" style="padding:3px;text-align:center;">Avg Rate</th>
                           <th rowspan="2" style="padding:3px;text-align:center;">Unit</th>
                           <th colspan="2" style="padding:3px;text-align:center;">Cumulative Certified</th>
                           <th colspan="2" style="padding:3px;text-align:center;">Previous Certified</th>
                           <th colspan="2" style="padding:3px;text-align:center;">Current Certified</th>
                        </tr>
                        <tr style="font-size:14px;">
                            <th style="padding:3px;text-align:center;">Qty</th>
                            <th style="padding:3px;text-align:center;">Amount</th>                         
                            <th style="padding:3px;text-align:center;">Qty</th>
                            <th style="padding:3px;text-align:center;">Amount</th>                         
                            <th style="padding:3px;text-align:center;">Qty</th>
                            <th style="padding:3px;text-align:center;">Amount</th>
                         </tr>
					   </thead>
					  <tbody id="NMRAbstractTableData">
					  
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
						<c:if test="${status eq true }">
							<td class="text-center"><input type="text"  id="pettyExpensesCurrentCerti" name="pettyExpensesCurrentCerti" class="form-control raDeductionAmt currentCAmnt" style="width:100%;text-align:center;background-color: #fff;"  onkeypress="return isNumberCheck(this, event)"  onkeyup="pettyAndOtherChange(this.id)" value="${pettyExpensesCurrentCerti}" readonly="readonly"> <input type="hidden" name="actualPettyExpensesCurrentCerti" id="actualPettyExpensesCurrentCerti"  value="${pettyExpensesCurrentCerti}" > <br></td>
						</c:if>
						<c:if test="${status ne true }">
								<td class="text-center"><input type="text"  id="pettyExpensesCurrentCerti" name="pettyExpensesCurrentCerti" class="form-control raDeductionAmt currentCAmnt" style="width:100%;text-align:center;"  onkeypress="return isNumberCheck(this, event)"  onkeyup="pettyAndOtherChange(this.id)" value="${pettyExpensesCurrentCerti}" > <input type="hidden" name="actualPettyExpensesCurrentCerti" id="actualPettyExpensesCurrentCerti"  value="${pettyExpensesCurrentCerti}" > <br></td>
						</c:if>
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
						<c:if test="${status eq true }">
							<td class="text-center"><input type="text"  id="other" name="other" class="form-control raDeductionAmt currentCAmnt" style="width:100%;text-align:center;background-color: #fff;"  onkeypress="return isNumberCheck(this, event)"  onkeyup="pettyAndOtherChange(this.id)" autocomplete="off"  value="${other}" readonly="readonly"><input type="hidden" id="actualOtherAmt" name="actualOtherAmt" value="${other}">  <br></td>
						</c:if>
						<c:if test="${status ne true }">
							<td class="text-center"><input type="text"  id="other" name="other" class="form-control raDeductionAmt currentCAmnt" style="width:100%;text-align:center;"  onkeypress="return isNumberCheck(this, event)"  onkeyup="pettyAndOtherChange(this.id)" autocomplete="off"  value="${other}" ><input type="hidden" id="actualOtherAmt" name="actualOtherAmt" value="${other}">  <br></td>
						</c:if>
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
							<tr class="hideInPrint" style="background-color: #ccc;">
								<td class="text-center"></td>
								<td class="text-center">Total Amount(B)</td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"></td>
								<td style="text-align:center;padding:1px;"><input type="text" name="totalAmtCumulative" id="totalAmtCumulative" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="" readonly=""></td>
								<td style="text-align:center;padding:1px;"></td>
								<td class="text-center"><input type="text" name="totalAmtPrevCerti" id="totalAmtPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="" readonly=""></td>
								<td class="text-center"></td>
								<td class="text-center"><input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="" readonly=""></td>			
							</tr>
							<tr class="hideInPrint">
								<td class="text-center"></td> 	
								<td class="text-center"></td>
								<td style="text-align:center;padding:8px;"></td>
								<td style="text-align:center;padding:8px;"></td>
								<td style="text-align:center;padding:8px;"></td>
								<td style="text-align:center;padding:8px;"></td>
								<td style="text-align:center;padding:8px;"></td>
								<td style="text-align:center;padding:8px;"></td>
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
								<td style="text-align:center;padding:1px;"><strong id="CCToatalAmount">0</strong><input type="hidden" name="paybleAmount" id="paybleAmount"> </td>				       
							</tr>
								
							<tr>
								<td class="text-center" colspan="5" style="padding:1px;">Amount in Words</td>
								<td class="text-center" colspan="6" style="padding:1px;"><span id="amountInWords"></span><span style="display:none;" id="printfinalTotalWorkOrderAmountInWords"></span></td>								
							</tr>
								
							<tr class="hideinPrint">
								<td class="text-center" colspan="5" style="padding:1px;"><strong></strong></td>
								<td class="text-center" colspan="6" style="padding:1px;"><strong><a href=""  data-toggle="modal" data-target="#NMRBillTableModal" id="NMRHideprint">NMR Details</a></strong></td>
							</tr> 
						  </tbody>
					 </table> 
					  <%-- <c:if test="${status eq true }"> --%>
					   <c:if test="${isBillUpdatePage ne true }"> 
					  
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
						  
							  	<c:when test="${countVerifiedEmpNames eq listOfVerifiedEmpNames.size() && WorkOrderBean.approverEmpId eq workOrderStatus}">
								
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
				</c:if>		 
					 
					 					 
					 <div style="width:100%;" id="showHideRemarks">
					     <div class="col-md-6 Mrgtop10" style="width:50%;">
							<label class="control-label col-md-2" style="width:20%;float:left;">Remarks : </label>
							<div class="col-md-4 resize-vertical" style="width:60%;float:left;position: relative;margin-bottom: 10px;">
								<input type="hidden" name="actualremarks" value="${woLevelPurpose}">
							 	<textarea name="remarks" class="form-control resize-vertical" id="remarks" href="#" data-toggle="tooltip" title=" <c:out value='${woLevelPurpose}'></c:out>" placeholder=" <c:out value='${woLevelPurpose}'></c:out>"  id="NoteId"  autocomplete="off"></textarea> 
							</div>
					    </div>
					 </div>
		   <c:set value="ADV" var="ADV"></c:set>
           <c:set value="SEC" var="SEC"></c:set>
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
            
					<c:if test="${changedListDtls.size()!=0}">
						<div class="col-md-12 Mrgtop10" id="ModificationDetails">
								<label class="control-label col-md-2" >Modification Details : </label>							
								<div class="col-md-10" style="position: relative;margin-bottom: 10px;" >
									<c:forEach items="${changedListDtls}" var="chngedDtls">
										${chngedDtls.REMARKS}	
									</c:forEach>
								</div>
						</div>
					</c:if>
                    <div class="col-md-12 text-center center-block" style="margin-top:30px;"><!-- <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#NMRBillTableModal">NMR Abstract</button> -->
					  <c:if test="${status ne true }">					  		
					         <button class="btn btn-warning" type="submit" id="approveNMRID" onclick="return approveNMRBill();" >Approve</button>
						     <button class="btn btn-warning" type="submit" id="rejectNMRID" onclick="return rejectNMRBill();" >Reject</button>						
					   	     <input type="button" name="submitBtn"   class="btn btn-warning btn-custom-width" onclick="printPage()" 	value="Print Abstract"  />
						     <input type="button" name="sumadhuraCopy" id="sumadhuraCopy" onclick="return getSumadhuraCopy()" class="btn btn-warning btn-custom-width"value="Sumadhura Copy" />
						     <input type="button" name="contractorCopy" id="contractorCopy" onclick="return getContractorCopy()" class="btn btn-warning btn-custom-width"value="Contractor Copy" />
					 	     <input type="button" name="nmrDetails" id="nmrDetails" onclick="return printNMRDetails();" class="btn btn-warning btn-custom-width"value="NMR Details" />
							 <input type="button" name="billLedger" id="billLedger"   class="btn btn-warning btn-custom-width"  value="Ledger" />
							 <input type="button" name="closeBtn" id="closeBtn" onclick="return closeView()" class="btn btn-warning btn-custom-width" value="Close" />  
					  		<input type="hidden" value="approveContractorBills.spring" id="pageType">
					  </c:if>
					  <!--  if it is status page but not bill update page -->
					  <c:if test="${status eq true }">
					  		<c:if test="${isBillUpdatePage ne true }">
					   			<input type="button" name="closeBtn" id="closeBtn" onclick="return closeView()" class="btn btn-warning btn-custom-width" value="Close" />
					   			<input type="button" name="submitBtn"   class="btn btn-warning btn-custom-width" onclick="printPage()" 	value="Print Abstract"  />
					   			<input type="button" name="sumadhuraCopy" id="sumadhuraCopy" onclick="return getSumadhuraCopy()" class="btn btn-warning btn-custom-width"value="Sumadhura Copy" />					 	
					 			<input type="button" name="contractorCopy" id="contractorCopy" onclick="return getContractorCopy()" class="btn btn-warning btn-custom-width"value="Contractor Copy" />
					 			<input type="button" name="nmrDetails" id="nmrDetails" onclick="return printNMRDetails();" class="btn btn-warning btn-custom-width"value="NMR Details" />
					   		 	<input type="button" name="billLedger" id="billLedger"   class="btn btn-warning btn-custom-width"  value="Ledger" />
					   		 	<input type="hidden" value="viewContractorBillStatus.spring" id="pageType">
					   		</c:if>
					  </c:if>
			     </div>
					  
				<c:if test="${isBillUpdatePage ne true }">
					<!-- this is common file for showing images  -->
						<%@include file="ImgPdfCommonJsp.jsp" %>
				</c:if>
				<div class="clearFix"></div>
				
				<!--if isBillUpdatePage equal true then this page for Update Temporary bills  -->
				<c:if test="${isBillUpdatePage eq true }">
				<input type="hidden" value="updateTempContractorBills.spring" id="pageType">
	
	<!-- ***********************************************this is for pdf file download start******************************************************** -->

				<div class="col-md-12" style="margin-top: 10px;" id="PDFView">				
				<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
				if(pdfcount==0){
					%>					
					<%
				}else{
					%>
				<h3>You can see the PDF</h3>	
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
							 <div class="col-md-3 pdfcount pdf-delete<%=i%>"   id="pdfDivHideShow<%=pdfName %>">
							   <div class="pdf-cls" style="margin-bottom:15px;"> 
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
				<div class="col-md-12 Mrgtop20"  id="IMAGEView">
				<%
							int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
							//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
							if(imagecount==0){
				%> 					
							 					<%
							 				}else{
							 				%>
							 					<h3>You can see the Images</h3>
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
									 
									 <div class="container-1"   id="imageDivHideShow<%=imageB64 %>">
											<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
											<div class="middle-1">
											 <div class="columns download">
										        <p><a class="button btn-dwn btn-success btn-small" onclick="toDataURL('${requestScope[index]}',this)"download><i class="fa fa-download"></i>&nbsp;Download</a><button onclick="deleteWOFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="button btn-dwn btn-danger btn-small1"><i class="fa fa-remove"></i> &nbsp; Delete</button></p>
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
							
							<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
							<input type="hidden" name="pdfAlreadyPresent"  id="pdfAlreadyPresent"  value="<%=pdfcount%>">
								<div class="col-md-12">
										<div class="file-upload color-head-file" id="ishidden">
												<c:if test="${(imagecount+pdfcount)<8}"><h3>You can upload Images/PDF(s) here :</h3>
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
	     <button type="button" class="btn btn-warning " data-dismiss="modal" >Close</button>
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName %>','${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<%} %>
<%} %>

	
<!-- modal popup for image pop start-->
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
		


		 <c:if test="${isBillUpdatePage eq true }">
		        	<div class="col-md-12 text-center center-block">
		        	  <input type="button" name="updateBtn" id="updateBtn" class="btn btn-warning btn-custom-width" value="Update Bill" />
		        	  <input type="button" name="closeBtn" id="closeBtn" onclick="return closeView()" class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
		        	</div>
		</c:if>


		</c:if>
				
					  </div>
					  <!-- Ledger -->
					  <div class="col-md-12">
					    <table style="border:1px solid #000;width:100%;margin-bottom:30px;margin-top:30px;" id="paymentLedgerTable">
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
          <c:if test="${status ne true }">
        	 <button type="button" class="btn btn-warning" id="recoverySubmitBtnID" onclick="recoverySubmitButton()">Submit</button>
       	  </c:if>
       	   <c:if test="${status eq true }">
          	<button type="button" class="btn btn-warning" id="recoverySubmitBtnID" onclick="recoverySubmitButton()">Close</button>
          </c:if>
        </div>
      </div>
    </div>
  </div>
</div>

 <!-- modal popup for recovery click action end -->	              
  <div id="recoveryAmountDetails"> </div>
  
				<div id="NMRBillTableModal" class="modal fade" role="dialog">
					<div class="modal-dialog" style="width:95%;">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title text-center" style="font-weight:1000;">NMR Details</h4>
							</div>
							<div class="modal-body">
								 <div class="table-responsive">
									 <table style="border:1px solid #000;width:3000px;margin-bottom:15px;" id="NMRBillstableId" >
						              <thead  id="NMRHeadData" class="cal-thead-inwards">       </thead>					  
						             <tbody id="NMRBillstableIdfirstRow" class="tbl-fixedheader-tbody">
						  
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
	<script type="text/javascript" src="js/WorkOrder/NMRStatus.js"></script>
	<script type="text/javascript">
		//$("#LoadingId").show();
		//disabling paste 
		$('body').bind('paste', function (e) { e.preventDefault();	});
		
		setTimeout(function(){		
			 loadNMRBillDataForApprovel();	
			 loadNMRCompletedBillData();			
		 }, 500); 
		 
		 var statusOfPage="${status}";
		 if(statusOfPage=="true"){
			 $("#forPrintPage").show();
		 }
		 //for sumadhura copy
		 function getSumadhuraCopy(){
			 $("#printName").val("Sumadhura Copy");
			var url="nmrBillContractorCopy.spring";
			$('#approveNMRBillFormId').attr('target', '_blank');
			document.getElementById("approveNMRBillFormId").action =url;
			document.getElementById("approveNMRBillFormId").method = "POST";
			document.getElementById("approveNMRBillFormId").submit();
		}
		//for Print NMR Details
		 function printNMRDetails(){
			 $("#printName").val("NMR Details");
			 var url="nmrBillContractorCopy.spring";
		     $('#approveNMRBillFormId').attr('target', '_blank');
			 document.getElementById("approveNMRBillFormId").action =url;
			 document.getElementById("approveNMRBillFormId").method = "POST";
			 document.getElementById("approveNMRBillFormId").submit();
		}
		//for contractor copy
		function getContractorCopy(){
			 $("#printName").val("Contractor Copy");
			 var url="nmrBillContractorCopy.spring";
		     $('#approveNMRBillFormId').attr('target', '_blank');
			 document.getElementById("approveNMRBillFormId").action =url;
			 document.getElementById("approveNMRBillFormId").method = "POST";
			 document.getElementById("approveNMRBillFormId").submit();
		}
		function closeView(){	goBack() ;}
	    function goBack() {  window.history.back();	}
			
			var sumOfRecovery=0;
			var sumOfAllCumulative=0;
	//calculating recovery amount
	   function calculateRecoveryAmount(childId){
			 var i=parseInt($("#rowsToIterate").val());
			 sumOfRecovery=0;
			 var child_product_id=$("#childId"+childId).val();
				
			 var totalAmount=$("#"+child_product_id+"totalRecoveryAmount").text();
			 totalAmount=parseFloat(totalAmount);
			 var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
			
			 sumOfAllCumulative=0;
				for (var i1 = 1; i1 <= i; i1++) {
					var temp_child_product_id=$("#childId"+i1).val();
					var recovery_amount=parseFloat($("#"+temp_child_product_id+"currentAmount").val()==""?0:$("#"+temp_child_product_id+"currentAmount").val());
				    var previous=$("#"+temp_child_product_id+"previous").text();//getting previous value using child id 
					previous=parseFloat(previous);
			     	sumOfRecovery+=(recovery_amount);
			     	$("#"+temp_child_product_id+"cumulative").text((previous+recovery_amount).toFixed(2));
			     	sumOfAllCumulative+=previous+recovery_amount;
			     	
			    }
			sumOfRecovery=sumOfRecovery.toFixed(2);
		
			var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
			cumulative=parseFloat(cumulative);
				/* if(cumulative>totalAmount){
					alert("you can't recover amount more than Cumulative amount");
					$("#"+child_product_id+"currentAmount").val($("#"+child_product_id+"currentAmount1").val());
					calculateRecoveryAmount(childId);
				} */
			$("#sumOFRecoveryCumulativce").text(sumOfAllCumulative.toFixed(2));				
			$("#sumOfCurrentAmount").text(sumOfRecovery);				
		}
		//printing the recovery values in NMR Details table
		function recoverySubmitButton(){
			$("#currentRecoveryAmount").val(sumOfRecovery);
			$("#recoverycurrentAmount").val(sumOfRecovery);
			//$("#sumOfCurrentAmount").text(sumOfRecovery);
			
			var previouscumilativeRecovery=$("#previousRecovery").val()==""?0:parseFloat($("#previousRecovery").val()); 
			$("#cumulativeRecovery").val(inrFormat(sumOfAllCumulative.toFixed(2)));
			$("#currentRecoveryAmount").val($("#sumOfCurrentAmount").text());

			$("#cumulativeRecovery").val(inrFormat(parseFloat($("#sumOFRecoveryCumulativce").text()).toFixed(2)));
			NMRCreateTableSubmit();
			$("#recoverySubmitBtnID").attr("data-dismiss", "modal");
		}
		//approving NMR Bill	
		function approveNMRBill(){
			
			var billDate=$("#billDate").val();
			if(billDate==""||billDate==null){
				alert("Please Select Date..");
				$("#billDate").focus();
				return false;
				
			}
						
			var str="";
			var rowsToIterate=$("#rowsToIterate").val();
			$("#recoveryAmountDetails").html("");
			var row1=0;
			var htmlData="";
			for (var i = 1; i <= rowsToIterate; i++) {
				var child_product_id=$("#childId"+i).val();
				var recovery_quantity=$("#"+child_product_id+"totalRecoveryAmount").text();;
				var recovery_amount=$("#"+child_product_id+"currentAmount").val();				
				var actualrecovery_amount1=$("#"+child_product_id+"currentAmount1").val();				
				var mesurment_id=$("#"+child_product_id+"mesurment_id").val();
				if(recovery_amount!="0"){
					row1++;
					htmlData+="<input type='hidden' name='child_product_id"+row1+"' id='child_product_id"+i+"' value='"+child_product_id+"'>";
					htmlData+="<input type='hidden' name='measurement_id"+row1+"' id='measurement_id"+i+"' value='"+mesurment_id+"'>";
					htmlData+="<input type='hidden' name='recovery_amount"+row1+"' id='recovery_amount"+i+"' value='"+(recovery_amount)+"'>";
					htmlData+="<input type='hidden' name='recovery_quantity"+row1+"' id='recovery_quantity"+i+"' value='"+(recovery_quantity)+"'>";
					htmlData+="<input type='hidden' name='actualrecovery_amount1"+row1+"' id='actualrecovery_amount1"+i+"' value='"+(actualrecovery_amount1)+"'>";
					htmlData+="<input type='hidden' name='currentRecoveryAmount11"+row1+"' id='currentRecoveryAmount11"+i+"' value='"+Math.abs((actualrecovery_amount1-recovery_amount)).toFixed(2)+"'>";
				}
			}
			$("#recoveryAmountDetails").html(htmlData);
			debugger;
			if($("#CCToatalAmount").text().replace(/,/g,'')<0){
				alert("Amount going negative please check.");
				return false;
			}
			
			$("#totalAmtCurntDeduc").val($("#totalAmtCurntDeduc").val().replace(/,/g,''));
			
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
			
			 var canISubmit = window.confirm("Do you want to approve bill?");	     
			 if(canISubmit == false) { return false; }
			
			 $("#recoveryAmountDetails").append("<input type='hidden' name='rowsToIterate1' id='rowsToIterate1' value='"+row1+"'>");
			 $("#rowsToIterate").val(row1);
			 $(".overlay_ims").show();	
			 $(".loader-ims").show(); 
			 
			 document.getElementById("rejectNMRID").disabled = true;   
			 document.getElementById("approveNMRID").disabled = true;   
			  	
			 document.getElementById("approveNMRBillFormId").action = "approveNMRBill.spring";
			 document.getElementById("approveNMRBillFormId").method = "POST";
			 document.getElementById("approveNMRBillFormId").submit();
		}
		//rejecting NMR Bill 
		 function rejectNMRBill(){
			 var remarks=document.getElementById("remarks");
				if(remarks.value==null||remarks.value==""){
					alert("Enter reason to reject NMR bill");
					remarks.focus;
					remarks.placeholder="Enter reason here";
				return false;
				}
			 var canISubmit = window.confirm("Do you want to reject bill?");	     
			  if(canISubmit == false) {
			        return false;
			  }
			  $(".overlay_ims").show();	
				 $(".loader-ims").show(); 
				 
				document.getElementById("rejectNMRID").disabled = true;   
				document.getElementById("approveNMRID").disabled = true;   			  
			    document.getElementById("approveNMRBillFormId").action = "rejectNMRBill.spring";
			    document.getElementById("approveNMRBillFormId").method = "POST";
			    document.getElementById("approveNMRBillFormId").submit();
			  
		 }
		 //edit NMR Details table row(Quantity)
		 function EditNMRBill(serialNo){			 
			 var canIEdit = window.confirm("Do you want to Edit this bill?");	     
			  if(canIEdit == false) {
			        return false;
			  }else{
				   
				  var Description=$("#Description"+serialNo).val();
				  var workOrderNo=$("#workOrderNo").val();
				  var approvePage=$("#approvePage").val();
				  var typeOfWork="NMR";
				  var siteId=$("#siteId").val();
				  $("#Remarks"+(serialNo)).prop("readonly",false);					
					$.ajax({
						url : "loadWorkDescNMRBills.spring",
						type : "GET",
						data : {
							minorHeadId:Description,		
							workOrderNo:workOrderNo,
							siteId:siteId
						},
						success : function(data) {
							$.each(data,function(key,value){
								
								WO_WORK_DESCRIPTION=value.WO_WORK_DESCRIPTION.replace(/ /g,'');
								 var regExpr = /[^a-zA-Z0-9-. ]/g;
						 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");  
								//$("#"+WO_WORK_DESCRIPTION+(serialNo)).val(value.ALLOCATED_QTY);
								$("#"+WO_WORK_DESCRIPTION+(serialNo)).prop("readonly",false);
							
						   });
						}
				 });
			  }			
		 }
		var dateDate1;
		//printing from date and max date in NMR Abstract table from NMR Details table 
		 function Datepush(){
		   dateDate1=[];
			$(".Date").each(function(){
				if($(this).val()!=''){
					var date = moment($(this).val(), "DD-MM-YYYY").toDate();
					var currentDate=$(this).val();
					dateDate1.push(date);
				}
			});		 
			var sorted = dateDate1.sort(sortDates);
			var minDate = sorted[0];
			var maxDate = sorted[sorted.length-1];
			
			$("#fromDate1").text(minDate.getDate()+"-"+(minDate.getMonth()+1)+"-"+minDate.getFullYear());
			$("#toDate1").text(maxDate.getDate()+"-"+(maxDate.getMonth()+1)+"-"+maxDate.getFullYear());
		 }
		 //giving max and min date
		 function sortDates(a, b){
		     return a.getTime() - b.getTime();
		 }

		
	</script>
	<script type="text/javascript">
	//updating NMR Bill
		$("#updateBtn").on("click",function(){
			 var result=validateFileExtention();
			 if(result==false){
				 return false;
			 }
			 var canISubmit = window.confirm("Do you want to update bill?");
			  
			 if(canISubmit == false) {
			        return false;
			 }
			excuteDeleteFunction();
		});
		var deleteUploadedimageFiles=new Array();
		var deleteUploadedPDFFiles=new Array();
		//here just sending url for deleting
		function deleteFile(divId,url){
			 $.ajax({
				  url : url,
				  type : "get"/* ,
				  success : function(data) {
					console.log(data);
					// window.location.reload();
				  },
				  error:  function(data, status, er){
					//  alert(data+"_"+status+"_"+er);
					} */
				  });
		}
		//deleting Work order file
		function deleteWOFile(divId,imagePath){
		 var canISubmit = window.confirm("Do you want to delete image?");
		     
		 if(canISubmit == false) {
		        return false;
		 }
		 $("#imageDivHideShow"+divId).remove();
		 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");		 
	
		 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
		 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
		 var url="deleteWOBillsImage.spring?imagePath="+imagePath+"&workOrderNo=${billBean.workOrderNo}&tempBillNo=${billBean.tempBillNo}&siteId=${billBean.siteId}";

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
			 var url="deleteWOBillsImage.spring?imagePath="+imagePath+"&workOrderNo=${billBean.workOrderNo}&tempBillNo=${billBean.tempBillNo}&siteId=${billBean.siteId}";
			
			 deleteUploadedPDFFiles.push({"pdfNo":divId,"url":url});
			 
			 $("#pdfDivHideShow"+divId).remove();
			 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");
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
			debugger;
			var len=deleteUploadedimageFiles.length;
			len=len+deleteUploadedPDFFiles.length;
			var timeOut=len*250;
			/* $(".overlay_ims").show();	
			$(".loader-ims").show(); */
			setTimeout(function(){
				var url="updateContractorBill.spring";
				document.getElementById("approveNMRBillFormId").action =url;
			    document.getElementById("approveNMRBillFormId").method = "POST";
			    document.getElementById("approveNMRBillFormId").submit();
				/* $(".overlay_ims").hide();	
				$(".loader-ims").hide(); */
			}, timeOut) ;
		
		}
		//sorting data by number's if String having pdf1,pdf0 spliting with pdf as delimeter
		function sortPdfData(a,b){
			var	pdf=a.pdfNo.split("pdf")[1];
			var	pdf1=b.pdfNo.split("pdf")[1];
			 if(pdf < pdf1) {
				    return 1;
			 }else if (pdf1 < pdf) {
				    return -1;
			 }else{ return 0;}
		}
		//sorting images 
		function sortUrlData(a, b) {
			var	imageNo=a.imageNo.split("image")[1];
			var	imageNo1=b.imageNo.split("image")[1];
			if(imageNo < imageNo1) {
				    return 1;
			}else if (imageNo1 < imageNo) {
				    return -1;
			}else{ return 0;}
		} 
		//to print page
		function printPage(){window.print();}
		
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
			var isCommonApproval="${isCommonApproval}";
			if(isCommonApproval=="true"){
				referrer="siteWiseApproveContractorBills.spring";
			}
			var isSiteWiseStatusPage="${isSiteWiseStatusPage}";
			if(isSiteWiseStatusPage=="true"){
				referrer="siteWiseContractorBillStatus.spring";
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
	
</body>
</html>
