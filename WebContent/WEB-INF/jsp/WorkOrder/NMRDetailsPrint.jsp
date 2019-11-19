<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="../CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- <link href="css/style.css" rel="stylesheet" type="text/css">-->
<!-- <!-- <link href="css/custom.min.css" rel="stylesheet">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">

 --> 
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>


<title>NMR Details</title>
<style>
/* loader css */
#loadingMessage{
   display: inline-block;
    position: absolute;
    top: 65%;
    left: 58%;
    margin-top: 54px;
    margin-left: 15px;
    color: #fff;
}


.lds-facebook {
  display: inline-block;
  position: absolute;
  width: 84px;
  height: 64px;
  background-color:#ef7e2d;
  padding:5px;

}

.loader-sumadhura{ 
	position: absolute;
   left:43%;
   top:40%;
   }
    @media only screen and (min-width:320px) and (max-width:767px){
    .loader-sumadhura {
    position: absolute;
    left: 40%;
    top: 75%;
}
   }
.lds-facebook div {
  display: inline-block;
  position: absolute;
  left: 6px;
  width: 5px;
  background: #fff;
  animation: lds-facebook 1.2s cubic-bezier(0, 0.5, 0.5, 1) infinite;
}
.lds-facebook div:nth-child(1) {
  left: 16px;
  animation-delay: -0.60s;
}
.lds-facebook div:nth-child(2) {
  left: 25px;
  
  animation-delay: -0.48s;
}
.lds-facebook div:nth-child(3) {
  left: 35px;
  
  animation-delay: -0.36s;
}
.lds-facebook div:nth-child(4) {
  left: 45px;
  animation-delay: -0.24s;
  
}
.lds-facebook div:nth-child(5) {
  left: 55px;
  animation-delay: -0.12s;
  
}
.lds-facebook div:nth-child(6) {
  left: 65px;
  animation-delay: 0;
  
}
@keyframes lds-facebook {
  0% {
    top: 6px;
    height: 51px;
  }
  50%, 100% {
    top: 19px;
    height: 26px;
  }
}
/* loader css */
.print_top{margin-top: 15px;}
 @media print{
 @page {size: landscape; margin: 2mm;margin-right: 2mm;} 
 .print_top{margin-top: 25px;}
 .printbtn{display:none;}
  body{font-size:10px;}
  thead {
display: table-row-group;
}
}
   /*  body { 
        writing-mode: tb-rl;
    } */
</style>
</head>
<body style="margin: 15px;">

 <div class="overlay_ims" ></div>
	 <div class="loader-sumadhura" id="loaderId"> 
		<div class="lds-facebook">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingMessage">Loading...</div>
	</div>


<table style="border:1px solid #000;width:100%;border-collapse: collapse;" cellpadding="0">
	 <tbody>
		   <tr>
		     <td style="border:1px solid #000;" colspan="6">
			     <div style="width:15%;border-right:1px solid #000;text-align:center;float:left;"><img src="images/SumadhuraLogo2015.png" style="margin-top:5px;margin-bottom:5px;" /></div>
			     <div style="width:80%;text-align:center;float:left;"><h2 class="print_top"style="margin-bottom: 2px;">
			     SUMADHURA INFRACON PVT LTD </h2>
			    <span> 
			     <strong>
			   		<c:forEach items="${SiteAddress }" var="siteAddr">
						<p> <strong> ${siteAddr}</strong></p>
					</c:forEach>
		      	</strong>
		      </span>
                  
                  </div>
		     </td>
		   </tr>
		   <tr><td style="border:1px solid #000;padding:10px;"colspan="6"></td></tr>
		   <tr><td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;"colspan="6">NMR DETAILS</td></tr>
		   <tr><td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;"colspan="6">
		<strong>${billBean.contractorName}</strong>
		   
		   						<input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   />
								<input type="hidden" name="noOfRowsToIterate" id="noOfRowsToIterate">
								<input type="hidden" name="approvePage" id="approvePage" value="true">
								<input type="hidden" name="siteId" id="siteId" value="${billBean.siteId }">
								<input type="hidden" name="tempBillNo" id="tempBillNo" value="${billBean.tempBillNo }">
								<input type="hidden" name="NMRBillNo" value="${billBean.permanentBillNo}">
								<input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
								<input type="hidden" id="workOrderNo" name="workOrderNo" value="${billBean.workOrderNo}">
								<input type="hidden" name="billType" id="billType" value="NMR">
								 <input type="hidden"  name="workOrderNo" id="workOrderNo" readonly="readonly"  value="${billBean.workOrderNo}">
							   	<input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
							    <input type="hidden" name="approvePage" id="approvePage" value="${approvePage}">
							    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
							    <input type="hidden" name="advPage" id="advPage" value="false">
							    <input type="hidden" id="billDate" value="${billBean.paymentReqDate}">
							    <input type="hidden" name="billNo" id="billNo" value="${billBean.billNo}">
							    <input type="hidden" name="statusOfPage" id="statusOfPage" value="${status }">
		   
		   
		   
		   
		   </td></tr>
		  <!--  <tr><td style="border:1px solid #000;padding:2px;text-align:right;font-weight:bold;"colspan="6"></td></tr> -->
		   <tr><td style="border:1px solid #000;padding:2px;text-align:left;font-weight:bold;"colspan="6">VENDOR : Labour supply</td></tr>
		    <tr><td style="border:1px solid #000;padding:2px;text-align:left;font-weight:bold;" colspan="4">Work Order/Ref. No : ${billBean.workOrderNo} - Bill No : ${billBean.billNo}</td>
		    <!-- <td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;"><span style="visibility:hidden;">Lorem</span></td>
		    <td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;"><span style="visibility:hidden;">Lorem</span></td>
		    <td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;"><span style="visibility:hidden;">Lorem</span></td> -->
		    <td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;">Date</td>
		    <td style="border:1px solid #000;padding:2px;text-align:center;font-weight:bold;">${billBean.paymentReqDate}</td>
		    </tr>
	 </tbody>
