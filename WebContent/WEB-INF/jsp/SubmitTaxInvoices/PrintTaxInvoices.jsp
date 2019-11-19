<%@page import="java.util.Map,java.text.SimpleDateFormat"%>
<%@page import="java.util.Map,java.util.List,java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="../CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		
<title>Print</title>

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
.btn-warning {
    color: #fff;
    background-color: #f0ad4e;
    border-color: #eea236;
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
								<!-- <a class="site_title" href="submitTaxInvoices.spring">Back</a> -->
				</div>
			</div>
	<div style="width:100%;border: 1px solid #000;height: 150px;">
		<div style="width:15%;float:left;">
			<img  class="text-img" style="float: left;margin-bottom: 44px;" src="images/LOGO_ISO.png" />
		</div>
		<div style="width:70%;float:left;">
		<h2 class="text-center" style="color:black;margin-top: 69px;font-size:21px">RECEIPT OF TAX INVOICES</h2>
		</div>
		<div style="width:15%;float:left;">
			<h2 class="text-center" style="color:black;margin-top: 69px;font-size:14px">Project : ${siteName}</h2>
		</div>
	</div>
    <table class="table" style="width: 100%;margin-top: -1px;">
        <tbody>
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
         		 <td  class="text-left">S.No.</td> 
                 <td  class="text-left" style="width:200px;height:33px">PO Id</td> 
                 <td  class="text-left">PO Date</td> 
                 <td  class="text-left">Invoice Number</td> 
                 <td  class="text-left">Invoice Date</td> 
                 <td  class="text-left" style="width:310px">Vendor Name</td> 
                 <td  class="text-left" style="width:230px">Invoice Amount</td> 
    			 <td  class="text-left">Store Received Date</td> 
                 <td  class="text-left">Submitted By</td> 
                 <td  class="text-left">Received By</td> 
                 <td  class="text-left">Received By</td> 
                 <td  class="text-left">Received By</td> 
                 <td  class="text-left">Received By</td> 
           </tr>
           
            <c:forEach items="${invoiceDetialsBean}" var="element">  
			   <tr>
			     <td  class="text-left">${element.strSerialNo}</td> 
                 <td  class="text-left" style="width:200px;height:33px">${element.poNo}</td> 
                 <td  class="text-left">${element.poDate}</td> 
                 <td  class="text-left">${element.invoiceNumber}</td> 
                 <td  class="text-left">${element.invoiceDate}</td> 
                 <td  class="text-left" style="width:310px">${element.vendorName}</td> 
                 <td  class="text-left" style="width:230px">${element.totalAmount}</td> 
    			 <td  class="text-left">${element.receivedDate}</td> 
    			 <td  class="text-left">${element.submittedBy}</td> 
                 <td  class="text-left">${element.receivedBy1}</td> 
                 <td  class="text-left">${element.receivedBy2}</td> 
                 <td  class="text-left">${element.receivedBy3}</td> 
                 <td  class="text-left">${element.receivedBy4}</td> 
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
    		<div class="col-md-12 center-block text-center">
    			<button onclick="myFunction()" id="printpage" class="btn btn-warning" style="padding: 6px 12px;border-radius: 9px;width: 110px; height: 30px;margin-top: 55px;">Print</button>
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
	/* code for Backbutton */
  	/* history.pushState(null, null, location.href);
    window.onpopstate = function () {
        history.go(1);
    }; */
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
		<center><font color="red" size="5"><span id="msg1"><c:out value="Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation."></c:out></span> </font></center>
	</c:otherwise>
	</c:choose>
	 
</form>
</body>
</html>
