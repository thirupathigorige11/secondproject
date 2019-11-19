//holding data of vendor name
var vendorData="";


function appendRow(row){
	var htmlData="";
	var popupData="";
	var rowsCount=parseInt($(".submitInvoiceRows:last").attr("id").split("submitInvoiceRowsId")[1]);
	var rowNum=rowsCount+1;
	var status=validateInvoiceRowData(row,"");
	if(status==false){
		return false;
	}

	
	debugger;
	htmlData+='<tr class="submitInvoiceRows" id="submitInvoiceRowsId'+(rowNum)+'">';
	htmlData+='<td><input type="text" name="vendorName'+rowNum+'" id="VendorNameId'+rowNum+'" class="form-control text-center" onkeyup="loadVendorData(this.id,this.name,this.value,'+rowNum+')"  placeholder="vendor name"  autocomplete="off">';
	htmlData+='<input type="hidden" name="vendorId'+rowNum+'" id="vendorId'+rowNum+'">';
	htmlData+='<input type="hidden" name="VendorAddress'+rowNum+'" id="VendorAddress'+rowNum+'">';
	htmlData+='<input type="hidden" name="GSTINNumber'+rowNum+'" id="GSTINNumber'+rowNum+'">';
	htmlData+='<input type="hidden" name="isValidInvoiceToSubmit'+rowNum+'" id="isValidInvoiceToSubmit'+rowNum+'">';
	htmlData+='<input type="hidden" name="recordsToproceed" id="recordsToproceed'+rowNum+'" value="'+rowNum+'"></td>';
	
	htmlData+='<td>';
	htmlData+='<select name="invoiceNumber'+rowNum+'" id="invoiceNumber'+rowNum+'" class="form-control text-center"  style="width: 140px;display: inline-block;padding: 0px;"  onchange="assignValuesToFields(this.id, '+(rowNum)+')"><option value="">Select invoice number</option></select><input type="hidden" name="indentEntryId'+rowNum+'" id="indentEntryId'+rowNum+'">';
	htmlData+='<a id="showLink'+rowNum+'"  target="_blank"  title="click here to see invoice"  style="display: none;margin-bottom: 7px;height: 28px;">Click Here</a> <button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal'+rowNum+'" id="showLinkButton'+rowNum+'" style="display: none;margin-bottom: 7px;height: 28px;"><i class="fa fa-question-circle  red-tooltip " data-toggle="tooltip" data-placement="auto" id="WDDwtailsTable1" title=""></i></button></td>';
	htmlData+='<td><input type="text" name="invoiceAmount'+rowNum+'" id="invoiceAmount'+rowNum+'"	class="form-control text-center" placeholder="invoice amount" readonly></td>';
	htmlData+='<td><input type="text" name="invoiceDate'+rowNum+'" id="invoiceDate'+rowNum+'"	class="form-control text-center" placeholder="invoice date" readonly></td>';
	htmlData+='<td><input type="text" name="remarks'+rowNum+'" id="remarks'+rowNum+'"	class="form-control" placeholder="remarks"></td>';
	htmlData+='<td>';
	htmlData+='<button type="button" name="addNewItemBtn" value="Add New Item" id="addNewItemBtnId'+rowNum+'" onclick="appendRow('+(rowNum)+')" class="btnaction"><i class="fa fa-plus"></i></button>';
	htmlData+='<button type="button" name="addDeleteItemBtn" value="Delete Item" id="addDeleteItemBtnId'+rowNum+'" onclick="deleteRow(this, '+(rowNum)+')" class="btnaction"  style="margin-left: 3px;"><i class="fa fa-trash"></i></button>';
	htmlData+='</td>';
	htmlData+='</tr>';
	
	popupData+='<div class="modal fade" id="myModal'+rowNum+'" role="dialog"><div class="modal-dialog modal-sm">';
	popupData+='<div class="modal-content"><div class="modal-header"> <button type="button" class="close" data-dismiss="modal">&times;</button>';
	popupData+='<h4 class="modal-title text-center">Invoices</h4></div>';
	popupData+='<div class="modal-body"  id="HyperLinksForInvoice'+rowNum+'" style="height:250px;overflow-y: scroll;"></div><div class="modal-footer"></div>';
	popupData+='</div></div></div>"';
	
	$("#popupForInvoiceHyperLink").append(popupData);
	
	$("#addNewItemBtnId"+(row)).hide();
	$("#addDeleteItemBtnId"+(row)).show();
 
	
	$("#submitInvoiceRowsId"+(rowsCount)).after(htmlData);
}
function deleteRow(event,row){
	debugger;
	var rowsCount=parseInt($(".submitInvoiceRows:last").attr("id").split("submitInvoiceRowsId")[1]);
	if(row==rowsCount){
		alert("This row can not be deleted.");
		return false;
	}
	$("#submitInvoiceRowsId"+row).remove();
}

