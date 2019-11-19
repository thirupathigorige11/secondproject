package com.sumadhura.transdao;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.PaymentDto;

public interface ContractorPaymentProcessDao {

	public List<PaymentBean> getAccDeptContractorPaymentPendingDetails(String strUserId);

	public String getApproverEmpIdInAccounts(String strUserId);
	public String getApproverDeptIdInAccounts(String strUserId);
	public int getAccCntTempPaymentTransactionSeqNo();
	public int insertCntTempPaymentTransactionsTbl(PaymentBean objPaymentBean, int intTempPaymentTransactionId);
	public int updateCntAccDeptPaymentProcsstbl(PaymentBean objPaymentBean);
	public int saveCntAccountApprovalDetails(PaymentBean objPaymentBean, String strUserId, int intTempPaymentTransactionId, String siteId);
	public int setInactiveRowInCntAccDeptPmtProcessTbl(int accDeptPmtProcessId);
	public int saveCntAccountRejectDetails(PaymentBean objPaymentBean, String strUserId, String siteId);
	public String[] getEmailsOfEmpByDeptId(String pendingDeptId);
	public String[] getEmailsOfEmpByEmpId(String requestReceiveFrom);
	public int setInactiveRowInAccCntPaymentTbl(int cntPaymentId);
	public int setInactiveRowInQsContractorBill(String billId, String qsWorkorderIssueId, String siteId);
	public List<PaymentBean> getCntAccDeptPaymentApprovalDetails(String site_id, String user_id);
	public List<PaymentModesBean> getPaymentModes();
	public int updateCntTempPaymentTransactionsTbl(PaymentBean objPaymentBean,String strPendingEmpId);
	public String getLowerEmpIdInAccounts(String strUserId);
	public int updateRowInCntAccTempPaymentTransactions(PaymentBean objPaymentBean);
	public int updateIntiateAmountInCntAccDeptPaymentProcesstbl(PaymentBean objPaymentBean);
	public int getAccCntPaymentTransactionSeqNo();
	public int insertCntPaymentTransactionsTbl(PaymentBean objPaymentBean, int intPaymentTransactionId);
	public int insertCntAdvanceCreditHistory(PaymentBean objPaymentBean);
	public int insertCntInvoiceHistory(PaymentBean objPaymentBean, int intPaymentTransactionId);
	public int insertCntSecDepositCreditHistory(PaymentBean objPaymentBean);
	public int insertOrUpdateAdvancePaidAmountInCntAdvanceDtlsTable(PaymentBean objPaymentBean);
	public int revertPendingApprovalToLowerEmployee(String strLowerEmpId, int intTempPaymentTransactionId,String comments);
	public int updateAllocatedAmountInCntAccDeptPaymentProcesstbl(PaymentBean objPaymentBean);
	public int inactivePaymentAfterCheck(PaymentBean objPaymentBean);
	public Map<String, String> getDeductionAmountDetails(String tempBillId);
	public int insertCntAdvanceDebitHistory(PaymentBean objPaymentBean, double advanceDeductionAmount);
	public int insertCntSecDepositDebitHistory(PaymentBean objPaymentBean, double securityDepositDeductionAmount);
	public int minusDeductionAmountFromAdvanceAmountInCntAdvanceDtlsTable(PaymentBean objPaymentBean,double advanceDeductionAmount);
	public int depositDeductionAmountUnderSecDepositInCntSecDepositTable(PaymentBean objPaymentBean,double securityDepositDeductionAmount);
	public int inactiveSecDepositInCntSecDepositTable(PaymentBean objPaymentBean);
	public List<PaymentBean> getViewContractorPaymentDetails(String fromDate, String toDate, String contractorId,String user_id, String dropdown_SiteId, String workOrderNo);
	public List<PaymentBean> viewMyContractorPayment(String fromDate, String toDate, String site_id, String user_id,String contractorId, String workOrderNo, String dropdown_SiteId);
	public List<PaymentBean> getContractorPaymentDetailsToUpdate(String site_id, String user_id, String fromDate,String toDate, String contractorId, String workOrderNo);
	public int updateRefNoInAccDeptContractorTransaction(String strRefNo, int intPaymentTransactionId,String paymentMode, String paymentDate);
	public int updateRefNoInContractorInvoiceHistory(String strRefNo, String paymentMode, int intPaymentTransactionId);
	public List<PaymentBean> downloadInvoicePaymentsToExcelFileForUpdate(String user_id,String fromDate, String toDate);
	public List<PaymentBean> downloadContractorPaymentsToExcelFileForUpdate(String user_id, String fromDate, String toDate);
	public int updateVendorPaymentRefNoFromExcel(String strRefNo, int intPaymentTransactionId,java.util.Date paymentDate);
	public int updateContractorPaymentRefNoFromExcel(String strRefNo, int intPaymentTransactionId, java.util.Date paymentDate);
	public int revertPendingEmployeeInAccCntPayment(int accDeptPmtProcessId, int cntPaymentId);
	
	
	
}
