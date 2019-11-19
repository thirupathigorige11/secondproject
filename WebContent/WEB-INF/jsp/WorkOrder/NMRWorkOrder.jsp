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
	String TempWorkOrderNo = resource.getString("label.TempWorkOrderNo");
	String WorkOrderDate = resource.getString("label.WorkOrderDate");
	String panCardNo = resource.getString("label.panCardNo");
	String Address = resource.getString("label.Address");
	String phoneNo = resource.getString("label.phoneNo");
	String vendorName = resource.getString("label.contractorname");
	String WO_MajorHead = resource.getString("label.WO-MajorHead");
	String WO_MinorHead = resource.getString("label.WO-MinorHead");
	String WO_Desc = resource.getString("label.WO-Desc");
 	String scope_Of_work= resource.getString("label.scope-Of-work");//not used
 	String manualdesc= resource.getString("label.manualdesc");
	String amountInCurrency= resource.getString("label.amountInCurrency");
 	String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency");
	String workOrderDateSelection=resource.getString("label.workOrderDateSelection");
	String measurement = resource.getString("label.UOM");
	String AcceptedRate = resource.getString("label.AcceptedRate");
	String TotalAmount = resource.getString("label.TotalAmount");
	
	//Loading Indent Issue Table Column Headers/Labels - Start
%>

<html>
<head>
<!-- <script src="js/WorkOrder/workorder.js" type="text/javascript"></script> -->
<script src="js/WorkOrder/NMRworkorder.js" type="text/javascript"></script>
<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>

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
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>

<!-- <title>Create WorkOrder </title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">


<script type="text/javascript">
function formatColumns(colName) {
    var colNm = colName.replace(/ /g,'');
    return colNm.replace(/\./g,'');
}
 
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
            
     
            debugger; 
            WOMajorHeadId = ui.item.option.value;
            WOMajorHeadName = ui.item.value;
            
            var ele = this.element[0].name;
            //alert(ele);
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            //alert("After removing numbers = "+str1);
        
	        var WO_MajorHead =  "<%= WO_MajorHead %>";
	        WO_MajorHead = formatColumns(WO_MajorHead);
		    //alert(WO_MajorHead);
		  	 
		  	var WO_MinorHead =  "<%= WO_MinorHead %>";
		  	WO_MinorHead = formatColumns(WO_MinorHead);
		 //	alert(WO_MinorHead);
		  
		 	var Wo_WorkDesc =  "<%= WO_Desc %>";
		 	Wo_WorkDesc = formatColumns(Wo_WorkDesc);
		 	
		 	
			var measurement =  "<%= measurement %>";
			measurement = formatColumns(measurement);
		 	
		 	
			//alert(childProductColumn);
			
			var rowNum = ele.match(/\d+/g);
			//alert(rowNum);
			debugger;             
            if(str1 == WO_MajorHead) {
            	WOMajorHeadId = ui.item.option.value;
            	WOMajorHeadName = ui.item.value;
              //  debugger;
                loadSubProds(WOMajorHeadId, rowNum);
                this._trigger( "select", event, {
                    item: ui.item.option
                  });
            }            
            else if(str1 == WO_MinorHead) {
            	WOMinorHeadId = ui.item.option.value;
            	WOMinorHeadName = ui.item.value;
             //   debugger;
                loadSubSubProducts(WOMinorHeadId, rowNum);
                this._trigger( "select", event, {
                    item: ui.item.option
                  });
            }
            else if(str1 == Wo_WorkDesc) {
            	debugger;
            	WOWorkDescId = ui.item.option.value;
            	Wo_WorkDesc = ui.item.value;
            	$("#WO_Desc"+rowNum).attr("class", "ui-autocomplete-input WorkDesc");
            	 loadUnits(WOWorkDescId, rowNum);
          //      loadScopeOfWork(WOWorkDescId, rowNum);
            /*  var valStatus=childcampare(Wo_WorkDesc, rowNum)
               if(valStatus==false){
            	   return false;
               }else{
            	   loadUnits(WOWorkDescId, rowNum);
            	   this._trigger( "select", event, {
                       item: ui.item.option
                     });
                   
               } */
                
            } else if(str1 == measurement) {
            	WOUOMId = ui.item.option.value;
            	WOUOMName = ui.item.value;
                debugger;
                //loadWOWorkArea(WOUOMId, rowNum);
                this._trigger( "select", event, {
                    item: ui.item.option
                  });
            }
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

  var now=new Date();
 var yesterdayMs = now.getTime() - 1000*60*60*24*<%=workOrderDateSelection%>; // Offset by one day;
 now.setTime( yesterdayMs );
 
  $(function() {
	  var staffId=$("#hiddenPODate").val();
	  $("#WorkOrderDate").datepicker({
		  dateFormat: 'dd-mm-yy',
		  minDate:new Date(),
		//maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
	  });
  });
  
</script>
<script>
/* $("#scopeOfWork1").focus(function() {
	debugger;
	  alert( "Handler for .focus() called." );
	}); */
	
	function myscopefocus(id){
		 $("#hiddenrownum").val(id); 
		$("#modalForScopeWork"+id).modal();
	}
</script>
<script type="text/javascript">
//var delOne;

