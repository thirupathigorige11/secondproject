<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<!-- <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">-->
 <style>
 .error {
    color: red; display:block; padding-top:5px; font-size: 12px;
}
.success {
    color: #5e9641;; display:block; padding-top:5px; font-size: 12px;
}

/*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width: 200px !important;text-align: center}
 .tblprodindissu thead tr th:nth-child(2),.tblprodindissu tbody tr td:nth-child(2){width: 400px !important;text-align: center}
 .tblprodindissu thead tr th,.tblprodindissu tbody tr td{text-align: center; }
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/
/* use in fixed   header*/
.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 17px ) !important;
}
@media only screen and (min-width:320px) and (max-width:1024px){
	.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 0px ) !important;
}
}
@media only screen and (min-width:1440px){
	.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 17px ) !important;
}
.tbl-width-medium{
	width:100%;
}
}
.tbl-fixedheader-tbody{
 display: inline-block; 
 max-height: 350px !important; 
 overflow-y: scroll;
 overflow-x: auto;
}
input[type="checkbox"]{
  width: 23px; /*Desired width*/
  height: 18px; /*Desired height*/
}
.checkboxlable{
	padding: 2% 10% 2% 10%;
    text-align: left;
    vertical-align: middle;
    display: flex;
}
.checkboxtext{
	margin-top:2%;
}
input[type="checkbox"] {
    width: 35px;
    height: 25px;
}
@media only screen and (min-width:320px) and (max-width:768px){
	.colonclass{
		display: none;
	}
	.padding-t-10{
		padding-bottom: 10px;
	}
}
.ui-autocomplete{
	width: 10%;
}
.btn-check{
	border-radius:50%;
	background-color:#008000;
	height:50px;
	width:50px;
	border:1px solid #008000;
	}
	.btn-check i{
	font-size: 25px;	
	color: #fff;
	}
	.btn-warning1{
	border-radius:50%;
	background-color:red;
	height:50px;
	width:50px;
	border:1px solid red;
	}
	.fa-warning i{
	font-size: 25px;	
	color: #fff;
	}
 </style>
 <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<script type="text/javascript">

function validateForm() {
    var x = document.forms["myForm"]["vendor"].value;
    if (x == "") {
        alert("Name must be filled out");
        return false;
    }
}