</table>

<!-- 
     <table style="border:1px solid #000;width:1500px;margin-bottom:15px;" id="NMRBillstableId" >
					   <thead  id="NMRHeadData">		  

					   </thead>
					  
					  <tbody id="NMRBillstableIdfirstRow">

					     </tbody>
		 </table>	 -->



<table style="border:1px solid #000;width:100%;border-collapse: collapse;margin-top:-1px;" cellpadding="0">
	<thead  id="NMRHeadData">		  	  
<!-- 	  <tr>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">S.No</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Date</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Description</th>
	    <th colspan="2" style="background-color:#ccc;border:1px solid #000;">Duration</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">No.hours</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">M/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">F/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">M/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">F/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">M/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">F/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">M/C</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">F/C</th>
	    <th colspan="8" style="background-color:#ccc;border:1px solid #000;">Hours</th>
	    <th colspan="2" style="background-color:#ccc;border:1px solid #000;">Hours</th>
	    <th colspan="2" style="background-color:#ccc;border:1px solid #000;">Hours</th>
	    <th colspan="2" style="background-color:#ccc;border:1px solid #000;">Hours</th>
	    <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Remarks1</th>
	  </tr> -->
	<!--   <tr>
	    
	    <th style="background-color:#ccc;border:1px solid #000;">From</th>
	    <th style="background-color:#ccc;border:1px solid #000;">To</th>
	    <th style="background-color:#ccc;border:1px solid #000;">M/C(hrs)</th>
	    <th style="background-color:#ccc;border:1px solid #000;">F/C(hrs)</th>
	     <th style="background-color:#ccc;border:1px solid #000;">M/C(hrs)</th>
	    <th style="background-color:#ccc;border:1px solid #000;">F/C(hrs)</th>
	     <th style="background-color:#ccc;border:1px solid #000;">M/C(hrs)</th>
	    <th style="background-color:#ccc;border:1px solid #000;">F/C(hrs)</th>
	     <th style="background-color:#ccc;border:1px solid #000;">M/C(hrs)</th>
	    <th style="background-color:#ccc;border:1px solid #000;">F/C(hrs)</th>
	  </tr> -->
	</thead>
	<tbody id="NMRBillstableIdfirstRow">
	 <tr>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">1</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">23-10-2018</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">House Keeping Work</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">9.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">18.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">8.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">Dummy data tata</td>
	</tr>
	<tr>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">1</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">23-10-2018</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">House Keeping Work</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">9.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">18.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">8.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">Dummy data tata</td>
	</tr>
	<tr>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">1</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">23-10-2018</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">House Keeping Work</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">9.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">18.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">8.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">Dummy data tata</td>
	</tr>
	<tr>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">1</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">23-10-2018</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">House Keeping Work</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">9.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">18.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">8.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">3.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">24.00</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">-</td>
	      <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">Dummy data data</td>
	</tr>
	
	<tr>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">Total Hours</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">98.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">98.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">98.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">98.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	</tr> 
	<tr>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">Total Labours</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">22.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">22.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">22.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;">22.00</th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	      <th style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;"></th>
	</tr> 
	</tbody>
</table>
 <div style="width:100%;text-align:center;margin-top:0px;">
  						<%--   <c:if test="${status eq true }"> --%>
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
			<%-- 	</c:if>		 --%>
 </div>
 <div style="text-align:center;">
 <button class="printbtn" style="color:#fff;background-color:orange;border-radius:5px;border:1px solid orange;padding:6px 12px;margin-top:15px;text-align:center;" onclick="window.print();">Print</button>
 </div>
 <%-- ${nextLevelApprovedId} --%>


