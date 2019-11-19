<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>

<%
	Map<String, String> viewGrnPageDataMap = (Map<String, String>)request.getAttribute("viewPoPageData");
	String firstTableData = viewGrnPageDataMap.get("AddressDetails");
	List<ProductDetails> getPoDetails = new ArrayList<ProductDetails>();
	String strPONumber = null;
	String reqDate = null;
	String strIndentNo = null;
	String siteWiseIndentNo = null;
	String strVendorName = null;
	String strVendorAddress = null;
	String vendorState = null;
	String strVendorGSTIN = null;
	String strReceiverName = null;
	String strReceiverAddress = null;
	String first[] = null;	
	String strReceiverMobileNo = null;
	String strReceiverGSTIN = null;
	String finalamount = null;
String finamount=null;
           			String roundoff=null;
           			String gross=null;
           			String inwards=null;
           			String subject=null;
           			String contactPersonName=null;
           			String billingAddress=null;
           			String billingAddressGSTIN=null;
           			String approverName=null;
           			String approveDate=null;
           			String preparedName=null;
           			String preparedDate=null;
           			String verifyName=null;
           			String verifyDate=null;
           			String creationDate=null;
           			String strReceiverContactPerson = "";
           			String strReceiverContactLandLineNo = "";
           			String vendoeEmail ="";
           			String billingAddressCompanyName=null;
           			String siteName=null;
           			String siteId=null;
           			String vendorId=null;
           			String noOfRows=null;
           			String productName=null;
           			String productId=null;
           			String subProduct=null;
           			String subProdId=null;
           			String childProd=null;
           			String childProdId=null;
           			String measureMentName=null;
           			String measurementId=null;
           			String quantity=null;
           			String indentCreationDetailsId=null;
           			String strDeliveryDate=null;
           			String tempPoOrNot=null;
           			
	if(firstTableData != null) {
		first = firstTableData.split("@@");		
		strPONumber = first[0];
		reqDate = first[1];
		//strIndentNo = first[2];
		strVendorName = first[2];
		strVendorAddress = first[3];
		vendorState = first[4];
		strVendorGSTIN = first[5];
		strReceiverName=first[6];
		strReceiverAddress=first[7];
		strReceiverMobileNo = first[8];
		strReceiverGSTIN = first[9];
		subject=first[11];
		contactPersonName=first[12];
		
		billingAddress=first[14];
		approverName=first[15];
		approveDate=first[16];
		preparedName=first[17];
		preparedDate=first[18];
		verifyName=first[19];
		verifyDate=first[20];
		creationDate=first[21];
		//siteWiseIndentNo=first[22];
		billingAddressGSTIN=first[22];
		strReceiverContactPerson = first[23];
		strReceiverContactLandLineNo = first[24];
		vendoeEmail = first[25];
		billingAddressCompanyName=first[26];
		siteId=first[27];
		siteName=first[28];
		strDeliveryDate=first[29];
		vendorId=first[30];
		tempPoOrNot=first[31];
	//	finalamount=first[11];
	
		//	strRoundOff = first[21];
	  //  strVehicleNo = first[22];
	}
	String second[] = null;
	String secondTableData = viewGrnPageDataMap.get("productDetails");
	if(secondTableData != null) {
		second = secondTableData.split("&&");
	}
	
	
	String third[] = null;
	String thirdTableData = viewGrnPageDataMap.get("TransportDetails");
	System.out.println("thirdTableData "+thirdTableData);
	if(thirdTableData != null) {
		third = thirdTableData.split("&&");
	}
	System.out.println("third "+third.length);
	String ConveyanceName = null;
	String ConveyanceAmount = null;
	String GSTTax = null;
	String GSTAmount = null;
	String AmountAfterTax = null;
	String totalamount = null;
	String CGST = "";
	String SGST = "";
	String IGST = "";
	//Double percent = 0.0;
	String CGSTAMT ="";
	String SGSTAMT ="";
	String IGSTAMT ="";
	String locationData="";
	String four[] = null;
	
	String locationTableData = viewGrnPageDataMap.get("locationDetails");

	System.out.println("*******************************************************************");
	System.out.println("locationdata"+locationTableData);
	if(locationTableData != null && !locationTableData.equals("")) {
		four = locationTableData.split("&&");
		locationData="true";
	}else{
		locationData="false";
	}
		
	%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../CacheClear.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<style>
html{overflow-x: auto;padding: 17px !important;width: 100%;}
.site_title {
	margin-left: 238px;
}
.cancelPerminentPo{
    margin-top: 10px;
    padding: 7px 12px;
    border-radius: 5px;
    }
    
#printPageButton{
   /*  margin-right:25px;   */
    height: 28px;
    background: #b3b5f;
    margin-top: 10px;
    /* position: absolute; */
    /* margin-left: 50px; */
    /* margin-top: 10px; */
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;

}
#cancelPerminentpo{
   /*  margin-right:25px; */
    height: 28px;
   /*  margin-left: 470px; */
    background: #b3b5f;
    margin-top: 10px;
  /*   position: absolute; */
    /* margin-left: 50px; */
    /* margin-top: 10px; */
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;
}
table {
	margin: 6px auto;
	border: 0;
	border-spacing: 0;
}

table tr td {
	border: 1px solid #000;
	padding: 0px 5px;
	height: 20px;
	text-align: center;
	min-width: 36px;
	font-size: 12px;
	/* font-weight: bold; */
	/* font-family: Calibri; */
}

.contentBold {
	font-weight: bold;
	font-size: 14px;
}

.text-center {
	text-align: center;
}

.text-right {
	text-align: right;
}

.text-left {
	text-align: left;
}

