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
	String indentCreationDetailsId = resource.getString("label.indentCreationDetailsId");
	String vendorDescription = resource.getString("label.vendorDescription");
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="CacheClear.jsp" />  
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
	<style>
	#ccemailcheckbox,#sub-conmatiner,#conclusionHeader{margin:5px 5px 10px 15px;}
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
.form-control1{border:1px solid #ccc;}
/* css for iframe modal popup */
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}

.iframe-pdf {
  opacity: 1;
  display: block;
  width: 100%;
  height: auto;
  transition: .5s ease;
  backface-visibility: hidden;
}

.middle {
  transition: .5s ease;
  opacity: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  width:100%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
  height:100%;
}

.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.modal-lg-width{
width:95%;
}
/* text {
 background-color: #4CAF50;
 color: white; 
  font-size: 16px;
 padding: 16px 32px;
} */
.btn-fullwidth:hover{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:110px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:110px;
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent;
    background-color: transparent;
    border-color: transparent;
}
 .btn-fullwidth:active{
 color: transparent;
    background-color: transparent;
    border-color: transparent;
 }
 .btn-fullwidth.focus, .btn-fullwidth:focus {
    color: transparent;
    background-color: transparent;
    border-color: transparent;
}
/*css for iframe modal popup*/
.btn-downloaddelete{
position: absolute;
    right: 20px;
    bottom: 0px;
    /* top: 10px; */
    margin-bottom: 6px;
    }
.middle{    position: absolute;
    right: 15px !important;
    top: 0px !important;}
.file-left{float:left;margin-bottom:20px;}
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
/*  width: 75% !important; */
 border-radius: 0px !important;
 margin-bottom: 10px;
 border: none;
    box-shadow: none;
    border-bottom: 2px solid #ccc;
 }
 .workorder_modal_text {
    border-bottom: 2px solid #bdb7ab !important;
    border-top: none !important;
    border-left: none !important;
    border-right: none !important;
    border-radius: 0px !important;
}
.padding-div {
    padding-right: 25px !important;
    /* padding-left: 30px !important; */
    margin-top:10px;
}
.percentsymbol{
position:absolute;
top:18px;
right:18px;
}
	.addConclusionBtnDiv{
	    margin-top: 10px;
	}
	.widtheightytwo{
		width:82%;
	}
	.spanheading{
	    font-size: 14px;
	    font-weight: 1000;
	    font-family: Calibri;
	    margin-left: 15px;
	}
	.ui-state-highlight{
		background-color:#fff !important;
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
	
    /*  var vfx = fldLength;  */ 
     var vfx = $(".productrowcls:last").attr("id").split("tr-class")[1];  
     vfx=parseInt(vfx); 
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
	  var vendorDescription =  "<%= vendorDescription %>";
	  vendorDescription = formatColumns(vendorDescription);
	 
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

	 
	  var CreationDetailsColumn =  "<%= indentCreationDetailsId %>";
	  CreationDetailsColumn = formatColumns(CreationDetailsColumn);
	 
	 
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
    			Map<String, String> products = (Map<String, String>)request.getAttribute("tempPoProd");
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
    		//alert(dynamicSelectBoxId+"ravi");    		
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
    	else if(tableColumnName == vendorDescription) {
    		var dynamicSelectBoxId = "vendorDescription"+vfx;    		
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("input");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("placeholder", 'Vendor Description');
    	    /* div.setAttribute("onchange", "return validateProductAvailability(this);"); */
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
		    div.setAttribute("placeholder", 'Quantity');
		  
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
		    div.setAttribute("placeholder", 'Price');
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
		    div.setAttribute("placeholder", 'Basic Amount');
		    div.setAttribute("readonly", 'true');	
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		   	div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'Discount');
		    cell.appendChild(div);
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-percent percentsymbol");
		    cell.appendChild(btn);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "amtAfterDiscount"+vfx); 
		    div.setAttribute("id", "amtAfterDiscount"+vfx); 
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		 /*    div.setAttribute("id", "tax"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control' );
		    div.setAttribute("placeholder", 'Amount After Discount');
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
		    div.setAttribute("placeholder", 'Tax Amount');
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
		    div.setAttribute("placeholder", "Amount After Tax");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("placeholder", "Other Or TransportCharges");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("placeholder", "TaxOn Other Or TransportCharges");
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
		    div.setAttribute("placeholder", "Other Or TransportChargesAfterTax");
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "totalAmount"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Total Amount");
		    cell.appendChild(div);
		    
		    var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name", "indentCreationDetailsId"+vfx);
		    div.setAttribute("id", "indentCreationDetailsId"+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		    
		    
		    
		   
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "hsnCode"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'HSNCode');
		    cell.appendChild(div);
		    
		    		    
   		} 
   		 else if(tableColumnName == vendorDescription) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);		   
		    div.setAttribute("class", 'form-control');
		   
		    cell.appendChild(div);	
		    
		  	  
		    
		    
		 	
   		} 
    
   		/*  else if(tableColumnName == CreationDetailsColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		} 
     */
    	
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
		    
		    var div3 = document.createElement("input");
		    div3.setAttribute("type", "hidden");
		    div3.setAttribute("id", "newRow"+vfx);
		    div3.setAttribute("value", "N");
		   
		   
		    cell.appendChild(div3);
		    
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
    	    div.setAttribute("name", "Conveyance"+vfx);
    	    div.setAttribute("onchange", "changeconveyance("+vfx+")");
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);   	    
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
		    div.setAttribute("placeholder", 'Conveyance Amount');
		    div.setAttribute("onkeyup", "calculateGSTTaxAmount("+vfx+")");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event)");
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


