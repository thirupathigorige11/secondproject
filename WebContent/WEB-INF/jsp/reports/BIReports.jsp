<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>



<html>
<head>
<style>
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
.pro-table tbody tr td,.pro-table tbody tr th{
	margin:2px 3px;
	width:100%;
	min-width:213px; 
}
.form-control {
    display: block;
    width: 100%;
    height: 34px;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #423636;
    background-color: #fff;
    background-image: none;
    border: 1px solid #2b2626;
    border-radius: 4px;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
    -webkit-transition: border-color ease-in-out .15s,-webkit-box-shadow ease-in-out .15s;
    -o-transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    transition: border-color ease-in-out .15s,box-shadow ease-in-out .15s;
    }
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />

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
		<link href="css/style.css" rel="stylesheet">
		<link rel="stylesheet" href="jquery.stickytable.min.css">
		<script src="//code.jquery.com/jquery.min.js"></script>
		<script src="jquery.stickytable.min.js"></script>
		
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<jsp:include page="../CacheClear.jsp" />  
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	#Subcantainer{
	float: right;
    margin-top: -327px;
    margin-right: 79px;
    font-size: 16px;
    font-weight: bold;
    width: 150px;
    height: 300px;
    border: 1px solid black;
    }
	</style>
</head>
<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	</style>
</noscript>
<div class="container body" id="mainDivId">
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
							<li class="breadcrumb-item"><a href="#">Inwards</a></li>
							<li class="breadcrumb-item active">Reports</li>
						</ol>
					</div>
