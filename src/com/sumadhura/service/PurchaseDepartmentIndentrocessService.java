package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDto;

public interface PurchaseDepartmentIndentrocessService {



	public List<IndentCreationBean> getAllPurchaseIndents();

	public List<Map<String,Object>> getAllProducts();





	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber);





	public void printIndent(Model model, HttpServletRequest request, int site_id, String user_id);

	public List<ProductDetails> createPO(Model model, HttpServletRequest request,HttpSession session);

	public List<Map<String, Object>> sendenquiry(Model model, HttpServletRequest request, int site_id, String user_id,String siteId);


	public  List<Map<String, Object>> sendenquiry2(Model model, HttpServletRequest request, int site_id, String user_id,String siteName);


	public String SavePoDetails(Model model, HttpServletRequest request,HttpSession session,String strFinacialYear);

	public List<ProductDetails> loadCreatePOPage(Model model, HttpServletRequest request);

	public List<ProductDetails> getIndentsProductWise(String product, String subProduct, String childProduct);

	public List<IndentCreationDto> getPendingIndents(String user_id, String site_id);

	public String SaveEnquiryForm(Model model, HttpServletRequest request, HttpSession session);

	public boolean getVendorPasswordInDB(String uname,int indentNumber, String pass);

	public List<Map<String, Object>> getComparisionDtls(HttpServletRequest request,HttpSession session);
	public List<IndentCreationBean> getAllSitePurchaseIndents(String site);

	public List<ProductDetails> getTermsAndConditions(String site_id);
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String siteId,String tempPoNumber,String AllOrNot);
	public String getDetailsforPoApproval(String poNumber, String siteId,HttpServletRequest request);
	public String SavePoApproveDetails(String poNumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPo);
	public String RejectPoDetails(HttpSession session,HttpServletRequest request);
	public List<ProductDetails> getQuatationProductDetails(HttpServletRequest request,HttpSession session);
	public List<ProductDetails> getQuatationDetails(HttpServletRequest request);

	public String closeIndent(Model model, HttpServletRequest request, int site_id, String user_id,
			String siteId);

	public List<ProductDetails> getProductDetailsLists(String indentNo, String vendorName,HttpServletRequest request,String CheckVal); //check val : tempPO(cancel) or not (it is using in create PO and CancelPo

	public String getSiteIdByPONumber(String poNumber);
	public List<IndentCreationBean> ViewTempPo(String fromDate, String toDate,String tempPoNumber);

	public List<ProductDetails> combinedDetailsChildProductWise(List<ProductDetails> indentDetails);

	//public List<Map<String, Object>> getListOfActivePOs(String site_id);

	public String editAndSaveUpdatePO(Model model, HttpServletRequest request, HttpSession session);
	public List<ProductDetails> getProductDetailsListsForQuatation(String indentNo, String vendorName,HttpServletRequest request);

	public String getDefaultCCEmails(String site_id);

	public int getSiteWiseIndentNo(int indentNumber);
	public void printIndentForPurchase(Model model, HttpServletRequest request, int site_id, String user_id);
	public String CancelPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String siteId);
	public List<ProductDetails> getListOfCancelPo(String userId,String siteId);
	public List<ProductDetails> getViewCancelPoDetails(String poNumber, String reqSiteId);
	public List<ProductDetails> getProductDetailsListsForCancelPo(String poNumber,String reqSiteId);
	public List<ProductDetails> getTransChrgsDtlsForCancelPo(String poNumber,String reqSiteId);
	public String updateTempPoPage(Model model, HttpServletRequest request,HttpSession session);
	public String tempPoSubProducts(String prodId,String indentNumber,String reqSiteId);
	public String tempPoChildProducts(String subProdId,String indentNumber,String reqSiteId);
	public List<ProductDetails> getTempTermsAndConditions(String poNumber,String isRevised,String siteId);
	public String  getCancelPoComments(String poNumber);
	public String getTempPOSubject(String poNumber);
	public String getTempPoCCEmails(String poNumber);
	public String getNoOfRowsForUpdatePO(HttpServletRequest request,HttpSession session);
	public int insertSiteLevelIndentData(int site_Id,String user_Id,int indent_Number,String siteWiseIndent);
	public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentNumber,int site_id,String user_id);//for indent creation  at the time of site levcel po
	public String RejectMailTempPO(HttpSession session ,HttpServletRequest request);
	public String getTempPOPassword(String tempPONumber);
	public void sendTempPoMailCommonData(String temp_Po_Number,String mailComments,List<String> listOfDetails,String subject,String type,int intPortNo);
	//public Map<String, String> TempPoloadProds();
	public String checkRevisedOrNot(String poNumber);
	public List<ProductDetails> getViewPoDetailsDetails(HttpServletRequest request,String fromDate,String toDate);//to get mis reports
	public List<ProductDetails> getViewMarketPurchaseDetails(HttpServletRequest request,String fromDate,String toDate,String purchaseType);// to get market purchase details
	public List<ProductDetails> getVendorPaymentDetails(HttpServletRequest request,String fromDate,String toDate);// MIS Vendor Payment details
	public List<ProductDetails> getSiteLevelPoDetails(HttpServletRequest request,String fromDate,String toDate);// MIS SiteLevel Po Details
	public List<IndentCreationBean> ViewSiteTempPo(String fromDate, String toDate,String tempPoNumber,String siteId); // this is for siteWise data Temp pO
	public String getCancelPOPendinfEmpId(String pendingEmpId,String typeOfPo);
	public int inActivePOTable(String poNumber,String siteId);
	public boolean checkPoDoneInvoiceOrDcPayment(String poNumber,String siteId,String vendorId); //whether invoice or dc is Alreay initiate or not check here
	public int saveCancelPoDetailsInCancelTbl(String poentryId,String poNumber,String pendingEmpId,String status,String passwdForMail); // save cancel po information like status,pending emp id
	public int saveCancelPoApproveDetails(String poentryId,String siteId,String userEmpId,String status,String vendorComments,String normalComments,String ccmails); // for approving data saved in Approved table
	public int updateCancelPoDetailsInCancelTbl(String poentryId,String pendingEmpId,String status,String passwdForMail,boolean rejectOrNot); // boolean for same method use so here use boolean
	public void sendMailToVendorForCancelPO(HttpServletRequest request,String poentryId,String poNumber,String vendorId,String siteId,String poDate,String siteName,int portNumber,String ccmails);
	public String CancelPerminentPODetailsReject(HttpSession session,HttpServletRequest request,String poentryId,String isMail);
	public void sendMailPerminentPoCancel(HttpServletRequest request,String pendingEmpId,String poentryId,String poNumber,String vendorId,String siteId,String poDate,String siteName,String passwdForMail,String userId);
	public boolean checkAlreadyCancelOrnot(String poNumber);
	public String getPoQuantityIndentCreationDetailsForCancelPermanentPo(String indentNumber,String poentryId,String poNumber);
	public void sendPreviousApproveEmpMail(String poentryId,String poNumber,String poDate,String siteName,String user_id,String vendorId,String approvalEmpId);
	public boolean  updatePaymentDtls(HttpServletRequest request,String poNumber,String siteId,String vendorId); // this is for payment tables update payment intiate on po 
	public List<Map<String, Object>> getListOfCancelPoShow(String site_id);
	public List<Map<String, Object>> getListOfActivePOForCancelPermanentPOs(String site_id,String fromdate,String todate,String poNumber);
	public boolean checkPoPaymentDoneOrNot(String poNumber,String siteId,String vendorId,String poDate,String siteName,String poEntryId,int portNumber,boolean isUpdatePO,boolean isRevised);
	public List<IndentCreationBean> ViewandGetCancelPo(String fromDate, String toDate,String PoNumber);
	public List<String> getCancelPoData(String poNumber,String siteId);
	public boolean isThisPOGoingToBeCanceled(String strPoEntryId);
	public List<String> getTempPoConclusions(String poNumber);
	public String getVendorCCEmails(String site_id); //this is used to getting the vendor ccmailids 
public String SaveUpdatePoApproveDetails(final String tempPONumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPO);
public String getAndCheckPOBOQQuantity(HttpSession session,HttpServletRequest request,String siteId,boolean approveOrNot,boolean isCancelled);
public String getAndCheckApprovalPOBOQQuantity(HttpSession session,HttpServletRequest request);
public String gettingPOBoqQuantityAjax(String groupId,String siteId);
public List<Map<String, Object>> getListOfActivePOs(String site_id,String poNumber);
public boolean checkIsUpdateOrNot(HttpSession session,HttpServletRequest request,boolean isRevised,boolean isModified);
public boolean getQuatationDetailsAndCheck(String indentNumber,String vendorId,String siteId);
public boolean checkConditionForIncreaseDecreaseQuantity(HttpServletRequest request);
public List<IndentCreationBean> ViewPoPendingforApprovalForMarketingHead(String fromDate, String toDate, String strUserId,String tempPoNumber, String AllOrNot, String multiplePendingEmpForQuery);
}
