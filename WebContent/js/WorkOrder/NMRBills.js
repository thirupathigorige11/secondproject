//for multiple select box  for Block
$(document).ready(function() {
	$("#typeofworkselect").select2();
});

(function($) {
	$.widget("custom.combobox",
				{
						_create : function() {
							this.wrapper = $("<span>").addClass("custom-combobox").insertAfter(this.element);
							this.element.hide();
							this._createAutocomplete();
							this._createShowAllButton();
						},

						_createAutocomplete : function() {
							var selected = this.element.children(":selected"), value = selected
									.val() ? selected.text() : "";

							this.input = $("<input>").appendTo(this.wrapper)
									.val(value).attr("title", "")
									.attr("id", this.element[0].name)								
									.autocomplete({
										delay : 0,
										minLength : 0,
										source : $.proxy(this, "_source")
									}).tooltip({
										tooltipClass : "ui-state-highlight"
									});

							this._on(this.input, {
								autocompleteselect : function(event, ui) {
									ui.item.option.selected = true;
									
									var workDesc = "";
									var ele = this.element[0].name;									
									// Removing numbers from the header names
									var str1 = ele.replace(/[0-9]/g, '');	
									var rowNum = ele.match(/\d+/g);
									
									if (str1 == 'workDesc') {
										var workOrderId = ui.item.option.value;
										
										loadWorkOrderNo(workOrderId);
										this._trigger("select", event, {
											item : ui.item.option
										});
									}else{
										var status=NMRDescriptionCompare('description', rowNum);
										if(status!=false){
											this._trigger("select", event, {
												item : ui.item.option
											});
										}else{
											$("#Description"+rowNum).val("");
											return false;
										}
									}									
								},
								autocompletechange : "_removeIfInvalid"
							});
						},

						// Enable below code to create Show All Button - Start
						_createShowAllButton : function() {
							var input = this.input, wasOpen = false;
							$("<a>").attr("tabIndex", -1).attr("title",	"Show All Items").tooltip().appendTo(this.wrapper).button({
								icons : {
									primary : "ui-icon-triangle-1-s"
								},
								text : false
							}).removeClass("ui-corner-all").addClass("custom-combobox-toggle ui-corner-right").mousedown(function() {
												wasOpen = input.autocomplete(
														"widget")
														.is(":visible");
											}).click(function() {
										input.focus();

										// Close if already visible
										if (wasOpen) {
											return;
										}

										// Pass empty string as value to search
										// for, displaying all results
										input.autocomplete("search", "");
									});
						},
						// Enable below code to create Show All Button - End

						_source : function(request, response) {
							var matcher = new RegExp($.ui.autocomplete
									.escapeRegex(request.term), "i");
							response(this.element.children("option").map(
									function() {
										var text = $(this).text();
										if (this.value	&& (!request.term || matcher.test(text)))
											return {
												label : text,
												value : text,
												option : this
											};
									}));
						},

						_removeIfInvalid : function(event, ui) {

							// Selected an item, nothing to do
							if (ui.item) {
								return;
							}

							// Search for a match (case-insensitive)
							var value = this.input.val(), valueLowerCase = value
									.toLowerCase(), valid = false;
							this.element.children("option").each(function() {
												if ($(this).text().toLowerCase() === valueLowerCase) {
													this.selected = valid = true;
													return false;
												}
											});

							// Found a match, nothing to do
							if (valid) {
								return;
							}

							// Remove invalid value
							this.input.val("").attr("title", value + " didn't match any item").tooltip("open");
							this.element.val("");
							this._delay(function() {
								this.input.tooltip("close").attr("title", "");
							}, 2500);
							// this.input.autocomplete( "instance" ).term = "";
						},
						_destroy : function() {
							this.wrapper.remove();
							this.element.show();
						}

					});
})(jQuery);

$(function() {
	$("#combobox1").combobox();
	$("#toggle").click(function() {
		$("#combobox1").toggle();
	});
});
$(function() {
	$("#comboboxworkOrderNo").combobox();

});

function loadWorkOrderNo() {

	var contractorId = document.getElementById("ContractorId").value;
	var siteId = document.getElementById("siteId").value;
	var url = "getWorkOrderNo.spring?workDescId=0&siteId=" + siteId	+ "&contractorId=" + contractorId + "&typeOfWork='NMR'";
	var request = getAjaxObject();
	try {
		request.onreadystatechange = function() {

			if (request.readyState == 4 && request.status == 200) {

				var resp = request.responseText;
				resp = resp.trim();

				var spltData = resp.split("|");				
				available = new Array();
				for (var j = 0; j < spltData.length; j++) {
					available[j] = spltData[j];
				}

				var subProdToSet = "workOrderNo";				
				var selectBox = document.getElementById(subProdToSet);
				
				// Removing previous options from select box - Start
				if (document.getElementById(subProdToSet) != null
						&& document.getElementById(subProdToSet).options.length > 0) {
					document.getElementById(subProdToSet).options.length = 0;
				}
				// Removing previous options from select box - End

				initOpt = document.createElement("option");
				initOpt.text = "--Select--";
				initOpt.value = "";
				selectBox.appendChild(initOpt);

				var defaultOption;
				var data;

				for (var i = 0; i < available.length; i++) {
					defaultOption = document.createElement("option");
					data = available[i].split("_");
					if (data[0] != "" && data[0] != null && data[0] != '') {
						var prodIdAndName = data[0] + "$" + data[1] + "$"
								+ data[2];
						defaultOption.text = data[1];
						defaultOption.value = prodIdAndName;
						selectBox.appendChild(defaultOption);
					}
				}
			}
		};
		request.open("POST", url, false);
		request.send(null);
	} catch (e) {
		alert("Unable to connect to server!");
	}
}

//auto complete for contractor name
$("#ContractorName").keyup(function() {

	var contractorName = $("#ContractorName").val();

	var url = "loadAndSetContractorInfo.spring";
	$("#ContractorName").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : url,
				type : "GET",
				data : {
					contractorName : contractorName
				},
				contentType : "application/json",
				success : function(data) {
					response(data.split("@@"));
				}
			});
		},
		change : function(event, ui) {
			if (!ui.item) {
				// The item selected from the menu, if any. Otherwise the
				// property is null
				// so clear the item for force selection
				$("#ContractorName").val("");
			}
		},
		autoFocus : true,
		select : function(event, ui) {
			AutoCompleteSelectHandler(event, ui);
		}

	});
});

// code for selected text
function AutoCompleteSelectHandler(event, ui) {
	var selectedObj = ui.item;
	isTextSelect = "true";
	$("#workOrderNo").val("");
	$("#totalAmtToPay").val("");
	var contractorName = selectedObj.value;

	var url = "loadAndSetVendorInfoForWO.spring";
	$.ajax({
		url : url,
		type : "get",
		data : {
			contractorName : contractorName,
			loadcontractorData : true
		},
		contentType : "application/json",
		success : function(data) {
			$("#contractorName").val(contractorName);
			if (data != "" || data != "null") {

				var contractorData = data[0].split("@@");
				var contractorId = contractorData[0];

				$("#ContractorId").val(contractorId);
				$("#MobileNo").val(contractorData[3]);
				$("#PanCardNo").val(contractorData[4]);
				$("#AccountNo").val(contractorData[5]);
				$("#ifscCode").val(contractorData[6]);
				$("#contractorGSTIN").val(contractorData[7]);
				$("#printContractorName").text(contractorName);
				loadWorkOrderNo();
			}

		},
		error : function(data, status, er) {
			alert(data + "_" + status + "_" + er);
		}
	});
}

