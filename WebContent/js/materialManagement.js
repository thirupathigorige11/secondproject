

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
	    
		var valStatus = validateRowData();
		
		if(valStatus == false) {
		    	return false;
		 }
		
		var tbllength=$('#indentIssueTableId').find('tr').length;
		/*alert(tbllength);*/
		if(tbllength==2){
			var tid=$('#indentIssueTableId tr:last').attr('id');
			var res = tid.split("productRow")[1];
			$("#addDeleteItemBtnId"+res).show();
		}
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
	    	
	    	var	row = tbl.insertRow(tbl.rows.length);
		
	    	var i;
	    
	    	var tableColumnName = "";
	    	var columnToBeFocused = "";
	    	
	    	var rowNum = getLastRowNum();
	    	
	    	$("#addNewItemBtnId"+rowNum).hide();
	    	
	    	rowNum = rowNum+1;
	    	$(row).attr("id", "productRow"+rowNum);
	    	$(row).attr("class", "productRowCls");
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
	    
	    	$("#Product"+lastDiv).focus();
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
	var canIDelete=window.confirm("Do you want to delete?");
	if(canIDelete==false){
		return false;
	}
	var rowscount=$('#indentIssueTableId').find('tr').length;
	//removing row
	
	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}
   $("#productRow"+currentRow).remove();
	
	var tid=$('#indentIssueTableId tr:last').attr('id');	
	var res = tid.split("productRow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
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
	
	
	
	var requesteddate=$("#ReqDateId").val();
	if(requesteddate == "" || requesteddate == '' || requesteddate == null) {
		alert("Please Choose Requested Date .");
		document.getElementById("requesteddate").focus();
	//alert(requestedDate);
	}
	
	//alert("hai");
	
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
/****** Method for to call Duplicate Slip Numbers*************/

function validateSlipNumbers(){
	
	 var ReqDate=$("#ReqDateId").val();
	 var siteid=$("#ProjectNameId").val();
	 var slipnumber=$("#SlipNumberId").val();
	 
	 var url = "getIssueCount.spring?slipNumber="+slipnumber+"&receiveDate="+ReqDate+"&SiteId="+siteid;
	 $.ajax({
	  url : url,
	  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "post",
	  contentType : "application/json",
	  success : function(data) {
		 	 if (data>0){
				 //server sent response as false, not valid data
				$("#errorMessageInvoiceNumber").show();
				 $("#SlipNumberId").focus();
				
				 
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

/********** Method for load the Contractors**************/
function populateData() {
	debugger;
var contName=$("#ContractorNameId").val();
 var url = "loadAndSetContractorInfo.spring?contractorName="+contName;

  $.ajax({
  url : url,
  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
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
  		$("#ContractorNameId").autocomplete({
	  		source : resultArray,
	  		change: function (event, ui) {
                if(!ui.item){
               	//if list item not selected then make the text box null	
               	 $("#ContractorNameId").val("");
                }
              },
	  		select: function (event, ui) {
                AutoCompleteSelectHandler(event, ui);
            }

	  	});
  },
  error:  function(data, status, er){
	 // alert(data+"_"+status+"_"+er);
	  }
  });

//code for selected text
	function AutoCompleteSelectHandler(event, ui)
	{               
	    var selectedObj = ui.item;       
	    isTextSelect="true";
	  
	  var contractorName=selectedObj.value;
	 
		 var url = "loadAndSetVendorInfoForWO.spring";
		 $.ajax({
			  url : url,
			  type : "get",
			 data:{
				 contractorName:contractorName,
				 loadcontractorData:true	 
			 },
			  contentType : "application/json",
			  success : function(data) {
				  $("#contractorName").val(contractorName);
				  if(data!=""||data!="null"){
					  debugger;
					var contractorData=data[0].split("@@");
					var contractorId=contractorData[0];
					$("#VendorAddress").val(contractorData[2]);
					$("#contractorId").val(contractorId);
					var str="<option value='0'>Select Work Order No</option>";
					var siteid=$("#SiteId").val();
					$.ajax({
						url:"getWorkOrderNo.spring?workDescId=0&siteId="+siteid+"&contractorId="+contractorId+"&typeOfWork=PIECEWORK@@NMR",
						type : "POST",
						contentType : "application/json",
						success:function(resp){
							debugger;	
							var array=new Array();
							array=resp.split("|");
							for (var i = 0; i < array.length; i++) {
								debugger;
								var array_element = array[i].split("_");
								if(array_element!="")
								str+="<option value="+array_element[0]+">"+array_element[0]+"-"+array_element[3]+"</option>";
								
							}
							$("#workOrderNo").html(str);
						
						}
					});
					}
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
		 var available = new Array();
	    	

		 
	}

	
	function loadWorkOrderNo(){
		 //load work order no
		var available = new Array();
	    	
		 var url = "getWorkOrderNo.spring?workDescId=0&siteId="+$("#SiteId").val()+"&contractorId="+contractorId;
		 $.ajax({
			  url : url,
			  type : "get",
			
			  contentType : "application/json",
			  success : function(data) {
				  debugger;
				  for(var j=0; j<data.length; j++) {
			    		available[j] = data[j];
			    	}
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
	}

};
/*function setContractorData(cName) {
	
	var url = "loadAndSetContractorInfo.spring?contractorName="+cName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setContrcData;
		request.open("get", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}
function setContrcData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		//alert(resp);
		var vendorId = resp.split("@@")[0];
		$("#ContractorNameId").val(vendorId);
	}
}
*/
/********** Method for to load the Employee Details****************/

function populateEmployee() {
var empName=$("#RequesterNameId").val();

 var url = "loadAndSetEmployerInfo.spring?employeeName="+empName;

  $.ajax({
  url : url,
  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
  type : "get",
  Edata : "",
  contentType : "application/json",
  success : function(data) {
	  console.log(data);
	  Edata = data.split("@@");
	  
	  var resultArray = [];
	  for(var i=0;i<Edata.length;i++){
	      resultArray.push(Edata[i].split("@@")[0]);
	  }
  		$("#RequesterNameId").autocomplete({
	  		source : resultArray
	  	});
  },
  error:  function(data, status, er){
	  alert(data+"_"+status+"_"+er);
	  }
  });


  $('#RequesterNameId').on('change', function(){
		var value = $(this).val();
		 	
		/*value = value.replace("&", "$$$");*/
		//alert(value);
		
		setEmpData (value); //pass the value as paramter
 });
};

function populateRequesterId() {
	 $('#RequesterNameId').on('change', function() {
	 var empname=$("#RequesterNameId").val();
	 // alert(empname);	
	 $.ajax({
		 url: "./getEmployerid.spring?employeeName="+empname,
		 type: 'GET',
		 data:"",
		 success : function(data) {
			 $("#RequesterIdId").val(data);
		 },
		 error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
	  	});
}

function populateEmployeeid(){
	 $('#RequesterIdId').on('change', function() {
	 var empid=$("#RequesterIdId").val();
	 // alert(empid);	
	 $.ajax({
		 url: "./getEmployerName.spring?employeeid="+empid,
		 type: 'GET',
		 data:"",
		 success : function(data) {
			 $("#RequesterNameId").val(data);
		 },
		 error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
	  	});	
}

/*function setEmpData(eName) {
	
	var url = "loadAndSetEmployerInfo.spring?employeeName="+eName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setEmployeeData;
		request.open("GET", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setEmployeeData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		//alert(resp);
		var vendorId = resp.split("@@")[0];
		$("#RequesterNameId").val(vendorId);
	}
}*/

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
		var requesteddate=$("#ReqDateId").val();
		
		productAvailability(pro, subPro, childPro, measurementId,requesteddate,currentRowNum);
		
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

function productAvailability(mainProdId, subProdId, childProdId, measurementId,requesteddate,currentRowNum) {
	debugger;
	var site_id=$("#SiteId").val();
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requesteddate+"&isMaterialManagement=yes&site_id="+site_id;
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

function populateFloor(blockObj) {
	
	var blockId = blockObj.id;
	//alert(blockId);
	
	var selectedBlock = document.getElementById(blockId);
	selectedBlock = selectedBlock.options[selectedBlock.selectedIndex].value;
	
	//alert(selectedBlock);
	
	if(selectedBlock == "" || selectedBlock == '' || selectedBlock == null) {
		
		document.getElementById("FloorId").value = "";
		document.getElementById("FloorId").options.length = 1;
		
		document.getElementById("FlatNumberId").value = "";
		document.getElementById("FlatNumberId").options.length = 1;
	}
	else {
		var selectedBlockId = selectedBlock.split("$")[0];		
		//alert("Block Id = "+selectedBlockId);		
		//var selectedBlockName = selectedBlock.split("$")[1];
		//alert("Block Name = "+selectedBlockName);
		getFloorDetails(selectedBlockId);		
	}
}

function getFloorDetails(blockId) {
	
	var url = "getFloorDetails.spring?blockId="+blockId;
	//alert(url);
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
                
				var resp = request.responseText;
				resp = resp.trim();
				
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	
		    	var selectBox = document.getElementById("FloorId");
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById("FloorId") != null && document.getElementById("FloorId").options.length > 0) {
			    	document.getElementById("FloorId").options.length = 0;
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
			    	data = available[i].split("@@");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {
			    		var floorIdAndName = data[0]+"$"+data[1];
			    		defaultOption.text = data[1];
			    	    defaultOption.value = floorIdAndName;
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

function populateFlat(floorObj) {
	
	var floorId = floorObj.id;
	//alert(blockId);
	
	var selectedFloor = document.getElementById(floorId);
	selectedFloor = selectedFloor.options[selectedFloor.selectedIndex].value;
	
	//alert(selectedFloor);
	
	if(selectedFloor == "" || selectedFloor == '' || selectedFloor == null) {
		document.getElementById("FlatNumberId").value = "";
		document.getElementById("FlatNumberId").options.length = 1;
	}
	else {
		var selectedFloorId = selectedFloor.split("$")[0];		
		//alert("Floor Id = "+selectedFloorId);		
		//var selectedFloorName = selectedFloor.split("$")[1];
		//alert("Floor Name = "+selectedFloorName);
		getFlatDetails(selectedFloorId);		
	}
}

function getFlatDetails(floorId) {
	
	var url = "getFlatDetails.spring?floorId="+floorId;
	//alert(url);
	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {

				var resp = request.responseText;
				resp = resp.trim();
				
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	
		    	var selectBox = document.getElementById("FlatNumberId");
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById("FlatNumberId") != null && document.getElementById("FlatNumberId").options.length > 0) {
			    	document.getElementById("FlatNumberId").options.length = 0;
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
			    	data = available[i].split("@@");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {
			    		var flatIdAndName = data[0]+"$"+data[1];	    		
			    		defaultOption.text = data[1];
			    	    defaultOption.value = flatIdAndName;
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

function validateUnitsAndAvailability(qtyObj) {
	
	var qtyObjectId = qtyObj.id;
	
	var currentRowNum = qtyObjectId.match(/\d+/g);
	//alert("Row Number In Quantity Field = "+currentRowNum);
	
	var pro = $("#combobox"+currentRowNum).val();	
	pro = pro.split("$")[0];
	//alert(pro);
	
	var subPro = $("#comboboxsubProd"+currentRowNum).val();
	subPro = subPro.split("$")[0];
	//alert(subPro);
	
	var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
	childPro = childPro.split("$")[0];
	//alert(childPro);	
	var requesteddate=$("#ReqDateId").val();
	
	
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
	// this is for expiry date che
	ExpiryDate(pro, subPro, childPro,"",requesteddate,qty,currentRowNum);
	
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
/*==========================================expiry date purpose start=======================================
*/


function ExpiryDate(mainProdId, subProdId, childProdId,siteId,requesteddate,issuedQuantity, currentRowNum) {
	
	var url ="checkExpiryDate.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&siteId="+siteId+"&requesteddate="+requesteddate+"&Quantity="+issuedQuantity;
	var request = getAjaxObject();
	var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
	childPro = childPro.split("$")[1];
	try {
		request.onreadystatechange = function() {
			if(request.readyState == 4 && request.status == 200) {
				var resp = request.responseText;
				resp = resp.trim();
				if(resp=="Failed") {
					alert(childPro+ " Product Expired");
					$("#QuantityId"+currentRowNum).val("0");
					return false;
				}else{
					return true;
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






/*=============================================expiry date end =============================================
*/



function saveRecords(saveBtnClicked) {
	debugger;
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	
	var vendorissuedTypeid=document.getElementById("vendorissuedTypeid").value;
	
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	

	
	var empName = document.getElementById("RequesterNameId").value;

	if(empName == "" || empName == '' || empName == null) {
		alert("Please select the employee name.");
		document.getElementById("empName").focus();
		return;
	}
	
	
	if(vendorissuedTypeid=="OUTR"){
		var conId = document.getElementById("contractorId").value;
		if(conId == "" || conId == '' || conId == null) {
			alert("Please select valid contractor name.");
			document.getElementById("ContractorNameId").focus();
			return;
		}	
	}else{
		
	}
	
	
	var workOrderNo = document.getElementById("workOrderNo").value;
	var SiteId=  document.getElementById("SiteId").value;
	var makeWorkOrderNoMandatory=document.getElementById("makeWorkOrderNoMandatory").value;
/*	var index = makeWorkOrderNoMandatory.search(",");
	var flag=true;
	if (index >= 0) {
		var siteIdMadatory = makeWorkOrderNoMandatory.split(",");
			for (var ind = 0; ind < siteIdMadatory.length; ind++) {
				if(SiteId==siteIdMadatory[ind]){
				if(workOrderNo == "" || workOrderNo == '' || workOrderNo == null|| workOrderNo == "0") {
					alert("Please select work order no.");
					document.getElementById("workOrderNo").focus();
					flag=false;
					break;
				}	
			}
		}
	}else{
		if(SiteId==makeWorkOrderNoMandatory){
			if(workOrderNo == "" || workOrderNo == '' || workOrderNo == null|| workOrderNo == "0") {
				alert("Please select work order no.");
				document.getElementById("workOrderNo").focus();
				flag=false;
			}	
		}
	}
	if(flag == false) {
    	return;
	}*/
	
	

	
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