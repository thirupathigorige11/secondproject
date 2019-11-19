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
.selectAllchk span {
	font-size: 16px;
	font-weight: bold;
}

.selectAllchk .selectAllchkinput {
	height: 18px;
	width: 18px;
	vertical-align: bottom;
}

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
						<li class="breadcrumb-item active">Child Products For Approval</li>
					</ol>

				</div>

				<div class="col-md-12">
					<form id="ChildProductApprovalFormId">
						<div class="table-responsive">


							<table id="tblnotification" class="table table-new cellspacing="0">
								<thead>
									<tr>
										<th>Select</th>
										<th>Temp Child Product Id</th>
										<th>Initiated Date</th>
										<th>Product Name</th>
										<th>Sub Product Name</th>
										<th>Child Product Name</th>
										<th>Group Name</th>
										<th>Measurement Name</th>
										<th>Remarks (mouse over to see)</th>
										<th>Comments</th>

									</tr>
								</thead>
								<tbody>
									<c:forEach items="${ListOfTempChildProducts}" var="element">
										<tr>
											<td><div class="col-md-12 text-center selectAllchk">
													<input type="checkbox" class="selectAllchkinput" name="chkbox${element.intSerialNo}" value="checked" />
												</div>
												<input type="hidden" name="tempChildProductId${element.intSerialNo}" value="${element.tempChildProductId}" /> 
												<input type="hidden" name="childProductName${element.intSerialNo}" value="${element.childProductName}" /> 
												<input type="hidden" name="subProductId${element.intSerialNo}" value="${element.subProductId}" />
												</td>
											<td>${element.tempChildProductId}</td>
											<td>${element.creationDate}</td>
											<td>${element.productName}</td>
											<td>${element.subProductName}</td>
											<td>${element.childProductName}</td>
											<td>${element.materialGroupName}</td>
											<td>${element.measurementName}</td>
											<td title="${element.strRemarksForTitle}">${element.strRemarksForView}</td>
											<td><input type="text" name="remarks" value="" autocomplete="off" /></td>
										</tr>
										
									</c:forEach>
								</tbody>
							</table>
							<input type="hidden" name="noOfTempChildProducts" value="${ListSize}" />
							<div class="col-md-12 text-center center-block">
								<input type="button" value="Approve" id="btn-approve" class="btn btn-warning" onclick="approve()" /> 
								<input type="button" value="Reject" id="btn-reject" class="btn btn-warning" onclick="reject()" />
							</div>

						</div>
					</form>
				</div>
			</div>
			<!-- /page content -->
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

		function approve() {
			var isAnyChecked = validateCheckBoxes();
			if (isAnyChecked == false) {
				return false;
			}
			var canISubmit = window.confirm("Do you want to Approve?");
			if (canISubmit == false) {
				return false;
			}
			$("#btn-approve").attr("disabled", true);
			$("#btn-reject").attr("disabled", true);

			document.getElementById("ChildProductApprovalFormId").action = "approveTempChildProduct.spring";
			document.getElementById("ChildProductApprovalFormId").method = "POST";
			document.getElementById("ChildProductApprovalFormId").submit();

		}

		function reject() {
			var isAnyChecked = validateCheckBoxes();
			if (isAnyChecked == false) {
				return false;
			}
			var canISubmit = window.confirm("Do you want to Reject?");
			if (canISubmit == false) {
				return false;
			}
			$("#btn-approve").attr("disabled", true);
			$("#btn-reject").attr("disabled", true);

			document.getElementById("ChildProductApprovalFormId").action = "rejectTempChildProduct.spring";
			document.getElementById("ChildProductApprovalFormId").method = "POST";
			document.getElementById("ChildProductApprovalFormId").submit();

		}
		
		function validateCheckBoxes() {
			if ($('input[type=checkbox]:checked').length == 0) {
				alert('Please select atleast one.');
				return false;
			}
		}

		/* Reload the site when reached via browsers back button */
		if (!!window.performance && window.performance.navigation.type == 2) {
			window.location.reload();
		}
		/* Reload the site when reached via browsers back button */
	</script>

</body>
</html>

