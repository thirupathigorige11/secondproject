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
	<!-- 	<link href="css/Custom.min.css" rel="stylesheet"> -->
		
	</head>

	<body >
	
	
		<div class="loginbody">
			<div class="loginbox">
				<img src="images/loginlogo.png">
				
				
				
				<form  class="form-horizontal" id="CreateCentralIndentFormId" action="forGetPassword.spring"  method="post" autocomplete="off">
					
					<span style="color:red;">${Message}</span><br/>
					
					<div class="form-group">
					
						 <div class="col-sm-12 col-xs-12">
							
							<select  id="site_id"  name="site_id"  class="form-control">
				                <option id=""  value="">-- Select Site--</option>
								<option  value="102">ACROPOLIS</option>
								<option  value="103">LNR LAKE BREEZE</option>
								<option  value="104">SILVER RIPPLES</option>
								<option  value="105">SOHAM</option>
								<option  value="106">PRANAVAM MTB</option>
								<option  value="107">EDEN GARDEN</option>
								<!-- <option  value="108">ASSET STORE</option> -->
								<!-- <option  value="109">MADHURAM</option> -->
								<option   value="301">SHIKARAM</option>
								<option  value="999">CENTRAL</option>
								<option  value="998">PURCHASE DEPARTMENT</option>
								<option  value="112">DEV-HYD</option>
								<!-- <option  value="999">Central_phase2_testing</option>
								<option  value="402">soham_phase2_testing</option>
								<option  value="403">Eden_garden_testing</option>
								<option  value="998">purchase_phase2_testing</option> -->
								<option  value="111">NANDANAM</option>
								<option  value="114">HORIZON</option>
								<option  value="122">DEV-BNG</option>
								<option  value="115">TESTING</option>
								<option  value="119">HO-BNG</option>
								<option  value="997">ACCOUNTS DEPARTMENT</option>
								<option  value="118">WAVE ROCK</option>
								<option  value="110">ESSENZA</option>
								<option  value="996">MARKETING</option>
								<option  value="124">SUSHANTHAM</option>
								<option  value="126">LAKE SHORE</option>
								<option  value="127">BIOGAS PLANT</option>
								<option  value="201">PROJECT1</option>
								<option  value="202">PROJECT2</option>
								<option  value="203">PROJECT3</option>
								<option  value="204">PROJECT4</option>
								<option  value="205">PROJECT5</option>
								<option  value="206">PROJECT6</option>
								<option  value="201">Testing1</option>
								<option  value="202">Testing2</option>
						</select>	
						</div> 
					
					</div>
					
					
			<!-- 		<div class="form-group">
					
					<div class="col-sm-12 col-xs-12">
					
					
					
							<input type="text" class="form-control" placeholder="User name">
							<label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">User Name:</label>
							<input type="text" class="form-control"  name="userName"  placeholder="userName" autocomplete="off"><br>
						</div>
					</div>
					
					<label class="control-label  col-xs-12 col-md-5" style="font-size: 14px;margin-top: -40px;margin-left: 82px;"><center>OR</center></label>
					 -->
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">Emp Id:</label> -->
							<input type="text" class="form-control" name="empId"  placeholder="empId" autocomplete="off">
						</div>
					</div>
					 <div class="form-group">
						<div class="col-sm-12 col-xs-12">
						<button type="submit" class="form-control btn btn-default " padding="0.01em 140px;" >LOGIN</button>
						
						<!-- <button type="button" value=""  class="form-control btn btn-default onclick="viewNewPassword()">SUBMIT</button> -->
						</div>
					   </div>
					   
		<!-- 			<div class="form-group">
						<div class="col-sm-6 col-xs-6 text-left">
							<input id="checkbox-1" class="checkbox-custom flat" name="checkbox-1" type="checkbox" >
							<label for="checkbox-1" class="checkbox-custom-label">Remember password.</label>
						</div>
						<div class="col-sm-6 col-xs-6 text-right">
							<a href="javascript:void(0);"><i class="fa fa-lock" aria-hidden="true"></i> Forgot password?</a>
						</div>
					</div> -->
				</form>
				<% 
				String strResponse=request.getParameter("showPage");
				if(strResponse.equalsIgnoreCase("true")){
				
				%>
				
				<form  class="form-horizontal" id="CreateCentralIndentFormId" action="changeToNewPassword.spring"  method="post" autocomplete="off">
					<div class="form-group">
						
					</div>
					<div class="form-group">
					<span style="color:red;">${Message}</span><br/>
					<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="User name"> -->
							
							<input type="hidden" class="form-control"  name="rand_Num" value="${rand_Num}" autocomplete="off">
							 <input type="hidden" class="form-control"  name="dbSaltPassword" value="${dbSaltPassword}" autocomplete="off"> 
							<input type="hidden" class="form-control"  name="empId" value="${empId}" autocomplete="off">
							<input type="hidden" class="form-control"  name="site_id" value="${site_id}" autocomplete="off">
							
						</div>
					
					<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="User name"> -->
							<!-- <label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">New Password:</label> -->
							<input type="text" class="form-control"  name="userName" placeholder="userName"   autocomplete="off"><br>
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">OTP:</label> -->
							<input type="password" class="form-control" name="newPassword" placeholder="newPassword"  autocomplete="off">
						</div>
					</div>
					
					<div class="form-group">
					<div class="col-sm-12 col-xs-12">
				<!-- 	<label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">Conform Password:</label> -->
							<input type="password" class="form-control"  name="conformPassword"  placeholder="conformPassword" autocomplete="off"><br>
					</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <label class="control-label  col-xs-12 col-md-5" style="font-size:14px;">OTP:</label> -->
							<input type="text" class="form-control" name="otp" placeholder="otp"  autocomplete="off">
						</div>
					</div>
					 <div class="form-group">
						<div class="col-sm-12 col-xs-12">
						<button type="submit" class="form-control btn btn-default">LOGIN</button>
						
						<!-- <button type="button" value=""  class="form-control btn btn-default onclick="viewNewPassword()">SUBMIT</button> -->
						</div>
					   </div>
					   
		<!-- 			<div class="form-group">
						<div class="col-sm-6 col-xs-6 text-left">
							<input id="checkbox-1" class="checkbox-custom flat" name="checkbox-1" type="checkbox" >
							<label for="checkbox-1" class="checkbox-custom-label">Remember password.</label>
						</div>
						<div class="col-sm-6 col-xs-6 text-right">
							<a href="javascript:void(0);"><i class="fa fa-lock" aria-hidden="true"></i> Forgot password?</a>
						</div>
					</div> -->
				</form>
				
			<%} %>	
				
				
			</div>
		</div>
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<script>
			$(function(){
					$(".loginbody").css('min-height', $(document).height());
			});
			
			

			function viewNewPassword() {
			
			
			document.getElementById("CreateCentralIndentFormId").action ="forGetPassword.spring";
			document.getElementById("CreateCentralIndentFormId").method ="POST";
			document.getElementById("CreateCentralIndentFormId").submit();
		}
			
			
		</script>
		
		<script>
		
	  	history.pushState(null, null, location.href);
	    window.onpopstate = function () {
	        history.go(1);
	    };
	
		</script>
	</body>
</html>