function getAjaxObject() {

	var request = null;

	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return request;
}

// calculating Current Certified amount
function isNum(value) {
	var numRegex = /^[0-9.]+$/;
	return numRegex.test(value)
}

function loadNMRCompletedBillData() {
	var ContractorId = $("#ContractorId").val();
	var ContractorName = $("#ContractorName").val();
	var workOrderNo = $("#workOrderNo").val();
	var approvePage = $("#approvePage").val();
	var typeOfWork = "NMR";
	var siteId = $("#siteId").val();
	var billNo=$("#NMRBillNo").val();
	var isApprovePage="false";
debugger;
	$.ajax({
				url : "loadCompletedNMRBillData.spring",
				type : "GET",
				data : {
					contractorId : ContractorId,
					workOrderNo : workOrderNo,
					approvePage : approvePage,
					siteId : siteId,
					typeOfWork : typeOfWork,
					billNo:billNo,
					isApprovePage:isApprovePage
					
				},
				success : function(data) {
					var billLedgerData = "";
					var	PETTY=0.00;
					var	OTHER=0.00;
					var RECOVERY=0.00;
					var holdPrevAmount=0
					$.each(data,function(key, value) {
						var printAdvAmount=0.00;
						var printSecDeposit=0.00;
						var printPettyExpences=0.00;
						var printOther=0.00;
						var printRecoveryAmt=0.00;
							debugger;
						if(value.DEDUCTION_AMOUNT1!=null){
							var typeOfDeduction=new Array();
							var typeOfDeductionVal=new Array();
							//type of deduction in coming in the array where is spliting by @@
							typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
							//type of DEDUCTION_AMOUNT in coming in the array where is spliting by @@
							typeOfDeductionVal=	value.DEDUCTION_AMOUNT1.split("@@")
							for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
								
								if (typeOfDeduction[i2]=="OTHER"){
									OTHER+=+typeOfDeductionVal[i2]
									printOther=parseFloat(typeOfDeductionVal[i2]);
								}else if (typeOfDeduction[i2]=="PETTY"){
									PETTY+=+typeOfDeductionVal[i2]
									printPettyExpences=parseFloat(typeOfDeductionVal[i2]);
								}else if (typeOfDeduction[i2]=="RECOVERY"){
									RECOVERY+=+typeOfDeductionVal[i2]
									printRecoveryAmt=parseFloat(typeOfDeductionVal[i2]);
								}
							}
						}
										
					billLedgerData += '<tr>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;width:10%;">'+value.ENTRY_DATE+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><a target="_blank" style="color:#0000ff;" class="anchorblue" href="showWOCompltedBillsDetails.spring?BillNo='
						+ value.BILL_ID+ '&WorkOrderNo='+ workOrderNo.split("$")[0]+ '&billType='+ value.PAYMENT_TYPE+ '&site_id='+ value.SITE_ID+ '&isBillUpdatePage=false&status=true">'+ value.BILL_ID + '</a></td>';
						 	debugger;
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="BillAmountCls"><span>'+inrFormat(parseFloat(value.CERTIFIED_AMOUNT).toFixed(2)) + '</span></td>';// CERTIFIED_AMOUNT
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td> ';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="MaterialDeductions">'+inrFormat(printRecoveryAmt.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="PettyExpenses">'+inrFormat(printPettyExpences.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="OtherAmount">'+inrFormat(printOther.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					/*billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="RecoveryAmountCls"><span>'+parseFloat(value.CERTIFIED_AMOUNT-value.PAYBLE_AMOUNT)+'</span></td>';*/
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="AmountPaidCls"><span>'+ inrFormat(parseFloat(value.PAYBLE_AMOUNT).toFixed(2)) + '</span></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="CumulativeAmountCls"><span>'+ inrFormat((holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT)).toFixed(2))+ '</span></td>	';
					billLedgerData += '</tr>';
					holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);
								
				});
					
					$("#pettyExpensesPrevCerti").val((PETTY).toFixed(2));
					$("#otherAmtPrevCerti").val((OTHER).toFixed(2));
					
					$("#previousRecovery").val(RECOVERY.toFixed(2));
					$("#cumulativeRecovery").val(RECOVERY.toFixed(2));
					
					$("#hiddenpreviousPettyExpences").val((PETTY).toFixed(2));
					$("#hiddenpreviousotherAmount").val((OTHER).toFixed(2));
					$("#hiddenRecoveryAmount").val((RECOVERY).toFixed(2));
					
					$("#pettyExpensesCumulative").val((PETTY).toFixed(2));
					$("#otherAmtCumulative").val((OTHER).toFixed(2));
					
					$("#previousRecovery").val((RECOVERY).toFixed(2));
					$("#cumulativeRecovery").val((RECOVERY).toFixed(2));
					
					holdPrevAmount=holdPrevAmount.toFixed(2)
					billLedgerData += '<tr>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;width:10%;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';// CERTIFIED_AMOUNT
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td> ';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><span  id="TotalCumulativeAmount">'+inrFormat(parseFloat(holdPrevAmount).toFixed(2))+'</span></td>	';
					billLedgerData += '</tr>';
					
					billLedgerData += '<tr style="background-color: #ccc;">';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;width:10%;"></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">Total</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" id="TotalBillAmount"></td>';// CERTIFIED_AMOUNT
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td> ';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">'+inrFormat(RECOVERY.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">'+inrFormat(PETTY.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">'+inrFormat(OTHER.toFixed(2))+'</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">-</td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><span  id="TotalAmountPaid">'+inrFormat(holdPrevAmount)+'</span></td>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><span  id="TotalCumulativeAmount"></span></td>	'; //'+inrFormat(holdPrevAmount)+'
					billLedgerData += '</tr>';

					$("#ledger").html(billLedgerData);
					calLedgerTotalValues();
					
					if(data.length==0){
						$("#paymentLedgerTable").hide();
						$("#appendNMRBillDetailsNoDataMsg").html("<h3 style='text-align:center;'>No payment has been initiated for this Work Order.</h3>");
					}else{
						$("#paymentLedgerTable").show();
						 $("#appendBillDetailsTotal").html("<div  style='font-size: 20px;margin: 10px 20px 0px 0px;'><div style='width:350px;'><div style='float:left;width:200px;'> Net Payable Amount</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'>"+$("#TotalAmountPaid").text()+"</div></div><div style='width:350px;'><div style='float:left;width:200px;'>Total Deduction</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'>"+inrFormat((RECOVERY+PETTY+OTHER))+"</div></div><div style='width:350px;'><div style='float:left;width:200px;'>Grand Total</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'>"+$("#TotalBillAmount").text()+"</div></div></div>");
					}
				//	pettyAndOtherChange();
					/*$('strong').each(function(){
						// Do your magic here
					    if (isNum($(this).text())){ // regular expression for numbers only.
					    	var tempVal=parseFloat($(this).text());
					    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
					    		$(this).text(inrFormat(tempVal.toFixed(2)));
				           }
					});*/
				}
			});
}

// **************************************************************************************************
function loadNMRBillData() {
	var ContractorId = $("#ContractorId").val();
	var ContractorName = $("#ContractorName").val();
	var workOrderNo = $("#workOrderNo").val();
	var approvePage = $("#approvePage").val();
	var typeOfWork = "NMR";
	var siteId = $("#siteId").val();
	$("#printWorkOrderNo").text(workOrderNo.split("$")[0]);
	if (workOrderNo.length == 0) {
		return false;
	}
	$.ajax({
				url : "loadNMRBillData.spring",
				type : "GET",
				data : {
					contractorId : ContractorId,
					workOrderNo : workOrderNo,
					approvePage : approvePage,
					siteId : siteId,
					typeOfWork : typeOfWork
				},
				success : function(data) {
					var workDescNames = new Array();
					var minorHeadNames = new Array();

					var strNMRFirstRowtbodyData = "";
					var strNMRtheadData = "";
					var strNMRSecondRowtbodyData = "";
					var strNMRThirdRowtbodyData = "";
					var strAbstractTableData = "";
					var strFirstRowOfTableHead = "";
					var strSecondRowOfTableHead = "";
					var serialNumber = 1;
					var d = new Date();
					var date = d.toLocaleDateString();

					lengthOfRows = data.length;
					// dynamic Headers for table
					$("#noOfRowsToIterate").val(1);
					
					var tempMinorHeadName = "";
					var tempWORowCode = "";
					var tempWorkDescName = "";
					var tempWDName = new Array();
					var minorHeadNames2=new Array();
					// Total Number of minor head and major head's
					var noOfWorkDesc = 0;
					var noOfMinorHead = 0;
					var regExpr = /[^a-zA-Z0-9-. ]/g;
					var duplicateWorkAreaId="";
					var workDescriptionid="";
					$("#NMRAbstractTableData").html('');
					$.each(data,function(key, value) {
						debugger;
							var regExpr = /[^a-zA-Z0-9-. ]/g;
							const isThis_Str_InArray1 = value.WO_MINORHEAD_DESC.replace(regExpr, "")+ "@@"+ value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+ "@@" + value.RATE+"@@"+value.WO_ROW_CODE;
							const isInArray1 = minorHeadNames.includes(isThis_Str_InArray1);
							if (isInArray1 == false) {
								debugger;
								minorHeadNames.push(isThis_Str_InArray1);
							}
							
							let WO_WORK_DESCRIPTION = value.WO_WORK_DESCRIPTION.replace(/ /g, '');
							try {
									WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.trim();
									WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.replace(regExpr, "");
							} catch (e) {
								console.log(e);
							}
							
							var previousQty = value.PREVQTY == null ? 0	: value.PREVQTY;
							/*for (var index = noOfMinorHead; index <= noOfMinorHead; index++) {
								debugger;
								let str = minorHeadNames[index].split("@@");
								tempMinorHeadName=str[0];
								tempWORowCode=str[4];
								//var regExpr = /[^a-zA-Z0-9-. ]/g;
								strAbstractTableData += '<tr>';
								strAbstractTableData += '<td style="text-align:center;padding:5px;"><h5><strong></strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+ value.WO_WORK_DESC_ID+ '"><input type="hidden" name="majorheadId" id="majorheadId" value="'+ value.WO_MAJORHEAD_ID+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID+ '"></td>';
								strAbstractTableData += '<td style="padding:5px;" colspan="11"><h5><strong>'+ tempMinorHeadName	+ '('+tempWORowCode+')</strong><h5><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+ value.WO_MEASURMENT_ID+ '"></td>';
								strAbstractTableData += '</tr>';
							}*/
							//avoiding printing duplicate minor head 
							//by checking it's already printed or not
							/*if (tempMinorHeadName != (value.WO_MINORHEAD_DESC+value.WO_ROW_CODE)){
								tempMinorHeadName = (value.WO_MINORHEAD_DESC+value.WO_ROW_CODE) ;
								//tempWORowCode=value.WO_ROW_CODE;
								noOfMinorHead++;
								strAbstractTableData += '<tr>';
								strAbstractTableData += '<td style="text-align:center;padding:5px;"><h5><strong></strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+ value.WO_WORK_DESC_ID+ '"><input type="hidden" name="majorheadId" id="majorheadId" value="'+ value.WO_MAJORHEAD_ID+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID+ '"></td>';
								strAbstractTableData += '<td style="padding:5px;" colspan="11"><h5><strong>'+ value.WO_MINORHEAD_DESC	+ '('+value.WO_ROW_CODE+')</strong><h5><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+ value.WO_MEASURMENT_ID+ '"></td>';
								strAbstractTableData += '</tr>';
								tempWorkDescName = "";
							}*/
										//checking here is this WD is exist or not if exist it will give true or false
							const isThis_Str_InArray = value.WO_WORK_DESCRIPTION+ "@@" + value.WO_WORK_DESC_ID;
							const isInArray = tempWDName.includes(isThis_Str_InArray);

							if (isInArray == false) {
									noOfWorkDesc++;
									workDescNames.push(WO_WORK_DESCRIPTION+ "@@"+ value.WO_WORK_DESC_ID);
									tempWDName.push(isThis_Str_InArray);
							}
							
							if (tempWorkDescName != (value.WO_WORK_DESCRIPTION)) {
									tempWorkDescName = (value.WO_WORK_DESCRIPTION);
									var previousQty = 0;
									var prevAreaQuantity = new Array();
									var lengthOfThePrevArea = 0;
									var previousNMRBillAmount=0;
									var printPreviousRateAndQty="";											
									try {
										var index = value.PREVQTY.search("@@");
										if (index >= 0) {
										prevAreaQuantity = value.PREVQTY.split("@@");
										var tempPreviousQty=0;
										var tempAmount=0;
											for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
												let	array_element = prevAreaQuantity[ind].split("##");
												var noOfWorker=parseFloat(array_element[0]);
												var noOfHrs=parseFloat(array_element[1]);
												var rate=parseFloat(array_element[2]);
												//	var c=noOfWorker+noOfHrs;
												 tempPreviousQty += parseFloat(((noOfWorker* noOfHrs)));
												 tempAmount+= parseFloat(((noOfWorker* noOfHrs*rate)));
												/*var num =tempPreviousQty;
												var n = num.toFixed(2);		*/									
												previousQty=tempPreviousQty;
												previousNMRBillAmount=tempAmount;
												//IMP
												/*previousNMRBillAmount+=parseFloat(n)*array_element[2];				
												if(ind<prevAreaQuantity.length-1&&array_element[0]!=0){
														printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+"),";	
												}else if(array_element[0]!=0){
														printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+")";
												}*/
											}
										} else {
											debugger;
											prevAreaQuantity = value.PREVQTY.split("##");
											previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]));
											previousQty=previousQty.toFixed(3);
											previousNMRBillAmount=parseFloat(previousQty)*prevAreaQuantity[2];
											//	printPreviousRateAndQty+="(Qty = "+previousQty+" , Rate = "+prevAreaQuantity[2]+")";
										}
											previousNMRBillAmount=((previousNMRBillAmount)/8).toFixed(2);
								} catch (e) {
									console.log(e);
								}
								try {
									previousQty=(previousQty/8).toFixed(3);
								} catch (e) {
									console.log(e);
								}	
									strAbstractTableData += '<tr>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><h5><strong>'+ serialNumber+ '</strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+ value.WO_WORK_DESC_ID+ '"><input type="hidden" name="majorheadId" id="majorheadId" value="'+ value.WO_MAJORHEAD_ID+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID+ '"></td>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong >'+ value.WO_WORK_DESCRIPTION+' </strong><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+ value.WO_MEASURMENT_ID+ '"></td>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong  id="'+ WO_WORK_DESCRIPTION+ 'TotalQty">0</strong></td>';
				                    strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong id="'+WO_WORK_DESCRIPTION+ 'AvgRate"></strong></td>';
				                    strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong>'+ value.WO_MEASURMEN_NAME+ '</strong></td>';
				                    strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong class="CBQtyClass" name="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" id="CB'+ WO_WORK_DESCRIPTION+ 'Qty" >'+ previousQty+ '</strong></td>';
				                    strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong class="CBAmountClass" name="CB'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="CB'+ WO_WORK_DESCRIPTION+ 'Amount" >'+ inrFormat(previousNMRBillAmount)+ '</strong></td>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong class="PCQtyClass" name="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty" id="PC'+ WO_WORK_DESCRIPTION+ 'Qty" >'+ previousQty+ '</strong></td>';
									strAbstractTableData += ' <td style="text-align:center;padding:5px;"><strong class="PCAmountClass" name="PC'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="PC'+ WO_WORK_DESCRIPTION+ 'Amount" >'+ inrFormat(previousNMRBillAmount)+ '</strong></td>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong name="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Qty"  id="'+ WO_WORK_DESCRIPTION+ 'Qty" class="CCQtyClass">0.000</strong></td>';
									strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong name="'+ value.WO_MINORHEAD_ID+ WO_WORK_DESCRIPTION+ 'Amount" id="'+ WO_WORK_DESCRIPTION+ 'Amount" class="CCAmountClass">0.00</strong></td>';
									strAbstractTableData += '</tr>';
									serialNumber++;
									$("#NMRAbstractTableData").append(strAbstractTableData);
									strAbstractTableData="";
							}/*else{*/
							
							debugger;
							
								/*if(value.WO_WORK_DESC_ID!=workDescriptionid){
									
									workDescriptionid=value.WO_WORK_DESC_ID;
									duplicateWorkAreaId=1;	
								}else{
									var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
									$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(2));
									duplicateWorkAreaId=duplicateWorkAreaId+1;
								}*/
							const isThis_Str_InArray2 = value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+"@@"+value.WO_ROW_CODE;
							const isInArray2 = minorHeadNames2.includes(isThis_Str_InArray2);
							if (isInArray2 == false) {
								debugger;
								minorHeadNames2.push(isThis_Str_InArray2);
								var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
								$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(3));
							}
							/*}*/
					});
					strAbstractTableData="";
					strAbstractTableData += '<tr style="background-color: #ccc;">';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center">Total(A)</td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><span id="TotalACBQty"></span></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><span id="TotalACBAmt"></span></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><span id="TotalAPCQty"></span></td>';
					strAbstractTableData += '<td class="text-center"><span id="TotalAPCAmt"></span></td>';
					strAbstractTableData += '<td class="text-center"><span id="TotalACCQty"></span></td>';
					strAbstractTableData += '<td class="text-center"><span id="TotalACCAmt"></span><input type="hidden" id="CertifiedAmount" name="CertifiedAmount"></td>';
					strAbstractTableData += '</tr>';

					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '</tr>';

					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '</tr>';
					
					
					var hiddenpreviousPettyExpences=$("#hiddenpreviousPettyExpences").val();
					var hiddenpreviousotherAmount=$("#hiddenpreviousotherAmount").val();
					var hiddenRecoveryAmount=$("#hiddenRecoveryAmount").val();
					
				    strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center"><span>Petty Expenses</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt CumilativeCAmount" id="pettyExpensesCumulative"  style="border:none;text-align:center;" value="'+hiddenpreviousPettyExpences+'" readonly/></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class=""><div class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt PreviousCAmnt" id="pettyExpensesPrevCerti"  style="border:none;text-align:center;" value="'+hiddenpreviousPettyExpences+'" readonly/></div></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center" style="padding: 0px 5px;"><input type="text"  id="pettyExpensesCurrentCerti" name="pettyExpensesCurrentCerti" class="form-control text-center raDeductionAmt currentCAmnt" autocomplete="off" onkeyup="pettyAndOtherChange(this.id)" onkeypress="return isNumberCheck(this, event)" readonly></td>';
					strAbstractTableData += '</tr>';
					
					
					
					
					//'+(serialNumber+1) + '
					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center"><span>Others</span></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><input type="text"  name="otherAmtCumulative" class="CcAmnt CumilativeCAmount" id="otherAmtCumulative"  style="border:none;text-align:center;" value="'+hiddenpreviousotherAmount+'" readonly/></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt PreviousCAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="'+hiddenpreviousotherAmount+'" readonly/></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center" style="padding: 0px 5px;"><input type="text"  id="other" name="other" class="form-control text-center raDeductionAmt currentCAmnt" onkeyup="pettyAndOtherChange(this.id)" autocomplete="off" onkeypress="return isNumberCheck(this, event)" readonly></td>';
					strAbstractTableData += '</tr>';

					//'+ (serialNumber+2 )+ '
					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td> 	';
					strAbstractTableData += '<td class="text-center"><span>Recovery</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><input type="text"  name="cumulativeRecovery" class="CcAmnt CumilativeCAmount" id="cumulativeRecovery"  style="border:none;text-align:center;" value="'+hiddenRecoveryAmount+'" readonly/></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"><input type="text"  name="previousRecovery" class="PcAmnt PreviousCAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="'+hiddenRecoveryAmount+'" readonly/></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"><input type="text"  id="currentRecoveryAmount" name="currentRecoveryAmount" class="raDeductionAmt currentCAmnt" value=""   style="border:none;width: 100%;text-align: center;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount"> <br><a class="" href="#" id="recoveryLink" onclick="recoveryOpenModal()" >Click Here</a></td>';
					strAbstractTableData += '</tr>';

					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '</tr>';
					
					
					strAbstractTableData += '<tr style="background-color: #ccc;">';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center">Total Amount(B)</td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"><input type="text" name="totalAmtCumulative" id="totalAmtCumulative" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="" readonly=""></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"><input type="text" name="totalAmtPrevCerti" id="totalAmtPrevCerti" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="" readonly=""></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"><input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc" class="addFractionAndMakeInrFormat" style="border:none;text-align:center;background-color: #ccc;" value="6" readonly=""></td>';
					strAbstractTableData += '</tr>';
					
					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '<td class="text-center"></td>';
					strAbstractTableData += '</tr>';
										

					strAbstractTableData += '<tr style="background-color: #ccc;">';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><h5><strong></strong></h5></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong>Net Payable Amount (A - B) </strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong></strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong></strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong></strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong  id="CBTotalQty">0</strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong  id="CBToatalAmount">0</strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong  id="PCTotalQty">0</strong></td>';
					strAbstractTableData += ' <td style="text-align:center;padding:5px;"><strong  id="PCToatalAmount" >0</strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"><strong id="CCTotalQty">0</strong></td>';
					strAbstractTableData += '<td style="text-align:center;padding:5px;"> <input type="hidden" name="paybleAmount" id="paybleAmount" > <strong id="CCToatalAmount">0</strong></td>';
					strAbstractTableData += '</tr>';

					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td  style="text-align:center;padding:13px;" colspan="5">Amount in Words</td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;" colspan="6"><span id="finalTotalWorkOrderAmountInWords"></span><span style="display:none;" id="printfinalTotalWorkOrderAmountInWords"></span></td>';
					strAbstractTableData += '</tr>';

					strAbstractTableData += '<tr>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;" colspan="5"></td>';
					strAbstractTableData += '<td style="text-align:center;padding:13px;" colspan="6"><strong><a href="javascript:void(0);" onclick="openAbstract()" id="NmrAbstract">NMR Details</a></strong></td>';
					strAbstractTableData += '</tr>';
					
					$("#NMRAbstractTableData").append(strAbstractTableData);
					pettyAndOtherChange();
					NMRBillRecovery();					
					CalculateTotalAbstract();
					debugger;
					$("#previousRecovery").val(inrFormat($("#previousRecovery").val()));
					$("#cumulativeRecovery").val(inrFormat($("#cumulativeRecovery").val()));
					$("#otherAmtPrevCerti").val(inrFormat($("#otherAmtPrevCerti").val()));
					$("#pettyExpensesPrevCerti").val(inrFormat($("#pettyExpensesPrevCerti").val()));
					
					
					
					if (typeof (Storage) !== "undefined") {
						sessionStorage.setItem("NMRDATA", data);
						sessionStorage.setItem("workDescNames", workDescNames);
						sessionStorage.setItem("minorHeadNames", minorHeadNames);
						$("#noOfWDToIterate").val(noOfWorkDesc);
					} else {
						alert("Sorry, your browser does not support Web Storage...");
					};

					serialNumber = 1;
					strFirstRowOfTableHead += "<tr>";
					strSecondRowOfTableHead += "<tr>";

					// Table Head
					strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 16px;padding:5px;width:3%"rowspan="2">S.NO</th>';
					strFirstRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:6%;"rowspan="2">Date</th>';
					strFirstRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:10%;"rowspan="2">Description</th>';
					strFirstRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:10%;"rowspan="2">Manual Description</th>';
					strFirstRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:10%;"colspan="2" rowspan="1">Duration</th>';
					strFirstRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;"rowspan="2">No. of hours</th>';

					// Table Head
					strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;">From</th>';
					strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;">To</th>';

					// Table Body
					strNMRFirstRowtbodyData += '<tr id="tablerow1" class="tablerowcls">';
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;">1</td>';
					strNMRFirstRowtbodyData += "<td style='text-align:center;padding:5px;'><div class=''><input type='text' class='form-control Date' id='Date1' name='WorkDate1' onchange='NMRDescriptionCompare(\"date\","+serialNumber+")' onkeydown='return false' placeholder='dd-mm-yy' style='width:67%;margin-left:2%;float:left;'><label class='input-group-addon btn datepicker-paymentreq-fromdate' id='datepickerIcon' onclick='openCalenderInNMRDetails("+ serialNumber+ ")' for='fromDate'><span class='fa fa-calendar'></span></label></div><input type='hidden' id='Datehidden1'> </td> ";// onchange='NMRDateCompare("+serialNumber+")'
					//strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control Date" value="" id="Date1" name="WorkDate1"  onchange="NMRDescriptionCompare(\'date\','+ serialNumber+ ')" onkeydown="return false" placeholder="dd-mm-yy"/><input type="hidden" value="" id="Datehidden1"  onchange="NMRDescriptionCompare('+ serialNumber + ')"></td>';
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;">';
					strNMRFirstRowtbodyData += '<input type="hidden" id="Descriptionhidden1"><select class="form-control Description" id="Description1"  name="Description1"  onchange="NMRDescriptionCompare(\'description\','+ serialNumber + ')" style="padding:0px">';// style="width:200px;"
					strNMRFirstRowtbodyData += '<option value="">--select--</option>';

					strNMRSecondRowtbodyData += ' <tr>';
					strNMRSecondRowtbodyData += '<td style="text-align:right;padding:5px;font-weight: 1000;font-size: 14px;" colspan="'+ (7 + noOfWorkDesc+noOfWorkDesc)	+ '">Total Number of Hours</td>';

					strNMRThirdRowtbodyData += ' <tr>';
					strNMRThirdRowtbodyData += '<td style="text-align:right;padding:5px;font-weight: 1000;font-size: 14px;" colspan="'+ (7 + noOfWorkDesc+noOfWorkDesc) + '">Total Labor</td>';

					var lengthOfRows = 0;
					lengthOfRows = data.length;
					
					if (typeof (Storage) !== "undefined") {
						// Store
						sessionStorage.setItem("lengthOfRows", lengthOfRows);
					} else {
						alert("Sorry, your browser does not support Web Storage...");
					};

					var strTempFirstRowData = "";
					var strTempSecondRowData = "";
					// dynamic Headers for table
					var tempMinorHeadName = "";
					var tempWORowCode = "";
					var workDescTableHeadNames = new Array();
				$.each(data, function(key, value) {
					// this variable's for Table Head
					const	isThis_Str_InArray = value.WO_WORK_DESCRIPTION+ "@@" + value.WO_WORK_DESC_ID;
					const	isInArray = workDescTableHeadNames.includes(isThis_Str_InArray);
					if (isInArray == false) {
								debugger;
									workDescTableHeadNames.push(isThis_Str_InArray);
									//<br><span style="float:left"> QTY</span><span>|</span> <span style="float:right">RATE</span>
									//here if you want to remove any column decrease the colspan by 1
									strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 16px;padding:5px;width:4%;" rowspan="1"  colspan="2">'+ value.WO_WORK_DESCRIPTION+ '</th>';
									//onlyWorkDeskNames+= ' <th style="text-align: center;font-size: 16px;padding:5px;width:4%;" rowspan="2"  colspan="2">'+ value.WO_WORK_DESCRIPTION+ ' </th>';
									//strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 16px;padding:5px;width:4%;"rowspan="2" >Rate</th>';
									//strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Total Qty</th>';
									//strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Prev Qty</th>';
									strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Qty</th>';
									strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Rate</th>';

									//strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >'+ value.WO_WORK_DESCRIPTION+ '(hrs)</th>';
										// this variable for Table Data
										let WO_WORK_DESCRIPTION = "";
										try {
											WO_WORK_DESCRIPTION = value.WO_WORK_DESCRIPTION.trim();
										} catch (e) {console.log(e);}
										
										debugger
										
										WO_WORK_DESCRIPTION = value.WO_WORK_DESCRIPTION.replace(/ /g, '');
										try {
											WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.trim();
											var regExpr = /[^a-zA-Z0-9-. ]/g;
											WO_WORK_DESCRIPTION = WO_WORK_DESCRIPTION.replace(regExpr, "");
										} catch (e) {console.log(e);}
										
										//strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';
										//strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';
										
										strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control validatefield" value="" name="'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" id="'+ (WO_WORK_DESCRIPTION + serialNumber)+ '"  onkeypress="return isNumberCheck(this, event)"  onkeyup="calculateData(this.className, this.id, this.value, '+ serialNumber+ ')" readonly/>';
										strTempFirstRowData+='<input type="hidden" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/><input type="hidden" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/><input type="hidden" class="form-control prevWDTotalAmount'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalAmount'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/>';
											
										strTempFirstRowData += '<input type="hidden" name="rate1" id="" value="'+ (value.RATE)+ '"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+ value.WO_MINORHEAD_ID	+ '"><input type="hidden" name="workDescId'+ serialNumber+ '" id="workDescId'+ serialNumber+ '" value="'+ value.WO_WORK_DESC_ID	+ '"></td>';
										strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control WDAmount'+ (WO_WORK_DESCRIPTION )+ '" id="WDRate'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value="0"  readonly/></td>';
										
										strTempSecondRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control  '+ WO_WORK_DESCRIPTION+ '" value="0"  name="'+serialNumber+ (WO_WORK_DESCRIPTION+ serialNumber + serialNumber)+ '" id="'+serialNumber+ (WO_WORK_DESCRIPTION+ serialNumber + serialNumber)	+ '" readonly/><input type="hidden" class="RowWDAmount'+WO_WORK_DESCRIPTION+'" id="RowAmount'+serialNumber+ (WO_WORK_DESCRIPTION+ serialNumber + serialNumber)+ '" ></td>';
										strNMRSecondRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control" id="'+ WO_WORK_DESCRIPTION+ 'finalHours" value="0.00" readonly><input type="hidden" id="FinalWDAmount'+ WO_WORK_DESCRIPTION+ '"><input type="hidden" id="sumOfWDAmount'+ WO_WORK_DESCRIPTION+ '"></td>';
										strNMRThirdRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control" id="'+ WO_WORK_DESCRIPTION+ 'Days" value="0.000" readonly></td>';
									}
								});
				
				
				workDescTableHeadNames = new Array();
				$.each(data, function(key, value) {
					// this variable's for Table Head
					const	isThis_Str_InArray = value.WO_WORK_DESCRIPTION+ "@@" + value.WO_WORK_DESC_ID;
					const	isInArray = workDescTableHeadNames.includes(isThis_Str_InArray);
					if (isInArray == false) {
						debugger;
						workDescTableHeadNames.push(isThis_Str_InArray);
						strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >'+ value.WO_WORK_DESCRIPTION+ '(hrs)</th>';
					}
				});
				
				
				strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 14px;padding:5px;width:15%;" colspan="'+ noOfWorkDesc + '" rowspan="1">Total</th>';
				strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 14px;padding:5px;width:10%"rowspan="2">Remarks</th>';
				strFirstRowOfTableHead += ' <th style="font-size: 14px;padding: 0px 40px 0px 10px;width:5%"rowspan="2">Actions</th>';

					strFirstRowOfTableHead += "</tr>";
					strSecondRowOfTableHead += '</tr>';

					$("#NMRHeadData").html(strFirstRowOfTableHead + strSecondRowOfTableHead);
				
					var tempRowData = "";
					for (var index = 0; index < minorHeadNames.length; index++) {
					debugger;
						let str = minorHeadNames[index].split("@@");
						var regExpr = /[^a-zA-Z0-9-. ]/g;
						if (tempMinorHeadName != (str[0]+str[4])) {
							tempMinorHeadName = (str[0]+str[4]);
							tempWORowCode=str[4];
							strNMRFirstRowtbodyData += '<option value="'+ str[0] + '@@' + str[1] + '@@' + str[4] + '@@' + str[3]+'">' + str[0]+ '('+str[4]+')</option>';
						}
						tempRowData += "<input type='hidden' name='minorWDId1' value='"	+ str[1] + "@@" + str[2] + '@@' + str[4] +  '@@' + str[3] + "'>";
					}

					// Table Body
					strNMRFirstRowtbodyData += '</select>';
					strNMRFirstRowtbodyData += '<i class="fa fa-question-circle  red-tooltip " data-toggle="tooltip" data-placement="auto" id="WDDwtailsTable1" title=""></i><div id="showTable1" style="display:none;position:relative;z-index:9999;"></div></td>';
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control ManuvalDescription" value=""  placeholder="manual Description" id="ManuvalDescription1" name="ManuvalDescription1"/>';
					strNMRFirstRowtbodyData += tempRowData + "</td>";
					strNMRFirstRowtbodyData += ' <td style="text-align:center;padding:5px;"><input type="text" class="form-control From" value="9.00" id="From1"  name="FromTime1" onkeypress="return HoursNumberCheck(this, event)" onfocusout="calHours(1)" onkeyup="fromToDurationKeyUp(1)"/><input type="hidden" value="9.00" id="FromHidden1"/></td>'; // style="width:
																																																// 60px;"
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control To" value="18.00" id="To1"  name="toTime1" onkeypress="return HoursNumberCheck(this, event)" onfocusout="calHours(1)" onkeyup="fromToDurationKeyUp(1)"/><input type="hidden" value="9.00" id="ToHidden1"/></td>'; // style="width:
																																														// 60px;"
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control Noofhours" value="9" id="Noofhours1"  name="Noofhours1" onkeypress="return HoursNumberCheck(this, event)" onkeyup="NoOFHoursChange('+ serialNumber+')"/></td>';

					strNMRFirstRowtbodyData += strTempFirstRowData;
					strNMRFirstRowtbodyData += strTempSecondRowData;

					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control" value="" id="Remarks"  name="Remarks1" /></td>';
					strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><button onclick="appendRow(1)"  style="float:left;"  id="AddNMRBtn1" class="btnaction"  type="button"><i class="fa fa-plus"></i></button>';
					strNMRFirstRowtbodyData += '<button style="display:none;float:left;" id="DeleteNMRBtn1" onclick="DeleteNMRBillRow(1)" id="" class="btnaction"  type="button"><i class="fa fa-trash"></i></button></td>';
					strNMRFirstRowtbodyData += '</tr>';

					
					strNMRSecondRowtbodyData += '<td style="text-align:center;padding:5px;"></td>';
					strNMRSecondRowtbodyData += '</tr>';

					
					strNMRThirdRowtbodyData += '<td style="text-align:center;padding:5px;"></td>';
					strNMRThirdRowtbodyData += '</tr>';

					$("#NMRBillstableIdfirstRow").html(strNMRFirstRowtbodyData);
					$(function() {
					    $("#Description1").combobox();    	        
					}); 
					$("#NMRBillstableId").css("width", (2000+300*workDescNames.length));
					$("#NMRBillstableIdfirstRow").append(strNMRSecondRowtbodyData);
					$("#NMRBillstableIdfirstRow").append(strNMRThirdRowtbodyData);
					$("#Date1").datepicker({dateFormat: 'dd-mm-yy', maxDate:0 });
					$(".overlay_ims").hide();
					$(".loader-ims").hide();
					$(".From, .To, .Noofhours").bind('paste', function (e) {
						e.preventDefault();
					});
				}
			});
}

