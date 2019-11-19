package com.sumadhura.transdao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.TransportChargesDto;

public interface PurchaseDepartmentIndentProcessDao {



	public List<IndentCreationDto> getPendingIndents(String pendingEmpId,String strSiteId);

	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber);

	public List<Map<String, Object>> getVendorOrSiteAddress(String SiteId);

	public int insertTempPOEntryDetails(ProductDetails productDetails,String poEntrySeqNo);

	public int updatePurchaseDeptIndentProcesstbl(double intQuantity,int purchaseDeptSeqId,String strStatus);

	public int getPoEnterSeqNo();

	public int getPoEnterSeqNoOrMaxId(String poState);

	public int getTempPoEnterSeqNoOrMaxId();

	public int insertTempPOEntry(ProductDetails productDetails,String tempuser,String ccEmailId,String subject);

	public List<IndentCreationBean> getupdatePurchaseDeptIndentProcess(int indentNumber,String intEmployeeId,String siteId,String strApproverEmpId,String sessionSiteId);

	public List<Map<String, Object>> getAllProducts(String purchaseDeptId);

	public List<ProductDetails> getIndentsProductWise(String product, String subProduct, String childProduct);
	public int insertPurchaseIndentProcess(int purchaseIndentProcessId, IndentCreationBean purchaseIndentDetails,
			int IndentCreationDetailsId, int indentReqSiteId, String reqReceiveFrom);


	public int insertVendorEnquiryForm(ProductDetails productDetails);

	public int insertVendorEnquiryFormDetails(IndentCreationBean indentCreationBean);

	public int getEnquiryFormSeqNo();

	public Map<String, String> getVendorEmail(String vendor_Id);

	public String getIndentCreationDate(int intIndentNumber);

	public int setVendorPasswordInDB(String vendor_Pass, String vendor_Id);

	public String getVendorPasswordInDB(String vendor_Id);

	public List<Map<String, Object>> getComparisionDetails(String indentNumber, String strProductDtls,String vendorId);

	public double getIntiatedQuantityInPurchaseTable(String purchaseDepartmentIndentProcessSeqId);

	public int getPoTransChrgsEntrySeqNo();

	public int insertPOTempTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails,
			TransportChargesDto transportChargesDto);

	/*public List<ProductDetails> getTermasAndconditions();*/

	public int getEnquiryFormDetailsId();

	public int updateEnquiryFormDetails(ProductDetails productDetails);
	public String  getTemproryuser(String strUserId);
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String siteId,String tempPoNumber,String AllOrNot);
	public int saveTempTermsconditions(String termsAndCondition,String strPONumber,String strVendorId,String strIndentNo);
	public String getPendingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName) throws ParseException;
	public String getPendingProductDetails(String poNumber, String siteId,HttpServletRequest request,String deliverySiteState);
	public String getPendingTransportChargesList(String poNumber,String strSiteId,String gstinumber,HttpServletRequest request,String deliverySiteState);
	public String getAndsaveVendorDetails(String poNumber, String siteId,String userId,HttpServletRequest request,String revision_No,String old_Po_Number,String siteLevelPoPreparedBy,String siteName, String deliveryDate);
	public String getAndsavePendingProductDetails(String poNumber, String siteId,HttpServletRequest request,String permponumber,int intPOEntrySeqId,String old_Po_Number);// revised po to get received quantity from inwards from po doing
	public String getAndsavePendingTransportChargesList(String poNumber,String strSiteId,HttpServletRequest request,int poEntryId);
	public int updatepoEntrydetails(String poNumber,String indentnumber,String strSiteId,String strUserId,String sessionSite_id,String siteLevelPoPreparedBy,String password, String deliveryDate);  //password for mail purpose
	public String gettermsconditions(String poNumber,String permPoNumber);
	public int insertPOEntry(ProductDetails productDetails);
	public int saveTermsconditions(String termsAndCondition,int strPONumber,String strVendorId,String strIndentNo);
	public int insertPOTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails, TransportChargesDto transportChargesDto);
	public int insertPOEntryDetails(ProductDetails productDetails,int poEntrySeqNo);
	public int updatePurchaseDeptIndentProcestbl(String indentNumber,String poNumber, String quantity,String indentCreationdtlsId,boolean typePo);
	public int saveVendorTermsconditions(String termsAndCondition,String strVendorId,String strIndentNo);
	public List<String> getVendortermsconditions(String indentNo,String vendorId);
	public String getVendorDetails(String poNumber, String siteId, String userId, HttpServletRequest request);
	public int doInactiveInIndentCreation(String indentNumber);

	public int getIndentCreationApprovalSequenceNumber();

	public int insertIndentCreationApprovalDtls(int indentCreationApprovalSeqNum, String indentNumber,
			IndentCreationDto indentCreationDto);

	public int doInactiveInPurchaseTable(String indentNumber,String typeOfPurchase,String Quantity);


	public List<ProductDetails> getProductDetailsLists(String indentNo, String vendorName,List<ProductDetails> listProductDetails,HttpServletRequest request);
	public Map<String, String> getApproveCreateEmp(int indentnumber,HttpServletRequest request);
	public void getVerifiedEmpNames(int indentnumber,HttpServletRequest request,String siteId);
	public List<String> getAllEmployeeEmailsUnderDepartment(String deptId);

	public int getSiteIdByPONumber(String poNumber);
	public int updateTempPoEntry(String approvalEmpId,String poNumber,String ccmailId,String siteLevelPoPreparedBy,String indentnumber,String passwdForMail, String deliveryDate);

	public int insertTempPOorPOCreateApproverDtls(String strTempPoNumber,String poNumber,String strEmpId,String strSIteId,String strOperationType,String remaks);

	public List<IndentCreationBean> ViewTempPo(String fromDate, String toDate,String tempPoNumber);

	//public List<Map<String, Object>> getListOfActivePOs(String siteId);

	public int updatePOEntryDetails(ProductDetails productDetails);

	public int updatePOTransportChargesDetails(TransportChargesDto transportChargesDto, int id);

	public int getPoEnterSeqNoByPONumber(String poNumber, String toSite);

	public int deletePOEntryDetails(String poEntryDetailsId);

	public int updatePOIntiatedQuantityInPDTable(String indentCreationDetailsId, String quantity);

	public int deletePOTransportChargesDetails(int poTransChrgsDtlsSeqNo);

	public List<IndentCreationBean> getDeletedProductDetailsLists(int indentNumber);

	public String getStateNameForTermsAndConditions(String site_id);
	public int getPoInfinityNumberGenerator(String serviceState);
	public int getPoYearWiseNumberGenerator(String serviceState);
	public int getUpdatePoNumberGeneratorHeadOfficeWise(int infinityNumber,int yearWiseNumber,String serviceName);
	
	public String getSiteWisePoNumber(String siteWise_Number);
	public String getStateWiseYearPoNumber(String siteWise_Number);
	public int getUpdateStateWisePoNumber(int siteWise_Number,int stateYearWisePoNum,String strSiteId);
	public int getStateWisePoNumber(String strSiteId);
	public int getStateWiseYearPo_Number(String SiteId);
	public int getHeadOfficeInfinitMaxId(String poState);
	public String getSiteAddress(String siteId);
	public int getSiteWiseIndentNo(int indentNumber);
	public int updateTablesOnTempPORejection(String indentNumber,String ponumber,String indentCreationdtlsId);
	public String getPoCreatedEmpName(String userId);
	public String getPoCreatedEmpId(String tempPoNumber);
	public int productWiseInactiveInPurchaseTable(String indentNumber,String typeOfPurchase,String Quantity);
	
	public List<IndentCreationBean> purchasePrintIndent(int indentNumber);
	public int insertPurchaseDepttbl(int purchaseIndentProcessId,String sessionSiteId,String strUserId,ProductDetails productDetails);

	public int insertIndentCreationtbl(ProductDetails productDetails,String indentCreationId,String isSiteLevelPo);
	public String  getpendingEmpId(String poNumber,String userId);
	public int updateEmpId(String pendingEmpId,String temp_Po_Number);
	public List<ProductDetails> getListOfCancelPo(String userId,String siteId);
	public List<ProductDetails> getViewCancelPoDetails(String poNumber, String reqSiteId) ;
	public List<ProductDetails> getProductDetailsListsForCancelPo(String poNumber,String reqSiteId);
	public List<ProductDetails> getTransChrgsDtlsForCancelPo(String poNumber,String reqSiteId);
	public int getPoEnterSeqNoByTempPONumber(String poNumber,String toSite);
	public int updateTempPOEntryDetails(ProductDetails productDetails);
	public int insertTempPOTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails, TransportChargesDto transportChargesDto,String poNumber);
	public int updateTempPOTransportChargesDetails(TransportChargesDto transportChargesDto,int id);
	public int deleteTempPOTransportChargesDetails(int poTransChrgsDtlsSeqNo);
	public int deleteTempPOEntryDetails(String poEntryDetailsId);
	public int updateTempPOQuantityDetails(String indentCreationDetailsId, String quantity,String strQuantity);
	
	public int updateTempPOVendorDetails(String vendorId,String poNumber,String ccEmailId,String subject,String isUpdate, String strDeliveryDate,String payment_Req_Days);
	public String  getpendingUserId(String temp_Po_Number);
	public int updateTempPoVieworCancel(String temp_Po_Number,String siteId);
	public String tempPoSubProducts(String prodId, String indentNumber, String reqSiteId);
	public String tempPoChildProducts(String subProdId, String indentNumber, String reqSiteId);
	public int insertTempPOEntryDetails(ProductDetails productDetails,int poEntrySeqNo);
	public String  getPoInitiateQuan(String indentCreationDetailsId);
	public List<ProductDetails> getTempTermsAndConditions(String poNumber,String isRevised,String siteId);
	public int deleteTemppoTermsAdnConditions(String poNumber,String isUpadted);
	public String  getCancelPoComments(String  poNumber);
	public String getTempPOSubject(String poNumber);
	public String getTempPoCCEmails(String poNumber);
	public List<String> getRevisionNumber(String editPoNumber);
	public String inactiveOldPo(String old_Po_Number,String isApproveOrNot);
	public double getRequestedQuantityInPurchaseTable(String indentCreationDetailsId);
	//public int updatePurchasetblForRevision(String indentCreationDetailsId,String poIntiatedQuantity,String status);
	public int checkIOndentCreationtbl(String indentNumber);
	public String getSiteLevelPoNumber(String site_Id);
	public int insertSiteLevelIndentData(int site_Id,String user_Id,int indent_Number,String siteWiseIndent);
	public int updateSiteLeveltbl(String site_Id,int total_Records,int current_Records);
	public int updateAccPayment(String old_Po_Number,String new_Po_Number,String totalAmt,boolean isUpdated);
	public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentNumber,int site_id,String user_id);
	public List<String> getApproveMailDetails(String tempPoNumber,String userId);
	public int getDataForMailForTempPo(String poNumber, String siteId);
	public String checkApproveStatus(String poNumber);
	public int updateTotalAmt(String  grandtotal,String approvalEmpId,String strPONumber);
	public String getTempPOPassword(String tempPoNumber,boolean isPerminentCancelPo);
	public String getTempPoTotalAmt(String strUserId,int temppoentryId);
	public String getCancelOrNot(String tempPoNumber);
	public String checkRevisedOrNot(String poNumber);
	public String getOldPoQuantitytoRevised(String poEntryId,String indentCreationDetailsId); //this is for revised po to get received quantity from inwards from po 
	public String activeOldPOTable(String poNumber);//revised po is rejected then old po active 
	public List<String>  getAccDeptEmails(String deptId);// revised po time mails send to acc dept 
