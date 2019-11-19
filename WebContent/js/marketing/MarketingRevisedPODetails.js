
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
	
	//validating product table
	var validateProductTableRow=validateProductRow();
	if(validateProductTableRow==false){
		return false;
	}
	
	var currentRows = document.getElementById("numberOfRows").value;
    document.getElementById("numberOfRows").value=parseInt(currentRows)+1;
	
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
		    
		    $("#addNewItemBtnId"+rowNum).hide();
		    
		    rowNum =parseInt($(".productchargesrow:last").attr("id").split("tr-class")[1])+1;
		    var rowid="tr-class"+rowNum;
			$(row).attr("id", rowid);
		    $(row).attr("class", "productchargesrow");
		    
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

	//validating charges table
	var validateChargesTableRow=validateChargesRow();
	if(validateChargesTableRow==false){
		return false;
	}
	var tbllength=$('#doInventoryChargesTableId').find('tr').length;
	if(tbllength==2){
		var tid=$('#doInventoryChargesTableId tr:last').attr('id');
		var res = tid.split("chargesrow")[1];
		$("#addDeleteItemBtnId"+res).show();
	}

	var currentRows = document.getElementById("noofTransRows").value;
	document.getElementById("noofTransRows").value=parseInt(currentRows)+1;
	
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
		    debugger;
		    var rowChargesNum =$(".chargesrow:last").attr("id").split("chargesrow")[1];
		    
		    $("#addNewChargesItemBtnId"+rowChargesNum).hide();
		    
		    rowChargesNum = parseInt(rowChargesNum)+1;
		    var rowid="chargesrow"+rowChargesNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "chargesrow");
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
	var canIDelete=window.confirm("Do you want to delete?");
	if(canIDelete==false){
		return false;
	}
	//removing row
	var n=0;
	$(".chargesrow").each(function(){debugger;
		var id=$(this).attr("id").split("chargesrow")[1];
		if($("#transactionActionValue"+id).val()!="R"){
			n++;
		}
	});

	if(n==1){
		alert("This row can't be deleted.");
		return false;
	}
   $("#chargesrow"+currentRow).remove();
	
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
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

function calculatequantitybase(rowId,currentORnew){
	var stQantity= $("#QuantityId"+ rowId).val()==""?0:$("#QuantityId"+ rowId).val();
	if (isNaN(stQantity)) {
		debugger;
		alert("Please enter valid data.");
		$("#QuantityId"+rowId).val("0");
		$("#QuantityId"+rowId).focus();
		
		return false;
	}
	var Quantity= document.getElementById("QuantityId"+ rowId).value;
	var price= document.getElementById("PriceId"+ rowId).value;
	
	//Calculating the basic amount
	var BasicAmonmt=parseFloat(Quantity)*parseFloat(price);
	$("#BasicAmountId"+rowId).val(parseFloat(BasicAmonmt).toFixed(2));
	
	// Calculating the discount
	var Disocuntval=$("#Discount"+ rowId).val();
	var DiscountamtAfrTax=parseFloat(BasicAmonmt - ( BasicAmonmt*Disocuntval/100 ));
	
	// Appending the discount amount
	$("#amtAfterDiscount"+ rowId).val(parseFloat(DiscountamtAfrTax).toFixed(2));

	//taking  the tax
	var e1 = document.getElementById("TaxId"+rowId);
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
	$("#TaxAmountId"+ rowId).val(parseFloat(taxAmntVal).toFixed(2));	
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	$("#AmountAfterTaxId"+ rowId).val(parseFloat(amontAfterTaxVal).toFixed(2));
	var chidProduct=$("#childProduct"+rowId).val();
	deleteWhileChangeProductTable(chidProduct.split("$")[0]);
}

/*************end cal price amount *******************/

function calculateTotalAmount(strSerialNumber) {
	var qty = $("#QuantityId" +strSerialNumber).val();
	var amnt = $("#PriceId"+strSerialNumber).val();
	if(isNaN(qty)){
		alert("Please enter valid data.");
		$("#stQantity" +strSerialNumber).val('');
		return false;
	}
	var  BAmnt = $("#BasicAmountId"+strSerialNumber).val(tAmnt);
	calculatequantitybase(strSerialNumber);
	var tAmnt = 0;
	var totalAmnt = (qty*amnt);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	tAmnt = parseFloat(totalAmnt);
	$("#BasicAmountId"+strSerialNumber).val(tAmnt.toFixed(2));
	
	// Calculating the discount
	var Disocuntval=document.getElementById("Discount"+ strSerialNumber).value;
	var DiscountamtAfrTax=parseFloat(tAmnt - ( tAmnt*Disocuntval/100 ));
	
	// Appending the discount amount
	$("#amtAfterDiscount"+ strSerialNumber).val(parseFloat(DiscountamtAfrTax).toFixed(2));
	
	checkQuantity(strSerialNumber);
	
	//taking  the tax
	var e1 = document.getElementById("TaxId"+strSerialNumber);
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
	document.getElementById("TaxAmountId"+ strSerialNumber).value = taxAmntVal;
	var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
	$("#AmountAfterTaxId"+ strSerialNumber).val(parseFloat(amontAfterTaxVal).toFixed(2));
	var chidProduct=$("#childProduct"+strSerialNumber).val();
	deleteWhileChangeProductTable(chidProduct.split("$")[0]);
 };