function submitForm(){
	   debugger;
	   var checkIsAttachmentExistOrNot="checkIsAttachmentExistOrNot";
	   var status=validateInvoiceRowData(0,checkIsAttachmentExistOrNot);
	   if(status==false){
	   	return false
	   }
	   var canISubmit = window.confirm("Do you want to Submit?");	     
	   if(canISubmit == false) { 
	    return false;
	   }
	   $("#submitInvoicesId").attr("disabled",true);
	   document.getElementById("submitInvoicesFormId").action =	"saveTaxInvoices.spring";
		document.getElementById("submitInvoicesFormId").method = "POST";
		document.getElementById("submitInvoicesFormId").submit();
}




function validateInvoiceRowData(rowNum,isValidInvoiceToSave){
	var error=true;
	debugger;
//	$(".submitInvoiceRows").each(function(){
	$.each($(".taxInvoiceCheckBox"), function(){       
		var rowNum=$(this).attr("id").split("submitTaxInvoiceCheckBox")[1];
		$("#recordsToproceed"+rowNum).removeAttr("name");
	});
	var lengthOfTaxInvoice=$(".taxInvoiceCheckBox:checked").length;
	if(lengthOfTaxInvoice==0){
		alert("Please select at least one tax invoice.");
		return false;
	}
	$.each($(".taxInvoiceCheckBox:checked"), function(){       
		debugger;
		var rowNum=$(this).attr("id").split("submitTaxInvoiceCheckBox")[1];
		if($("#VendorNameId"+rowNum).val()==''){
			alert("Please enter vendor name.");
			$("#VendorNameId"+rowNum).focus();
			return error=false;
		}
		if($("#invoiceNumber"+rowNum).val()==''||$("#invoiceNumber"+rowNum).val()==null){
			alert("Please enter invoice number.");
			$("#invoiceNumber"+rowNum).focus();
			return error=false;
		}
		//this condition will be executed only form submit time 
		if(isValidInvoiceToSave.length!=0){
			if($("#isValidInvoiceToSubmit"+rowNum).val()==''||$("#isValidInvoiceToSubmit"+rowNum).val()=='false'){
				alert("No attachments found for invoice number \""+$("#invoiceNumber"+rowNum).val().split("@@")[0]+"\", Invoice can not be submited.");
				$("#invoiceNumber"+rowNum).focus();
				return error=false;
			}else{
				$("#recordsToproceed"+rowNum).attr("name","recordsToproceed");
				$("#recordsToproceed"+rowNum).val(rowNum);
			}
		}
	});
	return error;
}

function loadVendorData(id,name,value,rowNum){
	//if(vendorData.length==0){
	debugger;
	var vendorName=$("#"+id).val();
		$.ajax({
			url : "./autoSearchvendor.spring",
			type : "POST",
			data : {
				term : vendorName
			},
			dataType : "json",
			success : function(data) {
				debugger;
				vendorData=data;
				$("#VendorNameId"+rowNum).autocomplete({
			  		source : data,
			  		 change: function (event, ui) {
			              if(!ui.item){
			                   // The item selected from the menu, if any. Otherwise the property is null
			                   //so clear the item for force selection
			                   $("#VendorNameId"+rowNum).val("");
			               }
			            },
			  		 select: function (event, ui) {
			                  AutoCompleteSelectHandler(event, ui,rowNum);
			              }
				  	});
			},
				error: function(data, status, er){
					alert(data+"_"+status+"_"+er);
				}
		});
	/*}else{
		debugger;
		$("#VendorNameId"+rowNum).autocomplete({
	  		source : vendorData,
	  		 change: function (event, ui) {
	              if(!ui.item){
	                   // The item selected from the menu, if any. Otherwise the property is null
	                   //so clear the item for force selection
	                   $("#VendorNameId"+rowNum).val("");
	               }
	            },
	  		 select: function (event, ui) {
                   AutoCompleteSelectHandler(event, ui,rowNum);
               }

	  	});
	}*/
}
  