public Map<String, Object> getAccPaymentDetailsByPoNo(String poNo, String siteId, String vendorId);
	public List<Integer> getInitiatedPaymentDtlsIdsByPaymentId(int paymentId);
	public List<Integer> getPaymentCompletedPaymentDtlsIdsByPaymentId(int paymentId);
	public String getRequestedAmount(Integer paymentDetailsId);
	public boolean isAccTempPaymentTransactionTblHasPaymentDetailsId(int paymentDetailsId);
	public int inactiveRowInAccTempPaymentTransactions(Integer paymentDetailsId);
	public int inactiveRowInAccDeptPmtProcessTbl(Integer paymentDetailsId);
	public int inactiveRowInAccPaymentDtls(Integer paymentDetailsId);
	public int updateReqUptoInAccPayment(double reqAmount, int paymentId);
	public boolean isAccDeptPmtProcessTblHasPaymentDetailsId(Integer paymentDetailsId);
	public boolean isAccPaymentDtlsHasPaymentDetailsId(Integer paymentDetailsId);
	public List<String> getSiteLevelPoAmountInitiateEmail(int paymentDetailsId);
	public int updateIndentAndDcPONumber(String old_Po_Number,String new_Po_Number,String totalAmt);
	public String getPermenentPoNumber(String poState,String type,String siteId,String financialyear);//to get po_Number for separe methode
	public List<String> getBillingAddGstin(String receiverState);//to get billing address gstin number state
	public List<ProductDetails> getViewPoDetailsDetails(HttpServletRequest request,String fromDate,String toDate);//to get mis reports
	public List<ProductDetails> getViewMarketPurchaseDetails(HttpServletRequest request,String fromDate,String toDate,String purchaseType);// to get market purchase details
	public List<ProductDetails> getVendorPaymentDetails(HttpServletRequest request,String fromDate,String toDate);// MIS Vendor Payment details
	public List<ProductDetails> getSiteLevelPoDetails(HttpServletRequest request,String fromDate,String toDate);// MIS SiteLevel Po Details
	public String getPoLevelComments(String poNumber,String FinalPoOrNot);
	public List<IndentCreationBean> ViewSiteTempPo(String fromDate, String toDate,String tempPoNumber,String siteId);
	public String getCancelPOPendinfEmpId(String pendingEmpId,String typeOfPo);
	public int inActivePOTable(String poNumber,String siteId);
	
	public boolean checkPoDoneInvoiceOrDcPayment(String poNo, String siteId, String vendorId);
	public int saveCancelPoDetailsInCancelTbl(String poentryId,String poNumber,String pendingEmpId,String status,String passwdForMail); // save cancel po information like status,pending emp id
	public int saveCancelPoApproveDetails(String poentryId,String siteId,String userEmpId,String status,String vendorComments,String normalComments,String ccmails); // for approving data saved in Approved table
	public int updateCancelPoDetailsInCancelTbl(String poentryId,String pendingEmpId,String status,String passwdForMail,boolean RejectOrNot); // boolean for same method use so here use boolean
	public String  getCancelPerminentPoComments(String poentryId);
	public String[] getCancelPerminentPoEmails(String poentryId);
	public boolean checkAlreadyCancelOrnot(String poNumber);
	public String getPoQuantityIndentCreationDetailsForCancelPermanentPo(String indentNumber,String poentryId,String poNumber);
	public String  getCancelPerminentPoInternalComments(String poentryId); // to get the every emp comments
	public Map<String, String> getVendorNameEmail(String vendor_Id);
	public String  previousEmpId(String previousEmpId);
	public boolean  updatePaymentDtls(HttpServletRequest request,String poNumber,String siteId,String vendorId); // this is for payment tables update
	public List<Map<String, Object>> getListOfCancelPoShow(String site_id);
	public List<Map<String, Object>> getListOfActivePOForCancelPermanentPOs(String site_id,String fromdate,String todate,String poNumber);
	public boolean checkPoPaymentDoneOrNot(String poNo, String siteId, String vendorId,String poDate,String siteName,String poEntryId,int portNumber,boolean isUpdatePO,boolean isRevised); // to check the payment done or not
	public String getCreatedEmpIdandMail(String poEntryId); // when the mail send to the vendor and purchase or accounts it will taken the below name emailids and mobile numners purpose
	public List<IndentCreationBean> ViewandGetCancelPo(String fromDate, String toDate,String PoNumber); // TO SEE PERMANENT PO PURPOSE WRITTEN IT
	public List<String> getCancelPoData(String poNumber,String siteId);// this is for vendor  open previous mail then it will not open to vendor showing to vendor
