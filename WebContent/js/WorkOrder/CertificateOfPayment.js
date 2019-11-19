//this for multiple select option
$(document).ready(function (){
	$("#typeofworkselect").select2();
});
//to open calender when you click on calender icon 
function openCalender(){
	$("#billDate").focus();
}
(function( $ ) {
    $.widget( "custom.combobox", {
      _create: function() {
        this.wrapper = $( "<span>" )
          .addClass( "custom-combobox" )
          .insertAfter( this.element );
 
        this.element.hide();
        this._createAutocomplete();
        this._createShowAllButton();
      },
 
      _createAutocomplete: function() {
        var selected = this.element.children( ":selected" ),
          value = selected.val() ? selected.text() : "";
 
        this.input = $( "<input>" )
          .appendTo( this.wrapper )
          .val( value )
          .attr( "title", "" )        
          
          .attr("id", this.element[0].name)
          //Enable below line to get input box background color - Start
          //.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
          //Enable below line to get input box background color - End
          .autocomplete({
            delay: 0,
            minLength: 0,
            source: $.proxy( this, "_source" )
          })
          .tooltip({
            tooltipClass: "ui-state-highlight"
          });
 
        this._on( this.input, {
          autocompleteselect: function( event, ui ) {
            ui.item.option.selected = true;           
            
            var workDesc = "";         
            var ele = this.element[0].name;            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');        
			             
           if(str1 == 'workDesc') {
        	  var workOrderId = ui.item.option.value;              
              loadWorkOrderNo(workOrderId);
            }    
          
            this._trigger( "select", event, {
              item: ui.item.option
            });
          },
          autocompletechange: "_removeIfInvalid"
        });
      },
      
      
 	//Enable below code to create Show All Button - Start
        _createShowAllButton: function() {
        var input = this.input,
          wasOpen = false;
 
        $( "<a>" )
          .attr( "tabIndex", -1 )
          .attr( "title", "Show All Items" )
          .tooltip()
          .appendTo( this.wrapper )
          .button({
            icons: {
              primary: "ui-icon-triangle-1-s"
            },
            text: false
          })
          .removeClass("ui-corner-all" )
          .addClass("custom-combobox-toggle ui-corner-right")
          .mousedown(function() {
            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
          })
          .click(function() {
            input.focus();
 
            // Close if already visible
            if ( wasOpen ) {
              return;
            }
 
            // Pass empty string as value to search for, displaying all results
            input.autocomplete( "search", "" );
          });
      },  
    //Enable below code to create Show All Button - End 
 
      _source: function( request, response ) {
        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
        response( this.element.children( "option" ).map(function() {
          var text = $( this ).text();
          if ( this.value && ( !request.term || matcher.test(text) ) )
            return {
              label: text,
              value: text,
              option: this
            };
        }) );
      },
 
      _removeIfInvalid: function( event, ui ) {
 
        // Selected an item, nothing to do
        if ( ui.item ) {
          return;
        }
 
        // Search for a match (case-insensitive)
        var value = this.input.val(),
          valueLowerCase = value.toLowerCase(),
          valid = false;
        this.element.children( "option" ).each(function() {
          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
            this.selected = valid = true;
            return false;
          }
        });
 
        // Found a match, nothing to do
        if ( valid ) {
          return;
        }
        
        // Remove invalid value
        this.input
          .val( "" )
          .attr( "title", value + " didn't match any item" )
          .tooltip( "open" );
        this.element.val( "" );
        this._delay(function() {
          this.input.tooltip( "close" ).attr( "title", "" );
        }, 2500 );
        //this.input.autocomplete( "instance" ).term = "";
      }, 
      _destroy: function() {
        this.wrapper.remove();
        this.element.show();
      }
      
    });
  })( jQuery ); 

  $(function() {
    $("#combobox1").combobox();    
    $( "#toggle").click(function() {
      $( "#combobox1").toggle();
    });
  });
  $(function() {
		$( "#comboboxworkOrderNo").combobox();
  });
	  
 
  //loading work order number
  function loadWorkOrderNo(){
		
	  var contractorId = document.getElementById("ContractorId").value;	
	  var typeOfWork=$("#typeOfWork").val();
	  if(typeOfWork=='WOB'){
		  typeOfWork  ='\'PIECEWORK\'';
	  }else if(typeOfWork=='WOB_CONSULTANT'){
		  typeOfWork ='\'CONSULTANT\'';
	  }
	  var siteId = document.getElementById("siteId").value;
	  var url = "getWorkOrderNo.spring?workDescId=0&siteId="+siteId+"&contractorId="+contractorId+"&typeOfWork="+typeOfWork;

	  var request = getAjaxObject();
		
		try {
			request.onreadystatechange = function() {
				
				if(request.readyState == 4 && request.status == 200) {
	        
					var resp = request.responseText;
					resp = resp.trim();			

			    	var spltData = resp.split("|");
			    	available = new Array();
			    	for(var j=0; j<spltData.length; j++) {
			    		available[j] = spltData[j];
			    	}
					
			    	var subProdToSet = "workOrderNo";
			    	var selectBox = document.getElementById(subProdToSet);
				 
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
				    		var prodIdAndName = data[0]+"$"+data[1]+"$"+data[2];	    		
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
//this method for loading contractor names autocomplete
function populateData(){	
var contName=$("#ContractorName").val();
 var url = "loadAndSetContractorInfo.spring?contractorName="+contName;
  $.ajax({
  url : url,
  type : "get",
  Cdata : "",
  contentType : "application/json",
  success : function(data) {
	  Cdata = data.split("@@");
	  
	  var resultArray = [];
	  for(var i=0;i<Cdata.length;i++){
	      resultArray.push(Cdata[i].split("@@")[0]);
	  }
	   //attach autocomplete
	  $("#ContractorName").autocomplete({
	  		source : resultArray,
	  		 autoFocus:true,
	  		 change: function (event, ui) {
	                if(!ui.item){
	                    // The item selected from the menu, if any. Otherwise the property is null
	                    //so clear the item for force selection
	                    $("#contractorName").val("");
	                }
	            },
	  		 select: function (event, ui) {
		          	AutoCompleteSelectHandler(event, ui);
		     }
	  	});
  },
  error:  function(data, status, er){
	  alert(data+"_"+status+"_"+er);
	  }
  });
};
//code for selected text contractor name
function AutoCompleteSelectHandler(event, ui){               
    var selectedObj = ui.item;       
    isTextSelect="true"; 
    $("#workOrderNo").val("");
    $("#totalAmtToPay").val("");
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

				var contractorData=data[0].split("@@");
				var contractorId=contractorData[0];
			
				$("#ContractorId").val(contractorId);
				$("#MobileNo").val(contractorData[3]);
				$("#PanCardNo").val(contractorData[4]);
				$("#AccountNo").val(contractorData[5]);
				$("#ifscCode").val(contractorData[6]+" & "+contractorData[8]);
				$("#contractorGSTIN").val(contractorData[7]);
				//contractorData[8] GSTIN number
				if(contractorData[6]=="-"){
					$("#ifscCode").val(contractorData[8]);	
				}else if ( contractorData[8]=="-"){
					$("#ifscCode").val(contractorData[6]);	
				}else if(contractorData[6]=="-" && contractorData[8]=="-"){
					$("#ifscCode").val("-");
				}else{
					$("#ifscCode").val(contractorData[6]+" & "+contractorData[8]);
				}
				
				loadWorkOrderNo();
			  }
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
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

function validateAdvBill() { }
function validatePayment(releaseAmt){}

function calculateAdvBillAmt(releaseAmt) {}

function isNum(value) {
	  var numRegex=/^[0-9.]+$/;
	  return numRegex.test(value)
}
//calculating ra bill final amount
function CurrentCertifiedamnt(){
	
	var CrntCertifiedid=$("#raAmountToPay").val();
	var totalAmtToPay=parseFloat($("#totalAmtToPay").val().replace(/,/g,''));
	var raPcAmt=$("#raPc").val();
	
	if(CrntCertifiedid==''){
		CrntCertifiedid=0;
	}
	var utstandingamnt=$("#outstandingAdvTotalAmt").val();
	var raccamnt=$("#raCc").val();
	
	var totlamtA=parseFloat(CrntCertifiedid)+parseFloat(raPcAmt);
		$("#raCc").val(totlamtA.toFixed(2));
		$("#totalCc").val(CrntCertifiedid);
		$("#raTotalPc").val(raPcAmt);
		$("#raTotalCc").val(totlamtA.toFixed(2));
		
		
		//displaying final Amount
		
		var totalAmountA=parseFloat($("#totalCc").val());
		if(totalAmountA==""){
			totalAmountA=0;
		}
		var totalamntB=parseFloat($("#totalAmtCurntDeduc").val());
		var finalTotalAmount=totalAmountA-totalamntB;
		$("#finalAmt").val(finalTotalAmount);
		debugger;
		if(parseInt($("#finalAmt").val())!=0){
			var amountInWords=	convertNumberToWords(finalTotalAmount);
			$("#amountInWords").text(amountInWords);
		}
		if(parseInt($("#finalAmt").val())<0 ){
			$("#amountInWords").text("");
		}
		if(parseInt($("#finalAmt").val())==0){
			$("#amountInWords").text("Zero Rupees Only");
		}
		
		
		var finalamnt=$("#totalAmtPreviousCertified").val();
		var raccdedamnt=$("#raCcDeductionAmthidden").val();debugger;
		if($("#finalAmt").val()<0){
			alert("You can not initiate more than Certified Amount.");
			$("#finalAmt").val('0')
			$("#totalAmtCumulativeCertified").val("0.00");
			return false;
		}
		//$("#raCcDeductionAmt").val(parseFloat(raccdedamnt)+parseFloat(finalamnt)+parseFloat($("#raCcDeductionAmt").val()==""?0:$("#raCcDeductionAmt").val()));
		$("#raCcDeductionAmt").val((parseFloat($("#raCcDeductionAmt").val()==""?0:$("#raCcDeductionAmt").val())).toFixed(2));
		//comparing totalAmtToPay with CrntCertified Amount
		caldeductionamt();
}
//calculating final amount when you change the Advance Deduction,Petty Expenses, Security Deposit(E) and Others
function caldeductionamt(id){
	var totalamntB=0;
	var pctotalamntB=0;
	var cctotalamntB=0;
	var value=0;
	var PettyExpensesCC=0;	
	var OthersCC=0;
	var AdvanceDeductionCC=0;
	//var Outstandingval=parseFloat($("#outstandingAdvTotalAmt").val());
	
	var Outstandingval=parseFloat($("#outstandingAdvPrevAmt").val());

	if(id=="ra"){
		var deductionamt=parseFloat($("#raDeductionAmt").val()==""?0:$("#raDeductionAmt").val());
		if(deductionamt==""){
			deductionamt=0;
		}
		if(deductionamt!=0)
		if(isNaN(deductionamt)){
			alert("Enter only digits");
			$("#raDeductionAmt").val('0');
			return false;
		}
		
		if(deductionamt==''){
			deductionamt=0;			
		}
		if(deductionamt>Outstandingval){			
			alert("This Amount is greater than OutStanding Amount.");
			$("#raDeductionAmt").val('0');
			deductionamt=0;	
		}
		var CCOutAdv=Outstandingval-deductionamt;
		$("#outstandingAdvTotalAmt").val(CCOutAdv.toFixed(2));
		var finalamnt=$("#totalAmtPreviousCertified").val();	
		if(finalamnt==""||finalamnt==undefined){
			finalamnt=0;
		}
		AdvanceDeductionCC=parseFloat($("#raPrevDeductionAmt").val())+parseFloat(deductionamt);
		$("#raCcDeductionAmt").val((AdvanceDeductionCC+parseFloat(finalamnt)).toFixed(2));
	}
	if(id=="pe"){
		changetheValue();
		var raccdedamnt=$("#raCcDeductionAmthidden").val();
		if(raccdedamnt=="" || raccdedamnt==null || raccdedamnt==undefined){
			raccdedamnt=0;
		}
		var pcamnt=$("#pettyExpensesCurrentCerti").val();
		if(pcamnt==""){
			pcamnt=0;
		}
		PettyExpensesCC=parseFloat($("#pettyExpensesPrevCerti").val())+parseFloat(pcamnt);
		$("#pettyExpensesCumulative").val(PettyExpensesCC.toFixed(2));
		var currentRacc=$("#raDeductionAmt").val();
		var finalamnt=$("#totalAmtPreviousCertified").val();
		//$("#raCcDeductionAmt").val(parseFloat(raccdedamnt)+parseFloat(finalamnt)+parseInt(currentRacc));
	}
	if(id=="ot"){
		changetheValue();
		var raccdedamnt=$("#raCcDeductionAmthidden").val();
		if(raccdedamnt=="" || raccdedamnt==null || raccdedamnt==undefined){
			raccdedamnt=0;
		}
		var otccamnt=$("#other").val();
		if(otccamnt==""){ otccamnt=0; }
		OthersCC=parseFloat($("#otherAmtPrevCerti").val())+parseFloat(otccamnt);
		$("#otherAmtCumulative").val(OthersCC.toFixed(2));
		var currentRacc=$("#raDeductionAmt").val();
		var finalamnt=$("#totalAmtPreviousCertified").val();
		//$("#raCcDeductionAmt").val(parseFloat(raccdedamnt)+parseInt(finalamnt)+parseInt(currentRacc));
	}		
	jQuery('.raDeductionAmt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){  	value=0;   }
		  totalamntB+=parseFloat(value);
	});
	
	jQuery('.PcAmnt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){ value=0;   }
		  pctotalamntB+=parseFloat(value);
	});
	jQuery('.CcAmnt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){   	value=0;   }
		  cctotalamntB+=parseFloat(value);
	});
	
	var finalamnt=$("#totalAmtPreviousCertified").val();	
	var raccdedamnt=$("#raCcDeductionAmthidden").val();
	if(id==undefined){
		var currentRacc=$("#raDeductionAmt").val();		
	}
	
	var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
	    $("#totalDeductionAmtCumulative").val((cctotalamntB).toFixed(2)); //+recoverycurrentAmount
	    $("#totalDeductionAmtPrevCerti").val(pctotalamntB.toFixed(2));
		$("#totalAmtCurntDeduc").val((totalamntB).toFixed(2));
		
		//calculating release advance
		var advanceCurrAmount=$("#advanceCurrAmount").val();
		var releaseAdvPrevAmt=$("#releaseAdvPrevAmt").val();
		if(advanceCurrAmount=="" || advanceCurrAmount=="null"){
			advanceCurrAmount=0;
		}
		if(releaseAdvPrevAmt=="" || releaseAdvPrevAmt=="null"){
			releaseAdvPrevAmt=0;
		}
		var releaseAdvTotalAmt=parseFloat(advanceCurrAmount)+parseFloat(releaseAdvPrevAmt);
		$("#releaseAdvTotalAmt").val(releaseAdvTotalAmt.toFixed(2));
		
		var ratotalCC=$("#raTotalCc").val();		
		var totalDeductionAmtCumulative=$("#totalDeductionAmtCumulative").val();
		if(ratotalCC=="" || ratotalCC=="null"){
			ratotalCC=0;
		}
		if(totalDeductionAmtCumulative=="" || totalDeductionAmtCumulative=="null"){
			totalDeductionAmtCumulative=0;
		}
		var CcFinalAmt=parseFloat(ratotalCC)-parseFloat(totalDeductionAmtCumulative)+parseFloat(releaseAdvTotalAmt);
		
		var raTotalPc=$("#raTotalPc").val();
		var totalDeductionAmtPrevCerti=$("#totalDeductionAmtPrevCerti").val();
		if(raTotalPc=="" || raTotalPc=="null"){
			raTotalPc=0;
		}
		if(totalDeductionAmtPrevCerti=="" || totalDeductionAmtPrevCerti=="null"){
			totalDeductionAmtPrevCerti=0;
		}
		var PcFinalAmt=parseFloat(raTotalPc)-parseFloat($("#totalDeductionAmtPrevCerti").val())+parseFloat(releaseAdvPrevAmt);
		if(CcFinalAmt<0){
			alert("You can't initiate more than Certified Amount.");
			$("#finalAmt").val("0.00");
			$("#totalAmtCumulativeCertified").val("0.00");
			if(id=="ra"){
				$("#raDeductionAmt").val("0");
				//$("#raCcDeductionAmt").val($("#raPrevDeductionAmt").val());
				caldeductionamt("ra");
			}
			if(id=="pe"){
				$("#pettyExpensesCurrentCerti").val("0");
				caldeductionamt("pe");
			}
			if(id=="ot"){
				$("#other").val("0");
				caldeductionamt("ot");
			}
			if(id=="recovery"){
				$("#currentRecoveryAmount").val("0");
				$("#cumulativeRecovery").val($("#previousRecovery").val());
				caldeductionamt("recovery");
			}
			
			return false;
		}
		$("#totalAmtCumulativeCertified").val((CcFinalAmt).toFixed(2));
		$("#totalAmtPreviousCertified").val(PcFinalAmt.toFixed(2));
		
		var totalAmountA=$("#totalCc").val();
		if(totalAmountA=="" || totalAmountA=="null"){
			totalAmountA=0;
		}
		//displaying final Amount		
		var finalTotalAmount=parseFloat(totalAmountA)-totalamntB+parseFloat(advanceCurrAmount);		
		$("#finalAmt").val((finalTotalAmount).toFixed(2));
		if(finalamnt=="NaN"){	
			   $("#totalAmtCumulativeCertified").val(parseFloat(CcFinalAmt.toFixed(2))-parseFloat($("#totalAmtPreviousCertified").val()));
			    $("#raCcDeductionAmt").val(parseFloat(raccdedamnt)+parseFloat($("#totalAmtPreviousCertified").val()));			  
		}
		if(finalamnt<0){
			alert("You can't initiate more than Certified Amount.");
			$("#finalAmt").val('0.00');
			$("#totalAmtCumulativeCertified").val("0.00");
			
			if(id=="ra"){
				$("#raDeductionAmt").val("0");
				//$("#raCcDeductionAmt").val($("#raPrevDeductionAmt").val());
				caldeductionamt("ra");
			}
			if(id=="pe"){
				$("#pettyExpensesCurrentCerti").val("0");
				caldeductionamt("pe");
			}
			if(id=="ot"){
				$("#other").val("0");
				caldeductionamt("ot");
			}
			if(id=="recovery"){
				$("#currentRecoveryAmount").val("0");
				$("#cumulativeRecovery").val($("#previousRecovery").val());
				caldeductionamt("recovery");
			}
			return false;
		}
		var amountInWords=	convertNumberToWords((finalTotalAmount).toFixed(2));
		if(parseInt($("#finalAmt").val())!=0){
			$("#amountInWords").text(amountInWords);
		}
		if(parseInt($("#finalAmt").val())<0 || (finalTotalAmount)<0){
			$("#amountInWords").text("");
		}
		if(parseInt($("#finalAmt").val())==0){
			$("#amountInWords").text("Zero Rupees Only");
		}
		$(".overlay_ims").hide();	
		$(".loader-ims").hide();
		
}
//security change method for calculating security amount (Abstract amount/security percentage)
function securityperchange(){
	var secperamt=$("#securityPer").val();
	if(secperamt==""){
		secperamt=0;
	}
	var totalAamt=$("#totalCc").val()==''?0:$("#totalCc").val();
	var secpervalamt=parseFloat(totalAamt)*(parseFloat(secperamt)/100);
	var secpervalamtCC=parseFloat($("#secDepositPrevCerti").val()==''?0:$("#secDepositPrevCerti").val())+secpervalamt;	
	
	$("#secDepositCurrentCerti").val(secpervalamt.toFixed(2));	
	$("#secDepositCumulative").val(secpervalamtCC.toFixed(2));
	$("#secDepositCurrentCerti").attr("readonly", true);
	caldeductionamt();	
} 
	

//for calculating ra bill 
function raCalcAmountToPay(raAmountToPay,num) {}
//validating contractor name and work order number
function validateForm(){
	var ContractorId=$("#ContractorId").val();
	var workOrderNo=$("#workOrderNo").val();
	if(ContractorId.length==0){
		alert("Please enter contractor name.");
		$(".overlay_ims").hide();	
		$(".loader-ims").hide();
		return false;
	}
	if(workOrderNo.length==0){
		alert("Please select work order number.");
		$(".overlay_ims").hide();	
		$(".loader-ims").hide();
		return false;
	}
	return true;
}
//approving ra bill
function approveRABill() {
	var status=paymentAreaBtn();
	if(status==false){
		//$("#modal-certificatepaymentra-click").modal('show');
		//$("#modal-approverabill").modal('show');
		return false;
	}
	var currentAmount=$("#currentAmount").text()==""?0:parseFloat($("#currentAmount").text());
	var CrntCertifiedid=$("#raAmountToPay").val();
	
	var totalAmtToPay=parseFloat($("#totalAmtToPay").val().replace(/,/g,''));
	var raPcAmt=$("#raPc").val();
	var raDeductionAmt=$("#raDeductionAmt").val();
	if(CrntCertifiedid==''){
		CrntCertifiedid=0;
	}
	var utstandingamnt=$("#outstandingAdvTotalAmt").val();
	var raccamnt=$("#raCc").val();
	
	var outstandracc=parseFloat(CrntCertifiedid)+parseFloat(raPcAmt)+parseFloat(utstandingamnt); //-parseFloat(raDeductionAmt);
	outstandracc=outstandracc.toFixed(2);
	debugger;
	if(totalAmtToPay<outstandracc){
		alert("This Amount is greater than WO Amount.");
		$("#raAmountToPay").focus();
		return false;		
	}
	var caltotal=caldeductionamt();
	if(caltotal==false){
		return false;
	}
	validateWoOrderAmnt();
	var len=$("input[name='chk1']:checked").length;
	if(len==0){
		alert("Please select payment work order area.");
		$("#tbl-2").show();
		return false;
	}
	if(parseInt(currentAmount)!=parseInt(CrntCertifiedid)){
		alert("Please check landing page ceritified amount and abstract amount we got difference please make them same.");
		return false;
	}
	$("#appendWorkAreaId").html("");
	$.each($("input[name='chk1']:checked"), function(){ 
		var htmlData="";
		var thisVal=$(this).val();
		var workAreaId=$("#workAreaId"+thisVal).val();
		var block_name=$("#"+thisVal+"BLOCK_NAME").text().trim();
		var area=$("#CCQty"+thisVal).val().trim();
		var totalSelectedAreaAMount=$("#CCAmount"+thisVal).text().trim();
		var actualQty=$("#TotalQty"+thisVal).text();
		var initiatedQunatity=$("#CBQty"+thisVal).text();
		var price=$("#TotalRate"+thisVal).text();
		var totalQTY=$("#TotalQty"+thisVal).text();
		var TotalRate=$("#TotalRate"+thisVal).text();
		var wo_work_issue_area_dtls_id=$("#WO_WORK_ISSUE_AREA_DTLS_ID"+thisVal).val();
		htmlData+="<input type='hidden'  name='totalWOQuantity'   value='"+totalQTY+"'/>";
		htmlData+="<input type='hidden'  name='WOQuantityRate'   value='"+TotalRate+"'/>";
		
		htmlData+="<input type='hidden'  name='price'   value='"+price+"'/>";
		htmlData+="<input type='hidden'  name='initiatedQunatity'   value='"+initiatedQunatity+"'/>";
		htmlData+="<input type='hidden'  name='block_name'   value='"+block_name+"' readonly/>";
		htmlData+="<input type='hidden'  name='workAreaId'   value='"+workAreaId+"' readonly/>";
		htmlData+="<input type='hidden'  name='qty'  ' value='"+area+"' readonly/>";
		htmlData+="<input type='hidden'  name='ActualQTY' value='"+actualQty+"' readonly/>";
		
		htmlData+="<input type='hidden'  name='wo_work_issue_area_dtls_id'  value='"+wo_work_issue_area_dtls_id+"' />";
		
		$("#appendWorkAreaId").append(htmlData);
	});
	
	var str="";
	var rowsToIterate=$("#rowsToIterate").val();
	$("#recoveryAmountDetails").append("");
	var row1=0;
	for (var i = 1; i <= rowsToIterate; i++) {
		var htmlData="";
		var child_product_id=$("#childId"+i).val();
		var recovery_quantity=$("#"+child_product_id+"totalRecoveryAmount").text();;
		var recovery_amount=$("#"+child_product_id+"currentAmount").val();
		
		var recovery_amount1=$("#"+child_product_id+"currentAmount1").val();
		
		var mesurment_id=$("#"+child_product_id+"mesurment_id").val();
		if(recovery_amount!="0"){
			row1++;
			htmlData+="<input type='hidden' name='child_product_id"+row1+"' id='child_product_id"+i+"' value='"+child_product_id+"'>";
			htmlData+="<input type='hidden' name='measurement_id"+row1+"' id='measurement_id"+i+"' value='"+mesurment_id+"'>";
			htmlData+="<input type='hidden' name='recovery_amount"+row1+"' id='recovery_amount"+i+"' value='"+(recovery_amount)+"'>";
			htmlData+="<input type='hidden' name='recovery_quantity"+row1+"' id='recovery_quantity"+i+"' value='"+(recovery_quantity)+"'>";
			htmlData+="<input type='hidden' name='recovery_amount1"+row1+"' id='recovery_amount1"+i+"' value='"+(recovery_amount1)+"'>";
			htmlData+="<input type='hidden' name='currentRecoveryAmount11"+row1+"' id='currentRecoveryAmount11"+i+"' value='"+Math.abs((recovery_amount1-recovery_amount)).toFixed(2)+"'>";
		$("#recoveryAmountDetails").append(htmlData);
		}
	}

	var canISubmit = window.confirm("Do you want to approve bill?");
      
	  if(canISubmit == false) {
          return false;
      }
	  $(".overlay_ims").show();	
		 $(".loader-ims").show(); 
		 
	 $("#recoveryAmountDetails").append("<input type='hidden' name='rowsToIterate1' id='rowsToIterate1' value='"+row1+"'>");
	  $("#rowsToIterate").val(row1);
     $("#rejectRABtn").attr("disabled", true);   
   	  $("#approveRABtn").attr("disabled", true); 
      
      document.getElementById("contractorRABill").action = "approveWoRABill.spring";
      document.getElementById("contractorRABill").method = "POST";
      document.getElementById("contractorRABill").submit();
}
//approving advance bill
function approveAdvBill(){
	//apprRejFormId
	var len=$("input[name='chk1']:checked").length;
	if(len==0){
		alert("Please select payment work order area.");
		$("#tbl-2").show();
		return false;
	}

  	$("#appendWorkAreaId").html("");
	$.each($("input[name='chk1']:checked"), function(){ 
		var htmlData="";
		var thisVal=$(this).val();
		var workAreaId=$("#workAreaId"+thisVal).val();
		var block_name=$("#"+thisVal+"BLOCK_NAME").text().trim();
		var area=$("#CCQty"+thisVal).val().trim();
		var totalSelectedAreaAMount=$("#CCAmount"+thisVal).text().trim();
		var actualQty=$("#TotalQty"+thisVal).text();
		var initiatedQunatity=$("#CBQty"+thisVal).text();
		var price=$("#TotalRate"+thisVal).text();
		
		var totalQTY=$("#TotalQty"+thisVal).text();
		var TotalRate=$("#TotalRate"+thisVal).text();
		var wo_work_issue_area_dtls_id=$("#WO_WORK_ISSUE_AREA_DTLS_ID"+thisVal).val();
		htmlData+="<input type='hidden'  name='totalWOQuantity'   value='"+totalQTY+"'/>";
		htmlData+="<input type='hidden'  name='WOQuantityRate'   value='"+TotalRate+"'/>";
		
		htmlData+="<input type='hidden'  name='price'   value='"+price+"'/>";
		htmlData+="<input type='hidden'  name='initiatedQunatity'   value='"+initiatedQunatity+"'/>";
		htmlData+="<input type='hidden'  name='block_name'   value='"+block_name+"' readonly/>";
		htmlData+="<input type='hidden'  name='workAreaId'   value='"+workAreaId+"' readonly/>";
		htmlData+="<input type='hidden'  name='qty'  ' value='"+area+"' readonly/>";
		htmlData+="<input type='hidden'  name='ActualQTY' value='"+actualQty+"' readonly/>";
	
		htmlData+="<input type='hidden'  name='wo_work_issue_area_dtls_id'  value='"+wo_work_issue_area_dtls_id+"' />";
		$("#appendWorkAreaId").append(htmlData);
	});
	
	var raCc=$("#raCc").val();
	var totalAmtToPay=parseFloat($("#totalAmtToPay").val().replace(/,/g,''));
	
	if(raCc==''){
		raCc=0;
	}
	var utstandingamnt=$("#outstandingAdvTotalAmt").val();	
	var outstandracc=parseFloat(raCc)+parseFloat(utstandingamnt);
	outstandracc=outstandracc.toFixed(2);
	if(totalAmtToPay<outstandracc){
		alert("This Amount is greater than WO Amount.");		
		$("#finalAmt").val("0.00");
		$("#totalAmtCumulativeCertified").val("0.00");		
		return false;
		
	}
	
	var canISubmit = window.confirm("Do you want to approve bill?");
	  
    if(canISubmit == false) {
        return false;
    }
    $(".overlay_ims").show();	
	 $(".loader-ims").show(); 
	 
     document.getElementById("approveBtn").disabled = true;   
     document.getElementById("rejectBtn").disabled = true;   
       
      document.getElementById("apprRejFormId").action = "approveWoAdvBill.spring";
      document.getElementById("apprRejFormId").method = "POST";
      document.getElementById("apprRejFormId").submit();
}
//reject ra bill
function rejectRABill(){
	var remarks=document.getElementById("remarks");
	if(remarks.value==null||remarks.value==""){
		alert("Enter reason to reject RA bill");
		remarks.focus;
		remarks.placeholder="Enter reason here";
	return false;
	}
	
	
	var str="";
	var rowsToIterate=$("#rowsToIterate").val();
	$("#recoveryAmountDetails").append("");
	var row1=0;
	for (var i = 1; i <= rowsToIterate; i++) {
		var htmlData="";
		var child_product_id=$("#childId"+i).val();
		var recovery_quantity=$("#"+child_product_id+"totalRecoveryAmount").text();;
		var recovery_amount=$("#"+child_product_id+"currentAmount").val();
		var previous_amount=$("#"+child_product_id+"previous").text();
		var mesurment_id=$("#"+child_product_id+"mesurment_id").val();
		if(recovery_amount!="0"){
			htmlData+="<input type='hidden' name='child_product_id"+row1+"' id='child_product_id"+row1+"' value='"+child_product_id+"'>";
			htmlData+="<input type='hidden' name='measurement_id"+row1+"' id='measurement_id"+row1+"' value='"+mesurment_id+"'>";
			htmlData+="<input type='hidden' name='recovery_amount"+row1+"' id='recovery_amount"+row1+"' value='"+(previous_amount)+"'>";
			htmlData+="<input type='hidden' name='recovery_quantity"+row1+"' id='recovery_quantity"+row1+"' value='"+(recovery_quantity)+"'>";
		$("#recoveryAmountDetails").append(htmlData);
		row1++;
	
		}
	}
	$("#rowsToIterate").val(row1);
	$("#recoveryAmountDetails").append("<input type='hidden' name='rowsToIterate1' id='rowsToIterate1' value='"+row1+"'>");

	//apprRejFormId
	 var canISubmit = window.confirm("Do you want to reject RA bill?");
    
    if(canISubmit == false) {
        return false;
    }
    $(".overlay_ims").show();	
	 $(".loader-ims").show(); 
	 
      $("#rejectRABtn").attr("disabled", true);   
   	  $("#approveRABtn").attr("disabled", true); 
    
    document.getElementById("contractorRABill").action = "rejectWorkOrderBill.spring";
    document.getElementById("contractorRABill").method = "POST";
    document.getElementById("contractorRABill").submit();
}
//reject advance bill
function rejectAdvBill(){
	var remarks=document.getElementById("remarks");
	if(remarks.value==null||remarks.value==""){
		alert("Enter reason to reject Advance bill");
		remarks.focus;
		remarks.placeholder="Enter reason here";
		return false;
	}
	//apprRejFormId
	 var canISubmit = window.confirm("Do you want to reject bill?");
    
    if(canISubmit == false) {
        return false;
    }
    $(".overlay_ims").show();	
	 $(".loader-ims").show(); 
	 
    document.getElementById("rejectBtn").disabled = true;   
    document.getElementById("approveBtn").disabled = true;   
    document.getElementById("apprRejFormId").action = "rejectWorkOrderBill.spring";
    document.getElementById("apprRejFormId").method = "POST";
    document.getElementById("apprRejFormId").submit();
}
//calculating recovery amount and printing in certification page
var sumOfRecovery=0;
var prevRecoveryAmount=0;
function calculateRecoveryAmount(recoveryValue,childId){		
    sumOfRecovery=0;
    prevRecoveryAmount=0;
	var i=parseInt($("#rowsToIterate").val());
	
	if(recoveryValue.length==0){
		recoveryValue=0;
	}
	var child_product_id=$("#childId"+childId).val();
	if(!isNum(recoveryValue)){//&&(recoveryValue!="0"||recoveryValue.length==0)
		alert('Please Enter only digits');
		$("#"+child_product_id+"currentAmount").val('0');
		return false;
	}

	
	var totalAmount=$("#"+child_product_id+"totalRecoveryAmount").text();
	totalAmount=parseFloat(totalAmount);
	var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
	
	if(recovery_amount==0){
	//	$("#"+child_product_id+"currentAmount").val('0');
	}

for (var i1 = 1; i1 <= i; i1++) {
		var temp_child_product_id=$("#childId"+i1).val();
		var recovery_amount=parseFloat($("#"+temp_child_product_id+"currentAmount").val()==""?0:$("#"+temp_child_product_id+"currentAmount").val());
		var previous=$("#"+temp_child_product_id+"previous").text();//getting previous value using child id 
		previous=parseFloat(previous);
	 	sumOfRecovery+=(recovery_amount);
	 	prevRecoveryAmount+=previous+recovery_amount;
	 	$("#"+temp_child_product_id+"cumulative").text(previous+recovery_amount);
	}
	sumOfRecovery=sumOfRecovery.toFixed(2);
	var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
	cumulative=parseFloat(cumulative);
	
	
	var previouscumilativeRecovery=$("#previousRecovery").val()==""?0:parseFloat($("#previousRecovery").val());
	$("#sumOfCurrentAmount").text(sumOfRecovery);
	$("#sumOfRecoveryCumulative").text(prevRecoveryAmount.toFixed(2));
}

function recoverySubmit(){
	$("#currentRecoveryAmount").val(sumOfRecovery);
	$("#recoverycurrentAmount").val(sumOfRecovery);	
	$("#cumulativeRecovery").val((prevRecoveryAmount).toFixed(2));  //+previouscumilativeRecovery
	
	caldeductionamt("recovery");
}

//reseting values 
function clearAllValues(){
	$("#raAmountToPay").val('0.00');
	$("#raCc").val('0.00');
	$("#raPc").val('0.00');
	$("#raTotalCc").val('0.00');
	$("#raTotalPc").val('0.00');
	$("#totalCc").val('0.00');
	$("#finalAmt").val('0.00');
	$("#totalAmtCumulativeCertified").val('0.00');
	$("#totalAmtPreviousCertified").val('0.00');
	$("#totalDeductionAmtCumulative").val('0.00');
	$("#totalAmtCurntDeduc").val('0.00');
	$("#totalDeductionAmtCumulative").val('0.00');
	$("#other").val('0.00');
	$("#pettyExpensesCurrentCerti").val('0.00');
	$("#raDeductionAmt").val('0.00');
	$("#finalAmt").val("0.00");
	
	$("#advanceCurrAmount").val("0.00");
	$("#outstandingAdvPrevAmt").val("0.00");
	$("#outstandingAdvTotalAmt").val("0.00");
	$("#releaseAdvTotalAmt").val("0.00");
	$("#outstandingAdvTotalAmt").val("0.00");
	$("#raPaidAmnt").val("0.00");
	$("#secDepositCurrentCerti").val("0.00");
	$("#recoveryCurrentCerti").val("0.00");
	$("#advanceToBeDeductionBillDetails").html("");	
}

//loading recovery data
function loadRARecovery(){
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var approvePage=$("#approvePage").val();
	var billType=$("#billType").val();
	var tempBillNo=$("#tempBillNo").val();
	var site_id=$("#site_id").val();

var str="";
var str1="";
var htmlData="";
	$.ajax({
		url : "loadRecoveryAreaDetails.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			approvePage:approvePage,
			billType:billType,
			tempBillNo:tempBillNo,
			site_id:site_id
			},
		success : function(data) {
			var i=0;
			var sumOfRecovery=0;
			var sumOfCumulative=0;
			var sumOfPrev=0;
			var childProductName="";
			var temprecoveryAmount="";
			var tempcurrentrecovery=0;
			var temptotalRecoveryAmount=0;
			var temptotalRecoveryQTY=0;
			$("#RecoveryStatement").html("");
			$("#ViewRecovery").html("");
			$.each(data,function(key,value){	
		
				if(value.ISSUED_QTY=="0"){
					return ;
				}
				
				var amount_per_unit_before_taxes=parseFloat(value.AMOUNT_PER_UNIT_BEFORE_TAXES).toFixed(2);
				var issuedQty=parseFloat(value.ISSUED_QTY);
				var currentRecoveryAmount=0;
			
				var recoveryAmount=value.RECOVERY_AMOUNT==null?0:parseFloat(value.RECOVERY_AMOUNT);
				temprecoveryAmount=value.RECOVERY_AMOUNT;
				var recoveryCurrentAmount=parseFloat(value.TOTAL_AMOUNT)-recoveryAmount;
				
				var totalRecoveryAmount=value.TOTAL_AMOUNT==null?"":value.TOTAL_AMOUNT;
				
				temptotalRecoveryAmount+=totalRecoveryAmount;
				//sum of total quantity
				temptotalRecoveryQTY+=issuedQty;
				
				
				let tempCumulative=(recoveryAmount+recoveryCurrentAmount);
				
				var child_product_id=value.CHILD_PRODUCT_ID;
				if(childProductName!=value.CHILD_PROD_NAME&&value.CHILD_PROD_NAME!=undefined){
					
					childProductName=value.CHILD_PROD_NAME;
					i++;
					str="<tr> <td > "+i+"<input type='hidden' id='childId"+i+"' value='"+child_product_id+"'> </td>" +
						"<td><strong>"+value.CHILD_PROD_NAME+"</strong></td>" +
						"<td id='"+child_product_id+"issuedQty'>"+issuedQty+"</td>" +
						"<td id='"+child_product_id+"totalRecoveryAmount'>"+totalRecoveryAmount+"</td>" +
						"<td  id='"+child_product_id+"mesurment_name'>"+value.MESURMENT_NAME+" <input type='hidden'  id='"+child_product_id+"mesurment_id' value='"+value.UNITS_OF_MEASUREMENT+"'></td>" +
						"<td id='"+child_product_id+"cumulative'>"+recoveryAmount+"</td>" +
						"<td id='"+child_product_id+"previous'>"+recoveryAmount+"</td>" +
						"<td> <input type='text' class='form-control' id='"+child_product_id+"currentAmount' name='recoverycurrentAmount' value='0' onkeypress='return isNumber(this, event)' onkeyup='calculateRecoveryAmount(this.value,"+i+");'>  </td>" +
					"</tr>";
		
					$("#RecoveryStatement").append(str);
					
				}else{
					//for recovery statement
					var  tempIssuedQty =$("#"+child_product_id+"issuedQty").text();
					tempIssuedQty=parseFloat(tempIssuedQty);
					var before_taxes =$("#"+child_product_id+"totalRecoveryAmount").text();
					before_taxes=parseFloat(before_taxes);
						var currentAmount=parseFloat($("#"+child_product_id+"currentAmount").val());
						$("#"+child_product_id+"issuedQty").text((tempIssuedQty+issuedQty).toFixed(2));
						$("#"+child_product_id+"totalRecoveryAmount").text((before_taxes+totalRecoveryAmount).toFixed(2));
		
						
			}
		});
			var sumOfCurrRecovery=0;
			for (var i1 = 1; i1 <= i; i1++) { 
				var child_product_id=$("#childId"+i1).val();
				var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
				var totalRecoveryAmount=$("#"+child_product_id+"totalRecoveryAmount").text();//getting previous cumulative value using child id
				totalRecoveryAmount=parseFloat(totalRecoveryAmount);
	
				var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
				cumulative=parseFloat(cumulative);
				var previous=$("#"+child_product_id+"previous").text();//getting previous value using child id 
				previous=parseFloat(previous);
				sumOfCumulative+=+cumulative;
				sumOfPrev+=+previous;
				sumOfRecovery+=(recovery_amount);
				sumOfCurrRecovery+=(totalRecoveryAmount-previous);
			}
		$("#currentRecoveryAmount").val(sumOfRecovery.toFixed(2));
		$("#showCurrentRecoveryAmount").html(sumOfCurrRecovery.toFixed(2));
		$("#recoverycurrentAmount").val(sumOfRecovery.toFixed(2));
		if(billType=="ADV"){
			$("#recoverycurrentAmount").val(0);
		}
		htmlData+="<input type='hidden' name='rowsToIterate' id='rowsToIterate' value='"+i+"'>";
		//this is for final amount
		var temp=" <tr style='background-color: #cccccc;'>" +
		"<td></td>" +
		"<td class='text-right'><h4><strong>Total Amount</strong></h4></td>" +
		"<td  id='totalWORecoveryQTY'>"+(temptotalRecoveryQTY.toFixed(2))+"</td>" +
		"<td id='totalWORecoveryAmount'>"+(temptotalRecoveryAmount.toFixed(2))+"</td>" +
		"<td ></td>" +
		"<td><strong  id='sumOfRecoveryCumulative'>"+(sumOfPrev.toFixed(2))+"</strong></td>" +
		"<td ><strong>"+(sumOfPrev.toFixed(2))+"</strong></td>" +
		"<td ><strong id='sumOfCurrentAmount'>"+sumOfRecovery+"</strong></td></tr>";	
		
		$("#RecoveryStatement").append(temp);
	
		
		$("#RecoveryStatement").append(htmlData);	
		
		}			
	});
	
	
}
function sortFloor(a, b) {}
//loading abstract 
function loadPaymentWorkArea(){
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var site_id=$("#site_id").val();
	var approvePage=$("#approvePage").val();
	var tempBillNo=$("#tempBillNo").val();
	var billDate=$("#billDate").val();
	var d = new Date();
	var n = d.toLocaleDateString();	
	var billType=$("#billType").val();	
	var majorHeadNames=new Array();
	var majorHeadNameAndId=new Array();
	$.ajax({
		url : "loadWorkOrderArea.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			billType:billType,
			approvePage:approvePage,
			tempBillNo:tempBillNo,
			site_id:site_id
		},
		success : function(data	) {			
			var str="";		
			var i=1;
			var basic_amount=0;			
			var minorHeadName="";
			var majorHeadName="";
			var workDescName="";
			var maintaingPrevRateToNewRow=0
			var majorHeadLength=1;
			var textHeading=65;
			var increIndex=1;
			var workdescIncre=1;			
			var sumOfTotalQunatity=0;
			var sumOfPrevQty=0;
			var sumOfCumulativeQTy=0;
			var sumOfPrevAmount=0;
			var sumOfCumulativeAmount=0;
			var sumOfAllocatedQuantity=0;
			var sumOfCurrentAmount=0;
			$.each(data,function(key,value){
			var blockName=(value.BLOCK_NAME==null?"-":value.BLOCK_NAME);
			var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
			var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
		
			if(approvePage=="true"){
				var htmlData="";
				htmlData+="<input type='hidden'  name='actualWorkAreaId' id='actualWorkOrderAreaId' style='border:none; text-align:center;' value='"+value.WO_WORK_AREA_ID+"' readonly/>";
				htmlData+="<input type='hidden'  name='actualQty' id='actualQty' style='border:none; text-align:center;' value='"+value.ALLOCATED_QTY+"' readonly/>";
				$("#appendActualAreaDetails").append(htmlData);
				var pravArea=value.PREVAREA==""?0:value.PREVAREA;
				pravArea=pravArea==null?0:pravArea;
				//cheking is mejor head name is already printed or not 
				if(majorHeadName!=value.WO_MAJORHEAD_DESC){
			//	if(i==majorHeadLength){
					majorHeadLength++;
					//stroing major head name and work desc in temp variable
					majorHeadName=value.WO_MAJORHEAD_DESC.trim();
					workDescName=value.WO_WORK_DESCRIPTION;
					minorHeadName=value.WO_MINORHEAD_DESC;
					if( majorHeadNames.includes(value.WO_MINORHEAD_DESC)==false){
						if(value.ALLOCATED_QTY!=undefined){
							majorHeadNames.push(value.WO_MAJORHEAD_DESC);
							majorHeadNameAndId.push(value.WO_MAJORHEAD_ID+"@@"+value.WO_MAJORHEAD_DESC);
						}
					}
					
					
					increIndex=1;
					str+=" <tr> <td class='text-center'></td>" +
					"<td class='text-left'style='text-align: center;' colspan='10' ><strong> "+value.WO_MAJORHEAD_DESC+" </strong></td>" +				
					"</tr>";
					
					//printing minor head
					str+=" <tr> <td class='text-center'></td>" +
					"<td class='text-left'style='text-align: center;' colspan='10'><strong> "+value.WO_MINORHEAD_DESC+" </strong></td>" +
					"</tr>";

					str+=" <tr> <td class='text-center'></td> " +
						"<td class='text-left'   style='font-weight: bold;text-align: center;' colspan='10'>"+value.WO_WORK_DESCRIPTION+"</td>" +					
						"</tr>";
					
				//}
				//checking is work description is changed or not if changed execute this block to print the work description
				}else if(minorHeadName!=value.WO_MINORHEAD_DESC){
					minorHeadName=value.WO_MINORHEAD_DESC;
					//printing minor head
					str+=" <tr> <td class='text-center'></td>" +
					"<td class='text-left'style='text-align: center;' colspan='10'><strong> "+value.WO_MINORHEAD_DESC+" </strong></td>" +			
					"</tr>";
				}
				
				
				
				if(workDescName!=value.WO_WORK_DESCRIPTION){
					//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
					
					workDescName=value.WO_WORK_DESCRIPTION;
					str+=" <tr> <td class='text-center'></td> " +
					"<td class='text-left'   style='font-weight: bold;text-align: center;' colspan='10'>"+value.WO_WORK_DESCRIPTION+"</td>" +				
					"</tr>";
				}
				
				var RA_amountTotalQTY=0;
				var ADV_amountTotalQTY=0;
				
				var flatName=value.NAME==null?"-":value.NAME;
				var flatname=value.FLATNAME==null?"-":value.FLATNAME;
				var floorName=value.FLOOR_NAME==null?"-":value.FLOOR_NAME;
				var txtid=value.WO_WORK_AREA_ID;
				var allocatedArea=value.ALLOCATED_QTY==""?0:parseFloat(value.ALLOCATED_QTY);
				var pravArea=0;
				var prevAreaQuantity=new Array();
				
				var lengthOfThePrevArea=0;
				var previousBillAmount=0;
				var customMsg="";
				
				var blockNames='';
				if(value.FLATNAME!=null){
					blockNames=blockNames+" , "+value.FLATNAME;
				}
				if(value.FLOOR_NAME!=null){
					blockNames=blockNames+" , "+value.FLOOR_NAME;
				}
				debugger;
				try {
					var index=value.PREVAREA.search("@@");
					if(index>=0){
						prevAreaQuantity=value.PREVAREA.split("@@");
						for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
							let array_element = prevAreaQuantity[ind];
							
							if(billType=="ADV"){
								let tempQtyAndRate=array_element.split("-");
								if(tempQtyAndRate[1]=="ADV"){
									pravArea+=parseFloat(tempQtyAndRate[0]);
									var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
									previousBillAmount+=tempQtyAndRate[0]*rate;
									if(ind<prevAreaQuantity.length-1){
										customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
									}else{
										customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
									}
								}else 	if(tempQtyAndRate[1]=="RA"){
									debugger;
									//RA_amountTotalQTY+=parseFloat(tempQtyAndRate[0]);
								}
								lengthOfThePrevArea++;
							}else{
								var tempQtyAndRate=array_element.split("-");
								pravArea+=parseFloat(tempQtyAndRate[0]);//array position (0) getting prev QTY
								var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
								
								previousBillAmount+=tempQtyAndRate[0]*rate;
								if(ind<prevAreaQuantity.length-1){
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
								}else{
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
								}
								lengthOfThePrevArea++;
							}
						}

					}else{
						if(billType==value.PREVAREA.split("-")[1]){
							let tempQtyAndRate = value.PREVAREA.split("-");
							pravArea+=parseFloat(tempQtyAndRate[0]);
							var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
							previousBillAmount+=tempQtyAndRate[0]*rate;
						}else{
						//	RA_amountTotalQTY+=parseFloat(value.PREVAREA.split("-")[0]);							
						}
					}
				} catch (e) {
					console.log(e);
				}
				if(value.ALLOCATED_QTY==undefined){
					debugger;
					allocatedArea=value.ALLOCATED_QTY==undefined?0.00:parseFloat(value.ALLOCATED_QTY);
				}
				 var multiplyArea=(parseFloat(pravArea)+allocatedArea);
				 
				 sumOfPrevQty+=+pravArea;
				 sumOfCumulativeQTy+=+multiplyArea;
				 
				 sumOfPrevAmount+=+(previousBillAmount);
				 sumOfCumulativeAmount+=+(previousBillAmount+(allocatedArea*value.PRICE));
				 
				 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
				 
				 sumOfAllocatedQuantity+=+allocatedArea;
				 sumOfCurrentAmount+=+(allocatedArea*value.PRICE);
				 if(value.ALLOCATED_QTY!=undefined){
					 str+=" <tr>  <td class='text-center'><input type='checkbox' name='chk1'  style='width: 100%;height: 16px;cursor: pointer;' class='chkcls' id='chk1"+i+"'  checked='true' value='"+i+"' onclick='checkQuantity("+i+")'></td>" ;
				 }else{
					 str+=" <tr>  <td class='text-center'><input type='checkbox' name='chk1'  style='width: 100%;height: 16px;cursor: pointer;' class='chkcls' id='chk1"+i+"' value='"+i+"' onclick='checkQuantity("+i+")'></td>" ;
				 }
				if(lengthOfThePrevArea>=1)
					str+="  <td class='text-left'>"+value.BLOCK_NAME+blockNames+"  <br> \n"+(customMsg)+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
				else
					str+="  <td class='text-left'>"+value.BLOCK_NAME+blockNames+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
		
				str+=	"  <td class='text-right'><span id='TotalQty"+i+"'>"+(value.AREA_ALOCATED_QTY).toFixed(2)+"</span> </td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
						"  <td class='text-right'><span id='TotalRate"+i+"'>"+(value.PRICE).toFixed(2)+"</span> </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
						"  <td class='text-right'><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
						"  <td class='text-right'> <span id='CBQty"+i+"'>"+(multiplyArea).toFixed(2)+" </span> <input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
						"  <td class='text-right'><span id='CBAmount"+i+"'>"+((allocatedArea*value.PRICE+previousBillAmount).toFixed(2))+" </span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
						"  <td class='text-right'><span id='PCQty"+i+"'>"+(pravArea).toFixed(2)+"</span></td>" +
						"  <td class='text-right'><span id='PCAmount"+i+"'>"+(previousBillAmount).toFixed(2)+"</span> <input type='hidden' value='"+(pravArea+RA_amountTotalQTY)+"' id='RA_amountTotalQTY"+i+"'> </td>" +
						"   <td class='text-right'><input type='text' class='form-control txtcls copyPasteRestricted' value='"+allocatedArea+"' id='CCQty"+i+"' onkeypress='return isNumber(this, event)' onkeyup='calculatePayRabill("+i+")'> </td>" +
						"   <td class='text-right'><span class='CCAmountcls' id='CCAmount"+i+"'>"+((allocatedArea*value.PRICE)).toFixed(2)+" </span></td>"+
						"   </tr>";
				//onchange='validateFloatKeyPress(this);'  onchange='validateFloatKeyPress(this);'

			}else{
				//cheking is mejor head name is already printed or not 
				if(majorHeadName!=value.WO_MAJORHEAD_DESC){
				//if(i==majorHeadLength){
					majorHeadLength++;
					//stroing major head name and work desc in temp variable
					majorHeadName=value.WO_MAJORHEAD_DESC
					workDescName=value.WO_WORK_DESCRIPTION;
					minorHeadName=value.WO_MINORHEAD_DESC;
					if( majorHeadNames.includes(value.WO_MINORHEAD_DESC)==false){
						majorHeadNames.push(value.WO_MAJORHEAD_DESC);
						majorHeadNameAndId.push(value.WO_MAJORHEAD_ID+"@@"+value.WO_MAJORHEAD_DESC);
					}
					increIndex=1;
					str+=" <tr> <td class='text-center'></td>" +						
						"  <td class='text-left'style='text-align: center;' colspan='10'><strong>"+value.WO_MAJORHEAD_DESC+"</strong></td>" +				     
						"   </tr>";
					
					//Minor Head Printing
					str+=" <tr> <td class='text-center'></td>" +
					"<td class='text-left'style='text-align: center;' colspan='10'><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +					
					"</tr>";
					
					str+=" <tr> <td class='text-center'></td> " +						
						"<td class='text-left'  style='font-weight: bold;text-align: center;' colspan='10' >"+value.WO_WORK_DESCRIPTION+"</td>" +						
						"</tr>";
					
				//}
				//checking is work description is changed or not if changed execute this block to print the work description
				}else if(minorHeadName!=value.WO_MINORHEAD_DESC){
					minorHeadName=value.WO_MINORHEAD_DESC;
					str+=" <tr> <td class='text-center'></td>" +					
					"<td class='text-left'style='text-align: center;' colspan='10'><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +					
					"</tr>";
				}
				if(workDescName!=value.WO_WORK_DESCRIPTION){
					//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
					
					workDescName=value.WO_WORK_DESCRIPTION;
					str+=" <tr> <td class='text-center'></td> " +						
					"<td class='text-left'   style='font-weight: bold;text-align: center;' colspan='10'>"+value.WO_WORK_DESCRIPTION+"</td>" +			
					"</tr>";
					
				}
				
				var RA_amountTotalQTY=0;
				var ADV_amountTotalQTY=0;
				debugger;
				var flatName="";//value.NAME==null?"-":value.NAME;
				var flatname="";//value.FLATNAME==null?"-":value.FLATNAME;
				var floorName="";//value.FLOOR_NAME==null?"-":value.FLOOR_NAME;
				var txtid=value.WO_WORK_AREA_ID;
				var blockNames='';
				if(value.FLATNAME!=null){
					blockNames=blockNames+" , "+value.FLATNAME;
				}
				if(value.FLOOR_NAME!=null){
					blockNames=blockNames+" , "+value.FLOOR_NAME;
				}
				
				maintaingPrevRateToNewRow=value.PRICE;
				var pravArea=0;
				var prevAreaQuantity=new Array();
				var lengthOfThePrevArea=0;
				var customMsg="";
				var previousBillAmount=0;
				try {
					var index=value.PREVAREA.search("@@");
					if(index>=0){//if this value.PREVAREA more than one previous bill QTY 
						prevAreaQuantity=value.PREVAREA.split("@@");
						for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
							let array_element = prevAreaQuantity[ind];
							if(billType=="ADV"){
								let tempQtyAndRate=array_element.split("-");
								if(tempQtyAndRate[1]=="ADV"){
									pravArea+=parseFloat(tempQtyAndRate[0]);
									
									var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
									previousBillAmount+=tempQtyAndRate[0]*rate;
									
									
								}else if(tempQtyAndRate[1]=="RA"){
									//RA_amountTotalQTY+=parseFloat(tempQtyAndRate[0]);
								}
								if(ind<prevAreaQuantity.length-1){
									customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
								}else{
									customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
								}
								
								lengthOfThePrevArea++;
							}else{
								var tempQtyAndRate=array_element.split("-");
								pravArea+=parseFloat(tempQtyAndRate[0]);//array position (0) getting prev QTY
								var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
								
								previousBillAmount+=tempQtyAndRate[0]*rate;
								if(ind<prevAreaQuantity.length-1){
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
								}else{
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
								}
								
								
								lengthOfThePrevArea++;
							}
						}
						//alert(prevAreaQuantity.toString());
					}else{
						if(billType==value.PREVAREA.split("-")[1]){
							let tempQtyAndRate = value.PREVAREA.split("-");
							pravArea+=parseFloat(tempQtyAndRate[0]);
							var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
							previousBillAmount+=tempQtyAndRate[0]*rate;
						}else{
						//	RA_amountTotalQTY+=parseFloat(value.PREVAREA.split("-")[0]);
						//lengthOfThePrevArea++;
						//	customMsg+="(Qty = "+value.PREVAREA.split("-")[0]+" , Rate = "+value.PREVAREA.split("-")[2]+")";
						}
					}
				} catch (e) {
					//alert(e);
				}
			
  				var newchar = '+';
				prevAreaQuantity = prevAreaQuantity.toString().split(',').join(newchar);
				
				 sumOfPrevQty+=+pravArea;
				 sumOfCumulativeQTy+=+pravArea;
				
				 sumOfPrevAmount+=(previousBillAmount);
				 sumOfCumulativeAmount+=(previousBillAmount);
				 
				 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
				 pravArea=pravArea.toFixed(2)
				 var isWDCompleted=pravArea>=value.AREA_ALOCATED_QTY;
				 
					str+=" <tr>  <td class='text-center'><input type='checkbox'  style='width: 100%;height: 16px;cursor: pointer;' class='chkcls' name='chk1' id='chk1"+i+"' value='"+i+"'  onclick='checkQuantity("+i+")'></td>" ;
							/*"<td class='text-center'>"+(increIndex++)+"</td>" +*/
					if(lengthOfThePrevArea>=1){
						str+="  <td class='text-left'>"+value.BLOCK_NAME+blockNames+"  <br> \n "+(customMsg)+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
					}else{
						str+="  <td class='text-left'>"+value.BLOCK_NAME+blockNames+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
					}
					str+="  <td class='text-right'><span id='TotalQty"+i+"'>"+(value.AREA_ALOCATED_QTY).toFixed(2)+"</span> </td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
							"  <td class='text-right'><span id='TotalRate"+i+"'>"+(value.PRICE).toFixed(2)+"</span> </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
							"  <td class='text-right'><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
							"  <td class='text-right'><span id='CBQty"+i+"'>"+(pravArea)+"</span> <input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
							"  <td class='text-right'><span id='CBAmount"+i+"'>"+(previousBillAmount).toFixed(2)+"</span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
							"  <td class='text-right'><span id='PCQty"+i+"'>"+(pravArea)+"</span>  </td>" +
							"  <td class='text-right'><span id='PCAmount"+i+"'>"+(previousBillAmount).toFixed(2)+"</span><input type='hidden' value='"+(parseFloat(pravArea)+RA_amountTotalQTY)+"' id='RA_amountTotalQTY"+i+"'>  </td>" ;
							if(isWDCompleted)
							str+="   <td class='text-right'><input type='text' class='form-control txtcls copyPasteRestricted' value='0' id='CCQty"+i+"' onkeypress='return isNumber(this, event)' onkeyup='calculatePayRabill("+i+")' readonly> </td>" ;
							else
							str+="   <td class='text-right'><input type='text' class='form-control txtcls copyPasteRestricted' value='0' id='CCQty"+i+"' onkeypress='return isNumber(this, event)' onkeyup='calculatePayRabill("+i+")'> </td>" ;
					
							str+="   <td class='text-right'><span class='CCAmountcls' id='CCAmount"+i+"'></span></td>"+
							"   </tr>";
				
				
			}
			i++;
		//	}
			});
			
			str+=" <tr style='background-color: #cccccc;'>  <td class='text-center'></td>" +
		/*	"<td class='text-center'>"+(increIndex++)+"</td>" +*/
			"  <td class='text-left'><strong>Total Amount/Quantity</strong> </td>" +
			"  <td class='text-right'><span style='display:none;'"+sumOfTotalQunatity.toFixed(2)+"</td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
			"  <td class='text-right'> </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
			"  <td class='text-right'></td>" +
			"  <td class='text-right'> <span id='cumulativeQuantity'  style='display:none'>"+(sumOfCumulativeQTy).toFixed(2)+" </span></td>" +
			"  <td class='text-right'><span id='cumulativeAmount'>"+(sumOfCumulativeAmount).toFixed(2)+" </span></td>" +
			"  <td class='text-right'><span id='previousQuantity'  style='display:none'>"+(sumOfPrevQty).toFixed(2)+" </span></td>" +
			"  <td class='text-right'><span id='previousAmount'>"+(sumOfPrevAmount).toFixed(2)+"</span></td>" +
			"   <td class='text-right'><span id='currentQuantity'  style='display:none'>"+(sumOfAllocatedQuantity).toFixed(2)+"</span></td>" +
			"   <td class='text-right'><span id='currentAmount'>"+(sumOfCurrentAmount).toFixed(2)+"</span></td>"+
			"   </tr>";
			
			$("#cumulativeQuantity").text((sumOfPrevQty).toFixed(2));
			$("#cumulativeAmount").text((sumOfPrevAmount).toFixed(2));
			$("#previousQuantity").text((sumOfPrevQty).toFixed(2));
			$("#previousAmount").text((sumOfPrevAmount).toFixed(2));
			
			$("#paymentByArea").html(str);
			var newchar = '\\';
		
			majorHeadNames = majorHeadNames.toString().split(',').join(newchar);
			majorHeadNameAndId = majorHeadNameAndId.toString().split(',').join('#');
			$("#paymentTypeOfWork").html(majorHeadNames);
			$("#paymentTypeOfWork1").val(majorHeadNameAndId);
			
		    $(".copyPasteRestricted").bind('paste', function (e) {
				e.preventDefault();
			});
			
			if(approvePage=="true"){
				var paymentAmnt=0;
				$(".chkcls:checked").each(function(){
					   var chkid=$(this).attr('id');
					   var chkspiltid=chkid.split("chk1")[1];
					   var CCAmnt=$("#CCAmount"+chkspiltid).text();
					   paymentAmnt+=parseFloat(CCAmnt);
				});
				$("#currentAmount").text(paymentAmnt.toFixed(2));
			}
			
		},
		error : function(err) {
			//alert(err);
		}
	});
}
//checking previous bills whether the bills are in running or not
function loadADVRABillNo(){
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var approvePage=$("#approvePage").val();
	var tempBillNo=$("#tempBillNo").val();	
	var contractorGSTIN=$("#contractorGSTIN").val();
	var billType=$("#billType").val();
	$.ajax({
		url : "loadAdvRAPermanentBillNo.spring",
		type : "GET",
		data : {
			ContractorId : ContractorId,
			workOrderNo:workOrderNo,
			billType:billType,
			contractorGSTIN:contractorGSTIN
		},
		success : function(data) {
			var index=data.search("@@");
			var billNumber="";
			var invoiceNumber="";
			if(index>=0){
				alert("Please check the previous running account bills.");
				$('#rasubmitBtn').attr('disabled',true);
				$('#submitBtn').attr('disabled',true);
			}else{
				var billNumber=data.split("_")[0];
				var invoiceNumber=data.split("_")[1];
				$("#raBillNo").val(billNumber);
				$("#advBillNo").val(billNumber);
				
				$("#billInvoiceNo").val(invoiceNumber);
				//$("#advBillInvoiceNo").val(invoiceNumber);
				$("#actualBillInvoiceNo").val(invoiceNumber);
			}
		}
	});
	$.ajax({
		url:"loadWOWorkTypeDesc.spring",
		type:"GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			billType:billType
			},
		success:function(response){
				var selectdata='';
        		$.each(response, function(key, val){
					 selectdata+='<option value='+key+'@@'+val+'>'+val+'</option>';
				})
				$("#typeofworkselect").html(selectdata);
		}
	});
}
var holdAdvBillNumberAndAmount="";
//when work order number changing getting the certified data
function workOrderNumChanged(){	
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var site_id=$("#site_id").val();	
	var approvePage=$("#approvePage").val();
	var tempBillNo=$("#tempBillNo").val();
	
	if(ContractorId.length==0){
		alert("Please enter contractor name.");
		return false;
	}
	var billDate=$("#billDate").val();
	var d = new Date();
	var n = d.toLocaleDateString();
	
	$.ajax({
		url : "loadAdvBillCertificatateDetails.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			approvePage:approvePage,
			billDate:billDate,
			tempBillNo:tempBillNo,
			site_id:site_id
		},
		success : function(data) {
			$("#appendBillDetails").html("");
		 
			var AdvPaidAmount=0.00;
			var htmlData="";
			var advHtmlData="";
			var raHtmlData="";
			var initiatedAdv=0;
			var raPaidAmt=0.00;
			var holdPrevAmount=0.00;
			var raPrevCertifiedAmount=0.00;
			var advPrevCertifiedAmount=0.00;
			var deduction_amount=0.00;
			var sec_percentege=0.00;
			var	ADV=0.00;
			var	SEC=0.00;
			var	PETTY=0.00;
			var	OTHER=0.00;
			var RECOVERY=0.00;
			var secDeposit=0.00;
			var appendPrevDeductVal="";
			
			$.each(data,function(key,value){
			var billStatus=value.STATUS;
			var holdAdvBillNumberAndAmount="";
				if(billStatus=="A"){
					billStatus="Active";
				}else if(billStatus=="R"){
					billStatus="Rejected";
				}else if(billStatus=="I"){
					billStatus="Completed";
				}		
				
			
				var emp=value.PENDING_EMP_ID;
				var payment_type=value.PAYMENT_TYPE;
				if(payment_type=="ADV"){
					AdvPaidAmount+=+value.PAYBLE_AMOUNT;
					advPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
					debugger;
					var pendingid=" Advance Completed ";
					 
		    		raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><a  target='_blank' style='color:#0000ff;' class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>"+inrFormat(parseFloat(value.CERTIFIED_AMOUNT).toFixed(2))+"</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(value.PAYBLE_AMOUNT).toFixed(2))+"</span></td>";
					raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT)).toFixed(2))+"</span></td></tr>";
					holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);
					
				}else if(payment_type=="RA"){
						raPaidAmt+=+value.PAYBLE_AMOUNT;
						//raPaidAmt+=+value.CERTIFIED_AMOUNT;
						raPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
						sec_percentege=value.SEC_PERCENTEGE;
						var status=value.STATUS=="R"?" RA Rejected ":value.STATUS;
						var pendingid=" RA Completed ";
			
						var printAdvAmount=0.00;
						var printSecDeposit=0.00;
						var printPettyExpences=0.00;
						var printOther=0.00;
						var printRecoveryAmt=0.00;
						if(value.DEDUCTION_AMOUNT!=null){
							var typeOfDeduction=new Array();
							var typeOfDeductionVal=new Array();
							//type of deduction in coming in the array where is spliting by @@
							typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
							//type of DEDUCTION_AMOUNT in coming in the array where is spliting by @@
							typeOfDeductionVal=	value.DEDUCTION_AMOUNT.split("@@")
							for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
								if(typeOfDeduction[i2]=="ADV"){
									deduction_amount+=+typeOfDeductionVal[i2];
									printAdvAmount=typeOfDeductionVal[i2];
								}else if (typeOfDeduction[i2]=="OTHER"){
									OTHER+=+typeOfDeductionVal[i2]
									printOther=typeOfDeductionVal[i2];
								}else if (typeOfDeduction[i2]=="PETTY"){
									PETTY+=+typeOfDeductionVal[i2]
									printPettyExpences=typeOfDeductionVal[i2];
								}else if (typeOfDeduction[i2]=="RECOVERY"){
									RECOVERY+=+typeOfDeductionVal[i2]
									printRecoveryAmt=typeOfDeductionVal[i2];
								}else if(typeOfDeduction[i2]=="SEC"){
									secDeposit+=+typeOfDeductionVal[i2];
									printSecDeposit=typeOfDeductionVal[i2];
								}
							}
					 //deduction_amount+=+value.DEDUCTION_AMOUNT.split("@@")[0];
					// secDeposit+=+value.DEDUCTION_AMOUNT.split("@@")[1];
					}
			 /*raHtmlData+=" <tr><td style='padding:10px;border:1px solid #000;'>"+payment_type+"</td><td style='padding:10px;border:1px solid #000;'>0</td><td style='padding:10px;border:1px solid #000;'>"+value.CERTIFIED_AMOUNT+"</td><td style='padding:10px;border:1px solid #000;'>"+value.PAYBLE_AMOUNT+"</td><td style='padding:10px;border:1px solid #000;'>"+value.BILL_ID+"</td><td style='padding:10px;border:1px solid #000;'>"+printAdvAmount+" </td><td style='padding:10px;border:1px solid #000;'>"+printRecoveryAmt+" </td><td style='padding:10px;border:1px solid #000;'>"+printSecDeposit+"</td><td style='padding:10px;border:1px solid #000;'>"+printPettyExpences+"</td><td style='padding:10px;border:1px solid #000;'>"+printOther+"</td><td style='padding:10px;border:1px solid #000;'>"+value.ENTRY_DATE+"</td><td style='padding:10px;border:1px solid #000;'>"+pendingid+"</td></tr>";*/
						 /*raHtmlData+=" <tr><td style='padding:10px;border:1px solid #000;'>"+payment_type+"</td><td style='padding:10px;border:1px solid #000;'>0</td><td style='padding:10px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+value.PAYBLE_AMOUNT+"</span></td>" +
						 "  <td style='padding:10px;border:1px solid #000;'><a  target='_blank' style='color:#0000ff;' class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td></td>" +
						 "  <td style='padding:10px;border:1px solid #000;'><span>"+printAdvAmount+"</span> </td><td style='padding:10px;border:1px solid #000;'><span>"+printRecoveryAmt+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+printSecDeposit+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+printPettyExpences+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+printOther+"</span></td><td style='padding:10px;border:1px solid #000;'>"+value.ENTRY_DATE+"</td><td style='padding:10px;border:1px solid #000;'>"+pendingid+"</td></tr>";*/

						debugger;
						raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><a   target='_blank' style='color:#0000ff;'  class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'>"+inrFormat(parseFloat(value.CERTIFIED_AMOUNT).toFixed(2))+"</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(printAdvAmount).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(printRecoveryAmt).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(printPettyExpences).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(printOther).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(printSecDeposit).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span class='amountPaid'>"+inrFormat(parseFloat(value.PAYBLE_AMOUNT).toFixed(2))+"</span></td>";
						raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span class='cumulativeTotal'>"+inrFormat((holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT)).toFixed(2))+"</span</td></tr>";
						holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT); 
						 
						 
				}	
			});
		
			// raHtmlData+=" <tr style='background-color: #ccc;'><td style='padding:10px;border:1px solid #000;'>Total Amount</td><td style='padding:10px;border:1px solid #000;'>"+(advPrevCertifiedAmount.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'>"+(raPrevCertifiedAmount.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'>"+(raPaidAmt+AdvPaidAmount).toFixed(2)+"</td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'>"+(deduction_amount.toFixed(2))+" </td><td style='padding:10px;border:1px solid #000;'>"+(RECOVERY.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'>"+(secDeposit.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'>"+(PETTY.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'>"+(OTHER.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'></td></tr>";

			raHtmlData+="<tr><td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(parseFloat(holdPrevAmount).toFixed(2))+"</span></td></tr>";
			
			raHtmlData+="<tr  style='border: 1px solid #000;background-color:#ccc;padding:5px;'><td style='padding:10px;border:1px solid #000;'>Total Amount</td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((parseFloat(raPrevCertifiedAmount).toFixed(2)))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((parseFloat(advPrevCertifiedAmount).toFixed(2)))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((parseFloat(deduction_amount).toFixed(2)))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span><span>"+inrFormat((parseFloat(RECOVERY).toFixed(2)))+"</span></span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span><span>"+inrFormat((parseFloat(PETTY).toFixed(2)))+"</span></span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((parseFloat(OTHER).toFixed(2)))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'>0.00</td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat((parseFloat(secDeposit).toFixed(2)))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'><span>"+inrFormat(holdPrevAmount.toFixed(2))+"</span></td>";
			raHtmlData+="<td style='padding:10px;border:1px solid #000;'></td></tr>";
			
		//	 raHtmlData+=" <tr><td>Total Amount</td><td>"+(raPrevCertifiedAmount+advPrevCertifiedAmount)+"</td><td>"+(raPaidAmt+AdvPaidAmount)+"</td><td></td><td>"+(secDeposit+PETTY+OTHER)+" </td><td>0</td><td>0</td><td>0</td><td>"+n+"</td><td></td></tr>";
			// $("#appendBillDetailsTotal").html("<div  style='font-size: 20px;'><label> Paid Amount  :</label> "+((raPaidAmt+AdvPaidAmount).toFixed(2))+" <br> <label>Total Deduction:</label> "+((deduction_amount+secDeposit+PETTY+OTHER+RECOVERY).toFixed(2))+" <br><label> Grand Total :</label> "+((raPrevCertifiedAmount+advPrevCertifiedAmount).toFixed(2))+"</div>");
			 
			 $("#appendBillDetailsTotal").html("<div  style='font-size: 20px;margin: 10px 20px 0px 0px;'><div style='width:450px;'><div style='float:left;width:200px;'> Paid Amount </div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'>"+inrFormat(((raPaidAmt+AdvPaidAmount).toFixed(2)))+"</div></div><div style='width:450px;'><div style='float:left;width:200px;'>Total Deduction</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'>"+inrFormat(((secDeposit+PETTY+OTHER+RECOVERY).toFixed(2)))+"</div></div><div style='width:450px;'><div style='float:left;width:200px;'>Grand Total</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'>"+inrFormat(((raPrevCertifiedAmount+advPrevCertifiedAmount-deduction_amount).toFixed(2)))+"</div></div></div>"); //deduction_amount+
			 
			 var raPage=$("#raPage").val();
		
			//AdvPaidAmount	 -Advance paid amount
			//deduction_amount	- advance duduction amount in RA Bill
			//raPaidAmt  -RA paid amount, with cutting all the duduction amount
			//raPrevCertifiedAmount	-Total RA initiated amount with out cutting's
			//ADV		=previous deducted amount in RA-advance deduction amount
		    //SEC		=previous deducted amount in RA-securtiy amount
			//PETTY		=previous deducted amount in RA-perry amount
			//OTHER		=previous deducted amount in RA-other amount
			//RECOVERY	=previous deducted amount in RA-newwly deducted amount 
		debugger;
			$("#outstandingAdvPrevAmt").val((AdvPaidAmount-deduction_amount).toFixed(2));
			//deduction_amount
			$("#releaseAdvTotalAmt").val(AdvPaidAmount.toFixed(2));
			$("#actualreleaseAdvTotalAmt").val(AdvPaidAmount.toFixed(2));
			//for create adv
			var currentDeductionAmount=parseFloat($("#raDeductionAmt").val()==""?0:$("#raDeductionAmt").val());
			if(AdvPaidAmount!=0){  //&&raPage!="false"
				$("#outstandingAdvTotalAmt").val((AdvPaidAmount-deduction_amount-currentDeductionAmount).toFixed(2));
				$("#outstandingAdvTotalAmthidden").val((AdvPaidAmount-deduction_amount-currentDeductionAmount).toFixed(2));
			}else{
				$("#outstandingAdvTotalAmt").val((AdvPaidAmount-currentDeductionAmount).toFixed(2));
			}
			
			$("#TotalPaidAmt").val(raPaidAmt.toFixed(2));
			$("#TotalPaidAmt1").val(raPaidAmt.toFixed(2));
			$("#totalAmtPreviousCertified").val(AdvPaidAmount.toFixed(2));
			$("#totalAmtCumulativeCertified").val(AdvPaidAmount.toFixed(2));
	
			var raAmountToPay=$("#raAmountToPay").val();
			if(raPage!=undefined&&raPage!=null){
			//for RA Page 
			//if(raPage!="false"){
					$("#raPc").val(raPrevCertifiedAmount.toFixed(2));
	
					if(raAmountToPay==""||raAmountToPay=="0"){
						raAmountToPay=0;
					}
				
					$("#raCc").val((raPrevCertifiedAmount+parseFloat(raAmountToPay)).toFixed(2));
			
					$("#raTotalPc").val(raPrevCertifiedAmount.toFixed(2));
					$("#raTotalCc").val((raPrevCertifiedAmount+parseFloat(raAmountToPay)).toFixed(2));
        			 if(approvePage=="true"){
        					$("#raeductionPrevCumulative").val((raPaidAmt+deduction_amount).toFixed(2));
        			 }else{
        					
        			 }
        			 
        			// $("#raPrevDeductionAmt").val(((raPaidAmt)+(deduction_amount+secDeposit+PETTY+OTHER)).toFixed(2));
			
					$("#raPrevDeductionAmt").val(deduction_amount.toFixed(2));
				
					$("#secDepositPrevCerti").val(secDeposit.toFixed(2));
					$("#pettyExpensesPrevCerti").val(PETTY.toFixed(2));
					$("#otherAmtPrevCerti").val(OTHER.toFixed(2));
					$("#raCcDeductionAmt").val(deduction_amount.toFixed(2));
					$("#raCcDeductionAmthidden").val(deduction_amount.toFixed(2));
					var currentrecovery=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
//					var currentrecovery=parseFloat($("#recoverycurrentAmount").val());
					$("#previousRecovery").val(RECOVERY.toFixed(2));
					$("#cumulativeRecovery").val((RECOVERY+currentrecovery).toFixed(2));
					
					appendPrevDeductVal+="<input type='hidden' name='secDepositPrevCertihidden' id='secDepositPrevCertihidden' value='"+secDeposit+"'>";
					appendPrevDeductVal+="<input type='hidden' name='pettyExpensesPrevCertihidden' id='pettyExpensesPrevCertihidden' value='"+PETTY+"'>";
					appendPrevDeductVal+="<input type='hidden' name='raPrevDeductionAmthidden' id='raPrevDeductionAmthidden' value='"+deduction_amount+"'>";
					appendPrevDeductVal+="<input type='hidden' name='otherAmtPrevCertihidden' id='otherAmtPrevCertihidden' value='"+OTHER+"'>";//for advance payment page
					$("#appendPrevDeductVal").html(appendPrevDeductVal);
					var advPage=$("#advPage").val();
				
					var raTotalCc= $("#raTotalCc").val();
					
					calculateAmountOfRA();
					
					if(advPage!="true"){
						$("#releaseAdvTotalAmt").val("");
						$("#totalAmtPreviousCertified").val("");
						$("#releaseAdvPrevAmt").val("");
						$("#totalAmtCumulativeCertified").val("");
//						$("#outstandingAdvPrevAmt").val("");
					
					}else {
						if(raPage!="true"){
						
						}
							
					}

			//	}
			}
			var billType=$("#billType").val();
			if(billType=="ADV"){
				$("#secDepositCurrentCerti").val('0.00');
				$("#pettyExpensesCurrentCerti").val('0.00');
				$("#raDeductionAmt").val('0.00');
				$("#other").val('0.00');
				$("#raPaidAmnt").val('0.00');
				$("#raAmountToPay").val('0.00');
				$("#totalCc").val('0.00');
				$("#raDeductionAmt").val('0.00');
				//$("#showingRecoveryinADV").val('0.00');
			}
		var amountInWords=convertNumberToWords((raPaidAmt+AdvPaidAmount));
		if(parseInt((raPaidAmt+AdvPaidAmount))!=0){
			$("#amountInWords").text(amountInWords);
		}
		if(parseInt((raPaidAmt+AdvPaidAmount))<0 ){
			$("#amountInWords").text("");
		}
		if(parseInt((raPaidAmt+AdvPaidAmount))==0){
			$("#amountInWords").text("Zero Rupees Only");
		}
		
		if(raPaidAmt!=0||AdvPaidAmount!=0){
			$("#appendBillDetails").append(advHtmlData);
			$("#appendBillDetails").append(raHtmlData);
			$("#hideIt").show();
			$("#appendBillDetailsTotal").show();
		}else{
			$("#hideIt").hide();
			$("#appendBillDetailsTotal").hide();
		}	
			
			htmlData+="<input type='hidden'  name='releaseAdvPrevAmt11' id='releaseAdvPrevAmt11' style='border:none; text-align:center;' value='"+AdvPaidAmount+"' readonly/>";
			htmlData+="<input type='hidden'  name='releaseAdvTotalAmt11' id='releaseAdvTotalAmt11' style='border:none; text-align:center;' value='"+(AdvPaidAmount==""?0:AdvPaidAmount)+"' readonly/>";
			htmlData+="<input type='hidden'  name='outstandingAdvTotalAmt11' id='outstandingAdvTotalAmt11' style='border:none; text-align:center;' value='"+AdvPaidAmount+"' readonly/>";
		
			$("#appendBillDetails1").append(htmlData);
			caldeductionamt();
			$(".addFractionAndMakeInrFormat").each(function(){
				debugger;
				var value=$(this).val();
				var currentvalue=value==""?0:parseFloat(value);
				$(this).text(inrFormat(currentvalue.toFixed(2)));
			});
		},error:function(errer){
			
		}
});
	
	loadPaymentWorkArea();
}


