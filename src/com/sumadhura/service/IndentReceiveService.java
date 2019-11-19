package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentReceiveDto;

public interface IndentReceiveService {

	public Map<String, String> loadProds(String siteId); // siteid for which type po Store or marketingPo
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadIndentReceiveMeasurements(String childProdId);
	public Map<String, String> getGSTSlabs();
	public int getIndentEntrySequenceNumber();
	public int insertInvoiceData(int indentEntrySeqNum, IndentReceiveDto objIndentReceiveDto);
	public String getVendorInfo(String vendName);	
	//public int insertIndentReceiveData(int indentEntrySeqNum, IndentReceiveDto irdto);
	public int updateIndentAvalibility(IndentReceiveDto irdto, String siteId);
	public void updateIndentAvalibilityWithNewIndent(IndentReceiveDto irdto, String siteId);	
	public String indentProcess(Model model, HttpServletRequest request, HttpSession session);
	public void saveReciveDetailsIntoSumadhuraCloseBalance(IndentReceiveDto irdto, String siteId);
	public String loadProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, HttpServletRequest request, HttpSession session);
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(IndentReceiveDto irdto, String siteId);
	public Map<String, String> getOtherCharges(); 
	public void saveTransactionDetails(String invoiceNum, String transactionId,String gstId,String gstAmount,String totAmtAfterGSTTax,String transactionInvoiceId,String transAmount, String siteId, String indentEntrySeqNum) ;
	public int getInvoiceCount(String  strInvoiceNmber, String vendorId, String receiveDate);
	public int isInvoicesaved(  HttpServletRequest request );
	public List<ProductDetails> getPODetails(String poNumber, String reqSiteId);
	public List<ProductDetails> getProductDetailsLists(String poNumber, String reqSiteId);
	public List<ProductDetails> getTransChrgsDtls(String poNumber, String reqSiteId);
	public String processingPOasInvoice(Model model, HttpServletRequest request, HttpSession session);
	List<Map<String, Object>> getListOfActivePOs(String site_id);
	public String loadSubProdsByPONumber(String prodId, String poNumber, String reqSiteId);
	public String loadChildProdsByPONumber(String subProductId, String poNumber, String reqSiteId);
	public String getPriceRatesByChildProduct(String childProdId, String poNumber, String reqSiteId);
	public int checkPOisActive(String poNumber);
	public int getCheckIndentAvailable(String  indentNumber);
	public int getCheckPoAvailable(String poNumber,String vendorId);
	public List<Map<String, Object>> getListOfActiveMarketingPOs(String site_id,String status);
	public List<ProductDetails> getMarketingPODetails(String poNumber, String reqSiteId);
	public List<MarketingDeptBean> getPOProductLocationDetails(String poNumber);
	public List<Map<String, Object>> loadInvoiceNumberByVendorId(String site_id, String vendorId);
	public String saveTaxInvoicesDetails(HttpServletRequest request, HttpSession session, RedirectAttributes redir);
	public GetInvoiceDetailsBean getSubmitTaxInvoiceFromAndToDetails(String user_id, GetInvoiceDetailsBean bean);
	public List<Map<String, Object>> receiveTaxInvoicesDetails(GetInvoiceDetailsBean bean,String status,String condition);
	public String approveTaxInvoicesDetails(HttpServletRequest request, HttpSession session, RedirectAttributes redir);
	public String rejectTaxInvoicesDetails(HttpServletRequest request, HttpSession session);
	public List<Map<String, Object>> loadAllTaxInvoicesDetails(
			GetInvoiceDetailsBean bean);
	public void getCreditNoteDetails(HttpServletRequest request, Model model);
	public void doCreditNote(Map<String, String> map, HttpServletRequest request, String credit_for, String INVorDC);
	public boolean isIndentEntryIdHasCreditNote(String strIndentEntryId);
	public String getAndCheckReceiveBOQQuantity(HttpSession session,HttpServletRequest request,String siteId); // this is for boq quantity check purpose
}
