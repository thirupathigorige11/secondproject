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

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
</head>

<style>
/*css code for print */
@media print{
.nav_menu, .print_hide, .breadcrumb, .btn-custom-width, #printrecovery{
display:none;
}
.print-tnlrowcolor{background-color:#dbecf6 !important;}
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
#NMRHeadData tr th{
	border: 1px solid #000 !important;
} 
#NMRBillstableIdfirstRow tr td{
	border: 1px solid #000 !important;
}
#NMRAbstractTableData tr td {
	border: 1px solid #000 !important;
}
</style>
<script>
	/* 	if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("viewRABilsforapprovles.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		} */
</script>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	</style>
</noscript>

 <div class="overlay_ims" ></div>
	 <div class="loader-ims" id="loaderId"> <!--  -->
		<div class="lds-ims">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingimsMessage">Loading...</div>
	</div>


	<div class="container body" id="mainDivId">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">
					<div class="clearfix"></div>
					<jsp:include page="./../SideMenu.jsp" />
				</div>
			</div>
			<jsp:include page="./../TopMenu.jsp" />
			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active" id="CopyHeading">${printName}</li>
					</ol>
				</div>
			
			
				<c:set value="Sumadhura Copy" var="sumadhuraCopy"/>
				<c:set value="Contractor Copy" var="contractorCopy"/>
				<c:set value="NMR Details" var="nmrPrint"/>
				
		
				<table 	style="margin-bottom: -1px; border: 1px solid #000; width: 100%;font-weight:bold;">
					<tbody>
						<tr >
							<td rowspan="3" style="width:20%;border:1px solid #000;"  id="showHideImg"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;padding:10px;" /></td> 
						<!-- </tr>
						<tr>
						 -->	<td  style="width: 80%; border: 1px solid #000;"	class="text-center bck-ground"><h5>	<strong id="contractorCopyContractorName">SUMADHURA INFRACON PVT LTD</strong></h5></td>							
						</tr>
						<tr>
							<td  style="width: 80%; border: 1px solid #000;"	class="text-center bck-ground"><h5>	<strong id="nameOfBillType">Certificate of Payment</strong></h5>
								<input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   />
								<input type="hidden" name="noOfRowsToIterate" id="noOfRowsToIterate">
								<input type="hidden" name="approvePage" id="approvePage" value="true">
								<input type="hidden" name="siteId" id="siteId" value="${billBean.siteId }">
								<input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }">
								<input type="hidden" name="NMRBillNo" value="${billBean.permanentBillNo}">
								<input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
								<input type="hidden" id="workOrderNo" name="workOrderNo" value="${billBean.workOrderNo}">
								<input type="hidden" name="billType" id="billType" value="NMR">
								<input type="hidden" name="PageName" id="PageName" value="SumadhuraCopy">
								 <input type="hidden"  name="workOrderNo" id="workOrderNo" readonly="readonly"  value="${billBean.workOrderNo}">
							   	<input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
							    <input type="hidden" name="approvePage" id="approvePage" value="${approvePage}">
							    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
							    <input type="hidden" name="advPage" id="advPage" value="false">
							    <input type="hidden" id="billDate" value="${billBean.paymentReqDate}">
							    <input type="hidden" name="billNo" id="billNo" value="${billBean.billNo}">
							    <input type="hidden" name="statusOfPage" id="statusOfPage" value="${status }">
							    
			</td>							
					  
					
						</tr>
					</tbody>
				 </table>
				 
	<c:if test="${printName eq sumadhuraCopy}">
				 <table 	style="margin-bottom: -1px; border: 1px solid #000; width: 100%;font-weight:bold;" id="SumadhuraCopy">
					<tbody>
						<tr>
						  <td style="width:50%;border:1px solid #000;padding:7px;">
						     <div class="mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Contractor&nbsp;Name</strong></div><div class="mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top" style="width:45%;float:left;"><label id="ContractorName">${billBean.contractorName}</label></div></div>				          
							  <div class="clearfix"></div>
							  <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class="mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top" style="width:45%;float:left;">${billBean.contractorPanNo}</div></div>
							  <div class="clearfix"></div>							  
							  <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Account No</strong> </div><div class="mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top" style="width:45%;float:left;"> ${billBean.contractorBankAccNumber}</div></div>
							  <div class="clearfix"></div>
				              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>IFSC & Bank Name</strong>  </div><div class="mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top" style="width:45%;float:left;"> 
				          	
				        	  <c:set value="-" var="delimiter"></c:set>
				              <c:choose>
				              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
				              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
				              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
				              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
				              </c:choose>
              			</div></div>
						<div class="clearfix"></div>
				        <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Mobile No  </strong></div><div class="mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class="mrg-Top" style="width:45%;float:left;"> ${billBean.contractorPhoneNo}</div></div>
						<div class="clearfix"></div> 
						  </td>
						  <td style="width:50%;border:1px solid #000;padding:7px;">
						     <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Project</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"> <strong>${billBean.siteName}</strong><input type="hidden"  name = "siteId" id = "site_id" value="${billBean.siteId}"  /></div> </div>
			                 <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Work order No</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="workOrderNo"> ${billBean.workOrderNo}</label></div></div>
				              <div class="" style="width:100%;"><div class="mrg-Top" style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;" ><label class="addFractionAndMakeInrFormat"  id="workOrderAMount">${billBean.totalAmount}</label> </div> </div>
				             <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Type Of Work </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="typeOfWork" style="float: left;">Labour Supply</label></div></div>
				             <div class="" style="width:100%;"><div style="width:45%;float:left;" class=" mrg-Top"> Bill No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top">${billBean.billNo }</div></div>
				             <div class="" style="width:100%;"><div style="width:45%;float:left;" class=" mrg-Top"> Invoice No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top">${billBean.billInvoiceNumber}</div></div>
				             <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Bill Date </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="totalAmtToPay">${billBean.paymentReqDate}</label></div></div>
						  </td>
						</tr>
					</tbody>
				</table>
	</c:if>

   <c:if test="${printName eq contractorCopy}">
       <table style="margin-bottom: -1px;border:1px solid #000;width:100%;font-weight:bold;" id="ContractorCopy">
          <tbody>
            <tr>
              <td style="width:50%;border:1px solid #000;padding:5px;">
              <div class=""><div class=""><strong>To,</strong></div></div>
			  <div class="clearfix"></div>
			   <div class=""><div class=""><strong>Sumadhura Infracon Pvt Ltd</strong></div></div>
			  <div class="clearfix"></div>
			  <div class=""><div class="">
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
			  <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class=" mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  
			  <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top"style="width:45%;float:left;"><strong>Account No</strong> </div><div class=" mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;"> ${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top"style="width:45%;float:left;"><strong>IFSC & Bank Name</strong>  </div><div class=" mrg-Top"style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top"style="width:45%;float:left;">
              
              				 <c:set value="-" var="delimiter"></c:set>
				              <c:choose>
				              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
				              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
				              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
				              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
				              </c:choose>
              </div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Mobile No  </strong></div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;"> ${billBean.contractorPhoneNo}<br></div></div>
			  <div class="clearfix"></div> 
             
             
              </td>
                <td style="width:50%;border:1px solid #000;padding:7px;">
						     <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Project</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"> <strong>${billBean.siteName}</strong><input type="hidden"  name = "siteId" id = "site_id" value="${billBean.siteId}"  /></div> </div>
			                 <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Work order No</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="workOrderNo"> ${billBean.workOrderNo}</label></div></div>
				              <div class="" style="width:100%;"><div class="mrg-Top" style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;"  id="workOrderAMount" ><label class="addFractionAndMakeInrFormat" id="workOrderAmount">${billBean.totalAmount}</label> </div> </div>
				             <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Type Of Work </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="typeOfWork" style="float: left;">Labour Supply</label></div></div>
				             <div class="" style="width:100%;"><div style="width:45%;float:left;" class=" mrg-Top"> Bill No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top">${billBean.billNo }</div></div>
				             <div class="" style="width:100%;"><div style="width:45%;float:left;" class=" mrg-Top"> Invoice No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top">${billBean.billInvoiceNumber }</div></div>
				             <div class="" style="width:100%;"><div class=" mrg-Top"  style="width:45%;float:left;">Bill Date </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"><label id="totalAmtToPay">${billBean.paymentReqDate}</label></div></div>
				</td>
