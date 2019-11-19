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
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css"/>

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
 table.dataTable {border-collapse:collapse !important;}
 table.table-bordered.dataTable th:last-child{border-right-width:1px;}
 .form-control{height:34px;}
 .input-group-addon {border: 1px solid #ccc !important;border-left-width: 0 !important;}
 table.table-bordered.dataTable th{border-left-width:1px;}
 
</style>
<script type="text/javascript">
	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var poNum = document.getElementById("poNumber").value;
		if (from == "" && to == "" && poNum == "") {
			alert("Atleast one field is required!");
			$("#ReqDateId1").focus();
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
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Cancel PO</li>
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
			     <div class="col-md-12">
					 <div class="container border-inwards-box">										  
						 <form name="myForm" class="form-horizontal" action="cancelPerminentPO.spring"> 
						      <div class="col-md-offset-1 col-md-10">
							   <div class="col-md-12 text-center"><span class="text-center"><font color=red size=4 face="verdana">${responseMessage}</font></span>
									<%-- <span><font size="4" color="red" face="verdana">${errorMessage}</font></span>
									<span class="text-center"><font color=red size=4 face="verdana">${responseMessage1}</font></span> --%>
								</div>
								<div class="col-md-6 col-xs-12">
								  <div class="form-group">
								  <label class="control-label col-md-4 col-xs-12">PO From Date :</label>									
								   <div class="col-md-8 col-xs-12 input-group">
								     <input type="text" class="form-control readonly-color" name="fromDate" id="ReqDateId1" autocomplete="off" readonly/>	
								      <label class="input-group-addon btn ReqDateId1" for="ReqDateId1">
										<span class="fa fa-calendar"></span>
									</label>
								   </div>
							    </div>	
								</div>
								<div class="col-md-6 col-xs-12">
								  <div class="form-group">
								  <label class="control-label col-md-4 col-xs-12">PO To Date :</label>									
								  <div class="col-md-8 col-xs-12 input-group">
								    <input type="text" class="form-control readonly-color" name="toDate" id="ReqDateId2" autocomplete="off" readonly/>	
								   <label class="input-group-addon btn ReqDateId2" for="ReqDateId2">
										<span class="fa fa-calendar"></span>
									</label>
								  </div>
								</div>		
								</div>
								<div class="col-md-5 col-xs-5"><hr class="hr-line"></div><div class="col-md-2 col-xs-2 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 col-xs-5"><hr class="hr-line"></div>	
								<div class="clearfix"></div>
								<div class="col-md-offset-3 col-md-6">
								  <div class="form-group">
								  <label class="control-label col-md-4 col-xs-12">PO Number :</label>									
								  <div class="col-md-8 col-xs-12">
								    <input type="text" class="form-control" name="poNumber" id="poNumber" autocomplete="off" />	
								  </div>
								</div>		
								</div>
							   </div>
							   <div class="col-md-12 text-center center-block"><button type="submit" value="submit" class="btn btn-warning text-center"   onclick="return validate();">Submit</button></div>
					   </form>						
					</div>
               </div> 
               <c:if test="${showData}"> 
               <c:if test="${isMarketing}">
               <div class="col-md-12">
			   <div class="table-responsive"> 
					<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0">
						<thead>				
							<tr>
								<th>PO Creation Date</th>				    				
	    						<th>PO Number</th>				    						
	    						<th>Vendor Name</th>				    						
	    						<th>Project Name</th>	
							</tr>
					    </thead>
						<tbody>
							<c:forEach items="${listOfPOs}" var="element">  
								<tr>
								    <td>${element.get("PO_DATE")}</td>									
									<td><a href='showPerminentPODetailsToCancel.spring?poNumber=${element.get("PO_NUMBER")}&siteId=${element.get("SITE_ID")}&poType=${element.get("PREPARED_BY")}&siteName=${element.get("SITE_NAME")}&isApprove=false' style="text-decoration: underline;color: blue;" class="ponumber">${element.get("PO_NUMBER")}</a></td>
									<td>${element.get("VENDOR_NAME")}</td>
									<td>${element.get("SITE_NAME")}</td>
																		
								</tr>
						  </c:forEach>
					   </tbody>
					</table>
				</div>
              </div>
              </c:if>
              <c:if test="${isPurchase}">
              <div class="col-md-12">
			   <div class="table-responsive"> 
					<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0">
						<thead>				
							<tr>
								<th>PO Creation Date</th>				    				
	    						<th>PO Number</th>				    						
	    						<th>Vendor Name</th>				    						
	    						<th>Project Name</th>				    						
	    						<th>SiteWise Indent Number</th>
							</tr>
					    </thead>
						<tbody>
							<c:forEach items="${listOfPOs}" var="element">  
								<tr>
								    <td>${element.get("PO_DATE")}</td>									
									<td><a href='showPerminentPODetailsToCancel.spring?poNumber=${element.get("PO_NUMBER")}&siteName=${element.get("SITE_NAME")}&siteId=${element.get("SITE_ID")}&poType=${element.get("PREPARED_BY")}&isApprove=false' style="text-decoration: underline;color: blue;" class="ponumber">${element.get("PO_NUMBER")}</a></td>
									<td>${element.get("VENDOR_NAME")}</td>
									<td>${element.get("SITE_NAME")}</td>
									<td>${element.get("SITEWISE_INDENT_NO")}</td>									
								</tr>
						  </c:forEach>
					   </tbody>
					</table>
				  </div>
               </div>
              </c:if> 
              </c:if>               
            </div>
          </div>
      </div>
    </body>  
     
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
					});
				   $('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				   $(".ponumber").click(function(){
					   $(".loader-sumadhura").show();
				   });
		});		
		$(function() {
			  $("#ReqDateId1").datepicker({
				  dateFormat: 'dd-M-y',
				maxDate: new Date(),
				 changeMonth: true,
			      changeYear: true
			  });
			  $(".ReqDateId1").click(function(){
				  $("#ReqDateId1").focus();
			  });
			  $("#ReqDateId2").datepicker({
				  dateFormat: 'dd-M-y',
				  maxDate: new Date(),
				  changeMonth: true,
			      changeYear: true
			  });
			  $(".ReqDateId2").click(function(){
				  $("#ReqDateId2").focus();
			  });
			  return false;
		});
		
	</script>
</html>
