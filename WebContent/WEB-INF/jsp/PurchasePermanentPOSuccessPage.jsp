<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.text.DecimalFormat"%>
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
//	String strRoundOff = null;
//	String strtotal=null;
//	String strgrandtotal=null;
//	String strVehicleNo = null;
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
           			
           			
	if(firstTableData != null) {
		first = firstTableData.split("@@");		
		strPONumber = first[0];
		reqDate = first[1];
		strIndentNo = first[2];
		strVendorName = first[3];
		strVendorAddress = first[4];
		vendorState = first[5];
		strVendorGSTIN = first[6];
		strReceiverName=first[7];
		strReceiverAddress=first[8];
		strReceiverMobileNo = first[9];
		strReceiverGSTIN = first[10];
		subject=first[12];
		contactPersonName=first[13];
		
		billingAddress=first[15];
		approverName=first[16];
		approveDate=first[17];
		preparedName=first[18];
		preparedDate=first[19];
		verifyName=first[20];
		verifyDate=first[21];
		creationDate=first[22];
		siteWiseIndentNo=first[23];
		billingAddressGSTIN=first[24];
		strReceiverContactPerson = first[25];
		strReceiverContactLandLineNo = first[26];
		vendoeEmail = first[27];
		billingAddressCompanyName=first[28];
		siteId=first[29];
		siteName=first[30];
		strDeliveryDate=first[31];
		vendorId=first[32];
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
	
	for(int j=0; j <= third.length-1; j++) {
		String data = third[j];
			String splData[] = data.split("@@");
		  //  System.out.println("splData "+splData);
			//third = data.split("@@");
			ConveyanceName = splData[0];
			if(!ConveyanceName.equalsIgnoreCase("None")){ 
			break;
			}
			}
	double totalCGST=0.0;
	double totalSGST=0.0;
	double totalIGST=0.0;
	double totalBasicAmount=0.0;
	double subTotalAmount=0.0;
	double TransportBasicAmt=0.0;
	double transportCGST=0.0;
	double transportSGST=0.0;
	double transportIGST=0.0;
	%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="CacheClear.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">


<style>
html{overflow-x: auto;padding: 17px !important;width: 100%;}
.cancelPerminentPo{
    margin-top: 10px;
    padding: 7px 12px;
    border-radius: 5px;
    }
.site_title {
	margin-left: 238px;
}
#printPageButton{
    /* margin-right:25px; */
   /*  width: 110px; */
    height: 28px;
   /*  margin-left: 390px; */
    /* background: #b3b5f; */
    margin-top: 10px;
    /* position: absolute; */
    /* margin-left: 50px; */
    /* margin-top: 10px; */
    height: 37px;
   /*  background: #f3ba52; */
    border-radius: 8px;

}
#cancelPerminentpo{
    height: 28px;
   /*  margin-left: 516px; */
    background: #b3b5f;
    margin-top: 10px;
    /* position: absolute; */
    /* margin-left: 50px; */
    /* margin-top: 10px; */
    height: 37px;
   /*  background: #f3ba52; */
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
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
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
   /*  margin-left: 516px; */
    margin-top: 10px;
    height: 37px;
   /*  background: #f3ba52; */
    border-radius: 8px;
}
.viewComp{

	width: 139px;
   /*  margin-left: 349px;
    margin-top: -63px; */
    height: 37px;
    /* background: #f3ba52; */
    border-radius: 8px;
    margin-top:10px;

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
	.viewQuote,.print-hide,#notCancelperminent{
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
	#printPageButton, #cancelPerminentpo {
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
}
</style>
</head>

<body>

 <div class="loginbox">
	<div style="border: 0;" class=" nav_title">
	<%if(Boolean.valueOf(request.getAttribute("imageClick") == null ? "" : request.getAttribute("imageClick").toString())){} else{ %>
		<a class="site_title" href="dashboard.spring"><img
			src="images/home.png"> </a>
			<%} %>
	</div>
