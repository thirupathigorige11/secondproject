package com.sumadhura.bean;

import java.io.Serializable;

import java.util.Comparator;

import com.sumadhura.dto.PriceMasterDTO;

public class ContractorQSBillBean implements Cloneable {

	private String siteId;
	private String siteName;
	private String siteNameForPrint;
	private String siteAddress;

	private String userId;
	private String approverEmpId;
	private String fromEmpId;
	private String fromuserName;
	private String toEmpId;
	private String toUserName;
	private String pendingEmpId;
	private String pendingDeptId;
	// For Contractor
	private String contractorId;
	private String contractorName;
	private String contractorAddress;
	private String contractorPhoneNo;
	private String contractorPanNo;
	private String contractorBankAccNumber;
	private String contractorBankName;
	private String contractorIFSCCode;
	private String GSTIN;
	private String fromDate;
	private String toDate;
	// For Bill
	private String billNo;
	private String oldBillNo;
	private String permanentBillNo;
	private String actualBillNumber;
	private String billInvoiceNumber;
	private String condition;
	private String tempBillNo;
	private String paymentTYpeOfWork1;
	private String typeOfWork;
	private String workOrderNo;
	private String oldWorkOrderNo;
	private String oldWorkOrderTotalAmount;
	private String workOrderIssueId;
	private String workOrderName;
	private String billDate;
	private String raBillNo;
	private String advanceBillAmt;
	private String previousAdvBillAmt;
	private String releaseAdvTotalAmt;
	private String releaseAdvPrevAmt;
	private String outstandingAdvPrevAmt;
	private String outstandingAdvTotalAmt;
	private String totalAmtCumulativeCertified;
	private String totalAmtPreviousCertified;
	private String paybleAmt;
	private String totalAmount;
	private String securityDepPer;
	//
	private String paymentType;
	private String entryDate;
	private String paymentReqDate;
	private String QSWorkOrderIssueId;
	private String status;
	private String remarks;
	private String isApprovePage;
	// For approve
	private String actualBillAmout;
	private String changedBillAmount;
	// For RA Bill
	private String advanceDeductionId;
	private String billWiseAdvanceDeductionAmt;
	private String raDeductionAmt;
	private String totalAmtToPay;
	private String secDepositCurrentCerti;
	private String pettyExpensesCurrentCerti;
	private String otherAmout;
	private String raCumulaticeCerti;
	private String raPrevCerti;
	private String totalCurrentCerti;
	private String totalCurrntDeducAmt;

	// for boq
	private String child_product_id;
	private String childProdName;
	private String measurement_id;
	private String recovery_amount;
	private String recovery_amount1;
	private String recovery_quantity;

	// for NMR Bill's
	private String tempWorkPMTDtlId;
	private String workDate1;
	private String description1;
	private String manualDesc1;
	private String majorHeadId1;
	private String minorHeadId1;
	private String workDescId1;
	private String mesurmentId1;
	private String woRowCode;

	private String majorHeadName1;
	private String minorHeadName1;
	private String workDescName1;
	private String mesurmentName1;
	private String rate1;
	private String amount1;
	private String fromTime1;
	private String toTime1;
	private String noOfHours1;
	private String noOfworkers1;
	private String workDays1;
	private String NMRRowIndex1;
	private String NMRBillBlocks;
	private String isSiteWiseStatusPage;
	private String isCommonApproval;
	
	@Override
	public Object clone() throws CloneNotSupportedException {

		return super.clone();
	}
	
	

