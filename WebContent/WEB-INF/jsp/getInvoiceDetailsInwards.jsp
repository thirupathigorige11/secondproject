<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%
	//Loading Indent Receive Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String serialNumber = resource.getString("label.serialNumber");
	String basicAmount = resource.getString("label.basicAmount");
	String taxPer = resource.getString("label.tax");
	String taxAmount = resource.getString("label.taxAmount");
	String amountAfterTax = resource.getString("label.amountAfterTax");
	String otherOrTransportCharges = resource.getString("label.otherOrTransportCharges");
	String taxOnOtherOrTransportCharges = resource.getString("label.taxOnOtherOrTransportCharges");
	String otherOrTransportChargesAfterTax = resource.getString("label.otherOrTransportChargesAfterTax");
	String sNumber = resource.getString("label.sNumber");
	String conveyanceCharges = resource.getString("label.conveyanceCharges");
	String conveyanceAmount = resource.getString("label.conveyanceAmount");
	String trasportInvoice = resource.getString("label.trasportInvoice");
	String GSTAmount = resource.getString("label.GSTAmount");
	String chargesAmountAfterTax = resource.getString("label.chargesAmountAfterTax");
	String conveyanceTax = resource.getString("label.conveyanceTax");
	String actions = resource.getString("label.actions");
	String totalAmount = resource.getString("label.totalAmount");	
	String finalamountdiv = "Not Available";
	
	
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>
<style>
.btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
.table>thead>tr>th{
border-top:1px solid #000 !important;
border-bottom:1px solid #000 !important;
background-color:#ddd;
}
.pro-table>thead>tr>th {border-top:1px solid #000 !important;}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
.Btnbackground{
background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
}
.middle {position: absolute;right: 35px !important;bottom: 16px !important;}
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
</style>
<html>
<head>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />

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
		<jsp:include page="CacheClear.jsp" />  
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
        <title>Sumadhura-IMS</title>
        <link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
        
       </head>
       <body class="nav-md" onload="finalamountdisplay()">
       
       <noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
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
	
	  /* css for iframe modal popup */
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
	cursor:pointer !important;
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
  cursor:pointer !important;
}

.pdf-cls:hover .middle {
  opacity: 1;
  cursor:pointer !important;
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
margin-top:-45px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
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

	</style>
	
</noscript>

<script>

//Create DIV element and append to the table cell
var i = 1;

function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	//debugger;
	
     var vfx = fldLength;     
     //removing space
     tableColumnName=tableColumnName.trim();
     var snoDivIdColumn =  "<%= serialNumber %>";
     snoDivIdColumn = formatColumns(snoDivIdColumn);
     //alert(snoColumn);
     

	 
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
	 
<%-- 	 var hsnCodeColumn =  "<%= hsnCode %>";
	 hsnCodeColumn = formatColumns(hsnCodeColumn); --%>
	 
	 
<%-- 	 var expiryDate=  "<%= expiryDate %>";
	 expiryDate = formatColumns(expiryDate);
	 //alert(hsnCodeColumn); --%>
	 
 	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn); 
	 //alert(actionsColumn);

     if(tableColumnName == snoDivIdColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        //snoDiv.setAttribute("class", style);
        //snoDiv.setAttribute("className", style);
        cell.appendChild(snoDiv);
    }
    else  if(tableColumnName == taxColumn) {
    		var dynamicSelectBoxId = "TaxId"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
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
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
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
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);	    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("onblur", "calculateTotalAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');			    
		    cell.appendChild(div);		    
   		}
    	else if(tableColumnName == basicAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == taxAmountColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}
    	else if(tableColumnName == amountAfterTaxColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
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
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
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
		    div2.setAttribute("style", "border: 1px solid #312d2d;"); 
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
		    div1.setAttribute("onclick", "deleteRow(this, "+vfx+")")
 			div1.setAttribute("style", "border: 1px solid #312d2d;"); 
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}   
}




