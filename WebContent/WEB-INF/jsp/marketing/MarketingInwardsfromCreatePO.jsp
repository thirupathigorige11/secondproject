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
/* css for market inwards */
.form-control{height:34px !important;}
 .breadcrumb {background: #eaeaea !important;}
.marGinBottom{margin-bottom:15px;}
.btnpadd{margin-right:5px;}
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
.table>thead>tr>th{
border-top:1px solid #000 !important;
border-bottom:1px solid #000 !important;
background-color:#ccc;
}
.table tbody tr td{
border:1px solid #000;
}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
    margin-left: 15px;
    font-weight: bold;
}
.invoiceGrandDiv{
  	color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
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
 .pro-table tbody tr ,.pro-table tbody tr th{
	margin:2px 3px;
	width:100%;
	min-width:213px; 
}
/*styles for auto complete*/
 .autocomplete {
  /*the container must be positioned relative:*/
  position: relative;
  display: inline-block;
}
.autocomplete-items {
  position: absolute;
  border: 1px solid #d4d4d4;
  border-bottom: none;
  border-top: none;
  z-index: 99;
  /*position the autocomplete items to be the same width as the container:*/
  top: 100%;
  left: 0;
  right: 0;
}

.autocomplete-items div {
  padding: 10px;
  cursor: pointer;
  background-color: #fff; 
  border-bottom: 1px solid #d4d4d4; 
}

.autocomplete-items div:hover {
  /*when hovering an item:*/
  background-color: #e9e9e9; 
}

.autocomplete-active {
  /*when navigating through the items using the arrow keys:*/
  background-color: DodgerBlue !important; 
  color: #ffffff; 
}

/*styles for auto complte*/
 
</style>

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
  <script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
 <jsp:include page="../CacheClear.jsp" />  
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
		<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
	<style>
		#finalAmntDiv{
	font-size: 24px;
    font-weight: bold;
    margin-left: 14px; 
    
	
	}
	.form-group label{
    text-align: left
}
#CreditAmountDiv{
    font-size: 24px;
    font-weight: bold;
    color: #f0ad4e;
    margin-left: 15px;
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
.width150{
	width:150px;
}
	</style>

 <script>
//Create DIV element and append to the table cell
var i = 1;

