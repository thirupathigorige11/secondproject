package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.WorkOrderBean;

public interface WorkOrderDao {

	//List<Map<String, Object>> loadChildProduct(String prodId, String prodName);

	Map<String, String> loadQSProducts(String siteId, String typeOFWork);

	String loadWOSubProds(String mainProductId, String siteId, String typeOfWork);

	String loadWOChildProds(String subProductId, String siteId, String typeOfWork);

	String loadWorkOrderMeasurements(String childProductId, String siteId, String typeOfWork);

	List<String> getContractorInfo(String vendorName, boolean flag, String fromIndentReturns);

	List<Map<String, Object>> loadWOAreaMapping(String siteId, String mesuremnt, String unitsOfMeasurementId, String typeOfWork);

	int getQS_WO_Temp_Issue_Dtls();

	WorkOrderBean getWorkOrderFromAndToDetails(String user_id1, WorkOrderBean workOrderBean);

	int getMaxQSWorkOrderNo(String siteId);

	List<Number> insertWorkOrderDetail(WorkOrderBean workOrderBean, String workOrderCreateEmpId);

	int inserWorkOrderCreationDetail(WorkOrderBean workOrderBean, String OperType);

	

	List<WorkOrderBean> getPendingWorkOrder(String user_id, String siteId,String statusType);

	boolean checkWorkOrderNumberIsValidForEmployee(String workOrderNo, String user_id, boolean status, String statusType);

	String getWorkOrderNumber(String siteWiseWorkOrderNo, int site_Id, String user_id);

	WorkOrderBean getWorkOrderDetailsByWorkOrderId(String workOrderNo);

	List<WorkOrderBean> getCreatedWorkOrderDetails(WorkOrderBean workOrderBean);

	List<Integer> insertWorkOrderTempIssueDetails(WorkOrderBean bean, String workOrderTMPIssurPK);

	void updateWorkAreaMapping(WorkOrderBean bean, String nextApprovelEmpId, String opeType);

	List<Map<String, Object>> loadWOAreaMappingByWorkOrderNo(String acceptedArea, String siteId, String selectALLData2, String operType, String mesurment, String unitsOfMeasurementId, String typeOfWork);

	int updatePendingWorkOrder(WorkOrderBean workOrderBean, String tempIssueId, String workOrderNoSiteWise, String nextApprovelEmpId, String insertType);

	//String getWorkOrderPendingEmployeeId1(String user_id);

	int updateWorkOrderComments(String tempIssueId, String actualComments, String changedAcceptedRate);

	int getChnagedWorkOrderSeqNumber();

	int updateWorkOrderAreaDetails( WorkOrderBean changedWorkOderDetail, WorkOrderBean actualWorkOderDetail);

	int insertWorkOrderChangedDetails(WorkOrderBean actualWorkOderDetail, WorkOrderBean changedWorkOderDetail,
			String tempIssueId, String nextApprovelEmpId, String string, String user_id, String strFinalChangedComments);

	int doInActiveWorkOder(String string, String tempIssueId, String string2) throws Exception;

	int updateWorkOrderPriceByTmpIssuId(String string, String changedQuantity, String tempIssueId, String actualAcceptedRate,String changedTotalAmount, String changedQuantity2);

	int rejectWorkOrderCreationCreation(WorkOrderBean bean);

	List<WorkOrderBean> getMyWorkOrderStatus(String fromDate, String toDate, String site_Id, String workOrderNumber);

	List<WorkOrderBean> getDeletedProductDetailsLists(String workOrderNo, String user_id, int site_Id, String typeOfWork);

	List<WorkOrderBean> getMyWOCompltedDetail(String fromDate, String toDate, String site_Id, String workOrderNumber, String user_id, String contractorId);

	boolean checkCompletedWorkOrderNumberIsValidForEmployee(String siteWiseWorkOrderNo, String user_id, boolean status, String site_Id, String operType);

	List<WorkOrderBean> getALLCompltedWorkOrderS(String workOrderNo, String user_id, String site_Id, boolean status);

	WorkOrderBean getCompletedWorkOrderDetailsByWorkOrderNumber(String workOrderNo, String workOrderIssueNo);

	String getPermanentWorkOrderIssueIdNumber(String workOrderNo, String site_Id, String user_id);

	int insertTermsAndConditions(WorkOrderBean workOrderBean, String workOrderTMPIssurPK, String[] termsAndConditions);

	List<Map<String, Object>> loadTermsAndConditions(String tempIssueId, String opertype);

	int updateTheTermsAndCondition(String tempIssueId);

	int updatePurpose(String workOrderNoSiteWise, String purpose);
	public Map<String,String> loadWorkDsc(ContractorQSBillBean billBean);
	
	public String getWorkOrderDetails(String steWorkDescId,String strControctorId, int SiteId, String strTypeOfWork);

	int doInActiveWorkOder(String tempIssueId, String actualAcceptedRate);

	List<Map<String, Object>> getTempIssDTLSId(String tempIssueId, String actualAcceptedRate, boolean isDelete);

	int insertPermanentTermsAndConditions(WorkOrderBean workOrderBean, String tempIssueId, String[] changedTC);

	int updatePermanentWOPurpose(String workOrderNoSiteWise, String changedWorkOrderLevelPurpose);

	int doInActivePermanentWorkOder(String tempIssueId, String actualTotalAmount);

	List<Map<String, Object>> getPermanentTempIssDTLSId(String tempIssueId, String actualTotalAmount, boolean b);