public String  getCCmails(String poEntryId,String siteId,boolean isCancel); // THIS IS FOR MAILS SAVED AND GET THAT PURPOSE WRITTEN IT
	public String  getCancelPoApprovalCCmails(String poentryId); // ccmails purpose written this one
	public boolean isThisPOGoingToBeCanceled(String poEntryId);
	public List<Map<String, Object>> getPricesOfTempPoProducts(String poNumber);
	public List<Map<String, Object>> getPricesOfPermanentPoProducts(String poNumber);
	public int[] insertTempPoConclusions(List<String> conclusions, String tempPoEntryId, String indentCreationId);
	public int[] insertPoConclusions(List<String> conclusions, String poEntryId, String tempPoEntryId, String indentCreationId);
	public int deleteTempPoConclusions(String poNumber);
	public List<String> getTempPoConclusions(String tempPoEntryId);
	public List<String> getPoConclusions(String poEntryId);
	public List<String> getPoConclusionsByPoNumber(String poNumber);
		public String getStateforSendEnquiry(String site_id);
	public String  getUserMailIds(String userId);// which is used to getting the current employee maild id
	public String gettingEmpId(String poEntryId); // getting the mails for who are doing previous emp ids
	public List<ProductDetails> gettingProductsForUpdatePO(String indentnumber,String oldPOEntryId,String tempPONumber);
	public String getAndsaveVendorUPdatePoDetails(String tempPONumber, String siteId,String user_id,HttpServletRequest request,String revision_No,
			String oldPoNumber,String siteLevelPoPreparedBy,String siteName,String deliveryDate,String oldPODate);
	public String inactiveUpdatePO(String oldPONumber);
	public double  updateAdvancePaidAmount(HttpServletRequest request,String poNumber,String siteId,String vendorId,double totalAmount);
	public List<Map<String, Object>> getListOfActivePOs(String site_id,String PONumber);
	public boolean getQuatationDetailsAndCheck(String indentNumber,String vendorId,String siteId);
	public boolean checkVendorQuantity(String vendorId,String indentCreationDetailsId);
	public String checkApprovePendingEmp(String poNumber,boolean isPeminent);
	public Map<String,String> getChildProductsForCompare(String indentNumber);
	public int deleteVndTermsAdnConditions(String indentNumber,String vendorId);
	public boolean checkSameEmpOrNot(String user_id,String tempPoNumber);
	public double getPOPendingQuantity(String childProductList,String siteId,String measurementList);
	public List<Map<String, Object>> getAndCheckApprovalPOBOQQuantity(String poNumber,String siteId);
	public List<IndentCreationBean> ViewPoPendingforApprovalForMarketingHead(String fromDate, String toDate, String strUserId,String tempPoNumber, String AllorNot, String multiplePendingEmpForQuery);
	public boolean checkSameEmpOrNotForMarketingHead(String user_id, String tempPoNumber, List<String> multiplePendingEmpList);
	
}
