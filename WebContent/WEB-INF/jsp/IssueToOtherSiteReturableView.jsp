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
<style>
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
.Btnbackground{
background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
}
</style>
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
		<link href="css/style.css" rel="stylesheet">
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">

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
	  taxChargeColumn = formatColumns(taxChargeColumn);
	 
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
    	    var defaultOption = document.createElement("option");
    	   /*  defaultOption.text = "Select one..."; */
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
    	     $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
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
		
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   
   
    	else if(tableColumnName == trasportInvoiceColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		  
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
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Library</li>
						</ol>
					</div>
					
					
<div>
	<div align="center">
	<label class="errorMessage"><c:out value="${requestScope['Message']}"></c:out> </label> 
	
		<form:form modelAttribute="issueToOtherSiteModelForm"  id = "doInventoryUpdateFormId" class="form-inline"   method="POST" >
	<c:forEach var="getInvoiceDetails" items="${requestScope['listOfIssueToOtherSiteInwardList']}"> 
	<div class="container">
	 <div class="row">
	  <div class="col-xs-12">
	<div class="form-group col-xs-12" style="margin-bottom:25px;">
	
			<label for="invoice-number" class="col-sm-2" style="width:13%;">Invoice Number: </label>
			<form:input path="invoiceNumber" id="InvoiceNumberId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.invoiceNumber}" readonly="true"/> 
			
		
			<label for="invoice-date" class="col-sm-2" style="width:11%;"> Invoice Date: </label>
			<form:input path="invoiceDate" id="InvoiceDateId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.invoiceDate}"  readonly="true"/> 
		
		
			<label for="vendor-name" class="col-sm-2" style="width:14%;">Vendor Name: </label>
			<form:input path="vendorName" id="VendorNameId" class="form-control col-sm-2 marginRight"  value="${getInvoiceDetails.vendorName}" readonly="true"/>
	     	<input type="hidden" id="VendorId" name="VendorId" class="form-control col-sm-2 marginRight"  value="${getInvoiceDetails.vendorId}" readonly="true"/>
		
		</div>
		<div class="form-group col-xs-12" style="margin-bottom:10px;">
					
			<label for="gst-in" class="col-sm-2" style="width:13%;">GSTIN : </label>
				<form:input path="gsinNo" id="GSTINNumber" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.gsinNo}"  readonly="true"/> 
				
			<label for="vendor-address" class="col-sm-2" style="width:11%;">Vendor Address : </label>
				
				<form:input path="vendorAdress" id="VendorAddress" value="${getInvoiceDetails.vendorAdress}" class="form-control col-sm-2 marginRight" readonly="true"/> 
			
			<label for="state" class="col-sm-2" style="width:14%;">State : </label>
			
				<form:select path="state" id="state" class="form-control col-sm-2 marginRigh specialWidth" style="width: 171px;">
				              <form:option value="">---Select--</form:option>
					          <form:option value="1">Local</form:option>
					          <form:option value="2">Non Local</form:option>
						</form:select>	
		</div>
		<div class="form-group col-xs-12" style="margin-bottom:6px;">
		
		
			
			<%-- <label for="pono" class="col-sm-2" style="width:11%;">PONO : </label>
			
				<form:input path="poNo" name="poNo" id="PONOId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.poNo}" readonly="true"/> 
			
			<label for="po-date" class="col-sm-2" style="width:14%;">PO Date : </label>
		
			<form:input path="poDate" name="poDate" id="poDateId1" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.poDate}" readonly="true"/> 
		 --%>	
		</div>
		
	
		<div class="form-group col-xs-12" style="margin-bottom:10px;">
			
			
			
			<label for="vehicle-no" class="col-sm-2" style="width:13%;">Vehicle No : </label>
			
			<form:input path="vehileNo" name="vehileNo" id="vehileNoId" class="form-control col-sm-2 marginRight " value="${getInvoiceDetails.vehileNo}"  placeholder="Ex. AP-33-TV-2223"  title="Ex. AP-33-TV-2223"/> 
		
			<label for="transporter-name" class="col-sm-2" style="width:11%;">Transporter Name : </label>
			
			<form:input path="transporterName" name="transporterName" id="transporterNameId" value="${getInvoiceDetails.transporterName}" class="form-control col-sm-2 marginRight" /> 
						
			<label for="received-date" class="col-sm-2" style="width:14%;">Received Date : </label>
			
				 <form:input path="receivedDate" name="receivedDate" id="receivedDate" class="form-control col-sm-2 marginRight" /> 
				
		</div>

		
		
		<div class="form-group col-xs-12">
			
		

				 
				<%--  <form:input path="intrmidiatetEntryId" name="intrmidiatetEntryId"   value="${getInvoiceDetails.intrmidiatetEntryId}" class="form-control col-sm-2 marginRight" />   --%>
			 <input  type = "text" id="intrmidiatetEntryId" name="intrmidiatetEntryId"  style="display:none;" class="form-control" autocomplete="off" readonly="true" value="${getInvoiceDetails.intrmidiatetEntryId}"/> 
		
			
		</div>
		</div>
		</div>
		</div>
 </c:forEach> 
	
		
	               <!-- PRODUCT DETAILS -->
	       <div class="table-responsive protbldiv">    
	   
	        ` <div class="clearfix"></div>
				<table id="doInventoryTableId" class="table pro-table" style="">
				<tr class="table_header" style="  background: #eae7e7;">
					<th>S NO</th>
    				<th>Product</th>
    				<th>Sub Product</th>
    				<th>Child Product</th>    								
    				<th>Unit of Measurement</th>
    				<th>Quantity</th> 
    				<th> Price</th>
    				<!-- <th> BasicAmount</th>
    				<th> TaxPer </th> -->
    				<th> HSNCode </th>
    				<!-- <th>Tax Amount</th>
    				<th>AmountAfterTax</th>
    				<th> Other Or TransportCharges</th>
    				<th>TaxOn Other Or TransportCharges</th>
    				<th> Other Or TransportChargesAfterTax </th> -->
    				<th> Total Amount </th>
					<th>Expire Date</th>
					<th>Issued Time</th>
				</tr>
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfIssueToOtherSiteProductDetails']}" step="1" begin="0">	
	        	 	 <tr>
	        	  	<td>						
				       <div id="snoDivId${GetProductDetails.strSerialNo}">${GetProductDetails.strSerialNo}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
	        	  
					<td>
					
					      
                     <%--  <form:input  id= "product${GetProductDetails.strSerialNo}" name = "product${GetProductDetails.strSerialNo}" path="product"  class="form-control" readonly="true" value="${GetProductDetails.product}"/> --%> 
					<input type="text"  id="product${GetProductDetails.strSerialNo}"  name="product${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.product}"/>
					
					</td>
					<td>
						<%-- <form:input path="subProduct${GetProductDetails.strSerialNo}"    class="form-control" readonly="true" value="${GetProductDetails.subProduct}"/> --%>
					<input type="text"  id="subProduct${GetProductDetails.strSerialNo}"  name="subProduct${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.subProduct}"/>
				
					</td>
					<td>
					<input type="text"  id="childProduct${GetProductDetails.strSerialNo}"  name="childProduct${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="<c:out value="${GetProductDetails.childProduct}"/>"/>
						<%-- <form:input path="childProduct${GetProductDetails.strSerialNo}" class="form-control" readonly="true" value="${GetProductDetails.childProduct}"/> --%>
					</td>
					
					<td>
					<input type="text"  id="unitsOfMeasurement${GetProductDetails.strSerialNo}"  name="unitsOfMeasurement${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.unitsOfMeasurement}"/>
						<%-- <form:input path="unitsOfMeasurement${GetProductDetails.strSerialNo}"  class="form-control" readonly="true" value="${GetProductDetails.unitsOfMeasurement}"/> --%>
					</td>
					<td >
					<input type="text"  id="quantity${GetProductDetails.strSerialNo}"  name="quantity${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.quantity}"/>
						<%-- <form:input path="quantity${GetProductDetails.strSerialNo}"    class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.quantity}"/> --%>
                 	<%-- 	<form:input path="ProductAvailability" id="ProductAvailabilityId1" readonly="true" class="form-control"  value="${GetProductDetails.ProductAvailability}"/> --%> 
					</td>					
				
					<td>
					<input type="text"  id="price${GetProductDetails.strSerialNo}"  name="price${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.price}"/>
						<%-- <form:input path="price${GetProductDetails.strSerialNo}"    class="form-control" readonly="true" value="${GetProductDetails.price}"/> --%>
					</td>
					<%-- <td> 
						<form:input path="basicAmount"  id="BasicAmountId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.basicAmount}"/>
					</td>
					
					<td>
						<form:input path="tax"  readonly="true" class="form-control" autocomplete="off" value="${GetProductDetails.tax}"/>
					</td> --%>
					<td>
					<input type="text"  id="hsnCode${GetProductDetails.strSerialNo}"  name="hsnCode${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
						<%-- <form:input path="hsnCode${GetProductDetails.strSerialNo}"  readonly="true" class="form-control"  onkeydown="appendRow()" autocomplete="off"  value="${GetProductDetails.hsnCode}"/> --%>
					</td>
					<%-- <td>
						<form:input path="taxAmount" id="TaxAmountId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
					</td>
					<td>
						<form:input path="amountAfterTax" id="AmountAfterTaxId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
					   
					
					</td>
					<td>
						<form:input path="otherOrTransportCharges" id="OtherOrTransportChargesId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/>
						<input type = "text" id="otherOrTransportCharges${GetProductDetails.strSerialNo}"  name="otherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/>
					</td>
					<td>
						<form:input path="taxOnOtherOrTransportCharges" id="TaxOnOtherOrTransportChargesId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/>
						<input  type="text" id="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  name="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true"  class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/>
					</td>
					<td>
						<input type="text" id="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  name="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/>
						<form:input path="otherOrTransportChargesAfterTax" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/>
					</td> --%>
					<td>
					<input type="text"  id="totalAmount${GetProductDetails.strSerialNo}"  name="totalAmount${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/>
					<%-- <input type="text"  id="totalAmount${GetProductDetails.strSerialNo}"  name="totalAmount${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/> --%>
						<%-- <form:input path="totalAmount"  id="TotalAmountId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/> --%>
					</td>		
					<td>
					
					
					<input type="text"  id="expireDate${GetProductDetails.strSerialNo}"  name="expireDate${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.expireDate}"/>
					</td>  
					<td>
						<input type="text"  id="issueTime${GetProductDetails.strSerialNo}"  name="issueTime${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.issuedTime}"/>
					</td>
					<%-- <input  type = "text" id="indentEntryDetailsId${GetProductDetails.strSerialNo}" name="indentEntryDetailsId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/> --%>
					<%-- <form:input path="indentEntryDetailsId1" id="indentEntryDetailsId${GetProductDetails.strSerialNo}" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/> --%>
					<td style="display:none">
					<input  type = "hidden" id="productId${GetProductDetails.strSerialNo}" name="productId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.productId}"/>
					<input  type = "hidden" id="subProductId${GetProductDetails.strSerialNo}" name="subProductId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.subProductId}"/>
					<input  type = "hidden" id="childProductId${GetProductDetails.strSerialNo}" name="childProductId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.childProductId}"/>
					<input  type = "hidden" id="measurmentId${GetProductDetails.strSerialNo}" name="measurmentId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.measurmentId}"/>
					
					</td>  
				</tr>
		
		

		
			
			</c:forEach>
	</table>
	</div>
			<br/>
	
			
<!-- 	*********************************** -->
		<%-- 	<div class="clearfix"></div><br/>
			 <div class="table-responsive protbldiv">
			<table id="doInventoryChargesTableId" class="table pro-table" >
				<tr style="
    background: #eae7e7;">
					<th><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<th><%= trasportInvoice %></th>
    			
    			
    			
			<!-- 	<label class="control-label col-sm-2 col-xs-12">Invoice Date : </label> -->
			<div class="col-sm-3 col-xs-12" >
				<form:input path="InvoiceDate" id="InvoiceDateId" class="form-control"/>
			</div>
    				<th><%= actions %></th>
  				</tr>
			
				<tr>
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="Conveyance1" id="Conveyance1" class="form-control" >
							<form:option value=""></form:option>
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
					
						<form:input path="ConveyanceAmount1" id="ConveyanceAmount1"   type="number"  placeholder="Please enter Amount"  class="form-control" autocomplete="off"/>
					</td>
					<td>
						<form:select path="GSTTax1" id="GSTTax1" class="form-control"  onchange="calculateGSTTaxAmount(1)">
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
						<form:input path="GSTAmount1" id="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control" autocomplete="off"/>
					</td>
						<td>
						<form:input path="AmountAfterTax1" type="number"  id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path="TransportInvoice1"  type="text" id="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					
					 
					<td>
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</table>
			</div><br><br>  --%>
		<%-- 	<div class="container">
				<div class="row" style="border:1px solid #ccc;padding:20px;">
					<div class="col-sm-6"><div class="comment" style="text-align:left;"><span class="h4" style="margin-right:20px;">Note:</span><form:textarea path="Note" id="NoteId" class="form-control"/></div></div>
					<div class="col-sm-6"><span class="h4" style="margin-top:20px;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv"></span></div>
				</div>
				</div> <br><br> --%>	
		<label class="control-label col-sm-1  col-xs-12"> NOTE:
							
			</label>
			<div class="col-sm-3  col-xs-12">
						<form:textarea path="Note" id="NoteId" class="form-control"/>
					</div> 
					
			<form:input path="strTotalInvoice" id="strTotalInvoice" style="display:none;" class="form-control col-sm-2 marginRight" value="${requestScope['doubleFinalAmount']}" readonly="true"/> 
			
			
				<!-- 	<label class="control-label col-sm-4  col-xs-12">
							Final Amount <span id="finalAmntDiv"></span>
			</label> -->
				<!-- <h2 class="center-block" style="font-size: 24px;font-weight: 600;"><strong>Final Amount:</strong></h2><span id="finalAmntDiv" class="finalAmountDiv"></span> -->
				<%-- 	<div class="col-sm-3  col-xs-12">
						<form:textarea path="note" id="NoteId" class="form-control" value="${getInvoiceDetails.note}" readonly="true"/>
					</div> --%>
			<input type="hidden" name="numbeOfRowsToBeProcessed"  id="countOfRows">
			<input type="hidden" name="numbeOfTransRowsToBeProcessed"  id="countOfTransRowsRows">
			<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntryId" value= "" id="ttlAmntForIncentEntryId" >
			<input type="hidden" name="strIssueType" value= "${requestScope['strIssueType']}" id="strIssueType" >
</form:form>
		
			
	</div>
	
	</div>
			<div class="form-group clearfix ">
					<div class=" col-sm-12 form-submit" >
				<!-- 	<div class="Btnbackground">
					<input class="btn btn-warning col-sm-2 col-sm-offset-4" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
					</div> -->
						<!-- <input class="tblchargesbtn" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()"> -->
						<!-- <input class="tblchargesbtn" type="button" value="View GRN" id="viewGrnBtnId" onclick="viewGRN()"> -->
						<input class="tblchargesbtn btn btn-warning col-sm-2" type="button"  style="margin-top: 18px; margin-left: 440px;" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')">
						<!-- <input class="tblchargesbtn" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')"> -->
				
			
			</div>
					
					<!-- /page content -->        
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
		<script src="js/issueToOtherSite_ReturnableView.js" type="text/javascript"></script>
		
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			$(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			});
			
			/* Reload the site when reached via browsers back button */
			if(!!window.performance && window.performance.navigation.type == 2)
			{
			    window.location.reload();
			}
		    /* Reload the site when reached via browsers back button */
			
		</script>
</body>
</html>
