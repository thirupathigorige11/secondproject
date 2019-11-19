<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	//Loading Indent Receive Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String otherOrTransportCharges = resource.getString("label.otherOrTransportCharges");
	String taxOnOtherOrTransportCharges = resource.getString("label.taxOnOtherOrTransportCharges");
	String otherOrTransportChargesAfterTax = resource.getString("label.otherOrTransportChargesAfterTax");
	String totalAmount = resource.getString("label.totalAmount");	
	String actions = resource.getString("label.actions");	
	String otherCharges = resource.getString("label.otherCharges");
	String note = resource.getString("label.note");
	String finalAmount = resource.getString("label.finalAmount");
	String calculateBtn = resource.getString("label.calculateBtn");
	String viewGRNBtn = resource.getString("label.viewGRNBtn");
	String saveBtn = resource.getString("label.saveBtn");	
	String expiryDate = resource.getString("label.expiryDate");
	String conveyanceCharges = resource.getString("label.conveyanceCharges");
	String conveyanceAmount = resource.getString("label.conveyanceAmount");
	String trasportInvoice = resource.getString("label.trasportInvoice");
	String GSTAmount = resource.getString("label.GSTAmount");
	String chargesAmountAfterTax = resource.getString("label.chargesAmountAfterTax");
	String conveyanceTax = resource.getString("label.conveyanceTax");
	String sNumber = resource.getString("label.sNumber");
//	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String pageTitle = resource.getString("label.pageTitle");
	String colon = resource.getString("label.colon");
	String invoiceNumber = resource.getString("label.invoiceNumber");
	String invoiceDate = resource.getString("label.invoiceDate");
	String vendorName = resource.getString("label.vendorName");
	String gSTIN = resource.getString("label.gSTIN");
	String vendorAddress = resource.getString("label.vendorAddress");
	String state = resource.getString("label.state");	
	String serialNumber = resource.getString("label.serialNumber");
	String product = resource.getString("label.product");
	String subProduct = resource.getString("label.subProduct");
	String childProduct = resource.getString("label.childProduct");
	String quantity = resource.getString("label.quantity");
	String measurement = resource.getString("label.measurement");	
	String productAvailability = resource.getString("label.productAvailability");
	String price = resource.getString("label.price");
	String basicAmount = resource.getString("label.basicAmount");	
	String discount = resource.getString("label.discount");	
	 String AmountAfterDiscount = resource.getString("label.AmountAfterDiscount");	
	String taxPer = resource.getString("label.tax");
	String hsnCode = resource.getString("label.hsnCode");
	String taxAmount = resource.getString("label.taxAmount");
	String amountAfterTax = resource.getString("label.amountAfterTax");

	String remarks = resource.getString("label.remarks");
%>

<html>
<head>
<style>

