package com.sumadhura.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.WorkOrderBean;

public interface WorkOrderService {

	//List<Map<String, Object>> loadChildProduct(String prodId, String prodName);

	Map<String, String> loadQSProducts(String siteId, String typeOFWork);

	String loadWOSubProds(String mainProductId, String siteId, String typeOfWork);

	String loadWOChildProds(String subProductId, String siteId, String typeOfWork);

	String loadWorkOrderMeasurements(String childProductId, String siteId, String typeOfWork);

	List<String> getContractorInfo(String employeeName, String loadVendorData, String fromIndentReturns);

	List<Map<String, Object>> loadWOAreaMapping(String siteId, String mesuremnt, String unitsOfMeasurementId, String typeOfWork);

	int getQS_WO_Temp_Issue_Dtls();

	WorkOrderBean getWorkOrderFromAndToDetails(String user_id1, WorkOrderBean workOrderBean);

	int getMaxQSWorkOrderNoSiteWise(String siteId);

	String doWorkOrderEntry(WorkOrderBean workOrderBean, HttpServletRequest request, Model model, String siteId,
			String user_id1, HttpSession session);

	List<WorkOrderBean> getPendingWorkOrder(String user_id, String siteId,String statusType);

	boolean checkWorkOrderNumberIsValidForEmployee(String workOrderNo, String user_id, boolean status, String statusType);

	String getWorkOrderNumber(String siteWiseWorkOrderNo, int site_Id, String user_id);

	WorkOrderBean getWorkOrderDetailsByWorkOrderId(String workOrderNo);

	List<WorkOrderBean> getCreatedWorkOrderDetails(WorkOrderBean workOrderBean);

	List<Map<String, Object>> loadWOAreaMappingByWorkOrderNo(String siteId, String selectALLData, String selectALLData2, String operType, String mesurment, String unitsOfMeasurementId, String typeOfWork);

	String approveWorkOrderCreation(WorkOrderBean workOrderBean, HttpServletRequest request, HttpSession session, Model model, String site_Id, String user_id, MultipartFile[] file);

	String rejectWorkOrderCreation(WorkOrderBean workOrderBean, Model model, HttpSession session, HttpServletRequest request, String site_Id, String user_id);

	List<WorkOrderBean> getMyWOPendingStatusDetail(String fromDate, String toDate, String site_Id,String workOrderNumber);

	List<WorkOrderBean> getDeletedProductDetailsLists(String workOrderNo, String user_id, int site_Id, String TypeOfWork);

	List<WorkOrderBean> getMyWOCompltedDetail(String fromDate, String toDate, String site_Id, String workOrderNumber, String user_id, String string);

	boolean checkCompletedWorkOrderNumberIsValidForEmployee(String siteWiseWorkOrderNo, String user_id, boolean status, String site_Id, String operType);

	List<WorkOrderBean> getALLCompltedWorkOrderS(String workOrderNo, String user_id, String site_Id, boolean status);

	WorkOrderBean getCompletedWorkOrderDetailsByWorkOrderNumber(String workOrderNo, String workOrderIssueNo);

	String getPermanentWorkOrderIssueIdNumber(String workOrderNo, String site_Id, String user_id);

	List<WorkOrderBean> getTermsAndConditions(String siteId);

	List<Map<String, Object>> loadTermsAndConditions(String tempIssueId, String operType);
	
	public Map<String,String> loadWorkDsc(ContractorQSBillBean billBean);
	
	public String getWorkOrderDetails(String steWorkDescId,String strControctorId, int siteId, String strTypeOfWork);

	String updatePermanentWorkOrderDetails(WorkOrderBean orderBean, HttpServletRequest request, HttpSession session,
			Model model, String site_Id, String user_id, MultipartFile[] files);

	String rejectPermanentWorkOrder(Model model, HttpSession session, HttpServletRequest request, String site_Id,
			String user_id);

	List<WorkOrderBean> getMyTempWOForUpdateDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String user_id);

	String getWorkOrderNoSiteWise(String siteId, String siteName, String woTypeOfWork);

	String loadScopeOfWork(WorkOrderBean bean);

	Map<String, String> loadNMRProducts(String[] data);

	String doNMRWorkOrderEntry(WorkOrderBean orderBean, HttpServletRequest request, Model model, String siteId,
			String user_id1, HttpSession session);

	String checkWorkOrderNoExistsOrNot(String workOrderNo, String siteId);

	List<Map<String, Object>> loadWOAreaMappingForReviseByWorkOrderNo(WorkOrderBean bean);

	String doReviseWorkOrder (WorkOrderBean orderBean, HttpServletRequest request, Model model,
			String siteId, String user_id, HttpSession session) throws Exception;

	List<Map<String, Object>> getNewRevisedWorkOrderName(String user_id, WorkOrderBean bean);

	List<Map<String, Object>> loadWOAreaMappingForRevise(WorkOrderBean bean);

	public List<WorkOrderBean> getSitewiseWOPendingStatusDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber);

	public List<WorkOrderBean> getSitewiseWOCompltedDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String user_id, String contractorId);

	List<WorkOrderBean> getCompltedWorkOrderDetailForRevise(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String typeOfWork, String contractorId);

	String doReviseNMRWorkOrderEntry(WorkOrderBean orderBean, HttpServletRequest request, Model model, String siteId,
			String user_id, HttpSession session);

	String loadScopeOfWorkAndBOQAmount(WorkOrderBean bean);

	List<Map<String, Object>> getModificationDetailsList(String workOrderIssueNo, String user_id);

	List<Map<String, Object>> getWorkOrderVerifiedEmpNames(WorkOrderBean bean);

	List<Map<String, Object>> getPermanentWorkOrderVerifiedEmpNames(WorkOrderBean bean);

	List<WorkOrderBean> getAllRevisedWorkOrderList(WorkOrderBean workOrderBean, String typeOfWork);

	Map<String, String> validateWOMaterialBOQDetails(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, JSONException, Exception;

	 List<Map<String, Object>>  getMaterialBOQProductDetails(WorkOrderBean workOrderBean);

	boolean checkIsAnyBillisInActiveMode(WorkOrderBean bean);

	String checkIsDraftWorkOrderExistsOrNot(HttpServletRequest request, HttpSession session);

	List<Map<String, Object>> getTempMaterialBOQProductDetails(
			WorkOrderBean workOrderBean);

	List<Map<String, Object>> getTempWorkDescMaterialBOQProductDetails(HttpServletRequest request, HttpSession session);

	List<Map<String, Object>> getTempWorkAreaWiseMaterialDetails(HttpServletRequest request, HttpSession session);

	List<Map<String, Object>> autoCompleteWorkOrderNo(HttpServletRequest request);

	List<Map<String, Object>> userAbleToCreateWOTypes(String user_id, WorkOrderBean workOrderBean);

	String approveWorkOrderFromMail(WorkOrderBean bean, HttpServletRequest request, HttpSession session);

	String rejectOrModifyWorkOrderFromMail(WorkOrderBean bean, HttpServletRequest request, HttpSession session);
 
	Map<String, Object> loadActiveWorkOrderDetails(WorkOrderBean bean);

}
