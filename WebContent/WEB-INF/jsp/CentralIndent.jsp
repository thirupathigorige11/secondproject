<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import = "java.util.ResourceBundle" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String tableHead = resource.getString("label.indentIssueTableHead");
	String colon = resource.getString("label.colon");
	
	String reqId = resource.getString("label.reqId");
	String date = resource.getString("label.date");
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
	String measurement = resource.getString("label.measurement");
	String remarks = resource.getString("label.remarks");
	String uOrF = resource.getString("label.uOrF");
	String note = resource.getString("label.note");
	String productAvailability = resource.getString("label.productAvailability");
	String actions = resource.getString("label.actions");
	//Loading Indent Issue Table Column Headers/Labels - Start
%>

<html>
<head>
<script src="js/CentralIndent.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/modal-minimize-and-maximize/font-awesome.css" rel="stylesheet" type="text/css" /> 
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		

		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script  src="js/sidebar-resp.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style type="text/css">  

    .modal-header .btnGrp{
      position: absolute;
      top: 8px;
      right: 10px;
    } 
 

    .min{
         width: auto; 
        height: 35px;
        overflow: hidden !important;
        padding: 51px !important;
        margin: 2px;    

        float: left;  
        position: static !important; 
      }

    .min .modal-dialog, .min .modal-content{
        height: 100%;
        width: 100%;
        margin: 0px !important;
        padding: 0px !important; 
      }

    .min .modal-header{
        height: 100%;
        width: 100%;
        margin: 0px !important;
        padding: 3px 5px !important; 
      }

    .display-none{display: none;}

    button .fa{
        font-size: 16px;
       /*  margin-left: 10px; */
      }

    .min .fa{
        font-size: 14px; 
      }

    .min .menuTab{display: none;}

    button:focus { outline: none; }

    .minmaxCon{
     height: 115px;
     width:500px;
    bottom: 0px;
    left: -50px;
    position: fixed;
    right: 1px;
    z-index: 9999;
    }
    .table>thead:first-child>tr:first-child>th {
    border-top: 0;
}
.notificationNumber{
	position: absolute;
    top: -8px;
    color: #fff;
    background-color: red;
    display: inline-block;
    min-width: 10px;
    padding: 3px 7px;
    font-size: 12px;
    font-weight: 700;
    line-height: 1;
    text-align: center;
    white-space: nowrap;
    vertical-align: middle;
    border-radius: 10px;
}
#tblnotification3 thead tr th, #tblnotification3 tbody tr td, #tblnotification  tbody tr td, #tblnotification  thead tr th, #tblnotification1  tbody tr td, #tblnotification1  thead tr th{text-align:center;}
.bordernone{border: none;}
.plusbtn{
	width: 30px;
    background: green;
    padding: 5px;
    text-align: center;
    border-radius: 50%;
    float: left;
}
.minusbtn{
	width: 30px;
    background: red;
    padding: 5px;
    text-align: center;
    border-radius: 50%;
    float: left;
    margin-left:10px;
}
.whitecolor{
	color:#fff;
}
.actionbtn .btnaction{
    margin:0px 5px;
}
table tbody {
font-size: 14px;
}
  </style> 

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
           
            
            var prodId = "";
            var prodName = "";
            
            prodId = ui.item.option.value;
            prodName = ui.item.value;
            
            var ele = this.element[0].name;
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            
	        var productColumn =  "<%= product %>";
		    productColumn = formatColumns(productColumn);
		  	 
		  	var subProductColumn =  "<%= subProduct %>";
		  	subProductColumn = formatColumns(subProductColumn);
		     
		 	var childProductColumn =  "<%= childProduct %>";
		 	childProductColumn = formatColumns(childProductColumn);
			
			var rowNum = ele.match(/\d+/g);
			             
            if(str1 == productColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                loadSubProds(prodId, rowNum);
            }            
            else if(str1 == subProductColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                loadSubSubProducts(prodId, rowNum);
            }
            else if(str1 == childProductColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                loadUnits(prodId, rowNum);
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
	  $("#ReqDateId").datepicker({
		  dateFormat: 'dd-M-y',
		maxDate: new Date()
	  
	  });
  });
  
</script>

<script type="text/javascript">

