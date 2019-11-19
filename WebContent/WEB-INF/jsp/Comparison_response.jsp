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
</style>
<script type="text/javascript">
if (typeof(Storage) !== "undefined") {
	debugger;
    sessionStorage.setItem("${UserId}tempRowsIncre12",2);
 } else {
    alert("Sorry, your browser does not support Web Storage...");
 };
 </script>

<body class="nav-md">
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
			<%-- <c:if test="${comparison}">
			<div class="col-md-12 anchor-back">
					<a href="${requestScope['url']}">Back</a>
			</div>
			</c:if> --%>
				
 <center><font color="green" size="5"><c:out value="${requestScope['message']}"></c:out> </font></center>
 
  <center><font color="red" size="5"><c:out value="${requestScope['message1']}"></c:out> </font></center>

<center><font color="green" size="5"><c:out value="${requestScope['response']}"></c:out> </font></center>
 
  <center><font color="red" size="5"><c:out value="${requestScope['response1']}"></c:out> </font></center>
  
  
<div class="success_page" style="margin-top: 2%;"><font color="green" size="4"><strong>${customMsg}</strong></font></div>

	<input type="hidden" name="urlForActivateSubModuleWoBills" id="urlForActivateSubModuleWoBills" value="${urlForActivateSubModule}">
</div>
                  
</div>
</div>
 


  </body>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
    <script src="js/sidebar-resp.js"></script>

<script>
			
					
				    
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
			}); */
		</script>

</html>
