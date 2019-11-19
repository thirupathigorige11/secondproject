<%@page import="java.util.Iterator"%>

<%@page import="com.sumadhura.bean.ProductDetails"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.DecimalFormat"%>
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
<link href="js/inventory.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

<style>
table.dataTable {border-collapse:collapse !important;}
</style>

<script>
	function myFunction() {
		/* alert(123); */
		var popup = document.getElementById("myPopup");
		
		alert(popup);
		popup.classList.toggle("show");
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
						<li class="breadcrumb-item"><a href="#">Reports</a></li>
						<li class="breadcrumb-item"><a href="#">Cumulative Stock</a></li>
						<li class="breadcrumb-item active">View Cumulative Stock</li>
					</ol>
				</div>
				
<!--  ---------------------------------------------------------------------		-->	
<%              
				String ReceivedQuantity=request.getAttribute("ReceivedQuantity")==null?"":request.getAttribute("ReceivedQuantity").toString();

			if(ReceivedQuantity.equalsIgnoreCase("true")){              
%>



				

				
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr>
								
								<th>Invoice Number/DC Number</th>
								<th>Child Product Name</th>
								<th>Measurement</th>
								<th>Quantity</th>								
								<th>Price Per Unit</th>
								<th>Basic Amount</th>
								<th>Tax Amount</th>
								<th>Other Charges After Tax</th>
								<th>Total Amount</th>
								<th>Received Date</th>
								
							</tr>
						</thead>
						<tbody>
							<%
							
							
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {

									
									
									String invoiceNumber = "";
									
									String strChildproductName = "";
									String pricePerUnit = "";
									String  TotalAmount="";
									double price=0.0;
									double tempQuantity=0.0;
									String  basicAmount="";
									String taxAmount="";
									
									
									String strMeasurementName = "";
									String receivedDate="";
									String Qunatity="";
									String otherChargesAfterTax="";

									String strissuedQty = "";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();
										
									
										invoiceNumber= objProductDetails.getInvoiceNumber() == null ? "" : objProductDetails.getInvoiceNumber().toString();
										strChildproductName = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										strMeasurementName=objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										pricePerUnit=objProductDetails.getAmount() == null ? "0" : objProductDetails.getAmount().toString();
										receivedDate=objProductDetails.getDate() == null ? "" : objProductDetails.getDate().toString();
										Qunatity=objProductDetails.getRecivedQty() == null ? "0" : objProductDetails.getRecivedQty().toString();
										basicAmount=objProductDetails.getBasicAmt() == null ? "0" : objProductDetails.getBasicAmt().toString();
										taxAmount=objProductDetails.getTaxAmount() == null ? "0" : objProductDetails.getTaxAmount().toString();
										otherChargesAfterTax=objProductDetails.getOtherchargesaftertax1() == null ? "0" : objProductDetails.getOtherchargesaftertax1().toString();
										TotalAmount=objProductDetails.getTotalAmount() == null ? "0" : objProductDetails.getTotalAmount().toString();
										
										/* price=Double.parseDouble(pricePerUnit);
										tempQuantity=Double.parseDouble(Qunatity);
										
										
										basicAmount=((price)*(tempQuantity));
										String strBasicAmount = String.format ("%.2f", basicAmount);
										double basicAmt=Double.parseDouble(strBasicAmount);
										
										
										taxAmount=((basicAmt)*(tax/100));
									//	String 	strTaxAmount=String.format ("%.2f", taxAmount);
										TotalAmount=basicAmt+taxAmount;
										String 	strTotalAmount=String.format ("%.2f", TotalAmount);
										 */
								
										out.println("<tr>");

									//	 if (strSearchType.equals("ADMIN")) {

										//	out.println("<td title = '" + strMouseOverDate + "'>");

									//	} else {
											out.println("<td>");

									//	} 
										out.println(invoiceNumber);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strMeasurementName);
										out.println("</td>");
										
										out.println("<td>");
										out.println(Qunatity);
										out.println("</td>");
										
										out.println("<td>");
										out.println(pricePerUnit);
										out.println("</td>");
										
										out.println("<td>");
										out.println(basicAmount);
										out.println("</td>");
										
										out.println("<td>");
										out.println(taxAmount);
										out.println("</td>");
										
										
										out.println("<td>");
										out.println(otherChargesAfterTax);
										out.println("</td>");
										
										out.println("<td>");
										out.println(TotalAmount);
										out.println("</td>");
										out.println("<td>");
										out.println(receivedDate);
										out.println("</td>");
										
										out.println("</tr>");

									}
								}
								

								List<ProductDetails> totalProductList1 = (List<ProductDetails>) request.getAttribute("productList1");

								if (totalProductList1 != null) {

									
									
									String dcNumber = "";
									
									String strChildproductName = "";
									String pricePerUnit = "";
									String  TotalAmount="";
									double price=0.0;
									double tempQuantity=0.0;
									String basicAmount="";
									
									String taxAmount="";
									
									String strMeasurementName = "";
									String receivedDate="";
									String Qunatity="";
									String otherChargesAfterTax="";

									String strissuedQty = "";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList1.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();
										
									
										dcNumber= objProductDetails.getDcNumber() == null ? "" : objProductDetails.getDcNumber().toString();
										strChildproductName = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										strMeasurementName=objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										pricePerUnit=objProductDetails.getAmount() == null ? "0" : objProductDetails.getAmount().toString();
										receivedDate=objProductDetails.getDate() == null ? "" : objProductDetails.getDate().toString();
										Qunatity=objProductDetails.getRecivedQty() == null ? "0" : objProductDetails.getRecivedQty().toString();
										
										basicAmount=objProductDetails.getBasicAmt() == null ? "0" : objProductDetails.getBasicAmt().toString();
										taxAmount=objProductDetails.getTaxAmount() == null ? "0" : objProductDetails.getTaxAmount().toString();
										TotalAmount=objProductDetails.getTotalAmount() == null ? "0" : objProductDetails.getTotalAmount().toString();
										otherChargesAfterTax=objProductDetails.getOtherchargesaftertax1() == null ? "0" : objProductDetails.getOtherchargesaftertax1().toString();
										/* price=Double.parseDouble(pricePerUnit);
										tempQuantity=Double.parseDouble(Qunatity);
										
										
										basicAmount=((price)*(tempQuantity));
										String strBasicAmount = String.format ("%.2f", basicAmount);
										double basicAmt=Double.parseDouble(strBasicAmount);
										
										
										taxAmount=((basicAmt)*(tax/100));
									//	String 	strTaxAmount=String.format ("%.2f", taxAmount);
										TotalAmount=basicAmt+taxAmount; */
									//	String 	strTotalAmount=String.format ("%.2f", TotalAmount);
								
										out.println("<tr>");

									//	 if (strSearchType.equals("ADMIN")) {

										//	out.println("<td title = '" + strMouseOverDate + "'>");

									//	} else {
											out.println("<td>");

									//	} 
										out.println(dcNumber);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strMeasurementName);
										out.println("</td>");
										
										out.println("<td>");
										out.println(Qunatity);
										out.println("</td>");
										
							
										out.println("<td>");
										out.println(pricePerUnit);
										out.println("</td>");
										
										out.println("<td>");
										out.println(basicAmount);
										out.println("</td>");
										
										out.println("<td>");
										out.println(taxAmount);
										out.println("</td>");
										
										out.println("<td>");
										out.println(otherChargesAfterTax);
										out.println("</td>");
										
										out.println("<td>");
										out.println(TotalAmount);
										out.println("</td>");
										out.println("<td>");
										out.println(receivedDate);
										out.println("</td>");
										

										
										

										out.println("</tr>");

									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
		
		</tbody>
					</table>
				</div>
				
				<% }    else {  %>
				
				<!-- /////////////////////////////////////////////////////////// -->
				
				
				
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr>
								
								<th>Invoice Number/DC Number</th>
								<th>Child Product Name</th>
								<th>Measurement</th>
								<th>Quantity</th>								
								<th>Price Per Unit</th>
								<th>Basic Amount</th>
								<th>Tax Amount</th>								
								<th>Total Amount</th>
								<th>Issued Date</th>
								
							</tr>
						</thead>
						<tbody>



							<%
							
							
								List<ProductDetails> totalProductList = (List<ProductDetails>) request.getAttribute("productList");

								if (totalProductList != null) {

									
									
									String invoiceNumber = "";
									
									String strChildproductName = "";
									String pricePerUnit = "";
									double price=0.0;
									double tempQuantity=0.0;
									double tax=0.0;
									double TotalAmount=0.0;
									double basicAmount=0.0;
									
									double taxAmount=0.0;
									
									String strMeasurementName = "";
									String receivedDate="";
									String Qunatity="";
									String otherChargesAfterTax="";

									String strissuedQty = "";
									String dcNumber="";
									String strSearchType = request.getAttribute("SEARCHTYPE") == null
											? ""
											: request.getAttribute("SEARCHTYPE").toString();
									ProductDetails objProductDetails = null;

									Iterator itr = totalProductList.iterator();

									while (itr.hasNext()) {
										objProductDetails = (ProductDetails) itr.next();
										
									
										invoiceNumber= objProductDetails.getInvoiceNumber() == null ? "" : objProductDetails.getInvoiceNumber().toString();
										strChildproductName = objProductDetails.getChild_ProductName() == null ? "" : objProductDetails.getChild_ProductName().toString();
										strMeasurementName=objProductDetails.getMeasurementName() == null ? "" : objProductDetails.getMeasurementName().toString();
										pricePerUnit=objProductDetails.getAmount() == null ? "0" : objProductDetails.getAmount().toString();
										receivedDate=objProductDetails.getDate() == null ? "" : objProductDetails.getDate().toString();
										Qunatity=objProductDetails.getRecivedQty() == null ? "0" : objProductDetails.getRecivedQty().toString();
									//	basicAmount=objProductDetails.getBasicAmt() == null ? "0" : objProductDetails.getBasicAmt().toString();
										tax=Double.parseDouble(objProductDetails.getTax() == null ? "0" : objProductDetails.getTax().toString());
									//	TotalAmount=objProductDetails.getTotalAmount() == null ? "0" : objProductDetails.getTotalAmount().toString();
										otherChargesAfterTax=objProductDetails.getOtherchargesaftertax1() == null ? "0" : objProductDetails.getOtherchargesaftertax1().toString();
										dcNumber=objProductDetails.getDcNumber() == null ? "0" : objProductDetails.getDcNumber().toString();
									
										if(invoiceNumber==null || invoiceNumber.equals("")){
											invoiceNumber=dcNumber;
										}
											
										
										
										 price=Double.parseDouble(pricePerUnit);
										tempQuantity=Double.parseDouble(Qunatity);
										
										
										basicAmount=((price)*(tempQuantity));
										String strBasicAmount = String.format ("%.2f", basicAmount);
										double basicAmt=Double.parseDouble(strBasicAmount);
										
										
										taxAmount=((basicAmt)*(tax/100));
									//	String 	strTaxAmount=String.format ("%.2f", taxAmount);
										TotalAmount=basicAmt+taxAmount;
										String 	strTotalAmount=String.format ("%.2f", TotalAmount); 
								
										out.println("<tr>");

									//	 if (strSearchType.equals("ADMIN")) {

										//	out.println("<td title = '" + strMouseOverDate + "'>");

									//	} else {
											out.println("<td>");

									//	} 
										out.println(invoiceNumber);
										out.println("</td>");

										out.println("<td>");
										out.println(strChildproductName);
										out.println("</td>");

										out.println("<td>");
										out.println(strMeasurementName);
										out.println("</td>");
										
										out.println("<td>");
										out.println(Qunatity);
										out.println("</td>");
										
					
										out.println("<td>");
										out.println(pricePerUnit);
										out.println("</td>");
										
										out.println("<td>");
										out.println(basicAmount);
										out.println("</td>");
										
										out.println("<td>");
										out.println(taxAmount);
										out.println("</td>");
										

										
										out.println("<td>");
										out.println(TotalAmount);
										out.println("</td>");

										out.println("<td>");
										out.println(receivedDate);
										out.println("</td>");
										

										out.println("</tr>");

									}
								}
							%>
							<%-- <c:forEach var="ProductCentralView" items="${productList}">
		

		</c:forEach> --%>
	
		</tbody>
					</table>
				</div>
				
				<%  } %>

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
	<script src="js/sidebar-resp.js"></script>

	<script>
		$(document).ready(
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		
		
		//$("#tblnotification").stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
