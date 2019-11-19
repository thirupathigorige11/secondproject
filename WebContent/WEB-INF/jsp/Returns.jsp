<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
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
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.error {color: red; display:block; padding-top:5px; font-size: 12px;}
.sucess {color: green; display:block; padding-top:5px; font-size: 12px;}
table.dataTable {border-collapse:collapse !important;}
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
								<li class="breadcrumb-item"><a href="#">Issue</a></li>
								<li class="breadcrumb-item active">Returns</li>
								<li class="breadcrumb-item active">Requester Id</li>
							</ol>
						</div>
						<div align="middle">
						  <label class="error"><c:out value="${requestScope['failed']}"></c:out></label>
						  <label class="sucess"><c:out value="${requestScope['sucess']}"></c:out></label>
						</div>
						<form action="indentReturnsController.spring" method="post">
						<%
										   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
										   if(isShowGrid.equals("true")){
										%>
						<div class="table-responsive">
							<table id="tblnotification"	class="table table-new" cellspacing="0">
								<thead>
									<tr>
									    <th> Date </th>
										<th> Issue Slip Number</th>
					    				<th> Contractor Name </th>
					    				<th> Requester Name </th> 
									    <th> Product</th>
					    				<th> Sub Product </th>
					    				<th> Child Product </th>    				
					    				<th> Measurement </th>
					    				<th> Quantity </th>
					    				<th> Block Name </th>				    				
					    				<th> Returns</th>
									</tr>
		
								</thead>
								<tbody>
				                 <c:forEach items="${prodList}" var="returns">
									<tr>	
					    				<td>${returns.date} <input type="hidden" id="chk2" class="validateIssueReturnFileds" name="indentEntry" value="${returns.strIndententrtDetailsId}">  </td>
					                   	<td>${returns.issueSlipNumber}</td>
										<td>${returns.contractorName}</td>
										<td>${returns.requesterName}</td>
										<td>${returns.strProductName}</td>
										<td>${returns.strSubProductName}</td>
										<td>${returns.strChildProductName}</td>
										<td>${returns.strMesurmentName}</td>
										<td>${returns.strQuantity}</td>
										<td>${returns.blockName}</td>								
										<td><input type="text" class="form-control" name="RETURNS" id="currentQty${returns.strIndententrtDetailsId}"  onkeypress='return isNumberCheck(this, event)' autocomplete="off"><input type="hidden" name="quantity" id="actualQty${returns.strIndententrtDetailsId}" value="${returns.strQuantity}"/>	</td>
		                             </tr>
							    </c:forEach>
							</tbody> 
				         </table>
					</div>
			         <div id="submitbutton" class="col-md-12 text-center center-block">
					    <input type="submit" value="Submit" id="Submit" onclick="return validatePageFileds()" class="btn btn-warning btn-bottom Mrgtop10" name="Submit">
					</div>
		         <%}%>
                </form>
             </div> 
		  </div>
		</div>


	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
    <script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
	<script>
	function validatePageFileds() {
		debugger;
		var flag=true;
		var isDataEnterd=0;
		$(".validateIssueReturnFileds").each(function () {

	     	debugger; 
			var id=$(this).val();
	     	var actualQty=parseFloat($("#actualQty"+id).val()==""?0:$("#actualQty"+id).val());
	     	var currentQty=parseFloat($("#currentQty"+id).val()==""?0:$("#currentQty"+id).val());
	     	if(currentQty>actualQty&&actualQty>=0){
	     		$("#currentQty"+id).focus();
	     		alert("Return quantity should not be greater than recived quantity");
	     		flag=false;
	     		return false;
	     	}
	     	if(currentQty>0){
	     		isDataEnterd++;
	     	}
		   });
		
		if(flag==false){
			return false
		} 
		if(isDataEnterd==0){
			alert("Please return at least one product.");
			return false;
		}
		var canISubmit = window.confirm("Do you want to Submit?");	     
		  if(canISubmit == false) { 
		        return false;
		  }
		  
		 // document.getElementById("Submit").disabled = true;   
		  return true;
	}
	
	
		$(document).ready( function() { 
			    $(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					
					
					
					//this code for to active the side menu 
					  var referrer="doIndentReturns.spring";
						 $SIDEBAR_MENU.find('a').filter(function () {
					        var urlArray=this.href.split( '/' );
					        for(var i=0;i<urlArray.length;i++){
						     	 if(urlArray[i]==referrer) {
						     		 return this.href;
						     	 }
					        }
						 }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
					
					
				});

	</script>

</body>
</html>
