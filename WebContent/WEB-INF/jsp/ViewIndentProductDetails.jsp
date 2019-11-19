<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.*"%>
<%@page import="com.sumadhura.bean.IndentCreationBean"%>
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
<link href="css/custom.css" rel="stylesheet"  type="text/css">
<link href="js/inventory.css" rel="stylesheet"  type="text/css">
<link href="css/topbarres.css" rel="stylesheet"  type="text/css">


<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
</head>

<style>
 table.dataTable {border-collapse:collapse !important;}
</style>
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
						<li class="breadcrumb-item active">View All Product Details</li>
					</ol>
				</div>
				<!-- user341 -->
				<div class=""> <!-- class=" container1" style="max-width: 763px;margin-right: auto;" -->
				                    <div class="table-responsive">
									<table id="tblnotification"	class="table table-new">
										<thead>
											<tr>
												<th>Site Wise Indent Number</th>	
												<th>Indent Name</th>
												<th>Product Name</th>
					    						<th>Sub Product Name</th>
					    						<th>Child Product Name</th>
					    						<th>UOM</th>				    						
					    						<th>Requested Quantity</th>
					    						<th>PO Initiated Quantity</th>
					    						<th>Received Quantity</th>
					    						<th>Pending Dept Name</th>
					    						<th>Remarks</th>
					    						<th>PO Number</th>
					    						<th>Vendor Name</th>
											</tr>
										</thead>
										<tbody>
											<%	
											
												List<IndentCreationBean> AllProductList = (List<IndentCreationBean>) request.getAttribute("allProductlist") ;
												ProductDetails objProductDetails = null;
												String productName="";
												String subProductName="";
												String childProductName="";
												String measurement="";
												String req_Quantity="";
												String po_Initiated_Quantity="";
												String receved_Quantity="";
												int site_Wise_Indent_No=0;
												String pending_Dept_Id="";
												String remarks="";
												String allocatedQuan="";
											
												double request_Quantity=0.0;
												double received_Quantity=0.0;
												double doubleRecQuantity=0.0;
												String po_init_quan="";
												String po_Number="";
												String site_Name="";
												String vendor_Id="";
												int site_Id=0;
												String indentName="";
												IndentCreationBean indentObj = new IndentCreationBean(); 
												for(int i=0;i<AllProductList.size();i++){
													
													indentObj=AllProductList.get(i);
													
												
											
												
												
												
										
												
												//	indentObj.setAllocatedQuantity();
													/* indentObj.setReceived_Quantity(receved_Quantity);
													indentObj.setPending_Dept_Id(pending_Dept_Id);
													indentObj.setType_Of_Purchase(remarks);
													indentObj.setPoIntiatedQuantity(String.valueOf(po_init_quan)); */
											
												productName=indentObj.getProduct1();
												subProductName=indentObj.getSubProduct1();
												childProductName=indentObj.getChildProduct1();
												measurement=indentObj.getUnitsOfMeasurement1();
												req_Quantity=indentObj.getStrRequestQuantity();
												indentName=indentObj.getIndentName();
												//allocatedQuan=(productList.get("ALLOCATED_QUANTITY") == null ? "0" : productList.get("ALLOCATED_QUANTITY").toString());
												receved_Quantity=indentObj.getReceived_Quantity();
												site_Wise_Indent_No=indentObj.getSiteWiseIndentNo();
												pending_Dept_Id=indentObj.getPending_Dept_Id();
												remarks=indentObj.getType_Of_Purchase();
												
												po_Number=indentObj.getPonumber();
												site_Name=indentObj.getSiteName();
												vendor_Id=indentObj.getVendorId();
												site_Id=indentObj.getSiteId();
											//	po_Initiated_Quantity=(productList.get("PO_INTIATED_QUANTITY") == null ? "0" : productList.get("PO_INTIATED_QUANTITY").toString());				
												
												po_init_quan=indentObj.getPoIntiatedQuantity();
												
											 
												
											
												 
													
													%>	
														
														<tr>
														<% 	out.println("<td>");
																		out.println(site_Wise_Indent_No);
																		out.println("</td>");
																		out.println("<td>");
																		out.println(indentName);
																		out.println("</td>");
														out.println("<td>");
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
																		out.println(req_Quantity);
																		out.println("</td>");
															out.println("<td>");
																		out.println(po_init_quan);
																		out.println("</td>");
															out.println("<td>");
																		out.println(receved_Quantity);
																		out.println("</td>");
																		out.println("<td>");
																		out.println(pending_Dept_Id);
																		out.println("</td>");
														
																		out.println("<td style='width:300px;word-break:break-all;'>");
																		out.println(remarks);
																		out.println("</td>");
															
																		if(pending_Dept_Id.equalsIgnoreCase("CANCEL PO")){
																			out.println("<td>");
																			out.println("<a href='PrintCancelPOData.spring?poNumber="+po_Number+"&siteId="+site_Id+"&vendorId1="+vendor_Id+"&poEntryId="+indentObj.getPoentryId()+"&siteName="+site_Name+"' style='text-decoration: underline;color: blue;'target='_blank'>"+po_Number+"</a>");
																			out.println("</td>");
																		}else{
																		out.println("<td>");
																		out.println("<a href='getPoDetailsList.spring?poNumber="+po_Number+"&siteId="+site_Id+"&siteName="+site_Name+"&vendorId1="+vendor_Id+"' style='text-decoration: underline;color: blue;' target='_blank'>"+po_Number+"</a>");
																		out.println("</td>");
																		}
																		out.println("<td>");
																		out.println(indentObj.getVendorName());
																		out.println("</td>");
														%>
														
														</tr>
														
																
											
												
											<% 	} %>
											
								     </tbody>
							</table>
					</div>
				</div>
				<!-- user341 -->
          </div>
      </div>     
</div>
  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>

<script>
$(document).ready(
function() {
	$(".up_down").click( function() {
		$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
		$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
		});
	$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
	});

</script>
</body>
</html>
