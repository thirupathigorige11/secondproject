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

<body class="nav-md">
	<div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">

					<div class="clearfix"></div>

					<jsp:include page="../SideMenu.jsp" />

				</div>
			</div>
			<jsp:include page="../TopMenu.jsp" />


			<!-- page content -->
			<div class="right_col" role="main">
<c:choose>
<c:when test="${not empty isNotRefeshed}">
			<%-- <c:forEach items="${successPayments}" var="element">  
				<font color="green" size="5">${element.isVNDorCNT},${element.intPaymentTransactionId}</font><br>
			</c:forEach>
			<c:forEach items="${failedPayments}" var="element">  
				<c:choose>
			 		<c:when test = "${element.substring(0,6).equals('Failed')}">
			 			<font color="red" size="5">${element.intPaymentTransactionId}</font>
			 		</c:when> 
					<c:otherwise>
						
					</c:otherwise>	 
				</c:choose>
			</c:forEach>
			 --%>
 			<c:if test="${response.equals('VerificationFailed')}">
 				<%-- <center><font color="red" size="5">Amount Verification Failed.</font></center><br> --%>
				<c:forEach items="${errMsg}" var="element">  
					<center><font color="red" size="5">${element}</font></center><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${response.equals('FormulaProblem')}">
 				<center><font color="red" size="5">Remove Formulas in Excel & Upload Again.</font></center><br>
			</c:if>
			
			<c:if test="${response.equals('Success')}">
 				<center><font color="green" size="5">BOQ Uploaded Successfully.</font></center><br>
 				<c:forEach items="${errMsg}" var="element">  
					<center><font color="green" size="5">${element}</font></center><br>
				</c:forEach>
			</c:if>
			
			<c:if test="${response.equals('Exception')}">
 				<center><font color="red" size="5">BOQ Upload Failed.</font></center><br>
 				<c:forEach items="${errMsg}" var="element">  
					<center><font color="red" size="5">${element}</font></center><br>
				</c:forEach>
			</c:if>
 			
			
			
			
			<%-- <c:if test="${response.equals('Success')}">
 				<font color="green" size="5"><center>All Payments Updated Successfully.</center><br><br><br>see success payments below.</font><br>
 				<c:forEach items="${successPayments}" var="element">  
						<font color="green" size="5">
						<c:if test="${element.isVNDorCNT.equals('VND')}"><c:if test="${not empty element.strPONo}">PO Number - ${element.strPONo},</c:if> <c:if test="${not empty element.strInvoiceNo}">Invoice No - ${element.strInvoiceNo}</c:if></c:if>
						<c:if test="${element.isVNDorCNT.equals('CNT')}"><c:if test="${not empty element.workOrderNo}">WorkOrder No - ${element.workOrderNo}</c:if></c:if>
						</font><br>
				</c:forEach>
			</c:if>
			
			
			<c:if test="${response.equals('Failed')}">
 				<font color="red" size="5"><center>Some Payments Updated. But some are Failed.</center></font> 
 				<br><br><br><font color="green" size="5">see success payments below.</font><br>
 				<c:forEach items="${successPayments}" var="element">  
						<font color="green" size="5">
						<c:if test="${element.isVNDorCNT.equals('VND')}"><c:if test="${not empty element.strPONo}">PO Number - ${element.strPONo},</c:if> <c:if test="${not empty element.strInvoiceNo}">Invoice No - ${element.strInvoiceNo}</c:if></c:if>
						<c:if test="${element.isVNDorCNT.equals('CNT')}"><c:if test="${not empty element.workOrderNo}">WorkOrder No - ${element.workOrderNo}</c:if></c:if>
						</font><br>
				</c:forEach>
 				<br><br><font color="red" size="5">see failed payments below.</font><br>
				<c:forEach items="${failedPayments}" var="element">  
					<font color="red" size="5">
						<c:if test="${element.isVNDorCNT.equals('VND')}"><c:if test="${not empty element.strPONo}">PO Number - ${element.strPONo},</c:if> <c:if test="${not empty element.strInvoiceNo}">Invoice No - ${element.strInvoiceNo}</c:if></c:if>
						<c:if test="${element.isVNDorCNT.equals('CNT')}"><c:if test="${not empty element.workOrderNo}">WorkOrder No - ${element.workOrderNo}</c:if></c:if>
						</font><br>
				</c:forEach>
			</c:if>
			
 
 			<c:if test="${response.equals('Exception')}">
 				<font color="red" size="5"><center>Sorry, Update Failed. Exception Occurred.</center></font><br><br><br>
				<c:if test="${not empty successPayments}">
 					<font color="green" size="5">Here are some payments updated. </font><br>
					<c:forEach items="${successPayments}" var="element">  
						<font color="green" size="5">
						<c:if test="${element.isVNDorCNT.equals('VND')}"><c:if test="${not empty element.strPONo}">PO Number - ${element.strPONo},</c:if> <c:if test="${not empty element.strInvoiceNo}">Invoice No - ${element.strInvoiceNo}</c:if></c:if>
						<c:if test="${element.isVNDorCNT.equals('CNT')}"><c:if test="${not empty element.workOrderNo}">WorkOrder No - ${element.workOrderNo}</c:if></c:if>
						</font><br>
					</c:forEach>
				</c:if>
			</c:if> --%>
			

</c:when>
<c:otherwise>
<center><font color="red" size="5"><span id="refresh_msg">Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.</span></font></center>
</c:otherwise>
</c:choose>

				</div>
                  
  </div>
  </div>
 

<input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">
  </body>

<script src="js/jquery-ui.js"></script>

					<script src="js/custom.js"></script>

					<script>
					
					/* code for Backbutton */
				  	history.pushState(null, null, location.href);
				    window.onpopstate = function () {
				        history.go(1);
				    };
				    /* code for Backbutton */
				    
			$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});

										});

						$(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
						});
						
						//this code for to active the side menu 									
						var referrer=$("#pageType").val();
						if(referrer==''||referrer==null){referrer="empty";}
						$SIDEBAR_MENU.find('a').filter(function () {
						var urlArray=this.href.split( '/' );
						for(var i=0;i<urlArray.length;i++){
						if(urlArray[i]==referrer) {
						return this.href;
						}
						}
						}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');

					</script>

</html>
