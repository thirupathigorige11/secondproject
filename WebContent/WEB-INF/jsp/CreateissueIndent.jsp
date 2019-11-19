<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import = "java.util.ResourceBundle" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<html>
<head>
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		 <script src="js/Reject.js" type="text/javascript"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<jsp:include page="CacheClear.jsp" />  
		<!-- Meta, title, CSS, favicons, etc. -->
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
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		 <title>Sumadhura-IMS</title>
        <link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">




</head>

<body class="nav-md">
	
<div class="container body" id="mainDivId">
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
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Home</a></li>
								<li class="breadcrumb-item active">View My Request Details</li>
							</ol>
						</div>
					
					
				
 <div class="">
		<form class="form-horizontal" name="myform" id="CreateCentralIndentFormId" action="sendissued.spring" method="POST" style="padding:15px;" >
	 <c:forEach items="${IndentDtls}" var="element">
			  <div class="border-indent">
			   <div class="col-md-4">
			    <div class="form-group"> 
					<label class="control-label col-md-6">Site Wise Indent No :</label>
					<div class="col-md-6" >
					<input name="indentNumber" id="indentNumberId" type="hidden" readonly="true" class="form-control" value="${element.indentNumber}"/>
					<input name="siteWiseIndentNo" id="siteWiseIndentNo"  readonly="true" class="form-control" value="${element.siteWiseIndentNo}"/>
					</div>
					</div>
			   </div>
			 	 <div class="col-md-4">
			 	  <div class="form-group"> 
					<label class="control-label col-md-6">Send To :</label>
					<div class="col-md-6" >
						<input name="siteName" id="siteNameId"  readonly="true" class="form-control" value="${element.siteName}"/>
					    <input type= "hidden" name="Site" id="Site"  class="form-control" value="${element.siteId}$${element.siteName}"/>
					    <input type= "hidden" name="issueType" id="issueType"  class="form-control" value="NonReturnable"}/>
					    <input type="hidden" name="urlForActivateSubModule" value="${urlForActivateSubModule}">
					</div>
					</div>
			 	 </div>
				<div class="col-md-4">
				 <div class="form-group"> 
					<label class="control-label col-md-6">Req No :</label>
					<div class="col-md-6" >
						<input name="ReqId" id="ReqId"  readonly="true" class="form-control" value=${IndentEntrySeqNo}>
					</div>

					</div>
				</div> 
					
		   
		   <div class="col-md-4">
		     <div class="form-group"> 
		   <label class="control-label col-md-6"> Req Date :</label>
		   <div class="col-md-6" >
						<input type= "text" name="ReqDate" id="ReqDate"  class="form-control" value=${todayDate} readonly>
			</div>
			</div>
		   </div>
			<div class="col-md-4">
			 <div class="form-group"> 
				  <label class="control-label col-md-6">Vehicle No :</label>
		   <div class="col-md-6" >
							<input name="vehicleNo" id="vehicleNo"   class="form-control" autocomplete="off"/>
			</div>
			 
		   </div>
			</div>
		  </div>
		   </c:forEach>
</div>
	  		<input type="hidden" name="numbeOfRowsToBeProcessed"   value="${requestScope['numberOfRows']}" />
