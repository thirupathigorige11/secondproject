
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
            this._trigger( "select", event, {
                item: ui.item.option
              });
        	//alert("Products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
        }            
        else if(str1 == "SubProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubSubProducts(prodId, rowNum);
            this._trigger( "select", event, {
                item: ui.item.option
              });
            //alert("Sub products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
        }
        else if(str1 == "ChildProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
        	var tablelength=$("#doInventoryTableId > tbody > tr").length;
          	if(tablelength==1){
          		 this._trigger( "select", event, {
                       item: ui.item.option
                     });
          		 loadUnits(prodId, rowNum);  
          		 
          	}
          	else{
          		var childstatus=childcampare(prodName, rowNum);
          		if(childstatus==true){
                  	this._trigger( "select", event, {
                          item: ui.item.option
                        }); 
                  	 loadUnits(prodId, rowNum); 
                  }
                  else{
                  	 //loadUnits(prodId, rowNum); 
                  	$("#ChildProduct"+rowNum).val('');
                  	var emptychild=$("#ChildProduct"+rowNum).val();
                  	loadUnits(emptychild, rowNum); 
                  	return false;
                  }
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
  
  $(function() {
	  $("#InvoiceDateId").datepicker({ 
		  dateFormat: 'dd-M-y',
		  maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
   //   showButtonPanel: true,
   // maxDate: '@maxDate',
   //   minDate: '@minDate'  
	  });
  });
  
  $(function() {
	  $("#expireDateId1").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:0,
		  changeMonth: true,
	      changeYear: true
	  });
  });
  $(function() {
	  $("#poDateId").datepicker({
		  
		 dateFormat: 'dd-M-y',
		 maxDate: new Date(),
		  changeMonth: true,
	      changeYear: true
	  });
  });
  $(function() {
	  var staffId=$("#hiddenPODate").val();
	  
	  $("#receivedDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate: '-'+ staffId+'d',
		 maxDate: new Date() ,
		 onSelect: function(dateText) {
		    // alert("Selected date: " + dateText + "; input's current value: " + this.value);
		 	
		 $("#saveBtnId").disabled = true;
		 var invoiceId=$("#InvoiceNumberId").val();
		 var vendname=$("#vendorIdId").val();
		 var recedate=dateText;
		 var vName = "VND03";
	/*	 var requestData = {
				 vendorId: invoiceId,
				 venName: vendname,
				 receiDate: recedate
		 };*/
		// var url = "loadAndSetVendorInfo.spring?vendName="+vName;
		 var url = "getReceiveCount.spring?invoiceNumber="+invoiceId+"&vendorname="+vendname+"&receiveDate="+recedate;
		 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			 	 if (data>0){
					 //server sent response as false, not valid data
					$("#errorMessageInvoiceNumber").show();
					 $("#InvoiceNumberId").focus();
					 $('.btn-visibilty').attr('disabled', 'disabled');
	  				    $('.btn-visibilty1').closest('td').find('input').attr('disabled', 'disabled');
	 				    $('.btn-visibilty1').closest('td').find('.custom-combobox-toggle').addClass('hide');
					 $("#doInventoryTableId").find("input,button,textarea,select").attr("disabled", "disabled");
					//$("#doInventoryTableId").find("*").attr("disabled", "disabled");
					 $("#saveBtnId").disabled = true;
					 
					 //hide error message
					// $("#errorMessageInvoiceNumber").hide();
				 }
		  },
		  error:  function(vName, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
		 
		 
	}
	  });
  });
