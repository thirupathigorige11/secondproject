<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%
	//Loading Indent Receive Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String serialNumber = resource.getString("label.serialNumber");
	String basicAmount = resource.getString("label.basicAmount");
	String taxPer = resource.getString("label.tax");
	String taxAmount = resource.getString("label.taxAmount");
	String amountAfterTax = resource.getString("label.amountAfterTax");
	String otherOrTransportCharges = resource.getString("label.otherOrTransportCharges");
	String taxOnOtherOrTransportCharges = resource.getString("label.taxOnOtherOrTransportCharges");
	String otherOrTransportChargesAfterTax = resource.getString("label.otherOrTransportChargesAfterTax");
	String sNumber = resource.getString("label.sNumber");
	String conveyanceCharges = resource.getString("label.conveyanceCharges");
	String conveyanceAmount = resource.getString("label.conveyanceAmount");
	String trasportInvoice = resource.getString("label.trasportInvoice");
	String GSTAmount = resource.getString("label.GSTAmount");
	String chargesAmountAfterTax = resource.getString("label.chargesAmountAfterTax");
	String conveyanceTax = resource.getString("label.conveyanceTax");
	String actions = resource.getString("label.actions");
	String totalAmount = resource.getString("label.totalAmount");	
	
	
	
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="CacheClear.jsp" />  
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
	        <title>Sumadhura-IMS</title>
	        <link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
	        <style>
			.finalAmountDiv{ color: #f0ad4e; font-size: 24px; }
			.form-group label{ text-align: left }
			.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
			.table-new tbody tr td {border-top:0px !important;}
			.table-new tbody tr td:first-child, .table-new thead tr th:first-child {min-width:23px;width:52px;}
			.input-group-addon{border:1px solid #ccc !important;}
			.Btnbackground{
			background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
			}	
       </style>
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

<script>

//Create DIV element and append to the table cell
var i = 1;

function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	//debugger;
	
     var vfx = fldLength;     
     //removing space
     tableColumnName=tableColumnName.trim();
     var snoDivIdColumn =  "<%= serialNumber %>";
     snoDivIdColumn = formatColumns(snoDivIdColumn);
     //alert(snoColumn);
     

	 
	 var basicAmountColumn =  "<%= basicAmount %>";
	 basicAmountColumn = formatColumns(basicAmountColumn);
	 //alert(basicAmountColumn);	 
	 
	 var taxAmountColumn =  "<%= taxAmount %>";
	 taxAmountColumn = formatColumns(taxAmountColumn);
	 //alert(taxAmountColumn);
	 
	 var amountAfterTaxColumn =  "<%= amountAfterTax %>";
	 amountAfterTaxColumn = formatColumns(amountAfterTaxColumn);
	 //alert(amountAfterTaxColumn);
	 
	 var otherOrTransportChargesColumn =  "<%= otherOrTransportCharges %>";
	 otherOrTransportChargesColumn = formatColumns(otherOrTransportChargesColumn);
	 //alert(otherOrTransportChargesColumn);
	 
	 var taxOnOtherOrTransportChargesColumn =  "<%= taxOnOtherOrTransportCharges %>";
	 taxOnOtherOrTransportChargesColumn = formatColumns(taxOnOtherOrTransportChargesColumn);
	 //alert(taxOnOtherOrTransportChargesColumn);
	 
	 var otherOrTransportChargesAfterTaxColumn =  "<%= otherOrTransportChargesAfterTax %>";
	 otherOrTransportChargesAfterTaxColumn = formatColumns(otherOrTransportChargesAfterTaxColumn);
	 //alert(otherOrTransportChargesAfterTaxColumn);
	 
	 var totalAmountColumn =  "<%= totalAmount %>";
	 totalAmountColumn = formatColumns(totalAmountColumn);
	 //alert(totalAmountColumn);
	 
<%-- 	 var hsnCodeColumn =  "<%= hsnCode %>";
	 hsnCodeColumn = formatColumns(hsnCodeColumn); --%>
	 
	 
<%-- 	 var expiryDate=  "<%= expiryDate %>";
	 expiryDate = formatColumns(expiryDate);
	 //alert(hsnCodeColumn); --%>
	 
<%-- 	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn); --%>
	 //alert(actionsColumn);

     if(tableColumnName == snoDivIdColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        //snoDiv.setAttribute("class", style);
        //snoDiv.setAttribute("className", style);
        cell.appendChild(snoDiv);
    }
    else  if(tableColumnName == taxColumn) {
    		var dynamicSelectBoxId = "TaxId"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("onchange", "calculateTaxAmount("+vfx+")");
    	    cell.appendChild(div);
    	    
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "--Select--";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);
    	    var option = "";
    		<% 
    			Map<String, String> gstTaxCal = (Map<String, String>)request.getAttribute("gstMap");
    			for(Map.Entry<String, String> tax : gstTaxCal.entrySet()) {
				String val = tax.getKey()+"$"+tax.getValue();
			%>
				option = document.createElement("option");
	    	    option.text = "<%= tax.getValue() %>";
	    	    option.value = "<%= val %>";
	    	    div.appendChild(option);
    		<% 
				} 
			%>    	    
    	    cell.appendChild(div);
    	}
    	else if(tableColumnName == quantityColumn) {   		
    		
    		cell.className  = "s-50";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);		    
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}

}




