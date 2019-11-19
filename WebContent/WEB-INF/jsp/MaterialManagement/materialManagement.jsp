<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import = "java.util.ResourceBundle" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
	String tableHead = resource.getString("label.indentIssueTableHead");
	String colon = resource.getString("label.colon");
	String makeWorkOrderNoMandatory=resource.getString("workOrderNoMandatoryForIndentIssue");
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
	String WorkOrderNo=resource.getString("label.WorkOrderNo");
	String isRecoverable=resource.getString("label.isRecoverable");
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
<script src="js/materialManagement.js" type="text/javascript"></script>
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
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		<jsp:include page="../CacheClear.jsp" />  
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.form-control{height:34px;}
.input-group-addon{border:1px solid #ccc !important;}
#indentIssueTableId thead, #indentIssueTableId tbody tr{table-layout:fixed;display:table;width:100%;}
#indentIssueTableId tbody tr td:first-child, #indentIssueTableId thead tr th:first-child {min-width: 20px;text-align: center;width:50px;}
#indentIssueTableId tbody tr td{border-top:none !important;}
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
           // alert("ele"+ele);
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
           // alert("After removing numbers = "+str1);
            
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
	  var staffId=$("#hiddenPODate").val();
	  $("#ReqDateId").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate: '-'+ staffId+'d',
		maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
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
	  var isRecoverable =  "<%= isRecoverable %>";
	  isRecoverable = formatColumns(isRecoverable);
	 
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
		  /*   div.setAttribute("onkeydown", "appendRow()"); */
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
    	/*  else if(tableColumnName == isRecoverable) {
    		 var dynamicSelectBoxId = "isRecoverable"+vfx;
     		//alert(dynamicSelectBoxId);    		
     		var div = document.createElement("select");
     	    div.setAttribute("name", tableColumnName+vfx);
     	    div.setAttribute("id", dynamicSelectBoxId);
     	    div.setAttribute("class", 'form-control');
     	   var option1 = document.createElement("option");
     	    option1.text = "Select";
     	    option1.value = "N/A";
   	        div.appendChild(option1); 
   	       var option2 = document.createElement("option");
   	    	option2.text = "Yes";
   	    	option2.value = "Yes";
	        div.appendChild(option2); 
	        var option3 = document.createElement("option");
	        option3.text = "No";
	        option3.value = "No";
	        div.appendChild(option3); 
     	    cell.appendChild(div);    	    
     	       	    		    
   		}  */
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

//Validation started 
function validateRowData() {
  
      	var reqId = "<%= reqId %>";
      	reqId = formatColumns(reqId)+"Id";
      	reqId = '"'+reqId+'"';
      	reqId = reqId.replace(/\"/g, "");
      	//alert(reqId);
      	
      	var vendorissuedTypeid=document.getElementById("vendorissuedTypeid").value;
    	
    	if(vendorissuedTypeid==""){
    		alert("Please select indent type.");
    		document.getElementById("vendorissuedTypeid").focus();
    		return false;
    	}
    	var vehicleNumberid=document.getElementById("vehicleNumberid").value;
		/* var regex=/^([A-Z]{2,3})-(\d{2,4})\w|([A-Z]{2,3})-\d{2}-[A-Z]{1,2}-\d{1,4}\w/;
		 if (!regex.test(vehicleNumberid)) {
			alert("Please enter valid vehicle number.");
			return false;
		} */ 
 
      	
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
	  	
   		/* var slipIdVal = document.getElementById(slipId).value;
   		
		if(slipIdVal == "" || slipIdVal == null || slipIdVal == '') {
			alert("Please enter Slip Number.");
			document.getElementById(slipId).focus();
			return false;
		} */
		
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

						<jsp:include page="../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="../TopMenu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Issue</a></li>
							<li class="breadcrumb-item active">New Issue</li>
						</ol>
					</div>
					
					
					<div>
 <div align="center">
	<form:form modelAttribute="indentIssueModelForm" id="indentIssueFormId" class="form-horizontal">
	  <div class="border-indent">
		<span style="color:red;">${noStock}</span><br/>
		 <div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
				  <label class="control-label col-md-6 col-sm-6 col-xs-12">Issue Type :</label>
				  <div class="col-md-6 col-sm-6 col-xs-12">
				   <select id="vendorissuedTypeid" name="vendorissuedTypeName" class="form-control">
				     <option value="">--Select--</option>
				     <option value="OUTR">Repair</option>
				     <option value="OUTS">Scrap</option>
				     <option value="OUTT">Loss-Theft</option>
				   </select>
				  </div>
			  </div>			
		 </div>
		
		
		   <div class="col-md-6 col-xs-12 col-sm-6">
		     <div class="form-group">
				<label class="control-label col-md-6 col-xs-12 col-sm-6"><%= reqId%> <%= colon %> </label>
				<div class="col-md-6 col-xs-12 col-sm-6">
					<form:input path="ReqId" id="ReqIdId"  readonly="true" class="form-control"/>
				</div>
			</div>
		   </div>
			<div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
				<label class="control-label col-md-6 col-xs-12 col-sm-6 "><%= date%> <%= colon %> </label>
				<div class="col-md-6 col-xs-12 col-sm-6 input-group">
					<form:input path="ReqDate" id="ReqDateId" class="form-control" autocomplete="off"/>
					<label class="input-group-addon btn" for="ReqDateId">	<span class="fa fa-calendar"></span></label>
				</div>
		      </div>
			</div>
	  		<div class="col-md-6 col-xs-12 col-sm-6">
			    <div class="form-group">
					<label class="control-label col-md-6 col-xs-12 col-sm-6">Employee Name <%= colon %>  </label>
					<div class="col-md-6 col-xs-12 col-sm-6">
						<form:input path="RequesterName" id="RequesterNameId"  onkeyup="populateEmployee(this);"  autocomplete="off" class="form-control"/>
					</div>
			   </div>
			</div>
			<div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
					<label class="control-label col-md-6 col-xs-12 col-sm-6">Employee ID <%= colon %> </label>
					<div class="col-md-6 col-xs-12 col-sm-6">
						<form:input path="RequesterId" id="RequesterIdId" class="form-control"/>
					</div>
		     </div>
		   </div>		   
	  	  <%-- <div class="col-md-6">
			  <div class="form-group">
					<label class="control-label col-md-6"><%= projectName %> <%= colon %>  </label>
					<div class="col-md-6" >
						<form:input path="ProjectName" id="ProjectNameId"  readonly="true" class="form-control" autocomplete="off"/>
					</div>
			 </div>
		  </div>
		  <div class="col-md-6">
			  <div class="form-group">
					<label class="control-label col-md-6"><%= block %> <%= colon %> </label>
					<div class="col-md-6" >
						<form:select path="Block" id="BlockId" onchange="populateFloor(this)" class="form-control">
			  					<form:option value="">--Select--</form:option> 
			  				<%
				    			Map<String, String> blocksMap = (Map<String, String>)request.getAttribute("blocksMap");
				    			for(Map.Entry<String, String> blocks : blocksMap.entrySet()) {
									String availableBlocks = blocks.getKey()+"$"+blocks.getValue();
							%>
									<form:option value="<%= availableBlocks %>"><%= blocks.getValue() %></form:option>
				    		<% } %>
							</form:select>
			  		</div>
	  			</div>
	  	  </div>
	  	  <div class="col-md-6">
			 <div class="form-group">
			    <label class="control-label col-md-6"><%= floor %> <%= colon %> </label>
				<div class="col-md-6" >
					<form:select path="Floor" id="FloorId" onchange="populateFlat(this)" class="form-control">
		  						<form:option value="">--Select--</form:option>
				    </form:select> 
				</div>
			  </div>
		 </div>
		 <div class="col-md-6">
		  <div class="form-group">
				<label class="control-label col-md-6"><%= flatNumber %> <%= colon %> </label>
				<div class="col-md-6" >
					<form:select path="flatNumber" id="FlatNumberId"  class="form-control">
		  			<form:option value="">--Select--</form:option>
				    </form:select> 
				</div>
		   </div>
		 </div>
		 <div class="col-md-6">
			<div class="form-group">
				<label class="control-label col-md-6"><%= slipNumber %> <%= colon %>  </label>
				<div class="col-md-6" >
					<form:input path="SlipNumber" id="SlipNumberId"   class="form-control" onblur="return validateSlipNumbers(this, event);"  autocomplete="off"/>
			         <span id="errorMessageInvoiceNumber" style="display:none; color:red">* Already exist the slip number</span>
		        </div>
	        </div>
	     </div>
	     <div class="col-md-6">
			  <div class="form-group">
				<label class="control-label col-md-6"><%= ContractorName %> <%= colon %>  </label>
				<div class="col-md-6" >
				 	<form:input path="ContractorName" id="ContractorNameId"  onkeyup="return populateData();"  autocomplete="off" class="form-control"/> 
				<input type="hidden" name="contractorId" id="contractorId">
				<!--	<form:input path="ContractorName" id="ContractorNameId"    autocomplete="off" class="form-control"/>-->
				</div>
				<input type="hidden" id="hiddenPODate"  name="hiddenPODate" value="${noOfDays}"/>
			</div>
		 </div>
			 --%>
		  <div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
				  <label class="control-label col-md-6 col-xs-12 col-sm-6">Vendor Name :</label>
				  <div class="col-md-6 col-xs-12 col-sm-6">
				   <input type="text"  id="VendorNameId" name="vendorissuedName" class="form-control"/>
				   
				  </div>
			  </div>
			
		  </div>
		  
		  	<div class="col-md-6 col-xs-12 col-sm-6">
			<div class="form-group">
			<label class="control-label col-md-6 col-xs-12 col-sm-6"><%= ContractorName %> <%= colon %>  </label>
			<div class="col-md-6 col-xs-12 col-sm-6">
				 	<form:input path="ContractorName" id="ContractorNameId"  onkeyup="return populateData();"  autocomplete="off" class="form-control"/> 
			<input type="hidden" name="contractorId" id="contractorId">
				<!--	<form:input path="ContractorName" id="ContractorNameId"    autocomplete="off" class="form-control"/>-->
			</div>
				<input type="hidden" id="hiddenPODate"  name="hiddenPODate" value="${noOfDays}"/>
				<input type="hidden" id="moduleName"  name="moduleName" value="MaterialManagement"/>
				 <input type="hidden" name="makeWorkOrderNoMandatory" id="makeWorkOrderNoMandatory" value="<%=makeWorkOrderNoMandatory%>">
				 <input type="hidden" name="isThisSiteWiseModule" id="isThisSiteWiseModule" value="${isThisSiteWiseModule}">
			</div>
		</div>
		  
		  <div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
				  <label class="control-label col-md-6 col-xs-12 col-sm-6">Address :</label>
				  <div class="col-md-6 col-xs-12 col-sm-6">
				   <input type="text"  id="VendorAddress" name="vendorissuedaddressName" class="form-control" title="" />
				  
				  <input type="hidden" name="VendorId" id="vendorIdId" />
				  </div>
			  </div>			
		 </div>
		
		 <div class="col-md-6 col-xs-12 col-sm-6">
			  <div class="form-group">
				  <label class="control-label col-md-6 col-xs-12 col-sm-6">Vehicle Number :</label>
				  <div class="col-md-6 col-xs-12 col-sm-6">
				    <input type="text" class="form-control" id="vehicleNumberid" name="vehicleNo" placeholder="Ex. AP-33-TV-2223" autocomplete="off" title="Ex. AP-33-TV-2223">
				  </div>
			  </div>			
		</div>
		
	
		<div class="col-md-6 col-xs-12 col-sm-6">
			 <div class="form-group">
			   <label class="control-label col-md-6 col-xs-12 col-sm-6"><%= WorkOrderNo %> <%= colon %>  </label>
				<div class="col-md-6 col-xs-12 col-sm-6" >
				 	<%-- <form:input path="WorkOrderNo" id="WorkOrderNo"  autocomplete="off" class="form-control"/> --%>
				 	<input type="hidden" name="SiteId" id="SiteId" value="${SiteId}"> 
					<form:select  path="workOrderNo" id="workOrderNo" class="rabill-class-select form-control" > 
					</form:select>
				</div>
			 </div>
	  	 </div>  
	  </div>
	  
	 <div class="table-responsive">
		<table id="indentIssueTableId" class="table tblprodindissu table-new" style="width:2300px;">
				<thead class="cal-thead-inwards">
				 <tr>
					<th><%= serialNumber %></th>
    				<th><%= product %></th>
    				<th><%= subProduct %></th>
    				<th><%= childProduct %></th>    				
    				<th><%= measurement %></th>
    			<%-- 	<th><%= productAvailability %></th> --%>
    				<th  class="w-70"><%= quantity %></th>
    				<%-- <th><%= hsnCode %></th> --%>
    				<th><%= uOrF %></th>
    				<%-- <th><%=isRecoverable %></th> --%>
    				<th><%= remarks %></th>
    				<th><%= actions %></th>
  				</tr>
				</thead>
				<tbody class="tbl-fixedheader-tbody">
				   <tr id="productRow1" class="productRowCls">
					<td>						
						<div id="snoDivId1">1</div>
					</td>
					<td>
						<select id="combobox1" name="Product1" >
							<option value="">--Select--</option>
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
						<form:input path="Quantity1" id="QuantityId1"  onkeypress="return validateNumbers(this, event);" onblur="return validateUnitsAndAvailability(this);" class="form-control" autocomplete="off"/>
						<form:input path="ProductAvailability1" id="ProductAvailabilityId1" readonly="true" class="form-control"/>
					</td>
					<td>
						<form:input path="UOrF1" id="UOrFId1"   class="form-control"/>
					</td>
					<%-- <td>
						<form:input path="isRecoverable" id="isRecoverable" class="form-control"/>
						<form:select path="isRecoverable1" class="form-control">
							<form:option value="N/A">Select</form:option>
						<form:option value="Yes">Yes</form:option>
						<form:option value="No">No</form:option>
						</form:select>
					</td>	 --%>
					<td>
						<form:input path="Remarks1" id="RemarksId1"   class="form-control"/>
					</td>	
								
					<td>
						<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button style="display:none;" type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button>
					</td>
				</tr>	
				</tbody>			
			</table>
			</div>
			
			<div class="col-md-6 Mrgtop10"> <!-- tblindentissunote -->
				
					<label class="col-md-4">
						<%= purpose %> <%= colon %>
					</label>
					<div class="col-md-8">
						 <form:textarea path="Purpose" id="PurposeId" class="form-control tb_notes"/>
					</div>
				
				
			</div>
				
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
		</form:form>
		
		<div class="col-md-12 text-center center-block" >
				<input type="button" class="btn btn-warning" value="Submit" id="saveBtnId" onclick="saveRecords('SaveClicked')">
		</div>
	</div>
	
	</div>
	<!-- /page content -->        
</div>
</div>
</div>

<script src="js/custom.js"></script>
<script src="js/sidebar-resp.js"></script>
  <script>
  
  
  /********** Method for load the Contractors**************/
/*   function populateData() {
  	debugger;
  var contName=$("#ContractorNameId").val();
   var url = "loadAndSetContractorInfo.spring?contractorName="+contName;

    $.ajax({
    url : url,
    //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
    type : "get",
    Cdata : "",
    contentType : "application/json",
    success : function(data) {
  	  console.log(data);
  	  Cdata = data.split("@@");
  	  
  	  var resultArray = [];
  	  for(var i=0;i<Cdata.length;i++){
  	      resultArray.push(Cdata[i].split("@@")[0]);
  	  }
    		$("#ContractorNameId").autocomplete({
  	  		source : resultArray,
  	  		change: function (event, ui) {
                  if(!ui.item){
                 	//if list item not selected then make the text box null	
                 	 $("#ContractorNameId").val("");
                  }
                },
  	  		select: function (event, ui) {
                  AutoCompleteSelectHandler(event, ui);
              }

  	  	});
    },
    error:  function(data, status, er){
  	 // alert(data+"_"+status+"_"+er);
  	  }
    });

  //code for selected text
  	function AutoCompleteSelectHandler(event, ui)
  	{               
  	    var selectedObj = ui.item;       
  	    isTextSelect="true";
  	  
  	  var contractorName=selectedObj.value;
  	 
  		 var url = "loadAndSetVendorInfoForWO.spring";
  		 $.ajax({
  			  url : url,
  			  type : "get",
  			 data:{
  				 contractorName:contractorName,
  				 loadcontractorData:true	 
  			 },
  			  contentType : "application/json",
  			  success : function(data) {
  				  $("#contractorName").val(contractorName);
  				  if(data!=""||data!="null"){
  					debugger;
  					var contractorData=data[0].split("@@");
  					var contractorId=contractorData[0];
  					
  					$("#contractorId").val(contractorId);
  					var str="<option value='0'>Select Work Order No</option>";
  					var siteid=$("#SiteId").val();
  					$.ajax({
  						url:"getWorkOrderNo.spring?workDescId=0&siteId="+siteid+"&contractorId="+contractorId+"&typeOfWork=PW@@NMR",
  						type : "POST",
  						contentType : "application/json",
  						success:function(resp){
  							debugger;	
  							var array=new Array();
  							array=resp.split("|");
  							for (var i = 0; i < array.length; i++) {
  								debugger;
  								var array_element = array[i].split("_");
  								if(array_element!="")
  								str+="<option value="+array_element[0]+">"+array_element[0]+"-"+array_element[3]+"</option>";
  							}
  							$("#workOrderNo").html(str);
  						
  						}
  					});
  					}
  			  },
  			  error:  function(data, status, er){
  				  alert(data+"_"+status+"_"+er);
  				  }
  			  });
	  		 var available = new Array();
  	}

  }; */
  
		
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
		debugger;
		if(request.readyState == 4 && request.status == 200) {
		
			debugger;
			var resp = request.responseText;
			resp = resp.trim();
			//alert(resp);
			var vendorId = resp.split("|")[0];
			var vendorAddress = resp.split("|")[1];
			var vendorGsinNo = resp.split("|")[2];
			
			$("#vendorIdId").val(vendorId);
			$("#VendorAddress").val(vendorAddress);
			$("#GSTINNumber").val(vendorGsinNo);			
		}
	}
  
		$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
			});
			
			
			
