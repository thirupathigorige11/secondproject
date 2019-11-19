<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<jsp:include page="../CacheClear.jsp" />  
<meta http-equiv="X-UA-Compatible" content="IE=edge">
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
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
  
<style>
.success,.error {text-align: center; font-size: 16px;font-weight:bold;}
.table.dataTable {border-collapse:collapse !important;]}
.form-control{height:34px;}
@media only screen and (min-width:320px) and (max-width:768px){.form-group{text-align:left !important;}}
</style>

<script type="text/javascript">
	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;

		if (from == "" && to == "") {
			alert("Please select From Date or To Date !");
			return false;
		}
		$('.loader-sumadhura').show();
	}
</script>
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
								<li class="breadcrumb-item"><a href="#">Reports</a></li>
								<li class="breadcrumb-item active">Get GRN Details For DC</li>
							</ol>
						</div>		
						<div class="loader-sumadhura" style="display: none;z-index:999;">
						<div class="lds-facebook"> 
						<div></div> <div></div><div></div>	<div></div>	<div></div>	<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>
						<div class="border-inwards-box text-center">
						    <label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label>
							<form class="form-inline"  action="getDcGrnViewDts.spring" >
							     <div class="form-group">
								    <label for="date" class="control-label">From Date :</label>
								 </div>
								 <div class="form-group input-group">
								   <input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly>
								   <label class="input-group-addon btn input-group-addon-border" for="ReqDateId1"><span class="fa fa-calendar"></span></label>
							  	</div>
							  	 <div class="form-group">
								    <label for="todate" class="control-label">To Date :</label>
								 </div>
								  <div class="form-group input-group">
								    <input type="text" id="ReqDateId2" class="form-control readonly-color" name="toDate" value="${toDate}" autocomplete="off" readonly>
								    <label class="input-group-addon btn input-group-addon-border" for="ReqDateId2"><span class="fa fa-calendar"></span></label>
							  	</div>
							    <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning hidden-xs hidden-sm hidden-md"  onclick="return validate();">Submit</button>
							    <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning visible-xs visible-sm hidden-lg visible-md Mrgtop10 text-center center-block"  onclick="return validate();">Submit</button>
						   </form>				
					 </div>
					<%	String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
								   if(isShowGrid.equals("true")){
								%>
			       <div class="table-responsive">
					  <table id="tblnotification"	class="table table-new " cellspacing="0">
						<thead>
							<tr>
			    				<th>DC Date</th>
			    				<th>DC Number</th>
			    				<th>Vendor</th>
			    				<th>Status</th>
				            </tr>
						</thead>
						<tbody>
							<c:forEach items="${indentIssueData}" var="element">  
							<tr>
							    <td>${element.receivedDate}</td>
								<%String typeOfGrn="dcGrn"; %>
								<td><a href="getDcFormGrnViewDts.spring?invoiceNumber=${element.requesterId}&vendorId=${element.vendorId}&dcDate=${element.receivedDate}&dcEntryId=${element.dcEntryId}&type=<%=typeOfGrn%>" class="anchor-class">${element.requesterId}</a></td>
								<td>${element.vendorName}</td>
								<td>${element.status}</td>
							</tr>
							</c:forEach>
				       </tbody>
					</table>
				</div>
           <%
				   }
           %>
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
	<script src="js/sidebar-resp.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			  changeMonth: true,
		      changeYear: true,
		      maxDate: new Date(),
			  onSelect: function(selected) {
     	        $("#ReqDateId2").datepicker("option","minDate", selected)
     	        }
		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  changeMonth: true,
		      changeYear: true,
		      maxDate: new Date(),
			  onSelect: function(selected) {
	            	$("#ReqDateId1").datepicker("option","maxDate", selected)
	            	        }

		  });
		  return false;
	});
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
					$('.anchor-class').click(function(){
						$('.loader-sumadhura').show();
					});
				});

		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
