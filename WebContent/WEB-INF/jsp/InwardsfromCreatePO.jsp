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
	
	

	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
		<jsp:include page="CacheClear.jsp" />  
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="js/inventory.css" rel="stylesheet" type="text/css" /> 
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.2/sweetalert.min.js"></script>
<script>
//Create DIV element and append to the table cell
var i = 1;

function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
     var vfx = fldLength;     
     //removing space
     tableColumnName=tableColumnName.trim();
     var snoColumn =  "<%= serialNumber %>";
     snoColumn = formatColumns(snoColumn);
     
     var productColumn =  "<%= product %>";
     productColumn = formatColumns(productColumn);
  	 
  	 var subProductColumn =  "<%= subProduct %>";
  	 subProductColumn = formatColumns(subProductColumn);
     
 	 var childProductColumn =  "<%= childProduct %>";
 	 childProductColumn = formatColumns(childProductColumn);
	 
	 var quantityColumn =  "<%= quantity %>";
	 quantityColumn = formatColumns(quantityColumn);
	 
	 var measurementColumn =  "<%= measurement %>";
	 measurementColumn = formatColumns(measurementColumn);
	 
	 var productAvailabilityColumn =  "<%= productAvailability %>";
	 productAvailabilityColumn = formatColumns(productAvailabilityColumn);
	 
	 var priceColumn =  "<%= price %>";
	 priceColumn = formatColumns(priceColumn);
	 
	 var taxColumn =  "<%= taxPer %>";
	 taxColumn = formatColumns(taxColumn);
	 
	 var basicAmountColumn =  "<%= basicAmount %>";
	 basicAmountColumn = formatColumns(basicAmountColumn);
	 
	 var discountColumn =  "<%= discount %>";
	 discountColumn = formatColumns(discountColumn);
	 
	 var AmountAfterDiscountColumn =  "<%= AmountAfterDiscount %>";
	 AmountAfterDiscountColumn = formatColumns(AmountAfterDiscountColumn);
	 
	 var taxAmountColumn =  "<%= taxAmount %>";
	 taxAmountColumn = formatColumns(taxAmountColumn);
	 
	 var amountAfterTaxColumn =  "<%= amountAfterTax %>";
	 amountAfterTaxColumn = formatColumns(amountAfterTaxColumn);
	 
	 var otherOrTransportChargesColumn =  "<%= otherOrTransportCharges %>";
	 otherOrTransportChargesColumn = formatColumns(otherOrTransportChargesColumn);
	 
	 var taxOnOtherOrTransportChargesColumn =  "<%= taxOnOtherOrTransportCharges %>";
	 taxOnOtherOrTransportChargesColumn = formatColumns(taxOnOtherOrTransportChargesColumn);
	 
	 var otherOrTransportChargesAfterTaxColumn =  "<%= otherOrTransportChargesAfterTax %>";
	 otherOrTransportChargesAfterTaxColumn = formatColumns(otherOrTransportChargesAfterTaxColumn);
	 
	 var totalAmountColumn =  "<%= totalAmount %>";
	 totalAmountColumn = formatColumns(totalAmountColumn);
	 
	 var hsnCodeColumn =  "<%= hsnCode %>";
	 hsnCodeColumn = formatColumns(hsnCodeColumn);
	 
	 
 	 var expiryDate=  "<%= expiryDate %>";
	 expiryDate = formatColumns(expiryDate);
	 
	 var remarksColumn =  "<%= remarks %>";
	 remarksColumn = formatColumns(remarksColumn);
	 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);

     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        cell.appendChild(snoDiv);
    }
    else {
    	if(tableColumnName == productColumn) {
    		var dynamicSelectBoxId = "combobox"+vfx;
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
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
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
    	    div.setAttribute("onchange", "return appendvalues("+vfx+");");
    	   	cell.appendChild(div);
    	}    	
    	else if(tableColumnName == taxColumn) {
    		/* var dynamicSelectBoxId = "taxAmount"+vfx; */
    		var div = document.createElement("input");
    	    div.setAttribute("name", 'TaxId'+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", 'TaxId'+vfx);
    	    div.setAttribute("readonly", "true");
    	    div.setAttribute("type", "hidden");
    	   /*  div.setAttribute("onchange", "calculateTaxAmount("+vfx+")"); */
    	    cell.appendChild(div);
    	   
    	    var div1 = document.createElement("input");
    	    div1.setAttribute("name", 'TaxIdHidden'+vfx);
    	    div1.setAttribute("id", 'TaxIdHidden'+vfx);
    	    div1.setAttribute("class", 'form-control');
    	    div1.setAttribute("readonly", "true");
    	    div1.setAttribute("type", "text");
    	    cell.appendChild(div1);    	    
    	    
    	}
    	else if(tableColumnName == quantityColumn) {   
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "quantity"+vfx);
		    div.setAttribute("id", "stQantityPOP"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx);	     */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		    div.setAttribute("autocomplete", "off");
		    div.setAttribute("onblur", "calculatequantitybaseinpop("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
		    
		  
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "PriceId"+vfx);
		    div.setAttribute("id", "PriceId"+vfx);
		    div.setAttribute("readonly", "true");
		   /*  div.setAttribute("id", tableColumnName+vfx);	    */ 
		   /*  div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		   /*  div.setAttribute("onblur", "calculateTotalAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "BasicAmountId"+vfx);
		    div.setAttribute("id", "BasicAmountId"+vfx);
		    div.setAttribute("readonly", "true");
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		  /*   div.setAttribute("onblur", "calculatePriceAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		    div.setAttribute("readonly", "true");
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		   /*  div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "amtAfterDiscount"+vfx); 
		    div.setAttribute("id", "amtAfterDiscount"+vfx);
		    div.setAttribute("readonly", "true");
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		 /*    div.setAttribute("id", "tax"+vfx); */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control' );
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", 'TaxAmountId'+vfx);
		    div.setAttribute("id", "TaxAmountId"+vfx);
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		   /*  div.setAttribute("id", "amountAfterTax"+vfx); */
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", 'AmountAfterTaxId'+vfx);
		 /*    div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    div.setAttribute("id", "AmountAfterTaxId"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "OtherOrTransportChargesId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("value", "0.00");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "TaxOnOtherOrTransportChargesId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("value", "0.00");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "OtherOrTransportChargesAfterTaxId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("value", "0.00");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "TotalAmountId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control totalAmount');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "HSNCodeId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", "true");
		    cell.appendChild(div);
   		} else if(tableColumnName == expiryDate) {
   			
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "expireDate"+vfx);
		    div.setAttribute("id", "expireDateId"+vfx);
		    div.setAttribute("style", "border: 1px solid #312d2d;"); 
		    div.setAttribute("class", 'form-control');
		    
		    cell.appendChild(div);
		    $("#expireDateId"+vfx).datepicker({dateFormat: 'dd-M-y'});
		   
   		}    
   		else if(tableColumnName == remarksColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);		   
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    
    	else if(tableColumnName == actionsColumn) {
			var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewItemBtn");
		    div2.setAttribute("id", "addNewItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendRow("+vfx+")");
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
		    div1.setAttribute("onclick", "deleteDebitNoteRow(this, "+vfx+")");
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}   

    }
}

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {

     var vfx = fldLength;     
	//removing space
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
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Conveyance Amount");
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
		    div.setAttribute("type", "text");
		    div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", "Amount After Tax");
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   
   
    	else if(tableColumnName == trasportInvoiceColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);	
		    div.setAttribute("placeholder", "Transport Invoice Number");
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
		    div1.setAttribute("onclick", "deleteConveyanceRow(this, "+vfx+")");
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
	$(".productrow").each(function(){
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
function createInvoice() {debugger;
	
var indentFor = document.getElementById("indentFor").value;
	if(indentFor == "" || indentFor == null || indentFor == '') {
		alert("Please select the Indent for.");
		document.getElementById("indentFor").focus();
		return false;
	}
var invoiceId = document.getElementById("InvoiceNumberId").value;
	if(invoiceId == "" || invoiceId == null || invoiceId == '') {
		alert("Please enter the Invoice Number.");
		document.getElementById("invoiceId").focus();
		return false;
	}
	
var invoiceDate = document.getElementById("InvoiceDateId").value;
	if(invoiceDate == "" || invoiceDate == null || invoiceDate == '') {
		alert("Please enter the Invoice Date.");
		document.getElementById("invoiceDate").focus();
		return false;
	}
var CrdState = document.getElementById("state").value;
	if(CrdState == "" || CrdState == null || CrdState == '') {
		alert("Please enter the select the State.");
		document.getElementById("CrdState").focus();
		return false;
	}
var receivedDate = document.getElementById("receivedDate").value;
	if(receivedDate == "" || receivedDate == null || receivedDate == '') {
		alert("Please enter the Received Date.");
		document.getElementById("receivedDate").focus();
		return false;
	}
	
var transporterName = document.getElementById("transporterNameIdId").value;
	if(transporterName == "" || transporterName == null || transporterName == '') {
		alert("Please select the transportorName.");
		document.getElementById("transporterNameIdId").focus();
		return false;
	}
	/* var expireDate = document.getElementById("expireDate").value;
	if(expireDate == "" || expireDate == null || expireDate == '') {
		alert("Please enter the expireDate Date.");
		document.getElementById("expireDate").focus();
		return false;
	} */
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
/*  var valStatus = validateRowData(1);
	if(valStatus == false) {
    	return;
	} */
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
	//Expiry date condition to check the empty or not
	debugger;
	var dateStatus=validateDate();
	if(dateStatus==false){
		return false;
	}

	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	debugger;
	$('.loader-sumadhura').show();
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	if(indentFor==1){
		document.getElementById("ProductWiseIndentsFormId").action = "convertPOtoInvoice.spring";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
	}
	else if(indentFor==2){
		document.getElementById("ProductWiseIndentsFormId").action = "convertPOtoDC.spring";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
	}
	else{}
	
}



</script>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.table-new thead, .table-new tbody tr {table-layout: fixed;display: table; width: 100%;}
.table-new tbody tr td{border-top: 0px !important;}
.table-new thead tr th:first-child,.table-new tbody tr td:first-child{width:52px;}
.font-16{font-size: 16px;}
.colonclass{margin-top: 1%;font-weight: bold;font-size: 16px;}
.creditOptionAnchor{float:left;color:blue;font-weight:bold;font-family:14px;}
.creditOptionAnchor:hover{cursor: pointer;color:blue;}
 .font-18{font-size: 18px;}
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
							<li class="breadcrumb-item active">Inwards From PO</li>
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
					
					<div>
	<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div>
		<div class="container border-inwards">
	 <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">	
	  <div class="col-md-4">
	    <div class="form-group">
	        <label class="col-md-6">Indent For : </label>
	        <div class="col-md-6">
	            <select name="indentFor" id="indentFor" class="form-control">
	                <option value="">---Select--</option>
	                <option value="1">Invoice</option>
	                <option value="2">DC</option>
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
				<form:input path="vendorAddress" name="vendorAddress" id="VendorAddress" class="form-control" value="${poDetails.vendorAddress}" data-toggle="tooltip" data-placement="bottom" title="${poDetails.vendorAddress}" readonly="true" />
			</div>
		 </div>
			
		</div>		
		<!-- need to verify -->	
		<div class="col-md-4">
				<input type="hidden" id="vendorId" name="VendorId" class="form-control" value="${poDetails.vendorId}" />
		</div>	
		
		<div class="clearfix"></div>
			  <div class="col-md-4">
			   <div class="form-group">
			  <label class="col-md-6">Indent No : </label>
			<div class="col-md-6" >
				<input type="text" id="sitewiseIndentNumber" name="sitewiseIndentNumber" class="form-control" readonly="true" value="${poDetails.siteWiseIndentNo}">
				<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${poDetails.indentNo}">
			</div>
			</div>
			  </div>
			 <div class="col-md-4">
			  <div class="form-group">
			   <label class="col-md-6">Site Name : </label>
			<div class="col-md-6" >
				<input type="text" id=siteName" name="SiteName" class="form-control" readonly="true" value="${poDetails.siteName}">
				<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}">
				<input type="hidden" id="payment_Req_Date"  name="payment_Req_Date" value="${poDetails.payment_Req_days}"/>
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
			<div class="col-md-6 col-xs-12 input-group">
				<input type="text" name="InvoiceorDCDate" id="InvoiceDateId" class="form-control readonly-color" autocomplete="off" value="" readonly="true">
				<label class="input-group-addon btn input-group-addon-border" for="InvoiceDateId"><span class="fa fa-calendar"></span></label>
			</div>
			</div>
		</div>
		<!-- <div class="col-md-4">
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
		</div> -->
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6">PO No : </label>
			<div class="col-md-6" >
				<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off" readonly="true" value="${poNumber}" data-toggle="tooltip" data-placement="bottom" title="${poNumber}">
			</div>
		 </div>
		</div>
			
		  <div class="col-md-4">
		   <div class="form-group">
		  <label class="col-md-6 col-xs-12">PO Date :</label>
			<div class="col-md-6 col-xs-12 input-group">
				<input type="text"  name="poDate" id="poDateId" class="form-control" readonly="true" value="${poDetails.poDate}">
				<label class="input-group-addon btn input-group-addon-border" for=""><span class="fa fa-calendar"></span></label>
			</div>
			</div>
		  </div>
		  <div class="clearfix"></div>
		 	<div class="col-md-4">
		 	 <div class="form-group">
		 	  <label class="col-md-6">E Way Bill No :</label>
			<div class="col-md-6 ">
				<input type="text"  name="eWayBillNo" id="eWayBillNoId" class="form-control" value="">
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
			<%-- <select id="transporterName" name="transporterName" class="form-control GSTClass " value="5%">
								<option value="">--select--</option>
						    		<%	
						    			Map<String, String> transportorMap1 = (Map<String, String>)request.getAttribute("transportorMap");
						    			for(Map.Entry<String, String> tax1 : transportorMap1.entrySet()) {
						    				String transportorId = tax1.getKey()+"$"+tax1.getValue();
									%>
										<option value="<%= transportorId %>"><%= tax1.getValue() %></option>
						    		<% } %> 
			</select> --%> 
			 <input type="text"  name="transporterName1" id="transporterNameIdId" onkeyup="return populateData();" class="form-control" value=""> 
			 <input type="hidden"  name="transporterName" id="transporterNameId"  class="form-control" value=""> 
			</div>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6 col-xs-12">Received Date : </label>
			<div class="col-md-6 col-xs-12 input-group">
				<input type="text"  name="receivedDate" id="receivedDate" class="form-control readonly-color" readonly="true" autocomplete="off" value="" ><!-- readonly="true" -->
				<label class="input-group-addon btn input-group-addon-border" for="receivedDate"><span class="fa fa-calendar"></span></label>
			</div>
		 </div>
		</div>		
							  
		<input type="hidden" id="hiddenPODate"  name="hiddenPODate" value="${noOfDays}"/>
		<input type="hidden" id="poEntryId"  name="poEntryId" value="${poDetails.poEntryId}"/>		
		<input type="hidden" name="siteId" id="siteIdId"  readonly="true" class="form-control" value="${siteId}"/>
		<input type="hidden" id="allSiteIds" value="${Allsites}"/>
		<input type="hidden" id="state" 	 name="state"     value="${poDetails.state}"/>
		 	
			</div>
		
		</c:forEach>
	<!-- </div>
		</div>
		</div> -->
	

	      <div class="clearfix"></div>
	      <div>
	       <div class="table-responsive Mrgtop20"> 
				<table id="doInventoryTableId-Main" class="table  pro-table table-new" style="width:4396px;">
				<thead class="cal-thead-inwards">
				 <tr>
					<th>S.NO</th>
    				<th>Product</th>
    				<th>Sub Product</th>
    				<th>Child Product</th>    								
    				<th>Units Of Measurement</th>
    				<th>Quantity</th>
    				<th>Received Quantity</th> 
    				<th>PO Quantity</th>
    				<th>Price</th>
    				<th>Basic Amount</th>
    				<th>Discount</th>
    				<th>Amount After Discount</th>
    				<th>Tax </th>
    				<th>HSN Code </th>
    				<th>Tax Amount</th>
    				<th>Amount After Tax</th>
    				<th>Other Or Transport Charges</th>
    				<th>Tax On Other Or Transport Charges</th>
    				<th>Other Or Transport Charges After Tax </th>
    				<th>Total Amount </th>
    				<th>Remarks </th>
					<th>Expire Date</th> 
				</tr>
				</thead>
				<tbody class="tbl-fixedheader-tbody">
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
	        	  <tr id="tr-class${GetProductDetails.serialno}" class="productrow">
	        	 	<td>	
	        	  						
				       <div id="mainsnoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="product${GetProductDetails.serialno}" readonly="true" name="mainProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1" data-toggle="tooltip" data-placement="bottom" title="${GetProductDetails.productName}" value="${GetProductDetails.productName}">
						<option value="${GetProductDetails.productId}$${GetProductDetails.productName}">${GetProductDetails.productName}</option>	
							<%-- <option value="">${GetProductDetails.product}</option> --%>
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
					<form:select path="" id="subProduct${GetProductDetails.serialno}"  readonly="true" name="mainSubProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}" data-toggle="tooltip" data-placement="bottom" title="${GetProductDetails.sub_ProductName}">
					<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
					</form:select>
					</td>
					<td>
						<form:select path="" id="childProduct${GetProductDetails.serialno}" readonly="true" name="mainChildProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}" data-toggle="tooltip" data-placement="bottom" title="${GetProductDetails.child_ProductName}">
						<option value="<c:out value='${GetProductDetails.child_ProductId}$${GetProductDetails.child_ProductName}'/>">${GetProductDetails.child_ProductName}</option>
					</form:select>
					<input type="hidden" id="groupId${GetProductDetails.serialno}"  name ="groupId${GetProductDetails.serialno}"  readonly="true"   class="form-control" value="${GetProductDetails.groupId}"/>
					</td>
					
					<td>
					<form:select path="" id="mainUnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="mainUnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
						<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
					</form:select>
					</td>
					  <td >
                        <input type="text"  id="mainQantity${GetProductDetails.serialno}"  class="form-control qtyinput"  name="mainquantity${GetProductDetails.serialno}" onfocusout ="validateQuantity(${GetProductDetails.serialno})" onkeypress="return isNumber(event)" tabindex="${GetProductDetails.serialno}" value="" autocomplete="off"/> 
                        <input type="hidden"  id="hiddenQantity${GetProductDetails.serialno}"     class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
    			    	<input type="hidden"  id="avalBOQQty${GetProductDetails.serialno}"   name="avalBOQQty${GetProductDetails.serialno}" value="${GetProductDetails.availableQuantity}"/>
						<input type="hidden"  id="BOQQty${GetProductDetails.serialno}"  name="BOQQty${GetProductDetails.serialno}" value="${GetProductDetails.boqQuantity}"/>
    			    </td>	
    			    <td >
                        <input type="text"  name="ReceivedQty${GetProductDetails.serialno}" id="ReceivedQty${GetProductDetails.serialno}" readonly="true" class="form-control" value="${GetProductDetails.recivedQty}"/> 
                    </td>                  			
					<td >
                        <input type="text"  readonly="true" class="form-control" id="POQuantity${GetProductDetails.serialno}" value="${GetProductDetails.requiredQuantity}"/> 
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
						<input type="text" id="hsnCode${GetProductDetails.serialno}"  name ="mainhsnCode${GetProductDetails.serialno}"   class="form-control" value="${GetProductDetails.hsnCode}"/>
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
					  <div class="input-group">
					  	<input type="text" id="expireDate${GetProductDetails.serialno}"  name="expireDate${GetProductDetails.serialno}"    class="form-control ExpiryDate" autocomplete="off"/>
					  	<label class="input-group-addon btn input-group-addon-border" for="expireDate${GetProductDetails.serialno}"><span class="fa fa-calendar"></span></label>
					 </div>
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
				<br><br><br>
				
				
				
			<!-- 	//********************************************************************* -->
				
