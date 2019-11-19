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

#tblnotification thead, #tblnotification tbody tr, #doInventoryChargesTableId thead, #doInventoryChargesTableId tbody tr{table-layout:fixed;display:table;width:100%;}
#tblnotification thead tr th:first-child, #tblnotification tbody tr td:first-child,#doInventoryChargesTableId thead tr th:first-child, #doInventoryChargesTableId tbody tr td:first-child{width:52px;min-width:23px;}
#tblnotification tbody tr td, #doInventoryChargesTableId tbody tr td{border-top:0px !important;}
#doInventoryChargesTableId thead tr th:last-child, #doInventoryChargesTableId tbody tr td:last-child{width:100px;min-width:90px;}
.form-control{height:34px !important;}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.tdspan{
display:inline-block;
margin-top: -35px;
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
 height: 32px;
 margin-left:15px;
 }
 
 .myinputstyles{
 display: inline !important;
 width: 75% !important;
 border-radius: 0px !important;
 margin-bottom: 10px;
 border: none;
 box-shadow: none;
 border-bottom: 2px solid #000;
 }
 .breadcrumb{margin-left:0px !important;}
 .nav-md .container.body .right_col {
    padding: 10px 20px 0;
    margin-left: 0px !important;
    background: #fff;
}
</style>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
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
    padding-left: 10px;

	
	}
.tblprodindissudiv1 tbody {
    display: inline-block;
    max-height: 200px;
    overf
}

