 <!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>

<!-- <title>Approve RA Bill</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
 <!-- for data table check box  -->
 <link type="text/css" href="css/dataTables.checkboxes.css" rel="stylesheet" />
 
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
 

<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<style>
/*css code for print */
th{text-align:center !important;}
tbody{color:#000;font-weight:bold;}
 
/*css code for print */
/* fixed header table css for modal*/
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:70px;
}
 
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }
 
 
/* fixed header table css for modal*/
.modal-body-scroll{
max-height:450px;
overflow-y:auto;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}
.scroll-rabill-tbl>table>thead>tr>th{
border:1px solid #000;
background-color:#ccc;
color:#000;
border-top:1px solid #000 !important;
text-align:center;
}
.scroll-rabill-tbl>table>tbody>tr>td{
border:1px solid #000;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}

.rabill-class-select{
width:130px;
}
.mrg-Top{
margin-top:5px;
}
.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
.inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

}
.media-style{
width:39% !important;

}
.submitstyle{
margin-top: 20px;
width: 23% !important;
}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}
.td-active input{
background-color:#dbecf6;
}
.td-active{
background-color:#dbecf6;
}
.td-active input:focus{

}
.td-active input{
font-weight:bold;
}
.border-none input{
border:none !important;
}
.input-outline input{
outline:none;
}
.table-border-submitTaxInvoices thead tr th, .table-border-submitTaxInvoices tbody tr td{
 border:1px solid #000;
 font-weight: bold;
}
.table-border-submitTaxInvoices{
border:1px solid #000;

}
/* written by thirupathi */
.deduction{
	width: 200px;
	 text-align:center;
	/*  padding-left:8%; */
	 margin-left:30%;
}
.deduction .lst{
  text-align:center;
}
.hidebtn1{
    visibility: hidden;
}
.deletebtn{
	margin-left:2%;
}
.heightthirty{
  height:30px;
}
</style>
</head>
 <body class="nav-md">
	<div class="container body">
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
		<div>
			<ol class="breadcrumb">
				<li class="breadcrumb-item"><a href="#">Home</a></li>
				<li class="breadcrumb-item active">Submit Tax Invoices</li>
			</ol>
		</div>
		 <!-- loader  -->
	<div class="overlay_ims"></div>
		 <div class="loader-ims" id="loaderId"> 
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	</div> 
	<form:form id="submitInvoicesFormId" class="form-horizontal" action="saveTaxInvoices.spring" modelAttribute="InvoiceDetailsBean">
		<input type="hidden" name="site_id" id="site_id" value="${SiteId}">
		<input type="hidden" name="approverEmpId" id="approverEmpId" value="${InvoiceDetailsBean.approverEmpId}">
			<div class="table">
				<table id="submitTaxInvoices" class="table table-striped  st-table stacktable large-only dataTable no-footer" style="border-collapse: collapse !important;">
					<thead>
						<tr>
							<th>Select Invoice<input type="checkbox" name="checkALl" id="checkAll" onclick="checkAllCheckBox()"></th>
							<th class="text-center">Vendor Name</th>
							<th class="text-center">Invoice Number</th>
							<th class="text-center">Invoice Amount</th>
							<th class="text-center">Invoice Date</th>
							<th class="text-center">Remarks</th>
							<!-- <th>Action</th> -->
						</tr>
					</thead>
					<tbody>
					<c:set value="0" var="index"></c:set>
					<c:forEach var="taxInvoiceDetails" items="${InvoiceDetails}">
						<c:set value="${index+1}" var="index"></c:set>
						<tr class="submitInvoiceRows" id="submitInvoiceRowsId${index}">
							<td class="text-center"><input type="checkbox" name="submitTaxInvoiceCheckBox${index}" id="submitTaxInvoiceCheckBox${index}" onclick="checkAttachmentForInvoice(this.id,${index},this.value);" class="taxInvoiceCheckBox" value="" ></td>
							<td class="text-center">
								<input type="hidden" name="vendorName${index}" id="VendorNameId${index}" value="${taxInvoiceDetails.VENDOR_NAME }" title="${taxInvoiceDetails.VENDOR_NAME }" class="form-control text-center"  placeholder="vendor name" autocomplete="off" readonly="readonly"/>
								<span style="color: red;display: none;" id="errorMsg${index}" >No Attachments Found.</span>
								<input type="hidden" name="vendorId${index}" id="vendorId${index}" value="${taxInvoiceDetails.VENDOR_ID}">
								<input type="hidden" name="VendorAddress${index}" id="VendorAddress${index}" value="${taxInvoiceDetails.ADDRESS }">
								<input type="hidden" name="GSTINNumber${index}" id="GSTINNumber${index}" value="${taxInvoiceDetails.GSIN_NUMBER }">
								<input type="hidden" name="isValidInvoiceToSubmit${index}" id="isValidInvoiceToSubmit${index}" value="">
								<input type="hidden" id="recordsToproceed${index}">
								${taxInvoiceDetails.VENDOR_NAME }
							</td>
							<td class="text-center">
								<input type="hidden" name="invoiceNumber${index}" id="invoiceNumber${index}" style="width: 65%;" class="form-control text-center" value="${taxInvoiceDetails.INVOICE_ID }" title="${taxInvoiceDetails.INVOICE_ID }" readonly="readonly">
								<a href="getInvoiceDetails.spring?invoiceNumber=<c:out value='${taxInvoiceDetails.INVOICE_ID}'></c:out>&vendorName=${taxInvoiceDetails.VENDOR_NAME }&vendorId=${taxInvoiceDetails.VENDOR_ID}&IndentEntryId=${taxInvoiceDetails.INDENT_ENTRY_ID }&SiteId=${taxInvoiceDetails.SITE_ID }&invoiceDate=${taxInvoiceDetails.INVOICE_DATE}&indentType=IN" id="showLink${index}"  target="_blank"  class="anchor-class"  title="click here to see invoice"  style="margin-bottom: 7px;height: 28px;">${taxInvoiceDetails.INVOICE_ID}</a>
								<button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal1" id="showLinkButton1" style="display: none;margin-bottom: 7px;height: 28px;"><i class="fa fa-question-circle  red-tooltip " id="WDDwtailsTable1" ></i></button>
								<input type="hidden" name="indentEntryId${index}" id="indentEntryId${index}" value="${taxInvoiceDetails.INDENT_ENTRY_ID }">
								<input type="hidden" name="poId${index}"  value="${taxInvoiceDetails.PO_ID }">
								<input type="hidden" name="poDate${index}" value="${taxInvoiceDetails.PODATE }">
								<input type="hidden" name="receivedOrIssuedDate${index}" value="${taxInvoiceDetails.RECEIVED_OR_ISSUED_DATE }">
								
						 	</td>
							
							<td class="text-center">${taxInvoiceDetails.TOTAL_AMOUNT}<input type="hidden" name="invoiceAmount${index}" id="invoiceAmount${index}"  value="${taxInvoiceDetails.TOTAL_AMOUNT}"  title="${taxInvoiceDetails.TOTAL_AMOUNT}" class="form-control text-center" placeholder="invoice amount" readonly/></td>
							<td class="text-center">${taxInvoiceDetails.INVOICE_DATE}<input type="hidden" name="invoiceDate${index}" id="invoiceDate${index}" value="${taxInvoiceDetails.INVOICE_DATE}"  title="${taxInvoiceDetails.INVOICE_DATE}"  class="form-control text-center" placeholder="invoice date" readonly/></td>
							<td class="text-center"><input type="text" name="remarks${index}" id="remarks${index}" class="form-control" ></td>
							<!-- <td>
								<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId1" onclick="appendRow(1)" class="btnaction"><i class="fa fa-plus"></i></button>
								<button type="button" style="display:none;" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button>
							</td> -->
						</tr>
						
						
						</c:forEach>
						
					</tbody>
				</table>
				<div  class="text-center" style="margin-top: 30px;">
					<button type="submit" class="btn btn-warning" id="submitInvoicesId" onclick="return submitForm()">Submit</button>
				</div>
				 <div class="clearfix"></div>
					
		    </div>    
		</form:form>
	  <div class="clearfix"></div>
	  
	   
