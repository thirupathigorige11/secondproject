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
/* 	String requestorName = resource.getString("label.requestorName");
	String requestorId = resource.getString("label.requestorId"); */
	
	String projectName = resource.getString("label.projectName");
	/* String block = resource.getString("label.block");
	String floor = resource.getString("label.floor");
	String flatNumber = resource.getString("label.flatNumber");

	String slipNumber = resource.getString("label.slipNumber"); */
	String ContractorName = resource.getString("label.ContractorName");
	//String issueNumber = resource.getString("label.issueNumber");
	
	
	String serialNumber = resource.getString("label.serialNumber");
	/* String product = resource.getString("label.product");
	String subProduct = resource.getString("label.subProduct");
	String childProduct = resource.getString("label.childProduct"); */
	String quantity = resource.getString("label.quantity");
	String purpose = resource.getString("label.purpose");
	String remarks = resource.getString("label.remarks");
/* 	String uOrF = resource.getString("label.uOrF"); */
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
	String scope_Of_work= resource.getString("label.scope-Of-work");
	String acceptedRateCurrency=resource.getString("label.acceptedRateCurrency");
	String workOrderDateSelection=resource.getString("label.workOrderDateSelection");
	String measurement = resource.getString("label.UOM");
	String AcceptedRate = resource.getString("label.AcceptedRate");
	String totalAmount = resource.getString("label.TotalAmount");
/* 	String materialAmount = resource.getString("label.materialAmount");
	String laborAmount = resource.getString("label.laborAmount"); */

%>

<html>
<head>
		<script src="js/WorkOrder/workorder.js" type="text/javascript"></script>
		<script src="js/WorkOrder/CommonCode.js" type="text/javascript"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
 .anchormouseover:hover{
   cursor:pointer;
 }