	public String getFromDate() {
		return fromDate;
	}



	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}



	public String getToDate() {
		return toDate;
	}



	public void setToDate(String toDate) {
		this.toDate = toDate;
	}



	public String getBillInvoiceNumber() {
		return billInvoiceNumber;
	}



	public void setBillInvoiceNumber(String billInvoiceNumber) {
		this.billInvoiceNumber = billInvoiceNumber;
	}



	public String getBillWiseAdvanceDeductionAmt() {
		return billWiseAdvanceDeductionAmt;
	}



	public void setBillWiseAdvanceDeductionAmt(String billWiseAdvanceDeductionAmt) {
		this.billWiseAdvanceDeductionAmt = billWiseAdvanceDeductionAmt;
	}



	public String getAdvanceDeductionId() {
		return advanceDeductionId;
	}



	public void setAdvanceDeductionId(String advanceDeductionId) {
		this.advanceDeductionId = advanceDeductionId;
	}



	public String getIsSiteWiseStatusPage() {
		return isSiteWiseStatusPage;
	}



	public void setIsSiteWiseStatusPage(String isSiteWiseStatusPage) {
		this.isSiteWiseStatusPage = isSiteWiseStatusPage;
	}



	public String getIsCommonApproval() {
		return isCommonApproval;
	}



	public void setIsCommonApproval(String isCommonApproval) {
		this.isCommonApproval = isCommonApproval;
	}



	public String getWoRowCode() {
		return woRowCode;
	}

	public void setWoRowCode(String woRowCode) {
		this.woRowCode = woRowCode;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOldWorkOrderNo() {
		return oldWorkOrderNo;
	}

	public void setOldWorkOrderNo(String oldWorkOrderNo) {
		this.oldWorkOrderNo = oldWorkOrderNo;
	}

	public String getOldWorkOrderTotalAmount() {
		return oldWorkOrderTotalAmount;
	}

	public void setOldWorkOrderTotalAmount(String oldWorkOrderTotalAmount) {
		this.oldWorkOrderTotalAmount = oldWorkOrderTotalAmount;
	}

	public String getActualBillNumber() {
		return actualBillNumber;
	}

	public void setActualBillNumber(String actualBillNumber) {
		this.actualBillNumber = actualBillNumber;
	}

	public String getSiteNameForPrint() {
		return siteNameForPrint;
	}

	public void setSiteNameForPrint(String siteNameForPrint) {
		this.siteNameForPrint = siteNameForPrint;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getContractorIFSCCode() {
		return contractorIFSCCode;
	}

	public void setContractorIFSCCode(String contractorIFSCCode) {
		this.contractorIFSCCode = contractorIFSCCode;
	}

	public String getContractorBankAccNumber() {
		return contractorBankAccNumber;
	}

	public void setContractorBankAccNumber(String contractorBankAccNumber) {
		this.contractorBankAccNumber = contractorBankAccNumber;
	}

	public String getContractorBankName() {
		return contractorBankName;
	}

	public void setContractorBankName(String contractorBankName) {
		this.contractorBankName = contractorBankName;
	}

	public String getNMRBillBlocks() {
		return NMRBillBlocks;
	}

	public void setNMRBillBlocks(String nMRBillBlocks) {
		NMRBillBlocks = nMRBillBlocks;
	}

	public String getNMRRowIndex1() {
		return NMRRowIndex1;
	}

	public void setNMRRowIndex1(String nMRRowIndex1) {
		NMRRowIndex1 = nMRRowIndex1;
	}

	public String getPaymentTYpeOfWork1() {
		return paymentTYpeOfWork1;
	}

	public void setPaymentTYpeOfWork1(String paymentTYpeOfWork1) {
		this.paymentTYpeOfWork1 = paymentTYpeOfWork1;
	}

	public String getTempWorkPMTDtlId() {
		return tempWorkPMTDtlId;
	}

	public void setTempWorkPMTDtlId(String tempWorkPMTDtlId) {
		this.tempWorkPMTDtlId = tempWorkPMTDtlId;
	}

	public String getRate1() {
		return rate1;
	}

	public void setRate1(String rate1) {
		this.rate1 = rate1;
	}

	public String getAmount1() {
		return amount1;
	}

	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}

	public String getWorkDate1() {
		return workDate1;
	}

	public void setWorkDate1(String workDate1) {
		this.workDate1 = workDate1;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getManualDesc1() {
		return manualDesc1;
	}

	public void setManualDesc1(String manualDesc1) {
		this.manualDesc1 = manualDesc1;
	}

	public String getMajorHeadId1() {
		return majorHeadId1;
	}

	public void setMajorHeadId1(String majorHeadId1) {
		this.majorHeadId1 = majorHeadId1;
	}

	public String getMinorHeadId1() {
		return minorHeadId1;
	}

	public void setMinorHeadId1(String minorHeadId1) {
		this.minorHeadId1 = minorHeadId1;
	}

	public String getWorkDescId1() {
		return workDescId1;
	}

	public void setWorkDescId1(String workDescId1) {
		this.workDescId1 = workDescId1;
	}

	public String getMesurmentId1() {
		return mesurmentId1;
	}

	public void setMesurmentId1(String mesurmentId1) {
		this.mesurmentId1 = mesurmentId1;
	}

	public String getMajorHeadName1() {
		return majorHeadName1;
	}

	public void setMajorHeadName1(String majorHeadName1) {
		this.majorHeadName1 = majorHeadName1;
	}

	public String getMinorHeadName1() {
		return minorHeadName1;
	}

	public void setMinorHeadName1(String minorHeadName1) {
		this.minorHeadName1 = minorHeadName1;
	}

	public String getWorkDescName1() {
		return workDescName1;
	}

	public void setWorkDescName1(String workDescName1) {
		this.workDescName1 = workDescName1;
	}

	public String getMesurmentName1() {
		return mesurmentName1;
	}

	public void setMesurmentName1(String mesurmentName1) {
		this.mesurmentName1 = mesurmentName1;
	}

	public String getFromTime1() {
		return fromTime1;
	}

	public void setFromTime1(String fromTime1) {
		this.fromTime1 = fromTime1;
	}

	public String getToTime1() {
		return toTime1;
	}

	public void setToTime1(String toTime1) {
		this.toTime1 = toTime1;
	}

	public String getNoOfHours1() {
		return noOfHours1;
	}

	public void setNoOfHours1(String noOfHours1) {
		this.noOfHours1 = noOfHours1;
	}

	public String getNoOfworkers1() {
		return noOfworkers1;
	}

	public void setNoOfworkers1(String noOfworkers1) {
		this.noOfworkers1 = noOfworkers1;
	}

	public String getWorkDays1() {
		return workDays1;
	}

	public void setWorkDays1(String workDays1) {
		this.workDays1 = workDays1;
	}

	public String getPermanentBillNo() {
		return permanentBillNo;
	}

	public void setPermanentBillNo(String permanentBillNo) {
		this.permanentBillNo = permanentBillNo;
	}

	public String getRecovery_amount1() {
		return recovery_amount1;
	}

	public void setRecovery_amount1(String recovery_amount1) {
		this.recovery_amount1 = recovery_amount1;
	}

	public String getIsApprovePage() {
		return isApprovePage;
	}

	public void setIsApprovePage(String isApprovePage) {
		this.isApprovePage = isApprovePage;
	}

	public String getRecovery_quantity() {
		return recovery_quantity;
	}

	public void setRecovery_quantity(String recovery_quantity) {
		this.recovery_quantity = recovery_quantity;
	}

	public String getChild_product_id() {
		return child_product_id;
	}

	public void setChild_product_id(String child_product_id) {
		this.child_product_id = child_product_id;
	}

	public String getChildProdName() {
		return childProdName;
	}

	public void setChildProdName(String childProdName) {
		this.childProdName = childProdName;
	}

	public String getMeasurement_id() {
		return measurement_id;
	}

	public void setMeasurement_id(String measurement_id) {
		this.measurement_id = measurement_id;
	}

	public String getRecovery_amount() {
		return recovery_amount;
	}

	public void setRecovery_amount(String recovery_amount) {
		this.recovery_amount = recovery_amount;
	}

	public String getWorkOrderIssueId() {
		return workOrderIssueId;
	}

	public void setWorkOrderIssueId(String workOrderIssueId) {
		this.workOrderIssueId = workOrderIssueId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOldBillNo() {
		return oldBillNo;
	}

	public void setOldBillNo(String oldBillNo) {
		this.oldBillNo = oldBillNo;
	}

	public String getBillNo() {
		return billNo;
	}

	/**
	 * @param billNo
	 *            the billNo to set
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	/**
	 * @return the workOrderName
	 */
	public String getWorkOrderName() {
		return workOrderName;
	}

	/**
	 * @param workOrderName
	 *            the workOrderName to set
	 */
	public void setWorkOrderName(String workOrderName) {
		this.workOrderName = workOrderName;
	}

	/**
	 * @return the securityDepPer
	 */
	public String getSecurityDepPer() {
		return securityDepPer;
	}

	/**
	 * @param securityDepPer
	 *            the securityDepPer to set
	 */
	public void setSecurityDepPer(String securityDepPer) {
		this.securityDepPer = securityDepPer;
	}

	public String getTotalCurrntDeducAmt() {
		return totalCurrntDeducAmt;
	}

	public void setTotalCurrntDeducAmt(String totalCurrntDeducAmt) {
		this.totalCurrntDeducAmt = totalCurrntDeducAmt;
	}

	public String getTotalCurrentCerti() {
		return totalCurrentCerti;
	}

	public void setTotalCurrentCerti(String totalCurrentCerti) {
		this.totalCurrentCerti = totalCurrentCerti;
	}

	public String getSecDepositCurrentCerti() {
		return secDepositCurrentCerti;
	}

	public void setSecDepositCurrentCerti(String secDepositCurrentCerti) {
		this.secDepositCurrentCerti = secDepositCurrentCerti;
	}

	public String getPettyExpensesCurrentCerti() {
		return pettyExpensesCurrentCerti;
	}

	public void setPettyExpensesCurrentCerti(String pettyExpensesCurrentCerti) {
		this.pettyExpensesCurrentCerti = pettyExpensesCurrentCerti;
	}

	public String getOtherAmout() {
		return otherAmout;
	}

	public void setOtherAmout(String otherAmout) {
		this.otherAmout = otherAmout;
	}

	public String getRaCumulaticeCerti() {
		return raCumulaticeCerti;
	}

	public void setRaCumulaticeCerti(String raCumulaticeCerti) {
		this.raCumulaticeCerti = raCumulaticeCerti;
	}

	public String getRaPrevCerti() {
		return raPrevCerti;
	}

	public void setRaPrevCerti(String raPrevCerti) {
		this.raPrevCerti = raPrevCerti;
	}

	public String getActualBillAmout() {
		return actualBillAmout;
	}

	public void setActualBillAmout(String actualBillAmout) {
		this.actualBillAmout = actualBillAmout;
	}

	public String getChangedBillAmount() {
		return changedBillAmount;
	}

	public void setChangedBillAmount(String changedBillAmount) {
		this.changedBillAmount = changedBillAmount;
	}

	public String getRaDeductionAmt() {
		return raDeductionAmt;
	}

	public void setRaDeductionAmt(String raDeductionAmt) {
		this.raDeductionAmt = raDeductionAmt;
	}

	public String getTotalAmtToPay() {
		return totalAmtToPay;
	}

	public void setTotalAmtToPay(String totalAmtToPay) {
		this.totalAmtToPay = totalAmtToPay;
	}

	public String getPendingEmpId() {
		return pendingEmpId;
	}

	public void setPendingEmpId(String pendingEmpId) {
		this.pendingEmpId = pendingEmpId;
	}

	public String getPendingDeptId() {
		return pendingDeptId;
	}

	public void setPendingDeptId(String pendingDeptId) {
		this.pendingDeptId = pendingDeptId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getApproverEmpId() {
		return approverEmpId;
	}

	public void setApproverEmpId(String approverEmpId) {
		this.approverEmpId = approverEmpId;
	}

	public String getFromEmpId() {
		return fromEmpId;
	}

	public void setFromEmpId(String fromEmpId) {
		this.fromEmpId = fromEmpId;
	}

	public String getFromuserName() {
		return fromuserName;
	}

	public void setFromuserName(String fromuserName) {
		this.fromuserName = fromuserName;
	}

	public String getToEmpId() {
		return toEmpId;
	}

	public void setToEmpId(String toEmpId) {
		this.toEmpId = toEmpId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getContractorId() {
		return contractorId;
	}

	public void setContractorId(String contractorId) {
		this.contractorId = contractorId;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getContractorAddress() {
		return contractorAddress;
	}

	public void setContractorAddress(String contractorAddress) {
		this.contractorAddress = contractorAddress;
	}

	public String getContractorPhoneNo() {
		return contractorPhoneNo;
	}

	public void setContractorPhoneNo(String contractorPhoneNo) {
		this.contractorPhoneNo = contractorPhoneNo;
	}

	public String getContractorPanNo() {
		return contractorPanNo;
	}

	public void setContractorPanNo(String contractorPanNo) {
		this.contractorPanNo = contractorPanNo;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}

	public String getTempBillNo() {
		return tempBillNo;
	}

	public void setTempBillNo(String tempBillNo) {
		this.tempBillNo = tempBillNo;
	}

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getRaBillNo() {
		return raBillNo;
	}

	public void setRaBillNo(String raBillNo) {
		this.raBillNo = raBillNo;
	}

	public String getAdvanceBillAmt() {
		return advanceBillAmt;
	}

	public void setAdvanceBillAmt(String advanceBillAmt) {
		this.advanceBillAmt = advanceBillAmt;
	}

	public String getPreviousAdvBillAmt() {
		return previousAdvBillAmt;
	}

	public void setPreviousAdvBillAmt(String previousAdvBillAmt) {
		this.previousAdvBillAmt = previousAdvBillAmt;
	}

	public String getReleaseAdvTotalAmt() {
		return releaseAdvTotalAmt;
	}

	public void setReleaseAdvTotalAmt(String releaseAdvTotalAmt) {
		this.releaseAdvTotalAmt = releaseAdvTotalAmt;
	}

	public String getReleaseAdvPrevAmt() {
		return releaseAdvPrevAmt;
	}

	public void setReleaseAdvPrevAmt(String releaseAdvPrevAmt) {
		this.releaseAdvPrevAmt = releaseAdvPrevAmt;
	}

	public String getOutstandingAdvPrevAmt() {
		return outstandingAdvPrevAmt;
	}

	public void setOutstandingAdvPrevAmt(String outstandingAdvPrevAmt) {
		this.outstandingAdvPrevAmt = outstandingAdvPrevAmt;
	}

	public String getOutstandingAdvTotalAmt() {
		return outstandingAdvTotalAmt;
	}

	public void setOutstandingAdvTotalAmt(String outstandingAdvTotalAmt) {
		this.outstandingAdvTotalAmt = outstandingAdvTotalAmt;
	}

	public String getTotalAmtCumulativeCertified() {
		return totalAmtCumulativeCertified;
	}

	public void setTotalAmtCumulativeCertified(String totalAmtCumulativeCertified) {
		this.totalAmtCumulativeCertified = totalAmtCumulativeCertified;
	}

	public String getTotalAmtPreviousCertified() {
		return totalAmtPreviousCertified;
	}

	public void setTotalAmtPreviousCertified(String totalAmtPreviousCertified) {
		this.totalAmtPreviousCertified = totalAmtPreviousCertified;
	}

	public String getPaybleAmt() {
		return paybleAmt;
	}

	public void setPaybleAmt(String paybleAmt) {
		this.paybleAmt = paybleAmt;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getPaymentReqDate() {
		return paymentReqDate;
	}

	public void setPaymentReqDate(String paymentReqDate) {
		this.paymentReqDate = paymentReqDate;
	}

	public String getQSWorkOrderIssueId() {
		return QSWorkOrderIssueId;
	}

	public void setQSWorkOrderIssueId(String qSWorkOrderIssueId) {
		QSWorkOrderIssueId = qSWorkOrderIssueId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "";
	}

}
