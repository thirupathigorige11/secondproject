package com.sumadhura.bean;

public class ProductDetails {

	private String productId;
	private String productName;
	private String sub_ProductId;
	private String sub_ProductName;
	private String child_ProductId;
	private String child_ProductName;
	private String measurementName;
	private String measurementId;
	private String site_Id;
	private String quantity;
	private String amount;
	private String PricePerUnit;
	private String basicAmt;
	private String recivedQty;
	private String issuedQty;
	private String siteName;
	private String strSerialNumber;
	private String strIndentId;
	private String strProjectName;
	private String vendorName;
	private String vendorId;
	private String strGSTINNumber;
	private int serialno;
	private String price;
	private String vendorAddress;
	private String poIntiatedQuantity;
	private String requiredQuantity;
	private String vendormentionquan;
	private String othercharges1;
	private String otherchargesaftertax1;
	private String taxonothertranportcharge1;
	private String hsnCode;
	private String pricePerUnitBeforeTax;
	private String tax;
	private String taxAmount;
	private String amountAfterTax;
	private String totalAmount;
	private String strPONumber;
	private String userId;
	private String poNumber;
	private int poEntrySeqNumber;
	private int poEntryId;
	private int PoNumber1;
	private String dcNumber;
	private String invoiceNumber;
	private String siteWiseIndentNo;
	private String purchasedeptRequestReceiveQuantity;
	private String versionNo;
	private String refferenceNo;
	private String strPoPrintRefdate;
	private double strPoAlreadyReceivedQuantity;
	
	
	
	private String indentDate;
	private String receiveDate;
	private String invoiceAmount;
	private double poTotal;
	private String materialDesc;

	private int Quatations;
	private String indentEntryId;
	private String grnDate;
	private String deliveryStatus;
	private String purchase_po_Req_Date;
	
private String purchaseDeptIndentProcessSeqId;
	
	private String strDiscount;
	private String strAmtAfterDiscount;
	private String IndentNo;
	
	private String pendingQuantity;
	private String requestQantity;;
	private String lastissuedDate;
	private String finalamtdiv;
	private Double OtherOrTransportCharges1;
	private Double TaxOnOtherOrTransportCharges1;
	private Double OtherOrTransportChargesAfterTax1;
	private Double TotalAmount1;
	private Double OtherCharges;
	private String ConveyanceId1;
	private String Conveyance1;
	private String ConveyanceAmount1;
	private String GSTTaxId1;
	private String GSTTax1;
	private String GSTAmount1;
	private String AmountAfterTaxx1;
	private String TransportInvoice1;
	
	private String strTermsConditionId;
	private String strTermsConditionName;
	private int intSerialNumber;
	
	private String childProductCustDisc;
	private String indentCreationDetailsId;
	private String poDate;
	


	private String enquiryFormDetailsId;
	private String subject;
	private String billingAddress;
	private String taxId;
	private String productBatchData;
	private String poEntryDetailsId;
	private String poTransChrgsDtlsSeqNo;
	private double actualQuantity;
	private double issuedQuantity;
	private double currentQuantity;
	private String outIssue;
	private String ccEmail;
	private int revision_Number;
	private String edit_Po_Number;
	private String strCreateDate;
	private String preparedBy;
	private String type_Of_purchase;
	private String passwdForMail;
	
	private String strDeliveryDate;
	private String indentName;
	
	private String payment_Done_Upto;
	private String balance_Amount;
	private String payment_Req_Amt;
	private String advance_Amt;
	private String pending_Emp;
	private String deliveryDate;
	private String adjustedAdvance;
	private String invoiceDate;
	private String dc_or_Invoice;
	
	private String expiryDate;
	
	private String productType;
	
	private String createdBy;
	private String payment_Req_days; // this is used for no of days from receiving material
	private String strPoEntryId;
	private String state;
	private String operation_Type;
	private String groupId;
	
	
	private String boqQuantity;
	private String availableQuantity;

	
	private String strChildProdname;
	private String locationArea;
	private String locationFromdate;
	private String locationToDate;
	private String locationAreaId;