function updateTempPo() {debugger;
	
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
/*  var valStatus = validateRowData(1);
	if(valStatus == false) {
    	return;
	} */
	calculateOtherCharges();
	var result=validateFileExtention();
	  if(result==false){
		  return false;
	  }
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	excuteDeleteFunction();
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("ProductWiseIndentsFormId").action = "updateTempPoPage.spring";
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
							<li class="breadcrumb-item active">View Cancel</li>
						</ol>
					</div>
					<div>
					<div>
		            <form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">	                
					  <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">	
					  <div class="col-md-12 border-indent">
						  <div class="col-md-4">
						        <div class="form-group">
									<label class="control-label col-md-6">Vendor Name : </label>
									<div class="col-md-6" >
										<form:input path="vendorName" id="VendorNameId" name ="vendorName" class="form-control" data-toggle="tooltip" title="${poDetails.vendorName}" value="${poDetails.vendorName}" readonly="true" />
									</div>
								</div>
						   </div>
						   <div class="col-md-4">
					          <div class="form-group">
								<label class="control-label col-md-6">GSTIN : </label>
								<div class="col-md-6">
									<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" value="${poDetails.strGSTINNumber}" autocomplete="off" readonly="true" />
								</div>
							  </div>
						  </div>
						  <div class="col-md-4">
							  <div class="form-group">
								<label class="control-label col-md-6">Vendor Address : </label>
								<div class="col-md-6" >
									<form:input path="vendorAddress" id="VendorAddress" class="form-control" data-toggle="tooltip" title="${poDetails.vendorAddress}" value="${poDetails.vendorAddress}" readonly="true" />
								</div>
							  </div>
						  </div>
						  <div class="col-md-4">
							 <div class="form-group">
								<label class="control-label col-md-6">Indent No :</label>
								<div class="col-md-6" >
								    <input type="text" id="siteIndentNumber" name="siteIndentNumber" class="form-control" readonly="true" value="${poDetails.siteWiseIndentNo}">
									<input type="hidden" id="indentNumber" name="strIndentNo" class="form-control" readonly="true" value="${poDetails.indentNo}">
									<input type="hidden" id="siteLevelPoPreparedBy" name="siteLevelPoPreparedBy" class="form-control" readonly="true" value="${poDetails.type_Of_purchase}">
									<input type="hidden" id="password" name="password" class="form-control" readonly="true" value="${poDetails.passwdForMail}">
								</div>
							</div>
						 </div>
						 <div class="col-md-4">
						  <div class="form-group">
							 	<label class="control-label col-md-6">Site Name :</label>
								<div class="col-md-6" >
									<input type="text" id=toSiteId1" name="toSite1" class="form-control" readonly="true" value="${poDetails.siteName}">
									<input type="hidden" id=siteName" name="SiteName" class="form-control" readonly="true" value="${SiteName}">
									<input type="hidden" id=toSiteId" name="toSite" class="form-control" readonly="true" value="${SiteId}">
								</div>
						   </div>
						</div>
						<div class="col-md-4">
						  <div class="form-group"> 
							<label class="control-label col-md-6">PO NO : </label>
							<div class="col-md-6" >
								<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off"  readonly="true" value="${poNumber}" data-toggle="tooltip" title="${poNumber}">
								<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
							</div>
						  </div>
					</div>
					<div class="col-md-4">
					
					  <div class="form-group">
					  
							<label class="control-label col-md-6">Delivery Date :</label>
							<div class="col-md-6  col-xs-12 input-group" >
							<input type="text" id="deliveryDateFor" name="deliveryDate" class="form-control item_name readonly-color datepicker" value="${poDetails.strDeliveryDate}" autocomplete="off" readonly/>
							 <%--  <input type="text" id="deliveryDate1" name="deliveryDate" class="form-control readonly-color hasDatepicker datepicker" value="${poDetails.strDeliveryDate}"  autocomplete="off" readonly> --%>
							  <label class="input-group-addon btn input-group-addon-border" for="deliveryDateFor"><span class="fa fa-calendar"></span></label>
							<input type="hidden" id="olddeliveryDate" name="olddeliveryDate" class="form-control deliveryDate" value="${poDetails.strDeliveryDate}" autocomplete="off">
							</div>
						</div>
					</div>
					<div class="col-md-4">
						  <div class="form-group">
								<label class="control-label col-md-6">Payment Req Days :</label>
								<div class="col-md-6" >
								<input type="text" id="days" name="days" class="form-control" value="${poDetails.payment_Req_days}" onfocusout="copyPasteCheckingNumber(this.id)" onkeypress="return isNumberCheck(this, event)" autocomplete="off">
								<input type="hidden" id="olddays" name="olddays" class="form-control " value="${poDetails.payment_Req_days}" >
								</div>
						 </div>
					</div>
					<div class="col-md-4">
						  <div class="form-group">	
								<div class="col-sm-2 col-xs-12" >
									<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${poDetails.vendorId}" />
									<input type="hidden" id="strVendorId" name="strVendorId" class="form-control" value="${poDetails.vendorId}" />
								</div>
								<div class="col-sm-2 col-xs-12" >
									<input type="hidden" id="siteId" name="siteId" class="form-control" value="${poDetails.site_Id}" />
								<input type="hidden" id="oldPONumber" name="oldPONumber" class="form-control" value="${poDetails.edit_Po_Number}" />
	<input type="hidden" id="POTotalId" name="POTotal" class="form-control" readonly="true" value="${poDetails.finalamtdiv}"/>
								</div>
							</div>
					</div>
					<div class="col-md-4">
					  <div class="form-group">
							  <div class="form-group col-md-6">
							<div class="col-md-6" >
								<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" />
							</div>
						</div>
						</div>
					</div>
                   <input type="hidden" name="product1" id="product1" class="form-control" autocomplete="off"  readonly="true" value="${localStorage.getItem("prodId")}" data-toggle="tooltip" title="${poNumber}">
		           </div>
	        </c:forEach>
	        <div class="clearfix"></div>
	          <div class="table-responsive">    	   
				 <table id="doInventoryTableId" class="table pro-table table-new"> <!-- style="" -->
				  <thead>
				    <tr class="table_header">  <!--  style="  background: #eae7e7;" -->
						<th>S NO</th>
	    				<th>Product</th>
	    				<th>Sub Product</th>
	    				<th>Child Product</th>    											
	    				<th>Units Of Measurement</th>
	    				<th>Vendor Description</th>    	
	    				<th>Quantity</th> 
	    				<th>Price</th>
	    				<th>Basic Amount</th>
	    				<th>Discount</th>
	    				<th>Amount After Discount</th>
	    				<th>Tax</th>
	    				<th>HSN Code </th>
	    				<th>Tax Amount</th>
	    				<th>Amount After Tax</th>
	    				<th>Other Or Transport Charges</th>
	    				<th>Tax On Other Or Transport Charges</th>
	    				<th>Other Or Transport Charges After Tax</th>
	    				<th>Total Amount </th>    				
						<th>Actions</th>				
				  </tr>
				</thead>
				<tbody>
				   <input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">		        	 	
	        	 	
	        	 	<input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="indentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
	        	 	        	 	
	        	 	
	        	 	 <tr id="tr-class${GetProductDetails.serialno}" class="productrowcls">
	        	 	<td>	
	        	  						
				       <div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="combobox${GetProductDetails.serialno}" readonly="true" name="Product${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1 value="${GetProductDetails.productName}">
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
					   <form:select path="" id="comboboxsubProd${GetProductDetails.serialno}"  readonly="true" name="SubProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
					    <option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
					   </form:select>
					</td>
					<td>
						<form:select path="" id="comboboxsubSubProd${GetProductDetails.serialno}" readonly="true" name="ChildProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
						 <option value="${GetProductDetails.child_ProductId}$<c:out value="${GetProductDetails.child_ProductName}"/>"><c:out value="${GetProductDetails.child_ProductName}"/></option>
					    </form:select>
					</td>
					<td>
						<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="UnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
							<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
						</form:select>
					</td>
					<td>
					 <input type="text"  id="vendorDescription${GetProductDetails.serialno}" readonly="true" name="vendorDescription${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.childProductCustDisc}"/> 
					</td>
					<td>
                        <input type="text"  id="stQantity${GetProductDetails.serialno}"    name="quantity${GetProductDetails.serialno}" onchange="calculatequantitybase(${GetProductDetails.serialno}, 'Old')" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					  <input type="hidden"  id="strQantity${GetProductDetails.serialno}"    name="strQuantity${GetProductDetails.serialno}" onchange="calculatequantitybase(${GetProductDetails.serialno}, 'Old')" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					<!-- when the old quantity no need so written value set to 0 -->
					<input type="hidden"  id="oldQuantity${GetProductDetails.serialno}"    name="oldQuantity${GetProductDetails.serialno}"   class="form-control" value="0"/>   
					<input type="hidden"  name="groupId${GetProductDetails.serialno}" id="groupId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.groupId}"/>
					</td>
					<td>
					  <input type="text" id="price${GetProductDetails.serialno}"  name ="PriceId${GetProductDetails.serialno}"  onkeypress="return validateNumbers(this, event)" onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}"/>
					</td>
					<td> 
					   <input type="text" id="BasicAmountId${GetProductDetails.serialno}"  name ="BasicAmountId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.basicAmt}"/>
					</td>
					<td> 
					  <input type="text" id="Discount${GetProductDetails.serialno}" name="Discount${GetProductDetails.serialno}" onkeypress="return validateNumbers(this, event)" onblur="calculateDiscountAmount(${GetProductDetails.serialno});" readonly="true" class="form-control disable-class1" value="${GetProductDetails.strDiscount}"/><i  class="fa fa-percent percentsymbol"></i>
                    </td>
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
					    <input type="hidden"   class='ttamount form-control' id='TotalAmountId${GetProductDetails.serialno}'  name='strTotalAmount${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.totalAmount}"/>
					</td>
					<td>
					 	<button type="button" name="addremoveItemBtn${GetProductDetails.serialno}" id="addremoveItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-remove"></i></button>
					<!-- <button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>  -->
						<button type="button" name="editItemBtn${GetProductDetails.serialno}" value="Edit Item" id="editItem${GetProductDetails.serialno}" class="btnaction" onclick="editInvoiceRow(${GetProductDetails.serialno})" ><i class="fa fa-pencil"></i></button> 
			 		<%--  <button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow(${GetProductDetails.serialno})" class="btnaction"><i class="fa fa-plus"></i></button>  --%>
					<!-- <input type="hidden" name="addNewrowItemBtn${GetProductDetails.serialno}" id="addNewrowItemBtn${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"/> -->	
				<c:if test="${listOfGetProductDetails.size() eq GetProductDetails.serialno}">
				<button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow(${GetProductDetails.serialno})" class="btnaction"><i class="fa fa-plus"></i></button>
				</c:if>
					</td>
					 <td style="display:none;"> 
					   <input type="hidden"   id='indentCreationDetailsId${GetProductDetails.serialno}'   name='indentCreationDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.indentCreationDetailsId}"/>
					</td> 
					<td style="display:none">
					    <input type="text"   id='poEntryDetailsId${GetProductDetails.serialno}'  name='poEntryDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.poEntryDetailsId}"/>
					</td>	
					<td style="display:none">
					<input  type = "hidden" id="actionValueId${GetProductDetails.serialno}"  name="actionValue${GetProductDetails.serialno}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					</td> 
				</tr>
			    </c:forEach> 
		     </tbody>
	      </table>
      </div>		
	<div>			
	<%	List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("listOfGetProductDetails");
	
	                String hiddenFieldHtml1="<input type=\"hidden\" id=\"numberOfRows\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
	out.println(hiddenFieldHtml1); %>  
	</div>

	 <div class="clearfix"></div><br/>
		<div class="table-responsive">
		  <table id="doInventoryChargesTableId" class="table pro-table table-new">
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
  			 <thead>
  			 <tbody>
			<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
				<tr id="trans-tr-class${GetTransportDetails.strSerialNumber}" class="chargesrowcls"> 
					<td>						
						<div id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
					</td>
					<td>						
							<form:select path="" id="Conveyance${GetTransportDetails.strSerialNumber}" readonly="true" name="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" onchange="changeconveyance(${GetTransportDetails.strSerialNumber})">
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
						<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" readonly="true" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="text"  onkeypress="return validateNumbers(this, event)" placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass" autocomplete="off" onkeyup="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})"/>
					</td>
					<td>
						<form:select path="" id="GSTTax${GetTransportDetails.strSerialNumber}" readonly="true" name="GSTTax${GetTransportDetails.strSerialNumber}" class="form-control GSTClass " value="" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})">
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
						<form:input path="" id="GSTAmount${GetTransportDetails.strSerialNumber}" readonly="true" name="GSTAmount${GetTransportDetails.strSerialNumber}" type="number" placeholder="GST Amount" value="${GetTransportDetails.GSTAmount1}" class="form-control noneClass" autocomplete="off"/>
					</td>
						<td>
						<form:input path="" type="number"  name="AmountAfterTax${GetTransportDetails.strSerialNumber}" readonly="true" id="AmountAfterTax${GetTransportDetails.strSerialNumber}" placeholder="Amount After Tax"  value="${GetTransportDetails.amountAfterTaxx1}" class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" readonly="true" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					<td>
						<button type="button" name="addremoveItemBtn${GetTransportDetails.strSerialNumber}" id="addremoveChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="removeTransRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
						 <button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId${GetTransportDetails.strSerialNumber}' onclick="appendChargesRow(${GetTransportDetails.strSerialNumber})" class="btnaction "><i class="fa fa-plus"></i></button> 
					<!-- <button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="deleteRow(this, ${GetTransportDetails.strSerialNumber})" ><i class="fa fa-trash"></i></button> -->	
						<button type="button" name="editchargesItemBtn${GetTransportDetails.strSerialNumber}" value="Edit Item" id="editchargesItem${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="editchargesInvoiceRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button>
					</td>
										
					<td style="display:none">
						<form:input path=""  type="text" id="poTransChrgsDtlsSeqNo${GetTransportDetails.strSerialNumber}" name="poTransChrgsDtlsSeqNo${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number" readonly="true" value="${GetTransportDetails.poTransChrgsDtlsSeqNo}" onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					 <td style="display:none">
					<input  type = "text" id="transactionActionValue${GetTransportDetails.strSerialNumber}"  name="transactionActionValue${GetTransportDetails.strSerialNumber}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					</td> 
				</tr>
				</c:forEach>
				</tbody>
			</table>
			<input type="hidden" id="noofTransRows" name="noofTransRows" value="${requestScope['listOfTransChrgsDtls'].size()}"/>
			</div><br/><br/><br/>
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
				
				
			<<div class="container">
				<div class="col-md-6">
					<div class="col-md-2">
						<label class="control-label col-sm-2" >Note: </label>
					</div>
					<div class="col-md-10">
						<form:textarea path="" href="#" data-toggle="tooltip" title="${IndentLevelCommentsList}"   id="NoteId" name="Note" class="form-control" autocomplete="off" placeholder="${IndentLevelCommentsList}"></form:textarea>
					</div>
				</div>
				<div class="col-md-5 col-md-offset-1">
					<div class="col-md-6 text-right"><h4><strong>Final Amount</strong></h4></div>
					<div class="col-md-1"><h4><strong>:</strong></h4></div>
					<div class="col-md-5 text-right no-padding-right no-padding-left">
						<h4><span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv"  ></span></h4>
						<input type="hidden" id="${GetProductDetails.serialno}" />
					</div>
				</div>
			</div>
					
				
			
			
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<!-- <input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId"> -->
						
						<!-- delete pdf values -->
						<input type="hidden" name="deletePdf0" value="" id="deletePdf0">
						<input type="hidden" name="deletePdf1" value="" id="deletePdf1">
						<input type="hidden" name="deletePdf2" value="" id="deletePdf2">
						<input type="hidden" name="deletePdf3" value="" id="deletePdf3">
	<!-- ***********************************************this is for pdf file download start******************************************************** -->					
			
			<!-- ***********************************************this is for pdf file download start******************************************************** -->
					
					<div class="col-md-12" style="margin-top: 10px;">
					<!-- 	<h3>You can see the PDF</h3> -->
					<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
					if(pdfcount==0){
						%>
						<!-- <h3>No PDF</h3> -->
						<%
					}else{
						%>
					<h3 style="color: #ffa500;">You can see the PDF(s) below :</h3>	
						<%
					}	
						for(int i=0;i<pdfcount;i++){
					  	String pdfName="pdf"+i;
					  	String PathdeletePdf="PathdeletePdf"+i;
					  	log(pdfName);
					
					%>
					   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
					    <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
					 <%
							if(request.getAttribute(pdfName)!=null){
					%>
								 <div class="col-md-3 pdfcount pdf-delete<%=i%>"  id="pdfDivHideShow<%=pdfName %>">
								  <div class="pdf-cls" style="margin-bottom:15px;cursor:pointer;"> 
								  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
								  <iframe class="iframe-pdf filesCount" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
								  <div class="middle" data-toggle="modal" data-target="#myModalpdf<%=i%>">
									<!-- <button type="button" class="btn btn-danger btn-fullwidth" ><i class="fa fa-close"></i></button> -->
								</div>
								<input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
								<p class="btn-downloaddelete"><a class="btn btn-success btn-xs" download onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i class="fa fa-download"></i> Download</a>
								<button class="btn btn-danger btn-xs" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')"><i class="fa fa-close"></i>Delete</button></p>
								<%--  <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')" data-dismiss="modal">Delete</button> --%>
								</div>
								</div>
					<%} %>
					<%} %>
					</div>
					 
					 		<!-- ***********************************************this is for pdf download end************************************************** -->		
							<div class="col-md-12 Mrgtop20">
											<%
															int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
															//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
					 				if(imagecount==0){
					 					%>
					 				<!-- 	<h3>No Images</h3> -->
					 					<%
					 				}else{
					 				%>
					 					<h3 style="color: #ffa500;">You can see the Images below :</h3>
					 				<%
					 				}		
					 				
																for (int i = 0; i < imagecount; i++) {
																	String imageB64="image"+i;	
																	String deleteB64="delete"+i;
																	log(imageB64);
																	out.print("<div>");
														%>
														       <c:set value="<%=imageB64 %>" var="index"></c:set>
														        <c:set value="<%=deleteB64 %>" var="delete"></c:set>
																							<%
														//	if (i == 0) {
															if(request.getAttribute(imageB64)!=null){
														%>
														 <div class='col-md-4 Mrgtop20' id='imageDivHideShow<%=imageB64 %>'>
														 <div class="container-1"  >
																		<img class="img-responsive img-table-getinvoice filesCount"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" style="cursor:pointer;"/>
																		 <div class="middle-1">
																		<div class="columns download">
															           <p>
															             <a class="btn btn-dwn btn-success btn-xs"  download onclick="toDataURL('${requestScope[index]}',this)" ><i class="fa fa-download"></i>&nbsp;Download</a>
															             <button onclick="deleteWOImageFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="btn btn-dwn btn-danger btn-xs"><i class="fa fa-remove"></i> &nbsp;Delete</button>
															          </p>
															       </div>
															       </div>
															       </div>
															       </div>
														<%
															}
														//	}
														%>
													<%
														out.print("</div>");
													%>
													<%}%>
											<%  int totalValue=imagecount+pdfcount; %>
												<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
												<input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfcount%>">
												<input type="hidden" name="imagesAlreadyexisted" id="imagesAlreadyexisted"	value="<%=imagecount%>" />
												<input type="hidden" name="pdfAlreadyExised" id="pdfAlreadyExised" value="<%=pdfcount%>">
												<input type="hidden" name="totalCount" id="totalCount" value="<%=totalValue%>"/>
												<div class="col-md-12">
													<div class="file-upload" id="ishidden">
														<%-- <c:if test="${(imagecount+pdfcount)<8}"> --%><h3>You can upload Images/PDFs here :</h3><%-- </c:if> --%>
															<P><strong>Maximum File Upload Size: 1MB</strong></P>
														  <div class="col-md-12 files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <!-- <i class="fa fa-plus"></i>  -->
															<div class="col-md-12">
															 <div class="market_file_style">
															  <div class="clearfix"></div>
															  <input type="file" id="file" name="file" style="display:none">
															 </div>
															</div>
													</div>
											   </div>
									</div>
							
						<!-- model popup for pdf start  -->
						<%
						for(int i=0;i<pdfcount;i++){
					  	String pdfName="pdf"+i;
					  	String PathdeletePdf="PathdeletePdf"+i;
					  	log(pdfName);
					
					%>
					   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
					   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
					 <%
							if(request.getAttribute(pdfName)!=null){
					%>
								<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
					  <div class="modal-dialog modal-lg-width">
					
					    <!-- Modal content-->
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal">&times;</button>
					        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i+1 %></strong></h4>
					      </div>
					      <div class="modal-body">
					         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
							 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
							 <embed src="${requestScope[pdfBase64]}" style="height:500px;width:100%;">
					      </div>
					      <input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
					      <div class="modal-footer">
					       <p class="text-center">
					       <%--  --%>
						     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
						     
						     <%-- <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')" data-dismiss="modal">Delete</button> --%>
						   
						   </p>
					      </div>
					    </div>
					
					  </div>
					</div>
					<%} %>
					<%} %>
					
					
					<!-- pdf model popup end  -->
						
						
					<!-- modal popup for image pop start-->
					<!-- Modal -->
						 <!-- Modal -->
						<%	 imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
								for (int i = 0; i < imagecount; i++) { 
							String index="image"+i;				
							log(index);
							%>
						  <div class="modal fade custmodal" id="uploadinvoice-img<%=i %>" role="dialog">
					    <div class="modal-dialog modal-lg custom-modal-lg">
					    
					      <!-- Modal content-->
					      <div class="modal-content">
					        <div class="modal-header cust-modal-header">
					          <button type="button" class="close" data-dismiss="modal">&times;</button>
					          
					        </div>
					        <div class="modal-body cust-modal-body">
					    <c:set value="<%=index %>" var="i"></c:set>
					    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="${requestScope[i]}" />
					          
					        </div>
					        <div class="modal-footer cust-modal-footer">
					          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
					        </div>
					      </div>
					      
					    </div>
					  </div>
					  <%} %>
					  <div id="allImgPdfUrlToDeleteFile" ></div>
						<div class="col-md-12 text-center center-block" style="margin:60px 0px 30px 0px;"> 
						   <input class="btn btn-warning" type="button" value="Calculate" data-toggle="myModal"  id="calculateBtnId" onclick="calculateOtherCharges()">
						   <input type="button"  class="btn btn-warning  form-submit modelcreatePO" onclick="createModelPOPUP()" value="Submit" id="saveBtnId" ">
						</div> 
			
		   <input type="hidden" name="kilobyte" id="kilobyte" value="${KILO_BYTE}">
           <!-- **************************************************************		 --> 		
	       <!-- ***********************************************this is for pdf download end************************************************** -->		
					
		
			<br/>
	<!--term and condition modal pop up start -->
 	<div class="modal fade" id="myModal" role="dialog">
  		<div class="modal-dialog modal-lg">
  		      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        		<div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
        		  <button type="button" class="close" data-dismiss="modal">&times;</button>
         		 <h4 class="modal-title text-center">Terms and Conditions</h4>
        		</div>
        		<form action="" method="post">
        		<div class="modal-body" style="height:400px;overflow-y: scroll;">
       			 <div class="container">
					<div class="row">
       				 <div class="control-group" id="fields">
        		   <div class="controls" id="profs"> 
     				<div class="col-md-12 appen-div-workorder">
     
     				<c:forEach items="${listTermsAndCondition}" var="item">
     				<input type="hidden" name="termsAndCondold" class="form-control" value="${item.strTermsConditionName}"/>
     				<div class="col-md-12 remove-filed" style="margin-top: 10px;">
     				 <div class="col-md-10">
      					<input type="text" name="termsAndCond" class="form-control" value="${item.strTermsConditionName}"/>
     				 </div>
				      <div class="col-md-2 margin-close">
				      <button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
				      </div>
     
      			   </div>
      			  </c:forEach>
      
      			  </div>
        		  <div class="col-md-12">
			       <div class="col-md-12 input_fields_wrap"  style="margin-top: 10px;">
			         <div class="col-md-10"><input type="text" class="form-control" id="workorder_modal_text1" name="termsAndCond" value=""/><input type="hidden" name="termsAndCondold" class="form-control" value=""/></div>
			         <div class="col-md-2 margin-close"><button type="button" class="btn btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
			        
			      </div>
			     </div>
          		 <br>
          <div class="col-md-12">
          	<div class="col-md-12" style="margin-bottom: 10px;margin-top: 5px;">
	        	<span class="spanheading">(Optional)If you want to add CC In emails.</span>
	       </div>
	       <div class="col-md-12">
	         <div class="col-md-10">
	       		<input type="text" id="" name="ccEmailId2" value="${ccEmails}" class="form-control"/>
	       		<input type="hidden" id="" name="oldccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/>
	       	</div>
	       </div>
            <div class="col-md-12" style="margin-bottom: 10px;margin-top: 5px;">
                <span class="spanheading">Subject</span>      			
        	</div>
        	<div class="col-md-12">
        	 <div class="col-md-10">
        		<input type="text" id="" name="subject"  class="form-control" value="<c:out value='${subject}'/> " Placeholder="Please enter the subject"/>
        		<input type="hidden" id="" name="strSubject"  class="form-control"  value="<c:out value='${strSubject}'/> "/>
        	 </div>
        	</div>
            <div class="col-md-12" style="font-family: Calibri">
 				<div class="" id="conclusionHeader"style="font-family: Calibri;text-align: left;font-size: 14px;font-weight:1000;">Conclusions:</div>
				  <div id="addconclusiontxt">
				  		<c:forEach items="${conclusions}" var="conclusion"> 
				  		<input type="hidden" class="form-control" value="${conclusion}" name="conclusionDescold">
				  			<div class="">
					  			<div class="hideInprintCls">
					  				<div class="col-md-10 addConclusionBtnDiv">
					  					<input type="text" class="form-control" value="${conclusion}" name="conclusionDesc">
					  				</div>
						  			<div class="col-md-2 addConclusionBtnDiv">
						  				<button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger remove-button"><i class="fa fa-remove"></i></button>
						  			</div>
					 	 		</div>
					   		</div>
					   </c:forEach>
					   
					   <!-- <div class="conclusionMainDivCls">
					  	<div class="hideInprintCls">
					  		<div class="col-md-10"  style="margin-top: 10px;">
					  			<input type="text" class="form-control" value="Conclusion two" name="conclusionDesc">
					  		</div>
						  	<div class="col-md-2 addConclusionBtnDiv">
						  		<button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger remove-button"><i class="fa fa-remove"></i></button>
						  	</div>
					 	 </div>
					   </div> -->					  
				  </div> 
				  <div class="conclusionMainDivCls" style="" id="conclusionMainDiv1">
				    <div class="hideInprintCls">
					  <div class="col-md-10 addConclusionBtnDiv">
			 			<input type="text" class="form-control conclusiontxt" id="conclusionId1" name="conclusionDesc">
			 		  </div>
			    	  <div class="col-md-2 addConclusionBtnDiv">
						 <button type="button"  id="conclusionAddBtn"  onclick="appendConclusionTxtBx()" class="btn btn-success plus-button addbtncls "><i class="fa fa-plus"></i></button>
					  </div>
					</div> 
				  </div>    
 			 </div>
 			 <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
 			 	<button type="button" class="btn btn-warning  form-submit" data-dismiss="modal" onclick="updateTempPo()">Submit</button>
 			 </div>
 			 </div>
             </div>
            </div>
	       </div>
         </div>
        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
	    
		 </div>
          
        </div>
      </div>
      </form>      
     </div>
    </div>
	</div>	
	<!--term and condition modal pop up end -->
																								
        </div>
      </div>
      </form>
      
    </div>
  </div>
	</div>		