//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	debugger;
	 //alert(cell+"-->"+text+"-->"+style+"-->"+fldLength+"-->"+cellsLen+"-->"+tableColumnName);
	
     //var temp = cellsLen-1;
     var vfx = fldLength;
     //alert(style);
     //if(text == 0 || text == '0') {
    	
     //alert(tableColumnName);
    
     var snoColumn =  "<%= serialNumber %>";
     snoColumn = formatColumns(snoColumn);
     //alert(snoColumn);
     
     var WO_MajorHead =  "<%= WO_MajorHead %>";
     WO_MajorHead = formatColumns(WO_MajorHead);
     //alert(productColumn);
  	 
  	 var WO_MinorHead =  "<%= WO_MinorHead %>";
  	WO_MinorHead = formatColumns(WO_MinorHead);
 	 //alert(subProductColumn);
     
 	 var WO_Desc =  "<%= WO_Desc %>";
 	 WO_Desc = formatColumns(WO_Desc);
	 //alert(childProductColumn);
	 
	 var manualdesc =  "<%= manualdesc %>";
	 manualdesc = formatColumns(manualdesc);
	 
	 var measurement =  "<%= measurement %>";
	 measurement = formatColumns(measurement);
	 //alert(measurementColumn);
	 
	 var productAvailabilityColumn =  "<%= productAvailability %>";
	 productAvailabilityColumn = formatColumns(productAvailabilityColumn);
	 //alert(productAvailabilityColumn);
	 
	 var quantityColumn =  "<%= quantity %>";
	 quantityColumn = formatColumns(quantityColumn);
	 //alert(quantityColumn);
	 
	 var AcceptedRate =  "<%= AcceptedRate %>";
	 AcceptedRate = formatColumns(AcceptedRate);
	 //alert(uOrFColumn);
	 
	 var TotalAmount =  "<%= TotalAmount %>";
	 TotalAmount = formatColumns(TotalAmount);
	 //alert(remarksColumn);
	 
	  var note =  "<%= note %>";
	  note = formatColumns(note);
	 
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
    	if(tableColumnName == WO_MajorHead) {
    		var dynamicSelectBoxId = "combobox"+vfx;
    		//alert(dynamicSelectBoxId);
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
    		
    	    cell.appendChild(div); 
    	    <% 
			Map<String, String> products = (Map<String, String>)request.getAttribute("workMajorHead");
			for(Map.Entry<String, String> prods : products.entrySet()) {
			String prodName=prods.getValue().replace("\"","#");
			String prodName1=prods.getValue().replace("\"","'");
			String val = prods.getKey()+"$"+prodName;
		%>
		try{
			option = document.createElement("option");
			var tempval="<%=prodName1%>";
			
    	    option.text = tempval;
    	    option.value = "<%= val %>";
		}catch(e){
			console.log(e);
		}
    	    <%--  option="<option value="<%= val %>">"<%= prods.getValue() %>"</option>"; --%>
    	    div.appendChild(option);
		<% 
			} 
		%>
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}
    	else if(tableColumnName == WO_MinorHead) {
    		
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
    	else if(tableColumnName == WO_Desc) {
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
    	
/*     	else if(tableColumnName == manualdesc) {
    		//alert(manualdesc);  
    	
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "woManualDesc"+vfx);
		    div.setAttribute("id", "woManualDesc"+vfx);
		    div.setAttribute("onfocus", "myscopefocus("+vfx+")");		
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);		

		  //  cell.appendChild(div1);	
   		}
    	  */
    	else if(tableColumnName == measurement) {
    		//var dynamicSelectBoxId = tableColumnName+"Id"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", "UnitsOfMeasurement"+vfx);
    	    div.setAttribute("id", "UOMId"+vfx);
    	//   div.setAttribute("onchange", "return validateProductAvailability(this);");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	}   	
    	else if(tableColumnName == quantityColumn) {
			
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		 //   div.setAttribute("readonly", true);
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		  /*   div.setAttribute("onkeydown", "appendRow()"); */
		    div.setAttribute("onkeyup", "validateNumbers(this.value, '"+vfx+"');");
	        div.setAttribute("onkeypress","return isNumberCheck(this, event)");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("autocomplete", "off");
		    cell.appendChild(div);
	
		    var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderArea'+vfx);
			 cell.append(divNew);
			 
			 var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderWorkArea'+vfx);
			 cell.append(divNew);
   		}else if(tableColumnName == AcceptedRate) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", AcceptedRate+vfx);
		    div.setAttribute("id", AcceptedRate+"Id"+vfx);
		    div.setAttribute("onkeypress","return isNumberCheck(this, event)");
		    div.setAttribute("onkeyup", "calCulateTotalAmout(this.value,'"+vfx+"')");
		    div.setAttribute("class", 'form-control');
		    div.setAttribute("autocomplete", "off");
		    cell.appendChild(div);			    
   		}  
    	else if(tableColumnName == TotalAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("class", 'form-control TotalNMRBOQ_Amount');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == note) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    //div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("name", "comments"+vfx);
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
	debugger;
  
     
  	  
 	  	var reqDate = "<%= WorkOrderDate %>";
 	  	reqDate = formatColumns(reqDate);
 	  	reqDate = '"'+reqDate+'"';
 	  	reqDate = reqDate.replace(/\"/g, "");
 	  	//alert(reqDate);
 	  	
 	  	var reqDateVal = document.getElementById(reqDate).value;
 	  	
 		if(reqDateVal == "" || reqDateVal == null || reqDateVal == '') {
			alert("Please select Date.");
			document.getElementById(reqDate).focus();
			return false;
	  	} 
 	  	//validation written by thirupathi
	  	var WorkOrderName=$("#workOrderName").val();
	  	var ContractorName =$("#contractorName").val();
	  	
	  
	  	/* if(WorkOrderName == "" || WorkOrderName == null || WorkOrderName == '') {
			alert("Please Enter WorkOrder Name.");
			$("#workOrderName").focus();
			return false;
	  	}  */
	  	if(ContractorName == "" || ContractorName == null || ContractorName == '') {
			alert("Please Enter Contractor Name.");
			$("#contractorName").focus();
			return false;
	  	} 
 	  
	  //validation by thirupathi
		
		var elementList = document.getElementsByTagName("*");
      	
      	var rowNums = getAllProdsCount();
      	
      	var splitedRows = rowNums.split("|");
      	
      	for(var x=0; x < rowNums.length; x++) {
      		
      		var curRow = splitedRows[x];
		
		
		 <%-- var ContractorId = "<%= ContractorName %>";
    	   ContractorId = formatColumns(ContractorId)+"Id";
    	   ContractorId = '"'+ContractorId+'"';
    	   ContractorId = ContractorId.replace(/\"/g, ""); --%>
    	  
      		
      		var product = "<%= WO_MajorHead %>";
      	  	product = formatColumns(product)+curRow;
      	  	product = '"'+product+'"';
      	  	product = product.replace(/\"/g, "");
      	 	
      	  
      	  	var subProduct = "<%= WO_MinorHead %>";
      	  	subProduct = formatColumns(subProduct)+curRow;
      	  	subProduct = '"'+subProduct+'"';
      	  	subProduct = subProduct.replace(/\"/g, "");
      	  	//alert(subProduct);
      	  
      	  	var childProduct = "<%= WO_Desc %>";
      	  	childProduct = formatColumns(childProduct)+curRow;
      	  	childProduct = '"'+childProduct+'"';
      	  	childProduct = childProduct.replace(/\"/g, "");
      	  
      	  
      	  	var quantity = "<%= quantity %>";
      	  	quantity = formatColumns(quantity)+"Id"+curRow;
      	  	quantity = '"'+quantity+'"';
      	  	quantity = quantity.replace(/\"/g, "");
      	
      	  
      	  	var measurement = "<%= measurement %>";
      	  	measurement = formatColumns(measurement)+"Id"+curRow;
      	 	measurement = '"'+measurement+'"';
      	  	measurement = measurement.replace(/\"/g, "");
      	  	//alert(measurement);
      		
      	  var acceptedrate="<%=AcceptedRate%>";
      	  acceptedrate= formatColumns(acceptedrate)+"Id"+curRow;
      	  acceptedrate= '"'+acceptedrate+'"';
      	  acceptedrate= acceptedrate.replace(/\"/g, "");
      	
      		for (var i in elementList) {
      			
      			if (elementList[i].id != "") {
      				
      				if(elementList[i].id == product) {
      	    		  	var pro = document.getElementById(product).value;
      					if(pro == "" || pro == null || pro == '') {
      						alert("Please enter major head.");
      						document.getElementById(product).focus();
      						return false;
      					}
      	    	  	} 
      		  	  	else if(elementList[i].id == subProduct) {
      	    		  	var subPro = document.getElementById(subProduct).value;
      					if(subPro == "" || subPro == null || subPro == '') {
      						alert("Please enter work order minor head.");
      						document.getElementById(subProduct).focus();
      						return false;
      					}
      	    	  	}  	  
      		  	  	else if(elementList[i].id == childProduct) {
      	    		  	var childPro = document.getElementById(childProduct).value;
      					if(childPro == "" || childPro == null || childPro == '') {
      						alert("Please enter work Description.");
      						document.getElementById(childProduct).focus();
							return false;
						}
      	    	  	}	  	 
      		  	else if(elementList[i].id == measurement) {
  	    		 	var units = document.getElementById(measurement).value;
  					if(units == "" || units == null || units == '') {
  						alert("Please select Units Of Measurement.");
  						document.getElementById(measurement).focus();
  						return false;
  					}
				}       else if(elementList[i].id == quantity) {
      	    			var qty = document.getElementById(quantity).value;
      	    			//qty = parseFloat(qty);
      					if(qty == "" || qty == null || qty == '' || qty == '.') {
      						alert("Please select work order area.");
      						document.getElementById(quantity).value = "";
      						document.getElementById(quantity).focus();
      						myFunction();
      						return false;
      					}
      					if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
      						alert("Please select work order area.");
      						document.getElementById(quantity).value = "";
      						document.getElementById(quantity).focus();
      						myFunction();
      						return false;
      					}
      	    	  	} 
				   else if(elementList[i].id == acceptedrate) {
     	    			var qty = document.getElementById(acceptedrate).value;
     	    			//qty = parseFloat(qty);
     					if(qty == "" || qty == null || qty == '' || qty == '.') {
     						alert("Please enter accepted rate.");
     						document.getElementById(acceptedrate).value = "";
     						document.getElementById(acceptedrate).focus();
     						return false;
     					}
     					if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
     						alert("Please enter accepted rate.");
     						document.getElementById(acceptedrate).value = "";
     						document.getElementById(acceptedrate).focus();
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
 .modalscopeheader{background-color:#ccc;border-radius:5px 5px 0px 0px;}
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
</style>
	<style>
	 .header-modalworkOrder{
	     background-color: #bdb7ab;
    border: 1px solid #bdb7ab;
    color: #fff;
    border-radius: 50px;
    margin-top: 5px;
    margin-left: 5px;
    margin-right: 5px;
	 
	 }
	 .workorder_modal_text{
	 border-bottom:2px solid #bdb7ab;
	 border-top:none;
	 border-left:none;
	 border-right:none;
	 border-radius:0px;
	 }
	 .workorder_modal_text:focus{
	 border-bottom:2px solid #bdb7ab;
	 border-top:none;
	 border-left:none;
	 border-right:none;
	 border-radius:0px;
	 box-shadow: none;
	 }
	 .remove-button{
	 font-size: 12px;
    padding: 5px 6px;
	 }
	 .plus-button{
	
    font-size: 12px;
    padding: 5px 6px;
	 }
	 .red{
	 color:red;
	 font-size:14px;
	 font-weight:bold;
	 }
	 .margin-close{
	 margin-top:5px;
	 }
	 .black{
	  color:black;
	 font-size:14px;
	 font-weight:bold;
	 }
	 .control-text{
	 border:1px solid #ddd !important;
	 }
	 .remove-filed{
	 margin-bottom:15px;
	 }
	 .mrg-btm{
	 margin-bottom:15px;
	 }
	 /*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:56px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/
#WorkOrderDate{
    display: inline;
    width: 84%;
    float: left;
    border-radius: 5px 0px 0px 5px;
}
#datepickerIcon{
    height: 30px;
    float: left;
    border: 1px solid #000 !important;
    margin-left: -1px;
    background-color:#ddd;
    border-radius: 0px 5px 5px 0px;
}

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

						<jsp:include page="./../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="./../TopMenu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Create NMR Work Order</li>
						</ol>
					</div>
					
					
					<div>
			<div align="center"><span style="text-align: center;" ><font color=red size=4 face="verdana">${responseMessage}</font></span></div>
 <div class="col-md-12">
<form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal">		
	  <div class="border-background-workorder">
		<span style="color:red;">${noStock}</span><br/>
		   <div class="col-md-12">
		    <div class="col-md-6">
		     <div class="form-group">
			<label class="control-label col-md-6"><%= TempWorkOrderNo%><%= colon %></label>
			<div class="col-md-6" >
				<form:input path="siteWiseWONO" id="workOrderId"  name="workOrderId" class="form-control" readonly="true"/>
			</div>
			</div>
		    </div>
			<div class="col-md-6">
			 <div class="form-group">
			<label class="control-label col-md-6"><%= WorkOrderNo%> <%= colon %> </label>
			<div class="col-md-6" >
				<form:input path="workOrderNo" id="workOrderNo" class="form-control" autocomplete="off"  onblur="checkWorkOrderNo(this.value)"/>
				<input type="hidden" value="${WorkOrderBean.workOrderNo}" id="actualWorkOrderNumber" name="actualWorkOrderNumber">
			</div>
		   </div>
			</div>
		   
		     <div class="col-md-6">
		       <div class="form-group">
			<label class="control-label col-md-6"><%= WorkOrderName%><%= colon %></label>
			<div class="col-md-6" >
				<form:input path="workOrderName" id="workOrderName"  name="workOrderName" class="form-control"   placeholder="Work Order Name"/>
			</div>
			</div>
		     </div>
			 <div class="col-md-6">
			  <div class="form-group">
			<label class="control-label col-md-6 col-xs-12"><%= WorkOrderDate%> <%= colon %> </label>
			<div class="col-md-6 col-xs-12 input-group">
				<form:input path="workOrderDate" id="WorkOrderDate" class="form-control" autocomplete="off" readonly="true" style="background-color: #fff;width:80%;" placeholder="dd-mm-yy"/><label class='btn datepicker-paymentreq-fromdate' id='datepickerIcon' onclick='openCalender()' for='fromDate'><span class='fa fa-calendar'></span></label>
			</div>
		   </div>
			 </div>
		     <div class="col-md-6">
		      <div class="form-group">
				<label class="control-label col-md-6"><%= vendorName %> <%= colon %>  </label>
				<div class="col-md-6" >
					<form:input path="contractorName" id="contractorName"  onkeyup="populateContractor(this);"  autocomplete="off" class="form-control" placeholder="Contractor Name"/>
						<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId"/>
						<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO"/>
						<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId"/>
						<form:input path="approverEmpMail" type="hidden" name="approverEmpMail" id="approverEmpMail"/>
						<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo"/>
						<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom"/>
						<form:input path="totalWoAmount" id="totalWoAmount" type="hidden" />
						<input name="TotalAmountOfWorkOrder"  id="TotalAmountOfWorkOrder"  type="hidden" />
						<form:input path="siteId" id="siteId"  type="hidden" />
						<form:input path="boqNo" id="boqNumber"  type="hidden" />
						<form:input path="isSaveOrUpdateOperation" id="isSaveOrUpdateOperation"  type="hidden" value=""/>
						<input id="typeOfWork" value="NMR" type="hidden">
				</div>
				</div>
		     </div>
	  		<div class="col-md-6">
	  		 <div class="form-group">
				<label class="control-label col-md-6"><%= panCardNo %> <%= colon %> </label>
				<div class="col-md-6" >
					<form:input path="contractorPanNo"  name="contractorPanCardNo" id="contractorPanCardNo" class="form-control"   readonly="true" placeholder="PAN Card Number"/>
				</div>
		   </div>
	  		</div>
			<div class="col-md-6">
			 <div class="form-group">
					<label class="control-label col-md-6"><%= Address %> <%= colon %>  </label>
					<div class="col-md-6" >
							<form:input path="contractorAddress" name="contractorAddress"  class="form-control" id="contractorAddress"  readonly="true"  placeholder="Contractor Address"/>
					</div>
			</div>
			</div>	
		   <div class="col-md-6">
		    <div class="form-group">
					 <label class="control-label col-md-6"><%= phoneNo %> <%= colon %> </label>
					 <div class="col-md-6">
					 <form:input path="contractorPhoneNo"  name="contractorPhoneNo"  class="form-control" id="contractorPhoneNo" readonly="true"  placeholder="Phone Number "/>
					</div>
			</div>	
		   </div>
			</div>				
</div>
		    
		  <div class="col-md-12 no-padding-left no-padding-right">
		   <div class="table-responsive"> <!-- /*tblprodindissudiv*/ -->
		<table id="indentIssueTableId" class="table table-bordered tblprodindissu" style="width:2000px;">
					
			<thead class="cal-thead-inwards">
			
  				<tr>
  				<th><%= serialNumber %></th>
  				<th><%= WO_MajorHead %></th>
  				<th><%= WO_MinorHead %></th>
  				<th><%= WO_Desc %></th>
  				<th><%= measurement %></th>
  				<th><%= quantity %></th>
  				<th><%= AcceptedRate %></th>
  				<th><%= TotalAmount %></th>
  				<th><%= note %></th>
  				<th><%= actions %></th>
  				
  				</tr>
			</thead>
  				<tbody class="tbl-fixedheader-tbody">
				<tr id="productrow1" class="productrow">
			<!--	<tr id="workorderrow1" class="workorderrowcls">-->
					<td>						
						<div id="snoDivId1">1</div>
					</td>
					<td>
						<select path="WO_MajorHead1" id="combobox1" name="WO_MajorHead1" >
							<option value="">Select one...</option>
					    		<%					    		
					    			for(Map.Entry<String, String> prods : products.entrySet()) {
					    				String prodName=prods.getValue().replace("\"","#");
					    				String prodName1=prods.getValue().replace("\"","'");
					    				String prodIdAndName = prods.getKey()+"$"+prodName;
								%>
									<option value="<%= prodIdAndName %>"> <%= prodName1 %></option>
					    		<% 
					    			} 
					    		%>
						</select>
					</td>
					<td>
						<form:select path="WO_MinorHead1" id="comboboxsubProd1" class="form-control"/>
					</td>
					<td>
						<form:select path="WO_Desc1" id="comboboxsubSubProd1" class="form-control" name="WO_Desc1" />
						
					</td>
				<%-- <td>
						<form:input path="woManualDesc1" class="form-control scopeOfWork1_class" name="woManualDesc1" id="woManualDesc1" onclick="myscopefocus(1)"/>	
						<div class="modalpopup1" id="modalpopup1"></div>					
					</td> --%>
					<td>
						<form:select path="UnitsOfMeasurement1" id="UOMId1"  class="form-control"  onchange="loadWOAmtAndQTY(this.value,1)"/>
					</td>
					<td class="w-70">
					<!-- onchange="validateFloatKeyPress(this,1)"  -->
						<form:input path="Quantity1"  id="QuantityId1"  onkeypress="return isNumberCheck(this, event)"  onkeyup="validateNumbers(this.value, 1);"  class="form-control" autocomplete="off"/>
						<input type="hidden" name="" id="TotalBOQAmount">
						<input type="hidden" name="" id="TotalNMR_WO_initiatedAmount">
					</td>
					<td>
						<form:input path="AcceptedRate1" id="AcceptedRateId1" onkeypress="return isNumberCheck(this, event)"  onkeyup="calCulateTotalAmout(this.value,1)"  class="form-control" autocomplete="off" />
					</td>
					<td>
						<form:input path="TotalAmount1" id="TotalAmountId1"  class="form-control TotalNMRBOQ_Amount" autocomplete="off" readonly="true" />
					</td>
					<td>
						<form:input path="comments1" type="text" class="form-control" id="NoteId1" name="comments1" autocomplete="off"/>
					</td>				
					<td>
						<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button style="display:none;" type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button>
					</td>
				</tr>
				</tbody>				
			</table>
			</div>
		  </div>
			
			<div class="col-md-12" style="padding-left:0px;margin-top:15px;"> <!-- tblindentissunote -->
				<div class="col-md-6" style="padding-left:0px;">
				 <div class="form-group">
				 <label class="col-md-2 pt-10">
						<%= purpose %> <%= colon %>
					</label>
					<div class="col-md-10">
						 <form:textarea path="Purpose" id="PurposeId" class="tb_notes"/>
					<input type="hidden"  name="TC_listSize" value="${TC_listSize}">
				</div>
				
				</div>
				
				</div>
			
			</div>

<!-- Modal popup for workorder submit start-->
 <!-- Modal -->
<div id="myModal-workOrder" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header header-modalworkOrder text-center" >
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Terms & Conditions</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
      <div class="col-md-12 appen-div-workorder">
      <c:forEach items="${listTermsAndCondition }" var="TAC">
					
						
      <div class="col-md-12 remove-filed">
		      <div class="col-md-10">
		      <input type="hidden" name="TC_listSize${TAC.indexNumber}" value="${TAC.indexNumber}">
		      <input type="text" name="termsAndCOnditions" class="form-control workorder_modal_text" id="terms${TAC.indexNumber}"  value="${TAC.strTermsConditionName}"/>
		      </div>
		      <div class="col-md-2 margin-close">
		      <button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
		      </div>
      </div>
      	</c:forEach>
      
      
      </div>
       <div class="col-md-12">
       <div class="col-md-12 mrg-btm">
       <div class="input_fields_wrap">
    <!-- button class="add_field_button btn btn-info iddd">Add More Fields</button>
    <div><input type="text" class="form-control workorder_modal_text" id="workorder_modal_text1" name="mytext[]"></div> -->
 <div class="col-md-10">
 <input type="text" class="form-control workorder_modal_text " id="workorder_modal_text1" name="termsAndCOnditions"/></div>
        <div class="col-md-2 margin-close"><button type="button" class="btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
        
</div>
</div>
    <%-- 	<div class="col-md-12 margin-close">
         <input type="checkbox" style="float:  left;"><span style="float:  left;"> (Optional)If you want to add CC In emails.</span>
         <input type="text" class="form-control control-text" id="email-popup-workorder" name="optionalCCmails" value="${optionalCCmails}">
         </div> --%>
         <!--  <div class="col-md-12 margin-close">
         <input type="checkbox" style="float:  left;"><span style="float:  left;"> Subject</span>
         <input type="text" class="form-control control-text" id="email-popup-workorder" placeholder="Please enter the subject">
         </div> -->
       </div>
      </div>
      <div class="modal-footer">
       <div class="col-md-12 text-center center-block">
         <button type="button" class="btn btn-warning"  id="saveWorkOrder" data-dismiss="modal" onclick="saveRecords('SaveClicked')" >Submit</button>
       </div>
      </div>
    </div>

  </div>
</div>
<!-- Modal popup for workorder submit end -->
			
		<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
	<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
</form:form>
		
		<div class="col-md-12 text-center center-block" style="margin-bottom:10px;" >
		<button type="button" class="btn btn-warning" style="display: none;" value="Draft Work Order" id="saveBtnId1" onclick="openTermsModal(this.value)">
		<i class="fa fa-floppy-o" aria-hidden="true"></i>&emsp;Draft Work Order
		</button>
		<input type="button" class="btn btn-warning" value="Submit" id="saveBtnId" onclick="openTermsModal(this.value)">
		</div>
	</div>

	<div class="clearfix"></div>
	
	</div>    
</div>
</div>
</div>

${javaScriptProp}

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
			

		</script> 
		<script>

	//	var rowsIncre=1;
		if (typeof(Storage) !== "undefined") {
		    // Store   	
		     debugger;
		    sessionStorage.setItem("rowsIncre", 1);
		     var i=parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
		       if(i==2){
		    	   sessionStorage.setItem("${UserId}tempRowsIncre12",1);
		           window.location.reload();
		       }
		} else {
		   alert("Sorry, your browser does not support Web Storage...");
		};
		//validating Work area 
		function validateWorkArea(val,row){
				val=$("#actualWorkAreaId"+row).val();
				var allocatedArea = parseInt($("#" + val + "AVAILABE_AREA").val().trim());
				var availbleArea= parseInt($("#" + val + "WORK_AREA").text().trim());
				
				var actualAreaAlocatedQTY =	parseInt($("#actualAreaAlocatedQTY"+row).val());
	            if(allocatedArea>availbleArea){
	            	alert("you can't allocate the more than area, of availbale area.");
	            	$("#"+val+"AVAILABE_AREA").val(actualAreaAlocatedQTY);
	            	return false;
	            }
	    }
	
		 //validating work area value
		function validateWorkAreaVal(val) {
			var amt = parseInt($("#" + val + "AVAILABE_AREA").val().trim());
			var actualArea=parseInt($("#"+val+"WORK_AREA").text().trim());
			var accepted_rate=$("#"+val+"ACCEPTED_RATE").val()==""?0:$("#"+val+"ACCEPTED_RATE").val();
			
			if(amt>actualArea){
				$("#" + val + "AVAILABE_AREA").val("0");
				alert("you can't allocate the more than area, of availbale area.");
				$("#" + val + "val").prop("checked", false);
				return false;
			}
		
			if (!isNum(amt)&&amt!=0) {
				alert("Please enter correct value.");
				$("#" + val + "AVAILABE_AREA").val("0");
				$("#" + val + "AVAILABE_AREA").focus();
				$("#" + val + "val").prop("checked", false);
				return false;
			}
			if($("#" + val + "val").is(":checked")==false){
				$("#" + val + "AVAILABE_AREA").val("0");
			}			
			if(amt==0){
				alert("Please enter correct value.");
				$("#" + val + "val").prop("checked", false);
				return false;
			}
			accepted_rate=parseFloat(accepted_rate);
			if(accepted_rate==0){
				alert("Please enter accepted rate.");
				$("#"+val+"ACCEPTED_RATE").focus();
				$("#" + val + "val").prop("checked", false);
				return false;
			}			
		}
		function isNum(value){
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value);
		}
		
			function checkWorkOrderNo(workOrderNo){
				var actualWorkOrderNumber=$("#actualWorkOrderNumber").val();
				var actualarray=actualWorkOrderNumber.split("/");
				var array=new Array();
				array=workOrderNo.split("/");
				
				for (var index = 0; index < actualarray.length-1; index++) {
					 if(actualarray[index]!=array[index]){
							alert("Please enter correct format number of work order.");
							$('#saveBtnId').attr('disabled',true);
							$('#saveBtnId1').attr('disabled',true);
							 $('#saveWorkOrder').attr('disabled',true);
							return false;
					 }
				}
				if(array.length>4){
					alert("Please enter correct format number of work order.");
					 $('#saveBtnId').attr('disabled',true);
					 $('#saveWorkOrder').attr('disabled',true);
					return false;
				}else{
					 $('#saveBtnId').attr('disabled',false);
					 $('#saveWorkOrder').attr('disabled',false);
				}

				if(!isNum(array[array.length-1])){
					alert("Please enter correct format number of work order.");
					$('#saveBtnId').attr('disabled',true);
					$('#saveBtnId1').attr('disabled',true);
					 $('#saveWorkOrder').attr('disabled',true);
					return false;
				}else{
					 $('#saveBtnId').attr('disabled',false);
						$('#saveBtnId1').attr('disabled',false);
					 $('#saveWorkOrder').attr('disabled',false);
				}

				var url = "checkWorkOrderNoExistsOrNot.spring";
				$.ajax({
					url : url,
					type : "get",
					data:{
						workOrderNo:workOrderNo
					},
					success : function(data) {
					debugger;
						if(data=="true"){
							alert("Work order number already exists,Please enter another work order number.");
							$('#saveBtnId').attr('disabled',true);
							$('#saveBtnId1').attr('disabled',true);
							 $('#saveWorkOrder').attr('disabled',true);
						}else{
							 $('#saveBtnId').attr('disabled',false);
								$('#saveBtnId1').attr('disabled',false);
							 $('#saveWorkOrder').attr('disabled',false);
						}
					}
				});
			}
		
		function loadWOAmtAndQTY(childProdId,rownum){
			childProdId = $("#comboboxsubSubProd"+rownum).val().split("$")[0];
			var mesumentId=$("#UOMId"+rownum).val().split("$")[0];
			var WO_MinorHead=$("#comboboxsubProd"+rownum).val().split("$")[0];
			var siteId=$("#siteId").val();
			var url = "loadScopeOfWork_AmountAndQty.spring?WO_MinorHead="+WO_MinorHead+"&childProductId="+childProdId+"&site_id="+siteId+"&mesumentId="+mesumentId+"&typeOfWork=NMR&isApproveOrRevisePage=false";
			  $.ajax({
				  url : url,
				  type : "get",
				contentType : "application/json",
				success : function(data) {
					var array=new Array();
					array=data.split("@@");
					
					var BOQ_No=array[0];
					var quantity=array[1];
					var amount=array[2];
					
					var totalBOQAmount=array[3];
					var totalNMRWOInitiatedAmount=array[4];
					
					$("#boqNumber").val(BOQ_No);
					
					$("#TotalBOQAmount").val(totalBOQAmount);
					$("#TotalNMR_WO_initiatedAmount").val(totalNMRWOInitiatedAmount);

				}
			  });
		}
		
		function validateFloatKeyPress(el,rownum) {
				var value = parseFloat(el.value);
			    el.value = (isNaN(value)) ? '' : value.toFixed(2);
			    validateNumbers(value,rownum);
		}
		
		function validateNumbers(quantity,rownum){
			if($("#UOMId"+rownum).val()==''||$("#UOMId"+rownum).val()==null){
				
				var valStatus = validateMajorHeadTable();
			    
			    if(valStatus == false) {
			        return false;
			    }
				alert("Please select UOM");
				$("#UOMId"+rownum).focus();
				//return error=false;;
				return false;
			}
			
			
			
		if(quantity.length<1){
			return false;
		}
		if(!isNum(quantity)){
				$("#QuantityId"+rownum).val('');
				$("#QuantityId"+rownum).focus();
				alert("please enter only numbers");
				return false;
		}
			
			var AcceptedRateId=	$("#AcceptedRateId"+rownum).val()==""?0:$("#AcceptedRateId"+rownum).val();
			calCulateTotalAmout(AcceptedRateId,rownum);
		}
		
		function calCulateTotalAmout(acceptedRate,rownum){
			if($("#QuantityId"+rownum).val()==''||$("#QuantityId"+rownum).val()=='0'||$("#QuantityId"+rownum).val()=='0.00'){
				alert("Please enter quantity");
				$("#QuantityId"+rownum).focus();
				$("#AcceptedRateId"+rownum).val("");
				return false;
			}
			if($("#AcceptedRateId"+rownum).val()!=''){
				
			//||$("#AcceptedRateId"+rownum).val()=='0'||$("#AcceptedRateId"+rownum).val()=='0.00'
				var valStatus = validateMajorHeadTable();
			    
			    if(valStatus == false) {
			        return false;
			    }
			}
		    // Retrieve
		    if(acceptedRate.length==0){
		    	return false;
		    }
		    acceptedRate=parseFloat(acceptedRate);
		    
		    var numRegex=/^[0-9.]+$/;
		    if(!isNum(acceptedRate)){
		    	alert("Enter only digits");
		    	$("#AcceptedRateId"+rownum).val("");
		    	$("#TotalAmountId"+rownum).val("");
		    	return false;
		    }
			let  rowsIncre=sessionStorage.getItem("rowsIncre");
		   	var qty=$("#QuantityId"+rownum).val()==""?0:$("#QuantityId"+rownum).val();
		   	var TotalAmount=qty*acceptedRate;
			
		   	$("#TotalAmountId"+rownum).val(TotalAmount.toFixed(2));
		   	var sumofTotalAmount=0;
		
		   	$(".productrow").each(function(){ 
				var currentId=$(this).attr("id").split("productrow")[1];	
				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
			//chargesRowCountNum.push(currentId);
			});
		   	
		   	debugger;
			
			var totalBOQAmount=$("#TotalBOQAmount").val()==""?0:parseFloat($("#TotalBOQAmount").val());
			var totalNMRWOInitiatedAmount=$("#TotalNMR_WO_initiatedAmount").val()==""?0:parseFloat($("#TotalNMR_WO_initiatedAmount").val());
			var initiateAmount=totalBOQAmount-totalNMRWOInitiatedAmount;
			if(sumofTotalAmount>initiateAmount){
				alert("Total NMR BOQ Amount "+(totalBOQAmount)+" Initiated Amount is "+(totalNMRWOInitiatedAmount)+" you can initiate now = "+initiateAmount);
				$("#TotalAmountId"+rownum).val("");
				$("#AcceptedRateId"+rownum).val("");
				return false;
			}
		}	
	</script>
		
		
<script type="text/javascript">
function loadSubProds(prodId, rowNum) {
	debugger;
	
	var requesteddate=$("#workOrderId").val();
	if(requesteddate == "" || requesteddate == '' || requesteddate == null) {
		alert("Please Choose Requested Date .");
		document.getElementById("requesteddate").focus();
	//alert(requestedDate);
	}
	
	//alert("hai");
	
	prodId = prodId.split("$")[0];
	
	var url = "workOrderSubProducts.spring?mainProductId="+prodId+"&typeOfWork=NMR";;
	
	
	//alert(url);
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			//alert("request.readyState "+request.readyState);
			
			if(request.readyState == 4 && request.status == 200) {
                
				$("#WO_MinorHead"+rowNum).val("");
				$("#WO_Desc"+rowNum).val("");
			    $("#UOMId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();			

		    	var spltData = resp.split("|");
		    	//alert(spltData);
		    	
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSet = "comboboxsubProd"+rowNum;
		    	//alert(subProdToSet);
		    	
		    	var selectBox = document.getElementById(subProdToSet);
			    //alert(selectBox);
			    
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

function loadSubSubProducts(subProdId, rowNum) {
	debugger;
	subProdId = subProdId.split("$")[0];
	//alert("Sub Product Id After Split = "+subProdId);
	
	var url = "workOrderChildProducts.spring?subProductId="+subProdId+"&typeOfWork=NMR";
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#WO_MinorHead"+rowNum).val("");
				$("#WO_Desc"+rowNum).val("");
				$("#UOMId"+rowNum).val("");
				var resp = request.responseText;
				resp = resp.trim();			

		    	var spltData = resp.split("|");
		    	//alert(spltData);
		    	
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subSubProdToSelect = "comboboxsubSubProd"+rowNum;
		    	//alert(subSubProdToSelect);
		    	
		    	var selectBox = document.getElementById(subSubProdToSelect);
			    //alert(selectBox);
			    
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
function loadScopeOfWork(childProdId, rowNum){

}



function loadUnits(childProdId, rowNum) {


	childProdId = childProdId.split("$")[0];
	debugger;
	
	var url = "listOfWOmesurment.spring?childProductId="+childProdId+"&typeOfWork=NMR";
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                
				$("#UOMId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				//alert(available);
				
		    	var unitsToSelect = "UOMId"+rowNum;
		    	//alert(unitsToSelect);
		    	
		    	var selectBox = document.getElementById(unitsToSelect);
			    //alert(selectBox);
			    
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
	//myFunction();
}

function populateContractor() {
	
	debugger;var contractorName=$("#contractorName").val();
var nameRegex=/^[a-zA-z ]+$/;
if(contractorName.length>0){

if(!nameRegex.test(contractorName)){
	alert("please enter Contractor name.");
	//$("#contractorName").val("");
	return false;
}
}else{
	return false;
}
	 var url = "loadAndSetVendorInfoForWO.spring";
	 debugger;
  $.ajax({
	  url : url,
	  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "get",
	data:{
		contractorName:contractorName,
		loadcontractorData:false		
	},
	  contentType : "application/json",
	  success : function(data) {
	
	  		$("#contractorName").autocomplete({
		  		source : data,
		  		 change: function (event, ui) {
		                if(!ui.item){
		                    // The item selected from the menu, if any. Otherwise the property is null
		                    //so clear the item for force selection
		                    $("#contractorName").val("");
		                }
		            },
		  		 select: function (event, ui) {
	                   AutoCompleteSelectHandler(event, ui);
	               }

		  	});
	  },
	  error:  function(data, status, er){
		  alert(data+"_"+status+"_"+er);
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
				  debugger;
				  $("#contractorName").val(contractorName);
				  if(data!=""||data!="null"){

					  var contractorData=data[0].split("@@");
					var contractorId=contractorData[0];
					
					$("#contractorId").val(contractorId);
					 $("#contractorAddress").val(contractorData[2]);
					  $("#contractorPhoneNo").val(contractorData[3]);
					  $("#contractorPanCardNo").val(contractorData[4]);
					  $("#contractorGSTINNO").val(contractorData[7]);
					  $("#contractorAddress").prop("readonly",true);
					  $("#contractorPhoneNo").prop("readonly",true);
					  $("#contractorPanCardNo").prop("readonly",true);  
					  //checking is any work order drafted for this contractor or not
					  checkIsDraftWorkOrderExistsOrNot(contractorId);
				  }
				
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
	}
};

function checkIsDraftWorkOrderExistsOrNot(contractorId){
	var siteId=$("#siteId").val(); 
	var typeOfWork=$("#typeOfWork").val();
	var url = "checkIsDraftWorkOrderExistsOrNot.spring";
	 $.ajax({
		  url : url,
		  type : "get",
		 data:{
			 contractorId:contractorId,
			 siteId:siteId,
			 typeOfWork:typeOfWork
		 },
		  contentType : "application/json",
		  success : function(draftedTempWoNumber) {
			  if(draftedTempWoNumber==""){
			     $('#saveBtnId').attr('disabled',false);
			 	$('#saveBtnId1').attr('disabled',false);
			     $('#saveWorkOrder').attr('disabled',false);
			  }else{
			    var isConfirm= confirm("You have a Drafted Work Order with this Contractor, Click OK to continue with Drafted Work Order.");
				//alert(isConfirm);
				if(isConfirm==true){
					var url="showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="+draftedTempWoNumber+"&workOrderNumber=&siteId="+siteId+"&status=false&statusType=DF";
					window.location.assign(url);
				}else{
					  $("#contractorName").val("");
					  $("#contractorId").val("");
					  $("#contractorAddress").val("");
					  $("#contractorPhoneNo").val("");
					  $("#contractorPanCardNo").val("");
					  $("#contractorGSTINNO").val("");
					/*   $("#combobox1").attr('disabled',true);	
					  $("#WO_MajorHead1").attr('disabled',true); */
				}
				$('#saveBtnId').attr('disabled',true);
				$('#saveBtnId1').attr('disabled',true);
				$('#saveWorkOrder').attr('disabled',true);
			  }
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
}


</script>
<script>
	$(document).ready(function() {
		    var max_fields      = 50; //maximum input boxes allowed
		    var wrapper         = $(".appen-div-workorder"); //Fields wrapper
		    var add_button      = $(".add_field_button"); //Add button ID
	    
		    var textCount = 1; //initlal text box count
		    $('.add_field_button').on("click",function(e){ //on add input button click
		        e.preventDefault();
		    var termsAndCOnditions=$("#workorder_modal_text1").val();
	    
		    if(termsAndCOnditions.length==0){
		    	alert("Please enter terms and conditions");
		    	$("#workorder_modal_text1").focus();
		    	return false;
		    }
	    
	        if(textCount < max_fields){ //max input box allowed
	        	textCount++; //text box increment
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-10"><input type="text" id="TCTxtCount'+textCount+'" name="termsAndCOnditions" value="'+termsAndCOnditions+'" class="form-control workorder_modal_text"/></div><div class="col-md-2 margin-close"><button type="button" class="btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#TCTxtCount"+textCount).val(termsAndCOnditions);
	    });
	    	
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent().parent(".remove-filed").remove(); textCount--;
	    })
	});
	
var serialNumber=1;
function appendtextbox(btn){
	debugger;
	var textlength=$('.scopeofworkid'+btn).length;
	var defaultSOW=$("#defaultScopeOfWOrk"+btn).val();
	if(defaultSOW.length==0){
		
	alert("please enter scope of Work.");
		return false;
	}
	//defaultSOW=defaultSOW.replace(/[\"\""]/g, '');
	var appendtextid=textlength+1;
    $("#textboxDiv"+btn).append('<div class="col-md-12 mrg-btm" id="newtextId"><div class="form-group"><div class="col-md-11"><input type="text" name="ScopeOfWork'+btn+'" class="form-control txt-border scopeofworkid" value="'+defaultSOW+'" /></div><div class="col-md-1"><button class="btn btn-danger" type="button"><i class="fa fa-close"></i></button></div></div></div>');  
    serialNumber=1;
    $("#defaultScopeOfWOrk"+btn).val('');
    
    
}
function submitScopeOfWork(rowNum){
	$("#modalForScopeWork"+rowNum).modal("hide");
}

//to open calender when you click on calender icon 
function openCalender(){
	$("#WorkOrderDate").focus();
}
</script>
</body>
</html>
