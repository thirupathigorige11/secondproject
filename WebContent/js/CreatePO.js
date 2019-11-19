
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
          		$("#ChildProduct"+rowNum).val("");
          		var tablelength=$("#doInventoryTableId > tbody > tr").length;	
          		if(tablelength>1){
          			debugger;
          			for(var i=1;i<tablelength;i++){
          				debugger;
          				var compareChild=$("#ChildProduct"+i).val();
          				if(compareChild==prodName){
          					alert("This child product is already exist, Please choose different child product.");	
          					$("#ChildProduct"+rowNum).val('');
                          	var emptychild=$("#ChildProduct"+rowNum).val();
                          	loadUnits(emptychild, rowNum); 
                          	return false;
          					
          				   }  
          				else{
          					this._trigger( "select", event, {
                                item: ui.item.option
                              }); 
                        	 loadUnits(prodId, rowNum); 
          				}
          			}

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
	
	//validating product table written by thirupathi
	var valProductTable=validateRowData();
	if(valProductTable==false){
		return false;
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
		    var rowNum = parseFloat($(".productTableRow:last").attr("id").split("tr-class")[1]);		    		    
		   $("#addNewItemBtnId"+rowNum).hide();		    
		    rowNum = rowNum+1;		    
		    var rowid="tr-class"+rowNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "productTableRow");
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
		    document.getElementById("Product"+rowNum).focus();
		}
	}
}

