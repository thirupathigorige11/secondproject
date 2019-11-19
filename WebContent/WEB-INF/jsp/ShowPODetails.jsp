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
<style>
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
.pro-table tbody tr td,.pro-table tbody tr th{
	margin:2px 3px;
	width:100%;
	min-width:213px; 
}
 .text-line {
        background-color: transparent;
        color:#313030;
        outline: none;
        outline-style: none;
        outline-offset: 0;
        border-top: none;
        border-left: none;
        border-right: none;
        border-bottom: solid #bdb7ab 1px;
        padding: 3px 10px;
    }
 .mybtnstyles{
 height: 30px;
 margin-top: -6PX;
 border-radius: 0px !important;
 margin-bottom: 10px;
 background-color: transparent;
 }
 .myinputstyles{
 display: inline !important;
 width: 75% !important;
 border-radius: 0px !important;
 margin-bottom: 10px;
 border: none;
    box-shadow: none;
    border-bottom: 2px solid #ccc;
 }
 
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
		
		<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
	<style>
		#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	.form-group label{
    text-align: left
}
table { border-collapse: collapse; empty-cells: show; }

td { position: relative; }

tr.strikeout td:before {
  content: " ";
  position: absolute;
  top: 50%;
  left: 0;
  border-bottom: 1px solid red;
  width: 100%;
    z-index: 100px;
}

tr.strikeout td:after {
  content: "\00B7";
  font-size: 1px;
  z-index: 100px;
}

/ Extra styling /
td { width: 100px; }
th { text-align: left; }
.Btnbackground{
background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
}
.closeiconforimg {
    position: relative;
    top: -88px;
    right: 13px;
    z-index: 100;
    background-color: #e0e0e0;
    padding: 5px 5px;
    color: #000;
    font-weight: bold;
    cursor: pointer;
    text-align: center;
    font-size: 17px;
    line-height: 10px;
    border-radius: 50%;
    border: 1px solid blue;
    color: blue;
    opacity: 2;
}
.form-control1{
	float: left;
    margin-top: 3px;
    width: 222px;
    display: block;
    width: 100%;
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555;
    background-color: #fff;
    background-image: none;
    border: 1px solid #ccc;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;

}
	</style>
<script type="text/javascript">
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
<script>
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
	 
	 var discountColumn =  "<%= discount %>";
	 discountColumn = formatColumns(discountColumn);
	 //alert(discountColumn);	 
	 
	 	 var AmountAfterDiscountColumn =  "<%= AmountAfterDiscount %>";
	 	AmountAfterDiscountColumn = formatColumns(AmountAfterDiscountColumn);
	 //alert(AmountAfterTaxColumn);
	 
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
	 
	 
<%-- 	 var expiryDate=  "<%= expiryDate %>";
	 expiryDate = formatColumns(expiryDate);
	 //alert(hsnCodeColumn); --%>
	 
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
    		var dynamicSelectBoxId = "taxAmount"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", 'tax'+vfx);
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
		    div.setAttribute("name", "quantity"+vfx);
		    div.setAttribute("id", "stQantity"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx);	     */
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
		    div.setAttribute("name", "Price"+"Id"+vfx);
		    div.setAttribute("id", "price"+vfx);
		   /*  div.setAttribute("id", tableColumnName+vfx);	    */ 
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "BasicAmount"+"Id"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculatePriceAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "tax"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		   /*  div.setAttribute("name", tableColumnName+vfx); */
		    div.setAttribute("id", "amountAfterTax"+vfx);
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		 /*    div.setAttribute("id", "tax"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control' );
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", 'taxAmount'+vfx);
		    div.setAttribute("id", "TaxAmount"+vfx);
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		   /*  div.setAttribute("id", "amountAfterTax"+vfx); */
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", 'amountAfterTax'+vfx);
		 /*    div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    div.setAttribute("id", "TaxAftertotalAmount"+vfx);
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
		    div.setAttribute("name", "totalAmount"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "hsnCode"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		} /*  else if(tableColumnName == expiryDate) {
   			
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "expireDate"+vfx);
		    div.setAttribute("id", "expireDateId"+vfx);
		    div.setAttribute("class", 'form-control');
		    
		    cell.appendChild(div);
		    $("#expireDateId"+vfx).datepicker({dateFormat: 'dd-M-y'});
		   
   		}     */
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
		    div1.setAttribute("onclick", "deleteRow(this, "+vfx+")");
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


