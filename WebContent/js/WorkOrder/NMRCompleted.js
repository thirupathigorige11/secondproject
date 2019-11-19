	$("#billLedger").on("click",function(){
		
			var url="woBillLedger.spring";
			$('#approveNMRBillFormId').attr('target', '_blank');
		    document.getElementById("approveNMRBillFormId").action =url;
		    document.getElementById("approveNMRBillFormId").method = "POST";
		    document.getElementById("approveNMRBillFormId").submit();
		});

//this method for loading ledger
function loadNMRCompletedBillData() {
	var ContractorId = $("#ContractorId").val();
	var ContractorName = $("#ContractorName").val();
	var workOrderNo = $("#workOrderNo").val();
	var approvePage = $("#approvePage").val();
	var typeOfWork = "NMR";
	var billNo=$("#billNo").val();	
	var siteId=$("#site_id").val();
	var tempBillNo=parseInt($("#tempBillNo").val());
	var isBillLedger=$("#isThisBillLedger").val();
	var currentNMRBillAmount=0;
	var previousNMRBillAmount=0;
	var previousPaybleAmount=0;
	$.ajax({
			url : "loadCompletedNMRBillData.spring",
			type : "GET",
			data : {
				contractorId : ContractorId,
				workOrderNo : workOrderNo,
				approvePage : approvePage,
				siteId : siteId,
				typeOfWork : typeOfWork,
				billNo:billNo
			},
			success : function(data) {
				var billLedgerData = "";
				var	PETTY=0.00;
				var	OTHER=0.00;
				var RECOVERY=0.00;
				var holdPrevAmount=0;
				$.each(data,function(key, value) {
					 if(billNo!=value.BILL_ID&&value.TEMP_BILL_ID<tempBillNo){
						 previousNMRBillAmount=(parseFloat(previousNMRBillAmount)+parseFloat(value.CERTIFIED_AMOUNT));
						 previousPaybleAmount=(parseFloat(previousPaybleAmount)+parseFloat(value.PAYBLE_AMOUNT));
					 }
					 
						var printAdvAmount=0.00;
						var printSecDeposit=0.00;
						var printPettyExpences=0.00;
						var printOther=0.00;
						var printRecoveryAmt=0.00;
						
						if(value.DEDUCTION_AMOUNT1!=null){
							var typeOfDeduction=new Array();
							var typeOfDeductionVal=new Array();
							//type of deduction in coming in the array where is spliting by @@
							typeOfDeduction=value.TYPE_OF_DEDUCTION.split("@@");
							//type of DEDUCTION_AMOUNT in coming in the array where is spliting by @@
							typeOfDeductionVal=	value.DEDUCTION_AMOUNT1.split("@@")
							for (var i2 = 0; i2 < typeOfDeduction.length; i2++) {
								/*if(typeOfDeduction[i2]=="ADV"){
									deduction_amount+=+typeOfDeductionVal[i2];
									printAdvAmount=typeOfDeductionVal[i2];
								}else */
								if (typeOfDeduction[i2]=="OTHER"){
									OTHER+=+typeOfDeductionVal[i2]
									printOther=parseFloat(typeOfDeductionVal[i2]);
								}else if (typeOfDeduction[i2]=="PETTY"){
									PETTY+=+typeOfDeductionVal[i2]
									printPettyExpences=parseFloat(typeOfDeductionVal[i2]);
								}else if (typeOfDeduction[i2]=="RECOVERY"){
									RECOVERY+=+typeOfDeductionVal[i2]
									printRecoveryAmt=parseFloat(typeOfDeductionVal[i2]);
								}/*else if(typeOfDeduction[i2]=="SEC"){
									secDeposit+=+typeOfDeductionVal[i2];
									printSecDeposit=typeOfDeductionVal[i2];
								}*/
							}
					}
					//	holdPrevAmount=parseFloat(holdPrevAmount);
						
					billLedgerData += '<tr>';
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;width:10%;">'+value.ENTRY_DATE+'</td>';
					 if(isBillLedger!="true"){
						 billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><a target="_blank" style="color:#0000ff;" class="anchorblue" href="showWOCompltedBillsDetails.spring?BillNo='
								+ value.BILL_ID+ '&WorkOrderNo='+ workOrderNo.split("$")[0]+ '&billType='+ value.PAYMENT_TYPE+ '&site_id='+ value.SITE_ID+ '&isBillUpdatePage=false&status=true">'+ value.BILL_ID + '</a></td>';
					 }else{
						 billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;">'+ value.BILL_ID + '</td>';
					 }
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="BillAmountCls"><span>'+ inrFormat(parseFloat(value.CERTIFIED_AMOUNT).toFixed(2)) + '</span></td>';// CERTIFIED_AMOUNT
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
					billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;" class="CumulativeAmountCls"><span>'+inrFormat( (holdPrevAmount+parseFloat(value.PAYBLE_AMOUNT)).toFixed(2))+ '</span></td>	';
					billLedgerData += '</tr>';
					holdPrevAmount+=parseFloat(value.PAYBLE_AMOUNT);

				});
				var pettyExpensesCurrentCerti=$("#pettyExpensesCurrentCerti").val();
				var other=$("#other").val();
				var recoverycurrentAmount=parseFloat($("#recoverycurrentAmount").val()==""?0:$("#recoverycurrentAmount").val());
				
				$("#pettyExpensesPrevCerti").val(inrFormat((PETTY-pettyExpensesCurrentCerti).toFixed(2)));
				$("#otherAmtPrevCerti").val(inrFormat((OTHER-other).toFixed(2)));
				
				
			
				$("#sumOfPreviousRecovery").val(inrFormat((RECOVERY-recoverycurrentAmount).toFixed(2)));
				$("#previousRecovery").val(inrFormat((RECOVERY-recoverycurrentAmount).toFixed(2)));
				$("#cumulativeRecovery").val(inrFormat((RECOVERY).toFixed(2)));
				$("#sumOfCumulativeRecovery").val(RECOVERY);
				
				$("#hiddenpreviousPettyExpences").val((PETTY-pettyExpensesCurrentCerti).toFixed(2));
				$("#hiddenpreviousotherAmount").val((OTHER-other).toFixed(2));
				debugger;
				holdPrevAmount=holdPrevAmount.toFixed(2);
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
				billLedgerData += '<td style="padding:8px;text-align:center;border:1px solid #000;"><span  id="TotalCumulativeAmount">'+inrFormat(holdPrevAmount)+'</span></td>	';
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
				$("#appendBillDetails").html(billLedgerData);
				calLedgerTotalValues();
				if(data.length==0){
					$("#paymentLedgerTable").hide();
				    $("#appendNMRBillDetailsNoDataMsg").html("<h3 style='text-align:center;'>No payment has been initiated.</h3>");
				}else{
					 $("#paymentLedgerTable").show();
					    $("#paymentLedgerTable").show();	
					    $("#contractorRABill").show();
					    $("#sumadhuraLogoAndName").show();
					    $("#billLedgerOfBill").show();
					    $("#printshowRa").show();
				   $("#appendBillDetailsTotal").html("<div  style='font-size: 20px;margin: 10px 20px 0px 0px;'><div style='width:350px;'><div style='float:left;width:200px;'> Net Payable Amount</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'><span>"+$("#TotalAmountPaid").text()+"</span</div></div><div style='width:350px;'><div style='float:left;width:200px;'>Total Deduction</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'><span>"+inrFormat((RECOVERY+PETTY+OTHER))+"</span></div></div><div style='width:350px;'><div style='float:left;width:200px;'>Grand Total</div><div style='float:left;width:50px;'>:</div><div style='float:left;width:100px;text-align:right;'><span>"+$("#TotalBillAmount").text()+"</span></div></div></div>");
				}				
				
				$("#raPc").text(inrFormat(previousNMRBillAmount.toFixed(2)));
				$("#raTotalPc").text(previousNMRBillAmount.toFixed(2));
				$("#nmrPrevPaybleAmount").text(previousPaybleAmount.toFixed(2));
				$("#WoAMount").text(inrFormat(parseFloat($("#WoAMount").text()).toFixed(2)));
				$("#TotalPaidAmt1").val(inrFormat(previousPaybleAmount.toFixed(2)));
				$("#TotalPaidAmt").val(inrFormat(previousPaybleAmount.toFixed(2)));				
				$("#totalAmtPrevCerti").val(inrFormat(previousNMRBillAmount.toFixed(2)));				
				var currentRecoveryAmount=parseFloat($("#currentRecoveryAmount").text()==""?"0.00":$("#currentRecoveryAmount").text().replace(/,/g,''));			
				$("#raTotalCc").text(inrFormat((parseFloat($("#raTotalPc").text().replace(/,/g,''))+parseFloat($("#totalCc").text().replace(/,/g,''))).toFixed(2)));
				$("#raCc").text(inrFormat((parseFloat($("#raTotalPc").text().replace(/,/g,''))+parseFloat($("#totalCc").text().replace(/,/g,''))).toFixed(2)));
				$("#nmrCCPaybleAmount").text(inrFormat((parseFloat($("#finalAmt").text().replace(/,/g,''))+parseFloat($("#nmrPrevPaybleAmount").text().replace(/,/g,''))).toFixed(2)));
				$("#nmrPrevPaybleAmount").text("0.00");
				
				
				try{
				var pettyExpensesPrevCerti=$("#pettyExpensesPrevCerti").val()==""?0:parseFloat($("#pettyExpensesPrevCerti").val().replace(/,/g,''));
				var otherAmtPrevCerti=$("#otherAmtPrevCerti").val()==""?0:parseFloat($("#otherAmtPrevCerti").val().replace(/,/g,''));
				
				var pettyExpensesCurrentCerti=$("#pettyExpensesCurrentCerti").val()==""?0:parseFloat($("#pettyExpensesCurrentCerti").val().replace(/,/g,''));
				var other=$("#other").val()==""?0:parseFloat($("#other").val().replace(/,/g,''));
				
				$("#pettyExpensesCumulative").val((pettyExpensesPrevCerti+pettyExpensesCurrentCerti).toFixed(2));
				$("#otherAmtCumulative").val((otherAmtPrevCerti+other).toFixed(2));
				}catch(e){
					console.log(e);
				}

				
				var CurrentDed=0;
				
				$(".currentCAmnt").each(function(){
					CurrentDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
				});
				$("#totalAmtCurntDeduc").val(inrFormat(CurrentDed.toFixed(2)));
				
				var PrevDed=0;						
				$(".PreviousCAmnt").each(function(){
					PrevDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
				});
				$("#totalAmtPrevCerti").val(inrFormat(PrevDed.toFixed(2)));
				
				var CumilativeDed=0;
				$(".CumilativeCAmount").each(function(){
					CumilativeDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
				});
				$("#totalAmtCumulative").val(inrFormat(CumilativeDed.toFixed(2)));
				try{
				var CurrentCCFinalAmnt=parseFloat($("#totalCc").text()==''?0:$("#totalCc").text().replace(/,/g,''))-parseFloat($("#totalAmtCurntDeduc").val()==''?0:$("#totalAmtCurntDeduc").val().replace(/,/g,''));
				$("#finalAmt").val(CurrentCCFinalAmnt.toFixed(2));
				debugger;
				var PreviCCFinalAmnt=parseFloat($("#raTotalPc").text()==''?0:$("#raTotalPc").text().replace(/,/g,''))-parseFloat($("#totalAmtPrevCerti").val()==''?0:$("#totalAmtPrevCerti").val().replace(/,/g,''));
				$("#totalAmtPreviousCertified").val(PreviCCFinalAmnt.toFixed(2));
			//	$("#totalAmtPreviousCertified").val("0.00");
				
				var CumilativeCCFinalAmnt=parseFloat($("#raTotalCc").text()==''?0:$("#raTotalCc").text().replace(/,/g,''))-parseFloat($("#totalAmtCumulative").val()==''?0:$("#totalAmtCumulative").val().replace(/,/g,''));
				$("#totalAmtCumulativeCertified").val(CumilativeCCFinalAmnt.toFixed(2));
				
				$("#raAmountToPay").text(inrFormat(parseFloat($("#raAmountToPay").text()==""?0.00:$("#raAmountToPay").text()).toFixed(2)));
				//$("#raPc").text(inrFormat(parseFloat($("#raPc").text()).toFixed(2)));
				$("#totalCc").text(inrFormat(parseFloat($("#totalCc").text()==""?0.00:$("#totalCc").text()).toFixed(2)));
				$("#raTotalPc").text(inrFormat(parseFloat($("#raTotalPc").text()==""?0.00:$("#raTotalPc").text()).toFixed(2)));							
					
				$("#finalAmt").val(inrFormat($("#finalAmt").val()==""?0.00:$("#finalAmt").val()));
				$("#totalAmtPreviousCertified").val(inrFormat($("#totalAmtPreviousCertified").val()==""?0.00:$("#totalAmtPreviousCertified").val()));
				$("#totalAmtCumulativeCertified").val(inrFormat($("#totalAmtCumulativeCertified").val()==""?0:$("#totalAmtCumulativeCertified").val()));
				$("#currentRecoveryAmount").text(inrFormat(parseFloat($("#currentRecoveryAmount").text()==""?0:$("#currentRecoveryAmount").text()).toFixed(2)));
				debugger;
				//$("#sumOfPreviousRecovery").val(inrFormat(parseFloat($("#sumOfPreviousRecovery").val()==""?0.00:$("#sumOfPreviousRecovery").val()).toFixed(2)));
				$("#sumOfCumulativeRecovery").val(inrFormat(parseFloat($("#sumOfCumulativeRecovery ").val()==""?0.00:$("#sumOfCumulativeRecovery ").val()).toFixed(2)));
				$("#other").val(inrFormat(parseFloat($("#other").val()==""?0.00:$("#other").val()).toFixed(2)));
			//	$("#otherAmtPrevCerti").val(inrFormat(parseFloat($("#otherAmtPrevCerti").val()==""?0.00:$("#otherAmtPrevCerti").val()).toFixed(2)));
				$("#otherAmtCumulative").val(inrFormat(parseFloat($("#otherAmtCumulative").val()==""?0.00:$("#otherAmtCumulative").val()).toFixed(2)));
				$("#pettyExpensesCurrentCerti").val(inrFormat(parseFloat($("#pettyExpensesCurrentCerti").val()==""?0.00:$("#pettyExpensesCurrentCerti").val()).toFixed(2)));
			//	$("#pettyExpensesPrevCerti").val(inrFormat(parseFloat($("#pettyExpensesPrevCerti").val()==""?0.00:$("#pettyExpensesPrevCerti").val()).toFixed(2)));
				$("#pettyExpensesCumulative").val(inrFormat(parseFloat($("#pettyExpensesCumulative").val()==""?0.00:$("#pettyExpensesCumulative").val()).toFixed(2)));
				}catch(e){
					console.log(e);
				}
				
				var num=$("#totalAmtToPay").val();
				$("#printtotalAmtToPay").text(inrFormat(parseFloat(num).toFixed(2)));
				
				$(".overlay_ims").hide();	
				$(".loader-ims").hide();
				
			}
	});
}

