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
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


</head>

<style>
table.dataTable {border-collapse: collapse !important;}

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
						<li class="breadcrumb-item active">Get Indent Details</li>
					</ol>
				</div>
		        <!-- loader -->
				<div class="loader-sumadhura" style="display: none;z-index:999;">
					<div class="lds-facebook">
						<div></div>	<div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingMessage">Loading...</div>
				</div>
			     <!-- loader -->
				<h3 class="text-center"><font color=green size=4 face="verdana">${responseMessage}</font></h3>
		        <h3 class="text-center"><font color=red size=4 face="verdana">${responseMessage1}</font></h3>
				<div>
				<div class="container border-inwards-box" >
					 <font size="4" color="red" face="verdana">${errorMessage}</font>
					 <form name="myForm" class="form-inline" id="IndentCreationDetailsShowId"> 
						<div class="inwardInvoice">
							<div class="col-md-12 col-sm-12 col-xs-12">
							 <div class="form-group">
							   <label >Indent Number :</label>
							  <input type="text" class="form-control" name="siteWiseIndentNo" id="siteWiseIndentNo" autocomplete="off" />
							</div>
						    <button type="submit" class="btn btn-warning btn-sm" onclick="indentCreate()">Submit</button>
						    </div>
						</div>
				   </form>
				</div>
				<!-- user341 -->
				<div class="Mrgtop20"> <!-- container1 -->
				<div class="table-responsive">
				     <table id="tblnotification" class="table table-new " cellspacing="0">
							<thead>
								<tr>
									<th>Create Date</th>
			 						<th>Site Wise Indent Number</th>
			 						<th>Indent Name</th>
			 						<th>Schedule Date</th>    						
			 						<th>Required Date</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pendingIndents}" var="element">  
								<tr>
								    <td>${element.createDate}</td>									
									<td><a href="IndentCreationDetailsShow.spring?siteWiseIndentNo=${element.siteWiseIndentNo}" class="anchor-class">${element.siteWiseIndentNo}</a></td>
									<td>${element.indentName}</td>
									<td>${element.scheduleDate}</td>
									<td>${element.requiredDate}</td>
									
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
			});

	/* $('.anchor-class').click(function(){
		$(".loader-sumadhura").show();
		setTimeout(function(){
			$(".loader-sumadhura").hide();	
		}, 600);
	});	 */
	$(".anchor-class").click(function(ev){
		if(ev.ctrlKey==false && ev.shiftKey==false){
			$(".loader-sumadhura").show();
		}
	});
function indentCreate(){
	var siteWiseIndentNo = $("#siteWiseIndentNo").val().trim();
	if(siteWiseIndentNo ==""){
		alert("Please fill the indent number");
		$("#siteWiseIndentNo").focus();
		return false;
	}
	$(".loader-sumadhura").show();
	document.getElementById("IndentCreationDetailsShowId").action = "IndentCreationDetailsShow.spring";
	document.getElementById("IndentCreationDetailsShowId").method = "GET";
	document.getElementById("IndentCreationDetailsShowId").submit();
}
</script>
  </body>
</html>