function createPO() {debugger;
	
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
/*  var valStatus = validateRowData(1);
	if(valStatus == false) {
    	return;
	} */
	calculateOtherCharges();

	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("ProductWiseIndentsFormId").action = "convertPOtoInvoice.spring";
	document.getElementById("ProductWiseIndentsFormId").method = "POST";
	document.getElementById("ProductWiseIndentsFormId").submit();
}



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
							<li class="breadcrumb-item active">Create PO</li>
						</ol>
					</div>
					
					
					<div>
	<div align="center">
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="container">
	 <div class="row">
	 <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">	
	  <div class="col-xs-12">
	  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Vendor Name : </label>
			<div class="col-sm-2 col-xs-12" >
				<form:input path="vendorName" name="vendorName" id="VendorNameId" class="form-control" value="${poDetails.vendorName}" readonly="true" />
			</div>
						<label class="control-label col-sm-2" style="width:13%;">GSTIN : </label>
			<div class="col-sm-2 col-xs-12">
				<form:input path="strGSTINNumber" name="strGSTINNumber" id="GSTINNumber" class="form-control" value="${poDetails.strGSTINNumber}" autocomplete="off" readonly="true" />
			</div>
			<label class="control-label col-sm-2" style="width:13%;">Vendor Address : </label>
			<div class="col-sm-2 col-xs-12" >
				<form:input path="vendorAddress" name="vendorAddress" id="VendorAddress" class="form-control" value="${poDetails.vendorAddress}" readonly="true" />
			</div>
			
		
			
			<div class="col-sm-2 col-xs-12" >
				<input type="hidden" id="vendorId" name="VendorId" class="form-control" value="${poDetails.vendorId}" />
			</div>
		</div>
			  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
		<!-- 	<label class="control-label col-sm-2" style="width:13%;">Indent No : : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value=${indentNo}>
			</div>
			
			 -->
		
			
			
		</div>
			  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Indent No : : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${poDetails.indentNo}">
			</div>
			
			
		 	<label class="control-label col-sm-2" style="width:13%;">SiteName : : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" id=siteName" name="SiteName" class="form-control" readonly="true" value="${poDetails.siteName}">
				<input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}">
			</div>
			
					 	<label class="control-label col-sm-2" style="width:13%;">Invoice Number :</label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" name="InvoiceNumber" id="InvoiceNumberId" class="form-control" autocomplete="off" value="">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			
		

			
			
		</div>
		
		
					  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Invoice Date :  </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" name="InvoiceDate" id="InvoiceDateId" class="form-control" autocomplete="off" value="">
			</div>
			
			
		 	<label class="control-label col-sm-2" style="width:13%;">State : </label>
			<div class="col-sm-2 col-xs-12" >
						<select name="state" id="state" class="form-control">
				              <option value="">---Select--</option>
					          <option value="1">Local</option>
					          <option value="2">Non Local</option>
						</select>
				<!-- <input type="text" name="state" id="state" class="form-control"value="Local"}>  -->
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}>  -->
			</div>
			
					 	<label class="control-label col-sm-2" style="width:13%;">PONO : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off" readonly="true" value="${poNumber}">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			
		

			
			
		</div>
		
		
							  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">PO Date :   </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text"  name="poDate" id="poDateId" class="form-control" readonly="true" value="${poDetails.poDate}">
			</div>
			
			
		 	<label class="control-label col-sm-2" style="width:13%;">E Way Bill No : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text"  name="eWayBillNo" id="eWayBillNoId" class="form-control" value=""}>
				<!--  <input type="hidden"name="eWayBillNo" id="eWayBillNoId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			
					 	<label class="control-label col-sm-2" style="width:13%;">Vehicle No : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" name="vehileNo" id="vehileNoId" class="form-control" value="">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			
		

			
		</div>
		
				
							  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Transporter Name :  </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text"  name="transporterName" id="transporterNameId" class="form-control" value="">
			</div>
			
			
		 	<label class="control-label col-sm-2" style="width:13%;">Recived Date : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text"  name="receivedDate" id="receivedDate" class="form-control" autocomplete="off" value="">
				<!-- <input type="hidden"name="eWayBillNo" id="eWayBillNoId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			
		
			
		

		</div>
		
		<div class="form-group col-xs-12" style="margin-bottom:22px;">
		</div>
	</div>
	</c:forEach>
