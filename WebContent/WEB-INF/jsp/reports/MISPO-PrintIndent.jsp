<%@page import="java.util.Map,java.text.SimpleDateFormat"%>
<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<%--   <%
	Map<String, String> viewGrnPageDataMap = (Map<String, String>)request.getAttribute("viewGrnPageData");
	String firstTableData = viewGrnPageDataMap.get("tblOneData");	
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
	}
	String second[] = null;
	String secondTableData = viewGrnPageDataMap.get("tblTwoData");
	if(secondTableData != null) {
		second = secondTableData.split("&&");
	}

	%>   --%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		
<title>Print MIS Po Details</title>

<style>
@media print{
 #printbutton{display:none;}
 table { page-break-inside:auto }
    tr    { page-break-inside:avoid; page-break-after:auto }
    thead { display:table-header-group }
    tfoot { display:table-footer-group }
}
</style>
</head>

<body>
<div align=center>
	<table style="width:100%;border-collapse:collapse;border:1px solid #000;">
	 <tbody>
	   <tr>
	     <td style="width:15;border:2px solid #000;"colspan="2"><div style="text-align:center;"><img src="images/LOGO_ISO.png"/></div></td>
	     <td style="width:60%;border:2px solid #000;"colspan="13"><div style="text-align:center;font-size:25px;">PURCHASE DEPARTMENT MIS REPORT</div></td>
	     <td style="width:25;border:2px solid #000;"colspan="2"><div style="text-align:center;">Date:${currentDate}</div></td>
	   </tr>
	   <tr>
	     <td colspan="9" style="border:2px solid #000;text-align:left;border-right: none;"><div style="text-align:left;font-size:20px;float:left;"><strong>Report Type :</strong></div><div style="float:left;margin-top: 5px;">Purchase Department PO Purchase</div></td>
	     <td colspan="9" style="border:2px solid #000;padding:4px;text-align:right;border-left: none;"><span style="font-size:20px;"><strong>Report From:</strong></span> ${fromDate}</td>
	   </tr>
	   <tr>
	     <td colspan="18" style="border:2px solid #000;padding:4px;text-align:right;"><span style="font-size:20px;"><strong>To:</strong></span>${toDate}</td>
	   </tr>
	   <tr>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>S.No</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Project Name</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Indent Number</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Indent Date</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>No.of Quotations</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>PO Number</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>PO Date</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Indent To PO Duration Days</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Vendor Name</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>PO Amount</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Expected Delivery Date</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Description of Materials</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Invoice Number</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Invoice Date</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Invoice Amount</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>GRN Date</strong></td>
	     <td style="border:2px solid #000;padding:4px;text-align:center;"><strong>Delivery Status in Days</strong></td>
	     
	    
      </tr>
      <% int i=1; %>
       <c:forEach items="${poDetails}" var="element">
     <tr>
    
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"><%=i%></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.siteName}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.indentNo}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.indentDate}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.quatations}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.strPONumber}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.poDate}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.purchase_po_Req_Date}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.vendorName}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.poTotal}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.deliveryDate}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.materialDesc}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.invoiceNumber}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.receiveDate}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.invoiceAmount}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.grnDate}</td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;">${element.deliveryStatus}</td>
     <%i++; %>
     </tr>
     </c:forEach>
   <!--  <tr>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
     </tr> -->
  <!-- <tr>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
     </tr>  -->
    <!--  <tr>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
     <tr> -->
   <!--  <tr>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
	     <td style="border:2px solid #000;padding:4px;word-break:break-word;text-align:center;"></td>
     <tr>  -->

	 </tbody>
	</table>
	</div>
	<div class="col-md-12 Mrgtop10" style="width:100%;margin-top:20px;">
					    <div class="col-md-8" style="width:65%;float:left;"></div>
					    <div style="text-align:left;width:35%;float:right;">
					    <div class="col-md-7 text-left" style="width:40%;float:left;"><strong>PO AMOUNT</strong></div><div class="col-md-1 text-left" style="width:10%;float:left;"><strong>:</strong></div><div class="col-md-5 text-left"style="width:50%;float:left;text-align:left;">${Po_GrandTotal}</div>
					    <div class="col-md-7 text-left" style="width:40%;float:left;"><strong>INVOICE AMOUNT</strong></div><div class="col-md-7 text-left" style="width:10%;float:left;"><strong>:</strong></div><div class="col-md-5 text-left" style="width:50%;float:left;text-align:left;">${Invoice_GrandTotal}</div>
					    </div>
					</div>
	<div style="width:100%;margin-top:120px;padding-bottom:15px;">
	 <div style="width:33.3%;float:left;text-align:center;"><strong>Authorized By</strong></div>
	 <div style="width:33.3%;float:left;text-align:center;"><strong>Prepared By</strong></div>
	 <div style="width:33.3%;float:left;text-align:center;"><strong>Verified By</strong></div>
	</div>
	<div style="width:100%;margin-top:25px;text-align:center;" id="printbutton">
	 <input type="button" style="background-color:#ffa500;border:1px solid #ffa500;border-radius:5px;width:150px;color:#fff;padding:8px 12px;" Value="Print" onclick="window.print();"/>
	</div>
	
</body>
</html>
