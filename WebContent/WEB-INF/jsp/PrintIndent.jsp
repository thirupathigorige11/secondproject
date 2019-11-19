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
<jsp:include page="CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		
<title>Print Indent</title>

<style>
.empty-border{
border: none;
}
.site_title{
	    margin-left: 238px;
	}
	table{
		margin:6px auto;
		border:0;
		border-spacing:0;
		font-family: Calibri;
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
		color:red;
		
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
#printpage{
    border-radius: 4px;
    color: #fff;
    background-color: #f0ad4e;
    border-color: #eea236;
    width: 110px;
    height: 28px;
    text-align: center;
    background: #b3b5f color: #fff;
    background-color: #f0ad4e;
    border-color: #eea236;
    border: none;

}
.headerTitle{
    line-height: 7px;
}
#mytdstyle{
width:10px;
}
@media print{
#printpage{
display:none;
}

@media print{
.loginbox{
display:none;
}
</style>
</head>

<body>

			<div class="loginbox">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="getIndentCreationDetails.spring">Back</a>
				</div>
			</div>
	
    <table class="table" style="border:1px solid #000;">
        <tbody>
         <c:forEach items="${IndentDtls}" var="element"> 
            <tr>
                 <td colspan="6" rowspan="3">
                	<div style="display:inline-block;float:left;border-right:2px solid #000;padding:10px;"><img src="images/LOGO_ISO.png" /></div>
                		  <div class="headerTitle">
                		<h1 class="text-center" style="color:black;vertical-align:middle;display:inline-block;margin-top:50px;font-size: 28px;">MATERIAL INDENT NOTE</h1>
                		<!-- <p class="text-right header-content">Original for Consignee</p>
                		<p class="header-content">CKB Plaza, Site no.43,2nd Floor, Varthur Main Road, </p>
                		<p class="header-content">Marathahalli, Bangalore - 560037.</p>
                		<p class="header-content">GSTIN: 29AAQCS9641A1ZZ</p> -->
                	</div>
                </td>
                <td class="text-left">Version No:</td><td style="color:red;">  ${element.versionNo}</td>
               
            </tr>
           <tr>
                <td class="text-left">Issue Date</td>
                <td style="color:red;" class="pull-right"><span>  ${element.issue_date} </span></td> 
             </tr>
             <tr>
                <td class="text-left">Reference No:</td>
                <td style="color:red;">${element.reference_No} </td> 
            </tr>
        
              <tr>
                <td colspan="6" rowspan="2"  class="text-center"><span class="header-content" style="color:black;">Name of the Project:</span> Sumadhura ${createdSiteName}<br> ${siteAddress}<br>  </td> 
                <td class="text-left" >Indent No:</td> 
                <td > ${element.siteWiseIndentNo}</td>
                
            </tr>
    
              <tr>
                <td class="text-left">Date:</td><td> <span >${element.strCreateDate}</span></td> 
                </tr>
            </c:forEach>
         <tr class="bckcolor">
                <td  class="some-text text-left" id="mytdstyle">SL.NO </td> 
                <td  class="text-left" style="width:310px;height:33px"> Name of the Material/Product with Specification </td> 
                 <td  class="text-left">UOM</td> 
                 <td  class="text-left" style="width:200px">Reqd Qty</td> 
                  <td rowspan="2" class="text-left">Required Date</td> 
                <td  class="text-left">Present Stock</td> 
                <td  class="text-left">To Order</td> 
                <td  class="text-left" style="width:230px">Remarks</td> 
    
           </tr>
           
            
           <tr class="bckcolor">
        	
           </tr>
      <tr class="bckcolor">
        	
           </tr>
           <c:forEach items="${IndentDetails}" var="element"> 
             <tr>
                <td  class="text-left"><span class="contentBold">${element.strSerialNumber}</span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold" ><c:out value='${element.childProduct1}' /></span></td> 
                
                <td   class="text-left"><span class="contentBold">${element.unitsOfMeasurement1}</span></td> 
                <td   class="text-left"><span class="contentBold">${element.requiredQuantity1}</span></td> 
                <td   class="text-left"><strong>${element.strRequiredDate}</strong></td> 
                <td   class="text-left"><span class="contentBold">${element.productAvailability}</span></td> 
                
             
                
                <td   class="text-left"><span class="contentBold">${element.orderQuantity}</span></td> 
                <td   class="text-left"><span class="contentBold"><c:out value='${element.remarks1}' /></span></td>
       		 	
           </tr>
           </c:forEach>
          <tr>
                <td  class="text-left"><span class="contentBold"></span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold"></span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	
           </tr>
         	<%--  <% 	} %> --%>    
                  <tr>
                <td  class="text-left"><span class="contentBold"></span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold"></span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	
           </tr>
         



                   <tr>
                <td  class="text-left"><span class="contentBold"></span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold"></span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	
           </tr>
                   <tr>
                <td  class="text-left"><span class="contentBold"></span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold"></span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	
           </tr>
                   <tr>
                <td  class="text-left"><span class="contentBold"></span>  </td> 
                <td  class="text-left" style="height:33px"><span class="contentBold"></span></td> 
                <td   class="text-left"> </td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"><span class="contentBold"></span></td> 
                <td   class="text-left"></td> 
       		 	
           </tr>
      </tbody>
    </table>
           
            <div  class="empty-border">
          <!--    <td class="empty-border"> </td>  -->
            					<%
            					String name="";String date="";
					    			Map<String, String> creationNames = (Map<String, String>)request.getAttribute("listCreatedName");
					    			for(Map.Entry<String, String> tax : creationNames.entrySet()) {
					    				 name=tax.getKey().toUpperCase();
					    				 date= tax.getValue();
					    			}
					    				
								%>
				<%-- 	 <div class="empty-border" style="position: absolute;    margin-top: 73px;" >
					 	<span class=""><span class="preparedclss" style="position: absolute;margin-top: 50px;margin-left: 101px;font-family: Calibri;font-weight: bold;">Preparedby</span>
					 		<span style="    margin-left: 55px;width:250px;font-family: Calibri"><%=name%><span style="margin-left: 10px;"><%= date %></span> </span>
					 	</span>
					 </div>			
				 --%>	    			
									
					    	
 
  
                      
  
            
         <%--    <form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option> --%>
            

  <!--               <td class="empty-border"> </td> 
                <td  class="empty-border"></td>  -->
               
               					<%
               					String strname="";String strdate="";
					    			Map<String, String> verfiedNames = (Map<String, String>)request.getAttribute("listOfVerifiedNames");
					    			
					    			if(verfiedNames.size()>0 ){
					    			   for(Map.Entry<String, String> tax : verfiedNames.entrySet()) {
					    				strname = tax.getKey() == null ? " " : tax.getKey().toUpperCase();
					    				strdate = tax.getValue() == null ? " " : tax.getValue();
					    			  }
					    			}
					    				
								%>
  
      <%--  <div class="empty-border" style="position: absolute;    margin-top: 73px;" ><span class="">
	       <span class="preparedclss" style="position: absolute;margin-top: 50px;margin-left: 472px;font-family: Calibri;font-weight: bold;">Verifiedby</span>
	       <span style="   margin-left: 400px;width:250px;font-family: Calibri;word-break: break-all"><%= strname%>	<span style="margin-left: 10px;word-break: break-all"><%= strdate %></span></span>
	       </span>
       </div>     
  --%>
   
                
                  				<%
                  				String approvedName="";String approvedDate="";
					    			Map<String, String> approverNames = (Map<String, String>)request.getAttribute("listOfApprovedNames");
					    			if(approverNames.size()>0 ){
					    			
					    			  for(Map.Entry<String, String> tax : approverNames.entrySet()) {
					    				
					    				  approvedName = tax.getKey() == null ? " " : tax.getKey().toUpperCase();
					    				  approvedDate = tax.getValue() == null ? " " : tax.getValue();
						    			  }
					    			}	
								%>
    	  <%-- <div class="entire-section" style="background: red;width: 1157px;margin: 63px auto;">
    			<div class="first-div" style="width: 350px;height: auto;padding: 10px;display: inline-block;float: left;margin-right: 27px;">
    			<div style="text-align: center;margin-bottom: 3px"><%=name%></div><div style="text-align: center;"><%=date%></div><div style="font-weight: bold;text-align: center;margin-top: 10px;">Preparedby</div></div>
    			<div class="second-div" style="width: 350px;height: auto;padding: 10px;display: inline-block;float: left;"><div style="text-align: center;margin-bottom: 3px"><%= strname%></div><div style="text-align: center;    height: 24px;"><%= strdate %></div><div style="font-weight: bold;text-align: center;margin-top: 10px;">Verifiedby</div></div>
    			<div class="third-div" style="width: 350px;height: auto;padding: 10px;display: inline-block;float: right;"><div style="text-align: center;margin-bottom: 3px"><%= approvedName%></div><div style="text-align: center;    height: 24px;"><%= approvedDate %></div><div style="font-weight: bold;text-align: center;margin-top: 10px;">Approvedby</div></div>
    		</div>  --%>
    		<div style="width:100%;display:inline-block;margin-top:60px;">
    		 <div style="width:30%;display:inline-block;text-align:center;"><div><span style="font-size: 15px;"><%=name%></span></div><div><span style="font-size: 15px;"><%=date%></span></div><div><strong>Prepared By</strong></div></div>
    		 <div style="width:30%;display:inline-block;text-align:center;"><div><span style="font-size: 15px;"><%= strname%></span></div><div><span style="font-size: 15px;"><%= strdate %></span></div><div><strong>Verified By</strong></div></div>
    		 <div style="width:30%;display:inline-block;text-align:center;"><div><span style="font-size: 15px;"><%= approvedName%></span></div><div><span style="font-size: 15px;"><%= approvedDate %></span></div><div><strong>Approved By</strong></div></div>
    		</div>
    		<br/>
    		<br/>
    		<div style="width:100%;">
    		 <div class="footer-content" style="font-family: calibri; font-size: 16px;font-weight: bold;margin-top: 50px;"><p>*THIS IS SYSTEM GENERATED INDENT , SIGNATURE IS NOT REQUIRED . </p></div>
    		</div>
    		
    		
    		<!-- <div style="width:100%;">
    		 <div style="width:33.3%;display:inline-block;">
    		  <p>Prepared By</p>
    		 </div>
    		 <div style="width:33.3%;display:inline-block;">
    		  <p>Verified By</p>
    		 </div>
    		 <div style="width:33.3%;display:inline-block;">
    		 <p>Approved By</p>
    		 </div>
    		</div>
    		 -->
    		<div style="text-align:center;"><button class="btn btn-warning" onclick="myFunction()" id="printpage"  style="width: 110px; height: 28px; text-align:center;background: #b3b5f color: #fff;
    background-color: #f0ad4e;
    border-color: #eea236;">Print</button>
    		</div>
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
