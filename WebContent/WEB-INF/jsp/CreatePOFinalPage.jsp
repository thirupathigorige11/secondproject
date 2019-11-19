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
	String billingAddressGSTIN=null;
String finamount=null;
           			String roundoff=null;
           			String gross=null;
           			String inwards=null;
           			String subject=null;
           			String contactPersonName=null;
           			String billingAddress=null;
           			String approverName=null;
           			String approveDate=null;
           			String preparedName=null;
           			String preparedDate=null;
           			String verifyName=null;
           			String verifyDate=null;
           			String indentCreationDate=null;
           			String siteWiseIndentNo=null;
           			String strReceiverContactPerson = "";
           			String strReceiverContactLandLineNo = "";
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
           			String receiverContactPerTwo="";
           			String strReceiverContactPerson1 = "";
           			
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
		indentCreationDate=first[22];
		siteWiseIndentNo=first[23];
		billingAddressGSTIN=first[24];
		strReceiverContactPerson1 = first[25];
		strReceiverContactLandLineNo = first[26];
		billingAddressCompanyName=first[28];
		siteId=first[29];
		siteName=first[30];
		strDeliveryDate=first[31];
		vendorId=first[32];
	//	finalamount=first[11];
	
		//	strRoundOff = first[21];
	  //  strVehicleNo = first[22];
	}
	if(strReceiverContactPerson1!=null && strReceiverContactPerson1.contains(",")){
		//String receiveData[] = null;
		strReceiverContactPerson=strReceiverContactPerson1.split(",")[0];
		receiverContactPerTwo=strReceiverContactPerson1.split(",")[1];
	}else{	
		strReceiverContactPerson=first[25];
		receiverContactPerTwo="";
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
	
	String strTotalBasicAmt="";
	String strTotalCGST="";
	String strTotalSGST="";
	String strTotalIGST="";
	String strSubTotalAmount="";
	String strTransportBasicAmt="";
	String strtransportCGST="";
	String strtransportSGST="";
	String strtransportIGST="";
	
	%>   
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="CacheClear.jsp" />  
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 

		
<title>Create PO</title>

<style>

body {
margin:15px;
}

.site_title{
	    margin-left: 238px;
	}
	table{
		margin:6px auto;
		border:0;
		border-spacing:0;
	}
	table tr td{
		
		border:1px solid #000;
		padding: 0px 5px;
		height:20px;
		text-align:center;
		min-width:36px;
		font-size:12px;
		/* font-weight: bold; */
		/* font-family: Calibri; */
	}
	.contentBold{
	 font-weight: bold;
    font-size: 14px;
    
    }
	.text-center{
		text-align:center;
		
	}
	.text-right{
		text-align:right;
	}
	.text-left{
		text-align:left;
	}
	.lasttd{
		height: 135px;
		text-align: center;
		vertical-align: bottom;
	}
	.br0{
		border-right:0;
	}
	.bt0{
		border-top:0;
	}
	.bb0{
		border-bottom:0;
	}
	.bl0{
		border-left:0;
	}
	.ht150{
		height:150px ;
	}
	.verticalbott{
		vertical-align:bottom;
	}
	.verticaltop{
		vertical-align:top;
	}
	.verticalmiddle{
		vertical-align:middle
	}
	.width11{
		width:11%;
	}
	.width20{
		width:20%;
	}
	.width85{
		min-width:85px;
	}
	.width60{
		min-width:60px
	}
	.width75{
		min-width:75px;
	}
	.pbl0{
		padding-bottom:0;
		padding-left:0;	
	}
	.pbr0{
		padding-bottom:0;
		padding-right:0;
	}
	.pb0{
		padding-bottom:0;
	}
	.fontnormal{
		font-weight:normal;
	}
	.width160{
		min-width:160px;
	}
	.ptitle{
		font-size:21px;
		font-weight:600;
		text-align:center;
	}
	.totalamt{
		font-size:18px;
	}
	.trheadings td{
		font-size:16px;
	}
	.n-b td{font-weight:200}
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
.container-header{
	font-size: 22px;
    background: #b4d4e3;
}
.moveimage{
    position: absolute;
    left: 41px;
    bottom: 170px;
}
.bckcolor{
background: #b4d4e3;
font-size: 18px;
font-weight: bold;
}
.text-img{
	float: left;
	position: relative;
    bottom: -26px;
    right: -38px;
}
.img-table-getinvoice{
	opacity: 1;
    display: block;
    width: 100%;
    height: 150px;
    transition: .5s ease;
    backface-visibility: hidden;
}
.header-content{
font-weight: bold;

}
.footer-bottom{
    padding-bottom: 50px;

}
.headerTitle{
    line-height: 7px;
}
.content-bold{
font-weight: bold;
}
.addres-split{
width:230px;
}
.text-alignment-content{
font-size: 16px;
font-weight: bold;
margin-right: 10px;	
}
.colon-conent{
    margin-right: 10px;
    }
    
    @media print{
#printpage{
display:none;
}
@media print{
#Home_image{
display:none;
}
.pdf-cls{display:none;}
}



