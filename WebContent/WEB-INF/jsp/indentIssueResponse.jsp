<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="CacheClear.jsp" />  
<style type="text/css">
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
	</head>

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
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Library</li>
						</ol>
					</div>
					
					
					<div>
					<c:choose>
					  	<c:when test="${empty exceptionMsg}">
							<div class="col-md-12 text-center center-block" style="margin-top: 30px;">
								<button class="btn btn-check"><i class="fa fa-check" aria-hidden="true"></i></button>
							</div>
						</c:when>
						<c:otherwise>
						    <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
								<button class="btn btn-warning"><i class="fa fa-warning" aria-hidden="true"></i></button>
							</div>
						</c:otherwise>
					</c:choose>
					  	<div align="center" style="color: green;font-size: 20px;font-weight: 600;padding-top: 100px;" >
							<c:choose>
					  			<c:when test="${empty exceptionMsg}">
						    		Material Issued Successfully...
						  		</c:when>
						 		<c:otherwise>
						    		<span style="color: red;"> ${exceptionMsg} </span><br/>
						  		</c:otherwise>
							</c:choose>
						</div>	
					</div>
					
					<!-- /page content -->        
				</div>
			</div>
		</div>
   
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script> 
		<script src="js/sidebar-resp.js"></script>
		
		
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			/* $(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
			
			//this code for to active the side menu 
			var referrer="${urlForActivateSubModule}";
			if(referrer.length!=0){
				$SIDEBAR_MENU.find('a').filter(function () {
			           var urlArray=this.href.split( '/' );
			           for(var i=0;i<urlArray.length;i++){
			        	 if(urlArray[i]==referrer) {
			        		 return this.href;
			        	 }
			           }
			    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
			}
			
			history.pushState(null, null, location.href);
		    window.onpopstate = function () {
		        history.go(1);
		    };
		</script>
	</body>
</html>