<!-- *************** Table 01 grid***************** -->
<div class="">
				<div class="col-md-12"> <!-- class="container1" -->
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>
							<tr>
							<!--  	<th>Actions</th>-->
								<th>Product Name</th>
								<th>Sub Product Name</th>
								<th>Child Product Name</th>
								<th>Units of Measurement</th>
								<th>Request Quantity</th>
								<th>Pending Quantity</th>
								<th>Available Quantity</th>
								<th>Issue Quantity</th>
								<!-- <th>Site Quantity</th>-->
								
							</tr>
						</thead>
						<tbody>
						
							<c:forEach items="${IndentDetails}" var="element"> 
							<div style="display: none;">
							
							
							<%-- this is for indent creation and approval system 2nd module start ---%>
							
					   			<input type="hidden" id="productId${element.strSerialNumber}" value="${element.productId1}" name="product${element.strSerialNumber}"/>
								<input type="hidden" id="subProductId${element.strSerialNumber}" value="${element.subProductId1}" name="subProduct${element.strSerialNumber}" />
								<input type="hidden" id="childProductId${element.strSerialNumber}" value="${element.childProductId1}" name="childProduct${element.strSerialNumber}"/>
								<input type="hidden" id="unitsOfMeasurementId${element.strSerialNumber}" value="${element.unitsOfMeasurementId1}" name="unitsOfMeasurement${element.strSerialNumber}"/>
					<input type="hidden" id="wholeRowProdIds${element.strSerialNumber}" value="<c:out value='${element.product1}'/>@@<c:out value='${element.subProduct1}'/>@@<c:out value='${element.childProduct1}'/>@@<c:out value='${element.unitsOfMeasurement1}'/>@@${element.requiredQuantity1}@@${element.pendingQuantity}@@${element.indentAvailability}" name="wholeRowProdIds${element.strSerialNumber}"/>
								<input type="hidden" id="requiredQuantityId${element.strSerialNumber}" value="${element.requiredQuantity1}" name="requiredQuantity${element.strSerialNumber}"/>
								<input type="hidden" id="indentProcessId${element.strSerialNumber}" value="${element.indentProcessId}" name="indentProcessId${element.strSerialNumber}"/>
							    <input type="hidden" id="pendingQuantityId${element.strSerialNumber}" value="${element.pendingQuantity}" name="pendingQuantity${element.strSerialNumber}"/>
							    <input type="hidden" id="indentAvailabilityId${element.strSerialNumber}" value="${element.indentAvailability}" name="indentAvailability${element.strSerialNumber}"/>
							     <input type="hidden" id="centralIndentReqDtlsId${element.strSerialNumber}" value="${element.centralIndentReqDtlsId}" name="centralIndentReqDtlsId${element.strSerialNumber}"/>
							    
							  	<%-- this is for indent creation and approval system 2nd module end ---%>  
							
								<%-- this is for issue to other site code using 1st module start ---%>
							
							    <input type="hidden" id="Product${element.strSerialNumber}" value="${element.productId1}$${element.product1}" name="Product${element.strSerialNumber}"/>
								<input type="hidden" id="SubProduct${element.strSerialNumber}" value="${element.subProductId1}$${element.subProduct1}" name="SubProduct${element.strSerialNumber}" />
								<input type="hidden" id="ChildProduct${element.strSerialNumber}" value="${element.childProductId1}$${element.childProduct1}" name="ChildProduct${element.strSerialNumber}"/>
								<input type="hidden" id="UnitsOfMeasurement${element.strSerialNumber}" value="${element.unitsOfMeasurementId1}$${element.unitsOfMeasurement1}" name="UnitsOfMeasurement${element.strSerialNumber}"/>
							 
							
							
							
							    <%-- this is for issue to other site code using 1st module end ---%>
							
							</div>
					   		<tr>
									
								<td class="tiptext">${element.product1}</td>
								<td>${element.subProduct1}</td>
								<td>${element.childProduct1}</td>
								<td>${element.unitsOfMeasurement1}</td>
								<td>${element.requiredQuantity1}</td>	
								<td>${element.pendingQuantity}</td>	
			                     <td>${element.indentAvailability}</td>	
								<td>   <input type="text" class="form-control issuedquantity" id="Quantity${element.strSerialNumber}" name="Quantity${element.strSerialNumber}" value="" onkeypress="return isNumberCheck(this, event)" autocomplete="off"/>       </td>
							<!-- 	<td>${element.issuedquantity}</td> -->
						<!-- 	<td>100</td>	
								<td id="sttle-id1"><a href="" style="color: blue" >SETTEL</a></td>	 -->
							</tr>
							</c:forEach>
						</tbody>
		             </table>
		         </div>
                 </div>
				<div class="col-md-12 text-center center-block">				
					<input name="submit" type="submit" class="btn btn-warning" value="Send" id="saveBtnId" onclick="return validateForm();">				
			        <input type="button" class="btn btn-warning" value="Reject" id="saveBtnId" onclick="RejectQuantity()" style="display:none;" >
			       <input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
				</div>
		 </form> 
	</div>
	<!-- /page content -->   

		
</div>
</div>