</style>

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
            var WOMajorHeadId = "";
            var WOMajorHeadName = "";
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
		 	
		 	
			var measurement =  "<%= measurement %>";
			measurement = formatColumns(measurement);
			
			var rowNum = ele.match(/\d+/g);
			
            if(str1 == WO_MajorHead) {
            	WOMajorHeadId = ui.item.option.value;
            	WOMajorHeadName = ui.item.value;
                loadSubProds(WOMajorHeadId, rowNum);
                this._trigger( "select", event, {
                    item: ui.item.option
                  });
            }            
            else if(str1 == WO_MinorHead) {
            	WOMinorHeadId = ui.item.option.value;
            	WOMinorHeadName = ui.item.value;
                loadSubSubProducts(WOMinorHeadId, rowNum);
                this._trigger( "select", event, {
                    item: ui.item.option
                  });
            }
            else if(str1 == Wo_WorkDesc) {
            	debugger;
            	$("#WO_Desc"+rowNum).addClass("childproduct");
            	WOWorkDescId = ui.item.option.value;
            	Wo_WorkDesc = ui.item.value;
            	 this._trigger( "select", event, {
                     item: ui.item.option
                   });
                 loadUnits(WOWorkDescId, rowNum);
                 loadScopeOfWork(WOWorkDescId, rowNum);
                /*  var tablerowslength=$(".workorderrowcls").length;
                if(tablerowslength==1){
                    this._trigger( "select", event, {
                        item: ui.item.option
                      });
                    loadUnits(WOWorkDescId, rowNum);
                    loadScopeOfWork(WOWorkDescId, rowNum);
                }
                else{
                	var childstatus=childcampare(WOMajorHeadName, rowNum);
                	if(childstatus==true){
                		 this._trigger( "select", event, {
                             item: ui.item.option
                           });
                         loadUnits(WOWorkDescId, rowNum);
                         loadScopeOfWork(WOWorkDescId, rowNum);
                	} else{
                		
                		var emptyvalue=$("#WO_Desc"+rowNum).val();
                		loadUnits(emptyvalue, rowNum);
                        loadScopeOfWork(emptyvalue, rowNum);
                        $("#WO_Desc"+rowNum).val('');
                        return false;
                	}
                }  */
                 
            } else if(str1 == measurement) {
            	WOUOMId = ui.item.option.value;
            	WOUOMName = ui.item.value;
                debugger;
                var checkduplicate=checkingDuplicateRow(WOUOMId, rowNum);
                if(checkduplicate=="true"){
                	 loadWOWorkArea(WOUOMId, rowNum);
                     this._trigger( "select", event, {
                         item: ui.item.option
                       });
                }               
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
	function myscopefocus(id){
		 $("#hiddenrownum").val(id); 
		$("#modalForScopeWork"+id).modal();
	}
</script>
<script type="text/javascript">
//var delOne;

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
	 
	 var TotalAmount =  "<%= totalAmount %>";
	 TotalAmount = formatColumns(TotalAmount);
	 
	 <%-- var laborAmount =  "<%= laborAmount %>";
	 laborAmount = formatColumns(laborAmount);
	 
	 var materialAmount =  "<%= materialAmount %>";
	 materialAmount = formatColumns(materialAmount); --%>
	 
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
    }
    else {	
    	if(tableColumnName == WO_MajorHead) {
    	    var dynamicSelectBoxId = "combobox"+vfx;
    	    var div = document.createElement("select");
    	    div.setAttribute("name", tableColumnName+vfx);
    	    div.setAttribute("id", dynamicSelectBoxId);
    	    div.setAttribute("class", 'form-control');
    	    if(text == cellsLen-1) {
    	    	div.setAttribute("onkeydown", "appendRow()");    	    	
    	    }    	        	  
    	    var defaultOption = document.createElement("option");
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
	    	    option.text = "<%=prodName1 %>";
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
    		var div = document.createElement("select");
    	    div.setAttribute("name", "UnitsOfMeasurement"+vfx);
    	    div.setAttribute("id", "UOMId"+vfx);
    		 div.setAttribute("onchange", "return loadWOWorkArea(this.value,"+vfx+");");
    	    div.setAttribute("class", 'form-control');
    	    cell.appendChild(div);
    	}   	
    	else if(tableColumnName == quantityColumn) {
			
    		cell.className  = "w-70";
    		
    		var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("readonly", true);
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		     
		    var newSocialLink = document.createElement("a");
		    var liText = document.createTextNode("Click Here");
		    newSocialLink.appendChild(liText);
		    newSocialLink.setAttribute("onclick", "myFunction('"+vfx+"')");
		 
		    newSocialLink.setAttribute('id', 'click-show');
		    newSocialLink.setAttribute('class', 'anchormouseover');
		    cell.appendChild(newSocialLink);
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
			newSocialLink1.setAttribute("data-target", "#myModal-click");
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
		
			
			//show 
			 var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderArea'+vfx);
			 cell.append(divNew);
			 
			 var divNew=document.createElement("div");
			 divNew.setAttribute('id', 'appendWorkOrderWorkArea'+vfx);
			 cell.append(divNew);
		
			 cell.appendChild(newSocialLink1);
			 cell.appendChild(newLinkForMaterial);
   		}   	
    /* 	else if(tableColumnName == laborAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		}
    	else if(tableColumnName == materialAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
   		} */
    	else if(tableColumnName == TotalAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
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
		    div.setAttribute("name",  "MaterialAmountId"+vfx);
		    div.setAttribute("id",  "MaterialAmountId"+vfx);
		    div.setAttribute("class", 'materialAmount');
		    cell.appendChild(div);
			
			var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("name", "LaborAmountId"+vfx);
		    div.setAttribute("id","LaborAmountId"+vfx);
		    div.setAttribute("autocomplete", "off");
		    div.setAttribute("class", 'laborAmount');
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

//Validation started 
function validateRowData() {
	
 	  	var reqDate = "<%= WorkOrderDate %>";
 	  	reqDate = formatColumns(reqDate);
 	  	reqDate = '"'+reqDate+'"';
 	  	reqDate = reqDate.replace(/\"/g, "");
 	  	
 	  	var reqDateVal = document.getElementById(reqDate).value;
 	   var typeOfWork=$("#typeOfWork").val();
 		if(reqDateVal == "" || reqDateVal == null || reqDateVal == '') {
			alert("Please select date.");
			document.getElementById(reqDate).focus();
			return false;
	  	} 
 	  	//validation written by thirupathi
	  	var WorkOrderName=$("#workOrderName").val();
	  	var ContractorName =$("#contractorName").val();
	  	if(typeOfWork!="MATERIAL"){
		  	if(ContractorName == "" || ContractorName == null || ContractorName == '') {
				alert("Please enter contractor name.");
				$("#contractorName").focus();
				return false;
		  	} 
	  	}
		
		var elementList = document.getElementsByTagName("*");
      	
      	var rowNums = getAllProdsCount();
      	
      	var splitedRows = rowNums.split("|");
      	
      	for(var x=0; x < rowNums.length; x++) {
      		
      		var curRow = splitedRows[x];		
      		
      		var product = "<%= WO_MajorHead %>";
      	  	product = formatColumns(product)+curRow;
      	  	product = '"'+product+'"';
      	  	product = product.replace(/\"/g, "");
      	  
      	  	var subProduct = "<%= WO_MinorHead %>";
      	  	subProduct = formatColumns(subProduct)+curRow;
      	  	subProduct = '"'+subProduct+'"';
      	  	subProduct = subProduct.replace(/\"/g, "");
      	  
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
      						alert("Please enter minor head.");
      						document.getElementById(subProduct).focus();
      						return false;
      					}
      	    	  	}  	  
      		  	  	else if(elementList[i].id == childProduct) {
      	    		  	var childPro = document.getElementById(childProduct).value;
      					if(childPro == "" || childPro == null || childPro == '') {
      						alert("Please enter work description.");
      						document.getElementById(childProduct).focus();
							return false;
						}
      	    	  	}	  	 
      		  	else if(elementList[i].id == measurement) {
  	    		 	var units = document.getElementById(measurement).value;
  					if(units == "" || units == null || units == '') {
  						alert("Please select units of measurement.");
  						document.getElementById(measurement).focus();
  						return false;
  					}
				}       else if(elementList[i].id == quantity) {
      	    			var qty = document.getElementById(quantity).value;
      					if(qty == "" || qty == null || qty == '' || qty == '.') {
      						alert("Please select work order area.");
      						document.getElementById(quantity).value = "";
      						document.getElementById(quantity).focus();
      						myFunction(curRow);
      						return false;
      					}
      					if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
      						alert("Please select work order area.");
      						document.getElementById(quantity).value = "";
      						document.getElementById(quantity).focus();
      						myFunction(curRow);
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
#floortable tbody tr td:first-child, #floortable thead tr th:first-child {min-width: 20px;text-align: center; width:50px !important;}
#floortable thead, #floortable tbody tr{table-layout:fixed;display:table;width:100%;}
#floortable>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
#floortable thead{width: calc(100% - 17px) !important;}
.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}

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
 .txt-height{display:inline-block;border-bottom: solid 1px #000;min-height:10px;width: 100%;resize: vertical;}
 .txt-border:focus{box-shadow: none !important;}
</style>
	<style>
	 .header-modalworkOrder{
			background-color: #ccc;
		    border: 1px solid #ccc;
		    color: #000;
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
		font-size: 14px;
	    padding: 6px 12px;
	 }
	 .plus-button{	
	    font-size: 14px;
	    padding: 6px 12px;
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
	 margin-bottom:8px;
	 }
	 .mrg-btm{
	 margin-bottom:15px;
	 }
	 

/* css loader  */

.loader {
  border: 5px solid #f3f3f3;
  border-radius: 50%;
  border-top: 5px solid #3498db;
  width: 40px;
  height: 40px;
  -webkit-animation: spin 1s linear infinite; /* Safari */
  animation: spin 1s linear infinite;
}

/* Safari */
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
/*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:56px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu thead tr th{border-top:1px solid #000 !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/	 

#datepickerIcon{
    height: 30px;
    float: left;
    border: 1px solid #000 !important;
    margin-left: -1px;
    background-color:#ddd;
    border-radius: 0px 5px 5px 0px;
}
.fnt-18{
font-weight:1000;
font-size:18px;
}
.spanheading{
    float: left;
    margin-bottom: 10px;
    margin-top: 5px;
    font-size: 15px;
    font-weight: 1000;
    font-family: Calibri;
}

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
				
				<div class="right_col" role="main">
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Create Work Order</li>
						</ol>
					</div>
					<div>
			<div align="center"><span style="text-align: center;" ><font color=red size=4 face="verdana">${responseMessage}</font></span></div>
 <div class="col-md-12">
	<div class="overlay_ims" style="display: none;"></div>
	 <div class="loader-ims" id="loaderId" style="display: none;"> <!--  -->
		<div class="lds-ims">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingimsMessage">Loading...</div>
	</div>
<form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal">		
	  <div class="border-background-workorder">
		<span style="color:red;">${noStock}</span><br/>
		   <div class="col-md-12">
		    <div class="col-md-6">
		     <div class="form-group">
			<label class="control-label col-md-6"><%= TempWorkOrderNo%> <%= colon %></label>
			<div class="col-md-6" >
				<form:input path="siteWiseWONO" id="workOrderId"  name="workOrderId" class="form-control" readonly="true"/>
			</div>
			</div>
		    </div>
			<div class="col-md-6">
			 <div class="form-group">
			<label class="control-label col-md-6"><%= WorkOrderNo%> <%= colon %> </label>
			<div class="col-md-6" >
				<form:input path="workOrderNo" id="WorkOrderNo" class="form-control" autocomplete="off"  onblur="checkWorkOrderNo(this.value)"/><!-- onblur="checkWorkOrderNo(this.value)"  -->
			<input type="hidden" value="${WorkOrderBean.workOrderNo}" id="actualWorkOrderNumber" name="actualWorkOrderNumber">
			</div>
		   </div>
			</div>
		   
		     <div class="col-md-6">
		       <div class="form-group">
			<label class="control-label col-md-6"><%= WorkOrderName%> <%= colon %></label>
			<div class="col-md-6" >
				<form:input path="workOrderName" id="workOrderName"  name="workOrderName" class="form-control" autocomplete="off"  placeholder="Work Order Name"/>
			</div>
			</div>
		     </div>
			 <div class="col-md-6">
			  <div class="form-group">
			<label class="control-label col-md-6 col-xs-12"><%= WorkOrderDate%> <%= colon %> </label>
			<div class="col-md-6 col-xs-12 input-group">
				<form:input path="workOrderDate" id="WorkOrderDate" class="form-control"  autocomplete="off" onkeydown="return false" placeholder="dd-mm-yyyy"/><label class='btn input-group-addon '  onclick='openCalender()' for='WorkOrderDate'><span class='fa fa-calendar'></span></label>
				<form:input path="typeOfWork" id="typeOfWork" value="${WorkOrderBean.typeOfWork}" type="hidden"/>
				<form:input path="approverEmpId" type="hidden" name="approverEmpId" id="approverEmpId"/>
				<form:input path="approverEmpMail" type="hidden" name="approverEmpMail" id="approverEmpMail"/>
				<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo"/>
				<form:input path="workorderFrom" type="hidden" name="workorderFrom" id="workorderFrom"/>
				<form:input path="totalWoAmount" type="hidden" id="totalWoAmount" />
				<form:input path="siteId"  type="hidden" id="siteId" />
				<form:input path="boqRecordType" type="hidden"  id="WORecordContails"/>
				<form:input path="isSaveOrUpdateOperation"  type="hidden" id="isSaveOrUpdateOperation"/>
				<input name="TotalAmountOfWorkOrder"  type="hidden"  id="sumofTotalAmount"/>
			</div>
		   </div>
			 </div>
			 <c:if test="${WorkOrderBean.typeOfWork ne 'MATERIAL' }">
		     <div class="col-md-6">
		      <div class="form-group">
				<label class="control-label col-md-6"><%= vendorName %> <%= colon %>  </label>
				<div class="col-md-6" >
					    <form:input path="contractorName" id="contractorName"  onkeyup="populateContractor(this);"  autocomplete="off" class="form-control" placeholder="Contractor Name"/>
						<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId"/>
						<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO"/>

						
				</div>
				</div>
		     </div>
	  		<div class="col-md-6">
	  		 <div class="form-group">
				<label class="control-label col-md-6"><%= panCardNo %> <%= colon %> </label>
				<div class="col-md-6" >
					<form:input path="contractorPanNo"  name="contractorPanCardNo" id="contractorPanCardNo" class="form-control" readonly="true" placeholder="PAN Card Number"/>
				</div>
		   </div>
	  		</div>
			<div class="col-md-6">
			 <div class="form-group">
				<label class="control-label col-md-6"><%= Address %> <%= colon %>  </label>
				<div class="col-md-6" >
						<form:input path="contractorAddress" name="contractorAddress"  class="form-control" id="contractorAddress" readonly="true" placeholder="Contractor Address"/>
				</div>
				</div>
			</div>	
		   <div class="col-md-6">
		    <div class="form-group">
				 <label class="control-label col-md-6"><%= phoneNo %> <%= colon %> </label>
				 <div class="col-md-6">
					 <form:input path="contractorPhoneNo"  name="contractorPhoneNo"  class="form-control" id="contractorPhoneNo" readonly="true" placeholder="Phone Number "/>
				</div>
			</div>	
		   </div>
		   </c:if>
	</div>				
</div>
		    
		  <div class="col-md-12 no-padding-left no-padding-right">
		   <div class="table-responsive"> <!-- /*tblprodindissudiv*/ -->
		<table id="indentIssueTableId1" class="table table-bordered tblprodindissu" style="width:2000px;">
					
			<thead class="cal-thead-inwards">
			
  				<tr>
  				<th><%= serialNumber %></th>
  				<th><%= WO_MajorHead %></th>
  				<th><%= WO_MinorHead %></th>
  				<th><%= WO_Desc %></th>
  				<th><%= scope_Of_work %></th>
  				<th><%= measurement %></th>
  				<th><%= quantity %></th>
  				<th><%= totalAmount %></th>
  				<th><%= note %></th>
  				<th><%= actions %></th>
  				
  				</tr>
			</thead>
  				<tbody class="tbl-fixedheader-tbody">
				<tr id="workorderrow1" class="workorderrowcls">
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
						<form:select path="WO_Desc1" id="comboboxsubSubProd1" class="form-control" name="WO_Desc1"/>
						
					</td>
					<td>
						<form:input path="scopeOfWOrk1" class="form-control scopeOfWork1_class" name="scopeOfWork1" id="scopeOfWork1" onclick="myscopefocus(1)"  onkeydown="return false;"  autocomplete="off"/>	
						<div class="modalpopup1" id="modalpopup1"></div>					
					</td>
					<td>
						<form:select path="UnitsOfMeasurement1" id="UOMId1"  class="form-control"  onchange="loadWOWorkArea(this.value,1);"/>
					</td>
					<td class="w-70">
						<form:input path="Quantity1" readonly="true" id="QuantityId1" onkeypress="return validateNumbers(this, event);" class="form-control" autocomplete="off"/>
						<!--  data-toggle="modal" data-target="#Modal-create-wo-popup"  -->
						<a href="javascript:myFunction(1)" style="cursor: pointer;">Click Here</a><br>
						<a href="javascript:showDetailsFunction(1)" style="display: none;cursor: pointer;" onclick="showDetailsFunction(1)" data-toggle="modal" data-target="#myModal-click" id="showQty1">View Details</a>
						<a href="javascript:void(0)" style="cursor: pointer;display: none;" onclick="showWDGroupWiseMaterialDetails(1)"  id="showMaterialQty1">Material Details</a>
						<div id="appendWorkOrderArea1">
						</div>
						<div id="appendWorkOrderWorkArea1">
						
						</div>
					</td>
				<!-- 	<td>
						<input name="labourAmount1" id="LaborAmountId1"  class="form-control" autocomplete="off" readonly="true">
					</td>
					<td>
						<input name="materialAmount1" id="MaterialAmountId1"  class="form-control" autocomplete="off" readonly="true">
					</td> -->
					<td>
						<input name="TotalAmount1" id="TotalAmountId1"  class="form-control" autocomplete="off" readonly="true">
						<input type="hidden" name="WoRecordContains1" id="WoRecordContains1">
						<input type="hidden" name="labourAmount1" id="LaborAmountId1"  class="laborAmount">
						<input type="hidden" name="materialAmount1" id="MaterialAmountId1" class="materialAmount" >
					</td>
					<td>
						<form:input path="comments1" type="text" class="form-control" id="comments1" name="comments1"  autocomplete="off"/>
					</td>				
					<td>
						<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId1" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i></button>
						<button type="button" style="display:none;" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId1" onclick="deleteRow(this, 1)" class="btnaction"><i class="fa fa-trash"></i></button>
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
			<div class="col-md-12" style="padding-left:0px;margin-top:15px;margin-bottom:30px;">
				<div class="col-md-5 col-md-offset-7">
					 <div class="col-md-6 fnt-18">Labor Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="laborWoAmountSpan">0.00</span><form:input path="laborWoAmount" type="hidden"  class="form-control"  id="laborWoAmount"/></div>
					 <div class="clearfix"></div>
					 <div class="col-md-6 fnt-18">Material Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="materialWoAmountSpan">0.00</span><form:input path="materialWoAmount" type="hidden" class="form-control"   id="materialWoAmount"/></div>
					 <div class="clearfix"></div>
					 <div class="col-md-6 fnt-18 text-left">Grand Total</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="WoGrandTotalAmount">0.00</span></div>
				</div>
			</div>
			


<div id="myModal-click" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg" style="width:95%;">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center center-block">View Area Details</h4>
      </div>
      <div class="modal-body modal-content-scroll" >
      <div class="table-responsive">
       <table class="table table-bordered table-createwo-showquantity" >
       <thead>
        <tr>  
         <th>Block</th>
         <th>Floor</th>
         <th>Flat</th>
         <th>Measurement</th>
         <th>Record Type</th>
         <th>Allocate Area</th>
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
      <div class="table-responsive">
       <table class="table table-bordered table-createwo-showquantity" >
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




			<!-- Modal popup for workorder submit start-->
			<div id="myModal-workOrder" class="modal fade" role="dialog">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content" style="border-radius: 25px;">
			      <div class="modal-header header-modalworkOrder text-center" >
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title" style="font-weight:700;">Terms & Conditions</h4>
			      </div>
			      <div class="modal-body" style="overflow:hidden;">
			      <div class="col-md-12 appen-div-workorder">
				      <c:forEach items="${listTermsAndCondition }" var="TAC">
										
				      <div class="col-md-12 remove-filed">
						  <div class="col-md-11 no-padding-left">
						      <input type="hidden" name="TC_listSize${TAC.indexNumber}" value="${TAC.indexNumber}">
						      <input type="text" name="termsAndCOnditions" class="form-control" id="terms${TAC.indexNumber}" onkeyup="workordertermstitle(this)"  title="<c:out value='${TAC.strTermsConditionName}' />" value="<c:out value='${TAC.strTermsConditionName}' />"/>
						  </div>
						  <div class="col-md-1 margin-close">
						      <button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button>
						  </div>
				      </div>
				      </c:forEach>
			      </div>
			       <div class="col-md-12">
			        <div class="col-md-12">
			         <div class="input_fields_wrap">
					 <div class="col-md-11 no-padding-left">
						 <input type="text" class="form-control"  onkeyup="workordertermstitle(this)" id="workorder_modal_text1" name="termsAndCOnditions"/></div>
					 	<div class="col-md-1 margin-close"><button type="button" class="btn btn-success plus-button add_field_button" ><i class="fa fa-plus "></i></button></div>
					 </div>
					 </div>
			       <%--   <div class="col-md-11 margin-close" style="padding-right: 25px;">
			         <span class="spanheading"> (Optional)If you want to add CC In emails.</span>
			         	<input type="text" class="form-control"  id="email-popup-workorder" name="optionalCCmails" value="${optionalCCmails}">
			         </div> --%>
			       <!--   <div class="col-md-11 margin-close" style="padding-right: 25px;">
			         <span class="spanheading"> Subject</span>
			         <input type="text" class="form-control"  id="email-popup-workorder" placeholder="Please enter the subject">
			         </div> -->
			       </div>
			      </div>
			      <div class="modal-footer">
			       <div class="col-md-12 text-center center-block">
			         <button type="button" class="btn btn-warning" id="saveWorkOrder" data-dismiss="modal" onclick="saveRecords('SaveClicked')" >Submit</button>
			       </div>
			      </div>
			    </div>			
			  </div>
			</div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
		</form:form>
		
		<div class="col-md-12 text-center center-block" style="margin-bottom:10px;" >
		<!-- <input type="button" class="btn btn-warning" value="Draft Work Order" id="saveBtnId" onclick="submitbuttonclick(this.value)"> -->
		
		<button type="button" class="btn btn-warning icon-save" value="Draft Work Order" id="saveBtnId" onclick="submitbuttonclick(this.value)" style="width: ">
			<i class="fa fa-floppy-o" aria-hidden="true"></i>&emsp;Draft Work Order
		</button>
		
			<input type="button" class="btn btn-warning" value="Submit" id="saveBtnId1" onclick="submitbuttonclick(this.value)">
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
<script src="js/WorkOrder/WOCommonCode.js"></script>
  <script>
  		$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
  		
  		if (typeof(Storage) !== "undefined") {   	
		     debugger;
		    sessionStorage.setItem("rowsIncre", 1);
		     var i=parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
		       if(i==2){
		    	   sessionStorage.setItem("${UserId}tempRowsIncre12",1);
		           window.location.reload();
		       }else{
		    	   sessionStorage.setItem("${UserId}tempRowsIncre12",1);
		       }
		} else {
		   alert("Sorry, your browser does not support Web Storage...");
		}; 
		
		function validateWorkArea(val,row){
				debugger;
			 	
				val=$("#actualWorkAreaId"+row).val();
				var allocatedArea = parseFloat($("#" + val + "ALLOCATE_AREA").val());
				var availbleArea= parseFloat($("#" + val + "WORK_AREA").text());
				
				var actualAreaAlocatedQTY =	parseFloat($("#actualAreaAlocatedQTY"+row).val());
	            if(allocatedArea>availbleArea){
	            	alert("You can't allocate more than availbale area.");
	            	$("#"+val+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
	            	return false;
	            }
	    	}
		
		
		function validateWorkAreaVal(val) {
			debugger;
			var amt = parseFloat($("#LABOR" + val + "ALLOCATE_AREA").val());
			var availableArea=parseFloat($("#LABOR"+val+"WORK_AREA").text().trim());
			var accepted_rate=$("#LABOR"+val+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+val+"ACCEPTED_RATE").val();
			var boq_rate=$("#LABOR"+val+"BOQ_RATE").val()==""?0:parseFloat($("#LABOR"+val+"BOQ_RATE").val());
			if($("#LABOR" + val + "val").prop("checked")==true){
				$("#MATERIAL" + val + "val").prop("checked", true);
			}else{
				$("#MATERIAL" + val + "val").prop("checked", false);
			}
			
			debugger;
			
			if(amt>availableArea){
				$("#LABOR" + val + "ALLOCATE_AREA").val("0");
				alert("You can't allocate more than available area.");
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
		
			if (!isNum(amt)&&amt!=0) {
				alert("Please enter correct value.");
				$("#LABOR" + val + "ALLOCATE_AREA").val("0");
				$("#LABOR" + val + "ALLOCATE_AREA").focus();
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
			if($("#LABOR" + val + "val").is(":checked")==false){
				$("#LABOR" + val + "ALLOCATE_AREA").val("0");
			}
			
			if(amt==0){
				alert("Please enter correct value.");
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
			accepted_rate=parseFloat(accepted_rate);
			if(accepted_rate==0){
				alert("Please enter accepted rate.");
				$("#LABOR"+val+"ACCEPTED_RATE").focus();
				$("#LABOR"+val+"ACCEPTED_RATE").val('0');
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
			if(accepted_rate>boq_rate){
				alert("You can't enter more than BOQ amount.");
				$("#LABOR"+val+"ACCEPTED_RATE").focus();
				$("#LABOR"+val+"ACCEPTED_RATE").val('0');
				$("#LABOR" + val + "val").prop("checked", false);
				$("#MATERIAL" + val + "val").prop("checked", false);
				return false;
			}
			
		}
		
		function isNum(value) {
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value)
		}
		
		function calCulateTotalAmout(acceptedRate,rownum){
			if (typeof(Storage) !== "undefined") {
			     debugger;
		    // Retrieve
		    if(acceptedRate.length==0){
		    	return false;
		    }
		    acceptedRate=parseFloat(acceptedRate);
		    
		    var numRegex=/^[0-9]+$/;
		    if(!isNum(acceptedRate)){
		    	alert("Enter only digits");
		    	$("#AcceptedRateId"+rownum).val("");
		    	$("#TotalAmountId"+rownum).val("");
		    	return false;
		    }
			let  rowsIncre=sessionStorage.getItem("rowsIncre");
		    var quantity=$("#qtyForCalculate"+rowsIncre).val();
			var qty=$("#QuantityId"+rownum).val();
			$("#TotalAmountId"+rownum).val(qty*acceptedRate);
			
			} else {
			   alert("Sorry, your browser does not support Web Storage...");
			};
			//alert(acceptedRate*amt);
		}

		

		
		function myFunction(rowNUM) {
			
			 $('#Modal-create-wo-popup'+rowNUM).modal('hide'); 
	        //maintaining Area
	         $(".allocateAreaHidden").each(function(){
				var assigntohidden=$(this).val();
				var currentId=$(this).attr("id").split("ALLOCATE_AREAHIDDEN")[0];
				$("#"+currentId+"ALLOCATE_AREA").val(assigntohidden);
			})
			
			//maintaining rate
			$(".acceptedRateHidden").each(function(){
				var assigntohidden=$(this).val();
				var currentId=$(this).attr("id").split("ACCEPTED_RATEHIDDEN")[0];
				$("#"+currentId+"ACCEPTED_RATE").val(assigntohidden);
			})
			
			
			var appendId=$("#allocatedareaAppend").val();
			var mesurmentId="";
			var UOMId="";
			mesurmentId= $("#WO_Desc"+rowNUM).val();
		    UOMId=$("#UOMId"+rowNUM).val();
				 
				 if(mesurmentId==undefined||mesurmentId==""||mesurmentId==null){
					alert("Please select work descripition.");
					$("#WO_Desc"+rowNUM).focus();
					 return false;
				 }
				 if(UOMId==undefined||UOMId==""||UOMId==null){
						alert("Please select UOM.");
						$("#UOMId"+rowNUM).focus();
						return false;
				 }
			$('#Modal-create-wo-popup'+rowNUM).modal('show'); 
		}
		
		//this method checking all the checkboxes 
		function checkAllCheckBox(rowNum){
			var flag="true";
			$("input[name='chk1"+rowNum+"'").not(this).prop('checked', this.checked);
			var len = $("input[name='chk1"+rowNum+"']:checked").length;
			$.each($("input[name='chk1"+rowNum+"']"), function(){            
				var workAreaId=$(this).val();
			
				 var seletedArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
	             var accepted_rate=parseFloat($("#LABOR"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+workAreaId+"ACCEPTED_RATE").val());
	             var boq_rate=$("#LABOR"+workAreaId+"BOQ_RATE").val()==""?0:parseFloat($("#LABOR"+workAreaId+"BOQ_RATE").val());
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
	        });
			if(flag=="true"){
				$("input[name='chk1"+rowNum+"'").prop('checked', true);	
			}else{
				$("input[name='chk1"+rowNum+"'").prop('checked', false);
			}
			var varr=document.getElementById("checkAll"+rowNum+"").checked;
			if(varr==false){
				$("input[name='chk1"+rowNum+"'").prop('checked', false);
			}
			
		}
		function closeWorkAreaPopup(rowsIncre){
			$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
		}
		//adding duplicate row
		function addDuplicateWorkAreaRow(workAreaId,rowNum,id){
		debugger;
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
		 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;cursor:pointer;' class='workareatr' id='LABOR"+workAreaId+id+"val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value)' value='"+workAreaId+id+"'></td> ";
		 str+="<td style=''  id='LABOR"+workAreaId+id+"BLOCK_NAME'>"+LABORblock_name+"</td> ";//<input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'>
		 str+="<td style='' id='LABOR"+workAreaId+id+"FLOOR_NAME'> "+(LABORfloor_name==null?"-":LABORfloor_name)+"</td>";
		 str+="<td style=''  id='LABOR"+workAreaId+id+"FLAT_ID' >"+(LABORflat_id)+"<input type='hidden' id='workAreaIdValue"+workAreaId+id+"' value='"+workAreaForDB+"'>   </td>";
		 str+="<td style='' id='LABOR"+workAreaId+id+"ACTUAL_AREA' > "+LABORactualArea.toFixed(2)+" </td>";
		 str+="<td style='' id='LABOR"+workAreaId+id+"WORK_AREA' > "+(LABORavailableArea.toFixed(2))+"<input type='hidden' id='LABOR"+workAreaId+id+"WOID' value='"+LABORWOID+"'> </td>";
		 str+="<td style='' id='LABOR"+workAreaId+id+"INITIATED_AREA' >"+LABORinitiated_area.toFixed(2)+"</td>";
		 str+="<td  style='' id='LABOR"+workAreaId+id+"WO_MEASURMEN_NAME'>"+(LABORwo_measurmen_name)+"</td>";
		 str+="<td  style='' id='LABOR"+workAreaId+id+"RECORD_TYPE'>"+(LABORrecord_type)+"<input type='hidden' value='"+Contains+"' id='LABOR"+workAreaId+id+"Contains'></td>";
		  
		 str+="<td style=''  id='LABOR"+workAreaId+id+"AVAILABE_AREA12'> <input type='text' class='form-control allocateArea"+rowNum+" pastenotallowed' style='width: 100%;' id='LABOR"+workAreaId+id+"ALLOCATE_AREA' name='"+workAreaForDB+"' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)'> <input type='hidden' class='allocateAreaHidden' id='"+workAreaId+id+"ALLOCATE_AREAHIDDEN'></td>";
		 str+="<td style='' id='LABOR"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control pastenotallowed' style='width: 100%;' id='LABOR"+workAreaId+id+"BOQ_RATE' value='"+LABORarea_price_per_unit.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
	     str+="<td style='' id='LABOR"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control acceptedRate"+rowNum+" pastenotallowed' style='width: 100%' id='LABOR"+workAreaId+id+"ACCEPTED_RATE' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)'><input type='hidden' class='acceptedRateHidden' id='"+workAreaId+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaId+id+"' id='hiddenid"+workAreaId+id+"'></td>";
		 str+="<td style=''><button type='button'  class='btnaction' id='LABOR"+workAreaId+id+"Btn' onclick='addDuplicateWorkAreaRow(\""+workAreaId+id+"\","+rowNum+","+(id)+")'><i class='fa fa-plus'></i></button></td>";
		 str+="</tr>";
		 
		 if(ContainsSpecailChar==true){  
			 str+="<tr  id='MATERIAL"+workAreaId+id+"Row'>";
			 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;display:none;' class='workareatr' id='MATERIAL"+workAreaId+id+"val' name='chk1"+rowNum+"' value='"+workAreaId+id+"'></td> ";
			 str+="<td style=''  id='MATERIAL"+workAreaId+id+"BLOCK_NAME'>"+MATERIALblock_name+"</td> ";//<input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'>
			 str+="<td style='' id='MATERIAL"+workAreaId+id+"FLOOR_NAME'> "+(MATERIALfloor_name==null?"-":MATERIALfloor_name)+"</td>";
			 str+="<td style=''  id='MATERIAL"+workAreaId+id+"FLAT_ID' >"+(MATERIALflat_id)+"<input type='hidden' id='workAreaIdValue"+workAreaId+id+"' value='"+workAreaForDB+"'></td>";
			 str+="<td style='' id='MATERIAL"+workAreaId+id+"ACTUAL_AREA' > "+MATERIALactualArea.toFixed(2)+" </td>";
			 str+= "<td style='' id='MATERIAL"+workAreaId+id+"WORK_AREA' > "+(MATERIALavailableArea.toFixed(2))+"<input type='hidden' id='MATERIAL"+workAreaId+id+"WOID' value='"+MATERIALWOID+"'> </td>";
			 str+="<td style='' id='MATERIAL"+workAreaId+id+"INITIATED_AREA' >"+MATERIALinitiated_area.toFixed(2)+"</td>";
			 str+="<td  style='' id='MATERIAL"+workAreaId+id+"WO_MEASURMEN_NAME'>"+(MATERIALwo_measurmen_name)+"</td>";
			 str+="<td  style='' id='MATERIAL"+workAreaId+id+"RECORD_TYPE'>"+(MATERIALrecord_type)+"</td>";		  
			 str+="<td style=''  id='MATERIAL"+workAreaId+id+"AVAILABE_AREA12'> <input type='text' class='form-control allocateArea"+rowNum+"' style='width: 100%;' id='MATERIAL"+workAreaId+id+"ALLOCATE_AREA' name='"+workAreaForDB+"' value='0' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)' readonly='readonly'> <input type='hidden' class='allocateAreaHidden' id='"+workAreaId+id+"ALLOCATE_AREAHIDDEN'></td>";
			 str+="<td style='' id='MATERIAL"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control' style='width: 100%;' id='MATERIAL"+workAreaId+id+"BOQ_RATE' value='"+MATERIALarea_price_per_unit.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
		     str+="<td style='' id='MATERIAL"+workAreaId+id+"AVAILABE_AREA13'> <input type='text' class='form-control acceptedRate"+rowNum+"' style='width: 100%' id='MATERIAL"+workAreaId+id+"ACCEPTED_RATE'  value='"+MATERIALarea_price_per_unit+"' onkeyup='validateBBQ(\""+workAreaId+id+"\")' onkeypress='return isNumberCheck(this, event)' readonly='readonly'><input type='hidden' class='acceptedRateHidden' id='"+workAreaId+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaId+id+"' id='hiddenid"+workAreaId+id+"'></td>";
			 str+="<td style=''></td>";
			 str+="</tr>";
			 
			 $("#MATERIAL"+workAreaId+"Row").after(str);
		 }else{
			 $("#LABOR"+workAreaId+"Row").after(str);
		 }
			$(".pastenotallowed").bind('paste', function (e) {//here preventing to enter alphabets
				e.preventDefault();
			});
			//$("#MATERIAL"+workAreaForDB+rowNum+id+rowNum+"Row").after(str);
			$("#LABOR"+workAreaId+"Btn").hide();
		}
	
		//this code is for appending all the values of allocated area
		function addWorkArea(rowsIncre, operationtype){
			var materialWoAmount=0.0;
			var laborWoAmount=0.0;
			
			var totalQuantity = 0;
			debugger;
			var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
			 if(operationtype=="1"){
				 $("#showQty"+rowsIncre).show();
				 $("#showMaterialQty"+rowsIncre).show();
				$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
				return false;
			} 
			if(operationtype!="1")
			if(len==0){
					alert("Please select atleast one work order area.");
					$('#Modal-create-wo-popup'+rowsIncre).modal('show'); 
					
					return false;
			}
			
			//maintaining old values
			
			$(".allocateArea"+rowsIncre+"").each(function(){
			var assigntohidden=$(this).val();
			var currentId=$(this).attr("id").split("ALLOCATE_AREA")[0];
			$("#"+currentId+"ALLOCATE_AREAHIDDEN").val(assigntohidden);
			})
			
			$(".acceptedRate"+rowsIncre+"").each(function(){
				var assigntohidden=$(this).val();
				var currentId=$(this).attr("id").split("ACCEPTED_RATE")[0];
				$("#"+currentId+"ACCEPTED_RATEHIDDEN").val(assigntohidden);
			})
			
			var canIcloseModelPopup=true;
			var rows=0;
			var totalAmount=0;
			var htmlData="";	
			var workOrderContains=new Array();
			var  workOrderTypeOfWork=$("#typeOfWork").val();
			$.each($("input[name='chk1"+rowsIncre+"']:checked"), function(){            
				debugger;
				rows++;
				var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
    			var workAreaId=$(this).val();
    			var recordId=$(this).attr("id").split(workAreaId)[0];
    			var workAreaForDB=$("#"+recordId+workAreaId+"WOID").val()==""?0:$("#"+recordId+workAreaId+"WOID").val();
                var seletedArea=parseFloat($("#"+recordId+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#"+recordId+workAreaId+"ALLOCATE_AREA").val());
                var availableArea=parseFloat($("#"+recordId+workAreaId+"WORK_AREA").text().trim());
                var wo_measurmen_name=$("#"+recordId+workAreaId+"WO_MEASURMEN_NAME").text();
                var wo_mesurment_id=$("#"+recordId+workAreaId+"UOMId").val();
                var record_type=$("#"+recordId+workAreaId+"RECORD_TYPE").text();
                
                var accepted_rate=parseFloat($("#"+recordId+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#"+recordId+workAreaId+"ACCEPTED_RATE").val());
                var boq_no=$("#"+workAreaId+"BOQ_NO").val();
                var BOQ_RATE=parseFloat($("#"+recordId+workAreaId+"BOQ_RATE").val().trim());
                var workOrderRowContains=$("#"+recordId+workAreaId+"Contains").val();
                const isInArray1 = workOrderContains.includes(workOrderRowContains);//
				if (isInArray1 == false) {
					debugger;
					workOrderContains.push(workOrderRowContains);
				}
              
                //var availableArea=(availableArea-seletedArea);
                if(record_type!="MATERIAL"){
                	totalAmount+=accepted_rate*seletedArea;
                	laborWoAmount+=accepted_rate*seletedArea;
            	}
                if(record_type=="MATERIAL"){
            		materialWoAmount+=accepted_rate*seletedArea;
                }
                //if this work order is Material Work Order then assigning material amount to total amount
                if(workOrderTypeOfWork=='MATERIAL'){
                	totalAmount=materialWoAmount;
                }
                
                if(seletedArea==0){
    				alert("Please enter correct value.");
    				$("#"+recordId+ workAreaId + "val").prop("checked", false);
    				canIcloseModelPopup=false;
    				return false;
    			}
                if(accepted_rate==0){
                	alert("Please enter accepted rate.");
                	$("#"+recordId+workAreaId+"ACCEPTED_RATE").focus();
                	canIcloseModelPopup=false;
                	return false;
                }
            	
                debugger;
                var value=seletedArea/availableArea;
                var percentage=value*100;
       		 //alert(percentage);
                if (isNaN(seletedArea)) {
    				alert("Please enter only digits.");
    				$("#"+recordId+ workAreaId + "ALLOCATE_AREA").val("0");
    				$("#"+recordId+ workAreaId + "val").prop("checked", false);
    				canIcloseModelPopup=false;
    				return false;
    			}
                if(seletedArea<=0){
                	alert("Please enter correct value for area");
                	canIcloseModelPopup=false;
                	return false;
                }
                if(seletedArea>availableArea){
                	alert("You can't allocate more the available area.");
                	$("#"+recordId+ workAreaId + "ALLOCATE_AREA").val("0");
                	$("#"+recordId+ workAreaId + "val").prop("checked", false);
                	canIcloseModelPopup=false;
                	return false;
                }
                
           	   	var block_name=$("#"+recordId+workAreaId+"BLOCK_NAME").text().trim();
                var floor_name=$("#"+recordId+workAreaId+"FLOOR_NAME").text().trim();
                var flat_id=$("#"+recordId+workAreaId+"FLAT_ID").text().trim();
            
                htmlData+="<input type='hidden' name='boq_no"+rowsIncre+"' id='boq_no"+rowsIncre+"' value='"+boq_no+"'>";
                htmlData+="<input type='hidden' name='lengthOfSelectedArea"+rowsIncre+"' id='lengthOfSelectedArea"+rowsIncre+"' value='"+len+"'>";
                htmlData+="<input type='hidden' name='BLOCK_NAME"+rowsIncre+"' id='BLOCK_NAMET"+rowsIncre+rows+"' value='"+block_name+"'>";
                htmlData+="<input type='hidden' name='FLOOR_NAME"+rowsIncre+"' id='FLOOR_NAMET"+rowsIncre+rows+"' value='"+floor_name+"'>";
                htmlData+="<input type='hidden' name='FLAT_ID"+rowsIncre+"' id='FLAT_IDT"+rowsIncre+rows+"' value='"+flat_id+"'>";
                htmlData+="<input type='hidden' name='selectedArea"+rowsIncre+"' id='selectedAreaT"+rowsIncre+rows+"' value='"+seletedArea+"'>";
                htmlData+="<input type='hidden' name='boqRate"+rowsIncre+"' id='boqRateT"+rowsIncre+rows+"' value='"+BOQ_RATE+"'>";
                htmlData+="<input type='hidden' name='accepted_rate"+rowsIncre+"' id='accepted_rateT"+rowsIncre+rows+"' value='"+accepted_rate+"'>";
                htmlData+="<input type='hidden' name='record_type"+rowsIncre+"' id='record_typeT"+rowsIncre+rows+"' value='"+record_type+"'>";
                
            	htmlData+="<input type='hidden' name='percentage"+rowsIncre+"' value='"+percentage+"'>";
                htmlData+="<input type='hidden' name='availableArea"+rowsIncre+"' value='"+availableArea+"'>";
                htmlData+="<input type='hidden' name='workAreaId"+rowsIncre+"' value='"+workAreaForDB+"'>";
                //htmlData+="<input type='hidden' name='available"+rowsIncre+"' value='"+availableArea+"'>";
                htmlData+="<input type='hidden' name='wo_measurmen_name"+rowsIncre+"' id='wo_measurmen_nameT"+rowsIncre+rows+"' value='"+wo_measurmen_name+"'>";
                if(record_type!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
               		totalQuantity += +seletedArea;
                }
    		});
		    debugger;
		    if(canIcloseModelPopup==false){
				return false;
			}
		    $("#appendWorkOrderArea"+rowsIncre).html(htmlData);
		    if($("#WORecordContails").val()==""||$("#WORecordContails").val().length==0){
			    $("#WORecordContails").val(workOrderContains.toString());
			  }
		    
		    materialWoAmount=materialWoAmount.toFixed(2);
		    laborWoAmount=laborWoAmount.toFixed(2);
		  
		    
		    $("#WoRecordContains"+rowsIncre).val(workOrderContains.toString());
		    $("#LaborAmountId"+rowsIncre).val(laborWoAmount);
		    $("#MaterialAmountId"+rowsIncre).val(materialWoAmount);
		    
		    materialWoAmount=0;
		    laborWoAmount=0;
			$(".workorderrowcls").each(function(){
				var currentId=$(this).attr("id").split("workorderrow")[1];	
				laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val()==""?0:$("#LaborAmountId"+currentId).val());
				materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val()==""?0:$("#MaterialAmountId"+currentId).val());
			});
			  materialWoAmount=materialWoAmount.toFixed(2);
			  laborWoAmount=laborWoAmount.toFixed(2);
			  $("#materialWoAmount").val(materialWoAmount);
			  $("#laborWoAmount").val(laborWoAmount);
			  $("#materialWoAmountSpan").text(materialWoAmount);
			  $("#laborWoAmountSpan").text(laborWoAmount);
			  $("#WoGrandTotalAmount").text((parseFloat(laborWoAmount)+parseFloat(materialWoAmount)).toFixed(2));
		      $("#sumofTotalAmount").val((parseFloat(laborWoAmount)+parseFloat(materialWoAmount)).toFixed(2));
		      
		      
		    if(operationtype!="1")
			$("#showQty"+rowsIncre).show();
			$("#showQty"+rowsIncre).css("display","inherit");
		    $("#showMaterialQty"+rowsIncre).show();
			var RowNumid=rowsIncre;
            $("#QuantityId"+RowNumid).val(totalQuantity);
            $("#TotalAmountId"+RowNumid).val(totalAmount.toFixed(2));
			$("#qtyForCalculate"+RowNumid).val("QuantityId"+RowNumid);
			$("#table-quantity").hide();
			$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
		}
		
		$(document).ready(function() {
			  /* selectall checkbox */
			 $("#checkAll").click(function() {
				 $('input:checkbox').not(this).prop('checked', this.checked);
				 debugger;
				 var thisname=$(this).val();
				 alert($(this).attr("name"));
				 debugger;
				 $.each($("input[name='chk1']:checked"), function(){            
					 var workAreaId=$(this).val();
					 var len = $("input[name='chk1']:checked").length;
					 var seletedArea=parseFloat($("#"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#"+workAreaId+"ALLOCATE_AREA").val());
					 var accepted_rate=parseFloat($("#"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#"+workAreaId+"ACCEPTED_RATE").val());
					 if(seletedArea==0){
							alert("Please enter correct value.");
							$("#"+workAreaId+"ALLOCATE_AREA").focus();
							$("#" + workAreaId + "val").prop("checked", false);
							$("#checkAll").prop("checked",false);
						    $('input:checkbox').not(this).prop('checked',false);
	    				return false;
	    			}
					 if(accepted_rate==0){
							 alert("Please enter accepted rate.");
							 $("#"+workAreaId+"ACCEPTED_RATE").focus();
							 $("#" + workAreaId + "val").prop("checked", false);	
							 $("#checkAll").prop("checked",false);
							 $('input:checkbox').not(this).prop('checked',false);
							 return false;
					  }
							            	
				});
			 console.log("checked");
		});

		});
		</script>
		
		
		<script type="text/javascript">
function loadSubProds(prodId, rowNum) {
	debugger;
	
	var requesteddate=$("#workOrderId").val();
	if(requesteddate == "" || requesteddate == '' || requesteddate == null) {
		alert("Please Choose Requested Date .");
		document.getElementById("requesteddate").focus();
	}
	
	prodId = prodId.split("$")[0];
	console.log("${WorkOrderBean.typeOfWork}");
	var url = "workOrderSubProducts.spring?mainProductId="+prodId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
	
	var request = getAjaxObject();
	try {
		request.onreadystatechange = function() {
			
			//alert("request.readyState "+request.readyState);
			
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
	console.log("${WorkOrderBean.typeOfWork}");
	var url = "workOrderChildProducts.spring?subProductId="+subProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
	var request = getAjaxObject();
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#WO_MinorHead"+rowNum).val("");
				$("#WO_Desc"+rowNum).val("");
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
//This method is for loading scope of the work row by row
function loadScopeOfWork(childProdId, rowNum){
	debugger;
	
	childProdId = childProdId.split("$")[0];
	var mesumentId="";//$("#UOMId"+rowNum).split("$")[0];
	var strData="";
	var WO_MinorHead=$("#comboboxsubProd"+rowNum).val().split("$")[0];
	debugger;
	console.log("${WorkOrderBean.typeOfWork}");
	var siteId=$("#siteId").val();
	var url = "loadScopeOfWork_AmountAndQty.spring?WO_MinorHead="+WO_MinorHead+"&childProductId="+childProdId+"&site_id="+siteId+"&mesumentId="+mesumentId+"&typeOfWork=${WorkOrderBean.typeOfWork}&isApproveOrRevisePage=false";
	  $.ajax({
		  url : url,
		  type : "get",
		contentType : "application/json",
		success : function(data) {
			
		console.log(data);
			var regExpr = /[^a-zA-Z0-9-. ]/g;
 			data=data.split("@@")[0];
 
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
				    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk2" name="ScopeOfWork'+rowNum+'" onkeyup="updateTitle(this)"></div>';//txt box
				    popupmodaldyn+='<div class="col-md-1"></div>'; //<button class="btn btn-success Addscope_txt" type="button" onclick="appendtextbox('+rowNum+')"><i class="fa fa-plus"></i></button>
				    popupmodaldyn+='</div>';
					popupmodaldyn+='</div>';
			    }
			    popupmodaldyn+='<div id="textboxDiv'+rowNum+'">';
			    popupmodaldyn+='</div>';
			    popupmodaldyn+='<div class="col-md-12">';
			    popupmodaldyn+='<div class="form-group">';
			    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk1" name="ScopeOfWork'+rowNum+'" onkeyup="updateTitle(this)"></div>';//txt box value="'+strData+'" title="'+strData+'"
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
 }

function loadWOWorkArea(UOMId, rowNum) {
	debugger;
	 var checkduplicate=checkingDuplicateRow(rowNum);
     if(checkduplicate=="false"){
    	 $("#UOMId"+rowNum).val('');
    	 return false;
     }
	
	var popupmodaldyn="";
    debugger;
	/*  jQuery.noConflict();*/
	 $('#Modal-create-wo-popup').modal('hide'); 
     $("#TotalAmountId"+rowNum).val('0');
		var appendId=$("#allocatedareaAppend").val();
		var mesurmentId="";
		var UOMId="";
	
		     debugger;
		
		 //$("#appendWorkOrderArea"+rowNum).html("");
		$("#QuantityId"+rowNum).val("");
	
		 mesurmentId= $("#WO_Desc"+rowNum).val();
		 UOMId=$("#UOMId"+rowNum).val();
		 
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
		 var mesurmentId=$("#comboboxsubSubProd"+rowNum).val().split("$")[0];
		 var  UnitsOfMeasurementId=$("#UOMId"+rowNum).val().split("$")[0];
		 var closeString="closebtn";
		 var submitString="submitbtn";
//	<!-- modal popup for create work order table for show start -->
	//<!-- Modal -->
		popupmodaldyn+=' <div id="Modal-create-wo-popup'+rowNum+'" class="modal fade" role="dialog">';
		popupmodaldyn+='<div class="modal-dialog modal-lg" style="width:95%;">';
	    popupmodaldyn+='<div class="modal-content">';
	    popupmodaldyn+='<div class="modal-header">';
	    popupmodaldyn+='<button type="button" class="close"  onclick="closeWorkAreaPopup('+rowNum+')" >&times;</button>';
		popupmodaldyn+='<h4 class="modal-title text-center">Work Area </h4>';
		popupmodaldyn+='</div>';
	   	popupmodaldyn+='<div class="modal-body">';
	    popupmodaldyn+='<div class="table-responsive">';
		popupmodaldyn+='<table class="table" id="floortable" style="">';
	 	popupmodaldyn+='<thead>';
		popupmodaldyn+='<tr>';
	    popupmodaldyn+='<th style="" class="text-center"><input type="checkbox" id="checkAll'+rowNum+'" style="width:100%;height:16px;cursor:pointer;margin-left: 26%;" value="'+rowNum+'" onclick="checkAllCheckBox('+rowNum+')"></th>';
	    popupmodaldyn+='<th style="" >Block</th>';
	    popupmodaldyn+='<th style="">Floor</th>';
	    popupmodaldyn+='<th style="" >Flat</th>';
	    popupmodaldyn+='<th style="" >Actual Area</th>';
	    popupmodaldyn+='<th style="" >Available Area</th>';
	    popupmodaldyn+='<th style="" >Initiated Area</th>';
	    popupmodaldyn+='<th style="" >Measurement</th>';
	    popupmodaldyn+='<th style="" >Record Type</th>';
	    popupmodaldyn+='<th style="" >Allocate Area</th>';
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
	  var workOrderTypeOfWork=$("#typeOfWork").val();
	  $("#loderId"+rowNum).show();
		debugger;
		var url = "loadWOAreaMapping.spring";
		$.ajax({
			url : url,
			type : "get",
			data:{mesurmentId:mesurmentId,
				UnitsOfMeasurementId:UnitsOfMeasurementId,
				typeOfWork:workOrderTypeOfWork
				},
			success : function(data) {
		//		data.sort(sortDataBlockFloorWise);
				str="";
				var tbleRowNum=1;
				var id=1;
			$.each(data,function(key,value){
					debugger;
					
					var initiated_area=value.WO_WORK_INITIATE_AREA==null?"0":parseFloat(value.WO_WORK_INITIATE_AREA).toFixed(2);
					var wo_work_area=value.WO_WORK_AREA==null?"0":parseFloat(value.WO_WORK_AREA).toFixed(2);
					var availableArea=value.WO_WORK_AVAILABE_AREA==null?"0":parseFloat(value.WO_WORK_AVAILABE_AREA).toFixed(2);
					var flatname=value.FLATNAME==null?"-":value.FLATNAME;
						
					//var QS_BOQ_DETAILS_ID	QWAM.QS_BOQ_DETAILS_ID
					var area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0":parseFloat(value.QS_AREA_PRICE_PER_UNIT).toFixed(2);
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
					
					//value.WO_CONTAINS
					
				   str+="<tr id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Row'> "
				   		if(value.RECORD_TYPE=='LABOR'||workOrderTypeOfWork=='MATERIAL'){
				   			str+="<td style='' class='text-center'><input type='checkbox'  style='width: 100%;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value)' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td> "
				   		}else{
				   			str+="<td style='' class='text-center'><input type='checkbox' style='width:100%;display:none;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1val' name='chk1"+rowNum+"'  value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td> "
				   		}			     		
				   str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BLOCK_NAME'>   "+value.BLOCK_NAME+"  <input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> "+
						"<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1FLOOR_NAME'> "+(value.FLOOR_NAME==null?"-":value.FLOOR_NAME)+"</td>"+
					    "<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1FLAT_ID' >"+(flatname)+"<input type='hidden' id='workAreaIdValue"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1' value='"+value.WO_WORK_AREA_GROUP_ID+"'></td>"+
					    "<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACTUAL_AREA' >"+wo_work_area+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1UOMId' name='UOMId' value='"+value.WO_MEASURMENT_ID+"'><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WOID' value='"+value.WO_WORK_AREA_ID+"'></td>"+
					    "<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WORK_AREA' > "+(availableArea)+" </td>"+
					    "<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1INITIATED_AREA' >"+initiated_area+" <input type='hidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BOQ_NO' value='"+value.BOQ_NO+"'>  </td>"+
					    "<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1WO_MEASURMEN_NAME'>"+(value.WO_MEASURMEN_NAME==null?"-":value.WO_MEASURMEN_NAME.toUpperCase())+"</td>";
				   str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1RECORD_TYPE'>"+value.RECORD_TYPE+"<input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Contains'></td>"; //"+area_price_per_unit+"value.WO_CONTAINS
						//code for allocate area   
				       if(isValueZero==0){
					    str+="<td style=''><input type='text' class='form-control'style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ALLOCATE_AREA' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"\",\""+value.RECORD_TYPE+"\")' readonly></td>";// "+(recordType)+"
					   }else{
					    str+="<td style=''><input type='text' class='form-control allocateArea"+rowNum+" pastenotallowed "+value.WO_WORK_AREA_GROUP_ID+"' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ALLOCATE_AREA'  name='"+value.WO_WORK_AREA_GROUP_ID+"'  value='0' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\",\""+value.RECORD_TYPE+"\")' onkeypress='return isNumberCheck(this, event)'   "+recordType+"> <input type='hidden' class='allocateAreaHidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ALLOCATE_AREAHIDDEN'></td>"; //"+value.WO_WORK_AVAILABE_AREA+"
					   }
					    str+="<td style=''><input type='text' class='form-control' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1BOQ_RATE' value='"+area_price_per_unit+"' title='"+area_price_per_unit+"' readonly='readonly'  style=' width: 66px;'> </td>";
					   
					    //code for accepted rate
					   if(isValueZero==0||value.RECORD_TYPE=="MATERIAL"&&workOrderTypeOfWork!='MATERIAL'){//this condition is for accepted rate readonly
					   	str+="<td style=''> <input type='text' class='form-control' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACCEPTED_RATE' value='"+area_price_per_unit+"' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\",\""+value.RECORD_TYPE+"\")' readonly> <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1' id='hiddenid"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td>";
					   }else{
					   	str+="<td style=''> <input type='text' class='form-control acceptedRate"+rowNum+" pastenotallowed' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1ACCEPTED_RATE' value='0' onkeyup='validateBBQ(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\",\""+value.RECORD_TYPE+"\")' onkeypress='return isNumberCheck(this, event)'><input type='hidden' class='acceptedRateHidden' id='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"' id='hiddenid"+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1'></td>"; //"+area_price_per_unit+"
					   }
					   
					    //if this is the meterial record of Work Order then do not show the plus button for new row
					    //if this is Material Work Order Show plus button
					   if(value.RECORD_TYPE!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
						   if(isValueZero!=0){
						   	str+="<td style=''><button type='button' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1Btn' class='btnaction' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+rowNum+id+"1\","+rowNum+",1)'><i class='fa fa-plus'></i></button></td>";
						   }else{
						   	str+="<td style=''></td>";
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

function loadUnits(childProdId, rowNum) {
	childProdId = childProdId.split("$")[0];
	debugger;
	console.log("${WorkOrderBean.typeOfWork}");
	var url = "listOfWOmesurment.spring?childProductId="+childProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
	  
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
	//myFunction();
}

function populateContractor() {
	
debugger;var contractorName=$("#contractorName").val();
var nameRegex=/^[a-zA-z ]+$/;
if(contractorName.length>0){

if(!nameRegex.test(contractorName)){
	alert("Please enter contractor name.");
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
	var workOrderTypeOfWork=$("#typeOfWork").val();
	var url = "checkIsDraftWorkOrderExistsOrNot.spring";
	 $.ajax({
		  url : url,
		  type : "get",
		 data:{
			 contractorId:contractorId,
			 siteId:siteId,
			 typeOfWork:workOrderTypeOfWork
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
					var url="showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="+draftedTempWoNumber+"&tempWorkOrderIssueNo=&siteId="+siteId+"&status=false&statusType=DF";
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
	 debugger;
	    	e.preventDefault();
	    var tc=$("#workorder_modal_text1").val();
	    if(tc.length==0){
	    	alert("Please enter terms and condition");
	    	$("#workorder_modal_text1").focus();
	    	return false;
	    }
	    
	        if(textCount < max_fields){ //max input box allowed
	        	textCount++; //text box increment
	            $(wrapper).append('<div class="col-md-12 remove-filed"><div class="col-md-11 no-padding-left"><input type="text" name="termsAndCOnditions"  title="'+tc+'"  value="'+tc+'" id="TC'+textCount+'" onkeyup="workordertermstitle(this)" class="form-control"/></div><div class="col-md-1"><button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
	        }
	        $("#workorder_modal_text1").val("");
	        $("#workorder_modal_text1").focus();
	        $("#TC"+textCount).val(tc);
	        
	    });
	    	
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); $(this).parent().parent(".remove-filed").remove(); textCount--;
	    })
	});
	
	</script>



<script>
var serialNumber=1;
function appendtextbox(btn){
	debugger;
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
	var defaultSOW=$("#"+btn+"defaultScopeOfWOrk1").val();

	if(defaultSOW.length==0){		
		alert("Please enter scope of work.");
		$("#"+btn+"defaultScopeOfWOrk1").focus();
		return false;
	}
	//defaultSOW=defaultSOW.replace(/[\"\""]/g, '');
	var appendtextid=parseInt(textlength)+1;
	 $("#textboxDiv"+btn).append('<div class="col-md-12" id="newtextId"><div class="form-group" id="newtxtbox'+appendtextid+'"><div class="col-md-11"><input type="text" name="ScopeOfWork'+btn+'"   id="'+btn+'defaultScopeOfWOrk'+appendtextid+'" class="form-control txt-height scopeofworkid'+btn+'" value="'+defaultSOW+'" title="'+defaultSOW+'" onkeyup="updateTitle(this)"></div><div class="col-md-1"><button class="btn btn-danger" type="button" onclick="deleteScopeOfWork('+appendtextid+')"><i class="fa fa-close"></i></button></div></div></div>');  
    serialNumber=1;
    $("#"+btn+"defaultScopeOfWOrk1").val('');
    $("#"+btn+"defaultScopeOfWOrk1").removeAttr('title');
    $("#"+btn+"defaultScopeOfWOrk1").focus();
    $("#"+btn+"defaultScopeOfWOrk"+appendtextid).val(defaultSOW);
}
function submitScopeOfWork(rowNum){
	debugger;
	$("#modalForScopeWork"+rowNum).modal("hide");
	//	$("#modal-certificatepayment-adv").modal('hide');
}

function validateBBQ(id,recordTypeOfWD){
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
//for Terms and condition title on change
	function workordertermstitle(id){
		 debugger;
		 $(id).attr("title", $(id).val());
	 }
	function submitbuttonclick(buttonValue){ 
		debugger;
		var reqDateVal =$("#WorkOrderDate").val();
	    var conName = $("#contractorName").val();  	
	    var WorkOrderName = $("#workOrderName").val();
	    var typeOfWork=$("#typeOfWork").val();
	    if(buttonValue!=undefined){
		 	$("#isSaveOrUpdateOperation").val(buttonValue);
	    }
 
 		if(reqDateVal == "" || reqDateVal == null || reqDateVal == '') {
			alert("Please select date.");
			$("#WorkOrderDate").focus();
			return false;
	  	} 
 		if(typeOfWork!="MATERIAL"){
 			if(conName == "" || conName == '' || conName == null) {
 	 	        alert("Please select the contractor name.");
 	 	       $("#contractorName").focus();
 	 	        return false;
 	 	    }
 		}
 		
 		 var valStatus = validateRowData();
 	     if(valStatus == false) {
 	          return false;
 	    }
		$("#saveBtnId").attr("data-toggle", "modal");
	    $("#saveBtnId").attr("data-target", "#myModal-workOrder");
	    $("#saveBtnId1").attr("data-toggle", "modal");
	    $("#saveBtnId1").attr("data-target", "#myModal-workOrder");
	
	}
	//to open calender when you click on calender icon 
	function openCalender(){
		$("#WorkOrderDate").focus();
	}
	//updating scope of work title
	function updateTitle(current){
		$(current).attr("title", $(current).val());
	}
</script>
<!-- script for scope of work order -->
</body>
</html>
