//append row to the HTML table
function appendRow() {
    debugger;
    var valStatus = validateMajorHeadTable();
    
    if(valStatus == false) {
        return false;
    }
    var tbllength=$('#indentIssueTableId').find('tr').length;
	/*alert(tbllength);*/
	if(tbllength==2){
		var tid=$('#indentIssueTableId tr:last').attr('id');
		var res = tid.split("productrow")[1];
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
       // alert(valStatus);
         
        if(valStatus == false) {
            return false;
        }
        if (typeof(Storage) !== "undefined") {
            // Store    
             debugger;
         
            // Retrieve
         var row1=sessionStorage.getItem("rowsIncre");
         var r=parseInt(row1)+1;
         sessionStorage.setItem("rowsIncre", r);
        } else {
           alert("Sorry, your browser does not support Web Storage...");
        };
        if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
             
            //alert("HAI");
            var row = tbl.insertRow(tbl.rows.length);
         
            var i;
         
            var tableColumnName = "";
            var columnToBeFocused = "";
             
            var rowNum = getLastRowNum();
             
            $("#addNewItemBtnId"+rowNum).hide();
             
            rowNum = rowNum+1;
            var rowid="productrow"+rowNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "productrow");
            debugger;
            for (i = 0; i < tbl.rows[0].cells.length; i++) {
             
            var x = document.getElementById("indentIssueTableId").rows[0].cells;
            tableColumnName = x[i].innerText;
            tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
            tableColumnName = tableColumnName.replace(/\./g,'');
            try {
            	   tableColumnName = tableColumnName.trim();
			} catch (e) {
			console.log("error while creating Row Of NMR Data"+e);
			}
            columnToBeFocused = x[1].innerText;
            columnToBeFocused = columnToBeFocused.replace(/ /g,'');
            //alert("Table Column Name = "+tableColumnName.replace(/ /g,''));
             
            createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);
        }
         
            var lastDiv = getLastRowNum();
            $("#WO_MajorHead"+lastDiv).focus();
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
  /*  document.getElementById("hiddenSaveBtnId").value = "";
     
    if(document.getElementById("addNewItemBtnId"+currentRow) != null && document.getElementById("addDeleteItemBtnId"+currentRow) != null) {
        alert("This row can't be deleted.");
    }
    else {
        var row = btn.parentNode.parentNode;
        row.parentNode.removeChild(row);
    }
    */
	 var canIDelete = window.confirm("Do you want to delete row?");
     
	    if(canIDelete == false) {
	        return false;
	    }
	
    debugger;
    var rowscount=$('#indentIssueTableId').find('tr').length;
	
	if(rowscount==2){
		alert("this row con't be deleted.");
		return false;
	}
	//removing row
	
   $("#productrow"+currentRow).remove();
	
	var tid=$('#indentIssueTableId tr:last').attr('id');	
	var res = tid.split("productrow")[1];
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
 
function loadSubProds(prodId, rowNum) {/*
    debugger;
     
     
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
                $("#UOMId"+rowNum).val("");
                 
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
*/}
 
function loadSubSubProducts(subProdId, rowNum) {/*
     
    subProdId = subProdId.split("$")[0];
    //alert("Sub Product Id After Split = "+subProdId);
     
    var url = "indentIssueChildProducts.spring?subProductId="+subProdId;
       
    var request = getAjaxObject();
     
    try {
        request.onreadystatechange = function() {
             
            if(request.readyState == 4 && request.status == 200) {
                 
                $("#ChildProduct"+rowNum).val("");
                $("#UOMId"+rowNum).val("");
                 
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
*/}
/****** Method for to call Duplicate Slip Numbers*************/
 

 
/********** Method for load the Contractors**************/
function populateData() {/*
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
            source : resultArray
        });
  },
  error:  function(data, status, er){
      alert(data+"_"+status+"_"+er);
      }
  });
 
 
  $('#ContractorNameId').on('change', function(){debugger;
        var value = $(this).val();
         
        value = value.replace("&", "$$$");
        //alert(value);
         
        setContractorData (value); //pass the value as paramter
 });
*/};
function setContractorData(cName) {/*
     
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
*/}
function setContrcData() {/*
    if(request.readyState == 4 && request.status == 200) {
        var resp = request.responseText;
        resp = resp.trim();
        //alert(resp);
        var vendorId = resp.split("@@")[0];
        $("#ContractorNameId").val(vendorId);
    }
*/}
 
/********** Method for to load the Employee Details****************/
 
function populateEmployee() {/*
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
 
 
  $('#RequesterNameId').on('change', function(){debugger;
        var value = $(this).val();
             
        value = value.replace("&", "$$$");
        //alert(value);
         
        setEmpData (value); //pass the value as paramter
 });
*/};
/*function populateRequesterId() {
     
 
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
     
}*/
/*function populateEmployeeid(){
     
 
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
function setEmpData(eName) {
     
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
}
 */
function loadUnits(childProdId, rowNum) {/*
     
    childProdId = childProdId.split("$")[0];
     
    var url = "listUnitsOfSubProducts.spring?childProductId="+childProdId;
       
    var request = getAjaxObject();
     
    try {
        request.onreadystatechange = function() {
             
            if(request.readyState == 4 && request.status == 200) {
                 
                $("#UOMId"+rowNum).val("");
                 
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
                 
                var unitsToSelect = "UOMId"+rowNum;
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
*/}
 