<%--               <td style="width:50%;padding:5px;">
              <!-- removed by ACP -->
              <!-- <div class="" style="height:50px;"></div> --> 
              <div class="" style="width:100%;margin-top: 10px;">
			  <div class="" style="width:45%;float:left;"><strong>Project</strong></div><div class="" style="width:10%;float:left;">:</div><div class="" style="width:45%;float:left;"><strong>${billBean.siteName}</strong></div>
			  	</div>
			  	<div class="clearfix"></div>
			  	 <div class="" style="width:100%;margin-top: 10px;">
			  	<div  class="" style="width:45%;float:left;"><strong>Type of work</strong></div><div class="" style="width:10%;float:left;">:</div><div style="width:45%;float:left;"class="" id="typeOfWork">Labour Supply</div>
			  </div>
			  <div class="clearfix"></div>
			   <div class="" style="width:100%;margin-top: 10px;">
			   <!-- <div  class="col-md-4">Type of work </div><div class="col-md-1">:</div><div class="col-md-6"><input type="text" value="Masonry Work" style="border:none;" readonly /></div> -->
			    <div class="" style="width:45%;float:left;"><strong>Work order No</strong></div><div class="" style="width:10%;float:left;">:</div><div class="" style="width:45%;float:left;"> ${billBean.workOrderNo}
			  
			    </div>
			    </div>
			    <div class="clearfix"></div>
			     <div class="" style="width:100%;margin-top: 10px;">
	              	<div class="mrg-Top" style="width:45%;float:left;">WO Amount </div><div class="mrg-Top"style="width:10%;float:left;">:</div><div class="mrg-Top" style="width:45%;float:left;"  id="workOrderAMount">${billBean.totalAmount}</div>
	           </div>
              <div class="clearfix"></div>
              	<div class="" style="width:100%;margin-top: 10px;">
              	<div style="width:45%;float:left;" class="mrg-Top">Bill No </div><div class="" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top">${billBean.billNo }</div>
              	 </div>
              	 <div class="clearfix"></div>
               <div class="" style="width:100%;margin-top: 10px;">
               		<div class="mrg-Top" style="width:45%;float:left;"><strong>Bill Date</strong></div><div class="" style="width:10%;float:left;">:</div><div class=" mrg-Top" style="width:45%;float:left;">${billBean.paymentReqDate}</div>
           		</div>
              </td> --%>
            </tr>
          </tbody>
        </table>
        </c:if>
		<!--details part of table end-->
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
		<table  style="margin-bottom:25px;border:1px solid #000;width:100%;font-weight:bold;">
             <thead class="thead-color-certificateofpayment">
		          <tr>
		              <th rowspan="2" class="text-center vertical-alignment" style="width:5%;border:1px solid #000;">S.NO</th>
		              <th rowspan="2" class="text-center vertical-alignment" style="width:45%;border:1px solid #000;">Description</th>
		              <th colspan="3" class="text-center" style="width:50%;border:1px solid #000;">Amount</th>
		            </tr>
		            <tr>
		              <th class="text-center"style="border:1px solid #000;">Cumulative Certified</th>
		              <th class="text-center"style="border:1px solid #000;">Previous Certified</th>
		              <th class="text-center"style="border:1px solid #000;">Current Certified</th>
		            </tr>
                   </thead>
                  <tbody>
		               <tr style="font-size: 15px;" class="print-tnlrowcolor">
		              <td class="text-center" style="border:1px solid #000;padding:5px;"><strong>I</strong></td>
		              <td class="text-center td-active border-none input-outline" style="border:1px solid #000;padding:5px;"><span style="font-weight: 900;">Total Value of Work Completed / Certified </span></td>
		              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><label  id="raCc"  class="addFractionAndMakeInrFormat"  style="border:none; text-align:center;"></label></td>
		              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><label  id="raPc"  class="addFractionAndMakeInrFormat"  style="border:none; text-align:center;"></label></td>
		              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;" > <label  id="raAmountToPay" class="addFractionAndMakeInrFormat" style="border:none; text-align:center;">${billBean.totalCurrentCerti}</label></td><!--  ${billBean.totalCurrentCerti}amount to pay-->
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
		              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><label id="raTotalCc"  style="border:none;text-align:center;" class="addFractionAndMakeInrFormat" ></label></td>
		              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><label  id="raTotalPc" style="border:none;text-align:center;"  class="addFractionAndMakeInrFormat" ></label></td>
		              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualRaAmountToPay" id="actualRaAmountToPay" value="${billBean.totalCurrentCerti }">  <label id="totalCc" name="totalCc" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;">${billBean.totalCurrentCerti }</label></td>
		            </tr>
		         <tr>
              <td class="text-center" style="border:1px solid #000;padding: 5px;">II</td>
              <td class="text-center" style="border:1px solid #000;padding: 5px;"><span>Paid Amount</span></td>
              
              <td class="text-center" style="border:1px solid #000;padding: 5px;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt addFractionAndMakeInrFormat CumilativeCAmount"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding: 5px;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt addFractionAndMakeInrFormat PreviousCAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding: 5px;"><input type="text" class="raPaidAmnt addFractionAndMakeInrFormat" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0.00" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
           <tr>
           
              <td class="text-center" style="border:1px solid #000;padding:5px;">a)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Advance Deduction</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raeductionCumulative" id="raeductionCumulative" class="addFractionAndMakeInrFormat CumilativeCAmount"  style="border:none;text-align:center;" value="0.00" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raePrevductionCumulative" id="raeductionPrevCumulative" class="addFractionAndMakeInrFormat PreviousCAmnt"  style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt"> <input type="text"  name="raDeductionAmt" id="raDeductionAmt" class="addFractionAndMakeInrFormat currentCAmnt" autocomplete="off"  onfocusout="raCalcAmountToPay(contractorRABill.raAmountToPay.value,2)"  style="border:none;text-align:center;" value="0.00" readonly/></td><!-- deduction amt here -->
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">b)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Security Deposit(E)</span><span id="securityPer"></span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositCumulative" id="secDepositCumulative" class="addFractionAndMakeInrFormat CumilativeCAmount" style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositPrevCerti" id="secDepositPrevCerti" class="addFractionAndMakeInrFormat PreviousCAmnt" style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" > <input type="text"  name="secDepositCurrentCerti" class="addFractionAndMakeInrFormat currentCAmnt" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="0.00" readonly/></td>
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">c)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Petty Expenses</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCumulative" id="pettyExpensesCumulative" class="addFractionAndMakeInrFormat CumilativeCAmount" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesPrevCerti" id="pettyExpensesPrevCerti" class="addFractionAndMakeInrFormat PreviousCAmnt" style="border:none;text-align:center;" value="${pettyExpensesPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti" class="addFractionAndMakeInrFormat currentCAmnt"  autocomplete="off"   style="border:none;text-align:center;" value="${pettyExpensesCurrentCerti }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Others</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtCumulative" id="otherAmtCumulative" class="addFractionAndMakeInrFormat CumilativeCAmount"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtPrevCerti" id="otherAmtPrevCerti" class="addFractionAndMakeInrFormat PreviousCAmnt"  style="border:none;text-align:center;" value="${otherAmtPrevCerti}" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="other" id="other"  autocomplete="off" class="addFractionAndMakeInrFormat currentCAmnt"  style="border:none;text-align:center;" value="${other }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;" ><input type="text"  name="cumulativeRecovery" class="CcAmnt addFractionAndMakeInrFormat CumilativeCAmount" id="sumOfCumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;" ><input type="text"  name="previousRecovery" class="PcAmnt addFractionAndMakeInrFormat PreviousCAmnt" id="sumOfPreviousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span id="currentRecoveryAmount">${recoveryAmount}</span>   <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount}" class="currentCAmnt"> <a class="print_hide" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
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
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt" class="addFractionAndMakeInrFormat"  style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvCurrent" id="outstandingAdvCurrent" class="addFractionAndMakeInrFormat"  autocomplete="off"  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">IV</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Release advance (G)</span> </td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;" value="0.00" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt" class="addFractionAndMakeInrFormat"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount" class="addFractionAndMakeInrFormat" autocomplete="off"  style="border:none;text-align:center;" value="0.00" onfocusout="calculateAdvBillAmt(this.value)" readonly/></td>
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
					 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span> Amount In Words</span></td>
					 <td class="text-center td-active" colspan="3" id="nmrAmountInWords" style="border:1px solid #000;padding:5px;"></td>			
			</tr>
					
			 	    <tr id="SumadhuraEmpSign"> 
		               <td colspan="5" style="border:1px solid #000;padding:5px;">
		               <div style="width:100%;margin-top:50px;"></div>
		               <div style="width:100%;">
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
								  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</strong><br>
								  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</strong><br>
								  	  <strong  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</strong><br>
								    </h5>
							    </div>
						  	</c:when>
						  
							  	<c:when test="${countVerifiedEmpNames eq listOfVerifiedEmpNames.size() && nextLevelApprovedId eq workOrderStatus}">
								
								  	 <div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
									    <h5>
									      <strong style="text-align: center;">Approved By </strong><br><br>
									  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</strong><br>
									  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</strong><br>
									  	  <strong  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</strong><br>
									    </h5>
								    </div>
							
							  	</c:when>
						  	
						  	<c:otherwise>
							  	<div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
								    <h5>
								 	  <strong style="text-align: center;">Verified By </strong><br><br>
								  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</strong><br>
								  	  <strong style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</strong><br>
								  	  <strong  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATED_DATE }</strong><br>
								    </h5>
							    </div>
						  	</c:otherwise>
						  </c:choose>
						
						   <c:set value="${countVerifiedEmpNames+1 }" var="countVerifiedEmpNames"></c:set>
					   </c:forEach>
					   
		               </div>
		              </td>
		            </tr>
		             <tr id="ContractorSign" style="display: none;"> 
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
             
			 <div class="col-md-12 text-center center-block btn-print-contractorcopy" style="margin-bottom:30px;margin-top:30px;">
  	 	      <input type="button" name="submitBtn"   class="btn btn-warning btn-custom-width" onclick="printPage()" 	value="Print" />
  	 	    <!--   <input type="button" name="contractorNMRCopy"  id="contractorNMRCopy"  class="btn btn-warning btn-custom-width" onclick="contractorCopyPrintPage()" value="Contractor Copy" /> -->
  	 	      <!--   <input type="button" name="sumadhuraNMRCopy"   class="btn btn-warning btn-custom-width" onclick="contractorCopyPrintPage()" value="Sumadhura Copy" /> -->
   		    </div>  
				 
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
			          <th>Cummulative Recovered</th>
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
			         <!-- <button type="button" class="btn btn-danger" id="printContractorCopy" onclick="">Print</button> -->
			        </div>
			      </div>
			    </div>
		
			  </div>
			</div>
			<!-- table added dynamically -->
			<div class="col-md-12">
			 <!-- 	<div style="display: none;"  id="NMRDetailsTable"> -->
	         <div class="" style="overflow-y:auto;">
	            <table style="border:1px solid #000;width:1500px;margin-bottom:15px;display: none;" id="NMRBillstableId" >
					   <thead  id="NMRHeadData">
					 
					   </thead>
					  
					  <tbody id="NMRBillstableIdfirstRow">
					 </tbody>
			 </table>	
	         </div>
	</div>
			<table style="border:1px solid #000;width:100%;margin-bottom:15px;margin-top:-1px;display: none;" id="NMRDetailsTable">
					  <tbody id="NMRAbstractTableData">
					  </tbody>
			 </table> 
