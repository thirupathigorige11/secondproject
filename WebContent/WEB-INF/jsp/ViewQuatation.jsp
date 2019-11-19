<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


 <%
	Map<String, String> viewGrnPageDataMap = (Map<String, String>)request.getAttribute("viewPoPageData");
 	System.out.println("viewGrnPageDataMap in top"+viewGrnPageDataMap);
	String firstTableData = request.getAttribute("AddressDetails").toString();
	System.out.println("indent number in top"+firstTableData);
	
	String strIndentNo = null;
	String strVendorName = null;
	String strVendorAddress = null;
	String strVendorGSTIN = null;
	String first[] = null;	
	String finamount=null;
    String roundoff=null;
    String gross=null;
    String indentDate="";
    String inwars="";
    String otherCharges="";
    String siteWiseIndentNo="";
    String siteName="";
           			
           			
	if(firstTableData != null) {
		first = firstTableData.split("@@");		
		strIndentNo = first[0];
		System.out.println("indent number"+strIndentNo);
		indentDate = first[1];
		strVendorName = first[2];
		strVendorAddress = first[3];
		strVendorGSTIN = first[4];
		siteWiseIndentNo = first[5];
		siteName = first[6];
		
		
		
	//	finalamount=first[11];
	
		//	strRoundOff = first[21];
	  //  strVehicleNo = first[22];
	}
	String second[] = null;
	String secondTableData = viewGrnPageDataMap.get("productDetails");
	if(secondTableData != null) {
		second = secondTableData.split("&&");
	}
	
	
	/* String third[] = null;
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
	String IGSTAMT =""; */
		
	%>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="CacheClear.jsp" />  

		
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<style>
.site_title{
	    margin-left: 238px;
	}
	table{
		border:0;
		border-spacing:0;
		border-collapse:collapse !important;
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

}
.bckcolor th{border:1px solid #000;padding:5px;}
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
}s
</style>
</head>

<body>

			<div class="loginbox">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="getIndentCreationDetails.spring"><img src="images/home.png"></a>
				</div>
			</div>
<div class="theader" style="border: 1px solid;margin-bottom: 14px;">
	<div class="sction">
		<div class="ttitle" style=" font-size: 21px;width:50%;float:left;text-align:center;padding-top:35px;">
	     <span style="margin-top:25px;">Quotation</span>
		</div>
		<div class="aside-section" style="border-right: 1px solid #000; height: 101px;width:50%;">
			
		</div>
		<div class="aside-section" style="border-left: 1px solid;">
			<img  class="text-img" style="float: right;margin-top: -123px; margin-right: 130px;height: 91px;" src="images/logo.png" />
		</div>
	</div>
</div>

<div class="header-content" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;">
<%-- <span class="text-alignment-content">Indent Number</span><span class="colon-conent">:</span><%=strIndentNo %><br> --%>
<span class="text-alignment-content">Sitewise Indent Number</span><span class="colon-conent">:</span><%=siteWiseIndentNo %><br>
<%-- <span class="text-alignment-content">Site</span><span class="colon-conent">:</span><%=siteName %><br> --%>
<span class="text-alignment-content">Indent Date</span><span class="colon-conent" style="margin-left: 82px;">:</span><%=indentDate %><br>
<!-- <span class="text-alignment-content">Date</span><span class="colon-conent" style="margin-left: 43px">:</span> -->

</div>

<div class="header-content1 addres-split" style="margin-bottom: 21px;font-size: 13px;font-family: Calibri;margin-left:2px;"> <h2>Vendor Details</h2>
<span class="content-bold"><%=strVendorName %></span><br>
<span class="content-bold"><%=strVendorAddress %></span><br>
<span class="content-bold" style="width: 211px"></span><br>
<span class="content-bold">GSTIN : <%=strVendorGSTIN %></span>
</div>
					 	
					 	<!--  Start section here for radio buttons inputs -->
					 	
						  	
						  <!-- ************* Table Search*************	 -->
	<div align="" class="formShow pricing-box" >

			 <div class="row">
	 		 	<div class="col-xs-12 bottom-class">
<%-- 		<div class="form-group col-xs-12 font-class" style="margin-bottom:10px;">
						<label  class="control-label col-sm-2" style="" >Indent Number  </label>
							<div class="col-sm-2 col-xs-12" style="margin-top: 7px;font-size: 12px;">
								:${getInvoiceDetails.strIndentId}
							</div>
							
						<label  class="control-label col-sm-2 font-class" style="" >Site Name </label>
							<div class="col-sm-2 col-xs-12" style="margin-top: 7px;font-size: 12px;">
									:${getInvoiceDetails.siteName}							
							</div>
					
						<label  class="control-label col-sm-2" style="" >Vendor name  </label>
							<div class="col-sm-2 col-xs-12" style="margin-top: 7px;font-size: 12px;">
								:${getInvoiceDetails.vendorName}
							</div>
			
				</div> --%>
