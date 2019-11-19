<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">


<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

</head>
<style>
.table.dataTable {border-collapse:collapse !important;}
.form-control{height:34px;}
.alert-msg{font-weight:bold;font-size:16px;color:green;}
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
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Inwards From Other Site</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="display:none;z-index:999;">
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
				<div class="container border-inwards-box">
				    <span class="alert-msg"><c:out value="${requestScope['Message']}"></c:out> </span>
					<form class="form-inline" name="myForm" action="IssueToOtherSitGetInvoiceIdDetails.spring">
						<div class="form-group">
							<label class="control-label" for="InvoiceId">Invoice ID:</label> 
							<input type="text" name="RequestId" class="form-control" id="RequestId" autocomplete="off" />
						</div>
						<button type="submit" value="submit" class="btn btn-warning" onclick="return validate()"> Submit</button>
					</form>
				</div>
	            <c:set value="1" var="serialNo"></c:set>
				<div class="table-responsive">
				 <table id="tblnotification" class="table table-new">
						<thead>
							<tr>
								<th>S No</th>
								<th>Requested ID</th>
								<th>Indent Number</th>
								<th>Issued Site</th>
								<th>Issued Time</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${listOfIssueToOtherSiteInwardList}" var="inwardList">
							<tr>
								<td>${serialNo} <c:set value="${serialNo+1}" var="serialNo"></c:set></td>
								<td><a class="anchor-class" href="IssueToOtherSitGetInvoiceIdDetails.spring?RequestId=${inwardList.intrmidiatetEntryId}&fromSite=${inwardList.fromSite}&fromSiteId=${inwardList.fromSiteId}"> ${inwardList.intrmidiatetEntryId}</a>  </td>
								<td>${inwardList.siteWiseIndentNo}</td>
								<td>${inwardList.fromSite}</td>
								<td>${inwardList.issuedTime}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
            </div>
        </div>
    </div>
	<script src="js/jquery-ui.js"></script>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	
	
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script src="js/custom.js"></script>

	<script>
		$(document).ready(	function() { 
			   $(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
				$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
			});

		$(".anchor-class").click(function(ev){
			if(ev.ctrlKey==false && ev.shiftKey==false){
				$(".loader-sumadhura").show();
			}
		});	
		
		/* Reload the site when reached via browsers back button */
		if(!!window.performance && window.performance.navigation.type == 2)
		{
		    window.location.reload();
		}
	    /* Reload the site when reached via browsers back button */
		
	</script>
 </body>
</html>