<div id="popupForInvoiceHyperLink">
  <!-- Modal -->
  <div class="modal fade" id="myModal1" role="dialog"><div class="modal-dialog modal-sm">
  <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Invoices</h4>
        </div>
        <div class="modal-body"  id="HyperLinksForInvoice1" style="height:250px;overflow-y: scroll;">
       
        </div>
        <div class="modal-footer">
      </div>
      </div>
      
    </div>
  </div>
</div>	  
	  
	 </div>
	</div>
 </div>
</body>


	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>		



<script src="js/SubmitTaxInvoicesJs/submitTaxInvoices.js"  ></script>

<script>
 //for multiple select box  for Block
   $(document).ready(function() {
	   $("#submitTaxInvoices").DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
		   'select': {
			      'style': 'multi'
			   }});
   });
   $(window).load(function () {
		 $(".overlay_ims").hide();	
		 $(".loader-ims").hide();
	});
   
   
   
	/* //this code for to active the side menu 
	var referrer="submitTaxInvoices.spring";
	$SIDEBAR_MENU.find('a').filter(function () {
           var urlArray=this.href.split( '/' );
           for(var i=0;i<urlArray.length;i++){
        	 if(urlArray[i]==referrer) {
        		 return this.href;
        	 }
           }
    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active'); */
   
    /* Reload the site when reached via browsers back button */
	if(!!window.performance && window.performance.navigation.type == 2)
	{
	    window.location.reload();
	}
    /* Reload the site when reached via browsers back button */
	
   
	   </script>
</html>
