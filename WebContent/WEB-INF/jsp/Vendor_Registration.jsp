<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<html>
<head>
  <link rel="icon" href="images/sumadhuraa _1.jpg" type="image/gif" sizes="16x16">
  <title>Vendor_Registration</title>
  		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<!-- ravi written -->
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<!--   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="CacheClear.jsp" />  
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
		<link href="css/style.css" rel="stylesheet">
		<!-- ravi written end -->
		
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<style>
table, th, td {
    border: 2px solid black ;
   border-collapse: collapse;
}
td{
font-weight: bold;
font-family: Calibri;
font-size:14px;
}
textarea {
    width: 100%;
    height: 146px;
    padding: 5px 6px;
    box-sizing: border-box;
    border: 1px solid #a9a9a9;
    background-color: #ffffff;
    font-size: 16px;
    margin-bottom: -5px;
    border: none;
    resize: none;
}
.bgtablehead{
  background-color: #ffffff;
  color: black;
}
:required:focus {
  box-shadow: 0  0 3px rgba(255,0,0,0.5); 
}
input{
      border:none;
}
/*#border{
  background-color: lightgrey;
    width: 1238px;
    border: 3px solid black;
    padding: 25px;
    margin: 25px;
}*/
</style>

		
<script>

$( function() {
    $( "#date-vendorRegistration" ).datepicker({
      changeMonth: true,
      changeYear: true,
      minDate: new Date(2004, 1 - 1, 1)
    });
  } );

/* *********** Method for validating the GSTN ****************** */
function validateGSTN(){

	var GSTNum = $("#GSTINumber").val();
	 if(GSTNum.length==0){
		//alert("please fill the with GSTIN Number");
		//$("#GSTINumber").focus();
		return false;
	}
 	var gstinRegex =/^\d{2}[A-Z]{5}\d{4}[A-Z]{1}\d[Z]{1}[A-Z\d]{1}/; ///^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;

	if (GSTNum.length != 0) {
		if (!gstinRegex.test(GSTNum)) {
			alert(" invalid GSTIN number");
			setTimeout(function(){
		 		$("#GSTINumber").val("");
		 	}, 1000);
			return false;
		}
	} 
	 var url = "getGstinNumber.spring?GstiNumber="+GSTNum;
	 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			  console.log(data); 
			 	debugger;
			 	 if (data == "true" ){
				 	alert("* GSTINumber is already exists");
				 	setTimeout(function(){
				 		$("#GSTINumber").val("");
				 	}, 1000);
				 		 return false;
				 	 }
			 	 
}
	 });
}

/* ************************************************************check vendor name********************************************************************* */
  function validateCompanyName(){
	 var vendor = $("#vendor").val();
 	var url = "validateCompanyName.spring?vendor="+vendor;
	 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			  console.log(data); 
			 	debugger;
			 	 if (data == "true" ){
				 	alert("* VendorName is already exists");
				 	setTimeout(function(){
				 		$("#vendor").val("");
				 	}, 1000);
				 		 return false;
				 	 }
			 	 
}
	 });
} 
 
/* *********** Type of bussiness ****************** 
function typeOfbussiness() {
    var checkBox = document.getElementById("myCheck");
    var text = document.getElementById("myChec");
    if (checkBox.checked == true){
        text.style.display = "block";
    } else {
       text.style.display = "none";
    }
}
*/
function natureOfbussiness() {
    var checkBox = document.getElementById("myCheck1");
    var text = document.getElementById("myChec11");
    if (checkBox.checked == true){
        text.style.display = "block";
    } else {
       text.style.display = "none";
    }
}
function selectOnlyThis(id) {debugger;
	 
    for (var i = 1;i <= 5; i++)
    {
        document.getElementById(i).checked = false;
    }
    document.getElementById(id).checked = true;
}

function AllowNumbersOnly(e) {
	  var charCode = (e.which) ? e.which : e.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	    e.preventDefault();
	  }
	}
</script>
<script>
  
</script>
</head>

<body>
 <form action="savevendor.spring" method="post" enctype="multipart/form-data">
<div style="width: 100%;"> 
<table width="90%"  align="center" style="border: 1px solid #6f6767;">

<tr>
 <td width="190px; "><img src="images/sumadhura_vendorRegistraion.jpg"  style="width: 215px;margin-left: 43px;"></td>
<td class="bgtablehead" style="width: 310px; height: 145px;font-size: 25px;text-align: center;"">Vendor Registration Form</td>
</tr>