</div>
</div>

	      <div class="clearfix"></div>
	       <div class="table-responsive protbldiv">    
	   
				<table id="doInventoryTableId" class="table pro-table" style="">
				<tr class="table_header" style="  background: #eae7e7;">
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
					<!-- <th>Expire Date</th> -->
					<th>Actions</th>
				</tr>
				<input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
	        	 	
	        	 	
	        	 	<input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="indentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
	        	 	
	        	 	
	        	 	
	        	 	 <tr id="tr-class">
	        	 	<td>	
	        	  						
				       <div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="product${GetProductDetails.serialno}" readonly="true" name="Product${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1 value="${GetProductDetails.productName}">
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
					<form:select path="" id="subProduct${GetProductDetails.serialno}"  readonly="true" name="SubProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
					<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
					</form:select>
					</td>
					<td>
						<form:select path="" id="childProduct${GetProductDetails.serialno}" readonly="true" name="ChildProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
						<option value="${GetProductDetails.child_ProductId}$${GetProductDetails.child_ProductName}">${GetProductDetails.child_ProductName}</option>
					</form:select>
					</td>
					
					<td>
					<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="UnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
						<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
					</form:select>
					</td>
					<td >
                        <input type="text"  id="stQantity${GetProductDetails.serialno}"    name="quantity${GetProductDetails.serialno}" onchange="calculatequantitybase(${GetProductDetails.serialno})" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					</td>					
				
					<td>
					<input type="text" id="price${GetProductDetails.serialno}"  name ="Price${GetProductDetails.serialno}"   onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}"/>
					</td>
					<td> 
					<input type="text" id="BasicAmountId${GetProductDetails.serialno}"  name ="BasicAmount${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.basicAmt}"/>
					</td>
						<td> 
					<input type="text" id="Discount${GetProductDetails.serialno}" name="Discount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(${GetProductDetails.serialno});" readonly="true" class="form-control disable-class1" value="${GetProductDetails.strDiscount}"/><i  style="margin-top: -21px;margin-left: 171px;" class="fa fa-percent"></i>

					
					</td>
<%-- 						<td>
						<form:select path="" id="Discount${GetProductDetails.serialno}"  name ="Discount${GetProductDetails.serialno}"   readonly="true" onchange="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" >
							<form:option value="2$5%" >${GetProductDetails.strDiscount}</form:option>
					    		<%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
					    		<% } %>
						
						
						</form:select>
					</td> --%>
					
					<td>
					<input type="text" id="amtAfterDiscount${GetProductDetails.serialno}"  name ="amtAfterDiscount${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.strAmtAfterDiscount}"/>
					</td>
					
					
					<td>
						<form:select path="" id="taxAmount${GetProductDetails.serialno}"  name ="tax${GetProductDetails.serialno}" readonly="true"   onchange="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" >
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
					<input type="text" id="hsnCode${GetProductDetails.serialno}"  name ="hsnCode${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
					</td>
					<td>
					<input type="text" id="TaxAmount${GetProductDetails.serialno}"  name ="taxAmount${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
					</td>
					<td>
					<input type="text" id="TaxAftertotalAmount${GetProductDetails.serialno}"  name ="amountAfterTax${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
					</td>
					<td>
					    <input type = "text" id="OtherOrTransportChargesId${GetProductDetails.serialno}"  name="otherOrTransportCharges${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.othercharges1}"/>
					</td>
					<td>
			         	<input  type="text" id="TaxOnOtherOrTransportChargesId${GetProductDetails.serialno}"  name="taxOnOtherOrTransportCharges${GetProductDetails.serialno}"  readonly="true"  class="form-control" value="${GetProductDetails.taxonothertranportcharge1}"/>
					</td>
					<td>
					<input type="text" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.serialno}"  name="otherOrTransportChargesAfterTax${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.otherchargesaftertax1}"/>
					</td>
					<td>
					    <input type="text"   class='ttamount form-control' id='TotalAmountId${GetProductDetails.serialno}'  name='totalAmount${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.totalAmount}"/>
					 <input type="hidden"   id='indentCreationDetailsId${GetProductDetails.serialno}'  name='indentCreationDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.indentCreationDetailsId}"/>
					</td>
							