$(document).ready(function(){
	//search data from table
	   $("#myInput").on("keyup", function() {
		    var value = $(this).val().toLowerCase();
		    $("#records_table tr").filter(function() {
		      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
		    });
	 	});
		
	$("#l_mastres").addClass('active');
	$("#l_vendors").addClass('current-page');
	$('label.error').hide();
	$('label.success').hide();
	$("#addData").hide();
	$("#viewTable").hide();
	$("#Vendor_delete").hide();
	$("#submitbutton").hide();
	$("#myInput").hide();
	$("#mobileView").hide();
	 
	$('select#ServiceType').change(function(){debugger;

		 var varServiceType = document.getElementById('ServiceType').options[document.getElementById('ServiceType').selectedIndex].value;
	     if(varServiceType == 'add'){
	    		$('label.error').show();
				 $('label.success').show();
				 $("#addData").show();
		    	 $("#viewTable").hide();
		    	 $("#myInput").hide();
		    	 $("#mobileView").hide();
		    	 $("#Vendor_delete").hide();
		    	 $("#submitbutton").show();
	    	$(function() {
	    		$("#vendor").autocomplete({

	    			source : function(request, response) {
	    				$.ajax({
	    					url : "autoSearchvendor.spring",
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
	    	});

	    	
	    	
	    	
	    	
	     }
	     if(varServiceType == 'View'){debugger;
	    		$('label.error').hide();
				 $('label.success').hide();
				 $("#addData").hide();
		    	 $("#Vendor_delete").hide();
		    	 $("#submitbutton").hide();
		    	 
		    	 var screenwidth=screen.width;
	    	$(".loader-sumadhura").show();
	    	 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'ViewVendor.spring',
					success : function(data) {
						var trHTML = '';
						  var dataLen = data.length;
						  $('#records_table').empty();
						  if(screenwidth>1023){
							  for (i=0; i<dataLen; i++) {
					             	trHTML += "<tr><td style= 'text-decoration: underline;' ><a  href='getVendorDetails.spring?vendor_Id="+data[i].vendor_Id+"'>"+data[i].vendor_name+"</a></td><td>"+data[i].address+"</td><td>"+data[i].mobile_number+"</td><td>"+data[i].date+"</td></tr>";
					          }
							  $('#records_table').html(trHTML);
				              $("#viewTable").show();
				              $("#myInput").show();
						  }else{
							  	
							  for (i=0; i<dataLen; i++) {
								  trHTML+='<table  class="table table-bordered table-striped">';	           
								  trHTML+='<tbody>';
								  trHTML += "<tr><td style='font-weight: bold;'>Vendor Id</td><td style= 'text-decoration: underline;' ><a  href='getVendorDetails.spring?vendor_Id="+data[i].vendor_Id+"'>"+data[i].vendor_name+"</a></td></tr>";
								  trHTML += "<tr><td style='font-weight: bold;'>Address</td><td style='width:69%;word-break:break-all;'>"+data[i].address+"</td></tr>";
								  trHTML += "<tr><td style='font-weight: bold;'>Mobile Number</td><td style='width:69%;word-break:break-all;'>"+data[i].mobile_number+"</td></tr>";
								  trHTML += "<tr><td style='font-weight: bold;'>Vendor Date Of Inclusion</td><td style='width:69%;word-break:break-all;'>"+data[i].date+"</td></tr>";								  
							  }
							  $("#mobileView").html(trHTML);
							  $("#mobileView").show();
						  }						  			            
			              $(".loader-sumadhura").hide();
						},  
					error : function(e) {  
						 $(".loader-sumadhura").hide();
						alert('Error: ' + e);   
						} 
				});
	    	 
	    	 
	    	 
	     }
	     if(varServiceType == 'delete'){debugger;
	    	$('label.error').show();
			 $('label.success').show();
			 $("#addData").hide();
	    	 $("#viewTable").hide();
	    	 $("#myInput").hide();
	    	 $("#mobileView").hide();
	    	 $("#Vendor_delete").show();
	    	 $("#submitbutton").show();
	    	
	    	$(function() {
	    		$("#VendarName_Delete").autocomplete({

	    			source : function(request, response) {
	    				$.ajax({
	    					url : "autoSearchvendor.spring",
	    					type : "POST",
	    					data : {
	    						term : request.term
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
	    	
	     }
	     if(varServiceType==""){debugger;
	     	 $('label.error').hide();
		 	 $('label.success').hide();
	    	 $("#addData").hide();
	    	 $("#viewTable").hide();
	    	 $("#Vendor_delete").hide();
	    	 $("#myInput").hide();
	    	 $("#submitbutton").hide();
	     }
	});
	});
	

	 function validate(){
		
		 
		 var ServiceType = document.getElementById('ServiceType').value;
		
		 if (ServiceType == 'delete') {
			 var VendarName_Delete =  document.getElementById('VendarName_Delete').value;
			 if(VendarName_Delete=='' || VendarName_Delete==null){
		     alert( "please enter vendor name!" );
			return false;
			}
		 }  else {
			 var vendoradd =  document.getElementById('vendor').value;
			 if(vendoradd=='' || vendoradd==null){
		     alert( "please enter vendor name!" );
			return false;
			}
			 var ads = document.getElementById('address').value;
			 if((ads == "" || ads == null)){
				 alert( "please enter adress" );
					return false;		 
			 }
			
			 
			 
			 var mobilenumber = document.getElementById('mobilenumber').value;

			    if (mobilenumber == "" || mobilenumber == null) {
			            alert("please enter your mobile no.");
			            return false;
			        }
			        if (mobilenumber.length < 10 || mobilenumber.length > 12) {
			            alert("mobile no. is not valid, please enter 10 or 12 digit mobile no.");
			            return false;
			        }

			        
			        
			 
			 var gsin = document.getElementById('gsinnumber').value;
			 if((gsin == "" || gsin == null)){
				 alert( "please enter gstin number" );
					return false;
			 }
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
		<div class="right_col" role="main">
			<div>
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a href="#">Masters</a></li>
					<li class="breadcrumb-item active">Vendor Master</li>
				</ol>
			</div>
			<div class="loader-sumadhura" style="display: none;z-index:999;">
				<div class="lds-facebook">
					<div></div><div></div><div></div><div></div><div></div><div></div>
				</div>
				<div id="loadingMessage">Loading...</div>
			 </div>
			<div align="center" >
			<div class="" style="background-color: #ccc;">
				<br>
				<label class="error" style="font-size: 20px;margin-left: 130px;"></label>
				<label class="success" style="font-size: 20px;margin-left: 130px;"></label>
				<form name="myForm" action=""  enctype="multipart/form-data" method="" >
					<div class="form-group col-md-6 col-sm-12 col-xs-12 padding-t-10">
					     <div class="input_div">
					    	 <div class="row">
					    	 	<div class="col-md-5 col-sm-11 text-left">
									<label class="commlbl">ServiceType</label>
								</div>
								<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
								<div class="col-md-6 col-sm-12">
									<select id="ServiceType" name="ServiceType" class="form-control">
						                <option value="">select</option>
									  <!--   <option value="add">add</option> -->
									    <option value="View">View</option>
									    <option value="delete">delete</option>
									</select>
								</div>
						    </div>
					     </div>
					</div>
					<div id="Vendor_delete" class="col-md-6 col-sm-12 col-xs-12" style="margin-bottom: 20px;">
					      <div class="input_div">
					      	<div class="form-group">
						      	<div class="row">
						    	  <div class="col-md-5 col-sm-11 text-left">
					             	 <label class="commlbl"> Vendor Name </label>
					              </div>
					              <div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					              <div class="col-md-6 col-sm-12">
					             	 <input type="text" name="VendarName_Delete" placeholder="Vendor Name" id="VendarName_Delete" class="form-control" autocomplete="off">
					              </div>
					              </div>
					          </div>
					     </div>
					</div>
					<div class="clearfix"></div>
					<span id="addData">
					<div id="Vendor_add" class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> Vendor Name  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div class="col-md-6 col-sm-12">
					              		<input type="text" id="vendor" name="vendor" placeholder="Vendor Name"  class="form-control">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					
					<div id="Address_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					         <div class="input_div">
					         	<div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					              		<label class="commlbl"> Address </label>
					              	</div>
					              	<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					                <div class="col-md-6 col-sm-12">
					              		<input type="text" id="address" name="address" placeholder="Address" class="form-control">
					              	</div>
					              </div>
					         </div>
					    </div>
					</div>
					
					<div id="MobileNumber_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					         <div class="input_div">
					         	<div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					              		<label class="commlbl"> Mobile Number  </label>
					            	</div>
					            	<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					            	<div class="col-md-6 col-sm-12">
					              		<input type="text" id="mobilenumber" name="mobilenumber" placeholder="Mobile Number" class="form-control" maxlength="12" autocomplete="off">
					              	</div>
					              </div>
					         </div>
					    </div>
					</div>
					
					<div id="GSIN_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="input_div">
					         <div class="form-group">
					         	<div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					              		<label class="commlbl"> GSTIN Number </label>
					              	</div>
					              	<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					              	<div class="col-md-6 col-sm-12">
					              		<input type="text" id="gsinnumber" name="gsinnumber" placeholder="GSTIN Number" class="form-control" autocomplete="off">
					              	</div>
					         	</div>
					    	</div>
						</div>
					</div>
					<div id="state_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> Vendor State </label>					          			
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div class="col-md-6 col-sm-12">
					          			<select id="state_add" name="state" class="form-control">
							                 <option value="">select</option>
										    <option value="Andaman and Nicobar Islands 35 AN">Andaman and Nicobar Islands 35 AN</option>
										    <option value="Andhra Pradesh">Andhra Pradesh</option>
										    <option value="Andhra Pradesh (New) ">Andhra Pradesh (New)</option>
										    <option value="Arunachal Pradesh">Arunachal Pradesh</option>
										    <option value="Assam">Assam</option>
										    <option value="Bihar">Bihar</option>
										    <option value=Chattisgarh>Chattisgarh</option>
										    <option value="Dadra and Nagar Haveli ">Dadra and Nagar Haveli</option>
										    <option value="Daman and Diu">Daman and Diu</option>
										    <option value="Delhi">Delhi </option>
										    <option value="Goa">Goa</option>
										    <option value="Gujarat">Gujarat</option>
										    <option value="Haryana ">Haryana</option>
										    <option value="Himachal Pradesh">Himachal Pradesh</option>
										    <option value="Assam">Assam</option>
										    <option value="Jammu and Kashmir">Jammu and Kashmir</option>
										    <option value="Jharkhand">Jharkhand</option>
										    <option value="Karnataka ">Karnataka</option>
										    <option value="Kerala">Kerala</option>
										    <option value="Lakshadweep Islands">Lakshadweep Islands </option>
										    <option value="Madhya Pradesh">Madhya Pradesh</option>
										    <option value="Maharashtra">Maharashtra</option>
										    <option value="Manipur ">Manipur</option>
										    <option value="Meghalaya">Meghalaya</option>
										    <option value="Mizoram">Mizoram</option>
										    <option value="Nagaland">Nagaland</option>
										    <option value="Odisha">Odisha</option>
										    <option value="Pondicherry ">Pondicherry</option>
										    <option value="Punjab">Punjab</option>
										    <option value="Rajasthan">Rajasthan </option>
										    <option value="Sikkim">Sikkim</option>
										    <option value="Tamil Nadu ">Tamil Nadu</option>
										    <option value="Telangana ">Telangana</option>
										    <option value="Tripura">Tripura</option>
										    <option value="Uttar Pradesh">Uttar Pradesh</option>
										    <option value="Uttarakhand">Uttarakhand</option>
										    <option value="West Bengal">West Bengal</option>
										</select>
					              		<!-- <input type="text" id="state" name="state" class="form-control">-->
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>

					<!-- ********** More feilds for add vendor**************** -->
					<div class="clearfix"></div>
					
					<div id="business_add" class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div class="col-md-12 col-sm-12 text-left">
					          			<label class="commlbl">Type of Business (Mark one Only) </label>
					          			
					          		</div>
					          		<div class="col-md-12 col-sm-12">
					          			<div class="col-md-6 col-sm12 checkboxlable">
					          				 <input type="checkbox" class="checkedclass1" name="typeOfBusiness" value="Proprietorship"/><span class="checkboxtext">Proprietorship</span>  
					          			</div>
					          			<div class="col-md-6 col-sm12 checkboxlable">
					          				<input type="checkbox"  class="checkedclass1" name="typeOfBusiness" value="Partnership" /><span class="checkboxtext">Partnership</span>  
					          			</div>
					          			<div class="col-md-6 col-sm12 checkboxlable">
					          				 <input type="checkbox" class="checkedclass1" name="typeOfBusiness" value="Corporate"/> <span class="checkboxtext">Corporate/Limited </span>
					          			</div>
					          			<div class="col-md-6 col-sm12 checkboxlable">
					          				<input type="checkbox" class="checkedclass1" name="typeOfBusiness" value="privateltd"/><span class="checkboxtext">Private Ltd.</span> 
					          			</div>
						           		<div class="col-md-12 checkboxlable">
						           			<input type="checkbox"  class="checkedclass1" name="others" /><span class="" style="margin-top: 1%;">Others (Specify)&nbsp;&nbsp;</span> <span><input type="text" name="typeOfBusiness" value=""/></span>
						           		</div>
					 				 </div>
					              </div>
					          </div>
					     </div>
					</div>
					
					<div id="business_nature" class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div class="col-md-12 col-sm-12 text-left">
					          			<label class="commlbl">Nature of Business (Enclose Company Profile) </label>
					          			
					          		</div>
					          		<div class="col-md-12 col-sm-12">
						          		<div class="col-md-6 col-sm12 checkboxlable">
						          			<input type="checkbox" class="checkedclass2"name="Manufacturer" value="Manufacturer"/><span class="checkboxtext">Manufacturer</span>
						          		</div>
						          		<div class="col-md-6 col-sm12 checkboxlable">
						          			 <input type="checkbox" class="checkedclass2" name="Authorized" value="Authorized" /> <span class="checkboxtext">Authorized Agent</span>
						          		</div>
						          		<div class="col-md-6 col-sm12 checkboxlable">
						          			<input type="checkbox"  class="checkedclass2" name="Trader" value="Trader"/><span class="checkboxtext">Trader</span>
						          		</div>
						          		<div class="col-md-6 col-sm12 checkboxlable">
						          			<input type="checkbox" class="checkedclass2" name="Consulting"  value="Consulting" /><span class="checkboxtext">Consulting Company</span>
						          		</div>
						          		<div class="col-md-12 col-sm12 checkboxlable">
						          			<input type="checkbox" class="checkedclass2" name="Others" /><span class="" style="margin-top: 1%;">Others (Specify)&nbsp;&nbsp;</span><span><input type="text" name="natureOfBusiness" value=""/></span>
						          		</div>       
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div class="clearfix"></div>
					<div id="email_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> Vendor Email  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="email" name="email" placeholder="Vendor Email" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="landnumber_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> LandLine Number  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="landnumber" name="landnumber" placeholder="LandLine Number" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="statecode_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> State Code  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="statecode" name="statecode" placeholder="State Code" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="vendorcontactperson_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> VendorContactPersonName  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="vendorcontactperson" placeholder="Contact Person Name" name="vendorcontactperson" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="vendorContactPerson2_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> VendorContactPersonName_2 </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="vendorContactPerson2" placeholder="Contact Person Name 2" name="vendorContactPerson2" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="vendorMobileNumber2_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> VendorMobileNumber_2 </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="vendorMobileNumber2" placeholder="Vendor Mobile Number" name="vendorMobileNumber2" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div class="clearfix"></div>
					<div id="panNumber_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl">Pan Number  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="panNumber" placeholder="Pan Number" name="panNumber" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="accountNumber_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> Account Number  </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="accountNumber" placeholder="Account Number" name="accountNumber" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="accountHolderName_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl">Account Holder Name </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="accountHolderName" placeholder="Account Holder Name" name="accountHolderName" autocomplete="off" class="form-control">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="accountType_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl">Account Type</label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="accountType" placeholder="Account Type" name="accountType" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="bankName_add"  class="col-md-6 col-sm-12 col-xs-12">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl">Bank Name </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="bankName" placeholder="Bank Name" name="bankName" class="form-control">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					<div id="ifscCode_add"  class="col-md-6 col-sm-12 col-xs-12" style="margin-bottom: 20px;">
					     <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">
					    	 		<div  class="col-md-5 col-sm-11 text-left">
					          			<label class="commlbl"> Ifsc Code </label>
					          		</div>
					          		<div class="col-md-1 col-sm-1 commlbl colonclass">:</div>
					          		<div  class="col-md-6 col-sm-12">
					              		<input type="text" id="ifscCode" placeholder="Ifsc Code" name="ifscCode" class="form-control" autocomplete="off">
					              	</div>
					              </div>
					          </div>
					     </div>
					</div>
					
					<div class="clearfix"></div>
					<div id="image_add" style="margin-left: 20px;">
					     <!-- <div class="form-group">
					          <div class="input_div">
					          	 <div class="row">					    	 		
					          		<div class="file-upload" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
										<input type="file" name="file"><br><input type="file" name="file"><br>
										<input type="file" name="file"><br><input type="file" name="file"><br>
										<input type="file" name="file"><br><input type="file" name="file"><br>
										<input type="file" name="file"><br><input type="file" name="file"><br>
						
									</div>						
					              </div>
					          </div>
					     </div> -->
					     <div class="col-md-12" style="margin-top: 50px;margin-bottom: 50px;">						
						     <div class="floatLeft text-left">
							  <h4 class="text-left Mrgtop20"><strong>You can upload Images/PDF(s) here </strong>(<span style="font-size: 14px;"> Note :  Now you can upload upto 8 file's</span>)</h4>
							  <P ><strong>Maximum File Upload Size: 1MB</strong></P>
							  <div class="files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;margin-bottom: 30px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <!-- <i class="fa fa-plus"></i>  -->
								<div class="col-md-12">
								 <div class="market_file_style">
								  <div class="clearfix"></div>
								 </div>
								</div>
								<input type="file" id="file" name="file" style="display:none">
							 </div>
					</div>
					</div>
					</span>
					
					<!-- <h3>You can upload Vendor Registration Form here</h3>-->
					
					



					<div id="submitbutton" class="col-md-12 col-sm-12 col-xs-12 text-center center-block" style="margin-bottom: 30px;margin-top: 30px;">
						<button type="button" id="Submit" class="btn smal btnname btn btn-warning btn-lg" onclick="deleteVendor()">Submit</button>
					</div>
				</form>

			</div>
			<br></br>
			<div id="VendorList">
			
			</div>

		 
 		</div>
 		<div id="webView">
 			<input id="myInput" type="text" class="form-control"  placeholder="Search.." style="width: 24%;float:right;margin-bottom:20px;margin-right: 17px;">
 			<table class="table table-bordered tblprodindissu" style="margin-bottom: 30px;display:none;" id="viewTable" border="2" cellpadding="2">
		    <thead class="cal-thead-inwards">
		    	<tr>
			    	<td style="width:200px;text-align: center;">Vendor Id</td>
			    	<td style="width: 400px;text-align: center;">Address</td>
			    	<td style="text-align: center;">Mobile Number</td>
			    	<td style="text-align: center;">Vendor Date Of Inclusion</td>
		    	</tr>
		    </thead>
		    <tbody id="records_table" class="tbl-fixedheader-tbody">
		    
		    </tbody>
		 </table>
 		</div>
 		<div id="mobileView">
 		
 		</div> 		
 		<div id="deleteModal" class="modal fade" role="dialog">
		  <div class="modal-dialog" style="margin-top:10%;">
		    <div class="modal-content">
		      <div class="modal-body">
		       	<div id="success">
		       		<div class="col-md-12 text-center center-block" style="margin-top: 30px;">
						<button class="btn btn-check"><i class="fa fa-check" aria-hidden="true"></i></button>
					</div>
					<p style="text-align: center;font-size: 24px;color: green;padding-top: 100px;">Vendor has been deleted.</p>
		       	</div>
		       	<div id="error">
		       		 <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
						<button class="btn btn-warning1"><i class="fa fa-warning" aria-hidden="true"></i></button>
					 </div>
					 <p style="text-align: center;font-size: 24px;color: red;padding-top: 100px;">Vendor deleting has been failed.</p>
		       	</div>
		      </div>
		      <div class="modal-footer" style="text-align: center !important;">
		        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
		      </div>
		    </div>		
		  </div>
		</div>	
 	</div>
 </div>
 </div>
 </div>

	<link href="js/inventory.css" rel="stylesheet" type="text/css" />
	<script src="js/custom.js"></script>
  	<script>
		
	  	$('input.checkedclass1').on('change', function() {
		    $('input.checkedclass1').not(this).prop('checked', false);  
		});
	  
		$(document).ready(function() {	
			$(".up_down").click(function(){ 
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			}); 
		});
		function uploadFile(){
			var classlength=$(".selectCount").length;
			 if(classlength>7){
				alert("You can't upload more than eight files");
				return false;
			}
			addFile();
		    var id=$(".selectCount:last").attr("id").split("file_select")[1];
			$("#file_select"+id).trigger("click");		
		}
	    //ellipse's  for file name if you get morethan 25 letters in uploaded file 
		function start_and_end(str) {
			if (str.length > 25) {
			   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
			}
		  return str;
		}
		//first file change 
		$(".selectCount").bind("change", function() {debugger;
			var id=$(this).attr("id").split("file_select")[1];
			if($(this).val()==''){		
				$("#market_file_style"+id).remove(); 
			}
			if((this.files[0].size/1024)<=1024){
				$('#close_btn'+id).show();
				$("#fileName"+id).text(this.files[0].name);
			}else{
				alert("Please Upload Below 1 mb Pdf File");		
				$("#market_file_style"+id).remove(); 
			}
			//scoll to bottom 
			window.scrollTo(0,document.querySelector("body").scrollHeight);
			console.log(document.querySelector("body").scrollHeight);
		}); 
		//from second file onwards 
		function fileChange(id, current){ debugger;
		 	if($(current).val()==''){
		 		$("#market_file_style"+id).remove(); 		
			}
			if((current.files[0].size/1024)<=1024){
				$('#close_btn'+id).show();
				$("#fileName"+id).text(start_and_end(current.files[0].name));
			}else{
				alert("Please Upload Below 1 mb Pdf File");
				$("#market_file_style"+id).remove(); 		
			}	
			//scoll to bottom 
			window.scrollTo(0,document.querySelector("body").scrollHeight);
			console.log(document.querySelector("body").scrollHeight);
		}
		//adding file 
		function addFile(){
			var classlength=$(".selectCount").length;
			if(classlength!=0){
				var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
				if($("#file_select"+classLastId).val()==""){
				 $("#market_file_style"+classLastId).remove(); 			
				}
			}else{
				var classLastId=0;
			}		
			var dynamicId = parseInt(classLastId) + 1;
			 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style" id="market_file_style'+dynamicId+'"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" onchange="fileChange('+dynamicId+', this)" class="selectCount" style="display:none;"/><div class=""><span id="fileName'+dynamicId+'"  style="float:left;padding: 5px 10px 5px 0px;width:200px;"></span><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;padding: 2px 5px;margin-top: 5px;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
		}
		//delete file
		function filedelete(id){
			var canIDelete=window.confirm("Do you want to delete file?");
			if(canIDelete == false ){
				return false;
			}
			$("#market_file_style"+id).remove();
		}
		function deleteVendor(){
			 var VendarName_Delete = $('#VendarName_Delete').val();
			 if(VendarName_Delete=='' || VendarName_Delete==null){
		     	alert( "please enter vendor name!" );
		     	$('#VendarName_Delete').focus()
				return false;
			 }
			 var url = "deleteVendor.spring?VendarName_Delete="+VendarName_Delete;
			 $.ajax({
				  url : url,
				  type : "post",
				  contentType : "application/json",
				  success : function(data) {debugger;
						if(data=="true"){
							$("#VendarName_Delete").val('');
							$(".modal").modal();
							$("#success").show();
							$("#error").hide();
						}else{
							$(".modal").modal();
							$("#success").hide();
							$("#error").show();
						}
				  }
			});
		}
	 </script> 
</body>
</html>