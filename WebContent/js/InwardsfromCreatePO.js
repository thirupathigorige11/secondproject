
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
        autocompleteselect: function( event, ui ) {debugger;
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
        }            
        else if(str1 == "SubProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubSubProducts(prodId, rowNum);
        }
        else if(str1 == "ChildProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
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
        else if(str1 == "UnitsOfMeasurement") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            appendvales( prodId ,prodName,rowNum);
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
  
  $(function() {debugger;
  var staffId=$("#hiddenPODate").val();

  $("#receivedDate").datepicker({
  	 
  	  dateFormat: 'dd-M-y',
  	  minDate: '-'+ staffId+'d',
  	 maxDate: new Date() 
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
  
/*  $(function() {
	  $("#expireDateId1").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:0,
		  changeMonth: true,
	      changeYear: true
	  });
  });*/
 /* $(function() {
	  $("#poDateId").datepicker({
		  
		 dateFormat: 'dd-M-y',
		 maxDate: new Date(),
		  changeMonth: true,
	      changeYear: true
	  });
  });*/
  

  $(function() {
	  $("#receivedDate").datepicker({
		  dateFormat: 'dd-M-y',
		 maxDate: new Date() ,
		 onSelect: function(dateText) {
		    // alert("Selected date: " + dateText + "; input's current value: " + this.value);
		 	
		 $("#saveBtnId").disabled = true;
		 var invoiceId=$("#InvoiceNumberId").val();
		 var vendname=$("#vendorId").val();
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
					 
					//$("#doInventoryTableId").find("*").attr("disabled", "disabled");
					 $("#saveBtnId").disabled = true;
					 
					 //hide error message
					// $("#errorMessageInvoiceNumber").hide();
				 }else{
					 $("#errorMessageInvoiceNumber").hide();
				 }
		  },
		  
		  error:  function(vName, status, er){
			  alert(data+"_"+status+"_"+er);
		
			  }
		  });
		 
		 
	}
	  });
  });



//append row to the HTML table
function appendRow(rowId) {debugger;

   var tbllength=$('#doInventoryTableId').find('tr').length;	
	// for  delete button showing in first row	
	if(tbllength==2){
		var tid=$('#doInventoryTableId tr:last').attr('id');
		var res = tid.split("debitnotetablerow")[1];
		$("#addDeleteItemBtnId"+res).show();
	}
	
	var pressedKey = window.event;
	
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewItemBtnId")) {
			document.getElementById("hiddenSaveBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;	
	    var tbl = document.getElementById("doInventoryTableId");
	    
	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowNum = getLastRowNum();
		    
		    $("#addNewItemBtnId"+rowId).hide();
		    
		    rowNum = rowNum+1;
		    $(row).attr("id", "debitnotetablerow"+rowNum);
		    $(row).attr("class", "debitnotetablerow");
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
		    document.getElementById("Product"+lastDiv).focus();
		}
	}
}

//Append Row for other charges


function appendChargesRow() {debugger;
	var tbllength=$('#doInventoryChargesTableId').find('tr').length;
	if(tbllength==2){
		var tid=$('#doInventoryChargesTableId tr:last').attr('id');
		var res = tid.split("chargesrow")[1];
		$("#addDeleteChargesItemBtnId"+res).show();
	}
	var pressedKey = window.event;
	
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewChargesItemBtnId1")) {
			document.getElementById("hiddenSaveChargesBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveChargesBtnId").value;		
	    var tbl = document.getElementById("doInventoryChargesTableId");
	    	    
	    calcuLateFinalAmount();
	   
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
			$(row).attr("class", "conveyanceRow");
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {		    	
		    	var x = document.getElementById("doInventoryChargesTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');    	
		    	createChargesCell(row.insertCell(i), i, "row", rowChargesNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }		    
		    var lastDiv = getLastChargesRowNum();
		    document.getElementById("Conveyance"+lastDiv).focus();
		}
	}
}


/************** Calculation in model pop up*************/