function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
     var vfx = fldLength;     
     //removing space
     tableColumnName=tableColumnName.trim();
     var snoColumn =  "<%=serialNumber %>";
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
	 
	 
 	 var expiryDate=  "<%= expiryDate %>";
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
    	    div.setAttribute("onchange", "return appendvalues("+vfx+");");
    	   	cell.appendChild(div);
    	}    	
    	else if(tableColumnName == taxColumn) {
    		/* var dynamicSelectBoxId = "taxAmount"+vfx; */
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("input");
    	    div.setAttribute("name", 'TaxId'+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id", 'TaxId'+vfx);
    	   /*  div.setAttribute("onchange", "calculateTaxAmount("+vfx+")"); */
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
		    div.setAttribute("id", "stQantityPOP"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx);	     */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		    div.setAttribute("onblur", "calculatequantitybaseinpop("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
		    
		   /*  var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);		 */    
   		}    	
    	else if(tableColumnName == priceColumn) {
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "PriceId"+vfx);
		    div.setAttribute("id", "PriceId"+vfx);
		   /*  div.setAttribute("id", tableColumnName+vfx);	    */ 
		   /*  div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		   /*  div.setAttribute("onblur", "calculateTotalAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "BasicAmountId"+vfx);
		    div.setAttribute("id", "BasicAmountId"+vfx);
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		  /*   div.setAttribute("onblur", "calculatePriceAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == discountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "Discount"+vfx);
		    div.setAttribute("id", "Discount"+vfx);
		  /*   div.setAttribute("id", tableColumnName+"Id"+vfx); */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);"); */
		   /*  div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == AmountAfterDiscountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", "amtAfterDiscount"+vfx); 
		    div.setAttribute("id", "amtAfterDiscount"+vfx);
		    /* div.setAttribute("id", tableColumnName+"Id"+vfx); */
		 /*    div.setAttribute("id", "tax"+vfx); */
		    /* div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateDiscountAmount("+vfx+")"); */
		    div.setAttribute("class", 'form-control' );
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
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
		    div.setAttribute("type", "number");
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
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxOnOtherOrTransportChargesColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "TaxOnOtherOrTransportChargesId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == otherOrTransportChargesAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "OtherOrTransportChargesAfterTaxId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == totalAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "TotalAmountId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("class", 'totalAmount');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == hsnCodeColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "HSNCodeId"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
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
/* function abc(valr){
			
			//alert("vfx "+valr);
			  $("#expireDateId"+valr).datepicker({dateFormat: 'dd-M-y'});
			 // alert("ok");
			  
		
		} */
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

	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	
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
<title>Inwards from PO</title>
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

						<jsp:include page="../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="../TopMenu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Inwards PO</li>
						</ol>
					</div>
					
					
					<div>
	<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="col-md-12">
		<div class="container border-inwards">
	<%--  <c:forEach var="poDetails" items="" step="1" begin="0"> --%>	
	 <!--  <div class="col-md-12"> -->
	  <div class="col-md-4">
	   <div class="form-group">
			<label class="col-md-6">Indent For : </label>
			<div class="col-md-6" >
						<select name="indentFor" id="indentFor" class="form-control">
				              <option value="">---Select--</option>
					          <option value="1">Invoice</option>
					          <option value="2">DC</option>
						</select>

			</div>
			</div>
	  </div>
	  <div class="clearfix"></div>
	
		<!-- </div> -->
		<div class="col-md-4">
		  <div class="form-group">
			<label class="col-md-6">Vendor Name : </label>
			<div class="col-md-6" >
				<form:input path="vendorName" name="vendorName" id="VendorNameId" class="form-control" value="TBDB" readonly="true" />
			</div>
			</div>
		</div>
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6">GSTIN : </label>
			<div class="col-md-6">
				<form:input path="strGSTINNumber" name="strGSTINNumber" id="GSTINNumber" class="form-control" value="DHCD4985DFJ" autocomplete="off" readonly="true" />
			</div>
		 </div>
		</div>	 
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6">Vendor Address : </label>
			<div class="col-md-6" >
				<form:input path="vendorAddress" name="vendorAddress" id="VendorAddress" class="form-control" value="NO.385/1,B.R Bhavan,Near Milk Dairy,Immadihalli Main Road,Whitefield,Bangalore -560045" readonly="true" />
			</div>
		 </div>
			
		</div>		
		<!-- need to verify -->	
		<div class="col-md-4">
				<input type="hidden" id="vendorId" name="VendorId" class="form-control" value="" />
		</div>	
		 
		 
		 <!--  need to verify -->
			 <!--  <div class="form-group col-xs-12" style="margin-bottom:22px;">
		</div> -->
		<div class="clearfix"></div>
			  <div class="col-md-4">
			   <div class="form-group">
			  <label class="col-md-6">Indent No : </label>
			<div class="col-md-6" >
				<input type="text" id="sitewiseIndentNumber" name="sitewiseIndentNumber" class="form-control" readonly="true" value="12212">
				<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="">
			</div>
			</div>
			  </div>
			 <div class="col-md-4">
			  <div class="form-group">
			   <label class="col-md-6">Site Name : </label>
			<div class="col-md-6" >
				<input type="text" id=siteName" name="SiteName" class="form-control" readonly="true" value="Acropolis">
				<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="">
			</div>
			  </div>
			 </div>
		 	<div class="col-md-4">
		 	 <div class="form-group">
		 	  <label class="col-md-6">Invoice/DC Number :</label>
			<div class="col-md-6" >
				<input type="text" name="InvoiceorDCNumber" id="InvoiceNumberId" class="form-control" autocomplete="off" value="121212">
				<span id="errorMessageInvoiceNumber" style="display:none; color:red">* Already exist the invoice/DC number</span>
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
		 	 </div>
		 	</div>
			
					 	
			
		
		<div class="col-md-4">
		  <div class="form-group">
					  	<label class="col-md-6">Invoice/DC Date :  </label>
			<div class="col-md-6" >
				<input type="text" name="InvoiceorDCDate" id="InvoiceDateId" class="form-control" autocomplete="off" value="">
			</div>
			</div>
		</div>
		<div class="col-md-4">
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
		</div>
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6">PO No : </label>
			<div class="col-md-6" >
				<input type="text" name="poNo" id="PONOId" class="form-control" autocomplete="off" readonly="true" value="PO/SIPL/Hyd/50/47/2018-19/47" title="PO/SIPL/Hyd/50/47/2018-19/47">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
		 </div>
		</div>
		  <div class="col-md-4">
		   <div class="form-group">
		  <label class="col-md-6">PO Date :   </label>
			<div class="col-md-6" >
				<input type="text"  name="poDate" id="poDateId" class="form-control" readonly="true" value="">
			</div>
			</div>
		  </div>
		 	<div class="col-md-4">
		 	 <div class="form-group">
		 	  <label class="col-md-6">E Way Bill No : </label>
			<div class="col-md-6" >
				<input type="text"  name="eWayBillNo" id="eWayBillNoId" class="form-control" value=""}>
				<!--  <input type="hidden"name="eWayBillNo" id="eWayBillNoId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
		 	 </div>
		 	</div>
			<div class="col-md-4">
			 <div class="form-group">
			  <label class="col-md-6">Vehicle No : </label>
			<div class="col-md-6" >
				<input type="text" name="vehileNo" id="vehileNoId" class="form-control" value="">
				<!-- <input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
			 </div>
			</div>
		<div class="col-md-4">
		 <div class="form-group ">
			<label class="col-md-6">Transporter Name :  </label>
			<div class="col-md-6" >
				<input type="text"  name="transporterName" id="transporterNameId" class="form-control" value="">
			</div>
			</div>
		</div>
		<div class="col-md-4">
		 <div class="form-group">
		  <label class="col-md-6">Received Date : </label>
			<div class="col-md-6" >
				<input type="text"  name="receivedDate" id="receivedDate" class="form-control" autocomplete="off" value="">
				<!-- <input type="hidden"name="eWayBillNo" id="eWayBillNoId" class="form-control" readonly="true" value=${SiteId}> -->
			</div>
		 </div>
		</div>		
							  
			
		 	
			
		
		<%-- </c:forEach> --%>
		</div>
	<!-- </div>
		</div>
		</div> -->
	
