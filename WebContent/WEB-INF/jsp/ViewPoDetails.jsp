<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

	<%@page import="com.sumadhura.bean.IndentCreationBean"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
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

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css"/>
<link href="css/topbarres.css" rel="stylesheet" type="text/css"/>

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
table.dataTable {border-collapse:collapse !important;}
.success,.error {text-align: center;font-size: 14px;}
.form-control{height:34px;}
.input-group-addon {border: 1px solid #ccc !important;border-left-width: 0 !important;}
 table.table-bordered.dataTable th:last-child{border-right-width:1px;}
 table.table-bordered.dataTable th{border-left-width:1px;}
  @media only screen and (max-width: 800px) and (min-width:320px){.dataTables_paginate { display: block !important;}}
  .chexkbox_siteall {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
 .chexkbox_siteall1 {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
 .chexkbox_site {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;}
 .checkbox_sitelabel {margin-top: 6px;margin-right: 15px;float: left;font-size: 15px;}
</style>

<script type="text/javascript">


	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;
		var poNumber = document.getElementById("poNumber").value;
		var vendorName = document.getElementById("VendorNameId").value;
		var checkBoxCheck = $('input.chexkbox_siteall').is(':checked')
		var SiteData=[];
        $(".chexkbox_site").each(function(){debugger;
        	//debugger;
        	if($(this).prop("checked") == true){
        	var currentSite=$(this).val();
        	SiteData.push(currentSite);
        	}
        });
        console.log("SiteData: "+SiteData);
        console.log("SiteData: "+SiteData.length);
        $('#siteNames').val(SiteData);
   	
		 if (from == "" && to == ""  && poNumber =="" && vendorName =="" && checkBoxCheck == false ) { 
			alert("Please Select any one of the Input !");
			return false;
		}
		$(".loader-sumadhura").show();
		
		
	}
</script>
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
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">${requestScope['PurchasePO']}</li>
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

<div class="">
<div class="container border-indents-box">
<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
	<c:choose>
	<c:when test="${allPoOrNot}">	
	<form class="form-horizontal"  action="ViewAllPo.spring" >
	<div class="col-md-10 col-md-offset-1">
    <div class="col-md-6">
     <div class="form-group">
	    <label for="date" class="col-md-4">From Date :</label>
	    <div class="col-md-7 input-group"><input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly>
	      <label class="input-group-addon btn" for="ReqDateId1">
										<span class="fa fa-calendar"></span>
									</label>
	    </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">To Date :</label>
	   <div class="col-md-7 input-group">
	    <input type="text" id="ReqDateId2" class="form-control readonly-color"  name="toDate" value="${toDate}" autocomplete="off" readonly>
	     <label class="input-group-addon btn" for="ReqDateId2">
					<span class="fa fa-calendar"></span>
		</label>
	    
	   </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Po Number :</label>
	   <div class="col-md-7 input-group">
	    <input type="text" id="poNumber" class="form-control readonly-color"  name="poNumber"  autocomplete="off" >
	     
	    
	   </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Vendor Name :</label>
	   <div class="col-md-7 input-group">
	    <input type="text" id="VendorNameId" class="form-control readonly-color"  name="vendorName"  onkeyup="getVendorId()" autocomplete="off" >
	     <input type="hidden" name="VendorId" id="vendorIdId" class="form-control" value="" autocomplete="off"/>
	    
	   </div>
  	</div>
    </div>
     <div class="col-md-12 col-sm-12 no-padding-left">
								<div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class="chexkbox_siteall" /><label class="checkbox_sitelabel "><strong>Show Sites</strong></label></div></div>
								<div class="col-md-12 text-left"><div class="col-md-12 hide_select_site" style="display:none;"><input type="checkbox" class=" chexkbox_siteall1"><label class="checkbox_sitelabel"> Select All</label></div></div>
								
								<div class="col-md-12 text-left hide_select_site" style="display:none;">
								<c:forEach items="${siteDetails}" var="site">
                				  <div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="${site.key}"></div><div class="col-md-10"><label class="checkbox_sitelabel">${site.value}</label></div></div>
								   </c:forEach>
							  </div>
							  </div>
 	 <!-- <hr><label class="or-class" style="position: absolute; margin-top: -33px; margin-left: 142px;margin-bottom: 15px;" >(Or)</label> -->
 	<%-- <div class="col-md-5 col-xs-5"><hr class="hr-line"></div><div class="col-md-2 col-xs-2 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 col-xs-5"><hr class="hr-line"></div>
    <div class="col-md-8 col-md-offset-2">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Po Number:</label>
	    <div class="col-md-6"><input type="text" id="poNumber" class="form-control" name="poNumber" value="${poNumber}" autocomplete="off"></div>
  	</div>
     
    </div> --%>
 
	     <%
			String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
			log("View PO Details ");
			if (strSearchType.equals("ADMIN")) {
				//request.setAttribute("SEARCHTYPE", "");
			%>
			<label for="todate" style="margin-left: 4%;">Site</label>
			<%
			List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
			String strSiteId = "";
			String strSiteName = "";
		%> 
		<select id="dropdown_SiteId" name="dropdown_SiteId" style="margin-left:6%;"	class="custom-combobox form-control indentavailselect">
				<option value=""></option>
		<%
			for (Map siteList : totalSiteList) {
			strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
			strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
		%>
			<option value="<%=strSiteId%>"><%=strSiteName%></option>
		<%
			}//for loop
		%>
	</select><br> <%
 	}
 %>	
  
 
      <div class="col-md-12 text-center center-block">
      <input type="hidden" name="POSite_id" value="${POSite_id}">
      <input type="hidden" class="form-control" id="siteNames" name="siteIds"  value="" autocomplete="off"/>
        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
        <div style="margin-top: 36px;display:none;">${displayErrMsg}</div>
      </div>
  </div>
  </form>
	</c:when>			
		
		
		<c:when test="${sitePO}">	
	<form class="form-horizontal"  action="getPoDetails.spring" >
	<div class="col-md-10 col-md-offset-1">
    <div class="col-md-6">
     <div class="form-group">
	    <label for="date" class="col-md-4">From Date :</label>
	    <div class="col-md-7 input-group"><input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly>
	      <label class="input-group-addon btn" for="ReqDateId1">
										<span class="fa fa-calendar"></span>
									</label>
	    </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">To Date :</label>
	   <div class="col-md-7 input-group">
	    <input type="text" id="ReqDateId2" class="form-control readonly-color"  name="toDate" value="${toDate}" autocomplete="off" readonly>
	     <label class="input-group-addon btn" for="ReqDateId2">
					<span class="fa fa-calendar"></span>
		</label>
	    
	   </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Po Number :</label>
	   <div class="col-md-7 input-group">
	    <input type="text" id="poNumber" class="form-control readonly-color"  name="poNumber"  autocomplete="off" >
	     
	    
	   </div>
  	</div>
    </div>
    <div class="col-md-6">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Vendor Name :</label>
	   <div class="col-md-7 input-group">
	  <input type="text" id="VendorNameId" class="form-control readonly-color"  name="vendorName"  onkeyup="getVendorId()" autocomplete="off" >
	     <input type="hidden" name="VendorId" id="vendorIdId" class="form-control" value="" autocomplete="off"/>
	     
	    
	   </div>
  	</div>
    </div>
 	 <!-- <hr><label class="or-class" style="position: absolute; margin-top: -33px; margin-left: 142px;margin-bottom: 15px;" >(Or)</label> -->
 	<%-- <div class="col-md-5 col-xs-5"><hr class="hr-line"></div><div class="col-md-2 col-xs-2 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 col-xs-5"><hr class="hr-line"></div>
    <div class="col-md-8 col-md-offset-2">
      <div class="form-group">
	    <label for="todate" class="col-md-4">Site Name:</label>
	    <c:forEach items="${siteDetails}" var="site">
	    <div class="col-md-6"><select><option>${site.key}</option></select><input type="text" id="poNumber" class="form-control" name="poNumber" value="${site.key}" autocomplete="off"></div>
  		</c:forEach>
  	</div>
     
    </div> --%>
    <c:if test="${DeptPo}">
     <div class="col-md-12 col-sm-12 no-padding-left">
								<div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class="chexkbox_siteall" /><label class="checkbox_sitelabel "><strong>Show Sites</strong></label></div></div><c:if test="${PurchasePO}">
								<div class="col-md-12 text-left"><div class="col-md-12 hide_select_site" style="display:none;"><input type="checkbox" class=" chexkbox_siteall1"><label class="checkbox_sitelabel"> Select All</label></div></div></c:if>
								
								<div class="col-md-12 text-left hide_select_site" style="display:none;">
								<c:forEach items="${siteDetails}" var="site">
                				  <div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="${site.key}"></div><div class="col-md-10"><label class="checkbox_sitelabel">${site.value}</label></div></div>
								   </c:forEach>
							  </div>
							  </div></c:if>
 
	     <%
			String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
			log("View PO Details ");
			if (strSearchType.equals("ADMIN")) {
				//request.setAttribute("SEARCHTYPE", "");
			%>
			<label for="todate" style="margin-left: 4%;">Site</label>
			<%
			List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
			String strSiteId = "";
			String strSiteName = "";
		%> 
		<select id="dropdown_SiteId" name="dropdown_SiteId" style="margin-left:6%;"	class="custom-combobox form-control indentavailselect">
				<option value=""></option>
		<%
			for (Map siteList : totalSiteList) {
			strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
			strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
		%>
			<option value="<%=strSiteId%>"><%=strSiteName%></option>
		<%
			}//for loop
		%>
	</select><br> <%
 	}
 %>	
  
 
      <div class="col-md-12 text-center center-block">
      <input type="hidden" name="POSite_id" value="${POSite_id}">
      <input type="hidden" class="form-control" id="siteNames" name="siteIds"  value="" autocomplete="off"/>
        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
        <div style="margin-top: 36px;display:none;">${displayErrMsg}</div>
      </div>
  </div>
  </form>
	</c:when>
			
	</c:choose>			
				
				
				
				
</div>
</div>

				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="col-md-12">
			<div class="table-responsive"> <!-- container1 -->
					<table id="tblnotification"	class="table table-bordered table-new " cellspacing="0">
						<thead>
	                         <tr>
								<th>PO Date</th>
			    				<th>PO Number</th>
			    				<th>Project Name</th>
			    				<th>Vendor Name</th>
			    				<th>Subject</th>
			    				<th>Type of PO</th>
					          </tr>
						</thead>
						<tbody>
						<% List<IndentCreationBean> poDetails = (List<IndentCreationBean>) request.getAttribute("PODetails");
						 for (IndentCreationBean element : poDetails) {
							 String type_Of_Po=element.getType_Of_Purchase();
							//int indent_CreationId=element.getIndentNumber();
						 
						%>
				<%-- <c:forEach items="${indentgetData}" var="element"> --%>  
				<tr>
				<%
										out.println("<td style='color: black'>");
										out.println(element.getStrScheduleDate());
										out.println("</td>");
										if(type_Of_Po.contains("CANCEL PO")){
											out.println("<td>");
											out.println("<a href='PrintCancelPOData.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&poEntryId="+element.getPoentryId()+"&vendorId1="+element.getVendorId()+"&siteName="+element.getSiteName()+"' style='text-decoration: underline;color: blue;' class='poNumber'>"+element.getPonumber()+"</a>");
											out.println("</td>");	
										}
										else if(!type_Of_Po.contains("MARKETING")){
										out.println("<td>");
										out.println("<a href='getPoDetailsList.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&vendorId1="+element.getVendorId()+"&siteName="+element.getSiteName()+"' style='text-decoration: underline;color: blue;' class='poNumber'>"+element.getPonumber()+"</a>");
										out.println("</td>");
										}else{
											out.println("<td>");
											out.println("<a href='getMarketingPoDetailsList.spring?poNumber="+element.getPonumber()+"&siteId="+element.getSiteId()+"&vendorId1="+element.getVendorId()+"&siteName="+element.getSiteName()+"' style='text-decoration: underline;color: blue;' class='poNumber'>"+element.getPonumber()+"</a>");
											out.println("</td>");
										}
										out.println("<td style='color: black'>");
										out.println(element.getSiteName());
										out.println("</td>");
										
										out.println("<td style='color: black'>");
										out.println(element.getVendorName());
										out.println("</td>");
										
										out.println("<td style='color: black'>");
										out.println(element.getSubject());
										out.println("</td>");
										
										
										out.println("<td style='color: black'>");
										out.println(type_Of_Po);
										out.println("</td>");
									
										%>
				   <%--  <td style="color: black;">${element.strScheduleDate}</td>
					
					<td><a href="getPoDetailsList.spring?poNumber=${element.ponumber}&siteId=${element.siteId}&siteName=${element.siteName}&vendorId1=${element.vendorId}" style="text-decoration: underline;color: blue;">${element.ponumber}</a></td>
					
				 	<td style="color: black;">${element.siteName}</td>
    				
					<td style="color: black;">${element.vendorName}</td>
					<td style="color: black;">${element.subject}</td>
					<td style="color: black;">${element.type_Of_Purchase}</td> --%>
					
				</tr>
				<%} %>
				<%-- </c:forEach> --%>
				
						</tbody>
				
					</table>
				</div>
           <%
				   }
           %>
				<!-- /page content -->
				</div>
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
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			maxDate: new Date(),
			 changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	     	        $("#ReqDateId2").datepicker("option","minDate", selected)
	     	        }

		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			  changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	            	$("#ReqDateId1").datepicker("option","maxDate", selected)
	            	        }

		  });
		  return false;
	});
		$(document).ready(function() {
					$(".up_down").click(function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
					$(".poNumber").click(function(){
						$(".loader-sumadhura").show();
					});
					$(".chexkbox_siteall1").click(function () {
				   	     $('.chexkbox_site').not(this).prop('checked', this.checked);
				   	 });
				});

		$('.chexkbox_siteall').click(function(){
            if($(this).prop("checked") == true){
            	$(".hide_select_site").toggle(500);
            	$(".checkbox_sitelabel1").toggle(500);
            }
            else if($(this).prop("checked") == false){
            	$(".hide_select_site").hide(500);
            	$(".checkbox_sitelabel1").hide(500);
            }
        });
		// getting the vendor data set in that
		 function getVendorId(){
        		$("#saveBtnId").attr("disabled", true);
        	  $.ajax({
        	  url : "./getVendorDetails.jsp",
        	  type : "get",
        	  contentType : "application/json",
        	  success : function(data) {
        	  		$("#VendorNameId").autocomplete({
        		  		source : data,
	    		  		select: function( event, ui ) { debugger;
	    		  			$('.loader-sumadhura').show();
	    	    			var value = ui.item.value;
	    			  		value = value.replace("&", "$$$");
	    			  		setVendorData(value);
	    		  		 }
        		  	});
        	  },
        	  error:  function(data, status, er){
        		  //alert(data+"_"+status+"_"+er);
        		  }
        	  });
        	};
      

        function setVendorData(vName) {
        	var url = "loadAndSetVendorInfo.spring?vendName="+vName;
        	  
        	if(window.XMLHttpRequest) {
        		request = new XMLHttpRequest();	  
        	}  
        	else if(window.ActiveXObject) {
        		request = new ActiveXObject("Microsoft.XMLHTTP");  
        	}	  
        	try {
        		request.onreadystatechange = setVedData;
        		request.open("POST", url, true);
        		request.send();  
        	}
        	catch(e) {
        		alert("Unable to connect to server!");
        	}
        }

        function setVedData() {
        	if(request.readyState == 4 && request.status == 200) {
        		var resp = request.responseText;
        		resp = resp.trim();
        		var vendorId = resp.split("|")[0];
        		var vendorAddress = resp.split("|")[1];
        		var vendorGsinNo = resp.split("|")[2];
			$("#saveBtnId").removeAttr("disabled");
        		$('.loader-sumadhura').hide();
        		$("#vendorIdId").val(vendorId);
        		$("#VendorAddress").val(vendorAddress);
        		$("#GSTINNumber").val(vendorGsinNo);			
        	}
        }
		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
