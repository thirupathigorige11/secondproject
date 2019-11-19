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
	
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.table-new tbody tr th{background-color:#ccc;border-top:1px solid #000 !important;}
.finalAmountDiv{
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
 
 .mrg-l-30{
	 margin-left:30px;
 }
 
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="CacheClear.jsp" />  
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	.pro-table tbody tr td:last-child, .pro-table tbody tr th:last-child {
    min-width: 100px;
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
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("onchange", 'changeconveyance('+vfx+')');
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
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("onkeypress", 'return isNumberCheck(this, event)');
		    div.setAttribute("placeholder", 'Please enter Amount');
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
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", "GST Amount");
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
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
	var valProdTable=validateProducttable();
	if(valProdTable==false){
		return false;
	}
	var valCharges=validateChargesRow();
	if(valCharges==false){
		return false;
	}
	calculateOtherCharges();
	$('.modal').modal('show');

}
function saveEnquiry() {
	
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
	
	/*if(valStatus == false) {
    	return;
	}*/
	var chargesRowCountNum=[];
	$(".myinputstyles").each(function(){
		var currentId=$(this).attr("id").split("termsAndCond")[1];	
		chargesRowCountNum.push(currentId);
	});
	$('#countOftermsandCondsfeilds').val(chargesRowCountNum);
	calculateOtherCharges();
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("ProductWiseIndentsFormId").action = "SaveEnquiryForm.spring";
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
							<li class="breadcrumb-item active">Fill Quotation</li>
						</ol>
					</div>
					
					
					<div>
	<div class="">
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" >
		
	  <div class="col-md-12 border-indent">
	  <div class="col-md-4">
	  <div class="form-group">
			<label class="control-label col-md-6">Vendor Name : </label>
			<div class="col-md-6" >
				<form:input path="vendorName" id="VendorNameId" class="form-control" data-toggle="tooltip" title="${vendorName}" value="${vendorName}" readonly="true" />
			</div>
	  </div>
	 </div>
	  <div class="col-md-4">
	  <div class="form-group">
						<label class="control-label col-md-6" >GSTIN : </label>
			<div class="col-md-6">
				<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" data-toggle="tooltip" title="${strGSTINNumber}" value="${strGSTINNumber}" autocomplete="off" readonly="true" />
			</div>
	</div>
	</div>
	<div class="col-md-4">
	<div class="form-group">
			<label class="control-label col-md-6">Vendor Address :</label>
			<div class="col-md-6" >
				<form:input path="vendorAddress" id="VendorAddress" class="form-control" data-toggle="tooltip" title="${vendorAddress}" value="${vendorAddress}" readonly="true" />
			</div>
	</div>
	</div>
		 
	<div class="col-md-4">
	<div class="form-group">
			<label class="control-label col-md-6">Site wise Indent No :</label>
			<div class="col-md-6" >
				<input type="hidden" id="indentNumber"   name="indentNumber" class="form-control" readonly="true" value=${indentNo}>
				<input type="text" id="siteWiseIndentNo" name="siteWiseIndentNo" class="form-control" readonly="true" value=${siteWiseIndentNo}>
			</div>
	</div>
	</div>
	<div class="col-md-4">
	<div class="form-group">
		 	<label class="control-label col-md-6">Site Name :</label>
			<div class="col-md-6" >
				<input type="text" id=siteName" name="SiteName" class="form-control" readonly="true" data-toggle="tooltip" title="${SiteName}" value="${SiteName}">
				<input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}>
			</div>
	</div>
	</div>
	<div class="col-md-4">
	<div class="form-group">	
			<div class="col-md-6" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" />
				<input type="hidden" id="pass" name="pass" class="form-control" value="${pass}" />
			</div>
	</div>
	</div>
	<div class="col-md-6" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control"  />
				<input type="hidden" id="fillform" name="fillform" value="true" />
	</div>
	</div>
   
   <div class="clearfix"></div>
   <div class="table-responsive">
					<table id="tblnotification"	class="table table-new" cellspacing="0" style="width:2500px;">
						<thead>
							<tr> <!-- class="tblheaderall" -->
								<th>S.No</th>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Vendor Description</th>
								<th>Measurement</th>
								<th>PO Pending Quantity</th>
								<th>Quantity</th>
								<th>HSN Code</th>
								<th>Price</th>
								<th>Basic Amount</th>
								<th>Discount</th>
								<th>Amount After Discount</th>
								<th>Tax</th>
								 <th>Tax Amount</th> 
								<th>Amount After Tax</th>
								<th>Other or Transport charges</th>
    							<th>Tax On Other Or Transport Charges</th>
    							<th>Other Or Transport Charges AfterTax</th>
								<th>Total Amount</th>
							</tr>
						</thead>
						<tbody>
							</tr>
							<%
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
									String indentCreationDetailsId = "";
									
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
										pendingQuantity = objProductDetails.getPendingQuantity() == null ? "" : objProductDetails.getPendingQuantity().toString();
										strMesurment = objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										siteName = objProductDetails.getSiteName() == null ? "" : objProductDetails.getSiteName().toString();										
										strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();	
										strMouseOverDate = objProductDetails.getStrOtherSiteQtyDtls() == null ? "" : objProductDetails.getStrOtherSiteQtyDtls().toString();
										strIndentNo = objProductDetails.getStrIndentId() == null ? "" : objProductDetails.getStrIndentId().toString();
										purchaseDeptIndentProcessSeqId = Integer.parseInt(objProductDetails.getPurchaseDeptIndentProcessSeqId() == null ? "0" : objProductDetails.getPurchaseDeptIndentProcessSeqId().toString());
										poIntiatedQuantity = objProductDetails.getQuantity() == null ? "" : objProductDetails.getQuantity().toString();
										requestQantity = objProductDetails.getRequestQantity() == null ? "" : objProductDetails.getRequestQantity().toString();
										indentCreationDetailsId = objProductDetails.getIndentCreationDetailsId() == null ? "" : objProductDetails.getIndentCreationDetailsId().toString();
										
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
										out.println("<input type='hidden' name='indentCreationDetailsId"+strSerialNumber+"' value='"+indentCreationDetailsId+"' />");
										
										
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
										out.println("<input type='text' id='strProduct"+strSerialNumber+"' style='width:134px'; class='form-control' name='strProduct"+strSerialNumber+"' title='"+strProduct+"' value='"+strProduct+"' readonly='true'/>");
										//out.println(strProduct);
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' id='strSubProduct"+strSerialNumber+"' style='width:134px'; class='form-control' name='strSubProduct"+strSerialNumber+"' title='"+strSubProduct+"' value='"+strSubProduct+"' readonly='true'/>");
										//out.println(strSubProduct);
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' id='strChildproduct"+strSerialNumber+"' style='width:134px'; class='form-control' name='strChildproduct"+strSerialNumber+"' title='"+strChildproduct+"' value='"+strChildproduct+"' readonly='true'/>");
										//out.println(strChildproduct);
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='childProductCustomerDesc"+strSerialNumber+"' style='width:134px'; class='form-control' name='childProductCustomerDesc"+strSerialNumber+"'/>");
										out.println("</td>");
										
										
										
										out.println("<td>");
										out.println(strMesurment);
										out.println("</td>");
										
										out.println("<td>");
										/* out.println(pendingQuantity); */
										out.println("<input type='text' class='form-control' ' style='width:139px' value='"+poIntiatedQuantity+"' readonly='true'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' id='stQantity"+strSerialNumber+"' class='form-control' ' style='width:80px' onblur='calculateChangedAmount("+strSerialNumber+")'  onkeypress='return isNumberCheck(this, event)' name='stQantity"+strSerialNumber+"' autocomplete='off'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'' class='form-control' ' style='width:67px' name='hsnCode"+strSerialNumber+"'  />");
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' id='price"+strSerialNumber+"' onblur='calculateChangedAmount("+strSerialNumber+")' onkeypress='return isNumberCheck(this, event)' class='form-control'' style='width:72px' name='price"+strSerialNumber+"' autocomplete='off' onblur='calculateTotalAmount("+strSerialNumber+")' />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' onblur='calculateChangedAmount("+strSerialNumber+")' id='BasicAmountId"+strSerialNumber+"'class='form-control'' style='width:93px' name='basicAmount"+strSerialNumber+"'  readonly='true'/>");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='Discount"+strSerialNumber+"' ' class='form-control' name='Discount"+strSerialNumber+"'  onkeypress='return isNumberCheck(this, event)' onblur='calculateChangedAmount("+strSerialNumber+")' autocomplete='off' onblur='calculateDiscountAmount("+strSerialNumber+")'/>");
											
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='amtAfterDiscount"+strSerialNumber+"' ' class='form-control'' style='width:141px' name='amtAfterDiscount"+strSerialNumber+"' readonly='true' />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<select id='taxAmount"+strSerialNumber+"'' class='form-control' ' style='width:80px' name='tax"+strSerialNumber+"' onblur='calculateChangedAmount("+strSerialNumber+")' onblur='calculateTaxAmount("+strSerialNumber+")')>"+
										"<option value=''>--Select--</option><option value='1$0'>0%</option><option value='2$5'>5%</option><option value='3$12'>12%</option><option value='4$18'>18%</option><option value='5$28'>28%</option></select>");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='TaxAmount"+strSerialNumber+"'' style='width:115px'' class='form-control' name='taxAmount"+strSerialNumber+"' readonly='true' />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxAftertotalAmount"+strSerialNumber+"' ' style='width:115px'' class='form-control' name='amountAfterTax"+strSerialNumber+"'  readonly='true' />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesId"+strSerialNumber+"' ' style='width:170px'' class='form-control' name='OtherOrTransportChargesId"+strSerialNumber+"' readonly='true' />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxOnOtherOrTransportChargesId"+strSerialNumber+"' ' style='width:234px'' class='form-control' name='TaxOnOtherOrTransportChargesId"+strSerialNumber+"' readonly='true'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"' ' style='width:229px' class='form-control' name='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"' readonly='true' />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' class='ttamount form-control'' style='width:85px' id='TotalAmountId"+strSerialNumber+"'   name='totalAmount"+strSerialNumber+"'  readonly='true'/>");
										out.println("</td>");


									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		
					</table>
			
				</div>
				
