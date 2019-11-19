package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.sumadhura.bean.DebitNoteBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.CreditNoteDto;
import com.sumadhura.dto.IndentReceiveDto;

public interface IndentReceiveDao {

	public Map<String, String> loadProds(String siteId); // siteId for which type of po we know it
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadIndentReceiveMeasurements(String childProdId);
	public Map<String, String> getGSTSlabs();
	public int getIndentEntrySequenceNumber();
	public int insertInvoiceData(int indentEntrySeqNum, IndentReceiveDto objIndentReceiveDto);
	public String getVendorInfo(String vendName);
	public int insertIndentReceiveData(int indentEntrySeqNum, int intIndentEntryDetailsSeqNo,IndentReceiveDto irdto, String userId, String siteId,int intPriceListSeqNo);	
	public int updateIndentAvalibility(IndentReceiveDto irdto, String siteId);	
	public void updateIndentAvalibilityWithNewIndent(IndentReceiveDto irdto, String siteId);
	public String getProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, String siteId);
	public void saveReciveDetailsIntoSumduraPriceList(IndentReceiveDto irdto, String invoiceNumber, String siteId, String id,int entryDetailssequenceId,int intPriceListSeqNo,String typeOfPurchase);
	public String getIndentAvailableId(IndentReceiveDto irdto, String site_id);
	public String getProductAvailabilitySequenceNumber();
	public int getEntryDetailsSequenceNumber();
	public String getindentEntrySerialNo(String receviedDate) throws Exception;
	public void saveReciveDetailsIntoSumadhuraCloseBalance(IndentReceiveDto irdto, String siteId);
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(IndentReceiveDto irdto, String siteId);
	public Map<String, String> getOtherCharges();
	public void saveTransactionDetails(String invoiceNum, String transactionId, String gstId, String gstAmount, String totAmtAfterGSTTax, String transactionInvoiceId, String transAmount, String siteId, String indentEntrySeqNum);
	public int getInvoiceCount(String  strInvoiceNmber, String vendorId,String strReceiveStartDate, String receiveDate);
	public int getIndentEntryDtails_SeqNumber();
	public int getInvoiceSaveCount(String  strInvoiceNmber, String vendorId,String receiveDate);
	public List<ViewIndentIssueDetailsBean> getSiteWiseInvoiceDetails(String fromDate, String toDate, String siteId,String indentType);
	public List<ProductDetails> getPODetails(String poNumber, String reqSiteId);
	public List<ProductDetails> getProductDetailsLists(String poNumber, String reqSiteId);
	public List<ProductDetails> getTransChrgsDtls(String poNumber, String reqSiteId);
	public int updateReceiveQuantityInIndentCreationDtls(String string, String string2);
	public int setPOInactive(String string, String reqSiteId);
	public int setIndentInactiveAfterChecking(String indentNumber);
	public List<Map<String, Object>> getListOfActivePOs(String site_id);
	public int getPriceList_SeqNumber();
	public int getCreditNoteSequenceNumber();
	public int getCreditNoteDetailsSequenceNumber();
	public int insertCreditNote(CreditNoteDto creditNoteDto);
	public int insertCreditNoteDetails(CreditNoteDto creditNoteDto,ProductDetails productDetails);
	public String loadSubProdsByPONumber(String prodId, String poNumber, String reqSiteId);
	public String loadChildProdsByPONumber(String subProductId, String poNumber, String reqSiteId);
	public String getPriceRatesByChildProduct(String childProdId, String poNumber, String reqSiteId);
	public int updateAllocatedQuantityInPurchaseDeptTable(String receiveQuantity,String indentCreationDetailsId,HttpServletRequest request);
	public int updateInvoiceNoInAccPaymentTbl(String invoiceNumber, String invoiceAmount, String invoiceDate,String receviedDate, String poNo,
			String vendorId,String creditNoteNumber,String creditTotalAmount,String indentEntryNo, String site_id);
	public int checkPOisActive(String poNumber);
	public int updateReceivedQuantityInPoEntryDetails(String totalQuantity, String poEntryDetailsId, HttpServletRequest request);
	public int getCheckIndentAvailable(String  strInvoiceNmber);
	public String getPoEntryDetailsandIndentCreationDetails(String poNumber,String childProdId,String indentNumber);
	public String getIndentCreationDetailsId(String indentNumber,String childProdId);
	public int getCheckPoAvailable(String poNumber,String vendorId);
	public List<Map<String, Object>> getListOfActiveMarketingPOs(String site_id,String status);
	public List<ProductDetails> getMarketingPODetails(String poNumber, String reqSiteId);
	public List<MarketingDeptBean> getPOProductLocationDetails(String poNumber);
	//public List<String> getInvoiceDetailsForShow(String invoiceNumber);
	public List<Map<String, Object>> loadInvoiceNumberByVendorId(String site_id, String vendorId);
	public int saveTaxInvoicesData(List<GetInvoiceDetailsBean> invoiceDetialsBean, String nextLevelApprovalEmpId);
	public int saveTaxInvoicesApprRejctDetails( List<GetInvoiceDetailsBean> invoiceDetialsBean,
			String nextLevelApprovalEmpId);
	public GetInvoiceDetailsBean getSubmitTaxInvoiceFromAndToDetails(String user_id, GetInvoiceDetailsBean bean);
	public List<Map<String, Object>> receiveTaxInvoicesDetails(GetInvoiceDetailsBean bean,String status,String condition);
	public int approveTaxInvoicesData(List<GetInvoiceDetailsBean> invoiceDetialsBean, String nextLevelApprovalEmpId);
	public int rejectTaxInvoicesData(List<GetInvoiceDetailsBean> invoiceDetialsBean, String nextLevelApprovalEmpId);
	public List<Map<String, Object>> loadAllTaxInvoicesDetails(
			GetInvoiceDetailsBean bean);
	public String getEmployeeDetails(String userId);
	public void setApprovalEmployeeDetails(List<String> listOfIndentEntryIds, List<GetInvoiceDetailsBean> invoiceDetialsBean);
	public List<DebitNoteBean> getCreditNoteDetails(String indentEntryId,String dcEntryId, DebitNoteBean totals);
	public boolean isIndentEntryIdHasCreditNote(String strIndentEntryId);
	DebitNoteBean getCreditNote(String indentEntryId, String dcEntryId);
	DebitNoteBean getVendorDetails(String vendorId);
	DebitNoteBean getInvoiceDetails(String indentEntryId);
	DebitNoteBean getDCDetails(String dcEntryId);
	DebitNoteBean getSiteDetailsFromVendorTable(String siteId);
	DebitNoteBean getSiteDetails(String siteId);
	List<DebitNoteBean> getPOEntryDetails(String poEntryId);
	public String getSiteAddressByUsingSiteId(String reqSiteId);
	
		public String gettingReqBoqQuantityAjax(String groupId,String siteId);

}
