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
<script src="js/createPO.js" type="text/javascript"></script>
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
		<link href="css/style.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>

<title>Central Indent</title>

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
            //alert(this.element[0].name);
            //alert(ui.item.option.value);
            //alert(ui.item);
            
            //alert(this.input.id);
            //var id = this.input.text();
            //alert(this.input);
            
            //var count = 1;
            //var isProdSel = false;
            
            var prodId = "";
            var prodName = "";
            
            prodId = ui.item.option.value;
            prodName = ui.item.value;
            
            var ele = this.element[0].name;
            //alert(ele);
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            //alert("After removing numbers = "+str1);
            
	        var productColumn =  "<%= product %>";
		    productColumn = formatColumns(productColumn);
		    //alert(productColumn);
		  	 
		  	var subProductColumn =  "<%= subProduct %>";
		  	subProductColumn = formatColumns(subProductColumn);
		 	//alert(subProductColumn);
		     
		 	var childProductColumn =  "<%= childProduct %>";
		 	childProductColumn = formatColumns(childProductColumn);
			//alert(childProductColumn);
			
			var rowNum = ele.match(/\d+/g);
			//alert(rowNum);
			             
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
//var delOne;
 


//Validation started 

	
function createPO() {
	
	//document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	//var valStatus = appendRow();
	
	/*if(valStatus == false) {
    	return;
	}*/
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}	
	document.getElementById("CreateCentralIndentFormId").action = "createPODetails.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
function printIndent() {
	
	
	document.getElementById("CreateCentralIndentFormId").action = "printIndent.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
//Validation End
</script>
</head>

<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
		.description {
    display:none;
    }
    .popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* The actual popup */
.popup .popuptext {
	visibility: hidden;
	width: 160px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 8px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@
-webkit-keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}

}
@
keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
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
							<li class="breadcrumb-item active">Create PO</li>
						</ol>
					</div>
					
					
					<div>
 <div align="center">
		<form class="form-horizontal" name="myForm" id="CreateCentralIndentFormId" action="sendenquiry.spring" method="POST" style="padding:15px;">
	 	   <div class="form-group" style="margin-top: 23px; margin-bottom: 66px;" style="display:none;">
			<c:forEach items="${IndentDtls}" var="element">
			<label class="control-label col-sm-2">Indent Number </label>
			<div class="col-sm-3" >
			<input name="indentNumber" id="indentNumberId"  readonly="true" class="form-control" value="${element.indentNumber}"}/>
				<%-- <form:input path="ReqId" id="ReqIdId"  readonly="true" class="form-control"/> --%>
			</div>
			<label class="control-label col-sm-2">Site Name </label>
			<div class="col-sm-3" >
				<input name="siteName" id="siteNameId"  readonly="true" class="form-control" value="${element.siteName}"}/>
				
				<input type="hidden" name="siteId" id="siteIdId"   class="form-control" value="${element.siteId}"}/>
			</div>
			</c:forEach>
		   </div>
		 
			<%-- <label class="control-label col-sm-2">ReqSite Name </label>
			<div class="col-sm-3" >
			<input name="indentNumber" id="indentNumberId"  readonly="true" class="form-control" value="${reqSiteName}"}/>
				<form:input path="ReqId" id="ReqIdId"  readonly="true" class="form-control"/>
			</div> --%>
		   
		   
		   
		   
	  		<input type="hidden" name="numberOfRows"   value="${requestScope['IndentDetails'].size()}" />
