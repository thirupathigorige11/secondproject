<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<html>
<head>
 
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
  <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
  <script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
<jsp:include page="../CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
		 <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>
		<!--  <link href="css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">  -->
	    <title>Sumadhura-IMS</title>
        <link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
  div.dataTables_scrollBody table { border-top: none;  margin-top: -2px !important; margin-bottom: 0 !important;}
  div.dataTables_wrapper div.dataTables_paginate { margin: 7px 0px;white-space: nowrap;text-align: right;}
  div.dataTables_wrapper {width: 1300px;margin: 0 auto;}
 </style>
</head>
<body class="nav-md" id="body-refresh">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	</style>
</noscript>
<div class="container body" id="mainDivId">
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
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Marketing</a></li>
							<li class="breadcrumb-item active">View My Hoarding Details</li>
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
			<div class="border-inwards-box">
			    <form class="form-horizontal" id="hoardingForm">
					<div class="col-md-4">
					 <div class="form-group">
					   <label class="col-md-6 col-xs-12">From Date:</label>
					   <div class="col-md-6 col-xs-12 input-group"><input type="text" onchange="fromDateChange()" class="form-control readonly-color" placeholder="Select From Date" name="sitewisefromDate" id="sitewisefromDate" readonly="true"/>
					   <label class="input-group-addon btn input-group-addon-border" for="sitewisefromDate">
								   <span class="fa fa-calendar"></span>
				             </label>
					   </div>
					 </div>
					</div>
					<div class="col-md-4">
					 <div class="form-group">
					   <label class="col-md-6 col-xs-12">To Date:</label>
					   <div class="col-md-6 col-xs-12 input-group">
					   <input type="text" class="form-control readonly-color" onchange="toDateChange()" placeholder="Select To Date" name="sitewisetoDate" id="sitewisetoDate" readonly="true"/>
					   <label class="input-group-addon btn input-group-addon-border" for="sitewisetoDate">
								   <span class="fa fa-calendar"></span>
				             </label>
					   </div>
					 </div>
					</div>
					<div class="col-md-4">
					 <div class="form-group">
					   <label class="col-md-6">Site:</label>
					   <div class="col-md-6">
							<select class="form-control" name="site" id="site">
								<option value="-1">--Select Site--</option>
								<c:forEach items="${site}" var="site">
                               <option value="${site.key}">${site.value}</option>
                               </c:forEach>
							</select>
					 </div>
					 </div>
					</div>
				  <div class="col-md-12 text-center center-block">
			    <button type="button" class="btn btn-warning" id="submitTbl" onclick="submitHoarding()">Submit</button>
			   </div> 
			   </form>
			
			</div>
		</div>
			  <!-- page content -->
			  
		
	<c:if test="${requestScope.isShow}">
			  
		<div class="col-md-12">
			<div class="table-responsive">
			    <table class="table table-new table-Market display nowrap" id="hoarding_table" style="width:100%;border-collapse:collapse !important;">
			    <thead>
			      <tr>
				       <th style="width:45px;">S.No</th>
				       <th>Child Product Name</th>
				       <th>Location</th>
				       <th>Date</th>
				       <th>Quantity</th>
				       <th>Amount</th>
				       <th>Rate</th>
				       <th>Site Name</th>
			      </tr>
			    </thead>
			    <tbody>
			    
			    
			    
			     <c:forEach var="marketingPOProductDetails" items="${requestScope['marketingPOProductDetailsList']}">    
			    
			       <tr>
			            
			            <td>${marketingPOProductDetails.serialno}</td>
			            <td>${marketingPOProductDetails.child_ProductName}</td>
			            <td>${marketingPOProductDetails.location}</td>  
			            <td>${marketingPOProductDetails.hoardingDate}</td>
			            <td>${marketingPOProductDetails.quantity}</td>
			            <td>${marketingPOProductDetails.amount}</td>
			            <td>${marketingPOProductDetails.rate}</td>
			            <td>${marketingPOProductDetails.siteName}</td>
	               </tr>
	               
	           </c:forEach>  
			    
			    
			  
			    </tbody>
			   </table>
			    </div>
			  <!-- page content -->
			  <div class="col-md-12 no-padding-left"><div class="col-md-1 no-padding-left"><h4>Note :</h4></div><div class="col-md-11 Mrgtop10"><b>Details are Displaying  as per PO</b></div></div>
			</div>
			 <c:if test="${requestScope.totalAmount != null}">
			  <div class="col-md-12 text-right"><h4><strong>Total Amount :<c:out value="${requestScope.totalAmount}"></strong></h4></c:out>
			</c:if> 
	</c:if>		
	        </div>
	      </div>
	</div>

<!-- scripts -->

       <script src="js/custom.js"></script>
        <script src="js/jquery.dataTables.min.js"></script>
		<script src="js/dataTables.bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/InwardsfromCreatePO.js" type="text/javascript"></script>
        <script src="js/jquery.timepicker.min.js"></script> 
        <script src="js/sidebar-resp.js" type="text/javascript"></script>  
        <script>
       
 $(document).ready(function(){$(".up_down").click(  function() {
		$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
		$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
		
		$("#sitewisefromDate").datepicker({dateFormat: 'dd-M-y',
			 changeMonth: true,
		      changeYear: true});
		 $("#sitewisetoDate").datepicker({dateFormat: 'dd-M-y',
			 changeMonth: true,
		      changeYear: true});
		 
				});
 $("#hoarding_table").DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
	 "scrollY": "300px",
     "scrollX": true,
     "paging": true,
     "scrollCollapse": true,
     
 });     
$( "#sitewisefromDate" ).datepicker({
	dateFormat: 'dd-M-y',
	  changeMonth: true,
    changeYear: true
  });
$( "#sitewisetoDate" ).datepicker({
	dateFormat: 'dd-M-y',
	  changeMonth: true,
    changeYear: true
  });
 
 }); 
 function submitHoarding(){
	      debugger;
		  var fromdate = $('#sitewisefromDate').val();
		  var todate = $('#sitewisetoDate').val();
		  var site = $('#site').val();
		  
		  if(fromdate == "" && todate == "" && site == "-1"){	
			  alert('Please select Fromdate or Todate Or Site');
			  return false;
			 
		  }
		  

	 $('.loader-sumadhura').show();
	 document.getElementById("hoardingForm").action = "getViewMyHoardingDetails.spring";
	 document.getElementById("hoardingForm").method = "POST";
	 document.getElementById("hoardingForm").submit();
	 
 }
 	function fromDateChange(){
		var x=$('#sitewisefromDate').datepicker("getDate");
		$("#sitewisetoDate").datepicker( "option", "minDate",x ); 	
	}
	function toDateChange(){
		var x=$('#sitewisetoDate').datepicker("getDate");
		$("#sitewisefromDate").datepicker( "option", "maxDate",x ); 	
	}
</script>
</body>
</html>
