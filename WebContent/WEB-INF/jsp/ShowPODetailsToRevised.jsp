<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.File"%>
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
<style>
.btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
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
 /* width: 75% !important; */
 border-radius: 0px !important;
 margin-bottom: 10px;
 border: none;
    box-shadow: none;
    border-bottom: 2px solid #ccc;
 }
 
 #isNewOrOld{
 display: none;
 }
 .notemaindiv{
 	margin-bottom:30px;
 }
.notecls{
    text-align: left;
    margin-left: -15px;
 }
 .notetextarea{
 	resize:vertical;
 	min-height: 30px;
 }
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
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
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
		
	<style>
	.fixedheader thead, .fixedheader tbody tr{table-layout:fixed;display:table;width:100%;}
.fixedheader thead tr th:first-child, .fixedheader tbody tr td:first-child{width:52px;}
.btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
.table-new tbody tr td{border-top:0px !important;}
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

.mybtnstyles {
    height: 30px;
    right:0px;
    position:absolute;
    margin-top: -33PX;
    border-radius: 0px !important;
    margin-bottom: 10px;
    background-color: transparent;
}
 <!-- style for modapopup add remove text -->

		#mainDivId {
			display : none;
		}
		
		
	 .header-modalworkOrder{
	     background-color: #bdb7ab;
    border: 1px solid #bdb7ab;
    color: #fff;
    border-radius: 50px;
    margin-top: 5px;
    margin-left: 5px;
    margin-right: 5px;
	 
	 }
	 .padding-div{
	 padding-right: 30px !important;
    padding-left: 30px !important;
	 }
	 .workorder_modal_text{
	 border-bottom:2px solid #bdb7ab !important;
	 border-top:none !important;
	 border-left:none !important;
	 border-right:none !important;
	 border-radius:0px !important;
	 }
	 .remove-button{
	 font-size: 12px;
    padding: 5px 6px;
	 }
	 .plus-button{
	
    font-size: 12px;
    padding: 5px 6px;
	 }
	 .red{
	 color:red;
	 font-size:14px;
	 font-weight:bold;
	 }	
	 .black{
	  color:black;
	 font-size:14px;
	 font-weight:bold;
	 }
	 .control-text{
	 border:1px solid #ddd !important;
	 }