</div> 
<div style="width:100%;">
		<div style="display: inline-block; width: 30%;text-align: center;font-size: 22px;font-weight: bold;border: 2px solid black;float: left;margin-top: 6px;padding-top: 50px;height: 137px;">Purchase Order</div>
		<div style="display: inline-block; width: 40%;">
			<table style="width: 100%;border: 1px solid black; border-collapse: collapse;margin-left: -2px;height: 137px;">
				<tr>
					<th style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri;height: 10px;">Version No:</th>
					<th style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['versionNo']}</th>
				</tr>
				<tr>
					<td style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">Issue Date:</td>
					<td style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['strPoPrintRefdate']}</td>
				</tr>
				<tr>
					<td	style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">Refference Number:</td>
					<td style="border: 2px solid black; border-collapse: collapse; padding: 5px; text-align: left;font-weight: bold;font-size: 16px;font-family: Calibri">${requestScope['refferenceNo']}</td>

				</tr>
			</table>
		</div>
		<div  style="display: inline-block; width: 25%;">
			<div style="display: inline-block;float: right;height: 148px;width: 149px;">
				<img class="text-responsive" style="width: 150px;" src="images/LOGO_ISO.png" />
			</div>
		</div>
	</div>


	<div class="bAddress" style="margin-top: 28px;float:left;width:65%;">
		<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;margin-top: 8px;">
			<span class="text-alignment-content">PO Ref No: </span><span
				class="colon-conent" style="margin-left: 40px;">:</span><%=strPONumber%><br>
			<span class="text-alignment-content">PO Date</span><span
				class="colon-conent" style="margin-left: 61px;">:</span><%=reqDate%><br>
		</div>
	</div>


	<div class="DAddress" style="float:right;margin-top:35px;width:35%;">
		<div class="header-content2" style="margin-bottom: 21px; font-size: 19px; font-family: Calibri;">
			<span class="text-alignment-content">Indent No </span><span
				class="colon-conent">:</span><%=siteWiseIndentNo%><br> <span
				class="text-alignment-content">Indent Date</span><span
				class="colon-conent" style="margin-left: -7px;">:</span><%=creationDate%><br>
		</div>
	</div>

	<!-- 
			
	<div class="sub-cont"
		style="font-family: Calibri; font-size: 14px;">

		<br>
	</div>
 -->

	<div class="header-content1 addres-split"
		style="font-size: 13px; font-family: Calibri;">
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
	<div class="bAddress" style="float: left;width: 50%;">
		<div class="header-content2"
			style="margin-bottom: 21px; font-size: 16px; font-family: Calibri; width: 307px;">
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


	<div class="DAddress" style="float:left;width:50%;margin-bottom:30px;">
		<div class="header-content2"
			style="font-size: 18px; font-family: Calibri; width: 100%;float:left;">
			<span class="content-bold" style="font-size: 19px;">Delivery
				Address</span><br> <span class="billingaddress" style=" word-wrap: break-word;"><%=strReceiverName%> , <br><%=strReceiverAddress%> </span>
				<%-- <div style="word-break: break-all;">
				<span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=strReceiverGSTIN%></span>
			</div> --%>
			<div>
			
		
				<span style="font-weight: bold; font-size: 16px;">Contact No:
				</span><span><%=strReceiverMobileNo%>(<%=strReceiverContactPerson%>)</span>
			</div>
			<div>
				<span style="font-weight: bold; font-size: 16px;">Contact No:
				</span><span><%=strReceiverContactLandLineNo%></span>
			</div>
		</div>
	</div>
	
	<%   
	String value=request.getAttribute("isCGSTSGST").toString();
	
	if(value.equalsIgnoreCase("true")){
	
	
	%>

	<table  style="border:1px solid #000;font-family: Calibri;margin-left:0px;"> <!-- class="table table-bordered" -->
		<thead>
		
			<tr class="bckcolor" style="font-family: Calibri; font-weight: bold;margin-top: 80px;width: 1050px;margin-left: 20px;height: 35px;">
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
			</thead>
			<tbody>
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
						
						totalCGST=totalCGST+Double.parseDouble((splData[11]));
						totalCGST=Double.parseDouble(new DecimalFormat("##.##").format((totalCGST)));
						 totalSGST=totalSGST+Double.parseDouble((splData[13]));
						 totalSGST=Double.parseDouble(new DecimalFormat("##.##").format((totalSGST)));
						 //totalIGST="";
						 totalBasicAmount=totalBasicAmount+Double.parseDouble((splData[7]));
						 totalBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format((totalBasicAmount)));
						 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
						 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
						
						ProductDetails productDetails = new ProductDetails();
						productDetails.setProductId(splData[24]);
						productDetails.setProductName(splData[23]);
						productDetails.setSub_ProductId(splData[25]);
						productDetails.setSub_ProductName(splData[1]);
						productDetails.setChild_ProductId(splData[26]);
						productDetails.setChild_ProductName(splData[2]);
						productDetails.setMeasurementId(splData[27]);
						productDetails.setMeasurementName(splData[4]);
						productDetails.setQuantity(splData[5]);
						productDetails.setIndentCreationDetailsId(splData[28]);
						getPoDetails.add(productDetails); 

						 /* productName=splData[23];
	           			 productId=splData[24];
	           			 subProduct=splData[1];
	           			 subProdId=splData[25];
	           			 childProd=splData[2];
	           			 childProdId=splData[26];
	           			 measureMentName=splData[4];
	           			 measurementId=splData[27];
	           			 quantity=splData[5];
	           			 indentCreationDetailsId=splData[28];
						 */
						
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
			<% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
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
				
				<td class="text-left"><span class="contentBold"></span></td>
			</tr>
			<tr>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>				
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;">Sub Total</td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>				
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=totalBasicAmount%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=totalCGST%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=totalSGST%></td>				
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=subTotalAmount%></td>
			</tr>
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
			<%} %>
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
					
					 TransportBasicAmt=TransportBasicAmt+Double.parseDouble(ConveyanceAmount);
           			 transportCGST=transportCGST+Double.parseDouble(splData[7]);
           			 transportSGST=transportSGST+Double.parseDouble(splData[9]);
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
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[7]%><span class="contentBold"></span>
				</td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span class="contentBold"></span>
				</td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[9]%></td>
				<%-- <td style="font-size: 14px;font-weight: bold;"><%=splData[10]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td> --%>
				<td style="font-size: 14px;font-weight: bold;"><%=totalamount%></td>
			</tr>
			
			
			<%
				}	}
			%>
			 <%  totalBasicAmount=totalBasicAmount+TransportBasicAmt;
           	totalCGST=totalCGST+transportCGST;
           	totalSGST=totalSGST+transportSGST;
       		totalCGST=Double.parseDouble(new DecimalFormat("##.##").format((totalCGST)));
       		totalSGST=Double.parseDouble(new DecimalFormat("##.##").format((totalSGST)));
       
           
           %>

			<tr style="border: 2px solid black;">
                <td style="border: 2px solid black;"></td><td style="border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-center totalamt"> Total</td>
                 <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" colspan="4" class="text-right"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-center"><%=totalBasicAmount%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" colspan="2" class="text-right totalamt"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-center totalamt"><%=totalCGST%></td>
                <td style="border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=totalSGST%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=finamount %></td>
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
	
	<table style="border:1px solid #000;font-family: Calibri;    font-weight: bold;margin-top: 98px;width: 1050px;;margin-left: 0px;">  <!-- class="table table-bordered" -->
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
						
						totalIGST=totalIGST+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[15])));
						totalIGST=Double.parseDouble(new DecimalFormat("##.##").format((totalIGST)));
						 totalBasicAmount=totalBasicAmount+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[7])));
						 totalBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format((totalBasicAmount)));
						 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
						 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
						
						ProductDetails productDetails = new ProductDetails();
						productDetails.setProductId(splData[24]);
						productDetails.setProductName(splData[23]);
						productDetails.setSub_ProductId(splData[25]);
						productDetails.setSub_ProductName(splData[1]);
						productDetails.setChild_ProductId(splData[26]);
						productDetails.setChild_ProductName(splData[2]);
						productDetails.setMeasurementId(splData[27]);
						productDetails.setMeasurementName(splData[4]);
						productDetails.setQuantity(splData[5]);
						productDetails.setIndentCreationDetailsId(splData[28]);
						getPoDetails.add(productDetails); 
						/*  productName=splData[23];
	           			 productId=splData[24];
	           			 subProduct=splData[1];
	           			 subProdId=splData[25];
	           			 childProd=splData[2];
	           			 childProdId=splData[26];
	           			 measureMentName=splData[4];
	           			 measurementId=splData[27];
	           			 quantity=splData[5];
	           			 indentCreationDetailsId=splData[28]; */
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
	<% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
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
			<tr>
				<td class="text-left"></td>
				
				<td class="text-left"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;">Sub Total</td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=totalBasicAmount%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				
				<td></td>
				
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=totalIGST%></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=subTotalAmount%></td>
			</tr>
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
				
				<td></td>
				<td></td>
				<td class="text-left"><span class="contentBold"></span></td>
			</tr>
			<%} %>
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
					
					TransportBasicAmt=TransportBasicAmt+Double.parseDouble(ConveyanceAmount);
           			transportIGST=transportIGST+Double.parseDouble(splData[11]);

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
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=ConveyanceAmount%></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-center">
					<%-- <%=splData[6]%> --%></td>
				<%-- <td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[6]%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[7]%><span class="contentBold"></span>
				</td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span class="contentBold"></span>
				</td>
				<td style="font-size: 14px;font-weight: bold;"><%=splData[9]%></td> --%>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[10]%></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td>
				<td style="font-size: 14px;font-weight: bold;"><%=totalamount%></td>
			</tr>
			<%
			}	}
			%>
			<%  totalBasicAmount=totalBasicAmount+TransportBasicAmt;
           totalIGST=totalIGST+transportIGST;
           totalIGST=Double.parseDouble(new DecimalFormat("##.##").format((totalIGST)));
           
           %>

			<tr>
                <!-- <td></td><td></td> --><!-- <td></td> -->
                <td style="border: 2px solid black;"></td>
                <td  style="border: 2px solid black;" class="text-right"></td>
                <td style="border: 2px solid black;" class="text-center totalamt"> Total</td>
                <td colspan="4" style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-right"></td>
                <td  style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-center"><%=totalBasicAmount%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=totalIGST%></td>
                <td style="font-weight: bold;font-size: 16px;border: 2px solid black;"><%=finamount %></td>
                
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
		
	<div class="footer-content" style="margin-bottom:30px;">
		<span
			style="font-size: 16px; font-family: Calibri; font-weight: bold; margin-top: 22px;">Terms
			& Conditions</span><br>
		<%
			int sno = 0;
			List<String> listOfTermsAndConditions = (List) (request.getAttribute("listOfTermsAndConditions"));

			for (int i = 0; i < listOfTermsAndConditions.size(); i++) {

				out.println(++sno + "." + listOfTermsAndConditions.get(i) + "<br>");

			}
		%>

	</div>