<%-- 					<td>
					<form:input path="expireDate" id="expireDate1" class="form-control" autocomplete="off" readonly="true" value=""/>
					</td>   --%>
					
					<td style="display:none">
					<input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
					</td>  
					
					<td style="display:none">
					<input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
					</td>
					
					
					
					<td>
					<%-- 	<button type="button" name="addremoveItemBtn${GetProductDetails.serialno}" id="addremoveItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-remove"></i></button> --%>
						<%-- <button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button> --%>
						<button type="button" name="editItemBtn${GetProductDetails.serialno}" value="Edit Item" id="editItem${GetProductDetails.serialno}" class="btnaction" onclick="editInvoiceRow(${GetProductDetails.serialno})" ><i class="fa fa-pencil"></i></button> 
				<%-- 		<button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button> --%>
						<input type="hidden" name="addNewrowItemBtn${GetProductDetails.serialno}" id="addNewrowItemBtn${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"/>
				
					</td>
				</tr>
		
		
			

		
			
		 	</c:forEach> 
	</table>
	</div>

<%-- 				<div class="table-responsive" style="border: 1px solid #b7b7b7; margin-top: 65px;">
					<table id="tblnotification"	class="table table-striped table-bordered" cellspacing="0">
						<thead>
							<tr class="" style="background: rgb(193, 193, 193);">
								<th>S.No</th>
								<th>ProductName</th>
								<th>SubProductName</th>
								<th>ChildProductName</th>
								<th>Measurement</th>
								<th>PO Pending Quantity</th>
								<th>Quantity</th>
								<th>HSN Code</th>
								<th>Price</th>
								<th>Basic Amount</th>
								<th>Discount</th>
								<th>Amount after Discount</th>
								<th>Tax</th>
								 <th>Tax Amount</th> 
								<th>Amount After Tax</th>
								<th>Other or Transport charges</th>
    							<th>Tax On OtherOr Transport Charges</th>
    							<th>Other Or Transport ChargesA fterTax</th>
								<th>Total Amount</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody>
										<input type="hidden" id="noofrows" value="${requestScope['listOfGetProductDetails'].size()}" />
				
	        	 	 <tr id="tr-class">
	        	  	<td>	
	        	  						
				       <div id="snoDivId">1</div> 
						<form:input  id= "snoDivId1" path="strSerialNo"  class="form-control" readonly="true" value="1"/>
					</td>
					<td>
						<select id="combobox" name="Product" class="form-control  btn-visibilty btn-loop1 value="">
						<option value="${GetProductDetails.productId}$${GetProductDetails.product}">${GetProductDetails.product}</option>	
							<option value="">${GetProductDetails.product}</option>
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
					<form:select path="" id="comboboxsubProd1" name="SubProduct1" class="form-control  btn-visibilty1 btn-loop1">
					<option value="${GetProductDetails.subProductId}$${GetProductDetails.subProduct}">${GetProductDetails.subProduct}</option>
					</form:select>
					</td>
					<td>
						<form:select path="" id="comboboxsubSubProd1" name="ChildProduct1" class="form-control  btn-visibilty1 btn-loop1">
						<option value="${GetProductDetails.childProductId}$${GetProductDetails.childProduct}">${GetProductDetails.childProduct}</option>
					</form:select>
					</td>
					
					<td>
					<form:select path="" id="UnitsOfMeasurementId1"  name="UnitsOfMeasurement1"  class="form-control  btn-visibilty1 btn-loop1"  value="${GetProductDetails.unitsOfMeasurement}">
						<option value="${GetProductDetails.unitsOfMeasurementId}$${GetProductDetails.unitsOfMeasurement}">${GetProductDetails.unitsOfMeasurement}</option>
					</form:select>
					</td>
					<td >
                        <input type="text"  id="QuantityId1"    name="quantity1" onblur="calculateTotalAmount(1)"   class="form-control" value="${GetProductDetails.quantity}"/> 
					</td>					
				
					<td>
					<input type="text" id="PriceId1"  name ="PriceId1"   onblur="calculateTotalAmount(1)"  class="form-control" value="${GetProductDetails.price}"/>
					</td>
					<td> 
					<input type="text" id="BasicAmountId1"  name ="BasicAmountId1"  onblur="calculateTaxAmount(1)" class="form-control" value="${GetProductDetails.basicAmount}"/>
					</td>
					
					<td>
						<form:select path="" id="tax1"  name ="tax1"   readonly="true" onchange="calculateTaxAmount(1)" class="form-control" >
							<form:option value="2$5%" >5%</form:option>
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
					<input type="text" id="hsnCode1"  name ="hsnCode1"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
					</td>
					<td>
					<input type="text" id="TaxAmountId1"  name ="taxAmount1"  readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
					</td>
					<td>
					<input type="text" id="AmountAfterTaxId1"  name ="amountAfterTax1"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
					</td>
					<td>
					    <input type = "text" id="OtherOrTransportChargesId1"  name="otherOrTransportCharges1"  readonly="true" class="form-control" value="0"/>
					</td>
					<td>
			         	<input  type="text" id="TaxOnOtherOrTransportChargesId1"  name="taxOnOtherOrTransportCharges1"  readonly="true"  class="form-control" value="0"/>
					</td>
					<td>
					<input type="text" id="OtherOrTransportChargesAfterTaxId1"  name="otherOrTransportChargesAfterTax1"  readonly="true" class="form-control" value="0"/>
					</td>
					<td>
					    <input type="text"  id="TotalAmountId1"  name="totalAmount1"   readonly="true" class="form-control" value="0"/>
					</td>
						<td>
					    <input type="text"  id="TotalAmountId1"  name="totalAmount1"   readonly="true" class="form-control" value="0"/>
					</td>	
						<td>
					    <input type="text"  id="TotalAmountId1"  name="totalAmount1"   readonly="true" class="form-control" value="0"/>
					</td>	
						<td>
					    <input type="text"  id="TotalAmountId1"  name="totalAmount1"   readonly="true" class="form-control" value="0"/>
					</td>	 --%>
						
				<%-- 	<td>
					<form:input path="expireDate" id="expireDate1" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.expireDate}"/>
					</td>   --%>
					
					<td style="display:none">
					<input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
					</td>  
					
					<td style="display:none">
					<input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
					</td>
					
					
					
