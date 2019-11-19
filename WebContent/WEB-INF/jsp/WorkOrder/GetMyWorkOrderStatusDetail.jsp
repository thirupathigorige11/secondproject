<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@page import="com.sumadhura.bean.WorkOrderBean"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import = "java.util.ResourceBundle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"  %>

<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String tableHead = resource.getString("label.indentIssueTableHead");
	String colon = resource.getString("label.colon");
	
	String reqId = resource.getString("label.WorkOrderNo");
	String date = resource.getString("label.WorkOrderDate");
	String requestorName = resource.getString("label.requestorName");
	String requestorId = resource.getString("label.requestorId");
	
	
	String projectName = resource.getString("label.projectName");
	String block = resource.getString("label.block");
	String floor = resource.getString("label.floor");
	String flatNumber = resource.getString("label.flatNumber");
	String purpose = resource.getString("label.purpose");
	String slipNumber = resource.getString("label.slipNumber");
	String ContractorName = resource.getString("label.ContractorName");
	//String issueNumber = resource.getString("label.issueNumber");
	
	
	String serialNumber = resource.getString("label.serialNumber");
	String product = resource.getString("label.product");
	String subProduct = resource.getString("label.subProduct");
	String childProduct = resource.getString("label.childProduct");
	String quantity = resource.getString("label.quantity");
	
	String remarks = resource.getString("label.remarks");
	String uOrF = resource.getString("label.uOrF");
	String note = resource.getString("label.note");
	String productAvailability = resource.getString("label.productAvailability");
	String actions = resource.getString("label.actions");
	
	String WorkOrderNo = resource.getString("label.WorkOrderNo");
	String TempWorkOrderNo = resource.getString("label.TempWorkOrderNo");
	String WorkOrderDate = resource.getString("label.WorkOrderDate");
	String WorkOrderName = resource.getString("label.WorkOrderName");
	String panCardNo = resource.getString("label.panCardNo");
	String Address = resource.getString("label.Address");
	String phoneNo = resource.getString("label.phoneNo");
	String vendorName = resource.getString("label.contractorname");
	String WO_MajorHead = resource.getString("label.WO-MajorHead");
	String WO_MinorHead = resource.getString("label.WO-MinorHead");
	String WO_Desc = resource.getString("label.WO-Desc");
	String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency");
	String scope_Of_work= resource.getString("label.scope-Of-work");
	String measurement = resource.getString("label.UOM");
	String AcceptedRate = resource.getString("label.AcceptedRate");
	String TotalAmount = resource.getString("label.TotalAmount");
	
	//Loading Indent Issue Table Column Headers/Labels - Start
%>

<html>
<head>
<script src="js/WorkOrder/showWorkerOrder.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/loader.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">


		<jsp:include page="./../CacheClear.jsp" />  
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/sidebar-resp.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
		<!-- <title>View WorkOrder </title> -->
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<script type="text/javascript">
(function( $ ) {
    $.widget( "custom.combobox", {
      _create: function() {
        this.wrapper = $( "<span>" )
          .addClass( "custom-combobox" )
          .insertAfter( this.element );
 
        this.element.hide();
        this._createAutocomplete();
        this._createShowAllButton();
      },
 
      _createAutocomplete: function() {
        var selected = this.element.children( ":selected" ),
          value = selected.val() ? selected.text() : "";
 
        this.input = $( "<input>" )
          .appendTo( this.wrapper )
          .val( value )
          .attr( "title", "" )        
          
          .attr("id", this.element[0].name)
          //Enable below line to get input box background color - Start
          //.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
          //Enable below line to get input box background color - End
          .autocomplete({
            delay: 0,
            minLength: 0,
            source: $.proxy( this, "_source" )
          })
          .tooltip({
            tooltipClass: "ui-state-highlight"
          });
 
        this._on( this.input, {
          autocompleteselect: function( event, ui ) {
            ui.item.option.selected = true;
          
            
            this._trigger( "select", event, {
              item: ui.item.option
            });
          },
          autocompletechange: "_removeIfInvalid"
        });
      },
      
      
 	//Enable below code to create Show All Button - Start
        _createShowAllButton: function() {
        var input = this.input,
          wasOpen = false;
 
        $( "<a>" )
          .attr( "tabIndex", -1 )
          .attr( "title", "Show All Items" )
          .tooltip()
          .appendTo( this.wrapper )
          .button({
            icons: {
              primary: "ui-icon-triangle-1-s"
            },
            text: false
          })
          .removeClass("ui-corner-all" )
          .addClass("custom-combobox-toggle ui-corner-right")
          .mousedown(function() {
            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
          })
          .click(function() {
            input.focus();
 
            // Close if already visible
            if ( wasOpen ) {
              return;
            }
 
            // Pass empty string as value to search for, displaying all results
            input.autocomplete( "search", "" );
          });
      },  
    //Enable below code to create Show All Button - End 
 
      _source: function( request, response ) {
        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
        response( this.element.children( "option" ).map(function() {
          var text = $( this ).text();
          if ( this.value && ( !request.term || matcher.test(text) ) )
            return {
              label: text,
              value: text,
              option: this
            };
        }) );
      },
 
      _removeIfInvalid: function( event, ui ) {
 
        // Selected an item, nothing to do
        if ( ui.item ) {
          return;
        }
 
        // Search for a match (case-insensitive)
        var value = this.input.val(),
          valueLowerCase = value.toLowerCase(),
          valid = false;
        this.element.children( "option" ).each(function() {
          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
            this.selected = valid = true;
            return false;
          }
        });
 
        // Found a match, nothing to do
        if ( valid ) {
          return;
        }
        
        // Remove invalid value
        this.input
          .val( "" )
          .attr( "title", value + " didn't match any item" )
          .tooltip( "open" );
        this.element.val( "" );
        this._delay(function() {
          this.input.tooltip( "close" ).attr( "title", "" );
        }, 2500 );
        //this.input.autocomplete( "instance" ).term = "";
      }, 
      _destroy: function() {
        this.wrapper.remove();
        this.element.show();
      }
      
    });
  })( jQuery ); 

  $(function() {
    $("#combobox1").combobox();    
    $( "#toggle").click(function() {
      $( "#combobox1").toggle();
    });
  });
  
  $(function() {
	$( "#comboboxsubProd1").combobox();
	
  });
  
  $(function() {
	$("#comboboxsubSubProd1").combobox(); 
  });
  
  $(function() {
	  var staffId=$("#hiddenPODate").val();
	 /*  $("#workOrderDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:new Date(),
		//maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
	  }); */
  });
  
</script>

