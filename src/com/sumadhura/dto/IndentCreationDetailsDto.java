package com.sumadhura.dto;

public class IndentCreationDetailsDto {
	private String prodId;
	private String prodName;
	private String subProdId;
	private String subProdName;
	private String childProdId;
	private String childProdName;
	private String requiredQuantity;
	private String availableQuantity;
	private String measurementId;
	private String measurementName;	
	private String remarks;
	
	private String createdBy;
	private String groupId;
	
	
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	public String getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(String availableQuantity) {
		this.availableQuantity = availableQuantity;
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
	
	
	public String getRequiredQuantity() {
		return requiredQuantity;
	}
	public void setRequiredQuantity(String requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
