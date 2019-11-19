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
.tbl-width-fix1{width:1340px !important;}
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
.custom-combobox > input {width:80%;height:30px;height: 30px;
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
    width: calc( 100% - 17px ) !important; */
}
.tbl-width-fix {
    width: 4800px !important;
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

<!-- Modal popup for add remove -->
    
</style>
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
		<script src="js/sidebar-resp.js"></script>
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="CacheClear.jsp" />  
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
    	    div.setAttribute("style", ""); 
    	    
    	    
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
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
		    div.setAttribute("style", ""); 
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		   
		      
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
		    div.setAttribute("readonly", 'readonly');
		    div.setAttribute("style", "");  
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		   var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-percent");
		    btn.setAttribute("style", "margin-top: -21px;margin-left: 200px;");
		    cell.appendChild(btn);
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
		    div.setAttribute("style", ""); 
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
		    div.setAttribute("placeholder", "Please enter Amount");
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
					<div class="">
					   <form:form modelAttribute="indentReceiveModelForm" id="doInventoryFormId" class="form-horizontal" enctype="multipart/form-data">
						<div class="border-indent">
						  <div class="col-md-12">
							  <div class="col-md-4">
							     <div class="form-group">
									<label class="control-label col-md-6">Vendor Name : </label>
									<div class="col-md-6" >
										<form:input path="VendorName" id="VendorNameId" class="form-control" />
									</div>
								 </div>
							  </div>
							  <div class="col-md-4">
							    <div class="form-group">
									<label class="control-label col-md-6">GSTIN : </label>
									<div class="col-md-6">
										<form:input path="GSTINNumber" id="GSTINNumber" class="form-control" autocomplete="off" readonly="true" />
									</div>
								</div>
							</div>
							<div class="col-md-4">
							   <div class="form-group">
								  <label class="control-label col-md-6">Vendor Address : </label>
									<div class="col-md-6" >
										<form:input path="VendorAddress" id="VendorAddress" class="form-control" readonly="true" />
									</div>
								</div>
							</div>
							<div class="col-md-4">
								 <div class="form-group">
								    <label class="control-label col-md-6">Site : </label>
									<div class="col-md-6" >
										<form:input path="" id="SiteAddres" class="form-control" readonly="true" value="${siteName}"/>
									</div>
								 </div>
							</div>
							<div class="col-md-4">
							    <div class="form-group">
									  <label class="control-label col-md-6">Payment Req Days : </label>
										<div class="col-md-6" >
											<input type="text" id="days" name="days" class="form-control "  onkeypress="return isNumberCheck(this, event)" >
									    </div>
								 </div>
								 <div class="col-md-6 col-xs-12" >
									    <input type="hidden" id="vendorId" name="vendorId" class="form-control"  />
									    <input type="hidden" name="strCreationDate" id="strCreationDate"   class="form-control" value=""/>
										<input type="hidden" name="SiteName" id="siteName"   class="form-control" value="${siteName}"/>
										<input type="hidden" name="indentNumber" id="indentNumber"   class="form-control" value="${indentNumber}"/>
										<input type="hidden" name="siteWiseIndentNo" id="siteWiseIndentNo"   class="form-control" value="${siteWiseIndentNo}"/>
										<input type="hidden" name="toSiteId" id="toSiteId"   class="form-control" value=""/>
										<input type="hidden" name="siteId" id="siteId"   class="form-control" value="${site_id}"/>
										<input type="hidden" name="countOftermsandCondsfeilds" id="counttermsId"   class="form-control" value=""/>
										<input type="hidden" name="siteId" id="siteIdId"  readonly="true" class="form-control" value="${site_id}"/>
										<input type="hidden" id="allSiteIds" value="${Allsites}"/>
								 </div>
							</div>
	                     </div>
                       </div> 
                       <!-- ******************************************** -->
			           <div class="clearfix"></div>
					   <div class="table-responsive sticky-table sticky-headers sticky-ltr-cells">
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
					    				<th><%= taxOnOtherOrTransportCharges %></th>
					    				<th><%= otherOrTransportChargesAfterTax %></th>
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
											<form:select path="ChildProduct1" id="comboboxsubSubProd1" class="form-control  btn-visibilty1 btn-loop1"/>
										</td>
										<td>
					                        <input type="text" maxlength="500"  id="childProductVendorDesc1"   name="childProductVendorDesc1" onblur="calculateTotalAmount(1)"   class="form-control" value=""/> 
										</td>
										<td>
											<form:select path="UnitsOfMeasurement1" id="UnitsOfMeasurementId1" onchange="return validateProductAvailability(this);" class="form-control  btn-visibilty1 btn-loop1"/>
										</td>
										<td>
											<form:input path="Quantity1" id="QuantityId1" onkeypress="return validateNumbers(this, event);" onblur="calculateTotalAmount(1)" class="form-control  btn-visibilty1 btn-loop1" autocomplete="off"/>
					                        <%-- <form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/> --%>
										 <input type="hidden"  id="avalBOQQty1"    name="avalBOQQty"/>
					     				<input type="hidden" id="BOQQty1" class="BOQQty">
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
										<td>  <!-- width="70%" style="border: 1px solid #312d2d;" -->
										<input type="text" id="Discount1" autocomplete="off"  name="Discount1"  onblur="calculateDiscountAmount(1);"  class="form-control disable-class1" value=""/><i  style="margin-top: -21px;margin-left: 200px;" class="fa fa-percent"></i>
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
											<form:input path="HSNCode1" id="HSNCodeId1" onkeydown="appendRow()" class="form-control" autocomplete="off"/>
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
											<input type = "hidden"  id="isNewOrOld1"  name="isNewOrOld1"  class="form-control"  autocomplete="off"  value="new" readonly="true"/>
											<input type="hidden"  name="groupId1"  id="groupId1" type="hidden"  value="" class="form-control" readonly="true"/>
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
			           <div class="clearfix"></div>
			<div class="table-responsive Mrgtop20">
			<table id="doInventoryChargesTableId" class="table fixedTblHeader tbl-width-fix1">
			<thead class="calTheadColor">
				<tr >
					<th class="tblSlno"><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
     				<th><%= trasportInvoice %></th>
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
						<form:input path="ConveyanceAmount1" id="ConveyanceAmount1" name="ConveyanceAmount1" type="number"  placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off" onkeyup="calculateGSTTaxAmount(1)"/>
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
						<form:input path="GSTAmount1" id="GSTAmount1" name="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
						<td>
						<form:input path="AmountAfterTaxx1" type="number"  name="AmountAfterTaxx1" id="AmountAfterTaxx1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off" readonly="true"/>
					</td>
				
					<td>
						<form:input path="TransportInvoice1"  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
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
			
		<div class="Mrgtop10">
			 <div class="col-md-6 col-md-offset-6 Mrgtop10">
				<span class="h4 col-md-7 text-right"><strong>Final Amount:</strong></span> 
				<span id="finalAmntDiv" name ="finalAmntDiv" class="finalAmountDiv col-md-5 text-right" ></span>
			</div>
		</div> <br><br>
		<div class="col-md-12">
		  <div class="" style="margin-top: 50px;">						
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
		   
			<br/>
			<div class="form-group clearfix ">
					<div class="col-md-12 text-center center-block form-submit" style="margin-bottom: 5px !important;margin-top: 60px !important;">
					<!-- <div class="Btnbackground"> -->
						<input class="btn btn-warning" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
						<!-- <input class="tblchargesbtn" type="button" value="View GRN" id="viewGrnBtnId" onclick="viewGRN()"> -->
						<input class="tblchargesbtn btn btn-warning" type="button" value="Save" onclick="createModelPOPUP()" id="saveBtnId">
					<!-- </div> -->
				</div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
			</div>
			
							
			
		   
		  
	<!-- **************Modal POPUP*********** -->
			
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
       
       
        
    
     
    
        
       
     <div class="col-md-12 appen-div-workorder">
     
     <c:forEach items="${listTermsAndCondition }" var="item">
      <div class="col-md-12 remove-filed">
      <div class="col-md-10">
      
      <input type="text" name="termsAndCond" class="form-control workorder_modal_text myinputstyles" value="${item.strTermsConditionName}"/>
      </div>
      <div class="col-md-2 margin-close">
      <button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
      </div>
     
      </div>
      </c:forEach>
      
      </div>
         <div class="col-md-12 padding-div">
       <div class="input_fields_wrap">
         <div class="col-md-10"><input type="text" class="form-control workorder_modal_text myinputstyles" id="workorder_modal_text1" name="termsAndCond" value=""/></div>
         <div class="col-md-2 margin-close"><button type="button" class="btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
        
      </div>
    </div>
    
           <br>
         
            
            
            </div>
        </div>
	</div>
</div>
        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
        <div class="emailCC">
        	<input type="checkbox" name="check1" id="ccemailcheckbox" onclick="dynInput(this);" style="position: absolute;margin-left: -522px;" value="${ccEmails}" /><input type="text" id="" name="ccEmailId" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px"/><span style="position: absolute;margin-left: -501px;">(Optional)If you want to add CC In emails.</span><br>
     		
        </div>
        
        <div class="subject-container" style="margin-top: 48px;">
        <input type="checkbox" name="check2" id="sub-conmatiner"  style="position: absolute;margin-left: -522px;" value="" /><span style="position: absolute;margin-left: -501px;">Subject</span><br>
      	<!-- <input type="text" name="subject" id="" class=" form-control1 sub-control" Placeholder="Please enter the subject" style="float: left;margin-top: 3px;"/> --><br>
      	<input type="text" id="" name="subject"  class="form-control" Placeholder="Please enter the subject" style="float: left;"/>
        </div>
        
          <button type="button" class="btn btn-warning  onclick="saveRecords('SaveClicked')"	 form-submit" style="margin-right: 363px;width: 121px;margin-top: 67px;" data-dismiss="modal" onclick="createPO()">Submit</button>
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
<script src="js/CreatePoSiteLevel.js" type="text/javascript"></script>
		
		
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
					 var GSTINNumber=$("#GSTINNumber").val();
					var VendorAddress=$("#VendorAddress").val();
					if(GSTINNumber=="" || VendorAddress==''){
						alert("Please enter valid Vendor Name.");
						$("#VendorNameId").focus();
						return false;
					} 
			 		var transportDetails = document.getElementById("Conveyance1").value;
			 		if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
			 			alert("Please Select the Conveynace or None .");
			 			document.getElementById("Conveyance1").focus();
			 			return ;
			 		} else if(transportDetails !== "" || transportDetails !== '' || transportDetails !== null){
			 		$('.modal').modal('show');

			 		}
			 	}; 


			 	function createPO() {
			 		
			 		//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
			 		
			 		//var valStatus = appendRow();

			 		 var countOftermsandCondsfeilds = document.getElementsByClassName("myinputstyles");
   					
			 		 $("#counttermsId").val(countOftermsandCondsfeilds.length);
			 		
			 		 
			 		var canISubmit = window.confirm("Do you want to Submit?");
			 		
			 		if(canISubmit == false) {
			 			return;
			 		}
			 		
			 		//document.getElementById("saveBtnId").disabled = true;	
			 		//document.getElementById("countOfRows").value = getAllProdsCount();	
			 		document.getElementById("countOfRows").value = getAllProdsCount();
			 		document.getElementById("countOfChargesRows").value = getAllChargesCount();
			 		document.getElementById("doInventoryFormId").action ="SaveSiteLevelPoDetails.spring";
			 		document.getElementById("doInventoryFormId").method = "POST";
			 		document.getElementById("doInventoryFormId").submit();
			 	}
	 	
				/* ************* Method for new text feild in model popup ************** */
				
	
			
			 	
 </script>
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
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-10"><input type="text" name="termsAndCond" value="'+tc+'" class="form-control workorder_modal_text"/></div><div class="col-md-2 margin-close"><button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
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
	$('.file1').change(function(){
		if((this.files[0].size/1024)<=1024){
			$('.btn-close1').show();
			}else{
				alert("Please Upload Below 1 mb Pdf File");
				resetFile1();}
	});
	
	$('.btn-close1').change(function(){
		$('.btn-close1').hide();
	});
	
	$('.file2').change(function(){
		if((this.files[0].size/1024)<=1024){
			$('.btn-close2').show();
			}else{
				alert("Please Upload Below 1 mb Pdf File");
				resetFile2();}
	});
	$('.btn-close2').change(function(){
		$('.btn-close2').hide();
	});
	$('.file3').change(function(){
		if((this.files[0].size/1024)<=1024){
			$('.btn-close3').show();
			}else{
				alert("Please Upload Below 1 mb Pdf File");
				resetFile3();}
	});
	$('.btn-close3').change(function(){
		$('.btn-close3').hide();
	});
	$('.file4').change(function(){
		if((this.files[0].size/1024)<=1024){
			$('.btn-close4').show();
			}else{
				alert("Please Upload Below 1 mb Pdf File");
				resetFile4();}
	});
	$('.btn-close4').change(function(){
		$('.btn-close4').hide();
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
				  $("#ConveyanceAmount"+btn).val(" ");
				  $("#ConveyanceAmount"+btn).removeAttr("readonly");
				  $("#GSTTax"+btn).val("");
				  $("#GSTTax"+btn).removeAttr("readonly");
				  $("#GSTAmount"+btn).val(" ");
				  //$("#GSTAmount"+btn).removeAttr("readonly");
				  $("#AmountAfterTaxx"+btn).val(" ");
				 // $("#AmountAfterTaxx"+btn).removeAttr("readonly");
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
	  
		function start_and_end(str) {
			if (str.length > 25) {
			   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
			}
		  return str;
		}
		function isNumberCheck(el, evt) {
		    var charCode = (evt.which) ? evt.which : event.keyCode;				  
		    if (charCode < 48 || charCode > 57) {
		        return false;
		    }else{
				return true;
			}
		    
		}
	</script>	
</body>
</html>
