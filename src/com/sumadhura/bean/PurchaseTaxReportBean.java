package com.sumadhura.bean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseTaxReportBean {
	private String siteName;
	private String grnNo;
	private String receivedOrIssuedDate;
	private String poId;
	private String podate;
	private String state;
	private String tax;
	private String vendorName;
	private String address;
	private String vendorState;
	private String gsinNumber;
	private String productName;
	private String subProductName;
	private String childProductName;
	private String hsnCode;
	private String measurMntName;
	private String recevedQty;
	private String amountPerUnitBeforeTaxes;
	private String invoiceId;
	private String invoiceDate;
	private String basicAmount;
	private String otherCharges;
	private String taxOnOtherTransportChg;
	private String totalAmount;
	
	private String typeOfPurchase;
	private String cgst;
	private String sgst;
	private String igst;
	private String totalTax;
	
	
	
	public String getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(String totalTax) {
		this.totalTax = totalTax;
	}
	public String getTypeOfPurchase() {
		return typeOfPurchase;
	}
	public void setTypeOfPurchase(String typeOfPurchase) {
		this.typeOfPurchase = typeOfPurchase;
	}
	public String getCgst() {
		return cgst;
	}
	public void setCgst(String cgst) {
		this.cgst = cgst;
	}
	public String getSgst() {
		return sgst;
	}
	public void setSgst(String sgst) {
		this.sgst = sgst;
	}
	public String getIgst() {
		return igst;
	}
	public void setIgst(String igst) {
		this.igst = igst;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getGrnNo() {
		return grnNo;
	}
	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}
	public String getReceivedOrIssuedDate() {
		return receivedOrIssuedDate;
	}
	public void setReceivedOrIssuedDate(String receivedOrIssuedDate) {
		this.receivedOrIssuedDate = receivedOrIssuedDate;
	}
	public String getPoId() {
		return poId;
	}
	public void setPoId(String poId) {
		this.poId = poId;
	}
	public String getPodate() {
		return podate;
	}
	public void setPodate(String podate) {
		this.podate = podate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getVendorState() {
		return vendorState;
	}
	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}
	public String getGsinNumber() {
		return gsinNumber;
	}
	public void setGsinNumber(String gsinNumber) {
		this.gsinNumber = gsinNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSubProductName() {
		return subProductName;
	}
	public void setSubProductName(String subProductName) {
		this.subProductName = subProductName;
	}
	public String getChildProductName() {
		return childProductName;
	}
	public void setChildProductName(String childProductName) {
		this.childProductName = childProductName;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getMeasurMntName() {
		return measurMntName;
	}
	public void setMeasurMntName(String measurMntName) {
		this.measurMntName = measurMntName;
	}
	public String getRecevedQty() {
		return recevedQty;
	}
	public void setRecevedQty(String recevedQty) {
		this.recevedQty = recevedQty;
	}
	
	public String getAmountPerUnitBeforeTaxes() {
		return amountPerUnitBeforeTaxes;
	}
	public void setAmountPerUnitBeforeTaxes(String amountPerUnitBeforeTaxes) {
		this.amountPerUnitBeforeTaxes = amountPerUnitBeforeTaxes;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getBasicAmount() {
		return basicAmount;
	}
	public void setBasicAmount(String basicAmount) {
		this.basicAmount = basicAmount;
	}
	public String getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getTaxOnOtherTransportChg() {
		return taxOnOtherTransportChg;
	}
	public void setTaxOnOtherTransportChg(String taxOnOtherTransportChg) {
		this.taxOnOtherTransportChg = taxOnOtherTransportChg;
	}
	
	
	 public static void main(String []args){
		 String invoiceDate = "2019-06-04 17:25:02.0";
		 SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yy");
		    
		    try{
		    	
		    	if(!invoiceDate.equals("-")){
		    		Date invoice_date = dt.parse(invoiceDate);
		    		invoiceDate = dt1.format(invoice_date);
		    	}
		    }catch(Exception e){}
	        
	        System.out.println(invoiceDate);
	 }
	

}
