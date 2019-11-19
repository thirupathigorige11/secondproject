

function approveTaxInvoice(){
	   debugger;
	   var checkIsAttachmentExistOrNot="checkIsAttachmentExistOrNot";
	   var countRecords=0;
	   /*
		  var status=validateInvoiceRowData(0,checkIsAttachmentExistOrNot);
		  if(status==false){
		  	return false
		  }
	   	*/
	   var countRecords=$(".taxInvoiceCheckBox:checked").length;
	   if(countRecords==0){
		   alert("Please select at least one invoice to approve.");
		   return false;
	   }
	   
	   	var canISubmit = window.confirm("Do you want to Submit?");	     
	   	if(canISubmit == false) { 
	           return false;
	   	}
	    $.each($(".taxInvoiceCheckBox:checked"), function(){         
			//assigning values to check box if check box is checked, this will be used in back end for condition
			$(this).val("Approve");
	   });

	   	document.getElementById("approveInvoiceId").disabled = true;   
	    document.getElementById("rejectInvoiceId").disabled = true;   
	   	
	    document.getElementById("submitInvoicesFormId").action = "approveTaxInvoices.spring";
	    document.getElementById("submitInvoicesFormId").method = "POST";
	    document.getElementById("submitInvoicesFormId").submit();
	   	
}


function rejectTaxInvoice(){
	var countRecords=0
	debugger;
	  var countRecords=$(".taxInvoiceCheckBox:checked").length;
	  if(countRecords==0){
		   alert("Please select at least one invoice to reject.");
		   return false;
	   }
   
   
   	var canISubmit = window.confirm("Do you want to Submit?");	     
   	if(canISubmit == false) { 
           return false;
   	}
   	
   	$.each($(".taxInvoiceCheckBox:checked"), function(){         
   		//assigning values to check box if check box is checked, this will be used in back end for condition
   		$(this).val("Reject");
	});
   	
   	document.getElementById("approveInvoiceId").disabled = true;   
    document.getElementById("rejectInvoiceId").disabled = true;   
   	
   	//approveTaxInvoices.spring
    document.getElementById("submitInvoicesFormId").action = "rejectTaxInvoices.spring";
    document.getElementById("submitInvoicesFormId").method = "POST";
    document.getElementById("submitInvoicesFormId").submit();
}

function validateInvoiceRowData(rowNum,isValidInvoiceToSave){
	var error=true;
	$(".submitInvoiceRows").each(function(){
		debugger;
		var rowNum=$(this).attr("id").split("submitInvoiceRowsId")[1];
		if($("#VendorNameId"+rowNum).val()==''){
			alert("Please enter vendor name.");
			$("#VendorNameId"+rowNum).focus();
			return error=false;
		}
		if($("#invoiceNumber"+rowNum).val()==''){
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
			}
		}
	});
	return error;
}
