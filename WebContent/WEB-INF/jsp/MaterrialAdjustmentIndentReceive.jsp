<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
	String remarks = resource.getString("label.remarks");
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
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
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		
		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<script src="//code.jquery.com/jquery.min.js"></script>
		<script src="jquery.stickytable.min.js"></script>
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="CacheClear.jsp" />  
	
<style>
.pro-table tbody tr td:first-child, .pro-table tbody tr th:first-child {min-width: 20px;text-align: center;width:50px;}
.pro-table thead, .pro-table tbody tr{table-layout:fixed;display:table;width:100%;}
.finalAmountDiv{color: #f0ad4e;font-size: 24px;font-weight: bold;}
.form-group label{text-align: left}
.pro-table tbody tr td{border-top:0px !important;}


</style>	
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
	 
	 var remarksColumn =  "<%= remarks %>";
	 remarksColumn = formatColumns(remarksColumn);
	 //alert(remarksColumn);
	 
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
    	    div.setAttribute("style", "border: 1px solid #312d2d;"); 
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
    	    div.setAttribute("style", "border: 1px solid #312d2d;"); 
    	    
    	    
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}    	
    	else if(tableColumnName == measurementColumn) {
    		var dynamicSelectBoxId = "UnitsOfMeasurementId"+vfx;    		
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("style", ""); 
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
    	    div.setAttribute("style", ""); 
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
		    div.setAttribute("style", ""); 
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("style", ""); 
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
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculatePriceAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", "");  
		    div.setAttribute("readonly", "true");
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
   		}  else if(tableColumnName == expiryDate) {
   			
   		    var div1=document.createElement("div");
   		    div1.setAttribute("class", 'input-group');   		
		    cell.appendChild(div1);
		    
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "expireDate"+vfx);
		    div.setAttribute("id", "expireDateId"+vfx);
		    div.setAttribute("style", ""); 
		    div.setAttribute("class", 'form-control');		    
		    div1.appendChild(div);
		    
		    var div2 = document.createElement("label");
		    div2.setAttribute("class", "input-group-addon btn input-group-addon-border");
		    div2.setAttribute("for", "expireDateId"+vfx);
		    div1.appendChild(div2);
		    
		    var div3 = document.createElement("span");
		    div3.setAttribute("class", "fa fa-calendar");
		    div2.appendChild(div3);	
		    
		    $(function() {
		  	  $("#expireDateId"+vfx).datepicker({
		  		  dateFormat: 'dd-M-y',
		  		  minDate:0,
		  		  changeMonth: true,
		  	      changeYear: true
		  	  });
		    });
   		}    
   		else if(tableColumnName == remarksColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("style", ""); 
		   
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    
    	else if(tableColumnName == actionsColumn) {
			var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewItemBtn");
		    div2.setAttribute("id", "addNewItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendRow()");
		    div2.setAttribute("style", ""); 
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
		    div1.setAttribute("onclick", "deleteproductRow(this, "+vfx+")")
 			div1.setAttribute("style", ""); 
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
    	    div.setAttribute("onchange", "conveyancechange("+vfx+")"); 
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
		    div.setAttribute("placeholder", 'Pleease enter Amount');
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
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'GST Amount');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+"x"+vfx);
		    div.setAttribute("id", tableColumnName+"x"+vfx);
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
		    div2.setAttribute("onclick", "appendChargesRow("+vfx+")");
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
</head>
<body class="nav-md" id="body-refresh">
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
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Inwards</a></li>
							<li class="breadcrumb-item active">Make All Site Material Adjustment</li>
						</ol>
					</div>
					
					
   <div>
	<div class="col-md-12">
		<form:form modelAttribute="indentReceiveModelForm" id="doInventoryFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="container border-form-indent">
	 <div class="row">
	  <div class="col-md-12 col-xs-12 col-sm-12">
	        <div class="col-md-4 col-sm-6 col-xs-12">
	          <div class="form-group">
			<label  class="control-label col-md-6 col-sm-6 col-xs-12">Invoice Number : </label>
			<div class="col-md-6 col-sm-6 col-xs-12">
				<form:input path="InvoiceNumber" id="InvoiceNumberId" onclick="eidtTextBox()" class="form-control" autocomplete="off" value="ADJ_"/>
				<span id="errorMessageInvoiceNumber" style="display:none; color:red">* Already exist the invoice number</span>
				<input id="InvoiceNumberIdStatixText" type="hidden" value="ADJ_" size="50" />
				<input type="hidden" name="moduleName" id="moduleName" value="materialAdjustment">
				<input type="hidden" name="typeOfPurchase" value="localPurchase" >
				<input type="hidden"  id="state" name="state" class="form-control" value="1">
			</div>
			</div>
	        </div>
			<div class="col-md-4 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Invoice Date : </label>
			<div class="col-md-6 col-sm-6 col-xs-12 input-group">
				<form:input path="InvoiceDate" id="InvoiceDateId" class="form-control readonly-color" autocomplete="off" readonly="true"/>
				<label class="input-group-addon btn input-group-addon-border" for="InvoiceDateId">	<span class="fa fa-calendar"></span></label>
			</div>
			</div>
			</div>
			<div class="clearfix visible-sm"></div>
			<div class="col-md-4 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Vendor Name : </label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input path="VendorName" id="VendorNameId" class="form-control vendor" value="${vendorName}" readonly="true"/>
				<input type="hidden" name="VendorId" value="${vendorId}" id="vendorIdId">
			</div>
		   </div>
			</div>
		
		
		<div class="col-md-4 col-sm-6 col-xs-12">
		 <div class="form-group">
			
			<label class="control-label col-md-6 col-sm-6 col-xs-12">GSTIN : </label>
			<div class="col-md-6 col-sm-6 col-xs-12">
				<form:input path="GSTINNumber" id="GSTINNumber" value="${vendorGSTINNo}" class="form-control" autocomplete="off" readonly="true" />
			</div>
			</div>
		</div>
			<div class="col-md-4 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Vendor Address : </label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input path="VendorAddress" id="VendorAddress" value="${vendorAddress}" class="form-control" readonly="true" />
			</div>
			</div>
			</div>

			
			<div class="col-md-4 col-sm-6 col-xs-12">
				<div class="form-group">
				<label class="control-label col-md-6 col-sm-6 col-xs-12">Received Date : </label>
				<div class="col-md-6 col-sm-6 col-xs-12 input-group">
					<form:input path="receivedDate" name="receivedDate" id="receivedDate" class="form-control readonly-color" autocomplete="off" readonly="true"/>
					<label class="input-group-addon btn input-group-addon-border" for="receivedDate"><span class="fa fa-calendar"></span></label>
				</div>
				</div>
			</div>
			<div class="clearfix visible-sm"></div>
			
			<div class="col-md-4 col-sm-6 col-xs-12">
			    <div class="form-group">
					<label class="control-label col-md-6 col-sm-6 col-xs-12">Employee Name <%= colon %>  </label>
					<div class="col-md-6 col-sm-6 col-xs-12" >
						<form:input path="RequesterName" id="RequesterNameId"  onkeyup="populateEmployee(this);"  autocomplete="off" class="form-control"/>
					</div>
			   </div>
			</div>
			<div class="col-md-4 col-sm-6 col-xs-12">
			  <div class="form-group">
					<label class="control-label col-md-6 col-sm-6 col-xs-12">Employee ID <%= colon %> </label>
					<div class="col-md-6 col-sm-6 col-xs-12" >
						<form:input path="RequesterId" id="RequesterIdId" class="form-control"/>
					</div>
		     </div>
		   </div>	
	</div>