function AutoCompleteSelectHandler(event, ui,rowNum){    
    var selectedObj = ui.item;       
    isTextSelect="true";
	$("#invoiceNumber"+rowNum).html("");
    var vendorName=selectedObj.value;
    debugger;
	var url = "loadAndSetVendorInfo.spring?vendName="+vendorName;
	 $.ajax({
		  url : url,
		  type : "POST",
		  contentType : "application/json",
		  success : function(resp) {
			debugger;
			  var vendorDataArray=resp.split("|");
			  var vendorId = vendorDataArray[0];
			  var vendorAddress = vendorDataArray[1];
			  var vendorGsinNo = vendorDataArray[2];
				
			  $("#vendorId"+rowNum).val(vendorId);
			  $("#VendorAddress"+rowNum).val(vendorAddress);
			  $("#GSTINNumber"+rowNum).val(vendorGsinNo);
			  //loading invoice number related to vendor name
			  loadInvoiceNumberByVendorId(vendorId,vendorName,rowNum);
		  },
		  error:  function(data, status, er){
			  debugger;
			  alert(data+"_"+status+"_"+er);
			 }
		  });
}


function loadInvoiceNumberByVendorId(vendorId,vendorName,rowNum){
	debugger;
	var site_id=$("#site_id").val();
	var url = "loadInvoiceNumberForTaxSubmit.spring";
			 
	 $.ajax({
		  url : url,
		  type : "GET",
		  data:{
			  	vendorId:vendorId,
			  	site_id:site_id
			  },
		  success : function(resp) {
			  	var invoiceSelectBoxData="<option value=''>Select invoice number</option>";
			  	//var invoiceHyperLink="<table class='table table-new'><thead class='subtaxInvoicethead'><tr><td>Invoice Number</td></tr></thead><tbody>";
			 
			  	$.each(resp,function(key, value) {
			  	 //	debugger;
			  	 	invoiceSelectBoxData+="<option value='"+value.INVOICE_ID+"@@"+value.INVOICE_DATE+"@@"+value.TOTAL_AMOUNT+"@@"+value.INDENT_ENTRY_ID+"@@"+value.INDENT_TYPE+"'>"+value.INVOICE_ID+"</option>";
			  	 //	invoiceHyperLink+='<tr><td class="text-center"><a target="_blank" href="getInvoiceDetails.spring?invoiceNumber='+value.INVOICE_ID+'&vendorName='+vendorName+'&vendorId='+vendorId+'&IndentEntryId='+value.INDENT_ENTRY_ID+'&SiteId='+site_id+'&invoiceDate='+value.INVOICE_DATE+'&indentType='+value.INDENT_TYPE+'" class="anchor-class">'+value.INVOICE_ID+'</a></td></tr>';
			  	});
			  	$("#invoiceNumber"+rowNum).html(invoiceSelectBoxData);
			  	//invoiceHyperLink+="</tbody></table>";
			  	
			  	/*$("#WDDwtailsTable"+rowNum).attr("title", invoiceHyperLink);
				$("#WDDwtailsTable"+rowNum).tooltip();		*/
				//$("#HyperLinksForInvoice"+rowNum).html(invoiceHyperLink);
			
		  },
		  error:  function(data, status, er){
			  debugger;
			  alert(data+"_"+status+"_"+er);
			 }
		  });
}
//invoice number duplicate changes written by thirupathi
function invoiceCampare(rowNum){
	debugger;
	var invoiceNumber=$("#invoiceNumber"+rowNum).val();
	var vendorId=$("#vendorId"+rowNum).val();
	var tablelength=$(".submitInvoiceRows").length;	
	if(tablelength>1){
		debugger;
		jQuery('.submitInvoiceRows').each(function() {
			  var currentElement = parseInt($(this).attr("id").split("submitInvoiceRowsId")[1]);
			  
			  var currentInvoiceNumber = $("#invoiceNumber"+currentElement).val();
			  var currentVendorId=$("#vendorId"+currentElement).val();
			  if(currentInvoiceNumber==invoiceNumber&&currentVendorId==vendorId&&rowNum!=currentElement){
				alert("This invoice number is already exist, Please choose different invoice number.");
				$("#invoiceNumber"+rowNum).val("");
				return rv = false;
			   }     
			  else{
				  
				  return rv = true;
				  
			  }
		});
		 return rv;
	}
}
function checkAllCheckBox(){
	debugger;
	var error=true;
	if($("#checkAll").is(":checked")==false){
		$(".taxInvoiceCheckBox").prop('checked', false);
		return false;
	}
	$.each($(".taxInvoiceCheckBox"), function(){       
		var rowNum=$(this).attr("id").split("submitTaxInvoiceCheckBox")[1];
		$(".taxInvoiceCheckBox").prop('checked', true);
		var status=checkAttachmentForInvoice($(this).attr("id"),rowNum,"")
		if(status==false){
		 return false
		}
		
	});
	return error;
}
function checkAttachmentForInvoice(id,rowNum,value){
 	debugger;
 	var error=true;
 	/*var result=invoiceCampare(rowNum);
	if(result==false){
		return false;
	}
 	
 	var values=$("#"+id).val().split("@@");
 	var invoiceNumber=values[0];
 	var invoiceDate=values[1];
 	var invoiceAmount=values[2];
 	var indentEntryId=values[3];
 	var indentType=values[4];
 	if(invoiceNumber==""){
 		return false;
 	}*/
 	
 	if($("#"+id).is(":checked")==false){
 		$("#"+id).prop("checked", false);
 		return false;
 	}
 	
 	var invoiceNumber=$("#invoiceNumber"+rowNum).val();
	var indentEntryId=$("#indentEntryId"+rowNum).val();
 	var invoiceDate=$("#invoiceDate"+rowNum).val();
 	var invoiceAmount=$("#invoiceAmount"+rowNum).val();
 	var vendorId=$("#vendorId"+rowNum).val();
 	var vendorName=$("#VendorNameId"+rowNum).val();
	var site_id=$("#site_id").val();
	
	
	//var html='<a target="_blank" href="getInvoiceDetails.spring?invoiceNumber='+invoiceNumber+'&vendorName='+vendorName+'&vendorId='+vendorId+'&IndentEntryId='+indentEntryId+'&SiteId='+site_id+'&invoiceDate='+invoiceDate+'&indentType='+indentType+'" class="anchor-class">'+invoiceNumber+'</a>';
	$("#showLink"+rowNum).show();
	//$("#showLink"+rowNum).attr("href",'getInvoiceDetails.spring?invoiceNumber='+invoiceNumber+'&vendorName='+vendorName+'&vendorId='+vendorId+'&IndentEntryId='+indentEntryId+'&SiteId='+site_id+'&invoiceDate='+invoiceDate+'&indentType='+indentType+'')
 	var url = "checkAttachmentForInvoice.spring";
	 $.ajax({
		  url : url,
		  type : "GET",
		  data : {
			  indentEntryId:indentEntryId,
			  invoiceNumber:invoiceNumber,
			  vendorId:vendorId,
			  vendorName:vendorName,
			  site_id:site_id
		  },
		  contentType : "application/json",
		  success : function(resp) {
			  debugger;
			  if(resp=="false"){
				  alert("No attachments found for invoice number \""+invoiceNumber+"\",Tax invoice can not be submited.");
				  $("#isValidInvoiceToSubmit"+rowNum).val(resp);
				  $("#submitTaxInvoiceCheckBox"+rowNum).prop('checked', false);
				  $("#"+id).focus();
				//  return false;
			  }else{
				  $("#isValidInvoiceToSubmit"+rowNum).val(resp);
			  }
		  },
		  error:  function(data, status, er){
			  debugger;
			  alert(data+"_"+status+"_"+er);
			 }
		  });
 	 }