<%String strResponse=request.getAttribute("poPage") == null ? "" : request.getAttribute("poPage").toString(); 
//List<String> vendorData=(List<String>) request.getAttribute("cancelPOinternalComments"); 
String EmpComments="";
 /* if((vendorData!=null && vendorData.size()>0)){ */%>
 	<c:if test="${Comments}">
	  <p class="print-hide"><strong>Comments:</strong></p>
    <%-- <% for(int i=0;i<vendorData.size();i++){%> --%>
		  <p class="print-hide"> <c:out value="${cancelPOinternalComments}"></c:out></p> 
		  <p class="print-hide"><strong>Vendor Comments:</strong></p>
		  <p class="print-hide"> <c:out value="${cancelPOVendorComments}"></c:out></p>
		  
	 </c:if>
    <%--  <%}} %>  --%>
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
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFR</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -6px;
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
		/* String strResponse=request.getAttribute("poPage") == null ? "" : request.getAttribute("poPage").toString(); */

				
		if(strResponse.equalsIgnoreCase("true")){ %>
		
		<form:form modelAttribute="CreatePOModelForm" id="CreateCentralIndentFormId"  class="form-horizontal" >
                    <div class="text-left">
					<!-- <input type="button" class="btn btn-warning viewQuote" value="View Quotation" id="saveBtnId" style="width: 139px;" onclick="ViewQuatation()">					
					<input type="button" class="btn btn-warning viewComp" value="View Comparision" id="saveBtnId" style="width: 150px;" onclick="viewComparision()"> -->
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
					<input type="hidden" name="indentDate" id="indentDate" value="<%=creationDate%>">
					
				<% 	
				ProductDetails productDetails = new ProductDetails();
				int noRows=(getPoDetails.size());%>
				<input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value="<%=noRows%>">
			 <% //	out.println("the nio of rows"+noRows);
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
					
				</div>
				
				 <!-- <button onclick="myFunction()" id="printPageButton"
		class="btn btn-warning"
		>Print</button>  -->
</form:form>
<%} %>
<div class="text-left">
<c:if test="${Comments}">
<!-- <button onclick="myFunction()" id="printPageButton" class="btn btn-warning" style="float:left;">Print</button> -->
</c:if>
<form id="">
<%-- <c:if test="${isPerminentCancelPo}">
<button type="button" onclick="" id="cancelPerminentpo" data-toggle="modal" data-target="#cancelPerminentpomodal" class="btn btn-warning" style="float:left;">Cancel PO</button>
</c:if>	
 <c:if test="${isApprove}">