<!-- </div>
</div> -->

	      <div class="clearfix"></div>
	      <div class="">
	       <div class="table-responsive Mrgtop20">    <!--  protbldiv -->
	   
				<table id="doInventoryTableId-Main" class="table pro-table" style="width:4396px;">
				<thead>
				 <tr>
					<th>S.NO</th>
    				<th style="width:200px;">Product</th>
    				<th style="width:200px;">Sub Product</th>
    				<th style="width:300px;">Child Product</th>    								
    				<th>Units Of Measurement</th>
    				<th>Quantity</th>
    				<th>Received Quantity</th> 
    				 <th>PO Quantity</th>
    				<th> Price</th>
    				<th> Basic Amount</th>
    				<th>Discount</th>
    				<th>Amount After Discount</th>
    				<th> Tax </th>
    				<th> HSN Code </th>
    				<th>Tax Amount</th>
    				<th>Amount After Tax</th>
    				<th> Other Or Transport Charges</th>
    				<th>Tax On Other Or Transport Charges</th>
    				<th> Other Or Transport Charges After Tax </th>
    				<th> Total Amount </th>
    				<th> Remarks </th>
					 <th>Expire Date</th>  
					<!-- <th>Actions</th> -->
				</tr>
				</thead>
				<tbody>
				<%-- <input type="hidden" id="" value="${requestScope['listOfGetProductDetails'].size()}" /> --%>
				  <%-- <c:forEach var="GetProductDetails" items="" step="1" begin="0">	 --%>
	        	 	
	        	 	
	        	 	<%-- <input type="hidden" id="indentCreationDetailsId${GetProductDetails.serialno}"  name ="mainindentCreationDetailsId${GetProductDetails.serialno}"  onblur="calculateTaxAmount(${GetProductDetails.serialno})" class="form-control" value="${GetProductDetails.indentCreationDetailsId}"/>
	        	 	 --%>
	        	 	
	        	 	
	        	 	 <tr id="tr-class(1)">
	        	 	<td>	
	        	  						
				       <div id="mainsnoDivId(1)">1</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
					<td>
						<select id="product" readonly="true" name="mainProduct" class="form-control  btn-visibilty btn-loop1 value="">
						<option value="">Chemicals</option>	
							<%-- <option value="">${GetProductDetails.product}</option> --%>
					    		<%-- <%			
					    		
					    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
					    			for(Map.Entry<String, String> prods : prod.entrySet()) {
									String prodIdAndName = prods.getKey()+"$"+prods.getValue();
								%> --%>
									<option value="">Safety</option>
					    		<%-- <% } %> --%>
						</select>
						
					</td>
					
					
					
					
					<td>
					<form:select path="" id="subProduct"  readonly="true" name="mainSubProduct" class="form-control  btn-visibilty btn-loop">
					<option value="">Cement Type</option>
					</form:select>
					</td>
					<td>
					<form:select path="" id="childProduct" readonly="true" name="mainChildProduct" class="form-control  btn-visibilty btn-loop childproduct">
						<option value="Fixo Block Ultratech">Fixo Block Ultratech</option>
					</form:select>
					</td>
					
					<td>
					<form:select path="" id="mainUnitsOfMeasurementId" readonly="true" name="mainUnitsOfMeasurement"  class="form-control  btn-visibilty btn-loop"  value="">
						<option value="">Bag</option>
					</form:select>
					</td>
					  <td >
                        <input type="text"  id="mainQantity"  class="form-control qtyinput"  name="mainquantity" onfocusout ="validateQuantity(1)" onkeypress="return isNumber(event)" tabindex="" value=""/> 
                        <input type="hidden"  id="hiddenQantity"     class="form-control" value=""/> 
    			    </td>	
    			    <td >
                        <input type="text"  name="ReceivedQty" id="ReceivedQty" readonly="true" class="form-control" value="5"/> 
                    </td>                  			
					<td >
                        <input type="text"  readonly="true" class="form-control" value="10.0"/> 
                    </td>
					<td>
					<input type="text" id="mainprice"  name ="mainPrice"  readonly="true" onblur="calculateTotalAmount(1)"  class="form-control" value="100"/>
					</td>
					<td> 
					<input type="text" id="mainBasicAmountId"  name ="mainBasicAmount" readonly="true" onblur="calculateTaxAmount(1)" class="form-control" value="0"/>
					</td>
						<td> 
					<input type="text" id="mainDiscount" name="mainDiscount  onblur="calculateDiscountAmount(1);" readonly="true" class="form-control disable-class1" value="0"/><i  style="position:absolute;right:16px;top:16px;" class="fa fa-percent"></i>

					
					</td>
					
					<td>
					<input type="text" id="mainamtAfterDiscount"  name ="mainamtAfterDiscount"  readonly="true" class="form-control" value="0"/>
					</td>
					
					
					<td>
						<form:select path="" id="maintaxAmount"  name ="maintax" readonly="true"   onchange="calculateTaxAmount(1)" class="form-control bt-taxamout-coursor" >
							<form:option value="" >28%</form:option>
					    		<%-- <%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%> --%>
									<form:option value="">22%</form:option>
					    		<%-- <% } %> --%>
						
						
						</form:select>
					</td>
					<td>
					<input type="text" id="hsnCode"  name ="mainhsnCode"  readonly="true" class="form-control" value="8556"/>
					</td>
					<td>
					<input type="text" id="mainTaxAmount"  name ="maintaxAmount"  readonly="true" class="form-control" value="0"/>
					</td>
					<td>
					<input type="text" id="mainTaxAftertotalAmount"  name ="mainamountAfterTax"  readonly="true" class="form-control" value="0"/>
					</td>
					<td>
					    <input type = "text" id="mainOtherOrTransportChargesId"  name="mainotherOrTransportCharges"  readonly="true" class="form-control" value="0.00"/>
					</td>
					<td>
			         	<input  type="text" id="mainTaxOnOtherOrTransportChargesId"  name="maintaxOnOtherOrTransportCharges"  readonly="true"  class="form-control" value="0.00"/>
					</td>
					<td>
					<input type="text" id="mainOtherOrTransportChargesAfterTaxId"  name="mainotherOrTransportChargesAfterTax"  readonly="true" class="form-control" value="0.00"/>
					</td>
					
					
					
					<td>
					    <input type="text"   class='ttamount form-control' id='mainTotalAmountId'  name='maintotalAmount'    readonly="true" class="form-control"  value="0.00"/>
					 <input type="hidden"   id='indentCreationDetailsId'  name='mainindentCreationDetailsId'    readonly="true" class="form-control"  value=""/>
					<input type="hidden"   id='poEntryDetailsId'  name='mainPoEntryDetailsId'    readonly="true" class="form-control"  value=""/>
					</td>
					<td>
					  <input type="text" id="InvoiceRemarks"  name="InvoiceRemarks"    class="form-control" />
					</td>	
					 <td>
					  <input type="text" id="expireDate"  name="expireDate"    class="form-control ExpiryDate" autocomplete="off"/>
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
				
				</tr>
		 	<%-- </c:forEach>  --%>
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
				
				<%-- <input type="checkbox" id="creditnote" name="creditnote" style="float: left" data-toggle="modal" data-target="#myModal"/><span style="float:left;color:blue;font-weight:bold;font-family:14px;"> (Optional)Please click,If you want to return the products.</span>
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content" style="width: 1205px;margin-left: -139px;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Credit Note</h4>
        </div>
        <div class="modal-body">
        	<div class="col-md-4 col-md-offset-3"><div class="form-group">
        	 <label class="col-md-6">Credit Note Number</label>
        	 <div class="col-md-6"><input type="text" name="creditNoteNumber" id="creditNoteNumber" size="3" class="form-control" autocomplete="off" value=""></div>
        	</div></div>
        	<div class="clearfix"></div>
        	<div class="table-responsive protbldiv Mrgtop20">  
				<table id="doInventoryTableId" class="table pro-table tbl-width-inwards" ">
				<thead>
				 <tr >
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
					<th>Actions</th>
				</tr>
				</thead>
	            <tbody>
	             	 	 <tr id="">
	        	 		<td>						
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
						<form:select path="" id="comboboxsubProd1" name="SubProduct1" class="form-control  btn-visibilty1 btn-loop1"/> 
					</td>
					<td>
						<form:select path="" id="comboboxsubSubProd1" name="ChildProduct1" class="form-control  btn-visibilty1 btn-loop1"/> 
					</td>
					<td>
						<form:select path="" id="UnitsOfMeasurementId1"  name="UnitsOfMeasurement1" onchange="return appendvalues(1);" class="form-control  "/>
					</td>
					<td  class="s-50">
						 <input type="text"  id="stQantityPOP1"    name="quantity1" onblur="calculatequantitybaseinpop(1)"  class="form-control" value="10"/>  
                        <form:input path="" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td>					
					<td>
						<form:input path="" id="PriceId1" name="PriceId1" onkeypress="return validateNumbers(this, event);" value="20"  class="form-control"/>
					</td>
					<td>
						<form:input path="" id="BasicAmountId1" name="BasicAmountId1" class="form-control"  onkeypress="return validateNumbers(this, event);" />
					</td>
						<td>
						<form:input path="" id="Discount1" name="Discount1" class="form-control"  onkeypress="return validateNumbers(this, event);" value="10"  />
					</td>
						
		
					<td>
						<form:input path="" id="amtAfterDiscount1" name="amtAfterDiscount1" class="form-control"  onkeypress="return validateNumbers(this, event);" value="" />
					</td>
					
					<td>
						<form:input path="" id="TaxId1" name=""  class="form-control" value=""/>
							<form:option value="${GetProductDetails.taxId}$${GetProductDetails.tax}" >${GetProductDetails.tax}</form:option>
					    
					</td>
					
					<td>
						<form:select path="" id="TaxId1"  name ="TaxId1"    onchange="calculateChangedTaxAmount(1)" class="form-control bt-taxamout-coursor${GetProductDetails.serialno}" >
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
						<form:input path="" id="HSNCodeId1" name="HSNCodeId1"  class="form-control" autocomplete="off"/>
					</td>
					<td>
						<form:input path="" id="TaxAmountId1" name="TaxAmountId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="" id="AmountAfterTaxId1" name="AmountAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="" id="OtherOrTransportChargesId1" name="OtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="" id="TaxOnOtherOrTransportChargesId1" name="TaxOnOtherOrTransportChargesId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="" id="OtherOrTransportChargesAfterTaxId1" name="OtherOrTransportChargesAfterTaxId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<input type="text"   class='form-control totalAmount' id='TotalAmountId1'  name='TotalAmountId1'    readonly="true" class="form-control"  value=""/>
					<td>
			<!-- 		<td>
						<input type="text"   class='form-control' id='indentCreationDtlsId1'  name='indentCreationDtlsId1'    readonly="true" class="form-control"  value=""/>
					<td>
					<td>
						<input type="text"   class='form-control' id='poEntryDetailsId1'  name='indentCreationDtlsId1'    readonly="true" class="form-control"  value=""/>
					<td> -->
						<!-- <input type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId" onclick="appendRow()" /> -->
						<button type="button" name="addNewItemBtn" id="addNewItemBtnId1" onclick="appendRow(1)" class="btnaction"><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
					</td>
				</tr>
				<!--  start -->
				
	<!-- 			<tr><td><div id="snoDivId2">2</div></td><td><select name="Product2" id="combobox2" style="display: none;"><option value="">Select one...</option><option value="PROD001$Administrative">Administrative</option><option value="PROD003$Blockwork">Blockwork</option></select><span class="custom-combobox"><input title="" id="Product2" class="ui-autocomplete-input" autocomplete="off"><span role="status" aria-live="polite" class="ui-helper-hidden-accessible">2 results are available, use up and down arrow keys to navigate.</span><a tabindex="-1" title="Show All Items" class="ui-button ui-widget ui-state-default ui-button-icon-only custom-combobox-toggle ui-corner-right" role="button" aria-disabled="false"><span class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span><span class="ui-button-text"></span></a></span></td><td><select name="SubProduct2" id="comboboxsubProd2" style="display: none;"><option value="">--Select--</option><option value="SP009$Brick">Brick</option></select><span class="custom-combobox"><input title="" id="SubProduct2" class="ui-autocomplete-input" autocomplete="off"><span role="status" aria-live="polite" class="ui-helper-hidden-accessible">1 result is available, use up and down arrow keys to navigate.</span><a tabindex="-1" title="Show All Items" class="ui-button ui-widget ui-state-default ui-button-icon-only custom-combobox-toggle ui-corner-right" role="button" aria-disabled="false"><span class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span><span class="ui-button-text"></span></a></span></td><td><select name="ChildProduct2" id="comboboxsubSubProd2" style="display: none;"><option value="">--Select--</option><option value="CHP0194$Red Brick ">Red Brick </option></select><span class="custom-combobox"><input title="" id="ChildProduct2" class="ui-autocomplete-input" autocomplete="off"><span role="status" aria-live="polite" class="ui-helper-hidden-accessible">1 result is available, use up and down arrow keys to navigate.</span><a tabindex="-1" title="Show All Items" class="ui-button ui-widget ui-state-default ui-button-icon-only custom-combobox-toggle ui-corner-right" role="button" aria-disabled="false"><span class="ui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span><span class="ui-button-text"></span></a></span></td><td><select name="UnitsOfMeasurement2" class="form-control" id="UnitsOfMeasurementId2" onchange="return appendvalues(2);"><option value="">--Select--</option><option value="MST0194$Nos">Nos</option></select></td><td class="s-50"><input type="text" name="quantity2" id="stQantityPOP2" onblur="calculatequantitybaseinpop(2)" class="form-control"></td><td><input type="text" name="PriceId2" id="PriceId2" class="form-control"></td><td><input type="number" name="BasicAmountId2" id="BasicAmountId2" class="form-control"></td><td><input type="number" name="Discount2" id="Discount2" class="form-control"></td><td><input type="number" id="amtAfterDiscount2" class="form-control"></td><td><input name="TaxId2" class="form-control" id="TaxId2"></td><td><input type="text" name="hsnCode2" id="HSNCodeId2" class="form-control"></td><td><input type="number" name="TaxAmountId2" id="TaxAmountId2" readonly="true" class="form-control"></td><td><input type="number" name="amountAfterTax2" id="AmountAfterTaxId2" readonly="true" class="form-control"></td><td><input type="text" name="OtherOrTransportCharges2" id="OtherOrTransportChargesId2" readonly="true" class="form-control"></td><td><input type="text" name="TaxOnOtherOrTransportCharges2" id="TaxOnOtherOrTransportChargesId2" readonly="true" class="form-control"></td><td><input type="text" name="OtherOrTransportChargesAfterTax2" id="OtherOrTransportChargesAfterTaxId2" readonly="true" class="form-control"></td><td><input type="text" name="totalAmount2" id="TotalAmountId2" readonly="true" class="form-control"></td><td><button type="button" name="addNewItemBtn" id="addNewItemBtnId2" value="Add New Item" onclick="appendRow(2)" class="btnaction"><i class="fa fa-plus"></i></button> <button type="button" name="addDeleteItemBtn" id="addDeleteItemBtnI