</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
  <script>
  function validateForm() {
		debugger;
		/* var vehicleNo = document.getElementById("vehicleNo").value;	
		if(vehicleNo == "" || vehicleNo == null || vehicleNo == '') {
			alert("Please enter vehicle no");
			document.getElementById("vehicleNo").focus();
			return false;
		} */
		
    	/* var vehicleNumberid=vehicleNo;
		var regex=/^([A-Z]{2,3})-(\d{2,4})\w|([A-Z]{2,3})-\d{2}-[A-Z]{1,2}-\d{1,4}\w/;
		if (!regex.test(vehicleNumberid)) {
			alert("Please enter valid vehicle number.");
			document.getElementById("vehicleNo").focus();
			return false;
		} */
		var checkVal=0;
		$(".issuedquantity").each(function(){
			
			var currentVal=$(this).val();
			if(checkVal==0)
			checkVal=currentVal;
			var id=$(this).attr("id");
			debugger;
		});
		if(checkVal==0||checkVal=="0"||checkVal==""||checkVal=="0.0"){
			alert("Please enter at least one product the quantity.");
			return false;
		} 
		
		  var canISubmit = window.confirm("Do you want to Submit?");	     
		  if(canISubmit == false) { 
		        return false;
		  }
		
	}
		
			$(document).ready(function() {
				$('body').bind('copy paste',function(e) {
				    e.preventDefault(); return false; 
				});
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			
			
			$("#sttle-id1").click(function(){
				var tr    = $(this).closest('tr');
				var clone = tr.clone().insertAfter("#tblnotification" ).after('</>');
				console.log(clone);
				});
					$('#tblnotification tr').closest('tr').find('.issuedquantity').keyup(function(){
							debugger;
								var issuedQuantity1 = $(this).val();
								/* if(issuedQuantity1.length==0){
									return false;
								} */
								if(/(?<=\s|^)\d+(?=\s|$)/.test(issuedQuantity1)||/^(?:0|[1-9][0-9]*)\.[0-9]+$/.test(issuedQuantity1)){
									var issuedQuantity = parseFloat(issuedQuantity1);
									/* if(issuedQuantity==0){
										alert("Please enter valid quantity.");
										$("input[name=submit]").attr("disabled", "disabled");
										return false;
									} */
									var pendingQuantity = parseFloat($(this).closest('tr').find("td").eq(5).html());
									var availableQuantity = parseFloat($(this).closest('tr').find("td").eq(6).html());
									//alert(issuedQuantity+"<="+pendingQuantity+"&&"+issuedQuantity+"<="+availableQuantity);
										if(issuedQuantity<=pendingQuantity&&issuedQuantity<=availableQuantity)
											{}
										else{
											alert("Number Should not be greater than Pending Quantity & Available Quantity.");
											$(this).val("0")
											}
								}
								else{
										/* alert("Please Enter a Number");
										$(this).val("0"); */
								}
				
								$("input[name=submit]").removeAttr("disabled");
								/* $('#tblnotification > tbody  > tr').each(function() {
									var issuedQuantity1 = $(this).find('.issuedquantity').val();
									//alert(issuedQuantity1);
									debugger;
									if(/(?<=\s|^)\d+(?=\s|$)/.test(issuedQuantity1)||/^(?:0|[1-9][0-9]*)\.[0-9]+$/.test(issuedQuantity1)){
										var issuedQuantity = parseFloat(issuedQuantity1);
										var pendingQuantity = parseFloat($(this).closest('tr').find("td").eq(5).html());
										var availableQuantity = parseFloat($(this).closest('tr').find("td").eq(6).html());
										//alert(issuedQuantity+"<="+pendingQuantity+"&&"+issuedQuantity+"<="+availableQuantity);
											if(issuedQuantity<=pendingQuantity&&issuedQuantity<=availableQuantity)
												{ }
											else{
												$("input[name=submit]").attr("disabled", "disabled");
												return false;
												}
									}
									else{
										$("input[name=submit]").attr("disabled", "disabled");
										return false;
									}
								}); */
				
				
								});       	
			
					 //this code for to active the side menu 
					var referrer="${urlForActivateSubModule}";
					$SIDEBAR_MENU.find('a').filter(function () {
				           var urlArray=this.href.split( '/' );
				           for(var i=0;i<urlArray.length;i++){
				        	 if(urlArray[i]==referrer) {
				        		 return this.href;
				        	 }
				           }
				    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');		
			   /*No Backend Functionality For Reject */ 
            function RejectQuantity(){
				   debugger;
				   alert("Rejected Successfully!")
			   }
		</script> 
</body>
</html>
