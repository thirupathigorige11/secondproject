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
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
                <link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> 
		<!-- <link href="css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"> -->
		<link href="css/jquery.timepicker.min.css" rel="stylesheet" type="text/css">

		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<style>
     div.dataTables_scrollBody table { border-top: none;  margin-top: -2px !important; margin-bottom: 0 !important;}
     div.dataTables_wrapper div.dataTables_paginate { margin: 7px 0px;white-space: nowrap;text-align: right;}
     div.dataTables_wrapper {width: 1300px;margin: 0 auto;}
   #btn-search{padding:6px 12px;width:100px;}
   table.dataTable{border-collapse: collapse !important;}
   .ui-timepicker-container {z-index:1051 !important;} 
   label{text-align:left !important;}
 
   .anchor-market{float: left;}
   /* css for market inwards */
    .table-new>tbody+tbody {border-top: 1px solid #000 !important;}
  .form-control{height:34px !important;}
  .marGinBottom{margin-bottom:25px;}
  .btnpadd{margin-right:5px;}
  .text-center-left{text-align:center;}
  @media only screen and (min-width:320px) and (max-width:767px){.text-center-left{text-align:left;}}
  .selectsingleSite{padding-left:0px !important;margin-bottom:15px;font-weight:bold;color:#000;}
  .selectsingleSite .form-control{height:34px;font-weight: bold;color: #000;}
  .anchor-market:hover{color:#0000ff;font-size:15px;text-decoration:underline;}
  .anchor-market{color:#0000ff;font-size:15px;}
  .totalMarketingAmount td{background:#bfd9e5;text-align:right;font-size:13px;font-weight:bold;}
/* css for market inwards */
  .widthonefifty{	width:150px; }
  .table>tbody+tbody { border-top: 0px solid #000 !important;}
 </style>
</head>
<body class="nav-md" id="body-refresh">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId { display : none; }
	</style>	
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
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
							<li class="breadcrumb-item active">Update Expenditure</li>
						</ol>
			       </div>
			       <!-- loader -->
			          <div class="loader-sumadhura" style="display: none;z-index:999;">
						<div class="lds-facebook">
							<div></div><div></div><div></div><div></div><div></div><div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
			        </div>
	           <h3 class="text-center"><span><font color=red size=4 face="verdana">${message}</font></span></h3>		       
			   <h3 class="text-center"><span><font color=green size=4 face="verdana">${message1}</font></span></h3>    
			 <div>
			  <div class="col-md-12">
			   <div class="border-inwards-box">
				   <form:form modelAttribute="marketingExpenditureModelForm" class="form-horizontal" action="getAllViewExpenditures.spring" method="post">
				      <input type="hidden" id="referer" name="referer" value="UpdateExpenditures">
				    <div class="col-md-12">
				       <div class="col-md-4">
				        <div class="form-group">
					      <label class="col-md-6 col-sm-6">Invoice From Date :</label>
					      <div class="col-md-6 col-sm-6 input-group">
					        <input type="text" class="form-control readonly-color" name="datepickerfrom" id="datepickerfrom"  onchange="fromDateChange()" autocomplete="off" readonly="true"/>
					        <label class="input-group-addon btn input-group-addon-border" for="datepickerfrom">
								   <span class="fa fa-calendar"></span>
				             </label>
					      </div>
				       </div>
				      </div>
				      <div class="col-md-4">
				       <div class="form-group">
				        <label class="col-md-6 col-sm-6">Invoice To Date :</label>
				        <div class="col-md-6 col-sm-6 input-group">
				         <input type="text" class="form-control readonly-color" name="datepickerto" id="datepickerto"  onchange="toDateChange()" autocomplete="off" readonly="true"/>
				          <label class="input-group-addon btn input-group-addon-border" for="datepickerto">
								   <span class="fa fa-calendar"></span>
				             </label>
				       </div>
				      </div>
				     </div>
				    </div>
				    <div class="col-md-12">
				      <div class="col-md-4"> 
					     <div class="form-group">
						   <label class="col-md-6 col-sm-6">Invoice No:</label>
						   <div class="col-md-6 col-sm-6"><input type="text" class="form-control" name="invoiceid" id="invoiceid"/></div>
					    </div>
				      </div>
				      <div class="col-md-4">
				       <div class="form-group">
					     <label class="col-md-6 col-sm-6">Invoice Date:</label>
					     <div class="col-md-6 col-sm-6 input-group"><input type="text" class="form-control readonly-color" name="invoiceDate" id="invoiceDate" readonly="true"/>
					      <label class="input-group-addon btn input-group-addon-border" for="invoiceDate">
								   <span class="fa fa-calendar"></span>
				             </label>
					     </div>
				       </div>
				     </div>
				     <div class="col-md-4"> 
				      <div class="form-group">
					     <label class="col-md-6 col-sm-6">Vendor Name:</label>
					     <div class="col-md-6 col-sm-6">
					      <form:input path="vendorName" id="VendorNameId" class="form-control vendor" autocomplete="off" onkeyup="getVendorId()"/>
				          <input type="hidden" name="VendorId" id="vendorIdId" class="form-control" value=""/>
				         </div>
				     </div>
				    </div>
				    </div>
				    <div class="col-md-12 text-center center-block">
				      <button type="submit"  class="btn btn-warning" id="btn-search" onclick="return searchData()">Search</button>
				    </div>
				   </form:form>
			   </div>
	<!-- main page table -->
	
	<c:if test="${requestScope.isShowAll}">
			 <div class="table-responsive">
			  <table class="table table-new table-Market display nowrap" style="width:100%;border-collapse: collapse !important;" id="expenditure_table1">
			    <thead>
			     <tr>
			           <th style="width:45px;">S.No</th>
				       <th>Child Product Name</th>
				       <th>Site Name</th>
				       <th>Location</th>
				       <th>Quantity</th>
				       <th>From Date</th>
				       <th>To Date</th>
				       <th>Time</th>
				       <th>Amount</th>
			     </tr>
			    </thead>
			    <tbody>
			     <c:forEach var="expendatureDetails" items="${requestScope['expendaturesList']}">    
			       <tr class="view_expendature">
			            <td>${expendatureDetails.serialno}</td>
			            <td class=""  id="childproductid${expendatureDetails.serialno}">
			               ${expendatureDetails.child_ProductName}
			              <span id="HiddenChildId${expendatureDetails.serialno}" class="childprdctcls" style="display:none;">${expendatureDetails.child_ProductId}$${expendatureDetails.child_ProductName}</span>
			             </td>
			            <td class="childproductsitename" id="siteSiteName${expendatureDetails.serialno}">${expendatureDetails.siteName}<input type="hidden" value="${expendatureDetails.siteId}$${expendatureDetails.siteName}" id="expendatureSiteName${expendatureDetails.serialno}"></td>
			            <td class="childproductlocation" id="siteLocationName${expendatureDetails.serialno}">${expendatureDetails.location}<input type="hidden" value="${expendatureDetails.hoardingId}$${expendatureDetails.location}" id="expendatureLocationName${expendatureDetails.serialno}"></td>  
			            <td class= "sitequantity" id="sitequantity${expendatureDetails.serialno}">${expendatureDetails.quantity}</td>
			            <td class="sitefromdate" id="siteFromDate${expendatureDetails.serialno}">${expendatureDetails.fromDate}</td>
			            <td class="sitetodate" id="siteToDate${expendatureDetails.serialno}">${expendatureDetails.toDate}</td>			            
			            <td class="sitetime" id="siteTime${expendatureDetails.serialno}">${expendatureDetails.time}</td>
			            <td  class="siteamountcls" id="siteamount${expendatureDetails.serialno}">${expendatureDetails.amount}</td>
			            <span style="display:none;" class="childproductIds">${expendatureDetails.child_ProductId}</span>
			            <span style="display:none;" class="siteIds">${expendatureDetails.siteId}</span> 
			            <span style="display:none;" class="hodingId">${expendatureDetails.hoardingId}</span>
			            <input type="hidden" name="invoiceDateHidden" id="invoiceDateHiddenId" class="form-control" value="${expendatureDetails.invoiceDate}"/>
	                </tr>	               
	               </c:forEach>       
			    </tbody>
			   </table>
			 </div>
			 <c:if test="${requestScope.totalAmount != null}">
			  <div class="col-md-12 text-right"><h4><strong>Total Amount :<c:out value="${requestScope.totalAmount}"></c:out></strong></h4></div>
			</c:if>  
			<!-- calculate expendature -->
	        <a href="" class="anchor-market" id="anchorMarket" onclick="openCalExp()">Calculate Expenditure</a>		 
		   </c:if>
		 <!-- ************************************************Marketing Inwards PO Screen***************************************** -->
		 <c:if test="${requestScope.isShow}">
			  <div class="table-responsive">
			      <table class="table table-new table-Market display table-fixed-header" style="width:100%;table-layout:fixed;border-collapse: collapse !important;" id="expenditure">
			    <thead>
			     <tr>
				       <th style="width:45px;">S.No</th>
				       <th>INVOICE ID</th>
				       <th  style="width:100px;">INVOICE DATE</th>
				        <th>SITE NAME</th>
				        <th>AMOUNT</th>
			     </tr>
			    </thead>
			    <tbody>
			      <c:forEach var="marketingPOProductDetails" items="${requestScope['marketingPOProductDetailsList']}">  
			       <tr>
			            <td>${marketingPOProductDetails.serialno}</td>
			            <td>
			             <c:url value="getAllViewExpenditures.spring" var="getAllViewExpenditures">
                         <c:param name="invoiceid" value="${marketingPOProductDetails.invoiceNumber}" />
                         <c:param name="referer" value="UpdateExpenditures" />
                         <c:param name="invoiceDateHiddenId" value="${marketingPOProductDetails.invoiceDate}" />
                         </c:url>
			            <a class="anchor-class" href="<c:out value="${getAllViewExpenditures}"/>" ><span >${marketingPOProductDetails.invoiceNumber}</span></a>
			             </td>
			             <td>${marketingPOProductDetails.invoiceDate}</td> 
			             <td>${marketingPOProductDetails.siteName}</td> 
			            <td>${marketingPOProductDetails.invoiceAmount}</td>
	               </tr>
	             </c:forEach>       
	            </tbody>
			  </table>
			 </div>
		</c:if>	 			 
	 </div>
	</div>
  </div>
 </div>
</div>
<!-- modal popup for marketing module -->
<div id="modal-marketing" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:90%;">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Calculate Expenditures</strong></h4>
      </div>
      <div class="modal-body">
      <form id="calculateexpendature" >
    
 <div class="clearfix"></div>
 <div class="col-md-12 marGinBottom">
   <div class="col-md-4">
					 <div class="form-group">
					  <label class="control-label col-md-6">Expenditure For: </label>
						<div class="col-md-6" >
							<select class="form-control" name="expendituredropdown"  id="expendatureForId">
							  <!--  <option value="select">--Select--</option> -->
							   <option value="0">--select--</option>
							   <option value="SiteWise">Site</option>
							   <option value=MultipleSite>Multiple Sites</option>
							   <option value="LocationWise">Location Wise</option>
							   <option value="BrandingWise">Branding Wise</option>
							</select><br>
						</div>
					 </div>
				</div>
			 <div class="col-md-4 class_selectsite" style="display:none;" id="siteNameforSingleSite">
					 <div class="form-group">
					   <label class="col-md-6">Select Site :</label>
					   <div class="col-md-6 autocomplete" style="">
					       <select id="siteWiseSitename" name="siteWiseSitename" class="form-control" onchange="siteWiseSitenameChange()"></select>
                        </div>
					 </div>
				</div>
			 <div class="col-md-4 class_selectsite1" style="display:none;">
					 <div class="form-group">
					  <label class="control-label col-md-6">Select Location: </label>
						<div class="col-md-6" >
							<select class="form-control" id="stateSelc">
							   <option selected="true" disabled="disabled">--Select Location--</option>
							 <!--   <option value="Ban">Banglore</option>
							   <option value="Hyd">Hyderabad</option> -->
							</select><br>
						</div>
					 </div>
				</div>
   </div>
 
   <!-- Location wise -->
    <div id="location_site" style="display:none">
         <div class="clearfix"></div>
	     <div class="table-responsive">
	       <input type = "hidden" name="locationRowcount" id="locationRowcount" value="">
	     <table class="table table-new" style="display:none" id="tempshowhide">
	       <thead>
	         <tr>
	         <th>Child Product Name</th>
	         <th class="widthonefifty">Hoarding Location</th>
	         <th>From Date</th>
	         <th>To Date</th>
	         <th>Time</th>
	         <th>Quantity</th>
	         <th>Amount</th>
	         <th>Total Amount</th>
	         <th>Site Name</th>
	         </tr>
	       </thead>
	       <tbody id="locationAdd">
	       </tbody>
	       <tbody id="locationGrandTotal">
	       	<tr class="totalMarketingAmount">
	           <td colspan="7">Grand Total</td>
	            <td id="grandTotal"></td>
	           <td></td>
	          </tr>
	       </tbody>
	     </table>
	   </div>
	   <div class="text-center center-block">
	    <button type="button" class="btn btn-warning" onclick="siteAddFunction()">ADD</button>
	   </div>
   </div>
   <!-- Location wise -->
   
   <!-- Single site -->
    <div id="single_site"  style="display:none">
         <div class="clearfix"></div>
	     <div class="table-responsive">
	     <input type = "hidden" name="singleSiteRowcount" id="singleSiteRowcount" value="">
	     <table class="table table-new" id="siteWiseSite">
	       <thead>
	         <tr>
	         <th>Child Product Name</th>
	         <th class="widthonefifty">Hoarding Location</th>
	         <th>From Date</th>
	         <th>To Date</th>
	         <th>Time</th>
	         <th>Quantity</th>
	         <th>Amount</th>
	         <th>Total Amount</th>
	         <th>Site Name</th>
	         </tr>
	       </thead>
	       <tbody id="siteAdd">   </tbody>
	       <tbody>
	       	<tr class="totalMarketingAmount">
	           <td colspan="7">Grand Total</td>
	            <td id="grandTotalsite"></td>
	            <td></td>
	          </tr>
	       </tbody>
	     </table>
	   </div>
	   <div class="text-center center-block">
	    <button type="button" onclick="siteAddFunction()" class="btn btn-warning">ADD</button>
	   </div>
   </div>
   <!-- Single site -->
   <!-- Site wise -->
   <div class="clearfix"></div>
   <div id="sitewise_Site" style="display:none;">
	     <div class="table-responsive">
	       <input type = "hidden" name="multiSiteRowcount" id="multiSiteRowcount" value="">
	       <table class="table table-new" id="expenditureTable">
	        <thead>
	         <tr>
	           <th>S.No</th>
	           <th class="widthonefifty">Child Product Name</th>
	           <th style="width:150px;">Hoarding Location</th>
	           <th>Site</th>
	           <th>Quantity</th>
	           <th>From Date</th>
	           <th>To Date</th>
	           <th>Time</th>
	           <th>Amount</th>
	           <th>Total Amount</th>
	           <th style="width:120px;">Actions</th>
	         </tr>
	       </thead>
	       <tbody id="multisiteAdd">
	        
	       </tbody>
	       <tbody>
	       <tr class="totalMarketingAmount">
	           <td colspan="9">Grand Total</td>
	            <td id="grandTotalmultisite"></td>
	            <td></td>
	         </tr>
	       </tbody>
	     </table>
	   </div>
	  <div class="text-center center-block">
		 <button type="button" class="btn btn-warning" onclick="siteAddFunction()">ADD</button>
	  </div>
   </div>
   <!-- Site Wise -->
   <!-- Branding Wise -->
   <div id="branding_site" style="display:none;">
	     <div class="table-responsive">
	     <input type = "hidden" name="brandwiseRowcount" id="brandwiseRowcount" value="">
	     <table class="table table-new" id="brandingTable">
	       <thead>
	          <tr>
		         <th class="widthonefifty">Child Product Name</th>
		         <th class="widthonefifty">Hoarding Location</th>
		         <th>From Date</th>
		         <th>To Date</th>
		          <th>Time</th>
		         <th>Quantity</th>
		         <th> Amount</th>
		         <th>Total Amount</th>
		         <th class="widthonefifty">Site Name</th>
	          </tr>
	         </thead>
	         <tbody id="brandwiseAdd">
	         </tbody>
	         <tbody id="brandwiseGrandTotal">
	          <tr class="totalMarketingAmount">
	           <td colspan="7">Grand Total</td>
	            <td id="grandTotalbrand"></td>
	            <td></td>
	           </tr>
	         </tbody>
	        </table>
	      </div>
		   <div class="text-center center-block">
		    <button type="button" class="btn btn-warning" onclick="siteAddFunction()">ADD</button>
		   </div>
       </div>
       <!-- Branding Wise -->
       
	       <input type="hidden" name="expendatureId" value="${requestScope['expendatureId']}" />
	       <input type="hidden" id="expendatureTableCount" name="expendatureTableCount">
       </form>
      </div>
    </div>
  </div>
</div> 	
			<c:if test="${requestScope.isShowAll}">
			 <c:if test="${requestScope.expendatureId}">
			   <c:out value="*******************"></c:out>
			  <c:out value="${requestScope['expendatureId']}"></c:out>
			
			  </c:if>	    	
			</c:if>	
			<c:if test="${requestScope.isShowAll}"> 
			  <c:if test="${requestScope.invoiceId}">
			   <c:out value="*******************"></c:out>
			    <input type="hidden" value="${requestScope['invoiceId']}" />
			   <c:out value="${requestScope['invoiceId']}"></c:out>
			  </c:if>	    	
			</c:if>	
        <script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.dataTables.min.js"></script>
		<script src="js/dataTables.bootstrap.min.js"></script>
        <script src="js/marketing/updateexpenditure.js" type="text/javascript"></script>
        <script src="js/marketing/jquery.timepicker.min.js"></script>  
        <script src="js/sidebar-resp.js" type="text/javascript"></script>         
     
        <script>
	        $("#datepickerfrom").datepicker({
	        	dateFormat: 'dd-M-y',
	      	  changeMonth: true,
	          changeYear: true
	        });
	        $("#datepickerto").datepicker({
	        	dateFormat: 'dd-M-y',
	      	  changeMonth: true,
	          changeYear: true
	        });
	        $("#invoiceDate").datepicker({
	        	dateFormat: 'dd-M-y',
	        	  changeMonth: true,
	            changeYear: true
	        });
	        $(document).ready(function(){    
	        	$(".up_down").click(  function() {
	        	 $(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
	    		 $(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
	    		
	    		 $("#datepickerfrom").datepicker({dateFormat: 'dd-M-y',
	       			 changeMonth: true,
	       		      changeYear: true});
	       		 $("#datepickerto").datepicker({dateFormat: 'dd-M-y',
	       			 changeMonth: true,
	       		      changeYear: true});
	    	});
      
           $("#expendatureForId").change(function(){ 
            	  var expendatureForId=$(this).val();
            	  if(expendatureForId=="SiteWise"){
            		  $("#sitename").text($("#siteName").val());
            		  $(".class_selectsite").show(); 
            		  $("#siteNameforSingleSite").show();
            		  $("#sitewise_Site").hide();
            		  $(".class_selectsite1").hide();
            		  $("#branding_site").hide();
            		  $("#location_site").hide();
            		  if($("#siteWiseSitename").val()!=0){
            			  siteWiseSitenameChange();
            			  $("#single_site").show();			  
            		  }
            	  }
                 if(expendatureForId=="MultipleSite"){
                	  $(".class_selectsite1").hide();
                	  $("#single_site").hide();
                	  $("#siteNameforSingleSite").hide();
                	  $("#sitewise_Site").show();
                	  $(".class_selectsite").hide();
                	  $("#branding_site").hide();
                	  $("#location_site").hide();
                	  getLOcation();
                	  loadMultiSiteWiseTable()
            	  } 
                  if(expendatureForId=="BrandingWise"){
                	  $("#branding_site").show();
                	  $("#siteNameforSingleSite").hide();
                	  $("#single_site").hide();
                	  $("#sitewise_Site").hide();
                	  $(".class_selectsite1").hide();
                	  $(".class_selectsite").hide();
                	  $("#location_site").hide();
                	  
                	callingPerBrandwiseCalculation("", "BrandingWise");
            	  }
                  if(expendatureForId=="0"){ debugger
                	  $(".class_selectsite1").hide();
                	  $(".class_selectsite").hide();
                	  $("#single_site").hide();
                	  $("#siteNameforSingleSite").hide();
                	  $("#sitewise_Site").hide();
                	  $("#branding_site").hide();
                	  $("#location_site").hide();
            	  }
                  if(expendatureForId=="LocationWise"){
                	  $(".class_selectsite1").show();
                	  $(".class_selectsite").hide();
                	  $("#siteNameforSingleSite").hide();
                	  $("#single_site").hide();
                	  $("#sitewise_Site").hide();
                	  $("#branding_site").hide();
                	  $("#location_site").show();
                	  getLOcation();
            	  }
              });  // POID change function close
        });
        
        $('#expenditure_table1').DataTable({
        	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
        	"scrollX": true,
        	"scrollY": "300px",
	        "scrollCollapse": true,
	        "paging": true
        	});
        $('#expenditure').DataTable({
        	"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
        	"scrollX": true,
        	"scrollY": "300px",
	        "scrollCollapse": true,
	        "paging": true	
        });
       
        
     //searching  data   
     function searchData(){
        var fromDate = $("#datepickerfrom").val();
        var toDate = $("#datepickerto").val();
        var inVoiceId = $("#invoiceid").val();
        var invoiceDate = document.getElementById("invoiceDate").value;
		var vendorName = document.getElementById("VendorNameId").value;
        if(fromDate == "" && toDate =="" && inVoiceId=="" && invoiceDate==""  && vendorName==""){
         alert("Please Fill Dates Or Invoice Number Or VendorName");
         return false;
        }
      }
     /*  ==========================================================set vendor id for start==================================================  */ 
        //to get vendor id when you change vendor name
	     function getVendorId(){
	        	  $.ajax({
	        	  url : "./getVendorDetails.jsp",
	        	  type : "get",
	        	  contentType : "application/json",
	        	  success : function(data) {
	        	  		$("#VendorNameId").autocomplete({
	        		  		source : data,
		    		  		select: function( event, ui ) { 
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
	      }
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
        		$('.loader-sumadhura').hide();
        		$("#vendorIdId").val(vendorId);
        		$("#VendorAddress").val(vendorAddress);
        		$("#GSTINNumber").val(vendorGsinNo);			
        	}
        }
        function fromDateChange(){
     		var x=$('#datepickerfrom').datepicker("getDate");
     		$("#datepickerto").datepicker( "option", "minDate",x ); 	
     	}
     	function toDateChange(){
     		var x=$('#datepickerto').datepicker("getDate");
     		$("#datepickerfrom").datepicker( "option", "maxDate",x ); 	
     	} 
      </script>
</body>
</html>
