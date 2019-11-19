package com.sumadhura.transdao;

import java.util.List;

import java.util.Map;

import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.bean.ContractorQSBillBean;

public interface ContrctorBillGeneraterDao {

	List<Object> generateAdvanceRAPaymentBill(ContractorQSBillBean bean) throws Exception;

	List<Map<String, Object>> loadWorkOrderAreaForBill(String contractorId, String workDesc, String workOrderNo, String siteId,
			String userId, String billType);

	List<Map<String, Object>> getAdvPaymentCertificateDetail(ContractorQSBillBean bean);

	int insertPaymentAreaDetails(String[] workAreaId, String[] qty, int tempBllSeq, String paybleAmt, String operType, String[] actualQty, String[] initiatedArea, String[] wOQuantityRateArray, String[] totalWOQuantityArray);

	List<ContractorQSBillBean> getPendingWorkOrderBils(String user_id, String siteId);

	int insertTempBillApprRejDetail(ContractorQSBillBean bean, String operType);

	List<ContractorQSBillBean> getPendingWorkOrderContractorBillDetailById(String tmpBillNo, String userId, String billType, String status);

	List<Map<String, Object>> loadWorkOrderAreaForApprovelBill(String contractorId, String billType, String workOrderNo,
			String siteId, String userId);

	int saveChangedAdvBillDetails(ContractorQSBillBean actualBill, ContractorQSBillBean changedBill);

	int updateBillLevel(ContractorQSBillBean changedBill) throws Exception;

	boolean isValidTempBillNo(String[] data);

/*	int getRABillNo(String[] data);*/

	int rejectAdvanceGeneratedBill(String tempBillNo);

	List<Map<String, Object>> loadBillChangedDetails(String tmpBillNo, String siteId, String userId);

	List<Map<String, Object>> getRaAdvDeductionDetails(String tmpBillNo, String userId, String status);

	int updateChangedRADeductBillDetails(ContractorQSBillBean actualBill, ContractorQSBillBean changedBill);

	WorkOrderBean getWorkOrderFromAndToDetails(String userId, WorkOrderBean workOrderBean);

	int updateRemarks(String tempBillNo, String billRemarks);

	List<ContractorQSBillBean> getCompletdWorkOrderBills( String siteId, ContractorQSBillBean billBean);

	boolean isValidCompltedTempBillNo(String[] data);

	List<ContractorQSBillBean> getCompltedWorkOrderContractorBillDetailById(ContractorQSBillBean qsBillBean);

	List<Map<String, Object>> getRaAdvCompletedDeductionDetails(ContractorQSBillBean bean);

	String getWorkOrderPendingEmployeeId(String user_id,String site_id) throws Exception;

	List<Map<String, Object>> getAllDedcutedAmount(ContractorQSBillBean bean);

	int getContractorPaidAmount(ContractorQSBillBean bean);

	List<Map<String, Object>> loadDeductionTypes(String data);

	List<Map<String, Object>> loadPermanentWorkOrderArea(String tempBillNo, String workDesc, String workOrderNo,
			String siteId, String userId, String billType);

	List<Map<String, Object>> loadRecoveryAreaDetails(String[] data);

	int insertIntoBoqRecovery(ContractorQSBillBean billBean);

	int insertIntoBoqRecoveryHistory(ContractorQSBillBean billBean);

	int removeAmountFromBoqRecovery(ContractorQSBillBean billBean);

	int deleteFromBoqRecoveryHistory(ContractorQSBillBean billBean);

	int updateBoqRecovery(ContractorQSBillBean billBean, int condition);

	int updateBoqRecoveryHistory(ContractorQSBillBean billBean, int condition);

	boolean isAnyRaAndAdvBillPending(ContractorQSBillBean billBean);

	//List<ContractorQSBillBean> getBillsForUpdate(String siteId, String billType);
	String loadAdvRAPermanentBillNo(ContractorQSBillBean billBean, String  initials);
	String getWordDescNameAndId(ContractorQSBillBean bean);
	String checkBillNoExistsOrNot(ContractorQSBillBean billBean);
	
	 List<Map<String, Object>> loadNMRBillData(ContractorQSBillBean billBean);

	 int insertNMRData(List<ContractorQSBillBean> listOfNMRData);

	List<Object> insertNMRBillData(ContractorQSBillBean bean ) throws Exception;

	List<Map<String, Object>> loadNMRBillDataForApproval(ContractorQSBillBean billBean);

	int approveNMRBill(ContractorQSBillBean nMRbean) throws Exception;

	int updateNMRROWData(ContractorQSBillBean bean);

	int rejectNMRBill(ContractorQSBillBean nMRbean);

	List<Map<String, Object>> loadNMRBillDeatilsByBillNo(ContractorQSBillBean billBean);

	List<Map<String, Object>> loadWorkDescNMRBills(ContractorQSBillBean billBean);

	List<Map<String, Object>> loadPermanentNMRBillData(ContractorQSBillBean billBean);

	List<Map<String, Object>> loadPermanentNMRBillDetailsData(ContractorQSBillBean billBean);

	List<String> loadSiteAddress(ContractorQSBillBean qsBillBean);

	int updateNMRROWDataRemarks(ContractorQSBillBean changedNMRBillData);

	List<Map<String, Object>> loadPermanentRecoveryAreaDetails(String[] data);

	public List<ContractorQSBillBean> getSitewiseCompletdWorkOrderBills(String string, String siteId, String billType);

	List<Map<String, Object>> getTempBillVerifiedEmpNames(ContractorQSBillBean billBean);


	String checkInvoiceBillNoExistsOrNot(ContractorQSBillBean billBean);
	List<Map<String, Object>> userAbleToCreateBillTypes(String user_id, WorkOrderBean workOrderBean);

	


	
}