</div>
</div>

<!-- ******************************************** -->
			<div class="clearfix"></div><br/>
			<div class="table-responsive"> <!-- sticky-table sticky-headers sticky-ltr-cells -->
			<table id="doInventoryTableId" class="table pro-table table-new" style="width:4600px;">
			<thead class="cal-thead-inwards"> 
				<tr >
					<th style="width:50px;"><%= serialNumber %></th>
    				<th><%= product %></th>
    				<th><%= subProduct %></th>
    				<th><%= childProduct %></th>    								
    				<th><%= measurement %></th>
    				<th><%= quantity %></th> 
    				<th><%= price %></th>
    				<th><%= basicAmount %></th>
    				<th><%= taxPer %></th>
    				<th><%= hsnCode %></th>
    				<th><%= taxAmount %></th>
    				<th><%= amountAfterTax %></th>
    				<th><%= otherOrTransportCharges %></th>
    				<th><%= taxOnOtherOrTransportCharges %></th>
    				<th><%= otherOrTransportChargesAfterTax %></th>
    				<th><%= totalAmount %></th>
					<th><%=expiryDate %></th>
					<th><%=remarks %></th>
    				<th><%= actions %></th>
  				</tr>
  			</thead>
  			<tbody class="tbl-fixedheader-tbody">
				<tr id="productchargesrow1">
					<td class="text-center" style="width:50px;">						
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
						<form:select path="SubProduct1" id="comboboxsubProd1" class="form-control  btn-visibilty1 btn-loop1"/>
					</td>
					<td>
						<form:select path="ChildProduct1" id="comboboxsubSubProd1" class="form-control  btn-visibilty1 btn-loop1"/>
					</td>
					<td>
						<form:select path="UnitsOfMeasurement1" id="UnitsOfMeasurementId1" onchange="return validateProductAvailability(this);" class="form-control  btn-visibilty1 btn-loop1"/>
					</td>
					<td  class="s-50">
						<form:input path="Quantity1" id="QuantityId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)" class="form-control  btn-visibilty1 btn-loop1" autocomplete="off"/>
                        <form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td>
					<%-- <td>
						<form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td> --%>
					<td>
						<form:input path="Price1" id="PriceId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)" class="form-control"/>
					</td>
					<td>
						<form:input path="BasicAmount1" id="BasicAmountId1" class="form-control"  onkeypress="return validateNumbers(this, event);" onblur="calculatePriceAmount(1)" readonly="true"/>
					</td>
					<td>
						<form:select path="Tax1" id="TaxId1" onchange="calculateTaxAmount(1)" class="form-control">
							<form:option value="">--Select--</form:option>
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
						<form:input path="HSNCode1" id="HSNCodeId1"  class="form-control" autocomplete="off"/>
					</td>
					<td>
						<form:input path="TaxAmount1" id="TaxAmountId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="AmountAfterTax1" id="AmountAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="OtherOrTransportCharges1" id="OtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="TaxOnOtherOrTransportCharges1" id="TaxOnOtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="OtherOrTransportChargesAfterTax1" id="OtherOrTransportChargesAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="TotalAmount1" id="TotalAmountId1" readonly="true" class="form-control"/>
					</td>

					<td>
					<div class="input-group">
					<form:input path="expireDate1" id="expireDateId1" class="form-control" autocomplete="off" />
					<label class="input-group-addon btn input-group-addon-border" for="expireDateId1"><span class="fa fa-calendar"></span></label>
					</div>
						<%-- <form:input path="ExpiryDate" id="ExpiryDate" readonly="true" class="form-control"/> --%>
					</td>
					
					<td>
						<form:input path="Remarks1" id="RemarksId1" onkeydown="appendRow()" class="form-control" autocomplete="off"/>
					</td>	
					
					
					<td>
						<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
						<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button type="button" style="display:none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteproductRow(this, 1)" ><i class="fa fa-trash"></i></button>
					</td>
				</tr>
			</tbody>
			</table>
			</div>
			

