function deleteRow(btn, currentRow) {
	var rowscount=$('#indentIssueTableId').find('tr').length;
	//removing row
	
	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}
   $("#indentIssueRow"+currentRow).remove();
	
	var tid=$('#indentIssueTableId tr:last').attr('id');	
	var res = tid.split("indentIssueRow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
	}	
}

//append row to the HTML table
function appendRow() {
	
	var tbllength=$('#indentIssueTableId').find('tr').length;
	/*alert(tbllength);*/
	if(tbllength==2){
		var tid=$('#indentIssueTableId tr:last').attr('id');
		var res = tid.split("indentIssueRow")[1];
		$("#addDeleteItemBtnId"+res).show();
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
	    
	    var valStatus = validateRowData();
	    //alert(valStatus);
	    
	    if(valStatus == false) {
	    	return false;
		}
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
	    	
	    	var	row = tbl.insertRow(tbl.rows.length);
		
	    	var i;
	    
	    	var tableColumnName = "";
	    	var columnToBeFocused = "";
	    	
	    	var rowNum = getLastRowNum();
	    	
	    	$("#addNewItemBtnId"+rowNum).hide();
	    	
	    	rowNum = rowNum+1;
	    	 var rowid="indentIssueRow"+rowNum;
	           
	    	$(row).attr("id", rowid);
	        $(row).attr("class", "indentIssueRows");
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
    	
/*function deleteRow(btn, currentRow) {
	
	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	
	if(document.getElementById("addNewItemBtnId"+currentRow) != null && document.getElementById("addDeleteItemBtnId"+currentRow) != null) {
		alert("This row can not be deleted.");
	}
	else {
		var row = btn.parentNode.parentNode;
		row.parentNode.removeChild(row);
	}
}*/

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
	}
	var woTypeOfWork=$("#woTypeOfWork").val();
	if(woTypeOfWork == "" || woTypeOfWork == '' || woTypeOfWork == null) {
		alert("Please select work order type.");
		$("#woTypeOfWork").focus();
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
				$("#WD"+rowNum).val("");
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
			    		document.getElementById("groupId"+rowNum).value = data[2];
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
					
					$("#contractorId").val(contractorId);
					var str="<option value=''>Select Work Order No</option>";
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
								var array_element = array[i].split("_");
								if(array_element!="")
								str+="<option value="+array_element[0]+"_"+array_element[4]+">"+array_element[0]+"-"+array_element[3]+"</option>";
								
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

function workOrderFunction(id,value){
debugger;
	if(value!=""&&value!="OTHER"){
		$("#workOrderNoDiv").show();
	}else{
		$("#workOrderNoDiv").hide();
	}
	
	$("#wdHead1").show();
	$("#areaHead1").show();
	
	$("#wdBody1").show();
	$("#areaBody1").show();
	
	$("#blockHead1").show();
	$("#floorHead1").show();
	$("#flatHead1").show();
	
	$("#blockBody1").show();
	$("#floorBody1").show();
	$("#flatBody1").show();
	$("#QuantityId1").attr("readonly",false);
	$("#QuantityId1").val("0");
	
	
	if(value=="MATERIAL"){
		$("#txtWorkOrderNo").show();
		$("#workOrderNo").hide();
		
		$("#contractorId").val("");
		
		$("#wdHead1").show();
		$("#areaHead1").show();
		
		$("#wdBody1").show();
		$("#areaBody1").show();
		
		$("#blockHead1").hide();
		$("#floorHead1").hide();
		$("#flatHead1").hide();
		
		$("#blockBody1").hide();
		$("#floorBody1").hide();
		$("#flatBody1").hide();
		$("#ContractorNameId").attr("readonly",true);
		$("#ContractorNameId").val("");
		$("#isWorkOrderExistsInSite").val(value);
	}else if(value=="OTHER"){
		$("#wdHead1").hide();
		$("#areaHead1").hide();
		
		$("#wdBody1").hide();
		$("#areaBody1").hide();
		
		$("#blockHead1").show();
		$("#floorHead1").show();
		$("#flatHead1").show();
		
		$("#blockBody1").show();
		$("#floorBody1").show();
		$("#flatBody1").show();
		$("#QuantityId1").attr("readonly",false);
		$("#ContractorNameId").attr("readonly",false);
		$("#QuantityId1").val("0");
		$("#isWorkOrderExistsInSite").val("LABOR");
	}else{
		$("#txtWorkOrderNo").hide();
		$("#workOrderNo").show();
		$("#ContractorNameId").attr("readonly",false);
		$("#isWorkOrderExistsInSite").val("");
	}
}

function populateWorkOrderNo(){
	
debugger;

//$("#txtWorkOrderNo").keyup(function (){		
	debugger;
	var siteid=$("#SiteId").val();
	var woTypeOfWork=$("#woTypeOfWork").val();
	var txtWorkOrderNo=$("#txtWorkOrderNo").val();
	$("#txtWorkOrderNo").autocomplete({
   		source : function(request, response) {
   			$.ajax({
   				url:"autoCompleteWorkOrderNo.spring",
   					  type : "GET",
   					  data : {siteId:siteid,typeOfWork:woTypeOfWork,workOrderNo:txtWorkOrderNo},
	   					dataType : "json",
   						success : function(data) {
   							response($.map(data, function (value, key) { 
	                          debugger;
   								return {
	                                label: value.WORK_ORDER_NUMBER,
	                                value: value.QS_WORKORDER_ISSUE_ID
	                            };
	                       }));
	    				}
	    			});
	    		},
		        change: function (event, ui) {
		               if(!ui.item){
			                    // The item selected from the menu, if any. Otherwise the property is null
			                    //so clear the item for force selection
		                   $("#txtWorkOrderNo").val("");
		                   $(".clearbtn").hide();
		                }
		            },
		            select: function(event, ui) {  
		                $("#QS_WORKORDER_ISSUE_ID").val(ui.item.value);                  
		                //$("#tempchildProdID").val(ui.item.label);
		                $("#txtWorkOrderNo").val(ui.item.label);
		                $("#workOrderNo").val(ui.item.label);
		                populateWD();
		                return false;  
		            }  
	   });
//});
}
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


  /*$('#RequesterNameId').on('change', function(){debugger;
		var value = $(this).val();
		 	
		value = value.replace("&", "$$$");
		//alert(value);
		
		setEmpData (value); //pass the value as paramter
 });*/
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
}*/
/*function setEmployeeData() {
	debugger;
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		//alert(resp);
		var vendorId = resp.split("@@")[0];
		alert(vendorId+" setEmployeeData");
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
				$("#WD"+rowNum).val("");
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
		//alert(pro);
		
		var subPro = $("#comboboxsubProd"+currentRowNum).val();
		subPro = subPro.split("$")[0];
		//alert(subPro);
		
		var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		//alert(childPro);	
		var requesteddate=$("#ReqDateId").val();
		var groupId = $("#groupId"+currentRowNum).val();
		productAvailability(pro, subPro, childPro, measurementId,requesteddate,currentRowNum,groupId);
		
		var qty =$("#QuantityId"+currentRowNum).val()==""?0:$("#QuantityId"+currentRowNum).val();
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

function productAvailability(mainProdId, subProdId, childProdId, measurementId,requesteddate,currentRowNum,groupId) {
	debugger;
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requesteddate+"&groupId="+groupId;
	//alert(url);
	  
	var request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {

				var resp = request.responseText;
				var spltData = resp.split("_")[0]; 
				resp = resp.trim();
				//alert(resp);
				//holding previous quantity in below var
				var holdSameProductInitiatedQty=0;
				var currentProdName=$("#combobox"+currentRowNum).val().split("$")[0];
				var currentSubProdName=$("#comboboxsubProd"+currentRowNum).val().split("$")[0];
				var currentChildProdName=$("#comboboxsubSubProd"+currentRowNum).val().split("$")[0];
				var currentUOM=$("#UnitsOfMeasurementId"+currentRowNum).val().split("$")[0];
				$(".indentIssueRows").each(function(){
					var currentId=$(this).attr("id").split("indentIssueRow")[1];
					if(currentId!=currentRowNum){
						var productName=$("#combobox"+currentId).val().split("$")[0];
						var subProductName=$("#comboboxsubProd"+currentId).val().split("$")[0];
						var childProductName=$("#comboboxsubSubProd"+currentId).val().split("$")[0];
						var UOM=$("#UnitsOfMeasurementId"+currentId).val().split("$")[0];	
				 		var quantity=parseFloat($("#QuantityId"+currentId).val());
						if(currentProdName==productName && subProductName==currentSubProdName && currentChildProdName==childProductName && currentUOM==UOM ){
							holdSameProductInitiatedQty+=quantity;
						}
					}
				});
				
				
				console.log(holdSameProductInitiatedQty);
				spltData=parseFloat(spltData)-holdSameProductInitiatedQty;
				
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					resp = "0";
				}		    	
		    	document.getElementById("ProductAvailabilityId"+currentRowNum).value =spltData.toFixed(2);
            }
        };		
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function populateFloor(blockObj,rowNum) {
	debugger;
	var blockId = blockObj.id;
	//alert(blockId);
	
	var selectedBlock = document.getElementById(blockId);
	selectedBlock = selectedBlock.options[selectedBlock.selectedIndex].value;
	
	//alert(selectedBlock);
	
	if(selectedBlock == "" || selectedBlock == '' || selectedBlock == null) {
		
		document.getElementById("FloorId"+rowNum).value = "";
		document.getElementById("FloorId"+rowNum).options.length = 1;
		
		document.getElementById("FlatNumberId"+rowNum).value = "";
		document.getElementById("FlatNumberId"+rowNum).options.length = 1;
	}
	else {
		var selectedBlockId = selectedBlock.split("$")[0];		
		//alert("Block Id = "+selectedBlockId);		
		//var selectedBlockName = selectedBlock.split("$")[1];
		//alert("Block Name = "+selectedBlockName);
		getFloorDetails(selectedBlockId,rowNum);		
	}
}

function getFloorDetails(blockId,rowNum) {
	debugger;
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
		    	
		    	var selectBox = document.getElementById("FloorId"+rowNum);
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById("FloorId"+rowNum) != null && document.getElementById("FloorId"+rowNum).options.length > 0) {
			    	document.getElementById("FloorId"+rowNum).options.length = 0;
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

function populateFlat(floorObj,rowNum) {
	debugger;
	var floorId = floorObj.id;
	//alert(blockId);
	
	var selectedFloor = document.getElementById(floorId);
	selectedFloor = selectedFloor.options[selectedFloor.selectedIndex].value;
	
	//alert(selectedFloor);
	
	if(selectedFloor == "" || selectedFloor == '' || selectedFloor == null) {
		document.getElementById("FlatNumberId"+rowNum).value = "";
		document.getElementById("FlatNumberId"+rowNum).options.length = 1;
	}
	else {
		var selectedFloorId = selectedFloor.split("$")[0];		
		//alert("Floor Id = "+selectedFloorId);		
		//var selectedFloorName = selectedFloor.split("$")[1];
		//alert("Floor Name = "+selectedFloorName);
		getFlatDetails(selectedFloorId,rowNum);		
	}
}

function getFlatDetails(floorId,rowNum) {
	
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
		    	
		    	var selectBox = document.getElementById("FlatNumberId"+rowNum);
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById("FlatNumberId"+rowNum) != null && document.getElementById("FlatNumberId"+rowNum).options.length > 0) {
			    	document.getElementById("FlatNumberId"+rowNum).options.length = 0;
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
	/*================================================ this is for workorder description purpose start===========================================*/
	// when the user select the work order number then wd getting 
var WDData;
function populateWD() {debugger;
	$(".workOrderIcon_indentIssue").show();
	var woTypeOfWork=$("#woTypeOfWork").val();
	var workOrderId="";
	if($('#workOrderNo').val()!=null){
		 workOrderId=$('#workOrderNo').val().split("_")[0];
	}
	
	if($('#workOrderNo').val()!=null&&$('#workOrderNo').val().split("_")[1]=="LABOR"){
		$("#wdHead1").hide();
		$("#areaHead1").hide();
		
		$("#wdBody1").hide();
		$("#areaBody1").hide();
		
		$("#blockHead1").show();
		$("#floorHead1").show();
		$("#flatHead1").show();
		
		$("#blockBody1").show();
		$("#floorBody1").show();
		$("#flatBody1").show();
		$("#QuantityId1").attr("readonly",false);
		$("#QuantityId1").val("0");
		$("#isWorkOrderExistsInSite").val("LABOR");
	}else{
		$("#wdHead1").show();
		$("#areaHead1").show();
		
		$("#wdBody1").show();
		$("#areaBody1").show();
		
		$("#blockHead1").hide();
		$("#floorHead1").hide();
		$("#flatHead1").hide();
		
		$("#blockBody1").hide();
		$("#floorBody1").hide();
		$("#flatBody1").hide();
		$("#QuantityId1").attr("readonly",true);
		$("#QuantityId1").val("0");
		if($('#workOrderNo').val()!=null){
			$("#isWorkOrderExistsInSite").val($('#workOrderNo').val().split("_")[1]);
		}else if(woTypeOfWork=="MATERIAL"){
			$("#isWorkOrderExistsInSite").val("MATERIAL");
		}
		
	}
	
	var SiteId=$("#SiteId").val();
	if(woTypeOfWork=="MATERIAL"){
		workOrderId=$('#txtWorkOrderNo').val()==null?"":$('#txtWorkOrderNo').val();
	}

	var url = "getWdDetails.spring?workOrderNumber="+workOrderId+"&siteId="+SiteId;
 
	var urlForLink="getMyCompletedWorkOrder.spring?WorkOrderNo="+workOrderId+"&workOrderIssueNo=&site_id="+SiteId+"&operType=1&isUpdateWOPage=false";
	$("#workOrderLink").attr("href",urlForLink);
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				debugger;
				var resp = request.responseText;
				resp = resp.trim();
				WDData=resp;
				var spltData = resp.split("|");
		    	//alert(spltData);
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
		    	$("#WD1").html("");
		    	var selectBox = document.getElementById("WD1");
			    //alert(selectBox);
			    
			    //Removing previous options from select box - Start
			    /*if(document.getElementById("FlatNumberId"+rowNum) != null && document.getElementById("FlatNumberId"+rowNum).options.length > 0) {
			    	document.getElementById("FlatNumberId"+rowNum).options.length = 0;
			    }*/
			    //Removing previous options from select box - End
			    var holdWDDataInsession="<option>--Select--</option>";
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
			    		var wdIdAndName = data[0]+"$"+data[1];	    		
			    		defaultOption.text = data[1];
			    		holdWDDataInsession+="<option value='"+wdIdAndName+"'>"+data[1]+"</option>";
			    	    defaultOption.value = wdIdAndName;
			    	    selectBox.appendChild(defaultOption);
			    	}
			    }
			    sessionStorage.setItem("WDData",holdWDDataInsession);
            }
        };		
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
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


