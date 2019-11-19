<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="func"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

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
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.table>tbody+tbody {border-top: 1px solid #000;}
table.dataTable {border-collapse:collapse !important;}
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
						<div>
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Payments</a></li>
								<li class="breadcrumb-item active">Upload Excel</li>
							</ol>
						</div>
						<div>
							<div class="container border-inwards-box">
								<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label>
								<form class="form-horizontal" action="updatePaymentRefNoFromExcel.spring" method="POST"  enctype="multipart/form-data">
									<div class="col-md-4 col-md-offset-4 col-xs-12 col-sm-12">
										<div class="col-md-12 col-xs-12 col-sm-12">
											<div class="form-group">
												<label for="todate" class="col-md-5 col-sm-6">Upload Excel :</label>
												<div class="col-md-7 col-sm-6">
													<input type="file" name="file" id="fileUpload">
												</div>
											</div>
										</div>
										</div>
										<div class="col-md-12 col-xs-12 col-sm-12 Mrgtop10 text-center center-block">
									      <button type="submit" value="Submit" id="saveBtnId" class="btn btn-warning btn-sub-inwards" onclick="return validate();">Upload</button>
									     <div>${displayErrMsg}</div>
								      </div>
								 </form>
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
		<script src="js/jquery.dataTables.min.js"></script>
		<script src="js/dataTables.bootstrap.min.js"></script>
		<script src="js/sidebar-resp.js"></script>
        
</body>
</html>