/*************************************************************indent number  and po start******************************************************/
  function validateIndentNum(){//alert();
  
		 var indentNumber=$("#indentNumber").val();

		 
		 var typeOfPurchase=$("#typeOfPurchase").val();
		 
		 if(typeOfPurchase=='marketPurchase'){
		 
		 var url = "getCheckIndentAvailable.spring?indentNumber="+indentNumber;
		 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {debugger;
			 	 if (data==0){
			 		 //alert("iam from data 0")
					 //server sent response as false, not valid data
					$("#errorMessageIndentNumber").show();
					 $("#indentNumber").focus();
					 $('.btn-visibilty').attr('disabled', 'disabled');
	  				    $('.btn-visibilty1').closest('td').find('input').attr('disabled', 'disabled');
	 				    $('.btn-visibilty1').closest('td').find('.custom-combobox-toggle').addClass('hide');
					    $("#doInventoryTableId").find("input,button,textarea,select").attr("disabled", "disabled");
					//$("#doInventoryTableId").find("*").attr("disabled", "disabled");
					 $("#saveBtnId").disabled = true;
				 }
			 	 else{
			 		//alert("iam not from data 0");
			 		 $("#errorMessageIndentNumber").hide();
			 		 $('.btn-visibilty').removeAttr('disabled', 'disabled');
	  				 $('.btn-visibilty1').closest('td').find('input').removeAttr('disabled', 'disabled');
	 				 $('.btn-visibilty1').closest('td').find('.custom-combobox-toggle').removeClass('hide');
			 		 $("#doInventoryTableId").find("input,button,textarea,select").removeAttr("disabled");
			 	 }
		  },
		  error:  function(vName, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
		 
		 }
		};

 function validatePoNum(){//alert();
			  
	 
	 
	 var poNumber=$("#poNumber").val();
	 var vendorId=$("#vendorIdId").val();
	 var podate=$("#poDateId").val();

	// alert(podate);
	 var url = "getCheckPoAvailable.spring?poNumber="+poNumber+"&vendorId="+vendorId;
	 $.ajax({
	  url : url,
		 //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "post",
	 contentType : "application/json",
	 success : function(data) {debugger;
		if (data==0){
							 //server sent response as false, not valid data
				$("#errorMessagePONumber").show();
							 $("#poNumber").focus();
							 $('.btn-visibilty').attr('disabled', 'disabled');
			  				    $('.btn-visibilty1').closest('td').find('input').attr('disabled', 'disabled');
			 				    $('.btn-visibilty1').closest('td').find('.custom-combobox-toggle').addClass('hide');
							 $("#doInventoryTableId").find("input,button,textarea,select").attr("disabled", "disabled");
							// $('#VendorNameId').attr('disabled', 'disabled');
							 //$('#poDateId').attr('disabled', 'disabled');
							//$("#doInventoryTableId").find("*").attr("disabled", "disabled");
							 $("#saveBtnId").disabled = true;
						 }
				  },
				  error:  function(vName, status, er){
					  alert(data+"_"+status+"_"+er);
					  }
	  });
	 
	  };  // method end
		  
		  
  
  
  
  /**************************************************************indentNumber and po End********************************************************/
 
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
		  alert(data+"_"+status+"_"+er);
		  }
	  });
  	});
  	$('#VendorNameId').on('change', function(){
  		var value = $(this).val();
  		
  		value = value.replace("&", "$$$");
  		//alert(value);
  		
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
function appendRow(currentRow) {
	
	 var valRowStatus= validateRowData();
		
	    if(valRowStatus == false) {
	    	return false;
		}
	debugger;
	var tbllength=$('#doInventoryTableId').find('tr').length;
	/*alert(tbllength);*/
	if(tbllength==2){
		var tid=$('#doInventoryTableId tbody tr:last').attr('id');		
		var res = tid.split("productchargesrow")[1];
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
		//alert(hiddenSaveBtn);
		
	    var tbl = document.getElementById("doInventoryTableId");
	    
/*	    var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}*/
	    
	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowNum = getLastRowNum();
		    
		    $("#addNewItemBtnId"+rowNum).hide();
		    
		    rowNum = rowNum+1;
		    var rowid="productchargesrow"+rowNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "producttable");
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
		    
		    var lastDiv = getLastRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("Product"+lastDiv).focus();
		}
	}
}

//Append Row for other charges


