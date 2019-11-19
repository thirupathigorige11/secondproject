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
	
	
	
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>
<style>
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
</style>
<html>
<head>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />

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
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">

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
	  taxChargeColumn = formatChargesColumns(taxChargeColumn);
	 
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
    	     $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
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
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">With Pricing</li>
						</ol>
					</div>
					
					
<div>
	<div align="center">
	
		<form:form modelAttribute="DCViewModelForm"  id = "doDCUpdateFormId" class="form-inline"   method="POST" >
			  <c:forEach var="GetProductDetails" items="${requestScope['DCFormInwardList']}"> 
	<div class="container">
	 <div class="row">
	  <div class="col-xs-12">
	<div class="form-group col-xs-12" style="margin-bottom:35px;">
	
			<label for="invoice-number" class="col-sm-2" style="width:13%;">Invoice Number: </label>
			<form:input path="invoiceNumber" id="InvoiceNumberId" class="form-control col-sm-2 marginRight" value="${GetProductDetails.invoiceNumber}" /> 
			
		
			<label for="invoice-date" class="col-sm-2" style="width:11%;"> Invoice Date: </label>
			<form:input path="invoiceDate" id="InvoiceDateId" class="form-control col-sm-2 marginRight" value="${GetProductDetails.invoiceDate}"  /> 
		
		
			<label for="vendor-name" class="col-sm-2" style="width:14%;">Vendor Name: </label>
			<form:input path="vendorName" id="VendorNameId" class="form-control col-sm-2 marginRight"  value="${GetProductDetails.vendorName}"  readonly="true"/>
		    <input type="text" id="vendorId" name = "VendorId"  style="display:none;"class="form-control col-sm-2 marginRight"  value="${GetProductDetails.vendorId}" readonly="true"/>
		
		</div>
		<div class="form-group col-xs-12" style="margin-bottom:10px;">
				
			<label for="gst-in" class="col-sm-2" style="width:13%;">GSTIN : </label>
				<form:input path="gstinNumber" id="GSTINNumber" class="form-control col-sm-2 marginRight" value="${GetProductDetails.gstinNumber}"  readonly="true"/> 
				
			<label for="vendor-address" class="col-sm-2" style="width:11%;">Vendor Address : </label>
				
				<form:input path="vendorAddress" id="VendorAddress" value="${GetProductDetails.vendorAddress}" class="form-control col-sm-2 marginRight" readonly="true"/> 
				
			<label for="state" class="col-sm-2" style="width:14%;">State : </label>
			
				<form:input path="state" id="state" class="form-control col-sm-2 marginRight" readonly="true" value="${GetProductDetails.state}"/>
		</div>
		<div class="form-group col-xs-12" style="margin-bottom:10px;">
		
			
			<%-- <label for="pono" class="col-sm-2" style="width:11%;">PONO : </label>
			
				<form:input path="poNo" name="poNo" id="PONOId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.poNo}" readonly="true"/> 
			
			<label for="po-date" class="col-sm-2" style="width:14%;">PO Date : </label>
		
			<form:input path="poDate" name="poDate" id="poDateId1" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.poDate}" readonly="true"/> 
		 --%>	
		</div>
		
	
		<div class="form-group col-xs-12" style="margin-bottom:10px;">
			
			<%-- <label for="e-way" class="col-sm-2" style="width:13%;">E Way Bill No : </label>
			
				 <form:input path="eWayBillNo" name="eWayBillNo" id="eWayBillNoId" class="form-control col-sm-2 marginRight" value="${getInvoiceDetails.eWayBillNo}" readonly="true"/> 
		 --%>	
			
			<label for="vehicle-no" class="col-sm-2" style="width:13%;">Vehicle No : </label>
			
			<form:input path="vehileNo" name="vehileNo" id="vehileNoId" class="form-control col-sm-2 marginRight " value="${GetProductDetails.vehileNo}" /> 
		
			<label for="transporter-name" class="col-sm-2" style="width:11%;">Transporter Name : </label>
			
			<form:input path="transporterName" name="transporterName" id="transporterNameId" value="${GetProductDetails.transporterName}" class="form-control col-sm-2 marginRight" /> 
			<label for="received-date" class="col-sm-2" style="width:14%;">Received Date : </label>
			
				 <form:input path="receivedDate" name="receivedDate" id="receivedDate" class="form-control col-sm-2 marginRight" readonly="true" value="${GetProductDetails.receivedDate}" /> 
				
			
				
		</div>

		
		
		 <div class="form-group col-xs-12">
		
		<input  type = "text" id="intrmidiatetEntryId" name="intrmidiatetEntryId" style="display: none;"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.intrmidiatetEntryId}"/>
		
		</div> 
		</div>
		</div>
		</div>
 </c:forEach> 
	
		
	               <!-- PRODUCT DETAILS -->
	       <div class="table-responsive protbldiv">    
	   
	        ` <div class="clearfix"></div>
				<table id="doInventoryTableId" class="table pro-table" style="">
				<tr class="table_header" style="  background: #eae7e7;">
					<th>S NO</th>
    				<th>Product</th>
    				<th>Sub Product</th>
    				<th>Child Product</th>    								
    				<th>Unit of Measurement</th>
    				<th>Quantity</th> 
    				<th> Price</th>
    				<th> BasicAmount</th>
    				<th> TaxPer </th> 
    				<th> HSNCode </th>
    				 <th>Tax Amount</th>
    				<th>AmountAfterTax</th>
    				<th> Other Or TransportCharges</th>
    				<th>TaxOn Other Or TransportCharges</th>
    				<th> Other Or TransportChargesAfterTax </th>
    				<th> Total Amount </th>
					<th>Expire Date</th>
				</tr>
				  <c:forEach var="DCFormProductDetails" items="${requestScope['DCFormProductDetails']}" step="1" begin="0">	
	        	 	 <tr>
	        	  	<td>						
				       <div id="snoDivId${DCFormProductDetails.strSerialNo}">${DCFormProductDetails.strSerialNo}</div> 
						<%-- <form:input  id= "snoDivId${GetProductDetails.strSerialNo}" path="strSerialNo"  class="form-control" readonly="true" value="${GetProductDetails.strSerialNo}"/> --%>
					</td>
	        	  
					<td>
					
					      
                     <%--  <form:input  id= "product${GetProductDetails.strSerialNo}" path="product"  class="form-control" readonly="true" value="${GetProductDetails.product}"/> --%> 
					<input type="text"  id="product${DCFormProductDetails.strSerialNo}"  name="product${DCFormProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${DCFormProductDetails.product1}"/>
					
					</td>
					<td>
						<input type="text"  id="subProduct${DCFormProductDetails.strSerialNo}"  name="subProduct${DCFormProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${DCFormProductDetails.subProduct1}"/>
						<%-- <form:input path="subProduct"    class="form-control" readonly="true" value="${GetProductDetails.subProduct}"/> --%>
					</td>
					<td>
					
					
					
					
					
					
						<%-- <form:input path="childProduct" class="form-control" readonly="true" value="${GetProductDetails.childProduct}"/> --%>
					    <input type="text"  id="childProduct${DCFormProductDetails.strSerialNo}"  name="childProduct${DCFormProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${DCFormProductDetails.childProduct1}"/>
					</td>
					
					<td>
						<%-- <form:input path="unitsOfMeasurement"  class="form-control" readonly="true" value="${GetProductDetails.unitsOfMeasurement}"/> --%>
					   <input type="text"  id="unitsOfMeasurement${DCFormProductDetails.strSerialNo}"  name="unitsOfMeasurement${DCFormProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${DCFormProductDetails.unitsOfMeasurement1}"/>
					</td>
					<td >
						<%-- <form:input path="quantity"    class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.quantity}"/> --%>
                 	
                 	<input    type="text"  id="quantity${DCFormProductDetails.strSerialNo}"  name="quantity${DCFormProductDetails.strSerialNo}"  onblur="calculateTotalAmount(${DCFormProductDetails.strSerialNo})"        class="form-control" value="${DCFormProductDetails.quantity1}"/>
                 	<%-- 	<form:input path="ProductAvailability" id="ProductAvailabilityId1" readonly="true" class="form-control"  value="${GetProductDetails.ProductAvailability}"/> --%> 
					</td>					
				
					<td>
						<%-- <form:input path="price"    class="form-control" readonly="true" value="${GetProductDetails.price}"/> --%>
				       <input    type = "text"   id="price${DCFormProductDetails.strSerialNo}"  name="price${DCFormProductDetails.strSerialNo}"  onblur="calculateTotalAmount(${DCFormProductDetails.strSerialNo})"  class="form-control" value="${DCFormProductDetails.price1}"/>
				
					</td>
				 	<td> 
						<input  type = "text"   id="BasicAmountId${DCFormProductDetails.strSerialNo}"  name="BasicAmountId${DCFormProductDetails.strSerialNo}"  class="form-control" onblur="calculatePriceAmount(${DCFormProductDetails.strSerialNo})"   value="${DCFormProductDetails.basicAmount1}"/>
					</td> 
					
					 <td>
						
					
				<%-- 	 <form:select path="tax1"  id="tax${DCFormProductDetails.strSerialNo}" name="tax${DCFormProductDetails.strSerialNo}" onchange="calculateTaxAmount(${DCFormProductDetails.strSerialNo})" value="${DCFormProductDetails.tax1}" class="form-control"  >
							<form:option value="">--Select--</form:option>
					    		<%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option>
					    		<% } %>
						</form:select> --%>
						
							<select id="tax${DCFormProductDetails.strSerialNo}" name="tax${DCFormProductDetails.strSerialNo}" onchange="calculateTaxAmount(${DCFormProductDetails.strSerialNo})" value="${DCFormProductDetails.tax1}" class="form-control"  >
							<option value="">--select--<option>
					    		<%	
					    			Map<String, String> gstTax1 = (Map<String, String>)request.getAttribute("gstMap");
					    			for(Map.Entry<String, String> tax : gstTax1.entrySet()) {
									String taxIdAndPercentage = tax.getKey()+"$"+tax.getValue();
								%>
									<option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></option>
					    		<% } %>
						</select>
					
					</td> 
					<td>
						<input type="text"  id="hsnCode${DCFormProductDetails.strSerialNo}"  name="hsnCode${DCFormProductDetails.strSerialNo}"   readonly="true" class="form-control" value="${DCFormProductDetails.hsnCode1}"/>
						<%-- <form:input path="hsnCode"  readonly="true" class="form-control"  onkeydown="appendRow()" autocomplete="off"  value="${GetProductDetails.hsnCode}"/> --%>
					</td>
					<td>
					      <input type = "text" id="TaxAmountId${DCFormProductDetails.strSerialNo}" name="TaxAmountId${DCFormProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${DCFormProductDetails.taxAmount1}"/>
						<%-- <form:input path="taxAmount1" id="TaxAmountId${DCFormProductDetails.strSerialNo}" name="TaxAmountId${DCFormProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${DCFormProductDetails.taxAmount1}"/> --%>
					</td>
					<td>
						   <input type = "" id="AmountAfterTaxId${DCFormProductDetails.strSerialNo}"    name="AmountAfterTaxId${DCFormProductDetails.strSerialNo}"  class="form-control" value="${DCFormProductDetails.amountAfterTax1}"/>
					<%-- 	<form:input path="amountAfterTax1" id="AmountAfterTaxId${DCFormProductDetails.strSerialNo}"    name="AmountAfterTaxId${DCFormProductDetails.strSerialNo}"  class="form-control" value="${DCFormProductDetails.amountAfterTax1}"/> --%>
					</td> 
					<td>
						<%-- <form:input path="otherOrTransportCharges" id="OtherOrTransportChargesId${GetProductDetails.strSerialNo}" readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportCharges}"/> --%>
						<input type = "text" id="otherOrTransportCharges${DCFormProductDetails.strSerialNo}"  name="otherOrTransportCharges${DCFormProductDetails.strSerialNo}"   class="form-control" value="${DCFormProductDetails.otherOrTransportCharges1}"/>
					</td>
					<td>
						<%-- <form:input path="taxOnOtherOrTransportCharges" id="TaxOnOtherOrTransportChargesId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.taxOnOtherOrTransportCharges}"/> --%>
						<input  type="text" id="taxOnOtherOrTransportCharges${DCFormProductDetails.strSerialNo}"  name="taxOnOtherOrTransportCharges${DCFormProductDetails.strSerialNo}"    class="form-control" value="${DCFormProductDetails.taxOnOtherOrTransportCharges1}"/>
					</td>
					<td>
						<input type="text" id="otherOrTransportChargesAfterTax${DCFormProductDetails.strSerialNo}"  name="otherOrTransportChargesAfterTax${DCFormProductDetails.strSerialNo}"   class="form-control" value="${DCFormProductDetails.otherOrTransportChargesAfterTax1}"/>
						<%-- <form:input path="otherOrTransportChargesAfterTax" id="OtherOrTransportChargesAfterTaxId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.otherOrTransportChargesAfterTax}"/> --%>
					</td>
					<td>
					<input type="text"  id="totalAmount${DCFormProductDetails.strSerialNo}"  name="totalAmount${DCFormProductDetails.strSerialNo}"   class="form-control" value="${DCFormProductDetails.totalAmount1}"/>
						<%-- <form:input path="totalAmount"  id="TotalAmountId${GetProductDetails.strSerialNo}"  readonly="true" class="form-control" value="${GetProductDetails.totalAmount}"/> --%>
					</td>		
					<td>
					<%-- <form:input path="expireDate" id="expireDate${GetProductDetails.strSerialNo}" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.expireDate}"/> --%>
					<input type="text"  id="expireDate${DCFormProductDetails.strSerialNo}"  name="expireDate${DCFormProductDetails.strSerialNo}"   readonly="" class="form-control" value="${DCFormProductDetails.expireDate1}"/>
					
					
					
					<input  type = "hidden" id="productId${DCFormProductDetails.strSerialNo}" name="productId${DCFormProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${DCFormProductDetails.productId}"/>
					<input  type = "hidden" id="subProductId${DCFormProductDetails.strSerialNo}" name="subProductId${DCFormProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${DCFormProductDetails.subProductId}"/>
					<input  type = "hidden" id="childProductId${DCFormProductDetails.strSerialNo}" name="childProductId${DCFormProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${DCFormProductDetails.childProductId}"/>
					<input  type = "hidden" id="measurmentId${DCFormProductDetails.strSerialNo}" name="measurmentId${DCFormProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${DCFormProductDetails.unitsOfMeasurementId}"/>
					<input  type = "hidden" id="priceId${DCFormProductDetails.strSerialNo}" name="priceId${DCFormProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${DCFormProductDetails.priceId}"/>
				
					</td>  
					<%-- <td>
					<input  type = "text" id="indentEntryDetailsId${GetProductDetails.strSerialNo}" name="indentEntryDetailsId${GetProductDetails.strSerialNo}"  class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
					<form:input path="indentEntryDetailsId1" id="indentEntryDetailsId${GetProductDetails.strSerialNo}" class="form-control" autocomplete="off" readonly="true" value="${GetProductDetails.indentEntryDetailsId}"/>
					
					
					</td>   --%>
					
					
				</tr>
		
		

		
			
			</c:forEach>
	</table>
	</div>
			<br/>
	
			
<!-- 	*********************************** -->
			<div class="clearfix"></div><br/>
			<div class="table-responsive protbldiv">
			<table id="doInventoryChargesTableId" class="table pro-table" >
				<tr style="
    background: #eae7e7;">
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
			
				<tr>
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="conveyance1" id="Conveyance1" class="form-control" >
							<form:option value="">--Select--</form:option>
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
					
						<form:input path="conveyanceAmount1" id="ConveyanceAmount1"   type="number"  placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off"/>
					</td>
					<td>
						<form:select path="gstTax1" id="GSTTax1" class="form-control GSTClass"  onchange="calculateGSTTaxAmount(1)">
							<form:option value="">--Select--</form:option>
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
						<form:input path="gstAmount1" id="GSTAmount1" type="number" placeholder="GST Amount noneClass"  class="form-control noneClass" autocomplete="off"/>
					</td>
						<td>
						<form:input path="amountAfterTax1" type="number"  id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path="transportInvoice1"  type="text" id="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					
					 
					<td>
						<button type="button" name="addNewChargesItemBtn" id="addNewChargesItemBtnId1" onclick="appendChargesRow()" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</table>
			</div><br><br>
			<div class="container">
				<div class="row" style="padding:20px;">
					<div class="col-sm-6"><div class="comment" style="text-align:left;"><span class="h4" style="margin-right:20px;">Note:</span><form:textarea path="Note" id="NoteId" class="form-control"/></div></div>
					<div class="col-sm-6"><span class="h4" style="margin-top:20px;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv"></span></div>
				</div>
				</div> <br><br>
		<%-- 	<label class="control-label col-sm-1  col-xs-12"> NOTE:
							<%= note %> <%= colon %>
			</label>
			<div class="col-sm-3  col-xs-12">
						<form:textarea path="Note" id="NoteId" class="form-control"/>
					</div> --%>
				<!-- 	<label class="control-label col-sm-4  col-xs-12">
							Final Amount <span id="finalAmntDiv"></span>
			</label> -->
				<!-- <h2 class="center-block" style="font-size: 24px;font-weight: 600;"><strong>Final Amount:</strong></h2><span id="finalAmntDiv" class="finalAmountDiv"></span> -->
				<%-- 	<div class="col-sm-3  col-xs-12">
						<form:textarea path="note" id="NoteId" class="form-control" value="${getInvoiceDetails.note}" readonly="true"/>
					</div> --%>
			<input type="hidden" name="numbeOfRowsToBeProcessed"  id="countOfRows">
			<input type="hidden" name="numbeOfTransRowsToBeProcessed"  id="countOfTransRowsRows">
			<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked"  id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntryId"  id="ttlAmntForIncentEntryId" >
			
</form:form>
		
			
	</div>
	
	</div>
			<div class="form-group clearfix ">
					<div class=" col-sm-12 form-submit" >
					<div class="Btnbackground">
					<!-- <input class="btn btn-warning col-sm-2 col-sm-offset-4" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()"> -->
					</div>
						<!-- <input class="tblchargesbtn" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()"> -->
						<!-- <input class="tblchargesbtn" type="button" value="View GRN" id="viewGrnBtnId" onclick="viewGRN()"> -->
						<!-- <input class="tblchargesbtn btn btn-warning col-sm-2" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')"> -->
						<!-- <input class="tblchargesbtn" type="button" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')"> -->
				
			
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
		<script src="js/DcFormView.js" type="text/javascript"></script>
		
		
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
			
			
		 	$('#Conveyance1').change(function() {
				if ($(this).val() == "999$None"){
				  // Updating text input based on selected value
				  $(".noneClass").val(0);
				  $(".GSTClass").val( '1$0%');
				  }else{
					  $(".noneClass").val( " ");
				  }
				}); 
		 	
		 	
		 	function saveRecords(SaveClicked) {debugger;
			alert();
			document.getElementById("hiddenSaveBtnId").value = SaveClicked;

			var valStatus = appendRow();

			if(valStatus == false) {
				return;
			}
			//calculateOtherCharges();
			var otherChrgs = document.getElementById("Conveyance1").value;

			if(otherChrgs == "" || otherChrgs == '' || otherChrgs == null) {
				alert("Please enter Other Chargers.");
				document.getElementById("OtherChargesId").focus();
				return;
			}
			if(otherChrgs == "." || otherChrgs == "0." || otherChrgs == ".0" || otherChrgs == ".00") {
				alert("Please enter Other Chargers.");
				document.getElementById("OtherChargesId").focus();
				return;
			}
			else {
				//document.getElementById("calculateBtnId").click();
			}

			var canISubmit = window.confirm("Do you want to Update?");

			if(canISubmit == false) {
				return false;
			}


			//alert("before saving "+getAllProdsCount());
			document.getElementById("saveBtnId").disabled = true;	
			document.getElementById("countOfRows").value = getAllProdsCount();
			document.getElementById("countOfTransRowsRows").value = getChargeTotalSNOS();
			document.getElementById("doDCUpdateFormId").action = "updateDCForm.spring";
			document.getElementById("doDCUpdateFormId").method = "POST";
			document.getElementById("doDCUpdateFormId").submit();
		}
		 	
		 	function getChargeTotalSNOS() {

		 		var allElements = document.getElementsByTagName("*");
		 		var snos = "";
		 		for (var i = 0, n = allElements.length; i < n; ++i) {
		 			var el = allElements[i];
		 			if (el.id) {
		 				var ask = el.id;
		 				if(ask.indexOf("snoChargesDivId") != -1) {
		 					snos = snos+document.getElementById(el.id).textContent+"|";
		 				}
		 			}
		 		}
		 		return snos;
		 	}


		</script>
</body>
</html>