<button type="button"  id="notCancelperminent"  data-toggle="modal" data-target="#DonotcancelPerminentpomodal" class="btn btn-warning cancelPerminentPo" style="float:left;">Reject Cancel PO Request</button>

</c:if> --%>
</form>	
</div>
<form>
<!-- ************************************************this is for pdf ****************************************************************** -->
<%-- <div class="col-md-12"> <!-- style="margin-top: 100px;" -->
<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("count"))); 
  		for(int i=0;i<pdfcount;i++)
			{
			%>
 		
 		 <%if(i==0){ %>
 <div class="col-md-3">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf0']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	<button type="button" class="btn btn-fullwidth" data-toggle="modal" data-target="#myModal-pdf">121212</button>
	
	
  </div>
</div>

 </div>
 <%} if(i==1){ %>
 <div class="col-md-3">
 <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf1']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-fullwidth" data-toggle="modal" data-target="#myModal-pdf2">121212</button>
	
	
  </div>
</div>

 </div>
 <% } if(i==2){ %>
 <div class="col-md-3">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf2']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-fullwidth" data-toggle="modal" data-target="#myModal-pdf3">121212</button>
	
	
  </div>
</div>

 </div>
 <% }if(i==3){ %>
 <div class="col-md-3">
  <div class="pdf-cls" style="margin-bottom:15px;"> 
  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
  <iframe class="iframe-pdf" src="data:application/pdf;base64,${requestScope['pdf3']}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
  <div class="middle">
    <!-- <div class="text">John Doe</div> -->
	
	<button type="button" class="btn btn-fullwidth" data-toggle="modal" data-target="#myModal-pdf4">121212</button>
	
  </div>
</div>
 </div>
 <%} %>

  
 		
 		<%} %>
 		</div> --%>
 		<!-- /*code for iframe pdf*/ -->
