<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
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
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

</head>

<style>
.table>thead>tr>th{
	background-color:#ccc;
	border:1px solid #000;
	color:#000;
	border-bottom:none;
}
.table>tbody>tr>td{
	border:1px solid #000;
}
.table-bordered {
  border: 1px solid #000;
}
.form-control{
	height:34px;
}
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
						<li class="breadcrumb-item active">Inwards From PO</li>
					</ol>
				</div>
		
				

		<center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center>
				<!-- </div> -->
                  


<div>
 <div class="container border-inwards-box">
	<div class="col-md-6 col-md-offset-3">
	  
	 <form name="myForm" class="form-horizontal" action="showPODetails.spring"> 
	 
<div class="form-group inwardInvoice">
<div class="col-md-12 text-center"><span class="text-center"><font color=green size=4 face="verdana">${responseMessage}</font></span>
		<span><font size="4" color="red" face="verdana">${errorMessage}</font></span>
		<span class="text-center"><font color=red size=4 face="verdana">${responseMessage1}</font></span></div>
	<label class="col-md-3 col-xs-12 marginTop5">PO Number :</label>
		<div class="col-md-5 col-xs-12">
		 <input type="text" class="form-control" name="poNumber" id="poNumber" autocomplete="off" />
		</div>
		<div class="col-md-3 col-xs-12">
		 <!-- <button type="submit" value="submit" class="btn btn-warning"   onclick="window.location ('PODetailsShow.jsp')"><a href="PODetailsShow.jsp"><strong>Submit</strong></a></button> -->
		 <button type="submit" value="submit" class="btn btn-warning"   onclick="window.location ('PODetailsShow.jsp')">Submit</button>
		</div>
</div>
		<input type="hidden" name = "preparedBy" value="MARKETING_DEPT" />
</form>
	</div>
</div>
</div>
					<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>								
				          </div>
						<!-- LOader -->	
<!-- user341 -->
	<div class="table-responsive "> <!-- container1 -->
	
						<table id="tblnotification"	class="table table-striped table-bordered " cellspacing="0">
							<thead>
								<tr  class="tblheaderall">
								<th>PO Date</th>		    				
	    						<th>PO Number</th>    						
	    						<th>Vendor Name</th>    						
	    						<th>Site Name</th>
								</tr>
							</thead>
							<tbody>
					<%-- <c:forEach items="${listOfPOs}" var="element">   --%>
					<% List <Map<String, Object>> poDetails = (List <Map<String, Object>>) request.getAttribute("listOfPOs"); 
					SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
					String poDate="";
					String siteId="";
					String poNumber="";
					String vendorName="";
					String siteName="";
					 for (Map<?, ?> element : poDetails) {
						 poDate=element.get("PO_DATE") == null ? "0" : element.get("PO_DATE").toString();
						 siteId=element.get("SITE_ID") == null ? "0" : element.get("SITE_ID").toString();
						 poNumber=element.get("PO_NUMBER") == null ? "0" : element.get("PO_NUMBER").toString();
						 vendorName=element.get("VENDOR_NAME") == null ? "0" : element.get("VENDOR_NAME").toString();
						siteName=element.get("SITE_NAME") == null ? "0" : element.get("SITE_NAME").toString();
						// Date afterIndentDate  = myFormat.parse(poDate);
						 //poDate=outputFormat.format(afterIndentDate); 
					
					%>
					<tr>
					<% 				out.println("<td>");
									out.println(poDate);
									out.println("</td>");								
									out.println("<td>");
									out.println("<a class='anchor-class' href='showPODetails.spring?poNumber="+poNumber+"&reqSiteId="+siteId+"&preparedBy=MARKETING_DEPT' style='text-decoration: underline;color: blue'>"+poNumber+"</a>");
									out.println("</td>");
									out.println("<td>");
									out.println(vendorName);
									out.println("</td>");
									out.println("<td>");
									out.println(siteName);
									out.println("</td>");
							%>
					
					
					
					   <%--  <td>${parsedPoDate}</td>
					    
						
						<td><a href='showPODetails.spring?poNumber=${element.get("PO_NUMBER")}&reqSiteId=${element.get("SITE_ID")}&preparedBy=MARKETING_DEPT' style="text-decoration: underline;color: blue;">${element.get("PO_NUMBER")}</a></td>
						
						<td>${element.get("VENDOR_NAME")}</td>
						<td>${element.get("SITE_NAME")}</td> --%>
						<%-- <td>${element.get("SITEWISE_INDENT_NO")}</td> --%>
						
					</tr>
					<% } %>
					<%-- </c:forEach> --%>
					</tbody>
				 </table>
				</div>
			</div>
		</div>
	</div>
		 </body>

			<script src="js/jquery-ui.js"></script>
			<script src="js/custom.js"></script>
			<sctript src="js/sidebar-resp.js"></sctript>
			<script>
				$(document).ready(function() {
					$(".up_down").click(function() {
						$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
						$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
					});				
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});
				$('.anchor-class').click(function(ev){
					if(ev.ctrlKey==false && ev.shiftKey==false){
						$(".loader-sumadhura").show();
					}
				});
				
			</script>

</html>