.lasttd {
	height: 135px;
	text-align: center;
	vertical-align: bottom;
}

.br0 {
	border-right: 0;
}

.bt0 {
	border-top: 0;
}

.bb0 {
	border-bottom: 0;
}

.bl0 {
	border-left: 0;
}

.ht150 {
	height: 150px;
}

.verticalbott {
	vertical-align: bottom;
}

.verticaltop {
	vertical-align: top;
}

.verticalmiddle {
	vertical-align: middle
}

.width11 {
	width: 11%;
}

.width20 {
	width: 20%;
}

.width85 {
	min-width: 85px;
}

.width60 {
	min-width: 60px
}

.width75 {
	min-width: 75px;
}

.pbl0 {
	padding-bottom: 0;
	padding-left: 0;
}

.pbr0 {
	padding-bottom: 0;
	padding-right: 0;
}

.pb0 {
	padding-bottom: 0;
}

.fontnormal {
	font-weight: normal;
}

.width160 {
	min-width: 160px;
}

.ptitle {
	font-size: 21px;
	font-weight: 600;
	text-align: center;
}

.totalamt {
	font-size: 18px;
}

.trheadings td {
	font-size: 16px;
}

.n-b td {
	font-weight: 200
}
/* ************************************ Prashanth CSS */
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

.container-header {
	font-size: 22px;
	background: #b4d4e3;
}

.moveimage {
	position: absolute;
	left: 41px;
	bottom: 170px;
}

.bckcolor {
	background: #b4d4e3;
	font-size: 18px;
	font-weight: bold;
}

.text-img {
	float: left;
	position: relative;
	bottom: -26px;
	right: -38px;
}

.header-content {
	font-weight: bold;
}

.footer-bottom {
	padding-bottom: 50px;
}

.headerTitle {
	line-height: 7px;
}

.content-bold {
	font-weight: bold;
}

.addres-split {
	width: 230px;
}

.text-alignment-content {
	font-size: 16px;
	font-weight: bold;
	margin-right: 10px;
}

.colon-conent {
	margin-right: 10px;
}

.viewQuote{
	width: 139px;
    margin-left: 516px;
    margin-top: 10px;
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;
}
.viewComp{

	width: 139px;
    margin-left: 349px;
    margin-top: -63px;
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;

}
.img-table-getinvoice{
	opacity: 1;
    display: block;
    width: 100%;
    height: 150px;
    transition: .5s ease;
    backface-visibility: hidden;
}

@media print {
	.viewQuote,#cancelPerminentpo,#notCancelperminent {
		display: none;
	}
	
}

@media print {
	.viewComp {
		display: none;
	}
}

@media print {
	#printPageButton {
		display: none;
	}
}

@media print {
	#printPageButton {
		display: none;
	}
}

@media print {
	.loginbox {
		display: none;
	}
	.pdf-cls{
	display:none;
	}
	.print-hide{
	display:none;
	}
}
</style>
</head>

<body>

	 <div class="loginbox">
	 <c:if test="${vendorMail}">
		<div style="border: 0;" class=" nav_title">
		<%if(Boolean.valueOf(request.getAttribute("imageClick") == null ? "" : request.getAttribute("imageClick").toString())){} else{ %>
			<a class="site_title" href="dashboard.spring"><img
				src="images/home.png"> </a>
				<%} %>
		</div>
		</c:if>
	</div> 