function convertNumberToWords(amount) {
    var words = new Array();
    words[0] = '';
    words[1] = 'One';
    words[2] = 'Two';
    words[3] = 'Three';
    words[4] = 'Four';
    words[5] = 'Five';
    words[6] = 'Six';
    words[7] = 'Seven';
    words[8] = 'Eight';
    words[9] = 'Nine';
    words[10] = 'Ten';
    words[11] = 'Eleven';
    words[12] = 'Twelve';
    words[13] = 'Thirteen';
    words[14] = 'Fourteen';
    words[15] = 'Fifteen';
    words[16] = 'Sixteen';
    words[17] = 'Seventeen';
    words[18] = 'Eighteen';
    words[19] = 'Nineteen';
    words[20] = 'Twenty';
    words[30] = 'Thirty';
    words[40] = 'Forty';
    words[50] = 'Fifty';
    words[60] = 'Sixty';
    words[70] = 'Seventy';
    words[80] = 'Eighty';
    words[90] = 'Ninety';
    amount = amount.toString();
    var atemp = amount.split(".");
    var number = atemp[0].split(",").join("");
    var n_length = number.length;
    var words_string = "";
    if (n_length <= 9) {
        var n_array = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0);
        var received_n_array = new Array();
        for (var i = 0; i < n_length; i++) {
            received_n_array[i] = number.substr(i, 1);
        }
        for (var i = 9 - n_length, j = 0; i < 9; i++, j++) {
            n_array[i] = received_n_array[j];
        }
        for (var i = 0, j = 1; i < 9; i++, j++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                if (n_array[i] == 1) {
                    n_array[j] = 10 + parseInt(n_array[j]);
                    n_array[i] = 0;
                }
            }
        }
        value = "";
        for (var i = 0; i < 9; i++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                value = n_array[i] * 10;
            } else {
                value = n_array[i];
            }
            if (value != 0) {
                words_string += words[value] + " ";
            }
            if ((i == 1 && value != 0) || (i == 0 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "crore";
            }
            if ((i == 3 && value != 0) || (i == 2 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "Lakhs ";
            }
            if ((i == 5 && value != 0) || (i == 4 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "Thousand ";
            }
            if (i == 6 && value != 0 && (n_array[i + 1] != 0 && n_array[i + 2] != 0)) {
                words_string += "Hundred and ";
            } else if (i == 6 && value != 0) {
                words_string += "Hundred ";
            }
        }
        words_string = words_string.split("  ").join(" ");
    }
    return words_string+" Rupees Only";
}
function sumOfArea(){
	//ACP
	let sumOfCurrentQty=0;
	let sumOfCurrentAmount=0;
	let sumOfCumulativeQty=0;
	let sumOfCumulativeAmount=0;
	//ACP
	$.each($("input[name='chk1']:checked"), function(){
		let i=$(this).val();
		let CCQty=$("#CCQty"+i).val()==""?0:$("#CCQty"+i).val();
		sumOfCurrentAmount+=parseFloat($("#CCAmount"+i).text()==""?0:$("#CCAmount"+i).text());
		sumOfCumulativeAmount+=parseFloat($("#CBAmount"+i).text());
		sumOfCumulativeQty+=parseFloat($("#CBQty"+i).text());
		sumOfCurrentQty+=(parseFloat(CCQty));	
	});
 
	$("#cumulativeQuantity").text(sumOfCumulativeQty.toFixed(2));
	$("#cumulativeAmount").text(sumOfCumulativeAmount.toFixed(2));
	$("#currentQuantity").text(sumOfCurrentQty.toFixed(2));
	$("#currentAmount").text(sumOfCurrentAmount.toFixed(2));
}
var paymentareaAmnt=0;
function checkQuantity(id){

	
	var CCQty=$("#CCQty"+id).val();
	if(CCQty=="0"||CCQty==""){
		alert("Please enter current certified quantity");
		$("#CCQty"+id).focus();
		$("#CCQty"+id).val("0");
		$("#chk1"+id).prop("checked",false);
		return false;
	}
	if($("#chk1"+id).prop("checked") == true){		
		var areaamnt=$("#CCAmount"+id).html();
		paymentareaAmnt+=parseFloat(areaamnt);
	}
	if($("#chk1"+id).prop("checked") == false){
		var areaamnt=$("#CCAmount"+id).html();
		paymentareaAmnt-=parseFloat(areaamnt);
	}
	$("#hiddenpaymentareaAmt").val(paymentareaAmnt);
	sumOfArea();
}

