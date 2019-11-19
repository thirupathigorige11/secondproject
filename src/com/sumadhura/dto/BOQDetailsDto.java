package com.sumadhura.dto;

public class BOQDetailsDto {
	private int tempBOQDetailsId;
	private String workDescriptionId;
	private String measurementId;
	private String totalAreaOfAllBlocks;
	private String totalAmountForAllBlocks;
	private double DoubleTotalAreaOfAllBlocks;
	private double DoubleTotalAmountForAllBlocks;
	private int tempBOQNo;
	private String scopeOfWork;
	private String minorHeadId;
	private String action;
	private String recordType;
	
	
	public double getDoubleTotalAreaOfAllBlocks() {
		return DoubleTotalAreaOfAllBlocks;
	}
	public void setDoubleTotalAreaOfAllBlocks(double doubleTotalAreaOfAllBlocks) {
		DoubleTotalAreaOfAllBlocks = doubleTotalAreaOfAllBlocks;
	}
	public double getDoubleTotalAmountForAllBlocks() {
		return DoubleTotalAmountForAllBlocks;
	}
	public void setDoubleTotalAmountForAllBlocks(double doubleTotalAmountForAllBlocks) {
		DoubleTotalAmountForAllBlocks = doubleTotalAmountForAllBlocks;
	}
	public int getTempBOQDetailsId() {
		return tempBOQDetailsId;
	}
	public void setTempBOQDetailsId(int tempBOQDetailsId) {
		this.tempBOQDetailsId = tempBOQDetailsId;
	}
	public String getWorkDescriptionId() {
		return workDescriptionId;
	}
	public void setWorkDescriptionId(String workDescriptionId) {
		this.workDescriptionId = workDescriptionId;
	}
	public String getMeasurementId() {
		return measurementId;
	}
	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}
	public String getTotalAreaOfAllBlocks() {
		return totalAreaOfAllBlocks;
	}
	public void setTotalAreaOfAllBlocks(String totalAreaOfAllBlocks) {
		this.totalAreaOfAllBlocks = totalAreaOfAllBlocks;
	}
	public String getTotalAmountForAllBlocks() {
		return totalAmountForAllBlocks;
	}
	public void setTotalAmountForAllBlocks(String totalAmountForAllBlocks) {
		this.totalAmountForAllBlocks = totalAmountForAllBlocks;
	}
	public int getTempBOQNo() {
		return tempBOQNo;
	}
	public void setTempBOQNo(int tempBOQNo) {
		this.tempBOQNo = tempBOQNo;
	}
	public String getScopeOfWork() {
		return scopeOfWork;
	}
	public void setScopeOfWork(String scopeOfWork) {
		this.scopeOfWork = scopeOfWork;
	}
	public String getMinorHeadId() {
		return minorHeadId;
	}
	public void setMinorHeadId(String minorHeadId) {
		this.minorHeadId = minorHeadId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	

}
