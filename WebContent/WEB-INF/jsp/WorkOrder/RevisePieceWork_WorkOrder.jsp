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
             
	        var WO_MajorHead =  "<%=WO_MajorHead%>";
	        WO_MajorHead = formatColumns(WO_MajorHead);
 		  	 
		  	var WO_MinorHead =  "<%=WO_MinorHead%>";
		  	WO_MinorHead = formatColumns(WO_MinorHead);
 		  
		 	var Wo_WorkDesc =  "<%=WO_Desc%>";
		 	Wo_WorkDesc = formatColumns(Wo_WorkDesc);
 			
			var rowNum = ele.match(/\d+/g);
	 		             
            if(str1 == WO_MajorHead) {
            	WOMajorHeadId = ui.item.option.value;
            	WOMajorHeadName = ui.item.value;
              //  
                loadSubProds(WOMajorHeadId, rowNum);
            }            
            else if(str1 == WO_MinorHead) {
            	WOMinorHeadId = ui.item.option.value;
            	WOMinorHeadName = ui.item.value;
                 loadSubSubProducts(WOMinorHeadId, rowNum);
            }
            else if(str1 == Wo_WorkDesc) {
            	$("#WO_Desc"+rowNum).addClass("childproduct");
            	WOWorkDescId = ui.item.option.value;
            	Wo_WorkDesc = ui.item.value;
                loadUnits(WOWorkDescId, rowNum);
               loadScopeOfWork(WOWorkDescId, rowNum); 
               
            /* 	  var tablerowslength=$(".reviseworkorderrowcls").length;
                  if(tablerowslength==1){
                      this._trigger( "select", event, {
                          item: ui.item.option
                        });
                      loadUnits(WOWorkDescId, rowNum);
                      loadScopeOfWork(WOWorkDescId, rowNum);
                  }else{
                  	var childstatus=childcampare(WOMajorHeadName, rowNum);
                  	if(childstatus==true){
                  		 this._trigger( "select", event, {
                               item: ui.item.option
                             });
                           loadUnits(WOWorkDescId, rowNum);
                           loadScopeOfWork(WOWorkDescId, rowNum);
                  	}else{
                  		var emptyvalue=$("#WO_Desc"+rowNum).val();
                  		 // loadUnits(emptyvalue, rowNum);
                        //  loadScopeOfWork(emptyvalue, rowNum);
                          $("#WO_Desc"+rowNum).val('');
                          return false;
                  	}
                  }  */
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
	 /*  $("#workOrderDate").datepicker({
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
/*fixed header */
 .tblprodindissu thead, .tblprodindissu tbody tr{table-layout:fixed;display:table;width:100%;}
 .tblprodindissu thead tr th:first-child,.tblprodindissu tbody tr td:first-child{width:56px !important;min-width: 20px;text-align: center}
 .tblprodindissu tbody tr td{border-top:0px !important;}
 .tblprodindissu{border:0px !important;}
/*fixed header*/
 /* table border color changes */
 .txt-border{border-bottom:1px solid #000 !important;border-top:0px !important;border-left:0px !important;border-right:0px !important;border-radius:0px;}
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
.btn-fullwidth:hover{
background-color:transparent; border-color:transparent; color:transparent; height:200px;
width:100%; margin-top:110px;
}
.btn-fullwidth{
background-color:transparent; border-color:transparent; color:transparent;height:200px;
width:100%; margin-top:110px;
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent; background-color: transparent; border-color: transparent;
}
 .btn-fullwidth:active{ color: transparent; background-color: transparent; border-color: transparent;}
 .btn-fullwidth.focus, .btn-fullwidth:focus { color: transparent; background-color: transparent; border-color: transparent;}
/*css for iframe modal popup*/

/* spinner stles */
.spinnercls{display: inline; height: 50px; position: absolute; right: 0px; top: 1px; display:none;}

 /* end spinner stles */
 
 /* for action coloum */
 
 /* #floortable thead tr th, #floortable tbody tr td{
min-width:120px !important;
}
#floortable thead tr th:first-child, #floortable tbody tr td:first-child {
    min-width: 20px !important;
    text-align:center;
    width:50px;
} */
/*Fixed Header table*/
#floortable tbody tr td:first-child, #floortable thead tr th:first-child {min-width: 20px;text-align: center; width:60px !important;}
#floortable thead, #floortable tbody tr{table-layout:fixed;display:table;width:100%;}
#floortable>thead>tr>th {text-align: center;border-top: 1px solid #000 !important;width:100%;border-bottom:1px solid #000 !important;}
#floortable thead{width: calc(100% - 17px) !important;}
.fixed-table-body{display: inline-block; max-height: 300px;overflow-y: scroll;overflow-x: auto;}
.workareatr{width: 100% !important;}

/*Fixed Header table*/
 /* .tblprodindissu tbody tr td:last-child, .tblprodindissu tbody tr th:last-child {
    min-width: 147px;
} */

 /* end for action coloum */
 .fnt-18{
	font-weight:1000;
	font-size:18px;
 }
 .remove-button, .plus-button{
	font-size: 12px;
    padding: 8px 12px;
}
.mrg-btm-10 {
    margin-bottom: 10px;
}
.spanheading {
    font-size: 14px;
    font-weight: 1000;
    font-family: Calibri;
}
.mrg-t-10{
	margin-top: 10px;
}
.spinnerclsModal{
	left: 45%;
    top: 30%;
    height: 150px;
    position: absolute;
    z-index: 9999999999;
    display:none;
}
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			

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
    	} else if(tableColumnName == scope_Of_work) {
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
		    //if(typeOfWork=="PIECEWORK"||typeOfWork=="MATERIAL"){
		    if(typeOfWork!="NMR"){
		    	div.setAttribute("readonly", true);
		    }
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+vfx);
	
		    div.setAttribute("onkeypress", "return validateNumbers(this, event);");
	
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);
		    
		    //
		    //if(typeOfWork=="PIECEWORK" ||typeOfWork=="MATERIAL"){
			if(typeOfWork!="NMR"){ 
			  //creates new li and new a
			   // var newSocial = document.createElement("li");
			    var newSocialLink = document.createElement("a");
			 //creates text in the li
			    var liText = document.createTextNode("Click Here");
			 // ads text to a
			    newSocialLink.appendChild(liText);
			//    newSocialLink.onclick = myFunction(vfx);
			    newSocialLink.setAttribute("onclick", "myFunction(0,0,'"+vfx+"','show')");
			 
			    newSocialLink.setAttribute('id', 'click-show');
			
			    cell.appendChild(newSocialLink);
			}
debugger;
			//FOr show Details
		  	var newSocialLink1 = document.createElement("a");
			// creates text in the li
			var liText1 = document.createTextNode("View Details");
			// ads text to a
			newSocialLink1.appendChild(liText1);
			// newSocialLink1.onclick = showDetailsFunction(vfx);
		 	newSocialLink1.setAttribute('id', 'showQty'+vfx);
		 	newSocialLink1.setAttribute("onclick", "showDetailsFunction("+vfx+")");
		 	newSocialLink1.setAttribute("href", "javascript:showDetailsFunction("+vfx+")");
		
		 	newSocialLink1.setAttribute("data-toggle", "modal");
			newSocialLink1.setAttribute("data-target", "#myModal-showwo-showquantity");
			
			 //data-toggle="modal" data-target="" 
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
    	
    	else if(tableColumnName == AcceptedRate) {
    		 var typeOfWork="${WorkOrderBean.typeOfWork}";
 		    //
 			if(typeOfWork=="NMR"){ 
	    		var div = document.createElement("input");
			    div.setAttribute("type", "text");
			    div.setAttribute("name", AcceptedRate+vfx);
			    div.setAttribute("id", AcceptedRate+vfx);
			    div.setAttribute("onkeypress","return isNumberCheck(this, event)");
			    div.setAttribute("onkeyup", "calCulateTotalAmout(this.value,'"+vfx+"')");
			    div.setAttribute("class", 'form-control');
			    div.setAttribute("autocomplete", "off");
			    cell.appendChild(div);
 			}
   		}
    	
    	
    	else if(tableColumnName == TotalAmount) {
			var div = document.createElement("input");
		    div.setAttribute("type", "text");
		    div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    div.setAttribute("readonly", "true");
		    //div.setAttribute("onkeydown", "appendRow()");
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
		    //div.setAttribute("name", tableColumnName+vfx);
		    div.setAttribute("name", "comments"+vfx);
		    div.setAttribute("id", tableColumnName+"Id"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("class", 'form-control');
		    cell.appendChild(div);			    
		//need to check 		    
		    var div = document.createElement("input");
		    div.setAttribute("type", "hidden");
		    div.setAttribute("id", "isDelete"+vfx);
		    //div.setAttribute("onkeydown", "appendRow()");
		    div.setAttribute("value", 'N');
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
	});
	return error;
}

//childproduct duplicate changes written by thirupathi
//imp, d'nt touch
function childcampare(childname, rowNum){
	$("#WO_Desc"+rowNum).val('');
	var rv=true;
	
	var tablelength=$(".reviseworkorderrowcls").length;	
	if(tablelength>1){
		
		jQuery('.childproduct').each(function() {
			
			var currentElement = $(this);
			      value = currentElement.val().split("$")[1];
			  if(value==childname){
				alert("This child product is already exist, Please choose different child product.");				
				return rv = false;
			   }     
			  else{				  
				  return rv = true;				  
			  }
		});
		 return rv;
	}
}