function recoveryOpenModal() {
	var TotalACCAmt = parseFloat($("#TotalACCAmt").text());
	if (TotalACCAmt == '0') {
		alert("Please enter abstract.");
		$("#NMRBillTableModal").modal();
		return false;
	}
	$("#recoveryLink").attr("data-toggle", "modal");
	$("#recoveryLink").attr("data-target", "#modal-recovery-click");

}

var sumOfRecovery=0;
var prevRecoveryAmount=0;
function calculateRecoveryAmount(recoveryValue, childId) {

	var i = parseInt($("#rowsToIterate").val());
	sumOfRecovery = 0;
	if (recoveryValue.length == 0) {
		recoveryValue = 0;
		// return false;
	}
	var child_product_id = $("#childId" + childId).val();
	if (!isNum(recoveryValue)) {// &&(recoveryValue!="0"||recoveryValue.length==0)
		alert('Please Enter only digits');
		$("#" + child_product_id + "currentAmount").val('0');
		return false;
	}

	var totalAmount = $("#" + child_product_id + "totalRecoveryAmount").text();
	totalAmount = parseFloat(totalAmount);
	var recovery_amount = parseFloat($("#" + child_product_id + "currentAmount").val() == "" ? 0 : $("#" + child_product_id + "currentAmount").val());

	prevRecoveryAmount = 0;
	for (var i1 = 1; i1 <= i; i1++) {
		var temp_child_product_id = $("#childId" + i1).val();
		var recovery_amount = parseFloat($("#" + temp_child_product_id + "currentAmount").val() == "" ? 0: $("#" + temp_child_product_id + "currentAmount").val());
		
		var previous = $("#" + temp_child_product_id + "previous").text();// getting previous value using child  id
		previous = parseFloat(previous);
		sumOfRecovery += (recovery_amount);
		prevRecoveryAmount += previous + recovery_amount;
		$("#" + temp_child_product_id + "cumulative").text((previous + recovery_amount).toFixed(2));
	}
	sumOfRecovery = sumOfRecovery.toFixed(2);
	var cumulative = $("#" + child_product_id + "cumulative").text();// getting previous cumulative  value  using child id
	cumulative = parseFloat(cumulative);
	
	$("#sumOfCurrentAmount").text(sumOfRecovery);
	$("#sumOfRecoveryCumulative").text(prevRecoveryAmount.toFixed(2));
}

