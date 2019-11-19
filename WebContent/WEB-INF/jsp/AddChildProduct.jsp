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
		.custom-combobox {position: relative;display: inline-block;}
		.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;/* support: IE7 */*height: 1.7em;*top: 0.1em;}
		.success,.error {text-align: center;font-size: 14px;}
		.ui-widget label {font-size: 14px}
		.ui-autocomplete {max-height: 150px;overflow-y: auto;/* prevent horizontal scrollbar */overflow-x: hidden;/* add padding to account for vertical scrollbar */padding-right: 20px;}
		.pro-table thead, .pro-table tbody tr{table-layout:fixed;display:table;width:100%;}
		.table>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
		/* .pro-table thead{width: calc(100% - 18px) !important;} */
		.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
		.border-inwards-box{text-align: left;}
		.form-control, .custom-combobox-input{height:34px;}
		.table-new tbody tr td{border-top:0px !important;}
		</style>
<script type="text/javascript">


$(document).ready(function(){

	 $("#Child_Product").hide();
	 $("#Measurement_add").hide();
	 $("#SubProductsList").hide();
	 $("#ChildProduct_delete").hide();
	 $("#Submit").hide();
	 $("#combobox").on('change',  function () {
         //alert("test");              
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
	    	 $("#combobox_Product1").val("");
			 $("#combobox_SubProd1").val("");
			 $("#childproduct").val("");
			 $("#measurement").val("");
				
	    	 $("#Submit").show();
	    	
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
	    			},
	    			select: function( event, ui ) { 
    		  			//alert("UI: "+JSON.stringify(ui));
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
	    	 $("#Submit").hide();
	         $(".loader-sumadhura").show();
	    	 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'ViewChildProduct.spring?AllProducts=',
					//data: {'size':productName},
					success : function(data) {
						//alert("Success");  
						var trHTML = '';
						  var dataLen = data.length;
						//  alert(dataLen);
						  $('#records_table').empty();
						  trHTML += '<thead class="cal-thead-inwards"> <tr><th>S No</th><th>Product Name</th><th>Sub ProductName</th><th>Child Product Name</th><th>Measurement</th></tr></thead>';
			              
						  trHTML += '<tbody class="fixed-table-body">';
						  for (i=0; i<dataLen; i++) {
			            	  trHTML += '<tr><td>' + (i+1) + '</td><td>' + data[i].productName + '</td><td>' + data[i].sub_ProductName + '</td><td>' + data[i].child_ProductName + '</td><td>' + data[i].measurementName + '</td></tr>' ;
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
	    	 $("#Child_Product").hide();
	    	 $("#ChildProduct_Add").hide()
	    	 $("#Measurement_add").hide();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").show();
	    	 $("#combobox_deleteProd1").val("");
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
	    	$("#Submit").hide();
	     }
	});
	});

function formValidator() {
	   var serviceType = document.getElementById('ServiceType').value;
    if(serviceType == "add"){
	   var add_product = document.getElementById('combobox_Product').value;
       var add_subProduct  =  document.getElementById('combobox_SubProd').value;
       var add_childproduct  =  document.getElementById('childproduct').value;
       var add_measurement  =  document.getElementById('measurement').value;
       var cpgrp_mst = $('#cpgroup_measurement').val();	
   	   var materialGroupId = $('#materialGroupId').val();	
   	   var materialGroupMeasurementId = $('#materialGroupMeasurementId').val();	
   	
	   if(add_product == '' || add_product == null){
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
	   }else if(cpgrp_mst != 'NA'){
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
	    var del_product = document.getElementById('combobox_deleteProd').value;
	    var del_subProduct  =  document.getElementById('combobox_delete_SubProd').value;
	    var del_ChildProd  =  document.getElementById('combobox_delete_ChildProd').value;
	    if(del_product == '' || del_product == null){
	     alert("please enter product name");
	   	 return false;
	    }else if(del_subProduct == '' || del_subProduct == null || del_subProduct == '@@'){
	    alert("please enter sub product name");
	   	 return false;
	   	 
	    }else if(del_ChildProd  == '' || del_ChildProd  == null || del_ChildProd == '@@'){
		    alert("please enter  child product name");
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
								<li class="breadcrumb-item active">Child Product Masters</li>
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
						<div align="center">
						<div class=""> <!-- addvendorfrm -->
						<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
						<label class="error"><c:out value="${requestScope['errMessage']}"></c:out> </label> 					
						<form  name="myForm" action="save.spring"   method="post"  onsubmit="return formValidator()" class="form-horizontal border-inwards-box">
                          	<div class="col-md-6 col-md-offset-3">
                          	<div class="input_div">
								<div class="form-group">
										<label class="commlbl col-md-6">Service Type :</label> 
										<div class="col-md-6"> 
											<select id="ServiceType" name="ServiceType" class="form-control pordtxtname">
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
													<label class="commlbl col-md-6">Product Name: </label>
													<div class="col-md-6"> 
														<%
													          List<Map<String,Object>> totalProductList = (List<Map<String,Object>>)request.getAttribute("totalProductsList");
													          String strProductName = "";
													          String strProductId = "";
													          
													        %>
													
																					<select id="combobox_Product" name="combobox_Product1" class="form-control">
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
										<div class="ui-widget">
											<div class="form-group">
													<label class="commlbl col-md-6">Sub Product: </label>
													<div class="col-md-6"> 
													  <select id="combobox_SubProd" name="combobox_SubProd1" class="form-control"></select>
													</div>
											</div>
										</div>
								        <div class="input_div">
								         	<div class="form-group">
								              <label class="commlbl col-md-6"> Child Product : </label>								              
								              <div class="col-md-6"> 
					                               <input type="hidden" name="selectedSubProductId" value="" id="selectedSubProductId">
									               <input type="text" name="childproduct" id="childproduct" class="form-control pordtxtname" autocomplete="off">
								              </div>
								              </div>
								         </div>
									     <div class="input_div">
									         <div class="form-group">
									              <label class="commlbl col-md-6">Measurement :</label>									             
									               <div class="col-md-6"> 
									                 <input type="text" name="measurement" id="measurement"  onkeyup="populateSite(this)" class="form-control pordtxtname" autocomplete="off">
									              </div>
								              </div>
									      </div>
									      
									      <div class="ui-widget">
											<div class="form-group">
													<label class="commlbl col-md-6">Product Group: </label>
													<div class="col-md-6"> 
													  <select id="combobox_childproductgroup" name="combobox_childproductgroup" class="form-control"></select>
													</div>
											</div>
										</div>
									      <div class="input_div">
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
						       						<label class="commlbl col-md-6"> Product Name  : </label>
						       						<div class="col-sm-6"> 
												       <%
												       List<Map<String,Object>> allDeleteProductList = (List<Map<String,Object>>)request.getAttribute("totalProductsList");
												         
												        %>
												        
												        <select id="combobox_deleteProd" name="combobox_deleteProd1" class="form-control">
												         <option value=""></option>
												        <%
												        
												        if(allDeleteProductList !=null && allDeleteProductList.size() > 0){
												        
												        	String strDeleteProductId = "";
												        	String strDeleteProductName = "";
												        	
												          for(Map deleteProductList : allDeleteProductList){
												        	  strDeleteProductId = deleteProductList.get("PRODUCT_ID") == null ? "" : deleteProductList.get("PRODUCT_ID").toString();
												        	  strDeleteProductName = deleteProductList.get("NAME") == null ? "" : deleteProductList.get("NAME").toString(); 
												        	  
												       %>
												           <option value="<%= strDeleteProductId+ "@@"+ strDeleteProductName%>"><%= strDeleteProductName%></option>
												            <%}
												          }
												          %> 
												        </select>
						                      </div>
						               </div>
						        </div>
						     </div>								
					         <div class="ui-widget">
				          		<div class="form-group">
							             <label class="commlbl col-md-6">Sub Product :</label> 					            
							             <div class="col-md-6"> 
							                <select id="combobox_delete_SubProd" name="combobox_delete_SubProd1" class="form-control">
					                        </select>
					                      </div>
			                        </div>
			                  </div>
					          <div class="ui-widget">
						           <div class="form-group">
										<label class="commlbl col-md-6">Child Product :</label>  
							            <div class="col-md-6"> 
								            <select id="combobox_delete_ChildProd" name="combobox_delete_ChildProd1" class="form-control">
						                    </select>
					                     </div>
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
			<div id="SubProductsList">
			  <div class="col-md-12 no-padding-right">
				  <div class="col-md-3 col-md-offset-6" style="float:right;">
				    <input type="text" class="form-control" id="searchchildproductTableview" placeholder="Search..." style="display:none;"/>
				  </div>
			   </div>			
			   <div class="clearfix"></div>
			<div class="table-responsive "> <!-- recordtbls1 -->			   
				<table id="records_table" border="2" class="table table-bordered pro-table table-new Mrgtop20" cellpadding="2" style="margin-bottom:15px;">
		
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
			
	/* *****************************************************child product auto complete start******************************************************* */
	function populateSite(id) {debugger;
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
		
			var referrer="AddChildProduct.spring";
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