/* ******************* This section represents the Second grid append to Other trasport charges************** */

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	debugger;
	
     var vfx = fldLength;     
	//removing space
	tableColumnName=tableColumnName.trim();
	
     var snColumn =  "<%= sNumber %>";
     snoColumn = formatChargesColumns(snColumn);
     //alert(snoColumn);
     
	 
	 var conveyanceChargesColumn =  "<%= conveyanceCharges %>";
	 conveyanceChargesColumn = formatChargesColumns(conveyanceChargesColumn);
	 //alert(taxAmountColumn);
	 
	 var conveyanceAmountColumn =  "<%= conveyanceAmount %>";
	 conveyanceAmountColumn = formatChargesColumns(conveyanceAmountColumn);
	 //alert(amountAfterTaxColumn);
	 
	  var taxChargeColumn =  "<%= conveyanceTax %>";
	  taxChargeColumn = formatChargesColumns(taxChargeColumn);
	 
	  var GSTAmountColumn =  "<%= GSTAmount %>";
	  GSTAmountColumn = formatChargesColumns(GSTAmountColumn);
		 
		 
	var chargesAmountAfterTaxColumn =  "<%= chargesAmountAfterTax %>";
	chargesAmountAfterTaxColumn = formatChargesColumns(chargesAmountAfterTaxColumn);
	 
	var trasportInvoiceColumn =  "<%= trasportInvoice %>";
	trasportInvoiceColumn = formatChargesColumns(trasportInvoiceColumn);
			 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatChargesColumns(actionsColumn);
	 //alert(actionsColumn);
	
  //   alert("snoColumn"+snoColumn);
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
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId); 
    	    div.setAttribute("onchange", "changeconveyance("+vfx+")"); 
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Please select Transport"; 
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
    	    /*  $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			}); */
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    
		    div.setAttribute("placeholder", 'Please enter Amount');
		    cell.appendChild(div);
   		}
    	   	
    	else if(tableColumnName == taxChargeColumn) {
    		var dynamicSelectBoxId = "GSTTax"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id",tableColumnName+vfx);
    	    div.setAttribute("onchange", "calculateGSTTaxAmount("+vfx+")");
    	    cell.appendChild(div);
    	    
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Transport Tax";
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
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("placeholder", 'Amount After Tax');
		    cell.appendChild(div);
   		}   
   
    	else if(tableColumnName == trasportInvoiceColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", 'Transport Invoice Number');
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   
   
  
        	else if(tableColumnName == actionsColumn) {        	
			var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewChargesItemBtn");
		    div2.setAttribute("id", "addNewChargesItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendChargesRow()");
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
		    div1.setAttribute("onclick", "deleteRow(this, "+vfx+")");
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}
    }
}

/* ********************************************************** */