function calculatequantitybaseinpop(SerialNumber){debugger;

var popupQuantity= document.getElementById("stQantityPOP"+ SerialNumber).value;
var popupprice= document.getElementById("PriceId"+ SerialNumber).value;
var checkQty=validateCreditnoteQty(SerialNumber);
if(checkQty==false){
	return false;
}
//Calculating the basic amount
var popupBasicAmonmt=parseFloat(popupQuantity)*parseFloat(popupprice);
$("#BasicAmountId"+ SerialNumber).val(popupBasicAmonmt.toFixed(2));
//Calculating the discount
var popupDisocuntval=document.getElementById("Discount"+ SerialNumber).value;
var popupDiscountamtAfrTax=parseFloat(popupBasicAmonmt - ( popupBasicAmonmt*popupDisocuntval/100 ));
//Appending the discount amount
$("#amtAfterDiscount"+ SerialNumber).val(popupDiscountamtAfrTax.toFixed(2));
var popuptaxAmntVal = (popupDiscountamtAfrTax /100)* popupselectedTaxval;
//taking  the tax
calculateChangedTaxAmount(SerialNumber);
var popupselectetdtax = $('#TaxId'+SerialNumber).val();
var popupselectedTaxval = 0;
if(popupselectetdtax != "" && popupselectetdtax != '' && popupselectetdtax != null) {
	popupselectedTax1 = popupselectetdtax.split("$")[1];
	
	var popupselectedTaxval1 = popupselectedTax1;
	popupselectedTaxval = parseFloat(popupselectedTaxval1);
}
//Calculating the tax 
var popuptaxAmntVal = (popupDiscountamtAfrTax /100)* popupselectedTaxval;
popuptaxAmntVal = popuptaxAmntVal.toFixed(2);
//appending the tax amount
$("#TaxAmountId"+ SerialNumber).val(popuptaxAmntVal);
var popupamontAfterTaxVal= parseFloat(popupDiscountamtAfrTax) + parseFloat(popuptaxAmntVal);
popupamontAfterTaxVal = popupamontAfterTaxVal.toFixed(2);
$("#AmountAfterTaxId"+ SerialNumber).val(popupamontAfterTaxVal);
$("#TotalAmountId"+ SerialNumber).val(popupamontAfterTaxVal);

CalculateTotalamnt(SerialNumber);
}
//validating credit note childproduct quantity
function validateCreditnoteQty(SerialNumber){debugger;
	var error=true;
	var childproductId=$("#comboboxsubSubProd"+SerialNumber).val().split("$")[0];
	var childQty=parseFloat($("#stQantityPOP"+SerialNumber).val());
	//childQty=childQty.toFixed(2);
	$(".productrow").each(function(){
		var id=$(this).attr("id").split("tr-class")[1];
		var mainChildProductID=$("#childProduct"+id).val().split("$")[0];
		if(childproductId==mainChildProductID){
			var avalibleQty=parseFloat($("#POQuantity"+id).val())-(parseFloat($("#mainQantity"+id).val())+parseFloat($("#ReceivedQty"+id).val()));
			//var avalibleQty=$("#POQuantity"+id).val()-($("#mainQantity"+id).val()+$("#ReceivedQty"+id).val());
			//avalibleQty=parseFloat(avalibleQty).toFixed(2);
			if(childQty>avalibleQty){
				swal("Error..!", "You can't initiate more than available quantity!", "error");
				$("#stQantityPOP"+SerialNumber).val("");
				return error=false;
			}
		}
	});
	return error;
}

function CalculateTotalamnt(SerialNumber){debugger;
       var credit_for=$("#credit_for").val();
	   var sum = 0.00;
	   if(credit_for=="QUANTITY"){
		    $('.totalAmount').each(function() {
		        sum += Number($(this).val());
		    });
		    document.getElementById("creditTotalAmountId").value = sum;
		    document.getElementById("CreditAmountDiv").innerHTML = sum;
	   }
	   if(credit_for=="PRICE"){
			 var creditNotePrice=$("#creditNotePrice").val();
			 $("#CreditAmountDiv").text(parseFloat(creditNotePrice).toFixed(2));
			 $("#creditTotalAmountId").val(parseFloat(creditNotePrice).toFixed(2));
	   }
	   if(credit_for=="CONVEYANCE"){
			 var creditNoteConveyanceAmount=$("#creditNoteConveyanceAmount").val();
			 $("#CreditAmountDiv").text(parseFloat(creditNoteConveyanceAmount).toFixed(2));
			 $("#creditTotalAmountId").val(parseFloat(creditNoteConveyanceAmount).toFixed(2));
	   }
	   var totalAmt=  $("#finalAmntDiv").text();
	   var CreditAmountDiv=$("#CreditAmountDiv").text();
	   var popupamontAfterTaxVal = document.getElementById("TotalAmountId"+ SerialNumber).value || 0;
	   var finalAmount=parseFloat(totalAmt)+ parseFloat(CreditAmountDiv);
	    document.getElementById("toatlAmntDiv").innerHTML =finalAmount.toFixed(2);
}