d2" value="Delete Item" onclick="deleteRow(this, 2)" class="btnaction"><i class="fa fa-trash"></i></button></td></tr>
				 --><!--  end -->
		
		
			

		
			
		 	</c:forEach> 
	            </tbody>	 	
	        	 	
	        
	</table>
	</div> 
        </div>
        <div class="modal-footer">
         <div class="text-center center-block">
           <button type="button" class="btn btn-danger" data-dismiss="modal">Submit</button>
         </div>
        </div>
      </div>
      
    </div>
  </div>
				 --%>
<!-- ************************************************Marketing Inwards PO Screen***************************************** -->
 <a href="" class="anchor-market" id="anchorMarket" onclick="openCalExp()">Calculate Expenditure</a>
 
<!-- ************************************************Marketing Inwards PO Screen***************************************** -->			
				
				
			<!-- *****************************************************************************************	 -->
				
							<div class="clearfix"></div><br/>
			<div class="table-responsive "> <!-- protbldiv -->
			<table id="doInventoryChargesTableId" class="table tbl-width-inwards-half"> <!-- pro-table -->
				<thead>
				 <tr>
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
    				<th>Actions</th>
  				</tr>
				</thead>
			<tbody>
			 <%-- <c:forEach var="GetTransportDetails" items="" step="1" begin="0"> --%>
				<tr id="chargesrow(1)">
					<td>						
						<div id="snoChargesDivId(1)">1</div>
					</td>
					<td>						
							<form:select path="" id="Conveyance" name="Conveyance" class="form-control" readonly="true" >
							<form:option value="">None</form:option>
							<form:option value="">Transportation Charges</form:option>
							<form:option value="">Other Charges</form:option>
							
							
					   		<%-- <%	
					    			Map<String, String> conveyanceCharges1 = (Map<String, String>)request.getAttribute("chargesMap");
					    			for(Map.Entry<String, String> tax : conveyanceCharges1.entrySet()) {
					    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%> --%>
									<form:option value="">Clearence Charges</form:option>
					    		<%-- <% } %>  --%>
						</form:select>
					</td>
					<td>
					<%-- <%
					
					int i=1;
					%> --%>
						<form:input path="" id="ConveyanceAmount" name="ConveyanceAmount" type="number" onchange="calculateGSTTaxAmount(1)" placeholder="Please enter Amount" value="0" class="form-control noneClass" autocomplete="off"/>
					</td>
					<td>
						<form:select path="" id="GSTTax" name="GSTTax" class="form-control GSTClass " value="5%" onchange="calculateGSTTaxAmount(1)" readonly="true" >
							<form:option value="">0%</form:option>
							<form:option value="">5%</form:option>
							<form:option value="">12%</form:option>
							<form:option value="">18%</form:option>
					    		<%-- <%	
					    			Map<String, String> conveyanceTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : conveyanceTax1.entrySet()) {
					    				String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%> --%>
									<form:option value="">28%</form:option>
					    		<%-- <% } %>  --%>
						</form:select>
					</td>
						<td>
						<form:input path="" id="GSTAmount" name="GSTAmount" type="number" placeholder="GST Amount" value="0" readonly="true" class="form-control noneClass" autocomplete="off"/>
					</td>
						<td>
						<form:input path="" type="text"  name="AmountAfterTax" id="AmountAfterTax" placeholder="Amount After Tax"  value="2" readonly="true" class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice${GetTransportDetails.strSerialNumber}" name="TransportInvoice${GetTransportDetails.strSerialNumber}" placeholder="Transport Invoice Number"  onkeydown="appendRow()" readonly="true" class="form-control" autocomplete="off"/>
					</td>
					
					
					  
					<td>
						<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button> 
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
				<%-- </c:forEach> --%>
			</tbody>
			</table>
			</div>
			
