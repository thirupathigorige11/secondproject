<%@page import="java.util.Iterator"%>

<%@page import="com.sumadhura.bean.ProductDetails"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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
<link href="css/custom.min.css" rel="stylesheet" type="text/css">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<c:if test="${isContractorEditable==false}">
	<!-- <title>View Contractor Detail</title> -->
	<title>Sumadhura-IMS</title>
</c:if>
<c:if test="${isContractorEditable!=false}">
	<!-- <title>Update/Delete Contractor Detail</title> -->
	<title>Sumadhura-IMS</title>
</c:if>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
.btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
.btn-custom-width {
	width: 150px;
	margin: 15px;
}
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
.marg-left-15{
margin-left:15px;
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
margin-top:110px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:110px;
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
.Myclass1 {
	margin-top: -5px;
	position: absolute;
}
.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}
.popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* .Myclass {
	font-weight: bold;
	font-size: 16px;
	margin-top: -7px;
	position: absolute;
	margin-left: 25px;
} */

/* The actual popup */
.popup .popuptext {
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
}

/* Popup arrow */
.popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@
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
}

.container1 {
	
}

.media-style {
	width: 39% !important;
}

@media screen and (min-width: 480px) {
	.media-style {
		width: auto;
	}
	.submitstyle {
		margin-top: 20px;
		width: 100% !important;
	}
}

.abc {
	color: red;
}

.btn-ward {
	height: 29px;
	width: 121px;
	color: white;
	background-color: #ef7e2d;
	position: absolute;
	margin-left: 465px;
	margin-top: 48px;
}
/* .inward-invoice{
	color: #726969;
   /*  margin-left: 377px; */
font-size




:


 


24
px




;
}
* /
#WithoutPricingData {
	margin-top: 31px;
}

.form-content {
	margin-top: 30px;
}

/* .withdata{
    margin-top: 32px;
	border: 1px solid;
    height: 134px;
    width: 544px;
} */
.fields-space-with {
	margin-top: 26px;
}

.fields-space {
	margin-right: 25px;
}

.fields1-space {
	margin-left: 6px;
}

.withoutPricing-class {
	margin-bottom: 10px;
}

.formShow {
	/* margin-top: 35px; */
	/*     width: 1052px; */
	background: #fff;
	/*  overflow: scroll; */
	background: #ffffff;
}

    
    .DCNumber {
	color: black;
	font-weight: bold;
}

hr {
	display: block;
	margin-top: 0.5em;
	margin-bottom: 0.5em;
	margin-left: auto;
	margin-right: auto;
	border-style: inset;
	border-width: 2px;
}

.full-width {
	width: 100%;
}

.icons-bg {
	padding: 3px 10px;
}

.pricing-box {
	/* //background: rgba(33, 32, 31, 0.2); */
	display: flow-root;
	padding: 10px;
	border: solid 1px #151212;
}
/* .font-class{
color: #252222;
font-size: 14px;
font-weight: bold;

}
.font-class1{
border: none;
font-size: 12px;

} */
.table>tbody>tr>th {
	border: 1px solid white !important;
}

.table>tbody>tr>td {
	border: 1px solid #a29595 !important;
}

.table-responsive::-webkit-scrollbar-track {
	-webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
	background-color: #F5F5F5;
	border-radius: 10px;
}

.table-responsive::-webkit-scrollbar {
	width: 10px;
	background-color: #F5F5F5;
	height: 8px;
}

.table-responsive::-webkit-scrollbar-thumb {
	background-color: #c1c1c1;
	border-radius: 10px;
}

#myImg {
	border-radius: 5px;
	cursor: pointer;
	transition: 0.3s;
}

#myImg:hover {
	opacity: 0.7;
}

/* The Modal (background) */
/* .modal {
	/* display: none; /* Hidden by default */
	/* position: fixed; /* Stay in place */ */
	/* z-index: 1; /* Sit on top */
	/* padding-top: 100px; /* Location of the box */
	left: 0; */
	/* top: 0; */ */
	/* width: 100%; /* Full width */ */
	/* height: 100%; /* Full height */ */
	/* overflow: auto; /* Enable scroll if needed */ */
	/* background-color: rgb(0, 0, 0); /* Fallback color */ */
	/* background-color: rgba(0, 0, 0, 0.9); /* Black w/ opacity */ */
} */ */

/* Modal Content (image) */
/* .modal-content {
	margin: auto;
	display: block;
	width: 80%;
	max-width: 700px;
}
 */
/* Caption of Modal Image */

