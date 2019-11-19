<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
	String colon = resource.getString("label.colon");
	
	String reqId = resource.getString("label.WorkOrderNo");
	String WorkOrderName = resource.getString("label.WorkOrderName");
	String date = resource.getString("label.WorkOrderDate");
	
	String projectName = resource.getString("label.projectName");
	String purpose = resource.getString("label.purpose");
	String ContractorName = resource.getString("label.ContractorName");

	String serialNumber = resource.getString("label.serialNumber");
	String quantity = resource.getString("label.quantity");
	
	String remarks = resource.getString("label.remarks");
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
	String WO_Manual_Desc = resource.getString("label.WO-Manual-Desc");//not used
	String scope_Of_work= resource.getString("label.scope-Of-work");
	String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency");
	String measurement = resource.getString("label.UOM");
	String AcceptedRate = resource.getString("label.AcceptedRate");
	String TotalAmount = resource.getString("label.TotalAmount");
	String comments= resource.getString("label.Comments");
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
		<link href="css/ShowWorkOrder.css" rel="stylesheet"  type="text/css" >
		
		
<jsp:include page="./../CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
<!-- <title>Approve WorkOrder Page</title> -->
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
           
            var WOMajorHeadId = "";
            var WOMajorHeadName = "";
            
            var WOMinorHeadId = "";
            var WOMinorHeadName = "";
            
            var WOWorkDescId = "";
            var WOWorkDescName = "";
            
            WOMajorHeadId = ui.item.option.value;
            WOMajorHeadName = ui.item.value;
            
            var ele = this.element[0].name;
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            
	        var WO_MajorHead =  "<%= WO_MajorHead %>";
	        WO_MajorHead = formatColumns(WO_MajorHead);
		  	 
		  	var WO_MinorHead =  "<%= WO_MinorHead %>";
		  	WO_MinorHead = formatColumns(WO_MinorHead);
		  
		 	var Wo_WorkDesc =  "<%= WO_Desc %>";
		 	Wo_WorkDesc = formatColumns(Wo_WorkDesc);
			
			var rowNum = ele.match(/\d+/g);
			             
            if(str1 == WO_MajorHead) {
            	WOMajorHeadId = ui.item.option.value;
            	WOMajorHeadName = ui.item.value;
                loadSubProds(WOMajorHeadId, rowNum);
            }            
            else if(str1 == WO_MinorHead) {
            	WOMinorHeadId = ui.item.option.value;
            	WOMinorHeadName = ui.item.value;
                loadSubSubProducts(WOMinorHeadId, rowNum);
            }
            else if(str1 == Wo_WorkDesc) {
            	WOWorkDescId = ui.item.option.value;
            	Wo_WorkDesc = ui.item.value;
                loadUnits(WOWorkDescId, rowNum);
                loadWOAmtAndQTY(WOWorkDescId, rowNum);
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
	/*   $("#workOrderDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:new Date(),
		//maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
	  
	  }); */
  });
  
</script>

<script type="text/javascript">
//var delOne;
 function myscopefocus(id){
		 $("#hiddenrownum").val(id); 
		$("#modalForScopeWork"+id).modal();
	}
//Create DIV element and append to the table cell
function createCell(cell, text, style, fldLength, cellsLen, tableColumnName) {
	debugger;
	 
     var vfx = fldLength;
     //removing space
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
	  
	 var comments =  "<%= comments %>";
	 comments = formatColumns(comments);
	 
	 var actionsColumn =  "<%= actions %>";
	 actionsColumn = formatColumns(actionsColumn);
	 //alert(actionsColumn);
     
	 var typeOfWork="${WorkOrderBean.typeOfWork}";
	 
     if(tableColumnName == snoColumn) {
    	var snoDiv = document.createElement("div");
        txt = document.createTextNode(vfx);
        snoDiv.appendChild(txt);
        snoDiv.setAttribute("id", "snoDivId"+vfx);
        cell.appendChild(snoDiv);
        var div1 = document.createElement("input");
	    div1.setAttribute("type", "hidden");
	    div1.setAttribute("name", "isNewRowAdded"+vfx);
	    div1.setAttribute("id", "isNewRowAdded"+vfx);
	    div1.setAttribute("value", "true");
	    cell.appendChild(div1);		
	    
	    div1 = document.createElement("input");
	    div1.setAttribute("type", "hidden");
	    div1.setAttribute("name", "dispplayedRows");
	    div1.setAttribute("id", "dispplayedRows");
	    div1.setAttribute("value", vfx);
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
    	    	//div.setAttribute("onkeydown", "appendRow()");    	    	
    	    }    	        	   
    	    var defaultOption = document.createElement("option");
    	    defaultOption.text = "Select one...";
    	    defaultOption.value = "";
    	    div.appendChild(defaultOption);    	    
    	    var option = "";
    		<% 
    			Map<String, String> products = (Map<String, String>)request.getAttribute("workMajorHead");
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
    	else if(tableColumnName == WO_MinorHead) {
    		
    		var dynamicSelectBoxId = "comboboxsubProd"+vfx;
    		var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	  	    
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
    	    	//div.setAttribute("onkeydown", "appendRow()");
    	    }
    	    cell.appendChild(div);    	    
    	    $(function() {
    	        $("#"+dynamicSelectBoxId).combobox();
			});
    	}    
    	
    	else if(tableColumnName == scope_Of_work) {
    		//alert(scope_Of_work);  
    	
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", "scopeOfWork"+vfx);
		    div.setAttribute("id", "scopeOfWork"+vfx);
		    div.setAttribute("onfocus", "myscopefocus("+vfx+")");		
		    div.setAttribute("autocomplete", "off");
		    div.setAttribute("onkeydown", "return false");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		    var div1 = document.createElement("div");		
		    div1.setAttribute("class", 'modalpopup'+vfx);
		    div1.setAttribute("id", 'modalpopup'+vfx);
		    cell.appendChild(div1);	
   		}
    	
    	else if(tableColumnName == measurement) {
    		//var dynamicSelectBoxId = tableColumnName+"Id"+vfx;
    		//alert(dynamicSelectBoxId);    		
    		var div = document.createElement("select");
    	    div.setAttribute("name", "UnitsOfMeasurement"+vfx);
    	    div.setAttribute("id", "UnitsOfMeasurementId"+vfx);
    	    div.setAttribute("onchange", "return loadWOWorkAreaNewRow(this.value,"+vfx+");");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	}   	
    	else if(tableColumnName == quantityColumn) {
			
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
		    div.setAttribute("autocomplete", "off");
		    if(typeOfWork!="NMR"){
		        div.setAttribute("readonly", "true");
		    }
		    
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
	    	//div.setAttribute("onblur", "return validateUnitsAndAvailability(this);");
		    div.setAttribute("class", 'form-control copyPasteRestricted');
		    cell.appendChild(div);
		   //creates new li and new a
		   // var newSocial = document.createElement("li");
		    var newSocialLink = document.createElement("a");
		 	//creates text in the li
		    var liText = document.createTextNode("Click Here");
		 	//ads text to a
		    newSocialLink.appendChild(liText);
		    newSocialLink.onclick = myFunction;
		 
		    newSocialLink.setAttribute('id', 'click-show'+vfx);
		    
		  //FOr show Details
  			var newSocialLink1 = document.createElement("a");
			// 	creates text in the li
		    var liText1 = document.createTextNode("View Details");
			// ads text to a
		    newSocialLink1.appendChild(liText1);
			// newSocialLink1.onclick = showDetailsFunction(vfx);
		 	newSocialLink1.setAttribute('id', 'showQty'+vfx);
		 	newSocialLink1.setAttribute('class', 'anchormouseover');
		 	newSocialLink1.setAttribute("onclick", "showDetailsFunction("+vfx+")");
		
		 	newSocialLink1.setAttribute("data-toggle", "modal");
			newSocialLink1.setAttribute("data-target", "#myModal-showwo-showquantity");
			newSocialLink1.setAttribute("style", "display: none;");

			//FOr show Details
  			var newLinkForMaterial = document.createElement("a");
			// 	creates text in the li
		    var liText1 = document.createTextNode("Material Details");
			// ads text to a
		    newLinkForMaterial.appendChild(liText1);
			// newSocialLink1.onclick = showDetailsFunction(vfx);
		 	newLinkForMaterial.setAttribute('id', 'showMaterialQty'+vfx);
		 	newLinkForMaterial.setAttribute('class', 'anchormouseover');
		 	newLinkForMaterial.setAttribute("onclick", "showWDGroupWiseMaterialDetails("+vfx+")");
		
		 	newLinkForMaterial.setAttribute("data-toggle", "modal");
		 	//newLinkForMaterial.setAttribute("data-target", "#myModal-click");
		 	newLinkForMaterial.setAttribute("style", "display: none;");

		 	var divNew=document.createElement("div");
			divNew.setAttribute('id', 'appendWorkOrderArea'+vfx);
		 	cell.append(divNew);
		 	if(typeOfWork!="NMR"){
		 		cell.appendChild(newSocialLink);
		 		cell.appendChild(newSocialLink1);
		 		cell.appendChild(newLinkForMaterial);
		 	}
		  	 
		  	 
			 var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderWorkArea'+vfx);
			 cell.append(divNew);
			 
		  	 
		    $("#click-show"+vfx).attr("onclick","myFunction(0,0,"+vfx+",'show')");
   		} else if(tableColumnName == AcceptedRate) {
			if(typeOfWork=="NMR"){
	    		var div = document.createElement("input");
			    div.setAttribute("type", "text");
			    div.setAttribute("name", "AcceptedRate"+vfx);
			    div.setAttribute("id", "AcceptedRate"+vfx);
			    //div.setAttribute("onkeypress", "return validateNumbers(this, event);");
			    div.setAttribute("onkeydown", "isNumberCheck(this, event)");
			    div.setAttribute("onblur", "calCulateTotalAmout('a_rate',this.value,"+vfx+")");
			    div.setAttribute("class", 'form-control copyPasteRestricted');
			    cell.appendChild(div);
			    $(".copyPasteRestricted").bind('paste', function (e) {
					e.preventDefault();
				});
			}
    	 } else if(tableColumnName == TotalAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);		
		    
		    var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name", "WoRecordContains" +vfx);
		    div.setAttribute("id",  "WoRecordContains"+vfx);
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		    
			var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name",  "materialAmount"+vfx);
		    div.setAttribute("id",  "MaterialAmountId"+vfx);
		    div.setAttribute("class", 'materialAmount');
		    cell.appendChild(div);
			
			var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name", "labourAmount"+vfx);
		    div.setAttribute("id","LaborAmountId"+vfx);
		    div.setAttribute("class", 'laborAmount');
		    cell.appendChild(div);
   		}
    	
    	else if(tableColumnName == note) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == comments) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("autocomplete", "off");
		    div.setAttribute("name", tableColumnName+vfx);
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

//Validation started 
function validateRowData() {
	
  
     
  	  
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
      	 	//alert(product);
      	  
      	  	var subProduct = "<%= WO_MinorHead %>";
      	  	subProduct = formatColumns(subProduct)+curRow;
      	  	subProduct = '"'+subProduct+'"';
      	  	subProduct = subProduct.replace(/\"/g, "");
      	  	//alert(subProduct);
      	  
      	  	var childProduct = "<%= WO_Desc %>";
      	  	childProduct = formatColumns(childProduct)+curRow;
      	  	childProduct = '"'+childProduct+'"';
      	  	childProduct = childProduct.replace(/\"/g, "");
      	  	//alert(childProduct);
      	  
      	  	var quantity = "<%= quantity %>";
      	  	quantity = formatColumns(quantity)+curRow;
      	  	quantity = '"'+quantity+'"';
      	  	quantity = quantity.replace(/\"/g, "");
      	  //	alert(quantity);
      	  
      	  	var measurement = "<%= measurement %>";
      	  	measurement = formatColumns(measurement)+"Id"+curRow;
      	 	measurement = '"'+measurement+'"';
      	  	measurement = measurement.replace(/\"/g, "");
      	  	
      	  	
     	  	var acceptedrate="<%=AcceptedRate%>";
        	  acceptedrate= formatColumns(acceptedrate)+curRow+curRow;
        	  acceptedrate= '"'+acceptedrate+'"';
        	  acceptedrate= acceptedrate.replace(/\"/g, "");
        	
        	  
      	  	//alert(measurement);
      		  var typeOfWork= document.getElementById("typeOfWork").value;
  		
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
   						alert("Please enter qunatity.");
   						document.getElementById(quantity).value = "";
   						document.getElementById(quantity).focus();
   						if(typeOfWork!="NMR"){
   							myFunction();
   						}
   						return false;
   					}
   					if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
   						alert("Please enter qunatity.");
   						document.getElementById(quantity).value = "";
   						document.getElementById(quantity).focus();
   						if(typeOfWork!="NMR"){
   							myFunction();
   						}
   						
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
      		  	   else if(elementList[i].id == acceptedrate) {
      		  		if(typeOfWork=="NMR"){
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
      		  	   }//typeOfWork condition
    	    	  	} 
      				
      			}      			
      		}      		
      	}
	}
//Validation End
</script>
<style>
#floortable tbody tr td:first-child, #floortable thead tr th:first-child {min-width: 20px;text-align: center; width:50px !important;}
#floortable thead, #floortable tbody tr{table-layout:fixed;display:table;width:100%;}
#floortable>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
#floortable thead{width: calc(100% - 17px) !important;}
.fixed-table-body{display: inline-block; max-height: 350px;overflow-y: scroll;overflow-x: auto;}
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
/*fixed header */
.tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
.tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:56px !important;min-width: 20px;text-align: center}
.tblprodindissu tbody tr td{border-top:0px !important;}
.tblprodindissu{border:0px !important;}
/*fixed header*/
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
  

/* spinner stles */
.spinnercls{
    display: inline;
    height: 50px;
    position: absolute;
    right: 0px;
    top: 1px;
    display:none;
}
/* #floortable thead tr th, #floortable tbody tr td{min-width:120px !important;text-align:center;}
#floortable tbody tr td:first-child, #floortable thead tr th:first-child {
    min-width: 20px !important;
    width:50px;
} */
 /* end spinner stles */
 .ui-state-highlight{
 background-color:white !important;
 }
 .tblprodindissu tbody tr td:last-child,.tblprodindissu tbody tr th:last-child{
	min-width:150px;
 }
 .fnt-18{
	font-weight:1000;
	font-size:18px;
}
.WoGrandTotal{width:300px;float:right;font-size: 20px;margin-top:20px;}
.remove-button, .plus-button{
	font-size: 12px;
    padding: 8px 12px;
}
.mrg-t-10{
	margin-top: 10px;
}
.spanheading {
    font-size: 14px;
    font-weight: 1000;
    font-family: Calibri;
}
.mrg-btm-10 {
    margin-bottom: 10px;
}

.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 17px ) !important;
}
@media only screen and (min-width:320px) and (max-width:1024px){
	.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 0px ) !important;
}
}
@media only screen and (min-width:1440px){
	.cal-thead-inwards{
color: #000;
background: #ccc;
width: calc( 100% - 17px ) !important;
}
.tbl-width-medium{
	width:100%;
}
}
.tbl-fixedheader-tbody{
 display: inline-block; 
 max-height: 300px; 
 overflow-y: scroll;
 overflow-x: auto;
}
 .tblprodindissu tbody tr td, .tblprodindissu tbody tr th {
 	width:auto !important;
 }
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			var isCommonApproval="${isCommonApproval}";
			var workOrderStatus="${WorkOrderBean.workOrderStatus}";
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				if(isCommonApproval=="true"){
					window.location.assign("viewWOforApproval.spring");
				}else if(workOrderStatus=="DraftModify"){
					window.location.assign("openDraftWorkOrders.spring");
				}else{
					window.location.assign("viewPendingWOforApprove.spring");
				}
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
<%-- 							<%@include file="./../SideMenu.jsp" %> --%> 
						
					</div>
					</div>
						<jsp:include page="./../TopMenu.jsp" />  
				<%-- <%@include file="./../TopMenu.jsp" %> --%>
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Work Order</li>
						</ol>
					</div>
					
					
					<div class="col-md-12">
 <div align="center">
 <form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal" method="POST">
<div  class="col-md-12 border-background-workorder">

	<%-- <c:set  scope="session"   value="${WorkOrderBean}" var="WorkOrderBean"></c:set> --%>
	<c:set  scope="session"  var="deletedWorkOrderDetailsList"  value="${deletedWorkOrderDetailsList}"></c:set>
	<c:set scope="session" value="${listTermsAndCondition}" var="listTermsAndCondition"></c:set>
	<c:set scope="session"  value="${WorkOrderLevelPurpose}" var="WorkOrderLevelPurpose" ></c:set>
