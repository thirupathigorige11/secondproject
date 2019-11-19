<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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
<body class="nav-md">
	<div class="container body">
		<div class="main_container" id="main_container" style="margin-top: 5%;">

 <c:if test="${not empty buttonType}">
	<form:form action="${action}"  modelAttribute="WorkOrderBean">
				<form:input type="hidden" path="siteId"/>
				<form:input type="hidden" path="userId"/>
				<form:input type="hidden" path="QS_Temp_Issue_Id"/>
				<form:input type="hidden" path="siteWiseWONO"/>
				<form:input type="hidden" path="typeOfWork"/>
				<form:input type="hidden" path="workOrderStatus"/>
				<form:input type="hidden" path="isSaveOrUpdateOperation"/>
				<form:input type="hidden" path="approverEmpId"/>
				<form:input type="hidden" path="requestFromMail"/>
				<div class="col-md-12 text-center center-block" style="margin-top: 20px;">
					<div class="form-group">
						<label class="col-md-12">Please Enter Comment</label>
						<div class="col-md-12">
							<textarea class="form-control " name="remarks" id="remarks" placeholder="Please enter comment" style="width: 350px; margin: 15px auto;"></textarea>
						</div>
					</div>
				</div>

				<div class="col-md-12 text-center center-block">
					<button type="submit" class="btn btn-success" onclick="return validateForm()">Submit</button>
				</div>
	</form:form>
</c:if>
			
			<c:if test="${empty buttonType}">
				<c:choose>
				  	<c:when test="${not empty response}">
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
			
				<div class="" style="padding-top: 8%;" role="main">
						<center><font color="green" size="5"><c:out value="${requestScope['response']}"></c:out> </font></center>
	 					<center><font color="red" size="5"><c:out value="${requestScope['response1']}"></c:out> </font></center>
	  			</div>
 			</c:if>	
		</div>
	</div>

	<script src="js/jquery-ui.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script type="text/javascript">
		function validateForm() {
	debugger;
			var remarks = $("#remarks").val().trim();
			if (remarks == null || remarks == "") {
				alert("Please enter remarks");
				return false;
			}
/* 			 var canISubmit = window.confirm("Do you want submit?");
		     
		     if(canISubmit == false) {
			   return false;
		     }
 */			    
			return true;
		}
	</script>
</body>
</html>
