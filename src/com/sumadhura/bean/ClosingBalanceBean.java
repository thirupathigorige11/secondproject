package com.sumadhura.bean;

public class ClosingBalanceBean {

	private String productName;
	private String subProductName;
	private String childproductName;
	private String measurementid;
	private String quantity;
	private String totalAmt;
	private String siteName;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	private String date;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMeasurementid() {
		return measurementid;
	}

	public void setMeasurementid(String measurementid) {
		this.measurementid = measurementid;
	}

	public String getSubProductName() {
		return subProductName;
	}

	public void setSubProductName(String subProductName) {
		this.subProductName = subProductName;
	}

	public String getChildproductName() {
		return childproductName;
	}

	public void setChildproductName(String childproductName) {
		this.childproductName = childproductName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