</script>
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
						<jsp:include page="SideMenu.jsp" />  
					</div>
					</div>
						<jsp:include page="TopMenu.jsp" />  
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Central Indent</li>
						</ol>
					</div>
 <div align="center">
		<form:form modelAttribute="indentCreationModelForm" id="CentralIndentFormId" class="form-horizontal">
		  <div class="col-md-12 border-inwards-box">
		<span style="color:red;">${noStock}</span><br/>
	 	    <c:forEach items="${IndentDtls}" var="element">
	 	   <div class="col-md-6"><div class="form-group">
			<label class="control-label col-md-6">Site Wise Indent Number :</label>
			<div class="col-md-6" >
				<input name="indentNumber" id="indentNumberId"  type="hidden" readonly="true" value="${element.indentNumber}" class="form-control"/>
				<input name="siteWiseIndentNo" id="siteWiseIndentNo"  readonly="true" value="${element.siteWiseIndentNo}" class="form-control"/>
			</div>
			</div></div>
			 <div class="col-md-6">
			  <div class="form-group">
			<label class="control-label col-md-6">Site Name :</label>
			<div class="col-md-6" >
				<input name="siteName" id="siteNameId"  readonly="true" value="${element.siteName}" class="form-control"/>
				<input type="hidden" name="siteId"   value="${element.siteId}">	
 			    <input type="hidden" name="strSiteId"   value="${siteId}">
 			    <input type="hidden" name="urlForActivateSubModule" value="${urlForActivateSubModule}">
 			  </div>
 			  </div>
			 </div>
 			   <div class="col-md-6">
 			    <div class="form-group">
 			  <label class="control-label col-md-6">Indent Name :</label>
			<div class="col-md-6" >
				<input name="indentName" id="indentNameId"  readonly="true" value="${element.indentName}" class="form-control"/>  
 			  </div>
			</div>
 			   </div>
 			   
 			 
 			   
			 </c:forEach>
		   <input type="hidden" name="numberOfRows"   value="${requestScope['IndentDetails'].size()}">
			
		   <div style="display: none;">
		   <c:forEach items="${IndentDetails}" var="element"> 
		   	<input name="indentCreationDetailsId${element.strSerialNumber}" value="${element.indentCreationDetailsId}" />
			<input name="productId${element.strSerialNumber}" value="${element.productId1}" />
			<input name="subProductId${element.strSerialNumber}" value="${element.subProductId1}" />
			<input name="childProductId${element.strSerialNumber}" value="${element.childProductId1}" />
			<input name="unitsOfMeasurementId${element.strSerialNumber}" value="${element.unitsOfMeasurementId1}" />
			<input name="requiredQuantity${element.strSerialNumber}" value="${element.requiredQuantity1}" />
			
			<input name="productName${element.strSerialNumber}" value="${element.product1}" />
			<input name="subProductName${element.strSerialNumber}" value="${element.subProduct1}" />
			<input name="childProductName${element.strSerialNumber}" value="${element.childProduct1}" />
			<input name="unitsOfMeasurementName${element.strSerialNumber}" value="${element.unitsOfMeasurement1}" />
			<input name="remarks${element.strSerialNumber}" value="<c:out value='${element.remarks1}'/> " />
			
			</c:forEach>
		   </div>
	  		</div>
	  		<div class="col-md-12 text-right mrg-bottom10">
				<button class="btn btn-warning" type="button" id="showPopUp" onclick="showPopUpForSubmit()">
					<i class="fa fa-shopping-cart" aria-hidden="true"></i>
					<span class="notificationNumber" id="noOfSettled">0</span>
				</button>
			</div>
			<div class="clearfix"></div>
