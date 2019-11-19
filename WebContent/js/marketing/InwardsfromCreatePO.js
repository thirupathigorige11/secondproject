var childproduct;
var childprdctquantity;
var childproductID;
var childproductTotalAmount;

//loading childproduct and childproduct TotalAmount
function loadChildQty(){
	childproduct =[];
	childproductTotalAmount = [];
	//reading child product from product table and pushing to childproduct
	$(".childprdctcls").each(function(){
		var tempval = $(this).val();
		childproduct.push(tempval);
	});
	//reading child product TotalAmount from product table and pushing to childproductTotalAmount
	$(".ttamount").each(function(){
		var tempval = $(this).val();
		childproductTotalAmount.push(tempval);
	});
}


var siteNamesFromAjax;
//opening calculation expendature modal popup with checking validation
function openCalExp(){debugger;
	//validating vendor details
	var valVendorStatus=validateVendorDetails();
	if(valVendorStatus==false){
		return false;
	}
	//validating product table
	var flag = checkQuantityEnteredOrNot();
	if(flag==false){
		return false;
	}
	maincalculateOtherCharges();
	loadChildQty();
	
	childprdctquantity = [];
	//reading child product Quantity from product table and pushing to childprdctquantity
	$(".qtyinput").each(function(){
		var tempval = $(this).val();
		if(tempval!=0){
			childprdctquantity.push(tempval);
		}		
	});
	$("#anchorMarket").attr("data-toggle", "modal");
	$("#anchorMarket").attr("data-target", "#modal-marketing");	
	$("#Quantity").val(sessionStorage.tomodalqunval);
	$('#Quantity').bind('paste', function (e) { e.preventDefault();	});
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
      .attr("title", "")
      .attr("id", this.element[0].name)
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
            loadUnits(prodId, rowNum);
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
	  /*$("#receivedDate").datepicker({
	  	  dateFormat: 'dd-M-y',
	  	  minDate: '-'+ staffId+'d',
	  	 maxDate: new Date() 
	  });*/
  });
  
  $(function() {
	  $("#InvoiceDateId").datepicker({ 
		  dateFormat: 'dd-M-y',
		  maxDate: new Date(),
	  changeMonth: true,
      changeYear: true 
	  });
  });
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
		 $("#saveBtnId").disabled = true;
		 var invoiceId=$("#InvoiceNumberId").val();
		 var vendname=$("#vendorId").val();
		 var recedate=dateText;
		 var vName = "VND03";
		 var url = "getReceiveCount.spring?invoiceNumber="+invoiceId+"&vendorname="+vendname+"&receiveDate="+recedate;
		 $.ajax({
		  url : url,
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			 	 if (data>0){
					 //server sent response as false, not valid data
					$("#errorMessageInvoiceNumber").show();
					$("#InvoiceNumberId").val("");
					 $("#InvoiceNumberId").focus();
					 
					 $("#saveBtnId").disabled = true;
					 
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

//Append Row for other charges
function appendChargesRow() {
	var tbllength=$('#doInventoryChargesTableId').find('tr').length;
	//displaying delete button 
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
		    $("#Conveyance"+lastDiv).focus();
		}
	}
}


/************** Calculation in model pop up*************/

function calculatequantitybaseinpop(SerialNumber){debugger;

	var popupQuantity= document.getElementById("stQantityPOP"+ SerialNumber).value;
	var popupprice= document.getElementById("PriceId"+ SerialNumber).value;
	
	//Calculating the basic amount
	var popupBasicAmonmt=parseFloat(popupQuantity)*parseFloat(popupprice);
	document.getElementById("BasicAmountId"+ SerialNumber).value = popupBasicAmonmt;
	
	//Calculating the discount
	var popupDisocuntval=document.getElementById("Discount"+ SerialNumber).value;
	var popupDiscountamtAfrTax=parseFloat(popupBasicAmonmt - ( popupBasicAmonmt*popupDisocuntval/100 ));
	
	//Appending the discount amount
	document.getElementById("amtAfterDiscount"+ SerialNumber).value = popupDiscountamtAfrTax;
	
	var popuptaxAmntVal = (popupDiscountamtAfrTax /100)* popupselectedTaxval;
	//taking  the tax
	
	/*var e1 = document.getElementById(popuptax1);
	alert(e1)*/;
	calculateChangedTaxAmount(SerialNumber);
	var popupselectetdtax = $('#TaxId1').val();
	
	if(popupselectetdtax != "" && popupselectetdtax != '' && popupselectetdtax != null) {
		popupselectedTax1 = popupselectetdtax.split("$")[1];
		
		var popupselectedTaxval1 = popupselectedTax1;
		var popupselectedTaxval = parseFloat(popupselectedTaxval1);
	}
	
	
	//Calculating the tax 
	var popuptaxAmntVal = (popupDiscountamtAfrTax /100)* popupselectedTaxval;
	popuptaxAmntVal = popuptaxAmntVal.toFixed(2);
	//appending the tax amount
	document.getElementById("TaxAmountId"+ SerialNumber).value = popuptaxAmntVal;
	
	var popupamontAfterTaxVal= parseFloat(popupDiscountamtAfrTax) + parseFloat(popuptaxAmntVal);
	popupamontAfterTaxVal = popupamontAfterTaxVal.toFixed(2);
	document.getElementById("AmountAfterTaxId"+ SerialNumber).value = popupamontAfterTaxVal;
	
	 document.getElementById("TotalAmountId"+ SerialNumber).value = popupamontAfterTaxVal;
	
	CalculateTotalamnt(1);
}

function CalculateTotalamnt(SerialNumber){debugger;
	   var sum = 0.00;
	    $('.totalAmount').each(function() {
	        sum += Number($(this).val());
	    });
	    document.getElementById("creditTotalAmountId").value = sum;
	    document.getElementById("CreditAmountDiv").innerHTML = sum;
	   var totalAmt=  $("#finalAmntDiv").text();
	   var popupamontAfterTaxVal = document.getElementById("TotalAmountId"+ SerialNumber).value || 0;
	   //document.getElementById("toatlAmntDiv").innerHTML =parseFloat(totalAmt)- parseFloat(popupamontAfterTaxVal);
	   var totalAmountVal=parseFloat(totalAmt)- parseFloat(popupamontAfterTaxVal);
	   $("#toatlAmntDiv").text(totalAmountVal.toFixed(2));
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
 
 if(MainQuant < compareQty){
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
      if(!hasInput){
    	  alert("Please enter Quantity.");
    	  $("#mainQantity1").focus();    	 
    	 return false;
    	
       }
 }
}
 maincalculatequantitybase(rowId);
}

// Calculation for quantity base
function maincalculatequantitybase(rowId){
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
		$("#actionVal"+rowId).val("d");
	}
	if (Quantity == '' ){
		Quantity=0;
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
	//document.getElementById("mainTaxAmount"+ rowId).value = taxAmntVal;
	$("#mainTaxAmount"+ rowId).val(parseFloat(taxAmntVal).toFixed(2));
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	//document.getElementById("mainTaxAftertotalAmount"+ rowId).value = amontAfterTaxVal;
	$("#mainTaxAftertotalAmount"+ rowId).val(parseFloat(amontAfterTaxVal).toFixed(2));
	
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
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryChargesTableId').find('tr').length;
	if(rowscount==2){
		alert("This row can't be deleted.");
		return false;
	}
	//removing row
   $("#chargesrow"+currentRow).remove();
	
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

function calculateTotalAmount(strSerialNumber) {
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
	var e = document.getElementById("TaxId"+rowNum);
	var selectedTax = e.options[e.selectedIndex].value;
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;
			taxPercentage(basicAmnt, selectedTax, rowNum);
	}else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxId"+rowNum).value = "";
	}	
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
	/*document.getElementById("finalAmntDiv").innerHTML = tempAmnt;
	document.getElementById("ttlAmntForIncentEntryId").value = tempAmnt;*/
	$("#finalAmntDiv").text(parseFloat(tempAmnt).toFixed(2));
	$("#ttlAmntForIncentEntryId").val(parseFloat(tempAmnt).toFixed(2));
}
//get charges row table length
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
//get product row table length
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
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}
//loading sub products when you are changing product
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
//loading childproduct when you are changing subproduct
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
//loading Units Of Measurement when you are changing childproduct
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

function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	//alert("Tax Amount = "+taxAmnt);
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
	//alert("Tax Amount = "+taxAmnt);
	document.getElementById("TaxAmountId"+rowNum).value = parseFloat(chnagedTaxAmnt).toFixed(2);
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(chnagedTaxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxId"+rowNum).value = parseFloat(amntaftTx).toFixed(2);
}
//************ Calculation for Conveyance AMount*******

function GSTpercentage(basicAmnt, tax, rowNum) {
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	document.getElementById("GSTAmount"+rowNum).value = taxAmnt;
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTax"+rowNum).value = amntaftTx;
}

//*****************************************************
function calculuatesum(){debugger;
	
	var CreditAmount= $('#CreditAmountDiv').html();
	var TAmout = $('#finalAmntDiv').html();
	//document.getElementById("toatlAmntDiv").innerHTML 	= parseFloat(CreditAmount) + parseFloat(TAmout);
	var totalAmount=parseFloat(CreditAmount) + parseFloat(TAmout);
	$("#toatlAmntDiv").text(totalAmount.toFixed(2));
	
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
//validating quantity
function validateQtyvalEmpty(){
	var count=0;
	var error=true;
	var qtyLength=$(".qtyinput").length;
	$(".qtyinput").each(function(){
		if($(this).val().trim()=='' || $(this).val()=='0'){  count++; 	}
	})
	if(qtyLength==count){ return error=false; }
	return error;
}
//calculating other charges with product table
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
	//document.getElementById("finalAmntDiv").innerHTML = fnlAmnt;
	//document.getElementById("ttlAmntForIncentEntryId").value = fnlAmnt;
	
	$("#finalAmntDiv").text(parseFloat(fnlAmnt).toFixed(2));
	$("#ttlAmntForIncentEntryId").val(parseFloat(fnlAmnt).toFixed(2));
	//Setting Final Amount - End
		
	CalculateTotalamnt(1);
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
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {    return false;     }
    //just one dot
    if(number.length > 1 && charCode == 46) { return false;  }
    //get the carat position
    var caratPos = getSelectionStart(el);
    var dotPos = el.value.indexOf(".");
    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){  return false; }
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

