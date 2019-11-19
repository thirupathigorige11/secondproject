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
		<!-- link href="js/jquery-ui.css" rel="stylesheet" type="text/css" /-->
		<link href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link rel="stylesheet" href="css/main.css">
		<link rel="stylesheet" href="css/topbarres.css">
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
		<script type="text/javascript" src="js/JQuery.js"> </script>
		<script type="text/javascript" src="js/ComboBox_Product.js"> </script> 
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
		<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" />

		<style>
			.custom-combobox {position: relative;display: inline-block;width:100%;}
			.custom-combobox-input{width:88% !important;}
			.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;/* support: IE7 */*height: 1.7em;*top: 0.1em;}
			.success,.error {text-align: center;font-size: 14px;}
			.addvendorfrm {max-width: 100%;text-align: left; }
			.border-inwards-box{text-align: left;}
			.form-control, .custom-combobox-input{height:34px;}
			.table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
			.table-new tbody tr td {border-top:0px !important;}
			.pordtxtname{width:100% !important;max-width:100%;}
		</style>
<script type="text/javascript">

$(document).ready(function(){
	
	$("#Product_add").hide();
	$("#ProductList").hide();
	$("#submitbutton").hide();
	$("#Product_delete").hide();	
	$('select#ServiceType').change(function(){
		 $('label.error').hide();
		 $('label.success').hide();
		 var varServiceType = document.getElementById('ServiceType').options[document.getElementById('ServiceType').selectedIndex].value;
	     if(varServiceType == 'add'){
	    	 $("#Product_add").show();
	    	 $("#ProductList").hide();
	    		$("#submitbutton").show();
	    		$("#Product_delete").hide();
	    		 $("#product").val("");
	    		//var AllProducts=$("#AllProducts").val();
	    		
	    		
	    		/* $(function() {
		    		$("#product").autocomplete({

		    			source : function(request, response) { debugger;
		    				$.ajax({
		    					url : 'autoSearchproduct.spring?AllProducts=',
		    					type : "POST",
		    					data : {
		    						term : request.term
		    					},
		    					dataType : "json",
		    					success : function(data) {
		    						response(data);
		    					}
		    				});
		    			}
		    		});
		    	}); */
	    		
	     }else if(varServiceType == 'View'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#Product_add").hide();
	    	 $("#ProductList").show();
	    	 $("#submitbutton").hide();
	    	 $("#Product_delete").hide();
	    	 $('#records_table').val("");
	    	 $("#searchchildproductTableview").hide();
	    	 $('.loader-sumadhura').show();  
	    	// var AllProducts=$("#AllProducts").val();
	    		$.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'viewaddedproduct.spring?AllProducts=',
					//data: {'size':productName},
					success : function(data) {
						//alert("Success");  
						var trHTML = '';
						  var dataLen = data.length;
						//  alert(dataLen);
						  $('#records_table').empty();
						  trHTML += ' <thead class="cal-thead-inwards"><tr><th>S No</th><th>Products</th></tr></thead>';
						  trHTML += '<tbody class="tbl-fixedheader-tbody">';
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<tr><td>' + data[i].serialno + '</td><td>' + data[i].productName + '</td></tr>';
			              }	
			              trHTML += '</tbody>';
			              
			              $('#records_table').append(trHTML);
			              $('#searchchildproductTableview').show();
			              $('.loader-sumadhura').hide();
						},  
					error : function(e) {  
						alert('Error: ' + e);   			
						} 
				});
	     }else if(varServiceType == 'delete'){
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#ProductList").hide();
	    	 $("#submitbutton").show();
	    	 $("#Product_add").hide();
	    	 $("#Product_delete").show();
	    	 $("#product_delete1").val("");
	     }
	     else {
			 $('label.error').hide();
			 $('label.success').hide();
	    	 $("#ProductList").hide();
	    	 $("#submitbutton").hide();
	    	 $("#Product_add").hide();
	    	 $("#Product_delete").hide();
	     }
	});
	});





