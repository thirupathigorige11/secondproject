package com.sumadhura.dto;

public class IndentIssueDto implements Cloneable {

	private String prodId;
	private String prodName;
	private String subProdId;
	private String subProdName;
	private String childProdId;
	private String childProdName;
	private String quantity;
	private String measurementId;
	private String measurementName;
	private String hsnCd;
	private String uOrF;
	private String remarks;
	private String priceId;
	private String ContractorName;
	private String requestDate;
	private String isRecoverable;
	private String expiryDate;
	// ACP
	private String amountPerUnitBeforeTax;
	private String tax;
	private String otherOrTransportCharges;
	private String taxotherOrTransportCharges;
	private String receivedQuantity;
	private String amountAfterTaxotherOrTransportCharges;
	private String groupId;
	private String workorderNumber;
	private String wd;
	private String siteId;
	private String blockId;
	private String floorId;
	private String flatId;
	private String amount;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getWorkorderNumber() {
		return workorderNumber;
	}

	public void setWorkorderNumber(String workorderNumber) {
		this.workorderNumber = workorderNumber;
	}

	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getAmountAfterTaxotherOrTransportCharges() {
		return amountAfterTaxotherOrTransportCharges;
	}

	public void setAmountAfterTaxotherOrTransportCharges(String amountAfterTaxotherOrTransportCharges) {
		this.amountAfterTaxotherOrTransportCharges = amountAfterTaxotherOrTransportCharges;
	}

	public String getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(String receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}

	public String getOtherOrTransportCharges() {
		return otherOrTransportCharges;
	}

	public void setOtherOrTransportCharges(String otherOrTransportCharges) {
		this.otherOrTransportCharges = otherOrTransportCharges;
	}

	public String getTaxotherOrTransportCharges() {
		return taxotherOrTransportCharges;
	}

	public void setTaxotherOrTransportCharges(String taxotherOrTransportCharges) {
		this.taxotherOrTransportCharges = taxotherOrTransportCharges;
	}

	public String getAmountPerUnitBeforeTax() {
		return amountPerUnitBeforeTax;
	}

	public void setAmountPerUnitBeforeTax(String amountPerUnitBeforeTax) {
		this.amountPerUnitBeforeTax = amountPerUnitBeforeTax;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getIsRecoverable() {
		return isRecoverable;
	}

	public void setIsRecoverable(String isRecoverable) {
		this.isRecoverable = isRecoverable;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getContractorName() {
		return ContractorName;
	}

	public void setContractorName(String contractorName) {
		ContractorName = contractorName;
	}

	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public String getFloorId() {
		return floorId;
	}

	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}

	public String getFlatId() {
		return flatId;
	}

	public void setFlatId(String flatId) {
		this.flatId = flatId;
	}

	public String getuOrF() {
		return uOrF;
	}

	public void setuOrF(String uOrF) {
		this.uOrF = uOrF;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getSubProdId() {
		return subProdId;
	}

	public void setSubProdId(String subProdId) {
		this.subProdId = subProdId;
	}

	public String getSubProdName() {
		return subProdName;
	}

	public void setSubProdName(String subProdName) {
		this.subProdName = subProdName;
	}

	public String getChildProdId() {
		return childProdId;
	}

	public void setChildProdId(String childProdId) {
		this.childProdId = childProdId;
	}

	public String getChildProdName() {
		return childProdName;
	}

	public void setChildProdName(String childProdName) {
		this.childProdName = childProdName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public String getHsnCd() {
		return hsnCd;
	}

	public void setHsnCd(String hsnCd) {
		this.hsnCd = hsnCd;
	}
}
