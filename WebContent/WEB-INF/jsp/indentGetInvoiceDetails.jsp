<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
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
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<jsp:include page="CacheClear.jsp" />  
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style> .alert-msg{color: red;font-weight:bold;font-size:16px;}</style>
</head>
<script>
function validateForm() {
	var invoiceNum=document.getElementById("invoiceNumber").value;
	var vendorName=document.getElementById("vendorName").value;
	var invoiceDate=document.getElementById("invoiceDate").value;
	if(invoiceNum.length==0||invoiceNum==""){
		alert("Enter Invoice Number.");
		return false;
	}
	if(vendorName.length==0||vendorName==""){
		alert("Enter Vendor Name.");
		return false;
	}if(invoiceDate.length==0||invoiceDate==""){
		alert("Enter InvoiceDate Name.");
		return false;
	}
	return true;
}

</script>
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
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Update Invoice Details</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="z-index:99;display:none;">
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
				<div>
				<div class="container border-inwards-box">	
				   <span class="alert-msg"><c:out value="${requestScope['Message']}"></c:out> </span> 
				    <center><font size=5 >${requestScope['Message1']}</font></center>
					<form class="form-inline" name="myForm" action="doGetInvoiceDetails.spring" style="padding:15px;" onsubmit="return validateForm()">
					    <div class="form-group inwardInvoice">					   
					      <label class="control-label" for="email">Invoice ID:</label>
					      <input type="text" class="form-control" name="invoiceNumber" id="invoiceNumber" autocomplete="off">
					    </div>
					      <div class="form-group inwardInvoice">					   
					      <label class="control-label" for="email">Vendor Name:</label>
					        <input type="text" class="form-control" name="vendorName" id="vendorName" autocomplete="off">
					    </div>
<div class="form-group inwardInvoice">
      <label class="control-label" for="email">Invoice Date:</label>
      <div class="input-group">
        <input type="text" class="form-control readonly-color" name="invoiceDate" id="invoiceDate" readonly autocomplete="off">
        <label class="input-group-addon btn datepicker-paymentreq-fromdate input-group-addon-border" style="border:none!important;" id="datepickerIcon" for="invoiceDate">
					<span class="fa fa-calendar"></span>
				</label>
	 </div>
    </div>
					        <button type="submit" class="btn btn-sm btn-warning" onclick="return validate()" value="submit">Submit</button>
					   </form>
				  </div>
				</div>
           </div>
        </div>
    </div>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/jquery-ui.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script src="js/custom.js"></script>

	<script>
			$(document).ready(function() {
				$("#vendorName").keydown(function () {
					  var str=$(this).val();
					 // alert(str);
							$.ajax({
							  url : "./loadVendorNames.spring",
							  type : "GET",
							  data:{
								  vendorName:str
							  },
						      dataType : "json",
							  success : function(resp) {
								  debugger;
								  var availableProducts   = [];
								  $.each(resp,function(index,value){
										availableProducts.push(value);
									});
									$("#vendorName").autocomplete({
							               source: availableProducts
							         });	
							  },
							  error:  function(data, status, er){
								//  alert(data+"_"+status+"_"+er);
								  }
							  }); 
						});
				    $(".up_down").click( function() {
					    $(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					    $(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				    });
		
			  function validateForm() {
				var invoiceNum=document.getElementById("invoiceNumber").value;
				var vendorName=document.getElementById("vendorName").value;
				var invoiceDate=$("#invoiceDate").val();
				if(invoiceNum.length==0||invoiceNum==""){
					alert("Enter Invoice Number.");
					return false;
				}
				if(vendorName.length==0||vendorName==""){
					alert("Enter Vendor Name.");
					return false;
				}
				if(invoiceDate.length==0||invoiceDate==""){
					alert("Enter Invoice Date.");
					return false;
				}
				$('.loader-sumadhura').show();
				return true;
			}
			
			$(function() {
							$("#invoiceDate").datepicker({
								dateFormat: 'dd-M-y',
								changeMonth: true,
								changeYear: true
							});
						}); 
	</script>
</body>
</html>
