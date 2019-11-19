package com.sumadhura.dto;



//pavan added the objects
public class IndentReceiveDto {
	
	private String prodId;
	private String prodName;
	private String subProdId;
	private String subProdName;
	private String childProdId;
	private String childProdName;
	private String quantity;
	private String measurementId;
	private String measurementName;	
	private String price;
	private String basicAmnt;
	private String tax;
	private String hsnCd;
	private String taxAmnt;	
	private String amntAfterTax;
	private String otherOrTranportChrgs;
	private String taxOnOtherOrTranportChrgs;
	private String otherOrTransportChrgsAfterTax;
	private String totalAmnt;
	private String otherChrgs;
	private String poNo;
	private String dcNo;
	private String date;
	private String expiryDate;
	private String poDate;
	private String eWayBillNo;
	private String vehileNo;
	private String transporterName;
	private String strUserId;
	private String strSiteId;
	private String strInvoiceNo;
	private String strInoviceDate;
	private String strVendorId;
	private String strReceiveDate;
	private String strRemarks;
	private String strIndentEntryDetailsId;
	private String strTransOtherChrgsId;
	private String strGSTPercentage;
	private String strGSTAmount;
	private String strTotalAmountAfterGSTTax;
	private String strTransportInvoiceId;
	private String state;
	
	//ACP
	private String indentType;
	private String requesterId;
	private String requesterName;
	
	private String strConveyanceType;
	private String	strConveyanceAmount;
	private String strGSTTax;
	private String strChargesAmountAfterTax;
	private String strTransportInvoiceNum;
	private String grnNumber;
	private String indentNumber;
	private String strDcDate;
	private String note;
	private String DcGrnNumber;
	private String strIndentEntryId;
	private String strEntryDate;
	private String strDcEntryId;
	private String strIndentCreationId;
	private String payment_Req_Date;
	
