<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import = "java.util.ResourceBundle" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String tableHead = resource.getString("label.issueToOtherSiteTableHead");
	String colon = resource.getString("label.colon");	
	String reqId = resource.getString("label.reqId");
	String date = resource.getString("label.date");
	String purpose = resource.getString("label.purpose");
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
	String site = resource.getString("label.site");
	String vehicleNo = resource.getString("label.vehicleNo");
	//Loading Indent Issue Table Column Headers/Labels - Start
%>

<html>
<head>
<script src="js/issueToOtherSite.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
        <link href="js/inventory.css" rel="stylesheet" type="text/css" />
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

<style>
.Rtnble{float: left;  padding: 5px;}
.radioBttn{float: left;}
.pro-table thead, .pro-table tbody tr{table-layout:fixed;display:table;width:100%;}
 .pro-table thead tr th:first-child, .pro-table tbody tr td:first-child{width:46px;text-align: center;}
 .pro-table tbody tr td{border-top:0px !important;}	
 .input-group-addon {border: 1px solid #ccc !important;}
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
	  $("#ReqDateId").datepicker({dateFormat: 'dd-M-y',maxDate: new Date(),changeMonth: true,
	      changeYear: true});
  });
  
</script>

<script type="text/javascript">
 
//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
	 //alert(cell+"-->"+text+"-->"+style+"-->"+fldLength+"-->"+cellsLen+"-->"+tableColumnName);
	
     var vfx = fldLength;
     tableColumnName=tableColumnName.trim();
     var snoColumn =  "<%= serialNumber %>";
     snoColumn = formatColumns(snoColumn);
     //alert(snoColumn);
     
     var productColumn =  "<%= product %>";
     productColumn = formatColumns(productColumn);
     //alert(productColumn);
  	 
  	 var subProductColumn =  "<%= subProduct %>";
  	 subProductColumn = formatColumns(subProductColumn);
 	 //alert(subProductColumn);
     
 	 var childProductColumn =  "<%= childProduct %>";
 	 childProductColumn = formatColumns(childProductColumn);
	 //alert(childProductColumn);
	 
	 var measurementColumn =  "<%= measurement %>";
	 measurementColumn = formatColumns(measurementColumn);
	 //alert(measurementColumn);
	 
	 var productAvailabilityColumn =  "<%= productAvailability %>";
	 productAvailabilityColumn = formatColumns(productAvailabilityColumn);
	 //alert(productAvailabilityColumn);
	 
	 var quantityColumn =  "<%= quantity %>";
	 quantityColumn = formatColumns(quantityColumn);
	 //alert(quantityColumn);
	 
	 var uOrFColumn =  "<%= uOrF %>";
	 uOrFColumn = formatColumns(uOrFColumn);
	 //alert(uOrFColumn);
	 
	 var remarksColumn =  "<%= remarks %>";
	 remarksColumn = formatColumns(remarksColumn);
	 //alert(remarksColumn);
	 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);
	 //alert(actionsColumn);
     
     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        cell.appendChild(snoDiv);
    }
    else {
    	if(tableColumnName == productColumn) {
    		var dynamicSelectBoxId = "combobox"+vfx;
    		//alert(dynamicSelectBoxId);
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	//alert(temp);
    	    	//div.setAttribute("onkeydown", "appendRow()");    	    	
    	    }    	    
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Select one...";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);    	    
    	    var option = "";
    		<% 
    			Map<String, String> products = (Map<String, String>)request.getAttribute("productsMap");
    			for(Map.Entry<String, String> prods : products.entrySet()) {
				String val = prods.getKey()+"$"+prods.getValue();
			%>
				option = document.createElement("option");
	    	    option.text = "<%= prods.getValue() %>";
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
    	else if(tableColumnName == subProductColumn) {
    		
    		var dynamicSelectBoxId = "comboboxsubProd"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	//div.setAttribute("onkeydown", "appendRow()");
    	    }    	    
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();    	        
			});    	    
    	}
    	else if(tableColumnName == childProductColumn) {
    		var dynamicSelectBoxId = "comboboxsubSubProd"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	//div.setAttribute("onkeydown", "appendRow()");
    	    }
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}    	
    	else if(tableColumnName == measurementColumn) {
    		var dynamicSelectBoxId = tableColumnName+"Id"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("onchange", "return validateProductAvailability(this);");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	}
    	else if(tableColumnName == quantityColumn) {
			
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
	    	div.setAttribute("onblur", "return validateUnitsAndAvailability(this);");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		    
		    var div2 = document.createElement("input");
		    div2.setAttribute("type", "text");
		    div2.setAttribute("name", productAvailabilityColumn+vfx);
		    div2.setAttribute("id", productAvailabilityColumn+"Id"+vfx);
		    div2.setAttribute("readonly", "true");
		    div2.setAttribute("class", 'form-control');
		    cell.appendChild(div2);		    
   		}    	
    	else if(tableColumnName == uOrFColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "UOrF"+vfx);
		    div.setAttribute("id", "UOrFId"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == remarksColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
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

function validateRowData() {	
	
	var reqId = "<%= reqId %>";
  	reqId = formatColumns(reqId)+"Id";
  	reqId = '"'+reqId+'"';
  	reqId = reqId.replace(/\"/g, "");
  	//alert(reqId);
  	
  	var reqIdVal = document.getElementById(reqId).value;
  
  	if(reqIdVal == "" || reqIdVal == null || reqIdVal == '') {
		alert("Please enter Req Id.");
		document.getElementById(reqId).focus();
		return false;
  	}
	  
  	var reqDate = "<%= date %>";
  	reqDate = formatColumns(reqDate)+"Id";
  	reqDate = '"'+reqDate+'"';
  	reqDate = reqDate.replace(/\"/g, "");
  	//alert(reqDate);
  	
  	var reqDateVal = document.getElementById(reqDate).value;
  	var vehicleNo = document.getElementById("vehicleNo").value;
  
  	//alert("vehicleNo "+varvehicleNo);
  	
  	
  	if(reqDateVal == "" || reqDateVal == null || reqDateVal == '') {
		alert("Please select Date.");
		document.getElementById(reqDate).focus();
		return false;
  	}
  	
	if(vehicleNo == "" || vehicleNo == null || vehicleNo == '') {
		alert("Please enter vehicleNo");
		document.getElementById(vehicleNo).focus();
		return false;
  	}
  	
	var siteIds = document.getElementById("SiteId");
	
	var selectedSite = siteIds.options[siteIds.selectedIndex].value;
	//alert(selectedSite);
	
	if(selectedSite == "" || selectedSite == '' || selectedSite == null) {
		alert("Please select Site.");
		document.getElementById("SiteId").focus();
		return false;
	}
	
    var elementList = document.getElementsByTagName("*");
    
    var rowNums = getAllProdsCount();
  	
  	var splitedRows = rowNums.split("|");
  	
  	for(var x=0; x < rowNums.length; x++) {
  		
  		var curRow = splitedRows[x];
  		
  		var product = "<%= product %>";
  	  	product = formatColumns(product)+curRow;
  	  	product = '"'+product+'"';
  	  	product = product.replace(/\"/g, "");
  	 	//alert(product);
  	  
  	  	var subProduct = "<%= subProduct %>";
  	  	subProduct = formatColumns(subProduct)+curRow;
  	  	subProduct = '"'+subProduct+'"';
  	  	subProduct = subProduct.replace(/\"/g, "");
  	  	//alert(subProduct);
  	  
  	  	var childProduct = "<%= childProduct %>";
  	  	childProduct = formatColumns(childProduct)+curRow;
  	  	childProduct = '"'+childProduct+'"';
  	  	childProduct = childProduct.replace(/\"/g, "");
  	  	//alert(childProduct);
  	  
  	  	var quantity = "<%= quantity %>";
  	  	quantity = formatColumns(quantity)+"Id"+curRow;
  	  	quantity = '"'+quantity+'"';
  	  	quantity = quantity.replace(/\"/g, "");
  	  	//alert(quantity);
  	  
  	  	var measurement = "<%= measurement %>";
  	  	measurement = formatColumns(measurement)+"Id"+curRow;
  	 	measurement = '"'+measurement+'"';
  	  	measurement = measurement.replace(/\"/g, "");
  	  	//alert(measurement);
  		
  		for (var i in elementList) {
  			
  			if (elementList[i].id != "") {
  				
  				if(elementList[i].id == product) {
  	    		  	var pro = document.getElementById(product).value;
  					if(pro == "" || pro == null || pro == '') {
  						alert("Please enter Product.");
  						document.getElementById(product).focus();
  						return false;
  					}
  	    	  	} 
  		  	  	else if(elementList[i].id == subProduct) {
  	    		  	var subPro = document.getElementById(subProduct).value;
  					if(subPro == "" || subPro == null || subPro == '') {
  						alert("Please enter Sub Product.");
  						document.getElementById(subProduct).focus();
  						return false;
  					}
  	    	  	}  	  
  		  	  	else if(elementList[i].id == childProduct) {
  	    		  	var childPro = document.getElementById(childProduct).value;
  					if(childPro == "" || childPro == null || childPro == '') {
  						alert("Please enter Child Product.");
  						document.getElementById(childProduct).focus();
						return false;
					}
  	    	  	}	  	 
  		  	  	else if(elementList[i].id == quantity) {
  	    			var qty = document.getElementById(quantity).value;
  					if(qty == "" || qty == null || qty == '' || qty == '.') {
  						alert("Please enter Quantity.");
  						document.getElementById(quantity).value = "";
  						document.getElementById(quantity).focus();
  						return false;
  					}
  					if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
  						alert("Please enter valid Quantity.");
  						document.getElementById(quantity).value = "";
  						document.getElementById(quantity).focus();
  						return false;
  					}
  	    	  	}
  		  	  	else if(elementList[i].id == measurement) {
  	    		 	var units = document.getElementById(measurement).value;
  					if(units == "" || units == null || units == '') {
  						alert("Please enter Units Of Measurement.");
  						document.getElementById(measurement).focus();
  						return false;
  					}
				}      				
  			}      			
  		}      		
  	}
}
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
				<li class="breadcrumb-item"><a href="#">Issue</a></li>
				<li class="breadcrumb-item active">Issue to other Site</li>
			</ol>
		</div>
	<div>
	<div align="">
		<form:form modelAttribute="indentIssueOtherSiteForm" id="indentIssueOtherSiteFormId" class="form-horizontal">
			<div class="divindentiss">
			<div class="container">				
  				<div class="col-md-12 text-center center-block">
				     	 	<label><input type="radio" class="radioBttn" id="issueType" value="Returnable" name="issueType" style="height:20px; width:20px;" ><span class="Rtnble">Returnable</span></label>
				   		    <label><input type="radio" class="radioBttn"  id="issueType" value="NonReturnable" name="issueType" style="height:20px; width:20px;" ><span class="Rtnble">Non Returnable</span></label>
				 </div>
	  		</div>
	  		
	<!-- </div>   -->			
	  	<div class="col-md-12 border-inwards-box">
	  	 <div class="col-md-4">		
	  	 <div class="form-group">	  
			<label  class="control-label col-md-6" ><%= reqId %> <%= colon %> </label>
			<div class="col-md-6" >
				<form:input path="ReqId" id="ReqIdId" readonly="true" class="form-control"/>
			</div>
		</div>
		</div>
		 <div class="col-md-4">		
	  	 <div class="form-group">	  
			<label class="control-label col-md-6 col-xs-12"> <%= date %> <%= colon %> </label>
			<div class="col-md-6 col-xs-12 input-group">
				<form:input path="ReqDate" id="ReqDateId" class="form-control readonly-color" readonly="true"/>
				<label class="input-group-addon btn" for="ReqDateId"><span class="fa fa-calendar"></span></label>
			</div>
			</div>
			</div>
			 <div class="col-md-4">		
			  	 <div class="form-group">	  
					<label class="control-label col-md-6"><%= site %> <%= colon %>  </label>
					<div class="col-md-6" >
							  <form:select path="Site" id="SiteId" class="form-control">
									<form:option value="">--Select--</form:option>
							    		<%		
							    			Map<String, String> siteMap = (Map<String, String>)request.getAttribute("sitesMap");
							    			for(Map.Entry<String, String> sites : siteMap.entrySet()) {
												String siteIdAndName = sites.getKey()+"$"+sites.getValue();
										%>
												<form:option value="<%=siteIdAndName%>"><%=sites.getValue()%></form:option>
							    		<%
							    			} 
							    		%>
								</form:select>
					</div>
				</div>
			</div>
			 <div class="col-md-4">		
	  	 <div class="form-group">	  
			
		     	<label class="control-label col-md-6"> <%= vehicleNo %> <%= colon %> </label>
		     	<div class="col-md-6" >
				<form:input path="vehicleNo" id="vehicleNo" readonly="" class="form-control" autocomplete="off" />
				</div>
		</div>
		</div>
	  			
	  			
	  			
	  			

	  	</div>			
		<div class="clearfix"></div>
		   <div class="table-responsive tblprodindissudiv">
			<table id="indentIssueTableId" class="table table-new tblprodindissu pro-table" style="width:2000px;">
				<thead class="cal-thead-inwards">
				  <tr>
					<th><%= serialNumber %></th>
    				<th><%= product %></th>
    				<th><%= subProduct %></th>
    				<th><%= childProduct %></th>    				
    				<th><%= measurement %></th>
    				<th><%= quantity %></th>
    				<th><%= uOrF %></th>
    				<th><%= remarks %></th>
    				<th><%= actions %></th>
  				</tr>
				</thead>
				<tbody class="tbl-fixed-tbody">
				  <tr>
					<td>						
						<div id="snoDivId1">1</div>
					</td>
					<td>
						<select id="combobox1" name="Product1" >
							<option value="">Select one...</option>
					    		<%					    		
					    			for(Map.Entry<String, String> prods : products.entrySet()) {
									String prodIdAndName = prods.getKey()+"$"+prods.getValue();
								%>
									<option value="<%= prodIdAndName %>"> <%= prods.getValue() %></option>
					    		<% 
					    			} 
					    		%>
						</select>
					</td>
					<td>
						<form:select path="SubProduct1" id="comboboxsubProd1" class="form-control"/>
					</td>
					<td>
						<form:select path="ChildProduct1" id="comboboxsubSubProd1" class="form-control"/>
					</td>
					<td>
						<form:select path="UnitsOfMeasurement1" id="UnitsOfMeasurementId1" onchange="return validateProductAvailability(this);" class="form-control"/>
					</td>
					<td class="w-70">
						<form:input path="Quantity1" id="QuantityId1" maxlength="8" size="8"   onkeypress="return validateNumbers(this, event);" onblur="return validateUnitsAndAvailability(this);" class="form-control"/>
						<form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="UOrF1" id="UOrFId1"  class="form-control" autocomplete="off"/>
					</td>
					<td>
						<form:input path="Remarks1" id="RemarksId1"  class="form-control" autocomplete="off"/>
					</td>				
					<td>
						<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button>
					</td>
				</tr>	
				</tbody>			
			</table>			
			</div>
	
			<div class="col-md-6">
			
					<label class="control-label col-md-4">
							<%= purpose %> <%= colon %>
					</label>
					<div class="col-md-8">
						<form:textarea path="Purpose" id="PurposeId" class="form-control"/>
					</div>
		
			</div>
			<div class="col-md-12 text-center center-block">
			   <input class="btn btn-warning Mrgtop20" type="button" value="Submit" id="saveBtnId" onclick="saveRecords('SaveClicked')">
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			</div>
		</form:form>
	</div>
	</div>
	<!-- /page content -->        
	</div>
	</div>
</div>

<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
<script>

	$(document).ready(function() {	
		$(".up_down").click(function(){ 
			$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
			$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
		}); 
		
	});
	
	
</script> 
</body>
</html>