<style>
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
.fixed-tbl-rabill tbody tr td{font-weight:bold;}
.table-bold thead tr th{font-size:15px;font-weight:bold;}
.table-bold{font-weight:bold;}
.modalscopeheader{background-color:#ccc;border-radius:5px 5px 0px 0px;}
.txt-border{border:none !important;border-bottom:1px solid #000 !important;border-radius:0px;font-weight:bold;}
.border-bottom{border:none;border-bottom:1px solid #000;}
.scroll-rabill-tbl1{height:70px;overflow-y: auto;width:calc(100% - 17px); }
.scroll-rabill-tbl2{width:calc(100% - 17px); }
 /* table border color changes */
  .table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th{ border:1px solid #000;}
  .table-bordered{border:1px solid #000;}
  .table-bordered thead{background-color:#ccc;}
 /*table border color changes */
 .form-control{border:1px solid #000;}
 /* //AC */
 input[type=checkbox] {transform: scale(1.5);}
 #modaltbl{
 position: relative;
 height:300px;
 width:100%;
 overflow:hidden;
 }
 #modaltbl thead{
 width:100%;
 position: absolute;
 }
 #modaltbl tbody {
  position: absolute;
  top: 40px;
  overflow-x: hidden;
  overflow-y: scroll;
  height: 250px;
  
  }
  
  /* css for iframe modal popup */
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}

.iframe-pdf {
  opacity: 1;
  display: block;
  width: 100%;
  height: auto;
  transition: .5s ease;
  backface-visibility: hidden;
}

.middle {
  transition: .5s ease;
  opacity: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  width:100%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
}

.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.modal-lg-width{
width:95%;
}
/* text {
 background-color: #4CAF50;
 color: white; 
  font-size: 16px;
 padding: 16px 32px;
} */
.btn-fullwidth:hover{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent;
    background-color: transparent;
    border-color: transparent;
}
 .btn-fullwidth:active{
 color: transparent;
    background-color: transparent;
    border-color: transparent;
 }
 .btn-fullwidth.focus, .btn-fullwidth:focus {
    color: transparent;
    background-color: transparent;
    border-color: transparent;
}
/*css for iframe modal popup*/

/* spinner stles */
.spinnercls{
    display: inline;
    height: 50px;
    position: absolute;
    right: 0px;
    top: 1px;
    display:none;
}

 /* end spinner styles */ 
 /*fixed header in modal popup style*/ 
	 /* .table-quantity-showwo tbody tr td:first-child, .table-quantity-showwo thead tr th:first-child {min-width: 20px;text-align: center; width:50px !important;} */
	 .table-quantity-showwo thead, .table-quantity-showwo tbody tr{table-layout:fixed;display:table;width:100%;}
	 .table-quantity-showwo>thead>tr>th {text-align: center;width:100%;}
	 .table-quantity-showwo thead{width: calc(100% - 20px) !important;}
	 .fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
	 .table-quantity-showwo thead tr th,.table-quantity-showwo tbody tr td{border-bottom: 0px !important; border-left: 0px !important;}
	 
	  #DivIdToPrint thead, #DivIdToPrint tbody tr{table-layout:fixed;display:table;width:100%;}
	/*  #DivIdToPrint>thead>tr>th {text-align: center;width:100%;} */
	 #DivIdToPrint thead{width: calc(100% - 18px) !important;}
	 .fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
	 #DivIdToPrint thead tr th,#DivIdToPrint tbody tr td{border-bottom: 0px !important; border-left: 0px !important;}
	 
 /*fixed header in modal popup style*/
 /*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:62px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;font-family: arial, sans-serif;}
/*fixed header*/
     .WoGrandTotal{width:300px;float:right;font-size: 20px;margin-top:20px;}
     
 .fnt-18{
	font-weight:1000;
	font-size:18px;
 }
 .remove-button, .plus-button{
	font-size: 12px;
    padding: 8px 12px;
}
.mrg-btm-10 {
    margin-bottom: 10px;
}
.spanheading {
    font-size: 14px;
    font-weight: 1000;
    font-family: Calibri;
}
.mrg-t-10{
	margin-top: 10px;
}
</style>
<script type="text/javascript">
if (typeof (Storage) !== "undefined") {
	debugger;

	var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
	if (i == 2) {
		sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
		window.location.reload();
	}
} else {
	alert("Sorry, your browser does not support Web Storage...");
}
</script>
</head>

<body class="nav-md" id="workOrderDetailbody" style="display: none;" >
   <!-- loader  -->
	<div class="overlay_ims"></div>
		 <div class="loader-ims" id="loaderId"> 
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	</div> 
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
						<jsp:include page="./../SideMenu.jsp" /> 
                        <%-- <%@include file="./../SideMenu.jsp" %> --%> 							
						</div>
						</div>
						<jsp:include page="./../TopMenu.jsp" />  
					     <%-- <%@include file="./../TopMenu.jsp" %> --%>		
					    <!-- page content -->
						<div class="right_col" role="main">
							<div class="col-md-12">
								<ol class="breadcrumb">
									<li class="breadcrumb-item"><a href="#">Home</a></li>
									<li class="breadcrumb-item active">Work Order Status</li>
								</ol>
							</div>
						<div>	 
						 <div class="col-md-12">
						    <div>
						 		<div class="border-background-workorder isHideOrShow">
						 			<form:form modelAttribute="WorkOrderBean" id="printWorkOrderFormId" method="GET" class="form-horizontal" action="printWorkOrderDetail.spring">						 	
									   <span><font color=red size=4 face="verdana">${responseMessage}</font></span>
									<c:set scope="session" value="${WorkOrderBean}" var="WorkOrderBean"></c:set>
									<c:set scope="session" var="deletedWorkOrderDetailsList" value="${deletedWorkOrderDetailsList}"></c:set>
									<c:set scope="session" value="${listTermsAndCondition }" var="listTermsAndCondition"></c:set>
									<c:set scope="session" value="${WorkOrderLevelPurpose}" var="WorkOrderLevelPurpose"></c:set>
									<c:set scope="session" value="${workOrderCreationList}" var="workOrderCreationList"></c:set>
									<c:set scope="session" value="${listOfVerifiedEmpNames}" var="listOfVerifiedEmpNames"></c:set>

									<span style="color:red;">${noStock}</span><br/>
							            <c:set value="1" var="OperationType"></c:set>									
										<c:if test="${operType eq OperationType }">
										     <div class="col-md-6">
											      <div class="form-group">
													    <form:input path="siteWiseWONO" id="workOrderId" type="hidden"  name="workOrderId" class="form-control" readonly="true" title="${WorkOrderBean.siteWiseWONO}"/>
												        <label class="control-label col-md-6"><%= WorkOrderNo%> <%= colon %> </label>
												        <div class="col-md-6" >
													      <form:input path="workOrderNo" id="workOrderNo" class="form-control" autocomplete="off"  readonly="true"/>
												        </div>
											     </div>
										    </div>
										     <div class="col-md-6">
													<div class="form-group">
														<label class="control-label col-md-6">Old Work Order NO <%=colon%></label>
														<div class="col-md-6">
									     				  <c:if test="${fn:contains(WorkOrderBean.oldWorkOrderNo, '/')}">
										    				  	<form:input path="oldWorkOrderNo"  class="form-control" readonly="true"/>
										   				  </c:if>
										   				  <c:if test="${!fn:contains(WorkOrderBean.oldWorkOrderNo, '/')}">
										     				  	<input path="oldWorkOrderNo"  class="form-control" value="-" readonly="true">
										     			  </c:if>  
										     			</div>
													</div>
											</div>
										    	
											 <div class="col-md-6">
												 <div class="form-group">
													   <label class="control-label col-md-6"><%= WorkOrderDate%> <%= colon %> </label>
														<div class="col-md-6" >
															<form:input path="workOrderDate" id="workOrderDate" class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
														 </div>
												</div>
											</div>
										</c:if>
										<c:if test="${operType ne OperationType }">
											 <div class="col-md-6">
												 <div class="form-group">
													<label class="control-label col-md-6"><%= TempWorkOrderNo%></label>
													<div class="col-md-6" >
														<form:input path="siteWiseWONO" id="workOrderId"  name="workOrderId" class="form-control" readonly="true" title="${WorkOrderBean.siteWiseWONO}"/>
													</div>
												</div>
											</div>
											<div class="col-md-6">
												 <div class="form-group">
													<label class="control-label col-md-6"><%= WorkOrderNo%> <%= colon %> </label>
													<div class="col-md-6" >
														<form:input path="workOrderNo" id="workOrderNo" class="form-control" autocomplete="off"  readonly="true"/>
													</div>
												</div>
											</div>
										</c:if>								   
								       <div class="col-md-6">
											 <div class="form-group">
												<label class="control-label col-md-6"><%= WorkOrderName%></label>
												<div class="col-md-6" >
													<form:input path="workOrderName" id="workOrderName"  name="workOrderName" class="form-control" readonly="true" title="${WorkOrderBean.workOrderName}" />
													
													
															<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId"/>
															<form:input path="contractorId" type="hidden" name="ContractorId" id="ContractorId"/>
															<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId"/>
															<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo"/>
															<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom"/>
															<input type="hidden" name="site_id" id="site_id" value="${WorkOrderBean.siteId }">
															<form:input type="hidden" path="siteId" />
															<form:input path="typeOfWork" id="typeOfWork"  type="hidden"/>
															<form:input path="revision" id="revision"  type="hidden"/>
															<form:input path="oldWorkOrderNo" id="oldWorkOrderNo"  type="hidden"/>
															<form:input path="versionNumber" id="versionNumber"  type="hidden"/>
															<form:input type="hidden"  path="siteName" id="siteName"/>
															<form:input path="isCommonApproval" id="isCommonApproval"  type="hidden"  value="${isCommonApproval}"/>
											
															<form:input path="contractorBankAccNumber"   type="hidden" />
															<form:input path="contractorIFSCCode"   type="hidden" />
															<form:input path="contractorBankName"   type="hidden" />
															<form:input path="GSTIN" type="hidden" />
															<form:input path="QS_Temp_Issue_Id"  type="hidden" />
															<form:input path="woRecordContains" type="hidden" />
															<input name="requestFromMail" id="requestFromMail" type="hidden">
													
												</div>
											</div>
										</div>
										<c:if test="${operType ne OperationType }">
											 <div class="col-md-6">
												 <div class="form-group">
													<label class="control-label col-md-6"><%= WorkOrderDate%> <%= colon %> </label>
													<div class="col-md-6" >
														<form:input path="workOrderDate" id="workOrderDate" class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
													</div>
												</div>
											 </div>
										</c:if>						
								<c:if test="${WorkOrderBean.typeOfWork ne 'MATERIAL' }">				   
							  		   <div class="col-md-6">
											 <div class="form-group">
												<label class="control-label col-md-6"><%= vendorName %> <%= colon %>  </label>
												<div class="col-md-6" >
													<form:input path="contractorName" id="contractorName"  onkeyup="populateContractor(this);" readonly="true" autocomplete="off" class="form-control" title="${WorkOrderBean.contractorName}"/>
												 </div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-6"><%= panCardNo %> <%= colon %> </label>
												<div class="col-md-6" >
													<form:input path="contractorPanNo"  name="contractorPanCardNo" id="contractorPanCardNo" class="form-control" readonly="true" title="${WorkOrderBean.contractorPanNo}"/>
												</div>
										   </div>
								      </div>
							  		<div class="col-md-6">
										 <div class="form-group">
												<label class="control-label col-md-6"><%= Address %> <%= colon %>  </label>
												<div class="col-md-6" >
														<form:input path="contractorAddress" name="contractorAddress"  class="form-control" id="contractorAddress" readonly="true" title="${WorkOrderBean.contractorAddress}"/>
												</div>
										</div>
									</div>
									<div class="col-md-6">
										 <div class="form-group">
												 <label class="control-label col-md-6"><%= phoneNo %> <%= colon %> </label>
												 <div class="col-md-6" >
											      <form:input path="contractorPhoneNo"  name="contractorPhoneNo"  class="form-control" id="contractorPhoneNo" readonly="true" title="${WorkOrderBean.contractorPhoneNo}"/>
											    </div>
										</div>
									</div>
								</c:if>
									<c:if test="${isCompletedWO eq 'false' }">
										<input type="hidden" name="isApproveWorkOrder" id="isApproveWorkOrder" value="true">
									</c:if>
								</form:form>
						</div>
					   <c:set value="NMR" var="TypeOfWork"></c:set>
						 <div class="col-md-12 no-padding-left no-padding-right isHideOrShow">
							<div class="table-responsive"> <!-- /*tblprodindissudiv*/ -->
								<table id="indentIssueTableId" class="table table-bordered tblprodindissu" style="width:2000px;">
									<thead class="cal-thead-inwards">
							  				<tr>
							  				<th><%= serialNumber %></th>
							  				<th><%= WO_MajorHead %></th>
							  				<th><%= WO_MinorHead %></th>
							  				<th><%= WO_Desc %></th>
							  				<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
							  				<th><%= scope_Of_work %></th>
							  				</c:if>
							  				<th><%= measurement %></th>
							  				<th><%= quantity %></th>
							  				<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
							  				<th><%= AcceptedRate %></th>
							  				</c:if>
							  				
							  				<th><%= TotalAmount %></th>
							  				<th><%= note %></th>
							  				
							  				</tr>
										</thead>
						  				<tbody class="tbl-fixedheader-tbody">
						  					<c:set value="0" var="indexnumber"></c:set>
								  			<c:forEach items="${workOrderCreationList}"  var="workOrderDetail">  
								  				<c:set value="${indexnumber+1}" var="indexnumber"></c:set>								  		
								  					<input  name="isDelete${indexnumber}" id="isDelete${indexnumber}" readonly="true" value="z" class="form-control input-visibilty${indexnumber}" type="hidden" />
													<tr id="tr-class${indexnumber}">
														<td>				
															<div id="snoDivId1">${indexnumber}</div>
															<input name="dispplayedRows" type="hidden" value="${indexnumber}">	
															<input type="hidden" name="actualWorkOrderNo" value="${WorkOrderBean.siteWiseWONO}">
															<input type="hidden" name="nextApprovelEmpId" value="${WorkOrderBean.approverEmpId}">
															<input type="hidden" name="actualtempIssueId" value="${workOrderDetail.QS_Temp_Issue_Id}">	
															<input type="hidden" name="actualQS_Temp_Issue_Dtls_Id${indexnumber}" value="${workOrderDetail.QS_Temp_Issue_Dtls_Id}">
															<input type="hidden" name="actualWorkAreaID" value="${workOrderDetail.workAreaId}">
															
														</td>
														<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_MajorHead1}'/>" data-placement="left">
															<input type="hidden" name="actualwoMajorHead${indexnumber}" value="${workOrderDetail.woMajorHead}$${workOrderDetail.WO_MajorHead1}">
															<input type="text" id="comboboxProd${indexnumber}" readonly class="form-control"  value="<c:out value='${workOrderDetail.WO_MajorHead1}'/>">
														</td>
														<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>">
														  <input type="text" name="actualWO_MinorHead${indexnumber}" class="form-control"  readonly="readonly" value="<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>">
														
														</td>
														<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_Desc1}'/>">
															<input type="hidden" value="${workOrderDetail.wODescription}" id="WODESC${indexnumber}">
															<input type="text" name="actualWO_Desc${indexnumber}" id="workDesc${indexnumber}" class="form-control"  readonly="readonly" value="<c:out value='${workOrderDetail.WO_Desc1}'/>">
														</td>
													     <c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
														<td>
															<input type="text" class="form-control" name="woManualDesc" id="woManualDesc"  value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"   onclick="showScopeOfWork(${indexnumber})"   readonly="readonly"/>
														<!-- modal popup for scope of work start-->
																	<div id="modalForScopeWork${indexnumber}" class="modal fade" role="dialog">
																	  <div class="modal-dialog" style="width:90%">
																	
																	    <!-- Modal content-->
																	    <div class="modal-content">
																	      <div class="modal-header modalscopeheader">
																	        <button type="button" class="close" data-dismiss="modal">&times;</button>
																	        <h4 class="modal-title text-center"><strong>Scope Of Work</strong></h4>
																	      </div>
																	      <c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}" />
																	    
																	      <c:forEach var="scopeWork" items="${scopeOfWorkParts}">
																	       <div class="modal-body" style="overflow:hidden;">
																	          <div id="textboxDiv">
																	          </div>
																	          <div class="col-md-12 mrg-btm">
																	          <div class="form-group">
																	            <div class="col-md-12">  <input type="text" class="form-control txt-border scopeofworkid${indexnumber}" value="<c:out value='${scopeWork}'></c:out>" title="<c:out value='${scopeWork}'></c:out>"name="ScopeOfWork${indexnumber}"></div>
																	            <!-- <div class="col-md-1"><button class="btn btn-success Addscope_txt"><i class="fa fa-plus"></i></button></div> -->
																	          </div>
																	          </div>
																	          <input type="hidden" id="hiddenrownum">
																	      </div>
																	      </c:forEach>
																	  
																	      
																	      <div class="modal-footer">
																	        <div class="text-center center-block">
																	        <!--  <button type="button" class="btn btn-warning">Submit</button> -->
																	        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
																	        </div>
																	      </div>
																	    </div>
																	
																	  </div>
																	</div>
																	<!-- modal popup for scope of work end -->
														</td>
													</c:if>
													<td data-toggle="tooltip" title="${workOrderDetail.unitsOfMeasurement1}">
														<input type="hidden" value="${workOrderDetail.unitsOfMeasurement}" id="UOM${indexnumber }">
														<input type="text" name="actualunitsOfMeasurement${indexnumber}" id="actualunitsOfMeasurement${indexnumber}" class="form-control"  value="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>" readonly="readonly" title="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>"/>
													</td>
													<td class="w-70" data-toggle="tooltip">
														<input type="hidden" name="actualQuantity${indexnumber}" value="${workOrderDetail.quantity}">
													    <input  id="Quantity${indexnumber}" type="text" name="Quantity${indexnumber}" readonly="true" value="${workOrderDetail.quantity}" title="${workOrderDetail.quantity}"  class="form-control input-visibilty${indexnumber} workordertotalqty"/> 												
														<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">		
															<a href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber})">Click Here</a>
															<c:if test="${WorkOrderBean.woRecordContains ne 'LABOR' }">
																<a href="javascript:void(0)" style="cursor: pointer;display: block;" onclick="showWDGroupWiseMaterialDetails(${indexnumber})"  id="showMaterialQty${indexnumber}">Material Details</a>
														    </c:if>
														    <img src="images/spinner.gif" class="spinnercls" id="spinner${indexnumber}"></img>
													    </c:if>
													
														<div id="appendWorkOrderArea1${indexnumber}">
														</div>
														<div id="appendWorkOrderArea2${indexnumber}">
														</div>
														<div id="appendWorkOrderArea${indexnumber}">
														</div>
													</td>
													<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
															<td>
																<input type="hidden" id="actualAcceptedRate${indexnumber}" name="actualAcceptedRate${indexnumber}" value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}">
														     	<input id="AcceptedRate1${indexnumber}" type="text" onfocusout="calCulateTotalAmout('a_rate',this.value,${indexnumber})" name="AcceptedRate${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber} workorderacceptedRate" value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}"/>
															</td>
													</c:if>
													<td>
													   <input type="hidden" name="actualTotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}">
														<input name="TotalAmount${indexnumber}" type="text" value="${workOrderDetail.totalAmount1}" title="${workOrderDetail.totalAmount1}"  id="TotalAmountId${indexnumber}" readonly="true"   class="form-control totalRowamount" />
													</td>
													<td>
													    <input type="hidden" name="actualComments${indexnumber}" value="<c:out value='${workOrderDetail.comments1}'></c:out>">
														<input id="Comments${indexnumber}" value="<c:out value='${workOrderDetail.comments1}'></c:out>" title="<c:out value='${workOrderDetail.comments1}'></c:out>" name="Comments${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber}"/>
														<input type="hidden" name="IsComments" value="" id="hiddenCommentsId">
													</td>								
												</tr>
											</c:forEach>						
										</tbody>				
									</table>
								</div>
							</div>	
							<!-- <div class="WoGrandTotal isHideOrShow">
								<div style="float:left;width:120px;">Grand Total</div>
								<div style="float:left;width:20px;">:</div>
								<div style="float:left;width:160px;" id="WoGrandTotalAmount"></div>
							</div> -->
								
														
							<div class="col-md-6 isHideOrShow" style="margin-top:20px;">
								<div class=" ">
									 <label class="control-label col-md-4 no-padding-left" >Note: </label>
									 <div class="col-md-8 no-padding-left no-padding-right" style="margin-bottom: 10px;">
										<textarea href="#" data-toggle="tooltip" title="<c:out value='${WorkOrderLevelPurpose}'></c:out>"   id="NoteId" name="Note" class="form-control" autocomplete="off" placeholder="<c:out value='${WorkOrderLevelPurpose}'></c:out>"></textarea>
										<%-- <textarea  href="#" data-toggle="tooltip" title="${WorkOrderLevelPurpose}" class="form-control textarea-control-area" autocomplete="off" placeholder="<c:out value='${WorkOrderLevelPurpose}'></c:out>"></textarea> --%>
								     </div>
								</div>
							</div>		
							
							
		 <div class="col-md-6" style="padding-left:0px;margin-top:15px;margin-bottom:30px;"> 
	 		<c:if test="${WorkOrderBean.typeOfWork ne 'NMR'}">	
				 <div class="col-md-6 fnt-18 text-left">Labor Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="laborWoAmountSpan">${WorkOrderBean.laborWoAmount}</span> </div>
					 <div class="clearfix"></div>
					 <div class="col-md-6 fnt-18 text-left">Material Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="materialWoAmountSpan">${WorkOrderBean.materialWoAmount}</span></div>
					 <div class="clearfix"></div>
					 <div class="col-md-6 fnt-18 text-left">Grand Total</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="WoGrandTotalAmount">${WorkOrderBean.laborWoAmount+WorkOrderBean.materialWoAmount}</span></div> 
			</c:if>
			<c:if test="${WorkOrderBean.typeOfWork eq 'NMR'}">
				<div class="WoGrandTotal">
					 <div style="float:left;width:120px;">Grand Total</div>
					 <div style="float:left;width:20px;">:</div>
					 <div style="float:left;width:160px;" id="WoGrandTotalAmount">${WorkOrderBean.laborWoAmount+WorkOrderBean.materialWoAmount}</div>
				</div>
			</c:if>
 		</div>
													
							<div class="pull-left">
								<c:if test="${listTermsAndCondition.size()!=0 }">
									<label>Terms And Conditions :</label>
								</c:if>
								<ul>
								<c:forEach items="${listTermsAndCondition }" var="TAC">
									<input type="hidden" name="actualterms_condition_id" value="${TAC.TERMS_CONDITION_ID}">
									<input type="hidden" value="${TAC.TERMS_CONDITION_DESC}" name="actualTC" id="terms${TAC.indexNumber}" ><br>
									<%-- <input type="text" value="${TAC.TERMS_CONDITION_DESC}" name="changedTC" size="${TAC.TERMS_CONDITION_DESC.length()*2}"  id="terms${TAC.indexNumber}" readonly="readonly" ><br> --%>
									 <c:if test="${not empty TAC.TERMS_CONDITION_DESC.trim()}">
										<li>${TAC.TERMS_CONDITION_DESC}</li>
									</c:if>
								</c:forEach>
								</ul>
							</div>
							<div class="clearfix"></div>
							<div class="pull-left">
								<c:if test="${operType ne OperationType}">
									<c:if test="${deletedWorkOrderDetailsList.size()!=0 }">
										<label style="margin-top: 25px;"> Modification Details:</label>
									</c:if>
									<ul>
										 <c:forEach items="${deletedWorkOrderDetailsList}"  var="workOrderDeletedDetail">
									         <li>${workOrderDeletedDetail.remarks }</li>
										 </c:forEach>
									 </ul>
									 <input type="hidden" value="ViewMyWOStatus.spring" id="pageType">
							 	</c:if>
							 	
							 	<c:if test="${showWOModificationDetails eq true}">
									<c:if test="${modificationDetailsList.size()!=0 }">
										<label style="margin-top: 25px;"> Modification Details :</label>
									</c:if>
									<ul>
										 <c:forEach items="${modificationDetailsList}"  var="workOrderDeletedDetail">
									         <li>${workOrderDeletedDetail.REMARKS}</li>
										 </c:forEach>
										 <!-- <li><a href="getMyCompletedWorkOrder.spring?WorkOrderNo=WO/SIPL/SOH/5&workOrderIssueNo=264&site_id=105&operType=1&isUpdateWOPage=false&isHideOrShowData=true" style="text-decoration: underline;color: blue;">WO/SIPL/SOH/5</a></li> -->
									 </ul>
							 	</c:if>
							 	
								<!-- ***********************************************this is for pdf file download start******************************************************** -->
							
								<!-- this is common file for showing images  -->
								<%@include file="ImgPdfCommonJsp.jsp" %>
						
							</div>
							<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
							<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
							<input type="hidden" name="" value="${isCompletedWO}" id="isCompletedWO12">
							
							
							<c:set value="1" var="one"></c:set>
							<div class="col-md-12 col-xs-12 text-center center-block" >
								<input type="button" class="btn btn-warning" value="Print" id="printBtnId" onclick="printWorkOrder()" style="margin-bottom:5px;">
								<c:if test="${operType eq OperationType }">
									<c:if test="${WorkOrderBean.typeOfWork ne 'PIECEWORK'}">
										<c:if test="${WorkOrderBean.woRecordContains ne 'LABOR' }">
											<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="Product Details" id="productDetails" onclick="return productDetails();">
										</c:if>
									</c:if>
								</c:if>
							<c:if test="${isCompletedWO eq 'false' }">
								<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork}">
									<c:if test="${WorkOrderBean.woRecordContains ne 'LABOR' }">
										<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="Product Details" id="productDetails" onclick="return productDetails();">
									</c:if>
								</c:if>
							</c:if>
							<c:if test="${operType eq OperationType }">
									<!-- if this page is PIECE WORK show this Button's   check the type of work -->		
								<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork && WorkOrderBean.typeOfWork ne 'MATERIAL'}">
										<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Payments" id="WorkOrderPayments" onclick="return WorkOrderPayments();" data-toggle="modal" data-target="#modal-workorder-payments">
										<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Abstract" id="WorkOrderAbstract" onclick="return WorkOrderAbstract();" data-toggle="modal" data-target="#modal-certificatepaymentra-click">
										<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="Product Details" id="productDetails" onclick="return productDetails();">
									<c:if test="${fn:contains(WorkOrderBean.oldWorkOrderNo, '/')}">
										<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="View modification Details" id="WorkOrderVersion" onclick="return WorkOrderVersion();">
									</c:if>
								</c:if>
									<!-- if this page is NMR show this Button's  check the type of work -->
									<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork}">
											<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Payments" id="NMrWorkOrderPayments" data-toggle="modal" data-target="#WorkOrderNMRPayments">
											<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Abstract"  id="NmrAbstract" data-toggle="modal" data-target="#WorkOrderNMRAbstract">
										<c:if test="${fn:contains(WorkOrderBean.oldWorkOrderNo, '/')}">
											<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="View modification Details" id="WorkOrderVersion" onclick="return WorkOrderVersion();" >
										</c:if>
									</c:if>
									<input type="hidden" value="viewMyWorkOrders.spring" id="pageType">
							</c:if>
							</div>
							<div class="clearfix"></div>