function recoverySubmitButton() {debugger;
	$("#currentRecoveryAmount").val(sumOfRecovery);
	$("#recoverycurrentAmount").val(sumOfRecovery);
	$("#sumOfCurrentAmount").text(sumOfRecovery);

	var previouscumilativeRecovery = $("#previousRecovery").val() == "" ? 0: parseFloat($("#previousRecovery").val());
	$("#cumulativeRecovery").val(inrFormat(prevRecoveryAmount.toFixed(2)));
	$("#previousRecovery").val(inrFormat($("#sumOfPrevRecovery").text()));
	NMRCreateTableSubmit();
	pettyAndOtherChange("currentRecoveryAmount");
	$("#recoverySubmitBtnID").attr("data-dismiss", "modal");
}

function NMRBillRecovery() {

	var ContractorId = $("#ContractorId").val();
	var ContractorName = $("#ContractorName").val();
	var workOrderNo = $("#workOrderNo").val();
	var approvePage = $("#approvePage").val();
	var billType = $("#billType").val();
	var tempBillNo = $("#tempBillNo").val();
	var site_id = $("#site_id").val();	
	var str = "";
	var str1 = "";
	var htmlData = "";
	$.ajax({
				url : "loadRecoveryAreaDetails.spring",
				type : "GET",
				data : {
					contractorId : ContractorId,
					workOrderNo : workOrderNo,
					approvePage : approvePage,
					billType : billType,
					tempBillNo : tempBillNo,
					site_id : site_id
				},
				success : function(data) {
					var i = 0;
					var sumOfRecovery = 0;
					var sumOfCumulative = 0;
					var sumOfPrev = 0;
					var childProductName = "";
					var temprecoveryAmount = "";
					var tempcurrentrecovery = 0;
					var temptotalRecoveryAmount = 0;
					var temptotalRecoveryQTY = 0;
					$("#RecoveryStatement").html("");
					$("#ViewRecovery").html("");
					$.each(data, function(key, value) {

				    if (value.ISSUED_QTY == "0") {
							return;
					}

					var amount_per_unit_before_taxes = parseFloat(value.AMOUNT_PER_UNIT_BEFORE_TAXES).toFixed(2);
					var issuedQty = parseFloat(value.ISSUED_QTY);
					var currentRecoveryAmount = 0;

					var recoveryAmount = value.RECOVERY_AMOUNT == null ? 0: parseFloat(value.RECOVERY_AMOUNT);
					temprecoveryAmount = value.RECOVERY_AMOUNT;
					var recoveryCurrentAmount = parseFloat(value.TOTAL_AMOUNT)- recoveryAmount;
					
					var totalRecoveryAmount = value.TOTAL_AMOUNT == null ? "": parseFloat(value.TOTAL_AMOUNT);

					temptotalRecoveryAmount += totalRecoveryAmount;
										// sum of total quantity
					temptotalRecoveryQTY += issuedQty;

					let tempCumulative = (recoveryAmount + recoveryCurrentAmount);

					var child_product_id = value.CHILD_PRODUCT_ID;
				if (childProductName != value.CHILD_PROD_NAME && value.CHILD_PROD_NAME != undefined) {
										
					childProductName = value.CHILD_PROD_NAME;
					i++;
					str = "<tr> <td > "+ i+ "<input type='hidden' id='childId"+ i+ "' value='"+ child_product_id+ "'> </td><td><strong>"+ value.CHILD_PROD_NAME+ "</strong></td><td id='"+ child_product_id+ "issuedQty'  class='totalQtyConsumed'><span>"+ issuedQty.toFixed(2)+ "</span></td><td id='"+ child_product_id+ "totalRecoveryAmount'  class='totalAmountOfRecovery'><span>"+ totalRecoveryAmount.toFixed(2)+ "</span></td><td  id='"+ child_product_id+ "mesurment_name'>"+ value.MESURMENT_NAME+ " <input type='hidden'  id='"+ child_product_id+ "mesurment_id' value='"+ value.UNITS_OF_MEASUREMENT+ "'></td><td id='"+ child_product_id+ "cumulative'>"+ recoveryAmount.toFixed(2)+ "</td>"+ "<td id='"+ child_product_id+ "previous'>"+ recoveryAmount.toFixed(2)+ "</td><td> <input type='text' class='form-control' id='"+ child_product_id+ "currentAmount' name='recoverycurrentAmount' value='0.00' onkeypress='return isNumberCheck(this, event)' onkeyup='calculateRecoveryAmount(this.value,"+ i + ");'>  </td></tr>";
					$("#RecoveryStatement").append(str);

					} else {
						var tempIssuedQty = $("#" + child_product_id+ "issuedQty").text()==""?0:parseFloat($("#" + child_product_id+ "issuedQty").text());
						tempIssuedQty = tempIssuedQty;
						var before_taxes = $("#"+ child_product_id+ "totalRecoveryAmount").text();
						before_taxes = parseFloat(before_taxes);
						var currentAmount = parseFloat($("#" + child_product_id+ "currentAmount").val());
						$("#" + child_product_id+ "issuedQty").text((tempIssuedQty+ issuedQty).toFixed(2));
						$("#"+ child_product_id+ "totalRecoveryAmount").text((before_taxes+ totalRecoveryAmount).toFixed(2));

						}
					});
					var sumOfCurrRecovery = 0;
					for (var i1 = 1; i1 <= i; i1++) {
						var child_product_id = $("#childId" + i1).val();
						var recovery_amount = parseFloat($("#" + child_product_id + "currentAmount").val() == "" ? 0: $("#" + child_product_id + "currentAmount").val());
						var totalRecoveryAmount = $("#" + child_product_id + "totalRecoveryAmount").text();// getting previous cumulative value
										// using child id
						totalRecoveryAmount = parseFloat(totalRecoveryAmount);

						var cumulative = $("#" + child_product_id + "cumulative").text();// getting	 previous cumulative value using child id
						cumulative = parseFloat(cumulative);
						var previous = $("#" + child_product_id + "previous").text();// getting previous value using child id
						previous = parseFloat(previous);
						sumOfCumulative += +cumulative;
						sumOfPrev += +previous;
						sumOfRecovery += (recovery_amount);
						sumOfCurrRecovery += (totalRecoveryAmount - previous);
					}
					$("#currentRecoveryAmount").val(sumOfRecovery.toFixed(2));
					$("#showCurrentRecoveryAmount").html(sumOfCurrRecovery.toFixed(2));
					$("#recoverycurrentAmount").val(sumOfRecovery.toFixed(2));
					if (billType == "ADV") {
						$("#recoverycurrentAmount").val(0);
					}
					htmlData += "<input type='hidden' name='rowsToIterate' id='rowsToIterate' value='"+ i + "'>";

					// this is for final amount
					var temp = " <tr><td></td><td class='text-right'><h4><strong>Total Amount</strong></h4></td><td  id='totalWORecoveryQTY'><span>"+ (temptotalRecoveryQTY.toFixed(2)) + "</span></td><td id='totalWORecoveryAmount'>"+ (temptotalRecoveryAmount.toFixed(2)) + "</td><td ></td><td><strong  id='sumOfRecoveryCumulative'>"+ (sumOfPrev.toFixed(2)) + "</strong></td><td ><strong id='sumOfPrevRecovery'>" + (sumOfPrev.toFixed(2))+ "</strong></td><td ><strong id='sumOfCurrentAmount'>"+ sumOfRecovery.toFixed(2) + "</strong></td></tr>";

					$("#previousRecovery").val(inrFormat(sumOfPrev.toFixed(2)));
					$("#cumulativeRecovery").val(inrFormat(sumOfPrev.toFixed(2)));
					$("#RecoveryStatement").append(temp);
					
					$("#RecoveryStatement").append(htmlData);
					CalculateTotalAbstract();
					
				}
			});

}

