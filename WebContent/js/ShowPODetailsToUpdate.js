
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
        
        var prodId = "";
        var prodName = "";
        
        prodId = ui.item.option.value;
        prodName = ui.item.value;
        
        var ele = this.element[0].name;
        
        var rowNum = ele.match(/\d+/g);
        
        //Removing numbers from the header names
        var str1 = ele.replace(/[0-9]/g, '');
        
        if(str1 == "Product") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubProds(prodId, rowNum);
            this._trigger( "select", event, {
                item: ui.item.option
              });
        }            
        else if(str1 == "SubProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubSubProducts(prodId, rowNum);
            this._trigger( "select", event, {
                item: ui.item.option
              });
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
       /* this._trigger( "select", event, {
          item: ui.item.option
        });*/
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
	  $("#receivedDate").datepicker({
		  dateFormat: 'dd-M-y',
		 maxDate: new Date() ,
		 onSelect: function(dateText) {
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
	var currentRows = document.getElementById("numberOfRows").value;
    document.getElementById("numberOfRows").value=parseInt(currentRows)+1;
	
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
		    var rowid="tr-class"+rowNum;
			$(row).attr("id", rowid);
		    $(row).attr("class", "productRow");
		    
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');	    	
		        createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }
		    
		    var lastDiv = getLastRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("Product"+lastDiv).focus();
		}
	}
}

//Append Row for other charges


function appendChargesRow() {debugger;
var tbllength=$('#doInventoryChargesTableId').find('tr').length;
/*alert(tbllength);*/
if(tbllength==2){
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');
	var res = tid.split("trans-tr-class")[1];
	$("#addDeleteItemBtnId"+res).show();
}

	var currentRows = document.getElementById("noofTransRows").value;
	document.getElementById("noofTransRows").value=parseInt(currentRows)+1;
	
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
	    
	/*    var valStatus = validateRowData();
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
		    var rowChargesNum = getLastChargesRowNum();
		    
		    $("#addNewChargesItemBtnId"+rowChargesNum).hide();
		    
		    rowChargesNum = rowChargesNum+1;
		    var rowid="trans-tr-class"+rowChargesNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "chargesRow");
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
	var canIDelete=window.confirm("Do you want to delete?");
	if(canIDelete==false){
		return false;
	}
	//removing row
	var n=0;
	$(".chargesRow").each(function(){debugger;
		var id=$(this).attr("id").split("trans-tr-class")[1];
		if($("#transactionActionValue"+id).val()!="R"){
			n++;
		}
	});

	if(n==1){
		alert("This row can't be deleted.");
		return false;
	}
   $("#trans-tr-class"+currentRow).remove();
	
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("trans-tr-class")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<=currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calcuLateFinalAmount();
}

//deleting product table 
function deleteproductRow(btn, currentRow) {
	debugger;

	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryTableId').find('tr').length;
	var canIDelete=window.confirm("Do you want to delete?");
	if(canIDelete==false){
		return false;
	}
	//removing row
	var n=0;
	$(".productRow").each(function(){debugger;
		var id=$(this).attr("id").split("tr-class")[1];
		if($("#actionValueId"+id).val()!="R"){
			n++;
		}
	});

	if(n==1){
		alert("This row can't be deleted.");
		return false;
	}

	$("#tr-class"+currentRow).remove();
	
	var tid=$('#doInventoryTableId tr:last').attr('id');
	
	var res = tid.split("tr-class")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
		/*return false;*/
	}
	if(res<=currentRow){
		$("#addNewItemBtnId"+res).show();
	}
	calcuLateFinalAmount();
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
		alert("Please enter quantity.");
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
	
	//document.getElementById(price).value = basicAmt;
	$("#"+price).val(parseFloat(basicAmt).toFixed(2));
	
}


/*************** Calculation for qunatity base***************/

