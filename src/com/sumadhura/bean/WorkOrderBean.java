package com.sumadhura.bean;
 
public class WorkOrderBean implements Cloneable, Comparable<WorkOrderBean> {
	private String siteId;
	private String siteName;
	private String workOrderStatus;
	private String isUpdateWOPage;
	//private String scheduledDate;
	private String workOrderDate;
	private String workOrderName;
	private String noteComment;
	private String Purpose;
	private String remarks;
	// For Products
	private String WO_MajorHead1;
	private String WO_MinorHead1;
	private String WO_Desc1;
	private String woManualDesc1;
	private String scopeOfWOrk;
	private String scopeOfWOrk1;
	private String UnitsOfMeasurement1;
	private String Quantity1;
	private String Quantity;
	private String AcceptedRate1;
	private String TotalAmount1;
	private String boqRate;

	private String totalWoAmount;
	private String materialWoAmount;
	private String laborWoAmount;
	private String boqNo;

	private String woMajorHead;
	private String woMinorHeads;
	private String wODescription;
	private String wOManualDescription;
	private String UnitsOfMeasurement;

	private String AcceptedRate;
	private String basicAmount;
	private String totalAmount;// total AMT
	private String strTermsConditionId;
	private String strTermsConditionName;

	private String comments1;// WO_COMMENT
	private int indexNumber;
	private int woChangedDetailsId;
	private int indexNumber1 = 1;
	private String condition;
	// For Work Area Mapping
	private String flat_id;
	private String flat_name;
	private String floor_id;
	private String floor_name;
	private String block_id;
	private String block_name;

	private String workAreaId;
	private String inputBoxWorkAreaGroupId;
	private String actualArea;
	private String availbleArea;
	private String selectedArea;
	private String workAreaGroupId;
	private String paymentDone;
	private String percentage;
	private String wo_measurmen_name;

	// For NextLevelApprover Detail
	//private String workOrderIssueId;
	private String QS_Temp_Issue_Id;
	private String QS_Temp_Issue_Dtls_Id;
	private String tempIssueAreaDetailsId;
	private String requestFromMail;
	private String oldQSWorkOrderAreaDTLSId;
	private String woRowCode;
	private String siteWiseWONO; 
	private String workOrderCreateEmpId;
	private String workOrderCreadeDate;
/*	private String fromEmpId;
	private String fromEmpName;
	private String toEmpId;
	private String toEmpName;*/
	private String workorderFrom;
	private String workorderTo;
	private String approverEmpId;
	private String pendingEmpId;
	private String approverEmpMail;
	private String userId;
	// For Contractors
	private String contractorId;
	private String contractorName;
	private String contractorAddress;
	private String contractorPhoneNo;
	private String contractorPanNo;
	private String contractorBankName;
	private String GSTIN;
	private String contractorBankAccNumber;
	private String contractorIFSCCode;
	private String isSaveOrUpdateOperation;
	private String isCommonApproval;
	// For Bill
	private String typeOfWork;
	private String workOrderNo;
	private String actualWorkOrderNo;
	private String revisedWorkOrderNo;
	private String versionNumber;
	private String refferenceNo;
	private String revision;
	private String oldWorkOrderNo;
	private String oldWorkOrderLaborAmount;
	private String oldWorkOrderMaterialAmount;
	private String oldOrderTotalAmount;
	private String nmrPaymentDetails;
	private String woRecordContains;
	
	private double woBillBilledAmount;
	private double woBillPaidAmount;
	
	//for material BOQ product details
	private String materialGroupName;
	private String materialGroupId;
	private String workArea;
	private String perUnitQuantity;
	private String perUnitAmount;
	private String totalQuantity;
	private String boqRecordType;
	
	
	/*
	 * private String billDate; private String raBillNo;
	 */
	/*
	 * private String advanceBillAmt; private String previousAdvBillAmt; private
	 * String releaseAdvTotalAmt; private String releaseAdvPrevAmt; private
	 * String outstandingAdvPrevAmt; private String outstandingAdvTotalAmt;
	 * private String totalAmtCumulativeCertified; private String
	 * totalAmtPreviousCertified; private String paybleAmt;
	 */

