 <!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>

<!-- <title>Approve RA Bill</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<!-- <script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script> -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>

<%-- <c:set value="END" var="isCompletedBill"></c:set>
<c:if test="${billBean.approverEmpId eq isCompletedBill}">
	<c:if test="${billBean.paymentType ne 'NMR'}">
		<script src="js/WorkOrder/ShowCompletedRAAdv.js" type="text/javascript"></script>
	</c:if>
	<c:if test="${billBean.paymentType eq 'NMR'}">
		<script src="js/WorkOrder/NMRCompleted.js" type="text/javascript"></script>
	</c:if>
</c:if>

<c:if test="${billBean.approverEmpId  ne isCompletedBill}">
	<c:if test="${billBean.paymentType ne 'NMR'}">
			<script src="js/WorkOrder/ShowRaAdvStatus.js" type="text/javascript"></script>
	</c:if>
	<!--  based on the payment type loading JS files  -->
	<c:if test="${billBean.paymentType eq 'NMR'}">
			<script src="js/WorkOrder/NMRStatus.js" type="text/javascript"></script>
	</c:if>
</c:if> --%>

<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>

</head>

<style>
/*css code for print */
th{text-align:center !important;}
tbody{color:#000;font-weight:bold;}
@media print{
.print-tnlrowcolor{background-color:#ccc !important;}
.nav_menu, .print_hide, .breadcrumb, #printshowRa{
display:none;
}
.modal_recovery_class{display:none;}
.border-none{border:0px !important;}
.btn-print-row{
display:none;}
thead {
display: table-row-group;
}
}
/*css code for print */
/* fixed header table css for modal*/
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:70px;
}
/* .scroll-rabill-tbl1{
 width: calc(100% - 17px) !important;
 height:70px;
} */
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }
 
 
/* fixed header table css for modal*/
.modal-body-scroll{
max-height:450px;
overflow-y:auto;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}
.scroll-rabill-tbl>table>thead>tr>th{
border:1px solid #000;
background-color:#ccc;
color:#000;
border-top:1px solid #000 !important;
text-align:center;
}
.scroll-rabill-tbl>table>tbody>tr>td{
border:1px solid #000;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}

.rabill-class-select{
width:130px;
}
.mrg-Top{
margin-top:5px;
}
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
.td-active input{
background-color:#dbecf6;
}
.td-active{
background-color:#dbecf6;
}
.td-active input:focus{

}
.td-active input{
font-weight:bold;
}
.border-none input{
border:none !important;
}
.input-outline input{
outline:none;
}
.table-border-certificateofpayment thead tr th, .table-border-certificateofpayment tbody tr td{
 border:1px solid #000;
 font-weight: bold;
}
.table-border-certificateofpayment{
border:1px solid #000;

}
/* written by thirupathi */
.deduction{
	width: 200px;
	 text-align:center;
	/*  padding-left:8%; */
	 margin-left:30%;
}
.deduction .lst{
  text-align:center;
}
.hidebtn1{
    visibility: hidden;
}
.deletebtn{
	margin-left:2%;
}
.heightthirty{
  height:30px;
}
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("viewRABilsforapprovles.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>

<body class="nav-md">
	 <!-- <div class="overlay_ims"></div>
	 <div class="loader-ims" id="loaderId"> 
		<div class="lds-ims">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingimsMessage">Loading...</div>
	</div> -->
	
	
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
		<div>
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a href="#">Home</a></li>
					<li class="breadcrumb-item active">Work Order </li>
				</ol>
		</div>
<form:form   modelAttribute="billBean"   id="contractorRABill" class="form-horizontal" >
	<div class="">

    </div>    
</form:form>
 
 <div class="clearfix"></div>
			<div class="col-md-12">
			<div class="table-responsive Mrgtop10">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">

							<tr>
							<th>S.No</th>
								<th>Creation Date</th>
								<!-- <th>Work Order Date</th> -->
								<th>Work Order Number</th>
								<th>Total Amount</th>
								<!-- <th>Work Order Name</th>
								<th>Type Of Work</th>
								<th>Contractor Name</th> -->
							</tr>
						</thead>
						<tbody>
						<c:set value="1" var="count"></c:set>
				<c:forEach items="${listOfCompletedWorkOrder}" var="element">  
				<tr>
				<td  class="text-center">${count} <c:set value="${count+1}" var="count"></c:set></td>
				
				    <td style="color: black;" class="text-center">${element.workOrderDate}</td>
					<%-- <td style="color: black;">${element.workOrderCreadeDate}</td> --%>
					<td  class="text-center"><a target="_blank" href="getMyCompletedWorkOrder.spring?WorkOrderNo=${element.workOrderNo}&workOrderIssueNo=${element.QS_Temp_Issue_Id}&site_id=${element.siteId}&operType=1&isUpdateWOPage=false" style="text-decoration: underline;color: blue;">${element.workOrderNo}</a></td>
					<td style="color: black;"  class="text-center"><span class="addComma">${element.totalAmount}</span></td>
					<%-- <td  style="color: black;">${element.workOrderName}</td>
					<td   style="color: black;">${element.typeOfWork}</td>
					<td style="color: black;">${element.contractorName}</td> --%>
				</tr>
				</c:forEach>			
				<tbody>				
				</tbody>
					</table>
				</div>
				</div>
 
 
 
 <div class="col-md-12 text-center center-block Mrgtop20">
 <label>&nbsp;</label>
 </div>
 <div class="col-md-12 text-center center-block Mrgtop20">
   <!-- <input type="button" id="printshowRa" class="btn btn-warning" value="Print" onclick="myPrint()">
  -->
 </div>

<!-- Modal popup for approve RABill End -->
<input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
</body>

   <script src="js/jquery-ui.js"></script>
   <script src="js/custom.js"></script>
   <script>

   $(window).load(function () {
	   $('.addComma').each(function(){
			if (isNum($(this).text())){ // regular expression for numbers only.
		    debugger;
		    	var tempVal=parseFloat($(this).text());
		    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"&&this.id!="notificationBell"){
		    		$(this).text(inrFormat(tempVal.toFixed(2)));
		    	}
	          }
		});
	});
	function isNum(value) {
		  var numRegex=/^[0-9.]+$/;
		  return numRegex.test(value)
	}

   /*for print */
	function  myPrint(){
		window.print();
	}
   
	//this code for to active the side menu 
	var referrer="viewMyWorkOrders.spring";
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
