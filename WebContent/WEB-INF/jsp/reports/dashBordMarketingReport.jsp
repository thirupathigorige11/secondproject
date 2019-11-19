<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <%-- <jsp:include page="./Purchase_GUI_Reports.jsp" /> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
   <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
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
			.anchorcls{
				float: right;
			    margin-right: 20px;
			    margin-top: 10px;
			    font-size: 16px;
			    text-decoration: underline;
			    color: blue;	
			}
			.anchorcls:hover{
				cursor: pointer;
				text-decoration: underline;
			}
		</style>
  </head>
<body >
<form  class="form-horizontal" action="dashboard.spring"  method="post"    autocomplete="off">
	<div class="container body" id="mainDivId">
		<div class="main_container" id="main_container">			
			<div class="" role="main"> 
				<div>
					<div class="col-md-12">
	           		   <div class="clearfix"></div> 
	           		     <div class="col-md-12" style="margin-bottom:30px;border:1px solid;">
	           		     	<a href="MarketingReport.spring" class="anchorcls" target="_self">Click here to view more details</a>
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
	   </form>
	<script src="js/jquery.min.js"></script>
    <script src="js/d3.min.js"></script>
	<script src="js/d3pie.min.js"></script>
	<script>
		function setPage(){
			window.location.href="MarketingReport.spring";
		}
		loadPieChart();
		function loadPieChart(){
			
			 $.ajax({
				  url :"defaultProductDetais.spring",
				  type : "get",
				  Cdata : "",
				  contentType : "application/json",
				  success : function(data) {
					  debugger;
					  if(data!=""){
				 var productData=JSON.parse(data).xml.data;
				 var data1=[];
					if(productData.label){
						data1.push({"label":productData.label,"value":parseInt(productData.value)});
					}else{									 
						 for(var i=0;i<productData.length;i++){
							 data1.push({"label":productData[i].label,"value":parseInt(productData[i].value)});
						 }
					}	
				 /* var data=[
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
						 ]; */
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
				 	var sitesFromBackend=JSON.parse(data).xml.notedata.sitenames;
				 	sitesFromBackend = sitesFromBackend.replace("[", '');
				 	sitesFromBackend = sitesFromBackend.replace("]", '');
			 	    var siteNamesForNote='';
			 	    for(var i=0;i<sitesFromBackend.length;i++){
			 	    	siteNamesForNote+=sitesFromBackend[i]+",";
			 	    }
			 		var noteData="A Report on Marketing Expenditure for Projects "+ sitesFromBackend +" from "+JSON.parse(data).xml.notedata.fromdate+" to "+JSON.parse(data).xml.notedata.todate+".";
			 		 $("#noteSpan").html(noteData); 
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
</script>
</body>
</html>