//loading recovery table in recovery popup
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
								"<td id='"+child_product_id+"issuedQty' class='totalQtyConsumed'><span>"+issuedQty.toFixed(2)+"</span></td>" +
								"<td id='"+child_product_id+"totalRecoveryAmount'  class='totalAmountOfRecovery'><span>"+totalRecoveryAmount.toFixed(2)+"</span></td>" +
								"<td  id='"+child_product_id+"mesurment_name'>"+value.MESURMENT_NAME+" <input type='hidden'  id='"+child_product_id+"mesurment_id' value='"+value.UNITS_OF_MEASUREMENT+"'></td>" +
								"<td id='"+child_product_id+"cumulative'><span>"+tempCumulative.toFixed(2)+"</span></td>" +
								"<td id='"+child_product_id+"previous'><span>"+recoveryAmount.toFixed(2)+"</span></td>" +
								"<td> <input type='text' class='form-control' id='"+child_product_id+"currentAmount' value='"+(currentRecoveryAmount.toFixed(2))+"' onkeyup='calculateRecoveryAmount("+rowsOfRecovery+");' readonly='true'>  </td>" +
								"</tr>";
							
								$("#RecoveryStatement").append(str);
						/*		$("#ViewRecovery").append(str1);*/
							}else{
								
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
									$("#"+child_product_id+"issuedQty").text((tempIssuedQty+issuedQty).toFixed(2));
									$("#"+child_product_id+"totalRecoveryAmount").text((before_taxes+totalRecoveryAmount).toFixed(2));
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
						
						$("#cumulativeRecovery").val(inrFormat(sumOfCumulative));
						$("#previousRecovery").val(inrFormat(sumOfPrev));
						$("#RecoveryStatement").append(temp);
					
						$("#currentRecoveryAmount").val(inrFormat(sumOfRecovery));
						
					//$("#currentRecoveryAmount").html(sumOfRecovery);
					htmlData+="<input type='hidden' name='rowsToIterate' id='rowsToIterate' value='"+rowsOfRecovery+"'>";
					var temp=" <tr>" +
					"<td></td>" +
					"<td class='text-right'><h4><strong>Total Amount</strong></h4></td>" +
					"<td><span  id='totalQtyConsumed'></span></td>" +
					"<td> <span  id='totalAmountOfRecovery'></span></td><td ></td>" +
					"<td id='sumOFRecoveryCumulativce'><strong>"+(sumOfCumulative)+"</strong></td>" +
					"<td ><strong>"+(sumOfPrev)+"</strong></td>" +
					"<td ><strong id='sumOfCurrentAmount'>"+(sumOfRecovery)+"</strong></td></tr>";	
					
					//$("#totalAmtCurntDeduc").val(sumOfRecovery);	
					
					//var TotalPaidAmt1=$("#TotalPaidAmt1").text()==""?0:parseFloat($("#TotalPaidAmt1").text().replace(/,/g,''));
					//$("#totalAmtCumulative").val(inrFormat(TotalPaidAmt1+parseFloat(sumOfCumulative.replace(/,/g,'')).toFixed(2)));
					
					$("#RecoveryStatement").append(temp);
					$("#ViewRecovery").append(temp);
					CalculateTotalAbstract();
					var amountInWords=	convertNumberToWords($("#finalAmt").val().replace(/,/g,''));
					$("#amountInWords").text(amountInWords);
					$("#ViewRecovery").append(htmlData);	
					
					//Sum of Total Quantity Consumed & total Recovery Amount
					var totalQtyConsumed=0;
					var totalAmountOfRecovery=0
					$(".totalQtyConsumed").each(function(){
						
						var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
						totalQtyConsumed+=currentvalue
					});
					$(".totalAmountOfRecovery").each(function(){
						
						var currentvalue=$(this).text()==""?0:parseFloat($(this).text());
						$(this).text(inrFormat(currentvalue.toFixed(2)));
						totalAmountOfRecovery+=currentvalue;
					});
					$("#totalQtyConsumed").text(totalQtyConsumed.toFixed(2));
					$("#totalAmountOfRecovery").text(totalAmountOfRecovery.toFixed(2));

					/*$('strong').each(function(){
						// Do your magic here
					    if (isNum($(this).text())){ // regular expression for numbers only.
					    	var tempVal=parseFloat($(this).text());
					    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
					    		
					    		$(this).text(inrFormat(tempVal));
				           }
						});
						$('span').each(function(){
							// Do your magic here
						    if (isNum($(this).text())){ // regular expression for numbers only.
						    	var tempVal=parseFloat($(this).text());
						    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
						    		
						    		$(this).text(inrFormat(tempVal));
					           }
						});
						$('input[type="text"]').each(function(){
						    if (isNum(this.value)){ // regular expression for numbers only.
						    	var tempVal=parseFloat(this.value);
						    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo")
					        		//$(this).val(tempVal.toFixed(2));
						    	$(this).val(inrFormat(tempVal.toFixed(2)));
					           }
						});
			*/		
					}			
				});
				
			}//recovery method end
			
			
			
		 function loadNMRBillDataForApprovel(){
			 $(".overlay_ims").show();	
			 $(".loader-ims").show();	
			 
			 debugger;
				var ContractorId=$("#ContractorId").val();
				var ContractorName=$("#ContractorName").val();
				var workOrderNo=$("#workOrderNo").val();
				var approvePage=$("#approvePage").val();
				var typeOfWork="NMR";
				var siteId=$("#site_id").val();
				var approvePage="true";
				var tempBillNo=$("#tempBillNo").val();
				var BillNo=$("#billNo").val();
				var billType=$("#billType").val();
				if(workOrderNo.length==0){
					return false;
				}
				$.ajax({
					url : "loadPermanentNMRBillDetailsData.spring",
					type : "GET",
					data : {
						contractorId : ContractorId,
						workOrderNo:workOrderNo,
						approvePage:approvePage,
						siteId:siteId,
						typeOfWork:typeOfWork,
						approvePage: approvePage,
						tempBillNo:tempBillNo,
						BillNo:BillNo,
						billType:billType
					},
					success : function(data) {
					//console.log(JSON.stringify(data));
						$(".overlay_ims").show();	
						$(".loader-ims").show();
					//fruits.splice(index,1); used for remove element from an array
						var workDescNames=new Array();
						var tempworkDescNames=new Array();
						var minorHeadNames=new Array();
						var minorHeadNameAndDate=new Array();
						var strNMRFirstRowtbodyData="";
						
						var strNMRSecondRowtbodyData="";
						var strNMRThirdRowtbodyData="";
						var strNMRtheadData="";
						var strAbstractTableData="";
						var strFirstRowOfTableHead="";
						var strSecondRowOfTableHead="";
						var serialNumber=1;
						var d = new Date();
						var date = d.toLocaleDateString();
						
						lengthOfRows=data.length;
						//dynamic Headers for table 
						
						console.log("data "+data);
						var tempWorkDeskName="";
				 	    var tempRows=0;
				 	    
					//*******************************NMR Main Screen Data**********************************
				 	    						serialNumber=1;
						var rowsOFNMR=1;
						tempWorkDeskName="";
						
						var tempWDName=new Array();
						var allWDNameAndDate=new Array();
						var minorHeadNames2=new Array();
						 var tempMinorHeadName="";
						//nmr table date started
						$.each(data,function(key,value){
							
							var strAbstractTableData="";
							
							const isThis_Str_InArray1 = value.WO_MINORHEAD_DESC.replace(regExpr, "")+ "@@"+ value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+ "@@" + value.RATE+"@@"+value.WO_ROW_CODE+"@@"+value.NMRROWNUM;
							const isInArray1 = minorHeadNames.includes(isThis_Str_InArray1);
							if (isInArray1 == false) {
								
								minorHeadNames.push(isThis_Str_InArray1);
							}
							//minorHeadNames.push(value.WO_MINORHEAD_DESC+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_WORK_DESC_ID);
							
							
							let WO_WORK_DESCRIPTION=	value.WO_WORK_DESCRIPTION.replace(/ /g,'');
							try {
								WO_WORK_DESCRIPTION=	WO_WORK_DESCRIPTION.trim();
								var regExpr = /[^a-zA-Z0-9-. ]/g;
								WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
							
							} catch (e) {
								console.log(e);
							}
							workDescNames.push(WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID);
							//NMR Abstract Data 
							const isThis_Str_InArray = value.WO_WORK_DESCRIPTION;
							const isInArray = 	tempWDName.includes(isThis_Str_InArray);
							allWDNameAndDate.push(value.WO_WORK_DESCRIPTION+"@@"+value.WORK_DATE+"@@"+value.ALLOCATED_QTY);
							
							/*if(tempMinorHeadName!=(value.WO_MINORHEAD_DESC+value.WO_ROW_CODE)){
								tempMinorHeadName=(value.WO_MINORHEAD_DESC+value.WO_ROW_CODE);
								strAbstractTableData+='<tr>';
								strAbstractTableData+='<td style="text-align:center;padding:2px;"><h5><strong></strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+value.WO_WORK_DESC_ID+'"><input type="hidden" name="majorheadId" id="majorheadId" value="'+value.WO_MAJORHEAD_ID+'"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+value.WO_MINORHEAD_ID+'"></td>';
								strAbstractTableData+='<td style="padding:2px;" colspan="10"><h5><strong style="font-size:14px;">'+ value.WO_MINORHEAD_DESC	+ '('+value.WO_ROW_CODE+')</strong><h5><input type="hidden" name="mesurmentId" id="mesurmentId" value="'+value.WO_MEASURMENT_ID+'"></td>';
								strAbstractTableData+='</tr>';
								tempWorkDeskName="";
							}
							*/
							var regExpr = /[^a-zA-Z0-9-. ]/g;
							//Avoiding Duplicate Date to be Printed
							if(isInArray==false){
									tempWDName.push(value.WO_WORK_DESCRIPTION);
								    tempworkDescNames.push(value.WO_WORK_DESCRIPTION.replace(regExpr, "")+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC.replace(regExpr, "")+"@@"+value.WORK_DATE);
								    
							}
							/*if(tempWorkDeskName!=value.WO_WORK_DESCRIPTION){*/
							if(isInArray==false){
								tempWorkDeskName=value.WO_WORK_DESCRIPTION;
							
								var previousQty=0;
								var prevAreaQuantity=new Array();
								var lengthOfThePrevArea=0;
								var previousNMRBillAmount=0;
								var printPreviousRateAndQty="";
								
							
								debugger;
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
											/*previousNMRBillAmount+=parseFloat(n)*array_element[2];				
											if(ind<prevAreaQuantity.length-1&&array_element[0]!=0){
													printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+"),";	
											}else if(array_element[0]!=0){
													printPreviousRateAndQty+="(Qty = "+n+" , Rate = "+array_element[2]+")";
											}*/
										}
									} else {
									//	debugger;
										prevAreaQuantity = value.PREVQTY.split("##");
										previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]));
										previousQty=previousQty.toFixed(3);
										previousNMRBillAmount=parseFloat(previousQty)*prevAreaQuantity[2];
										//	printPreviousRateAndQty+="(Qty = "+previousQty+" , Rate = "+prevAreaQuantity[2]+")";
									}
										
							} catch (e) {
								console.log(e);
							}
							try {
								previousQty=(previousQty/8).toFixed(3);
								previousNMRBillAmount=((previousNMRBillAmount)/8).toFixed(2);
							} catch (e) {
								console.log(e);
							}	
								
								
								 	var allocatedWorkers=value.ALLOCATED_QTY==null?0:parseFloat(value.ALLOCATED_QTY);
								 	var currentBillRate=0;
								 	if(value.BILLRATE==undefined){
								 		currentBillRate=value.RATE==null?0:parseFloat(value.RATE);
								 	}else{
								 		currentBillRate=value.BILLRATE==null?0:parseFloat(value.BILLRATE);
								 	}
								 	
								 	
								 	strAbstractTableData+='<tr>';
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><h5><strong>'+rowsOFNMR+'</strong></h5><input type="hidden" name="workDescId" id="workDescId" value="'+value.WO_WORK_DESC_ID+'"><input type="hidden" name="majorheadId" id="majorheadId" value="'+value.WO_MAJORHEAD_ID+'"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+value.WO_MINORHEAD_ID+'"></td>';
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong>'+value.WO_WORK_DESCRIPTION+'</strong>';
									strAbstractTableData+='<input type="hidden" name="mesurmentId" id="mesurmentId" value="'+value.WO_MEASURMENT_ID+'">';
									strAbstractTableData+='<input type="hidden" name="tempBillNo" id="tempBillNo" value="'+value.TEMP_BILL_ID+'">';
									strAbstractTableData+='<input type="hidden" name="QS_INV_AGN_WORK_PMT_DTL_ID" id="QS_INV_AGN_WORK_PMT_DTL_ID" value="'+value.QS_INV_AGN_WORK_PMT_DTL_ID+'">';
									strAbstractTableData+='</td>';
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong id="'+WO_WORK_DESCRIPTION+'TotalQty">0</strong> </td>';
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong id="'+WO_WORK_DESCRIPTION+'AvgRate">'+parseFloat(currentBillRate).toFixed(2)+'</strong></td>';
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong>'+value.WO_MEASURMEN_NAME+'</strong></td>';
									//Cumulative qty
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong   class="CBQtyClass" id="CB'+WO_WORK_DESCRIPTION+'Qty" >'+((previousQty+allocatedWorkers))+'</strong></td>';
									//Cumulative amount
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  class="CBAmountClass" id="CB'+WO_WORK_DESCRIPTION+'Amount" >'+(((previousNMRBillAmount)+(allocatedWorkers*currentBillRate)))+'</strong></td>';
								//prev qty
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong class="PCQtyClass" id="PC'+WO_WORK_DESCRIPTION+'Qty">'+previousQty+'</strong></td>';
								//prev amount
									strAbstractTableData+=' <td style="text-align:center;padding:1px;"><strong  class="PCAmountClass" id="PC'+WO_WORK_DESCRIPTION+'Amount" >'+(inrFormat(previousNMRBillAmount))+'</strong></td>';
								//cumulative qty	
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  id="'+WO_WORK_DESCRIPTION+'Qty" class="CCQtyClass">0.000</strong> </td>';
									//
									strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  id="'+WO_WORK_DESCRIPTION+'Amount" class="CCAmountClass">0.00</strong>  </td>';			//'+((allocatedWorkers*currentBillRate).toFixed(2))+'	       
									strAbstractTableData+='</tr>';
									rowsOFNMR++;
									$("#NMRAbstractTableData").append(strAbstractTableData);
									strAbstractTableData='';
							}/* else{
								//var tempQty=$("#"+WO_WORK_DESCRIPTION+"Qty").text()==""?0:$("#"+WO_WORK_DESCRIPTION+"Qty").text();
								var str=WO_WORK_DESCRIPTION+"Qty1";
								var tempQty=$("#"+str).val();
								if(tempQty==""){
									tempQty=0;
								}
								$("#"+WO_WORK_DESCRIPTION+"Qty").text(tempQty+value.ALLOCATED_QTY);
							} */
							
							
							/*else{
							
								var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
								$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(2));
							}*/
							
							const isThis_Str_InArray2 = value.WO_MINORHEAD_ID+ "@@"+ value.WO_WORK_DESC_ID+"@@"+value.WO_ROW_CODE;
							const isInArray2 = minorHeadNames2.includes(isThis_Str_InArray2);
							if (isInArray2 == false) {
								
								minorHeadNames2.push(isThis_Str_InArray2);
								var prevQty=$("#"+WO_WORK_DESCRIPTION+"TotalQty").text();
								$("#"+WO_WORK_DESCRIPTION+"TotalQty").text((parseFloat(prevQty)+parseFloat(value.QUANTITY)).toFixed(2));
							}
							serialNumber++;
						});//landing page each loop completed
						
						//<input type="hidden" id="'+WO_WORK_DESCRIPTION+'Qty1" value="'+allocatedWorkers+'">
						//<input type="text" value="'+(allocatedWorkers*value.RATE)+'" name="finalAmt" id="finalAmt">
						//<input type="text" value="'+(allocatedWorkers*value.RATE)+'" name="actualPaybleAmount" id="actualPaybleAmount">
					
					 						
					    strAbstractTableData+='<tr style="background-color: #ccc;">';
						strAbstractTableData+='<td class="text-center"></td> 	';
						strAbstractTableData+='<td class="text-center">Total(A)</td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><span id="TotalACBQty"></span></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><span id="TotalACBAmt"></span></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><span id="TotalAPCQty"></span></td>';
						strAbstractTableData+='<td class="text-center"><span id="TotalAPCAmt"></span></td>';
						strAbstractTableData+='<td class="text-center"><span id="TotalACCQty"></span></td>';
						strAbstractTableData+='<td class="text-center"><span id="TotalACCAmt"></span><input type="hidden" id="CertifiedAmount" name="CertifiedAmount"> </td>';			
						strAbstractTableData+='</tr>';
						
						
						strAbstractTableData+='<tr class="hideInPrint">';
						strAbstractTableData+='<td  class="text-center"></td> 	';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';			
						strAbstractTableData+='</tr>';
						
						strAbstractTableData+='<tr class="hideInPrint">';
						strAbstractTableData+='<td class="text-center"></td> 	';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';			
						strAbstractTableData+='</tr>';
		
						var hiddenpettyExpensesCurrentCerti=$("#hiddenpettyExpensesCurrentCerti").val();
						var hiddenother=$("#hiddenother").val();
						
     					/*strAbstractTableData += '<tr class="hideInPrint">';
						strAbstractTableData += '<td class="text-center"></td> 	';
						strAbstractTableData += '<td class="text-center"><span>Petty Expenses</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"><input type="text"  name="pettyExpensesCumulative" class="CcAmnt" id="pettyExpensesCumulative"  style="border:none;text-align:center;" value="0.00" readonly/></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td class="text-center"><input type="text"  name="pettyExpensesPrevCerti" class="PcAmnt" id="pettyExpensesPrevCerti"  style="border:none;text-align:center;" value="0.00" readonly/></td>';
						strAbstractTableData += '<td class="text-center"></td>';
						strAbstractTableData += '<td class="text-center"><input type="text"  id="pettyExpensesCurrentCerti" name="pettyExpensesCurrentCerti" class="raDeductionAmt" style="border:none;width:100%;text-align:center;" value="'+hiddenpettyExpensesCurrentCerti+'" readonly> <input type="hidden" name="actualPettyExpensesCurrentCerti"  value="'+hiddenpettyExpensesCurrentCerti+'" > <br></td>';
						strAbstractTableData += '</tr>';
						
		                 
						strAbstractTableData += '<tr class="hideInPrint">';
						strAbstractTableData += '<td class="text-center"></td> 	';
						strAbstractTableData += '<td class="text-center"><span>Others</span></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"><input type="text"  name="otherAmtCumulative" class="CcAmnt" id="otherAmtCumulative"  style="border:none;text-align:center;" value="0.00" readonly/></td>';
						strAbstractTableData += '<td style="text-align:center;padding:3px;"></td>';
						strAbstractTableData += '<td class="text-center"><input type="text"  name="otherAmtPrevCerti" class="PcAmnt" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="0.00" readonly/></td>';
						strAbstractTableData += '<td class="text-center"></td>';
						strAbstractTableData += '<td class="text-center"><input type="text"  id="other" name="other" class="raDeductionAmt" style="border:none;width:100%;text-align:center;" value="'+hiddenother+'" readonly><input type="hidden" id="actualOtherAmt" name="actualOtherAmt" value="'+hiddenother+'">  <br></td>';
						strAbstractTableData += '</tr>';
		
			
						strAbstractTableData+='<tr class="hideInPrint">';
						strAbstractTableData+='<td class="text-center"></td> 	';
						strAbstractTableData+='<td class="text-center"><span>Recovery(B)</span><label id="showCurrentRecoveryAmount" style="display: none;"></label>  </td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="cumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"></td>';
						strAbstractTableData+='<td class="text-center"><input type="text"  name="previousRecovery" class="PcAmnt" id="previousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"><input type="text"  id="currentRecoveryAmount" name="currentRecoveryAmount" class="raDeductionAmt" value=""  style="border:none;width:100%;text-align:center;"  readonly="readonly"><input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount"> <br><a class="" href="#" data-toggle="modal" data-target="#modal-recovery-click" id="recovery">Click Here</a></td>';
						strAbstractTableData+='</tr>';
						
						strAbstractTableData+='<tr class="hideInPrint">';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:13px;"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';
						strAbstractTableData+='<td class="text-center"></td>';			
						strAbstractTableData+='</tr>';
						
						
						strAbstractTableData+='<tr class="hideInPrint" style="background-color: #ccc;">';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><h5><strong></strong></h5></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong>Net Payable Amount (A - B)</td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong></strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong></strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong></strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  id="CBTotalQty">0</strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  id="CBToatalAmount">0</strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong  id="PCTotalQty">0</strong></td>';
						strAbstractTableData+=' <td style="text-align:center;padding:1px;"><strong  id="PCToatalAmount" >0</strong></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong id="CCTotalQty">0</strong> <input type="hidden"  name="actualPaybleAmount" id="actualPaybleAmount"></td>';
						strAbstractTableData+='<td style="text-align:center;padding:1px;"><strong id="CCToatalAmount">0</strong><input type="hidden" name="finalAmt" id="finalAmt"> </td>';				       
						strAbstractTableData+='</tr>';
						
						strAbstractTableData+='<tr>';
						strAbstractTableData+='<td class="text-center" colspan="5" style="padding:1px;">Amount in Words</td>';
						strAbstractTableData+='<td class="text-center" colspan="6" style="padding:1px;" id="amountInWords"></td>';								
						strAbstractTableData+='</tr>';
						
						strAbstractTableData+='<tr class="hideinPrint">';
						strAbstractTableData+='<td class="text-center" colspan="5" style="padding:1px;"><strong></strong></td>';
						strAbstractTableData+='<td class="text-center" colspan="6" style="padding:1px;"><strong><a href="javascript:void(0);" onclick="openAbstract()" id="NmrAbstract">NMR Details</a></strong></td>';
						strAbstractTableData+='</tr>'; */
						
						/*strAbstractTableData+=' <tr>';
						strAbstractTableData+=' <td colspan="3" style="text-align:center;border-right: 0px !important;"><p style="margin-top:65px;"></p><strong>QS</strong></td>';
						strAbstractTableData+='  <td colspan="3" style="text-align:center;border-right: 0px !important;border-left: 0px !important;"><p style="margin-top:65px;border-left: 0px !important;"></p><strong>Sr.QS</strong></td>';
		      			strAbstractTableData+='  <td colspan="3" style="text-align:center;border-right: 0px !important;border-left: 0px !important;"><p style="margin-top:65px;"></p><strong>AGM/PM</strong></td>';
						strAbstractTableData+='  <td colspan="3" style="text-align:center;border-left: 0px !important;"><p style="margin-top:65px;"></p><strong>Project Incharge</strong></td>';
						strAbstractTableData+=' </tr>';*/
						
						
						$("#NMRAbstractTableData").append(strAbstractTableData);
						//pettyAndOtherChange();
					
						//calling recovery Methods
						 loadRARecovery();
						 
						if (typeof(Storage) !== "undefined") {
						         sessionStorage.setItem("NMRDATA", data);
						         sessionStorage.setItem("workDescNames", tempworkDescNames);
						         sessionStorage.setItem("minorHeadNames", minorHeadNames);
						} else {
						        alert("Sorry, your browser does not support Web Storage...");
						}
						
						
						