function calculatequantitybase(rowId,currentORnew){debugger;
	var isUpdated=$("#isUpdated").val();
	var stQantity= $("#stQantity"+ rowId).val()==""?0:$("#stQantity"+ rowId).val();
	if (isNaN(stQantity)) {
		debugger;
		alert("Please enter valid data.");
		$("#stQantity"+rowId).val("0");
		$("#stQantity"+rowId).focus();
		
		return false;
	}
	if(isUpdated=="true"){
	validateUpdatePOBOQQuantity(rowId);
	}else{
		validateRevisedBOQQuantity(rowId,currentORnew);
	}
	
	var Quantity= document.getElementById("stQantity"+ rowId).value;
	var price= document.getElementById("price"+ rowId).value;
	
	//Calculating the basic amount
	var BasicAmonmt=parseFloat(Quantity)*parseFloat(price);
	//document.getElementById("BasicAmountId"+ rowId).value = BasicAmonmt;
	$("#BasicAmountId"+rowId).val(parseFloat(BasicAmonmt).toFixed(2));
	
	// Calculating the discount
	var Disocuntval=$("#Discount"+ rowId).val();
	var DiscountamtAfrTax=parseFloat(BasicAmonmt - ( BasicAmonmt*Disocuntval/100 ));
	
	// Appending the discount amount
	$("#amtAfterDiscount"+ rowId).val(parseFloat(DiscountamtAfrTax).toFixed(2));

	//taking  the tax
	var e1 = document.getElementById("taxAmount"+rowId);
	//alert(e);
	
	var selectetdtax = e1.options[e1.selectedIndex].value;
	var selectedTaxval;
	if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
		selectedTax1 = selectetdtax.split("$")[1];
		
	    var selectedTaxval1 = selectedTax1;
		selectedTaxval = parseFloat(selectedTaxval1);
	}else{
		selectedTaxval=0;
	}
	
	
	// Calculating the tax 
	var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;
	
	// appending the tax amount
	$("#TaxAmount"+ rowId).val(parseFloat(taxAmntVal).toFixed(2));
	
	//
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	//document.getElementById("TaxAftertotalAmount"+ rowId).value = amontAfterTaxVal;
	$("#TaxAftertotalAmount"+ rowId).val(parseFloat(amontAfterTaxVal).toFixed(2));
	calculateOtherCharges();
}

/*************end cal price amount *******************/
/*$(function() {
	$('#price1').click(function (strSerialNumber) {*/

function calculateTotalAmount(strSerialNumber) {debugger;
 //("#ChildProduct"+rowNum).val("");


	var qty = $("#stQantity" +strSerialNumber).val();
	var amnt = $("#price"+strSerialNumber).val();
	if(isNaN(qty)){
		alert("Please enter valid data.");
		$("#stQantity" +strSerialNumber).val('');
		//$("#stQantity" +strSerialNumber).focus();
		return false;
	}
	if(qty=="" || qty=="0" || qty=="0.00"){
		return false;
	}
	var  BAmnt = $("#BasicAmountId"+strSerialNumber).val(tAmnt);
	calculatequantitybase(strSerialNumber);
	var tAmnt = 0;
	var isUpdated=$("#isUpdated").val();
	var a=checkQuantity(strSerialNumber);
	debugger;
	if(a==false){
		return false;
	}
	
	var totalAmnt = (qty*amnt);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	//alert(totalAmnt);
	
	tAmnt = parseFloat(totalAmnt);
	$("#BasicAmountId"+strSerialNumber).val(tAmnt.toFixed(2));
	
	// Calculating the discount
	var Disocuntval=document.getElementById("Discount"+ strSerialNumber).value;
	var DiscountamtAfrTax=parseFloat(tAmnt - ( tAmnt*Disocuntval/100 ));
	
	// Appending the discount amount
	//document.getElementById("amtAfterDiscount"+ strSerialNumber).value = DiscountamtAfrTax;
	$("#amtAfterDiscount"+ strSerialNumber).val(parseFloat(DiscountamtAfrTax).toFixed(2));
	//taking  the tax
	var e1 = document.getElementById("taxAmount"+strSerialNumber);
	//alert(e);
	
	var selectetdtax = e1.options[e1.selectedIndex].value;
	var selectedTaxval;
	if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
		selectedTax1 = selectetdtax.split("$")[1];
		
		var selectedTaxval1 = selectedTax1;
		selectedTaxval = parseFloat(selectedTaxval1);
	}else{
		selectedTaxval=0;
	}
	
	
	// Calculating the tax 
	var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;
	
	// appending the tax amount
	document.getElementById("TaxAmount"+ strSerialNumber).value = taxAmntVal;
	
	//
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	//document.getElementById("TaxAftertotalAmount"+ strSerialNumber).value = amontAfterTaxVal;
	$("#TaxAftertotalAmount"+ strSerialNumber).val(parseFloat(amontAfterTaxVal).toFixed(2));
	calculateOtherCharges();
	};