function formValidator(){
	
	var add = document.getElementById('product').value;

	var del =  document.getElementById('product_delete').value;
	//var productType =  document.getElementById('AllProducts').value;
	debugger;
	
	if((add.trim() == "" || add=="" || add == null ) && (del=="" || del==null))
	{
	alert( "please enter product name " );
	document.myForm.product.focus() ;
	return false;
	}
	/* if(productType == "true"){
		 var dept_Type = document.getElementById('Product_Type').value; 
		 if( dept_Type=="" || dept_Type==null){
			 alert("Please select prodcut type.");
			 $("#Product_Type").focus();
			 return false;
		 }
		} */		if(add!="" && add!=null){
	$('.loader-sumadhura').show();}
	/* if((del == null || del=="" && dept_Type=="" || dept_Type==null)){
		alert( "please  Select Product_Type" );
		return false;
	} */
	if(del != null && del != ""){
		var canISubmit = window.confirm("Do you want to Delete?");
		if(canISubmit == false) {
			return false;
		}
		$('.loader-sumadhura').show();
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
								<li class="breadcrumb-item active">Product Masters</li>
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
						  <div class="addvendorfrm">
							<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 							
							<label class="error"><c:out value="${requestScope['errMessage']}"></c:out> </label> 
						    <c:set value="" var="succMessage"></c:set>
						    <c:set value="" var="errMessage"></c:set>
						    <form name="myForm" action="saveproduct.spring"   onsubmit="return formValidator()"  method="post" class="form-horizontal border-inwards-box">
							 	<div class="col-md-6 col-md-offset-3">	     
								 	<div class="input_div">
							     	  	<div class="form-group">     	  			  		
													<label class="commlbl col-md-6">Service Type :</label>				
												<div class="col-md-6">
													<select id="ServiceType" name="ServiceType" class="form-control ">
									                    <option value="">--Select--</option>
													    <option value="add">Add</option>
													    <option value="View">View</option>
													    <option value="delete">Delete</option>
													</select>
												</div>				
									  	</div>
							      	</div>					
									<div id="Product_add">
										<div class="input_div">
										 	<div class="form-group">
										          		<label class="commlbl col-md-6">Product Name : </label>		      		
										      		<div class="col-md-6">
										         		 <input  type="text"  name="product" id="product" onkeyup="populateprodSite(this)" class="form-control pordtxtname">
										     		 </div>
											</div>
											<%-- <c:if test="${AllProducts}">
												<div class="form-group">   
											          	<label class="commlbl col-md-6">Product Type :</label>		      	
											      		<div class="col-md-6">
											         		  <select name="Product_Type" id="Product_Type" class="form-control">
											         		     <option value="">--SELECT--</option>
											         		     <option value="STORE">STORE</option>
											         		     <option value="MARKETING">MARKETING</option>
											     		      </select>
											     		 </div>
												</div>
											</c:if> --%>
								 		</div>
									</div>
									<div id="Product_delete" >

<div class="ui-widget">
   <div class="form-group">
		<!-- <div class="row"> -->
	 		<!-- <div class="col-sm-4 text-left"> --> 
      			 <label class="commlbl col-md-6">Product Name: </label>
       		<!-- </div> -->
       		<div class="col-md-6">
      <%
          List<Map<String,Object>> totalProductList = (List<Map<String,Object>>)request.getAttribute("totalProductsList");
          String strProductName = "";
          String strProductId = "";
          
        %>
        
        <select id="product_delete" name="product_delete1" class="form-control pordtxtname">
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
        
       <!--  </div> -->
    </div>
    </div>
    
    <%-- <c:if test="${Delete}">
    <div class="form-group"">
	     		<div class="row">
		      		<div class="col-sm-4 text-left">
		          		<label class="commlbl">Product Type : </label>
		      		</div>
		      		<div class="col-sm-6">
		         		  <select name="delete_Product_Type" id="Delete_Product_Type" class="form-control" onchange="getSiteRelatedProducts()"><option value="">--SELECT--</option>
		         		  <option value="STORE">STORE</option><option value="MARKETING">MARKETING</option>
		     		</select>
		     		
		     		 </div>
		 		</div>
			</div>
			<div class="form-group">
	     		<div class="row">
		      		<div class="col-sm-4 text-left">
		          		<label class="commlbl">Product Name:  : </label>
		      		</div>
		      		<div class="col-sm-6">
		         		<select id="product_delete" name="product_Delete" class="form-control pordtxtname">
        					<option value=""></option>
        			    </select>
		     		
		     		 </div>
		 		</div>
			</div>
			</c:if> --%>

    </div>
										<div id="submitbutton" class="col-md-12 text-center center-block">
									    <input type="submit" value="Submit" id="submitFormData" class="btn btn-warning Mrgtop20" name="Submit">
									</div>
	                           </div>
	                       </form>
	                   </div>
                  </div>
				<div id="ProductList">
				     <div class="col-md-12 no-padding-right">
						  <div class="col-md-3 col-md-offset-6" style="float:right;">
						    <input type="text" class="form-control" id="searchchildproductTableview" placeholder="Search..." style="display:none;"/>
						  </div>
					   </div>			
			         <div class="clearfix"></div>
					<div class="table-responsive">  <!-- recordtbls1 -->
						<table id="records_table" class="table table-striped table-bordered table-new Mrgtop20" border="2"  cellpadding="2">
						   
						 </table>
					</div>
				 </div>
                <%--  <input type="hidden" name="AllProducts" id="AllProducts" value="${AllProducts}"> --%>
               </div>
			</div>
		</div>		
		<script src="js/jquery.dataTables.min.js"></script>
	    <script src="js/dataTables.bootstrap.min.js"></script>	
		<script src="js/custom.js"></script>
		<script src="js/sidebar-resp.js"></script>
		<script>
			$(document).ready(function() {$(".up_down").click(function(){ 
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
			function getSiteRelatedProducts(){debugger;
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
			            	  trHTML += '<option value=' + data[i].PRODUCT_ID + '@@'  + data[i].NAME + '>'+  data[i].NAME + '</option>' ;
			              }	
			              
			              $('#product_delete').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}
			
			
			
			function populateprodSite(id) {debugger;
			  //var Product=$("#product").val();
			  //var Product_Type=$("#Product_Type").val();
			 var prod=$("#product").val();
			//var measurement=$("#measurement").val();
			
			  var url = "getProductforAutoComplete.spring?product="+prod;

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
				  $("#product").autocomplete({
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
			
			
		</script> 

</body>
</html>