@media print {
	.viewQuote {
		display: none;
	}
}

@media print {
	.viewComp,#IMAGEView,#PDFView{
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
}
</style>
</head>

<body>

			<div class="loginbox">
					<div style="border: 0;" id="Home_image" class=" nav_title">
								<a class="site_title" href="getIndentCreationDetails.spring"><img src="images/home.png"></a>
				</div>
			</div>
			
			
<div class="">
		<div class="" style="display: inline-block;width: 41%;text-align: center;font-size: 22px;font-weight: bold;border: 2px solid black;float: left;margin-top: 6px; height: 137px;margin-right: -1px;"><span style="display:inline-block;margin-top:35px;">Purchase Order</span></div>
		<div class="" style="display: inline-block; width: 38%;">

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
		<div class="" style="display: inline-block;width: 15%;">
			<figure>
				<img class="text-responsive"
					style="float: right; position: absolute; top: -6px; right: 24px;    width: 160px;"
					src="images/LOGO_ISO.png" />
			</figure>
		</div>
	</div>
	
	
<%-- <div class="theader" style="border: 1px solid;margin-bottom: 14px;">
	<div class="sction">
		<div class="ttitle" style=" font-size: 21px;margin-left: 248px;margin-bottom: -30px;margin-top: 31px;">
			<span style="margin-top: 35px;position: absolute;font-size: 24px;font-weight: bold;">Purchase Order</span>
		</div>
		<div class="aside-section" style="margin-left: 600px;border-left: 1px solid; height: 101px;">
			<span class="">Version No:<c:out value="${requestScope['versionNo']}"></c:out> </span><br>
			<hr>
			<span class="">Issue Date: <c:out value="${requestScope['strPoPrintRefdate']}"></c:out></span><br><hr>
			<span class="">Reference Number:<c:out value="${requestScope['refferenceNo']}"></c:out> </span><br>
		</div>
		<div class="aside-section" style="border-left: 1px solid;">
			<img class="text-img" style="float: right; margin-top: -123px; margin-right: 59px; border-left: 1px solid; height: 91px;" src="images/LOGO_ISO.png" />
		</div>
	</div>
</div> --%>

<div class="bAddress">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px; margin-top: 38px;">
		<span class="text-alignment-content">Temp PO Number</span><span class="colon-conent" style="margin-left: 39px;">:</span><%=strPONumber%><br>
		<span class="text-alignment-content">Date</span><span class="colon-conent" style="margin-left: 125px;">:</span><%=reqDate%></br>
		<c:if test = "${empty isSiteLevelPo || not isSiteLevelPo}">
		<span class="text-alignment-content">Delivery Date</span><span class="colon-conent" style="margin-left: 67px;">:</span><%=strDeliveryDate%><br>
		</c:if>
	</div>
</div>


<div class="DAddress" style="position: absolute;margin-left: 742px; margin-top: -141px;">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px; margin-left: 99px;">
		<span class="text-alignment-content">Indent Number</span><span class="colon-conent">:</span><%=siteWiseIndentNo%><br>
		<span class="text-alignment-content">Indent Date</span><span class="colon-conent" style="margin-left: 24px;">:</span><%=indentCreationDate%><br>
	</div>
</div>


<%-- <div class="header-content" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">
<span class="text-alignment-content">Temp PO Number</span><span class="colon-conent">:</span><%=strPONumber%><br>
<span class="text-alignment-content">Date</span><span class="colon-conent" style="margin-left: 86px">:</span><%=reqDate%>

</div>
	
	<div class="header-content2" style="float: right;margin-right: 411px;font-size: 13px;font-family: Calibri;margin-top: -58px;" >
	
<span class="text-alignment-content">Indent Number</span><span class="colon-conent">:</span><%=strIndentNo%><br>
	<span class="text-alignment-content">Indent Date</span><span class="colon-conent" style="    margin-left: 24px">:</span><%=indentCreationDate%><br>
	</div> --%>
<div class="sub-cont" style="font-family: Calibri; font-size:14px;margin-bottom: -99px;">

 <br>
</div>
<input type="hidden" name="indentDate" id="indentDate" value="<%=indentCreationDate%>">