//Append Row for other charges
function appendChargesRow() {debugger;
	//validating charges table written by thirupathi
	var valChargesTable=validateChargesTableRows();
	if(valChargesTable==false){
		return false;
	}
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
			$(row).attr("class", "chargesRow");
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
	var canIDelete=window.confirm("Do you want to delete row?")
	if(canIDelete==false){
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
		$("#addDeleteChargesItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calcuLateFinalAmount();
}

function deleteproductRow(btn, currentRow) {
	debugger;
	var canIDelete=window.confirm("Do you want to delete product.");
	if(canIDelete==false){
		return false;
	}
	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryTableId').find('tr').length;
	var n=1;
	$(".productTableRow").each(function(){
		var rowId=$(this).attr("id").split("tr-class")[1];
		if($("#idDeletedOrNot"+rowId).val()=="D"){
			n++;
		}
	});
	if((rowscount-1)==n){
		alert("This row can't be deleted.")
		return false;
	}
	//removing row
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
function calculateTotalAmount(strSerialNumber) {debugger;

	var qty = $("#stQantity" +strSerialNumber).val()==''?0:$("#stQantity" +strSerialNumber).val();
	var amnt = $("#price"+strSerialNumber).val()==""?0:$("#price"+strSerialNumber).val();
	if (isNaN(qty)) {
	   alert("Please enter valid data");
	   $("#stQantity" +strSerialNumber).val("");
	   $("#stQantity" +strSerialNumber).focus();
	   return false;
	}
	if (isNaN(amnt)) {
		   alert("Please enter valid data");
		   $("#price" +strSerialNumber).val("");
		   $("#price" +strSerialNumber).focus();
		   return false;
	}
	validatePOBOQQuantity(strSerialNumber);
	var  BAmnt = $("#BasicAmountId"+strSerialNumber).val(tAmnt);	
	var tAmnt = 0;
	
	var totalAmnt = (qty*amnt);
	totalAmnt = Math.round(totalAmnt * 100)/100;
	
	tAmnt = parseFloat(totalAmnt);
	$("#BasicAmountId"+strSerialNumber).val(tAmnt.toFixed(2));
	calculatequantitybase(strSerialNumber);
	calculateDiscountAmount(strSerialNumber);
	  
	};
/********************** Calculate Discount Amount**************************/
	
	function calculateDiscountAmount(strSerialNumber){		debugger;
		
		var discountnum="Discount"+strSerialNumber;    
		 var discountval = document.getElementById(discountnum).value;
		 if (isNaN(discountval)) {
			   alert("Please enter valid data");
			   $("#Discount"+strSerialNumber).val("");
			   $("#Discount"+strSerialNumber).focus();
			   return false;
		}
		 
		 	if (discountval.includes("%")){
		 		discval = discountval.substring(0, discountval.length-1);
		 	}else if(discountval.includes("")){
		 		discval = discountval;
		 		
		 	}		
		if(discountval > 100){
			alert('Please enter the discount 100 below');
			$("#Discount"+strSerialNumber).val('');
			$("#Discount"+strSerialNumber).focus();
			return false;
		}
		
		var Bamt= $("#BasicAmountId"+strSerialNumber).val();
		var  BAmnt = $("#taxAmount"+strSerialNumber).val(DamtAfrTax);		
		var numbers = $("#Discount"+strSerialNumber).val();
		
		var DamtAfrTax=Bamt - ( Bamt*discval/100 );
		
		$("#amtAfterDiscount"+strSerialNumber).val(parseFloat(DamtAfrTax).toFixed(2));
		calculateTaxAmount(strSerialNumber);
	}
	
	//********* Tax calculation for Grid one ***************
	function calculateTaxAmount(rowNum) {
		debugger;
		var selectedTax =$("#taxAmount"+rowNum).val()
		var basicAmnt =$("#amtAfterDiscount"+rowNum).val()==""?0:$("#amtAfterDiscount"+rowNum).val();
		if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
			selectedTax = selectedTax.split("$")[1];			
		}
		else {
			$("#TaxAmountId"+rowNum).val("");
			$("#AmountAfterTaxId"+rowNum).val("");
			selectedTax=0;
		}
		percentage(basicAmnt, selectedTax, rowNum);
	}
	
	//********** End Tax calculation for grid one***************
	function calculatesize(){
		//var filename=$("#file1").val();
		//alert("file name"+filename);
		//alert(filename.size());
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
		
		var selectetdtax = e1.options[e1.selectedIndex].value;
		
		if(selectetdtax != "" && selectetdtax != '' && selectetdtax != null) {
			selectedTax1 = selectetdtax.split("$")[1];
			
			var selectedTaxval1 = selectedTax1;
			var selectedTaxval = parseFloat(selectedTaxval1);
		}
		
		
		// Calculating the tax 
		var taxAmntVal = (DiscountamtAfrTax/100) * selectedTaxval;
		
		// appending the tax amount
		document.getElementById("TaxAmount"+ rowId).value = parseFloat(taxAmntVal).toFixed(2);
		
		//
		var amontAfterTaxVal= parseFloat(DiscountamtAfrTax) + parseFloat(taxAmntVal);
		document.getElementById("TaxAftertotalAmount"+ rowId).value = parseFloat(amontAfterTaxVal).toFixed(2);
		document.getElementById("TotalAmountId"+ rowId).value = parseFloat(amontAfterTaxVal).toFixed(2);
		
	
	}
	
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
	    document.getElementById("finalAmntDiv").innerHTML = parseFloat(tempAmnt).toFixed(2);
	    $("#ttlAmntForIncentEntryId").val(parseFloat(tempAmnt).toFixed(2));
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
			    		$("#groupId"+rowNum).val(data[2]);	
			    		//document.getElementById("groupId"+rowNum).value = data[2];
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
	var ConveyanceAmount=$("#ConveyanceAmount"+rowNum).val();
	if(isNaN(ConveyanceAmount)){
		alert("Please enter valid data.");
		$("#ConveyanceAmount"+rowNum).val('');
		return false;
	}
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

function removeRow(rowId){
alert(" Do you want to remove the Product");

/*	document.getElementById("ractionValueId1).value = "R";*/

$("#tr-class"+ rowId).addClass('strikeout');
$("#combobox"+ rowId).attr("disabled", true).css('cursor','not-allowed');
$("#comboboxsubProd").attr("disabled", true).css('cursor','not-allowed');
//$("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#snoDivId"+rowId).removeAttr('id');	
$("#price"+rowId).removeAttr('id');
$("#TaxAmountId"+rowId).removeAttr('id');
$("#BasicAmountId"+rowId).removeAttr('id');
$("#tax"+rowId).removeAttr('id');
$("#QuantityId"+rowId).removeAttr('id');
$("#AmountAfterTaxId"+rowId).removeAttr('id');
$("#OtherOrTransportChargesId"+rowId).removeAttr('id');
$("#TaxOnOtherOrTransportChargesId"+rowId).removeAttr('id');
$("#OtherOrTransportChargesAfterTaxId"+rowId).removeAttr('id');
$("#TotalAmountId"+rowId).removeAttr('id');
$("#amtAfterDiscount"+rowId).removeAttr('id');
$("#stQantity"+rowId).removeAttr('id');
//Written by thirupathi for assigning D value to that current deleted row.
$("#idDeletedOrNot"+rowId).val("D");

}

function editInvoiceRow(rowId){debugger;

alert("Do you want to update the Po product row " + rowId);

$("#comboboxsubProd1").attr("disabled", true).css('cursor','not-allowed');


$('.disable-class'+rowId).prop('readonly',false);
$('.bt-taxamout-coursor'+rowId).prop('readonly',false);
$('#tax'+rowId).prop('readonly',false);
$('#taxAmount'+rowId).removeAttr("readonly"); 
$('#hsnCode'+rowId).removeAttr("readonly"); 
/*$('.btn-visibilty'+rowId).closest('td').find('.custom-combobox-toggle').removeClass('hide'); 
$('.btn-visibilty'+rowId).attr('disabled', false);
$('#combobox'+rowId).attr('disabled', false);
$('#comboboxsubProd'+rowId).attr('disabled', false);
$('#comboboxsubSubProd'+rowId).attr('disabled', false);*/
/*$('.btn-loop'+rowId).attr('disabled', false);*/
	$('#stQantity'+rowId).prop('readonly', false);
	$('#taxAmount'+rowId).prop('readonly', false);
	$('#Discount'+rowId).prop('readonly', false);
	/*$('#UnitsOfMeasurementId'+rowId).attr('disabled', false);*/
	$('#price'+rowId).prop('readonly', false);
	$('#BasicAmountId'+rowId).prop('readonly', false);
$('#transporterNameId'+rowId).prop('readonly',false);
$('#InvoiceNumberId'+rowId).prop('readonly',false);
$('#InvoiceDateId'+rowId).prop('readonly',false);
$('#state').prop('readonly',false);
$('#InvoiceDateId').prop('readonly',false);
$('#eWayBillNoId').prop('readonly',false);
$("#addsave").show();

}

function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var Selectedtax= parseFloat(tax);
	var taxAmnt = (basicAmnt/100) * Selectedtax;
	document.getElementById("TaxAmount"+rowNum).value = parseFloat(taxAmnt).toFixed(2);
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("TaxAftertotalAmount"+rowNum).value = parseFloat(amntaftTx).toFixed(2);
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

	//validating product table written by thirupathi
    var valProductTable=validateRowData();
	if(valProductTable==false){
		return false;
	}
	//validating charges table written by thirupathi
	var valChargesTable=validateChargesTableRows();
	if(valChargesTable==false){
		return false;
	}
	
	var valQty=validateQtyOnSubmit();
	if(valQty==false){
		return false;
	}
	var resetSno = getTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	 
	var resetSnoId = resetSpltdData.length - 1;
	var myValues= new Array();
	myValues = resetSpltdData;
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
		//Setting Total Amount - End
		
		//Setting Final Amount - Start
		fnlAmnt = parseFloat(fnlAmnt);
		fnlAmnt = fnlAmnt + ttlAmnt;
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
		//Setting Final Amount - End
	}
	//Setting Final Amount - Start
	fnlAmnt = Math.round(fnlAmnt * 100)/100;
	document.getElementById("finalAmntDiv").innerHTML = parseFloat(fnlAmnt).toFixed(2);
	 $("#ttlAmntForIncentEntryId").val(parseFloat(fnlAmnt).toFixed(2));
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
	var last_element = sortedSnos[sortedSnos.length - 1];
	return last_element;
}