	public String getPayment_Req_Date() {
		return payment_Req_Date;
	}
	public void setPayment_Req_Date(String payment_Req_Date) {
		this.payment_Req_Date = payment_Req_Date;
	}
	
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getRequesterName() {
		return requesterName;
	}
	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	public String getIndentType() {
		return indentType;
	}
	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}
	public String getStrIndentCreationId() {
		return strIndentCreationId;
	}
	public void setStrIndentCreationId(String strIndentCreationId) {
		this.strIndentCreationId = strIndentCreationId;
	}
	public String getStrDcEntryId() {
		return strDcEntryId;
	}
	public void setStrDcEntryId(String strDcEntryId) {
		this.strDcEntryId = strDcEntryId;
	}
	public String getStrEntryDate() {
		return strEntryDate;
	}
	public void setStrEntryDate(String strEntryDate) {
		this.strEntryDate = strEntryDate;
	}
	public String getStrIndentEntryId() {
		return strIndentEntryId;
	}
	public void setStrIndentEntryId(String strIndentEntryId) {
		this.strIndentEntryId = strIndentEntryId;
	}
	public String getDcGrnNumber() {
		return DcGrnNumber;
	}
	public void setDcGrnNumber(String dcGrnNumber) {
		DcGrnNumber = dcGrnNumber;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStrDcDate() {
		return strDcDate;
	}
	public void setStrDcDate(String strDcDate) {
		this.strDcDate = strDcDate;
	}
	public String getIndentNumber() {
		return indentNumber;
	}
	public void setIndentNumber(String indentNumber) {
		this.indentNumber = indentNumber;
	}
	public String getStrGSTTax() {
		return strGSTTax;
	}
	public String getGrnNumber() {
		return grnNumber;
	}
	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}
	public void setStrGSTTax(String strGSTTax) {
		this.strGSTTax = strGSTTax;
	}
	public String getStrConveyanceType() {
		return strConveyanceType;
	}
	public void setStrConveyanceType(String strConveyanceType) {
		this.strConveyanceType = strConveyanceType;
	}


	public String getStrConveyanceAmount() {
		return strConveyanceAmount;
	}
	public void setStrConveyanceAmount(String strConveyanceAmount) {
		this.strConveyanceAmount = strConveyanceAmount;
	}

	public String getStrChargesAmountAfterTax() {
		return strChargesAmountAfterTax;
	}
	public void setStrChargesAmountAfterTax(String strChargesAmountAfterTax) {
		this.strChargesAmountAfterTax = strChargesAmountAfterTax;
	}
	public String getStrTransportInvoiceNum() {
		return strTransportInvoiceNum;
	}
	public void setStrTransportInvoiceNum(String strTransportInvoiceNum) {
		this.strTransportInvoiceNum = strTransportInvoiceNum;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStrTransOtherChrgsId() {
		return strTransOtherChrgsId;
	}
	public void setStrTransOtherChrgsId(String strTransOtherChrgsId) {
		this.strTransOtherChrgsId = strTransOtherChrgsId;
	}
	public String getStrGSTPercentage() {
		return strGSTPercentage;
	}
	public void setStrGSTPercentage(String strGSTPercentage) {
		this.strGSTPercentage = strGSTPercentage;
	}
	public String getStrGSTAmount() {
		return strGSTAmount;
	}
	public void setStrGSTAmount(String strGSTAmount) {
		this.strGSTAmount = strGSTAmount;
	}
	public String getStrTotalAmountAfterGSTTax() {
		return strTotalAmountAfterGSTTax;
	}
	public void setStrTotalAmountAfterGSTTax(String strTotalAmountAfterGSTTax) {
		this.strTotalAmountAfterGSTTax = strTotalAmountAfterGSTTax;
	}
	public String getStrTransportInvoiceId() {
		return strTransportInvoiceId;
	}
	public void setStrTransportInvoiceId(String strTransportInvoiceId) {
		this.strTransportInvoiceId = strTransportInvoiceId;
	}
	public String getStrIndentEntryDetailsId() {
		return strIndentEntryDetailsId;
	}
	public void setStrIndentEntryDetailsId(String strIndentEntryDetailsId) {
		this.strIndentEntryDetailsId = strIndentEntryDetailsId;
	}
	public String getStrRemarks() {
		return strRemarks;
	}
	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}
	public String getStrUserId() {
		return strUserId;
	}
	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}
	public String getStrSiteId() {
		return strSiteId;
	}
	public void setStrSiteId(String strSiteId) {
		this.strSiteId = strSiteId;
	}
	public String getStrInvoiceNo() {
		return strInvoiceNo;
	}
	public void setStrInvoiceNo(String strInvoiceNo) {
		this.strInvoiceNo = strInvoiceNo;
	}
	public String getStrInoviceDate() {
		return strInoviceDate;
	}
	public void setStrInoviceDate(String strInoviceDate) {
		this.strInoviceDate = strInoviceDate;
	}
	public String getStrVendorId() {
		return strVendorId;
	}
	public void setStrVendorId(String strVendorId) {
		this.strVendorId = strVendorId;
	}
	public String getStrReceiveDate() {
		return strReceiveDate;
	}
	public void setStrReceiveDate(String strReceiveDate) {
		this.strReceiveDate = strReceiveDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public String geteWayBillNo() {
		return eWayBillNo;
	}

	public void seteWayBillNo(String eWayBillNo) {
		this.eWayBillNo = eWayBillNo;
	}

	public String getVehileNo() {
		return vehileNo;
	}

	public void setVehileNo(String vehileNo) {
		this.vehileNo = vehileNo;
	}

	public String getTransporterName() {
		return transporterName;
	}

	public void setTransporterName(String transporterName) {
		this.transporterName = transporterName;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getDcNo() {
		return dcNo;
	}
	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}
	private String cgst;
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
	private String sgst;
	private String igst;
	
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBasicAmnt() {
		return basicAmnt;
	}
	public void setBasicAmnt(String basicAmnt) {
		this.basicAmnt = basicAmnt;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getHsnCd() {
		return hsnCd;
	}
	public void setHsnCd(String hsnCd) {
		this.hsnCd = hsnCd;
	}
	public String getTaxAmnt() {
		return taxAmnt;
	}
	public void setTaxAmnt(String taxAmnt) {
		this.taxAmnt = taxAmnt;
	}
	public String getAmntAfterTax() {
		return amntAfterTax;
	}
	public void setAmntAfterTax(String amntAfterTax) {
		this.amntAfterTax = amntAfterTax;
	}
	public String getOtherOrTranportChrgs() {
		return otherOrTranportChrgs;
	}
	public void setOtherOrTranportChrgs(String otherOrTranportChrgs) {
		this.otherOrTranportChrgs = otherOrTranportChrgs;
	}
	public String getTaxOnOtherOrTranportChrgs() {
		return taxOnOtherOrTranportChrgs;
	}
	public void setTaxOnOtherOrTranportChrgs(String taxOnOtherOrTranportChrgs) {
		this.taxOnOtherOrTranportChrgs = taxOnOtherOrTranportChrgs;
	}
	public String getOtherOrTransportChrgsAfterTax() {
		return otherOrTransportChrgsAfterTax;
	}
	public void setOtherOrTransportChrgsAfterTax(
			String otherOrTransportChrgsAfterTax) {
		this.otherOrTransportChrgsAfterTax = otherOrTransportChrgsAfterTax;
	}
	public String getTotalAmnt() {
		return totalAmnt;
	}
	public void setTotalAmnt(String totalAmnt) {
		this.totalAmnt = totalAmnt;
	}
	public String getOtherChrgs() {
		return otherChrgs;
	}
	public void setOtherChrgs(String otherChrgs) {
		this.otherChrgs = otherChrgs;
	}
}
