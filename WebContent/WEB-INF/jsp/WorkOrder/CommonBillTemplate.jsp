<!-- not used  -->
          <tbody>
            	<tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"><strong>I</strong></td>
              <td class="text-center td-active border-none input-outline" style="border:1px solid #000;padding:5px;"><span>Total Value of Work Completed / Certified</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raCc" id="raCc" style="border:none; text-align:center;" value="${billBean.totalCurrentCerti}" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="raPc" id="raPc" style="border:none; text-align:center;" value="0" readonly/></td><%-- ${billBean.totalCurrentCerti} --%>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;" > <input name="ACTUUAl" type="hidden" value="${billBean.paybleAmt}">  <input type="text"  name="raAmountToPay" id="raAmountToPay"   style="border:none; text-align:center;" value=" ${billBean.totalCurrentCerti}" readonly></td><!--  ${billBean.totalCurrentCerti}amount to pay-->
            </tr>
            <tr>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
            </tr>
        
         	<tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (A)</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalCc" name="raTotalCc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  id="raTotalPc" name="raTotalPc" style="border:none;text-align:center;" value="" readonly></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualRaAmountToPay" id="actualRaAmountToPay" value="${billBean.totalCurrentCerti }">  <input type="text" id="totalCc" name="totalCc"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.totalCurrentCerti}" readonly></td> <!-- ${billBean.totalCurrentCerti} -->
            </tr>
           <tr>
           <c:set value="ADV" var="ADV"></c:set>
           <c:set value="SEC" var="SEC"></c:set>
           <c:set value="PETTY" var="PETTY"></c:set>
           <c:set value="OTHER" var="OTHER"></c:set>
              <c:set value="RECOVERY" var="RECOVERY"></c:set>
              <c:forEach items="${deductionList }" var="deducList">
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq ADV}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="raDeductionAmt"></c:set>
            </c:if>
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq SEC}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="secDepositCurrentCerti"></c:set>
            </c:if>
             <c:if test="${deducList.TYPE_OF_DEDUCTION eq PETTY}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="pettyExpensesCurrentCerti"></c:set>
            </c:if>
            <c:if test="${deducList.TYPE_OF_DEDUCTION eq OTHER}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="other"></c:set>
            </c:if>
              <c:if test="${deducList.TYPE_OF_DEDUCTION eq RECOVERY}">
                  <c:set value="${deducList.DEDUCTION_AMOUNT}" var="recoveryAmount"></c:set>
            </c:if>
            </c:forEach> 
       
             <c:forEach items="${previousPaidAmount}" var="prevPaidAmt">
                  <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq ADV}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="raeductionPrevCumulative"></c:set>
            </c:if>
            <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq SEC}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="secDepositPrevCerti"></c:set>
            </c:if>
             <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq PETTY}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="pettyExpensesPrevCerti"></c:set>
            </c:if>
            <c:if test="${prevPaidAmt.TYPE_OF_DEDUCTION eq OTHER}">
                  <c:set value="${prevPaidAmt.DEDUCTION_AMOUNT}" var="otherAmtPrevCerti"></c:set>
            </c:if>
            </c:forEach>
              <tr>
              <td class="text-center" style="border:1px solid #000;"></td>
              <td class="text-center" style="border:1px solid #000;"><span>Paid Amount</span></td>
              
              <td class="text-center" style="border:1px solid #000;"><input type="text" name="TotalPaidAmt1" id="TotalPaidAmt1" class="CcAmnt"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;"><input type="text"  name="TotalPaidAmt" id="TotalPaidAmt" class="PcAmnt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;"><input type="text" class="raPaidAmnt" name="raPaidAmnt" id="raPaidAmnt" autocomplete="off"   style="border:none;text-align:center;" value="0" readonly/></td> <!-- onkeyup="caldeductionamt('ra')"  -->
            </tr>
           <tr>
           
              <td class="text-center" style="border:1px solid #000;padding:5px;">a)</td>
              <td class="text-center"style="border:1px solid #000;padding:5px;"><span>Advance Deduction</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raeductionCumulative" id="raeductionCumulative"  style="border:none;text-align:center;" value="" readonly/><input type="hidden" id="raCcDeductionAmthidden"></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="raePrevductionCumulative" id="raeductionPrevCumulative"  style="border:none;text-align:center;" value="${raeductionPrevCumulative }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${raDeductionAmt }" name="changedRaDeductionAmt" id="changedRaDeductionAmt"> <input type="text"  name="raDeductionAmt" id="raDeductionAmt" autocomplete="off"  onfocusout="raCalcAmountToPay(contractorRABill.raAmountToPay.value,2)"  style="border:none;text-align:center;" value="${raDeductionAmt }" readonly/></td><!-- deduction amt here -->
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">b)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Security Deposit(E)</span><span id="securityPer"></span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositCumulative" id="secDepositCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="secDepositPrevCerti" id="secDepositPrevCerti" style="border:none;text-align:center;" value="${secDepositPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti}" name="changedsecDepositCurrentCerti" id="changedsecDepositCurrentCerti" > <input type="text"  name="secDepositCurrentCerti" id="secDepositCurrentCerti"  autocomplete="off"  style="border:none;text-align:center;" value="${secDepositCurrentCerti}" readonly/></td>
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">c)</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Petty Expenses</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCumulative" id="pettyExpensesCumulative" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesPrevCerti" id="pettyExpensesPrevCerti" style="border:none;text-align:center;" value="${pettyExpensesPrevCerti }" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="pettyExpensesCurrentCerti" id="pettyExpensesCurrentCerti"  autocomplete="off"   style="border:none;text-align:center;" value="${pettyExpensesCurrentCerti }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Others</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtCumulative" id="otherAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="otherAmtPrevCerti" id="otherAmtPrevCerti"  style="border:none;text-align:center;" value="${otherAmtPrevCerti}" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="other" id="other"  autocomplete="off"  style="border:none;text-align:center;" value="${other }" readonly/></td>
            </tr>
             <tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">d)</td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span>Recovery</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;" ><input type="text"  name="cumulativeRecovery" class="CcAmnt" id="sumOfCumulativeRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center"  style="border:1px solid #000;padding:5px;" ><input type="text"  name="previousRecovery" class="PcAmnt" id="sumOfPreviousRecovery"  style="border:none;text-align:center;" value="0" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><span id="currentRecoveryAmount">${recoveryAmount}</span>   <input type="hidden" name="recoverycurrentAmount" id="recoverycurrentAmount" value="${recoveryAmount}"> <a class="print_hide" href="#" data-toggle="modal" data-target="#modal-recovery-click" >Click Here</a></td>
			 </tr>
			
           <tr style="font-size: 15px;" class="print-tnlrowcolor">
              <td class="text-center" style="border:1px solid #000;padding:5px;"></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Amount (B)</span></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulative" id="totalAmtCumulative"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtPrevCerti" id="totalAmtPrevCerti"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${secDepositCurrentCerti+raDeductionAmt}" name="actualTotalDeductAmt" id="actualTotalAmtCurrnt"> <input type="text" name="totalActualDeductAmt" id="totalAmtCurntDeduc"  style="border:none;text-align:center;" value="${secDepositCurrentCerti+raDeductionAmt+other+pettyExpensesCurrentCerti+recoveryAmount}" readonly/></td>
    		 </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">III</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Outstanding advance (F)</span></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text" name="outstandingAdvTotalAmt" id="outstandingAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvPrevAmt" id="outstandingAdvPrevAmt" style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text"  name="outstandingAdvCurrent" id=outstandingAdvCurrent  autocomplete="off"  style="border:none;text-align:center;" value="" readonly/></td>     
            </tr>
			<tr>
              <td class="text-center" style="border:1px solid #000;padding:5px;">IV</td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><span>Release advance (G)</span> </td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="hidden" name="actualreleaseAdvTotalAmt" id="actualreleaseAdvTotalAmt"><input type="text"  name="releaseAdvTotalAmt" id="releaseAdvTotalAmt"  style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center" style="border:1px solid #000;padding:5px;"><input type="text"  name="releaseAdvPrevAmt" id="releaseAdvPrevAmt"   style="border:none;text-align:center;" value="" readonly/></td>
              <td class="text-center input-outline" style="border:1px solid #000;padding:5px;"><input type="text" name="advanceCurrAmount" id="advanceCurrAmount"  autocomplete="off"  style="border:none;text-align:center;" value="" onfocusout="calculateAdvBillAmt(this.value)" readonly/></td>
            </tr>
			<tr>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
              <td style="border:1px solid #000;padding:15px;"></td>
            </tr>
			<tr style="font-size: 15px;" class="print-tnlrowcolor">
			 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Net Payable Amount (A - B + G) = C</span></td>
			 <td class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text"  name="totalAmtCumulativeCertified" id="totalAmtCumulativeCertified" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active" style="border:1px solid #000;padding:5px;"><input type="text" name="totalAmtPreviousCertified" id="totalAmtPreviousCertified" title="" style="border:none;text-align:center;" value="" readonly></td>
			 <td  class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><input type="hidden" value="${billBean.paybleAmt}" id="actualFinalAmt" name="actualFinalAmt">  <input type="text" name="finalAmt" id="finalAmt"  autocomplete="off"  style="border:none;text-align:center;" value="${billBean.paybleAmt}" readonly></td>
			</tr>
			<tr style="font-size: 15px;" class="print-tnlrowcolor">
			 <td colspan="2" class="text-center td-active input-outline" style="border:1px solid #000;padding:5px;"><span>Total Paid Amount In Words</span></td>
			 <td class="text-center td-active" colspan="3" id="amountInWords" style="border:1px solid #000;padding:5px;"></td>
			</tr>
			<tr class="payment-areaprint">
			<td colspan="2" class="text-center" style="border:1px solid #000;padding:5px;">Payment Area</td>
			<td colspan="3" class="text-center" style="border:1px solid #000;padding:5px;"><a class="certificate-link" href="javascript:void(0);" id="certificate-id"  data-toggle="modal" data-target="#modal-approverabill" >Abstract</a></td><!-- data-toggle="modal" data-target="#modal-showraadvbill" -->
			</tr>
			<tr>
			<!-- timeStamp Data in ${billBean.description1} variable with seperated by @@ symbol  -->
		<td colspan="5" class="text-center" style="border:1px solid #000;padding:5px;">
				 <div style="width:100%;height:50px;">
				<div style="width:33.3%;float:left;">
				 </div>
				 <div style="width:33.3%;float:left;">
				
				 </div>
				 <div style="width:33.3%;float:left;">
				
				 </div>
				 </div>
				 <div style="width:100%;">
				  <div style="width:33.3%;float:left;"><strong>QS</strong>
				 </div>
				 <div style="width:33.3%;float:left;"><strong>Project Manager</strong>
				 </div>
				 <div style="width:33.3%;float:left;"> <strong>Site Incharge</strong>
				 </div>
				 </div>
			</td>
			</tr>
          </tbody>
       