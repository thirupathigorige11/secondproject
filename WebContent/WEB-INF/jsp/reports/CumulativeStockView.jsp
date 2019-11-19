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
<link href="js/inventory.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<jsp:include page="../CacheClear.jsp" />  
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
table.dataTable {border-collapse:collapse !important;}
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
						<li class="breadcrumb-item"><a href="getSiteWiseCumulativeStock.spring">Reports</a></li>
						<li class="breadcrumb-item"><a href="getSiteWiseCumulativeStock.spring">Cumulative Stock</a></li>
						<li class="breadcrumb-item active">View Cumulative Stock</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="display: none;z-index:999;">
						<div class="lds-facebook"> 
						<div></div> <div></div><div></div>	<div></div>	<div></div>	<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>
				
				

				
				<% 
				
				String note=request.getAttribute("note")==null?"":request.getAttribute("note").toString();%>
					<%-- 
					<h4><strong></strong><%=note %></h4>
					<br> --%>
					<div class="table-responsive">
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Measurement</th>
								<th>Total Received Quantity</th>
								<th>Total Issued Quantity</th>
								<th>Total Present Quantity</th>
							</tr>
						</thead>
						<tbody>



							<%
							
							
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {

									String strProductId = "";
									String strSubProductId = "";
									String strChildProductId = "";
									String strMesurementId = "";
									
									
									String strProductName = "";
									String strSubProductName = "";
									String strChildproductName = "";
									String strtotalAmount = "";
									double actualQuantity = 0.0;
									double issuedQuantity = 0.0;
									double currentQuantity = 0.0;
									String strMeasurementName = "";
									String fromDate="";
									String toDate="";
									String typeOut="";
									String typeOuto="";
									String value="";

									String strissuedQty = "";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();
										
										strProductId = objProductDetails.getProductId() == null ? "" : objProductDetails.getProductId().toString();
										strSubProductId = objProductDetails.getSub_ProductId() == null ? "" : objProductDetails.getSub_ProductId().toString();
										strChildProductId = objProductDetails.getChild_ProductId() == null ? "" : objProductDetails.getChild_ProductId().toString();
										strMesurementId = objProductDetails.getMeasurementId() == null ? "" : objProductDetails.getMeasurementId().toString();

										strProductName = objProductDetails.getProductName() == null ? "" : objProductDetails.getProductName().toString();
										strSubProductName = objProductDetails.getSub_ProductName() == null ? "" : objProductDetails.getSub_ProductName().toString();
										strChildproductName = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										strMeasurementName=objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										actualQuantity =objProductDetails.getActualQuantity();
										issuedQuantity =objProductDetails.getIssuedQuantity();
										currentQuantity =objProductDetails.getCurrentQuantity();
										
										value=objProductDetails.getOutIssue();
										 String strValue[] = value.split("\\$");					
										 typeOut = strValue[0];
										 typeOuto  = strValue[3];
										 
										 String strSiteId=request.getAttribute("strSiteId").toString();
											fromDate=request.getAttribute("fromDate").toString();
										 	toDate=request.getAttribute("toDate").toString();
										
										
									//	out.println("current over data "+currentQuantity);
									out.println("<input type='hidden' name='strProductId' value='"+strProductId+"' />");
									out.println("<input type='hidden' name='strSubProductId' value='"+strSubProductId+"' />");
									out.println("<input type='hidden' name='strChildProductId' value='"+strChildProductId+"' />");
									out.println("<input type='hidden' name='strMesurementId' value='"+strMesurementId+"' />");
									out.println("<input type='hidden' name='fromDate' value='"+fromDate+"' />");
									out.println("<input type='hidden' name='toDate' value='"+toDate+"' />");
									out.println("<input type='hidden' name='strSiteId' value='"+strSiteId+"' />");
										
										out.println("<tr>");

									//	 if (strSearchType.equals("ADMIN")) {

										//	out.println("<td title = '" + strMouseOverDate + "'>");

									//	} else {
											out.println("<td>");

									//	} 
										out.println(strProductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strSubProductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproductName);
										out.println("</td>");
										
										out.println("<td>");
										out.println(strMeasurementName);
										out.println("</td>");
										
										out.println("<td>");
										out.println("<a href='TotalReceivedQuantity.spring?actualQuantity="+actualQuantity+"&strChildProductId="+strChildProductId+"&strSiteId="+strSiteId+"&fromDate="+fromDate+"&toDate="+toDate+"' class='anchor-class'>"+actualQuantity+"</a>");
										out.println("</td>");

										out.println("<td>");
										//out.println("<a>");
										out.println("<class='toooltip' data-toggle='tootlip' title='"+typeOut+"   "+typeOuto+"'>");
										out.println("<a href='TotalIssuedQuantity.spring?issuedQuantity="+issuedQuantity+"&strChildProductId="+strChildProductId+"&strSiteId="+strSiteId+"&fromDate="+fromDate+"&toDate="+toDate+"' class='anchor-class'>"+issuedQuantity+"</a>");
										//out.println("</a>");
										out.println("</td>");
										
										out.println("<td>");
										out.println(currentQuantity);
										out.println("</td>");

										

										out.println("</tr>");

									}
								}
							%>
							
		</tbody>
					</table>
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
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>

	<script>
		$(document).ready(
				function() { $(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					$('.anchor-class').click(function(){
						$('.loader-sumadhura').show();
						setTimeout(function(){
							$('.loader-sumadhura').hide();	
						}, 400);
					});
				});

		
		
		//$("#tblnotification").stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
