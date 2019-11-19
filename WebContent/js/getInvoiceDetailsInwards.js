
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
      .attr("title", "")
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
        
        //var str1 = ele.replace(/.$/,"");
        //alert(str1);
        
        var rowNum = ele.match(/\d+/g);
		//alert(rowNum);
        
        //Removing numbers from the header names
        var str1 = ele.replace(/[0-9]/g, '');
        
        if(str1 == "Product") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubProds(prodId, rowNum);
        	//alert("Products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
        }            
        else if(str1 == "SubProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubSubProducts(prodId, rowNum);
            //alert("Sub products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
        }
        else if(str1 == "ChildProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            //alert("Child products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
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
      .removeClass( "ui-corner-all" )
      .addClass( "custom-combobox-toggle ui-corner-right" )
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
  
  /*$(function() {
	  $("#InvoiceDateId").datepicker({
		  
		  maxDate: new Date
		  
		  });
  });*/
  
  $(function() {
	  $("#expiryDateId1").datepicker({dateFormat: 'dd-M-y'});
  });
  $(function() {
	  $("#poDateId").datepicker({dateFormat: 'dd-M-y'});
  });
  

 
  $(function() {
  	$('#VendorNameId').keypress(function () {
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
		 // alert(data+"_"+status+"_"+er);
		  }
	  });
  	});
  	$('#VendorNameId').on('change', function(){
  		var value = $(this).val();
  		setVendorData (value); //pass the value as paramter
	 });
  });
  
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
	if(request.readyState == 4 && request.status == 200) {
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

//append row to the HTML table
function appendRow() {
	debugger;

	
	var pressedKey = window.event;
	
	//alert(pressedKey.keyCode);
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewItemBtnId")) {
			document.getElementById("hiddenSaveBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
		//alert(hiddenSaveBtn);
		
	    var tbl = document.getElementById("doInventoryTableId");
	    
	 /* 31-AUG-17  var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}*/
	    
//	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowNum = getLastRowNum();
		    
		    document.getElementById("addNewItemBtnId"+rowNum).remove();
		    
		    rowNum = rowNum+1;
		    
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');
		    	//alert("Table Column Name = "+tableColumnName.replace(/ /g,''));	    	
		        createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }
		    $(".tablDisply").hide();
		    var lastDiv = getLastRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("Product"+lastDiv).focus();
		}
	}
}

//Append Row for other charges


function appendChargesRow() {
	debugger;
	
	var chargesrowvalidate=validateChargesRow();
	if(chargesrowvalidate==false){
		return false;
	}
	
	var tbllength=$('#doInventoryChargesTableId').find('tr').length;
	/*alert(tbllength);*/
	if(tbllength==2){
		var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
		var res = tid.split("chargesrow")[1];
		//alert(res);
		$("#addDeleteItemBtnId"+res).show();
	}
	var pressedKey = window.event;
	
	//alert(pressedKey.keyCode);
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewChargesItemBtnId")) {
			document.getElementById("hiddenSaveChargesBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveChargesBtnId").value;
		//alert(hiddenSaveBtn);
		
	    var tbl = document.getElementById("doInventoryChargesTableId");
	    
/*	    var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}
	    
*/	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowChargesNum = getLastChargesRowNum();
		    
		    $("#addNewChargesItemBtnId"+rowChargesNum).hide();
		    
		    rowChargesNum = rowChargesNum+1;
		    var rowid="chargesrow"+rowChargesNum;
			$(row).attr("id", rowid);
		    $(row).attr("class", "chargesrowcls");
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryChargesTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');
		    	//alert("Table Column Name = "+tableColumnName.replace(/ /g,''));	    	
		    	createChargesCell(row.insertCell(i), i, "row", rowChargesNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }
		    
		    var lastDiv = getLastRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("conveyanceCharges"+lastDiv).focus();
		}
	}
}
   
//************ End method for to append data to Other Charges************** 


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

//********** Get Last Row for Charges****************

function getLastChargesRowNum() {
	debugger;
	
	var allElements = document.getElementsByTagName("*");
	
	var allIds = [];
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
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
  

//********************************************************
    	
function deleteRow(btn, currentRow) {
	debugger;	
	var conveyanceTablelength =  $(".chargesrowcls").length;
	var hiddenRowcount = $(".hiddentablestikeout").length;
	var TotalrowCount = conveyanceTablelength - hiddenRowcount;
	var deletedRowscount = $(".hiddentablestikeout").val();
	var canIDelete = window.confirm("Do you want to delete this charges row " + currentRow);
	if(canIDelete == false) {
	    return false;
	}
	var n=0;
	$(".hiddentablestikeout").each(function(){
		 if(deletedRowscount == "R"){
			 n++;
		 }
	});
		if(n == TotalrowCount){
			alert("you can't delete this row.");
			return false;
		}
	
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryChargesTableId').find('tr').length;
	//removing row

	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}
	$("#chargesrow"+currentRow).remove();
	
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calculateOtherCharges();
}

