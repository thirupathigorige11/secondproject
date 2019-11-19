<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%
	Map<String, String> viewGrnPageDataMap = (Map<String, String>)request.getAttribute("viewGrnPageData");
	String firstTableData = viewGrnPageDataMap.get("tblOneData");
	String versioNoData = viewGrnPageDataMap.get("tblVersionTwoData");
        String preparedBy="";
	String versionNo="";		
	String reference_No="";	
	String issue_date="";	
	if(null!=versioNoData){
		String[] data=versioNoData.split("@@");
		versionNo=data[0];
		reference_No=data[1];
		issue_date=data[2];
		preparedBy=data[3];
	}
	String strFromSitGSTIN = null;
	String reqDate = null;
	String indentEntrySeqNum = null;
	String totalAmt = null;
	String strFromSiteAddress = null;
	String strFromSiteName = null;
	String strToSiteName = null;
	String strToSiteAddress = null;
	String strToSitGSTIN = null;
	String first[] = null;	
	String receivedDate = null;
	String strTotalAmountInWords = null;
	String strRoundOff = null;
	String strtotal=null;
	String strgrandtotal=null;
	String strVehicleNo = null;
	
	String strOtherChargesAmount = null;
	String strTaxOnOtherChargesAmount=null;
	String strAmountTaxOnOtherChargesAmount=null;
	String strAmountAfterTaxOnOtherChargesAmount = null;
	String strState = null;
	
	if(firstTableData != null) {
		first = firstTableData.split("@@");		
		strFromSitGSTIN = first[0];
		reqDate = first[1];
		indentEntrySeqNum = first[2];
		strgrandtotal = first[5];
		strFromSiteAddress = first[7];
		strFromSiteName = first[11];
		strToSiteName = first[17];
		strToSiteAddress=first[18];
		strToSitGSTIN=first[19];
		strTotalAmountInWords = first[6];
		strtotal = first[20];
		strRoundOff = first[21];
	    strVehicleNo = first[22];
	    strState = first[23];
	}
	String second[] = null;
	String secondTableData = viewGrnPageDataMap.get("tblTwoData");
	if(secondTableData != null) {
		second = secondTableData.split("&&");
	}
	String OtherChargesTotalAmount  =  request.getAttribute("tblThreeData") == null ? "0.0" : request.getAttribute("tblThreeData").toString();	
	
	%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="CacheClear.jsp" />  
		
<title>Delivery Challen</title>

<style>
.site_title {
	margin-left: 238px;
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
font-weight: bold;

}
.footer-bottom{
    padding-bottom: 50px;

}
.headerTitle{
    line-height: 7px;
display: inline-block;
}

.headerTitle-1 {
	line-height: 26px;
	display: inline-block;
	margin-right: -7px;
	margin-bottom: -1px;
}

.headerTitle-1 .header-content-para {
	border-bottom: 1px solid #000;
}

.header-content-para>div {
	border-right: 1px solid #000;
}

.table-bordered {
	border: 1px solid #000;
	margin-top: -2px;
	margin-bottom: -1px;
	height: 114px;
}
.table-border-none tr td{
border:none !important;
width:33.3%;
}
.table-border-none{width:100%;}
@media print {
	.loginbox {
		display: none;
	}
	#print-btn{
	 display:none;
	}
}
</style>
</head>

