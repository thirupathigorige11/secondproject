<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle) request.getAttribute("columnHeadersMap");
	String tableHead = resource.getString("label.indentIssueTableHead");
	String colon = resource.getString("label.colon");

	String reqId = resource.getString("label.WorkOrderNo");
	String WorkOrderName = resource.getString("label.WorkOrderName");
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
	String WorkOrderDate = resource.getString("label.WorkOrderDate");
	String panCardNo = resource.getString("label.panCardNo");
	String Address = resource.getString("label.Address");
	String phoneNo = resource.getString("label.phoneNo");
	String vendorName = resource.getString("label.contractorname");
	String WO_MajorHead = resource.getString("label.WO-MajorHead");
	String WO_MinorHead = resource.getString("label.WO-MinorHead");
	String WO_Desc = resource.getString("label.WO-Desc");
	String WO_Manual_Desc = resource.getString("label.WO-Manual-Desc");//not used
	String scope_Of_work= resource.getString("label.scope-Of-work");
	String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency");
	String measurement = resource.getString("label.UOM");
	String AcceptedRate = resource.getString("label.AcceptedRate");
	String TotalAmount = resource.getString("label.TotalAmount");


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
<link href="css/ShowWorkOrder.css" rel="stylesheet" type="text/css">


<jsp:include page="./../CacheClear.jsp" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<!-- <title>Update WorkOrder </title> -->
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
	        var WO_MajorHead =  "<%=WO_MajorHead%>";
	        WO_MajorHead = formatColumns(WO_MajorHead);
		    //alert(WO_MajorHead);
		  	 
		  	var WO_MinorHead =  "<%=WO_MinorHead%>";
		  	WO_MinorHead = formatColumns(WO_MinorHead);
		 //	alert(WO_MinorHead);
		  
		 	var Wo_WorkDesc =  "<%=WO_Desc%>";
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

<script type="text/javascript">

