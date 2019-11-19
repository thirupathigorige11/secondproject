

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

function loadSubProds(prodId, rowNum) {
	
	prodId = prodId.split("$")[0];
	
	var url = "indentIssueSubProducts.spring?mainProductId="+prodId;
	
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                
				$("#SubProduct"+rowNum).val("");
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");
				
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
	//alert("Sub Product Id After Split = "+subProdId);
	
	var url = "indentIssueChildProducts.spring?subProductId="+subProdId;
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();			

		    	var spltData = resp.split("|");
		    	//alert(spltData);
		    	
		    	available = new Array();combobox1
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

function loadUnits(childProdId, rowNum) {
	
	childProdId = childProdId.split("$")[0];
	
	var url = "listUnitsOfSubProducts.spring?childProductId="+childProdId;
	  
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
				//alert(available);
				
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

function saveRecords(saveBtnClicked) {
	
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
	document.getElementById("countOfRows").value = getAllProdsCount();	
	document.getElementById("indentIssueFormId").action = "doIndentIssue.spring";
	document.getElementById("indentIssueFormId").method = "POST";
	document.getElementById("indentIssueFormId").submit();
}

function editRow(rowId){
	$("#addsave").show();
	//loadSubProds1(prodId, rowNum);
	 var subProd= $("#comboboxsubProd"+rowId).val();
	// alert(subProd);
		var childProdct= $("#comboboxsubSubProd"+rowId).val();
		//alert(childProdct);
		var Uom= $("#UnitsOfMeasurementId"+rowId).val();
	$.each($('.btn-visibilty'+rowId),function(index,row){
		$(this).closest('td').find('input').removeAttr('disabled');
		$(this).closest('td').find('input').removeAttr('readonly');
		$(this).closest('td').find('.custom-combobox-toggle').removeClass('hide');
		
		productId = $('#combobox'+rowId).val();
		//alert(productId);
		
		
		SubproductId = $('#SubProduct'+rowId).val();
		//alert(SubproductId);
		//loadSubSubProducts(SubproductId,rowId);
		
		ChildproductId = $('#ChildProduct'+rowId).val();
		//alert(ChildproductId);
		//loadUnits(ChildproductId,rowId);
	});
	
	loadSubProds(productId,rowId);
	
	//alert(subProd);
	//var xyz= $('#SubProduct'+rowId).closest('td').find('select');
	//var strUser = xyz.options[xyz.selectedIndex].value;
	//alert(xyz);
	var strUser= subProd.split("_");
	$("#SubProduct"+rowId).val(strUser[1]);
	loadSubSubProducts(strUser[0],rowId);
	var strUser= childProdct.split("_");
	$("#ChildProduct"+rowId).val(strUser[1]);
	loadUnits(strUser[0],rowId);
	//loadUnits(ChildproductId,rowId);
	//  $("#ChildProduct"+rowId).val(childProdct);
	 // $("#UnitsOfMeasurementId"+rowId).val(Uom);
	//  $("#SubProduct"+rowId).val(subProd);
}

function saveRow(rowNum){
	debugger;
	var prd = "Product"+rowNum;
	var subProd = "SubProduct"+rowNum;
	var ChildP = "ChildProduct"+rowNum;
	var UOm = "UnitsOfMeasurementId"+rowNum;
	var Qnty = "QuantityId"+rowNum;
	
	var proDuct = document.getElementById(prd).value;
	//alert(Product);

	if(proDuct == "" || proDuct == null || proDuct == '') {
		//document.getElementById(qty).focus();
		document.getElementById(prd).removeEventListener("blur", "");
		return false;
	}
/*	if(proDuct == 0 || proDuct == 0.0 || proDuct == 0.00 || proDuct == '0' || proDuct == '0.0' || proDuct == '0.00' || proDuct == "0" || proDuct == "0.0" || proDuct == "0.00") {
		alert("Please enter product.");
		document.getElementById(prd).value = "";
		document.getElementById(prd).focus();
		return false;
	}*/
	
	var SuBproDuct = document.getElementById(subProd).value;
	//alert(SubProduct);

	if(SuBproDuct == "" || SuBproDuct == null || SuBproDuct == '') {
		//document.getElementById(qty).focus();
		document.getElementById(subProd).removeEventListener("blur", "");
		return false;
	}
/*	if(SuBproDuct == 0 || SuBproDuct == 0.0 || SuBproDuct == 0.00 || SuBproDuct == '0' || SuBproDuct == '0.0' || SuBproDuct == '0.00' || SuBproDuct == "0" || SuBproDuct == "0.0" || SuBproDuct == "0.00") {
		alert("Please enter sub product.");
		document.getElementById(subProd).value = "";
		document.getElementById(subProd).focus();
		return false;
		
	}*/
	var chilDproDuct = document.getElementById(ChildP).value;
	var UOm = document.getElementById(UOm).value;
	var Qunty = document.getElementById(Qnty).value;
	
	var canISubmit = window.confirm("Do you want to Update?");

	if(canISubmit == false) {
		return false;
		
	}
	var requestData = {
	 prodId: proDuct,
	 subName: SuBproDuct,
	 ChildP: chilDproDuct,
	 uom: UOm,
	 qnty: Qunty
}
	 var url = "getReceiveCount.spring?proDuct="+prodId+"&SuBproDuct="+subName+"&chilDproDuct="+ChildP+"&UOm="+uom+"&Qunty="+qnty;
	 $.ajax({
	  url : url,
	  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "post",
	  contentType : "application/json",
	  success : function(data) {
		 	 if (data>0){
			alert("Saved successfully")
			 }
	  },
	  error:  function(vName, status, er){
		  alert(data+"_"+status+"_"+er);
		  }
	  });
}