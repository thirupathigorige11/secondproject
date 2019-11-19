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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
	String WO_Manual_Desc = resource.getString("label.WO-Manual-Desc");//not used
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
		<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
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
           // alert(this.element[0].name);
            //alert(ui.item.option.value);
            //alert(ui.item);
            
            //alert(this.input.id);
            //var id = this.input.text();
            //alert(this.input);
            
            //var count = 1;
            //var isProdSel = false;
            
            var WOMajorHeadId = "";
            var WOMajorHeadName = "";
            
            var WOMinorHeadId = "";
            var WOMinorHeadName = "";
            
            var WOWorkDescId = "";
            var WOWorkDescName = "";
            
            WOMajorHeadId = ui.item.option.value;
            WOMajorHeadName = ui.item.value;
            
            var ele = this.element[0].name;
            //alert(ele);
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            //alert("After removing numbers = "+str1);
           debugger; 
	        var WO_MajorHead =  "<%= WO_MajorHead %>";
	        WO_MajorHead = formatColumns(WO_MajorHead);
		    //alert(WO_MajorHead);
		  	 
		  	var WO_MinorHead =  "<%= WO_MinorHead %>";
		  	WO_MinorHead = formatColumns(WO_MinorHead);
		 //	alert(WO_MinorHead);
		  
		 	var Wo_WorkDesc =  "<%= WO_Desc %>";
		 	Wo_WorkDesc = formatColumns(Wo_WorkDesc);
			//alert(childProductColumn);
			
			var rowNum = ele.match(/\d+/g);
			//alert(rowNum);
			             
            if(str1 == WO_MajorHead) {
            	WOMajorHeadId = ui.item.option.value;
            	WOMajorHeadName = ui.item.value;
              //  debugger;
                loadSubProds(WOMajorHeadId, rowNum);
            }            
            else if(str1 == WO_MinorHead) {
            	WOMinorHeadId = ui.item.option.value;
            	WOMinorHeadName = ui.item.value;
             //   debugger;
                loadSubSubProducts(WOMinorHeadId, rowNum);
            }
            else if(str1 == Wo_WorkDesc) {
            	WOWorkDescId = ui.item.option.value;
            	Wo_WorkDesc = ui.item.value;
            //    debugger;
                loadUnits(WOWorkDescId, rowNum);
            }
            
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
	  $("#workOrderDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:new Date(),
		//maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
	  });
  });
  
</script>