<div style="width:100%;">
		<div style="display: inline-block;
    width: 30%;text-align: center;font-size: 22px;font-weight: bold;border: 2px solid black;float: left;margin-top: 6px;height: 137px;">Purchase Order</span></div>
		<div style="display: inline-block; width: 40%;">

			<table style="width: 100%;border: 1px solid black; border-collapse: collapse;margin-left: -1px;height: 137px;">
				<tr>
					<th
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri;height: 10px;">Version No:</th>
					<th
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['versionNo']}</th>
				</tr>
				<tr>
					<td
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">Issue Date:</td>
					<td
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['strPoPrintRefdate']}</td>

				</tr>
				<tr>
					<td
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">Refference Number:</td>
					<td
						style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['refferenceNo']}</td>

				</tr>
			</table>
		</div>
		<div  style="display: inline-block; width: 20%;">
			<figure>
				<img class="text-responsive"
					style="float: right; position: absolute; top: -6px; right: 94px;    width: 150px;"
					src="images/LOGO_ISO.png" />
			</figure>
		</div>
	</div>


	<div class="bAddress" style="    margin-top: 28px;">
		<div class="header-content2"
			style="margin-bottom: 21px; font-size: 16px; font-family: Calibri; width: 100%; height: 118px; margin-top: 8px;">
			<div class="text-alignment-content" style="width:145px;float:left;"> <%=tempPoOrNot%> </div><span class="colon-conent" style="margin-left: 40px;">:</span><%=strPONumber%><br>
			<div class="text-alignment-content" style="width:145px;float:left;">Date</div><span class="colon-conent" style="margin-left: 39px;">:</span><%=reqDate%><br>
		<c:if test = "${tempPOOrNot}">
		<span class="text-alignment-content">Delivery Date</span><span class="colon-conent" style="margin-left: 92px;">:</span><%=strDeliveryDate%><br>
		</c:if>
		</div>
	</div>


	<%-- <div class="DAddress"
		style="position: absolute; margin-left: 742px; margin-top: -141px;">
		<div class="header-content2"
			style="margin-bottom: 21px; font-size: 19px; font-family: Calibri; width: 357px; height: 118px; margin-left: 145px;">
			<span class="text-alignment-content">Indent No </span><span
				class="colon-conent">:</span><%=siteWiseIndentNo%><br> <span
				class="text-alignment-content"> Date</span><span
				class="colon-conent" style="margin-left: 43px;">:</span><%=creationDate%><br>
		</div>
	</div>
 --%>
	
			
	<div class="sub-cont"
		style="font-family: Calibri; font-size: 14px; margin-bottom: -75px;">

		<br>
	</div>


	<div class="header-content1 addres-split"
		style="margin-bottom: 21px; font-size: 13px; font-family: Calibri;">
		<span class="content-bold" style="font-size: 22px;">To :</span><br>
		<span class="" style="font-size: 18px; font-family: Calibri;font-weight: bold;"><%=strVendorName%></span><br>
		<span class="" style="font-size: 18px; font-family: Calibri;width: 285px;display: block;"><%=strVendorAddress%></span>
		<span class="" style="font-size: 18px; font-family: Calibri;"><%=vendorState%></span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;">Email:<%=vendoeEmail%></span>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN: <%=strVendorGSTIN%></span>


	</div>
	<div class="header-content1"
		style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;">
		<span class="content-bold"><span class="text-alignment-content">Kind Attn..</span><span class="colon-conent" style="margin-left: 13px;">:</span><%=contactPersonName%>
		</span>
		<!-- -<input type="text" class="kindAttn" style="width:200px;border: none;"/> -->
	</div>
	<div class="header-content1"
		style="margin-bottom: 21px; font-size: 18px; font-family: Calibri;">
		<span class="content-bold"><span class="text-alignment-content">Sub</span><span
			class="colon-conent" style="margin-left: 58px;">:</span><%=subject%></span>
		<!--  -<input type="text" class="subcontent" style="width:200px;border: none;"/> -->
	</div>
	<div class="bAddress">
		<div class="header-content2"
			style="margin-bottom: 21px; font-size: 16px; font-family: Calibri; width: 307px; height: 118px;">
			<span class="content-bold" style="font-size: 19px;">Billing Address</span><br> 
				<div class="billingaddress" style="word-wrap: break-word;font-size: 18px;"><%=billingAddressCompanyName%></div>
				<div style="word-break: break-all;"><%=billingAddress%></div>
			<div>
				<%-- <span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=billingAddressGSTIN%></span> --%>
	 <span  style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN</span>:<span style="font-weight: bold;font-size: 16px;"><%=billingAddressGSTIN%></span>
			 <br> 	
			 <span style="font-size: 16px; font-family: Calibri;">Type of Transaction:</span>
			 	<span style="font-size: 18px; font-family: Calibri;font-weight: bold;">B2B</span>
			</div>
		</div>
	</div>


	<div class="DAddress"
		style="position: absolute; margin-left: 742px; margin-top: -141px;">
		<div class="header-content2"
			style="margin-bottom: 21px; font-size: 18px; font-family: Calibri; width: 357px; height: 118px;">
			<span class="content-bold" style="font-size: 19px;">ProjectName</span><br> <span class="billingaddress" style=" word-wrap: break-word;"><%=strReceiverName%></span>
				<div style="word-break: break-word;">
				<span style="font-weight: bold; font-size: 16px;"></span><span><%=strReceiverAddress%></span>
			</div> 
			<div>
			
		
				<span style="font-weight: bold; font-size: 16px;">
				</span><span></span>
			</div>
			<div>
				<span style="font-weight: bold; font-size: 16px;">
				</span><span></span>
			</div>
		</div>
	</div>
	
	<%   
	String value=request.getAttribute("isCGSTSGST").toString();
	
	if(value.equalsIgnoreCase("true")){
	
	
	%>

	<table  style="border:1px solid #000;font-family: Calibri; margin-top: 97px;margin-left: 0px;"> <!-- class="table table-bordered" -->
		<tbody>

			<tr>
			<tr class="bckcolor" style="font-family: Calibri;    font-weight: bold;margin-top: 80px;width: 1050px;margin-left: 20px;height: 35px;">
				<td class="text-left" style="text-align: center;    font-size: 16px;">S.NO</td>
				<!-- <td class="text-left">SubProductName</td> -->
				<td class="text-left" style="text-align: center;    font-size: 16px;">HSN/SAC</td>
				<td rowspan="2" class="text-left" style="word-break: break-all;text-align: center;font-size: 16px;width:200px;">Description of Goods</td>
				<td class="text-left" style="text-align: center;    font-size: 16px;">Vendor Product Desc</td>
				
				<td class="text-left" style="text-align: center;    font-size: 16px;">UOM</td>
				<td class="text-left" style="text-align: center;    font-size: 16px;">Qty/UOM</td>
				<td class="text-left" style="text-align: center;    font-size: 16px;">Price/UOM</td>
				<td class="text-left" style="text-align: center;    font-size: 16px;">Basic Amount</td>
				<td class="text-left" style="text-align: center;    font-size: 16px;"> Disc</td>
				<!--  <td class="text-left">Amount after Discount</td>  -->
				<td colspan="2" style="text-align: center;    font-size: 16px;">CGST</td>
				<td colspan="2" style="text-align: center;    font-size: 16px;">SGST</td>
				<!-- <td colspan="2">IGST</td> -->
				<!--  	<td   class="text-left">Amount after tax</td>  -->
				<td class="text-left" style="text-align: center;    font-size: 16px;">Sub Total</td>
			</tr>


			<tr class="bckcolor">

			</tr>
			<tr class="bckcolor">

			</tr>
			<tr>
				<%
					for (int j = 0; j <= second.length - 1; j++) {
						String data = second[j];
						String splData[] = data.split("@@");

						roundoff = splData[18];
						gross = splData[19];
						inwards = splData[20];
						finamount = splData[21];
						noOfRows=splData[0];
						
						
						
				%>
			
			<tr class="n-b" style="font-size: 16px;font-weight: bold;">
				<td style="font-size: 14px;font-weight: bold;"><%=splData[0]%></td>
					<td style="font-size: 14px;font-weight: bold;"><%=splData[3]%></td>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%=splData[1]%></td> --%>
				<td style="word-break: break-all;font-size: 14px;font-weight: bold;width:200px;"><%=splData[2]%></td>
				<td style="word-break: break-all;font-size: 14px;font-weight: bold;width:200px;"><%=splData[22]%></td>
				
			<td style="font-size: 14px;font-weight: bold;"><%=splData[4]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[5]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[7]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span>%</span></td>
				<%-- 	<td><%=splData[9]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%= splData[10] %></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[12]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[13]%></td>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%= splData[14] %></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[15]%></td> --%>
				<%-- <td><%=splData[16]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[17]%></td>


				<%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
			</tr>
			<%
				}
			%>

			</tr>

			<%--  <% 	} %> --%>
			<tr>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				<td class="text-left"></td>
				<td class="text-left"><input type="hidden" id="text-content3" /><span
					class="contentBold"></span></td>
				<td class="text-left"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td></td>
				<td></td> -->
				<td class="text-left"><span class="contentBold"></span></td>
			</tr>
			<%
				System.out.println("third " + third);

				for (int j = 0; j <= third.length - 1; j++) {
					String data = third[j];
					String splData[] = data.split("@@");
					//  System.out.println("splData "+splData);
					//third = data.split("@@");
					ConveyanceName = splData[0];
					ConveyanceAmount = splData[1];
					GSTTax = splData[2];
					GSTAmount = splData[3];
					AmountAfterTax = splData[4];
					totalamount = splData[5];

					//Double percent = 0.0;
			%>
			
		<% 	if(!ConveyanceName.equalsIgnoreCase("None")){  %>
			
			
			<tr>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				
				<td></td>
				<td style="word-break: break-all;font-size: 14px;font-weight: bold;width:200px;"><%=ConveyanceName%></td>
				<td class="text-left"><input type="hidden" id="text-content4" /><span
					class="contentBold"></span></td>
				<td class="text-left"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				<td></td>
				<td style="font-size: 14px;font-weight: bold;"><%=ConveyanceAmount%></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left">
					<%-- <%=splData[6]%> --%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[7]%><span class="contentBold"></span>
				</td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span class="contentBold"></span>
				</td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[9]%></td>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%=splData[10]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%=totalamount%></td>
			</tr>
			
			
			<%
				}	}
			%>
			<tr>
				<td></td>
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Total</td>
				<!-- <td></td>
				<td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;"><%=finamount%></td>

			</tr>



			<tr>
				<td></td>
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Round off</td>
				<td></td>
				<!-- <td></td>
				<td></td> -->
				<td style="font-size: 16px;font-weight: bold;"><%=roundoff%></td>
			</tr>

			<tr>
				<td></td>
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Grand Total</td>
				<td></td>
				<!-- <td></td>
				<td></td> -->
				<td style="font-size: 16px;font-weight: bold;"><%=gross%></td>
			</tr>

			<tr>
				<td colspan="14" class="totalamt text-left"
					style="font-weight: bold;">Amount in words : <%=inwards%></td>
				<!-- <td></td>
				<td></td> -->
				<!-- <td></td> -->
			</tr>

		</tbody>
	</table>
	
	<%} else { %>
	
	<table style="border:1px solid #000;font-family: Calibri;margin-left:0px;font-weight: bold;margin-top: 98px;width: 1050px;;">  <!-- class="table table-bordered" -->
		<tbody>



			<tr>
			<tr class="bckcolor" style="font-size: 16px;height: 35px;">
				<td class="text-left" style="text-align: center;font-size: 16px;">S.NO</td>
				<!-- <td class="text-left">SubProductName</td> -->
				<td class="text-left" style="text-align: center;font-size: 16px;">HSN/SAC</td>
				<td rowspan="2" class="text-left" style="text-align: center;font-size: 16px;width: 160px;">Description of Goods</td>
				<td class="text-left" style="text-align: center;font-size: 16px;">Vendor Product Desc</td>
				
				<td class="text-left" style="text-align: center;font-size: 16px;">UOM</td>
				<td class="text-left" style="text-align: center;font-size: 16px;">Qty/UOM</td>
				<td class="text-left" style="text-align: center;font-size: 16px;">Price/UOM</td>
				<td class="text-left" style="text-align: center;font-size: 16px;">Basic Amount</td>
				<td class="text-left" style="text-align: center;font-size: 16px;">Disc</td>
				<!--  <td class="text-left">Amount after Discount</td>  -->
				<!-- <td colspan="2">CGST</td>
				<td colspan="2">SGST</td> -->
				<td colspan="2" style="text-align: center;font-size: 16px;">IGST</td>
				<!--  	<td   class="text-left">Amount after tax</td>  -->
				<td class="text-left" style="text-align: center;font-size: 16px;">Sub Total</td>
			</tr>


			<tr class="bckcolor">

			</tr>
			<tr class="bckcolor">

			</tr>
			<tr>
				<%
					for (int j = 0; j <= second.length - 1; j++) {
						String data = second[j];
						String splData[] = data.split("@@");

						roundoff = splData[18];
						gross = splData[19];
						inwards = splData[20];
						finamount = splData[21];
						noOfRows=splData[0];
						
						
				%>
			
			<tr class="n-b" style="font-size: 16px;font-weight: bold;">
				<td style="font-size: 14px;font-weight: bold;"><%=splData[0]%></td>
					<td style="font-size: 14px;font-weight: bold;"><%=splData[3]%></td>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%=splData[1]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;width: 200px;word-break: break-all;"><%=splData[2]%></td>
				<td style="word-break: break-all;font-size: 14px;font-weight: bold;"><%=splData[22]%></td>
			
				<td style="font-size: 14px;font-weight: bold;"><%=splData[4]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[5]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[7]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span>%</span></td>
				<%-- 	<td><%=splData[9]%></td> --%>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%= splData[10] %></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[12]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[13]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%= splData[14] %></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[15]%></td>
				<%-- <td><%=splData[16]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[17]%></td>


				<%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
			</tr>
			<%
				}
			%>

			</tr>

			<%--  <% 	} %> --%>
			<tr>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				<td class="text-left"></td>
				<td class="text-left"><input type="hidden" id="text-content3" /><span
					class="contentBold"></span></td>
				<td class="text-left"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<!-- <td class="text-left"></td> -->
				<!-- <td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"></td>
				<td class="text-left"><span class="contentBold"></span></td> -->
				<td></td>
				<td></td>
				<td class="text-left"><span class="contentBold"></span></td>
			</tr>
			<%
				System.out.println("third " + third);

				for (int j = 0; j <= third.length - 1; j++) {
					String data = third[j];
					String splData[] = data.split("@@");
					//  System.out.println("splData "+splData);
					//third = data.split("@@");
					ConveyanceName = splData[0];
					ConveyanceAmount = splData[1];
					GSTTax = splData[2];
					GSTAmount = splData[3];
					AmountAfterTax = splData[4];
					totalamount = splData[5];

					//Double percent = 0.0;
			if(!ConveyanceName.equalsIgnoreCase("None")){       %>
			<tr>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				
				<td></td>
				<td style="font-size: 14px;font-weight: bold;width: 200px;word-break: break-all;"><%=ConveyanceName%></td>
				<td class="text-left"><input type="hidden" id="text-content4" /><span
					class="contentBold"></span></td>
				<td class="text-left"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<!-- <td class="text-left"><span class="contentBold"></span></td> -->
				<td></td>
				<td style="font-size: 14px;font-weight: bold;"><%=ConveyanceAmount%></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left">
					<%-- <%=splData[6]%> --%></td>
				<%-- <td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[7]%><span class="contentBold"></span>
				</td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span class="contentBold"></span>
				</td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[9]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[10]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=totalamount%></td>
			</tr>
			<%
			}	}
			%>
			

			<tr>
				<!-- <td></td> -->
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Total</td>
				<!-- <td></td>
				<td></td>
				<td></td> -->
				<td style="font-size: 16px;font-weight: bold;"><%=finamount%></td>

			</tr>



			<tr>
				<!-- <td></td> -->
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 14px;font-weight: bold;" colspan="10" class="text-right totalamt">Round off</td>
				<!-- <td></td>
				<td></td>
				<td></td> -->
				<td style="font-size: 16px;font-weight: bold;"><%=roundoff%></td>
			</tr>

			<tr>
				<!-- <td></td> -->
				<!-- <td></td> -->
				<td></td>
				<td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Grand Total</td>
				<!-- <td></td>
				<td></td>
				<td></td> -->
				<td style="font-size: 16px;font-weight: bold;"><%=gross%></td>
			</tr>

			<tr>
				<td colspan="14" class="totalamt text-left"
					style="font-weight: bold;">Amount in words : <%=inwards%></td>
				<!-- <td></td>
				<td></td> -->
				<!-- <td></td> -->
			</tr>
	</tbody>
	</table>
	<%} %>
	
	
	<%--  <c:set var="name" value="Dinesh" scope="page" />
 Local Variable : <c:out value="${name}" /> --%>
 
	<%-- <c:out value="${locationData}"/>   --%>
	<% if(locationData.equalsIgnoreCase("true")){ %>
	
		<div style="width:80%;margin-top:35px;">
				     <h4 style="">Location & Duration Details</h4>
						 <table class="tbl" style="width:100%;" cellspacing="0">
							<thead>
							
						     <tr>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">S.No</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">Description Of Goods</th>
						         <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">Location</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">From Date</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">To Date</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">Time</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">Quantity</th>
						        <th style="background-color:#b4d4e3;border:1px solid #000;padding:10px;">Site Name</th>
						        
						     </tr>
						     
						     
						   </thead>
	 						<tbody>
	<%
			int sNum=1;
					for (int j = 0; j <= four.length - 1; j++) {
						String data = four[j];
						String lData[] = data.split("@@");
						
						%>
						
							 <tr>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=sNum%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[0]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[1]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[2]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[3]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[4]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[5]%></td>
						       <td style="border:1px solid #000;padding:10px;text-align:center;"><%=lData[6]%></td>
						  
						     </tr>
						     
						     <% sNum++; } %>
						   </tbody>
						</table>
				    </div>
				    <%} %>
		
	<div class="footer-content" style="margin-bottom:30px;">
		<span
			style="font-size: 16px; font-family: Calibri; font-weight: bold; margin-top: 22px;">Terms& Conditions</span><br>
		<%
			int sno = 0;
			List<String> listOfTermsAndConditions = (List) (request.getAttribute("listOfTermsAndConditions"));
			
			for (int i = 0; i < listOfTermsAndConditions.size(); i++) {
			out.println(++sno + "." + listOfTermsAndConditions.get(i) + "<br>");

			}
		%>

	</div>
	

 	<c:if test="${Comments}">
	  <p class="print-hide"><strong>Comments:</strong></p>
    <%-- <% for(int i=0;i<vendorData.size();i++){%> --%>
		  <p class="print-hide"> <c:out value="${cancelPOinternalComments}"></c:out></p> 
		  <p class="print-hide"><strong>Vendor Comments:</strong></p>
		  <p class="print-hide"> <c:out value="${cancelPOVendorComments}"></c:out></p>
		  
	 </c:if>
	 <div class="for-stamp" style="margin-bottom: 40px;
    margin-top: 20px;">
		<span class="content-bold"
			style="font-size: 19px; font-family: Calibri;"> For Sumadhura
			Infracon Pvt Ltd</span>
	</div>
	<div class="row">
		<div class="firstRow" style="display: inline-block;width: 30%;margin-bottom: 30px;text-align: center;font-family: calibri;">
			<div><%=approverName%></div>
			<div><%=approveDate %></div>
			<div><span style="font-weight: bold;">Authorised by</span></div>
			
		</div>
		<div class="secondRow" style="display: inline-block;width: 30%;margin-bottom: 30px;text-align: center;font-family: calibri;">
			<div><%=preparedName%></div>
			<div><%=preparedDate%></div>
			<div><span  style="font-weight: bold;">Prepared by</span></div>
		</div>
		<div class="thirdRow" style="display: inline-block;width: 30%;margin-bottom: 30px;text-align: center;font-family: calibri;">
			<div><%=verifyName%></div>
			<div><%=verifyDate%></div>
			<div><span  style="font-weight: bold;">Verified by</span></div>
		</div>
	</div>
	<div class="sig-content" style="font-family: calibri; font-size: 16px; font-weight: bold; margin-top: 25px;;">*THIS IS SYSTEM GENERATED PURCHASE ORDER , SIGNATURE IS NOT REQUIRED .
	</div>
	<%-- <div class="for-stamp1" style="margin-top: -18px;">
		<span class="content-bold"
			style="font-size: 13px; font-family: Calibri; margin-left: 367px;">
			<span class="preparedclss"
			style="position: absolute; margin-top: 36px; margin-left: -260px; font-family: Calibri;font-size: 16px;">Authorised
				by</span> <span
			style="margin-left: -302px; font-size: 16px; font-family: Calibri; width: auto;"><%=approverName%><span
				style="margin-left: 5px;font-size: 12px;"><%=approveDate %></span> </span> </span>
	</div>

	<div class="for-stamp1" style="margin-top: -18px;margin-left: -29px;">
		<span class="content-bold"
			style="font-size: 13px; font-family: Calibri; margin-left: 468px;">
			<span class="preparedclss"
			style="position: absolute; margin-top: 37px; margin-left: 17px; font-family: Calibri;font-size: 16px;">
				Prepared by</span> <span
			style="margin-left: -46px; font-family: Calibri; width: auto;font-size: 16px;"><%=preparedName%><span
				style="margin-left: 4px;font-size: 12px;"><%=preparedDate%></span> </span> </span>
	</div>

	<div class="for-stamp1" style="margin-top: -18px;">

		<span class="content-bold"
			style="font-size: 13px; font-family: Calibri; margin-left: 546px;">
			<span class="preparedclss"
			style="position: absolute; margin-top: 36px; margin-left: 281px; font-family: Calibri">Verified
				by</span> <span
			style="margin-left: 203px; font-size: 13px; font-family: Calibri; width: auto;font-size: 16px;"><%=verifyName  %><span
				style="margin-left: 4px;font-size: 12px;"><%=verifyDate%></span> </span> </span>
	</div> --%> 


	<% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>

		
	<div class="footer-content">
		<h4 style="font-family: Calibri; margin-top: 35px; font-size: 22px;">
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFR</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -5px;
   "> ACON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
		</h4>
		<span style="font-family: Calibri;"> Sy No 148-2E,Nanakram
			Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</span><br> 
				<span style="font-family: Calibri;"><span style="font-weight: bold;font-family: Calibri;"></span> <span style="font-weight: bold;font-family: Calibri;margin-left: 0px;">Email</span>: info@sumadhuragroup.com,<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;"> URL</span>: Sumadhuragroup.com,
			<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">CIN</span>: U45200KA2012PTC062071</span><br>

	</div>
	

	<%}else{ %>
	<div class="footer-content">
			<h4 style="font-family: Calibri; margin-top: 35px; font-size: 22px;">
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFR</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -5px;
   "> ACON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
		</h4>
		<span style="font-family: Calibri;">43, CKB Plaza, 2nd Floor,
			Varthur Main Road, Marathahalli, Bangalore, Karnataka 560037</span><br>
		<span style="font-family: Calibri;"><span style="font-weight: bold;font-family: Calibri;">Tel</span>:080-42126699, <span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">Email</span>: info@sumadhuragroup.com,<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;"> URL</span>: sumadhuragroup.com,
			<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">CIN</span>: U45200KA2012PTC062071</span><br>

	</div>

	<%} %>
	

	
		<%
		String strResponse=request.getAttribute("poPage") == null ? "" : request.getAttribute("poPage").toString();

				
		if(strResponse.equalsIgnoreCase("true")){ %>
		
		<%-- <form:form modelAttribute="CreatePOModelForm" id="CreateCentralIndentFormId"   class="form-horizontal" >
		<div class="col-sm-3 pt-10">
					<input type="button" class="btn btn-warning viewQuote" value="View Quotation" id="saveBtnId" style="width: 139px;" onclick="ViewQuatation()">
					<input type="button" class="btn btn-warning viewComp" value="View Comparision" id="saveBtnId" style="width: 150px;" onclick="viewComparision()">
					<input type="hidden" name="vendorName1" id="vendorName1" value="<%=strVendorName%>">
					<input type="hidden" name="indentNumber" id="indentNumber" value="<%=strIndentNo%>">
					<input type="hidden" name="vendorAddress1" id="vendorAddress1" value="<%=strVendorAddress%>">
					<input type="hidden" name="strGSTINNumber1" id="strGSTINNumber1" value="<%=strVendorGSTIN%>">
					<input type="hidden" name="siteWiseIndentNo" id="siteWiseIndentNo" value="<%=siteWiseIndentNo%>">
					<input type="hidden" name="siteId" id="siteId" value="<%=siteId%>">
					<input type="hidden" name="siteName" id="siteName" value="<%=siteName%>">
					<input type="hidden" name="vendorId1" id="vendorId1" value="<%=vendorId%>">
					<input type="hidden" name="poNumber" id="poNumber" value="<%=strPONumber%>">
					<input type="hidden" name="numberOfRows" id="numberOfRows" value="<%=noOfRows%>">
					
				<% 	
				ProductDetails productDetails = new ProductDetails();
				int noRows=(getPoDetails.size());
			//	out.println("the nio of rows"+noRows);
				for(int i=1;i<=noRows;i++){ 
					
					productDetails = getPoDetails.get(i-1); %>
				
				<input type="hidden" name="checkboxname<%=i%>" id="" value="checked">
           			<input type="hidden" name="productId<%=i%>" id="productId${i}" value="<%=productDetails.getProductId()%>">
           			<input type="hidden" name="subProductId<%=i%>" id="" value="<%=productDetails.getSub_ProductId()%>">
           			<input type="hidden" name="childProductId<%=i%>" id="" value="<%=productDetails.getChild_ProductId()%>">
           			<input type="hidden" name="unitsOfMeasurementId<%=i%>" id="" value="<%=productDetails.getMeasurementId()%>">
           			<input type="hidden" name="product<%=i%>" id="" value="<%=productDetails.getProductName()%>">
           			<input type="hidden" name="subProduct<%=i%>" id="" value="<%=productDetails.getSub_ProductName()%>">
           			<input type="hidden" name="childProduct<%=i%>" id="" value="<%=productDetails.getChild_ProductName()%>">
           			<input type="hidden" name="unitsOfMeasurement<%=i%>" id="" value="<%=productDetails.getMeasurementName()%>">
           			<input type="hidden" name="strRequestQuantity<%=i%>" id="" value="<%=productDetails.getQuantity()%>">
				<input type="hidden" name="indentCreationDetailsId<%=i%>" id="" value="<%=productDetails.getIndentCreationDetailsId()%>">
				<%} %>
					
				</div> --%>
				
				 <!-- <button onclick="myFunction()" id="printPageButton"
		class="btn btn-warning"
		>Print</button>  -->
<%-- </form:form> --%>
<%} %>
<div class="col-md-12 text-center center-block">
  <c:if test="${Comments}">