/* Add Animation */
/* .modal-content, #caption {
	-webkit-animation-name: zoom;
	-webkit-animation-duration: 0.6s;
	animation-name: zoom;
	animation-duration: 0.6s;
}

@
-webkit-keyframes zoom {
	from {-webkit-transform: scale(0)
}

to {
	-webkit-transform: scale(1)
}

}
@
keyframes zoom {
	from {transform: scale(0)
}

to {
	transform: scale(1)
}

} */

/* The Close Button */
.close {
	position: absolute;
	top: 15px;
	right: 35px;
	color: #f1f1f1;
	font-size: 40px;
	font-weight: bold;
	transition: 0.3s;
}

/* .close:hover, .close:focus {
	color: #bbb;
	text-decoration: none;
	cursor: pointer;
} */

/* 100% Image Width on Smaller Screens */
/* @media only screen and (max-width: 700px) {
	.modal-content {
		width: 100%;
	}
} */

.Myclass {
	border-bottom: 1px solid black;
    width: 100%;
    display: inline;
    border-top: none;
    border-right: none;
    border-left: none;
    border-radius: 0px;
}
</style>

<script>
	function reloadFunc() {
		debugger;

		if (typeof (Storage) !== "undefined") {

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("updateContractorDetail.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
		;
	}
</script>


</head>

<body class="nav-md" onload="reloadFunc()">

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
							<li class="breadcrumb-item"><a href="#">Masters</a></li>
							<c:if test="${isContractorEditable==false}">

								<li class="breadcrumb-item active">View Transportor Details</li>
							</c:if>
							<c:if test="${isContractorEditable!=false}">
								<li class="breadcrumb-item active">Update/Delete Transportor
									Details</li>
							</c:if>

						</ol>
					</div>
					<div class="col-md-12">
						<div>
							<label class="success" style="font-size: 20px; color: green;"><c:out
									value="${requestScope['succMessage']}"></c:out> </label>
						</div>
<div class="col-md-12">
 <form:form action="saveTransportorDetails.spring"
				modelAttribute="ContractorBean" id="ProductWiseIndentsFormId" enctype="multipart/form-data"
				name="myForm" method="POST" class="form-horizontal">
						
							<div class="col-md-12 col-sm-12 col-xs-12 border-form-contractorupdate">
								<div class="col-md-6 col-sm-12 col-xs-12 ">
									<div class="form-group">
										<label class="control-label col-md-6 col-sm-12 col-xs-12" style="">First
											Name :</label>
										<div class="col-md-6 col-sm-12 col-xs-12">
											<form:input path="first_name" type="text" id="first_name"
												autocomplete="off" class=" form-control Myclass " required="true" />
											<form:input path="contractor_name" type="hidden"
												id="first_name" autocomplete="off" class=" form-control Myclass" />
										</div>
									</div>
								</div>
								<div class="col-md-6 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="control-label col-md-6 col-sm-12 col-xs-12" style=""> GSTIN
											:</label>
										<div class="col-md-6 col-sm-12 col-xs-12">
							<input type="hidden"
												id="actualgstinNumber" value="${ContractorBean.GSTIN }">
											<form:input path="GSTIN" type="text" id="gstin"
												autocomplete="off" class="form-control Myclass" />
										</div>
									</div>

								</div>
								<div class="col-md-6 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="control-label col-md-6 col-sm-12 col-xs-12" style="">Last
											Name :</label>
										<div class="col-md-6 col-sm-12 col-xs-12">
											<form:input path="contractor_id" type="hidden"
												autocomplete="off" class="form-control Myclass" />
											<form:input path="last_name" type="text" id="last_name"
												autocomplete="off" onblur="return nameValidation()"
												class="form-control Myclass" required="true" />
										</div>
									</div>

								</div>
								<div class="col-md-6 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="control-label col-md-6 col-sm-12 col-xs-12" style="">Bank
											Name :</label>
										<div class="col-md-6 col-sm-12 col-xs-12">
											<form:input path="bank_name" type="text" autocomplete="off"
												class="form-control Myclass" />
										</div>

									</div>
								</div>

								<div class="col-md-6 col-sm-12 col-xs-12">
									<div class="form-group">
										<label class="control-label col-md-6 col-sm-12 col-xs-12" style="">Address
											: </label>
										<div class="col-md-6 col-sm-12 col-xs-12">
											<form:input path="address" type="text" id="address"
												autocomplete="off" class="form-control Myclass" />
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6" style=""> Bank
											Acc Number :</label>
										<div class="col-md-6">
											<form:input path="bank_acc_number" type="text"
												autocomplete="off" class="form-control Myclass" />

										</div>

									</div>
								</div>

								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6" style="">Contact
											Number : </label>
										<div class="col-md-6">
											<form:input path="mobile_number" id="mobile_number"
												type="text" autocomplete="off" class="form-control Myclass" />
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6" style="">
											Account Type :</label>
										<div class="col-md-6 col-xs-12">
											<form:input path="account_type" type="text"
												autocomplete="off" class="form-control Myclass" />
										</div>

									</div>
								</div>
								<div class="col-md-6">
								 <div class="form-group">
									<label class="control-label col-md-6" style="">Alternate
										Contact Number : </label>
									<div class="col-md-6 col-xs-12">
										
										<form:input path="alternate_mobile_number"
											id="alternate_mobile_number" autocomplete="off" type="text"
											class="form-control Myclass" />
									</div>
								</div>
								</div>
								<div class="col-md-6">
								 <div class="form-group">
									<label class="control-label col-md-6" style="">Branch
										Name : </label>
									<div class="col-md-6">
										<form:input path="branch_name" id="branch_name" type="text"
											autocomplete="off" class="form-control Myclass" />
									</div>

								</div>
								</div>
								<div class="col-md-6">
								<div class="form-group ">
									<label class="control-label col-md-6" style="">Landline
										Number : </label>
									<div class="col-md-6">
									
										<form:input path="landline_number" id="landline_number"
											autocomplete="off" type="text" class="form-control Myclass" />
									</div>
									</div>
								</div>
									<div class="col-md-6">
									 <div class="form-group">
									<label class="control-label col-md-6" style=""> IFSC
										Code :</label>
									<div class="col-md-6">
										<form:input path="ifsc_code" type="text" autocomplete="off"
											class="form-control Myclass" />
									</div>


								</div>
									</div>
								<div class="col-md-6">
								 <div class="form-group">
									<label class="control-label col-md-6" style="">Email :
									</label>
									<div class="col-md-6">
										<form:input path="email" id="email" type="text"
											autocomplete="off" class="form-control Myclass" />
									</div>
									</div>
								</div>
									<div class="col-md-6">
									 <div class="form-group">
									<label class="control-label col-md-6" style="">State
										Name : </label>
									<div class="col-md-6">
										<form:input path="state_name" autocomplete="off" type="text"
											id="state_name" class="form-control Myclass" />
									</div>
								</div>
									</div>
								<div class="col-md-6">
								 <div class="form-group"
>

									<label class="control-label col-md-6" style="">PAN
										Number : </label>
									<div class="col-md-6">
									<input type="hidden"
											id="actualPanNumber" value="${ContractorBean.pan_number }">
										<form:input path="pan_number" autocomplete="off" type="text"
											id="panNumber" class="form-control Myclass" />
									</div>
									</div>
								</div>
									<div class="col-md-6">
									 <div class="form-group">
									<label class="control-label col-md-6" style="">City
										Name : </label>
									<div class="col-md-6"> 
										<form:input path="city_name" id="city_name" autocomplete="off"
											type="text" class="form-control Myclass" />
											<input type="hidden" name="strSubmitBtn" id="strSubmitBtn"/>
									</div>
								</div>
									</div>
									<c:if test="${isContractorEditable==false}">
                               <div class="clearfix"></div>
                               
                                <jsp:include page="WorkOrder/ImgPdfCommonJsp.jsp" />
                                </c:if>
                               <c:if test="${isContractorEditable!=false}">
                              <!--  ********************************** this is for update transoortor details start********************** -->
                              <!-- ***********************************************this is for pdf file download start******************************************************** -->					
		<div class="col-md-12" style="margin-top: 10px;">
					<!-- 	<h3>You can see the PDF</h3> -->
					<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
					if(pdfcount==0){
						%>
						<!-- <h3>No PDF</h3> -->
						<%
					}else{
						%>
					<h3 style="">You can see the PDF(s) below :</h3>	
						<%
					}	
						for(int i=0;i<pdfcount;i++){
					  	String pdfName="pdf"+i;
					  	String PathdeletePdf="PathdeletePdf"+i;
					  	log(pdfName);
					
					%>
					   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
					    <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
					 <%
							if(request.getAttribute(pdfName)!=null){
					%>
								 <div class="col-md-3 pdfcount pdf-delete<%=i%>"  id="pdfDivHideShow<%=pdfName %>">
								  <div class="pdf-cls" style="margin-bottom:15px;cursor:pointer;">
								  <iframe class="iframe-pdf filesCount" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
								  <div class="middle" data-toggle="modal" data-target="#myModalpdf<%=i%>">
								</div>
								<input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
								<p class="btn-downloaddelete"><a class="btn btn-success btn-xs" download onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i class="fa fa-download"></i> Download</a>
								<button class="btn btn-danger btn-xs" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')"><i class="fa fa-close"></i>Delete</button></p>
								</div>
								</div>
					<%} %>
					<%} %>
					</div>
					 
					 		<!-- ***********************************************this is for pdf download end************************************************** -->		
									<div class="col-md-12 Mrgtop20">
						
									<!-- <h3>You can see the Images</h3> -->
											<%
															int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
															//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
					 				if(imagecount==0){
					 					%>
					 				<!-- 	<h3>No Images</h3> -->
					 					<%
					 				}else{
					 				%>
					 					<h3 style="">You can see the Images below :</h3>
					 				<%
					 				}		
					 				
																for (int i = 0; i < imagecount; i++) {
																	String imageB64="image"+i;	
																	String deleteB64="delete"+i;
																	log(imageB64);
																	out.print("<div>");
														%>
														       <c:set value="<%=imageB64 %>" var="index"></c:set>
														        <c:set value="<%=deleteB64 %>" var="delete"></c:set>
																							<%
														//	if (i == 0) {
															if(request.getAttribute(imageB64)!=null){
														%>
														 <div class='col-md-4 Mrgtop20' id='imageDivHideShow<%=imageB64 %>'>
														 <div class="container-1"  >
																		<img class="img-responsive img-table-getinvoice filesCount"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" style="cursor:pointer;"/>
																		 <div class="middle-1">
																		<div class="columns download">
															           <p>
															             <a class="btn btn-dwn btn-success btn-xs"  download onclick="toDataURL('${requestScope[index]}',this)" ><i class="fa fa-download"></i>&nbsp;Download</a>
															             <button onclick="deleteWOImageFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="btn btn-dwn btn-danger btn-xs"><i class="fa fa-remove"></i> &nbsp;Delete</button>
															          </p>
															       </div>
															       </div>
															       </div>
															       </div>
														<%
															}
														//	}
														%>
													<%
														out.print("</div>");
													%>
													<%}%>
											<%  int totalValue=imagecount+pdfcount; %>
												<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
												<input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfcount%>">
												<input type="hidden" name="imagesAlreadyexisted" id="imagesAlreadyexisted"	value="<%=imagecount%>" />
												<input type="hidden" name="pdfAlreadyExised" id="pdfAlreadyExised" value="<%=pdfcount%>">
												
												<input type="hidden" name="totalCount" id="totalCount" value="<%=totalValue%>"/>
												
													<div class="col-md-12">
														<div class="file-upload" id="ishidden">
														  <%-- <c:if test="${(imagecount+pdfcount)<8}"> --%><h3>You can upload Images/PDFs here :</h3><%-- </c:if> --%>																		
 														  <P><strong>Maximum File Upload Size: 1MB</strong></P>
														  <div class="col-md-12 files_place"><button type="button" id="Add" style="font-size:14px;padding:8px;" class="btn btn-warning btn-xs market_btn_style"  onclick="uploadFile()">Upload Files</button></div> <!-- <i class="fa fa-plus"></i>  -->
															<div class="col-md-12">
															 <div class="market_file_style">
															  <div class="clearfix"></div>
															 </div>
															 <input type="file" id="file" name="file" style="display:none">
															</div>
 													</div>
												</div>
							         </div>
							
						<!-- model popup for pdf start  -->
						<%
						for(int i=0;i<pdfcount;i++){
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
							 <embed src="${requestScope[pdfBase64]}" style="height:500px;width:100%;">
					      </div>
					      <input type="hidden" name="imagePath" id="image_Path" value="${requestScope[deletePdf]}"/>
					      <div class="modal-footer">
					       <p class="text-center">
					       <%--  --%>
						     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						     
						     <%-- <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','<c:out value="${requestScope[deletePdf]}"></c:out>')" data-dismiss="modal">Delete</button> --%>
						   
						   </p>
					      </div>
					    </div>
					
					  </div>
					</div>
					<%} %>
					<%} %>
					
					
					<!-- pdf model popup end  -->
						
						
					<!-- modal popup for image pop start-->
					<!-- Modal -->
						 <!-- Modal -->
						<%	 imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
								for (int i = 0; i < imagecount; i++) { 
							String index="image"+i;				
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
					    <c:set value="<%=index %>" var="i"></c:set>
					    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="${requestScope[i]}" />
					          
					        </div>
					        <div class="modal-footer cust-modal-footer">
					          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
					        </div>
					      </div>
					      
					    </div>
					  </div>
					  <%} %>
	<div id="allImgPdfUrlToDeleteFile" ></div>
                               </c:if>
							</div>
							<div class="col-md-12 text-center center-block">
						<c:if test="${isContractorEditable==false}">

							<input type="button" name="submitBtn" style="margin-left: 190px;"
								onclick="return showContractorsFunction()"
								class="btn btn-warning btn-custom-width" id="closeBtn"
								value="<spring:message text="Close"/>" />
						</c:if>
						<c:if test="${isContractorEditable!=false}">
							<input type="submit" name="submitBtn"
								onclick="return updateFunction()"
								class="btn btn-warning btn-custom-width" id="updateBtn"
								value="<spring:message text="Update"/>" />
								
							<input type="submit" name="submitBtn"
								onclick="return deleteFunction()"
								class="btn btn-warning btn-custom-width" id="delBtn"
								value="<spring:message text="Delete"/>" />
								
						</c:if>

					</div>
							</form:form>
							</div>
						</div>
					</div>
					

					
			
			<!-- /page content -->
		</div>
	</div>
	






	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
	<script>
		function showContractorsFunction() {
			window.location.assign("viewTransportors.spring");
		}
		function deleteFunction() {
			var result = confirm("do you want to delete?");
			$("#strSubmitBtn").val("Delete");
			
			reloadFunc()
			if (!result) {
				return false;
			}
			$('#updateBtn').attr("disabled", true);
		    $('#delBtn').attr("disabled", true);
			document.getElementById("ProductWiseIndentsFormId").method = "POST";
		    document.getElementById("ProductWiseIndentsFormId").submit();
			
		}
		var first_name = $("#first_name").val();
		var last_name = $("#last_name").val();
		if (first_name.length == 0) {

			$("#first_name").prop("readonly", false);

		}
		if (last_name.length == 0) {
			$("#last_name").prop("readonly", false);
		}

		function updateFunction() {

			reloadFunc()
			$("submitBtn").prop("disabled", "true");
			var first_name = $("#first_name").val();
			var last_name = $("#last_name").val();
			var address = $("#address").val();
			var mobile_number = $("#mobile_number").val();
			var alternate_mobile_number = $("#alternate_mobile_number").val();

			var mobReges = /^[6-9][0-9]{9}$/;
			var mobRegex2 = /^[+][0-9]{2}[6-9][0-9]{9}$/;
			var checkWhiteSpace = new RegExp("/^\s+$/");
			var index = first_name.indexOf(' '); // 4

			if (address.length == 0) {
				alert("Please enter address.");
				$("#address").focus();
				return false;
			}

			if (mobile_number.length != 10 && mobile_number.length != 13) {
				alert("Please enter valid mobile number");
				$("#mobile_number").focus();
				return false;
			} else if (mobile_number.length == 10) {
				if (!mobReges.test(mobile_number)) {
					alert('Not a valid number');
					$("#mobile_number").focus();
					return false;
				}
			} else if (mobile_number.length == 13) {
				if (!mobRegex2.test(mobile_number)) {
					alert('Not a valid number');
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
			/* if (!regpan.test(panVal)) {
				alert(" invalid pan card number");
				return false;
			} */

			var gstin = $("#gstin").val();
			if (gstin.length == 0) {
				alert("Enter Gstin  number");
				return false;
			}
			//if (panVal.length != 0 && gstin.length != 0) {
				//var gstinRegex = /^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
				var gstinRegex =/^\d{2}[A-Z]{5}\d{4}[A-Z]{1}\d[Z]{1}[A-Z\d]{1}/; ///^([0][1-9]|[1-2][0-9]|[3][0-5])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$/;
				if (gstin.length != 0) {
					if (!gstinRegex.test(gstin)||gstin.length>15) {
						alert(" invalid GSTIN number");
						return false;
					}
				}
				//alert(gstin.substring(2, 12) + " " + panVal);
				/* if (gstin.substring(2, 12) != panVal) {
					alert(" Pan number not matching with GSTIN card number.");
					return false;
				} */

			//}
			 var result=validateFileExtention();
			 if(result==false){
				 return false;
			 }

			var result = confirm("do you want to update  transportor details?");
			if (!result) {
				return false;
			}
			excuteDeleteFunction();
			return result;

			/* $("myForm").submit(function(){
			    alert("Submitted");
			}); */
		}//method
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
		
		/* ********************************* this is for set the images /pdf files purpose written this one start()*********************** */
		var deleteUploadedimageFiles=new Array();
		 var deleteUploadedPDFFiles=new Array();
		 
		 
		  function deletepdf(divId,imagePath){
			  debugger;
			  var strName=$('#strName').val();
			  var invoice_Number=$('#invoice_Number').val();
			  var indent_Entry_Number=$('#indent_Entry_Number').val();
			  var site_Id=$('#site_Id').val();
			  var image_Path=$('#image_Path').val();
			 // image_Path=str.replace("Part0",divId);
				 var canISubmit = window.confirm("Do you want to delete PDF?");
				 if(canISubmit == false) {
				        return false;
				 }
				 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
				 $("#pdfAlreadyPresent").val(pdfAlreadyPresent-1);
				 var url="deletettransportorPdfandImage.spring?"+imagePath;
				 
				 
				 
				 deleteUploadedPDFFiles.push({"pdfNo":divId,"url":url});
				 debugger;
				 $("#pdfDivHideShow"+divId).remove();
				 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
				 var classlength=parseInt($(".selectCount").length);
				 //var k=8-(imagesAlreadyPresent+pdfAlreadyPresent);
				 if((pdfAlreadyPresent+imagesAlreadyPresent)==8){
					 $("#files_place").show();
					// $(".files_place1").append('<div class="clearfix"></div><input type="file" id="file_select1" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange(1)"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float:left;display:none;" onclick="filedelete(1)"><i class="fa fa-close"></i></button><button type="button" id="Add" class="btn btn-success btn-xs Mrgtop10" style="display: none;" onclick="addFile(1)"><i class="fa fa-plus"></i> Add File</button></div></div>'); 
				 }
				 
				 
			//	 deleteFile(divId,imagePath);
			}
	  
			function deleteFile(divId,url){
				debugger;
				
				 $.ajax({
					  url : url,
					  type : "get"
					  });
			}
		  
		  function deleteWOImageFile(divId,imagePath){
				 debugger;
				 var strName=$('#strName').val();
				 var invoice_Number=$('#invoice_Number').val();
				 var indent_Entry_Number=$('#indent_Entry_Number').val();
				 var site_Id=$('#site_Id').val(); 
			 var canISubmit = window.confirm("Do you want to delete image?");
			     
			 if(canISubmit == false) {debugger;
			        return false;
			 }
			 $("#imageDivHideShow"+divId).remove();
		
			 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
			 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
			 //var url="deleteInvoicePermanentImage.spring?imagePath="+imagePath+"&strVendorName=${getInvoiceDetails.vendorName}&siteId=${getInvoiceDetails.siteId}&invoiceNumber=${getInvoiceDetails.invoiceNumber}&indentEntryId=${getInvoiceDetails.indentEntryId}";
			 var url="deletettransportorPdfandImage.spring?"+imagePath;	
	
			 deleteUploadedimageFiles.push({"imageNo":divId,"url":url});
			 
			 
			 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
			 if((pdfAlreadyPresent+imagesAlreadyPresent)==8){
				 $("#files_place").show();
				// $(".files_place1").append('<div class="clearfix"></div><input type="file" id="file_select1" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange(1)"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn1" style="float:left;display:none;" onclick="filedelete(1)"><i class="fa fa-close"></i></button><button type="button" id="Add" class="btn btn-success btn-xs Mrgtop10" style="display: none;" onclick="addFile(1)"><i class="fa fa-plus"></i> Add File</button></div></div>'); 
			 }
			// deleteFile(divId,imagePath);
			 return false;
		}
		  
	  		// which is calling from js before submit button executed
			function excuteDeleteFunction(){
				debugger;
				var count=1;
				 deleteUploadedimageFiles.sort(sortUrlData);
					 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedimageFiles.length; imgPdfIndex++) {
							if(deleteUploadedimageFiles[imgPdfIndex].length!=0){
								//deleteFile(imgPdfIndex,deleteUploadedimageFiles[imgPdfIndex].url);
								// here we need to take all the images for back end 
									$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='imgPathtoDelete' id='imgPdfPath"+count+"'>");
									var imgePath=deleteUploadedimageFiles[imgPdfIndex].url.split("deletettransportorPdfandImage.spring?")[1];
									$("#imgPdfPath"+count).val(imgePath);
									count++;
							}
						} 
					 debugger;
					 deleteUploadedPDFFiles.sort(sortPdfData);
					 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedPDFFiles.length; imgPdfIndex++) {
							if(deleteUploadedPDFFiles[imgPdfIndex].length!=0){
							//deleteFile(imgPdfIndex,deleteUploadedPDFFiles[imgPdfIndex].url);
							// here we need to take the path and append the input type to the div tag for all pdfs taken at a time
								$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='PdfPathtodelete' id='imgPdfPath"+count+"'>");
								var pdfPath=deleteUploadedPDFFiles[imgPdfIndex].url.split("deletettransportorPdfandImage.spring?")[1];
								$("#imgPdfPath"+count).val(pdfPath);
							
								count++;
							}
						}
					 debugger;
					// var canISubmit = window.confirm("Do you want to delete image?");
				     
					/*  if(canISubmit == false) {debugger;
					        return false;
					 } */
					 //document.getElementById("updaetTempBtnId").disabled = true;  
					 $('#strSubmitBtn').val("Update");
					 $('#updateBtn').attr("disabled", true);
				     $('#delBtn').attr("disabled", true);
					 document.getElementById("ProductWiseIndentsFormId").method = "POST";
				     document.getElementById("ProductWiseIndentsFormId").submit();
				}
			// sort the files according to order
			function sortPdfData(a,b){
				debugger;
					
					var	pdf=a.pdfNo.split("pdf")[1];
					var	pdf1=b.pdfNo.split("pdf")[1];
					
					
					 if(pdf < pdf1) {
						    return 1;
					 }else if (pdf1 < pdf) {
						    return -1;
					 }else{ 
						return 0;	  
					  }
				}

				function sortUrlData(a, b) {
					debugger;
					
					var	imageNo=a.imageNo.split("image")[1];
					var	imageNo1=b.imageNo.split("image")[1];
					
					
					 if(imageNo < imageNo1) {
						    return 1;
					 }else if (imageNo1 < imageNo) {
						    return -1;
					 }else{ 
						return 0;	  
					  }
				} 
			  function updateTempWO(){
				  debugger;
				   var result=validateFileExtention();
				  if(result==false){
					  return false
				  }  
				    result = confirm("Do you want to update the temp WO");
					if (!result) {
					  return false;
					}
					 debugger;
					excuteDeleteFunction();
				}
	
		/* --------------------------------file added and deleted start-----------------------------------	 */  
		/*script for file upload*/
	/* 	user click on the add button then it will call */
function addFile(id){debugger;
 var classlength=parseInt($(".selectCount").length);
 var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
var k=imagesAlreadyPresent+pdfAlreadyPresent+classlength;

 if($("#file_select"+classLastId).val()==""){
	 alert("Please select file");
	 $("#file_select"+classLastId).focus();
	 return false;
 }
 if(k > 7){
	alert("Unable to upload more than eight files.");
	return false;
 }
 var btnid = $(".selectCount:last").attr("id").split("file_select")[1];
 var dynamicId = parseInt(k) + 1;
 classLastId = parseInt(classLastId) + 1;
 $(".files_place").append('<div class="clearfix"></div><input type="file" id="file_select'+classLastId+'" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange('+classLastId+')"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn'+classLastId+'" style="float:left;display:none;" onclick="filedelete('+classLastId+')"><i class="fa fa-close"></i></button></div></div>');
}
//this is for file change to display close button
function filechange(id){
	debugger;
	
	var size_file = ($("#file_select"+id))[0].files[0]
	file_size = size_file.size;
	if((size_file.type)=='application/pdf'){
		if((file_size/1024)<=1024 && (size_file.type)=='application/pdf'){$("#close_btn"+id).show();
		$("#Add").show();}
		else{
			alert("Uploaded file size("+file_size+ "), So Please upload Below 1 mb.");
			$("#file_select"+id).val("");
		    return false;	
		}
	}
	else{
 $("#close_btn"+id).show();
 $("#Add").show();}
}
//this is for to delete the file
function filedelete(id){
	debugger;
 var classlength=$(".selectCount").length;
 if(classlength==1){
	 $("#file_select"+id).val("");
	 $("#close_btn"+id).hide();
	 $("#Add").hide();
	 return false;
 }
 $("#file_select"+id).remove() ;
 $("#close_btn"+id).remove();
}	  

debugger;
var pdfImgLength=$("#totalCount").val();
if(pdfImgLength==8){
	$("#files_place").hide();
}else{
	$("#files_place").show();
}


//this code for download server images
function toDataURL(url, current) {
 // alert("hai");
	debugger;
    var httpRequest = new XMLHttpRequest();
    httpRequest.onload = function() {
       var fileReader = new FileReader();
          fileReader.onloadend = function() {
             console.log("File : "+fileReader.result);
				 $(current).removeAttr("onclick");
				 $(current).attr("href", fileReader.result);
				 $(current)[0].click();
				 $(current).removeAttr("href");
				 $(current).attr("onclick", "toDataURL('"+url+"', this)");
          }
          fileReader.readAsDataURL(httpRequest.response);
    };
    httpRequest.open('GET', url);
    httpRequest.responseType = 'blob';
    httpRequest.send();
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
		alert("Please upload below 1 mb PDF file.");
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
		alert("Please upload below 1 mb PDF file.");
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
	 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style" id="market_file_style'+dynamicId+'" style="margin-top: 20px;"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" onchange="fileChange('+dynamicId+', this)" class="selectCount" style="display:none;"/><div class=""><span id="fileName'+dynamicId+'"  style="float:left;padding-right: 10px;width:200px;"></span><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;padding: 2px 5px;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
}
//delete file
function filedelete(id){
	var canIDelete=window.confirm("Do you want to delete file?");
	if(canIDelete==false){
		return false;
	}
	$("#market_file_style"+id).remove();
}
function uploadFile(){debugger;
var count=$(".filesCount").length;
var classlength=$(".selectCount").length;
 if((classlength+count)>7){
	alert("Unable to upload more than eight files");
	return false;
}
addFile();
var id=$(".selectCount:last").attr("id").split("file_select")[1];
$("#file_select"+id).trigger("click");		
}
	function start_and_end(str) {
	if (str.length > 25) {
	   return str.substr(0, 10) + '...' + str.substr(str.length-10, str.length);
	}
  		return str;
	}
		/* ********************************* this is for set the images /pdf files purpose written this one end()*********************** */
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

			var condition = "2";

			$("#submitBtn").prop("disabled", true);
			$("#updateBtn").prop("disabled", true);

			$
					.ajax({
						url : "validatContractorname.spring",
						type : "GET",
						data : {
							first_name : first_name,
							last_name : last_name,
							contractor_id : "${ContractorBean.contractor_id}",
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

		$(document)
				.ready(
						$(function() {
							var isContractorEditable = "${isContractorEditable}";
							if (isContractorEditable == "false") {
								$('input[type="text"]').prop('readonly', true);
							}

							$("#panNumber")
									.on(
											"focusout",
											function() {

												debugger;
												var panNumber = $("#panNumber")
														.val();
												var actualPanNumber = $(
														"#actualPanNumber")
														.val();
												if (panNumber == actualPanNumber) {
													return false;
												}
												//alert(panNumber);
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

													return false;
												}
												$("#submitBtn").prop(
														"disabled", true);
												$("#updateBtn").prop(
														"disabled", true);
												var condition = "PAN"
												$
														.ajax({
															url : "validatContractorpanNumber.spring",
															type : "GET",
															data : {
																panNumber : panNumber,
																contractor_id : "${ContractorBean.contractor_id}",
																condition : condition
															},
															success : function(
																	resp) {
																if (resp == false) {
																	alert("Pan number already exists. please use another number ");
																	$(
																			"#submitBtn")
																			.prop(
																					"disabled",
																					true);
																	$(
																			"#updateBtn")
																			.prop(
																					"disabled",
																					true);
																} else {
																	$(
																			"#submitBtn")
																			.prop(
																					"disabled",
																					false);
																	$(
																			"#updateBtn")
																			.prop(
																					"disabled",
																					false);
																}
															},
															error : function(
																	err) {

															}
														});
											});
							$("#gstin")
									.on(
											"focusout",
											function() {
												var gstin = $(this).val();
												var actualgstinNumber = $(
														"#actualgstinNumber")
														.val();
												if (actualgstinNumber == gstin) {
													return false;
												}

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
														$("#gstin").val("");
														$("#submitBtn").prop( "disabled", false);
														$("#updateBtn").prop( "disabled", false);
														return false;
													}
												}

												$("#submitBtn").prop(
														"disabled", true);
												$("#updateBtn").prop(
														"disabled", true);
												var condition = "GSTIN"
												$
														.ajax({
															url : "validatContractorpanNumber.spring",
															type : "GET",
															data : {
																panNumber : gstin,
																contractor_id : "${ContractorBean.contractor_id}",
																condition : condition
															},
															success : function(
																	resp) {
																if (resp == false) {
																	alert("GSTIN number already exists. please use another number ");
																	$(
																			"#submitBtn")
																			.prop(
																					"disabled",
																					true);
																	$(
																			"#updateBtn")
																			.prop(
																					"disabled",
																					true);
																} else {
																	$(
																			"#submitBtn")
																			.prop(
																					"disabled",
																					false);
																	$(
																			"#updateBtn")
																			.prop(
																					"disabled",
																					false);
																}
															},
															error : function(
																	err) {

															}
														});
											});

							$(".up_down")
									.click(
											function() {
												$(this)
														.find('span')
														.toggleClass(
																'fa-chevron-up fa-chevron-down');
												$(this)
														.find('span')
														.toggleClass(
																'fa-chevron-right fa-chevron-left');
											});

						}));
	</script>
<!-- modal popup for image pop start-->
<!-- Modal -->
	 <!-- Modal -->
	<%-- <%	int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			for (int i = 0; i < imagecount; i++) { 
		String index="image"+i;				
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
    <c:set value="<%=index %>" var="i"></c:set>
    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope[i]}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  <%} %> --%>
		<!-- modal popup for invoice image end -->    
<!-- <div id="myModal-img-pop" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    Modal content
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Your Images</h4>
      </div>
      <div class="modal-body ">
        <img src="" class="img-responsive center-block">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-danger text-center center-block" data-dismiss="modal">Close</button>
      </div>
    </div>

  </div>
</div> -->
<!-- modal popup for image pop end -->
</body>
</html>