<body>

	<div class="loginbox">
		<div style="border: 0;" class=" nav_title">
			<a class="site_title" href="dashboard.spring"><img
				src="images/home.png"></a>
		</div>
	</div>

	<table class="table table-bordered">
		<tbody>

			<tr>
				<td colspan="13"><img class="" style="float: left;border-right: 2px solid #000;padding: 5px;"
					src="images/LOGO_ISO.png" />
					<div class="headerTitle">
						<h2 class="text-center">SUMADHURA INFRACON PVT LTD</h2>
						<p class="header-content">CKB Plaza, Site no.43,2nd Floor,
							Varthur Main Road,</p>
						<p class="header-content">Marathahalli, Bangalore - 560037.</p>
						<p class="header-content">GSTIN: 29AAQCS9641A1ZZ</p>
					</div>
					<div class="headerTitle-1" style="float: right;">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td>Version No:</td>
									<td><%=versionNo %></td>
								</tr>
								<tr>
									<td>Issue Date:</td>
									<td><%=issue_date %></td>
								</tr>
								<tr>
									<td>Reference No:</td>
									<td><%=reference_No%></td>
								</tr>
							</tbody>
						</table>


						<!-- <div class="header-content"> 
                	<div class="header-content-para"><div style="display:inline">Version No:</div><div style="display:inline">1.1</div>
                	<div class="header-content-para"><div style="display:inline"></div><div style="display:inline"></div></div>
                	<p></p></div>
                		</div> -->
					</div></td>
			</tr>
			<tr>
				<td colspan="13"></td>
			</tr>
			<tr>
				<td colspan="13" class="bckcolor">Delivery Challan</td>
			</tr>
			<tr>
				<td colspan="6" class="text-left">DC NO: <%=indentEntrySeqNum%></td>
				<td colspan="7" class="text-left">Transport Mode: Road</td>
			</tr>
			<tr>
				<td colspan="6" class="text-left">DC Date : <%=reqDate%>
				</td>
				<td colspan="7" class="text-left">Vehicle number: <%=strVehicleNo%>
				</td>
				<%--  <td colspan="5"><%=vehileNo%></td> --%>
			</tr>
			<tr>
				<td colspan="4" class="text-left">State: Karnataka</td>
				<td colspan="1" class="text-left">Code</td>
				<td colspan="1" class="text-left">29</td>
				<td colspan="7" class="text-left">Date of Supply: <%=reqDate%>
				</td>
			</tr>

			<tr>
				<td colspan="4" class="text-left">MMR NO:</td>
				<td colspan="1" class="text-left"></td>
				<td colspan="1" class="text-"></td>
				<td colspan="7" class="text-left">Place of Supply: <%=strToSiteAddress%>
				</td>
				<!--      	<td colspan="1" class="text-left">   </td> 
              	<td colspan="5" class="text-left"></td>  -->
			</tr>
			<tr>
				<td colspan="13"></td>
			</tr>
			<tr>
				<td colspan="6" class="bckcolor">Details of Consigner</td>
				<td colspan="7" class="bckcolor">Details of Consignee</td>
			</tr>
			<tr>
				<td colspan="6" class="text-left">Name:<span
					class="contentBold"> <%=strFromSiteName%></span></td>
				<td colspan="7" class="text-left">Name:<span
					class="contentBold"> <%=strToSiteName %></span></td>
			</tr>
			<tr>
				<td colspan="6" class="text-left">Site Address: <span
					class="contentBold"> <%=strFromSiteAddress%></span>
				</td>
				<td colspan="7" class="text-left">Site Address:<span
					class="contentBold"> <%=strToSiteAddress%></span>
				</td>
			</tr>

			<tr>
				<td colspan="6" class="text-left">GSTIN:<span
					class="contentBold"> <%=strFromSitGSTIN%>
				</span>
				</td>
				<td colspan="7" class="text-left">GSTIN: <span
					class="contentBold"> <%=strToSitGSTIN%>
				</span>
				</td>
			</tr>

			<tr>
				<td colspan="2" class="text-left">State:<b><c:out value="${strFromstate}"/></b></td>
				<td colspan="1" class="text-left">Code</td>
				<td colspan="1" class="text-left"><b><c:out value="${fromStateCode}"/></b></td>
				<td colspan="1" class="text-left"></td>
				<td colspan="1" class="text-left"></td>
				<td colspan="1" class="text-left">State: <b><c:out value="${strTostate}"/></b></td>
				<td colspan="1" class="text-left"></td>
				<td colspan="3" class="text-left"></td>
				<td colspan="1" class="text-left">Code</td>
				<td colspan="1" class="text-left"><b><c:out value="${toStateCode}"/></b></td>

			</tr>
			<tr class="bckcolor">
				<td rowspan="2" class="text-left">S.NO</td>
				<td rowspan="2" class="text-left">Product Description</td>
				<td rowspan="2" class="text-left">HSN Code</td>
				<td rowspan="2" class="text-left">UOM</td>
				<td rowspan="2" class="text-left">Qty</td>
				<td rowspan="2" class="text-left">Basic Rate</td>
				<td rowspan="2" class="text-left">Total amount before Tax</td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td rowspan="" colspan="2" class="text-left">CGST</td>
				<td rowspan="" colspan="2" class="text-left">SGST</td>
				<%}else{ %>
				<td rowspan="" colspan="2" class="text-left">IGST</td>
				<td rowspan="" colspan="2" class="text-left"></td>
				<%} %>
				<td rowspan="2" colspan="2" class="text-left">Total Amount</td>


			</tr>


			<tr class="bckcolor">
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td rowspan="" class="text-left">Tax %</td>
				<td rowspan="" class="text-left">Amount</td>
				<td class="text-left">Tax %</td>
				<td rowspan="" class="text-left">Amount</td>
				<%}else{ %>
				<td rowspan="" class="text-left">Tax %</td>
				<td rowspan="" class="text-left">Amount</td>
				<td rowspan="" class="text-left"></td>
				<td rowspan="" class="text-left"></td>
				<%} %>
			</tr>
			<% 
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           	%>
			<tr>
				<td class="text-left"><span class="contentBold"><%= splData[0] %></span>
				</td>
				<td class="text-left"><span class="contentBold"><%= splData[1] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[2] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[4] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[5] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[7] %></span></td>
				<td class="text-left"><%= splData[8] %></td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td class="text-left"><span class="contentBold"><%= splData[9] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[10] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[11] %></span>
				</td>
				<td class="text-left"><span class="contentBold"><%= splData[12] %></span></td>
				<%}else{ %>
				<td class="text-left"><span class="contentBold"><%= splData[13] %></span></td>
				<td class="text-left"><span class="contentBold"><%= splData[14] %></span></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%} %>
				<td colspan="2" class="text-left"><span class="contentBold"><%= splData[15] %></span></td>
			</tr>
			<% 	} %>
			<tr>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%}else{ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%} %>
				<td colspan="2" class="text-left">--</td>
			</tr>

			<tr>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%}else{ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%} %>
				<td colspan="2" class="text-left">--</td>
			</tr>

			<tr>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%}else{ %>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%} %>
				<td colspan="2" class="text-left">--</td>
			</tr>


			<tr>
				<td class="text-left"></td>
				<td colspan="2" class="text-left"><span class="contentBold">Other
						Amount</span></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"><span class="contentBold"><%=OtherChargesTotalAmount%></span></td>
				<%if(strState.equalsIgnoreCase("Local")){ %>
				<td class="text-left"><span class="contentBold">0</span></td>
				<td class="text-left"><span class="contentBold">0</span></td>
				<td class="text-left"><span class="contentBold">0</span></td>
				<td class="text-left"><span class="contentBold">0</span></td>
				<%}else{ %>
				<td class="text-left"><span class="contentBold">0</span></td>
				<td class="text-left"><span class="contentBold">0</span></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<%} %>
				<td colspan="2" class="text-left"><%=OtherChargesTotalAmount%></td>
			</tr>

			<tr>
				<td colspan="5" class="text-centre bckcolor">Total Amount in
					words</td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td class="text-left"></td>
				<td colspan="2" class="text-left"></td>
			</tr>

			<tr>
				<td colspan="5" class="text-centre"><span class="contentBold"><%=strTotalAmountInWords %></span></td>
				<td colspan="6" class="text-left">Total</td>
				<td colspan="2" class="text-left"><span class="contentBold"><%=strtotal%></span></td>
			</tr>

			<tr>
				<td colspan="5" class="text-centre"><span class="contentBold"></span></td>
				<td colspan="6" class="text-left">Round off</td>
				<td colspan="2" class="text-left"><span class="contentBold"><%=strRoundOff%></span></td>
			</tr>

			<tr>

				<td colspan="2" class="text-left bckcolor"></td>
				<td colspan="3" rowspan="4"></td>
				<td colspan="6" class="text-left bckcolor">Grand total</td>
				<td colspan="2" class="text-left"><span class="contentBold"><%=strgrandtotal%></span></td>
			</tr>

			<tr>

				<td colspan="2" class="text-left "></td>
				<td colspan="8" rowspan="4" class="text-left">
					<div class="text-center">
						Ceritified that the particulars given above are true and correct
						<h2 class="text-center footer-bottom">For Sumadhura Infracon
							Pvt Ltd</h2>
						<!-- 	<p>Authorised Signatory</p> -->

					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="text-left"></td>
			</tr>

			<tr>
				<td colspan="2" rowspan="2" class="text-left TC">Terms &
					Conditions</td>
			</tr>
			<tr>
				<td colspan="3">Seal</td>
			</tr>

		</tbody>
	</table>
	<div class="col-md-12" style="margin-top: 25px; margin-bottom: 25px;">
		<table class="table table-border-none">
			<tbody>
				<tr>
				<!-- <td class="pull-right"><div><p>asas</p><p>asas</p><p>asasasasas</p></div><p>Prepared By</p></td>
					<td style="text-align:center;">Received  by</td>
					<td class="pull-left">Authorised Signatory</td>  -->
					 <td style="width:100%;">
				     <div style="width:33.3%;display:inline;float:left;font-size: 15px;"><p><%=preparedBy %></p><p></p><p></p></div>
					 <div style="width:33.3%;display:inline;float:left;font-size: 15px;"><p></p><p></p><p></p></div>
					 <div style="width:33.3%;display:inline;float:left;font-size: 15px;"><p></p><p></p><p></p></div>
					</td>
				    					
				</tr>
				<tr>
				<td style="width:100%;">
				     <div style="width:33.3%;display:inline;float:left;text-align:center;font-size: 15px;"><strong>Prepared By</strong></div>
					 <div style="width:33.3%;display:inline;float:left;text-align:center;font-size: 15px;"><strong>Received  by</strong></div>
					 <div style="width:33.3%;display:inline;float:left;text-align:center;font-size: 15px;"><strong>Authorised Signatory</strong></div>
					</td>
				
				</tr>
			</tbody>
		</table>
		<!-- <div style="width:450px;display:inline-block;">Prepared By</div>
    <div style="width:450px;display:inline-block;">received  by</div>
     <div style="width:450px;display:inline-block;">Authorised Signatory</div> -->

	</div>
	<center><button onclick="myFunction()" class="btn btn-warning" id="print-btn"
		style="width: 110px; height: 28px; background: #b3b5f">Print</button></center>

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
	history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    };
    </script>
    
    
</body>
</html>