<input type="hidden" id="ServiceType" name="ServiceType" value="add" />

  <tr>
   <td class="bgtablehead" colspan="2" style="width: 310px;font-size: 18px;background-color: white;color: black;height: 67px;""><span style="margin-left: 8px;"> A.	COMPANY DETAILS & GENERAL INFORMATION</span></td>
  </tr>
  

<tr>
<td class="bgtablehead" style="width: 350px;">1.	Supplier Company Name:<span style="color:red;">*</span></td>
<td>
  <input type=text required="required" size="30" autocomplete="off" name="vendor" id="vendor" size="30"  border= "0" required ; style="font-size: 15px;width: 889px;height: 30px" ></td>
</tr>
<!-- onchange="validateCompanyName()" -->
<tr>
<td class="bgtablehead"  style="width: 350px;">2.	Address:<span style="color:red;">*</span></td>
<td><form><textarea required="required" name="address"></textarea></form></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 350px;">3.	Phone:<span style="color:red;">*</span></td>
<td style="margin-top: 21px;"><input type=text name="landnumber" id="textname" size="30" autocomplete="off" maxlength="11" required style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;">4. Email:<span style="color:red;">*</span></td>
<td><input type=text name="email" autocomplete="off" required="required" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;">5.Contact Person 1(Name & Mobile):<span style="color:red;">*</span></td>
<td><input type=text name="vendorContactPerson" id="textname" autocomplete="off" required="required" size="30" style="width: 440px;font-size: 15px;height: 21px;margin-top: 1px;" required placeholder="Contact Person Name ">
       <input type=text name="mobilenumber" id="textname" autocomplete="off"  size="30" style="width: 445px;font-size: 15px;height:21px;margin-top: 1px;" required placeholder="Mobile no"  maxlength="12" onkeypress="return AllowNumbersOnly(event)">
</td>
</tr>

<tr>
<td class="bgtablehead" style="width: 350px;">6.Contact Person 2(Name & Mobile):</td>
<td><input type=text name="vendorContactPerson2" id="textname" autocomplete="off" size="30" style="width: 440px;font-size: 15px;height: 21px;margin-top: 1px;"  placeholder="Contact Person Name ">
 <input type=text name="vendorMobileNumber2" id="textname" autocomplete="off"  size="30" style="width: 445px;font-size: 15px;height:21px;margin-top: 1px;"  placeholder="Mobile no"  maxlength="12" onkeypress="return AllowNumbersOnly(event)">
</td>
</tr>




<tr>
<td class="bgtablehead" >7.Type of Business (Mark one Only):<span style="color:red;">*</span></td>

<td style="height: 55px;background-color: white;">
    <input type="checkbox" name="typeOfBusiness" id="1" value="Proprietorship "  size="10" style="margin-top: 0px;"onclick="selectOnlyThis(this.id)">Proprietorship
    <input type="checkbox" name="typeOfBusiness" id="2" value="Partnership "      size="10" style="margin-left: 43px;"onclick="selectOnlyThis(this.id)">Partnership
    <input type="checkbox" name="typeOfBusiness" id="3" value="Corporate/Limited" size="10" style="margin-left: 26px;"onclick="selectOnlyThis(this.id)">Corporate/Limited
    <br>
    <input type="checkbox" name="typeOfBusiness" id="4"value="Private Ltd " size="10" style="margin-top: 5px;" onclick="selectOnlyThis(this.id)">Private Ltd
    <input type="checkbox" name="Others" id="5"  value="specify" style="margin-left: 65px;margin-top: 5px;" onclick="selectOnlyThis(this.id);">Others
    <input type="text" name="typeOfBusiness" placeholder="Specify" id="myChec"  style="display:block ;border: 1px solid black; margin-left: 150px;margin-top: 2px; margin-bottom: 3px;"" >

</td>

</tr>
<tr>
<td  class="bgtablehead"  >8.	Nature of Business (Enclose Company Profile):<span style="color:red;">*</span></td>
<td style="height: 55px;background-color: white;" >
    <input type="checkbox" name="Manufacturer" value="Manufacturer "  size="10" style="margin-top: 0px;">Manufacturer
    <input type="checkbox" name="Authorized" value="Authorized Agent " size="10"style="margin-left: 47px;">Authorized Agent
    <input type="checkbox" name="Trader" value="Trader    " size="10" style="margin-top: 13px;">Trader
    <br>
    <input type="checkbox" name="Consulting" value="Consulting Company "size="10">Consulting Company
    <input type="checkbox" id="myCheck1" name="Others" value="specify" style="margin-left: 8px;margin-top: 5px;" onclick="natureOfbussiness();">Others
    <input type="text" name="natureOfBusiness" placeholder="Specify" id="myChec11" style="display: none ;border: 1px solid black; margin-left: 150px;margin-top: 2px; margin-bottom: 3px;"" >
