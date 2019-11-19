<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="func"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="../CacheClear.jsp" />
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/misreports.css" rel="stylesheet" type="text/css">

 <link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="css/jquery.dataTablesFixedheader.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>

 
table.dataTable {border-collapse: collapse !important;}

</style>

</head>
<body class="nav-md">
<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal">
	<div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="../TopMenu.jsp" />  
			<input type="hidden" name="siteIds" id="siteIds" value="${siteIds}">
			<input type="hidden" name="fromDate" id="fromDate" value="${fromDate}">
			<input type="hidden" name="toDate" id="toDate" value="${toDate}"> 
			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Reports</a></li>
						<li class="breadcrumb-item active">MIS Local Purchase Details</li>
					</ol>
				</div>
               
               <%
						String isShowGrid = request.getAttribute("showGrid") == null
								? ""
								: request.getAttribute("showGrid").toString();
					//List<ProductDetails> poDetails=(List<ProductDetails>) request.getAttribute("po_Details");
					//int size=poDetails.size();
					//request.setAttribute("po_Details",poDetails);
						if (isShowGrid.equals("true")) {
					%> 
				<div class="col-md-12">
					<div class="container">
						<%-- <label class="success"> <c:out value="${requestScope['succMessage']}"></c:out> </label> --%>
						<form class="form-horizontal">
						 <table id="mistableFixedheader" class="table table-bordered display table-fixed-header" style="width:100%;">
                             <thead>
					            <tr>
					                <th>S.No</th>
					                <th>Project Name</th>
					              	<th>Vendor Name</th>
					                <th>Invoice Number/Dc Number</th>
					                <th>Invoice/Dc Date</th>
					              	<th>Description of Materials</th>
					                <th>GRN Date</th>
					           		 <th>Invoice/Dc Amount</th>
					                
					             
					                
					            </tr>
                            </thead>
                            <tbody>
                            <%
                            List<ProductDetails> poDetails = (List<ProductDetails>) request.getAttribute("LocalPurchaseDetails");
                            int i=1;
                            for (ProductDetails element : poDetails) {
                           	String invoiceOrDc=element.getDc_or_Invoice();
                            String site_Name=element.getSiteName();
                            String site_Id=element.getSite_Id();
                            String vendor_Id=element.getVendorId();
                            String invoiceNumber=element.getInvoiceNumber();
                            String invoiceAmount=element.getInvoiceAmount();
                            String typeOfGrn="MarketPurchase";
                            %>
                        
								<%-- <c:forEach items="${poDetails}" var="element"> --%>
                              <tr><%--  <td><%=i%></td> --%>
                                <%   
                                    out.println("<td>");
										out.println(i);
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getSiteName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getVendorName());
										out.println("</td>");
										
										if(invoiceOrDc.equalsIgnoreCase("INV")){
										out.println("<td>");
										out.println("<a href='getInvoiceDetails.spring?invoiceNumber="+element.getInvoiceNumber()+"&SiteId="+element.getSite_Id()+"&siteName="+element.getSiteName()+"&vendorId="+element.getVendorId()+"&IndentEntryId="+element.getIndentEntryId()+"&invoiceDate="+element.getInvoiceDate()+"' style='text-decoration: underline;color: blue;'>"+element.getInvoiceNumber()+"</a>");
										out.println("</td>");
										}else{
											out.println("<td>");
											out.println("<a href='getDcFormGrnViewDts.spring?invoiceNumber="+element.getInvoiceNumber()+"&SiteId="+element.getSite_Id()+"&siteName="+element.getSiteName()+"&vendorId="+element.getVendorId()+"&dcEntryId="+element.getIndentEntryId()+"&dcDate="+element.getInvoiceDate()+"&type="+typeOfGrn+"' style='text-decoration: underline;color: blue;'>"+"DC_NO"+element.getInvoiceNumber()+"</a>");
											out.println("</td>");
										}
										
										out.println("<td>");
										out.println(element.getReceiveDate());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getMaterialDesc());
										out.println("</td>");
										
										
										
										
										out.println("<td>");
										out.println(element.getGrnDate());
										out.println("</td>");
										
										out.println("<td class=\"valor2\">");
										out.println(invoiceAmount);
										out.println("</td>");
										
										
										
										
										
                                    %>
										
									<%i++; %>
                              </tr>
                            <%--  </c:forEach> --%>
                            <%} %>
                             
                            
                              
                            </tbody>
                            <!-- <tr class="info">
								<td colspan="8" class="text-right subTtl" style="display: none;">SUB
									TOTAL:</td>
								<td class="total text-right subTtl" style="display: none;"></td>
							</tr> -->
							<!-- </tbody> -->
							<tr class="info">
								<td colspan="7" class="text-right subTtl">SUB
									TOTAL:</td>
								<td class="total text-right subTtl"></td>
							</tr>
							
                             <!-- <tfoot>
					            <tr>
					                <th colspan="14" style="text-align:right">Total:</th>
					                <th class="total" id="total"></th>
					                <th></th>
					                <th></th>
					            </tr>
                          </tfoot> -->
            </table> 
						
						</form>
					</div>
				</div>
				<div class="col-md-12 text-right">
				 <div class="col-md-7"></div>
				 <div class="col-md-5">
				 <div class="col-md-6"><span class="totalAmount">Total Amount:</span></div>
				 <div class="col-md-6"><span class="totalAmounts">${Invoice_GrandTotal}</span></div>
				 
				</div>
				
			</div>
		</div>
		<%} %>
	</div>