/********************** Calculate Discount Amount**************************/
	
	function calculateDiscountAmount(strSerialNumber){debugger;
	
		var Bamt= $("#BasicAmountId"+strSerialNumber).val();		
		var numbers = $("#Discount"+strSerialNumber).val();
		if(numbers>100){
			alert("Unable to update more than 100%.");
			 $("#Discount"+strSerialNumber).val('');
			 $("#Discount"+strSerialNumber).focus();
			 return false;
		}		
		var DamtAfrTax=Bamt - ( Bamt*numbers/100 );		
		$("#amtAfterDiscount"+strSerialNumber).val(parseFloat(DamtAfrTax).toFixed(2));
		//taking  the tax
		var e1 = document.getElementById("TaxId"+strSerialNumber);		
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
		document.getElementById("TaxAmountId"+ strSerialNumber).value = taxAmntVal;
		var amontAfterTaxVal= parseFloat(DamtAfrTax) + parseFloat(taxAmntVal);
		$("#AmountAfterTaxId"+ strSerialNumber).val(parseFloat(amontAfterTaxVal).toFixed(2));
		var chidProduct=$("#childProduct"+strSerialNumber).val();
		deleteWhileChangeProductTable(chidProduct.split("$")[0]);
	}
	
	//********* Tax calculation for Grid one ***************
	function calculateTaxAmount(rowNum) {
		debugger;
		var e = document.getElementById("TaxId"+rowNum);
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
		var chidProduct=$("#childProduct"+rowNum).val();
		deleteWhileChangeProductTable(chidProduct.split("$")[0]);
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
		$("#finalAmntDiv").text(parseFloat(tempAmnt).toFixed(2))
	    $("#ttlAmntForIncentEntryId").val(tempAmnt);
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
				var spltData = resp.split("|");				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSelect = "subProduct"+rowNum;
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
				var spltData = resp.split("|");
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subSubProdToSelect = "childProduct"+rowNum;		    	
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
				
				$("#UnitsOfMeasurementId"+rowNum).val("");				
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
//********************** Tax calculation GST for second grid*****************

function calculateGSTTaxAmount(rowNum) {
	debugger;
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
		document.getElementById("GSTAmount"+rowNum).value = "";
		document.getElementById("AmountAfterTaxx"+rowNum).value = "";
	}	
}
function formatChargesColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}

