package com.sumadhura.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class IndentCreationBean {
	private String versionNo;
	private String reference_No;
	private String issue_date;
	
	private int indentNumber;
	private int indentCreationDetailsId;
	private int indentProcessId;
	private Date scheduleDate;
	private String indentFrom;
	private String indentTo;
	private Date requiredDate;
	private String product1;
	private String subProduct1;
	private String childProduct1;
	private String unitsOfMeasurement1;
	private String productId1;
	private String subProductId1;
	private String childProductId1;
	private String unitsOfMeasurementId1;
	private String productAvailability1;
	private String Quantity1;
	private String requiredQuantity1;
	private String allocatedQuantity;
	private String pendingQuantity;
	private String intiatedQuantity;
	private String remarks1;	
	private String purpose;
	private String strSerialNumber;
	private String strCreateDate;
	private String strScheduleDate;
	private String strRequiredDate;
	private String approverEmpId;
	private String siteName;
	private int siteId;
	private String status;
	private int centralIndentReqDtlsId;
	private String strRequestQuantity;
	private int purchaseDepartmentIndentProcessSeqId;
	private String poIntiatedQuantity;
	private String indentAvailability;
	private String indentRequestQuantity;
	private String fromEmpId;
	private String fromEmpName;
	private String toEmpId;
	private String toEmpName;
	
	private String ponumber;
	private String poentryId;
	private String materialEditComment;
	
	private String indentLevelComments;
	private int enquiryFormDetailsId;
	private String vendorId;
	
	private String purchaseDeptChildProdDisc;
	private String purchaseDeptReceivedDate;
	private String productAvailability;
	private double orderQuantity;

	private String printQuantity;
	private String receivedQuantity;
	private String Remarks;
	
	private int siteWiseIndentNo;
	private String vendorName;
	private String type_Of_Purchase;
	
	
	private String pending_Dept_Id;
	private String received_Quantity;
	
	
	private String subject;
	private String old_Po_Number;
	private String old_SiteWiseIndent_Number;
	
	private String old_Indent_Number;
	private String indentName;
	private String pending_Emp_Name;
	private String fromDate;
	private String toDate;
	private String siteWiseIndentNumber; // this is for marketing po they did have indent so it will create
	
	private String preparedBy; // this is for permanent PO cancel purpose written this one
	private String poAmount; // this is used in add on 
	private String createdBY; // this is used in add on for approval time we show that 
	
	
	
	
	//ACP this variable for central site indent operation
	private String senderSite;
	private String senderSiteName;
	private String indentSettledEmployeeName;
	private String currentUserMobileNumber;
	private String currentUserEemail;
	private String closed_Indent_Quan;
	private String groupId1;
	private int intSerialNo;
	
	
	
	
	
	public int getIntSerialNo() {
		return intSerialNo;
	}
	public void setIntSerialNo(int intSerialNo) {
		this.intSerialNo = intSerialNo;
	}
	public String getGroupId1() {
		return groupId1;
	}
	public void setGroupId1(String groupId1) {
		this.groupId1 = groupId1;
	}
	
	
		public String getClosed_Indent_Quan() {
		return closed_Indent_Quan;
	}
	public void setClosed_Indent_Quan(String closed_Indent_Quan) {
		this.closed_Indent_Quan = closed_Indent_Quan;
	}
		public String getPoAmount() {
		return poAmount;
	}
	public void setPoAmount(String poAmount) {
		this.poAmount = poAmount;
	}
	public String getCreatedBY() {
		return createdBY;
	}
	public void setCreatedBY(String createdBY) {
		this.createdBY = createdBY;
	}
	
	
	public String getCurrentUserMobileNumber() {
		return currentUserMobileNumber;
	}
	public void setCurrentUserMobileNumber(String currentUserMobileNumber) {
		this.currentUserMobileNumber = currentUserMobileNumber;
	}
	public String getCurrentUserEemail() {
		return currentUserEemail;
	}
	public void setCurrentUserEemail(String currentUserEemail) {
		this.currentUserEemail = currentUserEemail;
	}
	public String getIndentSettledEmployeeName() {
		return indentSettledEmployeeName;
	}
	public void setIndentSettledEmployeeName(String indentSettledEmployeeName) {
		this.indentSettledEmployeeName = indentSettledEmployeeName;
	}
	public String getSenderSiteName() {
		return senderSiteName;
	}
	public void setSenderSiteName(String senderSiteName) {
		this.senderSiteName = senderSiteName;
	}

	public String getSenderSite() {
		return senderSite;
	}
	public void setSenderSite(String senderSite) {
		this.senderSite = senderSite;
	}
	public String getPreparedBy() {
		return preparedBy;
	}
	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}
	public String getSiteWiseIndentNumber() {
		return siteWiseIndentNumber;
	}
	public void setSiteWiseIndentNumber(String siteWiseIndentNumber) {
		this.siteWiseIndentNumber = siteWiseIndentNumber;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getPending_Emp_Name() {
		return pending_Emp_Name;
	}
	public void setPending_Emp_Name(String pending_Emp_Name) {
		this.pending_Emp_Name = pending_Emp_Name;
	}
	
	
	
	
	
	
	
	public String getIndentName() {
		return indentName;
	}
	public void setIndentName(String indentName) {
		this.indentName = indentName;
	}
	public String getOld_Indent_Number() {
		return old_Indent_Number;
	}
	public void setOld_Indent_Number(String old_Indent_Number) {
		this.old_Indent_Number = old_Indent_Number;
	}
	public String getOld_SiteWiseIndent_Number() {
		return old_SiteWiseIndent_Number;
	}
	public void setOld_SiteWiseIndent_Number(String old_SiteWiseIndent_Number) {
		this.old_SiteWiseIndent_Number = old_SiteWiseIndent_Number;
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
	public String getOld_Po_Number() {
		return old_Po_Number;
	}
	public void setOld_Po_Number(String old_Po_Number) {
		this.old_Po_Number = old_Po_Number;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPending_Dept_Id() {
		return pending_Dept_Id;
	}
	public void setPending_Dept_Id(String pending_Dept_Id) {
		this.pending_Dept_Id = pending_Dept_Id;
	}
	public String getReceived_Quantity() {
		return received_Quantity;
	}
	public void setReceived_Quantity(String received_Quantity) {
		this.received_Quantity = received_Quantity;
	}
	public String getType_Of_Purchase() {
		return type_Of_Purchase;
	}
	public void setType_Of_Purchase(String type_Of_Purchase) {
		this.type_Of_Purchase = type_Of_Purchase;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	
	
	public int getSiteWiseIndentNo() {
		return siteWiseIndentNo;
	}
	public void setSiteWiseIndentNo(int siteWiseIndentNo) {
		this.siteWiseIndentNo = siteWiseIndentNo;
	}
	public String getReceivedQuantity() {
		return receivedQuantity;
	}
	public void setReceivedQuantity(String receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}
	public String getPrintQuantity() {
		return printQuantity;
	}
	public void setPrintQuantity(String printQuantity) {
		this.printQuantity = printQuantity;
	}
	public double getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(double orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getProductAvailability() {
		return productAvailability;
	}
	public void setProductAvailability(String productAvailability) {
		this.productAvailability = productAvailability;
	}
	public String getPurchaseDeptReceivedDate() {
		return purchaseDeptReceivedDate;
	}
	public void setPurchaseDeptReceivedDate(String purchaseDeptReceivedDate) {
		this.purchaseDeptReceivedDate = purchaseDeptReceivedDate;
	}
	public String getPurchaseDeptChildProdDisc() {
		return purchaseDeptChildProdDisc;
	}
	public void setPurchaseDeptChildProdDisc(String purchaseDeptChildProdDisc) {
		this.purchaseDeptChildProdDisc = purchaseDeptChildProdDisc;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public int getEnquiryFormDetailsId() {
		return enquiryFormDetailsId;
	}
	public void setEnquiryFormDetailsId(int enquiryFormDetailsId) {
		this.enquiryFormDetailsId = enquiryFormDetailsId;
	}
	public String getIndentLevelComments() {
		return indentLevelComments;
	}
	public void setIndentLevelComments(String indentLevelComments) {
		this.indentLevelComments = indentLevelComments;
	}
	public String getMaterialEditComment() {
		return materialEditComment;
	}
	public void setMaterialEditComment(String materialEditComment) {
		this.materialEditComment = materialEditComment;
	}
	public String getPonumber() {
		return ponumber;
	}
	public void setPonumber(String ponumber) {
		this.ponumber = ponumber;
	}
	public String getFromEmpId() {
		return fromEmpId;
	}
	public String getPoentryId() {
		return poentryId;
	}
	public void setPoentryId(String poentryId) {
		this.poentryId = poentryId;
	}
	public void setFromEmpId(String fromEmpId) {
		this.fromEmpId = fromEmpId;
	}
	public String getFromEmpName() {
		return fromEmpName;
	}
	public void setFromEmpName(String fromEmpName) {
		this.fromEmpName = fromEmpName;
	}
	public String getToEmpId() {
		return toEmpId;
	}
	public void setToEmpId(String toEmpId) {
		this.toEmpId = toEmpId;
	}
	public String getToEmpName() {
		return toEmpName;
	}
	public void setToEmpName(String toEmpName) {
		this.toEmpName = toEmpName;
	}
	public String getIndentRequestQuantity() {
		return indentRequestQuantity;
	}
	public void setIndentRequestQuantity(String indentRequestQuantity) {
		this.indentRequestQuantity = indentRequestQuantity;
	}
	public String getIndentAvailability() {
		return indentAvailability;
	}
	public void setIndentAvailability(String indentAvailability) {
		this.indentAvailability = indentAvailability;
	}
	public String getPoIntiatedQuantity() {
		return poIntiatedQuantity;
	}
	public void setPoIntiatedQuantity(String poIntiatedQuantity) {
		this.poIntiatedQuantity = poIntiatedQuantity;
	}
	public int getPurchaseDepartmentIndentProcessSeqId() {
		return purchaseDepartmentIndentProcessSeqId;
	}
	public void setPurchaseDepartmentIndentProcessSeqId(
			int purchaseDepartmentIndentProcessSeqId) {
		this.purchaseDepartmentIndentProcessSeqId = purchaseDepartmentIndentProcessSeqId;
	}
	public String getStrRequestQuantity() {
		return strRequestQuantity;
	}
	public void setStrRequestQuantity(String strRequestQuantity) {
		this.strRequestQuantity = strRequestQuantity;
	}
	public int getCentralIndentReqDtlsId() {
		return centralIndentReqDtlsId;
	}
	public void setCentralIndentReqDtlsId(int centralIndentReqDtlsId) {
		this.centralIndentReqDtlsId = centralIndentReqDtlsId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String strMouseOverData;
	

		private String issuedquantity;
	
	public String getIssuedquantity() {
		return issuedquantity;
	}
	public void setIssuedquantity(String issuedquantity) {
		this.issuedquantity = issuedquantity;
	}
	public String getIntiatedQuantity() {
		return intiatedQuantity;
	}
	public void setIntiatedQuantity(String intiatedQuantity) {
		this.intiatedQuantity = intiatedQuantity;
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
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	public int getIndentProcessId() {
		return indentProcessId;
	}
	public void setIndentProcessId(int indentProcessId) {
		this.indentProcessId = indentProcessId;
	}
	public String getStrMouseOverData() {
		return strMouseOverData;
	}
	public void setStrMouseOverData(String strMouseOverData) {
		this.strMouseOverData = strMouseOverData;
	}
	public String getQuantity1() {
		return Quantity1;
	}
	public void setQuantity1(String quantity1) {
		Quantity1 = quantity1;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getStrCreateDate() {
		return strCreateDate;
	}
	public void setStrCreateDate(String strCreateDate) {
		this.strCreateDate = strCreateDate;
	}
	public String getProductId1() {
		return productId1;
	}
	public void setProductId1(String productId1) {
		this.productId1 = productId1;
	}
	public String getSubProductId1() {
		return subProductId1;
	}
	public void setSubProductId1(String subProductId1) {
		this.subProductId1 = subProductId1;
	}
	public String getChildProductId1() {
		return childProductId1;
	}
	public void setChildProductId1(String childProductId1) {
		this.childProductId1 = childProductId1;
	}
	public String getUnitsOfMeasurementId1() {
		return unitsOfMeasurementId1;
	}
	public void setUnitsOfMeasurementId1(String unitsOfMeasurementId1) {
		this.unitsOfMeasurementId1 = unitsOfMeasurementId1;
	}
	

	
	public String getApproverEmpId() {
		return approverEmpId;
	}
	public void setApproverEmpId(String approverEmpId) {
		this.approverEmpId = approverEmpId;
	}
	public String getStrScheduleDate() {
		return strScheduleDate;
	}
	public void setStrScheduleDate(String strScheduleDate) {
		this.strScheduleDate = strScheduleDate;
	}
	public String getStrRequiredDate() {
		return strRequiredDate;
	}
	public void setStrRequiredDate(String strRequiredDate) {
		this.strRequiredDate = strRequiredDate;
	}
	public String getStrSerialNumber() {
		return strSerialNumber;
	}
	public void setStrSerialNumber(String strSerialNumber) {
		this.strSerialNumber = strSerialNumber;
	}
	public int getIndentNumber() {
		return indentNumber;
	}
	public void setIndentNumber(int indentNumber) {
		this.indentNumber = indentNumber;
	}
	
	public Date getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getIndentFrom() {
		return indentFrom;
	}
	public void setIndentFrom(String indentFrom) {
		this.indentFrom = indentFrom;
	}
	public String getIndentTo() {
		return indentTo;
	}
	public void setIndentTo(String indentTo) {
		this.indentTo = indentTo;
	}
	public Date getRequiredDate() {
		return requiredDate;
	}
	public void setRequiredDate(Date requiredDate) {
		this.requiredDate = requiredDate;
	}
	public String getProduct1() {
		return product1;
	}
	public void setProduct1(String product1) {
		this.product1 = product1;
	}
	public String getSubProduct1() {
		return subProduct1;
	}
	public void setSubProduct1(String subProduct1) {
		this.subProduct1 = subProduct1;
	}
	public String getChildProduct1() {
		return childProduct1;
	}
	public void setChildProduct1(String childProduct1) {
		this.childProduct1 = childProduct1;
	}
	public String getUnitsOfMeasurement1() {
		return unitsOfMeasurement1;
	}
	public void setUnitsOfMeasurement1(String unitsOfMeasurement1) {
		this.unitsOfMeasurement1 = unitsOfMeasurement1;
	}
	public String getProductAvailability1() {
		return productAvailability1;
	}
	public void setProductAvailability1(String productAvailability1) {
		this.productAvailability1 = productAvailability1;
	}
	
	public String getRequiredQuantity1() {
		return requiredQuantity1;
	}
	public void setRequiredQuantity1(String requiredQuantity1) {
		this.requiredQuantity1 = requiredQuantity1;
	}
	public String getRemarks1() {
		return remarks1;
	}
	public void setRemarks1(String remarks1) {
		this.remarks1 = remarks1;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public int getIndentCreationDetailsId() {
		return indentCreationDetailsId;
	}
	public void setIndentCreationDetailsId(int indentCreationDetailsId) {
		this.indentCreationDetailsId = indentCreationDetailsId;
	}
	
	
}