</div>

	<div class="header-content1 addres-split"
		style="margin-bottom: 21px;margin-top:15px; font-size: 18px; font-family: Calibri;">
		<span class="content-bold" style="font-size: 19px;">To :</span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;"><%=strVendorName%></span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;width: 285px;display: block;"><%=strVendorAddress%></span>
		<span class="" style="font-size: 16px; font-family: Calibri;"><%=vendorState%></span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN
			: <%=strVendorGSTIN%></span>
	</div>

<%-- <div class="header-content1 addres-split" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">
                                      
<span class="content-bold" style="font-size: 19px;">To : </span><br>
<span class="" style="font-size: 14px; font-family: Calibri;"><%=strVendorName %></span><br>
<span class="" style="font-size: 14px; font-family: Calibri;"><%=strVendorAddress %><span><br>
<span class="" style="font-size: 14px; font-family: Calibri;"><%=vendorState %></span><br>
<span class="" style="font-size: 14px; font-family: Calibri;">GSTN Number: <%=strVendorGSTIN %></span>
</div> --%>
  <div class="header-content1" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">
<span class="content-bold"><span class="text-alignment-content">Kind Attn..</span><span class="colon-conent" style="margin-left: 18px;">:</span><%=contactPersonName%>  </span> <!-- -<input type="text" class="kindAttn" style="width:200px;border: none;"/> -->
</div>	
<div class="header-content1" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">
<span class="content-bold"><span class="text-alignment-content">Sub</span><span class="colon-conent" style="margin-left: 63px;">:</span><%=subject %></span> <!--  -<input type="text" class="subcontent" style="width:200px;border: none;"/> -->
</div>	
<%-- <div class="header-content1" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;width: 206px;">


<span class="content-bold" style="font-size: 19px;">Billing Address</span><br>

<span class="billingaddress" style=" word-wrap: break-word;"><%=billingAddress%></span>

</div> --%>

	<div class="bAddress">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 18px; font-family: Calibri;width: 347px;height: 118px;">
		<div class="content-bold" style="font-size: 16px;">Billing Address</div> 
		<div class="billingaddress" style=" word-wrap: break-word;font-size: 16px;"><%=billingAddressCompanyName%> </div>
		
		<div style="word-break: break-all;"><%=billingAddress%></div>
			<div style="word-break: break-all;">
				<%-- <span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=billingAddressGSTIN%></span> --%>
			<span  style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN</span>:<span style="font-weight: bold;font-size: 16px;"><%=billingAddressGSTIN%></span>
			 <br> 
			 <span style="font-size: 16px; font-family: Calibri;">Type of Transaction:</span>
			 	<span style="font-size: 18px; font-family: Calibri;font-weight: bold;">B2B</span>
			</div>
	</div>
</div>

	<div class="DAddress" style="position: absolute;margin-left: 742px; margin-top: -141px;">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px;">
		<span class="content-bold" style="font-size: 18px;">Delivery Address</span><br> 
			<span class="billingaddress" style=" word-wrap: break-word;"><%=strReceiverName%> , <br><%=strReceiverAddress%> </span>
				<%-- <div style="word-break: break-all;">
				<span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=strReceiverGSTIN%></span>
			</div> --%>
				<% if(strReceiverMobileNo!=null && !strReceiverMobileNo.equals("")){ %>
				<div>
				<span style="font-weight: bold; font-size: 16px;">Contact No
				</span>:<span><%=strReceiverMobileNo%>(<%=strReceiverContactPerson%>)</span>
			</div>
			<%} %>
			<% if(strReceiverContactLandLineNo!=null && !strReceiverContactLandLineNo.equals("")){ %>
			<div>
				<span style="font-weight: bold; font-size: 16px;">Contact No
				</span>:<span><%=strReceiverContactLandLineNo%>(<%=receiverContactPerTwo%>)</span>
			</div>
			<% } %>
	
	</div>
		</div>
<%-- <div class="header-content2" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">


<p style="float: right;margin-top: -98px;margin-right: 231px;font-size: 13px;font-family: Calibri;"><span class="content-bold" style="font-size: 19px;">Delivery Address</span><br>
<span class="text-alignment-content">Name</span><span class="colon-conent" style="margin-left: 57px;">:</span><%=strReceiverName %><br>
<span class="text-alignment-content">Address</span><span class="colon-conent" style="margin-left: 43px;word-wrap: break-word;">:</span><span><%=strReceiverAddress %></span><br>
<span class="text-alignment-content">Mobile</span><span class="colon-conent" style="margin-left: 51px;">:</span><%=strReceiverMobileNo %><br>
<span class="text-alignment-content">GSTIN Number</span><span class="colon-conent" style="margin-left: 1px;">:</span><%=strReceiverGSTIN %></span>
</p>
</div> --%>
<!-- <div class="header-content1" style="float: right;margin-right: 215px;">
Delivery Address<br>
Sumadhura Soham
CBK Plaza, Site 43,2nd Flor<br>
Vartrhur Main Road, Marthahalli,<br>
Banglore-560037<br>
GSTIN Number:81324564
</div> -->