</td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;">9.	Parent Company (Full Legal Name):</td>
<td><input type=text name="parentCompany" id="textname"  size="30" style="width: 889px;font-size: 15px;height: 21px;"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;height: 60px;">10.	Subsidiaries, Associates and / or Overseas Representative(s)(attach a list if necessary): 
</td>
<td><input type=text name="overseases" id="textname" autocomplete="off" size="30" style="width: 889px;font-size: 15px;height: 56px;"></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 350px;">11.	Year Established :</td>
<td><input type=text name="yearEstablish" autocomplete="off" id="textname" size="30" style="width:889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 350px;">12.	Number of full time Employees:</td>
<td><input type=text name="noOfEmployees" id="noOfEmployees" autocomplete="off" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 350px;">13.	Date Of Vendor Inclusion:</td>
<td><input type=text name="date" id="date-vendorRegistration" autocomplete="off"  size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<div>
  <tr>
   <td class="bgtablehead"  colspan="2" style="width: 350px;background-color: white;color: black;height: 67px;font-size: 18px;""><span style="margin-left:8px">B.	COMMERCIAL INFORMATION REGISTRATION</span></span></td>
  </tr>
  
</div>
<tr>

<td class="bgtablehead" required="required" style="width: 350px;">14 .		GSTIN:<span style="color:red;">*</span></td>
<td ><input type=text name="gsinnumber" id="GSTINumber" size="30" onchange="validateGSTN()"  autocomplete="off"  style="width: 889px;font-size: 15px;height: 21px" required/></td>
 
<!--  onfocusout -->

</tr>
<tr>
<td class="bgtablehead" required="required" style="width: 350px;">15.	PAN NO:<span style="color:red;">*</span></td>
<td><input type=text name="panNumber" id="textname" size="30" autocomplete="off" required style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;">16.	PF Reg:</td>
<td><input type=text name="pfNo" id="textname" autocomplete="off"size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 350px;">17.ESI Reg. No:</td>
<td><input type=text name="ESiNo" id="textname" autocomplete="off" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>

 <tr>
   <td class="bgtablehead" colspan="2" style="width: 310px;background-color: white;color: black;height: 67px;font-size: 18px""><span style="margin-left:8px"">C.	FINANCIAL INFORMATION</span></td>
  </tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">18.	Annual Value of Total Sales for the last 3 years:</td>
<td><textarea name="totalsales"></textarea></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">19.	Bank Account Number:<span style="color:red;">*</span></td>
<td><input type=text name="accountNumber" id="textname" autocomplete="off" size="30" required style="width: 889px;font-size: 15px;height: 21px" onkeypress="return AllowNumbersOnly(event)"></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">20.	Account Holder Name:<span style="color:red;">*</span></td>
<td><input type=text name="accountHolderName" id="textname" autocomplete="off" size="30" required style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 310px;">21.	Account Type:<span style="color:red;">*</span></td>
<td><input type=text name="accountType" id="textname" size="30" required style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>

<tr>
<td class="bgtablehead" style="width: 310px;">22.Bank Name:<span style="color:red;">*</span></td>
<td><input type=text name="bankName" id="textname" size="30" required style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>

<tr>
<td class="bgtablehead" style="width: 310px;">23.	IFSC:<span style="color:red;">*</span></td>
<td><input type=text name="ifscCode" id="textname"  maxlength="11" minlength="11" autocomplete="off"  size="30" style="width: 889px;font-size: 15px;height: 21px" required></td>
</tr>

<tr>
<td class="bgtablehead" style="width: 310px;">24.	Branch Name:</td>
<td><input type=text name="branchName" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>

<tr>
<td class="bgtablehead"  style="width: 310px;">25.Bank City:</td>
<td><input type=text name="bankCity" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">26.Bank State:</td>
<td><input type=text name="bankState" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px"></td>
</tr>

<tr>
   <td class="bgtablehead" colspan="2" style="width: 310px;background-color: white;color: black;height: 67px;font-size: 18px""><span style="margin-left:8px"">D.	OTHER DETAILS</span></td>
  </tr>
  
<tr>
<td class="bgtablehead"  style="width: 310px;">27.	List of Works / Projects Completed</td>
<td ><textarea style="font-size: 15px;" name="listOfWork"></textarea></td>

</tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">28.	Major Customers:</td>
<td><input type=text name="majorCustomers" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px""></td>
</tr>
<tr>
<td class="bgtablehead"  style="width: 310px;">29.	Whether ISO Certified:(if yes, enclose the copy of certificate)
</td>
<td>
 <input type="radio" name="ISOcertified" id="1" value="Yes"  size="10" style="margin-top: 0px;">yes
    <input type="radio" name="ISOcertified" id="2" value="No"   size="10" style="margin-left: 43px;">No
<!-- <input type=text name="ISOcertified" id="textname" size="30" style="width: 889px;font-size: 15px;height: 32px""> --></td>
</tr>
<tr>
<td class="bgtablehead" style="width: 30px;">30.	Any Other Information:</td>
<td><input type=text name="OtherInfo" id="textname" size="30" style="width: 889px;font-size: 15px;height: 21px""></td>
</tr>

<tr>
<td class="bgtablehead"  style="width: 310px;">31.Upload Pdf/Image files</td>
<td ><div class="file-upload" style="float: left;  color: orange;font-size:  14px;margin-left: 33px;margin-top: 2px;font-weight: bold">
<input style="margin-top: 5px;"  type="file" name="file" id="file0" class="file0" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close0 file-left" onclick="resetFile0()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file1" class="file1" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close1 file-left" onclick="resetFile1()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file2" class="file2" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close2 file-left" onclick="resetFile2()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file3" class="file3" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close3 file-left" onclick="resetFile3()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file4" class="file4"  accept="application/pdf,image/*" ><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close4 file-left" onclick="resetFile4()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file5" class="file5" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close5 file-left" onclick="resetFile5()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;" type="file" name="file" id="file6" class="file6" accept="application/pdf,image/*" ><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close6 file-left" onclick="resetFile6()"><i class="fa fa-close"></i></button><br>
<input style="margin-top: 5px;margin-bottom: 2px;" type="file" name="file" id="file" class="file7" accept="application/pdf,image/*"/><button type="button" id="" style="display:none;margin-top:-38px;margin-left:250px" class="btn btn-danger btn-close7 file-left" onclick="resetFile7()"><i class="fa fa-close"></i></button><br>
					
	
</div></td>

</tr>

</table>
</div>
<!-- onclick="validateGSTN()" -->
<input type="submit"  style="border: 1px solid;margin-left: 620px;margin-top: 10px;width: 94px;height: 30px;font-weight: bold;">

<input type="hidden" name="kilobyte" id="kilobyte" value="${KILO_BYTE}">

</form>
<script>
$('.file0').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
	if((this.files[0].size/kilobyte)>=kilobyte){
		alert("please enter below 1 mb pdf file");
		resetFile0();
	}else{$('.btn-close0').show();}
}else{$('.btn-close0').show();}

//$('.btn-close0').show();
});
$('.file1').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile1();
		}else{$('.btn-close1').show();}
	}else{$('.btn-close1').show();}
});
$('.file2').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile2();
		}else{$('.btn-close2').show();}
	}else{$('.btn-close2').show();}

});
$('.file3').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile3();
		}else{$('.btn-close3').show();}
	}else{$('.btn-close3').show();}

});
$('.file4').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile4();
		}else{$('.btn-close4').show();}}else{$('.btn-close4').show();}
	//$('.btn-close4').show();
});
$('.file5').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile5();
		}else{$('.btn-close5').show();}
	}else{$('.btn-close5').show();}

});
$('.file6').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile6();
		}else{$('.btn-close6').show();}
	}else{$('.btn-close6').show();}

});
$('.file7').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile7();
		}else{$('.btn-close7').show();}
	}else{$('.btn-close7').show();}

});
function resetFile0() {
	  const file = document.querySelector('.file0');
	  file.value = '';
	  $('.btn-close0').hide();
	 
	}
function resetFile1() {
	  const file = document.querySelector('.file1');
	  file.value = '';
	  $('.btn-close1').hide();
	}
function resetFile2() {
	  const file = document.querySelector('.file2');
	  file.value = '';
	  $('.btn-close2').hide();
	}
function resetFile3() {
	  const file = document.querySelector('.file3');
	  file.value = '';
	  $('.btn-close3').hide();
	}

function resetFile4() {
	  const file = document.querySelector('.file4');
	  file.value = '';
	  $('.btn-close4').hide();
	}
function resetFile5() {
	  const file = document.querySelector('.file5');
	  file.value = '';
	  $('.btn-close5').hide();
	}
function resetFile6() {
	  const file = document.querySelector('.file6');
	  file.value = '';
	  $('.btn-close6').hide();
	}
function resetFile7() {
	  const file = document.querySelector('.file7');
	  file.value = '';
	  $('.btn-close7').hide();
	}


</script>
</body>
</html>
