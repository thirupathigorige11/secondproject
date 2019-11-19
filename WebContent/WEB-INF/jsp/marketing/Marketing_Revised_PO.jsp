<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
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
	String vendorDescription=resource.getString("label.vendorDescription");
	String discount = resource.getString("label.discount");	
	String AmountAfterDiscount = resource.getString("label.AmountAfterDiscount");	
	String isNewOrOld = resource.getString("label.isNewOrOld");
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
<style>
/* css for market inwards */
.form-control {height: 34px !important;}
.padding0{padding-left:0px !important;}
.padding0 select{margin-bottom:15px;height:34px;}
.text-center-left{text-align:center;}
@media only screen and (min-width:320px) and (max-width:767px){.text-center-left{text-align:left;}}
.selectsingleSite{padding-left:0px !important;margin-bottom:15px;font-weight:bold;color:#000;}
.selectsingleSite .form-control{height:34px;font-weight: bold;color: #000;}
.anchor-market{color:#0000ff;font-size:15px;}
.anchor-market:hover{color:#0000ff;font-size:15px;text-decoration:underline;}
.radio_Market{font-size:15px;font-weight:bold;margin-top:15px;margin-bottom:15px;}
.radio_Market input{ height: 17px;width: 23px;vertical-align: text-bottom;}
.totalMarketingAmount td{background:#bfd9e5;text-align:right;font-size:13px;font-weight:bold;}
/* css for market inwards */
@media only screen and (min-width:320px) and (max-width:767px){#FieldLocation_durationtable{width:1500px;}}
.remove-filed{margin-bottom:10px;}
.select_Brand{padding-left:0px !important;margin-bottom:15px;}
.select_Brand select{height:34px;font-weight:bold;}
.modal-header{background-color:#ccc;color:#000;border-radius:5px;}
.modal-title{font-weight:bold;font-size:18px;}
.emailCC{margin-top:15px;}
.btnaction {padding: 6px 12px;margin-left: 10px;}
.Location_btn_table{margin-top:15px;margin-bottom:15px;padding-left:0px !important;padding-right:0px !important;}
.Location_btn_table a{padding-left:0px;font-size:15px;color:#0000ff;}
.Location_btn_table a:hover{padding-left:0px;font-size:15px;color:#0000ff;text-decoration:underline;}
.table_hide_show{margin-top:15px;display:none;}  /*display:none; */
.breadcrumb {background: #eaeaea !important;}
.market_btn_style{margin-top:20px;margin-bottom:15px !important;}
.market_file_style{margin-top:10px;margin-bottom:0px;}
.files_place{margin-bottom:10px;}
.tbl-width-fix tbody tr td .ui-icon-triangle-1-s {
    display: inline-block;
    width: 0;
    height: 0;
    margin-left: -4px;
    margin-top: -3px;
    vertical-align: middle;
    border-top: 4px dashed;
    border-top: 4px solid\9;
    border-right: 4px solid transparent;
    border-left: 4px solid transparent;
}
.file-left{float:left;margin-bottom:20px;}
.tbl-width-fix1{width:1340px !important;table-layout: fixed;}
.tblFixedHeaderBody {
    display: block;
    max-height: 300px;
    overflow-y: scroll;
    overflow-x: auto;
}
 .fixedTblHeader thead, .fixedTblHeader tbody tr {
    table-layout: fixed;
    display: table;
    width: 100%;
}
.custom-combobox > input {width:100%;height:30px;height: 30px;
    border-radius: 3px;
    border: 1px solid #ccc;
    padding: 5px;}
   
     .tblSlno{
    width:50px;}
  
   .productchargesrow>td:first-child {width:50px;}
   .chargesrow>td:first-child{width:50px;}
.calTheadColor {
    color: #000;
    background: #ccc;
    width: calc( 100% - 17px ) !important; 
}
.tbl-width-fix {
    width: 4300px !important;
    table-layout:fixed;
    border-collapse:collapse;
}
@media only screen and (max-width:425px) and (min-width:320px){
.calTheadColor {
    color: #000;
    background: #ccc;
    width: calc( 100% - 0px ) !important; 
}
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
.btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
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
</style>
<style>
.table>thead>tr>th {
border-bottom:1px solid #000 !important;
border-top:1px solid #000 !important;
}
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
/* added by thiru */
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
	 .margin-close{
	 margin-top:5px;
	 }
	 .black{
	  color:black;
	 font-size:14px;
	 font-weight:bold;
	 }
	 .control-text{
	 border:1px solid #ddd !important;
	 }
    .Mrgtop30{
	margin-top:30px;
}
.spanMainDiv{
	width: 235px;
    border: 1px solid #000;
    overflow: hidden;
    padding: 5px;
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
<!-- Modal popup for add remove -->    
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		
		<script src="js/numberCheck.js" type="text/javascript"></script>
		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<script src="js/momment.js"></script>
		<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
		<script src="js/sweetalert2.js" type="text/javascript"></script>
		<jsp:include page="../CacheClear.jsp" />  
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	.hide{
		display: none !important;
	}
	.percentageSymbol{
		position: absolute;
		right: 17px;
        top: 17px;
	}
	.imageRot {
	   -webkit-animation:spin 1s ease-in-out;
	    -moz-animation:spin 1s ease-in-out;
	    animation:spin 1s ease-in-out;
	}
	@-moz-keyframes spin {
	    100% { -webkit-transform: rotate(360deg); transform:rotate(360deg); }
	}
	@-webkit-keyframes spin {
	    100% { -webkit-transform: rotate(360deg); transform:rotate(360deg); }
	}
	@keyframes spin { 
	    100% { -webkit-transform: rotate(360deg); transform:rotate(360deg); } 
	}
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
    		var dynamicSelectBoxId = "product"+vfx;
    		//alert(dynamicSelectBoxId); 
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId); 
    	    div.setAttribute("class","form-control"); 
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
    		var dynamicSelectBoxId = "subProduct"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class","form-control"); 
    	    div.setAttribute("style", ""); 
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();    	        
			});    	    
    	}
    	else if(tableColumnName == childProductColumn) {
    		var dynamicSelectBoxId = "childProduct"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class","form-control hiddenchild"); 
    	   // div.setAttribute("class", "hiddenchild"); 
    	    
    	    
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
		   /*  div.setAttribute("id", tableColumnName+vfx);	    */ 
		  /*   div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
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
    		
    		//cell.className  = "s-50";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return isNumberCheck(this, event)");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("autocomplete", "off"); 
		    div.setAttribute("class", 'form-control pasteDisable');			    
		    cell.appendChild(div);		    
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", "ReceivedQty"+vfx);
		    div2.setAttribute("id", "ReceivedQty"+vfx);
		    cell.appendChild(div2);
		    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return isNumberCheck(this, event)");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("autocomplete", "off"); 
		    div.setAttribute("class", 'form-control pasteDisable');			 
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);	
		    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculatePriceAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'readonly');
		    div.setAttribute("style", "");  
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		 	div.setAttribute("autocomplete", "off"); 
		    div.setAttribute("onkeypress", "return isNumberCheck(this, event)");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control pasteDisable');
		    cell.appendChild(div);
		   var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-percent percentageSymbol");
		    cell.appendChild(btn);
		    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		     div.setAttribute("name", "amtAfterDiscount"+vfx);
		    div.setAttribute("id", "amtAfterDiscount"+vfx);
		    div.setAttribute("readonly", "readonly");
		 /*    div.setAttribute("id", "tax"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control' );
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
   		/* } */
    	/* else if(tableColumnName == taxOnOtherOrTransportChargesColumn) { */
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", taxOnOtherOrTransportChargesColumn+vfx);
		    div.setAttribute("id", taxOnOtherOrTransportChargesColumn+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", "display:none;"); 
		    cell.appendChild(div);
   		/* } */
    	/* else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) { */
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", otherOrTransportChargesAfterTaxColumn+vfx);
		    div.setAttribute("id", otherOrTransportChargesAfterTaxColumn+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", "display:none;"); 
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
		    
		    
 			//new or old
		    
		    var div = document.createElement("input");
			//	div.setAttributesetAttribute("hidden", true);
			 //   div.setAttribute("td", "display: none;");
			    div.setAttribute("style", "display: none;");  
			    div.setAttribute("name", "isNewOrOld"+vfx);
			    div.setAttribute("id", "isNewOrOld");
			    div.setAttribute("value", "new");
			    div.setAttribute("class", 'form-control');
			  // div.setAttribute("style", 'display:none');
			    cell.appendChild(div);	
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("maxlength", "20"); 
		    cell.appendChild(div);
		    
		    var div1 = document.createElement("input");
		    div1.setAttribute("type", "hidden");
		    div1.setAttribute("name", "actionValueId"+vfx);
		    div1.setAttribute("id", "actionValueId"+vfx);
		    div1.setAttribute("value", "N");
		    cell.appendChild(div1);	
		    
   		}
    	/* else if(tableColumnName == isNewOrOldColumn) {
			var div = document.createElement("input");
		//	div.setAttributesetAttribute("hidden", true);
		 //   div.setAttribute("td", "display: none;");
		      div.setAttribute("style", "display: none;");  
		    div.setAttribute("name", "isNewOrOld"+vfx);
		    div.setAttribute("id", "isNewOrOld");
		    div.setAttribute("value", "new");
		    div.setAttribute("class", 'form-control');
		  // div.setAttribute("style", 'display:none');
		    cell.appendChild(div);			    
   		} */
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
		    div.setAttribute("class", 'form-control pasteDisable');
		    div.setAttribute("onfocusout", "calculateGSTTaxAmount("+vfx+")");
		    cell.appendChild(div);
		    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
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
		    div1.setAttribute("name", "transactionActionValue"+vfx);
		    div1.setAttribute("id", "transactionActionValue"+vfx);
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

/* ********************************************************** */

</script>
</head>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
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
							<li class="breadcrumb-item active">Create PO</li>
						</ol>
					</div>
					<div>
	<div class="col-md-12">
			 <!--  <div class="loader-ims" id="loaderId" style="display:none;"> 
		         <div class="lds-ims">
			       <div></div><div></div><div></div><div></div><div></div><div></div></div>
		        <div id="loadingimsMessage">Loading...</div>
	           </div> -->
	            <div class="overlay_ims" style="display: none;" ></div>
			  <div class="loader-ims" id="loaderId" style="display: none;">
					<div class="lds-ims">
						<div></div><div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingimsMessage">Loading...</div>
			  </div>
	  <form:form modelAttribute="CreatePOModelForm" id="doInventoryFormId" class="form-horizontal" enctype="multipart/form-data">
		  
		   <div class="clearfix"></div>
		   <div class="border-indent"> <!-- container -->
		    <c:forEach var="poDetails" items="${requestScope['poDetails']}" step="1" begin="0">
			<div class="col-md-12">
				  <div class="col-md-4">
				   <div class="form-group">
						<label class="control-label col-md-6">Vendor Name : </label>
						<div class="col-md-6" >
							<form:input path="vendorName" id="VendorNameId" class="form-control" data-toggle="tooltip" title="${poDetails.vendorName}" value="${poDetails.vendorName}" readonly="true" />
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
					  <label class="control-label col-md-6 col-xs-12">Delivery Date: </label>
						<div class="col-md-6 col-xs-12 input-group" >
							<form:input path="" id="deliveryDate" name="deliveryDate" placeholder="Delivery Date" class="form-control readonly-color"   value="${poDetails.strDeliveryDate}" autocomplete="off" readonly="true"/>
							<label class="input-group-addon btn input-group-addon-border" for="deliveryDate">	
							<span class="fa fa-calendar"></span></label>
						<input type="hidden" id="olddeliveryDate" name="olddeliveryDate" class="form-control deliveryDate" value="${poDetails.strDeliveryDate}" autocomplete="off">	
						</div>
					 </div>
				</div>
				<div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">PO To: </label>
						<div class="col-md-6" >
							<select class="form-control" id="poToId" name="poTo">
							   <option value="${poDetails.type_Of_Po}">${poDetails.type_Of_Po}</option>
							   <option value="SiteWise">Site Wise</option>
							   <option value="LocationWise">Location Wise</option>
							   <option value="BrandingWise">Branding Wise</option>
							</select>
						</div>
					 </div>
				</div>
				
				<div class="col-md-4 class_selectsite" style="display:none;">
					 <div class="form-group">
					   <label class="control-labe col-md-6">Site Name :</label>
					   <div class="col-md-6 autocomplete" style="">
                           <input id="siteNameId" type="text"  name="site" onkeyup="populateSite(this)" value="${poDetails.type_Of_Po_Details}" autocomplete="off" class="form-control" placeholder="Enter Site Name" onchange="siteNameChange()">
                       <input type="hidden" id="siteName" name="SiteName" class="form-control" readonly="true" value="${SiteName}"/>
				 <input type="hidden" id=url" name="url" class="form-control" readonly="true" value="${url}"> 
				<input type="hidden" id="toSite" name="toSiteId" class="form-control" readonly="true" value="${poDetails.site_Id}"/>
				<input type="hidden" id="POTotalId" name="POTotal" class="form-control" readonly="true" value="${poDetails.finalamtdiv}"/>
                       </div>
					 </div>
				</div>
				<div class="col-md-4 class_selectsite1" style="display:none;">
					 <div class="form-group">
					  <label class="control-label col-md-6">Select Site: </label>
						<div class="col-md-6" >
							<select name="siteNames" id="site" class="form-control" onchange="siteLocationChange('state')">
							<option value="${poDetails.type_Of_Po_Details}">${poDetails.type_Of_Po_Details}</option>
							 <option value="">Select</option>
							<c:forEach items="${siteLocationDetails}" var="siteLocationDetails">
								<option value="${siteLocationDetails}">${siteLocationDetails}</option>									
							</c:forEach> 
							
							</select>
						</div>
					 </div>
				</div>
				<div class="clearfix"></div>
				<div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">Payment Req Days: </label>
						<div class="col-md-6" >
							<input type="text" id="days" name="days" class="form-control " value="${poDetails.payment_Req_days}" onkeypress="return isNumberCheckForDays(this, event)" autocomplete="off">
						<input type="hidden" id="olddays" name="olddays" class="form-control " value="${poDetails.payment_Req_days}" onkeypress="return isNumberCheckForDays(this, event)" >
						</div>
					 </div>
				</div>
				<div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6 col-xs-12">PO NO : </label>
						<div class="col-md-6 col-xs-12 input-group" >
							<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off"  readonly="true" value="${poNumber}" data-toggle="tooltip" title="${poNumber}" style="z-index: 0;">
							<c:set var = "po_Number" scope = "session" value = "${poNumber}"/>
						</div>
					 </div>
				</div>
				<div class="col-md-6 col-xs-12" >
						<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${poDetails.vendorId}" />
					<input type="hidden" id="poEntryId"  name ="poEntryId"  class="form-control" value="${poDetails.poEntryId}"/>
					<input type="hidden" id="oldPODate"  name ="oldPODate"  class="form-control" value="${poDetails.poDate}"/>
					<input type="hidden" id="toSiteId" name="siteId" class="form-control" readonly="true" value="${poDetails.site_Id}"/>
					 <input type="hidden" id=SiteName" name="strSiteName" class="form-control" readonly="true" value="${poDetails.siteName}"> 
			     </div>
			</div>
			</c:forEach>
	</div>

		<!-- ******************************************** -->
	<div class="Mrgtop30"><h4><strong>Products Table :</strong></h4></div>	
	<div class="table-responsive  sticky-table sticky-headers sticky-ltr-cells"> <!-- protbldiv -->
		<table id="doInventoryTableId" class="table fixedTblHeader tbl-width-fix">
			<thead class="calTheadColor">
				<tr >
					<th class="tblSlno"><%= serialNumber %></th>
    				<th><%= product %></th>
    				<th><%= subProduct %></th>
    				<th><%= childProduct %></th>   
    				<th><%= vendorDescription %></th>  						 								
    				<th><%= measurement %></th>
    				<th><%= quantity %></th> 
    				<th><%= price %></th>
    				<th><%= basicAmount %></th>
    				<th>Discount</th>
    				<th>AmountAfterDiscount</th>
    				<th><%= taxPer %></th>
    				<th><%= hsnCode %></th>
    				<th><%= taxAmount %></th>
    				<th><%= amountAfterTax %></th>
    				<th><%= otherOrTransportCharges %></th>
    				<%-- <th style="display:none;"><%= taxOnOtherOrTransportCharges %></th>
    				<th style="display:none;"><%= otherOrTransportChargesAfterTax %></th> --%>
    				<th><%= totalAmount %></th>
    			 	<%--  <th style="display: none;"><%= isNewOrOld %></th>  --%>
    				<th><%= actions %></th>
    			
  				</tr>
  			</thead>
  			<tbody class="tblFixedHeaderBody">
  				<input type="hidden" id="numberOfRows" name="numberOfRows" value="${requestScope['listOfGetProductDetails'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
				<tr id="tr-class${GetProductDetails.serialno}" class="productchargesrow">
					<td class="tblSlno">						
						<div id="snoDivId${GetProductDetails.serialno}">${GetProductDetails.serialno}</div>
					</td>
					<td>
						<select id="product${GetProductDetails.serialno}" readonly="true" name="Product${GetProductDetails.serialno}" class="form-control  btn-visibilty1 btn-loop1" value="${GetProductDetails.productName}">
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
						<form:select path="" name="SubProduct${GetProductDetails.serialno}" readonly="true" id="subProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty1 btn-loop1">
					<option value="${GetProductDetails.sub_ProductId}$${GetProductDetails.sub_ProductName}">${GetProductDetails.sub_ProductName}</option>
					</form:select>
					</td>
					<td>
						<form:select path="" name="ChildProduct${GetProductDetails.serialno}" readonly="true" id="childProduct${GetProductDetails.serialno}" class="form-control  btn-visibilty1 btn-loop1 hiddenchild" value="${GetProductDetails.child_ProductName}">
					<option value="${GetProductDetails.child_ProductId}$<c:out value="${GetProductDetails.child_ProductName}"/>"><c:out value="${GetProductDetails.child_ProductName}"/></option>
					</form:select>
					</td>
					<td>
                        <input type="text" maxlength="500"  id="childProductVendorDesc${GetProductDetails.serialno}"   readonly="true" name="childProductVendorDesc${GetProductDetails.serialno}" onblur="calculateTotalAmount(${GetProductDetails.serialno})"   class="form-control"  data-toggle="tooltip" value="<c:out value='${GetProductDetails.childProductCustDisc}'/>" data-placement="bottom"/> 
					</td>
					<td>
						<form:select path="" name="UnitsOfMeasurement${GetProductDetails.serialno}" readonly="true" id="UnitsOfMeasurementId${GetProductDetails.serialno}" onchange="return validateProductAvailability(this);" value="${GetProductDetails.measurementName}" class="form-control  btn-visibilty1 btn-loop1">
						<option value="${GetProductDetails.measurementId}$${GetProductDetails.measurementName}">${GetProductDetails.measurementName}</option>
					</form:select>
					</td>
					<td>
						<form:input path="" name="Quantity${GetProductDetails.serialno}" readonly="true" id="QuantityId${GetProductDetails.serialno}" onkeypress="return isNumberCheck(this, event)" onblur="calculateTotalAmount(${GetProductDetails.serialno})" class="form-control  btn-visibilty1 btn-loop1 pasteDisable" value="${GetProductDetails.requiredQuantity}" autocomplete="off"/>
						<form:input path="" name="ReceivedQty${GetProductDetails.serialno}" id="ReceivedQty${GetProductDetails.serialno}"  value="${GetProductDetails.recivedQty}" type="hidden"/>
                        <%-- <form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/> --%>
					</td>
					<%-- <td>
						<form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td> --%>
					<td>
						<form:input path="" name="Price${GetProductDetails.serialno}" readonly="true" id="PriceId${GetProductDetails.serialno}" onkeypress="return isNumberCheck(this, event)" onblur="calculateTotalAmount(${GetProductDetails.serialno})" class="form-control pasteDisable" value="${GetProductDetails.price}" autocomplete="off"/>
					</td>
					<td>
						<form:input path="" name="BasicAmount${GetProductDetails.serialno}" readonly="true" id="BasicAmountId${GetProductDetails.serialno}" class="form-control"  onkeypress="return validateNumbers(this, event);" onblur="calculatePriceAmount(1)" value="${GetProductDetails.basicAmt}" />
					</td>
					<td>  <!-- width="70%" style="border: 1px solid #312d2d;" --> 
					<input type="text" id="Discount${GetProductDetails.serialno}" readonly="true" autocomplete="off"  name="Discount${GetProductDetails.serialno}"  onblur="calculateDiscountAmount(1);"  class="form-control disable-class1 pasteDisable" value="${GetProductDetails.strDiscount}" onkeypress="return isNumberCheck(this, event)" autocomplete="off"/><i class="fa fa-percent percentageSymbol pasteDisable"></i>
					</td>
					<td>
					<input type="text" id="amtAfterDiscount${GetProductDetails.serialno}"   readonly="true" name ="amtAfterDiscount${GetProductDetails.serialno}"   class="form-control"  readonly="true" value="${GetProductDetails.strAmtAfterDiscount}"/>
					</td>
					<td>
						<form:select path="" name="Tax${GetProductDetails.serialno}" readonly="true" id="TaxId${GetProductDetails.serialno}" onchange="calculateTaxAmount(1)" class="form-control">
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
						<form:input path="" name="HSNCode${GetProductDetails.serialno}" readonly="true" id="HSNCodeId${GetProductDetails.serialno}" onkeydown="appendRow()" class="form-control" autocomplete="off" value="${GetProductDetails.hsnCode}"/>
					 <input type="hidden"  id="oldHsnCode${GetProductDetails.serialno}" value="${GetProductDetails.hsnCode}" name="oldHsnCode${GetProductDetails.serialno}"/>
					</td>
					<td>
						<form:input path="" name="TaxAmount${GetProductDetails.serialno}" readonly="true" id="TaxAmountId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.taxAmount}"/>
					</td>
					<td>
						<form:input path="" name="AmountAfterTax${GetProductDetails.serialno}" readonly="true" id="AmountAfterTaxId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.amountAfterTax}"/>
					</td>
					<td style="display:none;">
						<form:input path="" name="OtherOrTransportCharges${GetProductDetails.serialno}" readonly="true" id="OtherOrTransportChargesId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.othercharges1}"/>
					</td>
					<td style="display:none;">
						<form:input path="" name="TaxOnOtherOrTransportCharges${GetProductDetails.serialno}" readonly="true" id="TaxOnOtherOrTransportChargesId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.taxonothertranportcharge1}"/>
					</td> 
					<td>
						<form:input path="" name="OtherOrTransportChargesAfterTax${GetProductDetails.serialno}" readonly="true" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.otherchargesaftertax1}"/>
					</td>
					<td>
						<form:input path="" name="TotalAmount${GetProductDetails.serialno}" readonly="true" id="TotalAmountId${GetProductDetails.serialno}"  class="form-control" value="${GetProductDetails.totalAmount}"/>
						<input type = "hidden"  id="isNewOrOld${GetProductDetails.serialno}" readonly="true" name="isNewOrOld${GetProductDetails.serialno}"  class="form-control"  autocomplete="off"  value="new" readonly="true"/>
					</td>

					<td>
						<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
						<c:if test="${listOfGetProductDetails.size() eq GetProductDetails.serialno}">
						<button type="button" name="addNewItemBtn${GetProductDetails.serialno}" id="addNewItemBtnId${GetProductDetails.serialno}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						</c:if>
						<button type="button" name="editItemBtn${GetProductDetails.serialno}" value="Edit Item" id="editItem${GetProductDetails.serialno}" class="btnaction" onclick="editInvoiceRow(${GetProductDetails.serialno})" ><i class="fa fa-pencil"></i></button> 
						<button type="button" name="addDeleteItemBtn${GetProductDetails.serialno}" id="addDeleteItemBtnId${GetProductDetails.serialno}" class="btnaction" onclick="removeRow(${GetProductDetails.serialno})" ><i class="fa fa-trash"></i></button>
					
					
					</td>
					<td style="display:none">
					<input  type = "hidden" id="actionValueId${GetProductDetails.serialno}"  name="actionValue${GetProductDetails.serialno}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					</td> 

				</tr>
				</c:forEach> 
			</tbody>
	</table>
</div>

<!-- ***********Transport Charges grid start here****************** -->
			<div class="Mrgtop30"><h4><strong>Transport Charges Table :</strong></h4></div>	
	<div class="clearfix"></div>
	<div class="table-responsive"><!--  protbldiv -->
	<table id="doInventoryChargesTableId" class="table fixedTblHeader tbl-width-fix1">
			<thead class="calTheadColor">
				<tr >
					<th class="tblSlno"><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<%-- <th><%= trasportInvoice %></th> --%>
    				<th><%= actions %></th>
  				</tr>
			</thead>
			<tbody class="tblFixedHeaderBody">
			<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransChrgsDtls']}" step="1" begin="0">
				<tr id="chargesrow${GetTransportDetails.strSerialNumber}" class="chargesrow">
					<td class="tblSlno">						
						<div  id="snoChargesDivId${GetTransportDetails.strSerialNumber}">${GetTransportDetails.strSerialNumber}</div>
					</td>
					<td>						
							<form:select path="" name="Conveyance${GetTransportDetails.strSerialNumber}" readonly="true" id="Conveyance${GetTransportDetails.strSerialNumber}" class="form-control" onchange="changeconveyance(${GetTransportDetails.strSerialNumber})">
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
						<form:input path=""  id="ConveyanceAmount${GetTransportDetails.strSerialNumber}" readonly="true" name="ConveyanceAmount${GetTransportDetails.strSerialNumber}" type="text"  placeholder="Please enter Amount"  value="${GetTransportDetails.conveyanceAmount1}" class="form-control noneClass pasteDisable" autocomplete="off" onkeyup="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})"/>
					</td>
					<td>
						<form:select path="" readonly="true" id="GSTTax${GetTransportDetails.strSerialNumber}" name="GSTTax${GetTransportDetails.strSerialNumber}" class="form-control GSTClass "  onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNumber})">
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
						<form:input path=""  id="GSTAmount${GetTransportDetails.strSerialNumber}" name="GSTAmount${GetTransportDetails.strSerialNumber}" type="number" placeholder="GST Amount"  value="${GetTransportDetails.GSTAmount1}" class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
						<td>
						<form:input path=""  type="number"  name="AmountAfterTaxx${GetTransportDetails.strSerialNumber}" id="AmountAfterTaxx${GetTransportDetails.strSerialNumber}" placeholder="Amount After Tax"  value="${GetTransportDetails.amountAfterTaxx1}" class="form-control noneClass"  autocomplete="off" readonly="true"/>
					</td>
					<td>
					<c:if test="${listOfTransChrgsDtls.size() eq GetTransportDetails.strSerialNumber}">
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId${GetTransportDetails.strSerialNumber}" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						</c:if>
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="removeTransRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-trash"></i></button>
					<button type="button" name="editchargesItemBtn${GetTransportDetails.strSerialNumber}" value="Edit Item" id="editchargesItem${GetTransportDetails.strSerialNumber}" class="btnaction" onclick="editchargesInvoiceRow(${GetTransportDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button>
				</td>
				<td style="display:none">
					<input  type = "text" id="transactionActionValue${GetTransportDetails.strSerialNumber}"  name="transactionActionValue${GetTransportDetails.strSerialNumber}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
					</td> 
				</tr>
				</c:forEach>
			</tbody>
  </table>
  <input type="hidden" id="noofTransRows" name="noofTransRows" value="${requestScope['listOfTransChrgsDtls'].size()}"/>
</div>
  



<!-- **************************************************************		 -->
<!-- ************************************* Field Location & Duration Details ************************************** -->
	
	<div class="Mrgtop30"><h4><strong>Field Location & Duration Details : <i id="refreshIcon" style="font-size: 18px;margin-left: 20px;" class="fa fa-refresh" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="1. If you don't get child products Please click here." onclick="createFirstRowForLocationDurationTable()"></i></strong></h4></div>	
	<div class="clearfix"></div>
	
	<div class="table-responsive"><!--  protbldiv -->
	<!-- <div class="col-md-12 Location_btn_table Mrgtop30"> 
	
	  <div class="table-responsive table_hide_show"> -->
	  <table id="doInventoryChargesTableId" class="table fixedTblHeader tbl-width-fix1" style="width: 1800px !important;">
			<thead class="calTheadColor">
	         <tr>
		           <th class="tblSlno">S.No</th>
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
	       
	       <tbody class="tblFixedHeaderBody" id="locationDurationTbody">
	       <c:choose><c:when test="${locationSize}">
	       <%--  <c:if test="${locationSize}"> --%>
	       <c:forEach var="GetLocationDetails" items="${requestScope['listLocationandFielddetails']}" step="1" begin="0">
	       
	         <tr id="location${GetLocationDetails.strSerialNumber}" class="location">
	           <td style="width:50px;">${GetLocationDetails.strSerialNumber}</td>
	           <td>
	              <select class="form-control" id="locationChildProduct${GetLocationDetails.strSerialNumber}" name="locationChildProduct${GetLocationDetails.strSerialNumber}" onchange="addLocation(${GetLocationDetails.strSerialNumber})"  readonly="true">
	             <option value="${GetLocationDetails.child_ProductId}$${GetLocationDetails.strChildProdname}">${GetLocationDetails.strChildProdname}</option>
	            <input type="hidden" id="oldChildProduct${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.child_ProductId}$${GetLocationDetails.strChildProdname}"  class="form-control readonly-color" name="oldChildProduct${GetLocationDetails.strSerialNumber}" placeholder="Select to date"  autocomplete="off" onchange="toDateChange(${GetLocationDetails.strSerialNumber})" readonly="true"/>    
	               </select>
	           </td>
	           <td><select class="form-control" id="location_mar${GetLocationDetails.strSerialNumber}" name="location_mar${GetLocationDetails.strSerialNumber}" readonly="true" >
	           <option value="${GetLocationDetails.locationAreaId}$${GetLocationDetails.locationArea}">${GetLocationDetails.locationArea}</option>
	           </select><input type="hidden" class="form-control" id="location_Id${GetLocationDetails.strSerialNumber}" name="location_Id${GetLocationDetails.strSerialNumber}"></td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="from_date_location${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.locationFromdate}" class="form-control readonly-color" name="from_date_location${GetLocationDetails.strSerialNumber}" placeholder="Select from date"  autocomplete="off" onchange="fromDateChange(${GetLocationDetails.strSerialNumber})" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="from_date_location${GetLocationDetails.strSerialNumber}" id="fromDateLable${GetLocationDetails.strSerialNumber}" disabled>	
					<span class="fa fa-calendar"></span></label>
	               </div>
	           </td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="to_date_location${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.locationToDate}"  class="form-control readonly-color" name="to_date_location${GetLocationDetails.strSerialNumber}" placeholder="Select to date"  autocomplete="off" onchange="toDateChange(${GetLocationDetails.strSerialNumber})" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="to_date_location${GetLocationDetails.strSerialNumber}" id="toDateLable${GetLocationDetails.strSerialNumber}" disabled>	
					<span class="fa fa-calendar"></span></label>
					</div>
	           </td>
	           <td>
	           <div class="input-group">
	              <input type="text"  class="form-control readonly-color" id="timepicker${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.locationTime}"  name="timepicker${GetLocationDetails.strSerialNumber}" placeholder="Select time"  autocomplete="off" readonly="true"/>
	              <label class="input-group-addon btn input-group-addon-border" for="timepicker${GetLocationDetails.strSerialNumber}" id="timeLable${GetLocationDetails.strSerialNumber}" disabled>	
					<span class="fa fa-clock-o"></span></label>
					</div>
	            </td>
	           <td><input type="text"  class="form-control pasteDisable" id="locationQuantity${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.locationQuantity}"  name="locationQuantity${GetLocationDetails.strSerialNumber}" value="" onkeyup="calculateLocationAmount(1)" placeholder="Please Enter Quantity"  autocomplete="off" onkeypress="return validateNumbers(this, event);" readonly="true"/></td>
	           <td><select class="form-control" id="site_Name${GetLocationDetails.strSerialNumber}" readonly="true" name="site_Name${GetLocationDetails.strSerialNumber}">
	           <option value="${GetLocationDetails.site_Id}$${GetLocationDetails.locationSiteName}">${GetLocationDetails.locationSiteName}</option>
	           </select></td>
	           <td><input type="text"   class="form-control pasteDisable" id="price_Aftertax${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.amountPerUnit}"  name="price_Aftertax${GetLocationDetails.strSerialNumber}" value="" onkeyup="locationAndDurationTablePricePerUnitAfterTax(1)"  placeholder="Please Enter Price Per Unit After Tax" autocomplete="off" onkeypress="return validateNumbers(this, event);" readonly="true"/></td>
	           <td>
	           	  <input type="text"   class="form-control pasteDisable" id="total_Amount${GetLocationDetails.strSerialNumber}" value="${GetLocationDetails.totalAmount}"  name="total_Amount${GetLocationDetails.strSerialNumber}" value="" onkeyup="durationTotalAmopuntChange(1)"  autocomplete="off" placeholder="Please Enter Total Amount" onkeypress="return validateNumbers(this, event);" readonly="true"/>
	           	  <input type="hidden"  id="locationActionValue${GetLocationDetails.strSerialNumber}" value="S" name="locationActionValue${GetLocationDetails.strSerialNumber}"/>
	           	</td>
	           	  
	           <td>
	          <%--  <c:if test="${listLocationandFielddetails.size() eq GetLocationDetails.strSerialNumber}"> --%>
	             <button type="button" style="display:none;" class="btnaction" id="addLocationPlusBtn${GetLocationDetails.strSerialNumber}" onclick="addLocationRow(${GetLocationDetails.strSerialNumber})"><i class="fa fa-plus"></i></button>
	            <%--  </c:if> --%>
	             <button type="button" class="btnaction" id="addLocationDeleteBtn${GetLocationDetails.strSerialNumber}" onclick="removeLocationRow(${GetLocationDetails.strSerialNumber})"><i class="fa fa-trash"></i></button>
	             <button type="button" class="btnaction" id="addLocationEditBtn${GetLocationDetails.strSerialNumber}"  onclick="editLocationRow(${GetLocationDetails.strSerialNumber})" ><i class="fa fa-pencil"></i></button>
	           </td>
	         </tr>
	         </c:forEach></c:when>
	         <c:otherwise>  
        <%-- <c:forEach var="GetLocationDetails" items="${requestScope['listLocationandFielddetails']}" step="1" begin="0"> --%>
	       
	         <tr id="location1" class="location">
	           <td style="width:50px;">1</td>
	           <td>
	              <select class="form-control" id="locationChildProduct1" name="locationChildProduct1" onchange="addLocation(1)" readonly="true" >
	             
	            <%-- <input type="hidden" id="oldChildProduct1"  readonly="true" class="form-control readonly-color" name="oldChildProduct${GetLocationDetails.strSerialNumber}" placeholder="Select to date"  autocomplete="off" onchange="toDateChange(${GetLocationDetails.strSerialNumber})" readonly="true"/>     --%>
	               </select>
	           </td>
	           <td><select class="form-control" id="location_mar1" name="location_mar1" readonly="true" >
	           
	           </select><input type="hidden" class="form-control" id="location_Id1" name="location_Id1"></td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="from_date_location1"  class="form-control readonly-color" name="from_date_location1" placeholder="Select from date"  autocomplete="off" onchange="fromDateChange(1)" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="from_date_location1" id="fromDateLable1" disabled>	
					<span class="fa fa-calendar"></span></label>
	               </div>
	           </td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="to_date_location1"  readonly="true" class="form-control readonly-color" name="to_date_location1" placeholder="Select to date"  autocomplete="off" onchange="toDateChange(1)" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="to_date_location1" id="toDateLable1" disabled>	
					<span class="fa fa-calendar"></span></label>
					</div>
	           </td>
	           <td>
	           <div class="input-group">
	              <input type="text"  class="form-control readonly-color" id="timepicker1"  readonly="true" name="timepicker1" placeholder="Select time"  autocomplete="off" readonly="true"/>
	              <label class="input-group-addon btn input-group-addon-border" for="timepicker1" id="timeLable1" disabled>	
					<span class="fa fa-clock-o"></span></label>
					</div>
	            </td>
	           <td><input type="text"  class="form-control" id="locationQuantity1"  readonly="true" name="locationQuantity1" value="" onkeyup="calculateLocationAmount(1)" placeholder="Please Enter Quantity"  autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>
	           <td><select class="form-control" id="site_Name1" readonly="true" name="site_Name1">
	           
	           </select></td>
	           <td><input type="text"   class="form-control" id="price_Aftertax1" readonly="true" name="price_Aftertax1" value="" onkeyup="locationAndDurationTablePricePerUnitAfterTax(1)"  placeholder="Please Enter Price Per Unit After Tax" autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>
	           <td>
	           	  <input type="text"   class="form-control" id="total_Amount1"  readonly="true" name="total_Amount1" value="" onkeyup="durationTotalAmopuntChange(1)"  autocomplete="off" placeholder="Please Enter Total Amount" onkeypress="return validateNumbers(this, event);"/>
	           	  <input type="hidden"  id="locationActionValue1" value="S" name="locationActionValue1"/>
	           	</td>
	           	  
	           <td>
	             <button type="button" class="btnaction" id="addLocationPlusBtn1" onclick="addLocationRow(1)"><i class="fa fa-plus"></i></button>
	             <button type="button" class="btnaction" id="addLocationDeleteBtn1" onclick="removeLocationRow(1)"><i class="fa fa-trash"></i></button>
	             <button type="button" class="btnaction" id="addLocationEditBtn1"  onclick="editLocationRow(1)" ><i class="fa fa-pencil"></i></button>
	           </td>
	         </tr>
	         <%-- </c:forEach> --%>
    		</c:otherwise>  
</c:choose>
	       </tbody>
	    </table>
	  </div>
	
	<div class="col-md-12 no-padding-right">
		<div class="col-md-6 col-md-offset-6 Mrgtop10 no-padding-right">
			<div class="col-md-12 Mrgtop10 no-padding-right">
				<span class="h4 col-md-6 text-right"><strong>Final Amount</strong></span> 
				<span class="h4 col-md-1"><strong>:</strong></span>
				<span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv col-md-5 text-right" ></span>
			</div>
	   </div> 	
	</div>



<!-- ************************************* Field Location & Duration Details ************************************** -->
			
			<!-- <div class="form-group clearfix "> -->
					
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
			<input type="hidden" name="locationLength" value="" id="locationLength">
			<input type="hidden" name="filesLength" value="" id="filesLength">
			 <input type="hidden"  id="oldNoofRecords" value="${locationSizeValue}" name="oldNoofRecords"/>
		   <div class="clearfix"></div>
	<br/>
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
	<!-- <div class="">
 	 <div class="">						
		<div class="floatLeft">
		  <h4 class="text-left Mrgtop20"><strong>You can upload Images/PDF(s) here </strong>(<span style="font-size: 14px;"> Note :  Now you can upload upto 8 file's</span>)</h4>
		  <P><strong>Maximum File Upload Size: 1MB</strong></P>
		  <div class="col-md-12 files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <i class="fa fa-plus"></i> 
			<div class="col-md-12">
			 <div class="market_file_style">
			  <div class="clearfix"></div>
			 </div>
			</div>
			<input type="file" id="file" name="file" style="display:none">
		 </div> 
	  </div>
   </div> -->
						
	<div class="col-md-12 text-center center-block form-submit market_btn_style" style="margin-bottom: 40px !important;">
		<input class="btn btn-warning" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
		<input class="tblchargesbtn btn btn-warning" type="button" value="Submit" onclick="createModelPOPUP()" id="saveBtnId">
	</div>	
					
 <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
      <!-- Modal content-->
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Terms and Conditions</h4>
        </div>
        <form action="" method="post">
        <div class="modal-body" style="height: 231px;overflow-y: scroll;">
        <div class="container">
	<div class="row">
  <div class="control-group" id="fields">
     <div class="controls" id="profs"> 
     <div class="col-md-12 appen-div-workorder">
     
     <c:forEach items="${listTermsAndCondition }" var="item">
      <input type="hidden" name="termsAndCondold" class="form-control" value="<c:out value='${item.strTermsConditionName}' />"/>
      <div class="col-md-12 remove-filed">
      <div class="col-md-11">
      <input type="text" name="termsAndCond" class="form-control workorder_modal_text myinputstyles" value="<c:out value='${item.strTermsConditionName}' />" title="${item.strTermsConditionName}"/>
      </div>
      <div class="col-md-1 margin-close">
      <button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
      </div>
      </div>
      </c:forEach>
      </div>
         <div class="col-md-12 padding-div">
       <div class="input_fields_wrap">
         <div class="col-md-11"><input type="text" class="form-control myinputstyles" id="workorder_modal_text1" name="termsAndCond" /></div>
         <div class="col-md-1 margin-close"><button type="button" class="btn btn-success plus-button add_field_button" style="font-size: 12px;padding: 8px 12px;margin-top: -3px;"><i class="fa fa-plus "></i></button></div>
       </div>
    </div>
    
           <br>
         
            
            
            </div>
        </div>
	</div>
</div>
       
       <div class="col-md-12">
          <div class="col-md-11 emailCC" >
        	<input type="hidden" name="check1" id="ccemailcheckbox" onclick="dynInput(this);"  value="${ccEmails}" /><span style="">(Optional)If you want to add CC In emails.</span><input type="text" id="" name="ccEmailId" value="${ccEmails}" class="form-control" style=""/><br>
     	 <input type="hidden" id="" name="oldccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/>
     	 <input type="hidden" class="form-control" value="" name="conclusionDescold">
     	 <input type="hidden" class="form-control conclusiontxt" id="conclusionId1" value="" name="conclusionDesc">
     	 
     	 </div>
       </div>
        
       <div class="col-md-12">
         <div class="subject-container col-md-11">
       	<span>Subject</span>
      	<!-- <input type="text" name="subject" id="" class=" form-control1 sub-control" Placeholder="Please enter the subject" style="float: left;margin-top: 3px;"/> --><br>
      	<input type="text" id="" name="subject"  class="form-control" Placeholder="Please enter the subject" value="${strSubject}"/>
      	<input type="hidden" id="" name="strSubject"  class="form-control"  value="${strSubject}"/>
        </div>
       </div>
      </div>
         <div class="modal-footer" >
          <div class="col-md-12 text-center center-block">
          <button type="button" class="btn btn-warning" id="createPoSubmit" data-dismiss="modal" onclick="createPO()">Submit</button>
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
	
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/marketing/jquery.timepicker.min.js"></script>       
       <script src="js/marketing/MarketingRevisedPODetails.js" type="text/javascript"></script>
       <script src="js/marketing/marketingCommonCode.js" type="text/javascript"></script>
	   <script src="js/sidebar-resp.js" type="text/javascript"></script> 
		
		<script>
		
			$(document).ready(function() {
				    $('[data-toggle="tooltip"]').tooltip();   
				
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				 $("#from_date_location").datepicker({dateFormat: 'dd-M-y',
					 changeMonth: true,
				      changeYear: true});
				 $("#to_date_location").datepicker({dateFormat: 'dd-M-y',
					 changeMonth: true,
				      changeYear: true});
				 debugger;
				 $("#timepicker").timepicker({
					 timeFormat: 'h:mm p',
					    interval: 10,
					    minTime: '00.00AM',
					    maxTime: '11.59PM',
					    startTime: '00:00',
					    dynamic: false,
					    dropdown: true,
					    scrollbar: true
					});
				 
					for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
						$("#product"+i).combobox();    
						$("#toggle").click(function() { $( "#combobox"+i).toggle();  });
						$("#subProduct"+i).combobox();
						$("#childProduct"+i).combobox(); 						
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
						$('#ChildProduct'+i).addClass('form-control ChildProduct');
					    $('.custom-combobox').closest('td').find('.custom-combobox-toggle').addClass('hide'); 
				   }	
					$(".location").each(function(){
						var id=$(".location").attr("id").split("location")[1];
						var locationIdForShow=$(".location:last").attr("id").split("location")[1];
						if(id==locationIdForShow){
							$("#addLocationPlusBtn"+id).show();
						}else{
							$("#addLocationPlusBtn"+id).hide();
						}
						
					})
					var locationIdForShow=$(".location:last").attr("id").split("location")[1];
					$("#addLocationPlusBtn"+locationIdForShow).show();
			});
			
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
	          
			 	
			 	function createModelPOPUP(){
					debugger;
					var VendorNameId=$("#VendorNameId").val();
					 var GSTINNumber=$("#GSTINNumber").val();
					var VendorAddress=$("#VendorAddress").val();
					var selectPoTo = $("#poToId").val();
					var siteNameIdsitewise = $("#siteNameId").val();
					var siteNameLocationwise = $("#site").val();
					var fLocationselect = $("#locationChildProduct1").val();
					var fLocationloc = $("#location_mar1").val();
					var fLocationfromdate = $("#from_date_location").val(); 
					var fLocationtodate = $("#to_date_location").val(); 
					var flocationtime = $("#timepicker").val();
					var flocationName = $("#site_Name1").val();
					if(VendorNameId==""){
						alert("Please enter vendor name.");
						$("#VendorNameId").focus();
						return false;
					} 
					if(GSTINNumber=="" || VendorAddress==''){
						alert("Please enter valid vendor name.");
						$("#VendorNameId").focus();
						return false;
					} 
					if(selectPoTo == "select"){
			 			alert("Please select PO To");
			 			$("#poToId").focus();
			 			return false;
			 		}
					if(selectPoTo == "SiteWise"){
			 			if(siteNameIdsitewise == ""){
			 				alert("Please select site")
			 				$("#siteNameId").focus();
			 				return false;
			 			}
			 		}
					if(selectPoTo == "LocationWise"){
			 			if(siteNameLocationwise == ""){
			 				alert("Please select site")
			 				$("#site").focus();
			 				return false;
			 			}
			 		}
			 		var transportDetails = document.getElementById("Conveyance1").value;
			 		if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
			 			alert("Please Select conveynace.");
			 			document.getElementById("Conveyance1").focus();
			 			return false;
			 		} 	
			 		//checking invoice Amount
			 		var locationChild=$("#locationChildProduct1").val();
			 		if(locationChild){
			 			var valTableData=validateLocationDurationTable();
			 			if(valTableData==false){
			 				return false;
			 			}
			 			var valInovoiceAmount=validateInvoiceAMountWithDurationTable();
						if(valInovoiceAmount==false){
							return false;
						}
			 		}					
			 		calculateOtherCharges();
			 		/*table hidden validation*/
			 		if(transportDetails != "" || transportDetails != '' || transportDetails != null){
			 		$('#myModal').modal('show');

			 		}
			 	}; 


			 	function createPO() {
			 	
			 		var countOftermsandCondsfeilds = document.getElementsByClassName("myinputstyles");   					
			 		$("#counttermsId").val(countOftermsandCondsfeilds.length);			 		
			 		 
			 		var canISubmit = window.confirm("Do you want to Submit?");
			 		
			 		if(canISubmit == false) {			 			
			 			return;
			 		}
			 		var locationRowCountNum=[];
			 		$(".location").each(function(){
			 			var currentId=$(this).attr("id").split("location")[1];	
			 			locationRowCountNum.push(currentId);
			 		});
			 		var filesLength=$(".selectCount").length;
			 		debugger;
			 		//console.log("chargesRowCountNum: "+chargesRowCountNum);
			 		$('#locationLength').val(locationRowCountNum);
			 		$(".overlay_ims").show();	
					$(".loader-ims").show();
			 		//document.getElementById("saveBtnId").disabled = true;	
			 		document.getElementById("filesLength").value = filesLength;	
			 		document.getElementById("countOfRows").value = getAllProdsCount();
			 		document.getElementById("countOfChargesRows").value = getAllChargesCount();
			 		document.getElementById("doInventoryFormId").action ="editAndSaveMarketingRevisedPO.spring";
			 		document.getElementById("doInventoryFormId").method = "POST";
			 		document.getElementById("doInventoryFormId").submit();
			 	}
	 	
				/* ************* Method for new text feild in model popup ************** */
				
	
			
			 	
 </script>
				<script>
	$(document).ready(function() {
		
		$('#days'). bind('copy paste cut',function(e) {
			e. preventDefault(); //disable cut,copy,paste.
		});
		$('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
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
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-11"><input type="text" name="termsAndCond" id="newTC'+x+'" title="'+tc+'" value="'+tc+'" class="form-control"/></div><div class="col-md-1 margin-close"><button type="button" class="btn btn-danger remove-button remove_field" style="font-size: 12px;padding: 8px 12px;margin-top: -3px;"><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#newTC"+x).val(tc);
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	    	//alert("Do you want to delete?");
	    	var caniDelete=window.confirm("Do you want to delete?");
	    	if(caniDelete==false){
	    		return false;
	    	}
	       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
	       
	    })
	     $(wrapper1).on("click",".remove_field", function(e){ //user click on remove text
	    	// alert("Do you want to delete?");
	    	 var caniDelete=window.confirm("Do you want to delete?");
		    	if(caniDelete==false){
		    		return false;
		    	}
	       /*  e.preventDefault(); */ $(this).parent().parent(".remove-filed").remove();
	    })
	    $("#deliveryDate").datepicker({
	        changeMonth: true,
	        changeYear: true,
	        dateFormat: 'dd-M-y',
	        minDate: 0
	      });
	    var BrandingData;
	    debugger;
	    var PoId=$("#poToId").val();
		  if(PoId=="SiteWise"){
			  $(".class_selectsite").show(); 
			  $(".class_selectsite1").hide();		
		  }
	      if(PoId=="LocationWise"){
	    	  $(".class_selectsite1").show();
	    	  $(".class_selectsite").hide();    
	    	  siteLocationChange();
		  }
	      if(PoId=="BrandingWise"){
	    	  $(".class_selectsite1").hide();
	    	  $(".class_selectsite").hide();
	    	  BrandingData=[];
	    	  $.ajax({
	    		  url : "brandingWiseDetails.spring",
	    		  type : "get",
	    		  Cdata : "",
	    		  contentType : "application/json",
	    		  success : function(data) {debugger;
	    			  console.log("data: "+JSON.stringify(data));	    		
	    			   $.each(data, function(key,value){debugger;
	    				 BrandingData.push(key+"$"+value);
	    				}); 
	    			   localStorage.setItem('BrandingDataFromLocalStorage', BrandingData);	    			 
	    		  }
	    	 });
	    	
		  }
	});
	
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
			  $("#AmountAfterTaxx"+btn).val("0");
			  $("#AmountAfterTaxx"+btn).attr("readonly", "true");						  
			  }else{
				  $("#ConveyanceAmount"+btn).val("");
				  $("#ConveyanceAmount"+btn).removeAttr("readonly");
				  $("#GSTTax"+btn).val("");
				  $("#GSTTax"+btn).removeAttr("readonly");
				  $("#GSTAmount"+btn).val("");
				  $("#AmountAfterTaxx"+btn).val("");
			  }
	} 
	 
	 /*Marketing Inwards Script For showing Divs*/ 
	 $("#singleSite").click(function (){
		 debugger;
	  $(".single_site").show(); 
	 });
	 $("#siteWise").click(function(){
		 $(".single_site").hide(); 
	 });
	 $("#brandingWise").click(function(){
		 $(".single_site").hide(); 
	 });
	 
	 </script>
	<!-- /*Marketing Inwards Select site autocomplete script*/ -->
	<script>
function autocomplete(inp, arr) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function(e) {
      var a, b, i, val = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!val) { return false;}
      currentFocus = -1;
      /*create a DIV element that will contain the items (values):*/
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");
      /*append the DIV element as a child of the autocomplete container:*/
      this.parentNode.appendChild(a);
      /*for each item in the array...*/
      for (i = 0; i < arr.length; i++) {
        /*check if the item starts with the same letters as the text field value:*/
        if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
          /*create a DIV element for each matching element:*/
          b = document.createElement("DIV");
          /*make the matching letters bold:*/
          b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
          b.innerHTML += arr[i].substr(val.length);
          /*insert a input field that will hold the current array item's value:*/
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
          /*execute a function when someone clicks on the item value (DIV element):*/
          b.addEventListener("click", function(e) {
              /*insert the value for the autocomplete text field:*/
              inp.value = this.getElementsByTagName("input")[0].value;
              /*close the list of autocompleted values,
              (or any other open lists of autocompleted values:*/
              closeAllLists();
          });
          a.appendChild(b);
        }
      }
  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {
        /*If the arrow DOWN key is pressed,
        increase the currentFocus variable:*/
        currentFocus++;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 38) { //up
        /*If the arrow UP key is pressed,
        decrease the currentFocus variable:*/
        currentFocus--;
        /*and and make the current item more visible:*/
        addActive(x);
      } else if (e.keyCode == 13) {
        /*If the ENTER key is pressed, prevent the form from being submitted,*/
        e.preventDefault();
        if (currentFocus > -1) {
          /*and simulate a click on the "active" item:*/
          if (x) x[currentFocus].click();
        }
      }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
      closeAllLists(e.target);
  });
}

