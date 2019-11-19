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
<link href="js/inventory.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<jsp:include page="CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.success,.error {text-align: center;font-size: 14px;}
.input-group-addon {border:1px solid #ccc !important;}
.anchor-blue{color:#0000ff;text-decoration:underline;}
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
								<li class="breadcrumb-item active">View Closed Indents</li>
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
						<c:choose>
						<c:when test="${purchase}">						
						<div class="container border-inwards-box">
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out></label> 
							<form class="form-horizontal"  action="ClosedIndentDetails.spring">	
							   <div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 ">					   
								     <div class="col-md-6 col-sm-6 col-xs-12 Mrgtop10">
									       <div class="form-group">
											    <label class="col-md-4 col-sm-6 col-xs-12 control-label" for="date">From Date :</label>
											    <div class="col-md-6 col-sm-6 col-xs-12 input-group">
											        <input type="text" class="form-control" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
											        <label class="input-group-addon btn" for="ReqDateId1"> <span class="fa fa-calendar"></span> </label>
											    </div>
									  	   </div>
								     </div>
								  	<div class="col-md-6 col-sm-6 col-xs-12 Mrgtop10">
									  	   <div class="form-group">
											    <label for="todate" class="col-md-4 col-sm-6 col-xs-12 control-label">To Date :</label>
											    <div class="col-md-6 col-sm-6 col-xs-12 input-group">
											      <input type="text" class="form-control" id="ReqDateId2"  name="toDate" value="${toDate}" autocomplete="off" readonly="true">
											      <label class="input-group-addon btn" for="ReqDateId2"> <span class="fa fa-calendar"></span></label>
											    </div>
											</div>	
								  	</div>
								  	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
									  	 <div class="col-lg-5 col-md-5 col-xs-5 col-sm-5"><hr class="hr-line"/></div>
									  	 <div class="col-md-1 col-lg-1 col-xs-2 col-sm-2 Mrgtop10"><strong>(Or)</strong></div>
									  	 <div class="col-lg-5 col-md-5 col-xs-5 col-sm-5"><hr class="hr-line"/></div>
								  	</div>
								  	<div class="col-md-6 col-sm-6 col-xs-12">
									  	 <div class="form-group">
										    <label for="todate" class="col-md-4 col-sm-6 col-xs-12 control-label">Indent Number :</label>
										    <div class="col-md-6 col-sm-6 col-xs-12">
										      <input type="text" class="form-control" id="indentNumber"  name="indentNumber" value="${indentNumber}" autocomplete="off">
										    </div>
									  	</div>
								  	</div>
								  	<div class="col-md-6 col-sm-6 col-xs-12">
									  	 <div class="form-group">
									  	    <label class="control-label col-md-4 col-sm-6 col-xs-12">Select Site :</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
											   <select name="site" id="site" class="form-control">
														<option value="">--Select--</option>																
														<c:forEach items="${siteDetails}" var="siteDetails">
														 <option value="${siteDetails.key}">${siteDetails.value}</option>
														</c:forEach>
												</select>
											</div>		
									  	 </div>
								  	</div>
							        <div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 text-center center-block">
								        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
								        <%-- <div style="margin-top: 36px">${displayErrMsg}</div> --%>
							       </div>
						     </div>
						  </form>	
					</div>					
					</c:when>
					<c:otherwise>
					<div class="container border-inwards-box">
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out></label> 
							<form class="form-horizontal"  action="ClosedIndentDetails.spring">	
							   <div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 ">					   
								     <div class="col-md-6 col-sm-6 col-xs-12 Mrgtop10">
									       <div class="form-group">
											    <label class="col-md-4 col-sm-6 col-xs-12 control-label" for="date">From Date :</label>
											    <div class="col-md-6 col-sm-6 col-xs-12 input-group">
											        <input type="text" class="form-control" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
											        <label class="input-group-addon btn" for="ReqDateId1"> <span class="fa fa-calendar"></span> </label>
											    </div>
									  	   </div>
								     </div>
								  	<div class="col-md-6 col-sm-6 col-xs-12 Mrgtop10">
									  	   <div class="form-group">
											    <label for="todate" class="col-md-4 col-sm-6 col-xs-12 control-label">To Date :</label>
											    <div class="col-md-6 col-sm-6 col-xs-12 input-group">
											      <input type="text" class="form-control" id="ReqDateId2"  name="toDate" value="${toDate}" autocomplete="off" readonly="true">
											      <label class="input-group-addon btn" for="ReqDateId2"> <span class="fa fa-calendar"></span></label>
											    </div>
											</div>	
								  	</div>
								  	<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
									  	 <div class="col-lg-5 col-md-5 col-xs-5 col-sm-5"><hr class="hr-line"/></div>
									  	 <div class="col-md-1 col-lg-1 col-xs-2 col-sm-2 Mrgtop10"><strong>(Or)</strong></div>
									  	 <div class="col-lg-5 col-md-5 col-xs-5 col-sm-5"><hr class="hr-line"/></div>
								  	</div>
								  	<div class="col-md-offset-3 col-md-6 col-sm-6 col-xs-12">
									  	 <div class="form-group">
										    <label for="todate" class="col-md-4 col-sm-6 col-xs-12 control-label">Indent Number :</label>
										    <div class="col-md-6 col-sm-6 col-xs-12">
										      <input type="text" class="form-control" id="indentNumber"  name="indentNumber" value="${indentNumber}" autocomplete="off">
										    </div>
									  	</div>
								  	</div>
								  	<%-- <div class="col-md-6 col-sm-6 col-xs-12">
									  	 <div class="form-group">
									  	    <label class="control-label col-md-4 col-sm-6 col-xs-12">Select Site :</label>
											<div class="col-md-6 col-sm-6 col-xs-12">
											   <select name="site" id="site" class="form-control">
														<option value="">--Select--</option>																
														<c:forEach items="${siteDetails}" var="siteDetails">
														 <option value="${siteDetails.key}">${siteDetails.value}</option>
														</c:forEach>
												</select>
											</div>		
									  	 </div>
								  	</div> --%>
							        <div class="col-md-12 col-sm-12 col-xs-12 col-lg-12 text-center center-block">
								        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
								        <%-- <div style="margin-top: 36px">${displayErrMsg}</div> --%>
							       </div>
						     </div>
						  </form>	
					</div>
					</c:otherwise>
					</c:choose>
					<%
					   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
					   if(isShowGrid.equals("true")){
					%>
				
			     <div class="table-responsive"> <!-- container1 -->
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
	                          <tr>
								<th>Indent Number</th>		
			    				<th>Indent Name</th>	
			    				<th>Closed Date</th>    				
	    				        <th>Site Name</th>
					         </tr>
						</thead>
						<tbody>
						<c:forEach items="${indentClosedData}" var="element">  
							<tr>
								<td><a href="ClosedIndentsList.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.ponumber}" class="anchor-blue">${element.siteWiseIndentNo}</a></td>
								<td>${element.indentName}</td>
							 	<td>${element.strScheduleDate}</td>
							 	<td>${element.siteName}</td>
							</tr>
						</c:forEach>
						<tbody>
				
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
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script type="text/javascript">


	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var indentNumber = document.getElementById("indentNumber").value;
		var site=$("#site").val();

		if (from == "" && to == "" && indentNumber == "") {
			alert("Please select From Date or To Date or Indent Number!");
			return false;
		}
		if(site == "" ){
			alert("Please select site!");
			return false;
		}
		$(".loader-sumadhura").show();
	}

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
					$(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
