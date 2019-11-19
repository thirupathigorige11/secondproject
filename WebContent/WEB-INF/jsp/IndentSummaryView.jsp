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
<jsp:include page="CacheClear.jsp" />  
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura_IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
table.dataTable{border-collapse: collapse !important;}
.breadcrumb {background: #f5f5f5;}
.info td{border:1px solid #000 !important;}
/* .popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}
 .elements {
        padding: 10px;
        max-width:290px;
        margin-left:  682px;
	    color: #ef7e2d;
	    font-size: 12px;
	    font-weight: bold;
    } */
/* The actual popup */
/* .popup .popuptext {
	visibility: hidden;
	width: 160px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 8px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -80px;
} */

/* Popup arrow */
/* .popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}
 */
/* Toggle this class - hide and show the popup */
/* .popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
} */

/* Add animation (fade in the popup) */
/* @
-webkit-keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}

}
@
keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
} */
</style>

<script src="js/scrolltotop.js"></script>
<script>
	function myFunction() {
		/* alert(123); */
		var popup = document.getElementById("myPopup");
		
		alert(popup);
		popup.classList.toggle("show");
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
						<li class="breadcrumb-item"><a href="#">History</a></li>
						<li class="breadcrumb-item active">Stock Availability</li>
					</ol>
				</div>

				<div class="">
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr>
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Measurement Name</th>
								<th>Purchased Qty</th>
								<th>Issued Qty</th>
								<th>Available Product Qty</th>
								<!-- <th>Basic Amount</th> -->
								<th>Price Per Unit</th>
								<th>Total Amount</th>
								<th>Expiry Date</th>
						   </tr>
						</thead>
						<tbody>
						     <c:forEach items="${list}" var="indent">
										<tr>				
											<td>${indent.productName}</td>
											<td>${indent.sub_ProductName}</td>
											<td>${indent.child_ProductName}</td>
											<td>${indent.measurementName}</td>
											<td>${indent.recivedQty}</td>	
											<td>${indent.issuedQty}</td>
											<td class="valor3 text-right">${indent.quantity}</td>	
											<%-- <td>${indent.basicAmt}</td>	 --%>
											<td>${indent.pricePerUnit}</td>	
											<td class="valor2 text-right">${indent.totalAmount}</td>
											<td>${indent.expiryDate}</td>
										</tr>
						</c:forEach>
			        </tbody>
					<tfoot>
				        <tr class="info">
				            <td colspan="6" class="text-right subTtl">TOTAL Quantity:</td>
				            <td class="quantity text-right subTtl" style="display:none;"></td>
				            <td class="text-right" style="display:none;"></td>
				            <td class="text-right" style="display:none;"></td>
				            <td class="text-right" style="display:none;"></td>
				        </tr>
		         </tfoot>
				<tfoot>
			        <tr class="info">
			            <td colspan="8" class="text-right subTtl">SUB TOTAL:</td>
			            <td class="total text-right subTtl" style="display:none;"></td>
			            <td class="text-right subTtl" style="display:none;"></td>
			           
			        </tr>
			   </tfoot>
		</table>
		<button onclick="topFunction()" id="myBtn" title="Go to top" class="arrows"><img src="images/toparrow.jpg" width="30px" height= "30px"></button>
		<div class="elements col-md-4 col-md-offset-8">
		    <div class="col-md-6 Mrgtop10">Grand Total Amount : </div>
		    <div class="col-md-6"><h4 style="ccolor: #ef7e2d;"><span>${requestScope['grandTotalAmount']}</span></h4></div>
		    </div>
		 			</div>	
		 			
		 </div>

				<!-- /page content -->
			</div>
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
	<script src="js/sidebar-resp.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>

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
					$('#tblnotification').DataTable({
							   "aLengthMenu": [[5,10,25, 50, 75, -1], [5,10, 25, 50, 75, "All"]],
						        "iDisplayLength": 10
						    }
					);
				});

		
		/* $('#tblnotification').stacktable({myClass:'stacktable small-only'}); */
		
		$(document).ready(function () {
			$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 $(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(tAmount.toFixed(2));
			});
		   
		});
		
		/* *************** Paginataion Total********* */
		
				$(document).ready(function () {
			$(".total").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
			 $(".total").text(0);
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("click", " .pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(tAmount.toFixed(2));
			});
		   
		});
	
				/* *************** Paginataion Quantity Total********* */
				
						$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 $(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("click", ".pagination",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		   
		});
						/* *************** Show Total********* */
						
						$(document).ready(function () {
					$(".total").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tAmount = 0;
					 $(".total").text(0);
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
				    });
				    $(".subTtl").hide();	
				 	$(document).on("change", " .dataTables_length",function(){
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tAmount = 0;
						 $(".total").text(0);
					    jQuery.each(val,function(index,item){
					    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					    });
					 
						$(".total").text(tAmount.toFixed(2));
					});
				   
				});

						/* *************** show Sub Total********* */
						
								$(document).ready(function () {debugger;
					$(".quantity").text(0);
					 var val = $('#tblnotification').find('tbody').find('tr');
					 var tquantity = 0;
					 $(".quantity").text(0);
				    jQuery.each(val,function(index,item){
				    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
				    });
				    $(".subTtl").hide();	
				 	$(document).on("change", ".dataTables_length",function(){debugger;
				 		$(".subTtl").show();
				 		$(".text-right").show();
				 		 var val = $('#tblnotification').find('tbody').find('tr');
						 var tquantity = 0;
						 $(".quantity").text(0);
					    jQuery.each(val,function(index,item){
					    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
					    });
					 
						$(".quantity").text(tquantity.toFixed(2));
					});
				   
				});
		
	
		   
		
		$(document).ready(function () {
			$(".quantity").text(0);
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tquantity = 0;
			 $(".quantity").text(0);
		    jQuery.each(val,function(index,item){
		    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') ));
		    });
		    $(".subTtl").hide();	
		 	$(document).on("keyup", ".input-sm",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tquantity = 0;
				 $(".quantity").text(0);
			    jQuery.each(val,function(index,item){
			    	tquantity = tquantity + (parseFloat(jQuery(item).find('.valor3 ').text().replace(/,/g,'') || 0));
			    });
			 
				$(".quantity").text(tquantity.toFixed(2));
			});
		   
		});
		
		
		
		
		
		
		
		
	</script>

</body>
</html>
