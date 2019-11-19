<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css"/>
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
 table.dataTable {border-collapse:collapse !important;}
 table.table-bordered.dataTable th:last-child{border-right-width:1px;}
 table.table-bordered.dataTable th{border-left-width:1px;}
 .form-control{height:34px;}
</style>
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
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Cancel PO Show</li>
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
               <c:if test="${isMarketing}">
               <div class="col-md-12">
			   <div class="table-responsive"> 
					<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0">
						<thead>				
							<tr>
								<th>PO Date</th>				    				
	    						<th>PO Number</th>				    						
	    						<th>Vendor Name</th>				    						
	    						<th>Site Name</th>
	    						<th>Cancel PO Date</th>				    						
	    						
							</tr>
					    </thead>
						<tbody>
							<c:forEach items="${listOfPOs}" var="element">  
								<tr>
								    <td>${element.get("PO_DATE")}</td>									
									<td><a href='PrintCancelPOData.spring?poNumber=${element.get("PO_NUMBER")}&siteId=${element.get("SITE_ID")}&poEntryId=${element.get("PO_ENTRY_ID")}&poType=${element.get("PREPARED_BY")}&siteName=${element.get("SITE_NAME")}&isApprove=false' style="text-decoration: underline;color: blue;" class="poNumber">${element.get("PO_NUMBER")}</a></td>
									<td>${element.get("VENDOR_NAME")}</td>
									<td>${element.get("SITE_NAME")}</td>
									<td>${element.get("ENTRY_DATE")}</td>
																		
								</tr>
						  </c:forEach>
					   </tbody>
					</table>
				</div>
              </div>
              </c:if>
              <c:if test="${isPurchase}">
              <div class="col-md-12">
			   <div class="table-responsive"> 
					<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0">
						<thead>				
							<tr>
								<th>PO Creation Date</th>				    				
	    						<th>PO Number</th>				    						
	    						<th>Vendor Name</th>				    						
	    						<th>Project Name</th>				    						
	    						<th>SiteWise Indent Number</th>
	    						<th>Cancel PO Date</th>
							</tr>
					    </thead>
						<tbody>
							<c:forEach items="${listOfPOs}" var="element">  
								<tr>
								    <td>${element.get("PO_DATE")}</td>									
									<td><a href='PrintCancelPOData.spring?poNumber=${element.get("PO_NUMBER")}&siteName=${element.get("SITE_NAME")}&siteId=${element.get("SITE_ID")}&poEntryId=${element.get("PO_ENTRY_ID")}&poType=${element.get("PREPARED_BY")}&isApprove=false' style="text-decoration: underline;color: blue;" class="poNumber">${element.get("PO_NUMBER")}</a></td>
									<td>${element.get("VENDOR_NAME")}</td>
									<td>${element.get("SITE_NAME")}</td>
									<td>${element.get("SITEWISE_INDENT_NO")}</td>
									<td>${element.get("ENTRY_DATE")}</td>									
								</tr>
						  </c:forEach>
					   </tbody>
					</table>
				  </div>
               </div>
              </c:if>               
            </div>
          </div>
      </div>
    </body>  
     
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
    <script>
		$(document).ready(function(){
			$(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
					});
				   $('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				   $(".poNumber").click(function(){
					   $(".loader-sumadhura").show();
				   });
		});		
	</script>
</html>
