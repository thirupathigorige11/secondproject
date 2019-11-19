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
<!-- <script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script> -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>

<c:set value="END" var="isCompletedBill"></c:set>
<c:if test="${billBean.approverEmpId eq isCompletedBill}">
<c:set value="viewCompletedBills.spring" var="urlForActivateSubModule"></c:set>
	<c:if test="${billBean.paymentType ne 'NMR'}">
		<script src="js/WorkOrder/ShowCompletedRAAdv.js" type="text/javascript"></script>
		
	</c:if>
	<c:if test="${billBean.paymentType eq 'NMR'}">
		<script src="js/WorkOrder/NMRCompleted.js" type="text/javascript"></script>
	</c:if>
</c:if>

<c:if test="${billBean.approverEmpId  ne isCompletedBill}">
<c:set value="viewContractorBillStatus.spring" var="urlForActivateSubModule"></c:set>
<c:if test="${billBean.isSiteWiseStatusPage eq 'true'}">
		<c:set value="siteWiseContractorBillStatus.spring" var="urlForActivateSubModule"></c:set>
	</c:if>
	<c:if test="${billBean.paymentType ne 'NMR'}">
			<script src="js/WorkOrder/ShowRaAdvStatus.js" type="text/javascript"></script>
	</c:if>
	<!--  based on the payment type loading JS files  -->
	<c:if test="${billBean.paymentType eq 'NMR'}">
			<script src="js/WorkOrder/NMRStatus.js" type="text/javascript"></script>
	</c:if>
</c:if>


<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>

</head>