function appendChargesRow(currentRow) {
debugger;
var Conveyance=$("#Conveyance"+currentRow).val();
var ConveyanceAmount=$("#ConveyanceAmount"+currentRow).val();
var GSTTax=$("#GSTTax"+currentRow).val();

if(Conveyance==""){
	alert("Please select Conveyance.");
	return false;
}
if(ConveyanceAmount==""){
	alert("Please Enter Conveyance Amount");
	return false;
}
if(GSTTax==""){
	alert("Please select GSTTax.");
	return false;
}
var tbllength=$('#doInventoryChargesTableId').find('tr').length;
/*alert(tbllength);*/
if(tbllength==2){
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
	$("#addDeleteChargesItemBtnId"+res).show();
}
	var pressedKey = window.event;
	
	//alert(pressedKey.keyCode);
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewChargesItemBtnId1")) {
			document.getElementById("hiddenSaveChargesBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveChargesBtnId").value;
		//alert(hiddenSaveBtn);
		
	    var tbl = document.getElementById("doInventoryChargesTableId");
	    
	    var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}
	    
	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowChargesNum = getLastChargesRowNum();
		    
		   $("#addNewChargesItemBtnId"+rowChargesNum).hide();
		    
		    rowChargesNum = rowChargesNum+1;
		    //ading id to current row
		    var rowid="chargesrow"+rowChargesNum;
			$(row).attr("id", rowid);
		    
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryChargesTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');
		    	//alert("Table Column Name = "+tableColumnName.replace(/ /g,''));	    
		    	/*row.insertCell(i).setAtt("id", rowChargesNum);*/
		    	createChargesCell(row.insertCell(i), i, "row", rowChargesNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }
		    
		    var lastDiv = getLastChargesRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("Conveyance"+lastDiv).focus();
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
		$("#addDeleteChargesItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calculateOtherCharges();
}

//deleting product table 
function deleteproductRow(btn, currentRow) {
	debugger;

	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryTableId').find('tr').length;
	//removing row
	$("#productchargesrow"+currentRow).remove();
	
	var tid=$('#doInventoryTableId tbody tr:last').attr('id');
	/*var tid=$("#doInventoryTableId").find("tr").length + 1*/
	
	var res = tid.split("productchargesrow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
		/*return false;*/
	}
	if(res<currentRow){
		$("#addNewItemBtnId"+res).show();
	}
	calculateOtherCharges();
}
/*************start cal price amount *******************/
function calculatePriceAmount(qtyNum) {
	var qty = "QuantityId"+qtyNum;
	var tAmnt = "BasicAmountId"+qtyNum;
	
	
	var price = "PriceId"+qtyNum; 
	
	
	
	var quantity = document.getElementById(qty).value;
	if(quantity == "" || quantity == null || quantity == '') {
		document.getElementById(qty).removeEventListener("blur", "");
		return false;
	}
	
	if(quantity == 0 || quantity == 0.0 || quantity == 0.00 || quantity == '0' || quantity == '0.0' || quantity == '0.00' || quantity == "0" || quantity == "0.0" || quantity == "0.00") {
		alert("Please enter valid quantity.");
		document.getElementById(qty).value = "";
		document.getElementById(qty).focus();
		return false;
	}
	
	
	
	var amount = document.getElementById(tAmnt).value;
	
	if(amount == "" || amount == null || amount == '') {
		document.getElementById(tAmnt).removeEventListener("blur", "");
		return false;
	}
	
	
	var basicAmt = (amount/quantity);
	basicAmt = Math.round(basicAmt * 100)/100;
	
	document.getElementById(price).value = basicAmt;
	
	
}
/*************end cal price amount *******************/

function calculateTotalAmount(qtyNum) {
	
	var qty = "QuantityId"+qtyNum;
	var amnt = "PriceId"+qtyNum;
	var tAmnt = "BasicAmountId"+qtyNum;
	
	validateUnitsAndAvailability(qtyNum);
	
	var quantity = document.getElementById(qty).value;
	//alert(quantity);
	
	if(quantity == "" || quantity == null || quantity == '') {
		//document.getElementById(qty).focus();
		//document.getElementById(qty).removeEventListener("blur", "");
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
		//document.getElementById(amnt).removeEventListener("blur", "");
		return false;
	}
	
	var totalAmnt = (quantity*amount);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	//alert(totalAmnt);
	
	document.getElementById(tAmnt).value = totalAmnt;
	//document.getElementById(amnt).removeEventListener("blur", "");
	//document.getElementById(tAmnt).focus();
	calculateTaxAmount(qtyNum);
}