</script>
<div class="container body" id="mainDivId">
			<div class="main_container" id="main_container">
				<div class="col-md-3 left_col" id="left_col">
					<div class="left_col scroll-view">             
						<div class="clearfix"></div>
						<jsp:include page="SideMenu.jsp" /> 
					</div>
					</div>
					<jsp:include page="TopMenu.jsp" />  
					<!-- page content -->
					<div class="right_col" role="main">
						<div>
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Inwards</a></li>
								<li class="breadcrumb-item active">Inwards from other site</li>
								<li class="breadcrumb-item active">Invoice ID</li>
							</ol>
						</div>

	                   <div class="">	
		                <form:form modelAttribute="issueToOtherSiteModelForm"  id = "doInventoryUpdateFormId" class="form-horizontal"   method="POST" >
			              <c:forEach var="getInvoiceDetails" items="${requestScope['listOfIssueToOtherSiteInwardList']}"> 
	                         <div class="container border-inwards">	
							  <div class="col-md-12">
							   <div class="col-md-4">
							   <div class="form-group">							
									<label for="invoice-number" class="col-md-6">Invoice Number :</label>
									<div class="col-md-6">
									   <form:input path="invoiceNumber" id="InvoiceNumberId" class="form-control  marginRight" value="${getInvoiceDetails.invoiceNumber}" readonly="true"/> 
                                                     <input type="hidden" name="senderSiteName" id="fromSite" value="${getInvoiceDetails.fromSite}">
			                                         <input type="hidden" name="senderSiteId" id="fromSiteId" value="${getInvoiceDetails.fromSiteId}">
									</div>
								</div>							  
							  </div>
							  <div class="col-md-4">
								 <div class="form-group">								
									<label for="invoice-date" class="col-md-6 col-xs-12"> Invoice Date : </label>
									<div class="col-md-6 col-xs-12 input-group">
									  <form:input type="text" path="invoiceDate" id="InvoiceDateId" class="form-control marginRight" value="${getInvoiceDetails.invoiceDate}"  readonly="true"/> 
									  <label class="input-group-addon btn" for="InvoiceDateId"> <span class="fa fa-calendar"></span> </label>
									</div>
									</div>
								</div>
								<div class="col-md-4">
								 <div class="form-group">
									<label for="vendor-name" class="col-md-6">Vendor Name : </label>
									<div class="col-md-6">
									   <form:input path="vendorName" id="VendorNameId" class="form-control"  value="${getInvoiceDetails.vendorName}" readonly="true"/>
								       <input type="text" id="VendorId" name = "VendorId"  style="display:none;"class="form-control"  value="${getInvoiceDetails.vendorId}" readonly="true"/>
									</div>
								
								</div>
								</div>
								<div class="col-md-4">
									 <div class="form-group">										
										<label for="gst-in" class="col-md-6" >GSTIN : </label>
										<div class="col-md-6">
										   <form:input path="gsinNo" id="GSTINNumber" class="form-control " value="${getInvoiceDetails.gsinNo}"  readonly="true"/> 
								        </div>
									</div>
								</div>
								<div class="col-md-4">
								  <div class="form-group">
									  <label for="vendor-address" class="col-md-6" >Vendor Address : </label>
									 <div class="col-md-6">
									  <form:input path="vendorAdress" id="VendorAddress" value="${getInvoiceDetails.vendorAdress}" class="form-control" readonly="true"/> 
									<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${site_id}">
									</div>
							      </div>										
								</div>
								<div class="col-md-4">
									 <div class="form-group">
									  <label for="state" class="col-md-6">State : </label>
									  <div class="col-md-6">
									   <form:select path="state" id="state" class="form-control">
								              <form:option value="">---Select--</form:option>
									          <form:option value="1">Local</form:option>
									          <form:option value="2">Non Local</form:option>
										</form:select>	
									  </div>
									 </div>
								</div>
								
								<%-- <form:input path="state" id="state" class="form-control col-sm-2 marginRight" readonly="true" value="${getInvoiceDetails.state}"/> --%>
								 <div class="col-md-4">
							            <div class="form-group">
										<label for="vehicle-no" class="col-md-6">Vehicle No : </label>
										<div class="col-md-6">
										  <form:input path="vehileNo" name="vehileNo" id="vehileNoId" class="form-control" value="${getInvoiceDetails.vehileNo}" autocomplete="off"/> 
										</div>
									</div>
						          </div>
								  <div class="col-md-4">
								   <div class="form-group">
									   <label for="transporter-name" class="col-md-6">Transporter Name : </label>
									   <div class="col-md-6">
									   <input type="text"  name="transporterName1" id="transporterNameIdId" onkeyup="return populateData();" class="form-control" value=""> 
			 						<input type="hidden"  name="transporterName" id="transporterNameId"  class="form-control" value="">
									<%-- <form:select path="transporterName" id="transporterNameId" name="transporterName" class="form-control GSTClass " value="5%">
									<form:option value="">--select--</form:option>
						    		<%	
						    			Map<String, String> transportorMap1 = (Map<String, String>)request.getAttribute("transportorMap");
						    			for(Map.Entry<String, String> tax1 : transportorMap1.entrySet()) {
						    				String transportorId = tax1.getKey()+"$"+tax1.getValue();
									%>
										<form:option data-toggle="tooltip" title="<%= tax1.getValue() %>" value="<%= transportorId %>"><%= tax1.getValue() %></form:option>
						    		<% } %> 
									</form:select> --%>
										<%-- <form:input path="transporterName" name="transporterName" id="transporterNameId" value="${getInvoiceDetails.transporterName}" class="form-control col-sm-2 marginRight" />  --%>
									   </div>
								   </div>
								  </div>
							     <div class="col-md-4">
							       <div class="form-group">
										<label for="received-date" class="col-md-6 col-xs-12">Received Date : </label>
									    <div class="col-md-6 col-xs-12 input-group">
									      <form:input path="receivedDate" name="receivedDate" id="receivedDate" class="form-control readonly-color" value="${getInvoiceDetails.receivedDate}" autocomplete="off"  readonly="true"/> 
										   <label class="input-group-addon btn" for="receivedDate"> <span class="fa fa-calendar"></span> </label>
										</div>
							        </div>
							     </div>
								 <div class="col-md-4">
									 <div class="form-group">
										<label for="received-date" class="col-md-6">Indent Number : </label>
										<div class="col-md-6">
										 <form:input path="siteWiseIndentNo" name="siteWiseIndentNo" id="siteWiseIndentNo" class="form-control" value="${getInvoiceDetails.siteWiseIndentNo}"  readonly="true" /> 
										 <form:hidden path="indentNumber" name="indentNumber" id="indentNumber" class="form-control" value="${getInvoiceDetails.indentNumber}"  readonly="true" /> 
										</div>
									 </div>
								 </div>				
								<div class="col-md-4">
									 <div class="form-group">
										 <div class="col-md-6">
										  <input  type = "text" id="intrmidiatetEntryId" name="intrmidiatetEntryId" style="display: none;"  class="form-control" autocomplete="off" readonly="true" value="${getInvoiceDetails.intrmidiatetEntryId}"/>
										 </div>
									</div> 
								</div>
								<input type="hidden" name="siteId" id="siteIdId"  readonly="true" class="form-control" value="${siteId}"/>
								<input type="hidden" id="allSiteIds" value="${Allsites}"/>
								</div> 
		                  </div> 
             </c:forEach> 
		   <div class="clearfix"></div>
	         <div class="Mrgtop20">
	                  <!-- PRODUCT DETAILS -->
			       <div class="table-responsive">   
						<table id="doInventoryTableId" class="table table-new inward-tbl tbl-width">
						<thead class="cal-thead-inwards">
						 <tr>
							<th>S NO</th>
		    				<th>Product</th>
		    				<th>Sub Product</th>
		    				<th>Child Product</th>    								
		    				<th>Unit of Measurement</th>
		    				<th>Quantity</th> 
		    				<th> Price</th>
		    				<th> BasicAmount</th> 
		    				<!-- <th> TaxPer </th> -->
		    				<th> HSNCode </th>
		    				<!-- <th>Tax Amount</th>
		    				<th>AmountAfterTax</th> -->
		    				<th> Other Or Transport Charges</th>
		    				<th>Tax On Other Or Transport Charges</th>
		    				<th>Other Or Transport Charges After Tax </th>
		    				<th>Total Amount </th>
							<th>Expire Date</th>
							<th>Issued Time</th>
						</tr>
						</thead>
						  <tbody class="tbl-fixedheader-tbody">
						    <c:forEach var="GetProductDetails" items="${requestScope['listOfIssueToOtherSiteProductDetails']}" step="1" begin="0">	
			        	 	 <tr class="productTableRow" id="tr-class${GetProductDetails.strSerialNo}">
			        	  	<td>						
						       <div id="snoDivId${GetProductDetails.strSerialNo}">${GetProductDetails.strSerialNo}</div> 
							</td>
							<td>
							   <input type="text"  id="product${GetProductDetails.strSerialNo}"  name="product${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.product}"/>
							</td>
							<td>
								<input type="text"  id="subProduct${GetProductDetails.strSerialNo}"  name="subProduct${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.subProduct}"/>
							</td>
							<td>
							    <input type="text"  id="childProduct${GetProductDetails.strSerialNo}"  name="childProduct${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="<c:out value="${GetProductDetails.childProduct}"/>"/>
							<input type="hidden"  id="ChildProduct${GetProductDetails.strSerialNo}"  name="ChildProduct${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="<c:out value="${GetProductDetails.childProductId}$${GetProductDetails.childProduct}"/>"/>
							</td>
							
							<td>
								<%-- <form:input path="unitsOfMeasurement"  class="form-control" readonly="true" value="${GetProductDetails.unitsOfMeasurement}"/> --%>
							   <input type="text"  id="unitsOfMeasurement${GetProductDetails.strSerialNo}"  name="unitsOfMeasurement${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.unitsOfMeasurement}"/>
							<input type="hidden"  id="UnitsOfMeasurement${GetProductDetails.strSerialNo}"  name="UnitsOfMeasurement${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.measurmentId}$${GetProductDetails.unitsOfMeasurement}"/>
							</td>
							<td >
								<%-- <form:input path="quantity"    class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.quantity}"/> --%>
		                 	
		                 	<input type="text"  id="quantity${GetProductDetails.strSerialNo}"  name="quantity${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.quantity}"/>
		                 	<%-- 	<form:input path="ProductAvailability" id="ProductAvailabilityId1" readonly="true" class="form-control"  value="${GetProductDetails.ProductAvailability}"/> --%> 
							<input type="hidden"  id="groupId${GetProductDetails.strSerialNo}"  name="groupId${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.groupId1}"/>
							<input type="hidden"  id="avalBOQQty${GetProductDetails.strSerialNo}"  name="avalBOQQty${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.availbleQuantity}"/>
							<input type="hidden"  id="BOQQty${GetProductDetails.strSerialNo}"  name="BOQQty${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.boqQuantity}"/>
							
							</td>					
						
							<td>
								<%-- <form:input path="price"    class="form-control" readonly="true" value="${GetProductDetails.price}"/> --%>
						       <input type = "text" id="price${GetProductDetails.strSerialNo}"  name="price${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.price}"/>
						
							</td>
						  	<td> 
						  	 <input type = "text" id="BasicAmountId${GetProductDetails.strSerialNo}"  name="BasicAmountId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.basicAmount}"/>
								<%-- <form:input path="basicAmount"  id="BasicAmountId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.basicAmount}"/> --%>
							</td>  
							
							<%-- <td>
								<form:input path="tax"  readonly="true" class="form-control" autocomplete="off" value="${GetProductDetails.tax}"/>
							</td> --%>
							<td>
								<input type="text"  id="hsnCode${GetProductDetails.strSerialNo}"  name="hsnCode${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
								<%-- <form:input path="hsnCode"  readonly="true" class="form-control"  onkeydown="appendRow()" autocomplete="off"  value="${GetProductDetails.hsnCode}"/> --%>
							</td>
							<%-- <td>
								<form:input path="taxAmount" id="TaxAmountId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
							</td>
							<td>
								<form:input path="amountAfterTax" id="AmountAfterTaxId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
							</td> --%>
							<td>
								<%-- <form:input path="otherOrTransportCharges" id="OtherOrTransportChargesId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/> --%>
								<input type = "text" id="otherOrTransportCharges${GetProductDetails.strSerialNo}"  name="otherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/>
							</td>
							<td>
								<%-- <form:input path="taxOnOtherOrTransportCharges" id="TaxOnOtherOrTransportChargesId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/> --%>
								<input  type="text" id="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  name="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true"  class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/>
							</td>
							<td>
								<input type="text" id="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  name="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/>
								<%-- <form:input path="otherOrTransportChargesAfterTax" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/> --%>
							</td>
							<td>
							<input type="text"  id="totalAmount${GetProductDetails.strSerialNo}"  name="totalAmount${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/>
								<%-- <form:input path="totalAmount"  id="TotalAmountId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/> --%>
							</td>		
							<td>
							<%-- <form:input path="expireDate" id="expireDate${GetProductDetails.strSerialNo}" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.expireDate}"/> --%>
							<input type="text"  id="expireDate${GetProductDetails.strSerialNo}"  name="expireDate${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.expireDate}"/>
							
							
							
							
						 
							</td>  
							<td>
							<input type="text"  id="issueTime${GetProductDetails.strSerialNo}"  name="issueTime${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.issuedTime}"/>
							<input  type = "hidden" id="productId${GetProductDetails.strSerialNo}" name="productId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.productId}"/>
							<input  type = "hidden" id="subProductId${GetProductDetails.strSerialNo}" name="subProductId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.subProductId}"/>
							<input  type = "hidden" id="childProductId${GetProductDetails.strSerialNo}" name="childProductId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.childProductId}"/>
							<input  type = "hidden" id="measurmentId${GetProductDetails.strSerialNo}" name="measurmentId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.measurmentId}"/>
							<input type="hidden" name="strIssueType" value= "${requestScope['strIssueType']}" id="strIssueType" >
							<input  type = "hidden" id="BasicAmountId${GetProductDetails.strSerialNo}" name="BasicAmountId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.basicAmount}"/>
							<input  type = "hidden" id="dcNumber${GetProductDetails.strSerialNo}" name="dcNumber${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.dcNumber}"/>
						 
							</td>  
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
	    </div>
