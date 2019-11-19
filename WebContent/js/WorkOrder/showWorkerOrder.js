//append row to the HTML table
  function appendRow() {
      rowsIncre++;
      var pressedKey = window.event;
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
         
          if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
        	  debugger;
              var row = tbl.insertRow(tbl.rows.length);
              var i;
              var tableColumnName = "";
              var columnToBeFocused = "";
              var rowNum =parseInt($(".workorderrowcls:last").attr("id").split("tr-class")[1]);
              $("#addNewItemBtnId"+rowNum).hide();              
              rowNum=rowNum+1;
              var rowid="tr-class"+rowNum;              
      		  $(row).attr("id", rowid);
              $(row).attr("class", "workorderrowcls");
              
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
              //focusing on product textbox
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
          var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;          
          return parseInt(lastDivVal);
  }
  //deleting row      
  function deleteRow(btn, currentRow) {debugger;
	  document.getElementById("hiddenSaveBtnId").value = "";
		var rowscount=$('#indentIssueTableId').find('tr').length;
		var canIDelete=window.confirm("Do you want to delete?");
		if(canIDelete==false){
			return false;
		}
		//removing row
		var n=0;
		$(".workorderrowcls").each(function(){debugger;
			var id=$(this).attr("id").split("tr-class")[1];
			if($("#isDelete"+id).val()!="d"){
				n++;
			}
		});

		if(n==1){
			alert("This row can't be deleted.");
			return false;
		}
	   $("#tr-class"+currentRow).remove();
		var tid=$('.workorderrowcls:last').attr('id');	
		var res = tid.split("tr-class")[1];
		if(rowscount==3){
			$("#addDeleteItemBtnId"+res).hide();
		}
		if(res<=currentRow){		
			$("#addNewItemBtnId"+res).show();
		}
  }
  //getting product table rows 
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
 //validating file size and file extention
  function validateFileExtention(){
	var ext="";
	var kilobyte=1024;
	var len=$('input[type=file]').val().length;
	var count=0;
	  $('input[type=file]').each(function () {
	        var fileName = $(this).val().toLowerCase(),
	         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
			 if(fileName.length!=0){
	        	if((this.files[0].size/kilobyte)<=kilobyte){
				}else{
					alert("Please upload below 1 MB PDF file");
					//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
					$(this).val('');
					$(this).focus();
					count++;
				 return false;
				}
		        if (!(regex.test(fileName))) {
		            $(this).val('');
		            alert('Please select correct file format');
		            count++;
		            return false;
		        }
	        }
	  });
	if(count==0)
	  return true;
	else
		return false;
	 }
  //revising NMR work order
  function reviseNMRWORecords(saveBtnClicked){
		debugger;
	  var operationType=$("#operType").val();
		 document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
		 var conName = document.getElementById("contractorName").value;
		 if(conName == "" || conName == '' || conName == null) {
		     alert("Please select the contractor name.");
		     document.getElementById("conName").focus();
		     return;
		 }
		 var newTermsAndCondition=$("#workorder_modal_text1").val();
		 if(newTermsAndCondition.trim().length==0){
			 $("#workorder_modal_text1").removeAttr("name");
		 }
		 var sumofTotalAmount=0;
		  $(".reviseworkorderrowcls").each(function(){
				debugger;
	 		var currentId=$(this).attr("id").split("tr-class")[1];	
	 		if($("#isDelete"+currentId).val()!="d"){
	 			sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val()==""?0:$("#TotalAmountId"+currentId).val());
			}
		}); 
		 	$("#sumofTotalAmount").val(sumofTotalAmount.toFixed(2));   
		 var canISubmit = window.confirm("Do you want to Revise Work Order?");
		     
	     if(canISubmit == false) {
		   return false;
	     }
		    
		 if (typeof(Storage) !== "undefined") {
		       sessionStorage.setItem("rowsIncre", 1);
		 } else {
		        alert("Sorry, your browser does not support Web Storage...");
		 };
		document.getElementById("saveBtnId").disabled = true;  
		document.getElementById("closeBtn").disabled = true;  
		document.getElementById("countOfRows").value = getAllProdsCount();  
			
		var typeOfWork=$("#typeOfWork").val();
		document.getElementById("workOrderFormId").action =	"saveReviseNMRWorkOrder.spring";
			
		document.getElementById("workOrderFormId").method = "POST";
		document.getElementById("workOrderFormId").submit();
	 
  }
  //revising work order 
  function reviseWORecords(saveBtnClicked){
		 var operationType=$("#operType").val();
	     var typeOfWork= $("#typeOfWork").val();
		 document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
		if(typeOfWork!="MATERIAL"){
			 var conName = $("#contractorName").val();
			 if(conName == "" || conName == '' || conName == null) {
			     alert("Please select the contractor name.");
			     document.getElementById("conName").focus();
			     return;
			 }
		}
		
		     
		var canISubmit = window.confirm("Do you want to Revise Work Order?");
		if(canISubmit == false) {
		     return false;
		}
     $(".overlay_ims").show();	
		 $(".loader-ims").show();
		if (typeof(Storage) !== "undefined") {
		      sessionStorage.setItem("rowsIncre", 1);
		} else {
		       alert("Sorry, your browser does not support Web Storage...");
		};
		 
		 /*document.getElementById("saveBtnId").disabled = true;  
		 document.getElementById("saveBtnId1").disabled = true;  
		 document.getElementById("closeBtn").disabled = true;*/  
		 
		 $("#saveBtnId").attr("disabled",true);
		 $("#saveBtnId1").attr("disabled",true);
		 //$("#finalReviseWOBtn").attr("disabled",true);
		 $("#closeBtn").attr("disabled",true);
		 
		 document.getElementById("countOfRows").value = getAllProdsCount();  
			    
		 var typeOfWork=$("#typeOfWork").val();
		 if(typeOfWork!="NMR"){
		     $("[name^=dispplayedRows]").each(function () {
				     var rowNum=$(this).val();
				     var result= addWorkArea(rowNum,"donotValidateMaterial");
				     if(result==false){
				     	canISubmit=false;
				     	return false; 
				      }
			});
		 }//typeOfWork condition
		 else if (typeOfWork=="NMR"){
		   	  
		 }      
		 $(".overlay_ims").hide();	
		 $(".loader-ims").hide();
		// return false;
		if(canISubmit == false) {
		    return false;
		}
		document.getElementById("workOrderFormId").action =	"saveReviseWorkOrder.spring";
		document.getElementById("workOrderFormId").method = "POST";
		document.getElementById("workOrderFormId").submit();
  }
  
  function updateWORecords(saveBtnClicked){
		 var operationType=$("#operType").val();
		 var typeOfWork= $("#typeOfWork").val();
		 var result=validateFileExtention();
		 if(result==false){
			 return false
		 }  
		 document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
		/* if(typeOfWork!="MATERIAL"){
		 var conName = $("#contractorName").val()
			 if(conName == "" || conName == '' || conName == null) {
			       alert("Please select the contractor name.");
			       document.getElementById("conName").focus();
			       return;
			  }
		 }*/
		  
		 var canISubmit = window.confirm("Do you want to Update Work Order?");
		  
		 if(canISubmit == false) {
		       return;
		 }
		    
		 if (typeof(Storage) !== "undefined") {
		         sessionStorage.setItem("rowsIncre", 1);
		 } else {
		         alert("Sorry, your browser does not support Web Storage...");
		 };
		 
		 $("#saveBtnId").attr("disabled", true);
		 $("#closeBtn").attr("disabled", true);
		 document.getElementById("countOfRows").value = getAllProdsCount();  
		 
		 if(operationType!="1"){
			 $("[name^=dispplayedRows]").each(function () {
				     var rowNum=$(this).val();
				     var result=   addWorkArea(rowNum,2);
					 if(result==false){
					  	 canISubmit=false;
					  	 return false;
					 }
			 });
			
			 document.getElementById("workOrderFormId").action =	"saveReviseWorkOrder.spring";
		}else{
			excuteDeleteFunction();
		}
		if(canISubmit == false) {
		     return false;
		}
		
		document.getElementById("workOrderFormId").method = "POST";
		document.getElementById("workOrderFormId").submit();
	}
  
  function saveNewWorkOrderRecords(){
	  debugger;
	  var typeOfWork= $("#typeOfWork").val();
	  var revision =$("#revision").val();
	  if(typeOfWork=="NMR"){
		 var valStatus = validateMajorHeadTable();  		      
		     if(valStatus == false) {
		         return false;
		     }
	   }
	   if(typeOfWork!="MATERIAL"){
	      var conName = document.getElementById("contractorName").value;
	  
	      if(conName == "" || conName == '' || conName == null) {
	          alert("Please select the contractor name.");
	          document.getElementById("conName").focus();
	          return;
	      }
	   }
		
		var lengthOfTermsAndCondition=$("[name^=changedTC]").length;
		var atualtermsAndConditionLength=$("#termsAndConditionLength").val();
		/*if(lengthOfTermsAndCondition!=atualtermsAndConditionLength){
			isWorkOrderDataChanged=true;
		}*/
	   var canISubmit = window.confirm("Do you want to Submit?");
	      
	      isWorkOrderDataChanged=false;
	      if(typeOfWork!="NMR"){
		      $("[name^=dispplayedRows]").each(function () {
		     	 var rowNum = $(this).val();
		     	 var result = addWorkArea(rowNum,2);
		     	
		     	 if(result == false){
		     		canISubmit = false;
		     		return false; 
		     	 }
		     	 var totalRowAmount=$("#TotalAmountId"+rowNum).val()==undefined?0:parseFloat($("#TotalAmountId"+rowNum).val());
		     	 var actualRowAmount=$("#actualTotalAmount"+rowNum).val()==undefined?0:parseFloat($("#actualTotalAmount"+rowNum).val());
		     	 if(totalRowAmount!=actualRowAmount||$("#isDelete"+rowNum).val()=="d"){
		     		isWorkOrderDataChanged=true;
		     	 }
			   });
	      }else if(typeOfWork=="NMR"){
	 		 var sumofTotalAmount=0;
			   	$(".workorderrowcls").each(function(){
			   		var rowNum=$(this).attr("id").split("tr-class")[1];	
		 		if($("#isDelete"+rowNum).val()!="d"){
		 			sumofTotalAmount+=parseFloat($("#TotalAmountId"+rowNum).val()==""?0:$("#TotalAmountId"+rowNum).val());
				}
		 		
		 		 var totalRowAmount=$("#TotalAmountId"+rowNum).val()==undefined?0:parseFloat($("#TotalAmountId"+rowNum).val());
		     	 var actualRowAmount=$("#actualTotalAmount"+rowNum).val()==undefined?0:parseFloat($("#actualTotalAmount"+rowNum).val());
		     	 if(totalRowAmount!=actualRowAmount||$("#isDelete"+rowNum).val()=="d"){
		     		isWorkOrderDataChanged=true;
		     	 }
		 		
			}); 
			 $("#sumofTotalAmount").val(sumofTotalAmount.toFixed(2)); 
			 $("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
	      }
	      
	      if(canISubmit == false) {
	          return false;
	      }
	      $("#isWorkOrderDataChanged").val(isWorkOrderDataChanged);
	      $("#saveBtnId").attr("disabled",true);
	      $("#submitBtnId").attr("disabled",true);
	      $("#rejectBtnId").attr("disabled",true);
	      $("#productDetails").attr("disabled",true);
	      $('#workOrderFormId').removeAttr('target');
	      var url="";
		    if(typeOfWork=="NMR"){
		    	if(revision=="0"){
		    		url="saveNMRWorkOrder.spring";	
		    	}else{
		    		url="saveReviseNMRWorkOrder.spring";
		    	}
		    }else{
		    	if(revision=="0"){
		    		url="saveWorkOrder.spring";
		    	}else{
		    		url="saveReviseWorkOrder.spring";
		    	}
		    }
	      
	      document.getElementById("workOrderFormId").action = url;
	      document.getElementById("workOrderFormId").method = "POST";
	      document.getElementById("workOrderFormId").submit();
		
  }
  
  
   function saveRecords(saveBtnClicked) {
	   $(".overlay_ims").show();	
	   $(".loader-ims").show();
      $("#hiddenSaveBtnId").val(saveBtnClicked);
      var typeOfWork= $("#typeOfWork").val();
  		if(typeOfWork=="NMR"){
  			 var valStatus = validateMajorHeadTable();  		      
  		      if(valStatus == false) {
  		    	$(".overlay_ims").hide();	
  		    	$(".loader-ims").hide();
  		          return false;
  		      }
  		}
  		if(typeOfWork!="MATERIAL"){
	      var conName = document.getElementById("contractorName").value;
	  
	      if(conName == "" || conName == '' || conName == null) {
	          alert("Please select the contractor name.");
	          document.getElementById("conName").focus();
	          $(".overlay_ims").hide();	
		      $(".loader-ims").hide();
	          return;
	      }
	   }
      
      var canISubmit = window.confirm("Do you want to Submit?");
      
      var typeOfWork=$("#typeOfWork").val();
      if(typeOfWork!="NMR"){
	      $("[name^=dispplayedRows]").each(function () {
	     	 var rowNum=$(this).val();
	     	 var result=   addWorkArea(rowNum,2);
	     	 if(result==false){
	     		$(".overlay_ims").hide();	
  		    	$(".loader-ims").hide();
	     		canISubmit=false;
	     		return false; 
	     	 }
		   });
      }
      
      if(canISubmit == false) {
    	  $(".overlay_ims").hide();	
	      $(".loader-ims").hide();
          return false;
      }
    
      var newTermsAndCondition=$("#workorder_modal_text1").val();
      if(newTermsAndCondition.trim().length==0){
    	  $("#workorder_modal_text1").removeAttr("name");
	  }

      $("#saveBtnId").attr("disabled",true);
      $("#submitBtnId").attr("disabled",true);
      $("#rejectBtnId").attr("disabled",true);
      $("#productDetails").attr("disabled",true);
      $("#modifyBtnId").attr("disabled",true);
      $('#workOrderFormId').removeAttr('target');
      document.getElementById("countOfRows").value = getAllProdsCount();  
      document.getElementById("workOrderFormId").action = "approveWorkOrderCreation.spring";
      document.getElementById("workOrderFormId").method = "POST";
      document.getElementById("workOrderFormId").submit();
  }
  
  function printWorkOrder(){
	  var canISubmit = window.confirm("Do you want to print work order?");
      
      if(canISubmit == false) {
          return false;
      }
      document.getElementById("printBtnId").disabled = true;   
      document.getElementById("printWorkOrderFormId").submit();
  }
  function rejectFromUpdatePage(rejectBtnClicked){
      var purpose=document.getElementById("purpose");
      if(purpose.value.length==0){
    	  alert("Enter the reason to reject the work order");
    	  purpose.placeholder = "Enter reason Here..";
    	  purpose.focus();
    	  return false;
      }
      var canISubmit = window.confirm("Do you want to Submit?");
      
      if(canISubmit == false) {
          return;
      }
      
      document.getElementById("saveBtnId").disabled = true;   
      document.getElementById("rejectBtnId").disabled = true;   
      document.getElementById("countOfRows").value = getAllProdsCount();  
      document.getElementById("workOrderFormId").action = "rejectPermanentWorkOrder.spring";
      document.getElementById("workOrderFormId").method = "POST";
      document.getElementById("workOrderFormId").submit();  
  }
  
  function sendTomodifyWorkOrder(saveBtnClicked,buttonValue){    
	  debugger;
      $("#operationTypeForWorkOrder").val("Modify");
 	 if(buttonValue!=undefined){
	 	  $("#isSaveOrUpdateOperation").val(buttonValue);
 	 }
      var purpose=document.getElementById("purpose");
      if(purpose.value.length==0){
    	  alert("Enter reason to modify the work Order");
    	  purpose.placeholder = "Enter reason Here..";
    	  purpose.focus();
    	  return false;
      }
      var canISubmit = window.confirm("Do you want to Submit?");
      
      if(canISubmit == false) {
          return false;
      }
      
      $("#saveBtnId").attr("disabled",true);
      $("#submitBtnId").attr("disabled",true);
      $("#rejectBtnId").attr("disabled",true);
      $("#productDetails").attr("disabled",true);
      $("#modifyBtnId").attr("disabled",true);
      
      document.getElementById("saveBtnId").disabled = true;   
      document.getElementById("countOfRows").value = getAllProdsCount();  
      document.getElementById("workOrderFormId").action = "rejectWorkOrderCreation.spring";
      document.getElementById("workOrderFormId").method = "POST";
      document.getElementById("workOrderFormId").submit();
  }
  
  function reject(saveBtnClicked,buttonValue) {    
    
      var messageForAlert="";
      if(buttonValue=="Discard"){
    	  messageForAlert="Enter reason to discard work Order.";
      }else if(buttonValue=="Reject"){
    	  messageForAlert="Enter reason to reject work Order.";
      }else{
    	  messageForAlert="Enter reason to reject work Order.";
      }
      $("#operationTypeForWorkOrder").val(buttonValue);
      if(buttonValue!=undefined){
		 	$("#isSaveOrUpdateOperation").val(buttonValue);
      }
    	  
      debugger;  
      var purpose=document.getElementById("purpose");
      if(purpose.value.length==0){
    	  alert(messageForAlert);
    	  purpose.placeholder = "Enter reason Here..";
    	  purpose.focus();
    	  return false;
      }
      var canISubmit = window.confirm("Do you want to Submit?");
      
      if(canISubmit == false) {
          return false;
      }
      
      $("#saveBtnId").attr("disabled",true);
      $("#submitBtnId").attr("disabled",true);
      $("#rejectBtnId").attr("disabled",true);
      $("#productDetails").attr("disabled",true);
      $("#modifyBtnId").attr("disabled",true);
      
      document.getElementById("countOfRows").value = getAllProdsCount();  
      document.getElementById("workOrderFormId").action = "rejectWorkOrderCreation.spring";
      document.getElementById("workOrderFormId").method = "POST";
      document.getElementById("workOrderFormId").submit();
  }
   
  function editRow(rowId){
      var result = confirm("Do you want to edit?");
	  if (!result) {
		  return false;
	  }
      $('#AcceptedRate'+rowId).attr('readonly', false);
      $('#ClickHere'+rowId).show();
      $("#addsave").show();
      $("#RequiredQuantity"+rowId).prop('readonly', false);
      $("#ProductAvailability"+rowId).prop('readonly', false);
      var typeOfWork=$("#typeOfWork").val();
      
      if(typeOfWork=="NMR")
    	  $('#Quantity'+rowId).attr('readonly', false);
     
      //ACP
       $('#Comments'+rowId).attr('readonly', false);
       var subProd= $("#comboboxsubProd"+rowId).val();
       var childProdct= $("#comboboxsubSubProd"+rowId).val();
       var Uom= $("#UnitsOfMeasurementId"+rowId).val();
       
       $.each($('.btn-visibilty'+rowId),function(index,row){           
    	   productId = $('#combobox'+rowId).val();
           SubproductId = $('#SubProduct'+rowId).val();        
           ChildproductId = $('#ChildProduct'+rowId).val();
        });
      
     /*
      * IMP code if you are changing major head and minor head work desk
      *  loadSubProds(productId,rowId, 'edit');
      
      var strUser= subProd.split("_");
      //$("#SubProduct"+rowId).val(strUser[1]);
      loadSubSubProducts(strUser[0],rowId, 'edit');
      var strUser= childProdct.split("_");
      //$("#ChildProduct"+rowId).val(strUser[1]);
      loadUnits(strUser[0],rowId, 'edit');*/
      $("#editItem"+rowId).attr("disabled",true);
  }
  var count=0;  
  function removeReviseWORow(rowId){
 	 	count++;
 	 	debugger;
  		var result = confirm("Do you want to remove the order?");
		if (!result) {
		  return false;
		}
		var typeOfWork=document.getElementById("typeOfWork").value;
		var flag="true";
		var operationType=$("#operType").val();
		var sendDataToBackEnd=new Array();
		var len=$("input[name='chk1"+rowId+"']:checked").length;
		//this code is used for while revising work order
		if(typeOfWork!="NMR"){
			$.each($("input[name='chk1"+rowId+"']"), function(){  
				    var workAreaId=$(this).val();
				    var recordType=$(this).attr("id").split(workAreaId)[0];
				    var seletedArea=parseFloat($("#"+recordType+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#"+recordType+workAreaId+"ALLOCATE_AREA").val());
	             	var accepted_rate=parseFloat($("#"+recordType+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#"+recordType+workAreaId+"ACCEPTED_RATE").val());
	             	var boq_rate=$("#"+recordType+workAreaId+"BOQ_RATE").val()==""?0:parseFloat($("#"+recordType+workAreaId+"BOQ_RATE").val());
	             	 
	             	var paymentdone= parseFloat($("#"+recordType+ workAreaId + "PAYMENTDONE").text());
				   
				  if(paymentdone!=0||paymentdone!=0.0){
				 			alert("You can not remove this work area, already payment initiated.");
				 			$("#"+recordType + workAreaId + "val").prop("checked", true);
				 			//$("#MATERIAL" + workAreaId + "val").prop("checked", true);
				 			flag="false";
			 			return false;
			 		}
			 		var advPreviousBillAmount=parseFloat($("#"+recordType + workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#"+recordType + workAreaId + "advPreviousBillAmount").val());;
			        if(advPreviousBillAmount!=0){
				           	alert("You can not remove this work area, already payment initiated.");
				 			$("#"+recordType + workAreaId + "val").prop("checked", true);
				 			//$("#MATERIAL" + workAreaId + "val").prop("checked", true);
				 			flag="false";
				 			return false;
			        }
			           
					  var workAreaForDB="";
					  var workIssueDetailsId="";
			          var seletedWorkArea="";
			          //if work Order type is material 
			          if(typeOfWork=="MATERIAL"){
			        	  workAreaForDB=$("#LABOR"+workAreaId+"workAreaID").val()==undefined?"":$("#LABOR"+workAreaId+"workAreaID").val();
						  workIssueDetailsId=$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#LABOR"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
				          seletedWorkArea=parseFloat($("#LABOR"+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#LABOR"+workAreaId+"ALLOCATE_AREA").val());
				      }else{
			        	  workAreaForDB=$("#MATERIAL"+workAreaId+"workAreaID").val()==undefined?"":$("#MATERIAL"+workAreaId+"workAreaID").val();
						  workIssueDetailsId=$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"QS_WO_TEMP_ISSUE_DTLS_ID").val();
				          seletedWorkArea=parseFloat($("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val());
				      }
			          if(workAreaForDB.length!=0&&workIssueDetailsId.length!=0&&seletedWorkArea.length!=0){
						var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
						sendDataToBackEnd.push(data);
			          }
				
		 		});
		 }else if(typeOfWork=="NMR"){
			 var previousQty=0;
				var PREVQTY=$("#nmrRowPaymentInitiatedDetils"+rowId).val();
			 	try {
					var index=PREVQTY.search("@@");
					if(PREVQTY.length!=0){
						if(index>=0){
							prevAreaQuantity=PREVQTY.split("@@");
							for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
								let	array_element = prevAreaQuantity[ind].split("##");
								var noOfWorker=parseFloat(array_element[0]);
								previousQty=previousQty+(parseFloat(noOfWorker));
							}
						}else{
							prevAreaQuantity = PREVQTY.split("##");
							previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
						}
					}
				} catch (e) {
					console.log(e);
				}
				var quantity=$("#Quantity"+rowId).val();
				if(previousQty!=0){
					alert("You can not remove this work area, already payment initiated.");
					$("#TotalAmountId"+rowId).val($("#actualTotalAmount"+rowId).val());
					$("#AcceptedRate"+rowId).val($("#actualAcceptedRate"+rowId).val());
					$("#Quantity"+rowId).val($("#actualQuantity"+rowId).val());
					flag="false";
				}
		 }
		if(flag=="false"){
			return false;
	     }
		  if(sendDataToBackEnd.length!=0){
				var canIcloseModelPopup= ajaxCallForValidatingWO_Material(rowId,sendDataToBackEnd,'removeRow')
				if(canIcloseModelPopup==false){
					flag=="false"
					return false;
				}
		  }
		 if(flag=="false"){
		    	return false;
		 }
		 
         var rows = $(".reviseworkorderrowcls").length;
         var strikeoutlength = $(".strikeout").length;
         if(strikeoutlength==(rows-1)){
            alert("This work order row can't be deleted.");
            return false;
         }
      
	    $("#tr-class"+rowId).addClass('strikeout');
	    $("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
	    $("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
        $('#snoDivId'+rowId).removeAttr('id');  
	    $('#combobox'+rowId).removeAttr('id');
	    $('#comboboxsubProd'+rowId).removeAttr('id');
	    $('#comboboxsubSubProd'+rowId).removeAttr('id');
	    $('#UnitsOfMeasurementId'+rowId).removeAttr('id');
	    $('#Quantity'+rowId).removeAttr('id');
	    document.getElementById("isDelete"+rowId).value="d";
		/*var sumofTotalAmount=0;
	   	$(".workorderrowcls").each(function(){
			var currentId=$(this).attr("id").split("tr-class")[1];	
			debugger;
			if($("#isDelete"+currentId).val()!="d"){
				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
			}
		});
	   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));*/
    }
  	//removing row
    function removeRow(rowId){
    	debugger; 
    	count++;
         var result = confirm("Do you want to remove the order?");
    	 
    	 if (!result) {
		   return false;
		 }
    	 
    	 var typeOfWork=document.getElementById("typeOfWork").value;
    	 var revision=$("#revision").val();
		 var operationType=$("#operType").val();
		 var sendDataToBackEnd=new Array();
		 var flag=true;
		 var len=$("input[name='chk1"+rowId+"']:checked").length;
		 if(typeOfWork!="NMR"){
				$.each($("input[name='chk1"+rowId+"']"), function(){ 
					 var workAreaId=$(this).val();
				     var recordType=$(this).attr("id").split(workAreaId)[0];
					 var seletedArea=parseFloat($("#"+recordType+workAreaId+"ALLOCATE_AREA").val()==""?0:$("#"+recordType+workAreaId+"ALLOCATE_AREA").val());
		             var accepted_rate=parseFloat($("#"+recordType+workAreaId+"ACCEPTED_RATE").val()==""?0:$("#"+recordType+workAreaId+"ACCEPTED_RATE").val());
		             var boq_rate=$("#"+recordType+workAreaId+"BOQ_RATE").val()==""?0:parseFloat($("#"+recordType+workAreaId+"BOQ_RATE").val());
			         var paymentdone= $("#"+recordType + workAreaId + "PAYMENTDONE").text()==undefined?0:$("#"+recordType + workAreaId + "PAYMENTDONE").text();
		 			debugger;
			 			if(paymentdone!=0||paymentdone!=0.0){
			 				alert("You can not remove this work area, already payment initiated.");
			 				flag="false";
			 				$("#"+recordType + workAreaId + "val").prop("checked", true);
			 				return false;
			 			}
			 			
			 			var advPreviousBillAmount=parseFloat($("#"+recordType+ workAreaId + "advPreviousBillAmount").val()==undefined?0:$("#"+recordType+ workAreaId + "advPreviousBillAmount").val());;
			            if(advPreviousBillAmount!=0){
			            	alert("You can not remove this work area, already payment initiated.");
			 				$("#"+recordType + workAreaId + "val").prop("checked", true);
			 				flag="false";
			 				return false;
			            }
		            
			            
			            var workAreaForDB=$("#"+recordType+workAreaId+"WOID").val()==undefined?"":$("#MATERIAL"+workAreaId+"WOID").val();
					    var workIssueDetailsId=$("#"+recordType+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val()==undefined?"":$("#MATERIAL"+workAreaId+"OLD_QS_WO_ISSUE_DTLS_ID").val();
			            var seletedWorkArea= $("#"+recordType+workAreaId+"ALLOCATE_AREA").val()==undefined?"":$("#MATERIAL"+workAreaId+"ALLOCATE_AREA").val();
			            if(recordType=="MATERIAL")
			   	  		if(workAreaForDB.length!=0&&workIssueDetailsId.length!=0&&seletedWorkArea.length!=0){
			   				var data ={inputBoxWorkAreaGroupId:workAreaId,workAreaGroupId:workAreaForDB,tempIssueAreaDetailsId:workIssueDetailsId,workAreaId: workAreaForDB,selectedArea:seletedWorkArea};
			   			 	sendDataToBackEnd.push(data);	
			   			}
		 		});
		 }else if(typeOfWork=="NMR"){
			 var previousQty=0;
			 var PREVQTY=$("#nmrRowPaymentInitiatedDetils"+rowId).val();
			 try {
				var index=PREVQTY.search("@@");
				if(PREVQTY.length!=0){
					if(index>=0){
						prevAreaQuantity=PREVQTY.split("@@");
						for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
							let	array_element = prevAreaQuantity[ind].split("##");
							var noOfWorker=parseFloat(array_element[0]);
							previousQty=previousQty+(parseFloat(noOfWorker));
						}
					}else{
						prevAreaQuantity = PREVQTY.split("##");
						previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]) / 8);
					}
				}
			} catch (e) {
				console.log(e);
			}
			
			var quantity=$("#Quantity"+rowId).val();
			if(previousQty!=0){
				alert("You can not remove this work area, already payment initiated.");
				$("#TotalAmountId"+rowId).val($("#actualTotalAmount"+rowId).val());
				$("#AcceptedRate"+rowId).val($("#actualAcceptedRate"+rowId).val());
				$("#Quantity"+rowId).val($("#actualQuantity"+rowId).val());
				flag="false";
				return false;
			}
		 }
		 if(flag=="false"){
				return false;
		  }
		 if(revision!="0"||revision!=0){
		  if(sendDataToBackEnd.length!=0){
				var canIcloseModelPopup= ajaxCallForValidatingWO_Material(rowId,sendDataToBackEnd,'removeRow')
				if(canIcloseModelPopup==false){
					flag=="false"
					return false;
				}
		  }
		 }
		 
		 
		 if(flag=="false"){
			  return false;
	    }
     
		 //var rows = document.getElementById("indentIssueTableId").getElementsByTagName("tr").length; 
          var rows = $(".workorderrowcls").length;
          var strikeoutlength = $(".strikeout").length;
          if(strikeoutlength==(rows-1)){
             alert("This work order row can't be deleted.");
             return false;
          }
         
	      $("#tr-class"+rowId).addClass('strikeout');
	      $("#addremoveItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
	      $("#editItem"+rowId).attr("disabled", true).css('cursor','not-allowed');
	      $("#addNewItemBtnId"+rowId).attr("disabled", true).css('cursor','not-allowed');
	      $('#snoDivId'+rowId).removeAttr('id');  
	      $('#combobox'+rowId).removeAttr('id');
	      $('#comboboxsubProd'+rowId).removeAttr('id');
	      $('#comboboxsubSubProd'+rowId).removeAttr('id');
	      $('#UnitsOfMeasurementId'+rowId).removeAttr('id');
	      $('#QuantityId'+rowId).removeAttr('id');
	      document.getElementById("isDelete"+rowId).value="d";
	  	  var sumofTotalAmount=0;
	   	  $(".workorderrowcls").each(function(){
			var currentId=$(this).attr("id").split("tr-class")[1];	
			debugger;
			if($("#isDelete"+currentId).val()!="d"){
				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
			}
		});
	   	$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
	   	$("#sumofTotalAmount").val(sumofTotalAmount.toFixed(2));
      }
    
     function validateMajorHeadTable(buttonValue){
     debugger;
    	 var error=true;
    	 var typeOfWork=$("#typeOfWork").val();
    	 var workOrderStatus=$("#workOrderStatus").val(); 
    	 if(buttonValue!=undefined){
    	 	  $("#isSaveOrUpdateOperation").val(buttonValue);
    	 	  //if button value is submit and work order status is Modify Work Order then change the submission method dynamically
    	 	  
	         /*var materialWoAmount=0;
	 		   var laborWoAmount=0;
	 		   var actualmaterialWoAmount=0;
	 		   var actuallaborWoAmount=0;
	 			$(".workorderrowcls").each(function(){
	 				var currentId=$(this).attr("id").split("tr-class")[1];	
	 				laborWoAmount +=parseFloat($("#LaborAmountId"+currentId).val()==""?0:$("#LaborAmountId"+currentId).val());
	 				materialWoAmount+=parseFloat($("#MaterialAmountId"+currentId).val()==""?0:$("#MaterialAmountId"+currentId).val());
	 				actuallaborWoAmount +=parseFloat($("#actualLaborAmountId"+currentId).val()==""?0:$("#actualLaborAmountId"+currentId).val());
	 				actualmaterialWoAmount+=parseFloat($("#actualMaterialAmountId"+currentId).val()==""?0:$("#actualMaterialAmountId"+currentId).val());
	 			});*/
     	 	  
    	 	  if(buttonValue=="Modify WorkOrder"&&"ModifyWorkOrder"==workOrderStatus){
    	 		 /*if(laborWoAmount==actuallaborWoAmount&&materialWoAmount==actualmaterialWoAmount){
 	 				alert("No changes has made, this work order cannot be Modify.");
 	 				return false;
 	 			}*/
    	 		 $("#finalWOSubmission").attr("onclick","saveNewWorkOrderRecords()");
    	 	  }
        }
    	 if(typeOfWork!="NMR"){
    		var woBillBilledAmount=parseFloat($("#woBillBilledAmount").val());
    		var sumofTotalAmount=0;
 		   	$(".workorderrowcls").each(function(){
 		   		debugger;
 		   		var currentId=$(this).attr("id").split("tr-class")[1];	
 				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
 			});
 		 	if(woBillBilledAmount>sumofTotalAmount){
		  		alert("can not revise work order it's seems your decresing wo amount that billed amount");
		  		return false;
		  	}
    		 
    		 $("#myModal-showeorkorder").modal("show"); 
    	   	 return true;
    	 }
    	 $("[name^=dispplayedRows]").each(function () {
	      	var id=$(this).val();
	        if($("#Quantity"+id).val()==''||$("#Quantity"+id).val()=='0'||$("#Quantity"+id).val()=='0.00'){
 				alert("Please enter quantity");
 				$("#Quantity"+id).focus();
 				return error=false;;
 			}

 			if($("#AcceptedRate"+id).val()==''||$("#AcceptedRate"+id).val()=='0'||$("#AcceptedRate"+id).val()=='0.00'){
 				alert("Please enter accepted rate");
 				$("#AcceptedRate"+id).focus();
 				return error=false;;
 			}
	      }); 
    	 
    	 
    	 $("[name^=dispplayedRows]").each(function () {
 	      	var rownum=$(this).val();
 	      	var sumofTotalAmount=0;
		   	var sumofActualTotalAmount=0;
		   	$(".workorderrowcls").each(function(){
		   		debugger;
		   		var currentId=$(this).attr("id").split("tr-class")[1];	
				sumofTotalAmount+=parseFloat($("#TotalAmountId"+currentId).val());
				sumofActualTotalAmount+=parseFloat($("#actualTotalAmount"+currentId).val());
			});
		   	
		   	//$("#WoGrandTotalAmount").text(sumofTotalAmount.toFixed(2));
		   	$("#sumofTotalAmount").val(sumofTotalAmount.toFixed(2));
			debugger;
			var totalBOQAmount=$("#TotalBOQAmount").val()==""?0:parseFloat($("#TotalBOQAmount").val());
			var totalNMRWOInitiatedAmount=$("#TotalNMR_WO_initiatedAmount").val()==""?0:parseFloat($("#TotalNMR_WO_initiatedAmount").val());
			var initiateAmount=totalBOQAmount-totalNMRWOInitiatedAmount;
			 if(typeOfWork=="NMR"){
				if(sumofTotalAmount>initiateAmount){
					if((totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount))>=parseFloat(sumofTotalAmount)){
						return true;
					}
					alert("Total NMR BOQ Amount "+(totalBOQAmount)+" Initiated Amount is "+(totalNMRWOInitiatedAmount-sumofActualTotalAmount)+" you can initiate now = "+(totalBOQAmount-(totalNMRWOInitiatedAmount-sumofActualTotalAmount)));
					$("#TotalAmountId"+rownum).val($("#actualTotalAmount"+rownum).val());
					$("#AcceptedRate"+rownum).val($("#actualAcceptedRate"+rownum).val());
					$("#Quantity"+rownum).val($("#actualQuantity"+rownum).val());
					$("#Quantity"+rownum).attr("readonly",false);
					$("#AcceptedRate"+rownum).attr("readonly",false);
				/*	$("#Quantity"+rownum).focus();
					$("#Quantity"+rownum).focusout();*/
					return error=false;
				}
			}
 	      }); 
    	 
    	  if(error!=false){
    			$("#myModal-showeorkorder").modal("show"); 
    	  }
    		
    	  $(".workorderacceptedRate").each(function(){
 				 
			var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
			$(this).val(currentvalue.toFixed(2));
		  });
				
		  $(".workordertotalqty").each(function(){
			 var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
			 $(this).val(currentvalue.toFixed(2));
		  });
		  
		  $(".totalRowamount").each(function(){
			 var currentvalue=$(this).val()==""?0:parseFloat($(this).val());
			 $(this).val(currentvalue.toFixed(2));
		  });
    		
    		return error;
    	}
     
      //for NMR Bills Method
	   function loadWOAmtAndQTY(childProdId,rownum){
			childProdId = $("#comboboxsubSubProd"+rownum).val().split("$")[0];
			var mesumentId=$("#UOMId"+rownum).val().split("$")[0];
			
			var url = "loadScopeOfWork_AmountAndQty.spring?childProductId="+childProdId+"&mesumentId="+mesumentId+"&typeOfWork=NMR";
			  $.ajax({
				  url : url,
				  type : "get",
				  contentType : "application/json",
				  success : function(data) {
					var array=new Array();
					array=data.split("@@");
					var quantity=array[1];
					var amount=array[2];
					var totalBOQAmount=array[3];
					var totalNMRWOInitiatedAmount=array[4];
					$("#TotalBOQAmount").val(totalBOQAmount);
					$("#TotalNMR_WO_initiatedAmount").val(totalNMRWOInitiatedAmount);
				}
			  });
				$(".overlay_ims").hide();
				$(".loader-ims").hide();
		}