<button onclick="myFunction()" id="printPageButton" class="btn btn-warning">Print</button>
</c:if>
<c:if test="${isPerminentCancelPo}">
<button type="button" onclick="" id="cancelPerminentpo" data-toggle="modal" data-target="#cancelPerminentpomodal" class="btn btn-warning">Cancel PO</button>
</c:if>	
<c:if test="${isApprove}">
<button type="button"  id="notCancelperminent"  data-toggle="modal" data-target="#DonotcancelPerminentpomodal" class="btn btn-warning cancelPerminentPo" >Reject Cancel PO Request</button>

</c:if>	
</div>
<form>
<jsp:include page="../WorkOrder/ImgPdfCommonJsp.jsp" />
<!-- ************************************************this is for pdf ****************************************************************** -->
<%-- <div class="col-md-12" style="margin-top: 100px;">
<% int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); %>
<%	for(int i=0;i<8;i++){
  	String pdfName="pdf"+i;
  	log(pdfName);
%>
 <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
 <% if(request.getAttribute(pdfName)!=null){ %>
				
			 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
			  <div class="pdf-cls" style="margin-bottom:15px;"> 
			  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
			  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
			  <div class="middle">
				<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf<%=i%>"><i class="fa fa-close"></i></button>
			</div>
			</div>
			</div>
<%} }%>
</div>
<div class="col-md-12 Mrgtop20">
					<!-- <h3 class="h3image">You can see the Images</h3> -->
			 <% int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			 	for (int i = 0; i < 8; i++) {
				String imageB64="image"+i;		
				String deleteB64="delete"+i;
				log(imageB64);
				if(request.getAttribute(imageB64)!=null)
				out.print("<div class='col-md-4 Mrgtop20'>");
			%>
											
				<c:set value="<%=imageB64 %>" var="index"></c:set>
				<c:set value="<%=deleteB64 %>" var="delete"></c:set>
			<% if(request.getAttribute(imageB64)!=null){
				log(imageB64);
			%>
									<div class="pdf-image<%=i%>">
									 <div class="container-1" id='imgId<%=imageB64 %>'>
													<img class="img-responsive img-table-getinvoice print-hide"  alt="img" src="data:image/jpeg;base64,${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
									</div></div>
									<% }
									if(request.getAttribute(imageB64)!=null)
									out.print("</div>");
								 }	%>
							
		</div>
		
		<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
		<input type="hidden" name="pdfAlreadyPresent" value="<%=pdfcount%>"> --%>
			
 		
