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
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
 
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>
 

<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>

</head>

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
<script>
	if (typeof (Storage) !== "undefined"){
		var i = parseInt(sessionStorage.getItem("${UserId}receiveTaxInvoices"));
		debugger;
		//alert(i);
		var isApprovePage="${isApprovePage}";
		if (i == 2) {
			sessionStorage.setItem("${UserId}receiveTaxInvoices", 1);
			if(isApprovePage=="true"){
				window.location.assign("receiveTaxInvoices.spring");
			}
		}else{
			sessionStorage.setItem("${UserId}receiveTaxInvoices", 1);
		}
	} else {
		alert("Sorry, your browser does not support Web Storage...");
	}
</script>
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
				<li class="breadcrumb-item active">Receive Tax Invoices</li>
			</ol>
		</div>
		<div class="overlay_ims"></div>
		 <div class="loader-ims" id="loaderId"> 
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	</div> 
<%-- <c:if test="${showTable eq 'false' }">	 --%>
			<%-- <div  class="text-center" >
				<h3>${infoMessage}</h3>
	</div> --%>
<c:if test="${isCommonApproval eq 'true' }">
		<div class="col-md-12">
		 <div class="container viewpaymentpending-container">
			 <font size="4" color="red" face="verdana">${errorMessage}</font>
			 <div class="col-md-12">
			
				 <form class="form-inline" name="myForm" action="${urlForActionTag}"> 
				 <div  class="form-group">
				 <label>Site :</label>
				 <select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control indentavailselect">	</select>
			 </div>
			  <input type="submit" class="btn btn-warning btn-sm btn-inline" name="submit" value="Submit" onclick="return validate()">
		</form>
		</div>
		</div>
  		<div class="clearfix"></div>
 
	</div>
