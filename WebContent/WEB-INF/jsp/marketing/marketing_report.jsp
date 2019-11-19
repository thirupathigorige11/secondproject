<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/select2.min.css"  rel="stylesheet" type="text/css">
		<jsp:include page="./../CacheClear.jsp" />  
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/select2.min.js"></script>
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
		<style>
			.chexkbox_siteall {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
	 		.chexkbox_siteall1 {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
	 		.chexkbox_site {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;}
	 		.checkbox_sitelabel {margin-top: 6px;margin-right: 15px;float: left;font-size: 15px;}
			.lableClass{font-size: 16px;margin-top: -1%;}
			.spinnercls{width: 30%;display: inline-block;position: absolute;left: 35%;z-index: 99999;top: 15%;}
			svg{
			 width:800px !important;			
			}
			.select2-selection{
				overflow: hidden !important;
			    max-height: 85px !important;
			    overflow-y: scroll !important;
			}
		</style>
  </head>
<body  class="nav-md">
	<div class="container body" id="mainDivId">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">
					<div class="clearfix"></div>
					<jsp:include page="../SideMenu.jsp" />  
				</div>
			</div>
			<jsp:include page="../TopMenu.jsp" />  
			<div class="right_col" role="main">
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Marketing Report</li>
					</ol>
				</div>
				<div>
					<div class="col-md-12">
			 		 <div class="loader-ims" id="loaderId" style="display:none;"> <!--  -->
		       		  <div class="lds-ims">
			    		   <div></div><div></div><div></div><div></div><div></div><div></div></div>
		       			   <div id="loadingimsMessage">Loading...</div>
	           		  </div>	
	           		   <div class="clearfix"></div>
	           		  <div class="col-md-12 viewpaymentpending-container">	
	           		  	<div class="col-md-12 no-padding-right">     		 
							<div class="col-md-4 col-xs-12 col-sm-12 text-left no-padding-left no-padding-right">
								<div class="form-group">
									<label class="control-label col-md-5 lableClass" style="margin-top: 1%;">From Date :</label>
									<div class="col-md-7 input-group  no-padding-right">
										<input type="text" id="fromDate" name="toDate" class="form-control deliveryDate readonly-color" onchange="fromDateChange()" autocomplete="off" readonly="true" placeholder="dd-mm-yy">
										<label class='btn input-group-addon'  for='fromDate'><span class='fa fa-calendar'></span></label>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-xs-12 col-sm-12 text-left no-padding-left no-padding-right">
								<div class="form-group">
									<label class="control-label col-md-5 lableClass" style="margin-top: 1%;">To Date :</label>
									<div class="col-md-7 input-group  no-padding-right">
										<input type="text" id="toDate" name="toDate" class="form-control deliveryDate readonly-color" onchange="toDateChange()" autocomplete="off" readonly="true" placeholder="dd-mm-yy">
										<label class='btn input-group-addon'  for='toDate'><span class='fa fa-calendar'></span></label>
									</div>
								</div>
							</div>	
							<div class="col-md-4 col-xs-12 col-sm-12 text-left no-padding-left no-padding-right">
								
							<label class="control-label col-md-4 lableClass text-left" style="margin-top: 2px;">Site Names :</label>
								 <div class="col-md-8 selectRow"> <select   name="sitenames"  id="sitenames" data-placeholder="Select an option" class="select2-offscreen" multiple style="width:100%;">
		           				</select></div>
		           				
		           				</div>
							</div>					
						<div class="col-md-12 col-sm-12 no-padding-left">
							<%-- <div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class="chexkbox_siteall" /><label class="checkbox_sitelabel "><strong>Show Sites</strong></label></div></div>
							<div class="col-md-12 text-left"><div class="col-md-12 hide_select_site" style="display:none;"><input type="checkbox" class=" chexkbox_siteall1"><label class="checkbox_sitelabel"> Select All</label></div></div>
							<div class="col-md-12 text-left hide_select_site" style="display:none;">
								<c:forEach items="${siteDetails}" var="site">
					        	    <div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="${site.key}"></div><div class="col-md-10"><label class="checkbox_sitelabel">${site.value}</label></div></div>
								</c:forEach>
							</div> --%>
							
						</div>
						<div class="col-md-12 text-center center-block" style="margin-top: 20px;">
							<button type="button" class="btn btn-warning" id="saveBtnId" onclick="submitData()">Submit</button>
						</div>					
					</div>
					
					<div class="col-md-12" style="margin-bottom:30px;border:1px solid;">
						<img src="images/spinner.gif" class="spinnercls">
						<div class="col-md-offset-1 col-md-11 col-xs-12 col-sm-12" style="height: 500px;text-align: center;"  id="myPie"></div>
						<div class="col-md-offset-1 col-md-11 col-xs-12 col-sm-12" style="height: 500px;text-align: center;display:none;"  id="sitesData"></div>
						<p class="text-center" id="errorData" style="padding: 10px;font-size: 20px;font-weight: bold;"></p>
						<p class="col-md-12" id="note" style="font-size: 17px;">Note: <span id="noteSpan" style="font-size: 14px;font-weight: bold;"></span></p>
						<input type="hidden" name="productData" id="productData" value="${requestScope['productData']}"/>
					</div>            
	          		 </div>
	           </div>
	         </div>
	       </div>
	   </div>
    <script src="js/custom.js"></script>
    <script src="js/d3.min.js"></script>
	<script src="js/d3pie.min.js"></script>
	<script>
		$(document).ready(function(){	
			$("#sitenames").select2();
			 $("#toDate").datepicker({
				dateFormat: 'dd-M-y',
				changeMonth: true,
				changeYear: true
			});
			$(function() {
				$("#fromDate").datepicker({
					dateFormat: 'dd-M-y',
					changeMonth: true,
					changeYear: true
				});
			}); 
			debugger;
			var data=$("#productData").val();
		});
		//from date and to date method
		function fromDateChange(){
			var x=$('#fromDate').datepicker("getDate");
			$("#toDate").datepicker( "option", "minDate",x ); 	
		}
		function toDateChange(){
			var x=$('#toDate').datepicker("getDate");
			$("#fromDate").datepicker( "option", "maxDate",x ); 	
		}
			loadPieChart();
		function loadPieChart(){
			
			 $.ajax({
				  url :"defaultProductDetais.spring",
				  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
				  type : "get",
				  Cdata : "",
				  contentType : "application/json",
				  success : function(data) {
					  debugger;
					  if(data!=""){
				 var productData=JSON.parse(data).xml.data;
				// productData=JSON.stringify(productData);
				 var data1=[];
					if(productData.label){
						data1.push({"label":productData.label,"value":parseInt(productData.value)});
					}else{									 
						 for(var i=0;i<productData.length;i++){
							 data1.push({"label":productData[i].label,"value":parseInt(productData[i].value)});
						 }
					}	
				 console.log("data: "+data1);
				 $("#myPie").html('');
			 	var pie = new d3pie("myPie", {
			 		header: {
			 		title: {
			 		text: "Marketing Major Heads Data"
			 		}
			 		},
			 		size: {
						"canvasWidth": 700
					},
			 		data: {
			 		content: data1
			 		},
			 		tooltips: {
			 			"enabled": true,
			 			"type": "placeholder",
			 			"string": "{label}: {value} ({percentage}%)",
			 					 "styles": {
			 						"backgroundColor": "#000000",
			 						"borderRadius": 5,
			 						"fontSize": 12
			 						}
			 		 },
			 		callbacks: {
			 		onMouseoverSegment: function(info) {
			 		console.log("mouse in", info);
			 		},
			 		onMouseoutSegment: function(info) {
			 		console.log("mouseout:", info);
			 		}
			 		}

			 		});
			 	debugger;
			 	var sitesFromBackend=JSON.parse(data).xml.notedata.sitenames;
			 	sitesFromBackend = sitesFromBackend.replace("[", '');
			 	sitesFromBackend = sitesFromBackend.replace("]", '');
			 	    var siteNamesForNote='';
			 	    for(var i=0;i<sitesFromBackend.length;i++){
			 	    	siteNamesForNote+=sitesFromBackend[i]+",";
			 	    }
			 		var noteData="A Report on Marketing Expenditure for Projects "+ sitesFromBackend +" from "+JSON.parse(data).xml.notedata.fromdate+" to "+JSON.parse(data).xml.notedata.todate+".";
			 		 $("#noteSpan").html(noteData);  
			 		 $("#note").show();  
				 	$(".spinnercls").hide();
					  }else{
						  $(".spinnercls").hide();
			  			  $("#errorData").html("No Data found.");
			  			  $("#note").hide();  
					  }
				  },
				  error:  function(data, status, er){
					  $(".spinnercls").hide();
					  alert(data+"_"+status+"_"+er);
				  }
			});
			
		}
		
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
		 $(".chexkbox_siteall1").click(function () {
	   	     $('.chexkbox_site').not(this).prop('checked', this.checked);
	   	 });
		 
		 function submitData(){debugger;
			 var fromDate=$("#fromDate").val();
			 var toDate=$("#toDate").val();	
			 var SiteData=$("#sitenames").val();			
	        console.log("SiteData: "+SiteData);
	       if(fromDate=="" && toDate=="" && SiteData==null){
	    	   alert("Please select atleat one field.");
	    	   return false;
	       }
	       if(fromDate=="" && toDate==""){
	    	   alert("Please select from date or to date.");
	    	   return false;
	       }
	       $("#note").hide(); 
	       $(".spinnercls").show();
	       $("#myPie").hide();
			 $.ajax({
				  url :'selectedProductDetailsForGraph.spring?fromDate='+fromDate+"&toDate="+toDate+"&SiteData="+SiteData,
				  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
				  type : "get",
				  Cdata : "",
				  contentType : "application/json",
				  success : function(data) {
					  debugger;
					  if(data!=""){
							  var productData=JSON.parse(data).xml.data;
								// productData=JSON.stringify(productData);
								var data1=[];
								if(productData.label){
									data1.push({"label":productData.label,"value":parseInt(productData.value)});
								}else{									 
									 for(var i=0;i<productData.length;i++){
										 data1.push({"label":productData[i].label,"value":parseInt(productData[i].value)});
									 }
								}								
								 console.log("data: "+data1);
		
							 /* 	 var data=[
								 	{"label":"Corporate Programme Online","value":224200},
								 	{"label":"Brand Ambassador","value":3409185},
								 	{"label":"ATL","value":16461747},
								 	{"label":"Administrative","value":22032},
								 	{"label":"Calling Activity","value":84957},
								 	{"label":"Research & Development","value":1180000},
								 	{"label":"Online Marketing","value":9294331},
								 	{"label":"BTL","value":9215262},
								 	{"label":"Public Relations","value":2824269},
								 	{"label":"Assets","value":99504},
								 	{"label":"Brand Management","value":18618872},
								 	{"label":"Production","value":3174470},
								 	{"label":"Events","value":585974}
								 ];  */ 
								 $("#sitesData").html('');
								 $("#sitesData").css({
					  					"height": "500px"
					  				});
							 	var pie = new d3pie("sitesData", {
							 		header: {
							 		title: {
							 		text: "Marketing Major Heads Data"
							 		}
							 		},
							 		size: {
										"canvasWidth": 700
									}, 
							 		data: {
							 		content: data1
							 		},
							 		tooltips: {
							 			"enabled": true,
							 			"type": "placeholder",
							 			"string": "{label}: {value} ({percentage}%)",
							 					 "styles": {
							 						"backgroundColor": "#000000",
							 						"borderRadius": 5,
							 						"fontSize": 12
							 						}
							 		 },
							 		//Here further operations/animations can be added like click event, cut out the clicked pie section.
							 		callbacks: {
							 		onMouseoverSegment: function(info) {
							 		console.log("mouse in", info);
							 		},
							 		onMouseoutSegment: function(info) {
							 		console.log("mouseout:", info);
							 		}
							 		}
		
							 		});
								 $("#noteSpan").html("");
								 var sitesFromBackend=JSON.parse(data).xml.notedata.sitenames;
								 	sitesFromBackend = sitesFromBackend.replace("[", '');
								 	sitesFromBackend = sitesFromBackend.replace("]", '');
								 	    var siteNamesForNote='';
								 	    for(var i=0;i<sitesFromBackend.length;i++){
								 	    	siteNamesForNote+=sitesFromBackend[i]+",";
								 	    }
								 		var noteData="A Report on Marketing Expenditure for Projects "+ sitesFromBackend +" from "+JSON.parse(data).xml.notedata.fromdate+" to "+JSON.parse(data).xml.notedata.todate+".";
								 	$("#noteSpan").html(noteData);  
								 	$("#note").show();  
									$(".spinnercls").hide();
									$("#errorData").hide();			  				
									$("#sitesData").show();
				  			}else{
				  				$("#myPie").html('');
				  				$("#sitesData").html('');
				  				$("#sitesData").css({
				  					"height": "0px"
				  				});
				  				$(".spinnercls").hide();
				  				$("#errorData").show();
				  				$("#errorData").html("No Data found.");
				  				$("#note").hide();
				  			}
						  },
				  error:  function(data, status, er){
					  $(".spinnercls").hide();
					  alert(data+"_"+status+"_"+er);
				  }
		 });
		 }
		 loadSiteNames();
		 function loadSiteNames(){
			 $.ajax({
					url:"siteNameDetails.spring",
					type:"GET",
					success:function(response){debugger;
							var response=JSON.parse(response).xml.site;
							var selectdata='';
			        		for(var i=0;i<response.length;i++){
			        			 selectdata+='<option value='+response[i].SITEID+'>'+response[i].SITENAME+'</option>';
			        		}
							$("#sitenames").html(selectdata);
					}
				});
		 }
	
</script>
</body>
</html>