/* ******************* This section represents the Second grid append to Other trasport charges************** */

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	debugger;
	
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
    	    div.setAttribute("onchange", "changeconveyance("+vfx+")" );
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
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", "Please enter Amount");
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
		    div.setAttribute("type", "number", placeholder="Enter Amount");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("placeholder", "GST Amount");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("placeholder", "Amount After Tax");
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

function finalamountdisplay()
{
	var noofrows=document.getElementById("noofrows").value;
	 document.getElementById("finalAmntDiv").innerHTML = document.getElementById("fa"+noofrows).value;
	// document.getElementById("invoiceTotalAmount").value = document.getElementById("fa"+noofrows).value;
		 
	}
/* ********************************************************** */

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
								<li class="breadcrumb-item"><a href="#">Inwards</a></li>
								<li class="breadcrumb-item active">Update Invoice Details</li>
								<li class="breadcrumb-item active">Invoice ID</li>
							</ol>
						</div>
					
					
                   <div>
	              <div class="">	
						<form:form modelAttribute="invoiceDetailsModelForm"  id ="doInventoryUpdateFormId" class="form-horizontal"   method="POST" enctype="multipart/form-data" >
							  <c:forEach var="getInvoiceDetails" items="${requestScope['listIssueToOtherSiteInwardLists']}"> 
							  <%-- <c:set  scope="session"   value="${requestScope['listIssueToOtherSiteInwardLists']}" var="getInvoiceDetails"></c:set> --%> 
					<div class="container border-inwards">
					  <div class="col-md-12">
					 <div class="col-md-4">
					  <div class="form-group">
					
							<label for="invoice-number" class="col-md-6">Invoice Number: </label>
							<div class="col-md-6">
							 <form:input path="invoiceNumber" id="InvoiceNumberId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.invoiceNumber}" readonly="true"/> 
							 <c:set  scope="session"   value="${getInvoiceDetails.invoiceNumber}" var="invoice_Number"> </c:set>
							</div>
						</div>
					 </div>
						<div class="col-md-4">
						 <div class="form-group">
							<label for="invoice-date" class="col-md-6 col-xs-12"> Invoice Date: </label>
							<div class="col-md-6 col-xs-12 input-group">
							 <form:input path="invoiceDate" id="InvoiceDateId" class="form-control marginRight" value="${getInvoiceDetails.invoiceDate}"  readonly="true"/> 
						     <label class="input-group-addon btn input-group-addon-border" for="InvoiceDateId"><span class="fa fa-calendar"></span></label>
						    </div>
						</div>
						</div>
						<div class="col-md-4">
						 <div class="form-group">
							<label for="vendor-name" class="col-md-6">Vendor Name: </label>
							<div class="col-md-6">
							 <form:input path="vendorName" id="VendorNameId" class="form-control marginRight"  value="${getInvoiceDetails.vendorName}" readonly="true"/>
						     <input type="hidden" name="vendorId" id="VendorId" class="form-control marginRight"  value="${getInvoiceDetails.vendorId}" />
						
							</div>
						
						</div>
						</div>
						</div>
						<div class="col-md-12">
						<div class="col-md-4">
						 <div class="form-group">
						<label for="received-date" class="col-md-6 col-xs-12">Received Date : </label>
						<div class="col-md-6 col-xs-12 input-group">
						  <form:input path="receivedDate" name="receivedDate" id="receivedDate1" class="form-control marginRight" value="${getInvoiceDetails.receivedDate}" readonly="true"/> 	
						  <label class="input-group-addon btn input-group-addon-border" for="receivedDate1"><span class="fa fa-calendar"></span></label>	
						</div>
						</div>
						</div>
						<div class="col-md-4">
						 <div class="form-group">
							<label for="gst-in" class="col-md-6">GSTIN : </label>
							<div class="col-md-6">
							 <form:input path="gsinNo" id="GSTINNumber" class="form-control marginRight" value="${getInvoiceDetails.gsinNo}"  readonly="true"/> 
							
							</div>
								</div>
						</div>
							<div class="col-md-4">
							 <div class="form-group">	
							<label for="vendor-address" class="col-md-6">Vendor Address : </label>
							<div class="col-md-6">
							 <form:input path="vendorAdress" id="VendorAddress" value="${getInvoiceDetails.vendorAdress}" class="form-control col-sm-2 marginRight" readonly="true"/> 
								
							</div>
								</div>
							</div>
						</div>
						<div class="col-md-12">
						<div class="col-md-4">
						 <div class="form-group ">
						
							<label for="state" class="col-md-6">State : </label>
							<div class="col-md-6">
							 <form:input path="state" id="state" class="form-control col-sm-2 marginRight" readonly="true" value="${getInvoiceDetails.state}"/>
						
							</div>
									</div>
						</div>
							<div class="col-md-4">
							 <div class="form-group">
							<label for="pono" class="col-md-6">PO NO : </label>
							<div class="col-md-6">
							 	<form:input path="poNo" name="poNo" id="PONOId" class="form-control marginRight" value="${getInvoiceDetails.poNo}" readonly="true"/>
							</div>
							 
							</div>
							
							</div>
							<div class="col-md-4">
							 <div class="form-group">
							<label for="po-date" class="col-md-6 col-xs-12">PO Date : </label>
						    <div class="col-md-6 col-xs-12 input-group">
						     <form:input path="poDate" name="poDate" id="poDateId1" class="form-control marginRight" value="${getInvoiceDetails.poDate}" readonly="true"/> 
							<label class="input-group-addon btn input-group-addon-border" for="poDateId1"><span class="fa fa-calendar"></span></label>
							
						    </div>
							
						</div>
							</div>
						
					</div>
						<div class="col-md-12">
						 <div class="col-md-4">
						  	<div class="form-group">
						  	 <label for="e-way" class="col-md-6">E Way Bill No : </label>
							<div class="col-md-6">
							  <form:input path="eWayBillNo" name="eWayBillNo" id="eWayBillNoId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.eWayBillNo}" readonly="true"/> 
							</div>
						  	</div>
						 </div>	
						<div class="col-md-4">
						<div class="form-group">
						  <label for="vehicle-no" class="col-md-6">Vehicle No : </label>
						 <div class="col-md-6">
						  <form:input path="vehileNo" name="vehileNo" id="vehileNoId" class="form-control col-sm-2 marginRight " value="${getInvoiceDetails.vehileNo}" readonly="true"/> 
						
						 </div>
						</div>
							
						</div>
						<div class="col-md-4">
						 <div class="form-group">
						  <label for="transporter-name" class="col-md-6">Transporter Name : </label>
							<div class="col-md-6">
							 <form:input path="transporterName" name="transporterName" id="transporterNameId" value="${getInvoiceDetails.transporterName}" class="form-control col-sm-2 marginRight" readonly="true"/> 
							
								<c:set value="${getInvoiceDetails.note}" var="note"/>
							</div>
						 </div>
							
							
						</div>
						</div>
				       <div class="col-md-12">
				        <div class="col-md-4">
				         <div class="form-group ">
						<label for="indentEntryId" class="col-md-6" style="display:none">indent Entry Id : </label>
						<div class="col-md-6">
						 <form:input path="indentEntryId" name="indentEntryId" id="indentEntryId" style="display:none" value="${getInvoiceDetails.indentEntryId}" class="form-control col-sm-2 marginRight" readonly="true"/> 
						 <input type="hidden" name="totalAmount" id="totalAmount" value="${getInvoiceDetails.totalAmount}"/>
						 <input type="hidden" name="indentEntry_Id" id="indentEntry_Id"  value="${getInvoiceDetails.indentEntryId}" class="form-control col-sm-2 marginRight" readonly="true"/> 
						 <c:set  scope="session"   value="${getInvoiceDetails.indentEntryId}" var="indent_Entry_Number"> </c:set>
						 <c:set  scope="session"   value="${getInvoiceDetails.siteId}" var="site_Id"> </c:set>
						</div>	
									
							
						</div>
				        </div>
				       </div>
						
						
						
						</div>
						<!-- 
						</div> -->
				     </c:forEach> 
					
						
					  <!-- PRODUCT DETAILS -->
					  <div class="clearfix"></div>
					     <div class="">
					       <div class="table-responsive Mrgtop20"> <!-- protbldiv  -->
								<table id="doInventoryTableId" class="table pro-table" style="width:4000px;">
								<thead>
								<tr>
									<th>S.NO</th>
				    				<th>Product</th>
				    				<th>Sub Product</th>
				    				<th>Child Product</th>    								
				    				<th>Unit of Measurement</th>
				    				<th>Quantity</th> 
				    				<th>Price</th>
				    				<th>Basic Amount</th>
				    				<th>Tax Per Unit</th>
				    				<th>HSN Code </th>
				    				<th>Tax Amount</th>
				    				<th>Amount After Tax</th>
				    				<th>Other Or TransportCharges</th>
				    				<th>TaxOn Other Or Transport Charges</th>
				    				<th>Other Or Transport Charges After Tax </th>
				    				<th>Total Amount </th>
									<th>Expire Date</th>
									<th>Actions</th>
								</tr>
								
								</thead>
								<tbody>
								 <tr>
								
								   <input type="hidden" id="noofrows" value="${requestScope['listOfGetProductDetails'].size()}" />
								  <c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">	
								
					        
					        	  	<td>						
								       <div id="snoDivId${GetProductDetails.strSerialNo}">${GetProductDetails.strSerialNo}</div> 
									</td>
									
									<td>
									<input type="text" id="product${GetProductDetails.strSerialNo}"  name ="product${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.product}"/>  
								    </td>
									<td>
									 <input type="text" id="subProduct${GetProductDetails.strSerialNo}"  name ="subProduct${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.subProduct}"/> 
									</td>
									<td>
									<input type="text" id="childProductt${GetProductDetails.strSerialNo}"  name ="childProduct${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.childProduct}"/> 
									</td>
									
									<td>
									<input type="text" id="unitsOfMeasurement${GetProductDetails.strSerialNo}"  name ="unitsOfMeasurement${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.unitsOfMeasurement}"/>
									</td>
									<td >
									<input type="text"  id="quantity${GetProductDetails.strSerialNo}"  name="quantity${GetProductDetails.strSerialNo}"   onchange="calculatequantitybase(${GetProductDetails.strSerialNo})" onblur="calculateTotalAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.quantity}"/> 
									 <input type="hidden"  id="oldQuantity${GetProductDetails.strSerialNo}"  name="oldQuantity${GetProductDetails.strSerialNo}"   onchange="calculatequantitybase(${GetProductDetails.strSerialNo})" onblur="calculateTotalAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.quantity}"/> 
									</td>					
								
									<td>
									<input type="text" id="price${GetProductDetails.strSerialNo}"  name ="price${GetProductDetails.strSerialNo}"  onblur="calculateTotalAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.price}"/>
									</td>
									<td> 
									<input type="text" id="BasicAmountId${GetProductDetails.strSerialNo}"  name ="BasicAmountId${GetProductDetails.strSerialNo}"  onblur="calculateTaxAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.basicAmount}"/>
									</td>
									
									<td>
									<input type="text" id="taxAmountId${GetProductDetails.strSerialNo}"  name ="tax${GetProductDetails.strSerialNo}"  onchange="calculateTaxAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.tax}"/>
									</td>
									<td>
									<input type="text" id="hsnCode${GetProductDetails.strSerialNo}"  name ="hsnCode${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.hsnCode}"/>
									</td>
									<td>
									<input type="text" id="TaxAmountId${GetProductDetails.strSerialNo}"  name ="taxAmount${GetProductDetails.strSerialNo}"  onchange="calculateTaxAmount(${GetProductDetails.strSerialNo})" readonly="true" class="form-control" value="${GetProductDetails.taxAmount}"/>
									</td>
									<td>
									<input type="text" id="AmountAfterTaxId${GetProductDetails.strSerialNo}"  name ="amountAfterTax${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.amountAfterTax}"/>
								    </td>
									<td>
									<input type = "text" id="otherOrTransportCharges${GetProductDetails.strSerialNo}"  name="otherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/>
									<input type = "hidden" id="otherOrTransportChargesId${GetProductDetails.strSerialNo}" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/>
									
									</td>
									<td>
										<input  type="text" id="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  name="taxOnOtherOrTransportCharges${GetProductDetails.strSerialNo}"  readonly="true"  class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/>
										<input  type="hidden" id="taxOnOtherOrTransportChargesId${GetProductDetails.strSerialNo}"  class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/>
									</td>
									<td>
										<input type="text" id="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  name="otherOrTransportChargesAfterTax${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/>
									</td>
									<td>
									<input type="text"  id="totalAmount${GetProductDetails.strSerialNo}"  name="totalAmount${GetProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/>
										</td>		
									<td>
									<form:input path="expireDate" id="expireDate${GetProductDetails.strSerialNo}" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.expireDate}"/>
									</td>  
									<td style="display:none">
									<input  type = "text" id="indentEntryDetailsId${GetProductDetails.strSerialNo}" style="display:none" name="indentEntryDetailsId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
									</td>  
									
										<td style="border: 1px solid #312d2d;text-align:center;">
										<button type="button" name="editItemBtn" value="Edit Item" id="editItem" class="btnaction" onclick="editInvoiceRow(${GetProductDetails.strSerialNo})" ><i class="fa fa-pencil"></i></button> 
									</td>
									
								 </tr>
						
							      <input type="hidden" id="fa${GetProductDetails.strSerialNo}" value="${GetProductDetails.finalAmountDiv}"/>
				
							  </c:forEach>
							</tbody>
					    </table>
					</div>
				</div>
				<!-- 	*********************************** -->
				<div class="clearfix"></div>
				  <div class="Mrgtop20">
						<div class="table-responsive "><!--  protbldiv -->
							<table id="doInventoryChargesTableId" class="table pro-table" style="width:1600px;" >
								<thead>
								 <tr style="background: #eae7e7;">
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
								<tbody>
								<c:forEach var="GetTransportDetails" items="${requestScope['listOfTransportChargesDetails']}" step="1" begin="0">
								 <tr id="chargesrow${GetTransportDetails.strSerialNo}" class="chargesrowcls"> 
									<td>						
										<div id="snoChargesDivId${GetTransportDetails.strSerialNo}">${GetTransportDetails.strSerialNo}</div>
									</td>
									<td>						
											<form:select path="" id="Conveyance${GetTransportDetails.strSerialNo}" class="form-control " name="Conveyance${GetTransportDetails.strSerialNo}" onchange="changeconveyance${GetTransportDetails.strSerialNo}" readonly="true">
											<form:option value="${GetTransportDetails.transportId}$${GetTransportDetails.conveyance}">${GetTransportDetails.conveyance}</form:option>
									    		<%	
									    			Map<String, String> conveyanceCharges1 = (Map<String, String>)request.getAttribute("chargesMap");
									    			for(Map.Entry<String, String> tax : conveyanceCharges1.entrySet()) {
													String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
												%>
													<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
									    		<% } %>
										</form:select>
									</td>
									<td>
									
										<form:input path="" name="ConveyanceAmount${GetTransportDetails.strSerialNo}"  id="ConveyanceAmount${GetTransportDetails.strSerialNo}"   type="number"  value="${GetTransportDetails.conveyanceAmount}"  class="form-control" autocomplete="off" onkeyup="calculateGSTTaxAmount(${GetTransportDetails.strSerialNo})" readonly="true"/>
									</td>
									<td>
										<form:select path="" id="GSTTax${GetTransportDetails.strSerialNo}" class="form-control"   name="GSTTax${GetTransportDetails.strSerialNo}" value="" onchange="calculateGSTTaxAmount(${GetTransportDetails.strSerialNo})" readonly="true">
											<form:option value="${GetTransportDetails.gstTaxId}$${GetTransportDetails.GSTTax}">${GetTransportDetails.GSTTax}</form:option>
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
										<form:input path="" name="GSTAmount${GetTransportDetails.strSerialNo}" id="GSTAmount${GetTransportDetails.strSerialNo}"  readonly="true" type="number" placeholder="GST Amount"  class="form-control" value="${GetTransportDetails.GSTAmount}" autocomplete="off"/>
									</td>
										<td>
										<form:input path="" type="number"  name="AmountAfterTax${GetTransportDetails.strSerialNo}"  readonly="true" id="AmountAfterTax${GetTransportDetails.strSerialNo}" placeholder="Amount After Tax"  class="form-control" value="${GetTransportDetails.amountAfterTax}" autocomplete="off"/>
									
									<input type="hidden"  name="iD${GetTransportDetails.strSerialNo}"   id="iD${GetTransportDetails.strSerialNo}" placeholder="Amount After Tax"  class="form-control" value="${GetTransportDetails.iD}"/>
									</td>
								
									<td>
										<form:input path=""  type="text" name="TransportInvoice${GetTransportDetails.strSerialNo}"  id="TransportInvoice${GetTransportDetails.strSerialNo}" placeholder="Transport Invoice Number"  onkeydown="appendRow()"   class="form-control" autocomplete="off" readonly="true"/>
										<input  type = "hidden" id="transactionActionValue${GetTransportDetails.strSerialNo}"  name="transactionActionValue${GetTransportDetails.strSerialNo}"  class="form-control  autocomplete="off" readonly="true" value="S"/> 
									</td>
									
									
									 
									<td>
										<button type="button" name="addNewChargesItemBtn${GetTransportDetails.strSerialNo}" id="addNewChargesItemBtnId${GetTransportDetails.strSerialNo}" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
										<%-- <button type="button" name="addDeleteItemBtn${GetTransportDetails.strSerialNo}" id="addDeleteItemBtnId${GetTransportDetails.strSerialNo}"  onclick="deleteRow(this,'${GetTransportDetails.strSerialNo}')" class="btnaction"><i class="fa fa-trash"></i></button> --%>
										<button type="button" name="addremoveItemBtn${GetTransportDetails.strSerialNo}" id="addremoveChargesItemBtnId${GetTransportDetails.strSerialNo}" class="btnaction" onclick="removeTransRow(${GetTransportDetails.strSerialNo})" ><i class="fa fa-remove"></i></button>
									    <button type="button" name="editchargesItemBtn${GetTransportDetails.strSerialNo}" value="Edit Item" id="editchargesItem${GetTransportDetails.strSerialNo}" class="btnaction" onclick="editchargesInvoiceRow(${GetTransportDetails.strSerialNo})" ><i class="fa fa-pencil"></i></button>
								</td>
								</tr>
								</c:forEach>
								</tbody>
							</table>
							</div>
							</div>
							<div class="col-md-12 no-padding-right no-padding-left">
							 <div class="col-md-6 Mrgtop20 no-padding-left ">
									<label class="col-md-3"><h4><strong>Note:</strong></h4></label><div class="col-md-9 no-padding-right"><form:textarea path="Note" id="NoteId" class="form-control" placeholder="${note}" style="resize:vertical;"/></div>
									</div>
									<div class="col-sm-6 Mrgtop20"><span class="h4"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv"></span></div>
						
							</div>
								<div class="clearfix"></div>
								
								
								<input type="hidden" name="strName" id="strName" value="${vendorName}"/>
								<input type="hidden" name="invoice_Number" id="invoice_Number" value="${invoice_Number}"/>
								<input type="hidden" name="indent_Entry_Number" id="indent_Entry_Number" value="${indent_Entry_Number}"/>
								<input type="hidden" name="site_Id" id="site_Id" value="${siteId}"/>
							
					            <input type="hidden" name="numbeOfRowsToBeProcessed"  id="countOfRows">
								<input type="hidden" name="numbeOfTransRowsToBeProcessed"  id="countOfTransRowsRows">
								<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveBtnId">
								<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveChargesBtnId">
								<input type="hidden" name="invoiceTotalAmount"  id=invoiceTotalAmount >
								<input type="hidden" name="VendorId" id="vendorIdId">
								<input type="hidden" name="chargesLength" id="chargesLength" value="">
				
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
								  <iframe class="iframe-pdf" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
								  <div class="middle" data-toggle="modal" data-target="#myModalpdf<%=i%>">
									<!-- <button type="button" class="btn btn-danger btn-fullwidth" ><i class="fa fa-close"></i></button> -->
								</div>
								<input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
								<p class="btn-downloaddelete"><a class="btn btn-success btn-xs" download onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i class="fa fa-download"></i> Download</a>
								<button class="btn btn-danger btn-xs" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')"><i class="fa fa-close"></i>&nbsp;Delete</button></p>
								<%--  <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')" data-dismiss="modal">Delete</button> --%>
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
																		<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" style="cursor:pointer;"/>
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
												<input type="hidden" name="totalCount" id="totalCount" value="<%=totalValue%>"/>
												
													<div class="col-md-12">
															<div class="file-upload color-head-file" id="ishidden">
																	<c:if test="${(imagecount+pdfcount)<8}"><h3>You can upload Images/PDFs here :</h3>
															</c:if>
															
															
															
															
															<div class="" id="files_place" style="display:none;">
															<div class="files_place"  >
																<input type="file" id="file_select1" name="file"
																	class="selectCount Mrgtop10" style="float: left;"
																	accept="application/pdf,image/*" onchange="filechange(1)" />
																<button type="button" class="btn btn-danger Mrgtop10"
																	id="close_btn1" style="float: left; display: none;"
																	onclick="filedelete(1)">
																	<i class="fa fa-close"></i>
																</button>
															</div>
															<div class="clearfix"></div>
															<button type="button" id="Add"
																class="btn btn-success btn-xs Mrgtop10"
																style="display: none;" onclick="addFile(1)">
																<i class="fa fa-plus"></i> Add File
															</button>
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
							<!-- modal popup for invoice image end --> 
				
					<div id="allImgPdfUrlToDeleteFile" ></div>
					</form:form>
					</div>
					
					</div>					
					<div class="clearfix"></div>
					<div class="text-center center-block">
						<input class="btn btn-warning btn-bottom" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
				        <input class="tblchargesbtn btn btn-warning btn-bottom" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')">
							
					</div>
									
				</div>
				</div>
				</div>					
						<!-- /page content -->   
				<!-- jQuery -->
				<script src="js/jquery.min.js"></script>
				<!-- Bootstrap -->
				<script src="js/bootstrap.min.js"></script>
				<!-- Custom Theme Scripts -->
				<script src="js/custom.js"></script>
				<script src="js/jquery-ui.js" type="text/javascript"></script>
				<script src="js/getInvoiceDetailsInwards.js" type="text/javascript"></script>
				<script src="js/sidebar-resp.js" type="text/javascript"></script>
				
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			//changing conveyance written by thirupathi	
		 	function changeconveyance(btn){
				debugger;
		 		if ($("#Conveyance"+btn).val() == "999$None"){
					  // Updating text input based on selected value
					  $("#ConveyanceAmount"+btn).val("0");
					  $("#ConveyanceAmount"+btn).attr("readonly", "true");
					  $("#GSTTax"+btn).val("1$0%");
					  $("#GSTTax"+btn).attr("disabled", "true");
					  $("#GSTAmount"+btn).val("0");
					  $("#GSTAmount"+btn).attr("readonly", "true");
					  $("#AmountAfterTax"+btn).val("0");
					  $("#AmountAfterTax"+btn).attr("readonly", "true");						  
					  }else{
						  $("#ConveyanceAmount"+btn).val(" ");
						  $("#ConveyanceAmount"+btn).removeAttr("readonly");
						  $("#GSTTax"+btn).val("");
						  $("#GSTTax"+btn).removeAttr("disabled");
						  $("#GSTAmount"+btn).val(" ");
						  $("#GSTAmount"+btn).removeAttr("readonly");
						  $("#AmountAfterTax"+btn).val(" ");
						  $("#AmountAfterTax"+btn).removeAttr("readonly");
					  }
		 	}
			
		</script>
			<!-- modal popup for invoice image start -->
		 <!-- Modal -->
  <div class="modal fade custmodal" id="uploadinvoice-img0" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
         
        </div>
        <div class="modal-body cust-modal-body">
          <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope['image0']}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
		<!-- modal popup for invoice image end -->    
			<!-- modal popup for invoice image start -->
		 <!-- Modal -->
  <div class="modal fade custmodal" id="uploadinvoice-img1" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          
        </div>
        <div class="modal-body cust-modal-body">
          <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope['image1']}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
		<!-- modal popup for invoice image end -->    
			<!-- modal popup for invoice image start -->
		 <!-- Modal -->
  <div class="modal fade custmodal" id="uploadinvoice-img2" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          
        </div>
        <div class="modal-body cust-modal-body">
          <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope['image2']}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
		<!-- modal popup for invoice image end -->    
			<!-- modal popup for invoice image start -->
		 <!-- Modal -->
  <div class="modal fade custmodal" id="uploadinvoice-img3" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          
        </div>
        <div class="modal-body cust-modal-body">
          <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope['image3']}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
	<script>
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
		        	if((this.files[0].size/kilobyte)<=kilobyte){
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
				// var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
				 /* var imagesAlreadyPresent=$("#imagesAlreadyPresent").val();
				 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val();
				 imagesAlreadyPresent=imagesAlreadyPresent-1;
				 $("#imagesAlreadyPresent").val(imagesAlreadyPresent); */
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
			 //$("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");		 
			 // deleteFile(divId,imagePath);
			 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
			 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
			 //var url="deleteInvoicePermanentImage.spring?imagePath="+imagePath+"&strVendorName=${getInvoiceDetails.vendorName}&siteId=${getInvoiceDetails.siteId}&invoiceNumber=${getInvoiceDetails.invoiceNumber}&indentEntryId=${getInvoiceDetails.indentEntryId}";
			 var url="deleteInvoicePermanentImage.spring?"+imagePath;	
			 //Stroing files url for deleting files after update button press
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
					 document.getElementById("doInventoryUpdateFormId").method = "POST";
				     document.getElementById("doInventoryUpdateFormId").submit();
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
	 alert("please select file");
	 $("#file_select"+classLastId).focus();
	 return false;
  }
  if(k > 7){
	alert("You can't upload more than eight files");
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
	//var classlength=parseInt($(".selectCount").length);
	//var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
	//var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
	//var k=imagesAlreadyPresent+pdfAlreadyPresent+classlength;
	
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
	//this code for download server images	
    
	</script>
	
		<!-- modal popup for invoice image end -->    
</body>
</html>