/********************** Calculate Discount Amount**************************/
	
	function calculateDiscountAmount(strSerialNumber){debugger;
	
		var Bamt= $("#BasicAmountId"+strSerialNumber).val();

		//var  BAmnt = $("#taxAmount"+strSerialNumber).val(DamtAfrTax);
		
		//var Dtax= $("#tax"+strSerialNumber);
		
		var numbers = $("#Discount"+strSerialNumber).val();
		if(numbers>100){
			alert("Unable to update more than 100%.");
			 $("#Discount"+strSerialNumber).val('');
			 $("#Discount"+strSerialNumber).focus();
			 return false;
		}
		
		var DamtAfrTax=Bamt - ( Bamt*numbers/100 );
		
		
		// alert("afterDiscount "+afterDiscount);
		//document.getElementById("amtAfterDiscount"+ strSerialNumber).value = DamtAfrTax;
		$("#amtAfterDiscount"+strSerialNumber).val(parseFloat(DamtAfrTax).toFixed(2));
		//document.getElementById("#taxAmount1").value = DamtAfrTax;
		
		//taking  the tax
		var e1 = document.getElementById("taxAmount"+strSerialNumber);
		//alert(e);
		
		var selectetdtax = e1.options[e1.selectedIndex].value;
		var selectedTaxval;
		if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
			selectedTax1 = selectetdtax.split("$")[1];
			
			var selectedTaxval1 = selectedTax1;
			selectedTaxval = parseFloat(selectedTaxval1);
		}else{
			selectedTaxval=0;
		}
		
		
		// Calculating the tax 
		var taxAmntVal = (DamtAfrTax/100) * selectedTaxval;
		
		// appending the tax amount
		document.getElementById("TaxAmount"+ strSerialNumber).value = taxAmntVal;
		
		//
		var amontAfterTaxVal= parseFloat(DamtAfrTax) + parseFloat(taxAmntVal);
		//document.getElementById("TaxAftertotalAmount"+ strSerialNumber).value = amontAfterTaxVal;
		$("#TaxAftertotalAmount"+ strSerialNumber).val(parseFloat(amontAfterTaxVal).toFixed(2));
		//calculateOtherCharges();
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
			/*selectedTax = selectedTax.substring(0, selectedTax.length - 1);*/
			/*selectedTax = selectedTax.trim();*/
			//alert("Selected Tax = "+selectedTax);
			
			var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;
			//alert("Basic Amount = "+basicAmnt);
			
			percentage(basicAmnt, selectedTax, rowNum);
		}
		else {
			document.getElementById("TaxAmountId"+rowNum).value = "";
			document.getElementById("AmountAfterTaxId"+rowNum).value = "";
		}	
		//calculateOtherCharges();
	}
	//********** End Tax calculation for grid one***************


	
	//********* Tax calculation for Grid one ***************
	/*function calculateDisTaxAmount(strSerialNumber) {
		debugger;
		var e = document.getElementById("amountAfterTax"+strSerialNumber);
		var selectedTax = e.options[e.selectedIndex].value;
		
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];
			
			//alert("Selected Tax = "+selectedTax);
			
			var basicAmnt = document.getElementById("BasicAmountId"+strSerialNumber).value;
			//alert("Basic Amount = "+basicAmnt);
			
			percentage(basicAmnt, selectedTax, strSerialNumber);
		}
		else {
			document.getElementById("TaxAmountId"+strSerialNumber).value = "";
			document.getElementById("AmountAfterTaxId"+strSerialNumber).value = "";
		}	
	}*/
		
		/*
		var basicAmnt = $("#taxAmount"+ strSerialNumber).val();
		//var  taxamnt = $("#amountAfterTax"+strSerialNumber).val();
		var numbers = [5, 12,18,28];
		var option = '';

		for (var i=0;i<numbers.length;i++){
			option += '<option value="'+ numbers[i] + '">' + numbers[i] + '</option>';


		}
		$("taxamnt").append(option);
		var e = document.getElementById("amountAfterTax"+strSerialNumber);
		var Ttax = e.options[e.selectedIndex].value;
		
		//var Discamnt = $("taxAmount"+strSerialNumber).val();
		//alert(e);
		
		var selectedTax =Ttax;
		//alert(selectedTax);
		
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];
			//selectedTax = selectedTax.trim();
			//alert("Selected Tax = "+selectedTax);
			
			var basicAmnt = $("#taxAmount"+strSerialNumber).val();
			//alert("Basic Amount = "+basicAmnt);
			
			percentage(basicAmnt, selectedTax);
		}
		else {
			document.getElementById("totalAmount").value = "";
			document.getElementById("totalAmount").value = "";
		}	
	}*/
	//********** End Tax calculation for grid one***************
	
