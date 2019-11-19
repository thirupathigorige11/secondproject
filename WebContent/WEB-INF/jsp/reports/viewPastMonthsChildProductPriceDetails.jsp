<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.sumadhura.dto.PriceMasterDTO"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>


<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="../CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.form-control{height:34px;}
form-horizontal .control-label{text-align:center !important;}
.tbl-inner-td{
padding:0px !important;

}
.tbl-inner-td>table>tbody>tr>th{border:1px solid #000 !important;}
#tblnotification thead tr th{font-weight:700;font-size:17px;}
@media only screen and (min-width:320px) and (max-width:425px){
.tbl-inner-td>table>tbody>tr>th{border:1px solid #000 !important;white-space:normal;}
.table-responsive>.table-bordered>tbody>tr>th>label{width: 222px !important;white-space: normal;}
}

@media only screen and (min-width:320px) and (max-width:767px){
.table-responsive>.table-bordered>thead>tr>th:first-child {border-left:1px solid #000;}
.table-responsive>.table-bordered>thead>tr>th:last-child {border-right: 1px solid #000;}
.form-horizontal .control-label{text-align:left !important;}
}
.success,.error {
	text-align: center;
	font-size: 16px;
}
.input-group-addon {
border: 1px solid #ccc !important;
}
.childproductTdata{text-align:center;border: 1px solid;max-width:350px;}
@media only screen and (min-width:320px) and (max-width:767px){
.childproductTdata{text-align:center;width:450px !important; word-break:break-word !important;max-width:450px;white-space: unset !important;}
.table-responsive>.table-bordered>thead>tr>th:first-child {border-left: 1px solid #000;}
.table-responsive>.table-bordered>thead>tr>th:last-child {border-right: 1px solid #000;}
}
.table-responsive>.table-bordered>thead>tr>th:first-child {border-left: 1px solid #000;}
.table-responsive>.table-bordered>thead>tr>th:last-child {border-right: 1px solid #000;}
}

</style>
<script type="text/javascript">
	function validate() {		
			var siteId=document.getElementById("dropdown_SiteId").value;
			var childProdName=document.getElementById("childProdName").value;
			 var siteName=$('#dropdown_SiteId').find('option:selected').text();
			 $("#SITE_NAME").val(siteName);
			if((childProdName==""||childProdName==null)&&siteId==""){
				alert("Please enter child product name!");
				document.getElementById("childProdName").focus;
				return false;	
			}
			$(".loader-sumadhura").show();
			return true;	
	}
	function clearText(){
		 $("#childProdName").val("");
		 $(".clearbtn").hide();
		}
</script>
</head>
<body class="nav-md">
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
						<li class="breadcrumb-item"><a href="#">Reports</a></li>
						<li class="breadcrumb-item active">Child Product Details</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="z-index:99;display:none;">
						<div class="lds-facebook">
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>

      <center> <label class="success" style="font-size: 20px; color: red;display: none;" id="successMsg"><c:out value="${requestScope['succMessage']}"></c:out> </label></center> 
	
	
<div class="">
      <%-- <label class="success text-center col-md-12"><c:out value="${requestScope['succMessage']}"></c:out> </label>  --%>
	<form:form  id="indentIssueFormId" action="ViewThreeMonthProductPrice.spring" class="form-horizontal border-inwards-box">
	<!-- getProductDetails.spring -->
			<%
				String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
				log("reposrtd/cumulative Stock");
				if (strSearchType.equals("ADMIN")) {
			%>
				
				<div class="col-md-4">
				  <div class="form-group">
				     <label class="col-md-6 control-label">Site :</label>			
					<div class="col-md-6" id="dropdownSiteId">
					 <input type="hidden" name="SITE_NAME" id="SITE_NAME">
					 <input type="hidden" name="PriceMaster" id="PriceMaster" value="${priceMaster }">
					 <input type="hidden" name="CHILD_PRODUCT_ID" id="childProdId" value="${childProductId }">
						<input type="hidden" name="NAME"  value="<c:out value='${childProdName}'/>">
						<input type="hidden" name="SITE_ID" value="${strSiteId}">
					 <select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control "> 
											
					</select>
			<%
		 		}
		 	%>	
				 </div>
	 		   </div>
			 </div>
			 <div class="col-md-4">
				 <div class="form-group">
					<label class="col-md-6 control-label">Child Product Name :</label>
					
					<div class="col-md-6"> <input id="childProdName" name="childProdName" autocomplete="off" class="form-control" size="20" value="<c:out value='${childProdName}'/>" title="<c:out value='${childProdName}'/>">
						<button  type="button" class="btn clearbtn" onclick="clearText()">x</button>
						
						
					</div>
				 </div>
			</div>
			<%-- <div class="col-md-4">
				  <div class="form-group">
					<label class="col-md-6 control-label">Month:</label>
						 <div class="col-md-6">
						 	  <input type="hidden" name="showingLastThreeMonthsData" id="showingLastThreeMonthsData" value="true">
							  <input type="text" id="monthYear" name = "monthYear" value="${monthYear}" class="form-control"  autocomplete="off" />
						</div>
					</div>
			</div> --%>
			<div class="col-md-4">
					<div class="form-group">
						<label class="col-md-6 control-label col-xs-12">Month:</label>
							 <div class="col-md-6 input-group  col-xs-12">
								  <input type="text" id="monthYear" name = "monthYear" value="${monthYear}" class="form-control" onkeydown="" autocomplete="off" />
							      <label class="input-group-addon btn" for="monthYear">
								  <span class="fa fa-calendar"></span>
						</label>
					  </div>
				   </div>
				</div>
			<div class="col-md-12 text-center center-block">
				<input type="submit" value="Submit" class="btn btn-warning Mrgtop10" id="saveBtnId" onclick="return validate();">
			</div>						
			<div>${displayErrMsg}</div>
	</form:form>
</div>
	
	
	
	
	  <div class="">
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				int rowSpanCol=(Integer)request.getAttribute("rowSpanCol") ;
				%>
			
				<div class="table-responsive" >
				
				<c:set value="0" var="countMonthAvailbleData"></c:set>
				<c:if test="${fn:length(thirdMonth) > 0}">
					<c:set value="${countMonthAvailbleData+1 }" var="countMonthAvailbleData"></c:set>
				</c:if>
				<c:if test="${fn:length(secondMonth) > 0}">
					<c:set value="${countMonthAvailbleData+1 }" var="countMonthAvailbleData"></c:set>
				</c:if>
				<c:if test="${fn:length(firstMonth) > 0}">
					<c:set value="${countMonthAvailbleData+1 }" var="countMonthAvailbleData"></c:set>
				</c:if>
				
				<c:if test="${countMonthAvailbleData==1}">
					<c:set value="1.2" var="countMonthAvailbleData"></c:set>
				</c:if>
				
				<c:set value="${countMonthAvailbleData*1100}" var="dynamicTableWidth"></c:set>
			
			
			<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0" style="width:${dynamicTableWidth}px;">
					<thead>
						<tr>
							<th style="text-align:center;">Child Product Name</th>
		    				<th style="text-align:center;">Measurement</th>
		    				<c:if test="${firstMonth!=null}"><c:if test="${fn:length(firstMonth) > 0}"><th style="text-align:center;">${firstMonth.get(0).month_name}</th></c:if></c:if>
		    				<c:if test="${secondMonth!=null}"><c:if test="${fn:length(secondMonth) > 0}"><th style="text-align:center;">${secondMonth.get(0).month_name }</th></c:if></c:if>
		    				<c:if test="${thirdMonth!=null}"><c:if test="${fn:length(thirdMonth) > 0}"><th style="text-align:center;">${thirdMonth.get(0).month_name}</th></c:if></c:if>
		    			</tr>
					</thead>
				<tbody>
				<tr>
				
				
				
				
				<%-- <c:choose>
					<c:when test="${thirdMonth!=null  && fn:length(thirdMonth) > 0 }"><td style="text-align:center;border: 1px solid;max-width:350px;"> ${thirdMonth.get(0).child_product_name} 	<c:set value="${fn:length(thirdMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set>${dynamicWidthToTd} </td><td style="text-align:center;border: 1px solid;">${thirdMonth.get(0).measurement_name}</td></c:when>
					<c:when test="${secondMonth!=null  && fn:length(secondMonth) > 0 }"><td style="text-align:center;border: 1px solid;max-width:350px;"> ${secondMonth.get(0).child_product_name} <c:set value="${fn:length(secondMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set>${dynamicWidthToTd}</td><td style="text-align:center;border: 1px solid;"> ${secondMonth.get(0).measurement_name} </td></c:when>
					<c:when test="${firstMonth!=null && fn:length(firstMonth) > 0}"><td style="text-align:center;border: 1px solid;max-width:350px;">  ${firstMonth.get(0).child_product_name} <c:set value="${fn:length(firstMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set>${dynamicWidthToTd}</td><td style="text-align:center;border: 1px solid;">${firstMonth.get(0).measurement_name} </td> </c:when>
				</c:choose> --%>
				<c:choose>
				<c:when test="${thirdMonth!=null && fn:length(thirdMonth) > 0 }"><td class="childproductTdata"> ${thirdMonth.get(0).child_product_name}<c:set value="${fn:length(thirdMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set></td><td style="text-align:center;border: 1px solid;">${thirdMonth.get(0).measurement_name}</td></c:when>
				<c:when test="${secondMonth!=null && fn:length(secondMonth) > 0 }"><td class="childproductTdata">${secondMonth.get(0).child_product_name} <c:set value="${fn:length(secondMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set></td><td style="text-align:center;border: 1px solid;"> ${secondMonth.get(0).measurement_name} </td></c:when>
				<c:when test="${firstMonth!=null && fn:length(firstMonth) > 0}"><td class="childproductTdata">${firstMonth.get(0).child_product_name} <c:set value="${fn:length(firstMonth.get(0).child_product_name)}" var="dynamicWidthToTd"></c:set></td><td style="text-align:center;border: 1px solid;">${firstMonth.get(0).measurement_name} </td> </c:when>
				</c:choose>
				
				
				<c:if test="${dynamicWidthToTd>250 }">
					<c:if test="${rowSpanCol==1 }">
					<c:set value="3" var="rowSpanCol"></c:set>
					</c:if>
					<c:if test="${rowSpanCol==2 }">
					<c:set value="3" var="rowSpanCol"></c:set>
					</c:if>
				</c:if>
				
				  <c:if test="${firstMonth!=null}"><c:if test="${fn:length(firstMonth) > 0}">  
				  <td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid #000;">
				
					<table class="tbl_height" border="1" style="width: 100%;height:100%; text-align: center;padding: 0px;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;" >
					  <thead>
					  <tr><th class="text-center" style="height: 30px;">Amount/unit Before Taxes</th><th scope="col" style="height: 30px;" class="text-center"> Amount/Unit After Taxes </th><th  style="height:30px;"class="text-center"> Invoice No/DC No </th><th  class="text-center">Vendor Name</th></tr> 
				     </thead>
				     <tbody>
				    	
				  	 	<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
				  	 	<c:choose>
				  	 	<c:when test="${incre< fn:length(firstMonth)}">
				  	 		<tr style="height: 30px;">
				  	 		<td>${firstMonth.get(incre).amount_per_unit_before_taxes}</td>
				  	 		<td>${firstMonth.get(incre).amount_per_unit_after_taxes }</td>
				  	 		<td><c:choose>
				  	 			<c:when test="${not empty firstMonth.get(incre).dc_number && not empty firstMonth.get(incre).invoice_number}">
										<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${firstMonth.get(incre).invoice_number}&vendorId=${firstMonth.get(incre).vendor_id}&invoiceDate=${firstMonth.get(incre).indent_recive_date}&indentEntryId=${firstMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${firstMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();" >INV_${firstMonth.get(incre).invoice_number}</a>&nbsp;/
										<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${firstMonth.get(incre).dc_number}&vendorId=${firstMonth.get(incre).vendor_id}&dcDate=${firstMonth.get(incre).dc_recive_date}&dcEntryId=${firstMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${firstMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${firstMonth.get(incre).dc_number}</a>
									</c:when>
									<c:when test="${not empty firstMonth.get(incre).invoice_number}">
										<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${firstMonth.get(incre).invoice_number}&vendorId=${firstMonth.get(incre).vendor_id}&invoiceDate=${firstMonth.get(incre).indent_recive_date}&indentEntryId=${firstMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${firstMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${firstMonth.get(incre).invoice_number}</a>
									</c:when>
									<c:when test="${not empty firstMonth.get(incre).dc_number}">
										<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${firstMonth.get(incre).dc_number}&vendorId=${firstMonth.get(incre).vendor_id}&dcDate=${firstMonth.get(incre).dc_recive_date}&dcEntryId=${firstMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${firstMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${firstMonth.get(incre).dc_number}</a>
									</c:when>
									
								</c:choose>
							</td>
							<td>${firstMonth.get(incre).vendor_name}</td>
				  	 		</tr>
				  	 	</c:when>
				  	 	<c:otherwise>
				  	 		<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
				  	 	</c:otherwise>
				  	 	</c:choose>
				  	 	</c:forEach>
				  	 	
				    </tbody>
				    </table>  
				    </td> </c:if>
				 </c:if> <!-- first  -->
			
				
				 <c:if test="${secondMonth!=null}"> <c:if test="${fn:length(secondMonth) > 0}">
				   <td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">
					 <table class="tbl_height" border="1" style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;" >
					     <thead>
						 <tr><th scope="col" style="border:1px solid #000;height:30px;"class="text-center">Amount/unit Before Taxes</th><th scope="col" style="border:1px solid #000;height:30px;" class="text-center">Amount/Unit After Taxes</th><th style="height:30px;"class="text-center"> Invoice No/DC No </th><th  class="text-center">Vendor Name</th></tr>
		                 </thead>
					   	 <tbody>
					    	
					  	 	<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
						  	 	<c:choose>
						  	 		<c:when test="${incre< fn:length(secondMonth)}">
						  	 			<tr  style="height: 30px;">
						  	 				<td>${secondMonth.get(incre).amount_per_unit_before_taxes}</td>
						  	 				<td>${secondMonth.get(incre).amount_per_unit_after_taxes }</td>
						  	 				<td><c:choose>
												<c:when test="${not empty secondMonth.get(incre).dc_number && not empty secondMonth.get(incre).invoice_number}">
													<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${secondMonth.get(incre).invoice_number}&vendorId=${secondMonth.get(incre).vendor_id}&invoiceDate=${secondMonth.get(incre).indent_recive_date}&indentEntryId=${secondMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${secondMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${secondMonth.get(incre).invoice_number}</a>&nbsp;/
													<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${secondMonth.get(incre).dc_number}&vendorId=${secondMonth.get(incre).vendor_id}&dcDate=${secondMonth.get(incre).dc_recive_date}&dcEntryId=${secondMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${secondMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${secondMonth.get(incre).dc_number}</a>
												</c:when>
												<c:when test="${not empty secondMonth.get(incre).invoice_number}">
													<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${secondMonth.get(incre).invoice_number}&vendorId=${secondMonth.get(incre).vendor_id}&invoiceDate=${secondMonth.get(incre).indent_recive_date}&indentEntryId=${secondMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${secondMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${secondMonth.get(incre).invoice_number}</a>
												</c:when>
												<c:when test="${not empty secondMonth.get(incre).dc_number}">
													<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${secondMonth.get(incre).dc_number}&vendorId=${secondMonth.get(incre).vendor_id}&dcDate=${secondMonth.get(incre).dc_recive_date}&dcEntryId=${secondMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${secondMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${secondMonth.get(incre).dc_number}</a>
												</c:when>
											   </c:choose>
											</td>
						  	 					<td>${secondMonth.get(incre).vendor_name}</td>
						  	 			</tr>
						  	 		</c:when>
									<c:otherwise>
						  	 			<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
						  	 		</c:otherwise>
						  	 	</c:choose>
					  	 	</c:forEach>
					  	 
					    	</tbody>
					    
					     </table> 
					</td>
				      </c:if></c:if>
				      
				       <!--Second  -->
				
				 
				 	<c:if test="${thirdMonth!=null}">  <c:if test="${fn:length(thirdMonth) > 0}">
					<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">
					 <table class="tbl_height" border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">
						  <thead>
						  <tr><th class="text-center" style="height:30px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;"class="text-center"> Amount/Unit After Taxes </th><th style="height:30px;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr> 
					      </thead> 
					      <tbody>
						<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
						  	 	<c:choose>
						  	 	<c:when test="${incre< fn:length(thirdMonth)}">
						  	 	<tr style="height: 30px;">
						  	 	<td>${thirdMonth.get(incre).amount_per_unit_before_taxes}</td>
						  	 	<td>${thirdMonth.get(incre).amount_per_unit_after_taxes }</td>
						  	 	<td><c:choose>
							  	 	<c:when test="${not empty thirdMonth.get(incre).dc_number &&not empty thirdMonth.get(incre).invoice_number}">
							  			<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${thirdMonth.get(incre).invoice_number}&vendorId=${thirdMonth.get(incre).vendor_id}&invoiceDate=${thirdMonth.get(incre).indent_recive_date}&indentEntryId=${thirdMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${thirdMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();" >INV_${thirdMonth.get(incre).invoice_number}</a>&nbsp;/
										<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${thirdMonth.get(incre).dc_number}&vendorId=${thirdMonth.get(incre).vendor_id}&dcDate=${thirdMonth.get(incre).dc_recive_date}&dcEntryId=${thirdMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${thirdMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${thirdMonth.get(incre).dc_number}</a>
								 	</c:when>
								 	<c:when test="${not empty thirdMonth.get(incre).invoice_number}">
										<a target="_blank" href="getGrnDetails.spring?invoiceNumber=${thirdMonth.get(incre).invoice_number}&vendorId=${thirdMonth.get(incre).vendor_id}&invoiceDate=${thirdMonth.get(incre).indent_recive_date}&indentEntryId=${thirdMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&siteId=${thirdMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${thirdMonth.get(incre).invoice_number}</a>
									</c:when>
									<c:when test="${not empty thirdMonth.get(incre).dc_number}">
										<a target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${thirdMonth.get(incre).dc_number}&vendorId=${thirdMonth.get(incre).vendor_id}&dcDate=${thirdMonth.get(incre).dc_recive_date}&dcEntryId=${thirdMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${thirdMonth.get(incre).site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${thirdMonth.get(incre).dc_number}</a>
									</c:when>
									</c:choose>
								</td>
												<td>${thirdMonth.get(incre).vendor_name}</td>
						  	 	</tr>
						  	 	</c:when>
						  	 	<c:otherwise>
						  	 	<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
						  	 	</c:otherwise>
						  	 	</c:choose>
						  </c:forEach>
						 </tbody>	
					    </table> 
					   	  </td>
				   </c:if>
				   
				   </c:if> <!-- Third -->
				</tr>
			
				</tbody>
					</table>
				</div>
           <%
				   }
           %>
 
	</table>
				<!-- /page content -->
				</div>
			</div>
		</div>
	</div>
	
	
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/marketing/jquery.ui.monthpicker.js" type="text/javascript"></script> 
	<script src="js/marketing/jquery.timepicker.min.js"></script> 
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
	
	
	function setSiteIdForSelectBox(siteId) {
		if (typeof(Storage) !== "undefined") { 	
		     debugger;
		    sessionStorage.setItem("siteIdAfterRefresh", siteId);
		} else {
		}
	}
	$(document).ready(function(){
		$('#monthYear').monthpicker({changeYear:true,dateFormat:"mm-yy"});
		//code for site id
	 	$("#dropdown_SiteId").on("change",function(){
			 var siteId=$(this).val();
			 var siteName=$('#dropdown_SiteId').find('option:selected').text();
			 if (typeof(Storage) !== "undefined") {
				    // Store   	
				     debugger;
				    sessionStorage.setItem("siteIdAfterRefresh", siteId);
				    sessionStorage.setItem("siteNameAfterRefresh",siteName);
				    // Retrieve
				    //document.getElementById("result").innerHTML = sessionStorage.getItem("siteIdAfterResfres");
				} else {
				  //  document.getElementById("result").innerHTML = "Sorry, your browser does not support Web Storage...";
				}
	}); 
		
	    var siteId="${strSiteId}";
	    var siteName=sessionStorage.getItem("siteNameAfterRefresh",siteName);
	    var siteData="<option value='"+siteId+"'>"+siteName+"</option>";
	    if(siteId.length!=0){
	    	$("#dropdown_SiteId").html(siteData);	
	    }
	    var data="";	 
		   if("${SiteId}"=="996"){
				data+="<option value='996' selected>MARKETING</option>";
				$("#dropdown_SiteId").html(data);		
			}else{
		$.ajax({
			  url : "./loadAllSites.spring",
			  type : "GET",
			 dataType : "json",
			  success : function(resp) {
				var type=$("#PriceMaster").val();
				var siteId=sessionStorage.getItem("siteIdAfterRefresh");
				var data="";	 
				data+="<option value=''></option>";
				 
				$.each(resp,function(index,value){
				debugger;
				
				if(type=="DeptWisePriceMaster"){
					if(value.SITE_ID=="996"){
						return;
					}
				}
				if("${strSiteId}"==value.SITE_ID){
					debugger;
					
					data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
				}else{
					data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
				}
					});  
				sessionStorage.setItem("siteIdAfterRefresh", "000");
				sessionStorage.setItem("siteIdAfterRefresh", "");
				var searchType="${SEARCHTYPE}";
				//if(searchType=="ADMIN"){
					$("#dropdown_SiteId").html(data);		
				//}
			  },
			  error:  function(data, status, er){
				 alert(data+"_"+status+"_"+er);
				  }
			  });  
			}
		debugger;
		var childProductText = $("#childProdName").val().trim();
		if(childProductText == ""){
			$(".clearbtn").hide();
			// false;
		}else{
			$(".clearbtn").show();
			//return false;
		}
		
		$("#childProdName").keyup(function () {
		
			debugger;
			var childProductText = $("#childProdName").val().trim();
			if(childProductText == ""){
				$(".clearbtn").hide();
				return false;
			}
			
			$(".clearbtn").show();
			var str=$(this).val();
		
			var type=$("#PriceMaster").val();
		
		$("#childProdName").autocomplete({
   			source : function(request, response) {
   				$.ajax({
   					  url : "./loadChildProductLatestPriceNames.spring",
   					  type : "GET",
   					  data:{
   						prodName:str,
   						type:type,
   						SITE_ID:siteId,
   						SITE_NAME:siteName
   						
   					  },
	   					dataType : "json",
   						success : function(data) {
   							response($.map(data, function (value, key) { 
	                          debugger;
   								return {
	                                label: value.NAME,
	                                value: value.CHILD_PRODUCT_ID
	                            };
	                       }));
	    				}
	    			});
	    		},
		        change: function (event, ui) {
		               if(!ui.item){
			                    // The item selected from the menu, if any. Otherwise the property is null
			                    //so clear the item for force selection
		                   $("#childProdName").val("");
		                    $(".clearbtn").hide();
		                }
		            },
		            select: function(event, ui) {  
		                $("#childProdID").val(ui.item.value);                  
		                $("#tempchildProdID").val(ui.item.label);
		                $("#childProdName").val(ui.item.label);
		                $("#childProdName").attr("title",ui.item.label);
		                return false;  
		            }  
	    		});
				});
	});
	
	
	$(function() {
		  $("#ReqDateId1").datepicker({dateFormat: 'dd-M-y',maxDate: new Date(),changeMonth: true,
		      changeYear: true});
		  $("#ReqDateId2").datepicker({dateFormat: 'dd-M-y',maxDate: new Date(),changeMonth: true,
		      changeYear: true});
		  return false;
	});
		$(document).ready(
				
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
				//	$('#tblnotification').DataTable();
				});
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		//displaying loader when click on hyper link
		function loader(){
			//$(".loader-sumadhura").show();
		}
		$(window).load(function(){
			var childTdHeight=$(".childproductTdata").height();
			$(".tbl_height").css({
				"height":childTdHeight+20
			})
		})
	</script>

</body>
</html>