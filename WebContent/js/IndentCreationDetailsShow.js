

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
    	
function deleteRow(btn, currentRow, rownum) {
	
	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	
	if(document.getElementById("addNewItemBtnId"+currentRow) != null && document.getElementById("addDeleteItemBtnId"+currentRow) != null) {
		alert("This row can not be deleted.");
	}
	else {
		var row = btn.parentNode.parentNode;
		//row.parentNode.removeChild(row);
		document.getElementById("isDelete"+rownum).value="d";
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

function loadSubProds(prodId, rowNum, action) {
	
	prodId = prodId.split("$")[0];
	var url = "indentIssueSubProducts.spring?mainProductId="+prodId;
	
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                if(action!='edit'){
                	$("#SubProduct"+rowNum).val("");
                	$("#ChildProduct"+rowNum).val("");
                	$("#UnitsOfMeasurement"+rowNum).val("");
                }
				
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
			  /*  initOpt.text = "--Select--";*/
			    initOpt.value = $("#actualSubProductId"+rowNum).val()+"$"+$("#SubProduct"+rowNum).val();
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

function loadSubSubProducts(subProdId, rowNum, action) {
	
	subProdId = subProdId.split("$")[0];
	//alert("Sub Product Id After Split = "+subProdId);
	
	var url = "indentIssueChildProducts.spring?subProductId="+subProdId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				if(action != 'edit') {
					$("#ChildProduct"+rowNum).val("");
					$("#UnitsOfMeasurement"+rowNum).val("");
					$("#RequiredQuantity"+rowNum).val("");
				}
				
				var resp = request.responseText;
				resp = resp.trim();			

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

function loadUnits(childProdId, rowNum, action) {
	
	childProdId = childProdId.split("$")[0];
	
	var url = "listUnitsOfSubProducts.spring?childProductId="+childProdId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                if(action!='edit'){
                	$("#UnitsOfMeasurement"+rowNum).val("");
                	$("#RequiredQuantity"+rowNum).val("");
                }
				
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
			/*    initOpt.text = "--Select--";*/
			    initOpt.value = $("#actualUnitsOfMeasurementId"+rowNum).val()+"$"+$("#UnitsOfMeasurement"+rowNum).val();
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
		
		var qty = document.getElementById("QuantityId"+currentRowNum).value;
		qty = parseFloat(qty);
		//alert(qty);
		
		var productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
		/*if(productAva != "" && productAva != '' && productAva != null) {
			productAva = productAva.split(" ")[0];
		}*/
		productAva = parseFloat(productAva);
		//alert(productAva);
		
		if(productAva <= 0) {
			//alert("This product is not available.");
			document.getElementById("QuantityId"+currentRowNum).value = "";		
			return false;
		}
		
		if(qty > productAva) {
			alert("Quantity should not be greatter than availability.");
			document.getElementById("QuantityId"+currentRowNum).focus();
			document.getElementById("QuantityId"+currentRowNum).value = "";
			return false;
		}
		else {
			return true;
		}
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId;
	//alert(url);
	  
	var request = getAjaxObject();

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

function removeRow(rowId){debugger;
var confirmCancel = confirm(" Do you want to remove the Product");
if(confirmCancel == false){
	return false;
}
/*document.getElementById("RemoveId").value = "";
*/
$("#tr-class"+rowId).addClass('strikeout');
$("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
$("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
$('#snoDivId'+rowId).removeAttr('id');	
$('#Product'+rowId).removeAttr('id');
$('#SubProduct'+rowId).removeAttr('id');
$('#ChildProduct'+rowId).removeAttr('id');
$('#UnitsOfMeasurement'+rowId).removeAttr('id');
$('#QuantityId'+rowId).removeAttr('id');
document.getElementById("isDelete"+rowId).value="d";
document.getElementById("actionValue"+rowId).value="R";




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
	/*document.getElementById("hiddenCommentsId").value = CommentsBtnClicked;*/
	
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	$(".loader-sumadhura").show();
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("indentCreationDetailsShowFormId").action = "approveIndentCreation.spring";
	document.getElementById("indentCreationDetailsShowFormId").method = "POST";
	document.getElementById("indentCreationDetailsShowFormId").submit();
}
function reject(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	$(".loader-sumadhura").show();
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("indentCreationDetailsShowFormId").action = "rejectIndentCreation.spring";
	document.getElementById("indentCreationDetailsShowFormId").method = "POST";
	document.getElementById("indentCreationDetailsShowFormId").submit();
}
function editRow(rowId){debugger;
	var CanIEdit=window.confirm("Do you want to Edit?");
	if(CanIEdit==false){
		return false;
	}
	$("#addsave").show();
	$("#Comments"+rowId).prop('readonly', false);
	$("#RequiredQuantity"+rowId).prop('readonly', false);
	$("#ProductAvailability"+rowId).prop('readonly', false);
	var subProd= $("#comboboxsubProd"+rowId).val();
	var childProdct= $("#comboboxsubSubProd"+rowId).val();
	var Uom= $("#UnitsOfMeasurementId"+rowId).val();
	$.each($('.btn-visibilty'+rowId),function(index,row){
		$(this).closest('td').find('input').removeAttr('disabled');
		$(this).closest('td').find('input').removeAttr('readonly');
		$(this).closest('td').find('.custom-combobox-toggle').removeClass('hide');		
		productId = $('#combobox'+rowId).val();
		SubproductId = $('#SubProduct'+rowId).val();
		ChildproductId = $('#ChildProduct'+rowId).val();
	});	
	loadSubProds(productId,rowId, 'edit');
	var strUser= subProd.split("_");
	loadSubSubProducts(strUser[0],rowId, 'edit');
	var strUser= childProdct.split("_");
	loadUnits(strUser[0],rowId, 'edit');
}

function validateBOQQuantity(id){debugger;
	if($("#RequiredQuantity"+id).val()=="" || $("#RequiredQuantity"+id).val()=="0"){
		return false;
	}
	$(".loader-sumadhura").show();
	var siteId=$("#siteId").val();
	var childProductId=$("#comboboxsubSubProd"+id).val().split("$")[0];
	var groupId=$("#groupId"+id).val();
	var indentNumber=$("#IndentNumberId").val();
	var allSiteIds=$("#allSiteIds").val().split(",");
	var count=0;
	for (var i = 0; i < allSiteIds.length; i++) {
		if(allSiteIds[i]==siteId){
			count++;
		}
	}
	if(count==0){
		var matchedEnteredQty=0;
		$(".producttable").each(function(){
			var currentId=$(this).attr("id").split("tr-class")[1];
			var currentGroupId=$("#groupId"+currentId).val();
			if(currentGroupId==groupId && $("#isDelete"+currentId).val()!="d"){
				matchedEnteredQty+=parseFloat($("#RequiredQuantity"+currentId).val()==""?0:$("#RequiredQuantity"+currentId).val())-parseFloat($("#Quantity"+currentId).val()==""?0:$("#Quantity"+currentId).val());
			}			
		})
		matchedEnteredQty=matchedEnteredQty.toFixed(3);
		var url="gettingBoqQuantityAjax.spring?childProductId="+childProductId+"&groupId="+groupId+"&siteId="+siteId;   //+"&indentPendingQuantity="+matchedEnteredQty+"&indentNumber="+indentNumber;
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
				if(parseFloat(matchedEnteredQty)>parseFloat(avalibleQty)){
					alert("You can not initiate Child Product "+$("#comboboxsubSubProd"+id).val().split("$")[1]+" more than "+BOQQTY+" "+$("#UnitsOfMeasurementId"+id).val().split("$")[1]+". As it is exceeding BOQ Quantity.");
					$("#RequiredQuantity"+id).val('');
					$("#RequiredQuantity"+id).focus();					
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