/*function calcuLateFinalAmount() {
	var taxamnt = $("#amountAfterTax1").val();
	var basicAmnt = $("#taxAmount1").val();
	var taxAmnt = (Discamnt/100) * taxamnt;
	document.getElementById("hiddenSaveBtnId").value = "";
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			//alert(ask);
			if(ask.indexOf("TotalTaxAftertotalAmount1") != -1) {
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
    	
    	if(fldName.indexOf("TotalTaxAftertotalAmount1") != -1) {
    		var fldAmnt = document.getElementById(fldName).value;
    		if(fldAmnt != "" && fldAmnt != null && fldAmnt != '') {
        		tempAmnt = parseFloat(tempAmnt) + parseFloat(fldAmnt);
        		tempAmnt = Math.round(tempAmnt * 100)/100;
    		}
		}	
    }
    //alert("Final Amount = "+tempAmnt);
	var taxAmnt = (Discamnt/100) * taxamnt;
	taxAmnt = Math.round(taxAmnt * 100)/100;
    document.getElementById("finalAmntDiv").innerHTML = taxAmnt;
    document.getElementById("ttlAmntForIncentEntryId").value = taxAmnt;
}*/
	
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
	    //document.getElementById("finalAmntDiv").innerHTML = tempAmnt;
		$("#finalAmntDiv").text(parseFloat(tempAmnt).toFixed(2))
	    //document.getElementById("ttlAmntForIncentEntryId").value = tempAmnt;
	    $("#ttlAmntForIncentEntryId").val(tempAmnt);
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
				
				$("#UnitsOfMeasurementId"+rowNum).val("");
				
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
/*//********* Tax calculation for Grid one ***************
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
*/



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

