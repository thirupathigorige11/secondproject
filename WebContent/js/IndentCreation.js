

//append row to the HTML table
function appendRow(btn) {
	var tbllength=$('#indentIssueTableId').find('tr').length;
	/*alert(tbllength);*/
	if(tbllength==2){
		var tid=$('#indentIssueTableId tr:last').attr('id');
		var res = tid.split("chargesrow")[1];
		$("#addDeleteItemBtnId"+res).show();
	}
	
	
	/*alert("btn: "+btn);*/
	var productId=$("#Product"+btn).val();
	var subproductId=$("#SubProduct"+btn).val();
	var childproductId=$("#ChildProduct"+btn).val();
	if(productId==""){
		alert("Please select Product.");
		$("#Product"+btn).focus();
		return false;
	}
	if(subproductId==""){
		alert("Please select SubProduct.");
		$("#SubProduct"+btn).focus();
		return false;
	}
	if(childproductId==""){
		alert("Please select ChildProduct.");
		$("#ChildProduct"+btn).focus();
		return false;
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
	
	    var tbl = document.getElementById("indentIssueTableId");
		   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
	    	
	    	var	row = tbl.insertRow(tbl.rows.length);		
	    	var i;	    
	    	var tableColumnName = "";
	    	var columnToBeFocused = "";	    	
	    	var rowNum = getLastRowNum();	    	
	    	$("#addNewItemBtnId"+rowNum).hide();
	    	
	    	rowNum = rowNum+1;
	    	var rowid="chargesrow"+rowNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "producttable");
	    	for (i = 0; i < tbl.rows[0].cells.length; i++) {
	    	
	    	var x = document.getElementById("indentIssueTableId").rows[0].cells;
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
	var CanIDelete=window.confirm("Do you want to delete?");
	if(CanIDelete==false){
		return false;
	}
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#indentIssueTableId').find('tr').length;
	
	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}
   $("#chargesrow"+currentRow).remove();
	
	var tid=$('#indentIssueTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
	}	
	//calculateOtherCharges();
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

function loadUnits(childProdId, rowNum) {
	debugger;
	if(childProdId==""){
		$("#UnitsOfMeasurementId"+rowNum).html("<option value=''>--Select--</option>");
		return false;
	}
	var childproduct=childProdId.split("$")[1];
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
		
		var subPro = $("#comboboxsubProd"+currentRowNum).val();
		subPro = subPro.split("$")[0];
		
		var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		
		var groupId = $("#groupId"+currentRowNum).val();
		
		productAvailability(pro, subPro, childPro, measurementId, currentRowNum, groupId);
		
		var qty = document.getElementById("QuantityId"+currentRowNum).value;
		qty = parseFloat(qty);
		
		var productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
		
		productAva = parseFloat(productAva);
		
		if(productAva <= 0) {
			//alert("This product is not available.");
			document.getElementById("QuantityId"+currentRowNum).value = "";		
			return false;
		}
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum, groupId) {
	
	var url = "getProductAvailability2.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&groupId="+groupId+"&isReceive=false";
	debugger;
	var request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {

				var resp = request.responseText;
				resp = resp.trim();
				
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					resp = "0";
				}	
				debugger;
				var spltData = resp.split("_");				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	$("#ProductAvailabilityId"+currentRowNum).val(available[0]);
		    	//$("#sumofrecieveQty"+currentRowNum).val(available[1]);
	    		//$("#sumofIssueQty"+currentRowNum).val(available[2]);
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

function validateUnitsAndAvailability(qtyObj) {
	debugger;
	var qtyObjectId = qtyObj.id;	
	var currentRowNum = qtyObjectId.match(/\d+/g);	
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
			var id=$(this).attr("id").split("chargesrow")[1];
			var currentGroupId=$("#groupId"+id).val();
			if(currentGroupId==groupId){
				matchedEnteredQty+=parseFloat($("#QuantityId"+id).val()==""?0:$("#QuantityId"+id).val());
			}			
		})
		debugger;
		matchedEnteredQty=matchedEnteredQty.toFixed(3);
		if(parseFloat(matchedEnteredQty)>parseFloat(totalQty)){
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
//checking product,subproduct and childproduct on submit
function validateProductData(){
	$(".ui-autocomplete-input").each(function(){
		debugger;
		var currentElement=$(this).val();
		var currentElementId=$(this).attr("id");
		
		if(currentElement==""){
			alert("Please select the required Data.");
			$("#"+currentElementId).focus();			
			return rv = false;
		}
		 else{			  
			  return rv = true;			  
		  }
		
	});
	
	return rv;
}
function validateMeasurmentandQtyData(){
	$(".measermentandqty").each(function(){
		debugger;
		var currentElement=$(this).val();
		var currentElementId=$(this).attr("id");
		
		if(currentElement==""){
			alert("Please enter the required Data.");
			$("#"+currentElementId).focus();			
			return rv = false;
		}
		 else{			  
			  return rv = true;			  
		  }
		
	});
	  return rv;
}

function saveRecords(saveBtnClicked, rowId) {debugger;

	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	var ScheduleDate= $("#ScheduleDateId").val();	
	if(ScheduleDate == "" || ScheduleDate == null || ScheduleDate == '') {
		alert("Please enter Schedule Date.");
		$("#ScheduleDateId").focus();
		return false;
	}				
	  
	var RequiredDate= $("#RequiredDateId").val();
	
	if(RequiredDate == "" || RequiredDate == null || RequiredDate == '') {
		alert("Please enter Required Date.");
		$("#RequiredDateId").focus();
		return false;
	}	
	//checking product,subproduct and childproduct 
	var validateProduct=validateProductData();
	if(validateProduct==false){
		return false;
	}
	//checking measurement  and Quantity 	
	var validateMeasurmentandQty=validateMeasurmentandQtyData();
	if(validateMeasurmentandQty==false){
		return false;
	}
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	
	
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		return;
	}
	$('.loader-sumadhura').show();
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("materialRowsId").value = getAllProdsCount();
	document.getElementById("indentCreationFormId").action = "submitIndentCreation.spring";
	document.getElementById("indentCreationFormId").method = "POST";
	document.getElementById("indentCreationFormId").submit();
}

function childcampare(childname, rowNum){
	$("#ChildProduct"+rowNum).val("");
	var tablelength=$("#indentIssueTableId > tbody > tr").length;	
	if(tablelength>1){
		debugger;
		jQuery('.ChildProductInput').each(function() {
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