</form:form>
<!-- ****************************************************pdf download start*************************************************************************** -->
<%-- <div id="myModalpdf" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF1</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf0']}" style="height:500px;width:100%";>
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	      <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(0)" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModalpdf2" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF2</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf1']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(1)" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModalpdf3" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF3</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf2']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(2)" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModalpdf4" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF4</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf3']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(3)" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>		 --%>
<!-- ******************************************************pdf download end**************************************************************** -->
	</div>
		</div>
		

	</div>
	</div>
	</div>
	
	
					
					<!-- /page content -->        
				</div>
			</div>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/ViewCancelTempPo.js" type="text/javascript"></script>
         <script src="js/sidebar-resp.js" type="text/javascript"></script>
		<script>
		//For combobox & readonly
		var tablelength=$("#doInventoryTableId > tbody > tr").length;
		for(var i=1;i<=tablelength;i++){
			debugger;
			$("#combobox"+i).combobox();    
		    $( "#comboboxsubProd"+i).combobox();
			$("#comboboxsubSubProd"+i).combobox(); 
			$('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide');			
			$('#Product'+i).attr('readonly','true');
			$('#SubProduct'+i).attr('readonly','true');
			$('#ChildProduct'+i).attr('readonly','true');
			$('#Product'+i).addClass('form-control');
			$('#Product'+i).attr('data-toggle', "tooltip");
			$('#Product'+i).attr('title', $('#Product'+i).val());
			$('#SubProduct'+i).addClass('form-control'); 
			$('#SubProduct'+i).attr('data-toggle', "tooltip");
			$('#SubProduct'+i).attr('title', $('#SubProduct'+i).val());
			$('#ChildProduct'+i).addClass('form-control');
			$('#ChildProduct'+i).attr('data-toggle', "tooltip");
			$('#ChildProduct'+i).attr('title', $('#ChildProduct'+i).val());
			$('#ChildProduct'+i).addClass('ChildProduct');
			$('#vendorDescription'+i).attr('data-toggle', "tooltip");
			$('#vendorDescription'+i).attr('title', $('#vendorDescription'+i).val());
		}
		$('[data-toggle="tooltip"]').tooltip(); 
		</script>
		
		<script>
		$(document).ready(function() {				
			for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
				$('.btn-visibilty'+i).prop('readonly', true).css('cursor','not-allowed');
				$('.bt-taxamout-coursor'+i).prop('readonly', true);
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
			
	/* ********** Model popup **********		 */
			
	function createModelPOPUP(){
		debugger;
		var validateProduct=validateProductRow();
		if(validateProduct==false){
			return false;
		}
		var chargesRow=validateChargesRow();
		if(chargesRow==false){
			return false;
		}	
		calculateOtherCharges();
		$('#myModal').modal('show');

	}
			
			 	
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
   /*  var next = 1; */
    var next = parseInt($("#countOftermsandCondsfeilds").val());
    $(".add-more").click(function(e){debugger;
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
                var fieldID = "#termsAndCond" + fieldNum;
                $(this).remove();
                $(fieldID).remove();
            });
    });
    

    
});