<!-- *************** Table 01 grid***************** -->
				<div class="container1">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="background-color: #e0dada;">
			<tr>
				<th id="checkedbox"><input type="checkbox" id="selectall" \/>&nbsp;Select All</th>
				<th>Product Name</th>
				<th>SubProduct Name</th>
				<th>ChildProduct Name</th>
				<th>Measurement Name</th>
				<th>Request Quantity</th>
				<th>Pending Quantity</th>
				<th>PO can generate</th>
			</tr>
						</thead>
						<tbody>
						
						 
						
				<c:forEach items="${IndentDetails}" var="element"> 
				<div style="display: none;">
				
				<input type="hidden" name="productId${element.strSerialNumber}" value="${element.productId1}" />
				<input type="hidden" name="subProductId${element.strSerialNumber}" value="${element.subProductId1}" />
				<input type="hidden" name="childProductId${element.strSerialNumber}" value="${element.childProductId1}" />
				<input type="hidden" name="unitsOfMeasurementId${element.strSerialNumber}" value="${element.unitsOfMeasurementId1}" />
				<input type="hidden" name="product${element.strSerialNumber}" value="${element.product1}" />
				<input type="hidden" name="subProduct${element.strSerialNumber}" value="${element.subProduct1}" />
				<input type="hidden" name="childProduct${element.strSerialNumber}" value="${element.childProduct1}" />
				<input type="hidden" name="unitsOfMeasurement${element.strSerialNumber}" value="${element.unitsOfMeasurement1}" />
				<input type="hidden" name="strRequestQuantity${element.strSerialNumber}" value="${element.strRequestQuantity}" />
				<input type="hidden" name="requiredQuantity${element.strSerialNumber}" value="${element.requiredQuantity1}" />
				<input type="hidden" name="pendingQuantity${element.strSerialNumber}" value="${element.pendingQuantity}" />
				<input type="hidden" name="purchaseDepartmentIndentProcessSeqId${element.strSerialNumber}" value="${element.purchaseDepartmentIndentProcessSeqId}" />
				<input type="hidden" name="poIntiatedQuantity${element.strSerialNumber}" value="${element.poIntiatedQuantity}" />
				
				</div>
		   		<tr>
					<td><input type="checkbox"   class="case" name="checkboxname${element.strSerialNumber}" value="checked"></input></td>				
					<td class="tiptext"  class="case">${element.product1}</td>
					<td class="case">${element.subProduct1}</td>
					<td class="case">${element.childProduct1}</td>
					<td class="case">${element.unitsOfMeasurement1}</td>
					<td class="case">${element.strRequestQuantity}</td>
					<td class="case">${element.pendingQuantity}</td>	
					<td class="case">${element.requiredQuantity1}</td>		
			<!-- 	<td>100</td>	
					<td id="sttle-id1"><a href="" style="color: blue" >SETTEL</a></td>	 -->
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>

				<div class="col-sm-3 pt-10">
		<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>
					
					</div>    
				<div class="col-sm-2 pt-10"> 
					<input type="button" class="btn btn-warning"value="Create PO" id="saveBtnId" onclick="createPO()">
				</div>
				<div class="col-sm-2 pt-10"> 
					<input type="button" class="btn btn-warning"value="Print Indent" id="saveBtnId" onclick="printIndent()">
				</div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
		</form>
				<form class="form-horizontal" name="myForm" action="CreatePO.spring" style="padding:15px;" style="display:none;">
		

	  		
<!-- *************** Table 01 grid***************** -->

			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
		</form>
	</div>
	
	</div>
	<!-- /page content -->   

		
</div>
</div>
</div>
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<script src="js/custom.js"></script>
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
			
			$("#sttle-id1").click(function(){
				var tr    = $(this).closest('tr');
				var clone = tr.clone().insertAfter("#tblnotification" ).after('</>');
				console.log(clone);
				});
			$(function(){

				 // add multiple select / deselect functionality
				 $("#selectall").click(function () {
				    $('.case').attr('checked', this.checked);
				 });

				 // if all checkbox are selected, check the selectall checkbox
				 // and viceversa
				 $(".case").click(function(){

				  if($(".case").length == $(".case:checked").length) {
				   $("#selectall").attr("checked", "checked");
				  } else {
				   $("#selectall").removeAttr("checked");
				  }

				 });
				});
			/* $('#saveBtnId1').click(function(){
			      window.open('CreatePO.spring');
			   });*/

		</script> 
</body>
</html>