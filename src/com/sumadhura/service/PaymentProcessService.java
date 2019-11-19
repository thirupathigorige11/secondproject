package com.sumadhura.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ContractorTaxReportBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PurchaseTaxReportBean;

public interface PaymentProcessService {

	public List<String> savePaymentIntiateDetails( HttpServletRequest request,HttpSession session);
	public List<PaymentBean> getInvoiceDetails(String site_id,Map<String,String> reqParamsMap, String selectAll, String ispaymentReqDateChecked, boolean isItFirstRequest);
	
	public List<String> savePaymentApprovalAndRejectDetails( HttpServletRequest request,HttpSession session,String strUserId);
	public List<PaymentBean> getPODetails(String site_id,Map<String,String> reqParamsMap, String selectAll);
	public List<PaymentBean> getAccDeptPaymentPendingDetails(String strUserId, HttpServletRequest request, ModelAndView model);
	
	public List<String> createAccountDeptTransaction(HttpServletRequest request , String steEmpId, String user_name, HttpSession session);
	public List<PaymentBean> updateAccountDeptTransaction(HttpServletRequest request, HttpServletResponse response, String strUserId, String user_name, HttpSession session);
	public String getPendingEmpId(String user_id, String site_id);
	public List<String> updateRefNoInAccDeptTransaction(HttpServletRequest request, String strUserId, HttpSession session);
	public List<String> updatePreviousPayments(HttpServletRequest request);
	public List<String> savePaymentIntiateDetailsOnPoAdvance(HttpServletRequest request, HttpSession session);
	public List<PaymentBean> getInvoiceDetailsForOldPayments(String siteId, Map<String, String> reqParamsMap);
	public List<PaymentBean> getContractorBillReqDetails(String user_Id,String site_id, Map<String,String> reqParamsMap);
	public List<String> saveContractorBillPaymentDetails( HttpServletRequest request,HttpSession session);
	public String[] getEmailsOfEmpByEmpIdAndEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(String input);
	public List<PaymentBean> getLocalPurchaseInvoiceDetails(String siteId, Map<String, String> reqParamsMap, String selectAll);
	public List<String> updateLocalPurchasePayments(HttpServletRequest request, HttpSession session);
	public List<PaymentBean> getAccDeptPaymentApprovalDetails(String site_id, String user_id, HttpServletRequest request, ModelAndView model);
	public List<PurchaseTaxReportBean> getPurchaseTaxReport(HttpServletRequest request, ModelAndView model);
	public void setExtraDetailsForExcel(HttpServletRequest request, HashMap<String, Object> extraDataMap);
	public List<ContractorTaxReportBean> getContractorTaxReport(HttpServletRequest request, ModelAndView model);
	public void setExtraDetailsForContractorTaxReportExcel(HttpServletRequest request, HashMap<String, Object> extraDataMap);
	
}
