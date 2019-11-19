<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="../CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<script type="text/javascript">    
</script>
<style>
table.dataTable {border-collapse:collapse !important;}
.breadcrumb {
    background: #eaeaea;
}
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
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">View My BOQ</li>
					</ol>
					
				</div>
				<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
				      <!-- Loader -->

    <%-- <center><span><font color=green size=4 face="verdana">${responseMessage}</font></span></center>
    <center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center> --%>
				
				<div class="col-md-12">
				<div class="table-responsive">
					<table id="tblnotification"	class="table table-new cellspacing="0">
						<thead>

						  <tr> <!-- class="tblheaderall" -->
							<th>Created Date</th>
    				        <th> BOQ Number</th>
    				        <th>Version Number</th>
    				        <th>Site Name</th>
    				       <th>Type of Work</th>
				            </tr>

						</thead>
						<tbody>
				<c:forEach items="${listofBOQ}" var="element">  
				<tr>
				    <td >${element.strBOQCreationDate}</td>					
					<td><a href="getBOQDetailsSelection.spring?BOQSeqNo=${element.strBOQSeqNo}&siteId=${element.strSiteId}&pageHighlightURL=${pageHighlightURL}" class="anchor-class">${element.strBOQNo}</a></td>					
					<td>${element.strVersionNo}</td>					
					<td>${element.strSiteName}</td>					
					<td>${element.typeOfWork}</td>
				</tr>
				</c:forEach>
				</tbody>
				</table>
				</div>
				
           </div>
				<!-- /page content -->
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
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
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
		$(document).ready(function(){
			  $('.SubProduct1').keyup(function(){
			    $(this).attr('title',$(this).val());
			  });
			  $('.anchor-class').click(function(){
				  $('.loader-sumadhura').show();
			  });
			});
		
		//this code for to active the side menu 									
		var referrer=$("#pageType").val();
		if(referrer==''||referrer==null){referrer="empty";}
		$SIDEBAR_MENU.find('a').filter(function () {
		var urlArray=this.href.split( '/' );
		for(var i=0;i<urlArray.length;i++){
		if(urlArray[i]==referrer) {
		return this.href;
		}
		}
		}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');

	</script>

</body>
</html>