/******** Method for Validating the Quantity**********/


function validateQuantity(rowId){debugger;
var n=1;
 var enterQuantity= $("#mainQantity"+ rowId).val();
 var ReceivedQty=$("#ReceivedQty"+rowId).val();
 var EnterQuant= parseFloat(enterQuantity);
 var compareQty=EnterQuant+parseFloat(ReceivedQty);
 var  mainQuantity= $("#hiddenQantity"+ rowId).val();
 var MainQuant = parseFloat(mainQuantity);
 if(enterQuantity<0){
	 alert("Please enter valid quantity");
	 $("#mainQantity"+ rowId).val('');
	 $("#mainQantity"+ rowId).css({
		  "border":"1px solid red"
	  });
	 return false;
 }
 else{
	 $("#mainQantity"+ rowId).css({
		  "border":"1px solid #ddd"
	  });
	 var tablelength=$("#doInventoryTableId-Main > tbody > tr").length;
	 if(tablelength==1){
		 var qty=$("#mainQantity1").val();
		 if(qty==''){
			 alert("Please enter Quantity.");
	    	  $("#mainQantity1").removeAttr("tabindex");
		 }
	 }
	 else{
		 $("#mainQantity"+ rowId).attr("tabindex", rowId);
	 }
 }
 
 if(MainQuant < compareQty.toFixed(3)){debugger;
  alert('Please enter less quantity than actual quantity.');
  $("#mainQantity"+ rowId).val('');  
  $("#mainQantity"+ rowId).removeAttr("tabindex");
  $("#mainQantity"+ rowId).css({
	  "border":"1px solid red"
  });
  return false;
 }
 else{
	 $("#mainQantity"+ rowId).css({
		  "border":"1px solid #ddd"
	  });
 }
var tablelength=$("#doInventoryTableId-Main > tbody > tr").length;
if(tablelength>1){
 if(rowId==tablelength){
	  var hasInput=false;
      $('.qtyinput').each(function () {
       if($(this).val()  !== ""){
        hasInput=true;
        $(this).unbind('blur');
       }
      }); 
      console.log(hasInput);
      if(!hasInput){
    	  alert("Please enter Quantity.");
    	  $("#mainQantity1").focus();    	 
    	 return false;
    	
       }else{
    
       }
 }
}
 maincalculatequantitybase(rowId);
 validateUnitsAndAvailability(rowId);
}

  // Calculation for quantity base

function maincalculatequantitybase(rowId){debugger;

var Quantity= document.getElementById("mainQantity"+ rowId).value;


if (Quantity == '0' ){
	$("#tr-class"+rowId).addClass('strikeout');
	
	$('#mainQantity'+ rowId).attr('readonly', 'true');	
	$('#mainsnoDivId'+ rowId).removeAttr('id');	
	$('#mainPriceId'+rowId).removeAttr('id');
	$('#mainTaxAmountId'+rowId).removeAttr('id');
	$('#mainBasicAmountId'+rowId).removeAttr('id');
	$('#maintax'+rowId).removeAttr('id');
	$('#mainQuantityId'+rowId).removeAttr('id');
	$('#mainAmountAfterTaxId'+rowId).removeAttr('id');
	$('#mainOtherOrTransportChargesId'+rowId).removeAttr('id');
	$('#mainTaxOnOtherOrTransportChargesId'+rowId).removeAttr('id');
	$('#mainOtherOrTransportChargesAfterTaxId'+rowId).removeAttr('id');
	$('#mainTotalAmountId'+rowId).removeAttr('id');
}
if (Quantity == '' ){
	Quantity=0;
	//$("#mainQantity"+ rowId).val('0');
}

var price= document.getElementById("mainprice"+ rowId).value;

//Calculating the basic amount
var BasicAmonmt=parseFloat(Quantity)*parseFloat(price);
document.getElementById("mainBasicAmountId"+ rowId).value = BasicAmonmt;

// Calculating the discount
var Disocuntval=document.getElementById("mainDiscount"+ rowId).value;
var DiscountamtAfrTax=parseFloat(BasicAmonmt - ( BasicAmonmt*Disocuntval/100 ));

// Appending the discount amount
document.getElementById("mainamtAfterDiscount"+ rowId).value = DiscountamtAfrTax;

//taking  the tax
var e1 = document.getElementById("maintaxAmount"+rowId);
var selectetdtax = e1.options[e1.selectedIndex].value;

if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
	selectedTax1 = selectetdtax.split("$")[1];
	
	var selectedTaxval1 = selectedTax1;
	var selectedTaxval = parseFloat(selectedTaxval1);
}