function calcuLateFinalAmount() {
	
	document.getElementById("hiddenSaveBtnId").value = "";
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			//alert(ask);
			if(ask.indexOf("TotalAmountId") != -1) {
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
    	
    	if(fldName.indexOf("TotalAmountId") != -1) {
    		var fldAmnt = document.getElementById(fldName).value;
    		if(fldAmnt != "" && fldAmnt != null && fldAmnt != '') {
        		tempAmnt = parseFloat(tempAmnt) + parseFloat(fldAmnt);
        		tempAmnt = Math.round(tempAmnt * 100)/100;
    		}
		}	
    }
    //alert("Final Amount = "+tempAmnt);
	tempAmnt = Math.round(tempAmnt * 100)/100;
    document.getElementById("finalAmntDiv").innerHTML = tempAmnt;
    document.getElementById("ttlAmntForIncentEntryId").value = tempAmnt;
}

/*START 31-AUG-17*/
function getAllChargesCount() {
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				var numberPattern = /\d+/g;
				var snosid = ask.match(numberPattern);
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}
/*END 31-AUG-17*/
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
	
	//var myOpts = document.getElementById("receivedDate").options;
	
	//alert("please enter the Received date");
	
	var requestedDate=$("#receivedDate").val();
	if(requestedDate == "" || requestedDate == '' || requestedDate == null) {
		alert("Please Choose Received Date .");
		document.getElementById("requestedDate").focus();
	//alert(requestedDate);
	}
	
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
			    		$("#groupId"+rowNum).val(data[2]);
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
	debugger;
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
	debugger;
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
		
		GSTpercentage(basicAmnt, selectedTax, rowNum);
	}
	else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxxId"+rowNum).value = "";
	}	
}
function formatChargesColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}


//********************** End Tax calculation GST for second grid*****************
function percentage(basicAmnt, tax, rowNum) {
	debugger;
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
	debugger;
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	//alert("Tax Amount = "+taxAmnt);
	document.getElementById("GSTAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxx"+rowNum).value = amntaftTx;
}