<!-- Modal popup for add remove -->
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
.marg-left-15{
margin-left:15px;
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
.btncls{
    font-size: 12px;
    padding: 8px 12px;
}
.margin-close{
	text-align:left;
	margin-top:10px;
}
.spanheading{
    font-size: 14px;
    font-weight: 1000;
    font-family: Calibri;
}
/* written by thirupathi */
.percentagecls{
    position: absolute;
    right: 15px;
    top: 19px;
}	
@media only screen and (min-width:320px){
.calc-thead-inwards{    
    color: #000;
    background: #ccc;
    width: calc( 100% - 17px ) !important;}
}
.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
.table-new thead tr th:first-child, .table-new  tbody tr td:first-child{width:76px;text-align: center;}
#doInventoryChargesTableId thead tr th:last-child, #doInventoryChargesTableId tbody tr td:last-child{width:150px;}
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
	 
	 	 var vendorDescriptionColumn =  "<%= vendorDescription %>";
	 	vendorDescriptionColumn = formatColumns(vendorDescriptionColumn);
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
    	    var div1 = document.createElement("input");
    	    div1.setAttribute("type", "hidden");
    	    div1.setAttribute("name", "groupId"+vfx);
    	    div1.setAttribute("id", "groupId"+vfx);
    	    cell.appendChild(div1);
    	}   

    	else if(tableColumnName == vendorDescriptionColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "childProductVendorDesc"+vfx);
		    div.setAttribute("id", "childProductVendorDesc"+vfx);
		    div.setAttribute("class", 'form-control');	
		   // div.setAttribute("style", 'width: 160px;');
		    cell.appendChild(div);		    
   		}
    	
    	else if(tableColumnName == measurementColumn) {
    		var dynamicSelectBoxId = "UnitsOfMeasurementId"+vfx; 
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("onchange", "return validateProductAvailability(this);");
    	   	cell.appendChild(div);
    	}    	
    	else if(tableColumnName == taxColumn) {
    		var dynamicSelectBoxId = "taxAmount"+vfx;
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
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("onblur", "return validateUnitsAndAvailability(this);");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("class", 'form-control');	
		    div.setAttribute("autocomplete", 'off');	
		    cell.appendChild(div);		    
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", "ReceivedQty"+vfx);
		    div2.setAttribute("id", "ReceivedQty"+vfx);
		    cell.appendChild(div2);	
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div.setAttribute("onblur", "return validateRevisedBOQQuantity("+vfx+", 'New');");
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);	 
		  /*   
		    var div3 = document.createElement("input");
		    div3.setAttribute("type", "text");
		    div3.setAttribute("name", productAvailabilityColumn+vfx);
		    div3.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div3.setAttribute("readonly", "true");
		    div3.setAttribute("class", 'form-control');
		    cell.appendChild(div3);	 */	 
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Price"+"Id"+vfx);
		    div.setAttribute("id", "price"+vfx);
		    div.setAttribute("autocomplete", 'off');
		    //div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		   div.setAttribute("onkeypress","return isNumberCheckRevisedPO(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "BasicAmount"+"Id"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculatePriceAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		    div.setAttribute("autocomplete", 'off');
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control disable-class1');
		    cell.appendChild(div);
		    
		    var i=document.createElement("i");
		    i.setAttribute("class", "fa fa-percent percentagecls");
		    cell.appendChild(i);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "amtAfterDiscount"+vfx); 
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("id", "amtAfterDiscount"+vfx); 
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control' );
		    div.setAttribute("readonly", 'true' );
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
		    div.setAttribute("type", "text");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
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
		    
		    var div = document.createElement("input");
		     div.setAttribute("type", "hidden"); 
		    div.setAttribute("id", "actionValueId"+vfx);
		    div.setAttribute("value", "N");
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
    	   
    	}
    	else if(tableColumnName == conveyanceAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
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
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("placeholder", "Amount After Tax");
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
		    
		    var div1 = document.createElement("input");
		    div1.setAttribute("type", "hidden");
		    div1.setAttribute("name", "actionValueId"+vfx);
		    div1.setAttribute("id", "actionValueId"+vfx);
		    div1.setAttribute("value", "N");
		    cell.appendChild(div1);	
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

function validateDiscount(){
	var valid = true;
	$(".disable-class1").each(function(){
		debugger;
		 
		if($(this).val()==''){
			debugger;
			alert("Please Enter Discount.");
			var InputId=$(this).attr("id");
			$("#"+InputId).focus();
			return valid=false;
		}		
	});	
	return valid;
}

function createModelPOPUP(id){
	debugger;
	var CurrentId=$(id).attr('id');
	var discountVal=validateDiscount();
	if(discountVal==false){
		return false;
	}	
	var validateQty=validateUpdatePoQty();
	if(validateQty==false){
		return false;
	}
	calculateOtherCharges();
	$(CurrentId).attr("data-target", "myModal1");
	$('#myModal1').modal('show');
}; 

function validateUpdatePoQty(){
	var error=true;
	$(".productRow").each(function(){
		var id=$(this).attr("id").split("tr-class")[1];
		if($("#actionValueId"+id).val()!="R"){
			var product=$("#Product"+id).val();
			var SubProduct=$("#SubProduct"+id).val();
			var ChildProduct=$("#ChildProduct"+id).val();
			var UnitsOfMeasurementId=$("#UnitsOfMeasurementId"+id).val();
			var stQantity=$("#stQantity"+id).val();
			var price=$("#price"+id).val();
			var Discount=$("#Discount"+id).val();
			var taxAmount=$("#taxAmount"+id).val();
			if($("#actionValueId"+id).val()=="N"){
				if(product==""){
					alert("Please select product.");
					$("#Product"+id).focus();
					return error=false;
				}
			}else{
				if($("#product"+id).val()==""){
					alert("Please select product.");
					$("#product"+id).focus();
					return error=false;
				}
			}			
			if(SubProduct==""){
				alert("Please select sub product.");
				$("#SubProduct"+id).focus();
				return error=false;
			}
			if(ChildProduct==""){
				alert("Please select child product.");
				$("#ChildProduct"+id).focus();
				return error=false;
			}
			if(UnitsOfMeasurementId==""){
				alert("Please select UOM.");
				$("#UnitsOfMeasurementId"+id).focus();
				return error=false;
			}
			if(stQantity=="" || stQantity=="0"){
				alert("Please enter quantity.");
				$("#stQantity"+id).focus();
				return error=false;
			}			
			if(price==""){
				alert("Please enter price.");
				$("#price"+id).focus();
				return error=false;
			}
			if(Discount==""){
				alert("Please enter discount.");
				$("#Discount"+id).focus();
				return error=false;
			}
			if(taxAmount==""){
				alert("Please enter tax amount.");
				$("#taxAmount"+id).focus();
				return error=false;
			}
		}else{
			
		}
	});
	return error;
}


function createPO() {debugger;
	
	
	calculateOtherCharges();
	/* var result=validateFileExtention();
	  if(result==false){
		  return false;
	  } */
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	excuteDeleteFunction();
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("ProductWiseIndentsFormId").action = "editAndSaveRevisedPO.spring";
	document.getElementById("ProductWiseIndentsFormId").method = "POST";
	document.getElementById("ProductWiseIndentsFormId").submit();
}
function updatePo() {debugger;


calculateOtherCharges();

var canISubmit = window.confirm("Do you want to Submit?");

if(canISubmit == false) {
	return;
}

//document.getElementById("saveBtnId").disabled = true;	
//document.getElementById("countOfRows").value = getAllProdsCount();	
document.getElementById("countOfRows").value = getAllProdsCount();
document.getElementById("countOfChargesRows").value = getAllChargesCount();
document.getElementById("ProductWiseIndentsFormId").action = "editAndSaveUpdatePO.spring";
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
							<li class="breadcrumb-item active">${requestScope['POType']}</li>
						</ol>
					</div>
					
					
					<div>
	<div>
	<% String isPoUpdated=request.getAttribute("isPoUpdated")== null ? "" : request.getAttribute("isPoUpdated").toString(); 
	
	if(isPoUpdated.equalsIgnoreCase("false")){
		
	
	%>
	
	
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="container">
	  <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">	
	  <div class="col-md-12 border-inwards">
	  <div class="col-md-4">
	  <div class="form-group">
			<label class="control-label col-md-6">Vendor Name : </label>
			<div class="col-md-6" >
				<form:input path="vendorName" id="VendorNameId" class="form-control" data-toggle="tooltip" title="${poDetails.vendorName}" value="${poDetails.vendorName}" readonly="true" />
			<input type="hidden" name="isUpdated" id="isUpdated" value="false"/>
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
			<label class="control-label col-md-6">Site Wise Indent No :</label>
			<div class="col-md-6" >
				<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${poDetails.indentNo}">
				<input type="text" id="siteWiseIndentNo" name="siteWiseIndentNo" class="form-control" readonly="true" value="${poDetails.siteWiseIndentNo}">
			</div>
			</div>
			</div>
			 <div class="col-md-4">
	  <div class="form-group">
			
		 	<label class="control-label col-md-6">Site Name :</label>
			<div class="col-md-6" >
			<input type="text" id=SiteName" name="strSiteName" class="form-control" readonly="true" value="${poDetails.siteName}" data-toggle="tooltip" title="${poDetails.siteName}"/>
				<input type="hidden" id="siteName" name="SiteName" class="form-control" readonly="true" value="${SiteName}"/>
				  <input type="hidden" id=url" name="url" class="form-control" readonly="true" value="${url}"> 
				<input type="hidden" id="toSite" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}"/>
				<input type="hidden" id="POTotalId" name="POTotal" class="form-control" readonly="true" value="${poDetails.finalamtdiv}"/>
			</div>
			</div>
			</div>
			 <div class="col-md-4">
	  <div class="form-group"> 
			<label class="control-label col-md-6">PO NO : </label>
			<div class="col-md-6" >
				<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off"  readonly="true" value="${poNumber}" data-toggle="tooltip" title="${poNumber}">
			<input type="hidden" id="strCreationDate"  name ="strCreationDate"  class="form-control" value="${poDetails.strCreateDate}"/>
			<c:set var = "po_Number" scope = "session" value = "${poNumber}"/>
			
			</div>
			
		

		
		</div>
		</div>
		 <div class="col-md-4">
	  <div class="form-group">
	
			<label class="control-label col-md-6">Delivery Date :</label>
			<div class="col-md-6 input-group" >
				<input type="text" id="deliveryDate" name="deliveryDate" class="form-control deliveryDate readonly-color" autocomplete="off" value="${poDetails.strDeliveryDate}" autocomplete="off">
				<label class="input-group-addon btn datepicker-paymentreq-fromdate input-group-addon-border" id="datepickerIcon" for="deliveryDate">
					<span class="fa fa-calendar"></span>
				</label>
				<input type="hidden" id="olddeliveryDate" name="olddeliveryDate" class="form-control deliveryDate" value="${poDetails.strDeliveryDate}" autocomplete="off">
			</div>
		</div>
		</div>
		 <div class="col-md-4">
	  <div class="form-group">
	
			<label class="control-label col-md-6">Payment Req Days :</label>
			<div class="col-md-6" >
				<input type="text" id="days" name="days" class="form-control " value="${poDetails.payment_Req_days}" onkeypress="return isNumberCheckForDays(this, event)" autocomplete="off">
				<input type="hidden" id="olddays" name="olddays" class="form-control " value="${poDetails.payment_Req_days}" onkeypress="return isNumberCheckForDays(this, event)" >
			</div>
		</div>
		</div>
		

			<div class="col-md-4" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${poDetails.vendorId}" />
				<input type="hidden" id="poEntryId"  name ="poEntryId"  class="form-control" value="${poDetails.poEntryId}"/>
				<input type="hidden" id="oldPODate"  name ="oldPODate"  class="form-control" value="${poDetails.poDate}"/>
				<input type="hidden" id="toSiteId" name="siteId" class="form-control" readonly="true" value="${poDetails.site_Id}"/>
				<input type="hidden" id="allSiteIds" name="allSiteIds" class="form-control" value="${Allsites}" />
			</div>
		
		
		<div class="form-group col-xs-12" style="margin-bottom:22px;">
		</div>
	</div>
	</c:forEach>

</div>

	      <div class="clearfix"></div>
	       <div class="table-responsive Mrgtop10">    
	   
				<table id="doInventoryTableId" class="table pro-table table-new" style="width:4500px;">
				<thead class="calc-thead-inwards">
				<tr class="table_header" style="  background: #eae7e7;">
					<th>S NO</th>
    				<th>Product</th>
    				<th>Sub Product</th>
    				<th>Child Product</th>  
    				<th><%= vendorDescription %></th>  								
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
				<tbody class="tbl-fixedheader-tbody">
				<input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
	        	 	
	        	 	
	        	 	<input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="indentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
	        	 		
	        	 	
	        	 	
	        	 	 <tr id="tr-class${GetProductDetails.serialno}" class="productRow">
	        	 	<td>	
	        	  						
				       <div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="product${GetProductDetails.serialno}" readonly="true" name="Product${GetProductDetails.serialno}" data-toggle="tooltip" title="${GetProductDetails.productName}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1 value="${GetProductDetails.productName}">
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
						<form:select path="" id="ChildProduct${GetProductDetails.serialno}" readonly="true" name="ChildProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}" >
						<option value="${GetProductDetails.child_ProductId}$<c:out value="${GetProductDetails.child_ProductName}"/>"><c:out value="${GetProductDetails.child_ProductName}"/></option>
					</form:select>
					<input type="hidden"   id='ChildProduct${GetProductDetails.serialno}'  class="ui-autocomplete-input" value="${GetProductDetails.child_ProductName}"/>
					</td>
					
					<td >
                        <input type="text" maxlength="500"  id="childProductVendorDesc${GetProductDetails.serialno}"  readonly="true" name="childProductVendorDesc${GetProductDetails.serialno}" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="<c:out value='${GetProductDetails.childProductCustDisc}'/>"/> 
					</td>
					<td>
					<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="UnitsOfMeasurement${GetProductDetails.serialno}"  class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
						<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
					</form:select>
					</td>
					<td >
                        <input type="text"  id="stQantity${GetProductDetails.serialno}"    name="quantity${GetProductDetails.serialno}" onkeypress="return isNumberCheckRevisedPO(this, event)" onfocusout="validateRevisedBOQQuantity(${GetProductDetails.strSerialNumber}, 'Old')" onblur="checkQuantity(${GetProductDetails.serialno})" onchange="calculatequantitybase(${GetProductDetails.serialno},'Old')" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					<input type="hidden"  id="stQuantity${GetProductDetails.serialno}"    name="strQuantity${GetProductDetails.serialno}" onchange="calculatequantitybase(${GetProductDetails.serialno},'Old')" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					<input type="hidden"  name="ReceivedQty${GetProductDetails.serialno}" id="ReceivedQty${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.recivedQty}"/> 
					<input type="hidden"  name="groupId${GetProductDetails.serialno}" id="groupId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.groupId}"/>
					<input type="hidden"  name="oldQuantity${GetProductDetails.serialno}" id="oldQuantity${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.requiredQuantity}"/>  
					</td>					
				
					<td>
					<input type="text" id="price${GetProductDetails.serialno}"  name ="PriceId${GetProductDetails.serialno}"   onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}" onkeypress="return isNumberCheckRevisedPO(this, event)"/>
					</td>
					<td> 
					<input type="text" id="BasicAmountId${GetProductDetails.serialno}"  name ="BasicAmountId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.basicAmt}" />
					</td>
						<td> 
					<input type="text" id="Discount${GetProductDetails.serialno}" name="Discount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(${GetProductDetails.serialno});" readonly="true" class="form-control disable-class1" value="${GetProductDetails.strDiscount}" onkeypress="return isNumberCheckRevisedPO(this, event)"/><i class="fa fa-percent percentagecls"></i>

					
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
					<input type = "hidden"  id="isNewOrOld${GetProductDetails.serialno}"  name="isNewOrOld${GetProductDetails.serialno}"  class="form-control"  autocomplete="off"  value="old" readonly="true"/>
					</td>	
					
					<td>
					 	<button type="button" name="addremoveItemBtn${GetProductDetails.serialno}" id="addremoveItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-remove"></i></button>
						<button type="button" name="editItemBtn${GetProductDetails.serialno}" value="Edit Item" id="editItem${GetProductDetails.serialno}" class="btnaction" onclick="editInvoiceRow(${GetProductDetails.serialno})" ><i class="fa fa-pencil"></i></button> 
				 		<%-- <button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>  --%>
					<c:if test="${listOfGetProductDetails.size() eq GetProductDetails.serialno}">
				<button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
				</c:if>
					</td>
					<td style="display:none">
					<input type="hidden"   id='indentCreationDetailsId${GetProductDetails.serialno}'  name='indentCreationDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.indentCreationDetailsId}"/>
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


			
			
						
	<div>			<%	List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("listOfGetProductDetails");

                     String hiddenFieldHtml1="<input type=\"hidden\" id=\"numberOfRows\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); %>  
			</div>
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
    				<th><%= actions %></th>
  				</tr>
			<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
				<tr class="chargesRow" id="trans-tr-class${GetTransportDetails.strSerialNumber}">
					<td>						
						<div id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
					</td>
					<td>						
							<form:select path="" id="Conveyance${GetTransportDetails.strSerialNumber}" readonly="true" name="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" onchange="changeconveyance('${GetTransportDetails.strSerialNumber}')">
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
						<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" readonly="true" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="text" onkeypress="return isNumberCheck(this, event)"  placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass" autocomplete="off" onkeyup="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})"/>
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
						<form:input path="" type="text"  name="AmountAfterTax${GetTransportDetails.strSerialNumber}" readonly="true" id="AmountAfterTax${GetTransportDetails.strSerialNumber}" placeholder="Amount After Tax"  value="${GetTransportDetails.amountAfterTaxx1}" class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" readonly="true" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					<td>
						<button type="button" name="addremoveItemBtn${GetTransportDetails.strSerialNumber}" id="addremoveChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="removeTransRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
						<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId${GetTransportDetails.strSerialNumber}' onclick="appendChargesRow(${GetTransportDetails.strSerialNumber})" class="btnaction "><i class="fa fa-plus"></i></button> 
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
			</table>
			<input type="hidden" id="noofTransRows" name="noofTransRows" value="${requestScope['listOfTransChrgsDtls'].size()}"/>
			</div><br/><br/><br/>
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
				
				
			<div class="container">
			
					<div class=""><span class="h4" style="margin-top:20px; margin-left: 71%;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv"  ></span></div>
				<input type="hidden" id="${GetProductDetails.serialno}" />
				</div>
				
				<div class="col-md-12 notemaindiv">
				<div class="col-md-1 notecls">Note:</div>
				<div class="col-md-3">
					<textarea rows="3" placeholder="please enter the comment" name="note" class="form-control notetextarea"></textarea>
				</div>
			</div>	
				
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<!-- delete pdf values -->
						<input type="hidden" name="deletePdf0" value="" id="deletePdf0">
						<input type="hidden" name="deletePdf1" value="" id="deletePdf1">
						<input type="hidden" name="deletePdf2" value="" id="deletePdf2">
						<input type="hidden" name="deletePdf3" value="" id="deletePdf3">
						
						
						
						
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
					<h3 style="">You can see the PDF(s) below :</h3>	
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
								  <iframe class="iframe-pdf filesCount" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
								  <div class="middle" data-toggle="modal" data-target="#myModalpdf<%=i%>">
								</div>
								<input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
								<p class="btn-downloaddelete"><a class="btn btn-success btn-xs" download onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i class="fa fa-download"></i> Download</a>
								<button class="btn btn-danger btn-xs" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')"><i class="fa fa-close"></i>Delete</button></p>
								</div>
								</div>
					<%} %>
					<%} %>
					</div>
					 
					 		<!-- ***********************************************this is for pdf download end************************************************** -->		
									<div class="col-md-12 Mrgtop20">
						
									<!-- <h3>You can see the Images</h3> -->
											<%
															int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
															//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
					 				if(imagecount==0){
					 					%>
					 				<!-- 	<h3>No Images</h3> -->
					 					<%
					 				}else{
					 				%>
					 					<h3 style="">You can see the Images below :</h3>
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
															 </div>
															 <input type="file" id="file" name="file" style="display:none">
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
						     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						     
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
					          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
					        </div>
					      </div>
					      
					    </div>
					  </div>
					  <%} %>
	<div id="allImgPdfUrlToDeleteFile" ></div>
		
			<%-- <div class="col-md-12" style="margin-top: 100px;">