<script src="js/custom.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>	
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>	
<script src="js/WorkOrder/NMRTable.js"></script>


 
 <c:set value="END" var="BillStatus"/>
	<c:if test="${nextLevelApprovedId eq BillStatus}">
		<script type="text/javascript" src="js/WorkOrder/NMRCompleted.js"></script>
	</c:if>
	<c:if test="${nextLevelApprovedId ne BillStatus}">
	<script type="text/javascript" src="js/WorkOrder/NMRStatus.js"></script>
	</c:if>
	
	
	<script type="text/javascript">
	
	 
 	setTimeout(function(){
		 //loadRARecovery();
	loadNMRBillDataForApprovel();	
		//loadNMRCompletedBillData();
	}, 300);
 	 
 	function loadNMRBillDataForApprovel(){
		debugger;
		var ContractorId=$("#ContractorId").val();
		var ContractorName=$("#ContractorName").val();
		var workOrderNo=$("#workOrderNo").val();
		var approvePage=$("#approvePage").val();
		var typeOfWork="NMR";
		var siteId=$("#site_id").val();
		var approvePage="true";
		var tempBillNo=$("#tempBillNo").val();
		var BillNo=$("#billNo").val();
		var billType=$("#billType").val();
		var requestFrom="nmrDetailsPrint";
		if(workOrderNo.length==0){
			return false;
		}
		var url="";
		if("${nextLevelApprovedId}"=="END"){
			url = "loadPermanentNMRBillDetailsData.spring";	
		}else{
			url = "loadNMRBillData.spring";
		}
		
		$.ajax({
			url :url,
			type : "GET",
			data : {
				contractorId : ContractorId,
				workOrderNo:workOrderNo,
				approvePage:approvePage,
				siteId:siteId,
				site_id:siteId,
				typeOfWork:typeOfWork,
				approvePage: approvePage,
				tempBillNo:tempBillNo,
				BillNo:BillNo,
				billType:billType,
				requestFrom:requestFrom
			},
			success : function(data) {
			//console.log(JSON.stringify(data));
			
			//fruits.splice(index,1); used for remove element from an array
				var workDescNames=new Array();
				var tempworkDescNames=new Array();
				var minorHeadNames=new Array();
				var minorHeadNameAndDate=new Array();
				var strNMRFirstRowtbodyData="";
				
				var strNMRSecondRowtbodyData="";
				var strNMRThirdRowtbodyData="";
				var strNMRtheadData="";
				var strAbstractTableData="";
				var strFirstRowOfTableHead="";
				var strSecondRowOfTableHead="";
				var serialNumber=1;
				var d = new Date();
				var date = d.toLocaleDateString();
				
				lengthOfRows=data.length;
				//dynamic Headers for table 
				
				console.log("data "+data);
				var tempWorkDeskName="";
		 	    var tempRows=0;
		 	    
			//*******************************NMR Main Screen Data**********************************
		 	    						serialNumber=1;
				var rowsOFNMR=1;
				tempWorkDeskName="";
				
				var tempWDName=new Array();
				var allWDNameAndDate=new Array();
				 var tempMinorHeadName="";
				//nmr table date started
				$.each(data,function(key,value){
					
					var strAbstractTableData="";
					
					const isThis_Str_InArray1 = value.WO_MINORHEAD_DESC.replace(regExpr, "")+ "@@"+ value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+ "@@" + value.RATE+"@@"+value.WO_ROW_CODE+"@@"+value.NMRROWNUM;
					const isInArray1 = minorHeadNames.includes(isThis_Str_InArray1);
					if (isInArray1 == false) {
						debugger;
						minorHeadNames.push(isThis_Str_InArray1);
					}
					
					let WO_WORK_DESCRIPTION=	value.WO_WORK_DESCRIPTION.replace(/ /g,'');
					try {
						WO_WORK_DESCRIPTION=	WO_WORK_DESCRIPTION.trim();
						var regExpr = /[^a-zA-Z0-9-. ]/g;
						WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
					} catch (e) {
						console.log(e);
					}
					workDescNames.push(WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID);
					//NMR Abstract Data 
					const needle = value.WO_WORK_DESCRIPTION;
					const isInArray = 	tempWDName.includes(needle);
					allWDNameAndDate.push(value.WO_WORK_DESCRIPTION+"@@"+value.WORK_DATE+"@@"+value.ALLOCATED_QTY);
					
					if(tempMinorHeadName!=(value.WO_MINORHEAD_DESC+value.WO_ROW_CODE)){
						tempMinorHeadName=(value.WO_MINORHEAD_DESC+value.WO_ROW_CODE);
						strAbstractTableData+='<tr>';
						strAbstractTableData+='<td style="text-align:center;padding:5px;"><h5><strong></strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+value.WO_WORK_DESC_ID+'"><input type="hidden" name="majorheadId" id="majorheadId" value="'+value.WO_MAJORHEAD_ID+'"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+value.WO_MINORHEAD_ID+'"></td>';
						strAbstractTableData+='<td style="padding:5px;" colspan="10"><h5><strong style="font-size:14px;">'+ value.WO_MINORHEAD_DESC	+ '('+value.WO_ROW_CODE+')</strong><h5><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+value.WO_MEASURMENT_ID+'"></td>';
						strAbstractTableData+='</tr>';
						tempWorkDeskName="";
					}
					
					var regExpr = /[^a-zA-Z0-9-. ]/g;
					//Avoiding Duplicate Date to be Printed
					if(isInArray==false){
							tempWDName.push(value.WO_WORK_DESCRIPTION);
						    tempworkDescNames.push(value.WO_WORK_DESCRIPTION.replace(regExpr, "")+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC.replace(regExpr, "")+"@@"+value.WORK_DATE);
					}
					if(tempWorkDeskName!=value.WO_WORK_DESCRIPTION){
						tempWorkDeskName=value.WO_WORK_DESCRIPTION;
					
						var previousQty=0;
						var prevAreaQuantity=new Array();
						var lengthOfThePrevArea=0;
						
						//var previousQty=value.PREVQTY==null?0:value.PREVQTY;
						 	
						 	try {
								var index=value.PREVQTY.search("@@");
								if(index>=0){
									prevAreaQuantity=value.PREVQTY.split("@@");
									for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
										let array_element = prevAreaQuantity[ind].split("&&");
										previousQty+=parseFloat((array_element[0]*array_element[1])/8);
									}
									//alert(prevAreaQuantity.toString());
								}else{
									prevAreaQuantity = value.PREVQTY.split("&&");
									previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
									
									
								}
							} catch (e) {
								//alert(e);
							}
							previousQty=previousQty.toFixed(2);
						 	var allocatedWorkers=value.ALLOCATED_QTY==null?0:parseInt(value.ALLOCATED_QTY);
						 	strAbstractTableData+='<tr>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><h5><strong>'+rowsOFNMR+'</strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+value.WO_WORK_DESC_ID+'"><input type="hidden" name="majorheadId" id="majorheadId" value="'+value.WO_MAJORHEAD_ID+'"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+value.WO_MINORHEAD_ID+'"></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong>'+value.WO_WORK_DESCRIPTION+'</strong>';
							strAbstractTableData+='<input type="hidden" name="mesurmentId" id="mesurmentId" value="'+value.WO_MEASURMENT_ID+'">';
							strAbstractTableData+='<input type="hidden" name="tempBillNo" id="tempBillNo" value="'+value.TEMP_BILL_ID+'">';
							strAbstractTableData+='<input type="hidden" name="QS_INV_AGN_WORK_PMT_DTL_ID" id="QS_INV_AGN_WORK_PMT_DTL_ID" value="'+value.QS_INV_AGN_WORK_PMT_DTL_ID+'">';
							strAbstractTableData+='</td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong>'+parseFloat(value.QUANTITY).toFixed(2)+'</strong> </td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong id="'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Rate">'+parseFloat(value.RATE).toFixed(2)+'</strong></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong>'+value.WO_MEASURMEN_NAME+'</strong></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong   class="CBQtyClass" id="CB'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Qty" >'+((previousQty+allocatedWorkers))+'</strong></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  class="CBAmountClass" id="CB'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Amount" >'+((previousQty+allocatedWorkers)*value.RATE)+'</strong></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong class="PCQtyClass" id="PC'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Qty">'+previousQty+'</strong></td>';
							strAbstractTableData+=' <td style="text-align:center;padding:5px;"><strong  class="PCAmountClass" id="PC'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Amount" >'+((previousQty*value.RATE).toFixed(2))+'</strong></td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  id="'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Qty" class="CCQtyClass">0</strong> </td>';
							strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  id="'+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+'Amount" class="CCAmountClass">'+((allocatedWorkers*value.RATE).toFixed(2))+'</strong>  </td>';				       
							strAbstractTableData+='</tr>';
							rowsOFNMR++;
					}
					serialNumber++;
					$("#NMRAbstractTableData").append(strAbstractTableData);
				});
				
				//<input type="hidden" id="'+WO_WORK_DESCRIPTION+'Qty1" value="'+allocatedWorkers+'">
				//<input type="text" value="'+(allocatedWorkers*value.RATE)+'" name="paybleAmount" id="paybleAmount">
				//<input type="text" value="'+(allocatedWorkers*value.RATE)+'" name="actualPaybleAmount" id="actualPaybleAmount">
			
			 						
			    strAbstractTableData+='<tr style="background-color: #ccc;">';
				strAbstractTableData+='<td class="text-center"></td> 	';
				strAbstractTableData+='<td class="text-center">Total(A)</td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"><span id="TotalACBQty"></span></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"><span id="TotalACBAmt"></span></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"><span id="TotalAPCQty"></span></td>';
				strAbstractTableData+='<td class="text-center"><span id="TotalAPCAmt"></span></td>';
				strAbstractTableData+='<td class="text-center"><span id="TotalACCQty"></span></td>';
				strAbstractTableData+='<td class="text-center"><span id="TotalACCAmt"></span><input type="hidden" id="CertifiedAmount" name="CertifiedAmount"> </td>';			
				strAbstractTableData+='</tr>';
				
				
				strAbstractTableData+='<tr>';
				strAbstractTableData+='<td class="text-center"></td> 	';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';			
				strAbstractTableData+='</tr>';
				
				strAbstractTableData+='<tr>';
				strAbstractTableData+='<td class="text-center"></td> 	';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';			
				strAbstractTableData+='</tr>';

	
				strAbstractTableData+='<tr>';
				strAbstractTableData+='<td class="text-center"></td> 	';
				strAbstractTableData+='<td class="text-center"><span>Recovery(B)</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td class="text-center"><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"><input type="text"  id="currentRecoveryAmount" name="currentRecoveryAmount" class="raDeductionAmt" value=""  style="border:none;width:100%;text-align:center;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount"> <br><a class="" href="#" data-toggle="modal" data-target="#modal-recovery-click" id="recovery">Click Here</a></td>';
				strAbstractTableData+='</tr>';
				
				strAbstractTableData+='<tr>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';
				strAbstractTableData+='<td class="text-center"></td>';			
				strAbstractTableData+='</tr>';
				
				
				strAbstractTableData+='<tr style="background-color: #ccc;">';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><h5><strong></strong></h5></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong>Net Payable Amount (A - B)</td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong></strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong></strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong></strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  id="CBTotalQty">0</strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  id="CBToatalAmount">0</strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong  id="PCTotalQty">0</strong></td>';
				strAbstractTableData+=' <td style="text-align:center;padding:5px;"><strong  id="PCToatalAmount" >0</strong></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong id="CCTotalQty">0</strong> <input type="hidden"  name="actualPaybleAmount" id="actualPaybleAmount"></td>';
				strAbstractTableData+='<td style="text-align:center;padding:5px;"><strong id="CCToatalAmount">0</strong><input type="hidden" name="paybleAmount" id="paybleAmount"> </td>';				       
				strAbstractTableData+='</tr>';
				
				strAbstractTableData+='<tr>';
				strAbstractTableData+='<td class="text-center" colspan="5" style="padding:13px;">Amount in Words</td>';
				strAbstractTableData+='<td class="text-center" colspan="6" style="padding:13px;" id="amountInWords"></td>';								
				strAbstractTableData+='</tr>';
				
				strAbstractTableData+='<tr class="hideinPrint">';
				strAbstractTableData+='<td class="text-center" colspan="5" style="padding:13px;"><strong></strong></td>';
				strAbstractTableData+='<td class="text-center" colspan="6" style="padding:13px;"><strong><a href="javascript:void(0);" onclick="openAbstract()" id="NmrAbstract">NMR Details</a></strong></td>';
				strAbstractTableData+='</tr>'; 
				
			
				
			/* 	strAbstractTableData+=' <tr>';
				strAbstractTableData+=' <td colspan="2" style="text-align:center;padding:13px;width:20%;"><p style="margin-top:35px;"></p><strong>QS</strong></td>';
				strAbstractTableData+='  <td colspan="2" style="text-align:center;padding:13px;width:20%;"><p style="margin-top:35px;"></p><strong>DM-QS</strong></td>';
      			strAbstractTableData+='  <td colspan="2" style="text-align:center;padding:13px;width:20%;"><p style="margin-top:35px;"></p><strong>Block Incharge</strong></td>';
				strAbstractTableData+='  <td colspan="2" style="text-align:center;padding:13px;width:20%;"><p style="margin-top:35px;"></p><strong>AGM</strong></td>';
				strAbstractTableData+='  <td colspan="3" style="text-align:center;padding:13px;width:20%;"><p style="margin-top:35px;"></p><strong>Project Incharge</strong></td>';
				strAbstractTableData+=' </tr>'; */
				
			/* 	strAbstractTableData+=' <tr>';
				strAbstractTableData+=' <td colspan="2" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>QS</strong></td>';
				strAbstractTableData+='  <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>Sr.QS</strong></td>';
      			strAbstractTableData+='  <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>AGM/PM</strong></td>';
				strAbstractTableData+='  <td colspan="3" style="text-align:center;padding:13px;width:25%;"><p style="margin-top:35px;"></p><strong>Project Incharge</strong></td>';
				strAbstractTableData+=' </tr>'; */
				
				
				$("#NMRAbstractTableData").append(strAbstractTableData);
				
				//calling recovery Methods
		//		 loadRARecovery();
				 
				if (typeof(Storage) !== "undefined") {
				         //Store
				         sessionStorage.setItem("NMRDATA", data);
				         sessionStorage.setItem("workDescNames", tempworkDescNames);
				         sessionStorage.setItem("minorHeadNames", minorHeadNames);
				         
				} else {
				           alert("Sorry, your browser does not support Web Storage...");
				}
				//**********************************************NMR Main Screen Data completed
				
				
		 	    			 	    
		 	    //************************************************NMR Abstract Table Head Data***************************
				  
					serialNumber=1;
					
				
							
					 
				
				
					strFirstRowOfTableHead+="<tr>";
					 strSecondRowOfTableHead+="<tr>";
				 
					 //Table Head
					strFirstRowOfTableHead+='<th rowspan="2" style="background-color:#ccc;border:1px solid #000;">S.No</th>';
					strFirstRowOfTableHead+='<th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Date</th>';
					strFirstRowOfTableHead+=' <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Description</th>';
					strFirstRowOfTableHead+='<th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Manual Description</th>';
					strFirstRowOfTableHead+=' <th colspan="2" style="background-color:#ccc;border:1px solid #000;">Duration</th>';
					strFirstRowOfTableHead+=' <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">No of Hours</th>';
							 
					 //Table Head
					 strSecondRowOfTableHead+='<th style="background-color:#ccc;border:1px solid #000;">From</th>';
					 strSecondRowOfTableHead+='<th style="background-color:#ccc;border:1px solid #000;">To</th>';
		           
					 var lengthOfRows=0;
					 lengthOfRows=data.length;
			
				        var strTempFirstRowData="";
						var strTempSecondRowData="";
					
						for (var index = 0; index < tempWDName.length; index++) {
							tempRows++;
							 strFirstRowOfTableHead+='<th rowspan="2" style="background-color:#ccc;border:1px solid #000;">'+tempWDName[index]+' </th>';
							 strSecondRowOfTableHead+='<th style="background-color:#ccc;border:1px solid #000;">'+tempWDName[index]+'(hrs)</th>';
						}
				
				
						 strFirstRowOfTableHead+=' <th colspan='+tempRows+' style="background-color:#ccc;border:1px solid #000;">Total</th>';
						 strFirstRowOfTableHead+='  <th rowspan="2" style="background-color:#ccc;border:1px solid #000;">Remarks</th>';
						 //if the statusOfPage variable value is true means this is the only show page
						
						 strFirstRowOfTableHead+="</tr>";
						 strSecondRowOfTableHead+='</tr>';
					
				$("#NMRHeadData").append(strFirstRowOfTableHead+strSecondRowOfTableHead);
				
			//************************************************NMR Abstract Table Head Data***************************
			
			
			
			
			var minorHeadId="";
			var workDate="";
			tempWorkDeskName="";
			//*************************************************NMR Abstract Table Body******************************
			//table Row Data 
			
			var workDescripition=new Array();
			
			var CheckingDupliWD_MH=new Array();
			var anothersendingWD_MH=new Array();
				//Table Data Started
		$.each(data,function(key,value){
					
			const needle = value.WO_MINORHEAD_ID+"@@"+value.NMRROWNUM;
			const isInArray = 	minorHeadNameAndDate.includes(needle);
			
			const checkWD=value.WO_WORK_DESCRIPTION+"@@"+value.WORK_DATE;
			//Avoid duplicate entry's  of Major Head and Minor Head's
			if(CheckingDupliWD_MH.includes(needle)==false){
				anothersendingWD_MH.push(value.WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC+"@@"+value.WORK_DATE+"@@"+value.NMRROWNUM);
				CheckingDupliWD_MH.push(needle);
			}
			//cheking is current record is exist in minorHeadNameAndDate
			if(isInArray==false &&(value.ALLOCATED_QTY!=undefined||value.ALLOCATED_QTY!=null)){
						minorHeadNameAndDate.push(needle);
					
					let WO_WORK_DESCRIPTION=	value.WO_WORK_DESCRIPTION.replace(/ /g,'');
					 try {
						 WO_WORK_DESCRIPTION=	WO_WORK_DESCRIPTION.trim();
							
						var regExpr = /[^a-zA-Z0-9-. ]/g;
						WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
						
					} catch (e) {
						console.log(e);
					}
					
									
					strNMRFirstRowtbodyData+='<tr id="tablerow'+(serialNumber)+'" class="tablerowcls">';
					strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;">'+(serialNumber)+'</td>';
					strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="Date'+serialNumber+'" >'+value.WORK_DATE+'</td>';
					strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;" id="Description'+serialNumber+'">'+ value.WO_MINORHEAD_DESC	+ '('+value.WO_ROW_CODE+')</td> <input type="hidden"  class="for Description" id="Description'+serialNumber+'"  value="'+value.WO_MINORHEAD_DESC+'@@'+value.WO_MINORHEAD_ID+'">';
					strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="ManuvalDescription'+serialNumber+'"></td> ';
				
					
					
					minorHeadId=value.WO_MINORHEAD_ID;
					workDate=value.WORK_DATE;
					tempWorkDeskName="";
					var tempRowsOFTD=0;
					var tempMinorHeadName="";
					
					
						var manualDescription=value.MANUAL_DESC==null?"":value.MANUAL_DESC;
						workDescripition.push(manualDescription+"@@"+(value.REMARKS==null?"":value.REMARKS));
								
						strNMRFirstRowtbodyData+=' <td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="From'+serialNumber+'"  >'+value.FROM_TIME.toFixed(2)+'</td>';
						strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="To'+serialNumber+'" >'+value.TO_TIME.toFixed(2)+'</td>';
						strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="Noofhours'+serialNumber+'"  >'+value.NO_OF_HOURS.toFixed(2)+'</td>';
					
						
						//this loop is for no of Workers are enter'd while creating NMR Abstract Bill's
						
						var intt=0;
						 for (var index = 0; index < tempworkDescNames.length; index++) {
								var str=tempworkDescNames[index].split("@@");
								if(value.WO_MINORHEAD_ID==minorHeadId&&workDate==value.WORK_DATE){
									 tempWorkDeskName=value.WO_WORK_DESCRIPTION;
									 WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
									 var regExpr = /[^a-zA-Z0-9-. ]/g;
								  WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");
							  strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;" id="'+(WO_WORK_DESCRIPTION+serialNumber)+'">0.00</td>';
	 						}
					} 

							tempWorkDeskName="";
							tempRowsOFTD=0;	
							
						//this loop is for No Of Hours 
						for (var index = 0; index < tempworkDescNames.length; index++) {
							var str=tempworkDescNames[index].split("@@");
							if(value.WO_MINORHEAD_ID==minorHeadId&&workDate==value.WORK_DATE){
								tempWorkDeskName=value.WO_WORK_DESCRIPTION;
								WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
								var regExpr = /[^a-zA-Z0-9-. ]/g;
								  WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
								  strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="'+serialNumber+(WO_WORK_DESCRIPTION+serialNumber+serialNumber)+'" >0.00</td>';										
							}
						}
						strNMRFirstRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  id="Remarks'+serialNumber+'" >rem</td>';
		
						strNMRFirstRowtbodyData+='</tr>';
						serialNumber++;
					}//if condition
				});
			
				
				
		$("#noOfRowsToIterate").val(minorHeadNameAndDate.length);
		//*************************************************NMR Abstract Table Body******************************
			
		
		
		
		//*************************************************NMR Abstract Table Body******************************
				$("#NMRBillstableIdfirstRow").html(strNMRFirstRowtbodyData);
				
				
				//this loop is using for special chars
				for (var index = 0; index < workDescripition.length; index++) {
					var tempstr=workDescripition[index].split("@@");
					$("#ManuvalDescription"+(index+1)).text(tempstr[0]);
					$("#Remarks"+(index+1)).text(tempstr[1]);
					$("#Remarks"+(index+1)).attr("title",tempstr[1]);
				}
		
		
			    strNMRSecondRowtbodyData+=' <tr>';
			    strNMRSecondRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  colspan="'+(7+tempRows)+'">Total Number of Hours</td>';
			
		 
			    strNMRThirdRowtbodyData+=' <tr>';
			    strNMRThirdRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"  colspan="'+(7+tempRows)+'">Total Labor</td>';
			

				var lengthOfRows=0;
				lengthOfRows=data.length;
				//adding empty td's in NMR Abstract Body
			    for (var index = 1; index <= tempRows; index++) {
			   
				}
			
			    tempWorkDeskName="";
				//this loop is for no of Hour's to sum  
					for (var index = 0; index < tempworkDescNames.length; index++) {
						var str=tempworkDescNames[index].split("@@");
							
							WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
							var regExpr = /[^a-zA-Z0-9-. ]/g;
							WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
						strNMRSecondRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;" id="'+WO_WORK_DESCRIPTION+'finalHours">0.00</td>';
						strNMRThirdRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;background-color:#ccc;" id="'+WO_WORK_DESCRIPTION+'Days">0.00</td>';
				}
			
				
			    strNMRSecondRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>';
			    //if this is not the status page then only print the td
			   
			    strNMRSecondRowtbodyData+='</tr>';
			    strNMRThirdRowtbodyData+='<td style="border:1px solid #000;padding-top:3px;padding-bottom:3px;text-align:center;"></td>';
				
			    //if this is not the status page then only print the td
				
			    strNMRThirdRowtbodyData+='</tr>';
			   
				$("#NMRBillstableIdfirstRow").append(strNMRSecondRowtbodyData);
				$("#NMRBillstableIdfirstRow").append(strNMRThirdRowtbodyData);
				
				
				/* debugger;
				var tempcolspan=tempRows*tempRows==1?2:tempRows*tempRows;
				
					var strNMREmpSignRowtbodyData="";
			        strNMREmpSignRowtbodyData+='   <tr id="SumadhuraEmpSign"> '
					strNMREmpSignRowtbodyData+='   <td colspan="'+(8+(tempcolspan))+'" style="border:1px solid #000;padding:5px;">';
					strNMREmpSignRowtbodyData+='<div style="width:100%;margin-top:50px;"></div>';
					
					strNMREmpSignRowtbodyData+='<div style="width:25%;float:left;text-align:center;"><strong>QS</strong>  </div>';
					strNMREmpSignRowtbodyData+='<div style="width:25%;float:left;text-align:center;"><strong>Sr.QS</strong>  </div>';
					strNMREmpSignRowtbodyData+='<div style="width:25%;float:left;text-align:center;"> <strong>AGM/PM</strong> </div>';
					strNMREmpSignRowtbodyData+='<div style="width:25%;float:left;text-align:center;"><strong> Project Incharge</strong> </div>';
					strNMREmpSignRowtbodyData+='</div>';
					strNMREmpSignRowtbodyData+='</td>';
					strNMREmpSignRowtbodyData+='</tr>';
				
					$("#NMRBillstableIdfirstRow").append(strNMREmpSignRowtbodyData); */
				
				
				
				serialNumber=1;
				
				var len=tempWDName.length;
				var increseIndex=0;
				//this is for calculation
				var tempWorkDeskNameForCheck=new Array();
				minorHeadNameAndDate=new Array();

					console.log(" minorHeadNameAndDate "+minorHeadNameAndDate);
					console.log(" tempWDName "+tempWDName);
					var tempBillNo=$("#tempBillNo").val();
				
			var intt=0;	
			//var m=anothersendingWD_MH;
			var index=0;
			

			function myStopFunction() {
			    clearInterval(myVar);
			}
			 var intt=0;
			debugger;
			/* $("#HelperGradeIDays").text('0') */
			 for (var index = 0; index < anothersendingWD_MH.length; index++) {
				 debugger;
				var array_element = anothersendingWD_MH[index];
				 var str=anothersendingWD_MH[index].split("@@");
					var	 minorHeadId=str[2];
					var	workDeskId=str[1];
					var workDate=str[4];
					var rowNum=str[5];
					//anothersendingWD_MH.push(value.WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC+"@@"+value.WORK_DATE+"@@"+value.NMRROWNUM);
					intt++;
					
					$.each(data,function(key,value){
						debugger;
						if(value.WO_MINORHEAD_ID==minorHeadId&&value.WORK_DATE==workDate&&value.NMRROWNUM==rowNum){
							WO_WORK_DESCRIPTION=value.WO_WORK_DESCRIPTION.replace(/ /g,'');
							 var regExpr = /[^a-zA-Z0-9-. ]/g;
					 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");  
					 		var allocatedQty=value.ALLOCATED_QTY==null?0:parseFloat(value.ALLOCATED_QTY);
					 		var noOfHrs=value.NO_OF_HOURS==""?0:parseFloat(value.NO_OF_HOURS);
							$("#"+WO_WORK_DESCRIPTION+(intt)).text(allocatedQty.toFixed(2));
							$("#"+intt+WO_WORK_DESCRIPTION+intt+intt).text(((value.ALLOCATED_QTY*noOfHrs).toFixed(2)));
							$("#actual"+WO_WORK_DESCRIPTION+intt).text(value.ALLOCATED_QTY);
							$("#minorWDId"+WO_WORK_DESCRIPTION+intt).text(value.WO_MINORHEAD_ID+"@@"+value.WO_WORK_DESC_ID);
							
							calculateData("form-control "+value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION,WO_WORK_DESCRIPTION+(intt),value.ALLOCATED_QTY,(intt));
						/* 	var Quantity=(value.ALLOCATED_QTY*noOfHrs)/8;
							$("#HelperGradeIDays").text(parseFloat($("#HelperGradeIDays").text()+Quantity).toFixed(2)); */
						}
					});
			}
			
			 convertToCombo();
			 $(".Description" ).each(function() {
					//
					debugger;

					 var current=$(this);
					  var currentId=current.attr('id');
					  var splitId=currentId.split("Description")[1];
					 var currentValue=current.val();
					 try {
						 currentValue=currentValue.split("@@")[1];
						 //CurrentCompareDescription=CurrentCompareDescription.trim();
					} catch (e) {
						// TODO: handle exception
					}	
					var noOfHours=$("#Noofhours"+splitId).val();
					
					var tect=0;
				for ( var j = 0; j < tempworkDescNames.length; j++) {
					//debugger;
						var str=tempworkDescNames[j].split("@@");
					 	var regExpr = /[^a-zA-Z0-9-. ]/g;
					 
				 		WO_WORK_DESCRIPTION=str[0].replace(regExpr,"");  
				 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(/ /g, '');
						var NoOfPersons=$("#"+WO_WORK_DESCRIPTION+splitId).val();
						var calPersons=(noOfHours*NoOfPersons)/8;
						var str1=currentValue+WO_WORK_DESCRIPTION;
						tect=$( "#"+str1+"Qty" ).text();
						var Rate=$("#"+str1+"Rate").text();
						var tempPersons=tect==""?0:parseFloat(tect);
						$("#"+str1+"Qty").text((calPersons+tempPersons).toFixed(2));		
						$("#"+str1+"Amount").text((parseFloat($("#"+str1+"Qty").text()).toFixed(2)*Rate).toFixed(2));			
						$("#CB"+str1+"Qty").text(($("#PC"+str1+"Qty").text()==""?0:parseFloat($("#PC"+str1+"Qty").text())+parseFloat($("#"+str1+"Qty").text()==''?0:$("#"+str1+"Qty").text())).toFixed(2));		
						$("#CB"+str1+"Amount").text(($("#PC"+str1+"Amount").text()==""?0:parseFloat($("#PC"+str1+"Amount").text())+parseFloat($("#"+str1+"Qty").text()*Rate)).toFixed(2));
					
					}
				});
			
				
				$(".overlay_ims").hide();	
				$(".loader-sumadhura").hide();
				
				CalculateTotalAbstract();
				//Datepush();
				$("#TotalACBAmt1").text($("#TotalACBAmt").text());
				$("#totalCc").text($("#TotalACCAmt").text());
				//$("#TotalAPCAmt1").text($("#TotalAPCAmt").text());
				var amountInWords=	convertNumberToWords($("#CCToatalAmount").text());
				$("#amountInWords").text(amountInWords);
				//*************************************************NMR Abstract Table Body******************************
		
			}//success function
		});//Ajax Call Completed
	
 		
 	} 
 	function calculateData(current, id, value, serialno){
 		var currentclass=current.split("form-control ")[1];
 		var Id=id;
 		var splitId=Id.split(/([0-9]+)/)[0];
 		var currentdata=value;

 		var Date=$("#Date"+serialno).text();
 		var Description=$("#Description"+serialno).text();
 		var From=$("#From"+serialno).text();
 		var To=$("#To"+serialno).text();
 		var Noofhours=$("#Noofhours"+serialno).text();
 	
 		var currentcalQty=0;

 		var TotalQtyCal=0;

 		try{
 			currentclass=currentclass.trim();
 			TotalQtyCal=parseFloat($("#"+currentclass+"TotalQty").text());	
 		}catch(e){
 			
 		}
		$("."+currentclass).each(function(){
 			var id=$(this).attr("id").match(/\d+/g);
 			currentcalQty+=parseFloat(($(this).val()*$("#Noofhours"+id).val())/8);
 		})

 		var previousCumcalQty=parseFloat($("#PC"+currentclass+"Qty").text());

 		currentdata=$("#"+id).val();
 		var assignvalu=Noofhours*currentdata;
 		var asignedId=splitId+serialno;
 		var assignedId=serialno+splitId+serialno+serialno;
 		$("#"+assignedId).val(assignvalu.toFixed(2));
 		var tablelength=$("#NMRBillstableId > tbody > tr").length;	
 		var totalvalamount=0;
 		$("#"+splitId+"Days").text('0');
 		//calculate description values
 		$(".tablerowcls" ).each(function() {
 			debugger;
 			var rowId=$(this).attr("id");
 			var res = rowId.split("tablerow")[1];
 			var totalval=parseFloat($("#"+splitId+res).text()==""?0:$("#"+splitId+res).text());
 			if(totalval=="" || totalval===NaN){
 				totalval=0;
 			}
 			var NoOfHours=$("#Noofhours"+res).text();
 			if(NoOfHours=='' || NoOfHours===NaN){
 				NoOfHours=0;
 			}
 			var totalNumberofHours=totalval*NoOfHours;
 			totalvalamount+=totalNumberofHours;
 			var calDays=totalNumberofHours/8;
 			
 			calDays=parseFloat(calDays);
 			var tempCalDays=calDays.toFixed(2);
 			tempCalDays=parseFloat(tempCalDays);
 			var tempDays=parseFloat($("#"+splitId+"Days").text());
 			//$("#"+splitId+"Days").text((tempCalDays+tempDays).toFixed(2));
 		});
 
 		$("#"+splitId+"finalHours").text(totalvalamount.toFixed(2));
 		var calDays=totalvalamount/8;
 		var getRate=$("#"+splitId+"Rate").text();
 		var calQtyRate=calDays*getRate;
 		$("#"+splitId+"Days").text(calDays.toFixed(3));
 	}
 	
	</script>
</body>
</html>