<%-- 				<div class="form-group col-xs-12 font-class" style="margin-bottom:10px;">
						<label  class="control-label col-sm-2" style="" >GSTIN  </label>
							<div class="col-sm-2 col-xs-12" style="margin-top: 7px;font-size: 12px;">
								:${getInvoiceDetails.strGSTINNumber}
							</div>
							
					
						
						<label  class="control-label col-sm-2 font-class" style="" >Vendor Address </label>
							<div class="col-sm-2 col-xs-12" style="margin-top: 7px;font-size: 12px;" >
								:${getInvoiceDetails.vendorAddress}
							</div>
				</div> --%>
		</div>
	</div>
	
			<div class="clearfix"></div><br/>
			<div class="table-responsive protbldiv">
			<!-- <span class="glyphicon glyphicon-chevron-right"  id="plusbutton"></span> -->
		
			<!-- <span class="glyphicon glyphicon-plus" id="plusbutton"></span> -->
			
			 <table class="table table-bordered" style="font-family: Calibri;">
              <thead>
                 <tr class="bckcolor">
                  <th class="text-left">S.NO </th> 
                  <th class="text-left"> Product Name </th> 
                  <th rowspan="2" class="text-left">Sub product Name</th> 
                  <th class="text-left">Child Product Name</th> 
                  <th class="text-left" width=10px;>UOM</th> 
                  <th class="text-left">HSN Code</th>
                  <th class="text-left">Quantity</th> 
                  <th class="text-left" >Price</th> 
                  <th class="text-left">Basic Amount</th> 
                  <th class="text-left">Discount</th> 
                  <th class="text-left" width=20px;>Amount after Discount</th> 
       		 	  <th class="text-left">Tax</th>                
	              <th class="text-left">Tax Amount</th>
	       		  <th class="text-left">Amount after tax</th> 
		          <th class="text-left">Total Amount</th>  
           </tr>
           
            </thead>
            <tbody>
         
      <tr class="bckcolor">
      
       <% 
                
               
                
           		for(int j=0; j <= second.length-1; j++) {
           			String data = second[j];
           			String splData[] = data.split("@@");
           			inwars=splData[14];
           		  	roundoff=splData[16];
           	   		gross=splData[17];
           	   	otherCharges=splData[18];
           			finamount=splData[19];
           			
           			
           			
           	%>
      
  	
     
             <tr  style="height:35px;">
                <td  class="text-left"><%= splData[0] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[1] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[2] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"> <%= splData[3] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"> <%= splData[4] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"> <%= splData[5] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[6] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[7] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[8] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[9] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[10] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[11] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[12] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[13] %><span class="contentBold"></span>  </td> 
                <td  class="text-left"><%= splData[15] %><span class="contentBold"></span>  </td> 
         	 </tr>
         	 <%} %>
         	              <tr  style="height:35px;">
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
         	  </tr>
         	 
         	    <tr  style="height:35px;">
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
         	  </tr>
         	 
         	    <tr  style="height:35px;">
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"> None <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left">  <%=otherCharges %><span class="contentBold"></span>  </td> 
         	  </tr>
         	 


	          <tr  style="height:35px;">
                <td colspan="14" class="text-right totalamt"> Total</td> <td class="text-left"><%=finamount %></td>                
            </tr>
            
	          <tr  style="height:35px;">
                <td colspan="14" class="text-right totalamt"> Round off</td> <td class="text-left"><%=roundoff %></td>  
            </tr >
	    	          <tr  style="height:35px;">
                <td colspan="14" class="text-right totalamt"> Gross Total</td> <td class="text-left"><%= gross%></td>          
            </tr>
		    	    
	      <tr  style="height:35px;">
                <td colspan="16" class="totalamt text-left" style="font-weight: bold;">Amount in words :<%=inwars %></td>
            </tr>
	      
         
         
     
        
       
        </tbody>
    </table>
    

			<br/>
	
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
			<input type="hidden" name="VendorId" value="" id="vendorIdId">
		

                 <div id="myModal" class="modal">
  	<!-- <span class="close">&times;</span> -->
 	 <img class="modal-content" id="img01">
 	 <div id=""></div>
</div>
       <div class="footer-content" style="font-size: 13px;font-family: Calibri;margin-top: -33px;"><span style="font-size:16px;font-weight:bold;">Terms & Conditions</span><br>


  <%
   int sno=0;
    List<String> listOfTermsAndConditions = (List)(request.getAttribute("listOfTermsAndConditions"));
    
    for(int i=0;i<listOfTermsAndConditions.size();i++){
    	
  
         out.println(++sno+"."+listOfTermsAndConditions.get(i)+"<br>");
        
      
        }
        
    
    %>



<%--     <%
   int sno=0;
    
   
    
    List<String> listOfTermsAndConditions = (List<String>)(request.getAttribute("listOfTermsAndConditions"));
    
    for(int i=0;i<listOfTermsAndConditions.size();i++){
    	
  
         out.println(++sno+"."+listOfTermsAndConditions.get(i)+"<br>");
        
      
        }
        
    
    %>
		 --%>	
		</div>
		<% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>
		
		
		 <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: 68px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
     <span style="font-family: Calibri;"> Sy no 148-2E,Nanakram Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</span><br>
     <span style="font-family: Calibri;"> URL: Sumadhuragroup.org <span style="font-weight:bold;">CIN </span>: U45200KA2012PTC062071</span><br>

     </div>
     
     <%}else{ %>
      <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: 68px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
     <span style="font-family: Calibri;">43, CKB Plaza, 2nd Floor, Varthur Main Road, Marathahalli, Bengaluru, Karnataka 560037</span><br>
     <span style="font-family: Calibri;">Phone:080-421621470, E info@sumadhuragroup.org, URL: Sumadhuragroup.org, CIN:U45200KA2012PTC062071</span><br>

     </div>

     <%} %>
     
     
     
</div>	
						    
	</div>
	
</div>
			
</div>
			    	
				    	
				    
				 		</form>
				
	</div>
	</div>
	
	   <div class="col-md-12 center-block text-center">
	   	<button onclick="myFunction()" class="btn btn-warning" id="printpage" style="color: #fff;background-color: #f0ad4e;border-color: #eea236;width: 110px;height: 28px;border-radius: 4px;border: 1px solid transparent;">Print</button>
	   </div>
					<!-- /page content -->        
	
	<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="rowId">
	
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>

	<script>
	function myFunction() {
	    window.print();
	}
	
    </script>

    
    
</body>
</html>
