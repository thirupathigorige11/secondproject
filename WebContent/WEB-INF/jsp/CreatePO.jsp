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
	String vendorDescription=resource.getString("label.vendorDescription");

	String remarks = resource.getString("label.remarks");
	String isNewOrOld = resource.getString("label.isNewOrOld");
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
		
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
		<link href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.css" rel="stylesheet" type="text/css">
        <link href="js/inventory.css" rel="stylesheet" type="text/css">
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
		<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
		
		
	<style>

	.finalAmountDiv{color: #000;font-size: 18px;font-weight: bold;}
	.form-group label{text-align: left}
	#isNewOrOld{display: none;}
	.breadcrumb {background: #f5f5f5 !important;}
	.floatLeft input, .floatLeft button{float:left;margin-bottom:15px;}	
	.form-group label{text-align: left}
    .table-new tbody tr td {border-top: 0px !important;}

	 <!-- style for modapopup add remove text -->
	 #mainDivId {display : none;}
	 .padding-div{padding-right: 30px !important;padding-left: 30px !important;}
	 .remove-button{font-size: 12px;padding: 8px 12px;}
	 .plus-button{font-size: 12px;padding: 8px 12px;}
	 .red{ color:red;font-size:14px;font-weight:bold;}
	/*  .margin-close{margin-top:5px;} */
	 .black{ color:black;font-size:14px;font-weight:bold;}
	 .control-text{border:1px solid #ddd !important;}
     .emailCC #ccemailcheckbox, .emailCC span{float:left;}
     .subject-container #sub-conmatiner, .subject-container span{float:left;}
     .input-group-addon {border: 1px solid #ccc !important;border-left-width: 0 !important;}
<!-- Modal popup for add remove -->

.addbtncls{
    padding: 5px;
    width: 40px;
    margin-left:10px;
}
.addConclusionBtnDiv{
    width: 14%;
    float: left;
    text-align: center;
    margin-top: 10px;
}
.spanheading{
    font-size: 14px;
    font-weight: 1000;
    font-family: Calibri;
}
.widtheightytwo{
	width:82%;
}
.percentageCls{
	position: absolute;
    right: 14px;
    top: 17px;
}
.ui-state-highlight{
	background-color:#fff;
	color:#000;
}
.files_place{margin-top:20px;}
.ui-autocomplete-input{
	height:30px !important;
}
@media only screen and (min-width:320px){
.calc-thead-inwards{    
    color: #000;
    background: #ccc;
    width: calc( 100% - 17px ) !important;}
}
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new  tbody tr td:first-child{width:76px;text-align: center;}
#doInventoryTableId thead tr th:nth-child(2), #doInventoryTableId tbody tr td:nth-child(2),#doInventoryTableId thead tr th:nth-child(3), #doInventoryTableId tbody tr td:nth-child(3),#doInventoryTableId thead tr th:nth-child(4), #doInventoryTableId tbody tr td:nth-child(4) {width:220px;}
#doInventoryTableId thead tr th:nth-child(7), #doInventoryTableId tbody tr td:nth-child(7),#doInventoryTableId thead tr th:nth-child(10), #doInventoryTableId tbody tr td:nth-child(10)  {width:120px;}
.mrg-btm-10{
	margin-bottom:10px;
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
     var productColumn =  "<%= product %>";
     productColumn = formatColumns(productColumn);  	 
  	 var subProductColumn =  "<%= subProduct %>";
  	 subProductColumn = formatColumns(subProductColumn);
 	 var childProductColumn =  "<%= childProduct %>";
 	 childProductColumn = formatColumns(childProductColumn);
	 var vendorDescriptionColumn =  "<%= vendorDescription %>";
	 vendorDescriptionColumn = formatColumns(vendorDescriptionColumn);
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
	 var remarksColumn =  "<%= remarks %>";
	 remarksColumn = formatColumns(remarksColumn);	 
	 var isNewOrOldColumn =  "<%= isNewOrOld %>";
	 isNewOrOldColumn = formatColumns(isNewOrOldColumn);
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);
	 
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

    	else if(tableColumnName == vendorDescriptionColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "childProductVendorDesc"+vfx);
		    div.setAttribute("id", "childProductVendorDesc"+vfx);
		    div.setAttribute("onblur", "return vendorTooltip("+vfx+");");		   
		    div.setAttribute("data-toggle", "tooltip");	   
		    div.setAttribute("data-placement", "bottom"); 
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
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
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("autocomplete", 'off');
		    cell.appendChild(div);		
		    
		    var div3 = document.createElement("input");
    	    div3.setAttribute("type", "hidden");
    	    div3.setAttribute("name", "avalBOQQty"+vfx);
    	    div3.setAttribute("id", "avalBOQQty"+vfx);
    	    cell.appendChild(div3);  
    	    
		    var div4 = document.createElement("input");
    	    div4.setAttribute("type", "hidden");
    	    div4.setAttribute("name", "BOQQty"+vfx);
    	    div4.setAttribute("id", "BOQQty"+vfx);
    	    cell.appendChild(div4);  
    	    
    	    var div1 = document.createElement("input");
    	    div1.setAttribute("type", "hidden");
    	    div1.setAttribute("name", "groupId"+vfx);
    	    div1.setAttribute("id", "groupId"+vfx);
    	    cell.appendChild(div1);  
    	    
    	    var div2 = document.createElement("input");
    	    div2.setAttribute("type", "hidden");
    	    div2.setAttribute("name", "oldQuantity"+vfx);
    	    div2.setAttribute("id", "oldQuantity"+vfx);
    	    div2.setAttribute("value", "0");
    	    cell.appendChild(div2);
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Price"+"Id"+vfx);
		    div.setAttribute("id", "price"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("autocomplete", 'off');
		    cell.appendChild(div);	
		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "BasicAmount"+"Id"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'readonly');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("autocomplete", 'off');
		    cell.appendChild(div);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-percent percentageCls");
		    cell.appendChild(btn);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "amtAfterDiscount"+vfx);
		    div.setAttribute("id", "amtAfterDiscount"+vfx);
		    div.setAttribute("readonly", 'readonly');
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
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", 'amountAfterTax'+vfx);
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
		    
		    
		    var div = document.createElement("input");
		     div.setAttribute("style", "display: none;"); 
		    div.setAttribute("name", "isNewOrOld"+vfx);
		    div.setAttribute("id", "isNewOrOld");
		    div.setAttribute("value", "new");
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
    	    div.setAttribute("onchange", "changeconveyance("+vfx+")"); 
    	    div.setAttribute("id", dynamicSelectBoxId);   	    
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
    	    
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event)");
		    div.setAttribute("placeholder", "Please enter Amount");
		    div.setAttribute("class", 'form-control');
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
    		div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("type", "number", placeholder="Enter Amount");
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
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Amount After Tax");
		    div.setAttribute("readonly", "true");
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
	//validating product table written by thirupathi
    var valProductTable=validateRowData();
	if(valProductTable==false){
		return false;
	}
	//validating charges table written by thirupathi
	var valChargesTable=validateChargesTableRows();
	if(valChargesTable==false){
		return false;
	}	
	calculateOtherCharges();
	$('.modal').modal('show');

}; 


function createPO() {
	var canISubmit = window.confirm("Do you want to Submit?");	
	if(canISubmit == false) {
		return;
	}
	$(".loader-ims").show();
	$('#saveBtnId').attr('disabled', 'disabled');
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("ProductWiseIndentsFormId").action = "SavePoDetails.spring";
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
					<div>
					 <div class="loader-ims" id="loaderId" style="display:none;"> <!--  -->
		        	 	<div class="lds-ims">
			       			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		        		<div id="loadingimsMessage">Loading...</div>
	           		</div>
						<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
						<div class="col-md-12 border-inwards">
							  <div class="col-md-12">
							        <div class="col-md-4">
							           <div class="form-group">
									        <label class="control-label col-md-6">Vendor Name :</label>
											<div class="col-md-6">
												<form:input path="vendorName" id="VendorNameId" class="form-control" title="${vendorName}" value="${vendorName}" readonly="true" />
											</div>
									   </div>
									</div>
									<div class="col-md-4">
							           <div class="form-group">
												<label class="control-label col-md-6">GSTIN :</label>
												<div class="col-md-6">
													<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" title="${strGSTINNumber}" value="${strGSTINNumber}" autocomplete="off" readonly="true" />
												</div>
										</div>
									</div>
									<div class="col-md-4">
							           <div class="form-group">
											<label class="control-label col-md-6">Vendor Address : </label>
											<div class="col-md-6" >
												<form:input path="vendorAddress" id="VendorAddress" class="form-control" title="${vendorAddress}" value="${vendorAddress}" readonly="true" />
											</div>
									   </div>
									</div>
							        <div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-md-6">Site Wise Indent No :</label>
											<div class="col-md-6" >
												<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value=${indentNo}>
												<input type="text" id="siteWiseIndentNo" name="siteWiseIndentNo" class="form-control" readonly="true" value=${siteWiseIndentNo}>
											</div>
									   </div>
									</div>
									 <div class="col-md-4">
										<div class="form-group">
										 	<label class="control-label col-md-6">Project Name :</label>
											<div class="col-md-6">
												<input type="text" id=siteName" name="SiteName" class="form-control" title="${SiteName}" readonly="true" value="${SiteName}">
												<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${SiteId}">
												<input type="hidden" id="siteId" name="siteId" class="form-control" value="${SiteId}">
											</div>
										</div>
									</div>
									 <div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-md-6 ">Delivery Date :</label>
											<div class="col-md-6 input-group">
												<input type="text" id="deliveryDate" name="deliveryDate" class="form-control deliveryDate readonly-color" autocomplete="off" readonly="true" placeholder="dd-mm-yy">
												<label class='btn input-group-addon'  for='deliveryDate'><span class='fa fa-calendar'></span></label>
											</div>
										</div>
									 </div>
									 
									  <div class="col-md-4">
										<div class="form-group">
											<label class="control-label col-md-6">Payment Req Days :</label>
											<div class="col-md-6">
											<input type="text" id="days" name="days" class="form-control "  onkeypress="return isNumberCheck(this, event)" >
												<!-- <input type="number" id="days" name="days" class="form-control " min="0"  > -->
											</div>
										</div>
									 </div>
									
									<div class="col-sm-2 col-xs-12" >
										<!-- <input type="hidden" id="vendorId" name="vendorId" class="form-control"  /> -->
									</div>
										<div class="col-sm-2 col-xs-12" >
										<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" />
									</div>
								
									 
									<div class="col-sm-2 col-xs-12" >
										<%-- <input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" /> --%>
										<input type="hidden" id="strCreationDate" name="strCreationDate" class="form-control" value="${strCreationDate}" />
										<input type="hidden" id="allSiteIds" value="${Allsites}"/>
									</div>
								</div>
					     </div>
				  
				
					      <div class="clearfix"></div>
					       <div class="table-responsive  Mrgtop20">  <!-- protbldiv -->
								<table id="doInventoryTableId" class="table table-new" style="width:3500px;">
								<thead class="calc-thead-inwards">
									<tr> <!-- class="table_header" -->
										<th>S NO</th>
					    				<th>Product</th>
					    				<th>Sub Product</th>
					    				<th>Child Product</th>  
					    				<th><%= vendorDescription %></th>  								
					    				<th>Units Of Measurement</th>
					    				<th>Quantity</th> 
    				                                        <th> Price</th>
					    				<th>BasicAmount</th>
					    				<th>Discount</th>
					    				<th>AmountAfterDiscount</th>
					    				<th>Tax</th>
					    				<th>HSNCode</th>
					    				<th>Tax Amount</th>
					    				<th>AmountAfterTax</th>
					    				<th>Other Or TransportCharges</th>
					    				<th>TaxOn Other Or TransportCharges</th>
					    				<th>Other Or TransportChargesAfterTax </th>
					    				<th>Total Amount </th>
										<th>Actions</th>
										
									</tr>
								</thead>
								<tbody class="tbl-fixedheader-tbody">
								  <input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" />
								  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
					        	  <input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="indentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
					        	 	 <tr id="tr-class${GetProductDetails.serialno}" class="productTableRow">
							        	 	<td>	
										       <div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
												<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
											</td>
											<td>
												<select id="combobox${GetProductDetails.serialno}" name="Product${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1 value="${GetProductDetails.productName}">
												<option value="${GetProductDetails.productId}$${GetProductDetails.productName}">${GetProductDetails.productName}</option>	
													
											    		<%			
											    		
											    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
											    			for(Map.Entry<String, String> prods : prod.entrySet()) {
															String prodIdAndName = prods.getKey()+"$"+prods.getValue();
														%>
															<option style="display: none" ="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
											    		<% } %>
												</select>
												
											</td>
											<td>
											<form:select path="" id="comboboxsubProd${GetProductDetails.serialno}"   name="SubProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
											<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
											</form:select>
											</td>
											<td>
												<form:select path="" id="comboboxsubSubProd${GetProductDetails.serialno}"  name="ChildProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
												<option value="${GetProductDetails.child_ProductId}$<c:out value="${GetProductDetails.child_ProductName}"/>"><c:out value="${GetProductDetails.child_ProductName}"/></option>
												 <input type="hidden"  id="childProduct${GetProductDetails.serialno}"   value="<c:out value='${GetProductDetails.child_ProductName}'/>"/>
						                        
											      </form:select>
											</td>
											
											<td >
						                        <input type="text" maxlength="999"  id="childProductVendorDesc${GetProductDetails.serialno}"   name="childProductVendorDesc${GetProductDetails.serialno}" onblur="vendorTooltip(${GetProductDetails.serialno})"   class="form-control vendDesc" data-toggle="tooltip" data-placement="bottom"  value="<c:out value='${GetProductDetails.childProductCustDisc}'/> " /> 
											</td>
											<td>
											<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.serialno}"  name="UnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
												<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
											</form:select>
											</td>
											<td>
						                        <input type="text"  id="stQantity${GetProductDetails.serialno}" onkeypress="return validateNumbers(this, event)" onchange="calculatequantitybase(${GetProductDetails.serialno})"   name="quantity${GetProductDetails.serialno}" onblur="calculateTotalAmount(${GetProductDetails.serialno})"  autocomplete="off" class="form-control" value="${GetProductDetails.requiredQuantity}"/>
						                        <input type="hidden"  id="poIntiatedQuantity${GetProductDetails.serialno}"    name="poIntiatedQuantity${GetProductDetails.serialno}"  value="${GetProductDetails.poIntiatedQuantity}"/>
						                        <input type="hidden"  id="strRequestQuantity${GetProductDetails.serialno}"    name="strRequestQuantity${GetProductDetails.serialno}" value="${GetProductDetails.purchasedeptRequestReceiveQuantity}"/>
						                        <input type="hidden"  id="groupId${GetProductDetails.serialno}"    name="groupId${GetProductDetails.serialno}" value="${GetProductDetails.groupId}"/>
						                        <input type="hidden"  id="avalBOQQty${GetProductDetails.serialno}"    name="avalBOQQty${GetProductDetails.serialno}" value="6"/>
						                        <input type="hidden"  id="BOQQty${GetProductDetails.serialno}"    name="BOQQty${GetProductDetails.serialno}" value="10"/>
						                        <input type="hidden"  id="oldQuantity${GetProductDetails.serialno}"    name="oldQuantity${GetProductDetails.serialno}" value="${GetProductDetails.requiredQuantity}"/>
						                         
											</td>	
											<td class="width-change">
											  <input type="text"  id="price${GetProductDetails.serialno}" autocomplete="off"  name ="PriceId${GetProductDetails.serialno}"  onkeypress="return validateNumbers(this, event)" onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}"/>
											</td>
											<td> 
											<input type="text" id="BasicAmountId${GetProductDetails.serialno}" autocomplete="off"  name ="BasicAmountId${GetProductDetails.serialno}" class="form-control" value="${GetProductDetails.basicAmt}" readonly="readonly"/>  <!--   onblur="calculateTaxAmount(${GetProductDetails.serialno})" -->
											</td>
											<td> 
											   <input type="text" id="Discount${GetProductDetails.serialno}" autocomplete="off"  onkeypress="return validateNumbers(this, event)" name="Discount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(${GetProductDetails.serialno});"  class="form-control disable-class1" value="${GetProductDetails.strDiscount}"/><i  style="    position: absolute;right: 10px; top: 17px;" class="fa fa-percent"></i>
											</td>
											<td>
											   <input type="text" id="amtAfterDiscount${GetProductDetails.serialno}"   name ="amtAfterDiscount${GetProductDetails.serialno}"   class="form-control" value="${GetProductDetails.strAmtAfterDiscount}" readonly="readonly"/>
											</td>
											<td>
												<form:select path="" id="taxAmount${GetProductDetails.serialno}"  name ="tax${GetProductDetails.serialno}"    onchange="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" >
													<form:option value="${GetProductDetails.taxId}" >${GetProductDetails.tax}</form:option>
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
											<input type="text" id="hsnCode${GetProductDetails.serialno}" name ="hsnCode${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.hsnCode}"/>
											</td>
											<td>
											<input type="text" id="TaxAmount${GetProductDetails.serialno}"  name ="taxAmount${GetProductDetails.serialno}" readonly="true"  class="form-control" value="${GetProductDetails.taxAmount}"/>
											</td>
											<td>
											<input type="text" id="TaxAftertotalAmount${GetProductDetails.serialno}"  name ="amountAfterTax${GetProductDetails.serialno}" readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
											</td>
											<td>
											    <input type = "text" id="OtherOrTransportChargesId${GetProductDetails.serialno}"  name="otherOrTransportCharges${GetProductDetails.serialno}" readonly="true"   class="form-control" value="${GetProductDetails.othercharges1}"/>
											</td>
											<td>
									         	<input  type="text" id="TaxOnOtherOrTransportChargesId${GetProductDetails.serialno}"  name="taxOnOtherOrTransportCharges${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.taxonothertranportcharge1}"/>
											</td>
											<td>
											<input type="text" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.serialno}"  name="otherOrTransportChargesAfterTax${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.otherchargesaftertax1}"/>
											</td>
											<td>
											    <input type="text"   class='ttamount form-control' id="TotalAmountId${GetProductDetails.serialno}"  name="totalAmount${GetProductDetails.serialno}" readonly="true"    class="form-control"  value="${GetProductDetails.totalAmount}"/>
											    <input type = "hidden"  id="isNewOrOld${GetProductDetails.serialno}"  name="isNewOrOld${GetProductDetails.serialno}"  class="form-control"  autocomplete="off"  value="old" readonly="true"/>
											</td>
											<td style="display:none">
											   <input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control  autocomplete="off"  value=""/>
											</td>  
											<td style="display:none">
											   <input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control  autocomplete="off"  value=""/>
											</td>
											<td>
												<input  type = "hidden" id="idDeletedOrNot${GetProductDetails.serialno}"  value=""/>
												<button type="button" name="addremoveItemBtn${GetProductDetails.serialno}" id="addremoveItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-remove"></i></button>
												<button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow(${GetProductDetails.serialno})" class="btnaction"><i class="fa fa-plus"></i></button>
												<input type="hidden" name="addNewrowItemBtn${GetProductDetails.serialno}" id="addNewrowItemBtn${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"/>
										   </td>
									</tr>
						 	    </c:forEach> 
						 	</tbody>
					</table>
				</div>
				<tbody>					
				  <tr>
					    <td style="display:none">
						  <input  type = "hidden" id="eactionValueId" style="display:none" name="eactionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
						</td> 
					   <td style="display:none">
					      <input  type = "hidden" id="ractionValueId" style="display:none" name="ractionValue"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
					   </td>
				  </tr>
				</tbody>
				<div><%	List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");
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
									out.println(hiddenFieldHtml1); %>  
				</div>
				<br>
				<div class="clearfix"></div><br/>
				<div class="table-responsive "> <!-- protbldiv -->
						<table id="doInventoryChargesTableId" class="table table-new">
							<thead class="calc-thead-inwards">
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
							  <tr id="chargesrow1" class="chargesRow">
									<td>						
										<div id="snoChargesDivId1">1</div>
									</td>
									<td>						
											<form:select path="" id="Conveyance1" name="Conveyance1" class="form-control" onchange="changeconveyance(1)">
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
									<%
									
									int i=1;
									%>
										<form:input path="" id="ConveyanceAmount1" name="ConveyanceAmount1" type="text"  onkeypress="return validateNumbers(this, event)" placeholder="Please enter Amount" onchange="calculateGSTTaxAmount(1)" class="form-control noneClass" autocomplete="off"/>
									</td>
									<td>
										<form:select path="" id="GSTTax1" name="GSTTax1" class="form-control GSTClass "  onchange="calculateGSTTaxAmount(1)">
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
										<form:input path="" id="GSTAmount1" name="GSTAmount1" type="number" readonly="true" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off"/>
									</td>
										<td>
										<form:input path="" type="number"  name="AmountAfterTax1" id="AmountAfterTax1" readonly="true" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off"/>
									</td>
								
									<td>
										<form:input path=""  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number" class="form-control" autocomplete="off"/>
									</td>
									<td>
										<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
										<button type="button" style="display:none;"  name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
								</td>
								</tr>
								
							</tbody>
							</table>
							</div><br/>
							<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
							<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
							<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
							<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
							<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
							<input type="hidden" name="VendorId" value="" id="vendorIdId">
								
								
							<div class="col-md-12 no-padding-left">
							<div class="col-md-6 notemaindiv no-padding-left">
								<div class="col-md-2 notecls no-padding-left">Note :</div>
								<div class="col-md-10">
									<textarea rows="3" placeholder="please enter the comment" name="note" class="form-control resize-vertical notetextarea"></textarea>
								</div>
							</div>	
							<div class="col-md-offset-2 col-md-4">
								<div class="col-md-12">
									<div class="col-md-5 no-padding-left no-padding-right">
										<strong class="finalAmountDiv">Final Amount</strong>
									</div>
									<div class="col-md-1">
										<strong class="finalAmountDiv">:</strong>
									</div>
									<div class="col-md-5 pull-right">
										<span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv" ></span>
										<input type="hidden" id="${GetProductDetails.serialno}" value="${GetProductDetails.finalamtdiv}"/>
									</div>
								</div>
						    </div>
								 
						   </div>
							<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
							
							<div class="col-md-12" style="margin-top: 50px;">						
						     <div class="floatLeft">
							  <h4 class="text-left Mrgtop20"><strong>You can upload Images/PDF(s) here </strong>(<span style="font-size: 14px;"> Note :  Now you can upload upto 8 file's</span>)</h4>
							  <P><strong>Maximum File Upload Size: 1MB</strong></P>
							  <div class="col-md-12 files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <!-- <i class="fa fa-plus"></i>  -->
								<div class="col-md-12">
								 <div class="market_file_style">
								  <div class="clearfix"></div>
								 </div>
								</div>
								<input type="file" id="file" name="file" style="display:none">
							 </div> 
						    </div>
				         <input type="hidden" name="kilobyte" id="kilobyte" value="${KILO_BYTE}">
				      <br/>
				      	<div class="col-md-12 text-center center-block form-submit Mrgtop20" style="margin-bottom:30px;">
							<input class="btn btn-warning" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">								
							<input class="tblchargesbtn btn btn-warning" type="button" value="Submit" onclick="createModelPOPUP()" id="saveBtnId">								
						</div>
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
						     <c:forEach items="${listTermsAndCondition }" var="item">
						      <div class="col-md-12 remove-filed Mrgtop10">
						      <div class="col-md-10">
						      <input type="text" name="termsAndCond" class="form-control workorder_modal_text" value="${item.strTermsConditionName}"/>
						      </div>
						      <div class="col-md-2 margin-close">
						      <button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
						      </div>
						      </div>
						      </c:forEach>
						      
						      </div>
						         <div class="col-md-12 padding-div Mrgtop10">
						       <div class="input_fields_wrap">
						         <div class="col-md-10"><input type="text" class="form-control workorder_modal_text" id="workorder_modal_text1" name="termsAndCond" value=""/></div>
						         <div class="col-md-2 margin-close"><button type="button" class="btn btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
						        
						      </div>
						    </div>
						     <br>
						         
						            
						            
						            </div>
						        </div>
							</div>
						</div>
				        <div class="modal-footer" style="border-top: 1px solid #fff;">
				        <div class="col-md-12">
				           <div class="emailCC Mrgtop10">
				        	<%--  <input type="checkbox" name="check1" id="ccemailcheckbox" onclick="dynInput(this);" value="${ccEmails}" /> --%>
				        	<span class="spanheading mrg-btm-10">(Optional)If you want to add CC In emails.</span>
				        	 <div class="widtheightytwo">
				        	   <input type="text" id="" name="ccEmailId" value="${ccEmails}" class="form-control"/>
				        	 </div>				     		
				           </div>				        
				           <div class="subject-container Mrgtop10">
				             <!-- <input type="checkbox" name="check2" id="sub-conmatiner" value="" /> -->
				             <span class="spanheading mrg-btm-10">Subject</span>
				      	     <!-- <input type="text" name="subject" id="" class=" form-control1 sub-control" Placeholder="Please enter the subject" style="float: left;margin-top: 3px;"/> --><br>
				      	    <div class="widtheightytwo">
				      	    	<input type="text" id="" name="subject"  class="form-control" Placeholder="Please enter the subject" />
				      	    </div>
				           </div>
				           <div class="" style="font-family: Calibri">
 							 <div class="" id="conclusionHeader"style="font-family: Calibri;margin-top:20px;text-align: left;font-size: 14px;font-weight:1000;">Conclusions:</div>
						 	  <div id="addconclusiontxt">
						 	
						 	  </div> 
						 	  <div class="conclusionMainDivCls" style="" id="conclusionMainDiv1">
						 	    <div class="hideInprintCls">
						 		  <div class="" style="width:82%;float:left;margin-top: 10px;">
						 			<input type="text" class="form-control conclusiontxt" name="conclusionDesc" id="conclusionId1">
						 		  </div>
						 		  <div class="addConclusionBtnDiv">
						 			<button type="button"  id="conclusionAddBtn"  onclick="appendConclusionTxtBx()" class="btn btn-success plus-button addbtncls "><i class="fa fa-plus"></i></button>
						 		 </div>
						 	   </div> 
						     </div>    
 						   </div>
				        </div>
				        <div class="col-md-12 text-center center-block Mrgtop10">
				            <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="createPO()">Submit</button>
				        </div>
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
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/CreatePO.js" type="text/javascript"></script>
         <script src="js/sidebar-resp.js" type="text/javascript"></script>
        
		<script>
		 //for combobox
		 debugger;
		var tablelength=$("#doInventoryTableId > tbody > tr").length;	
		for(var i=1;i<=tablelength;i++){
			 $("#combobox"+i).combobox();    
			 $("#comboboxsubProd"+i).combobox();	
			 $("#comboboxsubSubProd"+i).combobox();
		}	  			 
		</script>
		
		<script>
		$(document).ready(function() {	
			$('#days'). bind('copy paste cut',function(e) {
				e. preventDefault(); //disable cut,copy,paste.
			});
			$(".up_down").click(function(){ 
				$(function() {
					for(i=1;i<=100;i++){
						$("#combobox"+i).combobox();    
						$( "#toggle").click(function() { $( "#combobox"+i).toggle();  });
						$( "#comboboxsubProd"+i).combobox();
						$("#comboboxsubSubProd"+i).combobox(); 
						$("#UnitsOfMeasurementId"+i).combobox();
					}
						   
				}),
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			}); 
			for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
				$('.btn-visibilty'+i).prop('readonly', true).css('cursor','not-allowed');
				$('.bt-taxamout-coursor'+i).prop('readonly', true);
     		 	$('#Product'+i).prop('readonly', true).css('cursor','not-allowed');
     		 	$('#Product'+i).prop('title', $('#Product'+i).val());
     		 	$('#Product'+i).addClass('form-control');
				$('#SubProduct'+i).prop('readonly', true).css('cursor','not-allowed');
				$('#SubProduct'+i).prop('title', $('#SubProduct'+i).val());
				$('#SubProduct'+i).addClass('form-control');
				$('#ChildProduct'+i).prop('readonly', true).css('cursor','not-allowed');
				$('#ChildProduct'+i).prop('title', $('#ChildProduct'+i).val());
				$('#ChildProduct'+i).addClass('form-control');
				$('disable-class'+i).closest('td').find('input').attr('disabled', 'disabled');
			    $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide'); 
		   }			
		});
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
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
		function removeme(rowId){alert('');
			$("#termsAndCond"+rowId).remove();
			$('.remove-me'+rowId).hide();
		};
		
		function removeRow(rowId){debugger;
			var canIdelete=window.confirm(" Do you want to remove the Product");
			if(canIdelete==false){
				return false;
			}
			var rowscount=$('#doInventoryTableId').find('tr').length;
			var n=1;
			$(".productTableRow").each(function(){
				var rowId=$(this).attr("id").split("tr-class")[1];
				if($("#idDeletedOrNot"+rowId).val()=="D"){
					n++;
				}
			});
			if((rowscount-1)==n){
				alert("This row can't be deleted.")
				return false;
			}
			
			$("#tr-class"+rowId).addClass('strikeout');
			$("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
			$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
			$("#snoDivId"+rowId).removeAttr('id');	
			$("#price"+rowId).removeAttr('id');
			$("#TaxAmountId"+rowId).removeAttr('id');
			$("#BasicAmountId"+rowId).removeAttr('id');
			$("#tax"+rowId).removeAttr('id');
			$("#QuantityId"+rowId).removeAttr('id');
			$("#AmountAfterTaxId"+rowId).removeAttr('id');
			$("#OtherOrTransportChargesId"+rowId).removeAttr('id');
			$("#TaxOnOtherOrTransportChargesId"+rowId).removeAttr('id');
			$("#OtherOrTransportChargesAfterTaxId"+rowId).removeAttr('id');
			$("#TotalAmountId"+rowId).removeAttr('id');
			$("#Discount"+rowId).removeAttr('id');
			$("#UnitsOfMeasurementId"+rowId).removeAttr('id');
			$("#idDeletedOrNot"+rowId).val("D");
			
		
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
				  //$("#GSTAmount"+btn).removeAttr("readonly");
				  $("#AmountAfterTax"+btn).val("");
				  $("#AmountAfterTax"+btn).removeAttr("readonly");
			  }
		}
		$(function() {
		$(".deliveryDate").datepicker({
			dateFormat: 'dd-M-y',
			minDate: new Date(),
			changeMonth: true,
			changeYear: true
		});
	}); 
		</script>
		<!-- script for file upload -->
		<script>		
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
		            $(wrapper).append('<div class="col-md-12 remove-filed Mrgtop10"><div class="col-md-10"><input type="text" name="termsAndCond" value="'+tc+'" id="termsAndCond'+x+'" class="form-control workorder_modal_text"/></div><div class="col-md-2 margin-close"><button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
		        }
		        $("#workorder_modal_text1").val("");
		        $("#workorder_modal_text1").focus();
		        $("#termsAndCond"+x).val(tc);
		    });
		    
		    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
		    	//alert("Do you want to delete?");
		    	var canIDelete=window.confirm("Do you want to delete?");
		    	if(canIDelete==false){
		    		return false;
		    	}
		        $(this).parent().parent(".remove-filed").remove();
		       
		    })
		     $(wrapper1).on("click",".remove_field", function(e){ //user click on remove text
		    	 alert("Do you want to delete?");
		       $(this).parent().parent(".remove-filed").remove();
		    })
		});
		//adding conclusion 
		var id=1;
		function appendConclusionTxtBx(){
			var firstTextVal=$("#conclusionId1").val();
			if(firstTextVal.trim()==''){
				alert("please enter conclusion");
				$("#conclusionId1").focus();
				return false;
			}
			var lastTextboxId=$(".conclusionMainDivCls").attr("id").split("conclusionMainDiv")[1];
			id++;
			var addConclusionTxtBx=	'<div class="conclusionMainDivCls" id="conclusionMainDiv'+id+'"><div class="hideInprintCls"><div class=""  style="width:82%;float:left;margin-top: 10px;"><input type="text" class="form-control conclusiontxt" name="conclusionDesc" id="conclusionId'+id+'"></div><div class="addConclusionBtnDiv"><button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger remove-button"><i class="fa fa-remove"></i></button></div></div></div>';
			$("#addconclusiontxt").append(addConclusionTxtBx);
			
			$("#conclusionId"+id).val($("#conclusionId1").val());
			$("#conclusionId1").val('');	
			$("#conclusionId1").focus();		
		}
		//deleteting conclusion row written by thirupathi
		function deleteConclusionTxt(id){debugger;
			var canIDelete=window.confirm("Do you want to delete a conclusion?");
			if(canIDelete==false){
				return false;
			}			
			$(id).parents(".hideInprintCls").remove();			
		}
		//for vendor tooltip written by thirupathi
		function vendorTooltip(strSerialNumber){
			 var titleTxt = $("#childProductVendorDesc"+strSerialNumber).val();
			 $("#childProductVendorDesc"+strSerialNumber).attr('title', titleTxt); 
			 $('[data-toggle="tooltip"]').tooltip();			    
		}
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
				alert("Please Upload Below 1 mb Pdf File");		
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
				alert("Please Upload Below 1 mb Pdf File");
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
			 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style" id="market_file_style'+dynamicId+'"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" onchange="fileChange('+dynamicId+', this)" class="selectCount" style="display:none;"/><div class=""><span id="fileName'+dynamicId+'"  style="float:left;padding-right: 10px;width:200px;"></span><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;padding: 2px 5px;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
		}
		//delete file
		function filedelete(id){
			var canIDelete=window.confirm("Do you want to delete file?");
			if(canIDelete == false ){
				return false;
			}
			$("#market_file_style"+id).remove();
		}
		
		function uploadFile(){
			var classlength=$(".selectCount").length;
			 if(classlength>7){
				alert("You can't upload more than eight files");
				return false;
			}
			addFile();
		    var id=$(".selectCount:last").attr("id").split("file_select")[1];
			$("#file_select"+id).trigger("click");		
		}
	    //ellipse's  for file name if you get morethan 25 letters in uploaded file 
		function start_and_end(str) {
			if (str.length > 25) {
			   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
			}
		  return str;
		}
		
		function isNumberCheck(el, evt) {
		    var charCode = (evt.which) ? evt.which : event.keyCode;				  
		    if (charCode < 48 || charCode > 57 ) {
		        return false;
		    }else{
				return true;
			}
		}
		var referrer="${url}";
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