<%-- 					<td>
						<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId1" class="btnaction" onclick="removeRow(1)" ><i class="fa fa-remove"></i></button>
						<button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
						<button type="button" name="editItemBtn" value="Edit Item" id="editItem1" class="btnaction" onclick="editInvoiceRow(1)" ><i class="fa fa-pencil"></i></button> 
						<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<input type="hidden" name="addNewrowItemBtn" id="addNewrowItemBtn1" onclick="appendRow()" class="btnaction"/>
				
					</td> --%>
			
						</tbody>
							<%-- <%
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {
                                    int  purchaseDeptIndentProcessSeqId = 0;
									String strProduct = "";
									String strSubProduct = "";
									String strChildproduct = "";
									String strProductId = "";
									String strSubProductId = "";
									String strChildproductId = "";
									
									String strtotalAmount = "";
									String stQantity = "";
									String strMesurment = "";
									String strSiteId = "";
									String siteName = "";
									String strSerialNumber = "";
									String strMouseOverDate = "";
                                    String strIndentNo = "";
									String strissuedQty = "";
									String pendingQuantity = "";
									String poIntiatedQuantity = "";
									String requestQantity = "";
									String strMeasurmentId = "";
									
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();

										strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();
										 strProductId = objProductDetails.getProductId() == null ? "" : objProductDetails.getProductId().toString();
										 strSubProductId = objProductDetails.getSub_ProductId() == null ? "" : objProductDetails.getSub_ProductId().toString();
										 strChildproductId = objProductDetails.getChild_ProductId() == null ? "" :  objProductDetails.getChild_ProductId().toString();
										 strMeasurmentId = objProductDetails.getMeasurementId() == null ? "" : objProductDetails.getMeasurementId();
										
										 strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();
										strProduct = objProductDetails.getProductName() == null ? "" : objProductDetails.getProductName().toString();
										strSubProduct = objProductDetails.getSub_ProductName() == null ? "" : objProductDetails.getSub_ProductName().toString();
										strChildproduct = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										pendingQuantity = objProductDetails.getQuantity() == null ? "" : objProductDetails.getQuantity().toString();
										strMesurment = objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										siteName = objProductDetails.getSiteName() == null ? "" : objProductDetails.getSiteName().toString();										
										strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();	
										strMouseOverDate = objProductDetails.getStrOtherSiteQtyDtls() == null ? "" : objProductDetails.getStrOtherSiteQtyDtls().toString();
										strIndentNo = objProductDetails.getStrIndentId() == null ? "" : objProductDetails.getStrIndentId().toString();
										purchaseDeptIndentProcessSeqId = Integer.parseInt(objProductDetails.getPurchaseDeptIndentProcessSeqId() == null ? "0" : objProductDetails.getPurchaseDeptIndentProcessSeqId().toString());
										poIntiatedQuantity = objProductDetails.getPendingQuantity() == null ? "" : objProductDetails.getPendingQuantity().toString();
										requestQantity = objProductDetails.getRequestQantity() == null ? "" : objProductDetails.getRequestQantity().toString();
										
										
										//out.println(" mouse over data "+strMouseOverDate);
										
										out.println("<div style='display: none;'>");
									
										out.println("<input type='hidden' name='productId"+strSerialNumber+"' value='"+strProductId+"' />");
										out.println("<input type='hidden' name='subProductId"+strSerialNumber+"' value='"+strSubProductId+"' />");
										out.println("<input type='hidden' name='childProductId"+strSerialNumber+"' value='"+strChildproductId+"' />");
										out.println("<input type='hidden' name='unitsOfMeasurementId"+strSerialNumber+"' value='"+strMeasurmentId+"' />");
										
										
										out.println("<input type='hidden' name='ProductName"+strSerialNumber+"' value='"+strProduct+"' />");
										out.println("<input type='hidden' name='SubProductName"+strSerialNumber+"' value='"+strSubProduct+"' />");
										out.println("<input type='hidden' name='ChildProductName"+strSerialNumber+"' value='"+strChildproduct+"' />");
										out.println("<input type='hidden' name='unitsOfMeasurement"+strSerialNumber+"' value='"+strMesurment+"' />");
										out.println("<input type='hidden' name='pendingQuantity"+strSerialNumber+"' value='"+pendingQuantity+"' />");
										out.println("<input type='hidden' name='indentNo"+strSerialNumber+"' value='"+strIndentNo+"' />");
										out.println("<input type='hidden' name='purchaseDepartmentIndentProcessSeqId"+strSerialNumber+"' value='"+purchaseDeptIndentProcessSeqId+"' />");
										out.println("<input type='hidden' name='poIntiatedQuantity"+strSerialNumber+"' value='"+poIntiatedQuantity+"' />");
										out.println("<input type='hidden' name='requestQantity"+strSerialNumber+"' value='"+requestQantity+"' />");
										
										
										//out.println("<input type="hidden" name="poIntiatedQuantity${element.strSerialNumber}" value="${element.poIntiatedQuantity}" />");
										
										out.println("</div>");
										out.println("<tr>");
									//	out.println("<td><input type='checkbox' name='checkboxname"+strSerialNumber+"' value='checked'></input></td>");
										if (strSearchType.equals("ADMIN")) {

											out.println("<td title = '" + strMouseOverDate + "'>");

										} else {
											out.println("<td>");

										}
										out.println("<div id='snoDivId"+strSerialNumber+"'>"+strSerialNumber+"</div>");
										out.println("</td>");
										
										out.println("<td>");
										
										out.println(strProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strSubProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strMesurment);
										out.println("</td>");
										
										out.println("<td>");
										out.println(pendingQuantity);
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' id='stQantity"+strSerialNumber+"' name='stQantity"+strSerialNumber+"'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' name='hsnCode"+strSerialNumber+"'  />");
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' id='price"+strSerialNumber+"' name='price"+strSerialNumber+"' onchange='calculateTotalAmount("+strSerialNumber+")' />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='BasicAmountId"+strSerialNumber+"' name='basicAmount"+strSerialNumber+"'  />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input  type='text' id='tax"+strSerialNumber+"' name='Discount"+strSerialNumber+"'   onblur='calculateDiscountAmount("+strSerialNumber+")'/>");
											/* 	"<option value="+1+">--Select--</option><option value="+0+">0</option><option value="+1+">1</option><option value="+2+">2</option><option value="+3+">3</option><option value="+4+">4</option><<option value="+5+">5</option><option value="+6+">6</option><option value="+7+">7</option><option value="+8+">8</option><option value="+9+">9</option><option value="+10+">10</option>/select>"); */
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='amountAfterTax"+strSerialNumber+"' name='amtAfterDiscount"+strSerialNumber+"'  />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<select id='taxAmount"+strSerialNumber+"'  name='tax"+strSerialNumber+"'  onchange='calculateTaxAmount("+strSerialNumber+")')>"+
										"<option value=''>--Select--</option><option value='1$0'>0</option><option value='2$5'>5</option><option value='3$12'>12</option><option value='4$18'>18</option><option value='5$28'>28</option></select>");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='TaxAmount"+strSerialNumber+"' name='taxAmount"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxAftertotalAmount"+strSerialNumber+"' name='amountAfterTax"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesId"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxOnOtherOrTransportChargesId"+strSerialNumber+"'   />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' class='ttamount' id='TotalAmountId"+strSerialNumber+"'  name='totalAmount"+strSerialNumber+"'  />");
										out.println("</td>");


									}
								}
							%> --%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		
					
			
			
						
	<!-- <div>			<% /*	List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

				ProductDetails objProductDetails = null;
				Iterator itr = totalProductList.iterator();
				int purchaseDeptIndentProcessSeqId=0;
				String strSerialNumber="";
				while (itr.hasNext()) {  
					objProductDetails = (ProductDetails) itr.next();

					strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();			
                    purchaseDeptIndentProcessSeqId = Integer.parseInt(objProductDetails.getPurchaseDeptIndentProcessSeqId() == null ? "0" : objProductDetails.getPurchaseDeptIndentProcessSeqId().toString());
 			
 			  }
                     String hiddenFieldHtml1="<input type=\"hidden\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); */ %>  
			</div>  -->
				<br><br><br>
							<div class="clearfix"></div><br/>
			<div class="table-responsive protbldiv">
			<table id="doInventoryChargesTableId" class="table pro-table">
				<tr style="  background: #c1c1c1;">
					<th><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<th><%= trasportInvoice %></th>
    			
    			
    			
		<%-- 		<label class="control-label col-sm-2 col-xs-12">Invoice Date : </label>
			<div class="col-sm-3 col-xs-12" >
				<form:input path="InvoiceDate" id="InvoiceDateId" class="form-control"/>
			</div --%>
    				<th><%= actions %></th>
  				</tr>
			<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
				<tr>
					<td>						
						<div id="snoChargesDivId1">${GetTransportDetails.strSerialNumber}</div>
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
						<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="number"  placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" readonly="true" class="form-control noneClass" autocomplete="off"/>
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
						<form:input path="" type="number"  name="AmountAfterTaxx${GetTransportDetails.strSerialNumber}" id="AmountAfterTax${GetTransportDetails.strSerialNumber}" placeholder="Amount After Tax"  value="${GetTransportDetails.amountAfterTaxx1}" readonly="true" class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()" readonly="true" class="form-control" autocomplete="off"/>
					</td>
					
					
					 
					<td>
						<!-- <button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button> -->
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
				</c:forEach>
			</table>
			</div><br/><br/><br/>
			