function romanize(num) {
	  var lookup = {M:1000,CM:900,D:500,CD:400,C:100,XC:90,L:50,XL:40,X:10,IX:9,V:5,IV:4,I:1},roman = '',i;
	  for ( i in lookup ) {
	    while ( num >= lookup[i] ) {
	      roman += i;
	      num -= lookup[i];
	    }
	  }
	  return roman;
	}
//Calculating payment Ra bill written by thirupathi
function calculateAmountOfRA(){
	var approvePage=$("#approvePage").val();
	var raeductionPrevCumulative=$("#raeductionPrevCumulative").val()==""?0:parseFloat($("#raeductionPrevCumulative").val());
	//if(approvePage=="NaN"){
		raeductionPrevCumulative=$("#raPrevDeductionAmt").val()==""?0:parseFloat($("#raPrevDeductionAmt").val());	
		$("#raCcDeductionAmt").val(raeductionPrevCumulative.toFixed(2));
//	}
		
	var secDepositPrevCerti=$("#secDepositPrevCerti").val()==""?0:parseFloat($("#secDepositPrevCerti").val());
	var pettyExpensesPrevCerti=$("#pettyExpensesPrevCerti").val()==""?0:parseFloat($("#pettyExpensesPrevCerti").val());
	var otherAmtPrevCerti=$("#otherAmtPrevCerti").val()==""?0:parseFloat($("#otherAmtPrevCerti").val());

	var secDepositCurrentCerti=$("#secDepositCurrentCerti").val()==""?0:parseFloat($("#secDepositCurrentCerti").val());
	var pettyExpensesCurrentCerti=$("#pettyExpensesCurrentCerti").val()==""?0:parseFloat($("#pettyExpensesCurrentCerti").val());
	var raDeductionAmt=$("#raDeductionAmt").val()==""?0:parseFloat($("#raDeductionAmt").val());
	var other=$("#other").val()==""?0:parseFloat($("#other").val());
	var raTotalPc=$("#raTotalPc").val()==""?0:parseFloat($("#raTotalPc").val());

	$("#raCcDeductionAmt").val((raeductionPrevCumulative+raDeductionAmt).toFixed(2));
	$("#secDepositCumulative").val((secDepositPrevCerti+secDepositCurrentCerti).toFixed(2));
	$("#pettyExpensesCumulative").val((pettyExpensesPrevCerti+pettyExpensesCurrentCerti).toFixed(2));
	$("#otherAmtCumulative").val((otherAmtPrevCerti+other).toFixed(2));
	
	var raeductionCumulative=$("#raCcDeductionAmt").val()==""?0:parseFloat($("#raCcDeductionAmt").val());
	var secDepositCumulative=$("#secDepositCumulative").val()==""?0:parseFloat($("#secDepositCumulative").val());
	var pettyExpensesCumulative=$("#pettyExpensesCumulative").val()==""?0:parseFloat($("#pettyExpensesCumulative").val());
	var otherAmtCumulative=$("#otherAmtCumulative").val()==""?0:parseFloat($("#otherAmtCumulative").val());
	
	
	var totalCumulative=raeductionCumulative+secDepositCumulative+pettyExpensesCumulative+otherAmtCumulative;
	var raTotaoPrevAmt=	raeductionPrevCumulative+secDepositPrevCerti+pettyExpensesPrevCerti+otherAmtPrevCerti;
	$("#totalAmtPrevCerti").val(raTotaoPrevAmt.toFixed(2));
	$("#totalAmtCumulative").val(totalCumulative.toFixed(2));
	var raTotalCc=$("#raTotalCc").val()==""?0:parseFloat($("#raTotalCc").val());
	$("#totalAmtCumulativeCertified").val(raTotalCc-totalCumulative);
	
	$("#totalAmtPreviousCertified").val(raTotalPc-raTotaoPrevAmt);
	var radedctiontempval=$("#raCcDeductionAmt").val();
//	$("#raCcDeductionAmt").val((parseFloat(radedctiontempval)-raTotaoPrevAmt).toFixed(2));
	var  outstandingAdvTotalAmt=parseFloat($("#outstandingAdvTotalAmt").val()==""?0:$("#outstandingAdvTotalAmt").val());
	var advanceCurrAmount=parseFloat($("#advanceCurrAmount").val()==""?0:$("#advanceCurrAmount").val());
	$("#outstandingAdvTotalAmt").val((outstandingAdvTotalAmt+advanceCurrAmount).toFixed(2));
	$("#advanceCurrAmount").val(parseFloat($("#advanceCurrAmount").val()==""?0:$("#advanceCurrAmount").val()).toFixed(2));
	$("#advanceCurrAmount1").val(parseFloat($("#advanceCurrAmount").val()==""?0:$("#advanceCurrAmount").val()).toFixed(2));
}

