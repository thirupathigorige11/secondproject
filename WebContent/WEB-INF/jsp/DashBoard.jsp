<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns:jsp="http://java.sun.com/JSP/Page">
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
		<link href="css/style.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		<jsp:include page="CacheClear.jsp" />  
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js" charset="utf-8"></script>
		<script src="http://labratrevenge.com/d3-tip/javascripts/d3.tip.v0.6.3.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3-legend/1.7.0/d3-legend.min.js"></script>
		<style>
				.finalAmountDiv{ color: #f0ad4e;font-size: 24px;}
				.paymenttitle{margin-bottom:30px;}
		        .chart-container .active {background: lightcoral;}
			    .stacked-bar {font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; width: 960px; height: 500px; position: relative;}
		        svg {  width: 100%; height: 100%; position: center; }
		        text{ font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; }
		        .toolTip { font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; position: absolute;  display: none;  width: auto;  height: auto;
		         background: none repeat scroll 0 0 white; border: 0 none; border-radius: 8px 8px 8px 8px; box-shadow: -3px 3px 15px #888888;
		         color: black; font: 12px sans-serif; padding: 5px; text-align: center; }
		       .legend { font-family: "Helvetica Neue", Helvetica, Arial, sans-serif; font-size: 60%; }
		       text {font:10px sans-serif;}
		       .axis text {font: 10px sans-serif; }
		       .axis path{fill: none; stroke: #000; }
		       .axis line {fill: none; stroke: #000; shape-rendering: crispEdges; }
		       .x.axis path {display: none; }
		       .ToolTipClass, .ToolTipClass-Itm{margin-left: 20px;}
		   	   .bar:hover {  fill: #afaaa9;}
		       .d3-tip { font-weight: bold; padding: 12px; background:#a99f9f; color: #fff; border-radius: 2px;}
				/* Creates a small triangle extender for the tooltip */
			  .d3-tip:after { box-sizing: border-box; display: inline; font-size: 10px; width: 100%; line-height: 1; color: #a99f9f;
				  content: "\25BC"; position: absolute; text-align: center; }
		       /* Style northward tooltips differently */
		     .d3-tip.n:after { margin: -1px 0 0 0; top: 100%; left: 0; }
			.chatAsu{ height: 503px; width: 128px; border: 1px solid #887474; position: absolute;  margin-top: -503px; margin-left: 947px; overflow-y: scroll;  display: none; }
		    .stacked-sideBar{ width: 100px; height: 354px;  border: 1px solid;  position: absolute; margin-left: 936px; margin-top: -440px; }	   
		    .chart-container{ border: 1px solid; width: 946px; }
			.breadcrumb{ background: #eaeaea; }
			.loader { display: none;   margin: 0 auto; border: 4px solid #c5b3b3;border-radius: 50%; border-top: 4px solid orange; width: 43px; height: 43px; z-index: 10; -webkit-animation: spin 2s linear infinite; animation: spin 2s linear infinite;}
		   .hoverClass:hover { background-color: #d8d3d3e0; }
		   .indexClass{ width:2px !important; border:1px solid;word-break: break-word;'color:red;}
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
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Dashboard</li>
						</ol>
					</div>
					<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
					  <div class="modal-dialog" role="document">
					    <div class="modal-content">
					        <button class="close" data-dismiss="modal" style=" background: #020202;color: #f60909;line-height: 25px;position: absolute;right: -12px;text-align: center; top: -10px;width: 24px;text-decoration: none;font-weight: bold;-webkit-border-radius: 12px;-moz-border-radius: 12px;border-radius: 12px;-moz-box-shadow: 1px 1px 3px #000; -webkit-box-shadow: 1px 1px 3px #000;box-shadow: 1px 1px 3px #000;">X</button>
					     	<img src="images/diwali_wish.jpg" style="width: 100%;padding: 10px;">			
					    </div>
					  </div>
					</div>
					
					 <!-- <form action='http://129.154.74.18:8078/Sumadhura_UAT/StoreDailyReport.jsp?requestDate=25-10-18' method='POST'>

                           <input type='submit' value='Show Report' />

                     </form> -->
                      <!-- --------Page Content start here-----------		 -->	
			        <form  class="form-horizontal" action="dashboard.spring"  method="post"    autocomplete="off">		
	                  	
	                   <%
	                        String userProfile = String.valueOf(session.getAttribute("UserProfileType"));
	                        if (userProfile.equals("accounts")) {
	                            %>
	                            <jsp:include page="./reports/Payment_GUI_Reports.jsp" />
	                            <%
	                        } else if (userProfile.equals("store")) {
	                            %>
	                            <jsp:include page="./reports/Store_GUI_Reports.jsp" />
	                            <%
	
	                        }
	                        if (userProfile.equals("purchase")) {
	                            %>
	                            <jsp:include page="./reports/Purchase_GUI_Reports.jsp" />
	                            <%
	                        }
	                    %>
			        </form>
			        <!-- /page content -->   
				</div>
			</div>
		</div>
	</div>
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/sidebar-resp.js"></script>
	<script>
			$(document).ready(function() {
				var d = new Date();
				var dateMonth=d.getDate()+"-"+d.getMonth()+"-"+d.getFullYear();
				console.log("Date: "+dateMonth);
				/* if(dateMonth=="5-9-2019" || dateMonth=="6-9-2019" || dateMonth=="7-9-2019" || dateMonth=="8-9-2019"){ */
					//$("#myModal").modal();
				/* }	 */			
				setInterval(function(){
					$("#myModal").modal('hide');
				},11000);
				$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				});
			});
	</script>
  </body>
</html>
