package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.bean.ContractorQSBillBean;

public interface ContrctorBillGeneraterService {

	String generateAdvancePaymentBill(HttpServletRequest request, HttpSession session) throws Exception;

	List<Map<String, Object>> loadWorkOrderAreaForBill(String contractorId, String workDesc, String workOrderNo, String siteId,
			String userId, String billType);

	List<Map<String, Object>> getAdvPaymentCertificateDetail(ContractorQSBillBean bean);

	List<ContractorQSBillBean> getPendingWorkOrderBils(String user_id, String siteId);

	List<ContractorQSBillBean> getPendingWorkOrderContractorBillDetailById(String tmpBillNo, String userId, String billType, String status);

	List<Map<String, Object>> loadWorkOrderAreaForApprovelBill(String contractorId, String billType, String workOrderNo,
			String siteId, String userId);

	String approveWorkOrderBill(HttpServletRequest request, HttpSession session);

	boolean isValidTempBillNo(String[] data);
 

	String createWorkOrderRABill(HttpServletRequest request, HttpSession session) throws Exception;

	String rejctAdvanceBill(HttpServletRequest request, HttpSession session);


	List<Map<String, Object>> loadBillChangedDetails(String tmpBillNo, String siteId, String userId);

	String approveWoRABill(HttpServletRequest request, HttpSession session);

	List<Map<String, Object>> getRaAdvDeductionDetails(String tmpBillNo, String userId, String status);

	String rejectRABill(HttpServletRequest request, HttpSession session);

	WorkOrderBean getWorkOrderFromAndToDetails(String userId, WorkOrderBean workOrderBean);

	String uploadBOQ();

	List<ContractorQSBillBean> getCompletdWorkOrderBills( String siteId, ContractorQSBillBean billBean);

	boolean isValidCompltedTempBillNo(String[] data);

	List<ContractorQSBillBean> getCompltedWorkOrderContractorBillDetailById(ContractorQSBillBean qsBillBean);

	List<Map<String, Object>> getRaAdvCompletedDeductionDetails(ContractorQSBillBean bean);

	List<Map<String, Object>> getAllDedcutedAmount(ContractorQSBillBean bean);

	int getContractorPaidAmount(ContractorQSBillBean bean);

	List<Map<String, Object>> loadDeductionTypes(String ids);

	List<Map<String, Object>> loadPermanentWorkOrderArea(String tempBillNo, String string, String workOrderNo,
			String siteId, String userId, String billType);

	List<Map<String, Object>> loadRecoveryAreaDetails(String[] data);

	boolean isAnyRaAndAdvBillPending(ContractorQSBillBean billBean);

//	List<ContractorQSBillBean> getBillsForUpdate(String siteId, String billType);

	String loadAdvRAPermanentBillNo(ContractorQSBillBean billBean);
	
	String checkBillNoExistsOrNot(HttpServletRequest request, HttpSession session);
	
	List<Map<String, Object>> loadWorkDescNMRBills(ContractorQSBillBean billBean);
		 
	List<Map<String, Object>> loadNMRBillData(ContractorQSBillBean billBean);

	String generateNMRBill(HttpServletRequest request, HttpSession session) throws Exception;

	List<Map<String, Object>> loadNMRBillDataForApproval(ContractorQSBillBean billBean);

	String approveNMRBill(HttpServletRequest request, HttpSession session) throws Exception;

	String rejectNMRBill(HttpServletRequest request, HttpSession session);

	List<Map<String, Object>> loadNMRBillDeatilsByBillNo(ContractorQSBillBean billBean);

	List<Map<String, Object>> loadPermanentNMRBillData(ContractorQSBillBean billBean);

	List<Map<String, Object>> loadPermanentNMRBillDetailsData(ContractorQSBillBean billBean);

	List<String> loadSiteAddress(ContractorQSBillBean qsBillBean);

	List<Map<String, Object>> loadPermanentRecoveryAreaDetails(String[] data);

	public List<ContractorQSBillBean> getSitewiseCompletdWorkOrderBills(String string, String siteId, String billType);

	List<Map<String, Object>> getTempBillVerifiedEmpNames(ContractorQSBillBean billBean);


	List<Map<String, Object>> userAbleToCreateBillTypes(String user_id, WorkOrderBean workOrderBean);
}
