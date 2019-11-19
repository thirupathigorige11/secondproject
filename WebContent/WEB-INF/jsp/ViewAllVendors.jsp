<%@page import="java.util.Iterator"%>

<%@page import="com.sumadhura.bean.ProductDetails"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script> 
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <script src="js/sidebar-resp.js" type="text/javascript"></script>
  <title>View All Vendor Details</title>

<style>
/* css for iframe modal popup */
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}

.iframe-pdf {
  opacity: 1;
  display: block;
  width: 100%;
  height: auto;
  transition: .5s ease;
  backface-visibility: hidden;
}

.middle {
  transition: .5s ease;
  opacity: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  width:100%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
}

.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.modal-lg-width{
width:95%;
}
/* text {
 background-color: #4CAF50;
 color: white; 
  font-size: 16px;
 padding: 16px 32px;
} */
.btn-fullwidth:hover{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-49px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:0px;
}
.middle {
    position: absolute;
    right: 30px;
  bottom: -10px; 
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent;
    background-color: transparent;
    border-color: transparent;
}
 .btn-fullwidth:active{
 color: transparent;
    background-color: transparent;
    border-color: transparent;
 }
 .btn-fullwidth.focus, .btn-fullwidth:focus {
    color: transparent;
    background-color: transparent;
    border-color: transparent;
}
/*css for iframe modal popup*/

</style>
<script>



//======================        user341 START           ========================
$( function() {
	debugger;
    $( ".dateid" ).datepicker({
      changeMonth: true,
      changeYear: true,
      minDate: new Date(2004, 1 - 1, 1)
    });
  } );
	
