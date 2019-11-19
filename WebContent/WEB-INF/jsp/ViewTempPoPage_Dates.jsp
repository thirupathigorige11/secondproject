
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="CacheClear.jsp" />  
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.table.dataTable {border-collapse:collapse !important;}
.container-indents{font-size:15px;}
.success,.error {text-align: center;font-size: 18px;}
.input-group-addon{border:1px solid #ccc !important;}
</style>

<script type="text/javascript">


	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var tempPoNumber = document.getElementById("tempPoNumber").value;
		var isAllpos=document.getElementById("isAllpos").value;
		
		if (from == "" && to == "" && tempPoNumber == "") {
			//alert("Please select From Date or To Date or temp PO Number!");
			alert("At least one field is required.");
			return false;
		}
			if(isAllpos == "true") {
			document.getElementById("CreateCentralIndentFormId").action = "ViewAllTempPoPage.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}else{
			document.getElementById("CreateCentralIndentFormId").action = "ViewTempPoPage.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
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
								<li class="breadcrumb-item active">View Temp PO</li>
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
						<div>
						<div class="container border-inwards-box">
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 									
										
							<form class="form-horizontal"  id="CreateCentralIndentFormId" >
						    <div class="col-md-10 col-md-offset-1 col-sm-12 col-xs-12">
						     <div class="col-md-6 col-sm-12 col-xs-12">
						      <div class="form-group">
							    <label for="date" class="col-md-6 col-sm-12 col-xs-12 control-label">From Date :</label>
							    <div class="col-md-6 input-group"><input type="text" class="form-control readonly-color" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off" readonly="true"/>
						  	     <label class="input-group-addon btn" for="ReqDateId1"> <span class="fa fa-calendar"></span> </label>
						  	    </div>
						  	</div>
						     </div>
						  	<div class="col-md-6 col-sm-12 col-xs-12">
						  	 <div class="form-group">
							    <label for="todate" class="col-md-6 col-sm-12 col-xs-12 control-label">To Date :</label>
							    <div class="col-md-6 input-group">
							     <input type="text" id="ReqDateId2" class="form-control readonly-color"  name="toDate" value="${toDate}" autocomplete="off" readonly="true">
							      <label class="input-group-addon btn" for="ReqDateId2"> <span class="fa fa-calendar"></span> </label>
							    </div>
						  	</div>
						  	</div>
						  	
						  	<div class="col-md-12 col-xs-12 col-sm-12">
						  	 <div class="col-md-5 col-sm-5 col-xs-4"><hr class="hr-line"></div><div class="col-md-2 col-xs-2 col-sm-2 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 col-xs-4 col-sm-5"><hr class="hr-line"></div>
						  	</div>
							 <!-- <hr><label class="or-class" style="position: absolute; margin-top: -33px; margin-left: 191px;margin-bottom: 15px;" >(Or)</label>
							     -->
						  	<div class="col-md-6 col-md-offset-3">
						  	 <div class="form-group">
							    <label for="todate" class="col-md-6 col-sm-12 col-xs-12 control-label">Temporary PO Number :</label>
							    <div class="col-md-6">
							     <input type="text" id="tempPoNumber" class="form-control" name="tempPoNumber" value="${tempPONumber}" autocomplete="off">
							    <input type="hidden" id="isAllpos" class="form-control" name="isAllpos" value="${isAllPO}" autocomplete="off">
							    </div>
						  	</div>
						  	</div>
						    <!-- <div class="form-group">   -->      
						      <div class="col-md-12 col-sm-12 col-xs-12 text-center center-block">
						        <button type="button"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
						        <div style="margin-top: 36px;display:none;">${displayErrMsg}</div>
						      </div>
						   <!--  </div> -->
						    </div>
						  </form>
						</div>
						</div>

	<!-- 	=============================================show all types of pos start================================================================ -->



				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
					String typePO=request.getAttribute("AllPOs") == null ? "" : request.getAttribute("AllPOs").toString();
				   if(isShowGrid.equals("true") && typePO.equalsIgnoreCase("All")){
				%>
				<div class="">
			<div class="table-responsive Mrgtop20"> <!-- container1 -->
					<table id="tblnotification"	class="table table-new " cellspacing="0"> <!-- st-table -->
						<thead>
							<tr>
								<th>Temp PO Number</th>
			    				<th>Indent Number</th>
			    				<th>Created By</th>
			    				<th>Creation Date</th>
			    				<th>Project Name</th>
			    			    <th>Pending Employee Name</th>
			    			    <th>PO Number</th>
			    			    <th>Type Of PO</th>
						  </tr>
						</thead>
						<tbody>
						<% List<IndentCreationBean> poDetails = (List<IndentCreationBean>) request.getAttribute("PendingPoDetails");
						 for (IndentCreationBean element : poDetails) {
							// String type_Of_Po=element.getType_Of_Purchase();
							int indent_CreationId=element.getIndentNumber();
						 
						%>
				
							<tr>
							  <%
										
										if(indent_CreationId!=0){
										out.println("<td>");
										out.println("<a href='ViewTempPoPageShow.spring?siteWiseIndentNo="+element.getSiteWiseIndentNo()+"&poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&siteName="+element.getSiteName()+"' class='anchor-class'>"+element.getPonumber()+"</a>");
										out.println("</td>");
										}else{
											out.println("<td>");
											out.println("<a href='ViewMarketingTempPoPageShow.spring?siteWiseIndentNo="+element.getSiteWiseIndentNo()+"&poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&siteName="+element.getSiteName()+"' class='anchor-class'>"+element.getPonumber()+"</a>");
											out.println("</td>");
										}
										out.println("<td>");
										out.println(element.getSiteWiseIndentNo());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getToEmpName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getStrScheduleDate());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getSiteName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getPending_Emp_Name());
										out.println("</td>");
									
										out.println("<td>");
										out.println(element.getOld_Po_Number());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getType_Of_Purchase());
										out.println("</td>");
									
										%>
						    </tr>
						  <%}  %>
				
						<tbody>
				
					</table>
				</div>
				</div>
           <%
				   }
           %>
           <!-- 	=============================================show all types of pos end================================================================ -->
			
			
		<!-- 	=============================================show purchase types of pos start================================================================ -->



				<%
				   String isPurchaseShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
					String typePOPurchase=request.getAttribute("AllPOs") == null ? "" : request.getAttribute("AllPOs").toString();
				   if(isPurchaseShowGrid.equals("true") && typePOPurchase.equalsIgnoreCase("PURCHASE_PO")){
				%>
				<div class="">
			<div class="table-responsive Mrgtop20"> <!-- container1 -->
					<table id="tblnotification"	class="table table-new " cellspacing="0"> <!-- st-table -->
						<thead >
							<tr>
							 <th>Temp PO Number</th>
		    				 <th>Indent Number</th>
		    				 <th>Created By</th>
		    				 <th>Creation Date</th>
		    				 <th>Project Name</th>
		    			     <th>Pending Employee Name</th>
		    			     <th>PO Number</th>
		    			     <th>Type Of PO</th>
						   </tr>
						</thead>
						<tbody>
						<% List<IndentCreationBean> poDetails = (List<IndentCreationBean>) request.getAttribute("PendingPoDetails");
						 for (IndentCreationBean element : poDetails) {
							// String type_Of_Po=element.getType_Of_Purchase();
							int indent_CreationId=element.getIndentNumber();
						 
						%>
						<%-- <c:forEach items="${indentgetData}" var="element">   --%>
						<tr>
						  <%
												
									
										out.println("<td>");
										out.println("<a href='ViewTempPoPageShow.spring?siteWiseIndentNo="+element.getSiteWiseIndentNo()+"&poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&siteName="+element.getSiteName()+"' class='anchor-class'>"+element.getPonumber()+"</a>");
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getSiteWiseIndentNo());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getToEmpName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getStrScheduleDate());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getSiteName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getPending_Emp_Name());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getOld_Po_Number());
										out.println("</td>");
									
										out.println("<td>");
										out.println(element.getType_Of_Purchase());
										out.println("</td>");
									
										%>
					
					</tr>
				<%}  %>
				<tbody>
				</table>
				</div>
				</div>
           <%
				   }
           %>
           <!-- 	=============================================show purchase types of pos end================================================================ -->
			
		<!-- 	=============================================show marketing types of pos start================================================================ -->



				<%
				   String isMarketingShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
					String typePOMarketing=request.getAttribute("AllPOs") == null ? "" : request.getAttribute("AllPOs").toString();
				   if(isMarketingShowGrid.equals("true") && typePOMarketing.equalsIgnoreCase("MARKETING_PO")){
				%>
				<div class="">
			<div class="table-responsive Mrgtop20"> <!-- container1 -->
					<table id="tblnotification"	class="table table-new " cellspacing="0"> <!-- st-table -->
						<thead>
					     <tr>
							<th>Temp Po Number</th>	    				
		    				<!-- <th>Indent Number</th> -->
		    				<th>Created By</th>
		    				<th>Creation Date</th>
		    				<th>Project Name</th>
		    			   <th>Pending Employee Name</th>
		    			   <th>PO Number</th>
		    			   <th>Type Of PO</th>
				        </tr>
						</thead>
						<tbody>
						<% List<IndentCreationBean> poDetails = (List<IndentCreationBean>) request.getAttribute("PendingPoDetails");
						 for (IndentCreationBean element : poDetails) {
							// String type_Of_Po=element.getType_Of_Purchase();
							//int indent_CreationId=element.getIndentNumber();
						 
						%>
				
						<tr>
						  <%
												
									
				  						out.println("<td>");
										out.println("<a href='ViewMarketingTempPoPageShow.spring?siteWiseIndentNo="+element.getSiteWiseIndentNo()+"&poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&siteName="+element.getSiteName()+"' class='anchor-class'>"+element.getPonumber()+"</a>");
										out.println("</td>");
										
										/* out.println("<td style='color: black'>");
										out.println(element.getSiteWiseIndentNo());
										out.println("</td>"); */
										
										out.println("<td>");
										out.println(element.getToEmpName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getStrScheduleDate());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getSiteName());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getPending_Emp_Name());
										out.println("</td>");
										
										out.println("<td>");
										out.println(element.getOld_Po_Number());
										out.println("</td>");
									
										out.println("<td>");
										out.println(element.getType_Of_Purchase());
										out.println("</td>");
									
										%>
					
						</tr>
					  <%}  %>
				  <tbody>
				</table>
				</div>
				</div>
           <%
				   }
           %>
           <!-- 	=============================================show marketing types of pos end================================================================ -->
			
		
			
			
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
		$(document).ready( function() {
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

		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