<%   
	String value=request.getAttribute("isCGSTSGST").toString();
	
	if(value.equalsIgnoreCase("true")){
	
	
	%>



    <!-- <table class="table table-bordered" style="font-family: Calibri;    font-weight: bold;margin-top: 80px;width: 1050px;margin-left: 20px;"> -->
    <table style="border:1px solid #000;font-family: Calibri;    font-weight: bold;margin-top: 98px;width: 1050px;;margin-left: 0px;">
    
        <tbody>
              <tr>
                 <tr class="bckcolor" style="font-size: 16px;">
                <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">S.NO </td> 
               <!--  <td  class="text-left"> SubProductName </td> --> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">HSN/SAC</td> 
                <td rowspan="2" class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Description of Goods</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;width:169px;height: 35px;">Vendor Product Desc</td> 
                
                 <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Quantity/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Price/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Basic Amount</td> 
                <td class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Discount</td> 
               <!-- <td class="text-left">Amount after Discount</td>  -->
       		 	 <td colspan="2" style="text-align: center;    font-size: 16px;height: 35px;">CGST</td>
                <td colspan="2" style="text-align: center;    font-size: 16px;height: 35px;">SGST</td>
               <!--  <td colspan="2">IGST</td> -->
       		 	<!--	<td   class="text-left">Amount after tax</td>  -->
	           	<td   class="text-left" style="text-align: center;    font-size: 16px;height: 35px;">Sub Total</td>  
           </tr>
           
            
           <tr class="bckcolor">
        	
           </tr>
      <tr class="bckcolor">
        	
           </tr>
             <tr>
                <% 
                
               
                
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           			
           			
           			 roundoff=splData[18];
           			 gross=splData[19];
           			 inwards=splData[20];
           			 finamount=splData[21];
					noOfRows=splData[0];
					
					 totalCGST=totalCGST+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[11])));
					 totalSGST=totalSGST+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[13])));
					 //totalIGST="";
					 totalBasicAmount=totalBasicAmount+(Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[7]))));
					 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
					 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
					 strTotalBasicAmt=String.format("%.2f",totalBasicAmount);
						strTotalCGST=String.format("%.2f",totalCGST);
						strTotalSGST=String.format("%.2f",totalSGST);
						strSubTotalAmount=String.format("%.2f",subTotalAmount);
					
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
	           			 indentCreationDetailsId=splData[28]; */
           	%>
	            <tr class="n-b">	            	
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[0] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[3] %></td>
	               <%--  <td style="font-size: 16px;font-weight: bold;"><%= splData[1] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;;"><%= splData[2] %></td>
	                 <td style="word-break: break-all;font-weight: bold;"><%= splData[22] %></td>
	                
	                
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[4] %></td>
	                 <td style="font-size: 14px;font-weight: bold;"><%= splData[5] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[6] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[7] %></td>
	             <td style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span>%</span></td>
			<%-- 	<td><%=splData[9]%></td> --%>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[10] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[11] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[12] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[13] %></td>
	               <%--  <td style="font-size: 16px;font-weight: bold;"><%= splData[14] %></td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[15] %></td> --%>
	                	<%-- <td><%=splData[16]%></td> --%>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[17] %></td>
	                  
	                
	          <%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
	            </tr>
           	<% 	} %>           
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
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strTotalBasicAmt%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strTotalCGST%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strTotalSGST%></td>				
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strSubTotalAmount %></td>
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
         	<%--  <% 	} %> --%>    

          <% 
                
               System.out.println("third "+third);
                
           		for(int j=0; j <= third.length-1; j++) {
           			String data = third[j];
           			String splData[] = data.split("@@");
           		  //  System.out.println("splData "+splData);
           			//third = data.split("@@");
           			ConveyanceName = splData[0];
           			ConveyanceAmount = splData[1];
           			GSTTax = splData[2];
           			GSTAmount = splData[3];
           			AmountAfterTax =splData[4];
           			totalamount=splData[5];
           			
           		 TransportBasicAmt=TransportBasicAmt+Double.parseDouble(ConveyanceAmount);
       			 transportCGST=transportCGST+Double.parseDouble(splData[7]);
       			 transportSGST=transportSGST+Double.parseDouble(splData[9]);
           			 
           			//Double percent = 0.0;
           			 
           			
         if(!ConveyanceName.equalsIgnoreCase("None")){   	%>
             <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <!-- <td  class="text-left"><span class="contentBold"></span></td>  -->
                
               <td></td>
               <td style="font-size: 14px;font-weight: bold;"  ><%= ConveyanceName %></td> 
                 <td   class="text-left"><input type="hidden" id="text-content4"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
             	<!--   <td   class="text-left"><span class="contentBold"></span></td>  -->
                <td style="font-size: 14px;font-weight: bold;"  ></td> 
       		 	<!-- <	<td   class="text-left"></td> -->
       		  	<td class="text-center" style="font-size: 14px;font-weight: bold;"><%= ConveyanceAmount %></td>
	           	<td   class="text-left">	<!--<%=splData[6] %> --></td>  	
	          	<td  style="font-size: 14px;font-weight: bold;" class="text-left"><%=splData[6] %></td> 
	          	<td  style="font-size: 14px;font-weight: bold;" class="text-left"><%=splData[7] %><span class="contentBold"></span></td> 
	           	<td  style="font-size: 14px;font-weight: bold;" class="text-left"><%=splData[8] %><span class="contentBold"></span></td> 
	           		<td style="font-size: 14px;font-weight: bold;"><%=splData[9]%></td> 
	          <%-- 	<td style="font-size: 16px;font-weight: bold;" ><%=splData[10]%></td> 
	           	<td style="font-size: 16px;font-weight: bold;"><%=splData[11]%></td>  --%>
	           	 <td style="font-size: 16px;font-weight: bold;"><%=totalamount %></td>
           </tr>
        <% } } %>
 			<%  totalBasicAmount=totalBasicAmount+TransportBasicAmt;
           	totalCGST=totalCGST+transportCGST;
           	totalSGST=totalSGST+transportSGST;
           	totalCGST=Double.parseDouble(new DecimalFormat("##.##").format((totalCGST)));
           	totalSGST=Double.parseDouble(new DecimalFormat("##.##").format((totalSGST)));
           	
           	strTransportBasicAmt=String.format("%.2f",totalBasicAmount);
 			 strtransportCGST=String.format("%.2f",totalCGST);
 			 strtransportSGST=String.format("%.2f",totalSGST);
           
           	%>
   
	                <tr style="border: 2px solid black;">
                <td style="border: 2px solid black;"></td><td style="border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-center totalamt"> Total</td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" colspan="4" class="text-right"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-center"><%=strTransportBasicAmt%></td>
                 <td style="font-size: 16px;font-weight: bold;border: 2px solid black;" colspan="2" class="text-right totalamt"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-center totalamt"><%=strtransportCGST%></td>
                <td style="border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strtransportSGST%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=finamount %></td>
                
                
            </tr>
	    	    
	                
	                <tr>
                <td></td><td></td><!-- <td></td> -->
                <td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Round off</td><td></td><!-- <td></td><td></td> --><td style="font-size: 16px;font-weight: bold;"><%=roundoff %></td>
            </tr>
            
                          <tr>
                <td></td><td></td><!-- <td></td> -->
                <td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Grand Total</td><td></td><!-- <td></td><td></td> --><td style="font-size: 16px;font-weight: bold;"><%=gross %></td>
            </tr>
	    	    
	      <tr>
                <td style="font-size: 16px;font-weight: bold;" colspan="14" class="totalamt text-left" style="font-weight: bold;">Amount in words : <%=inwards %></td><!-- <td></td><td></td><td></td> -->
            </tr>
	         
       
        </tbody>
    </table>
    
    <%} else { %>
    
    <!-- <table class="table table-bordered" style="font-family: Calibri;    font-weight: bold;margin-top: 80px;width: 1054px;margin-left: 20px;"> -->
        <table style="border:1px solid #000;font-family: Calibri;    font-weight: bold;margin-top: 98px;width: 1050px;;margin-left: 0px;">
        <tbody>
              <tr>
                 <tr class="bckcolor" style="text-align: center;font-size: 16px;">
                <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">S.NO </td> 
               <!--  <td  class="text-left"> SubProductName </td>  -->
               <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">HSN/SAC</td> 
                <td rowspan="2" class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Description of Goods</td> 
                <td  class="text-left" style="text-align: center;font-size: 16px;width:169px;height: 35px;">Vendor Product Desc</td> 
                 
                 <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">UOM</td> 
                <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Qty/UOM</td> 
                <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Price/UOM</td> 
                <td  class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Basic Amount</td> 
                <td class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Disc</td> 
               <!-- <td class="text-left">Amount after Discount</td>  -->
       		 	<!--  <td colspan="2">CGST</td>
                <td colspan="2">SGST</td> -->
                <td colspan="2" style="text-align: center;font-size: 16px;height: 35px;">IGST</td>
       		 	<!--	<td   class="text-left">Amount after tax</td>  -->
	           	<td   class="text-left" style="text-align: center;font-size: 16px;height: 35px;">Sub Total</td>  
           </tr>
           
            
           <tr class="bckcolor">
        	
           </tr>
      <tr class="bckcolor">
        	
           </tr>
             <tr>
                <% 
                
               
                
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           			
           			
           			 roundoff=splData[18];
           			 gross=splData[19];
           			 inwards=splData[20];
           			 finamount=splData[21];
				noOfRows=splData[0];
				
				totalIGST=totalIGST+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[15])));
				 
				 totalBasicAmount=totalBasicAmount+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[7])));
				 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
				 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
				 
				 strTotalBasicAmt=String.format("%.2f",totalBasicAmount);
				 strSubTotalAmount=String.format("%.2f",subTotalAmount);
				 strTotalIGST=String.format("%.2f",totalIGST);
				
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
	            <tr class="n-b">	            	
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[0] %></td>
	                 <td style="font-size: 14px;font-weight: bold;"><%= splData[3] %></td>
	               <%--  <td style="font-size: 16px;font-weight: bold;"><%= splData[1] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;;"><%= splData[2] %></td>
	                 <td style="word-break: break-all;font-weight: bold;"><%= splData[22] %></td>
	                
	               
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[4] %></td>
	                 <td style="font-size: 14px;font-weight: bold;"><%= splData[5] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[6] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[7] %></td>
	             <td style="font-size: 14px;font-weight: bold;"><%=splData[8]%><span>%</span></td>
			<%-- 	<td><%=splData[9]%></td> --%>
	                <%-- <td style="font-size: 16px;font-weight: bold;"><%= splData[10] %></td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[11] %></td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[12] %></td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[13] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[14] %></td>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[15] %></td>
	                	<%-- <td><%=splData[]%></td> --%>
	                <td style="font-size: 14px;font-weight: bold;"><%= splData[17] %></td>
	                  
	                
	          <%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
	            </tr>
           	<% 	} %>           
	    <% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
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
				<!-- <td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"><span class="contentBold"></span></td>
				<td class="text-left"></td>
				<td class="text-left"><span class="contentBold"></span></td> -->
				<td></td>
				<td></td>
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
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strTotalBasicAmt%></td>
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				
				<td class="text-left" style="font-size: 14px;font-weight: bold;"></td>
				
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strTotalIGST%></td>
				<td class="text-center" style="font-size: 14px;font-weight: bold;"><%=strSubTotalAmount %></td>
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
         	<%--  <% 	} %> --%>    

          <% 
                
               System.out.println("third "+third);
                
           		for(int j=0; j <= third.length-1; j++) {
           			String data = third[j];
           			String splData[] = data.split("@@");
           		  //  System.out.println("splData "+splData);
           			//third = data.split("@@");
           			ConveyanceName = splData[0];
           			ConveyanceAmount = splData[1];
           			GSTTax = splData[2];
           			GSTAmount = splData[3];
           			AmountAfterTax =splData[4];
           			totalamount=splData[5];
           			
           			TransportBasicAmt=TransportBasicAmt+Double.parseDouble(ConveyanceAmount);
           			transportIGST=transportIGST+Double.parseDouble(splData[11]);
           			 
           			 
           			//Double percent = 0.0;
           			 
           			
          if(!ConveyanceName.equalsIgnoreCase("None")){  	%>
             <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <!-- <td  class="text-left"><span class="contentBold"></span></td>  -->
                
               <td></td>
               <td style="font-size: 14px;font-weight: bold;"  ><%= ConveyanceName %></td> 
                 <td   class="text-left"><input type="hidden" id="text-content4"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
             	<!--   <td   class="text-left"><span class="contentBold"></span></td>  -->
                <td style="font-size: 14px;font-weight: bold;"  ></td> 
       		 	<!-- <	<td   class="text-left"></td> -->
       		  	<td class="text-center" style="font-size: 14px;font-weight: bold;"><%= ConveyanceAmount %></td>
	           	<td   class="text-left">	<!--<%=splData[6] %> --></td>  	
	          <%-- 	<td  style="font-size: 16px;font-weight: bold;" class="text-left"><%=splData[6] %></td> 
	          	<td  style="font-size: 16px;font-weight: bold;" class="text-left"><%=splData[7] %><span class="contentBold"></span></td> 
	           	<td  style="font-size: 16px;font-weight: bold;" class="text-left"><%=splData[8] %><span class="contentBold"></span></td> 
	           		<td style="font-size: 16px;font-weight: bold;"><%=splData[9]%></td>  --%>
	          	<td style="font-size: 14px;font-weight: bold;" ><%=splData[10]%></td> 
	           	<td style="font-size: 14px;font-weight: bold;"><%=splData[11]%></td> 
	           	 <td style="font-size: 14px;font-weight: bold;"><%=totalamount %></td>
           </tr>
        <% } } %>

   <%  totalBasicAmount=totalBasicAmount+TransportBasicAmt;
           totalIGST=totalIGST+transportIGST;
           totalIGST=Double.parseDouble(new DecimalFormat("##.##").format((totalIGST)));
           strTransportBasicAmt=String.format("%.2f",totalBasicAmount);
           strtransportIGST=String.format("%.2f",totalIGST);
           
           %>
	                <tr>
                <!-- <td></td><td></td> --><!-- <td></td> -->
                <td colspan="2" style="border: 2px solid black;" class="text-right totalamt"> </td>
                <td  style="border: 2px solid black;" class="text-center totalamt"> Total</td>
                <td colspan="4" style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-right"></td>
                <td  style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-center"><%=strTransportBasicAmt%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strtransportIGST%></td>
                <td style="font-weight: bold;font-size: 16px;border: 2px solid black;"><%=finamount %></td>
                
            </tr>
	    	    
	                
	                <tr>
                <!-- <td></td><td></td> --><!-- <td></td> -->
                <td style="font-size: 16px;" colspan="10" class="text-right totalamt">Round off</td><td></td><!-- <td></td><td></td> --><td style="font-size: 16px;font-weight: bold;"><%=roundoff %></td>
            </tr>
            
                          <tr>
               <!--  <td></td><td></td> --><!-- <td></td> -->
                <td style="font-size: 16px;font-weight: bold;" colspan="10" class="text-right totalamt">Grand Total</td><td></td><!-- <td></td><td></td> --><td style="font-size: 16px;font-weight: bold;"><%=gross %></td>
            </tr>
	    	    
	      <tr>
                <td style="font-size: 16px;font-weight: bold;" colspan="14" class="totalamt text-left" style="font-weight: bold;">Amount in words : <%=inwards %></td><!-- <td></td><td></td><td></td> -->
            </tr>
	      
        </tbody>
    </table>
    <%
				}
			
			%>
			
    <% List<String> listOfTermsAndConditions = (List) (request.getAttribute("listOfTermsAndConditions"));
		if(listOfTermsAndConditions.size()>0){
		%>
    <div class="footer-content" ><span style="font-size: 16px;font-family: Calibri;font-weight:bold;">Terms & Conditions</span><br>
    <%
   int sno=0;
   // List<String> listOfTermsAndConditions = (List)(request.getAttribute("listOfTermsAndConditions"));
    
    for(int i=0;i<listOfTermsAndConditions.size();i++){
    	
  
         out.println(++sno+"."+listOfTermsAndConditions.get(i)+"<br>");
        
      
        }
        
    
    %>
 <!--   1.	Prices and Taxes mentioned above<br>
    2.	Freight: Included<br>
    3.	Devlivery: As per scedule sheet<br>
  <span class="content-bold">  4. 	Payment: Againest delivery of schedule qty.</span><br>
    5.	Eway bill No: Compulsary along with Invoice copy<br>
    6. 	mentioned the PO Ref No & GSTIN Invoice copy compulsory.<br>
    7.	Purcheser reserves the right to cancel/ hold the purchase order if the delivery are not satisfied.<br> -->
     
     </div>
     <% } %>
     <% String poLevelComments=request.getAttribute("PoLevelComments") == null ? "" : request.getAttribute("PoLevelComments").toString(); 
     if(poLevelComments!=null && !poLevelComments.equalsIgnoreCase("")){
     %>
      <p><strong>Comments:</strong></p>
     <c:out value="${PoLevelComments}"></c:out>
     <%} %>
    <div class="for-stamp"style="margin-bottom: 39px;margin-top: 20px;"><span class="content-bold" style="font-size: 19px;font-family: Calibri;"> For Sumadhura Infracon Pvt Ltd</span></div>
    
		<div class="row">
		<div class="firstRow" style="display: inline-block;width: 30%;margin-bottom: 30px;text-align: center;font-family: calibri;">
			<div><%=approverName%></div>
			<div><%=approveDate %></div>
			<div><span style="font-weight: bold;">Authorized by</span></div>
			
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
    
    <div class="footer-content" style="font-family: calibri; font-size: 16px;font-weight: bold;margin-top: 26px">*THIS IS SYSTEM GENERATED PURCHASE ORDER , SIGNATURE IS NOT REQUIRED . </div>
     <% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>
	<div class="footer-content">
		<h4 style="font-family: Calibri; margin-top: 83px; font-size: 18px;">
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFRA</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -4px;
   "> CON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
		</h4>
		<span style="font-family: Calibri;"> Sy No 148-2E,Nanakram
			Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</span><br> 
				<span style="font-family: Calibri;"><span style="font-weight: bold;font-family: Calibri;"></span> <span style="font-weight: bold;font-family: Calibri;margin-left: 0px;">Email</span>: info@sumadhuragroup.com,<span style="font-weight: bold;font-family: Calibri;"> URL</span>: sumadhuragroup.com,
			<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">CIN</span>: U45200KA2012PTC062071</span><br>

	</div>