<!-- *************** Table 01 grid***************** -->
			<div class="table-responsive">
				<table id="tblnotification"	class="table table-new Mrgtop10" cellspacing="0">
				 <thead>
					<tr>
						<th>S NO</th>
						<th>Product Name</th>
						<th>Sub Product Name</th>
						<th>Child Product Name</th>
						<th>UOM</th>
						<th>Requested Quantity</th>
						<th>Allocated Quantity</th>
						<th>Pending Quantity</th>
						<th>Initiated Quantity</th>
						<th>Comment</th>
						<th>Remarks</th>
						<th>Actions</th>
					</tr>
				 </thead>
				<tbody  id="indentProductData">
			   <c:forEach items="${IndentDetails}" var="element"> 
				<tr  id="indentproductrow${element.strSerialNumber}" class="indentProductClass">	
					<td><div id="snoDivId1">${element.strSerialNumber}</div></td>	
					<td>${element.product1}</td>
					<td>${element.subProduct1}</td>
					<td><a href="javascript:void(0);" data-toggle="tooltip" title="${element.strMouseOverData}" >${element.childProduct1}</a> </td>
					<td>${element.unitsOfMeasurement1}</td>
					<td id="RequestedQty${element.strSerialNumber}">${element.requiredQuantity1}</td>
					<td id="AllocatedQty${element.strSerialNumber}" class="allocatedQuantity">${element.allocatedQuantity}</td>
					<td id="PendingQty${element.strSerialNumber}" class="pendingQuantity">${element.pendingQuantity}</td>
					<td id="InitiatedQty${element.strSerialNumber}">${element.intiatedQuantity}</td>
					<td><input type="text" name="comments${element.strSerialNumber}" id="comments${element.strSerialNumber}" class="form-control"> </td>	
					<td><c:out value="${element.remarks}"/></td>
					<td id="sttle-id1" class="td-settle"><a  style="color: blue;cursor:pointer;" >SETTLE</a></td>	
					<td style="display:none;">${element.strMouseOverData}</td>
					<td style="display:none;" id="MainTablechildProductId${element.strSerialNumber}">${element.productId1}@@${element.subProductId1}@@${element.childProductId1}@@${element.unitsOfMeasurementId1}@@${element.indentProcessId}</td>
					<td style="display:none;">${element.product1}@@${element.subProduct1}@@${element.childProduct1}@@${element.unitsOfMeasurement1}@@${element.indentProcessId}</td>
				</tr>
			   </c:forEach>
			</tbody>
		</table>
		</div>
		<div class="">
          <button type="button" class="btn btn-warning"   id="sendToPurBtn" onclick="sendToPD('SaveClicked')">Send To Purchase Dept</button>
          <input type="button"  class="btn btn-warning" data-toggle="modal"  value="Cancel Indent" onclick="return cancelIndent()" id="cancelIndentBtn"/>
        
          <c:forEach items="${IndentDtls}" var="element">
        	<a class="btn btn-warning" target="_blank" href="ViewMyRaisedIndentsDetails.spring?siteWiseIndentNo=${element.siteWiseIndentNo}&indentNumber=${element.indentNumber}&siteId=${element.siteId}&url=${urlForActivateSubModule}">View Indent</a>
          </c:forEach>
        	
        </div>
		<!-- *************** Below table************ -->
		<div class="container2" id="table-2" style="display: none;margin-top:40px;"  id=""   uda-rowId='1'>
			<!-- <span class="fa fa-plus addicon" aria-hidden="true" style="float: right;font-size: 19px;margin-left: 10px;"></span> -->
			<table id="tblnotification1"	class="table table-striped table-bordered st-table" cellspacing="0">
				<thead style="background-color: #e0dada;">
					<tr  style="background: #bbbbbb;">
						<th>S NO</th>
						<th>Product Name</th>
						<th>Sub Product Name</th>
						<th>Child Product Name</th>
						<th>Quantity</th>
						<th style="width:150px;">Adjusted Quantity</th>
						<th style="width:200px;">Site Name</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
				
				</tbody>
			</table>
			<div class="col-md-12 text-center center-block">
					<input id="add" type="button" class="btn btn-warning modal-add"  value="Save">
			</div>   
		</div>
		
		 <!-- modal popup for indent reject start-->
	 <div id="modalCentralIndent-reject" class="modal fade" role="dialog">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Please Enter The Comment</h4>
	      </div>
	      <div class="modal-body" style="overflow:hidden;">
	      <textarea class="form-control" style="resize:vertical;" name="indentRemarks">
	      </textarea>
	        
	      </div>
	      <div class="modal-footer">
	       <div class="col-md-12 text-center center-block">
	        <button type="button" class="btn btn-warning" onclick="reject('SaveClicked')" data-dismiss="modal">Submit</button>
	       </div>
	      </div>
	    </div>
	  </div>
	</div>