function removeRow(rowId){debugger;
var CanIDelete=window.confirm("Do you want to delete?");
if(CanIDelete==false){
	return false;
}
debugger;
var n=0;
$(".productchargesrow").each(function(){debugger;
	var id=$(this).attr("id").split("tr-class")[1];
	if($("#actionValueId"+id).val()!="R"){
		n++;
	}
});

if(n==1){
	alert("This row can't be deleted.");
	return false;
}

$("#tr-class"+rowId).addClass('strikeout');
$("#addDeleteItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoDivId'+rowId).removeAttr('id');	
$('#PriceId'+rowId).removeAttr('id');
$('#TaxAmountId'+rowId).removeAttr('id');
$('#BasicAmountId'+rowId).removeAttr('id');
$('#tax'+rowId).removeAttr('id');
$('#QuantityId'+rowId).removeAttr('id');
$('#AmountAfterTaxId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesId'+rowId).removeAttr('id');
$('#TaxOnOtherOrTransportChargesId'+rowId).removeAttr('id');
$('#OtherOrTransportChargesAfterTaxId'+rowId).removeAttr('id');
$('#TotalAmountId'+rowId).removeAttr('id');
$('#actionValueId'+rowId).val("R");
var chidProduct=$("#childProduct"+rowId).val();
deleteWhileChangeProductTable(chidProduct.split("$")[0]);
}

function editchargesInvoiceRow(rowId){debugger;

	var canIEdit=window.confirm("Do you want to edit?");
	if(canIEdit==false){
		return false;
	}	
	$("#Conveyance"+rowId).attr('readonly', false);
	$("#ConveyanceAmount"+rowId).attr('readonly', false);
	$("#GSTTax"+rowId).attr('readonly', false);
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
	$('#QuantityId'+rowId).prop('readonly', false).css('cursor','allowed');
	$('#PriceId'+rowId).prop('readonly', false).css('cursor','allowed');
	$('#TaxId'+rowId).removeAttr('readonly');
	$('#Discount'+rowId).prop('readonly', false);
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
	$('#hsnCode'+rowId).prop('readonly', true);
	$('#TaxAmount'+rowId).prop('readonly', true);
	$('#TaxAftertotalAmount'+rowId).attr('readonly', true);
	$('#OtherOrTransportChargesId'+rowId).prop('readonly', true);
	$('#TaxOnOtherOrTransportChargesId'+rowId).prop('readonly', true);
$('#OtherOrTransportChargesAfterTaxId'+rowId).prop('readonly',true);
$('#TotalAmountId'+rowId).prop('readonly',true);

$("#addsave").show();

}

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
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	$("#TaxAmountId"+rowNum).val(parseFloat(taxAmnt).toFixed(2));
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	$("#AmountAfterTaxId"+rowNum).val(parseFloat(amntaftTx).toFixed(2));
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
function calculateOtherCharges() {
	
	//validating product table
	var validateProductTableRow=validateProductRow();
	if(validateProductTableRow==false){
		return false;
	}
	//validating charges table
	var validateChargesTableRow=validateChargesRow();
	if(validateChargesTableRow==false){
		return false;
	}
	//validating location field details table
	/*var validateLocationRow=validateLocationDurationTable();
	if(validateLocationRow==false){
		return false;
	}
	*/
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
		if(rsltTtlBscAmnt!=0){
			var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		}else{
			var charges = 0;
		}
	//	var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
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
	  
	var Quan = document.getElementById("QuantityId"+strSerialNumber).value;
	//alert(invoiceNum);
	
	if(Quan == "" || Quan == null || Quan == '') {
		alert("Please enter qunatity.");
		//document.getElementById("Quan").focus();
		return false;
	}				
	  
	var Amt = document.getElementById("PriceId"+strSerialNumber).value;
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
		var groupId = $("#groupId"+currentRowNum).val();
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
  	    				alert("Please choose expire date.");
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
	$("#ChildProduct"+rowNum).addClass("ChildProduct");
	var rv = true;
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
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
function checkUpdatePoQuantity(row){debugger;
	var actuqlQuantity=$("#stQantity"+row).val();
	var oldQuantity=$("#stQuantity"+row).val();
	var modifiedQuantity=Math.round((oldQuantity)*(120/100));
	if(modifiedQuantity>=actuqlQuantity){
		return true;
	}else{
		alert("Please enter quantity below "+modifiedQuantity+".");
		$("#stQantity"+row).val('');
		return false;
	}
}

	/*====================================== for checking boq quantity when po is updated start==============================================*/

function removeTransRow(rowId){debugger;
	var canIdelete=window.confirm("Do you want to delete?");
	if(canIdelete==false){
		return false;
	}
	var n=0;
	$(".chargesrow").each(function(){debugger;
		var id=$(this).attr("id").split("chargesrow")[1];
		if($("#transactionActionValue"+id).val()!="R"){
			n++;
		}
	});
	
	if(n==1){
		alert("This row can't be deleted.");
		return false;
	}
	/*	document.getElementById("ractionValueId"+rowId).value = "R";*/
	
	$("#chargesrow"+rowId).addClass('strikeout');
	$("#addDeleteChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
	$("#editchargesItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
	/*$("#addNewChargesItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');*/
	$('#snoChargesDivId'+rowId).removeAttr('id');	
	$('#transactionActionValue'+rowId).val("R");

}


function populateSite(id) {debugger;
var siteName=$("#siteNameId").val();
var url = "loadAndSetSiteNameInfo.spring?siteName="+siteName;

 $.ajax({
 url : url,
 type : "get",
 Cdata : "",
 contentType : "application/json",
 success : function(data) {
	  console.log(data);
	  Cdata = data.split("@@");
	  
	  var resultArray = [];
	  for(var i=0;i<Cdata.length;i++){
	      resultArray.push(Cdata[i].split("@@")[0]);
	  }
	  $("#siteNameId").autocomplete({
	  		source : resultArray,
	  		change: function (event, ui) {
               if(!ui.item){
              	//if list item not selected then make the text box null	
              	 $("#siteNameId").val("");
               }
             }/*,
	  		select: function (event, ui) {
               AutoCompleteSelectHandler(event, ui);
           }*/

	  	});
 },
 error:  function(data, status, er){
	 // alert(data+"_"+status+"_"+er);
	  }
 });
}
//append location row written by ashok
function addLocationRow(btn){
	debugger;
	var PoId=$("#poToId").val();
	  if(PoId=="SiteWise"){
		  var siteNameTop=$("#siteNameId").val();
		  if(siteNameTop=="" || siteNameTop==" "){
			  alert("Please enter site name.");
			  $("#siteNameId").focus();
			  return false;
		  }
	  }
	  if(PoId=="LocationWise"){
		var siteSelect=$("#site").val();
		if(siteSelect==""){
			alert("Please select site.");
			$("#site").focus();
			return false;
		}
	  }
	calculateOtherCharges();
	var valTableData=validateLocationDurationTable();
	if(valTableData==false){
		return false;
	}
	var locationlastID=$(".location:last").attr("id").split("location")[1];
	$("#addLocationPlusBtn"+locationlastID).hide();
	var tbllength=$('.location').length;
	if(tbllength==1){
		$("#addLocationDeleteBtn"+locationlastID).show();
	}
	
	var rowLCount = btn+1;
	var rowCount=$(".location").length;
	console.log(rowCount);
	var rowNew = "<tr  id='location"+rowLCount+"' class='location'><td style='width:50px;'>"+rowLCount+"</td><td>" +
			"<select class='form-control' id='locationChildProduct"+rowLCount+"' onchange='addLocation("+rowLCount+")' name='locationChildProduct"+rowLCount+"' >" +
			/*"<option>Acid</option>" +
			"<option>Air Spray</option><option>Mobile Phones</option>" +*/
			"</select>" +
			"</td><td><input type='hidden' placeholder='' class='form-control' id='location_Id"+rowLCount+"' name='location_Id"+rowLCount+"'/><select placeholder='' class='form-control' id='location_mar"+rowLCount+"' name='location_mar"+rowLCount+"'><option>--Select---</option></select></td><td><div class='input-group'><input type='text' placeholder='Select from date' class='form-control readonly-color' id='from_date_location"+rowLCount+"'onchange='fromDateChange("+rowLCount+")' name='from_date_location"+rowLCount+"' autocomplete='off' readonly='true'/><label class='input-group-addon btn input-group-addon-border' for='from_date_location"+rowLCount+"'><span class='fa fa-calendar'></span></label></div></td><td><div class='input-group'><input type='text' placeholder='Select to date' class='form-control readonly-color' onchange='toDateChange("+rowLCount+")' id='to_date_location"+rowLCount+"' name='to_date_location"+rowLCount+"' autocomplete='off' readonly='true'/><label class='input-group-addon btn input-group-addon-border' for='to_date_location"+rowLCount+"'><span class='fa fa-calendar'></span></label></div></td><td><div class='input-group'><input type='text'  class='form-control readonly-color' id='timepicker"+rowLCount+"' name='timepicker"+rowLCount+"' placeholder='Select time' autocomplete='off' readonly='true' style='z-index: 0;'/><label class='input-group-addon btn input-group-addon-border' for='timepicker"+rowLCount+"'><span class='fa fa-clock-o'></span></label></div></td><td><input type='text'  class='form-control' id='locationQuantity"+rowLCount+"' onkeyup='calculateLocationAmount("+rowLCount+")' name='locationQuantity"+rowLCount+"' placeholder='Please Enter Quantity' autocomplete='off'/></td><td><select class='form-control' id='site_Name"+rowLCount+"' name='site_Name"+rowLCount+"'></select></td><td><input type='text'   class='form-control' id='price_Aftertax"+rowLCount+"' name='price_Aftertax"+rowLCount+"' onkeyup='locationAndDurationTablePricePerUnitAfterTax("+rowLCount+")'  placeholder='Please Enter Price Per Unit After Tax' autocomplete='off'/></td><td><input type='text'   class='form-control' id='total_Amount"+rowLCount+"' name='total_Amount"+rowLCount+"' autocomplete='off'  placeholder='Please Enter Total Amount'  onkeyup='durationTotalAmopuntChange("+rowLCount+")'/><input type='hidden'  id='locationActionValue"+rowLCount+"' value='N' name='locationActionValue"+rowLCount+"'/></td><td><button type='button' id='addLocationPlusBtn"+rowLCount+"' onclick='addLocationRow("+rowLCount+")' class='btnaction'><i class='fa fa-plus'></i></button><button type='button'  id='addLocationDeleteBtn"+rowLCount+"' onclick='deleteLocationRow("+rowLCount+")' class='btnaction'><i class='fa fa-trash'></i></button></td></tr>";
    $("#locationDurationTbody").append(rowNew);
    var PoId=$("#poToId").val();
	  if(PoId=="SiteWise"){
		  $("#site_Name"+rowLCount).html("<option>"+$("#siteNameId").val()+"</option>");
		  $("#site_Name"+rowLCount).attr("readonly");
	  }
	  if(PoId=="LocationWise"){debugger;
		  var tempOptions="<option value=''>--select--</option>";
		for(var i=0;i<PODATA.length;i++){
			 tempOptions+="<option value='"+PODATA[i]+"'>"+PODATA[i].split("$")[1]+"</option>";
		}  
		 $("#site_Name"+rowLCount).html(tempOptions);
	  }
	  debugger;
	  if(PoId=="BrandingWise"){
		  //brandingData();
		  var  brandingData= localStorage.getItem('BrandingDataFromLocalStorage');
		  brandingData=brandingData.split(",");
		  var tempOptions="<option value=''>--select--</option>";
			for(var i=0;i<brandingData.length;i++){
				 tempOptions+="<option value='"+brandingData[i]+"'>"+brandingData[i].split("$")[1]+"</option>";
			}  
			 $("#site_Name"+rowLCount).html(tempOptions);
	  }
    $("#addLocationPlusBtn"+btn).hide();
    $("#addLocationDeleteBtn"+btn).show();
    $("#from_date_location"+rowLCount).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
    $("#to_date_location"+rowLCount).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
    $("#timepicker"+rowLCount).timepicker();
    //$("#locationQuantity"+rowLCount).val($("#QuantityId1").val());
    if(rowCount==1){
		$("#addLocationDeleteBtn"+btn).show();
     }
  //loading child data;
    loadChildDataFromProductTable();
    console.log("childDataForDuration: "+childDataForDuration);
	var selectoption="<option value=''>--select--</option>"
	for(var i=0;i<childDataForDuration.length;i++){
		selectoption+="<option value='"+childDataForDuration[i]+"'>"+childDataForDuration[i].split("$")[1]+"</option>";
	}
	$("#locationChildProduct"+rowLCount).html(selectoption);
	//focusing on last row first coloum	
	$("#locationChildProduct"+rowLCount).focus();
}



//delete a row in location and duration table
function deleteLocationRow(btn){
	var CanIDelet=window.confirm("Do you want to delete?");
	if(CanIDelet==false){
		return false;
	}
	var rowCount=$(".location").length;
	//checking row count
	if(rowCount==1){
		alert("This row can't be deleted.");
		return false;
	}	
	//deleting row
    $("#location"+btn).remove();
	 //finding last row id
	   debugger;
	 var lastRowId=$(".location:last").attr("id");
	 var splitId=lastRowId.split("location")[1];
	 if(rowCount==2){
			$("#addLocationDeleteBtn"+splitId).hide();
	 }
	 if(splitId<btn){		
			$("#addLocationPlusBtn"+splitId).show();
	 }	
}


function removeLocationRow(rowId){
	var canIdelete=window.confirm("Do you want to delete?");
	if(canIdelete==false){
		return false;
	}
	var n=0;
	$(".location").each(function(){debugger;
		var id=$(this).attr("id").split("location")[1];
		if($("#locationActionValue"+id).val()!="R"){
			n++;
		}
	});

	if(n==1){
		alert("This row can't be deleted.");
		return false;
	}
	/*	document.getElementById("ractionValueId"+rowId).value = "R";*/

	$("#location"+rowId).addClass('strikeout');
	$("#addLocationDeleteBtn"+rowId).attr("disabled", true).css('cursor','not-allowed');
	$("#addLocationEditBtn"+rowId).attr("disabled", true).css('cursor','not-allowed');
	$('#locationActionValue'+rowId).val("R");
}


function editLocationRow(rowId){
	console.log("Edit Location...");
	var canIdelete=window.confirm("Do you want to edit?");
	if(canIdelete==false){
		return false;
	}
	$("#locationChildProduct"+rowId).attr("readonly", false);
	$("#location_mar"+rowId).attr("readonly", false);
	$("#fromDateLable"+rowId).attr("disabled", false);
	$("#toDateLable"+rowId).attr("disabled", false);
	$("#timeLable"+rowId).attr("disabled", false);
	$("#locationQuantity"+rowId).attr("readonly", false);
	$("#site_Name"+rowId).attr("readonly", false);
	$("#price_Aftertax"+rowId).attr("readonly", false);
	$("#total_Amount"+rowId).attr("readonly", false);	
	$("#addLocationEditBtn"+rowId).attr("disabled", true).css('cursor','not-allowed');
	$('#locationActionValue'+rowId).val("E");
	var childProduct=$("#locationChildProduct"+rowId).val();
	var siteName=$("#site_Name"+rowId).val();
	$("#from_date_location"+rowId).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
	 $("#to_date_location"+rowId).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
	 debugger;
	 $("#timepicker"+rowId).timepicker({
		 timeFormat: 'h:mm p',
		    interval: 10,
		    minTime: '00.00AM',
		    maxTime: '11.59PM',
		    startTime: '00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
		});
	 
	 var PoId=$("#poToId").val();
	  if(PoId=="SiteWise"){
		  loadChildDataFromProductTable();
		  updateChildAtEditLocation(rowId);
	    	 
		  $("#site_Name"+rowId).html("<option>"+$("#siteNameId").val()+"</option>");
  		  $("#site_Name"+rowId).attr("readonly");		
	  }
     if(PoId=="LocationWise"){
    	 loadChildDataFromProductTable();
    	 updateChildAtEditLocation(rowId);
    	 var PODATA=localStorage.setItem('locatioWiseDataFromLocalStorage');
    	 PODATA=PODATA.split(",");
    	 var tempOptions="<option value=''>--select--</option>";
   		for(var i=0;i<PODATA.length;i++){
   			 tempOptions+="<option value='"+PODATA[i]+"'>"+PODATA[i].split("$")[1]+"</option>";
   		}  
   		 $("#site_Name"+rowId).html(tempOptions); 
	  }
     if(PoId=="BrandingWise"){
    	 loadChildDataFromProductTable();
    	 updateChildAtEditLocation(rowId);
    	 
    	 var  brandingData= localStorage.getItem('BrandingDataFromLocalStorage');
		  brandingData=brandingData.split(",");
		  var tempOptions="<option value=''>--select--</option>";
			for(var i=0;i<brandingData.length;i++){
				 tempOptions+="<option value='"+brandingData[i]+"'>"+brandingData[i].split("$")[1]+"</option>";
			}  
			$("#site_Name"+rowId).html(tempOptions);
			$("#site_Name"+rowId).val(siteName);			
	  }
}

function updateChildAtEditLocation(rowId){
	var childProduct=$("#locationChildProduct"+rowId).val();
	var selectoption="<option value=''>--select--</option>";
	 for(var i=0;i<childDataForDuration.length;i++){
		selectoption+="<option value='"+childDataForDuration[i]+"'>"+childDataForDuration[i].split("$")[1]+"</option>"
	}
	 $("#locationChildProduct"+rowId).html(selectoption);
	 $("#locationChildProduct"+rowId).val(childProduct);
}



function deleteWhileChangeProductTable(childproduct){debugger;	
	$(".location").each(function(){
		var id=$(this).attr("id").split("location")[1];
		if($("#locationChildProduct"+id).val().split("$")[0]==childproduct){
			$("#location"+id).remove();			
		}
	});
	var tableLength=$(".location").length;
	if(tableLength>0){
		var id=$(".location:last").attr("id").split("location")[1];
		$("#addLocationPlusBtn"+id).show();
	}else{
		createFirstRowForLocationDurationTable();
	}
	
}

function createFirstRowForLocationDurationTable(){
	   //loading child data;
	 $("#refreshIcon").addClass("imageRot").one('webkitAnimationEnd mozAnimationEnd oAnimationEnd msAnimationEnd animationend', function () {
		 $("#refreshIcon").removeClass("imageRot"); //remove anim class	           
	    });
    loadChildDataFromProductTable();
    console.log("childDataForDuration: "+childDataForDuration);
    var str='<tr id="location1" class="location">';
        str+='<td style="width:50px;">1</td>'
        str+='<td><select class="form-control" id="locationChildProduct1" name="locationChildProduct1"  onchange="addLocation(1)"></select></td>'
        str+='<td><select class="form-control" id="location_mar1" name="location_mar1"></select><input type="hidden" class="form-control" id="location_Id1" name="location_Id1"></td>'
        str+='<td><div class="input-group"><input type="text" id="from_date_location1" value="" onchange="fromDateChange(1)" class="form-control readonly-color" name="from_date_location1" placeholder="Select from date"  autocomplete="off"/><label class="input-group-addon btn input-group-addon-border" for="from_date_location1"><span class="fa fa-calendar"></span></label></div></td>'
        str+='<td><div class="input-group"><input type="text" id="to_date_location1" value="" onchange="toDateChange(1)" class="form-control readonly-color" name="to_date_location1" placeholder="Select to date"  autocomplete="off"/><label class="input-group-addon btn input-group-addon-border" for="to_date_location1"><span class="fa fa-calendar"></span></label></div></td>'
        str+='<td><div class="input-group"><input type="text"  class="form-control readonly-color" id="timepicker1" value="" readonly="true" name="timepicker1" placeholder="Select time"  autocomplete="off"/><label class="input-group-addon btn input-group-addon-border" for="timepicker1"><span class="fa fa-clock-o"></span></label></div></td>'
        str+='<td><input type="text"  class="form-control" id="locationQuantity1" value=""  name="locationQuantity1" value="" onkeyup="calculateLocationAmount(1)" placeholder="Please Enter Quantity"  autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>'
        str+='<td><select class="form-control" id="site_Name1" name="site_Name1"></select></td>'
        str+='<td><input type="text"   class="form-control" id="price_Aftertax1" value="" name="price_Aftertax1" value="" onkeyup="locationAndDurationTablePricePerUnitAfterTax(1)"  placeholder="Please Enter Price Per Unit After Tax" autocomplete="off" onkeypress="return validateNumbers(this, event);"/></td>'
        str+='<td><input type="text"   class="form-control" id="total_Amount1" value="" name="total_Amount1" value="" onkeyup="durationTotalAmopuntChange(1)"  autocomplete="off" placeholder="Please Enter Total Amount" onkeypress="return validateNumbers(this, event);"/><input type="hidden"  id="locationActionValue1" value="N" name="locationActionValue1"/></td>'
  	  str+='<td><button type="button" class="btnaction" id="addLocationPlusBtn1" onclick="addLocationRow(1)"><i class="fa fa-plus"></i></button>'
  	  str+=' <button type="button" style="display:none;" class="btnaction" id="addLocationDeleteBtn1" onclick="deleteLocationRow(1)"><i class="fa fa-trash"></i></button></td>'
        str+='</tr>'; 
     $("#locationDurationTbody").html(str);
     //appending child data
     debugger;
   	var selectoption="<option value=''>--select--</option>";
 		for(var i=0;i<childDataForDuration.length;i++){
 			selectoption+="<option value='"+childDataForDuration[i]+"'>"+childDataForDuration[i].split("$")[1]+"</option>"
 		}
   	var PoId=$("#poToId").val();
	  if(PoId=="SiteWise"){
		  $("#site_Name1").html("<option>"+$("#siteNameId").val()+"</option>");
		  $("#site_Name1").attr("readonly");
	  }
	  if(PoId=="LocationWise"){debugger;
		 var PODATA=localStorage.getItem('locatioWiseDataFromLocalStorage');
		 PODATA=PODATA.split(",");
		 var tempOptions="<option value=''>--select--</option>";
		 for(var i=0;i<PODATA.length;i++){
			 tempOptions+="<option value='"+PODATA[i]+"'>"+PODATA[i].split("$")[1]+"</option>";
		}  
     $("#site_Name1").html(tempOptions); 
	  }
	  if(PoId=="BrandingWise"){
		 var  brandingData= localStorage.getItem('BrandingDataFromLocalStorage');
		  brandingData=brandingData.split(",");
		  var tempOptions="<option value=''>--select--</option>";
		 for(var i=0;i<brandingData.length;i++){
			 tempOptions+="<option value='"+brandingData[i]+"'>"+brandingData[i].split("$")[1]+"</option>";
		 }  
		 $("#site_Name1").html(tempOptions);
	  }
     $("#locationChildProduct1").html(selectoption);
     $("#from_date_location1").datepicker({dateFormat: 'dd-M-y',
			 changeMonth: true,
		      changeYear: true});
		 $("#to_date_location1").datepicker({dateFormat: 'dd-M-y',
			 changeMonth: true,
		      changeYear: true
		 });
		 $("#timepicker1").timepicker({
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

function calculateLocationAmount(id) {debugger;
//checking number or not
if(isNaN($("#locationQuantity"+id).val())){
	alert("Please enter valid data.");
	$("#locationQuantity"+id).val("");
	$("#locationQuantity"+id).focus();
	return false;
}
var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
var Quantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
var totalAmount=parseFloat($("#total_Amount"+id).val()==""?0:$("#total_Amount"+id).val());
var totalAmnt = (totalAmount/Quantity);

$("#price_Aftertax"+id).val(totalAmnt.toFixed(2));
$(".hiddenchild").each(function(){debugger;
if($(this).val().split("$")[0]==locationChildProduct){debugger;
   var productChildID=$(this).val().split("$")[0];
	 var splitId1=$(this).attr("id").split("childProduct")[1];
	 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
	 var productQty=parseFloat($("#QuantityId"+splitId1).val());
	 var locationTotalAmount=0;
	 var locationQty=0;
	 $(".location").each(function(){
		var splitId=$(this).attr("id").split("location")[1];
		if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
			locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
			locationQty+=parseFloat($("#locationQuantity"+splitId).val());
		}
	 })
	 if(Quantity>productQty){
		 $("#locationQuantity"+id).val("0");
		 $("#price_Aftertax"+id).val("0");
		 $("#total_Amount"+id).val("0");
		 alert("You can't initiate more than childproduct quantity.")
		 return false;
	 }
	 if(locationTotalAmount>productTotalAmount){
		 $("#locationQuantity"+id).val("0");
		 $("#price_Aftertax"+id).val("0");
		 $("#total_Amount"+id).val("0")
		 alert("You can't initiate more than invoice amount.")
		 return false;
	 }
}
});

//totalAmnt = Math.round(totalAmnt * 100)/100;
}
//calculating location total amount by changing price 
function locationAndDurationTablePricePerUnitAfterTax(id){debugger;
	//checking number or not
	if(isNaN($("#price_Aftertax"+id).val())){
		alert("Please enter valid data.");
		$("#price_Aftertax"+id).val("");
		$("#price_Aftertax"+id).focus();
		return false;
	}
	var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
	var price_Aftertax=parseFloat($("#price_Aftertax"+id).val()==''?0:$("#price_Aftertax"+id).val());
	var locationQuantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
	var durationTotalAmount=price_Aftertax*locationQuantity;
  $("#total_Amount"+id).val(durationTotalAmount.toFixed(2));
	$(".hiddenchild").each(function(){debugger;
	if($(this).val().split("$")[0]==locationChildProduct){debugger;
	var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("childProduct")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var locationTotalAmount=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
			}
		 })
		 if(locationTotalAmount>productTotalAmount){
			 $("#total_Amount"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 alert("You can't initiate more than invoice amount.")
			 return false;
		 }
	  }
	});
	
	
}



//duration table total Amount Change
function durationTotalAmopuntChange(id){debugger;
	//checking number or not
	if(isNaN($("#total_Amount"+id).val())){
		alert("Please enter valid data.");
		$("#total_Amount"+id).val("");
		$("#total_Amount"+id).focus();
		return false;
	}
	var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
	var total_Amount=parseFloat($("#total_Amount"+id).val()==''?0:$("#total_Amount"+id).val());
	var locationQuantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
	var durationTotalAmount=total_Amount/locationQuantity;
	if($("#locationQuantity"+id).val()=='' || $("#locationQuantity"+id).val()=='0'){
		durationTotalAmount=0;
	}
$("#price_Aftertax"+id).val(durationTotalAmount.toFixed(2));
	$(".hiddenchild").each(function(){debugger;
	if($(this).val().split("$")[0]==locationChildProduct){debugger;
	var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("childProduct")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var locationTotalAmount=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
			}
		 })
		 if(locationTotalAmount>productTotalAmount){
			 $("#total_Amount"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 alert("You can't initiate more than invoice amount.")
			 return false;
		 }
	  }
	});
}

//checking invoice amount with duration table amount based on child product
function validateInvoiceAMountWithDurationTable(){
	var error=true;
	$(".hiddenchild").each(function(){debugger;
	     var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("childProduct")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var productTotalQty=parseFloat($("#QuantityId"+splitId1).val());
		 var locationTotalAmount=0;
		 var chilQtyVal=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
				chilQtyVal+=parseFloat($("#locationQuantity"+splitId).val());
			}
		 })
		 if(chilQtyVal>productTotalQty){
			 alert("You can't initiate more than childproduct quantity in Field Location & Duration Details table.");
			 return error=false;
		 }
		 if(locationTotalAmount>productTotalAmount){
			 alert("You can't initiate more than invoice amount.")
			 return error=false;
		 }
	  
	});
	return error;
}
function checkQuantity(row){debugger;
var error=true;
var receiveQuan=parseFloat($("#ReceivedQty"+row).val());
var actuqlQuantity=parseFloat($("#QuantityId"+row).val());
var actuaklQuantityForCheck=$("#QuantityId"+row).val();

if($("#actionValueId"+row).val()!="N"){
	//console.log($("#newRow").val());
	if(actuqlQuantity>=receiveQuan){
		//return true;
	}else{	
		if(actuaklQuantityForCheck!=''){
		alert("please enter above "+receiveQuan+" Quantity.");
		$("#QuantityId"+row).val('');
		//$("#stQantity"+row).focus();
		return false;
	}
	}

}
}
