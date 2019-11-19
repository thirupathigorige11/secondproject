<%@page import="java.util.Map,java.text.SimpleDateFormat"%>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		
<title>Send Enquiry</title>

<style>
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
</style>
</head>

<body>

			<div class="loginbox">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="createCentralIndent.spring">Back</a>
				</div>
			</div>
	
    <table class="table table-bordered">
        <tbody>
                     
            <tr>
                <td colspan="14">
                	<img  class="text-img" style="float: left;margin-bottom: 44px;" src="images/logo.png" />
                	<div class="headerTitle">
                		<h2 class="text-center">SUMADHURA INFRACON PVT LTD</h2>
                		<!-- <p class="text-right header-content">Original for Consignee</p>
                		<p class="header-content">CKB Plaza, Site no.43,2nd Floor, Varthur Main Road, </p>
                		<p class="header-content">Marathahalli, Bangalore - 560037.</p>
                		<p class="header-content">GSTIN: 29AAQCS9641A1ZZ</p> -->
                	</div>
                	
                </td> 
            </tr>
              <tr>
                <td colspan="14"  class="text-center">Project: Soham</td> 
            </tr>
             <tr>
                <td colspan="14">INTERNAL FLATS ALL ELECTRICAL(SWICH PLATE)ESTIMATED QTY.,3 Schedule.</td> 
            </tr>
            <tr>
                <td colspan="14">Indent No: ${indentNumber}, Date: <% java.util.Date d = new java.util.Date();
                String rd = new SimpleDateFormat("dd-MMM-yy").format(d);
                out.println(rd); %></td> 
            </tr>
              <tr>
                <td colspan="14"></td> 
 <%--            </tr>
              <tr>
                <td colspan="13" class="bckcolor" >Delivery Challan</td> 
            </tr>
              <tr>
                <td colspan="6" class="text-left">Req NO:</td> 
                  <td colspan="7" class="text-left">Transport Mode: Road</td> 
            </tr>
              <tr>
                <td colspan="6" class="text-left"> Req Date :  </td> 
                 <td colspan="7" class="text-left"> Vehicle number:   </td> <td colspan="5"><%=vehileNo%></td>
            </tr>
              <tr>
                <td colspan="4" class="text-left"> State: Karnataka </td> 
                 <td colspan="1" class="text-left"> Code  </td> 
                 <td colspan="1" class="text-left"> 29  </td>
              	<td colspan="7" class="text-left"> Date of Supply:  </td> 
            </tr>
         
          <tr>
                <td colspan="4" class="text-left"> </td> 
                 <td colspan="1" class="text-left"> </td> 
                 <td colspan="1" class="text-"> </td>
              	<td colspan="7" class="text-left"> Place of Supply:  </td> 
         <!--      	<td colspan="1" class="text-left">   </td> 
              	<td colspan="5" class="text-left"></td>  -->
            </tr>
          <tr>
                <td colspan="13"></td> 
           </tr>
           <tr>
                <td colspan="6" class="bckcolor">Details of Consigner</td> 
                <td colspan="7" class="bckcolor" >Details of Consignee</td> 
           </tr>
			 <tr>
                <td colspan="6" class="text-left">Name:<span class="contentBold"> </span></td> 
                <td colspan="7" class="text-left">Name:<span class="contentBold">   </span></td> 
           </tr>
          <tr>
                <td colspan="6" class="text-left">Site Address: <span class="contentBold"> </span> </td> 
                <td colspan="7" class="text-left">Site Address:<span class="contentBold"> </span> </td> 
           </tr>
         
           <tr>
                <td colspan="6" class="text-left">GSTIN:<span class="contentBold">  </span> </td> 
                <td colspan="7" class="text-left">GSTIN: <span class="contentBold">     </span> </td> 
           </tr>
         
           <tr>
                <td colspan="2" class="text-left">State: Karnataka</td> 
                <td colspan="1" class="text-left">Code </td> 
                <td colspan="1" class="text-left">29 </td> 
                <td colspan="1" class="text-left"> </td> 
                <td colspan="1" class="text-left"> </td> 
                <td colspan="1" class="text-left">State: Karnataka </td> 
                <td colspan="1" class="text-left"></td> 
                <td colspan="3" class="text-left"> </td> 
                  <td colspan="1" class="text-left">Code </td> 
                    <td colspan="1" class="text-left"> 29</td> 
                
           </tr>
 --%>         <tr class="bckcolor">
                <td  class="text-left">S.NO </td> 
                <td  class="text-left"> Description </td> 
                <td rowspan="2" class="text-left">GST Number</td> 
                 <td  class="text-left">Mannual Disription1</td> 
                 <td  class="text-left">Mannual Disription2</td> 
                <td  class="text-left">UOM</td> 
                <td  class="text-left">Quantity</td> 
                <td  class="text-left">Price</td> 
                <td  class="text-left">Basic Amount</td> 
       		 	<td class="text-left">Tax</td> 
       		 	<td   class="text-left">HSN Code</td> 
       		 	<td  class="text-left">Tax Amount</td> 
       		 	<td   class="text-left">Amount after tax</td> 
	           	<td   class="text-left">Total Amount</td>  
           </tr>
           
            
           <tr class="bckcolor">
        	
           </tr>
      <tr class="bckcolor">
        	
           </tr>
           <c:forEach items="${IndentDetails}" var="element"> 
             <tr>
                <td  class="text-left"><span class="contentBold">${element.strSerialNumber}</span>  </td> 
                <td  class="text-left"><span class="contentBold">${element.subProduct1}_${element.childProduct1}</span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><input type="text" id="text-content1"/><span class="contentBold"></span></td> 
                <td   class="text-left"><input type="text" id="text-content1"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold">${element.unitsOfMeasurement1}</span></td> 
                <td   class="text-left"><span class="contentBold">${element.requiredQuantity1}</span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"></td>  	
	          	<td   class="text-left"></td> 
	           	<td  class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
           </c:forEach>
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         	<%--  <% 	} %> --%>    
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><input type="text" id="text-content3"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         
             <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><input type="text" id="text-content4"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><input type="text" id="text-content5"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
            <tr>
                <td  class="text-left">  <span class="contentBold"></span>  </td> 
                <td  class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
                 <td   class="text-left"><input type="text" id="text-content6"/><span class="contentBold"></span></td> 
                  <td   class="text-left"><input type="text" id="text-content2"/><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	<td   class="text-left"></td>
       		  	<td   class="text-left"></td>  
	           	<td   class="text-left"> </td>  	
	          	<td   class="text-left"></td> 
	          	<td   class="text-left"><span class="contentBold"></span></td> 
	           	<td   class="text-left"><span class="contentBold"></span></td> 
           </tr>
         

         
         
           
       
           
       
        </tbody>
    </table>
    
        <button onclick="myFunction()" class="btn btn-warning" style="width: 110px; height: 28px;margin-left: 591px;background: #b3b5f">Print</button>
    
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
	
    </script>
    
    
</body>
</html>