<%-- <%String updateOrNot=request.getAttribute("uploadorNot").toString();	
		if(!updateOrNot.equalsIgnoreCase("false")){%> --%>
	<%-- <a href="http://129.154.74.18:8078/Sumadhura_UAT/downloadPdf.spring?&strPONumber=${poNumber}&type=permenent" target="_blank"> download PDF </a>	 --%> 
<%-- <%} %> --%>

<!-- /*butttons for alignment*/ -->
<div class="col-md-12 text-center center-block">
  <input type="button" class="btn btn-warning viewQuote" value="View Quotation" id="saveBtnId" style="width: 139px;" onclick="ViewQuatation()">					
  <input type="button" class="btn btn-warning viewComp" value="View Comparison" id="saveBtnId" style="width: 150px;" onclick="viewComparision()">
  <button onclick="myFunction()" id="printPageButton" class="btn btn-warning">Print</button>
  <c:if test="${isPerminentCancelPo}">
<button type="button" onclick="" id="cancelPerminentpo" data-toggle="modal" data-target="#cancelPerminentpomodal" class="btn btn-warning">Cancel PO</button>
</c:if>	
 <c:if test="${isApprove}">
<button type="button"  id="notCancelperminent"  data-toggle="modal" data-target="#DonotcancelPerminentpomodal" class="btn btn-warning cancelPerminentPo">Reject Cancel PO Request</button>

