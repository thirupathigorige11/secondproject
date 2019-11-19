 <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
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
                loadScopeOfWorkAndAmount(WOWorkDescId, rowNum);
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
	  /* $("#workOrderDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:new Date(),
		//maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
	  }); */
  });
  
</script>

<script type="text/javascript">

</script>
<style>
/* table border color changes */
.txt-border {
	border-bottom: 1px solid #000 !important;
	border-top: 0px !important;
	border-left: 0px !important;
	border-right: 0px !important;
	border-radius: 0px;
}

.table-bordered>tbody>tr>td, .table-bordered>tbody>tr>th,
	.table-bordered>tfoot>tr>td, .table-bordered>tfoot>tr>th,
	.table-bordered>thead>tr>td, .table-bordered>thead>tr>th {
	border: 1px solid #000;
}

.table-bordered {
	border: 1px solid #000;
}

.table-bordered thead {
	background-color: #ccc;
}
/*table border color changes */
.form-control {
	border: 1px solid #000;
}

//
AC
 input[type=checkbox] {
	transform: scale(1.5);
}

/* css for iframe modal popup */
.pdf-cls {
	position: relative;
	width: 100%;
	margin: auto;
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
	width: 100%;
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

.modal-lg-width {
	width: 95%;
}

.btn-fullwidth:hover {
	background-color: transparent;
	border-color: transparent;
	color: transparent;
	height: 200px;
	width: 100%;
	margin-top: 110px;
}

.btn-fullwidth {
	background-color: transparent;
	border-color: transparent;
	color: transparent;
	height: 200px;
	width: 100%;
	margin-top: 110px;
}

.btn-fullwidth:active:focus, .btn-fullwidth:active:hover {
	color: transparent;
	background-color: transparent;
	border-color: transparent;
}

.btn-fullwidth:active {
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
.spinnercls {
	display: inline;
	height: 50px;
	position: absolute;
	right: 0px;
	top: 1px;
	display: none;
}

/* end spinner stles */

/* for action coloum */
#floortable thead tr th, #floortable tbody tr td {
	min-width: 120px !important;
}

#floortable thead tr th:first-child, #floortable tbody tr td:first-child
	{
	min-width: 20px !important;
	text-align: center;
	width: 50px;
}

.tblprodindissu tbody tr td:last-child, .tblprodindissu tbody tr th:last-child
	{
	min-width: 147px;
}

/* end for action coloum */
/*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:56px !important;min-width: 20px;text-align: center}
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
				window.location.assign("reviseWorkOrder.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>


<script>

//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
     var vfx = fldLength;
     
    tableColumnName=tableColumnName.trim();
     var snoColumn =  "<%= serialNumber %>";
     snoColumn = formatColumns(snoColumn);
     
     var WO_MajorHead =  "<%= WO_MajorHead %>";
     WO_MajorHead = formatColumns(WO_MajorHead);
  	 
  	 var WO_MinorHead =  "<%= WO_MinorHead %>";
  	WO_MinorHead = formatColumns(WO_MinorHead);
     
 	 var WO_Desc =  "<%= WO_Desc %>";
 	 WO_Desc = formatColumns(WO_Desc);
	 
	 var scope_Of_work =  "<%= scope_Of_work %>";
	 scope_Of_work = formatColumns(scope_Of_work);
	 
	 var measurement =  "<%= measurement %>";
	 measurement = formatColumns(measurement);
	 
	 var productAvailabilityColumn =  "<%= productAvailability %>";
	 productAvailabilityColumn = formatColumns(productAvailabilityColumn);
	 
	 var quantityColumn =  "<%= quantity %>"; 
	 quantityColumn = formatColumns(quantityColumn);
	 
	 var AcceptedRate =  "<%= AcceptedRate %>";
	 AcceptedRate = formatColumns(AcceptedRate);
	 
	 var TotalAmount =  "<%= TotalAmount %>";
	 TotalAmount = formatColumns(TotalAmount);
	 
	  var note =  "<%= note %>";
	  note = formatColumns(note);
	 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);
     
     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        cell.appendChild(snoDiv);
    	var div = document.createElement("input");
	    div.setAttribute("type", "hidden");
	    div.setAttribute("name", "dispplayedRows");
	    div.setAttribute("id", "dispplayedRows");
	    div.setAttribute("value", vfx);
	    cell.appendChild(div);		
	    var div1 = document.createElement("input");
	    div1.setAttribute("type", "hidden");
	    div1.setAttribute("name", "isNewRowAdded"+vfx);
	    div1.setAttribute("id", "isNewRowAdded"+vfx);
	    div1.setAttribute("value", "true");
	    cell.appendChild(div1);		
    }
    else {	
    	if(tableColumnName == WO_MajorHead) {
    		var dynamicSelectBoxId = "combobox"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	//alert(temp);
    	    div.setAttribute("onkeydown", "appendRow()");    	    	
    	    }    	        	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Select one...";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);    	    
    	    var option = "";
    		<% 
    			Map<String, String> products = (Map<String, String>)request.getAttribute("workMajorHead");
    			for(Map.Entry<String, String> prods : products.entrySet()) {
				
    				String prodName=prods.getValue().replace("\"","#");
    				String prodName1=prods.getValue().replace("\"","'");
    				String val = prods.getKey()+"$"+prodName;
			%>
				option = document.createElement("option");
	    	    option.text = "<%= prodName1 %>";
	    	    option.value = "<%= val %>";
	    	    div.appendChild(option);
    		<% 
				} 
			%>
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}
    	else if(tableColumnName == WO_MinorHead) {
    		
    		var dynamicSelectBoxId = "comboboxsubProd"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	div.setAttribute("onkeydown", "appendRow()");
    	    }    	    
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});    	    
    	}
    	else if(tableColumnName == WO_Desc) {
    		var dynamicSelectBoxId = "comboboxsubSubProd"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	div.setAttribute("onkeydown", "appendRow()");
    	    }
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}    
    	
    	else if(tableColumnName == scope_Of_work) {
    		
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "scopeOfWork"+vfx);
		    div.setAttribute("id", "scopeOfWork"+vfx);
		    div.setAttribute("onfocus", "myscopefocus("+vfx+")");		
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);		
		    var div1 = document.createElement("div");		
		    div1.setAttribute("class", 'modalpopup'+vfx);
		    div1.setAttribute("id", 'modalpopup'+vfx);
		    cell.appendChild(div1);	
   		}
    	
    	else if(tableColumnName == measurement) {
    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", "UnitsOfMeasurement"+vfx);
    	    div.setAttribute("id", "UOMId"+vfx);
    		  div.setAttribute("onchange", "return loadWOWorkArea(this.value,"+vfx+");");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	}   	
    	else if(tableColumnName == quantityColumn) {
    		var typeOfWork="${WorkOrderBean.typeOfWork}";
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    if(typeOfWork=="PIECEWORK"){
		    	div.setAttribute("readonly", true);
		    }
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("onkeypress", "return calCulateTotalAmout('qty',this.value,"+vfx+");");
	
		    div.setAttribute("class", "form-control copyPasteRestricted");
		    cell.appendChild(div);
 
		    var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderArea'+vfx);
			 cell.append(divNew);
			 
			 var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderWorkArea'+vfx);
			 cell.append(divNew);
   		} else if(tableColumnName == AcceptedRate) {
    		 var typeOfWork="${WorkOrderBean.typeOfWork}";
 		    
 			//if(typeOfWork=="NMR"){ 
	    		var div = document.createElement("input");
			    div.setAttribute("type", "text");
			    div.setAttribute("name", AcceptedRate+vfx);
			    div.setAttribute("id", AcceptedRate+vfx);
			    div.setAttribute("onkeypress","return isNumberCheck(this, event)");
			    div.setAttribute("onkeyup", "calCulateTotalAmout('a_rate',this.value,'"+vfx+"')");
			    div.setAttribute("class", "form-control copyPasteRestricted");
			    div.setAttribute("autocomplete", "off");
			    cell.appendChild(div);
 			//}
			    $(".copyPasteRestricted").bind('paste', function (e) {
					e.preventDefault();
				});
   		}
    	
    	
    	else if(tableColumnName == TotalAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == note) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "comments"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == actionsColumn) {
	    	
	    	var div2 = document.createElement("button");
		    div2.setAttribute("type", "button");
		    div2.setAttribute("name", "addNewItemBtn");
		    div2.setAttribute("id", "addNewItemBtnId"+vfx);
		    div2.setAttribute("value", "Add New Item");
		    div2.setAttribute("onclick", "appendRow()");
		    div2.setAttribute("class", "btnaction");
		    cell.appendChild(div2);
		    
		    var btn2 = document.createElement("i");
		    btn2.setAttribute("class", "fa fa-plus");
		    div2.appendChild(btn2);
		    
		    cell.append(" ");
		    
	    	var div1 = document.createElement("button");
		    div1.setAttribute("type", "button");
		    div1.setAttribute("name", "addDeleteItemBtn");
		    div1.setAttribute("id", "addDeleteItemBtnId"+vfx);
		    div1.setAttribute("value", "Delete Item");
		    div1.setAttribute("onclick", "deleteRow(this, "+vfx+")");
		    div1.setAttribute("class", "btnaction");
		    cell.appendChild(div1);
		    
		    var btn = document.createElement("i");
		    btn.setAttribute("class", "fa fa-trash");
		    div1.appendChild(btn);			    
	    }   	
    }
}



</script>

</head>

<body class="nav-md">
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
				</div>
			</div>
			<jsp:include page="./../TopMenu.jsp" />

			<!-- page content -->
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Revise NMR Work Order</li>
					</ol>
				</div>
 				<!-- loader -->
			     <div class="overlay_ims" ></div>
				 <div class="loader-ims" id="loaderId"> <!--  -->
					<div class="lds-ims">
						<div></div><div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingimsMessage">Loading...</div>
				</div>

<div class="col-md-12">
	<div align="center">
						
			<c:if test="${isThisCorrectRevisedWorkOrder eq false }">
				<h3><strong>You can not revise this Work Order because you already revised this Work</strong></h3>
							<!--  Order please check the revised Work Order  -->
			</c:if>
<form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal" >
		<div class="col-md-12 border-background-workorder">
				<c:set scope="session" var="deletedWorkOrderDetailsList" value="${deletedWorkOrderDetailsList}"></c:set>
				<c:set scope="session" value="${listTermsAndCondition}" var="listTermsAndCondition"></c:set>
				<c:set scope="session" value="${WorkOrderLevelPurpose}" var="WorkOrderLevelPurpose"></c:set>
				<c:set scope="session" value="${workOrderCreationList}" var="workOrderCreationList"></c:set>
				<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
				<span style="color: red;">${noStock}</span><br />
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-6"><%=WorkOrderNo%></label>
						<div class="col-md-6">
							 <form:input path="siteWiseWONO" id="workOrderId" name="workOrderId" class="form-control" readonly="true" title="${WorkOrderBean.siteWiseWONO}" />
						</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
								<label class="control-label col-md-6">Revise Work Order NO</label>
							<div class="col-md-6">
								<input type="text" value="${strWorkOrderNumber1}" id="strWorkOrderNumber1" name="reviseWorkOrderNumber1" class="form-control" title="${strWorkOrderNumber1}" readonly="true">
								<input type="hidden" value="${isThisCorrectRevisedWorkOrder }" id="isThisCorrectRevisedWorkOrder" name="isThisCorrectRevisedWorkOrder"> 
								<input type="hidden" name="revisionNumber" id="revisionNumber" value="${revisionNumber }">
							</div>
					   </div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-6"><%=WorkOrderDate%><%=colon%> </label>
							<div class="col-md-6">
								<form:input path="workOrderDate" id="workOrderDate" class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
							<div class="form-group">
							<label class="control-label col-md-6"><%=WorkOrderName%></label>
								<div class="col-md-6">
									<form:input path="workOrderName" id="workOrderName" name="workOrderName" class="form-control" readonly="true" title="${WorkOrderBean.workOrderName}" />
								</div>
							</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
								<label class="control-label col-md-6"><%=vendorName%> <%=colon%> </label>
							<div class="col-md-6">
								<form:input path="contractorName" id="contractorName" onkeyup="populateContractor(this);" readonly="true" autocomplete="off" class="form-control" title="${WorkOrderBean.contractorName}" />
								<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO" />
								<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId" />
								<form:input path="approverEmpId" type="hidden"	name="approverEmpId" id="approverEmpId" />
								<form:input path="approverEmpMail" type="hidden" name="approverEmpMail" id="approverEmpMail"/>
								<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo" />
								<form:input path="workorderFrom" type="hidden"	name="workorderFrom" id="workorderFrom" />
								<form:input path="siteId" id="site_id" type="hidden" />
								<form:input path="siteName" type="hidden" />
								<input type="hidden" name="operType" id="operType"	value="${operType}">										<c:set value="reviseWorkOrder" var="OperationType"></c:set>
								<form:input path="isUpdateWOPage" type="hidden" name="isUpdateWOPage" value="${isUpdateWOPage}" />
								<form:input path="typeOfWork" id="typeOfWork" type="hidden" />
								<form:input path="boqNo" id="boqNo" type="hidden" />
								<form:input path="revision" id="revision" type="hidden" />
								<form:input path="oldWorkOrderNo" id="oldWorkOrderNo" type="hidden" />
								<form:input path="versionNumber" id="versionNumber"	type="hidden" />
								<form:input path="totalWoAmount" type="hidden" id="totalWoAmount"/>
								
								<form:input path="isSaveOrUpdateOperation" id="isSaveOrUpdateOperation"  type="hidden" value=""/>
								<input type="hidden" name="TotalBOQAmount" id="TotalBOQAmount">
								<input type="hidden" name="TotalNMR_WO_initiatedAmount" id="TotalNMR_WO_initiatedAmount">
								<input  id="sumofTotalAmount"  type="hidden" name="TotalAmountOfWorkOrder" >
								<input id="typeOfWork" value="NMR" type="hidden">
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-6"><%=panCardNo%><%=colon%> </label>
							<div class="col-md-6">
							  <form:input path="contractorPanNo" name="contractorPanCardNo" id="contractorPanCardNo" class="form-control" readonly="true" title="${WorkOrderBean.contractorPanNo}" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
								<label class="control-label col-md-6"><%=Address%> <%=colon%></label>
							<div class="col-md-6">
								<form:input path="contractorAddress" name="contractorAddress" class="form-control" id="contractorAddress" readonly="true" title="${WorkOrderBean.contractorAddress}" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label col-md-6"><%=phoneNo%> <%=colon%> </label>
							<div class="col-md-6">
								<form:input path="contractorPhoneNo" name="contractorPhoneNo" class="form-control" id="contractorPhoneNo" readonly="true" title="${WorkOrderBean.contractorPhoneNo}" />
							</div>
						</div>
					</div>
		</div>
	<c:set value="NMR" var="TypeOfWork"></c:set>
	<div class="clearfix"></div>
		<div class="">
			<div class="table-responsive">
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
							<th><%=actions%></th>
						</tr>
				</thead>
				<tbody class="tbl-fixedheader-tbody">
				<c:set value="0" var="indexnumber"></c:set>
				  <c:forEach items="${workOrderCreationList}"	var="workOrderDetail">
					 <c:set value="${indexnumber+1}" var="indexnumber"></c:set>
						 <input name="isDelete${indexnumber}" type="hidden" id="isDelete${indexnumber}" readonly="true" value="z" class="form-control input-visibilty${indexnumber}" />

						<tr id="tr-class${indexnumber}"	class="reviseworkorderrowcls">
						 <td>
						  <div id="snoDivId1" class="serialNumcls">
							<c:out value="${indexnumber}"></c:out>
						  </div> 
						  <input type="hidden" name="nmrRowPaymentInitiatedDetils${indexnumber}" id="nmrRowPaymentInitiatedDetils${indexnumber}" value="${workOrderDetail.nmrPaymentDetails}">
						  <input name="dispplayedRows" id="dispplayedRows" type="hidden" value="${indexnumber}">
						  <input type="hidden" name="actualWorkOrderNo" value="${WorkOrderBean.siteWiseWONO}"> 
						  <input type="hidden" name="nextApprovelEmpId" value="${WorkOrderBean.approverEmpId}">
						  <input type="hidden" name="actualtempIssueId" id="actualtempIssueId" value="${workOrderDetail.QS_Temp_Issue_Id}"> 
						  <input type="hidden" name="QS_Temp_Issue_Dtls_Id${indexnumber}" id="QS_Temp_Issue_Dtls_Id${indexnumber}" value="${workOrderDetail.QS_Temp_Issue_Dtls_Id}">
						  <input type="hidden" name="actualWorkAreaID" value="${workOrderDetail.workAreaId}">
						  <input type="hidden" name="actualWORowId${indexnumber}" value="${workOrderDetail.woRowCode}">
						 </td>
						 <td data-toggle="tooltip" title="${workOrderDetail.WO_MajorHead1}">
							<input type="hidden" name="actualwoMajorHead${indexnumber}" value="${workOrderDetail.woMajorHead}$${workOrderDetail.WO_MajorHead1}">
							<select id="combobox${indexnumber}" name="WO_MajorHead${indexnumber}" class="btn-tooltip btn-visibilty${indexnumber}" title="${workOrderDetail.WO_MajorHead1}">
 								<option value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">${workOrderDetail.WO_MajorHead1}</option>
									 <c:forEach items="${workMajorHead}" var="item">
											 <c:if test="${!(workOrderDetail.WO_MajorHead1==item.value)}">
												 <option value="${item.key}${item.value}"> ${item.value }</option>
											 </c:if>
									 </c:forEach>
							 </select>
						</td>


					<td data-toggle="tooltip" title="${workOrderDetail.WO_MinorHead1}">
						<input type="hidden" name="actualWO_MinorHead${indexnumber}" value="${workOrderDetail.woMinorHeads}$${workOrderDetail.WO_MinorHead1}">
						<select name="WO_MinorHead${indexnumber}" id="comboboxsubProd${indexnumber}" class=" btn-tooltip form-control btn-visibilty${indexnumber}">
							<option value="${workOrderDetail.woMinorHeads}$<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>">${workOrderDetail.WO_MinorHead1}</option>
						</select>
					</td>
					<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_Desc1}' />">
						 <input type="hidden" name="actualWO_Desc${indexnumber}" id="actualWO_Desc${indexnumber}" value="${workOrderDetail.wODescription}$${workOrderDetail.WO_Desc1}">
						<select name="WO_Desc${indexnumber}" id="comboboxsubSubProd${indexnumber}" class="form-control btn-visibilty${indexnumber}">
							<option value="${workOrderDetail.wODescription}$<c:out value='${workOrderDetail.WO_Desc1}'></c:out>">${workOrderDetail.WO_Desc1}</option>
						</select>
					</td>
					<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
					<td>
						<input type="text" class="form-control" name="woManualDesc${indexnumber}" id="woManualDesc${indexnumber}" value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" onclick="showScopeOfWork(${indexnumber})" readonly="true" />
						<!-- modal popup for scope of work start-->
						<div id="modalForScopeWork${indexnumber}" 	class="modal fade" role="dialog">
																<div class="modal-dialog" style="width: 90%;">
																	<!-- Modal content-->
																	<div class="modal-content">
																		<div class="modal-header modalscopeheader">
																			<button type="button" class="close" data-dismiss="modal">&times;</button>
																			<h4 class="modal-title text-center">Scope Of Work</h4>
																		</div>
																		<c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}" />

																		<c:forEach var="scopeWork" items="${scopeOfWorkParts}">
																			<div class="modal-body" style="overflow: hidden;">
																				<div id="textboxDiv"></div>
																				<div class="col-md-12 mrg-btm">
																					<div class="form-group">
																						<div class="col-md-12">
																							<input type="text" class="form-control txt-border scopeofworkid${indexnumber}" value="<c:out value='${scopeWork}'></c:out>" name="ScopeOfWork${indexnumber}">
																						</div>
																						<!-- <div class="col-md-1"><button class="btn btn-success Addscope_txt"><i class="fa fa-plus"></i></button></div> -->
																					</div>
																				</div>
																				<input type="hidden" id="hiddenrownum">
																			</div>
																		</c:forEach>


																		<div class="modal-footer">
																			<div class="text-center center-block">
																				<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
																			</div>
																		</div>
																	</div>

																</div>
								</div> <!-- modal popup for scope of work end --></td>
							</c:if>
				<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.unitsOfMeasurement1}'/>">
					<input type="hidden" name="actualunitsOfMeasurement${indexnumber}" id="actualunitsOfMeasurement${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'/>">
					<%-- <input type="text" name="UnitsOfMeasurement${indexnumber}"  id="UnitsOfMeasurementId${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$${workOrderDetail.unitsOfMeasurement1}"> --%>
					<select name="UnitsOfMeasurement${indexnumber}" id="UOMId${indexnumber}" class="form-control btn-visibilty${indexnumber}" onchange="loadWOWorkArea(this.value,${indexnumber});">
						<option value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'/>">${workOrderDetail.unitsOfMeasurement1}</option>
					</select>
				</td>
		<td class="w-70" data-toggle="tooltip">
		 <input type="hidden" name="actualQuantity${indexnumber}"  id="actualQuantity${indexnumber}" value="${workOrderDetail.quantity}" class="addFractioPoints">
		 <input id="Quantity${indexnumber}" onblur="calCulateTotalAmout('qty',this.value,${indexnumber})"  onkeypress='return isNumberCheck(this, event)'	name="Quantity${indexnumber}" value="${workOrderDetail.quantity}"
														class="form-control input-visibilty${indexnumber} addFractioPoints copyPasteRestricted" /> 
		<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
			<span id="ClickHere${indexnumber}" style="">
			<a	href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber},'show')">Click Here</a><br /></span>
		</c:if> <img src="images/spinner.gif" class="spinnercls" id="spinner${indexnumber}"></img>
		 <a href="javascript:showDetailsFunction(${indexnumber})"	style="display: none;"	onclick="showDetailsFunction(${indexnumber})" data-toggle="modal" data-target="#myModal-showwo-showquantity" id="showQty${indexnumber}">View Details </a>		 
			<div id="appendActualWorkOrderArea${indexnumber}"></div>
			<div id="appendWorkOrderArea2${indexnumber}"></div>
			<div id="appendWorkOrderArea${indexnumber}"></div>
			<div id="appendWorkOrderWorkArea${indexnumber}"></div></td>
		<c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
			<td><input type="hidden" id="actualAcceptedRate${indexnumber}" name="actualAcceptedRate${indexnumber}"	value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}" class="addFractioPoints">
				<input id="AcceptedRate${indexnumber}" name="AcceptedRate${indexnumber}"  onkeypress='return isNumberCheck(this, event)'	onfocusout="calCulateTotalAmout('a_rate',this.value,${indexnumber})" readonly="true"	class="form-control commmntstooltip input-visibilty${indexnumber} addFractioPoints copyPasteRestricted"	value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}" />
			</td>
		</c:if>
		<td>
			<input type="hidden" id="actualTotalAmount${indexnumber}" name="actualTotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}" class="addFractioPoints"> 
			<input name="TotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}" id="TotalAmountId${indexnumber}" readonly="true" class="form-control addFractioPoints" /></td>
		<td>
			<input type="hidden" name="actualComments${indexnumber}" value="<c:out value='${workOrderDetail.comments1}'></c:out>">
														
			<input id="Comments${indexnumber}" value="" placeholder="<c:out value='${workOrderDetail.comments1}'></c:out>" title="<c:out value='${workOrderDetail.comments1}'></c:out>"
														name="comments${indexnumber}" readonly="true" class="form-control commmntstooltip input-visibilty${indexnumber}" />
			<input type="hidden" name="IsComments" value=""
														id="hiddenCommentsId"></td>
		<td>
			<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${indexnumber}" class="btnaction" onclick="removeReviseWORow('${indexnumber}')">
							<i class="fa fa-remove"></i>
			</button>
			<button type="button" name="editItemBtn" value="Edit Item" id="editItem${indexnumber}" class="btnaction" onclick="editRow('${indexnumber}')"> <i class="fa fa-pencil"></i>
			</button>
			<%--  <c:if test="${workOrderCreationList.size() eq indexnumber}"> --%>
				<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId${indexnumber}" onclick="appendRow(${indexnumber})" class="btnaction">
					<i class="fa fa-plus"></i>
				</button>
			<%-- </c:if> --%>
		</td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
 </div>
</div>

	<div class="col-md-12 Mrgtop20">
		<label class="control-label col-md-2">Note: </label>
			<div class="col-md-3">
					<input type="hidden" name="actualPurpose" value="${WorkOrderLevelPurpose}">
					<textarea name="Purpose" id="purpose" href="#" data-toggle="tooltip" title="${WorkOrderLevelPurpose}" placeholder="${WorkOrderLevelPurpose}" id="NoteId" class="form-control resize-vertical" autocomplete="off"></textarea>
			</div>
	</div>

				<!-- this is common file for showing images  -->
				<%@include file="ImgPdfCommonJsp.jsp"%>
				<!-- modal popup for approve page start -->

				<div id="myModal-showeorkorder" class="modal fade" role="dialog">
					<div class="modal-dialog">

						<!-- Modal content-->
						<div class="modal-content">
							<div class="modal-header header-modalworkOrder text-center">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
								<h4 class="modal-title">Terms & Conditions</h4>
							</div>
						<div class="modal-body body-modal-workorder">
							<div class="col-md-12 appen-div-workorder">
								<c:forEach items="${listTermsAndCondition }" var="TAC">
									<c:if test="${not empty TAC.TERMS_CONDITION_DESC.trim()}">
										<input type="hidden" name="actualterms_condition_id" value="${TAC.TERMS_CONDITION_ID}">
										<input type="hidden" value="${TAC.TERMS_CONDITION_DESC}" name="actualTC" id="terms${TAC.indexNumber}">
										<br>

									<div class="col-md-12 remove-filed">
											<div class="col-md-10">
												
													<input type="text" autocomplete="off" class="form-control workorder_modal_text" onkeyup="workordertermstitle(this)" title="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" value="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" name="termsAndCOnditions" id="terms${TAC.indexNumber}">
											</div>
										<div class="col-md-2 margin-close">
												<button type="button" class="btn-danger remove-button remove_field" id="newTC"> <i class="fa fa-remove "></i>
												</button>
										</div>
									</div>
								    </c:if>
								</c:forEach>
							</div>
							<div class="col-md-12">
								<div class="input_fields_wrap">
									<div class="col-md-12 padd-modal-workorder">
										<div class="form-group">
											<div class="col-md-10">
												<input type="text" class="form-control workorder_modal_text" name="termsAndCOnditions" id="workorder_modal_text1" />
											</div>
											<div class="col-md-2 margin-close">
												<button type="button" class="btn-success plus-button add_field_button"> <i class="fa fa-plus "></i>
												</button>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-12 margin-close">
										<input type="checkbox"><span> (Optional)If you want to add CC In emails.</span> 
										<input type="text" class="form-control control-text" id="email-popup-workorder" name="optionalCCmails" value="${optionalCCmails}">
								</div>
							<!-- 	<div class="col-md-12 margin-close">
										<input type="checkbox"><span> Subject</span> 
										<input type="text" class="form-control control-text" id="email-popup-workorder" placeholder="Please enter the subject">
								</div> -->
								</div>
							</div>
							<div class="modal-footer">
								<div class="col-md-12 text-center center-block">
										<button type="button" class="btn btn-warning" data-dismiss="modal"  onclick="reviseNMRWORecords('SaveClicked')">Submit</button>
								</div>
							</div>
					</div>

				</div>
			</div>

							<!-- Modal Popup for approve page -->
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
</form:form>
	<div class="pull-left">
						
	</div>
	<div class="col-md-12" style="margin-top: 30px;">
		<c:if test="${isThisCorrectRevisedWorkOrder ne false }">
			<button type="button" class="btn btn-warning" value="Draft Work Order" style="margin-bottom: 5px;" onclick="return openTermsAndConditionModal(this.value);" id="saveBtnId1">
				<i class="fa fa-floppy-o" aria-hidden="true"></i>&emsp;Draft Work Order
			</button>
			<input type="button" class="btn btn-warning" value="Revise" style="margin-bottom: 5px;" onclick="return openTermsAndConditionModal(this.value);" id="saveBtnId">
			<input type="button" name="closeBtn" id="closeBtn" style="margin-bottom: 5px;" onclick="return closeView()" class="btn btn-warning btn-custom-width" id="closeBtn" value="Close" />
		</c:if>
		<!-- <input type="button" class="btn btn-warning" value="Reject" id="saveBtnId" onclick="rejectFromUpdatePage('SaveClicked')"> -->
		<c:if test="${operType eq OperationType }">
			<!-- 	<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Payments" id="WorkOrderPayments" data-toggle="modal" data-target="#modal-workorder-payments">
			<input type="button" class="btn btn-warning" style="margin-bottom:5px;" value="WorkOrder Abstract" id="WorkOrderAbstract" data-toggle="modal" data-target="#modal-certificatepaymentra-click"> -->
		</c:if>
	</div>

    <div class="clearfix"></div>
	</div>
	<!-- /page content -->
	</div>
	</div>
	</div>
	<link href="js/inventory.css" rel="stylesheet" type="text/css" />
	<script src="js/custom.js"></script>
	<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
	<script>
		function closeView(){
			debugger;
			goBack() ;
		}
		function goBack() {
		    window.history.back();
		}
			$(document).ready(function() {	
				if(window.history.length==1){
					$("#closeBtn").hide();
				}
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				$(".copyPasteRestricted").bind('paste', function (e) {
					e.preventDefault();
				});
			});
	
			
		</script>
		<script>
		debugger;
		var dispplayedRows=$("#dispplayedRows").val();
		var typeOfWork="${WorkOrderBean.typeOfWork}";
		if(typeOfWork=="PIECEWORK")
		$("[name^=dispplayedRows]").each(function () {
		debugger;
       	 var rowNum=$(this).val();
    	 loadWOWorkArea("false",rowNum);	
       });
		
		function loadSubProds(prodId, rowNum) {
			debugger;
			
			var requesteddate=$("#workOrderId").val();
			if(requesteddate == "" || requesteddate == '' || requesteddate == null) {
				alert("Please Choose Requested Date .");
				document.getElementById("requesteddate").focus();
			}
			prodId = prodId.split("$")[0];
			var url = "workOrderSubProducts.spring?mainProductId="+prodId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
			var request = getAjaxObject();
			try {
				request.onreadystatechange = function() {
					if(request.readyState == 4 && request.status == 200) {
		              	$("#WO_MinorHead"+rowNum).val("");
						$("#WO_Desc"+rowNum).val("");
					    $("#UOMId"+rowNum).val("");
						$("#scopeOfWork"+rowNum).val("");
						var resp = request.responseText;
						resp = resp.trim();			

				    	var spltData = resp.split("|");
				    	available = new Array();
				    	for(var j=0; j<spltData.length; j++) {
				    		available[j] = spltData[j];
				    	}
						
				    	var subProdToSet = "comboboxsubProd"+rowNum;
						var selectBox = document.getElementById(subProdToSet);
					  
						//Removing previous options from select box - Start
					    if(document.getElementById(subProdToSet) != null && document.getElementById(subProdToSet).options.length > 0) {
					    	document.getElementById(subProdToSet).options.length = 0;
					    }
					    //Removing previous options from select box - End
					    
					    initOpt = document.createElement("option");
					    initOpt.text = "--Select--";
					    initOpt.value = "";
					    selectBox.appendChild(initOpt);
					    
					    var defaultOption;
					    var data;
					    
					    for(var i=0; i<available.length; i++) {
					    	defaultOption = document.createElement("option");
					    	data = available[i].split("_");
					    	if(data[0] != "" && data[0] != null && data[0] != '') {
					    		var prodIdAndName = data[0]+"$"+data[1];	    		
					    		defaultOption.text = data[1];
					    	    defaultOption.value = prodIdAndName;
					    	    selectBox.appendChild(defaultOption);
					    	}
					    }			
		            }
		        };		
				request.open("POST", url, false);
				request.send(null); 
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}
			function myscopefocus(id){
				 $("#hiddenrownum").val(id); 
				$("#modalForScopeWork"+id).modal();
			}

		function loadSubSubProducts(subProdId, rowNum) {
			debugger;
			subProdId = subProdId.split("$")[0];
			var url = "workOrderChildProducts.spring?subProductId="+subProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
			  
			var request = getAjaxObject();
			
			try {
				request.onreadystatechange = function() {
					
					if(request.readyState == 4 && request.status == 200) {
						
						$("#WO_MinorHead"+rowNum).val("");
						$("#WO_Desc"+rowNum).val("");
						$("#UOMId"+rowNum).val("");
						$("#scopeOfWork"+rowNum).val("");
						var resp = request.responseText;
						resp = resp.trim();			

				    	var spltData = resp.split("|");
				    	//alert(spltData);
				    	
				    	available = new Array();
				    	for(var j=0; j<spltData.length; j++) {
				    		available[j] = spltData[j];
				    	}
						
				    	var subSubProdToSelect = "comboboxsubSubProd"+rowNum;
				    	var selectBox = document.getElementById(subSubProdToSelect);
					    //Removing previous options from select box - Start
					    if(document.getElementById(subSubProdToSelect) != null && document.getElementById(subSubProdToSelect).options.length > 0) {
					    	document.getElementById(subSubProdToSelect).options.length = 0;
					    }
					    //Removing previous options from select box - End
					    
					    initOpt = document.createElement("option");
					    initOpt.text = "--Select--";
					    initOpt.value = "";
					    selectBox.appendChild(initOpt);
					    
					    var defaultOption;
					    var data;
					    
					    for(var i=0; i<available.length; i++) {
					    	defaultOption = document.createElement("option");
					    	data = available[i].split("_");
					    	if(data[0] != "" && data[0] != null && data[0] != '') {
					    		var prodIdAndName = data[0]+"$"+data[1];
					    		defaultOption.text = data[1];
					    	    defaultOption.value = prodIdAndName;
					    	    selectBox.appendChild(defaultOption);
					    	}
					    }
		            }
		        };
				request.open("POST", url, false);
				request.send(null); 
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}
		function loadUnits(childProdId, rowNum) {


			childProdId = childProdId.split("$")[0];
			debugger;
			
			var url = "listOfWOmesurment.spring?childProductId="+childProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}";;
			  
			var request = getAjaxObject();
			
			try {
				request.onreadystatechange = function() {
					
					if(request.readyState == 4 && request.status == 200) {
		    			$("#UOMId"+rowNum).val("");
						var resp = request.responseText;
						resp = resp.trim();
						var spltData = resp.split("|");
					  	available = new Array();
				    	for(var j=0; j<spltData.length; j++) {
				    		available[j] = spltData[j];
				    	}
						
				    	var unitsToSelect = "UOMId"+rowNum;
				    	var selectBox = document.getElementById(unitsToSelect);
					    
					    //Removing previous options from select box - Start
					    if(document.getElementById(unitsToSelect) != null && document.getElementById(unitsToSelect).options.length > 0) {
					    	document.getElementById(unitsToSelect).options.length = 0;
					    }
					    //Removing previous options from select box - End
					    
					    initOpt = document.createElement("option");
					    initOpt.text = "--Select--";
					    initOpt.value = "";
					    selectBox.appendChild(initOpt);
					    
					    var defaultOption;
					    var data;
					    
					    for(var i=0; i<available.length; i++) {
					    	defaultOption = document.createElement("option");
					    	data = available[i].split("_");
					    	if(data[0] != "" && data[0] != null && data[0] != '') {
					    		var unitsIdAndType = data[0]+"$"+data[1];
					    		defaultOption.text = data[1];
					    	    defaultOption.value = unitsIdAndType;
					    	    selectBox.appendChild(defaultOption);
					    	}
					    }            	
		            }
		        };	
				request.open("POST", url, false);
				request.send();  
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}

		setTimeout(function(){
			debugger;
			loadScopeOfWorkAndAmount('ds',1);
				
		}, 200);
		
		//this method is used for load the total amount of NMR BOQ and already initiated BOQ Amount, based on type of work  ,
		//this method is used for both NMR work order and PIECE WORK work order 
		function loadScopeOfWorkAndAmount(childProdId, rowNum){
			debugger;
			
			childProdId = $("#comboboxsubSubProd"+rowNum).val().split("$")[0];
			var mesumentId="";//$("#UOMId"+rowNum).split("$")[0];
			var strData="";
			var WO_MinorHead=$("#comboboxsubProd"+rowNum).val().split("$")[0];
			debugger;
			var siteId=$("#site_id").val();
			var url = "loadScopeOfWork_AmountAndQty.spring?WO_MinorHead="+WO_MinorHead+"&childProductId="+childProdId+"&site_id="+siteId+"&mesumentId="+mesumentId+"&typeOfWork=${WorkOrderBean.typeOfWork}&boqNo=${WorkOrderBean.boqNo}&isApproveOrRevisePage=true";
			  $.ajax({
			    url : url,
			    type : "get",
				contentType : "application/json",
				success : function(data) {
					var regExpr = /[^a-zA-Z0-9-. ]/g;
					debugger;
					var array=new Array();
					array=data.split("@@");
			
					data=data.split("@@")[0];
					var totalBOQAmount=array[3];
					var totalNMRWOInitiatedAmount=array[4];
					$("#TotalBOQAmount").val(totalBOQAmount);
					$("#TotalNMR_WO_initiatedAmount").val(totalNMRWOInitiatedAmount);
					
					 $(".addFractioPoints").each(function(){
						 var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						 $(this).val(currentvalue.toFixed(2));
					  });
				}
			});	
		}
				function appendtextbox(btn){/* 
					debugger;
					var textlength=$('.scopeofworkid'+btn).length;
					var defaultSOW=$("#defaultScopeOfWOrk"+btn).val();

					if(defaultSOW.length==0){
						
					alert("Please enter scope of work.");
						$("#defaultScopeOfWOrk"+btn).focus();
						return false;
					}
					//defaultSOW=defaultSOW.replace(/[\"\""]/g, '');
					var appendtextid=textlength+1;
					 $("#textboxDiv"+btn).append('<div class="col-md-12" id="newtextId"><div class="form-group" id="newtxtbox'+appendtextid+'"><div class="col-md-11"><input type="text" name="ScopeOfWork'+btn+'"   id="defaultScopeOfWOrk'+btn+appendtextid+'" class="form-control txt-border txt-height scopeofworkid1" value="'+defaultSOW+'" title="'+defaultSOW+'"></div><div class="col-md-1"><button class="btn btn-danger" type="button" onclick="deleteScopeOfWork('+appendtextid+')"><i class="fa fa-close"></i></button></div></div></div>');  
				    serialNumber=1;
				    $("#defaultScopeOfWOrk"+btn).val('');
				    $("#defaultScopeOfWOrk"+btn).removeAttr('title');
				    $("#defaultScopeOfWOrk"+btn).focus();
				    $("#defaultScopeOfWOrk"+btn+appendtextid).val(defaultSOW);
				 */}
		
				function submitScopeOfWork(rowNum){}
				function addDuplicateWorkAreaRow(workAreaId,rowNum,id){}
							
				function validateBBQ(id){}
				//method for validating work area
				function validateWorkArea(val,workAreaId){}
				function loadWOWorkArea(UOMId, row) {}
		
				
				
		var rowsIncre=1;
		//validating row data
		function validateMajorHeadTable(){
			var error=true;
			$(".reviseworkorderrowcls").each(function(){
				var id=$(this).attr("id").split("tr-class")[1];
		debugger;
				if($("#WO_MajorHead"+id).val()==''){
					alert("Please select Major Head");
					$("#WO_MajorHead"+id).focus();
					return error=false;;
				}
				if($("#WO_MinorHead"+id).val()==''){
					alert("Please select Minor Head");
					$("#WO_MinorHead"+id).focus();
					//$("#WO_Desc"+id).val('');
					//$("#UOMId"+id).val('');
					return error=false;;
				}
				if($("#WO_Desc"+id).val()==''){
					alert("Please select WO Description");
					$("#WO_Desc"+id).focus();
					//$("#UOMId"+id).val('');
					return error=false;;
				}
				if($("#UOMId"+id).val()==''){
					alert("Please select UOM");
					$("#UOMId"+id).focus();
					return error=false;;
				}
				
				if($("#Quantity"+id).val()==''||$("#Quantity"+id).val()=='0'||$("#Quantity"+id).val()=='0.00'){
					alert("Please enter quantity");
					$("#Quantity"+id).focus();
					return error=false;;
				}
				
				if($("#AcceptedRate"+id).val()==''||$("#AcceptedRate"+id).val()=='0'||$("#AcceptedRate"+id).val()=='0.00'){
					alert("Please enter accepted rate");
					$("#AcceptedRate"+id).focus();
					return error=false;
				}
				
			});
			return error;
		}
		
		function openTermsAndConditionModal(buttonValue){
			debugger;
			 var conName =$("#contractorName").val();
			 var WorkOrderDate =$("#workOrderDate").val();
			  if(buttonValue!=undefined)
				 	$("#isSaveOrUpdateOperation").val(buttonValue);
			  
			    if(conName == "" || conName == '' || conName == null) {
			        alert("Please select the contractor name.");
			        $("#contractorName").focus();
			        return false;
			    }
			    if(WorkOrderDate == "" || WorkOrderDate == '' || WorkOrderDate == null) {
			        alert("Please select the Work Order Date.");
			        $("#WorkOrderDate").focus();
			        return false;
			    }
			
			 var valStatus = validateMajorHeadTable();
		     
			    if(valStatus == false) {
			        return false;
			    }
			  
			    $("#saveBtnId").attr("data-toggle", "modal");
			    $("#saveBtnId").attr("data-target", "#myModal-showeorkorder");
			    $("#saveBtnId1").attr("data-toggle", "modal");
			    $("#saveBtnId1").attr("data-target", "#myModal-showeorkorder");

		}
		//calculating total amount of work order row
		//this method will work for only NMR Work Order
		function calCulateTotalAmout(type,acceptedRate,rownum){
			var typeOfWork="${WorkOrderBean.typeOfWork}";
			if(typeOfWork=="PIECEWORK"){
				return false;
			}
			
			if($("#UOMId"+rownum).val()==''||$("#UOMId"+rownum).val()==null){
				var valStatus = validateMajorHeadTable();
			    if(valStatus == false) {
			        return false;
			    }
				return false;
			}
			
			var previousQty=0;
			var PREVQTY=$("#nmrRowPaymentInitiatedDetils"+rownum).val();
			var previousAcceptedRate=0;
			var previousNMRBillAmount=0;
			debugger;
		 	try {
				var index=PREVQTY.search("@@");
				if(PREVQTY.length!=0){
					if(index>=0){
						prevAreaQuantity=PREVQTY.split("@@");
						for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
							let	array_element = prevAreaQuantity[ind].split("##");
							var noOfWorker=parseFloat(array_element[0]);
							var noOfHrs=parseFloat(array_element[1]);
							var c=noOfWorker+noOfHrs;
							var tempPreviousQty = parseFloat(((noOfWorker* noOfHrs) / 8));
							var num =tempPreviousQty;
							var n = num.toFixed(2);
							previousAcceptedRate=array_element[2];
							previousQty=previousQty+(parseFloat(n));
							previousNMRBillAmount+=parseFloat(n)*array_element[2];
						}
					}else{
						prevAreaQuantity = PREVQTY.split("##");
						previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
						previousAcceptedRate=prevAreaQuantity[2];
						previousNMRBillAmount+=parseFloat(previousQty)*prevAreaQuantity[2];
					}
					
					previousAcceptedRate=parseFloat(previousAcceptedRate);
				
				//validation code
				var quantity=parseFloat($("#Quantity"+rownum).val());
				var accpeptedRate=parseFloat($("#AcceptedRate"+rownum).val());
				debugger;
				if(previousQty!=0){			
				if(type=="a_rate"){
				  var actualAcceptedRate= $("#actualAcceptedRate"+rownum).val();
					  if(accpeptedRate!=previousAcceptedRate&&actualAcceptedRate!=undefined){
							alert("You can not change the rate, payment already initiated for this quantity.");
							$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
							$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
							$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
							$("#AcceptedRate"+rownum).prop("readonly",true);
							return false;
					}		
				  }
				}
				if(previousQty>quantity){
					alert("You can not reduce the qunatity, payment already initiated for some quantity.");
					$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
					$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
					$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
					return false;
				}
				if(accpeptedRate!=previousAcceptedRate){
					if(previousQty==quantity){
						alert("Unable to modify the rate because payment had been initiated.");
						$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
						$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
						$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
						return false;
					}
				}		
				if(previousQty!=0){
					if(previousAcceptedRate>accpeptedRate){
						alert("You can not reduce the accepted rate, payment already initiated for some quantity.");
						$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
						$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
						$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
						return false;
					}
				}
			  }
			} catch (e) {
				console.log(e);
			}
			
		    if(type=="qty"){
		     	var rate=$("#AcceptedRate"+rownum).val();
				$("#TotalAmountId"+rownum).val((rate*acceptedRate).toFixed(2));
		    }else{
			  	var qty=$("#Quantity"+rownum).val();
				$("#TotalAmountId"+rownum).val((qty*acceptedRate).toFixed(2));
		    }
			
			var sumofTotalAmount=0;
			var sumofActualTotalAmount=0;
			
		   	$(".reviseworkorderrowcls").each(function(){
				debugger;
		   		var currentId=$(this).attr("id").split("tr-class")[1];	
		   		sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val()==""?0:$("#TotalAmountId"+currentId).val());
				sumofActualTotalAmount+=parseFloat($("#actualTotalAmount"+currentId).val()==undefined?0:$("#actualTotalAmount"+currentId).val());
				if($("#isDelete"+currentId).val()!="d"){
					
				}
			}); 
		   	
 
		   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
		  
			debugger;
			var totalBOQAmount=$("#TotalBOQAmount").val()==""?0:parseFloat($("#TotalBOQAmount").val());
			var totalNMRWOInitiatedAmount=$("#TotalNMR_WO_initiatedAmount").val()==""?0:parseFloat($("#TotalNMR_WO_initiatedAmount").val());
			var initiateAmount=totalBOQAmount-totalNMRWOInitiatedAmount;
			if(sumofTotalAmount>initiateAmount){
				if((totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount))>=parseFloat(sumofTotalAmount)){
					return true;
				}
				alert("Total NMR BOQ Amount "+(totalBOQAmount)+" Initiated Amount is "+(totalNMRWOInitiatedAmount-sumofActualTotalAmount)+" you can initiate now = "+(totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount)));
				$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val()==undefined?0:$("#actualTotalAmount"+rownum).val());
				$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val()==undefined?0:$("#actualAcceptedRate"+rownum).val());
				$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val()==undefined?0:$("#actualQuantity"+rownum).val());
				return error=false;
			}
		}
		function isNum(value) {
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value)
		}

		</script>
		<script>
	$(document).ready(function() {
	    var max_fields      = 50; //maximum input boxes allowed
	    var wrapper         = $(".appen-div-workorder"); //Fields wrapper
	    var add_button      = $(".add_field_button"); //Add button ID
	    
	    var termsAndConditionTxtBox = 1; //initlal text box count
	    $('.add_field_button').on("click",function(e){ //on add input button click
	        e.preventDefault();
	    var newTc=$("#workorder_modal_text1").val();
	    if(newTc.length==0){
	    	alert("Please enter terms and condition");
	    	return false;
	    
	    }
	        if(termsAndConditionTxtBox < max_fields){ //max input box allowed
	        	termsAndConditionTxtBox++; //text box increment
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-10"><input type="text" class="form-control workorder_modal_text" id="newTC'+termsAndConditionTxtBox+'" value="'+newTc+'" name="termsAndCOnditions"/></div><div class="col-md-2 margin-close"><button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#newTC"+termsAndConditionTxtBox).val(newTc);
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent().parent(".remove-filed").remove(); termsAndConditionTxtBox--;
	    })
	});
	
 $(document).ready(function() {
		 var rows = document.getElementById("indentIssueTableId").getElementsByTagName("tr").length;
		for(var i=0;i<rows-1;i++){
			//for combobox
			$("#combobox"+(i+1)).combobox();    
			$( "#comboboxsubProd"+(i+1)).combobox();
			$("#comboboxsubSubProd"+(i+1)).combobox(); 
			$("#UnitsOfMeasurementId"+(i+1)).combobox();
			$('.btn-visibilty'+(i+1)).closest('td').find('.custom-combobox-toggle').addClass('hide');
			//for readonly mode
			$('#WO_MajorHead'+(i+1)).attr('readonly', true);
			$('#WO_MajorHead'+(i+1)).addClass('form-control');
			$('#WO_MinorHead'+(i+1)).attr('readonly', true);
			$('#WO_MinorHead'+(i+1)).addClass('form-control');
			$('#WO_Desc'+(i+1)).addClass('form-control');
			$('#WO_Desc'+(i+1)).attr('readonly', true);
			//$('#UnitsOfMeasurementId'+(i+1)).attr('disabled', true);
			/* $('#UnitsOfMeasurementId'+(i+1)).addClass('form-control');
			$('#UnitsOfMeasurementId'+(i+1)).attr('readonly', true); */
			$('#UnitsOfMeasurement'+(i+1)).addClass('form-control');
			$('#UnitsOfMeasurement'+(i+1)).attr('readonly', true);
			$('#Quantity'+(i+1)).attr('readonly', true);
			
		}
		 
		
	});