//checking duplicate row in table
function checkingDuplicateRow(rowNum){
	debugger;
	var isWorkOrderExistsInSite=$("#isWorkOrderExistsInSite").val();
	var error="true";
	var currentProdName=$("#combobox"+rowNum).val().split("$")[0];
	var currentSubProdName=$("#comboboxsubProd"+rowNum).val().split("$")[0];
	var currentChildProdName=$("#comboboxsubSubProd"+rowNum).val().split("$")[0];
	var currentUOM=$("#UnitsOfMeasurementId"+rowNum).val().split("$")[0];
	var currentWD=$("#WD"+rowNum).val()==null?"":$("#WD"+rowNum).val().split("$")[0];
	
/*	var currentBlockId=$("#BlockId"+rowNum).val().split("$")[0];
	var currentFloorId=$("#FloorId"+rowNum).val().split("$")[0];
	var currentFlatNumberId=$("#FlatNumberId"+rowNum).val().split("$")[0];*/
	
	$(".indentIssueRows").each(function(){
		var currentId=$(this).attr("id").split("indentIssueRow")[1];
		if(currentId!=rowNum){
			var productName=$("#combobox"+currentId).val().split("$")[0];
			var subProductName=$("#comboboxsubProd"+currentId).val().split("$")[0];
			var childProductName=$("#comboboxsubSubProd"+currentId).val().split("$")[0];
			var UOM=$("#UnitsOfMeasurementId"+currentId).val().split("$")[0];	
			var WD=$("#WD"+currentId).val()==null?"":$("#WD"+currentId).val().split("$")[0];
			
			/*var blockId=$("#BlockId"+currentId).val().split("$")[0];
			var floorId=$("#FloorId"+currentId).val().split("$")[0];
			var flatNumberId=$("#FlatNumberId"+currentId).val().split("$")[0];*/
				if(currentProdName==productName && subProductName==currentSubProdName && currentChildProdName==childProductName && currentUOM==UOM&&currentWD==WD){
					alert("UOM and WD already exist, Please choose different UOM or WD.");
					return error="false";
				}
		}
	});

	return error;
	
}

