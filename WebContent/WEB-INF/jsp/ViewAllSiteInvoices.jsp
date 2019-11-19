
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
 <jsp:include page="CacheClear.jsp" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
.input-group-addon{border:1px solid #ccc !important;}
 .anchor-class{color:#0000ff;text-decoration:underline;}
/* .st-table thead tr th, .table-viewmypending thead tr th { background-color: #ddd; border: 0px solid #000 !important;  border-right: 1px solid #000 !important;} */
.success, .error {text-align: center;font-size: 16px;}
table.dataTable {border-collapse:collapse !important;}
</style>

<script type="text/javascript">


	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var selesite = document.getElementById("siteId").value;

		if (from == "" && to == "" && selesite == "") {
			alert("Please select From Date or To Date !");
			return false;
		}
		else if (from != "" && to != "" && selesite == "") {
			alert("Please select site !");
			return false;
		}
		else if (from == "" && to != "" && selesite == ""||from != "" && to == ""  && selesite == "") {
			alert("Please select site !");
			return false;
		}
		$(".loader-sumadhura").show();
		
	}
</script>
 <!--   <script type="text/javascript">

           onload=function(){
             debugger;
        	   var e=document.getElementById("refreshed");
               if(e.value=="no")e.value="yes";
               else{e.value="no";location.reload();}
           }

    </script> -->
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
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Site Wise Invoice Details</li>
					</ol>
				</div>
                <div class="loader-sumadhura" style="z-index:99;display:none;">
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
               
               
				<div class="col-md-12">
					<div class="container border-inwards-box">
						<label class="success"><c:out
								value="${requestScope['succMessage']}"></c:out> </label>
						<%-- 		<form:form  id="indentIssueFormId" action="getIndentReciveViewDts.spring">
											<td><strong>From Date :</strong> <input id="ReqDateId1" type="text" name="fromDate" value="${fromDate}" autocomplete="off">
											</td>
											<td><strong>To Date :</strong> <input id="ReqDateId2" type="text" name="toDate" value="${toDate}" autocomplete="off">
											</td>
											<td>
												
											</td> <br/><br/><br/>
				<input type="submit" value="Submit" id="saveBtnId" class="btn btn-warning" style="width: 136px" onclick="return validate();">
				<div style="margin-top: 36px">${displayErrMsg}</div>
				</form:form>
				 --%>
				<!-- changing the form action based on the condition by checking module type and operation -->
					<c:if test="${moduleName eq 'allSiteInvoiceDetails' }">
						<form class="form-horizontal" action="getAllSiteReciveInvoiceDts.spring" method="GET">
					</c:if>
					<c:if test="${moduleName eq 'allSiteMaterialAdjustmentInvoice' }">
						<form class="form-horizontal" action="getSiteWiseMaterialAdjDtls.spring" method="GET">
					</c:if>
						
							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="col-md-4 col-sm-6 col-xs-12 Mrg-bottom">
									<div class="form-group">
										<label for="date" class="col-md-5 col-sm-5 col-xs-12 ">From Date :</label>
										<div class="col-md-6 col-sm-7 no-padding-left-inwards input-group">
											<input type="text" id="ReqDateId1" class="form-control readonly-color"
												name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
												<label class="input-group-addon btn" for="ReqDateId1">	<span class="fa fa-calendar"></span></label>
										</div>
									</div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12 Mrg-bottom">
									<div class="form-group">
										<label for="todate" class="col-md-5 col-sm-5 col-xs-12 ">To Date :</label>
										<div class="col-md-6 col-sm-7 col-xs-12 no-padding-left-inwards input-group">
											<input type="text" id="ReqDateId2" class="form-control readonly-color"
												name="toDate" value="${toDate}" autocomplete="off" readonly="true">
												<label class="input-group-addon btn" for="ReqDateId2"><span class="fa fa-calendar"></span></label>
										</div>
									</div>
								</div>
                               <div class="clearfix visible-sm"></div>
								<div class="col-md-4 col-sm-6 col-xs-12">
									<div class="form-group">
										<label for="site" class="col-md-5 col-sm-5 col-xs-12 ">Site :</label>
										<div class="col-md-6 col-sm-7 col-xs-12 no-padding-left-inwards">
											<select id="siteId" name="siteId" class="form-control">
												<option value="">--Select--</option>
												
											<option value="All" <c:if test="${strSiteId eq 'All' }">selected="selected"</c:if>>All</option>
												<%
													Map<String, String> getSite = (Map<String, String>) request.getAttribute("getSiteList");
													String selectedSiteId=request.getAttribute("strSiteId")==null?"":request.getAttribute("strSiteId").toString();
													
													for (Map.Entry<String, String> site : getSite.entrySet()) {
														if(selectedSiteId.equals(site.getKey())){
													%>
																<option value="<%=selectedSiteId%>" selected="selected"><%=site.getValue()%></option>
													<%}else{
															
													%>
													<option value="<%=site.getKey()%>"><%=site.getValue()%></option>
													<%
														}}
													%>
											</select>
										</div>
									</div>
								</div>
							</div>



							<div class="col-md-12 text-center center-block">
								<button type="submit" value="Submit" id="saveBtnId"
									class="btn btn-warning btn-submit-inwards"
									onclick="return validate();">Submit</button>

							</div>
							<div style="color: red; margin-bottom: 2px; padding: 31px;">${displayErrMsg}</div>


						</form>


					</div>
                  
					<%
						String isShowGrid = request.getAttribute("showGrid") == null
								? ""
								: request.getAttribute("showGrid").toString();
						if (isShowGrid.equals("true")) {
					%>

					<div class="table-responsive">						
						<table id="tblnotification" class="table table-new" cellspacing="0">
							<thead>
								<tr>
									<th>S NO</th>
		                            <th>PO No</th>
									<th>PO Date</th>
									<th>Invoice No</th>
									<th>Invoice Date</th>
									<th>Vendor Name</th>
									<th>Total Amount</th>
									<th>Received Date</th>		
		    						<th>Received Time</th>	
    					    </tr>
						   </thead>
						<tbody>

								<%
									int count = 0;
								%>

								<c:forEach items="${indentIssueData}" var="element">
									<tr>
										<td><%=++count%></td>
										<td><a href="getPoDetailsList.spring?poNumber=${element.poNo}&siteId=${element.siteId}&imageClick=true" style="text-decoration: underline;color: blue;">${element.poNo}</a></td>
										<td>${element.poDate}</td>
										<td><a href="getInvoiceDetails.spring?invoiceNumber=${element.strNumber}&vendorName=${element.vendorName}&vendorId=${element.vendorId}&IndentEntryId=${element.indentEntryId}&SiteId=${element.siteId}&invoiceDate=${element.strInvoiceDate}&indentType=${indentType}&url=${url}"class="anchor-class" >${element.requesterId}</a></td>
										<td>${element.strInvoiceDate}</td>
										<td>${element.vendorName}</td>
										<td class="valor2 text-right" id="">${element.strTotalAmount}</td>
										<td>${element.receivedDate}</td>
										<td>${element.time}</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
			        	<tr class="info">
			            <td colspan="6" class="text-right subTtl">SUB TOTAL:</td>
			            <td class="total text-right subTtl"></td>
			            <td></td>
			            <td></td>
			        </tr>
			      </tfoot>
						</table>
					</div>
					<%
						}
					%>
					<!-- /page content -->
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
	<script src="js/sidebar.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script>
	$(".anchor-class").click(function(ev){
		if(ev.ctrlKey==false && ev.shiftKey==false){
			$(".loader-sumadhura").show();
		}
	});
		$(function() {
			$("#ReqDateId1").datepicker({
				dateFormat : 'dd-M-y',
				maxDate : new Date(),
				changeMonth : true,
				changeYear : true,
				onSelect: function(selected) {
	     	        $("#ReqDateId2").datepicker("option","minDate", selected)
	     	        }
			});
			$("#ReqDateId2").datepicker({
				dateFormat : 'dd-M-y',
				maxDate : new Date(),
				changeMonth : true,
				changeYear : true,
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
				});

		
		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
/* *************** SUB Total calculations ********* */
		
		$(document).ready(function () {
			var val = $('#tblnotification').find('tbody').find('tr');
			var tAmount = 0;
			jQuery.each(val,function(index,item){
				tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
			});
			var numberwithcomma=inrFormat(tAmount.toFixed(2));
			$(".total").text(numberwithcomma);
			
			$(document).on("click", " .pagination",function(){
				  var val = $('#tblnotification').find('tbody').find('tr');
				  var tAmount = 0;
				  jQuery.each(val,function(index,item){
				    tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				  });
			
				  var numberwithcomma=inrFormat(tAmount.toFixed(2));
					$(".total").text(numberwithcomma);
			});
			
			$(document).on("keyup", ".input-sm",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;				
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
			});
			
			$(document).on("change", " .dataTables_length",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;				
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
			});

      });
		//converting number with comma
		function inrFormat(nStr) { // nStr is the input string
		     nStr += '';
		     x = nStr.split('.');
		     x1 = x[0];
		     x2 = x.length > 1 ? '.' + x[1] : '';
		     var rgx = /(\d+)(\d{3})/;
		     var z = 0;
		     var len = String(x1).length;
		     var num = parseInt((len/2)-1);
		 
		      while (rgx.test(x1)){
		        if(z > 0){
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		        }
		        else{
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		          rgx = /(\d+)(\d{2})/;
		        }
		        z++;
		        num--;
		        if(num == 0){
		          break;
		        }
		      }
		     return x1 + x2;
		 } 
	 //checking from date and to date

	</script>

</body>
</html>