function validateFloatKeyPress(el) {
	var v = parseFloat(el.value);
    el.value = (isNaN(v)) ? '' : v.toFixed(2);
}
//calculating Abstract when you are changing quantity
function calculatePayRabill(btn){
	let sumOfCurrentQty=0;
	let sumOfCurrentAmount=0;
	let sumOfCumulativeQty=0;
	let sumOfCumulativeAmount=0;
	 var TotalQty=$("#TotalQty"+btn).html();
	 var TotalRate=$("#TotalRate"+btn).html();
	 var CBQty=$("#CBQty"+btn).html();
	 var CBAmount=$("#CBAmount"+btn).html();
	 var PCQty=$("#PCQty"+btn).html();
	 var PCAmount=$("#PCAmount"+btn).html();
	 var CCQty=$("#CCQty"+btn).val();
	 var CCAmount=$("#CCAmount"+btn).html();	 
	 var RA_amountTotalQTY=$("#RA_amountTotalQTY"+btn).val();
 
 if(CCQty==""||CCQty=="0"){
	 CCQty=0;
	$("#chk1"+btn).prop("checked",false);
 }
 if(CCAmount==""){
	 CCAmount=0;
 }
 if(CBQty==""){
	 CBQty=0;
 }
 if(CBAmount==""){
	 CBAmount=0;
 }
 if(PCQty==""){
	 PCQty=0;
 }
 if(PCAmount==""){
	 PCAmount=0;
 }
 if(RA_amountTotalQTY==""){
	 RA_amountTotalQTY=0;
 }
 //if((parseFloat(PCQty)+parseFloat(CCQty))>TotalQty){
 var billType=$("#billType").val();
 if(billType=="RA"){
	 RA_amountTotalQTY= parseFloat(PCQty);
 }
 if((parseFloat(RA_amountTotalQTY)+parseFloat(CCQty))>TotalQty){  //+parseFloat(PCQty)	
	 alert("It's seems area is allocating more than the total area, Please change the area.");
	 CCQty=0;
	 $("#CCQty"+btn).val('0');
	 var calQnty=parseFloat(TotalRate)*parseFloat(CCQty);
	 $("#CCAmount"+btn).html((calQnty).toFixed(2));
	 var clacCBQnty=parseFloat(PCQty)+parseFloat(CCQty);
	 $("#CBQty"+btn).html((clacCBQnty).toFixed(2));
	 var calCBAmount=parseFloat($("#CCAmount"+btn).html())+parseFloat(PCAmount);
	 $("#CBAmount"+btn).html((calCBAmount).toFixed(2));
	$("#chk1"+btn).prop("checked",false);
 }
 else{
	 var calQnty=parseFloat(TotalRate)*parseFloat(CCQty);
	 $("#CCAmount"+btn).html((calQnty).toFixed(2));
	 var clacCBQnty=parseFloat(PCQty)+parseFloat(CCQty);
	 $("#CBQty"+btn).html((clacCBQnty).toFixed(2));
	 var calCBAmount=parseFloat($("#CCAmount"+btn).html())+parseFloat(PCAmount);
	 $("#CBAmount"+btn).html((calCBAmount).toFixed(2));
 }
 var len=$("input[name='chk1']").length;
	$.each($("input[name='chk1']:checked"), function(){
		let i=$(this).val();
		let CCQty=$("#CCQty"+i).val()==""?0:$("#CCQty"+i).val();
		sumOfCurrentAmount+=parseFloat($("#CCAmount"+i).text()==""?0:$("#CCAmount"+i).text());
		sumOfCumulativeAmount+=parseFloat($("#CBAmount"+i).text());
		sumOfCumulativeQty+=parseFloat($("#CBQty"+i).text());
		sumOfCurrentQty+=(parseFloat(CCQty));	
	});
 
	
	$("#cumulativeQuantity").text(sumOfCumulativeQty.toFixed(2));
	$("#cumulativeAmount").text(sumOfCumulativeAmount.toFixed(2));
	$("#currentQuantity").text(sumOfCurrentQty.toFixed(2));
	$("#currentAmount").text(sumOfCurrentAmount.toFixed(2));	
}

