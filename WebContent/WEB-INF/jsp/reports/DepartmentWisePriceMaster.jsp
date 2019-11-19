<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="../CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">

<!-- <link href="css/dataTables.bootstrap.min.css" rel="stylesheet">  -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- Data Table scripts -->
<!-- <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.min.css"/> --> 
<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>

<!-- <script src="https://code.jquery.com/jquery-3.3.1.js"></script> -->
<!-- <script src="css/jquery.dataTables.min.css"></script> -->
<!-- Data Table Scripts -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.form-control{height:34px !important;}
.form-horizontal .control-label{text-align:center !important;}
.success,.error {
	text-align: center;
	font-size: 16px;
}
table.dataTable {border-collapse:collapse !important;}
input[type=search] {
    -webkit-appearance: textfield;
}
@media (max-width: 800px){
.dataTables_paginate {
    display: block !important;
}
}
.table>thead>tr>th{
background-color:#ccc !important;
border:1px solid #000 !important;
color:#000 !important;
}
.table>tbody>tr>td{
border:1px solid #000 !important;
}
table.dataTable {
border-collapse: collapse;
}
.input-group-addon {
border: 1px solid #ccc !important;
}
@media only screen and (min-width:320px) and (max-width:767px){
.table-responsive>.table-bordered>thead>tr>th:first-child {border-left:1px solid #000;}
.table-responsive>.table-bordered>thead>tr>th:last-child {border-right: 1px solid #000;}
.form-horizontal .control-label{text-align:left !important;}
}
</style>

<script type="text/javascript">
	function validate() {		
	debugger;
		var siteId=document.getElementById("dropdown_SiteId").value;
			var childProdName=document.getElementById("childProdName").value;
			 var siteName=$('#dropdown_SiteId').find('option:selected').text();
			 $("#SITE_NAME").val(siteName);
			 if((childProdName==""||childProdName==null)&&siteId==""){
				alert("Please enter child product name!");
				document.getElementById("childProdName").focus;
				return false;	
			}
			 $(".loader-sumadhura").show();
			return true;	
	}
	
	function clearText(){
	 $("#childProdName").val("");
	 $(".clearbtn").hide();
	}
</script>
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
						<li class="breadcrumb-item active">Price Master</li>
					</ol>
				</div>
				<div class="overlay_ims" ></div>
					 <div class="loader-ims" id="loaderId"> <!--  -->
						<div class="lds-ims">
							<div></div><div></div><div></div><div></div><div></div><div></div></div>
						<div id="loadingimsMessage">Loading...</div>
					</div>
				
				 <div class="loader-sumadhura" style="z-index:99;display:none;">
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
<div class="">
    <label class="success text-center col-md-12" id="successMsg" style="display: none;color: red;"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
	<form:form  id="indentIssueFormId" action="ViewThreeMonthProductPrice.spring" class="form-horizontal border-inwards-box">
		
		
			<div class="col-md-4">
				  <div class="form-group">
				     <label class="col-md-6 control-label">Site :</label>			
					<div class="col-md-6" id="dropdownSiteId">
					 <input type="hidden" name="SITE_NAME" id="SITE_NAME">
					 <input type="hidden" name="PriceMaster" id="PriceMaster" value="${priceMaster }">
					 <select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control "> 
					 
					</select>
				 </div>
	 		   </div>
			 </div>
			<div class="col-md-4">
				 <div class="form-group">
					<label class="col-md-6 control-label">Child Product Name :</label>
					<div class="col-md-6"> <input id="childProdName" name="childProdName" autocomplete="off" class="form-control" size="20" value="<c:out value='${childProdName}'/>">
						<button  type="button" class="btn clearbtn" onclick="clearText()">x</button>
							<input type="hidden" id="childProdID">
							<input type="hidden" id="tempchildProdID">
					</div>
				 </div>
			</div>
				<div class="col-md-4">
					<div class="form-group">
						<label class="col-md-6 control-label col-xs-12">Month:</label>
							 <div class="col-md-6 input-group col-xs-12">
								  <input type="text" id="monthYear" name = "monthYear" value="${monthYear}" class="form-control" onkeydown="" autocomplete="off" />
							      <label class="input-group-addon btn" for="monthYear">
								  <span class="fa fa-calendar"></span>
						</label>
					  </div>
				   </div>
				</div>
				
			<div class="col-md-12 text-center center-block">
				<input type="submit" value="Submit" class="btn btn-warning Mrgtop10" id="saveBtnId" onclick="return validate();">
			</div>	
			
								
			<div>${displayErrMsg}</div>
	</form:form>
</div>
<div class="clearfix"></div>
<div class="">
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="table-responsive" >
				<table id="example"	class="table table-striped table-bordered display" cellspacing="0">
					<thead>
							<tr>
								<th style="width:200px;">Site Name </th> 
								<th style="width:200px;">Invoice No/DC No </th>
								<th style="width:200px;">Vendor Name </th>
								<th style="width:200px;">Child Product Name</th> 
								<th>Measurement</th>
								<th>Amount/Unit Before Taxes</th>
								<th>Amount/Unit After Taxes</th>
							</tr>
					</thead>
					<tbody>
					<c:set var="search" value='"' />
						<c:set var="replace" value='' />
					
					<c:forEach items="${childPriceListBySite}" var="product">
						<tr>
							<td title="${product.site_name}">${product.site_name}</td>
							<c:choose>
								<c:when test="${not empty product.dc_number && not empty product.invoice_number}">
									<td><a  target="_blank" href="getGrnDetails.spring?invoiceNumber=${product.invoice_number}&vendorId=${product.vendor_id}&invoiceDate=${product.indent_recive_date}&indentEntryId=${product.indent_entry_id}&type=invoicePriceMaster&siteId=${product.site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${product.invoice_number} </a>&nbsp;/
										<a  target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${product.dc_number}&vendorId=${product.vendor_id}&dcDate=${product.dc_recive_date}&dcEntryId=${product.dc_entry_id}&type=dcGrnPriceMaster&SiteId=${product.site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${product.dc_number} </a>
									</td>
								</c:when>
								<c:when test="${not empty product.invoice_number}">
									<td><a  target="_blank" href="getGrnDetails.spring?invoiceNumber=${product.invoice_number}&vendorId=${product.vendor_id}&invoiceDate=${product.indent_recive_date}&indentEntryId=${product.indent_entry_id}&type=invoicePriceMaster&siteId=${product.site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">INV_${product.invoice_number} </a></td>
								</c:when>
								<c:when test="${not empty product.dc_number}">
									<td><a  target="_blank" href="getDcFormGrnViewDts.spring?invoiceNumber=${product.dc_number}&vendorId=${product.vendor_id}&dcDate=${product.dc_recive_date}&dcEntryId=${product.dc_entry_id}&type=dcGrnPriceMaster&SiteId=${product.site_id}&showHomebtn=false" style="text-decoration: underline;color: blue;" onclick="loader();">DC_${product.dc_number} </a></td>
								</c:when>
							</c:choose>
						
							<td title="${product.vendor_name}">${product.vendor_name}</td>
						<%-- <td><a href="getProductDetails.spring?CHILD_PRODUCT_ID=${product.child_product_id}&SITE_ID=${product.site_id}&PRICE_ID=${product.price_id}&NAME=<c:out value='${fn:replace(product.child_product_name, search, replace)}'/>" id="<c:out value='${fn:replace(product.child_product_name, search, replace)}'/>${product.site_id}"  style="text-decoration: underline;color: blue;" onclick="setSiteIdForSelectBox(${product.site_id},'<c:out value='${fn:replace(product.child_product_name, search, replace)}'/>${product.site_id}')" title="Click on the product name to see the details">${product.child_product_name}</a>  </td> --%>
							<td><a href="getProductDetails.spring?CHILD_PRODUCT_ID=${product.child_product_id}&SITE_ID=${product.site_id}&SITE_NAME=${product.site_name}&PriceMasterType=${priceMaster}&PRICE_ID=${product.price_id}&NAME=${product.child_product_name.replaceAll('[^A-Za-z]', '')}" id="<c:out value='${fn:replace(product.child_product_name, search, replace)}'/>${product.site_id}"  style="text-decoration: underline;color: blue;" onclick="setSiteIdForSelectBox(${product.site_id},'<c:out value='${fn:replace(product.child_product_name, search, replace)}'/>${product.site_id}')" title="Click on the product name to see the details">${product.child_product_name}</a>  </td>
							<td title="${product.measurement_name}">${product.measurement_name}</td>
							<td>${product.amount_per_unit_before_taxes}</td>
							<td>${product.amount_per_unit_after_taxes}</td>
						</tr>
					</c:forEach>	
				    </tbody>
				</table>
				</div>
           <%
				   }
           %>
        </div>
				<!-- /page content -->
			</div>
		</div>
	</div>

	<!-- jQuery -->
<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
<!-- <script src="js/stacktable.js"></script> -->
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/marketing/jquery.ui.monthpicker.js" type="text/javascript"></script> 
	<script src="js/marketing/jquery.timepicker.min.js"></script> 
	<script src="js/sidebar-resp.js"></script> 	
	<script src="js/dataTables.bootstrap.min.js"></script> 
	<script>
	function setSiteIdForSelectBox(siteId,childProductName) {
		debugger;
		if (typeof(Storage) !== "undefined") {
			 var childProdName=document.getElementById("childProdName").value;
			 if(childProdName==""){
				 childProdName=true;
			 }
		     var mounthYear=document.getElementById("monthYear").value;
		     var childProductNameUrl=document.getElementById(childProductName).getAttribute("href");
		     document.getElementById(childProductName).setAttribute("href",childProductNameUrl+"&monthYear="+mounthYear+"&childProdName="+childProdName);
		    sessionStorage.setItem("siteIdAfterRefresh", siteId);
		    // Retrieve
		    //document.getElementById("result").innerHTML = sessionStorage.getItem("siteIdAfterResfres");
		} else {
		  //  document.getElementById("result").innerHTML = "Sorry, your browser does not support Web Storage...";
		}
		//$(".loader-sumadhura").show();
	}
	$(document).ready(function(){
		//code for site id
	 	$("#dropdown_SiteId").on("change",function(){
	 		debugger;
			 var siteId=$(this).val();
			 var siteName=$('#dropdown_SiteId').find('option:selected').text();
			 $("#SITE_NAME").val(siteName);
			 if (typeof(Storage) !== "undefined") {
				    // Store   	
				     debugger;
				    sessionStorage.setItem("siteIdAfterRefresh", siteId);
				    sessionStorage.setItem("siteNameAfterRefresh",siteName);
				    // Retrieve
				    //document.getElementById("result").innerHTML = sessionStorage.getItem("siteIdAfterResfres");
				} else {
				  //  document.getElementById("result").innerHTML = "Sorry, your browser does not support Web Storage...";
				}
			 
	}); 
		
	    var siteId="${strSiteId}";
	    var siteName=sessionStorage.getItem("siteNameAfterRefresh",siteName);
	    var siteData="<option value='"+siteId+"'>"+siteName+"</option>";
	    if(siteId.length!=0){
	    	$("#dropdown_SiteId").html(siteData);	
	    }
	    var data="";	
	    		debugger;
	   if("${SiteId}"=="996"){
					data+="<option value='996' selected>MARKETING</option>";
					$("#dropdown_SiteId").html(data);		
		}else{
			$.ajax({
				  url : "./loadAllSites.spring",
				  type : "GET",
				  dataType : "json",
				  success : function(resp) {
				   debugger;
				   var siteId=sessionStorage.getItem("siteIdAfterRefresh");
				   data="";	 
				   data+="<option value=''></option>";
				 
						$.each(resp,function(index,value){
						debugger;
						if(value.SITE_ID=="996"){
							return;
						}
							if("${strSiteId}"==value.SITE_ID){
								debugger;
								data+="<option value="+value.SITE_ID+" selected>"+value.SITE_NAME+"</option>";
							}else{
								data+="<option value="+value.SITE_ID+">"+value.SITE_NAME+"</option>";
							}
		
						});  
				 
				sessionStorage.setItem("siteIdAfterRefresh", "000");
				sessionStorage.setItem("siteIdAfterRefresh", "");
				var searchType="${SEARCHTYPE}";
				if(searchType=="ADMIN"){
					$("#dropdown_SiteId").html(data);		
				}
				$("#successMsg").show();
				//
			  },
			  error:  function(data, status, er){
				//  alert(data+"_"+status+"_"+er);
				  }
			  });  
	}
		debugger;
		var childProductText = $("#childProdName").val().trim();
		if(childProductText == ""){
			$(".clearbtn").hide();
			// false;
		}else{
			$(".clearbtn").show();
			//return false;
		}
		
		$("#childProdName").keyup(function () {	
			
			var childProductText = $("#childProdName").val().trim();
			if(childProductText == ""){
				$(".clearbtn").hide();
				return false;
			}
			
			$(".clearbtn").show();
			var str=$(this).val();
		
			var type=$("#PriceMaster").val();
			 var siteId=$('#dropdown_SiteId').val();
			 var siteName=$('#dropdown_SiteId').find('option:selected').text();
			 $("#SITE_NAME").val(siteName); 	    
			
			
				$("#childProdName").autocomplete({
		   			source : function(request, response) {
		   				$.ajax({
		   					  url : "./loadChildProductLatestPriceNames.spring",
		   					  type : "GET",
		   					  data:{
		   						prodName:str,
		   						type:type,
		   						SITE_ID:siteId,
		   						SITE_NAME:siteName
		   						
		   					  },
			   					dataType : "json",
		   						success : function(data) {
		   							debugger;
		   							response($.map(data, function (value, key) { 
			                          debugger;
		   								return {
			                                label: value.NAME,
			                                value: value.CHILD_PRODUCT_ID
			                            };
			                       }));
			    				}
			    			});
			    		},
				        change: function (event, ui) {
				               if(!ui.item){
					                    // The item selected from the menu, if any. Otherwise the property is null
					                    //so clear the item for force selection
				                   $("#childProdName").val("");
				                    $(".clearbtn").hide();
				                }
				            },
				            select: function(event, ui) {  
				                $("#childProdID").val(ui.item.value);                  
				                $("#tempchildProdID").val(ui.item.label);
				                $("#childProdName").val(ui.item.label);
				                return false;  
				            }  
			    		});
				});

	});
	
	
	/* $(function() {
		  $("#ReqDateId1").datepicker({dateFormat: 'dd-M-y',maxDate: new Date(),changeMonth: true,
		      changeYear: true});
		  $("#ReqDateId2").datepicker({dateFormat: 'dd-M-y',maxDate: new Date(),changeMonth: true,
		      changeYear: true});
		  return false;
	}); */
	
	
		$(document).ready( function() {
					
			$('#monthYear').monthpicker({changeYear:true,dateFormat:"mm-yy"});
						
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					
				});

		
	
	</script>
<script>
	$(document).ready(function() {
	    $('#example').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
	});
	
	$(window).load(function () {
	  $(".overlay_ims").hide();	
	  $(".loader-ims").hide(); 
	});
</script>
</body>
</html>
