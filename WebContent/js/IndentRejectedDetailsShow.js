
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
            //alert(rowNum);
            $("#ProductId"+rowNum).val(prodId);
            //alert("prodId: "+prodId);
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
            $("#SubProductId"+rowNum).val(prodId);
            this._trigger( "select", event, {
                item: ui.item.option
              });
            //alert("Sub products are loading...");
            //alert("Prod Id = "+prodId+" and Prod Name = "+prodName);
        }      
        else if(str1 == "ChildProduct") {
       	debugger;
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
            loadUnits(prodId, rowNum);
           $("#ChildProductId"+rowNum).val(prodId);
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
    //$("#combobox1").combobox();    
    $( "#toggle").click(function() {
  //$( "#combobox1").toggle();
    });
  });
  
  $(function() {
	//$( "#comboboxsubProd1").combobox();	
  });
  
  $(function() {
	//$("#comboboxsubSubProd1").combobox();
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
	  $("#receivedDate").datepicker({
		  dateFormat: 'dd-M-y',
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

 
  $(function() {debugger;
  	$('#VendorNameId').keypress(function () {debugger;
  		
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
		
		$("#vendorId").val(vendorId);
		$("#VendorAddress").val(vendorAddress);
		$("#GSTINNumber").val(vendorGsinNo);			
	}
}

//append row to the HTML table
function appendRow() {
	debugger;
  //validating product row
	 var valStatus = validateProductRow();
		
		if(valStatus == false) {
	    	return false;
		}
		
	
	
	var currentRows = document.getElementById("numberOfRows").value;
    document.getElementById("numberOfRows").value=parseInt(currentRows)+1;
    document.getElementById("materialRows").value=parseInt(currentRows)+1;
	
	var pressedKey = window.event;
	
	//alert("hai"+currentRows);
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewItemBtnId")) {
			document.getElementById("hiddenSaveBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
		//alert(hiddenSaveBtn);
		
	    var tbl = document.getElementById("doInventoryTableId");
	   
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowNum = getLastRowNum();
		    
		    $("#addNewItemBtnId"+rowNum).hide();
		    
		    rowNum = rowNum+1;
		    var trid="tr-class"+rowNum;
		    $(row).attr("id", trid);
		    $(row).attr("class", "productrowcls");
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryTableId").rows[0].cells;
		    	//alert(x);
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
    	
function deleteRow(currentRow) {
	debugger;
	var canIDelete = window.confirm(" Do you want to Delete the Product "+ currentRow);
	if(canIDelete == false) {
	    return false;
	}
	var rowscount=$('#doInventoryTableId').find('tr').length;
	//removing row
	$("#tr-class"+currentRow).remove();
	
	var tid=$('#doInventoryTableId tr:last').attr('id');	
	var res = tid.split("tr-class")[1];	
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
	}	
	
}
/*************start cal price amount *******************/
function calculatePriceAmount(qtyNum) {
	var qty = "stQantity"+qtyNum;
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


/*************** Calculation for qunatity base***************/

function calculatequantitybase(rowId){debugger;
	var Quantity= document.getElementById("stQantity"+ rowId).value;
	var price= document.getElementById("price"+ rowId).value;
	
	//Calculating the basic amount
	var BasicAmonmt=parseFloat(Quantity)*parseFloat(price);
	document.getElementById("BasicAmountId"+ rowId).value = BasicAmonmt;
	
	// Calculating the discount
	var Disocuntval=document.getElementById("Discount"+ rowId).value;
	var DiscountamtAfrTax=parseFloat(BasicAmonmt - ( BasicAmonmt*Disocuntval/100 ));
	
	// Appending the discount amount
	document.getElementById("amtAfterDiscount"+ rowId).value = DiscountamtAfrTax;

	//taking  the tax
	var e1 = document.getElementById("taxAmount"+rowId);
	//alert(e);
	
	var selectetdtax = e1.options[e1.selectedIndex].value;
	
	if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
		selectedTax1 = selectetdtax.split("$")[1];
		
		var selectedTaxval1 = selectedTax1;
		var selectedTaxval = parseFloat(selectedTaxval1);
	}
	
	
	// Calculating the tax 
	var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;
	
	// appending the tax amount
	document.getElementById("TaxAmount"+ rowId).value = taxAmntVal;
	
	//
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	document.getElementById("TaxAftertotalAmount"+ rowId).value = amontAfterTaxVal;
	
	calculateOtherCharges();
}

/*************end cal price amount *******************/
/*$(function() {
	$('#price1').click(function (strSerialNumber) {*/
function calculateTotalAmount(strSerialNumber) {debugger;
$//("#ChildProduct"+rowNum).val("");
	var qty = $("#stQantity" +strSerialNumber).val();
	var amnt = $("#price"+strSerialNumber).val();
	
	var  BAmnt = $("#BasicAmountId"+strSerialNumber).val(tAmnt);
	
	var tAmnt = 0;	
	var totalAmnt = (qty*amnt);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	//alert(totalAmnt);
	
	tAmnt = parseFloat(totalAmnt);
	$("#BasicAmountId"+strSerialNumber).val(tAmnt);
	
	// Calculating the discount
	var Disocuntval=document.getElementById("Discount"+ strSerialNumber).value;
	var DiscountamtAfrTax=parseFloat(tAmnt - ( tAmnt*Disocuntval/100 ));
	
	// Appending the discount amount
	document.getElementById("amtAfterDiscount"+ strSerialNumber).value = DiscountamtAfrTax;

	//taking  the tax
	var e1 = document.getElementById("taxAmount"+strSerialNumber);
	//alert(e);
	
	var selectetdtax = e1.options[e1.selectedIndex].value;
	
	if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
		selectedTax1 = selectetdtax.split("$")[1];
		
		var selectedTaxval1 = selectedTax1;
		var selectedTaxval = parseFloat(selectedTaxval1);
	}
	
	
	// Calculating the tax 
	var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;
	
	// appending the tax amount
	document.getElementById("TaxAmount"+ strSerialNumber).value = taxAmntVal;
	
	//
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	document.getElementById("TaxAftertotalAmount"+ strSerialNumber).value = amontAfterTaxVal;
	
	calculateOtherCharges();
	};
/********************** Calculate Discount Amount**************************/
	
	function calculateDiscountAmount(strSerialNumber){debugger;
	
		var Bamt= $("#BasicAmountId"+strSerialNumber).val();

		//var  BAmnt = $("#taxAmount"+strSerialNumber).val(DamtAfrTax);
		
		//var Dtax= $("#tax"+strSerialNumber);
		
		var numbers = $("#Discount"+strSerialNumber).val();;
		
		
		var DamtAfrTax=Bamt - ( Bamt*numbers/100 );
		
		
		// alert("afterDiscount "+afterDiscount);
		document.getElementById("amtAfterDiscount"+ strSerialNumber).value = DamtAfrTax;
		//$("#amtAfterDiscount"+strSerialNumber).val(DamtAfrTax);
		//document.getElementById("#taxAmount1").value = DamtAfrTax;
		
		//taking  the tax
		var e1 = document.getElementById("taxAmount"+strSerialNumber);
		//alert(e);
		
		var selectetdtax = e1.options[e1.selectedIndex].value;
		
		if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
			selectedTax1 = selectetdtax.split("$")[1];
			
			var selectedTaxval1 = selectedTax1;
			var selectedTaxval = parseFloat(selectedTaxval1);
		}
		
		
		// Calculating the tax 
		var taxAmntVal = (DamtAfrTax/100) * selectedTaxval;
		
		// appending the tax amount
		document.getElementById("TaxAmount"+ strSerialNumber).value = taxAmntVal;
		
		//
		var amontAfterTaxVal= parseFloat(DamtAfrTax) + parseFloat(taxAmntVal);
		document.getElementById("TaxAftertotalAmount"+ strSerialNumber).value = amontAfterTaxVal;
		
		calculateOtherCharges();
	}
	
	//********* Tax calculation for Grid one ***************
	function calculateTaxAmount(rowNum) {
		debugger;
		var e = document.getElementById("taxAmount"+rowNum);
		//alert(e);
		
		var selectedTax = e.options[e.selectedIndex].value;
		//alert(selectedTax);
		
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];			
			var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;
			percentage(basicAmnt, selectedTax, rowNum);
		}
		else {
			document.getElementById("TaxAmountId"+rowNum).value = "";
			document.getElementById("AmountAfterTaxId"+rowNum).value = "";
		}	
		calculateOtherCharges();
	}
	//********** End Tax calculation for grid one***************
	
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
	
	//alert("charges count 1");
	
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
	
//	alert("trancharge count"+pipeData);
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
/**********************************************************************for edit start***************************************************************/


function loadSubProds(prodId, rowNum) {
	debugger;
	//alert(rowNum);
	prodId = prodId.split("$")[0];
	
	var url="indentReceiveSubProducts.spring?mainProductId="+prodId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				debugger;
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
			   // selectBox.appendChild(initOpt);
			    
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
//			    initOpt.text = "--Select--";
//			    initOpt.value = "";
	//		    selectBox.appendChild(initOpt);
			    
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
				$("#QuantityId"+rowNum).val('');
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
/*********************************************************************for edit start********************************************************************/
function loadSubProds1(prodId, rowNum, action) {
	
	prodId = prodId.split("$")[0];
	var url = "indentIssueSubProducts.spring?mainProductId="+prodId;
	//alert("the product is"+prodId);
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                if(action!='edit'){
                	$("#subProduct"+rowNum).val("");
                	$("#childProduct"+rowNum).val("");
                	$("#UnitsOfMeasurementId"+rowNum).val("");
                }
				
				var resp = request.responseText;
				resp = resp.trim();			

		    	var spltData = resp.split("|");
		    	//alert(spltData);
		    	
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSet = "subProduct"+rowNum;
		    	//alert(subProdToSet);
		    	
		    	var selectBox = document.getElementById(subProdToSet);
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById(subProdToSet) != null && document.getElementById(subProdToSet).options.length > 0) {
			    	document.getElementById(subProdToSet).options.length = 0;
			    }
			    //Removing previous options from select box - End
			    
			    initOpt = document.createElement("option");
			  /*  initOpt.text = "--Select--";*/
			    initOpt.value = $("#actualSubProductId"+rowNum).val()+"$"+$("#subProduct"+rowNum).val();
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
			    	    //alert("prod name value"+prodIdAndName);
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

function loadSubSubProducts1(subProdId, rowNum, action) {
	
	subProdId = subProdId.split("$")[0];
	var url = "indentIssueChildProducts.spring?subProductId="+subProdId;	  
	var request = getAjaxObject();	
	try {
		request.onreadystatechange = function() {			
			if(request.readyState == 4 && request.status == 200) {
				if(action != 'edit') {
					$("#childProduct"+rowNum).val("");
					$("#UnitsOfMeasurementIdt"+rowNum).val("");
				}				
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
			  /*  initOpt.text = "--Select--";*/
			    initOpt.value = $("#actualChildProductId"+rowNum).val()+"$"+$("#ChildProduct"+rowNum).val();
			    selectBox.appendChild(initOpt);
			    
			    var defaultOption;
			    var data;
			    
			    for(var i=0; i<available.length; i++) {
			    	defaultOption = document.createElement("option");
			    	data = available[i].split("_");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {
			    		var prodIdAndName = data[0]+"$"+data[1];
			    		defaultOption.text = data[1];
			    		$("#groupId"+rowNum).val(data[2]);
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

function loadUnits1(childProdId, rowNum, action) {
	debugger;
	childProdId = childProdId.split("$")[0];
	var url = "listUnitsOfSubProducts.spring?childProductId="+childProdId;	  
	var request = getAjaxObject();	
	try {
		request.onreadystatechange = function() {
			debugger;
			if(request.readyState == 4 && request.status == 200) {
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
			    	//document.getElementById(unitsToSelect).options.length = 0;
			    }
			    //Removing previous options from select box - End			    
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
			    	  //  selectBox.appendChild(defaultOption);
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

/********************************************************************for edit purpose end *************************************************************/

//********************** Tax calculation GST for second grid*****************

function calculateGSTTaxAmount(rowNum) {
	var e = document.getElementById("GSTTax"+rowNum);
	var selectedTax = e.options[e.selectedIndex].value;
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
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

function removeRow(rowId){debugger;
var canIRemove = window.confirm(" Do you want to remove the Product "+ rowId);
if(canIRemove == false) {
    return false;
}

$('#actionValueId'+rowId).val("R");
$("#CommentsId"+rowId).prop('readonly', true);
$("#RemarksId"+rowId).prop('readonly', true);
$("#QuantityId"+rowId).prop('readonly', true);
$("#QuantityId"+rowId).prop('readonly', true);
$("#Product"+rowId).prop('disabled', true);
$("#SubProduct"+rowId).prop('disabled', true);
$("#ChildProduct"+rowId).prop('disabled', true);

$("#UnitsOfMeasurementId"+rowId).prop('disabled', true);
$("#tr-class"+rowId).addClass('strikeout');

$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$.each($('.btn-visibilty'+rowId),function(index,row){
	$(this).closest('td').find('input').attr('disabled');
	$(this).closest('td').find('input').attr('readonly');
	$(this).closest('td').find('.custom-combobox-toggle').addClass('hide');
	$(this).closest('td').find('.ui-autocomplete-input').addClass('form-control');	
	
});

}

function editchargesInvoiceRow(rowId){debugger;
var canIEdit = window.confirm("Do you want to update the Po transport row " + rowId);
if(canIEdit == false) {
    return false;
}	
$('#actionValueId'+rowId).val("E");


$("#Conveyance"+rowId).attr("disabled", false).css('cursor','allowed');
$("#ConveyanceAmount"+rowId).attr('readonly', false);
$("#GSTTax"+rowId).attr('readonly', false);
$("#GSTAmount"+rowId).attr('readonly', false);
$("#AmountAfterTax"+rowId).attr('readonly', false);
$("#TransportInvoice"+rowId).attr('readonly', false);
$('#transactionActionValue'+rowId).val("E");

}
function editInvoiceRow(rowId){debugger;
var canIEdit = window.confirm("Do you want to edit Row"+rowId);
if(canIEdit == false) {
    return false;
}	
$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#Product'+i).attr('readonly','false');
$('#SubProduct'+i).attr('readonly','false');
$('#ChildProduct'+i).attr('readonly','false');
$("#addsave").show();
$("#CommentsId"+rowId).prop('readonly', false);
$("#RemarksId"+rowId).prop('readonly', false);
$("#QuantityId"+rowId).prop('readonly', false);
$("#UnitsOfMeasurementId"+rowId).prop('disabled', false);
$("#UnitsOfMeasurementId"+rowId).removeAttr('readonly');
$("#UnitsOfMeasurementId"+rowId).css('cursor', 'pointer');

$('#actionValueId'+rowId).val("E");

 var subProd= $("#comboboxsubProd"+rowId).val();
	var childProdct= $("#comboboxsubSubProd"+rowId).val();
	var Uom= $("#UnitsOfMeasurementId"+rowId).val();
	$.each($('.btn-visibilty'+rowId),function(index,row){
		$(this).closest('td').find('input').removeAttr('disabled');
		$(this).closest('td').find('input').removeAttr('readonly');
		$(this).closest('td').find('.ui-autocomplete-input').removeClass('form-control');	
		$(this).closest('td').find('.custom-combobox-toggle').removeClass('hide');			
		productId = $('#product'+rowId).val();
		SubproductId = $('#subProduct'+rowId).val();
		ChildproductId = $('#childProduct'+rowId).val();
	});

var strUser= subProd.split("_");

loadSubSubProducts1(strUser[0],rowId, 'edit');
var strUser= childProdct.split("_");

loadUnits1(strUser[0],rowId, 'edit');


}
//********************** End Tax calculation GST for second grid*****************

function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	document.getElementById("TaxAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("TaxAftertotalAmount"+rowNum).value = amntaftTx;
}
//************ Calculation for Conveyance AMount*******

function GSTpercentage(basicAmnt, tax, rowNum) {
	debugger;
	
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	document.getElementById("GSTAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	
	document.getElementById("AmountAfterTax"+rowNum).value = amntaftTx;
}

//*****************************************************
function calculateOtherCharges() {debugger;

	
	
	var valStatus = validateRowData(1);
	if(valStatus == false) {
    	return false;
	}
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
	//BasicAmountId1
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var fnlAmnt = 0;
	for(var x=0; x<snoId; x++) {
		var val = spltdData[x];
		var individualBasicAmount = document.getElementById("amtAfterDiscount"+val).value;	
		individualBasicAmount = individualBasicAmount.trim();
		individualBasicAmount = parseFloat(individualBasicAmount);
		individualBasicAmount = Math.round(individualBasicAmount * 100)/100;		
		var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
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
		var amntAfterTax = document.getElementById("TaxAftertotalAmount"+val).value;
		amntAfterTax = parseFloat(amntAfterTax);			
		var othOrTransChrgsAfterTax = document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value;
		othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);
		var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
		ttlAmnt = Math.round(ttlAmnt * 100)/100;
		document.getElementById("TotalAmountId"+val).value = parseFloat(ttlAmnt).toFixed(2);
		fnlAmnt = parseFloat(fnlAmnt);
		fnlAmnt = fnlAmnt + ttlAmnt;
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
	}
	fnlAmnt = Math.round(fnlAmnt * 100)/100;
	document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
  }
}

function calculateTotalBasicAmount() {debugger;
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var totalBasicAmount = 0;
	for(var x=0; x<snoId; x++) {
	
		var val = spltdData[x];
		var basicAmnt = document.getElementById("amtAfterDiscount"+val).value;
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
function validateRowData(strSerialNumber) {
	  
	var Quan = document.getElementById("stQantity"+strSerialNumber).value;
	//alert(invoiceNum);
	
	if(Quan == "" || Quan == null || Quan == '') {
		alert("Please enter Qunatity.");
		document.getElementById("Quan").focus();
		return false;
	}				
	  
	var Amt = document.getElementById("price"+strSerialNumber).value;
	//alert(invoiceDate);
	
	if(Amt == "" || Amt == null || Amt == '') {
		alert("Please enter the price.");
		document.getElementById("Amt").focus();
		return false;
	}	  
	  
	var disc = document.getElementById("Discount"+strSerialNumber).value;
	//alert(invoiceDate);
	
	if(disc == "" || disc == null || disc == '') {
		alert("Please enter the discount.");
		document.getElementById("Amt").focus();
		return false;
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

function validateProductAvailability(unitsFld) {debugger;
	//alert(unitsFld);
	var measurementId = unitsFld.id;
	var spiltid=measurementId.split("UnitsOfMeasurementId")[1];
	//alert(spiltid);
	var measurementval=unitsFld.value;
	//alert(measurementval);
	$("#UnitsOfMeasurementIdval"+spiltid).val(measurementval);
	var currentRowNum = measurementId.match(/\d+/g);
	//alert(currentRowNum);
	
	measurementId = document.getElementById(measurementId);
	measurementId = measurementId.options[measurementId.selectedIndex].value;
	//alert(measurementId);
	
	measurementId = measurementId.split("$")[0];
	//alert(measurementId);
	
	if(measurementId != "" && measurementId != '' && measurementId != null) {
	
		var pro = $("#ProductId"+currentRowNum).val();
		pro = pro.split("$")[0];
		//alert(pro);
		
		var subPro = $("#SubProductId"+currentRowNum).val();
		subPro = subPro.split("$")[0];
		//alert(subPro);
		
		var childPro = $("#ChildProductId"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		var groupId = $("#groupId"+currentRowNum).val();
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum, groupId);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum, groupId) {
	
	var url = "getProductAvailability2.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&groupId="+groupId+"&isReceive=false";
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
		    	$("#ProductAvailabilityId"+currentRowNum).val(available[0]);
		    	if($("#actionValueId"+currentRowNum).val()=="N"){
		    		$("#sumofrecieveQty"+currentRowNum).val(available[1]);
		    		$("#sumofIssueQty"+currentRowNum).val(available[2]);
		    		$("#indentPendingQty"+currentRowNum).val(available[3]);
		    		$("#BOQQty"+currentRowNum).val(available[4]);
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


function validateProductRow(){
	//debugger;
	var error=true;
	$(".productrowcls").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		if($("#Product"+currentId).val()==''){
			alert("please select Product.");
			$("#Product"+currentId).focus();
			return  error=false;
		}
		if($("#SubProduct"+currentId).val()==''){
			alert("please enter SubProduct.");
			$("#SubProduct"+currentId).focus();
			return  error=false;
		}
		if($("#ChildProduct"+currentId).val()==''){
			alert("please select ChildProduct.");
			$("#ChildProduct"+currentId).focus();
			return  error=false;
		}	
		if($("#UnitsOfMeasurementId"+currentId).val()==''){
			alert("please select Units Of Measurement.");
			$("#UnitsOfMeasurementId"+currentId).focus();
			return  error=false;
		}	
		if($("#QuantityId"+currentId).val()==''){
			alert("please enter Quantity.");
			$("#QuantityId"+currentId).focus();
			return  error=false;
		}	
	})
	return  error;
}




function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	calculateOtherCharges();
	//validating product row
  var valStatus = validateProductRow();
  	if(valStatus == false) {    
    	return false;
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
function childcampare(childname, rowNum){
	debugger;	
	$("#comboboxsubSubProd"+rowNum).val("");
	$("#ChildProduct"+rowNum).val("");
	$("#ChildProduct"+rowNum).attr("class", "ChildProduct");
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
		debugger;
		jQuery('.ChildProduct').each(function() {
			debugger;
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


function validateBOQQuantity(id, type){debugger;
if($("#QuantityId"+id).val()=="" || $("#QuantityId"+id).val()=="0"){
	return false;
}
var siteId=$("#siteId").val();
var childProductId=$("#ChildProductId"+id).val().split("$")[0];
var groupId=$("#groupId"+id).val();
var allSiteIds=$("#allSiteIds").val().split(",");
var count=0;
for (var i = 0; i < allSiteIds.length; i++) {
	if(allSiteIds[i]==siteId){
		count++;
	}
}
if(count==0){
	var matchedEnteredQty=0;
	$(".productrowcls").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		var currentGroupId=$("#groupId"+currentId).val();
		if(currentGroupId==groupId && $("#actionValueId"+currentId).val()!="R"){
			if(type=="Old" && $("#actionValueId"+currentId).val()!="N"){
				matchedEnteredQty+=parseFloat($("#QuantityId"+currentId).val()==""?0:$("#QuantityId"+currentId).val())-parseFloat($("#Quantity"+currentId).val()==""?0:$("#Quantity"+currentId).val());
			}else{
				matchedEnteredQty+=parseFloat($("#QuantityId"+currentId).val()==""?0:$("#QuantityId"+currentId).val());
			}			
		}			
	});
	matchedEnteredQty=matchedEnteredQty.toFixed(3);
	var url="gettingBoqQuantityAjax.spring?childProductId="+childProductId+"&groupId="+groupId+"&siteId="+siteId;
	$.ajax({
		 url : url,
		 type : "post",
		 success : function(data) {
			console.log("data: "+data);
			var avalibleQty=data.split("_")[0];
			var BOQQTY=data.split("_")[1];
			avalibleQty=parseFloat(avalibleQty).toFixed(3);
			//var totalQty=parseFloat(QtyData[0])-parseFloat(QtyData[1])+parseFloat(QtyData[2]);
			//var BOQQty=parseFloat(QtyData[3]).toFixed(2);
			if(parseFloat(matchedEnteredQty)>parseFloat(avalibleQty) && groupId!="0"){
			$(".loader-sumadhura").hide();
			alert("You can not initiate Child Product "+$("#ChildProductId"+id).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
			$("#QuantityId"+id).val('');
				$("#QuantityId"+id).focus();					
				return false;
			}
			$(".loader-sumadhura").hide();
		 }
	})
}else{

	$(".loader-sumadhura").hide();
}

}

function validateUnitsAndAvailability(qtyObj) {
	debugger;
	var qtyObjectId = qtyObj.id;	
	var currentRowNum = qtyObjectId.match(/\d+/g);	
	var qty = "";
	var productAva = "";	
	qty = document.getElementById("QuantityId"+currentRowNum).value;
	qty = parseFloat(qty);
	var allSiteIds=$("#allSiteIds").val().split(",");
	var siteId=$("#siteId").val();
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
		var totalQty=parseFloat($("#sumofrecieveQty"+currentRowNum).val())-parseFloat($("#sumofIssueQty"+currentRowNum).val())+parseFloat($("#indentPendingQty"+currentRowNum).val());
		var BOQQty=parseFloat($("#BOQQty"+currentRowNum).val()).toFixed(2);
		var matchedEnteredQty=0;
		$(".productrowcls").each(function(){
			var id=$(this).attr("id").split("tr-class")[1];
			var currentGroupId=$("#groupId"+id).val();
			if(currentGroupId==groupId){
				matchedEnteredQty+=parseFloat($("#QuantityId"+id).val()==""?0:$("#QuantityId"+id).val());
			}			
		})
		debugger;
		totalQty=(totalQty+matchedEnteredQty).toFixed(2);
		if(parseFloat(totalQty)>parseFloat(BOQQty) && groupId!='0'){
			alert("You can not initiate Child Product "+$("#ChildProduct"+currentRowNum).val()+" more than "+BOQQty+" "+$("#UnitsOfMeasurementId"+currentRowNum).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
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
