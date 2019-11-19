<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    String ccEmailId = "";
	String strIndentCreationDate = null;
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
    String preparedDate=null;
    String preparedName=null;
    String indentCreationdtlsId=null;
    String siteId=null;
    String verifyName=null;
    String verifyDate=null;
    String siteWiseIndentNo=null;	
    String billingAddressCompanyName=null;
    String strReceiverContactPerson=null;
String siteName=null;
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
           			String revision_No=null;
           			String old_Po_Number=null;
           			String siteLevelPoPreparedBy=null;
           			String strDeliveryDate=null;
    String strReceiverContactLandLineNo=null;
    String strBillingAddressGSTIN=null;
    String userId=null;
    String mailPassword=null;
    String receiverContactPerTwo="";
    String strReceiverContactLandLineNo1="";
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
		subject = first[12];
		contactPersonName=first[13];
	    ccEmailId = first[14];
        billingAddress=first[15];
	    preparedName=first[16];
	    preparedDate=first[17];
	   // indentCreationdtlsId=first[18];
	    siteId=first[19];
	    verifyName=first[20];
	    verifyDate=first[21];
	    strIndentCreationDate = first[22];
	    siteWiseIndentNo = first[23];
	    billingAddressCompanyName= first[24];
	    strReceiverContactLandLineNo1=first[25];
	     strReceiverContactLandLineNo=first[26];
	     strBillingAddressGSTIN=first[27];
		siteId=first[28];
		siteName=first[29];
		vendorId=first[30];
		revision_No=first[31];
		old_Po_Number=first[32];
		strDeliveryDate=first[33];
		siteLevelPoPreparedBy=first[34];
		//mailPassword=first[34];
	}
	
	if(strReceiverContactLandLineNo1!=null && strReceiverContactLandLineNo1.contains(",")){
		//String receiveData[] = null;
		strReceiverContactPerson=strReceiverContactLandLineNo1.split(",")[0];
		receiverContactPerTwo=strReceiverContactLandLineNo1.split(",")[1];
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

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="CacheClear.jsp" />  
<!-- <link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" /> -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script> 



		
<title> PO Approval</title>

<style>
.anchor-description{text-decoration:underline;color:#0000ff;}
.anchor-description:hover{color: #23527c;}
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
.header-content{
/* font-weight: bold; */

}
.footer-bottom{
    padding-bottom: 50px;

}
.headerTitle{
    line-height: 7px;
}
.content-bold{
/* font-weight: bold; */
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
  .table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th{
  border: 1px solid #2d2a2a;
  }
  @media print{
#printpage,#IMAGEView,#PDFView{
display:none;
}
.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th{
  border: 1px solid #2d2a2a;
  }
}
 @media print{
#Home_image{
display:none;
}

}

@media print {
	.viewQuote {
		display: none;
	}
	.print-btn{
	display:none;
	}
}
.img-temppo{
	 top:-6px;
	}
@media print {
	.viewComp {
		display: none;
	}
	.img-temppo{
	 top:-120px;
	}
	.pdf-cls{
	display:none;
	}
}

.notemaindiv{
 	margin-bottom:30px;
 	margin-top:30px;
}
.notecls{
    text-align: left;
    margin-left: -11px;
}
.notetextarea{
 	resize:vertical;
 	min-height: 30px;
}
.img-table-getinvoice{
	opacity: 1;
    display: block;
    width: 100%;
    height: 150px;
    transition: .5s ease;
    backface-visibility: hidden;
}
</style>
</head>

<body>

<form:form modelAttribute="CreatePOModelForm" id="ProductWiseIndentsFormId" class="form-horizontal">
			<div class="loginbox">
					<div style="border: 0;" id="Home_image" class=" nav_title">
								<a class="site_title" href="getIndentCreationDetails.spring"><img src="images/home.png"></a>
				</div>
			</div>
			
<div class="row" style="margin-left: 20px;">
		<div class="col-md-5" style="display: inline-block; width: 36%; text-align: center; font-size: 22px; font-weight: bold; border: 2px solid black; padding: 20px;float: left;
    margin-top: 6px;height: 137px;"><span style="display:inline-block;margin-top:35px;">Purchase Order</span></div>
		<div class="col-md-5" style="display: inline-block; width: 40%;">

			<table style="width: 105%;border: 1px solid black; border-collapse: collapse;margin-left: -17px;height: 137px;">
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
		<div class="col-md-2" style="display: inline-block; width: 10%;float:right;">
   <figure>
    <img class="text-responsive img-temppo"
     style="float: right;right: -76px;"
     src="images/LOGO_ISO.png" />
   </figure>
  </div>
	</div>
<div class="bAddress"     style="margin-left: 20px">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px; margin-top: 38px;">
		<span class="text-alignment-content">Temp PO Number</span><span class="colon-conent" style="margin-left: 39px;">:</span><%=strPONumber%><br>
		<span class="text-alignment-content">Date</span><span class="colon-conent" style="margin-left: 125px;">:</span><%=reqDate%><br>
		<span class="text-alignment-content">Delivery Date</span><span class="colon-conent" style="margin-left: 67px;">:</span><%=strDeliveryDate%><br>
	<input type="hidden" name="strPONumber" id="strPONumber" value=<%=strPONumber%>>
<input type="hidden" name="siteId" id="siteId" value=<%=siteId%>>
<input type="hidden" name="Email" id="Email" value="false">
<input type="hidden" name="deliveryDate" id="deliveryDate" value=<%=strDeliveryDate%>>
<input type="hidden" name="indentDate" id="indentDate" value="<%=strIndentCreationDate%>">
	<c:set var="ponumber" value="<%=strPONumber%>" />
	</div>
</div>


<div class="DAddress" style="position: absolute;margin-left: 742px; margin-top: -141px;">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px; margin-left: 99px;">
		<span class="text-alignment-content">Indent Number</span><span class="colon-conent">:</span><%=siteWiseIndentNo%><br>
		<span class="text-alignment-content">Indent Date</span><span class="colon-conent" style="margin-left: 24px;">:</span><%=strIndentCreationDate%><br>
	</div>
</div>

<div class="sub-cont" style="font-family: Calibri; font-size:14px;margin-bottom: 37px;">

 <br>
</div>
<input type="hidden" name="strIndentNo" id="strIndentNo" value=<%=strIndentNo%>>
<input type="hidden" name="fromdate" id="fromdate" value="${fromDate}">
<input type="hidden" name="toDate" id="toDate" value="${toDate}">
 <input type="hidden" name="password" id="password" value="${mailPasswd}"> 
</div>

	<div class="header-content1 addres-split"
		style="margin-bottom: 21px; font-size: 18px; font-family: Calibri;margin-left: 20px;   margin-top: -99px">
		<span class="content-bold" style="font-size: 19px;">To :</span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;"><%=strVendorName%></span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;width: 285px;display: block;"><%=strVendorAddress%></span>
		<span class="" style="font-size: 16px; font-family: Calibri;"><%=vendorState%></span><br>
		<span class="" style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN
			: <%=strVendorGSTIN%></span>
	</div>

  <div class="header-content1" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;margin-left: 20px;">
<span class="content-bold"><span class="text-alignment-content">Kind Attn..</span><span class="colon-conent" style="margin-left: 18px;">:</span><%=contactPersonName%>  </span> <!-- -<input type="text" class="kindAttn" style="width:200px;border: none;"/> -->
</div>	
<div class="header-content1" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;margin-left: 20px;">
<span class="content-bold"><span class="text-alignment-content">Sub</span><span class="colon-conent" style="margin-left: 63px;">:</span><%=subject %></span> <!--  -<input type="text" class="subcontent" style="width:200px;border: none;"/> -->
<input type ="hidden" name="subject" value="<c:out value='<%=subject %>'/> "/>
<input type ="hidden" name="revision_No" value="<%=revision_No %>"/>
<input type ="hidden" name="oldPoNumber" value="<%=old_Po_Number %>"/>
<input type ="hidden" name="siteLevelPoPreparedBy" value="<%=siteLevelPoPreparedBy %>"/>
<input type ="hidden" name="siteName" value="<%=siteName %>"/>
<input type="hidden" name="vendorId" id="vendorId" value="<%=vendorId%>">
<input type="hidden"  class="form-control"  name="url" value="${requestScope['url']}">

<input type="hidden" name="oldPOEntryId" id="oldPOEntryId" value="${old_PO_EntryId}">
<input type="hidden" name="oldPODate" id="oldPODate" value="${old_PO_Date}">
</div>	

	<div class="bAddress" style="margin-left: 20px">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 18px; font-family: Calibri;width: 347px;height: 118px;">
		<span class="content-bold" style="font-size: 18px;font-weight: bold;">Billing Address</span> 
			<div class="billingaddress" style="word-wrap: break-word;font-size: 18px;"><%=billingAddressCompanyName %></div>
			<%-- <div style="word-wrap: break-word;font-size: 18px;"><%=billingAddress %></div> --%>
			<div style="word-break: break-all;"><%=billingAddress%></div>
			 <div>
			 <span  style="font-size: 16px; font-family: Calibri;font-weight: bold;">GSTIN</span>:<span style="font-weight: bold;"><%=strBillingAddressGSTIN%></span>
			 <br> 
			 	<%-- <span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=strBillingAddressGSTIN%></span> --%>
			 	 <span style="font-size: 16px; font-family: Calibri;">Type of Transaction:</span>
			 	<span style="font-size: 18px; font-family: Calibri;font-weight: bold;">B2B</span>
			</div> 
	</div>
</div>

	<div class="DAddress" style="position: absolute;margin-left: 742px; margin-top: -141px;">
	<div class="header-content2" style="margin-bottom: 21px; font-size: 16px; font-family: Calibri;width: 357px;height: 118px;">
		<span class="content-bold" style="font-size: 18px;font-weight: bold;">Delivery Address</span><br> 
			<span class="billingaddress" style=" word-wrap: break-word;"><%=strReceiverName%> , <br><%=strReceiverAddress%> </span>
				
				<%-- <div>
			 	<span style="font-weight: bold; font-size: 16px;">GSTIN</span>:<span><%=strReceiverGSTIN%></span>
			</div>  --%>
				<% if(strReceiverMobileNo!=null && !strReceiverMobileNo.equals("")){ %>
				<div>
				<span style="font-weight: bold; font-size: 16px;">Contact No
				</span>:
				 <span><%=strReceiverMobileNo%>(<%=strReceiverContactPerson%>)</span> 
			</div>
			<%} %>
			<% if(strReceiverContactLandLineNo!=null && !strReceiverContactLandLineNo.equals("")){ %>
			<div>
				<span style="font-weight: bold; font-size: 16px;">Contact No
				</span>:
			 <span><%=strReceiverContactLandLineNo%>(<%=receiverContactPerTwo%>)</span> 
			</div>
		<%} %>
	
	
	</div>
		</div>
		


<%   
	String value=request.getAttribute("isCGSTSGST").toString();
	
	if(value.equalsIgnoreCase("true")){
	
	
	%>

    <table class="" style="font-family: Calibri;font-weight: bold;margin-top: 80px;width: 1144px;margin-left: 20px;border: 2px solid black;">
        <tbody>
              <tr>
                 <tr class="bckcolor">
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">S.NO </td> 
               <!--  <td  class="text-left"> SubProductName </td>  -->
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">HSN/SAC</td> 
                <td rowspan="2" class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Desciption of Goods</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Vendor Product Desc</td> 
                
                 <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Qty/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Price/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Basic Amount</td> 
                <td class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;"> Disc</td> 
              <!--   <td class="text-left">Amount after Discount</td>  -->
       		 	 <td colspan="2" style="text-align: center;    font-size: 16px;border: 2px solid black;">CGST</td>
                <td colspan="2" style="text-align: center;    font-size: 16px;border: 2px solid black;">SGST</td>
                <!-- <td colspan="2">IGST</td> -->
       		 <!-- 	<td   class="text-left">Amount after tax</td>  -->
	           	<td   class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Sub Total </td>  
           </tr>
           
            
   
      <tr class="bckcolor" style="border: 2px solid black;">
        	
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
					userId=splData[30];
					totalCGST=totalCGST+Double.parseDouble((splData[11]));
					totalCGST=Double.parseDouble(new DecimalFormat("##.##").format((totalCGST)));
					 totalSGST=totalSGST+Double.parseDouble((splData[13]));
					 totalSGST=Double.parseDouble(new DecimalFormat("##.##").format((totalSGST)));
					 //totalIGST="";
					 totalBasicAmount=totalBasicAmount+Double.parseDouble((splData[7]));
					 totalBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format((totalBasicAmount)));
					 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
					 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
					 
					 strTotalBasicAmt=String.format("%.2f",totalBasicAmount);
						strTotalCGST=String.format("%.2f",totalCGST);
						strTotalSGST=String.format("%.2f",totalSGST);
						strSubTotalAmount=String.format("%.2f",subTotalAmount);
					 
					 ProductDetails productDetails = new ProductDetails();
					productDetails.setProductId(splData[25]);
					productDetails.setProductName(splData[24]);
					productDetails.setSub_ProductId(splData[26]);
					productDetails.setSub_ProductName(splData[1]);
					productDetails.setChild_ProductId(splData[27]);
					productDetails.setChild_ProductName(splData[2]);
					productDetails.setMeasurementId(splData[28]);
					productDetails.setMeasurementName(splData[4]);
					productDetails.setQuantity(splData[5]);
					productDetails.setIndentCreationDetailsId(splData[23]);
					getPoDetails.add(productDetails); 
					
				//	out.println("the list size"+getPoDetails.size());
					/* 
					 productName=splData[24];
           			 productId=splData[25];
           			 subProduct=splData[1];
           			 subProdId=splData[26];
           			 childProd=splData[2];
           			 childProdId=splData[27];
           			 measureMentName=splData[4];
           			 measurementId=splData[28];
           			 quantity=splData[5];
           			 indentCreationDetailsId=splData[23]; */
           	%>
	            <tr class="n-b" style="border: 2px solid black;">	            	
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[0] %></td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[3] %></td>
	             <%--    <td style="font-size: 16px;font-weight: bold;"><%= splData[1] %></td> --%>
	                <td style="font-size: 16px;font-weight: bold;width: 160px;border: 2px solid black;"><input type="hidden" name="sendChildProductName<%= splData[0] %>" id="sendChildProductName<%= splData[0] %>" value="<c:out value='<%= splData[2]  %>'></c:out>">   <a href="javascript:void(0)"  class="anchor-description"  onclick="loadPriceMasterData('<%=splData[27]%>','<%= splData[2].replaceAll("[^A-Za-z 0-9]", "") %>','${site_idForPriceMaster}','${siteNameForPriceMaster}',<%= splData[0] %>)">   <%= splData[2] %> </a> </td>
	                 <td style="font-size: 16px;font-weight: bold;word-break: break-all;width: 250px;border: 2px solid black;"><%= splData[22] %></td>
	                
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[4] %></td>
	                 <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[5] %></td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[6] %></td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[7] %></td>
	               	<td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[8] %>%</td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"> <%= splData[10] %> </td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[11] %></td>
	               <%--  <td><%= splData[11] %></td> --%>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[12] %></td>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[13] %></td>
	               <%--  <td style="font-size: 16px;font-weight: bold;"><%= splData[14] %></td>
	                 <td style="font-size: 16px;font-weight: bold;"><%= splData[15] %></td> --%>
	            <%--     <td><%= splData[16] %></td> --%>
	                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%= splData[17] %></td>
	        
	         <input type="hidden" name="quantity<%=j%>" id="quantity<%=j%>" value=<%=splData[5]%>>          
	         <input type="hidden" name="indentCreationdtlsId<%=j%>" id="indentCreationdtlsId<%=j%>"  value=<%=splData[23]%>>
	        <input type="hidden" name="userId" id="userId" value="<%=userId%>">
	     
	         
	          <%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
	            </tr>
           	<% 	} %>           
	        <input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value=<%=second.length%>>    
            </tr>
            <% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
           <tr>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"><input type="hidden" id="text-content3" /><span
					class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
			</tr>
			<tr class="n-b" style="border: 2px solid black;">
				<td class="text-left" style="border: 2px solid black;"></td>				
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;">Sub Total</td>
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-left" style="border: 2px solid black;"></td>				
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strTotalBasicAmt%></td>
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strTotalCGST%></td>
				<td class="text-left" style="border: 2px solid black;"></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strTotalSGST%></td>				
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strSubTotalAmount%></td>
			</tr>
			<tr>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<!-- <td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td> -->
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"><input type="hidden" id="text-content3" /><span
					class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><input type="hidden" id="text-content2" /><span
					class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<!-- <td class="text-left"></td> -->
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<td class="text-left" style="border:2px solid #000;"></td>
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
				<!-- <td></td>
				<td></td> -->
				<td class="text-left" style="border:2px solid #000;"><span class="contentBold"></span></td>
			</tr>
           <% } %>
      
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
           			
           			 TransportBasicAmt=TransportBasicAmt+Double.parseDouble(splData[1]);
           			 transportCGST=transportCGST+Double.parseDouble(splData[7]);
           			 transportSGST=transportSGST+Double.parseDouble(splData[9]);
           			// transportIGST=0.0;
           			 
           			//Double percent = 0.0;
           			 
           			
          if(!ConveyanceName.equalsIgnoreCase("None")){  	%>
             <tr style="border: 2px solid black;">
                <td  class="text-left" style="border: 2px solid black;">  <span class="contentBold"></span>  </td> 
               <!--  <td  class="text-left"><span class="contentBold"></span></td>  -->
               
                <td style="border: 2px solid black;"></td>
                 <td  style="font-size: 16px;font-weight: bold;border: 2px solid black;" ><%= ConveyanceName %></td> 
                 <td  style="border: 2px solid black;" class="text-left"><input type="hidden" id="text-content4"/><span class="contentBold"></span></td> 
                  <td style="border: 2px solid black;"  class="text-left"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td  style="border: 2px solid black;" class="text-left"><span class="contentBold"></span></td> 
                <td  style="border: 2px solid black;" class="text-left"><span class="contentBold"></span></td> 
                <td  style="font-size: 16px;font-weight: bold;border: 2px solid black;" ><%= ConveyanceAmount %></td> 
	           	<td  style="border: 2px solid black;" class="text-left"></td>  	
	          	<td style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-left"><%=splData[6] %></td> 
	          	<td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-left"><%=splData[7] %><span class="contentBold"></span></td> 
	           	<td style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-left"><%=splData[8] %><span class="contentBold"></span></td> 
	           		<td style="font-size: 16px;font-weight: bold;border: 2px solid black;" ><%=splData[9]%></td> 
	          
	           	 <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=totalamount %></td>
           </tr>
           
            
            
           
        <% }   } %>
        
        	<% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
        	
        	<tr style="border: 2px solid black;">
                <td  class="text-left" style="border: 2px solid black;">  <span class="contentBold"></span>  </td> 
               <!--  <td  class="text-left"><span class="contentBold"></span></td>  -->
               
                <td style="border: 2px solid black;"></td>
                 <td  style="border: 2px solid black;" ></td> 
                 <td  style="border: 2px solid black;" class="text-left"><input type="hidden" id="text-content4"/><span class="contentBold"></span></td> 
                  <td style="border: 2px solid black;"  class="text-left"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td  style="border: 2px solid black;" class="text-left"><span class="contentBold"></span></td> 
                <td  style="border: 2px solid black;" class="text-left"><span class="contentBold"></span></td> 
                <td  style="border: 2px solid black;" ></td> 
   <!--     		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>   -->
	           	<td  style="border: 2px solid black;" class="text-left"><%-- <%=splData[6] %> --%> </td>  	
	          	<td style="border: 2px solid black;"  class="text-left"></td> 
	          	<td style="border: 2px solid black;"  class="text-left"><span class="contentBold"></span></td> 
	           	<td  style="border: 2px solid black;" class="text-left"><span class="contentBold"></span></td> 
	           		<td style="border: 2px solid black;" ></td> 
	          <%-- 	<td ><%=splData[10]%></td> 
	           	<td ><%=splData[11]%></td>  --%>
	           	 <td style="border: 2px solid black;"></td>
           </tr>
        	
        	
        	<%} %>
            
           
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
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"  class="text-right totalamt"><%=strtransportCGST%></td>
                <td style="border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strtransportSGST%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=finamount %></td>
                
                
            </tr>
            
	    	    
	                
	                <tr>
                <td style="border: 2px solid black;"></td><td style="border: 2px solid black;"></td><!-- <td></td> -->
                <td colspan="10" style="border: 2px solid black;" class="text-right totalamt">Round off</td><!-- <td></td><td></td> --><td style="border: 2px solid black;"></td><td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=roundoff %></td>
            </tr>
            
                          <tr>
                <td style="border: 2px solid black;"></td><td style="border: 2px solid black;"></td><!-- <td></td> -->
                <td colspan="10"  style="border: 2px solid black;" class="text-right totalamt">Grand Total</td><!-- <td></td><td></td --><td style="border: 2px solid black;"></td><td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=gross %></td>
            </tr>
	    	    
	      <tr>
                <td colspan="14" style="border: 2px solid black;" class="totalamt text-left" style="font-weight: bold;">Amount in words : <%=inwards %></td><!-- <td></td><td></td><td></td> -->
            </tr>
	      
         
         
     
        
       
        </tbody>
    </table>
    
    <%} else { %>
     <table class="" style="font-family: Calibri;    font-weight: bold;margin-top: 95px;width: 1154px;margin-left: 20px;border: 2px solid black;">
        <tbody>
              <tr>
                 <tr class="bckcolor" style="border: 2px solid black;">
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">S.NO </td> 
                 <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">HSN/SAC</td> 
               <!--  <td  class="text-left"> SubProductName </td>  -->
                <td rowspan="2" class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Description of Goods</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Vendor Product Desc</td> 
                
                 <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Qty/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Price/UOM</td> 
                <td  class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Basic Amount</td> 
                <td class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Disc</td> 
              <!--   <td class="text-left">Amount after Discount</td>  -->
       		 	<!--  <td colspan="2">CGST</td>
                <td colspan="2">SGST</td> -->
                <td colspan="2" style="text-align: center;    font-size: 16px;border: 2px solid black;">IGST</td>
       		 <!-- 	<td   class="text-left">Amount after tax</td>  -->
	           	<td   class="text-left" style="text-align: center;    font-size: 16px;border: 2px solid black;">Sub Total </td>  
           </tr>
             <tr style="border: 2px solid black;">
                <% 
                
              //  List<ProductDetails> getPoDetails = new ArrayList<ProductDetails>();
                
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           			
           			
           			 roundoff=splData[18];
           			 gross=splData[19];
           			 inwards=splData[20];
           			 finamount=splData[21];
					noOfRows=splData[0];
					userId=splData[30];
					 
					totalIGST=totalIGST+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[15])));
					totalIGST=Double.parseDouble(new DecimalFormat("##.##").format((totalIGST)));
					 totalBasicAmount=totalBasicAmount+Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(splData[7])));
					 totalBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format((totalBasicAmount)));
					 subTotalAmount=subTotalAmount+Double.parseDouble((splData[17]));
					 subTotalAmount=Double.parseDouble(new DecimalFormat("##.##").format((subTotalAmount)));
					 
					 strTotalBasicAmt=String.format("%.2f",totalBasicAmount);
					 strSubTotalAmount=String.format("%.2f",subTotalAmount);
					 strTotalIGST=String.format("%.2f",totalIGST);
					
					 ProductDetails productDetails = new ProductDetails();
					productDetails.setProductId(splData[25]);
					productDetails.setProductName(splData[24]);
					productDetails.setSub_ProductId(splData[26]);
					productDetails.setSub_ProductName(splData[1]);
					productDetails.setChild_ProductId(splData[27]);
					productDetails.setChild_ProductName(splData[2]);
					productDetails.setMeasurementId(splData[28]);
					productDetails.setMeasurementName(splData[4]);
					productDetails.setQuantity(splData[5]);
					productDetails.setIndentCreationDetailsId(splData[23]); 

					getPoDetails.add(productDetails);
					
				//	out.println("the list size"+getPoDetails.size());
						 /* productName=splData[24];
	           			 productId=splData[25];
	           			 subProduct=splData[1];
	           			 subProdId=splData[26];
	           			 childProd=splData[2];
	           			 childProdId=splData[27];
	           			 measureMentName=splData[4];
	           			 measurementId=splData[28];
	           			 quantity=splData[5];
	           			 indentCreationDetailsId=splData[23]; */
           	%>
	            <tr style="border: 2px solid black;">	            	
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[0] %></td>
	                 <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[3] %></td>
	              <%--   <td style="font-size: 16px;font-weight: bold;"><%= splData[1] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;width: 160px;border: 2px solid black;;"><input type="hidden" name="sendChildProductName<%= splData[0] %>" id="sendChildProductName<%= splData[0] %>" value="<c:out value='<%= splData[2]  %>'></c:out>">   <a href="javascript:void(0)"   class="anchor-description"  onclick="loadPriceMasterData('<%=splData[27]%>','<%= splData[2].replaceAll("[^A-Za-z 0-9]", "") %>','${site_idForPriceMaster}','${siteNameForPriceMaster}',<%= splData[0] %>)">   <%= splData[2] %> </a> </td>
	                <td style="font-size: 14px;font-weight: bold;    word-break: break-all;width: 250px;border: 2px solid black;"><%= splData[22] %></td>
	               
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[4] %></td>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[5] %></td>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[6] %></td>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[7] %></td>
	               	<td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[8] %>%</td>
	              <%--   <td style="font-size: 16px;font-weight: bold;"> <%= splData[10] %> </td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[11] %></td> --%>
	               <%--  <td><%= splData[11] %></td> --%>
	               <%--  <td style="font-size: 16px;font-weight: bold;"><%= splData[12] %></td>
	                <td style="font-size: 16px;font-weight: bold;"><%= splData[13] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[14] %></td>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[15] %></td>
	            <%--     <td><%= splData[16] %></td> --%>
	                <td style="font-size: 14px;font-weight: bold;border: 2px solid black;"><%= splData[17] %></td>
	        
	         <input type="hidden" name="quantity<%=j%>" id="quantity<%=j%>" value=<%=splData[5]%>>          
	         <input type="hidden" name="indentCreationdtlsId<%=j%>" id="indentCreationdtlsId<%=j%>"  value=<%=splData[23]%>>
	     <input type="hidden" name="userId" id="userId" value="<%=userId%>">
	         
	          <%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
	            </tr>
           	<% 	} %>           
	        <input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value=<%=second.length%>>    
            </tr>
           
 	<% if(!ConveyanceName.equalsIgnoreCase("None")){  %>
         	<%--  <% 	} %> --%>    
            <tr style=" border: 2px solid black;    height: 34px;">
                <td  class="text-left" style=" border: 2px solid black;">  <span class="contentBold"></span>  </td> 
               <!--  <td  class="text-left"><span class="contentBold"></span></td>  -->
                <td   class="text-left" style=" border: 2px solid black;"></td> 
                 <td   class="text-left" style=" border: 2px solid black;"><input type="hidden" id="text-content3"/><span class="contentBold"></span></td> 
                  <td   class="text-left" style=" border: 2px solid black;"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
                <td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
            <!--     <td   class="text-left"></td> 
       		 	<td   class="text-left"></td> -->
       		  	<td   class="text-left" style=" border: 2px solid black;"></td>  
	           	<!-- <td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td>  -->
	           		<td   class="text-left" style=" border: 2px solid black;"></td> 
	          	<td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
	          	 <td style="border: 2px solid black;"></td>
	          	 <td style="border: 2px solid black;"></td>
	           	<td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
           </tr>
           <tr>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;">Sub Total</td>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strTotalBasicAmt%></td>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td>
				
				<td><span class="contentBold"></span></td>
				
				<td class="text-center" style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strTotalIGST%></td>
				<td class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span><%=strSubTotalAmount%></td>
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
          			//transportSGST=transportSGST+Double.parseDouble(splData[9]);
          			// =0.0;
          			 
           			 
           			//Double percent = 0.0;
           			 
           			
           if(!ConveyanceName.equalsIgnoreCase("None")){ 	%>
             <tr style="border: 2px solid black;">
                <td  class="text-left" style=" border: 2px solid black;">  <span class="contentBold"></span>  </td> 
               <!--  <td  class="text-left"><span class="contentBold"></span></td>  -->
               
                <td style="border: 2px solid black;"></td>
                 <td   style="font-weight: bold;font-size: 14px;border: 2px solid black;"><%= ConveyanceName %></td> 
                 <td   class="text-left" style=" border: 2px solid black;"><input type="hidden" id="text-content4"/><span class="contentBold"></span></td> 
                  <td   class="text-left" style=" border: 2px solid black;"><input type="hidden" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
                <td   class="text-left" style=" border: 2px solid black;"><span class="contentBold"></span></td> 
                <td  style="font-weight: bold;font-size: 14px;border: 2px solid black;" ><%= ConveyanceAmount %></td> 
   <!--     		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>   -->
	           	<td   class="text-left" style=" border: 2px solid black;"><%-- <%=splData[6] %> --%> </td>  	
	          <%-- 	<td   class="text-left"><%=splData[6] %></td> 
	          	<td   class="text-left"><%=splData[7] %><span class="contentBold"></span></td> 
	           	<td   class="text-left"><%=splData[8] %><span class="contentBold"></span></td> 
	           		<td ><%=splData[9]%></td>  --%>
	          	<td style="font-weight: bold;font-size: 14px;border: 2px solid black;" ><%=splData[10]%></td> 
	           	<td style="font-weight: bold;font-size: 14px;border: 2px solid black;"><%=splData[11]%></td> 
	           	 <td style="font-weight: bold;font-size: 14px;border: 2px solid black;"><%=totalamount %></td>
           </tr>
        <% }  } %>

   
    <%  totalBasicAmount=totalBasicAmount+TransportBasicAmt;
           totalIGST=totalIGST+transportIGST;
           totalIGST=Double.parseDouble(new DecimalFormat("##.##").format((totalIGST)));
           
           strTransportBasicAmt=String.format("%.2f",totalBasicAmount);
           strtransportIGST=String.format("%.2f",totalIGST);
           
           %>
   
	                <tr>
                <!-- <td></td><td></td> --><!-- <td></td> -->
                <td colspan="2" style="border: 2px solid black;" class="text-right totalamt"></td>
                <td  style="border: 2px solid black;" class="text-center totalamt"> Total</td>
                <td colspan="4" style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-right"></td>
                <td  style="font-size: 16px;font-weight: bold;border: 2px solid black;" class="text-center"><%=strTransportBasicAmt%></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"></td>
                <td style="font-size: 16px;font-weight: bold;border: 2px solid black;"><%=strtransportIGST%></td>
                <td style="font-weight: bold;font-size: 16px;border: 2px solid black;"><%=finamount %></td>
                
            </tr>
            
	    	    
	                
	                <tr>
               <!--  <td></td><td></td> --><!-- <td></td> -->
                <td colspan="10" style="border: 2px solid black;" class="text-right totalamt">Round off</td><!-- <td></td><td></td> --><td style="border: 2px solid black;"></td><td style="font-weight: bold;font-size: 16px;border: 2px solid black;"><%=roundoff %></td>
            </tr>
            
                          <tr>
               <!--  <td></td><td></td> --><!-- <td></td> -->
                <td colspan="10" style="border: 2px solid black;" class="text-right totalamt">Grand Total</td><!-- <td></td><td></td --><td style="border: 2px solid black;"></td><td style="font-weight: bold;font-size: 16px;border: 2px solid black;"><%=gross %></td>
            </tr>
	    	    
	      <tr>
                <td colspan="14" style="border: 2px solid black;" class="totalamt text-left" style="font-weight: bold;border: 2px solid black;">Amount in words : <%=inwards %></td><!-- <td></td><td></td><td></td> -->
            </tr>
	      
         
         
     
        
       
        </tbody>
    </table>
     <%
				}
			
			%>
		<% List<String> listOfTermsAndConditions = (List) (request.getAttribute("listOfTermsAndConditions"));
		if(listOfTermsAndConditions.size()>0){
		%>	
    <div class="footer-content" style="font-size: 16px;font-family: Calibri;margin-left: 20px;"><span class="text-alignment-content">Terms & Conditions</span><br>
    <%
   
    int sno=0;
    
    //List<String> listOfTermsAndConditions = (List)(request.getAttribute("listOfTermsAndConditions"));
    
    for(int i=0;i<listOfTermsAndConditions.size();i++){
    	
  
         out.println(++sno+"."+listOfTermsAndConditions.get(i)+"<br>");
         
      
        }
        
    
 %>
     </div>
     <% } %>
     <% String poLevelComments=request.getAttribute("PoLevelComments") == null ? "" : request.getAttribute("PoLevelComments").toString(); 
     if(poLevelComments!=null && !poLevelComments.equalsIgnoreCase("")){
     %>
     <p><strong>Comments:</strong></p>
     <c:out value="${PoLevelComments}"></c:out>
     <%} %>
     <div class="col-md-12 notemaindiv">
		<div class="col-md-1 notecls">Note:</div>
		 <div class="col-md-3">
			<textarea rows="3" placeholder="please enter the comment" name="note" class="form-control notetextarea"></textarea>
		</div>
	</div>	
     <br>
      <div class="for-stamp"style="margin-bottom: 39px;margin-top: 20px;margin-left: 20px;"><span class="content-bold" style="font-size: 19px;font-family: Calibri;"> For Sumadhura Infracon Pvt Ltd</span></div>
  <!--   <div class="for-stamp"style="margin-bottom: 54px;margin-top: -16px;"><span class="content-bold" style="font-size: 19px;font-family: Calibri;"> For Sumadhura Infracon Pvt Ltd</span></div> -->