function setVendorData(vName) {
	var url = "loadAndSetVendorInfo.spring?vendName="+vName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setVedData;
		request.open("POST", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setVedData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		//alert(resp);
		var vendorId = resp.split("|")[0];
		var vendorAddress = resp.split("|")[1];
		var vendorGsinNo = resp.split("|")[2];
		
		$("#vendorIdId").val(vendorId);
		$("#VendorAddress").val(vendorAddress);
		$("#GSTINNumber").val(vendorGsinNo);			
	}
}
//=========================                user341 END               =================

	/* function myFunction() {
		/* alert(123); */
		/* var popup = document.getElementById("myPopup");
		
		alert(popup);
		popup.classList.toggle("show");
	} */
	function createPO() {
		
		//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
		
		//var valStatus = appendRow();
		
		/*if(valStatus == false) {
	    	return;
		}*/
		
		var canISubmit = window.confirm("Do you want to Submit?");
		
		if(canISubmit == false) {
			return;
		}
		
		//document.getElementById("saveBtnId").disabled = true;	
		//document.getElementById("countOfRows").value = getAllProdsCount();	
		document.getElementById("ProductWiseIndentsFormId").action = "loadCreatePOPage.spring";
		document.getElementById("ProductWiseIndentsFormId").method = "POST";
		document.getElementById("ProductWiseIndentsFormId").submit();
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
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">View Vendor Details</li>
					</ol>
				</div>
				
<div class="col-md-12">				
<form:form modelAttribute="getIndentsProductWiseModelForm" enctype="multipart/form-data"  id="ProductWiseIndentsFormId" method="POST" class="form-horizontal">
<c:forEach items="${listAllVendorsList}" var="element">  
<div class="col-md-12 border-indent">
	 		 	<div class="col-md-6" >
				<div class="form-group">
						    <label  class="control-label col-md-6">Vendor Name :</label>
							<div class="col-md-6">
							  <input type="text" class="form-control" name="vendorName" data-toggle="tooltip" title="${element.vendor_name}" value="${element.vendor_name}"/><input type="hidden" name="vendorId" value="${element.vendor_Id}"/>
						    </div>
				</div>
				</div>
				<div class="col-md-6" >
				<div class="form-group">	
						    <label  class="control-label col-md-6" style="" >GSTIN :</label>
							<div class="col-md-6">
									<input type="text" class="form-control" name="gsinNumber" id="gsinNumber" data-toggle="tooltip" title="${element.gsin_number}" value="${element.gsin_number}"/>						
							        <input type="hidden" class="form-control" name="strGsinNumber" id="strGsinNumber"  value="${element.gsin_number}"/>
							</div>
				</div>
				</div>
				<div class="col-md-6" >
				<div class="form-group">
						    <label  class="control-label col-md-6">Status :</label>
							<div class="col-md-6">
								<input type="text"  class="form-control"  value="ACTIVE"/>		
							</div>
				</div>
				</div>
				<div class="col-md-6" >
				<div class="form-group">
						    <label  class="control-label col-md-6" >State :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="vendorState" data-toggle="tooltip" title="${element.vendor_state}" value="${element.vendor_state}"/>
							</div>
				</div>
				</div>
			<div class="col-md-6" >
			<div class="form-group">
						    <label  class="control-label col-md-6" >Email :</label>
							<div class="col-md-6">
									<input type="text" class="form-control" name="vendorEmail" data-toggle="tooltip" title="${element.vendor_email}"  value="${element.vendor_email}"/>
							</div>
			</div>
			</div>
			<div class="col-md-6">
			<div class="form-group">			
							<label  class="control-label col-md-6" >Land Line :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="landline" data-toggle="tooltip" title="${element.landline}" value="${element.landline}"/>
							</div>
			</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6"  >State Code  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="stateCode" data-toggle="tooltip" title="${element.state_code}"value="${element.state_code}"/>
							</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
						    <label class="control-label col-md-6" style="" >Vendor Contact Person :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="contactPerson" data-toggle="tooltip" title="${element.vendorcontact_person}" value="${element.vendorcontact_person}"/>
							</div>	
				</div>
			</div>
			<div class="col-md-6">	
				<div class="form-group">
					        <label  class="control-label col-md-6"  >Vendor Address :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="address" data-toggle="tooltip" title="${element.address}"value="${element.address}"/>
							</div>
				</div>
			</div>
			<div class="col-md-6">	
				<div class="form-group">			
						   <label class="control-label col-md-6" >Mobile Number :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="mobileNumber" data-toggle="tooltip" title="${element.mobile_number}" value="${element.mobile_number}"/>
							</div>	
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">Vendor Contact Person_2 :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="vendorContactPerson_2" data-toggle="tooltip" title="${element.vendor_Contact_Person_2}"value="${element.vendor_Contact_Person_2}"/>
							</div>
			    </div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
						    <label class="control-label col-md-6" style="" >Mobile Number_2 :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="mobileNumber_2" data-toggle="tooltip" title="${element.mobile_Number_2}" value="${element.mobile_Number_2}"/>
							</div>	
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					       <label  class="control-label col-md-6">Type Of Business :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="typeOfBusiness" data-toggle="tooltip" title="${element.type_Of_Business}"value="${element.type_Of_Business}"/>
							</div>
			    </div>
			</div>
			<div class="col-md-6">
				<div class="form-group">			
						    <label class="control-label col-md-6" >Nature Of Business :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="natureOfBusiness" data-toggle="tooltip" title="${element.nature_Of_Business}" value="${element.nature_Of_Business}"/>
							</div>		
		

				</div>
				</div>
				<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">PAN Number :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="panNumber" data-toggle="tooltip" title="${element.pan_Number}"value="${element.pan_Number}"/>
							</div>
				</div>
				</div>
				<div class="col-md-6">
				<div class="form-group">		
						<label class="control-label col-md-6" >Bank Account Number :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="bankAccountNumber" data-toggle="tooltip" title="${element.acc_Number}" value="${element.acc_Number}"/>
							</div>	
				</div>
				</div>
				<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">Account Holder Name  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="accountHolderName" data-toggle="tooltip" title="${element.acc_Holder_Name}"value="${element.acc_Holder_Name}"/>
							</div>
				</div>
				</div>
				<div class="col-md-6">
				   <div class="form-group">			
						    <label class="control-label col-md-6" >Account Type</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="accountType" data-toggle="tooltip" title="${element.acc_Type}" value="${element.acc_Type}"/>
							</div>	
				   </div>
				 </div>
				<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">Bank Name  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="bankName" data-toggle="tooltip" title="${element.bank_name}"value="${element.bank_name}"/>
							</div>
				</div>
				</div>
				<div class="col-md-6">
				   <div class="form-group">			
						<label class="control-label col-md-6" >IFSC Code :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="ifscCode" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.ifsc_Code}" value="${element.ifsc_Code}"  required/>
							</div>		
		

				  </div>
				</div>
				<!-- ******************************************************newly added fields******************************************************** -->
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6"  >Parent Company :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="parentCompany" data-toggle="tooltip" title="${element.parentCompany}"value="${element.parentCompany}"/>
							</div>
			   </div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
						   <label class="control-label col-md-6" >Overseas Representative :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="OverSeases" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.overseasesRepresentative}" value="${element.overseasesRepresentative}"  required/>
							</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">Establish Year  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="EstablishYear" data-toggle="tooltip" title="${element.establishedYear}"value="${element.establishedYear}"/>
							</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">		
						   <label class="control-label col-md-6" >No Of Employees :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="noOfEmployees" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.noOfEmployees}" value="${element.noOfEmployees}"  required/>
							</div>	
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">PF Reg No :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="pfNo" data-toggle="tooltip" title="${element.PFRegNo}"value="${element.PFRegNo}"/>
							</div>
			    </div>
			</div>
			<div class="col-md-6">
				<div class="form-group">	
						    <label class="control-label col-md-6" style="" >ESI Reg No :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="esino" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.esiNo}" value="${element.esiNo}"  required/>
							</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6">Total Sales  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="totalSales" data-toggle="tooltip" title="${element.totalSalesForLastThreeYears}"value="${element.totalSalesForLastThreeYears}"/>
							</div>
			    </div>
			</div>
			<div class="col-md-6">
				<div class="form-group">				
						   <label class="control-label col-md-6">Branch Name :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="branchName" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.branchName}" value="${element.branchName}"  required/>
							</div>	
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6"  >Bank City :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="branchCity" data-toggle="tooltip" title="${element.bankcity}"value="${element.bankcity}"/>
							</div>
			</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">				
						   <label class="control-label col-md-6" >Bank State :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="bankState" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.bankState}" value="${element.bankState}"  required/>
							</div>		
		

				</div>
				</div>
				<div class="col-md-6">
				  <div class="form-group">
					        <label  class="control-label col-md-6"  >List Of Work Completed  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="ListOfWorkCompleted" data-toggle="tooltip" title="${element.listOfWorkCompleted}"value="${element.listOfWorkCompleted}"/>
							</div>
				  </div>
				</div>
				<div class="col-md-6">
				<div class="form-group">			
						    <label class="control-label col-md-6" >Major Customer :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="majorCustomer" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.majorCustomer}" value="${element.majorCustomer}"  required/>
							</div>	
				</div>
				</div>
				<div class="col-md-6">
				<div class="form-group">
					        <label  class="control-label col-md-6"  >ISO  :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control" name="iso" data-toggle="tooltip" title="${element.whetherISO}"value="${element.whetherISO}"/>
							</div>
				</div>
				</div>
				<div class="col-md-6">
				<div class="form-group">		
						   <label class="control-label col-md-6" >Other Information :</label>
							<div class="col-md-6">
								<input type="text" class="form-control" name="OtherInfo" maxlength="11" minlength="11" data-toggle="tooltip" title="${element.otherInformation}" value="${element.otherInformation}"  required/>
							</div>		
		

				</div>
				</div>
				<div class="col-md-6">
				  <div class="form-group">
					        <label  class="control-label col-md-6"  >Date Of Vendor Inclusion :</label>
							<div class="col-md-6" >
								<input type="text" class="form-control dateid" name="date" id="date-vendorRegistration" value="${element.date}"/>
							</div>
				  </div>
	           </div>
		</div>
		</c:forEach>
			<%-- <div class=""><h3>You can see the Images</h3></div>
			<br>
			<% int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount"))); 
			for(int i=0;i<imagecount;i++)
				
			{
				out.print("<div class='col-md-3' style=\"border:1px solid #000;padding:0;margin-right:10px;margin-bottom:15px;\">");
				%>
				<%if(i==0){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"   src="data:image/jpeg;base64,${requestScope['image0']}" data-toggle="modal" data-target="#uploadinvoice-img0"/><%}%>
				<%if(i==1){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image1']}" data-toggle="modal" data-target="#uploadinvoice-img1"/><%} %>
				<%if(i==2){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"   src="data:image/jpeg;base64,${requestScope['image2']}" data-toggle="modal" data-target="#uploadinvoice-img2"/><%} %>
				<%if(i==3){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image3']}" data-toggle="modal"  data-target="#uploadinvoice-img3"/><%} %>
				<%if(i==4){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image4']}" data-toggle="modal" data-target="#uploadinvoice-img4"/><%} %>
				<%if(i==5){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image5']}" data-toggle="modal" data-target="#uploadinvoice-img5"/><%} %>
				<%if(i==6){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image6']}" data-toggle="modal" data-target="#uploadinvoice-img6"/><%} %>
				<%if(i==7){ %><img style="height: 200px;width: 100%;" id="myImg" alt="img"    src="data:image/jpeg;base64,${requestScope['image7']}" data-toggle="modal"data-target="#uploadinvoice-img7"/><%} %>
				<%out.print("data-toggle=\"modal\" data-toggle=\"modal\" data-target=\"#uploadinvoice-img1\""); %>
				<%
				//out.print("\" />");
				
				out.print("<br>");
				
				out.print("<div class=\"columns download\" style='float:right;margin-right:5px;margin-top:10px;'><p><a href=\"data:image/jpeg;base64,");%>
				<%if(i==0){ %>${requestScope['image0']}<%} %>
				<%if(i==1){ %>${requestScope['image1']}<%} %>
				<%if(i==2){ %>${requestScope['image2']}<%} %>
				<%if(i==3){ %>${requestScope['image3']}<%} %>
				<%if(i==4){ %>${requestScope['image4']}<%} %>
				<%if(i==5){ %>${requestScope['image5']}<%} %>
				<%if(i==6){ %>${requestScope['image6']}<%} %>
				<%if(i==7){ %>${requestScope['image7']}<%} %>
				
				
				<%
				out.print("\" class=\"button btn-dwn\" download><i class=\"fa fa-download\"></i>Download</a></p></div>");
				
				
				out.print("</div>");}
			%>
			 --%>
			
			
			<!-- <img style="height: 200px;width: 300;" id="myImg" alt="img"  src="data:image/jpeg;base64,${requestScope['image0']}" />-->
		
		
		<!-- <div class="columns download">
          <p style="margin-bottom: 56px;">
            <a href="data:image/jpeg;base64,${requestScope['image']}" class="button btn-dwn" download><i class="fa fa-download"></i>Download</a>  
          </p>

       </div>  --> 
		<%-- <input type="hidden" name="imagesAlreadyPresent" value="<%=imagecount%>"/> --%>
		
		
<%-- <div class="file-upload" id="ishidden" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
<h3>You can upload Images here</h3>
	<%
	for(int i=0;i<imagecount;i++){
	out.println("<div id=\"fileupload"+i+"\" style=\"display:none\"><input type=\"file\" name=\"file\"></div>");
	} %>
	<%
	for(int i=imagecount;i<8;i++){
	out.println("<div id=\"fileupload"+i+"\" style=\"display:block\"><input type=\"file\" name=\"file\"></div>");
	} %>
	
	
</div>	 --%>
			
<!-- <div class="file-upload" style="float: left; color: orange;font-size: 14px;margin-top: 10px;font-weight: bold">
		<img style="height: 200px;width: 300;" id="myImg" alt="img"  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiGDHGRk6jl751shQy8C0CdiIcYvqyjtKd3Q2p2uJmX2FO08P8GQ" />
</div>	
 --> <div id="myModal" class="modal">
  	<span class="close">&times;</span>
 	 <img class="modal-content" id="img01">
 	 <div id=""></div>
</div>
	
<input type="hidden" name="kilobyte" id="kilobyte" value="${KILO_BYTE}">
<!-- **********************************************************pdf download start******************************************************************** -->
<div class="col-md-12" style="margin-top: 100px;">
	<!-- <h3 class="h3pdf">You can see the PDF</h3> -->
<% int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); %>

<%	for(int i=0;i<8;i++){
  	String pdfName="pdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
 <%
		if(request.getAttribute(pdfName)!=null){
%>
			 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
			  <div class="pdf-cls" style="margin-bottom:15px;"> 
			  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
			  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
			  <div class="middle">
				<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf<%=i%>"><i class="fa fa-close"></i></button>
			</div>
			</div>
			
			 </div>
<%} %>
<%} %>
</div>

		
		<div class="col-md-12 Mrgtop20">
					<!-- <h3 class="h3image">You can see the Images</h3> -->
			
									 <%
										int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));%>
										
										
										<% 	for (int i = 0; i < 8; i++) {
												String imageB64="image"+i;		
												String deleteB64="delete"+i;
												
												log(imageB64);
												if(request.getAttribute(imageB64)!=null)
												out.print("<div class='col-md-4 Mrgtop20'>");
												
									%>
											
									       <c:set value="<%=imageB64 %>" var="index"></c:set>
									       <c:set value="<%=deleteB64 %>" var="delete"></c:set>
									<%
									//	if (i == 0) {
								
									if(request.getAttribute(imageB64)!=null){
										log(imageB64);
									%>
									  <div class="pdf-image<%=i%>">
									 <div class="container-1" id='imgId<%=imageB64 %>'>
													<img class="img-responsive img-table-getinvoice"  alt="img" src="data:image/jpeg;base64,${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
													 <div class="middle-1">
													<div class="columns download">
										           <p>
										            <%--  <a href="data:image/jpeg;base64,${requestScope[index]}" class="button btn-dwn btn-success btn-xs" download><i class="fa fa-download"></i>Download</a> --%>
										            <%--  <button onclick="deleteImageFile('<%=i %>','${requestScope[delete]}')" type="button" id="myImg" class="button btn-dwn btn-danger btn-xs"><i class="fa fa-remove"></i>delete</button> --%>
										          </p>
										       </div>
										       </div>
										       </div></div>
									<%
										}
									//	}
									%>
				<%
								if(request.getAttribute(imageB64)!=null)
									out.print("</div>");
								%>
							<% }	%>
							
		</div>
		
		<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
		
		<input type="hidden" name="pdfAlreadyPresent" value="<%=pdfcount%>">
			<div class="col-md-12">
										<div class="file-upload" id="ishidden">
										<c:if test="${imagecount<5}"><!-- <h3 class="h3upload" >You can upload Images here</h3> -->
										
										</c:if>
											<%
												for (int i = 0; i < imagecount; i++) {
															out.println("<div class=\"col-md-12\" id=\"fileupload" + i
																	+ "\" style=\"display:none;margin-bottom:15px;\"><input type=\"file\" accept=\"application/pdf,image/*\" name=\"file\" class=\"file"+i
																	+ "\"><button type=\"button\"  style=\"display:none;\" class=\"btn btn-danger btn-close" +i 
																	+ "\" onclick=\"resetFile" +i 
																	+ "()\"></button></div>");
														}
											%>
											<%
												for (int i =  (imagecount+pdfcount); i < 8; i++) {
															out.println("<div class=\"col-md-12\" id=\"fileupload" + i
																	+ "\" style=\"display:block;margin-bottom:15px;\"><input type=\"file\"  accept=\"application/pdf,image/*\"  name=\"file\" class=\"file"+i 
																	+ "\"><button type=\"button\"  style=\"display:none;margin-top:-25px;margin-left:265px\" class=\"btn btn-danger btn-close" +i
																	+ "\" onclick=\"resetFile" +i 
																	+ "()\"><i class=\"fa fa-close\"></i></button></div>");
														}
											%>
										</div>
									</div>
		    <div class="col-md-12 class_file0" style="display:none">
	 		<input type="file" name="file" class="file8 file-left" id="file0" style="margin-left:15px;margin-top:10px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close8 file-left" onclick="resetFile8()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file1" style="display:none">
 			<input type="file" name="file" class="file9 file-left" id="file1" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close9 file-left" onclick="resetFile9()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file2" style="display:none">
 			<input type="file" name="file" class="file10 file-left" id="file2" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close10 file-left" onclick="resetFile10()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file3" style="display:none">
 			<input type="file" name="file" class="file11 file-left" id="file3" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close11 file-left" onclick="resetFile11()"><i class="fa fa-close"></i></button> 
 			</div>
 			<div class="col-md-12 class_file4" style="display:none;margin-bottom: 15px;">
 			<input type="file" name="file" class="file12 file-left" id="file4" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close12 file-left" onclick="resetFile12()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file5" style="display:none">
 			<input type="file" name="file" class="file13 file-left" id="file5" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close13 file-left" onclick="resetFile13()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file6" style="display:none">
 			<input type="file" name="file" class="file14 file-left" id="file6" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close14 file-left" onclick="resetFile14()"><i class="fa fa-close"></i></button>
 			</div>
 			<div class="col-md-12 class_file7" style="display:none">
 			<input type="file" name="file" class="file15 file-left" id="file7" style="margin-left:15px;margin-top:15px" accept="application/pdf,image/*"><button type="button" id="" style="display:none;margin-top:-25px;margin-left:280px" class="btn btn-danger btn-close15 file-left" onclick="resetFile15()"><i class="fa fa-close"></i></button>  
 			</div>	 		
<!-- **********************************************************pdf download end******************************************************************** -->		
<%-- 				<div class="table-responsive">
					<table id="tblnotification"	class="table table-striped table-bordered" cellspacing="0">
						<thead>
							<tr class="tblheaderall">
							
								<th>VENDORNAME</th>
								<th>GSIN NUMBER</th>
								<th>STATUS</th>
								<th>STATE</th>
								<th>EMAIL</th>
								<th>LANDLINE</th>
								<th>STATE CODE</th>
								<th>VENDOR CONTACT PERSON</th>
							</tr>
						</thead>
						<tbody>


						
	
					<tbody>
				<c:forEach items="${listAllVendorsList}" var="element">  
				<tr>
				    <td style="color: black;">${element.vendor_name}</td>
					 <td style="color: black;">${element.gsin_number}</td>
					 <td style="color: black;">ACTIVE</td>
					  <td style="color: black;">${element.vendor_state}</td>
					   <td style="color: black;">${element.vendor_email}</td>
					
					<td style="color: black;">${element.landline}</td>
					
					<td style="color: black;">${element.state_code}</td>
					
					<td style="color: black;">${element.vendorcontact_person}</td>
				</tr>
				</c:forEach>
				</tbody>

<div>
					<div>
								<a class="site_title" href="AddVendor.spring"><b>Back</b></a>
				</div>
			</div>

					</table>
					
				</div> --%>
						<input type="hidden" name="deletePdf0" value="" id="deletePdf0">
						<input type="hidden" name="deletePdf1" value="" id="deletePdf1">
						<input type="hidden" name="deletePdf2" value="" id="deletePdf2">
						<input type="hidden" name="deletePdf3" value="" id="deletePdf3">
						<input type="hidden" name="deletePdf4" value="" id="deletePdf4">
						<input type="hidden" name="deletePdf5" value="" id="deletePdf5">
						<input type="hidden" name="deletePdf6" value="" id="deletePdf6">
						<input type="hidden" name="deletePdf7" value="" id="deletePdf7">	
				<div>
				
				</div>
	
				<br><br><br><br>
				   <input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
				<div class="col-sm-2 pt-10"> 
			 		<input type="submit" class="btn btn-warning" style="width: 123px;margin-bottom: 50px;" value="Save" id="saveBtnId" onclick="saveRecords('SaveClicked')" />
				</div>
				
</form:form>
</div>
				<!-- /page content -->
			</div>
		</div>
	</div>

<div id="modal-image-allvendor" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Modal Header</h4>
      </div>
      <div class="modal-body">
        <p>Some text in the modal.</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div>




	<!-- jQuery -->
	<!-- <script src="js/jquery.min.js"></script> -->
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	

	<script>
		$(document).ready(					
				
				$(function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable();
				})
				
				
				
			
						
		
		);
	$(document).ready(function(){
		var isVendorDetailEditable="${isVendorDetailEditable}";
		debugger;
		if(isVendorDetailEditable=="false"){
			//ProductWiseIndentsFormId
			
			$('input[type="text"]').prop('readonly', true);
			 $("#ishidden").hide();
			 $("#saveBtnId").hide();
		}	  
		$('.Myclass').keyup(function(){
			    $(this).attr('title',$(this).val());
			  });
			});
			
		 $(document).ready(function(){
			//Get the modal
			 var modal = document.getElementById('myModal');

			 // Get the image and insert it inside the modal - use its "alt" text as a caption
			 var img = document.getElementById('myImg');
			 var modalImg = document.getElementById("img01");

		/* 	 img.onclick = function(){
			     modal.style.display = "block";
			     modalImg.src = this.src; */
			   
			/*  } */

			 // Get the <span> element that closes the modal
			 var span = document.getElementsByClassName("close")[0];

			 // When the user clicks on <span> (x), close the modal
			 span.onclick = function() { 
			     modal.style.display = "none";
			 }
			 });
		$(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		});
		
		/* ***********************************************************for gsin******************************************************************** */
function saveRecords(saveBtnClicked) {debugger;
	var responseGstin="";
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	var strGsinNumber=document.getElementById("gsinNumber").value;
	var oldGsinNumber=document.getElementById("strGsinNumber").value;
	/* if(!strGsinNumber.equals(oldGsinNumber)){
		responseGstin= "getGstinNumber.spring?GstiNumber="+strGsinNumber;
	} */
	 if(strGsinNumber!=oldGsinNumber){
		 
		 
		 var gstinRegex = /^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;

			if (strGsinNumber.length != 0) {
				if (!gstinRegex.test(strGsinNumber)) {
					alert(" invalid GSTIN number");
					setTimeout(function(){
				 		
				 	}, 1000);
					return false;
				}
			} 	
	//responseGstin= "getGstinNumber.spring?GstiNumber="+strGsinNumber;
	var url = "getGstinNumber.spring?GstiNumber="+strGsinNumber;
	 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "POST",
		  data: {
			  strGsinNumber:strGsinNumber  
		  },
		  contentType : "application/json",
		  success : function(data) {
			  console.log(data); 
			 	debugger;
			 	 if (data == "true" ){
			 		alert("Gsin Number already Exists.");
					document.getElementById("gsinNumber").focus();
					return;
				 	 }
			 	 else{
			 		 var canISubmit = window.confirm("Do you want to Submit?");
						
						if(canISubmit == false) {
							return;
						}
						
						document.getElementById("saveBtnId").disabled = true;	
						//document.getElementById("countOfRows").value = getAllProdsCount();	
						document.getElementById("ProductWiseIndentsFormId").action = "editAndSaveVendorDetails.spring";
						document.getElementById("ProductWiseIndentsFormId").method = "POST";
						document.getElementById("ProductWiseIndentsFormId").submit();
			 	 }
}
	 });
    } 
	 else{
		 var canISubmit = window.confirm("Do you want to Submit?");
			
			if(canISubmit == false) {
				return;
			}
			
			document.getElementById("saveBtnId").disabled = true;	
			//document.getElementById("countOfRows").value = getAllProdsCount();	
			document.getElementById("ProductWiseIndentsFormId").action = "editAndSaveVendorDetails.spring";
			document.getElementById("ProductWiseIndentsFormId").method = "POST";
			document.getElementById("ProductWiseIndentsFormId").submit();
	 }
	
	
}
		
		
	</script>