<%-- 	</c:if> --%>
</c:if>
		
	<%-- <c:if test="${showTable eq 'true' }"> --%>
	 <form:form id="submitInvoicesFormId" class="form-horizontal" modelAttribute="InvoiceDetailsBean">
		<input type="hidden" name="site_id" id="site_id" value="${SiteId}">
		<input type="hidden" name="approverEmpId" id="approverEmpId" value="${InvoiceDetailsBean.approverEmpId}">
			<div class="table">
				<table id="submitTaxInvoices" class="table table-striped  st-table stacktable large-only dataTable no-footer" style="border-collapse: collapse !important;">
					<thead style="border-collapse: collapse;">
						<tr>
						<c:if test="${isApprovePage ne 'false' }">
							<th><input type="checkbox" name="checkAll" id="checkAll"  >  </th>
						</c:if>
							<th>Vendor Name</th>
							<th>Project Name</th>
							<th>Invoice Number</th>
							<th>Invoice Amount</th>
							<th>Invoice Date</th>
							<th>Receive Date</th>
							<th>Remarks</th>
						<c:if test="${isApprovePage eq 'false' }">
							<th>Pending Employee</th>
						</c:if>
						</tr>
					</thead>
					<tbody>
					<c:set value="0" var="indexNumber"></c:set>
					<c:forEach var="taxInvoiceDetails" items="${InvoiceDetails }"> 
					<c:set value="${indexNumber+1}" var="indexNumber"></c:set>
						<tr class="submitInvoiceRows" id="submitInvoiceRowsId${indexNumber}">
						<c:if test="${isApprovePage ne 'false' }">
							<td><input type="checkbox" name="approveReject${indexNumber}" id="approveReject${indexNumber}" class="taxInvoiceCheckBox" value="" ></td>
						</c:if>
							<td title="${taxInvoiceDetails.VENDOR_NAME }">
								${taxInvoiceDetails.VENDOR_NAME }
								<input type="hidden" name="vendorName${indexNumber}" id="VendorNameId${indexNumber}" value="${taxInvoiceDetails.VENDOR_NAME }" class="form-control text-center" onkeyup="loadVendorData(this.id,this.name,${indexNumber})" placeholder="vendor name" autocomplete="off"/>
								<input type="hidden" name="vendorId${indexNumber}" id="vendorId${indexNumber}" value="${taxInvoiceDetails.VENDOR_ID}">
								<input type="hidden" name="isValidInvoiceToSubmit${indexNumber}" id="isValidInvoiceToSubmit${indexNumber}" value="true">
								<input type="hidden" name="recordsToproceed" id="recordsToproceed${indexNumber}" value="${indexNumber}">
								<input type="hidden" name="accTaxInvoiceDetailsID${indexNumber}" id="accTaxInvoiceDetailsID${indexNumber}" value="${taxInvoiceDetails.ACC_TAXINVOICE_DETAILS_ID}">
							</td>
							<td title="${taxInvoiceDetails.SITE_NAME}">${taxInvoiceDetails.SITE_NAME}</td>
							<td title="${taxInvoiceDetails.INVOICE_NO }">
							  	<a target="_blank" href="getInvoiceDetails.spring?invoiceNumber=${taxInvoiceDetails.INVOICE_NO}&vendorName=${taxInvoiceDetails.VENDOR_NAME}&vendorId=${taxInvoiceDetails.VENDOR_ID}&IndentEntryId=${taxInvoiceDetails.INDENT_ENTRY_ID}&SiteId=${taxInvoiceDetails.SITE_ID}&invoiceDate=${taxInvoiceDetails.INVOICE_DATE }&indentType=${taxInvoiceDetails.INDENT_TYPE}" class="anchor-class">${taxInvoiceDetails.INVOICE_NO}</a>
								<input type="hidden"  name="invoiceNumber${indexNumber}" id="invoiceNumber${indexNumber}"  value='${taxInvoiceDetails.INVOICE_NO}@@${taxInvoiceDetails.INDENT_ENTRY_ID}' >	
								<input type="hidden" name="indentEntryId${indexNumber}" id="indentEntryId${indexNumber}" value="${taxInvoiceDetails.INDENT_ENTRY_ID}">
						 		<input type="hidden" name="poId${indexNumber}"  value="${taxInvoiceDetails.PO_ID }">
								<input type="hidden" name="poDate${indexNumber}" value="${taxInvoiceDetails.PODATE }">
								<input type="hidden" name="receivedOrIssuedDate${indexNumber}" value="${taxInvoiceDetails.RECEIVED_OR_ISSUED_DATE }">
								<input type="hidden" name="siteName${indexNumber}" value="${taxInvoiceDetails.SITE_NAME }">
								
						 	</td>
							<td title="${taxInvoiceDetails.TOTAL_AMOUNT }">${taxInvoiceDetails.TOTAL_AMOUNT }<input type="hidden" name="invoiceAmount${indexNumber}" id="invoiceAmount${indexNumber}" value="${taxInvoiceDetails.TOTAL_AMOUNT }" class="form-control text-center" placeholder="invoice amount" readonly/></td>
							<td title="${taxInvoiceDetails.INVOICE_DATE}">${taxInvoiceDetails.INVOICE_DATE}<input type="hidden" name="invoiceDate${indexNumber}" id="invoiceDate${indexNumber}" value="${taxInvoiceDetails.INVOICE_DATE}" class="form-control text-center" placeholder="invoice date" readonly/></td>
							<td title="${taxInvoiceDetails.CREATION_DATE}">${taxInvoiceDetails.CREATION_DATE}<input type="hidden" name="receiveDate${indexNumber}" id="receiveDate${indexNumber}" value="${taxInvoiceDetails.CREATION_DATE}" class="form-control text-center" placeholder="receive date" readonly/></td>
							<td>
								<c:if test="${isApprovePage eq 'true' }">
									<input type="text" name="remarks${indexNumber}" id="remarks${indexNumber}" placeholder="<c:out value='${taxInvoiceDetails.REMARKS}'/>" title="<c:out value='${taxInvoiceDetails.REMARKS}'/>" class="form-control" placeholder="remarks"/>
								</c:if>
								<c:if test="${isApprovePage eq 'false' }">
								<input type="hidden" name="remarks${indexNumber}" id="remarks${indexNumber}" value="${taxInvoiceDetails.REMARKS}" class="form-control" placeholder="remarks"/>
									${taxInvoiceDetails.REMARKS}
								</c:if>
							</td>
							<c:if test="${isApprovePage eq 'false' }">
								<td>${taxInvoiceDetails.PENDING_EMP_NAME}</td>
							</c:if>
						</tr>
					  </c:forEach>
					</tbody>
				</table>
		    </div>    
		    
		    <%-- <c:forEach var="siteIdWithApprovalEmpId" items="${siteIdWithApprovalEmpId}">
		    
		    </c:forEach> --%>
		    
		</form:form>
	
	  <div class="clearfix"></div>
	  <c:if test="${isApprovePage ne 'false' }">
		  <div  class="text-center" >
				<button type="submit" class="btn btn-warning" id="approveInvoiceId" onclick="return approveTaxInvoice()">Approve</button>
				<button type="submit" class="btn btn-warning" id="rejectInvoiceId" onclick="return rejectTaxInvoice()">Reject</button>
		 </div>
	  </c:if>
	<%-- </c:if>	 --%> 		
	 </div>
	</div>
 </div>
</body>

   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script src="js/SubmitTaxInvoicesJs/receiveTaxInvoicesjs.js"  ></script>
   <script>
 //for multiple select box  for Block
   $(document).ready(function() {
	
		$.ajax({
			  url : "./loadAllSites.spring",
			  type : "GET",
			  dataType : "json",
			  success : function(resp) {
				var siteId=sessionStorage.getItem("siteIdAfterRefresh");
				var data="";	 
				data+="<option value=''></option>";
				$.each(resp,function(index,value){
					if("${strSiteId}"==value.SITE_ID){
						data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
					}else{
						data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
					}
				});  
				 $("#dropdown_SiteId").html(data);		
			  },
			  error:  function(data, status, er){
			
				  }
		 });  
		
		$('#submitTaxInvoices').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
	
		$(".taxInvoiceCheckBox").change(function () {
			$(this).val("");
			$(this).attr("id");
			debugger;
		});
		
		$("#checkAll").change(function () {
	
		   debugger;
		   var value=true;
		   if($("#checkAll").prop("checked") == true){
			   value=true;
		   }else{
			   value=false;
		   }
		   $.each($(".taxInvoiceCheckBox"), function(){         
				var thisVal=$(this).val();
				if(value){
					$(this).val(value);
				}else{
					$(this).val("");
				}
			 	  
				
			 	  $(this).prop("checked", value);
			 
			
		  });
	   });
	   
   });

   $(window).load(function () {
		 $(".overlay_ims").hide();	
		 $(".loader-ims").hide();
	}); 
   
   
	/* //this code for to active the side menu 
	var referrer="receiveTaxInvoices.spring";
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
