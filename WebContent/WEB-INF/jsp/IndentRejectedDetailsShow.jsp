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
	String vendorDescription=resource.getString("label.vendorDescription");

	String remarks = resource.getString("label.remarks");
	String isNewOrOld = resource.getString("label.isNewOrOld");
	String Comments = resource.getString("label.Comments");
	
	//Loading Indent Receive Table Column Headers/Labels - Start
%>

<html>
<head>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.table-new tbody tr td .ui-icon-triangle-1-s {
    /* display: inline-block; */
    width: 0;
    /* height: 0; */
    margin-left: -4px;
    margin-top: -3px;
    vertical-align: middle;
    border-top: 4px dashed;
    border-top: 4px solid\9;
    border-right: 4px solid transparent;
    border-left: 4px solid transparent;
}
.custom-combobox input {
height:32px;
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
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
	<style>
	.input-group-addon{border:1px solid #ccc !important;}
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
	debugger;
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
	 
	 	 var Comments =  "<%= Comments %>";
	 	CommentsColumn = formatColumns(Comments);
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
	 
	 
<%-- 	 var expiryDate=  "<%= expiryDate %>";
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
    	    var dynamicTexboxId = "ProductId"+vfx;
    	    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", dynamicTexboxId);
		    div2.setAttribute("id", dynamicTexboxId);		    
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);
		    
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
    	    var dynamicTexboxId = "SubProductId"+vfx;
    	    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", dynamicTexboxId);
		    div2.setAttribute("id", dynamicTexboxId);		    
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);
    	    
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
    	    var dynamicTexboxId = "ChildProductId"+vfx;
    	    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", dynamicTexboxId);
		    div2.setAttribute("id", dynamicTexboxId);		    
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);
    	}   

    	else if(tableColumnName == measurementColumn) {
    		var dynamicSelectBoxId = tableColumnName+"Id"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("onchange", "return validateProductAvailability(this);");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	    
    	    var dynamicTexboxId = "UnitsOfMeasurementIdval"+vfx;
    	    var div2 = document.createElement("input");
		    div2.setAttribute("type", "hidden");
		    div2.setAttribute("name", dynamicTexboxId);
		    div2.setAttribute("id", dynamicTexboxId);		    
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);
    	}   		
		else if(tableColumnName == quantityColumn) {
			
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
	    	div.setAttribute("onblur", "return validateBOQQuantity("+vfx+", 'New');");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("style", 'width:100px;float:left;margin-right:15px;');
		    cell.appendChild(div);
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    div2.setAttribute("style", 'width:100px;')
		    cell.appendChild(div2);
		    
		    var div3 = document.createElement("input");
		    div3.setAttribute("type", "hidden");
		    div3.setAttribute("name", "groupId"+vfx);
		    div3.setAttribute("id", "groupId"+vfx);
		    cell.appendChild(div3);  
		    
		    var div4 = document.createElement("input");
		    div4.setAttribute("class", "sumofrecieveQty");
		    div4.setAttribute("type", "hidden");
		    div4.setAttribute("id", "sumofrecieveQty"+vfx);
		    cell.appendChild(div4); 
		    
		    var div5 = document.createElement("input");
		    div5.setAttribute("class", "sumofIssueQty");
		    div5.setAttribute("type", "hidden");
		    div5.setAttribute("id", "sumofIssueQty"+vfx);
		    cell.appendChild(div5); 
		    
		    var div6 = document.createElement("input");
		    div6.setAttribute("class", "indentPendingQty");
		    div6.setAttribute("type", "hidden");
		    div6.setAttribute("id", "indentPendingQty"+vfx);
		    cell.appendChild(div6); 
		    
		    var div7 = document.createElement("input");
		    div7.setAttribute("class", "BOQQty");
		    div7.setAttribute("type", "hidden");
		    div7.setAttribute("id", "BOQQty"+vfx);
		    cell.appendChild(div7); 
   		}  
   		else if(tableColumnName == remarksColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		   
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}else if(tableColumnName == CommentsColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		   
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
		    div1.setAttribute("onclick", "deleteRow("+vfx+")");
		   // div1.setAttribute("onclick",  "removeRow()");
		    
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);
		}   
    	
    }
     
}

