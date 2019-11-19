package com.sumadhura.bean;

public class PaymentBean {

	private String strInvoiceNo ;
	private double doublePaymentIntiateAmount ;
	private String strPaymentIntiateType ;
	private String strVendorId ;
	public String getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(String balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	private String strPaymentReqDate ;
	private double doubleAmountToBeReleased ;
	private  String strRemarks ;
	private String strInvoiceDate;
	/*private int intPaymentNo;
	private int intPaymentSeqNo;*/
	private int intPaymentId;
	private double doubleInvoiceAmount;
	private String strSiteId;
	private double doublePaymentDoneUpto;
	private double doublePaymentRequestedUpto;
	private String strDCNo;
	private String strPONo;
	private String strApproverEmpId;
	private String strPendingEmpId;
	private String strPendingDeptId;
	private String strEmployeeId;
	private int intSerialNo;
	private String strVendorName;
	private String strReceiveDate;
	private String strPODate;
	private String strSiteName;
	//private int strIndentEntryId;
	private int intIndentEntryId;
	private String requestedAmount;
	private String requestedDate;
	private String actualRequestedAmount;
	private String actualRequestedDate;
	private String strComments;
	private int intTempTransactionId;
	//private int intAccDeptPaymentProcessSeqNo;
	private int intAccDeptPaymentProcessId;
	private String strRefrenceNo;
	private int intSiteWisePaymentId;
	private double doublePOTotalAmount;
	private String paymentType;
	private String paymentMode;
	private String paymentModeName;
	private String utrChequeNo;
	private int intPaymentDetailsId;
	//private String paymentId;
	//private String paymentDetailsId;
	private String strDCDate;
	private String strInvoiceReceivedDate;
	private String strInvoiceNoInAP;
	private String paymentModeDisplayName;
	private int intTempPaymentTransactionId;
	private double actualDoubleAdjustAmountFromAdvance;
	private double doublePaidAmount;
	private String strCreatedDate;
	private String strPendingDeptName;
	private String strPendingEmpName;
	private double doubleTransactionAmount;
	private double doubleCreditTotalAmount;
	private double doubleSecurityDeposit;//doubleAdvancePayment
	private String strPaymentRequestReceivedDate;
	private String strCreditNoteNumber;
	private String groupOfPaymentDetailsId;
	private String reqAmountAsGroup;
	private double doublePOAdvancePayment;
	private double doubleAdjustAmountFromAdvance;
	private boolean vendorHeader;
	private String strPaymentDate ;
	private String actualPaymentMode;
	private String actualUtrChequeNo;
	private int vendorGroupSerialNo;
	private String requestReceiveFrom;
	private int intPaymentTransactionId;
	private String status;
	private double doubleBalanceAmount;
	private String strRemarksForView;
	private int intNoofPaymentsVendorWise;
	private String invoiceImage0;
	private String invoiceImage1;
	private String invoiceImage2;
	private String invoiceImage3;
	private boolean hasImage;
		private String strInvoiceAmount;//this for jsp purpose to print crores
	public String getStrInvoiceAmount() {
			return strInvoiceAmount;
		}
		public void setStrInvoiceAmount(String strInvoiceAmount) {
			this.strInvoiceAmount = strInvoiceAmount;
		}
		public String getStrPOAmount() {
			return strPOAmount;
		}
		public void setStrPOAmount(String strPOAmount) {
			this.strPOAmount = strPOAmount;
		}
	private String strPOAmount;//this for jsp purpose to print crores

	
	
private String paymentReqAmt;
	
	private String ReqDeptId;
	private String ReqEmpId;
	private String particular;
	private String payentReqdate;
	//private String secBillNo;
	private String userId;
	
	private int contractorGroupSerialNo;
	private String strContractorId;
	private String strContractorName;
	private boolean contractorHeader;
	private String workOrderNo;
	private String raBillNo;
	private double doubleWorkOrderAmount;
	private double doubleRaBillAmount;
	private int intCntPaymentId;
	private String strBillId;
	private String strTempBillId;
	private String strRaBillDate;
	private String strWorkOrderDate;
	private String strAdvBillNo;
	private String strAdvBillDate;
	private double doubleAdvBillAmount;
	private String strSdBillNo;
	private String strSdBillDate;
	private double doubleSdBillAmount;
	private double doubleDeductionAmount;
	private String strRemarksForTitle;
	private String balanceAmt;//this for jsp purpose to print crores
private String isVNDorCNT;
	private java.util.Date paymentDate;
	private String qsWorkorderIssueId;
	private String billNumber;
	private double doubleBillAmount;
	private String strBillDate;
	private String strPoEntryId;
	private String payBalanceInPo;
	private String paymentDoneOnMultipleInvoices;
	private String paymentRequestedOnPO;
	private String adjustedAmountFromPo;
	private String taxInvoiceStatus;
	
	private String amountToBeReleased_WithCommas;
	private String invoiceAmount_WithCommas;
	private String poAmount_WithCommas;
	private String paymentDoneUpto_WithCommas;
	private String paymentRequestedUpto_WithCommas;
	private String requestedAmount_WithCommas;
	private String invoiceTotalAmount_WithCommas;
	private String poTotalAmount_WithCommas;
	private String paidAmount_WithCommas;
	private String transactionAmount_WithCommas;
	private String creditTotalAmount_WithCommas;
	private String securityDeposit_WithCommas;
	private String poAdvancePayment_WithCommas;
	private String adjustAmountFromAdvance_WithCommas;
	private String balanceAmount_WithCommas;
	private String vendorInvoiceTotalAmount_WithCommas;
	
	
	
	
	
	
	
	public String getVendorInvoiceTotalAmount_WithCommas() {
		return vendorInvoiceTotalAmount_WithCommas;
	}
	public void setVendorInvoiceTotalAmount_WithCommas(String vendorInvoiceTotalAmount_WithCommas) {
		this.vendorInvoiceTotalAmount_WithCommas = vendorInvoiceTotalAmount_WithCommas;
	}
	public String getTaxInvoiceStatus() {
		return taxInvoiceStatus;
	}
	public void setTaxInvoiceStatus(String taxInvoiceStatus) {
		this.taxInvoiceStatus = taxInvoiceStatus;
	}
	public String getPaymentRequestedOnPO() {
		return paymentRequestedOnPO;
	}
	public void setPaymentRequestedOnPO(String paymentRequestedOnPO) {
		this.paymentRequestedOnPO = paymentRequestedOnPO;
	}
	public String getAdjustedAmountFromPo() {
		return adjustedAmountFromPo;
	}
	public void setAdjustedAmountFromPo(String adjustedAmountFromPo) {
		this.adjustedAmountFromPo = adjustedAmountFromPo;
	}
	public String getPaymentDoneOnMultipleInvoices() {
		return paymentDoneOnMultipleInvoices;
	}
	public void setPaymentDoneOnMultipleInvoices(String paymentDoneOnMultipleInvoices) {
		this.paymentDoneOnMultipleInvoices = paymentDoneOnMultipleInvoices;
	}
	public String getPayBalanceInPo() {
		return payBalanceInPo;
	}
	public void setPayBalanceInPo(String payBalanceInPo) {
		this.payBalanceInPo = payBalanceInPo;
	}
	public String getPoAmount_WithCommas() {
		return poAmount_WithCommas;
	}
	public void setPoAmount_WithCommas(String poAmount_WithCommas) {
		this.poAmount_WithCommas = poAmount_WithCommas;
	}
	public String getInvoiceTotalAmount_WithCommas() {
		return invoiceTotalAmount_WithCommas;
	}
	public void setInvoiceTotalAmount_WithCommas(String invoiceTotalAmount_WithCommas) {
		this.invoiceTotalAmount_WithCommas = invoiceTotalAmount_WithCommas;
	}
		public String getAmountToBeReleased_WithCommas() {
		return amountToBeReleased_WithCommas;
	}
	public void setAmountToBeReleased_WithCommas(String amountToBeReleased_WithCommas) {
		this.amountToBeReleased_WithCommas = amountToBeReleased_WithCommas;
	}
	public String getInvoiceAmount_WithCommas() {
		return invoiceAmount_WithCommas;
	}
	public void setInvoiceAmount_WithCommas(String invoiceAmount_WithCommas) {
		this.invoiceAmount_WithCommas = invoiceAmount_WithCommas;
	}
	public String getPaymentDoneUpto_WithCommas() {
		return paymentDoneUpto_WithCommas;
	}
	public void setPaymentDoneUpto_WithCommas(String paymentDoneUpto_WithCommas) {
		this.paymentDoneUpto_WithCommas = paymentDoneUpto_WithCommas;
	}
	public String getPaymentRequestedUpto_WithCommas() {
		return paymentRequestedUpto_WithCommas;
	}
	public void setPaymentRequestedUpto_WithCommas(String paymentRequestedUpto_WithCommas) {
		this.paymentRequestedUpto_WithCommas = paymentRequestedUpto_WithCommas;
	}
	public String getRequestedAmount_WithCommas() {
		return requestedAmount_WithCommas;
	}
	public void setRequestedAmount_WithCommas(String requestedAmount_WithCommas) {
		this.requestedAmount_WithCommas = requestedAmount_WithCommas;
	}
	public String getPoTotalAmount_WithCommas() {
		return poTotalAmount_WithCommas;
	}
	public void setPoTotalAmount_WithCommas(String poTotalAmount_WithCommas) {
		this.poTotalAmount_WithCommas = poTotalAmount_WithCommas;
	}
	public String getPaidAmount_WithCommas() {
		return paidAmount_WithCommas;
	}
	public void setPaidAmount_WithCommas(String paidAmount_WithCommas) {
		this.paidAmount_WithCommas = paidAmount_WithCommas;
	}
	public String getTransactionAmount_WithCommas() {
		return transactionAmount_WithCommas;
	}
	public void setTransactionAmount_WithCommas(String transactionAmount_WithCommas) {
		this.transactionAmount_WithCommas = transactionAmount_WithCommas;
	}
	public String getCreditTotalAmount_WithCommas() {
		return creditTotalAmount_WithCommas;
	}
	public void setCreditTotalAmount_WithCommas(String creditTotalAmount_WithCommas) {
		this.creditTotalAmount_WithCommas = creditTotalAmount_WithCommas;
	}
	public String getSecurityDeposit_WithCommas() {
		return securityDeposit_WithCommas;
	}
	public void setSecurityDeposit_WithCommas(String securityDeposit_WithCommas) {
		this.securityDeposit_WithCommas = securityDeposit_WithCommas;
	}
	public String getPoAdvancePayment_WithCommas() {
		return poAdvancePayment_WithCommas;
	}
	public void setPoAdvancePayment_WithCommas(String poAdvancePayment_WithCommas) {
		this.poAdvancePayment_WithCommas = poAdvancePayment_WithCommas;
	}
	public String getAdjustAmountFromAdvance_WithCommas() {
		return adjustAmountFromAdvance_WithCommas;
	}
	public void setAdjustAmountFromAdvance_WithCommas(String adjustAmountFromAdvance_WithCommas) {
		this.adjustAmountFromAdvance_WithCommas = adjustAmountFromAdvance_WithCommas;
	}
	public String getBalanceAmount_WithCommas() {
		return balanceAmount_WithCommas;
	}
	public void setBalanceAmount_WithCommas(String balanceAmount_WithCommas) {
		this.balanceAmount_WithCommas = balanceAmount_WithCommas;
	}
	
	public String getStrPoEntryId() {
		return strPoEntryId;
	}
	public void setStrPoEntryId(String strPoEntryId) {
		this.strPoEntryId = strPoEntryId;
	}
	public String getStrBillDate() {
		return strBillDate;
	}
	public void setStrBillDate(String strBillDate) {
		this.strBillDate = strBillDate;
	}
	public double getDoubleBillAmount() {
		return doubleBillAmount;
	}
	public void setDoubleBillAmount(double doubleBillAmount) {
		this.doubleBillAmount = doubleBillAmount;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getQsWorkorderIssueId() {
		return qsWorkorderIssueId;
	}
	public void setQsWorkorderIssueId(String qsWorkorderIssueId) {
		this.qsWorkorderIssueId = qsWorkorderIssueId;
	}
	public java.util.Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(java.util.Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getIsVNDorCNT() {
		return isVNDorCNT;
	}
	public void setIsVNDorCNT(String isVNDorCNT) {
		this.isVNDorCNT = isVNDorCNT;
	}
	
	public String getPaymentReqAmt() {
		return paymentReqAmt;
	}
	public void setPaymentReqAmt(String paymentReqAmt) {
		this.paymentReqAmt = paymentReqAmt;
	}
	public String getReqDeptId() {
		return ReqDeptId;
	}
	public void setReqDeptId(String reqDeptId) {
		ReqDeptId = reqDeptId;
	}
	public String getReqEmpId() {
		return ReqEmpId;
	}
	public void setReqEmpId(String reqEmpId) {
		ReqEmpId = reqEmpId;
	}
	public String getParticular() {
		return particular;
	}
	public void setParticular(String particular) {
		this.particular = particular;
	}
	public String getPayentReqdate() {
		return payentReqdate;
	}
	public void setPayentReqdate(String payentReqdate) {
		this.payentReqdate = payentReqdate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStrTempBillId() {
		return strTempBillId;
	}
	public void setStrTempBillId(String strTempBillId) {
		this.strTempBillId = strTempBillId;
	}
	public String getStrPendingDeptId() {
		return strPendingDeptId;
	}
	public void setStrPendingDeptId(String strPendingDeptId) {
		this.strPendingDeptId = strPendingDeptId;
	}
	public String getStrRemarksForTitle() {
		return strRemarksForTitle;
	}
	public void setStrRemarksForTitle(String strRemarksForTitle) {
		this.strRemarksForTitle = strRemarksForTitle;
	}
	public String getStrSdBillNo() {
		return strSdBillNo;
	}
	public void setStrSdBillNo(String strSdBillNo) {
		this.strSdBillNo = strSdBillNo;
	}
	public String getStrSdBillDate() {
		return strSdBillDate;
	}
	public void setStrSdBillDate(String strSdBillDate) {
		this.strSdBillDate = strSdBillDate;
	}
	public double getDoubleSdBillAmount() {
		return doubleSdBillAmount;
	}
	public void setDoubleSdBillAmount(double doubleSdBillAmount) {
		this.doubleSdBillAmount = doubleSdBillAmount;
	}
	public double getDoubleDeductionAmount() {
		return doubleDeductionAmount;
	}
	public void setDoubleDeductionAmount(double doubleDeductionAmount) {
		this.doubleDeductionAmount = doubleDeductionAmount;
	}
	public String getStrAdvBillNo() {
		return strAdvBillNo;
	}
	public void setStrAdvBillNo(String strAdvBillNo) {
		this.strAdvBillNo = strAdvBillNo;
	}
	public String getStrAdvBillDate() {
		return strAdvBillDate;
	}
	public void setStrAdvBillDate(String strAdvBillDate) {
		this.strAdvBillDate = strAdvBillDate;
	}
	public double getDoubleAdvBillAmount() {
		return doubleAdvBillAmount;
	}
	public void setDoubleAdvBillAmount(double doubleAdvBillAmount) {
		this.doubleAdvBillAmount = doubleAdvBillAmount;
	}
	
	public String getStrRaBillDate() {
		return strRaBillDate;
	}
	public void setStrRaBillDate(String strRaBillDate) {
		this.strRaBillDate = strRaBillDate;
	}
	public String getStrWorkOrderDate() {
		return strWorkOrderDate;
	}
	public void setStrWorkOrderDate(String strWorkOrderDate) {
		this.strWorkOrderDate = strWorkOrderDate;
	}
	public String getStrBillId() {
		return strBillId;
	}
	public void setStrBillId(String strBillId) {
		this.strBillId = strBillId;
	}
	public int getIntCntPaymentId() {
		return intCntPaymentId;
	}
	public void setIntCntPaymentId(int intCntPaymentId) {
		this.intCntPaymentId = intCntPaymentId;
	}
	public double getDoubleWorkOrderAmount() {
		return doubleWorkOrderAmount;
	}
	public void setDoubleWorkOrderAmount(double doubleWorkOrderAmount) {
		this.doubleWorkOrderAmount = doubleWorkOrderAmount;
	}
	public double getDoubleRaBillAmount() {
		return doubleRaBillAmount;
	}
	public void setDoubleRaBillAmount(double doubleRaBillAmount) {
		this.doubleRaBillAmount = doubleRaBillAmount;
	}
	public String getWorkOrderNo() {
		return workOrderNo;
	}
	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}
	public String getRaBillNo() {
		return raBillNo;
	}
	public void setRaBillNo(String raBillNo) {
		this.raBillNo = raBillNo;
	}
	public boolean isContractorHeader() {
		return contractorHeader;
	}
	public void setContractorHeader(boolean contractorHeader) {
		this.contractorHeader = contractorHeader;
	}
	public int getContractorGroupSerialNo() {
		return contractorGroupSerialNo;
	}
	public void setContractorGroupSerialNo(int contractorGroupSerialNo) {
		this.contractorGroupSerialNo = contractorGroupSerialNo;
	}
	public String getStrContractorId() {
		return strContractorId;
	}
	public void setStrContractorId(String strContractorId) {
		this.strContractorId = strContractorId;
	}
	public String getStrContractorName() {
		return strContractorName;
	}
	public void setStrContractorName(String strContractorName) {
		this.strContractorName = strContractorName;
	}
	public boolean isHasImage() {
		return hasImage;
	}
	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}
	public String getInvoiceImage1() {
		return invoiceImage1;
	}
	public void setInvoiceImage1(String invoiceImage1) {
		this.invoiceImage1 = invoiceImage1;
	}
	public String getInvoiceImage2() {
		return invoiceImage2;
	}
	public void setInvoiceImage2(String invoiceImage2) {
		this.invoiceImage2 = invoiceImage2;
	}
	public String getInvoiceImage3() {
		return invoiceImage3;
	}
	public void setInvoiceImage3(String invoiceImage3) {
		this.invoiceImage3 = invoiceImage3;
	}
	public String getInvoiceImage0() {
		return invoiceImage0;
	}
	public void setInvoiceImage0(String invoiceImage0) {
		this.invoiceImage0 = invoiceImage0;
	}
	public String getPaymentModeName() {
		return paymentModeName;
	}
	public void setPaymentModeName(String paymentModeName) {
		this.paymentModeName = paymentModeName;
	}
	public int getIntNoofPaymentsVendorWise() {
		return intNoofPaymentsVendorWise;
	}
	public void setIntNoofPaymentsVendorWise(int intNoofPaymentsVendorWise) {
		this.intNoofPaymentsVendorWise = intNoofPaymentsVendorWise;
	}
	public String getStrRemarksForView() {
		return strRemarksForView;
	}
	public void setStrRemarksForView(String strRemarksForView) {
		this.strRemarksForView = strRemarksForView;
	}
	public double getDoubleBalanceAmount() {
		return doubleBalanceAmount;
	}
	public void setDoubleBalanceAmount(double doubleBalanceAmount) {
		this.doubleBalanceAmount = doubleBalanceAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getIntPaymentTransactionId() {
		return intPaymentTransactionId;
	}
	public void setIntPaymentTransactionId(int intPaymentTransactionId) {
		this.intPaymentTransactionId = intPaymentTransactionId;
	}
	public String getRequestReceiveFrom() {
		return requestReceiveFrom;
	}
	public void setRequestReceiveFrom(String requestReceiveFrom) {
		this.requestReceiveFrom = requestReceiveFrom;
	}
	public int getVendorGroupSerialNo() {
		return vendorGroupSerialNo;
	}
	public void setVendorGroupSerialNo(int vendorGroupSerialNo) {
		this.vendorGroupSerialNo = vendorGroupSerialNo;
	}
	public String getActualUtrChequeNo() {
		return actualUtrChequeNo;
	}
	public void setActualUtrChequeNo(String actualUtrChequeNo) {
		this.actualUtrChequeNo = actualUtrChequeNo;
	}
	public String getActualPaymentMode() {
		return actualPaymentMode;
	}
	public void setActualPaymentMode(String actualPaymentMode) {
		this.actualPaymentMode = actualPaymentMode;
	}
	public String getStrPaymentDate() {
		return strPaymentDate;
	}
	public void setStrPaymentDate(String strPaymentDate) {
		this.strPaymentDate = strPaymentDate;
	}
	public boolean isVendorHeader() {
		return vendorHeader;
	}
	public void setVendorHeader(boolean vendorHeader) {
		this.vendorHeader = vendorHeader;
	}
	public double getActualDoubleAdjustAmountFromAdvance() {
		return actualDoubleAdjustAmountFromAdvance;
	}
	public void setActualDoubleAdjustAmountFromAdvance(double actualDoubleAdjustAmountFromAdvance) {
		this.actualDoubleAdjustAmountFromAdvance = actualDoubleAdjustAmountFromAdvance;
	}
	public double getDoublePOAdvancePayment() {
		return doublePOAdvancePayment;
	}
	public void setDoublePOAdvancePayment(double doublePOAdvancePayment) {
		this.doublePOAdvancePayment = doublePOAdvancePayment;
	}
	public double getDoubleAdjustAmountFromAdvance() {
		return doubleAdjustAmountFromAdvance;
	}
	public void setDoubleAdjustAmountFromAdvance(double doubleAdjustAmountFromAdvance) {
		this.doubleAdjustAmountFromAdvance = doubleAdjustAmountFromAdvance;
	}
	public String getReqAmountAsGroup() {
		return reqAmountAsGroup;
	}
	public void setReqAmountAsGroup(String reqAmountAsGroup) {
		this.reqAmountAsGroup = reqAmountAsGroup;
	}
	public String getGroupOfPaymentDetailsId() {
		return groupOfPaymentDetailsId;
	}
	public void setGroupOfPaymentDetailsId(String groupOfPaymentDetailsId) {
		this.groupOfPaymentDetailsId = groupOfPaymentDetailsId;
	}
	public String getStrCreditNoteNumber() {
		return strCreditNoteNumber;
	}
	public void setStrCreditNoteNumber(String strCreditNoteNumber) {
		this.strCreditNoteNumber = strCreditNoteNumber;
	}
	public String getStrPaymentRequestReceivedDate() {
		return strPaymentRequestReceivedDate;
	}
	public void setStrPaymentRequestReceivedDate(String strPaymentRequestReceivedDate) {
		this.strPaymentRequestReceivedDate = strPaymentRequestReceivedDate;
	}
	public double getDoubleSecurityDeposit() {
		return doubleSecurityDeposit;
	}
	public void setDoubleSecurityDeposit(double doubleSecurityDeposit) {
		this.doubleSecurityDeposit = doubleSecurityDeposit;
	}
	public int getIntIndentEntryId() {
		return intIndentEntryId;
	}
	public void setIntIndentEntryId(int intIndentEntryId) {
		this.intIndentEntryId = intIndentEntryId;
	}
	
	public double getDoubleCreditTotalAmount() {
		return doubleCreditTotalAmount;
	}
	public void setDoubleCreditTotalAmount(double doubleCreditTotalAmount) {
		this.doubleCreditTotalAmount = doubleCreditTotalAmount;
	}
	public String getStrPendingDeptName() {
		return strPendingDeptName;
	}
	public void setStrPendingDeptName(String strPendingDeptName) {
		this.strPendingDeptName = strPendingDeptName;
	}
	public String getStrPendingEmpName() {
		return strPendingEmpName;
	}
	public void setStrPendingEmpName(String strPendingEmpName) {
		this.strPendingEmpName = strPendingEmpName;
	}
	
	public double getDoubleTransactionAmount() {
		return doubleTransactionAmount;
	}
	public void setDoubleTransactionAmount(double doubleTransactionAmount) {
		this.doubleTransactionAmount = doubleTransactionAmount;
	}
	
	public String getStrCreatedDate() {
		return strCreatedDate;
	}
	public void setStrCreatedDate(String strCreatedDate) {
		this.strCreatedDate = strCreatedDate;
	}
	public double getDoublePaidAmount() {
		return doublePaidAmount;
	}
	public void setDoublePaidAmount(double doublePaidAmount) {
		this.doublePaidAmount = doublePaidAmount;
	}
	public int getIntSiteWisePaymentId() {
		return intSiteWisePaymentId;
	}
	public void setIntSiteWisePaymentId(int intSiteWisePaymentId) {
		this.intSiteWisePaymentId = intSiteWisePaymentId;
	}
	public int getIntTempPaymentTransactionId() {
		return intTempPaymentTransactionId;
	}
	public void setIntTempPaymentTransactionId(int intTempPaymentTransactionId) {
		this.intTempPaymentTransactionId = intTempPaymentTransactionId;
	}
	public String getPaymentModeDisplayName() {
		return paymentModeDisplayName;
	}
	public void setPaymentModeDisplayName(String paymentModeDisplayName) {
		this.paymentModeDisplayName = paymentModeDisplayName;
	}
	public String getStrInvoiceNoInAP() {
		return strInvoiceNoInAP;
	}
	public void setStrInvoiceNoInAP(String strInvoiceNoInAP) {
		this.strInvoiceNoInAP = strInvoiceNoInAP;
	}
	public String getStrDCDate() {
		return strDCDate;
	}
	public void setStrDCDate(String strDCDate) {
		this.strDCDate = strDCDate;
	}
	public String getStrInvoiceReceivedDate() {
		return strInvoiceReceivedDate;
	}
	public void setStrInvoiceReceivedDate(String strInvoiceReceivedDate) {
		this.strInvoiceReceivedDate = strInvoiceReceivedDate;
	}
	public int getIntPaymentId() {
		return intPaymentId;
	}
	public void setIntPaymentId(int intPaymentId) {
		this.intPaymentId = intPaymentId;
	}
	public int getIntAccDeptPaymentProcessId() {
		return intAccDeptPaymentProcessId;
	}
	public void setIntAccDeptPaymentProcessId(int intAccDeptPaymentProcessId) {
		this.intAccDeptPaymentProcessId = intAccDeptPaymentProcessId;
	}
	public int getIntPaymentDetailsId() {
		return intPaymentDetailsId;
	}
	public void setIntPaymentDetailsId(int intPaymentDetailsId) {
		this.intPaymentDetailsId = intPaymentDetailsId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getUtrChequeNo() {
		return utrChequeNo;
	}
	public void setUtrChequeNo(String utrChequeNo) {
		this.utrChequeNo = utrChequeNo;
	}
	public double getDoublePOTotalAmount() {
		return doublePOTotalAmount;
	}
	public void setDoublePOTotalAmount(double doublePOTotalAmount) {
		this.doublePOTotalAmount = doublePOTotalAmount;
	}
	public String getStrComments() {
		return strComments;
	}
	public void setStrComments(String strComments) {
		this.strComments = strComments;
	}
	public int getIntTempTransactionId() {
		return intTempTransactionId;
	}
	public void setIntTempTransactionId(int intTempTransactionId) {
		this.intTempTransactionId = intTempTransactionId;
	}
	
	public String getStrRefrenceNo() {
		return strRefrenceNo;
	}
	public void setStrRefrenceNo(String strRefrenceNo) {
		this.strRefrenceNo = strRefrenceNo;
	}
	
	
	public double getDoublePaymentRequestedUpto() {
		return doublePaymentRequestedUpto;
	}
	public void setDoublePaymentRequestedUpto(double doublePaymentRequestedUpto) {
		this.doublePaymentRequestedUpto = doublePaymentRequestedUpto;
	}
	public String getActualRequestedAmount() {
		return actualRequestedAmount;
	}
	public void setActualRequestedAmount(String actualRequestedAmount) {
		this.actualRequestedAmount = actualRequestedAmount;
	}
	public String getActualRequestedDate() {
		return actualRequestedDate;
	}
	public void setActualRequestedDate(String actualRequestedDate) {
		this.actualRequestedDate = actualRequestedDate;
	}
	public String getRequestedDate() {
		return requestedDate;
	}
	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	
	
	
	
	
	public String getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(String requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	
	public String getStrSiteName() {
		return strSiteName;
	}
	public void setStrSiteName(String strSiteName) {
		this.strSiteName = strSiteName;
	}
	public String getStrPODate() {
		return strPODate;
	}
	public void setStrPODate(String strPODate) {
		this.strPODate = strPODate;
	}
	public String getStrReceiveDate() {
		return strReceiveDate;
	}
	public void setStrReceiveDate(String strReceiveDate) {
		this.strReceiveDate = strReceiveDate;
	}
	public String getStrVendorName() {
		return strVendorName;
	}
	public void setStrVendorName(String strVendorName) {
		this.strVendorName = strVendorName;
	}
	public int getIntSerialNo() {
		return intSerialNo;
	}
	public void setIntSerialNo(int intSerialNo) {
		this.intSerialNo = intSerialNo;
	}
	public String getStrEmployeeId() {
		return strEmployeeId;
	}
	public void setStrEmployeeId(String strEmployeeId) {
		this.strEmployeeId = strEmployeeId;
	}
	public String getStrPendingEmpId() {
		return strPendingEmpId;
	}
	public void setStrPendingEmpId(String strPendingEmpId) {
		this.strPendingEmpId = strPendingEmpId;
	}
	public String getStrDCNo() {
		return strDCNo;
	}
	public void setStrDCNo(String strDCNo) {
		this.strDCNo = strDCNo;
	}
	public String getStrPONo() {
		return strPONo;
	}
	public void setStrPONo(String strPONo) {
		this.strPONo = strPONo;
	}
	public String getStrApproverEmpId() {
		return strApproverEmpId;
	}
	public void setStrApproverEmpId(String strApproverEmpId) {
		this.strApproverEmpId = strApproverEmpId;
	}
	public double getDoublePaymentDoneUpto() {
		return doublePaymentDoneUpto;
	}
	public void setDoublePaymentDoneUpto(double doublePaymentDoneUpto) {
		this.doublePaymentDoneUpto = doublePaymentDoneUpto;
	}
	public double getDoublePaymentIntiateAmount() {
		return doublePaymentIntiateAmount;
	}
	public void setDoublePaymentIntiateAmount(double doublePaymentIntiateAmount) {
		this.doublePaymentIntiateAmount = doublePaymentIntiateAmount;
	}
	public double getDoubleAmountToBeReleased() {
		return doubleAmountToBeReleased;
	}
	public void setDoubleAmountToBeReleased(double doubleAmountToBeReleased) {
		this.doubleAmountToBeReleased = doubleAmountToBeReleased;
	}
	public String getStrSiteId() {
		return strSiteId;
	}
	public void setStrSiteId(String strSiteId) {
		this.strSiteId = strSiteId;
	}
	public double getDoubleInvoiceAmount() {
		return doubleInvoiceAmount;
	}
	public void setDoubleInvoiceAmount(double doubleInvoiceAmount) {
		this.doubleInvoiceAmount = doubleInvoiceAmount;
	}
	
	public String getStrInvoiceDate() {
		return strInvoiceDate;
	}
	public void setStrInvoiceDate(String strInvoiceDate) {
		this.strInvoiceDate = strInvoiceDate;
	}
	public String getStrInvoiceNo() {
		return strInvoiceNo;
	}
	public void setStrInvoiceNo(String strInvoiceNo) {
		this.strInvoiceNo = strInvoiceNo;
	}
	
	public String getStrPaymentIntiateType() {
		return strPaymentIntiateType;
	}
	public void setStrPaymentIntiateType(String strPaymentIntiateType) {
		this.strPaymentIntiateType = strPaymentIntiateType;
	}
	public String getStrVendorId() {
		return strVendorId;
	}
	public void setStrVendorId(String strVendorId) {
		this.strVendorId = strVendorId;
	}
	public String getStrPaymentReqDate() {
		return strPaymentReqDate;
	}
	public void setStrPaymentReqDate(String strPaymentReqDate) {
		this.strPaymentReqDate = strPaymentReqDate;
	}
	
	
	public String getStrRemarks() {
		return strRemarks;
	}
	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}
	public String strBillAmount;
	public String getStrBillAmount() {
		return strBillAmount;
	}
	public void setStrBillAmount(String strBillAmount) {
		this.strBillAmount = strBillAmount;
	}
	public String strWorkOrderAmount;
	public String getStrWorkOrderAmount() {
		return strWorkOrderAmount;
	}
	public void setStrWorkOrderAmount(String strWorkOrderAmount) {
		this.strWorkOrderAmount = strWorkOrderAmount;
	}
	private String strNmrBillNo;
	private String strNmrBillDate;
	private double doubleNmrBillAmount;
	public String getStrNmrBillNo() {
		return strNmrBillNo;
	}
	public void setStrNmrBillNo(String strNmrBillNo) {
		this.strNmrBillNo = strNmrBillNo;
	}
	public String getStrNmrBillDate() {
		return strNmrBillDate;
	}
	public void setStrNmrBillDate(String strNmrBillDate) {
		this.strNmrBillDate = strNmrBillDate;
	}
	public double getDoubleNmrBillAmount() {
		return doubleNmrBillAmount;
	}
	public void setDoubleNmrBillAmount(double doubleNmrBillAmount) {
		this.doubleNmrBillAmount = doubleNmrBillAmount;
	}
	
	
}
