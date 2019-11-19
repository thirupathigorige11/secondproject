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

<body onLoad="JavaScript:checkRefresh();" onUnload="JavaScript:prepareForRefresh();">
<form onSubmit="JavaScript:disableRefreshDetection()">
<input type="hidden" id="refreshed" value="no">
	

<c:choose>
	<c:when test="${not empty showGrid}">
				
			<div class="loginbox">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="ContractorPaymentApprovalForAccDept.spring">Back</a>
				</div>
			</div>
	
    <table class="table">
        <tbody>
                     
            <tr>
                 <td colspan="15" >
                	<img  class="text-img" style="float: left;margin-bottom: 44px;" src="images/LOGO_ISO.png" />
                	<div class="headerTitle">
                		<h2 class="text-center" style="color:black;margin-top: 69px;">PAYMENT REQUEST DETAILS</h2>
                		<!-- <p class="text-right header-content">Original for Consignee</p>
                		<p class="header-content">CKB Plaza, Site no.43,2nd Floor, Varthur Main Road, </p>
                		<p class="header-content">Marathahalli, Bangalore - 560037.</p>
                		<p class="header-content">GSTIN: 29AAQCS9641A1ZZ</p> -->
                	</div>
                
                </td>
              
                
               
            </tr>
          

            <c:forEach items="" var="element"> 
<%--               <tr>
                <td colspan="6" rowspan="2"  class="text-center"><span class="header-content" style="color:black;">Name of the Project:</span> Sumadhura ${createdSiteName}<br> ${siteAddress}<br>  </td> 
                <td class="text-left" >Indent No:</td> 
                <td  ></td>
            </tr> --%>
    
              <tr>
                 </tr>
                </c:forEach>
         <tr class="bckcolor">
                <td  class="some-text text-left" id="mytdstyle">Payment Initiated Date </td> 
                <td  class="text-left" style="width:310px;height:33px"> Site</td> 
                <td class="text-left">Contractor Name</td> 
                 <td  class="text-left">WorkOrder Date</td> 
                 <td  class="text-left" style="width:200px">WorkOrder Number</td> 
                <td  class="text-left" style="width:230px">WorkOrder Amt</td> 
                <td  class="text-left" style="width:230px">Payment Type</td> 
    			  <td  class="text-left">Bill Number</td> 
                <td  class="text-left" style="width:230px">Bill Amount</td> 
    			  <td  class="text-left">Bill Date</td> 
                <td  class="text-left" style="width:230px">Paid Amount</td> 
    			  <td  class="text-left" style="width:230px">Balance Amount</td> 
    			  <td  class="text-left" style="width:230px">Requested Amount</td> 
    			   <td  class="text-left" style="width:230px">Note</td> 
    			     <td  class="text-left" style="width:230px">Status</td> 
           </tr>
           
            <c:forEach items="${listofPendingPayments}" var="element">  
						<tr>
                <td  class="some-text text-left" id="mytdstyle">${element.strPaymentRequestReceivedDate}</td> 
                <td  class="text-left" style="width:310px;height:33px">${element.strSiteName} </td> 
                <td class="text-left">${element.strContractorName} </td> 
                 <td  class="text-left">${element.strWorkOrderDate} </td> 
                 <td  class="text-left" style="width:200px">${element.workOrderNo} </td> 
                <td  class="text-left" style="width:230px">${element.doubleWorkOrderAmount} </td> 
    			<td  class="text-left" style="width:230px">
    					<c:if test="${element.paymentType=='RA'}">RA BILL</c:if>
						<c:if test="${element.paymentType=='ADV'}">ADVANCE</c:if>
						<c:if test="${element.paymentType=='SEC'}">SECURITY DEPOSIT</c:if>
						<c:if test="${element.paymentType=='NMR'}">NMR</c:if>
				</td> 
    			  <td  class="text-left">${element.strBillId} </td> 
                <td  class="text-left" style="width:230px">${element.doubleBillAmount} </td> 
    			 <td  class="text-left">${element.strBillDate} </td> 
               <td  class="text-left" style="width:230px">${element.doubleAmountToBeReleased}</td> 
    			  <td  class="text-left" style="width:230px">${element.doubleBalanceAmount} </td> 
    			  <td  class="text-left" style="width:230px">${element.doubleAmountToBeReleased} </td> 
    			    <td  class="text-left" style="width:230px">${element.strRemarks} </td> 
    			     <td  class="text-left" style="width:230px">${element.status}</td> 
           </tr> 
           </c:forEach>

            <tr >
                <td  class="some-text text-left" id="mytdstyle"></td> 
                <td  class="text-left" style="width:310px;height:33px"> </td> 
                <td class="text-left"> </td> 
                 <td  class="text-left"> </td> 
                 <td  class="text-left" style="width:200px"> </td> 
                <td  class="text-left"> </td> 
                <td  class="text-left"> </td> 
                <td  class="text-left" style="width:230px"> </td> 
    			 <td  class="text-left" style="width:230px"></td> 
    			  <td  class="text-left" style="width:230px"> </td> 
    			  <td  class="text-left" style="width:230px"> </td> 
    			  <td  class="text-left" style="width:230px"> </td> 
    			   <td  class="text-left" style="width:230px"></td> 
    			     <td  class="text-left" style="width:230px"></td> 
    			     <td  class="text-left" style="width:230px"></td> 
    			 
           </tr> 
         



      </tbody>
    </table>
           
            <div  class="empty-border">
          <!--    <td class="empty-border"> </td>  -->
        <%--     					<%
            					String name="";String date="";
					    			Map<String, String> creationNames = (Map<String, String>)request.getAttribute("listCreatedName");
					    			for(Map.Entry<String, String> tax : creationNames.entrySet()) {
					    				 name=tax.getKey().toUpperCase();
					    				 date= tax.getValue();
					    			}
					    				
								%> --%>
				<%-- 	 <div class="empty-border" style="position: absolute;    margin-top: 73px;" >
					 	<span class=""><span class="preparedclss" style="position: absolute;margin-top: 50px;margin-left: 101px;font-family: Calibri;font-weight: bold;">Preparedby</span>
					 		<span style="    margin-left: 55px;width:250px;font-family: Calibri"><%=name%><span style="margin-left: 10px;"><%= date %></span> </span>
					 	</span>
					 </div>			
				 --%>	    			
									
					    	
 
  
                      
  
            
         <%--    <form:option value="<%= taxIdAndPercentage %>"><%= tax.getValue() %></form:option> --%>
            

  <!--               <td class="empty-border"> </td> 
                <td  class="empty-border"></td>  -->
               
