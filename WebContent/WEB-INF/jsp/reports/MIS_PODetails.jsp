<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="func"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="com.sumadhura.util.UIProperties"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="../CacheClear.jsp" />
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

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
 .chexkbox_site{ 
  top: 0;
  left: 0;
  height: 25px;
  width: 25px;
  background-color: #eee;
  float:left;
  }
  .chexkbox_siteall{
  top: 0;
  left: 0;
  height: 25px;
  width: 25px;
  background-color: #eee;
  float:left;
  }
  .checkbox_sitelabel{
   padding-left: 15px;
   margin-top: 8px;
   float: left;
   font-size: 15px;
   }
   .error{color:red;font-weight:bold;font-size:20px;text-align:center;}
 .no-padding-left{padding-left:0px !important;}
.no-padding-right{padding-right:0px !important;}
.display_flex{display:inline-flex;word-break: break-word;}
</style>

</head>
<body class="nav-md">
<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal">
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
						<li class="breadcrumb-item active">${MISPODETAILS}</li>
					</ol>
				</div>
                <!-- <div class="loader-ims" style="display:none;">
						<div class="lds-ims">
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
				</div> -->
				<div class="col-md-12">
					<div class="container">
						<p class="text-center"><label class="error "><c:out value="${requestScope['succMessage']}"></c:out>  </label></p>
						<form class="form-horizontal" id="MISPO_Page"  method="POST" > <!-- //-->
							<div class="col-md-12 col-sm-12 border-inwards-box">
							    <!-- <div class="col-md-2 text-left"><h4><strong>PO Date</strong></h4></div> -->
								<div class="col-md-10 col-md-offset-1 col-sm-12">
								  <div class="col-md-6 col-sm-6">
									<div class="form-group">
										<label for="date" class="col-md-5 marginTop5 col-sm-5"><c:out value="${PODate}"></c:out> From :</label>
										<div class="col-md-7 col-sm-7">
											<input type="text" class="form-control" id="ReqDateId1" name="fromDate"  autocomplete="off"">
										</div>
									</div>
								</div>
								<div class="col-md-6 col-sm-6">
									<div class="form-group">
										<label for="todate" class="col-md-5 col-sm-5 marginTop5">To:</label>
										<div class="col-md-7 col-sm-7">
											<input type="text" class="form-control" id="ReqDateId2" name="toDate" autocomplete="off">
										</div>
									</div>
								</div>
								</div>
							</div>
							
							<!-- site details checkboxes -->
							 <div class="col-md-12 col-sm-12 border-inwards-box">
							 <div class="col-md-12"><div class="col-md-12 text-left"><h4><strong>Select site :</strong></h4></div></div>
								<div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class=" chexkbox_siteall"><label class="checkbox_sitelabel"> Select All</label></div></div>
								<div class="col-md-12 text-left">
								<%-- <c:forEach items="${siteDetails}" var="siteDetails"> --%>
								<%
								Map<String, String> siteDetails =(Map<String, String>) request.getAttribute("siteDetails");
								String strEliminatedSitesFromReport =  UIProperties.validateParams.getProperty("ELIMINATE_SITES_FROM_STORE_REPORT"); 
								for(Map.Entry<String, String> retVal : siteDetails.entrySet()) { 
								String strSiteId=retVal.getKey(); 
								String siteName=retVal.getValue();;
									if((!strEliminatedSitesFromReport.contains(strSiteId))){ 
									
								
								%>
								 <%--  <div class="col-md-4 col-xs-12"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="<%=strSiteId%>"><label class="checkbox_sitelabel"><%=siteName%></label></div>   --%>
								  <div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="<%=strSiteId%>"></div><div class="col-md-11"><label class="checkbox_sitelabel"><%=siteName%></label></div></div>
								  
								   <%--  </c:forEach> --%>
								<%}} %>	
							  </div>
							  </div>
							<div class="col-md-12 text-center center-block">
								<button type="button"  id="submitBtnId" class="btn btn-warning" onclick="submitFunction(event)">Submit</button>
							</div>
							<!-- site details checkboxes -->
						<input type="hidden" class="form-control" id="siteNames" name="siteIds"  value="" autocomplete="off"/>
						<input type="hidden" class="form-control" id="noOfSelections" name="noOfSelections"  value="" autocomplete="off"/>	
						<input type="hidden" class="form-control" id="selectedReport" name="selectedReport"  value="${PurchasePoReport}" autocomplete="off"/>
						<input type="hidden" class="form-control" id="error" name="error"  value="${succMessage}" autocomplete="off"/>	
						</form>
						<%-- <%} %> --%>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</form:form>
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
    <script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	
<!-- script for date picker -->
<script>
debugger;
var successmessage="";
$("#err").text(successmessage);
$( function() {
  $( "#ReqDateId1" ).datepicker({
    changeMonth: true,
    changeYear: true,
    dateFormat: 'dd-mm-yy',
    maxDate: 0
  });
  $( "#ReqDateId2" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
	    dateFormat: 'dd-mm-yy',
	    maxDate: 0
	  });
  $( "#ReqDateId1" ).attr('readonly', true);
  $( "#ReqDateId2" ).attr('readonly', true);
  $(".chexkbox_siteall").click(function () {
	     $('.chexkbox_site').not(this).prop('checked', this.checked);
	 });
   
} );

function submitFunction(e) {debugger;
var n=0;
var fromDate = $("#ReqDateId1").val();
var toDate = $("#ReqDateId2").val();

if((fromDate == "" && toDate == "")){
	//$("#ReqDateId1" ).focus();
	alert("Please Select From (Or) To Date.");
	return false;
}
/* else if(toDate == ""){
	$("#ReqDateId2" ).focus();
	alert("Please select to date.");
	return false;
} */
$(".chexkbox_site").each(function(){
	if($(this).prop("checked") == true){
		n++;
	}
})
if(n<1){
	alert("Please select at lease one site ");
	return false;
}
var SiteData=[];
$(".chexkbox_site").each(function(){
	//debugger;
	if($(this).prop("checked") == true){
	var currentSite=$(this).val();
	SiteData.push(currentSite);
	}
});
console.log("SiteData: "+SiteData);
console.log("SiteData: "+SiteData.length);
$('#siteNames').val(SiteData);
$('#noOfSelections').val(SiteData.length);
debugger;
var slectedReport=$("#selectedReport").val();

 if(slectedReport=="SiteLevelPoReport"){
	 $("#ProductWiseIndentsFormId").attr("action", "MISSieLevelPoPage.spring");
 }if(slectedReport=="PurchasePoReport"){
	 $("#ProductWiseIndentsFormId").attr("action", "MISPoPage.spring");
 }if(slectedReport=="MarketPurchase"){
	 $("#ProductWiseIndentsFormId").attr("action", "MISMarketPurchasePage.spring");
 }if(slectedReport=="LocalPurchase"){
	 $("#ProductWiseIndentsFormId").attr("action", "MISLocalPurchasePage.spring");
 }if(slectedReport=="VendorPayment"){
	 $("#ProductWiseIndentsFormId").attr("action", "MISVendorPaymentPage.spring");
 }
 
	
	$("#ProductWiseIndentsFormId").attr("method", "POST"); 
	document.getElementById("ProductWiseIndentsFormId").submit();
}

</script>
</body>
</html>