</script>
<style>
.fixedheader tbody tr td:first-child, .fixedheader thead tr th:first-child {min-width: 20px;text-align: center; width:50px !important;}
.fixedheader thead, .fixedheader tbody tr{table-layout:fixed;display:table;width:100%;}
.fixedheader>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
.fixedheader thead{width: calc(100% - 17px) !important;}
.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
.fixedheader thead tr th{background-color:#ccc !important;}
.fixedheader tbody tr td{text-align:center !important;}
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
 /* table border color changes */
 .txt-border{border-bottom:1px solid #000 !important;border-top:0px !important;border-left:0px !important;border-right:0px !important;border-radius:0px;}
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
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:62px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/ 
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("updateWorkOrder.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>

</head>

<body class="nav-md">
 	<!-- loader  -->
	<div class="overlay_ims"></div>
		 <div class="loader-ims" id="loaderId"> 
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	</div>
	<noscript>
		<h3 align="center" style="font-weight: bold;">JavaScript is
			turned off in your web browser. Turn it on and then refresh the page.</h3>
		<style>
#mainDivId {
	display: none;
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
						<li class="breadcrumb-item active">View My Work Order</li>
					</ol>
				</div>

				<div class="col-md-12">
					<div align="center">
						<!-- @RequestParam("file") MultipartFile[] files -->
						<form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal" enctype="multipart/form-data">
							<div class="col-md-12 border-background-workorder">
								<%-- <c:set  scope="session"   value="${WorkOrderBean}" var="WorkOrderBean"></c:set> --%>
								<c:set scope="session" var="deletedWorkOrderDetailsList" value="${deletedWorkOrderDetailsList}"></c:set>
								<c:set scope="session" value="${listTermsAndCondition}"	var="listTermsAndCondition"></c:set>
								<c:set scope="session" value="${WorkOrderLevelPurpose}"	var="WorkOrderLevelPurpose"></c:set>
								<c:set scope="session" value="${workOrderCreationList}"	var="workOrderCreationList"></c:set>

								<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
								<span style="color: red;">${noStock}</span><br />
                                <div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6"><%=WorkOrderNo%></label>
										<div class="col-md-6">
											<form:input path="siteWiseWONO" id="workOrderId" name="workOrderId" class="form-control" readonly="true" title="${WorkOrderBean.siteWiseWONO}" />
											<form:input path="workOrderNo" type="hidden" id="workOrderNo" class="form-control" autocomplete="off"  readonly="true"/>
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6"><%=WorkOrderDate%>
											<%=colon%> </label>
										<div class="col-md-6">
											<form:input path="workOrderDate" id="workOrderDate"	class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
										</div>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6"><%=WorkOrderName%></label>
										<div class="col-md-6">
											<form:input path="workOrderName" id="workOrderName"
												name="workOrderName" class="form-control" readonly="true"
												title="${WorkOrderBean.workOrderName}" />
												
												
												<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO"/>
												<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId" />
												<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId" />
												<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo" />
												<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom" />
												<form:input path="siteId" id="siteId" type="hidden" />
												<form:input path="siteName" type="hidden"/>
												<input type="hidden" name="operType" id="operType" value="${operType}">
												<form:input path="isUpdateWOPage" type="hidden"	name="isUpdateWOPage" value="${isUpdateWOPage}" />
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
										    <label class="control-label col-md-6"><%=vendorName%> <%=colon%> </label>
											<div class="col-md-6">
												<form:input path="contractorName" id="contractorName" onkeyup="populateContractor(this);" readonly="true"
													autocomplete="off" class="form-control" title="${WorkOrderBean.contractorName}" />
												
											</div>
									  </div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6"><%=panCardNo%> <%=colon%> </label>
										<div class="col-md-6">
											<form:input path="contractorPanNo" name="contractorPanCardNo"
												id="contractorPanCardNo" class="form-control" readonly="true"
												title="${WorkOrderBean.contractorPanNo}" />
										</div>
									</div>
								</div>
		                        <div class="col-md-6">
									<div class="form-group">
											<label class="control-label col-md-6"><%=Address%> <%=colon%> </label>
											<div class="col-md-6">
												<form:input path="contractorAddress" name="contractorAddress"
													class="form-control" id="contractorAddress" readonly="true"
													title="${WorkOrderBean.contractorAddress}" />
											</div>
									</div>
								</div>
	                          	<div class="col-md-6">
									<div class="form-group">
										<label class="control-label col-md-6"><%=phoneNo%> <%=colon%> </label>
										<div class="col-md-6">
											<form:input path="contractorPhoneNo" name="contractorPhoneNo"
												class="form-control" id="contractorPhoneNo" readonly="true"
												title="${WorkOrderBean.contractorPhoneNo}" />
	
										</div>
									</div>	
								</div>
							</c:if>
							</div>
							<c:set value="NMR" var="TypeOfWork"></c:set>
							<div class="clearfix"></div>
							<div class="">
								<div class="table-responsive">
									<!-- /*tblprodindissudiv*/ -->
									<table id="indentIssueTableId" class="table table-bordered tblprodindissu" style="width:2000px;"> 
										<thead class="cal-thead-inwards">
											<tr>
												<th><%=serialNumber%></th>
												<th><%=WO_MajorHead%></th>
												<th><%=WO_MinorHead%></th>
												<th><%=WO_Desc%></th>
												<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
								  				<th><%= scope_Of_work %></th>
								  				</c:if>
								  				<th><%= measurement %></th>
								  				<th><%= quantity %></th>
								  				<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
								  				<th><%= AcceptedRate %></th>
								  				</c:if>
												<th><%=TotalAmount%></th>
												<th><%=note%></th>
												<%-- <th><%=actions%></th> --%>

											</tr>
										</thead>
										<tbody class="tbl-fixedheader-tbody">
											<c:set value="0" var="indexnumber"></c:set>
											<c:forEach items="${workOrderCreationList}"
												var="workOrderDetail">
												<c:set value="${indexnumber+1}" var="indexnumber"></c:set>
												<input name="isDelete${indexnumber}"
													id="isDelete${indexnumber}" readonly="true" value="z"
													class="form-control input-visibilty${indexnumber}"
													type="hidden" />

												<tr id="tr-class${indexnumber}">
													<td>
														<div id="snoDivId1">
															<c:out value="${indexnumber}"></c:out>
														</div> <input name="dispplayedRows" type="hidden"
														value="${indexnumber}"> <input type="hidden"
														name="actualWorkOrderNo"
														value="${WorkOrderBean.siteWiseWONO}"> <input
														type="hidden" name="nextApprovelEmpId"
														value="${WorkOrderBean.approverEmpId}"> <input
														type="hidden" name="actualtempIssueId"
														id="actualtempIssueId"
														value="${workOrderDetail.QS_Temp_Issue_Id}"> <input
														type="hidden" name="QS_Temp_Issue_Dtls_Id"
														id="QS_Temp_Issue_Dtls_Id"
														value="${workOrderDetail.QS_Temp_Issue_Dtls_Id}">
														<input type="hidden" name="actualWorkAreaID"
														value="${workOrderDetail.workAreaId}">
													</td>
													<td data-toggle="tooltip"
														title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>"
														data-placement="left"><input type="hidden"
														name="actualwoMajorHead${indexnumber}"
														value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">
														<select id="combobox${indexnumber}"
														name="Product${indexnumber}"
														class="btn-tooltip btn-visibilty${indexnumber}"
														title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">

															<option
																value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">${workOrderDetail.WO_MajorHead1}</option>
															<c:forEach items="${workMajorHead}" var="item">
																<c:if
																	test="${!(workOrderDetail.WO_MajorHead1==item.value)}">
																	<option value="${item.key}${item.value}">
																		${item.value }</option>
																</c:if>
															</c:forEach>
													    </select>
													</td>
					
													<%-- <td>
											        <form:select path="WO_MinorHead1" id="comboboxsubProd1" class="form-control"/>
										           </td> --%>
													<td data-toggle="tooltip" title="${workOrderDetail.WO_MinorHead1}">
													    <input type="hidden" name="actualWO_MinorHead${indexnumber}" value="${workOrderDetail.woMinorHeads}$${workOrderDetail.WO_MinorHead1}">
														<select name="SubProduct${indexnumber}"  id="comboboxsubProd${indexnumber}" class=" btn-tooltip form-control btn-visibilty${indexnumber}" >
														<option value="${workOrderDetail.woMinorHeads}$${workOrderDetail.WO_MinorHead1}">${workOrderDetail.WO_MinorHead1}</option>
														</select>
													</td>
													<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_Desc1}' />">
														<input type="hidden" name="actualWO_Desc${indexnumber}" id="actualWO_Desc${indexnumber}" value="${workOrderDetail.wODescription}$${workOrderDetail.WO_Desc1}">
														<select name="ChildProduct${indexnumber}" id="comboboxsubSubProd${indexnumber}" class="form-control btn-visibilty${indexnumber}">
														<option value="${workOrderDetail.wODescription}$${workOrderDetail.WO_Desc1}">${workOrderDetail.WO_Desc1}</option>
													    </select>
												   </td>
					                               <c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
												   <td>					
														<input type="text" class="form-control" name="woManualDesc${indexnumber}" id="woManualDesc${indexnumber}"  value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"   onclick="showScopeOfWork(${indexnumber})"  readonly="true"/>
														<!-- modal popup for scope of work start-->
															<div id="modalForScopeWork${indexnumber}" class="modal fade" role="dialog">
																 <div class="modal-dialog" style="width:90%;">
																    <!-- Modal content-->
																    <div class="modal-content">
																          <div class="modal-header modalscopeheader">
																            <button type="button" class="close" data-dismiss="modal">&times;</button>
																            <h4 class="modal-title text-center">Scope Of Work</h4>
																          </div>
																	          <c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}"  />
																	     <c:forEach var="scopeWork" items="${scopeOfWorkParts}">
																		       <div class="modal-body" style="overflow:hidden;">
																		          <div id="textboxDiv">
																		          </div>
																		          <div class="col-md-12 mrg-btm">
																		          <div class="form-group">
																		            <div class="col-md-12">  <input type="text" class="form-control txt-border scopeofworkid${indexnumber}" value="<c:out value='${scopeWork}'></c:out>" title="<c:out value='${scopeWork}'></c:out>" name="ScopeOfWork${indexnumber}"></div>
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
														     <input type="hidden" name="actualunitsOfMeasurement${indexnumber}" id="actualunitsOfMeasurement${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">
															<select name="UnitsOfMeasurement${indexnumber}" id="UnitsOfMeasurementId${indexnumber}" class="form-control btn-visibilty${indexnumber}" onchange="return validateProductAvailability(this);"  >
															<option value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">${workOrderDetail.unitsOfMeasurement1}</option>
															</select>
														</td>
					
														<td class="w-70" data-toggle="tooltip">
																<input type="hidden" name="actualQuantity${indexnumber}" value="${workOrderDetail.quantity}">
															    <input id="Quantity${indexnumber}" type="text" onblur="calCulateTotalAmout('qty',this.value,${indexnumber})"  name="Quantity${indexnumber}" value="${workOrderDetail.quantity}" class="form-control input-visibilty${indexnumber} workordertotalqty"/> 														
																<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">	
																	<a href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber},'show')">Click Here</a><br/>
																</c:if>
																
																<a href="javascript:showDetailsFunction(${indexnumber})" style="display: none;" onclick="showDetailsFunction(${indexnumber})" data-toggle="modal" data-target="#myModal-showwo-showquantity" id="showQty${indexnumber}">View Details 	</a>
																
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
														<input type="hidden" id="actualTotalAmount${indexnumber}" name="actualTotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}">
															<input name="TotalAmount${indexnumber}" type="text" value="${workOrderDetail.totalAmount1}";  id="TotalAmountId${indexnumber}" readonly="true"   class="form-control totalRowamount" />
														</td>
														<td>
														<input type="hidden" name="actualComments${indexnumber}" value="<c:out value='${workOrderDetail.comments1}'></c:out>">
															<input id="Comments${indexnumber}" value="" placeholder="<c:out value='${workOrderDetail.comments1}'></c:out>" title="<c:out value='${workOrderDetail.comments1}'></c:out>" name="Comments${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber}"/>
															<input type="hidden" name="IsComments" value="" id="hiddenCommentsId">
														</td>								
														<%-- <td>
															<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${indexnumber}" class="btnaction" onclick="removeRow('${indexnumber}')" ><i class="fa fa-remove"></i></button>
															<button type="button" name="editItemBtn" value="Edit Item" id="editItem${indexnumber}" class="btnaction" onclick="editRow('${indexnumber}')" ><i class="fa fa-pencil"></i></button>
														</td> --%>
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
							<div class="col-md-12 Mrgtop20">
								<label class="control-label col-md-2" >Note: </label>
								<div class="col-md-3" >
								<input type="hidden" name="actualPurpose" value="${WorkOrderLevelPurpose}">
									<textarea name="Purpose" id="purpose" href="#" data-toggle="tooltip" title="${WorkOrderLevelPurpose}" placeholder="${WorkOrderLevelPurpose}"  id="NoteId" class="form-control resize-vertical" autocomplete="off" ></textarea>
								</div>
							</div>
	                       <!-- ***********************************************this is for pdf file download start******************************************************** -->
							<div class="clearfix"></div>
							<div class="clearfix"></div>
							
							<div id="allImgPdfUrlToDeleteFile">
	
	</div>
							<div class="col-md-12" >
								<!-- <h3>You can see the PDF</h3> -->
							<%    int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
							if(pdfcount==0){
								%>
							<!-- 	<h3 style="float:left;">No PDF</h3> -->
								<%
							}else{
							%>
							<h3 style="float:left;color: #ffa500;">You can see the PDF(s) below :</h3>	
							<div class="clearfix"></div>
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
	
							<div class="clearfix"></div>
							<div class="col-md-12 Mrgtop20">
									
					
														<%
															int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
												 				if(imagecount==0){
												 					%>
												 					<!-- <h3 style="float:left;">No Images</h3> -->
												 					<%
												 				}else{
												 				%>
												 					<h3 style="float:left;color: #ffa500;">You can see the Images below :</h3>
												 					<div class="clearfix"></div>
												 				<%
												 				}						
					 				
					 											for (int i = 0; i < imagecount; i++) {
																	String imageB64="image"+i;		
																	String deleteB64="delete"+i;
																	
																	log(imageB64);
																	if(request.getAttribute(imageB64)!=null)
																	out.print("<div class='col-md-4 Mrgtop20'>");
														%>
														       <c:set value="<%=imageB64 %>" var="index"></c:set>
														       <c:set value="<%=deleteB64 %>" var="delete"></c:set>
														<%
														//	if (i == 0) {
													
														if(request.getAttribute(imageB64)!=null){
															log(imageB64);
														%>
														 
														 <div class="container-1" id='imageDivHideShow<%=imageB64 %>'>
																		<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
																		 <div class="middle-1">
																		<div class="columns download">
															           <p>
															             <a  class="button btn-dwn btn-success  btn-small" download onclick="toDataURL('${requestScope[index]}',this)"><i class="fa fa-download"></i>Download</a>
															             <button onclick="deleteWOImageFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="button btn-dwn btn-danger  btn-small1"><i class="fa fa-remove"></i>delete</button>
															          </p>
															       </div>
															       </div>
															       </div>
														<%
															}
														//	}
														%>
									<%
													if(request.getAttribute(imageB64)!=null)
														out.print("</div>");
													%>
												<% }	%>
												
							</div>
							<div class="clearfix"></div>
							<input type="hidden" name="imagesAlreadyPresent" id="imagesAlreadyPresent"	value="<%=imagecount%>" />
							
							<input type="hidden" name="pdfAlreadyPresent" id="pdfAlreadyPresent" value="<%=pdfcount%>">
			                <div class="col-md-12" style="text-align:left;">
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
		
						      <!-- modal popup for approve page start -->
				
							 <div id="myModal-showeorkorder" class="modal fade" role="dialog">
							       <div class="modal-dialog">
								    <!-- Modal content-->
								    <div class="modal-content">
									     <div class="modal-header header-modalworkOrder text-center" >
									        <button type="button" class="close" data-dismiss="modal">&times;</button>
									        <h4 class="modal-title">Terms & Conditions</h4>
									     </div>
							            <div class="modal-body body-modal-workorder">							     
								        <div class="col-md-12 appen-div-workorder">
									        <c:forEach items="${listTermsAndCondition }" var="TAC">
									        <input type="hidden" name="actualterms_condition_id" value="${TAC.TERMS_CONDITION_ID}">
											<input type="hidden" value="${TAC.TERMS_CONDITION_DESC}" name="actualTC" id="terms${TAC.indexNumber}" ><br>
									        <div class="col-md-12 remove-filed">
												<c:if test="${not empty TAC.TERMS_CONDITION_DESC }">
													<div class="col-md-12">
															<input type="text" class="form-control workorder_modal_text" value="<c:out value='${TAC.TERMS_CONDITION_DESC}' />"   title="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" name="changedTC"  id="terms${TAC.indexNumber}" ><br>
													</div>
												</c:if>
													<!-- <div class="col-md-2 margin-close">
														<button type="button" class="btn-danger remove-button remove_field" id="newTC"><i class="fa fa-remove "></i></button>
													</div> -->
										    </div>
									      </c:forEach>
								       </div>
								       <div class="col-md-12">
									        <div class="input_fields_wrap">
									                <div class="col-md-12 padd-modal-workorder">
												          <div class="form-group">
												          <!-- <div class="col-md-10"><input type="text" class="form-control workorder_modal_text" id="workorder_modal_text1"/></div>
												          <div class="col-md-2 margin-close"><button type="button" class="btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div> -->
												        
												          </div>
									                </div>
									        </div>
									        <div class="col-md-12 margin-close">
										         <input type="checkbox"><span> (Optional)If you want to add CC In emails.</span>
										         <input type="text" class="form-control control-text" id="email-popup-workorder">
									        </div>
									        <div class="col-md-12 margin-close">
										         <input type="checkbox"><span> Subject</span>
										         <input type="text" class="form-control control-text" id="email-popup-workorder" placeholder="Please enter the subject">
									        </div>
								      </div>
							       </div>
							      <div class="modal-footer">
							      <div class="col-md-12 text-center center-block">
							        <button type="button" class="btn btn-warning" data-dismiss="modal"  onclick="updateWORecords('SaveClicked')">Submit</button>
							        </div>
							      </div>
							    
							   </div>
							</div>
					</div>

	               <!-- Modal Popup for approve page -->
	
				  <%--  	Terms And Conditions : <br>
				 	<c:forEach items="${listTermsAndCondition }" var="TAC">
								<input type="hidden" name="actualterms_condition_id" value="${TAC.TERMS_CONDITION_ID}">
								<input type="hidden" value="${TAC.TERMS_CONDITION_DESC}" name="actualTC" id="terms${TAC.indexNumber}" ><br>
									 
								<input type="text" value="${TAC.TERMS_CONDITION_DESC}" name="changedTC" size="${TAC.TERMS_CONDITION_DESC.length()*2}"  id="terms${TAC.indexNumber}" ><br>
					</c:forEach>				 --%>
				
					<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
					<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
		       </form:form>
				<div class="pull-left">
				<%-- 	<label> Modification Details :</label>
					<ul>
					 <c:forEach items="${deletedWorkOrderDetailsList}"  var="workOrderDeletedDetail">
							<li>	${workOrderDeletedDetail.remarks }</li>
					 </c:forEach>
					</ul> --%>
			 </div>
			<div class="col-md-12" style="margin-top:30px;">
				<input type="button" class="btn btn-warning" value="Update"  onclick="updateWORecords('SaveClicked')" id="saveBtnId">
				<!-- <input type="button" class="btn btn-warning" value="Reject" id="saveBtnId" onclick="rejectFromUpdatePage('SaveClicked')"> -->
				<input type="button" name="closeBtn" id="closeBtn" onclick="return closeView()" class="btn btn-warning btn-custom-width" id="closeBtn"	value="Close" />
			</div>
			<div class="clearfix">&nbsp;</div>
			<!-- second table start -->
			<div id="table-quantity" style="display:none;">
			<div class="table-responsive Mrgtop20"  style="height:200px;overflow:auto;">
				 <table class="table table-bordered " >
					  <thead>
						  <tr>
						    <th style="width:14%;" class="text-center"><input type="checkbox" id="checkAll" style="width:14px; height:14px;"></th>
						    <th style="width:14%;" >Block</th>
						    <th style="width:14%;" >Floor</th>
						    <th style="width:14%;" >Flat</th>
						    <th style="width:14%;" >Actual Area</th>
						    <th style="width:14%;" >Available Area</th>
						    <th style="width:14%;" >Allocated Area</th>
						    <th style="width:14%;" >Measurement</th>
						  </tr>
					  </thead>
					  <tbody id="areaMapping12">		
					  </tbody>
				 </table>
			</div>
			<div class="col-md-12 text-center center-block">
			 <button class="btn btn-warning" id="hide-table-22">Submit</button>
			</div>
			 </div>
	     <!-- second table end -->
	   </div>
	  <!-- /page content -->        
    </div>
   </div>
  </div>
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<script src="js/custom.js"></script>
  <script>
	
  function closeView(){
		debugger;
		goBack() ;
	}
	function goBack() {
	    window.history.back();
	}
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
			
				/*  $(".workorderacceptedRate").each(function(){
					  debugger;
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					
					$(".workordertotalqty").each(function(){
						debugger;
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					$(".totalRowamount").each(function(){
						debugger;
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					}); */
				
				
				
			});
			
			
			
			