/* css for market inwards */
.form-control{height:34px !important;}
.breadcrumb {background: #eaeaea !important;}
.marGinBottom{margin-bottom:15px;}
.btnpadd{margin-right:5px;}
.text-center-left{text-align:center;}
@media only screen and (min-width:320px) and (max-width:767px){.text-center-left{text-align:left;}}
.selectsingleSite{padding-left:0px !important;margin-bottom:15px;font-weight:bold;color:#000;}
.selectsingleSite .form-control{height:34px;font-weight: bold;color: #000;}
.anchor-market{float: left;margin-bottom:10px;color:#0000ff;font-size:15px;font-weight:bold;}
.anchor-market:hover{border: 1px solid #0000ff;padding: 5px;border-radius: 25px;font-weight:bold;color:#0000ff;}
.radio_Market{font-size:15px;font-weight:bold;margin-top:15px;margin-bottom:15px;}
.radio_Market input{ height: 17px;width: 23px;vertical-align: text-bottom;}
.totalMarketingAmount td{background:#bfd9e5;text-align:right;font-size:13px;font-weight:bold;}
/* css for market inwards */
.table>thead>tr>th{border-top:1px solid #000 !important;border-bottom:1px solid #000 !important;background-color:#ccc;}
.table tbody tr td{border:1px solid #000;}
.finalAmountDiv{color: #f0ad4e;font-size: 24px;margin-left: 15px;font-weight: bold;}
.invoiceGrandDiv{color: #f0ad4e;font-size: 24px;}
.form-group label{text-align: left}
.text-line {background-color: transparent;color:#313030;outline: none;outline-style: none;outline-offset: 0;border-top: none;border-left: none;border-right: none;border-bottom: solid #bdb7ab 1px;padding: 3px 10px;}
.mybtnstyles{height: 30px;margin-top: -6px;border-radius: 0px !important;margin-bottom: 10px;background-color: transparent;}
.myinputstyles{display: inline !important;width: 75% !important;border-radius: 0px !important;margin-bottom: 10px;border: none;box-shadow: none;border-bottom: 2px solid #ccc;}
.pro-table tbody tr ,.pro-table tbody tr th{margin:2px 3px;width:100%;min-width:213px; }
.table>thead>tr>th{border-top:1px solid #000 !important;border-bottom:1px solid #000 !important;background-color:#ccc;}
.table tbody tr td{border:1px solid #000;}
.finalAmountDiv{color: #f0ad4e;font-size: 24px;margin-left: 15px;font-weight: bold;}
.invoiceGrandDiv{color: #f0ad4e;font-size: 24px;}
.form-group label{text-align: left}
.ui-timepicker-standard {z-index:1051 !important;}
.no-cursor{cursor: not-allowed;} 
.text-control{width:100%;border-radius:5px;margin-bottom:15px;}
</style>
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
	    <jsp:include page="../CacheClear.jsp" />  
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<script src="js/momment.js"></script>
		<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
		<script src="js/sweetalert2.js" type="text/javascript"></script>
		<script src="js/numberCheck.js" type="text/javascript"></script>
		
		<script>
//Create DIV element and append to the table cell
var i = 1;

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
     var vfx = fldLength;  
	 tableColumnName=tableColumnName.trim();
     var snColumn =  "<%= sNumber %>";
     snoColumn = formatChargesColumns(snColumn);	 
	 var conveyanceChargesColumn =  "<%= conveyanceCharges %>";
	 conveyanceChargesColumn = formatChargesColumns(conveyanceChargesColumn);
	 var conveyanceAmountColumn =  "<%= conveyanceAmount %>";
	 conveyanceAmountColumn = formatChargesColumns(conveyanceAmountColumn);
	 var taxChargeColumn =  "<%= conveyanceTax %>";
	 taxChargeColumn = formatColumns(taxChargeColumn);
	 var GSTAmountColumn =  "<%= GSTAmount %>";
	 GSTAmountColumn = formatChargesColumns(GSTAmountColumn);
	 var chargesAmountAfterTaxColumn =  "<%= chargesAmountAfterTax %>";
	 chargesAmountAfterTaxColumn = formatChargesColumns(chargesAmountAfterTaxColumn);
	 var trasportInvoiceColumn =  "<%= trasportInvoice %>";
	 trasportInvoiceColumn = formatChargesColumns(trasportInvoiceColumn);
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatChargesColumns(actionsColumn);
     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoChargesDivId"+vfx);
        cell.appendChild(snoDiv);
    }
    else {
    	if(tableColumnName == conveyanceChargesColumn) {
    		var dynamicSelectBoxId = "Conveyance"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", "Conveyance"+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId); 
    	    div.setAttribute("onchange", "conveyanceChange("+vfx+")");
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "--Select--";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);    	    
    	    var option = "";
    		<%
			Map<String, String> gstTax = (Map<String, String>)request.getAttribute("chargesMap");
			for(Map.Entry<String, String> tax : gstTax.entrySet()) {
			String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
			%>
				option = document.createElement("option");
	    	    option.text = "<%= tax.getValue() %>";
	    	    option.value = "<%= taxIdAndPercentage %>";
	    	    div.appendChild(option);
    		<% 
				} 
			%> 
    	    cell.appendChild(div); 
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);	
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'Conveyance Amount');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxChargeColumn) {
    		var dynamicSelectBoxId = "GSTTax"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id",tableColumnName+vfx);
    	    div.setAttribute("onchange", "calculateGSTTaxAmount("+vfx+")");
    	    cell.appendChild(div);
    	    
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "--Select--";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);
    	    var option = "";
    		<% 
    			Map<String, String> chargesGST = (Map<String, String>)request.getAttribute("gstMap");
				for(Map.Entry<String, String> tax : chargesGST.entrySet()) {
				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
			%>
				option = document.createElement("option");
	    	    option.text = "<%= tax.getValue() %>";
	    	    option.value = "<%= taxIdAndPercentage %>";
	    	    div.appendChild(option);
    		<% 
				} 
			%>        
    	    cell.appendChild(div);
    	}
    	else if(tableColumnName == GSTAmountColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number", placeholder="Enter Amount");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", 'GST Amount');
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    cell.appendChild(div);
   		}   	
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'Amount After Tax');
		    div.setAttribute("readonly", 'true');
		    cell.appendChild(div);
   		}   
    	else if(tableColumnName == trasportInvoiceColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'Transport Invoice Number');
		    cell.appendChild(div);
   		}  
        else if(tableColumnName == actionsColumn) {
			var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewChargesItemBtn");
		    div2.setAttribute("id", "addNewChargesItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendChargesRow(this, "+vfx+")");
		    div2.setAttribute("class", "btnaction");
		    cell.appendChild(div2);			    
		    
		    var btn2 = document.createElement("i");
		    btn2.setAttribute("class", "fa fa-plus");
		    div2.appendChild(btn2);
		    
		    cell.append(" ");
		    
	    	var div1 = document.createElement("button");
		    div1.setAttribute("type", "button");
		    div1.setAttribute("name", "addDeleteItemBtn");
		    div1.setAttribute("id", "addDeleteItemBtnId"+vfx);
		    div1.setAttribute("value", "Delete Item");
		    div1.setAttribute("onclick", "deleteConveyancerow(this, "+vfx+")");
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}
    }
}
function createModelPOPUP(){
	var transportDetails = document.getElementById("Conveyance1").value;
	if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
		alert("Please Select the Conveynace or None .");
		document.getElementById("Conveyance1").focus();
		return ;
	} else if(transportDetails !== "" || transportDetails !== '' || transportDetails !== null){
	$('.modal').modal('show');

	}
}; 

function validateDate(){debugger;
var error=true;
$(".tr-class").each(function(){
 var currentId=$(this).attr("id").split("tr-class")[1];	
	if($("#mainQantity"+currentId).val()!='0'){
		if($("#expireDate"+currentId).val()==''){
			alert("Please enter expirydate.");
			$("#expireDate"+currentId).focus();
			return error=false;
		}
	}
});
return error;
}


function validateVendorDetails(){
	var indentFor = document.getElementById("indentFor").value;
	if(indentFor == "" || indentFor == null || indentFor == '') {
		alert("Please select the Service for.");
		$("#indentFor").focus();
		return false;
	}
    var invoiceId = document.getElementById("InvoiceNumberId").value;
	if(invoiceId == "" || invoiceId == null || invoiceId == '') {
		alert("Please enter the Invoice Number.");
		$("#InvoiceNumberId").focus();
		return false;
	}
    var invoiceDate = document.getElementById("InvoiceDateId").value;
	if(invoiceDate == "" || invoiceDate == null || invoiceDate == '') {
		alert("Please enter the Invoice Date.");
		$("#InvoiceDateId").focus();
		return false;
	}
    var CrdState = document.getElementById("state").value;
	if(CrdState == "" || CrdState == null || CrdState == '') {
		alert("Please enter the select the State.");
		$("#state").focus();
		return false;
	}
    var receivedDate = document.getElementById("receivedDate").value;
	if(receivedDate == "" || receivedDate == null || receivedDate == '') {
		alert("Please enter the Received Date.");
		$("#receivedDate").focus();
		return false;
	}
}

function createInvoice() {
    var indentFor = document.getElementById("indentFor").value;
	var valVendorStatus=validateVendorDetails();
	if(valVendorStatus==false){
		return false;
	}
	//validating Quantity
	var flag1 = validateQtyvalEmpty();
	if(flag1==false){
	   alert("Please enter Qunatity.");
	   return false;
	}
	var flag = checkQuantityEnteredOrNot();
	if(flag==false){
		return false;
	}
	maincalculateOtherCharges();
	var tableRowsCount;
	var typeOfSite=$("#poToId").val();
	if(typeOfSite=="select"){
		loadChildQty();
		alert("please select site.");
		$("#modal-marketing").modal("show");
		return false;
	}
	if(typeOfSite=="SiteWise"){
		if($("#grandTotalsite").text()=='' || $("#grandTotalsite").text()=='0'){
			loadChildQty();
			alert("please enter amount.");
			$("#modal-marketing").modal("show");
			return false;
		}
		tableRowsCount=[];
		$(".siteclass").each(function(){
			var currentId=$(this).attr("id").split("siteAdd")[1];
			tableRowsCount.push(currentId);		
		});
		var singlesite=checkSingleSiteData();
		if(singlesite==false){
			return false;
		}
	}
	if(typeOfSite=="MultipleSite"){
		if($("#multiSiteGrandTotal").text()=='' || $("#multiSiteGrandTotal").text()=='0'){
			loadChildQty();
			alert("please enter amount.");
			$("#modal-marketing").modal("show");
			return false;
		}
		var valtable=checkMultipleSiteData();
		if(valtable==false){
			return false;
		}
		tableRowsCount=[];
		$(".expenditure").each(function(){
			var currentId=$(this).attr("id").split("multisiteAdd")[1];
			tableRowsCount.push(currentId);		
		});
		var multiSite=checkMultipleSiteData();
		if(multiSite==false){
			return false;
		}
	}
	if(typeOfSite=="LocationWise"){
		if($("#grandTotal").text()=='' || $("#grandTotal").text()=='0'){
			loadChildQty();
			alert("please enter amount.");
			$("#modal-marketing").modal("show");
			return false;
		}
		tableRowsCount=[];
		$(".locationtableclass").each(function(){
			var currentId=$(this).attr("id").split("locationAdd")[1];
			tableRowsCount.push(currentId);		
		});
		var locationSite=checkingLocationData();
		if(locationSite==false){
			return false;
		}
	}
	if(typeOfSite=="BrandingWise"){
		if($("#grandTotalbrand").text()=='' || $("#grandTotalbrand").text()=='0'){
			loadChildQty();
			alert("please enter amount.");
			$("#modal-marketing").modal("show");
			return false;
		}
		tableRowsCount=[];
		$(".brandingtablecls").each(function(){
			var currentId=$(this).attr("id").split("brandingtable")[1];
			tableRowsCount.push(currentId);		
		});
		var BrandingSite=checkingBrandingData();
		if(BrandingSite==false){
			return false;
		}
	}
	
	$("#multiplesitesrowcount").val(tableRowsCount);
	var transporterName = document.getElementById("transporterNameIdId").value;
	if(transporterName == "" || transporterName == null || transporterName == '') {
		alert("Please select the transportorName.");
		document.getElementById("transporterNameIdId").focus();
		return false;
	}
	
	debugger;
	var dateStatus=validateDate();
	if(dateStatus==false){
		return false;
	}
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	$("#loaderId").show();
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	if(indentFor==1){
		document.getElementById("ProductWiseIndentsFormId").action = "convertMarketingPOtoInvoice.spring";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
	}
	else if(indentFor==2){
		document.getElementById("ProductWiseIndentsFormId").action = "";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
	}
}
</script>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
</head>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	</style>
</noscript>
<div class="container body" id="mainDivId">
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
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Home</a></li>
								<li class="breadcrumb-item active">Inwards From PO</li>
							</ol>
						</div>
						 <div class="loader-ims" id="loaderId" style="display:none;"> <!--  -->
					         <div class="lds-ims">
						       <div></div><div></div><div></div><div></div><div></div><div></div></div>
					        <div id="loadingimsMessage">Loading...</div>
				           </div>
				<div>
				<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
						<div class="col-md-12">
								<div class="container border-inwards">
										 <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">
										  <div class="col-md-4">
											   <div class="form-group">
													<label class="col-md-6">Service For : </label>
													<div class="col-md-6" >
																<select name="indentFor" id="indentFor" class="form-control">
														              <option value="">---Select--</option>
															          <option value="1">Invoice</option>
															         <!--  <option value="2">DC</option> -->
																</select>
													</div>
												</div>
										  </div>
										  <div class="clearfix"></div>
											<div class="col-md-4">
											  <div class="form-group">
													<label class="col-md-6">Vendor Name : </label>
													<div class="col-md-6" >
														<form:input path="vendorName" name="vendorName" id="VendorNameId" class="form-control" value="${poDetails.vendorName}" readonly="true" />
													</div>
												</div>
											</div>
											<div class="col-md-4">
												 <div class="form-group">
													   <label class="col-md-6">GSTIN : </label>
														<div class="col-md-6">
															<form:input path="strGSTINNumber" name="strGSTINNumber" id="GSTINNumber" class="form-control" value="${poDetails.strGSTINNumber}" autocomplete="off" readonly="true" />
														</div>
												 </div>
											</div>	 
											<div class="col-md-4">
												 <div class="form-group">
													  <label class="col-md-6">Vendor Address : </label>
														<div class="col-md-6" >
															<form:input path="vendorAddress" name="vendorAddress" id="VendorAddress" class="form-control" value="${poDetails.vendorAddress}"  data-toggle="tooltip" data-placement="bottom" title="${poDetails.vendorAddress}" readonly="true" />
														</div>
												 </div>
												
											</div>		
											<div class="col-md-4">
													<input type="hidden" id="vendorId" name="VendorId" class="form-control" value="${poDetails.vendorId}" />
											</div>	
											<div class="clearfix"></div>
											<div class="col-md-4" style="display:none">
												<div class="form-group">
												    <label class="col-md-6">Indent No : </label>
													<div class="col-md-6" >
														<input type="hidden" id="sitewiseIndentNumber" name="sitewiseIndentNumber" class="form-control" readonly="true" value="${poDetails.siteWiseIndentNo}">
														<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${poDetails.indentNo}">
													</div>
												</div>
											</div>
											<div class="col-md-4">
													  <div class="form-group">
													   <label class="col-md-6">Site Name : </label>
														<div class="col-md-6" >
															<input type="text" id="siteName" name="SiteName" class="form-control" readonly="true" value="${poDetails.siteName}">
															<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}">
														</div>
													  </div>
											</div>
											 <div class="col-md-4">
												 	 <div class="form-group">
												 	     <label class="col-md-6">Invoice/DC Number :</label>
														  <div class="col-md-6" >
																<input type="text" name="InvoiceorDCNumber" id="InvoiceNumberId" class="form-control" autocomplete="off" value="">
																<span id="errorMessageInvoiceNumber" style="display:none; color:red">* Already exist the invoice/DC number</span>
																<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
														 </div>
												 	 </div>
											 </div>
											 <div class="col-md-4">
												  <div class="form-group">
													  <label class="col-md-6 col-xs-12">Invoice/DC Date :  </label>
													  <div class="col-md-6 col-xs-12 input-group" >
														<input type="text" name="InvoiceorDCDate" id="InvoiceDateId" class="form-control readonly-color" autocomplete="off" value="" readonly="true">
														<label class="input-group-addon btn input-group-addon-border" for="InvoiceDateId"><span class="fa fa-calendar"></span></label>
														
													  </div>
												</div>
											 </div>
											 <div class="clearfix"></div>
											<div class="col-md-4">
												 <div class="form-group">
												  <label class="col-md-6">State : </label>
													<div class="col-md-6" >
																<select name="state" id="state" class="form-control">
														              <option value="">---Select--</option>
															          <option value="1">Local</option>
															          <option value="2">Non Local</option>
																</select>
										
													</div>
												 </div>
											</div>
											<div class="col-md-4">
											 <div class="form-group">
												    <label class="col-md-6">PO No : </label>
													<div class="col-md-6" >
														<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off" readonly="true" value="${poNumber}"  data-toggle="tooltip" data-placement="bottom" title="${poNumber}">
														<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
													</div>
											 </div>
											</div>
											 <div class="col-md-4">
											   <div class="form-group">
												   <label class="col-md-6 col-xs-12">PO Date :   </label>
													<div class="col-md-6 col-xs-12 input-group" >
														<input type="text"  name="poDate" id="poDateId" class="form-control" readonly="true" value="${poDetails.poDate}">
														<label class="input-group-addon btn input-group-addon-border" for="poDateId"><span class="fa fa-calendar"></span></label>
													</div>
												</div>
											 </div>
											 <div class="col-md-4">
											 	 <div class="form-group">
											 	    <label class="col-md-6">E Way Bill No : </label>
													<div class="col-md-6" >
														<input type="text"  name="eWayBillNo" id="eWayBillNoId" class="form-control" value=""}>
													</div>
											 	 </div>
											 </div>
											 <div class="col-md-4">
												 <div class="form-group">
												  <label class="col-md-6">Vehicle No : </label>
													<div class="col-md-6" >
														<input type="text" name="vehileNo" id="vehileNoId" class="form-control" value="">
													</div>
												 </div>
											</div>
											<div class="col-md-4">
												 <div class="form-group ">
													<label class="col-md-6">Transporter Name :  </label>
													<div class="col-md-6" >
													<input type="text"  name="transporterName1" id="transporterNameIdId" onkeyup="return populateData();" class="form-control" value=""> 
			 										<input type="hidden"  name="transporterName" id="transporterNameId"  class="form-control" value="">
														<!-- <input type="text"  name="transporterName" id="transporterNameId" class="form-control" value=""> -->
													</div>
												 </div>
											</div>
											<div class="col-md-4">
												 <div class="form-group">
												    <label class="col-md-6 col-xs-12">Received Date : </label>
													<div class="col-md-6 col-xs-12 input-group">
														<input type="text"  name="receivedDate" id="receivedDate" class="form-control readonly-color" autocomplete="off" value="" readonly="true">
														<label class="input-group-addon btn input-group-addon-border" for="receivedDate"><span class="fa fa-calendar"></span></label>
														
													</div>
												 </div>
											</div>			  
											<input type="hidden" id="hiddenPODate"  name="hiddenPODate" value="${noOfDays}"/>
											<input type="hidden" id="payment_Req_Date"  name="payment_Req_Date" value="${poDetails.payment_Req_days}"/>
											</c:forEach>
									</div>
								  <div class="clearfix"></div>
										<div>
										       <div class="table-responsive Mrgtop20">    <!--  protbldiv -->
													<table id="doInventoryTableId-Main" class="table pro-table" style="width:4500px;">
														<thead>
															 <tr>
																<th>S.NO</th>
											    				<th style="width:250px;">Product</th>
											    				<th style="width:250px;">Sub Product</th>
											    				<th style="width:250px;">Child Product</th>    								
											    				<th>Units Of Measurement</th>
											    				<th>Quantity</th>
											    				<th>Received Quantity</th> 
											    				 <th>PO Quantity</th>
											    				<th> Price</th>
											    				<th> Basic Amount</th>
											    				<th>Discount</th>
											    				<th>Amount After Discount</th>
											    				<th> Tax </th>
											    				<th> HSN Code </th>
											    				<th>Tax Amount</th>
											    				<th>Amount After Tax</th>
											    				<th> Other Or Transport Charges</th>
											    				<th>Tax On Other Or Transport Charges</th>
											    				<th> Other Or Transport Charges After Tax </th>
											    				<th> Total Amount </th>
											    				<th> Remarks </th>
																 <th>Expire Date</th>  
															</tr>
														</thead>
													<tbody>
													  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
										        	 	 <tr class="tr-class" id="tr-class${GetProductDetails.serialno}">
											        	 	<td>			
														       <div id="mainsnoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
															</td>
															<td>
																<select id="product${GetProductDetails.serialno}" readonly="true" name="mainProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1" data-toggle="tooltip" data-placement="bottom"  value="${GetProductDetails.productName}" title="${GetProductDetails.productName}">
																<option value="${GetProductDetails.productId}$${GetProductDetails.productName}">${GetProductDetails.productName}</option>	
																	   <%			
															    		
															    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
															    			for(Map.Entry<String, String> prods : prod.entrySet()) {
																			String prodIdAndName = prods.getKey()+"$"+prods.getValue();
																		%>
																			<option value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
															    		<% } %>
																</select>
															</td>
															<td>
																<form:select path="" id="subProduct${GetProductDetails.serialno}"  readonly="true" name="mainSubProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}" data-toggle="tooltip" data-placement="bottom" title="${GetProductDetails.child_ProductName}">
																<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
																</form:select>
															</td>
															<td>
																<form:select path="" id="childProduct${GetProductDetails.serialno}" readonly="true" name="mainChildProduct${GetProductDetails.serialno}" class="form-control  childprdctcls btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}" data-toggle="tooltip" data-placement="bottom" title="${GetProductDetails.child_ProductName}">
																  <option value="<c:out value='${GetProductDetails.child_ProductId}$${GetProductDetails.child_ProductName}'/>">${GetProductDetails.child_ProductName}</option>
															   </form:select>
															</td>
															<td>
																<form:select path="" id="mainUnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="mainUnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
																	<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
																</form:select>
															</td>
														  <td>
									                        <input type="text"  id="mainQantity${GetProductDetails.serialno}"  class="form-control qtyinput"  name="mainquantity${GetProductDetails.serialno}" onfocusout ="validateQuantity(${GetProductDetails.serialno})" onkeypress="return validateNumbers(this, event)" tabindex="${GetProductDetails.serialno}" value=""/> 
									                        <input type="hidden"  id="hiddenQantity${GetProductDetails.serialno}"     class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
									                         <input type="hidden"  id="actionVal${GetProductDetails.serialno}" value="N"/> 
									    			    </td>	
									    			    <td>
									                        <input type="text"  name="ReceivedQty${GetProductDetails.serialno}" id="ReceivedQty${GetProductDetails.serialno}" readonly="true" class="form-control" value="${GetProductDetails.recivedQty}"/> 
									                    </td>                  			
														<td>
									                        <input type="text"  readonly="true" class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
									                    </td>
														<td>
														    <input type="text" id="mainprice${GetProductDetails.serialno}"  name ="mainPrice${GetProductDetails.serialno}"  readonly="true" onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}"/>
														</td>
														<td> 
														    <input type="text" id="mainBasicAmountId${GetProductDetails.serialno}"  name ="mainBasicAmount${GetProductDetails.serialno}" readonly="true" onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.basicAmt}"/>
														</td>
														<td> 
														   <input type="text" id="mainDiscount${GetProductDetails.serialno}" name="mainDiscount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(${GetProductDetails.serialno});" readonly="true" class="form-control disable-class1" value="${GetProductDetails.strDiscount}"/><i  style="position:absolute;right:16px;top:16px;" class="fa fa-percent"></i>
									                	</td>
														<td>
														   <input type="text" id="mainamtAfterDiscount${GetProductDetails.serialno}"  name ="mainamtAfterDiscount${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.strAmtAfterDiscount}"/>
														</td>
														<td>
															<form:select path="" id="maintaxAmount${GetProductDetails.serialno}"  name ="maintax${GetProductDetails.serialno}" readonly="true"   onchange="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" >
																<form:option value="${GetProductDetails.taxId}$${GetProductDetails.tax}" >${GetProductDetails.tax}</form:option>
														    		<%	
														    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
														    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
																		String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
																	%>
																		<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
														    		<% } %>
															
															
															</form:select>
														</td>
														<td>
														  <input type="text" id="hsnCode${GetProductDetails.serialno}"  name ="mainhsnCode${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
														</td>
														<td>
														  <input type="text" id="mainTaxAmount${GetProductDetails.serialno}"  name ="maintaxAmount${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
														</td>
														<td>
														  <input type="text" id="mainTaxAftertotalAmount${GetProductDetails.serialno}"  name ="mainamountAfterTax${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
														</td>
														<td>
														    <input type = "text" id="mainOtherOrTransportChargesId${GetProductDetails.serialno}"  name="mainotherOrTransportCharges${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.othercharges1}"/>
														</td>
														<td>
												         	<input  type="text" id="mainTaxOnOtherOrTransportChargesId${GetProductDetails.serialno}"  name="maintaxOnOtherOrTransportCharges${GetProductDetails.serialno}"  readonly="true"  class="form-control" value="${GetProductDetails.taxonothertranportcharge1}"/>
														</td>
														<td>
														<input type="text" id="mainOtherOrTransportChargesAfterTaxId${GetProductDetails.serialno}"  name="mainotherOrTransportChargesAfterTax${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.otherchargesaftertax1}"/>
														</td>
														<td>
														    <input type="text"   class='ttamount form-control' id='mainTotalAmountId${GetProductDetails.serialno}'  name='maintotalAmount${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.totalAmount}"/>
														    <input type="hidden"   id='indentCreationDetailsId${GetProductDetails.serialno}'  name='mainindentCreationDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.indentCreationDetailsId}"/>
														    <input type="hidden"   id='poEntryDetailsId${GetProductDetails.serialno}'  name='mainPoEntryDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.poEntryDetailsId}"/>
														</td>
														<td>
														   <input type="text" id="InvoiceRemarks${GetProductDetails.serialno}"  name="InvoiceRemarks${GetProductDetails.serialno}"    class="form-control" />
														</td>	
														 <td>
														    <input type="text" id="expireDate${GetProductDetails.serialno}"  name="expireDate${GetProductDetails.serialno}"    class="form-control ExpiryDate readonly-color" autocomplete="off" readonly="true"/>
														</td> 
														<td style="display:none">
														   <input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
														</td>  
														
														<td style="display:none">
														<input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
														</td>
													</tr>
											 	</c:forEach> 
											 </tbody>
										</table>
									</div>
									<td style="display:none">
									 <input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
									</td> 			
									<td style="display:none">
									 <input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
									</td>
							<div>			
					</div>
								
			<!-- 	//********************************************************************* -->
					<div class="Mrgtop20"><!-- <input type="checkbox" id="creditnote" name="creditnote" style="float: left" data-toggle="modal" data-target="#myModal"/><span style="float:left;color:blue;font-weight:bold;font-size:14px;">&nbsp;(Optional)Please click,If you want to return the products.</span> --></div>
									<div class="modal fade" id="myModal" role="dialog">
									    <div class="modal-dialog modal-lg" style="width:95%;">
									      <!-- Modal content-->
									      <div class="modal-content">
									        <div class="modal-header">
									          <button type="button" class="close" data-dismiss="modal">&times;</button>
									          <h4 class="modal-title text-center">Credit Note</h4>
									        </div>
									        <div class="modal-body">
									        	<div class="col-md-4 col-md-offset-3"><div class="form-group">
									        	 <label class="col-md-6">Credit Note Number</label>
									        	 <div class="col-md-6"><input type="text" name="creditNoteNumber" id="creditNoteNumber" size="3" class="form-control" autocomplete="off" value=""></div>
									        	</div></div>
									        	<div class="clearfix"></div>
									        	<div class="table-responsive protbldiv Mrgtop20">  
													<table id="doInventoryTableId" class="table pro-table tbl-width-inwards">
													<thead>
													 <tr >
														<th>S NO</th>
									    				<th>Product</th>
									    				<th>Sub Product</th>
									    				<th>Child Product</th>    								
									    				<th>Units Of Measurement</th>
									    				<th>Quantity</th> 
									    				<th> Price</th>
									    				<th> BasicAmount</th>
									    				<th>Discount</th>
									    				<th>AmountAfterDiscount</th>
									    				<th> Tax </th>
									    				<th> HSNCode </th>
									    				<th>Tax Amount</th>
									    				<th>AmountAfterTax</th>
									    				<th> Other Or TransportCharges</th>
									    				<th>TaxOn Other Or TransportCharges</th>
									    				<th> Other Or TransportChargesAfterTax </th>
									    				<th> Total Amount </th>
														<th>Actions</th>
													</tr>
												</thead>
										         <tbody>
										             <tr id="">
										        	 	<td>						
															   <div id="snoDivId1">1</div>
														  </td>
														<td>
															<select id="combobox1" name="Product1" class="form-control  btn-visibilty1 btn-loop1">
														    		<option value="">Select one...</option>
														    		<%			
														    		
														    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
														    			for(Map.Entry<String, String> prods : prod.entrySet()) {
																		String prodIdAndName = prods.getKey()+"$"+prods.getValue();
																	%>
																		<option value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
														    		<% } %>
															</select>
															
														</td>
														<td>
															<form:select path="" id="comboboxsubProd1" name="SubProduct1" class="form-control  btn-visibilty1 btn-loop1"/> 
														</td>
														<td>
															<form:select path="" id="comboboxsubSubProd1" name="ChildProduct1" class="form-control  btn-visibilty1 btn-loop1"/> 
														</td>
														<td>
															<form:select path="" id="UnitsOfMeasurementId1"  name="UnitsOfMeasurement1" onchange="return appendvalues(1);" class="form-control  "/>
														</td>
														<td  class="s-50">
															 <input type="text"  id="stQantityPOP1"    name="quantity1" onblur="calculatequantitybaseinpop(1)"  class="form-control" value="10"/>  
									                    <%--     <form:input path="" id="ProductAvailabilityId1" readonly="true" class="form-control"/> --%>
														</td>					
														<td>
															<form:input path="" id="PriceId1" name="PriceId1" onkeypress="return validateNumbers(this, event);" value="20"  class="form-control"/>
														</td>
														<td>
															<form:input path="" id="BasicAmountId1" name="BasicAmountId1" class="form-control"  onkeypress="return validateNumbers(this, event);" />
														</td>
															<td>
															<form:input path="" id="Discount1" name="Discount1" class="form-control"  onkeypress="return validateNumbers(this, event);" value="10"  />
														</td>
															
											
														<td>
															<form:input path="" id="amtAfterDiscount1" name="amtAfterDiscount1" class="form-control"  onkeypress="return validateNumbers(this, event);" value="" />
														</td>
														
											
														
														<td>
															<form:select path="" id="TaxId1"  name ="TaxId1"    onchange="calculateChangedTaxAmount(1)" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" >
																<form:option value="${GetProductDetails.taxId}$${GetProductDetails.tax}" >${GetProductDetails.tax}</form:option>
														    		<%	
														    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
														    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
																		String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
																	%>
																		<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
														    		<% } %>
															
															
															</form:select>
														</td>
														<td>
															<form:input path="" id="HSNCodeId1" name="HSNCodeId1"  class="form-control" autocomplete="off"/>
														</td>
														<td>
															<form:input path="" id="TaxAmountId1" name="TaxAmountId1" readonly="true" class="form-control"/>
														</td>
														<td>
															<form:input path="" id="AmountAfterTaxId1" name="AmountAfterTaxId1" readonly="true" class="form-control"/>
														</td>
														<td>
															<form:input path="" id="OtherOrTransportChargesId1" name="OtherOrTransportChargesId1" readonly="true" class="form-control"/>
														</td>
														<td>
															<form:input path="" id="TaxOnOtherOrTransportChargesId1" name="TaxOnOtherOrTransportChargesId1" readonly="true" class="form-control"/>
														</td>
														<td>
															<form:input path="" id="OtherOrTransportChargesAfterTaxId1" name="OtherOrTransportChargesAfterTaxId1" readonly="true" class="form-control"/>
														</td>
														<td>
															<input type="text"   class='form-control totalAmount' id='TotalAmountId1'  name='TotalAmountId1'    readonly="true" class="form-control"  value=""/>
														<td>
															<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
															<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow(1)" class="btnaction"><i class="fa fa-plus"></i></button>
															<button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteConveyancerow(this, 1)" ><i class="fa fa-trash"></i></button>
														</td>
													</tr>
										        </tbody>
										</table>
									</div> 
							</div>
							<div class="modal-footer">
								 <div class="text-center center-block">
									   <button type="button" class="btn btn-danger" data-dismiss="modal">Submit</button>
								</div>
							 </div>
					</div>
				 </div>
		</div>
	<!-- *****************************************************************************************	 -->
		<div class="clearfix"></div><br/>
				<div class="table-responsive "> <!-- protbldiv -->
								<table id="doInventoryChargesTableId" class="table tbl-width-inwards-half"> <!-- pro-table -->
										<thead>
											 <tr>
												<th><%= sNumber %></th>
							    				<th><%= conveyanceCharges %></th>
							    				<th><%= conveyanceAmount %></th>
							    				<th><%= conveyanceTax %></th>    								
							    				<th><%= GSTAmount %></th>
							   					<th><%= chargesAmountAfterTax %></th>
							     				<th><%= trasportInvoice %></th>
							    				<th><%= actions %></th>
							  				</tr>
										</thead>
										<tbody>
												 <c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
													<tr id="chargesrow${GetTransportDetails.strSerialNumber}" class="conveyanceRow">
														<td>						
															<div id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
														</td>
														<td>						
																<form:select path="" id="Conveyance${GetTransportDetails.strSerialNumber}" name="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" readonly="true" onchange="conveyanceChange(${GetTransportDetails.strSerialNumber})">
																
																<form:option value="${GetTransportDetails.conveyanceId1}$${GetTransportDetails.conveyance1}">${GetTransportDetails.conveyance1}</form:option>
														   		<%	
														    			Map<String, String> conveyanceCharges1 = (Map<String, String>)request.getAttribute("chargesMap");
														    			for(Map.Entry<String, String> tax : conveyanceCharges1.entrySet()) {
														    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
																	%>
																		<form:option value="<%=taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
														    		<% } %> 
															</form:select>
														</td>
														<td>
														<%
														
														int i=1;
														%>
															<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="text" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})" placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass" autocomplete="off" readonly="true"/>
														</td>
														<td>
															<form:select path="" id="GSTTax${GetTransportDetails.strSerialNumber}" name="GSTTax${GetTransportDetails.strSerialNumber}" class="form-control GSTClass " value="5%" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})" readonly="true" >
																
																<form:option value="${GetTransportDetails.GSTTaxId1}$${GetTransportDetails.GSTTax1}">${GetTransportDetails.GSTTax1}</form:option>
														          <option value="">--Select--</option>
														           <%	
														    			Map<String, String> conveyanceTax1 = (Map<String, String>)request.getAttribute("gstMap");
														    			for(Map.Entry<String, String> tax : conveyanceTax1.entrySet()) {
														    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
																	%>
																		<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
														    		<% } %> 
															</form:select>
														</td>
															<td>
															<form:input path="" id="GSTAmount${GetTransportDetails.strSerialNumber}" name="GSTAmount${GetTransportDetails.strSerialNumber}" type="number" placeholder="GST Amount" value="${GetTransportDetails.GSTAmount1}" readonly="true" class="form-control noneClass" autocomplete="off"/>
														</td>
															<td>
															<form:input path="" type="text"  name="AmountAfterTax${GetTransportDetails.strSerialNumber}" id="AmountAfterTax${GetTransportDetails.strSerialNumber}" placeholder="Amount After Tax"  value="${GetTransportDetails.amountAfterTaxx1}" readonly="true" class="form-control noneClass" autocomplete="off"/>
														</td>
													
														<td>
															<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()" class="form-control" autocomplete="off"/>
															<form:input path=""  type="hidden" id="actionStatus${GetTransportDetails.strSerialNumber}" name="TransportInvoicehidden${GetTransportDetails.strSerialNumber}" class="hiddentablestikeout" value="S"/>
														</td>
														<td>
															<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(this, ${GetTransportDetails.strSerialNumber})" class="btnaction "><i class="fa fa-plus"></i></button> 
															<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteConveyancerowstatic(this, ${GetTransportDetails.strSerialNumber})" ><i class="fa fa-trash"></i></button>
															<button type="button" name="addEditItemBtn" id="addEditChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="eidtConveyancerow(this, ${GetTransportDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button>
													</td>
													</tr>
													</c:forEach>
											
											</tbody>
								</table>
							</div>
						<!-- po product location details - start -->
							<c:if test="${LocationDetails}">
							<div class="table-responsive" style="display:none;">
									    <table class="table fixedTblHeader" id="FieldLocation_durationtable" style="width:2000px;">
									       <thead class="calTheadColor">
									         <tr>
										           <th style="width:50px;">S.No</th>
										           <th>Child Product Name</th>
										           <th>Location</th>
										           <th>From Date</th>
										           <th>To Date</th>
										           <th>Time</th>
										           <th>Quantity</th>
										           <th>Site Name</th>
										           <th>Price Per Unit After Tax</th>
										           <th>Total Amount</th>
										           <th>Actions</th>
									         </tr>
									       </thead>
									       <tbody class="durationTableBody">
									       <c:forEach var="ProdLocItem" items="${requestScope['POProductLocationDetails']}" step="1" begin="0">
									        <tr id="durationTablelocation1" class="durationTablelocation">
									           <td style="width:50px;">${ProdLocItem.strSerialNumber}</td>
									           <td>
									           <input type="text" value="${ProdLocItem.child_ProductId}$${ProdLocItem.child_ProductName}" id="dutrationChildId${ProdLocItem.strSerialNumber}" class="form-control">
									              <select class="form-control" id="locationChildProduct1" name="locationChildProduct1" onchange="addLocation(1)" style="display:none">
										                <option >Acid</option>
										                <option>Air Spray</option>
										                <option>Mobile Phones</option>
										                <option>Wires</option>
									               </select>
									           </td><select class="form-control" id="location_mar1" name="location_mar1"></select>
									           <td><input type="text" class="form-control" id="location_Id${ProdLocItem.strSerialNumber}" name="location_Id${ProdLocItem.strSerialNumber}" value="${ProdLocItem.hoardingId}$${ProdLocItem.location}"></td>
									           <td>
									               <input type="text" id="from_date_location${ProdLocItem.strSerialNumber}" class="form-control" name="from_date_location${ProdLocItem.strSerialNumber}" placeholder="Select from date" onkeydown="return false;" autocomplete="off" value="${ProdLocItem.fromDate}"/>
									           </td>
									           <td>
									               <input type="text" id="to_date_location${ProdLocItem.strSerialNumber}" class="form-control" name="to_date_location${ProdLocItem.strSerialNumber}" placeholder="Select to date" onkeydown="return false;" autocomplete="off" value="${ProdLocItem.toDate}"/>
									           </td>
									           <td>
									              <input type="text"  class="form-control" id="timepicker${ProdLocItem.strSerialNumber}" name="timepicker${ProdLocItem.strSerialNumber}" placeholder="Select time" onkeydown="return false;" autocomplete="off" value="${ProdLocItem.time}"/>
									            </td>
									           <td><input type="text"  class="form-control" id="locationQuantity${ProdLocItem.strSerialNumber}" name="locationQuantity${ProdLocItem.strSerialNumber}" onfocusout="calculateLocationAmount(1)" placeholder=""  autocomplete="off" value="${ProdLocItem.quantity}"/></td>
									           <td><select class="form-control" id="site_Name${ProdLocItem.strSerialNumber}" name="site_Name${ProdLocItem.strSerialNumber}">
									           		<option value="${ProdLocItem.siteId}$${ProdLocItem.siteName}">${ProdLocItem.siteId}$${ProdLocItem.siteName}</option>
									               </select>
									           </td>
									           <td><input type="text"   class="form-control" id="price_Aftertax${ProdLocItem.strSerialNumber}" name="price_Aftertax${ProdLocItem.strSerialNumber}" value="${ProdLocItem.rate}"/></td>
									           <td><input type="text"   class="form-control" id="total_Amount${ProdLocItem.strSerialNumber}" name="total_Amount${ProdLocItem.strSerialNumber}"/ value="${ProdLocItem.totalAmount}"></td>
									           <td>
									             <button type="button" class="btnaction" id="addLocationPlusBtn1" onclick="addLocationRow(1)"><i class="fa fa-plus"></i></button>
									             <button type="button" style="display:none;" class="btnaction" id="addLocationDeleteBtn1" onclick="deleteLocationRow(1)"><i class="fa fa-trash"></i></button>
									           </td>
									         </tr>  
									         </c:forEach>
									       </tbody>
									    </table>
									  </div>
									  </c:if>
												<!-- po product location details - end -->
									<div class="Mrgtop20"><a href="#" class="anchor-market" id="anchorMarket" onclick="openCalExp()">Calculate Expenditure</a></div>	
									<div class="clearfix"></div>		
									<div class="Mrgtop20 no-padding-left">
										 <div class="col-md-6 no-padding-left">
											 <div class="">
												  <div class="files_place">
												      <input type="file" id="file_select1" name="file" class="selectCount Mrgtop10" style="float:left;" accept="application/pdf,image/*" onchange="filechange(1)"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float:left;display:none;" onclick="filedelete(1)"><i class="fa fa-close"></i></button>
												  </div>
											      <div class="clearfix"></div>
											      <button type="button" id="Add" class="btn btn-success btn-xs Mrgtop10" style="display:none;" onclick="addFile()"><i class="fa fa-plus"></i> Add File</button>
											  </div>
										 </div>
								
									<!-- ***************************************************note for customer requirement***************************************** -->
										<div class="col-md-6 no-padding-left">
										                  <label class="control-label col-md-3 no-padding-left"><%= note %> <%= colon %></label>
															<div class="col-md-9 no-padding-left">
																<textarea name="Note" id="NoteId" class="text-control resize-vertical"></textarea> 
																
										                   </div>
										</div>
									</div>			
												<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
												<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
												<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
												<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
												<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
												<input type="hidden" name="creditTotalAmount" value="" id="creditTotalAmountId">
												<input type="hidden" name="VendorId" value="" id="vendorId">
												<input type="hidden" name="multiplesitesrowcount" id="multiplesitesrowcount">
																					 	
													<div class="clearfix"></div>
												<div class="col-md-6 text-right hidden-xs hidden-sm no-padding-right"></div>
												<div class="col-md-6 text-right hidden-xs hidden-sm no-padding-right">
														<div class="col-md-12 text-right no-padding-right" style="display:none;"><div class="col-md-5 text-left"><strong>PO Grand Total</strong></div><div class="col-md-1"><strong>:</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-md-5 no-padding-right"  >${totalAmount}</div></div>
														<div class="col-md-12 text-right no-padding-right" ><div class="col-md-5 text-left"><strong>Products Amount</strong></div><div class="col-md-1"><strong>:</strong></div><div class="col-md-5 finalAmountDiv no-padding-right" id="finalAmntDiv" name ="finalAmntDiv"  ><strong>${totalAmount}</strong></div></div>
														<div class="col-md-12 text-right no-padding-right" ><div class="col-md-5 text-left"><strong>Credit Note</strong></div><div class="col-md-1"><strong>:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-md-5 no-padding-left finalAmountDiv no-padding-right" >0</div></div>
														<div class="col-md-12 text-right no-padding-right"><div class="col-md-5 text-left"><strong>Total Invoice Amount</strong></div><div class="col-md-1"><strong>:</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-md-5 no-padding-right" >${totalAmount}</div></div>
													   <input type="hidden" id="1" value="${GetProductDetails.finalamtdiv}"/>
												</div>
											<!-- hidden for desktop devices -->
											<div class="col-xs-6 hidden-md hidden-lg text-left no-padding-right">
														<div class="col-xs-12 no-padding-right" style="display:none;"><div class="col-xs-12"><strong>PO Grand Total :</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-xs-12 no-padding-right"  >${totalAmount}</div></div>
														<div class="col-xs-12 no-padding-right"><div class="col-xs-12 no-padding-left"><strong>Products Amount :</strong></div><div class="col-xs-12 finalAmountDiv no-padding-right" id="finalAmntDiv" name ="finalAmntDiv"><strong>${totalAmount}</strong></div></div>
														<div class="col-xs-12 no-padding-right" ><div class="col-xs-12 no-padding-left"><strong>Credit Note:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-xs-12 no-padding-right finalAmountDiv">0</div></div>
														<div class="col-xs-12 no-padding-right"><div class="col-xs-12 no-padding-left"><strong>Total Invoice Amount :</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-xs-12 no-padding-right" >${totalAmount}</div></div>
													<input type="hidden" id="1" value="${GetProductDetails.finalamtdiv}"/>
												</div>
												</div>
												<!-- hidden for desktop devices -->	
												<div class="clearfix"></div>
													<div class="col-md-12 text-center center-block">
														<input type="button"  onclick="createInvoice()"  class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()" data-target="myModal" value="Submit" id="saveBtnId">
														<input type="button" class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()" data-target="myModal" value="Cal" id="saveBtnId">
												        <input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
														<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
													</div>
									  <!-- **************************************************************		 -->
									 <div class="modal fade" id="myModal" role="dialog">
									    <div class="modal-dialog">
									    
									      <!-- Modal content-->
									      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
									        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
									          <button type="button" class="close" data-dismiss="modal">&times;</button>
									          <h4 class="modal-title">Terms and Conditions</h4>
									        </div>
									        <form action="" method="post">
									        <div class="modal-body" style="height: 231px;overflow-y: scroll;">
									        <div class="container">
										<div class="row">
									
											<!-- <input type="hidden" name="count" value="1" /> -->
									        <div class="control-group" id="fields">
									        
									                       <div class="controls" id="profs"> 
									                <div class="input-append">
									             	<input type="hidden" id= "countOftermsandCondsfeilds" name="countOftermsandCondsfeilds" value="1">
									                    <div id="field margintopbtm"><input autocomplete="off" class="input form-control myinputstyles" id="termsAndCond1" name="termsAndCond1" type="text" data-items="8" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc";/><button id="b1" class="add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc"></span></button></div>
									                </div>
									            <br>
									            
									            </div>
									        </div>
										</div>
									</div>
									    
									        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
									        <div class="emailCC">
									        	<input type="checkbox" name="check1" id="ccemailcheckbox" onclick="dynInput(this);" style="position: absolute;margin-left: -522px;" value="" /><input type="text" id="" name="ccEmailId" class="form-control" style="float: left;margin-top: 28px;display:none"/><span style="position: absolute;margin-left: -501px;">(Optional)If you wan to add CC In emails.</span><br>
									     		
									        </div>
									        
									        <div class="subject-container" style="margin-top: 48px;">
									        <input type="checkbox" name="check2" id="sub-conmatiner"  style="position: absolute;margin-left: -522px;" value="" /><span style="position: absolute;margin-left: -501px;">Subject</span><br>
									      	<input type="text" name="subject" id="" class=" form-control1 sub-control" Placeholder="Please enter the subject" style="float: left;margin-top: 3px;display:none"/><br>
									        </div>
									        
									          <button type="button" class="btn btn-warning 	 form-submit" style="margin-right: 363px;width: 121px;margin-top: 67px;" data-dismiss="modal" onclick="createPO()">Submit</button>
									       																																	<!-- saveRecords(1)-->
									        </div>
									      </div>      
									      </form>
									      
									    </div>
									  </div>
										</div>		
											<!-- modal popup for marketing module -->
											 <!-- Modal -->
									<div id="modal-marketing" class="modal fade" role="dialog">
									  <div class="modal-dialog modal-lg" style="width:90%;">
									
									    <!-- Modal content-->
									    <div class="modal-content">
									      <div class="modal-header">
									        <button type="button" class="close" data-dismiss="modal">&times;</button>
									        <h4 class="modal-title text-center"><strong>Calculate Expenditure</strong></h4>
									      </div>
									      <div class="modal-body">
									       <div class="clearfix"></div>
												 <div class="col-md-12 marGinBottom">
												   <div class="col-md-4">
																	 <div class="form-group">
																	  <label class="control-label col-md-6">Expenditure For: </label>
																		<div class="col-md-6" >
																			<select class="form-control" id="poToId" name="expenditureFor">
																			   <option value="select">--Select--</option>
																			   <option value="SiteWise">Site</option>
																			   <option value="MultipleSite">Multiple Sites</option>
																			   <option value="LocationWise">Location Wise</option>
																			   <option value="BrandingWise">Branding Wise</option>
																			</select>
																		</div>
																	 </div>
																</div>
																<div class="col-md-4 class_selectsite" id="siteNameforSingleSite" style="display:none;">
																	 <div class="form-group">
																	   <label class="col-md-6">Select Site :</label>
																	   <div class="col-md-6 autocomplete" style="">
												                           <select id="singleSiteWiseSite" class="form-control" name="singleSiteWiseSite" onchange="siteWiseSitenameChange()"></select>
												                       </div>
																	 </div>
																</div>
															 <div class="col-md-4 class_selectsite1" style="display:none;">
																	 <div class="form-group">
																	  <label class="control-label col-md-6">Select Location: </label>
																		<div class="col-md-6" >
																			<select class="form-control" id="stateSelc">
																			   <option selected="true" disabled="disabled">--Select Location--</option>
																			 <!--   <option value="Ban">Banglore</option>
																			   <option value="Hyd">Hyderabad</option> -->
																			</select>
																		</div>
																	 </div>
																</div>
												 </div>
									 
									  <!-- Location wise -->
									    <div id="location_site" style="display:none">
									  
									         <div class="clearfix"></div>
										     <div class="table-responsive">
										     <table class="table" style="display:none" id="tempshowhide">
										       <thead>
										         <tr>
										         <th>Child Product Name</th>
										         <th class="widthonefifty">Hoarding Location</th>
										         <th>From Date</th>
										         <th>To Date</th>
										         <th class="widthonefifty">Time</th>
										         <th>Quantity</th>
										         <th>Amount</th>
										         <th>Total Amount</th>
										         <th>Site Name</th>
										           
										         </tr>
										       </thead>
										       <tbody id="locationAdd">
										    
										       </tbody>
										       <tbody id="locationGrandTotal">
										       	<tr class="totalMarketingAmount">
										           <td colspan="7">Grand Total</td>
										            <td id="grandTotal"></td>
										           <td></td>
										           
										          </tr>
										       </tbody>
										     </table>
										   </div>
										   <div class="text-center center-block">
										    <button type="button" class="btn btn-warning" data-dismiss="modal">close</button>
										    <button type="button" class="btn btn-warning" onclick="checkingLocationData()">ADD</button>
										   </div>
									   </div>
									   <!-- Location wise -->
									   
									   <!-- Single site -->
									    <div id="siteWiseSite"  style="display:none">  
									         <div class="clearfix"></div>
										     <div class="table-responsive">
										     <table class="table" id="singleSite">
										       <thead>
										         <tr>
										         <th>Child Product Name</th>
										         <th class="widthonefifty">Hoarding Location</th>
										         <th>From Date</th>
										         <th>To Date</th>
										         <th  class="widthonefifty">Time</th>
										         <th>Quantity</th>
										         <th>Amount</th>
										         <th>Total Amount</th>
										         <th>Site Name</th>	           
										         </tr>
										       </thead>
										       <tbody id="siteAdd">
										    
										       </tbody>
										       <tbody>
										       	<tr class="totalMarketingAmount">
										           <td colspan="7">Grand Total</td>
										            <td id="grandTotalsite"></td>
										            <td></td>
										           
										           
										          </tr>
										       </tbody>
										     </table>
										   </div>
										   <div class="text-center center-block">
										    <button type="button" class="btn btn-warning" data-dismiss="modal">close</button>
										    <button type="button" class="btn btn-warning" onclick="checkSingleSiteData()">ADD</button>
										   </div>
									   </div>
									   <!-- Single site -->
									   <!-- Site wise -->
									   <div class="clearfix"></div>
									   <div id="sitewise_Site" style="display:none;">
										     <div class="table-responsive">
										     <table class="table" id="expenditureTable">
										       <thead>
										         <tr>
										           <th>S.No</th>
										           <th class="widthonefifty">Child Product Name</th>
										           <th class="widthonefifty">Hoarding Location</th>
										            <th class="widthonefifty">Site</th>
										           <th>Quantity</th>
										           <th>From Date</th>
										           <th>To Date</th>
										           <th class="widthonefifty">Time</th>
										           <th>Amount</th>	          
										           <th>Total Amount</th>
										           <th style="width:120px;">Actions</th>										           
										         </tr>
										       </thead>
										       <tbody id="multisiteAdd">
										         <tbody>
										          <tr class="totalMarketingAmount">
										            <td></td>
										            <td colspan="8">Grand Total</td>
										            <td id="multiSiteGrandTotal"></td>
										            <td></td>
										          
										          </tr>
										          </tbody>
										       </tbody>
										     </table>
										   </div>
										   <div class="text-center center-block">
										    <button type="button" class="btn btn-warning" data-dismiss="modal">close</button>
										    <button type="button" class="btn btn-warning" onclick="checkMultipleSiteData()">save</button>
										   </div>
									   </div>
									   <!-- Site Wise -->
									   <!-- Branding Wise -->
									   <div id="branding_site" style="display:none;">
										     <div class="table-responsive">
										     <table class="table" id="BrandingWiseTable">
										       <thead>
										         <tr>
											         <th>Child Product Name</th>
											         <th class="widthonefifty">Hoarding Location</th>
											         <th>From Date</th>
											         <th>To Date</th>
											         <th class="widthonefifty">Time</th>
											         <th>Quantity</th>
											         <th> Amount</th>
											         <th>Total Amount</th>
											         <th>Site Name</th>
										         </tr>
										       </thead>
										       <tbody id="brandwiseAdd">
										       
										          
										       </tbody>
										       <tbody  id="brandwiseGrandTotal">
										         <tr class="totalMarketingAmount">
										           <td colspan="7">Grand Total</td>
										            <td id="grandTotalbrand"></td>
										            <td></td>	
										         </tr>
										       </tbody>
										     </table>
										   </div>
										   <div class="text-center center-block">
										    <button type="button" class="btn btn-warning" data-dismiss="modal">close</button>
										    <button type="button" class="btn btn-warning" onclick="checkingBrandingData()">ADD</button>
										   </div>
									   </div>
									  </div>
									 </div>
									</div>
								   </div>
								 </div>
							</form:form>
					  </div>
					 </div>
					</div>
					</div>
					</div>	
					<input type="hiiden" id="hiddenLocationText">	
				</div>
			</div>
		</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/marketing/jquery.timepicker.min.js"></script>  
        <script src="js/marketing/InwardsfromCreatePO.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
       	
		<script>		
		$(document).ready(function() {	
			$('[data-toggle="tooltip"]').tooltip();
			$(".up_down").click(function(){ 
				$(function() {
					for(i=1;i<=100;i++){
						$("#combobox"+i).combobox();    
						$( "#toggle").click(function() { $( "#combobox"+i).toggle();  });
						$( "#comboboxsubProd"+i).combobox();
						$("#comboboxsubSubProd"+i).combobox(); 
						$("#UnitsOfMeasurementId"+i).combobox(); }
										    
				}),
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			}); 
			for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
				$('.btn-visibilty'+i).prop('readonly', true).css('cursor','not-allowed');
				$('.bt-taxamout-coursor'+i).prop('readonly', true);
				$('disable-class'+i).closest('td').find('input').attr('disabled', 'disabled');
			 	$('#taxAmount'+i).prop('readonly', true);
				$('#UnitsOfMeasurementId'+i).prop('readonly', true);
				$('#price'+i).prop('readonly', true);
			    $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').removeClass('hide'); 
			}
		});
		$(document).ready(function() {	
			$(".up_down").click(function(){ 
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			}); 
			$("#UnitsOfMeasurementId1").select(function(){
			        alert("Text marked!");
			});
		});
		
			 	
		 	$('#ccemailcheckbox').on('click', function(){
				$('.form-control').show();
				
			});
		 	$('#sub-conmatiner').on('click', function(){
				$('.form-control1').show();
				
			});
		 	
		 	jQuery(document).ready(function($) {
		 	   // STOCK OPTIONS
		 		$('#sub-conmatiner').click(function(){debugger;
		 			if ($(this).is(':checked'))
		 	    $(this).next('form-control1').show();
		 	else
		 	    $(this).next('form-control1').hide();
		 		})
		 	});
		 	
		 	
		 	$(document).on('select','#UnitsOfMeasurementId1',function(){
		 	    alert("You clicked the element with and ID of 'test-element'");
		 	});
		 	
	/* ************* Method for new text feild in model popup ************** */
	
	
  $(document).ready(function(){
    var next = 1;
    $(".add-more").click(function(e){
        e.preventDefault();
        var addto = "#termsAndCond" + next;
        var addRemove = "#termsAndCond" + (next);
        next = next + 1;
        var newIn = '<input autocomplete="off" class="input form-control myinputstyles" style=" display: inline !important;width: 75% !important;border-radius: 0px !important;margin-bottom: 10px;border: none;box-shadow: none;border-bottom: 2px solid #ccc;" id="termsAndCond' + next + '" name="termsAndCond' + next + '" type="text">';
        var newInput = $(newIn);
        var removeBtn = '<button id="remove' + (next - 1) + '" class="remove-me mybtnstyles" ><span class="glyphicon glyphicon-remove" style="color:red;"></span></button></div><div id="field">';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field" + next).attr('data-source',$(addto).attr('data-source'));
        $("#countOftermsandCondsfeilds").val(next); 
        var countempty= $("#countOftermsandCondsfeilds").val(next);  
        if(countempty == 1){debugger;
        	 $("#countOftermsandCondsfeilds").val(next)==1;
        }
      $('.remove-me').click(function(e){
         e.preventDefault();
         var fieldNum = this.id.charAt(this.id.length-1);
         var fieldID = "#field" + fieldNum;
            $(this).remove();
            $(fieldID).remove();
       });
     });
  });
	
	$(function() {
		  $(".ExpiryDate").datepicker({ 
			  dateFormat: 'dd-M-y',
			  minDate:0,
		      changeMonth: true,
	          changeYear: true
		  });
	});
	$("#SiteWiseTime").timepicker({
		 timeFormat: 'h:mm p',
		    interval: 10,
		    minTime: '00.00AM',
		    maxTime: '11.59PM',
		    startTime: '00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
	});
  $("#poToId").change(function(){
	  var PoId=$(this).val();
	  var toHiddenLocation=$("#stateSelc").val();
	  $("#hiddenLocationText").val(toHiddenLocation);
	  if(PoId=="SiteWise"){
		  $("#sitename").text($("#siteName").val());
		  $(".class_selectsite").show(); 
		  $("#siteNameforSingleSite").show();
		  $("#sitewise_Site").hide();
		  $(".class_selectsite1").hide();
		  $("#branding_site").hide();
		  $("#location_site").hide();
		  if($("#singleSiteWiseSite").val()!=0){
			  siteWiseSitenameChange();
			  $("#siteWiseSite").show();			  
		  }
		  
	  }
     if(PoId=="MultipleSite"){
    	  $(".class_selectsite1").hide();
    	  $("#siteWiseSite").hide();
    	  $("#siteNameforSingleSite").hide();
    	  $("#sitewise_Site").show();
    	  $(".class_selectsite").hide();
    	  $("#branding_site").hide();
    	  $("#location_site").hide();
    	  
    	  getLOcation();
    	  loadMultiSiteTable();
	  } 
      if(PoId=="BrandingWise"){
    	  $("#branding_site").show();
    	  $("#siteNameforSingleSite").hide();
    	  $("#siteWiseSite").hide();
    	  $("#sitewise_Site").hide();
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
    	  $("#location_site").hide();
    	  
    	callingPerBrandwiseCalculation("", "BrandingWise");
	  }
      if(PoId=="select"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
    	  $("#siteNameforSingleSite").hide();
    	  $("#siteWiseSite").hide();
    	  $("#sitewise_Site").hide();
    	  $("#branding_site").hide();
    	  $("#location_site").hide();
	  }
      if(PoId=="LocationWise"){
    	  $("#stateSelc").val($("#hiddenLocationText").val());
    	  $(".class_selectsite1").show();
    	  $(".class_selectsite").hide();
    	  $("#siteWiseSite").hide();
    	  $("#sitewise_Site").hide();
    	  $("#branding_site").hide();
    	  $("#siteNameforSingleSite").hide();
    	  if($("#stateSelc").val()!=0){
    		  $("#location_site").show();
		  }
    	  getLOcation();
	  }
  });
  
  $("#stateSelc").change(function(){
	  var stateId=$(this).val();
	  CallingperSiteCalculation(stateId, "MultipleSite");
  });
  
 </script>	
</body>
</html>