<!-- 	*********************************** -->
			<div class="clearfix"></div>
			<div class="">
			<div class="table-responsive Mrgtop20">
			<table id="doInventoryChargesTableId" class="table table-new inward-tbl tbl-width-medium" >
				<thead class="cal-thead-inwards">
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
			    <tbody class="tbl-fixedheader-tbody">
			     <tr id="chargesrow1">
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="Conveyance1" id="Conveyance1" class="form-control" onchange="changeconveyance(1)">
							<form:option value="">Please select Transport</form:option>
					    		<%	
					    			Map<String, String> conveyanceCharges1 = (Map<String, String>)request.getAttribute("chargesMap");
					    			for(Map.Entry<String, String> tax : conveyanceCharges1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
					    		<% } %>
						</form:select>
					</td>
					<td>
					
						<form:input path="ConveyanceAmount1" id="ConveyanceAmount1"   type="number"  placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off"/>
					</td>
					<td>
						<form:select path="GSTTax1" id="GSTTax1" class="form-control GSTClass"  onchange="calculateGSTTaxAmount(1)">
							<form:option value="">Transport Tax</form:option>
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
						<form:input path="GSTAmount1" id="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
						<td>
						<form:input path="AmountAfterTax1" type="number"  id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
				
					<td>
						<form:input path="TransportInvoice1"  type="text" id="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					<td>
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" style="display: none;" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				    </td>
				</tr>
			    </tbody>
			</table>
		 </div>
		</div>
		<div class="container ">
			<div class="col-md-6 Mrgtop20">
			 <label class="control-label col-md-3">Note:</label>
			<div class="col-md-8">
				<form:textarea path="Note" id="NoteId" class="form-control resize-vertical"/>
			</div>
			</div>
		
			<%-- <div class="col-sm-6"><div class="comment" style="text-align:left;"><span class="h4" style="margin-right:20px;">Note:</span><form:textarea path="Note" id="NoteId" class="form-control"/></div></div> --%>
			<div class="col-md-6 Mrgtop20">
			 <div class="col-md-12"><span class="h4" ><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv"></span></div>
			</div>
		</div>
		<input type="hidden" name="numbeOfRowsToBeProcessed"  id="countOfRows">
		<input type="hidden" name="numbeOfTransRowsToBeProcessed"  id="countOfTransRowsRows">
		<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveBtnId">
		<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveChargesBtnId">
		<input type="hidden" name="ttlAmntForIncentEntryId"  id="ttlAmntForIncentEntryId" >
   </form:form>
