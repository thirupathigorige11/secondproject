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
<style>
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
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
//Create Charges  DIV element and append to the table cell

function createChargesCell(cell, text, style, fldLength, cellsLen, tableColumnName) {

     var vfx = fldLength;     

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
function saveEnquiry() {
	
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
	
	/*if(valStatus == false) {
    	return;
	}*/
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
							<li class="breadcrumb-item active">Create PO</li>
						</ol>
					</div>
					
					
					<div>
	<div align="center">
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal" enctype="multipart/form-data">
		<div class="container">
	 <div class="row">
	  <div class="col-xs-12">
	  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Vendor Name : </label>
			<div class="col-sm-2 col-xs-12" >
				<form:input path="vendorName" id="VendorNameId" class="form-control" value="${vendorName}" readonly="true" />
			</div>
						<label class="control-label col-sm-2" style="width:13%;">GSTIN : </label>
			<div class="col-sm-2 col-xs-12">
				<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" value="${strGSTINNumber}" autocomplete="off" readonly="true" />
			</div>
			<label class="control-label col-sm-2" style="width:13%;">Vendor Address : </label>
			<div class="col-sm-2 col-xs-12" >
				<form:input path="vendorAddress" id="VendorAddress" class="form-control" value="${vendorAddress}" readonly="true" />
			</div>
			
		
			
			<div class="col-sm-2 col-xs-12" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control" value="${vendorId}" />
			</div>
		</div>
			  <div class="form-group col-xs-12" style="margin-bottom:22px;">
	  
	
	
			<label class="control-label col-sm-2" style="width:13%;">Indent No : : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value=${indentNo}>
			</div>
			
			
		 	<label class="control-label col-sm-2" style="width:13%;">SiteName : : </label>
			<div class="col-sm-2 col-xs-12" >
				<input type="text" id=siteName" name="SiteName" class="form-control" readonly="true" value=${SiteName}>
				<input type="hidden" id=toSiteId" name="toSiteId" class="form-control" readonly="true" value=${SiteId}>
			</div>
			

			
			<div class="col-sm-2 col-xs-12" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control"  />
			</div>
		</div>
		
		
		<div class="form-group col-xs-12" style="margin-bottom:22px;">
		</div>
	</div>
</div>
</div>
				<div class="table-responsive" style="border: 1px solid #b7b7b7; margin-top: 65px;">
					<table id="tblnotification"	class="table table-striped table-bordered" cellspacing="0">
						<thead>
							<tr class="tblheaderall" style="background: rgb(193, 193, 193);">
								<th>S.No</th>
								<th>ProductName</th>
								<th>SubProductName</th>
								<th>ChildProductName</th>
								<th>Vendor Description</th>
								<th>Measurement</th>
								<th>PO Pending Quantity</th>
								<th>Quantity</th>
								<th>HSN Code</th>
								<th>Price</th>
								<th>Basic Amount</th>
								<th>Discount</th>
								<th>Discount after tax</th>
								<th>Tax</th>
								<th>Tax Amount</th>
								<th>Amount After Tax</th>
								<th>Other or Transport charges</th>
    							<th>TaxOnOtherOrTransportCharges</th>
    							<th>OtherOrTransportChargesAfterTax</th>
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
										poIntiatedQuantity = objProductDetails.getPendingQuantity() == null ? "" : objProductDetails.getPendingQuantity().toString();
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
										
										out.println(strProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strSubProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproduct);
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='childProductCustomerDesc"+strSerialNumber+"' name='childProductCustomerDesc"+strSerialNumber+"'/>");
										out.println("</td>");
										
										
										
										out.println("<td>");
										out.println(strMesurment);
										out.println("</td>");
										
										out.println("<td>");
										out.println(pendingQuantity);
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' id='stQantity"+strSerialNumber+"' name='stQantity"+strSerialNumber+"'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' name='hsnCode"+strSerialNumber+"'  />");
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' id='price"+strSerialNumber+"' name='price"+strSerialNumber+"' onchange='calculateTotalAmount("+strSerialNumber+")' />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='BasicAmountId"+strSerialNumber+"' name='basicAmount"+strSerialNumber+"'  />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<select id='tax"+strSerialNumber+"' name='Discount"+strSerialNumber+"'   onchange='calculateDiscountAmount("+strSerialNumber+")'>"+
												"<option value="+1+">--Select--</option><option value="+0+">0</option><option value="+1+">1</option><option value="+2+">2</option><option value="+3+">3</option><option value="+4+">4</option><<option value="+5+">5</option><option value="+6+">6</option><option value="+7+">7</option><option value="+8+">8</option><option value="+9+">9</option><option value="+10+">10</option>/select>");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='amountAfterTax"+strSerialNumber+"' name='amtAfterDiscount"+strSerialNumber+"'  />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<select id='taxAmount"+strSerialNumber+"'  name='tax"+strSerialNumber+"'  onchange='calculateTaxAmount("+strSerialNumber+")')>"+
										"<option value=''>--Select--</option><option value='1$0'>0</option><option value='2$5'>5</option><option value='3$12'>12</option><option value='4$18'>18</option><option value='5$28'>28</option></select>");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' id='TaxAmount"+strSerialNumber+"' name='taxAmount"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxAftertotalAmount"+strSerialNumber+"' name='amountAfterTax"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesId"+strSerialNumber+"' name='OtherOrTransportChargesId"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='TaxOnOtherOrTransportChargesId"+strSerialNumber+"' name='TaxOnOtherOrTransportChargesId"+strSerialNumber+"'   />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  id='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"'  name='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"' />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' class='ttamount' id='TotalAmountId"+strSerialNumber+"'  name='totalAmount"+strSerialNumber+"'  />");
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
				<br><br><br>
							<div class="clearfix"></div><br/>
			<div class="table-responsive protbldiv">
			<table id="doInventoryChargesTableId" class="table pro-table">
				<tr style="  background: #eae7e7;">
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
							<form:select path="" id="Conveyance1" name="Conveyance1" class="form-control" >
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
						<form:input path="" id="ConveyanceAmount1" name="ConveyanceAmount1" type="number"  placeholder="Please enter Amount"  class="form-control noneClass" autocomplete="off"/>
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
						<form:input path="" id="GSTAmount1" name="GSTAmount1" type="number" placeholder="GST Amount"  class="form-control noneClass" autocomplete="off"/>
					</td>
						<td>
						<form:input path="" type="number"  name="AmountAfterTax1" id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control noneClass" autocomplete="off"/>
					</td>
				
					<td>
						<form:input path=""  type="text" id="TransportInvoice1" name="TransportInvoice1" placeholder="Transport Invoice Number"  onkeydown="appendRow()"  class="form-control" autocomplete="off"/>
					</td>
					
					
					 
					<td>
						<button type="button" name="addNewChargesItemBtn" id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" id="addDeleteChargesItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				</td>
				</tr>
			</table>
			</div><br/><br/><br/>
			
			
				
				
			<div class="container">
			
					<div class=""><span class="h4" style="margin-top:20px; margin-left: 71%;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv" ></span></div>
				
				</div> <br><br>
				<br><br><br><br>
				<!-- <div class="col-sm-3 pt-10">
					<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>   -->  
				<div class="col-sm-2 pt-10"> 
					<input type="button"  class="btn btn-warning btn btn-info  col-sm-12 form-submit"   onclick="saveEnquiry()"  value="Save"  id="saveBtnId" >
				</div>
				
				<div class=" col-sm-12 form-submit" >
					<div class="Btnbackground">
						<input class="btn btn-warning col-sm-2 col-sm-offset-4" style="margin-top: -40px;" type="button" value="Calculate" id="calculateBtnId" onclick="calculateOtherCharges()">
					</div>
				</div>
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			

