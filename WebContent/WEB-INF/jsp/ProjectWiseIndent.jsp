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
<link href="js/inventory.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
table.dataTable {border-collapse:collapse !important;}

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
		var popup = document.getElementById("myPopup");
		
		alert(popup);
		popup.classList.toggle("show");
	}
	function createPO() {		
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
								<li class="breadcrumb-item active">Pending Indents</li>
							</ol>
						</div>	
						<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
				        <!-- Loader -->				
				       <center><span><font color=red size=4 face="verdana">${message}</font></span></center>
					   <form:form modelAttribute="getIndentsProductWiseModelForm" id="ProductWiseIndentsFormId" action="sendenquiry.spring" method="POST">
							  <div class="">
							      <div class="table-responsive">
										<table id="tblnotification1" class="table table-new" cellspacing="0">
											<thead>
												<tr> 
												<th>S No</th>
													<th>Created Date</th>
													<th>Site Wise Indent Number</th>
													<th>Indent Name</th>
													<th>Created Employee</th>
													<th>Required Date</th>
													<th>Purchase Dept Request Receive Date</th>
												</tr>
											</thead>
											<tbody>											
												<c:forEach items="${listofCentralIndents}" var="element">  
													<tr>
													<td>${element.strSerialNumber}</td>
													    <td>${element.strCreateDate}</td>					 
														<td><a href="viewPurchaseIndent.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteName=${element.siteName}&siteId=${element.siteId}&creationDate=${element.strCreateDate}&url=getAllIndentCreationDetails.spring" class="anchor-class">${element.siteWiseIndentNo}</a></td>
														<td>${element.indentName}</td>
														<td >${element.indentFrom}</td>
														<td >${element.strRequiredDate}</td>
														<td>${element.purchaseDeptReceivedDate}</td>
											        </tr>
										        </c:forEach>
									       </tbody>
										</table>
									</div>
							    </div>
					    </form:form>
					 </div>
				<!-- /page content -->
			</div>
		</div>
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script>
		$(document).ready($(function() {
					$(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
			     var pageURl=document.URL;
			     var showPrevPaginationNo=pageURl.split("showPrevPaginationNo=")[1];			 
			     if(showPrevPaginationNo == 'true'){
			                  $('#tblnotification1').DataTable({"stateSave": true,"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
			                  return true;
			     }else{
			    	 $('#tblnotification1').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
			     }
				
				
		})
		);
		$(".anchor-class").click(function(ev){
			if(ev.ctrlKey==false && ev.shiftKey==false){
				$(".loader-sumadhura").show();
			}
		});
		var referrer="${url}";
		$SIDEBAR_MENU.find('a').filter(function () {
		var urlArray=this.href.split( '/' );
		for(var i=0;i<urlArray.length;i++){
		if(urlArray[i]==referrer) {
		return this.href;
		}
		}
		}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
	</script>
   
</body>
</html>