</script>
		<!-- model popup for pdf start  -->
		<%
			int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
			for (int i = 0; i < pdfcount; i++) {
				String pdfName = "pdf" + i;
				String PathdeletePdf = "PathdeletePdf" + i;
				log(pdfName);
		%>
		<c:set value="<%=pdfName%>" var="pdfBase64"></c:set>
		<c:set value="<%=PathdeletePdf%>" var="deletePdf">
		</c:set>
		<%
			if (request.getAttribute(pdfName) != null) {
		%>
		<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
			<div class="modal-dialog modal-lg-width">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title text-center">
							<strong>Full Width PDF<%=i + 1%></strong>
						</h4>
					</div>
					<div class="modal-body">
						<!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
						<!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
						<embed
							src="data:application/pdf;base64,${requestScope[pdfBase64]}"
							style="height: 500px; width: 100%;">
					</div>
					<div class="modal-footer">
						<p class="text-center">
							<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
							<button type="button" class="btn btn-danger" id="deletePdf"
								onclick="deletepdf(<%=i %>,'${requestScope[deletePdf]}')"
								data-dismiss="modal">Delete</button>
						</p>
					</div>
				</div>

			</div>
		</div>
		<%
			}
		%>
		<%
			}
		%>


		<!-- pdf model popup end  -->


		<!-- modal popup for image pop start-->
		<!-- Modal -->
		<!-- Modal -->
		<%
			int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			for (int i = 0; i < imagecount; i++) {
				String index = "image" + i;
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
				<c:set value="<%=index%>" var="i"></c:set>
					<img style="height: auto; width: 100%" id="myImg" alt="img"
							class="img-responsive invoiceupload-popup-img center-block"
							src="data:image/jpeg;base64,${requestScope[i]}" />
					</div>
					<div class="modal-footer cust-modal-footer">
						<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>
		<%
			}
		%>
		<!-- modal popup for invoice image end -->
		<script>

