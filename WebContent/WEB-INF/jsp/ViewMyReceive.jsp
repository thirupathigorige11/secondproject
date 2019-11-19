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
<jsp:include page="CacheClear.jsp" />  
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
<!-- link href="js/jquery-ui.css" rel="stylesheet"-->
<link href="css/style.css" rel="stylesheet">
<title>Pending Indents</title>

</head>

<style>
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
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
.container1{
display:none;
}
.media-style{
width:39% !important;

}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}
/* .radiocontainer{
width:80%;
margin:0 auto;
} */

/* 
.radiocontainer .radioul{
  list-style: none;
  margin: 0;
  padding: 0;
	overflow: auto;
}
.radioul{
margin-left: -48px !important;
}
.radioul li{

  display: inline;
  position: relative;
  float: left;
  width: 28%;
  height: 100px;
	
}

.radioul li input[type=radio]{
  position: absolute;
    
    top: 41px;
    left: 55px;
    z-index: 10;
}

.radioul li label{
  display: inline;
  position: absolute;
  font-weight: 300;
  font-size: 1.35em;
  padding: 25px 25px 25px 80px;
  margin: 10px auto;
  height: 30px;
  z-index: 9;
  cursor: pointer;
  -webkit-transition: all 0.25s linear;
}



.radioul li .check{
  display: inline;
  position: absolute;
  border: 6px solid orange;
  border-radius: 100%;
  height: 22px;
  width: 22px;
  top: 40px;
  left: 50px;
	z-index: 5;
	transition: border .25s linear;
	-webkit-transition: border .25s linear;
}


.radioul li .check::before {
  display: inline;
  position: absolute;
	content: '';
  border-radius: 100%;
    height: 17px;
    width: 17px;
    top: -9px;
    left: -9px;
  margin: auto;
	transition: background 0.25s linear;
	-webkit-transition: background 0.25s linear;
}

input[type=radio]:checked ~ .check {
  border: 12px solid orange;
}

input[type=radio]:checked ~ .check::before{
  background: black;
}

 */
.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
/* .inward-invoice{
	color: #726969;
   /*  margin-left: 377px; */
    font-size: 24px;

} */
#WithoutPricingData{
	margin-top: 31px;
   
}
.form-content{
margin-top: 30px;
}
.bottom-class{
margin-top: 22px;
}
/* .withdata{
    margin-top: 32px;
	border: 1px solid;
    height: 134px;
    width: 544px;
} */
.fields-space-with{
    margin-top: 26px;
    
    }
 .fields-space{
     margin-right: 25px;
 
 }
 .fields1-space{
 	 margin-left: 6px;
 }
 .withoutPricing-class{
 	margin-bottom: 10px;
 }
 .formShow{
    border: 1px solid #e4e2e2;
    margin-top: 35px;
/*     width: 1052px; */
    background: #fff;
    /* overflow: scroll;
    background: #ffffff; */
    }
    .container{
/*     	background: rgba(146, 140, 132, 0.2); */
/*     	padding: 10px; */
    }
    
    .DCNumber{
    	color: black;
    	font-weight: bold;
    }
 
    hr { 
    display: block;
    margin-top: 0.5em;
    margin-bottom: 0.5em;
    margin-left: auto;
    margin-right: auto;
    border-style: inset;
    border-width: 2px;
} 
.full-width{
	width: 100%;
}
.icons-bg{
    padding: 3px 10px;
}

.pricing-box{
	background: rgba(33, 32, 31, 0.2);
    display: flow-root;
    padding: 10px;
    border: solid 1px #d8d3d3;
	
}
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
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
}
</style>
<script

src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.4/angular.min.js">
src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js">
	
</script>

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

						<jsp:include page="SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="TopMenu.jsp" />  


	<!-- page content -->
	<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Library</li>
					</ol>
				</div>
				<form name="myForm" action="doGetInvoiceDetails.spring" style=" margin-left: 108px; margin-bottom: 24px;"> 
				
				
	<form>
	
   
    <label class="radio-inline">
      <input type="radio" id="allindents" name="optradio"><span style="font-size: 18px">All Indents</span>
    </label>
     <label class="radio-inline">
      <input type="radio" id="indentwise" name="optradio"><span style="font-size: 18px">Indent Wise</span>
    </label>
  </form>
				
<!-- 	*************************** Indent wise************************		 -->	
				
				 	<div class="container " id="indentwisedata" style="display:none ;background-color: #ccc; width: 39%; margin: 9% auto; padding: 2px; text-align: center; font-size: 20px; height: 200px; border: 1px solid gray; box-shadow: 2px 3px;">
				 		<form id="indentwise" class="withdata" action="ViewIndentSenderDetails.spring" >
						  	<div class="DCNumber">Indent Number : </div><span>	<input type="text" name="indentNumber" id="indentNumber" autocomplete="off" /></span>
				  			<div class="btn-submit">	
				    		<button class="btn btn-warning col-sm-2 col-sm-offset-4" type="submit"  style="margin-top: 21px;" >SUBMIT</button> 
				    	</div>
				 		</form>
				 	</div>		
	<!-- ****************************** All Indent wise************************* -->
				
		<div class="container " id="Allindentwisedata" style="display:none">
			<form class="form-horizontal" name="myForm" action="sendenquiry.spring" style="padding:15px;">
	 	  
	  		<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">

							<tr>
						<th>Created Date</th>		
    				
    				<th>Indent ProcessNumber</th>
    				
    			<!-- 	<th>Created Employee</th> 	-->
    				
    				<th>Site Id</th>
    				
    			<!-- 	<th>Required Date</th>	-->
    				
				</tr>

						</thead>
						<tbody>
				<c:forEach items="${listofCentralIndents}" var="element">  
				<tr>
				    <td style="color: black;">${element.strCreateDate}</td> 	
					
					<td><a href="ViewIndentSenderDetails.spring?indentNumber=${element.indentNumber}&siteName=${element.siteName}" style="text-decoration: underline;color: blue;">${element.indentNumber}</a></td>
					
				<!-- 	<td style="color: black;">${element.indentFrom}</td>-->
					
					<td style="color: black;">${element.siteName}</td>
					
					<!--	<td style="color: black;">${element.strRequiredDate}</td>-->
				</tr>
				</c:forEach>
				</tbody>
					</table>


		</form>
				 	</div>
<!-- ****************************** product wise************************* -->
							
				
</form>
		
	
	
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
		<script type="text/javascript" src="js/ComboBox_IndentPOAvailabulity.js"></script>
		
<script>
		$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
										});

						$(function() {
							var div1 = $(".right_col").height();
							var div2 = $(".left_col").height();
							var div3 = Math.max(div1, div2);
							$(".right_col").css('max-height', div3);
							$(".left_col").css('min-height',
									$(document).height() - 65 + "px");
						});
		
/* Method for button show and show function
						 */

				 	 $(document).ready(function(){
							$("#producttwise").click(function(){
								$("#Allindentwisedata").hide();
								$("#indentwisedata").hide();
								$("#productwise").show();
								
								
							});
							$("#indentwise").click(function(){
								$("#indentwisedata").show();
								$("#Allindentwisedata").hide();
								$("#productwise").hide();
							});
							$("#allindents").click(function(){
								$("#Allindentwisedata").show();
								$("#indentwisedata").hide();
								$("#productwise").hide();
							}); 
							 
						 });	 
						 

 
 



</script>
</body>
</html>