<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("count"))); 
//String po_Number=request.getAttribute("poNumber").toString();
//String strPO_Number=po_Number.replace('/','$');
  		for(int i=0;i<pdfcount;i++)
			{
			%>
 		
 		 <%if(i==0){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf0']}" allow="fullscreen" style="height:200px;position:relative;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <%} if(i==1){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
 <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf1']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf2"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <% } if(i==2){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf2']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf3"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <% }if(i==3){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf3']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf4"><i class="fa fa-close"></i></button>
	
  </div>
</div>
 </div>
 <%} %>

  
 		
 		<%} %>
 		</div> --%>
 
 		
 		
	<!-- ***********************************************this is for pdf download end************************************************** -->		
			
			
			<%-- <div class="col-md-12" style="margin-top: 50px;">
		      <div class="col-md-4">
		      <h4 class="text-left Mrgtop20">You can upload Pdf here</h4>
		      		      <%int count=(4-pdfcount);
		       /* for(int i=count;i>=1;i--)  */
		      for(int i=1;i<=count;i++)
				{ %>
		       <input type="file" name="file" class="file<%=i%> file-left" accept="application/pdf"><button type="button" id="" style="display:none;"class="btn btn-danger btn-close<%=i%> file-left" onclick="resetFile<%=i%>()"><i class="fa fa-close"></i></button></br>
		     <!-- <input type="file" name="file" class="file2"><button type="button" style="display:none;" class="btn btn-danger btn-close2" onclick="resetFile2()"><i class="fa fa-close"></i></button> 
			<input type="file" name="file" class="file3"><button type="button" style="display:none;"class="btn btn-danger btn-close3" onclick="resetFile3()"><i class="fa fa-close"></i></button><br /> 
			<input type="file" name="file" class="file4"><button type="button" style="display:none;" class="btn btn-danger btn-close4" onclick="resetFile4()"><i class="fa fa-close"></i></button><br />  -->
			<%} %>
			 <input type="file" name="file" class="file5 file-left" id="file0" style="display:none" accept="application/pdf"><button type="button" id="" style="display:none;"class="btn btn-danger btn-close5 file-left" onclick="resetFile5()"><i class="fa fa-close"></i></button></br> 
 			<input type="file" name="file" class="file6 file-left" id="file1" style="display:none" accept="application/pdf"><button type="button" id="" style="display:none;"class="btn btn-danger btn-close6 file-left" onclick="resetFile6()"><i class="fa fa-close"></i></button></br>
 			<input type="file" name="file" class="file7 file-left" id="file2" style="display:none" accept="application/pdf"><button type="button" id="" style="display:none;"class="btn btn-danger btn-close7 file-left" onclick="resetFile7()"><i class="fa fa-close"></i></button></br> 
 			<input type="file" name="file" class="file8 file-left" id="file3" style="display:none" accept="application/pdf"><button type="button" id="" style="display:none;"class="btn btn-danger btn-close8 file-left" onclick="resetFile8()"><i class="fa fa-close"></i></button></br>  
		      </div>
		    </div> --%>
		  <input type="hidden" name="kilobyte" id="kilobyte" value="${KILO_BYTE}">
