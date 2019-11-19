package com.sumadhura.bean;

public class MaterialDetails {
	private String material;
	private String unit;
	private double nos;
	private double perUnitQuantity;
	private double rate;
	private double perUnitAmount;
	private String remarks;
	private String materialGroupId;
	private String materialGroupMeasurementId;
	
	private String workAreaId;
	private double workArea;
	private double totalQuantity;
	private double totalAmount;
	
	
	
	
	
	public String getWorkAreaId() {
		return workAreaId;
	}
	public void setWorkAreaId(String workAreaId) {
		this.workAreaId = workAreaId;
	}
	public double getPerUnitQuantity() {
		return perUnitQuantity;
	}
	public void setPerUnitQuantity(double perUnitQuantity) {
		this.perUnitQuantity = perUnitQuantity;
	}
	public double getPerUnitAmount() {
		return perUnitAmount;
	}
	public void setPerUnitAmount(double perUnitAmount) {
		this.perUnitAmount = perUnitAmount;
	}
	public double getWorkArea() {
		return workArea;
	}
	public void setWorkArea(double workArea) {
		this.workArea = workArea;
	}
	public double getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getMaterialGroupId() {
		return materialGroupId;
	}
	public void setMaterialGroupId(String materialGroupId) {
		this.materialGroupId = materialGroupId;
	}
	public String getMaterialGroupMeasurementId() {
		return materialGroupMeasurementId;
	}
	public void setMaterialGroupMeasurementId(String materialGroupMeasurementId) {
		this.materialGroupMeasurementId = materialGroupMeasurementId;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public double getNos() {
		return nos;
	}
	public void setNos(double nos) {
		this.nos = nos;
	}
	
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
}
