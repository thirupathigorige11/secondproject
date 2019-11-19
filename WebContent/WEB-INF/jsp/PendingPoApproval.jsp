
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="java.util.List"%>
	<%@page import="com.sumadhura.bean.IndentCreationBean"%>

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
<link href="css/style.css" rel="stylesheet"type="text/css">
<link href="css/custom.css" rel="stylesheet"type="text/css">
<link href="css/topbarres.css" rel="stylesheet"type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css"/>

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
    .form-control{height:34px;}
	.table.dataTable {border-collapse:collapse !important;}
	 table.table-bordered.dataTable th:last-child{border-right-width:1px;}
	.container-indents{font-size:15px;}
	.success,.error {text-align: center;font-size: 14px;}
	.input-group-addon {border: 1px solid #ccc !important;border-left-width: 0 !important;}
	table.table-bordered.dataTable th{border-left-width:1px;}
	@media only screen and (max-width: 800px) and (min-width:320px){.dataTables_paginate {display: block;}}
</style>

<script type="text/javascript">
	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var tempPoNumber = document.getElementById("tempPoNumber").value;
		if (from == "" && to == "" && tempPoNumber=="") {
			alert("Atleast one field is required!");
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
								<li class="breadcrumb-item active">View Pending PO Approval</li>
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
						<h3 class="text-center"><span><font color=red size=4 face="verdana">${requestScope['message1']}</font></span></h3>	
						<h3 class="text-center"><span><font color=green size=4 face="verdana">${message}</font></span></h3>
						<div class="">
						<div class="container container-indents">
						 <label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label>	
						  <form class="form-horizontal"  action="ViewPoPendingforApproval.spring">
							<div class="col-md-10 col-md-offset-1">
						    <div class="col-md-6">
						     <div class="form-group">
							    <label for="date" class="col-md-6">From Date :</label>
							    <div class="col-md-6 input-group">
							     <input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly>
							     <label class="input-group-addon btn" for="ReqDateId1">
										<span class="fa fa-calendar"></span>
									</label>							     
							    </div>
						  	</div>
						    </div>
						     <div class="col-md-6">
						      <div class="form-group">
							    <label for="todate" class="col-md-6">To Date :</label>
							    <div class="col-md-6 input-group">
							     <input type="text" id="ReqDateId2" class="form-control readonly-color"  name="toDate" value="${toDate}" autocomplete="off" readonly>
							     <label class="input-group-addon btn" for="ReqDateId2">
										<span class="fa fa-calendar"></span>
									</label>
							    </div>
						  	</div>
						     </div>
						     <div class="clearfix"></div>
						  	<div class="col-md-12 col-xs-12"><div class="col-md-5 col-xs-4"><hr class="hr-line"></div><div class="col-md-2 col-xs-2 text-center Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 col-xs-4"><hr class="hr-line"></div></div>
						   <div class="clearfix"></div>
						  	<!-- <hr><label class="or-class" style="position: absolute; margin-top: -33px; margin-left: 191px;margin-bottom: 15px;" >(Or)</label> -->
						  	<div class="col-md-6 col-md-offset-3">
						  	 <div class="form-group">
							    <label for="todate" class="col-md-6">Temp PO Number:</label>
							    <div class="col-md-6">
							     <input type="text" id="tempPoNumber" class="form-control" name="tempPoNumber" value="${tempPoNumber}" autocomplete="off">
							    </div>
						  	</div>
						  	</div>
						    <div class="col-md-12 text-center center-block">
						        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning"onclick="return validate();">Submit</button>
						        <div style="margin-top: 36px;display:none;">${displayErrMsg}</div>
						       <%--  <div style="margin-top: 36px;">${response}</div> --%>
						     </div>
						    </div>
						  </form>										
						</div>
					</div>
	
					<%
					   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
					   if(isShowGrid.equals("true")){
					%>
				<div class="clearfix"></div>	
				<div class="Mrgtop20">
					<div class="table-responsive "> <!-- container1 -->
						<table id="tblnotification"	class="table table-striped table-bordered table-new" cellspacing="0">
							<thead>
		                        <tr>
									<th>Created Date</th>
									<th>Created by</th>
				    				<th>Temp PO/PO Number</th>
				    				<th>PO Amount</th>
				    				<th>Project Name</th>
				    				<th>Vendor Name</th>
				    				<th>Site Wise Indent Number</th>
				    				
				    				<th>Type of PO</th>
				    				<th>Pending Emp Name</th>
						       </tr>
						   </thead>
						   <tbody>
								<% List<IndentCreationBean> poDetails = (List<IndentCreationBean>) request.getAttribute("PendingPoDetails");
								
								for (IndentCreationBean element : poDetails) {
									 String type_Of_Po=element.getType_Of_Purchase();
									int indent_CreationId=element.getIndentNumber();
									
									// int count=Integer.valueOf((element.getStrSerialNumber())); 
								%>
				
								<tr>              
								<%
														out.println("<td style='color: black'>");
														out.println(element.getStrScheduleDate());
														out.println("</td>");
														
														out.println("<td style='color: black'>");
														out.println(element.getCreatedBY());
														out.println("</td>");
														
														if(type_Of_Po.equalsIgnoreCase("CANCEL PO")){
															
															out.println("<td>");
															out.println("<a  href='showPerminentPODetailsToCancel.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&indentNumber="+element.getIndentNumber()+"&siteWiseIndentNo="+element.getSiteWiseIndentNumber()+"&siteName="+element.getSiteName()+"&fromdate="+element.getFromDate()+"&toDate="+element.getToDate()+"&poType="+element.getPreparedBy()+"&isApprove=true' class='anchor-class' id='ponumber'+count+>"+element.getPonumber()+"</a>");
															out.println("</td>");
														
														}else if(type_Of_Po.equalsIgnoreCase("UPDATE PO")){
															out.println("<td>");
															out.println("<a href='getDetailsforPoApproval.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&indentNumber="+element.getIndentNumber()+"&siteWiseIndentNo="+element.getSiteWiseIndentNumber()+"&siteName="+element.getSiteName()+"&fromdate="+element.getFromDate()+"&toDate="+element.getToDate()+"&isUpdate=true' style='text-decoration: underline;color: blue;' class='poNumber'>"+element.getPonumber()+"</a>");
															out.println("</td>");
														}
														else{
														if(indent_CreationId!=0 && !type_Of_Po.equalsIgnoreCase("MARKETING DEPT")){
														out.println("<td>");
														out.println("<a href='getDetailsforPoApproval.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&indentNumber="+element.getIndentNumber()+"&siteWiseIndentNo="+element.getSiteWiseIndentNumber()+"&siteName="+element.getSiteName()+"&fromdate="+element.getFromDate()+"&toDate="+element.getToDate()+"' class='anchor-class' class='anchor-class'>"+element.getPonumber()+"</a>");
														out.println("</td>");
														}else{
															out.println("<td>");
															out.println("<a href='getDetailsforMarketingPoApproval.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&indentNumber="+element.getIndentNumber()+"&siteWiseIndentNo="+element.getSiteWiseIndentNumber()+"&siteName="+element.getSiteName()+"&fromdate="+element.getFromDate()+"&toDate="+element.getToDate()+"' class='anchor-class'>"+element.getPonumber()+"</a>");
															out.println("</td>");
														}
														}
														out.println("<td style='color: black'>");
														out.println(element.getPoAmount());
														out.println("</td>"); 
														
														out.println("<td style='color: black'>");
														out.println(element.getSiteName());
														out.println("</td>");
														
														out.println("<td style='color: black'>");
														out.println(element.getVendorName());
														out.println("</td>");
														
														out.println("<td style='color: black'>");
														out.println(element.getSiteWiseIndentNumber());
														out.println("</td>");
														
														
														
														out.println("<td style='color: black'>");
														out.println(type_Of_Po);
														out.println("</td>");
														
														out.println("<td style='color: black'>");
														out.println(element.getPending_Emp_Name());
														out.println("</td>");
													
														%>
				
					
									</tr>
									
									<%}  %>
									
									<% %>
				
						    <tbody>
					   </table>
				   </div>
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
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			 changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	     	        $("#ReqDateId2").datepicker("option","minDate", selected)
	     	        }

		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			  changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	            	$("#ReqDateId1").datepicker("option","maxDate", selected)
	            	        }

		  });
		  return false;
	});
		$(document).ready(function() {
					$(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					$(".anchor-class").click(function(ev){
						if(ev.ctrlKey==false && ev.shiftKey==false){
							$(".loader-sumadhura").show();
						}
					});
				});
		var referrer="${url}";
		$SIDEBAR_MENU.find('a').filter(function () {
		var urlArray=this.href.split( '/' );
		for(var i=0;i<urlArray.length;i++){
		if(urlArray[i]==referrer) {
		return this.href;
		}
		}
		}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
		   //$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
