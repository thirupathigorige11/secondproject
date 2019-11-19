package com.sumadhura.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;


public interface ContractorPaymentProcessService {

	public List<PaymentBean> getAccDeptContractorPaymentPendingDetails(String strUserId);
	public List<String> createAccDeptContractorPaymentTransaction(HttpServletRequest request, String strUserId,String user_name);
	public List<PaymentBean> getCntAccDeptPaymentApprovalDetails(String site_id, String user_id);
	public List<PaymentModesBean> getPaymentModes();
	public List<PaymentBean> updateAccountDeptTransaction(HttpServletRequest request, HttpServletResponse response,String strUserId, String user_name);
	public List<PaymentBean> getViewContractorPaymentDetails(String fromDate, String toDate, String contractorId,String user_id, String dropdown_SiteId, String workOrderNo);
	public List<PaymentBean> viewMyContractorPayment(String fromDate, String toDate, String site_id, String user_id,String contractorId, String workOrderNo, String dropdown_SiteId);
	public List<PaymentBean> getContractorPaymentDetailsToUpdate(String site_id, String user_id, String fromDate,String toDate, String contractorId, String workOrderNo);
	public String updateRefNoInAccDeptContractorTransaction(HttpServletRequest request, String strUserId);
	public List<PaymentBean> downloadPaymentsToExcelFileForUpdate(String user_id, String fromDate, String toDate,String contractorId, String workOrderNo, String dropdown_SiteId);
	public void writeDataToExcelSheet(XSSFWorkbook workbook, List<PaymentBean> listofPendingPayments);
	public void writeExcelDataToResponse(HttpServletResponse response, File file);
	public String generateFileName(String user_id);
	public String updatePaymentRefNoFromExcel(MultipartFile multipartFile, List<PaymentBean> successPayments, List<PaymentBean> failedPayments, List<String> errMsg) throws IOException;
	
	
	
}