<!-- modal popup for indent reject end -->
		</form:form>
		<!-- ************ Model*********** -->

	<div class="modal fade mymodal" id="modal-1" role="dialog">
	   <form:form modelAttribute="indentReceiveModelForm" id="doInventoryFormId" class="form-horizontal">
	    <div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	      </div>
	      <div class="modal-body">
	        <div class="container3">
				<table id="tblnotification3"	class="table table-bordered st-table" cellspacing="0">
				  <thead style="background-color: #e0dada;">
					<tr>
					  <th>Product </th>
					  <th>SubProduct </th>
					  <th>ChildProduct </th>
					  <th>Allocating</th>
					  <th>Quantity</th>
					  <th>Actions</th>
					</tr>
				  </thead>
					<tbody>
					
					</tbody>
				</table>
			  <div>
			   <c:forEach items="${IndentDtls}" var="element">
			     <input type="hidden" name="bfIndentNumber" value="${element.indentNumber}" />
				 <input type="hidden" name="siteWiseIndentNo12" id="siteWiseIndentNo12"  value="${element.siteWiseIndentNo}" />
				 <input type="hidden" name="siteName" id="siteNameId"  value="${element.siteName}"  />
			     <input type="hidden" name="bfSiteId" value="${element.siteId}" />
			     <input type="hidden" name="urlForActivateSubModule" value="${urlForActivateSubModule}">		
			  </c:forEach>
			   <input type="hidden" id="bfRows" name="bfRows" value="0" />
			 </div>
			</div>
	          <button type="button" class="btn btn-warning" id="saveBtnId" onclick="saveRecords('SaveClicked')">Submit</button>
	      </div>
	    </div>
	   </div>
   	  </form:form>
  	 </div>
  	 <div class="minmaxCon"></div>  
     <input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
	 <input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
	</div>
  </div>