function calculateTotalAmount(qtyNum) {
	
	var qty = "QuantityId"+qtyNum;
	var amnt = "PriceId"+qtyNum;
	var tAmnt = "BasicAmountId"+qtyNum;
	var taxamt="TaxAmountId"+qtyNum;
	var taxper="taxAmountId"+qtyNum;

	var taxper1=document.getElementById(taxper).value;
//	alert(taxper1);
	var quantity = document.getElementById(qty).value;
	//alert(quantity);

	if(quantity == "" || quantity == null || quantity == '') {
		//document.getElementById(qty).focus();
		document.getElementById(qty).removeEventListener("blur", "");
		return false;
	}
	if(quantity == 0 || quantity == 0.0 || quantity == 0.00 || quantity == '0' || quantity == '0.0' || quantity == '0.00' || quantity == "0" || quantity == "0.0" || quantity == "0.00") {
		alert("Please enter valid quantity.");
		document.getElementById(qty).value = "";
		document.getElementById(qty).focus();
		return false;
	}

	var amount = document.getElementById(amnt).value;
	//alert(amount);

	if(amount == "" || amount == null || amount == '') {
		//document.getElementById(amnt).focus();
		document.getElementById(amnt).removeEventListener("blur", "");
		return false;
	}
	
	var totalAmnt = (quantity*amount);
	totalAmnt = Math.round(totalAmnt * 100)/100;
	var taxAmount=(totalAmnt*taxper1)/100;

	document.getElementById(tAmnt).value = totalAmnt;
	document.getElementById(taxamt).value = taxAmount;
	document.getElementById(amnt).removeEventListener("blur", "");
	document.getElementById(tAmnt).focus();
}

function calcuLateFinalAmount() {
	//alert("cal final amount ");
	document.getElementById("hiddenSaveBtnId").value = "";
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			//alert(ask);
			if(ask.indexOf("totalAmount") != -1) {
				//allIds[i] = ask;
				//alert(el.id);
				pipeData = pipeData+(el.id)+"|";
			}
	  	}
	}
	
	var data = pipeData.split("|");
	//alert(data.length);
	
	var lastDiv = data.length-1;
	//alert(lastDiv);
	
	var tempAmnt = 0;
	
	for(var x=0; x < lastDiv; x++) {
    	var fldName = data[x];
    	//alert(fldName);
    	
    	if(fldName.indexOf("totalAmount") != -1) {
    		var fldAmnt = document.getElementById(fldName).value;
    		if(fldAmnt != "" && fldAmnt != null && fldAmnt != '') {
        		tempAmnt = parseFloat(tempAmnt) + parseFloat(fldAmnt);
        		tempAmnt = Math.round(tempAmnt * 100)/100;
    		}
		}	
    }
  //  alert("Final Amount = "+tempAmnt);
	tempAmnt = Math.round(tempAmnt * 100)/100;
	document.getElementById("finalAmntDiv").innerHTML = tempAmnt;
	/* document.getElementById("ttlAmntForIncentEntryId").value = tempAmnt;*/
	$('#invoiceTotalAmount').val(tempAmnt);
}

function getAllProdsCount() {
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {
				var numberPattern = /\d+/g;
				var snosid = ask.match(numberPattern);
				//alert(snosid);
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}

function loadSubProds(prodId, rowNum) {
	
	prodId = prodId.split("$")[0];
	
	var url="indentReceiveSubProducts.spring?mainProductId="+prodId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#SubProduct"+rowNum).val("");
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");				
				$("#TaxId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSelect = "comboboxsubProd"+rowNum;
		    	//alert(subProdToSelect);
		    	
		    	var selectBox = document.getElementById(subProdToSelect);
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById(subProdToSelect) != null && document.getElementById(subProdToSelect).options.length > 0) {
			    	document.getElementById(subProdToSelect).options.length = 0;
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
	
	subProdId = subProdId.split("$")[0];
	
	var url="indentReceiveChildProducts.spring?subProductId="+subProdId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");
				$("#TaxId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
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

function loadUnits(prodId, rowNum) {
	
	//alert("loadUnits --> rowNum = "+rowNum);
	
	prodId = prodId.split("$")[0];
	
	var url = "listIndentReciveUnitsOfChildProducts.spring?productId="+prodId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var unitsToSelect = "UnitsOfMeasurementId"+rowNum;
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
}
//********* Tax calculation for Grid one ***************
function calculateTaxAmount(rowNum) {
	
	var e = document.getElementById("TaxId"+rowNum);
	//alert(e);
	
	var selectedTax = e.options[e.selectedIndex].value;
	//alert(selectedTax);
	
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		//alert("Selected Tax = "+selectedTax);
		
		var basicAmnt = document.getElementById("BasicAmountId"+rowNum).value;
		//alert("Basic Amount = "+basicAmnt);
		
		percentage(basicAmnt, selectedTax, rowNum);
	}
	else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxId"+rowNum).value = "";
	}	
}
//********** End Tax calculation for grid one***************




//********************** Tax calculation GST for second grid*****************

function calculateGSTTaxAmount(rowNum) {

	var e = document.getElementById("GSTTax"+rowNum);
	//alert(e);
	
	var selectedTax = e.options[e.selectedIndex].value;
	//alert(selectedTax);
	
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		//alert("Selected Tax = "+selectedTax);
		
		var basicAmnt = document.getElementById("ConveyanceAmount"+rowNum).value;
		//alert("Basic Amount = "+basicAmnt);
		
		GSTpercentage(basicAmnt, selectedTax, rowNum);
	}
	else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxId"+rowNum).value = "";
	}	
}
function formatChargesColumns(colName) {debugger;
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}


