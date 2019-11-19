//append row to the HTML table
function appendRow() {
    var tbllength = $('.workorderrowcls').length;  
    if(tbllength==1){    	
    	var tid = $('.workorderrowcls').last().attr("id");     	
    	var res = tid.split("workorderrow")[1];
    	$("#addDeleteItemBtnId"+res).show();
    }
    var pressedKey = window.event;
    if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
         
        btn = pressedKey.target || pressedKey.srcElement;
        var buttonId = btn.id;
         
        if(buttonId.includes("addNewItemBtnId")) {
            document.getElementById("hiddenSaveBtnId").value = "";
        }
         
        var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
     
        var tbl = document.getElementById("indentIssueTableId1");
         
        var valStatus = validateRowData();
        if(valStatus == false) {
            return false;
        }
        if (typeof(Storage) !== "undefined") {
         var row1=sessionStorage.getItem("rowsIncre");
         var r=parseInt(row1)+1;
         sessionStorage.setItem("rowsIncre", r);
        } else {
           alert("Sorry, your browser does not support Web Storage...");
        };
        if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
            var row = tbl.insertRow(tbl.rows.length);
         
            var i;
         
            var tableColumnName = "";
            var columnToBeFocused = "";
             
            var rowNum = getLastRowNum();
             
            $("#addNewItemBtnId"+rowNum).hide();
             
            rowNum = rowNum+1;
            var rowid="workorderrow"+rowNum;
           
    		$(row).attr("id", rowid);
            $(row).attr("class", "workorderrowcls");
            for (i = 0; i < tbl.rows[0].cells.length; i++) {
             
            var x = document.getElementById("indentIssueTableId1").rows[0].cells;
            tableColumnName = x[i].innerText;
            tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
            tableColumnName = tableColumnName.replace(/\./g,'');
            columnToBeFocused = x[1].innerText;
            columnToBeFocused = columnToBeFocused.replace(/ /g,'');
             
            createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);
        }
         
            var lastDiv = getLastRowNum();
            //focusing on Majorhead
            $("#WO_MajorHead"+lastDiv).focus();
        }
    }
}
//getting table rows numbers
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
 //updated delete code written by thirupathi        
function deleteRow(btn, currentRow) {
	document.getElementById("hiddenSaveBtnId").value = "";
	 var canIDelete = window.confirm("Do you want to delete row?");
     
	 if(canIDelete == false) {
	        return false;
	 }
	
	 var rowscount= $('.workorderrowcls').length;
	 if(rowscount==1){
		alert("This row can't be deleted.");
		return false;
	 }
	//removing row
    $("#workorderrow"+currentRow).remove();
	
	var tid=$('.workorderrowcls').last().attr('id');	
	var res = tid.split("workorderrow")[1];
	if(rowscount==2){
		$("#addDeleteItemBtnId"+res).hide();
	}
	
	if(res<currentRow){		
		$("#addNewItemBtnId"+res).show();
	}	
	
	  	var materialWoAmount=0;
	    var laborWoAmount=0;
		$(".workorderrowcls").each(function(){
			var currentId=$(this).attr("id").split("workorderrow")[1];	
			laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val());
			materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val());
		});
	
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
                pipeData = pipeData+snosid+"|";
            }
        }
    }
    return pipeData;
}
 