//checking duplicate row in table
function checkingDuplicateRow(rowNum){
	debugger;
	var error="true";
	var currentWO_MajorHead=$("#combobox"+rowNum).val().split("$")[0];
	var currentWO_MinorHead=$("#comboboxsubProd"+rowNum).val().split("$")[0];
	var currentWO_Desc=$("#comboboxsubSubProd"+rowNum).val().split("$")[0];
	var currentUOM=$("#UOMId"+rowNum).val().split("$")[0];
	$(".reviseworkorderrowcls").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		if(currentId!=rowNum){
		
			var WO_MajorHead=$("#combobox"+currentId).val().split("$")[0];
			var WO_MinorHead=$("#comboboxsubProd"+currentId).val().split("$")[0];
			var WO_Desc=$("#comboboxsubSubProd"+currentId).val().split("$")[0];
			var UOM=$("#UOMId"+currentId).val().split("$")[0];			
			if(currentWO_MajorHead==WO_MajorHead && WO_MinorHead==currentWO_MinorHead && currentWO_Desc==WO_Desc && currentUOM==UOM){
				alert("UOM already exist, Please choose different UOM.");
				return error="false";
			}
		}
	})

	return error;
	
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
						<li class="breadcrumb-item active">Revise Work Order</li>
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
					<!-- @RequestParam("file") MultipartFile[] files -->
					<c:if test="${isThisCorrectRevisedWorkOrder eq false }">
						<strong>You can not revise this Work Order because you already revised this Work</strong><!--  Order please check the revised Work Order  -->
					</c:if>
			<form:form modelAttribute="WorkOrderBean" id="workOrderFormId" class="form-horizontal" >
						<div class="col-md-12 border-background-workorder">
							<%-- <c:set  scope="session"   value="${WorkOrderBean}" var="WorkOrderBean"></c:set> --%>
							<c:set scope="session" var="deletedWorkOrderDetailsList" value="${deletedWorkOrderDetailsList}"></c:set>
							<c:set scope="session" value="${listTermsAndCondition}"	var="listTermsAndCondition"></c:set>
							<c:set scope="session" value="${WorkOrderLevelPurpose}"	var="WorkOrderLevelPurpose"></c:set>
							<c:set scope="session" value="${workOrderCreationList}"	var="workOrderCreationList"></c:set>
							<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
							<span style="color: red;">${noStock}</span><br />
				             <div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6"><%=WorkOrderNo%> <%=colon%></label>
									<div class="col-md-6">
										<form:input path="siteWiseWONO" id="workOrderId" name="workOrderId" class="form-control" readonly="true"  title="${WorkOrderBean.siteWiseWONO}" />
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6">Revise Work Order NO <%=colon%></label>
									<div class="col-md-6">
					     				  <input type="text" value="${strWorkOrderNumber1}" id="strWorkOrderNumber1" name="reviseWorkOrderNumber1" class="form-control" title="${strWorkOrderNumber1}" readonly="true" >  
									  <input type="hidden" value="${isThisCorrectRevisedWorkOrder }" id="isThisCorrectRevisedWorkOrder" name="isThisCorrectRevisedWorkOrder">
									<input type="hidden" name="revisionNumber" id="revisionNumber" value="${revisionNumber }">
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6"><%=WorkOrderDate%> <%=colon%> </label>
									<div class="col-md-6">
											<form:input path="workOrderDate" id="workOrderDate"	class="form-control" autocomplete="off" readonly="true" title="${WorkOrderBean.workOrderDate}" />
											
										<form:input path="GSTIN" type="hidden" id="contractorGSTINNO" name="contractorGSTINNO" />
										<form:input path="contractorId" type="hidden" name="contractorId" id="contractorId" />
										<form:input path="approverEmpId" type="hidden"	name="approverEmpId" id="approverEmpId" />
										<form:input path="approverEmpMail" type="hidden" name="approverEmpMail" id="approverEmpMail"/>
										<form:input path="workorderTo" type="hidden" name="workorderTo" id="workorderTo" />
										<form:input path="workorderFrom" type="hidden"	name="workorderFrom" id="workorderFrom" />
										<form:input path="siteId" id="site_id" type="hidden" />
										<form:input path="siteName" type="hidden" />
										<input type="hidden" name="operType" id="operType"	value="${operType}">										
										<c:set value="reviseWorkOrder" var="OperationType"></c:set>
										<form:input path="isUpdateWOPage" type="hidden" name="isUpdateWOPage" value="${isUpdateWOPage}" />
										<form:input path="typeOfWork" id="typeOfWork" type="hidden" />
										<form:input path="boqNo" id="boqNo" type="hidden" />
										<form:input path="revision" id="revision" type="hidden" />
										<form:input path="oldWorkOrderNo" id="oldWorkOrderNo" type="hidden" />
										<form:input path="versionNumber" id="versionNumber"	type="hidden" />
										<form:input path="totalWoAmount" type="hidden" id="totalWoAmount"/>
										<input type="hidden" name="TotalBOQAmount" id="TotalBOQAmount">
										<input type="hidden" name="TotalNMR_WO_initiatedAmount" id="TotalNMR_WO_initiatedAmount">
										<form:input path="boqRecordType"   type="hidden"  id="WORecordContails"/>
										<form:input path="isSaveOrUpdateOperation" id="isSaveOrUpdateOperation"  type="hidden" value=""/>
										
										<form:input path="materialWoAmount" id="materialWoAmount"  type="hidden"/>
										<form:input path="laborWoAmount" id="laborWoAmount"  type="hidden"/>
										<form:input path="woBillBilledAmount" id="woBillBilledAmount"  type="hidden"/>
										<form:input path="woBillPaidAmount" id="woBillPaidAmount"  type="hidden"/>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6"><%=WorkOrderName%> <%=colon%></label>
									<div class="col-md-6">
										<form:input path="workOrderName" id="workOrderName" name="workOrderName" class="form-control" title="${WorkOrderBean.workOrderName}" />
									</div>
								</div>
							</div>
				 <c:if test="${WorkOrderBean.typeOfWork ne 'MATERIAL' }">
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6"><%=vendorName%> <%=colon%> </label>
									<div class="col-md-6">
										<form:input path="contractorName" id="contractorName" onkeyup="populateContractor(this);" readonly="true" autocomplete="off" class="form-control" title="${WorkOrderBean.contractorName}" />

									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label class="control-label col-md-6"><%=panCardNo%> <%=colon%>
									</label>
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
									<label class="control-label col-md-6"><%=phoneNo%> <%=colon%>
									</label>
									<div class="col-md-6">
										<form:input path="contractorPhoneNo" name="contractorPhoneNo" class="form-control" id="contractorPhoneNo" readonly="true" title="${WorkOrderBean.contractorPhoneNo}" />
									</div>
								</div>
							</div>
						</c:if>
						</div>
						<c:set value="NMR" var="TypeOfWork"></c:set>
						<div class="clearfix"></div>
						<div class="">
							<div class="table-responsive">
								 <table id="indentIssueTableId"class="table table-bordered tblprodindissu" style="width:2000px;">
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
									<c:forEach items="${workOrderCreationList}" var="workOrderDetail">
									<c:set value="${indexnumber+1}" var="indexnumber"></c:set>
										 <input name="isDelete${indexnumber}" type="hidden" id="isDelete${indexnumber}" readonly="true" value="z" class="form-control input-visibilty${indexnumber}" />
									 <tr id="tr-class${indexnumber}" class="reviseworkorderrowcls">
										<td>
											 <div id="snoDivId1">
											 <c:out value="${indexnumber}"></c:out>
											 </div> 
											 <input name="dispplayedRows" id="dispplayedRows" type="hidden" value="${indexnumber}"> 
											 <input	type="hidden" name="actualWorkOrderNo" 	value="${WorkOrderBean.siteWiseWONO}"> 
											 <input type="hidden" name="nextApprovelEmpId" value="${WorkOrderBean.approverEmpId}">
											 <input type="hidden" name="actualtempIssueId" id="actualtempIssueId" value="${workOrderDetail.QS_Temp_Issue_Id}">
											 <input type="hidden" name="QS_Temp_Issue_Dtls_Id${indexnumber}" id="QS_Temp_Issue_Dtls_Id${indexnumber}" value="${workOrderDetail.QS_Temp_Issue_Dtls_Id}">
											<input type="hidden" name="actualWorkAreaID" value="${workOrderDetail.workAreaId}">
										</td>
										<td data-toggle="tooltip" title="${workOrderDetail.WO_MajorHead1}">
											<input type="hidden" name="actualwoMajorHead${indexnumber}" alue="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">
											<select id="combobox${indexnumber}" name="WO_MajorHead${indexnumber}" class="btn-tooltip btn-visibilty${indexnumber}" title="<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">				
											<option value="${workOrderDetail.woMajorHead}$<c:out value='${workOrderDetail.WO_MajorHead1}'></c:out>">${workOrderDetail.WO_MajorHead1}</option>
											<c:forEach items="${workMajorHead}" var="item">
												<c:if	test="${!(workOrderDetail.WO_MajorHead1==item.value)}">
														 <option value="${item.key}${item.value}"> ${item.value }</option>
												 </c:if>
											 </c:forEach>
											</select>
										</td>
										<td data-toggle="tooltip" title="${workOrderDetail.WO_MinorHead1}">
											<input type="hidden" name="actualWO_MinorHead${indexnumber}" value="${workOrderDetail.woMinorHeads}$${workOrderDetail.WO_MinorHead1}">
												<select name="WO_MinorHead${indexnumber}" id="comboboxsubProd${indexnumber}" class=" btn-tooltip form-control  btn-visibilty${indexnumber}">
												 <option value="${workOrderDetail.woMinorHeads}$<c:out value='${workOrderDetail.WO_MinorHead1}'></c:out>">${workOrderDetail.WO_MinorHead1}</option>
												 </select>
										</td>
										 <td data-toggle="tooltip" title="<c:out value='${workOrderDetail.WO_Desc1}' />">
											 <input type="hidden" name="actualWO_Desc${indexnumber}" id="actualWO_Desc${indexnumber}" value="${workOrderDetail.wODescription}$${workOrderDetail.WO_Desc1}">
											<select name="WO_Desc${indexnumber}" id="comboboxsubSubProd${indexnumber}" class="form-control btn-visibilty${indexnumber} childproduct">
												 <option value="${workOrderDetail.wODescription}$<c:out value='${workOrderDetail.WO_Desc1}'></c:out>">${workOrderDetail.WO_Desc1}</option>
											 </select>
										</td>
										<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
										<td>
										    <input type="text" class="form-control" name="woManualDesc${indexnumber}" id="woManualDesc${indexnumber}"
																	value="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"
																	title="${fn:replace(workOrderDetail.wOManualDescription, '@@', ' ')}"
																	onclick="showScopeOfWork(${indexnumber})" readonly="true" />
																	<!-- modal popup for scope of work start-->
											<div id="modalForScopeWork${indexnumber}" class="modal fade" role="dialog">
												<div class="modal-dialog" style="width: 90%;">
																			<!-- Modal content-->
													 <div class="modal-content">
														<div class="modal-header modalscopeheader">
															 <button type="button" class="close" data-dismiss="modal">&times;</button>
																 <h4 class="modal-title text-center">Scope Of Work</h4>
														</div>
														 <c:set var="scopeOfWorkParts" value="${fn:split(workOrderDetail.wOManualDescription, '@@')}" />
			
															<c:set var="indexOfScopeOfWork" value="1"></c:set>
																<div class="modal-body" style="overflow: hidden;">															
															
																	<div id="textboxDiv${indexnumber}">
																	<c:forEach var="scopeWork" items="${scopeOfWorkParts}">
																	<c:set var="indexOfScopeOfWork" value="${indexOfScopeOfWork+1}"></c:set>
																		<div class="col-md-12 mrg-btm">
																			<div class="form-group"  id="newtxtbox${indexOfScopeOfWork}">
																			<div class="col-md-11">
																				<input type="text" class="form-control scopeofworkid${indexnumber}" id="${indexnumber}defaultScopeOfWOrk${indexOfScopeOfWork}" value="<c:out value='${scopeWork}'></c:out>" name="ScopeOfWork${indexnumber}">
																			</div>
																			<div class="col-md-1"><button type="button" class="btn btn-danger Addscope_txt" onclick="deleteScopeOfWork(${indexOfScopeOfWork})"><i class="fa fa-close"></i></button></div>
																			</div>
																		</div>
																	</c:forEach>
																	</div>
																	
																	<div class="col-md-12 mrg-btm">
																		<div class="form-group">
																		<div class="col-md-11">
																			<input type="text" class="form-control scopeofworkid${indexnumber}" id="${indexnumber}defaultScopeOfWOrk1" name="ScopeOfWork${indexnumber}">
																		</div>
																		<div class="col-md-1"><button type="button" class="btn btn-success Addscope_txt" onclick="appendtextbox(${indexnumber})"><i class="fa fa-plus"></i></button></div>
																		</div>
																	</div>
																	 <input type="hidden" id="hiddenrownum">
																</div>
															
			
															 <div class="modal-footer">
																<div class="text-center center-block">
																<!-- 	 <button type="button" class="btn btn-warning">Submit</button> -->
																	 <button type="button" class="btn btn-warning" data-dismiss="modal">Submit</button>
																 </div>
															 </div>
													</div>
			
												</div>
											</div> <!-- modal popup for scope of work end -->
										</td>
										</c:if>
										<td data-toggle="tooltip" title="${workOrderDetail.unitsOfMeasurement1}">
											<input type="hidden" name="actualunitsOfMeasurement${indexnumber}" id="actualunitsOfMeasurement${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'/>">
											 <%-- <input type="text" name="UnitsOfMeasurement${indexnumber}"  id="UnitsOfMeasurementId${indexnumber}" value="${workOrderDetail.unitsOfMeasurement}$${workOrderDetail.unitsOfMeasurement1}"> --%>
											<select name="UnitsOfMeasurement${indexnumber}" id="UOMId${indexnumber}" class="form-control btn-visibilty${indexnumber}" onchange="loadWOWorkArea(this.value,${indexnumber});">
												 <option value="${workOrderDetail.unitsOfMeasurement}$<c:out value='${workOrderDetail.unitsOfMeasurement1}'/>">${workOrderDetail.unitsOfMeasurement1}</option>
											</select>
										</td>
										<td class="w-70" data-toggle="tooltip">															
										    <input type="hidden" name="actualQuantity${indexnumber}"  id="actualQuantity${indexnumber}"value="${workOrderDetail.quantity}">
											<input id="Quantity${indexnumber}" onblur="calCulateTotalAmout('qty',this.value,${indexnumber})"	name="Quantity${indexnumber}" value="${workOrderDetail.quantity}" class="form-control input-visibilty${indexnumber}" /> 
											<c:if test="${WorkOrderBean.typeOfWork ne TypeOfWork }">
												<span id="ClickHere${indexnumber}" style="">
												<a	href="javascript:myFunction(${workOrderDetail.QS_Temp_Issue_Dtls_Id},${workOrderDetail.totalAmount1},${indexnumber},'show')">Click Here</a><br /></span>
												<a href="javascript:void(0)"	style="display: none;"	onclick="showDetailsFunction(${indexnumber})" data-toggle="modal" data-target="#myModal-showwo-showquantity" id="showQty${indexnumber}">View Details </a>
												<a href="javascript:void(0)" style="cursor: pointer;display: none;" onclick="showWDGroupWiseMaterialDetails(${indexnumber})"  id="showMaterialQty${indexnumber}">Material Details</a>
											</c:if>
											<img src="images/spinner.gif" class="spinnercls" id="spinner${indexnumber}"></img>
											
															 
											<div id="appendActualWorkOrderArea${indexnumber}"></div>
														
											<div id="appendWorkOrderArea2${indexnumber}"></div>
												
											<div id="appendWorkOrderArea${indexnumber}"></div>
											<div id="appendWorkOrderWorkArea${indexnumber}"></div>
																
										</td>			
										 <c:if test="${WorkOrderBean.typeOfWork eq TypeOfWork }">
										 <td >
											<input type="hidden" id="actualAcceptedRate${indexnumber}" name="actualAcceptedRate${indexnumber}"	value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}">
											<input id="AcceptedRate1${indexnumber}"	onfocusout="calCulateTotalAmout('a_rate',this.value,${indexnumber})" name="AcceptedRate${indexnumber}" readonly="true"	class="form-control commmntstooltip input-visibilty${indexnumber}"	value="${workOrderDetail.totalAmount1/workOrderDetail.quantity}" />
										</td>
										</c:if>
										<td>
											<input type="hidden" id="actualTotalAmount${indexnumber}" name="actualTotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}"> 
											<input name="TotalAmount${indexnumber}" value="${workOrderDetail.totalAmount1}" id="TotalAmountId${indexnumber}" readonly="true" class="form-control" />
											<input type="hidden" name="labourAmount${indexnumber}" id="LaborAmountId${indexnumber}"  class="laborAmount"  value="${workOrderDetail.totalAmount1}">
											<input type="hidden" name="materialAmount${indexnumber}" id="MaterialAmountId${indexnumber}" class="materialAmount" >
											
											<input type="hidden" name="actuallabourAmount${indexnumber}" id="actualLaborAmountId${indexnumber}" value="${workOrderDetail.totalAmount1}">
											<input type="hidden" name="actualmaterialAmount${indexnumber}" id="actualMaterialAmountId${indexnumber}">
											
											<input type="hidden" name="WoRecordContains${indexnumber}" id="WoRecordContains${indexnumber}">
										</td>
										<td>
											<input type="hidden" name="actualComments${indexnumber}" value="<c:out value='${workOrderDetail.comments1}'></c:out>">
																
											<input id="Comments${indexnumber}" value="" placeholder="<c:out value='${workOrderDetail.comments1}'></c:out>" title="<c:out value='${workOrderDetail.comments1}'></c:out>"	name="comments${indexnumber}" readonly="true" class="form-control commmntstooltip input-visibilty${indexnumber}" />
											<input type="hidden" name="IsComments" value=""	id="hiddenCommentsId">
										</td>
										<td>
											<button type="button" name="addremoveItemBtn" id="addremoveItemBtnId${indexnumber}" class="btnaction" onclick="removeReviseWORow('${indexnumber}')">
											<i class="fa fa-remove"></i>
											</button>
											<button type="button" name="editItemBtn" value="Edit Item" id="editItem${indexnumber}" class="btnaction" onclick="editRow('${indexnumber}')">
											 <i class="fa fa-pencil"></i>
											</button>
											<c:if test="${workOrderCreationList.size() eq indexnumber}">
												<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId${indexnumber}" onclick="appendRow()" class="btnaction"><i class="fa fa-plus"></i>
												</button>
											</c:if>
										</td>
									</tr>
								</c:forEach>
								</tbody>
								</table>
							</div>
						</div>				
						<div class="col-md-6 Mrgtop20">
							<label class="control-label col-md-2">Note: </label>
							<div class="col-md-8">
								<input type="hidden" name="actualPurpose" value="${WorkOrderLevelPurpose}">
								<textarea name="Purpose" id="purpose" href="#" data-toggle="tooltip" title="${WorkOrderLevelPurpose}" placeholder="${WorkOrderLevelPurpose}" id="NoteId" class="form-control resize-vertical" autocomplete="off"></textarea>
							</div>
						</div>
						
						<div class="col-md-6 Mrgtop20">
								 <div class="col-md-6 fnt-18 text-left">Labor Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="laborWoAmountSpan">${WorkOrderBean.laborWoAmount}</span></div>
								 <div class="clearfix"></div>
								 <div class="col-md-6 fnt-18 text-left">Material Amount</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="materialWoAmountSpan">${WorkOrderBean.materialWoAmount}</span></div>
								 <div class="clearfix"></div>
								 <div class="col-md-6 fnt-18 text-left">Grand Total</div><div class="col-md-1 fnt-18">:</div><div class="col-md-5 fnt-18 text-right"><span id="WoGrandTotalAmount">${WorkOrderBean.laborWoAmount+WorkOrderBean.materialWoAmount}</span></div>
						</div>
						
						
				
									<!-- this is common file for showing images  -->
						<%@include file="ImgPdfCommonJsp.jsp"%>
		
		
						<!-- Common Code -->
						<%-- <%@ include file="CommonPopupCode.jsp" %> --%>
									<!-- -----------------------------------------------------------Bills Code--------------------------------------------------  -->
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
				<th >Group Name</th>
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
		
