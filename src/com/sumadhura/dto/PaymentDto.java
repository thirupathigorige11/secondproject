package com.sumadhura.dto;

public class PaymentDto {

	private String strInvoiceNo ;
	private double doublePaymentIntiateAmount ;
	private String strPaymentIntiateType ;
	private String strVendorId ;
	private String strPaymentReqDate ;
	private double doubleAmountToBeReleased ;
	private  String strRemarks ;
	private String strInvoiceDate;
	//private int intPaymentNo;
	//private int intPaymentSeqNo;
	private int intPaymentId;
	private double doubleInvoiceAmount;
	private String strSiteId;
	private double doublePaymentDoneUpto;
	private String strDCNo;
	private String strPONo;
	private String strApproverEmpId;
	private String strPendingEmpId;
	private String strEmployeeId;
	//private int intAccDeptPaymentProcessSeqNo;
	private int intAccDeptPaymentProcessId;
	private String strRefrenceNo;
	private int intSiteWisePaymentId;
	private double doublePOAmount;
	private int intPaymentDetailsId;
	private String strPODate;
	private String strDCDate;
	private String strInvoiceReceivedDate;
	private String strInvoiceNoInAP;
	private int intTempPaymentTransactionId;
	private String paymentType;
	private String paymentMode;
	private String utrChequeNo;
	private int intIndentEntryId;
	private double doubleCreditTotalAmount;
	private String strCreditNoteNumber;
	private double doubleAdjustAmountFromAdvance;
	private String strPaymentDate ;
	private double actualDoubleAdjustAmountFromAdvance;
	private String strPendingDeptId;
	
	
	public String getStrPendingDeptId() {
		return strPendingDeptId;
	}
	public void setStrPendingDeptId(String strPendingDeptId) {
		this.strPendingDeptId = strPendingDeptId;
	}
	public double getActualDoubleAdjustAmountFromAdvance() {
		return actualDoubleAdjustAmountFromAdvance;
	}
	public void setActualDoubleAdjustAmountFromAdvance(double actualDoubleAdjustAmountFromAdvance) {
		this.actualDoubleAdjustAmountFromAdvance = actualDoubleAdjustAmountFromAdvance;
	}
	public String getStrPaymentDate() {
		return strPaymentDate;
	}
	public void setStrPaymentDate(String strPaymentDate) {
		this.strPaymentDate = strPaymentDate;
	}
	public double getDoubleAdjustAmountFromAdvance() {
		return doubleAdjustAmountFromAdvance;
	}
	public void setDoubleAdjustAmountFromAdvance(double doubleAdjustAmountFromAdvance) {
		this.doubleAdjustAmountFromAdvance = doubleAdjustAmountFromAdvance;
	}
	public String getStrCreditNoteNumber() {
		return strCreditNoteNumber;
	}
	public void setStrCreditNoteNumber(String strCreditNoteNumber) {
		this.strCreditNoteNumber = strCreditNoteNumber;
	}
	public double getDoubleCreditTotalAmount() {
		return doubleCreditTotalAmount;
	}
	public void setDoubleCreditTotalAmount(double doubleCreditTotalAmount) {
		this.doubleCreditTotalAmount = doubleCreditTotalAmount;
	}
	public int getIntIndentEntryId() {
		return intIndentEntryId;
	}
	public void setIntIndentEntryId(int intIndentEntryId) {
		this.intIndentEntryId = intIndentEntryId;
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
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public int getIntTempPaymentTransactionId() {
		return intTempPaymentTransactionId;
	}
	public void setIntTempPaymentTransactionId(int intTempPaymentTransactionId) {
		this.intTempPaymentTransactionId = intTempPaymentTransactionId;
	}
	public int getIntSiteWisePaymentId() {
		return intSiteWisePaymentId;
	}
	public void setIntSiteWisePaymentId(int intSiteWisePaymentId) {
		this.intSiteWisePaymentId = intSiteWisePaymentId;
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
	
	public String getStrPODate() {
		return strPODate;
	}
	public void setStrPODate(String strPODate) {
		this.strPODate = strPODate;
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
	public String getStrRefrenceNo() {
		return strRefrenceNo;
	}
	public void setStrRefrenceNo(String strRefrenceNo) {
		this.strRefrenceNo = strRefrenceNo;
	}
	public int getIntPaymentDetailsId() {
		return intPaymentDetailsId;
	}
	public void setIntPaymentDetailsId(int intPaymentDetailsId) {
		this.intPaymentDetailsId = intPaymentDetailsId;
	}
	public double getDoublePOAmount() {
		return doublePOAmount;
	}
	public void setDoublePOAmount(double doublePOAmount) {
		this.doublePOAmount = doublePOAmount;
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
	
	
}
