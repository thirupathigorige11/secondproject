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
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
	.table-new tbody tr td {border: 1px solid #000 !important;color: #000;border-top: 0px !important;}
	#tblnotification thead, #tblnotification tbody tr{table-layout:fixed;display:table;width:100%;}
	@media only screen and (min-width:320px) and (max-width:768px){.table-new{width:800px !important;}}
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
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">History</a></li>
						<li class="breadcrumb-item active">Total Present Quantity</li>
					</ol>
				</div>
                <div class="">
                   <div class="col-md-offset-9 col-md-3">
                     <input type="text" class="form-control" id="searchId" placeholder="Search..."/>
                   </div>
                </div>
                <div class="clearfix"></div>
				<div class="table-responsive Mrgtop10">
					<table id="tblnotification"	class="table table-striped table-new" cellspacing="0" >
						<thead class="cal-thead-inwards">
							<tr>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Measurement</th>
								<th>Total Present Quantity</th>
							</tr>
						</thead>
						<tbody class="tbl-fixedheader-tbody">
                      
						<tr>


							<%
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {

									String strProduct = "";
									String strSubProduct = "";
									String strChildproduct = "";
									String strtotalAmount = "";
									String stQantity = "";
									String strMesurment = "";
									String strSiteId = "";
									String strMouseOverDate = "";

									String strissuedQty = "";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();

										strProduct = objProductDetails.getProductName() == null ? "" : objProductDetails.getProductName().toString();
										strSubProduct = objProductDetails.getSub_ProductName() == null ? "" : objProductDetails.getSub_ProductName().toString();
										strChildproduct = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										stQantity = objProductDetails.getQuantity() == null ? "" : objProductDetails.getQuantity().toString();
										strMesurment = objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										strMouseOverDate = objProductDetails.getStrOtherSiteQtyDtls() == null ? "" : objProductDetails.getStrOtherSiteQtyDtls().toString();
										
										//out.println(" mouse over data "+strMouseOverDate);
										
										
										out.println("<tr>");

										if (strSearchType.equals("ADMIN")) {

											out.println("<td title = '" + strMouseOverDate + "'>");

										} else {
											out.println("<td>");

										}
										out.println(strProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strSubProduct);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproduct);
										out.println("</td>");
										
										
										out.println("<td>");
										out.println(strMesurment);
										out.println("</td>");

										out.println("<td>");
										out.println(stQantity);
										out.println("</td>");

										

										out.println("</tr>");

									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		</tr>
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

	<script>
		$(document).ready(
				function() {
					$(".up_down").click(function() {
						        $(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
					
					/*search functionality in table*/
					$("#searchId").on("keyup", function() {
					    var value = $(this).val().toLowerCase();
					    $("#tblnotification tbody tr").filter(function() {
					      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
					    });
                    });
					/*search functionality in table*/
					//$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		/* $(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		}); */
		
		//$("#tblnotification").stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