</div>
<div class="col-md-12 text-center center-block Mrgtop20" >
				
					<input class="btn btn-warning btn-bottom" type="button"  value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
				
						<input class="tblchargesbtn btn btn-warning btn-bottom "   type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')">
						
				
			
</div>
</div>
			
					<!-- /page content -->        
			
</div> 
</div>
</div>
</div>

		
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/issueToOtherSite_NonReturnableView.js" type="text/javascript"></script>
		
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			
			
			
		 	/* $('#Conveyance1').change(function() {
				if ($(this).val() == "999$None"){
				  // Updating text input based on selected value
				  $(".noneClass").val(0);
				  $(".GSTClass").val( '1$0%');
				  }else{
					  $(".noneClass").val( " ");
				  }
				}); */ 
				
				//changing conveyance written by thirupathi	
			 	function changeconveyance(btn){
					debugger;
			 		if ($("#Conveyance"+btn).val() == "999$None"){
						  // Updating text input based on selected value
						  $("#ConveyanceAmount"+btn).val("0");
						  $("#ConveyanceAmount"+btn).attr("readonly", "true");
						  $("#GSTTax"+btn).val("1$0%");
						  $("#GSTTax"+btn).attr("readonly", "true");
						  $("#GSTAmount"+btn).val("0");
						  $("#GSTAmount"+btn).attr("readonly", "true");
						  $("#AmountAfterTax"+btn).val("0");
						  $("#AmountAfterTax"+btn).attr("readonly", "true");						  
						  }else{
							  $("#ConveyanceAmount"+btn).val(" ");
							  $("#ConveyanceAmount"+btn).removeAttr("readonly");
							  $("#GSTTax"+btn).val("");
							  $("#GSTTax"+btn).removeAttr("readonly");
							  $("#GSTAmount"+btn).val("");
							 // $("#GSTAmount"+btn).removeAttr("readonly");
							  $("#AmountAfterTax"+btn).val("");
							 // $("#AmountAfterTaxx"+btn).removeAttr("readonly");
						  }
			 	}
				
				
				
				
				
		 	//append row to the HTML table
		 	function appendRow() {
		 		debugger;


		 		var pressedKey = window.event;

		 		//alert(pressedKey.keyCode);
		 		if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {

		 			btn = pressedKey.target || pressedKey.srcElement;
		 			var buttonId = btn.id;

		 			if(buttonId.includes("addNewItemBtnId")) {
		 				document.getElementById("hiddenSaveBtnId").value = "";
		 			}

		 			var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
		 			//alert(hiddenSaveBtn);

		 			var tbl = document.getElementById("doInventoryTableId");

		 			/* 31-AUG-17  var valStatus = validateRowData();
		 		    //alert(valStatus);

		 		    if(valStatus == false) {
		 		    	return false;
		 		}*/

//		 			calcuLateFinalAmount();

		 			if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {

		 				var	row = tbl.insertRow(tbl.rows.length);

		 				var i;

		 				var tableColumnName = "";
		 				var columnToBeFocused = "";
		 				var rowNum = getLastRowNum();

		 				document.getElementById("addNewItemBtnId"+rowNum).remove();

		 				rowNum = rowNum+1;

		 				for (i = 0; i < tbl.rows[0].cells.length; i++) {

		 					var x = document.getElementById("doInventoryTableId").rows[0].cells;
		 					tableColumnName = x[i].innerText;
		 					tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		 					tableColumnName = tableColumnName.replace(/\./g,'');
		 					columnToBeFocused = x[1].innerText;
		 					columnToBeFocused = columnToBeFocused.replace(/ /g,'');
		 					//alert("Table Column Name = "+tableColumnName.replace(/ /g,''));	    	
		 					createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);	    	
		 				}
		 				$(".tablDisply").hide();
		 				var lastDiv = getLastRowNum();
		 				//alert(lastDiv);

		 				document.getElementById("Product"+lastDiv).focus();
		 			}
		 		}
		 	}
		 	function saveRecords(saveBtnClicked) {debugger;

		 		document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;

		 		var valStatus = appendRow();

		 		if(valStatus == false) {
		 			return;
		 		}
		 		//calculateOtherCharges();
		 		var otherChrgs = document.getElementById("Conveyance1").value;

		 		if(otherChrgs == "" || otherChrgs == '' || otherChrgs == null) {
		 			alert("Please enter Other Conveyance or None");
		 			document.getElementById("OtherChargesId").focus();
		 			return;
		 		}
		 		var transporterName = document.getElementById("transporterNameIdId").value;
	    		if(transporterName == "" || transporterName == null || transporterName == '') {
	    			alert("Please select the transportorName.");
	    			document.getElementById("transporterNameIdId").focus();
	    			return false;
	    		}
		 		if(otherChrgs == "." || otherChrgs == "0." || otherChrgs == ".0" || otherChrgs == ".00") {
		 			alert("Please enter Other Chargers.");
		 			document.getElementById("OtherChargesId").focus();
		 			return;
		 		}
		 		else {
		 			//document.getElementById("calculateBtnId").click();
		 		}
				
		 		var state=$("#state").val();
		 		var ReceivedDate =$("#receivedDate").val();
		 		if(state==""){
		 			alert("Please select state.");
		 			$("#state").focus();
		 			return false;
		 		}
		 		if(ReceivedDate==""){
		 			alert("Please select receive date.");
		 			$("#receivedDate").focus();
		 			return false;
		 		}
		 		var valQty=validateQtyOnSubmit();
		 		if(valQty==false){
		 			return false;
		 		}
		 		var canISubmit = window.confirm("Do you want to Update?");

		 		if(canISubmit == false) {
		 			return false;
		 		}


		 		//alert("before saving "+getAllProdsCount());
		 		document.getElementById("saveBtnId").disabled = true;	
		 		document.getElementById("countOfRows").value = getAllProdsCount();
		 		document.getElementById("countOfTransRowsRows").value = getChargeTotalSNOS();
		 		document.getElementById("doInventoryUpdateFormId").action = "doinwardsFromOtherSite.spring";
		 		document.getElementById("doInventoryUpdateFormId").method = "POST";
		 		document.getElementById("doInventoryUpdateFormId").submit();
		 	}
		 	
		 	function getChargeTotalSNOS() {

		 		var allElements = document.getElementsByTagName("*");
		 		var snos = "";
		 		for (var i = 0, n = allElements.length; i < n; ++i) {
		 			var el = allElements[i];
		 			if (el.id) {
		 				var ask = el.id;
		 				if(ask.indexOf("snoChargesDivId") != -1) {
		 					snos = snos+document.getElementById(el.id).textContent+"|";
		 				}
		 			}
		 		}
		 		return snos;
		 	}
		 	/* Reload the site when reached via browsers back button */
			if(!!window.performance && window.performance.navigation.type == 2)
			{
			    window.location.reload();
			}
		    /* Reload the site when reached via browsers back button */
			
		    
			 //this code for to active the side menu 
			var referrer="${urlForActivateSubModule}";
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