<a href="JavaScript:void(0);" class="creditOptionAnchor" data-toggle="modal" data-target="#myModal"> GENERATE DEBIT NOTE (Click to return Products)</a>
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg"  style="width: 1205px;">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center"><strong>Credit Note</strong></h4>
        </div>
        <div class="modal-body">
        	<div class="col-md-4 col-md-offset-2">
        	   <div class="form-group">
	        	 <label class="control-label col-md-6 font-16">Credit Note Number : </label>
	        	 <div class="col-md-6" style="margin-top: 1%;">
	        	 	<input type="text" name="creditNoteNumber" id="creditNoteNumber" placeholder="Credit Note Number" class="form-control" autocomplete="off">
	        	 </div>
	        	</div>
        	</div>
        	<div class="col-md-4">
        	   <div class="form-group">
	        	 <label class="control-label col-md-4 col-md-offset-2 font-16">Credit For : </label>
	        	 <div class="col-md-6" style="margin-top: 1%;">
	        	 	<select id="credit_for" name="credit_for" class="form-control">
	        	 		<option value="">--Select--</option>
	        	 		<option value="QUANTITY">Quantity</option>
	        	 		<option value="PRICE">Price</option>
	        	 		<option value="CONVEYANCE">Conveyance</option>
	        	 	</select>
	        	 </div>
	        	</div>
        	</div>
        	<div class="clearfix"></div>
        	<div id="quantityDiv">
        		<div class="table-responsive protbldiv Mrgtop20">  
					<table id="doInventoryTableId" class="table table-new pro-table tbl-width-inwards" ">
						<thead class="cal-thead-inwards">
							 <tr>
								<th>S NO</th>
			    				<th>Product</th>
			    				<th>Sub Product</th>
			    				<th>Child Product</th>    								
			    				<th>Units Of Measurement</th>
			    				<th>Quantity</th> 
			    				<th>Price</th>
			    				<th>Basic Amount</th>
			    				<th>Discount</th>
			    				<th>Amount After Discount</th>
			    				<th>Tax </th>
			    				<th>HSNCode </th>
			    				<th>Tax Amount</th>
			    				<th>Amount After Tax</th>
			    				<th>Other Or Transport Charges</th>
			    				<th>TaxOn Other Or Transport Charges</th>
			    				<th>Other Or Transport Charges After Tax </th>
			    				<th>Total Amount </th>
								<th>Actions</th>
							</tr>
						</thead>
				        <tbody class="tbl-fixedheader-tbody">
				             <tr id="debitnotetablerow1" class="debitnotetablerow">
				        	 	<td>						
									<div id="snoDivId1">1</div>
								</td>
								<td>
									<select id="combobox1" name="Product1" class="form-control  btn-visibilty1 btn-loop1"  >
								    		<option value="">Select one...</option>
								    		<%			
								    		
								    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
								    			for(Map.Entry<String, String> prods : prod.entrySet()) {
												String prodIdAndName = prods.getKey()+"$"+prods.getValue();
											%>
												<option title=" <%= prods.getValue() %>"data-toggle="tooltip" data-placement="bottom" value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
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
								<td>
									 <input type="text"  id="stQantityPOP1"    name="quantity1" onblur="calculatequantitybaseinpop(1)"  class="form-control" autocomplete="off"/>  
			                    </td>					
								<td>
									<form:input path="" id="PriceId1" name="PriceId1" onkeypress="return validateNumbers(this, event);"  class="form-control" readonly="true"/>
								</td>
								<td>
									<form:input path="" id="BasicAmountId1" name="BasicAmountId1" class="form-control"  onkeypress="return validateNumbers(this, event);" readonly="true"/>
								</td>
								<td>
									<form:input path="" id="Discount1" name="Discount1" class="form-control"  onkeypress="return validateNumbers(this, event);" value=""  readonly="true"/>
								</td>
								<td>
									<form:input path="" id="amtAfterDiscount1" name="amtAfterDiscount1" class="form-control"  onkeypress="return validateNumbers(this, event);" readonly="true"/>
								</td>
								<td>
								<input type="hidden"  id="TaxId1"  name ="TaxId1" class="form-control">
								<input type="text"  id="TaxIdHidden1"  class="form-control"  name ="TaxIdHidden1"  readonly="true">
								<%-- 	<form:select path="" id="TaxId1"  name ="TaxId1"    onchange="calculateChangedTaxAmount(1)" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" readonly="true">
										<form:option value="${GetProductDetails.taxId}$${GetProductDetails.tax}" >${GetProductDetails.tax}</form:option>
								    		<%	
								    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
								    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
												String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
											%>
												<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
								    		<% } %>
									
									
									</form:select> --%>
								</td>
								<td>
									<form:input path="" id="HSNCodeId1" name="HSNCodeId1"  class="form-control" autocomplete="off" readonly="true"/>
								</td>
								<td>
									<form:input path="" id="TaxAmountId1" name="TaxAmountId1" readonly="true" class="form-control"/>
								</td>
								<td>
									<form:input path="" id="AmountAfterTaxId1" name="AmountAfterTaxId1" readonly="true" class="form-control"/>
								</td>
								<td>
									<form:input path="" id="OtherOrTransportChargesId1" name="OtherOrTransportChargesId1" readonly="true" class="form-control" value="0.00"/>
								</td>
								<td>
									<form:input path="" id="TaxOnOtherOrTransportChargesId1" name="TaxOnOtherOrTransportChargesId1" readonly="true" class="form-control" value="0.00"/>
								</td>
								<td>
									<form:input path="" id="OtherOrTransportChargesAfterTaxId1" name="OtherOrTransportChargesAfterTaxId1" readonly="true" class="form-control" value="0.00"/>
								</td>
								<td>
									<input type="text"   class='form-control totalAmount' id='TotalAmountId1'  name='TotalAmountId1'    readonly="true" class="form-control"/>
								<td>
									<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow(1)" class="btnaction"><i class="fa fa-plus"></i></button>
									<button type="button" style="display: none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteDebitNoteRow(this, 1)" ><i class="fa fa-trash"></i></button>
								</td>
							</tr>							
				          </tbody>
						 </table>
					  </div> 
			       	</div>
			       	<div id="priceDiv" style="height: 100px;margin-bottom:15px;">
			        	<div class="col-md-8 col-md-offset-2" style="border: 1px solid black;padding: 30px;background: #ccc;margin-bottom: 15px;">
			        		<div class="form-group">
					         <label class="control-label col-md-offset-3 col-md-3 font-16 text-right">Enter Price</label>
					         <div class="col-md-1 colonclass">:</div>
					         <div class="col-md-3 no-padding-left" style="margin-top: 1%;">
					         	<input type="text" name="creditNotePrice" id="creditNotePrice" onkeypress="return isNumberCheck(this, event)" onfocusout="checkingNumberOnPaste(this.id)" placeholder="Credit Note Price" class="form-control" autocomplete="off">
					        </div>
					       </div>
			        	</div>
			       	</div>
			       	<div id="convenceDiv" style="height: 100px;margin-bottom:15px;">
			        	<div class="col-md-8 col-md-offset-2" style="border: 1px solid black;padding: 30px;background: #ccc;margin-bottom: 15px;">
			        	   <div class="form-group">
					         <label class="control-label col-md-offset-2 col-md-4 font-16 text-right">Enter Conveyance Amount</label>
					         <div class="col-md-1 colonclass">:</div>
					         <div class="col-md-3 no-padding-left" style="margin-top: 1%;">
					         	<input type="text" name="creditNoteConveyanceAmount" onkeypress="return isNumberCheck(this, event)" onfocusout="checkingNumberOnPaste(this.id)" id="creditNoteConveyanceAmount" placeholder="Credit Note Conveyance Amount" class="form-control" autocomplete="off">
					        </div>
					       </div>
			        	</div>
			       	</div>        	
			       </div>
			      <div class="modal-footer">
			         <div class="text-center center-block">
			           <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
			           <button type="button" class="btn btn-warning" onclick="debitNoteSubmit()">Submit</button>
			         </div>
			      </div>
			     </div>
			    </div>
			  </div>
			<!-- *****************************************************************************************	 -->
				
			<div class="clearfix"></div><br/>
			<div class="table-responsive "> <!-- protbldiv -->
			<table id="doInventoryChargesTableId" class="table table-new tbl-width-inwards-half"> <!-- pro-table -->
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
				<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
					<tr id="chargesrow${GetTransportDetails.strSerialNumber}" class="conveyanceRow">
						<td>						
							<div id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
						</td>
						<td>						
								<form:select path="" id="Conveyance${GetTransportDetails.strSerialNumber}" name="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" readonly="true" >
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
							<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="number" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})" placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass" autocomplete="off" readonly="true"/>
						</td>
						<td>
							<form:select path="" id="GSTTax${GetTransportDetails.strSerialNumber}" name="GSTTax${GetTransportDetails.strSerialNumber}" class="form-control GSTClass " value="5%" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})" readonly="true" >
								<form:option value="${GetTransportDetails.GSTTaxId1}$${GetTransportDetails.GSTTax1}">${GetTransportDetails.GSTTax1}</form:option>
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
							<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
							<form:input path=""  type="hidden" id="actionStatus${GetTransportDetails.strSerialNumber}" name="TransportconveyanceInvoice${GetTransportDetails.strSerialNumber}" class="hiddentablestikeout" value="S"/>
						</td>					  
						<td>
							<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
							<button type="button" name="addEditItemBtn" id="addEditChargesItemBtnId1" class="btnaction" onclick="editRow(this,${GetTransportDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button> 
							<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this,${GetTransportDetails.strSerialNumber})" ><i class="fa fa-trash"></i></button>
					   	</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="Mrgtop20 no-padding-left">
		<div class="col-md-6 no-padding-left">
			<div class="">
				<div class="files_place">
					<input type="file" id="file_select1" name="file" class="selectCount Mrgtop10" style="float: left;" accept="application/pdf,image/*" onchange="filechange(1)" />
					<button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float: left; display: none;" onclick="filedelete(1)">
						<i class="fa fa-close"></i>
					</button>
				</div>
				<div class="clearfix"></div>
				<button type="button" id="Add"	class="btn btn-success btn-xs Mrgtop10"	style="display: none;" onclick="addFile()">
					<i class="fa fa-plus"></i> Add File
				</button>
			</div>
		</div>
									<!-- 