function loadNMRBillNo() {
	var ContractorId = $("#ContractorId").val();
	var ContractorName = $("#ContractorName").val();
	var workOrderNo = $("#workOrderNo").val();
	var contractorGSTIN=$("#contractorGSTIN").val();
	var approvePage = $("#approvePage").val();
	var tempBillNo = $("#tempBillNo").val();
	var billType = $("#billType").val();
	$.ajax({
		url : "loadAdvRAPermanentBillNo.spring",
		type : "GET",
		data : {
			ContractorId : ContractorId,
			workOrderNo : workOrderNo,
			billType : billType,
			approvePage : approvePage,
			contractorGSTIN:contractorGSTIN
		},
		success : function(data) {			
			var isContainsSpecialChar = data.search("@@");
			if (isContainsSpecialChar >= 0) {
	            alert("Please check the previous running account bills.");
				$('#formSubmitBtn').attr('disabled', true);
				$("#isPrevBillIsRunning").val("true");
			} else {
				var billNumber=data.split("_")[0];
				var invoiceNumber=data.split("_")[1];
				$("#printnmrBillNo").text(billNumber);
				$("#NMRBillNo").val(billNumber);
				$("#actualBillNumber").val(billNumber);
			
				$("#billInvoiceNo").val(invoiceNumber);
			 	$("#actualBillInvoiceNo").val(invoiceNumber);
			 	$("#printnmrInvoiceBillNo").text(invoiceNumber);
			 	
			 	$('#formSubmitBtn').attr('disabled', false);
				$("#isPrevBillIsRunning").val("false");
			}
		}

	});
}
$(document).ready(function() {

	$("#workOrderNo").on("change", function() {
		$(".overlay_ims").show();
		$(".loader-ims").show();
		if ($(this).val() == "") {
			$(".overlay_ims").hide();
			$(".loader-ims").hide();
			return false;
		}
    debugger;
		var num=$(this).val().split("$")[2];
		 $("#totalAmtToPay").val(num);
		 $("#printtotalAmtToPay").text(inrFormat(parseFloat(num).toFixed(2)));
		 loadNMRBillNo();
		 loadNMRCompletedBillData();
		 loadNMRBillData();
		
	});

});