	int updatePermanentWorkOrder(WorkOrderBean workOrderBean, String tempIssueId, String workOrderNoSiteWise,String nextApprovelEmpId, String string);

	int updatePermanentWorkOrderPriceByTmpIssuId(String string, String changedAcceptedRate, String tempIssueId,String actualTotalAmount, String changedTotalAmount, String changedQuantity);

	int updatePermanentWorkOrderCreationDetails( WorkOrderBean changedWorkOderDetail,WorkOrderBean actualWorkOderDetail);

	List<Integer> insertPermanentQSTEMPDetails(WorkOrderBean bean, String tempIssueId);

	int updatePermanentWorkOrderComments(String tempIssueId, String changedComments, String changedAcceptedRate);

	int rejectPermanentWorkOrderCreationCreation(WorkOrderBean bean);

	List<Integer> WorkOrderTempIssueAreaDetails(WorkOrderBean bean, String workOrderTMPIssurPK);

	List<WorkOrderBean> getMyTempWOForUpdateDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String user_id);

	int doInActivePermanentCurrentWorkOder(String string, String tempIssueId, String string2);

	String getWorkOrderNoSiteWise(String siteId, String siteName, String woTypeOfWork);

	int insertIntoWOReservedNum(WorkOrderBean bean);

	String loadScopeOfWork(WorkOrderBean bean);

	Map<String, String> loadNMRProducts(String[] data);

	String checkWorkOrderNoExistsOrNot(String workOrderNo, String siteId);

	int deleteWOCommitedData(String workOrderTMPIssurPK, String siteId);

	List<Map<String, Object>> loadWOAreaMappingForReviseByWorkOrderNo(WorkOrderBean bean);

	List<Map<String, Object>> getNewRevisedWorkOrderName(String user_id, WorkOrderBean bean);

	List<Map<String, Object>> loadWOAreaMappingForRevise(WorkOrderBean bean);

	public List<WorkOrderBean> getSitewiseWorkOrderStatus(String fromDate, String toDate, String site_Id,
			String workOrderNumber);

	public List<WorkOrderBean> getSitewiseWOCompltedDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String user_id, String contractorId);
	List<Number> insertReviseWorkOrderDetail(WorkOrderBean workOrderBean, String workOrderCreateEmpId);

	int revertAlltheAllocatedQuantity(String actualtempIssueId);

	List<WorkOrderBean> getCompltedWorkOrderDetailForRevise(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String typeOfWork, String contractorId);

	String loadScopeOfWorkAndBOQAmount(WorkOrderBean bean);

	List<Map<String, Object>> getModificationDetailsList(String workOrderIssueNo, String user_id);

	List<Map<String, Object>> getWorkOrderVerifiedEmpNames(WorkOrderBean bean);

	List<Map<String, Object>> getPermanentWorkOrderVerifiedEmpNames(WorkOrderBean bean);

	List<WorkOrderBean> getAllRevisedWorkOrderList(WorkOrderBean workOrderBean, String typeOfWork);

	 List<Map<String, Object>>  loadMaterialBoqDetailsForWO(WorkOrderBean bean);

	int insertDataIntoWorkOrderTempProductDtls(List<WorkOrderBean> listOfMaterialBoq,String operationType);

	int deleteWorkOrderTempProductDtls(String string, String string2);

	List<Map<String, Object>> loadWorkOrderMaterialBOQ(WorkOrderBean bean);

	int insertWOmaterialChangedDetails(WorkOrderBean actualWorkOderDetail, WorkOrderBean changedWorkOderDetail, String materialModified, int changedDtlsPK, String actionType);

	Map<String, String> validateWOMaterialBOQDetails(List<WorkOrderBean> list, String siteId, String workOrderNumber, String contractorId, String operationType, String condition) throws Exception;

	 List<Map<String, Object>>  getMaterialBOQProductDetails(WorkOrderBean workOrderBean);

	boolean checkIsAnyBillisInActiveMode(WorkOrderBean bean);

	int sendToModifyWorkOrder(WorkOrderBean workOrderBean);

	int updateWorkOrderRowScope(String string, String woManualDesc);

	int changeWorkOrderApprRejDetail(WorkOrderBean workOrderBean, String actionType);

	String checkIsDraftWorkOrderExistsOrNot(String workOrderNo, String contractorName,String typeOfWork, String siteId);

	List<Map<String, Object>> getTempMaterialBOQProductDetails(
			WorkOrderBean workOrderBean);

	int insertCompletedWorkOrder(WorkOrderBean workOrderBean, String workOrderTMPIssurPK, String siteWiseWONO,
			String nextApprovelEmpId);

	List<Map<String, Object>> getTempWorkDescMaterialBOQProductDetails(WorkOrderBean workOrderBean);

	List<Map<String, Object>> getTempWorkAreaWiseMaterialDetails(WorkOrderBean workOrderBean);

	List<Map<String, Object>> autoCompleteWorkOrderNo(int siteId, String strTypeOfWork, String workOrderNo);

	List<Map<String, Object>> userAbleToCreateWOTypes(String user_id, WorkOrderBean workOrderBean);

	int takeTempWorkOrderQuantityBack(WorkOrderBean bean,String tempIssueId);

	int batchUpdateWorkAreaMapping(List<WorkOrderBean> updateWorkAreaDetails, String string);

	int updateWorkOrderDetail(WorkOrderBean bean);

	int rejectWorkOrderFromMail(WorkOrderBean workOrderBean);

	

	Map<String, Object> loadActiveWorkOrderDetails(WorkOrderBean bean);
}