<div class="Mrgtop20 no-padding-left">
 <div class="col-md-6 no-padding-left">
  <div class="file-upload" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
	<input type="file" name="file"><br><input type="file" name="file"><br>
	<input type="file" name="file"><br><input type="file" name="file"><br>
</div>
 </div>

<!-- 
***************************************************note for customer requirement***************************************** -->
<div class="col-md-6 no-padding-left">
 <label class="control-label col-md-3 no-padding-left">
							<%= note %> <%= colon %>
					</label>
					<div class="col-md-9 no-padding-left">
						<textarea name="Note" id="NoteId" class="form-control" style="resize:vertical;"></textarea>
 
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
																	 	
				<div class="clearfix"></div>
			<div class="col-md-12 text-right hidden-xs hidden-sm">
					<div class="col-md-5 col-md-offset-7 text-right" style="display:none;"><div class="col-md-5"><strong>PO Grand Total</strong></div><div class="col-md-1"><strong>:</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-md-6"  ></div></div>
					<div class="col-md-5 col-md-offset-7 text-right" ><div class="col-md-5 text-left"><strong>Products Amount</strong></div><div class="col-md-1"><strong>:</strong></div><div class="col-md-5 finalAmountDiv" id="finalAmntDiv" name ="finalAmntDiv"  ><strong></strong></div></div>
					<div class="col-md-5 col-md-offset-7 text-right" ><div class="col-md-5 text-left"><strong>Credit Note</strong></div><div class="col-md-1"><strong>:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-md-5" >0</div></div>
					<div class="col-md-5 col-md-offset-7 text-right"><div class="col-md-5 text-left"><strong>Total Invoice Amount</strong></div><div class="col-md-1"><strong>:</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-md-5" ></div></div>
				<input type="hidden" id="1" value=""/>
			</div>
		<!-- hidden for desktop devices -->
		<div class="col-xs-12 hidden-md hidden-lg text-left no-padding-left">
					<div class="col-xs-12 no-padding-left" style="display:none;"><div class="col-xs-12"><strong>PO Grand Total :</strong></div><div  id="invoiceGrandDiv" name ="invoiceGrandDiv" class="invoiceGrandDiv col-xs-12 no-padding-left"  ></div></div>
					<div class="col-xs-12 no-padding-left"><div class="col-xs-12 no-padding-left"><strong>Products Amount :</strong></div><div class="col-xs-12 finalAmountDiv no-padding-left" id="finalAmntDiv" name ="finalAmntDiv"  ><strong></strong></div></div>
					<div class="col-xs-12 no-padding-left" ><div class="col-xs-12 no-padding-left"><strong>Credit Note:</strong></div><div id="CreditAmountDiv" name ="CreditAmountDiv" class="col-xs-12 no-padding-left" >0</div></div>
					<div class="col-xs-12 no-padding-left"><div class="col-xs-12 no-padding-left"><strong>Total Invoice Amount :</strong></div><div id="toatlAmntDiv" name ="toatlAmntDiv" class="finalAmountDiv col-xs-12 no-padding-left" ></div></div>
				<input type="hidden" id="1" value=""/>
			</div>
			</div>
			<!-- hidden for desktop devices -->	
			<div class="clearfix"></div>
				<div class="col-md-12 text-center center-block">
				 <!-- <div class="col-sm-2 pt-10">  -->
					<input type="button"  onclick="createInvoice()"  class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()" data-target="myModal" value="Submit" id="saveBtnId" ">
				<!-- </div>
						<div class="col-sm-2 pt-10">  -->
					<input type="button" class="btn btn-warning form-submit modelcreatePO btn-submit-inwards" onclick="maincalculateOtherCharges()" data-target="myModal" value="Calculate" id="saveBtnId" ">
				<!-- </div> -->
				
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
				</div>
			