//*****************************************************
function calculateOtherCharges() {

	
	
/*	var valStatus = validateRowData();
	if(valStatus == false) {
    	return false;
	}*/
    var transportDetails = document.getElementById("Conveyance1").value;
    if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
		alert("Please Select the Conveynace or None .");
		document.getElementById("Conveyance1").focus();
		return false;
	}
	
	
	
	var resetSno = getTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	var resetSnoId = resetSpltdData.length - 1;
	for(var x=0; x<resetSnoId; x++) {

		var resetval = resetSpltdData[x];
		 document.getElementById("OtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("TaxOnOtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("OtherOrTransportChargesAfterTaxId"+resetval).value = "";
		 document.getElementById("TotalAmountId"+resetval).value = "";
	}
	
	 var convenceCharges=0;
	 var convenceTaxAmt=0;
	 var convenceAmtAfterTax=0;
	 
	 var chargeSno = getChargeTotalSNOS();
		var snoSpltdData = chargeSno.split("|");
		var chargeSnoId = snoSpltdData.length - 1;

		for(var y=0; y<chargeSnoId; y++) {
		
			var chargeVal = snoSpltdData[y];
			 convenceCharges = parseFloat($('#ConveyanceAmount'+chargeVal+'').val().trim());
			 convenceTaxAmt = $('#GSTTax'+chargeVal+'').val().trim();
			 var array = convenceTaxAmt.split("$");
			 var taxPercentage = array[1];
			 taxPercentage = taxPercentage.replace("%", "");

	 console.log("taxPercentage "+taxPercentage);
	 convenceCharges = Math.round(convenceCharges * 100)/100;
	var rsltTtlBscAmnt = calculateTotalBasicAmount();
	
	var snos = getTotalSNOS();
	//alert(snos);
	
	//BasicAmountId1
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	//var individualBasicAmount = 0;
	
	var fnlAmnt = 0;

	
	for(var x=0; x<snoId; x++) {

		var val = spltdData[x];
		var individualBasicAmount = document.getElementById("BasicAmountId"+val).value;
	
		individualBasicAmount = individualBasicAmount.trim();
		individualBasicAmount = parseFloat(individualBasicAmount);
		individualBasicAmount = Math.round(individualBasicAmount * 100)/100;
		
		var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		//charges = Math.round(charges * 100)/100;
		if (snoSpltdData[y] > 1) {
			var existVal = $("#OtherOrTransportChargesId"+val).val();
			var taxOnOtherCharges = $("#TaxOnOtherOrTransportChargesId"+val).val();
		}
		var amount = 0;
		 var result = 0;
		if (existVal == null || existVal == '' || existVal=="") {
		    document.getElementById("OtherOrTransportChargesId"+val).value = parseFloat(charges).toFixed(2);
		    result = parseFloat(charges).toFixed(2);
		} else {
			existVal = Math.round(existVal * 100)/100;
			result = parseFloat(charges)+parseFloat(existVal);
			document.getElementById("OtherOrTransportChargesId"+val).value = result.toFixed(2);

		}
		var taxAmount = (charges/100)*taxPercentage;
		
		if (taxOnOtherCharges == null || taxOnOtherCharges == '' || taxOnOtherCharges=="") {
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result);
			document.getElementById("TaxOnOtherOrTransportChargesId"+val).value = taxAmount.toFixed(2);
			document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		} else {
			var addedCharge = parseFloat(taxAmount)+parseFloat(taxOnOtherCharges);
			document.getElementById("TaxOnOtherOrTransportChargesId"+val).value = addedCharge.toFixed(2);
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result) +  parseFloat(taxOnOtherCharges);;
			document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		}
		
		//Setting Total Amount - Start
		var amntAfterTax = document.getElementById("AmountAfterTaxId"+val).value;
		amntAfterTax = parseFloat(amntAfterTax);
		//alert("Amount After Tax = "+amntAfterTax);
			
		var othOrTransChrgsAfterTax = document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value;
		othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);
		//alert("Other Or Transport Charges After Tax = "+othOrTransChrgsAfterTax);
		
		var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
		ttlAmnt = Math.round(ttlAmnt * 100)/100;
		//alert("Other Or Transport Charges After Tax = "+ttlAmnt);
		
		document.getElementById("TotalAmountId"+val).value = parseFloat(ttlAmnt).toFixed(2);
		//Setting Total Amount - End
		
		//Setting Final Amount - Start
		fnlAmnt = parseFloat(fnlAmnt);
		fnlAmnt = fnlAmnt + ttlAmnt;
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
		//Setting Final Amount - End
	}
	//Setting Final Amount - Start
	fnlAmnt = Math.round(fnlAmnt * 100)/100;
	document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
	//Setting Final Amount - End
}
}

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
		alert("Please Choose Invoice Date.");
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
	
    var receivedDate = document.getElementById("receivedDate").value;
    if(receivedDate == "" || receivedDate == '' || receivedDate == null) {
		alert("Please Choose ReciveD Date .");
		document.getElementById("receivedDate").focus();
		return false;
	}
    var state = document.getElementById("state").value;
    if(state == "" || state == '' || state == null) {
		alert("Please Choose one option .");
		document.getElementById("state").focus();
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
		var requestedDate=$("#receivedDate").val();
		var groupId = $("#groupId"+currentRowNum).val();
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum,requestedDate,groupId);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum,requestedDate,groupId) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requestedDate+"&groupId="+groupId+"&isReceive=true";
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
				var spltData = resp.split("_");				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	//document.getElementById("ProductAvailabilityId"+currentRowNum).value = resp[0];		
		    	$("#ProductAvailabilityId"+currentRowNum).val(available[0]);
		    	
	    		$("#totalQuantity"+currentRowNum).val(available[1]);
	    		$("#BOQQty"+currentRowNum).val(available[2]);
			}		
		};
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

	/***************************** this is used to test the boq quantity start*********************************************************/