function removeRow(){debugger;
alert(" Do you want to remove the Product");

/*	document.getElementById("ractionValueId1).value = "R";*/

$("#tr-class").addClass('strikeout');
$("#combobox1").attr("disabled", true).css('cursor','not-allowed');
$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed');
$("#addNewItemBtnId1").attr("disabled", true).css('cursor','not-allowed');
$('#snoDivId1').removeAttr('id');	
$('#PriceId1').removeAttr('id');
$('#TaxAmountId1').removeAttr('id');
$('#BasicAmountId1').removeAttr('id');
$('#tax1').removeAttr('id');
$('#QuantityId1').removeAttr('id');
$('#AmountAfterTaxId1').removeAttr('id');
$('#OtherOrTransportChargesId1').removeAttr('id');
$('#TaxOnOtherOrTransportChargesId1').removeAttr('id');
$('#OtherOrTransportChargesAfterTaxId1').removeAttr('id');
$('#TotalAmountId1').removeAttr('id');




}

function editchargesInvoiceRow(rowId){debugger;

	var canIEdit=window.confirm("Do you want to edit?");
	if(canIEdit==false){
		return false;
	}
	$("#comboboxsubProd"+rowId).attr("disabled", true).css('cursor','not-allowed');
	
	$("#Conveyance"+rowId).attr("disabled", false).css('cursor','allowed');
	$("#ConveyanceAmount"+rowId).attr('readonly', false);
	$("#GSTTax"+rowId).attr('readonly', false);
	$("#TransportInvoice"+rowId).attr('readonly', false);
	$('#transactionActionValue'+rowId).val("E");

}
function editInvoiceRow(rowId){debugger;

	var canIEdit=window.confirm("Do you want to edit?");
	if(canIEdit==false){
		return false;
	}
	
	$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed');
	
	$('#actionValueId'+rowId).val("E");
	$('#childProductVendorDesc'+rowId).prop('readonly', false);
	$('#stQantity'+rowId).prop('readonly', false);
	$('#price'+rowId).prop('readonly', false);
	$('#taxAmount'+rowId).removeAttr('readonly');
	$('#Discount'+rowId).prop('readonly', false);
	$('#hsnCode'+rowId).prop('readonly', false);
	$("#editItem"+rowId).attr("disabled", "true").css('cursor','not-allowed');
	$("#addsave").show();
}
function editUpdatePoRow(rowId){debugger;
var canIdelete=window.confirm("Do you want to update?");
if(canIdelete==false){
	return false;
}
$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed');
$('#actionValueId'+rowId).val("E");
$('#childProductVendorDesc'+rowId).prop('readonly', true);
	$('#stQantity'+rowId).prop('readonly', false);
	$('#stQantity'+rowId).focus();
	$('#price'+rowId).prop('readonly', true);
	$('#Discount'+rowId).prop('readonly', true);
	$('#amtAfterDiscount'+rowId).prop('readonly', true);
	$('#taxAmount'+rowId).remove('readonly',true);
	$('#hsnCode'+rowId).prop('readonly', false);
	$('#TaxAmount'+rowId).prop('readonly', true);
	$('#TaxAftertotalAmount'+rowId).attr('readonly', true);
	$('#OtherOrTransportChargesId'+rowId).prop('readonly', true);
	$('#TaxOnOtherOrTransportChargesId'+rowId).prop('readonly', true);
$('#OtherOrTransportChargesAfterTaxId'+rowId).prop('readonly',true);
$('#TotalAmountId'+rowId).prop('readonly',true);

$("#addsave").show();

}


//********************** End Tax calculation GST for second grid*****************
/*function percentage(Discamnt, taxamnt, strSerialNumber) {
	debugger;
	 var sum = 0;
	 var Discamnt = $("#GSTAmount" +strSerialNumber).val();
	var taxamnt = $("#AmountAfterTax" +strSerialNumber).val();
	var basicAmnt = $("#taxAmount"+strSerialNumber).val();
	var taxAmnt = (Discamnt/100) * taxamnt;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	//alert("Tax Amount = "+taxAmnt);
	document.getElementById("TaxAmount"+strSerialNumber).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("TaxAftertotalAmount"+strSerialNumber).value = amntaftTx;
	document.getElementById("TotalTaxAftertotalAmount"+strSerialNumber).value = amntaftTx;
	//document.getElementById("finalAmntDiv").value= sum;
	
	$('.ttamount').each(function() {
		//alert("before")
        sum += Number($(this).val());
		//alert(sum);
    });
	document.getElementById("finalAmntDiv1").innerHTML = sum;
	//document.getElementById("#finalAmntDiv").value = amntaftTx;
}
*/