	// this method is used for checking is objects are duplicate or not
	//if object's are duplicate then Set implemented class 
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof WorkOrderBean))
			return false;
		if (obj == this)
			return true;

		WorkOrderBean bean = (WorkOrderBean) obj;
		System.out.println("WorkOrderBean.equals() this workAreaId is" + this.workAreaId + " obj  workAreaId is "
				+ bean.workAreaId);
		boolean flag = this.WO_Desc1.equals(bean.WO_Desc1);
		boolean acceptedrate1 = this.TotalAmount1.equals(bean.TotalAmount1);

		if (flag == false && acceptedrate1 == false) {
			int i = Integer.valueOf(bean.getIndexNumber1());
			System.out.println("Object is not duplicate..!! " + i + " " + bean.getQuantity() + " " + this.Quantity);
			// this.indexNumber1++;
			this.setIndexNumber1(i + 1);
			System.out.println(this.indexNumber1);
		} else if (flag && acceptedrate1) {
			/*
			 * System.out.println("Object is  duplicate..!!"+bean.getQuantity()+
			 * " "+this.Quantity); int qty= Integer.valueOf(bean.getQuantity())+
			 * Integer.valueOf(this.Quantity);
			 * bean.setQuantity(String.valueOf(qty));
			 * bean.setTotalAmount1(String.valueOf(qty*Integer.valueOf(bean.
			 * getAcceptedRate1()))); System.out.println(bean.getTotalAmount1()+
			 * " "+this.getTotalAmount1());
			 */
			System.out.println("Object is  duplicate.. after !!" + bean.getQuantity());
		}
		return flag && acceptedrate1;
	}

	@Override
	public int hashCode() {
		System.out.println("WorkOrderBean.hashCode()");
		return QS_Temp_Issue_Id.hashCode() + WO_Desc1.hashCode();
	}

	@Override
	public int compareTo(WorkOrderBean o) {

		String majorHead = o.getWO_MajorHead1();
		String majorHead1 = this.getWO_MajorHead1();

		System.out.println(majorHead + " " + majorHead1);
		int result = 0;// majorHead.compareTo(majorHead1);
		/*
		 * if(result==0){ return 1; }else{ return 0; }
		 */
		return result;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
 	public String getApproverEmpMail() {
		return approverEmpMail;
	}

	public void setApproverEmpMail(String approverEmpMail) {
		this.approverEmpMail = approverEmpMail;
	}

	public String getOldWorkOrderLaborAmount() {
		return oldWorkOrderLaborAmount;
	}

	public void setOldWorkOrderLaborAmount(String oldWorkOrderLaborAmount) {
		this.oldWorkOrderLaborAmount = oldWorkOrderLaborAmount;
	}

	public String getOldWorkOrderMaterialAmount() {
		return oldWorkOrderMaterialAmount;
	}

	public void setOldWorkOrderMaterialAmount(String oldWorkOrderMaterialAmount) {
		this.oldWorkOrderMaterialAmount = oldWorkOrderMaterialAmount;
	}

	public String getOldOrderTotalAmount() {
		return oldOrderTotalAmount;
	}

	public void setOldOrderTotalAmount(String oldOrderTotalAmount) {
		this.oldOrderTotalAmount = oldOrderTotalAmount;
	}

	public String getRequestFromMail() {
		return requestFromMail;
	}

	public void setRequestFromMail(String requestFromMail) {
		this.requestFromMail = requestFromMail;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}


	public String getIsCommonApproval() {
		return isCommonApproval;
	}

	public void setIsCommonApproval(String isCommonApproval) {
		this.isCommonApproval = isCommonApproval;
	}

	public String getIsSaveOrUpdateOperation() {
		return isSaveOrUpdateOperation;
	}

	public void setIsSaveOrUpdateOperation(String isSaveOrUpdateOperation) {
		this.isSaveOrUpdateOperation = isSaveOrUpdateOperation;
	}

	public String getMaterialGroupName() {
		return materialGroupName;
	}

	public void setMaterialGroupName(String materialGroupName) {
		this.materialGroupName = materialGroupName;
	}

	public String getWoRecordContains() {
		return woRecordContains;
	}

	public void setWoRecordContains(String woRecordContains) {
		this.woRecordContains = woRecordContains;
	}

	public String getInputBoxWorkAreaGroupId() {
		return inputBoxWorkAreaGroupId;
	}

	public void setInputBoxWorkAreaGroupId(String inputBoxWorkAreaGroupId) {
		this.inputBoxWorkAreaGroupId = inputBoxWorkAreaGroupId;
	}

	public String getWorkAreaGroupId() {
		return workAreaGroupId;
	}

	public void setWorkAreaGroupId(String workAreaGroupId) {
		this.workAreaGroupId = workAreaGroupId;
	}

	public String getAvailbleArea() {
		return availbleArea;
	}

	public void setAvailbleArea(String availbleArea) {
		this.availbleArea = availbleArea;
	}

	public String getPaymentDone() {
		return paymentDone;
	}

	public void setPaymentDone(String paymentDone) {
		this.paymentDone = paymentDone;
	}

	public int getWoChangedDetailsId() {
		return woChangedDetailsId;
	}

	public void setWoChangedDetailsId(int woChangedDetailsId) {
		this.woChangedDetailsId = woChangedDetailsId;
	}

	public String getMaterialWoAmount() {
		return materialWoAmount;
	}

	public void setMaterialWoAmount(String materialWoAmount) {
		this.materialWoAmount = materialWoAmount;
	}

	public String getLaborWoAmount() {
		return laborWoAmount;
	}

	public void setLaborWoAmount(String laborWoAmount) {
		this.laborWoAmount = laborWoAmount;
	}

	public String getBoqRecordType() {
		return boqRecordType;
	}

	public void setBoqRecordType(String boqRecordType) {
		this.boqRecordType = boqRecordType;
	}

	public String getTempIssueAreaDetailsId() {
		return tempIssueAreaDetailsId;
	}

	public void setTempIssueAreaDetailsId(String tempIssueAreaDetailsId) {
		this.tempIssueAreaDetailsId = tempIssueAreaDetailsId;
	}

	
	public String getMaterialGroupId() {
		return materialGroupId;
	}

	public void setMaterialGroupId(String materialGroupId) {
		this.materialGroupId = materialGroupId;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}

	public String getPerUnitQuantity() {
		return perUnitQuantity;
	}

	public void setPerUnitQuantity(String perUnitQuantity) {
		this.perUnitQuantity = perUnitQuantity;
	}

	public String getPerUnitAmount() {
		return perUnitAmount;
	}

	public void setPerUnitAmount(String perUnitAmount) {
		this.perUnitAmount = perUnitAmount;
	}

	public String getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(String totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public double getWoBillBilledAmount() {
		return woBillBilledAmount;
	}

	public void setWoBillBilledAmount(double woBillBilledAmount) {
		this.woBillBilledAmount = woBillBilledAmount;
	}

	public double getWoBillPaidAmount() {
		return woBillPaidAmount;
	}

	public void setWoBillPaidAmount(double woBillPaidAmount) {
		this.woBillPaidAmount = woBillPaidAmount;
	}

	public String getBoqRate() {
		return boqRate;
	}

	public void setBoqRate(String boqRate) {
		this.boqRate = boqRate;
	}

	public String getWoRowCode() {
		return woRowCode;
	}

	public void setWoRowCode(String woRowCode) {
		this.woRowCode = woRowCode;
	}

	public String getOldQSWorkOrderAreaDTLSId() {
		return oldQSWorkOrderAreaDTLSId;
	}

	public void setOldQSWorkOrderAreaDTLSId(String oldQSWorkOrderAreaDTLSId) {
		this.oldQSWorkOrderAreaDTLSId = oldQSWorkOrderAreaDTLSId;
	}

	public String getNmrPaymentDetails() {
		return nmrPaymentDetails;
	}

	public void setNmrPaymentDetails(String nmrPaymentDetails) {
		this.nmrPaymentDetails = nmrPaymentDetails;
	}

	public String getContractorBankName() {
		return contractorBankName;
	}

	public void setContractorBankName(String contractorBankName) {
		this.contractorBankName = contractorBankName;
	}

	public String getContractorBankAccNumber() {
		return contractorBankAccNumber;
	}

	public String getContractorIFSCCode() {
		return contractorIFSCCode;
	}

	public void setContractorIFSCCode(String contractorIFSCCode) {
		this.contractorIFSCCode = contractorIFSCCode;
	}

	public void setContractorBankAccNumber(String contractorBankAccNumber) {
		this.contractorBankAccNumber = contractorBankAccNumber;
	}

	public String getOldWorkOrderNo() {
		return oldWorkOrderNo;
	}

	public void setOldWorkOrderNo(String oldWorkOrderNo) {
		this.oldWorkOrderNo = oldWorkOrderNo;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getRefferenceNo() {
		return refferenceNo;
	}

	public void setRefferenceNo(String refferenceNo) {
		this.refferenceNo = refferenceNo;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getRevisedWorkOrderNo() {
		return revisedWorkOrderNo;
	}

	public void setRevisedWorkOrderNo(String revisedWorkOrderNo) {
		this.revisedWorkOrderNo = revisedWorkOrderNo;
	}

	public String getActualWorkOrderNo() {
		return actualWorkOrderNo;
	}

	public void setActualWorkOrderNo(String actualWorkOrderNo) {
		this.actualWorkOrderNo = actualWorkOrderNo;
	}

	public String getScopeOfWOrk1() {
		return scopeOfWOrk1;
	}

	public void setScopeOfWOrk1(String scopeOfWOrk1) {
		this.scopeOfWOrk1 = scopeOfWOrk1;
	}

	public String getScopeOfWOrk() {
		return scopeOfWOrk;
	}

	public void setScopeOfWOrk(String scopeOfWOrk) {
		this.scopeOfWOrk = scopeOfWOrk;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBoqNo() {
		return boqNo;
	}

	public void setBoqNo(String boqNo) {
		this.boqNo = boqNo;
	}

	public String getIsUpdateWOPage() {
		return isUpdateWOPage;
	}

	public void setIsUpdateWOPage(String isUpdateWOPage) {
		this.isUpdateWOPage = isUpdateWOPage;
	}

	public String getTotalWoAmount() {
		return totalWoAmount;
	}

	public void setTotalWoAmount(String totalWoAmount) {
		this.totalWoAmount = totalWoAmount;
	}

	public String getWorkOrderName() {
		return workOrderName;
	}

	public void setWorkOrderName(String workOrderName) {
		this.workOrderName = workOrderName;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getWorkOrderStatus() {
		return workOrderStatus;
	}

	public void setWorkOrderStatus(String workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}

/*	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;

	}*/

	public String getWorkOrderDate() {
		return workOrderDate;
	}

	public void setWorkOrderDate(String workOrderDate) {
		this.workOrderDate = workOrderDate;
	}

	public String getNoteComment() {
		return noteComment;
	}

	public void setNoteComment(String noteComment) {
		this.noteComment = noteComment;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String purpose) {
		Purpose = purpose;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWO_MajorHead1() {
		return WO_MajorHead1;
	}

	public void setWO_MajorHead1(String wO_MajorHead1) {
		WO_MajorHead1 = wO_MajorHead1;
	}

	public String getWO_MinorHead1() {
		return WO_MinorHead1;
	}

	public void setWO_MinorHead1(String wO_MinorHead1) {
		WO_MinorHead1 = wO_MinorHead1;
	}

	public String getWO_Desc1() {
		return WO_Desc1;
	}

	public void setWO_Desc1(String wO_Desc1) {
		WO_Desc1 = wO_Desc1;
	}

	public String getWoManualDesc1() {
		return woManualDesc1;
	}

	public void setWoManualDesc1(String woManualDesc1) {
		this.woManualDesc1 = woManualDesc1;
	}

	public String getUnitsOfMeasurement1() {
		return UnitsOfMeasurement1;
	}

	public void setUnitsOfMeasurement1(String unitsOfMeasurement1) {
		UnitsOfMeasurement1 = unitsOfMeasurement1;
	}

	public String getQuantity1() {
		return Quantity1;
	}

	public void setQuantity1(String quantity1) {
		Quantity1 = quantity1;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getAcceptedRate1() {
		return AcceptedRate1;
	}

	public void setAcceptedRate1(String acceptedRate1) {
		AcceptedRate1 = acceptedRate1;
	}

	public String getTotalAmount1() {
		return TotalAmount1;
	}

	public void setTotalAmount1(String totalAmount1) {
		TotalAmount1 = totalAmount1;
	}

	public int getIndexNumber1() {
		return indexNumber1;
	}

	public void setIndexNumber1(int indexNumber1) {
		this.indexNumber1 = indexNumber1;
	}

	public String getWoMajorHead() {
		return woMajorHead;
	}

	public void setWoMajorHead(String woMajorHead) {
		this.woMajorHead = woMajorHead;
	}

	public String getWoMinorHeads() {
		return woMinorHeads;
	}

	public void setWoMinorHeads(String woMinorHeads) {
		this.woMinorHeads = woMinorHeads;
	}

	public String getwODescription() {
		return wODescription;
	}

	public void setwODescription(String wODescription) {
		this.wODescription = wODescription;
	}

	public String getwOManualDescription() {
		return wOManualDescription;
	}

	public void setwOManualDescription(String wOManualDescription) {
		this.wOManualDescription = wOManualDescription;
	}

	public String getUnitsOfMeasurement() {
		return UnitsOfMeasurement;
	}

	public void setUnitsOfMeasurement(String unitsOfMeasurement) {
		UnitsOfMeasurement = unitsOfMeasurement;
	}

	public String getAcceptedRate() {
		return AcceptedRate;
	}

	public void setAcceptedRate(String acceptedRate) {
		AcceptedRate = acceptedRate;
	}

	public String getBasicAmount() {
		return basicAmount;
	}

	public void setBasicAmount(String basicAmount) {
		this.basicAmount = basicAmount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getComments1() {
		return comments1;
	}

	public void setComments1(String comments1) {
		this.comments1 = comments1;
	}

	public int getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getFloor_id() {
		return floor_id;
	}

	public void setFloor_id(String floor_id) {
		this.floor_id = floor_id;
	}

	public String getFloor_name() {
		return floor_name;
	}

	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}

	public String getFlat_id() {
		return flat_id;
	}

	public void setFlat_id(String flat_id) {
		this.flat_id = flat_id;
	}

	public String getFlat_name() {
		return flat_name;
	}

	public void setFlat_name(String flat_name) {
		this.flat_name = flat_name;
	}

	public String getBlock_name() {
		return block_name;
	}

	public void setBlock_name(String block_name) {
		this.block_name = block_name;
	}

	public String getBlock_id() {
		return block_id;
	}

	public void setBlock_id(String block_id) {
		this.block_id = block_id;
	}

	public String getWorkAreaId() {
		return workAreaId;
	}

	public void setWorkAreaId(String workAreaId) {
		this.workAreaId = workAreaId;
	}

	public String getActualArea() {
		return actualArea;
	}

	public void setActualArea(String actualArea) {
		this.actualArea = actualArea;
	}

	public String getSelectedArea() {
		return selectedArea;
	}

	public void setSelectedArea(String selectedArea) {
		this.selectedArea = selectedArea;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getWo_measurmen_name() {
		return wo_measurmen_name;
	}

	public void setWo_measurmen_name(String wo_measurmen_name) {
		this.wo_measurmen_name = wo_measurmen_name;
	}

	public String getQS_Temp_Issue_Id() {
		return QS_Temp_Issue_Id;
	}

	public void setQS_Temp_Issue_Id(String qS_Temp_Issue_Id) {
		QS_Temp_Issue_Id = qS_Temp_Issue_Id;
	}

	public String getQS_Temp_Issue_Dtls_Id() {
		return QS_Temp_Issue_Dtls_Id;
	}

	public void setQS_Temp_Issue_Dtls_Id(String qS_Temp_Issue_Dtls_Id) {
		QS_Temp_Issue_Dtls_Id = qS_Temp_Issue_Dtls_Id;
	}

	public String getSiteWiseWONO() {
		return siteWiseWONO;
	}

	public void setSiteWiseWONO(String siteWiseWONO) {
		this.siteWiseWONO = siteWiseWONO;
	}

	public String getWorkOrderCreateEmpId() {
		return workOrderCreateEmpId;
	}

	public void setWorkOrderCreateEmpId(String workOrderCreateEmpId) {
		this.workOrderCreateEmpId = workOrderCreateEmpId;
	}

	public String getWorkOrderCreadeDate() {
		return workOrderCreadeDate;
	}

	public void setWorkOrderCreadeDate(String workOrderCreadeDate) {
		this.workOrderCreadeDate = workOrderCreadeDate;
	}

/*	public String getFromEmpId() {
		return fromEmpId;
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
	}*/

	public String getWorkorderFrom() {
		return workorderFrom;
	}

	public void setWorkorderFrom(String workorderFrom) {
		this.workorderFrom = workorderFrom;
	}

	public String getWorkorderTo() {
		return workorderTo;
	}

	public void setWorkorderTo(String workorderTo) {
		this.workorderTo = workorderTo;
	}

	public String getApproverEmpId() {
		return approverEmpId;
	}

	public void setApproverEmpId(String approverEmpId) {
		this.approverEmpId = approverEmpId;
	}

	public String getPendingEmpId() {
		return pendingEmpId;
	}

	public void setPendingEmpId(String pendingEmpId) {
		this.pendingEmpId = pendingEmpId;
	}

	public String getContractorId() {
		return contractorId;
	}

	public void setContractorId(String contractorId) {
		this.contractorId = contractorId;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getContractorAddress() {
		return contractorAddress;
	}

	public void setContractorAddress(String contractorAddress) {
		this.contractorAddress = contractorAddress;
	}

	public String getContractorPhoneNo() {
		return contractorPhoneNo;
	}

	public void setContractorPhoneNo(String contractorPhoneNo) {
		this.contractorPhoneNo = contractorPhoneNo;
	}

	public String getContractorPanNo() {
		return contractorPanNo;
	}

	public void setContractorPanNo(String contractorPanNo) {
		this.contractorPanNo = contractorPanNo;
	}

	 

	public String getTypeOfWork() {
		return typeOfWork;
	}

	public void setTypeOfWork(String typeOfWork) {
		this.typeOfWork = typeOfWork;
	}

	public String getWorkOrderNo() {
		return workOrderNo;
	}

	public void setWorkOrderNo(String workOrderNo) {
		this.workOrderNo = workOrderNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("WorkOrderBean [siteId=")
		.append(siteId)
		.append(", siteName=")
		.append(siteName)
		.append(", workOrderStatus=")
		.append(workOrderStatus)
		.append(", isUpdateWOPage=")
		.append(isUpdateWOPage)
		.append(", workOrderDate=")
		.append(workOrderDate)
		.append(", workOrderName=")
		.append(workOrderName)
		.append(", siteWiseWONO=")
		.append(siteWiseWONO)
		.append(", userId=")
		.append(userId)
		.append(", contractorId=")
		.append(contractorId)
		.append(", contractorName=")
		.append(contractorName)
		.append(", typeOfWork=")
		.append(typeOfWork)
		.append(", workOrderNo=")
		.append(workOrderNo)
		.append(", actualWorkOrderNo=")
		.append(actualWorkOrderNo)
		.append(", revisedWorkOrderNo=")
		.append(revisedWorkOrderNo)
		.append(", versionNumber=")
		.append(versionNumber)
		.append(", revision=")
		.append(revision)
		.append(", oldWorkOrderNo=")
		.append(oldWorkOrderNo)
		.append(", approverEmpId=")
		.append(approverEmpId)
		.append(", isSaveOrUpdateOperation=")
		.append(isSaveOrUpdateOperation)
		.append(", requestFromMail=")
		.append(requestFromMail)
		.append(", remarks=")
		.append(remarks)
		.append("]");
		return builder.toString();
	}

	
}