function appendvalues(rowId){
	Childprod = $("#comboboxsubSubProd"+ rowId).val();
	var Childprod = Childprod.split("$")[0];
	var poNumber1=$("#PONOId").val();
	var reqSiteId1=$("#toSiteId").val();
	var  url = "getPriceRatesByChildProduct.spring?childProdId="+Childprod+"&poNumber="+poNumber1+"&reqSiteId="+reqSiteId1;
	$.ajax({
		  url : url,
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			var values = data.split(":");
			$("#stQantityPOP"+rowId).val(values[0]);
			$("#PriceId"+rowId).val(values[1]);
			$("#BasicAmountId"+rowId).val(values[2]);
			$("#Discount"+rowId).val(values[3]);
			$("#amtAfterDiscount"+rowId).val(values[4]);
			$("#TaxId"+rowId).val(values[6]+"$"+values[7]+"%");
			$("#HSNCodeId"+rowId).val(values[5]);
			$("#TaxAmountId"+rowId).val(values[8]);
			$("#AmountAfterTaxId"+rowId).val(values[9]);
			$("#OtherOrTransportChargesId"+rowId).val(values[10]);
			$("#TaxOnOtherOrTransportChargesId"+rowId).val(values[11]);
			$("#OtherOrTransportChargesAfterTaxId"+rowId).val(values[12]);
			$("#TotalAmountId"+rowId).val(values[13]);
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
    if (charCode > 31 && charCode!=46 && (charCode < 48 || charCode > 57)) { return false; }
    return true;
}

var childData;
var siteNamechild;
var locationChild;

$(function(){
	 var tomodalqunval = $("#quntval").val();
	 sessionStorage.tomodalqunval = tomodalqunval;
});
//this method for appending row in multi site table plus button 
function appendModalRow(id){
	var valtable=validatingMultiSiteTable();
	if(valtable==false){
		return false;
	}
	var qtytype="'qty'";
	var amttype="'amount'";	
    var newId=parseInt($(".expenditure:last").attr("id").split("multisiteAdd")[1])+1;
	var addRow='<tr id="multisiteAdd'+newId+'" class="expenditure"><td>'+newId+'</td><td><select  onchange="expendituremultiChildProductChange('+newId+', this)" id="exapendChildproduct'+newId+'"  name="exapendChildproduct'+newId+'" class="form-control childprdctclsmulti"></select></td><td><select class="form-control multiareasclass" id="multisiteareaid'+newId+'" name="multisiteareaid'+newId+'" onchange="getMultiSiteDatesAndQuantity('+newId+')"></select></td><td><select id="sitenameid'+newId+'" name="sitenameid'+newId+'" class="form-control multsitenamecls" onchange="getMultiSiteDatesAndQuantity('+newId+')"></select></td><td><input id="multisitequanty'+newId+'"  name="multisitequanty'+newId+'" type="text" class="form-control" onkeypress="return isNumber(this, event)" onkeyup="multisiteWiseQtyAndAmountChange('+newId+', '+qtytype+')"/></td><td><input type="text" class="form-control classDatepicker readonly-color" readonly="true" name="multisitefromDate'+newId+'" id="multisitefromDate'+newId+'" placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" readonly="true" id="multisitetoDate'+newId+'" name="multisitetoDate'+newId+'" placeholder="Select To Date"/></td><td><input type="text" class="form-control timepicker" id="MultiSiteWiseTime'+newId+'" name="MultiSiteWiseTime'+newId+'" placeholder="Select Time"></td><td><input type="text" id="multisplitamount'+newId+'" name="multisplitamount'+newId+'" onkeypress="return isNumber(this, event)" onkeyup="multisiteWiseQtyAndAmountChange('+newId+', '+amttype+')"  class="form-control"/></td><td><input type="text" class="form-control multiSitetotalamountclass" id="totalamountmultsite'+newId+'" name="totalamountmultsite'+newId+'" onkeypress="return isNumber(this, event)" onkeyup="multiSiteTotalChange('+newId+', this)"></td><td><button type="button" class="btnaction btnpadd" onclick="appendModalRow('+newId+')"><i class="fa fa-plus"></i></button><button class="btnaction" type="button" onclick="deleteRow('+newId+')"><i class="fa fa-trash"></i></button></td></tr>';
	$("#multisiteAdd").append(addRow);
	debugger;
	$('#MultiSiteWiseTime'+newId).timepicker({
		 timeFormat: 'h:mm p',
		    interval: 10,
		    minTime: '00.00AM',
		    maxTime: '11.59PM',
		    startTime: '00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
		});
		  
		  $("#multisitetoDate"+newId).datepicker({ 
			  dateFormat: 'dd-M-y',
			  /* maxDate:0,*/
		     changeMonth: true,
	         changeYear: true
		  });
		  $(function() {
			  $(".classDatepicker ").datepicker({ 
				  dateFormat: 'dd-M-y',
				  /* maxDate:0,*/
			     changeMonth: true,
		         changeYear: true
			  });
		});
	
	var addsiteName = "<option value=''>--Please Select Site--</option>";
	 if(siteNamesFromAjax.xml.site.SITEID){
		  addsiteName+='<option value="'+siteNamesFromAjax.xml.site.SITEID+"$"+siteNamesFromAjax.xml.site.SITENAME+'">'+siteNamesFromAjax.xml.site.SITENAME+'</option>';
	  }
	  else{
		  for(var i=0; i<siteNamesFromAjax.xml.site.length;i++){
			 addsiteName+='<option value="'+siteNamesFromAjax.xml.site[i].SITEID+"$"+siteNamesFromAjax.xml.site[i].SITENAME+'">'+siteNamesFromAjax.xml.site[i].SITENAME+'</option>';
		  } 
	  }
	 $("#sitenameid"+newId).html(addsiteName);
	
	var selectoptionmulti = "<option value=''>--select--</option>";
	for(var j=0;j<childproduct.length;j++){
		if($("#mainQantity"+(j+1)).val()!=0){		
			selectoptionmulti+='<option value="'+childproduct[j]+'">'+childproduct[j].split("$")[1]+'</option>';
		}
	}
	$("#exapendChildproduct"+newId).html(selectoptionmulti);
	
	var result = eval('(' + siteNamechild + ')');
	 var addsitedata = "<option>--Please Select Site--</option>";
	for(var i=0; i<=result.xml.siteNames.site.length;i++){
		addsitedata+='<option value="'+result.xml.siteNames.siteid[i]+'">'+result.xml.siteNames.site[i]+'</option>';
	}
	$(".multisitenamecls").html(addsitedata); 
	/*----------------------Location  START-----------------------*/
	var result = eval('(' + locationChild + ')');
	 var addlocationdata = "<option>--Select Location--</option>";
	//Loaction getting
	
	for(var i=0; i<result.xml.locationname.area.length;i++){
		addlocationdata+='<option value="'+result.xml.locationname.area[i]+'">'+result.xml.locationname.area[i]+'</option>';
	}
	$("#stateSelc"+newId).html(addlocationdata); 
	
	//for datepicker
	$("#sitewisefromDate"+newId).datepicker({
		 dateFormat: 'dd-M-y',
	    changeMonth: true,
        changeYear: true
       /* maxDate:0*/
	});
	$("#sitewisetoDate"+newId).datepicker({
		 dateFormat: 'dd-M-y',
		    changeMonth: true,
	        changeYear: true
	       /* maxDate:0*/
	});
}
//load child product 
var childData;
function loadchildDataInModal(){
	childData=[];
	$(".childproduct").each(function(){
		if($(this).val()!=''){
			childData.push($(this).val());
		}
	});
}
function getSiteNamesForMulti(templength){
	var addsiteName = "<option value=''>--Please Select Site--</option>";
	$.ajax({
		  url : "siteNameDetails.spring",
		  type : "get",
		  contentType : "application/json",
		  success : function(data) {
			  siteNamesFromAjax = eval('(' + data + ')');
			  if(siteNamesFromAjax.xml.site.SITEID){
				  addsiteName+='<option value="'+siteNamesFromAjax.xml.site.SITEID+"$"+siteNamesFromAjax.xml.site.SITENAME+'">'+siteNamesFromAjax.xml.site.SITENAME+'</option>';
			  }
			  else{
				  for(var i=0; i<siteNamesFromAjax.xml.site.length;i++){
					 addsiteName+='<option value="'+siteNamesFromAjax.xml.site[i].SITEID+"$"+siteNamesFromAjax.xml.site[i].SITENAME+'">'+siteNamesFromAjax.xml.site[i].SITENAME+'</option>';
				  } 
			  }
			  for(var i=0;i<templength;i++){
					$("#sitenameid"+(i+1)).html(addsiteName);
			}
		  }
	});
}
//model LocationWise service start
function getLOcation(){
	  $.ajax({
		 	url:"siteLocationDetails.spring",
			type:"GET",
			success:function(response){
				locationChild = response;
					console.log("response: "+JSON.stringify(response));
					var result = eval('(' + response + ')');
					 var addlocationdata = "<option>--Select Location--</option>";
					//Loaction getting
					for(var i=0; i<result.xml.locationname.area.length;i++){
						addlocationdata+='<option value="'+result.xml.locationname.area[i]+'">'+result.xml.locationname.area[i]+'</option>';
					}
					$("#stateSelc").html(addlocationdata); 
					$(".multiloccls").html(addlocationdata);
			}
		}); 
}

function expenditureChildProductChange(id, val){
	debugger;
	Areaforall($(val).val(), id);
	var TempChildProductValue = $(val).val();
	var MainrowLength = $(".tr-class").length;
	var tempgranttotal = 0;
	var tempamount = 0;
	for(var i=0;i<MainrowLength;i++){
		debugger;
		if(TempChildProductValue == $("#childProduct"+(i+1)).val()){
			var mainQantity=$("#mainQantity"+(i+1)).val()==''?0:$("#mainQantity"+(i+1)).val();
			$("#sitequanty"+id).val(mainQantity); 
			$("#totalamountsite"+id).val($("#mainTotalAmountId"+(i+1)).val());
			if(mainQantity==0){
				tempamount=0;
			}
			else{
				tempamount = $("#mainTotalAmountId"+(i+1)).val()/$("#mainQantity"+(i+1)).val();
			}
			$("#splitamount"+id).val(tempamount);
			var grandTotal1=0;
			$(".totalamountclass").each(function(){
			if($(this).val() == ""){
				grandTotal1 = 0;
			}else{
				grandTotal1  +=  parseFloat($(this).val());
				$("#grandTotalsite").html(grandTotal1); 
			}
		
			});
		}
	}
	
}


function expendituremultiChildProductChange(id, val){
	Areaforall($(val).val(), id);
	var TempChildProductValue = $(val).val();
	var MainrowLength = $(".tr-class").length;
	for(var i=0;i<MainrowLength;i++){
		debugger;
		if(TempChildProductValue == $("#childProduct"+(i+1)).val()){
			var mainQantity=$("#mainQantity"+(i+1)).val()==''?0:$("#mainQantity"+(i+1)).val();
			$("#multisitequanty"+id).val(mainQantity); 
			$("#totalamountmultsite"+id).val($("#mainTotalAmountId"+(i+1)).val());
			var tempAmount =0;
			if(mainQantity==0){
				tempamount=0;
			}
			else{
				tempAmount =  $("#mainTotalAmountId"+(i+1)).val()/$("#mainQantity"+(i+1)).val();
	    	}
			$("#multisplitamount"+id).val(tempAmount);
			var grandTotal1=0;
			$(".multiSitetotalamountclass").each(function(){
			if($(this).val() == ""){
				grandTotal1 = 0;
			}else{
				grandTotal1  +=  parseFloat($(this).val());
				$("#multiSiteGrandTotal").html(grandTotal1); 
			}
			});
	}
		
  }
  getMultiSiteDatesAndQuantity(id);
}

function expenditureBrandChildProductChange(id,val){
	var TempChildProductValue = $(val).val();
	var MainrowLength = $(".tr-class").length;
	for(var i=0;i<MainrowLength;i++){
		debugger;
		if(TempChildProductValue == $("#childProduct"+(i+1)).val()){
			$("#locationquantity"+id).val($("#mainQantity"+(i+1)).val()); 
			$("#locationtotalAmount"+id).val($("#mainTotalAmountId"+(i+1)).val());
		}
	}
}

function sitequanttykeyup(id, val){
	var TempChildProductValue = $(val).val();
	var MainrowLength = $(".tr-class").length;
	var tempamount = parseFloat($("#totalamountsite"+(id)).val() / $("#sitequanty"+(id)).val());
	if($("#sitequanty"+id).val()=="" || $("#sitequanty"+id).val()=="0"){  tempamount=0; }
	$("#splitamount"+id).val(tempamount.toFixed(2));
	SiteWiseGrandTotal();
}

//site wise total amount change
function SiteWiseTotalAmountChange(id){debugger;
	var totalAmount=parseFloat($("#totalamountsite"+id).val());
	var siteWiseChildName=$("#singleSiteChild"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	for(var m=0;m<doInventoryTableIdMain;m++){
		if(siteWiseChildName==$("#childProduct"+(m+1)).val().split("$")[1]){ 
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(m+1)).val())
			if(totalAmount>productTotalAmount && (totalAmount-1)>productTotalAmount){
				alert("you can't give more than invoice amount.");
				$("#totalamountsite"+id).val("0");
				$("#splitamount"+id).val("0");
				SiteWiseGrandTotal();
				return false;
			}else{
				var tempamount = parseFloat($("#totalamountsite"+(id)).val()/$("#sitequanty"+(id)).val());
				if($("#sitequanty"+id).val()=="" || $("#sitequanty"+id).val()=="0"){
					tempamount=0;
				}
				$("#splitamount"+id).val(tempamount.toFixed(2));
				SiteWiseGrandTotal();
			}
	   }
   }
}

function SiteWiseGrandTotal(){
	var grandTotal1=0;
	$(".totalamountclass").each(function(){
	 if($(this).val() == ""){
		grandTotal1 += 0;
	 }else{
		grandTotal1  +=  parseFloat($(this).val());
	 }
   });
  $("#grandTotalsite").html(grandTotal1); 
}
//model LocationWise service end

//multi site grand total
function MultiSuteGrandTotal(){
	var grandTotal1=0;
	$(".multiSitetotalamountclass").each(function(){ if($(this).val() == ""){  grandTotal1 += 0;	}else{ grandTotal1  +=  parseFloat($(this).val());	} 	});
	$("#multiSiteGrandTotal").html(grandTotal1.toFixed(2)); 	
}
function getMonth(date) {
	  var month = date.getMonth() + 1;
	  return month < 10 ? '0' + month : '' + month; // ('' + month) for string result
}


//loading location wise table
function CallingperSiteCalculation(stateId, expendatureType){debugger;
	  var Invoicedate=new Date($("#InvoiceDateId").val());
	  var month=getMonth(Invoicedate)
	  var month_year=month+"-"+Invoicedate.getFullYear();
	  $.ajax({
		 	url:"getAvailableAreaForSale.spring",
			type:"POST",
			Contenttype:" application/json",
			data : {
				 "location": ""+stateId,
				"expendatureType": ""+expendatureType,
				"month_year" : month_year
			},
			success:function(response){debugger;
			var childprdctquantity=[];
				$(".qtyinput").each(function(){
					var tempval = $(this).val();
					if(tempval!=0){
						childprdctquantity.push(tempval);
					}		
				});
				var global_response;
				var addlocationwise='';
				var newId = 0;
				var tempchildData = childproduct;
				var tempchildquantity = childprdctquantity;
				var tempchildproductTotalAmount = childproductTotalAmount;
				var tempresp = eval('('+response+')');
				$("#tempshowhide").show();
				if(!tempresp.xml){
					$("#locationAdd").html("<tr><td style='text-align:center;border:1px solid #000;' colspan='9'>No Data</td></tr>");
					$("#locationGrandTotal").hide();
					return false;
				}
				global_response = tempresp.xml.site;
				var childproductlength = $(".childprdctcls").length;
				var AmountForProductForSite = 0;
				var grandTotal = 0;
				var num=0;
				var siteId="'locwise'";
				for(var i=0;i<childproductlength;i++){
				  if($("#mainQantity"+(i+1)).val()!=0){		
					if(global_response.siteId){
						newId=num+1;
						var AmountForProductForSite = tempchildproductTotalAmount[i] * (tempresp.xml.site.availableArea/tempresp.xml.site.totalArea);
						var basicAmount  =0;
						if(tempchildquantity[i]==''){
							basicAmount  =0;
						}else{
							basicAmount  =  AmountForProductForSite/tempchildquantity[i];
						}
						grandTotal += AmountForProductForSite;
						addlocationwise += '<tr id="locationAdd'+newId+'" class="locationtableclass"><td ><input id="childprdcts'+newId+'" type="text" class="form-control childprdctsLocation" name="childprdcts'+newId+'"  value="'+tempchildData[i].split('$')[1]+'" data-toggle="tooltip" title="'+tempchildData[i].split('$')[1]+'" readonly><input id="LocationChildId'+newId+'" class="LocationChildcls" type="hidden"  name="LocationChildId'+newId+'"  value="'+tempchildData[i]+'"></td><td><select class="form-control" id="LocationLocationId'+newId+'" name="LocationLocationId'+newId+'"></select></td><td><input type="text" class="form-control ExpiryDate readonly-color" readonly="true" name="locwisefromDate'+newId+'"  onchange="setTodate('+newId+', '+siteId+')"  id="locwisefromDate'+newId+'" placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" name="locwisetoDate'+newId+'" id="locwisetoDate'+newId+'" readonly="true" placeholder="Select To Date"/></td><td><input type="text" class="form-control timepicker" id="LocationTime'+newId+'" name="LocationTime'+newId+'" placeholder="Select Time"></td><td><input onkeypress="return isNumber(this, event)"  onkeyup="Locationquanttykeyup('+newId+', this)" name="locationquantity'+newId+'" id="locationquantity'+newId+'" type="text" class="form-control" value="'+tempchildquantity[i]+'" readonly></td><td><input type="text" class="form-control" onkeypress="return isNumber(this, event)"  onkeyup="locationWiseAmountChange('+newId+')" name="locationtotalAmount'+newId+'" id="locationtotalAmount'+newId+'" value="'+basicAmount.toFixed(2)+'"  readonly></td><td><input value="'+AmountForProductForSite.toFixed(2)+'" id="locationsplitamount'+newId+'"  name="locationsplitamount'+newId+'" type="text" class="form-control locationTotalAmount" onkeypress="return isNumber(this, event)" onkeyup="locationWiseTotalAmountChange('+newId+')"  readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="locationquantity'+newId+'" id="sitenamelocationwise'+newId+'" value="'+tempresp.xml.site.siteName+'"  data-toggle="tooltip" title="'+tempresp.xml.site.siteName+'" readonly><input type="hidden" name="locationSiteId'+newId+'" id="locationSiteId'+newId+'" value="'+tempresp.xml.site.siteId+'$'+tempresp.xml.site.siteName+'"></td></tr>';
						var durationTableLength=$(".durationTablelocation").length;
						if(durationTableLength==0){   getLocationForLocandBranding(tempchildData[i], num);	}
						num++;
					}
					else{
						for(var j=0; j<global_response.length;j++){
							newId=num+1;
							var AmountForProductForSite = tempchildproductTotalAmount[i] * (tempresp.xml.site[j].availableArea/tempresp.xml.site[j].totalArea);
							var basicAmount  =0;
							if(tempchildquantity[i]==''){
								basicAmount  =0;
							}else{
								basicAmount  =  AmountForProductForSite/tempchildquantity[i];
							}
							grandTotal += AmountForProductForSite;
							addlocationwise += '<tr id="locationAdd'+newId+'" class="locationtableclass"><td ><input id="childprdcts'+newId+'" type="text" class="form-control" childprdctsLocation" name="childprdcts'+newId+'"  value="'+tempchildData[i].split('$')[1]+'" data-toggle="tooltip" title="'+tempchildData[i].split('$')[1]+'" readonly><input id="LocationChildId'+newId+'" type="hidden" class="LocationChildcls"  name="LocationChildId'+newId+'"  value="'+tempchildData[i]+'"></td><td><select class="form-control" id="LocationLocationId'+newId+'" name="LocationLocationId'+newId+'"></select></td><td><input type="text" class="form-control ExpiryDate readonly-color" readonly="true" name="locwisefromDate'+newId+'"  onchange="setTodate('+newId+', '+siteId+')"  id="locwisefromDate'+newId+'" placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" name="locwisetoDate'+newId+'" id="locwisetoDate'+newId+'" readonly="true" placeholder="Select To Date"/></td><td><input type="text" class="form-control timepicker" id="LocationTime'+newId+'" name="LocationTime'+newId+'" placeholder="Select Time"></td><td><input onkeypress="return isNumber(this, event)" onkeyup="Locationquanttykeyup('+newId+', this)" name="locationquantity'+newId+'" id="locationquantity'+newId+'" type="text" class="form-control" value="'+tempchildquantity[i]+'"  readonly></td><td><input type="text" class="form-control" onkeypress="return isNumber(this, event)" onkeyup="locationWiseAmountChange('+newId+')" name="locationtotalAmount'+newId+'" id="locationtotalAmount'+newId+'" value="'+basicAmount.toFixed(2)+'"  readonly></td><td><input value="'+AmountForProductForSite.toFixed(2)+'" id="locationsplitamount'+newId+'"  name="locationsplitamount'+newId+'"  type="text" class="form-control locationTotalAmount" onkeypress="return isNumber(this, event)"  onkeyup="locationWiseTotalAmountChange('+newId+')"  readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="locationquantity'+newId+'" id="sitenamelocationwise'+newId+'" value="'+tempresp.xml.site[j].siteName+'" data-toggle="tooltip" title="'+tempresp.xml.site[j].siteName+'" readonly><input type="hidden" name="locationSiteId'+newId+'" id="locationSiteId'+newId+'" value="'+tempresp.xml.site[j].siteId+'$'+tempresp.xml.site[j].siteName+'"></td></tr>';
							num++;
						}
						var durationTableLength=$(".durationTablelocation").length;
						if(durationTableLength==0){  getLocationForLocandBranding(tempchildData[i], num); }
					}	
				  }
				}
				$("#locationAdd").html(addlocationwise);
				$("#grandTotal").html(grandTotal.toFixed(2));
				$('[data-toggle="tooltip"]').tooltip(); 
				/*for(var i=0;i<childproductlength;i++){
				  if($("#mainQantity"+(i+1)).val()!=0){			
					if(global_response.siteId){
						$("#LocationTime1").timepicker({
							 timeFormat: 'h:mm p',
							    interval: 10,
							    minTime: '00.00AM',
							    maxTime: '11.59PM',
							    startTime: '00:00',
							    dynamic: false,
							    dropdown: true,
							    scrollbar: true
						});
						$("#locwisefromDate1").datepicker({
							    dateFormat: 'dd-M-y',
							    changeMonth: true,
						        changeYear: true
						        maxDate:0
						        
						});
						$("#locwisetoDate1").datepicker({
							 dateFormat: 'dd-M-y',
							    changeMonth: true,
						        changeYear: true
						        maxDate:0
						});
					}
					else{						
						for(var j=0; j<global_response.length;j++){
							newId=j+1;
							$("#LocationTime"+(j+1)).timepicker({
								 timeFormat: 'h:mm p',
								    interval: 10,
								    minTime: '00.00AM',
								    maxTime: '11.59PM',
								    startTime: '00:00',
								    dynamic: false,
								    dropdown: true,
								    scrollbar: true
							});
							$("#locwisefromDate"+(j+1)).datepicker({
							        dateFormat: 'dd-M-y',
								    changeMonth: true,
							        changeYear: true
							        maxDate:0
							});
							$("#locwisetoDate"+(j+1)).datepicker({
								 dateFormat: 'dd-M-y',
								    changeMonth: true,
							        changeYear: true
							        maxDate:0
							});
						}
					}	
				 }
				}*/
				
				var locationTableRowLength=$(".locationtableclass").length;
				for(var i=0;i<locationTableRowLength;i++){
					$("#LocationTime"+(i+1)).timepicker({
						 timeFormat: 'h:mm p',
						    interval: 10,
						    minTime: '00.00AM',
						    maxTime: '11.59PM',
						    startTime: '00:00',
						    dynamic: false,
						    dropdown: true,
						    scrollbar: true
					});
					$("#locwisefromDate"+(i+1)).datepicker({
					        dateFormat: 'dd-M-y',
						    changeMonth: true,
					        changeYear: true
					       /* maxDate:0*/
					});
					$("#locwisetoDate"+(i+1)).datepicker({
						 dateFormat: 'dd-M-y',
						    changeMonth: true,
					        changeYear: true
					       /* maxDate:0*/
					});
				}
				
				var durationTableLength=$(".durationTablelocation").length;
				var locationtableLength=$(".locationtableclass").length;
				if(durationTableLength>0){
					for(var i=0; i<locationtableLength;i++){
						var locationChildName=$("#childprdcts"+(i+1)).val();
						var locationSiteName=$("#locationSiteId"+(i+1)).val();
						for(var k=0;k<durationTableLength;k++){
							if(locationChildName==$("#dutrationChildId"+(k+1)).val().split("$")[1]){  // && BrandingSiteName== $("#site_Name"+(k+1)).val()
								//$("#locationquantity"+(i+1)).val($("#locationQuantity"+(k+1)).val());
								//$("#sitewisefromDate"+(k+1)).val($("#from_date_location"+(j+1)).val());
								$("#locwisefromDate"+(i+1)).val($("#from_date_location"+(k+1)).val());
								$("#locwisetoDate"+(i+1)).val($("#to_date_location"+(k+1)).val());
								$("#LocationTime"+(i+1)).val($("#timepicker"+(k+1)).val());
								$("#LocationLocationId"+(i+1)).html("<option val='"+$("#location_Id"+(k+1)).val()+"'>"+$("#location_Id"+(k+1)).val().split("$")[1]+"</option>");
							}
							/*else{debugger;
								var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
								for(var m=0;m<doInventoryTableIdMain;m++){
									if(locationChildName==$("#childProduct"+(m+1)).val().split("$")[1]){
										$("#locationquantity"+(i+1)).val($("#mainQantity"+(m+1)).val());
								}
							}
						}	*/	
					}
				 }
				}/*else{*/
				var invoiceTableRowsLength=$(".tr-class").length;
					for(var i=0; i<locationtableLength;i++){debugger;
						var locationChildName=$("#childprdcts"+(i+1)).val();
						var locationSiteName=$("#locationSiteId"+(i+1)).val();
						for(var k=0;k<invoiceTableRowsLength;k++){
						if(locationChildName==$("#childProduct"+(k+1)).val().split("$")[1]){
						 $("#locationquantity"+(i+1)).val($("#mainQantity"+(k+1)).val());
					    }
				    }
				/*  }*/
				}
				
				var locationSiteLength=$("#tempshowhide").find("tbody").find("tr").length;
				for(var i=0;i<locationSiteLength-1;i++){
					var splitAmount=parseFloat($("#locationsplitamount"+(i+1)).val())/parseFloat($("#locationquantity"+(i+1)).val());
					if($("#locationquantity"+(i+1)).val()=="" || $("#locationquantity"+(i+1)).val()=="0"){
						splitAmount=0;
					}
					$("#locationtotalAmount"+(i+1)).val(splitAmount.toFixed(2));
				}       
				
			}
	}); 
}


function callingPerBrandwiseCalculation(id, expendaturetype){
	 var Invoicedate=new Date($("#InvoiceDateId").val());
	 var month=getMonth(Invoicedate)
	 var month_year=month+"-"+Invoicedate.getFullYear();
	 $.ajax({
		 	url:"getAvailableAreaForSale.spring",
			type:"POST",
			Contenttype:" application/json",
			data : {
				 "location": ""+id,
				"expendatureType": ""+expendaturetype,
				"month_year" : month_year
			},
			success:function(response){
				var childprdctquantity=[];
				$(".qtyinput").each(function(){
					var tempval = $(this).val();
					if(tempval!=0){
						childprdctquantity.push(tempval);
					}		
				});
				var global_response;
				var addbrandwise ='';
				var num = 0;
				var tempchildData = childproduct;
				var tempchildquantity = childprdctquantity;
				var tempchildproductTotalAmount = childproductTotalAmount;
				console.log("tempchildquantity :"+tempchildquantity);
				console.log("tempchildData :"+tempchildData);
				console.log("tempchildproductTotalAmount :"+tempchildproductTotalAmount);
				var tempresp = eval('('+response+')');
				$("#tempshowhide").show();
				if(!tempresp.xml){
					$("#brandwiseAdd").html("<tr><td style='text-align:center;border:1px solid #000;' colspan='9'>No Data</td></tr>");
					$("#brandwiseGrandTotal").hide();
					return false;
				}
				global_response = tempresp.xml.site;
				var childproductlength = $(".childprdctcls").length;
				var AmountForProductForSite = 0;
				var grandTotal =0;
				for(var i=0;i<childproductlength;i++){
				  if($("#mainQantity"+(i+1)).val()!=0){		
					for(var j=0; j<global_response.length;j++){
						newId=num+1;
						var AmountForProductForSite = tempchildproductTotalAmount[i] * (tempresp.xml.site[j].availableArea/tempresp.xml.site[j].totalArea);
						var basicAmount  =0;
						if(tempchildquantity[i]==""){
							basicAmount=0;
						}
						else{
							basicAmount  =  AmountForProductForSite/tempchildquantity[i];
						}
						grandTotal += AmountForProductForSite;
						addbrandwise += '<tr id="brandingtable'+newId+'" class="brandingtablecls"><td ><input type="text" class="form-control childprdctsLocation" name="childdata'+newId+'" id="Brandingchilddata'+newId+'" value="'+tempchildData[i].split('$')[1]+'" data-toggle="tooltip" title="'+tempchildData[i].split('$')[1]+'" readonly><input type="hidden" name="BrandingChildId'+newId+'" class="BrandingChildCls" id="BrandingChildId'+newId+'" value="'+tempchildData[i]+'"></td><td><select class="form-control widthonefifty" id="BrandinglocationId'+newId+'" name="BrandinglocationId'+newId+'"></select></td><td><input type="text" class="form-control ExpiryDate readonly-color" id="brandwisefromDate'+newId+'" name="brandwisefromDate'+newId+'" readonly="true" placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" id="brandwisetoDate'+newId+'" readonly="true" name="brandwisetoDate'+newId+'" placeholder="Select To Date"/></td><td><input type="text" class="form-control timepicker" id="BrandingTime'+newId+'" name="BrandingTime'+newId+'" placeholder="Select Time"></td><td><input onkeyup="Brandingquanttykeyup('+newId+', this)" onkeypress="return isNumber(this, event)" id="Brandingquantity'+newId+'" name="brandingquantity'+newId+'" type="text" class="form-control" value="'+tempchildquantity[i]+'" readonly></td><td><input type="text" class="form-control" onkeypress="return isNumber(this, event)" name="brandingtotalAmount'+newId+'" id="brandingtotalAmount'+newId+'" value="'+basicAmount.toFixed(2)+'" onkeyup="brandingWiseAmountChange('+newId+')" readonly></td><td><input value="'+AmountForProductForSite.toFixed(2)+'" id="brandingsplitamount'+newId+'"  name="brandingsplitamount'+newId+'"  type="text" class="form-control brandingTotalAmount" onkeypress="return isNumber(this, event)" onkeyup="BrandingWiseTotalAmountChange('+newId+')" readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="brandnamelocationwise'+newId+'" id="brandnamelocationwise'+newId+'" value="'+tempresp.xml.site[j].siteName+'" data-toggle="tooltip" title="'+tempresp.xml.site[j].siteName+'" readonly><input type="hidden" name="BrandingSiteId'+newId+'" id="BrandingSiteId'+newId+'" value="'+tempresp.xml.site[j].siteId+'$'+tempresp.xml.site[j].siteName+'"></td></tr>';
						num++;
					}
				  }
					var durationTableLength=$(".durationTablelocation").length;
					if(durationTableLength==0){
					    getLocationForLocandBranding(tempchildData[i], num);
					}
				}
				$("#brandwiseAdd").html(addbrandwise);
				$("#grandTotalbrand").html(grandTotal.toFixed(2));
				$('[data-toggle="tooltip"]').tooltip(); 
				for(var j=0; j<num;j++){
					if($("#mainQantity"+(j+1)).val()!=0){		
					   $("#BrandingTime"+(j+1)).timepicker({
						 timeFormat: 'h:mm p',
						 interval: 10,
						 minTime: '00.00AM',
						 maxTime: '11.59PM',
					     startTime: '00:00',
						 dynamic: false,
						 dropdown: true,
						 scrollbar: true
					 });
					 $("#brandwisefromDate"+(j+1)).datepicker({
						 dateFormat: 'dd-M-y',
						 changeMonth: true,
					     changeYear: true
					    /* maxDate:0*/
					});
					$("#brandwisetoDate"+(j+1)).datepicker({
						 dateFormat: 'dd-M-y',
						 changeMonth: true,
					     changeYear: true
					});
				  }
				}
				debugger;
				var durationTableLength=$(".durationTablelocation").length;
				var brandingtableLength=$(".brandingtablecls").length;
				if(durationTableLength>0){
					for(var i=0; i<brandingtableLength;i++){
						var BrandingChildName=$("#Brandingchilddata"+(i+1)).val();
						var BrandingSiteName=$("#BrandingSiteId"+(i+1)).val();
						for(var k=0;k<durationTableLength;k++){
							if(BrandingChildName==$("#dutrationChildId"+(k+1)).val().split("$")[1]){ //&& BrandingSiteName== $("#site_Name"+(k+1)).val()
								//$("#Brandingquantity"+(i+1)).val($("#locationQuantity"+(k+1)).val());
								$("#brandwisefromDate"+(i+1)).val($("#from_date_location"+(k+1)).val());
								$("#brandwisetoDate"+(i+1)).val($("#to_date_location"+(k+1)).val());
								$("#BrandingTime"+(i+1)).val($("#timepicker"+(k+1)).val());
								$("#BrandinglocationId"+(i+1)).html("<option val='"+$("#location_Id"+(k+1)).val()+"'>"+$("#location_Id"+(k+1)).val().split("$")[1]+"</option>");
							}
						/*	else{
								var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
								for(var m=0;m<doInventoryTableIdMain;m++){
									if(BrandingChildName==$("#childProduct"+(m+1)).val().split("$")[1]){
										$("#Brandingquantity"+(i+1)).val($("#mainQantity"+(m+1)).val());
								}
							}
						}	*/	
					}
				 }
				}/*else{*/
				    var invoiceTableRowsLength=$(".tr-class").length;
					for(var i=0; i<brandingtableLength;i++){
						var BrandingChildName=$("#Brandingchilddata"+(i+1)).val();
						var BrandingSiteName=$("#BrandingSiteId"+(i+1)).val();
						for(var k=0;k<invoiceTableRowsLength;k++){
						if(BrandingChildName==$("#childProduct"+(k+1)).val().split("$")[1]){
						 $("#Brandingquantity"+(i+1)).val($("#mainQantity"+(k+1)).val());
					    }
				    }
				  }
				/*}*/
				
				var singleSiteLength=$("#BrandingWiseTable").find("tbody").find("tr").length;
				for(var i=0;i<singleSiteLength-1;i++){
					var splitAmount=parseFloat($("#brandingsplitamount"+(i+1)).val())/parseFloat($("#Brandingquantity"+(i+1)).val());
					if($("#Brandingquantity"+(i+1)).val()=="" || $("#Brandingquantity"+(i+1)).val()=="0"){
						splitAmount=0;
					}
					$("#brandingtotalAmount"+(i+1)).val(splitAmount.toFixed(2));
				}
			}
	 });
}
//get the location for location wise  && Branding wise
function getLocationForLocandBranding(childname, length){
	debugger;
	$.ajax({
	 	url:"loadAndSetAreaData.spring?childProductId="+childname.split('$')[0],
		type:"GET",
		success:function(response){
			debugger;
				var result = eval('(' + response + ')');
				var addLocation = "<option>--Select Location--</option>";
				 if(result.xml){
					 for(var i=0; i<result.xml.Area.length;i++){						 
						 addLocation+='<option value="'+result.xml.Area[i].AreaId+"$"+result.xml.Area[i].AreaName+'">'+result.xml.Area[i].AreaName+'</option>';
					}					
					 var expendatureName=$("#poToId").val();
					 if(expendatureName=="LocationWise"){
						 $(".LocationChildcls").each(function(){debugger;
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("LocationChildId")[1];
								$("#LocationLocationId"+id).html(addLocation);
							}
						})
					 }
					 else{
						 $(".BrandingChildCls").each(function(){debugger;
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("BrandingChildId")[1];					
								$("#BrandinglocationId"+id).html(addLocation);
							}
						})
					 }
				 } //1 st is empty
				 else{
					 console.log("no locations;");
				 }
		}
		}); 
}		
//get the location for site && multi location
function Areaforall(val, id){debugger;
	$.ajax({
	 	url:"loadAndSetAreaData.spring?childProductId="+val.split('$')[0],
		type:"GET",
		success:function(response){
			debugger;
				var result = eval('(' + response + ')');
				 var addsitearea = "<option value='0'>--Select Location--</option>";
				 if(result.xml){
					 for(var i=0; i<result.xml.Area.length;i++){						 
						 addsitearea+='<option value="'+result.xml.Area[i].AreaId+"$"+result.xml.Area[i].AreaName+'">'+result.xml.Area[i].AreaName+'</option>';
					}
				 } //1 st is empty
				 else{
					 console.log("no locations;");
				 }
				$("#siteareaid"+id).html(addsitearea);  
				$("#multisiteareaid"+id).html(addsitearea);
		}
	}); 
}