<%-- 	<div class="for-stamp1" style="margin-top: -18px;">
		<span class="content-bold" style="font-size: 13px; font-family: Calibri; margin-left: 367px;">
			<span class="preparedclss" style="position: absolute; margin-top: 18px; margin-left: -260px;font-family: Calibri"> Authorised by</span>
			<span style=" margin-left: -302px;font-size: 13px; font-family: Calibri;width:auto; ">	</div>
	
	<div class="for-stamp1" style="margin-top: -18px;">
		<span class="content-bold" style="font-size: 13px; font-family: Calibri; margin-left: 468px;">
			<span class="preparedclss" style="position: absolute; margin-top: 37px; margin-left: 97px;font-family: Calibri">Prepared  by</span>
			<span style=" margin-left: 29px;font-size: 13px; font-family: Calibri;width:auto; "><%= preparedName%><span style="margin-left: 23px;"><%= preparedDate%></span></span>
		</span>
	</div>
	
	<div class="for-stamp1" style="margin-top: -18px;">
		
		<span class="content-bold" style="font-size: 13px; font-family: Calibri; margin-left: 546px;">
		<span class="preparedclss" style="position: absolute; margin-top: 53px; margin-left: 380px;font-family: Calibri">Verified by</span>
		<span style=" margin-left: 372px;font-size: 13px; font-family: Calibri;width:auto; "><%=verifyName%><span style="margin-left: 23px;"><%=verifyDate%></span></span>
		
		</span>
		
		
	</div> --%>
			<div class="row" style="margin-left: 20px">
		<div class="firstRow" style="display: inline-block;width: 30%;margin-bottom: 30px;text-align: center;font-family: calibri;">
			<%-- <div><%=approverName%></div>
			<div><%=approveDate %></div> --%>
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
  <% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>
 <!-- <div class="footer-content" style="font-family: calibri; font-size: 16px;font-weight: bold;margin-top: 26px;margin-left: 20px;">*THIS IS SYSTEM GENERATED PURCHASE ORDER , SIGNATURE IS NOT REQUIRED . </div> -->
	<div class="footer-content" style="margin-left: 20px">
		<h4 style="font-family: Calibri; margin-top: 83px; font-size: 18px;">
			<span style="border-bottom: 3px solid #d86464;">SUMADHURA INFRA</span> <span style="border-bottom: 3px solid green;"></span><span  style="border-bottom: 3px solid green;margin-left: -4px;
    "> CON PRI</span><span style="border-bottom: 3px solid orange;">VATE LIMITED</span>
		</h4>
		<span style="font-family: Calibri;"> Sy No 148-2E,Nanakram
			Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</span><br> 
				<span style="font-family: Calibri;"><span style="font-weight: bold;font-family: Calibri;"></span> <span style="font-weight: bold;font-family: Calibri;margin-left: 0px;">Email</span>: info@sumadhuragroup.com,<span style="font-weight: bold;font-family: Calibri;"> URL</span>: sumadhuragroup.com,
			<span style="font-weight: bold;font-family: Calibri;margin-left: 10px;">CIN</span>: U45200KA2012PTC062071</span><br>

	</div>


     <%}else{ %>
     
          	<div class="footer-content" style="margin-left: 20px">
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
      
     
     </br>
 <!--  ************ Modal popup for Approval*************    -->
   
     <div class="modal fade" id="myModalApproval" role="dialog">
  
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content" style="border: 1px solid #443c3c;padding: 6px;border-radius: 27px;">
        <div class="modal-header" style=" background: #bdb7ab;border: 1px solid #bdb7ab;padding: 10px;border-radius: 50px;color: white">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">CC Emails</h4>
        </div>
        <form action="" method="post">
        <div class="modal-body" style="height: 231px;overflow-y: scroll;">
        <div class="container">
	<div class="row">

	</div>
</div>

        <div class="modal-footer" style="border-top: 1px solid #fdf9f9;">
        <div class="emailCC">
        	<input type="text" id="email-cc" name="ccEmailId2" value="${ccEmails}" class="form-control" style="float: left;margin-top: 28px;"/>
     		
        </div>
        

          <button type="button" id="approve_submit" class="btn btn-warning 	print-btn form-submit" style="margin-right: 175px;width: 121px;margin-top: 67px;"  onclick="Approve()">Submit</button>
       																																	<!-- saveRecords(1)-->
        </div>
      </div>
      </form>
      
    </div>
  </div>
  </div>	 
  
  <form action="" method="post">

  </form>
  
  <!-- ==========================================cancel============================================= -->



 
     
       <!--   "  --><% String responseVal=request.getAttribute("response").toString();
       
       if(responseVal.equalsIgnoreCase("true")){
     
       %>
       
       
       <% 
       String showPODetailsToVendor = request.getAttribute("showPODetailsToVendor") == null ? "" : request.getAttribute("showPODetailsToVendor").toString();
       String Email= request.getAttribute("Email") == null ? "" : request.getAttribute("Email").toString();
       if(!showPODetailsToVendor.equalsIgnoreCase("yes")) { //this is for mail reject dont appear cancel,reject,Approve
       if(!Email.equalsIgnoreCase("true")){ %>
       <div style=" background: #fff;"> 
     
      <!--  <button onclick="createModelPOPUP1()"  class="btn btn-warning print-btn" style="width: 137px;height: 35px;margin-bottom: 50px;margin-top: 12px; margin-left: 1006px;background: #b3b5f;position: absolute;">Modify Temp Po</button>		
       <button class="btn btn-warning print-btn" onclick="createModelPOPUP()"  style="width: 137px; height: 35px; margin-bottom: 50px; margin-top: 8px;margin-left: 314px;background: #b3b5f">Approve</button>
       <button  onclick="createModelPOPUPReject()" class="btn btn-warning print-btn" style="width: 137px; height: 35px; margin-bottom: 50px; margin-top: 8px;background: #b3b5f;position: absolute;margin-left: 37px;">cancel Temp PO</button>	 -->
		 
		
		</div>
		<% } }
		
       }else {
		
		%>

		<% } %>
	 </form:form>	
	 <jsp:include page="WorkOrder/ImgPdfCommonJsp.jsp" />  
	 	<form:form modelAttribute="CreatePOModelForm" id="CreateCentralIndentFormId"   class="form-horizontal" >
	 	
	 	<div id="modal-modifytemPo" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Modify Temp PO Remarks</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
       <div class="col-md-12">
        <input type="text" name="Remarks_cancel" id="Remarks_cancel" class="form-control" />
       </div>
      </div>
      <div class="modal-footer text-center">
        <!-- <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button> -->
        <button type="button" class="btn btn-warning form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="ViewOrCancel()">Submit</button>
      </div>
    </div>

  </div>
</div>
  <!-- modal popup for modify temp Po -->    
  <!-- modal popup for Cancel temp po -->
   <!-- Modal -->
<div id="modal-canceltemPo" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Cancel Temp PO Remarks</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
       <div class="col-md-12">
        <input type="text" name="Remarks" id="Remarks" class="form-control" />
       </div>
      </div>
      <div class="modal-footer text-center">
        <!-- <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button> -->
        <button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="Reject()">Submit</button>
      </div>
    </div>

  </div>
</div>
 

<!-- *******************************************************************Reject For Comment end*********************************************************************** -->
<div class="">
					<!-- <input type="button" class="btn btn-warning viewQuote" value="View Quotation " id="saveBtnId" style="width: 137px; height: 35px; margin-bottom: 50px; margin-top: -84px;margin-left: 822px;background: #b3b5f" onclick="ViewQuatation()">
					<input type="button" class="btn btn-warning viewComp" value="View Comparision" id="saveBtnId" style="width: 137px; height: 35px; margin-bottom: 50px; margin-top: -85px;margin-left: 651px;background: #b3b5f" onclick="viewComparision()"> -->
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
					<input type="hidden" name="poType" value="temporary">	<!-- this line is added for comparison  -->
					<input type="hidden" name="strPONumber" id="strPONumber" value=<%=strPONumber%>>
					<input type="hidden" name="fromdate" id="fromdate" value="${fromDate}">
					<input type="hidden" name="toDate" id="toDate" value="${toDate}">
						<input type="hidden" name="strIndentNo" id="strIndentNo" value="<%=strIndentNo%>">
					 <input type="hidden" name="password" id="password" value="${mailPasswd}"> 
					  <input type="hidden" name="userId" id="userId" value="<%=userId%>">
					  <input type="hidden" name="indentDate" id="indentDate" value="<%=strIndentCreationDate%>">
					<!-- productDetails.setProductId(splData[25]);
					productDetails.setProductName(splData[24]);
					productDetails.setSub_ProductId(splData[26]);
					productDetails.setSub_ProductName(splData[1]);
					productDetails.setChild_ProductId(splData[27]);
					productDetails.setChild_ProductName(splData[2]);
					productDetails.setMeasurementId(splData[28]);
					productDetails.setMeasurementName(splData[4]);
					productDetails.setQuantity(splData[5]);
					productDetails.setIndentCreationDetailsId(splData[23]);
					 -->
				<% 	
				
				ProductDetails productDetails = new ProductDetails();
				int noRows=(getPoDetails.size()); %>
				<input type="hidden" name="totalNoOfRecords" id="totalNoOfRecords" value="<%=noRows%>">
				
				<%
			//	out.println("the nio of rows"+noRows);
				for(int i=1;i<=noRows;i++){ 
					
					productDetails = getPoDetails.get(i-1);
				
				
				%>
				
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
				
				<input type="hidden" name="quantity<%=i-1%>" id="" value="<%=productDetails.getQuantity()%>">
				<input type="hidden" name="indentCreationdtlsId<%=i-1%>" id="" value="<%=productDetails.getIndentCreationDetailsId()%>">
				
				
				<%} %>
					
					
				</div>
<!-- 						<div style="background-color: #eaeae4;">
		 <button onclick="myFunction()" class="btn btn-warning" id="printpage"style="width: 110px; height: 32px;margin-left: 295px;background: #b3b5f;margin-bottom: 42px; margin-top:79px;">Print</button>
		</div> -->
	<%-- 	<%String updateOrNot=request.getAttribute("uploadorNot").toString();	
		if(!updateOrNot.equalsIgnoreCase("false")){%> --%>
 	<%-- <a href="http://129.154.74.18:8078/Sumadhura_UAT/downloadPdf.spring?&strPONumber=<c:out value="${poNumber}"/>&type=temp" target="_blank"> download PDF </a>	   --%>
 		
 		<!-- /*code for iframe pdf*/ -->
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
 		
 		
 		
 		<%-- <%} %> --%>
 		
 		
 		<!-- <input type="button" class="btn btn-warning viewQuote" value="View or Cancel " id="saveBtnId" style="width: 137px; height: 35px; margin-bottom: 50px; margin-top: -84px;margin-left: 1102px;background: #b3b5f" onclick="ViewOrCancel()"> -->
</form:form>
<!-- buttons alignment -->
<!-- <div class="col-md-12 text-center center-block">
 <input type="button" class="btn btn-warning viewQuote" value="View Quotation " id="saveBtnId" style="height: 35px;margin-bottom:10px;background: #b3b5f" onclick="ViewQuatation()">
 <input type="button" class="btn btn-warning viewComp" value="View Comparision" id="saveBtnId" style=" height: 35px;margin-bottom:10px; background: #b3b5f" onclick="viewComparision()">
 <button onclick="createModelPOPUP1()"  class="btn btn-warning print-btn" id="modify_temp_po" style="height: 35px; background: #b3b5f;margin-bottom:10px;">Modify Temp Po</button>		
 <button class="btn btn-warning print-btn" onclick="createModelPOPUP()"  id="approve_temp_po" style=" height: 35px; background: #b3b5f;margin-bottom:10px;">Approve</button>
 <button  onclick="createModelPOPUPReject()" class="btn btn-warning print-btn" id="cancel_temp_po" style=" height: 35px; background: #b3b5f;margin-bottom:10px;">cancel Temp PO</button>	
</div> -->
<!-- buttons alignment -->
<!-- modal popup iframe pdf -->
    <!--modal popup-->
   <!-- Modal -->
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
   <!-- modal popup iframe pdf -->
   <div class="col-md-12 text-center center-block">
 <input type="button" class="btn btn-warning viewQuote" value="View Quotation " id="saveBtnId" style="height: 35px;margin-bottom:10px;background: #b3b5f" onclick="ViewQuatation()">
 <input type="button" class="btn btn-warning viewComp" value="View Comparison" id="saveBtnId" style=" height: 35px;margin-bottom:10px; background: #b3b5f" onclick="viewComparision()">
 <!-- <button onclick="createModelPOPUP1()"  class="btn btn-warning print-btn"  data-toggle="modal" data-target="#modal-modifytemPo" style="height: 35px; background: #b3b5f;margin-bottom:10px;">Modify Temp Po</button>		
 <button class="btn btn-warning print-btn" onclick="createModelPOPUP()"  id="approve_temp_po" style=" height: 35px; background: #b3b5f;margin-bottom:10px;">Approve</button>
 <button  onclick="createModelPOPUPReject()" class="btn btn-warning print-btn" id="cancel_temp_po"  data-toggle="modal" data-target="#modal-canceltemPo"  style=" height: 35px; background: #b3b5f;margin-bottom:10px;">Cancel Temp PO</button>	 -->
</div>
   
   <div id="childProductName" class="modal fade" role="dialog">
  <!-- <div class="modal-dialog modal-lg" style="width:90% !important;">
    Modal content
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Price Master</h4>
      </div>
      <div class="modal-body"> -->
       <div class="table-responsive">
  
		<table id="tblnotification"	class="table table-bordered "  cellspacing="0" >
					<thead id="priceMasterTableHead" >
					<%-- <tr>
							<th rowspan="2" style="vertical-align:middle">Child Product</th>
		    				<th rowspan="2" style="vertical-align:middle">Measurement</th>
		    				<c:if test="${thirdMonth!=null}"><c:if test="${fn:length(thirdMonth) > 0}"><th style="text-align:center;">${thirdMonth.get(0).month_name}</th></c:if></c:if>
		    				<c:if test="${secondMonth!=null}"><c:if test="${fn:length(secondMonth) > 0}"><th style="text-align:center;">${secondMonth.get(0).month_name }</th></c:if></c:if>
		    				<c:if test="${firstMonth!=null}"><c:if test="${fn:length(firstMonth) > 0}"><th style="text-align:center;">${firstMonth.get(0).month_name}</th></c:if></c:if>
		    			</tr>
		    			 --%>
					</thead>
		<tbody id="priceMasterTableBody" >
	<%-- 		<tr>
				<c:choose>
					<c:when test="${thirdMonth!=null  && fn:length(thirdMonth) > 0 }"><td style="border: 1px solid;"> <label>${thirdMonth.get(0).child_product_name} </label></td><td style="border: 1px solid;"> <label> ${thirdMonth.get(0).measurement_name} </label></td></c:when>
					<c:when test="${secondMonth!=null  && fn:length(secondMonth) > 0 }"><td style="border: 1px solid;"> <label>${secondMonth.get(0).child_product_name} </label></td><td style="border: 1px solid;"> <label> ${secondMonth.get(0).measurement_name} </label></td></c:when>
					<c:when test="${firstMonth!=null && fn:length(firstMonth) > 0}"><td style="border: 1px solid;">  <label>${firstMonth.get(0).child_product_name} </label></td><td style="border: 1px solid;"> <label> ${firstMonth.get(0).measurement_name} </label></td> </c:when>
				</c:choose>
				<c:if test="${thirdMonth!=null}">  <c:if test="${fn:length(thirdMonth) > 0}">
					<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">
					 <table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;;margin-top: -1px;margin-bottom: -1px;">
						  <tr><th class="text-center" style="height:30px;border:1px solid #000;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;border:1px solid #000;"class="text-center"> Amount/Unit After Taxes </th><th style="height:30px;border:1px solid #000;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr> 
					    	 
						<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
						  	 	<c:choose>
						  	 	<c:when test="${incre< fn:length(thirdMonth)}">
						  	 	<tr style="height: 30px;">
						  	 	<td>${thirdMonth.get(incre).amount_per_unit_before_taxes}</td>
						  	 	<td>${thirdMonth.get(incre).amount_per_unit_after_taxes }</td>
						  	 	<td><c:choose>
												<c:when test="${not empty thirdMonth.get(incre).invoice_number}">
													<a href="getGrnDetails.spring?invoiceNumber=${thirdMonth.get(incre).invoice_number}&vendorId=${thirdMonth.get(incre).vendor_id}&invoiceDate=${thirdMonth.get(incre).indent_recive_date}&indentEntryId=${thirdMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&SiteId=${thirdMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${thirdMonth.get(incre).invoice_number}</a>
												</c:when>
												<c:when test="${not empty thirdMonth.get(incre).dc_number}">
													<a href="getDcFormGrnViewDts.spring?invoiceNumber=${thirdMonth.get(incre).dc_number}&vendorId=${thirdMonth.get(incre).vendor_id}&dcDate=${thirdMonth.get(incre).dc_recive_date}&dcEntryId=${thirdMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${thirdMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${thirdMonth.get(incre).dc_number}</a>
												</c:when>
											   </c:choose>
											</td>
												<td>${thirdMonth.get(incre).vendor_name}</td>
						  	 	</tr>
						  	 	</c:when>
						  	 	<c:otherwise>
						  	 	<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
						  	 	</c:otherwise>
						  	 	</c:choose>
						  </c:forEach>
						 	
					    </table> 
					   	  </td>
				   </c:if></c:if> <!-- First -->
				
				 <c:if test="${secondMonth!=null}"> <c:if test="${fn:length(secondMonth) > 0}">
				   <td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">
					 <table border="1" style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;margin-top: -1px;margin-bottom: -1px;" >
					
						 <tr><th scope="col" style="border:1px solid #000;height:30px;"class="text-center">Amount/unit Before Taxes</th><th scope="col" style="border:1px solid #000;height:30px;" class="text-center">Amount/Unit After Taxes</th><th style="border:1px solid #000;height:30px;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr>
		
					   	 <tbody>
					    	
					  	 	<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
						  	 	<c:choose>
						  	 		<c:when test="${incre< fn:length(secondMonth)}">
						  	 			<tr  style="height: 30px;">
						  	 				<td>${secondMonth.get(incre).amount_per_unit_before_taxes}</td>
						  	 				<td>${secondMonth.get(incre).amount_per_unit_after_taxes }</td>
						  	 				<td><c:choose>
												<c:when test="${not empty secondMonth.get(incre).invoice_number}">
													<a href="getGrnDetails.spring?invoiceNumber=${secondMonth.get(incre).invoice_number}&vendorId=${secondMonth.get(incre).vendor_id}&invoiceDate=${secondMonth.get(incre).indent_recive_date}&indentEntryId=${secondMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&SiteId=${secondMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${secondMonth.get(incre).invoice_number}</a>
												</c:when>
												<c:when test="${not empty secondMonth.get(incre).dc_number}">
													<a href="getDcFormGrnViewDts.spring?invoiceNumber=${secondMonth.get(incre).dc_number}&vendorId=${secondMonth.get(incre).vendor_id}&dcDate=${secondMonth.get(incre).dc_recive_date}&dcEntryId=${secondMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${secondMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${secondMonth.get(incre).dc_number}</a>
												</c:when>
											   </c:choose>
											</td>
						  	 					<td>${secondMonth.get(incre).vendor_name}</td>
						  	 			</tr>
						  	 		</c:when>
									<c:otherwise>
						  	 			<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
						  	 		</c:otherwise>
						  	 	</c:choose>
					  	 	</c:forEach>
					  	 
					    	</tbody>
					    
					     </table> 
					</td>
				      </c:if></c:if>
				      
				       <!--Second  -->
				  <c:if test="${firstMonth!=null}"><c:if test="${fn:length(firstMonth) > 0}">  
				  <td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid #000;">
				
					<table border="1" style="width: 100%; text-align: center;padding: 0px;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;margin-top: -1px;margin-bottom: -1px;" >
					  
					  <tr><th class="text-center" style="border:1px solid #000;height: 30px;">Amount/unit Before Taxes</th><th scope="col" style="border:1px solid #000;height: 30px;" class="text-center"> Amount/Unit After Taxes </th><th  style="height:30px;border:1px solid #000;" class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr> 
				     <tbody>
				    	
				  	 	<c:forEach var="incre" begin="0" end="${rowSpanCol-1}">
				  	 	<c:choose>
				  	 	<c:when test="${incre< fn:length(firstMonth)}">
				  	 		<tr style="height: 30px;">
				  	 		<td>${firstMonth.get(incre).amount_per_unit_before_taxes}</td>
				  	 		<td>${firstMonth.get(incre).amount_per_unit_after_taxes }</td>
				  	 		<td><c:choose>
									<c:when test="${not empty firstMonth.get(incre).invoice_number}">
										<a href="getGrnDetails.spring?invoiceNumber=${firstMonth.get(incre).invoice_number}&vendorId=${firstMonth.get(incre).vendor_id}&invoiceDate=${firstMonth.get(incre).indent_recive_date}&indentEntryId=${firstMonth.get(incre).indent_entry_id}&type=invoicePriceMaster&SiteId=${firstMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${firstMonth.get(incre).invoice_number}</a>
									</c:when>
									<c:when test="${not empty firstMonth.get(incre).dc_number}">
										<a href="getDcFormGrnViewDts.spring?invoiceNumber=${firstMonth.get(incre).dc_number}&vendorId=${firstMonth.get(incre).vendor_id}&dcDate=${firstMonth.get(incre).dc_recive_date}&dcEntryId=${firstMonth.get(incre).dc_entry_id}&type=dcGrnPriceMaster&SiteId=${firstMonth.get(incre).site_id}" style="text-decoration: underline;color: blue;">${firstMonth.get(incre).dc_number}</a>
									</c:when>
								</c:choose>
							</td>
							<td>${firstMonth.get(incre).vendor_name}</td>
				  	 		</tr>
				  	 	</c:when>
				  	 	<c:otherwise>
				  	 		<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>
				  	 	</c:otherwise>
				  	 	</c:choose>
				  	 	</c:forEach>
				  	 	
				    </tbody>
				    </table>  
				    </td> </c:if></c:if> <!-- Third  -->
				</tr> --%>

			</tbody>
		</table>
		
	<!-- main table completed -->
	<!-- 
			</div>
      </div>
      <div class="modal-footer">
        <div class="col-md-12 text-center center-block">
	        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div> -->
    </div>
  
  </div>
</div>
  <!-- modal popup for modify temp Po -->
   <!-- Modal -->
   <form id="CancelOrTemp"   class="form-horizontal" >
   <input type="hidden" name="fromdate" id="fromdate" value="${fromDate}">
<input type="hidden" name="toDate" id="toDate" value="${toDate}">
 <input type="hidden" name="password" id="password" value="${mailPasswd}"> 
 <input type="hidden" name="siteId" id="siteId" value=<%=siteId%>>
  <input type="hidden" name="userId" id="userId" value="<%=userId%>">
<!-- <div id="modal-modifytemPo" class="modal fade" role="dialog">
  <div class="modal-dialog">

    Modal content
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Modify Temp PO Remarks</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
       <div class="col-md-12">
        <input type="text" name="Remarks_cancel" id="Remarks_cancel" class="form-control" />
       </div>
      </div>
      <div class="modal-footer text-center">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="ViewOrCancel()">Submit</button>
      </div>
    </div>

  </div>
</div>
  modal popup for modify temp Po    
  modal popup for Cancel temp po
   Modal
<div id="modal-canceltemPo" class="modal fade" role="dialog">
  <div class="modal-dialog">

    Modal content
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Cancel Temp PO Remarks</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
       <div class="col-md-12">
        <input type="text" name="Remarks" id="Remarks" class="form-control" />
       </div>
      </div>
      <div class="modal-footer text-center">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="Reject()">Submit</button>
      </div>
    </div>

  </div>
</div> -->
</form>
  <!-- modal popup for cancel tem Po --> 
    
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	//this code is for temp po for price master, comparing price
	var newWindow ;
	function loadPriceMasterData(childProdId,childProdName,site_id,siteName,rowNum){
		debugger;
		var newWnidow=openNewWindow();	
		var divToPrint=document.getElementById('childProductName');
		newWnidow.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">'
			    +'<meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">'
			    +'<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/style.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/custom.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/loader.css">'
			    +'<style>.loader-ims{left:50% !important;}.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th, .newwindow-table thead tr th{background-color:#ccc;border:1px solid #000 !important;text-align:center;}.newwindow-table tbody tr td{border:1px solid #000;text-align:center;}.Mrgtop20{margin-top:20px;}.checkboxsites,.checkboxsitesall{height: 24px;width: 24px;vertical-align: bottom;}</style>'
			    +'<script src="js/jquery.min.js"><'+'/script>'
			    +'<script src="js/bootstrap.min.js"><\/script>'						
			    +'</head>'
			    +'<body>'
			    +'<div class="overlay_ims"></div>'
				+'<div class="loader-ims" id="loaderId">'
				+'<div class="lds-ims">'
				+'<div></div><div></div><div></div><div></div><div></div><div></div></div>'
				+'<div id="loadingimsMessage">Loading...</div>'
				+'</div>'
			  	+'</body>'
				+'</html>'); 
	 newWnidow.document.close(); 
	//	var site_id="${site_idForPriceMaster}";
		console.log(childProdId+" "+childProdName);
		childProdName=$("#sendChildProductName"+rowNum).val();
		var priceMasterTableHead="";
		var priceMasterTableBody="";
		$("#priceMasterTableHead").html("");
		$("#priceMasterTableBody").html("");
		priceMasterTableHead+='<tr>';
		priceMasterTableHead+='<th rowspan="2" style="vertical-align:middle">Child Product<input type="hidden" name="childProdId" id="childProdId" value="'+childProdId+'"><input type="hidden" name="childProdName" id="childProdName" value="'+childProdName+'"> </th>';
		priceMasterTableHead+='<th rowspan="2" style="vertical-align:middle">Measurement <input type="hidden" name="selectedSiteData" id="selectedSiteData" value="'+site_id+'"> </th>';
		$.ajax({
			 url : "./getProductDetailsForPO.spring",
			  type : "GET",
			  data:{
				  CHILD_PRODUCT_ID:childProdId,
				  SITE_ID:site_id,
				  NAME:childProdName
			  },
			dataType : "json",
			success : function(data) {
				let countMonthAvailbleData=0;
				let getChildProdNameLength=0;
				debugger;
				let thirdMonth="";
				let secondMonth="";
				let firstMonth="";
				
				//$("#childProductName").modal("show");	
				console.log(data);
				try{	
					//this code is for printing month names
						thirdMonth=data[0].length
						if(thirdMonth>0){
							priceMasterTableHead+='<th style="text-align:center;">'+data[0][0].month_name+'</th>';
							countMonthAvailbleData++;
						}
						secondMonth=data[1].length
						if(secondMonth>0){
							priceMasterTableHead+='<th style="text-align:center;">'+data[1][0].month_name+'</th>';
							countMonthAvailbleData++;
						}
						firstMonth=data[2].length
						if(firstMonth>0){
							priceMasterTableHead+='<th style="text-align:center;">'+data[2][0].month_name+'</th>';
							countMonthAvailbleData++;
						}
						if(countMonthAvailbleData==1){
							countMonthAvailbleData=1.2;
						}
						$("#tblnotification").css({
							"width":1100*countMonthAvailbleData
						});
						//this code is for printing child prod name and mesurment
						debugger;
						if(thirdMonth>0){
							getChildProdNameLength=data[0][0].child_product_name.length;
							childProdName=data[0][0].child_product_name;
							//$("#childProdName").html(childProdName);
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;"  id="txtchildProductName">'+data[0][0].child_product_name+'</td>';
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;">'+data[0][0].measurement_name+'</td>';
						}else if(secondMonth>0){
							getChildProdNameLength=data[1][0].child_product_name.length;
							childProdName=data[1][0].child_product_name;
							//$("#childProdName").html(childProdName);
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;"  id="txtchildProductName">'+data[1][0].child_product_name+'</td>';
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;">'+data[1][0].measurement_name+'</td>';
						}else if(firstMonth>0){
							getChildProdNameLength=data[2][0].child_product_name.length;
							childProdName=data[2][0].child_product_name;
							//$("#childProdName").html(childProdName);
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;"  id="txtchildProductName">'+data[2][0].child_product_name+'</td>';
							priceMasterTableBody+='<td style="text-align:center;border: 1px solid;">'+data[2][0].measurement_name+'</td>';
						}
						
						var forRowSpan=0;
						if (firstMonth >= secondMonth && firstMonth >= thirdMonth) {
							console.log("First number is largest. ");
							forRowSpan = firstMonth;
						} else if (secondMonth >= firstMonth && secondMonth >= thirdMonth) {
							console.log("Second number is largest.");
							forRowSpan = secondMonth;
						} else if (thirdMonth >= firstMonth && thirdMonth >= secondMonth) {
							console.log("Third number is largest.");
							forRowSpan = thirdMonth;
						} else{
							console.log("Entered numbers are not distinct.");
						}
						console.log(forRowSpan);
						
				}catch(e){
					console.log(e);
				}
				
				priceMasterTableHead+='</tr>';
				
				
				 if (thirdMonth == 0 && secondMonth == 0 && thirdMonth== 0) {
					 childProdName=data[3][0].child_product_name;
				} 
				
				
				if(thirdMonth>0){
					
					priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
					priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
					priceMasterTableBody+='<tr><th class="text-center" style="height:30px;width:200px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;width:200px;" class="text-center"> Amount/Unit After Taxes </th><th  scope="col" style="height:30px;width:200px;" class="text-center"> Invoice No/DC No </th><th  scope="col" style="height:30px;width:200px;" class="text-center">Vendor Name</th></tr>'; 
						for (var incre = 0; incre < forRowSpan; incre++) {
						debugger;
							if(incre<thirdMonth){
								priceMasterTableBody+='<tr style="height: 30px;">';
								priceMasterTableBody+='<td>'+data[0][incre].amount_per_unit_before_taxes+'</td>';
								priceMasterTableBody+='<td>'+data[0][incre].amount_per_unit_after_taxes+'</td>';
								priceMasterTableBody+='<td>';
								if(data[0][incre].invoice_number!=""&&data[0][incre].dc_number!=""){
									priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV0'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'\')">INV_'+data[0][incre].invoice_number+'</a>&nbsp/&nbsp';
									priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC0'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'\')">DC_'+data[0][incre].dc_number+'</a>';
								}else if(data[0][incre].invoice_number!=""){
									priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV0'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'\')">INV_'+data[0][incre].invoice_number+'</a>';
								}else if(data[0][incre].dc_number!=""){
									priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC0'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'\')">DC_'+data[0][incre].dc_number+'</a>';
								}
							   
								priceMasterTableBody+='</td>';
								priceMasterTableBody+='<td>'+data[0][incre].vendor_name+'</td>';
								priceMasterTableBody+='</tr>';
							}else{
								priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
							}
						}
						priceMasterTableBody+='</table>';
						priceMasterTableBody+='</td>';
					}
					
				if(secondMonth>0){
						
						priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
						priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
						priceMasterTableBody+='<tr><th class="text-center" style="height:30px;width:200px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;width:200px;"class="text-center"> Amount/Unit After Taxes </th><th scope="col" style="height:30px;width:200px;" class="text-center"> Invoice No/DC No </th><th  scope="col" style="height:30px;width:200px;" class="text-center">Vendor Name</th></tr>'; 
					
						
						for (var incre = 0; incre < forRowSpan; incre++) {
							debugger;
								if(incre<secondMonth){
									priceMasterTableBody+='<tr style="height: 30px;">';
									priceMasterTableBody+='<td>'+data[1][incre].amount_per_unit_before_taxes+'</td>';
									priceMasterTableBody+='<td>'+data[1][incre].amount_per_unit_after_taxes+'</td>';
									priceMasterTableBody+='<td>';
									if(data[1][incre].invoice_number!=""&&data[1][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV1'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'\')">INV_'+data[1][incre].invoice_number+'</a>&nbsp/&nbsp';
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC1'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'\')">DC_'+data[1][incre].dc_number+'</a>';
									}else if(data[1][incre].invoice_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV1'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'\')">INV_'+data[1][incre].invoice_number+'</a>';
									}else if(data[1][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC1'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'\')">DC_'+data[1][incre].dc_number+'</a>';
									}
								   
									priceMasterTableBody+='</td>';
									priceMasterTableBody+='<td>'+data[1][incre].vendor_name+'</td>';
									priceMasterTableBody+='</tr>';
								}else{
									priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
								}
							}
						priceMasterTableBody+='</table>';
						priceMasterTableBody+='</td>'; 
						
					}
					
					
					if(firstMonth>0){
						priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
						priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
						priceMasterTableBody+='<tr><th class="text-center" style="height:30px;width:200px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;width:200px;"class="text-center"> Amount/Unit After Taxes </th><th scope="col" style="height:30px;width:200px;" class="text-center"> Invoice No/DC No </th><th scope="col" style="height:30px;width:200px;" class="text-center">Vendor Name</th></tr>'; 
					
						for (var incre = 0; incre < forRowSpan; incre++) {
							debugger;
								if(incre<firstMonth){
									priceMasterTableBody+='<tr style="height: 30px;">';
									priceMasterTableBody+='<td>'+data[2][incre].amount_per_unit_before_taxes+'</td>';
									priceMasterTableBody+='<td>'+data[2][incre].amount_per_unit_after_taxes+'</td>';
									priceMasterTableBody+='<td>';
									if(data[2][incre].invoice_number!=""&&data[2][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV2'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'\')">INV_'+data[2][incre].invoice_number+'</a>&nbsp/&nbsp';
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC2'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'\')">DC_'+data[2][incre].dc_number+'</a>';
									}else  if(data[2][incre].invoice_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV2'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'\')">INV_'+data[2][incre].invoice_number+'</a>';
									}else if(data[2][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC2'+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'\')">DC_'+data[2][incre].dc_number+'</a>';
									}
								   
									priceMasterTableBody+='</td>';
									priceMasterTableBody+='<td>'+data[2][incre].vendor_name+'</td>';
									priceMasterTableBody+='</tr>';
								}else{
									priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;&emsp;</td><td>&emsp;&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
								}
							}
								 	
						priceMasterTableBody+='</table>';
						priceMasterTableBody+='</td>';
					}
			priceMasterTableBody+='</tr>';
			priceMasterTableBody+='</tbody>';
			priceMasterTableBody+='</table>';


			
			if (thirdMonth == 0 && secondMonth == 0 && firstMonth== 0) {
				debugger;
				$("#priceMasterTableBody").html("<p style='text-align: center;font-size: 22px;'>Selected Products were not received in Project '"+siteName+"'. <input type='hidden' name='childProdName' id='childProdName' value='"+childProdName+"'><input type='hidden' name='childProdId' id='childProdId' value='"+childProdId+"'></p>");
				$("#tblnotification").hide();
			}else{$("#tblnotification").show();
				debugger;
				$("#priceMasterTableHead").html(priceMasterTableHead);
				$("#priceMasterTableBody").html(priceMasterTableBody);
				$("#childProdName").val(childProdName);
				 if(getChildProdNameLength<=20){
						$("#txtchildProductName").css({
							"width":"250px"
						});
					}else if(getChildProdNameLength<=30){
						$("#txtchildProductName").css({
							"width":"300px"
						});
					}else if(getChildProdNameLength<=60){
						$("#txtchildProductName").css({
							"width":"350px"
						});
					}else if(getChildProdNameLength<=80){
						$("#txtchildProductName").css({
							"width":"400px"
						});
					}else{
						$("#txtchildProductName").css({
							"width":"500px"
						});
					} 
			} 
			/* $("#childProductName").modal("show");	 */
			var divToPrint=document.getElementById('childProductName');
			 newWnidow.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">'
					    +'<meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">'
					    +'<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">'
					    +'<link rel="stylesheet" type="text/css" href="css/style.css">'
					    +'<link rel="stylesheet" type="text/css" href="css/custom.css">'
					    +'<link rel="stylesheet" type="text/css" href="css/loader.css">'
					    +'<style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th, .newwindow-table thead tr th{background-color:#ccc;border:1px solid #000 !important;text-align:center;}.newwindow-table tbody tr td{border:1px solid #000;text-align:center;}.Mrgtop20{margin-top:20px;}.checkboxsites,.checkboxsitesall{height: 24px;width: 24px;vertical-align: bottom;}.loader-ims {position: fixed;left: 50% !important;top: 50% !important; z-index: 99999;}</style>'
					    +'<script src="js/jquery.min.js"><'+'/script>'
					    +'<script src="js/bootstrap.min.js"><\/script>'						
					    +'<script src="js/WorkOrder/pricemasterWindow.js"><\/script>'
					    +'</head>'
					    +'<body id="printPriceMasterData" style="margin:25px;"><div>'+divToPrint.innerHTML+'</div>'
					    +'<div class="overlay_ims" style="display:none;"></div>'
						+'<div class="loader-ims" id="loaderId" style="display:none;">'
						+'<div class="lds-ims">'
						+'<div></div><div></div><div></div><div></div><div></div><div></div></div>'
						+'<div id="loadingimsMessage">Loading...</div>'
						+'</div>'
					    +'<div class="col-md-12 text-center center-block Mrgtop20">'
					    +'<button class="btn btn-warning" id="viewReport">View Report</button>'
					    +'</div>'
						+'<div class="col-md-12 form-horizontal toggleSites" style="display:none;">'
						+'<div class="col-md-6 col-sm-6 col-lg-12 Mrgtop20 ">'
						+'<div class="form-group">'
						+'<div class="hideshowReport" style="display:none;">'
						+'<label>Select Site :</label>'
						+'<div class="col-md-12">'
						+'<div class="col-md-6"><input type="checkbox" id="checkboxsites" class="checkboxsitesall" name="allsites"/>&nbsp;Select All</div>'
						+'<div class="clearfix"></div>'
						+'<div id="siteNames">'						
						+'</div>'
						+'<div class="col-md-12 text-center center-block">'
						+'<button class="btn btn-warning  Mrgtop20" type="submit" onclick="sitesDatasubmit()">Submit</button>'
						+'<div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'</div>'
						+'<div class="clearfix"></div>'
						+'<div class="hideshowSitedata" id="appendPriceMasterData">'
						/*+'<div class="table-responsive">'
					    +'<table class="table table-bordered table-striped newwindow-table tbl">'
						+'<thead>'
						+'<tr>'
						+'<th>S.No</th>'
						+'<th>HSN/SAC</th>'
						+'<th>Description Of Goods</th>'						
						+'</tr>'
						+'</thead>'
						+'<tbody>'
						+'<tr>'
						+'<td>1</td>'
						+'<td>11100</td>'
						+'<td>Pannels</td>'
						+'</tr>'
						+'</tbody>'
						+'</table>'
						+'</div>' */
						+'</div>'
						+'<div id="isShowHideNote" style="display:none;"><p class="Mrgtop20"><strong >Note :</strong> No data available of <strong><span id="childprodName"></span></strong> for Projects <span id="nositeNote"></span></p></div>'
						+'</div>'
						+'</body>'
						+'</html>'); 
			 newWnidow.document.close();  
			}
			
			 
		});
		
	}
	
	/*hyperlink in new tab*/
	function openNewWindowinnewwindow(id) {
		 debugger;
		 var childProductNameUrl=document.getElementById(id).getAttribute("href");
		 document.getElementById(id).setAttribute("href","javascript:void(0);");
	     window.open(childProductNameUrl, '_blank', 'width=1500,height=600', 'toolbar=0,location=0,menubar=0');
	      /*  popup.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css"><style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th{background-color:#ccc;border:1px solid #000;text-align:center;}</style></head>'+
					'<body id="printPriceMasterData" style="margin:25px;">'+divToPrint.innerHTML+'</body></html>');  */
	      
		}
	
	function compareWindowfunc() {
		var popup = window.open('', 'example', 'width=800,height=400', 'toolbar=0,location=0,menubar=0');
		debugger;
		  if (popup){				  
			  var divToPrint=document.getElementById('childProductName');
			  debugger;
				/* popup.document.write('<html><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css"><style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}</style></head>'+
											'<body id="printPriceMasterData"><table class="table table-bordered"><thead><tr><th>S.No</th><th>Name</th><th>Adress</th></tr></thead><tbody><tr><td>'+id+'</td></tr></tbody></table></body></html>'); 
				 */		
				  popup.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css"><style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th{background-color:#ccc;border:1px solid #000;text-align:center;}</style></head>'+
											'<body id="printPriceMasterData" style="margin:25px;">'+divToPrint.innerHTML+'</body></html>'); 
				  debugger;
		  }			  
		}


	
	function Approve() {
		
		//var valStatus = appendRow();
			var canISubmit = window.confirm("Do you want to Submit?");
			if(canISubmit == false) {return;}
			//document.getElementById("saveBtnId").disabled = true;	
			//document.getElementById("countOfRows").value = getAllProdsCount();	
		//	document.getElementById("countOfRows").value = getAllProdsCount();
		//	document.getElementById("countOfChargesRows").value = getAllChargesCount();
			document.getElementById("ProductWiseIndentsFormId").action = "SavePoApproveDetails.spring";
			document.getElementById("ProductWiseIndentsFormId").method = "POST";
			document.getElementById("ProductWiseIndentsFormId").submit();
			$('#approve_submit').attr("disabled", true);
			$('#approve_temp_po').attr("disabled", true);
			$('#modify_temp_po').attr("disabled", true);
			$('#cancel_temp_po').attr("disabled", true);
		}

	function createModelPOPUP(){
		$('#myModalApproval').modal('show');
	}
	
	function createModelPOPUP1(){
		$(".w3-container").show();
		$(".w3-container1").hide();
	}
		function createModelPOPUPReject(){
			$(".w3-container1").show();
			$(".w3-container").hide();
		}
		
	
	/* ***** Method the show and hide the Subject************	  */	
	$('#area').hide();
	function checkval() {
	    if ($('#sub-conmatiner').is(':checked')) {
	        $('#area').show();
	    } else {
	        $('#area').hide();
	    }
	}
	$(function () {
	    checkval(); // this is launched on load
	    $('#sub-conmatiner').click(function () {
	        checkval(); // this is launched on checkbox click
	    });

	});
	function Reject() {
			//var valStatus = appendRow();
			var canISubmit = window.confirm("Do you want to Submit?");
			if(canISubmit == false) {return;}
			
			document.getElementById("CreateCentralIndentFormId").action = "RejectPoDetails.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
			$('#approve_temp_po').attr("disabled", true);
			$('#modify_temp_po').attr("disabled", true);
			$('#cancel_temp_po').attr("disabled", true);
		}
	
		function ViewQuatation() {
			document.getElementById("CreateCentralIndentFormId").action = "getQuatationDetails.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}
		function viewComparision() {
			document.getElementById("CreateCentralIndentFormId").action = "getComparisionStatement.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}
		
		function ViewOrCancel() {
			document.getElementById("CreateCentralIndentFormId").action = "cancelPO.spring";
			document.getElementById("CreateCentralIndentFormId").method = "POST";
			document.getElementById("CreateCentralIndentFormId").submit();
			$('#approve_temp_po').attr("disabled", true);
			$('#modify_temp_po').attr("disabled", true);
			$('#cancel_temp_po').attr("disabled", true);
		}
		function myFunction() {
	    	window.print();
		}
		
		  function openNewWindow() {
				 debugger;
			       return window.open('', '_blank', 'width=1500,height=600', 'toolbar=0,location=0,menubar=0');
			       /*  popup.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css"><style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th{background-color:#ccc;border:1px solid #000;text-align:center;}</style></head>'+
							'<body id="printPriceMasterData" style="margin:25px;">'+divToPrint.innerHTML+'</body></html>');  */
			       
		  	}
		  var referrer="${url}";
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
