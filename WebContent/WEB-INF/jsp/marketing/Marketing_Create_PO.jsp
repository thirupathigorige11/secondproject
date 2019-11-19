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
		<jsp:include page="../CacheClear.jsp" />  
		<script src="js/momment.js"></script>
		<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
		<script src="js/sweetalert2.js" type="text/javascript"></script>
		<script src="js/marketing/marketingCommonCode.js" type="text/javascript"></script>
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
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
    	    div.setAttribute("style", ""); 
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
    	    div.setAttribute("class", "hiddenchild"); 
    	    
    	    
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
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("autocomplete", "off"); 
		    div.setAttribute("class", 'form-control pasteDisable');			    
		    cell.appendChild(div);		    
		    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
		    /* var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("style", "border: 1px solid #312d2d;"); 
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);		 */    
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
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
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control disableCopyPaste pasteDisable');
		    cell.appendChild(div);
		   var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-percent");
		    btn.setAttribute("style", "margin-top: -21px;margin-left: 200px;");
		    cell.appendChild(btn);
		    $('.disableCopyPaste'). bind('copy paste cut',function(e) {
				e. preventDefault(); //disable cut,copy,paste.
			});
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
		    div.setAttribute("maxlength","20");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", ""); 
		    cell.appendChild(div);
		    
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
        snoDiv.setAttribute("style", "width: 32px");
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
    	    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
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
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("placeholder", "Please enter Amount");
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("onkeyup", "calculateGSTTaxAmount("+vfx+")");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event)");
		    div.setAttribute("class", 'form-control pasteDisable');
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
    	    defaultOption.text = "--select--";
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
		    div.setAttribute("type", "number");
		    div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("readonly", 'readonly');
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+"x"+vfx);
		    div.setAttribute("id", tableColumnName+"x"+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "Amount After Tax");
		    div.setAttribute("readonly", 'readonly');
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
			 <div class="overlay_ims" style="display: none;" ></div>
			  <div class="loader-ims" id="loaderId" style="display: none;">
					<div class="lds-ims">
						<div></div><div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingimsMessage">Loading...</div>
			  </div>
	  <form:form modelAttribute="indentReceiveModelForm" id="doInventoryFormId" class="form-horizontal" enctype="multipart/form-data">
		  
		   <div class="clearfix"></div>
		   <div class="border-indent"> <!-- container -->
			<div class="col-md-12">
				  <div class="col-md-4">
				   <div class="form-group">
						<label class="control-label col-md-6">Vendor Name : </label>
						<div class="col-md-6" >
							<form:input path="VendorName" placeholder="Search..." id="VendorNameId" class="form-control" style="z-index:2;position: inherit;" autocomplete="off"/>
						</div>
					</div>
				  </div>
				  <div class="col-md-4">
					 <div class="form-group">
						<label class="control-label col-md-6">GSTIN : </label>
						<div class="col-md-6">
							<form:input path="GSTINNumber" id="GSTINNumber" placeholder="GSTIN Number" class="form-control" autocomplete="off" readonly="true"  data-toggle="tooltip" data-placement="bottom"/>
						</div>
					 </div>
				  </div>
				 <div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">Vendor Address : </label>
						<div class="col-md-6" >
							<form:input path="VendorAddress" id="VendorAddress" placeholder="Vendor Address" class="form-control" readonly="true"  data-toggle="tooltip" data-placement="bottom"/>
						</div>
					 </div>
				 </div>
				<div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6 col-xs-12">Delivery Date: </label>
						<div class="col-md-6 col-xs-12 input-group" >
							<form:input path="" id="deliveryDate" name="deliveryDate" placeholder="Delivery Date" class="form-control readonly-color"   autocomplete="off" readonly="true"/>
							<label class="input-group-addon btn input-group-addon-border" for="deliveryDate">	
							<span class="fa fa-calendar"></span></label>
							
						</div>
					 </div>
				</div>
				<div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">PO To: </label>
						<div class="col-md-6" >
							<select class="form-control" id="poToId" name="poTo">
							   <option value="select">--Select--</option>
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
                           <input id="siteNameId" type="text"  name="site" onkeyup="populateSite(this)"  autocomplete="off" class="form-control" placeholder="Enter Site Name" onchange="siteNameChange()">
                       </div>
					 </div>
				</div>
				<div class="col-md-4 class_selectsite1" style="display:none;">
					 <div class="form-group">
					  <label class="control-label col-md-6">Select Site: </label>
						<div class="col-md-6" >
							<select name="siteNames" id="site" class="form-control" onchange="siteLocationChange()">
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
							<input type="text" id="days" name="days" class="form-control "  placeholder="Ex : 10" onkeypress="return isNumberCheck(this, event)" autocomplete="off">
						</div>
					 </div>
				</div>
				<div class="col-md-6 col-xs-12" >
						<input type="hidden" id="vendorId" name="vendorId" class="form-control"  />
						<input type="hidden" name="strCreationDate" id="strCreationDate"   class="form-control" value=""/>
						<input type="hidden" name="SiteName" id="siteName"   class="form-control" value="${siteName}"/>
						<input type="hidden" name="indentNumber" id="indentNumber"   class="form-control" value="${indentNumber}"/>
						<input type="hidden" name="siteWiseIndentNo" id="siteWiseIndentNo"   class="form-control" value="${siteWiseIndentNo}"/>
						<input type="hidden" name="toSiteId" id="toSiteId"   class="form-control" value=""/>
						<input type="hidden" name="countOftermsandCondsfeilds" id="counttermsId"   class="form-control" value=""/>
			     </div>
			</div>
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
				<tr id="productchargesrow1" class="productchargesrow">
					<td class="tblSlno">						
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
						<form:select path="ChildProduct1" id="comboboxsubSubProd1" class="form-control  btn-visibilty1 btn-loop1 hiddenchild"/>
					</td>
					<td>
                        <input type="text" maxlength="500"  id="childProductVendorDesc1"   name="childProductVendorDesc1" onblur="calculateTotalAmount(1)"   class="form-control" value=""  data-toggle="tooltip" data-placement="bottom"/> 
					</td>
					<td>
						<form:select path="UnitsOfMeasurement1" id="UnitsOfMeasurementId1" onchange="return validateProductAvailability(this);" class="form-control  btn-visibilty1 btn-loop1"/>
					</td>
					<td>
						<form:input path="Quantity1" id="QuantityId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)" class="form-control  btn-visibilty1 btn-loop1 pasteDisable" autocomplete="off"/>
                        <%-- <form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/> --%>
					</td>
					<%-- <td>
						<form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td> --%>
					<td>
						<form:input path="Price1" id="PriceId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)" class="form-control pasteDisable" autocomplete="off"/>
					</td>
					<td>
						<form:input path="BasicAmount1" id="BasicAmountId1" class="form-control"  onkeypress="return validateNumbers(this, event);" onblur="calculatePriceAmount(1)" readonly="true"/>
					</td>
					<td>  <!-- width="70%" style="border: 1px solid #312d2d;" -->
					<input type="text" id="Discount1" autocomplete="off"  name="Discount1"  onkeypress="return validateNumbers(this, event);" onblur="calculateDiscountAmount(1);"  class="form-control disableCopyPaste disable-class1 pasteDisable" value="" autocomplete="off"/><i  style="margin-top: -21px;margin-left: 200px;" class="fa fa-percent"></i>
					</td>
					<td>
					<input type="text" id="amtAfterDiscount1"   name ="amtAfterDiscount1"   class="form-control" value="" readonly="readonly"/>
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
						<form:input path="HSNCode1" id="HSNCodeId1" maxlength="20" onkeydown="appendRow()" class="form-control" autocomplete="off"/>
					</td>
					<td>
						<form:input path="TaxAmount1" id="TaxAmountId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="AmountAfterTax1" id="AmountAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td style="display:none;">
						<form:input path="OtherOrTransportCharges1" id="OtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td>
					<td style="display:none;">
						<form:input path="TaxOnOtherOrTransportCharges1" id="TaxOnOtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td> 
					<td>
						<form:input path="OtherOrTransportChargesAfterTax1" id="OtherOrTransportChargesAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="TotalAmount1" id="TotalAmountId1" readonly="true" class="form-control"/>
						<input type = "hidden"  id="isNewOrOld1"  name="isNewOrOld1"  class="form-control"  autocomplete="off"  value="new" readonly="true"/>
					</td>

					<td>
						<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
						<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button type="button" style="display: none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteproductRow(this, 1)" ><i class="fa fa-trash"></i></button>
					</td>
					

				</tr>
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
				<tr id="chargesrow1" class="chargesrow">
					<td class="tblSlno">						
						<div  id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="Conveyance1" id="Conveyance1" class="form-control" onchange="changeconveyance(1)">
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
						<form:input path="ConveyanceAmount1" id="ConveyanceAmount1" name="ConveyanceAmount1" type="text" onkeypress="return validateNumbers(this, event)"  placeholder="Please enter Amount"  class="form-control noneClass pasteDisable" autocomplete="off" onkeyup="calculateGSTTaxAmount(1)"/>
					</td>
					<td>
						<form:select path="GSTTax1" id="GSTTax1" name="GSTTax1" class="form-control GSTClass "  onchange="calculateGSTTaxAmount(1)">
							<form:option value="">--select--</form:option>
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
						<form:input path="GSTAmount1" id="GSTAmount1" name="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
						<td>
						<form:input path="AmountAfterTaxx1" type="number"  name="AmountAfterTaxx1" id="AmountAfterTaxx1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
					<td>
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" style="display: none;"  name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</tbody>
  </table>