</form>



<!-- Modal -->
<!-- model popup for pdf start  -->
	<%-- <%
	  int pdfcount1 = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
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
<%} %> --%>


<!-- pdf model popup end  -->
	  
    
  <%-- <%} %> --%>
		<!-- modal popup for invoice image end -->  
  <!-- Modal -->
	<%-- <%	 int imagecount1 = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
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
  <%} %> --%>



  <!--modal popup-->
   <!-- Modal -->
  <div class="modal fade" id="cancelPerminentpomodal" role="dialog" >
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Comments</h4>
        </div>
        
        <div class="modal-body" style="overflow:hidden;">
          <form class="form-horzontal" id="CancelPerminentPo">
          
          <div class="col-md-12">
            <div class="form-group">
            <label>Internal Comment:</label>         
                <textarea class="form-control" name="normalComment" placeholder="Please enter comment" value="${cancelPOinternalComments}">${cancelPOinternalComments}</textarea>
              </div>
            </div>
            <div class="col-md-12">
            <div class="form-group"> 
            <label>Vendor Comment:</label>             
                <textarea class="form-control" name="vendorComment" placeholder="Please enter the comment" value="${cancelPOVendorComments}">${cancelPOVendorComments}</textarea>
              </div>
            </div>
            <div class="col-md-12">
            <div class="form-group">  
             <label>CC Mails:</label>           
                <input class="form-control" name="ccComment" placeholder="Add CC Emails Here" value="${ccEmails}"/>
              </div>
            </div>
            <c:if test="${isApprove}">
             <input type="hidden" name="isApprove" id="poNumber" value="true">
            </c:if>
            <input type="hidden" name="poNumber" id="poNumber" value="<%=strPONumber%>">
        <input type="hidden" name="siteId" id="siteId" value="<%=siteId%>">
        <input type="hidden" name="vendorId" id="vendorId" value="<%=vendorId%>">
         <input type="hidden" name="poentryId" id="poentryId" value="${poentryId}">
          <input type="hidden" name="poDate" id="poDate" value="<%=reqDate%>">
		<input type="hidden" name="siteName" id="siteName" value="<%=siteName%>">
		 <input type="hidden" name="poType" id="poType" value="${poType}">
         <input type="hidden" name="chekFromMail" id="chekFromMail" value="${chekFromMail}">
         <input type="hidden" name="password" id="password" value="${password}">
         <input type="hidden" name="userId" id="userId" value="${userId}">
         <input type="hidden" name="urlName" id="urlName" value="${urlName}">
          </form>
        </div>
        <div class="modal-footer text-center center-block">
          <button type="button" class="btn btn-warning" data-dismiss="modal" onclick="CancelPerminentPO()">Submit</button>
        </div>
      </div>
      
    </div>
  </div>
  <!-- modal popup for cancel perminent po -->