<div>
				<% String hiddenFieldHtml1="<input type=\"hidden\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); %> 
			</div>
				
							<div class="clearfix"></div>
							
						
			<div class="table-responsive Mrgtop20">  <!-- protbldiv" style="border: 0px;" -->
			<table id="doInventoryChargesTableId" class="table pro-table table-new">
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
			
				<tr class="chargesrowCls" id="chargesrow1">
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td>						
							<form:select path="" id="Conveyance1" name="Conveyance1" onchange="changeconveyance(1)" class="form-control" >
							<form:option value="">--Select--</form:option>
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
						<form:input path="" id="ConveyanceAmount1" name="ConveyanceAmount1" type="text" min="0" onkeypress="return isNumberCheck(this, event)" placeholder="Please enter Amount"  onblur="calculateGSTTaxAmount(1)" class="form-control noneClass" autocomplete="off"/>
					</td>
					<td>
						<form:select path="" id="GSTTax1" name="GSTTax1" class="form-control GSTClass " style="width: 146px" onblur="calculateGSTTaxAmount(1)">
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
						<form:input path="" id="GSTAmount1" name="GSTAmount1" type="text" readonly="true" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off"/>
					</td>
						<td>
						<form:input path="" type="number"  name="AmountAfterTax1" id="AmountAfterTax1" readonly="true" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					
					 
					<td>
						<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" style="display:none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</table>
			</div>
		
			
			
				
			<div class="container">
				<div class="col-md-offset-6 col-md-6">
					<div class="col-md-5 text-right"><h4><strong>Final Amount</strong></h4></div>
					<div class="col-md-1"><h4><strong>:</strong></h4></div>
					<div class="col-md-6 text-right no-padding-left no-padding-right"><h4><strong><span id="finalAmntDiv" class="finalAmountDiv" ></span></strong></h4></div>
				</div>
				</div> <br><br>
				<br><br><br><br>
				<!-- <div class="col-sm-3 pt-10">
					<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>   -->  
				<div class="col-md-12 text-center center-block pt-10"> 
				<input class="btn btn-warning"  type="button" value="Calculate" data-toggle="myModal"  id="calculateBtnId" onclick="calculateOtherCharges()">
					<input type="button"  onclick="createModelPOPUP()"  class="btn btn-warning btn btn-info form-submit modelcreatePO" onclick="calculateOtherCharges()" data-target="myModal" value="Submit" id="saveBtnId">				
						
					</div>
				
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="countOftermsandCondsfeilds" value="" id="countOftermsandCondsfeilds">

