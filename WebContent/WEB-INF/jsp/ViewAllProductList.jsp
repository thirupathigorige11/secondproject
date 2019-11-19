<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@page import="com.sumadhura.bean.VendorDetails"%>
<%@page import="java.util.*"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="CacheClear.jsp" />  
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">

<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>

</head>

<style>
table.dataTable {border-collapse:collapse !important;}
@media only screen and (min-width:320px) and (max-width:992px){
.st-table {display: table !important;}.dataTables_paginate {display: block !important;}.dataTables_info {display: block;}}
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
</style>
<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js"></script>

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
						<li class="breadcrumb-item active">View Product Details</li>
					</ol>
				</div>
	
				<!-- Loader  -->
				<div class="loader-sumadhura" style="z-index: 999;">
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
				<!-- LOader -->
			
<!-- user341 -->
<c:if test="${siteWise}">

<div class="col-md-12">
                    <div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>

							<tr>
							<th>Product Name</th>		
    				
    						<th>Sub Product Name</th>
    						
    						<th>Child Product Name</th>
    						
    						<th>Measurement</th>
    						
    						
    				
							</tr>

						</thead>
						<tbody>
			<%	
			
				List<Map<String,Object>> AllProductList = (List<Map<String,Object>>) request.getAttribute("allProductlist") ;
				VendorDetails objProductDetails = null;
				String productName="";
				String subProductName="";
				String childProductName="";
				String measurement="";
			
				for(Map productList :AllProductList){
				objProductDetails = new VendorDetails();
				
			//	objProductDetails.setProduct_Id((productList.get("Product_Id") == null ? "" : productList.get("Product_Id").toString()));
				productName=(productList.get("Product_Name") == null ? "" : productList.get("Product_Name").toString());
			//	objProductDetails.setSubProduct_Id((productList.get("Sub_Product_Id") == null ? "" : productList.get("Sub_Product_Id").toString()));
				subProductName=(productList.get("Sub_Product_Name") == null ? "" : productList.get("Sub_Product_Name").toString());
			//	objProductDetails.setChildProduct_Id((productList.get("Child_product_id") == null ? "" : productList.get("Child_product_id").toString()));
				childProductName=(productList.get("Child_Product_Name") == null ? "-" : productList.get("Child_Product_Name").toString());
			//	objProductDetails.setMeasurement_Id((productList.get("MeasurmentId") == null ? "-" : productList.get("MeasurmentId").toString()));
				measurement=(productList.get("Measurment_Name") == null ? "-" : productList.get("Measurment_Name").toString());
						
						
					%>	
						
						<tr>
						<% 				out.println("<td>");
										out.println(productName);
										out.println("</td>");
										out.println("<td>");
										out.println(subProductName);
										out.println("</td>");
										out.println("<td>");
										out.println(childProductName);
										out.println("</td>");
										out.println("<td>");
										out.println(measurement);
										out.println("</td>");
						
						%>
						
						</tr>
						
								
						
				<%-- <c:forEach items="${allProductlist}" var="element">   --%>
				<%-- <tr>
				    <td style="color: black;">${element.product_name}</td>
					
					<td>${element.subProduct_Name}</td>
					
					<td style="color: black;">${element.childProduct_Name}</td>
					<td style="color: black;">${element.measurement_Name}</td>
					
				</tr> --%>
				
			<% 	} %>
				<%-- </c:forEach> --%>
				</tbody>
					</table>
					</div>
				</div>
				</c:if>
				<!-- user341 -->

	<!-- ************************************************all the data showing to the user director level******************************************* -->
<c:if test="${all}">
<div class="col-md-12">
                    <div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>

							<tr>
							<th>Product Name</th>		
    				
    						<th>Sub Product Name</th>
    						
    						<th>Child Product Name</th>
    						
    						<th>Measurement</th>
    						
    						<th>Product Type</th>
    				
							</tr>

						</thead>
						<tbody>
			<%	
			
				List<Map<String,Object>> AllProductList = (List<Map<String,Object>>) request.getAttribute("allProductlist") ;
				VendorDetails objProductDetails = null;
				String productName="";
				String subProductName="";
				String childProductName="";
				String measurement="";
				String productType="";
			
				for(Map productList :AllProductList){
				objProductDetails = new VendorDetails();
				
			//	objProductDetails.setProduct_Id((productList.get("Product_Id") == null ? "" : productList.get("Product_Id").toString()));
				productName=(productList.get("Product_Name") == null ? "" : productList.get("Product_Name").toString());
			//	objProductDetails.setSubProduct_Id((productList.get("Sub_Product_Id") == null ? "" : productList.get("Sub_Product_Id").toString()));
				subProductName=(productList.get("Sub_Product_Name") == null ? "" : productList.get("Sub_Product_Name").toString());
			//	objProductDetails.setChildProduct_Id((productList.get("Child_product_id") == null ? "" : productList.get("Child_product_id").toString()));
				childProductName=(productList.get("Child_Product_Name") == null ? "-" : productList.get("Child_Product_Name").toString());
			//	objProductDetails.setMeasurement_Id((productList.get("MeasurmentId") == null ? "-" : productList.get("MeasurmentId").toString()));
				measurement=(productList.get("Measurment_Name") == null ? "-" : productList.get("Measurment_Name").toString());
				productType=(productList.get("PRODUCT_DEPT") == null ? "-" : productList.get("PRODUCT_DEPT").toString());
						
						
					%>	
						
						<tr>
						<% 				out.println("<td>");
										out.println(productName);
										out.println("</td>");
										out.println("<td>");
										out.println(subProductName);
										out.println("</td>");
										out.println("<td>");
										out.println(childProductName);
										out.println("</td>");
										out.println("<td>");
										out.println(measurement);
										out.println("</td>");
										out.println("<td>");
										out.println(productType);
										out.println("</td>");
						
						
						%>
						
						</tr>
						
								
						
				<%-- <c:forEach items="${allProductlist}" var="element">   --%>
				<%-- <tr>
				    <td style="color: black;">${element.product_name}</td>
					
					<td>${element.subProduct_Name}</td>
					
					<td style="color: black;">${element.childProduct_Name}</td>
					<td style="color: black;">${element.measurement_Name}</td>
					
				</tr> --%>
				
			<% 	} %>
				<%-- </c:forEach> --%>
				</tbody>
					</table>
					</div>
				</div>
				</c:if>

	<!-- *********************************************************all the data showing to user director level end*********************************** -->
</div>

  </body>

					<script src="js/jquery-ui.js"></script>
					<script src="js/custom.js"></script>
					
		
		
					<script>
					$(window).load(function () {debugger;
						  //alert("Window Loaded");
						  $(".loader-sumadhura").hide();
						});
			$(document).ready(
					
			function() {
				
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				
				});
			$('#tblnotification').DataTable();
			
						/* $(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
						}); */
					</script>

</html>