<!-- ***********Transport Charges grid start here****************** -->
			<div class="clearfix"></div><br/>
			
			<div class="table-responsive" style="display: none;">
				<table id="doInventoryChargesTableId" class="table pro-table table-new tbl-width-medium">
				<thead class="cal-thead-inwards">
					 <tr >
	                    <th style="width:50px;"><%= sNumber %></th>
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
						<td class="text-center">						
							<div id="snoChargesDivId1">1</div>
						</td>
						<td>						
								<form:select path="Conveyance1" id="Conveyance1" class="form-control" onchange="conveyancechange(1)">
								<form:option value="">Please select Transport</form:option>
						    		<%	
						    			Map<String, String> conveyanceCharges1 = (Map<String, String>)request.getAttribute("chargesMap");
						    		
						    			for(Map.Entry<String, String> tax : conveyanceCharges1.entrySet()) {
						    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
						    				if(tax.getValue().equals("None")){
						    			%>
									
										<form:option value="<%=taxIdAndPercentage %>" selected="selected"><%= tax.getValue() %></form:option>
						    	
						    			<% }
						    				} %>
							</form:select>
						</td>
						<td>
						
							<form:input path="ConveyanceAmount1" id="ConveyanceAmount1" value="0" name="ConveyanceAmount1" type="number"  placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off"/>
						</td>
						<td>
							<form:select path="GSTTax1" id="GSTTax1" name="GSTTax1" class="form-control GSTClass "  onchange="calculateGSTTaxAmount(1)">
								<form:option value="">Transport Tax</form:option>
						    		<%	
						    			Map<String, String> conveyanceTax1 = (Map<String, String>)request.getAttribute("gstMap");
						    			for(Map.Entry<String, String> tax : conveyanceTax1.entrySet()) {
						    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
						    				if(tax.getValue().equals("0%")){
						    					
						    				
									%>
										<form:option value="<%= taxIdAndPercentage %>" selected="selected"><%= tax.getValue() %></form:option>
						    		<% } }%>
							</form:select>
						</td>
							<td>
							<form:input path="GSTAmount1" id="GSTAmount1" value="0" name="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off" readonly="true"/>
						</td>
							<td>
							<form:input path="AmountAfterTaxx1" type="number" value="0" name="AmountAfterTaxx1" id="AmountAfterTaxx1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off" readonly="true"/>
						</td>
					
						<td>
							<form:input path="TransportInvoice1"  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
						</td>
						
						
						 
						<td>
							<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
							<button type="button" style="display:none;" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
					</td>
					</tr>
				</tbody>
				</table>
			</div>

			<div class="col-md-12 Mrgtop20 no-padding-right no-padding-left">
					<div class="col-md-6 ">
					<div class="form-group">
					  <label class="control-label col-md-4 no-padding-left no-padding-right">
							<%= note %> <%= colon %>
					</label>
					<div class="col-md-8">
						<form:textarea path="Note" id="NoteId" class="form-control" style="resize:vertical;"/>
					</div>
					</div>
					</div>
					<div class="col-md-6 no-padding-left no-padding-right"><span class="h4" class="Mrgtop20"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv"></span></div>
				
				</div>
			
