<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="CacheClear.jsp" /> 
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css"rel="stylesheet" />
<link href="css/style.css" rel="stylesheet">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<script>
	function myFunction() {
		/* alert(123); */
		var popup = document.getElementById("myPopup");

		alert(popup);
		popup.classList.toggle("show");
	}
</script>

</head>

<style>
table.dataTable {border-collapse:collapse !important;}
tr:nth-child(even) {
    background-color: transparent !important;
}
.text-left-button{float:left;}
.ui-widget {
    font-family: none !important;
    font-size:17px;
 }
.select-form-control{height: 34px;
    border: 1px solid #9e9696;
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
	font-size:17px;
	*height: 1.7em;
	*top: 0.1em;
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
    background: #fff;
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
.custom-combobox-input{
 color: #000000;
 height: 30px;
    font-size: 14px;
 font-weight: 400;
 line-height: 1.471;
 font-family: 'robotolight' !important;
} 

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
.ui-menu-item{
	font-size: 14px !important;
    font-weight: 400 !important;
}
.ui-corner-all{
	font-size: 14px !important;
	font-family: 'robotolight';
}
.ui-button{
  height: 30px !important;
    top: 1px !important;
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
	<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Pending Indents</li>
					</ol>
				</div>
				<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
						<!-- Loader -->	
				<center><font color="green" size="5"><c:out value="${requestScope['response']}"></c:out> </font></center> 
                <center><font color="red" size="5"><c:out value="${requestScope['response1']}"></c:out> </font></center>
				<div class="col-md-12">
				<form name="myForm" action="doGetInvoiceDetails.spring"> 
				
	 <form> 
	 <div class="col-md-12 border-indent">
	 <div class="col-md-offset-3 col-md-6">
	  <div class="col-md-4"><label class="radio-inline">
     <input type="radio" id="indentwise" name="optradio" class="Pendingradio_class"><span style="font-size: 18px">Project Wise</span>
    </label></div>
	<div class="col-md-4"><label class="radio-inline">
      <input type="radio" id="producttwise" name="optradio" class="Pendingradio_class"><span style="font-size: 18px">Product Wise</span>
    </label></div>
    <div class="col-md-4"><label class="radio-inline">
      <input type="radio" id="allindents" name="optradio" class="Pendingradio_class"><span style="font-size: 18px">All Indents</span>
    </label></div>
	 </div>
    </div>
    
  </form>
  <center><span><font color=red size=4 face="verdana">${responseMessage}</font></span></center>
	<div class="container-indents" id="indentwisedata" style="display:none;">
				 		<form id="indentwise" class="form-horizontal withdata" action="getAllIndentCreationDetails.spring" >
				  		 	<div class="col-md-12">
				  		 	  <div class="col-md-offset-3 col-md-6">
				  		 	    <div class="form-group">
						    <label class="control-label col-md-4" style="">Select Site:</label>
							 <div class="col-md-6">
							   <select name="site" id="site" class="form-control select-form-control">
									<option value="">Select</option>
									<c:forEach items="${siteDetails}" var="siteDetails">
										<option value="${siteDetails.key}">${siteDetails.value}</option>
									
									</c:forEach>
							</select>
							 </div>
						</div>
						
				  		 	  </div>
				  		 	<div class="col-md-3 text-left-button">
				  		 	 <button type="submit" value="Submit" onclick="return myFunction()" class="btn btn-warning " ><strong>Submit</strong></button>
				  		 	</div>
				    		
				    	 </div>
				    	</form>
	</div>		 	
				 	
				 	
				 	
				 	
	<!-- ****************************** All Indent wise************************* -->
				
		<div class="container " id="Allindentwisedata" style="display:none">
			<form class="form-horizontal" name="myForm" action="sendenquiry.spring">
	  		<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr> <!--  class="tblheaderall" -->
							<th>Created Date</th>
		    				<th>Site Wise Indent Number</th>
		    				<th>Indent Name</th>
		    				<th>Created Employee</th>    				
    				        <th>Project Name</th>    				
    				        <th>Required Date</th>    				
    				        <th>Purchase Dept Request Receive Date</th>
    				        
				            </tr>

						</thead>
						<tbody>
				<c:forEach items="${listofCentralIndents}" var="element">  
				<tr>
				    <td>${element.strCreateDate}</td>
					
					<td><a href="viewPurchaseIndent.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteName=${element.siteName}&siteId=${element.siteId}&creationDate=${element.strCreateDate}&url=getIndentCreationDetails.spring" style="text-decoration: underline;color: blue;" class="anchor-class">${element.siteWiseIndentNo}</a></td>
					
					<td>${element.indentName}</td>
					<td>${element.indentFrom}</td>
					
					<td>${element.siteName}</td>
					
				
					
					<td>${element.strRequiredDate}</td>
					
					<td>${element.purchaseDeptReceivedDate}</td>
				</tr>
				</c:forEach>
				</tbody>
				</table>


		</form>
				 	</div>
<!-- ****************************** product wise************************* -->
<div class="clearfix"></div>
			<div class="container-indents" id="productwise" style="display:none">
				<form id="productWiseTbleForm"><!-- "centralview.getIndentsProductWise.spring" --> 
					<!-- 	<div class="table-responsive"> -->
							<table class="text-center table prodt_tbl" id="tblnotification1">
							<tbody>
								<tr>
								<%
											Object obj = request.getAttribute("totalProductsList");
										if(obj!=null){System.out.println("Data coming");
											List<Map<String,Object>> totalProductList = (List<Map<String,Object>>)obj;
																							if (totalProductList.size() > 0) {
																								String strProductName = "";
																								String strProductId = "";
										%>
									<td class="text-left">
										<div class="ui-widget">
											<label class="commlbl">Product Name</label>
											</div>
									</td>
									<td>:</td>

									<td class="text-left">
									<div>
										 <select id="combobox_ProductSel" name="combobox_Product">
											<option value=""></option>
											 <%
												for (Map productList : totalProductList) {
																												strProductId = productList.get("PRODUCT_ID") == null ? ""
																														: productList.get("PRODUCT_ID").toString();
																												strProductName = productList.get("PRODUCT_NAME") == null ? "" : productList.get("PRODUCT_NAME").toString();
											%> 

											<option value="<%=strProductId + "@@" + strProductName%>"><%=strProductName%></option>
											 <% 
												}
											}
										}
										else{System.out.println("null coming");}
											 %> 
									</select>

										</div>

									</td>
									
								</tr>
								<tr>

									<td class="text-left">
										<div class="ui-widget">

											<label class="commlbl">Sub Product Name</label>
											</div>
									</td>
									<td>:</td>
									<td class="text-left">
									<div>
										<select id="combobox_SubProductSel" name="combobox_SubProduct">
												<option value=""></option>
										</select>

									</div></td>
									
								</tr>
								<tr>
									<td class="text-left">
										
											<div class="ui-widget">

												<label class="commlbl">Child Product Name</label>
												</div>
									</td>
									<td>:</td>
									<td class="text-left">
									<div>
									<select id="combobox_ChildProductSel"
										name="combobox_ChildProduct">
											<option value=""></option>

									</select>

										</div>
										</td>
									
								</tr>
								<tr>
									<td class="text-left">
										<%
											session = request.getSession(true);
																			String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
																			
																			if (strSearchType.equals("ADMIN")) {
										%>
										<div class="ui-widget">
											<label class="commlbl">Site</label>
											</div>
									</td>
									<td class="text-left">
									<div>
										<%
											List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
																				String strSiteId = "";
																				String strSiteName = "";
										%> <select id="dropdown_SiteId" name="dropdown_SiteId"
										class="custom-combobox form-control indentavailselect">
											<option value=""></option>

											<%
												for (Map siteList : totalSiteList) {
																						strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
																						strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
											%>

											<option value="<%=strSiteId%>"><%=strSiteName%></option>
											<%
												}
											%>

									</select> <%
 	}
 %>

										</div>
									<td>
									<td></td>
									<td></td>
								</tr>

								<tr>
								  <td></td>
									<td colspan="3" class="text-left">
										<button type="button" onclick="productWiseSubmit()" class="btnname btn btn-warning">Submit</button>
									</td>
								</tr>
                                  </tbody>
							</table>
					</form>
			</div>	
				
				
</form>
</div>					 	
     
			</div>
	</div>
</div>
	
	
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/ComboBox_IndentPOAvailabulity.js"></script>
		
<script>
$(".anchor-class").click(function(ev){
	if(ev.ctrlKey==false && ev.shiftKey==false){
		$(".loader-sumadhura").show();
	}
});
debugger;

	if($("#indentwise").prop("checked") == true){
		$("#indentwisedata").show();
		$("#Allindentwisedata").hide();
		$("#productwise").hide();
	}
	else if($("#producttwise").prop("checked") == true){
		$("#Allindentwisedata").hide();
		$("#indentwisedata").hide();
		$("#productwise").show();
	}
	else if($("#allindents").prop("checked") == true){
		$("#Allindentwisedata").show();
		$("#indentwisedata").hide();
		$("#productwise").hide();
    }
	


				 	 $(document).ready(function(){
				 		 
				 		event.preventDefault();
							$("#producttwise").click(function(){
								$("#Allindentwisedata").hide();
								$("#indentwisedata").hide();
								$("#productwise").show();
								
								
							});
							$("#indentwise").click(function(){
								$("#indentwisedata").show();
								$("#Allindentwisedata").hide();
								$("#productwise").hide();
							});
							$("#allindents").click(function(){
								$("#Allindentwisedata").show();
								$("#indentwisedata").hide();
								$("#productwise").hide();
							   //var tableId = $('#tblnotification').DataTable({"aLengthMenu":[10, 25, 50, 100,  "All"]});
							}); 
							 
						 });	 
				 	$(document).ready(
							function() {
								$(".up_down").click( function() {
											$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
											$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
										});
								var tableId =  $('#tblnotification').DataTable({"aLengthMenu":[10, 25, 50, 100,  "All"]})
								tableId.destroy();
								debugger;
								
								var pageURl=document.URL;
							/* 	var pagename=pageURl.split("?")[0].split('/'); */
								var pagename1=pageURl.split("?")[0].split('/')[4];
							     var showPrevPaginationNo=pageURl.split("showPrevPaginationNo=")[1];
							   //  alert("showPrevPaginationNo "+showPrevPaginationNo);
							     if(showPrevPaginationNo == 'true'){
							              
							    	 if(pagename1=="getIndentCreationDetails.spring"){
							    		 var n=0;
							    		 $(".Pendingradio_class").each(function(){
							    			 if($(this).prop("checked") == true){
							    				n++; 
							    			 }
							    		 });
							    		 if(n==0){
							    			 
							    			 $("#allindents").attr('checked', 'checked');
								    		 $("#Allindentwisedata").show();
								    		 //$('#tblnotification').DataTable(); 
								    		var tableId = $('#tblnotification').DataTable({"stateSave": true ,"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
							    		 }							    	
							    	 }else{
							    		
							    		 var tableId = $('#tblnotification').DataTable({"stateSave": true ,"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
						                  return true;
							    	 }      
							     }else{
							    	
							    	 var tableId = $('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
							     }
							});

					
					
					//$('#tblnotification').stacktable({myClass:'stacktable small-only'});

					function myFunction() {
						var submitVal = document.getElementById('site').value;
						if (submitVal == '' || submitVal == null) {
							alert("Please Select Site!");
							return false;
						}
						$(".loader-sumadhura").show();
					}
					
					function productWiseSubmit(){					
						var product=$("#combobox_Product").val();
						var subproduct=$("#combobox_SubProduct").val();
						var childproduct=$("#combobox_ChildProduct").val();
						if(product=="" && subproduct=="" && childproduct==""){
							alert("Atleast one field is required.");
							return false;
						}
						
						$(".loader-sumadhura").show();
						document.getElementById("productWiseTbleForm").action = "getIndentsProductWise.spring";
						document.getElementById("productWiseTbleForm").method = "POST";
						document.getElementById("productWiseTbleForm").submit();
					}
</script>
</body>
</html>