<%--                					<%
               					String strname="";String strdate="";
					    			Map<String, String> verfiedNames = (Map<String, String>)request.getAttribute("listOfVerifiedNames");
					    			
					    			if(verfiedNames.size()>0 ){
					    			   for(Map.Entry<String, String> tax : verfiedNames.entrySet()) {
					    				strname = tax.getKey() == null ? " " : tax.getKey().toUpperCase();
					    				strdate = tax.getValue() == null ? " " : tax.getValue();
					    			  }
					    			}
					    				
								%> --%>
  
      <%--  <div class="empty-border" style="position: absolute;    margin-top: 73px;" ><span class="">
	       <span class="preparedclss" style="position: absolute;margin-top: 50px;margin-left: 472px;font-family: Calibri;font-weight: bold;">Verifiedby</span>
	       <span style="   margin-left: 400px;width:250px;font-family: Calibri;word-break: break-all"><%= strname%>	<span style="margin-left: 10px;word-break: break-all"><%= strdate %></span></span>
	       </span>
       </div>     
  --%>
   
<%--                 
                  				<%
                  				String approvedName="";String approvedDate="";
					    			Map<String, String> approverNames = (Map<String, String>)request.getAttribute("listOfApprovedNames");
					    			if(approverNames.size()>0 ){
					    			
					    			  for(Map.Entry<String, String> tax : approverNames.entrySet()) {
					    				
					    				  approvedName = tax.getKey() == null ? " " : tax.getKey().toUpperCase();
					    				  approvedDate = tax.getValue() == null ? " " : tax.getValue();
						    			  }
					    			}	
								%> --%>

