<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%
	Map<String, String> viewGrnPageDataMap = (Map<String, String>)request.getAttribute("viewGrnPageData");
	String firstTableData = viewGrnPageDataMap.get("tblOneData");	
	String gstinNumber = null;
	String sysDate = null;
	String invoiceNumber = null;
	String poNo = null;
	String dcNo = null;
	String invoiceDate = null;
	String projectName = null;
	String fnlAmnt = null;
	String curInWords = null;
	String address = "";
	String SerialNo="";
	String vendorName = "";
	String first[] = null;	
	String poDate=null;
	String eWayBillNo=null;
	String vehileNo=null;
	String transporterName=null;
	String transportationChrgs = null;
	String receivedDate = null;
	String strroundoff = null;
	String strgrandtotal = null;
	String strUserName = "";
	String strcurrentDateAndTime = "";
	if(firstTableData != null) {
		first = firstTableData.split("@@");		
		gstinNumber = first[0];
		sysDate = first[1];
		invoiceNumber = first[2];
		invoiceDate = first[3];
		projectName = first[4];
		fnlAmnt = first[5];
		curInWords = first[6];
		address = first[7];
		SerialNo=first[8];
		poNo=first[9];
		dcNo=first[10];
		vendorName=first[11];
		poDate=first[12];
		eWayBillNo=first[13];
		vehileNo=first[14]; 
		transporterName=first[15];
		transportationChrgs=first[16];
		receivedDate = first[17];
		strroundoff = first[18];
		strgrandtotal = first[19];
		strUserName = first[20];
		strcurrentDateAndTime = first[21];
		
	}
	String second[] = null;
	String secondTableData = viewGrnPageDataMap.get("tblTwoData");
	if(secondTableData != null) {
		second = secondTableData.split("&&");
	}
%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
<title>Untitled Document</title>

<style>
    .btn-back{background-color:orange;padding:3px 6px;border:1px solid orange;color:#fff;border-radius: 5px;position: absolute;margin-top: -30px;}
	table{
		margin:6px auto;
		border:0;
		border-spacing:0;
	}
	table tr td{
		border:1px solid #000;
		padding:8px;
		height:20px;
		text-align:center;
		font-weight:bold;
		min-width:36px;
		font-size:14px;
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
	.nav_title{
	    margin-left: 238px;
	}
	    @media print{
#printpage{
display:none;
}
}
@media print{
#homeimage, .btn-back{
display:none;
}

}
.anchorback{position: absolute;
    left: 183px;
    margin-top: 8px;}
#printPageButton{
    margin-right:25px;
    width: 110px; 
    background: #b3b5f;   
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;
}
</style>
</head>

<body>
	<div class="loginbox">
	                  <a class="anchorback" href="${requestScope['urlName']}">Back</a>
					<div style="border: 0;" class=" nav_title">
					          
								<a class="site_title" href="dashboard.spring"><img id="homeimage" src="images/home.png"></a>
				</div>
				
			
			</div>
    <table class="table table-bordered">
        <tbody>
                     	
            <tr>
                <td colspan="2" rowspan="3" class="verticalmiddle"><img src="images/logo.png" /></td><td colspan="10" rowspan="3" class="ptitle">Goods Received Notes</td>
                <td colspan="3" class="text-right">Version No:</td>
                <td class="fontnormal">2.0</td>
            </tr>
            <tr>
                <td colspan="3" class="text-right">Issue Date : </td>
                <td class="fontnormal">04.09.2018</td>
            </tr>
            <tr>
                <td colspan="3" class="text-right">Reference No : </td>
                <td class="fontnormal">SUMADHURA - Store - F 04</td>
            </tr>
            <tr>
                <td colspan="4" rowspan="4" class="text-left verticaltop">Supplier Name &amp; Address : </br></br><%=vendorName%> &amp; <%=address%></td>
				<td colspan="2" rowspan="5" style="border-bottom:0"></td>
                <td class="text-left width85 verticalbott pbl0">Project Name</td><td colspan="5"><%= request.getAttribute("siteName")==null?session.getAttribute("SiteName"):request.getAttribute("siteName") %></td>
                <td colspan="3" class="text-right verticalbott pbr0">E Way Bill No</td><td><%=eWayBillNo%></td>
            </tr>
            <tr>
                <td class="text-left verticalbott pbl0">GRN No</td><td colspan="5"><%=SerialNo%></td>
                <td colspan="3" class="text-right verticalbott pbr0">GRN Date</td><td><%=receivedDate%></td>
            </tr><tr>
                <td class="text-left verticalbott pbl0">PO No</td><td colspan="5"><%=poNo%></td>
                <td colspan="3" class="text-right verticalbott pbr0">PO Date</td><td><%=poDate%></td>
            </tr><tr>
                <td class="text-left verticalbott pbl0">DC No</td><td colspan="5"><%-- <%=dcNo%> --%></td>
                <td colspan="3" class="text-right verticalbott pbr0">DC Date</td><td></td>
            </tr>
            <tr>
                <td colspan="3" class="text-left">GSTIN No</td><td><%=gstinNumber%></td>
                <td class="text-left verticalbott pbl0">Invoice No</td><td colspan="5"><%=invoiceNumber%></td>
                <td colspan="3" class="text-right verticalbott pbr0">Invoice Date</td><td><%=invoiceDate%></td>
            </tr><tr>
                <td colspan="6" rowspan="2" style="border-top:0;border-left:0;"></td>
           
                <td class="text-left verticalbott pbl0">Vehicle No</td><td colspan="5"><%=vehileNo%></td>
                <td colspan="3" class="text-right verticalbott pbr0">Transporter Name</td><td><%=transporterName%></td>
            </tr>
           
            <tr>
                <td colspan="11"></td>
            </tr><tr class="trheadings">
                <td>Sl NO</td>
                
               <!--  <td class="width20">PRODUCT NAME</td> -->
               <!--  <td class="width60">Sub Name</td>
                <td class="width40">Child Name</td> -->
                <td class="width60">HSN CODE</td>
                <td class="width20">DESCRIPTION</td>
                <td>UNIT</td>
                <td class="width60">ORDER QTY</td>
                <td>RECEIVED QTY</td>
                <td>RATE</td>
                <td>BASIC AMOUNT</td>
                <td colspan="2">CGST</td>
                <td colspan="2">SGST</td>
                <td colspan="2">IGST</td>
                <td class="width75">TOTAL AMOUNT AFTER TAX</td>
                <td class="width160">REMARKS</td>
               
                
            </tr>