function validateUnitsAndAvailability(currentRowNum) {
	debugger;
	//var qtyObjectId = qtyObj.id;	
	//var currentRowNum = qtyObjectId.match(/\d+/g);	
	var qty = "";
	var productAva = "";	
	qty = document.getElementById("QuantityId"+currentRowNum).value;
	qty = parseFloat(qty);
	var allSiteIds=$("#allSiteIds").val().split(",");
	var siteId=$("#siteIdId").val();
	var groupId=$("#groupId"+currentRowNum).val();
	if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
		alert("Please enter valid quantity.");
		document.getElementById("QuantityId"+currentRowNum).value = "";
		document.getElementById("QuantityId"+currentRowNum).focus();
		return false;
	}
	var count=0;
	for (var i = 0; i < allSiteIds.length; i++) {
		if(allSiteIds[i]==siteId){
			count++;
		}
	}
	if(count==0){
		console.log(" Not Present");
		var totalQty=parseFloat($("#totalQuantity"+currentRowNum).val());
		var BOQQty=parseFloat($("#BOQQty"+currentRowNum).val()).toFixed(3);
		var matchedEnteredQty=0;
		$(".producttable").each(function(){
			var id=$(this).attr("id").split("productchargesrow")[1];
			var currentGroupId=$("#groupId"+id).val();
			if(currentGroupId==groupId){
				matchedEnteredQty+=parseFloat($("#QuantityId"+id).val()==""?0:$("#QuantityId"+id).val());
			}			
		})
		debugger;
		matchedEnteredQty=matchedEnteredQty.toFixed(3);
		if(parseFloat(matchedEnteredQty)>parseFloat(totalQty) && groupId!='0'){
			alert("You can not initiate Child Product "+$("#comboboxsubSubProd"+currentRowNum).val()+" more than "+BOQQty+" "+$("#UnitsOfMeasurementId"+currentRowNum).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
			$("#QuantityId"+currentRowNum).val('');
			$("#QuantityId"+currentRowNum).focus();
			return false;
		}
		
	}else{
		console.log("  Present");
		
	}
	productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
	productAva = parseFloat(productAva);	
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

function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	calculateOtherCharges();
	var SiteId=$("#SiteId").val();
	var valStatus = appendRow();
	 var valRowStatus= validateRowData();
	
	    if(valRowStatus == false) {
	    	return false;
		}
	if(valStatus == false) {
    	return;
	}
	
    var elementList = document.getElementsByTagName("*");
    
    var rowNums = getAllProdsCount();
    //alert(rowNums);
    
    var splitedRows = rowNums.split("|");
    
    for(var x=0; x < rowNums.length; x++) {
    	
    	var curRow = splitedRows[x];
    	
         var expireDateId = "expireDateId"+curRow;
    	
    	for (var i in elementList) {
        	
    		if (elementList[i].id != "") {
    	
    			if(elementList[i].id == expireDateId) {
  	    		  var expireDateId = document.getElementById(expireDateId).value;
  	    		  if(expireDateId == "" || expireDateId == null || expireDateId == '') {
  	    				alert("Please Choose Expire Date.");
  	    				document.getElementById(expireDateId).focus();
  	    				return false;
  	    		  }
  	    		 var invoiceDate = document.getElementById("InvoiceDateId").value;
  	    	    if(invoiceDate == "" || invoiceDate == '' || invoiceDate == null) {
  	    			alert("Please Choose invoice Date .");
  	    			document.getElementById("InvoiceDateId").focus();
  	    			return false;
  	    		}
  	    		  
  	    	  var vendorName = document.getElementById("VendorNameId").value;
	    	    if(vendorName == "" || vendorName == '' || vendorName == null) {
	    			alert("Please Choose vendor Name  .");
	    			document.getElementById("VendorNameId").focus();
	    			return false;
	    		}
  	    	    
	    	    var state = document.getElementById("state").value;
	    	    if(state == "" || state == '' || state == null) {
	    			alert("Please Choose state  .");
	    			document.getElementById("state").focus();
	    			return false;
	    		}
	    	    
	    	    var invoiceNumber = document.getElementById("InvoiceNumberId").value;
	    	    if(invoiceNumber == "" || invoiceNumber == '' || invoiceNumber == null) {
	    			alert("Please Choose Invoice Number  .");
	    			document.getElementById("InvoiceNumberId").focus();
	    			return false;
	    		}
	    	    var typeOfPurchase = document.getElementById("typeOfPurchase").value;
	    	    if(typeOfPurchase == "" || typeOfPurchase == '' || typeOfPurchase == null) {
	    			alert("Please Choose Indent Or Po");
	    			document.getElementById("typeOfPurchase").focus();
	    			return false;
	    		}
	    	    var transporterName = document.getElementById("transporterNameIdId").value;
	    		if(transporterName == "" || transporterName == null || transporterName == '') {
	    			alert("Please select the transportorName.");
	    			document.getElementById("transporterNameIdId").focus();
	    			return false;
	    		}
	    	    
	    	    //alert(" before type of purchase");
	    	    
	    	    var typeOfPurchase = document.getElementById("typeOfPurchase").value;
	    	    var finalAmntDiv = document.getElementById("finalAmntDiv").innerText;
	    	    var vendorId=$("#vendorIdId").val();
	    	    //var finalAmntDiv = $("#finalAmntDiv").val();
	    	   // alert(finalAmntDiv);
	    	    
	    	    var poNumber = $("poNumber").val();
	    	   // var indentNumber = $("indentNumber").val();// indentNumber
	    	   
	    	   // alert(" poNumber "+poNumber);

	    	    var indentNumber = document.getElementById("indentNumber").value;
	    	    
	    	    //alert(" indentNumber "+indentNumber);
	    	    
	    	    if(typeOfPurchase == 'PO'){
	    	   
	    	    
	    	        if((poNumber == "" || poNumber == '' || poNumber == null ) ) {
	    			   alert("Please Enter  poNumber");
	    			  //document.getElementById("indentNumber").focus();
	    			  document.getElementById("poNumber").focus();
	    			return false;
	    		}
	    	    
	    	    }else{
	    	    	
	    	    	
	    	    	
	    	    	 if(typeOfPurchase == 'marketPurchase' && (indentNumber == "" || indentNumber == '' || indentNumber == null ) ) {
		    			   alert("Please Enter  indentNumber");
		    			  //document.getElementById("indentNumber").focus();
		    			  document.getElementById("indentNumber").focus();
		    			return false;
	    	    	 }else{debugger;
	    	    		 
	    	    		  if((typeOfPurchase == 'localPurchase' && ((finalAmntDiv>=10000) && (vendorId!='113'))) && (indentNumber == "" || indentNumber == '' || indentNumber == null )) {
	    		    			if(((vendorId =='125') && (SiteId =='999'))){
	    		    				
	    		    			}
	    		    			else{
	    		    				alert("Invoice Amount Should Not Exceed Rs.10,000");
	    		    			
	    		    			document.getElementById("indentNumber").focus();
	    		    			return false;
	    		    			}
	    		    		}
	    	    		 
	    	    	 }
	    	    }
	    	    
	    	    
  	    	    
  	    	  } 
    		}
    	}
    }
	
	var canISubmit = window.confirm("Do you want to Save?");
	
	if(canISubmit == false) {
		return false;
	}
	
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	
	document.getElementById("doInventoryFormId").action = "doIndentReceive.spring";
	document.getElementById("doInventoryFormId").method = "POST";
	document.getElementById("doInventoryFormId").submit();
}

/*auto complete for transportor start*/
function populateData() {debugger;
	
	var transporterNameId=$("#transporterNameIdId").val();	
	var url = "getTransportorData.spring?transportorName="+transporterNameId;
	var request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				var resp = request.responseText;
				resp = resp.trim();
				console.log("res: "+resp);
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					return false;
				}	
				var spltData = resp.split("@@");				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available.push(spltData[j]);
		    	}
		    	$("#transporterNameIdId").autocomplete({
		  			source : available,
		  			select: function (a, b) {debugger;
		  				setTransportorData (b.item.label);
		  		    }
		  		});
            }		
        };		
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}
function setTransportorData(vName) {debugger;
	
	var url = "getTransportorId.spring?transportorName="+vName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setTransData;
		request.open("POST", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setTransData() {debugger;
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		$("#transporterNameId").val(resp);		
	}
}
//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){
	$("#ChildProduct"+rowNum).val("");
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
		debugger;
		jQuery('.ui-autocomplete-input').each(function() {
			  var currentElement = $(this);
			      value = currentElement.val();
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