<!--**************************************************************modal pop	up*********************** -->

  <!-- model popup for pdf start  -->
	<%
	  int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
	for(int i=0;i<8;i++){
  	String pdfName="pdf"+i;
  	String PathdeletePdf="PathdeletePdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
 <%
		if(request.getAttribute(pdfName)!=null){
%>
			<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i+1 %></strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope[pdfBase64]}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(<%=i %>,'${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<%} %>
<%} %>


<!-- pdf model popup end  -->
	  
    </div>
  </div>
  <%-- <%} %> --%>
		<!-- modal popup for invoice image end -->  
  <!-- Modal -->
	<%	 int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			for (int i = 0; i < 8; i++) { 
		String index="image"+i;	
		String imageB64="image"+i;
		
		log(index);
		%>
	  <div class="modal fade custmodal" id="uploadinvoice-img<%=i %>" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          
        </div>
        <div class="modal-body cust-modal-body">
    <c:set value="<%=imageB64 %>" var="index"></c:set>
    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope[index]}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <a href="data:image/jpeg;base64,${requestScope[index]}" class="btn btn-dwn btn-success" download><i class="fa fa-download"></i> Download</a>
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
          <button onclick="deleteImageFile('<%=i %>','${requestScope[delete]}')" type="su" id="myImg" class="btn btn-dwn btn-danger" data-dismiss="modal">Delete</button>
        </div>
      </div>
      
    </div>
  </div>
  <%} %>
 <script>
 function deletepdf(rowId){
		debugger;
	$('.pdf-delete'+rowId).hide();
	$("#hiddenRowNum").val(rowId);
	$("#deletePdf"+rowId).val(rowId);
	$("#file"+rowId).show();
	$(".class_file"+rowId).show();
	
}
function deleteImageFile(rowId){
	debugger;
$('.pdf-image'+rowId).hide();
$("#hiddenRowNum").val(rowId);
$("#deletePdf"+rowId).val(rowId);
$("#file"+rowId).show();
$(".class_file"+rowId).show();

}
/* function resetFile() {
	  const file = document.querySelector('.file');
	  file.value = '';
	  $('.btn-close1').hide();
	 
	}
 */$('.btn-close1').change(function(){
	$('.btn-close1').hide();
});
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
		if((this.files[0].size/kilobyte)>=kilobyte){
			var	kilobyte=$("#kilobyte").val();
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
$('.file8').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile8();
		}else{$('.btn-close8').show();}
	}else{$('.btn-close8').show();}

});