$(document).ready(function() {	
	  $(function() {
	  	$('#VendorNameId').keypress(function () {
		debugger;
	  	$.ajax({
		  url : "./getVendorDetails.jsp",
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "get",
		  data : "",
		  contentType : "application/json",
		  success : function(data) {
		  		$("#VendorNameId").autocomplete({
			  		source : data
			  	});
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
	  	});
	  	$('#VendorNameId').on('change', function(){
	  		debugger;
	  		var value = $(this).val();
	  		value = value.replace("&", "$$$");
	  		setVendorData (value); //pass the value as paramter
		 });
	  });
	  
			  $("#vendorissuedTypeid").on('change',  function() {
					  var indentType=$(this).val();
					  $("#VendorNameId").val("");
					  $("#VendorAddress").val("");
					  $("#ContractorNameId").val("");
					  debugger;
					  if(indentType=="OUTR"){
						  $("#VendorNameId").attr("readonly",true);
						  $("#VendorAddress").attr("readonly",true);
						  $("#ContractorNameId").attr("readonly",false);
					  }else if(indentType=="OUTS"){
						  $("#ContractorNameId").attr("readonly",true);
						  $("#VendorNameId").attr("readonly",false);
						  $("#VendorAddress").attr("readonly",false);
					  }else if(indentType=="OUTT"){
						  $("#ContractorNameId").attr("readonly",true);
						  $("#VendorNameId").attr("readonly",true);
						  $("#VendorAddress").attr("readonly",true);
					  }
			  });
	  

				 $('#RequesterNameId').on('change',  function() {
				 
				 var empname=$("#RequesterNameId").val();
				
				 $.ajax({
					 url: "./getEmployerid.spring?employeeName="+empname,
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
						 $("#RequesterNameId").val(data);
					 },
					 error:  function(data, status, er){
						  alert(data+"_"+status+"_"+er);
						  }
					  });
				  	});

				
			});
		</script> 
</body>
</html>