<div id="generateFileDiv">
<form:form action="generatePaymentTextFile.spring" name="generatefile" id="generatefile" method="get">
<input type="hidden" name="generateFileifTrue" value="true">
</form:form>
</div>
    		<button onclick="myFunction()" id="printpage" class="btn btn-warning" style="width: 110px; height: 28px; margin-left: 800px;margin-top: 55px;background: #b3b5f">Print</button>
    		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	/* code for Backbutton */
  	history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    };
    /* code for Backbutton */
	function myFunction() {
	    window.print();
	}
	$(document).ready(function(){
		//code for site id
		var generateFile="${generateFile}";
		if(generateFile=="true"){
			window.generatefile.submit();
			
			$("#generateFileDiv").remove();
		//$("#generatefile").attr('action', 'ViewThreeMonthProductPrice.spring');
			/* if("${refreshOneTime}"=="true"){
				window.location.reload();	
			} */
		}
	});
	
	/* disable refresh (F5, Ctr+F5 and Ctrl+R) */
    function disableF5(e) { if ((e.which || e.keyCode) == 116 || (e.which || e.keyCode) == 82) e.preventDefault(); };
	$(document).ready(function(){
    $(document).on("keydown", disableF5);
    });
    /* disable refresh (F5, Ctr+F5 and Ctrl+R) */
    
	
	/************************* Code for Detecting Page Refreshes - START *****************************/
	//source: http://www.tedpavlic.com/post_detect_refresh_with_javascript.php
					
function checkRefresh()
{ 	
preventFormResubmitOnPressBackButton(); 

// Get the time now and convert to UTC seconds
var today = new Date();
var now = today.getUTCSeconds();

// Get the cookie
var cookie = document.cookie;
var cookieArray = cookie.split('; ');

// Parse the cookies: get the stored time
for(var loop=0; loop < cookieArray.length; loop++)
{
	var nameValue = cookieArray[loop].split('=');
	// Get the cookie time stamp
	if( nameValue[0].toString() == 'SHTS' )
	{
		var cookieTime = parseInt( nameValue[1] );
	}
	// Get the cookie page
	else if( nameValue[0].toString() == 'SHTSP' )
	{
		var cookieName = nameValue[1];
	}
}

if( cookieName &&
	cookieTime &&
	cookieName == escape(location.href) &&
	Math.abs(now - cookieTime) < 5 )
{
	// Refresh detected

	// Insert code here representing what to do on
	// a refresh
	
	document.getElementById("refresh_msg").innerHTML="Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.";
	document.getElementById("msg1").innerHTML="";
	
	// If you would like to toggle so this refresh code
	// is executed on every OTHER refresh, then 
	// uncomment the following line
	// refresh_prepare = 0; 
}	

// You may want to add code in an else here special 
// for fresh page loads
}

function prepareForRefresh()
{
if( refresh_prepare > 0 )
{
	// Turn refresh detection on so that if this
	// page gets quickly loaded, we know it's a refresh
	var today = new Date();
	var now = today.getUTCSeconds();
	document.cookie = 'SHTS=' + now + ';';
	document.cookie = 'SHTSP=' + escape(location.href) + ';';
}
else
{
	// Refresh detection has been disabled
	document.cookie = 'SHTS=;';
	document.cookie = 'SHTSP=;';
}
}

function disableRefreshDetection()
{
// The next page will look like a refresh but it actually
// won't be, so turn refresh detection off.
refresh_prepare = 0;

// Also return true so this can be placed in onSubmits
// without fear of any problems.
return true;
} 

//By default, turn refresh detection on
var refresh_prepare = 1;

/************************* Code for Detecting Page Refreshes - END *****************************/


/* Prevent form resubmit after pressing back button */						
function preventFormResubmitOnPressBackButton(){
 	debugger;
	var e=document.getElementById("refreshed");
	if(e.value=="no")e.value="yes";
	else{e.value="no";location.reload();}
}
/* Prevent form resubmit after pressing back button */						
					

	
    </script>
    </c:when>
	<c:otherwise>
		<center><font color="red" size="5"><span id="msg1"><c:out value="No Approvals Done"></c:out></span> </font></center>
	</c:otherwise>
	</c:choose>
	 
</form>
</body>
</html>