<!-- **************************************************************		 -->
			
		
			
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
             	<input type="hidden" id= "countOftermsandCondsfeilds" name="countOftermsandCondsfeilds" value="1">
                    <div id="field margintopbtm"><input autocomplete="off" class="input form-control myinputstyles" id="termsAndCond1" name="termsAndCond1" type="text" data-items="8" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc";/><button id="b1" class="add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus" style="border: none;box-shadow: none;border-bottom: 2px solid #ccc"></span></button></div>
                </div>
            <br>
            
            </div>
        </div>
	</div>
</div>
    
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
		<!-- modal popup for marketing module -->
		 <!-- Modal -->
<div id="modal-marketing" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:90%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Calculate Expenditure</strong></h4>
      </div>
      <div class="modal-body">
        <!-- <div class="col-md-12 text-center-left">
   <div class="col-md-4 radio_Market"><input type="radio" id="singleSite" name="marketradio" checked/><label>&nbsp;<strong>Single Site</strong></label></div>
   <div class="col-md-4 radio_Market"><input type="radio" id="siteWise" name="marketradio" /><label>&nbsp;<strong>Site Wise</strong></label></div>
   <div class="col-md-4 radio_Market"><input type="radio" id="brandingWise"name="marketradio" /><label>&nbsp;<strong>Branding Wise</strong></label></div>
 </div> -->
 <div class="clearfix"></div>
 <div class="col-md-12 marGinBottom">
   <div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">Expenditure For: </label>
						<div class="col-md-6" >
							<select class="form-control" id="poToId">
							  <!--  <option value="select">--Select--</option> -->
							   <option value="SiteWise">Site</option>
							   <option value="LocationWise">Multiple Sites</option>
							   <option value="LocationWise_1">Location Wise</option>
							   <option value="BrandingWise">Branding Wise</option>
							</select>
						</div>
					 </div>
				</div>
				<!-- <div class="col-md-4 class_selectsite">
					 <div class="form-group">
					   <label class="col-md-6">Select Site :</label>
					   <div class="col-md-6 autocomplete" style="">
                           <input id="mySites" type="text" class="form-control" name="myCountry" placeholder="Select Site">
                       </div>
					 </div>
				</div> -->
			 <div class="col-md-4 class_selectsite1" style="display:none;">
					 <div class="form-group">
					  <label class="control-label col-md-6">Select Site: </label>
						<div class="col-md-6" >
							<select class="form-control" id="stateSelc">
							   <option selected="true" disabled="disabled">--Select Location--</option>
							   <option value="Ban">Banglore</option>
							   <option value="Hyd">Hyderabad</option>
							</select>
						</div>
					 </div>
				</div>
 </div>
   <!-- Single site -->
    <div id="single_site">
    <!-- <h4><u><strong>Single Site</strong></u></h4> -->
         <!-- <div class="col-md-3 selectsingleSite">
          <select class="form-control">
          <option>-- Please Select Site --</option>
           <option>Acropolis</option>
           <option>Eden Gardens</option>
           <option>Soham</option>
           <option>Kondapur</option>
         </select>
         </div> -->
         <div class="clearfix"></div>
	     <div class="table-responsive">
	     <table class="table">
	       <thead>
	         <tr>
	           <th>Site Name</th>
	           <th>Child Product Name</th>
	           <th>Location</th>
	           <th>Quantity</th>
	           <th>Amount</th>
	         </tr>
	       </thead>
	       <tbody>
	          <tr>
	            <td>Acropolis</td>
	            <td>Acid</td>
	            <td>Hyderabad</td>
	            <td>100</td>
	            <td>10,000</td>
	            
	            
	          </tr>
	          <tr class="totalMarketingAmount">
	           <td colspan="4">Grand Total</td>
	            <td>10,000</td>
	          </tr>
	       </tbody>
	     </table>
	   </div>
	   <div class="text-center center-block">
	    <button type="button" class="btn btn-warning">ADD</button>
	   </div>
   </div>
   <!-- Single site -->
   <!-- Site wise -->
   <div class="clearfix"></div>
   <div id="sitewise_Site" style="display:none;">
   <!-- <h4><u><strong>Site Wise</strong></u></h4> -->
	     <div class="table-responsive">
	     <table class="table" id="expenditureTable">
	       <thead>
	         <tr>
	           <th>S.No</th>
	           <th class="width150">Child Product Name</th>
	           <th class="width150">Location</th>
	           <th>Quantity</th>
	           <th>Amount</th>
	           <th>From Date</th>
	           <th>To Date</th>
	           <th>Site</th>
	           <th style="width:120px;">Actions</th>
	           
	         </tr>
	       </thead>
	       <tbody>
	          <tr id="expenditure1" class="expenditure">
	          	<td>1</td>	           
	            <td>
	             <select id="exapendChildproduct1" class="form-control">
	             </select>
	            </td>
	            <td>
	              <select id="exapendLocation1" class="form-control">
	              	<option>Hyderabad</option>
	              	<option>Banglore</option>
	              </select>
	             </td>
	             <td><input type="text" class="form-control"/></td>
	             <td><input type="text" class="form-control"/></td>
	             <td><input type="text" class="form-control" id="sitewisefromDate" placeholder="Select From Date"/></td>
	            <td><input type="text" class="form-control" id="sitewisetoDate" placeholder="Select To Date"/></td>
	            <td>
	            <select class="form-control">
	              <option>-- Please Select Site --</option>
		           <option>Acropolis</option>
		           <option>Eden Gardens</option>
		           <option>Soham</option>
		           <option>Kondapur</option>
	            </select>
	            </td>
	            <td><button class="btnaction btnpadd" onclick="appendModalRow(1)"><i class="fa fa-plus"></i></button><button class="btnaction"><i class="fa fa-trash"></i></button></td>
	            
	           
	          </tr>
	          <tr class="totalMarketingAmount">
	            <td></td>
	            <td colspan="3">Grand Total</td>
	            <td>10,000</td>
	            <td></td>
	            <td></td>
	            <td></td>
	            <td></td>
	          </tr>
	       </tbody>
	     </table>
	   </div>
	   <div class="text-center center-block">
	    <button type="button" class="btn btn-warning">Calculate</button>
	    <button type="button" class="btn btn-warning">ADD</button>
	   </div>
   </div>
   <!-- Site Wise -->
   <!-- Branding Wise -->
   <div id="branding_site" style="display:none;">
   <!-- <h4><u><strong>Branding Wise</strong></u></h4> -->
	     <div class="table-responsive">
	     <table class="table">
	       <thead>
	         <tr>
	           <th>Site Name</th>
	           <th>Location</th>
	           <th>Child Product Name</th>
	           <th>Quantity</th>
	           <th>Amount</th>
	           
	         </tr>
	       </thead>
	       <tbody>
	          <tr>
	            <td>Acropolis</td>
	            <td>Hyderabad</td>
	            <td>Acid</td>
	            <td>100</td>
	            <td>10,000</td>
	            
	            
	          </tr>
	          <tr class="totalMarketingAmount">
	           <td colspan="4">Grand Total</td>
	            <td>10,000</td>
	            
	          </tr>
	       </tbody>
	     </table>
	   </div>
	   <div class="text-center center-block">
	    <!-- <button type="button" class="btn btn-warning">Calculate</button> -->
	    <button type="button" class="btn btn-warning">ADD</button>
	   </div>
   </div>
   <!-- Branding Wise -->
 
      </div>
      <!-- <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div> -->
    </div>

  </div>