<h3 class="Mrgtop20">You can upload Invoice here</h3>
<div class="file-upload" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
	<input type="file" name="file" accept='image/*' ><br><input type="file" name="file" accept='image/*' ><br>
	<input type="file" name="file" accept='image/*' ><br><input type="file" name="file" accept='image/*' ><br>
</div>					

		
			<br/>
			
					<div class="col-md-12 text-center center-block form-submit" >
					<div class="Btnbackground">
						<input class="btn btn-warning btn-bottom" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
						<input class="tblchargesbtn btn btn-warning btn-bottom" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')">
					</div>
				</div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			
			
			</form:form>
		</div>
	</div>
	</div>
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
		<script src="js/MaterialAdjustmentindentReceive.js" type="text/javascript"></script>
		<script src="js/sidebar-resp.js" type="text/javascript"></script>
		
		
		<script>
		
//this method will keep the static data		
		function eidtTextBox() {
			  var readOnlyLength = $('#InvoiceNumberIdStatixText').val().length;
			   $('#InvoiceNumberId').on('keypress, keydown', function(event) {
			    if ((event.which != 37 && (event.which != 39)) && ((this.selectionStart < readOnlyLength) ||
			        ((this.selectionStart == readOnlyLength) && (event.which == 8)))) {
			      return false;
			    }
			  }); 
			}
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			function validateFileExtention(){
				debugger;			
				var ext="";
								var kilobyte=1024;
								var len=$('input[type=file]').val().length;
								var count=0;
							
								  $('input[type=file]').each(function () {
								        var fileName = $(this).val().toLowerCase(),
								         regex = new RegExp("(.*?)\.(jpeg|png|jpg)$");
								        	debugger;
										 if(fileName.length!=0){
								        	if((this.files[0].size/kilobyte)<=kilobyte){
											}else{
												if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
											        ext= fileName.substring(fileName.lastIndexOf(".")+1); 
												alert("Please Upload Below 1 MB "+ext+" File");
												count++;
												//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
												$(this).val('');
											return false;
											}
								        	
									        if (!(regex.test(fileName))) {
									            $(this).val('');
									            alert('Please select correct file format');
									            count++;
									            return false; 	
									        }
								        }
								    });
								// 
								  if(count!=0){
									  return false;  
								  }else{
									  return true;
								  }
						}

			
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
	          
			//conveyance change for none selection written by thirupathi
			
			function conveyancechange(id){
				
				if ($("#Conveyance"+id).val() == "999$None"){
					$("#ConveyanceAmount"+id).val("0");
					$("#ConveyanceAmount"+id).attr("readonly", "true");
					$("#GSTTax"+id).val('1$0%');
					$("#GSTTax"+id).attr("readonly", "true");
					$("#GSTAmount"+id).val("0");					
					$("#AmountAfterTaxx"+id).val("0");							
				}
				else{
					$("#ConveyanceAmount"+id).val(" ");
					$("#ConveyanceAmount"+id).removeAttr("readonly");
					$("#GSTTax"+id).val('');
					$("#GSTTax"+id).removeAttr("readonly");
					$("#GSTAmount"+id).val("");					
					$("#AmountAfterTaxx"+id).val("");
					
				}
			}
			
			 	
			 	
			 	
			 	
		</script>
</body>
</html>