<%-- 	<c:set  scope="session"  value="${workOrderCreationList}" var="workOrderCreationList"></c:set> --%>
	
		<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
		<span style="color:red;">${noStock}</span><br/>
	       <div class="col-md-6">
		   <div class="form-group">
			<label class="control-label col-md-6"><%= TempWorkOrderNo%> <%= colon %> </label>
			<div class="col-md-6" >
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
				<label class="control-label col-md-6"><%= WorkOrderName%> <%= colon %> </label>
				<div class="col-md-6" >
					<form:input path="workOrderName" id="workOrderName"  name="workOrderName" class="form-control"  title="${WorkOrderBean.workOrderName}" />
				<input type="hidden" name="actualWorkOrderName" id="actualWorkOrderName" value="<c:out value='${WorkOrderBean.workOrderName}'></c:out>">
				</div>
			</div>
			</div>
			<div class="col-md-6">
			<div class="form-group">
				<label class="control-label col-md-6"><%= WorkOrderDate%> <%= colon %> </label>
				<div class="col-md-6" >
					<form:input path="workOrderDate" id="WorkOrderDate" class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}"/>
						<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId"/>
						<form:input path="approverEmpMail" type="hidden" name="approverEmpMail" id="approverEmpMail"/>
						<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo"/>
						<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom"/>
						<form:input path="siteId" id="siteId"  type="hidden" />
						<form:input path="siteName" type="hidden"/>
						<form:input path="boqNo" id="boqNo" type="hidden" />
						<form:input path="typeOfWork" id="typeOfWork"  type="hidden"/>
						<form:input path="revision" id="revision"  type="hidden"/>
						<form:input path="oldWorkOrderNo" id="oldWorkOrderNo"  type="hidden"/>
						<form:input path="versionNumber" id="versionNumber"  type="hidden"/>
						<form:input path="workOrderStatus" id="workOrderStatus"  type="hidden"/>
						<form:input path="isSaveOrUpdateOperation" id="isSaveOrUpdateOperation"  type="hidden"/>
						<form:input path="QS_Temp_Issue_Id" id="QS_Temp_Issue_Id"  type="hidden" />
						<form:input path="isCommonApproval" id="isCommonApproval"  type="hidden"  value="${isCommonApproval}"/>
						
						<input type="hidden" name="isApproveWorkOrder" id="isApproveWorkOrder" value="true">
						<input type="hidden" name="actualtempIssueId" id="actualtempIssueId" value="${WorkOrderBean.QS_Temp_Issue_Id}">
						
						<form:input path="materialWoAmount" id="materialWoAmount"  type="hidden"/>
						<form:input path="laborWoAmount" id="laborWoAmount"  type="hidden"/>
						
					 	<form:input path="woBillBilledAmount" id="woBillBilledAmount"  type="hidden"/>
						<form:input path="woBillPaidAmount" id="woBillPaidAmount"  type="hidden"/>
						<input type="hidden" name="isWorkOrderDataChanged" id="isWorkOrderDataChanged">
						<input name="TotalAmountOfWorkOrder"  type="hidden"  id="sumofTotalAmount"/>
				</div>
		   </div>
		   </div>
		 <c:if test="${WorkOrderBean.typeOfWork ne 'MATERIAL' }">
		 <div class="col-md-6">
	  		<div class="form-group">
			  <label class="control-label col-md-6"><%= vendorName %> <%= colon %>  </label>
				<div class="col-md-6" >
					<form:input path="contractorName" id="contractorName"  onkeyup="populateContractor(this);" readonly="true" autocomplete="off" class="form-control"  title="${WorkOrderBean.contractorName}"/>
						<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId"/>
						<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO"/>
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
					 <div class="col-md-6" >
					 <form:input path="contractorPhoneNo"  name="contractorPhoneNo"  class="form-control" id="contractorPhoneNo" readonly="true" title="${WorkOrderBean.contractorPhoneNo}"/>
					
					</div>
				</div>
		 	</div>
		 </c:if>
		 	</div>
		 	<input type="hidden" name="TotalBOQAmount" id="TotalBOQAmount">
			<input type="hidden" name="TotalNMR_WO_initiatedAmount" id="TotalNMR_WO_initiatedAmount">
			<c:set value="NMR" var="TypeOfWork"></c:set>
		    <div class="clearfix"></div>
		  <div class="">
		   <div class="table-responsive"> <!-- /*tblprodindissudiv*/ -->
				<table id="indentIssueTableId" class="table table-bordered tblprodindissu fixed_headers" style="width:2200px;">
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
  				<th><%=comments %></th>
  				<th><%= actions %></th>
  				
  				</tr>
			</thead>
  			<tbody class="tbl-fixedheader-tbody">
  			<c:set value="0" var="indexnumber"></c:set>
  			<c:forEach items="${workOrderCreationList}"  var="workOrderDetail">  
  				<c:set value="${indexnumber+1}" var="indexnumber"></c:set>
  					<input  name="isDelete${indexnumber}" id="isDelete${indexnumber}" readonly="true" value="z" class="form-control input-visibilty${indexnumber}" type="hidden" />
			
				<tr id="tr-class${indexnumber}"  class="workorderrowcls" >  
					<td>				
						<div id="snoDivId1">${indexnumber}</div>
						<input type="hidden" name="nmrRowPaymentInitiatedDetils${indexnumber}" id="nmrRowPaymentInitiatedDetils${indexnumber}" value="${workOrderDetail.nmrPaymentDetails}">
						<input name="dispplayedRows" id="dispplayedRows" type="hidden" value="${indexnumber}">	
						<input type="hidden" name="actualWorkOrderNo" value="${WorkOrderBean.siteWiseWONO}">
						<input type="hidden" name="nextApprovelEmpId" value="${WorkOrderBean.approverEmpId}">
							
						<input type="hidden" name="QS_Temp_Issue_Dtls_Id${indexnumber}" id="QS_Temp_Issue_Dtls_Id${indexnumber}" value="${workOrderDetail.QS_Temp_Issue_Dtls_Id}">
						<input type="hidden" name="actualWorkAreaID" value="${workOrderDetail.workAreaId}">
					</td>
						<td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>" data-placement="left">
						<input type="hidden" name="actualwoMajorHead${indexnumber}" value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>" >
						<select id="combobox${indexnumber}" name="WO_MajorHead${indexnumber}"  class="btn-tooltip btn-visibilty${indexnumber}" title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>"> 
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
						<select name="WO_MinorHead${indexnumber}"  id="comboboxsubProd${indexnumber}" class=" btn-tooltip form-control btn-visibilty${indexnumber}" >
							<option value="${workOrderDetail.woMinorHeads}$<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>">${workOrderDetail.WO_MinorHead1}</option>
						</select>
					</td>
				   <td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_Desc1}' />">
							<input type="hidden" name="actualWO_Desc${indexnumber}" id="actualWO_Desc${indexnumber}" value="${workOrderDetail.wODescription}$<c:out value='${workOrderDetail.WO_Desc1}'></c:out>">
							<select name="WO_Desc${indexnumber}" id="comboboxsubSubProd${indexnumber}" class="form-control btn-visibilty${indexnumber}">
							<option value="${workOrderDetail.wODescription}$<c:out value='${workOrderDetail.WO_Desc1}'></c:out>">${workOrderDetail.WO_Desc1}</option>
						</select>
					</td>
					<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
					<td>		
						<input type="hidden"  name="woManualDesc${indexnumber}" id="woManualDesc${indexnumber}" value="<c:out value='${workOrderDetail.wOManualDescription}'></c:out>"  />		
						<input type="text" class="form-control" name="woManualDesc${indexnumber}" id="woManualDesc${indexnumber}" value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}" title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"  data-toggle="modal" data-target="modalForScopeWork${indexnumber}"  onclick="showScopeOfWork(${indexnumber})"   readonly="true"/>
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
								      <c:set var="indexOfScopeOfWork" value="1"></c:set>
								       <div class="modal-body" style="overflow:hidden;">
								       
								       
								          <div id="textboxDiv${indexnumber}">
								          <c:forEach var="scopeWork" items="${scopeOfWorkParts}">
								          <c:set var="indexOfScopeOfWork" value="${indexOfScopeOfWork+1}"></c:set>
									        <c:if test="${scopeWork.trim().length()>0}">
									          <div class="col-md-12 mrg-btm">
									          <div class="form-group"  id="newtxtbox${indexOfScopeOfWork}">
									            <div class="col-md-11">  <input type="text" class="form-control txt-border scopeofworkid${indexnumber}" id="${indexnumber}defaultScopeOfWOrk${indexOfScopeOfWork}" value="<c:out value='${scopeWork}'></c:out>" name="ScopeOfWork${indexnumber}" readonly><%-- ${scopeWork.trim().length()} --%></div>
									           <div class="col-md-1"><button type="button" class="btn btn-danger Addscope_txt" onclick="deleteScopeOfWork(${indexOfScopeOfWork})"><i class="fa fa-close"></i></button></div>
									          </div>								          
									          </div>
									         </c:if>
								           </c:forEach>
								          </div>
								          <input type="hidden" id="hiddenrownum">
								         
								          <div class="col-md-12">
											   <div class="form-group">
											   <div class="col-md-11"><input type="text" class="form-control txt-border txt-height scopeofworkid${indexnumber}" id="${indexnumber}defaultScopeOfWOrk1" name="ScopeOfWork${indexnumber}"></div> 
											   <div class="col-md-1"><button class="btn btn-success Addscope_txt" type="button" onclick="appendtextbox(${indexnumber})"><i class="fa fa-plus"></i></button></div>
											 </div>
										 </div>
								      </div>
								      <div class="modal-footer">
								        <div class="text-center center-block">
								         <button type="button" class="btn btn-warning" data-dismiss="modal">Submit</button>
								       <!--  <button type="button" class="btn btn-warning" >Close</button> -->
								        </div>
								      </div>
								    </div>
								
								  </div>
								</div>
								<!-- modal popup for scope of work end -->
					</td>
					</c:if>
					<td >
					<input type="hidden"  id="actualunitsOfMeasurement${indexnumber}" name="actualunitsOfMeasurement${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">
						<select name="UnitsOfMeasurement${indexnumber}" id="UnitsOfMeasurementId${indexnumber}" class="form-control btn-visibilty${indexnumber}"   data-toggle="tooltip" data-placement="bottom" title="<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">
						<option value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'></c:out>">${workOrderDetail.unitsOfMeasurement1}</option>
						</select>
					</td>
					
				 <td class="w-70" data-toggle="tooltip">
						<input type="hidden" name="actualQuantity${indexnumber}" id="actualQuantity${indexnumber}" value="${workOrderDetail.quantity}" class="actualworkordertotalqty">
				
					    <input id="Quantity${indexnumber}"   onkeypress="return isNumberCheck(this, event)" onblur="calCulateTotalAmout('qty',this.value,${indexnumber})"  name="Quantity${indexnumber}" value="${workOrderDetail.quantity}" class="form-control input-visibilty${indexnumber} workordertotalqty copyPasteRestricted" data-toggle="tooltip" data-placement="bottom"  title="${workOrderDetail.quantity}"/> 
						<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
							<span id="ClickHere${indexnumber}" style=""><a href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber},'show')" >Click Here</a><br/></span>
							<a href="javascript:showDetailsFunction(${indexnumber})" style="display: none;" onclick="showDetailsFunction(${indexnumber})" data-toggle="modal" data-target="#myModal-showwo-showquantity" id="showQty${indexnumber}">View Details 	</a>
							<a href="javascript:void(0)" style="cursor: pointer;display: none;" onclick="showWDGroupWiseMaterialDetails(${indexnumber})"  id="showMaterialQty${indexnumber}">Material Details</a>
							<img src="images/spinner.gif" class="spinnercls" id="spinner${indexnumber}"></img>
						</c:if>
						
					 	<div id="appendActualWorkOrderArea${indexnumber}"></div>
						<div id="appendWorkOrderArea2${indexnumber}"></div>
						<div id="appendWorkOrderArea${indexnumber}"></div> 
						<div id="appendWorkOrderWorkArea${indexnumber}"></div>
				</td>
					
		        <c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
				<td>
						<input type="hidden" id="actualAcceptedRate${indexnumber}" name="actualAcceptedRate${indexnumber}" value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}" class="actualworkorderacceptedRate">
				     	<input id="AcceptedRate${indexnumber}"  onkeypress="return isNumberCheck(this, event)"  onblur="calCulateTotalAmout('a_rate',this.value,${indexnumber})" name="AcceptedRate${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber} workorderacceptedRate copyPasteRestricted" value="${ workOrderDetail.totalAmount1/workOrderDetail.quantity}" data-toggle="tooltip" data-placement="bottom"  title="${ workOrderDetail.totalAmount1/workOrderDetail.quantity}"/>
				</td>
			    </c:if>
					<td>
					<input type="hidden" id="actualTotalAmount${indexnumber}" name="actualTotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}" class="actualtotalRowamount">
						<input name="TotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}"  id="TotalAmountId${indexnumber}" readonly="true"   class="form-control totalRowamount" />
						<input type="hidden" name="labourAmount${indexnumber}" id="LaborAmountId${indexnumber}"  class="laborAmount"  value="${workOrderDetail.totalAmount1}">
						<input type="hidden" name="materialAmount${indexnumber}" id="MaterialAmountId${indexnumber}" class="materialAmount" >
						
						<input type="hidden" name="actuallabourAmount${indexnumber}" id="actualLaborAmountId${indexnumber}" value="${workOrderDetail.totalAmount1}">
						<input type="hidden" name="actualmaterialAmount${indexnumber}" id="actualMaterialAmountId${indexnumber}">
						
						<input type="hidden" name="WoRecordContains${indexnumber}" id="WoRecordContains${indexnumber}">
					</td>
					<td>
						<input type="hidden" name="actualComments${indexnumber}" value="${workOrderDetail.comments1}">
						<input id="Comments12${indexnumber}" value="" placeholder="<c:out value='${workOrderDetail.comments1}'></c:out>" title="<c:out value='${workOrderDetail.comments1}'></c:out>" name="Comments12${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber}" data-toggle="tooltip" data-placement="bottom"/>
						<input type="hidden" name="IsComments" value="" id="hiddenCommentsId">
					</td>
					<td>
						<input id="Comments${indexnumber}" value="" title="<c:out value='${workOrderDetail.comments1}'></c:out>" name="Comments${indexnumber}" readonly="true"  class="form-control commmntstooltip input-visibilty${indexnumber}" data-toggle="tooltip" data-placement="bottom"/>
					</td>								
					<td>
						<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${indexnumber}" class="btnaction" onclick="removeRow('${indexnumber}')" ><i class="fa fa-remove"></i></button>
						<button type="button" name="editItemBtn" value="Edit Item" id="editItem${indexnumber}" class="btnaction" onclick="editRow('${indexnumber}')" ><i class="fa fa-pencil"></i></button>
					<c:if test="${workOrderCreationList.size() eq indexnumber}">
						<%-- <c:if test="${WorkOrderBean.workOrderStatus eq 'DraftModify' }"> --%>
							<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId${indexnumber}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<%-- </c:if> --%>
					</c:if>
					</td>
				</tr>
				</c:forEach>
				</tbody>				
			</table>
			</div>
		  
		  </div>
   <!-- 

		<div class="WoGrandTotal">
			 <div style="float:left;width:120px;">Grand Total</div>
			 <div style="float:left;width:20px;">:</div>
			 <div style="float:left;width:160px;" id="WoGrandTotalAmount"></div>
		</div>	 --> 
		<div class="col-md-12" style="padding-left:0px;margin-top:15px;margin-bottom:30px;">
		<c:if test="${WorkOrderBean.typeOfWork ne 'NMR'}">	
			<div class="col-md-5 col-md-offset-7">
				 <div class="col-md-6 fnt-18 text-left">Labor Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="laborWoAmountSpan">${WorkOrderBean.laborWoAmount}</span> </div>
				 <div class="clearfix"></div>
				 <div class="col-md-6 fnt-18 text-left">Material Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="materialWoAmountSpan">${WorkOrderBean.materialWoAmount}</span> </div>
				 <div class="clearfix"></div>
				 <div class="col-md-6 fnt-18 text-left">Grand Total</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="WoGrandTotalAmount">${WorkOrderBean.laborWoAmount+WorkOrderBean.materialWoAmount}</span></div>
			</div>
		</c:if>
		<c:if test="${WorkOrderBean.typeOfWork eq 'NMR'}">
			<div class="WoGrandTotal">
				 <div style="float:left;width:120px;">Grand Total</div>
				 <div style="float:left;width:20px;">:</div>
				 <div style="float:left;width:160px;" id="WoGrandTotalAmount">${WorkOrderBean.laborWoAmount+WorkOrderBean.materialWoAmount}</div>
			</div>
		</c:if>
		</div>
		<div class="col-md-12" style="padding-left:0px;margin-top:15px;"> <!-- tblindentissunote -->
				<div class="col-md-8" style="padding-left:0px;">
				 <div class="form-group">
				 <label class="col-md-2 pt-10">
						<%= purpose %> <%= colon %>
					</label>
					<div class="col-md-10">
						 <textarea name="Purpose" class="form-control resize-vertical" id="purpose" href="#" data-toggle="tooltip" title="<c:out value='${WorkOrderLevelPurpose}'></c:out>" placeholder="<c:out value='${WorkOrderLevelPurpose}'></c:out>"  autocomplete="off" ></textarea>
			</div>
			</div>
		</div>
	</div>
	
	
		<!-- this is common file for showing images  -->
		<%@include file="ImgPdfCommonJsp.jsp" %>





<!-- Model -->
<!-- Model For Work description wise group wise material details -->
<div id="ViewMaterialProductDetails" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:95%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">Material Area Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive" id="materialAreaDetails">
       <table class="table table-bordered table-createwo-showquantity">
       <thead style="color: black;">
			<tr>
				<th>S.No</th>
				<th>Work Description</th>
				<th>Group Name</th>
				<th>UOM</th>
				<!-- <th>Block</th>
				<th>Floor</th>
				<th>Flat</th> -->
				<th>Total Quantity</th>
				<!-- <th>Issued Quantity</th>
				<th>Available Quantity</th> -->
			</tr>
		</thead>
       <tbody id="showWDGroupWiseMaterialDetails">
      
       
       </tbody>
       </table>
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>


<!-- Model -->
<!-- Model For Work description wise group wise area wise material details -->
<div id="ViewAreaWiseMaterialDetails" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:95%;">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">Area Wise Material Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive">
       <table class="table table-bordered table-createwo-showquantity" >
       <thead style="color: black;">
			<tr>
				<th>S.No</th>
				<th>Work Description</th>
				<th>Group Name</th>
				<th>UOM</th>
				<th>Block</th>
				<th>Floor</th>
				<th>Flat</th>
				<th>Total Quantity</th>
				<!-- <th>Issued Quantity</th>
				<th>Available Quantity</th> -->
			</tr>
		</thead>
       <tbody id="showAreaWiseWDGroupMaterialDetails">
      
       
       </tbody>
       </table>
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>




		
<input type="hidden" name="imagesAlreadyPresent"	value="<%=imagecount%>" />

<!-- modal popup for approve page start -->
<div id="myModal-showeorkorder" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg">

    <!-- Modal content-->
    <div class="modal-content">
     <div class="modal-header header-modalworkOrder text-center" >
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Terms & Conditions  <input type="hidden" id="termsAndConditionLength" value="${listTermsAndCondition.size()}"></h4>
      </div>
     
     <div class="modal-body body-modal-workorder">
     
      <div class="col-md-12 appen-div-workorder">
      
        <c:forEach items="${listTermsAndCondition }" var="TAC">
        <c:if test="${not empty TAC.TERMS_CONDITION_DESC.trim()}">
        <input type="hidden" name="actualterms_condition_id" value="${TAC.TERMS_CONDITION_ID}">
		<input type="hidden" value="${TAC.TERMS_CONDITION_DESC}" name="actualTC" id="terms${TAC.indexNumber}" ><br>
      
       	<div class="col-md-12  no-padding-left remove-filed mrg-t-10">
			<div class="col-md-11">
					<input type="text" autocomplete="off" class="form-control"  onkeyup="workordertermstitle(this)"  title="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" value="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" name="termsAndCOnditions"  id="terms${TAC.indexNumber}"  >
			</div>
			<div class="col-md-1">
				<button type="button" class="btn btn-danger remove-button remove_field" id="newTC"><i class="fa fa-remove "></i></button>
			</div>
		</div>
	  </c:if>
	  </c:forEach>
      
      </div>
       <div class="col-md-12">
        <div class="input_fields_wrap">
        <div class="col-md-12 padd-modal-workorder mrg-t-10" style="padding-left:15px !important;">
          <div class="form-group">
          <div class="col-md-11"><input type="text"  autocomplete="off" class="form-control" id="workorder_modal_text1"  onkeyup="workordertermstitle(this)"  name="termsAndCOnditions"  /></div>
          <div class="col-md-1"><button type="button" class="btn btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
        
</div>
       </div>
</div>
    
    
         <div class="col-md-11 margin-close">
         <span class="spanheading mrg-btm-10">(Optional)If you want to add CC In emails.</span>
         <input type="text" class="form-control mrg-t-10" id="email-popup-workorder" name="optionalCCmails" value="${optionalCCmails}">
         </div>
          <!-- <div class="col-md-11 margin-close">
         <span class="spanheading mrg-btm-10">Subject</span>
         <input type="text" class="form-control mrg-t-10" id="email-popup-workorder" placeholder="Please enter the subject">
         </div> -->
       
      </div>
      </div>
      <div class="modal-footer">
      <div class="col-md-12 text-center center-block">
        <button type="button" class="btn btn-warning" data-dismiss="modal" id="finalWOSubmission"  onclick="saveRecords('SaveClicked')">Submit</button>
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
						 
					<input type="text" value="${TAC.TERMS_CONDITION_DESC}" name="termsAndCOnditions" size="${TAC.TERMS_CONDITION_DESC.length()*2}"  id="terms${TAC.indexNumber}" ><br>
		</c:forEach>				 --%>
			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			<input type="hidden" name="operationTypeForWorkOrder" id="operationTypeForWorkOrder">
		</form:form>
		<div class="col-md-12 text-left">
		<c:if test="${deletedWorkOrderDetailsList.size()!=0 }">
			<label> Modification Details :</label>
		</c:if>
			<ul>
			 <c:forEach items="${deletedWorkOrderDetailsList}"  var="workOrderDeletedDetail">
					<li>${workOrderDeletedDetail.remarks }</li>
			 </c:forEach>
			 
			</ul>
	 </div>
	
	<div class="col-md-12" style="margin-top:30px;margin-bottom:30px;">
		<c:if test="${WorkOrderBean.workOrderStatus eq 'DraftModify' }">
	 		<button type="button" class="btn btn-warning" value="Draft Work Order" onclick="return validateMajorHeadTable(this.value)"  id="saveBtnId">
	 			<i class="fa fa-floppy-o" aria-hidden="true"></i>&emsp;Draft Work Order
			</button>
				<input type="button" class="btn btn-warning" value="Submit" onclick="return validateMajorHeadTable(this.value)"  id="submitBtnId">
			    <button type="button" class="btn btn-warning" value="Discard" name="rejectWorkOrder" id="rejectBtnId" onclick="reject('SaveClicked',this.value)">
			 	<i class="fa fa-trash" aria-hidden="true"></i>&emsp;Discard
			 </button>
		</c:if>
		<c:if test="${WorkOrderBean.workOrderStatus ne 'DraftModify' && WorkOrderBean.workOrderStatus ne 'ModifyWorkOrder' }">
	 		<input type="button" class="btn btn-warning" value="Approve" onclick="return validateMajorHeadTable(this.value)"  id="saveBtnId">
	 		<c:if test="${WorkOrderBean.typeOfWork ne 'NMR'}">	
	 			<input type="button" class="btn btn-warning" value="Product Details" id="productDetails" onclick="return productDetails();">
	 		</c:if>
	 		<input type="button" class="btn btn-warning" value="Reject" name="rejectWorkOrder" id="rejectBtnId" onclick="reject('SaveClicked',this.value)">
			<c:if test="${WorkOrderBean.workOrderStatus ne 'ModifyWorkOrder' }">
		 			<input type="button" class="btn btn-warning" value="Modify" name="modifyWorkOrder" id="modifyBtnId" onclick="sendTomodifyWorkOrder('SaveClicked',this.value)">
		 	</c:if>
	 	</c:if>
	
	 	<c:if test="${WorkOrderBean.workOrderStatus eq 'ModifyWorkOrder' }">
	 		<input type="button" class="btn btn-warning" value="Modify WorkOrder" onclick="return validateMajorHeadTable(this.value)"  id="saveBtnId">
	 		<c:if test="${WorkOrderBean.typeOfWork ne 'NMR'}">	
	 			<input type="button" class="btn btn-warning" value="Product Details" id="productDetails" onclick="return productDetails();">
	 		</c:if>
	 		<input type="button" class="btn btn-warning" value="Discard Modify WorkOrder" name="rejectWorkOrder" id="rejectBtnId" onclick="reject('SaveClicked',this.value)">
	 	</c:if>
		<%-- 	<c:if test="${WorkOrderBean.workOrderStatus ne 'DraftModify' }">
					<input type="button" class="btn btn-warning" value="Modify" name="modifyWorkOrder" id="modifyBtnId" onclick="sendTomodifyWorkOrder('SaveClicked')">
				</c:if> --%>
	</div>
	
	<div class="clearfix"></div>
    
