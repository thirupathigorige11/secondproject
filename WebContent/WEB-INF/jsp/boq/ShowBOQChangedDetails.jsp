<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import = "java.util.ResourceBundle" %>
<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");

/* label.M.head=Major Head 
label.Minor_head=Minor Head
label.work_desc=Work Desc
label.scope_of_work=Scope Of Work
label.B.Price=Price
label.Quantity=Quantity
label.Block =Block
label.floor=Floor
label.flat=Flat */

/* String colon = resource.getString("label.colon") */;
String serialNumber = resource.getString("label.serialNumber");
String majorHead = resource.getString("label.Major_head");
String minorHead = resource.getString("label.Minor_head");
String work_desc = resource.getString("label.work_desc");
String measurement = resource.getString("label.measurement");
String scope_of_work = resource.getString("label.scope_of_work");

String price = resource.getString("label.BoqPrice");
String quantity = resource.getString("label.Quantity");
String block = resource.getString("label.Block");
String floor = resource.getString("label.floor");
String flatNumber= resource.getString("label.flat");
String remarks = resource.getString("label.remarks");
String actions = resource.getString("label.actions");
String tableHead = resource.getString("label.indentIssueTableHead");
String areaPricePerUnit=resource.getString("label.areaPricePerUnit");
%>
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
						<li class="breadcrumb-item active">BOQ Changes</li>
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
	 	             <input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">
	 	             
	 	      </div>
	 	      
	 	<c:choose>
		<c:when test = "${showChanges}">
	 	      
	 	      <!--  -->
          <table id="boqTableId" class="table table-striped table-bordered st-table stacktable large-only dataTable no-footer "><!-- tblprodindiss -->
		<thead>
		<tr>
		<th class="width-indent-th"><%= serialNumber %></th>
    				<th width=213px;><%= majorHead %></th>
    				<th width=213px;><%= minorHead %></th>
    				<th width=213px;><%= work_desc %></th>
    				<th width=213px;><%= measurement %></th>     				
    				<th  width=213px;class="w-70">Old Quantity</th>
    				<th  width=213px;class="w-70">New Quantity</th>
    				<th>Old Price</th>
    				<th>New Price</th>
    				<th  width=213px;class="w-70"><%= block %></th>
    				<th  width=213px;class="w-70"><%= floor %></th>
    				<th  width=213px;class="w-70"><%= flatNumber  %></th>
    				<th  width=213px;class="w-70">Actions</th>
		</tr>
		</thead>
		<tbody>
  			
  			 <c:forEach var="boqData" items="${requestScope['ReviseBOQChangedDetails']}">
  			<tr>
  					<td>${boqData.strSerialNumber}</td>
  					<td>${boqData.strMajorHeadDesc }</td>
  					<td>${boqData.strMinorHeadDesc }</td>
  					<td>${boqData.strWorkDescription }</td>
  					<td>${boqData.strMeasurementName }</td>
  					<td>${boqData.oldStrArea }</td>
  					<td>${boqData.strArea }</td>
  					<td>${boqData.oldDoubleLaborRatePerUnit }</td>
  					<td>${boqData.doubleLaborRatePerUnit }</td>
  					<td>${boqData.strBlock }</td>
  					<td>${boqData.strFloor }</td>
  					<td>${boqData.strFlat }</td>
  					<td>${boqData.action }</td>
  					
  			</tr>
  			</c:forEach>

				
					</tbody>	
		</table>
           <!--  -->
           <div class="row">
				<div class="form-group">
				  <label class="control-label col-md-2" >Modification Details: </label>
						<div class="col-md-4" style="margin-bottom: 10px;" >
							<ol type="1">
							<c:forEach var="ChangedDetails" items="${requestScope['ReviseBOQChangedDetails']}">
							  <li>${ChangedDetails.modificationDetails}</li>
							 </c:forEach>
							</ol>
						</div>
				</div>
			
			</div>
           <!--  -->
		</c:when> 
		<c:otherwise>
			<center><h3>There is No Changes</h3></center>
		</c:otherwise>	 
		</c:choose>
		
          <div class="col-md-12 text-center cnter-block"><a href="downloadBOQ.spring?BOQNumber=${ObjBOQDetails.strBOQNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&permanentBOQ=true&versionNo=${ObjBOQDetails.strVersionNo}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning ">DOWNLOAD BOQ</a>						 
           <a href="getBOQAllVersions.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bootom">VIEW ALL VERSIONS</a>						
           <a href="getBOQDetailsSelection.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bootom">Back To BOQ Main Page</a>
			</div>
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
					$('.btn-bootom').click(function(){
						$('.loader-sumadhura').show();
					});

				});

		
		
		     $('#example').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
		     $('#example').stacktable({myClass:'stacktable small-only'});
		function formValidator() {debugger;
			var show=false;
			var product = document.getElementById('combobox_Product').value;
			var subProduct=document.getElementById('combobox_SubProduct').value;
			var childProduct=document.getElementById('combobox_ChildProduct').value;
			if (product == '' || product == null || product == '@@') {
				alert("please enter product name");
				return false;

			}
			if(product!=='' && subProduct!='' && childProduct.split("@@")[1]!=''){
				show=true;
			}
			
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