<div class="file-upload" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
	<input type="file" name="file"><br><input type="file" name="file"><br>
	<input type="file" name="file"><br><input type="file" name="file"><br>
</div>
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
				
				
			<div class="container">
			
					<div class=""><span class="h4" style="margin-top:20px; margin-left: 71%;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv"  ></span></div>
				<input type="hidden" id="${GetProductDetails.serialno}" />
				</div> <br><br>
				<br><br><br><br>
				<!-- <div class="col-sm-3 pt-10">
					<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>   -->  
				<div class="col-sm-2 pt-10"> 
					<input type="button"  style="position: absolute;margin-top: -40px;width: 161px;" onclick="createPO()"  class="btn btn-warning btn btn-info  col-sm-12 form-submit modelcreatePO" onclick="calculateOtherCharges()" value="Submit" id="saveBtnId" ">
					<!-- <input type="button"  style="position: absolute;margin-top: -40px;width: 161px;" onclick="createModelPOPUP()"  class="btn btn-warning btn btn-info  col-sm-12 form-submit modelcreatePO" onclick="calculateOtherCharges()" data-target="myModal" value="Create PO" id="saveBtnId" ">  -->
				</div>
				
				<div class=" col-sm-12 form-submit" >
					<div class="Btnbackground">
						<input class="btn btn-warning col-sm-2 col-sm-offset-4" style="margin-top: -40px;" type="button" value="Calculate" data-toggle="myModal"  id="calculateBtnId" onclick="calculateOtherCharges()">
					</div>
				</div>
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<!-- <input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">  -->
			