<!-- 	</div> -->
			<!-- table added dynamically -->
		 </div>
		</div>
	</div>
	


<script src="js/custom.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>	
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>	
<script src="js/WorkOrder/NMRTable.js"></script>

<script src="js/WorkOrder/workorder.js" type="text/javascript"></script>
<c:set value="END" var="BillStatus"/>

<c:if test="${nextLevelApprovedId eq BillStatus}">
	<script type="text/javascript" src="js/WorkOrder/NMRCompleted.js"></script>
	<c:set value="viewCompletedBills.spring" var="urlForActivateSubModule"></c:set>
</c:if>
<c:if test="${nextLevelApprovedId ne BillStatus}">
<c:set value="viewContractorBillStatus.spring" var="urlForActivateSubModule"></c:set>
			<script type="text/javascript" src="js/WorkOrder/NMRStatus.js"></script>
</c:if>



<script>

function printPage(){
	window.print();
}


var contractorCopy="${contractorCopy}";
var sumadhuraCopy="${sumadhuraCopy}";
var nmrPrint="${nmrPrint}";
var printName="${printName}";
debugger;
if(printName==contractorCopy){
	contractorCopyPrintPage();	
}
if(printName==nmrPrint){
	//var str=$("#NMRDetailsTable").show();
	//$("#NMRBillstableId").show();
	//console.log(str);
}
function contractorCopyPrintPage(){
	debugger;
	$("#SumadhuraCopy").hide();
	$("#ContractorCopy").show();
	$("#contractorCopyContractorName").html($("#ContractorName").val());
	$("#nameOfBillType").html("Bill");
	$("#contractorNMRCopy").hide();
	$("#ContractorSign").show();
	$("#SumadhuraEmpSign").hide();
	$("#showHideImg").hide();
//	printPage();
}

