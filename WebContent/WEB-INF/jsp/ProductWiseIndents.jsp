<%@page import="java.util.Iterator"%>

<%@page import="com.sumadhura.bean.ProductDetails"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<jsp:include page="CacheClear.jsp" />  
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
.mybtnstylesbtn{margin:2px;}
table.dataTable {border-collapse:collapse !important;}
@media only screen and (max-width:800px){.st-table {
    display: table !important;
    border-color: #5a4d4d;
}}
.popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* The actual popup */
.popup .popuptext {
	visibility: hidden;
	width: 160px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 8px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@
-webkit-keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}

}
@
keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}
</style>

<script>
//======================        user341 START           ========================
function setVendorData(vName) {
	var url = "loadAndSetVendorInfo.spring?vendName="+vName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setVedData;
		request.open("POST", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setVedData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		//alert(resp);
		var vendorId = resp.split("|")[0];
		var vendorAddress = resp.split("|")[1];
		var vendorGsinNo = resp.split("|")[2];
		
		$("#vendorIdId").val(vendorId);
		$("#VendorAddress").val(vendorAddress);
		$("#GSTINNumber").val(vendorGsinNo);			
	}
}
//=========================                user341 END               =================

	function myFunction() {
		/* alert(123); */
		var popup = document.getElementById("myPopup");
		
		alert(popup);
		popup.classList.toggle("show");
	}
	function createPO() {
		
		//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
		
		//var valStatus = appendRow();
		
		/*if(valStatus == false) {
	    	return;
		}*/
		
		var canISubmit = window.confirm("Do you want to Submit?");
		
		if(canISubmit == false) {
			return;
		}
		
		//document.getElementById("saveBtnId").disabled = true;	
		//document.getElementById("countOfRows").value = getAllProdsCount();	
		document.getElementById("ProductWiseIndentsFormId").action = "loadCreatePOPage.spring";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
	}
</script>
</head>
<body class="nav-md">
	<div class="container body">
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
						<li class="breadcrumb-item active">Product wise indents</li>
					</ol>
				</div>
				
