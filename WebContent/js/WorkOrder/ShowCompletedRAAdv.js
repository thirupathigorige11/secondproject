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
//loading work order number based on site id, contractor id and work description id 
function loadWorkOrderNo(workDescId) {		
     var contractorId = document.getElementById("ContractorId").value;
     workDescId = workDescId.split("$")[0];
	 var siteId = document.getElementById("siteId").value;
     var url = "getWorkOrderNo.spring?workDescId="+workDescId+"&siteId="+siteId+"&contractorId="+contractorId;
	 url = "getWorkOrderNo.spring?workDescId=0&siteId="+siteId+"&contractorId="+contractorId;
		
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
		    	var subProdToSet = "comboboxworkOrderNo";			    	
		    	var selectBox = document.getElementById(subProdToSet);
			    //Removing previous options from select box - Start
			    if(document.getElementById(subProdToSet) != null && document.getElementById(subProdToSet).options.length > 0) {
			    	document.getElementById(subProdToSet).options.length = 0;
			    }
			    //Removing previous options from select box - End			    
			    initOpt = document.createElement("option");
			    initOpt.text = "--Select--";
			    initOpt.value = "";
			    
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
//this method for loading contractor names autocomplete
function populateData() {	
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
//code for selected text
function AutoCompleteSelectHandler(event, ui){               
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
				var contractorData=data[0].split("@@");
				var contractorId=contractorData[0];
			
				$("#ContractorId").val(contractorId);
				$("#MobileNo").val(contractorData[3]);
				$("#PanCardNo").val(contractorData[4]);
				$("#AccountNo").val(contractorData[5]);
				$("#ifscCode").val(contractorData[6]+" & "+contractorData[8]);
				
			  }
		  },
		  error:  function(data, status, er){
			  alert(data+"_"+status+"_"+er);
		 }
	 });
}

function setContractorData(cName) {
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
		
		var vendorId = resp.split("@@")[0];
		$("#ContractorName").val(resp.split("@@")[0]);
		$("#ContractorId").val(resp.split("@@")[1]);
		$("#PanCardNo").val(resp.split("@@")[2]);
		$("#AccountNo").val(resp.split("@@")[4]);
		$("#ifscCode").val(resp.split("@@")[6] + ' & '+ resp.split("@@")[5]);
		$("#MobileNo").val(resp.split("@@")[3]);
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

function closeView(){
	goBack() ;
}

function goBack() {
    window.history.back();
}
//loading recovery 
function loadRARecovery(){
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var approvePage=$("#approvePage").val();
	var tempBillNo=$("#tempBillNo").val();
	var site_id=$("#site_id").val();
	var billNo=$("#billNo").val();
	var str="";
	var str1="";
	var htmlData="";
	$.ajax({
		url : "loadPermanentRecoveryAreaDetails.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			approvePage:approvePage,
			tempBillNo:tempBillNo,
			site_id:site_id,
			billNo:billNo
			},
		success : function(data) {
			var rowsOfRecovery=0;
			var sumOfRecovery=0;
			var sumOfCumulative=0;
			var sumOfPrev=0;
			var childProductName="";
			var temprecoveryAmount="";
			$("#RecoveryStatement").html("");
			$("#ViewRecovery").html("");
			$.each(data,function(key,value){
				var amount_per_unit_before_taxes=parseFloat(value.AMOUNT_PER_UNIT_BEFORE_TAXES);
				var issuedQty=parseFloat(value.ISSUED_QTY);
				
				var recoveryAmount=value.RECOVERY_AMOUNT==null?0:parseFloat(value.RECOVERY_AMOUNT);
				temprecoveryAmount=value.RECOVERY_AMOUNT;
				var recoveryCurrentAmount=parseFloat(value.TOTAL_AMOUNT)-recoveryAmount;
				var totalRecoveryAmount=value.TOTAL_AMOUNT==null?"":value.TOTAL_AMOUNT;
				console.log(recoveryAmount);
				let tempCumulative=(recoveryAmount+recoveryCurrentAmount);
				
				var currentRecoveryAmount=value.CURRENTRECOVERY==null?0:parseFloat(value.CURRENTRECOVERY);
				 tempCumulative=(recoveryAmount+currentRecoveryAmount);
				
				var child_product_id=value.CHILD_PRODUCT_ID;
				if(childProductName!=value.CHILD_PROD_NAME){
					childProductName=value.CHILD_PROD_NAME;
					rowsOfRecovery++;
					str="<tr> <td > "+rowsOfRecovery+"<input type='hidden' id='childId"+rowsOfRecovery+"' value='"+child_product_id+"'> </td>" +
					"<td><strong>"+value.CHILD_PROD_NAME+"</strong></td>" +
					"<td id='"+child_product_id+"issuedQty'   class='totalQtyConsumed'><span>"+issuedQty+"</span></td>" +
					"<td id='"+child_product_id+"totalRecoveryAmount'  class='totalAmountOfRecovery'><span>"+totalRecoveryAmount+"</span></td>" +
					"<td  id='"+child_product_id+"mesurment_name'  >"+value.MESURMENT_NAME+" <input type='hidden'  id='"+child_product_id+"mesurment_id' value='"+value.UNITS_OF_MEASUREMENT+"'></td>" +
					"<td id='"+child_product_id+"cumulative'><span>"+tempCumulative+"</span></td>" +
					"<td id='"+child_product_id+"previous'><span>"+recoveryAmount+"</span></td>" +
					"<td> <input type='text' class='form-control' id='"+child_product_id+"currentAmount' value='"+(currentRecoveryAmount)+"' onkeyup='calculateRecoveryAmount("+rowsOfRecovery+");' readonly='true'>  </td>" +
					"</tr>";
					$("#RecoveryStatement").append(str);
			/*		$("#ViewRecovery").append(str1);*/
				}else{
					debugger;
					//for recovery statement
					var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
					cumulative=parseFloat(cumulative);
					var previous=$("#"+child_product_id+"previous").text();//getting previous value using child id 
					previous=parseFloat(previous);
					var  tempIssuedQty =$("#"+child_product_id+"issuedQty").text();
					tempIssuedQty=parseFloat(tempIssuedQty);
					var before_taxes =$("#"+child_product_id+"totalRecoveryAmount").text();
					before_taxes=parseFloat(before_taxes);
					//	var currentAmount=parseFloat($("#"+child_product_id+"currentAmount").val());
						if(value.RECOVERY_AMOUNT==null){/*||temprecoveryAmount!=value.RECOVERY_AMOUNT*/
						}
						$("#"+child_product_id+"issuedQty").text(tempIssuedQty+issuedQty);
						$("#"+child_product_id+"totalRecoveryAmount").text(before_taxes+totalRecoveryAmount);
			}

			});
			for (var i1 = 1; i1 <= rowsOfRecovery; i1++) {
				var child_product_id=$("#childId"+i1).val();
				var recovery_amount=parseFloat($("#"+child_product_id+"currentAmount").val()==""?0:$("#"+child_product_id+"currentAmount").val());
				var cumulative=$("#"+child_product_id+"cumulative").text();//getting previous cumulative value using child id 
				
				cumulative=parseFloat(cumulative);
				var previous=$("#"+child_product_id+"previous").text();//getting previous value using child id 
				previous=parseFloat(previous);
				sumOfCumulative+=+cumulative;
				sumOfPrev+=+previous;
				sumOfRecovery+=(recovery_amount);
			}			

			sumOfCumulative=sumOfCumulative.toFixed(2)
			sumOfPrev=sumOfPrev.toFixed(2);
			sumOfRecovery=sumOfRecovery.toFixed(2);
			$("#sumOfCumulativeRecovery").text(sumOfCumulative);
			$("#sumOfPreviousRecovery").text(sumOfPrev);
			$("#sumOfCumulativeRecovery").val(sumOfCumulative);
			$("#sumOfPreviousRecovery").val(sumOfPrev);

			
		//$("#currentRecoveryAmount").html(sumOfRecovery);
		htmlData+="<input type='hidden' name='rowsToIterate' id='rowsToIterate' value='"+rowsOfRecovery+"'>";
		var temp=" <tr>" +
		"<td></td>" +
		"<td class='text-right'><h4><strong>Total Amount</strong></h4></td>" +
		"<td><strong  id='totalQtyConsumed'></strong></td>" +
		"<td> <strong  id='totalAmountOfRecovery'></strong></td><td></td>" +
		"<td c><strong><span>"+inrFormat(sumOfCumulative)+"</span></strong></td>" +
		"<td ><strong><span>"+inrFormat(sumOfPrev)+"</span></strong></td>" +
		"<td ><strong><span>"+inrFormat(sumOfRecovery)+"</span></strong></td></tr>";	
		if(billType=="ADV"){
			$("#recoverycurrentAmount").val(0);
		}
		$("#RecoveryStatement").append(temp);
		$("#ViewRecovery").append(temp);
		
		$("#ViewRecovery").append(htmlData);	
		
		
		//Sum of Total Quantity Consumed & total Recovery Amount
		var totalQtyConsumed=0;
		var totalAmountOfRecovery=0
		$(".totalQtyConsumed").each(function(){
			debugger;
			var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
			totalQtyConsumed+=currentvalue
		});
		$(".totalAmountOfRecovery").each(function(){
			debugger;
			var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
			$(this).text(inrFormat(currentvalue.toFixed(2)));
			totalAmountOfRecovery+=currentvalue;
		});
		$("#totalQtyConsumed").text(totalQtyConsumed.toFixed(2));
		$("#totalAmountOfRecovery").text(totalAmountOfRecovery.toFixed(2));
		
		
		
			console.log("recovery data "+data);
		}			
	});
	
}//recovery method end