<style>
/*css code for print */
th{text-align:center !important;}
tbody{color:#000;font-weight:bold;}
@media print{
@page {size: landscape}
.print-tnlrowcolor{background-color:#ccc !important;}
.nav_menu, .print_hide, .breadcrumb, #printshowRa{
display:none !important;
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
				window.location.assign("viewRABilsforapprovles.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>

<body class="nav-md">
	 <div class="overlay_ims"></div>
	 <div class="loader-ims" id="loaderId"> 
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
						<div class="">
      <!-- <div class="table-responsive"> -->
	  <!--Header part of table start--->
        <table style="margin-bottom: -1px;border:1px solid #000;width:100%;" id="sumadhuraLogoAndName"> <!-- class="table table-border-certificateofpayment" -->
          <tbody style="border:1px solid #000;">
            <tr>
              <td rowspan="2" colspan="2" style="width:20%;padding:6px;border:1px solid #000;"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;" /></td>
              <td  style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4></td>
             </tr>
            <tr><td  style="width:80%;border:1px solid #000;" class="text-center bck-ground"><h5><strong>Ledger </strong></h5></td>
           </tr>
         </tbody>
          
          </table>
		   <!---header part of table end--->
		  <!--details part of table start-->
        <!-- <table class="table table-border-certificateofpayment tbl-border-top tbl-border-bottom"style="margin-bottom: 0px;" > -->
        <table id="billLedgerOfBill" style="margin-bottom: -1px;margin-top: 0px;border:1px solid #000;width:100%;font-weight: bold;" >
          <tbody>
            <tr>
              <td style="width:50%;padding:15px;border:1px solid #000;">
              <div class=""style="width:45%;float:left;">Project</div><div class=""style="width:10%;float:left;">:</div><div class=""style="width:45%;float:left;"><input type="text" value="${SiteName}" class="heightthirty" style="border:none;" readonly /><input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
              <div style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Contractor&nbsp;Name</strong></div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div class="" style="width:45%;float:left;"><input type="text" class="heightthirty border-none" id="ContractorName" name="ContractorName" value="${billBean.contractorName}" readonly="readonly" style="border:none;width:100%;"  onkeyup="return populateData(this, event);"  autocomplete="off"   /><input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   /></div></div>
			<c:if test="${billBean.paymentType ne 'NMR'}">
				<div class="" style="width:100%;">
              	<div class=" mrg-Top"style="width:45%;float:left;">Type Of Work</div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;"><p id="typeOfWork" style="word-break: break-all;"></p></div>
             </div>
            </c:if>
            
           
            <c:if test="${billBean.paymentType eq 'NMR'}">
				<div class="" style="width:100%;">
              	<div class=" mrg-Top"style="width:45%;float:left;">Type Of Work</div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top"style="width:45%;float:left;"><p id="" style="word-break: break-all;"> Labour Supply</p></div>
             </div>
            </c:if>
            
            
             <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   />
             
			 <input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
			 <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${billBean.tempBillNo } " readonly="readonly">
			 <input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			 <input type="hidden" name="approvePage" id="approvePage" value="true">
			 <input type="hidden" name="billType" id="billType" value="${billBean.paymentType}">
			 <input type="hidden" name="isThisBillLedger" id="isThisBillLedger" value="true">
			 <input type="hidden" class="heightthirty border-none"  style="border:none;width: 100%;" name="workOrderNo" id="workOrderNo" readonly="readonly"  value="${billBean.workOrderNo}">
			<input type="hidden" name="billNo" id="billNo" value="${billBean.permanentBillNo}">
			 <div class="clearfix"></div> 
             </td>
              <td style="width:50%;padding:5px;">
                 <div class=" " style="width:100%;"><div class="mrg-Top" style="width:45%;float:left;"><strong>Work Order No</strong> </div><div class=" " style="width:10%;float:left;"><strong>:</strong></div><div class="  " style="width:45%;float:left;">${billBean.workOrderNo}</div></div>
                  <div class="clearfix"></div>
                <div class=" " style="width:100%;"><div class="mrg-Top" style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class=" " style="width:10%;float:left;"><strong>:</strong></div><div class="  " style="width:45%;float:left;">${billBean.contractorPanNo}</div></div>
                <div class="clearfix"></div>
                <div class=" mrg-Top"style="width:100%;"><div class="" style="width:45%;float:left;"><strong>Mobile No  </strong></div><div class="" style="width:10%;float:left;"><strong>:</strong></div><div  class="heightthirty"  style="width:45%;float:left;"> ${billBean.contractorPhoneNo}</div></div>
              </td>
            </tr>
          </tbody>
        </table>
		<!--details part of table end-->


         <!-- checkboxes table end --> 
      <!-- </div> -->
    </div>    
</form:form>

<input type="hidden" id="totalCc"><input type="hidden" id="finalAmt"><input type="hidden" id="pettyExpensesPrevCerti"><input type="hidden" id="otherAmtPrevCerti">
<input type="hidden" id="raTotalPc"><input type="hidden" id="totalAmtPreviousCertified"><input type="hidden" id="totalAmtPreviousCertified">
<input type="hidden" id="raTotalCc">
<input type="hidden" id="totalAmtCumulativeCertified">
<input type="hidden" id="raAmountToPay"><input type="hidden" id="raPc"><input type="hidden" id="raTotalPc">

<input type="hidden" id="totalAmtCurntDeduc">
<div id="appendNMRBillDetailsNoDataMsg"></div>
<div class="clearfix"></div>
 <div class="" style="margin-top: 0px;" id="paymentLedgerTable">
   <table style="border:1px solid #000;width:100%;padding:5px;">
   <thead>
    <tr class="print-tnlrowcolor">
    		 <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Date</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Particular</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Bill Amount</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Advance</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Advance Deduction</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">SD Release</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Material Deductions</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Petty Expenses</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Other Amount</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Hold Amount</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Hold Release</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">SD 5%</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Amount Paid</th>
		     <th style="border:1px solid #000;padding:5px;background-color:#ccc;">Cumulative Total</th>
	</tr>
   </thead>
  <tbody id="appendBillDetails" style="border:1px solid #000;"  class="linksContainer">
  
  </tbody>
 </table>
</div>
  
  <!-- <div id="appendBillDetailsTotal"  style="float:  right;"></div> -->
 
  <div class="col-md-12 text-center center-block Mrgtop20">
 <label>&nbsp;</label>
 </div>
 <div class="col-md-12 text-center center-block Mrgtop20">
   <input type="button" id="printshowRa" class="btn btn-warning" value="Print" onclick="myPrint()">
 
 </div>

<!-- Modal popup for approve RABill End -->
<input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
  </body>

   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>
   
	$("#paymentLedgerTable").hide();	
	$("#contractorRABill").hide();
	$("#sumadhuraLogoAndName").hide();
	$("#billLedgerOfBill").hide();
        $("#printshowRa").hide();
   
	setTimeout(function(){		
		 loadNMRCompletedBillData();			
	 }, 500); 
	
	
	debugger;  
	//this code for to active the side menu 
	var referrer="${urlForActivateSubModule}";
	if(referrer.length!=0){
	debugger;
		$SIDEBAR_MENU.find('a').filter(function () {
	           var urlArray=this.href.split( '/' );
	           for(var i=0;i<urlArray.length;i++){
	        	 if(urlArray[i]==referrer) {
	        		 return this.href;
	        	 }
	           }
	    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
	}
	

		  /*for print */
	function  myPrint(){
		window.print();
	}
	   </script>
</html>
