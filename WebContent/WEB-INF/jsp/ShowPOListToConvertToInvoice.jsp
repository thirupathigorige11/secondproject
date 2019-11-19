<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
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


<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.form-control{height:34px;}
table.dataTable{border-collapse:collapse !important;}
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
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Inwards From PO</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="z-index:99;display:none;">
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
		        <center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center>
				<div>
				<div>
				  <div class="container border-inwards-box">									  
					   <form name="myForm" class="form-inline" action="showPODetails.spring"> 					 
							<div class="form-group">
							   <div class="col-md-12 text-center">
							        <span class="text-center"><font color=green size=4 face="verdana">${responseMessage}</font></span>
									<span><font size="4" color="red" face="verdana">${errorMessage}</font></span>
									<span class="text-center"><font color=red size=4 face="verdana">${responseMessage1}</font></span>
							   </div>
								<label>PO Number :</label>								
									<input type="text" class="form-control" name="poNumber" id="poNumber" autocomplete="off" />
								    <button type="submit" value="submit" class="btn btn-warning submitFunc"   onclick="window.location ('PODetailsShow.jsp')">Submit</button>
									</div>
				       </form>
					 </div>
				  </div>
				</div>				
				<div class="table-responsive"> <!-- container1 -->
				     <table id="tblnotification" class="table table-new " cellspacing="0">
							<thead>
								<tr>
									<th>PO Date</th>	
				    				<th>PO Number</th>
				    				<th>Vendor Name</th>
				    				<th>Site Name</th>
				    				<th>Indent Number</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${listOfPOs}" var="element">  
								<tr>
								    <td>${element.get("PO_DATE")}</td>									
									<td><a href='showPODetails.spring?poNumber=${element.get("PO_NUMBER")}&reqSiteId=${element.get("SITE_ID")}' class="anchor-class">${element.get("PO_NUMBER")}</a></td>									
									<td>${element.get("VENDOR_NAME")}</td>
									<td>${element.get("SITE_NAME")}</td>
									<td>${element.get("SITEWISE_INDENT_NO")}</td>
								</tr>
								</c:forEach>
							</tbody>
					 </table>
				 </div>
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
			$(document).ready( function() {
				$(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				$('.anchor-class,.submitFunc').click(function(ev){
					if(ev.ctrlKey==false && ev.shiftKey==false){
						$(".loader-sumadhura").show();
					}
				});
				
				});

       </script>
</body>
</html>