<!-- **************************************************************		 -->
			
		
			<br/>
 <div class="modal fade" id="myModal1" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Terms and Conditions</h4>
        </div>
        <form action="" method="post">
        <div class="modal-body" style="height: 400px;overflow-y: scroll;">
          <div class="container">
			<div class="row">
        		<div class="control-group" id="fields">
        			<div class="controls" id="profs"> 
     					<div class="col-md-12 appen-div-workorder">
					     <c:forEach items="${listTermsAndCondition}" var="item">
					     <input type="hidden" name="termsAndCondold" class="form-control" value="<c:out value='${item.strTermsConditionName}'/>" />
					      <div class="col-md-12 remove-filed">
						      <div class="col-md-10" style="margin-top: 10px;">
						      	<input type="text" name="termsAndCond" class="form-control" value="<c:out value='${item.strTermsConditionName}'/>" />
						      	
						      </div>
						      <div class="col-md-2 margin-close">
						     	 <button type="button" class="btn btn-danger btncls remove-button remove_field" ><i class="fa fa-remove "></i></button>
						      </div>
					      </div>
					      </c:forEach>      
      					</div>
        				<div class="col-md-12 padding-div" style="margin-top:10px;">
					       <div class="input_fields_wrap">
					         <div class="col-md-10" style="margin-top: 10px;"><input type="text" class="form-control" id="workorder_modal_text1" name="termsAndCond" value=""/><input type="hidden" name="termsAndCondold" class="form-control" value=""/></div>
					         <div class="col-md-2 margin-close"><button type="button" class="btn btn-success btncls plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
					      </div>
    					</div>
       					<div class="col-md-12">
				          	<div class="col-md-12" style="text-align: left;margin-bottom: 15px;margin-top: 15px;">
					        	<span class="spanheading marg-left-15">(Optional)If you want to add CC In emails.</span>
					       </div>
					       <div class="col-md-12">
					         <div class="col-md-10">
					       		<input type="text" id="" name="ccEmailId2" value="${ccEmails}" class="form-control"/>
					       		<input type="hidden" id="" name="oldccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/>
					       	</div>
					       </div>
				           <div class="col-md-12" style="text-align: left;margin-bottom: 15px;margin-top: 15px;">
				                <span class="spanheading marg-left-15">Subject</span>      			
				           </div>
				           <div class="col-md-12">
				        	 <div class="col-md-10">
				        		<input type="text" id="" name="subject"  class="form-control" Placeholder="Please enter the subject" value="<c:out value='${strSubject}'/>"/>
				        		<input type="hidden" id="" name="strSubject"  class="form-control"  value="<c:out value='${strSubject}'/> "/>
				        	 </div>
				           </div>
           				 <div class="col-md-12" style="font-family: Calibri">
 							<div class="marg-left-15" id="conclusionHeader"style="font-family: Calibri;margin-top:20px;text-align: left;font-size: 14px;font-weight:1000;">Conclusions:</div>
						 <div id="addconclusiontxt">
						 <!--  <div class="">
						  	<div class="hideInprintCls">
						  		<div class="col-md-10"  style="margin-top: 10px;">
						  			<input type="text" class="form-control" value="Conclusion one" name="conclusionDesc">
						  		</div>
							  	<div class="col-md-2 addConclusionBtnDiv margin-close">
							  		<button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger btncls remove-button"><i class="fa fa-remove"></i></button>
							  	</div>
						 	 </div>
						   </div>
						   <div class="">
						  	<div class="hideInprintCls">
						  		<div class="col-md-10"  style="margin-top: 10px;">
						  			<input type="text" class="form-control" value="Conclusion two" name="conclusionDesc">
						  		</div>
							  	<div class="col-md-2 addConclusionBtnDiv margin-close">
							  		<button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger btncls remove-button"><i class="fa fa-remove"></i></button>
							  	</div>
						 	 </div>
						   </div> -->
				  		</div> 
					  <div class="conclusionMainDivCls" style="" id="conclusionMainDiv1">
					    <div class="hideInprintCls">
						  <div class="col-md-10" style="margin-top: 10px;">
						  <input type="hidden" class="form-control" value="" name="conclusionDescold">
				 			<input type="text" class="form-control conclusiontxt" id="conclusionId1" name="conclusionDesc">
				 		  </div>
				    	  <div class="col-md-2 addConclusionBtnDiv margin-close">
							 <button type="button"  id="conclusionAddBtn"  onclick="appendConclusionTxtBx()" class="btn btn-success btncls plus-button addbtncls "><i class="fa fa-plus"></i></button>
						  </div>
						</div> 
					  </div>    
 			 		</div>
		 			 <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
		 			 	 <button type="button" class="btn btn-warning  form-submit" data-dismiss="modal" onclick="createPO()">Submit</button>
		 			 </div>
 				 </div>
            	</div>
        	</div>
		 </div>
    	</div>
      </div>
      <input type="hidden" name="hiddenRowNum" id="hiddenRowNum" >
      </form>
    </div>
  </div>
</div>	
</form:form>
			<div class="col-md-12 text-center center-block" style="margin:60px 0px 30px 0px;"> 
				<input type="button"   class="btn btn-warning btn-bottom form-submit modelcreatePO"  id="UpdatePo" onclick="createModelPOPUP(this)" value="Revise PO" id="saveBtnId" ">
				<input class="btn btn-warning btn-bottom" type="button" value="Calculate" data-toggle="myModal"  id="calculateBtnId" onclick="calculateOtherCharges()">
			</div>