function appendRow(NMRRowNum) {
    debugger;
  
    /* var tbllength = $('.workorderrowcls').length;  
    if(tbllength==1){    	
    	var tid = $('.workorderrowcls').last().attr("id");     	
    	var res = tid.split("workorderrow")[1];
    	$("#addDeleteItemBtnId"+res).show();
    }
    var pressedKey = window.event;
     
    //alert(pressedKey.keyCode);
    if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
         
        btn = pressedKey.target || pressedKey.srcElement;
        var buttonId = btn.id;
         
        if(buttonId.includes("addNewItemBtnId")) {
            document.getElementById("hiddenSaveBtnId").value = "";
        }
         
        var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
     
        var tbl = document.getElementById("indentIssueTableId");
         
       /*  var valStatus = validateRowData();
       // alert(valStatus);
         
        if(valStatus == false) {
            return false;
        } *//*
        if (typeof(Storage) !== "undefined") {
            // Store    
             debugger;
         
            // Retrieve
         var row1=sessionStorage.getItem("rowsIncre");
         var r=parseInt(row1)+1;
         sessionStorage.setItem("rowsIncre", r);
        } else {
           alert("Sorry, your browser does not support Web Storage...");
        };
        if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
             
            //alert("HAI");
            var row = tbl.insertRow(tbl.rows.length);
         
            var i;
         
            var tableColumnName = "";
            var columnToBeFocused = "";
             debugger;
            //var rowNum = getLastRowNum();
            var rowNum = $(".reviseworkorderrowcls:last").attr("id").split("tr-class")[1];
             
            $("#addNewItemBtnId"+rowNum).hide();
             
            rowNum = parseInt(rowNum)+1;
            var rowid="tr-class"+rowNum;
           
    		$(row).attr("id", rowid);
            $(row).attr("class", "reviseworkorderrowcls");
            for (i = 0; i < tbl.rows[0].cells.length; i++) {
             
            var x = document.getElementById("indentIssueTableId").rows[0].cells;
            tableColumnName = x[i].innerText;
            tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
            tableColumnName = tableColumnName.replace(/\./g,'');
            columnToBeFocused = x[1].innerText;
            columnToBeFocused = columnToBeFocused.replace(/ /g,'');
            //alert("Table Column Name = "+tableColumnName.replace(/ /g,''));
             
            createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);
        }
         
            var lastDiv = getLastRowNum();
            //alert(lastDiv);
         
            //document.getElementById("Product"+lastDiv).focus();
            document.getElementById("WO_MajorHead1"+lastDiv).focus();
        }
    } */
    var rowsLength=$(".reviseworkorderrowcls").length;
    	rowsLength=rowsLength+1;
    var str=	"str+='<option value=''></option>'";
    <% 
	Map<String, String> products1 = (Map<String, String>)request.getAttribute("workMajorHead");
	for(Map.Entry<String, String> prods : products1.entrySet()) {
		String prodName=prods.getValue().replace("\"","#");
		String prodName1=prods.getValue().replace("\"","'");
		String val = prods.getKey()+"$"+prodName;
	%>
		str+='<option value="<%=val%>"><%=prodName1%></option>'
	<% 
		} 
	%>
	var Quty="'qty'";
	var a_rate="'a_rate'";
    var appendRow='<tr id="tr-class'+rowsLength+'" class="reviseworkorderrowcls">'
    +'<td><div class="serialNumcls">'+rowsLength+'</div><input type="hidden" name="dispplayedRows" id="dispplayedRows" value="'+rowsLength+'"><input type="hidden" id="isNewRowAdded'+rowsLength+'" name="isNewRowAdded'+rowsLength+'" value="true"></td>'
    +'<td><select id="combobox'+rowsLength+'" name="WO_MajorHead'+rowsLength+'" onchange="loadSubProds(this.value,"'+rowsLength+'")" class="ui-autocomplete-input form-control">'
    +'</select></td>'
    +'<td><select name="WO_MinorHead'+rowsLength+'" id="comboboxsubProd'+rowsLength+'"  onchange="loadSubSubProducts(this.value,'+rowsLength+');"  class="ui-autocomplete-input form-control"></select></td>'
    +'<td><select name="WO_Desc'+rowsLength+'"  onchange="loadUnits(this.value,'+rowsLength+');"  id="comboboxsubSubProd'+rowsLength+'" class="form-control btn-visibilty'+rowsLength+'"></select></td>'
    +'<td><select name="UnitsOfMeasurement'+rowsLength+'" id="UOMId'+rowsLength+'" class="form-control btn-visibilty'+rowsLength+'"></select></td>'
    +'<td><input id="Quantity'+rowsLength+'" name="Quantity'+rowsLength+'" onblur="calCulateTotalAmout('+Quty+',this.value,'+rowsLength+')" onkeypress="return isNumberCheck(this, event)"  style="width:44%;"  class="form-control input-visibilty'+rowsLength+' addFractioPoints copyPasteRestricted" /></td>'
    +'<td><input id="AcceptedRate'+rowsLength+'" name="AcceptedRate'+rowsLength+'" onkeypress="return isNumberCheck(this, event)" onfocusout="calCulateTotalAmout('+a_rate+',this.value,'+rowsLength+')" class="form-control commmntstooltip input-visibilty'+rowsLength+' addFractioPoints copyPasteRestricted"/></td>'
    +'<td><input id="TotalAmountId'+rowsLength+'" name="TotalAmount'+rowsLength+'"class="form-control addFractioPoints" readonly/></td>'
    +'<td><input id="Comments'+rowsLength+'" name="comments'+rowsLength+'"class="form-control commmntstooltip input-visibilty'+rowsLength+'"/></td>'
    +'<td>'
    +'<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId'+rowsLength+'" class="btnaction" style="margin-right:5px;"onclick="deleteReviseWo('+rowsLength+')"><i class="fa fa-trash"></i></button>'
    +'<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId'+rowsLength+'" onclick="appendRow('+rowsLength+')" class="btnaction"><i class="fa fa-plus"></i></button>'
    +'</td>'
    +'</tr>';
    $("#tr-class"+NMRRowNum).closest("tr").after(appendRow);
    $("#combobox"+rowsLength).html(str);
    $("#combobox"+rowsLength).combobox();
    $("#comboboxsubProd"+rowsLength).combobox();
    $("#comboboxsubSubProd"+rowsLength).combobox();
    // loadScopeOfWorkAndAmount(this.value,"'+rowsLength+'");
    $(".copyPasteRestricted").bind('paste', function (e) {
					e.preventDefault();
				});
    //for serial numbers
    $(".serialNumcls").each(function(i){
   		 $(this).text(i+1);
    });
  
}
//deleting a row
function deleteReviseWo(id){
	var conformation = confirm("Do you want to delete?");
	if(conformation == true){
		deleteReviseWorowscount = $(".reviseworkorderrowcls").length;
		$("#tr-class"+id).remove();
	}
	else{
		return false;
	
	}
}

