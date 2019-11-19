package com.sumadhura.bean;

import java.io.Serializable;

public class ViewIndentIssueDetailsBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String requesterName;
	private String requesterId;
	private String productName;
	private String subProdName;
	private String childProdName;
	private String issueQTY;
	private String vendorName;
	private String vendorId;
	private String receivedDate;
	private String entryDate;
	private String blockId;
	private String floorId;
	private String strSlipNumber;
	private String contractorName;
	private String contractorAddress;
	private String strTotalAmount;
	private String strInvoiceDate;
	private String date;
	private String time;
	private String invdate;
	private String siteName;
	private String poNo;
	private String poDate;
	private String indentEntryId;
	private String siteId;
	private String status;
	private String indentTo;
	private String indentType;
	private int serialNo;
	private String siteWiseIndentNo;
	private String invoiceAmount;
	private String requestedAmount;
	private String requestedDate;
	private String remarks;
	private String paymentId;
	private String paymentDetailsId;
	
	private String basicAmount;
	

	private String tax;
	private String otherCharges;
	private String amountAfterTaxPerUnit;
	
	private String taxAmount;
	private String totalAmt;
	private String measurementName;
	private String issuerName;
	private String block_Name;
	
	private String issueType;
	private String vehileNo;
	private String vendorAdress;
	private String isRecoverable;
	private String UORF;
	
	
	public String getContractorAddress() {
		return contractorAddress;
	}

	public void setContractorAddress(String contractorAddress) {
		this.contractorAddress = contractorAddress;
	}

	public String getUORF() {
		return UORF;
	}

	public void setUORF(String uORF) {
		UORF = uORF;
	}

	public String getIsRecoverable() {
		return isRecoverable;
	}

	public void setIsRecoverable(String isRecoverable) {
		this.isRecoverable = isRecoverable;
	}

	public String getVendorAdress() {
		return vendorAdress;
	}

	public void setVendorAdress(String vendorAdress) {
		this.vendorAdress = vendorAdress;
	}

	public String getVehileNo() {
		return vehileNo;
	}

	public void setVehileNo(String vehileNo) {
		this.vehileNo = vehileNo;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	private String floor_Name;
	private String flat_Name;
	private String dcEntryId;
	private String basicAmtAfterTax;
	private String indentName;
	private String strNumber;
	private String expiryDate;
	private String workOrderNumber;
	private String workOrderIssueId;
	private String dcNumber;
	private String invoiceNumber;
	private String dcNoForHyperLink;
	private String invNoForHyperLink;
	private String strDcDate;
	
	
	
	
	
	
	
	
	
	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public String getStrDcDate() {
		return strDcDate;
	}

	public void setStrDcDate(String strDcDate) {
		this.strDcDate = strDcDate;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getDcNoForHyperLink() {
		return dcNoForHyperLink;
	}

	public void setDcNoForHyperLink(String dcNoForHyperLink) {
		this.dcNoForHyperLink = dcNoForHyperLink;
	}

	public String getInvNoForHyperLink() {
		return invNoForHyperLink;
	}

	public void setInvNoForHyperLink(String invNoForHyperLink) {
		this.invNoForHyperLink = invNoForHyperLink;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}

	public String getWorkOrderIssueId() {
		return workOrderIssueId;
	}

	public void setWorkOrderIssueId(String workOrderIssueId) {
		this.workOrderIssueId = workOrderIssueId;
	}

	public String getStrNumber() {
		return strNumber;
	}

	public void setStrNumber(String strNumber) {
		this.strNumber = strNumber;
	}

	public String getIndentName() {
		return indentName;
	}

	public void setIndentName(String indentName) {
		this.indentName = indentName;
	}

	public String getBasicAmtAfterTax() {
		return basicAmtAfterTax;
	}

	public void setBasicAmtAfterTax(String basicAmtAfterTax) {
		this.basicAmtAfterTax = basicAmtAfterTax;
	}

	public String getDcEntryId() {
		return dcEntryId;
	}

	public void setDcEntryId(String dcEntryId) {
		this.dcEntryId = dcEntryId;
	}

	public String getFloor_Name() {
		return floor_Name;
	}

	public void setFloor_Name(String floor_Name) {
		this.floor_Name = floor_Name;
	}

	public String getBlock_Name() {
		return block_Name;
	}

	public void setBlock_Name(String block_Name) {
		this.block_Name = block_Name;
	}

	
	
	
	
	
	
	
	
	public String getFlat_Name() {
		return flat_Name;
	}

	public void setFlat_Name(String flat_Name) {
		this.flat_Name = flat_Name;
	}

	

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	

	public String getBasicAmount() {
		return basicAmount;
	}

	public void setBasicAmount(String basicAmount) {
		this.basicAmount = basicAmount;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getAmountAfterTaxPerUnit() {
		return amountAfterTaxPerUnit;
	}

	public void setAmountAfterTaxPerUnit(String amountAfterTaxPerUnit) {
		this.amountAfterTaxPerUnit = amountAfterTaxPerUnit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentDetailsId() {
		return paymentDetailsId;
	}

	public void setPaymentDetailsId(String paymentDetailsId) {
		this.paymentDetailsId = paymentDetailsId;
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

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getSiteWiseIndentNo() {
		return siteWiseIndentNo;
	}

	public void setSiteWiseIndentNo(String siteWiseIndentNo) {
		this.siteWiseIndentNo = siteWiseIndentNo;
	}
	
	
	
	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getIndentTo() {
		return indentTo;
	}

	public void setIndentTo(String indentTo) {
		this.indentTo = indentTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getIndentEntryId() {
		return indentEntryId;
	}

	public void setIndentEntryId(String indentEntryId) {
		this.indentEntryId = indentEntryId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getInvdate() {
		return invdate;
	}

	public void setInvdate(String invdate) {
		this.invdate = invdate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStrInvoiceDate() {
		return strInvoiceDate;
	}

	public void setStrInvoiceDate(String strInvoiceDate) {
		this.strInvoiceDate = strInvoiceDate;
	}

	public String getStrTotalAmount() {
		return strTotalAmount;
	}

	public void setStrTotalAmount(String strTotalAmount) {
		this.strTotalAmount = strTotalAmount;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getStrSlipNumber() {
		return strSlipNumber;
	}

	public void setStrSlipNumber(String strSlipNumber) {
		this.strSlipNumber = strSlipNumber;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSubProdName() {
		return subProdName;
	}

	public void setSubProdName(String subProdName) {
		this.subProdName = subProdName;
	}

	public String getChildProdName() {
		return childProdName;
	}

	public void setChildProdName(String childProdName) {
		this.childProdName = childProdName;
	}

	public String getIssueQTY() {
		return issueQTY;
	}

	public void setIssueQTY(String issueQTY) {
		this.issueQTY = issueQTY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
