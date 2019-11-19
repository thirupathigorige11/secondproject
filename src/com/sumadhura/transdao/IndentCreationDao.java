package com.sumadhura.transdao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.IndentReceiveDto;

public interface IndentCreationDao {

	public int insertIndentCreation(int indentCreationSeqNum, IndentCreationDto indentCreationDto);

	public String getPendingEmployeeId(String user_id, int siteId);

	public int getIndentCreationDetailsSequenceNumber();

	public int insertIndentCreationDetails(int indentCreationDetailsSeqNum, int indentCreationSeqNum,
			IndentCreationDetailsDto indentCreationDetailsDto);

	public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentCreationSeqNum,
			IndentCreationDto indentCreationDto);

	public List<IndentCreationBean> getIndentFromAndToDetails(String centralDeptId);

	public int rejectIndentCreation(int indentCreationSeqNum, IndentCreationDto indentCreationDto);

	
	public int insertIndentChangedDetails(IndentCreationBean actualIndentDetails, IndentCreationBean changedIndentDetails, int indentNumber, int indentChangedDetailsSeqNum,int IndentCreationDetailsId,String indentChangeAction,String user_id);
	public int getIndentChangedDetailsSequenceNumber();

	public int insertCentralIndentProcess(int indentProcessId, IndentCreationBean changedIndentDetails, int IndentCreationDetailsId, int site_id);

	public int getCentralIndentProcessSequenceNumber();

	public String getPendingDeptId(String user_id, int siteId);

	public int updateIndentCreationDetails(int IndentCreationDetailsId, IndentCreationBean changedIndentDetails);

	public List<ProductDetails> getAllSubProducts(String prodId);

	public List<ProductDetails> getAllChildProducts(String strSubProdId);

	// public int updatePurchaseDepartmentIndentProcess(ProductDetails
	// productDetails,String strSattus);

	public int updateIndentCreationDetails(ProductDetails productDetails);

	public void inactiveIndent(ProductDetails productDetails);



	public List<IndentCreationBean> getViewMyRequestIndents(String centralDeptId, String siteId, String siteWiseIndentNo);
	public List<IndentCreationBean> getViewAllMyRequestIndents(String centralDeptId, int indentNumber);

	public List<IndentCreationBean> getViewissuedIndentDetailsLists(int indentNumber, String siteId);

	public int updateCentralProcessReqDtlsTable(int site_id, String user_id, IndentCreationBean indentCreationBean,int indentNumber);

	public int getRejectedDetails(Model model, HttpServletRequest request, int site_id, String user_id);

	public List<IndentCreationBean> getViewReceiveIndentDetails(String siteId);

	public List<IndentCreationBean> getViewReceivedIndentDetailsLists(int indentNumber);

	public int getReceivedDetails(IndentCreationBean indentCreationBean, int site_id, String user_id);

	public int updateReceivedDetails(double issuedQuantity, int intCentralIndentReqDtlsId);

	public List<IndentCreationBean> getViewReceiveDetails(int indentNumber);

	public int getRejectedQuantityDetails(Model model, HttpServletRequest request, int site_id, String user_id);

	public List<IndentCreationBean> getAndUpdateCentralIndentRequestDetails(int indentNumber);

	public int updateIndentCreationDetials(double issuedQuantity, int indentCreationDetailId);

	public List<IndentCreationBean> getAndUpdateCentralIndentProcess(int indentNumber);

	public List<IndentCreationBean> getAndUpdateIndentNo(int indentNumber);

	public List<Map<String, Object>> getCentralIndentProcessDetails( String strChildProductId, String strMesurmentId,String StrIndentNo);

	public List<Map<String, Object>> getIndentCreationDetails(int intIndentNumber, String aprroverTo);

	public List getAllEmployeeEmailsUnderDepartment(String deptId);

	public void getIndentFromDetails(int indentNumber, IndentCreationBean icb);

	public void getIndentToDetails(String strSiteId, String user_id, IndentCreationBean icb);

	public void getIndentToDetails2(String strSiteId, String user_id, IndentCreationBean icb);

	public int getIndentCreationApprovalSequenceNumber();

	public int insertIndentCreationApprovalAsApprove(int indentCreationApprovalSeqNum, int indentNumber,
			IndentCreationDto indentCreationDto);

	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId, IndentCreationDto indentCreationDto, String strChangedComments);

	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb, int siteId);// siteId
																											// for
																											// customer
																											// need

	public int getIndentCreationSequenceNumber();

	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber);

	public boolean checkIndentNumberIsValidForEmployee(int indentNumber, String user_id);

	public List<IndentCreationBean> getIndentCreationLists(int indentNumber);

	public String getTempPasswordOfIndent(int indentNumber);

	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId);

	public String getIndentFrom(int indentNumber);

	public String getIndentTo(String user_id);

	public int deleteRowInIndentCreationDetails(int IndentCreationDetailsId);

	public List<ViewIndentIssueDetailsBean> getRaisedIndentDetails(String fromDate, String toDate, String siteId,String indentNumber);

	public int updateProductsComments(int intIndentCreationDetailsId, String strRemarks);

	public String getVendorDetails(String indentNumber, String siteId, HttpServletRequest request, String siteName,	String vendorId) throws ParseException;

	public String getProductDetails(String poNumber, String siteId, HttpServletRequest request, String receiverState);

	public String getTransportChargesListForGRN(String poNumber, String strSiteId, String gstinumber,HttpServletRequest request, String tempPoNumber, String receiverState);

	public List<IndentCreationBean> getPoDetails(String fromDate, String toDate, String siteId, String poNumber,String sessionId, boolean allPoOrNot,String siteids,String vendorName);

	public List<IndentCreationBean> getClosedIndents(String fromDate, String toDate, String deptId,String indentNumber,String sessionId,String sites);

	public String getIndentLevelComments(int indentNo);

	public List<IndentCreationBean> getndentChangedDetails(int indentNo);

	public int getMaxOfSiteWiseIndentNumber(int siteId);

	public int getIndentNumber(int siteWiseIndentNumber, int site_Id);

	public int getSiteWiseIndentNo(int indentNumber);

	public List<IndentCreationBean> viewIndentProductDetails(String fromDate, String toDate, String siteId);

	public int updateIndentCreationForCentral(String pendingEmpId, int siteId, int indentNumber);

	public List<IndentCreationBean> getRejectedIndentsList(String userId);

	public List<String> getPendingEmployeeIdForIndent(String user_id, int reqSiteId);

	public String[] getEmailsOfEmployeesInAllLowerDeptOfThisEmployee(String indentNumber);

	public String getEmpdesignation(String user_Id);

	public String getIndentRejectedComments(int oldIndentNumber);

	public int updateIndentCreationApprovetbl(String oldIndentNumber);

	public List<Map<String, Object>> getIndentCreatedEmpName(String reqSiteId, int indentNumber);
	public Map<String,String> getChildProductsWithGroupId(String groupId);
	public double getindentAndDcReceivedQuantity(String childProductList,String siteId,String measurementList);
	public double getIssuedQuantity(String childProductList,String siteId,String measurementList);
	public double getIndentPendingQuantity(String childProductList,String siteId,String measurementList);
	public double getBOQQuantity(String groupId,String siteId);
	public double getIndentPreviousQuantity(String childProductList,String indentNumber,String siteId) ;

}