<!-- **************************************************************		 -->
			
		
			<br/>
 <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Terms and Conditions</h4>
        </div>
        <form action="" method="post">
        <div class="modal-body">
        <div class="container">
		   <div class="row">
	        <div class="col-md-12 control-group" id="fields">
	        	<div class="controls" id="profs"> 
	               <form class="input-append">
	             	   <input type="hidden" id= "countOftermsandCondsfeilds" name="countOftermsandCondsfeilds" value="1" />
	                   <div class="col-md-12 field margintopbtm"><input autocomplete="off" class="input form-control myinputstyles" style="border-radius: 4px !important;" id="termsAndCond1" name="termsAndCond1" type="text" data-items="8"/><button id="b1" class="btn btn-success add-more mybtnstyles mrg-l-30" type="button" onclick="addMore(1)"><i class="fa fa-plus"></i></button><button id="closebtn1" class="btn btn-danger mybtnstyles mrg-l-30" type="button" onclick="remove(1)" style="display:none;"><i class="fa fa-close"></i></button></div>
	               </form>
	                <br>
	            </div>
	        </div>
			</div>
		</div>
        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
          <button type="button" class="btn btn-warning 	 form-submit" style="margin-right: 221px;width: 121px;" data-dismiss="modal" onclick="saveEnquiry()">Submit</button>
       	</div>
      </div>
      </form>
    </div>
  </div>
 </div>		
			
			<div class="form-group clearfix ">
		
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
			</div>
			</form:form>
	</div></div>
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
        <script src="js/CreatePO_Fillform.js" type="text/javascript"></script> 
         <script src="js/sidebar-resp.js" type="text/javascript"></script> 
	 	<script src="js/numberCheck.js" type="text/javascript"></script>
		
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
		 	  /* ************ Method for validation discount************     */  
