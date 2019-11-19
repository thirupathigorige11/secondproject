<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>

<%
	//Loading Indent Receive Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
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
	String taxPer = resource.getString("label.tax");
	String hsnCode = resource.getString("label.hsnCode");
	String taxAmount = resource.getString("label.taxAmount");
	String amountAfterTax = resource.getString("label.amountAfterTax");
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
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
<style>
.no-padding-left{
padding-left:0px;
}
 .no-padding-right{
 padding-right:0px;
 }
.table>tbody>tr>th{
background-color:#ccc;
border-top:1px solid #000 !important;
}
/* .tablDisply{
display:none;
} */
.showColumns .tablDisply{
display:table-cell;
}
.pro-table tbody tr td,.pro-table tbody tr th{
	margin:2px 3px;
	width:100%;
	min-width:213px; 
}
.showColumns {
  -webkit-transition-property: all;
  -webkit-transition-duration: .5s;
  -webkit-transition-timing-function: ease-out;4444
  -moz-transition-property: all;
  -moz-transition-duration: .5s;
  -moz-transition-timing-function: ease-out;
  -ms-transition-property: all;
  -ms-transition-duration: .5s;
  -ms-transition-timing-function: ease-out;
  transition-property: all;
  transition-duration: .5s;
  transition-timing-function: ease-out;
 }
.finalAmntDiv{
    color: #f0ad4e;
    font-size: 24px;
}

</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<title><%= pageTitle %></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		<jsp:include page="CacheClear.jsp" />  
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<script type="text/javascript">

//Create DIV element and append to the table cell
var i = 1;