<style>
/* .table-quantity-showwo tbody tr td:first-child, .table-quantity-showwo thead tr th:first-child {min-width: 20px;text-align: center; width:50px !important;} */
.table-quantity-showwo thead, .table-quantity-showwo tbody tr{table-layout:fixed;display:table;width:100%;}
.table-quantity-showwo>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;background-color:#ccc;}
.table-quantity-showwo thead{width: calc(100% - 17px) !important;}
.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
.txt-border{border:none !important;border-bottom:1px solid #000 !important;border-radius:0px;}

 /* table border color changes */
  .table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>thead>tr>th{
  border:1px solid #000;
  }
  .table-bordered{
  border:1px solid #000;
  }
  .table-bordered thead{
background-color:#ccc;
  }
 /*table border color changes */
 .form-control{
 border:1px solid #000;
 }
 //AC
 input[type=checkbox] {
  transform: scale(1.5);
}
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
  
     .WoGrandTotal{width:300px;float:right;font-size: 20px;margin-top:20px;}
     /*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:54px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/
</style>
<script type="text/javascript">
if (typeof (Storage) !== "undefined") {
	debugger;

	var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
	if (i == 2) {
		sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
		//window.location.reload();
		window.location.assign("updateTempWO.spring");
	}
} else {
	alert("Sorry, your browser does not support Web Storage...");
}
</script>
</head>

<body class="nav-md">
       <div class="overlay_ims"></div>
	   <div class="loader-ims" id="loaderId" > <!--  -->
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
	                       <%-- 	<%@include file="./../SideMenu.jsp" %> --%> 
							
						</div>
						</div>
							<jsp:include page="./../TopMenu.jsp" />  
					       <%-- <%@include file="./../TopMenu.jsp" %> --%>
		
					<!-- page content -->
					<div class="right_col" role="main">
						<div class="col-md-12">
							<ol class="breadcrumb">
								<li class="breadcrumb-item"><a href="#">Home</a></li>
								<li class="breadcrumb-item active">Update Temp WorkOrder</li>
							</ol>
						</div>
						 <!-- loader -->
					     <div class="overlay_ims" ></div>
						 <div class="loader-ims" id="loaderId"> <!--  -->
							<div class="lds-ims">
								<div></div><div></div><div></div><div></div><div></div><div></div></div>
							<div id="loadingimsMessage">Loading...</div>
						</div>
					<div>
	 
	               <div class="col-md-12">
	 	             <form:form modelAttribute="WorkOrderBean" id="updateTempWorkOrderFormId" method="GET" class="form-horizontal" action="updateTempWorkOrder.spring" enctype="multipart/form-data">
	                   <div>
				 		 <div class="border-background-workorder">
				 	       <%-- 		<form:form modelAttribute="WorkOrderBean" id="printWorkOrderFormId" method="GET" class="form-horizontal" action="printWorkOrderDetail.spring"> --%>
	 	
							<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
							<c:set  scope="session"   value="${WorkOrderBean}" var="WorkOrderBean"></c:set>
							<c:set  scope="session"  var="deletedWorkOrderDetailsList"  value="${deletedWorkOrderDetailsList}"></c:set>
							<c:set scope="session" value="${listTermsAndCondition }" var="listTermsAndCondition"></c:set>
							<c:set scope="session"  value="${WorkOrderLevelPurpose}" var="WorkOrderLevelPurpose" ></c:set>
							<c:set  scope="session"  value="${workOrderCreationList}" var="workOrderCreationList"></c:set>
				 
			
							<span style="color:red;">${noStock}</span><br/>
		                    <div class="col-md-6">
							    <div class="form-group">
									<label class="control-label col-md-6"><%= TempWorkOrderNo%></label>
									<div class="col-md-6">
										<form:input path="siteWiseWONO" id="workOrderId"  name="workOrderId" class="form-control" readonly="true" title="${WorkOrderBean.siteWiseWONO}"/>
										
										<%-- <input type="text" name="nextApprovelpendingEmpId" value="${WorkOrderBean.pendingEmpId}"> --%>
										
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
				      	<div class="col-md-6">
							<div class="form-group">
								<label class="control-label col-md-6"><%= WorkOrderName%></label>
								<div class="col-md-6" >
									<form:input path="workOrderName" id="workOrderName"  name="workOrderName" class="form-control" readonly="true" title="${WorkOrderBean.workOrderName}" />
								</div>
						    </div>
						</div>
						<div class="col-md-6">
							  <div class="form-group">
								<label class="control-label col-md-6"><%= WorkOrderDate%> <%= colon %> </label>
								<div class="col-md-6" >
									<form:input path="workOrderDate" id="workOrderDate" class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
									<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId"/>
									<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO"/>
									<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId"/>
									<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo"/>
									<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom"/>
									<form:input path="siteId" type="hidden" name="siteId" id="siteId"/>
									<form:input path="typeOfWork" id="typeOfWork"  type="hidden"/>
									<form:input path="revision" id="revision"  type="hidden"/>
									<form:input path="oldWorkOrderNo" id="oldWorkOrderNo"  type="hidden"/>
									<form:input path="versionNumber" id="versionNumber"  type="hidden"/>
								</div>
			                </div>
			          </div>
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
								 <div class="col-md-6">
							       <form:input path="contractorPhoneNo"  name="contractorPhoneNo"  class="form-control" id="contractorPhoneNo" readonly="true" title="${WorkOrderBean.contractorPhoneNo}"/>
								</div>
						   </div>
					          <%-- 	</form:form> --%>
			 	       </div>
			 	       </c:if>
			 	    </div>
				 	  <c:set value="NMR" var="TypeOfWork"></c:set>
				      <div class="col-md-12 no-padding-left no-padding-right">
				      <div class="table-responsive"> <!-- /*tblprodindissudiv*/ -->
			         <table id="indentIssueTableId" class="table table-bordered tblprodindissu" style="width:2000px">
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
							<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>" data-placement="left">
								<input type="hidden" name="actualwoMajorHead${indexnumber}" value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">
								<input type="text" id="comboboxProd${indexnumber}" readonly class="form-control"  value="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">
						
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
							<input type="text" class="form-control" name="woManualDesc" id="woManualDesc"  value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"   onclick="showScopeOfWork(${indexnumber})" readonly="readonly"/>
							
												
						<!-- modal popup for scope of work start-->
									<div id="modalForScopeWork${indexnumber}" class="modal fade" role="dialog">
									  <div class="modal-dialog" style="width:90%;">
									
									    <!-- Modal content-->
									    <div class="modal-content">
									      <div class="modal-header modalscopeheader">
									        <button type="button" class="close" data-dismiss="modal">&times;</button>
									        <h4 class="modal-title text-center">Scope Of Work</h4>
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
									        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
									        </div>
									      </div>
									    </div>
									
									  </div>
									</div>
									<!-- modal popup for scope of work end -->
						</td>
						</c:if>
						<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">
							<input type="hidden" value="${workOrderDetail.unitsOfMeasurement}" id="UOM${indexnumber }">
							<input type="text" name="actualunitsOfMeasurement${indexnumber}" id="actualunitsOfMeasurement${indexnumber}" class="form-control"  value="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>" readonly="readonly" title="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>"/>
						</td>
						<td class="w-70" data-toggle="tooltip">
							<input type="hidden" name="actualQuantity${indexnumber}" value="${workOrderDetail.quantity}">
							<input  id="Quantity${indexnumber}" type="text" onfocusout="calCulateTotalAmout('qty',this.value,${indexnumber})"  name="Quantity${indexnumber}" readonly="true" value="${workOrderDetail.quantity}" title="${workOrderDetail.quantity}"  class="form-control input-visibilty${indexnumber}"/> 
							<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">	
									<a href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber})">Click Here</a>
							</c:if>
							<div id="appendWorkOrderArea1${indexnumber}"></div>
							
							<div id="appendWorkOrderArea2${indexnumber}"></div>
							
							<div id="appendWorkOrderArea${indexnumber}">
									
							</div>
							</td>
							<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
								<td>
									<input type="hidden" id="actualAcceptedRate${indexnumber}" name="actualAcceptedRate${indexnumber}" value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}">
							     	<input id="AcceptedRate1${indexnumber}" type="text" onfocusout="calCulateTotalAmout('a_rate',this.value,${indexnumber})" name="AcceptedRate${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber}" value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}"/>
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
				<div class="WoGrandTotal">
								<div style="float:left;width:120px;">Grand Total</div>
								<div style="float:left;width:20px;">:</div>
								<div style="float:left;width:160px;" id="WoGrandTotalAmount"></div>
							</div>		
			<div class="col-md-12 no-padding-left no-padding-right" style="margin-top:20px;">
				<div class="col-md-6 no-padding-left no-padding-right">
				 <label class="control-label col-md-4 no-padding-left" >Note: </label>
				<div class="col-md-8 no-padding-left no-padding-right" style="margin-bottom: 10px;">
				<%-- 	<form:textarea path="Purpose" href="#" data-toggle="tooltip" title="${WorkOrderLevelPurpose}"   id="NoteId" name="Note" class="form-control" autocomplete="off" placeholder="${IndentLevelCommentsList}"></form:textarea> --%>
					<textarea  href="#" data-toggle="tooltip" title="<c:out value='${WorkOrderLevelPurpose}'></c:out>"   id="NoteId" name="Note" class="form-control textarea-control-area" autocomplete="off" placeholder="<c:out value='${WorkOrderLevelPurpose}'></c:out>"></textarea>
				</div>
				</div>
			</div>
			
		<c:if test="${listTermsAndCondition.size()!=0}">	
			<div class="pull-left">
				<label>Terms And Conditions :</label>
			<ul>
			<c:forEach items="${listTermsAndCondition}" var="TAC">
			  <c:if test="${not empty TAC.TERMS_CONDITION_DESC.trim()}">
				<li>${TAC.TERMS_CONDITION_DESC}</li>
			</c:if>
			</c:forEach>
			</ul>
			</div>
		</c:if>
			<div class="clearfix"></div>
			
			<div class="pull-left">
				<c:if test="${operType!=1}">
				<c:if test="${deletedWorkOrderDetailsList.size()!=0}">
					<label margin-top: 25px;"> Modification Details:</label>
					<ul>
					 <c:forEach items="${deletedWorkOrderDetailsList}"  var="workOrderDeletedDetail">
				         <li>${workOrderDeletedDetail.remarks }</li>
					 </c:forEach>
					 </ul>
					 </c:if>
		 	</c:if>
	
		<!-- ***********************************************this is for pdf file download start******************************************************** -->
	
	<div id="allImgPdfUrlToDeleteFile">
	
	</div>
	
	
	<div class="col-md-12" style="margin-top: 10px;">
	<!-- 	<h3>You can see the PDF</h3> -->
	<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
	if(pdfcount==0){
		%>
		<!-- <h3>No PDF</h3> -->
		<%
	}else{
		%>
	<h3 style="color: #ffa500;">You can see the PDF(s) below :</h3>	
		<%
	}	
		for(int i=0;i<pdfcount;i++){
	  	String pdfName="pdf"+i;
	  	log(pdfName);
	
	%>
	   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
	 <%
			if(request.getAttribute(pdfName)!=null){
	%>
				 <div class="col-md-3 pdfcount pdf-delete<%=i%>"  id="pdfDivHideShow<%=pdfName %>">
				  <div class="pdf-cls" style="margin-bottom:15px;"> 
				  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
				  <iframe class="iframe-pdf" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
				  <div class="middle">
					<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf<%=i%>"><i class="fa fa-close"></i></button>
				</div>
				</div>
				</div>
	<%} %>
	<%} %>
	</div>
	 
	 		<!-- ***********************************************this is for pdf download end************************************************** -->		
					<div class="col-md-12 Mrgtop20">
		
					<!-- <h3>You can see the Images</h3> -->
				<%
					int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
					//int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
	 				if(imagecount==0){
	 			%>
	 				<!-- 	<h3>No Images</h3> -->
				<%
					}else{
	 			%>
	 					<h3 style="color: #ffa500;">You can see the Images below :</h3>
 				<%
	 				}		
	 				
					 for (int i = 0; i < imagecount; i++) {
						 String imageB64="image"+i;	
						 String deleteB64="delete"+i;
						 log(imageB64);
						 out.print("<div class='col-md-4 Mrgtop20'>");
			   %>
					       <c:set value="<%=imageB64 %>" var="index"></c:set>
					        <c:set value="<%=deleteB64 %>" var="delete"></c:set>
			   <%
			
					if(request.getAttribute(imageB64)!=null){
			    %> 
			    	<div class="container-1"  id='imageDivHideShow<%=imageB64 %>'>
						<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
					<div class="middle-1">
						<div class="columns download">
						     <p>
						     <a class="button btn-dwn btn-success btn-small" download onclick="toDataURL('${requestScope[index]}',this)"><i class="fa fa-download"></i>&nbsp;Download</a>
						         <button onclick="deleteWOImageFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="button btn-dwn btn-danger btn-small1"><i class="fa fa-remove"></i> &nbsp;Delete</button>
						      </p>
				     	</div>
					  </div>
					 </div>
				<%
					}
				%>
				<%
						out.print("</div>");
				%>
				<%}%>
								
				 <input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
				 <input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfcount%>">
								
				<div class="col-md-12">
					<div class="file-upload color-head-file" id="ishidden">
						<c:if test="${(imagecount+pdfcount)<8}"><h3>You can upload Images/PDFs here :</h3>
						</c:if>
				<%
						for (int i = 0; i < imagecount; i++) {
							out.println("<div id=\"fileupload" + i
										+ "\" style=\"display:none;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'   name=\"file\"></div>");
						}
				 %>
				 <%
						 for (int i = (imagecount+pdfcount); i < 8; i++) {
							 out.println("<div id=\"fileupload" + i
								 + "\" style=\"display:block;margin-bottom:15px;\"><input type=\"file\"  accept='application/pdf,image/*'  name=\"file\"></div>");
					     }
				%>
			 </div>
		 </div>
			
			</div>
			<%-- </c:if> --%>
			</div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			
			<div class="col-md-12 text-center center-block" style="margin-top:35px;">
			<input type="button" class="btn btn-warning" value="Update" id="updaetTempBtnId" onclick="updateTempWO()">
			<input type="button" class="btn btn-warning" value="Close" id="closeBtn"  onclick="return closeView()">
			
			<div class="col-md-12 text-center center-block" style="margin-top:35px;">
			
		</div>
		</div>
			
		<div class="clearfix"></div>
		<!-- second table start -->
		<div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-lg" style="width:90%;">
	
	    <!-- Modal content-->
	    <div class="modal-content" >
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title text-center">Work Order Details</h4>
	      </div>
	      <div class="modal-body modal-content-scroll">
	       	<div class="table-responsive">
		 <table class="table table-quantity-showwo" > <!-- id="modaltbl"  removed-->
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
	        <div class="col-md-12 text-center center-block"><button type="button" class="btn btn-danger" data-dismiss="modal">Close</button></div>
	      </div>
	    </div>
	  </div>
	</div>
		
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
			 <embed src="${requestScope[pdfBase64]}" style="height:500px;width:100%;">
	      </div>
	      <div class="modal-footer">
	       <p class="text-center">
		     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
		     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName%>','${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button>
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
		<%	imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
				for (int i = 0; i < imagecount; i++) { 
			String index="image"+i;				
			log(index);
		%>
	   <div class="modal fade custmodal" id="uploadinvoice-img<%=i %>" role="dialog">
	    <div class="modal-dialog modal-lg custom-modal-lg">
	      <!-- Modal content-->
	      <div class="modal-content">
	        <div class="modal-header cust-modal-header">
	          <button type="button" class="close" data-dismiss="modal">&times;</button>
	        </div>
	        <div class="modal-body cust-modal-body">
	  		  <c:set value="<%=index %>" var="i"></c:set>
	    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="${requestScope[i]}" />
	        </div>
	        <div class="modal-footer cust-modal-footer">
	          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	        </div>
	      </div>
	    </div>
	  </div>
	  <%} %>
		<!-- modal popup for invoice image end -->    
	
		<!-- second table end -->
		</div>
		<!-- /page content -->        
		</form:form>
		
	</div>
	</div>
	</div>
	</div>
	</div>
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
  <script>
  function closeView(){
		debugger;
		goBack() ;
	}
  
	function goBack() {
	    window.history.back();
	}
	
  function validateFileExtention(){
	  debugger;
	var ext="";
	var kilobyte=1024;
	var countUploadedFiles=0;
	var len=$('input[type=file]').val().length;
	
	var count=0;
/* 	if(len==0){
		alert("please at least one file.");
		return false;
	} */
	  $('input[type=file]').each(function () {
	        var fileName = $(this).val().toLowerCase(),
	         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
	        	
			 if(fileName.length!=0){
	        	if((this.files[0].size/kilobyte)<=kilobyte){
	        		countUploadedFiles++;
				}else{
					alert("Please upload below 1 MB PDF file");
					//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
					$(this).val('');
					$(this).focus();
					count++;
				 return false;
				}
	        	
		   /*      if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			        ext= fileName.substring(fileName.lastIndexOf(".")+1); */
		   
		        if (!(regex.test(fileName))) {
		            $(this).val('');
		            alert('Please select correct file format');
		            count++;
		            return false;
		        }
	        }
	    });
	//alert(countUploadedFiles);
	/* if(countUploadedFiles==0){
		 alert('Please select at least one file.');
		 return false;
	} */
	if(count==0){
	  return true;}
	else{
		return false;}
}//validateFileExtention method
  
	 var deleteUploadedimageFiles=new Array();
	 var deleteUploadedPDFFiles=new Array();
	 
	 
	  function deletepdf(divId,imagePath){
		  debugger;
			 var canISubmit = window.confirm("Do you want to delete PDF?");
			 if(canISubmit == false) {
			        return false;
			 }
			 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val()==""?0:parseInt($("#pdfAlreadyPresent").val());
			 $("#pdfAlreadyPresent").val(pdfAlreadyPresent-1);
			 var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
			 
			 deleteUploadedPDFFiles.push({"pdfNo":divId,"url":url});
			 
			 $("#pdfDivHideShow"+divId).remove();
			 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");

		//	 deleteFile(divId,imagePath);
		}
  
		function deleteFile(divId,url){
			debugger;
			// var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
			 /* var imagesAlreadyPresent=$("#imagesAlreadyPresent").val();
			 var pdfAlreadyPresent=$("#pdfAlreadyPresent").val();
			 imagesAlreadyPresent=imagesAlreadyPresent-1;
			 $("#imagesAlreadyPresent").val(imagesAlreadyPresent); */
			 $.ajax({
				  url : url,
				  type : "get"
				  });
		}
	  
	  function deleteWOImageFile(divId,imagePath){
			 debugger;
			 
		 var canISubmit = window.confirm("Do you want to delete image?");
		     
		 if(canISubmit == false) {
		        return false;
		 }
		 $("#imageDivHideShow"+divId).remove();
		 $("#ishidden").append("<div id='fileupload' style='display:block;margin-bottom:15px;''><input type='file'   accept='image|pdf/*'   name='file'></div>");		 
		 // deleteFile(divId,imagePath);
		 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val()==""?0:parseInt($("#imagesAlreadyPresent").val());
		 $("#imagesAlreadyPresent").val(imagesAlreadyPresent-1);
		 var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
		 	
		 //Stroing files url for deleting files after update button press
		 deleteUploadedimageFiles.push({"imageNo":divId,"url":url});
		// deleteFile(divId,imagePath);
		 return false;
	}
  
		function excuteDeleteFunction(){
			debugger;
			var count=1;
			 deleteUploadedimageFiles.sort(sortUrlData);
				 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedimageFiles.length; imgPdfIndex++) {
						if(deleteUploadedimageFiles[imgPdfIndex].length!=0){
							$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='imgPathtoDelete' id='imgPdfPath"+count+"'>");
							//deleteFile(imgPdfIndex,deleteUploadedimageFiles[imgPdfIndex].url);
							imgePath=deleteUploadedimageFiles[imgPdfIndex].url.split("deleteWOPermanentImage.spring?")[1];
							$("#imgPdfPath"+count).val(imgePath);
							count++;
						}
					} 
				 deleteUploadedPDFFiles.sort(sortPdfData);
				 for (var imgPdfIndex = 0; imgPdfIndex < deleteUploadedPDFFiles.length; imgPdfIndex++) {
						if(deleteUploadedPDFFiles[imgPdfIndex].length!=0){
							$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='PdfPathtodelete' id='imgPdfPath"+count+"'>");
							//deleteFile(imgPdfIndex,deleteUploadedPDFFiles[imgPdfIndex].url);
							pdfPath=deleteUploadedPDFFiles[imgPdfIndex].url.split("deleteWOPermanentImage.spring?")[1];
							$("#imgPdfPath"+count).val(pdfPath);
							count++;
						}
					}
				 document.getElementById("updaetTempBtnId").disabled = true;   
				 $("#saveBtnId").attr("disabled", true);
				 $("#closeBtn").attr("disabled", true);
				 document.getElementById("updateTempWorkOrderFormId").method = "POST";
			     document.getElementById("updateTempWorkOrderFormId").submit();
			}
		function sortPdfData(a,b){
			debugger;
				
				var	pdf=a.pdfNo.split("pdf")[1];
				var	pdf1=b.pdfNo.split("pdf")[1];
				
				
				 if(pdf < pdf1) {
					    return 1;
				 }else if (pdf1 < pdf) {
					    return -1;
				 }else{ 
					return 0;	  
				  }
			}

			function sortUrlData(a, b) {
				debugger;
				
				var	imageNo=a.imageNo.split("image")[1];
				var	imageNo1=b.imageNo.split("image")[1];
				
				
				 if(imageNo < imageNo1) {
					    return 1;
				 }else if (imageNo1 < imageNo) {
					    return -1;
				 }else{ 
					return 0;	  
				  }
			} 
		  function updateTempWO(){
			  debugger;
			   var result=validateFileExtention();
			  if(result==false){
				  return false
			  }  
			    result = confirm("Do you want to update the temp WO");
				if (!result) {
				  return false;
				}
				 debugger;
				excuteDeleteFunction();
			}
		
			$(document).ready(function() {	
				$(".overlay_ims").show();	
				$(".loader-ims").show();
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
			
			
/* $(document).ready(function() {	

				
				//alert("oooo");
				 $('#contractorName').on('change',  function() {
				
			//	 alert("ok");
					 
					 var contractorName=$("#contractorName").val();
				// alert(contractorName);	
				 $.ajax({
					 url: "./getEmployerid.spring?employeeName="+contractorName,
					 type: 'GET',
					 data:"",
					 success : function(data) {
						 $("#RequesterIdId").val(data);
					 },
					 error:  function(data, status, er){
						  alert(data+"_"+status+"_"+er);
						  }
					  });
				  	});
				
				 $('#RequesterIdId').on('change', function() {
				 var empid=$("#RequesterIdId").val();
				 // alert(empid);	
				 $.ajax({
					 url: "./getEmployerName.spring?employeeid="+empid,
					 type: 'GET',
					 data:"",
					 success : function(data) {
						 $("#contractorName").val(data);
					 },
					 error:  function(data, status, er){
						  alert(data+"_"+status+"_"+er);
						  }
					  });
				  	});

				
			}); */
		</script> 
		<script>
		var rowsIncre=1;
		function showScopeOfWork(row){
			$('#modalForScopeWork'+row).modal('show'); 
		}
		function myFunction(workOrderID,acceptedRate,row) {
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
							alert("Please select work descripition.");
							 return false;
						 }
						 mesurmentId= $("#WODESC"+row).val();
							var  UnitsOfMeasurementId=$("#UOM"+row).val();
						var url = "loadWOAreaMapping.spring";
						$.ajax({
							url : url,
							type : "get",
							data:{
								acceptedRate:acceptedRate,
								workOrderID:workOrderID,
								mesurmentId:mesurmentId,
								UnitsOfMeasurementId:UnitsOfMeasurementId,
								operType:"${operType}"								
								},
				//			contentType : "application/json",
							success : function(data) {
							
								var str="";
								   var data1="";
								   var totalAreaQty=0;
								   var rowNum=row;	   
								$.each(data,function(key,value){
							debugger;
								if(value.PRICE!=undefined&&value.PRICE!=""){
									//var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
									var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
									var flatname=value.FLATNAME==null?"-":value.FLATNAME;
									var flatName=value.FLATNAME==null?"-":value.FLATNAME;
								str+="<tr> "
									+"<td title='"+value.WO_WORK_AREA_ID+"'>   "+value.BLOCK_NAME+"</td> "
									+"<td> "+floorName+"</td>"
									+"<td>"+flatName+"</td>"
									+"<td id='"+value.WO_WORK_AREA_ID+"WO_MEASURMEN_NAME'> <strong>"+value.WO_MEASURMEN_NAME.toUpperCase()+"</td>"
									+"<td id='"+value.WO_WORK_AREA_ID+"RECORD_TYPE'> <strong>"+value.RECORD_TYPE.toUpperCase()+"</td>"
									+"<td id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA' >"+inrFormat(value.AREA_ALOCATED_QTY) +" </td>"
									+"<td id='"+value.WO_WORK_AREA_ID+"PRICE' > "+inrFormat(value.PRICE) +" </td>"
									+"</tr>";
											totalAreaQty+=+value.AREA_ALOCATED_QTY;
								}
								rowNum++;
								});
								//alert(totalAreaQty);
								//$("#Quantity"+row).val(totalAreaQty);
								$("#appendWorkOrderArea1"+row).html(data1);
								$("#areaMapping").html(str);
								
							},
							error : function(data, status, er) {
								alert(data + "_" + status + "_" + er);
							}
						});

						$("#table-quantity").show();
		}
		
		$(document).ready(function() {
			if(window.history.length==1){
				$("#closeBtn").hide();
			}
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
		 //to distroy loader
		 $(window).load(function () {
			 calGrandTotal();
			 $('input[type="text"]').each(function(){
				    if (isNum(this.value)){ // regular expression for numbers only.
				    	var tempVal=parseFloat(this.value);
				    	if(this.id!="site_id"&&this.id!="workOrderId"&&this.id!="contractorPhoneNo")
			        		//$(this).val(tempVal.toFixed(2));
				    	$(this).val(inrFormat(tempVal.toFixed(2)));
			           }
				});
			 
			 $(".overlay_ims").hide();	
			 $(".loader-ims").hide();
		});
		 function isNum(value) {
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value)
		}
		 function calGrandTotal(){
			 var WoGrandTotal=0;
				$(".totalRowamount").each(function(){						
					WoGrandTotal+=parseFloat($(this).val()==""?0:parseFloat($(this).val()));
				});
				$("#WoGrandTotalAmount").text(inrFormat(WoGrandTotal.toFixed(2)));
		 }
		
		//this code for download server images
			function toDataURL(url, current) {
				debugger;
			    var httpRequest = new XMLHttpRequest();
			    httpRequest.onload = function() {
			       var fileReader = new FileReader();
			          fileReader.onloadend = function() {
			             console.log("File : "+fileReader.result);
							 $(current).removeAttr("onclick");
							 $(current).attr("href", fileReader.result);
							 $(current)[0].click();
							 $(current).removeAttr("href");
							 $(current).attr("onclick", "toDataURL('"+url+"', this)");
			          }
			          fileReader.readAsDataURL(httpRequest.response);
			    };
			    httpRequest.open('GET', url);
			    httpRequest.responseType = 'blob';
			    httpRequest.send();
			 }
			//this code for download server images	
			
			//to ditroy loaders
		$(window).load(function() {
			 $(".overlay_ims").hide();	
			 $(".loader-ims").hide();
		});
			
		//this code for to active the side menu 
		var referrer="updateTempWO.spring";
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
