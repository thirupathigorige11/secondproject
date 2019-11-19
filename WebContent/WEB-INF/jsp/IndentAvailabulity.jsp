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
<jsp:include page="CacheClear.jsp" />  

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link type="text/css"
	href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css"
	rel="stylesheet" />
<!-- link href="js/jquery-ui.css" rel="stylesheet"-->
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.custom-combobox {position: relative;display: inline-block;}
.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;/* support: IE7 */*height: 1.7em;*top: 0.1em;}
.custom-combobox-input {margin: 0;padding: 0.3em;}
#dropdown {width: 88%;padding: 3px;border-color: rgb(169, 169, 169);}
.commlbl{text-align:left !important;}
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
						<li class="breadcrumb-item"><a href="#">History</a></li>
						<li class="breadcrumb-item active">View Product Quantity</li>
					</ol>
				</div>
				<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
						<!-- LOader -->	
				<form action="centralview.spring" onsubmit="return formValidator()" class="form-horizontal horizantal-form col-xs-12 col-md-12 border-inwards-box" method="post">
					<div class=" col-xs-12 col-md-offset-3 col-md-6"> <!-- prodt_tbl -->					
						<div class="form-group">
							<label class="control-label commlbl col-xs-12 col-md-6">Product Name :</label>
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
													strProductId = productList.get("PRODUCT_ID") == null ? "": productList.get("PRODUCT_ID").toString();
													strProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
												%>
	
												<option value="<%=strProductId + "@@" + strProductName%>"><%=strProductName%></option>
												<%
													}
																											}
												%>
										</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label commlbl col-xs-12 col-md-6" >Sub Product Name:</label>
							<div class="col-xs-12 col-md-6">
								<select id="combobox_SubProduct" name="combobox_SubProduct">
									<option value=""></option>
								</select>
							</div>
						</div>						
						<div class="form-group">
							<label class="control-label commlbl col-md-6 col-xs-12" >Child Product Name:</label>
							<div class="col-xs-12 col-md-6">
									<select id="combobox_ChildProduct" name="combobox_ChildProduct">
										<option value=""></option>
									</select>
							</div>
						</div>						
						<div class="col-md-12 text-center center-block">
							<button type="submit" value="Submit" class="btnname btn btn-warning Mrgtop10">Submit</button>
						</div>
					</div>
					<div class="text-left">
							<%
								session = request.getSession(true);
																String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
																
																if (strSearchType.equals("ADMIN")) {
							%>
							<div class="ui-widget">
								<label class="commlbl">Site :</label>
						  </div>
						  <div class="text-left">
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
	<!-- Custom Theme Scripts -->

	<!--  script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script-->
	<script src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/ComboBox_IndentAvailabulity.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/sidebar-resp.js"></script>

	<script>
		$(document).ready(
				function() {$(".up_down").click(function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				});

		function formValidator() {

			var product = document.getElementById('combobox_Product').value;
			if (product == '' || product == null || product == '@@') {
				alert("please enter product name");
				return false;

			}
			$(".loader-sumadhura").show();
			/*  else {
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