//validating contractor name, wo number, billdate and advance amount
function validateTextFileds(){
	var ContractorId=$("#ContractorId").val();
	var workOrderNo=$("#workOrderNo").val();
	var billingDate=$("#billDate").val();
	var advanceCurrAmount=$("#raAmountToPay").val();
		
	if(ContractorId.length==0){
		$("#ContractorName").focus();
		alert("Please enter contractor name.");
		return false;
	}
   if(workOrderNo.length==0){
	   $("#workOrderNo").focus()
		alert("Please select work order number.");
		return false;
	}
	if(billingDate==""||billingDate==null||billingDate==undefined){
		$("#billDate").focus();
		alert("Please select date");
		return false;
	}
	if(advanceCurrAmount==""||advanceCurrAmount==null||advanceCurrAmount==undefined){
		$("#raAmountToPay").focus();
		alert("Please enter amount.");
		return false;
	}
	
}

function validateBillInvoiceNumber(workOrderNo,ContractorId,oldBillNo,isInvoiceOrBillNumberValidation){
	debugger;
	var error=true;
	var url = "checkBillNoExistsOrNot.spring";
	 $.ajax({
		  url : url,
		  type : "get",
		  async: false,
		 data:{
			 workOrderNo:workOrderNo,
			 ContractorId:ContractorId	,
			 oldBillNo:oldBillNo,
			 isInvoiceOrBillNumberValidation:isInvoiceOrBillNumberValidation
		 },
		  contentType : "application/json",
		  success : function(data) {
				if(data!="true"){
					 $('#rasubmitBtn').attr('disabled',false);
					 $('#submitBtn').attr('disabled',false);	
				}else{
					alert("Bill number already exists, please enter another bill number.");								
					$('#rasubmitBtn').attr('disabled',true);
					$('#submitBtn').attr('disabled',true);
					if(isInvoiceOrBillNumberValidation=="invoiceNumber"){
						$("#billInvoiceNo").val("");
						$("#billInvoiceNo").focus();
					}
					return error=false;
				}
		  }
	});
	 return error;
 }