<!-- ****** Section grid for Load the reports*********** -->


				<div class="container">
				<div id="chartContainer" style="height: 370px;width:500px; max-width: 920px; margin: 0px auto;background: #d8d3d3;margin-top: 30px;">
					
				
				</div><div id="Subcantainer" class="Subcantainer" style="display:none" ><span class="subsubconatiner" style="margin-left: 42px;">Details</span>
				<div>Soham:250Bags</div>
				<div>Acroplois:250Bags</div>
				<div>Madhuram:250Bags</div>
				<div>Central:250Bags</div>
				<div>Eden garden:250Bags</div>
				</div>
				<div id="chartContainer-1" style="height: 370px;width:500px; max-width: 920px; margin: 0px auto;margin-top: 30px;"></div>
				<div id="chartContainer3" style="height: 450px; width: 100%;margin-top: 80px;"></div>
				<div id="resizable" style="height: 370px;border:1px solid gray;margin-top: 80px;">
						<div id="chartContainer4" style="height: 100%; width: 100%;"></div>
				</div>
			</div>
		</div>

	</div>
	</div>
	
	
					
					<!-- /page content -->        
				</div>
			</div>
		</div>
		
		
		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.canvasjs.min.js" type="text/javascript"></script>
		<script src="js/canvasjs.min.js" type="text/javascript"></script>
		
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			$(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			});
/* ******* Method for Pie Charts************		 */
			window.onload = function () {debugger;

			var chart = new CanvasJS.Chart("chartContainer", {
				animationEnabled: true,
				title:{
					text: "Stock Avilabity",
					/* horizontalAlign: "left" */
				},
				data: [{
					type: "pie",
					startAngle: 60,
					//innerRadius: 60,
					indexLabelFontSize: 17,
					indexLabel: "{label} - #percent%",
					toolTipContent: "<b>{label}:</b> {y} (#percent%)",
					dataPoints: [
						{ y: 150, label: "Chemicals" },
						{ y: 28, label: "Bricks" },
						{ y: 10, label: "Cement" },
						{ y: 7, label: "Administrative"},
						{ y: 15, label: "Stationary"},
					]
				}]
			});
			chart.render();

/* *********** Method for funel charts******************	 */	 	
			 	

var chart = new CanvasJS.Chart("chartContainer-1", {
	animationEnabled: true,
	title:{
		
		text: "Shortage Prodcts"
	},
	data: [{
		type: "funnel",
		indexLabel: "{label} - {y}",
		toolTipContent: "<b>{label}</b>: {y} <b>({percentage}%)</b>",
		neckWidth: 20,
		neckHeight: 0,
		valueRepresents: "area",
		dataPoints: [
			{ y: 3871, label: "Tiles" },
			{ y: 2496, label: "Mildsteel" },
			{ y: 1398, label: "Electrical" },
			{ y: 1118, label: "Lubricants" },
			{ y: 201, label: "RCC" }
			
		]
	}]
});
calculatePercentage();
chart.render();

function calculatePercentage() {
	var dataPoint = chart.options.data[0].dataPoints;
	var total = dataPoint[0].y;
	for(var i = 0; i < dataPoint.length; i++) {
		if(i == 0) {
			chart.options.data[0].dataPoints[i].percentage = 100;
		} else {
			chart.options.data[0].dataPoints[i].percentage = ((dataPoint[i].y / total) * 100).toFixed(2);
		}
	}
}
var options = {
		animationEnabled: true,
		theme: "red",
		title:{
			text: "Monthly Expenses, 2017-18"
		},
		axisY :{
			includeZero: false,
			prefix: "$",
			lineThickness: 0
		},
		toolTip: {
			shared: true
		},
		legend: {
			fontSize: 13
		},
		data: [{
			type: "splineArea",
			showInLegend: true,
			name: "Salaries",
			style:"font-family:calibri;",
			yValueFormatString: "$#,##0",
			xValueFormatString: "MMM YYYY",
			dataPoints: [
				{ x: new Date(2016, 2), y: 28000 },
				{ x: new Date(2016, 3), y: 31500 },
				{ x: new Date(2016, 4), y: 28500 },
				{ x: new Date(2016, 5), y: 30400 },
				{ x: new Date(2016, 6), y: 26900 },
				{ x: new Date(2016, 7), y: 31400 },
				{ x: new Date(2016, 8), y: 31400 },
				{ x: new Date(2016, 9), y: 31000 },
				{ x: new Date(2016, 10), y: 33000 },
				{ x: new Date(2016, 11), y: 35000 },
				{ x: new Date(2017, 0),  y: 37900 },
				{ x: new Date(2017, 1),  y: 38000 }
			]
	 	},
		{
			type: "splineArea", 
			showInLegend: true,
			name: "Office Cost",
			yValueFormatString: "$#,##0",
			dataPoints: [
				{ x: new Date(2016, 2), y: 18100 },
				{ x: new Date(2016, 3), y: 15000 },
				{ x: new Date(2016, 4), y: 14000 },
				{ x: new Date(2016, 5), y: 18500 },
				{ x: new Date(2016, 6), y: 18500 },
				{ x: new Date(2016, 7), y: 21800 },
				{ x: new Date(2016, 8), y: 20000 },
				{ x: new Date(2016, 9), y: 23000 },
				{ x: new Date(2016, 10), y: 22000 },
				{ x: new Date(2016, 11), y: 24000 },
				{ x: new Date(2017, 0), y: 23000 },
				{ x: new Date(2017, 1), y: 20500 }
			]
	 	},
		{
			type: "splineArea", 
			showInLegend: true,
			name: "Entertainment",
			yValueFormatString: "$#,##0",     
			dataPoints: [
				{ x: new Date(2016, 2), y: 13100 },
				{ x: new Date(2016, 3), y: 8000 },
				{ x: new Date(2016, 4), y: 5400 },
				{ x: new Date(2016, 5), y: 4000 },
				{ x: new Date(2016, 6), y: 7000 },
				{ x: new Date(2016, 7), y: 7500 },
				{ x: new Date(2016, 8), y: 6200 },
				{ x: new Date(2016, 9), y: 8500 },
				{ x: new Date(2016, 10), y: 11300 },
				{ x: new Date(2016, 11), y: 12500 },
				{ x: new Date(2017, 0), y: 10500 },
				{ x: new Date(2017, 1), y: 9500 }
			]
	 	},
		{
			type: "splineArea", 
			showInLegend: true,
			yValueFormatString: "$#,##0",      
			name: "Maintenance",
			dataPoints: [
				{ x: new Date(2016, 2), y: 1900 },
				{ x: new Date(2016, 3), y: 2300 },
				{ x: new Date(2016, 4), y: 1650 },
				{ x: new Date(2016, 5), y: 1860 },
				{ x: new Date(2016, 6), y: 1200 },
				{ x: new Date(2016, 7), y: 1000 },
				{ x: new Date(2016, 8), y: 1200 },
				{ x: new Date(2016, 9), y: 4500 },
				{ x: new Date(2016, 10), y: 1300 },
				{ x: new Date(2016, 11), y: 3700 },
				{ x: new Date(2017, 0), y: 2700 },
				{ x: new Date(2017, 1), y: 2300 }
			]
		}]
	};
	$("#chartContainer3").CanvasJSChart(options);
	
	var options1 = {
			animationEnabled: true,
			title: {
				text: "Payment Approvals site wise"
			},
			data: [{
				type: "column", //change it to line, area, bar, pie, etc
				legendText: "Payments approvals sitewise",
				showInLegend: true,
				dataPoints: [
					{  y: 25, label: "Acropolis"},
					{ y: 6, label: "Soham" },
					{ y: 14, label: "Eden Garden" },
					{ y: 12 , label: "Lake Breze"},
					{ y: 16 , label: "MADHURAM"},
					{ y: 01 , label: "CENTRAL"},
					{ y: 5 , label: "ASSET STORE"},
					{ y: 14, label: "Eden Garden" },
					{ y: 12 , label: "Lake Breze"},
					{ y: 16 , label: "MADHURAM"},
					{ y: 01 , label: "CENTRAL"},
					{ y: 5 , label: "ASSET STORE"},
					{ y: 14, label: "Eden Garden" },
					{ y: 12 , label: "Lake Breze"},
					{ y: 16 , label: "MADHURAM"},
					{ y: 01 , label: "CENTRAL"},
					{ y: 5 , label: "ASSET STORE"},
					]
				}]
		};

		$("#resizable").resizable({
			create: function (event, ui) {
				//Create chart.
				$("#chartContainer4").CanvasJSChart(options1);
			},
			resize: function (event, ui) {
				//Update chart size according to its container size.
				$("#chartContainer4").CanvasJSChart().render();
			}
		});
	
};			 	
		$("#chartContainer").click(function(){
			$("#Subcantainer").show();
			
		})	 	
		</script>
</body>
</html>
