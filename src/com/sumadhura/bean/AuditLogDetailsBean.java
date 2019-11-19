package com.sumadhura.bean;

public class AuditLogDetailsBean {

	
	
	private Integer logId;
	private String date;
	private String loginId;
	private String status;
	private String siteId;
	private String operationName;
	private String entryDetailsId;
	
	
	public String getEntryDetailsId() {
		return entryDetailsId;
	}
	public void setEntryDetailsId(String entryDetailsId) {
		this.entryDetailsId = entryDetailsId;
	}
	public String getDate() {
		return date;
	}
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
}