<%} else { %>










          <!-- *************************************************Update PO data********************************************************************************************* -->
	
	
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="container">
	
	  <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">	
	  <div class="col-md-12 border-indent">
	  <div class="col-md-4">
	    <div class="form-group">
			<label class="control-label col-md-6">Vendor Name : </label>
			<div class="col-md-6 col-xs-12" >
				<form:input path="vendorName" id="VendorNameId" class="form-control" data-toggle="tooltip" title="${poDetails.vendorName}" value="${poDetails.vendorName}" readonly="true" />
			<input type="hidden" name="isUpdated" id="isUpdated" value="true"/>
			</div>
			</div>
	  </div>
	 <div class="col-md-4">
	   <div class="form-group">
						<label class="control-label col-md-6">GSTIN : </label>
			<div class="col-md-6 col-xs-12">
				<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" value="${poDetails.strGSTINNumber}" autocomplete="off" readonly="true" />
			</div>
			</div>
	 </div>
	 <div class="col-md-4">
	   <div class="form-group">
			<label class="control-label col-md-6">Vendor Address : </label>
			<div class="col-md-6 col-xs-12">
				<form:input path="vendorAddress" id="VendorAddress" class="form-control" data-toggle="tooltip" title="${poDetails.vendorAddress}" value="${poDetails.vendorAddress}" readonly="true" />
			</div>
			
		</div>
	 </div>
	 <div class="col-md-4">
	   <div class="form-group">
			<label class="control-label col-md-6">Site Wise Indent No :</label>
			<div class="col-md-6 col-xs-12" >
				<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${poDetails.indentNo}">
				<input type="text" id="siteWiseIndentNo" name="siteWiseIndentNo" class="form-control" readonly="true" value="${poDetails.siteWiseIndentNo}">
				<input type="hidden" id="strIndentNo" name="strIndentNo" class="form-control" readonly="true" value="${poDetails.indentNo}">
				
			</div>
			</div>
	 </div>
	 <div class="col-md-4">
	 <div class="form-group">
	  <label class="control-label col-md-6">Site Name :</label>
			<div class="col-md-6">
				<input type="hidden" id=siteName" name="SiteName" class="form-control" readonly="true" value="${SiteName}">
			 <input type="text" id=SiteName" name="strSiteName" class="form-control" readonly="true" value="${poDetails.siteName}"> 
 <input type="hidden" id=url" name="url" class="form-control" readonly="true" value="${url}"> 
				<%-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}"> --%>
				<input type="hidden" id=toSiteId" name="toSite" class="form-control" readonly="true" value="${poDetails.site_Id}">
			</div>
	  </div>
	 </div>
	 <div class="col-md-4">
	   <div class="form-group">	 
			<label class="control-label col-md-6">PO NO : </label>
			<div class="col-md-6 col-xs-12" >
				<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off"  readonly="true" value="${poNumber}" data-toggle="tooltip" title="${poNumber}">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			<input type="hidden" id="strCreationDate"  name ="strCreationDate"  class="form-control" value="${poDetails.strCreateDate}"/>
			<input type="hidden" id="state"  name ="state"  class="form-control" value="${poDetails.state}"/>
			<input type="hidden" id="subject"  name ="subject"  class="form-control" value="<c:out value='${poDetails.subject}'/>"/>
			<input type="hidden" id="versionNumber"  name ="versionNumber"  class="form-control" value="${poDetails.versionNo}"/>
			<input type="hidden" id="refferenceNo"  name ="refferenceNo"  class="form-control" value="${poDetails.refferenceNo}"/>
			<input type="hidden" id="printRefferenceNo"  name ="printRefferenceNo"  class="form-control" value="${poDetails.strPoPrintRefdate}"/>
			<input type="hidden" id="preparedBy"  name ="preparedBy"  class="form-control" value="${poDetails.preparedBy}"/>
			<input type="hidden" id="ccmails"  name ="ccmails"  class="form-control" value="${ccEmails}"/>
			<input type="hidden" id="poEntryId"  name ="poEntryId"  class="form-control" value="${poDetails.poEntryId}"/>
			<input type="hidden" id="payment_Req_Days"  name ="payment_Req_Days"  class="form-control" value="${poDetails.payment_Req_days}"/>
			<input type="hidden" id="oldPODate"  name ="oldPODate"  class="form-control" value="${poDetails.poDate}"/>
			<input type="hidden" id="toSiteId" name="siteId" class="form-control" readonly="true" value="${poDetails.site_Id}">
			<input type="hidden" id="increase_Quantity"  name ="increase_Quantity"  class="form-control" value="${increase_Quantity}"/>
			<input type="hidden" id="decrease_Quantity"  name ="decrease_Quantity"  class="form-control" value="${decrease_Quantity}"/>
			<input type="hidden" id="updatePoEmpIds"  name ="updatePoEmpIds"  class="form-control" value="${updatePOEmpIds}"/>
			<input type="hidden" id="currentEmpId"  name ="currentEmpId"  class="form-control" value="${currentUsereId}"/>
			<input type="hidden" id="POTotalId" name="POTotal" class="form-control" readonly="true" value="${poDetails.finalamtdiv}"/>
			<input type="hidden" id="allSiteIds" name="allSiteIds" class="form-control" value="${Allsites}" />
			</div>
		</div>
	 </div>
	 <div class="col-md-4">
	   	<div class="form-group">
			<label class="control-label col-md-6">Delivery Date :</label>
			<div class="col-md-6 col-xs-12">
				<input type="text" id="deliveryDate123" name="deliveryDate" class="form-control" value="${poDetails.strDeliveryDate}" autocomplete="off" readonly="true">
			</div>
		</div>
	 </div>
		<div class="form-group">	
			<div class="col-md-6 col-xs-12">
				<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${poDetails.vendorId}" />
			</div>
		</div>
			  <div class="form-group">
			<div class="col-md-6" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" />
				<input type="hidden" id="strVendorId" name="strVendorId" class="form-control" value="${poDetails.vendorId}" />
			</div>
		</div>
	</div>
	</c:forEach>