<!-- **************************************************************		 -->
			
		
			<br/>
 <%-- <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Terms and Conditions</h4>
        </div>
        <div class="modal-body" style="height: 231px;overflow-y: scroll;">
           <div class="checkbox"> 
           
           
           <input type="hidden" id="noofTermsANdConditionrows" name="noofTermsANdConditionrows" value="${requestScope['listTermsAndCondition'].size()}" />
				  <c:forEach var="GetProductDetails" items="${requestScope['listTermsAndCondition']}" step="1" begin="0">	
	        	 	
	        	 	<div style="float:left;margin-left: 11px;">		
	        	 	 <input class="check_list" name="check_list${GetProductDetails.intSerialNumber}" value="${GetProductDetails.strTermsConditionId}" type="checkbox">
	        	 	    ${GetProductDetails.strTermsConditionName}</br>
			         </div>	</br>
			       
				</c:forEach>
           
           
           
			<!-- <label>
			  <input class="check_list" name="check_list1" type="checkbox">
			  1. Prices and Taxes: As mentione above.
			</label></br>
            	<label>
			  <input class="check_list" name="check_list2" type="checkbox">
			     2. Fright : Included
			</label></br>
			<label>
			  <input class="check_list" name="check_list3" type="checkbox">
			 3. Payment: 15days from the date of delivery
			</label></br>
			<label>
			  <input class="check_list" name="check_list4" type="checkbox">
			  4. mention the PO ref No and GSTIN in Invoice Copy Compulsory
			</label></br>
			<label>
			  <input class="check_list" name="check_list5" type="checkbox">
			 5.E way Bill No:compulasory along with Invoice copy if applicable
			</label></br>
			<label>
			  <input class="check_list" name="check_list6" type="checkbox">
			 <p>6. Delivery Timings:9.30am to 1pm and 2pm to 5pm(Working days only)</p>
			</label></br>
			<label>
			  <input class="check_list" name="check_list7" type="checkbox">
			  <p>7.Purchase resurves the right to cancel/hold the purchase order it the deliveries arenot satisfied.</p>
			</label></br> -->
			
		</div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-warning 	 form-submit" style="margin-right: 221px;width: 121px;" data-dismiss="modal" onclick="createPO()">Submit</button>
       																																	<!-- saveRecords(1)-->
        </div>
      </div>
      
    </div>
  </div> --%>
			
			
			<div class="form-group clearfix ">
		
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
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
         <script src="js/CreatePO.js" type="text/javascript"></script> 
		
		
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
			 	
	
		</script>
</body>
</html>