function calLedgerTotalValues(){
	var finalBillAmount=0;
	var finalAmountPaid=0;
	var finalCumulativeAmount=0;
	var finalRecoveryAmount=0;
	
	$(".BillAmountCls").each(function(){
		finalBillAmount+=parseFloat($(this).text()==''?0:$(this).text().replace(/,/g,''));
	})
	$(".AmountPaidCls").each(function(){
		finalAmountPaid+=parseFloat($(this).text()==''?0:$(this).text().replace(/,/g,''));
	})
	$(".CumulativeAmountCls").each(function(){
		finalCumulativeAmount+=parseFloat($(this).text()==''?0:$(this).text().replace(/,/g,''));
	})
	$(".RecoveryAmountCls").each(function(){
		finalRecoveryAmount+=parseFloat($(this).text()==''?0:$(this).text().replace(/,/g,''));
	})

	$("#TotalBillAmount").html(inrFormat(finalBillAmount.toFixed(2)));
	$("#TotalAmountPaid").html(inrFormat(finalAmountPaid.toFixed(2)));
	$("#TotalRecoveryAmount").html(inrFormat(finalRecoveryAmount.toFixed(2)));
	//$("#TotalCumulativeAmount").html(inrFormat(finalCumulativeAmount.toFixed(2)));
}

function getWorkDesc(serialNo){
	var workDescNames;
	var minorHeadNames;
	if (typeof(Storage) !== "undefined") {
	    // Store   Retrieve
	  data=sessionStorage.getItem("NMRDATA");
	  lengthOfRows=sessionStorage.getItem("lengthOfRows");
	  workDescNames=sessionStorage.getItem("workDescNames");
	  minorHeadNames=sessionStorage.getItem("minorHeadNames");
	  var tempworkDescNames=workDescNames.split(",");		
		for ( var index = 0; index < tempworkDescNames.length; index++) {
			var str=tempworkDescNames[index].split("@@");
			let WO_WORK_DESCRIPTION=str[0];
			$("#"+WO_WORK_DESCRIPTION+(serialNo)).val("0");
			$("#"+serialNo+WO_WORK_DESCRIPTION+serialNo+serialNo).val("0");
		};
	
	} else {
	   alert("Sorry, your browser does not support Web Storage...");
	};

}

