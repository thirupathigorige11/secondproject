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
<script src="js/IndentCreationDetailsShow.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
	    <jsp:include page="CacheClear.jsp" />  
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
        <link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />

		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

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



  
</script>

<script type="text/javascript">
//var delOne;
 function editTotalRow()
 {
	
 }
//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	
	 //alert(cell+"-->"+text+"-->"+style+"-->"+fldLength+"-->"+cellsLen+"-->"+tableColumnName);
	
     //var temp = cellsLen-1;
     var vfx = fldLength;
     
     //removing space
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
//Validation End
</script>
<style>
.input-group-addon{border:1px solid #ccc !important;}
.width-inwars-table{width:1500px;}
.form-group label{ text-align: left}
table { border-collapse: collapse; empty-cells: show; }
td { position: relative; }
tr.strikeout td:before {
  content: " ";
  position: absolute;
  top: 50%;
  left: 0;
  border-bottom: 1px solid red;
  width: 100%;
    z-index: 100;
}
tr.strikeout td:after {
  content: "\00B7";
  font-size: 1px;
  z-index: 100;
}
/* / Extra styling / */
/* td { width: 100px; } */
th { text-align: left; }
.Btnbackground{
background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
}
.closeiconforimg {
    position: relative;
    top: -88px;
    right: 13px;
    z-index: 100;
    background-color: #e0e0e0;
    padding: 5px 5px;
    color: #000;
    font-weight: bold;
    cursor: pointer;
    text-align: center;
    font-size: 17px;
    line-height: 10px;
    border-radius: 50%;
    border: 1px solid blue;
    color: blue;
    opacity: 2;
}
#indentIssueTableId thead, #indentIssueTableId tbody tr{table-layout:fixed;display:table;width:100%;}
#indentIssueTableId thead tr th:first-child, #indentIssueTableId tbody tr td:first-child{width:52px !important;}
#indentIssueTableId tbody tr td, #indentIssueTableId tbody tr td{border-top:0px !important;}

</style>
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
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">View my indent</li>
						</ol>
					</div>
					<!-- loader -->
						<div class="loader-sumadhura" style="display: none;z-index:999;">
							<div class="lds-facebook">
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
								<div></div>
							</div>
							<div id="loadingMessage">Loading...</div>
						</div>
					    <!-- loader -->					
					<div>
					<form:form modelAttribute="indentCreationModelForm" id="indentCreationDetailsShowFormId" class="form-horizontal">
					 <div class="col-md-12 col-sm-12 col-xs-12">
						  	   <div class="border-indent">
						  	    <c:forEach var="getIndentCreation" items="${requestScope['IndentGetList']}">
						 	    <div class="col-md-6 col-sm-6 col-xs-12">
						 	     <div class="form-group">
								<label class="control-label col-md-6 col-sm-6 col-xs-12">Site wise Indent Number :</label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input  id="siteWiseIndentNo" name="siteWiseIndentNo"  readonly="true" value="${getIndentCreation.siteWiseIndentNo}" class="form-control"/>
									<input  id="IndentNumberId" name="indentNumber"  readonly="true" type="hidden" value="${getIndentCreation.indentNumber}" class="form-control"/>
								
								<input type="hidden" class="form-control"  name="versionNo" value="${getIndentCreation.versionNo}">
								<input type="hidden" class="form-control"  name="reference_No" value="${getIndentCreation.reference_No}">
								<input type="hidden"  class="form-control"  	name="issue_date" value="${getIndentCreation.issue_date}">
								
								</div>
								</div>
						 	    </div>
								<div class="col-md-6 col-sm-6 col-xs-12">
								  <div class="form-group">
								<label class="control-label col-md-6 col-sm-6 col-xs-12">Schedule Date :</label>
								<div class="col-md-6 col-sm-6 col-xs-12 input-group">
									<input  id="ScheduleDateId" name="ScheduleDate" class="form-control readonly-color" readonly="true" autocomplete="off" value="${getIndentCreation.strScheduleDate}"/>
					                <label class="input-group-addon btn" for="ScheduleDateId"> <span class="fa fa-calendar"></span> </label>
								</div>
							   </div>
								</div>
						  		<div class="col-md-6 col-sm-6 col-xs-12">
							  		 <div class="form-group">
										<label class="control-label col-md-6 col-sm-6 col-xs-12">Created by :</label>
										<div class="col-md-6 col-sm-6 col-xs-12" >
											<input  id="IndentFromId" name="IndentFrom" readonly="true" class="form-control" value="${getIndentCreation.indentFrom}"/>
										</div>
									</div>
						  		</div>
								<div class="col-md-6 col-sm-6 col-xs-12">
								   <div class="form-group">
									<label class="control-label col-md-6 col-sm-6 col-xs-12">Pending Employee/Dept Name :</label>
									<div class="col-md-6 col-sm-6 col-xs-12" >
										<input  id="IndentToId" name="IndentTo" readonly="true" class="form-control" value="${getIndentCreation.indentTo}"/>
									</div>
							      </div> 
								</div>	
						   		<div class="col-md-6 col-sm-6 col-xs-12">
							   		 <div class="form-group">
										<label class="control-label col-md-6 col-sm-6 col-xs-12">Required Date :</label>
										<div class="col-md-6 col-sm-6 col-xs-12 input-group" >
											<input  id="RequiredDateId" name="RequiredDate" class="form-control" autocomplete="off" readonly="true" value="${getIndentCreation.strRequiredDate}"/>
							                <label class="input-group-addon btn" for="RequiredDateId"> <span class="fa fa-calendar"></span> </label>
										</div>
									</div>
						   		</div>	
						   		<div class="col-md-6 col-sm-6 col-xs-12">
							   		 <div class="form-group">
										<label class="control-label col-md-6 col-sm-6 col-xs-12">Indent Name :</label>
										<div class="col-md-6 col-sm-6 col-xs-12">
											<input  id="indentNameId" name="indentName" class="form-control" autocomplete="off" readonly="true" value="${getIndentCreation.indentName}"/>
										</div>
									</div>
						   		</div>	
								<div class="col-md-6 col-sm-6 col-xs-12">
								  <div class="form-group">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<input type="hidden"  id="siteId" name="siteId" readonly="true" class="form-control" value="${getIndentCreation.siteId}"/>
									</div>
								   </div>
								</div>
						  	    </c:forEach>
						  	  
							    <input type="hidden" name="noofRowsTobeProcessed" value="${requestScope['IndentCreationDetailsList'].size()}"/>
							 <input type="hidden" name="numberOfRows" value="${requestScope['IndentCreationDetailsList'].size()}"/>
							  </div>
						  	   </div> 
						  	   <div class="clearfix"></div>     
								  <div class="col-md-12">
								   <div class="table-responsive "> <!-- tblprodindissudiv -->
								    <table id="indentIssueTableId" class="table table-new" style="width:2500px"> <!-- tblprodindissu -->
									
									<thead class="cal-thead-inwards">
										<tr>
											<th><%= serialNumber %></th>
						    				<th><%= product %></th>
						    				<th><%= subProduct %></th>
						    				<th><%= childProduct %></th>    				
						    				<th><%= measurement %></th>
						    				<th class="w-70"><%= quantity %></th>
						    				<th> PO Initiated Quantity </th> 
						    				<th> Received Quantity </th>  
						    				<th><%= remarks %></th>
						    			    <th>Purchase Department Remarks </th>  
						    			    <!-- 	 <th>Edit</th>  -->
						    			    <th style="display:none""><%= actions %></th>
						  				</tr>
						  				</thead>
						  				<tbody class="tbl-fixedheader-tbody">
						  				 <c:forEach var="getIndentCreationDetails" items="${requestScope['IndentCreationDetailsList']}">
										<input  name="isDelete${getIndentCreationDetails.strSerialNumber}" id="isDelete${getIndentCreationDetails.strSerialNumber}" readonly="true" value="z" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
										
										<tr id="tr-class${getIndentCreationDetails.strSerialNumber}">
											<td>						
												<div id="snoDivId${getIndentCreationDetails.strSerialNumber}">${getIndentCreationDetails.strSerialNumber}</div>
											</td>
											<td data-toggle="tooltip" title="${getIndentCreationDetails.product1}">
												<select id="combobox${getIndentCreationDetails.strSerialNumber}" name="product${getIndentCreationDetails.strSerialNumber}"  class="form-control btn-visibilty${getIndentCreationDetails.strSerialNumber}" readonly >
													<option value="${getIndentCreationDetails.product1}">${getIndentCreationDetails.product1}</option> 
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
											<td data-toggle="tooltip" title="${getIndentCreationDetails.subProduct1}">
												<select name="subProduct${getIndentCreationDetails.strSerialNumber}"  id="comboboxsubProd${getIndentCreationDetails.strSerialNumber}" class="form-control btn-visibilty${getIndentCreationDetails.strSerialNumber}" readonly >
												<option value="${getIndentCreationDetails.subProduct1}">${getIndentCreationDetails.subProduct1}</option>
												</select>
											</td>
											<td data-toggle="tooltip" title="<c:out value='${getIndentCreationDetails.childProduct1}' />">
												<select name="childProduct${getIndentCreationDetails.strSerialNumber}" id="comboboxsubSubProd${getIndentCreationDetails.strSerialNumber}" class="form-control btn-visibilty${getIndentCreationDetails.strSerialNumber}" readonly >
												<option value="<c:out value='${getIndentCreationDetails.childProduct1}' />"><c:out value='${getIndentCreationDetails.childProduct1}' /></option>
												</select>
											</td>
											<td data-toggle="tooltip" title="${getIndentCreationDetails.unitsOfMeasurement1}">
												<select name="unitsOfMeasurement${getIndentCreationDetails.strSerialNumber}" id="UnitsOfMeasurementId${getIndentCreationDetails.strSerialNumber}" class="form-control btn-visibilty${getIndentCreationDetails.strSerialNumber}" onchange="return validateProductAvailability(this);" readonly >
												<option value="${getIndentCreationDetails.unitsOfMeasurement1}">${getIndentCreationDetails.unitsOfMeasurement1}</option>
												</select>
											</td>
											<td data-toggle="tooltip" title="${getIndentCreationDetails.requiredQuantity1}">
												<input  name="requiredQuantity1${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.requiredQuantity1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/>
											 <td>
											
											<input name="poInitiatedQuantity${getIndentCreationDetails.strSerialNumber}" readonly="true" data-toggle="tooltip" value="${getIndentCreationDetails.poIntiatedQuantity}" title="${getIndentCreationDetails.poIntiatedQuantity}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/>
											</td>
											<td>
											
											<input name="receivedQuantity${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.receivedQuantity}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/>
											</td> 
											<td>
												
										    <input name="remarks${getIndentCreationDetails.strSerialNumber}" readonly="true"   value='<c:out value="${getIndentCreationDetails.remarks1}"/>' data-toggle="tooltip" title="<c:out value="${getIndentCreationDetails.remarks1}"/>"  readonly="true" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/>
											<input  name="actualProductId${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.productId1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
										    <input  name="actualSubProductId${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.subProductId1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
											<input  name="actualChildProductId${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.childProductId1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
											<input  name="actualUnitsOfMeasurementId${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.unitsOfMeasurementId1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
											<input  name="actualRequiredQuantity${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.requiredQuantity1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
											<input  name="indentCreationDetailsId${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.indentCreationDetailsId}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden" />
											<input name="productAvailability${getIndentCreationDetails.strSerialNumber}" readonly="true" value="${getIndentCreationDetails.productAvailability1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}" type="hidden"/>
											
											</td>	
											
											<td>
											
											 <input name="purchaseDeptRemarks${getIndentCreationDetails.strSerialNumber}" readonly="true"   value='<c:out value="${getIndentCreationDetails.type_Of_Purchase}"/>'  data-toggle="tooltip" title="<c:out value="${getIndentCreationDetails.type_Of_Purchase}"/>"  readonly="true" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/> 
											</td> 
										 	<!-- <td>
												<button type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" style="" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-pencil"></i></button>
											</td> 		-->	
										 	<td style="display:none">
										 		<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${getIndentCreationDetails.strSerialNumber}" class="btnaction" onclick="removeRow(${getIndentCreationDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
												
											</td> 
											<td style="display:none">
										 		<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${getIndentCreationDetails.strSerialNumber}" class="btnaction" onclick="removeRow(${getIndentCreationDetails.strSerialNumber})" ><i class="fa fa-remove"></i></button>
											<%-- 	<button type="button" name="editItemBtn" value="Edit Item" id="editItem1" class="btnaction" onclick="editRow('${getIndentCreationDetails.strSerialNumber}')" ><i class="fa fa-pencil"></i></button> --%>
												<!-- <button type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button> -->
												
											</td>
										</tr>	
										</c:forEach>	
										<c:forEach var="getdeletedProductDetails" items="${requestScope['deletedProductDetailsList']}">
											
										<tr id="tr-class${getdeletedProductDetails.strSerialNumber}" class="strikeout">
											<td>						
												<div  id="snoDivId${getIndentCreationDetails.strSerialNumber}">${getIndentCreationDetails.strSerialNumber}</div>
											</td>
											<td>
												<select id="combobox${getdeletedProductDetails.strSerialNumber}" name="Product${getdeletedProductDetails.strSerialNumber}"  class="form-control btn-visibilty${getdeletedProductDetails.strSerialNumber}" readonly>
													<option value="${getdeletedProductDetails.productId1}$${getdeletedProductDetails.product1}">${getdeletedProductDetails.product1}</option> 
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
												<select name="SubProduct${getdeletedProductDetails.strSerialNumber}"  id="comboboxsubProd${getdeletedProductDetails.strSerialNumber}" class="form-control btn-visibilty${getdeletedProductDetails.strSerialNumber}" readonly>
												<option value="${getdeletedProductDetails.subProductId1}$${getdeletedProductDetails.subProduct1}">${getdeletedProductDetails.subProduct1}</option>
												</select>
											</td>
											<td>
												<select name="ChildProduct${getdeletedProductDetails.strSerialNumber}" id="comboboxsubSubProd${getdeletedProductDetails.strSerialNumber}" class="form-control btn-visibilty${getdeletedProductDetails.strSerialNumber}" readonly>
												<option value="${getdeletedProductDetails.childProductId1}$${getdeletedProductDetails.childProduct1}">${getdeletedProductDetails.childProduct1}</option>
												</select>
											</td>
											<td>
												<select name="UnitsOfMeasurement${getdeletedProductDetails.strSerialNumber}" id="UnitsOfMeasurementId${getdeletedProductDetails.strSerialNumber}" class="form-control btn-visibilty${getdeletedProductDetails.strSerialNumber}" onchange="return validateProductAvailability(this);"  readonly>
												<option value="${getdeletedProductDetails.unitsOfMeasurementId1}$${getdeletedProductDetails.unitsOfMeasurement1}">${getdeletedProductDetails.unitsOfMeasurement1}</option>
												</select>
											</td>
											<td class="w-70">
												<input  id="RequiredQuantity${getdeletedProductDetails.strSerialNumber}" name="RequiredQuantity${getdeletedProductDetails.strSerialNumber}" readonly="true" title="${getdeletedProductDetails.requiredQuantity1}" value="${getdeletedProductDetails.requiredQuantity1}" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/>
												<%-- <input name="ProductAvailability${getdeletedProductDetails.strSerialNumber}" readonly="true" value="${getdeletedProductDetails.productAvailability1}" class="form-control input-visibilty${getdeletedProductDetails.strSerialNumber}"/> --%>
											</td>
										
											<td>
												<a><input name="Remarks${getdeletedProductDetails.strSerialNumber}" href="#" data-toggle="tooltip" title="${getdeletedProductDetails.remarks1}"  readonly="true" value="${getdeletedProductDetails.remarks1}" class="form-control input-visibilty${getdeletedProductDetails.strSerialNumber}"/></a>
											
											</td>
											<td>
											
											 <input name="purchaseDeptRemarks${getIndentCreationDetails.strSerialNumber}" readonly="true"   value="<c:out value="${getIndentCreationDetails.type_Of_Purchase}"/>" data-toggle="tooltip" title="${getIndentCreationDetails.type_Of_Purchase}"  readonly="true" class="form-control input-visibilty${getIndentCreationDetails.strSerialNumber}"/> 
											</td> 
														<td>
												<%-- <input id="Comments${getdeletedProductDetails.strSerialNumber}"  name="Comments${getdeletedProductDetails.strSerialNumber}" readonly="true"  class="form-control input-visibilty${getdeletedProductDetails.strSerialNumber}"/>
												<input type="hidden" name="IsComments" value="" id="hiddenCommentsId">
											 --%>
											
												
											</td>	
										 			
										 	<td>
										 		
											</td>
										</tr>	
										</c:forEach>		
									</table>
									</div>
								  </div>
			
			<div class="row"> <!-- tblindentissunote -->
				 <c:forEach var="getIndentCreation" items="${requestScope['IndentCreationList']}">
					<div class="col-md-6">
					 <div class="form-group">
					 <label class="col-md-4 pt-10">
						<%= purpose %> <%= colon %>
					</label>
					<div class="col-md-8">
						 <form:textarea path="Purpose" id="PurposeId" class="form-control tb_notes resize-vertical" placeholder="write ur purpose here"/>
						 ${getIndentCreation.purpose}
					</div>
					</div>
					</div>
				</c:forEach>
				
			</div>
			<!--  	<div class="col-sm-2 pt-10">
						<input type="button" class="btn btn-warning"value="Approve" id="saveBtnId" onclick="saveRecords('SaveClicked')">
					</div>-->
			<!--	<div class="col-sm-2 pt-10">
						<input type="button" class="btn btn-warning"value="Reject" id="saveBtnId" onclick="reject('SaveClicked')">
					</div>-->
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" id="materialEditComment" name="materialEditComment" value="${requestScope['materialEditComment']}" />
				  
				  
	        <div class="col-md-6 Mrgtop10">
	         <div class="form-group">
	          <label class="control-label col-md-6" >Note: </label>
			<div class="col-md-6" >
				<form:textarea path="" href="#" data-toggle="tooltip" title="${IndentLevelCommentsList}"   id="NoteId" name="Note" class="form-control resize-vertical" autocomplete="off" placeholder="${IndentLevelCommentsList}"></form:textarea></br>
			</div>
	         </div>
	        </div>
	        <div class="clearfix"></div>		 
			<div class="col-md-6">
			 <div class="col-md-4"><h2><strong>Modification Details:</strong></h2></div>
			 <div class="col-md-8">
			  <c:forEach var="indentCreationComments" items="${requestScope['materialEditCommentList']}" step="1" begin="0">
				  	<div class="footer-cont" style="">
	        	 	<ul>	
	        	 		
	        	 	 <li class="check_list" name="editComments" style="position: relative;margin-top: 48px;margin-left: -20px;   margin-bottom: 16px;" >
	        	 	    ${indentCreationComments.materialEditComment}</br>
	        	 	   </li>
			         </ul>
			         </div>
			       
				</c:forEach>
			 </div>
					
			</div>
				
				
		</form:form>
		
	</div>
	
    
	
	<% 
	String reponse=request.getAttribute("print").toString();
	if(reponse=="true"){%>
	 <div class="col-md-12 text-center center-block pt-10"> 
<input type="button" class="btn btn-warning"value="Print Indent" id="saveBtnId" onclick="printIndent()">
	
	
		<% }else {%>
		
		<input type="hidden" class="btn btn-warning"value="Print Indent" id="saveBtnId" onclick="printIndent()">
		
	<%
		}
	%>
	</div>	
	</div>
	<!-- /page content -->        
</div>
</div>


<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
  <script>
		
  function printIndent() {
		
		$(".loader-sumadhura").show();
		document.getElementById("indentCreationDetailsShowFormId").action = "printIndent.spring";
		document.getElementById("indentCreationDetailsShowFormId").method = "POST";
		document.getElementById("indentCreationDetailsShowFormId").submit();
	}
  
  
			$(document).ready(
					/* $(function() {
for(i=1;i<=100;i++){	$("#combobox"+i).combobox();    
					    $( "#toggle").click(function() { $( "#combobox"+i).toggle();  });
					      $( "#comboboxsubProd"+i).combobox();
							$("#comboboxsubSubProd"+i).combobox(); 
								$("#UnitsOfMeasurementId"+i).combobox(); }
					    
					  }) */
					$(function() {	
				
			
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
				$('.btn-visibilty'+i).closest('td').find('input').attr('disabled', 'disabled');
			    $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide');
			    }
			}));
			
			
			$(document).ready(function(){
			    $('[data-toggle="tooltip"]').tooltip();   
			});
			function myFunction() {
			    window.print();
			};
			
		</script> 
</body>
</html>