function isNumberCheckRevisedPO(el, evt) {
	debugger;
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var num=el.value;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
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
function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	//alert("Tax Amount = "+taxAmnt);
	//document.getElementById("TaxAmount"+rowNum).value = taxAmnt;
	$("#TaxAmount"+rowNum).val(parseFloat(taxAmnt).toFixed(2));
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	//document.getElementById("TaxAftertotalAmount"+rowNum).value = amntaftTx;
	$("#TaxAftertotalAmount"+rowNum).val(parseFloat(amntaftTx).toFixed(2));
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
		alert("Please select the conveynace.");
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
		var individualBasicAmount = document.getElementById("amtAfterDiscount"+val).value;
	
		individualBasicAmount = individualBasicAmount.trim();
		individualBasicAmount = parseFloat(individualBasicAmount);
		individualBasicAmount = Math.round(individualBasicAmount * 100)/100;
		
		var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		if((individualBasicAmount=='0'  && rsltTtlBscAmnt=='') || rsltTtlBscAmnt=='' ){
			charges=0;
		}
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
		var amntAfterTax = document.getElementById("TaxAftertotalAmount"+val).value;
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
	//document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
	$("#finalAmntDiv").text(parseFloat(fnlAmnt).toFixed(2));
	$("#ttlAmntForIncentEntryId").val(fnlAmnt);
	//Setting Final Amount - End
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
		alert("Please enter qunatity.");
		//document.getElementById("Quan").focus();
		return false;
	}				
	  
	var Amt = document.getElementById("price"+strSerialNumber).value;
	//alert(invoiceDate);
	
	if(Amt == "" || Amt == null || Amt == '') {
		alert("Please enter price.");
		document.getElementById("Amt").focus();
		return false;
	}	  
	  
	var disc = document.getElementById("Discount"+strSerialNumber).value;
	//alert(invoiceDate);
	
	if(disc == "" || disc == null || disc == '') {
		alert("Please enter discount.");
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
		var groupId = $("#groupId"+currentRowNum).val();
		//alert(childPro);	
		var requestedDate=$("#receivedDate").val();
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum,requestedDate,groupId);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum,requestedDate,groupId) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requestedDate+"&groupId="+groupId;
	//alert(url);
	
	var	request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				var spltData = resp.split("_");				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					resp = "0";
				}
				$("#ProductAvailabilityId"+currentRowNum).val(available[0]);
		    	//document.getElementById("ProductAvailabilityId"+currentRowNum).value = resp[0];		
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

function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	calculateOtherCharges();
	