//=============================================================Lading page completed here==========================================================
//=================================================================================================================================================
				 	    			 	    
//************************************************NMR Abstract Table Head Data***************************
						  
							 serialNumber=1;
							 strFirstRowOfTableHead+="<tr>";
							 strSecondRowOfTableHead+="<tr>";
						 
							 //Table Head
							 strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px;"rowspan="2">S.NO</th>';
							 strFirstRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width: 105px;"rowspan="2">Date</th>';
							 strFirstRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width:15%;"rowspan="2">Description</th>';
							 strFirstRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width:15%;"rowspan="2">Manual Description</th>';
						     strFirstRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;"colspan="2"rowspan="1">Duration</th>';
							 strFirstRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;"rowspan="2">No. of hours</th>';
							 
							 //Table Head
							 strSecondRowOfTableHead+= '<th style="text-align: center;font-size: 16px;padding:5px;">From</th>';
							 strSecondRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;">To</th>';
				           
							 var lengthOfRows=0;
							 lengthOfRows=data.length;
					
						        var strTempFirstRowData="";
								var strTempSecondRowData="";
							
								/*for (var index = 0; index < tempWDName.length; index++) {
									tempRows++;
									 strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px; "rowspan="2" >'+tempWDName[index]+' </th>';
									 strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 16px;padding:5px;width:4%;"rowspan="2" >Rate</th>';
									 strSecondRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width:5%;">'+tempWDName[index]+'(hrs)</th>';
								}*/
								//adding work description names as headers
								for (var index = 0; index < tempWDName.length; index++) {
									tempRows++;
									strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px;  rowspan="1"  colspan="2">'+tempWDName[index]+' </th>';
									//strFirstRowOfTableHead += ' <th style="text-align: center;font-size: 16px;padding:5px;width:4%;"rowspan="2" >Rate</th>';
									
								///	strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Total Qty</th>';
									///strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Prev Qty</th>';
						
									strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Qty</th>';
									strSecondRowOfTableHead += '<th style="text-align: center;font-size: 16px;padding:5px;width:4%;" >Rate</th>';
									// strSecondRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width:5%;">'+tempWDName[index]+'(hrs)</th>';
								}
								//adding number of hours 
								for (var index = 0; index < tempWDName.length; index++) {
									 strSecondRowOfTableHead+='<th style="text-align: center;font-size: 16px;padding:5px;width:5%;">'+tempWDName[index]+'(hrs)</th>';
								}
						
								 strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px;width:15%;"colspan="'+tempRows+'"rowspan="1">Total</th>';
								 strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px;"rowspan="2">Remarks</th>';
								 //if the statusOfPage variable value is true means this is the only show page
								 if (statusOfPage!="true") {
									 strFirstRowOfTableHead+=' <th style="text-align: center;font-size: 16px;padding:5px;"rowspan="2">Actions</th>';
								 }
						         
								
								 strFirstRowOfTableHead+="</tr>";
								 strSecondRowOfTableHead+='</tr>';
							
						$("#NMRHeadData").append(strFirstRowOfTableHead+strSecondRowOfTableHead);
						
					//************************************************NMR Abstract Table Head Data***************************
					
					var minorHeadId="";
					var workDate="";
					tempWorkDeskName="";
					//*************************************************NMR Abstract Table Body******************************
					//table Row Data 
					
					var workDescripition=new Array();
					
					var CheckingDupliWD_MH=new Array();
					var anothersendingWD_MH=new Array();
					var requestFrom="nmrDetailsPrint";
					$.ajax({
						url : "loadPermanentNMRBillDetailsData.spring",
						type : "GET",
						data : {
							contractorId : ContractorId,
							workOrderNo:workOrderNo,
							approvePage:approvePage,
							siteId:siteId,
							typeOfWork:typeOfWork,
							approvePage: approvePage,
							tempBillNo:tempBillNo,
							BillNo:BillNo,
							billType:billType,
							requestFrom:requestFrom
						},
						success : function(data) {
							
							
							//Table Data Started
						$.each(data,function(key,value){
									
							const isThis_Str_InArray = value.WO_MINORHEAD_ID+"@@"+value.NMRROWNUM;
							const isInArray = 	minorHeadNameAndDate.includes(isThis_Str_InArray);
							
							const checkWD=value.WO_WORK_DESCRIPTION+"@@"+value.WORK_DATE;
							//Avoid duplicate entry's  of Major Head and Minor Head's
							if(CheckingDupliWD_MH.includes(isThis_Str_InArray)==false){
								anothersendingWD_MH.push(value.WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC+"@@"+value.WORK_DATE+"@@"+value.NMRROWNUM);
								CheckingDupliWD_MH.push(isThis_Str_InArray);
							}
							//checking is current record is exist in minorHeadNameAndDate
							if(isInArray==false &&(value.ALLOCATED_QTY!=undefined||value.ALLOCATED_QTY!=null)){
								minorHeadNameAndDate.push(isThis_Str_InArray);
									
									let WO_WORK_DESCRIPTION=	value.WO_WORK_DESCRIPTION.replace(/ /g,'');
									try {
										WO_WORK_DESCRIPTION=	WO_WORK_DESCRIPTION.trim();
										var regExpr = /[^a-zA-Z0-9-. ]/g;
										WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
									} catch (e) {
										console.log(e);
									}
									
									strNMRFirstRowtbodyData+='<tr id="tablerow'+(serialNumber)+'" class="tablerowcls">';
									strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;">'+(serialNumber)+'';
									strNMRFirstRowtbodyData+='<input type="hidden" name="tempBillNo" id="tempBillNo" value="'+value.TEMP_BILL_ID+'">';
									strNMRFirstRowtbodyData+='<input type="hidden" name="nmrRowNumber'+serialNumber+'" id="nmrRowNumber'+serialNumber+'" value="'+value.NMRROWNUM+'">';
									strNMRFirstRowtbodyData+='<input type="hidden" name="QS_INV_AGN_WORK_PMT_DTL_ID'+serialNumber+'" id="QS_INV_AGN_WORK_PMT_DTL_ID'+serialNumber+'" value="'+value.QS_INV_AGN_WORK_PMT_DTL_ID+'">';
									strNMRFirstRowtbodyData+='</td>';
									strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control Date" value="'+value.WORK_DATE+'" id="Date'+serialNumber+'" title="'+value.WORK_DATE+'" name="WorkDate'+serialNumber+'" readonly style="width: 100px;"/></td>';
									strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;">';
									strNMRFirstRowtbodyData+='<select class="form-control Description" id="Description'+serialNumber+'"  name="Description'+serialNumber+'" readonly  style="display:none;">';
									minorHeadId=value.WO_MINORHEAD_ID;
									var woRowCode=value.WO_ROW_CODE;
									var tempRowNum=value.NMRROWNUM
									workDate=value.WORK_DATE;
									tempWorkDeskName="";
									var tempRowsOFTD=0;
									var tempMinorHeadName="";
									var addDescTextBox="";
									//tempWorkDeskName=value.WO_WORK_DESCRIPTION
									//this loop is for select box option
									
								 	/*$.each(data,function(key,value){
								 			if(minorHeadId==value.WO_MINORHEAD_ID){
								 				if(tempMinorHeadName!=value.WO_MINORHEAD_DESC){
								 					tempMinorHeadName=value.WO_MINORHEAD_DESC
												strNMRFirstRowtbodyData+='<option value="'+value.WO_MINORHEAD_DESC+'@@'+value.WO_MINORHEAD_ID+'" selected>'+value.WO_MINORHEAD_DESC+'</option>';
								 					addDescTextBox="<input type='text' value='"+value.WO_MINORHEAD_DESC+"' title='"+value.WO_MINORHEAD_DESC+"' class='form-control' readonly='true'>";
								 			}
								 			}else{
								 				if(tempMinorHeadName!=value.WO_MINORHEAD_DESC){
								 					tempMinorHeadName=value.WO_MINORHEAD_DESC
													strNMRFirstRowtbodyData+='<option value="'+value.WO_MINORHEAD_DESC+'@@'+value.WO_MINORHEAD_ID+'">'+value.WO_MINORHEAD_DESC+'</option>';
								 				}
								 			}
										}); */
									
								 	for (var index = 0; index < minorHeadNames.length; index++) {
										
											let str = minorHeadNames[index].split("@@");
											var regExpr = /[^a-zA-Z0-9-. ]/g;
											if(minorHeadId==str[1]&&str[4]==woRowCode&&str[5]==tempRowNum) {
											//	tempMinorHeadName = str[0].replace(regExpr, "");
											//  woRowCode=str[4];
												
												strNMRFirstRowtbodyData += '<option value="'+ str[0] + '@@' + str[1] + '@@' + str[4] + '">' + str[0]+ '('+str[4]+')</option>';
												addDescTextBox="<input type='text' value='"+ str[0]+ "("+str[4]+")' title='"+ str[0]+ "("+str[4]+")' class='form-control' readonly='true'>";
											}
										//	tempRowData += "<input type='hidden' name='minorWDId1' value='"	+ str[1] + "@@" + str[2] + '@@' + str[4] + "'>";
										}

									
										var manualDescription=value.MANUAL_DESC==null?"":value.MANUAL_DESC;
										workDescripition.push(manualDescription+"@@"+(value.REMARKS==null?"":value.REMARKS));
												
										strNMRFirstRowtbodyData+='</select>';
										strNMRFirstRowtbodyData+=addDescTextBox;
										strNMRFirstRowtbodyData+='</td>';
										strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control ManuvalDescription" value='+(manualDescription)+'  title='+(manualDescription)+' placeholder="manual Description" id="ManuvalDescription'+serialNumber+'" name="ManuvalDescription'+serialNumber+'" readonly/></td>';
										strNMRFirstRowtbodyData+=' <td style="text-align:center;padding:5px;"><input type="text" class="form-control From" value="'+value.FROM_TIME.toFixed(2)+'" title="'+value.FROM_TIME.toFixed(2)+'" id="From'+serialNumber+'"  style="width: 60px;" name="FromTime'+serialNumber+'" readonly/><input type="hidden" value="'+value.FROM_TIME.toFixed(2)+'" id="actualFrom'+serialNumber+'"   name="actualFromTime'+serialNumber+'" /></td>';
										strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control To" value="'+value.TO_TIME.toFixed(2)+'" title="'+value.TO_TIME.toFixed(2)+'" id="To'+serialNumber+'" style="width: 60px;" name="toTime'+serialNumber+'" readonly/><input type="hidden"  value="'+value.TO_TIME.toFixed(2)+'" id="actulTo'+serialNumber+'" name="actualToTime'+serialNumber+'"/></td>';
										strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control Noofhours" value="'+value.NO_OF_HOURS.toFixed(2)+'" title="'+value.NO_OF_HOURS.toFixed(2)+'"  id="Noofhours'+serialNumber+'"  name="Noofhours'+serialNumber+'" readonly/><input type="hidden" class="form-control Noofhours" value="'+value.NO_OF_HOURS.toFixed(2)+'" id="actualNoofhours'+serialNumber+'"  name="actualNoofhours'+serialNumber+'"/></td>';
									
										//this loop is for no of Workers are enter'd while creating NMR Abstract Bill's
										
										var intt=0;
										 for (var index = 0; index < tempworkDescNames.length; index++) {
												var str=tempworkDescNames[index].split("@@");
												if(value.WO_MINORHEAD_ID==minorHeadId&&workDate==value.WORK_DATE){
													 tempWorkDeskName=value.WO_WORK_DESCRIPTION;
													 WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
													 var regExpr = /[^a-zA-Z0-9-. ]/g;
												  WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");
													
												 // strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';
												 // strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';
													  
						
												  strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;" id='+value.WO_WORK_DESC_ID+'><input type="text" class="form-control " value="0" name="noOfWorkers'+serialNumber+'" id="'+(WO_WORK_DESCRIPTION+serialNumber)+'"  onkeyup="calculateData(this.id, this.value, '+serialNumber+')" onkeypress="return isNumber(this.value, event)"  readonly="readonly"/>';  //  style="width: 100px;"
												  strNMRFirstRowtbodyData+='<input type="hidden" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/><input type="hidden" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/>';
											      strNMRFirstRowtbodyData+='<input type="hidden" class="form-control " value="'+value.ALLOCATED_QTY+'" name="actualnoOfWorkers'+serialNumber+'" id="actual'+(WO_WORK_DESCRIPTION+serialNumber)+'""/><input type="hidden" name="minorWDId'+serialNumber+'" id="minorWDId'+(WO_WORK_DESCRIPTION+serialNumber)+'" value="'+(str[2]+"@@"+str[1])+'"><input type="hidden" name="rate'+serialNumber+'" id="" value="'+(value.RATE)+'"><input type="hidden" name="minorHeadId" id="minorHeadId" value="'+str[2]+'"><input type="hidden" name="workDescId'+serialNumber+'" id="workDescId'+serialNumber+'" value="'+str[1]+'"></td>';
											      strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control WDAmount'+ (WO_WORK_DESCRIPTION )+ '" id="WDRate'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value="0" readonly/></td>';
			  	 						}
									} 

											tempWorkDeskName="";
											tempRowsOFTD=0;	
											
										//this loop is for No Of Hours 
										for (var index = 0; index < tempworkDescNames.length; index++) {
											var str=tempworkDescNames[index].split("@@");
											if(value.WO_MINORHEAD_ID==minorHeadId&&workDate==value.WORK_DATE){
												tempWorkDeskName=value.WO_WORK_DESCRIPTION;
												WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
												var regExpr = /[^a-zA-Z0-9-. ]/g;
												  WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
												  strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control '+WO_WORK_DESCRIPTION+'" value="0"  name="noOfWorkers_hrs'+serialNumber+'" id="'+serialNumber+(WO_WORK_DESCRIPTION+serialNumber+serialNumber)+'" readonly/><input type="hidden" class="RowWDAmount'+WO_WORK_DESCRIPTION+'" id="RowAmount'+serialNumber+ (WO_WORK_DESCRIPTION+ serialNumber + serialNumber)+ '" ></td>';
												 // strNMRFirstRowtbodyData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control WDAmount'+ (WO_WORK_DESCRIPTION )+ '" id="WDRate'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" readonly/></td>';
											}
										}
										strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control" value="'+(value.REMARKS==null?"":value.REMARKS)+'" title="'+(value.REMARKS==null?"":value.REMARKS)+'" id="Remarks'+serialNumber+'"  name="Remarks'+serialNumber+'" readonly/></td>';
									//it this is the status page then don't show the edit  button
									if (statusOfPage!="true") {
										strNMRFirstRowtbodyData+='<td style="text-align:center;padding:5px;"><button  id="" class="btnaction"  type="button" onclick="EditNMRBill('+serialNumber+')"><i class="fa fa-pencil"></i></button></td>';  //onclick="editRow('+serialNumber+')" <button onclick="deleteRow('+serialNumber+')" id="" class=""  type="button">Delete</button>
									}
										strNMRFirstRowtbodyData+='</tr>';
										serialNumber++;
									}//if condition
								});
							
								
								
						$("#noOfRowsToIterate").val(minorHeadNameAndDate.length);
						//*************************************************NMR Abstract Table Body******************************
						//*************************************************NMR Abstract Table Body******************************
						$("#NMRBillstableIdfirstRow").html(strNMRFirstRowtbodyData);
						
						    //this loop is using for special chars
							for (var index = 0; index < workDescripition.length; index++) {
								var tempstr=workDescripition[index].split("@@");
								$("#ManuvalDescription"+(index+1)).val(tempstr[0]);
								$("#Remarks"+(index+1)).val(tempstr[1]);
								$("#Remarks"+(index+1)).attr("title",tempstr[1]);
							}
						
							    strNMRSecondRowtbodyData+=' <tr>';
							    strNMRSecondRowtbodyData+='<td style="text-align:right;padding:5px;font-weight: 1000;font-size: 14px;" colspan="'+(7+tempRows+tempRows)+'">Total Number of Hours</td>';
						
						 
							    strNMRThirdRowtbodyData+=' <tr>';
							    strNMRThirdRowtbodyData+='<td style="text-align:right;padding:5px;font-weight: 1000;font-size: 14px;" colspan="'+(7+tempRows+tempRows)+'">Total Labor</td>';
						

								var lengthOfRows=0;
								lengthOfRows=data.length;
								//adding empty td's in NMR Abstract Body
							    for (var index = 1; index <= tempRows; index++) {
							    /* 	
									strNMRSecondRowtbodyData+='<td style="text-align:center;padding:5px;"></td>';
									strNMRThirdRowtbodyData+='<td style="text-align:center;padding:5px;"></td>'; */
								}
							
							    tempWorkDeskName="";
								//this loop is for no of Hour's to sum  
									for (var index = 0; index < tempworkDescNames.length; index++) {
										var str=tempworkDescNames[index].split("@@");
											
										WO_WORK_DESCRIPTION=str[0].replace(/ /g,'');
										var regExpr = /[^a-zA-Z0-9-. ]/g;
										WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr, "");
										strNMRSecondRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control" value="0.00" id="'+WO_WORK_DESCRIPTION+'finalHours" readonly><input type="hidden" id="FinalWDAmount'+ WO_WORK_DESCRIPTION+ '"><input type="hidden" id="sumOfWDAmount'+ WO_WORK_DESCRIPTION+ '"></td>';
										strNMRThirdRowtbodyData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control" value="0.000" id="'+WO_WORK_DESCRIPTION+'Days" readonly></td>';
								}
							
								
							    strNMRSecondRowtbodyData+='<td style="text-align:center;padding:5px;"></td>';
							    //if this is not the status page then only print the td
							    if (statusOfPage!="true") {
								    strNMRSecondRowtbodyData+='<td style="text-align:center;padding:5px;"></td>';
							    }
							    strNMRSecondRowtbodyData+='</tr>';
							    strNMRThirdRowtbodyData+='<td style="text-align:center;padding:5px;"></td>';
								
							    //if this is not the status page then only print the td
								if (statusOfPage!="true") {
									strNMRThirdRowtbodyData+='<td style="text-align:center;padding:5px;"></td>';
								}
							    strNMRThirdRowtbodyData+='</tr>';
							   
								$("#NMRBillstableIdfirstRow").append(strNMRSecondRowtbodyData);
								$("#NMRBillstableIdfirstRow").append(strNMRThirdRowtbodyData);
								
								//$("#NMRBillstableId").css("width", (2000+100*workDescNames.length));
								serialNumber=1;
								
								var len=tempWDName.length;
								var increseIndex=0;
								//this is for calculation
								var tempWorkDeskNameForCheck=new Array();
								minorHeadNameAndDate=new Array();

		 						console.log(" minorHeadNameAndDate "+minorHeadNameAndDate);
		 						console.log(" tempWDName "+tempWDName);
		 						var tempBillNo=$("#tempBillNo").val();
		 					
							var intt=0;	
							var index=0;

							function myStopFunction() {
							    clearInterval(myVar);
							}
							 var intt=0;
							
							 for (var index = 0; index < anothersendingWD_MH.length; index++) {
								 
								var array_element = anothersendingWD_MH[index];
								 var str=anothersendingWD_MH[index].split("@@");
									var	 minorHeadId=str[2];
									var	workDeskId=str[1];
									var workDate=str[4];
									var rowNum=str[5];
									//anothersendingWD_MH.push(value.WO_WORK_DESCRIPTION+"@@"+value.WO_WORK_DESC_ID+"@@"+value.WO_MINORHEAD_ID+"@@"+value.WO_MINORHEAD_DESC+"@@"+value.WORK_DATE+"@@"+value.NMRROWNUM);
									intt++;
									$.each(data,function(key,value){
										
										if(value.WO_MINORHEAD_ID==minorHeadId&&value.WORK_DATE==workDate&&value.NMRROWNUM==rowNum){
											WO_WORK_DESCRIPTION=value.WO_WORK_DESCRIPTION.replace(/ /g,'');
											 var regExpr = /[^a-zA-Z0-9-. ]/g;
									 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");  
											$("#"+WO_WORK_DESCRIPTION+(intt)).val(value.ALLOCATED_QTY);
											$("#"+WO_WORK_DESCRIPTION+(intt)).attr("title", value.ALLOCATED_QTY);
											$("#IndividualWDTotalQTY"+WO_WORK_DESCRIPTION+(intt)).val(value.QUANTITY);
											$("#IndividualWDTotalQTY"+WO_WORK_DESCRIPTION+(intt)).attr("title", value.QUANTITY);
											
											$("#actual"+WO_WORK_DESCRIPTION+intt).val(value.ALLOCATED_QTY);
											$("#WDRate"+WO_WORK_DESCRIPTION+intt).val(value.RATE);
											$("#WDRate"+WO_WORK_DESCRIPTION+intt).attr("title", value.RATE);
											//$("#"+WO_WORK_DESCRIPTION+(intt)).prop("readonly",false);
											$("#minorWDId"+WO_WORK_DESCRIPTION+intt).val(value.WO_MINORHEAD_ID+"@@"+value.WO_WORK_DESC_ID);

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
														tempPreviousQty += parseFloat(((noOfWorker* noOfHrs)));
														tempAmount+= parseFloat(((noOfWorker* noOfHrs*rate)));
																					
														previousQty=tempPreviousQty;
														previousNMRBillAmount=tempAmount;
													}
												} else {
													debugger;
													prevAreaQuantity = value.PREVQTY.split("##");
													previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]));
													previousQty=previousQty.toFixed(3);
													previousNMRBillAmount=parseFloat(previousQty)*prevAreaQuantity[2];
													
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
											$("#prevWDTotalQTY"+WO_WORK_DESCRIPTION+(intt)).val(previousQty);
											$("#prevWDTotalQTY"+WO_WORK_DESCRIPTION+(intt)).attr("title",previousQty);
											
											calculateData("form-control "+value.WO_MINORHEAD_ID+value.WO_ROW_CODE+WO_WORK_DESCRIPTION+" validatefield "+value.RATE,WO_WORK_DESCRIPTION+(intt),value.ALLOCATED_QTY,(intt));
										}
									});
							}
							
							 convertToCombo();
							 $(".Description" ).each(function() {
									//
									

									 var current=$(this);
									  var currentId=current.attr('id');
									  var splitId=currentId.split("Description")[1];
									 var currentValue=current.val();
									 try {
										 currentValue=currentValue.split("@@")[1]+currentValue.split("@@")[2];
										 //CurrentCompareDescription=CurrentCompareDescription.trim();
									} catch (e) {
										// TODO: handle exception
									}	
									var noOfHours=$("#Noofhours"+splitId).val();
									
									var tect=0;
								for ( var j = 0; j < tempworkDescNames.length; j++) {
									//
									var str=tempworkDescNames[j].split("@@");
									 var regExpr = /[^a-zA-Z0-9-. ]/g;
									 
								 		WO_WORK_DESCRIPTION=str[0].replace(regExpr,"");  
								 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(/ /g, '');
									var NoOfPersons=$("#"+WO_WORK_DESCRIPTION+splitId).val();
									var calPersons=parseFloat((noOfHours*NoOfPersons)/8);
									try{
									 calPersons=calPersons.toFixed(2);
									 calPersons=parseFloat(calPersons);
									
									}catch(e){
										console.log(e);
									}
									
									var str1=currentValue+WO_WORK_DESCRIPTION;
									tect=$( "#"+str1+"Qty" ).text();
									var Rate=$("#"+str1+"Rate").text();
									var tempPersons=tect==""?0:parseFloat(tect);
									$("#"+str1+"Qty").text((calPersons+tempPersons).toFixed(2));		
									$("#"+str1+"Amount").text((parseFloat($("#"+str1+"Qty").text()).toFixed(2)*Rate).toFixed(2));			
									$("#CB"+str1+"Qty").text(($("#PC"+str1+"Qty").text()==""?0:parseFloat($("#PC"+str1+"Qty").text())+parseFloat($("#"+str1+"Qty").text()==''?0:$("#"+str1+"Qty").text())).toFixed(2));		
									$("#CB"+str1+"Amount").text(($("#PC"+str1+"Amount").text()==""?0:parseFloat($("#PC"+str1+"Amount").text())+parseFloat($("#"+str1+"Qty").text()*Rate)).toFixed(2));
									
								}
								});
							
							
								CalculateTotalAbstract();
								Datepush();
								$("#raTotalCc").text($("#TotalACBAmt").text());
								$("#totalCc").text($("#TotalACCAmt").text());
								//$("#raTotalPc").text($("#TotalAPCAmt").text());
								//*************************************************NMR Abstract Table Body******************************
					
							$(".overlay_ims").hide();	
							$(".loader-ims").hide();
							NMRCreateTableSubmit();
							pettyAndOtherChange();
							var amountInWords=	convertNumberToWords($("#CCToatalAmount").text());
							$("#amountInWords").text(amountInWords);
							
							/*$('strong').each(function(){
								// Do your magic here
							    if (isNum($(this).text())){ // regular expression for numbers only.
							    	var tempVal=parseFloat($(this).text());
							    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
							    		$(this).text(inrFormat(tempVal));
						           }
								});
								$('span').each(function(){
									// Do your magic here
								    if (isNum($(this).text())){ // regular expression for numbers only.
								    	var tempVal=parseFloat($(this).text());
								    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="contractorPhoneNo"&&this.id!="workOrderId"&&this.id!="notificationBell")
								    		$(this).text(inrFormat(tempVal));
							           }
								});*/
						}//inner success function operation completed
					});//inner ajax call completed operation completed					

					}//success function
				});//Ajax Call Completed
			}//method completed
		 
		 
		 
		 //printing from and to dates 
		 var dateDate1;
		 function Datepush(){
			 
		   dateDate1=[];
			$(".Date").each(function(){
				if($(this).val()!=''){
					var date = moment($(this).val(), "DD-MM-YYYY").toDate();
					var currentDate=$(this).val();
					/* dateDate.push(new Date($(this).val())); */
					dateDate1.push(date);
				}
			});
				var sorted = dateDate1.sort(sortDates);
				var minDate = sorted[0];
				var maxDate = sorted[sorted.length-1];
			
				$("#fromDate1").text(minDate.getDate()+"-"+(minDate.getMonth()+1)+"-"+minDate.getFullYear());
				$("#toDate1").text(maxDate.getDate()+"-"+(maxDate.getMonth()+1)+"-"+maxDate.getFullYear());
		 }
		 
		 function sortDates(a, b){
		     return a.getTime() - b.getTime();
		 }
		 
		//converting to combobox
			function convertToCombo(){
				var tableLength=$(".tablerowcls").length;
				for(var i=0; i<tableLength;i++){					
					$("#Description"+(i+1)).attr("disabled", "true");
				}				
			} 
			
			
			//calculating ledger values for total
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
				//$("#TotalCumulativeAmount").html(finalCumulativeAmount.toFixed(2));
			}

			
