package com.sumadhura.dto;

import java.util.Date;

import com.sumadhura.util.ValidateParams;

public class IndentCreationDto {

	private int indentNumber;
	private Date createDate;
	private int siteId;
	private String userId;
	private String purpose;
	private Date scheduleDate;
	private String pendingEmpId;
	private String pendingDeptId;
	private Date requiredDate;
	private String reqQuantity;
	private String allocatedQuantity;
	private String pendingQuantity;
	private String poIntiatedQuantity;
	private String intiatedQuantity;
	private String tempPass;
	private int siteWiseIndentNo;
	
	private String versionNo;
	private String reference_No;
	private String issue_date;
	private int sequenccce_Number;
	private String indentName;
	
	public String getIndentName() {
		return indentName;
	}
	public void setIndentName(String indentName) {
		this.indentName = indentName;
	}
	public int getSequenccce_Number() {
		return sequenccce_Number;
	}
	public void setSequenccce_Number(int sequenccce_Number) {
		this.sequenccce_Number = sequenccce_Number;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getReference_No() {
		return reference_No;
	}
	public void setReference_No(String reference_No) {
		this.reference_No = reference_No;
	}
	public String getIssue_date() {
		return issue_date;
	}
	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}
	public int getSiteWiseIndentNo() {
		return siteWiseIndentNo;
	}
	public void setSiteWiseIndentNo(int siteWiseIndentNo) {
		this.siteWiseIndentNo = siteWiseIndentNo;
	}
	public String getTempPass() {
		return tempPass;
	}
	public void setTempPass(String tempPass) {
		this.tempPass = tempPass;
	}
	public String getIntiatedQuantity() {
		return intiatedQuantity;
	}
	public void setIntiatedQuantity(String intiatedQuantity) {
		this.intiatedQuantity = intiatedQuantity;
	}
	public String getReqQuantity() {
		return reqQuantity;
	}
	public void setReqQuantity(String reqQuantity) {
		this.reqQuantity = reqQuantity;
	}
	public String getAllocatedQuantity() {
		return allocatedQuantity;
	}
	public void setAllocatedQuantity(String allocatedQuantity) {
		this.allocatedQuantity = allocatedQuantity;
	}
	public String getPendingQuantity() {
		return pendingQuantity;
	}
	public void setPendingQuantity(String pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	public String getPoIntiatedQuantity() {
		return poIntiatedQuantity;
	}
	public void setPoIntiatedQuantity(String poIntiatedQuantity) {
		this.poIntiatedQuantity = poIntiatedQuantity;
	}
	public int getIndentNumber() {
		return indentNumber;
	}
	public void setIndentNumber(int indentNumber) {
		this.indentNumber = indentNumber;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
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
	public Date getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
	
	
}
