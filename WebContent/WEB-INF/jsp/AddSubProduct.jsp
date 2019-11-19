<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
        	<jsp:include page="CacheClear.jsp" />  
				
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		<link rel="stylesheet" href="css/main.css">
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		

		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
		<script type="text/javascript" src="js/JQuery.js"> </script>
		<script type="text/javascript" src="js/ComboBox_SubProd.js"> </script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
		<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" />

		<style>
		.success,.error {text-align: center;font-size: 14px;}
		.custom-combobox {position: relative;display: inline-block;width:100%;}
		.custom-combobox-input{width:88% !important;}
		.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;/* support: IE7 */*height: 1.7em;*top: 0.1em;}
		.ui-widget label{font-size:14px}
		
		.pro-table thead, .pro-table tbody tr{table-layout:fixed;display:table;width:100%;}
		.table>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
		.pro-table thead{width: calc(100% - 18px) !important;}
		.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
		.border-inwards-box{text-align: left;}
		.form-control, .custom-combobox-input{height:34px;}
		.table-new tbody tr td {border-top:0px !important;}
		.pordtxtname{width:100% !important;max-width:100%;}
		</style>
<script type="text/javascript">
$(document).ready(function()
		{
	$("#Product_add").hide();
	$("#SubProduct_add").hide();
	$("#SubProductList").hide();
	$("#SubProduct_delete").hide();
	$("#Submit").hide();
	$('select#ServiceType').change(function(){

		 var varServiceType = document.getElementById('ServiceType').options[document.getElementById('ServiceType').selectedIndex].value;
	     if(varServiceType == 'add'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Product_add").show();
	    	 $("#SubProduct_add").show();
	    	 $("#SubProductList").hide();
	    	 $("#SubProduct_delete").hide();
	    	 $("#combobox_Product1").val("");
	    	 $("#subproduct").val("");
	    		$("#Submit").show();
	    		
	    		/* $(function() {
	    			var prodId=$("#combobox_Product").val().split("@@")[0];
		    		$("#subproduct").autocomplete({

		    			source : function(request, response) {debugger;
		    				$.ajax({
		    					url : "getSubProducts.spring",
		    					type : "POST",
		    					data : {
		    						'prodId':$("#combobox_Product").val().split("@@")[0],
		    						term : request.term
		    					},
		    					dataType : "json",
		    					success : function(data) {
		    						console.log(data);
		    						 var dataLen = data.length;
		    						 // alert(dataLen);
		    			              for (i=0; i<dataLen; i++) {
		    			            	  trHTML += '<option value=' + data[i].sub_ProductId + '@@'  + data[i].sub_ProductName + '>'+  data[i].sub_ProductName + '</option>' ;
		    			              }	
		    			              //alert(trHTML);
		    			             $('#subproduct').empty();
		    			             $('#subproduct').html(trHTML);
		    						//alert(data);
		    						//response(data);
		    					}
		    				});
		    			}
		    		});
		    	}); */
	    		
	     }else if(varServiceType == 'View'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#SubProductList").show();
	    	 $("#SubProduct_delete").hide();
	    	 $("#Submit").hide();
	    	 $(".loader-sumadhura").show();	
	    
	    	 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'ViewSubProduct.spring?AllProducts=',
					//data: {'size':productName},
					success : function(data) {
						//alert("Success");  
						var trHTML = '';
						  var dataLen = data.length;
						//  alert(dataLen);
						  $('#records_table').empty();
						  trHTML += ' <thead><tr><th>S No</th><th>Product Name</th><th style=""> Sub Product Name</th></tr></thead>';
						  trHTML += '<tbody style="" class="fixed-table-body">';
						  for (i=0; i<dataLen; i++) {
			            	  trHTML += '<tr><td >' + (i+1) + '</td><td >' + data[i].productName + '</td><td >' + data[i].sub_ProductName + '</td></tr>';
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
	     }else if(varServiceType == 'delete'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#SubProductList").hide();
	    	 $("#SubProduct_delete").show();
	    	 $("#combobox_Delete_Product1").val("");
	    	 $("#combobox_delete_SubProd1").val("");
	    	 $("#Submit").show();
	     }else {
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#SubProductList").hide();
	    	 $("#SubProduct_delete").hide();
	    	 $("#Submit").hide();
	     }
	     
	});
	});



	
	
       function formValidator() {
	   var serviceType = document.getElementById('ServiceType').value;
       if(serviceType == "add"){
	   var add_product = document.getElementById('combobox_Product').value;
       var add_subProduct  =  document.getElementById("subproduct").value;
	   if(add_product == '' || add_product == null){
	   alert("please enter product name");
	   return false;
	   }else if(add_subProduct == '' || add_subProduct == null){
	    alert("please enter sub product name");
	   	return false;
	   	
	    	}
	   
	   $(".loader-sumadhura").show();
	    }else if(serviceType == "delete"){
	    var del_product = document.getElementById('combobox_Delete_Product').value;
	    var del_subProduct  =  document.getElementById('combobox_delete_SubProd').value;
	    	   
	    if(del_product == '' || del_product == null){
	     alert("please enter product name");
	   	 return false;
	    }else if(del_subProduct == '' || del_subProduct == null || del_subProduct == '@@'){
	    alert("please enter sub product name");
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
								<li class="breadcrumb-item"><a href="#">Masters</a></li>
								<li class="breadcrumb-item active">Sub Product Master</li>
							</ol>
						</div>	
						<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>								
				          </div>
						<!-- LOader -->					
						<div align="center">
						<div class="">	<!--  addvendorfrm	 -->				
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
						<label class="error"><c:out value="${requestScope['errMessage']}"></c:out> </label>
						 <c:set value="" var="succMessage"></c:set>
						 <c:set value="" var="errMessage"></c:set> 						
					    <form name="myForm"  action="savesubproduct.spring"  onsubmit="return formValidator()" method="post" class="form-horizontal border-inwards-box">
						    <div class="col-md-6 col-md-offset-3">
						      <div class="input_div">
									<div class="form-group">					     	  		
										<label class="commlbl col-md-6">Service Type :</label>										
										<div class="col-sm-6">
											<select id="ServiceType" name="ServiceType" class="form-control">
							                    <option value="">--Select--</option>
											    <option value="add">Add</option>
											    <option value="View">View</option>
											    <option value="delete">Delete</option>
											</select>												
										</div>
						  	         </div>
								</div>					
								<div id="Product_add">
								     <div class="ui-widget">
								     	<div class="form-group">											
								       		<label class="commlbl col-md-6">Product Name :</label>								       		
							       			<div class="col-md-6"> 
											       <%
											          List<Map<String,Object>> totalProductList = (List<Map<String,Object>>)request.getAttribute("totalProductsList");
											          String strProductName = "";
											          String strProductId = "";
											          
											        %>
											        
											        <select id="combobox_Product" name="combobox_Product1" class="form-control pordtxtname">
											         <option value=""></option>
											        <%
											          for(Map productList : totalProductList){
											        	  strProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
											        	  strProductId = productList.get("PRODUCT_ID") == null ? "" : productList.get("PRODUCT_ID").toString();       
											       %>
											          
											           <option value="<%= strProductId+ "@@"+ strProductName%>"><%= strProductName%></option>
											            <%}
											          %>
											        </select>
							    			   </div>
								    	  </div>
								     </div>
								</div>				
					        
								<div id="SubProduct_add">
								     <div class="input_div">
								         <div class="form-group">
								              	<label class="commlbl col-md-6"> Sub Product : </label>								              	
								              	<div class="col-md-6"> 
								              		<input type="text" name="subproduct" id="subproduct" onkeyup="populateSite(this)" class="form-control pordtxtname">
								              	</div>
								              </div>
								        </div>
								   </div>
					
								   <div id="SubProduct_delete">		
										<div class="ui-widget">
											<div class="form-group">
										       		<label class="commlbl col-md-6">Product Name: </label>										       		
										       		<div class="col-md-6"> 
												       <%
												          List<Map<String,Object>> totalProductListDel = (List<Map<String,Object>>)request.getAttribute("totalProductsList");
												          String strDelProductName = "";
												          String strDelProductId = "";
												          
												        %>
												        
												        <select id="combobox_Delete_Product" name="combobox_Delete_Product1" class="form-control pordtxtname">
												         <option value=""></option>
												        <%
												          for(Map productList : totalProductListDel){
												        	  strDelProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
												        	  strDelProductId = productList.get("PRODUCT_ID") == null ? "" : productList.get("PRODUCT_ID").toString();       
												       %>
												          
												           <option value="<%= strDelProductId+ "@@"+ strDelProductName%>"><%= strDelProductName%></option>
												            <%}
												          %>
												        </select>
												     </div>
												</div>										        
										    </div>									    
										    <div class="form-group">									 			
									       		<label class="commlbl col-md-6">Sub Product: </label>
										       	<div class="col-md-6"> 										       	
										       	  <select id="combobox_delete_SubProd" name="combobox_delete_SubProd1"  class="form-control pordtxtname"></select>
										       	</div>
											</div>
									     </div>
										<div id="submitbutton" class="col-md-12 text-center center-block">
										   <input type="submit" value="Submit" id="Submit" class="btn btn-warning" name="Submit">
										</div>
									  </div>
					             </form>
					           </div>
					   </div>
					<div id="SubProductList">
					     <div class="col-md-12 no-padding-right">
						  <div class="col-md-3 col-md-offset-6" style="float:right;">
						    <input type="text" class="form-control" id="searchchildproductTableview" placeholder="Search..." style="display:none;"/>
						  </div>
					   </div>			
			         <div class="clearfix"></div>
						<div class="table-responsive"> <!-- recordtbls1 -->
							<table class="table pro-table table-new Mrgtop20" id="records_table" cellpadding="2">
							    
							 </table>
						 </div>
					 </div>
				</div>
			</div>
		</div>

		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<script src="js/custom.js"></script>
		<script src="js/sidebar-resp.js"></script>
         <script>
		
			$(document).ready(function() {	
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
		
			/* **************************************************auto complete start *************************************************** */
			
			function populateSite(id) {debugger;
			  //var Product=$("#product").val();
			  //var Product_Type=$("#Product_Type").val();
			 var prodId=$("#combobox_Product").val().split("@@")[0];
			var subprod=$("#subproduct").val();
			
			  var url = "getSubProductsforAutoComplete.spring?Product="+prodId+"&subprodId="+subprod;

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
				  $("#subproduct").autocomplete({
				  		source : resultArray,
				  		change: function (event, ui) {
			                if(!ui.item){
			               	//if list item not selected then make the text box null	
			               	// $("#subproduct").val("");
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
			
			
		</script> 
 
</body>
</html>