<!-- Modal -->
	<div id="modal-workorder-payments" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg" style="width: 90%">		
			<!-- Modal content-->
		<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h4 class="modal-title text-center">Work Order Payments</h4>
							</div>
								 <div class="modal-body">
									<div class="table-responsive">
										<table class="table table-bordered table-bold">
											<thead>
												<tr>
													<th>Payment Type</th>
												 	<th>ADV Amount</th>
													<th>RA Amount</th>
													<th>Paid Amount</th>
													<th>Bill Number</th>
													<th>Deducted Amount</th>
													<th>Recovery Deducted</th>
													<th>Security Deposit</th>
													<th>Petty Expenses</th>
													<th>Other Amount</th>
													<th>Entry Date</th>
													<th>Status</th>
												</tr>
											</thead>
											<tbody id="appendBillDetails">
											</tbody>
										</table>
									</div>
								</div>
										<div class="modal-footer">
											<div class="text-center center-block">
														<button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
											</div>
										</div>
						</div>		
						</div>
						</div>
									<!-- Modal -->
						<div id="modal-certificatepaymentra-click" class="modal fade" role="dialog">
							<div class="modal-dialog modal-lg modal-rabill-customwidth">				
										   <!-- Modal content-->
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal">&times;</button>
											<h4 class="modal-title text-center">Abstract</h4>
									</div>
									<div class="modal-body modal-body-scroll">
										<div class="table-responsive scroll-rabill-tbl2">
											<table class="table table-bordered" style="margin-bottom: -1px;">
												<!-- tbl-rabill -->
												<thead>
													<tr>
													<th rowspan="2" class="vertical-alignment" style="width: 31%; font-size: 15px; font-weight: bold;">Description of Work</th>
													<th rowspan="2" class="vertical-alignment" style="width: 14%; font-size: 15px; font-weight: bold">Total Qty</th>
													<th rowspan="2" class="vertical-alignment" style="width: 14%; font-size: 15px; font-weight: bold">Rate</th>
													<th rowspan="2" class="vertical-alignment" style="width: 14%; font-size: 15px; font-weight: bold">Unit</th>
													<th colspan="2" style="font-size: 15px; font-weight: bold">Cummulative Bill</th>
													</tr>
													<tr>
													<th	style="width: 15%; font-size: 15px; font-weight: bold; background-color: #ccc;">Qty</th>
													<th	style="width: 14%; font-size: 15px; font-weight: bold; background-color: #ccc;">Amount</th>	
													</tr>
												</thead>
											</table>
										</div>
										<div style="height: auto; overflow-y: scroll; max-height: 400px;">
												<table class="table table-bordered fixed-tbl-rabill">
														<tbody id="paymentByArea">
				
														</tbody>
												</table>
										</div>				
									</div>
									<div class="modal-footer">
										<div class="text-center center-block">
											<input type="button" name="abstractPrint" id="abstractPrintBtn" class="btn btn-warning" value="Print Abstract" />
											<button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
										</div>
									</div>
								</div>
							 </div>
						</div>
				
  <!--===========================================================Bills Code Completed==================================================  -->
		 <!-- modal popup for approve page start -->
				 <div id="myModal-showeorkorder" class="modal fade" role="dialog">
					 <div class="modal-dialog modal-lg">
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
				
												 <div class="col-md-12 remove-filed margin-close mrg-btm-10">
													 <div class="col-md-11 no-padding-right no-padding-left">
														 <%-- <input type="text" class="form-control workorder_modal_text"  title="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" value="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" name="termsAndCOnditions"  id="terms${TAC.indexNumber}" ><br> --%>
														 <input type="text" autocomplete="off" class="form-control" onkeyup="workordertermstitle(this)" title="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" value="<c:out value='${TAC.TERMS_CONDITION_DESC}' />" name="termsAndCOnditions" id="terms${TAC.indexNumber}">
													</div>
													<div class="col-md-1">
														<button type="button" class="btn btn-danger remove-button remove_field" id="newTC">
															 <i class="fa fa-remove "></i>
														 </button>
													 </div>
												 </div>
										</c:if>
									</c:forEach>
				
								</div>
								<div class="col-md-12">
									<div class="input_fields_wrap">
										<div class="col-md-12  margin-close padd-modal-workorder mrg-btm-10">
											<div class="form-group">
												<div class="col-md-11 no-padding-right no-padding-left">
													<input type="text"	class="form-control" name="termsAndCOnditions" id="workorder_modal_text1" />
												</div>
												<div class="col-md-1">
												<button type="button" class="btn btn-success plus-button add_field_button"> <i class="fa fa-plus "></i></button>
												</div>
											   </div>
											  </div>
											 </div>
											 <div class="col-md-11 margin-close">
												 <span class="spanheading"> (Optional)If 	you want to add CC In emails.</span>
												  <input type="text"  class="form-control mrg-t-10"	id="email-popup-workorder" name="optionalCCmails" value="${optionalCCmails}">
											 </div>
											 <!-- <div class="col-md-11 margin-close">
											 <span class="spanheading"> Subject</span>
											  <input type="text" class="form-control mrg-t-10" id="email-popup-workorder" placeholder="Please enter the subject">
										 </div> -->
								 </div>
						 </div>
								 <div class="modal-footer">
										<div class="col-md-12 text-center center-block">
											<%--  <c:if test="${isThisCorrectRevisedWorkOrder eq false }"> --%>
											<button type="button" class="btn btn-warning" data-dismiss="modal" onclick="reviseWORecords('SaveClicked')">Submit</button>
											<%-- </c:if> --%>

										</div>
								</div>
						</div>
				
					</div>
				</div>
				
				<!-- Modal Popup for approve page -->
				
				<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
					<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			</form:form>
			 <div class="pull-left"></div>
			<div class="col-md-12" style="margin-top: 30px;">
				<c:if test="${isThisCorrectRevisedWorkOrder ne false }">
					<button type="button" class="btn btn-warning" value="Draft Work Order" style="margin-bottom: 5px;" data-toggle="modal" onclick="return openModelPopupForRevise(this.value)" id="saveBtnId1">
						<i class="fa fa-floppy-o" aria-hidden="true"></i>&emsp;Draft Work Order
					</button>
					<input type="button" class="btn btn-warning" value="Revise" style="margin-bottom: 5px;" data-toggle="modal"   onclick="return openModelPopupForRevise(this.value)" id="saveBtnId">
					<input type="button" name="closeBtn" id="closeBtn" style="margin-bottom: 5px;" onclick="return closeView()" class="btn btn-warning btn-custom-width" id="closeBtn" value="Close" />
				</c:if>											
				<c:if test="${operType eq OperationType }">												
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
		<script src="js/WorkOrder/WOCommonCode.js"></script>
		<script>
			function closeView(){
				debugger;
				goBack() ;
			}
			function openModelPopupForRevise(buttonValue){
			  	debugger;
		         var valStatus = validateRowData();
		         if(valStatus == false) {
		             return false;
		         } 
			  	var woBillBilledAmount=parseFloat($("#woBillBilledAmount").val());
			  	var sumofTotalAmount=0;
			   	$(".reviseworkorderrowcls").each(function(){
					var currentId=$(this).attr("id").split("tr-class")[1];	
					sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
				});
			  	if(woBillBilledAmount>sumofTotalAmount){
			  		alert("can not revise work order it's seems your decresing wo amount that billed amount");
			  		return false;
			  	}
			  	
			  	 if(buttonValue!=undefined)
			    	 	$("#isSaveOrUpdateOperation").val(buttonValue);
						$("#myModal-showeorkorder").modal("show"); 
	    	   	  return true;
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
			});
		 </script>
		<script>
		
		var dispplayedRows=$("#dispplayedRows").val();
		var typeOfWork="${WorkOrderBean.typeOfWork}";
		///if(typeOfWork=="PIECEWORK"||typeOfWork=="MATERIAL"){
		if(typeOfWork!="NMR"){
			$("[name^=dispplayedRows]").each(function () {
				debugger;
	       	 	var rowNum=$(this).val();
	    	 	loadWOWorkArea("false",rowNum);	
	      		 });
		}else{
			 $(".overlay_ims").hide();	
		   	 $(".loader-ims").hide();
		}
		
		function loadSubProds(prodId, rowNum) {
			
			
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
		function myscopefocus(id){
			 $("#hiddenrownum").val(id); 
			 $("#modalForScopeWork"+id).modal();
		}

		function loadSubSubProducts(subProdId, rowNum) {
			 subProdId = subProdId.split("$")[0];
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
			}catch(e) {
				alert("Unable to connect to server!");
			}
	}
		
	function loadUnits(childProdId, rowNum) {
			childProdId = childProdId.split("$")[0];
			var url = "listOfWOmesurment.spring?childProductId="+childProdId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
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

		setTimeout(function(){
			
			loadScopeOfWork('ds',1);
				
			}, 200);
		
		//this method is used for load the total amount of NMR BOQ and already initiated BOQ Amount, based on type of work  ,
		//this method is used for both NMR work order and PIECE WORK work order 
		function loadScopeOfWork(childProdId, rowNum){
			
			
			childProdId = $("#comboboxsubSubProd"+rowNum).val().split("$")[0];
			var mesumentId="";//$("#UOMId"+rowNum).split("$")[0];
			var strData="";
			var WO_MinorHead=$("#comboboxsubProd"+rowNum).val().split("$")[0];
			
			var siteId=$("#site_id").val();
			var url = "loadScopeOfWork_AmountAndQty.spring?WO_MinorHead="+WO_MinorHead+"&childProductId="+childProdId+"&site_id="+siteId+"&mesumentId="+mesumentId+"&typeOfWork=${WorkOrderBean.typeOfWork}";
			  $.ajax({
				  url : url,
				  type : "get",
				contentType : "application/json",
				success : function(data) {
					
				console.log(data);
					var regExpr = /[^a-zA-Z0-9-. ]/g;
					debugger;
					var array=new Array();
					array=data.split("@@");
					
					data=data.split("@@")[0];
					
					var totalBOQAmount=array[3];
					var totalNMRWOInitiatedAmount=array[4];
					$("#TotalBOQAmount").val(totalBOQAmount);
					$("#TotalNMR_WO_initiatedAmount").val(totalNMRWOInitiatedAmount);
					
					if(rowNum==1){
						return;
					}
					 $("#scopeOfWork"+rowNum).val(data);
					 
					 //creating dynamic modal popup written byu thirupathi
						var popupmodaldyn="";
					 
					    popupmodaldyn+='<div id="modalForScopeWork'+rowNum+'" class="modal fade" role="dialog">';
					    popupmodaldyn+='<div class="modal-dialog" style="width:90%;">';
					    popupmodaldyn+='<div class="modal-content">';
					    popupmodaldyn+='<div class="modal-header modalscopeheader">';
					    popupmodaldyn+='<button type="button" class="close" data-dismiss="modal">&times;</button>';
					    popupmodaldyn+='<h4 class="modal-title text-center">Scope Of Work</h4>';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<div class="modal-body" style="overflow:hidden;">';
					    if(data){
						    popupmodaldyn+='<div class="col-md-12">';
						    popupmodaldyn+='<div class="form-group">';
						    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk2" name="ScopeOfWork'+rowNum+'" ></div>';//txt box
						    popupmodaldyn+='<div class="col-md-1"></div>'; //<button class="btn btn-success Addscope_txt" type="button" onclick="appendtextbox('+rowNum+')"><i class="fa fa-plus"></i></button>
						    popupmodaldyn+='</div>';
							popupmodaldyn+='</div>';
					    }
					    popupmodaldyn+='<div id="textboxDiv'+rowNum+'">';
					    popupmodaldyn+='</div>';
					    popupmodaldyn+='<div class="col-md-12">';
					    popupmodaldyn+='<div class="form-group">';
					    popupmodaldyn+='<div class="col-md-11"><input type="text" class="form-control txt-height scopeofworkid'+rowNum+'" id="'+rowNum+'defaultScopeOfWOrk1" name="ScopeOfWork'+rowNum+'"></div>';//txt box value="'+strData+'" title="'+strData+'"
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
		
				function appendtextbox(btn){
					
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
					 $("#textboxDiv"+btn).append('<div class="col-md-12" id="newtextId"><div class="form-group" id="newtxtbox'+appendtextid+'"><div class="col-md-11"><input type="text" name="ScopeOfWork'+btn+'"   id="'+btn+'defaultScopeOfWOrk'+appendtextid+'" class="form-control txt-height scopeofworkid'+btn+'" value="'+defaultSOW+'" title="'+defaultSOW+'"></div><div class="col-md-1"><button class="btn btn-danger" type="button" onclick="deleteScopeOfWork('+appendtextid+')"><i class="fa fa-close"></i></button></div></div></div>');  
				    serialNumber=1;
				    $("#"+btn+"defaultScopeOfWOrk1").val('');
				    $("#"+btn+"defaultScopeOfWOrk1").removeAttr('title');
				    $("#"+btn+"defaultScopeOfWOrk1").focus();
				    $("#"+btn+"defaultScopeOfWOrk1"+appendtextid).val(defaultSOW);
				}
		
				function submitScopeOfWork(rowNum){
					
					$("#modalForScopeWork"+rowNum).modal("hide");
					//	$("#modal-certificatepayment-adv").modal('hide');
				}
				function addDuplicateWorkAreaRow(workAreaId,rowNum,id){
					debugger;
							var workAreaForDB=$("#workAreaIdValueLABOR"+workAreaId).val()==""?0:$("#workAreaIdValueLABOR"+workAreaId).val();
							var dummyAreaDetailsPK=$("#"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==""?0:$("#"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
							
							var seletedArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
							var actualArea=parseFloat($("#LABOR"+workAreaId+"ACTUAL_AREA").text()==""?0:$("#LABOR"+workAreaId+"ACTUAL_AREA").text());
				            var availableArea=parseFloat($("#LABOR"+workAreaId+"WORK_AREA").text().trim());
				        	var initiated_area=parseFloat($("#LABOR"+workAreaId+"INITIATED_AREA").text()==""?0:$("#LABOR"+workAreaId+"INITIATED_AREA").text());
				            var wo_measurmen_name=$("#LABOR"+workAreaId+"WO_MEASURMEN_NAME").text();
				            var area_price_per_unit = parseFloat($("#LABOR"+workAreaId+"BOQ_RATE").val());
				            var accepted_rate=parseFloat($("#LABOR"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+workAreaId+"ACCEPTED_RATE").val());
				            var boq_no=$("#LABOR"+workAreaId+"BOQ_NO").val();
				         	var block_name=$("#LABOR"+workAreaId+"BLOCK_NAME").text().trim();
				            var floor_name=$("#LABOR"+workAreaId+"FLOOR_NAME").text().trim();
				            var flat_id=$("#LABOR"+workAreaId+"FLAT_ID").text().trim();
				            var RECORD_TYPE=$("#LABOR"+workAreaId+"RECORD_TYPE").text().trim();
				            var Contains=$("#LABOR"+workAreaId+"Contains").val();
				            var ContainsSpecailChar = Contains.includes("@@");
				            var laborWorkAreaId=$("#LABOR"+workAreaId+"workAreaID").val();
				            if(ContainsSpecailChar==true){    
				            	//variables for material values
					            var seletedArea1=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val());
								var actualArea1=parseFloat($("#MATERIAL"+workAreaId+"ACTUAL_AREA").text()==""?0:$("#MATERIAL"+workAreaId+"ACTUAL_AREA").text());
					            var availableArea1=parseFloat($("#MATERIAL"+workAreaId+"WORK_AREA").text().trim());
					        	var initiated_area1=parseFloat($("#MATERIAL"+workAreaId+"INITIATED_AREA").text()==""?0:$("#MATERIAL"+workAreaId+"INITIATED_AREA").text());
					            var wo_measurmen_name1=$("#MATERIAL"+workAreaId+"WO_MEASURMEN_NAME").text();
					            var area_price_per_unit1 =parseFloat($("#MATERIAL"+workAreaId+"BOQ_RATE").val());
					            var accepted_rate1=parseFloat($("#MATERIAL"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#MATERIAL"+workAreaId+"ACCEPTED_RATE").val());
					            var block_name1=$("#MATERIAL"+workAreaId+"BLOCK_NAME").text().trim();
					            var floor_name1=$("#MATERIAL"+workAreaId+"FLOOR_NAME").text().trim();
					            var flat_id1=$("#MATERIAL"+workAreaId+"FLAT_ID").text().trim();
					            var RECORD_TYPE1=$("#MATERIAL"+workAreaId+"RECORD_TYPE").text().trim();
					            var materailWorkAreaId=$("#MATERIAL"+workAreaId+"workAreaID").val();
					            initiated_area1=initiated_area1+seletedArea1;
					            var selectedArea1=0
				            }
				            
				           
				            var recordName=workAreaForDB.replace('LABOR', '');
				            if(seletedArea<=0||seletedArea==0){
				            	alert("Please enter correct value for area");
				            	return false;
				            }
				            initiated_area=initiated_area+seletedArea;
				            
				            var selectedArea=0
				           
				            $("[name^="+recordName+"]").each(function () {
				        		
				        		var recorType=$(this).attr("id").split(recordName)[0];
				        		if(recorType=="LABOR"){
					        		selectedArea+=parseFloat($(this).val());
				        		}else{
				        			selectedArea1+=parseFloat($(this).val());
				        		}
				        	});
				            //first time don't minus the availble area bcoz it's already deducted from qty
				         /*    if(id!=1){ */
				            	availableArea=(availableArea-seletedArea).toFixed(2);
				            	availableArea1=(availableArea1-seletedArea1).toFixed(2);
						/* 	}  */
				            if(availableArea<0||availableArea==0){
				            	alert("you can't create a new row because avalible area is zero.");
				            	return false;
				            }
				            dummyAreaDetailsPK=dummyAreaDetailsPK+1;
				            debugger;
				            //if(id!=1)
				          //workAreaId=workAreaId+id;
				          id=parseInt(workAreaId.split(recordName)[1])+1;
				          var idForMaterial=workAreaForDB.replace("LABOR", "MATERIAL");
				          
						 var str="<tr  id='"+workAreaForDB+id+"Row' class='"+workAreaForDB+"'>";
						 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;cursor:pointer' class='workareatr' id='"+workAreaForDB+id+"val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value,"+rowNum+")' value='"+recordName+id+"'></td> ";
						 str+="<td style=''  id='"+workAreaForDB+id+"BLOCK_NAME'>"+block_name+"<input type='hidden' id='"+workAreaForDB+id+"workAreaID' value='"+laborWorkAreaId+"'><input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> ";
						 str+="<td style='' id='"+workAreaForDB+id+"FLOOR_NAME'> "+(floor_name)+"<input type='hidden' id='"+workAreaForDB+id+"QS_WO_TEMP_ISSUE_DTLS_ID' value='"+dummyAreaDetailsPK+"'></td>";
						 str+="<td style=''  id='"+workAreaForDB+id+"FLAT_ID' >"+(flat_id)+"<input type='hidden' id='workAreaIdValue"+workAreaForDB+id+"' value='"+workAreaForDB+"'>   </td>";
						 str+="<td style='' id='"+workAreaForDB+id+"ACTUAL_AREA' >"+actualArea.toFixed(2)+" </td>";
						 str+= "<td style='' id='"+workAreaForDB+id+"WORK_AREA' >"+(availableArea)+" </td>";						
						 str+="<td  style='' id='"+workAreaForDB+id+"WO_MEASURMEN_NAME'>"+(wo_measurmen_name)+"</td>";
						 str+="<td  style='' id='"+workAreaForDB+id+"RECORD_TYPE'>"+RECORD_TYPE+"<input type='hidden' value='"+Contains+"' id='"+workAreaForDB+id+"Contains'></td>";
						 str+="<td style='' id='"+workAreaForDB+id+"PAYMENTDONE' >0.00</td>";  
						 str+="<td style=''> <input type='text' class='form-control pastenotallowed allocateArea"+rowNum+"' style='width: 100%;' id='"+workAreaForDB+id+"ALLOCATE_AREA' name='"+recordName+"' value='0' onkeyup='validateBBQ(\""+workAreaForDB+id+"\", \""+recordName+"\")' onkeypress='return isNumberCheck(this, event)'> <input type='hidden' class='allocateAreaHidden' id='"+workAreaForDB+id+"ALLOCATE_AREAHIDDEN'></td>";
						 str+="<td style=''> <input type='text' class='form-control pastenotallowed' style='width: 100%;' id='"+workAreaForDB+id+"BOQ_RATE' value='"+area_price_per_unit.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
					     str+="<td style='' > <input type='text' class='form-control pastenotallowed acceptedRate"+rowNum+"' style='width: 100%;' id='"+workAreaForDB+id+"ACCEPTED_RATE' value='0' onkeyup='validateBBQ(\""+workAreaForDB+id+"\", \""+recordName+"\")' onkeypress='return isNumberCheck(this, event)'><input type='hidden' class='acceptedRateHidden' id='"+workAreaForDB+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaForDB+"' id='hiddenid"+workAreaForDB+id+"'></td>";
						 str+="<td style=''><button type='button' class='btnaction'  id='"+workAreaForDB+id+"addbtn' onclick='addDuplicateWorkAreaRow(\""+recordName+id+"\","+(rowNum)+","+(id)+")'><i class='fa fa-plus'></i></button></td>";
						 str+="</tr>";
						 if(ContainsSpecailChar==true){ 
							 str+="<tr  id='"+idForMaterial+id+"Row' class='"+workAreaForDB+"'>";
							 str+="<td  style='' class='text-center'><input type='checkbox' style='width:100%;height:16px;display:none;' class='workareatr' id='"+idForMaterial+id+"val' name='chk1"+rowNum+"' onclick='validateWorkAreaVal(this.value,"+rowNum+")' value='"+recordName+id+"'></td> ";
							 str+="<td style=''  id='"+idForMaterial+id+"BLOCK_NAME'>"+block_name1+"<input type='hidden' id='"+idForMaterial+id+"workAreaID' value='"+materailWorkAreaId+"'><input type='hidden' id='currentRowNum' name='currentRowNum' value='"+rowNum+"'> </td> ";
							 str+="<td style='' id='"+idForMaterial+id+"FLOOR_NAME'> "+(floor_name1)+"<input type='hidden' id='"+idForMaterial+id+"QS_WO_TEMP_ISSUE_DTLS_ID' value='"+dummyAreaDetailsPK+"'></td>";
							 str+="<td style=''  id='"+idForMaterial+id+"FLAT_ID' >"+(flat_id1)+"<input type='hidden' id='workAreaIdValue"+idForMaterial+id+"' value='"+workAreaForDB+"'>   </td>";
							 str+="<td style='' id='"+idForMaterial+id+"ACTUAL_AREA' > "+actualArea1.toFixed(2)+" </td>";
							 str+= "<td style='' id='"+idForMaterial+id+"WORK_AREA' > "+(availableArea1)+" </td>";						
							 str+="<td  style='' id='"+idForMaterial+id+"WO_MEASURMEN_NAME'>"+(wo_measurmen_name1)+"</td>";
							 str+="<td  style='' id='"+idForMaterial+id+"RECORD_TYPE'>"+RECORD_TYPE1+"</td>";
							 str+="<td style='' id='"+idForMaterial+id+"PAYMENTDONE' >0.00</td>";  
							 str+="<td style=''> <input type='text' class='form-control pastenotallowed allocateArea"+rowNum+"' style='width: 100%;' id='"+idForMaterial+id+"ALLOCATE_AREA' name='"+recordName+"' value='0'  readonly='readonly' > <input type='hidden' class='allocateAreaHidden' id='"+idForMaterial+id+"ALLOCATE_AREAHIDDEN'></td>";
							 str+="<td style=''> <input type='text' class='form-control pastenotallowed' style='width: 100%;' id='"+idForMaterial+id+"BOQ_RATE' value='"+area_price_per_unit1.toFixed(2)+"' readonly='readonly'  style=' width: 66px;'> </td>";
						     str+="<td style='' > <input type='text' class='form-control pastenotallowed acceptedRate"+rowNum+"' style='width: 100%;' id='"+idForMaterial+id+"ACCEPTED_RATE' value='"+area_price_per_unit1.toFixed(2)+"' readonly='readonly' ><input type='hidden' class='acceptedRateHidden' id='"+idForMaterial+id+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+workAreaForDB+"' id='hiddenid"+idForMaterial+id+"'></td>";
							 str+="<td></td>";
							 str+="</tr>";	
							 
							 $("#MATERIAL"+workAreaId+"Row").after(str);
						 }else{
							 $("#LABOR"+workAreaId+"Row").after(str);
						 }
				 
					 $("#LABOR"+workAreaId+"addbtn").hide();
					 $(".pastenotallowed").bind('paste', function (e) {//here preventing to enter alphabets
						e.preventDefault();
					});
							
				}
							
				function validateBBQ(id,workAreaGroupId){
					
					var hiddenId=$("#hiddenid"+id).val();	
					var BOQ_RATEID=id+"BOQ_RATE";
					var ACCEPTED_RATEID=id+"ACCEPTED_RATE";
					var BOQ_RATEVal=parseFloat($("#"+BOQ_RATEID).val());
					var ACCEPTED_RATEVal=parseFloat($("#"+ACCEPTED_RATEID).val());
					var WoID=$("#workAreaIdValue"+id).val();
					
					var AllocateArea=parseFloat($("#"+id+"ALLOCATE_AREA").val()==""?0:$("#"+id+"ALLOCATE_AREA").val());
					var AvailableArea=parseFloat($("#"+id+"WORK_AREA").text()==""?0:$("#"+id+"WORK_AREA").text());	
					var AllocatedArea=parseFloat($("#"+id+"ACTUAL_AREA").text());	
					var currentSplitId=id.split(hiddenId)[1];
					
					var idforName=hiddenId.replace("LABOR", '');
					 $("[name^="+idforName+"]").each(function () {
					   	    var splitid=$(this).attr("id").split(idforName)[1].split("ALLOCATE_AREA")[0];
					   	    var workAreaRowId=$(this).attr("id");
					   	   	if(splitid>currentSplitId){
					   	   		$("#"+$(this).attr("id")).val('0');					   	   		
					   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0');
					   			//$("#" + $(this).attr("id").split("ALLOCATE_AREA")[0] + "val").prop("checked", true);
					   	   	   // $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text('0');
					   	   	}
					   	});
					 
					   //material formula 
					    var idForMaterial=id.replace("LABOR", "MATERIAL");
			            var materialAvbleQty=$("#"+idForMaterial+"WORK_AREA").text();
			        	var AllocateArea=parseFloat($("#"+id+"ALLOCATE_AREA").val()==''?0:$("#"+id+"ALLOCATE_AREA").val());
			        	var AvailableArea=parseFloat($("#"+id+"WORK_AREA").text()==''?0:$("#"+id+"WORK_AREA").text());
			        	var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			        	$("#"+idForMaterial+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
			        	
					    var selectedArea=0
					    var WORK_AREA=0;
			            $("[name^="+idforName+"]").each(function () {
			        		
			        		selectedArea+=parseFloat($(this).val());
			        		var splitid=$(this).attr("id").split(idforName)[1].split("ALLOCATE_AREA")[0];
			           	   	if(splitid>currentSplitId){
			           	   		$("#"+$(this).attr("id")).val('0');
			           	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((AvailableArea-AllocateArea).toFixed(2));
			           	   	if(AvailableArea<AllocateArea){
			           			alert("You can't initiate more than available area.");
			           			$("#"+$(this).attr("id")).val('0');
					   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0'); 
			        	   	   /*  $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"INITIATED_AREA").text('0'); */
			           			$("#"+hiddenId+"ALLOCATE_AREA").focus();
			           			return false;
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
						$("#"+id+"ALLOCATE_AREA").val("0");
						$("#"+id+"ALLOCATE_AREA").focus();
					}			
					//material formula 
				    var idForMaterial=id.replace("LABOR", "MATERIAL");
		            var materialAvbleQty=$("#"+idForMaterial+"WORK_AREA").text();
		        	var AllocateArea=parseFloat($("#"+id+"ALLOCATE_AREA").val()==''?0:$("#"+id+"ALLOCATE_AREA").val());
		        	var AvailableArea=parseFloat($("#"+id+"WORK_AREA").text()==''?0:$("#"+id+"WORK_AREA").text());
		        	var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
		        	$("#"+idForMaterial+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
					
				}
				//method for validating work area
				function validateWorkArea(val,workAreaId,workAreaGroupId){
					debugger;
					var hiddenId=$("#hiddenid"+workAreaId).val();	
					//workAreaId=$("#actualWorkAreaId"+row).val();ACTUAL_AREA
					var recordTypeForId=hiddenId.split(workAreaGroupId)[0];
					var allocatedArea = parseFloat($("#" + workAreaId + "ALLOCATE_AREA").val()==""?0:$("#" + workAreaId + "ALLOCATE_AREA").val());
					var ACTUAL_AREA = parseFloat($("#" + workAreaId + "ACTUAL_AREA").text());
					var availbleArea= parseFloat($("#" + workAreaId + "WORK_AREA").text());
					var paymentdone= parseFloat($("#" + workAreaId + "PAYMENTDONE").text());
					var actualAreaAlocatedQTY =	parseFloat($("#" + workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#" + workAreaId + "ACTUAL_ALLOCATE_AREA").val());
					var materialActualAreaAlocatedQTY =	parseFloat($("#MATERIAL" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val());
					var actualAreaAlocatedQTYPrice =parseFloat($("#" + workAreaId + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#" + workAreaId + "ACTUAL_ACCEPTED_RATE").val());
					
					var WoRkOrIdForMaterial=workAreaId.replace("LABOR", "MATERIAL");					
					var ACTUAL_AREA1 = parseFloat($("#" + WoRkOrIdForMaterial + "ACTUAL_AREA").text());					
					var paymentdone1= parseFloat($("#" + WoRkOrIdForMaterial + "PAYMENTDONE").text());
					var actualAreaAlocatedQTY1 =	parseFloat($("#" + WoRkOrIdForMaterial + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#" + WoRkOrIdForMaterial + "ACTUAL_ALLOCATE_AREA").val());
					var actualAreaAlocatedQTYPrice1 =parseFloat($("#" + WoRkOrIdForMaterial + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#" + WoRkOrIdForMaterial + "ACTUAL_ACCEPTED_RATE").val());
					
					if(!isNum(val) && val!==''){
						alert("Please Enter Only digit's");
					
						$("#"+workAreaId+"ALLOCATE_AREA").focus();
						$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
		            	return false;
					}
					
					if(allocatedArea>availbleArea){
		            	alert("you can't allocate area more than of availbale area.");		            	
		            		$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
		            		allocatedArea=actualAreaAlocatedQTY;
		            }
					if(allocatedArea<paymentdone){
						alert("You can not reduce  the work area, payment already initiated for this area");
		            	$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
		            	allocatedArea=actualAreaAlocatedQTY;
		            	//$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val(materialActualAreaAlocatedQTY);
		            	//return false;
					}
				
					var currentSplitId=workAreaId.split(hiddenId)[1];
					
					 $("[name^="+hiddenId+"]").each(function () {
				   	    var splitid=$(this).attr("id").split(hiddenId)[1].split("ALLOCATE_AREA")[0];
				   	    var workAreaRowId=$(this).attr("id");
				   	   	if(splitid>currentSplitId){
				   	   	
				   	   	    $("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text('0');
				   	   	    var paymentDone=$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"PAYMENTDONE").text();
				   	   	    if(paymentDone=='0'||paymentDone=='0.00'){
				   	   	    	$("#" + $(this).attr("id").split("ALLOCATE_AREA")[0] + "val").prop("checked", false);
				   	   			$("#"+$(this).attr("id")).val('0');
				   	   	    }
				   	   	}
				   	});
					
					var materialAvbleQty=$("#"+WoRkOrIdForMaterial+"WORK_AREA").text();
			        var AllocateArea=parseFloat($("#"+workAreaId+"ALLOCATE_AREA").val()==''?0:$("#"+workAreaId+"ALLOCATE_AREA").val());
			        var AvailableArea=parseFloat($("#"+workAreaId+"WORK_AREA").text()==''?0:$("#"+workAreaId+"WORK_AREA").text());
			        var materialQtyFormula=parseFloat((materialAvbleQty*AllocateArea)/AvailableArea);
			        $("#"+WoRkOrIdForMaterial+"ALLOCATE_AREA").val(materialQtyFormula.toFixed(3));
			        
			        var availbleArea1= parseFloat($("#" + WoRkOrIdForMaterial + "WORK_AREA").text());
			        var allocatedArea1 = parseFloat($("#" + WoRkOrIdForMaterial + "ALLOCATE_AREA").val()==""?0:$("#" + WoRkOrIdForMaterial + "ALLOCATE_AREA").val());
			        
				    var selectedArea=0
				    var WORK_AREA=0;
				    var prevValue=0;
		            $("[name^="+hiddenId+"]").each(function () {
		        		
		        		selectedArea=parseFloat($(this).val());
		        		var splitid=$(this).attr("id").split(hiddenId)[1].split("ALLOCATE_AREA")[0];
		        		//var actualAreaAlocatedQTY =	parseFloat($("#" +hiddenId+splitid + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#" + hiddenId+splitid + "ACTUAL_ALLOCATE_AREA").val());
		        		var workAreaRowId=$(this).attr("id");
		        		var recordType=workAreaRowId.split(hiddenId)[0];
		           	    WORK_AREA=parseFloat($("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text());
		           	   	if(splitid>currentSplitId){
		           	 	
		          	 	 var paymentDone=$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"PAYMENTDONE").text();
			   	   	     if(paymentDone=='0'||paymentDone=='0.00')
		           	   		$("#"+$(this).attr("id")).val('0');
			   	   	        if(recordType=="LABOR"){
			   	   	    		$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0]+"WORK_AREA").text((availbleArea-allocatedArea).toFixed(2));	
			   	   	        }else{
			   	   	      		$("#"+$(this).attr("id").split("ALLOCATE_AREA")[0].replace("LABOR", "MATERIAL")+"WORK_AREA").text((availbleArea1-allocatedArea1).toFixed(2));	
			   	   	        }						   		           	   	   
		           	   	} 
		           	 prevValue=selectedArea;
		           	}); 
				 
					
					var totalArea=availbleArea+actualAreaAlocatedQTY;
					
					if(allocatedArea<paymentdone){
						alert("You can not reduce  the work area, payment already initiated for this area");
		            	$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
		            	//$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val(materialActualAreaAlocatedQTY);
		            	return false;
					}
				
					  if(allocatedArea>totalArea){
			            	alert("you can't allocate area more than of availbale area.");
			            	if($("#"+workAreaId+"WORK_AREA").val()=='' || $("#"+workAreaId+"WORK_AREA").val()=='0'){
			            		$("#"+workAreaId+"ALLOCATE_AREA").val('0');
			            		return false;
			            	}else{
			            		$("#"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
			            	}
			            }           
					   
			            
		    	}
				var lengthOfDisplayedRows=$("[name^=dispplayedRows]").length;	
		function loadWOWorkArea(UOMId, row) {
				debugger;
			
			var checkduplicate=checkingDuplicateRow(row);
		     if(checkduplicate=="false"){
		    	 $("#UOMId"+row).val('');
		    	 return false;
		     }
		     
			var popupmodaldyn="";
		    
		    var loadNewData="";
		    if(UOMId!="false"){
		    	loadNewData=true;
		    }
			/*  jQuery.noConflict();*/
			 $('#Modal-create-wo-popup').modal('hide'); 
		    // $("#TotalAmountId"+row).val('0');
				var appendId=$("#allocatedareaAppend").val();
				var mesurmentId="";
				var UOMId="";
			
				     
				
				 //$("#appendWorkOrderArea"+row).html("");
				$("#QuantityId"+row).val("");
			
				 mesurmentId= $("#WO_Desc"+row).val();
				 UOMId=$("#UnitsOfMeasurement"+row).val();
				 
				 mesurmentId=$("#comboboxsubSubProd"+row).val().split("$")[0];
				 UOMId=$("#UOMId"+row).val().split("$")[0];
				 
				 
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
//			<!-- modal popup for create work order table for show start -->
			//<!-- Modal -->
			popupmodaldyn+=' <div id="Modal-create-wo-popup'+row+'" class="modal fade" role="dialog">';
				popupmodaldyn+=' <div class="modal-dialog modal-lg" style="width:95%;">';
			
			    /*  Modal content onclick="return addWorkArea('+row+', 1)" */
			    popupmodaldyn+='<div class="modal-content">';
			    popupmodaldyn+='<div class="modal-header">';
			    popupmodaldyn+='<button type="button" class="close"  onclick="closeWorkAreaPopup('+row+')" >&times;</button>';
			    popupmodaldyn+='<h4 class="modal-title text-center">Work Area </h4>';
			    popupmodaldyn+='</div>';
			    popupmodaldyn+='<div class="modal-body">';
			    popupmodaldyn+='<img src="images/spinner.gif" class="spinnerclsModal" id="loaderModal'+row+'"></img>';
			    popupmodaldyn+='<div class="table-responsive">';
			    popupmodaldyn+='<table class="table" id="floortable" style="">';
			    popupmodaldyn+='<thead>';
			    popupmodaldyn+='<tr>';
			    popupmodaldyn+='<th class="text-center"><input type="checkbox" id="checkAll'+row+'" style="width:100%;height:16px;cursor:pointer;" value="'+row+'" onclick="checkAllCheckBox('+row+')"></th>';
			    popupmodaldyn+='<th style="" >Block</th>';
			    popupmodaldyn+='<th style="">Floor</th>';
			    popupmodaldyn+='<th style="" >Flat</th>';
			    popupmodaldyn+='<th style="" >Actual Area</th>';
			    popupmodaldyn+='<th style="" >Available Area</th>';
/* 			    popupmodaldyn+='<th style="" >Initiated Area</th>'; */
			    popupmodaldyn+='<th style="" >Measurement</th>';
			    popupmodaldyn+='<th style="" >Record Type</th>';
			    popupmodaldyn+='<th style="" >Payment Initiated Area</th>';
			    popupmodaldyn+='<th style="" >Allocate Area</th>';
			    popupmodaldyn+='<th style="">BOQ Rate(<%=acceptedRateCurrency %>)</th>';
			    popupmodaldyn+='<th style="">Accepted Rate(<%=acceptedRateCurrency %>)</th>';
			    popupmodaldyn+='<th style="">Actions</th>';
			    popupmodaldyn+='</thead>';
			    popupmodaldyn+='<tbody id="areaMapping'+row+'" class="fixed-table-body">';
			    popupmodaldyn+='<div class="loader" id="loderId'+row+'" style="display:none;"></div>';
		    	popupmodaldyn+='</tbody>';
			    popupmodaldyn+='</table>';
			    popupmodaldyn+='</div> ';
		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='<div class="modal-footer">';
		    	popupmodaldyn+='<div class="text-center center-block" id="submitButtonWorkArea">';
		    	popupmodaldyn+='<button type="button" id="addWorkAreaBtn'+row+'" class="btn btn-warning" onclick="return addWorkArea('+row+',\'validateLabor\')">Submit</button>';/*  */
		   		popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';

		    	popupmodaldyn+='</div>';
		    	popupmodaldyn+='</div>';
		     	//<!-- modal popup for create work order table for show end -->
			  $("#appendWorkOrderWorkArea"+row).html(popupmodaldyn);
			  $("#loderId"+row).show();
				
				 $("#appendWorkOrderArea"+row).html("");
				 var site_id=$("#site_id").val();
				 var workOrderNo=$("#workOrderId").val();
				 var workOrderID=$("#QS_Temp_Issue_Dtls_Id"+row).val();
				 var boqNo=$("#boqNo").val();
				 var workOrderTypeOfWork=$("#typeOfWork").val();
				// $('#Modal-create-wo-popup'+row).modal('show'); 
				
							var url = "loadWOAreaForRevise.spring";
							$.ajax({
								url : url,
								type : "get",
								data:{
									//acceptedRate:acceptedRate,
									workOrderID:workOrderID,
									mesurmentId:mesurmentId,
									UnitsOfMeasurementId:UOMId,
									site_id:site_id,
									operType:"${operType}"	,
									workOrderNo:workOrderNo,
									boqNo:boqNo,
									loadNewData:loadNewData,
									typeOfWork:workOrderTypeOfWork
									},
				
								success : function(data) {
									  var materialAmount=0;
									   var totalAreaQty=0;
									   var rowNum=row;	  
									   var id=1;
									   var duplicateWorkAreaId=0;
									   var workDescriptionid="";
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
												//pravArea+=tempQtyAndRate[0];
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
											pravArea=raPraviousArea.toFixed(2);
										//} 
									
									// checking here is there any duplicate work group area id is there are not if there hold the duplicate work group area id
									
									if(value.WO_WORK_AREA_GROUP_ID!=workDescriptionid){
										//if got new work group area id show the (plus) previous row (Plus) button
										$("#LABOR"+workDescriptionid+duplicateWorkAreaId+"addbtn").show();
										workDescriptionid=value.WO_WORK_AREA_GROUP_ID;
										duplicateWorkAreaId=1;	
									}else{
										//if got duplicate work group area id hide the (plus) button
										$("#"+value.RECORD_TYPE+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn").hide();
										if(workOrderTypeOfWork=='MATERIAL'){
											$("#LABOR"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn").css({'display':'none'});
											//alert($("#LABOR"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn").val());
											//alert($("#LABOR"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn").attr("id"));
										}
										duplicateWorkAreaId=duplicateWorkAreaId+1;
									}
								
								    var area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0":value.QS_AREA_PRICE_PER_UNIT;
									//var recordType=value.RECORD_TYPE=="MATERIAL"?"true":false;
									var recordTypeForReadonly=value.RECORD_TYPE=='MATERIAL'?'readonly':'';
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
									//for material we are reducing id bcz both labor and material should have unique id
									if(workOrderTypeOfWork=='MATERIAL'){
										recordTypeForReadonly="";
										//we are changing rtypeForId to "LABOR" becoz we can use LABOR Validation to Materail Work Order
										//so need to change Id of the text box
										rtypeForId='LABOR';
									}
												
									if(value.PRICE==undefined){
										if(value.BLOCK_NAME!=undefined&&value.WO_WORK_AREA_ID!=undefined){
											var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;	
											var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
											var flatname=value.FLATNAME==null?"-":value.FLATNAME;
											flatname=flatname==0?"-":flatname;
											var isValueZero=value.WO_WORK_AVAILABE_AREA;
											str+="<tr  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Row' class='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'>"
											if(rtypeForId=='MATERIAL'){
												str+="<td  class='text-center'><input type='checkbox'  style='display:none;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td>"
											}else{
												str+="<td  class='text-center'><input type='checkbox'  style='width: 100%;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"' onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td>"
											}
									      	 	 
											str+="<td id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BLOCK_NAME'>"+value.BLOCK_NAME+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"workAreaID' value='"+value.WO_WORK_AREA_ID+"'></td> ";
											str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLOOR_NAME'> "+floorName+"<input type='hidden' id='workAreaIdRecordType"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.RECORD_TYPE+"'></td>";
											str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLAT_ID'>"+(flatname)+"<input type='hidden' id='workAreaIdValue"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'> </td>";
											str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_AREA' >"+wo_work_area.toFixed(2)+"</td>";
											str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WORK_AREA' >"+value.WO_WORK_AVAILABE_AREA.toFixed(2)+"</td>";
											str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WO_MEASURMEN_NAME'>"+value.WO_MEASURMEN_NAME+"</td>";
										    str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"RECORD_TYPE'>"+value.RECORD_TYPE+" <input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Contains'></td>"; //"+area_price_per_unit+"
										         
										   	str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"PAYMENTDONE'>"+pravArea+"</td>";
										    if(isValueZero==0){
										    	str+= "<td style=''><input type='text'  style='width:100%;'  class='form-control copyPasteRestricted "+value.WO_WORK_AREA_GROUP_ID+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREA' value='0'   name='"+value.WO_WORK_AREA_GROUP_ID+"'  onkeyup='validateBBQ(\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")' onkeypress='return isNumberCheck(this, event)' readonly>  <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' id='hiddenid"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'> </td>";
										    }else{
										  		str+= "<td style=''><input type='text'  style='width:100%;'  class='form-control copyPasteRestricted "+value.WO_WORK_AREA_GROUP_ID+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREA' value='0'   name='"+value.WO_WORK_AREA_GROUP_ID+"'   onkeyup='validateBBQ(\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")' onkeypress='return isNumberCheck(this, event)'  "+recordTypeForReadonly+">  <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' id='hiddenid"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'> </td>";	
										    }
										    str+="<td style=''><input type='text' class='form-control' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BOQ_RATE' value='"+area_price_per_unit.toFixed(2)+"' readonly='readonly'  style='width: 66px;'> </td>";
										    
										    if((isValueZero==0||value.RECORD_TYPE=="MATERIAL")&&workOrderTypeOfWork!='MATERIAL'){
										        str+="<td style='' > <input type='text' style='width:100%;' class='form-control copyPasteRestricted' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+area_price_per_unit.toFixed(2)+"'  onkeyup='validateBBQ(\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")'  onkeypress='return isNumberCheck(this, event)' readonly> </td>";
										    }else{
										       str+="<td style='' > <input type='text' style='width:100%;' class='form-control copyPasteRestricted' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='0.00'  onkeyup='validateBBQ(\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")'  onkeypress='return isNumberCheck(this, event)'><input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' id='hiddenid"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'> </td>";
										    }
										      if(value.RECORD_TYPE!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
										      	str+="<td style=''><button type='button' class='btnaction' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn' onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+",2)'><i class='fa fa-plus'></i></button></td>";
										      }else{
										    	str+="<td style=''></td>"; 
										      }
										    str+="</tr>";
										
										}
										
								}
								//alert(value.WO_CONTAINS);
								if(value.PRICE!=undefined&&value.PRICE!=""){
									var wo_work_area=value.WO_WORK_AREA==null?"0":value.WO_WORK_AREA;
									var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
									var flatName=value.FLATNAME==null?"-":value.FLATNAME;
									flatName=flatName==0?"-0":flatName;
									area_price_per_unit=value.QS_AREA_PRICE_PER_UNIT==null?"0.00":value.QS_AREA_PRICE_PER_UNIT;
									str+="<tr   id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Row' class='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'>"
									if(rtypeForId=='MATERIAL'){
										str+=" <td  class='text-center'><input type='checkbox' checked='checked' style='display:none;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"'  onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td> "
									}else{
										str+=" <td  class='text-center'><input type='checkbox' checked='checked' style='width: 100%;height:16px;cursor: pointer;' class='workareatr' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"val' name='chk1"+row+"'  onclick='validateWorkAreaVal(this.value,"+row+");' value='"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'></td> "
									}										
									str+=" <td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BLOCK_NAME' >   "+value.BLOCK_NAME+"<input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"workAreaID' value='"+value.WO_WORK_AREA_ID+"'></td> ";
									str+=" <td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLOOR_NAME'> "+floorName+"<input type='hidden' id='workAreaIdRecordType"+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+value.RECORD_TYPE+"'><input type='hidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>";
									str+=" <td  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"FLAT_ID' > "+flatName+"<input type='hidden' id='workAreaIdValue"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"' value='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+"'> </td>";
									str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_AREA' >"+wo_work_area.toFixed(2)+" </td>";
									str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WORK_AREA' >"+value.WO_WORK_AVAILABE_AREA.toFixed(2)+" </td>";
									str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"WO_MEASURMEN_NAME'>"+value.WO_MEASURMEN_NAME+"<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"raPreviousBillAmount' value='"+raPreviousBillAmount+"'><input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"advPreviousBillAmount' value='"+advPreviousBillAmount+"'></td>";
									    
								    str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"RECORD_TYPE'>"+value.RECORD_TYPE+"<input type='hidden' value='"+(workOrderTypeOfWork=='MATERIAL'?value.RECORD_TYPE:value.WO_CONTAINS)+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"Contains'></td>"; //"+area_price_per_unit+"   
								    str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"PAYMENTDONE'>"+pravArea+"<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"raPraviousArea' value='"+raPraviousArea+"'><input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AdvPraviousArea' value='"+AdvPraviousArea+"'></td>";
								    str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA12'> <input type='text'   style='width:100%;'  class='form-control copyPasteRestricted allocateArea"+rowNum+" "+value.WO_WORK_AREA_GROUP_ID+"'   id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREA'   name='"+value.WO_WORK_AREA_GROUP_ID+"'   onfocusout='validateWorkArea(this.value,\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")' value='"+value.AREA_ALOCATED_QTY+"' onkeypress='return isNumberCheck(this, event)'  "+recordTypeForReadonly+"><input type='hidden' class='allocateAreaHidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ALLOCATE_AREAHIDDEN'><input type='text' style='border: none;width:100%;' value='selected' title='already allocated quantity' readonly > </td>";
								    str+=" <td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text' class='form-control' style='width: 100%;' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"BOQ_RATE' value='"+area_price_per_unit.toFixed(2)+"' readonly='readonly'  style='width: 66px;'>  <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' id='hiddenid"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'> </td>";
								   
									if(pravArea!=0){
							  	 	   str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text'  style='width:100%;' class='form-control copyPasteRestricted acceptedRate"+rowNum+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+value.PRICE.toFixed(2)+"' onfocusout='validateWorkAreaAcceptedRate(this.value,\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")'  onkeypress='return isNumberCheck(this, event)' readonly> <input type='hidden' class='acceptedRateHidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATEHIDDEN'> </td>";
									}else{
								 	   str+="<td style='' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"AVAILABE_AREA13'> <input type='text'  style='width:100%;' class='form-control copyPasteRestricted acceptedRate"+rowNum+"' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATE' value='"+value.PRICE.toFixed(2)+"' onfocusout='validateWorkAreaAcceptedRate(this.value,\""+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\",\""+value.WO_WORK_AREA_GROUP_ID+"\")'  onkeypress='return isNumberCheck(this, event)' "+recordTypeForReadonly+"> <input type='hidden' class='acceptedRateHidden' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACCEPTED_RATEHIDDEN'> <input type='hidden' value='"+value.WO_WORK_AREA_GROUP_ID+"' id='hiddenid"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"'> </td>";
									}
								
								 	if(value.RECORD_TYPE!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
								 	   str+="<td style=''><button type='button' class='btnaction' id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"addbtn' value='"+duplicateWorkAreaId+"'  onclick='addDuplicateWorkAreaRow(\""+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"\","+row+",1)'><i class='fa fa-plus'></i></button></td>";
								 	}else{
								 	   str+="<td style=''></td>"; 
								 	}
							 	 
							 		str+="</tr>";
										
								   
								   //this is the actual data before changing the values of work area 
								   //this values are going to backend with out modifying anything
								   data1+="<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ALLOCATE_AREA' value='"+value.AREA_ALOCATED_QTY+"'>";
								   data1+="<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ACCEPTED_RATE'  value='"+value.PRICE+"'>";

								   data1+=" <input type='hidden' name='actualAreaAlocatedQTY"+row+"' id='actualAreaAlocatedQTY"+row+id+"' value='"+value.AREA_ALOCATED_QTY+"'>"+
									"<input type='hidden' name='actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID"+row+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'>";
								   data1+="<input type='hidden' name='actualWorkAreaId"+row+"' id='actualWorkAreaId"+row+id+"' value='"+value.WO_WORK_AREA_ID+"'>";
								   data1+="<input type='hidden' name='actualAreaAlocatedQTYPrice"+row+"' id='actualAreaAlocatedQTYPrice"+row+id+"' value='"+value.PRICE+"'>";
									 
								   data1+="<input type='hidden' name='BLOCK_NAME1"+row+"' value='"+value.BLOCK_NAME+"'>";
								   data1+="<input type='hidden' name='FLOOR_NAME1"+row+"' value='"+floorName+"'>";
								   data1+="<input type='hidden' name='FLAT_ID1"+row+"' value='"+flatName+"'>";
								   data1+="<input type='hidden' name='actualrecordType"+row+"' value='"+value.RECORD_TYPE+"'>";
								   
								   data1+="<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ALLOCATE_AREA' value='"+value.AREA_ALOCATED_QTY+"'>";
								   data1+="<input type='hidden'  id='"+rtypeForId+value.WO_WORK_AREA_GROUP_ID+duplicateWorkAreaId+"ACTUAL_ACCEPTED_RATE'  value='"+value.PRICE+"'>";
	 						        									
	 									totalAreaQty+=+value.AREA_ALOCATED_QTY;
	 									if(value.RECORD_TYPE=="MATERIAL"){
	 										materialAmount=materialAmount+(value.AREA_ALOCATED_QTY*value.PRICE);	
	 									}
									}
									rowNum++;
									debugger;
									id++;
									$("#appendActualWorkOrderArea"+row).append(data1);
									$("#areaMapping"+row).append(str);
									
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
									 $(".copyPasteRestricted").bind('paste', function (e) {
											e.preventDefault();
										});
									
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
						   				var recordType=$(this).attr("id").split(workAreaId)[0];
						   				//var workAreaForDB=$("#workAreaIdValue"+workAreaId).val()==""?0:$("#workAreaIdValue"+workAreaId).val();
						   				var workAreaForDB=$("#workAreaIdValue"+recordType+workAreaId).val()==""?0:$("#workAreaIdValue"+recordType+workAreaId).val().split("LABOR")[1];
						   				//seletedArea=parseFloat($("#"+workAreaForDB+(id)+"ALLOCATE_AREA").val());
						   				
						   				if(recordType=='MATERIAL'){
						   					return ;
						   				}
	
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
									
									
								/* 	var tempHoldingNum=1;
									var actualArea=0;
									var seletedArea=0;
									duplicateWorkAreaId=1;
									workDescriptionid="";
									//this loop is used to assign the values of availble area same values like a while creating work order
									$.each($("input[name='chk1"+row+"']:checked"), function(){            
										debugger;
										var len = $("input[name='chk1"+row+"']:checked").length;
						   				var workAreaId=$(this).val();
						   				//var recordType=$("#workAreaIdRecordType"+workAreaId).val();
						   				var recordType=$(this).attr("id").split(workAreaId)[0];
						   				var workAreaForDB=$("#workAreaIdValue"+recordType+workAreaId).val()==""?0:$("#workAreaIdValue"+recordType+workAreaId).val();
						   				var seletedArea=parseFloat($("#"+recordType+workAreaId+"ALLOCATE_AREA").val());
						              
						   			 var n=1;
					   			 	 //below two variables for material purpose
					   			 	 var seletedArea1;
					   			 	 var actualArea1;
					   			 	 //checking duplicate WDGroup id with checked area
						   			 $.each($("input[name='chk1"+row+"']:checked"), function(){            
						   				debugger;
											var len = $("input[name='chk1"+row+"']:checked").length;
							   				var workAreaId1=$(this).val();		
							   				var recordType1=$(this).attr("id").split(workAreaId1)[0];
							   				var workAreaForDB1=$("#workAreaIdValue"+recordType1+workAreaId1).val()==""?0:$("#workAreaIdValue"+recordType1+workAreaId1).val();
							   				var id=workAreaId1.split(workAreaForDB1.split(recordType1)[1])[1];							   				
							   				var withoutRecordforId=workAreaForDB1.split(recordType1)[1];
							   				if(workAreaForDB==workAreaForDB1 && recordType!="MATERIAL"){						   					
							   				 						
							   						console.log("Duplicate");
							   						seletedArea=parseFloat($("#"+workAreaForDB+(id)+"ALLOCATE_AREA").val());
													actualArea=$("#"+workAreaForDB+(id)+"WORK_AREA").text()==""?0:parseFloat($("#"+workAreaForDB+(id)+"WORK_AREA").text());
													seletedArea1=parseFloat($("#MATERIAL"+withoutRecordforId+(id)+"ALLOCATE_AREA").val());
													actualArea1=$("#MATERIAL"+withoutRecordforId+(id)+"WORK_AREA").text()==""?0:parseFloat($("#MATERIAL"+withoutRecordforId+(id)+"WORK_AREA").text());
													duplicateWorkAreaId=duplicateWorkAreaId+1;	
													tempHoldingNum++;
													
													$("#"+workAreaForDB+(parseInt(id)+1)+"WORK_AREA").text((actualArea-seletedArea).toFixed(2));
													$("#MATERIAL"+withoutRecordforId+(parseInt(id)+1)+"WORK_AREA").text((actualArea1-seletedArea1).toFixed(2));
							   				
							   					n++;
							   				}else{
							   					console.log("No Duplicate")
							   				}
							   				
						   			 });
									
						            }); */
									
									
								},
								error : function(data, status, er) {
									alert(data + "_" + status + "_" + er);
								}
							});
	
			}
		
		
		function closeWorkAreaPopup(rowsIncre){
			
			$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
		}
			 
		var rowsIncre=1;
		//calculating total amount of work order row
		//this method will work for only NMR Work Order
		function calCulateTotalAmout(type,acceptedRate,rownum){
			var typeOfWork="${WorkOrderBean.typeOfWork}";
			//if(typeOfWork=="PIECEWORK"||typeOfWork=="MATERIAL"){
			if(typeOfWork!="NMR"){
				return false;
			}
				
				
			if (typeof(Storage) !== "undefined") {
			     
		    // Retrieve
		    if(type=="qty"){
		     	var qty=$("#AcceptedRate1"+rownum).val();
				$("#TotalAmountId"+rownum).val(qty*acceptedRate);
		    	
		    }else{
			  	var qty=$("#Quantity"+rownum).val();
				$("#TotalAmountId"+rownum).val(qty*acceptedRate);
		    }
			} else {
			   alert("Sorry, your browser does not support Web Storage...");
			};
			
			var sumofTotalAmount=0;
		   	$(".reviseworkorderrowcls").each(function(){
				
		   		var currentId=$(this).attr("id").split("tr-class")[1];	
				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
			});
		   	
			//total BOQ AMount
			var totalBOQAmount=$("#TotalBOQAmount").val()==""?0:parseFloat($("#TotalBOQAmount").val());
			//till date inititiated BOQ Amount
			var totalNMRWOInitiatedAmount=$("#TotalNMR_WO_initiatedAmount").val()==""?0:parseFloat($("#TotalNMR_WO_initiatedAmount").val());
			//current work order amount we have to minus the current work order amount as well becoz this is the completed NMR WO
			//this work order amount is already added in ($("#TotalNMR_WO_initiatedAmount")) this amount
			var currentWorkOrderTotalAmount=$("#totalWoAmount").val();
			totalNMRWOInitiatedAmount=(totalNMRWOInitiatedAmount-currentWorkOrderTotalAmount)
			var initiateAmount=totalBOQAmount-totalNMRWOInitiatedAmount;
						
			if(sumofTotalAmount>initiateAmount){
				alert("Total NMR BOQ Amount "+(totalBOQAmount.toFixed(2))+" Initiated Amount is "+(totalNMRWOInitiatedAmount.toFixed(2))+" you can initiate now = "+initiateAmount);
				$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
				$("#AcceptedRate1"+rownum).val($("#actualAcceptedRate"+rownum).val());
				$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
				return false;
			}
		}
		
		function validateWorkAreaAcceptedRate(val,workAreaId){
			
		
	 		var allocatedArea = parseFloat($("#" + workAreaId + "ALLOCATE_AREA").val());
			
			var accepted_rate= parseFloat($("#" + workAreaId + "ACCEPTED_RATE").val().trim());
			var boq_rate= parseFloat($("#" + workAreaId + "BOQ_RATE").val().trim());
			var paymentdone= parseFloat($("#" + workAreaId + "PAYMENTDONE").text());
			
			var actualAreaAlocatedQTY =	parseFloat($("#" + workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#" + workAreaId + "ACTUAL_ALLOCATE_AREA").val());
			var actualAreaAlocatedQTYPrice =parseFloat($("#" + workAreaId + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#" + workAreaId + "ACTUAL_ACCEPTED_RATE").val());
			
			if(allocatedArea==actualAreaAlocatedQTY&&accepted_rate!=actualAreaAlocatedQTYPrice&&paymentdone!=0){
				alert("You can't modify accepted rate for the current qunatity please change quantity to modify rate.");
			
				//$("#"+workAreaId+"ACCEPTED_RATE").focusout();
            	$("#"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
            	return false;
			}
			
			if(!isNum(val)){
				$("#"+workAreaId+"ACCEPTED_RATE").focus();
            	$("#"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
            	return false;
			}
			if(accepted_rate>boq_rate){
				alert("You can't enter accepted rate more than BOQ rate.");
				$("#"+workAreaId+"ACCEPTED_RATE").focus();
            	$("#"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
            	return false;
			}
			if(accepted_rate<actualAreaAlocatedQTYPrice&&paymentdone!=0){
				alert("You can not reduce the work area accepted rate, payment already initiated for this area");
            	$("#"+workAreaId+"ACCEPTED_RATE").val(actualAreaAlocatedQTYPrice);
            	return false;
			}
		}
	
	

		function validateWorkAreaVal(workAreaId,row) {
			debugger;
			var sendDataToBackEnd=new Array();
			var workOrderTypeOfWork=$("#typeOfWork").val();
			var allocatedArea = parseFloat($("#LABOR" + workAreaId + "ALLOCATE_AREA").val().trim());
			var actualArea= parseFloat($("#LABOR" + workAreaId + "WORK_AREA").text().trim());
			var accepted_rate=$("#LABOR"+workAreaId+"ACCEPTED_RATE").val().trim();
            var Contains=$("#LABOR"+workAreaId+"Contains").val();
            var ContainsSpecailChar = Contains.includes("@@");
            
            
			var actualAllocatedAre=$("#LABOR" + workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + workAreaId + "ACTUAL_ALLOCATE_AREA").val();
			actualAllocatedAre=actualAllocatedAre==undefined?0:actualAllocatedAre;
			
			
			var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text());
			if($("#LABOR" + workAreaId + "val").is(":checked")==false){
				$("#LABOR" + workAreaId + "val").prop("checked", false);
				$("#MATERIAL" + workAreaId + "val").prop("checked", false);
			}else{
				$("#MATERIAL" + workAreaId + "val").prop("checked", true);	
			}
			if(paymentdone!=0||paymentdone!=0.0){
				alert("You can not remove this work area, already payment initiated.");
				$("#LABOR" + workAreaId + "val").prop("checked", true);
				$("#MATERIAL" + workAreaId + "val").prop("checked", true);
				return false;
			} 
			var advPreviousBillAmount=parseFloat($("#LABOR" + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + workAreaId + "advPreviousBillAmount").val());;
             if(advPreviousBillAmount!=0){
            	alert("You can not remove this work area, already payment initiated.");
  				$("#LABOR" + workAreaId + "val").prop("checked", true);
 				$("#MATERIAL" + workAreaId + "val").prop("checked", true);
 				return false;
            } 
            	
            var workAreaForDB="";
 		    var workIssueDetailsId="";
            var seletedWorkArea="";
    		if(workOrderTypeOfWork=="MATERIAL"){
    			workAreaForDB=$("#LABOR"+workAreaId+"workAreaID").val();
    		        workIssueDetailsId=$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
                        seletedWorkArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
    		}else{
    			 workAreaForDB=$("#MATERIAL"+workAreaId+"workAreaID").val()==undefined?"":$("#MATERIAL"+workAreaId+"workAreaID").val();
    		    workIssueDetailsId=$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
                seletedWorkArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==undefined?"0":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()); 
				/* workAreaForDB=$("#MATERIAL"+workAreaId+"workAreaID").val()==undefined?"":$("#MATERIAL"+workAreaId+"workAreaID").val();
    		    workIssueDetailsId=$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
                seletedWorkArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==undefined?"0":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()); */

    		}
		     if(workAreaForDB!=""&&workIssueDetailsId!=""&&seletedWorkArea!=0){
		    	 var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
					sendDataToBackEnd.push(data);
		     }
					
			  if(sendDataToBackEnd.length!=0){
				var canIcloseModelPopup= ajaxCallForValidatingWO_Material(row,sendDataToBackEnd,'isMaterialIssued')
				if(canIcloseModelPopup==false){
					return false;
				} 
			  }
			
			
			  
            actualArea+= parseFloat(actualAllocatedAre);
			if(allocatedArea>actualArea){
				alert("You can not enter the value more than of availble area.");
				$("#LABOR" + workAreaId + "val").prop("checked", false);
				$("#MATERIAL" + workAreaId + "val").prop("checked", false);
				return false;
			}
			var len=$("input[name='chk1"+row+"']:checked").length;
			var validationForCheckBox=0;
			/* if(ContainsSpecailChar==true){  
				validationForCheckBox=1;
	        } */
			if(len==validationForCheckBox){
				alert("You can't remove last work area row.");
				$("#LABOR" + workAreaId + "val").prop("checked", true);
				$("#MATERIAL" + workAreaId + "val").prop("checked", true);
				return false;
			}
			if($("#LABOR" + workAreaId + "val").is(":checked")==false){
				alert("Removed work area from list");
				//$("#LABOR"+workAreaId+"ACCEPTED_RATE").val("0");
				$("#LABOR" + workAreaId + "ALLOCATE_AREA").val("0");
				$("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val("0");
				$("#LABOR" + workAreaId + "val").prop("checked", false);
				$("#MATERIAL" + workAreaId + "val").prop("checked", false);
				return false;
			}else{
				var qty=parseFloat($("#Quantity"+row).val());
			}
			
			
			if (isNaN(allocatedArea)&&allocatedArea!=0) {
				alert("Please enter only numbers.");
				$("#LABOR" + workAreaId + "ALLOCATE_AREA").val("0");
				$("#MATERIAL" + workAreaId + "ALLOCATE_AREA").val("0");
				$("#LABOR" + workAreaId + "val").prop("checked", false);
				$("#MATERIAL" + workAreaId + "val").prop("checked", false);
			}
			
			if(allocatedArea==0){
				alert("Please enter correct value.");
				$("#LABOR" + workAreaId + "val").prop("checked", false);
				$("#MATERIAL" + workAreaId + "val").prop("checked", false);
				return false;
			}
			if(!isNum(accepted_rate)||accepted_rate=="0"){
					alert("Please enter accepted rate.");
					$("#LABOR"+workAreaId+"ACCEPTED_RATE").focus();
					$("#LABOR" + workAreaId + "val").prop("checked", false);
					$("#MATERIAL" + workAreaId + "val").prop("checked", false);
					return false;
				}
		}
		
		function isNum(value) {
			  var numRegex=/^[0-9.]+$/;
			  return numRegex.test(value)
		}
		function showScopeOfWork(row){
			$('#modalForScopeWork'+row).modal('show'); 
		}
 
/* 		function showWDGroupWiseMaterialDetails(rowsIncre){
			debugger;
 			var WD=$("#comboboxsubSubProd"+rowsIncre).val();
			if(WD==null||WD.length==0){
				alert("Please select WD.");
				return false;
			}
			WD=WD.split("$")[0];
			var siteId=$("#site_id").val();
			 var workOrderTypeOfWork=$("#typeOfWork").val();
			 var WorkOrderNo=$("#workOrderId").val();
			 var url="viewMaterialProductWDDetails.spring";
			 
			$.ajax({
				  url : url,
				  type : "get",
				  contentType : "application/json",
					data:{
						workDescId:WD,
						siteId:siteId,
						typeOfWork:workOrderTypeOfWork
					},
				  success : function(data) {
					//	alert(data);
						debugger;
						var htmlData="";
						var rows=1;
						$.each(data,function(key,value){
							htmlData+="<tr>";
							htmlData+="<td>"+rows+"</td>";
							htmlData+="<td>"+value.WO_WORK_DESCRIPTION+"</td>";
							htmlData+="<td class='anchor-class' onclick='loadAreaWiseWDMaterialDetails("+rowsIncre+",\""+value.MATERIAL_GROUP_ID+"\")'>"+value.MATERIAL_GROUP_NAME+"</td>";
							htmlData+="<td>"+value.MATERIAL_GROUP_MST_NAME+"</td>";
							htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>";
							htmlData+="</tr>";
						});
						$("#showWDGroupWiseMaterialDetails").html(htmlData);
						$("#ViewMaterialProductDetails").modal();
					}
			});
			
		}
		
		function loadAreaWiseWDMaterialDetails(rowsIncre,materialGroupId){
			debugger;
			var WD=$("#comboboxsubSubProd"+rowsIncre).val();
			if(WD==null||WD.length==0){
				alert("Please select WD.");
				return false;
			}
			WD=WD.split("$")[0];
			var siteId=$("#siteId").val();
			var workOrderTypeOfWork=$("#typeOfWork").val();
			var WorkOrderNo=$("#WorkOrderNo").val();
			var urlForAreaWiseDivisionDetails="viewMaterialAreaWiseDetails.spring";
			
			$.ajax({
				  url : urlForAreaWiseDivisionDetails,
				  type : "get",
				  contentType : "application/json",
					data:{
						workDescId:WD,
						siteId:siteId,
						typeOfWork:workOrderTypeOfWork,
						materialGroupId:materialGroupId
					},
				  success : function(data) {
						//alert(data);
						debugger;
						var htmlData="";
						var rows=1;
						$.each(data,function(key,value){
							htmlData+="<tr>";
							htmlData+="<td>"+rows+"</td>";
							htmlData+="<td>"+value.WO_WORK_DESCRIPTION+"</td>";
							htmlData+="<td>"+value.MATERIAL_GROUP_NAME+"</td>";
							htmlData+="<td>"+value.MATERIAL_GROUP_MST_NAME+"</td>";
							htmlData+="<td>"+value.BLOCK_NAME+"</td>";
							htmlData+="<td>"+(value.FLOOR_NAME==null?'-':value.FLOOR_NAME)+"</td>";
							htmlData+="<td>"+(value.FLAT_NAME==null?'-':value.FLAT_NAME)+"</td>"; 
							htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>";
							// htmlData+="<td>0</td>";
							//htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>"; 
							htmlData+="</tr>";
							rows++;
						});
						$("#showAreaWiseWDGroupMaterialDetails").html(htmlData);
						$("#ViewAreaWiseMaterialDetails").modal();
					}
			});
		} */
		
		//for showing the selected work area
		function showDetailsFunction(rowsIncre) {
			
			
			var len=$("#lengthOfSelectedArea"+rowsIncre).val();
			var htmlData="";
			for (var i = 1; i <= len; i++) {
					var block_name=$("#BLOCK_NAMET"+rowsIncre+i+"").val();
					var floor_name=$("#FLOOR_NAMET"+rowsIncre+i+"").val();
					var flat_id=$("#FLAT_IDT"+rowsIncre+i+"").val();
					var seletedWorkArea=$("#selectedAreaT"+rowsIncre+i+"").val();
					var wo_measurmen_name=$("#wo_measurmen_nameT"+rowsIncre+i+"").val();
					var accepted_rate=$("#accepted_rateT"+rowsIncre+i+"").val();
					var record_typeT=$("#record_typeT"+rowsIncre+i+"").val();
					htmlData+=" <tr><td>"+block_name+"</td>	<td>"+floor_name+"</td>	<td>"+flat_id+"</td> <td>"+wo_measurmen_name+"</td><td>"+record_typeT+"</td> <td>"+seletedWorkArea+"</td><td>"+accepted_rate+"</td </tr>";		
				}
			
			$("#tblShowDetails").html(htmlData);
		}
		
		
		function myFunction(workOrderID,acceptedRate,row,isShow) {
			
			$("#spinner"+row).show();
			var appendId=$("#allocatedareaAppend").val();
			//make the div data empty
		
			$("#areaMapping").html("");
			workDescId= $("#comboboxsubSubProd"+row).val();
			 
			if(workDescId==undefined||workDescId==""||workDescId==null){
				alert("Please select Work description.");
				 return false;
			 }
			 UOMId=$("#UOMId"+row).val().split("$")[0];
			
			 if(UOMId==undefined||UOMId==""||UOMId==null){
						alert("Please select UOM.");
						$("#UOMId"+row).focus();
						return false;
				 }
		 
			/*  //maintaining Area
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
			 */
			 
			 
			 $('#Modal-create-wo-popup'+row).modal('show'); 
			$("#spinner"+row).hide();
			$("#loderId").hide();

		}
		
		
//============================================Method for Calculating the total work area ====================================================		
		function addWorkArea(rowsIncre, operationtype){
			debugger;
			var materialWoAmount=0.0;
			var laborWoAmount=0.0;
			var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
			var isDelete=$("#isDelete"+rowsIncre).val();
			var dummyAreaDetailsPrimaryKey=100;
		
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
				}) 
			*/
				
			var canIcloseModelPopup=true;
			var workOrderContains=new Array();
			
			var sendDataToBackEnd=new Array();

			var total = 0;
			var rowNum=rowsIncre;
			var rows=0;
			var totalAmount=0;
			var htmlData="";
			var  workOrderTypeOfWork=$("#typeOfWork").val();
			$.each($("input[name='chk1"+rowsIncre+"']:checked"), function(){            
				
				
				rows++;
				var len = $("input[name='chk1"+rowsIncre+"']:checked").length;
   				var workAreaId=$(this).val();
   				var recordtype=$(this).attr("id").split(workAreaId)[0];
   				var workAreaForDB=$("#"+recordtype+workAreaId+"workAreaID").val();
                var seletedWorkArea=parseFloat($("#"+recordtype+workAreaId+"ALLOCATE_AREA").val());
                var actualArea1=parseFloat($("#"+recordtype+workAreaId+"ACTUAL_AREA").text().trim());
                var actualArea=$("#"+recordtype+workAreaId+"WORK_AREA").text()==""?0:parseFloat($("#"+recordtype+workAreaId+"WORK_AREA").text());
                var wo_measurmen_name=$("#"+recordtype+workAreaId+"WO_MEASURMEN_NAME").text();
                var record_type=$("#"+recordtype+workAreaId+"RECORD_TYPE").text();
                var accepted_rate=parseFloat($("#"+recordtype+workAreaId+"ACCEPTED_RATE").val().trim());
                var BOQ_RATE=parseFloat($("#"+recordtype+workAreaId+"BOQ_RATE").val().trim());
                var workIssueDetailsId=$("#"+recordtype+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#"+recordtype+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
                var workOrderRowContains=$("#"+recordtype+workAreaId+"Contains").val();
                var availableArea=(actualArea-seletedWorkArea);
              
                //this code for getting the paid amount for work area
                var paymentdone= parseFloat($("#"+recordtype + workAreaId + "PAYMENTDONE").text()==""?0:$("#"+recordtype + workAreaId + "PAYMENTDONE").text());
    			var raPraviousArea=parseFloat($("#"+recordtype + workAreaId + "raPraviousArea").val()==undefined?0:$("#"+recordtype + workAreaId + "raPraviousArea").val());
				var AdvPraviousArea=parseFloat($("#" +recordtype+ workAreaId + "AdvPraviousArea").val()==undefined?0:$("#"+recordtype + workAreaId + "AdvPraviousArea").val());
				var raPreviousBillAmount=parseFloat($("#" +recordtype+ workAreaId + "raPreviousBillAmount").val()==undefined?0:$("#" +recordtype+ workAreaId + "raPreviousBillAmount").val());;
				var advPreviousBillAmount=parseFloat($("#"+recordtype + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#" +recordtype+ workAreaId + "advPreviousBillAmount").val());;
		
				if(record_type=="MATERIAL"){
					var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
					//var things ={ workOrderNo:workOrderNumber,contractorId:contractorId,siteId:siteId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,actualArea:actualArea1,availbleArea:actualArea,wo_measurmen_name:wo_measurmen_name,paymentDone:paymentdone,selectedArea:seletedWorkArea};
					sendDataToBackEnd.push(data);
				}
				
                var actualAreaAlocatedQTYPrice =$("#" +recordtype+ workAreaId + "ACTUAL_ACCEPTED_RATE").val()==""?0:$("#" +recordtype+ workAreaId + "ACTUAL_ACCEPTED_RATE").val();
                var actualAllocatedAre=$("#" +recordtype+ workAreaId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#"+recordtype+ workAreaId + "ACTUAL_ALLOCATE_AREA").val();
				
                if(actualAreaAlocatedQTYPrice=="NaN"||actualAreaAlocatedQTYPrice==undefined){
					actualAreaAlocatedQTYPrice=0;
					actualAllocatedAre=0;
				}else{
					actualAreaAlocatedQTYPrice=parseFloat(actualAreaAlocatedQTYPrice);
					actualAllocatedAre=parseFloat(actualAllocatedAre);
				}        
                
              var validateAvailbleArea=actualAllocatedAre+actualArea;
			  if(actualArea<seletedWorkArea){
					
	            	alert("you can't allocate area more than of availbale area.");	
	               	$("#"+recordtype + workAreaId + "BOQ_RATE").focus();
	               	canIcloseModelPopup=false;
	               	return false;
                }
           	    
				 //checking here is the adv previous amount if not zero but trying to descrese the quantity 
			//checking here is the adv previous amount if not zero but trying to descrese the quantity 
				advPreviousBillAmount=parseFloat(advPreviousBillAmount.toFixed(2));
				var tempAdvAmount= (accepted_rate*seletedWorkArea).toFixed(2);
				if(advPreviousBillAmount!=0){
				  if(tempAdvAmount<advPreviousBillAmount){
					  
					 alert("It's seems area is decreasing than the paid amount, Please change the area.");
					 $("#"+recordtype+workAreaId+"ALLOCATE_AREA").val(actualAllocatedAre);
					 $("#"+recordtype+workAreaId+"ALLOCATE_AREA").focus();
					 canIcloseModelPopup=false;
					 return false;
 				  }
				 }
				
			    const isInArray1 = workOrderContains.includes(workOrderRowContains);
				if (isInArray1 == false) {
					
					workOrderContains.push(workOrderRowContains);
				}
				 
				// totalAmount=(paymentdone*actualAreaAlocatedQTYPrice);
				if(record_type!="MATERIAL"&&workOrderTypeOfWork!='MATERIAL'){
				 	totalAmount+=accepted_rate*(seletedWorkArea);
				 	laborWoAmount+=accepted_rate*seletedWorkArea;
            	}else{
            		materialWoAmount+=accepted_rate*seletedWorkArea;
                }
				 
				 //if this work order is Material Work Order then assigning material amount to total amount
                if(workOrderTypeOfWork=='MATERIAL'){
                	totalAmount=materialWoAmount;
                }
				if(accepted_rate.length==0){
		            alert("Please enter accepted rate.");
		               	$("#"+recordtype+workAreaId+"ACCEPTED_RATE").focus();
		               	canIcloseModelPopup=false;
		               	return false;
		        }else if(accepted_rate==0){
		        	alert("Please enter accepted rate.");
	               	$("#"+recordtype+workAreaId+"ACCEPTED_RATE").focus();
	               	canIcloseModelPopup=false;
	               	return false;
		        }
               var value=seletedWorkArea/actualArea;
               var percentage=value*100;
               
          		if (isNaN(seletedWorkArea)) {
          			alert("Please enter only digits.");
	   				$("#" +recordtype+ workAreaId + "ALLOCATE_AREA").val("0");
	   				$("#"+recordtype+workAreaId+"ALLOCATE_AREA").focus();
	   				$("#"+recordtype + workAreaId + "val").prop("checked", false);
	   				canIcloseModelPopup=false;
	   				return false;
	   			}
               if(seletedWorkArea<=0){
               	   alert("Plese enter correct value for area");
            	   $("#"+recordtype+workAreaId+"ALLOCATE_AREA").focus();
	               canIcloseModelPopup=false;
	               return false;
               }
               
           	
           	   htmlData+="<input type='hidden' name='lengthOfSelectedArea"+rowsIncre+"' id='lengthOfSelectedArea"+rowsIncre+"' value='"+len+"'>";
         	   htmlData+="<input type='hidden' name='percentage"+rowsIncre+"' value='"+percentage+"'>";
               htmlData+="<input type='hidden' name='selectedArea"+rowsIncre+"' id='selectedAreaT"+rowsIncre+rows+"' value='"+seletedWorkArea+"'>";
               htmlData+="<input type='hidden' name='availableArea"+rowsIncre+"' value='"+actualArea+"'>";
               htmlData+="<input type='hidden' name='workAreaId"+rowsIncre+"' value='"+workAreaForDB+"'>";
               htmlData+="<input type='hidden' name='available"+rowsIncre+"' value='"+availableArea+"'>";
               htmlData+="<input type='hidden' name='wo_measurmen_name"+rowsIncre+"'  id='wo_measurmen_nameT"+rowsIncre+rows+"'  value='"+wo_measurmen_name+"'>";
               htmlData+="<input type='hidden' name='record_type"+rowsIncre+"' id='record_typeT"+rowsIncre+rows+"' value='"+record_type+"'>";
               htmlData+="<input type='hidden' name='accepted_rate"+rowsIncre+"' id='accepted_rateT"+rowsIncre+rows+"' value='"+accepted_rate+"'>";
               htmlData+="<input type='hidden' name='boqRate"+rowsIncre+"' id='boqRateT"+rowsIncre+rows+"' value='"+BOQ_RATE+"'>";
              if(workIssueDetailsId.length!=0&&workIssueDetailsId!="NaN"){
	               htmlData+="<input type='hidden' name='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+"' id='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+rows+"' value='"+workIssueDetailsId+"'>";
			}else{
			
			 	   htmlData+="<input type='hidden' name='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+"' id='QS_WO_TEMP_ISSUE_DTLS_ID"+rowsIncre+rows+"' value='"+(dummyAreaDetailsPrimaryKey)+"A'>";   
			 		dummyAreaDetailsPrimaryKey++;
			}
               var block_name=$("#"+recordtype+workAreaId+"BLOCK_NAME").text().trim();
               var floor_name=$("#"+recordtype+workAreaId+"FLOOR_NAME").text().trim();
               var flat_id=$("#"+recordtype+workAreaId+"FLAT_ID").text().trim();
              
               htmlData+="<input type='hidden' name='BLOCK_NAME"+rowsIncre+"'   id='BLOCK_NAMET"+rowsIncre+rows+"' value='"+block_name+"'>";
	           htmlData+="<input type='hidden' name='FLOOR_NAME"+rowsIncre+"'  id='FLOOR_NAMET"+rowsIncre+rows+"'  value='"+floor_name+"'>";
	           htmlData+="<input type='hidden' name='FLAT_ID"+rowsIncre+"' id='FLAT_IDT"+rowsIncre+rows+"' value='"+flat_id+"'>";
           		
               
	           if(record_type!="MATERIAL"||workOrderTypeOfWork=='MATERIAL'){
	     		   total += seletedWorkArea;
           	}
            rowNum++;
 		});
			if(canIcloseModelPopup==false){
				return false;
			}
			
			$("#appendWorkOrderArea"+rowsIncre).html(htmlData);
			var id12=$("#appendData"+rowsIncre).val();
			
			
			 materialWoAmount=materialWoAmount.toFixed(2);
			 laborWoAmount=laborWoAmount.toFixed(2);
			  
			 $("#LaborAmountId"+rowsIncre).val(laborWoAmount);
			 $("#MaterialAmountId"+rowsIncre).val(materialWoAmount);
			 $("#WoRecordContains"+rowsIncre).val(workOrderContains.toString());
		  if($("#WORecordContails").val()==""||$("#WORecordContails").val().length==0){
				    $("#WORecordContails").val(workOrderContains.toString());
		   }
		  console.log(JSON.stringify(sendDataToBackEnd));
		  
		
		  if(operationtype=="validateLabor"){
			  
			  canIcloseModelPopup= ajaxCallForValidatingWO_Material(rowsIncre,sendDataToBackEnd,'')
				if(canIcloseModelPopup==false){
					return false;
				} 	
			  $(".overlay_ims").hide();	
			  $(".loader-ims").hide();
		  }
		  		
       	  //$("#TotalAmountId"+rowsIncre).val((total)*acceptedRate); 
         	$("#TotalAmountId"+rowsIncre).val(totalAmount.toFixed(2));
         	$("#Quantity"+rowsIncre).val(total.toFixed(2)); 
         	$("#QuantityId"+rowsIncre).val(total.toFixed(2));
         	$("#showQty"+rowsIncre).show();
         	$("#showQty"+rowsIncre).css("display","inherit");
         	$("#showMaterialQty"+rowsIncre).show();
			$("#table-quantity").hide();
			var materialWoAmount=0;
		    var laborWoAmount=0;
			$(".reviseworkorderrowcls").each(function(){
				var currentId=$(this).attr("id").split("tr-class")[1];	
				if($("#isDelete"+currentId).val()!="d"){
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
			  WoGrandTotal=parseFloat(laborWoAmount)+parseFloat(materialWoAmount);
			 $("#WoGrandTotalAmount").text(inrFormat(WoGrandTotal.toFixed(2)));
			
			$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
			$(".overlay_ims").hide();	
			$(".loader-ims").hide(); 
		}
		//============================================Method for Calculating the total Qty and Total Amount of Work ================================		
		
		function ajaxCallForValidatingWO_Material(rowsIncre,sendDataToBackEnd,isMaterialIssued){
				debugger;
			//alert("Please wait for response, popup will close automatically."); 
			/* $(".overlay_ims").show();	
			$(".loader-ims").show(); */
			//$(".loader-ims").show();
			$("#addWorkAreaBtn"+rowsIncre).hide();
			$("#saveBtnId").attr("disabled",true);
			$("#saveBtnId1").attr("disabled",true);
			$("#loaderModal"+rowsIncre).show();
			var canIcloseModelPopup=true;
			var contractorId=$("#contractorId").val();
			var workOrderNumber=$("#workOrderId").val();
			var siteId=$("#site_id").val();
			var workAreaGroupId="";
			var workAreaId="";
			var url = "validateWOMaterialBOQDetails.spring";
			var status;
			if(isMaterialIssued=='removeRow'){
				status=false;
			}else{
				status=true;
			}
			//status=false;
			 $.ajax({
				url : url,
				type : "get",
				async: status,
				data:{
					data:JSON.stringify(sendDataToBackEnd),
					contractorId:contractorId,
					workOrderNumber:workOrderNumber,
					siteId:siteId,
					condition:isMaterialIssued
					},
					beforeSend: function (SOAPEnvelope)  {
						$("#loaderModal"+rowsIncre).show();
						$("#addWorkAreaBtn"+rowsIncre).hide();
						//$('#Modal-create-wo-popup'+rowsIncre).modal('show'); 
		            },
				success : function(data) {

					$("#loaderModal"+rowsIncre).hide();
					$("#addWorkAreaBtn"+rowsIncre).show();
					$("#saveBtnId").attr("disabled",false);
					$("#saveBtnId1").attr("disabled",false);
					//$(".loader-ims").hide();
					$.each(data,function(key, value) {
						//alert(key+" "+value);
						
						var temp=value.split("@@")[1];
						workAreaId=temp.split("##")[0];
						workAreaGroupId=temp.split("##")[1];
						
						if(isMaterialIssued.length!=0){
							if(key=="MaterialIssued"){
								alert('You can not remove this work area, already material issued for '+value.split("@@")[1].split("##")[2]+" quantity");
								$("#LABOR" + workAreaGroupId + "val").prop("checked", true);
				 				$("#MATERIAL" + workAreaGroupId + "val").prop("checked", true);
				 			
				 				var actualAllocatedAre=$("#LABOR" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#LABOR" + workAreaGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
				 				actualAllocatedAre=$("#MATERIAL" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + workAreaGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#MATERIAL" + workAreaGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
				 				setTimeout(function(){
				 					//$('#Modal-create-wo-popup'+rowsIncre).modal('show');
				 					$("#LABOR"+workAreaGroupId+"ALLOCATE_AREA").focus();
								}, 200);
				 				canIcloseModelPopup=false;
				 			}
						}else{
							if(key!="MaterialIssued"){
								var tempGroupId=key.split("@@")[1];
								if(key.split("@@")[0]=="NewPerUnitLessThanOldPerUnit"){
									alert('Can not revise work order, already material issued per unit quantity was changed.');
								}else if(key.split("@@")[0]=="IssuedMaterialQuanity"){
									alert('You can not reduce material qty, already material issued for '+value.split("@@")[1].split("##")[2]+" quantity");
								}
								$("#LABOR" + tempGroupId + "val").prop("checked", true);
				 				$("#MATERIAL" + tempGroupId + "val").prop("checked", true);
								
				 				var actualAllocatedAre=$("#LABOR" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#LABOR" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#LABOR" + tempGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
				 				actualAllocatedAre=$("#MATERIAL" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val()==""?0:$("#MATERIAL" + tempGroupId + "ACTUAL_ALLOCATE_AREA").val();
				 				$("#MATERIAL" + tempGroupId + "ALLOCATE_AREA").val(actualAllocatedAre);
								
								$('#Modal-create-wo-popup'+rowsIncre).modal('show');
								setTimeout(function(){
									//$('#Modal-create-wo-popup'+rowsIncre).modal('show');
								
									$("#LABOR"+tempGroupId+"ALLOCATE_AREA").focus();
								}, 200);
								canIcloseModelPopup=false;
							}	
						}
 					
					});
					 $(".overlay_ims").hide();	
					 $(".loader-ims").hide();
					 $("#loderId"+rowsIncre).hide();
					 if(status==true){
						$('#Modal-create-wo-popup'+rowsIncre).modal('hide'); 
					}
				}
			}); 
			 return canIcloseModelPopup;
      	}
			
			
			
//============================================Method for checking all the Work =========================================================
			function checkAllCheckBox(rowNum){
				var sendDataToBackEnd=new Array();
				var flag="true";
				$("input[name='chk1"+rowNum+"'").not(this).prop('checked', this.checked);
				var len = $("input[name='chk1"+rowNum+"']:checked").length;
				$.each($("input[name='chk1"+rowNum+"']"), function(){      
					var workAreaId=$(this).val();
					var seletedWorkArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
		            var accepted_rate=parseFloat($("#LABOR"+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#LABOR"+workAreaId+"ACCEPTED_RATE").val());
		            var boq_rate=$("#LABOR"+workAreaId+"BOQ_RATE").val()==""?0:parseFloat($("#LABOR"+workAreaId+"BOQ_RATE").val());
			        var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text());		             
			 		var advPreviousBillAmount=parseFloat($("#LABOR" + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + workAreaId + "advPreviousBillAmount").val());;
			 		
			 		  var workAreaForDB="";
					  var workIssueDetailsId="";
			          var seletedWorkArea="";
			          //if work Order type is material 
			          if(typeOfWork=="MATERIAL"){
			        	  workAreaForDB=$("#LABOR"+workAreaId+"workAreaID").val()==undefined?"":$("#LABOR"+workAreaId+"workAreaID").val();
						  workIssueDetailsId=$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
				          seletedWorkArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
				      }else{
			        	  workAreaForDB=$("#MATERIAL"+workAreaId+"workAreaID").val()==undefined?"":$("#MATERIAL"+workAreaId+"workAreaID").val();
						  workIssueDetailsId=$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
				          seletedWorkArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val());
				      }
			          if(workAreaForDB.length!=0&&workIssueDetailsId.length!=0&&seletedWorkArea.length!=0){
						var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
						sendDataToBackEnd.push(data);
			          }
			 		
			 		
					 if(seletedWorkArea<paymentdone){
						alert("You can not reduce  the work area,payment already initiated for some area");
			           	$("#LABOR"+workAreaId+"ALLOCATE_AREA").val(actualAreaAlocatedQTY);
			           	return false;
					}	  	 			
					if(seletedWorkArea==0){
			    		alert("Please enter correct value.");
			    		$("#LABOR"+workAreaId+"ALLOCATE_AREA").focus();
			    		$("#LABOR" + workAreaId + "val").prop("checked", false);
			    		$("#MATERIAL" + workAreaId + "val").prop("checked", false);
			    		//$("#checkAll"+rowNum+"").prop("checked",false);
			    		//$("input[name='chk1"+rowNum+"'").not(this).prop('checked',false);
			    		flag="false";
			    		return false;
			    	}
		            if(accepted_rate==0){
			            alert("Please enter accepted rate.");
			            $("#LABOR"+workAreaId+"ACCEPTED_RATE").focus();
			            $("#LABOR" + workAreaId + "val").prop("checked", false);	
			            $("#MATERIAL" + workAreaId + "val").prop("checked", false);
			            //$("#checkAll"+rowNum+"").prop("checked",false);
			            //$("input[name='chk1"+rowNum+"'").not(this).prop('checked',false);
			            flag="false";
			            return false;
			        }
		            if(accepted_rate>boq_rate){
		        		alert("You can't enter accpeted rate more than BOQ rate.");
		        		$("#LABOR"+workAreaId+"ACCEPTED_RATE").focus();
		        		$("#LABOR"+workAreaId+"ACCEPTED_RATE").val('0');
		        		//$("#checkAll"+rowNum+"").prop("checked",false);
		        		$("#LABOR" + workAreaId + "val").prop("checked", false);
		        		$("#MATERIAL" + workAreaId + "val").prop("checked", false);
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
				
				var varr=document.getElementById("checkAll"+rowNum+"").checked;
//	        	            if(varr==false&&flag=="true")
		    if(varr==false||flag=="false")
				if(sendDataToBackEnd.length!=0){
					var canIcloseModelPopup= ajaxCallForValidatingWO_Material(rowNum,sendDataToBackEnd,'removeRow')
					if(canIcloseModelPopup==false){
						flag=="false"
						checkPaymentInitiatedOrMaterialCheckBox(rowNum);
						return false;
					}
			  }
			  
	        	if(flag=="false"){
			  	return false;
			}
				
				if(flag=="true"){
					$("input[name='chk1"+rowNum+"'").prop('checked', true);	
				}else{
					$("input[name='chk1"+rowNum+"'").prop('checked', false);
					checkPaymentInitiatedOrMaterialCheckBox(rowNum);
				}
			  /*  var varr=document.getElementById("checkAll"+rowNum+"").checked;
				  if(varr==false){
				  	$("input[name='chk1"+rowNum+"'").prop('checked', false);
				  } 
			  */
			}
			
			function checkPaymentInitiatedOrMaterialCheckBox(rowNum){
				$.each($("input[name='chk1"+rowNum+"']"), function(){            
					var workAreaId=$(this).val();
					var paymentdone= parseFloat($("#LABOR" + workAreaId + "PAYMENTDONE").text());
					var advPreviousBillAmount=parseFloat($("#LABOR" + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#LABOR" + workAreaId + "advPreviousBillAmount").val());;
		            if(paymentdone!=0||paymentdone!=0.0||advPreviousBillAmount!=0){
		            	$("#LABOR" + workAreaId + "val").prop("checked", true);
		       			$("#MATERIAL" + workAreaId + "val").prop("checked", true);
	 		 		} 
				});
			}

		$(document).ready(function() {
			
						//this code is for appending all the values of allocated area
						$("#hide-table-2").click(function() {

						});
						
						/* selectall checkbox */
						$("#checkAll").click(function() {
									$('input:checkbox').not(this).prop('checked', this.checked);
						console.log("checked");
						});

		});
		</script>


		<script type="text/javascript">

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
	            $(wrapper).append('<div class="col-md-12 remove-filed  margin-close"><div class="col-md-11 no-padding-right no-padding-left"><input type="text" class="form-control" id="newTC'+termsAndConditionTxtBox+'" value="'+newTc+'" name="termsAndCOnditions"/></div><div class="col-md-1"><button type="button" class="btn btn-danger remove-button remove_field" ><i class="fa fa-remove "></i></button></div></div>'); //add input box
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
 function validateFileExtention(){
	  
	var ext="";
	var kilobyte=1024;
	  $('input[type=file]').each(function () {
	        var fileName = $(this).val().toLowerCase(),
	         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
	        	
			 if(fileName.length!=0){
	        	if((this.files[0].size/kilobyte)<=kilobyte){
				}else{
					alert("Please upload below 1 MB PDF file");
					//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
					$(this).val('');
				return false;
				}
	        	
		   /* if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			  ext= fileName.substring(fileName.lastIndexOf(".")+1); 
		   */
		   
		        if (!(regex.test(fileName))) {
		            $(this).val('');
		            alert('Please select correct file format');
		        }
	        }
	    });
	}
 
 function deletepdf(divId,imagePath){
	  
		 var canISubmit = window.confirm("Do you want to delete PDF?");
		 if(canISubmit == false) {
		        return false;
		 }
		 deleteFile(divId,imagePath);
	}
 function deleteWOFile(divId,imagePath){
	 
	 
 var canISubmit = window.confirm("Do you want to delete image?");
     
 if(canISubmit == false) {
        return false;
 }
 deleteFile(divId,imagePath);
 return false;
}
 
 function deleteFile(divId,imagePath){
	 
/* 	 var canISubmit = window.confirm("Do you want to delete image?");
     
	  if(canISubmit == false) {
         return false;
     }	 */
	 var url="deleteWOPermanentImage.spring?imagePath="+imagePath+"&workOrderNo=${WorkOrderBean.siteWiseWONO}&siteId=${WorkOrderBean.siteId}";
	 var imagesAlreadyPresent=$("#imagesAlreadyPresent").val();
	 imagesAlreadyPresent=imagesAlreadyPresent-1;
	 $("#imagesAlreadyPresent").val(imagesAlreadyPresent);
	 $.ajax({
		  url : url,
		  type : "get",
		  success : function(data) {
			console.log(data);
			 window.location.reload();
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
 return false;
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
						<h4 class="modal-title text-center center-block"><strong>View Area Details</strong></h4>
					</div>
					<div class="modal-body modal-content-scroll">
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
			<div class="modal-dialog modal-lg" style="width: 90%;">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title text-center">Work Area</h4>
					</div>
					<div class="modal-body">
						<div class="table-responsive">
							<table class="table table-bordered">
								<thead>
									<tr>
										<th style="width: 14%;" class="text-center">
							
										<input type="checkbox" id="checkAll"	style="width: 14px; height: 14px;"></th>
										<th style="width: 14%;">Block</th>
										<th style="width: 14%;">Floor</th>
										<th style="width: 14%;">Flat</th>
										<th style="width: 14%;">Actual Area</th>
										<th style="width: 14%;">Available Area</th>
										<th style="width: 14%;">Measurement</th>
										<th style="width: 14%;">Payment_Initiated_Area</th>
										<th style="width: 14%;">Allocated_Area</th>
										<th style="width: 14%;">BOQ Rate(<%=acceptedRateCurrency%>)
										</th>
										<th style="width: 14%;">Accepted_Rate(<%=acceptedRateCurrency%>)
										</th>
									</tr>
								</thead>
								<tbody id="areaMapping">

								</tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<div class="text-center center-block">
							<button type="button" id="hide-table-2" class="btn btn-warning"
								data-dismiss="modal">Submit</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- modal popup for create work order table for show end -->


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
							<button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
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
		<div class="modal fade custmodal" id="uploadinvoice-img<%=i%>"
			role="dialog">
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
						<button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>
		<%
			}
		%>
		<!-- modal popup for invoice image end -->
		<script>

function appendRow() {
    
  
    var tbllength = $('.workorderrowcls').length;  
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
         
         var valStatus = validateRowData();
       // alert(valStatus);
         
        if(valStatus == false) {
            return false;
        } 
        if (typeof(Storage) !== "undefined") {
            // Store    
             
         
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
            $("#WO_MajorHead"+lastDiv).focus();
        }
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
	 calGrandTotal();
	// $(".overlay_ims").hide();	
	 //$(".loader-ims").hide();
});


function calGrandTotal(){
	/* 	 $("#materialWoAmountSpan").text(inrFormat(parseFloat( $("#materialWoAmountSpan").text())));
		$("#laborWoAmountSpan").text(inrFormat(parseFloat( $("#laborWoAmountSpan").text())));
		$("#WoGrandTotalAmount").text(inrFormat(parseFloat( $("#WoGrandTotalAmount").text()))); */
}



//this code for to active the side menu 
var referrer="reviseWorkOrder.spring";
$SIDEBAR_MENU.find('a').filter(function () {
       var urlArray=this.href.split( '/' );
       for(var i=0;i<urlArray.length;i++){
    	 if(urlArray[i]==referrer) {
    		 return this.href;
    	 }
       }
}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');


function ReviseBtn(){
	var valTable=validateTable();
	if(valTable==false){
		return false;
	}
	$("#myModal-showeorkorder").modal();
}

function validateTable(){
	var error=true;
	$(".reviseworkorderrowcls").each(function(){
		var id=$(this).attr("id").split("tr-class")[1];
		if($("#isDelete"+id).val()=="N"){
			var WO_MajorHead=$("#WO_MajorHead"+id).val();
			var WO_MinorHead=$("#WO_MinorHead"+id).val();
			var WO_Desc=$("#WO_Desc"+id).val();
			var UOMId=$("#UOMId"+id).val();
			var QuantityId=$("#QuantityId"+id).val();
			
			if(WO_MajorHead==""){
				alert("Please select major head.");
				$("#WO_MajorHead"+id).focus();
				return error=false;
			}
			if(WO_MinorHead==""){
				alert("Please select minor head.");
				$("#WO_MinorHead"+id).focus();
				return error=false;
			}
			if(WO_Desc==""){
				alert("Please select work description.");
				$("#WO_Desc"+id).focus();
				return error=false;
			}
			if(UOMId==""){
				alert("Please select UOM.");
				$("#UOMId"+id).focus();
				return error=false;
			}
			if(QuantityId==""){
				alert("Please select area.");
				$("#Modal-create-wo-popup"+id).modal();
				return error=false;
			}
		}
	});
	return error;
}
//delete scopeof work order
function deleteScopeOfWork(btn){
	$("#newtxtbox"+btn).remove();
}

</script>
</body>
</html>