</div>
  



<!-- **************************************************************		 -->
<!-- ************************************* Field Location & Duration Details ************************************** -->
	<div class="col-md-12 Location_btn_table Mrgtop30"> 
	  <a href="#" onclick="openLocationAndDurationTable()">Field Location & Duration Details</a>
	  <div class="table-responsive table_hide_show">
	    <table class="table fixedTblHeader" id="FieldLocation_durationtable" style="width:2000px;">
	       <thead class="calTheadColor">
	         <tr>
		           <th style="width:50px;">S.No</th>
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
	       <tbody class="tblFixedHeaderBody">
	         <tr id="location1" class="location">
	           <td style="width:50px;">1</td>
	           <td>
	              <select class="form-control" id="locationChildProduct1" name="locationChildProduct1" onchange="addLocation(1)">
	               <!--  <option>Acid</option>
	                <option>Air Spray</option>
	                <option>Mobile Phones</option>
	                <option>Wires</option> -->
	               </select>
	           </td>
	           <td><select class="form-control" id="location_mar1" name="location_mar1"></select><input type="hidden" class="form-control" id="location_Id1" name="location_Id1"></td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="from_date_location1" class="form-control readonly-color"  onchange="fromDateChange(1)" name="from_date_location1" placeholder="Select from date"  autocomplete="off" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="from_date_location1">	
					<span class="fa fa-calendar"></span></label>
	               </div>
	           </td>
	           <td>
	           <div class="input-group">
	               <input type="text" id="to_date_location1" class="form-control readonly-color" onchange="toDateChange(1)" name="to_date_location1" placeholder="Select to date"  autocomplete="off" readonly="true"/>
	               <label class="input-group-addon btn input-group-addon-border" for="to_date_location1">	
					<span class="fa fa-calendar"></span></label>
					</div>
	           </td>
	           <td>
	           <div class="input-group">
	              <input type="text"  class="form-control readonly-color" id="timepicker1" style="z-index:0;" name="timepicker1" placeholder="Select time"  autocomplete="off" readonly="true"/>
	              <label class="input-group-addon btn input-group-addon-border" for="timepicker1">	
					<span class="fa fa-clock-o"></span></label>
					</div>
	            </td>
	           <td><input type="text"  class="form-control pasteDisable" id="locationQuantity1" name="locationQuantity1" onkeyup="calculateLocationAmount(1)" placeholder="Please Enter Quantity"  autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>
	           <td><select class="form-control" id="site_Name1" name="site_Name1"></select></td>
	           <td><input type="text"   class="form-control pasteDisable" id="price_Aftertax1" name="price_Aftertax1" onkeyup="locationAndDurationTablePricePerUnitAfterTax(1)"  placeholder="Please Enter Price Per Unit After Tax" autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>
	           <td><input type="text"   class="form-control pasteDisable" id="total_Amount1" name="total_Amount1" onkeyup="durationTotalAmopuntChange(1)"  autocomplete="off" placeholder="Please Enter Total Amount" onkeypress="return validateNumbers(this, event);"/></td>
	           <td>
	             <button type="button" class="btnaction" id="addLocationPlusBtn1" onclick="addLocationRow(1)"><i class="fa fa-plus"></i></button>
	             <button type="button" style="display:none;" class="btnaction" id="addLocationDeleteBtn1" onclick="deleteLocationRow(1)"><i class="fa fa-trash"></i></button>
	           </td>
	         </tr>
	       </tbody>
	    </table>
	  </div>
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
		   <div class="clearfix"></div>
	<br/>
	<div class="">
 	 <div class="">						
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
   </div>
						
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
         <div class="col-md-11"><input type="text" class="form-control myinputstyles" id="workorder_modal_text1" name="termsAndCond" value=""/></div>
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
     	 </div>
       </div>
        
       <div class="col-md-12">
         <div class="subject-container col-md-11">
       	<span>Subject</span>
      	<!-- <input type="text" name="subject" id="" class=" form-control1 sub-control" Placeholder="Please enter the subject" style="float: left;margin-top: 3px;"/> --><br>
      	<input type="text" id="" name="subject"  class="form-control" Placeholder="Please enter the subject" />
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
       <script src="js/marketing/marketing_CreatePO.js" type="text/javascript"></script>
	   <script src="js/sidebar-resp.js" type="text/javascript"></script> 
		
		<script>
		
			$(document).ready(function() {
				    $('[data-toggle="tooltip"]').tooltip();   
				    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
				
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				 $("#from_date_location1").datepicker({dateFormat: 'dd-M-y',
					 changeMonth: true,
				      changeYear: true});
				 $("#to_date_location1").datepicker({dateFormat: 'dd-M-y',
					 changeMonth: true,
				      changeYear: true});
				 debugger;
				 $("#timepicker1").timepicker({
					 timeFormat: 'h:mm p',
					    interval: 10,
					    minTime: '00.00AM',
					    maxTime: '11.59PM',
					    startTime: '00:00',
					    dynamic: false,
					    dropdown: true,
					    scrollbar: true
					});
				
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
					var fLocationfromdate = $("#from_date_location1").val(); 
					var fLocationtodate = $("#to_date_location1").val(); 
					var flocationtime = $("#timepicker1").val();
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
			 				alert("Please select location")
			 				$("#site").focus();
			 				return false;
			 			}
			 		}
					//validating product table
					var validateProductTableRow=validateProductRow();
					if(validateProductTableRow==false){
						return false;
					}
					//validating charges table
					var validateChargesTableRow=validateChargesRow();
					if(validateChargesTableRow==false){
						return false;
					}
					
			 		var transportDetails = document.getElementById("Conveyance1").value;
			 		if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
			 			alert("Please Select conveynace.");
			 			document.getElementById("Conveyance1").focus();
			 			return false;
			 		} 
			 		/*table hidden validation*/
			 		 if($('.table_hide_show').css('display') == 'block'){
			 		  //validating duration table
						var validateFieldlocationTableRow=validateFieldlocationRow();
						if(validateFieldlocationTableRow==false){
							return false;
						}
						//checking invoice Amount
						var valInovoiceAmount=validateInvoiceAMountWithDurationTable();
						if(valInovoiceAmount==false){
							return false;
						}
					}
			 		calculateOtherCharges();
			 		/*table hidden validation*/
			 		if(transportDetails != "" || transportDetails != '' || transportDetails != null){
			 		$('.modal').modal('show');

			 		}
			 	}; 


			 	function createPO() {
			 	
			 		$(".overlay_ims").show();	
					$(".loader-ims").show();
			 		var countOftermsandCondsfeilds = document.getElementsByClassName("myinputstyles");   					
			 		$("#counttermsId").val(countOftermsandCondsfeilds.length);			 		
			 		 
			 		var canISubmit = window.confirm("Do you want to Submit?");
			 		
			 		if(canISubmit == false) {
			 			$(".overlay_ims").hide();	
						$(".loader-ims").hide();
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
			 		 $('#saveBtnId').attr("disabled", true);
			 		 $('#calculateBtnId').attr("disabled", true);
			 		$('#locationLength').val(locationRowCountNum);
			 		//document.getElementById("saveBtnId").disabled = true;	
			 		document.getElementById("filesLength").value = filesLength;	
			 		document.getElementById("countOfRows").value = getAllProdsCount();
			 		document.getElementById("countOfChargesRows").value = getAllChargesCount();
			 		document.getElementById("doInventoryFormId").action ="SaveMarketingPoDetails.spring";
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
		$('.disableCopyPaste'). bind('copy paste cut',function(e) {
			e. preventDefault(); //disable cut,copy,paste.
		});
		
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
	    	 //alert("Do you want to delete?");
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
	   /*  $(".Location_btn_table a").click(function(){
	    		sessionStorage.lcoID = $("#QuantityId1").val();
	        $(".table_hide_show").toggle();
	        //adding alues to select option 
	        var childDataLength=childData.length;
	    	if(childDataLength>=0){debugger;	
	    	var selectoption="<option>--select--</option>"
	    	for(var i=0;i<childDataLength;i++){
	    		selectoption+="<option value='"+childData[i]+"'>"+childData[i].split("$")[1]+"</option>"
	    	}
	    	$("#locationChildProduct1").html(selectoption);
	    	}
	    }); */
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
				  //$("#GSTAmount"+btn).removeAttr("readonly");
				  $("#AmountAfterTaxx"+btn).val("");
				 // $("#AmountAfterTaxx"+btn).removeAttr("readonly");
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
    			   if(key!='996'){
    				 tempOptions+="<option value='"+key+"$"+value+"'>"+value+"</option>";
    				 BrandingData.push(key+"$"+value);
    			   }
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
  function siteLocationChange(){debugger;
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
			  var tempOptions="<option value=''>--select--</option>";
		   $.each(data, function(key,value){debugger;
		   if(key!='996'){
			   tempOptions+="<option value='"+key+"$"+value+"'>"+value+"</option>";
				 PODATA.push(key+"$"+value); 
		   }
			});  
			  $("#site_Name1").html(tempOptions);
		  }
	 });
	  console.log("PODATA: "+PODATA);
	
  }
  function siteNameChange(){
	  $("#site_Name1").html("<option>"+$("#siteNameId").val()+"</option>");
	  $("#site_Name1").attr("readonly");
  }
  function isNumberCheck(el, evt) {
	    var charCode = (evt.which) ? evt.which : event.keyCode;				  
	    if (charCode < 48 || charCode > 57) {
	        return false;
	    }else{
			return true;
		}
	    
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
	}); 
	//from second file onwards 
	function fileChange(id, current){ debugger;
	
	var result=validateFileExtention();
	  if(result==false){
		  return false
	  }
	
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
		if(canIDelete==false){
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
		//scoll to bottom 
		window.scrollTo(0,document.querySelector("body").scrollHeight);
		console.log(document.querySelector("body").scrollHeight);
	}
  
	function start_and_end(str) {
		if (str.length > 25) {
		   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
		}
	  return str;
	}
	 function validateFileExtention(){
		  debugger;
		var ext="";
		var kilobyte=1024;
		var countUploadedFiles=0;
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
		        	if((this.files[0].size/kilobyte)<=kilobyte){
		        		countUploadedFiles++;
					}else{
						alert("Please upload below 1 MB PDF file");
						//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
						$(this).val('');
						$(this).focus();
						count++;
					 return false;
					}
		        	
			   /*      if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
				        ext= fileName.substring(fileName.lastIndexOf(".")+1); */
			   
			        if (!(regex.test(fileName))) {
			            $(this).val('');
			            alert('Please select correct file format');
			            count++;
			            return false;
			        }
		        }
		    });
		//alert(countUploadedFiles);
		if(countUploadedFiles==0){
			 alert('Please select at least one file.');
			 return false;
		}
		if(count==0){
		  return true;}
		else{
			return false;}
	}//validateFileExtention method
	function fromDateChange(id){
		var x=$('#from_date_location'+id).datepicker("getDate");
		$("#to_date_location"+id).datepicker( "option", "minDate",x ); 	
	}
	function toDateChange(id){
		var x=$('#to_date_location'+id).datepicker("getDate");
		$("#from_date_location"+id).datepicker( "option", "maxDate",x ); 	
	}
 </script>	
</body>
</html>