</div>

	      <div class="clearfix"></div>
	       <div class="table-responsive">    
	   
				<table id="doInventoryTableId" class="table table-new fixedheader" style="width:3500px;">
				  <thead class="cal-thead-inwards">
				   <tr>
					<th>S NO</th>
    				<th>Product</th>
    				<th>Sub Product</th>
    				<th>Child Product</th>  
    				<th><%= vendorDescription %></th>  								
    				<th>Units Of Measurement</th>
    				<th>Quantity</th> 
    				<th> Price</th>
    				<th> BasicAmount</th>
    				<th>Discount</th>
    				<th>AmountAfterDiscount</th>
    				<th> Tax </th>
    				<th> HSNCode </th>
    				<th>Tax Amount</th>
    				<th>Amount After Tax</th>
    				<th> Other Or Transport Charges</th>
    				<th>TaxOn Other Or Transport Charges</th>
    				<th> Other Or Transport Charges After Tax </th>
    				<th> Total Amount </th>
					<!-- <th>Expire Date</th> -->
					<th>Actions</th>
					<!-- <th style="display:none">isNewOrOld</th> -->
				</tr>
				</thead>
				<tbody class="tbl-fixedheader-tbody">
				<input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
	        	 	
	        	 	
	        	 	<input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="indentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
	        	 		
	        	 	
	        	 	
	        	 	 <tr id="tr-class${GetProductDetails.serialno}" class="productRow">
	        	 	<td>	
	        	  						
				       <div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="product${GetProductDetails.serialno}" readonly="true" name="Product${GetProductDetails.serialno}" title="${GetProductDetails.productName}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop1 value="${GetProductDetails.productName}">
						<option value="${GetProductDetails.productId}$${GetProductDetails.productName}">${GetProductDetails.productName}</option>	
							<%-- <option value="">${GetProductDetails.product}</option> --%>
					    		<%-- <%			
					    		
					    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
					    			for(Map.Entry<String, String> prods : prod.entrySet()) {
									String prodIdAndName = prods.getKey()+"$"+prods.getValue();
								%>
									<option value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
					    		<% } %> --%>
						</select>
						
					</td>
					
					
					
					
					<td>
					<form:select path="" id="subProduct${GetProductDetails.serialno}"  readonly="true" name="SubProduct${GetProductDetails.serialno}" title="${GetProductDetails.sub_ProductName}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
					<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
					</form:select>
					</td>
					<td>
						<form:select path="" id="childProduct${GetProductDetails.serialno}" readonly="true" name="ChildProduct${GetProductDetails.serialno}" title="${GetProductDetails.child_ProductName}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}">
						<option value="${GetProductDetails.child_ProductId}$${GetProductDetails.child_ProductName}">${GetProductDetails.child_ProductName}</option>
					</form:select>
					</td>
					
					<td >
                        <input type="text" maxlength="500"  id="childProductVendorDesc${GetProductDetails.serialno}"  readonly="true" style="width: 160px;"  name="childProductVendorDesc${GetProductDetails.serialno}" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   title="<c:out value='${GetProductDetails.childProductCustDisc}'/> " class="form-control"  value="<c:out value='${GetProductDetails.childProductCustDisc}'/>"> 
					</td>
					<td>
					<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.serialno}" readonly="true" name="UnitsOfMeasurement${GetProductDetails.serialno}"  title="${GetProductDetails.measurementName}" class="form-control  btn-visibilty${GetProductDetails.serialno} btn-loop${GetProductDetails.serialno}"  value="${GetProductDetails.measurementName}">
						<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
					</form:select>
					</td>
					<td >
                    <input type="text"  id="stQantity${GetProductDetails.serialno}"    name="quantity${GetProductDetails.serialno}"  onfocusout="validateUpdatePOBOQQuantity(${GetProductDetails.serialno})" onkeypress="return isNumberCheck(this, event)" onchange="calculatequantitybase(${GetProductDetails.serialno},'Old')" onblur="return validateUnitsAndAvailability(this);"  <%-- onblur="calculateTotalAmount(${GetProductDetails.serialno})" --%>  class="form-control" value="${GetProductDetails.requiredQuantity}" /> <%-- onblur="calculateTotalAmount(${GetProductDetails.serialno})" --%>
					<input type="hidden"  id="stQuantity${GetProductDetails.serialno}"    name="strQuantity${GetProductDetails.serialno}" onchange="calculatequantitybase(${GetProductDetails.serialno},'Old')" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					<input type="hidden"  name="ReceivedQty${GetProductDetails.serialno}" id="ReceivedQty${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.recivedQty}"/> 
					<input type="hidden"  name="groupId${GetProductDetails.serialno}" id="groupId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.groupId}"/> 
					<input type="hidden"  name="oldQuantity${GetProductDetails.serialno}" id="oldQuantity${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.requiredQuantity}"/> 
					<input type="hidden" id="totalQuantity1" class="totalQuantity">
					 <input type="hidden" id="BOQQty1" class="BOQQty">
					</td>					
				
					<td>
					<input type="text" id="price${GetProductDetails.serialno}"  name ="PriceId${GetProductDetails.serialno}"   onblur="calculateTotalAmount(${GetProductDetails.serialno})"  class="form-control" value="${GetProductDetails.price}"/>
					</td>
					<td> 
					<input type="text" id="BasicAmountId${GetProductDetails.serialno}"  name ="BasicAmountId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.basicAmt}"/>
					</td>
					<td> 
					<input type="text" id="Discount${GetProductDetails.serialno}" name="Discount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(${GetProductDetails.serialno});" readonly="true" class="form-control disable-class1" value="${GetProductDetails.strDiscount}"/><i  style="position:absolute;right:18px;top:18px;" class="fa fa-percent"></i>

					
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
					    		<%-- <%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
					    		<% } %> --%>
						
						
						</form:select>
					</td>
					<td>
					<input type="text" id="hsnCode${GetProductDetails.serialno}"  name ="hsnCode${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
					<input type="hidden" id="oldhsnCode${GetProductDetails.serialno}"  name ="oldhsnCode${GetProductDetails.serialno}"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
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
					<input type="hidden"   class='ttamount form-control' id='strTotalAmount${GetProductDetails.serialno}'  name='strTotalAmount${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.totalAmount}"/>
					<input type = "hidden"  id="isNewOrOld${GetProductDetails.serialno}"  name="isNewOrOld${GetProductDetails.serialno}"  class="form-control"  autocomplete="off"  value="old" readonly="true"/>
					</td>	
					
					<td>
					 	<%-- <button type="button" name="addremoveItemBtn${GetProductDetails.serialno}" id="addremoveItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-remove"></i></button> --%>
					<!-- <button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>  -->
						<button type="button" name="editItemBtn${GetProductDetails.serialno}" value="Edit Item" id="editItem${GetProductDetails.serialno}" class="btnaction" onclick="editUpdatePoRow(${GetProductDetails.serialno})" ><i class="fa fa-pencil"></i></button> 
			 		<%-- <button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>  --%>
					<!-- <input type="hidden" name="addNewrowItemBtn${GetProductDetails.serialno}" id="addNewrowItemBtn${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"/> -->	
				
					</td>
					<td style="display:none">
					<input  type = "hidden" id="isNewOrOld${GetProductDetails.serialno}"  name="isNewOrOld${GetProductDetails.serialno}"  class="form-control  autocomplete="off"  value="old"/>
					</td>
					
				<!---	<td style="display:none">
					<input type="hidden"   id='indentCreationDetailsId${GetProductDetails.serialno}'  name='indentCreationDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.indentCreationDetailsId}"/>
					</td> -->
					
					<td style="display:none">
					    <input type="text"   id='poEntryDetailsId${GetProductDetails.serialno}'  name='poEntryDetailsId${GetProductDetails.serialno}'    readonly="true" class="form-control"  value="${GetProductDetails.poEntryDetailsId}"/>
					</td>	
					
					<td style="display:none">
					<input  type = "hidden" id="actionValueId${GetProductDetails.serialno}"  name="actionValue${GetProductDetails.serialno}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					</td> 
					
		<!-- 		<td style="display:none">
					<input  type = "text" id="eactionValueId"  name="eactionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
					</td>  
					
					<td style="display:none">
					<input  type = "text" id="ractionValueId"  name="ractionValue"  class="form-control  autocomplete="off" readonly="true" value=""/>
					</td>    -->
				</tr>
		
					
		 	</c:forEach> 
		 	</tbody>
	</table>
	</div>


			
			
						
	<div>			<%	List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("listOfGetProductDetails");
	String CurrntEmpId=request.getAttribute("currentUsereId") == null ? "" : request.getAttribute("currentUsereId").toString();
	String UpdateDPoempIds=request.getAttribute("updatePOEmpIds") == null ? "" : request.getAttribute("updatePOEmpIds").toString();
	
                     String hiddenFieldHtml1="<input type=\"hidden\" id=\"numberOfRows\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); %>  
			</div>
				<br><br><br>
							<div class="clearfix"></div><br/>
			<div class="table-responsive">
			<table id="doInventoryChargesTableId" class="table table-new fixedheader">
			<thead class="cal-thead-inwards">
				<tr>
					<th><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<th><%= trasportInvoice %></th>
     				<%if(UpdateDPoempIds.contains(CurrntEmpId)){ %>
    				<th><%= actions %></th> 
    			<%} %>
    			
				<!-- <label class="control-label col-sm-2 col-xs-12">Invoice Date : </label> -->
			<%-- <div class="col-sm-3 col-xs-12" >
				<form:input path="InvoiceDate" id="InvoiceDateId" class="form-control"/>
			</div > --%>
    			 
  				</tr>
  			</thead>
  			<tbody class="tbl-fixedheader-tbody">
			<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
				<tr class="chargesRow" id="trans-tr-class${GetTransportDetails.strSerialNumber}">
					<td>						
						<div id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
					</td>
					<td>						
							<form:select path="" id="Conveyance${GetTransportDetails.strSerialNumber}" readonly="true" name="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" >
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
						<form:input path="" id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" readonly="true" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="number"  placeholder="Please enter Amount" value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass" autocomplete="off" onkeyup="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})"/>
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
						<form:input path=""  type="hidden" id="poTransChrgsDtlsSeqNo${GetTransportDetails.strSerialNumber}" name="poTransChrgsDtlsSeqNo${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number" readonly="true" value="${GetTransportDetails.poTransChrgsDtlsSeqNo}" onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
						<input  type = "hidden" id="transactionActionValue${GetTransportDetails.strSerialNumber}"  name="transactionActionValue${GetTransportDetails.strSerialNumber}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					
					</td>
					<% if(UpdateDPoempIds.contains(CurrntEmpId)){ %>
					 <td>
						<button type="button" name="addremoveItemBtn${GetTransportDetails.strSerialNumber}" id="addremoveChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="removeTransRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
						 <button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId${GetTransportDetails.strSerialNumber}' onclick="appendChargesRow(${GetTransportDetails.strSerialNumber})" class="btnaction "><i class="fa fa-plus"></i></button> 
					<!-- <button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="deleteRow(this, ${GetTransportDetails.strSerialNumber})" ><i class="fa fa-trash"></i></button> -->	
						<button type="button" name="editchargesItemBtn${GetTransportDetails.strSerialNumber}" value="Edit Item" id="editchargesItem${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="editchargesInvoiceRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button>
					</td>
					<%} %>
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
			
								
	<!-- ***********************************************this is for pdf file download start******************************************************** -->
							<div class="col-md-12" style="margin-top: 10px;">
								<!-- 	<h3>You can see the PDF</h3> -->
								<%
									int pdfcount = Integer.parseInt(String.valueOf(request
													.getAttribute("pdfcount")));
											if (pdfcount == 0) {
								%>
								<!-- <h3>No PDF</h3> -->
								<%
									} else {
								%>
								<h3 style="">You can see the PDF(s) below :</h3>
								<%
									}
											for (int i = 0; i < pdfcount; i++) {
												String pdfName = "pdf" + i;
												String PathdeletePdf = "PathdeletePdf" + i;
												log(pdfName);
								%>
								<c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
								<c:set value="<%=PathdeletePdf %>" var="deletePdf">
								</c:set>
								<%
									if (request.getAttribute(pdfName) != null) {
								%>
								<div class="col-md-3 pdfcount pdf-delete<%=i%>"
									id="pdfDivHideShow<%=pdfName%>">
									<div class="pdf-cls"
										style="margin-bottom: 15px; cursor: pointer;">
										<iframe class="iframe-pdf filesCount"
											src="${requestScope[pdfBase64]}" allow="fullscreen"
											style="height: 200px; width: 100%; border: 1px solid #000;"></iframe>
										<div class="middle" data-toggle="modal"
											data-target="#myModalpdf<%=i%>"></div>
										<input type="hidden" name="imagePath" id="image_Path"
											value="${requestScope[deletePdf]}" />
										<p class="btn-downloaddelete">
											<a class="btn btn-success btn-xs" download
												onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i
												class="fa fa-download"></i> Download</a>
											<button class="btn btn-danger btn-xs" id="deletePdf"
												onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')">
												<i class="fa fa-close"></i>Delete
											</button>
										</p>
									</div>
								</div>
								<%
									}
								%>
								<%
									}
								%>
							</div>

			<!-- ***********************************************this is for pdf download end************************************************** -->		
				<div class="col-md-12 Mrgtop20">
						
				<!-- <h3>You can see the Images</h3> -->
				<%
				int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
				//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
				if (imagecount == 0) {
				%>
				<!-- 	<h3>No Images</h3> -->
				<%
				} else {
				%>
				<h3 style="">You can see the Images below :</h3>
				<%
				}
				for (int i = 0; i < imagecount; i++) {
					 String imageB64 = "image" + i;
					 String deleteB64 = "delete" + i;
					 log(imageB64);
					 out.print("<div>");
					 %>
					<c:set value="<%=imageB64 %>" var="index"></c:set>
					<c:set value="<%=deleteB64 %>" var="delete"></c:set>
					<%
					//	if (i == 0) {
				if (request.getAttribute(imageB64) != null) {
					%>
					<div class='col-md-4 Mrgtop20' id='imageDivHideShow<%=imageB64%>'>
					<div class="container-1"  >
					<img class="img-responsive img-table-getinvoice filesCount"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" style="cursor:pointer;"/>
					<div class="middle-1">
					<div class="columns download">
					<p>
					<a class="btn btn-dwn btn-success btn-xs"  download onclick="toDataURL('${requestScope[index]}',this)" ><i class="fa fa-download"></i>&nbsp;Download</a>
					<button onclick="deleteWOImageFile('<%=imageB64%>','${requestScope[delete]}')" type="button" class="btn btn-dwn btn-danger btn-xs"><i class="fa fa-remove"></i> &nbsp;Delete</button>
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
				<%
				}
				%>
				<%
				int totalValue = imagecount + pdfcount;
											%>
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
															 </div>
															 <input type="file" id="file" name="file" style="display:none">
															</div>
 													</div>
												</div>
							         </div>
							
						<!-- model popup for pdf start  -->
						<%
							for (int i = 0; i < pdfcount; i++) {
										String pdfName = "pdf" + i;
										String PathdeletePdf = "PathdeletePdf" + i;
										log(pdfName);
						%>
					   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
					   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
					 <%
					 	if (request.getAttribute(pdfName) != null) {
					 %>
								<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
					  <div class="modal-dialog modal-lg-width">
					
					    <!-- Modal content-->
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal">&times;</button>
					        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i + 1%></strong></h4>
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
						     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						     
						     <%-- <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')" data-dismiss="modal">Delete</button> --%>
						   
						   </p>
					      </div>
					    </div>
					
					  </div>
					</div>
					<%
						}
					%>
					<%
						}
					%>
					
					
					<!-- pdf model popup end  -->
						
						
					<!-- modal popup for image pop start-->
					<!-- Modal -->
						 <!-- Modal -->
						<%
							imagecount = Integer.parseInt(String.valueOf(request
											.getAttribute("imagecount")));
									for (int i = 0; i < imagecount; i++) {
										String index = "image" + i;
										log(index);
						%>
						  <div class="modal fade custmodal" id="uploadinvoice-img<%=i%>" role="dialog">
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
					          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
					        </div>
					      </div>
					      
					    </div>
					  </div>
					  <%
					  	}
					  %>
	<div id="allImgPdfUrlToDeleteFile" ></div>
				
				
			<div class="container">
			
					<div class=""><span class="h4" style="margin-top:20px; margin-left: 71%;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv"  ></span></div>
				<input type="hidden" id="${GetProductDetails.serialno}" />
				</div> <br><br>
				<br><br><br><br>
				<!-- <div class="col-sm-3 pt-10">
					<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>   -->  
				<div class="col-md-12 text-center center-block"> 
					<input type="button"  class="btn btn-warning btn-bottom form-submit modelcreatePO" onclick="createModelPOPUP(this)"  value="Update PO" id="saveBtnId" "> <!-- data-target="myModal1" -->
				
						<input class="btn btn-warning btn-bottom"  type="button" value="Calculate" data-toggle="myModal1"  id="calculateBtnId" onclick="calculateOtherCharges()">
					<!-- </div> -->
				</div>
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
	
	
	<!-- ***********************************************this is for pdf file download start******************************************************** -->					
			<%-- <div class="col-md-12" style="margin-top: 100px;">
<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("count"))); 
//String po_Number=request.getAttribute("poNumber").toString();
//String strPO_Number=po_Number.replace('/','$');
  		for(int i=0;i<pdfcount;i++)
			{
			%>
 		
 		 <%if(i==0){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf0']}" allow="fullscreen" style="height:200px;position:relative;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <%} if(i==1){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
 <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf1']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf2"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <% } if(i==2){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf2']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf3"><i class="fa fa-close"></i></button>
	
	
  </div>
</div>

 </div>
 <% }if(i==3){ %>
 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf3']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf4"><i class="fa fa-close"></i></button>
	
  </div>
</div>
 </div>
 <%} %>

  
 		
 		<%} %>
 		</div> --%>
 
 		
 		
	<!-- ***********************************************this is for pdf download end************************************************** -->		
						

