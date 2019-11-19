<%@page import="oracle.net.aso.a"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.WorkOrderBean"%>
<%@page import="java.util.List"%>
<%@page import = "java.util.ResourceBundle" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency"); 
String amountInCurrency=resource.getString("label.amountInCurrency"); 

String work_order_print_version_No=resource.getString("work_order_print_version_No");
String work_order_print_reference_No=resource.getString("work_order_print_reference_No");
String work_order_print_issue_date=resource.getString("work_order_print_issue_date");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <title>Print Work Order</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">

		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<script src="//code.jquery.com/jquery.min.js"></script>
		<script src="jquery.stickytable.min.js"></script>
		  		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
	
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="./../CacheClear.jsp" /> 
	
		 	<style>
		 	
	table.dataTable {border-collapse: collapse !important;}
		.tbl-td{
		border-top:1px solid #fff !important;		
		padding:3px;
		}
		.tbl-td-right{
		border-right:1px solid #fff !important;
		}
		.tbl-td-bottom{
		border-bottom:1px solid #fff !important;
		padding:5px;
		}
	.conatianer1{
	    float: left;
	}
	 body{
  font-weight:600;
  }
    table, th, td , tbody {
    border: 1px solid #000 !important;
    border-collapse: collapse;
}
thead{
background-color:#ccc;
}
.page-body{
  padding: 20px;
}
.tbl-border-top{
  border-bottom: none !important;
  margin-top:-1px;
}
.tbl-border-top{

  margin-bottom:0px;
}
.tbl-border-top tr .td-top{
  border-top:none !important;
}
.vertical-alignment { 
  vertical-align: middle !important;
}
.bck-ground{
background-color:#ccc;
}
.td-active{
background-color:#dbecf6;
}
.td-active input{
background-color:#dbecf6;
}
.td-background{
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
.tbl-corner-right{
height:73px;
margin-right:-16px;
width: 300px;
text-align: center;
}

@media print {
.btn-warning{
display:none;
}
.breadcrumb{
display:none;
}
.left_col{
display:none;
}
.nav_menu{
display:none;
}
thead {
    display: table-row-group;
}
.img-workorder-logo{
margin-top:5px;
}
.head-logo-workorder{
width:350px;
float:left;
}
.tbl-corner-right{
height:90px;
margin-right:-16px;
width: 250px;
text-align: center;
}
.border-print{
border-top:1px solid #000;
}
.print-tblheight{height:80px;}
/* #printMinusTable{margin-top: -100px !important;} */
}
@media screen and (min-width: 320px) and (max-width: 777px) {
	.companyLogoImage{
		width: 50px !important;
		height: 50px !important;;
	}
	.borderrightinmedia{
		height: 141px !important;;
	}
/* 	$('body').css({
		"-webkit-transform": "rotate(90deg)"
	}); */
}

	</style>
		
</head>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
</noscript>
<script>
    if ( window.history.replaceState ) {
        window.history.replaceState( null, null, window.location.href );
    }
    if (typeof(Storage) !== "undefined") {
    	debugger;
        sessionStorage.setItem("${UserId}tempRowsIncre12",2);
     } else {
        alert("Sorry, your browser does not support Web Storage...");
     };
</script>

<div class="container body" id="mainDivId">
	<div class="main_container" id="main_container">
				<c:if test="${requestFromMail ne 'true' }">
				<div class="col-md-3 left_col" id="left_col">
					<div class="left_col scroll-view">
             
						<div class="clearfix"></div>

						<jsp:include page="./../SideMenu.jsp" />  
						
					</div>
					</div>
					
						<jsp:include page="./../TopMenu.jsp" /> 
				</c:if>
<div class="right_col" role="main">
					<div id="workOrderPrintPageHeading">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Work Order</a></li>
							<li class="breadcrumb-item active">Print Work Order</li>
						</ol>
					</div>
					
					<div >
					<div class="col-md-12" style="margin-bottom:20px" id="backButton">
						<a  href="javascript:void(0);" onclick="closeView();" class="btn btn-warning btn-xs"><i class="fa fa-arrow-left"> Back</i></a>
					</div>
					 <table style="border:1px solid #000 !important;width:100%;" class="Mrgtop20" id="printMinusTable">
					   <tbody>
					    <tr style="border:1px solid #000 !important;">
					      <td style="border:1px solid #000 !important;">
					      <div style="width:15%;float: left;text-align:center;padding:10px;border-right:1px solid #000;" class="borderrightinmedia">
					        <img src="images/SumadhuraLogo2015.png" class="companyLogoImage"/>
					       </div>
					       <div style="width:55%;text-align:center;float:left;height:80px;padding-top: 2%;" class="print-tblheight">
					       <h5><strong>SUMADHURA INFRACON (P) LTD</strong></h5>
					         <strong> 
					   			  <c:forEach items="${SiteAddress }" var="siteAddr">
									<p> <strong> ${siteAddr}</strong></p>
								  </c:forEach>
							</strong>
							</div>
					        
					       <div style="width:30%;float:right;margin-right:0px;margin-top:1px;margin-bottom:1px;height:auto;" class="print-tblheight">
					        <table style="border:1px solid #000;width:100%;height:118px;">
					         <tbody>
					         <tr>
					         <td class="tbl-td">Version No</td>
					         <td class="tbl-td tbl-td-right"><%=work_order_print_version_No %> </td>
					         
					         </tr>
					         <tr>
					          <td class="tbl-td" style="border-bottom: 1px solid #fff;">Issue Date</td>
					           <td class="tbl-td tbl-td-right"><%=work_order_print_issue_date %></td>
					          
					         </tr>
					          <tr>
					          <td class="tbl-td" style="border-bottom:1px solid #fff !important">Reference No</td>
					           <td class="tbl-td-bottom tbl-td-right"><%=work_order_print_reference_No %> </td>
					          
					         </tr>
					         </tbody>
					        </table>
					       </div>
					      </td>
					    </tr>
					    <tr style="border:1px solid #000 !important;border-bottom:1px solid #000 !important;">
					    <td style="border:1px solid #000 !important;border-bottom:1px solid #000 !important;"><h4 class="text-center"><strong><u>WORK ORDER</u></strong></h4></td>
					    </tr>
					    <tr style="border:1px solid #000 !important;"> 
					       <td style="border:1px solid #000 !important;border-bottom:1px solid #000 !important;padding:15px;">
					    
					         <div style="width:50%;float:left;">
				              <h5><strong><u>Contractor Details :</u></strong></h5>
				              <h5><strong>${WorkOrderBean.contractorName} <c:set value="NMR" var="TypeOfWork"></c:set></u></strong></h5>
				             <h5><strong>Address : ${WorkOrderBean.contractorAddress}</strong></h5>
				             <h5><strong>GSTIN No : ${WorkOrderBean.GSTIN}</strong></h5>
				             <h5><strong>PAN No : ${WorkOrderBean.contractorPanNo}</strong></h5> 
				             <h5><strong>Mobile No : ${WorkOrderBean.contractorPhoneNo}</strong></h5>
				             </div>
					          <div style="width:50%;text-align:left;float:right;">
					          <div style="width:40%;float:left;"><h5><strong>Project</strong></h5></div><div style="width:3%;float:left;"><h5>:</h5></div><div style="width:57%;float:left;text-align:left; padding-left: 20px;"><h5><strong>${WorkOrderBean.siteName}</strong></h5></div><div class="clearfix"></div>
				              <div style="width:40%;float:left;"><h5><strong>Work&nbsp;Order/Ref.&nbsp;No&nbsp;</strong></h5></div><div style="width:3%;float:left;"><h5>:</h5></div><div style="width:57%;float:left;text-align:left;padding-left: 20px;"><h5><strong>${WorkOrderBean.workOrderNo}</strong></h5></div><div class="clearfix"></div>
				              <div style="width:40%;float:left;"><h5><strong>Date</strong></h5></div><div style="width:3%;float:left;"><h5>:</h5></div><div style="width:57%;float:left;text-align:left; padding-left: 20px;"><h5><strong>${WorkOrderBean.workOrderDate }</strong></h5></div><div class="clearfix"></div>
					        </div>
					       </td>
					    </tr>
					    <tr style="border:1px solid #000 !important;">
					   <c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
					    	<td style="border:1px solid #000 !important;padding-left:5x;padding-top:15px;padding-bottom:15px;">
					    		<div class="col-md-12">Further to the Quote and Subsequent discussion we had in our Office, We are Pleased to Offer for the Below mentioned Works. The actual quantity shall be measured and Certified by Our Engineer - In - Charge</div>
					    	</td>
					    </c:if>
					    <c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
					    	<td style="border:1px solid #000 !important;padding-left:5x;padding-top:15px;padding-bottom:15px;">
					    		<div class="col-md-12">Labour Supply</div>
					    	</td>
					    </c:if>
					    </tr>
					   
					   </tbody>
					  </table>
					 </div>
					
					 <table style="border:1px solid #000;width:100%;margin-bottom:15px;margin-top:-1px;">
					   <thead>
					    <tr>
					     <th style="font-weight: bold;font-size: 15px;text-align: center;">S.NO</th>
					     <th style="padding:15px;font-weight: bold;font-size: 15px;">Description of Items</th>
					     <th style="padding:15px;font-weight: bold;font-size: 15px;">Unit</th>
					     <th style="padding:15px;font-weight: bold;font-size: 15px;">Quantity</th>
					     <th style="padding:15px;font-weight: bold;font-size: 15px;">Accepted Rate(<%=acceptedRateCurrency%>)</th>
					     <th style="padding:15px;font-weight: bold;font-size: 15px;">Amount(<%=acceptedRateCurrency%>) </th> <%--    <%=amountInCurrency %> --%>
					    </tr>
					   </thead>
					   <tbody>
					  
					    
					    
					    <c:set value="0" var="indexnumber"></c:set>

					   <% char cCapital=65; %>
					   <% char cSmall=97; %>
					   
					   
					   <c:forEach items="${workOrderCreationList}"  var="workOrderDetail">  
					   <c:set value="1" var="num"></c:set>
					   
					   <c:if test="${num==1}">
					   
					   
					   <c:if test="${majorHead ne  workOrderDetail.WO_MajorHead1}">
					   <% cSmall=97; %>
					   </c:if>
					   
					   <!-- Cheking major head is printed or not if not print -->
					   <c:if test="${majorHead ne  workOrderDetail.WO_MajorHead1}"><!--  || minorHead ne workOrderDetail.WO_MinorHead1  -->
						 
						   <c:set value="0" var="indexnumber"></c:set>
						   <c:set value="2" var="num"></c:set> 
							 <c:if test="${majorHead ne  workOrderDetail.WO_MajorHead1}">
							    <tr>
								     <td style="padding:15px;"><strong><%=cCapital++ %>)</strong></td>
								 	 <c:set value="${workOrderDetail.WO_MajorHead1}" var="majorHead"></c:set>
								 	 <c:set value="" var="minorHead"></c:set>
									 <%-- <c:set value="<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>" var="minorHead"></c:set> --%>
								     <td style="padding:15px;"colspan="5"><strong style="font-weight:1000;font-size:16px;">${workOrderDetail.WO_MajorHead1}</strong></td>
							    </tr>
							</c:if>
							
							<!-- //printing minor heads -->
							<c:if test="${minorHead ne workOrderDetail.WO_MinorHead1}">
							 <tr>
					            	<td style="padding:15px;"><strong><%=cSmall++  %>)</strong></td>
								  
								    <%--  <c:set value="${workOrderDetail.WO_MajorHead1}" var="majorHead"></c:set> --%>
									 <%--  <c:set value="<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>" var="minorHead" ></c:set> --%>
									   <c:set value="${workOrderDetail.WO_MinorHead1}" var="minorHead"></c:set>
								     <td style="padding:15px;"colspan="5"><strong style="font-weight:1000;font-size:14px;font-weight: bold;"> <c:out value='${workOrderDetail.WO_MinorHead1}'></c:out></strong></td>
							</tr>
							</c:if>
							
							
							<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
							<!--  cheking here is the previous scope of work order and new scope of work same or not if not same then print -->
							<c:if test="${tempScopeOfWork ne workOrderDetail.wOManualDescription }">
							<!-- cheking in this scope of work is there more than one Scope of work is availble or not  -->
							<c:if test="${fn:contains(workOrderDetail.wOManualDescription, '@@')}">  
							  <tr>
		          	         	<td style="padding:15px;"></td>
						           <td style="padding:15px;"colspan="5"> 
							           <h5><u><strong>Scope Of Work</strong> </u></h5>
							           <!--  every time the serail number starting from 1 -->
							           <%int serialNumber=1; %>      
							           <!-- Storing previous printed Scope of Work in temp variable -->
							 	  		<c:set value="${workOrderDetail.wOManualDescription}" var="tempScopeOfWork"></c:set>
							              <c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}" />
							            <ol>
							              <c:forEach var="scopeWork" items="${scopeOfWorkParts}">
							             <%-- <label><%=serialNumber++ %>) &emsp;</label><c:out value='${scopeWork}'></c:out><br> --%>
							             <li><c:out value='${scopeWork}'></c:out></li>
							            </c:forEach>
							            </ol>
							           </td>
						       </tr>
		 					</c:if>
		 					<!-- Cheking in this Scope of work is @@ symbol is availble or not if then don't print   -->
		 					</c:if>
		 					</c:if>
					    </c:if>
					    <!-- //printing minor heads -->
							<c:if test="${minorHead ne workOrderDetail.WO_MinorHead1}">
							 <tr>
					            	<td style="padding:15px;"><strong><%=cSmall++  %>)</strong></td>
								  
								    <%--  <c:set value="${workOrderDetail.WO_MajorHead1}" var="majorHead"></c:set> --%>
									 <%--  <c:set value="<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>" var="minorHead" ></c:set> --%>
									   <c:set value="${workOrderDetail.WO_MinorHead1}" var="minorHead"></c:set>
								     <td style="padding:15px;"colspan="5"><strong style="font-weight:1000;font-size:14px;font-weight: bold;"> <c:out value='${workOrderDetail.WO_MinorHead1}'></c:out></strong></td>
							</tr>
							</c:if>
					    
					    
					    
					    
					    </c:if>
					    <!-- Printing scope of work  -->
					    <c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
							<c:if test="${tempScopeOfWork ne workOrderDetail.wOManualDescription }">
								<!-- cheking in this scope of work is there more than one Scope of work is availble or not  -->
								<!-- if you want to print (-) then Comment this condition -->
							<c:if test="${fn:contains(workOrderDetail.wOManualDescription, '@@')}">  
							  <tr>
		          	         		  <td style="padding:15px;"></td>
						           <td style="padding:15px;"colspan="5"> 
							           <h5><u><strong>Scope Of Work</strong> </u></h5>
							           <%int serialNumber=1; %>      
							 	  <c:set value="${workOrderDetail.wOManualDescription}" var="tempScopeOfWork"></c:set>
							         <c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}" />
							            <ol>
							              <c:forEach var="scopeWork" items="${scopeOfWorkParts}">
							         	  	 <li><c:out value='${scopeWork}'></c:out></li>
							              </c:forEach>
							            </ol>
							           </td>
						       </tr>
						       </c:if>
		 					</c:if>
		 				</c:if>
					    
					    <!-- Serial number for work description -->
					   <c:set value="${indexnumber+1}" var="indexnumber"></c:set>
						    <tr>
						     <td style="padding:15px;"><strong>${indexnumber }</strong></td>
						     <td style="padding:15px;"><strong><c:out value='${workOrderDetail.WO_Desc1}'/></strong></td>
						     <td style="padding:15px;"><strong>${workOrderDetail.unitsOfMeasurement1}</strong></td>
						     <td style="padding:15px;"><strong  class="workordertotalqty">${workOrderDetail.quantity}</strong></td>
						     <td style="padding:15px;"><strong  class="workorderacceptedRate">${workOrderDetail.totalAmount1/workOrderDetail.quantity}</strong></td><!-- <fmt:formatNumber type = "number"  maxIntegerDigits = "2" value = "${workOrderDetail.totalAmount1/workOrderDetail.quantity}" /> -->
						     <td style="padding:15px;"><strong class="workordertotalamount">${workOrderDetail.totalAmount1}</strong></td>
						    </tr>
					    </c:forEach>
					    
					      <!-- again Serial number from 0 -->
					    <c:set value="0" var="indexnumber"></c:set>
					    <tr>
						     <td style="padding:15px;"><strong></strong></td>
					  	     <td style="padding:15px;text-align:right;"colspan="4" ><b style="font-weight:1000;font-size:16px;">Total Amount</b></td>						   
						     <td style="padding:15px;"><strong id="finalTotalWorkOrderAmount"></strong></td>
					   </tr>
					    <tr>
						     <!-- <td style="padding:15px;"><strong></strong></td> -->
					  	     <td style="padding:15px;text-align:right;"colspan="3" ><b style="font-weight:1000;font-size:16px;">Amount In Words</b></td>						   
						     <td style="padding:15px;" colspan="3"><strong id="finalTotalWorkOrderAmountInWords"></strong></td>
					   </tr>
					    <tr> 
					    <c:set value="0" var="Zero"></c:set>
					    <c:if test="${listTermsAndCondition.size()!=0}">
					     <td style="padding:15px;"colspan="6">
					    	 <h4><strong><u>Terms & Conditions :</u></strong></h4>
					      <c:forEach items="${listTermsAndCondition }" var="termsCondition">
							<c:if test="${not empty termsCondition.TERMS_CONDITION_DESC }">
								 <c:set value="${indexnumber+1}" var="indexnumber"></c:set><!-- for side number  -->
								 <div class="col-md-12 note-table" >${indexnumber} ) &nbsp;&nbsp;${termsCondition.TERMS_CONDITION_DESC}</div>
							</c:if>
						  </c:forEach>
							</td> 
						</c:if>
					   </tr>
					
					   <tr>
					   <td style="padding:15px;"colspan="6">
					   <p></p>
					   <p></p>
					   <p></p>
					   <p></p>
					   <p></p>
					   <c:set value="1" var="countVerifiedEmpNames"></c:set>
					   <c:set value="END" var="workOrderStatus"/>
					   <c:set value="${100/listOfVerifiedEmpNames.size() }" var="dynamicWidth"></c:set>
					   <c:if test="${listOfVerifiedEmpNames.size()==1}">
						<c:set value="${100/2}" var="dynamicWidth"></c:set>
						</c:if>
						
					   <c:forEach items="${listOfVerifiedEmpNames}" var="verifiedEmpNames">
						  <c:choose>
						  	<c:when test="${1 eq listOfVerifiedEmpNames.size() && WorkOrderBean.approverEmpId eq workOrderStatus}">
								  	 <div class="note-table" style="width:100%;float:left;display:inline-block;text-align:center;">
									    <h5>
									      <strong style="text-align: center;">Prepared By / Verified By / Approved By </strong><br><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
									  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATION_DATE }</span><br>
									    </h5>
								    </div>
							</c:when>
						  	<c:when test="${countVerifiedEmpNames eq 1 }">
							  	<div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
								    <h5>
								      <strong style="text-align: center;">Prepared By </strong><br><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
								  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATION_DATE }</span><br>
								    </h5>
							    </div>
						  	</c:when>
						   	<c:when test="${listOfVerifiedEmpNames.size() eq 1&& WorkOrderBean.approverEmpId eq workOrderStatus}">
								  	 <div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
									    <h5>
									      <strong style="text-align: center;">Approved By </strong><br><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
									  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
									  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATION_DATE }</span><br>
									    </h5>
								    </div>
							</c:when>
						  	<c:otherwise>
							  	<div class="note-table" style="width:${dynamicWidth}%;float:left;display:inline-block;text-align:center;">
								    <h5>
								 	  <strong style="text-align: center;">Verified By </strong><br><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_NAME }</span><br>
								  	  <span style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.EMP_DESIGNATION }</span><br>
								  	  <span  style="text-align: center;margin-top: 5px;"> ${verifiedEmpNames.CREATION_DATE }</span><br>
								    </h5>
							    </div>
						  	</c:otherwise>
						  </c:choose>
						
						   <c:set value="${countVerifiedEmpNames+1 }" var="countVerifiedEmpNames"></c:set>
					   </c:forEach>
					  <!--   <div class="note-table" style="width:33.3%;float:left;display:inline-block;"><h5><strong>Project Incharge </strong></h5></div>
					    <div class="text-center" style="width:33.3%;float:center;display:inline-block;"><h5><strong>AGM/PM/SPM </strong></h5></div>
					    <div class="note-table-right" style="width:33.3%;float:right;display:inline-block;"><h5><strong>Contractor</strong></h5></div>
 -->					   
					   </td>
					  </tr>
					   </tbody>
					  </table>
					 <div class="col-md-12 text-center center-block" style="margin: 30px 0px 30px 0px;"><button class="btn btn-warning" onclick="window.print()">Print</button></div>
	</div>