$(document).ready(function() {	

				
				//alert("oooo");
/* 				 $('#contractorName').on('change',  function() {
				
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
				  	}); */
				
/* 				 $('#RequesterIdId').on('change', function() {
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
				  	}); */

				
			});
			
			
		</script> 
		<script>
		var rowsIncre=1;
		function calCulateTotalAmout(type,acceptedRate,rownum){
			if (typeof(Storage) !== "undefined") {
			     debugger;
		    // Retrieve
		    if(type=="qty"){
		     	var qty=$("#AcceptedRate1"+rownum).val();
				$("#TotalAmountId"+rownum).val(qty*acceptedRate);
		    	
		    }else{
			  	var qty=$("#Quantity"+rownum).val();
				$("#TotalAmountId"+rownum).val(qty*acceptedRate);
		    }
			} else {
			   alert("Sorry, your browser does not support Web Storage...");
			};
			//alert(acceptedRate*amt);
		}
		
		//method for validating work area
		function validateWorkArea(val,row){
			debugger;
		
			workAreaId=$("#actualWorkAreaId"+row).val();
			var allocatedArea = parseInt($("#" + workAreaId + "AVAILABE_AREA").val().trim());
			var availbleArea= parseInt($("#" + workAreaId + "WORK_AREA").text().trim());
			
			var actualAreaAlocatedQTY =	parseInt($("#actualAreaAlocatedQTY"+row).val()==""?0:$("#actualAreaAlocatedQTY"+row).val());
			if(allocatedArea<actualAreaAlocatedQTY){
				return false;
			}
			var totalArea=availbleArea+actualAreaAlocatedQTY;
			
            if(allocatedArea>totalArea){
            	alert("you can't allocate the more than area, of availbale area.");
            	$("#"+workAreaId+"AVAILABE_AREA").val(actualAreaAlocatedQTY);
            	return false;
            }
    	}

		function validateWorkAreaVal(val,row) {
			var allocatedArea = parseInt($("#" + val + "AVAILABE_AREA").val().trim());
			var actualArea= parseInt($("#" + val + "WORK_AREA").text().trim());
			var accepted_rate=$("#"+val+"ACCEPTED_RATE").val().trim();
			debugger;
			if(allocatedArea>actualArea){
				alert("you can not enter the value more than of availble area.");
				$("#" + val + "val").prop("checked", false);
				return false;
			}
			var len=$("input[name='chk1']:checked").length;
			if(len==0){
				alert("you can't remove last work order.");
				$("#" + val + "val").prop("checked", true);
				return false;
			}
			if($("#" + val + "val").is(":checked")==false){
				alert("removed order from list");
				$("#"+val+"ACCEPTED_RATE").val("0");
			
				$("#" + val + "AVAILABE_AREA").val("0");
			}else{
			var qty=parseInt($("#Quantity"+row).val());
			}
			
			
			if (isNaN(allocatedArea)&&allocatedArea!=0) {
				alert("Please enter only numbers.");
				$("#" + val + "AVAILABE_AREA").val("0");
				$("#" + val + "val").prop("checked", false);
			}
			
			if(allocatedArea==0){
				alert("Please enter correct value.");
				$("#" + val + "val").prop("checked", false);
				return false;
			}
			if(!isNum(accepted_rate)){
					alert("Please enter accepted rate.");
					$("#"+val+"ACCEPTED_RATE").focus();
					$("#" + val + "val").prop("checked", false);
					return false;
				}
		}
		
		function isNum(value) {
			  var numRegex=/^[0-9]+$/;
			  return numRegex.test(value)
		}
		function showScopeOfWork(row){
			$('#modalForScopeWork'+row).modal('show'); 
		}
		//for showing the selected work area
		function showDetailsFunction(rowsIncre) {
			
			debugger;
			var len=$("#lengthOfSelectedArea"+rowsIncre).val();
			var htmlData="";
			for (var i = 1; i <= len; i++) {
					var block_name=$("#BLOCK_NAMET"+rowsIncre+i+"").val();
					var floor_name=$("#FLOOR_NAMET"+rowsIncre+i+"").val();
					var flat_id=$("#FLAT_IDT"+rowsIncre+i+"").val();
					var seletedArea=$("#selectedAreaT"+rowsIncre+i+"").val();
					var recordType=$("#recordTypeT"+rowsIncre+i+"").val();
					var wo_measurmen_name=$("#wo_measurmen_nameT"+rowsIncre+i+"").val();
					var accepted_rate=$("#accepted_rateT"+rowsIncre+i+"").val();
					htmlData+=" <tr><td>"+block_name+"</td>	<td>"+floor_name+"</td>	<td>"+flat_id+"</td> <td>"+wo_measurmen_name+"</td><td>"+recordType+"</td> <td>"+seletedArea+"</td><td>"+accepted_rate+"</td </tr>";		
				}
			
			$("#tblShowDetails").html(htmlData);
		}
		
		
		function myFunction(workOrderID,acceptedRate,row,isShow) {
			var appendId=$("#allocatedareaAppend").val();
			//make the div data empty
					    if (typeof(Storage) !== "undefined") {
					         sessionStorage.setItem("${UserId}tempRowsIncre",row);
				        } else {
				           alert("Sorry, your browser does not support Web Storage...");
				        };
			$("#areaMapping").html("");
			 mesurmentId= $("#ChildProduct"+row).val();
			 
			 if(mesurmentId==undefined||mesurmentId==""||mesurmentId==null){
				alert("Please select Work description.");
				 return false;
			 }
			 mesurmentId= $("#actualWO_Desc"+row).val().split("$")[0];
			 var  UnitsOfMeasurementId=$("#actualunitsOfMeasurement"+row).val().split("$")[0];
//	 var f=$("#comboboxsubSubProd"+row1).val().split("$")[0];
			 $("#appendWorkOrderArea"+row).html("");
			 var siteId=$("#siteId").val();
			debugger;
						var url = "loadWOAreaMapping.spring";
						$.ajax({
							url : url,
							type : "get",
							data:{
								acceptedRate:acceptedRate,
								workOrderID:workOrderID,
								mesurmentId:mesurmentId,
								UnitsOfMeasurementId:UnitsOfMeasurementId,
								siteId:siteId,
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
								/* if(value.PRICE==undefined){
									if(value.BLOCK_NAME!=undefined&&value.WO_WORK_AREA_ID!=undefined){
										var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;	
										var flatname=value.FLATNAME==null?"-":value.FLATNAME;
										str+="<tr> "+
									"<td style='width:14%;'  class='text-center'><input type='checkbox' class='workareatr' id='"+value.WO_WORK_AREA_ID+"val' name='chk1' onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_ID+"'></td> "+
									"  <td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"BLOCK_NAME'>   "+value.BLOCK_NAME+" </td> "+
									" <td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"FLOOR_NAME'> "+value.FLOOR_NAME+"</td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"FLAT_ID'>"+(flatname)+"</td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"ACTUAL_AREA' > "+wo_work_area+" </td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"WORK_AREA' > "+value.WO_WORK_AVAILABE_AREA+" </td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"WO_MEASURMEN_NAME'> <label>"+value.WO_MEASURMEN_NAME+"</td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA12' contenteditable='true'>  <input type='text' class='form-control' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA' value='0'>  </td>"+
									    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA13'> <input type='text' class='form-control' id='"+value.WO_WORK_AREA_ID+"ACCEPTED_RATE' > </td>"+
									    "</tr>";
									}
							} */
						if(value.PRICE!=undefined&&value.PRICE!=""){
									var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
									var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
									var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;
									var flatname=value.FLATNAME==null?"-":value.FLATNAME;
								str+="<tr> "+
									"<td style='width:14%;'  class='text-center'><input type='checkbox'  checked='checked' class='workareatr' id='"+value.WO_WORK_AREA_ID+"val' name='chk1' onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_ID+"'></td> "+
									"<td style='width:14%;'  id='"+value.WO_WORK_AREA_ID+"BLOCK_NAME' title='"+value.WO_WORK_AREA_ID+"' >   "+value.BLOCK_NAME+"</td> "+
									"<td style='width:14%;'  id='"+value.WO_WORK_AREA_ID+"FLOOR_NAME'> "+floorName+"</td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"FLAT_ID' > "+flatName+"</td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"ACTUAL_AREA' > "+wo_work_area+" </td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"WORK_AREA' > "+value.WO_WORK_AVAILABE_AREA+" </td>"+
								    "<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"WO_MEASURMEN_NAME'> <label>"+value.WO_MEASURMEN_NAME+"</td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"RECORD_TYPE'>"+value.RECORD_TYPE+"</td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA12' contenteditable='true'> <input type='text' class='form-control' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA' onkeyup='validateWorkArea(this.value,"+rowNum+")' value='"+value.AREA_ALOCATED_QTY+"' readonly='true'><input type='text' style='width:100%;border: none;' value='selected' title='already alocated quantity' readonly > </td>"+
									"<td style='width:14%;' id='"+value.WO_WORK_AREA_ID+"AVAILABE_AREA13'> <input type='text' class='form-control' id='"+value.WO_WORK_AREA_ID+"ACCEPTED_RATE' value='"+value.PRICE+"'  readonly='true'> </td>"+
									"</tr>";
									   
									data1+="<input type='hidden' name='BLOCK_NAME1"+row+"' value='"+value.BLOCK_NAME+"'>";
									data1+="<input type='hidden' name='FLOOR_NAME1"+row+"' value='"+floorName+"'>";
									data1+="<input type='hidden' name='FLAT_ID1"+row+"' value='"+flatName+"'>";
 						            data1+="<input type='hidden' name='actualAreaAlocatedQTYPrice"+row+"' value='"+value.PRICE+"'>";
 						            	  
 									data1+=" <input type='hidden' name='actualAreaAlocatedQTY"+row+"' id='actualAreaAlocatedQTY"+rowNum+"' value='"+value.AREA_ALOCATED_QTY+"'>"+
									   "<input type='hidden' name='actualQS_WO_TEMP_ISSUE_DTLS_ID"+row+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'>";
									data1+="<input type='hidden' name='actualWorkAreaId"+row+"' id='actualWorkAreaId"+rowNum+"' value='"+value.WO_WORK_AREA_ID+"'>";
 									//data1+="<hr>";
 									
 									//data1+="<input type='hidden' name='actualWorkOrderAreaDataId"+row+"' id='actualWorkAreaId"+rowNum+"' value='"+value.WO_WORK_AREA_ID+"@@"+value.AREA_ALOCATED_QTY+"@@tempDTLSNo"+value.QS_WO_TEMP_ISSUE_DTLS_ID+"'>";
 									totalAreaQty+=+value.AREA_ALOCATED_QTY;
								}
								rowNum++;
								});
								//alert(totalAreaQty);
								//$("#Quantity"+row).val(totalAreaQty);
								$("#appendWorkOrderArea1"+row).html(data1);
								$("#areaMapping").html(str);
								if(isShow!="hide")
									$('#Modal-create-wo-popup').modal('show'); 

							},
							error : function(data, status, er) {
								alert(data + "_" + status + "_" + er);
							}
						});

						//$("#table-quantity").show();
		}
		
		$(document).ready(function() {
			//$("#workOrderFormId :input").prop("readonly", true);
		
						//this code is for appending all the values of allocated area
						$("#hide-table-2").click(function() {
						var rowsIncre=0;	var total = 0;
							 if (typeof(Storage) !== "undefined") {
						     
						         rowsIncre= sessionStorage.getItem("${UserId}tempRowsIncre");
					        } else {
					           alert("Sorry, your browser does not support Web Storage...");
					        };
							var rowNum=rowsIncre;
							var rows=0;
							var totalAmount=0;
							$.each($("input[name='chk1']:checked"), function(){            
								var htmlData="";
								debugger;
								rows++;
								var len = $("input[name='chk1']:checked").length;
				    			var workAreaId=$(this).val();
				                var seletedArea=parseFloat($("#"+workAreaId+"AVAILABE_AREA").val().trim());
				                var actualArea1=parseInt($("#"+workAreaId+"ACTUAL_AREA").text().trim());
				                var actualArea=$("#"+workAreaId+"WORK_AREA").text();
				                var wo_measurmen_name=$("#"+workAreaId+"WO_MEASURMEN_NAME").text();
				                var accepted_rate=parseFloat($("#"+workAreaId+"ACCEPTED_RATE").val().trim());
				                var recordType=$("#"+workAreaId+"RECORD_TYPE").text();
				                var availableArea=(actualArea-seletedArea);
				                totalAmount+=accepted_rate*seletedArea;
				            	var actualAreaAlocatedQTY =	parseInt($("#actualAreaAlocatedQTY"+rowNum).val());
				                /* if(seletedArea>actualArea){
				                	alert("you can't allocate the more than area, of availbale area.");
				                	$("#"+workAreaId+"AVAILABE_AREA").val(actualAreaAlocatedQTY);
				                	return false;
				                } */
				                
				                if(accepted_rate.length==0){
				                	alert("Please enter accepted rate.");
				                	$("#"+workAreaId+"ACCEPTED_RATE").focus();
				                	return false;
				                }
				                var value=seletedArea/actualArea;
				                var percentage=value*100;
				                
				    			
				                
				           		if (isNaN(seletedArea)) {
				    				alert("Please enter only digits.");
				    				$("#" + workAreaId + "AVAILABE_AREA").val("0");
				    				$("#" + workAreaId + "val").prop("checked", false);
				    				return false;
				    			}
				                if(seletedArea<=0){
				                	alert("Plese enter correct value for area");
				                	return false;
				                }
				                debugger;
				            	
				            		var actualWorkAreaId=	$("#actualWorkAreaId"+rowNum).val();
				            		htmlData+="<input type='hidden' name='lengthOfSelectedArea"+rowsIncre+"' id='lengthOfSelectedArea"+rowsIncre+"' value='"+len+"'>";
				          		htmlData+="<input type='hidden' name='percentage"+rowsIncre+"' value='"+percentage+"'>";
				                htmlData+="<input type='hidden' name='selectedArea"+rowsIncre+"' id='selectedAreaT"+rowsIncre+rows+"' value='"+seletedArea+"'>";
				                htmlData+="<input type='hidden' name='actualArea"+rowsIncre+"' value='"+actualArea+"'>";
				                htmlData+="<input type='hidden' name='workAreaId"+rowsIncre+"' value='"+workAreaId+"'>";
				                htmlData+="<input type='hidden' name='available"+rowsIncre+"' value='"+availableArea+"'>";
				                htmlData+="<input type='hidden' name='record_type"+rowsIncre+"'  id='recordTypeT"+rowsIncre+rows+"'  value='"+recordType+"'>";
				                htmlData+="<input type='hidden' name='wo_measurmen_name"+rowsIncre+"'  id='wo_measurmen_nameT"+rowsIncre+rows+"'  value='"+wo_measurmen_name+"'>";
				                htmlData+="<input type='hidden' name='accepted_rate"+rowsIncre+"' id='accepted_rateT"+rowsIncre+rows+"' value='"+accepted_rate+"'>";
				                
				                var block_name=$("#"+workAreaId+"BLOCK_NAME").text().trim();
				                var floor_name=$("#"+workAreaId+"FLOOR_NAME").text().trim();
				                var flat_id=$("#"+workAreaId+"FLAT_ID").text().trim();
				                if(block_name!=""&&floor_name!=""){
				             		 htmlData+="<input type='hidden' name='BLOCK_NAME"+rowsIncre+"' readonly='true'  id='BLOCK_NAMET"+rowsIncre+rows+"' value='"+block_name+"'>";
					                htmlData+="<input type='hidden' name='FLOOR_NAME"+rowsIncre+"readonly='true'  id='FLOOR_NAMET"+rowsIncre+rows+"'  value='"+floor_name+"'>";
					                htmlData+="<input type='hidden' name='FLAT_ID"+rowsIncre+"' id='FLAT_IDT"+rowsIncre+rows+"' readonly='true' value='"+flat_id+"'>";
			                	//htmlData+="<input type='text' name='changedWorkOrderAreaDataId"+rowsIncre+"' id='actualWorkAreaId"+rowsIncre+"' value='"+workAreaId+"@@"+seletedArea+"@@"+percentage.toFixed(2)+"'>";
				             	//$("#appendWorkOrderArea"+rowsIncre).append(htmlData);
				                }
				               
				            	
					            total += seletedArea;
				            	$("#appendWorkOrderArea"+rowsIncre).append(htmlData);
				               
				                rowNum++;
				  				
				    		});
				
							var id12=$("#appendData"+rowsIncre).val();
				      	//    var tot=parseInt($("#Quantity"+rowsIncre).val());
				        	//var acceptedRate=$("#AcceptedRate1"+rowsIncre).val();
				       		
				        	//$("#TotalAmountId"+rowsIncre).val((total)*acceptedRate); 
				        	//$("#TotalAmountId"+rowsIncre).val(totalAmount);
				          	//$("#Quantity"+rowsIncre).val(total); 
				          	$("#showQty"+rowsIncre).show();
							$("#table-quantity").hide();
						});
						/* selectall checkbox */
						$("#checkAll").click(function() {
									$('input:checkbox').not(this).prop('checked', this.checked);
						console.log("checked");
						});

		});
		</script>


		<script type="text/javascript">