function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
     var vfx = fldLength;  
     //removing space 
     tableColumnName=tableColumnName.trim();
     var snoColumn =  "<%= serialNumber %>";
     snoColumn = formatColumns(snoColumn);
     //alert(snoColumn);
     
     var productColumn =  "<%= product %>";
     productColumn = formatColumns(productColumn);
     //alert(productColumn);
  	 
  	 var subProductColumn =  "<%= subProduct %>";
  	 subProductColumn = formatColumns(subProductColumn);
 	 //alert(subProductColumn);
     
 	 var childProductColumn =  "<%= childProduct %>";
 	 childProductColumn = formatColumns(childProductColumn);
	 //alert(childProductColumn);
	 
	 var quantityColumn =  "<%= quantity %>";
	 quantityColumn = formatColumns(quantityColumn);
	 //alert(quantityColumn);
	 
	 var measurementColumn =  "<%= measurement %>";
	 measurementColumn = formatColumns(measurementColumn);
	 //alert(measurementColumn);
	 
	 var productAvailabilityColumn =  "<%= productAvailability %>";
	 productAvailabilityColumn = formatColumns(productAvailabilityColumn);
	 //alert(productAvailabilityColumn);
	 
	 var priceColumn =  "<%= price %>";
	 priceColumn = formatColumns(priceColumn);
	 //alert(priceColumn);
	 
	 var taxColumn =  "<%= taxPer %>";
	 taxColumn = formatColumns(taxColumn);
	 //alert(taxColumn);
	 
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
	 
	 var hsnCodeColumn =  "<%= hsnCode %>";
	 hsnCodeColumn = formatColumns(hsnCodeColumn);
	 
	 
	 var expiryDate=  "<%= expiryDate %>";
	 expiryDate = formatColumns(expiryDate);
	 //alert(hsnCodeColumn);
	 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);
	 //alert(actionsColumn);

     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        //snoDiv.setAttribute("class", style);
        //snoDiv.setAttribute("className", style);
        cell.appendChild(snoDiv);
    }
    else {
    	if(tableColumnName == productColumn) {
    		var dynamicSelectBoxId = "combobox"+vfx;
    		//alert(dynamicSelectBoxId); 
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);   	    
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Select one...";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);    	    
    	    var option = "";
    		<%
    			Map<String, String> products = (Map<String, String>)request.getAttribute("productsMap");
    			for(Map.Entry<String, String> prods : products.entrySet()) {
				String val = prods.getKey()+"$"+prods.getValue();
			%>
				option = document.createElement("option");
	    	    option.text = "<%= prods.getValue() %>";
	    	    option.value = "<%= val %>";
	    	    div.appendChild(option);
    		<% 
				} 
			%>
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}
    	else if(tableColumnName == subProductColumn) {
    		var dynamicSelectBoxId = "comboboxsubProd"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();    	        
			});    	    
    	}
    	else if(tableColumnName == childProductColumn) {
    		var dynamicSelectBoxId = "comboboxsubSubProd"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	    var div1 = document.createElement("input");
    	    div1.setAttribute("type", "hidden");
    	    div1.setAttribute("name", "groupId"+vfx);
    	    div1.setAttribute("id", "groupId"+vfx);
    	    cell.appendChild(div1);
    	}    	
    	else if(tableColumnName == measurementColumn) {
    		var dynamicSelectBoxId = "UnitsOfMeasurementId"+vfx;    		
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("onchange", "return validateProductAvailability(this);");
    	   	cell.appendChild(div);
    	}    	
    	else if(tableColumnName == taxColumn) {
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
    		
    		cell.className  = "tablDisply";
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control ');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
    		cell.className  = "tablDisply"
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		   
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
    		cell.className  = "tablDisply";
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		   
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
    		cell.className  = "tablDisply";
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
    		cell.className  = "tablDisply";
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    
		    div.setAttribute("name", tableColumnName+"Id"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesColumn) {
    		
    		cell.className  = "tablDisply";
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		   
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
    		
    		cell.className  = "tablDisply";
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		   
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) {
    		
    		cell.className  = "tablDisply";
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		  
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
    		
    		cell.className  = "tablDisply";
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == hsnCodeColumn) {
    		cell.className  = "";
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control tablDisply');
		    cell.appendChild(div);
		    
		    
		    var div6 = document.createElement("input");
		    div6.setAttribute("class", "totalQuantity");
		    div6.setAttribute("type", "hidden");
		    div6.setAttribute("id", "totalQuantity"+vfx);
		    cell.appendChild(div6); 
		    
		    var div7 = document.createElement("input");
		    div7.setAttribute("class", "BOQQty");
		    div7.setAttribute("type", "hidden");
		    div7.setAttribute("id", "BOQQty"+vfx);
		    cell.appendChild(div7);
		    
   		}  else if(tableColumnName == expiryDate) {
   			
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "expireDate"+vfx);
		    div.setAttribute("id", "expireDateId"+vfx);
		    div.setAttribute("class", 'form-control');
		    
		    cell.appendChild(div);
		    $("#expireDateId"+vfx).datepicker({dateFormat: 'dd-M-y'});
		   
   		}    	
    	else if(tableColumnName == actionsColumn) {
			var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewItemBtn");
		    div2.setAttribute("id", "addNewItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendRow()");
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
		    div1.setAttribute("onclick", "deleteproductRow(this, "+vfx+")");
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}   	
    }
}
//Create Charges  DIV element and append to the table cell

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {

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
    	    /* $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			}); */
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Please enter Amount");
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
		    div.setAttribute("readonly", "true");
		    div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("placeholder", "Amount After Tax");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   
   
    	else if(tableColumnName == trasportInvoiceColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Transport Invoice Number");
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

</script>
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

						<jsp:include page="SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="TopMenu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Inwards</a></li>
							<li class="breadcrumb-item active">DC_FORM</li>
						</ol>
					</div>
					
					
					<div>			
					
					
					
					
	<div class="col-md-12">
	
	
		<form:form modelAttribute="DCModelForm" id="doDCFormId" class="form-horizontal">
		
		<span class="alert-msg"><c:out value="${requestScope['Message']}"></c:out> </span>	
	<div class="container border-form-indent">
	<!--  <div class="row"> -->
	  <div class="col-md-12">
	  <div class="col-md-4">
	 			<div class="form-group">
					
					 <label  class="control-label col-md-6" >DC Number : </label>
					<div class="col-md-6" >
						<form:input path="DCNumber" id="DCNumberId" class="form-control" autocomplete="off"/>
						<span class="errormsg" style="color:red; display:none">* Already have DC Number</span>
					</div>
					</div>
					</div>
					<div class="col-md-4">
					 <div class="form-group">
					<label class="control-label col-md-6" > DC Date : </label>
							<div class="col-md-6" >
								<form:input path="DCDate" id="DCDate" name="DCDate" class="form-control" autocomplete="off"/>
							</div>
							</div>
					</div>
				   <div class="col-md-4">
				    <div class="form-group">
				     <label class="control-label col-md-6"> Vendor Name :</label>
						<div class="col-md-6" >
							<form:input path="VendorName" id="VendorNameId" class="form-control" autocomplete="off"/>
							<input type="hidden" name="VendorId" id="vendorIdId" class="form-control" value=""/>
						</div>
				    </div>
				   </div>
					
			
				</div>
				<div class="col-md-12">
				
				 <div class="col-md-4">
				  <div class="form-group">
						<label  class="control-label col-md-6" >GSTIN : </label>
							<div class="col-md-6" >
								<form:input path="GSTINNumber" id="GSTINNumber" class="form-control" autocomplete="off" readonly="true"/>
							</div>
							
				</div>
				 </div>
						<div class="col-md-4">
						 <div class="form-group">
						  <label  class="control-label col-md-6" >Vendor Address :</label>
							<div class="col-md-6" >
								<form:input path="VendorAddress" id="VendorAddress" class="form-control" autocomplete="off" readonly="true"/>
								<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${site_id}">
							</div>
						 </div>
						</div>
						<div class="col-md-4">
						 <div class="form-group">
						  <label  class="control-label col-md-6"  >State :</label>
							<div class="col-md-6" >
									<form:select path="state" id="state" class="form-control">
								              <form:option value="">---Select--</form:option>
									          <form:option value="1">Local</form:option>
									          <form:option value="2">Non Local</form:option>
										</form:select>	
							</div>
						 </div>
						</div>
				    <div class="clearfix"></div>
			
               <div class="col-md-4">
                <div class="form-group">
							<label class="control-label col-md-6" >Recieved Form : </label>
				<div class="col-md-6" >
					<select path="typeOfPurchase" id="state1" class="form-control" name="typeOfPurchase" onchange='Checklist(this.value);'>
				              <option value="">---Select--</option>
					          <option name="typeOfPurchase" value="marketPurchase">Market Purchase</option>
					          <option name="typeOfPurchase" value="localPurchase">Local Purchase</option>
					          <option name="typeOfPurchase" value="P0">PO</option>
						<select>	
			</div>
				</div>	
               </div>
				<div class="col-md-4">
				<div class="form-group">
				 <label class="control-label col-md-6" >PO Date : </label>
							<div class="col-md-6" >
								<form:input path="poDate" name="poDate" id="poDateId" class="form-control" autocomplete="off"/>
							</div>	
				</div>
				</div>		
				<div class="col-md-4">
				 <div class="form-group">
				  <label  class="control-label col-md-6" >E Way Bill No :</label>
							<div class="col-md-6" >
								<form:input path="eWayBillNo" name="eWayBillNo" id="eWayBillNoId" class="form-control" autocomplete="off"/>
							</div>
				 </div>
				</div>		
							
				
				
					
				
					<div class="col-md-4">
					 <div class="form-group">
					<label  class="control-label col-md-6" >Vehicle No : </label>
							<div class="col-md-6" >
								<form:input path="vehileNo" name="vehileNo" id="vehileNoId"  class="form-control" autocomplete="off"/>
							</div>
							</div>
					
					</div>
					<div class="col-md-4">
					 <div class="form-group">
					   <label  class="control-label col-md-6" >Transporter Name :  </label>
							<div class="col-md-6" >
							<%-- <form:select path="transporterName" id="transporterNameId" name="transporterName" class="form-control GSTClass " value="5%" >
								<form:option value="">--select--</form:option>
						    		<%	
						    			Map<String, String> transportorMap1 = (Map<String, String>)request.getAttribute("transportorMap");
						    			for(Map.Entry<String, String> tax1 : transportorMap1.entrySet()) {
						    				String transportorId = tax1.getKey()+"$"+tax1.getValue();
									%>
										<form:option data-toggle="tooltip" title="<%= tax1.getValue() %>" value="<%= transportorId %>"><%= tax1.getValue() %></form:option>
						    		<% } %> 
							</form:select> --%>
								<input type="text"  name="transporterName1" id="transporterNameIdId" onkeyup="return populateData();" class="form-control" value=""> 
							 <input type="hidden"  name="transporterName" id="transporterNameId"  class="form-control" value="">
							</div>
					 </div>
					</div>
					<div class="col-md-4">
					 <div class="form-group">
					  <label  class="control-label col-md-6" >Received Date : </label>
							<div class="col-md-6" >
								<form:input path="receivedDate" name="receivedDate" id="receivedDate"  class="form-control" autocomplete="off"/>
							</div>
					 </div>
					</div>
					</div>
					
				
				
				<!-- ============================================received From ======================================================= -->
				
				<div class="col-md-4">
				 <div class="form-group">
		  <div  id="others-divpo" style="display: none;">
			<label class="control-label col-md-6" >PO Number : </label>
			<div class="col-md-6" >
													<form:input path="" name="poNo" onblur="validatePoNum()" id="poNumber"
														class="form-control ValidateSameClass" />
													<span id="errorMessagePONumber" style="display:none; color:red">*please give Po number and vendor name same in po</span> 
			</div>
		</div>	
</div>
				</div>
       <div class="col-md-4">
        <div class="form-group">
         <div  id="others-divLoc" style="display: none;">
			<label class="control-label col-md-6">Indent Number: </label>
			<div class="col-md-6" >
				<form:input path="" name="indentNumber"  onblur="validateIndentNum()" id="indentNumber" class="form-control ValidateSameClass"   />
				 <span id="errorMessageIndentNumber" style="display:none; color:red">* Not existed the indent number</span> 
			</div>
		</div>
        </div>	
        
       </div>
		<input type="hidden" name="siteId" id="siteIdId"  readonly="true" class="form-control" value="${siteId}"/>
		<input type="hidden" id="allSiteIds" value="${Allsites}"/>
    </div>
		
				
				
				
<!-- 			
	
</div> -->
			<div class="table-responsive Mrgtop20"> <!-- protbldiv -->
			
			<table id="doInventoryTableId" class="table pro-table">
				<tr>
					<th><%= serialNumber %></th>
    				<th><%= product %></th>
    				<th><%= subProduct %></th>
    				<th><%= childProduct %></th>    								
    				<th><%= measurement %></th>
    				<th><%= quantity %></th> 
    				<th   class="tablDisply"><%= price %></th>
    				<th  class="tablDisply"><%= basicAmount %></th>
    				<th  class="tablDisply" ><%= taxPer %></th>
    				<th  class=""><%= hsnCode %></th>
    				<th   class="tablDisply"><%= taxAmount %></th>
    				<th  class="tablDisply"><%= amountAfterTax %></th>
    				<th  class="tablDisply" ><%= otherOrTransportCharges %></th>
    				<th  class="tablDisply"><%= taxOnOtherOrTransportCharges %></th>
    				<th  class="tablDisply"><%= otherOrTransportChargesAfterTax %></th>
    				<th  class="tablDisply"><%= totalAmount %></th>
					<th><%=expiryDate %></th>
				
		
    				<th><%= actions %></th>
  				</tr>
				<tr  id="productchargesrow1" class="producttable">
					<td>						
						<div id="snoDivId1">1</div>
					</td>
					<td>
					
					
						<select id="combobox1" name="Product1" class="form-control btn-visibilty1 btn-loop1">
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
						<form:select path="SubProduct1" id="comboboxsubProd1" class="form-control btn-visibilty1 btn-loop1"/>
					</td>
					<td>
						<form:select path="ChildProduct1" id="comboboxsubSubProd1" class="form-control btn-visibilty1 btn-loop1"/>
					</td>
					
					<td>
						<form:select path="UnitsOfMeasurement1" id="UnitsOfMeasurementId1" onchange="return validateProductAvailability(this);" class="form-control btn-visibilty1 btn-loop1"/>
					</td>
					<td  class="s-50">
						<form:input path="Quantity1" id="QuantityId1" onkeypress="return validateNumbers(this, event);" onkeyup="calculateTotalAmount(1)" class="form-control btn-visibilty1 btn-loop1" autocomplete="off"/>
                        <form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
                        <form:input path="groupId1" id="groupId1" type="hidden"/>
                        <input type="hidden" id="totalQuantity1" class="totalQuantity">
					     <input type="hidden" id="BOQQty1" class="BOQQty">
					</td>					
					
					<td class="tablDisply">
						<form:input path="Price1" id="PriceId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)"  class="form-control "/>
					</td>

					<td class="tablDisply">
						<form:input path="BasicAmount1" id="BasicAmountId1" class="form-control " readonly="true" onkeypress="return validateNumbers(this, event);"  onblur="calculatePriceAmount(1)"/>
					</td>
					<td class="tablDisply">
						<form:select path="Tax1" id="TaxId1" onchange="calculateTaxAmount(1)" class="form-control " >
							<form:option value="">-select-</form:option>
					    		<%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
					    		<% } %>
						</form:select>
					</td>
					<td class="">
						<form:input path="HSNCode1" id="HSNCodeId1" class="form-control " autocomplete="off"/>  <!-- onkeydown="appendRow()"  -->
					</td>
					<td class="tablDisply">
						<form:input path="TaxAmount1" id="TaxAmountId1" readonly="true" class="  form-control " />
					</td>
					<td class="tablDisply">
						<form:input path="AmountAfterTaxId1" id="AmountAfterTaxId1" readonly="true" class="  form-control" />
					</td>
					<td class="tablDisply">
						<form:input path="OtherOrTransportCharges1" id="OtherOrTransportChargesId1" readonly="true" class="  form-control "  />
					</td>
					<td class="tablDisply">
						<form:input path="TaxOnOtherOrTransportCharges1" id="TaxOnOtherOrTransportChargesId1" readonly="true" class="  form-control " />
					</td>
					<td class="tablDisply">
						<form:input path="OtherOrTransportChargesAfterTax1" id="OtherOrTransportChargesAfterTaxId1" readonly="true" class="  form-control"   />
					</td>
					<td class="tablDisply">
						<form:input path="TotalAmount1" id="TotalAmountId1" readonly="true" class="form-control"/>
					<td>
					<form:input path="expireDate1" id="expireDateId1" type="text" class="form-control" autocomplete="off" />
						<%-- <form:input path="ExpiryDate" id="ExpiryDate" readonly="true" class="form-control"/> --%>
					</td>
					
					<td>
						<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
						<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
                        <button type="button" style="display:none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteproductRow(this, 1)" ><i class="fa fa-trash"></i></button>
					</td>
				</tr>
			</table>
			</div>
			<!-- ***********Transport Charges grid start here****************** -->
			
			<div class="table-responsive Mrgtop20"><!--  protbldiv -->
			<table id="doInventoryChargesTableId" class="table pro-table">
				<tr style="  background: #eae7e7;">
					<th><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<th><%= trasportInvoice %></th>
    				<th><%= actions %></th>
  				</tr>
			
				<tr id="chargesrow1">
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="Conveyance1" id="Conveyance1" class="form-control"   onchange="changeconveyance(1)">
							<form:option value="">Please select Transport</form:option>
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
					
					<form:input path="ConveyanceAmount1" id="ConveyanceAmount1" name="ConveyanceAmount1" type="number" min="0" placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off"/> 
					</td>
					<td>
					 	<form:select path="GSTTax1" id="GSTTax1" name="GSTTax1" class="form-control GSTClass "  onchange="calculateGSTTaxAmount(1)">
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
						 <form:input path="GSTAmount1" id="GSTAmount1" name="GSTAmount1" type="number" readonly="true" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off"/> 
					</td>
						<td>
						 <form:input path="AmountAfterTax1" type="number"  name="AmountAfterTax1" readonly="true" id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
					 <form:input path="TransportInvoice1"  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/> 
					</td>
					
					
					 
					<td>
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" style="display:none;" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</table>
			</div>
			
			<div class="col-md-12 Mrgtop20 no-padding-left no-padding-right">
			 <div class="col-md-6 no-padding-left no-padding-right"><div class="form-group">
			
					<label class="control-label col-md-4 col-xs-12">
							<%= note %> <%= colon %>
					</label>
					
					<div class="col-md-7 col-xs-12">
						<form:textarea path="Note" id="NoteId" class="form-control resize-vertical"/>
					</div>
					</div></div>
					<div class="col-md-6 no-padding-left no-padding-right">
					 <div class="form-group">
					 <label class="control-label col-md-4">
							<%= finalAmount %> <%= colon %> 
					</label>
					<div class="col-md-8">
					 <span id="finalAmntDiv"></span>
					</div>
					</div>
					
					</div>
			</div>
			<div>
					<div class="col-md-12 text-center center-block Mrgtop20"> <!--  form-submit  -->
				<!-- 	<div class="Btnbackground"> -->
						<input class="btn btn-warning tblchargesbtn " type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
						<input class="btn btn-warning tblchargesbtn" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')">
					</div>
					<!-- </div> -->
				
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
			</div>
		</form:form>
		</div>	
	</div>
	</div>
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
<script src="js/DCForm.js" type="text/javascript"></script>
		
		
		<script>
		
		function Checklist(val){debugger;
		 var element=document.getElementById("others-divpo");
		 var element1=document.getElementById("others-divLoc");
		 var element2=document.getElementById("others-divMp");
		 if(val=='P0'){
		$("#others-divpo").show();	
		 $("#others-divLoc").hide();
		 $("#others-divMp").hide();
		 }
		 else if (val=='marketPurchase'){
		   $("#others-divLoc").show();
		   $("#others-divpo").hide();
		  /*  $("#others-divLoc").hide(); */
		 }
		 else if(val=='localPurchase'){
		 $("#others-divLoc").show();
		/*  $("#others-divpo").hide(); */
		   $("#others-divMp").hide();
		}
		}
		
		
		
		
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
			
 			$(document).ready(function(){
				$("#plusbutton").click(function(){
				 $(".tablDisply").toggle();
				   
				})
				});
/*  			$(document).ready(function(){
 				  var effect = 'slide';
 				 $("#btnmore").click(function(e){
 				    e.preventDefault();
 				   var text = $("#btnmore").text();
 				   if(text == "Show More"){
 					   $('#doInventoryTableId').parent().addClass("showColumns");
			    		//$(".tablDisply").show();
			    		window.scrollBy(1000, 0);
				     	//$(".protbldiv").scrollLeft(3000);
				     	var leftPos = $('.showColumns').scrollLeft();
				     	  $(".showColumns").animate({scrollLeft: leftPos + 5000}, 500);
			    	}if(text == "<<Show Less"){
			    		$('#doInventoryTableId').parent().removeClass("showColumns");
			    		//$(".tablDisply").hide();
			    		
			    	}
 				    $(this).text( $(this).text() == "Show More" ? "<<Show Less" : "Show More" );
 				  	
 				     	
 				    })
 				    
 				    $(".btnaction").click(function(){
 				    	var text = $("#btnmore").text();
 				    	if(text == "<<Show Less"){
 				    		
 				    		$('#doInventoryTableId').parent().addClass("showColumns");
 				    		var leftPos = $('.showColumns').scrollLeft();
 					     	  $(".showColumns").animate({scrollLeft: leftPos + 5000}, 500);
 				    	//	$(".tablDisply").show();
 				    	}if(text == "Show More"){
 				    		$('#doInventoryTableId').parent().removeClass("showColumns");
 				    		//$(".tablDisply").hide();
 				    	}
 				    	
 				    })
 				}) */

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
				  $("#GSTAmount"+btn).val(" ");
				  //$("#GSTAmount"+btn).removeAttr("readonly");
				  $("#AmountAfterTax"+btn).val(" ");
				  $("#AmountAfterTax"+btn).removeAttr("readonly");
			  }
 	} 
 				
		</script>
		
		
</body>
</html>