</div>
		<!-- modal popup for marketing module -->
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/marketing/marketingInwards.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/InwardsfromCreatePO.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js" type="text/javascript"></script> 
		
		
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
			
			/* $(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
		
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
 /*Marketing Inwards Script For showing Divs*/ 
 $("#singleSite").click(function (){
  $("#single_site").show(); 
  $("#sitewise_Site").hide();
  $("#branding_site").hide();
 });
	 $("#siteWise").click(function (){
		  $("#single_site").hide(); 
		  $("#sitewise_Site").show();
		  $("#branding_site").hide();
		 });
	 $("#brandingWise").click(function (){
		  $("#single_site").hide(); 
		  $("#sitewise_Site").hide();
		  $("#branding_site").show();
		 });
	 
	 $( "#sitewisefromDate" ).datepicker({
	      changeMonth: true,
	      changeYear: true
	    });
	 $( "#sitewisetoDate" ).datepicker({
	      changeMonth: true,
	      changeYear: true
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
 
  $("#poToId").change(function(){debugger;
	  var PoId=$(this).val();
	  if(PoId=="SiteWise"){
		  $(".class_selectsite").show(); 
		  $("#single_site").show();
		  $("#sitewise_Site").hide();
		  $(".class_selectsite1").hide();
		  $("#branding_site").hide();
		  
	  }
     if(PoId=="LocationWise"){
    	  $(".class_selectsite1").hide();
    	  $("#single_site").hide();
    	  $("#sitewise_Site").show();
    	  $(".class_selectsite").hide();
    	  $("#branding_site").hide();
	  } 
      if(PoId=="BrandingWise"){
    	  $("#branding_site").show();
    	  $("#single_site").hide();
    	  $("#sitewise_Site").hide();
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
	  }
      if(PoId=="select"){
    	  $(".class_selectsite1").hide();
    	  $(".class_selectsite").hide();
    	  $("#single_site").hide();
    	  $("#sitewise_Site").hide();
    	  $("#branding_site").hide();
	  }
      if(PoId=="LocationWise_1"){
    	  $(".class_selectsite1").show();
    	  $(".class_selectsite").hide();
    	  $("#single_site").hide();
    	  $("#sitewise_Site").hide();
    	  $("#branding_site").hide();
	  }
      
  });
  $("#stateSelc").change(function(){
	  var stateId=$(this).val();
	  if(stateId=="Ban"){
		  $("#branding_site").show(); 
	  }
	  if(stateId=="Hyd"){
		  $("#branding_site").show(); 
	  }
  });
   
  
 </script>	
</body>
</html>