// Calculating the tax 
var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;

// appending the tax amount
document.getElementById("mainTaxAmount"+ rowId).value = taxAmntVal;

var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
document.getElementById("mainTaxAftertotalAmount"+ rowId).value = amontAfterTaxVal;

maincalculateOtherCharges()
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
	var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;
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
	var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;
	return parseInt(lastDivVal);
}
  

//********************************************************

function deleteRow(btn, currentRow) {
	debugger;	
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryChargesTableId').find('tr').length;
	
	//removing row
	
	if(rowscount==2){
		alert("This row can't be deleted.");
		return false;
	}
   /*$("#chargesrow"+currentRow).remove();*/
	var prevAction = $("#actionStatus"+currentRow).val();
	$("#chargesrow"+currentRow).addClass('strikeout');
	$("#addEditChargesItemBtnId"+currentRow).prop('disabled', true);
	$("#ConveyanceAmount"+currentRow).addClass("no-cursor").attr('readonly',true);
	$("#Conveyance"+currentRow).addClass("no-cursor").attr('readonly', true);
	$("#GSTTax"+currentRow).addClass("no-cursor").attr('readonly', true);
	 $("#actionStatus"+currentRow).val("D");
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
	if(rowscount==3){
		$("#addDeleteChargesItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calcuLateFinalAmount();
}
//deleting debit note table row
function deleteDebitNoteRow(btn, currentRow) {
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryTableId').find('tr').length;
	//removing row	
	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}	
	 swal({
         title: "Are you sure ??",
         text: "Do you want to delete!",
         icon: "warning",
         buttons: true,
         dangerMode: true,
     }).then(function(isConfirm){
    	 if (isConfirm) {
 		    swal("Deleted!", "Table row has been deleted.", "success");
 		   $("#debitnotetablerow"+currentRow).remove();			
			var tid=$('#doInventoryTableId tr:last').attr('id');	
			var res = tid.split("debitnotetablerow")[1];
			if(rowscount==3){
				$("#addDeleteItemBtnId"+res).hide();
			}
			if(res<currentRow){		
				$("#addNewItemBtnId"+res).show();
			}	
 		  }
     });
}







/*************start cal price amount *******************/
function calculatePriceAmount(qtyNum) {
	var qty = "mainQantity"+qtyNum;
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
/*$(function() {
	$('#price1').click(function (strSerialNumber) {*/
function calculateTotalAmount(strSerialNumber) {debugger;
$//("#ChildProduct"+rowNum).val("");
	var qty = $("#mainQantity" +strSerialNumber).val();
	var amnt = $("#price"+strSerialNumber).val();	
	var  BAmnt = $("#BasicAmountId"+strSerialNumber).val();	
	var tAmnt = 0;	
	var totalAmnt = (qty*amnt);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	tAmnt = parseFloat(totalAmnt);	  
	};
/********************** Calculate Discount Amount**************************/
	
	function calculateDiscountAmount(strSerialNumber){	
		var Bamt= $("#BasicAmountId"+strSerialNumber).val();
		var  BAmnt = $("#taxAmount"+strSerialNumber).val(DamtAfrTax);
		var numbers = $("#Discount"+strSerialNumber).val();
		var DamtAfrTax=Bamt - ( Bamt*numbers/100 );
		$("#amtAfterDiscount"+strSerialNumber).val(DamtAfrTax);
	}
	
	//********* Tax calculation for Grid one ***************
	function calculateTaxAmount(rowNum) {
		debugger;
		var e = document.getElementById("taxAmount"+rowNum);
		var selectedTax = e.options[e.selectedIndex].value;		
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];
			var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;
			percentage(basicAmnt, selectedTax, rowNum);
		}
		else {
			document.getElementById("TaxAmountId"+rowNum).value = "";
			document.getElementById("AmountAfterTaxId"+rowNum).value = "";
		}	
	}
	
	//********* Tax calculation for changed Tax ***************
	function calculateChangedTaxAmount(rowNum) {
		debugger;
		var e = document.getElementById("TaxId"+rowNum);		
		var selectedTax = e.value;		
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];
			var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;			
			taxPercentage(basicAmnt, selectedTax, rowNum);
		}
		else {
			document.getElementById("TaxAmountId"+rowNum).value = "";
			document.getElementById("AmountAfterTaxId"+rowNum).value = "";
		}	
	}
	//********** End Tax calculation for grid one***************

	//********** End Tax calculation for grid one***************
	
	
	function calcuLateFinalAmount() {		
		document.getElementById("hiddenSaveBtnId").value = "";		
		var allElements = document.getElementsByTagName("*");		
		var pipeData = "";
		for (var i = 0, n = allElements.length; i < n; ++i) {
		  	var el = allElements[i];
		  	if (el.id) {
				var ask = el.id;
				if(ask.indexOf("TotalAmountId") != -1) {
					pipeData = pipeData+(el.id)+"|";
				}
		  	}
		}
		
		var data = pipeData.split("|");
		var lastDiv = data.length-1;
		var tempAmnt = 0;
		
		for(var x=0; x < lastDiv; x++) {
	    	var fldName = data[x];	    	
	    	if(fldName.indexOf("TotalAmountId") != -1) {
	    		var fldAmnt = document.getElementById(fldName).value;
	    		if(fldAmnt != "" && fldAmnt != null && fldAmnt != '') {
	        		tempAmnt = parseFloat(tempAmnt) + parseFloat(fldAmnt);
	        		tempAmnt = Math.round(tempAmnt * 100)/100;
	    		}
			}	
	    }
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
function getAllProdsCount() {debugger;
	
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
	var poNumber=$("#PONOId").val();
	var reqSiteId=$("#toSiteId").val();	
	var url="indentReceiveSubProductsByPONumber.spring?mainProductId="+prodId+"&poNumber="+poNumber+"&reqSiteId="+reqSiteId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#ChildProduct"+rowNum).val("");
				$("#SubProduct"+rowNum).val("");				
				$("#UnitsOfMeasurementId"+rowNum).val("");				
				$("#TaxId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				var spltData = resp.split("|");
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSelect = "comboboxsubProd"+rowNum;
		    	var selectBox = document.getElementById(subProdToSelect);			    
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
	var poNumber=$("#PONOId").val();
	var reqSiteId=$("#toSiteId").val();	
	var url="indentReceiveChildProductsByPONumber.spring?subProductId="+subProdId+"&poNumber="+poNumber+"&reqSiteId="+reqSiteId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");
				$("#TaxId"+rowNum).val("");
				
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

function loadUnits(prodId, rowNum) {	
	prodId = prodId.split("$")[0];	
	var url = "listIndentReciveUnitsOfChildProducts.spring?productId="+prodId;	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
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



//********************** Tax calculation GST for second grid*****************

function calculateGSTTaxAmount(rowNum) {
	debugger;
	var e = document.getElementById("GSTTax"+rowNum);
	var selectedTax = e.value;
	
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

function removeRow(){debugger;
alert(" Do you want to remove the Product");
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

function editInvoiceRow(rowId){debugger;

alert("Do you want to update the Po product row " + rowId);

	$('#mainQantity'+rowId).prop('readonly', false);
}
function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	document.getElementById("TaxAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("TaxAftertotalAmount"+rowNum).value = amntaftTx;
}
/******** Calculation for Changed tax amount percentage**********/
function taxPercentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var chnagedTaxAmnt = (basicAmnt/100) * Selectedtax;
	document.getElementById("TaxAmountId"+rowNum).value = parseFloat(chnagedTaxAmnt).toFixed(2);
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(chnagedTaxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxId"+rowNum).value = parseFloat(amntaftTx).toFixed(2);;
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

function calculuatesum(){debugger;
	
	var CreditAmount= $('#CreditAmountDiv').html();
	var TAmout = $('#finalAmntDiv').html();
	document.getElementById("toatlAmntDiv").innerHTML 	= parseFloat(CreditAmount) + parseFloat(TAmout);
	
}
//***********************************************
function checkQuantityEnteredOrNot() {
	var resetSno = maingetTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	var resetSnoId = resetSpltdData.length - 1;
	for(var x=0; x<resetSnoId; x++) {

		var resetval = resetSpltdData[x];
		var mainQuan =  document.getElementById("mainQantity"+resetval).value;
		if(mainQuan == "" || mainQuan == null || mainQuan == '') {
			alert("Please enter the Quantity.");
			$("#mainQantity"+resetval).css({
				 "border":"1px solid red"
			});
			return false;
		}
		else{
			$("#mainQantity"+resetval).css({
				 "border":"1px solid #ddd"
			});
		}
	}
}
//***********************************************


function validateQtyvalEmpty(){debugger;
	var count=0;
	var error=true;
	var qtyLength=$(".qtyinput").length;
	$(".qtyinput").each(function(){debugger
		if($(this).val().trim()=='' || $(this).val()=='0'){
			count++;
		}
	})
	if(qtyLength==count){
		return error=false;
	}
	return error;
}



function maincalculateOtherCharges() {
    var transportDetails = document.getElementById("Conveyance1").value;
    if(transportDetails == "" || transportDetails == '' || transportDetails == null) {
		alert("Please Select the Conveynace or None .");
		document.getElementById("Conveyance1").focus();
		return false;
	}
	
	
	
	var resetSno = maingetTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	var resetSnoId = resetSpltdData.length - 1;
	for(var x=0; x<resetSnoId; x++) {

		var resetval = resetSpltdData[x];
		 document.getElementById("mainOtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("mainTaxOnOtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("mainOtherOrTransportChargesAfterTaxId"+resetval).value = "";
		 document.getElementById("mainTotalAmountId"+resetval).value = "";
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

	 convenceCharges = Math.round(convenceCharges * 100)/100;
	var rsltTtlBscAmnt = maincalculateTotalBasicAmount();
	
	var snos = maingetTotalSNOS();
	//BasicAmountId1
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;	
	var fnlAmnt = 0;
	
	for(var x=0; x<snoId; x++) {

		var val = spltdData[x];
		var individualBasicAmount = document.getElementById("mainamtAfterDiscount"+val).value;
	
		individualBasicAmount = individualBasicAmount.trim();
		individualBasicAmount = parseFloat(individualBasicAmount);
		individualBasicAmount = Math.round(individualBasicAmount * 100)/100;
		
		var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		//charges = Math.round(charges * 100)/100;
		if (snoSpltdData[y] > 1) {
			var existVal = $("#mainOtherOrTransportChargesId"+val).val();
			var taxOnOtherCharges = $("#mainTaxOnOtherOrTransportChargesId"+val).val();
		}
		var amount = 0;
		 var result = 0;
		if (existVal == null || existVal == '' || existVal=="") {
		    document.getElementById("mainOtherOrTransportChargesId"+val).value = parseFloat(charges).toFixed(2);
		    result = parseFloat(charges).toFixed(2);
		} else {
			existVal = Math.round(existVal * 100)/100;
			result = parseFloat(charges)+parseFloat(existVal);
			document.getElementById("mainOtherOrTransportChargesId"+val).value = result.toFixed(2);

		}
		var taxAmount = (charges/100)*taxPercentage;
		
		if (taxOnOtherCharges == null || taxOnOtherCharges == '' || taxOnOtherCharges=="") {
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result);
			document.getElementById("mainTaxOnOtherOrTransportChargesId"+val).value = taxAmount.toFixed(2);
			document.getElementById("mainOtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		} else {
			var addedCharge = parseFloat(taxAmount)+parseFloat(taxOnOtherCharges);
			document.getElementById("mainTaxOnOtherOrTransportChargesId"+val).value = addedCharge.toFixed(2);
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result) +  parseFloat(taxOnOtherCharges);;
			document.getElementById("mainOtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		}
		
		//Setting Total Amount - Start
		var amntAfterTax = document.getElementById("mainTaxAftertotalAmount"+val).value;
		amntAfterTax = parseFloat(amntAfterTax);
		var othOrTransChrgsAfterTax = document.getElementById("mainOtherOrTransportChargesAfterTaxId"+val).value;
		othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);		
		var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
		ttlAmnt = Math.round(ttlAmnt * 100)/100;		
		document.getElementById("mainTotalAmountId"+val).value = parseFloat(ttlAmnt).toFixed(2);
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
	document.getElementById("ttlAmntForIncentEntryId").value = fnlAmnt;
	//Setting Final Amount - End
	//calculuatesum();
	//CalculateTotalamnt(1);
	calculateFinalAmount();
}
}
/***** Setting total amount**********/



function maincalculateTotalBasicAmount() {debugger;
	var snos = maingetTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var totalBasicAmount = 0;
	for(var x=0; x<snoId; x++) {
	
		var val = spltdData[x];
		var basicAmnt = document.getElementById("mainamtAfterDiscount"+val).value;
		basicAmnt = basicAmnt.trim();
		basicAmnt = parseFloat(basicAmnt);
		basicAmnt = Math.round(basicAmnt * 100)/100;
		totalBasicAmount = parseFloat(totalBasicAmount) + basicAmnt;
		totalBasicAmount = Math.round(totalBasicAmount * 100)/100;
	}
	return totalBasicAmount;
}


function calculateTotalBasicAmount() {debugger;
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var totalBasicAmount = 0;
	for(var x=0; x<snoId; x++) {
	
		var val = spltdData[x];
		var basicAmnt = document.getElementById("TaxAftertotalAmount"+val).value;
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

function maingetTotalSNOS() {
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("mainsnoDivId") != -1) {
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
	var last_element = sortedSnos[sortedSnos.length - 1];
	return last_element;
}

//Field Validation started
function validateRowData(strSerialNumber) {
	  
	var Quan = document.getElementById("mainQantity"+strSerialNumber).value;
	if(Quan == "" || Quan == null || Quan == '') {
		alert("Please enter Qunatity.");
		document.getElementById("Quan").focus();
		return false;
	}				
	  
	var Amt = document.getElementById("price"+strSerialNumber).value;
	if(Amt == "" || Amt == null || Amt == '') {
		alert("Please enter the price.");
		document.getElementById("Amt").focus();
		return false;
	}	  
	  
	var disc = document.getElementById("Discount"+strSerialNumber).value;	
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

function validateProductAvailability(unitsFld) {
	
	var measurementId = unitsFld.id;	
	var currentRowNum = measurementId.match(/\d+/g);
	measurementId = document.getElementById(measurementId);
	measurementId = measurementId.options[measurementId.selectedIndex].value;
	measurementId = measurementId.split("$")[0];
	
	if(measurementId != "" && measurementId != '' && measurementId != null) {	
		var pro = $("#combobox"+currentRowNum).val();
		pro = pro.split("$")[0];
		var subPro = $("#comboboxsubProd"+currentRowNum).val();
		subPro = subPro.split("$")[0];		
		var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		var requestedDate=$("#receivedDate").val();
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum,requestedDate);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}
 
function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum,requestedDate) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requestedDate;
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

function appendvalues(rowId){debugger;
	Childprod = $("#comboboxsubSubProd"+ rowId).val();
	var Childprod = Childprod.split("$")[0];
	var poNumber1=$("#PONOId").val();
	var reqSiteId1=$("#toSiteId").val();
	var  url = "getPriceRatesByChildProduct.spring?childProdId="+Childprod+"&poNumber="+poNumber1+"&reqSiteId="+reqSiteId1;
	$.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			var values = data.split(":");
			//$("#stQantityPOP"+rowId).val(values[0]);
			$("#PriceId"+rowId).val(values[1]);
			//$("#BasicAmountId"+rowId).val(values[2]);
			$("#Discount"+rowId).val(values[3]);
			//$("#amtAfterDiscount"+rowId).val(values[4]);
			$("#TaxId"+rowId).val(values[6]+"$"+values[7]+"%");
			$("#TaxIdHidden"+rowId).val(values[7]+"%");
			$("#HSNCodeId"+rowId).val(values[5]);
			//$("#TaxAmountId"+rowId).val(values[8]);
			//$("#AmountAfterTaxId"+rowId).val(values[9]);
			/*$("#OtherOrTransportChargesId"+rowId).val(values[10]);
			$("#TaxOnOtherOrTransportChargesId"+rowId).val(values[11]);
			$("#OtherOrTransportChargesAfterTaxId"+rowId).val(values[12]);*/
			//$("#TotalAmountId"+rowId).val(values[13]);
			$("#indentCreationDtlsId"+rowId).val(values[14]);
			$("#poEntryDetailsId"+rowId).val(values[15]);			
		  }
	});
}
 	
function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	calculateOtherCharges();
    var elementList = document.getElementsByTagName("*");    
    var rowNums = getAllProdsCount();    
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
//Quantity validation for accepting digits written by thirupathi
 
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && charCode!=46 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}
/*edit rows*/
function editRow(current, eid){
	debugger;
	$("#ConveyanceAmount"+eid).removeAttr('readonly');
	$("#Conveyance"+eid).removeAttr('readonly');
	$("#GSTTax"+eid).removeAttr('readonly');
	$("#actionStatus"+eid).val("E");
}

/*auto complete for transportor start*/
function populateData() {
	
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
		  				setVendorData (b.item.label);
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
function setVendorData(vName) {
	
	var url = "getTransportorId.spring?transportorName="+vName;
	  
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
		$("#transporterNameId").val(resp);		
	}
}




/*edit rows*/
/*delete row*/
function deleteConveyanceRow(current, id){
	debugger;
  var conveyanceTablelength =  $(".conveyanceRow").length;
  var hiddenRowcount = $(".hiddentablestikeout").length;
  var TotalrowCount = conveyanceTablelength - hiddenRowcount;
  var deletedRowscount = $(".hiddentablestikeout").val();
  
  
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
	$("#chargesrow"+id).remove();
	var tid=$('.conveyanceRow:last').attr('id');
	var res = tid.split("chargesrow")[1];
	/*if(conveyanceTablelength==3){
		$("#addDeleteItemBtnId"+res).hide();
		return false;
	}*/
	if(res<id){
		$("#addNewChargesItemBtnId"+res).show();
	}
}
	/************************************ check the boq quantity for one start***************************************************/
function validateUnitsAndAvailability(currentRowNum) {
	debugger;
	//var qtyObjectId = qtyObj.id;	
	//var currentRowNum = qtyObjectId.match(/\d+/g);	
	var qty = "";
	var productAva = "";	
	qty = document.getElementById("mainQantity"+currentRowNum).value;
	qty = parseFloat(qty);
	var allSiteIds=$("#allSiteIds").val().split(",");
	var siteId=$("#siteIdId").val();
	var groupId=$("#groupId"+currentRowNum).val();
	if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
		alert("Please enter valid quantity.");
		document.getElementById("mainQantity"+currentRowNum).value = "";
		document.getElementById("mainQantity"+currentRowNum).focus();
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
		var totalQty=parseFloat($("#avalBOQQty"+currentRowNum).val());
		var BOQQty=parseFloat($("#BOQQty"+currentRowNum).val()).toFixed(3);
		var matchedEnteredQty=0;
		$(".productrow").each(function(){
			var id=$(this).attr("id").split("tr-class")[1];
			var currentGroupId=$("#groupId"+id).val();
			if(currentGroupId==groupId){
				matchedEnteredQty+=parseFloat($("#mainQantity"+id).val()==""?0:$("#mainQantity"+id).val());
			}			
		})
		debugger;
		matchedEnteredQty=matchedEnteredQty.toFixed(3);
		if(parseFloat(matchedEnteredQty)>parseFloat(totalQty) && groupId!='0'){
			alert("You can not initiate Child Product "+$("#childProduct"+currentRowNum).val()+" more than "+BOQQty+" "+$("#mainUnitsOfMeasurementId"+currentRowNum).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
			$("#mainQantity"+currentRowNum).val('');
			$("#mainQantity"+currentRowNum).focus();
			return false;
		}
		
	}else{
		console.log("  Present");
		
	}

	
	
	
	//productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
	//productAva = parseFloat(productAva);	
}



/*delete row*/

//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){debugger;
    var rv = true;
	$("#ChildProduct"+rowNum).val("");
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
		debugger;
		jQuery('.ui-autocomplete-input').each(function() {
			  var currentElement = $(this);
			      value = currentElement.val(); 
			  if(value==childname){
				swal("Error..!", "This child product is already exist, Please choose different child product.", "error")
				return rv = false;
			   }     
			  else{				  
				  return rv = true;				  
			  }
		});
	}
	 return rv;
}
