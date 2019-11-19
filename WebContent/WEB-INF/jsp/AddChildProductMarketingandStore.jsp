<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<jsp:include page="CacheClear.jsp" />  

        <meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="css/main.css">
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		
		
		<script type="text/javascript" src="js/JQuery.js"> </script>
		<script type="text/javascript" src="js/ComboBox_ChildProduct.js"> </script>

		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
		<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" />
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
		<style>
		.pordtxtname {width:100% !important;max-width:100% !important;}
		.custom-combobox {position: relative;display: inline-block;width:88% !important;}
		.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;/* support: IE7 */*height: 1.7em;*top: 0.1em;}
		.success,.error {text-align: center;font-size: 14px;}
		.ui-widget label {font-size: 14px}
		.ui-autocomplete {max-height: 150px;overflow-y: auto;/* prevent horizontal scrollbar */overflow-x: hidden;/* add padding to account for vertical scrollbar */padding-right: 20px;}
		.pro-table thead, .pro-table tbody tr{table-layout:fixed;display:table;width:100%;}
		.table>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
		.pro-table thead{width: calc(100% - 18px) !important;} 
		.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
		.border-inwards-box{text-align: left;}
		.form-control, .custom-combobox-input{height:34px;width:100% !important;}
		.table-new tbody tr td{border-top:0px !important;}
		.view_All{color:#0000ff;margin-top:10px;margin-left:10px;text-decoration:underline;vertical-align: -webkit-baseline-middle;}
		.view_All:hover{color:#0000ff;margin-top:10px;margin-left:10px;vertical-align: -webkit-baseline-middle;}
		
		
		</style>
<script type="text/javascript">


$(document).ready(function(){

	 $("#Child_Product").hide();
	 $("#Measurement_add").hide();
	 $("#SubProductsList").hide();
	 $("#ChildProduct_delete").hide();
	 $("#ChildProduct_view").hide();
	 $("#Submit").hide();
	 $("#viewAll").hide();
	 $("#combobox").on('change',  function () {
         alert("test");              
     });
	
	$('select#ServiceType').change(function(){

		 var varServiceType = document.getElementById('ServiceType').options[document.getElementById('ServiceType').selectedIndex].value;
	     if(varServiceType == 'add'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Child_Product").show();;
	    	 $("#Measurement_add").show();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").hide();
	    	 $("#ChildProduct_view").hide();
	    	 $("#Product_Type").val("");
				$("#product_Add1").val("");
				$("#combobox_SubProd1").val("");
				$("#childproduct").val("");
				$("#measurement").val("");
	    	 $("#Submit").show();
	    	 $("#viewAll").hide();
	    	
	    	$(function() {
	    		$("#childproduct").autocomplete({

	    			source : function(request, response) {
	    				var varSelectedSubProductId = document.getElementById('selectedSubProductId').value;
	    				//alert(varSelectedSubProductId);
	    				$.ajax({
	    					url : "autoSearchchildproduct.spring",
	    					type : "POST",
	    					data : {
	    						term : request.term,subProductId:varSelectedSubProductId
	    					},
	    					dataType : "json",
	    					success : function(data) {
	    						
	    						//alert(data);
	    						response(data);
	    					}
	    				});
	    			}
	    		});
	    	});
	    	
	     }else if(varServiceType == 'View'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Child_Product").hide();
	    	 $("#Measurement_add").hide();
	    	 $("#SubProductsList").show();
	    	 $("#ChildProduct_delete").hide();
	    	 $("#ChildProduct_view").show();
	    	 $("#child_name").val("");
	    	 $('#records_table').html("");
	    	 $("#searchchildproductTableview").hide();
	    	 //$("Submit").hide();
	    	 $("#Submit").hide();
	    	 $("#viewAll").show();
	         //$(".loader-sumadhura").show();
	    	 
	     }else if(varServiceType == 'delete'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Child_Product").hide();
	    	 $("#ChildProduct_Add").hide()
	    	 $("#Measurement_add").hide();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_view").hide();
	    	 $("#viewAll").hide();
	    	 $("#ChildProduct_delete").show(); 
	    	 $("#Delete_Product_Type").val("");
			 $("#product_Delete").val("");
			 $("#combobox_delete_SubProd1").val("");
			 $("#combobox_delete_ChildProd1").val("");
	    	 
	    	$("#Submit").show();
	    	
	     }else{
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Child_Product").hide();
	    	 $("#Measurement_add").hide();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").hide();
	    	 $("#ChildProduct_view").hide();
	    	$("#Submit").hide();
	    	 $("#viewAll").hide();
	     }
	});
	});

	function formValidator() {debugger;
	   var serviceType = document.getElementById('ServiceType').value;
    if(serviceType == "add"){
    	var product_Type=document.getElementById('Product_Type').value;
	   var add_product = document.getElementById('product_Add').value;
       var add_subProduct  =  document.getElementById('combobox_SubProd').value;
       var add_childproduct  =  document.getElementById('childproduct').value;
       var add_measurement  =  document.getElementById('measurement').value;
       var cpgrp_mst = $('#cpgroup_measurement').val();	
   	   var materialGroupId = $('#materialGroupId').val();	
   	   var materialGroupMeasurementId = $('#materialGroupMeasurementId').val();	
   	
       if(product_Type == "" || product_Type ==null){
    	   alert("Please Select Product Type");
    	   return false;
    	   } 
   	   else if(add_product == '' || add_product == null || add_product=='--Select--'){
	   alert("please enter product name");
	   return false;
	   }else if(add_subProduct == '' || add_subProduct == null || add_subProduct == '@@'){
	    alert("please enter sub product name");
	   	return false;
	   	
	   }else if(add_childproduct == '' || add_childproduct == null){
		    alert("please enter child product name");
		   	return false;
		   	
	   }else if(add_measurement == '' || add_measurement == null){
		    alert("please enter  measurement name");
		   	return false;
	   }else if(cpgrp_mst != 'NA' && cpgrp_mst != 'MKT'){
		   if(cpgrp_mst != add_measurement || cpgrp_mst == ''){
				alert("Measurement has to match for Group Measurement.\r\n(Unless you select Group Measurement as 'NA')");
			   	return false;
			}
		}
       var canISubmit = window.confirm("Do you want to Submit?");
		if (canISubmit == false) {
			return false;
		}
	   debugger;
	   $(".loader-sumadhura").show();
	    }else if(serviceType == "delete"){
	    var product_Type=	document.getElementById('Delete_Product_Type').value;
	    var del_product = document.getElementById('product_delete').value;
	    var del_subProduct  =  document.getElementById('combobox_delete_SubProd').value;
	    var del_ChildProd  =  document.getElementById('combobox_delete_ChildProd').value;
	    if(product_Type =="" || product_Type==null){
	    	 alert("please select product type");
		   	 return false;
	    }
	    else if(del_product == '' || del_product == null || del_product=='--Select--'){
	     alert("please select product name");
	   	 return false;
	    }else if(del_subProduct == '' || del_subProduct == null || del_subProduct == '@@'){
	    alert("please select sub product name");
	   	 return false;
	   	 
	    }else if(del_ChildProd  == '' || del_ChildProd  == null || del_ChildProd == '@@'){
		    alert("please select child product name");
		   	return false;
	    	   }
	    $(".loader-sumadhura").show();
	     }
	    
	    
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
						<li class="breadcrumb-item"><a href="#">Masters</a>
						</li>
						<li class="breadcrumb-item active">Child Product Masters</li>
					</ol>
				</div>
				<!-- Loader  -->
				<div class="loader-sumadhura" style="display: none; z-index: 999;">
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
				<!-- LOader -->
				<div align="center">
					<div class="">
						<!-- addvendorfrm -->
						<label class="success"><c:out
								value="${requestScope['succMessage']}"></c:out> </label> <label
							class="error"><c:out
								value="${requestScope['errMessage']}"></c:out> </label>
						<form name="myForm" action="save.spring" method="post"
							onsubmit="return formValidator()"
							class="form-horizontal border-inwards-box">
							<div class="col-md-6 col-md-offset-3">
								<div class="input_div">
									<div class="form-group">
										<label class="commlbl col-md-6">Service Type :</label>
										<div class="col-md-6">
											<select id="ServiceType" name="ServiceType"
												class="form-control pordtxtname">
												<option value="">--Select--</option>
												<option value="add">Add</option>
												<option value="View">View</option>
												<option value="delete">Delete</option>
											</select>
										</div>
									</div>
								</div>
								<div id="Child_Product">
									<div id="indentIssueTableId">
										<div class="ui-widget">
											<div class="form-group">
												<label class="commlbl col-md-6">Product Type :</label>
												<div class="col-md-6">
													<select name="Product_Type" id="Product_Type"
														class="form-control" onchange="getSiteRelatedProducts()">
														<option value="">--SELECT--</option>
														<option value="STORE">STORE</option>
														<option value="MARKETING">MARKETING</option>
														<option value="ALL">ALL</option>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="commlbl col-md-6">Product Name: </label>

												<div class="col-md-6">
													<select id="product_Add" name="product_Add1"
														class="form-control pordtxtname"
														onchange="getSiteRelatedSubProductsProducts()">
														<option value=""></option>
													</select> <input type="hidden" name="selectedSiteData"
														id="selectedSiteData" value="true" />
												</div>
											</div>
										</div>
										<div class="ui-widget">
											<div class="form-group">
												<label class="commlbl col-md-6">Sub Product: </label>
												<div class="col-md-6">
													<select id="combobox_SubProd" name="combobox_SubProd1"
														class="form-control"></select>
												</div>
											</div>
										</div>
										<div class="input_div">
											<div class="form-group">
												<label class="commlbl col-md-6"> Child Product : </label>
												<div class="col-md-6">
													<input type="hidden" name="selectedSubProductId" value=""
														id="selectedSubProductId"> <input type="text"
														name="childproduct" id="childproduct"
														class="form-control pordtxtname" autocomplete="off">
												</div>
											</div>
										</div>
										<div class="input_div">
											<div class="form-group">
												<label class="commlbl col-md-6">Measurement :</label>
												<div class="col-md-6">
													<input type="text" name="measurement" id="measurement" onkeyup="populateChild(this)"
														class="form-control pordtxtname" autocomplete="off">
												</div>
											</div>
										</div>
										
										
										<!-- hide below 2 fields with id 'combobox_childproductgroup' & 'cpgroup_measurement' when select ProductType as marketing -->
										<div class="ui-widget not-for-marketing">
											<div class="form-group">
													<label class="commlbl col-md-6">Product Group: </label>
													<div class="col-md-6"> 
													  <select id="combobox_childproductgroup" name="combobox_childproductgroup" class="form-control"></select>
													</div>
											</div>
										</div>
									      <div class="input_div not-for-marketing">
									         <div class="form-group">
									              <label class="commlbl col-md-6">Group Measurement :</label>									             
									               <div class="col-md-6"> 
									                 <input type="text" name="cpgroup_measurement" id="cpgroup_measurement"  class="form-control pordtxtname" readonly="true">
									              </div>
								              </div>
									      </div>
									      
									      <div class="input_div">
									         <div class="form-group">
									              <label class="commlbl col-md-6">Remarks :</label>									             
									               <div class="col-md-6"> 
									                 <input type="text" name="remarks" id="remarks"  class="form-control pordtxtname" autocomplete="off">
									              </div>
								              </div>
									      </div>
									     <input type="hidden" id="childproductgroup" name="childproductgroup" value="" />
									     <input type="hidden" id="materialGroupId" name="materialGroupId" value="" />
									     <input type="hidden" id="materialGroupMeasurementId" name="materialGroupMeasurementId" value="" />
									    										
									</div>
									<!-- <label> SubProduct</label> <input type="text" name="subproduct"> -->
								</div>
								<div id="ChildProduct_delete">
									<div id="indentIssueTableId">
										<div class="ui-widget">
											<div class="form-group">
												<label class="commlbl col-md-6"> ProductType : </label>
												<div class="col-md-6">

													<select name="delete_Product_Type" id="Delete_Product_Type"
														class="form-control"
														onchange="getSiteRelatedProductsToDelete()"><option
															value="">--SELECT--</option>
														<option value="STORE">STORE</option>
														<option value="MARKETING">MARKETING</option>
														<option value="ALL">ALL</option>
													</select>
												</div>
											</div>
											<div class="ui-widget">
											<div class="form-group">
												<label class="commlbl col-md-6">Product Name: </label>

												<div class="col-md-6">
													<select id="product_delete" name="product_Delete"
														class="form-control pordtxtname"
														onchange="getSiteRelatedSubProductsDelete()">
														<option value=""></option>
													</select>
												</div>
											</div>
											</div>
										</div>
									</div>
									<div class="ui-widget">
										<div class="form-group">
											<label class="commlbl col-md-6">Sub Product :</label>
											<div class="col-md-6">
												<select id="combobox_delete_SubProd"
													name="combobox_delete_SubProd1" class="form-control"
													onchange="getSiteRelatedChildProductsDelete()">
												</select>
											</div>
										</div>
									</div>
									<div class="ui-widget">
										<div class="form-group">
											<label class="commlbl col-md-6">Child Product :</label>
											<div class="col-md-6">
												<select id="combobox_delete_ChildProd"
													name="combobox_delete_ChildProd1" class="form-control">
												</select>
											</div>
										</div>
									</div>
								</div>
									<div id="ChildProduct_view">
									<div id="indentIssueTableId">
										<div class="ui-widget">
											<div class="form-group">
												<label class="commlbl col-md-6">Child Product Name: </label>

												<div class="col-md-6">
												<input type="text" name="child_name" onkeyup="populateSite(this)" class="form-control" id="child_name" autocomplete="true" value=""/>	
												</div>
											</div>
										</div>
									</div>
									
								</div>
								<div id="submitbutton" class="col-md-12 text-center center-block">
									<input type="submit" value="Submit" id="Submit" class="btn btn-warning Mrgtop20" name="Submit" /> 
									<input type="hidden" name="selectedSiteData" id="selectedSiteData" value="true" />
									<a href="#"  id="viewAll"  name="viewAll" class="view_All" onclick="getChildProducts()">Show All</a>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div id="SubProductsList">
				    <div class="col-md-12 no-padding-right">
					  <div class="col-md-3 col-md-offset-6" style="float:right;">
					    <input type="text" class="form-control" id="searchchildproductTableview" placeholder="Search..." style="display:none;"/>
					  </div>
			        </div>	
			        <div class="clearfix"></div>
					<div class="table-responsive ">
						<!-- recordtbls1 -->
						<table id="records_table" border="2"
							class="table table-bordered pro-table table-new Mrgtop20" cellpadding="2"
							style="margin-bottom: 15px;">

						</table>
					</div>
				</div>

			</div>
		</div>
	</div>

<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
  <script>
		
			$(document).ready(function() {	
				loadChildProdGroups();
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				$("#searchchildproductTableview").on("keyup", function() {
				    var value = $(this).val().toLowerCase();
				    $("#records_table tbody tr").filter(function() {
				      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
				    });
				  });
				
			});
			/* ************************************************add time getting the products start*********************************************** */
			function getSiteRelatedProducts(){debugger;
			var Product_Type=$("#Product_Type").val();
			$('#product_Add').html('');
			$('#product_Add').val('');
			//--Rafi--
			if(Product_Type=='MARKETING'){
				$('.not-for-marketing').hide();
				$('#materialGroupId').val('MKT');	
				$('#childproductgroup').val('MKT');	
				$('#materialGroupMeasurementId').val('MKT');	
				$('#cpgroup_measurement').val('MKT');
			} 
			else{
				$('.not-for-marketing').show();
				$('#materialGroupId').val('');	
				$('#childproductgroup').val('');	
				$('#materialGroupMeasurementId').val('');	
				$('#cpgroup_measurement').val('');
			}
			//--------
			
			$.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getSiteRelatedProducts.spring?Product_Type='+Product_Type,
					success : function(data) {
						var trHTML = '<option>--Select--</option>';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].PRODUCT_ID + '@@'  + data[i].NAME + '">'+  data[i].NAME + '</option>' ;
			              }	
			              
			              $('#product_Add').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}
			/* ************************************************add time getting the products end*********************************************** */
			/* ************************************************deleted time getting the subproducts start*********************************************** */
			function getSiteRelatedSubProductsProducts(){debugger;
			var prod_id=$("#product_Add").val();
			$('#combobox_SubProd').html('');
			$('#combobox_SubProd').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getSubProducts.spring?prodId='+prod_id,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].sub_ProductId + '@@'  + data[i].sub_ProductName + '">'+  data[i].sub_ProductName + '</option>' ;
			              }	
			              
			              $('#combobox_SubProd').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}
			/* ************************************************deleted time getting the subproducts end*********************************************** */
		/* ************************************************deleted time getting the products start*********************************************** */
			function getSiteRelatedProductsToDelete(){debugger;
			var Product_Type=$("#Delete_Product_Type").val();
			$('#product_delete').html('');
			$('#product_Delete').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getSiteRelatedProducts.spring?Product_Type='+Product_Type,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].PRODUCT_ID + '@@'  + data[i].NAME + '">'+  data[i].NAME + '</option>' ;
			              }	
			              
			              $('#product_delete').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}	
		/* ***********************************************deleted sub products selected started****************************************************** */
		function getSiteRelatedSubProductsDelete(){debugger;
			var prod_id=$("#product_delete").val();
			$('#combobox_delete_SubProd').html('');
			$('#combobox_delete_SubProd').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getSubProducts.spring?prodId='+prod_id,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].sub_ProductId + '@@'  + data[i].sub_ProductName + '">'+  data[i].sub_ProductName + '</option>' ;
			              }	
			              
			              $('#combobox_delete_SubProd').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}	
		/* ***********************************************deleted sub products selected started****************************************************** */
		function getSiteRelatedChildProductsDelete(){debugger;
			var SubprodId=$("#combobox_delete_SubProd").val();
			$('#combobox_delete_SubProd').html('');
			$('#combobox_delete_SubProd').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getChildProducts.spring?subProdId='+SubprodId,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].child_ProductId + '@@'  + data[i].child_ProductName + '">'+  data[i].child_ProductName + '</option>' ;
			              }	
			              
			              $('#combobox_delete_SubProd').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}	
	/* ***************************************************view all product details start ajax call********************************************** */	
				
	function getChildProducts(){
		 $(".loader-sumadhura").show();
		$.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'ViewChildProduct.spring?AllProducts=true',
					//data: {'size':productName},
					success : function(data) {
						//alert("Success");  
						var trHTML = '';
						  var dataLen = data.length;
						//  alert(dataLen);
						  $('#records_table').empty();
						  trHTML += '<thead class="cal-thead-inwards"> <tr><th>S No</th><th>Product Name</th><th>Sub ProductName</th><th>Child Product Name</th><th>Measurement</th><th>Product Type</th></tr></thead>';
			              
						  trHTML += '<tbody class="fixed-table-body">';
						  debugger;
						  for (i=0; i<dataLen; i++) {
			            	  trHTML += '<tr><td>' + (i+1) + '</td><td>' + data[i].productName + '</td><td>' + data[i].sub_ProductName + '</td><td>' + data[i].child_ProductName + '</td><td>' + data[i].measurementName + '</td><td>' + data[i].productType + '</td></tr>' ;
			              }
						  trHTML += '</tbody>';
			              $('#records_table').append(trHTML);
			              $('#searchchildproductTableview').show();
			              $(".loader-sumadhura").hide();
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
	}
	
	/* **************************************************populate state start ******************************************************************** */
	function populateSite(id) {debugger;
	 
  var childProduct=$("#child_name").val();
  var url = "ViewChildProductForAutoComplete.spring?childProduct="+childProduct;

   $.ajax({
   url : url,
   //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
   type : "GET",
   Cdata : "",
   contentType : "application/json",
   success : function(data) {
 	  console.log(data);
 	 Cdata = data.split("@@");
	  
	  var resultArray = [];
	  for(var i=0;i<Cdata.length;i++){
	      resultArray.push(Cdata[i].split("@@")[0]);
	  }
	  $("#child_name").autocomplete({
	  		source : resultArray,
	  		change: function (event, ui) {
                if(!ui.item){
               	//if list item not selected then make the text box null	
               	 $("#child_name").val("");
                }
              },
	  		select: function (event, ui) {debugger;
                showChildProducts(event, ui.item);
            }

	  	});
 	  
 	 /* var trHTML = '';
	  var dataLen = data.length;
	//  alert(dataLen);
	  $('#records_table').empty();
	  trHTML += '<thead class="cal-thead-inwards"> <tr><th>S No</th><th>Product Name</th><th>Sub ProductName</th><th>Child Product Name</th><th>Measurement</th><th>Product Type</th></tr></thead>';
     
	  trHTML += '<tbody class="fixed-table-body">';
	  debugger;
	  for (i=0; i<dataLen; i++) {
   	  trHTML += '<tr><td>' + (i+1) + '</td><td>' + data[i].productName + '</td><td>' + data[i].sub_ProductName + '</td><td>' + data[i].child_ProductName + '</td><td>' + data[i].measurementName + '</td><td>' + data[i].productType + '</td></tr>' ;
     }
	  trHTML += '</tbody>';
     $('#records_table').append(trHTML);
     $('#searchchildproductTableview').show();
     $(".loader-sumadhura").hide(); */
   },
   error:  function(data, status, er){
 	 // alert(data+"_"+status+"_"+er);
 	  }
   });

 //code for selected text
 	function AutoCompleteSelectHandler(event, ui)
 	{}
 
};


		function showChildProducts(e, val){debugger;
			
			$.ajax({
				type: 'GET',
				//dataType: 'json',
				url: 'ViewandGetChildProduct.spring?childProductName='+val.value,
				//data: {'size':productName},
				success : function(data) {
					//alert("Success");  
					var trHTML = '';
					  var dataLen = data.length;
					//  alert(dataLen);
					
					  $('#records_table').html("");
					  trHTML += '<thead class="cal-thead-inwards"> <tr><th>S No</th><th>Product Name</th><th>Sub ProductName</th><th>Child Product Name</th><th>Measurement</th><th>Product Type</th></tr></thead>';
		              
					  trHTML += '<tbody class="fixed-table-body">';
					  debugger;
					  for (i=0; i<dataLen; i++) {
		            	  trHTML += '<tr><td>' + (i+1) + '</td><td>' + data[i].productName + '</td><td>' + data[i].sub_ProductName + '</td><td>' + data[i].child_ProductName + '</td><td>' + data[i].measurementName + '</td><td>' + data[i].productType + '</td></tr>' ;
		              }
					  trHTML += '</tbody>';
		              $('#records_table').html(trHTML);
		              $('#searchchildproductTableview').show();
		              $(".loader-sumadhura").hide();
					},  
				error : function(e) {  
					alert('Error: ' + e);   
					} 
			});
		}

		/* *****************************************************child product auto complete start******************************************************* */
		function populateChild(id) {debugger;
				  //var Product=$("#product").val();
				  //var Product_Type=$("#Product_Type").val();
				 var childId=$("#childproduct").val();
				var measurement=$("#measurement").val();
				
				  var url = "getmeasurementforAutoComplete.spring?childProduct="+childId+"&measurement="+measurement;

				   $.ajax({
				   url : url,
				   //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
				   type : "GET",
				   contentType : "application/json",
				   success : function(data) {
				 	  console.log(data);
				 	  Cdata = data.split("@@");
					  
					  var resultArray = [];
					  for(var i=0;i<Cdata.length;i++){
					      resultArray.push(Cdata[i].split("@@")[0]);
					  }
					  $("#measurement").autocomplete({
					  		source : resultArray,
					  		change: function (event, ui) {
				                if(!ui.item){
				               	//if list item not selected then make the text box null	
				               	 //$("#measurement").val("");
				                }
				              },
					  		select: function (event, ui) {debugger;
				                //showChildProducts(event, ui.item);
				            }

					  	}); 
				 	 
				   },
				   error:  function(data, status, er){
				 	
				 	  }
				   });

				};			

		var referrer="AddChildProducts.spring";
		$SIDEBAR_MENU.find('a').filter(function () {
		var urlArray=this.href.split( '/' );
		for(var i=0;i<urlArray.length;i++){
		if(urlArray[i]==referrer) {
		return this.href;
		}
		}
		}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');

		</script> 
</body>
</html>