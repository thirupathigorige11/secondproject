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

<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> -->
<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />
<!-- link href="js/jquery-ui.css" rel="stylesheet"-->
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> 
<link href="js/inventory.css" rel="stylesheet"> 
<link href="css/topbarres.css" rel="stylesheet"> 
<!-- <link rel="stylesheet" type="text/css" href="css/jquery.dataTablesFixedheader.min.css"> -->
<!-- <link rel="stylesheet" type="text/css" href="css/jquery.dataTablesFixedheader.min.css"> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.table.dataTable {border-collapse:collapse !important;}
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
	width:100%;
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
.breadcrumb {
    background: #eaeaea !important;
}
</style>

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
				<div class="">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">BOQ Details</li>
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
				  <div class="col-md-12 border-indent"> <!--  container1 -->
	 	             <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">BOQ Number :</label>
			        <div class="col-md-6" >
			        <p><strong>${ObjBOQDetails.strBOQNo}</strong></p>
				       <%--  <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${boqMainList.intTempBOQNo}" class="form-control"/>
			            <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${ObjBOQDetails.strBOQNo}" class="form-control"/>  --%>
			          </div> 
			      </div>
	 	             </div>
			       <div class="col-md-4">
			        <div class="form-group">
			            <label class="control-label col-md-6">Version Number :</label>
			            <div class="col-md-6" >
			             <p><strong>${ObjBOQDetails.strVersionNo}</strong></p>
				       <%--  <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${boqMainList.intTempBOQNo}" class="form-control"/>
			            <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${ObjBOQDetails.strVersionNo}" class="form-control"/>  --%>
			          </div> 
			      </div>
			       </div>
			       <div class="col-md-4">
			        <div class="form-group">
			            <label class="control-label col-md-6">Creation Date :</label>
			            <div class="col-md-6" >
			            <p><strong>${ObjBOQDetails.strBOQCreationDate}</strong></p>
				        <%-- <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${boqMainList.intTempBOQNo}" class="form-control"/>
			            <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${ObjBOQDetails.strBOQCreationDate}" class="form-control"/> --%> 
			             </div> 
			      </div>
			       </div>
			        <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">Type of Work : </label>
			            <div class="col-md-6">
			              <p><strong>${ObjBOQDetails.typeOfWork}</strong></p>
			            </div>
			      </div>
	 	             </div>
	 	              <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">BOQ Total :</label>
			            <div class="col-md-6">
			              <p><strong>${ObjBOQDetails.boqTotalAmount}</strong></p>
			            </div>
			      </div>
			      
	 	             </div>
	 	             <div class="col-md-4">
		 	              <div class="form-group">
					            <label class="control-label col-md-6">BOQ Material Amount :</label>
					            <div class="col-md-6">
					              <p><strong>${ObjBOQDetails.boqMaterialAmount}</strong></p>
					            </div>
				         </div>
	 	             </div>
	 	             <div class="col-md-4">
		 	              <div class="form-group">
					            <label class="control-label col-md-6">BOQ Labor Amount :</label>
					            <div class="col-md-6">
					              <p><strong>${ObjBOQDetails.boqLaborAmount}</strong></p>
					            </div>
				         </div>
	 	             </div>
	 	      </div>
           <!-- container1 end -->
				<div class="col-md-12 border-inwards-box">
				  	<form action=""  id="getBOQDetailsform" class="horizantal-form" method="post" >
					<!-- <div class="prodt_tbl col-xs-12 col-md-12"> -->
					<div class="col-md-4">
						<div class="form-group">
						<label class="col-md-6">Major Head :</label>
						<div class="col-xs-12 col-md-6">
													<%
											List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request
																									.getAttribute("totalProductsList");
																							if (totalProductList.size() > 0) {
																								String strProductName = "";
																								String strProductId = "";
										%> <select id="combobox_Product" name="combobox_Product">
											<option value=""></option>
											<%
												for (Map productList : totalProductList) {
																												strProductId = productList.get("WO_MAJORHEAD_ID") == null ? ""
																														: productList.get("WO_MAJORHEAD_ID").toString();
																												strProductName = productList.get("WO_MAJORHEAD_DESC") == null ? "" : productList.get("WO_MAJORHEAD_DESC").toString();
											%>

											<option value="<%=strProductId + "@@" + strProductName%>"><%=strProductName%></option>
											<%
												}
																										}
											%>
									</select>
						</div>
						</div>
						</div>
						<div class="col-md-4">
						<div class="form-group">
							<label class="col-md-6" style="font-size:14px;">Minor Head :</label>
							<div class="col-md-6"><select id="combobox_SubProduct"  name="combobox_SubProduct">
									<option value=""></option>
								</select></div>
								
							
						</div>
						</div>
						<div class="col-md-4">
						<div class="form-group">
							<label class="col-xs-12 col-md-5" style="">Work Description :</label>
							<div class="col-xs-12 col-md-6">
								<select id="combobox_ChildProduct" name="combobox_ChildProduct">
									<option value=""></option>

								</select>
							</div>
						</div>
						</div>
							<div class="col-md-12 text-center center-block Mrgtop10">
							<button type="button" onclick="return formValidator()" value="Submit" class="btnname btn btn-warning">Submit</button>
							</div>
							<input type="hidden" name="BOQNo" value="${ObjBOQDetails.strBOQNo}"/>
							<input type="hidden" name="BOQDate" value="${ObjBOQDetails.strBOQCreationDate}"/>
							<input type="hidden" name="BOQSeqNo" value="${ObjBOQDetails.strBOQSeqNo}"/>
							<input type="hidden" name="siteId" value="${ObjBOQDetails.strSiteId}"/>
							<input type="hidden" name="siteName" value="${ObjBOQDetails.strSiteName}"/>
							<input type="hidden" name="versionNo" value="${ObjBOQDetails.strVersionNo}"/>
							<input type="hidden" name="typeOfWork" value="${ObjBOQDetails.typeOfWork}"/>
							<input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">

						<!-- </div> -->

					</form>
				</div>
						<!-- TABLE -->
					<div class="col-md-12" style="margin-top:20px;">
					  <div class="clearfix" ></div>
										<table id="example"	class="table table-striped table-bordered st-table"  cellspacing="0">
											<thead>
												<tr>
													<th style="width:200px;">Major Head </th> 
													<th>Total Material Amount</th>
													<th>Total Labor Amount</th>
													<th>Total Amount</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${majorHeadsDetails}" var="element">
													<tr>
														<td><a class="anchor-class" href="getMinorHeads.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&majorHeadId=${element.strMajorHeadId}&pageHighlightURL=${pageHighlightURL}"  title="Click on the product name to see the details">${element.strMajorHeadDesc}</a>  </td>
														<td title="${element.strMajorHeadMaterialTotalAmount}">${element.strMajorHeadMaterialTotalAmount}</td>
														<td title="${element.strMajorHeadLaborTotalAmount}">${element.strMajorHeadLaborTotalAmount}</td>
														<td title="${element.strMajorHeadTotalAmount}">${element.strMajorHeadTotalAmount}</td>
														
													</tr>
												</c:forEach>	
										 </tbody>
								  </table>
					              <div class="col-md-12 text-center center-block">
					                 
					    <a href="getBOQDetails.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&showCompleteBOQ=true&pageHighlightURL=${pageHighlightURL}" class="btn btn-warning btn-bottom" >SHOW COMPLETE BOQ</a>
						<a href="downloadBOQ.spring?BOQNumber=${ObjBOQDetails.strBOQNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&permanentBOQ=true&versionNo=${ObjBOQDetails.strVersionNo}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bottom">DOWNLOAD BOQ</a>						
						<a href="getBOQChangedWorkDetails.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&versionNo=${ObjBOQDetails.strVersionNo}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bottom">VIEW CHANGES</a>						
						<a href="getBOQAllVersions.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bottom">VIEW ALL VERSIONS</a>
					              </div>
						   
					</div>
                  <!-- TABLE - end -->
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
	<script type="text/javascript" src="js/BOQJS/SelectBOQWork.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
    <script src="js/dataTables.bootstrap.min.js"></script>
    <script src="js/sidebar-resp.js"></script>
	<!-- script for data table fixed header-->
<!-- <script src="js/jqueryFixedheader-3.3.1.js"></script>
<script src="js/jquery.dataTablesFixedheader.min.js"></script> -->

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
					$('.anchor-class').click(function(){
						$('.loader-sumadhura').show();
					});

				});

		
		     $('#example').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		     //$('#example').stacktable({myClass:'stacktable small-only'});
		function formValidator() {debugger;
			var show=false;
			var product = document.getElementById('combobox_Product').value;
			var subProduct=document.getElementById('combobox_SubProduct').value;
			var childProduct=document.getElementById('combobox_ChildProduct').value;
			if (product == '' || product == null || product == '@@') {
				alert("Please select major head.");
				return false;

			}
			if(product!=='' && subProduct!='' && childProduct.split("@@")[1]!=''){
				show=true;
			}
			$('.loader-sumadhura').show();
			document.getElementById("getBOQDetailsform").action = "getBOQDetails.spring?&&show="+show;
			document.getElementById("getBOQDetailsform").method = "POST";
			document.getElementById("getBOQDetailsform").submit();
			
			
			
			
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
</body>
</html>