/*	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}*/
	
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
//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){
	debugger;
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
		
	/*	for(var i=1;i<tablelength;i++){
			debugger;
			var value=$("#ChildProduct"+i).text();
			//var value=$('#ChildProduct'+i).find('option:selected').text();
			if(value==childname){
				alert("This child product is already exist, Please choose different child product.");
				
				return false;
			   }    
		}*/
		jQuery('.ui-autocomplete-input').each(function() {
			debugger;
			  var currentElement = $(this);
			      value = currentElement.val();
			  var id=$(this).attr("id").split("ChildProduct")[1];
			  if(value==childname && id!=rowNum){
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
//common method for revised and update po
function checkQuantity(row){debugger;
    var error=true;
	var receiveQuan=parseFloat($("#ReceivedQty"+row).val()==""?0:$("#ReceivedQty"+row).val());
	if($("#stQantity"+row).val()==""){
		return false;
	}
	var actuqlQuantity=parseFloat($("#stQantity"+row).val());
	var isUpdated=$("#isUpdated").val();
	//only for update po
	if(isUpdated=="true"){
		//this values are percentages which are getting from back End
		var increaseQuantity=parseFloat($("#increase_Quantity").val());
		var decreaseQuantity=parseFloat($("#decrease_Quantity").val());
		var currentEmpId=$("#currentEmpId").val();
		var updatePOEmpIds=$("#updatePoEmpIds").val();
		var emp=updatePOEmpIds.split(",");
		var empCount=0;
		for(var i=0;i<emp.length;i++){
			if(currentEmpId==emp[i]){
				empCount++;
			}
		}
		
		//PO Quantity
		var oldQuantity=parseFloat($("#stQuantity"+row).val());
		//getting the value after adding 20% to PO Quantity
		var upperModifiedQuantity=parseFloat((oldQuantity)*(increaseQuantity/100));
		upperModifiedQuantity=upperModifiedQuantity+oldQuantity;
		//getting the value after duducting 5% from PO Quantity
		var lowModifiedQuantity=parseFloat((oldQuantity)*(decreaseQuantity/100));
		lowModifiedQuantity=oldQuantity-lowModifiedQuantity;
		/*  if entered Quantity is more than PoQuantity else 1st condition for checking weather entered quantity is morethan 5% or not inside 1st condition
		  for checking entered quantity is less than 5% and the else block for after duducting 5% Quantity from po Quantity is lessthan received Quantity.  
		 */
		if(empCount==0){
			if(actuqlQuantity>oldQuantity ){
				if(actuqlQuantity>upperModifiedQuantity){
					alert("Unable to update more than quantity("+upperModifiedQuantity+").");
					$("#stQantity"+row).val('');
					return error=false;
				}
			}
			else{
				var enterUpTo;
				if(lowModifiedQuantity>receiveQuan){
					enterUpTo=lowModifiedQuantity;
				}else{
					enterUpTo=receiveQuan;
				}
				if(actuqlQuantity<enterUpTo){
					alert("Unable to update less than quantity("+enterUpTo+ ").");
					$("#stQantity"+row).val('');
					return error=false;
				}				
			}
		}				
	}else{
		if(actuqlQuantity>receiveQuan){
			//return true;
		}else{			
			alert("Unable to update less than quantity("+receiveQuan+").");
			$("#stQantity"+row).val('');
			//$("#stQantity"+row).focus();
			return false;
		}
	}
}
function checkUpdatePoQuantity(row){debugger;
	var actuqlQuantity=$("#stQantity"+row).val();
	var oldQuantity=$("#stQuantity"+row).val();
	var modifiedQuantity=Math.round((oldQuantity)*(120/100));
	if(modifiedQuantity>=actuqlQuantity){
		return true;
	}else{
		alert("Unable to update more than quantity("+modifiedQuantity+").");
		$("#stQantity"+row).val('');
		return false;
	}
		//$("#stQuantity"+row).val();Math.round(totalBasicAmount * 100)/100;
	
}

	/*====================================== for checking boq quantity when po is updated start==============================================*/

function validateUpdatePOBOQQuantity(id){debugger;

if($("#stQantity"+id).val()=="" || $("#stQantity"+id).val()=="0"){
	return false;
}

$(".loader-sumadhura").show();
var siteId=$("#toSiteId").val();
var childProductId=$("#childProduct"+id).val().split("$")[0];
var groupId=$("#groupId"+id).val();
//var indentNumber=$("#IndentNumberId").val();
var allSiteIds=$("#allSiteIds").val().split(",");
var count=0;
for (var i = 0; i < allSiteIds.length; i++) {
	if(allSiteIds[i]==siteId){
		count++;
	}
}
if(count==0){
	var matchedEnteredQty=0;
	$(".productRow").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		var currentGroupId=$("#groupId"+currentId).val();
		if(currentGroupId==groupId /*&& $("#isDelete"+currentId).val()!="d"*/){
			matchedEnteredQty+=parseFloat($("#stQantity"+currentId).val()==""?0:$("#stQantity"+currentId).val())-parseFloat($("#stQuantity"+currentId).val()==""?0:$("#stQuantity"+currentId).val());
		}			
	})
	matchedEnteredQty=matchedEnteredQty.toFixed(3);
	var url="gettingPOBoqQuantityAjax.spring?childProductId="+childProductId+"&groupId="+groupId+"&siteId="+siteId;   //+"&indentPendingQuantity="+matchedEnteredQty+"&indentNumber="+indentNumber;
	$.ajax({
		 url : url,
		 type : "post",
		 success : function(data) {				 
			$(".loader-sumadhura").hide();
			//console.log(JSON.stringify(data).xml.STATUS);
			/*if(data.xml.STATUS==false){
				alert("You can not initiate Child Product "+$("#comboboxsubSubProd"+id).val().split("$")[1]+" more than "+data.xml.BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
				$("#RequiredQuantity"+id).val('');
				$("#RequiredQuantity"+id).focus();					
				return false;
			}else{
				console.log("Status: "+data.xml.STATUS);
			}*/	
			var avalibleQty=data.split("_")[0];
			var BOQQTY=data.split("_")[1];
			avalibleQty=parseFloat(avalibleQty).toFixed(3);
			if(parseFloat(matchedEnteredQty)>parseFloat(avalibleQty) &&  groupId!='0'){
				alert("You can not initiate Child Product "+$("#subProduct"+id).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
				$("#stQantity"+id).val('');
				$("#stQantity"+id).focus();					
				return false;					
			}else{
				console.log("Success");
			}
		}
	});
}else{
	console.log("Site Matched.");
	$(".loader-sumadhura").hide();
}

}

		/*================================================ revised boq quantity check here start==========================================*/
function validateRevisedBOQQuantity(id, type){debugger;
if($("#stQantity"+id).val()=="" || $("#stQantity"+id).val()=="0"){
	return false;
}
var siteId=$("#toSiteId").val();
var childProductId=$("#ChildProduct"+id).val().split("$")[0];
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
	$(".productRow").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		var currentGroupId=$("#groupId"+currentId).val();
		if(currentGroupId==groupId && $("#actionValueId"+currentId).val()!="R"){
			if(type=="Old" && $("#actionValueId"+currentId).val()!="N"){
				matchedEnteredQty+=parseFloat($("#stQantity"+currentId).val()==""?0:$("#stQantity"+currentId).val())-parseFloat($("#stQuantity"+currentId).val()==""?0:$("#stQuantity"+currentId).val());
			}else{
				matchedEnteredQty+=parseFloat($("#stQantity"+currentId).val()==""?0:$("#stQantity"+currentId).val());
			}			
		}			
	});
	matchedEnteredQty=matchedEnteredQty.toFixed(3);
	var url="gettingPOBoqQuantityAjax.spring?childProductId="+childProductId+"&groupId="+groupId+"&siteId="+siteId;
	$.ajax({
		 url : url,
		 async: true,
		 type : "post",
		 success : function(data) {
			console.log("data: "+data);
			var avalibleQty=data.split("_")[0];
			var BOQQTY=data.split("_")[1];
			avalibleQty=parseFloat(avalibleQty).toFixed(3);
			//var totalQty=parseFloat(QtyData[0])-parseFloat(QtyData[1])+parseFloat(QtyData[2]);
			//var BOQQty=parseFloat(QtyData[3]).toFixed(2);
			if(parseFloat(matchedEnteredQty)>parseFloat(avalibleQty) && groupId!='0'){
				$(".loader-sumadhura").hide();
				$("#stQantity"+id).val('');
				alert("You can not initiate Child Product "+$("#ChildProduct"+id).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
				$("#stQantity"+id).focus();					
				return false;
			}
			$(".loader-sumadhura").hide();
		 }
	})
}else{
	$(".loader-sumadhura").show();
}
}



