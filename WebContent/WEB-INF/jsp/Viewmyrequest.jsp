<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="CacheClear.jsp" />  
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">

<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="js/inventory.css"/>
<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />

<!-- link href="js/jquery-ui.css" rel="stylesheet"-->
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
</head>

<style>
table.dataTable {border-collapse:collapse !important;}
.custom-combobox {
	position: relative;
	display: inline-block;
}
.form-control{
height:34px;
}
.custom-combobox-toggle {
	position: absolute;
	top: 0;
	bottom: 0;
	margin-left: -1px;
	padding: 0;
	/* support: IE7 */
	*height: 1.7em;
	*top: 0.1em;
}

.custom-combobox-input {
	margin: 0;
	padding: 0.3em;
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
.container1{
display:none;
}
.media-style{
width:39% !important;

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

#WithoutPricingData{
	margin-top: 31px;
   
}
.form-content{
margin-top: 30px;
}
.bottom-class{
margin-top: 22px;
}

.fields-space-with{
    margin-top: 26px;
    
    }
 .fields-space{
     margin-right: 25px;
 
 }
 .fields1-space{
 	 margin-left: 6px;
 }
 .withoutPricing-class{
 	margin-bottom: 10px;
 }
 .formShow{
    border: 1px solid #e4e2e2;
    margin-top: 35px;
/*     width: 1052px; */
    background: #fff;
    /* overflow: scroll;
    background: #ffffff; */
    }
  
    
    .DCNumber{
    	color: black;
    	font-weight: bold;
    }
 
    hr { 
    display: block;
    margin-top: 0.5em;
    margin-bottom: 0.5em;
    margin-left: auto;
    margin-right: auto;
    border-style: inset;
    border-width: 2px;
} 
.full-width{
	width: 100%;
}
.icons-bg{
    padding: 3px 10px;
}

.pricing-box{
	background: rgba(33, 32, 31, 0.2);
    display: flow-root;
    padding: 10px;
    border: solid 1px #d8d3d3;
	
}
.custom-combobox {
	position: relative;
	display: inline-block;
}

.custom-combobox-toggle {
	position: absolute;
	top: 0;
	bottom: 0;
	margin-left: -1px;
	padding: 0;
	/* support: IE7 */
	*height: 1.7em;
	*top: 0.1em;
}

.custom-combobox-input {
	margin: 0;
	padding: 0.3em;
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
</style>


<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	</style>
</noscript>
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
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">View My Request</li>
					</ol>
				</div>
				<!-- loader -->
					<div class="loader-sumadhura" style="display: none;z-index:999;">
						<div class="lds-facebook">
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>
					<!-- loader -->
			<%-- 	<form name="myForm"  class="form-inline" action="ViewMyRequest.spring">  --%>
				<div class="col-md-12 text-center">
				  <label class="radio-inline">
				      <input type="radio" id="allindents" name="optradio"><span style="font-size: 18px">All Indents</span>
				    </label>
				     <label class="radio-inline">
				      <input type="radio" id="indentwise" name="optradio"><span style="font-size: 18px">Site wise Indents</span>
				    </label>
				</div>
   
  
				
<!-- 	*************************** Indent wise************************		 -->	
				
				 	<div class="">
				 	 <div class="container border-indents-box" id="form-indent-box">
				 		<form id="indentwiseForm" class="form-inline withdata">
						  	<div class="form-group">
						  	  <label class="DCNumber indent-label">Site Wise Indent Number : </label>
						  	  <input type="text" class="form-control form-control-width" name="siteWiseIndentNo" id="siteWiseIndentNo" autocomplete="off" />
						  	</div>
							<div class="form-group">
							  <label class="control-label indent-label" style="">Site:</label>
						       <select name="siteId" id="siteId" class="form-control form-control-width">
									<option value="">--Select--</option>
									<c:forEach items="${siteDetails}" var="siteDetails">
										<c:if test="${strSiteId eq  siteDetails.key}">
											<option value="${siteDetails.key}" selected="selected">${siteDetails.value}</option>
										</c:if>
										<c:if test="${strSiteId ne  siteDetails.key}">
											<option value="${siteDetails.key}">${siteDetails.value}</option>
										</c:if>
									
									</c:forEach>
							   </select>
							</div>
				    		<button class="btn btn-warning" type="button" onclick="submitFun()">Submit</button> 
				 		</form> 
				 	</div>
				 	</div>		
				 	<div class="clearfix"></div>
					<div  id="tblnotificationIndentWise1"  class="table-responsive"  style="display: none;" > 
							<table id="tblnotificationIndentWise"	class="table table-new" cellspacing="0" >
								<thead style="color: black;">
									<tr>
										<th>Created Date</th>		
					    				<th>Indent Number</th>
					    				<th>Requested Site Id</th>
					    			</tr>
								</thead>
									<tbody id="indentWiseData">
									<c:forEach items="${listofSiteWiseCentralIndents}" var="element">  
										<tr>
										    <td>${element.strCreateDate}</td> 	
											<td><a href="ViewIndentissuedDetails.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteName=${element.siteName}&siteId=${element.siteId}&url=${urlForActivateSubModule}" class="anchor-class">${element.siteWiseIndentNo}</a></td>
											<td>${element.siteName}</td>
										</tr>
									</c:forEach>
								</tbody>
								</table>
							</div>
				 	
	                <!-- ****************************** All Indent wise************************* -->
				
							<!-- <div class="container " id="Allindentwisedata" style="display:none"> -->
								<%-- <form class="form-horizontal" name="myForm" action="sendenquiry.spring" style="padding:15px;"> --%>
								<div class="clearfix"></div>
								<div id="tblnotificationAllIndent1" class="table-responsive"  style="display: none;" >
									  		<table id="tblnotificationAllIndent"	class="table table-new" cellspacing="0">
												<thead>
													<tr>
														<th>Created Date</th>		
									    				<th>Indent Number</th>
									    				<th>Requested Site Id</th>
									    			</tr>
												</thead>
												<tbody>
													<c:forEach items="${listofCentralIndents}" var="element">  
														<tr>
														    <td>${element.strCreateDate}</td> 	
															<td><a href="ViewIndentissuedDetails.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteName=${element.siteName}&siteId=${element.siteId}&url=${urlForActivateSubModule}" class="anchor-class">${element.siteWiseIndentNo}</a></td>
															<td>${element.siteName}</td>
														</tr>
													</c:forEach>
												</tbody>
												
													<tbody id="SiteWiseIndentsData">
													<c:forEach items="${listofSiteWiseCentralIndents}" var="element">  
														<tr>
														    <td>${element.strCreateDate}</td> 	
															<td><a href="ViewIndentissuedDetails.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteName=${element.siteName}&siteId=${element.siteId}" class="anchor-class">${element.siteWiseIndentNo}</a></td>
															<td>${element.siteName}</td>
														</tr>
													</c:forEach>
												</tbody>
												</table>
								</div>
					
						
					<!-- ****************************** product wise************************* -->
												
									
					<%-- </form> --%>
						<div>
					  <div align="center" style="color: green;">
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
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/ComboBox_IndentPOAvailabulity.js"></script>
		
		<script src="js/jquery.dataTables.min.js"></script>
		<script src="js/dataTables.bootstrap.min.js"></script>
		<script src="js/sidebar-resp.js"></script>
		
<script>


		$(document).ready( function() {
				$(".up_down").click( function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
			    	$('#tblnotificationAllIndent').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
			    	$('#tblnotificationIndentWise').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		
			    	$(window).load(function () {
			    	    if($("#allindents").prop("checked") == true){
			    	    	debugger;
							$("#Allindentwisedata").show();
							$("#indentwisedata").hide();
							$("#productwise").hide();
							$("#form-indent-box").hide();
							
							$("#AllIndentsData").show();
							$("#SiteWiseIndentsData").hide();
							
							
							$("#tblnotificationIndentWise1").hide();
						 	$("#tblnotificationAllIndent1").show();
			            }
			    	    
			    	    if($("#indentwise").prop("checked") == true){
			    	    	debugger;
							$("#indentwisedata").show();
							$("#Allindentwisedata").hide();
							$("#productwise").hide();
							$("#form-indent-box").show();
							$("#AllIndentsData").hide();
							$("#SiteWiseIndentsData").show();
							
							
						 	$("#tblnotificationAllIndent1").hide();
							
						 	var indentWiseData=$("#indentWiseData").text();
						 	//cheking the length of table body
						 	//based on that showing the table
							if(indentWiseData.length>50){
								$("#tblnotificationIndentWise1").show();	
							}
			    	    }
					});
		
		});

				
		
/* Method for button show and show function
						 */

				 	 $(document).ready(function(){
				 		   $("#form-indent-box").hide();
						     $("#producttwise").click(function(){
									$("#Allindentwisedata").hide();
									$("#indentwisedata").hide();
									$("#productwise").show();
							});
							
							$("#indentwise").click(function(){
								debugger;
								$("#indentwisedata").show();
								$("#Allindentwisedata").hide();
								$("#productwise").hide();
								$("#form-indent-box").show();
								$("#AllIndentsData").hide();
								$("#SiteWiseIndentsData").show();
								
								
							 	$("#tblnotificationAllIndent1").hide();
								
							 	var indentWiseData=$("#indentWiseData").text();
							 	//cheking the length of table body
							 	//based on that showing the table
								if(indentWiseData.length>50){
									$("#tblnotificationIndentWise1").show();	
								}
							});
							
						 	 
						 	 
							
							$("#allindents").click(function(){
								debugger;
								$("#Allindentwisedata").show();
								$("#indentwisedata").hide();
								$("#productwise").hide();
								$("#form-indent-box").hide();
								
								$("#AllIndentsData").show();
								$("#SiteWiseIndentsData").hide();
								
								
								$("#tblnotificationIndentWise1").hide();
							 	$("#tblnotificationAllIndent1").show();
							 	
							 	
								
							}); 
							 
							
							var showRequestData="${showRequestData}";
							debugger;

							if(showRequestData=="AllIndentsData"){
								/* $("#Allindentwisedata").show();
								$("#indentwisedata").hide();
								$("#productwise").hide();
								$("#form-indent-box").hide(); */
								//jQuery("#allindents").attr('checked', true);
								
							}
							if(showRequestData=="SiteWiseIndentsData"){
								$("#indentwisedata").show();
								$("#Allindentwisedata").hide();
								$("#productwise").hide();
								$("#form-indent-box").show();
								$("#AllIndentsData").hide();
								$("#SiteWiseIndentsData").show();
								
								$("#tblnotificationIndentWise1").show();
								jQuery("#indentwise").attr('checked', true);
								
							}
						 });
                       function submitFun(){
                    	   var siteName = $("#siteId").val();
                    	   if(siteName == ""){
                    		   alert("Please select site.");
                    		   $("#siteId").focus();
                    		   return false;
                    	   }
                    	   debugger;
                    	   $('.loader-sumadhura').show();
                    	   document.getElementById("indentwiseForm").action = "ViewMyRequest.spring";
                       	   document.getElementById("indentwiseForm").method = "GET";
                       	   document.getElementById("indentwiseForm").submit();
                       	  
                       }
                       $(".anchor-class").click(function(ev){
                   		if(ev.ctrlKey==false && ev.shiftKey==false){
                   			$(".loader-sumadhura").show();
                   		}
                   	});

</script>
</body>
</html>
