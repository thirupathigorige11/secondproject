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
<jsp:include page="WEB-INF/jsp/CacheClear.jsp" />  
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
<!-- link href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css" rel="stylesheet">  -->

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<script type="text/javascript">    
</script>
<style>
/* new styles */
.storedailyreportth{width:31% !important;}
.toredailyreporttd{width:69% !important;word-break: break-all !important;}
 .st-val{white-space: normal !important;word-break: break-all !important;}
.total-xs span{font-weight:700;font-size:16px;padding:8px;}
.total-xs{padding:8px;}
.tbltopbottom{margin-top:25px;margin-bottom:25px;}
.totalAmountcls{text-align:right;}
.padding-project .col-md-2{padding-bottom:15px;text-align:center;}
.project1Anchor{color: blue;text-decoration: underline;font-size:16px;cursor:pointer;float:left;}
.table-bordered thead tr th{border:1px solid #000;background-color:#ccc;}
@media screen and (max-width: 767px){.table-responsive>.table-bordered {border: 1px solid #000;}}
table, .table-bordered tbody tr td{border:1px solid #000;}
.table>thead:first-child>tr:first-child>th {border-top:1px solid #000;}
/* new styles */
.right_col-dailyreport{padding:25px;}
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	.tableBody{ width: 100%;
    margin: 0px auto;
    background: #ebeeed;
    padding: 15px;}
.breadcrumb {
    background: #eaeaea;
}
.card {
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
    transition: 0.3s;
    padding:5px;
    background:#fff;
    margin-bottom:5px;
    border-radius:5px;
    overflow:hidden;
  
}

.card:hover {
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
}
.loader {
  border: 3px solid #f3f3f3;
  border-radius: 50%;
  border-top: 3px solid #3498db;
  width: 20px;
  height: 20px;
  -webkit-animation: spin 0.5s linear infinite; /* Safari */
  animation: spin 0.5s linear infinite;
  display:block;
  float:left;
  margin-left:10px;
}

/* Safari */
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
</head>
<body class="nav-md">

	<div class="container body">
		<div class="main_container" id="main_container">
			<%-- <div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="WEB-INF/jsp/SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="WEB-INF/jsp/TopMenu.jsp" />  
 --%>
 
			<!-- page content -->
			<div class="right_col-dailyreport" role="main">
			<img src="images/logo.png" class="img-responsive text-left"/><br/>
				<!-- Breadcrumbs -->
				<!--  <div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">View My BOQ</li>
					</ol>
					
				</div> -->
				<!-- Breadcrumbs -->
   
			<!-- main Page Content starting from here -->
			 <!-- Project Hyper Links -->
			  <div class="col-md-12 tableBody">
			 
			  	<div id="sitedatadetails" class=""> 
			  	
			  	</div>
			  		
			  </div>
			   </div>
			 <!-- Project Hyper Links -->	
		
			<!-- main page content end -->	
			</div>
			<!-- /page content -->
		</div>
	</div>

	<!-- jQuery -->
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="StoreDailyReport.js"></script>
	
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
					$('#tblnotification').DataTable();
				});

		  $('#tblnotification').stacktable({myClass:'stacktable small-only'}); 
		
	</script>

</body>
</html>