	public String getLocationAreaId() {
		return locationAreaId;
	}

	public void setLocationAreaId(String locationAreaId) {
		this.locationAreaId = locationAreaId;
	}

	private String locationTime;
	private String locationQuantity;
	private String locationSiteName;
	private String amountPerUnit;
	private String type_Of_Po;
	private String type_Of_Po_Details;
	
	
	
	public String getType_Of_Po() {
		return type_Of_Po;
	}

	public void setType_Of_Po(String type_Of_Po) {
		this.type_Of_Po = type_Of_Po;
	}

	public String getType_Of_Po_Details() {
		return type_Of_Po_Details;
	}

	public void setType_Of_Po_Details(String type_Of_Po_Details) {
		this.type_Of_Po_Details = type_Of_Po_Details;
	}
	
	public String getAmountPerUnit() {
		return amountPerUnit;
	}

	public void setAmountPerUnit(String amountPerUnit) {
		this.amountPerUnit = amountPerUnit;
	}

	public String getStrChildProdname() {
		return strChildProdname;
	}

	public void setStrChildProdname(String strChildProdname) {
		this.strChildProdname = strChildProdname;
	}

	public String getLocationArea() {
		return locationArea;
	}

	public void setLocationArea(String locationArea) {
		this.locationArea = locationArea;
	}

	public String getLocationFromdate() {
		return locationFromdate;
	}

	public void setLocationFromdate(String locationFromdate) {
		this.locationFromdate = locationFromdate;
	}

	public String getLocationToDate() {
		return locationToDate;
	}

	public void setLocationToDate(String locationToDate) {
		this.locationToDate = locationToDate;
	}

	public String getLocationTime() {
		return locationTime;
	}

	public void setLocationTime(String locationTime) {
		this.locationTime = locationTime;
	}

	public String getLocationQuantity() {
		return locationQuantity;
	}

	public void setLocationQuantity(String locationQuantity) {
		this.locationQuantity = locationQuantity;
	}

	public String getLocationSiteName() {
		return locationSiteName;
	}

	public void setLocationSiteName(String locationSiteName) {
		this.locationSiteName = locationSiteName;
	}

	public String getBoqQuantity() {
		return boqQuantity;
	}

	public void setBoqQuantity(String boqQuantity) {
		this.boqQuantity = boqQuantity;
	}