</div>
</div>
</div>
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<script src="js/custom.js"></script>
<script src="js/CentralIndent.js"></script>
  <script>
		    var prodName = "";
			$(document).ready(function() {
					$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			
		/* 	$(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
			//clicking on settle hyperlink
			$(".td-settle").click(function(){
				debugger;
				//$('#tblnotification1').find('tbody').empty();
				var val = $(this).closest('tr');
			   	var product =  $(val).find('td')[1].innerHTML;			   	
				var innerProduct =  $(val).find('td')[2].innerHTML;	
				var childProduct =  $(val).find('td')[3].innerHTML;	
				var Quantity =  $(val).find('td')[7].innerHTML-$(val).find('td')[8].innerHTML;
				if(Quantity==0){
					alert("Sorry! you can not settle this product the settlement quantity is zero.");
					//generatingError=0;
					return false;
				}
				var datawithbr = $(val).find('td')[12].innerHTML;
				var bulkdata = $(val).find('td')[13].innerHTML;
				var bulkdataProdName = $(val).find('td')[14].innerHTML;
				var childproId=bulkdata.split("@@")[2];	
				var length=$(".settleTableRow").length;
				var rowId;
				if(length==0){
					rowId=1;
				}else{
					rowId=parseInt($('#tblnotification1 tr:last').attr('id').split("settleTableRow")[1])+1;		
				}
				$(".container2").fadeIn("slow");
				$("#tblnotification1").show();
				$("#add").show();
				
			    var data = '<tr class="settleTableRow" id="settleTableRow'+rowId+'">';
			    data += '<td>'+rowId+'</td>';
			    data += '<td class="tiptext">'+product+'</td>';
			    data += '<td>'+innerProduct+'</td>';
			    data += '<td><span class="childname">'+childProduct+'</span><input type="hidden" id="lowchildProductId'+rowId+'" onblur="lowerSettTableSiteChange('+rowId+')" value="'+childproId+'" class="childId"></td>';
			    data += '<td id="quantity'+rowId+'">'+Quantity+'</td>';			 
				data += '<td><input type="text" class="form-control aqunatinty adjustedQuantity" name= "aquantity" id="Aquantity'+rowId+'" onkeypress="return isNumberCheck(this, event)" onblur="lowerSettTableSiteChange('+rowId+')"/><span class="errormag" style="display:none;color:red;">Allocating quantity should be below Quantity</span></td>';
				data += '<td><select  id="site_id'+rowId+'"  name="site_id"  class="form-control" onchange="lowerSettTableSiteChange('+rowId+')">';
				data += '<option id=""  value="">-- Select Site--</option>';
				var arr=datawithbr.split("\n");
				var len=arr.length-1;
				if(len==0){
					alert("You can not settle this product, because this product not availble in any site");
					$('#tblnotification1').hide();
					$(".container2").hide();
					return false;
				}
				var siteNameId=$("#siteNameId").val();
				for(i=0;i<len;i++){						
					var arr1=arr[i].split("=");
					if(siteNameId!=arr1[0]){
					data += '<option   value="'+arr1[1]+'-'+arr1[0]+'">'+arr1[0]+'</option>';
					
					//	alert("You can not settle this product, because you can not settle the same product to same indent ");
						//$('#tblnotification1').hide();
						//$(".container2").hide();
					//	return false;
					}
				}
				data += '</select>	</td>';
				data += '<td style="display:none;">'+datawithbr+'</td>';
				data += '<td style="display:none;">'+bulkdata+'</td>';
				data+='<td style="display:none;">'+bulkdataProdName+'</td>';
				data += '<td><div class="col-md-12 text-center center-block actionbtn"><button type="button" class="btnaction btnaction" onclick="addRow('+rowId+')"><i class="fa fa-plus"></i></button><button type="button" id="deleteSettleMentData" class="btnaction deletebtnaction" onclick="deleteSettleTableRow('+rowId+')"><i class="fa fa-trash"></i></button></div></td>';
				data += '</tr>';
			    
				$('#tblnotification1').find('tbody').append(data);
				//$('#tblnotification1').fadeIn("slow");
				$(".adjustedQuantity").bind('paste', function (e) {
					e.preventDefault();
				});
			});
			//it will execute when you click on settle hyperlink
			$(".td-settle").click(function(){
			
			});
			//adding row when you click on plus button in lower settled table
			function addRow(id){
				var arr1 = $('#settleTableRow'+id);
				var cloneIndex = $("#container2").length;
			 	var rowId =  parseInt($("#table-2").attr('uda-rowId'));
				var newRowId = rowId+1;
				var newRowId = rowId+1;
			   	var product =  $(arr1).find('td')[1].innerHTML;
				var innerProduct =  $(arr1).find('td')[2].innerHTML;	
				var childProduct =  $(arr1).find('td')[3].firstElementChild.firstChild.outerHTML;
				var childproId =  $(arr1).find('td')[3].lastChild.value;				
				var Quantity =  $(arr1).find('td')[4].innerHTML;
				var datawithbr = $(arr1).find('td')[7].innerHTML;	
				var bulkdata = $(arr1).find('td')[8].innerHTML;
				var bulkdataProdName = $(arr1).find('td')[9].innerHTML;
				var length=$(".settleTableRow").length;
				var rowId;
				debugger;
				if(length==0){
					rowId=1;
				}else{
					rowId=parseInt($('#tblnotification1 tr:last').attr('id').split("settleTableRow")[1])+1;		
				}
				
			    var data = '<tr class="settleTableRow" id="settleTableRow'+rowId+'">';
			    data += '<td>'+rowId+'</td>';
			    data += '<td class="tiptext">'+product+'</td>';
			    data += '<td>'+innerProduct+'</td>';
			    data += '<td><span class="childname">'+childProduct+'</span><input type="hidden" class="childId" id="lowchildProductId'+rowId+'" value="'+childproId+'"></td>';
			    data += '<td id="quantity'+rowId+'">'+Quantity+'</td>';			   
				data += '<td><input type="text" class="form-control aqunatinty adjustedQuantity" name= "aquantity" id="Aquantity'+rowId+'"   onblur="lowerSettTableSiteChange('+rowId+')" onkeypress="return isNumberCheck(this, event)"/><span class="errormag" style="display:none;color:red;">Allocating quantity should be below Quantity</span></td>';
				data += '<td><select  id="site_id'+rowId+'"  name="site_id"  class="form-control" onchange="lowerSettTableSiteChange('+rowId+')">';
				data += '<option id=""  value="">-- Select Site--</option>';
				
				var arr=datawithbr.split("\n");
				var len=arr.length-1;
				var siteNameId=$("#siteNameId").val();
				for(i=0;i<len;i++){						
					var arr1=arr[i].split("=");
					if(siteNameId!=arr1[0]){
						data += '<option   value="'+arr1[1]+'-'+arr1[0]+'">'+arr1[0]+'</option>';
					}
				}
				data += '</select>	</td>';
				data += '<td style="display:none;">'+datawithbr+'</td>';
				data += '<td style="display:none;">'+bulkdata+'</td>';
			    data+='<td style="display:none;">'+bulkdataProdName+'</td>';
				data += '<td><div class="col-md-12 text-center center-block actionbtn"><button type="button" class="btnaction" onclick="addRow('+rowId+')"><i class="fa fa-plus"></i></button><button type="button" id="deleteSettleMentData" class="btnaction deletebtnaction" onclick="deleteSettleTableRow('+rowId+')"><i class="fa fa-trash"></i></button></div></td>';
				data += '</tr>';
			    
				$('#tblnotification1').find('tbody').append(data);
				$('#tblnotification1').fadeIn(500);
				$(".adjustedQuantity").bind('paste', function (e) {
					e.preventDefault();
				});
			}
			
			
			//clicking on save button
			
			$("#add").click(function(){
				debugger;
				//validating settled table
				var valSettledTable=validateSettleTable();
				if(valSettledTable==false){
					return false;
				}
				
				var quan = 0;
				var rownum = 0; 
				$('#tblnotification1').find('tbody').find('tr').fadeOut(500);
				//$('#tblnotification1').find('tbody').find('tr').css("display", "none");  
				$('#tblnotification1').find('tbody').innerHTML = "";
				$('#tblnotification3').find('tbody').empty();
				var arr = $('#tblnotification1').find('tbody').find('tr');
			
				
			    jQuery.each(arr,function(index,val){
					debugger;
				var rowNumber= val.id.split("settleTableRow")[1];
			    var product =  $(val).find('td')[1].innerHTML;
				var innerProduct =  $(val).find('td')[2].innerHTML;	
				var childProduct =  $(val).find('td')[3].innerHTML;	
				var Quantity = $(val).find('td')[4].innerHTML;	
 		//		var Quantity1 = $($(val).find('td'))[3].text();
			    var aQuantity =$(val).find('#Aquantity'+rowNumber).val();
			    quan += parseInt(aQuantity);
			    var siteName = $($(val).find('td')[6]).find('select option:selected').text();
			    var bulkdata = $(val).find('td')[8].innerHTML;
			    var bulkdataProdName = $(val).find('td')[9].innerHTML;
			    var datawithiseq = $(val).find('#site_id').val();
			    //var siteval = datawithiseq.split("-")[0];
			    
				var datawithiseq = $(val).find('td option:selected').val();
				var siteval = datawithiseq.split("-")[0];
				
				rownum++;
				
			    var data = '<tr class="finalSettledRow" id="finalTableRow'+rownum+'">';
			    data += '<td class="tiptext">'+product+'</td>';
			    data += '<td>'+innerProduct+'</td>';
			    data += '<td>'+childProduct+'</td>';			
			    data += '<td> <input type="text" class="bordernone"  readonly="true" name="sitename'+rownum+'" value="'+siteName+'" title="'+siteName+'"/></td>'
			    data += '<td><input type="text" class="bordernone"  readonly="true" name="aquantity'+rownum+'" value="'+aQuantity+'" title="'+aQuantity+'"/> </td>'
			    data += '<td style="display:none;"><input type="text" readonly="true" name="bulkdata'+rownum+'" value="'+bulkdata+'"/><input type="text" readonly="true" name="bulkdataProdName'+rownum+'"   id="bulkdataProdNameA'+rownum+'"/><input type="hidden" name="settlementRowNumber" value="'+rownum+'"></td>';
			    data += '<td><div class="col-md-12 text-center center-block actionbtn"><button type="button" id="" onclick="deleteFinalSettled(this, '+rownum+')" class="btnaction"><i class="fa fa-trash"></i></button></div></td>'; 
			    data += '</tr>';
			    
			    $("#bfRows").val(rownum);
			    $('#tblnotification3').find('tbody').append(data);
			    $("#bulkdataProdNameA"+rownum).val(bulkdataProdName);
			    debugger;
			  /*  $(this).find(data).remove(); */
			   }); 

				
			  /*   var noOfSettled= $('#tblnotification3').find('tbody').find("tr").length;
			    $("#noOfSettled").text(noOfSettled); */
			    var noOfSettled=$(".settleTableRow").length;
				 $("#noOfSettled").text(noOfSettled);
				 var cartLength=$("#noOfSettled").text();
					if(cartLength!=0){
						$("#sendToPurBtn").attr("disabled", "disabled");
						$("#cancelIndentBtn").attr("disabled", "disabled");
					}else{
						$("#sendToPurBtn").removeAttr("disabled");
						$("#cancelIndentBtn").removeAttr("disabled");
					}
				 
				 $("#noOfSettled").animate({ fontSize: "24px" }, 500 );
				 $("#noOfSettled").animate({ color: "green" }, 500 );
				 $("#noOfSettled").animate({ fontSize: "12px" }, 500 );
				 //$("#noOfSettled").animate({ color: "#fff" }, 1000 );
				 $(".container2").fadeOut("slow");
			});
			
  /*  *************** Method for Minimise and maxmise the model table*********** */
	 $(document).ready(function(){
	     var $content, $modal, $apnData, $modalCon; 
	     $content = $(".min");   
	     $(".modalMinimize").on("click", function(){debugger;
	     		$modalCon = $(this).closest(".mymodal").attr("id"); 
	     		/* var data = '<tr>'; */
	            $apnData = $(this).closest(".mymodal");
	            $modal = "#" + $modalCon;
	            $(".modal-backdrop").addClass("display-none");   
	            $($modal).toggleClass("min");  
	            if ( $($modal).hasClass("min") ){ 
	            	   $("#table-2").hide();
	                   $(".minmaxCon").append($apnData);  
	                   $(this).find("i").toggleClass( 'fa-minus').toggleClass( 'fa-clone');
	            }else { 
	               	// $('.tblnotification3').find('tbody').append($apnData);
	                   $(".container").append($apnData); 
	                   $(this).find("i").toggleClass( 'fa-clone').toggleClass( 'fa-minus');
	             };
	      });
	
	      $("button[data-dismiss='modal']").click(function(){ 
	          $(this).closest(".mymodal").removeClass("min");
	          $(".container2").removeClass($apnData);   
	          $(this).next('.modalMinimize').find("i").removeClass('fa fa-clone').addClass( 'fa fa-minus');
	      });	
	 }); 
  
  /*  **************** Loop Code*************** */
	 $(document).on("click", ".addicon",function(){
	   var regex = /^(.+?)(\d+)$/i;
	   var cloneIndex = $("#container2").length;
	   var rowId =  parseInt($("#table-2").attr('uda-rowId'));
	   var newRowId = rowId+1;
	   $("#rowId").val(newRowId);
	    // var clone = $(".withoutPricing-class:last").clone();
	   var Invoiceval= clone.find("#DCInvoice"+rowId).val();
	   var DCNumberval= clone.find("#DCNumber"+rowId).val();
	   clone.attr("uda-rowId", rowId+1);
	   clone.find("#DCNumber"+rowId).removeAttr("id").removeAttr("name").attr("id","DCNumber"+newRowId).attr("name","DCNumber"+newRowId).attr("data-Number",DCNumberval);
	   clone.find("#DCInvoice"+rowId).removeAttr("id").removeAttr("name").attr("id","DCInvoice"+newRowId).attr("name","DCInvoice"+newRowId).attr("data-Invoice",Invoiceval);
	   clone.find("#DCDate"+rowId).removeAttr("id").removeAttr("name").attr("id","DCDate"+newRowId).attr("name","DCDate"+newRowId);
	   clone.appendTo( "#parent_witghoutpricing" ).after('</>');
	  //  $("#parent_witghoutpricing").clone().attr('#parent_witghoutpricing', '#parent_witghoutpricing'+ rowId++).insertAfter("#parent_witghoutpricing");
	   $(this).css("visibility", "hidden");
	  
	 });

	//opening setteled products in modal popup
	 function showPopUpForSubmit(){
		var tableLength=$(".finalSettledRow").length;
		if(tableLength==0){
			alert("No products are selected for settelement.")			
			return false;
		}		
		$("#tblnotification3").show();
		$("#saveBtnId").show();
		 $("#showPopUp").attr("data-target", "#modal-1");
		 $("#showPopUp").attr("data-toggle", "modal");
		 $(".container3").fadeIn(1000);
	 }	
	
	//deleting final settled table row	
	 function deleteFinalSettled(current, id){
		debugger;
		var canIDelete=window.confirm("Do you want to delete?");
			if(canIDelete==false){
				return false;
			}
			
			var rowsCount=$(".finalSettledRow").length;
			if(rowsCount==1){
				/* alert("This row can not be deleted.");
				return false; */
				$("#tblnotification3").hide();
				$("#saveBtnId").hide();
				$("#showPopUp").removeAttr("data-target");
				$("#showPopUp").removeAttr("data-toggle");
				$("#modal-1").modal("hide");
			}
			//var num=$(this).attr("id").split("finalTableRow")[0];
			$("#finalTableRow"+id).remove();
			$("#settleTableRow"+id).remove();
			//updating cart number
			var noOfSettled=$(".finalSettledRow").length;
			$("#noOfSettled").text(noOfSettled);
			var cartLength=$("#noOfSettled").text();
			if(cartLength!=0){
				$("#sendToPurBtn").attr("disabled", "disabled");
				$("#cancelIndentBtn").attr("disabled", "disabled");
			}else{
				$("#sendToPurBtn").removeAttr("disabled");
				$("#cancelIndentBtn").removeAttr("disabled");
			}
	}
	
	//deleting lower setteled table row
	function deleteSettleTableRow(id){
		debugger;
		var canIDelete=window.confirm("Do you want to delete?");
		if(canIDelete==false){
			return false;
		}
		var noOfSettled=$("#noOfSettled").text();
		var rowsLength=$(".deletebtnaction").length;
		if((rowsLength-noOfSettled)<2){
			$("#tblnotification1").hide();
			$("#add").hide();	
		}else{
		
		}
		$("#settleTableRow"+id).remove();
		var cartLength=$("#noOfSettled").text();
		if(cartLength!=0){
			$("#sendToPurBtn").attr("disabled", "disabled");
			$("#cancelIndentBtn").attr("disabled", "disabled");
		}else{
			$("#sendToPurBtn").removeAttr("disabled");
			$("#cancelIndentBtn").removeAttr("disabled");
		}
	}
	
	//validating settled table row
	function validateSettleTable(){
		var error=true;
		$(".settleTableRow").each(function(){
			var id=$(this).attr("id").split("settleTableRow")[1];
			var quantity=$("#Aquantity"+id).val();
			var siteName=$("#site_id"+id).val();
			if(quantity.trim()==""){
				alert("Please enter quantity.");
				$("#Aquantity"+id).focus();
				return error=false;
			}
			if(siteName==""){
				alert("Please select site name.");
				$("#site_id"+id).focus();
				return error=false;
			}
		});
		return error;
	}
	
	//validating lower settled table 
	function lowerSettTableSiteChange(eleid){
		var currentSiteName=$("#site_id"+eleid).val().split("-")[1];
		var SiteQty=$("#site_id"+eleid).val().split("-")[0];
		var currentChildProductId=$("#lowchildProductId"+eleid).val();
		var currentEnteredQty=parseFloat($("#Aquantity"+eleid).val());
		var requiredQuantity=parseFloat($("#quantity"+eleid).text());
		var compareQty=0;
		//entering more than required quantity
		if(requiredQuantity<currentEnteredQty){
			alert("Can not settle more than required quantity.")
			$("#Aquantity"+eleid).val("");
			$("#Aquantity"+eleid).focus();
			return false;
		}
		
		var error=true;
		var enteredTotalQtyforChild=0;
		//checking duplicate sitenames and GETTING TOTAL Quantity
		$(".settleTableRow").each(function(){
			var id=$(this).attr("id").split("settleTableRow")[1];
			var lowerSiteName=$("#site_id"+id).val().split("-")[1];
			var lowerChildId=$("#lowchildProductId"+id).val();
			//getting child product entered total quantity
			if(lowerChildId==currentChildProductId){
				enteredTotalQtyforChild+=parseFloat($("#Aquantity"+id).val()==""?0:$("#Aquantity"+id).val());
			}	
			//checking duplicate site name
			if(eleid!=id && $("#site_id"+id).val()!==""){	
				if(lowerChildId==currentChildProductId && lowerSiteName==currentSiteName){
					alert("Already the child product is issuing for the selected site.");
					$("#site_id"+eleid).val("");
					$("#site_id"+eleid).focus();
					error=false;
					return false;
				}				
			}			
		});
		//validating with site Quantity with entered quantity
		if(parseFloat(SiteQty)<currentEnteredQty && error==true){
			alert("The selected site does not have required quantity.");
			$("#Aquantity"+eleid).val("");
			$("#Aquantity"+eleid).focus(); 
			/* $("#site_id"+eleid).val("");
			$("#site_id"+eleid).focus(); */
			return false;
		}
		//validating with required quantity with entered total child total quantity
		if(requiredQuantity<enteredTotalQtyforChild && error==true){
			alert("The selected site does not have required quantity.")
			$("#Aquantity"+eleid).val("");
			$("#Aquantity"+eleid).focus();
			return false;
		}
		
	}
	//checking number number 
		function isNumberCheck(el, evt) {
		    var charCode = (evt.which) ? evt.which : event.keyCode;
		    var num=el.value;
		    var number = el.value.split('.');
		    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
		        return false;
		    }
		    //just one dot
		    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {
		         return false;
		    }
		    //get the carat position
		    var caratPos = getSelectionStart(el);
		    var dotPos = el.value.indexOf(".");
		    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
		        return false;
		    }
		    return true;
		}
		function getSelectionStart(o) {
			if (o.createTextRange) {
				var r = document.selection.createRange().duplicate();
				r.moveEnd('character', o.value.length);
				if (r.text == '') return o.value.length;
				return o.value.lastIndexOf(r.text);
			} else return o.selectionStart;
		}	
		
		 //this code for to active the side menu 
		var referrer="${urlForActivateSubModule}";
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