<!-- **************************************************************		 -->
			
		
			<br/>
 <div class="modal fade" id="myModal1" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">CC MAILS</h4>
        </div>
        <form action="" method="post">
        <div class="modal-body">
        <div class="container">
	<div class="row">
        <div class="control-group" id="fields">
        <div class="controls" id="profs"> 
           
     <div class="col-md-12 appen-div-workorder hidden">
     
      <c:forEach items="${listTermsAndCondition}" var="item">
      <div class="col-md-12 remove-filed">
      <div class="col-md-10">
      
      <input type="text" name="termsAndCond" class="form-control workorder_modal_text myinputstyles" value="<c:out value='${item.strTermsConditionName}'/>" />
      </div>
      <div class="col-md-2 margin-close">
      <button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
      </div>
     
      </div>
      </c:forEach> 
      <c:forEach items="${conclusions}" var="conclusion"> 
		<div class="">
		<div class="hideInprintCls">
		<div class="col-md-10"  style="margin-top: 10px;">
		<input type="text" class="form-control" value="${conclusion}" name="conclusionDesc">
		</div>
		<div class="col-md-2 addConclusionBtnDiv">
		<button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger remove-button"><i class="fa fa-remove"></i></button>
		</div>
		</div>
		</div>
		</c:forEach>
      </div>
         <!-- <div class="col-md-12 padding-div">
       <div class="input_fields_wrap">
         <div class="col-md-10"><input type="text" class="form-control workorder_modal_text myinputstyles" id="workorder_modal_text1" name="termsAndCond" value=""/></div>
         <div class="col-md-2 margin-close"><button type="button" class="btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
        
      </div>
    </div> -->
    
           <br>
         
            
            
            </div>
        </div>
	</div>
</div>
        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
        <div class="emailCC">
        	<%-- <input type="checkbox" name="check1" id="ccemailcheckbox" onclick="dynInput(this);" style="position: absolute;margin-left: -522px;" value="${ccEmails}" /> --%><input type="text" id="" name="ccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/><span style="position: absolute;margin-left: -525px;">(Optional)If you want to add CC In emails.</span><br>
     		<input type="hidden" id="" name="oldccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/>
        </div>
        <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
        	  <button type="button" class="btn btn-warning  form-submit"  data-dismiss="modal" onclick="updatePo()">Submit</button>
       	</div>
        </div>
      </div>
      </form>
      
    </div>
  </div>
	</div>	
</form:form>
<%-- 	<div id="myModalpdf" class="modal fade" role="dialog">
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
	    
	   </p>
      </div>
    </div>

  </div>
</div>	 --%>
		<%
			}
		%>
	
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
        <script src="js/ShowPODetailsToUpdate.js" type="text/javascript"></script>
        <script src="js/numberCheck.js" type="text/javascript"></script>
		
		
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
			     //$("#Conveyance"+i).attr("disabled", true).css('cursor','not-allowed');
			    }
			
		});
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			/* $(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
		
		/* 
		 	$('#Conveyance1').change(function() {
				if ($(this).val() == "999$None"){
				  // Updating text input based on selected value
				  $(".noneClass").val(0);
				  $(".GSTClass").val( '1$0%');
				  }else{
					  $(".noneClass").val( " ");
				  }
				});  */
				/* $('#Conveyance1').change(function() {
					debugger;
					if($(this).is(':readonly'))
				     {
				       alert("read only");
				     }
				});  */ 
			 	
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
	$('#days'). bind('copy paste cut',function(e) {
		e.preventDefault();
	});
    var next = parseInt($("#countOftermsandCondsfeilds").val());
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
	
