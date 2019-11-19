<%@page import="java.util.Arrays"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<jsp:include page="CacheClear.jsp" />  
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

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
table.dataTable {border-collapse:collapse !important;}
.info td{border:1px solid #000 !important}
.success,.error {text-align: center;font-size: 16px;}
.border-inwards-box label{text-align:left !important;}
.form-control{height:32px;border:1px solid #000;}
.anchor-class{color:#0000ff !important;}
.textOverflow{white-space: nowrap;overflow: hidden;text-overflow: ellipsis;}
</style>

<script type="text/javascript">


	function validate() {debugger;
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;

		if (from == "" && to == "") {
			alert("Please select From Date or To Date !");
			return false;
		}
		
		var searchType=document.getElementById("searchType").value;
			if(searchType=='ADMIN'){
			var siteId=document.getElementById("dropdown_SiteId").value;
			if(siteId==""||siteId==null){
				alert("Please select the Site !");
				return false;	
			}
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
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Issue</a></li>
								<li class="breadcrumb-item active">Issue Details</li>
							</ol>
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
				        <div class="col-md-12">						
						<label class="success text-center center-block"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
							<form:form  id="indentIssueFormId" action="getIndentViewDts.spring" class="form-horizontal">
								<div class="col-md-12 border-inwards-box">
									<div class="col-md-4 col-sm-6 col-xs-12">
										 <div class="form-group">
											<label class="col-md-6 col-sm-12 col-xs-12">From Date :</label>
											<div class="col-md-6 col-sm-12 col-xs-12 input-group">
												<input id="ReqDateId1" type="text" name="fromDate" value="${fromDate}" class="form-control readonly-color" autocomplete="off" readonly="true">
												<label class="input-group-addon btn" for="ReqDateId1">	<span class="fa fa-calendar"></span></label>
											</div>
										</div>
									</div>
									<div class="col-md-4 col-sm-6 col-xs-12">
										 <div class="form-group">
											<label class="col-md-6 col-sm-12 col-xs-12">To Date :</label>
											<div class="col-md-6 col-sm-12 col-xs-12 input-group">
												<input id="ReqDateId2" type="text" name="toDate" value="${toDate}" class="form-control readonly-color" autocomplete="off" readonly="true">
												<label class="input-group-addon btn" for="ReqDateId2">	<span class="fa fa-calendar"></span></label>
											</div>
										</div>
									</div>	
									<%
										String strSearchType = request.getAttribute("SEARCHTYPE") == null ? "" : request.getAttribute("SEARCHTYPE").toString();
									  
										log("reposrtd/cumulative Stock"); %>
										<input id="searchType" type="hidden" name="searchType" value="<%=strSearchType%>" class="form-control" autocomplete="off">
									<%	if (strSearchType.equals("ADMIN")) {
										
									%>
									<div class="col-md-4 col-sm-6 col-xs-12">
								      <div class="form-group">
											<label class=" col-md-6 col-sm-12 col-xs-12">Site :</label> 
									 		<div class="col-md-6 col-sm-12 col-xs-12">
												<%
													List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
														String strSiteId = "";
														String strSiteName = "";
														String selectedSiteId=request.getAttribute("strSiteId")==null?"":request.getAttribute("strSiteId").toString();
												%> <select id="dropdown_SiteId" name="dropdown_SiteId" class="custom-combobox form-control "> <!-- indentavailselect -->
													<option value=""></option>
												<%
													for (Map siteList : totalSiteList) {
													strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
													strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
													if(selectedSiteId.equals(strSiteId)){
													%>
														<option value="<%=strSiteId%>" selected="selected"><%=strSiteName%></option>
													<%}else{
													
													%>
														<option value="<%=strSiteId%>"><%=strSiteName%></option>
													<%
												}}
												%>
							
									          </select>
									    </div>
									</div>
							    </div>
								<%
								 	}
								 %>
										
								<div class="col-md-4 col-sm-6 col-xs-12">
								 <div class="form-group">
									<label class="col-md-6 col-sm-12 col-xs-12">Block :</label>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<select name="blockIdName" id="BlockId" onchange="populateFloor(this)" class="form-control">
									  					<option value="">--Select--</option> 
									  				<%
										    			Map<String, String> blocksMap = (Map<String, String>)request.getAttribute("blocksMap");
									  					String blockId=request.getAttribute("blockId")==null?"":request.getAttribute("blockId").toString();
										    			for(Map.Entry<String, String> blocks : blocksMap.entrySet()) {
															String availableBlocks = blocks.getKey()+"$"+blocks.getValue();
															if(blockId.equals(blocks.getKey())){
													%>
																<option value="<%= availableBlocks %>" selected="selected"><%= blocks.getValue() %></option>
											    	<% }else{
													%>
															<option value="<%= availableBlocks %>"><%= blocks.getValue() %></option>
										    		<% }} %>
													</select>
										
										
									</div>
								</div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12">
								 <div class="form-group">
									<label class="col-md-6 col-sm-12 col-xs-12">Floor :</label>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<%-- <input id="floor" type="text" name="floor" value="${floor}" class="form-control" autocomplete="off"> --%>
										
										<select name="floorIdName" id="FloorId" onchange="populateFlat(this)" class="form-control">
							  						<option value="">--Select--</option>
							  						<%
										    		String floorMap = (String)request.getAttribute("floorMap");
							  						String floordataList[]=floorMap.split("\\|");
							  						String floorId = request.getAttribute("floorId")==null?"":request.getAttribute("floorId").toString();
							  						log("floordataList "+Arrays.toString(floordataList)+" floorMap"+floorMap);
							  						if(floorMap.length()!=0)
							  						for (String flatdata : floordataList) {
							  							String tempData[]=flatdata.split("@@");
														String availableBlocks =tempData[0]+"$"+tempData[1];
														if(floorId.equals(tempData[0])){
													%>
														<option value="<%= availableBlocks %>" selected="selected"><%= tempData[1]%></option>
									    			<% }else{	
													%>
															<option value="<%= availableBlocks %>"><%=tempData[1]%></option>
										    		<% }} %>
									    	    </select>
									</div>
								</div>
								</div>
		
								<div class="col-md-4 col-sm-6 col-xs-12">
								 <div class="form-group">
									<label class="col-md-6 col-sm-12 col-xs-12">Flat :</label>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<select name="flatIdName" id="FlatNumberId"  class="form-control">
							  				<option value="">--Select--</option>
							  						<%
							  						String flatMap = (String)request.getAttribute("flatMap");
							  						String flatdataList[]=flatMap.split("\\|");
							  						String flatId = request.getAttribute("flatId")==null?"":request.getAttribute("flatId").toString();
							  						log(Arrays.toString(flatdataList));
							  						if(flatMap.length()!=0)
							  						for (String flatdata : flatdataList) {
							  							String tempData[]=flatdata.split("@@");
														String availableBlocks =tempData[0]+"$"+tempData[1];
														if(flatId.equals(tempData[0])){
													%>
															<option value="<%= availableBlocks %>" selected="selected"><%= tempData[1]%></option>
											    	<% }else{	
													%>
															<option value="<%= availableBlocks %>"><%=tempData[1] %></option>
										    		<% }} %>
							  			
									   </select> 
										
									</div>
								</div>
								</div>		
								<div class="col-md-4 col-sm-6 col-xs-12">
								 <div class="form-group">
									<label class="col-md-6 col-sm-12 col-xs-12">Issue Type :</label>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<select id="issueType" name="issueType" class="form-control">
										${selectBoxOption}
										</select>
									</div>
								</div>
								</div>		
								<div class="col-md-4 col-sm-6 col-xs-12">
								 <div class="form-group">
									<label class="col-md-6 col-sm-12 col-xs-12">Contractor Name :</label>
									<div class="col-md-6 col-sm-12 col-xs-12">
										<input  type="text" name="contractorName" id="ContractorNameId"  onkeyup="return populateData();"  value="${contractorName}" class="form-control" autocomplete="off">
										<input type="hidden" name="contractorId" id="contractorId" value="${contractorId }">
									</div>
								</div>
								</div>
							<div class="col-md-12 col-sm-12 col-xs-12 text-center center-block">
							    <input type="submit" value="Submit" id="saveBtnId" onclick="return validate();" class="btn btn-warning">
						   </div>
						   <div>${displayErrMsg}</div>
						  </div>
						</form:form>
                       <div class="clearfix"></div>
						<%
						   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
						   if(isShowGrid.equals("true")){
						%>
						<div class="table-responsive Mrgtop20 dragscroll">
							<table id="tblnotification"	class="table table-new" cellspacing="0" style="width:3000px;">
							  <thead>
								<tr>
									<th>S.NO</th>
									<th>Issued date</th>
									<th>Req Id</th>
									<th>Issue Type</th>
									<th>Work OrderNumber</th>		
				    				<th>Product Name</th>
				    				<th>Sub Product Name</th>
				    				<th style="max-width:200px;">Child Product Name</th>
				    				<th>Measurement</th>
				    				<th>Issued Quantity</th>
				    				<!-- <th>Basic Amount</th>
				    				<th>Total Amount After Tax</th> -->
				    				<th>Total Amount After All Charges</th>
				    				<th>Employee Id</th>
				    				<th>Employee Name</th>
				    				<th>Contractor  Name</th>
				    				<th>Vendor Name</th>
				    			<!-- 	<th>Issuer Name</th> -->
				    				<th>Slip number</th>
				    				<th>Block Name</th>
				    				<th>Floor Name</th>
				    				<th>Flat Name</th>
				    				<th>Issued Time</th>
				    				<th>Expiry Date</th>
				    				<th>Remarks</th>
				    				<!-- <th>Floor Id</th> -->
				    			</tr>
							  </thead>
							<tbody>
							   <c:forEach items="${indentIssueData}" var="element">  
							     <tr>
									<td>${element.serialNo}</td>
								    <td>${element.entryDate}</td>
								    <td><a href="getIndentIssuedDetails.spring?indentEntryId=${element.indentEntryId}&dropdown_SiteId=${element.siteId}&indentType=${element.indentType}&fromDate=N/A&toDate=N/A&loadSingleData=true&moduleName=GetIssueDetails" class="anchor-class">${element.indentEntryId}</a></td>
								    <td>${element.issueType}</td>
								    <td><a href="getMyCompletedWorkOrder.spring?WorkOrderNo=${element.workOrderNumber}&workOrderIssueNo=&site_id=${element.siteId}&operType=1&isUpdateWOPage=false" class="anchor-class">${element.workOrderNumber}</a></td>
									<td>${element.productName}</td>
									<td>${element.subProdName}</td>
									<td title="<c:out value="${element.childProdName}"/>" class="textOverflow" style="max-width:200px;">${element.childProdName}</td>
									<td>${element.measurementName}</td>					
									<td class="valor5 text-right">${element.issueQTY}</td>					
									<td class="valor2 text-right" id="" style="color: black;">${element.totalAmt}</td>
									<td>${element.requesterId}</td>
									<td>${element.requesterName}</td>
									<td>${element.contractorName}</td>
									<td>${element.vendorName}</td>
									<%-- <td>${element.issuerName}</td> --%>
									<td>${element.strSlipNumber}</td>
									<td>${element.block_Name}</td>
									<td>${element.floor_Name}</td>
									<td>${element.flat_Name}</td>
									<td>${element.time}</td>
									<td>${element.expiryDate}</td>
									<td title="${element.remarks}" class="textOverflow" style="max-width:200px;">${element.remarks}</td>
									<%-- <td>${element.floorId}</td> --%>
								</tr>								
							   </c:forEach>
							 </tbody>
							<tfoot>
						        <tr class="info">
						            <td colspan="9" class="text-right subTtl">TOTAL:</td>
						             <td class="issuedQuantitytotal text-right subTtl"></td>
						            <!--<td class="basicAmount text-right subTtl"></td>
						              <td class="totalaftertax text-right subTtl"></td>  -->
						            <td class="total text-right subTtl"></td>
						            <td></td>
						             <td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
						        </tr>
							</tfoot>
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
	<!-- <script type="text/javascript" src="js/dragscroll.js"></script> -->
	<!-- <script src="js/sidebar-resp.js" type="text/javascript"></script> -->
	<script>
	$(".anchor-class").click(function(ev){
		if(ev.ctrlKey==false && ev.shiftKey==false){
			$(".loader-sumadhura").show();
		}
	});
	//Load Block for common site wise issue details
	function getBlockDetails(){
		
	}
	
	
	
	
	/********** Method for load the Contractors**************/
	function populateData() {
		debugger;
	var contName=$("#ContractorNameId").val();
	 var url = "loadAndSetContractorInfo.spring?contractorName="+contName;

	  $.ajax({
	  url : url,
	  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "get",
	  Cdata : "",
	  contentType : "application/json",
	  success : function(data) {
		  console.log(data);
		  Cdata = data.split("@@");
		  
		  var resultArray = [];
		  for(var i=0;i<Cdata.length;i++){
		      resultArray.push(Cdata[i].split("@@")[0]);
		  }
	  		$("#ContractorNameId").autocomplete({
		  		source : resultArray,
		  		change: function (event, ui) {
	                if(!ui.item){
	               	//if list item not selected then make the text box null	
	               	 $("#ContractorNameId").val("");
	                }
	              },
		  		select: function (event, ui) {
	                AutoCompleteSelectHandler(event, ui);
	            }

		  	});
	  },
	  error:  function(data, status, er){
		 // alert(data+"_"+status+"_"+er);
		  }
	  });

	//code for selected text
		function AutoCompleteSelectHandler(event, ui){               
		    var selectedObj = ui.item;       
		    isTextSelect="true";
		  
		  var contractorName=selectedObj.value;
		 
			 var url = "loadAndSetVendorInfoForWO.spring";
			 $.ajax({
				  url : url,
				  type : "get",
				 data:{
					 contractorName:contractorName,
					 loadcontractorData:true	 
				 },
				  contentType : "application/json",
				  success : function(data) {
					  $("#contractorName").val(contractorName);
					  if(data!=""||data!="null"){
						debugger;
						var contractorData=data[0].split("@@");
						var contractorId=contractorData[0];
						$("#contractorId").val(contractorId);
					
						}
				  },
				  error:  function(data, status, er){
					  alert(data+"_"+status+"_"+er);
					  }
				  });
		}
	};
	function getAjaxObject() {
		
		var request = null;
		
		if(window.XMLHttpRequest) {
			request = new XMLHttpRequest();
		}  
		else if(window.ActiveXObject) {
			request = new ActiveXObject("Microsoft.XMLHTTP");  
		}
		return request;
	}

	function populateFloor(blockObj) {
		
		var blockId = blockObj.id;
		//alert(blockId);
		
		var selectedBlock = document.getElementById(blockId);
		selectedBlock = selectedBlock.options[selectedBlock.selectedIndex].value;
		
		//alert(selectedBlock);
		
		if(selectedBlock == "" || selectedBlock == '' || selectedBlock == null) {
			
			document.getElementById("FloorId").value = "";
			document.getElementById("FloorId").options.length = 1;
			
			document.getElementById("FlatNumberId").value = "";
			document.getElementById("FlatNumberId").options.length = 1;
		}
		else {
			var selectedBlockId = selectedBlock.split("$")[0];		
			//alert("Block Id = "+selectedBlockId);		
			//var selectedBlockName = selectedBlock.split("$")[1];
			//alert("Block Name = "+selectedBlockName);
			getFloorDetails(selectedBlockId);		
		}
	}

	function getFloorDetails(blockId) {
		
		var url = "getFloorDetails.spring?blockId="+blockId;
		//alert(url);
		  
		var request = getAjaxObject();
		
		try {
			request.onreadystatechange = function() {
				
				if(request.readyState == 4 && request.status == 200) {
	                
					var resp = request.responseText;
					resp = resp.trim();
					
					var spltData = resp.split("|");
			    	//alert(spltData);
					
			    	available = new Array();
			    	for(var j=0; j<spltData.length; j++) {
			    		available[j] = spltData[j];
			    	}
			    	
			    	var selectBox = document.getElementById("FloorId");
				    //alert(selectBox);
				    
				    //Removing previous options from select box - Start
				    if(document.getElementById("FloorId") != null && document.getElementById("FloorId").options.length > 0) {
				    	document.getElementById("FloorId").options.length = 0;
				    }
				    //Removing previous options from select box - End
				    
				    initOpt = document.createElement("option");
				    initOpt.text = "--Select--";
				    initOpt.value = "";
				    selectBox.appendChild(initOpt);
				    
				    var defaultOption;
				    var data;
				    
				    for(var i=0; i<available.length; i++) {
				    	defaultOption = document.createElement("option");
				    	data = available[i].split("@@");
				    	if(data[0] != "" && data[0] != null && data[0] != '') {
				    		var floorIdAndName = data[0]+"$"+data[1];
				    		defaultOption.text = data[1];
				    	    defaultOption.value = floorIdAndName;
				    	    selectBox.appendChild(defaultOption);
				    	}
				    }
	            }
	        };		
			request.open("POST", url, false);
			request.send();  
		}
		catch(e) {
			alert("Unable to connect to server!");
		}
	}

	function populateFlat(floorObj) {
		
		var floorId = floorObj.id;
		//alert(blockId);
		
		var selectedFloor = document.getElementById(floorId);
		selectedFloor = selectedFloor.options[selectedFloor.selectedIndex].value;
		
		//alert(selectedFloor);
		
		if(selectedFloor == "" || selectedFloor == '' || selectedFloor == null) {
			document.getElementById("FlatNumberId").value = "";
			document.getElementById("FlatNumberId").options.length = 1;
		}
		else {
			var selectedFloorId = selectedFloor.split("$")[0];		
			//alert("Floor Id = "+selectedFloorId);		
			//var selectedFloorName = selectedFloor.split("$")[1];
			//alert("Floor Name = "+selectedFloorName);
			getFlatDetails(selectedFloorId);		
		}
	}

	function getFlatDetails(floorId) {
		
		var url = "getFlatDetails.spring?floorId="+floorId;
		//alert(url);
		  
		var request = getAjaxObject();
		
		try {
			request.onreadystatechange = function() {
				
				if(request.readyState == 4 && request.status == 200) {

					var resp = request.responseText;
					resp = resp.trim();
					
					var spltData = resp.split("|");
			    	//alert(spltData);
					
			    	available = new Array();
			    	for(var j=0; j<spltData.length; j++) {
			    		available[j] = spltData[j];
			    	}
			    	
			    	var selectBox = document.getElementById("FlatNumberId");
				    //alert(selectBox);
				    
				    //Removing previous options from select box - Start
				    if(document.getElementById("FlatNumberId") != null && document.getElementById("FlatNumberId").options.length > 0) {
				    	document.getElementById("FlatNumberId").options.length = 0;
				    }
				    //Removing previous options from select box - End
				    
				    initOpt = document.createElement("option");
				    initOpt.text = "--Select--";
				    initOpt.value = "";
				    selectBox.appendChild(initOpt);
				    
				    var defaultOption;
				    var data;
				    
				    for(var i=0; i<available.length; i++) {
				    	defaultOption = document.createElement("option");
				    	data = available[i].split("@@");
				    	if(data[0] != "" && data[0] != null && data[0] != '') {
				    		var flatIdAndName = data[0]+"$"+data[1];	    		
				    		defaultOption.text = data[1];
				    	    defaultOption.value = flatIdAndName;
				    	    selectBox.appendChild(defaultOption);
				    	}
				    }
	            }
	        };		
			request.open("POST", url, false);
			request.send();  
		}
		catch(e) {
			alert("Unable to connect to server!");
		}
	}


	
	
	
	
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
		$(document).ready(function() { $(".up_down").click( function() { 
			                    $(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					        $('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				    });
		           //$('#tblnotification').stacktable({myClass:'stacktable small-only'});
/* *************** SUB Total calculations ********* */
		
		$(document).ready(function () {
			debugger;
			var val = $('#tblnotification').find('tbody').find('tr');
			var tAmount = 0;
			var taxafterAmount = 0;
			var basicAmount=0;
			var issuedQuantitytotal=0;
			jQuery.each(val,function(index,item){
				tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
				taxafterAmount = taxafterAmount + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') ));
				basicAmount = basicAmount + (parseFloat(jQuery(item).find('.valor4').text().replace(/,/g,'') ));
				issuedQuantitytotal = issuedQuantitytotal + (parseFloat(jQuery(item).find('.valor5').text().replace(/,/g,'') ));
				/* $(".issuedQuantitytotal").text(issuedQuantitytotal); */
			});
			var numberwithcomma=inrFormat(tAmount.toFixed(2));
			$(".total").text(numberwithcomma);
			var numberwithcommas=inrFormat(taxafterAmount.toFixed(2));
			$(".totalaftertax").text(numberwithcommas);
			
			var numberwithBasic=inrFormat(basicAmount.toFixed(2));
			$(".basicAmount").text(numberwithBasic);
			
			var IssuedQuantitywithComma=inrFormat(issuedQuantitytotal.toFixed(2));
			$(".issuedQuantitytotal").text(IssuedQuantitywithComma);
			
			$(document).on("click", " .pagination",function(){
				  var val = $('#tblnotification').find('tbody').find('tr');
				  var tAmount = 0;
				  var taxafterAmount = 0;
				  var basicAmount=0;
				  var issuedQuantitytotal=0;
				  jQuery.each(val,function(index,item){
				    tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				    taxafterAmount = taxafterAmount + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
				    basicAmount = basicAmount + (parseFloat(jQuery(item).find('.valor4').text().replace(/,/g,'') ));
				    issuedQuantitytotal = issuedQuantitytotal + (parseFloat(jQuery(item).find('.valor5').text().replace(/,/g,'') ));
				    $(".issuedQuantitytotal").text(issuedQuantitytotal);
				  });
			
				  var numberwithcomma=inrFormat(tAmount.toFixed(2));
					$(".total").text(numberwithcomma);
					var numberwithcommas=inrFormat(taxafterAmount.toFixed(2));
					$(".totalaftertax").text(numberwithcommas);
					var numberwithBasic=inrFormat(basicAmount.toFixed(2));
					$(".basicAmount").text(numberwithBasic);
					var IssuedQuantitywithComma=inrFormat(issuedQuantitytotal.toFixed(2));
					$(".issuedQuantitytotal").text(IssuedQuantitywithComma);
					
			});
			
			$(document).on("keyup", ".input-sm",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;
				var taxafterAmount = 0;
				var basicAmount=0;
				var issuedQuantitytotal=0;
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					 taxafterAmount = taxafterAmount + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
					 basicAmount = basicAmount + (parseFloat(jQuery(item).find('.valor4').text().replace(/,/g,'') ||0));
					 issuedQuantitytotal = issuedQuantitytotal + (parseFloat(jQuery(item).find('.valor5').text().replace(/,/g,'') ||0));
					 $(".issuedQuantitytotal").text(issuedQuantitytotal);
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
				var numberwithcommas=inrFormat(taxafterAmount.toFixed(2));
				$(".totalaftertax").text(numberwithcommas);
				var numberwithBasic=inrFormat(basicAmount.toFixed(2));
				$(".basicAmount").text(numberwithBasic);
				var IssuedQuantitywithComma=inrFormat(issuedQuantitytotal.toFixed(2));
				$(".issuedQuantitytotal").text(IssuedQuantitywithComma);
				
			});
			var serachType="${SEARCHTYPE}";
			if(serachType=="ADMIN"){
			$("#dropdown_SiteId").on("change",function(){
				var siteId=$(this).val();
				debugger;
				$.ajax({
					url : "getBlockDetails.spring",
					type : "get",
					data:{siteId:siteId},
					success : function(data) {
						debugger;
						var str="";
						str+="<option value=''>--Select--</option> ";
						$.each(data,function(key,value){
							debugger;
							console.log(value);
							var availableBlocks = key+"$"+value;
							str+="<option value='"+availableBlocks+"'>"+value+"</option> ";
						});
						$("#BlockId").html(str);
					}
				});
				
			});
		}//serachtype condition
			
			$(document).on("change", " .dataTables_length",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;
				var taxafterAmount = 0;
				var basicAmount=0;
				var issuedQuantitytotal=0;
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
					 taxafterAmount = taxafterAmount + (parseFloat(jQuery(item).find('.valor3').text().replace(/,/g,'') || 0));
					 basicAmount = basicAmount + (parseFloat(jQuery(item).find('.valor4').text().replace(/,/g,'') ||0));
					 issuedQuantitytotal = issuedQuantitytotal + (parseFloat(jQuery(item).find('.valor5').text().replace(/,/g,'') ||0));
					 $(".issuedQuantitytotal").text(issuedQuantitytotal);
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
				var numberwithcommas=inrFormat(taxafterAmount.toFixed(2));
				$(".totalaftertax").text(numberwithcommas);
				
				var numberwithBasic=inrFormat(basicAmount.toFixed(2));
				$(".basicAmount").text(numberwithBasic);
				var IssuedQuantitywithComma=inrFormat(issuedQuantitytotal.toFixed(2));
				$(".issuedQuantitytotal").text(IssuedQuantitywithComma);
				
			});

      });
		//converting number with comma
		function inrFormat(nStr) { // nStr is the input string
		     nStr += '';
		     x = nStr.split('.');
		     x1 = x[0];
		     x2 = x.length > 1 ? '.' + x[1] : '';
		     var rgx = /(\d+)(\d{3})/;
		     var z = 0;
		     var len = String(x1).length;
		     var num = parseInt((len/2)-1);
		 
		      while (rgx.test(x1)){
		        if(z > 0){
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		        }
		        else{
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		          rgx = /(\d+)(\d{2})/;
		        }
		        z++;
		        num--;
		        if(num == 0){
		          break;
		        }
		      }
		     return x1 + x2;
		 } 
	</script>

</body>
</html>