function validateInvoiceBillNumber(billInvoiceNo,actualBillInvoiceNo){
	debugger;
	var error=true;
	var actualarray=actualBillInvoiceNo.split("/")
	
	var array=new Array();
	array=billInvoiceNo.split("/");
	
	if(billInvoiceNo.length==0){
		alert("Please enter invoice number.");
		$("#billInvoiceNo").focus();
		return false;
	}else{
		if(actualarray.length!=array.length){
			 alert("Please enter correct format number of bill invoice.");
				$('#rasubmitBtn').attr('disabled',true);
				$('#submitBtn').attr('disabled',true);
				$("#billInvoiceNo").focus();
				return false;
		}
		if(!isNum(array[array.length-1])){
			 alert("Please enter correct format number of bill invoice.");
				$('#rasubmitBtn').attr('disabled',true);
				$('#submitBtn').attr('disabled',true);
				$("#billInvoiceNo").focus();
				return false;
		}
		
		for (var index = 0; index < actualarray.length-1; index++) {
			 if(actualarray[index]!=array[index]){
				 alert("Please enter correct format number of bill invoice.");
					$('#rasubmitBtn').attr('disabled',true);
					$('#submitBtn').attr('disabled',true);
					$("#billInvoiceNo").focus();
					$("#billInvoiceNo").val(actualBillInvoiceNo);
				return error=false;
			 }
		}
	}
	return error;
}

