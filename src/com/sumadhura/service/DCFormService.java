package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.DCFormBean;
import com.sumadhura.bean.DCFormViewBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.DCFormDto;

public interface DCFormService {
	public Map<String, String> loadProds(String siteId); // siteId for stote data or marketing data
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadDCFormMeasurements(String childProdId);
	public Map<String, String> getGSTSlabs();
	
	public int insertDCFormData(int dcformSeqNum, DCFormDto dcformdto) throws Exception;
	public String getVendorInfo(String vendName);	
	
	public Map<String, String> getOtherCharges(); 
	public String dcFormProcess(Model model, HttpServletRequest request, HttpSession session);
	public int updateDCFormAvalibility(DCFormDto dcformdto, String site_id);
	public void updateDCFormWithNewDCForm(DCFormDto dcformdto, String siteId);
	public List<DCFormViewBean> getDcFormLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId);
	public List<DCFormViewBean> getDcFormProductDetailsLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId);
	public List<DCFormViewBean> getDcFormTransportChargesList(String dCNumber, String siteId);
	public String doIndentGetDcFormDetails(Model model, HttpServletRequest request, HttpSession session);
	public int getDcCount(String strInvoicNo, String strVendorName, String strReceiveDate);
	public int getDCSaveCountForVerification(Model model, HttpServletRequest request,HttpSession session);
	public String dcFormUpdate(Model model, HttpServletRequest request, HttpSession session) throws Exception;
	public String getIndentIdNo(String strVendorId,String DCNumber,String strSiteId);
	public int isDCPresentAndInactive(String strVendorId,String strSiteId,String strDCNumber,String dcDate);
	public String covertDcWithoutPricing(Model model, HttpServletRequest request, HttpSession session);
	public int checkDCInactive(String strDCNO);
	
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId);
	public String getDcGrnDetails(String invoiceNumber, String siteId,HttpServletRequest request);
	public Map<String, String> loadProdsByPONumber(String poNumber, String reqSiteId);
	public String processingPOasDC(Model model, HttpServletRequest request, HttpSession session);
	public Map<String, String> TempPoloadProds(String indentNumber,String reqSiteId);
	List<Map<String, Object>> getVendorInfoByID(String vendorId);
	public String checkDcNumberExisted(String dcNumber,String vendorId,String DcDate,String siteId);
	public String getTransportorId(String transportorName);
	public String getTransportorData(String transportorName);
}