//Field Validation started
function validateRowData() { debugger;
	  var tableRowsLength=$("#doInventoryTableId").find("tbody").find("tr").length;
	  var error=true;
	  $(".productTableRow").each(function(){
		  var id=$(this).attr("id").split("tr-class")[1];
		  var IsDeleted=$("#idDeletedOrNot"+id).val();
		  if(IsDeleted!="D"){
			  var prod=$("#Product"+id).val();
				if(prod == "" || prod == null || prod == '') {
					alert("Please enter product.");
					 $("#Product"+id).focus();
					return error=false;
				}
			  var subprod=$("#SubProduct"+id).val();
				if(subprod == "" || subprod == null || subprod == '') {
					alert("Please enter sub product.");
					 $("#SubProduct"+id).focus();
					return error=false;
				}
			  var child=$("#ChildProduct"+id).val();
				if(child == "" || child == null || child == '') {
					alert("Please enter child product.");
					 $("#ChildProduct"+id).focus();
					return error=false;
				}
			  var UOM=$("#UnitsOfMeasurementId"+id).val();
				if(UOM == "" || UOM == null || UOM == '') {
					alert("Please select UOM.");
				    $("#UnitsOfMeasurementId"+id).focus();
					return error=false;
				}
						
			  var Quan = $("#stQantity"+id).val();
				if(Quan == "" || Quan == null || Quan == '') {
					alert("Please enter qunatity.");
					 $("#stQantity"+id).focus();
					return error=false;
				}
			  var Amt =$("#price"+id).val();
				if(Amt == "" || Amt == null || Amt == '') {
					alert("Please enter the price.");
					$("#price"+id).focus();
					return error=false;
				}	  
				  
			  var disc =$("#Discount"+id).val();
				if(disc == "" || disc == null || disc == '') {
					alert("Please enter the discount.");
					$("#Discount"+id).focus();
					return error=false;
				}	
			  var TaxAmnt =$("#taxAmount"+id).val();
				if(TaxAmnt == "" || TaxAmnt == null || TaxAmnt == '') {
					alert("Please enter the Tax.");
					$("#taxAmount"+id).focus();
					return error=false;
				}
		  }
		  
	  })
	  return error;
}