<!--      <div class="footer-content">
    <span style="border-bottom: 3px solid #d86464;">SUMADHURA</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -4px;
    display: inline-block;"> INFRACON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
     <div style="font-family: Calibri;"> Sy no 148-2E,Nanakram Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</div><br>
     <span style="font-family: Calibri;"> URL: Sumadhuragroup.org <span style="font-weight:bold;">CIN </span>: U45200KA2012PTC062071</span><br>

     </div> -->


     <%}else{ %>
     
     	<div class="footer-content">
	<h4 style="font-family: Calibri; margin-top: 83px; font-size: 18px;">
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFRA</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -4px;
    "> CON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
		</h4>
		<span style="font-family: Calibri;">43, CKB Plaza, 2nd Floor,
			Varthur Main Road, Marathahalli, Bangalore, Karnataka 560037</span><br>
		<span style="font-family: Calibri;"><span style="font-weight: bold;font-family: Calibri;">Tel</span>:080-42126699, <span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">Email</span>: info@sumadhuragroup.com,<span style="font-weight: bold;font-family: Calibri;"> URL</span>: sumadhuragroup.com,
			<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">CIN</span>: U45200KA2012PTC062071</span><br>

	</div>
<!--      <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: 68px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
     <span style="font-family: Calibri;">43, CKB Plaza, 2nd Floor, Varthur Main Road, Marathahalli, Bengaluru, Karnataka 560037</span><br>
     <span style="font-family: Calibri;">Phone:080-421621470, E info@sumadhuragroup.org, URL: Sumadhuragroup.org, CIN:U45200KA2012PTC062071</span><br>

     </div> -->

     <%} %>
        