<!-- modal popup for Donot cancel perminent po -->
   <!-- Modal -->
  <div class="modal fade" id="DonotcancelPerminentpomodal" role="dialog" >
    <div class="modal-dialog modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title text-center">Comments</h4>
        </div>
        
        <div class="modal-body" style="overflow:hidden;">
          <form class="form-horzontal" id="DonotCancelPerminentPo">
          
        
            <div class="col-md-12">
            <div class="form-group">             
                <textarea class="form-control" name="Remarks" placeholder="Please enter the comment"></textarea>
              </div>
            </div>
            <c:if test="${isApprove}">
             
             <input type="hidden" name="isApprove" id="poNumber" value="true">
            </c:if>
            <input type="hidden" name="poNumber" id="poNumber" value="<%=strPONumber%>">
        <input type="hidden" name="siteId" id="siteId" value="<%=siteId%>">
        <input type="hidden" name="vendorId" id="vendorId" value="<%=vendorId%>">
         <input type="hidden" name="poentryId" id="poentryId" value="${poentryId}">
         <input type="hidden" name="indentNumber" id="indentNumber" value="<%=strIndentNo%>">
         <input type="hidden" name="numberOfRows" id="numberOfRows" value="<%=noOfRows%>">
         <input type="hidden" name="poDate" id="poDate" value="<%=reqDate%>">
         <input type="hidden" name="siteName" id="siteName" value="<%=siteName%>">
         <input type="hidden" name="poTotal" id="poTotals" value="<%=gross%>">
          <input type="hidden" name="poType" id="poType" value="${poType}">
         <input type="hidden" name="chekFromMail" id="chekFromMail" value="${chekFromMail}">
         <input type="hidden" name="password" id="password" value="${password}">
         <input type="hidden" name="userId" id="userId" value="${userId}">
         <input type="hidden" name="urlName" id="urlName" value="${urlName}">
        
          </form>
        </div>
        <div class="modal-footer">
         <div class="col-md-12 text-center center-block">
       <c:if test="${isApprove}">       
          <button type="button" class="btn btn-warning cancelPerminentPOReject" data-dismiss="modal" onclick="CancelPerminentPOReject()">Submit</button>
       </c:if>
       </div>
       </div>
      </div>
      
    </div>
  </div>
  <!-- modal popup for Donot cancel perminent po -->
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/indentReceive.js" type="text/javascript"></script>

	<script>
		function myFunction() {
			window.print();
		}
		function CancelPerminentPO() {
			//alert("hai")

			document.getElementById("CancelPerminentPo").action = "saveCancelPerminentPODetails.spring";
			document.getElementById("CancelPerminentPo").method = "POST";
			document.getElementById("CancelPerminentPo").submit();
			debugger;
			$('#cancelPerminentpo').attr("disabled", true);
			$('#notCancelperminent').attr("disabled", true);
		}
		function CancelPerminentPOReject() {
			//alert("hai")

			document.getElementById("DonotCancelPerminentPo").action = "CancelPerminentPODetailsReject.spring";
			document.getElementById("DonotCancelPerminentPo").method = "POST";
			document.getElementById("DonotCancelPerminentPo").submit();
			$('#cancelPerminentpo').attr("disabled", true);
			$('#notCancelperminent').attr("disabled", true);
		}

		
	</script>


</body>
</html>
