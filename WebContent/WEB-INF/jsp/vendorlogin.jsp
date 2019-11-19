<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="CacheClear.jsp" />  
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
				<form  class="form-horizontal" action="vendorloginsubmit.spring"  method="post" autocomplete="off">
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							
			<!--  			<select  id="site_id"  name="site_id"  class="form-control">
				                <option id=""  value="">-- Select Site--</option>
								<option  value="102">ACROPOLIS</option>
								<option  value="103">LNR LAKE BREEZE</option>
								<option  value="104">SILVER RIPPLES</option>
								<option  value="105">SOHAM</option>
								<option  value="106">PRANAVAM MTB</option>
								<option  value="107">EDEN GARDEN</option>
								<option  value="108">ASSET STORE</option>
								<option  value="109">MADHURAM</option>
								<option   value="301">SHIKARAM</option>
								<option  value="999">CENTRAL</option>
								<option  value="113">PURCHASE DEPARTMENT</option>
								<option  value="112">Development</option>
								<option  value="401">Central_phase2_testing</option>
								<option  value="402">soham_phase2_testing</option>
								<option  value="403">Eden_garden_testing</option>
								<option  value="404">purchase_phase2_testing</option>
						</select>	-->	
						<input type="hidden" name="indentNumber" value="${requestScope['indentNumber']}"/>
						<input type="hidden" name="vendordata" value="${requestScope['vendordata']}"/>
						<input type="hidden" name="indentCreationDetailsIdForenquiry" value="${requestScope['indentCreationDetailsIdForenquiry']}"/>
						<input type="hidden" name="childProductMap" value="${requestScope['childProductMap']}"/>
						
						</div>
					</div>
					<div class="form-group">
					<span style="color:red;">${Message}</span><br/>
					<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="User name"> -->
							<input type="text" class="form-control"  name="uname"  placeholder="UserName" autocomplete="off">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-12 col-xs-12">
							<!-- <input type="text" class="form-control" placeholder="Password"> -->
							<input type="password" class="form-control" name="pass"  placeholder="Password" autocomplete="off">
						</div>
					</div>
					 <div class="form-group">
						<div class="col-sm-12 col-xs-12">
						<button type="submit" class="form-control btn btn-default">LOGIN</button>
						</div>
					   </div>
					   
					<div class="form-group">
						<div class="col-sm-6 col-xs-6 text-left">
							<input id="checkbox-1" class="checkbox-custom flat" name="checkbox-1" type="checkbox" >
							<label for="checkbox-1" class="checkbox-custom-label">Remember password.</label>
						</div>
						<div class="col-sm-6 col-xs-6 text-right">
							<a href="javascript:void(0);"><i class="fa fa-lock" aria-hidden="true"></i> Forgot password?</a>
						</div>
					</div>
				</form>
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
		</script>
	</body>
</html>
