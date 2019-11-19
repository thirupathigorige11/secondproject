<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>

</head>

<style>
.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
.inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

}
.media-style{
width:39% !important;

}
.submitstyle{
margin-top: 20px;
width: 23% !important;
}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}


.btn-check{
border-radius:50%;
background-color:#008000;
height:50px;
width:50px;
border:1px solid #008000;
}
.btn-check i{
font-size: 25px;	
color: #fff;
}
.btn-warning{
border-radius:50%;
background-color:red;
height:50px;
width:50px;
border:1px solid red;
}
.fa-warning i{
font-size: 25px;	
color: #fff;
}

</style>
<script type="text/javascript">
if (typeof(Storage) !== "undefined") {
	debugger;
    sessionStorage.setItem("${UserId}tempRowsIncre12",2);
    sessionStorage.setItem("${UserId}receiveTaxInvoices",2);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>
<!--  onLoad="JavaScript:checkRefresh1();" onUnload="JavaScript:prepareForRefresh1();"  -->
<body class="nav-md">
<form onSubmit="JavaScript:disableRefreshDetection()">
	<div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="SideMenu.jsp" />

				</div>
			</div>
			<jsp:include page="TopMenu.jsp" />


			<!-- page content -->
			<div class="right_col" role="main">
				<c:if test="${iscancel}">
			<a class="anchorback" href="${requestScope['urlName']}" style="text-decoration:underline">Back</a>
			</c:if>
<c:choose>
					<c:when test="${not empty response || not empty message}">
						<div class="col-md-12 text-center center-block"
							style="margin-top: 30px;">
							<button class="btn btn-check">
								<i class="fa fa-check" aria-hidden="true"></i>
							</button>
						</div>
					</c:when>
					<c:otherwise>
						<div class="col-md-12 text-center center-block"
							style="margin-top: 30px;">
							<button class="btn btn-warning">
								<i class="fa fa-warning" aria-hidden="true"></i>
							</button>
						</div>
					</c:otherwise>
</c:choose>
  <center class="hideMsg"><font color="green" size="5"><c:out value="${requestScope['message']}"></c:out> </font></center>
 
  <center class="hideMsg"><font color="red" size="5"><c:out value="${requestScope['message1']}"></c:out> </font></center>

 <center class="hideMsg"><font color="green" size="5"><c:out value="${requestScope['response']}"></c:out> </font></center>
 
  <center class="hideMsg"><font color="red" size="5"><c:out value="${requestScope['response1']}"></c:out> </font></center>
  
  
<div class="success_page" style="margin-top: 2%;"><font color="green" size="4"><strong>${customMsg}</strong></font></div>

<input type="hidden" name="urlForActivateSubModuleWoBills" id="urlForActivateSubModuleWoBills" value="${urlForActivateSubModule}">
<c:if test="${iscancel}">
<p class ="text-left;" style="color: green;font-size: 24px"><c:out value="${requestScope['message2']}"></c:out></p>
<p class="text-left;" style="color: green;font-size: 24px"><c:out value="${requestScope['response2']}"></c:out></p>
</c:if>
				</div>
                  
  </div>
  </div>
 
<input type="hidden" id="refreshed" value="no">
</form>
  </body>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
    <script src="js/sidebar-resp.js"></script>

					<script>
					
					/* code for Backbutton */
				  	history.pushState(null, null, location.href);
				    window.onpopstate = function () {
				        history.go(1);
				    };  
				    /* code for Backbutton */
				    
			$(document).ready(function() {
				$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});

			});

			debugger;  
			//this code for to active the side menu 
			var referrer=$("#urlForActivateSubModuleWoBills").val();
			if(referrer.length!=0){
			debugger;
				$SIDEBAR_MENU.find('a').filter(function () {
			           var urlArray=this.href.split( '/' );
			           for(var i=0;i<urlArray.length;i++){
			        	 if(urlArray[i]==referrer) {
			        		 return this.href;
			        	 }
			           }
			    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
			}
			
					/*	$(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
						});

						/************************ Code for Detecting Page Refreshes - START ****************************/
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
						document.getElementById("NoApprovals_msg").innerHTML="";

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

						// By default, turn refresh detection on
						var refresh_prepare = 1;

						/************************ Code for Detecting Page Refreshes - END ****************************/
				/* / Prevent form resubmit after pressing back button /	 */
						function preventFormResubmitOnPressBackButton(){
						debugger;
						var e=document.getElementById("refreshed");
						if(e.value=="no")e.value="yes";
						else{e.value="no";location.reload();}
						}
				/* / Prevent form resubmit after pressing back button / */
					</script>

</html>
