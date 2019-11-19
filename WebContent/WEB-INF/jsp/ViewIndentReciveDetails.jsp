<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="func"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

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
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.loader-sumadhuraforsubmit{ position: absolute;left:58%;top:50%;}
    @media only screen and (min-width:320px) and (max-width:767px){.loader-sumadhuraforsubmit {position: absolute;left: 40%;top: 75%;}}

.form-control{border:1px solid #000 !important;height:34px !important;}
.selectAllchk .selectAllchkinput{height:22px;width:22px;vertical-align: bottom;}
.selectAllchk span{font-size:16px;font-weight:bold;}
.table>tbody+tbody {border-top: 1px solid #000;}
table.dataTable {border-collapse:collapse !important;}
.success, .error {text-align: center;font-size: 14px;}
</style>

<script type="text/javascript">
	function validate() {		
	   debugger;
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		if (from == "" && to == "" ) {
			alert("Please select From Date or To Date !");
			return false;
		}
		$(".loader-sumadhura").show();
		var siteId = document.getElementById("dropdown_SiteId").value;
		if (siteId == "" || siteId == null) {
			alert("Please select the Site !");
			//$(".loader-sumadhura").hide();
			return false;
		}
		//$(".loader-sumadhura").show();

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
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Get Site Wise Receive Details</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="display: none;z-index:999;">
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
					<div class="loader-sumadhuraforsubmit" style="z-index:999;">
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

				<div class="">
					<div class="container border-inwards-box">
						<label class="success"><c:out
								value="${requestScope['succMessage']}"></c:out> </label>


						<form class="form-horizontal"
							action="getIndentReciveViewDts.spring">
							<div class="col-md-12">
								<div class="col-md-4">
									<div class="form-group">
										<label for="date" class="col-md-6 marginTop5">Received From
											Date :</label>
										<div class="col-md-6 input-group">
											<input type="text" class="form-control col-md-6 readonly-color"
												id="ReqDateId1" name="fromDate" value="${fromDate}"
												autocomplete="off" readonly>
												<label class="input-group-addon btn ReqDateId1" for="fromDate">
												       <span class="fa fa-calendar"></span>
												    </label>
												
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label for="todate" class="col-md-6 marginTop5">Received To
											Date :</label>
										<div class="col-md-6 input-group">
											<input type="text" class="form-control col-md-6 readonly-color"
												id="ReqDateId2" name="toDate" value="${toDate}"
												autocomplete="off" readonly>
												<label class="input-group-addon btn ReqDateId2" for="toDate">
												       <span class="fa fa-calendar"></span>
												    </label>
												
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<%
											String strSearchType = request.getAttribute("SEARCHTYPE") == null
													? ""
													: request.getAttribute("SEARCHTYPE").toString();
											log("reposrtd/cumulative Stock");
											if (strSearchType.equals("ADMIN")) {
										%>
										<!-- <div class="ui-widget"> -->
										<label class="col-md-6 marginTop5">Site :</label>
										<!-- commlbl  -->
										<!-- </div> -->
										<div class="text-left col-md-6">
											<%
												List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request
															.getAttribute("allSitesList");
													String strSiteId = "";
													String strSiteName = "";
											%>
											<select id="dropdown_SiteId" name="dropdown_SiteId"
												class="custom-combobox form-control indentavailselect">
												<option value=""></option>

												<%
													for (Map siteList : totalSiteList) {
															strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
															strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
												%>

												<option value="<%=strSiteId%>"><%=strSiteName%></option>
												<%
													}
												%>

											</select>
											<%
												}
											%>

										</div>
									</div>






								</div>
								<div class="col-md-12 text-center center-block Mrgtop10">
									<button type="submit" value="Submit" id="saveBtnId"
										class="btn btn-warning btn-sub-inwards"
										onclick="return validate();">Submit</button>
									<div>${displayErrMsg}</div>
								</div>
								</div>
						</form>




					
				</div>
				<div class="clearfix"></div>
				<div class="col-md-12">
					<%
						String isShowGrid = request.getAttribute("showGrid") == null
								? ""
								: request.getAttribute("showGrid").toString();
						if (isShowGrid.equals("true")) {
					%>
					<div class="table-responsive ">
						<table id="tblnotification" class="table table-new" cellspacing="0">
							<thead>
								<tr>

									<th>Invoice Number/DC Number</th>
									<th>PO Number</th>
									<th>Received Date</th>
									<th>Vendor Name</th>
									<th>Receiver Name</th>


									<th>Product Name</th>
									<th>Sub Product Name</th>
									<th>Child Product Name</th>

									<th>Measurement</th>

									<th>Received Quantity</th>
									<th>Basic Amount</th>
									<th>Tax Amount</th>
									<th>Other Charges</th>
									<th>Total Amount</th>
									<th>Invoice Total Amount</th>
									<th>Expiry Date</th>

								</tr>

							</thead>
							<tbody>
								<c:forEach items="${indentIssueData}" var="element">
									<tr>

										<%-- <td>${element.requesterId}</td> --%>
										<td>
											<c:if test="${not empty element.invoiceNumber}">
												
												<c:if test="${element.indentType eq 'INU'}">
														${element.invoiceNumber}
												</c:if>
												<c:if test="${element.indentType ne 'INU'}">
													<a href="getGrnDetails.spring?invoiceNumber=${element.invNoForHyperLink}&vendorId=${element.vendorId}&invoiceDate=${element.strInvoiceDate}&indentEntryId=${element.indentEntryId}&siteId=${element.siteId}&showHomebtn=false&indentType=${element.indentType}" target="_blank" style="text-decoration: underline;color: blue;">${element.invoiceNumber}</a>
												</c:if>
											</c:if>
											
											<c:if test="${not empty element.dcNumber}">
												<c:if test="${not empty element.invoiceNumber}"> / </c:if>
											<a href="getDcFormGrnViewDts.spring?invoiceNumber=${element.dcNoForHyperLink}&vendorId=${element.vendorId}&dcDate=${element.strDcDate}&dcEntryId=${element.dcEntryId}&SiteId=${element.siteId}&type=dcGrnForReceiveDtls" target="_blank"  style="text-decoration: underline;color: blue;">${element.dcNumber}</a>
											</c:if>
										</td>
										
										<td>
											<c:if test="${not empty element.poNo}">
											<a href="getPoDetailsList.spring?poNumber=${element.poNo}&siteId=${element.siteId}" target="_blank"  style="text-decoration: underline;color: blue;">${element.poNo}</a>
											</c:if>
										</td>
					
										<td>${element.receivedDate}</td>
										<td>${element.vendorName}</td>
										<td>${element.userId}</td>


										<td>${element.productName}</td>
										<td>${element.subProdName}</td>
										<td>${element.childProdName}</td>
										<td>${element.requesterName}</td>
										<td class="valor2">${element.issueQTY}</td>
										<td class="basicAmt">${element.basicAmount}</td>
										<td class="taxAmt">${element.taxAmount}</td>
										<td class="otcAmt">${element.otherCharges}</td>
										<td class="tatlAmt">${element.totalAmt}</td>
										<td class="invtlAmt">${element.strTotalAmount}</td>
										<td>${element.expiryDate}</td>
									</tr>
								</c:forEach>
						</tbody>
							<tr class="info">
								<td colspan="9" class="text-right subTtl" >SUB TOTAL:</td>
								<td class="total text-right subTtl"></td>
								<td class="batotal  text-right basubTtl"></td>
								<td class="taxtotal  text-right taxsubTtl"></td>
								<td class="otctotal  text-right otcsubTtl"></td>
								<td class="tatltotal  text-right tatlsubTtl"></td>
								<!-- <td class="invtltotal  text-right invtlsubTtl"></td> -->	<!-- commented because same invoice total adding multiple times -->
								<td></td>
								<td class="text-right invtlsubTtl"></td>
							</tr>
							<!-- </tbody> -->
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
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script>
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
	
	
		$(function() {
			$("#ReqDateId1").datepicker({
				dateFormat : 'dd-M-y',
				maxDate : new Date(),
				changeMonth: true,
			      changeYear: true,
			      onSelect: function(selected) {
		     	        $("#ReqDateId2").datepicker("option","minDate", selected)
		     	        }

			});
			$(".ReqDateId1").click(function(){
				$("#ReqDateId1").focus();	
			
				});
			
			$("#ReqDateId2").datepicker({
				dateFormat : 'dd-M-y',
				maxDate : new Date(),
				changeMonth: true,
			      changeYear: true,
			      onSelect: function(selected) {
		            	$("#ReqDateId1").datepicker("option","maxDate", selected)
		            	        }

			});
			$(".ReqDateId2").click(function(){
				$("#ReqDateId2").focus();	
			
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

		/* $(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		}); */
		$('#tblnotification').stacktable({
			myClass : 'stacktable small-only'
		});

		/* *************** calculations for filter data***********************	 */
		$(document).ready(function () {
			debugger;
			$(".subTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 //$(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
		    });
		 
			$(".total").text(inrFormat(tAmount.toFixed(2)));
			
			
			$(".info td").show();
			//$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 //$(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    //$(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 //$(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		   
		});
		/* *************** calculations for filter data***********************	 */
		/* *************** calculations for Basic Amount***********************	 */
		 $(document).ready(function () {
			 $(".basubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var batAmount = 0;
				 //$(".batotal").text(0);
			    jQuery.each(val,function(index,item){
			    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".batotal").text(inrFormat(batAmount.toFixed(2)));
			 
			 
		 
			//$(".batotal").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var batAmount = 0;
			 //$(".batotal").text(0);
		    jQuery.each(val,function(index,item){
		    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') ));
		    });
		   // $(".basubTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".basubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var batAmount = 0;
				 //$(".batotal").text(0);
			    jQuery.each(val,function(index,item){
			    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".batotal").text(inrFormat(batAmount.toFixed(2)));
			});
		 	$(document).on("change", ".input-sm",function(){
		 		debugger;
		 		datatableSelectChange();
		 		$(".basubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var batAmount = 0;
				 //$(".batotal").text(0);
			    jQuery.each(val,function(index,item){
			    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".batotal").text(inrFormat(batAmount.toFixed(2)));
			});
		   
		});
		
		/*  * *************** Paginataion Total********* */
			
			$(document).ready(function () {
		//$(".batotal").text(0);
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var tAmount = 0;
		 //$(".batotal").text(0);
	    jQuery.each(val,function(index,item){
	    	tAmount = tAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') ));
	    });
	    //$(".basubTtl").hide();	
	 	$(document).on("click", " .pagination",function(){
	 		$(".basubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var batAmount = 0;
			 //$(".batotal").text(0);
		    jQuery.each(val,function(index,item){
		    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".batotal").text(inrFormat(batAmount.toFixed(2)));
		});
	   
	});
		/* *************** calculations for Basic Amount***********************	 */
	/* *************** calculations for Tax Amount***********************	 */
	$(document).ready(function () {
		$(".taxsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var taxtAmount = 0;
		 //$(".taxtotal").text(0);
	    jQuery.each(val,function(index,item){
	    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".taxtotal").text(inrFormat(taxtAmount.toFixed(2)));	
	 	
	
			//$(".taxtotal").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var taxtAmount = 0;
			 //$(".taxtotal").text(0);
		    jQuery.each(val,function(index,item){
		    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') ));
		    });
		    //$(".taxsubTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".taxsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var taxtAmount = 0;
				 //$(".taxtotal").text(0);
			    jQuery.each(val,function(index,item){
			    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".taxtotal").text(inrFormat(taxtAmount.toFixed(2)));
			});
		 	$(document).on("change", ".input-sm",function(){
		 		$(".taxsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var taxtAmount = 0;
				 //$(".taxtotal").text(0);
			    jQuery.each(val,function(index,item){
			    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".taxtotal").text(inrFormat(taxtAmount.toFixed(2)));
			});
		   
		});
		
		/*  * *************** Paginataion Total********* */
			
			$(document).ready(function () {
		//$(".taxtotal").text(0);
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var taxtAmount = 0;
		 //$(".taxtotal").text(0);
	    jQuery.each(val,function(index,item){
	    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') ));
	    });
	   // $(".taxsubTtl").hide();	
	 	$(document).on("click", " .pagination",function(){
	 		$(".taxsubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var taxtAmount = 0;
			 //$(".taxtotal").text(0);
		    jQuery.each(val,function(index,item){
		    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".taxtotal").text(inrFormat(taxtAmount.toFixed(2)));
		});
	   
	});
	/* *************** calculations for Tax Amount***********************	 */
	/* *************** calculations for Other Charges***********************	 */
	$(document).ready(function () {
		$(".otcsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var otctAmount = 0;
		 //$(".otctotal").text(0);
	    jQuery.each(val,function(index,item){
	    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));	
		
		
			//$(".otctotal").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var otctAmount = 0;
			 //$(".otctotal").text(0);
		    jQuery.each(val,function(index,item){
		    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') ));
		    });
		    //$(".otcsubTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".otcsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var otctAmount = 0;
				 //$(".otctotal").text(0);
			    jQuery.each(val,function(index,item){
			    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));
			});
		 	$(document).on("change", ".input-sm",function(){
		 		$(".otcsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var otctAmount = 0;
				 //$(".otctotal").text(0);
			    jQuery.each(val,function(index,item){
			    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));
			});
		   
		});
		
		/*  * *************** Paginataion Total********* */
			
			$(document).ready(function () {
		//$(".otctotal").text(0);
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var otctAmount = 0;
		 //$(".otctotal").text(0);
	    jQuery.each(val,function(index,item){
	    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') ));
	    });
	    //$(".otcsubTtl").hide();	
	 	$(document).on("click", " .pagination",function(){
	 		$(".otcsubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var otctAmount = 0;
			 //$(".otctotal").text(0);
		    jQuery.each(val,function(index,item){
		    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));
		});
	   
	});
	/* *************** calculations for Other Charges ***********************	 */
	/* *************** calculations for Total Amount Beside Charges column***********************	 */
	$(document).ready(function () {
		debugger;
	$(".tatlsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var tatltAmount = 0;
		 //$(".tatltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".tatltotal").text(inrFormat(tatltAmount.toFixed(2)));	
	
			//$(".tatltotal").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tatltAmount = 0;
			 //$(".tatltotal").text(0);
		    jQuery.each(val,function(index,item){
		    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') ));
		    });
		    //$(".tatlsubTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".tatlsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tatltAmount = 0;
				 //$(".tatltotal").text(0);
			    jQuery.each(val,function(index,item){
			    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".tatltotal").text(inrFormat(tatltAmount.toFixed(2)));
			});
		 	$(document).on("change", ".input-sm",function(){
		 		$(".tatlsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tatltAmount = 0;
				 //$(".tatltotal").text(0);
			    jQuery.each(val,function(index,item){
			    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".tatltotal").text(inrFormat(tatltAmount.toFixed(2)));
			});
		   
		});
		
		/*  * *************** Paginataion Total********* */
			
			$(document).ready(function () {
		//$(".tatltotal").text(0);
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var tatltAmount = 0;
		 //$(".tatltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') ));
	    });
	    //$(".tatlsubTtl").hide();	
	 	$(document).on("click", " .pagination",function(){
	 		$(".tatlsubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var tatltAmount = 0;
			 //$(".tatltotal").text(0);
		    jQuery.each(val,function(index,item){
		    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".tatltotal").text(inrFormat(tatltAmount.toFixed(2)));
		});
	 	
	   
	});
	/* *************** calculations for Total Amount Beside Charges column ***********************	 */
	/* *************** calculations for Invoice Total Amount***********************	 */
	$(document).ready(function () {
		$(".invtlsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var invtltAmount = 0;
		 //$(".invtltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".invtltotal").text(invtltAmount.toFixed(2));	
				//$(".invtltotal").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var invtltAmount = 0;
			 //$(".invtltotal").text(0);
		    jQuery.each(val,function(index,item){
		    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') ));
		    });
		    //$(".invtlsubTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".invtlsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var invtltAmount = 0;
				 //$(".invtltotal").text(0);
			    jQuery.each(val,function(index,item){
			    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".invtltotal").text(inrFormat(invtltAmount.toFixed(2)));
			});
		 	$(document).on("change", "#tblnotification_length .input-sm",function(){
		 		$(".invtlsubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var invtltAmount = 0;
				 //$(".invtltotal").text(0);
			    jQuery.each(val,function(index,item){
			    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".invtltotal").text(inrFormat(invtltAmount.toFixed(2)));
			});
		   
		});
		
		/*  * *************** Paginataion Total********* */
			
			$(document).ready(function () {
		//$(".invtltotal").text(0);
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var invtltAmount = 0;
		 //$(".invtltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') ));
	    });
	   // $(".invtlsubTtl").hide();	
	 	$(document).on("click", " .pagination",function(){
	 		$(".invtlsubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var invtltAmount = 0;
			 //$(".invtltotal").text(0);
		    jQuery.each(val,function(index,item){
		    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".invtltotal").text(inrFormat(invtltAmount.toFixed(2)));
		});
	   
	});
	/* *************** calculations for Invoice Total Amount ***********************	 */
		
		/* *************** Paginataion Total********* */
		
				$(document).ready(function () {
			//$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 //$(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    //$(".subTtl").hide();	
		   
		 	$(document).on("click", " .pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 //$(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
				
			});
		   
		});
	
				/* *************** Paginataion Quantity Total********* */
				
						$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 //$(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		    //$(".subTtl").hide();	
		 	$(document).on("click", ".pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 //$(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(inrFormat(tquantity.toFixed(2)));
			});
		   
		});
						/* *************** Show Total********* */
						
						$(document).ready(function () {
					//$(".total").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tAmount = 0;
					 //$(".total").text(0);
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
				    });
				   // $(".subTtl").hide();	
				   $("input[name='tblnotification_length]'").change(function() {
					   debugger;
						$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tAmount = 0;
						 //$(".total").text(0);
					    jQuery.each(val,function(index,item){
					    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					    });
					 
						$(".total").text(inrFormat(tAmount.toFixed(2)));
				   });
				 	/* $(document).on("change", "input[name='tblnotification_length]' ",function(){
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tAmount = 0;
						 //$(".total").text(0);
					    jQuery.each(val,function(index,item){
					    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					    });
					 
						$(".total").text(tAmount.toFixed(2));
					}); */
				   
				});

						/* *************** show Sub Total********* */
						
								$(document).ready(function () {debugger;
					$(".quantity").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tquantity = 0;
					 //$(".quantity").text(0);
				    jQuery.each(val,function(index,item){
				    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
				    });
				   // $(".subTtl").hide();	
				 	$(document).on("change", ".dataTables_length",function(){debugger;
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tquantity = 0;
						 //$(".quantity").text(0);
					    jQuery.each(val,function(index,item){
					    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
					    });
					 
						$(".quantity").text(inrFormat(tquantity.toFixed(2)));
					});
				   
				});
		
	
		   
		
		$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 //$(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		   // $(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 //$(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(inrFormat(tquantity.toFixed(2)));
			});
		   
		});
		
	</script>
	<script>
	 function datatableSelectChange(){
		 
		//basic amount calucalation
			 $(".basubTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var batAmount = 0;
				 //$(".batotal").text(0);
			    jQuery.each(val,function(index,item){
			    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
			    });
			 
				$(".batotal").text(batAmount.toFixed(2));
				//basic amount calucalation
				//total calucalation
				 $(".subTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 //$(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
		    });
		 
			$(".total").text(inrFormat(tAmount.toFixed(2)));
		
				//total calucalation
				//tax amount calucalation
				 $(".taxsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var taxtAmount = 0;
		 //$(".taxtotal").text(0);
	    jQuery.each(val,function(index,item){
	    	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".taxtotal").text(taxtAmount.toFixed(2));	
				//tax amount calucalation
				//other charges calucalation 
				 $(".otcsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var otctAmount = 0;
		 //$(".otctotal").text(0);
	    jQuery.each(val,function(index,item){
	    	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));	
				//other charges calucalation
				//total amount calucaltion
				 $(".tatlsubTtl").show();
 		$(".text-right").show();
 		 var val = $('#tblnotification').find('tbody').find('tr');
		 var tatltAmount = 0;
		 //$(".tatltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".tatltotal").text(tatltAmount.toFixed(2));	
				//total amount calucaltion
				//invoice total amount calucalation
			$(".invtlsubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var invtltAmount = 0;
			 //$(".invtltotal").text(0);
		    jQuery.each(val,function(index,item){
		    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".invtltotal").text(inrFormat(invtltAmount.toFixed(2)));
		//invoice total amount calucalation
	 }
	
	</script>
	<script>
	$(".tblheaderall").click(function(){
		//basic amount calucalation
		 $(".basubTtl").show();
	 		$(".text-right").show();
	 		 var val = $('#tblnotification').find('tbody').find('tr');
			 var batAmount = 0;
			 //$(".batotal").text(0);
		    jQuery.each(val,function(index,item){
		    	batAmount = batAmount + (parseFloat(jQuery(item).find('.basicAmt').text().replace(/,/g,'') || 0));
		    });
		 
			$(".batotal").text(inrFormat(batAmount.toFixed(2)));
			//basic amount calucalation
			//total calucalation
			 $(".subTtl").show();
		$(".text-right").show();
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var tAmount = 0;
		 //$(".total").text(0);
	    jQuery.each(val,function(index,item){
	    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
	    });
	 
		$(".total").text(inrFormat(tAmount.toFixed(2)));
	
			//total calucalation
			//tax amount calucalation
			 $(".taxsubTtl").show();
	$(".text-right").show();
	 var val = $('#tblnotification').find('tbody').find('tr');
	 var taxtAmount = 0;
	 //$(".taxtotal").text(0);
   jQuery.each(val,function(index,item){
   	taxtAmount = taxtAmount + (parseFloat(jQuery(item).find('.taxAmt').text().replace(/,/g,'') || 0));
   });

	$(".taxtotal").text(taxtAmount.toFixed(2));	
			//tax amount calucalation
			//other charges calucalation 
			 $(".otcsubTtl").show();
	$(".text-right").show();
	 var val = $('#tblnotification').find('tbody').find('tr');
	 var otctAmount = 0;
	 //$(".otctotal").text(0);
   jQuery.each(val,function(index,item){
   	otctAmount = otctAmount + (parseFloat(jQuery(item).find('.otcAmt').text().replace(/,/g,'') || 0));
   });

	$(".otctotal").text(inrFormat(otctAmount.toFixed(2)));	
			//other charges calucalation
			//total amount calucaltion
			 $(".tatlsubTtl").show();
	$(".text-right").show();
	 var val = $('#tblnotification').find('tbody').find('tr');
	 var tatltAmount = 0;
	 //$(".tatltotal").text(0);
   jQuery.each(val,function(index,item){
   	tatltAmount = tatltAmount + (parseFloat(jQuery(item).find('.tatlAmt').text().replace(/,/g,'') || 0));
   });

	$(".tatltotal").text(inrFormat(tatltAmount.toFixed(2)));	
			//total amount calucaltion
			//invoice total amount calucalation
		$(".invtlsubTtl").show();
		$(".text-right").show();
		 var val = $('#tblnotification').find('tbody').find('tr');
		 var invtltAmount = 0;
		 //$(".invtltotal").text(0);
	    jQuery.each(val,function(index,item){
	    	invtltAmount = invtltAmount + (parseFloat(jQuery(item).find('.invtlAmt').text().replace(/,/g,'') || 0));
	    });
	 
		$(".invtltotal").text(inrFormat(invtltAmount.toFixed(2)));
	//invoice total amount calucalation
	});
	
	//to distroy loader
	 $(window).load(function () {
		 $(".loader-sumadhuraforsubmit").hide();
	 });	
	
	</script>
</body>
</html>