<!-- Model -->
<!-- Model For Work description wise group wise material details -->
<div id="ViewMaterialProductDetails" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:95%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">Material Area Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive" id="materialAreaDetails">
       <table class="table table-bordered table-createwo-showquantity" >
       <thead style="color: black;">
			<tr>
				<th>S.No</th>
				<th>Work Description</th>
				<th>Group Name</th>
				<th>UOM</th>
			 	<th>Total Quantity</th>
			</tr>
		</thead>
       <tbody id="showWDGroupWiseMaterialDetails">
      
       
       </tbody>
       </table>
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>


<!-- Model -->
<!-- Model For Work description wise group wise area wise material details -->
<div id="ViewAreaWiseMaterialDetails" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:95%;">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">Area Wise Material Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive">
       <table class="table table-bordered table-createwo-showquantity" >
       <thead style="color: black;">
			<tr>
				<th>S.No</th>
				<th>Work Description</th>
				<th>Group Name</th>
				<th>UOM</th>
				<th>Block</th>
				<th>Floor</th>
				<th>Flat</th>
				<th>Total Quantity</th>
				<!-- <th>Issued Quantity</th>
				<th>Available Quantity</th> -->
			</tr>
		</thead>
       <tbody id="showAreaWiseWDGroupMaterialDetails">
      
       
       </tbody>
       </table>
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>


							
							
							<!-- second table start -->
							<div id="myModal" class="modal fade" role="dialog">
							   <div class="modal-dialog modal-lg" style="width:90%;">							
							          <!-- Modal content-->
							         <div class="modal-content">
									      <div class="modal-header modalscopeheader">
									        <button type="button" class="close" data-dismiss="modal">&times;</button>
									        <h4 class="modal-title text-center"><strong>Work Order Details</strong></h4>
									      </div>
									      <div class="modal-body modal-content-scroll">
									       	<div class="table-responsive">
												 <table class="table table-bordered table-quantity-showwo" > <!-- id="modaltbl"  removed-->
												  <thead>
													  <tr>
														    <th>Block</th>
														    <th>Floor</th>
														    <th>Flat</th>
														    <th>Measurement</th> 
														    <th>Record Type</th>  
														    <th>Allocated Area</th>  
														    <th>Accepted Rate(<%=acceptedRateCurrency %>)</th>    
														</tr>
												  </thead>												 
												  <tbody id="areaMapping" class="fixed-table-body">
												  </tbody>
												 </table>
										     </div>
									      </div>
									     <div class="modal-footer">
									        <div class="col-md-12 text-center center-block"><button type="button" class="btn btn-warning" data-dismiss="modal">Close</button></div>
									     </div>
							           </div> 
							        </div>
						       </div>
							 <!-- ============================ NMR Payment Model Popup Start ===============================================================
							
							<!-- second table start -->
							<div id="WorkOrderNMRPayments" class="modal fade" role="dialog">
							  <div class="modal-dialog modal-lg" style="width:90%;">
							
							    <!-- Modal content-->
							    <div class="modal-content">
							      <div class="modal-header">
							        <button type="button" class="close" data-dismiss="modal">&times;</button>
							        <h4 class="modal-title text-center">WorkOrder NMR Payments</h4>
							      </div>
							      <div class="modal-body modal-content-scroll">
							       	<div class="table-responsive"  id="appendNMRBillDetailsNoDataMsg">
									  <table style="border:1px solid #000;width:100%;margin-bottom:30px;margin-top:30px;display:none;" class="bordsertable"  id="paymentLedgerTable">
							                   <thead>
								                 <tr style="background-color: #ccc;">
						    	                   <th style="padding:8px;text-align:center;width:5%;border:1px solid #000;">Date</th>
						                           <th style="padding:8px;text-align:center;border:1px solid #000;">Particular</th>
						                             <th style="padding:8px;text-align:center;border:1px solid #000;">Bill Amount</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Advance</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Advance Deduction</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">SD Release</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Material Deductions</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Petty Expenses</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Other Amount</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Hold Amount</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Hold Release</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">SD 5%</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Amount Paid</th>
												     <th style="padding:8px;text-align:center;border:1px solid #000;">Cumulative Total</th>   
						                        </tr>                        
											   </thead>
											  <tbody id="ledger">
											  </tbody>
									         </table>
								   </div>
							      </div>
							     <div class="modal-footer">
							        <div class="col-md-12 text-center center-block"><button type="button" class="btn btn-warning" data-dismiss="modal">Close</button></div>
							     </div>
							   </div>
							 </div>
						 </div>
						 
						 <!-- ============================ NMR Payment Model Popup End ===============================================================
						 	
						 <!-- ============================================ NMR Bill Details Start  ===================================================== -->
						 <!-- second table start -->
							<div id="WorkOrderNMRAbstract" class="modal fade" role="dialog">
							  <div class="modal-dialog modal-lg" style="width:90%;">
							
							    <!-- Modal content-->
							    <div class="modal-content">
							      <div class="modal-header">
							        <button type="button" class="close" data-dismiss="modal">&times;</button>
							        <h4 class="modal-title text-center"><strong>WorkOrder NMR Abstract</strong></h4>
							      </div>
							      <div class="modal-body"> <!-- modal-content-scroll -->
							       	<div class="table-responsive">
								      <table style="width:100%;margin-bottom:15px;margin-top:-1px;border:1px solid #000;" class="table bordsertable" id="DivIdToPrint">
							              <thead style="font-size: 16px;">
								               <tr style="background-color: #ccc;">
						    	                     <th rowspan="2" style="padding:8px;text-align:center;width:50px;border:1px solid #000;">S.NO</th>
						                           <th rowspan="2" style="padding:8px;text-align:center;border:1px solid #000;">Description</th>
						                           <th rowspan="2" style="padding:8px;text-align:center;border:1px solid #000;">Total Qty</th>
						                           <th rowspan="2" style="padding:8px;text-align:center;border:1px solid #000;">Rate</th>
						                           <th rowspan="2" style="padding:8px;text-align:center;border:1px solid #000;">Unit</th>
						                           <th colspan="2" style="padding:8px;text-align:center;border:1px solid #000;">Cumulative Certified</th>
						                       <!--<th colspan="2" style="padding:8px;text-align:center;">Previous Certified</th>
						                           <th colspan="2" style="padding:8px;text-align:center;">Current Certified</th> -->
						                        </tr>
						                         <tr style="background-color: #ccc;">
						                            <th style="padding:8px;text-align:center;border:1px solid #000;">Qty</th>
						                            <th style="padding:8px;text-align:center;border:1px solid #000;">Amount</th>                         
						                          <!--   <th style="padding:8px;text-align:center;">Qty</th>
						                            <th style="padding:8px;text-align:center;">Amount</th>                         
						                            <th style="padding:8px;text-align:center;">Qty</th>
						                            <th style="padding:8px;text-align:center;">Amount</th> -->
						                         </tr>
										  </thead>
										 <tbody id="NMRAbstractTableData" class="fixed-table-body">
												    
										 </tbody>
									</table> 
								</div>
							    </div>
							    <div class="modal-footer">
							        <div class="col-md-12 text-center center-block">
								        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
								        <!-- <input type='button' id='btn' value='Print' onclick='printDiv();'> -->
							        </div>
							     </div>
							  </div>
							</div>
						 </div>
						<!-- ============================================ NMR Bill Details End  ===================================================== -->
						<!-- Modal -->
						<div id="modal-workorder-payments" class="modal fade" role="dialog">
						  <div class="modal-dialog modal-lg" style="width:90%">
						
						    <!-- Modal content-->
						    <div class="modal-content">
						      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal">&times;</button>
						        <h4 class="modal-title text-center">Work Order Payments</h4>
						      </div>
						      <div class="modal-body">
						        <div class="table-responsive" id="appendBillDetailsNoDataMsg">						    
								   <table class="table table-bordered table-bold">
									   <thead>
										    <tr>
												 <th>Date</th>
											     <th>Particular</th>
											     <th>Bill Amount</th>
											     <th>Advance</th>
											     <th>Advance Deduction</th>
											     <th>SD Release</th>
											     <th>Material Deductions</th>
											     <th>Petty Expenses</th>
											     <th>Other Amount</th>
											     <th>Hold Amount</th>
											     <th>Hold Release</th>
											     <th>SD 5%</th>
											   <!--   <th>TDS %</th>
		     									 <th>GST %</th> -->
											     <th>Amount Paid</th>
											     <th>Cumulative Total</th>
											</tr>
									   </thead>
									   <tbody id="appendBillDetails">
									  
									   </tbody>
								 </table>
						     </div>
						  </div>
					      <div class="modal-footer">
						        <div class="text-center center-block">
						         <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						        </div>
					      </div>
						 </div>
					  </div>
				</div>	
						
				<!-- Modal -->
				<div id="modal-certificatepaymentra-click" class="modal fade" role="dialog">
				    <div class="modal-dialog modal-lg modal-rabill-customwidth">						
					    <!-- Modal content-->
					    <div class="modal-content">
						    <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal">&times;</button>
						        <h4 class="modal-title text-center"><strong>Abstract</strong></h4>
						     </div>
						     <div class="modal-body modal-body-scroll">
						         <div class="table-responsive scroll-rabill-tbl2"> <!--  -->
							        <table class="table table-bordered"style="margin-bottom:-1px;"> <!-- tbl-rabill -->
								        <thead>
								           <tr>
								              <th rowspan="2" class="vertical-alignment" style="width:31%;font-size:15px;font-weight:bold;">Description of Work</th>
								              <th rowspan="2" class="vertical-alignment" style="width:14%;font-size:15px;font-weight:bold">Total Qty</th>
								              <th rowspan="2" class="vertical-alignment" style="width:14%;font-size:15px;font-weight:bold">Rate</th>
								              <th rowspan="2" class="vertical-alignment" style="width:14%;font-size:15px;font-weight:bold">Unit</th>
								              <th colspan="2" style="font-size:15px;font-weight:bold">Cumulative Bill</th>
								              <!-- <th colspan="2" >Previous Certified</th> -->
								              <!-- <th colspan="2">Current Certified</th> -->
								            </tr>
								            <tr>
								              <th style="width:15%;font-size:15px;font-weight:bold">Qty </th>
								              <th style="width:14%;font-size:15px;font-weight:bold">Amount</th>
								            </tr>
								        </thead>
							        </table>
						        </div>
						        <div style="height:auto;overflow-y:scroll;max-height:400px;">
							        <table class="table table-bordered fixed-tbl-rabill">
								         <tbody id="paymentByArea" >
								
								         </tbody> 
									</table>
						        </div>
						      </div>
					         <div class="modal-footer">
						        <div class="text-center center-block">
						         <input type="button" name="abstractPrint" id="abstractPrintBtn" class="btn btn-warning" onclick="return abstractPrintBtn();"  value="Print Abstract"/>
						         <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						        </div>
					        </div>
						  </div>
						 </div>
					</div>	
							
							  <%-- <%@ include file="CommonBillsCode.jsp" %> --%>
							  
								<!-- model popup for Bills Details end  -->
							  
							  
								<!-- model popup for pdf start  -->
							<%
							 
								for(int i=0;i<pdfcount;i++){
							  	String pdfName="pdf"+i;
							  	String PathdeletePdf="PathdeletePdf"+i;
							  	log(pdfName);
						
									%>
									   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
									   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
									 <%
											if(request.getAttribute(pdfName)!=null){
									%>
									<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
										  <div class="modal-dialog modal-lg-width">
										
										    <!-- Modal content-->
										    <div class="modal-content">
										      <div class="modal-header">
										        <button type="button" class="close" data-dismiss="modal">&times;</button>
										        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i+1 %></strong></h4>
										      </div>
										      <div class="modal-body">
										         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
												 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
												 <embed src="data:application/pdf;base64,${requestScope[pdfBase64]}" style="height:500px;width:100%;">
										      </div>
										      <div class="modal-footer">
										       <p class="text-center">
											     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
											  	<%--  <button type="button" class="btn btn-warning" id="deletePdf" onclick="deletepdf(<%=i %>,'${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button> --%>
											   </p>
										      </div>
										    </div>
										 </div>
									</div>
							<%} %>
						<%} %>
						
						
						<!-- pdf model popup end  -->
							
							<!-- modal popup for image pop start-->
						<!-- Modal -->
							 <!-- Modal -->
							<%	 imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
									for (int i = 0; i < imagecount; i++) { 
								String index="image"+i;				
								log(index);
								%>
							  <div class="modal fade custmodal" id="uploadinvoice-img<%=i%>" role="dialog">
						    <div class="modal-dialog modal-lg custom-modal-lg">
						    
						      <!-- Modal content-->
						      <div class="modal-content">
						        <div class="modal-header cust-modal-header">
						          <button type="button" class="close" data-dismiss="modal">&times;</button>
						          
						        </div>
						        <div class="modal-body cust-modal-body">
						    <c:set value="<%=index %>" var="i"></c:set>
						    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope[i]}" />
						          
						        </div>
						        <div class="modal-footer cust-modal-footer">
						          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
						        </div>
						      </div>
						      
						    </div>
						  </div>
						  <%} %>
								<!-- modal popup for invoice image end -->    
							
							
							<!-- <div id="table-quantity" style="display:none;">
						
							 <div class="col-md-12 text-center center-block">
							 <button class="btn btn-warning" id="hide-table-2">Close</button>
							 </div>
							 </div> -->
							<!-- second table end -->
							</div>
							<!-- /page content -->        
						</div>
	              </div>
	          </div>
	        </div>
	  </div>
	<!-- <link href="js/inventory.css" rel="stylesheet" type="text/css" /> -->
	<script src="js/custom.js"></script>
	<script type="text/javascript" src="js/WorkOrder/NMRCompleted.js"></script>
	
	<script src="js/WorkOrder/NMRBills.js"></script>
	<script src="js/WorkOrder/NMRTable.js"></script>
	<script src="js/WorkOrder/WOCommonCode.js"></script>
	  <!-- using this common code in View My WorkOrder Revise Work Order -->

		 <script> 
		 
		 	//if this request from mail then submit form to print work order details
			var requestFromMail =window.location.href.split("&requestFromMail=")[1];
			debugger;
			if(requestFromMail == "true"){
				$("#requestFromMail").val(true);
				document.getElementById("printBtnId").disabled = true;   
			    document.getElementById("printWorkOrderFormId").submit();
			}else{
				$("#workOrderDetailbody").show();
			} 
		 
			 function productDetails(){
				var url="viewProductDetails.spring";
				//$('#printWorkOrderFormId').attr('target', '_blank');
				document.getElementById("printWorkOrderFormId").action = url;
				document.getElementById("printWorkOrderFormId").method = "POST";
				document.getElementById("printWorkOrderFormId").submit();  	 
			 }
		 
			function WorkOrderPayments(){
				debugger;
				var contractorId=$("#contractorId").val();
				var workOrderNo=$("#workOrderId").val();
				var approvePage="false";
				var site_id=$("#site_id").val();
				$("#hideIt").hide();
				$.ajax({
					url : "loadAdvBillCertificatateDetails.spring",
					type : "GET",
					data : {
						contractorId : contractorId,
						workOrderNo:workOrderNo,
						approvePage:approvePage,
						site_id:site_id
							},
					success : function(data) {
						$("#appendBillDetails").html("");
					
						var paybleAmt=0.00;
						var htmlData="";
						var advHtmlData="";
						var raHtmlData="";
						var initiatedAdv=0.00;
						var raPaidAmt=0.00;
						var holdPrevAmount=0.00;
						var raPrevCertifiedAmount=0.00;
						var advPrevCertifiedAmount=0.00;
						var deduction_amount=0.00;
						var sec_percentege=0.00;
						var ADV=0.00;
						var SEC=0.00;
						var PETTY=0.00;
						var OTHER=0.00;
						var RECOVERY=0.00;
						var secDeposit=0.00;
						var RA_TDS_AMOUNT=0.00;
						var RA_GST_AMOUNT=0.00;
						var ADV_TDS_AMOUNT=0.00;
						var ADV_GST_AMOUNT=0.00;
						var appendPrevDeductVal="";
						$.each(data,function(key,value){
							debugger;
						var billStatus=value.STATUS;
							//$("#totalAmtToPay").val(value.TOTAL_WO_AMOUNT);
							/* if(billStatus=="A"){
								billStatus="Active";
							}else if(billStatus=="R"){
								billStatus="Rejected";
							}else if(billStatus=="I"){
								billStatus="Completed";
							}		 */
							initiatedAdv++;
						
							var emp=value.PENDING_EMP_ID;
							var payment_type=value.PAYMENT_TYPE;
							if(payment_type=="ADV"){
								var printTDS_Amount=0.00;
								var printGSTAmount=0.00
								paybleAmt+=+value.PAYBLE_AMOUNT;
								advPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
								ADV_GST_AMOUNT=+value.GST_AMOUNT;
								printGSTAmount=value.GST_AMOUNT;
								
								var status=value.STATUS=="R"?" Advance Rejected ":value.STATUS;
								if(value.DEDUCTION_AMOUNT!=null){
									var typeOfDeduction=new Array();
									var typeOfDeductionVal=new Array();
									//type of deduction in coming in the array where is spliting by @@
									typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
									//type of DEDUCTION_AMOUNT in coming in the array where is spliting by @@
									typeOfDeductionVal=	value.DEDUCTION_AMOUNT.split("@@")
									for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
										 if(typeOfDeduction[i2]=="TDS"){
											ADV_TDS_AMOUNT+=+typeOfDeductionVal[i2];
											printTDS_Amount=typeOfDeductionVal[i2];
										}
									}
							}
								var pendingid=" Advance Completed ";

								raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><a  target='_blank' style='color:#0000ff;' class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>"+value.CERTIFIED_AMOUNT+"</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
								
								/* raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printTDS_Amount+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printGSTAmount+"</span></td>";
 */								
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+value.PAYBLE_AMOUNT+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span></td></tr>";
								holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);
							}else if(payment_type=="RA"){
									raPaidAmt+=+value.PAYBLE_AMOUNT;
									//raPaidAmt+=+value.CERTIFIED_AMOUNT;
									raPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
									RA_GST_AMOUNT+=+value.GST_AMOUNT;
									var printGSTAmount=value.GST_AMOUNT;
									sec_percentege=value.SEC_PERCENTEGE;
									var status=value.STATUS=="R"?" RA Rejected ":value.STATUS;
									var pendingid=" RA Completed ";
								//new code 
									var printAdvAmount=0;
									var printSecDeposit=0;
									var printPettyExpences=0;
									var printOther=0;
									var printRecoveryAmt=0;
									var printTDS_Amount=0.00;
									if(value.DEDUCTION_AMOUNT!=null){
										var typeOfDeduction=new Array();
										var typeOfDeductionVal=new Array();
										//type of deduction in coming in the array where is spliting by @@
										typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
										//type of DEDUCTION_AMOUNT in coming in the array where is spliting by @@
										typeOfDeductionVal=	value.DEDUCTION_AMOUNT.split("@@")
										for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
											if(typeOfDeduction[i2]=="ADV"){
												deduction_amount+=+typeOfDeductionVal[i2];
												printAdvAmount=typeOfDeductionVal[i2];
											}else if (typeOfDeduction[i2]=="RECOVERY"){
												RECOVERY+=+typeOfDeductionVal[i2];
												printRecoveryAmt=typeOfDeductionVal[i2];
											}else if(typeOfDeduction[i2]=="SEC"){
												secDeposit+=+typeOfDeductionVal[i2];
												printSecDeposit=typeOfDeductionVal[i2];
											}else if (typeOfDeduction[i2]=="PETTY"){
												PETTY+=+typeOfDeductionVal[i2];
												printPettyExpences=typeOfDeductionVal[i2];
											}else if (typeOfDeduction[i2]=="OTHER"){
												OTHER+=+typeOfDeductionVal[i2];
												printOther=typeOfDeductionVal[i2];
											}else if(typeOfDeduction[i2]=="TDS"){
												RA_TDS_AMOUNT+=+typeOfDeductionVal[i2];
												printTDS_Amount=typeOfDeductionVal[i2];
											}
										}
		
								}
 
								debugger;
								raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><a   target='_blank' style='color:#0000ff;'  class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>"+value.CERTIFIED_AMOUNT+"</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printAdvAmount+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printRecoveryAmt+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printPettyExpences+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printOther+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printSecDeposit+"</span></td>";
								
/* 								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printTDS_Amount+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+printGSTAmount+"</span></td>";
 */								
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span class='amountPaid'>"+value.PAYBLE_AMOUNT+"</span></td>";
								raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span class='cumulativeTotal'>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span</td></tr>";
								holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);
 							}	
						});
					
						// raHtmlData+=" <tr style='background-color: #ccc;'><td style='padding:10px;border:1px solid #000;'>Total Amount</td><td style='padding:10px;border:1px solid #000;'><span>"+(advPrevCertifiedAmount.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(raPrevCertifiedAmount.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(raPaidAmt+paybleAmt).toFixed(2)+"</span></td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'><span>"+(deduction_amount.toFixed(2))+"</span> </td><td style='padding:10px;border:1px solid #000;'><span>"+(RECOVERY.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(secDeposit.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(PETTY.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(OTHER.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'></td></tr>";
					
					
				raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
	/* 			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>"; */
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td></tr>";
				
				raHtmlData+="<tr  style='border: 1px solid #000;background-color:#ccc;padding:5px;'><td style='padding:10px;border:1px solid #000;'>Total Amount</td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(raPrevCertifiedAmount)+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(advPrevCertifiedAmount)+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(deduction_amount)+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span><span>"+(RECOVERY)+"</span></span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span><span>"+(PETTY)+"</span></span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(OTHER)+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(secDeposit)+"</span></td>";
				/* raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+(ADV_TDS_AMOUNT+RA_TDS_AMOUNT).toFixed(2)+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+parseFloat(RA_GST_AMOUNT+ADV_GST_AMOUNT).toFixed(2)+"</span></td>"; */
				
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td>";
				raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td></tr>";
						
				//$("#appendBillDetails").append(raHtmlData);
				$("#appendBillDetails").append(raHtmlData);
						
				if(advPrevCertifiedAmount==0&&raPrevCertifiedAmount==0){
					$("#appendBillDetailsNoDataMsg").html("<h3 style='text-align:center;'>No payment has been initiated for this Work Order.</h3>");
				}
				$('span').each(function(){
					// Do your magic here
				    if (isNum($(this).text())){ // regular expression for numbers only.
				    	var tempVal=parseFloat($(this).text());
				    	if(this.id!="site_id"&&this.id!="workOrderId"&&this.id!="contractorPhoneNo"&&this.id!="MobileNo"&&this.id!="notificationBell")
				   			$(this).text(inrFormat(tempVal.toFixed(2)));
				          }
						});
					},error:function(errer){
						
					}
			});
				
				$("#hideIt").show();
			}//WorkOrderPayments
			
			function ViewProductDetails() {
				var url="viewProductDetails.spring";
				//$('#printWorkOrderFormId').attr('target', '_blank');
			      document.getElementById("printWorkOrderFormId").action = url;
			      document.getElementById("printWorkOrderFormId").method = "POST";
			      document.getElementById("printWorkOrderFormId").submit();
			}
			
			function WorkOrderVersion(){
				debugger;
				  var url="showAllWorkOrderVersion.spring";
				//$('#printWorkOrderFormId').attr('target', '_blank');
			      document.getElementById("printWorkOrderFormId").action = url;
			      document.getElementById("printWorkOrderFormId").method = "POST";
			      document.getElementById("printWorkOrderFormId").submit();  
			}
			
			
			function WorkOrderAbstract(){
				debugger;
				var contractorId=$("#contractorId").val();
				var workOrderNo=$("#workOrderId").val();
				var site_id=$("#site_id").val();
				var tempBillNo="00";
				var billType="RA";
				var site_id=$("#site_id").val();
				$("#hideIt").hide();
				/* $(".overlay_ims").show();
				$(".loader-ims").show(); */
			
				$.ajax({
					url : "loadWorkOrderArea.spring",
					type : "GET",
					data : {
						contractorId : contractorId,
						workOrderNo:workOrderNo,
						site_id:site_id,
						billType:billType
					},
					success : function(data	) {
		
						str="";
						
						var i=1;
						var basic_amount=0;
						var price=0;
						var area_alocated_qty=0;
		
						var majorHeadName="";
						var minorHeadName="";
						var workDescName="";
						var majorHeadLength=1;
						var textHeading=65;
						var increIndex=1;
						var workdescIncre=1;
						
						var sumOfTotalQunatity=0;
						var sumOfPrevQty=0;
						var sumOfCumulativeQTy=0;
						var sumOfPrevAmount=0;
						var sumOfCumulativeAmount=0;
						var sumOfAllocatedQuantity=0;
						var sumOfCurrentAmount=0;
						$.each(data,function(key,value){
							debugger;
						//data.sort(sortFloor);
							basic_amount=(value.TOTAL_WO_AMOUNT==null?"0":value.TOTAL_WO_AMOUNT);
						//$("#totalAmtToPay").val(basic_amount);
						var blockName=(value.BLOCK_NAME==null?"-":value.BLOCK_NAME);
						var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
						var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
					
						price=value.PRICE;
						area_alocated_qty+=value.AREA_ALOCATED_QTY;
					//	if(approvePage=="true"){
		
						debugger;
						//cheking is mejor head name is already printed or not 
						
						if(majorHeadName!=value.WO_MAJORHEAD_DESC){
						//if(i==majorHeadLength){
							majorHeadLength++;
							//stroing major head name and work desc in temp variable
							majorHeadName=value.WO_MAJORHEAD_DESC.trim();
							workDescName=value.WO_WORK_DESCRIPTION;
							minorHeadName=value.WO_MINORHEAD_DESC;
							increIndex=1;
							//print Major Head Name
							str+=" <tr>" +
								/*"  <td class='text-center'>"+(String.fromCharCode(textHeading++))+"</td>" +*/
								"  <td class='text-center'style='word-break:break-all;font-size:15px;' colspan='6'><strong>"+value.WO_MAJORHEAD_DESC+"</strong></td>" +
								/* "  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'><input type='hidden' value='CertifiedTotalQTY'></td>" +
								"   <td class='text-right'style='width:113px;'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid'></td>" + */
								"   </tr>";
			 
							 str+=" <tr>" +
								/*"  <td class='text-center'>"+(String.fromCharCode(textHeading++))+"</td>" +*/
								"  <td class='text-center'style='word-break:break-all;font-size:14px;' colspan='6'><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +
								/* "  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'></td>" +
								"  <td class='text-right'style='width:114px;'><input type='hidden' value='CertifiedTotalQTY'></td>" +
								"   <td class='text-right'style='width:113px;'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid'></td>" + */
								"   </tr>";
			 
							str+=" <tr> " +
								/*	"<td class='text-center'>"+romanize(workdescIncre++)+"</td>" +*/
								"  <td class='text-center'style='word-break:break-all;font-size:13px;' colspan='6'>"+value.WO_WORK_DESCRIPTION+"</td>" +
								/* "  <td class='text-right'></td>" +
								"  <td class='text-right'></td>" +
								"  <td class='text-right'></td>" +
								"  <td class='text-right'><input type='hidden' value='Certified'></td>" +
								"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid"+i+"'></td>" +
								 */
								"   </tr>";
							
					//	}
						//checking is work description is changed or not if changed execute this block to print the work description
						}else if(minorHeadName!=value.WO_MINORHEAD_DESC){
							//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
							
							minorHeadName=value.WO_MINORHEAD_DESC;
							str+=" <tr>  " +
									/*"<td class='text-center'>"+romanize(workdescIncre++)+"</td>" +*/
							"  <td class='text-center'style='word-break:break-all;font-weight: bold;font-size:14px;' colspan='6' >"+value.WO_MINORHEAD_DESC+"</td>" +
							/* "  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'><input type='hidden' value='Certified'></td>" +
							"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid"+i+"'></td>" +
						 */
							"   </tr>";
							
						}
						if(workDescName!=value.WO_WORK_DESCRIPTION){
							//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
							
							workDescName=value.WO_WORK_DESCRIPTION;
							str+=" <tr>  " +
									/*"<td class='text-center'>"+romanize(workdescIncre++)+"</td>" +*/
							"  <td class='text-center'style='word-break:break-all;font-size:13px;' colspan='6'>"+value.WO_WORK_DESCRIPTION+"</td>" +
							/* "  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'><input type='hidden' value='Certified'></td>" +
							"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid"+i+"'></td>" +
						 */
							"   </tr>";
						
						}
						var flatName=value.NAME==null?"-":value.NAME;
						var floorName=value.FLOOR_NAME==null?"-":value.FLOOR_NAME;
						var txtid=value.WO_WORK_AREA_ID;
					
						var lengthOfThePrevArea=0;
						var customMsg="";
						var previousBillAmount=0;
						var pravArea=0;
						var prevAreaQuantity=new Array();
						debugger;
						try {
							var index=value.PREVAREA.search("@@");
							if(index>=0){
								prevAreaQuantity=value.PREVAREA.split("@@");
								for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
									let array_element = prevAreaQuantity[ind];
									var tempQtyAndRate=array_element.split("-");
									pravArea+=parseFloat(tempQtyAndRate[0]);//array position (0) getting prev QTY
									var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
									
									previousBillAmount+=tempQtyAndRate[0]*rate;
									if(ind<prevAreaQuantity.length-1){
										customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
									}else{
										customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
									}
									
									lengthOfThePrevArea++;
								}
								
							}else{
								var tempQtyAndRate=value.PREVAREA.split("-");
								pravArea=parseFloat(tempQtyAndRate[0]);
								previousBillAmount+=tempQtyAndRate[0]*tempQtyAndRate[2];
								customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
							}
						} catch (e) {
							console.log(e);
						}
					
		  				var newchar = '+';
						prevAreaQuantity = prevAreaQuantity.toString().split(',').join(newchar);
						 sumOfPrevQty+=+pravArea;
						 sumOfCumulativeQTy+=+pravArea;
						 sumOfPrevAmount+=+(previousBillAmount);
						 sumOfCumulativeAmount+=+(previousBillAmount);
						 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
					
						 var blockNames='';
							if(value.FLATNAME!=null){
								blockNames=blockNames+" , "+value.FLATNAME;
							}
							if(value.FLOOR_NAME!=null){
								blockNames=blockNames+" , "+value.FLOOR_NAME;
							}
						 
						 
						str+=" <tr>" +
								/*"<td class='text-center'>"+(increIndex++)+"</td>" +*/
								"  <td class='text-left' style='word-break:break-all;width: 188px;'>"+value.BLOCK_NAME+blockNames+"   <br><span  style='word-break:break-all;width: 188px;'>"+customMsg+"</span><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" +
								"  <td class='text-right'><span id='TotalQty"+i+"'>"+value.AREA_ALOCATED_QTY+"</span></td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
								"  <td class='text-right'><span id='TotalRate"+i+"'>"+value.PRICE+"</span></td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
								"  <td class='text-right'><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
								"  <td class='text-right'><span id='CBQty"+i+"'>"+pravArea+"</span> <input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
								"  <td class='text-right'><span id='CBAmount"+i+"'>"+((previousBillAmount).toFixed(2))+"</span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
								
								"   </tr>";
							i++;	
						});
						
						
						str+=" <tr>  " +
						/*	"<td class='text-center'>"+(increIndex++)+"</td>" +*/
							"  <td class='text-left' style='width:31%;'><strong>Total Amount/Quantity</strong> </td>" +
							"  <td class='text-right' style='width:14%;'><span>"+sumOfTotalQunatity.toFixed(2)+"</span></td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
							"  <td class='text-right'style='width:14%;' > </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
							"  <td class='text-right' style='width:14%;'></td>" +
							"  <td class='text-right'style='width:15%;'> <span id='cumulativeQuantity'  style='display:none'>"+sumOfCumulativeQTy.toFixed(2)+"</span></td>" +
							"  <td class='text-right'style='width:14%;'><span id='cumulativeAmount'>"+sumOfCumulativeAmount.toFixed(2)+"</span></td>" +
							
							"   </tr>";
		
						
						$("#paymentByArea").html(str);
						$('span').each(function(){
							// Do your magic here
						    if (isNum($(this).text())){ // regular expression for numbers only.
						    	var tempVal=parseFloat($(this).text());
						    	if(this.id!="site_id"&&this.id!="workOrderId"&&this.id!="contractorPhoneNo"&&this.id!="MobileNo"&&this.id!="notificationBell")
						    		$(this).text(inrFormat(tempVal.toFixed(2)));
					           }
						});
						$("#hideIt").show();
						/* $(".overlay_ims").hide();
						$(".loader-ims").hide(); */
					
					},
					error : function(err) {
						alert(err);
					}
				});
			}//WorkOrderAbstract
				
			function abstractPrintBtn(){
				debugger;
				 var contractorName=$("#ContractorName").val();
				 var contractorId=$("#ContractorId").val();
				 var url="printWOAbstract.spring";
				 $('#printWorkOrderFormId').attr('target', '_blank');
			     document.getElementById("printWorkOrderFormId").action =url;
			     document.getElementById("printWorkOrderFormId").method = "POST";
			     document.getElementById("printWorkOrderFormId").submit();
		  }
			
		</script> 
		  
		  
		  
		  
		  
		  
		  <script>
			setTimeout(function(){
				debugger;
				 /*  $(".workorderacceptedRate").each(function(){
					
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					
					$(".workordertotalqty").each(function(){
					
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					$(".totalRowamount").each(function(){
					
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					}); */
				
				
				/* this method will get executd from NMRComplted.js  which is used to load the completed Bill's */
				debugger;
				var typeOfWork="${WorkOrderBean.typeOfWork}"
				var operationTyp="${operType}";
				if(typeOfWork=="NMR"){
					if(operationTyp=="1"){
						loadNMRCompletedBillData();
						loadNMRBillData();
						$('strong').each(function(){
							// Do your magic here
						    if (isNum($(this).text())){ // regular expression for numbers only.
						    	var tempVal=parseFloat($(this).text());
						    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
						    		$(this).text(inrFormat(tempVal.toFixed(2)));
					           }
						});
					}
				}
			}, 300);
			
			function printDiv() 
			{
				var divToPrint=document.getElementById('DivIdToPrint');
				var newWin=window.open('','Print-Window');
				newWin.document.open();
				newWin.document.write('<html><head> <style></style>  </head><body onload="window.print()">'+divToPrint.innerHTML+'</body></html>');
				newWin.document.close();
				setTimeout(function(){newWin.close();},10);
			}
			
			function loadNMRBillData() {
				debugger;
				var ContractorId = $("#ContractorId").val();
				var ContractorName = $("#ContractorName").val();
				var workOrderNo = $("#workOrderNo").val();
				var approvePage = $("#approvePage").val();
				var typeOfWork = "NMR";
				var siteId = $("#siteId").val();
				$("#printWorkOrderNo").text(workOrderNo.split("$")[0]);
				if (workOrderNo.length == 0) {
					return false;
				}
				
				$.ajax({	url : "loadNMRBillData.spring",
							type : "GET",
							data : {
								contractorId : ContractorId,
								workOrderNo : workOrderNo,
								approvePage : approvePage,
								siteId : siteId,
								typeOfWork : typeOfWork
							},
							success : function(data) {
								var workDescNames = new Array();
								var minorHeadNames = new Array();
		
								var strNMRFirstRowtbodyData = "";
								var strNMRtheadData = "";
								var strNMRSecondRowtbodyData = "";
								var strNMRThirdRowtbodyData = "";
								var strAbstractTableData = "";
								var strFirstRowOfTableHead = "";
								var strSecondRowOfTableHead = "";
								var serialNumber = 1;
								var d = new Date();
								var date = d.toLocaleDateString();
								var totalQty=0;								
								lengthOfRows = data.length;
								// dynamic Headers for table
								$("#noOfRowsToIterate").val(1);
								console.log("data " + data);
								var tempMinorHeadName = "";
								var tempWorkDescName = "";
								var tempWDName = new Array();
								var minorHeadNames2=new Array();
								// Total Number of minor head and major head's
								var noOfWorkDesc = 0;
								var noOfMinorHead = 0;
								var regExpr = /[^a-zA-Z0-9-. ]/g;
								$.each(data,function(key, value) {
													debugger;
								var regExpr = /[^a-zA-Z0-9-. ]/g;
												
								minorHeadNames.push(value.WO_MINORHEAD_DESC.replace(regExpr, "")+ "@@"+ value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+ "@@" + value.RATE);
													
								let	WO_WORK_DESCRIPTION = value.WO_WORK_DESCRIPTION.replace(/ /g, '');
									try {
										WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.trim();
										WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.replace(regExpr, "");
		
									} catch (e) {
										console.log(e);
									}
		
									var previousQty = value.PREVQTY == null ? 0	: value.PREVQTY;
		
								/* if (tempMinorHeadName != value.WO_MINORHEAD_DESC) {
										tempMinorHeadName = value.WO_MINORHEAD_DESC;
								noOfMinorHead++;
								strAbstractTableData += '<tr>';
								strAbstractTableData += '<td style="text-align:center;padding:5px;width:50px;border-top:1px solid #000;"><h5><strong></strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'
								+ value.WO_WORK_DESC_ID+ '"><input type="hidden" name="majorheadId" id="majorheadId" value="'+ value.WO_MAJORHEAD_ID+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID+ '"></td>';strAbstractTableData += '<td style="padding:5px;border-top:1px solid #000;border-right:1px solid #000;" colspan="11"><h5><strong>'+ tempMinorHeadName+ '</strong><h5><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+ value.WO_MEASURMENT_ID+ '"></td>';
							strAbstractTableData += '</tr>';
							tempWorkDescName = "";
							} */
								debugger;
								const needle = value.WO_WORK_DESCRIPTION+ "@@" + value.WO_WORK_DESC_ID;
								const isInArray = tempWDName.includes(needle);
								if (isInArray == false) {
									noOfWorkDesc++;
										workDescNames.push(WO_WORK_DESCRIPTION+ "@@"+ value.WO_WORK_DESC_ID);
										tempWDName.push(needle);
								}
								/* if (tempWorkDescName != value.WO_WORK_DESCRIPTION) { */
									if (isInArray == false) {
										tempWorkDescName = value.WO_WORK_DESCRIPTION;
										var previousQty = 0;
										var prevAreaQuantity = new Array();
										var lengthOfThePrevArea = 0;
										var previousNMRBillAmount=0;
										var printPreviousRateAndQty="";
											debugger;
									/* try {
										var index = value.PREVQTY.search("@@");
											if (index >= 0) {
													prevAreaQuantity = value.PREVQTY.split("@@");
												for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
													let	array_element = prevAreaQuantity[ind].split("&&");
													var noOfWorker=parseFloat(array_element[0]);
													var noOfHrs=parseFloat(array_element[1]);
													//var c=noOfWorker+noOfHrs;
													var tempPreviousQty = parseFloat(((noOfWorker* noOfHrs) / 8));
													var num =tempPreviousQty;
													var n = num.toFixed(2);
													
													previousQty=previousQty+(parseFloat(n));
													previousNMRBillAmount+=parseFloat(n)*array_element[2];
													
													if(ind<prevAreaQuantity.length-1&&array_element[0]!=0){
														printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+"),";	
													}else if(array_element[0]!=0){
														printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+")";
													}
												}
											} else {
													prevAreaQuantity = value.PREVQTY.split("&&");
													previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
													previousQty=previousQty.toFixed(2);
													previousNMRBillAmount=(previousQty)*prevAreaQuantity[2];
													//printPreviousRateAndQty+="(Qty = "+previousQty+" , Rate = "+prevAreaQuantity[2]+")";
												}
												//previousNMRBillAmount=previousNMRBillAmount.toFixed(2);
											previousQty=previousQty.toFixed(2);
										} */
										debugger;
										try {
											var index = value.PREVQTY.search("@@");
											if (index >= 0) {
											prevAreaQuantity = value.PREVQTY.split("@@");
											var tempPreviousQty=0;
											var tempAmount=0;
												for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
													let	array_element = prevAreaQuantity[ind].split("##");
													var noOfWorker=parseFloat(array_element[0]);
													var noOfHrs=parseFloat(array_element[1]);
													var rate=parseFloat(array_element[2]);
													//	var c=noOfWorker+noOfHrs;
													 tempPreviousQty += parseFloat(((noOfWorker* noOfHrs)));
													 tempAmount+= parseFloat(((noOfWorker* noOfHrs*rate)));
													/*var num =tempPreviousQty;
													var n = num.toFixed(2);		*/									
													previousQty=tempPreviousQty;
													previousNMRBillAmount=tempAmount;
													/*previousNMRBillAmount+=parseFloat(n)*array_element[2];				
													if(ind<prevAreaQuantity.length-1&&array_element[0]!=0){
															printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+"),";	
													}else if(array_element[0]!=0){
															printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+")";
													}*/
												}
											} else {
												debugger;
												prevAreaQuantity = value.PREVQTY.split("##");
												previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]));
												previousQty=previousQty.toFixed(3);
												previousNMRBillAmount=parseFloat(previousQty)*prevAreaQuantity[2];
												//	printPreviousRateAndQty+="(Qty = "+previousQty+" , Rate = "+prevAreaQuantity[2]+")";
											}
												previousNMRBillAmount=((previousNMRBillAmount)/8).toFixed(2);
									}
										catch (e) {
											console.log(e);
										}
										previousQty=(previousQty/8).toFixed(3);
										totalQty+=value.QUANTITY;
														strAbstractTableData += '<tr>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;width:50px;"><h5>'+ serialNumber+ '</h5><input type="hidden" name="workDescId" id="workDescId" value="'+ value.WO_WORK_DESC_ID+ '"><input type="hidden" name="majorheadId" id="majorheadId" value="'+ value.WO_MAJORHEAD_ID+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID+ '"></td>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong >'+ value.WO_WORK_DESCRIPTION+ '</strong><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+ value.WO_MEASURMENT_ID+ '"></td>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong  id="'+ WO_WORK_DESCRIPTION+ 'TotalQty"  class="TotalQuantity">0</strong></td>';
							                            strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong id="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Rate">'+ value.RATE+ '</strong></td>';
							                            strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong>'+ value.WO_MEASURMEN_NAME+ '</strong></td>';
							                            strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong class="CBQtyClass" name="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" id="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" >'+ previousQty+ '</strong></td>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;border:1px solid #000;"><strong class="CBAmountClass" name="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" >'+ ((previousNMRBillAmount))+ '</strong></td>';
														/* strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong class="PCQtyClass" name="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" id="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" >'+ previousQty+ '</strong></td>';
														strAbstractTableData += ' <td style="text-align:center;padding:5px;"><strong class="PCAmountClass" name="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" >'+ ((previousQty * value.RATE).toFixed(2))+ '</strong></td>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong name="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty"  id="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" class="CCQtyClass"></strong></td>';
														strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong name="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" class="CCAmountClass"></strong></td>';
		 */												strAbstractTableData += '</tr>';
														serialNumber++;
														$("#NMRAbstractTableData").append(strAbstractTableData);
														strAbstractTableData="";
														// }
													}/* else{
															var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
															$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(2));
														} */
														const isThis_Str_InArray2 = value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+"@@"+value.WO_ROW_CODE;
														const isInArray2 = minorHeadNames2.includes(isThis_Str_InArray2);
														if (isInArray2 == false) {
															debugger;
															minorHeadNames2.push(isThis_Str_InArray2);
															var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
															$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(2));
														}
										});
			
								var totalQty=0;
								$(".TotalQuantity").each(function(){
									debugger;
									var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
									totalQty+=currentvalue;
								});
								strAbstractTableData += '<tr style="background-color: #ccc;">';
								strAbstractTableData += '<td class="text-center"  style="text-align:center;padding:13px;border:1px solid #000;width:50px;"></td> 	';
								strAbstractTableData += '<td class="text-center"  style="text-align:center;padding:13px;border:1px solid #000;">Total</td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;border:1px solid #000;"><strong>'+totalQty+'</strong></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;border:1px solid #000;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;border:1px solid #000;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;border:1px solid #000;"><strong><span id="TotalACBQty"></span></strong></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;border:1px solid #000;"><strong><span id="TotalACBAmt"></span></strong></td>';
				/* 				strAbstractTableData += '<td style="text-align:center;padding:13px;"><span id="TotalAPCQty"></span></td>';
								strAbstractTableData += '<td class="text-center"><span id="TotalAPCAmt"></span></td>';
								strAbstractTableData += '<td class="text-center"><span id="TotalACCQty"></span></td>';
								strAbstractTableData += '<td class="text-center"><span id="TotalACCAmt"></span><input type="hidden" id="CertifiedAmount" name="CertifiedAmount"></td>';
		 */						strAbstractTableData += '</tr>';
		
						/* 		strAbstractTableData += '<tr>';
								strAbstractTableData += '<td class="text-center"></td> 	';
								strAbstractTableData += '<td class="text-center"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
								strAbstractTableData += '<td class="text-center"></td>';
								strAbstractTableData += '<td class="text-center"></td>';
								strAbstractTableData += '<td class="text-center"></td>';
								strAbstractTableData += '</tr>';
		 */
		
								$("#NMRAbstractTableData").append(strAbstractTableData);					
								CalculateTotalAbstract();
								//alert("width: "+(1500+50*workDescNames.length));
								$("#NMRBillstableId").css("width", (2000+150*workDescNames.length));
								$("#NMRBillstableIdfirstRow").append(strNMRSecondRowtbodyData);
								$("#NMRBillstableIdfirstRow").append(strNMRThirdRowtbodyData);
								$("#Date1").datepicker({ dateFormat: 'dd-mm-yy' });
								
								
								$('strong').each(function(){
									// Do your magic here
								    if (isNum($(this).text())){ // regular expression for numbers only.
								    	var tempVal=parseFloat($(this).text());
								    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
								    		$(this).text(inrFormat(tempVal));
							           }
								});
								$(".overlay_ims").hide();
								$(".loader-ims").hide();
							}
						});
			}
			
			
			//checking contractor name and work order number and date 
			function openAbstract(){ 
				var NameoftheContractor=$("#contractorName").val(); 
				var WorkOrderRefNo=$("#workOrderNo").val(); 
				
				if(NameoftheContractor==""){
					alert("Please enter contractor name");
					$("#ContractorName").focus();
					return false;
				}
				if(WorkOrderRefNo==""){
					alert("Please select work order reference number");
					$("#workOrderNo").focus();
					return false;
				}
				$("#NmrAbstract").attr("data-toggle", "modal");
				$("#NmrAbstract").attr("data-target", "#NMRBillTableModal");
			}

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
		/* 			function sortFloor(a, b) {
						debugger;
						
						var	majorHeadName=a.WO_MAJORHEAD_DESC;
						var	majorHeadName1=b.WO_MAJORHEAD_DESC;
						
						
						 if(majorHeadName > majorHeadName1) {
							    return 1;
						 }else if (majorHeadName1 > majorHeadName) {
							    return -1;
						 }else{ 
							 var	workDescName1=b.WO_WORK_DESCRIPTION;
							 var	workDescName2=b.WO_WORK_DESCRIPTION;
							 if (workDescName1 > workDescName2) {
								    return 1;
							 } else if (workDescName2 > workDescName1) {
								    return -1;
							 } else { 
								 var block1 = a.BLOCK_NAME;
								 var block2 = b.BLOCK_NAME;
								 if (block1 > block2) {
									    return 1;
								 } else if (block2 > block1) {
									    return -1;
								 } else { 
									  var floor1=a.FLOOR_NAME==null?"":a.FLOOR_NAME;
									  var floor2=b.FLOOR_NAME==null?"":b.FLOOR_NAME;
									 
									   var isText1=floor1.charAt(0);
										  var isText2=floor2.charAt(0);
									  debugger;
		 							  if(isNaN(isText1)){
		 								 floor1="0"+floor1;
										  //return 1;
									  }else if(isNaN(isText2)){
										  floor2="0"+floor2;
										//  return 1;
									  }  
									//if(!isNaN(isText1)&&!isNaN(isText2))
									  if(floor1>floor2){
										  return 1;
									  }else  if(floor2>floor1){
										return -1;
									  }else{
										return   0;
									  }
									 return 0;
								 }
								 return 0;
							 }
							return 0;	  
						  }
			} */
					
		
				var rowsIncre=1;
				function showScopeOfWork(row){
					$('#modalForScopeWork'+row).modal('show'); 
				}
				function myFunction(workOrderID,acceptedRate,row) {
					$("#spinner"+row).show();
					$("#myModal").modal();			
					var appendId=$("#allocatedareaAppend").val();
					//make the div data empty
							    if (typeof(Storage) !== "undefined") {
							         sessionStorage.setItem("${UserId}tempRowsIncre",row);
						        } else {
						           alert("Sorry, your browser does not support Web Storage...");
						        };
								$("#areaMapping").html("");
								 $("#appendWorkOrderArea"+row).html("");
								debugger;
								 mesurmentId= $("#workDesc"+row).val();
								 if(mesurmentId==undefined||mesurmentId==""||mesurmentId==null){
									alert("Please select Work descripition.");
									 return false;
								 }
								 mesurmentId= $("#WODESC"+row).val();
									var  UnitsOfMeasurementId=$("#UOM"+row).val();
								var url = "loadWOAreaMapping.spring";
								 var site_id=$("#site_id").val();
								 var workOrderNo=$("#workOrderNo").val();
								$.ajax({
									url : url,
									type : "get",
									data:{
										acceptedRate:acceptedRate,
										workOrderID:workOrderID,
										mesurmentId:mesurmentId,
										UnitsOfMeasurementId:UnitsOfMeasurementId,
										operType:"${operType}"	,
										siteId:site_id,
										workOrderNo:workOrderNo
										},
					
									success : function(data) {
		
										var str="";
										   var data1="";
										   var totalAreaQty=0;
										   var rowNum=row;	   
										$.each(data,function(key,value){
									debugger;
										if(value.PRICE!=undefined&&value.PRICE!=""){
										//	var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
											var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
											var flatname=value.FLATNAME==null?"-":value.FLATNAME;
											value.WO_WORK_AREA_ID;
										str+="<tr> "
											+"<td title='"+value.WO_WORK_AREA_ID+"'>   "+value.BLOCK_NAME+"</td> "
											+"<td> "+floorName+"</td>"
											+"<td>"+flatname+"</td>"
											+"<td id='"+value.WO_WORK_AREA_ID+"WO_MEASURMEN_NAME'> <strong>"+value.WO_MEASURMEN_NAME.toUpperCase()+"</td>"
											+"<td id='"+value.WO_WORK_AREA_ID+"RECORD_TYPE'> <strong>"+value.RECORD_TYPE.toUpperCase()+"</td>"
											
											+"<td id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA' > "+inrFormat(value.AREA_ALOCATED_QTY) +" </td>"
											+"<td id='"+value.WO_WORK_AREA_ID+"PRICE' > "+inrFormat(value.PRICE) +" </td>"
											+"</tr>";
										//	totalAreaQty+=+value.AREA_ALOCATED_QTY;
										}
										rowNum++;
										});
										//alert(totalAreaQty);
										//$("#Quantity"+row).val(totalAreaQty);
										$("#appendWorkOrderArea1"+row).html(data1);
										$("#areaMapping").html(str);
										$("#spinner"+row).hide();
									},
									error : function(data, status, er) {
										alert(data + "_" + status + "_" + er);
									}
								});
		
								$("#table-quantity").show();
				}
				
				$(document).ready(function() {
					//$("#printWorkOrderFormId :input").prop("readonly", true);
				//For Printing Abstract
				
					
								//this code is for appending all the values of allocated area
								$("#hide-table-2").click(function() {
							
									$("#table-quantity").hide();
								});
								/* selectall checkbox */
								$("#checkAll").click(function() {
											$('input:checkbox').not(this).prop('checked', this.checked);
								console.log("checked");
								});
		
				});
				
				function deletepdf(divId,imagePath){
					  debugger;
						 var canISubmit = window.confirm("Do you want to delete PDF?");
						 if(canISubmit == false) {
						        return false;
						 }
						 deleteFile(divId,imagePath);
					}
				
				 function deleteWOFile(divId,imagePath){
					 debugger;
					 
				 var canISubmit = window.confirm("Do you want to delete image?");
				     
				 if(canISubmit == false) {
				        return false;
				 }
				 deleteFile(divId,imagePath);
				}
				 function deleteFile(divId,imagePath){
				/* 	 debugger;
					 
				 var canISubmit = window.confirm("Do you want to delete image?");
				     
				 if(canISubmit == false) {
				        return false;
				 }	 */
				 var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
				 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val();
				 imagesAlreadyPresent=imagesAlreadyPresent-1;
				 $("#imagesAlreadyPresent").val(imagesAlreadyPresent);
				 $.ajax({
					  url : url,
					  type : "get",
					// contentType : "application/json",
					  success : function(data) {
					console.log(data);
					 /* $("#imgId"+divId).remove();
						 $("#ishidden").append("<div style=\"display:block;margin-bottom:15px;\"><input type=\"file\"  accept=\"image/*\"  name=\"file\"></div>"); */
						 window.location.reload();
					  },
					  error:  function(data, status, er){
						  alert(data+"_"+status+"_"+er);
						  }
					  });
				 return false;
				 }
				 //to distroy loader
				 $(window).load(function () {
					 calGrandTotal();
					 $('input[type="text"]').each(function(){
						    if (isNum(this.value)){ // regular expression for numbers only.
						    	var tempVal=parseFloat(this.value);
						    	if(this.id!="site_id"&&this.id!="workOrderId"&&this.id!="contractorPhoneNo"){
					        		//$(this).val(tempVal.toFixed(2));
								   	debugger;
								    var temp=	$(this).val(inrFormat(tempVal.toFixed(2)));
								    console.log(temp);
						    	}
					           }
						});
					 $(".overlay_ims").hide();	
					 $(".loader-ims").hide();
				});
				 function isNum(value) {
					  var numRegex=/^[0-9.]+$/;
					  return numRegex.test(value);
				}
				 function calGrandTotal(){
					 var WoGrandTotal=0;
						$(".totalRowamount").each(function(){						
							WoGrandTotal+=parseFloat($(this).val()==""?0:parseFloat($(this).val().replace(/,/g,'')));
						});
						$("#WoGrandTotalAmount").text(inrFormat(parseFloat($("#WoGrandTotalAmount").text()).toFixed(2)));
						$("#laborWoAmountSpan").text(inrFormat(parseFloat($("#laborWoAmountSpan").text()).toFixed(2)));
						$("#materialWoAmountSpan").text(inrFormat(parseFloat($("#materialWoAmountSpan").text()).toFixed(2)));
				 }
				//this code for to active the side menu 
					var referrer=$("#pageType").val();
					$SIDEBAR_MENU.find('a').filter(function () {
				           var urlArray=this.href.split( '/' );
				           for(var i=0;i<urlArray.length;i++){
				        	 if(urlArray[i]==referrer) {
				        		 return this.href;
				        	 }
				           }
				    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');					
				</script>	
  </body>
</html>