</div>
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script>
		var urlToActive="";
		var isRequestFromStatus="${WorkOrderBean.approverEmpId}";
		var url="${urlForActivateSubModule}";
		debugger;
		//this code is for activating the sub module which was clicked by user
		if(url.length!=0){
			urlToActive=url;
		}else if("${WorkOrderBean.workOrderStatus}"=="openDraftWorkOrders.spring"){
			urlToActive="ViewMyWOStatus.spring";
		}else if(isRequestFromStatus!="END"){
			//if isRequestFromStatus is not equals to END then this request coming from work order status page
			urlToActive="ViewMyWOStatus.spring";
		}else if(isRequestFromStatus=="END"){
			//if isRequestFromStatus is  equals to END then this request coming from view work orders 
			urlToActive="viewMyWorkOrders.spring";
		}
		if("${WorkOrderBean.isCommonApproval}"=="true"){
			urlToActive="viewWOforApproval.spring";
		}
		
		//this code for to active the side menu 
		var referrer=urlToActive;
		$SIDEBAR_MENU.find('a').filter(function () {
	           var urlArray=this.href.split( '/' );
	           for(var i=0;i<urlArray.length;i++){
	        	 if(urlArray[i]==referrer) {
	        		 return this.href;
	        	 }
	           }
	    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
		
			/*code for sidebar menu--*/
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
			});
			
		
	
		function closeView(){
			goBack() ;
		}
		function goBack() {
		    window.history.back();
		}
		//total final work order amount calculation
				
				function calTotalWorkOrderAmount(){
					var totalWorkOrderAmount=0;
					
					$(".workorderacceptedRate").each(function(){
						var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
						$(this).text(inrFormat(currentvalue.toFixed(2)));
					});
					
					$(".workordertotalqty").each(function(){
						var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
						$(this).text(inrFormat(currentvalue.toFixed(2)));
					});
					
					$(".workordertotalamount").each(function(){
						var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
						totalWorkOrderAmount+=currentvalue;
						$(this).text(inrFormat(currentvalue.toFixed(2)));
					})
					$("#finalTotalWorkOrderAmount").text(inrFormat(totalWorkOrderAmount.toFixed(2)));		
					var amountInWords=	convertNumberToWords(totalWorkOrderAmount);
					$("#finalTotalWorkOrderAmountInWords").text(amountInWords);
				}
 				var requestFromMail="${requestFromMail}";
				if(requestFromMail=="true"||requestFromMail==true){
					$("#mainDivId").removeClass("container");
					$("#mainDivId").css({"padding": "100px 50px 50px 100px"});
					$("#workOrderPrintPageHeading").hide();
					$("#backButton").hide();
					if(window.history.length==1){
						$("#closeBtn").hide();
					}
				} 
				calTotalWorkOrderAmount();		
		</script>
</body>
</html>