function siteWiseSitenameChange(){debugger;
    loadSiteTable();
	var siteName=$("#singleSiteWiseSite").val().split("$")[1];
	for(var j=0;j<childproduct.length;j++){	
		$("#sitename"+(j+1)).val(siteName);
		$("#sitename"+(j+1)).attr("title", siteName);
	}
	$("#siteWiseSite").show();
}
//get site names 
function getsiteNames(){
	$.ajax({
	 	url:"siteNameDetails.spring",
		type:"GET",
		success:function(response){
			    siteNamechild =response ;
				var result = eval('(' + response + ')');
				var addsitedata = "<option value='0'>--Please Select Site--</option>";
				for(var i=0; i<result.xml.site.length;i++){
					addsitedata+='<option value="'+result.xml.site[i].SITEID+'$'+result.xml.site[i].SITENAME+'">'+result.xml.site[i].SITENAME+'</option>';
				}
				$("#singleSiteWiseSite").html(addsitedata); 
		}
	}); 
}
getsiteNames();

function deleteRow(currentRow) {
	var CanIDelete=window.confirm("Do you want to delete row?");
    if(CanIDelete==false){
    	return false;
    }
    var rowscount=$('.expenditure').length;
	if(rowscount==1){
		alert("this row con't be deleted.");
		return false;
	}
	//removing row
   $("#multisiteAdd"+currentRow).remove();
   MultiSuteGrandTotal();
}
function loadMultiSiteTable(){
	var templength = $(".tr-class").length;
	var addrow='';
	var addmultirow='';
	var newId = 0;
	var siteId="'multisite'";
	var qtytype="'qty'";
	var amttype="'amount'";
	getSiteNamesForMulti(templength);
	for(var i=0; i<templength;i++){
		
		 if($("#mainQantity"+(i+1)).val()!=0){	
			 newId++;
			 addmultirow += '<tr id="multisiteAdd'+newId+'" class="expenditure"><td>'+newId+'</td><td><select name="exapendChildproduct'+newId+'" id="exapendChildproduct'+newId+'" class="form-control childprdctclsmulti"  onchange="expendituremultiChildProductChange('+newId+', this)"></select></td><td><select class="form-control multiareasclass" id="multisiteareaid'+newId+'" name="multisiteareaid'+newId+'" onchange="getMultiSiteDatesAndQuantity('+newId+')"></select></td><td><select class="form-control multsitenamecls" name="sitenameid'+newId+'" id="sitenameid'+newId+'" onchange="getMultiSiteDatesAndQuantity('+newId+')"></select></td><td><input onkeypress="return isNumber(this, event)" onkeyup="multisiteWiseQtyAndAmountChange('+newId+', '+qtytype+')" name="multisitequanty'+newId+'" id="multisitequanty'+newId+'" type="text"class="form-control pasteDisable"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" id="multisitefromDate'+newId+'" name="multisitefromDate'+newId+'" onchange="setTodate('+newId+', '+siteId+')" readonly="true"  placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" id="multisitetoDate'+newId+'" readonly="true" name="multisitetoDate'+newId+'"   placeholder="Select To Date"/></td><td><input type="text" class="form-control timepicker" id="MultiSiteWiseTime'+newId+'" name="MultiSiteWiseTime'+newId+'" placeholder="Select Time"></td><td><input id="multisplitamount'+newId+'" onkeypress="return isNumber(this, event)" onkeyup="multisiteWiseQtyAndAmountChange('+newId+', '+amttype+')" name="multisplitamount'+newId+'" type="text" class="form-control pasteDisable"/></td><td><input id="totalamountmultsite'+newId+'" name="totalamountmultsite'+newId+'" type="text" class="form-control multiSitetotalamountclass pasteDisable" onkeypress="return isNumber(this, event)" onkeyup="multiSiteTotalChange('+newId+', this)"/></td><td><button type="button" class="btnaction btnpadd" onclick="appendModalRow('+newId+')"><i class="fa fa-plus"></i></button><button type="button" class="btnaction" onclick="deleteRow('+newId+')"><i class="fa fa-trash"></i></button></td></tr>';
		 }
	}
	$("#multisiteAdd").html(addmultirow);
	$('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
	for(var i=0; i<templength;i++){
		if($("#mainQantity"+(i+1)).val()!=0){		
			$('#MultiSiteWiseTime'+(i+1)).timepicker({
				timeFormat: 'h:mm p',
			    interval: 10,
			    minTime: '00.00AM',
			    maxTime: '11.59PM',
			    startTime: '00:00',
			    dynamic: false,
			    dropdown: true,
			    scrollbar: true
			});
		}
	}
	$(function() {
		  $(".ExpiryDate ").datepicker({ 
			  dateFormat: 'dd-M-y',
			 /*  maxDate:0,*/
		      changeMonth: true,
	          changeYear: true
		  });
	});
	var selectoptionmulti = "<option value=''>--select--</option>";
	for(var j=0;j<childproduct.length;j++){
		if($("#mainQantity"+(j+1)).val()!=0){		
		   selectoptionmulti+='<option value="'+childproduct[j]+'">'+childproduct[j].split("$")[1]+'</option>';
		}
	}
	$(".childprdctclsmulti").each(function(){
		$(this).html(selectoptionmulti);
	});
	
}
// to load quantity, date, time and quantity based on childId, siteLocation and site name
function getMultiSiteDatesAndQuantity(id){debugger;
	var durationTableLength=$(".durationTablelocation").length;
	var multiSiteChildId=$("#exapendChildproduct"+id).val();
	var multiSiteLocation=$("#multisiteareaid"+id).val();
	var multiSiteSite=$("#sitenameid"+id).val();
	if(multiSiteChildId=="" || multiSiteLocation=="" || multiSiteSite==""){ return false; 	}
	if(durationTableLength>0){
			for(var j=0;j<durationTableLength;j++){
				if(multiSiteChildId==$("#dutrationChildId"+(j+1)).val() && multiSiteLocation==$("#location_Id"+(j+1)).val() && multiSiteSite==$("#site_Name"+(j+1)).val()){
					$("#multisitefromDate"+id).val($("#from_date_location"+(j+1)).val());
					$("#multisitetoDate"+id).val($("#to_date_location"+(j+1)).val());
					$("#MultiSiteWiseTime"+id).val($("#timepicker"+(j+1)).val());
					/*$("#multisitequanty"+id).val($("#locationQuantity"+(j+1)).val());
					var splitAmount=parseFloat($("#totalamountmultsite"+id).val())/parseFloat($("#multisitequanty"+id).val());
					if($("#multisitequanty"+id).val()=="" || $("#multisitequanty"+id).val()=="0"){ splitAmount=0; 	}
					$("#multisplitamount"+id).val(splitAmount.toFixed(2));	*/				
				}
			/*	else{
					var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
					for(var k=0;k<doInventoryTableIdMain;k++){
						if(multiSiteChildId==$("#childProduct"+(k+1)).val()){
							$("#multisitequanty"+id).val($("#mainQantity"+(k+1)).val());
							var splitAmount=parseFloat($("#totalamountmultsite"+id).val())/parseFloat($("#multisitequanty"+id).val());
							if($("#multisitequanty"+id).val()=="" || $("#multisitequanty"+id).val()=="0"){ splitAmount=0;	}
							$("#multisplitamount"+id).val(splitAmount.toFixed(2));
					}
				}
			}*/
		}
		
	}/*else{*/
		var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
		for(var k=0;k<doInventoryTableIdMain;k++){
			if(multiSiteChildId==$("#childProduct"+(k+1)).val()){
				$("#multisitequanty"+id).val($("#mainQantity"+(k+1)).val());
				var splitAmount=parseFloat($("#totalamountmultsite"+id).val())/parseFloat($("#multisitequanty"+id).val());
				if($("#multisitequanty"+id).val()=="" || $("#multisitequanty"+id).val()=="0"){ splitAmount=0;	}
				$("#multisplitamount"+id).val(splitAmount.toFixed(2));
		}
	}
/*}*/
}
//loading single site table when you change site name 
function loadSiteTable(){
	var templength = $(".tr-class").length;
	var addrow='';
	var newId = 0;
	getSiteNamesForMulti(templength);
	var siteId="'sitewise'";
	for(var i=0; i<templength;i++){
		 
		 if($("#mainQantity"+(i+1)).val()!=0){	
			 newId++;
			 addrow += '<tr id="siteAdd'+newId+'" class="siteclass"><td ><input type="text" id="singleSiteChild'+newId+'" class="form-control" name="singleSiteChild'+newId+'" value="'+childproduct[i].split("$")[1]+'" data-toggle="tooltip" title="'+childproduct[i].split("$")[1]+'" readonly><input type="hidden" name="childdata'+newId+'" id="childdata'+newId+'"  value="'+childproduct[i]+'"></td><td><select class="form-control areasclass" id="siteareaid'+newId+'" name="siteareaid'+newId+'"></select></td><td><input type="text" class="form-control ExpiryDate readonly-color" name ="sitewisefromDate'+newId+'" id="sitewisefromDate'+newId+'" onchange="setTodate('+newId+', '+siteId+')" readonly="true" placeholder="Select From Date"/></td><td><input type="text" class="form-control ExpiryDate readonly-color" id="sitewisetoDate'+newId+'" name="sitewisetoDate'+newId+'" placeholder="Select To Date" readonly="true"/></td><td><input type="text" id="SiteWiseTime'+newId+'" name="SiteWiseTime'+newId+'" class="form-control timepicker" placeholder="Select Time"></td><td><input type="text" id="sitequanty'+newId+'"  name="sitequanty'+newId+'" onkeyup="sitequanttykeyup('+newId+',this)" onkeypress="return isNumber(this, event)" class="form-control pasteDisable"></td><td><input type="text" class="form-control pasteDisable" name="splitamount'+newId+'" id="splitamount'+newId+'" onkeypress="return isNumber(this, event)" onkeyup="siteWiseAmountChange('+newId+')" ></td><td><input type="text" class="form-control totalamountclass pasteDisable" id="totalamountsite'+newId+'"  name="totalamountsite'+newId+'" onkeypress="return isNumber(this, event)"  onkeyup="SiteWiseTotalAmountChange('+newId+')"></td><td ><input type="text" class="form-control siteneme"  name="sitename'+newId+'" id="sitename'+newId+'" data-toggle="tooltip"  readonly></td></tr>';
		 }
	}
	$("#siteAdd").html(addrow);
	$('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
	$('[data-toggle="tooltip"]').tooltip(); 
	var durationTableLength=$(".durationTablelocation").length;
	var SingleSiteName=$("#singleSiteWiseSite").val();
	//checking wether the duration table there or not
	var singleSiteLength=$("#singleSite").find("tbody").find("tr").length;
	if(durationTableLength>0){
		for(var i=0; i<singleSiteLength-1;i++){
			//if($("#mainQantity"+(i+1)).val()!=0){			
			 newId=i+1;
			var singleSiteChild=$("#singleSiteChild"+(i+1)).val();
			for(var j=0;j<durationTableLength;j++){
				if(singleSiteChild==$("#dutrationChildId"+(j+1)).val().split("$")[1]){
					//$("#sitequanty"+newId).val($("#locationQuantity"+(j+1)).val());
					$("#siteareaid"+(i+1)).html("<option value='"+$("#location_Id"+(j+1)).val()+"'>"+$("#location_Id"+(j+1)).val().split("$")[1]+"</option>");
					$("#sitewisefromDate"+(i+1)).val($("#from_date_location"+(j+1)).val());
					$("#sitewisetoDate"+(i+1)).val($("#to_date_location"+(j+1)).val());
					$("#SiteWiseTime"+(i+1)).val($("#timepicker"+(j+1)).val());
				}
				/*else{
					var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
					for(var k=0;k<doInventoryTableIdMain;k++){
						if(singleSiteChild==$("#childProduct"+(k+1)).val().split("$")[1]){
							$("#sitequanty"+newId).val($("#mainQantity"+(k+1)).val());
					    }
				   }
			    }	*/	
		 }
			
	   //}
	}
	}/*else{*/
	debugger;
	
		for(var i=0; i<singleSiteLength-1;i++){
			 var singleSiteChild=$("#singleSiteChild"+(i+1)).val();			
				 Areaforall($("#childdata"+(i+1)).val(), (i+1));
		         var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
		         for(var k=0;k<doInventoryTableIdMain;k++){
		        	if(singleSiteChild==$("#childProduct"+(k+1)).val().split("$")[1]){ 
		        		 if($("#mainQantity"+(k+1)).val()!=0){
		        			 $("#sitequanty"+(i+1)).val($("#mainQantity"+(k+1)).val());
		        		 }
		        	}
	      }
	  }
/*	}*/
	//setting time 
	var singleSiteLength=$("#singleSite").find("tbody").find("tr").length;
	for(var i=0; i<singleSiteLength-1;i++){
	   $('#SiteWiseTime'+(i+1)).timepicker({
		 timeFormat: 'h:mm p',
		    interval: 10,
		    minTime: '00.00AM',
		    maxTime: '11.59PM',
		    startTime: '00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
		});
	}

		var selectOption="<option>--select--</option>";
		var selectoptionLocation = "<option>--select--</option>";
		var selectoptionbrand = "<option>--select--</option>";
		for(var j=0;j<childproduct.length;j++){
			if($("#mainQantity"+(j+1)).val()!=0){
				selectOption+='<option value="'+childproduct[j]+'">'+childproduct[j].split("$")[1]+'</option>';
				selectoptionLocation+='<option value="'+childproduct[j]+'">'+childproduct[j].split("$")[1]+'</option>';
				selectoptionbrand+='<option value="'+childproduct[j]+'">'+childproduct[j].split("$")[1]+'</option>';
			}
		} 
		
		$(".sitecls").each(function(){
			$(this).html(selectOption);
		});
		
		$(".childprdctsLocation").each(function(){
			$(this).html(selectoptionLocation);
		});
		
		$(".childprdctsBrandwise").each(function(){
			$(this).html(selectoptionbrand);
		});
		$("#sitename"+newId).html($("#siteName").val());
		$("#childprdct").html($("#childProduct1").val());
		
		var singleSiteLength=$("#singleSite").find("tbody").find("tr").length;
		console.log(singleSiteLength);
		debugger;
		for(var i=0;i<singleSiteLength-1;i++){
			var singleSiteChild=$("#singleSiteChild"+(i+1)).val();
			var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
			for(var j=0; j<doInventoryTableIdMain;j++){
			 if($("#mainQantity"+(j+1)).val()!=0){
				if(singleSiteChild==$("#childProduct"+(j+1)).val().split("$")[1]){
					$("#totalamountsite"+(i+1)).val($("#mainTotalAmountId"+(j+1)).val());
					var splitAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val())/parseFloat($("#sitequanty"+(i+1)).val());					
					if($("#sitequanty"+(j+1)).val()=="" || $("#sitequanty"+(j+1)).val()=="0"){ splitAmount=0; }
					$("#splitamount"+(i+1)).val(splitAmount.toFixed(2));
				}
			 }
			}
		}
		
  $(function() {
		  $(".ExpiryDate ").datepicker({ 
			  dateFormat: 'dd-M-y',
			  /* maxDate:0,*/
		      changeMonth: true,
	          changeYear: true
		  });
	});
	//calculating GrandTotal
	SiteWiseGrandTotal();
}
//multisite total amount change
function multiSiteTotalChange(id, current){
	var multisitequanty=parseFloat($("#multisitequanty"+id).val());
	if(multisitequanty=="" || multisitequanty==0){
		$("#multisitequanty"+id).val('0');
		alert("Please enter quantity.");
		return false;
	}
	var totalAmount=parseFloat($(current).val()==""?0:$(current).val())/multisitequanty;
	$("#multisplitamount"+id).val(totalAmount.toFixed(2));	
	MultiSuteGrandTotal();
}

//Location site Quantity change
function Locationquanttykeyup(id, current){
	var splitAmount=parseFloat($("#locationsplitamount"+id).val())/parseFloat($("#locationquantity"+id).val());
	if($("#locationquantity"+id).val()=="" || $("#locationquantity"+id).val()=="0"){ splitAmount=0; }
	$("#locationtotalAmount"+id).val(splitAmount.toFixed(2));
}

//location wise Total Amount Change
function locationWiseTotalAmountChange(id){
	var locationChild=$("#childprdcts"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	for(var j=0; j<doInventoryTableIdMain;j++){
		if(locationChild==$("#childProduct"+(j+1)).val().split("$")[1]){
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var grandTotal1=0;
				$(".locationTotalAmount").each(function(){
					var splitId=$(this).attr("id").split("locationsplitamount")[1];
					if($("#childProduct"+(j+1)).val().split("$")[0]==$("#LocationChildId"+splitId).val().split("$")[0]){
						grandTotal1  +=  parseFloat($("#locationsplitamount"+splitId).val()==''?0:$("#locationsplitamount"+splitId).val());	
					}
				});
			if(grandTotal1>productTotalAmount && (grandTotal1-1)>productTotalAmount){
				alert("You can't initiate more than invoice amount.");
				$("#locationtotalAmount"+id).val("0");
				$("#locationsplitamount"+id).val("0");
				locationWiseGrandTotal();
				return false;
			}
			else{
				var splitAmount=parseFloat($("#locationsplitamount"+id).val()==''?0:$("#locationsplitamount"+id).val())/parseFloat($("#locationquantity"+id).val());
				if($("#locationquantity"+id).val()=="" || $("#locationquantity"+id).val()=="0"){ splitAmount=0;	}
				$("#locationtotalAmount"+id).val(splitAmount.toFixed(2));
			}
		}
	}
	locationWiseGrandTotal();
}
//location grand total
function locationWiseGrandTotal(){
	var grandTotal1=0;
	$(".locationTotalAmount").each(function(){ if($(this).val() == ""){grandTotal1 += 0;}else{grandTotal1  +=  parseFloat($(this).val());} 	});
	$("#grandTotal").html(grandTotal1.toFixed(2)); 
}

//location wise Total Amount Change
function BrandingWiseTotalAmountChange(id){
	
	var locationChild=$("#Brandingchilddata"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	for(var j=0; j<doInventoryTableIdMain;j++){
		if(locationChild==$("#childProduct"+(j+1)).val().split("$")[1]){
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var grandTotal1=0;
			$(".brandingTotalAmount").each(function(){
				if($(this).val() == ""){
					grandTotal1 = 0;
				}else{
					grandTotal1  +=  parseFloat($(this).val());			
				}
			});
			var tempVal=parseInt(grandTotal1)-parseInt(productTotalAmount);
			if(tempVal!=-1 && tempVal!=1 && tempVal!=0){
				alert("You can't initiate more than invoice amount.");
				$("#brandingtotalAmount"+id).val("0");
				$("#brandingsplitamount"+id).val("0");
				BrandingWiseGrandTotal();
				return false;
			}
			else{
				var splitAmount=parseFloat($("#brandingsplitamount"+id).val())/parseFloat($("#Brandingquantity"+id).val());
				if($("#Brandingquantity"+id).val()=="" || $("#Brandingquantity"+id).val()=="0"){ splitAmount=0; 	}
				$("#brandingtotalAmount"+id).val(splitAmount.toFixed(2));
			}
		}
	}
	BrandingWiseGrandTotal();
}
//Branding grand total
function BrandingWiseGrandTotal(){
	var grandTotal1=0;
	$(".brandingTotalAmount").each(function(){ if($(this).val() == ""){ grandTotal1 += 0; }else{ grandTotal1  +=  parseFloat($(this).val()); 	} 	});
	$("#grandTotalbrand").html(grandTotal1.toFixed(2)); 
}


//branding  Quantity change
function Brandingquanttykeyup(id, current){
	var splitAmount=parseFloat($("#brandingsplitamount"+id).val())/parseFloat($("#Brandingquantity"+id).val());
	if($("#Brandingquantity"+id).val()=="" || $("#Brandingquantity"+id).val()=="0"){ splitAmount=0; 	}
	$("#brandingtotalAmount"+id).val(splitAmount.toFixed(2));
}
//single site add button functionality
function checkSingleSiteData(){
	var showHide=0;
	var expenditureTable=$("#singleSite").find("tbody").find("tr").length;
	var selectSite=$("#singleSiteWiseSite").val();
	if(selectSite=="0"){
		alert("Please select Site");
		$("#singleSiteWiseSite").focus();
		return false;
	}
	for(var j=0; j<expenditureTable-1;j++){
		var locationChild=$("#singleSiteChild"+(j+1)).val();
		var locationTotalAmount=parseFloat($("#totalamountsite"+(j+1)).val());
		var locationTotalQty=parseFloat($("#sitequanty"+(j+1)).val());
		var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
		var tempTotal=0;
		for(var k=0; k<doInventoryTableIdMain;k++){
			if(locationChild==$("#childProduct"+(k+1)).val().split("$")[1]){
				var actionval=$("#actionVal"+(j+1)).val();
				if(actionval!="d"){
					var ProductTotalAmount=parseFloat($("#mainTotalAmountId"+(k+1)).val());
					var ProductTotalQty=parseFloat($("#mainQantity"+(k+1)).val());
					var tempVal=parseInt(locationTotalAmount)-parseInt(ProductTotalAmount);
					//var tempValQty=parseInt(locationTotalQty)-parseInt(ProductTotalQty);
					if(tempVal!=-1 && tempVal!=1 && tempVal!=0 || locationTotalQty>ProductTotalQty || locationTotalQty=="0" || locationTotalQty=="0.00" || locationTotalQty=="0.0" || locationTotalQty=="0."){ showHide++; }
				}
			}
		}
	}
	if(showHide==0)	{
		$("#modal-marketing").modal("hide");
	}else{
		swal("Error!", "It seems quantity/amount going more, Please check once.", "error");
		$("#modal-marketing").modal("show");
		return false
	}
}

//multi site add button functionality
function checkMultipleSiteData(){debugger;
	
	var childproductStatus=validatingChildProduct();
	if(childproductStatus==false){
		return false;
	}
	var valmultitable=validatingMultiSiteTable();
	if(valmultitable==false){
		return false;
	}
	var siteStatus=validatingSiteName();
	if(siteStatus==false){
		return false;
	}
	
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	var showHide=0;
	for(var j=0; j<doInventoryTableIdMain;j++){
		if($("#mainQantity"+(j+1)).val()!=0){	
			var compareChild=$("#childProduct"+(j+1)).val();
			var actionval=$("#actionVal"+(j+1)).val();
			if(actionval!="d"){
				var expenditureTable=$("#expenditureTable").find("tbody").find("tr").length;
				var ProductTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
				var ProductTotalQty=parseFloat($("#mainQantity"+(j+1)).val());
				var tempTotal=0;
				var tempTotalQty=0;
					$(".expenditure").each(function(){
						var id=$(this).attr("id").split("multisiteAdd")[1];
						if(compareChild==$("#exapendChildproduct"+id).val()){
							tempTotal+=parseFloat($("#totalamountmultsite"+id).val());
							tempTotalQty+=parseFloat($("#multisitequanty"+id).val());
						}
					})
					var tempVal=parseInt(tempTotal)-parseInt(ProductTotalAmount);
					if(tempVal!=-1 && tempVal!=1 && tempVal!=0 || tempTotalQty>ProductTotalQty || tempTotalQty=="0" || tempTotalQty=="0.00" || tempTotalQty=="0."){ showHide++; }
			}
		}
	}
	if(showHide==0){
		$("#modal-marketing").modal("hide");
	}else{
		swal("Error!", "It seems quantity/amount going more, Please check once.", "error");
		$("#modal-marketing").modal("show");
		return false
	}
}
//checking location wise data
function checkingLocationData(){
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	var showHide=0;
	for(var j=0; j<doInventoryTableIdMain;j++){
		var compareChild=$("#childProduct"+(j+1)).val();
		var actionval=$("#actionVal"+(j+1)).val();
		if(actionval!="d"){
			var expenditureTable=$("#tempshowhide").find("tbody").find("tr").length;
			var ProductTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var tempTotal=0;
			for(var k=0; k<expenditureTable-1;k++){
				if(compareChild.split("$")[1]==$("#childprdcts"+(k+1)).val()){
					tempTotal+=parseFloat($("#locationsplitamount"+(k+1)).val());
				}
			}
			var tempVal=parseInt(tempTotal)-parseInt(ProductTotalAmount);
			if(tempVal!=-1 && tempVal!=1 && tempVal!=0){ showHide++; }
		}					
	}
	if(showHide==0)	{
		$("#modal-marketing").modal("hide");
	}else{
		swal("Error!", "It seems quantity/amount going more, Please check once.", "error");
		$("#modal-marketing").modal("show");
		return false
	}
}
//checking Branding wise data
function checkingBrandingData(){
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	var showHide=0;
	for(var j=0; j<doInventoryTableIdMain;j++){
		var compareChild=$("#childProduct"+(j+1)).val();
		var actionval=$("#actionVal"+(j+1)).val();
		if(actionval!="d"){
			var expenditureTable=$("#BrandingWiseTable").find("tbody").find("tr").length;
			var ProductTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var tempTotal=0;
			for(var k=0; k<expenditureTable-1;k++){
				if(compareChild.split("$")[1]==$("#Brandingchilddata"+(k+1)).val()){
					tempTotal+=parseFloat($("#brandingsplitamount"+(k+1)).val());
				}
			}
			var tempVal=parseInt(tempTotal)-parseInt(ProductTotalAmount);
			if(tempVal!=-1 && tempVal!=1 && tempVal!=0){ showHide++;	}	
		}
	}
	if(showHide==0){ 
		$("#modal-marketing").modal("hide");
	}else{
		swal("Error!", "It seems quantity/amount going more, Please check once.", "error");
		$("#modal-marketing").modal("show");
		return false
	}
}
//site wise amount change
function siteWiseAmountChange(id){
	var amount=parseFloat($("#splitamount"+id).val());
	var quantity=parseFloat($("#sitequanty"+id).val());
	var siteWiseChildName=$("#singleSiteChild"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	if($("#splitamount"+id).val()==""){ amount=0; }
	if($("#sitequanty"+id).val()==""){ quantity=0;	}
	var totalAmount=amount*quantity;
	$("#totalamountsite"+id).val(totalAmount.toFixed(2));
	SiteWiseGrandTotal();
	for(var m=0;m<doInventoryTableIdMain;m++){
		if(siteWiseChildName==$("#childProduct"+(m+1)).val().split("$")[1]){ 
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(m+1)).val())
			if(totalAmount>productTotalAmount && (totalAmount-1)>productTotalAmount){
				swal("Error!", "You can't give more than invoice amount.", "error");
				$("#totalamountsite"+id).val("0");
				$("#splitamount"+id).val("0");
				SiteWiseGrandTotal();
				return false;
			}
	   }
   }
}
//multi site wise amount change
function multisiteWiseQtyAndAmountChange(id, type){
	var amount=parseFloat($("#multisplitamount"+id).val()==''?0:$("#multisplitamount"+id).val());
	var quantity=parseFloat($("#multisitequanty"+id).val()==''?0:$("#multisitequanty"+id).val());
	if(type=="amount"){
		$("#totalamountmultsite"+id).val((amount*quantity).toFixed(2));
	}else{
		var multisitequanty=$("#multisitequanty"+(id)).val();
		var tempamount =0;
		if(multisitequanty=='' || multisitequanty==0){
			tempamount =0;
		}
		else{
			tempamount = parseFloat($("#totalamountmultsite"+(id)).val() /multisitequanty );
		}
	   $("#multisplitamount"+id).val(tempamount.toFixed(2));
	}
	MultiSuteGrandTotal();
}
//location site wise amount change
function locationWiseAmountChange(id){
	var amount=parseFloat($("#locationtotalAmount"+id).val()==''?0:$("#locationtotalAmount"+id).val());
	var quantity=parseFloat($("#locationquantity"+id).val()==''?0:$("#locationquantity"+id).val());
	var totalAmount=amount*quantity;
	$("#locationsplitamount"+id).val(totalAmount.toFixed(2));
	locationWiseGrandTotal();
	var locationChild=$("#childprdcts"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	for(var j=0; j<doInventoryTableIdMain;j++){
		if(locationChild==$("#childProduct"+(j+1)).val().split("$")[1]){
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var grandTotal1=0;
			$(".locationTotalAmount").each(function(){
				var splitId=$(this).attr("id").split("locationsplitamount")[1];
				if($("#childProduct"+(j+1)).val().split("$")[0]==$("#LocationChildId"+splitId).val().split("$")[0]){
					grandTotal1  +=  parseFloat($("#locationsplitamount"+splitId).val()==''?0:$("#locationsplitamount"+splitId).val());	
				}
			});
			var tempVal=parseInt(grandTotal1)-parseInt(productTotalAmount);
			if(tempVal!=-1 && tempVal!=1 && tempVal!=0){
				swal("Error!", "You can't initiate more than invoice amount.", "error");
				$("#locationtotalAmount"+id).val("0");
				$("#locationsplitamount"+id).val("0");
				locationWiseGrandTotal();
				return false;
			}
		}
	}
}

//location site wise amount change
function brandingWiseAmountChange(id){
	var splitAmount=parseFloat($("#brandingtotalAmount"+id).val()==''?0:$("#brandingtotalAmount"+id).val())*parseFloat($("#Brandingquantity"+id).val()==''?0:$("#Brandingquantity"+id).val());
	$("#brandingsplitamount"+id).val(splitAmount.toFixed(2));
	var locationChild=$("#Brandingchilddata"+id).val();
	var doInventoryTableIdMain=$("#doInventoryTableId-Main").find("tbody").find("tr").length;
	for(var j=0; j<doInventoryTableIdMain;j++){
		if(locationChild==$("#childProduct"+(j+1)).val().split("$")[1]){
			var productTotalAmount=parseFloat($("#mainTotalAmountId"+(j+1)).val());
			var grandTotal1=0;
			$(".brandingTotalAmount").each(function(){
				if($(this).val() == ""){ grandTotal1 = 0; 	}else{ 	grandTotal1  +=  parseFloat($(this).val());			}
			});
			var tempVal=parseInt(grandTotal1)-parseInt(productTotalAmount);
			if(tempVal!=-1 && tempVal!=1 && tempVal!=0){
				swal("Error!", "You can't initiate more than invoice amount.", "error");
				$("#brandingtotalAmount"+id).val("0");
				$("#brandingsplitamount"+id).val("0");
				BrandingWiseGrandTotal();
				return false;
			}
		}
	}
	BrandingWiseGrandTotal();
}
//checking number or not 
function isNumber(el, evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var num=el.value;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) { return false;   }
    //just one dot
    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) { return false;  }
    //get the carat position
    var caratPos = getSelectionStart(el);
    var dotPos = el.value.indexOf(".");
    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){ return false;  }
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

/*script for file upload*/
function addFile(){
  var classlength=$(".selectCount").length;
  var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
  if($("#file_select"+classLastId).val()==""){
	 swal("Error!", "Please select file.", "error");
	 $("#file_select"+classLastId).focus();
	 return false;
  }
  if(classlength>7){
	 swal("Error!", "You can't upload more than eight files.", "error");
	return false;
  }
  var btnid = $(".selectCount:last").attr("id").split("file_select")[1];
  var dynamicId = parseInt(classLastId) + 1;
  $(".files_place").append('<div class="clearfix"></div><input type="file" id="file_select'+dynamicId+'" name="file" accept="application/pdf,image/*" class="selectCount Mrgtop10" style="float:left;" onchange="filechange('+dynamicId+')"/><button type="button" class="btn btn-danger Mrgtop10" id="close_btn'+dynamicId+'" style="float:left;display:none;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div></div>');
}
//this is for file change to display close button
function filechange(id){
	var size_file = ($("#file_select"+id))[0].files[0]
	file_size = size_file.size;
	if((size_file.type)=='application/pdf'){
		if((file_size/1024)<=1024 && (size_file.type)=='application/pdf'){$("#close_btn"+id).show();
		$("#Add").show();}
		else{
			swal("Error!", "Your file size"+file_size+ "So Please upload Below this 1MB.", "error");
			$("#file_select"+id).val("");
		    return false;	
		}
	}
	else{
  $("#close_btn"+id).show();
  $("#Add").show();}
}
//this is for to delete the file
function filedelete(id){
  var classlength=$(".selectCount").length;
  if(classlength==1){
	 $("#file_select"+id).val("");
	 $("#close_btn"+id).hide();
	 $("#Add").hide();
	 return false;
  }
  $("#file_select"+id).remove() ;
  $("#close_btn"+id).remove();
}
/*delete conveyence table row*/
function deleteConveyancerow(current, id){
	var conveyanceTablelength = $(".conveyanceRow").length;
	var backendrowLength = $(".hiddentablestikeout").length;
	var TotalRowCount = conveyanceTablelength - backendrowLength;
	var n=0;
	$(".hiddentablestikeout").each(function(){ 
    	if($(this).val() == "D") {     n++;		}
	});
	var canIDelete = window.confirm("Do you want to delete?");
	if(canIDelete == false){ 	return false;	}
	if(TotalRowCount == "1" && n==backendrowLength){
		swal("Error!", "You can't delete this row.", "error");		
		return false;
	}
	$("#chargesrow"+id).remove();
	var tid=$('.conveyanceRow:last').attr('id');
	var res = tid.split("chargesrow")[1];
	if(res<id){
		$("#addNewChargesItemBtnId"+res).show();
	}
}

/*edit conveyance table row*/
function eidtConveyancerow(current, id){
	var canIEdit = window.confirm("Are you sure you want to edit?")
	if(canIEdit == false){ 	return false;	}
	else{
	 $("#Conveyance"+id).attr("readonly", false);
	 $("#ConveyanceAmount"+id).attr("readonly", false);
	 $("#GSTTax"+id).attr("readonly", false);;
	 $("#actionStatus"+id).val("E");
	}
}
/*static delete button strike out*/
function deleteConveyancerowstatic(current, stid){
	var backendrowLength = $(".hiddentablestikeout").length;
	var prevAction = $("#actionStatus"+stid).val();
	var canIDelete = window.confirm("Are you sure you want to delete?")
	if(canIDelete == false){  return false; }
	else{
	   var conveyanceTablelength = $(".conveyanceRow").length;
	   var backendrowLength = $(".hiddentablestikeout").length;
	   var TotalRowCount = conveyanceTablelength - backendrowLength;		
		$("#addEditChargesItemBtnId"+stid).prop('disabled', true);
		$("#addEditChargesItemBtnId"+stid).prop('disabled', true);
		$("#actionStatus"+stid).val("D");
		var n=0;
		$(".hiddentablestikeout").each(function(){
			if($(this).val() == "D") { 	 n++;    }
			if(n == conveyanceTablelength){
				alert("You can't delete this row.");
				$("#chargesrow"+stid).removeClass("strikeout");
				$("#addEditChargesItemBtnId"+stid).prop('disabled', false);
				$("#actionStatus"+stid).val(prevAction);
				$("#chargesrow"+stid).removeClass("strikeout");
				$("#Conveyance"+stid).removeClass("no-cursor");
				$("#ConveyanceAmount"+stid).removeClass("no-cursor");
				$("#GSTTax"+stid).removeClass("no-cursor");
				return false;
			}
			$("#chargesrow"+stid).addClass("strikeout");
			$("#Conveyance"+stid).addClass("no-cursor").attr("readonly", true);
			$("#GSTTax"+stid).addClass("no-cursor").attr("readonly", true);
			$("#ConveyanceAmount"+stid).addClass("no-cursor").attr("readonly", true);
			$("#GSTTax"+stid).addClass("no-cursor").attr("readonly", true);
		});
	}
}
// this function for to set To Date
function setTodate(id, idname){
  /*var currentDate=new Date($("#"+idname+"fromDate"+id).val());
	$("#"+idname+"toDate"+id).val('');
	$("#"+idname+"toDate"+id).datepicker( "option", "minDate", currentDate );	*/	
}
//Conveyance change
function conveyanceChange(id){
	if ($('#Conveyance'+id).val() == "999$None"){
		  $("#ConveyanceAmount"+id).val('0');
		  $("#ConveyanceAmount"+id).attr('readonly', 'true');
		  $("#GSTTax"+id).val( '1$0%');
		  $("#GSTTax"+id).attr('readonly', 'true');
		  $("#GSTAmount"+id).val('0');
		  $("#AmountAfterTax"+id).val('0');
		  }else{
			  $("#ConveyanceAmount"+id).val(' ');
			  $("#ConveyanceAmount"+id).removeAttr('readonly');
			  $("#GSTTax"+id).removeAttr('readonly');
			  $("#GSTAmount"+id).val('');
			  $("#AmountAfterTax"+id).val('');
			  $("#GSTTax"+id).val('');
		  }
}

//validating site name in multi site
function validatingSiteName(){
	var error=true;	
	$(".multsitenamecls").each(function(){
		if($(this).val()==''){
			alert("please select Site.");
			$(this).focus();
			return error=false;
		}
	});	
	return error;
}
//validating child product  in multi site
function validatingChildProduct(){
	var error=true;	
	$(".childprdctclsmulti").each(function(){
		if($(this).val()==''){
			swal("Error!", "Please select child product.", "error");
			$(this).focus();
			return error=false;
		}
	});	
	return error;
}

function validatingMultiSiteTable(){
	var error=true;
	$(".expenditure").each(function(){debugger;
		var id=$(this).attr("id").split("multisiteAdd")[1];
		if($("#exapendChildproduct"+id).val()==""){
			swal("Error!", "Please select child product.", "error");
			$("#exapendChildproduct"+id).focus();
			return error=false;
		}
		if($("#sitenameid"+id).val()==""){
			swal("Error!", "Please select site.", "error");
			$("#sitenameid"+id).focus();
			return error=false;
		}
		if($("#multisitequanty"+id).val()==""){
			swal("Error!", "Please enter quantity.", "error");
			$("#multisitequanty"+id).focus();
			return error=false;
		}
		if($("#multisplitamount"+id).val()==""){
			swal("Error!", "Please enter amount.", "error");
			$("#multisplitamount"+id).focus();
			return error=false;
		}
		if($("#totalamountmultsite"+id).val()==""){
			swal("Error!", "Please enter total amount.", "error");
			$("#totalamountmultsite"+id).focus();
			return error=false;
		}
	});
	return error;
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