/*An array containing all the country names in the world:*/
var countries = ["Acropolis","Development","Soham","Nandanam","Kondapur"];

/*initiate the autocomplete function on the "myInput" element, and pass along the countries array as possible autocomplete values:*/
autocomplete(document.getElementById("mySites"), countries);
</script>
  <!-- /*Marketing Inwards Select site autocomplete script*/ -->
<script>
var PODATA;
var BrandingData;
  $("#poToId").change(function(){debugger;
	  var PoId=$(this).val();
	  if(PoId=="SiteWise"){
		  $("#siteNameId").val("");
		  $(".class_selectsite").show(); 
		  $(".class_selectsite1").hide();		
	  }
      if(PoId=="LocationWise"){
    	  $("#site").val("");
    	  $(".class_selectsite1").show();
    	  $(".class_selectsite").hide();    	
	  }
      if(PoId=="BrandingWise"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
    	  BrandingData=[];
    	  $.ajax({
    		  url : "brandingWiseDetails.spring",
    		  type : "get",
    		  Cdata : "",
    		  contentType : "application/json",
    		  success : function(data) {debugger;
    			  console.log("data: "+JSON.stringify(data));
    			  var tempOptions="<option>--select--</option>";
    			   $.each(data, function(key,value){debugger;
    				 tempOptions+="<option value='"+key+"$"+value+"'>"+value+"</option>";
    				 BrandingData.push(key+"$"+value);
    				}); 
    			   $("#site_Name1").html(tempOptions);
    		  }
    	 });
    	
	  }
      if(PoId=="select"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
	  }
   	 //creating duration table while changing PO TO
      createFirstRowForLocationDurationTable();
      
  })
  function siteLocationChange(name){debugger;
	  PODATA=[];
	  var site=$("#site").val();
	  $.ajax({
		  url : "getLocationData.spring?site="+site,
		  type : "get",
		  Cdata : "",
		  contentType : "application/json",
		  success : function(data) {debugger;
			  console.log("data: "+JSON.stringify(data));
			 // PODATA.push(data);
			  var tempOptions="<option>--select--</option>";
		   $.each(data, function(key,value){debugger;
			 tempOptions+="<option value='"+key+"$"+value+"'>"+value+"</option>";
			 PODATA.push(key+"$"+value);
			});  
			  localStorage.setItem('locatioWiseDataFromLocalStorage', PODATA);
			  if(name=="state"){
				   createFirstRowForLocationDurationTable();
			   }
		  }
	 });
	  console.log("PODATA: "+PODATA);
	
  }
  function siteNameChange(){
	  $("#site_Name1").html("<option>"+$("#siteNameId").val()+"</option>");
	  $("#site_Name1").attr("readonly");
  }
  function isNumberCheck(el, evt) {
		debugger;
	    var charCode = (evt.which) ? evt.which : event.keyCode;
	    var num=el.value;
	    var number = el.value.split('.');
	    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
	        return false;
	    }
	    //just one dot
	    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {
	         return false;
	    }
	    //get the carat position
	    var caratPos = getSelectionStart(el);
	    var dotPos = el.value.indexOf(".");
	    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
	        return false;
	    }
	    return true;
	}
	function getSelectionStart(o) {
		if (o.createTextRange) {
			var r = document.selection.createRange().duplicate();
			r.moveEnd('character', o.value.length);
			if (r.text == '') return o.value.length;
			return o.value.lastIndexOf(r.text);
		} else return o.selectionStart;
	}
	
	
 </script><script>
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
var PODATA;
var BrandingData;
  $("#poToId").change(function(){debugger;
	  var PoId=$(this).val();
	  if(PoId=="SiteWise"){
		  $(".class_selectsite").show(); 
		  $(".class_selectsite1").hide();		
	  }
      if(PoId=="LocationWise"){
    	  $(".class_selectsite1").show();
    	  $(".class_selectsite").hide();    	
	  }
      if(PoId=="BrandingWise"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
    	  BrandingData=[];
    	  $.ajax({
    		  url : "brandingWiseDetails.spring",
    		  type : "get",
    		  Cdata : "",
    		  contentType : "application/json",
    		  success : function(data) {debugger;
    			  console.log("data: "+JSON.stringify(data));
    			  var tempOptions="<option value=''>--select--</option>";
    			   $.each(data, function(key,value){debugger;
    				 tempOptions+="<option value='"+key+"$"+value+"'>"+value+"</option>";
    				 BrandingData.push(key+"$"+value);
    				}); 
    			   $("#site_Name1").html(tempOptions);
    		  }
    	 });
    	
	  }
      if(PoId=="select"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
	  }
     
  })
  function siteNameChange(){
	  $("#site_Name1").html("<option>"+$("#siteNameId").val()+"</option>");
	  $("#site_Name1").attr("readonly");
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
var childDataForDuration;
function loadChildDataFromProductTable(){debugger;
	childDataForDuration=[];
	$(".productchargesrow").each(function(){
		var id=$(this).attr("id").split("tr-class")[1];
		if($("#childProduct"+id).val()!="" && $("#actionValueId"+id).val()!="R"){
			childDataForDuration.push($("#childProduct"+id).val());
		}
	})
}
 </script>
</body>
</html>