//********************** End Tax calculation GST for second grid*****************
function percentage(basicAmnt, tax, rowNum) {
	
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	//alert("Tax Amount = "+taxAmnt);
	document.getElementById("TaxAmountId"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxId"+rowNum).value = amntaftTx;
}


//************ Calculation for Conveyance AMount*******

function GSTpercentage(basicAmnt, tax, rowNum) {
	console.log(basicAmnt+" "+tax+" "+rowNum)
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	//alert("Tax Amount = "+taxAmnt);
	document.getElementById("GSTAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTax"+rowNum).value = amntaftTx;
}

//*****************************************************


/*function calculateOtherCharges() {

	$('#calculateBtnId').prop('disabled', true);


	var valStatus = validateRowData();
	if(valStatus == false) {
		return false;
	}

	var chargeSno = getChargeTotalSNOS();	 
	var snoSpltdData = chargeSno.split("|");
	var chargeSnoId = snoSpltdData.length - 1;
	
	var tempConveyanceCount = chargeSnoId;
	
	
	
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;

	
	
	var tempProductsCount = snoId;
	
	var previousRecords = new Array();
	var previousRecordsloop = new Array();
	
	
	
	var prevFinalAmnt = 0;
	
	for(var j=0; j<snoId; j++) {
		
		
		var val = spltdData[j];
		
		
		
		var rec1 = document.getElementById("otherOrTransportCharges"+val).value;
		var rec2 = document.getElementById("taxOnOtherOrTransportCharges"+val).value;
		var rec3 = document.getElementById("otherOrTransportChargesAfterTax"+val).value;
		var rec4 = document.getElementById("totalAmount"+val).value;
		prevFinalAmnt = prevFinalAmnt + parseFloat(rec4);
		var finalRecord = rec1+"|"+rec2+"|"+rec3+"|"+rec4;
		previousRecords.push(finalRecord);
	}
	
	//alert("Previous Records Data = "+previousRecords);
	
	prevFinalAmnt = Math.round(prevFinalAmnt * 100)/100;
	//alert("Previous Final Amount = "+prevFinalAmnt);
	
	var tempConCount = 1;
	
	for(var i=0; i<chargeSnoId; i++) {
		
		var rowNumber = snoSpltdData[i];
		
		var conveyanceAmount = document.getElementById("ConveyanceAmount"+rowNumber).value;
		alert("Conveyance Amount = "+conveyanceAmount);
		
		var conveyanceGST = document.getElementById("GSTTax"+rowNumber);
		conveyanceGST = conveyanceGST.options[conveyanceGST.selectedIndex].value;
		if(conveyanceGST != "" && conveyanceGST != null && conveyanceGST != "null") {
			conveyanceGST = conveyanceGST.split("$")[1];
			conveyanceGST = conveyanceGST.substring(0, conveyanceGST.length - 1);
			conveyanceGST = conveyanceGST.trim();
		}
		//alert("Conveyance GST = "+conveyanceGST);
		
		var rsltTtlBscAmnt = calculateTotalBasicAmount();
		//alert("Total Basic Amount = "+rsltTtlBscAmnt);
		
		var fnlAmnt = 0;
		
		var tempCountVar = 0;
		
		
		//alert("snoId "+snoId);
		
		for(var x=0; x<snoId; x++) {

			var val = spltdData[x];
			var individualBasicAmount = document.getElementById("BasicAmountId"+val).value;
			var oOrTransportCharges = document.getElementById("otherOrTransportCharges"+val).value;
			var toOtherOrTransportCharges = document.getElementById("taxOnOtherOrTransportCharges"+val).value;
		
			
			//Setting Total Amount - Start
			var amntAfterTax = document.getElementById("AmountAfterTax"+val).value;
			amntAfterTax = parseFloat(amntAfterTax);
			//alert("Amount After Tax = "+amntAfterTax);
			
			individualBasicAmount = individualBasicAmount.trim();
			individualBasicAmount = parseFloat(individualBasicAmount);
			individualBasicAmount = Math.round(individualBasicAmount * 100)/100;
			
			var charges = (individualBasicAmount * conveyanceAmount) / rsltTtlBscAmnt;
			//charges = Math.round(charges * 100)/100;
			if (snoSpltdData[x] > 1) {
				var existVal = parseFloat(oOrTransportCharges);
				var taxOnOtherCharges = parseFloat(toOtherOrTransportCharges);
			}
			var amount = 0;
			 var result = 0;
			if (existVal == null || existVal == '' || existVal=="") {
			    document.getElementById("otherOrTransportCharges"+val).value = parseFloat(charges).toFixed(2);
			    result = parseFloat(charges).toFixed(2);
			} else {
				existVal = Math.round(existVal * 100)/100;
				result = parseFloat(charges)+parseFloat(existVal);
				document.getElementById("otherOrTransportCharges"+val).value = result.toFixed(2);

			}
			var taxAmount = (charges/100)*conveyanceGST;
			
			if (taxOnOtherCharges == null || taxOnOtherCharges == '' || taxOnOtherCharges=="") {
				var otherChaVal = parseFloat(taxAmount) +  parseFloat(result);
				document.getElementById("taxOnOtherOrTransportCharges"+val).value = taxAmount.toFixed(2);
				document.getElementById("otherOrTransportChargesAfterTax"+val).value =  otherChaVal.toFixed(2);
			} else {
				var addedCharge = parseFloat(taxAmount)+parseFloat(taxOnOtherCharges);
				document.getElementById("taxOnOtherOrTransportCharges"+val).value = addedCharge.toFixed(2);
				var otherChaVal = parseFloat(taxAmount) +  parseFloat(result) +  parseFloat(taxOnOtherCharges);;
				document.getElementById("otherOrTransportChargesAfterTax"+val).value =  otherChaVal.toFixed(2);
			}
		
				
			var othOrTransChrgsAfterTax = document.getElementById("totalAmount"+val).value;
			othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);
			//alert("Other Or Transport Charges After Tax = "+othOrTransChrgsAfterTax);
			
			var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
			ttlAmnt = Math.round(ttlAmnt * 100)/100;
			//alert("Other Or Transport Charges After Tax = "+ttlAmnt);
			
			document.getElementById("totalAmount"+val).value = parseFloat(ttlAmnt).toFixed(2);
			//Setting Total Amount - End
			
			//Setting Final Amount - Start
			fnlAmnt = parseFloat(fnlAmnt);
			fnlAmnt = fnlAmnt + ttlAmnt;
			fnlAmnt = Math.round(fnlAmnt * 100)/100;
			//Setting Final Amount - End
		}
		
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
		document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
		document.getElementById("invoiceTotalAmount").value = fnlAmnt;
		
		
		
		
		alert("fnlAmnt last"+fnlAmnt);
		//Setting Final Amount - End
	}
	}*/
//alert("fnlAmnt last"+fnlAmnt);

/*	document.getElementById("ttlAmntForIncentEntryId").value = fnlAmnt;
}*/



function calculateTotalBasicAmount() {
	
	
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var totalBasicAmount = 0;
	for(var x=0; x<snoId; x++) {
	
		var val = spltdData[x];
		var basicAmnt = document.getElementById("BasicAmountId"+val).value;
		basicAmnt = basicAmnt.trim();
		basicAmnt = parseFloat(basicAmnt);
		basicAmnt = Math.round(basicAmnt * 100)/100;
		totalBasicAmount = parseFloat(totalBasicAmount) + basicAmnt;
		totalBasicAmount = Math.round(totalBasicAmount * 100)/100;
	}
	return totalBasicAmount;
}



function getChargeTotalSNOS() {
	//debugger;
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}

function getTotalSNOS() {
	/*debugger;*/
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
			var el = allElements[i];
	  	if (el.id) {
	  				var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}

/********* Method for adding Transport charges****************/
function getTotalChargesSNOS() {
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}

/****************************************************************/
function getHighestTax() {
	
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;	
	var snoids = [];

	for(var x=0; x<snoId; x++) {	
		var val = spltdData[x];
		var selectedTax = document.getElementById("TaxId"+val);
		selectedTax = selectedTax.options[selectedTax.selectedIndex].value;
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		snoids[x] = selectedTax;
	}
	var sortedSnos = snoids.sort(function(a, b){return a - b;});
	//alert(sortedSnos);
	
	var last_element = sortedSnos[sortedSnos.length - 1];
	return last_element;
}

//Field Validation started
function validateRowData() {	
	  
	var invoiceNum = document.getElementById("InvoiceNumberId").value;
	//alert(invoiceNum);
	
	if(invoiceNum == "" || invoiceNum == null || invoiceNum == '') {
		alert("Please enter Invoice Number.");
		document.getElementById("InvoiceNumberId").focus();
		return false;
	}				
	  
	var invoiceDate = document.getElementById("InvoiceDateId").value;
	//alert(invoiceDate);
	
	if(invoiceDate == "" || invoiceDate == null || invoiceDate == '') {
		alert("Please enter Invoice Date.");
		document.getElementById("InvoiceDateId").focus();
		return false;
	}	  
	  
	var vendorName = document.getElementById("VendorNameId").value;
	//alert(vendorName);
	
	if(vendorName == "" || vendorName == null || vendorName == '') {
		alert("Please enter Vendor Name.");
		document.getElementById("VendorNameId").focus();
		return false;
	} 			
	  
	var vendorAdd = document.getElementById("VendorAddress").value;
	//alert(vendorAdd);
	
	if(vendorAdd == "" || vendorAdd == null || vendorAdd == '') {
		alert("Please enter Vendor Address.");
		document.getElementById("VendorAddress").focus();
		return false;
	}
	  
	var gsinNum = document.getElementById("GSTINNumber").value;
	//alert(gsinNum);
	
	if(gsinNum == "" || gsinNum == null || gsinNum == '') {
		alert("Please enter GRN NO.");
		document.getElementById("GSTINNumber").focus();
		return false;
	}	 
	
    var elementList = document.getElementsByTagName("*");
    
    var rowNums = getAllProdsCount();
    //alert(rowNums);
    
    var splitedRows = rowNums.split("|");
    
    for(var x=0; x < rowNums.length; x++) {
    	
    	var curRow = splitedRows[x];
    	
    	var prodId = "Product"+curRow;    	
    	var subProdId = "SubProduct"+curRow;		
    	var childProdId = "ChildProduct"+curRow;    	
    	var quantityId = "QuantityId"+curRow;    	
    	var unitsOfMeasurementId = "UnitsOfMeasurementId"+curRow;
    	var priceId = "PriceId"+curRow;
    	var basicAmountId = "BasicAmountId"+curRow;
    	var taxId = "TaxId"+curRow;
    	var hSNCodeId = "HSNCodeId"+curRow;
    	
    	for (var i in elementList) {
        	
    		if (elementList[i].id != "") {
	    	  
	    	  if(elementList[i].id == prodId) {
	    		  var pro = document.getElementById(prodId).value;
	    		  if(pro == "" || pro == null || pro == '') {
	    				alert("Please enter Product.");
	    				document.getElementById(prodId).focus();
	    				return false;
	    		  }
	    	  } 
		  	  else if(elementList[i].id == subProdId) {
		  		  var subPro = document.getElementById(subProdId).value;
	    		  if(subPro == "" || subPro == null || subPro == '') {
	    			  alert("Please enter Sub Product.");
	    			  document.getElementById(subProdId).focus();
	    			  return false;
	    		  }
	    	  }  	  
		  	  else if(elementList[i].id == childProdId) {
		  		  var childPro = document.getElementById(childProdId).value;
		  		  if(childPro == "" || childPro == null || childPro == '') {
		  			  alert("Please enter Child Product.");
		  			  document.getElementById(childProdId).focus();
		  			  return false;
		  		  }
	    	  }
	    	  else if(elementList[i].id == quantityId) {
	    		  var qty = document.getElementById(quantityId).value;
	    		  if(qty == "" || qty == null || qty == '' || qty == 0 || qty == '0' || qty == "0") {
	    			  alert("Please enter Quantity.");
	    			  document.getElementById(quantityId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == unitsOfMeasurementId) {
	    		  var units = document.getElementById(unitsOfMeasurementId).value;
	    		  if(units == "" || units == null || units == '') {
	    			  alert("Please enter Units Of Measurement.");
	    			  document.getElementById(unitsOfMeasurementId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == priceId) {
	    		  var amnt = document.getElementById(priceId).value;
	    		  if(amnt == "" || amnt == null || amnt == '' || amnt == 0 || amnt == 0.0 || amnt == '0.0' || amnt == "0.00") {
	    			  alert("Please enter Price.");
	    			  document.getElementById(priceId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == basicAmountId) {
	    		  var cmdxval = document.getElementById(basicAmountId).value;
	    		  if(cmdxval == "" || cmdxval == null || cmdxval == '' || cmdxval == 0.0 || cmdxval == '0.0' || cmdxval == "0.0") {
	    			  alert("Please enter Total Amount.");
	    			  document.getElementById(basicAmountId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == taxId) {
	    		  var taxVal = document.getElementById(taxId).value;
	    		  if(taxVal == "" || taxVal == null || taxVal == '') {
	    			  alert("Please select Tax.");
	    			  document.getElementById(taxId).focus();
	    			  return false;
	    		  }
			 }
			  else if(elementList[i].id == "state") { 
	    		var vendorstate = document.getElementById("state").value;
				if(vendorstate == "" || vendorstate == null || vendorstate == '') {
					alert("Please select State.");
					document.getElementById("state").focus();
					return false;
				}
		  }
	    	 else if(elementList[i].id == hSNCodeId) {
	    		 var hsnCodeVal = document.getElementById(hSNCodeId).value;
	    		 if(hsnCodeVal == "" || hsnCodeVal == null || hsnCodeVal == '') {
	    			 alert("Please enter HSN Code.");
	    			 document.getElementById(hSNCodeId).focus();
	    			 return false;
	    		 }
	    	 }
	      }
    	}
	}
}
//Field Validation started

//Quantity and Price Validation - Start
function validateNumbers(el, evt) {

    var charCode = (evt.which) ? evt.which : event.keyCode;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    //just one dot
    if(number.length > 1 && charCode == 46) {
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
//Quantity and Price Validation - End

function validateProductAvailability(unitsFld) {
	
	var measurementId = unitsFld.id;
	
	var currentRowNum = measurementId.match(/\d+/g);
	//alert(currentRowNum);
	
	measurementId = document.getElementById(measurementId);
	measurementId = measurementId.options[measurementId.selectedIndex].value;
	//alert(measurementId);
	
	measurementId = measurementId.split("$")[0];
	//alert(measurementId);
	
	if(measurementId != "" && measurementId != '' && measurementId != null) {
	
		var pro = $("#combobox"+currentRowNum).val();
		pro = pro.split("$")[0];
		//alert(pro);
		
		var subPro = $("#comboboxsubProd"+currentRowNum).val();
		subPro = subPro.split("$")[0];
		//alert(subPro);
		
		var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		//alert(childPro);	
		
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}
/*============================================================edit===================================*/

function editInvoiceRow(rowId){debugger;
var canIEdit = window.confirm("Do you want to update the Po poduct row " + rowId);
if(canIEdit == false) {
    return false;
}
$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed');

$('#actionValueId'+rowId).val("E");

$('#childProductVendorDesc'+rowId).prop('readonly', false);
$('#quantity'+rowId).prop('readonly', true);
$('#price'+rowId).prop('readonly', true);
//$('#BasicAmountId'+rowId).prop('readonly', false);
$('#Discount'+rowId).prop('readonly', false);
$('#amtAfterDiscount'+rowId).prop('readonly', false);
$('#taxAmountId'+rowId).remove('readonly');
$('#hsnCode'+rowId).prop('readonly', false);
$('#TaxAmount'+rowId).prop('readonly', false);
$('#TaxAftertotalAmount'+rowId).attr('readonly', false);
$('#OtherOrTransportChargesId'+rowId).prop('readonly', false);
$('#TaxOnOtherOrTransportChargesId'+rowId).prop('readonly', false);
$('#OtherOrTransportChargesAfterTaxId'+rowId).prop('readonly',false);
$('#TotalAmountId'+rowId).prop('readonly',false);

$("#addsave").show();

}


/*=============================================================end===================================*/
function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId;
	//alert(url);
	
	var	request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					resp = "0";
				}		    	
		    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = resp;		
			}		
		};
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function getAjaxObject() {
	
	var request = null;
	
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}
	return request;
}

function formatColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}

/*===================================================================cal quantity  base======================================*/

function calculatequantitybase(rowId){debugger;
var Quantity= document.getElementById("quantity"+ rowId).value;
var price= document.getElementById("price"+ rowId).value;

//Calculating the basic amount
var BasicAmonmt=parseFloat(Quantity)*parseFloat(price);
document.getElementById("BasicAmountId"+ rowId).value = BasicAmonmt;

//Calculating the discount
/*var Disocuntval=document.getElementById("Discount"+ rowId).value;
var DiscountamtAfrTax=parseFloat(BasicAmonmt - ( BasicAmonmt*Disocuntval/100 ));
 */
//Appending the discount amount
//document.getElementById("amtAfterDiscount"+ rowId).value = DiscountamtAfrTax;

//taking  the tax
var e1 = document.getElementById("TaxAmountId"+rowId);
//alert(e);

var selectetdtax = e1.options[e1.selectedIndex].value;

if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
	selectedTax1 = selectetdtax.split("$")[1];

	var selectedTaxval1 = selectedTax1;
	var selectedTaxval = parseFloat(selectedTaxval1);
}


//Calculating the tax 
//var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;

//appending the tax amount
//document.getElementById("TaxAmountId"+ rowId).value = taxAmntVal;


var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
document.getElementById("AmountAfterTaxId"+ rowId).value = amontAfterTaxVal;

calculateOtherCharges();




}


/*========================================================================quantity end=====================================================*/
/*=============================================================cal end base total==============================================================*/

function calculateTotalAmount(strSerialNumber) {debugger;
$//("#ChildProduct"+rowNum).val("");
var qty = $("#quantity" +strSerialNumber).val();
var amnt = $("#price"+strSerialNumber).val();

/*var  BAmnt = $("#BasicAmountId"+strSerialNumber).val(tAmnt);*/
var taxamt=$("#taxAmountId"+strSerialNumber).val();
/*alert(taxamt);*/
var tAmnt = 0;
//alert(quantity);



var totalAmnt = (qty*amnt);
totalAmnt = Math.round(totalAmnt * 100)/100;	
//alert(totalAmnt);

tAmnt = parseFloat(totalAmnt);
/*alert("tAmnt"+tAmnt);*/
$("#BasicAmountId"+strSerialNumber).val(tAmnt);
var taxamtafterper=Math.round(tAmnt*taxamt)/100;
/*alert("taxamtafterper"+taxamtafterper);*/
$("#TaxAmountId"+strSerialNumber).val(taxamtafterper);
var aftertaxamount=taxamtafterper+tAmnt;
/*alert("aftertaxamount "+aftertaxamount);*/
$('#AmountAfterTaxId'+strSerialNumber).val(aftertaxamount);
/**/
var a=parseFloat($("#otherOrTransportChargesAfterTax"+strSerialNumber).val());
/*alert("a val finalAmountDiv "+~~a);*/
var totamt=aftertaxamount+a;
$('#totalAmount'+strSerialNumber).val(totamt);
document.getElementById("finalAmntDiv").innerHTML = totamt;
/*alert("totamt "+totamt);*/
/*alert("amt"+amt);*/


//Calculating the discount
var Disocuntval=document.getElementById("Discount"+ strSerialNumber).value;
var DiscountamtAfrTax=parseFloat(tAmnt - ( tAmnt*Disocuntval/100 ));

//Appending the discount amount
//document.getElementById("amtAfterDiscount"+ strSerialNumber).value = DiscountamtAfrTax;

//taking  the tax
var selectetdtax = document.getElementById("taxAmountId"+strSerialNumber).value;
//alert(selectetdtax);
if(selectetdtax==0)
{
	$('#AmountAfterTaxId'+strSerialNumber).val(tAmnt);
}
//commented by thiru
var selectetdtax = e1.options[e1.selectedIndex].value;
//alert(selectetdtax);
if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
	selectedTax1 = selectetdtax.split("$")[1];

	var selectedTaxval1 = selectedTax1;
	var selectedTaxval = parseFloat(selectedTaxval1);
}


//Calculating the tax 
//var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;

//appending the tax amount
//document.getElementById("TaxAmountId"+ strSerialNumber).value = taxAmntVal;



var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
//alert(amontAfterTaxVal);
$('#AmountAfterTaxId'+strSerialNumber).val(amontAfterTaxVal);
document.getElementById("AmountAfterTaxId"+ strSerialNumber).value = amontAfterTaxVal;

calculateOtherCharges();


};	



/*============================================================cal end======================================================*/


//********* Tax calculation for Grid one ***************
function calculateTaxAmount(rowNum) {
	debugger;
	var e = document.getElementById("TaxAmountId"+rowNum);
	//alert(e);

	var selectedTax = e.options[e.selectedIndex].value;
	//alert(selectedTax);

	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		/*selectedTax = selectedTax.substring(0, selectedTax.length - 1);*/
		/*selectedTax = selectedTax.trim();*/
		//alert("Selected Tax = "+selectedTax);

		var basicAmnt = document.getElementById("BasicAmountId"+rowNum).value;
		//alert("Basic Amount = "+basicAmnt);

		percentage(basicAmnt, selectedTax, rowNum);
	}
	else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxId"+rowNum).value = "";
	}	
	calculateOtherCharges();


}
//********** End Tax calculation for grid one***************

/*	==========================================final amt======================================================*/
var n=0;
function calculateOtherCharges() {


	n++;
	//validating all charges row inputs
	var chargesRow=validateChargesRow();
	if(chargesRow==false){
		return false;
	}
	var valStatus = validateRowData(1);
	if(valStatus == false) {
		return false;
	}
	
	var resetSno = getTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	var resetSnoId = resetSpltdData.length - 1;
	for(var x=0; x<resetSnoId; x++) {

		var resetval = resetSpltdData[x];


		document.getElementById("otherOrTransportCharges"+resetval).value = "";
		document.getElementById("taxOnOtherOrTransportCharges"+resetval).value = "";
		document.getElementById("otherOrTransportChargesAfterTax"+resetval).value = "";
		document.getElementById("totalAmount"+resetval).value = "";
	}

	var convenceCharges=0;
	var convenceTaxAmt=0;
	var convenceAmtAfterTax=0;

	var chargeSno = getChargeTotalSNOS();
	var snoSpltdData = chargeSno.split("|");
	var chargeSnoId = snoSpltdData.length - 1;
//	alert("2 "+chargeSnoId);
	for(var y=0; y<chargeSnoId; y++) {
		//alert("y vsalue  "+y);
		var chargeVal = snoSpltdData[y];
		//alert(chargeVal);
		convenceCharges = parseFloat($('#ConveyanceAmount'+chargeVal).val().trim());
		//alert("convenceCharges"+convenceCharges);
		convenceTaxAmt = $('#GSTTax'+chargeVal).val().trim();
		//alert("convenceTaxAmt"+convenceTaxAmt);
		var array = convenceTaxAmt.split("$");
		var taxPercentage = array[1];
		taxPercentage = taxPercentage.replace("%", "");

		console.log("taxPercentage "+taxPercentage);
		convenceCharges = Math.round(convenceCharges * 100)/100;
		var rsltTtlBscAmnt = calculateTotalBasicAmount();

		var snos = getTotalSNOS();
//		alert(snos);

//		BasicAmountId1
		var spltdData = snos.split("|");
		var snoId = spltdData.length - 1;
//		var individualBasicAmount = 0;

		var fnlAmnt = 0;


		for(var x=0; x<snoId; x++) {

			var val = spltdData[x];
			var individualBasicAmount = document.getElementById("BasicAmountId"+val).value;


			//	alert("individualBasicAmount  "+individualBasicAmount);

			individualBasicAmount = individualBasicAmount.trim();
			individualBasicAmount = parseFloat(individualBasicAmount);
			individualBasicAmount = Math.round(individualBasicAmount * 100)/100;
			//	alert(individualBasicAmount);
			var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
			//	alert("charges"+charges);
			//charges = Math.round(charges * 100)/100;

			//alert(snoSpltdData[y]);
			if (snoSpltdData[y] > 1) {
				var existVal = $("#otherOrTransportCharges"+val).val();
				var taxOnOtherCharges = $("#taxOnOtherOrTransportCharges"+val).val();
			}
			var amount = 0;
			 var result = 0;
			if (existVal == null || existVal == '' || existVal=="") {
			    document.getElementById("otherOrTransportCharges"+val).value = parseFloat(charges).toFixed(2);
			    result = parseFloat(charges).toFixed(2);
			} else {
				existVal = Math.round(existVal * 100)/100;
				result = parseFloat(charges)+parseFloat(existVal);
				document.getElementById("otherOrTransportCharges"+val).value = result.toFixed(2);

			}
			var taxAmount = (charges/100)*taxPercentage;
			
			if (taxOnOtherCharges == null || taxOnOtherCharges == '' || taxOnOtherCharges=="") {
				var otherChaVal = parseFloat(taxAmount) +  parseFloat(result);
				document.getElementById("taxOnOtherOrTransportCharges"+val).value = taxAmount.toFixed(2);
				document.getElementById("otherOrTransportChargesAfterTax"+val).value =  otherChaVal.toFixed(2);
			} else {
				var addedCharge = parseFloat(taxAmount)+parseFloat(taxOnOtherCharges);
				document.getElementById("taxOnOtherOrTransportCharges"+val).value = addedCharge.toFixed(2);
				var otherChaVal = parseFloat(taxAmount) +  parseFloat(result) +  parseFloat(taxOnOtherCharges);;
				document.getElementById("otherOrTransportChargesAfterTax"+val).value =  otherChaVal.toFixed(2);
			}

			//Setting Total Amount - Start		

			//	var amntAfterTax = document.getElementById("TaxAftertotalAmount"+val).value;
			var amntAfterTax = document.getElementById("AmountAfterTaxId"+val).value;

			//	alert("TaxAftertotalAmount  "+amntAfterTax);

			amntAfterTax = parseFloat(amntAfterTax);
			//alert("Amount After Tax = "+amntAfterTax);

			var othOrTransChrgsAfterTax = document.getElementById("otherOrTransportChargesAfterTax"+val).value;
			othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);
			//alert("Other Or Transport Charges After Tax = "+othOrTransChrgsAfterTax);

			var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
			ttlAmnt = Math.round(ttlAmnt * 100)/100;
			//alert("Other Or Transport Charges After Tax = "+ttlAmnt);

			document.getElementById("totalAmount"+val).value = parseFloat(ttlAmnt).toFixed(2);
			//Setting Total Amount - End

			//Setting Final Amount - Start
			fnlAmnt = parseFloat(fnlAmnt);
			fnlAmnt = fnlAmnt + ttlAmnt;
			fnlAmnt = Math.round(fnlAmnt * 100)/100;
			//Setting Final Amount - End
		}
//		Setting Final Amount - Start
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
		document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
		document.getElementById("invoiceTotalAmount").value = fnlAmnt;
	
		
//		Setting Final Amount - End
	}
}

function validateChargesRow(){
	//debugger;
	var error=true;
	$(".chargesrowcls").each(function(){
		var currentId=$(this).attr("id").split("chargesrow")[1];
		if($("#Conveyance"+currentId).val()==''){
			alert("please select Conveyance type.");
			$("#Conveyance"+currentId).focus();
			return  error=false;
		}
		if($("#ConveyanceAmount"+currentId).val()==''){
			alert("please enter Conveyance Amount.");
			$("#ConveyanceAmount"+currentId).focus();
			return  error=false;
		}
		if($("#GSTTax"+currentId).val()==''){
			alert("please select GST Tax.");
			$("#GSTTax"+currentId).focus();
			return  error=false;
		}		
	})
	return  error;
}


/*	===============================================================final end=====================================*/
function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	var chargesRow=validateChargesRow();
	if(chargesRow==false){
		return false;
	}	
	var chargesRowCountNum=[];
	$(".chargesrowcls").each(function(){
		var currentId=$(this).attr("id").split("chargesrow")[1];	
		chargesRowCountNum.push(currentId);
	});
	debugger;
	//console.log("chargesRowCountNum: "+chargesRowCountNum);
	$('#chargesLength').val(chargesRowCountNum);
	calculateOtherCharges();
	
	var result=validateFileExtention();
	  if(result==false){
		  return false;
	  }
	var canISubmit = window.confirm("Do you want to Update?");
	
	if(canISubmit == false) {
		return false;
	}
	excuteDeleteFunction();
	
	//alert("before saving "+getAllProdsCount());
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfTransRowsRows").value = getChargeTotalSNOS();
	document.getElementById("doInventoryUpdateFormId").action = "updateInvoice.spring";
	document.getElementById("doInventoryUpdateFormId").method = "POST";
	document.getElementById("doInventoryUpdateFormId").submit();
}

//edit transfort charges row

function editchargesInvoiceRow(rowId){debugger;
var canIEdit = window.confirm("Do you want to update this transportation charges" + rowId);
if(canIEdit == false) {
    return false;
}	
$("#Conveyance"+rowId).attr('readonly', false);
$("#ConveyanceAmount"+rowId).attr('readonly', false);
$("#GSTTax"+rowId).attr('readonly', false);
$("#TransportInvoice"+rowId).attr('readonly', false);
$('#transactionActionValue'+rowId).val("E");

}

//remove transport charges row as temporarily 

function removeTransRow(rowId){debugger;
var conveyanceTablelength =  $(".chargesrowcls").length;
var hiddenRowcount = $(".hiddentablestikeout").length;
var TotalrowCount = conveyanceTablelength - hiddenRowcount;
var deletedRowscount = $(".hiddentablestikeout").val();
var canIDelete = window.confirm("Do you want to cancel this transportation charges.");
if(canIDelete == false) {
    return false;
}
var n=0;
$(".hiddentablestikeout").each(function(){
	 if(deletedRowscount == "D"){
		 n++;
	 }
});
	if(n == TotalrowCount){
		alert("you cant delete this row.");
		return false;
	}

$("#chargesrow"+rowId).addClass('strikeout');
$("#addremoveChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editchargesItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoChargesDivId'+rowId).removeAttr('id');
$('#transactionActionValue'+rowId).val("R");

}