function removeRow(rowId){debugger;
var canIRemove = window.confirm("Do you want to delete?");
if(canIRemove == false) {
    return false;
}
/*	document.getElementById("ractionValueId"+rowId).value = "R";*/
var n=0;
$(".productrowcls").each(function(){debugger;
	var id=$(this).attr("id").split("tr-class")[1];
	if($("#actionValueId"+id).val()!="R"){
		n++;
	}
});

if(n==1){
	alert("This row can't be deleted.");
	return false;
}
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
$('#TotalAmountId'+rowId).removeAttr('id');
$('#actionValueId'+rowId).val("R");




}
function removeme(rowId){alert('');
$("#termsAndCond"+rowId).remove();
$('.remove-me'+rowId).hide();
};

function removeTransRow(rowId){debugger;
var canIRemove = window.confirm("Do you want to Delete?");
if(canIRemove == false) {
    return false;
}
var n=0;
$(".chargesrowcls").each(function(){debugger;
	var id=$(this).attr("id").split("trans-tr-class")[1];
	if($("#transactionActionValue"+id).val()!="R"){
		n++;
	}
});

if(n==1){
	alert("This row can't be deleted.");
	return false;
}
$("#trans-tr-class"+rowId).addClass('strikeout');
$("#addremoveChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editchargesItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#addNewChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoChargesDivId'+rowId).removeAttr('id');	
$('#transactionActionValue'+rowId).val("R");

}

/* ************************************************************terms and conditions******************************************************************************* */
$(document).ready(function() {
    var max_fields      = 50; //maximum input boxes allowed
    var wrapper         = $(".appen-div-workorder"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID
    var wrapper1 		=$(".appen-div-workorder1");
    var x = 1;  ///initlal text box count
        $('.add_field_button').on("click",function(e){ //on add input button click
        e.preventDefault();
    var tc=$("#workorder_modal_text1").val();
    if(tc.length==0){
    	alert("please enter condition");
    	return false;
    }
    
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            $(wrapper).append('<div class="col-md-12 remove-filed" style="margin-top: 10px;"><div class="col-md-10"><input type="text" name="termsAndCond" value="'+tc+'" id="termsAndCond'+x+'" class="form-control" /></div><div class="col-md-2 margin-close"><button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
        }
        $("#workorder_modal_text1").val("");
        $("#workorder_modal_text1").focus();
        $("#termsAndCond"+x).val(tc);
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
    	alert("Do you want to delete?");
       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
       
    })
     $(wrapper1).on("click",".remove_field", function(e){ //user click on remove text
    	 alert("Do you want to delete?");
       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
    })
});
	
$(function() {
	$("#deliveryDate1").datepicker({
		dateFormat: 'dd-M-y',
		minDate: new Date(),
		changeMonth: true,
		changeYear: true
	});
}); 

		</script>
		<script>
		$('.modal').click(function(){
			$('.nav-md').css({"padding-right":"-17px"});
		});
		
		//adding conclusion 
		var id=1;
		function appendConclusionTxtBx(){
			var firstTextVal=$("#conclusionId1").val();
			if(firstTextVal.trim()==''){
				alert("Please enter conclusion");
				$("#conclusionId1").focus();
				return false;
			}
			var lastTextboxId=$(".conclusionMainDivCls").attr("id").split("conclusionMainDiv")[1];
			id++;
			var addConclusionTxtBx=	'<div class="conclusionMainDivCls" id="conclusionMainDiv'+id+'"><div class="hideInprintCls"><div class="col-md-10"  style="margin-top: 10px;"><input type="text" class="form-control conclusiontxt" id="conclusionId'+id+'" name="conclusionDesc"></div><div class="col-md-2 addConclusionBtnDiv"><button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger remove-button"><i class="fa fa-remove"></i></button></div></div></div>';
			$("#addconclusiontxt").append(addConclusionTxtBx);
			
			$("#conclusionId"+id).val($("#conclusionId1").val());
			$("#conclusionId1").val('');	
			$("#conclusionId1").focus();		
		}
		//deleteting conclusion row written by thirupathi
		function deleteConclusionTxt(id){debugger;
			var canIDelete=window.confirm("Do you want to delete?");
			if(canIDelete==false){
				return false;
			}			
			$(id).parents(".hideInprintCls").remove();			
		}
		/* *********************************************only integers allowed here start********************************************************* */

		function isNumberCheck(el, evt) {	
		    var charCode = (evt.which) ? evt.which : event.keyCode;				  
		    if (charCode < 48 || charCode > 57) {
		        return false;
		    }else{
				return true;
			}
		    
		}
		 function validateFileExtention(){
			  debugger;
			var ext="";
			var kilobyte=1024;
			var len=$('input[type=file]').val().length;
			var count=0;
		/* 	if(len==0){
				alert("please at least one file.");
				return false;
			} */
			  $('input[type=file]').each(function () {
			        var fileName = $(this).val().toLowerCase(),
			         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
			        	
					 if(fileName.length!=0){
				        if (!(regex.test(fileName))) {
				            $(this).val('');
				            alert('Please select correct file format');
				            count++;
				            return false;
				        }
			        }
			    });
			if(count==0)
			  return true;
			else
				return false;
			 }//validateFileExtention method
		  
			 var deleteUploadedimageFiles=new Array();
			 var deleteUploadedPDFFiles=new Array();
			 
			 
			  function deletepdf(divId,imagePath){
				  debugger;
				  var strName=$('#strName').val();
				  var invoice_Number=$('#invoice_Number').val();
				  var indent_Entry_Number=$('#indent_Entry_Number').val();
				  var site_Id=$('#site_Id').val();
				  var image_Path=$('#image_Path').val();
				 // image_Path=str.replace("Part0",divId);
					 var canISubmit = window.confirm("Do you want to delete?");
					 if(canISubmit == false) {
					        return false;
					 }
					 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
					 $("#pdfAlreadyPresent").val(pdfAlreadyPresent-1);
					 var url="deleteInvoicePermanentImage.spring?"+imagePath;
					 
					 
					 
					 deleteUploadedPDFFiles.push({"pdfNo":divId,"url":url});
					 debugger;
					 $("#pdfDivHideShow"+divId).remove();
					 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
					 var classlength=parseInt($(".selectCount").length);
					 //var k=8-(imagesAlreadyPresent+pdfAlreadyPresent);
					 if((pdfAlreadyPresent+imagesAlreadyPresent)==8){
						 $("#files_place").show();
						// $(".files_place1").append('<div class="clearfix"></div><input type="file" id="file_select1" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange(1)"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float:left;display:none;" onclick="filedelete(1)"><i class="fa fa-close"></i></button><button type="button" id="Add" class="btn btn-success btn-xs Mrgtop10" style="display: none;" onclick="addFile(1)"><i class="fa fa-plus"></i> Add File</button></div></div>'); 
					 }
					 
					 
				//	 deleteFile(divId,imagePath);
				}
		  
				function deleteFile(divId,url){
					debugger;
					
					 $.ajax({
						  url : url,
						  type : "get"
						  });
				}
			  
			  function deleteWOImageFile(divId,imagePath){
					 debugger;
					 var strName=$('#strName').val();
					 var invoice_Number=$('#invoice_Number').val();
					 var indent_Entry_Number=$('#indent_Entry_Number').val();
					 var site_Id=$('#site_Id').val(); 
				 var canISubmit = window.confirm("Do you want to delete image?");
				     
				 if(canISubmit == false) {debugger;
				        return false;
				 }
				 $("#imageDivHideShow"+divId).remove();
			
				 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
				 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
				 //var url="deleteInvoicePermanentImage.spring?imagePath="+imagePath+"&strVendorName=${getInvoiceDetails.vendorName}&siteId=${getInvoiceDetails.siteId}&invoiceNumber=${getInvoiceDetails.invoiceNumber}&indentEntryId=${getInvoiceDetails.indentEntryId}";
				 var url="deleteInvoicePermanentImage.spring?"+imagePath;	
		
				 deleteUploadedimageFiles.push({"imageNo":divId,"url":url});
				 
				 
				 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
				 if((pdfAlreadyPresent+imagesAlreadyPresent)==8){
					 $("#files_place").show();
					// $(".files_place1").append('<div class="clearfix"></div><input type="file" id="file_select1" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange(1)"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float:left;display:none;" onclick="filedelete(1)"><i class="fa fa-close"></i></button><button type="button" id="Add" class="btn btn-success btn-xs Mrgtop10" style="display: none;" onclick="addFile(1)"><i class="fa fa-plus"></i> Add File</button></div></div>'); 
				 }
				// deleteFile(divId,imagePath);
				 return false;
			}
			  
		  		// which is calling from js before submit button executed
				function excuteDeleteFunction(){
					debugger;
					var count=1;
					 deleteUploadedimageFiles.sort(sortUrlData);
						 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedimageFiles.length; imgPdfIndex++) {
								if(deleteUploadedimageFiles[imgPdfIndex].length!=0){
									//deleteFile(imgPdfIndex,deleteUploadedimageFiles[imgPdfIndex].url);
									// here we need to take all the images for back end 
										$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='imgPathtoDelete' id='imgPdfPath"+count+"'>");
										var imgePath=deleteUploadedimageFiles[imgPdfIndex].url.split("deleteInvoicePermanentImage.spring?")[1];
										$("#imgPdfPath"+count).val(imgePath);
										count++;
								}
							} 
						 debugger;
						 deleteUploadedPDFFiles.sort(sortPdfData);
						 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedPDFFiles.length; imgPdfIndex++) {
								if(deleteUploadedPDFFiles[imgPdfIndex].length!=0){
								//deleteFile(imgPdfIndex,deleteUploadedPDFFiles[imgPdfIndex].url);
								// here we need to take the path and append the input type to the div tag for all pdfs taken at a time
									$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='PdfPathtodelete' id='imgPdfPath"+count+"'>");
									var pdfPath=deleteUploadedPDFFiles[imgPdfIndex].url.split("deleteInvoicePermanentImage.spring?")[1];
									$("#imgPdfPath"+count).val(pdfPath);
								
									count++;
								}
							}
						 debugger;
						 //document.getElementById("updaetTempBtnId").disabled = true;   
						 document.getElementById("ProductWiseIndentsFormId").method = "POST";
					     document.getElementById("ProductWiseIndentsFormId").submit();
					}
				// sort the files according to order
				function sortPdfData(a,b){
					debugger;
						
						var	pdf=a.pdfNo.split("pdf")[1];
						var	pdf1=b.pdfNo.split("pdf")[1];
						
						
						 if(pdf < pdf1) {
							    return 1;
						 }else if (pdf1 < pdf) {
							    return -1;
						 }else{ 
							return 0;	  
						  }
					}

					function sortUrlData(a, b) {
						debugger;
						
						var	imageNo=a.imageNo.split("image")[1];
						var	imageNo1=b.imageNo.split("image")[1];
						
						
						 if(imageNo < imageNo1) {
							    return 1;
						 }else if (imageNo1 < imageNo) {
							    return -1;
						 }else{ 
							return 0;	  
						  }
					} 
				  function updateTempWO(){
					  debugger;
					   var result=validateFileExtention();
					  if(result==false){
						  return false
					  }  
					    result = confirm("Do you want to update?");
						if (!result) {
						  return false;
						}
						 debugger;
						excuteDeleteFunction();
					}
		
			/* --------------------------------file added and deleted start-----------------------------------	 */  
			/*script for file upload*/
		/* 	user click on the add button then it will call */
	function addFile(id){debugger;
	  var classlength=parseInt($(".selectCount").length);
	  var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
	  var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
	  var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
	 var k=imagesAlreadyPresent+pdfAlreadyPresent+classlength;
	 
	  if($("#file_select"+classLastId).val()==""){
		 alert("please select file");
		 $("#file_select"+classLastId).focus();
		 return false;
	  }
	  if(k > 7){
		alert("Unable to upload more than eight files");
		return false;
	  }
	  var btnid = $(".selectCount:last").attr("id").split("file_select")[1];
	  var dynamicId = parseInt(k) + 1;
	  classLastId = parseInt(classLastId) + 1;
	  $(".files_place").append('<div class="clearfix"></div><input type="file" id="file_select'+classLastId+'" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange('+classLastId+')"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn'+classLastId+'" style="float:left;display:none;" onclick="filedelete('+classLastId+')"><i class="fa fa-close"></i></button></div></div>');
	}
	//this is for file change to display close button
	function filechange(id){
		debugger;
		
		var size_file = ($("#file_select"+id))[0].files[0]
		file_size = size_file.size;
		if((size_file.type)=='application/pdf'){
			if((file_size/1024)<=1024 && (size_file.type)=='application/pdf'){$("#close_btn"+id).show();
			$("#Add").show();}
			else{
				alert("Uploaded file size("+file_size+"), So Please upload Below 1 mb.");
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
		debugger;
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
			
	debugger;
	    var pdfImgLength=$("#totalCount").val();
	    if(pdfImgLength==8){
	    	$("#files_place").hide();
	    }else{
	    	$("#files_place").show();
	    }

	    
	  //this code for download server images
		function toDataURL(url, current) {
		 // alert("hai");
			debugger;
		    var httpRequest = new XMLHttpRequest();
		    httpRequest.onload = function() {
		       var fileReader = new FileReader();
		          fileReader.onloadend = function() {
		             console.log("File : "+fileReader.result);
						 $(current).removeAttr("onclick");
						 $(current).attr("href", fileReader.result);
						 $(current)[0].click();
						 $(current).removeAttr("href");
						 $(current).attr("onclick", "toDataURL('"+url+"', this)");
		          }
		          fileReader.readAsDataURL(httpRequest.response);
		    };
		    httpRequest.open('GET', url);
		    httpRequest.responseType = 'blob';
		    httpRequest.send();
		 }
		
		</script>
		<script>
		$('.modal').click(function(){
			$('.nav-md').css({"padding-right":"-17px"});
		});
		
		
		//first file change 
		$(".selectCount").bind("change", function() {debugger;
			var id=$(this).attr("id").split("file_select")[1];
			if($(this).val()==''){		
				$("#market_file_style"+id).remove(); 
			}
			if((this.files[0].size/1024)<=1024){
				$('#close_btn'+id).show();
				$("#fileName"+id).text(this.files[0].name);
			}else{
				alert("Please upload below 1 mb PDF file.");		
				$("#market_file_style"+id).remove(); 
			}
			//scoll to bottom 
			window.scrollTo(0,document.querySelector("body").scrollHeight);
			console.log(document.querySelector("body").scrollHeight);
		}); 
		//from second file onwards 
		function fileChange(id, current){ debugger;
		 	if($(current).val()==''){
		 		$("#market_file_style"+id).remove(); 		
			}
			if((current.files[0].size/1024)<=1024){
				$('#close_btn'+id).show();
				$("#fileName"+id).text(start_and_end(current.files[0].name));
			}else{
				alert("Please upload below 1 mb PDF file.");
				$("#market_file_style"+id).remove(); 		
			}	
			//scoll to bottom 
			window.scrollTo(0,document.querySelector("body").scrollHeight);
			console.log(document.querySelector("body").scrollHeight);
		}
		//adding file 
		function addFile(){
			var classlength=$(".selectCount").length;
			if(classlength!=0){
				var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
				if($("#file_select"+classLastId).val()==""){
				 $("#market_file_style"+classLastId).remove(); 			
				}
			}else{
				var classLastId=0;
			}		
			var dynamicId = parseInt(classLastId) + 1;
			 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style" id="market_file_style'+dynamicId+'" style="margin-top: 20px;"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" onchange="fileChange('+dynamicId+', this)" class="selectCount" style="display:none;"/><div class=""><span id="fileName'+dynamicId+'"  style="float:left;padding-right: 10px;width:200px;"></span><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;padding: 2px 5px;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
		}
		//delete file
		function filedelete(id){
			var canIDelete=window.confirm("Do you want to delete file?");
			if(canIDelete==false){
				return false;
			}
			$("#market_file_style"+id).remove();
		}
		//uploading file
		function uploadFile(){debugger;
			var count=$(".filesCount").length;
			var classlength=$(".selectCount").length;
			 if((classlength+count)>7){
				alert("Unable to upload more than eight files.");
				return false;
			}
			 //creating input file div
			addFile();
		    var id=$(".selectCount:last").attr("id").split("file_select")[1];
			$("#file_select"+id).trigger("click");		
		}
	  //ellipse for uploaded file
		function start_and_end(str) {
			if (str.length > 25) {
			   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
			}
		  return str;
		}
	  	
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
					  $("#ConveyanceAmount"+btn).val("");
					  $("#ConveyanceAmount"+btn).removeAttr("readonly");
					  $("#GSTTax"+btn).val("");
					  $("#GSTTax"+btn).removeAttr("readonly");
					  $("#GSTAmount"+btn).val("");
					  $("#AmountAfterTax"+btn).val("");
				  }
	 	}
	 	
	 	    $(".datepicker").datepicker({
	 	    	dateFormat: 'dd-M-y',
	 			minDate: new Date(),
	 			changeMonth: true,
	 			changeYear: true
	 	    });
	 	
	  
		</script>
</body>
</html>
