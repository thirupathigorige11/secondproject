//append row to the HTML table
function appendRow() {
	
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
	 /*   
	    var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}*/
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
	    	
	    	var	row = tbl.insertRow(tbl.rows.length);
		
	    	var i;
	    
	    	var tableColumnName = "";
	    	var columnToBeFocused = "";
	    	
	    	var rowNum = getLastRowNum();
	    	
	    	document.getElementById("addNewItemBtnId"+rowNum).remove();
	    	
	    	rowNum = rowNum+1;
	    	
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
    	
function deleteRow(btn, currentRow) {
	
	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	
	if(document.getElementById("addNewItemBtnId"+currentRow) != null && document.getElementById("addDeleteItemBtnId"+currentRow) != null) {
		alert("This row can not be deleted.");
	}
	else {
		var row = btn.parentNode.parentNode;
		row.parentNode.removeChild(row);
	}
}


function getAllProdsCount() {
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {				
				//Reading last number from input element id's
				var numberPattern = /\d+/g;
				var snosid = ask.match(numberPattern);
				//alert(snosid);
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}








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

function formatColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}







function validateUnitsAndAvailability(qtyObj) {
	
	var qtyObjectId = qtyObj.id;
	
	var currentRowNum = qtyObjectId.match(/\d+/g);
	//alert("Row Number In Quantity Field = "+currentRowNum);
	
	var qty = "";
	var productAva = "";
	
	//if(document.getElementById("QuantityId"+currentRowNum) != null) {
	qty = document.getElementById("QuantityId"+currentRowNum).value;
	qty = parseFloat(qty);
	//alert(qty);
	
	if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
		alert("Please enter valid quantity.");
		document.getElementById("QuantityId"+currentRowNum).value = "";
		document.getElementById("QuantityId"+currentRowNum).focus();
		return false;
	}
	//}
	//if(document.getElementById("ProductAvailabilityId"+currentRowNum) != null) {
	productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
	productAva = parseFloat(productAva);
	/*if(productAva != "" && productAva != '' && productAva != null) {
		productAva = productAva.split(" ")[0];
		productAva = parseFloat(productAva);
	}*/
	//alert(productAva);
	//}
	
	//alert("productAva = "+productAva);
	
	if(productAva <= 0) {
		//alert("This product is not available.");
		document.getElementById("QuantityId"+currentRowNum).value = "";		
		return false;
	}
	
	if(qty != "" && productAva != "") {
		if(qty > productAva) {
			alert("Quantity should not be greatter than availability.");			
			document.getElementById("QuantityId"+currentRowNum).value = "";
			document.getElementById("QuantityId"+currentRowNum).focus();
			return false;
		}
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

function saveRecords(saveBtnClicked) {debugger;
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	
	
	document.getElementById("saveBtnId").disabled = true;	
	
	//document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("doInventoryFormId").action = "requestCentralIndentToOtherSite.spring";
//	alert("ok2");
	
	document.getElementById("doInventoryFormId").method = "POST";
	//alert("ok3");
	
	document.getElementById("doInventoryFormId").submit();
}
function validatePendingQuantity(){
	debugger;
	var isThisIndentIsCorrectToSendNextLevel=false;
 
	$(".indentProductClass").each(function(){
		debugger;
		var currentId=$(this).attr("id").split("indentproductrow")[1];	
		var pendingQuantity=$("#PendingQty"+currentId).text()==""?0:$("#PendingQty"+currentId).text();
		var initiatedQuantity=$("#InitiatedQty"+currentId).text()==""?0:$("#InitiatedQty"+currentId).text();
		var quantity=pendingQuantity-initiatedQuantity;
		if(quantity>0&&isThisIndentIsCorrectToSendNextLevel!=true){
			isThisIndentIsCorrectToSendNextLevel=true;
			return true;
		}
	});

	if(isThisIndentIsCorrectToSendNextLevel==false){
	
		return false;
	}
	return true;
}
function sendToPD(saveBtnClicked) {debugger;

document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;

var valStatus =validatePendingQuantity();
if(valStatus == false) {
	alert("You can not sent this indent to Purchase Department, as already all product settlement is in progress.");
	return false;
}
valStatus = appendRow();

if(valStatus == false) {
	return;
}

var canISubmit = window.confirm("Do you want to Submit?");

if(canISubmit == false) {
	return;
}

document.getElementById("saveBtnId").disabled = true;	
$('#sendToPurBtn').attr("disabled","disabled");
document.getElementById("countOfRows").value = getAllProdsCount();	
document.getElementById("CentralIndentFormId").action = "sendCentralIndentToPD.spring";
document.getElementById("CentralIndentFormId").method = "POST";
document.getElementById("CentralIndentFormId").submit();
}

function cancelIndent(){
	debugger;
	var valStatus =validatePendingQuantity();
	if(valStatus == false) {
		alert("You can not reject this Indent, as already all product settlement is in progress.");
		return false;
	}
	$("#modalCentralIndent-reject").modal('show');
}

function reject(saveBtnClicked) {
	debugger;
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;

	
	 var valStatus = appendRow();
	
	if(valStatus == false) {
    	return false;
	}
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return false;
	}
	
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("CentralIndentFormId").action = "rejectIndentCreation.spring";
	document.getElementById("CentralIndentFormId").method = "POST";
	document.getElementById("CentralIndentFormId").submit();
}