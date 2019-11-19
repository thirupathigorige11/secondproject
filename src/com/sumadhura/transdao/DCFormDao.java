package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sumadhura.bean.DCFormBean;
import com.sumadhura.bean.DCFormViewBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.DCFormDto;
import com.sumadhura.dto.DCToInvoiceDto;
import com.sumadhura.dto.IndentReceiveDto;
public interface DCFormDao {
	public Map<String, String> loadProds(String siteId); // siteId for store Or marketing po purpose
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadDCFormMeasurements(String childProdId);
	public Map<String, String> getGSTSlabs();
	public int insertDCFormData(int dcformSeqNum, DCFormDto dcformdto) throws Exception;
	public String getVendorInfo(String vendName);	
	public int insertDCFormReceiveData(int dcformSeqNum, int intDCFormSeqN,DCFormDto dcformdto,String userId, String siteId,int intSumadhurPriceList);
	public Map<String, String> getOtherCharges();
	public void saveTransactionDetails(String dcNO, String transactionId, String gstId, String gstAmount, String totAmtAfterGSTTax, String transactionInvoiceId, String transAmount, String siteId, String dcformSeqNum);
	public int getIndentEntrySequenceNumber();
	public int getDCFormSequenceNumber();
	public int updateIndentAvalibility(DCFormDto dcformdto, String site_id);
	public void updateIndentAvailabulityWithNewDCForm(DCFormDto dcformdto, String siteId);
	public String getIndentAvailableId(DCFormDto irdto, String site_id);
	public void saveReciveDetailsIntoSumduraPriceList(DCFormDto dcformdto, String dcNumber, String siteId, String id,int dcformSeqNum,int intPriceListSeqNo,String typeOfPurchase);
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(DCFormDto dcformdto, String siteId) throws Exception;
	public String getProductAvailabilitySequenceNumber();
	public List<DCFormViewBean> getDcFormDetails(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId);
	public List<DCFormViewBean> getGetProductDetailsLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId);
	public List<DCFormViewBean> getTransportChargesList(String dCNumber, String siteId);
	public int  getInvoiceCount(String  strDCNumber, String vendorId,String strReceiveStartDate, String receiveDate);
	public int getDCCount(String dcNumber, String vendorId,String receiveDate);
	public int getSavedDCCountForVerification(String  dcNumber, String vendorId, String receiveDate);
	public int updateDCFormDetails(String dcNumber);
	public String getDcEntryIdNo(String strVendorId,String dCNumber,String strSiteId,String dcDate);
	public int doDcInactive(int dc_entry_id);
	public int updateDCFormData(String dcNumber, DCToInvoiceDto dctoinvoicedto) throws Exception;
	public int updateDCPriceList(String dcNumber, DCToInvoiceDto irdto, int intIndentEntryDetailsSeqNo,String invoiceNum);
	/*public int updateTransactionDetails(String dcNO, String transactionId, String gstId, String gstAmount,
			String totAmtAfterGSTTax, String transactionInvoiceId, String transAmount, String siteId,
			String dcformSeqNum);*/
	
	public int updatePriceInIndentEntryDetails(String price, String quantity, String dcNumber);
	int updateSumadhuraClosingBalance(String price, String quantity, String invoiceNum,String dcNumber, String siteid,DCToInvoiceDto DCToInvoicedto);
	
	public int insertIndentReceiveData(int indentEntrySeqNum,int intIndentEntryDetailsSeqNo, DCToInvoiceDto DFDto, String userId, String siteId);
	public DCToInvoiceDto  getPriceDetails(String dcNumber, DCToInvoiceDto DCFormDto, int intIndentEntryDetailsSeqNo,String invoiceNum) ;


	
	public int insertIndentEntryDetailsWithoutPricing(int indent_entry_id, int intIndentEntryDetailsSeqNo,
			DCToInvoiceDto irdto, String userId, String siteId);
	public List<DCToInvoiceDto>  getDetailsInPriceTableByDcnumber(String dcNumber,String strSiteId,String vendorId,int dc_entry_id);
	public int updatePriceListWithoutPricing(String dcNumber,String invoiceNumber, int intIndentEntryDetailsSeqNo,
			DCToInvoiceDto dCToInvoicedto, String userId, String site_id);
	public int updateSumadhuraClosingBalByProduct(String dcNumber, String invoiceNumber,
			DCToInvoiceDto dCToInvoicedto, String userId, String site_id);
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId);
	public String getDcVendorDetails(String dcnumber, String siteId,String strVendorId,String dcDate,String dcEntryId);
	public String getDcProductDetails(String dcnumber, String siteId,String strVendorId,HttpServletRequest request,String dcDate,String dcEntryId);
	public String getDcTransportChargesListForGRN(String dcnumber,String strSiteId,String state,String vendorId,String dcDate,String dcEntryId);
	public int updateInvoiceNumber(String dcNumber, String invoiceNumber, String dcDate, String grn_no,String strSiteId,String strVendorId)  ;
	public int isDCPresentAndInactive(String strVendorId,String strSiteId,String strDCNumber,String dcDate);
	public int updateInvoiceNumberTrans(String invoiceNumber,int indent_entry_id,int dc_entry_id);
	public Map<String, String> loadProdsByPONumber(String poNumber, String reqSiteId);
	public int getDCEntrySequenceNumber();
	public int insertIndentEntry(String dcNumber, String invoiceNumber, String dcDate, String grn_no, String site_id,
			String vendorId, int indent_entry_id, DCFormDto db_DCEntry_Record,String Note,String payment_Req_Date);//note given for customer requirement -->Request date for customer requirement
	public int updateDcEntry(int indent_entry_id, int dc_entry_id, String invoiceNumber, String invoiceDate);
	public String getIndentIdNo(String strVendorId, String dCNumber, String strSiteId);
	public int getIndentIdByInvoiceNo(String vendorId, String invoiceNumber, String site_id, String invoiceDate);
	public Map<String, String> TempPoloadProds(String indentNumber,String reqSiteId) ;
	public DCFormDto getDB_DCEntry_Record(int dc_entry_id);
	public int updateIndentEntry(int indent_entry_id, DCFormDto db_DCEntry_Record);
	public int updateIndentEntryInCreditNote(String invoiceNumber, int indent_entry_id, int dc_entry_id);
	public String checkAllPosameOrNot(String strVendorId,String strSiteId,String strDCNumber,String dcDate);// this is used to check the all dc are po or not
	public String getPaymentRequestDays(String poNumber); // this is for getting the no of days material received 
	public List<Map<String, Object>> getVendorInfoByID(String vendorId);
	public boolean checkPaymentAndTaxInvoice(String invoiceNumber,String invoiceDate,String vendorName,String siteId);
	public boolean checkInvoiceDateAndNumber(String strInvoiceNumber,String strinvoiceDate,String vendorName,String site_id);
	public String checkDcNumberExisted(String dcNumber,String vendorId,String DcDate,String siteId);
	public String getTransportorId(String transportorName);
	public String getTransportorData(String transportorName);
}