@media only screen and (max-width: 767px) and (min-width: 320px){
.nav-md .container.body .right_col {padding-top:25px !important;padding:15px;}
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
    	    div.setAttribute("onchange", "changeconveyance("+vfx+")");
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "--Select--"; 
    	    //defaultOption.value = "";
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
		    div.setAttribute("onblur", "calculateGSTTaxAmount("+vfx+")");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("placeholder", 'Please enter amount');
		    div.setAttribute("onkeypress", "return isNumberCheck(this, event)");
		   
		    cell.appendChild(div);
   		}
    	   	
    	else if(tableColumnName == taxChargeColumn) {
    		var dynamicSelectBoxId = "GSTTax"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("class", 'form-control');
    	    div.setAttribute("id",tableColumnName+vfx);
    	    div.setAttribute("onblur", "calculateGSTTaxAmount("+vfx+")");
    	    div.setAttribute("onkeypress", "return isNumberCheck(this, event)");
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
		    div.setAttribute("placeholder", 'GST Amount');
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    cell.appendChild(div);
   		}   	
    
    	else if(tableColumnName == chargesAmountAfterTaxColumn) {   		
    		var div = document.createElement("input");
		    div.setAttribute("type", "number");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("readonly", 'true');
		    div.setAttribute("placeholder", 'Amount after tax');
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

 function createModelPOPUP(){debugger;
	 var validate=calculateOtherCharges();
	 if(validate==false){
		 return false;
	 }
	/* var transportDetails = document.getElementById("Conveyance1").value;
	if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
		//alert("Please Select the Conveynace or None .");
		document.getElementById("Conveyance1").focus();
		return ;
	} else if(transportDetails !== "" || transportDetails !== '' || transportDetails !== null){ */
	$('.modal').modal('show');

	/* } */
}; 
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
<%-- 				<div class="col-md-3 left_col" id="left_col">
					<div class="left_col scroll-view">
             
						<div class="clearfix"></div>

						<jsp:include page="SideMenu.jsp" />  
						
					</div>
					</div> --%>
						<jsp:include page="Vendor_TopMeneu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<!-- <div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active"></li>
						</ol>
					</div> -->
					
					
	<div>
	<div align="center">
		<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal"  enctype="multipart/form-data">
		<div class="container border-inwards">	
		  <div class="col-md-12">
		    <div class="col-md-4">
			     <div class="form-group">
					<label class="control-label col-md-6">Vendor Name : </label>
					<div class="col-md-6" >
					  <form:input path="vendorName" id="VendorNameId" class="form-control" value="${vendorName}" title="${vendorName}" readonly="true" />
				    </div>
				  </div>
			</div>
			<div class="col-md-4">
			     <div class="form-group">
						<label class="control-label col-md-6">GSTIN : </label>
						<div class="col-md-6">
							<form:input path="strGSTINNumber" id="GSTINNumber" class="form-control" title="${strGSTINNumber}" value="${strGSTINNumber}"  autocomplete="off" readonly="true" />
						</div>
				</div>
			</div>
			 <div class="col-md-4">
			     <div class="form-group">
					<label class="control-label col-md-6">Vendor Address : </label>
					<div class="col-md-6" >
						<form:input path="vendorAddress" id="VendorAddress" class="form-control" value="${vendorAddress}" title="${vendorAddress}" readonly="true" />
					</div>
				</div>
			</div>
			 
			 <div class="col-md-4">
			     <div class="form-group">		
					<label class="control-label col-md-6">Indent No :</label>
					<div class="col-md-6">
						<input type="hidden" id="indentNumber" name="indentNumber" class="form-control" readonly="true" value="${indentNo}" />
						<input type="text" id="siteWiseIndentNo" name="siteWiseIndentNo" class="form-control" readonly="true" value="${siteWiseIndentNo}" />
					</div>
				</div>
			</div>
			<div class="col-md-4">
			   <div class="form-group">
				 	<label class="control-label col-md-6">Project Name :</label>
					<div class="col-md-6" >
						<input type="text" id="siteName" name="SiteName" class="form-control" readonly="true" value="${SiteName}" title="${SiteName}" />
						<input type="hidden" id="toSiteId" name="toSiteId" class="form-control" readonly="true" value="${SiteId}" />
					</div>
			  </div>
			</div>
			<div class="col-md-4">
			     <div class="form-group">
					<label class="control-label col-md-6">Address :  </label>
					<div class="col-md-6" >
						<input type="text" id="address" name="address" class="form-control" readonly="true" class='toooltip' data-toggle='tootlip' title='${address}'value="${address}">
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
			<div class="col-md-4" >
				<input type="hidden" id="vendorId" name="vendorId" class="form-control"  />
			</div>
		</div>
	</div>
   
	<div class="table-responsive Mrgtop10">
			<table id="tblnotification"	class="table table-striped table-new" style="width:2000px;">
						<thead class="cal-thead-inwards">
							<tr>
								<th>S NO</th>
								<th hidden>Product Name</th>
								<th hidden>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Vendor Description</th>
								<th>UOM</th>
								<th>Enquiry Quantity</th>
								<th>Vendor Quantity</th>
								<th>HSN Code</th>
								<th>Price Per unit</th>
								<th hidden>Basic Amount</th>
								<th>Discount(%)</th>
								<th hidden>Amount after Discount</th>
								<th>Tax</th>
								<th hidden>Tax Amount</th>
								<th hidden>Amount After Tax</th>
								<th hidden>Other or Transport charges</th>
    							<th hidden>Tax On Other Or Transport Charges</th>
    							<th hidden>Other Or Transport Charges After Tax</th>
								<th>Total Amount</th>
								<th >Remarks</th>
							</tr>
						</thead>
						<tbody class="tbl-fixedheader-tbody">
							
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
									String productBatchData = "";
									
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
										productBatchData = objProductDetails.getProductBatchData() == null ? "" : objProductDetails.getProductBatchData().toString();
										
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
										out.println("<input type='text' name='indentCreationDetailsId"+strSerialNumber+"' value='"+indentCreationDetailsId+"' />");
										out.println("<input type='text' name='productBatchData"+strSerialNumber+"' value='"+productBatchData+"' />");
										
										
										
										//out.println("<input type="hidden" name="poIntiatedQuantity${element.strSerialNumber}" value="${element.poIntiatedQuantity}" />");
										
										out.println("</div>");
										out.println("<tr class='tblrowClass'>");
									//	out.println("<td><input type='checkbox' name='checkboxname"+strSerialNumber+"' value='checked'></input></td>");
										if (strSearchType.equals("ADMIN")) {

											out.println("<td title = '" + strMouseOverDate + "'>");

										} else {
											out.println("<td>");

										}
										out.println("<div  id='snoDivId"+strSerialNumber+"' style='width:30px;'>"+strSerialNumber+"</div>");
										out.println("</td>");
										
										out.println("<td hidden>");
										
										out.println("<div title="+strProduct+">"+strProduct+"</div>");
										out.println("</td>");

										out.println("<td hidden>");
										out.println("<div title="+strSubProduct+">"+strSubProduct+"</div>");
										out.println("</td>");

										out.println("<td>");%>
										<input type="text" class="form-control" data-toggle="tooltip" title="<%=strChildproduct%>" value="<c:out value='<%=strChildproduct%>'/>" readonly/>
										<%out.println("</td>");

										
										out.println("<td>");
										out.println("<input  class='form-control type='text' id='childProductCustomerDesc"+strSerialNumber+"'  class='form-control_vendor Myclass' name='childProductCustomerDesc"+strSerialNumber+"'/>");
										out.println("</td>");
										
										
										
										out.println("<td>");
										out.println("<div class='measurment'>"+strMesurment+"</div>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<div data-toggle='tootlip' title='"+pendingQuantity+"'>"+pendingQuantity+"</div>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'class='form-control' id='stQantity"+strSerialNumber+"'  class='form-control_vendor Myclass'  onkeypress='return isNumberCheck(this, event)' onblur='maincalculatequantitybase(this,"+strSerialNumber+")' name='stQantity"+strSerialNumber+"' autocomplete='off'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text'  name='hsnCode"+strSerialNumber+"'  class='form-control_vendor form-control' />");
										out.println("</td>");

										out.println("<td>");
										out.println("<input type='text' class='form-control' id='price"+strSerialNumber+"' name='price"+strSerialNumber+"'  class='form-control_vendor Myclass' onkeypress='return isNumberCheck(this, event)'  onblur='maincalculatequantitybase(this,"+strSerialNumber+")' onblur='calculateTotalAmount("+strSerialNumber+")' autocomplete='off' />");
										out.println("</td>");

										
										out.println("<td hidden>");
										out.println("<input type='text' class='form-control' class='form-control_vendor Myclass' id='BasicAmountId"+strSerialNumber+"' name='basicAmount"+strSerialNumber+"'  />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<input type='text' class='form-control_vendor Myclass form-control' id='Discount"+strSerialNumber+"' name='Discount"+strSerialNumber+"' onkeypress='return isNumberCheck(this, event)' onblur='maincalculatequantitybase(this,"+strSerialNumber+")'  onblur='calculateDiscountAmount("+strSerialNumber+")' autocomplete='off'/>");
											
										out.println("</td>");

										
										out.println("<td hidden>");
										out.println("<input type='text'   class='form-control_vendor form-control' id='amtAfterDiscount"+strSerialNumber+"' name='amtAfterDiscount"+strSerialNumber+"' autocomplete='off' />");
										out.println("</td>");

										
										out.println("<td>");
										out.println("<select id='taxAmount"+strSerialNumber+"'  name='tax"+strSerialNumber+"'   class='form-control_vendor Myclass form-control' onblur='maincalculatequantitybase(this,"+strSerialNumber+")' onblur='calculateTaxAmount("+strSerialNumber+")')>"+
										"<option value=''>Select</option><option value='1$0'>0 %</option><option value='2$5'>5 %</option><option value='3$12'>12 %</option><option value='4$18'>18 %</option><option value='5$28'>28 %</option></select>");
										out.println("</td>");

										
										out.println("<td hidden>");
										out.println("<input type='text'  id='TaxAmount"+strSerialNumber+"' name='taxAmount"+strSerialNumber+"'  class='form-control_vendor form-control' />");
										out.println("</td>");
										
										out.println("<td hidden >");
										out.println("<input type='text'  id='TaxAftertotalAmount"+strSerialNumber+"' name='amountAfterTax"+strSerialNumber+"'   class='form-control_vendor'/>");
										out.println("</td>");
										
										out.println("<td hidden>");
										out.println("<input type='text'  id='OtherOrTransportChargesId"+strSerialNumber+"' name='OtherOrTransportChargesId"+strSerialNumber+"'  readonly='true' class='form-control_vendor'/>");
										out.println("</td>");
										
										out.println("<td hidden >");
										out.println("<input type='text'  id='TaxOnOtherOrTransportChargesId"+strSerialNumber+"' name='TaxOnOtherOrTransportChargesId"+strSerialNumber+"' readonly='true'   class='form-control_vendor'/>");
										out.println("</td>");
										
										out.println("<td hidden>");
										out.println("<input type='text' id='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"'  name='OtherOrTransportChargesAfterTaxId"+strSerialNumber+"'   class='form-control'/>");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' style='background: #d8d2d2;' class='ttamount form-control_vendor Myclass form-control'  readonly='true'  id='TotalAmountId"+strSerialNumber+"'  name='totalAmount"+strSerialNumber+"'  />");
										out.println("</td>");
										
										out.println("<td>");
										out.println("<input type='text' style=' ' class=' form-control_vendor Myclass form-control' id='Remarks"+strSerialNumber+"'  name='Remarks"+strSerialNumber+"'  />");
										out.println("</td>");

									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		              
		               </tbody>
					</table>
			
				</div>
<div>
				<% String hiddenFieldHtml1="<input type=\"hidden\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); %> 
			</div>
				
			<div class="clearfix"></div>
			<div class="table-responsive Mrgtop10">
			<table id="doInventoryChargesTableId" class="table pro-table table-new ">
				<thead class="cal-thead-inwards">
				 <tr>
					<th><%= sNumber %></th>
    				<th><%= conveyanceCharges %></th>
    				<th><%= conveyanceAmount %></th>
    				<th><%= conveyanceTax %></th>    								
    				<th ><%= GSTAmount %></th>
   					<th><%= chargesAmountAfterTax %></th>
    				<th style="text-align: center;"><%= actions %></th>
  				</tr>
				</thead>
			    <tbody class="tbl-fixedheader-tbody">
				<tr class="conveyancetrClass" id="chargesrow1">
					<td>						
						<div id="snoChargesDivId1">1</div>
					</td>
					<td> 						
							<form:select path="" id="Conveyance1" name="Conveyance1" class="form-control_vendor form-control" onchange="changeconveyance(1)">
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
						<form:input path="" id="ConveyanceAmount1" name="ConveyanceAmount1" type="text" onblur="calculateGSTTaxAmount(1)" onkeypress="return isNumberCheck(this, event)"  placeholder="Please enter amount"  class="form-control_vendor noneClass form-control" autocomplete="off" min="0"/>
					</td>
					<td>
						<form:select path="" id="GSTTax1" name="GSTTax1" class="form-control_vendor GSTClass form-control"  onblur="calculateGSTTaxAmount(1)"  >
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
						<form:input path="" id="GSTAmount1" name="GSTAmount1"   placeholder="GST Amount" class="form-control_vendor noneClass  form-control" autocomplete="off" readonly="true"/>
					</td>
					<td>
						<form:input path="" type="number"  name="AmountAfterTax1" id="AmountAfterTax1" placeholder="Amount After Tax"  class="form-control_vendor noneClass form-control" autocomplete="off" readonly="true" min="0"/>
					</td>
					<td>
						<button type="button" name="addNewChargesItemBtn"  id='addNewChargesItemBtnId1' onclick="appendChargesRow(1)" class="btnaction "><i class="fa fa-plus"></i></button>
						<button type="button" style="display:none;" name="addDeleteItemBtn" id="addDeleteItemBtnId1" class="btnaction" onclick="deleteRow(this, 1)" ><i class="fa fa-trash"></i></button>
				   </td>
				   </tr>
				</tbody>
			</table>
			</div>
			
			
				
				
			<div class="container">
					<div class="col-md-4 col-md-offset-8"><span class="h4" style="margin-top:20px;"><strong>Final Amount:</strong></span><span id="finalAmntDiv" class="finalAmountDiv" ></span></div>
				
			</div> <br><br>
				<!-- <div class="col-sm-3 pt-10">
					<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>   -->  
				<div class="col-md-12 text-center center-block"> 
					
				
					<input class="btn btn-warning" style="" type="button" value="Calculate"   id="calculateBtnId" onclick="calculateOtherCharges()"> <!-- data-toggle="myModal" -->
					<input type="button" onclick="createModelPOPUP()"  class="btn btn-warning form-submit modelcreatePO" onclick="calculateOtherCharges()" data-target="myModal" value="Submit" id="saveBtnId">
					
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
        <div class="modal-body" style="height: 231px;overflow-y: scroll;">
        <div class="container">
	<div class="row">

		<!-- <input type="hidden" name="count" value="1" /> -->
        <div class="control-group" id="fields">        
             <div class="controls" id="profs"> 
                <form class="input-append">
             	   <input type="hidden" id= "countOftermsandCondsfeilds" name="countOftermsandCondsfeilds" value="1" />
                    <div class="field margintopbtm"><input autocomplete="off" class="input form-control myinputstyles" style="border-top: none;border-left: none; border-right: none;" id="termsAndCond1" name="termsAndCond1" type="text" data-items="8"/><button id="b1" class="btn btn-success add-more mybtnstyles" type="button" onclick="addMore(1)"><i class="fa fa-plus"></i></button><button id="closebtn1" class="btn btn-danger mybtnstyles" type="button" onclick="remove(1)" style="display:none;"><i class="fa fa-close"></i></button></div>
                </form>
            <br>
            
            </div>
        </div>
	</div>
</div>

        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
          <div class="col-md-12 text-center center-block">
            <button type="button" class="btn btn-warning form-submit" style=";" data-dismiss="modal" onclick="saveEnquiry()">Submit</button>
          </div>
         
       																																	<!-- saveRecords(1)-->
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

		</div>
		

	</div>
	</div>
	</div>
	
	
					
					<!-- /page content -->        
				</div>
			</div>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
         <script src="js/CreatePO_Vendor.js" type="text/javascript"></script> 
		<script>
		
			$(document).ready(function() {	
				 $('[data-toggle="tooltip"]').tooltip(); 
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
		 	$('#ccemailcheckbox').on('click', function(){
				$('.form-control').show();				
			});
		 	$('#sub-conmatiner').on('click', function(){
				$('.form-control1').show();				
			});
		 	jQuery(document).ready(function($) {
			 	   // STOCK OPTIONS
			 		$('#sub-conmatiner').click(function(){
			 			if ($(this).is(':checked'))
			 	    $(this).next('form-control1').show();
			 	else
			 	    $(this).next('form-control1').hide();
			 		})
			 	});
		 	
			$(document).ready(function(){
				  $('.Myclass').keyup(function(){
				    $(this).attr('title',$(this).val());
				  });
				});
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
	  var newIn = '<div class="clearfix"></div><input autocomplete="off" class="input form-control myinputstyles" style="border-radius: 0px !important;margin-bottom: 10px;border: none;box-shadow: none;border-bottom: 1px solid #ccc;" id="termsAndCond' + id+ '" name="termsAndCond' + id + '" type="text"/><button type="button" class="btn btn-danger mybtnstyles" style="display:none;" id="closebtn'+id+'" onclick="remove('+id+')""><i class="fa fa-close" ></i></button><button type="button" id="b'+id+'" class="btn btn-success mybtnstyles" onclick="addMore('+id+')"><i class="fa fa-plus" ></i></button>';
	  $(".field").append(newIn);
	  
}
 function remove(id){
	 $("#closebtn"+id).remove();
	 $("#termsAndCond"+id).remove();
} 
/* ************* Method for new text feild in model popup ************** */	
	
	   function validateChargesRow(){
		 var error=true;	
			$(".conveyancetrClass").each(function(){
				var id=$(this).attr("id").split("chargesrow")[1];
				//var chargesDelete=$("#chargesDelete").val();
				//if(chargesDelete!="D"){
					var Conveyance=$("#Conveyance"+id).val();
					if(Conveyance==""){
						alert("Please select Conveyance.");
						$("#Conveyance"+id).focus();
						return error=false;
					}
					var ConveyanceAmount=$("#ConveyanceAmount"+id).val();
					if(ConveyanceAmount==""){
						alert("Please enter Conveyance Amount.");
						$("#ConveyanceAmount"+id).focus();
						return error=false;
					}
					var GSTTax=$("#GSTTax"+id).val();
					if(GSTTax==""){
						alert("Please select GST Tax.");
						$("#GSTTax"+id).focus();
						return error=false;
					}
				//}
				
			});
			
			return error;
		}
	  //conveyance amount add validation
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
	   //conveyance amount add validation
	   
 </script>
</body>
</html>
