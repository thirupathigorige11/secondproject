<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">


<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link type="text/css"
	href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css"
	rel="stylesheet" />

<link href="css/style.css" rel="stylesheet">
<jsp:include page="CacheClear.jsp" />  
<style>
.custom-combobox {
	position: relative;
	display: inline-block;
}
.success,.error {
	text-align: center;
	font-size: 14px;
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
						<li class="breadcrumb-item active">Closing Balance By Product</li>
					</ol>
				</div>


				<div>
					<form action="getClosingDetailsBasedOnProduct.spring" onsubmit="return formValidator()" method="post" class="form-horizontal"
					style="background-color:#ccc;">

					<label class="success"><c:out value="${requestScope['errMessage']}"></c:out> </label> 
						<div class="form-group" style="margin-left:20px;">

							<label class="control-label col-xs-12 col-sm-2">From Date :</label>
							<div class="col-xs-12 col-sm-3">
								<input id="ReqDateId1" type="text" name="fromDate" class="form-control" style="width: 224px;" autocomplete="off">
							</div>
							<label class="control-label col-xs-12 col-sm-2">To Date :</label>
							<div class="col-xs-12 col-sm-3">
								<input id="ReqDateId2" type="text" name="toDate"  class="form-control"  style="width: 224px;" autocomplete="off">
							</div>
						</div>

						<div class="form-group" style="margin-left:20px;">

							<label class="control-label col-xs-12 col-sm-2">Product Name: </label>
							<div class="col-xs-12 col-sm-3">
								<%
									List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request.getAttribute("totalProductsList");
									if (totalProductList.size() > 0) {
										String strProductName = "";
								        String strProductId = "";
								%>
								<select id="combobox_Product" name="combobox_Product">
									<option value=""></option>
									<%
										for (Map productList : totalProductList) {
											strProductId = productList.get("PRODUCT_ID") == null ? "" : productList.get("PRODUCT_ID").toString();
											strProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
									%>

									<option value="<%=strProductId%>"><%=strProductName%></option>
									<%
										}																							}
									%>
								</select>

							</div>

							<label class="control-label col-xs-12 col-sm-2">SubProductName:</label>
							<div class="col-xs-12 col-sm-3">
								<select id="combobox_SubProduct" name="combobox_SubProduct">
									<option value=""></option>
								</select>
							</div>
						</div>

						<div class="form-group" style="margin-left:20px;">

							<label class="control-label col-xs-12 col-sm-2">Child Product Name:
							</label>

							<div class="col-xs-12 col-sm-3">
								<select id="combobox_ChildProduct" name="combobox_ChildProduct">
									<option value=""></option>

								</select>
							</div>


							<%
								session = request.getSession(true);
								String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
								String loginId = request.getAttribute("siteId") == null ? "" : request.getAttribute("siteId").toString();																
								if (strSearchType.equals("ADMIN")) {
							%>

							<label class="control-label col-sm-2">Site</label>

							<div class="col-sm-3">
								<%
									List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
									String strSiteId = "";
								    String strSiteName = "";
								%>
								<select id="dropdown_SiteId" name="dropdown_SiteId"
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
								<input type="hidden" name="loginId" id="loginId" value=<%=loginId%>>
							</div>
							<%
								}
							%>

						</div><br/>
						<br/>
						<div style="text-align:center;margin-top:-41px;;"> 
						 <input type="submit" value="Submit" class="btnname btn btn-warning col-sm-2 col-sm-offset-4" align="center">
						</div>
					</form>
				</div>

			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>



	<script src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/ComboBox_IndentAvailabulity.js"></script>
	<script src="js/custom.js"></script>

	<script>
		$(function() {
			$("#ReqDateId1").datepicker({
				dateFormat : 'dd-M-y',maxDate: new Date(),changeMonth: true,
			      changeYear: true
			});
			$("#ReqDateId2").datepicker({
				dateFormat : 'dd-M-y', maxDate: new Date(),changeMonth: true,
			      changeYear: true
			});
			return false;
		});

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
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		});

		function formValidator() {

			var fromDt = document.getElementById('ReqDateId1').value;
			if (fromDt == '' || fromDt == null || fromDt == '@@') {
				alert("please Choose From Date");
				return false;

			} else {
				
				var loginId = document.getElementById('loginId').value;
				if (loginId == '999') {
					var siteId = document.getElementById('dropdown_SiteId').value;
					if (siteId == '' || siteId == null || siteId == '@@') {
						alert("please Choose To Site Name");
						return false;
						
					}
				}
				var toDate = document.getElementById('ReqDateId2').value;
		
				if (!toDate == '' || !toDate == null || !toDate == "@@") {
					if (toDate == '' || toDate == null || toDate == '@@') {
						alert("please Choose To Date");
						return false;
	
					}
					
					
					if ((new Date(toDate).getTime()) >= (new Date(fromDt).getTime())) {
					    //alert('correct');
					} else {
					    alert("To Date Not Lessthan From Date");
					    return false;
					}
				
				}
			/* 	var product = document.getElementById('combobox_Product').value;
				if (product == '' || product == null || product == '@@') {
					alert("please enter product name");
					return false;

				}
				
				
				
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
				} */
			}
		}
	</script>
</body>
</html>