function removeme(rowId){alert('');
	$("#termsAndCond"+rowId).remove();
	$('.remove-me'+rowId).hide();
};
function removeRow(rowId){debugger;
var CanIDelete=window.confirm("Do you want to delete?");
if(CanIDelete==false){
	return false;
}
debugger;
var n=0;
$(".productRow").each(function(){debugger;
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

function removeTransRow(rowId){debugger;
var canIdelete=window.confirm("Do you want to delete?");
if(canIdelete==false){
	return false;
}
var n=0;
$(".chargesRow").each(function(){debugger;
	var id=$(this).attr("id").split("trans-tr-class")[1];
	if($("#transactionActionValue"+id).val()!="R"){
		n++;
	}
});

if(n==1){
	alert("This row can't be deleted.");
	return false;
}
/*	document.getElementById("ractionValueId"+rowId).value = "R";*/

$("#trans-tr-class"+rowId).addClass('strikeout');
$("#addremoveChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editchargesItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#addNewChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoChargesDivId'+rowId).removeAttr('id');	
$('#transactionActionValue'+rowId).val("R");

}
/* *******************************************************************terms and conditions******************************************************************* */

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
	    	alert("please enter terms and condition.");
	    	return false;
	    }
	    
	        if(x < max_fields){ //max input box allowed
	            x++; //text box increment
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-10" style="margin-top: 10px;"><input type="text" name="termsAndCond" value="'+tc+'" id="termsAndCond'+x+'" class="form-control"/></div><div class="col-md-2 margin-close"><button type="button" class="btn btn-danger btncls remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#workorder_modal_text1").focus();
	        $("#termsAndCond"+x).val(tc);
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	    	var caniDelete=window.confirm("Do you want to delete?");
	    	if(caniDelete==false){
	    		return false;
	    	}
	       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
	       
	    })
	     $(wrapper1).on("click",".remove_field", function(e){ //user click on remove text
	    	 var caniDelete=window.confirm("Do you want to delete?");
		    	if(caniDelete==false){
		    		return false;
		    	}
	       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
	    })
	});
	
	


/* ***********************************************************************terms and conditions end*************************************************************** */
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
 	
 	/*delete functionality for pdf*/
 	
 	var n=0;
 	function deletepdf(rowId){
 			debugger;
 		$('.pdf-delete'+rowId).hide();
 		$("#hiddenRowNum").val(rowId);
 		$("#deletePdf"+rowId).val(rowId);
 		$("#file"+rowId).show();
 		/*  $.ajax(
 				{ url : "deletePoFile.spring?po_Number="+po_Number+"&imgNumber="+rowId,
		  type : "post",
		 dataType : "json",
	 	// contentType:"xml",
		  success : function(response) {
			  alert(response);
			  console.log("res: "+response);
			  if(response=="success"){
				  $("#file1").show();
			  }
			  window.location.reload();
			}

});  */
 	
 	}
		
	$('.file1').change(function(){
	var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close1').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile1();}
	});
	
	$('.btn-close1').change(function(){
		$('.btn-close1').hide();
	});
	
	$('.file2').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close2').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile2();}
	});
	$('.btn-close2').change(function(){
		$('.btn-close2').hide();
	});
	$('.file3').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close3').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile3();}
	});
	$('.btn-close3').change(function(){
		$('.btn-close3').hide();
	});
	$('.file4').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close4').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile4();}
	});
	$('.btn-close4').change(function(){
		$('.btn-close4').hide();
	});
	$('.file5').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close5').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile5();}
		
	});
	
	$('.btn-close5').change(function(){
		$('.btn-close5').hide();
	});
	$('.file6').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close6').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile6();}
		
	});
	
	$('.btn-close6').change(function(){
		var	kilobyte=$("#kilobyte").val();
		$('.btn-close6').hide();
	});
	$('.file7').change(function(){
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close7').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile7();}
		
	});
	
	$('.btn-close7').change(function(){
		
		$('.btn-close7').hide();
	});
	$('.file8').change(function(){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)<=kilobyte){
			$('.btn-close8').show();
			}else{
				alert("Please upload below 1 mb PDF file.");
				resetFile8();}
		
	});
	
	$('.btn-close8').change(function(){
		$('.btn-close8').hide();
	});
	function resetFile1() {
		  const file = document.querySelector('.file1');
		  file.value = '';
		  $('.btn-close1').hide();
		 
		}
	function resetFile2() {
		  const file = document.querySelector('.file2');
		  file.value = '';
		  $('.btn-close2').hide();
		}
	function resetFile3() {
		  const file = document.querySelector('.file3');
		  file.value = '';
		  $('.btn-close3').hide();
		}
	function resetFile4() {
		  const file = document.querySelector('.file4');
		  file.value = '';
		  $('.btn-close4').hide();
		}
 	
	function resetFile5() {
		  const file = document.querySelector('.file5');
		  file.value = '';
		  $('.btn-close5').hide();
		}
	function resetFile6() {
		  const file = document.querySelector('.file6');
		  file.value = '';
		  $('.btn-close6').hide();
		}
	function resetFile7() {
		  const file = document.querySelector('.file7');
		  file.value = '';
		  $('.btn-close7').hide();
		}
	function resetFile8() {
		  const file = document.querySelector('.file8');
		  file.value = '';
		  $('.btn-close8').hide();
		}
	
	/*delete functionality for pdf*/
	 	$(function() {
		$(".deliveryDate").datepicker({
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
				alert("Please enter conclusion.");
				$("#conclusionId1").focus();
				return false;
			}
			var lastTextboxId=$(".conclusionMainDivCls").attr("id").split("conclusionMainDiv")[1];
			id++;
			var addConclusionTxtBx=	'<div class="conclusionMainDivCls" id="conclusionMainDiv'+id+'"><div class="hideInprintCls"><div class="col-md-10"  style="margin-top: 10px;"><input type="text" class="form-control conclusiontxt" id="conclusionId'+id+'" name="conclusionDesc"></div><div class="col-md-2 addConclusionBtnDiv margin-close"><button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt(this)" class="btn btn-danger btncls remove-button"><i class="fa fa-remove"></i></button></div></div></div>';
			$("#addconclusiontxt").append(addConclusionTxtBx);
			
			$("#conclusionId"+id).val($("#conclusionId1").val());
			$("#conclusionId1").val('');	
			$("#conclusionId1").focus();		
		}
		//deleteting conclusion row written by thirupathi
		function deleteConclusionTxt(id){debugger;
			var canIDelete=window.confirm("Do you want to delete conclusion?");
			if(canIDelete==false){
				return false;
			}			
			$(id).parents(".hideInprintCls").remove();			
		}
		function isNumberCheckForDays(el, evt) {
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
			        	/* if((this.files[0].size/kilobyte)<=kilobyte && (size_file.type)=='application/pdf'){
						}else{
							alert("Please upload below 1 MB PDF file");
							//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
							$(this).val('');
							$(this).focus();
							count++;
						 return false;
						} */
			        	
				   /*      if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
					        ext= fileName.substring(fileName.lastIndexOf(".")+1); */
				   
				        if (!(regex.test(fileName))) {
				            $(this).val('');
				            alert('Please select correct file format.');
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
					 var canISubmit = window.confirm("Do you want to delete PDF?");
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
					    result = confirm("Do you want to update the temp WO");
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
		 alert("Please select file");
		 $("#file_select"+classLastId).focus();
		 return false;
	  }
	  if(k > 7){
		alert("Unable to upload more than eight files.");
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
				alert("Uploaded file size("+file_size+ "), So Please upload Below 1 mb.");
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
		
		function uploadFile(){debugger;
			var count=$(".filesCount").length;
			var classlength=$(".selectCount").length;
			 if((classlength+count)>7){
				alert("Unable to upload more than eight files");
				return false;
			}
			addFile();
		    var id=$(".selectCount:last").attr("id").split("file_select")[1];
			$("#file_select"+id).trigger("click");		
		}
	  
		function start_and_end(str) {
			if (str.length > 25) {
			   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
			}
		  return str;
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
