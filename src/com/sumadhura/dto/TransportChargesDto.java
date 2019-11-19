package com.sumadhura.dto;

public class TransportChargesDto {
	
	private String transportId;
	private String transportGSTPercentage;
	private String transportGSTAmount;
	private String totalAmountAfterGSTTax;
	private String transportAmount;
	private String transportInvoice;
	private String totalamount;
	
	public String getTransportId() {
		return transportId;
	}
	public String getTotalamount() {
		return totalamount;
	}
	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}
	public String getTransportInvoice() {
		return transportInvoice;
	}
	public void setTransportInvoice(String transportInvoice) {
		this.transportInvoice = transportInvoice;
	}
	public void setTransportId(String transportId) {
		this.transportId = transportId;
	}
	public String getTransportGSTPercentage() {
		return transportGSTPercentage;
	}
	public void setTransportGSTPercentage(String transportGSTPercentage) {
		this.transportGSTPercentage = transportGSTPercentage;
	}
	public String getTransportGSTAmount() {
		return transportGSTAmount;
	}
	public void setTransportGSTAmount(String transportGSTAmount) {
		this.transportGSTAmount = transportGSTAmount;
	}
	public String getTotalAmountAfterGSTTax() {
		return totalAmountAfterGSTTax;
	}
	public void setTotalAmountAfterGSTTax(String totalAmountAfterGSTTax) {
		this.totalAmountAfterGSTTax = totalAmountAfterGSTTax;
	}
	public String getTransportAmount() {
		return transportAmount;
	}
	public void setTransportAmount(String transportAmount) {
		this.transportAmount = transportAmount;
	}
}