<!-- **************************************************************		 -->
			
		
			<br/>
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
             	<input type="hidden" id= "countOftermsandCondsfeilds" name="countOftermsandCondsfeilds" value="">
                    <div id="field margintopbtm"><input autocomplete="off" class="input form-control myinputstyles" id="termsAndCond1" name="termsAndCond1" type="text" data-items="8" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc";/><button id="b1" class="add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc"></span></button></div>
                </div>
            <br>
            
            </div>
        </div>
	</div>
</div>
        <!--         <div id="InputsWrapper">
           <div class="checkbox" style="display:inline;"> 
             <input id="nome" name="nome" type="text" class="text-line"  name="termsAndCond1" id="termsAndCond1" style="width: 451px;height: 34px;    display: inline;"><a href="#" class="removeclass"></a>
        	</div>
        <div id="AddMoreFileId" style="display: -webkit-inline-box;">
		<span class="glyphicon glyphicon-plus AddMoreFileBox" ></span><span class="glyphicon glyphicon-remove"></span>
        </div>
         <input type="hidden" name="numbeOfRowsToBeProcessedinPopup" value="1" id="rowId">
         </div> -->
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
		
		
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/ShowPODetails.js" type="text/javascript"></script>
		
		
		<script>
		$(document).ready(function() {	
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
			 	$('#stQantity'+i).prop('readonly', true);
			 	$('#taxAmount'+i).prop('readonly', true);
				$('#UnitsOfMeasurementId'+i).prop('readonly', true);
				$('#price'+i).prop('readonly', true);
				$('#BasicAmountId'+i).prop('readonly', true);
			     $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide'); 
			    }
			
		});
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
alert(" Do you want to remove the Product");

/*	document.getElementById("ractionValueId"+rowId).value = "R";*/

$("#tr-class").addClass('strikeout');
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

		</script>
</body>
</html>
