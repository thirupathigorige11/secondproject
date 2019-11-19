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
 
//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
	 //alert(cell+"-->"+text+"-->"+style+"-->"+fldLength+"-->"+cellsLen+"-->"+tableColumnName);
	
     //var temp = cellsLen-1;
     var vfx = fldLength;
     //alert(style);
     //if(text == 0 || text == '0') {
    	
     //alert(tableColumnName);
    
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
    	    	div.setAttribute("onkeydown", "appendRow()");    	    	
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
    	    	div.setAttribute("onkeydown", "appendRow()");
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
    	    	div.setAttribute("onkeydown", "appendRow()");
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
		    div.setAttribute("onkeydown", "appendRow()");
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
		    div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == remarksColumn) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeydown", "appendRow()");
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

//Validation started 
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
	  
	  	if(reqDateVal == "" || reqDateVal == null || reqDateVal == '') {
			alert("Please select Date.");
			document.getElementById(reqDate).focus();
			return false;
	  	} 	
 	  
	  	var requestorName = "<%= requestorName %>";
	  	requestorName = formatColumns(requestorName)+"Id";
	  	requestorName = '"'+requestorName+'"';
	  	requestorName = requestorName.replace(/\"/g, "");
	  	//alert(requestorName);
	  	
	  	var requestorNameVal = document.getElementById(requestorName).value;
   	  
	  	if(requestorNameVal == "" || requestorNameVal == null || requestorNameVal == '') {
			alert("Please enter Requestor Name.");
			document.getElementById(requestorName).focus();
			return false;
	  	}
	  
	  	var requestorId = "<%= requestorId %>";
	  	requestorId = formatColumns(requestorId)+"Id";
	  	requestorId = '"'+requestorId+'"';
	  	requestorId = requestorId.replace(/\"/g, "");
	  	//alert(requestorId);
	  	
	  	var requestorIdVal = document.getElementById(requestorId).value;
	
   		if(requestorIdVal == "" || requestorIdVal == null || requestorIdVal == '') {
			alert("Please enter Requestor Id.");
			document.getElementById(requestorId).focus();
			return false;
		}
	  
	  	var blockId = "<%= block %>";
	  	blockId = formatColumns(blockId)+"Id";
	  	blockId = '"'+blockId+'"';
	  	blockId = blockId.replace(/\"/g, "");
	  	//alert(blockId);
	  
	  	var floorId = "<%= floor %>";
	  	floorId = formatColumns(floorId)+"Id";
	  	floorId = '"'+floorId+'"';
	  	floorId = floorId.replace(/\"/g, "");
	  	//alert(floorId);
	  
	  	var flatId = "<%= flatNumber %>";
	  	flatId = formatColumns(flatId)+"Id";
	  	flatId = '"'+flatId+'"';
	  	flatId = flatId.replace(/\"/g, "");
	  	//alert(flatId);
		
   		/* var blockIdVal = document.getElementById(blockId).value;
   		
		if(blockIdVal == "" || blockIdVal == null || blockIdVal == '') {
			alert("Please select Block.");
			document.getElementById(blockId).focus();
			return false;
		}
		
   		var floorIdVal = document.getElementById(floorId).value;
   		
		if(floorIdVal == "" || floorIdVal == null || floorIdVal == '') {
			alert("Please select Floor.");
			document.getElementById(floorId).focus();
			return false;
		}
		
   		var flatIdVal = document.getElementById(flatId).value;
   		
		if(flatIdVal == "" || flatIdVal == null || flatIdVal == '') {
			alert("Please select Flat.");
			document.getElementById(flatId).focus();
			return false;
		} */
		
		var slipId = "<%= slipNumber %>";
	  	slipId = formatColumns(slipId)+"Id";
	  	slipId = '"'+slipId+'"';
	  	slipId = slipId.replace(/\"/g, "");
	  	//alert(slipId);
	  	
   		var slipIdVal = document.getElementById(slipId).value;
   		
		if(slipIdVal == "" || slipIdVal == null || slipIdVal == '') {
			alert("Please enter Slip Number.");
			document.getElementById(slipId).focus();
			return false;
		}
		
		var elementList = document.getElementsByTagName("*");
      	
      	var rowNums = getAllProdsCount();
      	
      	var splitedRows = rowNums.split("|");
      	
      	for(var x=0; x < rowNums.length; x++) {
      		
      		var curRow = splitedRows[x];
		
		
		 var ContractorId = "<%= ContractorName %>";
    	   ContractorId = formatColumns(ContractorId)+"Id";
    	   ContractorId = '"'+ContractorId+'"';
    	   ContractorId = ContractorId.replace(/\"/g, "");
    	  
      		
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
      	    			//qty = parseFloat(qty);
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
	
	//document.getElementById("saveBtnId").disabled = true;	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("CreateCentralIndentFormId").action = "createPO.spring";
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
			<label class="control-label col-sm-2">PO Number </label>
			<div class="col-sm-3" >
			<input name="indentNumber" id="indentNumberId"  readonly="true" class="form-control" value="${element.indentNumber}"}/>
				<%-- <form:input path="ReqId" id="ReqIdId"  readonly="true" class="form-control"/> --%>
			</div>
			<label class="control-label col-sm-2">Site Name </label>
			<div class="col-sm-3" >
				<input name="siteName" id="siteNameId"  readonly="true" class="form-control" value="${element.siteName}"}/>
			</div>
			</c:forEach>
		   </div>
	  		<input type="hidden" name="numberOfRows"   value="${requestScope['IndentDetails'].size()}" />
<!-- *************** Table 01 grid***************** -->
				<div class="container1">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="background-color: #e0dada;">
			<tr>
				<th>Actions</th>
				<th>Product Name</th>
				<th>SubProduct Name</th>
				<th>ChildProduct Name</th>
				<th>Measurement Name</th>
				<th>Request Quantity</th>
				<!-- <th>Site Quantity</th>-->
				
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
					<input type="hidden" name="requiredQuantity${element.strSerialNumber}" value="${element.requiredQuantity1}" />
				</div>
		   		<tr>
					<td><input type="checkbox" name="checkboxname${element.strSerialNumber}" value="checked"></input></td>				
					<td class="tiptext">${element.product1}</td>
					<td>${element.subProduct1}</td>
					<td>${element.childProduct1}</td>
					<td>${element.unitsOfMeasurement1}</td>
					<td>${element.requiredQuantity1}</td>	
			<!-- 	<td>100</td>	
					<td id="sttle-id1"><a href="" style="color: blue" >SETTEL</a></td>	 -->
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>

				<div class="col-sm-3 pt-10">
			<!-- 		<a class="site_title" href=""><input type="submit" class="btn btn-warning" value="Send Enquiry" id="saveBtnId" ></a>-->
					
					</div>    
				<div class="col-sm-2 pt-10"> 
					<input type="button" class="btn btn-warning"value="Create PO" id="saveBtnId" onclick="createPO()">
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
			
			/* $('#saveBtnId1').click(function(){
			      window.open('CreatePO.spring');
			   });*/

		</script> 
</body>
</html>