***************************************************note for customer requirement***************************************** -->
			<div class="col-md-6 no-padding-left no-padding-right">
			 <label class="control-label col-md-3 no-padding-left no-padding-right"><%= note %> <%= colon %></label>
					<div class="col-md-9 no-padding-left no-padding-right">
						<textarea name="Note" id="NoteId" class="form-control" style="resize:vertical;"></textarea> 
					</div>
			 <div class="clearfix"></div>
			<div class="col-md-12 text-right hidden-xs hidden-sm no-padding-right Mrgtop20 btn-bottom"  style="margin-bottom:30px;margin-top:30px;">
					<div class="col-md-12 text-right" style="display:none;"><div class="col-md-5 font-18"><strong>PO Grand Total</strong></div><div class="col-md-1 font-18"><strong>:</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-md-6 font-18"  ><strong>${totalAmount}</></div></div>
					<div class="col-md-12 text-right" ><div class="col-md-5 text-left font-18"><strong>Products Amount</strong></div><div class="col-md-1 font-18"><strong>:</strong></div><div class="col-md-5 finalAmountDiv font-18" id="finalAmntDiv" name ="finalAmntDiv"  ><strong>${totalAmount}</strong></div></div>
					<div class="col-md-12 text-right" ><div class="col-md-5 text-left font-18"><strong>Credit Note</strong></div><div class="col-md-1 font-18"><strong>:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-md-5 font-18" ><strong>0</strong></div></div>
					<div class="col-md-12 text-right"><div class="col-md-5 text-left font-18"><strong>Total Invoice Amount</strong></div><div class="col-md-1 font-18"><strong>:</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-md-5 font-18" ><strong>${totalAmount}</strong></div></div>
				<input type="hidden" id="1" value="${GetProductDetails.finalamtdiv}"/>
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
																	 	
			
			<!-- hidden for desktop devices -->
		    <div class="col-xs-12 hidden-md hidden-lg text-left no-padding-left" style="margin-bottom:30px;margin-top:30px;">
					<div class="col-xs-12 no-padding-left" style="display:none;"><div class="col-xs-12"><strong>PO Grand Total :</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-xs-12 no-padding-left"  >${totalAmount}</div></div>
					<div class="col-xs-12 no-padding-left"><div class="col-xs-12 no-padding-left"><strong>Products Amount :</strong></div><div class="col-xs-12 finalAmountDiv no-padding-left" id="finalAmntDiv" name ="finalAmntDiv"  ><strong>${totalAmount}</strong></div></div>
					<div class="col-xs-12 no-padding-left" ><div class="col-xs-12 no-padding-left"><strong>Credit Note:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-xs-12 no-padding-left" >0</div></div>
					<div class="col-xs-12 no-padding-left"><div class="col-xs-12 no-padding-left"><strong>Total Invoice Amount :</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-xs-12 no-padding-left" >${totalAmount}</div></div>
				    <input type="hidden" id="1" value="${GetProductDetails.finalamtdiv}"/>
			</div>
			</div>
			<!-- hidden for desktop devices -->	
			<div class="clearfix"></div>
				<div class="col-md-12 text-center center-block btn-bottom">
					<input type="button"  onclick="createInvoice()"  class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()"  value="Submit" id="saveBtnId" ">
					<input type="button" class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()"  value="Cal" id="saveBtnId" ">
					<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
					<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
				</div>
		</form:form>
	</div>
	</div>
	</div>
	</div>
	
		
		
		<!-- jQuery -->
		
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/InwardsfromCreatePO.js" type="text/javascript"></script>
        <script src="js/sidebar-resp" type="text/javascript"></script>
        <script src="js/numberCheck.js" type="text/javascript"></script>
		
		
		<script>
		
		$(document).ready(function() {
			$('[data-toggle="tooltip"]').tooltip();  
			$(".up_down").click(function(){ 
				$(function() {
					for(i=1;i<=100;i++){	$("#combobox"+i).combobox();    
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
				/* $("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed'); */
	/* 		 	$('#product'+i).prop('readonly', true).css('cursor','not-allowed');;
				$('#subProduct'+i).prop('readonly', true).css('cursor','not-allowed');;
				$('#childProduct'+i).prop('readonly', true).css('cursor','not-allowed');; */
				/* $('.btn-loop'+i).closest('td').find('input').attr('disabled', 'disabled'); */
				$('disable-class'+i).closest('td').find('input').attr('disabled', 'disabled');
			 	/* $('#mainQantity'+i).prop('readonly', true); */
			 	$('#taxAmount'+i).prop('readonly', true);
				$('#UnitsOfMeasurementId'+i).prop('readonly', true);
				$('#price'+i).prop('readonly', true);
				/* $('#BasicAmountId'+i).prop('readonly', true); */
			     $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').removeClass('hide'); 
			    }
			
		});
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			
		
			$(document).ready(function(){
			    $("#UnitsOfMeasurementId1").select(function(){
			        alert("Text marked!");
			    });
			});
			
		 	$('#Conveyance1').change(function() {
				if ($(this).val() == "999$None"){
				  // Updating text input based on selected value
				  $(".noneClass").val(0);
				  $(".GSTClass").val( '1$0%');
				  }else{
					  $(".noneClass").val( " ");
				  }
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
    /*     alert(next +'before'); */
        $("#countOftermsandCondsfeilds").val(next);  
      /*   alert(next +'after'); */
        var countempty= $("#countOftermsandCondsfeilds").val(next);  
        if(countempty == 1){debugger;
        	 $("#countOftermsandCondsfeilds").val(next)==1;
        }
     
        
        //alert("next "+next);
        
        
            $('.remove-me').click(function(e){
                e.preventDefault();
                var fieldNum = this.id.charAt(this.id.length-1);
                var fieldID = "#field" + fieldNum;
                $(this).remove();
                $(fieldID).remove();
            });
    });
    

    
});

function removeRow(rowId){debugger;
alert("Do you want to remove the Product");

/*	document.getElementById("ractionValueId"+rowId).value = "R";*/

$("#tr-class"+rowId).addClass('strikeout');
$("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoDivId'+rowId).removeAttr('id');	
$('#PriceId'+rowId).removeAttr('id');
$('#TaxAmountId'+rowId).removeAttr('id');
$('#BasicAmountId'+rowId).removeAttr('id');
$('#tax'+rowId).removeAttr('id');
$('#QuantityId'+rowId).removeAttr('id');
$('#AmountAfterTaxId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesId'+rowId).removeAttr('id');
$('#TaxOnOtherOrTransportChargesId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesAfterTaxId'+rowId).removeAttr('id');
$('#TotalAmountId').removeAttr('id');




}

//******** Method for validating the discount **************

function validatediscountcount(rowId){
	var discountval =$('#Discount'+rowId).val();
		if (discountval > 100){
			 alert("Please enter the discount below 100");
		}
	
	
}
$(function() {
	  $(".ExpiryDate").datepicker({ 
		  dateFormat: 'dd-M-y',
		  minDate:0,
	  changeMonth: true,
   changeYear: true
	  });
});

/*script for file upload*/
function addFile(){
  var classlength=$(".selectCount").length;
  var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
  if($("#file_select"+classLastId).val()==""){
	 alert("please select file");
	 $("#file_select"+classLastId).focus();
	 return false;
  }
  if(classlength>7){
	alert("You can't uoload more than eight files");
	return false;
  }
  var btnid = $(".selectCount:last").attr("id").split("file_select")[1];
  var dynamicId = parseInt(classLastId) + 1;
  $(".files_place").append('<div class="clearfix"></div><input type="file" id="file_select'+dynamicId+'" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange('+dynamicId+')"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn'+dynamicId+'" style="float:left;display:none;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
}
//this is for file change to display close button
function filechange(id){
	var size_file = ($("#file_select"+id))[0].files[0]
	file_size = size_file.size;
	if((size_file.type)=='application/pdf'){
		if((file_size/1024)<=1024 && (size_file.type)=='application/pdf'){$("#close_btn"+id).show();
		$("#Add").show();}
		else{
			alert("Your file size"+file_size+ "So Please upload Below this 1MB");
			$("#file_select"+id).val("");
		    return false;	
		}
	}
	else{
  $("#close_btn"+id).show();
  $("#Add").show();}
}
//this is for to delete the file
function filedelete(id){
  var classlength=$(".selectCount").length;
  if(classlength==1){
	 $("#file_select"+id).val("");
	 $("#close_btn"+id).hide();
	 $("#Add").hide();
	 return false;
  }
  $("#file_select"+id).remove() ;
  $("#close_btn"+id).remove();
}
	/* $(document).ready(function() {

		var MaxInputs       = 100; //maximum extra input boxes allowed
		var InputsWrapper   = $("#InputsWrapper"); //Input boxes wrapper ID
		var AddButton       = $(".AddMoreFileBox"); //Add button ID

		var x = InputsWrapper.length; //initlal text box count
		var FieldCount=1; //to keep track of text box added

		//on add input button click
		$(AddButton).click(function (e) {
			
			$(this).hide();
			/* document.getElementById("countOffeilds").value = All; */
		        //max input box allowed
/* 		        if(x <= MaxInputs) {
		            FieldCount++; //text box added ncrement
		            //add input box
		            $(InputsWrapper).append('<div><input type="text" class="text-line" name="termsAndCond'+ FieldCount +'" style="width: 451px;height: 34px; margin-bottom: 10px; id="termsAndCond'+ FieldCount +'"/></div><div  ></div>'+'<div style="display: -webkit-inline-box;"><span class="glyphicon glyphicon-plus AddMoreFileBox" ></span><span class="glyphicon glyphicon-remove removeclass"></span></div>');
		            x++; //text box increment
		            
		            		            
		            $('AddMoreFileBox').html("Add field");
		            
		            // Delete the "add"-link if there is 3 fields.
		            if(x == 100) {
		                $("#AddMoreFileId").hide();
		             	$("#lineBreak").html("<br>");
		            }
		        }
		        return false;
		});

		$("body").on("click",".removeclass", function(e){ //user click on remove text
		        if( x > 1 ) {
		                $(this).parent('div').remove(); //remove text box
		                x--; //decrement textbox
		            
		            	$("#AddMoreFileId").show();
		            
		            	$("#lineBreak").html("");
		            
		                // Adds the "add" link again when a field is removed.
		                $('AddMoreFileBox').html("Add field");
		        }
			return false;
		});

		});
	 */ 
	 $(document).ready(function() {
		 $("#quantityDiv").hide();
		 $("#priceDiv").hide();
		 $("#convenceDiv").hide();	
		 $('#credit_for').change(function() {debugger;
			 var credit_for=$("#credit_for").val();
			 if(credit_for==""){
				 $("#quantityDiv").hide();
				 $("#priceDiv").hide();
				 $("#convenceDiv").hide();
			 }
			 if(credit_for=="QUANTITY"){
				 //$("#quantityDiv").show();
				 //$("#quantityDiv").fadeIn("slow");
				 $("#quantityDiv").fadeIn(1000);
				 $("#priceDiv").hide();
				 $("#convenceDiv").hide();
			 }
			 if(credit_for=="PRICE"){
				 $("#quantityDiv").hide();
				 //$("#priceDiv").show();
				// $("#priceDiv").fadeIn("slow");
				 $("#priceDiv").fadeIn(1000);
				 $("#convenceDiv").hide();		 
			 }
			 if(credit_for=="CONVEYANCE"){
				 $("#quantityDiv").hide();
				 $("#priceDiv").hide();
				 //$("#convenceDiv").show(); 
				 //$("#convenceDiv").fadeIn("slow");
				 $("#convenceDiv").fadeIn(1000);
			 }
 		});
	 });
	 //debit note modal popup submit method
	 function debitNoteSubmit(){
		 var credit_for=$("#credit_for").val();
		 if(credit_for==""){
			 swal("Error!", "Please select credit for.", "error");
			 return false;
		 }
		 if(credit_for=="QUANTITY"){
			 debugger;
			 var amount=0;
			 var validateNum=0;
			$(".totalAmount").each(function(){
				if($(this).val()>0){
					amount+=parseFloat($(this).val());
				}else{					
					validateNum++;
				}
				
			});
			if(validateNum!=0){
				swal("Error!", "Please fill the all rows.", "error");
				return false;
			}
			$("#CreditAmountDiv").text(amount.toFixed(2));
			calculateFinalAmount();
			$("#myModal").modal('hide');			
		 }
		 if(credit_for=="PRICE"){
			 var creditNotePrice=$("#creditNotePrice").val();
			 if(creditNotePrice=="" || creditNotePrice<=0){
				 swal("Error!", "Please enter credit note price.", "error");
				 return false;
			 }
			 $("#CreditAmountDiv").text(parseFloat(creditNotePrice).toFixed(2));
			 calculateFinalAmount();
			 $("#myModal").modal('hide'); 
		 }
		 if(credit_for=="CONVEYANCE"){
			 var creditNoteConveyanceAmount=$("#creditNoteConveyanceAmount").val();
			 if(creditNoteConveyanceAmount=="" || creditNoteConveyanceAmount<=0){
				 swal("Error!", "Please enter credit note conveyance amount.", "error");
				 return false;
			 }
			 $("#CreditAmountDiv").text(parseFloat(creditNoteConveyanceAmount).toFixed(2));
			 calculateFinalAmount();
			 $("#myModal").modal('hide');
		 }
	 }
	 
	 
	 //calculating final amount
	 function calculateFinalAmount(){debugger;
		 var finalAmntDiv=$("#finalAmntDiv").text();
		 var CreditAmountDiv=$("#CreditAmountDiv").text();
		 var finalval=parseFloat(finalAmntDiv)+parseFloat(CreditAmountDiv);
		 $("#toatlAmntDiv").text(finalval.toFixed(2));
	 }
	 
		</script>
</body>
</html>