$('.file9').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile9();
		}else{$('.btn-close9').show();}
	}else{$('.btn-close9').show();}

});

$('.file10').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile10();
		}else{$('.btn-close10').show();}
	}else{$('.btn-close10').show();}

});

$('.file11').change(function(){
	if((this.files[0].type)=='application/pdf'){
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile11();
		}else{$('.btn-close11').show();}
	}else{$('.btn-close11').show();}

});
$('.file12').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile12();
		}else{$('.btn-close12').show();}
	}else{$('.btn-close12').show();}

});

$('.file13').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile13();
		}else{$('.btn-close13').show();}
	}else{$('.btn-close13').show();}

});

$('.file14').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile14();
		}else{$('.btn-close14').show();}
	}else{$('.btn-close14').show();}

});

$('.file15').change(function(){
	if((this.files[0].type)=='application/pdf'){
		var	kilobyte=$("#kilobyte").val();
		if((this.files[0].size/kilobyte)>=kilobyte){
			alert("please enter below 1 mb pdf file");
			resetFile15();
		}else{$('.btn-close15').show();}
	}else{$('.btn-close15').show();}

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
function resetFile8() {
	  const file = document.querySelector('.file8');
	  file.value = '';
	  $('.btn-close8').hide();
	}

function resetFile9() {
	  const file = document.querySelector('.file9');
	  file.value = '';
	  $('.btn-close9').hide();
	}

function resetFile10() {
	  const file = document.querySelector('.file10');
	  file.value = '';
	  $('.btn-close10').hide();
	}

function resetFile11() {
	  const file = document.querySelector('.file11');
	  file.value = '';
	  $('.btn-close11').hide();
	}

function resetFile12() {
	  const file = document.querySelector('.file12');
	  file.value = '';
	  $('.btn-close12').hide();
	}

function resetFile13() {
	  const file = document.querySelector('.file13');
	  file.value = '';
	  $('.btn-close13').hide();
	}

function resetFile14() {
	  const file = document.querySelector('.file14');
	  file.value = '';
	  $('.btn-close14').hide();
	}

function resetFile15() {
	  const file = document.querySelector('.file15');
	  file.value = '';
	  $('.btn-close15').hide();
	}

 </script>
</body>
</html>