//Quantity and Price Validation - Start
/*function validateNumbers(quantity, rownum) {
	debugger;
	if(quantity.length<1){
		return false;
	}
		if(!isNum(quantity)){
			$("#QuantityId"+rownum).val('');
			$("#QuantityId"+rownum).focus();
			alert("please enter only numbers");
			return false;
		}
		
	var AcceptedRateId=	$("#AcceptedRateId"+rownum).val();
	calCulateTotalAmout(AcceptedRateId,rownum)
 
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
}*/
 
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

function validateMajorHeadTable(){
	var error=true;
	$(".productrow").each(function(){
		var id=$(this).attr("id").split("productrow")[1];
		if($("#WO_MajorHead"+id).val()==''){
			alert("Please select Major Head");
			$("#WO_MajorHead"+id).focus();
			return error=false;;
		}
		if($("#WO_MinorHead"+id).val()==''){
			alert("Please select Minor Head");
			$("#WO_MinorHead"+id).focus();
			return error=false;;
		}
		if($("#WO_Desc"+id).val()==''){
			alert("Please select WO Description");
			$("#WO_Desc"+id).focus();
			return error=false;;
		}
		if($("#UOMId"+id).val()==''){
			alert("Please select UOM");
			$("#UOMId"+id).focus();
			return error=false;;
		}
		
		if($("#QuantityId"+id).val()==''||$("#QuantityId"+id).val()=='0'||$("#QuantityId"+id).val()=='0.00'){
			alert("Please enter quantity");
			$("#QuantityId"+id).focus();
			return error=false;;
		}
//		if(txtBoxIdName==$("#QuantityId"+id).attr("id")){
//			return false;
//		}
			
		if($("#AcceptedRateId"+id).val()==''||$("#AcceptedRateId"+id).val()=='0'||$("#AcceptedRateId"+id).val()=='0.00'){
			alert("Please enter accepted rate");
			$("#AcceptedRateId"+id).focus();
			return error=false;;
		}
		
	});
	return error;
}


function openTermsModal(buttonValue){
	debugger;
	 var conName =$("#contractorName").val();
	 var WorkOrderDate =$("#WorkOrderDate").val();
	  if(buttonValue!=undefined){
		 	$("#isSaveOrUpdateOperation").val(buttonValue);
	  }
	    if(conName == "" || conName == '' || conName == null) {
	        alert("Please select the contractor name.");
	        $("#contractorName").focus();
	        return false;
	    }
	    if(WorkOrderDate == "" || WorkOrderDate == '' || WorkOrderDate == null) {
	        alert("Please select the Work Order Date.");
	        $("#WorkOrderDate").focus();
	        return false;
	    }
	
	    var valStatus = validateMajorHeadTable();
     
	    if(valStatus == false) {
	        return false;
	    }
	  
	    $("#saveBtnId").attr("data-toggle", "modal");
	    $("#saveBtnId").attr("data-target", "#myModal-workOrder");
	    $("#saveBtnId1").attr("data-toggle", "modal");
	    $("#saveBtnId1").attr("data-target", "#myModal-workOrder");

}




function saveRecords(saveBtnClicked) {
    debugger;
 
    document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;     
    var newTc=document.getElementById("workorder_modal_text1").value;
    if(newTc==""||newTc==''){
    	$("#workorder_modal_text1").removeAttr("name");
    }
    var canISubmit = window.confirm("Do you want to Submit?");
     
    if(canISubmit == false) {
        return false;
    }
  
    /*var newTc=document.getElementById("workorder_modal_text1").value;
    if(newTc.length!=0){
   	  var wrapper    = $(".appen-div-workorder");
   	  $(wrapper).append('<input type="hidden" name="tc" value="'+newTc+'" class="form-control workorder_modal_text"/>');
    }*/
  
    
     if (typeof(Storage) !== "undefined") {
            // Store    
             debugger;
         sessionStorage.setItem("rowsIncre", 1);
        } else {
           alert("Sorry, your browser does not support Web Storage...");
      };
      
   	var sumofTotalAmount=0;
	debugger;
   	$(".productrow").each(function(){ 
		var currentId=$(this).attr("id").split("productrow")[1];
		debugger;
		sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
	});
   	$("#TotalAmountOfWorkOrder").val(sumofTotalAmount);
    $("#saveBtnId").attr("disabled", true);
    $("#saveBtnId1").attr("disabled", true);
 
    document.getElementById("countOfRows").value = getAllProdsCount();  
    document.getElementById("workOrderFormId").action = "saveNMRWorkOrder.spring";
    document.getElementById("workOrderFormId").method = "POST";
    document.getElementById("workOrderFormId").submit();
}

//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){
	$("#WO_Desc"+rowNum).val("");
		debugger;
	var rv = true;
		jQuery('.WorkDesc').each(function() {
			  var currentElementId = $(this).attr("id").split("WO_Desc")[1];
			   var currentValue = $(this).val();
			   var currentWoMinorHead=$("#WO_MinorHead"+rowNum).val();
			   var CompareWoMinorHead=$("#WO_MinorHead"+currentElementId).val();
			  if(currentValue==childname && currentWoMinorHead==CompareWoMinorHead){
				alert("This child product is already exist, Please choose different child product.");
				$("#UOMId"+rowNum).html('<option>--Select--</option>');
				$("#QuantityId"+rowNum).val('');
				$("#AcceptedRateId"+rowNum).val('');
				$("#TotalAmountId"+rowNum).val('');
				$("#NoteId"+rowNum).val('');
				return rv = false;
			   }     
			  else{
				  return rv = true;
			  }
		});
		 return rv;
}