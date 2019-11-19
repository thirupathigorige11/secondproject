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
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" href="css/main.css">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>


<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transportor Registration</title>
<link rel="shortcut icon" href="https://www.sumadhuragroup.com/wp-content/themes/constructo/images/favicon" type="image/x-icon">
<script type="text/javascript" src="js/JQuery.js"> </script>
<script type="text/javascript" src="js/ComboBox_Product.js"> </script> 
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
<link type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" />

<style>
.Myclass{
	font-weight: bold;
    font-size: 16px;
    margin-top: -7px;
    position: absolute;
    margin-left: 25px;
}
.custom-combobox {
position: relative;
display: inline-block;
}
.custom-combobox-toggle {
position: absolute;
top: 0;
bottom: 0;
margin-left: -1px;
padding: 0;
/* support: IE7 */
*height: 1.7em;
*top: 0.1em;

}
.success,.error {
	text-align: center;
	font-size: 14px;
}
</style>
<script type="text/javascript">

if (typeof(Storage) !== "undefined") {
       var i=parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
       if(i==2){
    	   sessionStorage.setItem("${UserId}tempRowsIncre12",1);
           window.location.reload();
       }
    } else {
       alert("Sorry, your browser does not support Web Storage...");
    };

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
							<li class="breadcrumb-item active">Transporter Registration</li>
						</ol>
					</div>			
	<div align="center" class="contractor-page-content">
	<div align="center"><h1>Transporter Registration  </h1> </div>
	
	<div class=""> <!-- addvendorfrm -->
	<label class="success" ><c:out value="${requestScope['succMessage']}"></c:out> </label> 
	<label class="error"><c:out value="${requestScope['errMessage']}"></c:out> </label> 