<form:form modelAttribute="CreatePOModelForm" id="CreateCentralIndentFormId"   class="form-horizontal" >
<div class="">
					<!-- <input type="button" class="btn btn-warning viewQuote" value="View Quotation" id="saveBtnId" style="width: 139px;" onclick="ViewQuatation()">
					<input type="button" class="btn btn-warning viewComp" value="View Comparision" id="saveBtnId" style="width: 139px;" onclick="viewComparision()"> -->
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
					<input type="hidden" name="indentDate" id="indentDate" value="<%=indentCreationDate%>">
					<input type="hidden" name="poType" value="temporary">	<!-- this line is added for comparison  -->
					
				<% 	
				ProductDetails productDetails = new ProductDetails();
				int noRows=(getPoDetails.size());
				//out.println("the nio of rows"+noRows);
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
			<!-- ****************************************modal pop up for ************************************************************* -->	
				
<%-- <div class="col-md-12">
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
				 <jsp:include page="WorkOrder/ImgPdfCommonJsp.jsp" />  
				<!-- ******************************************modal pop up for pdf******************************************************* -->
				</form:form>
	<div class="col-md-12 text-center center-block">
	  <input type="button" class="btn btn-warning viewQuote" value="View Quotation" id="saveBtnId" style="margin-bottom:10px;" onclick="ViewQuatation()">
	  <input type="button" class="btn btn-warning viewComp" value="View Comparison" id="saveBtnId" style="margin-bottom:10px;" onclick="viewComparision()">
	  <button onclick="myFunction()" class="btn btn-warning" id="printpage" style="margin-bottom:10px;">Print</button>
	</div>
  
    <%-- <%String updateOrNot=request.getAttribute("uploadorNot").toString();
		if(!updateOrNot.equalsIgnoreCase("false")){%> --%>
   <%--  <a href="http://129.154.74.18:8078/Sumadhura_UAT/downloadPdf.spring?&strPONumber=<c:out value="${poNumber}"/>&type=temp" target="_blank"> download PDF </a>	  
     --%>	<%-- <%} %> --%>
    		<!-- jQuery -->
    		<!-- Modal -->
    		<!-- ************************************ -->
<%-- <div id="myModal-pdf" class="modal fade" role="dialog">
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
</div> --%>
  <!--modal popup-->
    		
    		
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
    </script>
    
    
</body>
</html>