function save() {
	//changes for validating schedule date and required date written by thirupathi
	debugger;
	var scheduleDate=$("#ScheduleDateId").val();
	var requiredDate=$("#RequiredDateId").val();
	if(scheduleDate=="" || scheduleDate==null){
	alert("Please Select Schedule Date.");
	$("#ScheduleDateId").focus();
	return false;
	}
	if(requiredDate=="" || requiredDate==null){
	alert("Please Select Required Date.");
	$("#RequiredDateId").focus();
	return false;
	}
	//end changes for validating schedule date and required date
	
	//validating product row
	 var valStatus = validateProductRow();		
		if(valStatus == false) {
	    	return false;
		}
	//calculateOtherCharges();
	var rowscount=$('#doInventoryTableId').find('tr').length;
	//alert("rowscount: "+rowscount);
	$("#numberOfRows").val((rowscount-1));
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	$(".loader-sumadhura").show();
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("countOfRows").value = $('#doInventoryTableId').find('tr').length;
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("indentCreationFormId").action = "approveRejectedIndentCreation.spring";
	document.getElementById("indentCreationFormId").method = "POST";
	document.getElementById("indentCreationFormId").submit();
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
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Rejected Indent Approval</li>
						</ol>
					</div>
					<!-- loader -->
						<div class="loader-sumadhura" style="display: none;z-index:999;">
							<div class="lds-facebook">
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
							</div>
							<div id="loadingMessage">Loading...</div>
						</div>
					    <!-- loader -->
					
					<div>
	<div class="col-md-12 col-sm-12 col-xs-12">
	
		<form:form modelAttribute="indentCreationModelForm" id="indentCreationFormId" class="form-horizontal" >
		<div class="border-indent">
	 <!-- <div class="row"> -->
	 <c:forEach var="getIndentCreation" items="${requestScope['IndentCreationList']}">
	 	   <div class="col-md-6 col-sm-6 col-xs-12">
	 	    <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">New Site Wise Indent Number :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input path="siteWiseIndentNo" id="siteWiseIndentNo"  readonly="true" class="form-control"/>
				<form:input path="IndentNumber" id="IndentNumberId"  readonly="true" type="hidden" class="form-control"/>
			</div>
			</div>
	 	   </div>
			<div class="col-md-6 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Indent Date :</label>
			<div class="col-md-6 col-sm-6 col-xs-12 input-group">
				<form:input path="ScheduleDate" id="ScheduleDateId" class="form-control readonly-color" autocomplete="off" readonly="true"/>
				<label class="input-group-addon btn" for="ScheduleDateId"> <span class="fa fa-calendar"></span> </label>
			</div>
		   </div>
			</div>
	  		<div class="col-md-6 col-sm-6 col-xs-12">
	   			<div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Indent From :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input path="IndentFrom" id="IndentFromId" name="IndentFrom" readonly="true" class="form-control"/>
			</div>
			</div>
	  		</div>
			<div class="col-md-6 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Indent To :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input path="IndentTo" id="IndentToId" name="IndentTo" readonly="true" class="form-control" />
			</div>
		   </div> 
			</div>	
			<div class="col-md-6 col-sm-6 col-xs-12">
			<div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Required Date :</label>
			<div class="col-md-6 col-sm-6 col-xs-12 input-group" >
				<form:input path="RequiredDate" id="RequiredDateId" class="form-control readonly-color" autocomplete="off" readonly="true"/>
				<label class="input-group-addon btn" for="RequiredDateId"> <span class="fa fa-calendar"></span> </label>
			</div>
			</div>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Old Site Wise Indent Number :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
			<form:input path="old_SiteWiseIndent_Number" id="old_SiteWiseIndent_Number"  readonly="true" class="form-control"/>
			</div>
		   </div> 
			</div>	
			<div class="col-md-6 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label class="control-label col-md-6 col-sm-6 col-xs-12">Indent Name :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
			<form:input type="text" path="indentName" id="indentNameId"  readonly="false" class="form-control"/>
			</div>
		   </div> 
			</div>	
		    <div class="col-md-6 col-sm-6 col-xs-12">
		     <div class="form-group">
			<label style="visibility:hidden" class="control-label col-md-6 col-sm-6 col-xs-12">Approver Emp Id :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
				<form:input type="hidden"  path="approverEmpId" id="approverEmpIdId"  readonly="true" class="form-control"/>
			</div>
			</div>
			 </div> 
			 <div class="col-md-6 col-sm-6 col-xs-12">
			 <div class="form-group">
			<label style="visibility:hidden" class="control-label col-md-6 col-sm-6 col-xs-12">Old Indent Number :</label>
			<div class="col-md-6 col-sm-6 col-xs-12" >
			<form:input type="hidden" path="old_Indent_Number" id="old_Indent_Number"  readonly="true" class="form-control"/>
			</div>
		   </div> 
			</div>	
			
			<input type="hidden"  name="reqSiteName"  value="${getIndentCreation.siteName}" />
			<input type="hidden"  name="reqSiteId"  value="${getIndentCreation.siteId}" />
			<input type="hidden" id="siteId" name="siteId"  value="${getIndentCreation.siteId}" />
			<input type="hidden" id="allSiteIds" name="allSiteIds"  value="${Allsites}" />
		  </c:forEach>
</div>

          <!-- *************************************************Update PO data********************************************************************************************* -->
	<div class="container">
	 <div class="row">
</div>
</div>
	      <div class="clearfix"></div>
	       <div class="table-responsive ">	   
				<table id="doInventoryTableId" class="table table-new tbl-width-medium-average" style=""> <!-- pro-table -->
					<thead>
					 <tr>
						<th class="width-indent-th"><%= serialNumber %></th>
	    				<th><%= product %></th>
	    				<th><%= subProduct %></th>
	    				<th style="width:214px;"><%= childProduct %></th>    				
	    				<th><%= measurement %></th>
	    				<th class="w-70"><%= quantity %></th>
	    				<th><%= remarks %></th>
	    				<th><%=Comments %></th>						
						<th>Actions</th>
					 </tr>
					</thead>				
					<tbody>
					   <c:forEach var="GetProductDetails" items="${requestScope['IndentCreationDetailsList']}" step="1" begin="0">	
		        	 	 <tr id="tr-class${GetProductDetails.strSerialNumber}" class="productrowcls">
		        	 	<td>	
		        	  						
					       <div id="snoDivId${GetProductDetails.strSerialNumber}">${GetProductDetails.strSerialNumber}</div> 
							
						</td>
						<td>
							<select id="combobox${GetProductDetails.strSerialNumber}" readonly="true" name="Product${GetProductDetails.strSerialNumber}" class="form-control  btn-visibilty${GetProductDetails.strSerialNumber} btn-loop1 value="${GetProductDetails.product1}">
							<option value="${GetProductDetails.productId1}$${GetProductDetails.product1}">${GetProductDetails.product1}</option>	
								<%-- <option value="">${GetProductDetails.product}</option> --%>
						    		<%			
						    		
						    		Map<String, String> prod = (Map<String, String>)request.getAttribute("productsMap");
						    			for(Map.Entry<String, String> prods : prod.entrySet()) {
										String prodIdAndName = prods.getKey()+"$"+prods.getValue();
									%>
										<option value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
						    		<% } %>
							</select>
							
							<input type="hidden" name="ProductId${GetProductDetails.strSerialNumber}" id="ProductId${GetProductDetails.strSerialNumber}" value="${GetProductDetails.productId1}$${GetProductDetails.product1}">
						</td>		
						<td>
							<form:select path="" id="comboboxsubProd${GetProductDetails.strSerialNumber}"  readonly="true" name="SubProduct${GetProductDetails.strSerialNumber}" class="form-control  btn-visibilty${GetProductDetails.strSerialNumber} btn-loop${GetProductDetails.strSerialNumber}">
								<option value="${GetProductDetails.subProductId1}$${GetProductDetails.subProduct1}">${GetProductDetails.subProduct1}</option>
							</form:select>
							<input type="hidden" name="SubProductId${GetProductDetails.strSerialNumber}" id="SubProductId${GetProductDetails.strSerialNumber}" value="${GetProductDetails.subProductId1}$${GetProductDetails.subProduct1}">
						</td>
						<td>
							<form:select path="" id="comboboxsubSubProd${GetProductDetails.strSerialNumber}" readonly="true" name="ChildProduct${GetProductDetails.strSerialNumber}" class="form-control  btn-visibilty${GetProductDetails.strSerialNumber} btn-loop${GetProductDetails.strSerialNumber}">
								<option value="${GetProductDetails.childProductId1}$${GetProductDetails.childProduct1}">${GetProductDetails.childProduct1}</option>
							</form:select>
							<input type="hidden" name="ChildProductId${GetProductDetails.strSerialNumber}" id="ChildProductId${GetProductDetails.strSerialNumber}" value="${GetProductDetails.childProductId1}$${GetProductDetails.childProduct1}">
						</td>
						
						
						<td>
							<form:select path="" id="UnitsOfMeasurementId${GetProductDetails.strSerialNumber}" readonly="true" name="UnitsOfMeasurement${GetProductDetails.strSerialNumber}"  class="form-control  btn-visibilty${GetProductDetails.strSerialNumber} btn-loop${GetProductDetails.strSerialNumber}"  value="${GetProductDetails.unitsOfMeasurement1}" onchange="return validateProductAvailability(this);">
								<option value="${GetProductDetails.unitsOfMeasurementId1}$${GetProductDetails.unitsOfMeasurement1}">${GetProductDetails.unitsOfMeasurement1}</option>
							</form:select>
							<input type="hidden" name="UnitsOfMeasurementIdval${GetProductDetails.strSerialNumber}" id="UnitsOfMeasurementIdval${GetProductDetails.strSerialNumber}"  value="${GetProductDetails.unitsOfMeasurementId1}$${GetProductDetails.unitsOfMeasurement1}">
						</td>
						<td class="w-70" data-toggle="tooltip" title="${GetProductDetails.requiredQuantity1}">
							<input  id="QuantityId${GetProductDetails.strSerialNumber}" name="RequiredQuantity${GetProductDetails.strSerialNumber}" readonly="true" value="${GetProductDetails.requiredQuantity1}" class="form-control input-visibilty${GetProductDetails.strSerialNumber}" onfocusout="validateBOQQuantity(${GetProductDetails.strSerialNumber}, 'Old')" style="width:100px;float:left;margin-right:15px;"/>
							<input name="ProductAvailability${GetProductDetails.strSerialNumber}" id="ProductAvailabilityId${GetProductDetails.strSerialNumber}" readonly="true" value="${GetProductDetails.productAvailability1}" class="form-control input-visibilty${GetProductDetails.strSerialNumber}"  style="width:100px;"/>
							<input type="hidden" id="Quantity${GetProductDetails.strSerialNumber}" name="Quantity${GetProductDetails.strSerialNumber}" readonly="true" value="${GetProductDetails.requiredQuantity1}" class="form-control input-visibilty${GetProductDetails.strSerialNumber}" style="width:100px;float:left;margin-right:15px;"/>
						<input  type="hidden" id="oldQuantity${GetProductDetails.strSerialNumber}" name="oldQuantity${GetProductDetails.strSerialNumber}" readonly="true" value="${GetProductDetails.requiredQuantity1}" class="form-control cls-inputFirst input-visibilty${GetProductDetails.strSerialNumber}"/>
						</td>				
						<td>
							<a><input id="RemarksId${GetProductDetails.strSerialNumber}" name="Remarks${GetProductDetails.strSerialNumber}" href="#" data-toggle="tooltip" title="<c:out value='${GetProductDetails.remarks1}' />"  readonly="true" value="<c:out value='${GetProductDetails.remarks1}' />" class="form-control remarkstooltip input-visibilty${GetProductDetails.strSerialNumber}"/></a>
						</td>
						<td>
							<input id="CommentsId${GetProductDetails.strSerialNumber}"  name="Comments${GetProductDetails.strSerialNumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${GetProductDetails.strSerialNumber}"/>
							<input type="hidden" name="IsComments" value="" id="hiddenCommentsId">
							<input type="hidden" id="groupId${GetProductDetails.strSerialNumber}"  name="groupId${GetProductDetails.strSerialNumber}" readonly="true"  class="form-control"  value="${GetProductDetails.groupId1}" />		
						</td>
						<td>
						 	<button type="button" name="addremoveItemBtn${GetProductDetails.strSerialNumber}" id="addremoveItemBtnId${GetProductDetails.strSerialNumber}" class="btnaction" onclick="removeRow(${GetProductDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
						    <button type="button" name="editItemBtn${GetProductDetails.strSerialNumber}" value="Edit Item" id="editItem${GetProductDetails.strSerialNumber}" class="btnaction" onclick="editInvoiceRow('${GetProductDetails.strSerialNumber}')" ><i class="fa fa-pencil"></i></button> 
				 		    <button type="button" name="addNewItemBtn${GetProductDetails.strSerialNumber}" id="addNewItemBtnId${GetProductDetails.strSerialNumber}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button> 
					
						</td>
						<td style="display:none">					
							<input  type = "hidden" id="actionValueId${GetProductDetails.strSerialNumber}"  name="actionValue${GetProductDetails.strSerialNumber}"  class="form-control  autocomplete="off" readonly="true" value="S"/>
						</td> 
					</tr>		
			 		</c:forEach> 
				</tbody>
				<input type="hidden" id="numberOfRows" name="numberOfRows" value="${requestScope['IndentCreationDetailsList'].size()}"/>
				<input type="hidden" id="materialRows" name="materialRows" value="${requestScope['IndentCreationDetailsList'].size()}"/> 
	      </table>	
		</div>	
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="numbeOfChargesRowsToBeProcessed" value="" id="countOfChargesRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveChargesBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
			
			<div class="col-md-6 col-xs-12 no-padding-left no-padding-right Mrgtop20">
			 <label class="control-label col-md-2 col-xs-12 no-padding-left" >Note: </label>
			<div class="col-md-8 col-xs-12 no-padding-left no-padding-right" style="margin-bottom: 10px;" >
				<form:textarea path="" href="#" data-toggle="tooltip" title="${IndentLevelCommentsList}"   id="NoteId" name="Purpose" class="form-control resize-vertical" autocomplete="off" placeholder="${IndentLevelCommentsList}"></form:textarea>
			</div>
			</div>
				
			<div class="col-md-12 text-center center-block pt-10">
                           <button type="button" class="btn btn-warning form-submit" data-dismiss="modal" onclick="save()">Submit</button> 
			</div>
				
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
						
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
        <script src="js/IndentRejectedDetailsShow.js" type="text/javascript"></script>
        <script src="js/sidebar-resp.js"></script>
		<script>
		//For combobox & readonly
		var tablelength=$("#doInventoryTableId > tbody > tr").length;
		for(var i=1;i<=tablelength;i++){
			$("#combobox"+i).combobox();    
		    $( "#comboboxsubProd"+i).combobox();
			$("#comboboxsubSubProd"+i).combobox(); 
			$('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide');			
			$('#Product'+i).attr('readonly','true');
			$('#SubProduct'+i).attr('readonly','true');
			$('#ChildProduct'+i).attr('readonly','true');
			$('#Product'+i).addClass('form-control');
			$('#SubProduct'+i).addClass('form-control'); 
			$('#ChildProduct'+i).addClass('form-control');
			$('#ChildProduct'+i).addClass('ChildProduct');
		}		
		</script>
		<script>
		$(document).ready(function() {	
			 $(".up_down").click(function(){ 
			
			for(i=1;i<=100;i++){
				debugger;/* Here 100 is statically given. get the value dynamically from database.*/
				//$('.btn-visibilty'+i).prop('readonly', true).css('cursor','not-allowed');
				//$('.bt-taxamout-coursor'+i).prop('readonly', true);
				//$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed'); 
         	 	$('#Product'+i).prop('disabled', true).css('cursor','not-allowed');;
				$('#SubProduct'+i).prop('disabled', true).css('cursor','not-allowed');;
				$('#ChildProduct'+i).prop('disabled', true).css('cursor','not-allowed');; 
				/* $('.btn-loop'+i).closest('td').find('input').attr('disabled', 'disabled'); */
				$('disable-class'+i).closest('td').find('input').attr('disabled', 'disabled');
			 	$('#stQantity'+i).prop('readonly', true);			 	
				$('#UnitsOfMeasurementId'+i).prop('disabled', true);				
			    $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide'); 
			   // $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('form-control'); 
			     //$("#Conveyance"+i).attr("disabled", true).css('cursor','not-allowed');
			    }
			
		});
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
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
		 	
	/* ************* Method for new text feild in model popup ************** */
$(document).ready(function(){
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
alert(" Do you want to remove the Product");

/*	document.getElementById("ractionValueId"+rowId).value = "R";*/

$("#tr-class"+rowId).addClass('strikeout');
$("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editItem"+rowId).attr("disabled", true);
//$("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');



$("#QuantityId"+rowId).prop('readonly', true);
$("#RemarksId"+rowId).prop('readonly', true);
$("#CommentsId"+rowId).prop('readonly', true);
$("#Product"+rowId).prop('disabled', true);
$("#SubProduct"+rowId).prop('disabled', true);
$("#ChildProduct"+rowId).prop('disabled', true);
$("#UnitsOfMeasurementId"+rowId).prop('disabled', true);
$('.btn-visibilty'+rowId).closest('td').find('.custom-combobox-toggle').addClass('hide'); 
/* $('#snoDivId'+rowId).removeAttr('id');	
$('#PriceId'+rowId).removeAttr('id');
$('#TaxAmountId'+rowId).removeAttr('id');
$('#BasicAmountId'+rowId).removeAttr('id');
$('#tax'+rowId).removeAttr('id');
$('#QuantityId'+rowId).removeAttr('id');
$('#AmountAfterTaxId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesId'+rowId).removeAttr('id');
$('#TaxOnOtherOrTransportChargesId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesAfterTaxId'+rowId).removeAttr('id');
$('#TotalAmountId'+rowId).removeAttr('id'); */


$('#actionValueId'+rowId).val("R");




}

function removeTransRow(rowId){debugger;
alert(" Do you want to remove the Transaction Row");

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
	
/* ***********************************************************************terms and conditions end*************************************************************** */
	</script>
	
<!-- code for combobox written by thirupathi -->    
	
	<!-- <script>
		$(document).ready(function(){
			debugger;
			var tablelength=$("#doInventoryTableId > tbody > tr").length;
			
			for(var i=0;i<tablelength; i++){
				 $(function() {
					 debugger;
		    	        $("#combobox"+i).combobox();
					});
				  $(function() {
		    	        $("#comboboxsubProd"+i).combobox();
					});
				 $(function() {
		    	        $("#comboboxsubSubProd"+i).combobox();
					}); 
			}
			
		})
	 
	
 </script> -->
	
<!-- code for combobox--> 
<script>
	 
		  $("#RequiredDateId").datepicker({
			  dateFormat: 'dd-M-y',
			  minDate:0,
			  changeMonth: true,
		      changeYear: true
		  
		  });
	 
		  $("#ScheduleDateId").datepicker({
			  dateFormat: 'dd-M-y',
			  minDate:0,
			  changeMonth: true,
		      changeYear: true
		  
		  });
	
</script>


</body>
</html>