function isNum(value) {
	  var numRegex=/^[0-9.]+$/;
	  return numRegex.test(value)
}

//code for loading completed bill data
function loadPreviousCompletedBillData(){	
	var ContractorId=$("#ContractorId").val();
	var ContractorName=$("#ContractorName").val();
	var workOrderNo=$("#workOrderNo").val();
	var billDate=$("#billDate").val();
	
	var approvePage=$("#approvePage").val();
	var tempBillNo=$("#tempBillNo").val();
	try{tempBillNo=tempBillNo.trim()}catch(e){}
	var billNo=$("#billNo").val();
	var site_id=$("#site_id").val();
	//checking contractor name
	if(ContractorId.length==0){
		alert("Please enter contractor name.");
		return false;
	}

	var d = new Date();
	var n = d.toLocaleDateString();
	//checking work order number
	if(workOrderNo.length==0){
		alert("Please select work order number.");
		return false;
	}
	debugger;
	
	var isThisBillLedger=$("#isThisBillLedger").val();
	//if this is the ledger page no need to load data again because it's already loaded 
	//so the data stored in session storage 
	if(isThisBillLedger=="true"){
		if (typeof(Storage) !== "undefined") {
			debugger;
			//this session storage used for showing type of work
		  var   billDetails=sessionStorage.getItem("${UserId}"+tempBillNo+"CompletedBillDetails");
		  $("#appendBillDetails").html(billDetails);
		    $("#paymentLedgerTable").show();
		    $("#paymentLedgerTable").show();	
		    $("#contractorRABill").show();
		    $("#sumadhuraLogoAndName").show();
		    $("#billLedgerOfBill").show();
		    $("#printshowRa").show();
		  
			$('span').each(function(){
				// Do your magic here
			    if (isNum($(this).text())){ // regular expression for numbers only.
			    	var tempVal=parseFloat($(this).text());
			    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"&&this.id!="notificationBell"){
			    		$(this).text(inrFormat(tempVal.toFixed(2)));
			    	}
		          }
			});
			$(".overlay_ims").hide();	
			$(".loader-ims").hide();
		 } else {
		    alert("Sorry, your browser does not support Web Storage...");
		 };
	}
	
	
	if(isThisBillLedger!="true")
	$.ajax({
		url : "loadAdvBillCertificatateDetails.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			billDate:billDate,
			approvePage:approvePage,
			tempBillNo:tempBillNo,
			site_id:site_id
		},
		success : function(data) {
		
			$("#appendBillDetails").html("");
			
			var paybleAmt=0;
			var htmlData="";
			var advHtmlData="";
			var raHtmlData="";
			var printAdvHtmlData="";
			var printRaHtmlData="";
			var initiatedAdv=0.00;
			var raPaidAmt=0.00;
			var GrandTotal=0.00;
			var raPrevCertifiedAmount=0.00;
			var advPrevCertifiedAmount=0.00;
			var deduction_amount=0.00;
			var deduction_amount1=0.00;
			var advPrevAmt=parseFloat($("#finalAmt").val());
			var tempBillNo=	parseInt($("#tempBillNo").val());
			var holdPrevAmount=0.00;
			var	ADV=0.00;
			var	SEC=0.00;
			var	PETTY=0.00;
			var	OTHER=0.00;
			var RECOVERY=0.00;
			var secDeposit=0.00;
			var appendPrevDeductVal="";
			$.each(data,function(key,value){
				var billStatus=value.STATUS;
			
				initiatedAdv++;
				var emp=value.PENDING_EMP_ID;
				var payment_type=value.PAYMENT_TYPE;
				if(payment_type=="ADV"){
					if(billStatus!="R"){
						paybleAmt+=+value.PAYBLE_AMOUNT;				
						advPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
						GrandTotal+=+value.CERTIFIED_AMOUNT;
					}
					var status=value.STATUS=="R"?" Advance Rejected ":value.STATUS;
					var pendingid=" Advance Completed ";
					//advHtmlData+=" <tr> <td style='border:1px solid #000;padding:5px;'>"+payment_type+"</td><td style='border:1px solid #000;padding:5px;'>"+value.CERTIFIED_AMOUNT+"</td><td style='border:1px solid #000;padding:5px;'>0</td><td style='border:1px solid #000;padding:5px;'>"+value.PAYBLE_AMOUNT+"</td>     <td style='border:1px solid #000;padding:5px;'>"+value.BILL_ID+"</td>     <td style='border:1px solid #000;padding:5px;'>0 </td> <td style='border:1px solid #000;padding:5px;'>0 </td><td style='border:1px solid #000;padding:5px;'>0 </td><td style='border:1px solid #000;padding:5px;'>0 </td><td style='border:1px solid #000;padding:5px;'>0 </td><td style='border:1px solid #000;padding:5px;'>"+value.ENTRY_DATE+"</td>     <td style='border:1px solid #000;padding:5px;'>"+pendingid+"</td>    </tr>";
					/*advHtmlData+=" <tr> <td style='border:1px solid #000;padding:5px;'>"+payment_type+"</td><td style='border:1px solid #000;padding:5px;'><span>"+value.CERTIFIED_AMOUNT+"</span></td><td style='border:1px solid #000;padding:5px;'>0</td><td style='border:1px solid #000;padding:5px;'><span>"+value.PAYBLE_AMOUNT+"</span></td>    " +
					"  <td style='padding:10px;border:1px solid #000;'><a  target='_blank' style='color:#0000ff;'  class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td></td>" +
					"  <td style='border:1px solid #000;padding:5px;'>0.00</td> <td style='border:1px solid #000;padding:5px;'>0.00</td><td style='border:1px solid #000;padding:5px;'>0 </td><td style='border:1px solid #000;padding:5px;'>0.00 </td><td style='border:1px solid #000;padding:5px;'>0.00 </td><td style='border:1px solid #000;padding:5px;'>"+value.ENTRY_DATE+"</td>     <td style='border:1px solid #000;padding:5px;'>"+pendingid+"</td>    </tr>";
*/	
					raHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><a  target='_blank' style='color:#0000ff;' class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.PAYBLE_AMOUNT+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span></td></tr>";
					
					
					printRaHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>"+value.BILL_ID+"</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>0.00</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.PAYBLE_AMOUNT+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span></td></tr>";

					
					holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);

				}else if(payment_type=="RA"){
					if(billStatus!="R"){
						raPaidAmt+=+value.PAYBLE_AMOUNT;
				
						if(value.TEMP_BILL_ID!=tempBillNo){
							raPrevCertifiedAmount+=+value.CERTIFIED_AMOUNT;
						}
						GrandTotal+=+value.CERTIFIED_AMOUNT;
					}
					var status=value.STATUS=="R"?" RA Rejected ":value.STATUS;
					var pendingid=" RA Completed ";
					var printAdvAmount=0;
					var printSecDeposit=0;
					var printPettyExpences=0;
					var printOther=0;
					var printRecoveryAmt=0;
					if(value.DEDUCTION_AMOUNT!=null){						
						var typeOfDeduction=new Array();
						var typeOfDeductionVal=new Array();
						//type of deduction in coming in the array where it's spliting by @@
						typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
						//type of DEDUCTION_AMOUNT in coming in the array where it's spliting by @@
						typeOfDeductionVal=	value.DEDUCTION_AMOUNT.split("@@")
				
						for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
							if(typeOfDeduction[i2]=="ADV"){
								deduction_amount+=+typeOfDeductionVal[i2];
								printAdvAmount=typeOfDeductionVal[i2];
							}else if (typeOfDeduction[i2]=="RECOVERY"){
								RECOVERY+=+typeOfDeductionVal[i2];
								printRecoveryAmt=typeOfDeductionVal[i2];
							}else if(typeOfDeduction[i2]=="SEC"){
								secDeposit+=+typeOfDeductionVal[i2];
								printSecDeposit=typeOfDeductionVal[i2];
							}else if (typeOfDeduction[i2]=="PETTY"){
								PETTY+=+typeOfDeductionVal[i2]
								printPettyExpences=typeOfDeductionVal[i2];
							}else if (typeOfDeduction[i2]=="OTHER"){
								OTHER+=+typeOfDeductionVal[i2]
								printOther=typeOfDeductionVal[i2];
							}
						}
					}
					var DA=value.DA-value.DEDUCTION_AMOUNT;
					/*raHtmlData+=" <tr><td style='padding:10px;border:1px solid #000;'>"+payment_type+"</td><td style='padding:10px;border:1px solid #000;'>0.00</td><td style='padding:10px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+value.PAYBLE_AMOUNT+"</span></td>" +
					 " <td style='padding:10px;border:1px solid #000;'><a   target='_blank' style='color:#0000ff;' class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td></td>" +
				 	 " <td style='padding:10px;border:1px solid #000;'><span>"+printAdvAmount+"</span> </td><td style='padding:10px;border:1px solid #000;'><span>"+printRecoveryAmt+"</span> </td><td style='padding:10px;border:1px solid #000;'><span>"+printSecDeposit+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+printPettyExpences+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+printOther+"</span></td><td style='padding:10px;border:1px solid #000;'>"+value.ENTRY_DATE+"</td><td style='padding:10px;border:1px solid #000;'>"+pendingid+"</td></tr>";
*/					
					debugger;
					raHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><a   target='_blank' style='color:#0000ff;'  class='anchorblue' href='showWOCompltedBillsDetails.spring?BillNo="+value.BILL_ID+"&WorkOrderNo="+workOrderNo.split("$")[0]+"&billType="+value.PAYMENT_TYPE+"&site_id="+value.SITE_ID+"&isBillUpdatePage=false&status=true'>"+value.BILL_ID+"</a></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printAdvAmount+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printRecoveryAmt+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printPettyExpences+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printOther+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printSecDeposit+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span class='amountPaid'>"+value.PAYBLE_AMOUNT+"</span></td>";
					raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span class='cumulativeTotal'>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span</td></tr>";
					
					printRaHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'>"+value.PAYMENT_REQ_DATE+"</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>"+value.BILL_ID+"</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+value.CERTIFIED_AMOUNT+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printAdvAmount+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printRecoveryAmt+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printPettyExpences+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printOther+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+printSecDeposit+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span class='amountPaid'>"+value.PAYBLE_AMOUNT+"</span></td>";
					printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span class='cumulativeTotal'>"+(holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT))+"</span</td></tr>";


					
					holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);
				
				
					}	
				});
				var raAmountToPay=$("#raAmountToPay").val()==""?0:parseFloat($("#raAmountToPay").val());
				var nextLevelApprovelEmpID=$("#nextLevelApprovelEmpID").val();
			 
				raHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td></tr>";
				
				raHtmlData+="<tr  style='border: 1px solid #000;background-color:#ccc;padding:5px;'><td style='padding:5px;border:1px solid #000;'>Total Amount</td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(raPrevCertifiedAmount+raAmountToPay)+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(advPrevCertifiedAmount)+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(deduction_amount)+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span><span>"+(RECOVERY)+"</span></span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span><span>"+(PETTY)+"</span></span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(OTHER)+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(secDeposit)+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td>";
				raHtmlData+="<td style='padding:5px;border:1px solid #000;'></td></tr>";
				
				printRaHtmlData+="<tr><td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td></tr>";
				
				printRaHtmlData+="<tr  style='border: 1px solid #000;background-color:#ccc;padding:5px;'><td style='padding:5px;border:1px solid #000;'>Total Amount</td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(raPrevCertifiedAmount+raAmountToPay)+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(advPrevCertifiedAmount)+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(deduction_amount)+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span><span>"+(RECOVERY)+"</span></span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span><span>"+(PETTY)+"</span></span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(OTHER)+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'>0.00</td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+(secDeposit)+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'><span>"+holdPrevAmount+"</span></td>";
				printRaHtmlData+="<td style='padding:5px;border:1px solid #000;'></td></tr>";
				
				if (typeof(Storage) !== "undefined") {
					debugger;
					//this session storage used for showing type of work
				    sessionStorage.setItem("${UserId}"+tempBillNo+"CompletedBillDetails",(printAdvHtmlData+printRaHtmlData));
				 } else {
				    alert("Sorry, your browser does not support Web Storage...");
				 };
				
				//$("#appendBillDetailsFooter").html("<tr><td style='padding:10px;border:1px solid #000;'>Total Amount</td><td style='padding:10px;border:1px solid #000;'><span>"+(advPrevCertifiedAmount.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+((raPrevCertifiedAmount+raAmountToPay))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(raPaidAmt+paybleAmt).toFixed(2)+"</span></td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'><span>"+(deduction_amount.toFixed(2))+"</span> </td><td style='padding:10px;border:1px solid #000;'><span>"+(RECOVERY.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(secDeposit.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'><span>"+(PETTY.toFixed(2))+"</span></td><td style='padding:10px;border:1px solid #000;'>"+(OTHER.toFixed(2))+"</td><td style='padding:10px;border:1px solid #000;'></td><td style='padding:10px;border:1px solid #000;'></td></tr>");
				
				$("#appendBillDetailsTotal").html("<div  style='font-size: 20px;margin: 10px 20px 0px 0px;'><div style='width:450px;'><div style='float:left;width:200px;'> Paid Amount  </div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'><span>"+((raPaidAmt+paybleAmt).toFixed(2))+"</span></div></div><div style='width:450px;'><div style='float:left;width:200px;'>Total Deduction</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'><span>"+((secDeposit+PETTY+OTHER+RECOVERY).toFixed(2))+"</span></div></div><div style='width:450px;'><div style='float:left;width:200px;'> Grand Total</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:200px;text-align:right;'><span>"+((GrandTotal-parseFloat(deduction_amount)).toFixed(2))+"</span></div></div></div>"); //deduction_amount+
			 
				var raPage=$("#raPage").val();
				var billType=$("#billType").val();
				$("#releaseAdvTotalAmt").val(paybleAmt.toFixed(2));
				$("#actualreleaseAdvTotalAmt").val(paybleAmt.toFixed(2));
				var finalAmt=parseFloat($("#finalAmt").val()==""?0:$("#finalAmt").val());
				var raDeductionAmt=$("#raDeductionAmt").val()==""?0:parseFloat($("#raDeductionAmt").val());
				//for create adv
				$("#outstandingAdvTotalAmt").val((paybleAmt-deduction_amount).toFixed(2));
				
				if(billType=="ADV"){
					$("#TotalPaidAmt1").val((raPaidAmt).toFixed(2)); //-finalAmt
					$("#TotalPaidAmt").val((raPaidAmt).toFixed(2)); //-finalAmt
				}else{
					$("#TotalPaidAmt1").val((raPaidAmt-finalAmt).toFixed(2)); //-finalAmt
					$("#TotalPaidAmt").val((raPaidAmt-finalAmt).toFixed(2)); //-finalAmt
				}
				$("#totalAmtCumulativeCertified").val(paybleAmt.toFixed(2));
				
				if(billType=="ADV"&&nextLevelApprovelEmpID=="END"){
					$("#outstandingAdvPrevAmt").val((paybleAmt-deduction_amount-finalAmt).toFixed(2));	
					$("#totalAmtPreviousCertified").val(paybleAmt-advPrevAmt);
				}else{
					$("#outstandingAdvPrevAmt").val((paybleAmt-deduction_amount).toFixed(2));
					$("#totalAmtPreviousCertified").val(paybleAmt.toFixed(2));
				}
				if(billType=="RA"&&nextLevelApprovelEmpID=="END"){
					$("#outstandingAdvPrevAmt").val((paybleAmt-deduction_amount+raDeductionAmt).toFixed(2));
				}
					
				
			
				//paybleAmt	 -Advance paid amount
				//deduction_amount	- advance duduction amount in RA Bill
				//raPaidAmt  -RA paid amount, with cutting all the duduction amount
				//raPrevCertifiedAmount	-Total RA initiated amount with out cutting's
				//ADV		=previous deducted amount in RA-advance deduction amount
			    //SEC		=previous deducted amount in RA-securtiy amount
				//PETTY		=previous deducted amount in RA-perry amount
				//OTHER		=previous deducted amount in RA-other amount
				//RECOVERY	=previous deducted amount in RA-newwly deducted amount 
			
			   if(raPage!=undefined&&raPage!=null){
					//for RA Page 
					//		if(raPage!="false"&&billType!="ADV"){
					if(raPrevCertifiedAmount!=0){
						if(nextLevelApprovelEmpID=="END"){
							$("#raPc").val(raPrevCertifiedAmount.toFixed(2));
							$("#raTotalPc").val(raPrevCertifiedAmount.toFixed(2));
							$("#raCc").val((parseFloat($("#raPc").val())+raAmountToPay).toFixed(2));
						}
						$("#raTotalCc").val((raPrevCertifiedAmount+raAmountToPay).toFixed(2));
					}else{
						$("#raPc").val(raPrevCertifiedAmount.toFixed(2));
						$("#raCc").val((parseFloat($("#raPc").val())+raAmountToPay).toFixed(2));
					
						if(raPrevCertifiedAmount!=0)
						$("#raTotalCc").val(raPrevCertifiedAmount.toFixed(2));
						$("#raTotalPc").val(raPrevCertifiedAmount.toFixed(2));
					}
				
					//for advance payment page
					var advPage=$("#advPage").val();
						$("#releaseAdvTotalAmt").val("");
						$("#totalAmtPreviousCertified").val("");
						$("#releaseAdvPrevAmt").val("");
						$("#totalAmtCumulativeCertified").val("");
						//remove if want you to show adv paid amt 

						$("#raTotalCc").val($("#raCc").val());
						//if previous amount is not 0 then only print the sec,adv,petty,other amount
						if(raPrevCertifiedAmount!=0){
							var secDepositCurrentCerti=$("#secDepositCurrentCerti").val()==""?0:parseFloat($("#secDepositCurrentCerti").val());
							var pettyExpensesCurrentCerti=$("#pettyExpensesCurrentCerti").val()==""?0:parseFloat($("#pettyExpensesCurrentCerti").val());
							var raDeductionAmt=$("#raDeductionAmt").val()==""?0:parseFloat($("#raDeductionAmt").val());
							var other=$("#other").val()==""?0:parseFloat($("#other").val());
							var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
							try {	
								$("#raeductionPrevCumulative").val(deduction_amount-raDeductionAmt);
								//$("#raeductionPrevCumulative").val((deduction_amount).toFixed(2));
							    //$("#raeductionPrevCumulative").val(((raPaidAmt-finalAmt)+(deduction_amount-raDeductionAmt)).toFixed(2));
							} catch (e) {
								console.log(e);
							}					
							$("#secDepositPrevCerti").val((secDeposit-secDepositCurrentCerti).toFixed(2));
							$("#pettyExpensesPrevCerti").val((PETTY-pettyExpensesCurrentCerti).toFixed(2));
							$("#otherAmtPrevCerti").val((OTHER-other).toFixed(2));
							$("#sumOfPreviousRecovery").val((RECOVERY-recoverycurrentAmount).toFixed(2));
						}else{
							$("#raeductionPrevCumulative").val("0.00");
							$("#secDepositPrevCerti").val("0.00");
							$("#pettyExpensesPrevCerti").val("0.00");
							$("#otherAmtPrevCerti").val("0.00");
							$("#sumOfPreviousRecovery").val("0.00");
						}
						
						var raeductionPrevCumulative=$("#raeductionPrevCumulative").val()==""?0:parseFloat($("#raeductionPrevCumulative").val());
						var secDepositPrevCerti=$("#secDepositPrevCerti").val()==""?0:parseFloat($("#secDepositPrevCerti").val());
						var pettyExpensesPrevCerti=$("#pettyExpensesPrevCerti").val()==""?0:parseFloat($("#pettyExpensesPrevCerti").val());
						var otherAmtPrevCerti=$("#otherAmtPrevCerti").val()==""?0:parseFloat($("#otherAmtPrevCerti").val());
						var paidAmntPrevCerti=$("#TotalPaidAmt").val()==""?0:parseFloat($("#TotalPaidAmt").val());
						var recoveryPrevCerti=$("#sumOfPreviousRecovery").val()==""?0:parseFloat($("#sumOfPreviousRecovery").val());
						
						var secDepositCurrentCerti=$("#secDepositCurrentCerti").val()==""?0:parseFloat($("#secDepositCurrentCerti").val());
						var pettyExpensesCurrentCerti=$("#pettyExpensesCurrentCerti").val()==""?0:parseFloat($("#pettyExpensesCurrentCerti").val());
						var raDeductionAmt=$("#raDeductionAmt").val()==""?0:parseFloat($("#raDeductionAmt").val());
						var other=$("#other").val()==""?0:parseFloat($("#other").val());
						var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
						
						var raTotalPc=$("#raTotalPc").val()==""?0:parseFloat($("#raTotalPc").val());
						$("#raeductionCumulative").val((raeductionPrevCumulative+raDeductionAmt).toFixed(2));
						$("#secDepositCumulative").val((secDepositPrevCerti+secDepositCurrentCerti).toFixed(2));
						$("#pettyExpensesCumulative").val((pettyExpensesPrevCerti+pettyExpensesCurrentCerti).toFixed(2));
						$("#otherAmtCumulative").val((otherAmtPrevCerti+other).toFixed(2));
						$("#sumOfCumulativeRecovery").val((recoveryPrevCerti).toFixed(2));
						
						var raeductionCumulative=$("#raeductionCumulative").val()==""?0:parseFloat($("#raeductionCumulative").val());
						var secDepositCumulative=$("#secDepositCumulative").val()==""?0:parseFloat($("#secDepositCumulative").val());
						var pettyExpensesCumulative=$("#pettyExpensesCumulative").val()==""?0:parseFloat($("#pettyExpensesCumulative").val());
						var otherAmtCumulative=$("#otherAmtCumulative").val()==""?0:parseFloat($("#otherAmtCumulative").val());
						 var recoverycurrentAmount=$("#recoverycurrentAmount").val()==""?0:parseFloat($("#recoverycurrentAmount").val());
						 var ccPaidAmnt=$("#TotalPaidAmt1").val()==""?0:parseFloat($("#TotalPaidAmt1").val());
						 var sumOfCumulativeRecovery=$("#sumOfCumulativeRecovery").val()==""?0:parseFloat($("#sumOfCumulativeRecovery").val());
						 
						var totalCumulative=sumOfCumulativeRecovery+ccPaidAmnt+raeductionCumulative+secDepositCumulative+pettyExpensesCumulative+otherAmtCumulative+recoverycurrentAmount;
						var raTotaoPrevAmt=	paidAmntPrevCerti+recoveryPrevCerti+raeductionPrevCumulative+secDepositPrevCerti+pettyExpensesPrevCerti+otherAmtPrevCerti;
						$("#totalAmtPrevCerti").val(raTotaoPrevAmt.toFixed(2));
						
						totalCumulative=totalCumulative.toFixed(2);
						$("#totalAmtCumulative").val(totalCumulative);
						var raTotalCc=$("#raTotalCc").val()==""?0:parseFloat($("#raTotalCc").val());
						$("#totalAmtCumulativeCertified").val((raTotalCc-totalCumulative).toFixed(2));
						
						//$("#totalAmtPreviousCertified").val(raTotalPc-raTotaoPrevAmt);
						//$("#totalAmtPreviousCertified").val("0.00");
												
						var totalAmountToPAY=$("#totalCc").val()==""?0:$("#totalCc").val();
						var radedctiontempval=$("#raeductionCumulative").val();
						$("#raeductionCumulative").val(parseFloat(radedctiontempval).toFixed(2));
						
						$("#totalAmtPreviousCertified").val(parseFloat($("#raTotalPc").val())-parseFloat($("#totalAmtPrevCerti").val()));
						
						if(billType=="ADV"){					
							finalAmt=$("#finalAmt").val()==""?0:parseFloat($("#finalAmt").val());
							$("#advanceCurrAmount").val(finalAmt.toFixed(2));
							let totalAmtPreviousCertified=$("#totalAmtPreviousCertified").val();
							let releaseAdvTotalAmt=$("#releaseAdvTotalAmt").val();
							let releaseAdvPrevAmt=$("#releaseAdvPrevAmt").val();
							if(totalAmtPreviousCertified==""||totalAmtPreviousCertified=="null"||totalAmtPreviousCertified==undefined){		totalAmtPreviousCertified=0;
								}else{totalAmtPreviousCertified=parseFloat(totalAmtPreviousCertified);}
							
							if(releaseAdvTotalAmt==""||releaseAdvTotalAmt=="null"||releaseAdvTotalAmt==undefined){	
								releaseAdvTotalAmt=0;
							}else{	releaseAdvTotalAmt=parseFloat(releaseAdvTotalAmt);}
							
							if(releaseAdvPrevAmt==""||releaseAdvPrevAmt=="null"||releaseAdvPrevAmt==undefined){	releaseAdvPrevAmt=0;
							}else{	releaseAdvPrevAmt=parseFloat(releaseAdvPrevAmt);	}
							
							$("#releaseAdvTotalAmt").val((releaseAdvPrevAmt+finalAmt).toFixed(2));
							totalAmtCumulativeCertified=$("#totalAmtCumulativeCertified").val((finalAmt+totalAmtPreviousCertified).toFixed(2));
							$("#finalAmt").val(finalAmt.toFixed(2));
							
							
							$("#secDepositCurrentCerti").val('0.00');
							$("#pettyExpensesCurrentCerti").val('0.00');
							$("#raDeductionAmt").val('0.00');
							$("#other").val('0.00');
							//$("#raPaidAmnt").val('0.00');
							$("#raAmountToPay").val('0.00');
							$("#totalCc").val('0.00');
							$("#raDeductionAmt").val('0.00');
						}
				
			 }
			   var amountInWords=	convertNumberToWords(finalAmt);
			   
			   if(finalAmt==0){
				   $("#amountInWords").text("Zero Rupees Only");
			   }else{
				   $("#amountInWords").text(amountInWords);
			   }
			   $("#appendBillDetails").append(advHtmlData);
			   $("#appendBillDetails").append(raHtmlData);
			
			    $("#paymentLedgerTable").show();
			    $("#paymentLedgerTable").show();	
			    $("#contractorRABill").show();
			    $("#sumadhuraLogoAndName").show();
			    $("#billLedgerOfBill").show();
			    $("#printshowRa").show();
			   
			   
			   var billType=$("#billType").val();
			   if(billType=="RA"){
				   var raAmountToPay=	$("#raAmountToPay").val();
				   var secDepositCurrentCerti=$("#secDepositCurrentCerti").val();
				   if(secDepositCurrentCerti==""||secDepositCurrentCerti=="0"){
					$("#securityPer").html(" (0%)");
				   }else{
					   var num=(secDepositCurrentCerti/raAmountToPay)*(100);
					   num=num.toFixed(0);
					   $("#securityPer").html(" ("+num+"%)");
				}
			}
			  
			$("#workOrderAMount").text(inrFormat(parseFloat($("#workOrderAMount").text()).toFixed(2)));
			$("#workOrderAMount1").text(inrFormat(parseFloat($("#workOrderAMount1").text()).toFixed(2)));
			/*$(".addFractionAndMakeInrFormat").each(function(){
				debugger;
				var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
				$(this).text(inrFormat(currentvalue.toFixed(2)));
			});*/
			
			//$("#workOrderAMount").text(inrFormat(parseFloat($("#workOrderAMount").text())));
			
		/*	 $('span').each(function(){
					// Do your magic here
				    if (isNum($(this).text())){ // regular expression for numbers only.
				    	var tempVal=parseFloat($(this).text());
				    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"&&this.id!="notificationBell"){
				    		$(this).text(inrFormat(tempVal.toFixed(2)));
				    	}
			          }
				});*/
				$('input[type="text"]').each(function(){
				    if (isNum(this.value)){ // regular expression for numbers only.
				    	var tempVal=parseFloat(this.value);
				    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"){
				    		$(this).val(inrFormat(tempVal.toFixed(2)));
				    	}
			           }
				});
			$(".overlay_ims").hide();
			$(".loader-ims").hide();
			
		},error:function(error){
			console.log(error);
		}
 });
	
	var billType=$("#billType").val();	
	var majorHeadNames=new Array();
	debugger;
	var isThisBillLedger=$("#isThisBillLedger").val();
