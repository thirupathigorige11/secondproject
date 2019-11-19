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
<link href="css/topbarres.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<jsp:include page="CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.success,.error { text-align: center; font-size: 14px;}
</style>

<script type="text/javascript">
	function validate() {
		//var from = document.getElementById("ReqDateId1").value;
		//var to = document.getElementById("ReqDateId2").value;
		var site = document.getElementById("site").value;
		var PONumberId=document.getElementById("PONumberId").value;
		if (site == "" && PONumberId == "") {
			alert("Please select  site  Or Enter PO Number.");
			return false;
		}
		$(".loader-sumadhura").show();
	}
</script>
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
								<li class="breadcrumb-item active">${requestScope['POType']}</li>
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
						<div align="center">
						<div class="container border-inwards-box">
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
										
							<%String updatePO=request.getAttribute("isPoUpdated").toString(); 
							String isMarketing=request.getAttribute("isMarketingRevised")==null ? "" :request.getAttribute("isMarketingRevised").toString();
					
							if(updatePO.equalsIgnoreCase("false") && isMarketing.equalsIgnoreCase("false")){  %>
							<form class="form-inline"  action="RevisedPO.spring" >
								<div class="form-group">
							  	<label class="control-label" style="">Select Site:</label>						
									 <select name="site" id="site" class="form-control">
											<option value="">--Select--</option>									
											<c:forEach items="${siteDetails}" var="siteDetails">
												<option value="${siteDetails.key}">${siteDetails.value}</option>									
											</c:forEach>
									</select>
									<label class="control-label" style="margin-left: 30px;">PO Number:</label>
									<input type="text" name="PONumber" id="PONumberId" class="form-control"/>
							        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning btn-sm" style="margin-left: 30px;" onclick="return validate();">Submit</button>
							        <center><font color="red" style="margin-top: 10px;position: relative;top: 10px;" size="5"><c:out value="${requestScope['displayErrMsg']}"></c:out> </font></center>							       
							      </div>
						  </form>
							<% } else if(updatePO.equalsIgnoreCase("false") && isMarketing.equalsIgnoreCase("")){
							%>			
										
							<form class="form-inline"  action="RevisedPO.spring" >
								<div class="form-group">
							  	<label class="control-label" style="">Select Site:</label>						
									 <select name="site" id="site" class="form-control">
											<option value="">--Select--</option>									
											<c:forEach items="${siteDetails}" var="siteDetails">
												<option value="${siteDetails.key}">${siteDetails.value}</option>									
											</c:forEach>
									</select>
									<label class="control-label" style="">PO Number:</label>
									<input type="text" name="PONumber" id="PONumberId" class="form-control"/>
							        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning btn-sm"  onclick="return validate();">Submit</button>
							        <center><font color="red" size="5"><c:out value="${requestScope['displayErrMsg']}"></c:out> </font></center>							       
							      </div>
						  </form>
	
					<!-- ===============================================================updatePo Start====================================================================== -->			
						<%} else {%>	
						<form class="form-inline"  action="UpdatePO.spring" >  
				  	         <label class="control-label">Select Site:</label>						
							 <select name="site" id="site" class="form-control">
									<option value="">--Select--</option>									
									<c:forEach items="${siteDetails}" var="siteDetails">
									<option value="${siteDetails.key}">${siteDetails.value}</option>									
									</c:forEach>
							</select>
							<label class="control-label" style="">PO Number:</label>
							<input type="text" name="PONumber" id="PONumberId" class="form-control"/>
					        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning btn-sm"  onclick="return validate();">Submit</button>
					        <center><font color="red" size="5"><c:out value="${requestScope['displayErrMsg']}"></c:out> </font></center>
					   </form>
				      <%} %>
				  <!-- =========================================updatepo end============================================================================================	 -->	
						
					</div>
					</div>

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
		$(document).ready( function() {$(".up_down").click( function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
