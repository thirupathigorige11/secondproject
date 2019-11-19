<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
		<link href="css/style.css" rel="stylesheet">
		<link href="css/Custom.min.css" rel="stylesheet">
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
		<style>
		
			.loginerrmsg{
			background-color: red;
	        padding: 6px;
	        margin-bottom: 5px;
			}
		
		</style>
		
		
	</head>

	<body >
	
		<div class="loginbody">
			<div class="loginbox">
				<img src="images/loginlogo.png">
				<form  class="form-horizontal" action="login.spring"  method="post" autocomplete="off">
<!-- 					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							
							<select  id="site_id"  name="site_id"  class="form-control">
				                <option id=""  value="">-- Select Site--</option>
								<option  value="102">ACROPOLIS</option>
								<option  value="103">LNR LAKE BREEZE</option>
								<option  value="104">SILVER RIPPLES</option>
								<option  value="105">SOHAM</option>
								<option  value="106">PRANAVAM MTB</option>
								<option  value="107">EDEN GARDEN</option>
								<option  value="108">ASSET STORE</option>
								<option  value="109">MADHURAM</option>
								<option  value="301">SHIKARAM</option>
								<option  value="111">Nandhanam-I</option>
								<option  value="119">HO-BNG</option>
								<option  value="999">CENTRAL</option>
								<option  value="998">PURCHASE DEPARTMENT</option>
								<option  value="112">Development</option>
								<option  value="999">Central_phase2_testing</option>
								<option  value="402">soham_phase2_testing</option>
								<option  value="403">Eden_garden_testing</option>
								<option  value="998">purchase_phase2_testing</option>
								<option  value="997">ACCOUNTS DEPARTMENT</option>
						</select>	
						</div>
					</div>  -->
					 <% String loginFailed=request.getAttribute("loginFailed").toString();
					if(loginFailed.equalsIgnoreCase("true")){
					%> 
					<div class="loginerrmsg"> <i class="fa fa-exclamation-triangle" aria-hidden="true"></i>&nbsp&nbsp ${Message}</div>
					<%} else {%> 
					
				<div class="" > <i class="" aria-hidden="true"></i>&nbsp&nbsp ${Message}</div>
					<%} %>
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="User name"> -->
							<input type="text" class="form-control inputctrl"  id="frontend" name="site_id_backend"   placeholder="Please enter site" required>
							<input type="hidden" class="form-control"  id="backend" name="site_id"   placeholder="Please enter site" >
							
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="User name"> -->
							<input type="text" class="form-control inputctrl"  name="uname"  placeholder="UserName" required>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="Password"> -->
							<input type="password" class="form-control inputctrl" name="pass"  placeholder="Password" required>
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<button type="submit" class="form-control btn btn-default loginbtn">LOGIN</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-6 col-xs-6 text-left">
							<input id="checkbox-1" class="checkbox-custom flat" name="checkbox-1" type="checkbox" >
							<label for="checkbox-1" class="checkbox-custom-label">Remember password.</label>
						</div>
						<div class="col-sm-6 col-xs-6 text-right">
							<a href="ForGetPassword.jsp?showPage=false"><i class="fa fa-lock" aria-hidden="true"></i> Forgot password?</a>
						</div>
					</div>
					<%--  <% String loginFailed=request.getAttribute("loginFailed").toString();
					if(loginFailed.equalsIgnoreCase("true")){
					%> 
					 --%>
					<%-- <input type="hi" id="errmsg" value="${Message}"> --%>
					<%-- <%} %>  --%>
					<%
					 
			
						String indentNo=request.getParameter("IndentApproval");
						out.println("<input type='hidden' name='IndentApproval' value='"+indentNo+"' />"); %>
				</form>
			</div>
		</div>
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
				<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		  <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
  		<!-- <link rel="stylesheet" href="/resources/demos/style.css"> -->
		  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script>
		  $( function() {
		    
			  var availableTags = [
	                                 {'label': 'ACROPOLIS', value:'ACROPOLIS',backend:102},
			                         {'label': 'LNR LAKE BREEZE', value:'LNR LAKE BREEZE',backend:103},
			                         { 'label' : 'SILVER RIPPLES' , value:'SILVER RIPPLES',backend:104} ,
			                         {'label': 'SOHAM', 'value':'SOHAM',backend:105},
			                         {'label': 'PRANAVAM MTB', 'value':'PRANAVAM MTB',backend:106},
			                         {'label': 'EDEN GARDEN', 'value':'EDEN GARDEN',backend:107},
			                       /*   {'label': 'ASSET STORE', 'value':'ASSET STORE',backend:108}, */
			                         /* {'label': 'MADHURAM', 'value':'MADHURAM',backend:109}, */
			                         {'label': 'SHIKARAM', 'value':'SHIKARAM',backend:301},
			                         {'label': 'NANDHANAM', 'value':'NANDHANAM',backend:111},
			                         {'label': 'CENTRAL', 'value':'CENTRAL',backend:999},
			                         {'label': 'PURCHASE DEPARTMENT', 'value':'PURCHASE DEPARTMENT',backend:998},
			                         {'label': 'DEV-HYD', 'value':'DEV-HYD',backend:112},
			                         {'label': 'Central_phase2_testing', 'value':'Central_phase2_testing',backend:999},
			                         {'label': 'soham_phase2_testing', 'value':'soham_phase2_testing',backend:402},
			                         {'label': 'Eden_garden_testing', 'value':'Eden_garden_testing',backend:403},
			                         {'label': 'purchase_phase2_testing', 'value':'purchase_phase2_testing',backend:998},
			                         {'label': 'ACCOUNTS DEPARTMENT', 'value':'ACCOUNTS DEPARTMENT',backend:997},
			                         {'label': 'HO-BNG', 'value':'HO-BNG',backend:119},
			                         {'label': 'HORIZON', 'value':'HORIZON',backend:114},
			                         {'label': 'DEV-BNG', 'value':'DEV-BNG',backend:122}, 
			                        {'label': 'TESTING', 'value':'TESTING',backend:115},
			                        {'label': 'MARKETING', 'value':'MARKETING',backend:996},
			                          {'label': 'SUSHANTHAM', 'value':'SUSHANTHAM',backend:124},
			                          {'label': 'LAKE SHORE', 'value':'LAKE SHORE',backend:126},
			                          {'label': 'BIOGAS PLANT', 'value':'BIOGAS PLANT',backend:127},
			                          {'label': 'PROJECT1', 'value':'PROJECT1',backend:201},
				                         {'label': 'PROJECT2', 'value':'PROJECT2',backend:202},
				                         {'label': 'PROJECT3', 'value':'PROJECT3',backend:203},
				                         {'label': 'PROJECT4', 'value':'PROJECT4',backend:204},
			                          {'label': 'PROJECT5', 'value':'PROJECT5',backend:205},
				                         {'label': 'PROJECT6', 'value':'PROJECT6',backend:206},
				                         {'label': 'Testing1', 'value':'Testing1',backend:201},
				                         {'label': 'Testing2', 'value':'Testing2',backend:202}
			                         
			                       
			                         ];


		    
		    var select = false;

		    $("#frontend").autocomplete({
		        source: availableTags,
		        autoFocus: true,
		        selectFirst: true,
		        open: function(event, ui) { if(select) select=false; },
		        select: function(event, ui) {
		        	console.log(ui.item.label,ui.item.value);
		           $('#frontend').val(ui.item.label); 
		         	$('#backend').val(ui.item.backend);
		           
		           
		           
		            select=true;
		         /*    return false; */
		        }
		    }).blur(function(){
		        if(!select)
		        {
		            $("#frontend").val($('ul.ui-autocomplete li:first a').text());
		        }
		    }); 
		    
		    
		  } );
			  
			$(function(){
					$(".loginbody").css('min-height', $(document).height());
			});
		</script>
		
		<script>
		
		
		var  errmsg="";
	
		  $(".loginbtn").click(function(){
			 
			
		  });
		
		  $(".inputctrl").keyup(function(){
    	     $(".loginerrmsg").hide();    	  
          });
     
		  </script>
		
		<script>
		
	  	history.pushState(null, null, location.href);
	    window.onpopstate = function () {
	        history.go(1);
	    };
	
		</script>
	</body>
</html>