<!--             <tr>
                <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
                <td>% </td><td>Amt</td><td></td><td></td>
                <td>%</td><td>Amt</td><td></td><td></td>
            </tr> -->
            <% 
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           	%>
	            <tr class="n-b">	            	
	                <td><%= splData[0] %></td>
	                <%-- td><%= splData[1] %></td> --%>
	                <td><%= splData[2] %></td>
	                <td><%= splData[1] %></td>
	                <td><%= splData[4] %></td>
	                <td><%= splData[5] %></td>
	                <td><%= splData[6] %></td>
	                <td><%= splData[7] %></td>
	               	<td><%= splData[8] %></td>
	                <td><%= splData[9] %></td>
	                <td><%= splData[10] %></td>
	                <td><%= splData[11] %></td>
	                <td><%= splData[12] %></td>
	                <td><%= splData[13] %></td>
	                <td><%= splData[14] %></td>
	                <td><%= splData[15] %></td>
	                 <td><%= splData[16] %></td>
	                
	          <%--        <td><%= splData[17] %></td>
	                 <td><%= splData[18] %></td> --%>
	            </tr>
           	<% 	} %>           
	    
	                <tr>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	                 <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	                </tr>
	                <tr>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	   				<td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	                </tr>
	                	                <tr>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	   				<td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	                </tr>
	                
	              <% 
	              String chargeArray[] = null;
	          	String getChargeDts = viewGrnPageDataMap.get("NamesOfCharges");
	          	if(getChargeDts != null) {
	          		chargeArray = getChargeDts.split("&&");
	          	}
	           
           		for(int k=0; k <= chargeArray.length-1; k++) {
           			String data1 = chargeArray[k];
           			String splData1[] = data1.split("@@");
           
           		%>

           			<tr>
 	               <td></td>
 	               <td></td>
 	               <td><%= splData1[0] %></td>
 	               <td></td>
 	               <td></td>
 	               <td></td>
 	               <td></td>
 	               <td><%= splData1[1] %></td>
 	               <td><%= splData1[2] %></td>
 	   				<td><%= splData1[3] %></td>
 	               <td><%= splData1[4] %></td>
 	               <td><%= splData1[5] %></td>
 	               <td><%= splData1[6] %></td>
 	               <td><%= splData1[7] %></td>
 	               <td><%= splData1[8] %></td>
 	               <td></td>
 	                </tr>
           		<%} %>
         
           	
           	
           	
	                	                	            <%--     <tr>
	               <td></td>
	               <td></td>
	               <td>Transportation Charges</td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	   				<td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td><%=transportationChrgs%></td>
	               <td></td>
	                </tr>
	                	                	                	                <tr>
	               <td></td>
	               <td></td>
	               <td>Other Charges</td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	   				<td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	               <td></td>
	                </tr> --%>
	                
	                <tr>
                <td></td><td></td>
                <td colspan="12" class="text-right totalamt"> Total</td><td><%= fnlAmnt %></td><td></td>
                
            </tr>
            
	    	    
	                
	                <tr>
                <td></td><td></td>
                <td colspan="12" class="text-right totalamt">Round off</td><td><%=strroundoff%></td><td></td>
            </tr>
            
            
            
            
            
            
            
	    	               <tr>
                <td></td><td></td>
                <td colspan="12" class="text-right totalamt">Gross Total</td><td><%=strgrandtotal%></td><td></td>
            </tr>
	    	    
	      <tr>
                <td colspan="16" class="totalamt text-left">Amount In Words : <%=curInWords %></td>
            </tr>
	      <tr rowspan="4">
	      
	              
	             
               <td colspan="6" class="br0 ht150 verticalbott text-center pb0">Prepared By :  <%=strUserName%>  <%=strcurrentDateAndTime%> </td> <td colspan="6" class="br0 bl0 pb0 verticalbott text-center">Checked By</td> <td colspan="4" class="bl0 pb0 text-center verticalbott">Approved By</td> 
            </tr>
        </tbody>
    </table>
    
    <div style="text-align:center;"><button type="button" onclick="myFunction()" id="printPageButton" class="btn btn-warning " style="width: 110px; height: 28px; text-align:center;background: #b3b5f color: #fff;
    background-color: #f0ad4e;
    border-color: #eea236;">Print</button></div>

    		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	history.pushState(null, null, location.href);
	window.onpopstate = function () {
	history.go(1);
	};
	function myFunction() {
	    window.print();
	}
		
	var isShoworHide =window.location.href.split("&showHomebtn=")[1];
	debugger;
	if(isShoworHide == "false"){
	
		$('.loginbox').hide();
	}
	</script>
</body>
</html>