</div>
</div>
</div>
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<script src="js/custom.js"></script>
<script src="js/WorkOrder/WOCommonCode.js"></script>
  		<script>
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 		
				$(".copyPasteRestricted").bind('paste', function (e) {
					e.preventDefault();
				});
			});			
	 
			 function productDetails(){
					var url="viewProductDetails.spring";
					//$('#workOrderFormId').attr('target', '_blank');
					document.getElementById("workOrderFormId").action = url;
					document.getElementById("workOrderFormId").method = "POST";
					document.getElementById("workOrderFormId").submit();  	 
				 }	
			
			//to distroy loader
			 $(window).load(function () {
				 setTimeout(function(){
						loadWOAmtAndQTY('ds',1);
						var workOrderTypeOfWork="${WorkOrderBean.typeOfWork}"
							if(workOrderTypeOfWork!="NMR"){
								$("[name^=dispplayedRows]").each(function () {
									var rowNum=$(this).val();
						    	 	loadWOWorkArea("false",rowNum);	
						    	 	console.log("This is row wise data loading..."+rowNum);
						    	});
							}else{
								 $(".overlay_ims").hide();	
				 		   		 $(".loader-ims").hide();
							}
					}, 300);
			 });
		
		
		function loadWOAmtAndQTY(childProdId,rownum){
			debugger; 
			$(".workorderacceptedRate, .workordertotalqty").bind('paste', function (e) {
					e.preventDefault();
				});
			
			//rownum=1;
			childProdId = $("#comboboxsubSubProd"+rownum).val().split("$")[0];
			var WO_MinorHead=$("#comboboxsubProd"+rownum).val().split("$")[0];
			var siteId=$("#siteId").val();
			var url = "loadScopeOfWork_AmountAndQty.spring?childProductId="+childProdId+"&site_id="+siteId+"&WO_MinorHead="+WO_MinorHead+"&mesumentId="+WO_MinorHead+"&typeOfWork=${WorkOrderBean.typeOfWork}&boqNo=${WorkOrderBean.boqNo}&isApproveOrRevisePage=true";
			  $.ajax({
				  url : url,
				  type : "get",
				contentType : "application/json",
				success : function(data) {
					debugger;
					var array=new Array();
					array=data.split("@@");
					var quantity=array[1];
					var amount=array[2];
					var totalBOQAmount=array[3];
					var totalNMRWOInitiatedAmount=array[4];
					$("#TotalBOQAmount").val(totalBOQAmount);
					$("#TotalNMR_WO_initiatedAmount").val(totalNMRWOInitiatedAmount);

					
				    $(".workorderacceptedRate").each(function(){
					 	var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					
					$(".workordertotalqty").each(function(){
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					$(".totalRowamount").each(function(){
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					  
					$(".actualworkorderacceptedRate").each(function(){
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
						
					$(".actualworkordertotalqty").each(function(){
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					$(".actualtotalRowamount").each(function(){
						var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
						$(this).val(currentvalue.toFixed(2));
					});
					
					if(rownum==1){
						return ;
					}
		 			data=data.split("@@")[0];
		 			var rowNum=rownum;
		 			//$("#scopeOfWork"+rowNum).val(data);
					 
					 //creating dynamic modal popup written byu thirupathi
						var popupmodaldyn="";
					 
					    popupmodaldyn+='<div id="modalForScopeWork'+rowNum+'" class="modal fade" role="dialog">';
					    popupmodaldyn+='<div class="modal-dialog" style="width:75%;">';
					    popupmodaldyn+='<div class="modal-content">';
					    popupmodaldyn+='<div class="modal-header modalscopeheader">';
					    popupmodaldyn+='<button type="button" class="close" data-dismiss="modal">&times;</button>';
					    popupmodaldyn+='<h4 class="modal-title text-center">Scope Of Work</h4>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<div class="modal-body" style="overflow:hidden;">';
					    if(data){
						    popupmodaldyn+='<div class="col-md-12">';
						    popupmodaldyn+='<div class="form-group">';
						    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-border txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk2" name="ScopeOfWork'+rowNum+'" ></div>';//txt box
						    popupmodaldyn+='<div class="col-md-1"></div>'; //<button class="btn btn-success Addscope_txt" type="button" onclick="appendtextbox('+rowNum+')"><i class="fa fa-plus"></i></button>
						    popupmodaldyn+='</div>';
							popupmodaldyn+='</div>';
					    }
					    popupmodaldyn+='<div id="textboxDiv'+rowNum+'">';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<div class="col-md-12">';
					    popupmodaldyn+='<div class="form-group">';
					    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-border txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk1" name="ScopeOfWork'+rowNum+'"></div>';//txt box value="'+strData+'" title="'+strData+'"
					    popupmodaldyn+='<div class="col-md-1"><button class="btn btn-success Addscope_txt" type="button" onclick="appendtextbox('+rowNum+')"><i class="fa fa-plus"></i></button></div>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<input type="hidden" id="hiddenrownum">';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<div class="modal-footer">';
					    popupmodaldyn+='<div class="text-center center-block">';
					    popupmodaldyn+='<button type="button" class="btn btn-warning submit-close" id="ScopeOfWorkBtn'+rowNum+'"  onclick="submitScopeOfWork('+rowNum+')" >Submit</button>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='</div>';	   				   																						
					    $("#modalpopup"+rowNum).html(popupmodaldyn);	
					    $("#"+rowNum+"defaultScopeOfWOrk2").val(data);
					    $("#"+rowNum+"defaultScopeOfWOrk2").attr("title",data);
					
					

				}
			  });
		}//loadWOAmtAndQTY
		
		var serialNumber=1;
		function appendtextbox(btn){
			debugger;
			var textlength=$('.scopeofworkid'+btn).length;
			var defaultSOW=$("#"+btn+"defaultScopeOfWOrk1").val();
			var textlength;
			if($("#textboxDiv"+btn).find(".scopeofworkid"+btn).length!=0){
				textlength=$("#textboxDiv"+btn).find(".scopeofworkid"+btn+":last").attr("id").split("defaultScopeOfWOrk")[1];
			}else{
				var textBox2=$("#"+btn+"defaultScopeOfWOrk2").val();
				if(textBox2){
					textlength=2;
				}else{
					textlength=1;
				}
			}	
			
			if(defaultSOW.length==0){
				
			alert("Please enter scope of work.");
			$("#"+btn+"defaultScopeOfWOrk1").focus();
				return false;
			}
			//defaultSOW=defaultSOW.replace(/[\"\""]/g, '');
			var appendtextid=parseInt(textlength)+1;
			 $("#textboxDiv"+btn).append('<div class="col-md-12" id="newtextId"><div class="form-group" id="newtxtbox'+appendtextid+'"><div class="col-md-11"><input type="text" name="ScopeOfWork'+btn+'"   id="'+btn+'defaultScopeOfWOrk'+appendtextid+'" class="form-control txt-border txt-height scopeofworkid1" value="'+defaultSOW+'" title="'+defaultSOW+'"></div><div class="col-md-1"><button class="btn btn-danger" type="button" onclick="deleteScopeOfWork('+appendtextid+')"><i class="fa fa-close"></i></button></div></div></div>');  
		    serialNumber=1;
		    $("#"+btn+"defaultScopeOfWOrk1").val('');
		    $("#"+btn+"defaultScopeOfWOrk1").removeAttr('title');
		    $("#"+btn+"defaultScopeOfWOrk1").focus();
		   // $("#defaultScopeOfWOrk"+btn+appendtextid).val(defaultSOW);
		}
		function submitScopeOfWork(rowNum){
			debugger;
			$("#modalForScopeWork"+rowNum).modal("hide");
			//	$("#modal-certificatepayment-adv").modal('hide');
		}
		//delete scopeof work order
		function deleteScopeOfWork(btn){
			$("#newtxtbox"+btn).remove();
		}
		
		//this method for NMR 
		var rowsIncre=1;
		function calCulateTotalAmout(type,acceptedRate,rownum){
		    var TypeOfWork="${TypeOfWork}";
			var typeOfWork="${WorkOrderBean.typeOfWork}";
			if(TypeOfWork!=typeOfWork){
				return false;
			} 
			
			
			if($("#Quantity"+rownum).val()==''||$("#Quantity"+rownum).val()=='0'||$("#Quantity"+rownum).val()=='0.00'){
				alert("Please enter quantity");
				$("#Quantity"+rownum).focus();
				//$("#AcceptedRate"+rownum).val("");
				return false;
			}
			 if(type=="a_rate")
			if($("#AcceptedRate"+rownum).val()==''||$("#AcceptedRate"+rownum).val()=='0'||$("#AcceptedRate"+rownum).val()=='0.00'){
 				alert("Please enter accepted rate");
 				$("#AcceptedRate"+rownum).focus();
 				return error=false;
 			}
			//this code is for revise work order
				var previousQty=0;
				var PREVQTY=$("#nmrRowPaymentInitiatedDetils"+rownum).val();
				var previousAcceptedRate=0;
				
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
							}
						}else{
							prevAreaQuantity = PREVQTY.split("##");
							previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
							previousAcceptedRate=prevAreaQuantity[2];
						}
						var quantity=parseFloat($("#Quantity"+rownum).val());
						var accpeptedRate=parseFloat($("#AcceptedRate"+rownum).val());
						
						 if(type=="a_rate"){
						  	  var actualAcceptedRate= $("#actualAcceptedRate"+rownum).val();
							  if(accpeptedRate!=previousAcceptedRate&&PREVQTY.length!=0){
										alert("You can not change the rate, payment already initiated for this quantity.");
										$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
										$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
										$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
										$("#AcceptedRate"+rownum).prop("readonly",true);
										return false;
								}		
						  }
						
						
						if(previousQty>quantity){
							alert("You can not reduce the qunatity, payment already initiated for this quantity.");
							$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
							$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
							$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
							return false;
						}
						if(previousQty!=0){
							if(previousAcceptedRate>accpeptedRate){
								alert("You can not reduce the accepted rate, payment already initiated for this quantity.");
								$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
								$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
								$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
								return false;
							}
						} 
					}
				//	previousQty=previousQty.toFixed(2);
					previousAcceptedRate=parseFloat(previousAcceptedRate);
				} catch (e) {
					console.log(e);
				}
				
				var quantity=parseFloat($("#Quantity"+rownum).val());
				var accpeptedRate=parseFloat($("#AcceptedRate"+rownum).val());
				  
			    // Retrieve
			    if(type=="qty"){
			     	var qty=$("#AcceptedRate"+rownum).val();
					$("#TotalAmountId"+rownum).val(qty*acceptedRate);
			    	
			    }else{
				  	var qty=$("#Quantity"+rownum).val();
					$("#TotalAmountId"+rownum).val(qty*acceptedRate);
			    }
			    
				//loading actual and current amount of work row(WD) If they are diffrent hide modify button
				var actualTotalAmount=parseFloat($("#actualTotalAmount"+rownum).val());
				var TotalAmountId=parseFloat($("#TotalAmountId"+rownum).val());
				if(actualTotalAmount!=TotalAmountId){
					$("#modifyBtnId").hide();
					$("#rejectBtnId").hide();
				}
		    
			
		   	var sumofTotalAmount=0;
		   	var sumofActualTotalAmount=0;
		   	var materialWoAmount=0;
		   	var laborWoAmount=0;
		   	debugger;
		   	$(".workorderrowcls").each(function(){
		   		var currentId=$(this).attr("id").split("tr-class")[1];	
		   		sumofActualTotalAmount+=parseFloat($("#actualTotalAmount"+currentId).val()==undefined?0:$("#actualTotalAmount"+currentId).val());
				if($("#isDelete"+currentId).val()!="d"){
					sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
				}
			});
	 
		   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
			$("#sumofTotalAmount").val(sumofTotalAmount.toFixed(2));
			  
			var totalBOQAmount=$("#TotalBOQAmount").val()==""?0:parseFloat($("#TotalBOQAmount").val());
			var totalNMRWOInitiatedAmount=$("#TotalNMR_WO_initiatedAmount").val()==""?0:parseFloat($("#TotalNMR_WO_initiatedAmount").val());
			var initiateAmount=totalBOQAmount-totalNMRWOInitiatedAmount;
			if(sumofTotalAmount>initiateAmount){
				console.log((totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount))+" "+$("#TotalAmountId"+rownum).val());
				if((totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount))>=parseFloat(sumofTotalAmount)){
					return true;
				}
				alert("Total NMR BOQ Amount "+(totalBOQAmount)+" Initiated Amount is "+(totalNMRWOInitiatedAmount-sumofActualTotalAmount)+" you can initiate now = "+(totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount)));
				$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
				$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
				$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
				return error=false;
			}
			sumofTotalAmount=0;
			$(".workorderrowcls").each(function(){
		   		var currentId=$(this).attr("id").split("tr-class")[1];	
				if($("#isDelete"+currentId).val()!="d"){
					sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
				}
			});
		   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
			
		}//calCulateTotalAmout
		

		//this method is for validating values while selecting checkbox
		function validateWorkAreaVal(val,row,rowNum) {
			debugger;
			var sendDataToBackEnd=new Array();
			var allocatedArea = parseFloat($("#LABOR" + val + "ALLOCATE_AREA").val()==""?0:$("#LABOR"  + val + "ALLOCATE_AREA").val());
			var actualArea= parseFloat($("#LABOR" + val + "WORK_AREA").text()==""?0:$("#LABOR"  + val + "WORK_AREA").text());
			var accepted_rate=$("#LABOR" +val+"ACCEPTED_RATE").val();
			var isReviseWorkOrder=$("#revision").val();
			
			if($("#LABOR" + val + "val").is(":checked")==false){
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
			}else{
				$("#MATERIAL" + val + "val").prop("checked", true);	
			}
		 	if(isReviseWorkOrder!="0"&&isReviseWorkOrder!=null&&isReviseWorkOrder!=""){
				var paymentdone= parseFloat($("#LABOR"  + val + "PAYMENTDONE").text()==""?0:$("#LABOR"  + val + "PAYMENTDONE").text());
				
				if(allocatedArea<paymentdone){
					alert("You can not reduce  the work area, payment already initiated for this area");
	            	$("#LABOR" +val+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
	            	return false;
				}
				if(paymentdone!=0){
					alert("You can't remove work area, Payment already initiated.");
					$("#LABOR"  + val + "val").prop("checked", true);
					$("#MATERIAL"  + val + "val").prop("checked", true);
					return false;
				}
				var advPreviousBillAmount=parseFloat($("#LABOR" + val + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + val + "advPreviousBillAmount").val());;
	            if(advPreviousBillAmount!=0){
	            	alert("You can not remove this work area, already payment initiated.");
	 				$("#LABOR"  + val + "val").prop("checked", true);
	 				$("#MATERIAL"  + val + "val").prop("checked", true);
	 				flag="false";
	 				return false;
	            }
	            var workAreaForDB=$("#MATERIAL"+val+"WOID").val()==undefined?"":$("#MATERIAL"+val+"WOID").val();
		    var workIssueDetailsId=$("#MATERIAL"+val+"OLD_QS_WO_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+val+"OLD_QS_WO_ISSUE_DTLS_ID").val();
	            var seletedWorkArea= $("#MATERIAL"+val+"ALLOCATE_AREA").val()==undefined?"":$("#MATERIAL"+val+"ALLOCATE_AREA").val();
	   			if(workAreaForDB.length!=0&&workIssueDetailsId.length!=0&&seletedWorkArea.length!=0){
	   				var data ={inputBoxWorkAreaGroupId:val,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
	   			 	sendDataToBackEnd.push(data);	
	   			}
			}//revise work order condition
			 
			  if(sendDataToBackEnd.length!=0){
				  var canIcloseModelPopup= ajaxCallForValidatingWO_Material(row,sendDataToBackEnd,'isMaterialIssued')
				  if(canIcloseModelPopup==false){
					return false;
				  }   
			  }
		
	      var actualAreaAlocatedQTYPrice =$("#LABOR" + val + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#LABOR" + val + "ACTUAL_ACCEPTED_RATE").val();
              var actualAreaAlocatedQTY=$("#LABOR" + val + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + val + "ACTUAL_ALLOCATE_AREA").val();
              
              actualAreaAlocatedQTYPrice=actualAreaAlocatedQTYPrice==undefined?0:actualAreaAlocatedQTYPrice;
              actualAreaAlocatedQTY=actualAreaAlocatedQTY==undefined?0:actualAreaAlocatedQTY;
              
              var validateAvailbleArea=actualAreaAlocatedQTY+actualArea;
              if(allocatedArea>validateAvailbleArea){
              	alert("You can't allocate are more the available area.");
              	$("#LABOR" + val + "ALLOCATE_AREA").val(actualAllocatedAre);
              	$("#LABOR" + val + "val").prop("checked", false);
              	$("#MATERIAL"  + val + "val").prop("checked", false);
				return false;
              }
	 
			var len=$("input[name='chk1"+rowNum+"']:checked").length;
			if(len==0){
				alert("You can't remove last work order area.");
				$("#LABOR" + val + "val").prop("checked", true);
				$("#MATERIAL" + val + "val").prop("checked", true);
				return false;
			}
			if($("#LABOR" + val + "val").is(":checked")==false){
				alert("Removed work area from list");
				$("#LABOR"+val+"ACCEPTED_RATE").val('0');
				$("#MATERIAL" + val + "ALLOCATE_AREA").val("0");
				return false;
			}else{
				var qty=parseFloat($("#Quantity"+row).val());
			}
			
			
			if (isNaN(allocatedArea)&&allocatedArea!=0) {
				alert("Please enter only numbers.");
				$("#LABOR" + val + "ALLOCATE_AREA").val("0");
			 	$("#LABOR" + val + "val").prop("checked", false);
			 	$("#MATERIAL" + val + "val").prop("checked", false);
			}else if(allocatedArea==0){
				alert("Please enter correct value.");
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
			
			if(!isNum(accepted_rate)){
					alert("Please enter accepted rate.");
					$("#LABOR"+val+"ACCEPTED_RATE").focus();
					$("#LABOR" + val + "val").prop("checked", false);
					$("#MATERIAL" + val + "val").prop("checked", false);
					return false;
				}
		}//validateWorkAreaVal
		
		function isNum(value) {
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value)
		}
		//for showing the selected items
		function showDetailsFunction(rowsIncre) {
			
			
			 var len=$("#lengthOfSelectedArea"+rowsIncre).val();
			
			var htmlData="";
			for (var i = 1; i <= len; i++) {
					var block_name=$("#BLOCK_NAMET"+rowsIncre+i+"").val();
					var floor_name=$("#FLOOR_NAMET"+rowsIncre+i+"").val();
					var flat_id=$("#FLAT_IDT"+rowsIncre+i+"").val();
					var seletedArea=$("#selectedAreaT"+rowsIncre+i+"").val();
					var wo_measurmen_name=$("#wo_measurmen_nameT"+rowsIncre+i+"").val();
					var recordType=$("#recordTypeT"+rowsIncre+i+"").val();
					var accepted_rate=$("#accepted_rateT"+rowsIncre+i+"").val();
					htmlData+=" <tr><td>"+block_name+"</td>	<td>"+floor_name+"</td>	<td>"+flat_id+"</td> <td>"+wo_measurmen_name+"</td><td>"+recordType+"</td> <td>"+seletedArea+"</td><td>"+accepted_rate+"</td </tr>";		
				}
			
			$("#tblShowDetails").html(htmlData);
		}//showDetailsFunction
		
		function showScopeOfWork(row){
			$('#modalForScopeWork'+row).modal('show'); 
		}
			
			
			function validateWorkArea(val,workAreaId,type){
				debugger;
				var currentName=$("#"+workAreaId+"Hidden").val();
				var ACTUAL_AREA = parseFloat($("#LABOR" + workAreaId + "ACTUAL_AREA").text()==""?0:$("#LABOR" + workAreaId + "ACTUAL_AREA").text());
				var allocatedArea = parseFloat($("#LABOR" + workAreaId + "ALLOCATE_AREA").val()==""?0:$("#LABOR" + workAreaId + "ALLOCATE_AREA").val());				
				var BOQRate = parseFloat($("#LABOR" + workAreaId + "BOQ_RATE").val()==""?0:$("#LABOR" + workAreaId + "BOQ_RATE").val());
				var availbleArea= parseFloat($("#LABOR" + workAreaId + "WORK_AREA").text()==""?0:$("#LABOR" + workAreaId + "WORK_AREA").text());				
				var accepted_rate=parseFloat($("#LABOR" + workAreaId + "ACCEPTED_RATE").val()==""?0:$("#LABOR" + workAreaId + "ACCEPTED_RATE").val());
				
				var availbleArea1= parseFloat($("#MATERIAL" + workAreaId + "WORK_AREA").text()==""?0:$("#MATERIAL" + workAreaId + "WORK_AREA").text());
				var allocatedArea1 = parseFloat($("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val());
				
				var actualAreaAlocatedQTYPrice =$("#LABOR" + workAreaId + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#LABOR" + workAreaId + "ACTUAL_ACCEPTED_RATE").val();
		                var actualAreaAlocatedQTY=$("#LABOR" + workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + workAreaId + "ACTUAL_ALLOCATE_AREA").val();
		                actualAreaAlocatedQTYPrice=actualAreaAlocatedQTYPrice==undefined?0:actualAreaAlocatedQTYPrice;
		                actualAreaAlocatedQTY=actualAreaAlocatedQTY==undefined?0:actualAreaAlocatedQTY;
		                actualAreaAlocatedQTY=parseFloat(actualAreaAlocatedQTY);
				var totalArea=availbleArea+actualAreaAlocatedQTY;
				if(allocatedArea>availbleArea){
			            	alert("you can't allocate area more than of availbale area.");		            	
			         	$("#LABOR"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
			            	allocatedArea=actualAreaAlocatedQTY;
			            }
				var currentSplitId=workAreaId.split(currentName)[1];
				//resetting the values
				if(type!="rate")
					 $("[name^="+currentName+"]").each(function () {
					  	    var splitid=$(this).attr("id").split(currentName)[1].split("ALLOCATE_AREA")[0];
					  	    var recordID=$(this).attr("id").split(currentName)[0];
					  	   	if(splitid>currentSplitId){					   	 	
					   	   		var paymentDone=$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"PAYMENTDONE").text();
						   	   	 if(paymentDone=='0'||paymentDone=='0.00'){
						   	   		$("#"+$(this).attr("id")).val('0');
						   	   	}
						   	 $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0');						   	   				   	 
						   	}
						 });
						var rowNum=0;
						 //material formula 
			                var materialAvbleQty=$("#MATERIAL"+workAreaId+"WORK_AREA").text();
			        	var AllocateArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
			        	var AvailableArea=parseFloat($("#LABOR"+workAreaId+"WORK_AREA").text()==''?0:$("#LABOR"+workAreaId+"WORK_AREA").text());
			        	var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			        	$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
			        	
						//assigning avalible values
					var availbleArea1= parseFloat($("#MATERIAL" + workAreaId + "WORK_AREA").text()==""?0:$("#MATERIAL" + workAreaId + "WORK_AREA").text());
				        var allocatedArea1 = parseFloat($("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val());
						if(type!="rate")
						 $("[name^="+currentName+"]").each(function () {
							   
						   	    var splitid=$(this).attr("id").split(currentName)[1].split("ALLOCATE_AREA")[0];
						   	    var recordID=$(this).attr("id").split(currentName)[0];
						   	  /*   if(recordID=="LABOR"){
						   	    	rowNum+=parseFloat($(this).val()==''?0:$(this).val());
						   	    	//availbleArea=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+ "WORK_AREA").text()==""?0:$("#" + $(this).attr("id").split("ALLOCATE_AREA")[0] + "WORK_AREA").text());
						   	    	//allocatedArea=parseFloat($(this).val()==""?0:$(this).val());
						   	    }else{
						   	    	//availbleArea1=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+ "WORK_AREA").text()==""?0:$("#" + $(this).attr("id").split("ALLOCATE_AREA")[0] + "WORK_AREA").text());
						   	    	allocatedArea1=parseFloat($(this).val()==""?0:$(this).val());
						   	    } */
						   	   	if(splitid>currentSplitId){						   	 		
						   	   		var paymentDone=$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"PAYMENTDONE").text();
							   	     if(paymentDone=='0'||paymentDone=='0.00'){
							   	   		$("#"+$(this).attr("id")).val('0');
							   	   	 }
							   	   /*  if(recordID=="LABOR"){
							   	  		$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleArea-rowNum+parseFloat($(this).val()==''?0:$(this).val())).toFixed(2));							   	  	 
							   	    }else{
							   	    	//$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleArea1-allocatedArea1).toFixed(2));
							   	    } */
							   	 if(recordID=="LABOR"){
							   		 $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleArea-allocatedArea).toFixed(2));
							   	 }else{
							   		 $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleArea1-allocatedArea1).toFixed(2));
							   	 }
						   	   	}
						   	});
						
			             if(allocatedArea>availbleArea){
			            	alert("You can't allocate more than availbale area.");
			            	// $("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY); 
			            	if($("#"+workAreaId+"WORK_AREA").val()=='' || $("#"+workAreaId+"WORK_AREA").val()=='0'){
			            		$("#"+workAreaId+"ALLOCATE_AREA").val('0');
			            	}else{
			            		$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
			            	}
			            	var valCount=0;
			            	var availbleAreaHere= parseFloat($("#" + workAreaId + "WORK_AREA").text());
			            	$("[name^="+currentName+"]").each(function () {								
								valCount+=parseFloat($(this).val()==''?0:$(this).val());
						   	    var splitid=$( this).attr("id").split(currentName)[1].split("ALLOCATE_AREA")[0];
						   	   	if(splitid>currentSplitId){
						   	   		$("#"+$(this).attr("id")).val('0');
						   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleAreaHere-actualAreaAlocatedQTY).toFixed(2));
						   	   	}
						   	});
			            	return false;
			            } 
						
			            //this code for revised work order for validating Area And Accepted Rate
			            var isReviseWorkOrder=$("#revision").val();
						if(isReviseWorkOrder!="0"&&isReviseWorkOrder!=null&&isReviseWorkOrder!=""){
							var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text());
							
							if(allocatedArea<paymentdone){
								alert("You can not reduce  the work area, payment already initiated for this area");
				            	$("#LABOR"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
				            	return false;
							}
							//var actualAreaAlocatedQTYPrice=$("#actualAreaAlocatedQTYPrice"+row).val()==""?0:$("#actualAreaAlocatedQTYPrice"+row).val();
							//actualAreaAlocatedQTYPrice=parseFloat(actualAreaAlocatedQTYPrice);
							
							if(paymentdone!=0||paymentdone!=0.0)
							if(accepted_rate<actualAreaAlocatedQTYPrice){
								alert("You can not reduce  the work area accepted rate, payment already initiated for this area");
								$("#LABOR"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
				            	return false;
				            }
						}
			           /*  if(allocatedArea==0){
			            	alert("Please enter correct value.");
			            	$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
			            	return false;
			            } */
			            if(accepted_rate>BOQRate){
			            	alert("You can't initiate more than BOQ rate.");
			            	$("#LABOR"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
			            	return false;
			            }
			            /* if(accepted_rate==0){
			            	alert("Please enter accepted rate.");
		                	$("#"+workAreaId+"ACCEPTED_RATE").focus();
		                	$("#" + workAreaId + "val").prop("checked", false);
		                	return false
			            } */
			            debugger;
			            //material formula 
			            var materialAvbleQty=$("#MATERIAL"+workAreaId+"WORK_AREA").text();
			        	var AllocateArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
			        	var AvailableArea=parseFloat($("#LABOR"+workAreaId+"WORK_AREA").text()==''?0:$("#LABOR"+workAreaId+"WORK_AREA").text());
			        	var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			        	$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
			            
			            
			            
			   }
			
			
			
		/* 	$("[name^=dispplayedRows]").each(function () {
				
	       	 	var rowNum=$(this).val();
	    	 	loadWOWorkArea("false",rowNum);	
	    	 	
	       	}); 
		*/
			
			function closeWorkAreaPopup(rowsIncre){
				
				$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
			}
		 var lengthOfDisplayedRows=$("[name^=dispplayedRows]").length;	
			function loadWOWorkArea(UOMId, row) {
				var popupmodaldyn="";
			    debugger;
			    var loadNewData="";
			    if(UOMId!="false"){
			    	loadNewData=true;
			    }
				/*  jQuery.noConflict();*/
				 $('#Modal-create-wo-popup').modal('hide'); 
			   
					var appendId=$("#allocatedareaAppend").val();
					var mesurmentId="";
					var UOMId="";
				
					     
					
					 //$("#appendWorkOrderArea"+row).html("");
					$("#QuantityId"+row).val("");
					 
					 mesurmentId=$("#comboboxsubSubProd"+row).val().split("$")[0];
					 UOMId=$("#UnitsOfMeasurementId"+row).val().split("$")[0];
					 var isReviseWorkOrder=$("#revision").val();
					 if(mesurmentId==undefined||mesurmentId==""||mesurmentId==null){
						alert("Please select work descripition.");
						$("#WO_Desc"+row).focus();
						 return false;
					 }
					 if(UOMId==undefined||UOMId==""||UOMId==null){
							alert("Please select UOM.");
							$("#UOMId"+row).focus();
							return false;
					 }
					 var closeString="closebtn";
					 var submitString="submitbtn";
//				<!-- modal popup for create work order table for show start -->
				//<!-- Modal -->
				popupmodaldyn+=' <div id="Modal-create-wo-popup'+row+'" class="modal fade" role="dialog">';
					popupmodaldyn+=' <div class="modal-dialog modal-lg" style="width:95%;">';
				
				    /*  Modal content onclick="return addWorkArea('+row+', 1)" */
				    popupmodaldyn+=' <div class="modal-content">';
				    popupmodaldyn+=' <div class="modal-header">';
				    popupmodaldyn+=' <button type="button" class="close"  onclick="closeWorkAreaPopup('+row+')" >&times;</button>';
					popupmodaldyn+=' <h4 class="modal-title text-center">Work Area </h4>';
					popupmodaldyn+=' </div>';
				   	popupmodaldyn+=' <div class="modal-body">';
				    popupmodaldyn+=' <div class="table-responsive">';
					popupmodaldyn+=' <table class="table" id="floortable">';
				 	popupmodaldyn+=' <thead>';
					popupmodaldyn+=' <tr>';
				    popupmodaldyn+=' <th  class="text-center"><input type="checkbox" id="checkAll'+row+'" style="width:100%;float:none; height:16px;cursor:pointer;" value="'+row+'" onclick="checkAllCheckBox('+row+')"></th>';
				    popupmodaldyn+=' <th  >Block</th>';
				    popupmodaldyn+=' <th >Floor</th>';
				    popupmodaldyn+=' <th  >Flat</th>';
				    popupmodaldyn+=' <th  >Actual Area</th>';
				    popupmodaldyn+=' <th  >Available Area</th>';
	/* 			    popupmodaldyn+='   <th  >Initiated Area</th>'; */
				    popupmodaldyn+=' <th  >Measurement</th>';
				    popupmodaldyn+=' <th  >Record Type</th>';
				    if(isReviseWorkOrder!="0" &&isReviseWorkOrder!=null &&isReviseWorkOrder!="")
				    popupmodaldyn+=' <th  >Payment Initiated Area</th>';
				  
				    popupmodaldyn+=' <th  >Allocate Area</th>';
				    popupmodaldyn+=' <th >BOQ Rate(<%=acceptedRateCurrency %>)</th>';
				    popupmodaldyn+=' <th >Accepted Rate(<%=acceptedRateCurrency %>)</th>';
				    popupmodaldyn+=' <th>Actions</th>';
				    popupmodaldyn+='</thead>';
				    popupmodaldyn+='<tbody id="areaMapping'+row+'" class="fixed-table-body">';
				    popupmodaldyn+='<div class="loader" id="loderId'+row+'" style="display:none;"></div>';
			    	popupmodaldyn+='</tbody>';
				    popupmodaldyn+='</table>';
				    popupmodaldyn+='</div> ';
			    	popupmodaldyn+='</div>';
			    	popupmodaldyn+='<div class="modal-footer">';
			    	popupmodaldyn+='<div class="text-center center-block" id="submitButtonWorkArea">';
			    	popupmodaldyn+='<button type="button" id="hide-table-2" class="btn btn-warning" onclick="return addWorkArea('+row+',\'validateLabor\')">Submit</button>';/*  */
			   		popupmodaldyn+='</div>';
			    	popupmodaldyn+='</div>';
			    	popupmodaldyn+='</div>';
			    	popupmodaldyn+='</div>';
			    	popupmodaldyn+='</div>';
			     	//<!-- modal popup for create work order table for show end -->
				  $("#appendWorkOrderWorkArea"+row).html(popupmodaldyn);
				  $("#loderId"+row).show();
					
					 $("#appendWorkOrderArea"+row).html("");
					 //var site_id=$("#site_id").val();
					 var siteId=$("#siteId").val();
					 var workOrderNo=$("#workOrderNo").val();
					 var workOrderID=$("#QS_Temp_Issue_Dtls_Id"+row).val();
					 var boqNo=$("#boqNo").val();
					 var workOrderTypeOfWork=$("#typeOfWork").val();
					
					var url = "loadWOAreaMapping.spring";
						$.ajax({
						url : url,
						type : "get",
						data:{
								workOrderID:workOrderID,
								mesurmentId:mesurmentId,
								UnitsOfMeasurementId:UOMId,
								siteId:siteId,
								workOrderNo:workOrderNo,
								typeOfWork:workOrderTypeOfWork
							},
						success : function(data) {
							   var materialAmount=0;
							   var totalAreaQty=0;
							   var rowNum=1;	   
							   var workDescriptionid="";
							   var duplicateWorkAreaId=0;
							   var tempHoldingNum=1;
							$.each(data,function(key,value){
						
						var str="";
						   var data1="";
						var pravArea=0;
						var raPraviousArea=0;
						var AdvPraviousArea=0;
						var prevAreaQuantity=new Array();
						var lengthOfThePrevArea=0;
						var customMsg="";
						var raPreviousBillAmount=0;
						var advPreviousBillAmount=0;
						
						try {
							var index=value.PAYMENTDONE.search("@@");
							if(index>=0){//if this value.PREVAREA more than one previous bill QTY 
								prevAreaQuantity=value.PAYMENTDONE.split("@@");
								for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
									let array_element = prevAreaQuantity[ind];
									let tempQtyAndRate=array_element.split("-");
									if(tempQtyAndRate[1]=="ADV"){
										AdvPraviousArea+=parseFloat(tempQtyAndRate[0]);
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
										advPreviousBillAmount+=tempQtyAndRate[0]*rate;
									}else{
										raPraviousArea+=parseFloat(tempQtyAndRate[0]);//array position (0) getting prev QTY
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
										raPreviousBillAmount+=tempQtyAndRate[0]*rate;
									}
								}
								pravArea+=tempQtyAndRate[0];
							}else{
								let tempQtyAndRate = value.PAYMENTDONE.split("-");
								var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
								if("ADV"==tempQtyAndRate[1]){
									AdvPraviousArea+=parseFloat(tempQtyAndRate[0]);
									advPreviousBillAmount+=tempQtyAndRate[0]*rate;
								}else{
									raPraviousArea+=parseFloat(tempQtyAndRate[0]);
									raPreviousBillAmount+=tempQtyAndRate[0]*rate;
								}
							}
							advPreviousBillAmount=advPreviousBillAmount.toFixed(2);
						} catch (e) {
							console.log(e);
						}
						//if(raPraviousArea>AdvPraviousArea){
							//pravArea=raPraviousArea;
						//}else{
							pravArea=raPraviousArea;
						//}
						
							// checking here is there any duplicate work area id is there are not if there there
							debugger;
						//	if(value.RECORD_TYPE!='	MATERIAL')
							if(value.WO_WORK_AREA_GROUP_ID!=workDescriptionid ){
								$("#LABOR"+workDescriptionid+(duplicateWorkAreaId)+"Btn").show();
								workDescriptionid=value.WO_WORK_AREA_GROUP_ID;
								duplicateWorkAreaId=1;	
								tempHoldingNum=1;
							}else{
								$("#"+value.RECORD_TYPE+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Btn").hide();
								if(workOrderTypeOfWork=='MATERIAL'){
									$("#LABOR"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Btn").hide();
								}
								duplicateWorkAreaId=duplicateWorkAreaId+1;	
							}
							var rtypeForId;
							if(value.RECORD_TYPE=='MATERIAL'){
								rtypeForId='MATERIAL';
								//workOrderTypeOfWork is material work order dont decrease value of variable
								if(workOrderTypeOfWork!='MATERIAL'){
									duplicateWorkAreaId--;
								}
							}else{
								rtypeForId='LABOR'
							}
							
							//this typeOfWork variable holding work order type
							
							
						var isReadonly="false";
						var recordTypeForReadonly=value.RECORD_TYPE=='MATERIAL'?'readonly':'';
						
						if(workOrderTypeOfWork=='MATERIAL'){
							recordTypeForReadonly="";
							//we are changing rtypeForId to "LABOR" becoz we can use LABOR Validation to Materail Work Order
							//so need to change Id of the 
							rtypeForId='LABOR';
						}
						//if this condition is executed that menas this is new record, means the record not  selected while creation of work order time
						if(value.PRICE==undefined){
							if(value.BLOCK_NAME!=undefined&&value.WO_WORK_AREA_GROUP_ID!=undefined){
								var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;	
								var flatname=value.FLATNAME==null?"-":value.FLATNAME;
								var floorName=value.FLOOR_NAME==null?"-":value.FLOOR_NAME;
								floorName=floorName=="0"?"-":floorName;
								var area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0":value.QS_AREA_PRICE_PER_UNIT;
								var qs_area_amount=value.QS_AREA_AMOUNT==null?"0":value.QS_AREA_AMOUNT;
									
								str+="<tr  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Row'  class='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'>";
								if(rtypeForId=="MATERIAL"){
									str+="<td class='text-center'><input type='checkbox' style='display:none;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+");'  value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'  style='width: 100%;'></td> "
								}else{
									str+="<td  class='text-center'><input type='checkbox' style='width: 100%;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+");'  value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' ></td> "
								}
						   str+=" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BLOCK_NAME'>   "+value.BLOCK_NAME+" <input type='hidden'  id='actualWorkAreaId"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td> "+
								" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLOOR_NAME'> "+floorName+"  <input type='hidden'  id='actualAreaAlocatedQTY"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='0'></td>"+
								" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLAT_ID'>"+(flatname)+" <input type='hidden' id='workAreaIdValue"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.WO_WORK_AREA_GROUP_ID+"'>   </td>"+
								" <td  class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_AREA'>"+wo_work_area.toFixed(2)+" </td>"+
								" <td  class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WORK_AREA'>"+value.WO_WORK_AVAILABE_AREA.toFixed(2)+" </td>";
						   str+=" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WO_MEASURMEN_NAME'> "+value.WO_MEASURMEN_NAME+"</td>";
						   str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"RECORD_TYPE'>"+value.RECORD_TYPE+"<input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Contains'></td>"; //"+area_price_per_unit+"		    
						   
						   
							if(isReviseWorkOrder!="0" &&isReviseWorkOrder!=null &&isReviseWorkOrder!=""){
							    	str+="<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"PAYMENTDONE'>0.00</td>";
							 }
							    str+="<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA12'>  <input type='text' class='form-control pastenotallowed' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREA' name='"+value.WO_WORK_AREA_GROUP_ID+"' value='0'  style='width: 86px;text-align: right!important;' onkeypress='return isNumberCheck(this, event)' onkeyup='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"area\")' "+recordTypeForReadonly+">  <input type='hidden' id='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' > </td>"+
							    "<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text' class='form-control' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BOQ_RATE' value='"+area_price_per_unit+"'  style='width: 86px;text-align: right!important;' readonly> </td>";
							   if(rtypeForId=="MATERIAL"){
								   str+="<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+area_price_per_unit+"' style='width: 86px;text-align: right!important;' onkeyup='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"rate\")' onkeypress='return isNumberCheck(this, event)' "+recordTypeForReadonly+"><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WOID' value='"+value.WO_WORK_AREA_ID+"'></td>";
							   }else{
								   str+="<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE'  style='width: 86px;text-align: right!important;' onkeyup='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"rate\")' onkeypress='return isNumberCheck(this, event)' "+recordTypeForReadonly+"><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WOID' value='"+value.WO_WORK_AREA_ID+"'></td>";
							   }
							 //if this is the meterial record then do not show the plus button for new row
							    if(value.RECORD_TYPE!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
								    str+="<td style=''><button type='button' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Btn' class='btnaction' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+",2)'><i class='fa fa-plus'></i></button></td>";
								 }else{
							    	str+="<td style=''></td>"; 
							     }	
							    str+="</tr>";
							}
						}
						if(value.PRICE!=undefined&&value.PRICE!=""){
							//var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
							var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
							var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;
							var flatName=value.FLATNAME==null?"-":value.FLATNAME;
							var area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0":value.QS_AREA_PRICE_PER_UNIT;
							str+="<tr id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Row'  class='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'> "
								if(rtypeForId=="MATERIAL"){
									str+=" <td class='text-center'><input type='checkbox'  style='width: 100%;height:16px;display:none;cursor: pointer;' checked='checked' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td> "
								}else{
									str+=" <td class='text-center'><input type='checkbox'  style='width: 100%;height:16px;cursor: pointer;' checked='checked' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td> "
								}							
							str+=" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BLOCK_NAME' title='"+value.BLOCK_NAME+"' >   "+value.BLOCK_NAME+" <input type='hidden' id='workAreaIdRecordType"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.RECORD_TYPE+"'></td>  "+
								" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLOOR_NAME'> "+floorName+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID' value='"+value.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID+"'><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"OLD_QS_WO_ISSUE_DTLS_ID' value='"+(value.OLD_WO_WORK_ISSUE_AREA_DTLS_ID==null?0:value.OLD_WO_WORK_ISSUE_AREA_DTLS_ID)+"'></td>"+
								" <td class='text-center'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLAT_ID' > "+flatName+"  <input type='hidden' id='workAreaIdValue"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.WO_WORK_AREA_GROUP_ID+"'>  </td>"+
								" <td  class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_AREA'>"+wo_work_area.toFixed(2)+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WOID' value='"+value.WO_WORK_AREA_ID+"'> </td>"+
								" <td  class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WORK_AREA'>"+(value.WO_WORK_AVAILABE_AREA.toFixed(2))+" </td>";
							str+=" <td  class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WO_MEASURMEN_NAME'> "+value.WO_MEASURMEN_NAME+"<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"raPreviousBillAmount' value='"+raPreviousBillAmount+"'><input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"advPreviousBillAmount' value='"+advPreviousBillAmount+"'></td>";
							str+="<td style=''   id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"RECORD_TYPE'>"+value.RECORD_TYPE+"<input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Contains'></td>"; //"+area_price_per_unit+"
							
							if(isReviseWorkOrder!="0"&&isReviseWorkOrder!=null&&isReviseWorkOrder!=""){
								str+="<td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"PAYMENTDONE'>"+pravArea.toFixed(2)+"<input type='hidden'  id='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"raPraviousArea' value='"+raPraviousArea+"'><input type='hidden'  id='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AdvPraviousArea' value='"+AdvPraviousArea+"'></td>";
								isReadonly="readonly";
							}
								
							str+="<td> <input type='text' class='form-control pastenotallowed' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREA' value='"+value.AREA_ALOCATED_QTY+"'  name='"+value.WO_WORK_AREA_GROUP_ID+"' style='width: 86px;text-align: right!important;' onkeypress='return isNumberCheck(this, event)' onfocusout='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"area\")' "+recordTypeForReadonly+"> <input type='hidden' id='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"'></td>";
							str+="<td> <input type='text' class='form-control' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BOQ_RATE' value='"+area_price_per_unit.toFixed(2)+"' title='"+area_price_per_unit+"' style='width: 86px;text-align: right!important;' readonly> </td>";
							
							if(pravArea!=0){
									str+="<td> <input type='text' class='form-control' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+value.PRICE+"'style='width: 86px;text-align: right!important;' onfocusout='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"rate\")' onkeypress='return isNumberCheck(this, event)' readonly> </td>";
							}else{
									str+="<td> <input type='text' class='form-control pastenotallowed' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+value.PRICE+"'style='width: 86px;text-align: right!important;' onfocusout='validateWorkArea(this.value,\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\"rate\")' onkeypress='return isNumberCheck(this, event)' "+recordTypeForReadonly+"></td>";
							}
							//if this is the meterial record then do not show the plus button for new row
						    if(value.RECORD_TYPE!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
							   str+="<td style=''><button type='button' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Btn' class='btnaction' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+",1)'><i class='fa fa-plus'></i></button></td>";
							}else{
						    	str+="<td style=''></td>"; 
						    }	
							str+="</tr>";
								   
							data1+="<input type='hidden' name='BLOCK_NAME1"+row+"' value='"+value.BLOCK_NAME+"'>"
							+"<input type='hidden' name='FLOOR_NAME1"+row+"' value='"+floorName+"'>"
							+"<input type='hidden' name='FLAT_ID1"+row+"' value='"+flatName+"'>"
							+"<input type='hidden' name='actualrecordType"+row+"' value='"+value.RECORD_TYPE+"'>"
							+"<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ALLOCATE_AREA' value='"+value.AREA_ALOCATED_QTY+"'>"
							+"<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ACCEPTED_RATE'  value='"+value.PRICE+"'>"
							+"<input type='hidden' name='actualAreaAlocatedQTYPrice"+row+"'  id='actualAreaAlocatedQTYPrice"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.PRICE+"'>"
							+" <input type='hidden' name='actualAreaAlocatedQTY"+row+"' id='actualAreaAlocatedQTY"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.AREA_ALOCATED_QTY+"'>"
							+"<input type='hidden' name='actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID"+row+"' value='"+value.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID+"'>"
							+"<input type='hidden' name='actualWorkAreaId"+row+"' id='actualWorkAreaId"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.WO_WORK_AREA_ID+"'>";
						
							totalAreaQty+=+value.AREA_ALOCATED_QTY;
							if(value.RECORD_TYPE=="MATERIAL"){
								materialAmount=materialAmount+(value.AREA_ALOCATED_QTY*value.PRICE);	
							}
							
							}
							rowNum++;
							$("#appendActualWorkOrderArea"+row).append(data1);
							$("#areaMapping"+row).append(str);
							$(".pastenotallowed").bind('paste', function (e) {
								e.preventDefault();
							});
							$("#MaterialAmountId"+row).val(materialAmount);
							$("#actualMaterialAmountId"+row).val(materialAmount);
							if(workOrderTypeOfWork=='MATERIAL'){
								$("#LaborAmountId"+row).val("0");	
								$("#actualLaborAmountId"+row).val("0");	
							}
							
							var temp=value.AREA_ALOCATED_QTY==undefined?0:parseFloat(value.AREA_ALOCATED_QTY);//taking current value
							temp=parseFloat(temp.toFixed(2));
							if(rtypeForId=='MATERIAL'){
								var valueToAssign=parseFloat($("#"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"1WORK_AREA").text());//here taking previous value
								$("#"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"1WORK_AREA").text((temp+valueToAssign).toFixed(2));//here adding current value plus previous value for previous row, it will show the available area 
							}else{
								var valueToAssign=parseFloat($("#"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"1WORK_AREA").text());//here taking previous value
								$("#"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"1WORK_AREA").text((temp+valueToAssign).toFixed(2));//here adding current value plus previous value for previous row, it will show the available area
								console.log("rtypeForId "+$("#"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"1WORK_AREA").text());
							}
							});
							$("#loderId"+row).hide();
							$("#spinner"+row).hide();

							
							var tempHoldingNum=1;
							var actualArea=0;
							var seletedArea=0;
							duplicateWorkAreaId=1;
							workDescriptionid="";
							$.each($("input[name='chk1"+row+"']:checked"), function(){            
								debugger;								
								var len = $("input[name='chk1"+row+"']:checked").length;
				   				var workAreaId=$(this).val();
				   				var workAreaForDB=$("#workAreaIdValue"+workAreaId).val()==""?0:$("#workAreaIdValue"+workAreaId).val();
				   				var recordType=$(this).attr("id").split(workAreaId)[0];
				   				if(recordType=='MATERIAL'){
				   					return ;
				   				}
/* 				   				var laborSeletedArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
				                //checking here is there any duplicate work area id is there are not if there there
				                 var materialSeletedArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val());
 */				             
				                if(workAreaForDB!=workDescriptionid){
									workDescriptionid=workAreaForDB;
									duplicateWorkAreaId=1;
								}else{
									laborSeletedArea=parseFloat($("#LABOR"+workAreaForDB+(duplicateWorkAreaId)+"ALLOCATE_AREA").val());
									laborActualArea=$("#LABOR"+workAreaForDB+(duplicateWorkAreaId)+"WORK_AREA").text()==""?0:parseFloat($("#LABOR"+workAreaForDB+(duplicateWorkAreaId)+"WORK_AREA").text());
									materialSeletedArea=parseFloat($("#MATERIAL"+workAreaForDB+(duplicateWorkAreaId)+"ALLOCATE_AREA").val());
									materialActualArea=$("#MATERIAL"+workAreaForDB+(duplicateWorkAreaId)+"WORK_AREA").text()==""?0:parseFloat($("#MATERIAL"+workAreaForDB+(duplicateWorkAreaId)+"WORK_AREA").text());
									duplicateWorkAreaId=duplicateWorkAreaId+1;	
									$("#LABOR"+workAreaId+"WORK_AREA").text((laborActualArea-laborSeletedArea).toFixed(2));
									$("#MATERIAL"+workAreaId+"WORK_AREA").text((materialActualArea-materialSeletedArea).toFixed(2));
								}
						    });
							 if(lengthOfDisplayedRows==row){
				 		   		 //alert(lengthOfDisplayedRows+" "+rowNum);
				 		   		 $(".overlay_ims").hide();	
				 		   		 $(".loader-ims").hide();
				 		   	 }
							
							
/* 							var tempHoldingNum=1;
							var actualArea=0;
							var seletedArea=0;
							duplicateWorkAreaId=1;
							workDescriptionid="";
							var tempWDName=new Array();
							var num=0;
							 $.each($("input[name='chk1"+row+"']:checked"), function(){            
								debugger;
								num++;
								var len = $("input[name='chk1"+row+"']:checked").length;
				   				var workAreaId=$(this).val();				   				
				   				var workAreaForDB=$("#workAreaIdValue"+workAreaId).val()==""?0:$("#workAreaIdValue"+workAreaId).val();
				   			 	//var recordType=$("#workAreaIdRecordType"+workAreaId).val();
				   			 	var recordType=$(this).attr("id").split(workAreaId)[0];
				   	 
				   			 	 var n=1;
				   			 	 //below two variables for material purpose
				   			 	 var seletedArea1;
				   			 	 var actualArea1;
				   			 	 //checking duplicate WDGroup id with checked area
					   			 $.each($("input[name='chk1"+row+"']:checked"), function(){  
										var len = $("input[name='chk1"+row+"']:checked").length;
						   				var workAreaId1=$(this).val();	
						   				var workAreaForDB1=$("#workAreaIdValue"+workAreaId1).val()==""?0:$("#workAreaIdValue"+workAreaId1).val();
						   				var id=workAreaId1.split(workAreaForDB1)[1];
						   				if(workAreaForDB==workAreaForDB1 && recordType!="MATERIAL"){						   					
						   		 				console.log("Duplicate")
						   						seletedArea=parseFloat($("#"+recordType+workAreaForDB+(id)+"ALLOCATE_AREA").val());
												actualArea=$("#"+recordType+workAreaForDB+(id)+"WORK_AREA").text()==""?0:parseFloat($("#"+recordType+workAreaForDB+(id)+"WORK_AREA").text());
												seletedArea1=parseFloat($("#MATERIAL"+workAreaForDB+(id)+"ALLOCATE_AREA").val());
												actualArea1=$("#MATERIAL"+workAreaForDB+(id)+"WORK_AREA").text()==""?0:parseFloat($("#MATERIAL"+workAreaForDB+(id)+"WORK_AREA").text());
												duplicateWorkAreaId=duplicateWorkAreaId+1;	
												tempHoldingNum++;
												
												$("#"+recordType+workAreaForDB+(parseInt(id)+1)+"WORK_AREA").text((actualArea-seletedArea).toFixed(2));
												$("#MATERIAL"+workAreaForDB+(parseInt(id)+1)+"WORK_AREA").text((actualArea1-seletedArea1).toFixed(2));
						   			 
						   					n++;
						   				}else{
						   					console.log("No Duplicate")
						   				}
						   				
					   			 });
				   			 	console.log("rtypeForId "+$("#"+recordType+workAreaId+"WORK_AREA").text());
				   			    var seletedArea=parseFloat($("#"+recordType+workAreaId+"ALLOCATE_AREA").val());
				              
				            });  */
						
							},
								error : function(data, status, er) {
								 alert(data + "_" + status + "_" + er);
								 $(".overlay_ims").hide();	
				 		   		 $(".loader-ims").hide();
							}
					});
				}
			//LoadWorkArea method completed
			

			function addWorkArea(rowsIncre, operationtype){
				debugger;
				var materialWoAmount=0.0;
				var laborWoAmount=0.0;
				var canIcloseModelPopup=true;
				var sendDataToBackEnd=new Array();
				var totalQuantity = 0;
				var rowNum=rowsIncre;
				var rows=0;
				var totalAmount=0;
				var htmlData="";
				var revisionOfWorkOrder=$("#revision").val();
				//var areaDetailsIdPrimaryKey=100;
				var dummyAreaDetailsPrimaryKey=100;
				var isDelete=$("#isDelete"+rowsIncre).val();
				var  workOrderTypeOfWork=$("#typeOfWork").val();
				var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
				if(operationtype!="1")
				if(len==0){
						if(isDelete=="d"){
							return true;
						}
						alert("Please select atleast one work order area.");
						$('#Modal-create-wo-popup'+rowsIncre).modal('show'); 
						return false;
				}
				
				
				/* $(".allocateArea"+rowsIncre+"").each(function(){
					var assigntohidden=$(this).val();
					var currentId=$(this).attr("id").split("ALLOCATE_AREA")[0];
					$("#"+currentId+"ALLOCATE_AREAHIDDEN").val(assigntohidden);
					})
					
					$(".acceptedRate"+rowsIncre+"").each(function(){
					var assigntohidden=$(this).val();
					var currentId=$(this).attr("id").split("ACCEPTED_RATE")[0];
					$("#"+currentId+"ACCEPTED_RATEHIDDEN").val(assigntohidden);
					}) */
					var workOrderRowContains123=new Array();
			
				$.each($("input[name='chk1"+rowsIncre+"']:checked"), function(){            
					
				 
					rows++;
					var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
	   				var workAreaId=$(this).val();
	   				var recordTypeForId=$(this).attr("id").split(workAreaId)[0];
	   			 	var workAreaForDB=$("#"+recordTypeForId +workAreaId+"WOID").val();
	   			    var seletedArea=parseFloat($("#"+recordTypeForId+workAreaId+"ALLOCATE_AREA").val());
	                var actualArea1=parseFloat($("#"+recordTypeForId+workAreaId+"ACTUAL_AREA").text());
	                var actualArea=$("#"+recordTypeForId+workAreaId+"WORK_AREA").text()==""?0:parseFloat($("#"+recordTypeForId+workAreaId+"WORK_AREA").text());
	                var wo_measurmen_name=$("#"+recordTypeForId+workAreaId+"WO_MEASURMEN_NAME").text();
	                var recordType=$("#"+recordTypeForId+workAreaId+"RECORD_TYPE").text();
	                
	                var accepted_rate=parseFloat($("#"+recordTypeForId+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#"+recordTypeForId+workAreaId+"ACCEPTED_RATE").val());
	                var BOQ_RATE=parseFloat($("#"+recordTypeForId+workAreaId+"BOQ_RATE").val().trim());
	                var workOrderRowContains=$("#"+recordTypeForId+workAreaId+"Contains").val();
	                var availableArea=(actualArea-seletedArea);
	                if(recordType!="MATERIAL"&&workOrderTypeOfWork!='MATERIAL'){
	                	totalAmount+=accepted_rate*seletedArea;
	                	laborWoAmount+=accepted_rate*seletedArea;
	                }else{
	                	materialWoAmount+=accepted_rate*seletedArea;
	                }
	                //if this work order is Material Work Order then assigning material amount to total amount
	                if(workOrderTypeOfWork=='MATERIAL'){
	                	totalAmount=materialWoAmount;
	                }
	                
	                const isInArray1 = workOrderRowContains123.includes(workOrderRowContains);//
					if (isInArray1 == false) {
						debugger;
						workOrderRowContains123.push(workOrderRowContains);
					}
	                
	                debugger;
	           	    var actualAreaAlocatedQTY =	parseInt($("#actualAreaAlocatedQTY"+workAreaId).val());
	                var workIssueDetailsId=$("#"+recordTypeForId+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#"+recordTypeForId+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
	                var oldworkIssueDetailsId=$("#"+recordTypeForId+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val()==undefined?"":$("#"+recordTypeForId+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val();
	          
	                if(recordType=="MATERIAL"&&revisionOfWorkOrder!=0){
				var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:oldworkIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedArea};
				sendDataToBackEnd.push(data);
		  	}
					
	           	 	//this code for getting the paid amount for work area
	                var paymentdone= parseFloat($("#"+recordTypeForId+ workAreaId + "PAYMENTDONE").text()==""?0:$("#" +recordTypeForId+ workAreaId + "PAYMENTDONE").text());
	    			var raPraviousArea=parseFloat($("#"+recordTypeForId + workAreaId + "raPraviousArea").val()==undefined?0:$("#" +recordTypeForId+ workAreaId + "raPraviousArea").val());
					var AdvPraviousArea=parseFloat($("#" +recordTypeForId+ workAreaId + "AdvPraviousArea").val()==undefined?0:$("#" +recordTypeForId+ workAreaId + "AdvPraviousArea").val());
					var raPreviousBillAmount=parseFloat($("#" +recordTypeForId+ workAreaId + "raPreviousBillAmount").val()==undefined?0:$("#"+ recordTypeForId+workAreaId + "raPreviousBillAmount").val());;
					var advPreviousBillAmount=parseFloat($("#" +recordTypeForId+ workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#"+ recordTypeForId+workAreaId + "advPreviousBillAmount").val());;
					
	                var actualAreaAlocatedQTYPrice =$("#"+recordTypeForId + workAreaId + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#"+recordTypeForId + workAreaId + "ACTUAL_ACCEPTED_RATE").val();
	                var actualAllocatedAre=$("#"+recordTypeForId + workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#"+recordTypeForId + workAreaId + "ACTUAL_ALLOCATE_AREA").val();
					
	                if(actualAreaAlocatedQTYPrice=="NaN"||actualAreaAlocatedQTYPrice==undefined){
						actualAreaAlocatedQTYPrice=0;
						actualAllocatedAre=0;
					}else{
						actualAreaAlocatedQTYPrice=parseFloat(actualAreaAlocatedQTYPrice);
						actualAllocatedAre=parseFloat(actualAllocatedAre);
					}        
	                
	                if(actualArea<seletedArea){
	                	alert("You can't allocate more the available area.");
		               	//$("#" +recordTypeForId+ workAreaId + "ALLOCATE_AREA").focus();
		               	canIcloseModelPopup=false;
		               	return false;
	                }
	           	    
	                var validateAvailbleArea=actualAllocatedAre+actualArea;
	                if(seletedArea>validateAvailbleArea){
	                	alert("You can't allocate more the available area.");
	                	$("#"+recordTypeForId + workAreaId + "ALLOCATE_AREA").val(actualAllocatedAre);
	                    canIcloseModelPopup=false;
		   			 	$('#Modal-create-wo-popup'+rowsIncre).modal('show'); 
	                	return false;
	                }
	                
	              //checking here is the adv previous amount if not zero but trying to descrese the quantity
	            	advPreviousBillAmount=parseFloat(advPreviousBillAmount.toFixed(2));
					var tempAdvAmount= (accepted_rate*seletedArea).toFixed(2);
					if(advPreviousBillAmount!=0){
					  if(tempAdvAmount<advPreviousBillAmount){
							 alert("It's seems area is decreasing than the paid amount, Please change the area.");
							 $("#"+recordTypeForId+workAreaId+"ALLOCATE_AREA").val(actualAllocatedAre);
							 canIcloseModelPopup=false;
							 return false;
		   				  }
						 }
					
					  if(accepted_rate>BOQ_RATE){
			            	alert("You can't initiate more than BOQ rate.");
			            	$("#"+recordTypeForId+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
			            	 $("#"+recordTypeForId+workAreaId+"ACCEPTED_RATE").focus();
			         	  	 canIcloseModelPopup=false;
			            	return false;
			            }
	              
	               if(accepted_rate==0){
	               	 alert("Please enter accepted rate.");
	               	 $("#"+recordTypeForId+workAreaId+"ACCEPTED_RATE").focus();
	               	 canIcloseModelPopup=false;
	               	 return false;
	               }
	               var value=seletedArea/actualArea;
	               var percentage=value*100;
	               
	          	if (isNaN(seletedArea)) {
	   				alert("Please enter only digits.");
	   				$("#"+recordTypeForId + workAreaId + "ALLOCATE_AREA").val("0");
	   				$("#"+recordTypeForId + workAreaId + "ALLOCATE_AREA").focus();
	   				$("#" +recordTypeForId+ workAreaId + "val").prop("checked", false);
	   				canIcloseModelPopup=false;
	   				return false;
	   			}
	            if(seletedArea<=0){
	               	alert("Plese enter correct value for area");
	               	$("#" + workAreaId + "ALLOCATE_AREA").focus();
	               	canIcloseModelPopup=false;
	               	return false;
	             }
	            if(recordTypeForId=="MATERIAL"){
	            	seletedArea=parseFloat(seletedArea).toFixed(3);
	            }
	           	   var workAreaIDForDB=$("#"+recordTypeForId +workAreaId+"WOID").val();
	           	   htmlData+="<input type='hidden' name='lengthOfSelectedArea"+rowsIncre+"' id='lengthOfSelectedArea"+rowsIncre+"' value='"+len+"'>";
	         	   htmlData+="<input type='hidden' name='percentage"+rowsIncre+"' value='"+percentage+"'>";
	               htmlData+="<input type='hidden' name='selectedArea"+rowsIncre+"' id='selectedAreaT"+rowsIncre+rows+"' value='"+seletedArea+"'>";
	               htmlData+="<input type='hidden' name='availableArea"+rowsIncre+"' value='"+actualArea+"'>";
	               htmlData+="<input type='hidden' name='workAreaId"+rowsIncre+"' value='"+workAreaIDForDB+"'>";
	               htmlData+="<input type='hidden' name='available"+rowsIncre+"' value='"+availableArea+"'>";
	               htmlData+="<input type='hidden' name='wo_measurmen_name"+rowsIncre+"'  id='wo_measurmen_nameT"+rowsIncre+rows+"'  value='"+wo_measurmen_name+"'>";
	               htmlData+="<input type='hidden' name='record_type"+rowsIncre+"'  id='recordTypeT"+rowsIncre+rows+"'  value='"+recordType+"'>";
	               htmlData+="<input type='hidden' name='boqRate"+rowsIncre+"' id='boqRateT"+rowsIncre+rows+"' value='"+BOQ_RATE+"'>";
	               
	               
	               htmlData+="<input type='hidden' name='accepted_rate"+rowsIncre+"' id='accepted_rateT"+rowsIncre+rows+"' value='"+accepted_rate+"'>";
	            if(workIssueDetailsId.length!=0){
	               htmlData+="<input type='hidden' name='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+"' id='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+rows+"' value='"+workIssueDetailsId+"'>";
	               htmlData+="<input type='hidden' name='OLD_QS_WO_ISSUE_DTLS_ID"+rowsIncre+"' id='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+rows+"' value='"+oldworkIssueDetailsId+"'>";
	             }else{
	            	 htmlData+="<input type='hidden' name='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+"' id='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+rows+"' value='"+dummyAreaDetailsPrimaryKey+"A'>";
	            	 dummyAreaDetailsPrimaryKey++;
	             }
	               
	               var block_name=$("#"+recordTypeForId+workAreaId+"BLOCK_NAME").text().trim();
	               var floor_name=$("#"+recordTypeForId+workAreaId+"FLOOR_NAME").text().trim();
	               var flat_id=$("#"+recordTypeForId+workAreaId+"FLAT_ID").text().trim();
	              // if(block_name!=""&&floor_name!=""){
	            		htmlData+="<input type='hidden' name='BLOCK_NAME"+rowsIncre+"' readonly='true'  id='BLOCK_NAMET"+rowsIncre+rows+"' value='"+block_name+"'>";
		                htmlData+="<input type='hidden' name='FLOOR_NAME"+rowsIncre+"' readonly='true'  id='FLOOR_NAMET"+rowsIncre+rows+"'  value='"+floor_name+"'>";
		                htmlData+="<input type='hidden' name='FLAT_ID"+rowsIncre+"' id='FLAT_IDT"+rowsIncre+rows+"' readonly='true' value='"+flat_id+"'>";
	           		
	            	//$("#appendWorkOrderArea"+rowsIncre).append(htmlData);
	               //}
		            if(recordType!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
		            	totalQuantity += seletedArea;
	           	}
	            rowNum++;
	 		});
				if(canIcloseModelPopup==false){
					return false;
				}
				
				$("#appendWorkOrderArea"+rowsIncre).html(htmlData);
			    $("#WoRecordContains"+rowsIncre).val(workOrderRowContains123.toString());
				var id12=$("#appendData"+rowsIncre).val();
	     		
				if(operationtype=="validateLabor"&&revisionOfWorkOrder!=0){
					  canIcloseModelPopup= ajaxCallForValidatingWO_Material(rowsIncre,sendDataToBackEnd,'')
						if(canIcloseModelPopup==false){
							return false;
						} 
				}
				
			    materialWoAmount=materialWoAmount.toFixed(2);
			    laborWoAmount=laborWoAmount.toFixed(2);
			  
			    $("#LaborAmountId"+rowsIncre).val(laborWoAmount);
			    $("#MaterialAmountId"+rowsIncre).val(materialWoAmount);
				
	       		$("#TotalAmountId"+rowsIncre).val(totalAmount.toFixed(2));
	         	$("#Quantity"+rowsIncre).val(totalQuantity.toFixed(2)); 
	         	$("#QuantityId"+rowsIncre).val(totalQuantity.toFixed(2));
	         	
	         	sumOfWoAMounts(rowsIncre) ;
	         	materialWoAmount=0;
	 		    laborWoAmount=0;
	 		    var isDelete="";
	 			$(".workorderrowcls").each(function(){
	 				var currentId=$(this).attr("id").split("tr-class")[1];
	 				isDelete=$("#isDelete"+currentId).val();
	 				if(isDelete!="d"){
		 				laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val()==""?0:$("#LaborAmountId"+currentId).val());
		 				materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val()==""?0:$("#MaterialAmountId"+currentId).val());	
	 				}
	 			});
	 			materialWoAmount=materialWoAmount.toFixed(2);
	 			laborWoAmount=laborWoAmount.toFixed(2);
	 			
	 			$("#materialWoAmount").val(materialWoAmount);
	 			$("#laborWoAmount").val(laborWoAmount);
	 			$("#sumofTotalAmount").val(parseFloat(laborWoAmount)+parseFloat(materialWoAmount));
	 			
	 			$("#materialWoAmountSpan").text(materialWoAmount);
				$("#laborWoAmountSpan").text(laborWoAmount);
				$("#showQty"+rowsIncre).show();
	         	$("#showQty"+rowsIncre).css("display","inherit");
	         	$("#showMaterialQty"+rowsIncre).show();
				$("#table-quantity").hide();
				$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
				
				//loading actual and current amount of work row(WD) If they are diffrent hide modify button
				var actualTotalAmount=parseFloat($("#actualTotalAmount"+rowsIncre).val());
				var TotalAmountId=parseFloat($("#TotalAmountId"+rowsIncre).val());
				if(actualTotalAmount!=TotalAmountId){
					$("#modifyBtnId").hide();
					$("#rejectBtnId").hide();
				}
			}//
// 		addWorkArea method completed	
			
		function sumOfWoAMounts(rowsIncre) {
			var sumofTotalAmount=0;
             var materialWoAmount=0;
		    var laborWoAmount=0; 
    	   	$(".workorderrowcls").each(function(){
    			var currentId=$(this).attr("id").split("tr-class")[1];	
    			if($("#isDelete"+currentId).val()!="d"){
    				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
    				laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val()==""?0:$("#LaborAmountId"+currentId).val());
    				materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val()==""?0:$("#MaterialAmountId"+currentId).val());
    			}
    		});
    	   	materialWoAmount=materialWoAmount.toFixed(2);
			  laborWoAmount=laborWoAmount.toFixed(2);
			  $("#materialWoAmount").val(materialWoAmount);
			  $("#laborWoAmount").val(laborWoAmount);
			  $("#materialWoAmountSpan").text(materialWoAmount);
			  $("#laborWoAmountSpan").text(laborWoAmount);
			  sumofTotalAmount=parseFloat(laborWoAmount)+parseFloat(materialWoAmount);
    	   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
    	}	
			
		 function ajaxCallForValidatingWO_Material(rowsIncre,sendDataToBackEnd,isMaterialIssued){
				debugger;
			var canIcloseModelPopup=true;
			var contractorId=$("#contractorId").val();
			var workOrderNumber=$("#oldWorkOrderNo").val();
			var siteId=$("#siteId").val();
			var workAreaGroupId="";
			var workAreaId="";
			var url = "validateWOMaterialBOQDetails.spring";
			 $.ajax({
				url : url,
				type : "get",
				async: false,
				data:{
					data:JSON.stringify(sendDataToBackEnd),
					contractorId:contractorId,
					workOrderNumber:workOrderNumber,
					siteId:siteId 
					},
				success : function(data) {
					$.each(data,function(key, value) {
						debugger;
						
						//alert(key+" "+value);
						var temp=value.split("@@")[1];
						workAreaId=temp.split("##")[0];
						workAreaGroupId=temp.split("##")[1];
						
						if(isMaterialIssued.length!=0){
							if(key=="MaterialIssued"){
								alert('You can not remove this work area, already material issued.');
								$("#LABOR" + workAreaGroupId + "val").prop("checked", true);
				 				$("#MATERIAL" + workAreaGroupId + "val").prop("checked", true);
				 				canIcloseModelPopup=false;
				 				setTimeout(function(){
									$("#LABOR"+workAreaGroupId+"ALLOCATE_AREA").focus();
								}, 200);
				 				canIcloseModelPopup=false;
							}
						}else{
							if(key!="MaterialIssued"){
								var tempGroupId=key.split("@@")[1];
								
								if(key.split("@@")[0]=="NewPerUnitLessThanOldPerUnit"){
									alert('Can not revise work order. already material issued, per unit quantity was changed.');
								}else if(key.split("@@")[0]=="IssuedMaterialQuanity"){
									
								alert('You can not reduce material qty, already material issued for '+value.split("@@")[1].split("##")[2]+" quantity");
								$("#LABOR" + tempGroupId + "val").prop("checked", true);
				 				$("#MATERIAL" + tempGroupId + "val").prop("checked", true);
								
				 				var actualAllocatedAre=$("#LABOR" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#LABOR" + tempGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
				 				actualAllocatedAre=$("#MATERIAL" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#MATERIAL" + tempGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
				 				setTimeout(function(){
									$("#LABOR"+tempGroupId+"ALLOCATE_AREA").focus();
								}, 200);
				 				canIcloseModelPopup=false;
							}
								$('#Modal-create-wo-popup'+rowsIncre).modal('show');
							
							}
						}
					});
				}
			}); 
			 return canIcloseModelPopup;
      	}
 
		 function myFunction(workOrderID,acceptedRate,row,isShow) {
			debugger;
			$("#spinner"+row).show();
			var appendId=$("#allocatedareaAppend").val();
			//make the div data empty
					    if (typeof(Storage) !== "undefined") {
					         sessionStorage.setItem("${UserId}tempRowsIncre",row);
				        } else {
				           alert("Sorry, your browser does not support Web Storage...");
				        };
				$("#areaMapping").html("");
  
				var WO_Desc=$("#WO_Desc"+row).val();
				if(WO_Desc==""){
					alert("Please select work description.");
					return false;
				}
				var UnitsOfMeasurementId=$("#UnitsOfMeasurementId"+row).val();
				if(UnitsOfMeasurementId==null||UnitsOfMeasurementId==""){
					alert("Please select UOM.");
					return false;
				}
				
				$('#Modal-create-wo-popup'+row).modal('show'); 
				$("#spinner"+row).hide();
				$("#loderId").hide();
			
			 var url = "loadWOAreaMapping.spring";
		}
		
		//checking duplicate row in table
		function checkingDuplicateRow(rowNum){
			var error="true";
			var currentWO_MajorHead=$("#WO_MajorHead"+rowNum).val().split("$")[0];
			var currentWO_MinorHead=$("#WO_MinorHead"+rowNum).val().split("$")[0];
			var currentWO_Desc=$("#WO_Desc"+rowNum).val().split("$")[0];
			var currentUOM=$("#UnitsOfMeasurementId"+rowNum).val().split("$")[0];
			$(".workorderrowcls").each(function(){
				var currentId=$(this).attr("id").split("tr-class")[1];
				if(currentId!=rowNum){
					var WO_MajorHead=$("#WO_MajorHead"+currentId).val().split("$")[0];
					var WO_MinorHead=$("#WO_MinorHead"+currentId).val().split("$")[0];
					var WO_Desc=$("#WO_Desc"+currentId).val().split("$")[0];
					var UOM=$("#UnitsOfMeasurementId"+currentId).val()==undefined?$("#actualunitsOfMeasurement"+currentId).val().split("$")[0]:$("#UnitsOfMeasurementId"+currentId).val().split("$")[0];;			
					if(currentWO_MajorHead==WO_MajorHead && WO_MinorHead==currentWO_MinorHead && currentWO_Desc==WO_Desc && currentUOM==UOM){
						alert("UOM already exist, Please choose different UOM.");
						$("#UnitsOfMeasurementId"+rowNum).val("");
						return error="false";
					}
				}
			})

			return error;
			
		}
		
		function loadWOWorkAreaNewRow(UOMId, rowNum) {
			debugger;
			 var checkduplicate=checkingDuplicateRow(rowNum);
		     if(checkduplicate=="false"){  
		    	 $("#UOMId"+rowNum).val('');
		    	 return false;
		     } 
		     
		     var typeOfWork="${WorkOrderBean.typeOfWork}";
			//don't execute next line if this is NMR work order
		     if(typeOfWork=="NMR"){
		    	 return false;
		     }
		     
			var popupmodaldyn="";
		    debugger;
		 
			 $('#Modal-create-wo-popup').modal('hide'); 
		     $("#TotalAmountId"+rowNum).val('0');
				var appendId=$("#allocatedareaAppend").val();
				var mesurmentId="";
				var UOMId="";
		 		$("#QuantityId"+rowNum).val("");
		 		 var isReviseWorkOrder=$("#revision").val();
				 mesurmentId= $("#WO_Desc"+rowNum).val();
				 UOMId=$("#UnitsOfMeasurementId"+rowNum).val();
				 if(mesurmentId==undefined||mesurmentId==""||mesurmentId==null){
					alert("Please select work descripition.");
					$("#WO_Desc"+rowNum).focus();
					 return false;
				 }
				 if(UOMId==undefined||UOMId==""||UOMId==null){
						alert("Please select UOM.");
						$("#UOMId"+rowNum).focus();
						return false;
				 }
				 var workDescId=$("#comboboxsubSubProd"+rowNum).val().split("$")[0];
				 var  UnitsOfMeasurementId=$("#UnitsOfMeasurementId"+rowNum).val().split("$")[0];
				 var closeString="closebtn";
				 var submitString="submitbtn";
				 
//			<!-- modal popup for create work order table for show start -->
			//<!-- Modal -->
			popupmodaldyn+=' <div id="Modal-create-wo-popup'+rowNum+'" class="modal fade" role="dialog">';
				popupmodaldyn+=' <div class="modal-dialog modal-lg" style="width:95%;">';
			    popupmodaldyn+=' <div class="modal-content">';
			    popupmodaldyn+=' <div class="modal-header">';
			    popupmodaldyn+='   <button type="button" class="close"  onclick="closeWorkAreaPopup('+rowNum+')" >&times;</button>';
				popupmodaldyn+=' <h4 class="modal-title text-center">Work Area </h4>';
				popupmodaldyn+='  </div>';
			   	popupmodaldyn+=' <div class="modal-body">';
			    popupmodaldyn+=' <div class="table-responsive">';
				popupmodaldyn+=' <table class="table" id="floortable" style="">';
			 	popupmodaldyn+=' <thead>';
				popupmodaldyn+=' <tr>';
			    popupmodaldyn+=' <th style="" class="text-center"><input type="checkbox" id="checkAll'+rowNum+'" style="width: 100%;height:16px;cursor: pointer;" value="'+rowNum+'" onclick="checkAllCheckBox('+rowNum+')"></th>';
			    popupmodaldyn+=' <th style="" >Block</th>';
			    popupmodaldyn+='<th style="">Floor</th>';
			    popupmodaldyn+=' <th style="" >Flat</th>';
			    popupmodaldyn+='   <th style="" >Actual Area</th>';
			    popupmodaldyn+='   <th style="" >Available Area</th>';
		//	    popupmodaldyn+='   <th style="" >Initiated Area</th>';
			    popupmodaldyn+=' <th style="" >Measurement</th>';
			    popupmodaldyn+=' <th style="" >Record Type</th>';
			    if(isReviseWorkOrder!="0" &&isReviseWorkOrder!=null &&isReviseWorkOrder!="")
				    popupmodaldyn+=' <th  >Payment Initiated Area</th>';
			    popupmodaldyn+=' <th style="" >Allocate Area</th>';
			    popupmodaldyn+='<th style="">BOQ Rate(<%=acceptedRateCurrency %>)</th>';
			    popupmodaldyn+='<th style="">Accepted Rate(<%=acceptedRateCurrency %>)</th>';

			    
			    popupmodaldyn+='<th style=";">Actions</th>';
			    popupmodaldyn+='</thead>';
			    popupmodaldyn+='<tbody id="areaMapping'+rowNum+'" class="fixed-table-body">';
			    popupmodaldyn+='<div class="loader" id="loderId'+rowNum+'" style="display:none;"></div>';
		    	popupmodaldyn+='</tbody>';
			    popupmodaldyn+='</table>';
			    popupmodaldyn+='</div> ';
		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='<div class="modal-footer">';
		    	popupmodaldyn+='<div class="text-center center-block" id="submitButtonWorkArea">';
		    	popupmodaldyn+='<button type="button" id="hide-table-2" class="btn btn-warning" onclick="return addWorkArea('+rowNum+',2)">Submit</button>';/*  */
		   		popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';

		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';
		     	//<!-- modal popup for create work order table for show end -->
			  $("#appendWorkOrderWorkArea"+rowNum).html(popupmodaldyn);
			  var siteId=$("#siteId").val();
			  $("#loderId"+rowNum).show();
			  var workOrderTypeOfWork=$("#typeOfWork").val();
				debugger;
				var url = "loadWOAreaMapping.spring";
				$.ajax({
					url : url,
					type : "get",
					data:{mesurmentId:workDescId,UnitsOfMeasurementId:UnitsOfMeasurementId,
						isApproveWOPage:true,siteId:siteId,typeOfWork:workOrderTypeOfWork
						
					},
					success : function(data) {
				//		data.sort(sortDataBlockFloorWise);
						str="";
						var tbleRowNum=1;
						var id=1;
					$.each(data,function(key,value){
							debugger;
							
							var initiated_area=value.WO_WORK_INITIATE_AREA==null?"0":value.WO_WORK_INITIATE_AREA;
							var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;
							var flatname=value.FLATNAME==null?"-":value.FLATNAME;
								
							//var QS_BOQ_DETAILS_ID	QWAM.QS_BOQ_DETAILS_ID
							var area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0":value.QS_AREA_PRICE_PER_UNIT;
							var qs_area_amount=value.QS_AREA_AMOUNT==null?"0":value.QS_AREA_AMOUNT;
							var isValueZero=value.WO_WORK_AVAILABE_AREA;
							var recordType=value.RECORD_TYPE=='MATERIAL'?'readonly':'';//by using this variable we can make text box readonly
							var rtypeForId;
							if(value.RECORD_TYPE=='MATERIAL'){
								rtypeForId='MATERIAL';
								id--;
							}else{
								rtypeForId='LABOR'
							}
							
							//this typeOfWork variable holding work order type
							if(workOrderTypeOfWork=='MATERIAL'){
								recordType="";
								//we are changing rtypeForId to "LABOR" becoz we can use LABOR Validation to Materail Work Order
								//so need to change Id of the 
								rtypeForId='LABOR';
							}
							
							
						   str+="<tr id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Row'> "
						   		if(value.RECORD_TYPE=='LABOR' || workOrderTypeOfWork=='MATERIAL'){
						   			str+="<td style='' class='text-center'><input type='checkbox' style='width: 100%;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value,0,"+rowNum+")' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td> "
						   		}else{
						   			str+="<td style='' class='text-center'><input type='checkbox' style='width: 100%;height:16px;cursor: pointer;display:none;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1val' name='chk1"+rowNum+"'  value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td> "
						   		}			     		
						   str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BLOCK_NAME'>   "+value.BLOCK_NAME+"  <input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> "+
								"<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1FLOOR_NAME'> "+(value.FLOOR_NAME==null?"-":value.FLOOR_NAME)+"</td>"+
							    "<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1FLAT_ID' >"+(flatname)+"<input type='hidden' id='workAreaIdValue"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1' value='"+value.WO_WORK_AREA_GROUP_ID+"'>   </td>"+
							    "<td style='' class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACTUAL_AREA' >"+wo_work_area+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1UOMId' name='UOMId' value='"+value.WO_MEASURMENT_ID+"'><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WOID' value='"+value.WO_WORK_AREA_ID+"'></td>"+
							    "<td style='' class='text-right' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WORK_AREA' > "+(value.WO_WORK_AVAILABE_AREA==null?"-":value.WO_WORK_AVAILABE_AREA)+"<input type='hidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BOQ_NO' value='"+value.BOQ_NO+"'></td>";
						 //  str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1INITIATED_AREA' >"+initiated_area+"   </td>";
						   str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WO_MEASURMEN_NAME'>"+(value.WO_MEASURMEN_NAME==null?"-":value.WO_MEASURMEN_NAME.toUpperCase())+"</td>";
						   str+="<td style='' class='text-left' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1RECORD_TYPE'>"+value.RECORD_TYPE+"<input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Contains'></td>"; //"+area_price_per_unit+"value.WO_CONTAINS
						   if(isReviseWorkOrder!="0" &&isReviseWorkOrder!=null &&isReviseWorkOrder!=""){
						    	str+="<td id='"+rtypeForId+value.WO_WORK_AREA_ID+rowNum+id+"1PAYMENTDONE'>0.00</td>";
						 	}
							    if(isValueZero==0 ){
							    	str+="<td style='' class='text-center'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1AVAILABE_AREA12'> <input type='text' class='form-control pastenotallowed'style='width: 100%;    text-align: right !important;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ALLOCATE_AREA' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"\")' readonly></td>";// "+(recordType)+"
							    }else{
							    	str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1AVAILABE_AREA12'> <input type='text' class='form-control pastenotallowed allocateArea"+rowNum+" pastenotallowed "+value.WO_WORK_AREA_GROUP_ID+"' style='width: 100%;    text-align: right !important;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ALLOCATE_AREA'  name='"+value.WO_WORK_AREA_GROUP_ID+"'  value='0' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\")' onkeypress='return isNumberCheck(this, event)'   "+recordType+"> <input type='hidden' class='allocateAreaHidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ALLOCATE_AREAHIDDEN'></td>"; //"+value.WO_WORK_AVAILABE_AREA+"
							    }
							    str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' style='width: 100%;    text-align: right !important;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BOQ_RATE' value='"+area_price_per_unit+"' title='"+area_price_per_unit+"' readonly='readonly'  style=' width: 66px;'> </td>";
							   
							    if(isValueZero==0||value.RECORD_TYPE=="MATERIAL"&&workOrderTypeOfWork!='MATERIAL'){//this condition is for accepted rate readonly
							    	str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' style='width: 100%;    text-align: right !important;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACCEPTED_RATE' value='"+area_price_per_unit+"' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\")' readonly> <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1' id='hiddenid"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td>";
							    }else{
							    	str+="<td style='' class='text-center' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed acceptedRate"+rowNum+" pastenotallowed' style='width: 100%;    text-align: right !important;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACCEPTED_RATE' value='0' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\")' onkeypress='return isNumberCheck(this, event)'><input type='hidden' class='acceptedRateHidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"' id='hiddenid"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td>"; //"+area_price_per_unit+"
							    }
							   
							    //if this is the meterial record then do not show the plus button for new row
							    if(value.RECORD_TYPE!="MATERIAL" ||workOrderTypeOfWork=='MATERIAL'){
								    if(isValueZero!=0){
								    	str+="<td style=''><button type='button' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Btn' class='btnaction' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\","+rowNum+",1)'><i class='fa fa-plus'></i></button></td>";
								    }else{
								    	str+="<td style=''></td>"; //<button type='button' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Btn' class='btnaction' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\","+rowNum+",1)'><i class='fa fa-plus'></i></button>
								    }
							    }else{
							    	str+="<td style=''></td>"; 
							    }
							    str+="</tr>";
						id++;
						});			
					    $("#loderId"+rowNum).hide();//hiding the loader
						$("#areaMapping"+rowNum).html(str);//appending all the data to the pop up box
						$(".pastenotallowed").bind('paste', function (e) {//here preventing to enter alphabets
							e.preventDefault();
						});
					},
					error : function(data, status, er) {
						alert(data + "_" + status + "_" + er);
					}
				});
			}
		
		
		//adding duplicate row
		function addDuplicateWorkAreaRow(workAreaId,rowNum,id){
		debugger;
		var revisionOfWO=$("#revision").val();
			var workAreaForDB=$("#workAreaIdValue"+workAreaId).val()==""?0:$("#workAreaIdValue"+workAreaId).val();
			var LABORseletedArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
			var LABORactualArea=parseFloat($("#LABOR"+workAreaId+"ACTUAL_AREA").text()==""?0:$("#LABOR"+workAreaId+"ACTUAL_AREA").text());
            var LABORavailableArea=parseFloat($("#LABOR"+workAreaId+"WORK_AREA").text().trim());
        	var LABORinitiated_area=parseFloat($("#LABOR"+workAreaId+"INITIATED_AREA").text()==""?0:$("#LABOR"+workAreaId+"INITIATED_AREA").text());
            var LABORwo_measurmen_name=$("#LABOR"+workAreaId+"WO_MEASURMEN_NAME").text();
            var LABORrecord_type=$("#LABOR"+workAreaId+"RECORD_TYPE").text();            
            var LABORarea_price_per_unit =parseFloat($("#LABOR"+workAreaId+"BOQ_RATE").val());
            var LABORaccepted_rate=parseFloat($("#LABOR"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+workAreaId+"ACCEPTED_RATE").val());
            var boq_no=$("#"+workAreaId+"BOQ_NO").val();
         	var LABORblock_name=$("#LABOR"+workAreaId+"BLOCK_NAME").text().trim();
            var LABORfloor_name=$("#LABOR"+workAreaId+"FLOOR_NAME").text().trim();
            var LABORflat_id=$("#LABOR"+workAreaId+"FLAT_ID").text().trim();
            var LABORWOID=$("#LABOR"+workAreaId+"WOID").val();
            var Contains=$("#LABOR"+workAreaId+"Contains").val();
            var ContainsSpecailChar = Contains.includes("@@");
            if(ContainsSpecailChar==true){            	
	            var MATERIALseletedArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val());
	            var MATERIALactualArea=parseFloat($("#MATERIAL"+workAreaId+"ACTUAL_AREA").text()==""?0:$("#MATERIAL"+workAreaId+"ACTUAL_AREA").text());
	            var MATERIALavailableArea=parseFloat($("#MATERIAL"+workAreaId+"WORK_AREA").text().trim());
	        	var MATERIALinitiated_area=parseFloat($("#MATERIAL"+workAreaId+"INITIATED_AREA").text()==""?0:$("#MATERIAL"+workAreaId+"INITIATED_AREA").text());
	            var MATERIALwo_measurmen_name=$("#MATERIAL"+workAreaId+"WO_MEASURMEN_NAME").text();
	            var MATERIALrecord_type=$("#MATERIAL"+workAreaId+"RECORD_TYPE").text();            
	            var MATERIALarea_price_per_unit =parseFloat($("#MATERIAL"+workAreaId+"BOQ_RATE").val());
	            var MATERIALaccepted_rate=parseFloat($("#MATERIAL"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#MATERIAL"+workAreaId+"ACCEPTED_RATE").val());
	            var MATERIALblock_name=$("#MATERIAL"+workAreaId+"BLOCK_NAME").text().trim();
	            var MATERIALfloor_name=$("#MATERIAL"+workAreaId+"FLOOR_NAME").text().trim();
	            var MATERIALflat_id=$("#MATERIAL"+workAreaId+"FLAT_ID").text().trim();
	            var MATERIALWOID=$("#MATERIAL"+workAreaId+"WOID").val();
            }
          
            if(LABORseletedArea<=0||LABORseletedArea==0){
            	alert("Please enter correct value for area");
            	return false;
            }
            LABORinitiated_area=LABORinitiated_area+LABORseletedArea;
            LABORavailableArea=LABORavailableArea-LABORseletedArea;
            
            if(ContainsSpecailChar==true){  
	            MATERIALinitiated_area=MATERIALinitiated_area+MATERIALseletedArea;
	            MATERIALavailableArea=MATERIALavailableArea-MATERIALseletedArea;
            }
            
            if(LABORavailableArea<=0){
            	alert("You can't create new row.");
            	return false;
            }
            
            if(LABORaccepted_rate=="0"){
            	alert("Please enter accpeted rate");
            	return false;
            }
            
            
            var forBtnId=id;
            id=id+1;
		 var str="<tr  id='LABOR"+workAreaId+id+"Row'>";
		 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;cursor:pointer;' class='workareatr' id='LABOR"+workAreaId+id+"val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value,0,"+rowNum+")' value='"+workAreaId+id+"'></td> ";
		 str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"BLOCK_NAME'>"+LABORblock_name+"  <input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> ";
		 str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"FLOOR_NAME'> "+(LABORfloor_name==null?"-":LABORfloor_name)+"</td>";
		 str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"FLAT_ID' >"+(LABORflat_id)+"<input type='hidden' id='workAreaIdValue"+workAreaId+id+"' value='"+workAreaForDB+"'>   </td>";
		 str+="<td style='' class='text-right' id='LABOR"+workAreaId+id+"ACTUAL_AREA' > "+LABORactualArea.toFixed(2)+" </td>";
		 str+="<td style='' class='text-right' id='LABOR"+workAreaId+id+"WORK_AREA' > "+(LABORavailableArea.toFixed(2))+"<input type='hidden' id='LABOR"+workAreaId+id+"WOID' value='"+LABORWOID+"'> </td>";
		// str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"INITIATED_AREA' >"+LABORinitiated_area.toFixed(2)+"</td>";
		 str+="<td  style='' class='text-center' id='LABOR"+workAreaId+id+"WO_MEASURMEN_NAME'>"+(LABORwo_measurmen_name)+"</td>";
		 str+="<td  style=''class=''  id='LABOR"+workAreaId+id+"RECORD_TYPE'>"+(LABORrecord_type)+"<input type='hidden' value='"+Contains+"' id='LABOR"+workAreaId+id+"Contains'></td>";
		 if(revisionOfWO!="0"){
			 str+="<td  id='LABOR"+workAreaId+id+"PAYMENTDONE'>0.00<input type='hidden'  id='"+workAreaId+id+"raPraviousArea' value='0'><input type='hidden'  id='"+workAreaId+id+"AdvPraviousArea' value='0'></td>";
		 } 
		 str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"AVAILABE_AREA12'> <input type='text' class='form-control pastenotallowed allocateArea"+rowNum+"' style='width: 100%;text-align: right !important;' id='LABOR"+workAreaId+id+"ALLOCATE_AREA' name='"+workAreaForDB+"' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)'> <input type='hidden' class='allocateAreaHidden' id='"+workAreaId+id+"ALLOCATE_AREAHIDDEN'></td>";
		 str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' style='width: 100%;text-align: right !important;' id='LABOR"+workAreaId+id+"BOQ_RATE' value='"+LABORarea_price_per_unit.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
	     str+="<td style='' class='text-center' id='LABOR"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed acceptedRate"+rowNum+"' style='width: 100%;text-align: right !important;' id='LABOR"+workAreaId+id+"ACCEPTED_RATE' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)'><input type='hidden' class='acceptedRateHidden' id='"+workAreaId+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaId+id+"' id='hiddenid"+workAreaId+id+"'></td>";
		 str+="<td style=''><button type='button'  class='btnaction' id='LABOR"+workAreaId+id+"Btn' onclick='addDuplicateWorkAreaRow(\""+workAreaId+id+"\","+rowNum+","+(id)+")'><i class='fa fa-plus'></i></button></td>";
		 str+="</tr>";
		 
		 if(ContainsSpecailChar==true){  
			 str+="<tr  id='MATERIAL"+workAreaId+id+"Row'>";
			 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;display:none;' class='workareatr' id='MATERIAL"+workAreaId+id+"val' name='chk1"+rowNum+"' value='"+workAreaId+id+"'></td> ";
			 str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"BLOCK_NAME'>"+MATERIALblock_name+"  <input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> ";
			 str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"FLOOR_NAME'> "+(MATERIALfloor_name==null?"-":MATERIALfloor_name)+"</td>";
			 str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"FLAT_ID' >"+(MATERIALflat_id)+"<input type='hidden' id='workAreaIdValue"+workAreaId+id+"' value='"+workAreaForDB+"'>   </td>";
			 str+="<td style='' class='text-right' id='MATERIAL"+workAreaId+id+"ACTUAL_AREA' > "+MATERIALactualArea.toFixed(2)+" </td>";
			 str+= "<td style='' class='text-right' id='MATERIAL"+workAreaId+id+"WORK_AREA' > "+(MATERIALavailableArea.toFixed(2))+"<input type='hidden' id='MATERIAL"+workAreaId+id+"WOID' value='"+MATERIALWOID+"'> </td>";
		//	 str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"INITIATED_AREA' >"+MATERIALinitiated_area.toFixed(2)+"</td>";
			 str+="<td  style='' class='text-center' id='MATERIAL"+workAreaId+id+"WO_MEASURMEN_NAME'>"+(MATERIALwo_measurmen_name)+"</td>";
			 str+="<td  style='' class='' id='MATERIAL"+workAreaId+id+"RECORD_TYPE'>"+(MATERIALrecord_type)+"</td>";
			 if(revisionOfWO!="0"){
				 str+="<td  id='MATERIAL"+workAreaId+id+"PAYMENTDONE'>0.00<input type='hidden'  id='"+workAreaId+id+"raPraviousArea' value='0'><input type='hidden'  id='"+workAreaId+id+"AdvPraviousArea' value='0'></td>";
			 }
			 str+="<td style='' class='text-center'  id='MATERIAL"+workAreaId+id+"AVAILABE_AREA12'> <input type='text' class='form-control allocateArea"+rowNum+"' style='width: 100%;text-align: right !important;' id='MATERIAL"+workAreaId+id+"ALLOCATE_AREA' name='"+workAreaForDB+"' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)' readonly='readonly'> <input type='hidden' class='allocateAreaHidden' id='"+workAreaId+id+"ALLOCATE_AREAHIDDEN'></td>";
			 str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control' style='width: 100%;text-align: right !important;' id='MATERIAL"+workAreaId+id+"BOQ_RATE' value='"+MATERIALarea_price_per_unit.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
		     str+="<td style='' class='text-center' id='MATERIAL"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control acceptedRate"+rowNum+"' style='width: 100%;text-align: right !important;' id='MATERIAL"+workAreaId+id+"ACCEPTED_RATE'  value='"+MATERIALarea_price_per_unit+"' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)' readonly='readonly'><input type='hidden' class='acceptedRateHidden' id='"+workAreaId+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaId+id+"' id='hiddenid"+workAreaId+id+"'></td>";
			 str+="<td style=''></td>";
			 str+="</tr>";
			 
			 $("#MATERIAL"+workAreaId+"Row").after(str);
		 }else{
			 $("#LABOR"+workAreaId+"Row").after(str);
		 }
			//$("#MATERIAL"+workAreaForDB+rowNum+id+rowNum+"Row").after(str);
			$("#LABOR"+workAreaId+"Btn").hide();
			$(".pastenotallowed").bind('paste', function (e) {//here preventing to enter alphabets
				e.preventDefault();
			});
		}
		
		function validateBBQ(id){
			debugger;
			
			var hiddenId=$("#hiddenid"+id).val();	
			var BOQ_RATEID="LABOR"+id+"BOQ_RATE";
			var ACCEPTED_RATEID="LABOR"+id+"ACCEPTED_RATE";
			var BOQ_RATEVal=parseFloat($("#"+BOQ_RATEID).val());
			var ACCEPTED_RATEVal=parseFloat($("#"+ACCEPTED_RATEID).val());
			var workAreaId=$("#workAreaIdValue"+id).val();
			
			var AllocateArea=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
			var AvailableArea=parseFloat($("#LABOR"+id+"WORK_AREA").text()==''?0:$("#LABOR"+id+"WORK_AREA").text());	
			var wdId=hiddenId.split("A");
			var currentSplitId=id.split(workAreaId)[1];
			
			 if(AllocateArea==0){
					$("#LABOR" + id + "val").prop("checked", false);
					$("#MATERIAL" + id + "val").prop("checked", false);
				//	return false;
				}
			
			//alert(currentSplitId);
			
			 var rowNum=0;
			 var rowNum1=0;
			 var INITIATED_AREA=0;
			 var WORK_AREA=0;
			 var INITIATED_AREA1=0;
			 var WORK_AREA1=0;
			 
			 $("[name^="+workAreaId+"]").each(function () {
			   	    var splitid=$(this).attr("id").split(workAreaId)[1].split("ALLOCATE_AREA")[0];
			   	    var workAreaRowId=$(this).attr("id");
			   	   	if(splitid>currentSplitId){
			   	   		$("#"+$(this).attr("id")).val('0');
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0');
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text('0');
			   	   	}
			   	});	
			 var materialAvbleQty=$("#MATERIAL"+id+"WORK_AREA").text();
			 var AllocateArea=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
			 var AvailableArea=parseFloat($("#LABOR"+id+"WORK_AREA").text()==''?0:$("#LABOR"+id+"WORK_AREA").text());
			 var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			 $("#MATERIAL"+id+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
			 
			 var currentVal=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
			 var currentAllocateval=parseFloat($("#LABOR"+id+"WORK_AREA").text());
			 var currentVal1=parseFloat($("#MATERIAL"+id+"ALLOCATE_AREA").val()==''?0:$("#MATERIAL"+id+"ALLOCATE_AREA").val());
			 var currentAllocateval1=parseFloat($("#MATERIAL"+id+"WORK_AREA").text());
			 
			$("[name^="+workAreaId+"]").each(function () {
		   	    var splitid=$(this).attr("id").split(workAreaId)[1].split("ALLOCATE_AREA")[0];
		   	    var workAreaRowId=$(this).attr("id");
		   	    var recordType=$(this).attr("id").split(workAreaId)[0];
		   		if(recordType=="LABOR"){
		   			rowNum+=parseFloat($(this).val()==''?0:$(this).val());
		   		    WORK_AREA=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text());
		    	 	INITIATED_AREA=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text());
				}else{
					rowNum1+=parseFloat($(this).val()==''?0:$(this).val());
					WORK_AREA1=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text());
				   	INITIATED_AREA1=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text());
				}
		   	   	//alert("splitid: "+splitid);
		   	   	if(splitid>currentSplitId){
		   	   		$("#"+$(this).attr("id")).val('0');
		   	   		if(recordType=="LABOR"){	   	   		
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((currentAllocateval-currentVal).toFixed(2));
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text((INITIATED_AREA+rowNum).toFixed(2));
		   	   		}else{
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((currentAllocateval1-currentVal1).toFixed(2));
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text((INITIATED_AREA1+rowNum1).toFixed(2));
		   	   		}   	  
		   	   	}
		   	});
			
			
			//checking accepted rate with BOQ amount
			if(ACCEPTED_RATEVal>BOQ_RATEVal){
				alert("You can't initiate more than BOQ Rate.");
				$("#"+ACCEPTED_RATEID).val("");
				$("#"+ACCEPTED_RATEID).focus();
			}
			if(AvailableArea<AllocateArea){
				alert("You can't initiate more than available area.");
				$("#LABOR"+id+"ALLOCATE_AREA").val("0");
				$("#LABOR"+id+"ALLOCATE_AREA").focus();
				var aftermorevalrowNum=0;
				var aftermorevalrowNum1=0;
				var INITIATED_AREA=0;
				var INITIATED_AREA1=0;		
			    $("[name^="+workAreaId+"]").each(function () {
			   	    var splitid=$(this).attr("id").split(workAreaId)[1].split("ALLOCATE_AREA")[0];
			   	    var workAreaRowId=$(this).attr("id");	   	 
			   	   	if(splitid>currentSplitId){
			   	   		$("#"+$(this).attr("id")).val('0');
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0');
			   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text('0');
			   	   	}
			   	});
			     var materialAvbleQty=$("#MATERIAL"+id+"WORK_AREA").text();
				 var AllocateArea=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
				 var AvailableArea=parseFloat($("#LABOR"+id+"WORK_AREA").text()==''?0:$("#LABOR"+id+"WORK_AREA").text());
				 var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
				 $("#MATERIAL"+id+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
				 
				 var currentVal=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
				 var currentVal1=parseFloat($("#MATERIAL"+id+"ALLOCATE_AREA").val()==''?0:$("#MATERIAL"+id+"ALLOCATE_AREA").val());
				 var currentAllocateval=parseFloat($("#LABOR"+id+"WORK_AREA").text());		  
				 var currentAllocateval1=parseFloat($("#MATERIAL"+id+"WORK_AREA").text());	
				$("[name^="+workAreaId+"]").each(function () {
					debugger;			
			   	    var splitid=$(this).attr("id").split(workAreaId)[1].split("ALLOCATE_AREA")[0];
			   	    var workAreaRowId=$(this).attr("id");
			   	    var recordType=$(this).attr("id").split(workAreaId)[0];
			   		if(recordType=="LABOR"){
			   			aftermorevalrowNum+=parseFloat($(this).val()==''?0:$(this).val());
			   		    WORK_AREA=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text());
				   	 	INITIATED_AREA=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text());
			   		}else{
			   			aftermorevalrowNum1+=parseFloat($(this).val()==''?0:$(this).val());
			   		    WORK_AREA1=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text());
				   	 	INITIATED_AREA1=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text());
			   		}	   	   
			   	   	if(splitid>currentSplitId){
			   	   		$("#"+$(this).attr("id")).val('0');
			   	   		if(recordType=="LABOR"){
				   	   		$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((currentAllocateval-currentVal).toFixed(2));
				   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text((INITIATED_AREA+aftermorevalrowNum).toFixed(2));
			   	   		}else{
				   	   		$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((currentAllocateval1-currentVal1).toFixed(2));
				   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text((INITIATED_AREA1+aftermorevalrowNum1).toFixed(2));
			   	   		}	   	   	    
			   	   	}
			   	});
			}	
			 var materialAvbleQty=$("#MATERIAL"+id+"WORK_AREA").text();
			 var AllocateArea=parseFloat($("#LABOR"+id+"ALLOCATE_AREA").val()==''?0:$("#LABOR"+id+"ALLOCATE_AREA").val());
			 var AvailableArea=parseFloat($("#LABOR"+id+"WORK_AREA").text()==''?0:$("#LABOR"+id+"WORK_AREA").text());
			 var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			 $("#MATERIAL"+id+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
		}
		
		$(document).ready(function() {
			 
						/* selectall checkbox */
						$("#checkAll").click(function() {
									$('input:checkbox').not(this).prop('checked', this.checked);
									console.log("checked");
						});

		});
		</script>
		
		
		<script type="text/javascript">
function loadSubProds(prodId, rowNum) {debugger;
	debugger;
	var requesteddate=$("#workOrderId").val();
	if(requesteddate == "" || requesteddate == '' || requesteddate == null) {
		alert("Please choose requested date .");
		document.getElementById("requesteddate").focus();
	}
	
	prodId = prodId.split("$")[0];
	var siteId=$("#siteId").val();
	var url = "workOrderSubProducts.spring?mainProductId="+prodId+"&typeOfWork=${WorkOrderBean.typeOfWork}&isApproveWOPage=true&siteId="+siteId;;
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				$("#WO_MinorHead"+rowNum).val("");
				$("#WO_Desc"+rowNum).val("");
			        $("#UnitsOfMeasurementId"+rowNum).html("");
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

function loadSubSubProducts(subProdId, rowNum) {debugger;
	subProdId = subProdId.split("$")[0];
	var siteId=$("#siteId").val();
	var url = "workOrderChildProducts.spring?subProductId="+subProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}&isApproveWOPage=true&siteId="+siteId;;
	var request = getAjaxObject();
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				debugger;
				$("#WO_Desc"+rowNum).val("");
			    	$("#UnitsOfMeasurementId"+rowNum).html("");
				$("#scopeOfWork"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();			
		    	var spltData = resp.split("|");		    	
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

function loadUnits(childProdId, rowNum) {debugger;
	
	childProdId = childProdId.split("$")[0];
	var siteId=$("#siteId").val();
	var url = "listOfWOmesurment.spring?childProductId="+childProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}&isApproveWOPage=true&siteId="+siteId;
	var request = getAjaxObject();
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				debugger;
				$("#UnitsOfMeasurementId"+rowNum).html("");
				$("#scopeOfWork"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				var spltData = resp.split("|");
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	
		    	var unitsToSelect = "UnitsOfMeasurementId"+rowNum;
		    	var selectBox = document.getElementById(unitsToSelect);
		    	
			    //Removing previous options from select box - Start
			    if(document.getElementById(unitsToSelect) != null && document.getElementById(unitsToSelect).options.length > 0) {
			    	/* document.getElementById(unitsToSelect).options.length = 0; */
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

function populateContractor() {
	
	var contractorName=$("#contractorName").val();
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
				  $("#contractorName").val(contractorName);
				  if(data!=""||data!="null"){

					  var contractorData=data[0].split("@@");
					var contractorId=contractorData[0];
					
					$("#contractorId").val(contractorId);
					 $("#contractorAddress").val(contractorData[2]);
					  $("#contractorPhoneNo").val(contractorData[3]);
					  $("#contractorPanCardNo").val(contractorData[4]);
					  $("#contractorAddress").prop("readonly",true);
					  $("#contractorPhoneNo").prop("readonly",true);
					  $("#contractorPanCardNo").prop("readonly",true);  
				  }
				
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
	}

	 
	};

</script>
		<script>
		//title tooltip
		$('[data-toggle="tooltip"]').tooltip();
		
	
	$(document).ready(function() {
	    var max_fields      = 50; //maximum input boxes allowed
	    var wrapper         = $(".appen-div-workorder"); //Fields wrapper
	    var add_button      = $(".add_field_button"); //Add button ID
	    
	    var textCount = 1; //initlal text box count
	    $('.add_field_button').on("click",function(e){ //on add input button click
	        e.preventDefault();
	    var newTc=$("#workorder_modal_text1").val();
	    if(newTc.length==0){
	    	alert("Please enter terms and condition");
	    	return false;
	    
	    }
	        if(textCount < max_fields){ //max input box allowed
	        	textCount++; //text box increment
	            $(wrapper).append('<div class="col-md-12  no-padding-left remove-filed mrg-t-10"><div class="col-md-11"><input type="text" class="form-control"  title="'+newTc+'" value="'+newTc+'"  id="newtxtbox'+textCount+'"  onkeyup="workordertermstitle(this)"  name="termsAndCOnditions"/></div><div class="col-md-1"><button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#newtxtbox"+textCount).val(newTc);
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent().parent(".remove-filed").remove(); textCount--;
	    })
	});
	
	//for Terms and condition title on change
		function workordertermstitle(id){			 
			 $(id).attr("title", $(id).val());
		 }
	
 $(document).ready(function() {
		 var rows = document.getElementById("indentIssueTableId").getElementsByTagName("tr").length;
		for(var i=0;i<rows-1;i++){
			//for combobox
			$("#combobox"+(i+1)).combobox();    
			$( "#comboboxsubProd"+(i+1)).combobox();
			$("#comboboxsubSubProd"+(i+1)).combobox(); 
			$('.btn-visibilty'+(i+1)).closest('td').find('.custom-combobox-toggle').addClass('hide');
			//for readonly mode
			$('#WO_MajorHead'+(i+1)).attr('readonly', true);
			$('#WO_MajorHead'+(i+1)).addClass('form-control');
			$('#WO_MajorHead'+(i+1)).attr('title', $('#WO_MajorHead'+(i+1)).val());
			$('#WO_MinorHead'+(i+1)).attr('readonly', true);
			$('#WO_MinorHead'+(i+1)).addClass('form-control');
			$('#WO_MinorHead'+(i+1)).attr('title', $('#WO_MinorHead'+(i+1)).val());
			$('#WO_Desc'+(i+1)).addClass('form-control');
			$('#WO_Desc'+(i+1)).attr('readonly', true);
			$('#WO_Desc'+(i+1)).attr('title', $('#WO_Desc'+(i+1)).val());
			$('#UnitsOfMeasurementId'+(i+1)).attr('readonly', true);
			$('#Quantity'+(i+1)).attr('readonly', true);
			
		}
		 
		
	});
	 
 
//checking number number 
 function isNumberCheck(el, evt) {
 	
     var charCode = (evt.which) ? evt.which : event.keyCode;
     var num=el.value;
     var number = el.value.split('.');
     if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
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

//to distroy loader
 $(window).load(function () {
	 calGrandTotal();
	// $(".overlay_ims").hide();	
	// $(".loader-ims").hide();
		$(".copyPasteRestricted").bind('paste', function (e) {
			e.preventDefault();
		});
 });
 function calGrandTotal(){
	 debugger;
	 var WoGrandTotal=0;
		$(".totalRowamount").each(function(){						
			WoGrandTotal+=parseFloat($(this).val()==""?0:parseFloat($(this).val()));
		});
		/* var materialWoAmount=0;
	    var laborWoAmount=0;
		$(".workorderrowcls").each(function(){
			var currentId=$(this).attr("id").split("tr-class")[1];	
			laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val()==""?0:$("#LaborAmountId"+currentId).val());
			materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val()==""?0:$("#MaterialAmountId"+currentId).val());
		});
		materialWoAmount=materialWoAmount.toFixed(2);
		  laborWoAmount=laborWoAmount.toFixed(2);
		  $("#materialWoAmount").val(materialWoAmount);
		  $("#laborWoAmount").val(laborWoAmount);
		  $("#materialWoAmountSpan").text(materialWoAmount);
		  $("#laborWoAmountSpan").text(laborWoAmount);
		  WoGrandTotal=parseFloat(laborWoAmount)+parseFloat(materialWoAmount); */
		  $("#materialWoAmountSpan").text(inrFormat(parseFloat($("#materialWoAmountSpan").text()).toFixed(2)));
		  $("#laborWoAmountSpan").text(inrFormat(parseFloat($("#laborWoAmountSpan").text()).toFixed(2)));
		  $("#WoGrandTotalAmount").text(inrFormat(parseFloat($("#WoGrandTotalAmount").text()).toFixed(2)));
 }
 
//this code for to active the side menu 
 var referrer="viewPendingWOforApprove.spring";
 var workOrderStatus="${WorkOrderBean.workOrderStatus}";
 if("DraftModify"==workOrderStatus){
	 referrer="openDraftWorkOrders.spring";
 }else if("ModifyWorkOrder"==workOrderStatus){
	 referrer="openWorkOrderToModify.spring";
 }if(isCommonApproval=="true"){
	 referrer="viewWOforApproval.spring";
 }
 
 $SIDEBAR_MENU.find('a').filter(function () {
        var urlArray=this.href.split( '/' );
        for(var i=0;i<urlArray.length;i++){
     	 if(urlArray[i]==referrer) {
     		 return this.href;
     	 }
        }
 }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
 
//this method checking all the checkboxes 
	function checkAllCheckBox(rowNum){
		debugger;
		var flag="true";
		$("input[name='chk1"+rowNum+"'").not(this).prop('checked', this.checked);
		var len = $("input[name='chk1"+rowNum+"']:checked").length;
		$.each($("input[name='chk1"+rowNum+"']"), function(){            
			  var workAreaId=$(this).val();
		
			  var seletedArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
	          var accepted_rate=parseFloat($("#LABOR"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+workAreaId+"ACCEPTED_RATE").val());
	          var boq_rate=$("#LABOR"+workAreaId+"BOQ_RATE").val()==""?0:parseFloat($("#LABOR"+workAreaId+"BOQ_RATE").val());
	          var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text()==""?0:$("#LABOR" + workAreaId + "PAYMENTDONE").text());		             
			  var advPreviousBillAmount=parseFloat($("#LABOR" + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + workAreaId + "advPreviousBillAmount").val());;
			  var recordType=$("#LABOR"+workAreaId+"RECORD_TYPE").text()==""?0:$("#LABOR"+workAreaId+"RECORD_TYPE").text();
			  
	          /* var workAreaForDB=$("#"+recordType+workAreaId+"WOID").val()==undefined?"":$("#MATERIAL"+workAreaId+"WOID").val();
			  var workIssueDetailsId=$("#"+recordType+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val();
	          var seletedWorkArea= $("#"+recordType+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val();
	          if(recordType=="MATERIAL")
	   	  	  if(workAreaForDB.length!=0&&workIssueDetailsId.length!=0&&seletedWorkArea.length!=0){
	   			var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
	   			sendDataToBackEnd.push(data);	
	   		  } */

			  
			  
	          	if(seletedArea==0){
	    				alert("Please enter correct value.");
	    				$("#LABOR"+workAreaId+"ALLOCATE_AREA").focus();
	    				$("#LABOR" + workAreaId + "val").prop("checked", false);
	    				$("#checkAll"+rowNum+"").prop("checked",false);
	    				$("input[name='chk1"+rowNum+"'").not(this).prop('checked',false);
	    				flag="false";
	    				return false;
	    		}
         	   if(accepted_rate==0){
	                	alert("Please enter accepted rate.");
	                	$("#LABOR"+workAreaId+"ACCEPTED_RATE").focus();
	                	$("#LABOR" + workAreaId + "val").prop("checked", false);	
	                	$("#checkAll"+rowNum+"").prop("checked",false);
	                	$("input[name='chk1"+rowNum+"'").not(this).prop('checked',false);
	                	flag="false";
	                	return false;
	                }
         		if(accepted_rate>boq_rate){
     				alert("You can't enter accpeted rate more than BOQ rate.");
     				$("#LABOR"+workAreaId+"ACCEPTED_RATE").focus();
     				$("#LABOR"+workAreaId+"ACCEPTED_RATE").val('0');
     				$("#checkAll"+rowNum+"").prop("checked",false);
     				$("#LABOR" + workAreaId + "val").prop("checked", false);
     				flag="false";
     				return false;
     			}
         		
         		var varr=document.getElementById("checkAll"+rowNum+"").checked;
	            if(varr==false){
		       		if(paymentdone!=0||paymentdone!=0.0){
		       			alert("You can not remove this work area, already payment initiated.");
		       			$("input[name='chk1"+rowNum+"'").prop('checked', false);
		       			$("#LABOR" + workAreaId + "val").prop("checked", true);
		       			$("#MATERIAL" + workAreaId + "val").prop("checked", true);
		       			//$("#checkAll"+rowNum1+"").prop("checked",false);
		       			flag="false";
		       			return false;
		       		} 
		       		if(advPreviousBillAmount!=0){
		       			flag="false";
		       			return false;
		       		}
			   }
     });
		if(flag=="true"){
			$("input[name='chk1"+rowNum+"'").prop('checked', true);	
		}else{
			$("input[name='chk1"+rowNum+"'").prop('checked', false);
			
			$.each($("input[name='chk1"+rowNum+"']"), function(){            
				debugger;
				var workAreaId=$(this).val();
				var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text());
				var advPreviousBillAmount=parseFloat($("#LABOR" + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + workAreaId + "advPreviousBillAmount").val());;
	              if(paymentdone!=0||paymentdone!=0.0||advPreviousBillAmount!=0){
	            		$("#LABOR" + workAreaId + "val").prop("checked", true);
		       			$("#MATERIAL" + workAreaId + "val").prop("checked", true);
 		 			} 
			});
		}
	/* 	var varr=document.getElementById("checkAll"+rowNum+"").checked;
		if(varr==false){
			$("input[name='chk1"+rowNum+"'").prop('checked', false);
		} */
		
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
        <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
       </div>
      </div>
    </div>

  </div>
</div>
	<!-- modal popup for show quantity end -->
	<!-- modal popup for create work order table for show start -->
<!-- Modal -->
<div id="Modal-create-wo-popup" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width: 95%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">Work Area </h4>
      </div>
      <div class="modal-body">
     <div class="table-responsive">
      <table class="table table-bordered">
      <thead>
	  <thead>
	  <tr>
	    <th style="width:14%;" class="text-center"><input type="checkbox" id="checkAll" style="width:14px; height:14px;"></th>
	    <th style="width:14%;" >Block</th>
	    <th style="width:14%;" >Floor</th>
	    <th style="width:14%;" >Flat</th>
	    <th style="width:14%;" >Actual Area</th>
	    <th style="width:14%;" >Available Area</th>
	    <th style="width:14%;" >Measurement</th>
	    <c:if test="${WorkOrderBean.revision!=0&& WorkOrderBean.revision!=null&& not empty WorkOrderBean.revision}">
	 	   <th style="width:14%;" >Payment_Initiated_Area</th>
	    </c:if>
	    <th style="width:14%;" >Allocated Area</th>
	    <th style="width:14%;" >BOQ Rate(<%=acceptedRateCurrency %>)</th>       
	    <th style="width:14%;" >Accepted Rate(<%=acceptedRateCurrency %>)</th>        
	  </tr>
	  </thead>
	 
	  <tbody id="areaMapping">

	  </tbody>
      
      </table>
     </div>       

      </div>
      <div class="modal-footer">
        <div class="text-center center-block">
         <button type="button" id="hide-table-2" class="btn btn-warning" data-dismiss="modal">Submit</button>
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
						 <embed src="data:application/pdf;base64,${requestScope[pdfBase64]}" style="height:500px;width:100%;">
				      </div>
				      <div class="modal-footer">
				       <p class="text-center">
					     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
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
    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="data:image/jpeg;base64,${requestScope[i]}" />
          
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  <%} %>
		<!-- modal popup for invoice image end -->   
	
	
</body>
</html>
