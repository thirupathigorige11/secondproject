<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<html>
<head>
		
        <jsp:include page="../CacheClear.jsp" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
	   	<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
                <link href="js/inventory.css" rel="stylesheet" type="text/css" />		
		<script src="js/jquery.min.js"></script>
		
		
		<script src="js/bootstrap.min.js"></script>
		<script src="js/momment.js"></script>
		<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
		<script src="js/sweetalert2.js" type="text/javascript"></script>
		<script src="js/numberCheck.js" type="text/javascript"></script>
		
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
		<style>
		   .btnaction{margin-right:5px;}
		   .table-new thead, .table-new tbody tr{table-layout:fixed;display:table;width:100%;}
		   .table-new thead tr th:first-child, .table-new tbody tr td:first-child{width:60px;}
		   .table-new tbody tr td{border-top: 0px !important;}
		   .btn{padding: 4px 12px !important;}
		   tr.strikeout td:before {
			  content: " ";
			  position: absolute;
			  top: 50%;
			  left: 0;
			  border-bottom: 1px solid red;
			  width: 100%;
			  z-index: 100px;
			}
			
			tr.strikeout td:after {
			  content: "\00B7";
			  font-size: 1px;
			  z-index: 100px;
			}
		 </style>
</head>
<body class="nav-md" id="body-refresh">
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
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Marketing</a></li>
							<li class="breadcrumb-item active">Update Available Area</li>
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
			       <h3 class="text-center"><span><font color=green size=4 face="verdana">${message}</font></span></h3>
			 <div>
				<div class="col-md-12">
				  <!-- page content -->
				  <div class="border-inwards-box">
				   <form   id="updateAvailableArea" class="form-inline">
				    <div class="form-group">
				     <label>Month:</label>
				     </div>
				       <div class="form-group input-group">
				        <input type="text" id="monthYear" name = "monthYear" class="form-control readonly-color" autocomplete="off" readonly="true"/>
				        <label class="input-group-addon btn input-group-addon-border" for="monthYear"><span class="fa fa-calendar"></span></label>				     
				    </div>
				   <!--  <button type="submit" class="btn btn-warning" id="btn-search" >Submit</button> -->
				    <input type="button" class="btn btn-warning" value="Submit" name = "Get Daetails" onclick="submitUpdate()">
				   </form>
				   </div>
				   
				   <form action="" class="form-inline" method = "" id="updateavalArea">
				 
				      
				      <%
				      
				      String strShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				    
				     System.out.println("showGrid : "+strShowGrid);
				      
				      if(strShowGrid.equals("true")){
				      
				      %>
				        <div id="Markettable_hideshow">
				        <div class="table-responsive">
				    
				         <input type="hidden" id="totalNoOfRows" name="totalNoOfRows"    value="${totalNoOfRecords}">
				         <input type="hidden" id="month_year" name="month_year"    value="${month_year}">
				      
				      <table class="table table-new" id="expenditure_table" style="width:1500px;">
						    <thead class="cal-thead-inwards">
						     <tr>
						       <th>S.No</th>
						       <th>Site</th>
						       <th>UOM</th>
						       <th>Status</th>
						       <th>Total Area</th>
						       <th>Available Area</th>						       
						       <th>Action</th>
						     </tr>
						    </thead>
						    <tbody class="tbl-fixedheader-tbody">
						    
						      <c:forEach items="${marketingBeanList}" var="marketingBean">
				                    <tr id="tr-class${marketingBean.intSerialNo}" class="tr-class">	
				                      <td>${marketingBean.intSerialNo}</td>
				                       <input type="hidden" id="siteId${marketingBean.intSerialNo}" name="siteId${marketingBean.intSerialNo}"    value="${marketingBean.siteId}">
			                           <td> <input type="text" id="siteName${marketingBean.intSerialNo}" name="siteName${marketingBean.intSerialNo}"    value="${marketingBean.siteName}" class="form-control sitenameclass" readonly></td>
				                       <td> <input type="text" id="UOM${marketingBean.intSerialNo}" name="UOM${marketingBean.intSerialNo}"    value="${marketingBean.measurementName}" class="form-control" readonly></td>
				                      <td><select id="status${marketingBean.intSerialNo}" name="status${marketingBean.intSerialNo}"   onchange="statusChange(${marketingBean.intSerialNo})"  value="${marketingBean.strStatus}" class="form-control" readonly><option value="A">Active</option><option value="I">Inactive</option></select><input type="hidden" name="areaActionValue${marketingBean.intSerialNo}" id="areaActionValue${marketingBean.intSerialNo}" value="S"> </td>
				                       <td><input type="text" id="totalArea${marketingBean.intSerialNo}" name="totalArea${marketingBean.intSerialNo}"  placeholder="Total Area" autocomplete="off" onkeypress="return isNumberCheck(this, event)" onfocusout="checkValue(${marketingBean.intSerialNo})" value="${marketingBean.doubleTotalArea}" class="form-control pasteDisable" readonly> </td>
				                       <td> <input type="text" id="availableArea${marketingBean.intSerialNo}" name="availableArea${marketingBean.intSerialNo}" autocomplete="off" placeholder="Available Area"  onkeypress="return isNumberCheck(this, event)" onfocusout="checkValue(${marketingBean.intSerialNo})" value="${marketingBean.doubleAVailableArea}" class="form-control pasteDisable" readonly></td>
				                       <td>
     		                             <button type="button" name="editItemBtn${marketingBean.intSerialNo}" value="Edit Item" id="editItem${marketingBean.intSerialNo}" class="btnaction" onclick="editAvalibleRow(${marketingBean.intSerialNo})" ><i class="fa fa-pencil"></i></button>
     		                             <button type="button" class="btnaction btnpadd" onclick="appendRow(${marketingBean.intSerialNo})" id="addRowBtn${marketingBean.intSerialNo}"><i class="fa fa-plus"></i></button>
     		                             <button type="button" class="btnaction" id="deleteRowBtn${marketingBean.intSerialNo}" onclick="deleteRowStrikeOut(${marketingBean.intSerialNo})"><i class="fa fa-trash"></i></button> 
     		                           </td>
     		                        </tr>
				
			                 </c:forEach>
						         
						    </tbody>
				     </table>
				     </div>
				   <div class="col-md-12 text-center center-block">
				    <input type ="button" id="update_btn" class="btn btn-warning Mrgtop10" value ="Update" onclick="updateAvalibleAreaSubmit()">
				   </div>
				    </div>
				     <% } %>
				   
				    
				    </form>
				  <!-- page content -->
				</div>
	        </div>
	      </div>
	</div>
</div>
        <script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/marketing/jquery.ui.monthpicker.js" type="text/javascript"></script>      
        <script src="js/marketing/jquery.timepicker.min.js"></script>         
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
        <script src="js/marketing/updateavailable.js" type="text/javascript"></script>
        
 <script>
  $(document).ready(function () {
	$('#monthYear').monthpicker({changeYear:true,dateFormat:"mm-yy"});
	 $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
});
  if(!!window.performance && window.performance.navigation.type == 2)
	{
	    window.location.reload();
	}
  
 
  function submitUpdate(){
	  var monthYear = $("#monthYear").val();
	  if(monthYear ==""){
		  swal("Error!", "Please select month.", "error");
		  $("#monthYear").focus();
		  return false;
	  }
	  
	  $('.loader-sumadhura').show();
		 document.getElementById("updateAvailableArea").action = "getAvailableArea.spring";
		 document.getElementById("updateAvailableArea").method = "POST";
		 document.getElementById("updateAvailableArea").submit();
		 
  }
  
 </script>
</body>
</html>