function formatColumns(colName) {
    var colNm = colName.replace(/ /g,'');
    return colNm.replace(/\./g,'');
}
function getLastRowNum() {
    
    var allElements = document.getElementsByTagName("*");
       var allIds = [];
       for (var i = 0, n = allElements.length; i < n; ++i) {
           var el = allElements[i];
           if (el.id) {
               var ask = el.id;
               if(ask.indexOf("snoDivId") != -1) {
                   allIds[i] = el.id;
               }
           }
       }
        
       var lastDiv = (allIds.length) - 1;
       //alert(allIds[lastDiv]);
        
       var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;
       //alert(lastDivVal);
        
       return parseInt(lastDivVal);
}

function deleteRow(btn, currentRow) {
	debugger;
	document.getElementById("hiddenSaveBtnId").value = "";
	 var canIDelete = window.confirm("Do you want to delete?");
	 if(canIDelete == false) {
	        return false;
	 }
	var rowscount= $('.reviseworkorderrowcls').length;
	if(rowscount==1){
		alert("This row can't be deleted.");
		return false;
	}
	//removing row
   $("#tr-class"+currentRow).remove();
	
	var tid=$('.reviseworkorderrowcls').last().attr('id');	
	var res = tid.split("tr-class")[1];
	if(rowscount==2){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
	}	
//	calculateOtherCharges();
}
//to ditroy loaders
$(window).load(function() {
	 $(".overlay_ims").hide();	
	 $(".loader-ims").hide();
});
//this code for to active the side menu 
var referrer="reviseNMRWorkOrder.spring";
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
