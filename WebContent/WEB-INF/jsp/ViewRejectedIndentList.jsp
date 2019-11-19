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
<link href="js/inventory.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
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
						<li class="breadcrumb-item active">Get Indent List</li>
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
				<div> 
					<div class="table-responsive">
					   <table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr>  
								<th>Creation Date</th>	
	    						<th>Rejected Site Wise Indent Number</th>
	    						<th>Indent Name</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listOfIndents}" var="element">  
								<tr>
								    <td>${element.strCreateDate}</td>
									<td><a href='getProductDetailsForNewIndent.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&reqSiteId=${element.siteId}&indentNumber=${element.indentNumber}&indentName=${element.indentName}' class="anchor-class">${element.siteWiseIndentNo}</a></td>
									 <td>${element.indentName}</td>
									<input type="hidden" id=indentNumber" name="indentNumber" class="form-control" readonly="true" value="${element.indentNumber}">
								</tr>
							</c:forEach>
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
	$(document).ready( function() {
		$(".up_down").click( function() {
			$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
			$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
					});
		$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		$(".anchor-class").click(function(ev){
			if(ev.ctrlKey==false && ev.shiftKey==false){
				$(".loader-sumadhura").show();
			}
		});
});

</script>
 </body>
</html>
