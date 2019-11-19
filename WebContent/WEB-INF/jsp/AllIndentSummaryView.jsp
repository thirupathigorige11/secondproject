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
<link href="css/custom.min.css" rel="stylesheet">
<jsp:include page="CacheClear.jsp" />  
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

<style>
table.dataTable {border-collapse:collapse !important;}
table.table-bordered.dataTable th{border-left-width:0 !important;border-bottom:0 !important;}
.table>tbody+tbody{border:1px solid #000 !important;}
.table-bordered>tfoot>tr>td{border:1px solid #000 !important;}
.label-select{font-size:16px;margin-right:10px;}
#site{height:34px;margin-right:15px;}
@media only screen and (min-width:320px)and (max-width:767px){#site{height:34px;margin-right:15px;margin-bottom:10px;}}

</style>


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
						<li class="breadcrumb-item"><a href="#">History</a></li>
						<li class="breadcrumb-item active">All Sites Summary</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="z-index:999;">
						<div class="lds-facebook"> 
						<div></div> <div></div><div></div>	<div></div>	<div></div>	<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>    
				<div class="col-md-12">
				<div class="container border-inwards-box">
				<form:form action="getAllSiteProductDetails.spring"  name="allSummaryDetails" class="form-inline">
					<div class="form-group">
						<label class="control-label label-select" style="">Select Site:</label>
							 <select name="site" id="site" class="form-control">
									<option value="">--Select--</option>
									<option value="ALL">ALL</option>
									<c:forEach items="${siteDetails}" var="siteDetails">
										<option value="${siteDetails.key}">${siteDetails.value}</option>
									</c:forEach>
							</select>
					</div>
					  <button type="submit" value="Submit" onclick="return myFunction()" class="btn btn-warning">Submit</button>
				</form:form>
				</div>
				</div>
				
				
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div>
				
				
				
				<div class="col-md-12">
					<table id="tblnotification" class="table table-new" cellspacing="0">
						<thead>

							<tr class="">
								<th>Site Name</th>
								<th>Product Name</th>
								<th>SubProduct Name</th>
								<th>ChildProduct Name</th>
								<th>Measurement Name</th>
								<th>Product Qty</th>
								<!-- <th>Basic Amount</th> -->
								<th>Unit Per Price</th>
								<th>Total Amount</th>

							</tr>

						</thead>
						<tbody>


							<c:forEach items="${list}" var="indent" >
								<tr>
									<td>${indent.site_Id}</td>
									<td>${indent.productName}</td>
									<td>${indent.sub_ProductName}</td>
									<td>${indent.child_ProductName}</td>
									<td>${indent.measurementName}</td>
									<td class="valor3 text-right">${indent.quantity}</td>
									<%-- <td>${indent.basicAmt}</td> --%>
									<td>${indent.pricePerUnit}</td>
									<td>${indent.totalAmount}</td>
								</tr>
							</c:forEach>
						</tbody>
						
						
			<tbody>
        <tr class="info">
            <td colspan="5" class="text-right subTtl" style="border-right-width: 0px !important;border-left-width: 0px !important;border-bottom:0px !important;">TOTAL Quantity:</td>
            <td class="quantity text-right subTtl" style="border-right-width: 0px !important;border-bottom:0px !important;"></td>
            <td class=" text-right subTtl" style="border-right-width: 0px !important;border-bottom:0px !important;"></td>
            <td class=" text-right subTtl" style="border-right-width: 0px !important;border-bottom:0px !important;"></td>
        </tr>
   </tbody>
   </table>
				</div>
			<%-- <div class="elements"><span class="h4" style="margin-top:20px;"><strong>Grand Total Amount :</strong></span><span name="grandTotalAmt" style="color: #ef7e2d;" value="${requestScope['grandTotalAmount']}" readonly /></span></div> --%>
						
					</div>
		
		
		
		
		
 	 	<div class="col-md-12 text-right Mrgtop20"> <strong>Grand Total Amount :</strong> <span style="color:#ffa500;font-weight:700;">${requestScope['grandTotalAmount']}</span>
					</div>
				</div>
           <%
				   }

           %>
				<!-- /page content -->
			</div>

		</div>


	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>

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
		

				});
		$('#tblnotification').DataTable({
			   "aLengthMenu": [[10,25, 50, 75, -1], [10,25, 50, 75, "All"]],
		        "iDisplayLength": 10
		    }
	);
		$(window).load(function() {
			$('.loader-sumadhura').hide()
         });
		function myFunction() {
			var submitVal = document.getElementById('site').value;
			if (submitVal == '' || submitVal == null) {
				alert("Please Select Site!");
				return false;
			}
			$('.loader-sumadhura').show();
		}
	/* 	$('#tblnotification').stacktable(); */
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	
		$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 $(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
		    	$(".quantity").text(tquantity.toFixed(2));
		    });
		    $(".subTtl").show();	
		 	$(document).on("keyup", ".input-sm",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		 	$(document).on("click", " .pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		 	$(document).on("change", " .dataTables_length",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
		   
		});
		});
		
	
   

	
	
	</script>

</body>
</html>
