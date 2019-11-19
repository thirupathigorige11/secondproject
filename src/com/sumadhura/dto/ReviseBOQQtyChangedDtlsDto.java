package com.sumadhura.dto;

public class ReviseBOQQtyChangedDtlsDto {
	private int reviseBOQQuantityChangedDetailsId;
	private int reviseBOQChangedDetailsId;
	private String materialGroupId;
	private String materialGroupMeasurementId;
	private double oldPerUnitQuantity;
	private double newPerUnitQuantity;
	private double oldPerUnitAmount;
	private double newPerUnitAmount;
	private double oldTotalQuantity;
	private double newTotalQuantity;
	private double oldTotalAmount;
	private double newTotalAmount;
	private String remarks;
	private String status;
	private String action;
	private int tempBOQNo;
	
	
	public int getTempBOQNo() {
		return tempBOQNo;
	}
	public void setTempBOQNo(int tempBOQNo) {
		this.tempBOQNo = tempBOQNo;
	}
	public int getReviseBOQQuantityChangedDetailsId() {
		return reviseBOQQuantityChangedDetailsId;
	}
	public void setReviseBOQQuantityChangedDetailsId(int reviseBOQQuantityChangedDetailsId) {
		this.reviseBOQQuantityChangedDetailsId = reviseBOQQuantityChangedDetailsId;
	}
	public int getReviseBOQChangedDetailsId() {
		return reviseBOQChangedDetailsId;
	}
	public void setReviseBOQChangedDetailsId(int reviseBOQChangedDetailsId) {
		this.reviseBOQChangedDetailsId = reviseBOQChangedDetailsId;
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
	public double getOldPerUnitQuantity() {
		return oldPerUnitQuantity;
	}
	public void setOldPerUnitQuantity(double oldPerUnitQuantity) {
		this.oldPerUnitQuantity = oldPerUnitQuantity;
	}
	public double getNewPerUnitQuantity() {
		return newPerUnitQuantity;
	}
	public void setNewPerUnitQuantity(double newPerUnitQuantity) {
		this.newPerUnitQuantity = newPerUnitQuantity;
	}
	public double getOldPerUnitAmount() {
		return oldPerUnitAmount;
	}
	public void setOldPerUnitAmount(double oldPerUnitAmount) {
		this.oldPerUnitAmount = oldPerUnitAmount;
	}
	public double getNewPerUnitAmount() {
		return newPerUnitAmount;
	}
	public void setNewPerUnitAmount(double newPerUnitAmount) {
		this.newPerUnitAmount = newPerUnitAmount;
	}
	public double getOldTotalQuantity() {
		return oldTotalQuantity;
	}
	public void setOldTotalQuantity(double oldTotalQuantity) {
		this.oldTotalQuantity = oldTotalQuantity;
	}
	public double getNewTotalQuantity() {
		return newTotalQuantity;
	}
	public void setNewTotalQuantity(double newTotalQuantity) {
		this.newTotalQuantity = newTotalQuantity;
	}
	public double getOldTotalAmount() {
		return oldTotalAmount;
	}
	public void setOldTotalAmount(double oldTotalAmount) {
		this.oldTotalAmount = oldTotalAmount;
	}
	public double getNewTotalAmount() {
		return newTotalAmount;
	}
	public void setNewTotalAmount(double newTotalAmount) {
		this.newTotalAmount = newTotalAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