//	alert(isThisBillLedger);
	//if this is the ledger page no need to load data again because it's already loaded 
	//so the data stored in session storage 
	if(isThisBillLedger=="true"){
		if (typeof(Storage) !== "undefined") {
			debugger;
			//this session storage used for showing type of work
		  var   majorHeadNames=sessionStorage.getItem("${UserId}"+tempBillNo.trim()+"tempBillNoMajorHeadNames");
		  $("#typeOfWork").text(majorHeadNames);
		  $("#typeOfWork1").text(majorHeadNames);
		 } else {
		    alert("Sorry, your browser does not support Web Storage...");
		 };
	}
	if(isThisBillLedger!="true")
	$.ajax({
		url : "loadPermanentWorkOrderArea.spring",
		type : "GET",
		data : {
			contractorId : ContractorId,
			workOrderNo:workOrderNo,
			billDate:billDate,
			billType:billType,
			approvePage:approvePage,
			tempBillNo:tempBillNo,
			billNo:billNo,
			site_id:site_id
		},
		success : function(data	) {			
			str="";			
			var i=1;
			var basic_amount=0;
			var price=0;
			var area_alocated_qty=0;
			
			var majorHeadName="";
			var minorHeadName="";
			var workDescName="";
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
				basic_amount=(value.TOTAL_WO_AMOUNT==null?"0":value.TOTAL_WO_AMOUNT);
				var blockName=(value.BLOCK_NAME==null?"-":value.BLOCK_NAME);
				var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
				var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
		
				price=value.PRICE;
				area_alocated_qty+=value.AREA_ALOCATED_QTY;
				if(approvePage=="true"){
					var htmlData="";
					htmlData+="<input type='hidden'  name='actualWorkAreaId' id='actualWorkOrderAreaId' style='border:none; text-align:center;' value='"+value.WO_WORK_AREA_ID+"' readonly/>";
					htmlData+="<input type='hidden'  name='actualQty' id='actualQty' style='border:none; text-align:center;' value='"+value.ALLOCATED_QTY+"' readonly/>";
					$("#appendBillDetails").append(htmlData);
					var pravArea=value.PREVAREA==""?0:value.PREVAREA;
					pravArea=pravArea==null?0:pravArea;
					//cheking is mejor head name is already printed or not 
					if(majorHeadName!=value.WO_MAJORHEAD_DESC){
						majorHeadLength++;
						//stroing major head name and work desc in temp variable
						majorHeadName=value.WO_MAJORHEAD_DESC;
						workDescName=value.WO_WORK_DESCRIPTION;
						minorHeadName=value.WO_MINORHEAD_DESC;
						majorHeadNames.push(value.WO_MAJORHEAD_DESC);
						increIndex=1;
						str+=" <tr> " +
							"  <td colspan='9' class='text-center'style='font-size: 15px;'><strong>"+value.WO_MAJORHEAD_DESC+"</strong></td>" +
							/*"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''><input type='hidden' value='CertifiedTotalQTY'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid'></td>" +
							"  <td class='text-right'style=''><input type='hidden' value='PrevousQTY'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='PrevousPaidAmt' id='PrevousQTYtoGive'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='Current' id='currentQTYtoGive'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current' id='currentAmountPay'></td>" +*/
							"   </tr>";
					
					
						str+=" <tr> " +
							"  <td class='text-center'colspan='9' style=''><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +
							/*"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''></td>" +
							"  <td class='text-right'style=''><input type='hidden' value='CertifiedTotalQTY'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid'></td>" +
							"  <td class='text-right'style=''><input type='hidden' value='PrevousQTY'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='PrevousPaidAmt' id='PrevousQTYtoGive'></td>" +
							"   <td class='text-right'style=''><input type='hidden' value='Current' id='currentQTYtoGive'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current' id='currentAmountPay'></td>" +*/
							"   </tr>";
					

						str+=" <tr> " +
							"  <td colspan='9' class='text-center'>"+value.WO_WORK_DESCRIPTION+"</td>" +
							/*"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'><input type='hidden' value='Certified'></td>" +
							"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid"+i+"'></td>" +
							"  <td class='text-right'><input type='hidden' value='Prevous'></td>" +
							"   <td class='text-right'><input type='hidden' value='PrevousPaidAmt' id='PrevousQTYtoGive"+i+"'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current'> <input type='hidden' name='issueDetailId"+i+"' id='issueDetailId"+i+"' value='"+value.WO_WORK_ISSUE_DTLS_ID+"'> </td>" +
							"   <td class='text-right'></td>" +*/
							"   </tr>";
						//checking is work description is changed or not if changed execute this block to print the work description
					}else if(minorHeadName!=value.WO_MINORHEAD_DESC){
						minorHeadName=value.WO_MINORHEAD_DESC;
						str+=" <tr> " +
							/*"  <td class='text-center'>"+(String.fromCharCode(textHeading++))+"</td>" +*/
							"  <td colspan='9' class='text-center'style=''><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +
							/*"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'><input type='hidden' value='CertifiedTotalQTY'></td>" +
							"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid'></td>" +
							"  <td class='text-right'><input type='hidden' value='PrevousQTY'></td>" +
							"   <td class='text-right'><input type='hidden' value='PrevousPaidAmt' id='PrevousQTYtoGive'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current' id='currentQTYtoGive'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current' id='currentAmountPay'></td>" +*/
							"   </tr>";
					} 
				
					if(workDescName!=value.WO_WORK_DESCRIPTION){
						//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
					
						workDescName=value.WO_WORK_DESCRIPTION;
						str+=" <tr> " +
							"  <td colspan='9' class='text-center'style=''>"+value.WO_WORK_DESCRIPTION+"</td>" +
							/*"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'></td>" +
							"  <td class='text-right'><input type='hidden' value='Certified'></td>" +
							"   <td class='text-right'><input type='hidden' value='CertifiedPaidAmt' id='CertifiedQTYPaid"+i+"'></td>" +
							"  <td class='text-right'><input type='hidden' value='Prevous'></td>" +
							"   <td class='text-right'><input type='hidden' value='PrevousPaidAmt' id='PrevousQTYtoGive"+i+"'></td>" +
							"   <td class='text-right'><input type='hidden' value='Current'> <input type='hidden' name='issueDetailId"+i+"' id='issueDetailId"+i+"' value='"+value.WO_WORK_ISSUE_DTLS_ID+"'> </td>" +
							"   <td class='text-right'></td>" +*/
							"   </tr>";
					}
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
					try {
						var index=value.PREVAREA.search("@@");
						if(index>=0){
							prevAreaQuantity=value.PREVAREA.split("@@");
							for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
								let tempQtyAndRate = prevAreaQuantity[ind].split("-");
								lengthOfThePrevArea++;
								pravArea+=parseFloat(tempQtyAndRate[0]);
								var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
								if(ind<prevAreaQuantity.length-1){
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
								}else{
									customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
								}
								previousBillAmount+=tempQtyAndRate[0]*rate;
						}
					}else{
						let tempQtyAndRate = value.PREVAREA.split("-");
						lengthOfThePrevArea++;
						pravArea+=parseFloat(tempQtyAndRate[0]);
						var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
						previousBillAmount+=tempQtyAndRate[0]*rate;
					}
				} catch (e) {
					console.log(e);
				}
				
			//	var multiplyArea=(parseFloat(pravArea)+allocatedArea);
				var nextLevelApprovelEmpID=$("#nextLevelApprovelEmpID").val();
				
				//cheking is this ra bill is completed or not 
				if(nextLevelApprovelEmpID=="END"){
					var cumulativeQuantity=0;
					var currentQuantity=value.ALLOCATED_QTY==undefined?0.00:parseFloat(value.ALLOCATED_QTY);
					var previousQuantity=pravArea;
				
					multiplyArea=(parseFloat(previousQuantity)+currentQuantity);

					var newchar = '+';
					prevAreaQuantity = prevAreaQuantity.toString().split(',').join(newchar);
					
					
					 sumOfPrevQty+=+previousQuantity;
					 sumOfCumulativeQTy+=+multiplyArea;
					 sumOfPrevAmount+=+(previousBillAmount);
					 sumOfCumulativeAmount+=+(currentQuantity*value.PRICE+previousBillAmount);
					 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
					 
					 sumOfAllocatedQuantity+=+currentQuantity;
					 sumOfCurrentAmount+=+(currentQuantity*value.PRICE);
					 str+=" <tr> " ;
					 if(lengthOfThePrevArea>=2)
						str+="  <td class='text-left' style='width:271px;word-break: break-word;'>"+value.BLOCK_NAME+blockNames+"<br>"+customMsg+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
					 else
						str+="  <td class='text-left' style='width:271px;word-break: break-word;'>"+value.BLOCK_NAME+blockNames+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
					
					 str+="  <td class='text-right'><span id='TotalQty"+i+"'>"+value.AREA_ALOCATED_QTY.toFixed(2)+"</span></td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
							"  <td class='text-right'><span id='TotalRate"+i+"'>"+value.PRICE.toFixed(2)+"</span></td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
							"  <td class='text-right'><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
							"  <td class='text-right'><span id='CBQty"+i+"'>"+multiplyArea.toFixed(2)+"</span><input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
							"  <td class='text-right'><span id='CBAmount"+i+"'>"+((currentQuantity*value.PRICE+previousBillAmount).toFixed(2))+"</span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
							"  <td class='text-right'><span id='PCQty"+i+"'>"+(previousQuantity.toFixed(2))+"</span></td>" +
							"  <td class='text-right'><span id='PCAmount"+i+"'>"+((previousBillAmount).toFixed(2))+"</span></td>" +
							"   <td class='text-right'><span>"+(currentQuantity).toFixed(2)+"<span></td>" +
							"   <td class='text-right'><span id='CCAmount"+i+"'>"+((currentQuantity*value.PRICE).toFixed(2))+"</span></td>"+
							"   </tr>";
				 }	
			}
			i++;	
			});
			debugger;
			str+=" <tr style='background-color:#ccc;'>  " +
				"  <td class='text-left'style='width:271px;word-break: break-word;'><strong>Total Amount/Quantity</strong></td>" +
				"  <td class='text-right' ><span style='display:none;'>"+sumOfTotalQunatity.toFixed(2)+"</span></td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
				"  <td class='text-right' > </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
				"  <td class='text-right' ></td>" +
				"  <td class='text-right'> <span id='cumulativeQuantity' style='display:none'>"+sumOfCumulativeQTy.toFixed(2)+"</span></td>" +
				"  <td class='text-right'><span id='cumulativeAmount'>"+sumOfCumulativeAmount.toFixed(2)+"</span></td>" +
				"  <td class='text-right'><span id='previousQuantity'  style='display:none'>"+sumOfPrevQty.toFixed(2)+" </span></td>" +
				"  <td class='text-right'><span id='previousAmount'>"+sumOfPrevAmount.toFixed(2)+"</span></td>" +
				"   <td class='text-right'><span id='currentQuantity'  style='display:none'>"+sumOfAllocatedQuantity.toFixed(2)+"</span></td>" +
				"   <td class='text-right'><span id='currentAmount'>"+sumOfCurrentAmount.toFixed(2)+"</span></td>"+
				"   </tr>";
			
			$("#paymentByArea").html(str);
			debugger;
			var newchar = '\\';
			majorHeadNames = majorHeadNames.toString().split(',').join(newchar);
			$("#typeOfWork").text(majorHeadNames);
			
			if (typeof(Storage) !== "undefined") {
				debugger;
				//this session storage used for showing type of work
			    sessionStorage.setItem("${UserId}"+tempBillNo.trim()+"tempBillNoMajorHeadNames",majorHeadNames);
			 } else {
			    alert("Sorry, your browser does not support Web Storage...");
			 };
			
			$('span').each(function(){
				// Do your magic here
			    if (isNum($(this).text())){ // regular expression for numbers only.
			    	var tempVal=parseFloat($(this).text());
			    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"&&this.id!="notificationBell")
			    		$(this).text(inrFormat(tempVal.toFixed(2)));
		           }
			});
			$("#hideIt").show();
		},
		error : function(err) {
			alert(err);
		}
	});
}
//converting number to word
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
//validating file size and file type
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
					if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
				        ext= fileName.substring(fileName.lastIndexOf(".")+1); 
					alert("Please Upload Below 1 MB "+ext+" File");
					count++;
					//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
					$(this).val('');
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
	  if(count!=0){
		  return false;  
	  }else{
		  return true;
	  }
	 
}
function goToTop(){
    window.scrollTo(0, 0);
}
$(document).ready(function(){
	//contractor copy
	$("#contractorCopy").on("click",function(){
		var contractorName=$("#ContractorName").val();
		var contractorId=$("#ContractorId").val();
		var url="contractorCopy.spring";
	    
		$('#contractorRABill').attr('target', '_blank');
	    document.getElementById("contractorRABill").action =url;
	    document.getElementById("contractorRABill").method = "POST";
	    document.getElementById("contractorRABill").submit();
		
		
	/*	$("#Sumadhura_copy").hide();
		$("#EmployeeSignRow").hide();
		
		$("#Contractor_copy").show();
		$("#ContractorSignRow").show();*/
	});
	
	$("#billLedger").on("click",function(){
		/*	var contractorName=$("#ContractorName").val();
			var contractorId=$("#ContractorId").val();*/
			var url="woBillLedger.spring";
			$('#contractorRABill').attr('target', '_blank');
		    document.getElementById("contractorRABill").action =url;
		    document.getElementById("contractorRABill").method = "POST";
		    document.getElementById("contractorRABill").submit();
		});
	
	sessionStorage.setItem($("#userId").val()+"deleteUploadedFiles","");
	//updating bill 
	$("#updateBill").on("click",function(){
		 var contractorName=$("#ContractorName").val();
		 var contractorId=$("#ContractorId").val();
		 var result=validateFileExtention();
		 if(result==false){
			 return false;
		 }
		 var canISubmit = window.confirm("Do you want to update bill?");
		
		 if(canISubmit == false) {
		        return false;
		 }
		 excuteDeleteFunction();
		 sessionStorage.setItem($("#userId").val()+"deleteUploadedFiles","");
		  
	});
	//abstract print
	$("#abstractPrintBtn").on("click",function(){
		 var contractorName=$("#ContractorName").val();
		 var contractorId=$("#ContractorId").val();
		 var url="printAbstract.spring";
	 
		 
		 $('#contractorRABill').attr('target', '_blank');
	      document.getElementById("contractorRABill").action =url;
	      document.getElementById("contractorRABill").method = "POST";
	      document.getElementById("contractorRABill").submit();
	});

	setTimeout( function(){
			loadPreviousCompletedBillData();
			loadRARecovery();
	}, 1500)
 });