setTimeout(function(){	
		loadNMRCompletedBillData();
		loadRARecovery();
		//addFractionAndInrFormat
		var amountInWords=	convertNumberToWords(parseFloat($("#finalAmt").val().replace(/,/g,'')));
		$("#nmrAmountInWords").text(amountInWords);
		//var TotalPaidAmt1=$("#TotalPaidAmt1").text()==""?0:parseFloat($("#TotalPaidAmt1").text());
		//var currentRecoveryAmount=parseFloat($("#currentRecoveryAmount").val()==""?0:$("#currentRecoveryAmount").val());
		//$("#totalAmtCumulative").val((TotalPaidAmt1+parseFloat(currentRecoveryAmount)).toFixed(2));
		// setTimeout(function(){
				/* $(".addFractionAndMakeInrFormat").each(function(){
					debugger;
					var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
					$(this).text(inrFormat(currentvalue.toFixed(2)));
				});
				$(".addFractionAndMakeInrFormattxt").each(function(){
					debugger;
					var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
					$(this).val(inrFormat(currentvalue.toFixed(2)));
				}); */
				$(".overlay_ims").hide();	
				$(".loader-ims").hide();
			//},1000); 
}, 200);

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
//making comma to work order amount
$(window).load(function() {
	$("#workOrderAmount").text(inrFormat(parseFloat($("#workOrderAmount").text().replace(/,/g,'')).toFixed(2)));
	$("#workOrderAMount").text(inrFormat(parseFloat($("#workOrderAMount").text().replace(/,/g,'')).toFixed(2)));
});
</script>
</body>
</html>