<form:form modelAttribute="getIndentsProductWiseModelForm" class="form-horizontal" id="ProductWiseIndentsFormId">
		 <!--  <div class="container"> -->
	<div class="col-md-12 ">
	<div class="border-indent">
		<input type="hidden" name="count" value="1" />
        <div class="control-group" id="fields">
        	<input type="hidden" id="noofvendorsproccessed" name="noofvendorsproccessed" value="1" />
                       <div class="controls" id="profs"> 
                 <div class="input-append row vendor1">
                    <div class="col-md-3">
                      <div class="form-group" id="margintopbtm">
                    <label class="control-label col-md-6">Vendor Name : </label>
                    <div class="col-md-6">
                    <input id="VendorNameId1" name="vendorName1" class="form-control Vendor-Name-Id"  uda-form-no="1"  />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                     <div class="form-group">
                    <label class="control-label col-md-6">GSTIN : </label>
                    <div class="col-md-6" >
                    <input id="GSTINNumber1" name="strGSTINNumber1" class="form-control" autocomplete="off" readonly="true" />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                     <div class="form-group">
                    <label class="control-label col-md-6">Vendor Address : </label>
                    <div class="col-md-6" >  
                    <input id="VendorAddress1" name="vendorAddress1" class="form-control" readonly="true" />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3 text-center center-block">
                     <button id="b1" class="btn btn-success add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus"></span></button>
                     <button id="remove1" class="btn btn-danger remove-me mybtnstyles" type="button" onclick="deleteVendor(1)" ><span class="glyphicon glyphicon-minus"></span></button>
                    </div>
                   </div>
                    <div class="col-sm-2 col-xs-12" >
					<input type="hidden" id="vendorId1" name="vendorId1" class="form-control"  />
					<input type="hidden" name="strCreationDate" id="strCreationDate"   class="form-control" value=""}/>
					</div>
					
                </div>
                <div class="clearfix"></div>
                 
            
            
            </div>
            
           <!--  <div class="field">
            </div> -->
        </div>
	</div>

              <div class="clearfix"></div>
		      <div class="col-md-12">
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>
							<tr>
								<th></th>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Measurement</th>
								<th>Quantity</th>
								<th>Site</th>
								<th>Site Wise IndentNo</th>
								<th>Indent Name</th>
							</tr>
						</thead>
						<tbody>


							</tr>


							<%
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {

									String strProductId = "";
									String strProductName = "";
									String strSubProductName = "";
									String strSubProductId = "";
									String strChildproductName = "";
									String strChildproductId = "";
									String strtotalAmount = "";
									String strQuantity = "";
									String strMesurmentId = "";
									String strMesurmentName = "";
									String strSiteId = "";
									String siteName = "";
									String strSerialNumber = "";
									String strMouseOverDate = "";
                                    String strIndentNo = "";
                                    String siteWiseIndentNo = "";
									String strissuedQty = "";
									String indentCreationDetailsId = "";
									String IndentName="";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();
										strProductId = objProductDetails.getProductId() == null ? "" : objProductDetails.getProductId().toString();
										strSubProductId = objProductDetails.getSub_ProductId() == null ? "" : objProductDetails.getSub_ProductId().toString();
										strChildproductId = objProductDetails.getChild_ProductId() == null ? "" : objProductDetails.getChild_ProductId().toString();
										strMesurmentId = objProductDetails.getMeasurementId() == null ? "" : objProductDetails.getMeasurementId().toString();
										strProductName = objProductDetails.getProductName() == null ? "" : objProductDetails.getProductName().toString();
										strSubProductName = objProductDetails.getSub_ProductName() == null ? "" : objProductDetails.getSub_ProductName().toString();
										strChildproductName = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										strQuantity = objProductDetails.getQuantity() == null ? "" : objProductDetails.getQuantity().toString();
										strMesurmentId = objProductDetails.getMeasurementId() == null ? "" : objProductDetails.getMeasurementId().toString();
										strMesurmentName = objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										siteName = objProductDetails.getSiteName() == null ? "" : objProductDetails.getSiteName().toString();										
										strSerialNumber = objProductDetails.getStrSerialNumber() == null ? "" : objProductDetails.getStrSerialNumber().toString();	
										strMouseOverDate = objProductDetails.getStrOtherSiteQtyDtls() == null ? "" : objProductDetails.getStrOtherSiteQtyDtls().toString();
										strIndentNo = objProductDetails.getStrIndentId() == null ? "" : objProductDetails.getStrIndentId().toString();
										siteWiseIndentNo = objProductDetails.getSiteWiseIndentNo() == null ? "" : objProductDetails.getSiteWiseIndentNo().toString();
										strSiteId=objProductDetails.getSite_Id() == null ? "" : objProductDetails.getSite_Id().toString();
										indentCreationDetailsId=objProductDetails.getIndentCreationDetailsId() == null ? "" : objProductDetails.getIndentCreationDetailsId().toString();
										IndentName=objProductDetails.getIndentName() == null ? "-" : objProductDetails.getIndentName().toString();
										//out.println(" mouse over data "+strMouseOverDate);
										System.out.println("indent number is in jsp "+strIndentNo);
										out.println("<div style=''>");
										
										out.println("<input type='hidden' name='productId"+strSerialNumber+"' value='"+strProductId+"' />");
										out.println("<input type='hidden' name='subProductId"+strSerialNumber+"' value='"+strSubProductId+"' />");
										out.println("<input type='hidden' name='childProductId"+strSerialNumber+"' value='"+strChildproductId+"' />");
										out.println("<input type='hidden' name='unitsOfMeasurementId"+strSerialNumber+"' value='"+strMesurmentId+"' />");
										out.println("<input type='hidden' name='productName"+strSerialNumber+"' value='"+strProductName+"' />");
										out.println("<input type='hidden' name='subProductName"+strSerialNumber+"' value='"+strSubProductName+"' />");
										out.println("<input type='hidden' name='childProductName"+strSerialNumber+"' value='"+strChildproductName+"' />");
										out.println("<input type='hidden' name='unitsOfMeasurementName"+strSerialNumber+"' value='"+strMesurmentName+"' />");
										/* out.println("<input type='hidden' name='requiredQuantity"+strSerialNumber+"' value='"+strQuantity+"' />"); */
										out.println("<input type='hidden' name='indentNo"+strSerialNumber+"' value='"+strIndentNo+"' />");
										out.println("<input type='hidden' name='strSiteId"+strSerialNumber+"' value='"+strSiteId+"' />");
										out.println("<input type=\"hidden\" name=\"indentCreationDetailsId"+strSerialNumber+"\" value=\""+indentCreationDetailsId+"\" />");
										out.println("<input type=\"hidden\" name=\"indentName"+strSerialNumber+"\" value=\""+IndentName+"\" />");
										out.println("</div>");
										out.println("<tr>");
										out.println("<td><input type='checkbox' name='checkboxname"+strSerialNumber+"' value='checked'></input></td>");
										if (strSearchType.equals("ADMIN")) {

											out.println("<td title = '" + strMouseOverDate + "'>");

										} else {
											out.println("<td>");

										}
										out.println(strProductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strSubProductName);
										out.println("</td>");

										out.println("<td>");
										//out.println(strChildproductName);
										out.println("<input type=\"text\" class=\"form-control\" name=\"childProductPuchaseDeptDisc"+strSerialNumber+"\" value=\""+strChildproductName+"\" />");
										out.println("</td>");

										out.println("<td>");
										out.println(strMesurmentName);
										out.println("</td>");
										
										out.println("<td>");
										 out.println("<input type='text' class=\"form-control\" name='requiredQuantity"+strSerialNumber+"' value='"+strQuantity+"' />");
										out.println("</td>");

										out.println("<td>");
										out.println(siteName);
										out.println("</td>");
										
										out.println("<td>");
										out.println(siteWiseIndentNo);
										out.println("</td>");
										out.println("<td>");
										out.println(IndentName);
										out.println("</td>");
										out.println("</tr>");

									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		
					</table>
					
				</div>
				</div>
				<div>
				<% String hiddenFieldHtml1="<input type=\"hidden\" name=\"numberOfRows\" value=\""+totalProductList.size()+"\" />";
					out.println(hiddenFieldHtml1); %>
				</div>
	
				
				<div class="col-md-12 text-center center-block" style="margin-bottom: 20px;">
					<input type="button" onclick="sendEnqueryButton()" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" >					
				</div> 
</form:form>
			</div>
		</div>
	</div>
	
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/ProductWiseIndents.js"></script>
    <script src="js/sidebar-resp.js" type="text/javascript"></script>

	<script>
	$(document).ready(function() {	
		$(".up_down").click(function(){ 
			$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
			$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
		}); 
		
	});
					
					$(document).ready(function(){
					    var next = 2;
					    $(document).on('click','.add-more',function(e){
					    	/* $(this).closest('button').addClass('hide'); */
					        e.preventDefault();
					        var addto = "#VendorNameId" + next;
					        var addto2 = "#GSTINNumber" + next;
					        var addto3 = "#VendorAddress" + next;
					        var addRemove = "#VendorNameId" + (next);
					        var addremove2 = "#GSTINNumber" +(next);
					        var addremove3 = "#VendorAddress" + (next);
					        next = next + 1;
		                 
		                
					        var newIn = '<div class="input-append vendor'+next+'"  id="profs'+next+'">'
					        +'<div class="row">'
					        +'<div id="field' + next + '" class="mydiv col-md-3">'
					       
					        +'<div class="form-group">'
					        +'<label class="control-label col-md-6">Vendor Name : </label>'
					        +'<div class="col-md-6">'
					        +'<input autocomplete="off" class="input form-control myinputstyles Vendor-Name-Id" id="VendorNameId' + next + '" name="vendorName' + next + '" type="text" uda-form-no='+next+'>'
					        +'</div>'
					        +'</div>'
					        +'</div>'
					        +'<div id="field' + next + '" class="mydiv col-md-3">'
					        +'<div class="form-group">'
					        +'<label class="control-label col-md-6">GSTIN : </label>'
					        +'<div class="col-md-6">'
					        +'<input autocomplete="off" class="input form-control myinputstyles"  id="GSTINNumber' + next + '" name="strGSTINNumber' + next + '" type="text" readonly>'
					        +'</div>'
					        +'</div>'
					        +'</div>'
					        +'<div id="field' + next + '" class="mydiv col-md-3">'
					        +'<div class="form-group">'
					        +'<label class="control-label col-md-6">Vendor Address : </label>'
					        +'<div class="col-md-6">'
					        +'<input autocomplete="off" class="input form-control myinputstyles" id="VendorAddress' + next + '" name="vendorAddress' + next + '" type="text" readonly>'
					        +'	<input type="hidden" id="vendorId' + next + '" name="vendorId' + next + '" class="form-control"  readonly/> '
					        +'</div>'
					        +'</div>'
					        +'</div>'
					        +'<div class="col-md-3 text-center center-block">'
					        +'<button id="b1" class="btn btn-success add-more mybtnstylesbtn" type="button"><span class="glyphicon glyphicon-plus" ></span></button>'
					        +'<button id="remove" class="btn btn-danger remove-me mybtnstylesbtn" type="button" onclick="deleteVendor('+ next +')"><span class="glyphicon glyphicon-minus"></span></button>';
					        +'</div>'
					        var newInput = $(newIn);
					        $("#noofvendorsproccessed").val(next);
					        /* debugger;
					        alert(next); */
					        var removeBtn = '<button id="remove' + (next - 1) + '" class="btn btn-danger remove-me mybtnstyles" ><span class="glyphicon glyphicon-minus"></span></button>';
					        var removeButton = $(removeBtn);
					       /*  $(addto).after(newInput); */
					        $(".input-append").last().after(newInput);
					        
					        setvendor(next);
					        
					        
					        //$("#field"+next).after(removeButton);
					        $("#VendorNameId" + next).attr('data-source',$(addto).attr('data-source'));
					        $("#GSTINNumber" + next).attr('data-source',$(addto).attr('data-source'));
					        $("#VendorAddress" + next).attr('data-source',$(addto).attr('data-source'));
					        $("#count").val(next);  
					    });
					    
					    setvendor(1);
					    
					});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					$('#tblnotification').stacktable({myClass:'stacktable small-only'});
				
				
			
						
		function deleteVendor(id){
			var canIDelete=window.confirm("Do you want to delete?");
			if(canIDelete==false){
				return false;
			}
		    var rowLengthcount = $(".input-append").length;
             if(rowLengthcount == 1){
             	alert("this vendor can't be deleted");
             	debugger;
             	return false;
             }
             else{
             	 $(".vendor"+id).remove();
             }
		}
		
		
		function sendEnqueryButton(){
			alert('Currently this Service is not available, Please use "Site wise" or "All Indents" options to Send Vendor Enquiry Quotation.');
			return false;
			$(".loader-sumadhura").show();
			document.getElementById("ProductWiseIndentsFormId").action = "sendenquiry2.spring";
			document.getElementById("ProductWiseIndentsFormId").method = "POST";
			document.getElementById("ProductWiseIndentsFormId").submit();
		}
	</script>

</body>
</html>