</form:form>
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
    <!-- <script src="js/jquery.dataTables.min.js"></script> -->
	<!-- <script src="js/dataTables.bootstrap.min.js"></script> -->
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	
<!-- script for data table fixed header-->
<script src="js/jqueryFixedheader-3.3.1.js"></script>
<script src="js/jquery.dataTablesFixedheader.min.js"></script>
 <script>
 /* $(document).ready(function() { 
	    var tableheadfix = $('#mistableFixedheader').DataTable( {"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
	        "scrollY": "350px",
	       	"paging": true
	    });
	    
	} ); */
 /* script for subtotal calucalation */
  
 /*script for subtotal calucalation*/
 
function MISPrint() {debugger;
var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	//document.getElementById("countOfRows").value = getAllProdsCount();
	//document.getElementById("countOfChargesRows").value = getAllChargesCount();
	document.getElementById("ProductWiseIndentsFormId").action = "MISPoReportPage.spring";
	document.getElementById("ProductWiseIndentsFormId").method = "POST";
	document.getElementById("ProductWiseIndentsFormId").submit();
}
/* *************** calculations for filter data***********************	 */
$(document).ready(function () {

/* 		$(window).on( 'load', function () { */
	    var tableheadfix = $('#mistableFixedheader').DataTable( {"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
	        "scrollY": "300px",
	        "scrollCollapse": true,
	        "paging": true
	    });
	   
/* 	} ); */

	debugger;
	$(".info td").show();
	//$(".total").text(0);
	 var val = $('#mistableFixedheader').find('tbody').find('tr');
	 var tAmount = 0;
	 //$(".total").text(0);
    jQuery.each(val,function(index,item){
    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
    });
    //$(".subTtl").hide();	
 	$(document).on("keyup", ".dataTables_wrapper .dataTables_filter input, .paginate_button ",function(){
 		debugger;
 	
 		$(".subTtl").show();
 		$(".text-right").show();
 		 var val = $('#mistableFixedheader').find('tbody').find('tr');
		 var tAmount = 0;
		 //$(".total").text(0);
	    jQuery.each(val,function(index,item){
	    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
	    });
	 
		$(".total").text(tAmount.toFixed(2));
	});
   
});
/* *************** calculations for filter data***********************	 */
/* *************** Paginataion Total********* */
	
			$(document).ready(function () {debugger;
		//$(".total").text(0);
		 var val = $('#mistableFixedheader').find('tbody').find('tr');
		 var tAmount = 0;
		 //$(".total").text(0);
	    jQuery.each(val,function(index,item){
	    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
	    });
	    //$(".subTtl").hide();	
	   
	 	$(document).on("click", " .paginate_button",function(){
	 		$(".subTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#mistableFixedheader').find('tbody').find('tr');
			 var tAmount = 0;
			 //$(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
		    });
		 
			$(".total").text(tAmount.toFixed(2));
			
		});
	   
	});

			/* *************** Paginataion Quantity Total********* */
			/* *************** Show Total********* */
					
					$(document).ready(function () {
						debugger;
						$(".subTtl").show();
				//$(".total").text(0);
				debugger;
				 var val = $('#mistableFixedheader').find('tbody').find('tr');
				 var tAmount = 0;
				 //$(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
			    });
			   //$(".subTtl").show();	
			 	$(document).on("change", " .dataTables_length",function(){
			 		$(".subTtl").show();
			 		$(".text-right").show();
			 		 var val = $('#mistableFixedheader').find('tbody').find('tr');
					 var tAmount = 0;
					 //$(".total").text(0);
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				    });
				 
					$(".total").text(tAmount.toFixed(2));
				});
			   
			});

			 	function TableLoad(){
			 		$(".subTtl").show();
			 		$(".text-right").show();
			 		 var val = $('#mistableFixedheader').find('tbody').find('tr');
					 var tAmount = 0;
					 //$(".total").text(0);
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				    });
				 
					$(".total").text(tAmount.toFixed(2));
			 	}
			 	 setTimeout(function(){
			 		TableLoad();
			 		 }, 200);
			 	
			 	
					/* *************** show Sub Total********* */
 </script>
<!-- script for data table fixed header-->
</body>
</html>