$(document).ready(function(){
	//comboboxworkOrderNo
	//For RA page
	setTimeout( function(){
		var approvePage=$("#approvePage").val();
		if(approvePage!=undefined&&approvePage!=null&&approvePage!="false"){
			loadRARecovery();
			workOrderNumChanged();
		}
		
	}, 1500)
	//contractor copy for print
	$("#contractorCopy").on("click",function(){
		var flag=validateTextFileds();
		if(flag==false){return false;}
		var contractorName=$("#ContractorName").val();
		 var contractorId=$("#ContractorId").val();
		 var url="contractorCopy.spring";
	      document.getElementById("rasubmitBtn").disabled = true; 
	      document.getElementById("contractorCopy").disabled = true; 
	      
	      document.getElementById("contractorRABill").action =url;
	      document.getElementById("contractorRABill").method = "POST";
	      document.getElementById("contractorRABill").submit();
		 //contractorRABill
	});
	//ra page submit button for saving data
	$("#rasubmitBtn").on("click",function(){
		debugger;
		var error=true;
		var isOldOrNewBill=$("#isOldOrNewBill").val();
		if(isOldOrNewBill=="Old"){
			var oldBillNo=$("#oldBillNo").val();
			var array=new Array();
			array=oldBillNo.split("/");
			var billType=$("#billType").val();
			if(array[0]!=billType){
				alert("please enter correct format of Bill Number.");
				$("#oldBillNo").val('');
				return false;
			}
			if(!isNum(array[1])){
				alert("please enter correct format of Bill Number.");
				$("#oldBillNo").val('');
				return false;
			}
		}
		
		var ContractorId=$("#ContractorId").val();
		var workOrderNo=$("#workOrderNo").val();
		var billInvoiceNo=$("#billInvoiceNo").val()==undefined?"":$("#billInvoiceNo").val().trim();
		var actualBillInvoiceNo=$("#actualBillInvoiceNo").val();
		
		error=validateInvoiceBillNumber(billInvoiceNo,actualBillInvoiceNo);
		if(error==false){
			return false;
		}else{
			$('#rasubmitBtn').attr('disabled',false);
			$('#submitBtn').attr('disabled',false);
		}
		//if(actualBillInvoiceNo!=billInvoiceNo){
		var isInvoiceOrBillNumberValidation="invoiceNumber";
		var status=validateBillInvoiceNumber(workOrderNo,ContractorId,billInvoiceNo,isInvoiceOrBillNumberValidation);
		if(status==false){
			return false;
		}
		
		var CrntCertifiedid=$("#raAmountToPay").val();
		 
		if(CrntCertifiedid=="0"||CrntCertifiedid=="0.0"||CrntCertifiedid=="0.00"||CrntCertifiedid==""){
			alert("The bill paying amount is zero, can not submit the bill.");
			return false;
		}
		var totalAmtToPay=parseFloat($("#totalAmtToPay").val()==""?0:$("#totalAmtToPay").val().replace(/,/g,''));
		var raPcAmt=$("#raPc").val()==""?0:$("#raPc").val();
		var raDeductionAmt=$("#raDeductionAmt").val()==""?0:$("#raDeductionAmt").val();
		if(CrntCertifiedid==''){
			CrntCertifiedid=0;
		}
		var utstandingamnt=$("#outstandingAdvTotalAmt").val()==""?0:$("#outstandingAdvTotalAmt").val();
		var raccamnt=$("#raCc").val();
		debugger;
		var outstandracc=parseFloat(CrntCertifiedid)+parseFloat(raPcAmt)+parseFloat(utstandingamnt);//+parseFloat(raDeductionAmt)
		outstandracc=outstandracc.toFixed(2);
		if(totalAmtToPay<outstandracc){
			alert("This Amount is greater than WO Amount.");
			//$("#raAmountToPay").val('');		
			$("#raAmountToPay").focus();
			return false;
			//CrntCertifiedid=0;
			
		}
		var caltotal=caldeductionamt();
		if(caltotal==false){
			return false;
		}
		validateWoOrderAmnt();
		var flag=validateTextFileds();
		if(flag==false){return false;}
		var len=$("input[name='chk1']:checked").length;
		
		if(len==0){
			alert("Please select payment work order area.");
			$("#tbl-2").show();
			$('#modal-certificatepaymentra-click').modal('show'); 
			return false;
		}
 		
		    $("#appendWorkAreaId").html("");
			$.each($("input[name='chk1']:checked"), function(){  
				var htmlData="";
				var thisVal=$(this).val();
				var workAreaId=$("#workAreaId"+thisVal).val();
				var block_name=$("#"+thisVal+"BLOCK_NAME").text().trim();
				var area=$("#CCQty"+thisVal).val().trim();
				var totalSelectedAreaAMount=$("#CCAmount"+thisVal).text().trim();
				var actualQty=$("#ActualQTY"+thisVal).val();
				var initiatedQunatity=$("#CBQty"+thisVal).text();
				var wo_work_issue_area_dtls_id=$("#WO_WORK_ISSUE_AREA_DTLS_ID"+thisVal).val();
				var totalQTY=$("#TotalQty"+thisVal).text();
				var TotalRate=$("#TotalRate"+thisVal).text();
				htmlData+="<input type='hidden'  name='totalWOQuantity'   value='"+totalQTY+"'/>";
				htmlData+="<input type='hidden'  name='WOQuantityRate'   value='"+TotalRate+"'/>";
				/*htmlData+="<input type='hidden'  name='initiatedQunatity'   value='"+initiatedQunatity+"'/>";*/
				htmlData+="<input type='hidden'  name='initiatedQunatity'   value='"+initiatedQunatity+"'/>";
				htmlData+="<input type='hidden'  name='block_name'value='"+block_name+"' />";
				htmlData+="<input type='hidden'  name='workAreaId'  value='"+workAreaId+"' />";
				htmlData+="<input type='hidden'  name='qty'  value='"+area+"' />";
				htmlData+="<input type='hidden'  name='ActualQTY'  value='"+actualQty+"' />";
				htmlData+="<input type='hidden'  name='wo_work_issue_area_dtls_id'  value='"+wo_work_issue_area_dtls_id+"' />";
				
				$("#appendWorkAreaId").append(htmlData);
			});
			var str="";
			var rowsToIterate=$("#rowsToIterate").val();
			$("#recoveryAmountDetails").append("");
			var row1=0;
			for (var i = 1; i <= rowsToIterate; i++) {
				debugger;
				var htmlData="";
				var child_product_id=$("#childId"+i).val();
				var recovery_quantity=$("#"+child_product_id+"totalRecoveryAmount").text();
				var issuedQty=$("#"+child_product_id+"totalRecoveryAmount").text();
				var recovery_amount=$("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val();
				var mesurment_id=$("#"+child_product_id+"mesurment_id").val();
				if(recovery_amount!="0"){
					row1++;
					htmlData+="<input type='hidden' name='child_product_id"+row1+"' id='child_product_id"+i+"' value='"+child_product_id+"'>";
					htmlData+="<input type='hidden' name='measurement_id"+row1+"' id='measurement_id"+i+"' value='"+mesurment_id+"'>";
					htmlData+="<input type='hidden' name='recovery_amount"+row1+"' id='recovery_amount"+i+"' value='"+(recovery_amount)+"'>";
					htmlData+="<input type='hidden' name='recovery_quantity"+row1+"' id='recovery_quantity"+i+"' value='"+(issuedQty)+"'>";
				$("#recoveryAmountDetails").append(htmlData);
			
			
				}
			}
			
			$("#recoveryAmountDetails").append("<input type='hidden' name='rowsToIterate1' id='rowsToIterate1' value='"+row1+"'>");
			var canISubmit = window.confirm("Do you want to Submit?");	     
			if(canISubmit == false) {return false; }
			 $(".overlay_ims").show();	
			 $(".loader-ims").show(); 			 
			$('#rasubmitBtn').attr('disabled',true);
			$('#submitBtn').attr('disabled',true);
			$("#rowsToIterate").val(row1);
			$("#contractorRABill").submit();
	});
	//advance bill submit function
	$("#submitBtn").on("click",function(){
			debugger;
	var error=true;
	var isOldOrNewBill=$("#isOldOrNewBill").val();
	if(isOldOrNewBill=="Old"){
		var oldBillNo=$("#oldBillNo").val();
		var array=new Array();
		array=oldBillNo.split("/");
		var billType=$("#billType").val();
		if(array[0]!=billType){
			alert("please enter correct format of Bill Number.");
			$("#oldBillNo").val('');
			return false;
		}
		if(!isNum(array[1])){
			alert("please enter correct format of Bill Number.");
			$("#oldBillNo").val('');
			return false;
		}
	}
	
	var ContractorId=$("#ContractorId").val();
	var workOrderNo=$("#workOrderNo").val();
	var billInvoiceNo=$("#billInvoiceNo").val()==undefined?"":$("#billInvoiceNo").val().trim();
	var actualBillInvoiceNo=$("#actualBillInvoiceNo").val();

	error=validateInvoiceBillNumber(billInvoiceNo,actualBillInvoiceNo);
	if(error==false){
		return false;
	}else{
		$('#rasubmitBtn').attr('disabled',false);
		$('#submitBtn').attr('disabled',false);
	}
	var isInvoiceOrBillNumberValidation="invoiceNumber";
	var status=validateBillInvoiceNumber(workOrderNo,ContractorId,billInvoiceNo,isInvoiceOrBillNumberValidation);
	if(status==false){
		return false;
	}
	
	
		var finalAmt=$("#finalAmt").val();
		if(finalAmt=="0"||finalAmt=="0.0"||finalAmt=="0.00"||finalAmt==""){
			alert("The bill paying amount is zero, can not submit the bill.");
			return false;
		}
		
		
		var TotalPaidAmt1=$("#TotalPaidAmt1").val();
		var totalAmtToPay=parseFloat($("#totalAmtToPay").val().replace(/,/g,''));
		if(TotalPaidAmt1==''){ TotalPaidAmt1=0; }
		var utstandingamnt=$("#outstandingAdvTotalAmt").val();	
		var raCc=$("#raCc").val()==''?0:$("#raCc").val();
		var outstandracc=parseFloat(raCc)+parseFloat(utstandingamnt);
		outstandracc=outstandracc.toFixed(2);
		if(totalAmtToPay<outstandracc){
			alert("This Amount is greater than WO Amount.");
			$("#finalAmt").val("0.00");
			$("#totalAmtCumulativeCertified").val("0.00");			
			return false;
		}
		var ContractorName=$("#ContractorName").val();
		if(ContractorName==""||ContractorName==null||ContractorName==undefined){
			$("#ContractorName").focus();
			alert("Please enter contractor name.");
			return false;
		}
		var workOrderNo=$("#workOrderNo").val();
		if(workOrderNo==""||workOrderNo==null||workOrderNo==undefined){
			$("#workOrderNo").focus();
			alert("Please select work order number.");
			return false;
		}
		var typeofworkselect=$("#typeofworkselect").val();
		if(typeofworkselect==""||typeofworkselect==null||typeofworkselect==undefined){
			$("#typeofworkselect").focus();
			alert("Please select type of work.");
			return false;
		}
		var billingDate=$("#billDate").val();
		if(billingDate==""||billingDate==null||billingDate==undefined){
			$("#billDate").focus();
			alert("Please select date.");
			return false;
		}
		var advanceCurrAmount=$("#advanceCurrAmount").val();
		if(advanceCurrAmount==""||advanceCurrAmount==null||advanceCurrAmount==undefined){
			$("#advanceCurrAmount").focus();
			alert("Please enter amount.");
			return false;
		}
		
		var len=$("input[name='chk1']:checked").length;
		if(len==0){
			alert("Please select payment work order area.");
			$('#modal-certificatepayment-adv').modal('show'); 
			$("#tbl-2").show();
			return false;
		}
	    $("#appendWorkAreaId").html("");
		$.each($("input[name='chk1']:checked"), function(){ 
			var htmlData="";
			var thisVal=$(this).val();
			var workAreaId=$("#workAreaId"+thisVal).val();
			var block_name=$("#"+thisVal+"BLOCK_NAME").text().trim();
			var area=$("#CCQty"+thisVal).val().trim();
			var totalSelectedAreaAMount=$("#CCAmount"+thisVal).text().trim();
			var actualQty=$("#ActualQTY"+thisVal).val();
			var initiatedQunatity=$("#CBQty"+thisVal).text();
			var wo_work_issue_area_dtls_id=$("#WO_WORK_ISSUE_AREA_DTLS_ID"+thisVal).val();
			
			var totalQTY=$("#TotalQty"+thisVal).text();
			var TotalRate=$("#TotalRate"+thisVal).text();
			htmlData+="<input type='hidden'  name='totalWOQuantity'   value='"+totalQTY+"'/>";
			htmlData+="<input type='hidden'  name='WOQuantityRate'   value='"+TotalRate+"'/>";
				
			htmlData+="<input type='hidden'  name='initiatedQunatity'   value='"+initiatedQunatity+"'/>";
			htmlData+="<input type='hidden'  name='block_name'value='"+block_name+"' />";
			htmlData+="<input type='hidden'  name='workAreaId'  value='"+workAreaId+"' />";
			htmlData+="<input type='hidden'  name='qty'  value='"+area+"' />";
			htmlData+="<input type='hidden'  name='ActualQTY'  value='"+actualQty+"' />";	
			htmlData+="<input type='hidden'  name='wo_work_issue_area_dtls_id'  value='"+wo_work_issue_area_dtls_id+"' />";
			$("#appendWorkAreaId").append(htmlData);
		});
		
		 var canISubmit = window.confirm("Do you want to Submit?");
	     
		    if(canISubmit == false) {
		        return false;
		    }
		    $(".overlay_ims").show();	
			 $(".loader-ims").show(); 
			 
		    $('#submitBtn').attr('disabled',true);
		    $('#contractorBill').submit();
	});
	
	$("#isOldOrNewBill").on("change",function(){
		var isOldOrNewBill=$(this).val();
		if(isOldOrNewBill=="Old"){
			$("#showBillText").show();
		}else{
			$("#showBillText").hide();	
		}
	});
	

	
	//work order number change
	 $("#workOrderNo").on("change",function(){
			$(".overlay_ims").show();
			$(".loader-ims").show();
		 debugger;
		 var num=$(this).val().split("$")[2];
		 $("#totalAmtToPay").val(inrFormat(num));
		 $('#rasubmitBtn').attr('disabled',false);
		 $('#submitBtn').attr('disabled',false);
		 var result=validateForm();
		 
		 if(result==false){	return false; }
		 
		 clearAllValues();
		 loadRARecovery();
		 workOrderNumChanged();
		 loadADVRABillNo();
	 });
	 
	//For load contractor Details
	var approvePage=$("#approvePage").val();
	var raPage=	 $("#raPage").val();
	if(raPage!=undefined&&approvePage!=undefined&&raPage!=null&&approvePage!="false"){
	var url = "loadAndSetVendorInfoForWO.spring";
		 $.ajax({
			  url : url,
			  type : "get",
			 data:{
				 contractorName:$("#ContractorName").val(),
				 loadcontractorData:true	 
			 },
			  contentType : "application/json",
			  success : function(data) {
				 if(data!=""||data!="null"){
					 var contractorData=data[0].split("@@");
					var contractorId=contractorData[0];
				
					$("#ContractorId").val(contractorId);
					$("#MobileNo").val(contractorData[3]);
					$("#PanCardNo").val(contractorData[4]);
					$("#AccountNo").val(contractorData[5]);
					$("#ifscCode").val(contractorData[6]+" & "+contractorData[8]);
					loadWorkOrderNo();
				  }
				
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
}//condition
	 
 });//$('#Modal-create-wo-popup').modal('show'); 

//calculating security amount
function securityperchange1(btn){
	var secperamt=$("#securityPer"+btn).val();
	var totalAamt=$("#totalCc").val();
	var secpervalamt=parseFloat(totalAamt)*(parseFloat(secperamt)/100);
	$("#raDeductionAmt"+btn).val(secpervalamt);
	$("#raDeductionAmt"+btn).attr("readonly", true);
	caldeductionamt1(btn);
} 

function caldeductionamt1(id){
	var result=validateForm();
	if(result==false){
		return false;
	}
	var totalamntB=0;
	var pctotalamntB=0;
	var cctotalamntB=0;
	 var value=0;
	var PettyExpensesCC=0;	
	var OthersCC=0;
	var AdvanceDeductionCC=0;
	var dedval=$("#deduction"+id).val();
	var Outstandingval=parseFloat($("#outstandingAdvTotalAmt").val());
	if(dedval=="1"){
		var deductionamt=$("#raDeductionAmt"+id).val();
		var deductionPCAmnt=$("#raPrevDeductionAmt"+id).val();
		if(deductionamt==''){
			deductionamt=0;			
		}
		if(deductionPCAmnt==''){
			deductionPCAmnt=0;
		}
		if(deductionamt>Outstandingval){			
			alert("This Amount is greater than OutStanding Amount.");
			$("#raDeductionAmt"+id).val('');
			deductionamt=0;	
		}
		AdvanceDeductionCC=parseFloat(deductionPCAmnt)+parseFloat(deductionamt);
		$("#raCcDeductionAmt"+id).val(AdvanceDeductionCC.toFixed(2));
	}
	if(dedval=="2"){
		var deductionamt=$("#raDeductionAmt"+id).val();
		var deductionPCAmnt=$("#raPrevDeductionAmt"+id).val();
		if(deductionamt==''){
			deductionamt=0;			
		}
		if(deductionPCAmnt==''){
			deductionPCAmnt=0;
		}	
		AdvanceDeductionCC=parseFloat(deductionPCAmnt)+parseFloat(deductionamt);
		$("#raCcDeductionAmt"+id).val(AdvanceDeductionCC.oFixed(2));
	}
	if(dedval=="3"){
		var deductionamt=$("#raDeductionAmt"+id).val();
		var deductionPCAmnt=$("#raPrevDeductionAmt"+id).val();
		if(deductionamt==''){
			deductionamt=0;			
		}
		if(deductionPCAmnt==''){
			deductionPCAmnt=0;
		}		
		AdvanceDeductionCC=parseFloat(deductionPCAmnt)+parseFloat(deductionamt);
		$("#raCcDeductionAmt"+id).val(AdvanceDeductionCC.toFixed(2));
	}
	if(dedval=="4"){
		var deductionamt=$("#raDeductionAmt"+id).val();
		var deductionPCAmnt=$("#raPrevDeductionAmt"+id).val();
		if(deductionamt==''){
			deductionamt=0;			
		}
		if(deductionPCAmnt==''){
			deductionPCAmnt=0;
		}		
		AdvanceDeductionCC=parseFloat(deductionPCAmnt)+parseFloat(deductionamt);
		$("#raCcDeductionAmt"+id).val(AdvanceDeductionCC.toFixed(2));
	}		
	jQuery('.raDeductionAmt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){
		    	value=0;
		   }
		  totalamntB+=parseFloat(value);
	});
	
	jQuery('.PcAmnt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){
		    	value=0;
		   }
		  pctotalamntB+=parseFloat(value);
	});
	jQuery('.CcAmnt').each(function() {
		  var currentElement = $(this);
		      value = currentElement.val();
		  if(value==""){
		    	value=0;
		   }
		  cctotalamntB+=parseFloat(value);
	});
	var previousRecovery=$("#previousRecovery").text()==""?0:parseFloat($("#previousRecovery").text());
	
	    $("#totalDeductionAmtCumulative").val(cctotalamntB.toFixed(2));
	    $("#totalDeductionAmtPrevCerti").val(pctotalamntB.toFixed(2));
		$("#totalAmtCurntDeduc").val(totalamntB.toFixed(2));
		
		//calculating release advance
		var advanceCurrAmount=$("#advanceCurrAmount").val();
		var releaseAdvPrevAmt=$("#releaseAdvPrevAmt").val();
		if(advanceCurrAmount=="" || advanceCurrAmount=="null"){
			advanceCurrAmount=0;
		}
		if(releaseAdvPrevAmt=="" || releaseAdvPrevAmt=="null"){
			releaseAdvPrevAmt=0;
		}
		var releaseAdvTotalAmt=parseFloat(advanceCurrAmount)+parseFloat(releaseAdvPrevAmt);
		//this code removing release advance amount
		$("#releaseAdvTotalAmt").val(releaseAdvTotalAmt.toFixed(2));
	
		var ratotalCC=$("#raTotalCc").val();		
		var totalDeductionAmtCumulative=$("#totalDeductionAmtCumulative").val();
		if(ratotalCC=="" || ratotalCC=="null"){
			ratotalCC=0;
		}
		if(totalDeductionAmtCumulative=="" || totalDeductionAmtCumulative=="null"){
			totalDeductionAmtCumulative=0;
		}
		var CcFinalAmt=parseFloat(ratotalCC)-parseFloat(totalDeductionAmtCumulative)+parseFloat(releaseAdvTotalAmt);
		
		var raTotalPc=$("#raTotalPc").val();
		var totalDeductionAmtPrevCerti=$("#totalDeductionAmtPrevCerti").val();
		if(raTotalPc=="" || raTotalPc=="null"){
			raTotalPc=0;
		}
		if(totalDeductionAmtPrevCerti=="" || totalDeductionAmtPrevCerti=="null"){
			totalDeductionAmtPrevCerti=0;
		}
		var PcFinalAmt=parseFloat(raTotalPc)-parseFloat($("#totalDeductionAmtPrevCerti").val())+parseFloat(releaseAdvPrevAmt);
		$("#totalAmtCumulativeCertified").val((CcFinalAmt+parseFloat($("#cumulativeRecovery").val())).toFixed(2));
		$("#totalAmtPreviousCertified").val(PcFinalAmt.toFixed(2));
		
		//displaying final Amount		
		var totalAmountA=$("#totalCc").val();
		if(totalAmountA=="" || totalAmountA=="null"){
			totalAmountA=0;
		}
		//var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
		var finalTotalAmount=parseFloat(totalAmountA)-totalamntB+parseFloat(advanceCurrAmount);

		$("#finalAmt").val(finalTotalAmount.toFixed(2));debugger;
		if($("#finalAmt").val()<0){
			alert("you can not initiate more than Certified Amount.");
			$("#finalAmt").val("0.00");
			$("#totalAmtCumulativeCertified").val("0.00");
		}
	}