<form:form action="saveTransportorDetails.spring" modelAttribute="TransportorBean"  enctype="multipart/form-data" id="myForm" name="myForm" method="POST" class="form-horizontal">
	
	<div class="col-md-12">
	<div class="col-md-6" style="text-align:left;">
	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> First Name<span class="astrik-imp"><sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
	     <div class="col-md-6">
	      <form:input path="contractor_id"  type="hidden" class="form-control"/>
	      <form:input path="contractor_name" type="hidden"/>
	   	  <form:input path="first_name"  type="text" id="first_name" class="form-control" onkeyup="populateContractor(1);" required="true" autocomplete="off"/>
	     </div>
 	</div>
 		 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Last Name<span class="astrik-imp"><sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
	     <div class="col-md-6">
	      <form:input path="contractor_id"  type="hidden" class="form-control"/>
	    <form:input path="last_name"  type="text" id="last_name" onblur="return nameValidation();" onkeyup="populateContractor(2);" autocomplete="off" class="form-control" required="true"/>
	     </div>
 	</div>
 
 	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Address <span class="astrik-imp"> <sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
	     <div class="col-md-6"><form:input path="address"   type="text"  id="address" autocomplete="off" class="form-control" required="true"/></div>
 	 </div>
 	 
 	 <c:if test="${empty ContractorBean.contractor_id}">
		<div class="form-group">
		    <label for="inputdefault" class="col-md-5"> Contact Number<span class="astrik-imp"><sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
		    <div class="col-md-6"> <form:input path="mobile_number" id="mobile_number" autocomplete="off" type="text" class="form-control" required="true"/></div>
 		 </div>
	</c:if>
	 <c:if test="${!empty ContractorBean.contractor_id}">
			<div class="form-group">
		    <label for="inputdefault" class="col-md-5"> Contact Number<span class="astrik-imp"><sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
		     <div class="col-md-6"><form:input path="mobile_number" id="mobile_number1" autocomplete="off" type="text" class="form-control" required="true"/></div>
 		 </div>
	</c:if>
	
	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Alternate Contact Number </label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6"> <form:input path="alternate_mobile_number" autocomplete="off" id="alternate_mobile_number"  type="text" class="form-control"/></div>
 	 </div>
	
	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Landline Number </label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6"> <form:input path="landline_number" autocomplete="off" id="landline_number"  type="text" class="form-control"/></div>
 	 </div>
	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> E-mail</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6"> <form:input path="email" id="email"  type="text" autocomplete="off" class="form-control"/></div>
 	 </div>
 	   <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> PAN Number</label><span class="col-md-1"><strong>:</strong></span>
	     <div class="col-md-6"><form:input path="pan_number" autocomplete="off" id="panNumber"  type="text" class="form-control" /></div>
 	 </div>
 	
 	 <div class="file-upload" style="float: left;  color: orange;font-size:  14px;margin-left: 33px;margin-top: 2px;font-weight: bold">
		
		<div class="col-md-12" style="margin-top: 50px;">						
			<div class="floatLeft">
				<h4 class="text-left Mrgtop20"><strong>You can upload Images/PDF(s) here </strong>(<span style="font-size: 14px;"> Note :  Now you can upload upto 8 file's</span>)</h4>
				<P><strong>Maximum File Upload Size: 1MB</strong></P>
				<div class="col-md-12 files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <!-- <i class="fa fa-plus"></i>  -->
				<div class="col-md-12">
				<div class="market_file_style">
				<div class="clearfix"></div>
				</div>
				</div>
				<input type="file" id="file" name="file" style="display:none">
			</div> 
		</div>			
			
 		 <!-- <div class="col-md-6">
	 		 <input style="margin-top: 5px;"  type="file" name="file"   accept='image/*' ><br>
	 		 <input style="margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
			 <input style="margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
			 <input style=" margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
		</div>
		<div class="col-md-6">
		    <input style=" margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
			<input style="margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
			<input style=" margin-top: 5px;" type="file" name="file"   accept='image/*' ><br>
			<input style=" margin-top: 5px;margin-bottom: 2px;"" type="file" name="file"   accept='image/*' ><br>
		</div> -->

	
 	  
 	 </div>
	
 	<c:if test="${!empty ContractorBean.contractor_id}">
		<div class="col-md-12 text-center center-block">
		 <input type="submit" name="submitBtn" class="form-control btn btn-primary" id="updateBtn"	value="<spring:message text="Update"/>" />
		</div>
	</c:if>
</div>
	
	<div class="col-md-6" style="text-align:left;">
	  <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> GSTIN<span class="astrik-imp"><sup>*</sup></span></label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="GSTIN" type="text" id="gstin" autocomplete="off" class="form-control" /></div>
 	 </div>
	
	  <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Bank Name</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="bank_name"  type="text" autocomplete="off" class="form-control" /></div>
 	 </div>
 	
	<div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Bank Acc Number</label><span class="col-md-1"><strong>:</strong></span>
	     <div class="col-md-6"><form:input path="bank_acc_number" autocomplete="off"  type="text" class="form-control" /></div>
 	 </div>
 	 
 	  <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Account type</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="account_type"  type="text" autocomplete="off"  class="form-control" /></div>
 	 </div>
 	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> Branch Name</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="branch_name"  type="text" autocomplete="off"  class="form-control" /></div>
 	 </div>
	<div class="form-group">
	    <label for="inputdefault" class="col-md-5"> IFSC Code</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="ifsc_code"  type="text" autocomplete="off"  maxlength="11" minlength="11" class="form-control" /></div>
 	 </div>
 	<div class="form-group">
	    <label for="inputdefault" class="col-md-5"> State Name</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="state_name"  type="text" autocomplete="off"  class="form-control" /></div>
 	 </div>
 	 <div class="form-group">
	    <label for="inputdefault" class="col-md-5"> City Name</label><span class="col-md-1"><strong>:</strong></span>
	    <div class="col-md-6">
	     <form:input path="city_name"  type="text" autocomplete="off"  class="form-control" /></div>
 	 </div>
 	 
	 
 	 
	
 
		<c:if test="${!empty ContractorBean.contractor_id}">
				<div class="col-md-12 text-center center-block">
				 <input type="submit" name="submitBtn" class="form-control btn btn-info"	value="<spring:message text="Delete"/>" />
				</div>
		</c:if>
		
	</div>
	<c:if test="${empty ContractorBean.contractor_id}">
			<div class="form-group">
			     <!-- <label for="inputdefault" class="col-md-6">&emsp;	</label> -->
			     <div class="col-md-12"><input type="submit" id="submitBtn" name="submitBtn" onclick="return validateForm()" class="btn btn-warning" value="Submit"/>
 			 </div>
			     </div>
		</c:if>
	</div>
	
<div> </div>
</form:form>
</div>
</div>

 </div>
</div>
</div>
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
	
<script src="js/custom.js">
</script>

<script>
  
function validateForm() {
	debugger;
	
	 var result=validateFileExtention();
	 if(result==false){
		 return false;
	 }
	var first_name = $("#first_name").val();
	var last_name = $("#last_name").val();
	var address = $("#address").val();
	var mobile_number = $("#mobile_number").val();
	var alternate_mobile_number = $("#alternate_mobile_number").val();

	var mobReges = /^[6-9][0-9]{9}$/;
	var mobRegex2 = /^[+][0-9]{2}[6-9][0-9]{9}$/;
	var nameRegex = /^[a-zA-z]+$/;
	var checkWhiteSpace = new RegExp("/^\s+$/");

	var index = first_name.indexOf(' '); // 4

	if (first_name.length == 0) {
		alert("Please enter First name.");
		$("#first_name").focus();
		return false;
	}/*
		 * else if(index>0){ alert("Space is not allowed in name");
		 * $("#first_name").focus();
		 * 
		 * return false; } else if(!nameRegex.test(first_name)){ alert("Please
		 * enter only charchters."); $("#first_name").focus(); return false; }
		 */
	index = last_name.indexOf(' '); // 4
	if (last_name.length == 0) {
		alert("Please enter Last name.");
		$("#last_name").focus();
		return false;
	}/*
		 * else if(index>0){ alert("Space is not allowed in name");
		 * $("#last_name").focus(); return false; } else
		 * if(!nameRegex.test(last_name)){ alert("Please enter only
		 * charchters."); $("#last_name").focus(); return false; }
		 */
	if (address.length == 0) {
		alert("Please enter address.");
		$("#address").focus();
		return false;
	}

	if (mobile_number.length == 0) {
		alert("Please enter mobile number ");
		return false;
	}

	if (mobile_number.length != 10 && mobile_number.length != 13) {
		alert("Please enter valid mobile number ");
		$("#mobile_number").focus();
		return false;
	} else if (mobile_number.length == 10) {
		if (!mobReges.test(mobile_number)) {
			alert('Not a valid mobile number');
			$("#mobile_number").focus();
			return false;
		}
	} else if (mobile_number.length == 13) {
		if (!mobRegex2.test(mobile_number)) {
			alert('Not a valid mobile number');
			$("#mobile_number").focus();
			return false;
		}
	}
	if (alternate_mobile_number.length != 0) {
		if (alternate_mobile_number.length != 10
				&& alternate_mobile_number.length != 13) {
			alert("Please enter valid alternate contact number");
			$("#alternate_mobile_number").focus();
			return false;
		} else if (alternate_mobile_number.length == 10) {
			if (!mobReges.test(alternate_mobile_number)) {
				alert('alternate contact number is not a valid ');
				$("#alternate_mobile_number").focus();
				return false;
			}
		} else if (alternate_mobile_number.length == 13) {
			if (!mobRegex2.test(alternate_mobile_number)) {
				alert('alternate contact number is not a valid ');
				$("#alternate_mobile_number").focus();
				return false;
			}
		}
	}

	var email = $("#email").val();
	var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

	if (email.length != 0) {
		if (!filter.test(email)) {
			alert('Please provide a valid email address');
			email.focus;
			return false;
		}
	}

	var panVal = $('#panNumber').val();
	var regpan = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
	/* if (panVal.length == 0) {
		alert("Enter pan card number");
		return false;
	} */
	
	var gstin = $("#gstin").val();
	if (gstin.length == 0) {
		alert("Enter Gstin  number");
		return false;
	}
	//if (panVal.length != 0 && gstin.length != 0) {
		/*
		 * if (!regpan.test(panVal)) { alert(" invalid pan card number"); return
		 * false; }
		 */

		//var gstinRegex = /^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
		 var gstinRegex =/^\d{2}[A-Z]{5}\d{4}[A-Z]{1}\d[Z]{1}[A-Z\d]{1}/; ///^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
		if (gstin.length != 0) {
			if (!gstinRegex.test(gstin)||gstin.length>15) {
				alert(" invalid GSTIN number");
				$("#gstin").focus();
				return false;
			}
		}
		//alert(gstin.substring(2, 12) + " " + panVal);
		/* if (gstin.substring(2, 12) != panVal) {
			alert(" Pan number not matching with GSTIN card number.");
			return false;
		} */

	//}


	var result = confirm("do you want to register?");
	if (!result) {
		return false;
	}
}
function validateFileExtention(){
	debugger;			
	var ext="";
					var kilobyte=1024;
					var len=$('input[type=file]').val().length;
					var count=0;
				
					  $('input[type=file]').each(function () {
					        var fileName = $(this).val().toLowerCase(),
					         regex = new RegExp("(.*?)\.(jpeg|png|jpg|pdf)$");
					        	debugger;
							 if(fileName.length!=0){
					        	if((this.files[0].size/kilobyte)<=kilobyte){
								}else{
									if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
								        ext= fileName.substring(fileName.lastIndexOf(".")+1); 
									alert("Please Upload Below 1 MB "+ext+" File");
									count++;
									//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
									$(this).val('');
								return false;
								}
					        	
						        if (!(regex.test(fileName))) {
						            $(this).val('');
						            alert('Please select correct file format');
						            count++;
						            return false; 	
						        }
					        }
					    });
					// 
					  if(count!=0){
						  return false;  
					  }else{
						  return true;
					  }
					 }

 	function nameValidation() {
		var first_name = $("#first_name").val();
		var last_name = $("#last_name").val();
		var nameRegex = /^[a-zA-z]+$/;
		var checkWhiteSpace = new RegExp("/^\s+$/");
		if (first_name.length == 0) {
			alert("Enter first name");
			return false;
		}

		if (last_name.length == 0) {
			return false;
		}

		var condition = "1";

		$("#submitBtn").prop("disabled", true);
		$("#updateBtn").prop("disabled", true);

		$.ajax({
					url : "validatTransportorname.spring",
					type : "GET",
					data : {
						first_name : first_name,
						last_name : last_name,
						condition : condition
					},
					success : function(resp) {
						if (resp == false) {
							alert("This name is already exists. please use another name ");
							$("#submitBtn").prop("disabled", true);
							$("#updateBtn").prop("disabled", true);
							 $("#first_name").focus();
						} else {
							$("#submitBtn").prop("disabled", false);
							$("#updateBtn").prop("disabled", false);
						}
					},
					error : function(err) {

					}
				});
	} 
 	
	function populateContractor(condition) {
		var contractorName = "";
		debugger;
		if(condition==1){
			var contractorName = $("#first_name").val();
		}else if(condition==2){
			var contractorName = $("#last_name").val();
		}
		
		var nameRegex = /^[a-zA-z ]+$/;
		if (contractorName.length < 1) {
			return false;
		}
		var url = "loadAndSetVendorInfoForWO.spring";
		debugger;
		$.ajax({
			url : url,
			//url : "${pageContext.request.contextPath}/getVendorDetails.spring",
			type : "get",
			data : {
				contractorName : contractorName,
				loadcontractorData : false
			},
			contentType : "application/json",
			success : function(data) {

	console.log(data);
				if (condition == 1) {
					debugger;
					$("#first_name").autocomplete({
						source : data,
						   focus:function(e){e.stopPropagation();return false;},
					        select:function(e){e.stopPropagation();return false;}
						   });
				} else if (condition == 2) {
					$("#last_name").autocomplete({
						source : data,
						   focus:function(e){e.stopPropagation();return false;},
					        select:function(e){e.stopPropagation();return false;}
					});
				}
			},
			error : function(data, status, er) {
				alert(data + "_" + status + "_" + er);
			}
		});
		//code for selected text
	};

	$(document)
			.ready(
					function() {

						$("#gstin")
								.on(
										"blur",
										function() {
											var gstin = $(this).val();
											//alert(panNumber);
											if (gstin.length == 0) {
												$("#submitBtn").prop(
														"disabled", false);
												$("#updateBtn").prop(
														"disabled", false);

												return false;
											}

											//var gstinRegex = /^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
											var gstinRegex =/^\d{2}[A-Z]{5}\d{4}[A-Z]{1}\d[Z]{1}[A-Z\d]{1}/; ///^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
											if (gstin.length != 0) {
												if (!gstinRegex.test(gstin)||gstin.length>15) {
													alert(" invalid GSTIN number");
													$(this).val("");
													$("#submitBtn").prop( "disabled", false);
													$("#updateBtn").prop( "disabled", false);

													return false;
												}
											}

											$("#submitBtn").prop("disabled",
													true);
											$("#updateBtn").prop("disabled",
													true);
											var condition = "GSTIN"
											$
													.ajax({
														url : "validatTransPortorpanNumber.spring",
														type : "GET",
														data : {
															panNumber : gstin,
															condition : condition
														},
														success : function(resp) {
															if (resp == false) {
																alert("GSTIN number already exists. please use another number ");
																$("#submitBtn")
																		.prop(
																				"disabled",
																				true);
																$("#updateBtn")
																		.prop(
																				"disabled",
																				true);

																setTimeout(
																		function() {
																			$(
																					"#gstin")
																					.val(
																							"");

																		}, 1000)

															} else {
																$("#submitBtn")
																		.prop(
																				"disabled",
																				false);
																$("#updateBtn")
																		.prop(
																				"disabled",
																				false);
															}
														},
														error : function(err) {

														}
													});
										});
						$("myForm").submit(function() {
							if ($(this).valid()) {
								$(this).submit(function() {
									return false;
								});
								return true;
							} else {
								return false;
							}
						});

						setTimeout(function() {
							$(".success").text(" ");
						}, 5000)

						/* $("myForm").submit(function() {
						    $(this).submit(function() {
						        return false;
						    });
						    return true;
						}); */

						$("#panNumber")
								.on(
										"blur",
										function() {
											var panNumber = $(this).val();
											if (panNumber.length == 0) {
												$("#submitBtn").prop(
														"disabled", false);
												$("#updateBtn").prop(
														"disabled", false);
												return false;
											}
											var regpan = /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/;
											if (panNumber.length < 8) {
												$("#submitBtn").prop(
														"disabled", false);
												$("#updateBtn").prop(
														"disabled", false);
												return false;
											}
											if (!regpan.test(panNumber)) {
												alert('Not a valid pan number');
												$("#panNumber").val("");
												return false;
											}
											$("#submitBtn").prop("disabled",
													true);
											$("#updateBtn").prop("disabled",
													true);
											var condition = "PAN"
											$
													.ajax({
														url : "validatTransPortorpanNumber.spring",
														type : "GET",
														data : {
															panNumber : panNumber,
															condition : condition
														},
														success : function(resp) {
															if (resp == false) {
																alert("Pan number already exists. please use another number ");
																$("#submitBtn")
																		.prop(
																				"disabled",
																				true);
																$("#updateBtn")
																		.prop(
																				"disabled",
																				true);
																setTimeout(
																		function() {
																			$(
																					"#panNumber")
																					.val(
																							"");
																		}, 1000)

															} else {
																$("#submitBtn")
																		.prop(
																				"disabled",
																				false);
																$("#updateBtn")
																		.prop(
																				"disabled",
																				false);
															}
														},
														error : function(err) {

														}
													});
										});
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

				/* $('#records_table').DataTable(); */

			});

	$(function() {
		var div1 = $(".right_col").height();
		var div2 = $(".left_col").height();
		var div3 = Math.max(div1, div2);
		$(".right_col").css('max-height', div3);
		$(".left_col").css('min-height', $(document).height() - 65 + "px");
	});
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
	 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style" id="market_file_style'+dynamicId+'"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" onchange="fileChange('+dynamicId+', this)" class="selectCount" style="display:none;"/><div class=""><span id="fileName'+dynamicId+'"  style="float:left;padding-right: 10px;width:200px;color: black;"></span><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;padding: 2px 5px;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
}
//delete file
function filedelete(id){
	var canIDelete=window.confirm("Do you want to delete file?");
	if(canIDelete == false ){
		return false;
	}
	$("#market_file_style"+id).remove();
}

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
	
</script> 

</body>
</html>
