<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<!-- bootstrap styles -->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
		<!-- Bootstrap styles -->
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- Font Awesome -->
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<!-- Custom Theme Style -->
		<link href="css/style.css" rel="stylesheet">
		<link href="css/Custom.min.css" rel="stylesheet">
		<style>
		body{
		background-color:#000;
		}
		
		</style>
	</head>

	<body >
	
	<div class="page-sendemail">
	<form action="sendMailWithoutLogin.spring" class="form-horizontal">
	<h4 class="text-center"><strong>Send E-mails To Customers</strong></h4>
	<div class="col-md-12 col-xs-12">
	<div class="form-group">
	 <label class="col-md-3 col-xs-12"> Sent From E-mail : </label>
	<div class="col-md-9 col-xs-12"><input type="email" class="form-control" name="sentfromemail"></div>
	</div>
	<div class="form-group">
	 <label class="col-md-3 col-xs-12"> Password : </label>
	<div class="col-md-9 col-xs-12"><input type="password" class="form-control" name="passwordforemail"></div>
	</div>
	<div class="form-group">
	 <label class="col-md-3 col-xs-12"> Subject : </label>
	<div class="col-md-9 col-xs-12"><input type="text" class="form-control" name="subject"></div>
	</div>
	<div class="form-group">
	<label class="col-md-3 col-xs-12"> Body : </label>
	<div class="col-md-9 col-xs-12">
	 <textarea name="mailBody" class="form-control text-resize" cols="50" rows="7"></textarea> 
	</div>
	</div>
	<div class="form-group">
	<label class="col-md-3 col-xs-12"> Signature : </label>
	<div class="col-md-9 col-xs-12">
	 <textarea name="signature" class="form-control text-resize" cols="50" rows="7"></textarea> 
	</div>
	</div>
    <div class="col-md-12 text-center center-block">
     <input type = "submit" class="btn btn-danger" value="Send Email">
    </div>

	</div>
		</form>
		</div>
	</body>
</html>