/* 	        function validatediscount(rowId){debugger;
	    	var discountval=$('#Discount'+rowId).val();
	    	if(discountval > 100){
	    		alert('Please enter the discount 100 below');
	    		document.getElementById('#Discount'+rowId).focus();
	    	}
	    	return 
	    }	 */ 	
	/* ************* Method for new text feild in model popup ************** */
/* $(document).ready(function(){
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
        $("#countOftermsandCondsfeilds").val(next);  
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
    

    
}); */
	//});
	/* ************* Method for new text feild in model popup ************** */
	  function addMore(id){
		var tc=$("#termsAndCond"+id).val();
		if(tc.length==0){
		    alert("Please enter condition");
		    $("#termsAndCond"+id).focus();
		    return false;
		 }
		$("#closebtn"+id).show();
		$("#b"+id).hide();
		var id = id+1;
		  var newIn = '<div class="clearfix"></div><input autocomplete="off" class="input form-control myinputstyles" style="border-radius: 4px !important;" id="termsAndCond' + id+ '" name="termsAndCond' + id + '" type="text"/><button type="button" class="btn btn-danger mybtnstyles mrg-l-30" style="display:none;" id="closebtn'+id+'" onclick="remove('+id+')""><i class="fa fa-close" ></i></button><button type="button" id="b'+id+'" class="btn btn-success mybtnstyles mrg-l-30" onclick="addMore('+id+')"><i class="fa fa-plus" ></i></button>';
		  $(".field").append(newIn);
		  
	}
	 function remove(id){
		 $("#closebtn"+id).remove();
		 $("#termsAndCond"+id).remove();
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
	/* ************* Method for new text feild in model popup ************** */	
		

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

		</script>
</body>
</html>