/* Calculating  advance  quantity in advance page  written by thirupathi   */
function calreleaseAmnt(){ debugger;
	var WoAmnt=parseFloat($("#totalAmtToPay").val().replace(/,/g,'')).toFixed(2);
	var OutstandingadvanceAmnt=$("#outstandingAdvTotalAmt").val();
	var entReleaseAmnt=$("#advanceCurrAmount").val();
	var prevPCAmnt=$("#releaseAdvPrevAmt").val();
	
	if(prevPCAmnt==""){
		prevPCAmnt=0;
	}
	if(entReleaseAmnt==""){
		entReleaseAmnt=0;
	}
	var campareAmnt=parseFloat(entReleaseAmnt)+parseFloat($("#TotalPaidAmt").val());
	if(campareAmnt>WoAmnt){
		alert("Initiated amount is greater than work order amount. ");
		entReleaseAmnt=0;
		$("#advanceCurrAmount").val('0');
		$("#advanceCurrAmount").focus();
	}
	var totalCCAmnt=parseFloat(prevPCAmnt)+parseFloat(entReleaseAmnt);
	$("#releaseAdvTotalAmt").val(totalCCAmnt.toFixed(2));
	$("#totalAmtCumulativeCertified").val(totalCCAmnt.toFixed(2));
	$("#totalAmtPreviousCertified").val(prevPCAmnt);
	$("#outstandingAdvTotalAmt").val((parseFloat($("#outstandingAdvTotalAmthidden").val()==''?0:$("#outstandingAdvTotalAmthidden").val())+parseFloat($("#advanceCurrAmount").val()==''?0:$("#advanceCurrAmount").val())).toFixed(2));  //+parseFloat($("#outstandingAdvTotalAmt").val()
	
	var amountInWords=	convertNumberToWords(entReleaseAmnt);	
	if(parseInt(entReleaseAmnt)!=0){
		$("#amountInWords").text(amountInWords);
	}
	if(parseInt(entReleaseAmnt)<0){
		$("#amountInWords").text("");
	}
	if(parseInt(entReleaseAmnt)==0){
		$("#amountInWords").text("Zero Rupees Only");
	}
	
	$("#finalAmt").val(entReleaseAmnt);
	
}
//validating work order amount with certified bill amount
function validateWoOrderAmnt(){
	var WoAmnt=parseFloat($("#totalAmtToPay").val().replace(/,/g,'')).toFixed(2);	
	var raDeductionAmt=$("#raDeductionAmt").val();
	var finalAmt=$("#finalAmt").val();
	var totalPaidAmnt=$("#TotalPaidAmt").val();
	if(raDeductionAmt==""){
		raDeductionAmt=0;
	}
	if(finalAmt==""){
		finalAmt=0;
	}
	if(totalPaidAmnt==""){
		totalPaidAmnt=0;
	}
	
	var validateamnt=parseFloat(totalPaidAmnt)+parseFloat(finalAmt)-parseFloat(raDeductionAmt);
	if(validateamnt>WoAmnt){
		alert("Initiated amount is greater than work order amount.");
		return false;		
	}
}

//calculating abstract values 
function paymentAreaBtn(){
	$("#changedsecDepositCurrentCerti").val(parseFloat($("#changedsecDepositCurrentCerti").val()).toFixed(2));
	$("#actualRaAmountToPay").val(parseFloat($("#actualRaAmountToPay").val()).toFixed(2));
	var len=$("input[name='chk1']:checked").length;
	if(len==0){
		alert("Please select at least one work order payment area.");
		$("#tbl-2").show();
		return false;
	}
	var pagename=$("#pagename").val();
	if(pagename=="initiatera"){
		$("#raDeductionAmt").removeAttr("readonly");
		$("#pettyExpensesCurrentCerti").removeAttr("readonly");
		$("#other").removeAttr("readonly");
	}
	
	var flag=true;//ACP
	$(".chkcls:checked").each(function(){
		debugger; 
		var chkid=$(this).attr('id');
		   var chkspiltid=chkid.split("chk1")[1];
		   var totalQty=parseFloat($("#TotalQty"+chkspiltid).text());
		   var cumulativeQuantity=parseFloat($("#CBQty"+chkspiltid).text());
		   if(cumulativeQuantity>totalQty){
			   alert("It's seems area is allocating more than the total area, Please change the area.");
			   $("#CCQty"+chkspiltid).focus();
				//$("#modal-certificatepaymentra-click").modal('show');
				$("#modal-approverabill").modal('show');
				   $("#CCQty"+chkspiltid).focus();
			   flag=false;
			   return false;
		   }
		});
	
	if(flag==false){
		return false;
	}
	var paymentAmnt=0;
	$(".chkcls:checked").each(function(){
		   var chkid=$(this).attr('id');
		   var chkspiltid=chkid.split("chk1")[1];
		   var CCAmnt=$("#CCAmount"+chkspiltid).text();
		   paymentAmnt+=parseFloat(CCAmnt);
		});
	$("#hiddenpaymentareaAmt").val(paymentAmnt);
	var hiddenpayareaAmnt=$("#hiddenpaymentareaAmt").val();
	var billtype=$("#billType").val();
	if(billtype=="RA"){
		$("#raAmountToPay").val(paymentAmnt.toFixed(2));
		$("#currentAmount").text(paymentAmnt.toFixed(2));
		CurrentCertifiedamnt();
		securityperchange();
		$("#modal-certificatepaymentra-click").modal('hide');
		$("#modal-approverabill").modal('hide');
	}
	if (billtype == "ADV") {
		$("#advanceCurrAmount").val(paymentAmnt.toFixed(2));
		calreleaseAmnt();
		$("#modal-certificatepayment-adv").modal('hide');
		$("#modal-approveCOPF").modal('hide');
	}
}
//select all option method
$("#checkAll").change(function () {
    $('input:checkbox').not(this).prop('checked', this.checked);
	$.each($("input[name='chk1']:checked"), function(){   
		var thisVal=$(this).val();
		var workAreaId=$("#workAreaId"+thisVal).val();
		var len = $("input[name='chk1']:checked").length;
		var area=$("#CCQty"+thisVal).val()==""?0:$("#CCQty"+thisVal).val();    
		if(area==0){
			$(this).prop("checked", false);	
		}
		var actualQty=$("#TotalQty"+thisVal).text();
		var initiatedQunatity=$("#CBQty"+thisVal).text();
         if(area==0&&(initiatedQunatity!=actualQty)){
              	alert("Please enter quantity.");
              	$("#CCQty"+thisVal).focus();
               	$(this).prop("checked", false);	
               	$('input:checkbox').not(this).prop('checked',false);
               	return false;
         }
    });
    
});

//checking number or not 
function isNumber(el, evt) {
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

function findDot(event){
	 var textBox = event.target;
     var start = textBox.selectionStart;
     var end = textBox.selectionEnd;
     if(textBox.value.charAt(0)=='.'){
    	 textBox.value='';
    	 return false;
     }
	
}


function changetheValue(){
	var CrntCertifiedid=$("#raAmountToPay").val();
	var totalAmtToPay=parseFloat($("#totalAmtToPay").val().replace(/,/g,'')).toFixed(2);
	var raPcAmt=$("#raPc").val();
	
	if(CrntCertifiedid==''){
		CrntCertifiedid=0;
	}
	var utstandingamnt=$("#outstandingAdvTotalAmt").val();
	var raccamnt=$("#raCc").val();
	
	var totlamtA=parseFloat(CrntCertifiedid)+parseFloat(raPcAmt);
		$("#raCc").val(totlamtA.toFixed(2));
		$("#totalCc").val(CrntCertifiedid);
		$("#raTotalPc").val(raPcAmt);
		$("#raTotalCc").val(totlamtA.toFixed(2));
		
		
		//displaying final Amount
		
		var totalAmountA=parseFloat($("#totalCc").val());
		if(totalAmountA==""){
			totalAmountA=0;
		}
		var totalamntB=parseFloat($("#totalAmtCurntDeduc").val());
		var finalTotalAmount=totalAmountA-totalamntB;
		$("#finalAmt").val(finalTotalAmount);
		debugger;
		if(parseInt($("#finalAmt").val())!=0){
			var amountInWords=	convertNumberToWords(finalTotalAmount);
			$("#amountInWords").text(amountInWords);
		}
		if(parseInt($("#finalAmt").val())<0 ){
			$("#amountInWords").text("");
		}
		if(parseInt($("#finalAmt").val())==0){
			$("#amountInWords").text("Zero Rupees Only");
		}
		
		
		var finalamnt=$("#totalAmtPreviousCertified").val();
		var raccdedamnt=$("#raCcDeductionAmthidden").val();debugger;
		//$("#raCcDeductionAmt").val(parseFloat(raccdedamnt)+parseFloat(finalamnt)+parseFloat($("#raCcDeductionAmt").val()==""?0:$("#raCcDeductionAmt").val()));
		$("#raCcDeductionAmt").val((parseFloat($("#raCcDeductionAmt").val()==""?0:$("#raCcDeductionAmt").val())).toFixed(2));
}

