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
<jsp:include page="CacheClear.jsp" />
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
table.dataTable {
	border-collapse: collapse !important;
}

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

					<jsp:include page="SideMenu.jsp" />

				</div>
			</div>
			<jsp:include page="TopMenu.jsp" />

			<!-- page content -->
			<div class="right_col" role="main">
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Masters</a></li>
						<li class="breadcrumb-item active">Child Product Request
							Status</li>
					</ol>

				</div>

				<div class="col-md-12">
					<div class="table-responsive">
						<table id="tblnotification" class="table table-new cellspacing="0">
							<thead>
								<tr>

									<th>Initiated Date</th>
									<th>Temp Child Product Id</th>
									<th>Product Name</th>
									<th>Sub Product Name</th>
									<th>Child Product Name</th>
									<th>Group Name</th>
									<th>Measurement Name</th>
									<th>Pending Employee Name</th>
									<th>Status</th>
									<th>Remarks (mouse over to see)</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${ListOfTempChildProducts}" var="element">
									<tr>
										<td>${element.creationDate}</td>
										<td>${element.tempChildProductId}</td>
										<td>${element.productName}</td>
										<td>${element.subProductName}</td>
										<td>${element.childProductName}</td>
										<td>${element.materialGroupName}</td>
										<td>${element.measurementName}</td>
										<td>${element.pendingEmployeeId}</td>
										<td>${element.childProductStatus}</td>
										<td title="${element.strRemarksForTitle}">${element.strRemarksForView}</td>

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
					$('#tblnotification').DataTable(
							{
								"aLengthMenu" : [ [ 10, 25, 50, 100, -1 ],
										[ 10, 25, 50, 100, "All" ] ]
							});
				});

		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});

		$(document).ready(function() {
			$('.SubProduct1').keyup(function() {
				$(this).attr('title', $(this).val());
			});
			$('.anchor-class').click(function() {
				$('.loader-sumadhura').show();
			});
		});
	</script>

</body>
</html>
