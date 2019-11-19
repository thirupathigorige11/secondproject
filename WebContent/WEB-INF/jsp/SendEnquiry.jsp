<%@page import="java.util.Map,java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>



<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="CacheClear.jsp" />  
<%
String	strReceiverName="";
String	strReceiverAddress="";
String	strReceiverMobileNo="";
String	strReceiverGSTIN="";


%>
		
<title>Send Enquiry</title>

<style>
.border-none{
border: none;
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
}
@media print{
#printpage{
display:none;
}
@media print{
#back{
display:none;
}
.footer-content{
	margin-left:0px !important;
}
}
</style>
</head>

<body>

			<div class="loginbox" id="back">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="getIndentCreationDetails.spring">Back</a>
				</div>
			</div>
	
    <table class="table " style="font-family: Calibri;">
        <tbody>
             <tr>
                <td colspan="16"  class="text-center contentBold">Required Quote</td> 
            </tr> 
             <tr>
                <td colspan="16"  class="text-center contentBold"><span><img  class="text-img" style="float: left;margin-bottom: 44px;" src="images/logo.png" /></span>SUMADHURA INFRACON PVT LTD</td> 
            </tr>
            <tr>
                <td colspan="16"  class="text-center contentBold">Enquiry Date On: <%=new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date())%></td> 
            </tr>        
            <tr>
                <td colspan="16">
                	<!-- <img  class="text-img" style="float: left;margin-bottom: 44px;" src="images/logo.png" /> -->
                	<div class="headerTitle">
                		<h2 class="text-left text-center">Required To</h2>
                		<p class=" text-right header-content" style="margin-right: 181px;"> Indent Number: ${siteWiseIndentNo} </p>
                <p class=" text-left header-content" style="font-size: 16px;">Address</p>
             <%
             
             List<Map<String, Object>> listReceiverDtls = (List<Map<String, Object>>) request.getAttribute("listReceiverDtls");
             
             for(Map<String, Object> prods : listReceiverDtls) {
				strReceiverName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
				strReceiverAddress = prods.get("ADDRESS")==null ? "" :   prods.get("ADDRESS").toString();
				strReceiverMobileNo = prods.get("MOBILE_NUMBER")==null ? "" :   prods.get("MOBILE_NUMBER").toString();
				strReceiverGSTIN = prods.get("GSIN_NUMBER")==null ? "" :   prods.get("GSIN_NUMBER").toString();
				
             }	
             
    %>
             <p class="text-left header-content">Name:: <%=strReceiverName%></p>
                		<p class=" text-left header-content">Address: <%=strReceiverAddress %></p>
                		<p class="text-left header-content">Mobile: <%=strReceiverMobileNo %></p> 
                		<p class="text-left header-content">GSTIN: <%=strReceiverGSTIN %></p>
                		
             
             
             
             
       <!--        <p class=" text-right header-content" style="margin-right: 181px;">Indent Date:</p>
                		<p class="text-left header-content">Sy No 147/2,148/1, ECC Road </p>
                		<p class=" text-left header-content">Behind Salrpuria GR TEch park</p>
                		<p class="text-left header-content">Pattandur Agrahara Village</p> 
                		<p class="text-left header-content">K R Puram hobli, Banglore East</p>
                		<p class="text-left header-content"> Banglore- 566066</p>-->
                	</div>
                	
                </td> 
            </tr>
    

              <tr>
               
      <tr class="bckcolor">
                <td  class="text-left">S.NO </td> 
                <td  class="text-left"> HSN Code </td> 
                <td rowspan="2" class="text-left">Item Description</td> 
                 <td  class="text-left">Unit</td> 
                 <td  class="text-left">Quantity</td> 
                 <td  class="text-left">Vendor Quantity</td> 
                <td  class="text-left">MRP</td> 
                <td  class="text-left">Dis(%)</td> 
                <td  class="text-left">Dis Rate</td> 
                <td  class="text-left">SGST@%</td> 
                 <td   class="text-left">CGST@%</td> 
       		 	<td class="text-left">IGST@%</td> 
       		 	<td   class="text-left">Amount(INR) </td> 
       		 	<td  class="text-left">GST Amount</td> 
       		 	<td   class="text-left">Amount(INR)</td> 
	           	<td   class="text-left">Remarks</td>  
           </tr>
           
            
           <tr class="bckcolor">
        	
           </tr>
      <tr class="bckcolor">
        	
           </tr>
           <c:forEach items="${IndentDetails}" var="element"> 
             <tr>
                <td  class="text-left"><span class="contentBold">${element.strSerialNumber}</span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left" contenteditable="true"><span class="contentBold">${element.childProduct1}</span> </td> 
                <td   class="text-left" contenteditable="true"><span class="contentBold">${element.unitsOfMeasurement1}</span></td> 
                <td   class="text-left" contenteditable="true">${element.requiredQuantity1}</td> 
                <td   class="text-left"><input type="text" id="text-content2"  style="border: none;"/></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"></td>  	
	          	<td   class="text-left"></td> 
	           	<td  class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
	           	  	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
           </c:forEach>
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
	           	  	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         	<%--  <% 	} %> --%>    
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
	           	  	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         
             <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
	           	  	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td>
	           	  	<td   class="text-left"><span class="contentBold"></span></td>  
           </tr>
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                  <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                 <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td>
	           	  	<td   class="text-left"><span class="contentBold"></span></td>  
           </tr>
         

         
           
       
           
       
        </tbody>
    </table>
    <div class="footer-content" style=" margin-left: 200px;font-family: Calibri;">
    <p>Note: Kindly send  your terms and conditions along with HSN Code </p>
      <p style="margin-top: 18px;">From</p>
      
      <% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>

     <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: -10px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
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
      
      
   <!--  <p>Purchase Department</p>
     <p>Sumadhura Infracon Pvt Ltd, Banglore</p> -->
        </div>
        
    	<div class="col-md-12 text-center center-block">
    	<button onclick="myFunction()" id="printpage"class="btn btn-warning" style="color: #fff;background-color: #f0ad4e;border-color: #eea236;background: #b3b5f;border: 1px solid #eea236;width: 110px; height: 28px;margin-bottom:10px;margin-top: 20px;background: #b3b5f">Print</button>
    	</div>
    	<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	function myFunction() {
	    window.print();
	}
	
    </script>
    
    
</body>
</html>
