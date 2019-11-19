<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>

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
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
</head>

<style>
table.dataTable{border-collapse:collapse !important;}
.anchor-click{color: #0000ff;}
.anchor-click:hover{text-decoration: underline;color: #0000ff;}
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
						<li class="breadcrumb-item active">Get PO Details</li>
					</ol>
				</div>
				<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
				<!-- LOader -->
				<center><span><font color=green size=4 face="verdana">${responseMessage}</font></span></center>
		        <center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center>
				
				<!-- user341 -->
				<div class="table-responsive"> <!-- container1 -->
						<table id="tblnotification"	class="table table-new" cellspacing="0">
								<thead>
									<tr>
										<th>PO Date</th>
										<th>Temp PO Number</th>
										<th>Site Wise Indent Number</th>
					    				<th>Type Of PO</th>
				    				</tr>
				    			</thead>
								<tbody>
										<% List<ProductDetails> poDetails = (List<ProductDetails>) request.getAttribute("listOfPOs");
										 for (ProductDetails element : poDetails) {
											 String typePO=element.getType_Of_purchase();
											 if(typePO.contains("_")){
												 typePO=typePO.replace("_"," ");
											 }
											 //String type_Of_Po=element.getType_Of_Purchase();
											//int indent_CreationId=element.getIndentNumber();
										%>
										<tr>
										<%
														out.println("<td>");
														out.println(element.getPoDate());
														out.println("</td>");
														if(!element.getIndentNo().equalsIgnoreCase("-") && !element.getType_Of_purchase().contains("MARKETING")){
														out.println("<td>");
														out.println("<a href='getCanceledPo.spring?poNumber="+element.getStrPONumber()+"&reqSiteId="+element.getSite_Id()+"&indentNumber="+element.getIndentNo()+"&oldPONumber="+element.getEdit_Po_Number()+"'  class='anchor-click'>"+element.getStrPONumber()+"</a>");
														out.println("</td>");
														}else{
															out.println("<td>");
															out.println("<a href='getModifyingMarketingPo.spring?poNumber="+element.getStrPONumber()+"&reqSiteId="+element.getSite_Id()+"&oldPONumber="+element.getEdit_Po_Number()+"'  class='anchor-click'>"+element.getStrPONumber()+"</a>");
															out.println("</td>");
														}
														out.println("<td>");
														out.println(element.getSiteWiseIndentNo());
														out.println("</td>");
														
														out.println("<td>");
														out.println(typePO);
														out.println("</td>");
														
														
													
														%>
								
									</tr>
									<%}  %>
										
								</tbody>
							</table>
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
				<script src="js/sidebar-resp.js"></script>
				
				<script>
					$(document).ready(function() {$(".up_down").click(function() {
									$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
									$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
											});
								$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					$(".anchor-click").click(function(){
						debugger;
						$(".loader-sumadhura").show();
					});
					});
				
				</script>
</body>
</html>