function validateChargesTableRows(){
	var error=true;
	$(".chargesRow").each(function(){
		var id=$(this).attr("id").split("chargesrow")[1];
		var chargesDelete=$("#chargesDelete").val();
		//if(chargesDelete!="D"){
			var Conveyance=$("#Conveyance"+id).val();
			if(Conveyance==""){
				alert("Please select Conveyance.");
				$("#Conveyance"+id).focus();
				return error=false;
			}
			var ConveyanceAmount=$("#ConveyanceAmount"+id).val();
			if(ConveyanceAmount==""){
				alert("Please enter Conveyance Amount.");
				$("#ConveyanceAmount"+id).focus();
				return error=false;
			}
			var GSTTax=$("#GSTTax"+id).val();
			if(GSTTax==""){
				alert("Please select GST Tax.");
				$("#GSTTax"+id).focus();
				return error=false;
			}
		//}
	});
	
	return error;
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
		var groupId = $("#groupId"+currentRowNum).val();
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum,requestedDate,groupId);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum,requestedDate,groupId) {
	
	var url = "getProductAvailability2.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requestedDate+"&groupId="+groupId;
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
		    	//document.getElementById("ProductAvailabilityId"+currentRowNum).value = resp;
		    	//$("#totalQuantity"+currentRowNum).val(available[1]);
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

//checking number number 
function isNumberCheck(el, evt) {
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
function getSelectionStart(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate();
		r.moveEnd('character', o.value.length);
		if (r.text == '') return o.value.length;
		return o.value.lastIndexOf(r.text);
	} else return o.selectionStart;
}	
	/***********************************************check the BOQ QUANTITY START***********************************************/
function validatePOBOQQuantity(id){debugger;
if($("#stQantity"+id).val()=="" || $("#stQantity"+id).val()=="0"){
	return false;
}
$(".loader-sumadhura").show();
var siteId=$("#siteId").val();
var childProductId=$("#comboboxsubSubProd"+id).val().split("$")[0];
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
	$(".productTableRow").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		var currentGroupId=$("#groupId"+currentId).val();
		if(currentGroupId==groupId && $("#idDeletedOrNot"+currentId).val()!="D"){
			matchedEnteredQty+=parseFloat($("#stQantity"+currentId).val()==""?0:$("#stQantity"+currentId).val());
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
			$("#BOQQty"+id).val(BOQQTY);
			$("#avalBOQQty"+id).val(avalibleQty);
			avalibleQty=parseFloat(avalibleQty).toFixed(3);
			if(parseFloat(matchedEnteredQty)>parseFloat(avalibleQty) && groupId!='0'){
				alert("You can not initiate Child Product "+$("#comboboxsubSubProd"+id).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
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


function validateQtyOnSubmit(){
	var siteId=$("#siteId").val();
	var allSiteIds=$("#allSiteIds").val().split(",");
	var count=0;
	for (var i = 0; i < allSiteIds.length; i++) {
		if(allSiteIds[i]==siteId){
			count++;
		}
	}
	var error=true;
	if(count==0){
		$(".productTableRow").each(function(){
			var currentId=$(this).attr("id").split("tr-class")[1];
			if($("#idDeletedOrNot"+currentId).val()!="D"){
				var currentGroupId=$("#groupId"+currentId).val();
				//var avalBOQQty=$("#avalBOQQty"+currentId).val();
				var avalBOQQty=$("#avalBOQQty"+currentId).val();
				var BOQQTY=$("#BOQQty"+currentId).val();
				var matchedEnteredQty=0;
				$(".productTableRow").each(function(){
					var currentId1=$(this).attr("id").split("tr-class")[1];
					var currentGroupId1=$("#groupId"+currentId1).val();
					if(currentGroupId==currentGroupId1 && $("#idDeletedOrNot"+currentId1).val()!="D"){
						matchedEnteredQty+=parseFloat($("#stQantity"+currentId1).val()==""?0:$("#stQantity"+currentId1).val());
					}			
				});
				avalBOQQty=parseFloat(avalBOQQty).toFixed(3);
				if(parseFloat(matchedEnteredQty)>parseFloat(avalBOQQty) && currentGroupId!='0'){
					alert("You can not initiate Child Product"+$("#comboboxsubSubProd"+currentId).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+currentId).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
					$("#stQantity"+currentId).val('');
					$("#stQantity"+currentId).focus();		
					return error=false;
				}
			}			
		})
	}else{
		
	}
return error;
}