</c:if>
</div>
</form>
<!-- /*buttons for alignmnet*/ -->


<!-- Modal -->
<div id="myModal-pdf" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF1</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf0']}" style="height:500px;width:100%";>
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModal-pdf2" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF2</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf1']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModal-pdf3" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF3</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf2']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	   </p>
      </div>
    </div>

  </div>
</div>
<div id="myModal-pdf4" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF4</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/pdf;base64,${requestScope['pdf3']}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	   </p>
      </div>
    </div>

  </div>
</div>
  <!--modal popup-->
  <!-- modal popup for cancel perminent po -->
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
          
          <div class="col-md-12" id="vendorComment">
            <div class="form-group">  
                 <label>Internal Comment:</label>       
                <textarea class="form-control" name="normalComment" placeholder="Please enter internal  comment" value="${cancelPOinternalComments}">${cancelPOinternalComments}</textarea>
              
              </div>
            </div>
            <div class="col-md-12">
            <div class="form-group">  
             <label>Vendor Comment:</label>           
                <textarea class="form-control" name="vendorComment" placeholder="Please enter Vendor the comment" value="${cancelPOVendorComments}">${cancelPOVendorComments}</textarea>
              </div>
            </div>
             <div class="col-md-12">
            <div class="form-group">  
             <label>CC Mails:</label>           
                <input class="form-control" name="ccComment" placeholder="Add CC Emails Here" value="${ccEmails}"/>
              </div>
            </div>
            <c:if test="${isApprove}">
            <!-- <div class="col-md-12" >
            <div class="form-group">             
                <textarea class="form-control" name="remarks" placeholder="Please enter the comment"></textarea>
              </div>
            </div> -->
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
         <% 	
				ProductDetails productDetails = new ProductDetails();
				int noRows=(getPoDetails.size());%>
				<input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value="<%=noRows%>">
			 <% //	out.println("the nio of rows"+noRows);
				for(int i=0;i<noRows;i++){ 
					
					productDetails = getPoDetails.get(i); %>
			<input type="hidden" name="quantity<%=i%>" id="" value="<%=productDetails.getQuantity()%>">
			<input type="hidden" name="indentCreationdtlsId<%=i%>" id="" value="<%=productDetails.getIndentCreationDetailsId()%>">
			<% } %>
          </form>
        </div>
        <div class="modal-footer">
         <div class="col-md-12 text-center center-block">
          <button type="button" class="btn btn-warning cancelPerminentPO" data-dismiss="modal" onclick="CancelPerminentPO()">Submit</button>
          <!-- <button type="button" class="btn btn-warning cancelPerminentPOReject" data-dismiss="modal" onclick="CancelPerminentPOReject()">Submit</button> -->
         </div>
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
         <% 	
				ProductDetails productDetails1 = new ProductDetails();
				int noRows1=(getPoDetails.size());%>
				<input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value="<%=noRows%>">
			 <% //	out.println("the nio of rows"+noRows);
				for(int i=0;i<noRows1;i++){ 
					
					productDetails1 = getPoDetails.get(i); %>
			<input type="hidden" name="quantity<%=i%>" id="" value="<%=productDetails1.getQuantity()%>">
			<input type="hidden" name="indentCreationdtlsId<%=i%>" id="" value="<%=productDetails1.getIndentCreationDetailsId()%>">
			<% } %>
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
		
		function ViewQuatation() {

			document.getElementById("CreateCentralIndentFormId").action = "getQuatationDetails.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}
		function viewComparision() {
			//alert("hai")

			document.getElementById("CreateCentralIndentFormId").action = "getComparisionStatement.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}
		 function CancelPerminentPO() {
			//alert("hai")

			document.getElementById("CancelPerminentPo").action = "saveCancelPerminentPODetails.spring";
			document.getElementById("CancelPerminentPo").method = "POST";
			document.getElementById("CancelPerminentPo").submit();
			$('#cancelPerminentpo').attr("disabled", true);
			$('#notCancelperminent').attr("disabled", true);
		} 
		
	       function CancelPerminentPOReject() {
			debugger;
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