</script>
		<script>
	$(document).ready(function() {
	    
		if(window.history.length==1){
			$("#closeBtn").hide();
		}
		
		var max_fields      = 50; //maximum input boxes allowed
	    var wrapper         = $(".appen-div-workorder"); //Fields wrapper
	    var add_button      = $(".add_field_button"); //Add button ID
	    
	    var x = 1; //initlal text box count
	    $('.add_field_button').on("click",function(e){ //on add input button click
	        e.preventDefault();
	    var newTc=$("#workorder_modal_text1").val();
	    if(newTc.length==0){
	    	alert("Please enter terms and condition");
	    	return false;
	    }
	    
	    if(x < max_fields){ //max input box allowed
	            x++; //text box increment
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-10"><input type="text" class="form-control workorder_modal_text"  value="'+newTc+'" name="changedTC"/></div><div class="col-md-2 margin-close"><button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	     }
	        $("#workorder_modal_text1").val("");
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent().parent(".remove-filed").remove(); x--;
	    })
	});
	
 $(document).ready(function() {
		 var rows = document.getElementById("indentIssueTableId").getElementsByTagName("tr").length;
		for(var i=0;i<rows-1;i++){
			//for combobox
			$("#combobox"+(i+1)).combobox();    
			$( "#comboboxsubProd"+(i+1)).combobox();
			$("#comboboxsubSubProd"+(i+1)).combobox(); 
			$('.btn-visibilty'+(i+1)).closest('td').find('.custom-combobox-toggle').addClass('hide');
			//for readonly mode
			$('#Product'+(i+1)).attr('readonly', true);
			$('#Product'+(i+1)).addClass('form-control');
			$('#SubProduct'+(i+1)).attr('readonly', true);
			$('#SubProduct'+(i+1)).addClass('form-control');
			$('#ChildProduct'+(i+1)).addClass('form-control');
			$('#ChildProduct'+(i+1)).attr('readonly', true);
			$('#UnitsOfMeasurementId'+(i+1)).attr('disabled', true);
			$('#Quantity'+(i+1)).attr('readonly', true);
			
		}
		 
		
	});
/*  function validateFileExtention(){
	  debugger;
	var ext="";
	var kilobyte=1024;
	  $('input[type=file]').each(function () {
	        var fileName = $(this).val().toLowerCase(),
	         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
	        	debugger;
			 if(fileName.length!=0){
	        	if((this.files[0].size/kilobyte)<=kilobyte){
				}else{
					alert("Please upload below 1 MB PDF file");
					//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
					$(this).val('');
				return false;
				}
	        	
		 //  if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			// ext= fileName.substring(fileName.lastIndexOf(".")+1); 
		   
		   
		        if (!(regex.test(fileName))) {
		            $(this).val('');
		            alert('Please select correct file format');
		        }
	        }
	    });
	} */
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
		// deleteFile(divId,imagePath);
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
			 for (var imgIndex = 0; imgIndex < deleteUploadedimageFiles.length; imgIndex++) {
					if(deleteUploadedimageFiles[imgIndex].length!=0){
						$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='imgPathtoDelete' id='imgPdfPath"+count+"'>");
						//deleteFile(imgPdfIndex,deleteUploadedimageFiles[imgPdfIndex].url);
						imgePath=deleteUploadedimageFiles[imgIndex].url.split("deleteWOPermanentImage.spring?")[1];
						$("#imgPdfPath"+count).val(imgePath);
						count++;
					}
				} 
			 deleteUploadedPDFFiles.sort(sortPdfData);
			 for (var pdfIndex = 0; pdfIndex < deleteUploadedPDFFiles.length; pdfIndex++) {
					if(deleteUploadedPDFFiles[pdfIndex].length!=0){
						$("#allImgPdfUrlToDeleteFile").append("<input type='hidden' name='PdfPathtodelete' id='imgPdfPath"+count+"'>");
						//deleteFile(imgPdfIndex,deleteUploadedPDFFiles[pdfIndex].url);
						pdfPath=deleteUploadedPDFFiles[pdfIndex].url.split("deleteWOPermanentImage.spring?")[1];
						$("#imgPdfPath"+count).val(pdfPath);
						count++;
					}
				}
			  document.getElementById("workOrderFormId").action = "updatePermantWorkOrderDetail.spring";
			   document.getElementById("workOrderFormId").method = "POST";
			   document.getElementById("workOrderFormId").submit();
		}
 function deleteFile(divId,url){
	 debugger;
	// var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
	
	 $.ajax({
		  url : url,
		  type : "get",
		  success : function(data) {
	//		console.log(data);
	//  	 window.location.reload();
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
	 return false;
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
	</script>
	<!-- modal popup for show quantity strat -->
	<div id="myModal-showwo-showquantity" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">View Area Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive">
       <table class="table table-bordered table-createwo-showquantity">
       <thead>
        <tr>  
         <th>Block</th>
         <th>Floor</th>
         <th>Flat</th>
         <th>Measurement</th>
         <th>Record Type</th>
         <th>Allocated Area</th>
         <th>Accepted Rate</th>
        </tr>
       </thead>
       <tbody id="tblShowDetails">

       </tbody>
       </table>
      
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>
	<!-- modal popup for show quantity end -->
	<!-- modal popup for create work order table for show start -->
<!-- Modal -->
<div id="Modal-create-wo-popup" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:90%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Work Area</h4>
      </div>
      <div class="modal-body">
     <div class="table-responsive">
      <table class="table fixedheader">
	      <thead>
			  <tr>
			    <th class="text-center"><input type="checkbox" id="checkAll" style="width:14px; height:14px;"></th>
			    <th>Block</th>
			    <th>Floor</th>
			    <th>Flat</th>
			    <th>Actual Area</th>
			    <th>Available Area</th>
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
        <div class="text-center center-block">
         <button type="button" id="hide-table-2" class="btn btn-danger" data-dismiss="modal">Submit</button>
        </div>
      </div>
    </div>

  </div>
</div>
<!-- modal popup for create work order table for show end -->
	
	
	<!-- model popup for pdf start  -->
	<%
	  int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
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
	     <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf('<%=pdfName %>','${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button>
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
	<%	 int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
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
	<script>
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
		
		//this code for to active the side menu 
		var referrer="updateWorkOrder.spring";
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