function validateRowData1(rowNum){
	debugger;
	var error="true";
	var currentProdName=$("#combobox"+rowNum).val();
	var currentSubProdName=$("#comboboxsubProd"+rowNum).val();
	var currentChildProdName=$("#comboboxsubSubProd"+rowNum).val();
	var currentUOM=$("#UnitsOfMeasurementId"+rowNum).val();

	if(currentProdName==null||currentProdName==""){
		alert("Please select product.");
		$("#combobox"+rowNum).focus();
		return error="false";
	}
	
	if(currentSubProdName==null||currentSubProdName==""){
		alert("Please select sub product.");
		$("#comboboxsubProd"+rowNum).focus();
		return error="false";
	}
	
	if(currentChildProdName==null||currentChildProdName==""){
		alert("Please select child product.");
		$("#comboboxsubSubProd"+rowNum).focus();
		return error="false";
	}
	if(currentUOM==null||currentUOM==""){
		alert("Please select UOM.");
		$("#UnitsOfMeasurementId"+rowNum).focus()
		return error="false";
	}
	return error;
}

	/*============================================== this is for getting the block id and name start================================================*/
function populateBlockId(id) {debugger;
	var comboboxsubSubProd=$("#comboboxsubSubProd"+id).val();
	var comboboxsubProd1=$("#comboboxsubProd"+id).val();
	var comboboxsubSubProd1=$("#comboboxsubSubProd"+id).val();
	var UnitsOfMeasurementId1=$("#UnitsOfMeasurementId"+id).val();
	
	var checkduplicate=validateRowData1(id);
	   if(checkduplicate=="false"||checkduplicate==false){
	    	 $("#UnitsOfMeasurementId"+id).val('');
	    	 $("#WD"+id).val('');
	    	 return false;
	  }
	
	 checkduplicate=checkingDuplicateRow(id);
     if(checkduplicate=="false"){
    	 $("#UnitsOfMeasurementId"+id).val('');
    	 $("#WD"+id).val('');
    	 return false;
     }
	
    $("#modalBlockbody"+id).html("");
	$("#QuantityId"+id).val("");
	var wd=$('#WD'+id).val();
	var wdId=wd.split("$")[0];
	var materialGroupId=$('#groupId'+id).val();
	var workOrderNo=$("#workOrderNo").val()==null?$("#txtWorkOrderNo").val():$("#workOrderNo").val().split("_")[0];
	var woTypeOfWork=$("#woTypeOfWork").val();
	if(woTypeOfWork=="MATERIAL"){
		workOrderNo= $("#txtWorkOrderNo").val();
	}
	if(wd.length==0||materialGroupId.length==0){
		return false;
	}
	
	  $(".overlay_ims").show();	
	  $(".loader-ims").show();
	
	var materialBlockModelData="";
	
	materialBlockModelData+='<div class="modal" id="blockModal'+id+'">';
	materialBlockModelData+='<div class="modal-dialog modal-lg" style="width:90%;">';
	materialBlockModelData+='<div class="modal-content">';
	
	materialBlockModelData+='<!-- Modal Header -->';
	materialBlockModelData+='<div class="modal-header">';
	materialBlockModelData+='<h4 class="modal-title">Site Details</h4>';
	materialBlockModelData+='<button type="button" class="close" data-dismiss="modal">&times;</button>';
	materialBlockModelData+='</div>';
	materialBlockModelData+='<div class="modal-body" style="height:400px;overflow-y: scroll;">';
	materialBlockModelData+='<div  id="modalBlockbody'+id+'">';
	      
	materialBlockModelData+='</div>';						       
	materialBlockModelData+='</div>';
	materialBlockModelData+='<div class="modal-footer">';
	materialBlockModelData+='<div class="col-md-12 text-center center-block">';
	materialBlockModelData+='<button type="button" class="btn btn-warning" data-dismiss="modal" onclick="submitMaterialData('+id+')">Submit</button>';
	materialBlockModelData+='</div>';
	materialBlockModelData+='</div>';
	materialBlockModelData+='</div>';
	materialBlockModelData+='</div>';
	materialBlockModelData+='</div>';
	if(id!=1){
		$("#appendMaterialArea"+id).html(materialBlockModelData);
	}
	
	var SiteId=$("#SiteId").val();
	var url = "getWorkOrderWDBlockDetails.spring?wdId="+wdId+"&materialGroupId="+materialGroupId+"&groupIdUOM="+materialGroupId+"&workOrderNo="+workOrderNo+"&SiteId="+SiteId;
	$("#BlockId"+id).html("");
	
	  $.ajax({
		  url : url,
		  type : "POST",
		contentType : "application/json",
		success : function(flats) {
			debugger;
			flats=JSON.parse(flats);
			console.log("Data: "+JSON.stringify(flats));
			var str="";
			var subRowCount=1;
			//this code for floors
			  for(var i=0;i<flats.blockName.length;i++){
				
				if(flats.blockName[i].content!=undefined){
					  str+='<div class="col-md-12"><div class="col-md-2 block-cls">'+flats.blockName[i].content+'</div><div class="col-md-12">';	  
				  
				for(var j=0;j<flats.blockName[i].floor.length;j++){
					if(flats.blockName[i].floor[j].name!=undefined){
					 subRowCount++;
					 //this code if for only floor names 
					 str+='<div class="col-md-2 flatmainDiv" >';
						//+'<div class="col-md-2 no-padding-left"><input type="checkbox" class=""></div>'
					 str+='<div class="col-md-12 text-center">';
					 str+='<img src="images/Floor.jpg" style="width: 100px;">';
					 str+='</div>';
					 str+='<div class="col-md-12 text-center" style="font-size: 16px;color: green;font-weight: 900;">'+flats.blockName[i].floor[j].name+'</div>';
					 str+='<div class="col-md-12">';
					 str+='<input type="text" class="form-control doNotAllowCopyPaste sumOfQuantityRowWise'+id+'" id="'+subRowCount+'FloorQty'+id+'" name="FloorQty'+id+'"  onkeypress="return isNumberCheck(this, event)"   onblur="validateMaterialQuantity(this.id,\'Floor\','+id+','+subRowCount+')" value="">';
					 str+='<input type="hidden" class="form-control" id="'+subRowCount+'FloorId'+id+'" name="FloorId'+id+'"  value="'+flats.blockName[i].floor[j].id+'">'; 
					 str+='<input type="hidden" class="form-control" id="'+subRowCount+'FloorAvailQty'+id+'" name="FloorAvailQty'+id+'"  value="'+parseFloat(flats.blockName[i].floor[j].AvlQty).toFixed(2)+'">';
					 str+='<input type="hidden" class="form-control" id="'+subRowCount+'BlockId'+id+'" name="BlockId'+id+'"  value="'+flats.blockName[i].floor[j].blockId+'">';
					 str+='<input type="hidden" class="form-control" id="'+subRowCount+'typeOfRecords'+id+'" name="typeOfRecords'+id+'" value="Floor"> ';
					 str+='</div>';
					 str+='</div>';
					}else{
						//code for printing floor,flat names
						if(flats.blockName[i].floor[i].content!=undefined){
						 str+='<div class="col-md-1 no-padding-left no-padding-right text-center floor-cls">'+flats.blockName[i].floor[j].content+'</div>';
						 str+='<div class="col-md-11"  style="margin-bottom: 20px;">';
						/* str+='<div class="col-md-12">';
						 //+'<div class="col-md-2 no-padding-left"><input type="checkbox" class=""></div>'
						 str+='<div class="col-md-8 text-center">';
						 str+='<i class="fa fa-home" style="font-size:32px"></i>';
						 str+='</div>';
						 str+='</div>';*/
						 
						 str+='<div class="col-md-12">';
						// str+='<input type="text" class="form-control" name="floorQTy'+id+'" onclick="validateMaterialQuantity(this.id,"floor")">';// id="floorQtyId'+flats.blockName[i].floor[j].id+'"
						// str+='<input type="hidden" class="form-control" name="floorId'+id+'"  value="'+flats.blockName[i].floor[j].id+'">';// id="floorId'+flats.blockName[i].floor[j].id+'"
						// str+='<input type="hidden" class="form-control" name="floorAvailQty'+id+'"  value="'+flats.blockName[i].floor[j].AvlQty+'">';// id="floorAvailQty'+flats.blockName[i].floor[j].id+'"
						// str+='<input type="hidden" class="form-control" name="blockId'+id+'"  value="'+flats.blockName[i].floor[j].blockId+'">';// id="floorAvailQty'+flats.blockName[i].floor[j].id+'"
						
						for (var flat = 0; flat < flats.blockName[i].floor[j].flats.flat.length; flat++) {
							 //flats.blockName[i].floor[j].flats.flat[flat].name
							//code for printing flat names
							 if(flats.blockName[i].floor[j].flats.flat[flat].name!=undefined){
								 subRowCount++;
								 str+='<div class="col-md-2 flatmainDiv">';
								 //+'<div class="col-md-2 no-padding-left"><input type="checkbox" class=""></div>'
								 str+='<div class="col-md-12 text-center">';
								 str+='<i class="fa fa-home" style="font-size: 50px;color: #f26601;"></i>';
								 str+='</div>';
								// str+='</div>';
								 str+='<div class="col-md-12 text-center" style="font-size: 16px;color: green;font-weight: 900;">'+flats.blockName[i].floor[j].flats.flat[flat].name+'</div>';
								 str+='<div class="col-md-12">';
								 str+='<input type="text" class="form-control doNotAllowCopyPaste sumOfQuantityRowWise'+id+'" id="'+subRowCount+'FlatQty'+id+'" name="FlatQty'+id+'"  onkeypress="return isNumberCheck(this, event)"   onblur="validateMaterialQuantity(this.id,\'Flat\','+id+','+subRowCount+')" value="">';
								 str+='<input type="hidden" class="form-control" id="'+subRowCount+'FlatId'+id+'" name="FlatId'+id+'"  value="'+flats.blockName[i].floor[j].flats.flat[flat].id+'">';
								 str+='<input type="hidden" class="form-control" id="'+subRowCount+'FlatAvailQty'+id+'" name="FlatAvailQty'+id+'"  value="'+parseFloat(flats.blockName[i].floor[j].flats.flat[flat].AvlQty).toFixed(2)+'">';
								 str+='<input type="hidden" class="form-control" id="'+subRowCount+'FloorId'+id+'" name="FloorId'+id+'"  value="'+flats.blockName[i].floor[j].flats.flat[flat].floorId+'">';
								 str+='<input type="hidden" class="form-control" id="'+subRowCount+'BlockId'+id+'" name="BlockId'+id+'"  value="'+flats.blockName[i].floor[j].flats.flat[flat].blockId+'">';
								 str+='<input type="hidden" class="form-control" id="'+subRowCount+'typeOfRecords'+id+'" name="typeOfRecords'+id+'" value="Flat"> ';
								 str+='</div>';
								 str+='</div>';
							 }
						}
						 
						 str+='</div>';
						 str+='</div>';
					}
				}
				}
				str+='</div></div>';
			  }else{
				  //this code is for printing block name
				  if(flats.blockName[i].name!=undefined){
					  subRowCount++;  
					  str+='<div class="col-md-2 blockMainDiv">';
					     //+'<div class="col-md-2 no-padding-left"><input type="checkbox" class=""></div>'
						 str+='<div class="col-md-12 text-center">';
						 str+='<i class="fa fa-building" style="font-size: 50px;color: #f26601;"></i>';
						 str+='</div>';
						 str+='<div class="col-md-12 block-cls text-center" style="color:green;">'+flats.blockName[i].name+'</div>';
						 str+='<div class="col-md-12">';
						 str+='<input type="text" class="form-control doNotAllowCopyPaste sumOfQuantityRowWise'+id+'" id="'+subRowCount+'BlockQty'+id+'" name="BlockQty'+id+'"  onkeypress="return isNumberCheck(this, event)"   onblur="validateMaterialQuantity(this.id,\'Block\','+id+','+subRowCount+')" value="">';
						 str+='<input type="hidden" class="form-control" id="'+subRowCount+'BlockId'+id+'" name="BlockId'+id+'" value="'+flats.blockName[i].id+'">';
						 str+='<input type="hidden" class="form-control" id="'+subRowCount+'BlockAvailQty'+id+'" name="BlockAvailQty'+id+'" value="'+parseFloat(flats.blockName[i].AvlQty).toFixed(2)+'">';
						 str+='<input type="hidden" class="form-control" id="'+subRowCount+'typeOfRecords'+id+'" name="typeOfRecords'+id+'" value="Block">';
						 str+='</div>';
						 str+='</div>';
				  }
			  }
			}   
			  $(".overlay_ims").hide();	
			  $(".loader-ims").hide();
				 
			 $("#modalBlockbody"+id).html(str);
			 //
			 $(".doNotAllowCopyPaste").bind('paste', function (e) {
					e.preventDefault();
			 });
		}
	  });
}
	//method for validation block floor flat, common method 
	function validateMaterialQuantity(id,name,rowNum,subRowNum){
		var error=true;
		var currentValue=parseFloat($("#"+id).val()==""?0:$("#"+id).val());
		var availbleQty=parseFloat($("#"+subRowNum+name+"AvailQty"+rowNum).val()==""?0:$("#"+subRowNum+name+"AvailQty"+rowNum).val());
		console.log(currentValue+" "+availbleQty);
		if(currentValue>availbleQty){
			alert("You can not allocate quantity more than available quantity, quantity is "+availbleQty);
			$("#"+id).val("0");
			return false;
		}
		return error;
	}
	
	function submitMaterialData(rowNum){
		var sumOfQuantity=0;
		$(".sumOfQuantityRowWise"+rowNum).each(function(){
			var currentValue=$(this).val()==""?0:parseFloat($(this).val());
			sumOfQuantity+=currentValue;
		});
		
		var QuantityId=$("#QuantityId"+rowNum).val()==""?0:parseFloat($("#QuantityId"+rowNum).val());
		var ProductAvailabilityId=$("#ProductAvailabilityId"+rowNum).val()==null?"":parseFloat($("#ProductAvailabilityId"+rowNum).val());
		if(sumOfQuantity>ProductAvailabilityId){
			alert("Quantity should not be greatter than availability.");
			$("#QuantityId"+rowNum).val("0");
			$("#blockModal"+rowNum).show();
			return false;
		}else{
			$("#QuantityId"+rowNum).val(sumOfQuantity.toFixed(2));
		}
	}
	
	/*======================================================= getting the floor data start===============================================*/
	function populateFloorData(id) {debugger;

	var wd=$('#WD'+id).val();
	var wdId=wd.split("$")[0];
	var url = "getFloorDataDetails.spring?wdId="+wdId;
	$("#FloorId"+id).html("");
	var request = getAjaxObject();

	try {
	request.onreadystatechange = function() {
		
		if(request.readyState == 4 && request.status == 200) {
			debugger;
			var resp = request.responseText;
			resp = resp.trim();
			
			var spltData = resp.split("|");
	    	//alert(spltData);
			
	    	available = new Array();
	    	for(var j=0; j<spltData.length; j++) {
	    		available[j] = spltData[j];
	    	}
	    	
	    	var selectBox = document.getElementById("FloorId"+id);
		    //alert(selectBox);
		    
		    //Removing previous options from select box - Start
		    /*if(document.getElementById("FlatNumberId"+rowNum) != null && document.getElementById("FlatNumberId"+rowNum).options.length > 0) {
		    	document.getElementById("FlatNumberId"+rowNum).options.length = 0;
		    }*/
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
		    		var wdIdAndName = data[0]+"$"+data[1];	    		
		    		defaultOption.text = data[1];
		    	    defaultOption.value = wdIdAndName;
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
	/*============================================== GETTING THE FALT NUMBERS AND FLAT NAMES START===============================================*/
	
	function populateFlatData(id) {debugger;

	var wd=$('#WD'+id).val();
	var wdId=wd.split("$")[0];
	var url = "getFlatDataDetails.spring?wdId="+wdId;  
	var request = getAjaxObject();

	try {
	request.onreadystatechange = function() {
		
		if(request.readyState == 4 && request.status == 200) {
			debugger;
			var resp = request.responseText;
			resp = resp.trim();
			
			var spltData = resp.split("|");
	    	available = new Array();
	    	for(var j=0; j<spltData.length; j++) {
	    		available[j] = spltData[j];
	    	}
	    	
	    	var selectBox = document.getElementById("FlatNumberId"+id);		    
		    //Removing previous options from select box - Start
		    /*if(document.getElementById("FlatNumberId"+rowNum) != null && document.getElementById("FlatNumberId"+rowNum).options.length > 0) {
		    	document.getElementById("FlatNumberId"+rowNum).options.length = 0;
		    }*/
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
		    		var wdIdAndName = data[0]+"$"+data[1];	    		
		    		defaultOption.text = data[1];
		    	    defaultOption.value = wdIdAndName;
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
	
	
	

	/*================================================= this is for getting the block id and name end =============================================*/
	/*================================================== this is for workoredr description  purpose end ==========================================*/
function validateUnitsAndAvailability(qtyObj) {
	
	var qtyObjectId = qtyObj.id;
	
	var currentRowNum = qtyObjectId.match(/\d+/g);	
	var pro = $("#combobox"+currentRowNum).val();	
	pro = pro.split("$")[0];
	var subPro = $("#comboboxsubProd"+currentRowNum).val();
	subPro = subPro.split("$")[0];
	var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
	childPro = childPro.split("$")[0];
	var requesteddate=$("#ReqDateId").val();
	var qty = "";
	var productAva = "";
	
	qty = document.getElementById("QuantityId"+currentRowNum).value;
	qty = parseFloat(qty);
	if(qty==0 || qty==0.0 || qty==0.00 || qty=='0' || qty=='0.0' || qty=='0.00' || qty=="0" || qty=="0.0" || qty=="0.00") {
		alert("Please enter valid quantity.");
		document.getElementById("QuantityId"+currentRowNum).value = "";
		document.getElementById("QuantityId"+currentRowNum).focus();
		return false;
	}
	productAva = document.getElementById("ProductAvailabilityId"+currentRowNum).value;
	productAva = parseFloat(productAva);
	
	if(productAva <= 0) {
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
	var woTypeOfWork=$("#woTypeOfWork").val();
	if(woTypeOfWork!="MATERIAL"){
		var conId = document.getElementById("contractorId").value;
		if(conId == "" || conId == '' || conId == null) {
			alert("Please enter contractor name.");
			document.getElementById("ContractorNameId").focus();
			return;
		}
	}
	
	
	var workOrderNo = document.getElementById("workOrderNo").value;
	var SiteId=  document.getElementById("SiteId").value;
	var makeWorkOrderNoMandatory=document.getElementById("makeWorkOrderNoMandatory").value;
	
	var contractorName=document.getElementById("ContractorNameId").value;;
	
	var index = makeWorkOrderNoMandatory.search(",");
	var flag=true;
	if(contractorName!="SUMADHURA EMPLOYEES"){
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
	}//contractorName condition
	
 	if(flag == false) {
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