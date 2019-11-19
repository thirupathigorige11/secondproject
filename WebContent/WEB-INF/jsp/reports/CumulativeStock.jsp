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
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">
<jsp:include page="../CacheClear.jsp" />  


<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link type="text/css"href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css"rel="stylesheet" />
<!-- link href="js/jquery-ui.css" rel="stylesheet"-->
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">

<style>
@media only screen and (min-width:320px) and (max-width:374px){.custom-combobox-input {width:88% !important;}}
@media only screen and (min-width:375px) and (max-width:424px){.custom-combobox-input {width:90% !important;}}
@media only screen and (min-width:425px) and (max-width:767px){.custom-combobox-input {width:92% !important;}}
@media only screen and (min-width:768px) and (max-width:1023px){.custom-combobox-input {width:96% !important;}}
.mrg-bottom{margin-bottom:15px;}
.custom-combobox{width:100%;}
.custom-combobox-input {
    margin: 0;
    padding: 0.3em;
    width: 83%;
    height:34px;
}
.border-inwards-box {text-align:left !important;}
.ui-widget {
    font-family: inherit !important;
    font-size: 14px !important;
    font-weight: bold !important;
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
.form-control{height:34px;}
</style>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

</head>

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
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Reports</a></li>
						<li class="breadcrumb-item active">Cumulative Stock</li>
					</ol>
				</div>
				<div class="loader-sumadhura" style="display: none;z-index:999;">
						<div class="lds-facebook"> 
						<div></div> <div></div><div></div>	<div></div>	<div></div>	<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>
				
				<form action="ViewCumulativeStockList.spring" onsubmit="return formValidator()" class="form-horizontal horizantal-form" method="post">
				 <div class="border-inwards-box">
					 <div class="col-md-4">
					   <div class="form-group">
						   <label for="date" class="col-md-6 commlbl">From Date :</label>						
						   <div class="col-md-6 input-group">
						     <input type="text"  class="form-control readonly-color commlbl" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off" readonly>
						     <label class="input-group-addon btn input-group-addon-border" for="ReqDateId1"><span class="fa fa-calendar"></span></label>
						   </div>
  	                   </div>
					 </div>
			  	     <div class="col-md-4">
			  	        <div class="form-group">            
						       <label for="todate" class="col-md-6 commlbl">To Date :</label>
							   <div class="col-md-6 input-group">
							      <input type="text" id="ReqDateId2" class="form-control readonly-color commlbl"   name="toDate" value="${toDate}" autocomplete="off" readonly>
							      <label class="input-group-addon btn input-group-addon-border" for="ReqDateId2"><span class="fa fa-calendar"></span></label>
							   </div>
			  	       </div>
			  	    </div>
			  	   
			  	    <div class="col-md-4">			       
			       
										<%
											//session = request.getSession(true);
																			String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
																			log("reposrtd/cumulative Stock");
																			log(request.getAttribute("totalProductsList")==null?"nullcoming ":request.getAttribute("totalProductsList").toString());
																			if (strSearchType.equals("ADMIN")) {
										%>
								<div class="form-group">
										<div class="ui-widget"> <label class="col-md-6 commlbl">Site :</label></div>
										<div class="col-md-6 text-left">
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
	
										   </select> 
										</div>
								</div><%
								 	}
								 %>
							    </div>
							    <div class="clearfix"></div>
							    <div class="col-md-4">
								<div class="form-group">
								<label class="control-label commlbl col-xs-12 col-md-6">Product Name:  </label>
								<div class="col-xs-12 col-md-6">
															<%
															
													List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request.getAttribute("totalProductsList");
												 if (totalProductList.size() > 0) {
																										String strProductName = "";
																										String strProductId = "";
												%> <select id="combobox_Product" name="combobox_Product">
													<option value=""></option>
													<%
														for (Map productList : totalProductList) {
																														strProductId = productList.get("PRODUCT_ID") == null ? ""
																																: productList.get("PRODUCT_ID").toString();
																														strProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
													%>
		
													<option value="<%=strProductId + "@@" + strProductName%>"><%=strProductName%></option>
													<%
														}
													}else{
														%>
														
													<select id="combobox_Product" name="combobox_Product">
														<option value=""></option>
													</select>
														<%
													}
													%>
											</select>
								</div>
							</div>
						</div>
						
						<div class="col-md-4"> 
							<div class="form-group">
								<label class="control-label commlbl col-xs-12 col-md-6">Sub Product Name:</label>
								<div class="col-xs-12 col-md-6">
									<select id="combobox_SubProduct" name="combobox_SubProduct">
										<option value=""></option>
									</select>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label commlbl col-md-6">Child Product Name:</label>
								<div class="col-xs-12 col-md-6">
									<select id="combobox_ChildProduct"class="form-control" name="combobox_ChildProduct">
										<option value=""></option>
									</select>
								</div>
							</div>
						</div> 
						<div class="col-md-12 col-sm-12 col-xs-12 text-center center-block">
						  <button type="submit" value="Submit" class="btnname btn btn-warning Mrgtop10" >Submit</button>
						</div>
					</div>
				</form>
			</div>
           <!-- /page content -->
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<script src="js/sidebarresp.js"></script>
	<!-- Custom Theme Scripts -->




	<!--  script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script-->
	<script src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/ComboBox_IndentAvailabulity.js"></script>
	<script src="js/custom.js"></script>
	

	<script>
		$(document).ready(
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});

				});

		

		$(function() {
			  $("#ReqDateId1").datepicker({
				  dateFormat: 'dd-M-y',
				  changeMonth: true,
			      changeYear: true,
			      maxDate: new Date(),
				  onSelect: function(selected) {
	     	        $("#ReqDateId2").datepicker("option","minDate", selected)
	     	        }
			  });
			  $("#ReqDateId2").datepicker({
				  dateFormat: 'dd-M-y',
				  changeMonth: true,
			      changeYear: true,
			      maxDate: new Date(),
				  onSelect: function(selected) {
		            	$("#ReqDateId1").datepicker("option","maxDate", selected)
		            	        }
			  });
			  return false;
		});
		function formValidator() {
			
			 var fromDate = document.getElementById('ReqDateId1').value;
			 var toDate = document.getElementById('ReqDateId2').value;
			if (fromDate != '' && fromDate!= null && toDate == '') {
				alert("please enter Todate ");
				return false;

			} 
			if (toDate != '' && toDate!=null  && fromDate == '') {
				alert("please enter from Date");
				return false;

			}
			//done by aniket
			try{
				var siteId=document.getElementById("dropdown_SiteId").value;
				if(siteId==""||siteId==null){
					alert("Please select the Site !");
					return false;	
				}
			}catch(err){
					console.log(err);
				}
			

			var product = document.getElementById('combobox_Product').value;
			if (product == '' || product == null || product == '@@') {
				alert("please enter product name");
				return false;

			}/*  else {
				var subproduct = document.getElementById('combobox_SubProduct').value;
				if (subproduct == '' || subproduct == null
						|| subproduct == '@@') {
					alert("please enter subproduct name");
					return false;
				}
				var childproduct = document
						.getElementById('combobox_ChildProduct').value;
				if ((childproduct == "" || childproduct == null || childproduct == '@@')) {
					alert("please enter childproduct name");
					return false;
				}

				var siteid = document.getElementById('dropdown_SiteId').value;
				if ((siteid == "" || siteid == null || siteid == '@@')) {
					alert("please enter site id");
					return false;

				}
			} */
		}
	</script>
</body>
</html>