	public String getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(String availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public String getOperation_Type() {
		return operation_Type;
	}

	public void setOperation_Type(String operation_Type) {
		this.operation_Type = operation_Type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	public String getStrPoEntryId() {
		return strPoEntryId;
	}

	public void setStrPoEntryId(String strPoEntryId) {
		this.strPoEntryId = strPoEntryId;
	}
	
		public String getPayment_Req_days() {
		return payment_Req_days;
	}

	public void setPayment_Req_days(String payment_Req_days) {
		this.payment_Req_days = payment_Req_days;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	
	public String getDc_or_Invoice() {
		return dc_or_Invoice;
	}

	public void setDc_or_Invoice(String dc_or_Invoice) {
		this.dc_or_Invoice = dc_or_Invoice;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getAdjustedAdvance() {
		return adjustedAdvance;
	}

	public void setAdjustedAdvance(String adjustedAdvance) {
		this.adjustedAdvance = adjustedAdvance;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getPayment_Done_Upto() {
		return payment_Done_Upto;
	}

	public void setPayment_Done_Upto(String payment_Done_Upto) {
		this.payment_Done_Upto = payment_Done_Upto;
	}

	public String getBalance_Amount() {
		return balance_Amount;
	}

	public void setBalance_Amount(String balance_Amount) {
		this.balance_Amount = balance_Amount;
	}

	public String getPayment_Req_Amt() {
		return payment_Req_Amt;
	}

	public void setPayment_Req_Amt(String payment_Req_Amt) {
		this.payment_Req_Amt = payment_Req_Amt;
	}

	public String getAdvance_Amt() {
		return advance_Amt;
	}

	public void setAdvance_Amt(String advance_Amt) {
		this.advance_Amt = advance_Amt;
	}

	public String getPending_Emp() {
		return pending_Emp;
	}

	public void setPending_Emp(String pending_Emp) {
		this.pending_Emp = pending_Emp;
	}

	public String getPurchase_po_Req_Date() {
		return purchase_po_Req_Date;
	}

	public void setPurchase_po_Req_Date(String purchase_po_Req_Date) {
		this.purchase_po_Req_Date = purchase_po_Req_Date;
	}

	public String getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(String grnDate) {
		this.grnDate = grnDate;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getIndentEntryId() {
		return indentEntryId;
	}

	public void setIndentEntryId(String indentEntryId) {
		this.indentEntryId = indentEntryId;
	}

	public int getQuatations() {
		return Quatations;
	}

	public void setQuatations(int quatations) {
		Quatations = quatations;
	}

	public String getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(String indentDate) {
		this.indentDate = indentDate;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	
	public double getPoTotal() {
		return poTotal;
	}

	public void setPoTotal(double poTotal) {
		this.poTotal = poTotal;
	}

	public String getMaterialDesc() {
		return materialDesc;
	}

	public void setMaterialDesc(String materialDesc) {
		this.materialDesc = materialDesc;
	}

	public double getStrPoAlreadyReceivedQuantity() {
		return strPoAlreadyReceivedQuantity;
	}

	public void setStrPoAlreadyReceivedQuantity(double strPoAlreadyReceivedQuantity) {
		this.strPoAlreadyReceivedQuantity = strPoAlreadyReceivedQuantity;
	}

	
	
	
	

	public String getIndentName() {
		return indentName;
	}

	public void setIndentName(String indentName) {
		this.indentName = indentName;
	}

	public String getStrDeliveryDate() {
		return strDeliveryDate;
	}

	public void setStrDeliveryDate(String strDeliveryDate) {
		this.strDeliveryDate = strDeliveryDate;
	}

	public String getPasswdForMail() {
		return passwdForMail;
	}

	public void setPasswdForMail(String passwdForMail) {
		this.passwdForMail = passwdForMail;
	}

	public String getType_Of_purchase() {
		return type_Of_purchase;
	}

	public void setType_Of_purchase(String type_Of_purchase) {
		this.type_Of_purchase = type_Of_purchase;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public String getStrCreateDate() {
		return strCreateDate;
	}

	public void setStrCreateDate(String strCreateDate) {
		this.strCreateDate = strCreateDate;
	}

	public int getRevision_Number() {
		return revision_Number;
	}

	public void setRevision_Number(int revision_Number) {
		this.revision_Number = revision_Number;
	}

	public String getEdit_Po_Number() {
		return edit_Po_Number;
	}

	public void setEdit_Po_Number(String edit_Po_Number) {
		this.edit_Po_Number = edit_Po_Number;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getRefferenceNo() {
		return refferenceNo;
	}

	public void setRefferenceNo(String refferenceNo) {
		this.refferenceNo = refferenceNo;
	}

	public String getStrPoPrintRefdate() {
		return strPoPrintRefdate;
	}

	public void setStrPoPrintRefdate(String strPoPrintRefdate) {
		this.strPoPrintRefdate = strPoPrintRefdate;
	}

	public String getPurchasedeptRequestReceiveQuantity() {
		return purchasedeptRequestReceiveQuantity;
	}

	public void setPurchasedeptRequestReceiveQuantity(
			String purchasedeptRequestReceiveQuantity) {
		this.purchasedeptRequestReceiveQuantity = purchasedeptRequestReceiveQuantity;
	}

	public String getSiteWiseIndentNo() {
		return siteWiseIndentNo;
	}

	public void setSiteWiseIndentNo(String siteWiseIndentNo) {
		this.siteWiseIndentNo = siteWiseIndentNo;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getPoNumber1() {
		return PoNumber1;
	}

	public void setPoNumber1(int poNumber1) {
		PoNumber1 = poNumber1;
	}

	public int getPoEntryId() {
		return poEntryId;
	}

	public void setPoEntryId(int poEntryId) {
		this.poEntryId = poEntryId;
	}
	
		public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

		public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	
	
		public String getOutIssue() {
		return outIssue;
	}

	public void setOutIssue(String outIssue) {
		this.outIssue = outIssue;
	}

		public double getActualQuantity() {
		return actualQuantity;
	}

	public void setActualQuantity(double actualQuantity) {
		this.actualQuantity = actualQuantity;
	}

	public double getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(double issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public double getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(double currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	
	
	
	
	public String getPoTransChrgsDtlsSeqNo() {
		return poTransChrgsDtlsSeqNo;
	}

	public void setPoTransChrgsDtlsSeqNo(String poTransChrgsDtlsSeqNo) {
		this.poTransChrgsDtlsSeqNo = poTransChrgsDtlsSeqNo;
	}

	public String getPoEntryDetailsId() {
		return poEntryDetailsId;
	}

	public void setPoEntryDetailsId(String poEntryDetailsId) {
		this.poEntryDetailsId = poEntryDetailsId;
	}

	public String getGSTTaxId1() {
		return GSTTaxId1;
	}

	public void setGSTTaxId1(String gSTTaxId1) {
		GSTTaxId1 = gSTTaxId1;
	}

	public String getConveyanceId1() {
		return ConveyanceId1;
	}

	public void setConveyanceId1(String conveyanceId1) {
		ConveyanceId1 = conveyanceId1;
	}

	public String getProductBatchData() {
		return productBatchData;
	}

	public void setProductBatchData(String productBatchData) {
		this.productBatchData = productBatchData;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getSerialno() {
		return serialno;
	}

	public void setSerialno(int serialno) {
		this.serialno = serialno;
	}

	public String getOthercharges1() {
		return othercharges1;
	}

	public void setOthercharges1(String othercharges1) {
		this.othercharges1 = othercharges1;
	}

	public String getOtherchargesaftertax1() {
		return otherchargesaftertax1;
	}

	public void setOtherchargesaftertax1(String otherchargesaftertax1) {
		this.otherchargesaftertax1 = otherchargesaftertax1;
	}

	public String getTaxonothertranportcharge1() {
		return taxonothertranportcharge1;
	}

	public void setTaxonothertranportcharge1(String taxonothertranportcharge1) {
		this.taxonothertranportcharge1 = taxonothertranportcharge1;
	}

	
	
	public String getPoIntiatedQuantity() {
		return poIntiatedQuantity;
	}

	public String getVendormentionquan() {
		return vendormentionquan;
	}

	public void setVendormentionquan(String vendormentionquan) {
		this.vendormentionquan = vendormentionquan;
	}

	public void setPoIntiatedQuantity(String poIntiatedQuantity) {
		this.poIntiatedQuantity = poIntiatedQuantity;
	}

	public String getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(String requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}


	
	
	public String getFinalamtdiv() {
		return finalamtdiv;
	}

	public void setFinalamtdiv(String finalamtdiv) {
		this.finalamtdiv = finalamtdiv;
	}

	
	
	

	public String getEnquiryFormDetailsId() {
		return enquiryFormDetailsId;
	}

	public void setEnquiryFormDetailsId(String enquiryFormDetailsId) {
		this.enquiryFormDetailsId = enquiryFormDetailsId;
	}

	public String getIndentCreationDetailsId() {
		return indentCreationDetailsId;
	}

	public void setIndentCreationDetailsId(String indentCreationDetailsId) {
		this.indentCreationDetailsId = indentCreationDetailsId;
	}

	public String getChildProductCustDisc() {
		return childProductCustDisc;
	}

	public void setChildProductCustDisc(String childProductCustDisc) {
		this.childProductCustDisc = childProductCustDisc;
	}

	public String getStrTermsConditionId() {
		return strTermsConditionId;
	}

	public void setStrTermsConditionId(String strTermsConditionId) {
		this.strTermsConditionId = strTermsConditionId;
	}

	public String getStrTermsConditionName() {
		return strTermsConditionName;
	}

	public void setStrTermsConditionName(String strTermsConditionName) {
		this.strTermsConditionName = strTermsConditionName;
	}

	public int getIntSerialNumber() {
		return intSerialNumber;
	}

	public void setIntSerialNumber(int intSerialNumber) {
		this.intSerialNumber = intSerialNumber;
	}

	

	public Double getOtherOrTransportCharges1() {
		return OtherOrTransportCharges1;
	}

	public void setOtherOrTransportCharges1(Double otherOrTransportCharges1) {
		OtherOrTransportCharges1 = otherOrTransportCharges1;
	}

	public Double getTaxOnOtherOrTransportCharges1() {
		return TaxOnOtherOrTransportCharges1;
	}

	public void setTaxOnOtherOrTransportCharges1(
			Double taxOnOtherOrTransportCharges1) {
		TaxOnOtherOrTransportCharges1 = taxOnOtherOrTransportCharges1;
	}

	public Double getOtherOrTransportChargesAfterTax1() {
		return OtherOrTransportChargesAfterTax1;
	}

	public void setOtherOrTransportChargesAfterTax1(
			Double otherOrTransportChargesAfterTax1) {
		OtherOrTransportChargesAfterTax1 = otherOrTransportChargesAfterTax1;
	}

	public Double getTotalAmount1() {
		return TotalAmount1;
	}

	public void setTotalAmount1(Double totalAmount1) {
		TotalAmount1 = totalAmount1;
	}

	public Double getOtherCharges() {
		return OtherCharges;
	}

	public void setOtherCharges(Double otherCharges) {
		OtherCharges = otherCharges;
	}

	public String getConveyance1() {
		return Conveyance1;
	}

	public void setConveyance1(String conveyance1) {
		Conveyance1 = conveyance1;
	}

	public String getConveyanceAmount1() {
		return ConveyanceAmount1;
	}

	public void setConveyanceAmount1(String conveyanceAmount1) {
		ConveyanceAmount1 = conveyanceAmount1;
	}

	public String getGSTTax1() {
		return GSTTax1;
	}

	public void setGSTTax1(String gSTTax1) {
		GSTTax1 = gSTTax1;
	}

	public String getGSTAmount1() {
		return GSTAmount1;
	}

	public void setGSTAmount1(String gSTAmount1) {
		GSTAmount1 = gSTAmount1;
	}

	public String getAmountAfterTaxx1() {
		return AmountAfterTaxx1;
	}

	public void setAmountAfterTaxx1(String amountAfterTaxx1) {
		AmountAfterTaxx1 = amountAfterTaxx1;
	}

	public String getTransportInvoice1() {
		return TransportInvoice1;
	}

	public void setTransportInvoice1(String transportInvoice1) {
		TransportInvoice1 = transportInvoice1;
	}

	public String getLastissuedDate() {
		return lastissuedDate;
	}

	public void setLastissuedDate(String lastissuedDate) {
		this.lastissuedDate = lastissuedDate;
	}

	public String getRequestQantity() {
		return requestQantity;
	}

	public void setRequestQantity(String requestQantity) {
		this.requestQantity = requestQantity;
	}

	public String getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(String pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public String getIndentNo() {
		return IndentNo;
	}

	public void setIndentNo(String indentNo) {
		IndentNo = indentNo;
	}

	public String getStrDiscount() {
		return strDiscount;
	}

	public void setStrDiscount(String strDiscount) {
		this.strDiscount = strDiscount;
	}

	public String getStrAmtAfterDiscount() {
		return strAmtAfterDiscount;
	}

	public void setStrAmtAfterDiscount(String strAmtAfterDiscount) {
		this.strAmtAfterDiscount = strAmtAfterDiscount;
	}

	public String getPurchaseDeptIndentProcessSeqId() {
		return purchaseDeptIndentProcessSeqId;
	}

	public void setPurchaseDeptIndentProcessSeqId(String purchaseDeptIndentProcessSeqId) {
		this.purchaseDeptIndentProcessSeqId = purchaseDeptIndentProcessSeqId;
	}

	

	public int getPoEntrySeqNumber() {
		return poEntrySeqNumber;
	}

	public void setPoEntrySeqNumber(int poEntrySeqNumber) {
		this.poEntrySeqNumber = poEntrySeqNumber;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getStrPONumber() {
		return strPONumber;
	}

	public void setStrPONumber(String strPONumber) {
		this.strPONumber = strPONumber;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getPricePerUnitBeforeTax() {
		return pricePerUnitBeforeTax;
	}

	public void setPricePerUnitBeforeTax(String pricePerUnitBeforeTax) {
		this.pricePerUnitBeforeTax = pricePerUnitBeforeTax;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getAmountAfterTax() {
		return amountAfterTax;
	}

	public void setAmountAfterTax(String amountAfterTax) {
		this.amountAfterTax = amountAfterTax;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStrGSTINNumber() {
		return strGSTINNumber;
	}

	public void setStrGSTINNumber(String strGSTINNumber) {
		this.strGSTINNumber = strGSTINNumber;
	}

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getStrProjectName() {
		return strProjectName;
	}

	public void setStrProjectName(String strProjectName) {
		this.strProjectName = strProjectName;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStrIndentId() {
		return strIndentId;
	}

	public void setStrIndentId(String strIndentId) {
		this.strIndentId = strIndentId;
	}

	public String getStrSerialNumber() {
		return strSerialNumber;
	}

	public void setStrSerialNumber(String strSerialNumber) {
		this.strSerialNumber = strSerialNumber;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	private String date;
	
	public String getIssuedQty() {
		return issuedQty;
	}

	public void setIssuedQty(String issuedQty) {
		this.issuedQty = issuedQty;
	}

	public String getRecivedQty() {
		return recivedQty;
	}

	public void setRecivedQty(String recivedQty) {
		this.recivedQty = recivedQty;
	}

	public String getBasicAmt() {
		return basicAmt;
	}

	public void setBasicAmt(String basicAmt) {
		this.basicAmt = basicAmt;
	}

	public String getPricePerUnit() {
		return PricePerUnit;
	}

	public void setPricePerUnit(String pricePerUnit) {
		PricePerUnit = pricePerUnit;
	}

	private String strOtherSiteQtyDtls;
	
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStrOtherSiteQtyDtls() {
		return strOtherSiteQtyDtls;
	}

	public void setStrOtherSiteQtyDtls(String strOtherSiteQtyDtls) {
		this.strOtherSiteQtyDtls = strOtherSiteQtyDtls;
	}

	

	public String getSite_Id() {
		return site_Id;
	}

	public void setSite_Id(String site_Id) {
		this.site_Id = site_Id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSub_ProductName() {
		return sub_ProductName;
	}

	public void setSub_ProductName(String sub_ProductName) {
		this.sub_ProductName = sub_ProductName;
	}

	public String getSub_ProductId() {
		return sub_ProductId;
	}

	public void setSub_ProductId(String sub_ProductId) {
		this.sub_ProductId = sub_ProductId;
	}

	

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public String getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(String measurementId) {
		this.measurementId = measurementId;
	}

	public String getChild_ProductId() {
		return child_ProductId;
	}

	public void setChild_ProductId(String child_ProductId) {
		this.child_ProductId = child_ProductId;
	}

	public String getChild_ProductName() {
		return child_ProductName;
	}

	public void setChild_ProductName(String child_ProductName) {
		this.child_ProductName = child_ProductName;
	}

}