function loadSubProds(prodId, rowNum) {} 
function loadSubSubProducts(subProdId, rowNum) {}
function populateData() {};
function setContractorData(cName) {}
function setContrcData() {}
function populateEmployee() {};
function loadUnits(childProdId, rowNum) {}
 
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
//saving data
function saveRecords(saveBtnClicked) {
    document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
    $(".overlay_ims").show();	
	$(".loader-ims").show();
    var valStatus = appendRow();
     
    if(valStatus == false) {
    	$(".overlay_ims").hide();	
		$(".loader-ims").hide();
        return false;
    }
    
    var conName = $("#contractorName").val();
    var typeOfWork=$("#typeOfWork").val();
	if(typeOfWork!="MATERIAL"){
	    if(conName == "" || conName == '' || conName == null) {
	        alert("Please select the contractor name.");
	        document.getElementById("conName").focus();
	        $(".overlay_ims").hide();	
			$(".loader-ims").hide();
	        return;
	    }
	}
    var chargesRowCountNum=[];
    var sumofTotalAmount=0;
/*	$(".workorderrowcls").each(function(){
		var currentId=$(this).attr("id").split("workorderrow")[1];	
		sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
		chargesRowCountNum.push(currentId);
	});*/
    
    var materialWoAmount=0;
    var laborWoAmount=0;
	$(".workorderrowcls").each(function(){
		var currentId=$(this).attr("id").split("workorderrow")[1];	
		laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val());
		materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val());
		chargesRowCountNum.push(currentId);
	});
	  materialWoAmount=materialWoAmount.toFixed(2);
	  laborWoAmount=laborWoAmount.toFixed(2);
	  $("#materialWoAmount").val(materialWoAmount);
	  $("#laborWoAmount").val(laborWoAmount);
	  $("#materialWoAmountSpan").text(materialWoAmount);
	  $("#laborWoAmountSpan").text(laborWoAmount);
      $("#sumofTotalAmount").val(parseFloat(laborWoAmount)+parseFloat(materialWoAmount));
    

    //$("#sumofTotalAmount").val(sumofTotalAmount);

 
    var tableLength=$(".workorderrowcls").length;
    for(var i=0;i<chargesRowCountNum.length;i++){
    	 addWorkArea(chargesRowCountNum[i], 2);
    }
    var canISubmit = window.confirm("Do you want to Submit?");
     
    if(canISubmit == false) {
    	$(".overlay_ims").hide();	
		$(".loader-ims").hide();
        return false;
    }
    
    var newTc=document.getElementById("workorder_modal_text1").value;
    if(newTc.length!=0){
   	  var wrapper    = $(".appen-div-workorder");
   	  $(wrapper).append('<input type="hidden" name="tc" value="'+newTc+'" class="form-control workorder_modal_text"/>');
    }
    if (typeof(Storage) !== "undefined") {
        sessionStorage.setItem("rowsIncre", 1);
     }else {
         alert("Sorry, your browser does not support Web Storage...");
     };
        
    document.getElementById("saveBtnId").disabled = true;   
    document.getElementById("saveBtnId1").disabled = true;
    document.getElementById("countOfRows").value = getAllProdsCount();  
    document.getElementById("workOrderFormId").action = "saveWorkOrder.spring";
    document.getElementById("workOrderFormId").method = "POST";
    document.getElementById("workOrderFormId").submit();
}
//delete scopeof work order
function deleteScopeOfWork(btn){
	$("#newtxtbox"+btn).remove();
}
//dont remove this childcampare method this is for fueture use
//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){
	$("#WO_Desc"+rowNum).val('');
	var rv=true;
	var tablelength=$(".workorderrowcls").length;	
	if(tablelength>1){
		jQuery('.childproduct').each(function() {
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
//checking duplicate row in table
function checkingDuplicateRow(rowNum){
	var error="true";
	var currentWO_MajorHead=$("#combobox"+rowNum).val().split("$")[0];
	var currentWO_MinorHead=$("#comboboxsubProd"+rowNum).val().split("$")[0];
	var currentWO_Desc=$("#comboboxsubSubProd"+rowNum).val().split("$")[0];
	var currentUOM=$("#UOMId"+rowNum).val().split("$")[0];

	$(".workorderrowcls").each(function(){
		var currentId=$(this).attr("id").split("workorderrow")[1];
		if(currentId!=rowNum){
			var WO_MajorHead=$("#combobox"+currentId).val().split("$")[0];
			var WO_MinorHead=$("#comboboxsubProd"+currentId).val().split("$")[0];
			var WO_Desc=$("#comboboxsubSubProd"+currentId).val().split("$")[0];
			var UOM=$("#UOMId"+currentId).val().split("$")[0];
		
			if(currentWO_MajorHead==WO_MajorHead && WO_MinorHead==currentWO_MinorHead && currentWO_Desc==WO_Desc && currentUOM==UOM){
				alert("UOM already exist, Please choose different UOM.");
				return error="false";
			}
		}
	});

	return error;
	
}

//for showing the selected work area
function showDetailsFunction(rowsIncre) {
   debugger;
   var len=$("#lengthOfSelectedArea"+rowsIncre).val();
	
	var htmlData="";
	for (var i = 1; i <= len; i++) {
			var block_name=$("#BLOCK_NAMET"+rowsIncre+i+"").val();
			var floor_name=$("#FLOOR_NAMET"+rowsIncre+i+"").val();
			var flat_id=$("#FLAT_IDT"+rowsIncre+i+"").val();
			var seletedArea=$("#selectedAreaT"+rowsIncre+i+"").val();
			var wo_measurmen_name=$("#wo_measurmen_nameT"+rowsIncre+i+"").val();
			var accepted_rateT=$("#accepted_rateT"+rowsIncre+i+"").val();
			var record_typeT=$("#record_typeT"+rowsIncre+i+"").val();
			htmlData+=" <tr><td>"+block_name+"</td>	<td>"+floor_name+"</td>	<td>"+flat_id+"</td> <td>"+wo_measurmen_name+"</td> <td>"+record_typeT+"</td> <td>"+seletedArea+"</td><td>"+accepted_rateT+"</td> </tr>";		
		}
	
	$("#tblShowDetails").html(htmlData);
}

function checkWorkOrderNo(workOrderNo){
	debugger;
	var actualWorkOrderNumber=$("#actualWorkOrderNumber").val();
	var typeOfWork=$("#typeOfWork").val();
	var actualarray=actualWorkOrderNumber.split("/");
	var lengthForWONumValidation=4;
	if(typeOfWork=="MATERIAL"){
		lengthForWONumValidation=5;
	}
	var array=new Array();
	array=workOrderNo.split("/");
	
	for (var index = 0; index < actualarray.length-1; index++) {
		 if(actualarray[index]!=array[index]){
			 alert("Please enter correct format number of work order.");
			 $('#saveBtnId').attr('disabled',true);
				$('#saveBtnId1').attr('disabled',true);
			 $('#saveWorkOrder').attr('disabled',true);
			return false;
		 }
	}
	
	if(array.length>lengthForWONumValidation){
		alert("Please enter correct format number of work order.");
		 $('#saveBtnId').attr('disabled',true);
		 $('#saveBtnId1').attr('disabled',true);
		 $('#saveWorkOrder').attr('disabled',true);
		return false;
	}else{
		 $('#saveBtnId').attr('disabled',false);
		 $('#saveBtnId1').attr('disabled',false);
		 $('#saveWorkOrder').attr('disabled',false);
	}

	
	if(!isNum(array[array.length-1])){
		alert("Please enter correct format number of work order.");
		 $('#saveBtnId').attr('disabled',true);
		 $('#saveBtnId1').attr('disabled',true);
		 $('#saveWorkOrder').attr('disabled',true);
		return false;
	}else{
		 $('#saveBtnId').attr('disabled',false);
		 $('#saveBtnId1').attr('disabled',false);
		 $('#saveWorkOrder').attr('disabled',false);
	}

	var url = "checkWorkOrderNoExistsOrNot.spring";
	$.ajax({
		url : url,
		type : "get",
		data:{
			workOrderNo:workOrderNo
		},success : function(data) {
		debugger;
			if(data=="true"){
				alert("Work order number already exists,Please enter another work order number.");
				 $('#saveBtnId').attr('disabled',true);
					$('#saveBtnId1').attr('disabled',true);
				 $('#saveWorkOrder').attr('disabled',true);
			}else{
				 $('#saveBtnId').attr('disabled',false);
					$('#saveBtnId1').attr('disabled',false);
				 $('#saveWorkOrder').attr('disabled',false);
			}
		}
	});
}


//checking number or not
function isNumberCheck(el, evt) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    var num=el.value;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
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

