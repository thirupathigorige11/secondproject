package com.sumadhura.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.transdao.ContractorPaymentProcessDao;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;


@Service
public class ContractorPaymentProcessServiceImpl extends UIProperties implements ContractorPaymentProcessService{
	
	@Autowired
	ContractorPaymentProcessDao objContractorPaymentDao;

	
	@Override
	public List<PaymentBean> getAccDeptContractorPaymentPendingDetails(String strUserId) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		//String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
		list = objContractorPaymentDao.getAccDeptContractorPaymentPendingDetails(strUserId);
		return list;
	}


	@Override
	public List<String> createAccDeptContractorPaymentTransaction(HttpServletRequest request, String strUserId,String user_name) {
		//this list is for to send payments pending for Accounts 2nd level.
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		//this map is for to send rejected payments in Accounts 1st level To All site level Employees.
		final Map<String,List<PaymentBean>> PaymentRejectMapToMail = new HashMap<String,List<PaymentBean>>();
		//this map is for to send status of Approved payments in Accounts 1st level To each requestRecieveFrom. 
		final Map<String,List<PaymentBean>> SendApproveStatusToRequestRecieveFromMapToMail = new HashMap<String,List<PaymentBean>>();
		int intTotalNoOfRecords =0;
		String strPendingEmpId = "";
		String strPendingDeptId = "";
		List<String> successList = new ArrayList<String>();
		int cntPaymentId = 0;
		String comments = "";
		double requestAmount = 0;
		String paymentMode = "";
		String utrOrChqNo = "";
		String paymentType = "";
		String siteId = "";
		String siteName = "";
		String contractorId = "";
		String contractorName = "";
		int accDeptPmtProcessId = 0;
		String siteWisePaymentId = "";
		String requestReceiveFrom = "";
		String remarks = "";
		String workOrderNo = "";
		String raBillNo = "";
		double workOrderAmount = 0;
		double raBillAmount = 0;
		String billId = "";
		double advBillAmount = 0;
		String advBillNo = "";
		double sdBillAmount = 0;
		String sdBillNo = "";
		double deductionAmount = 0;
		String raBillDate = "";
		String workOrderDate = "";
		String advBillDate = "";
		String sdBillDate = "";
		String paymentRequestDate = "";
		String paymentRequestReceivedDate = "";
		String qsWorkorderIssueId = "";
		
		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			if(intTotalNoOfRecords > 0){


				strPendingEmpId = objContractorPaymentDao.getApproverEmpIdInAccounts(strUserId);	
				strPendingDeptId = objContractorPaymentDao.getApproverDeptIdInAccounts(strUserId);	
				

				for(int i = 1;i<=intTotalNoOfRecords;i++){
					String strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					if(StringUtils.isNotBlank(strPaymentIntiateType)){
						cntPaymentId = Integer.parseInt(request.getParameter("cntPaymentId"+i));
						comments = request.getParameter("comments"+i);
						remarks = request.getParameter("remarks"+i);
						requestAmount = Double.valueOf(request.getParameter("requestAmount"+i));
						paymentMode = request.getParameter("paymentMode"+i);
						utrOrChqNo = request.getParameter("utrOrChqNo"+i);
						paymentType = request.getParameter("paymentType"+i);
						siteId = request.getParameter("siteId"+i);
						siteName = request.getParameter("siteName"+i);
						contractorId = request.getParameter("contractorId"+i);
						contractorName = request.getParameter("contractorName"+i);
						accDeptPmtProcessId = Integer.parseInt(request.getParameter("accDeptPmtProcessId"+i));
						siteWisePaymentId = request.getParameter("siteWisePaymentId"+i);
						requestReceiveFrom = request.getParameter("requestReceiveFrom"+i);
						workOrderNo = request.getParameter("workOrderNo"+i);
						raBillNo = request.getParameter("raBillNo"+i);
						workOrderAmount = Double.valueOf(request.getParameter("workOrderAmount"+i));
						raBillAmount = Double.valueOf(request.getParameter("raBillAmount"+i));
						billId = request.getParameter("billId"+i);
						advBillAmount = Double.valueOf(request.getParameter("advBillAmount"+i));
						advBillNo = request.getParameter("advBillNo"+i);
						sdBillAmount = Double.valueOf(request.getParameter("sdBillAmount"+i));
						sdBillNo = request.getParameter("sdBillNo"+i);
						deductionAmount = Double.valueOf(request.getParameter("deductionAmount"+i));
						raBillDate = request.getParameter("raBillDate"+i);
						workOrderDate = request.getParameter("workOrderDate"+i);
						advBillDate = request.getParameter("advBillDate"+i);
						sdBillDate = request.getParameter("sdBillDate"+i);
						paymentRequestDate = request.getParameter("paymentRequestDate"+i);
						paymentRequestReceivedDate = request.getParameter("paymentRequestReceivedDate"+i);
						qsWorkorderIssueId = request.getParameter("qsWorkorderIssueId"+i);
						
						PaymentBean objPaymentBean = new PaymentBean();
						
						objPaymentBean.setWorkOrderNo(workOrderNo);
						objPaymentBean.setRaBillNo(raBillNo);
						objPaymentBean.setDoubleAmountToBeReleased(requestAmount);
						objPaymentBean.setStrContractorId(contractorId);
						objPaymentBean.setStrContractorName(contractorName);
						objPaymentBean.setStrSiteId(siteId);
						objPaymentBean.setStrSiteName(siteName);
						objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
						objPaymentBean.setDoubleRaBillAmount(raBillAmount);
						objPaymentBean.setIntAccDeptPaymentProcessId(accDeptPmtProcessId);
						objPaymentBean.setIntCntPaymentId(cntPaymentId);
						objPaymentBean.setRequestReceiveFrom(requestReceiveFrom);
						objPaymentBean.setStrRemarks(comments);
						objPaymentBean.setStrBillId(billId);
						objPaymentBean.setPaymentType(paymentType);
						objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
						objPaymentBean.setStrAdvBillNo(advBillNo);
						objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
						objPaymentBean.setStrSdBillNo(sdBillNo);
						objPaymentBean.setDoubleDeductionAmount(deductionAmount);
						objPaymentBean.setStrRaBillDate(raBillDate);
						objPaymentBean.setStrWorkOrderDate(workOrderDate);
						objPaymentBean.setStrAdvBillDate(advBillDate);
						objPaymentBean.setStrSdBillDate(sdBillDate);
						objPaymentBean.setStrPaymentReqDate(paymentRequestDate);
						objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);
						objPaymentBean.setPaymentMode(paymentMode);
						objPaymentBean.setUtrChequeNo(utrOrChqNo);
						objPaymentBean.setIntSiteWisePaymentId(Integer.valueOf(siteWisePaymentId));
						objPaymentBean.setStrPendingEmpId(strPendingEmpId);
						objPaymentBean.setStrPendingDeptId(strPendingDeptId);
						objPaymentBean.setQsWorkorderIssueId(qsWorkorderIssueId);
						
						if(strPaymentIntiateType.equalsIgnoreCase("Approved")){
							int intTempPaymentTransactionId = objContractorPaymentDao.getAccCntTempPaymentTransactionSeqNo();
							objContractorPaymentDao.insertCntTempPaymentTransactionsTbl(objPaymentBean,intTempPaymentTransactionId);
							objContractorPaymentDao.updateCntAccDeptPaymentProcsstbl(objPaymentBean);
							objContractorPaymentDao.saveCntAccountApprovalDetails(objPaymentBean,strUserId,intTempPaymentTransactionId,siteId);
							
							successList.add("Payment Approved on Bill No:"+billId+" for "+contractorName+" successfully.");
								
							SuccessDataListToMail.add(objPaymentBean);
							//requestReceiveFrom will be different for each site.
							//here storing each requestReceiveFrom(as key) & its payment status list(as value) in a map.
							if(SendApproveStatusToRequestRecieveFromMapToMail.containsKey(requestReceiveFrom)){
								SendApproveStatusToRequestRecieveFromMapToMail.get(requestReceiveFrom).add(objPaymentBean);
							}
							else{
								List<PaymentBean> listForParticularRequestReceiveFrom = new ArrayList<PaymentBean>();
								listForParticularRequestReceiveFrom.add(objPaymentBean);
								SendApproveStatusToRequestRecieveFromMapToMail.put(requestReceiveFrom,listForParticularRequestReceiveFrom);
							}
						}
						else if(strPaymentIntiateType.equalsIgnoreCase("Rejected")){
							objContractorPaymentDao.setInactiveRowInCntAccDeptPmtProcessTbl(accDeptPmtProcessId);
							objContractorPaymentDao.saveCntAccountRejectDetails(objPaymentBean,strUserId,siteId);
							
							objContractorPaymentDao.revertPendingEmployeeInAccCntPayment(accDeptPmtProcessId,cntPaymentId);
							//objContractorPaymentDao.setInactiveRowInAccCntPaymentTbl(cntPaymentId);
							//objContractorPaymentDao.setInactiveRowInQsContractorBill(billId,qsWorkorderIssueId,siteId);
							
							successList.add("Payment Rejected on Bill No:"+billId+" for "+contractorName+" successfully.");
							
							//reject mails pending task.

						}
					}
					
				}
				
			}
		}
		catch (Exception e) {
			successList.add("Failed");
			e.printStackTrace();
		}
		final String user_name_final=user_name;
		final String pendingDeptId_final=strPendingDeptId;
		final int getLocalPort = request.getLocalPort();
		//final String requestReceiveFrom_final = requestReceiveFrom;
		/*if(SuccessDataListToMail.size()!=0 && StringUtils.isNotBlank(strPendingDeptId)){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						//sending mail for Approval to next level
						String [] emailToAddress = getEmailsOfEmpByDeptId(pendingDeptId_final);
						objEmailFunction.sendContractorPaymentApprovalMailToAccountsAllLevels(SuccessDataListToMail,emailToAddress,getLocalPort,user_name_final);
						//sending status mail to lower level 
						for(Map.Entry<String, List<PaymentBean>> entry : SendApproveStatusToRequestRecieveFromMapToMail.entrySet()){
							String [] emailToAddress1 = getEmailsOfEmpByEmpId(entry.getKey());
							objEmailFunction.sendContractorPaymentApprovalStatusMailToLowerEmployee(entry.getValue(),emailToAddress1,getLocalPort,user_name_final);
						}
					}

					
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}*/


		//strResponse = objPaymentProcessDao.getAccDeptPaymentPendingDetails(steEmpId);
		return successList;
	}
	
	protected String[] getEmailsOfEmpByDeptId(String pendingDeptId) {
		return objContractorPaymentDao.getEmailsOfEmpByDeptId(pendingDeptId);
	}
	private String[] getEmailsOfEmpByEmpId(String requestReceiveFrom) {
		return objContractorPaymentDao.getEmailsOfEmpByEmpId(requestReceiveFrom);
	}


	@Override
	public List<PaymentBean> getCntAccDeptPaymentApprovalDetails(String site_id, String user_id) {
		
		return objContractorPaymentDao.getCntAccDeptPaymentApprovalDetails(site_id,user_id);
		
	}


	@Override
	public List<PaymentModesBean> getPaymentModes() {
		return objContractorPaymentDao.getPaymentModes();
	}


	@Override
	public List<PaymentBean> updateAccountDeptTransaction(HttpServletRequest request, HttpServletResponse response,String strUserId, String user_name) {

		//this list is for to send payments pending for next Account Dept level.
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		//this map is for to send rejected payments in This Account level To its Lower Account Dept level Employees.
		final List<PaymentBean> PaymentRejectListToMail = new ArrayList<PaymentBean>();
		List<PaymentBean> paymentDetailsList = new ArrayList<PaymentBean>();
		
		int intTempPaymentTransactionId = 0;
		
		int intTotalNoOfRecords = 0;
		String strPendingEmpId = "";
		String strPendingDeptId = "";
		int cntPaymentId = 0;
		String comments = "";
		double requestAmount = 0;
		String paymentMode = "";
		String utrOrChqNo = "";
		String paymentType = "";
		String siteId = "";
		String siteName = "";
		String contractorId = "";
		String contractorName = "";
		int accDeptPmtProcessId = 0;
		String siteWisePaymentId = "";
		String requestReceiveFrom = "";
		String remarks = "";
		String workOrderNo = "";
		String raBillNo = "";
		double workOrderAmount = 0;
		double raBillAmount = 0;
		String billId = "";
		String tempBillId = "";
		double advBillAmount = 0;
		String advBillNo = "";
		double sdBillAmount = 0;
		String sdBillNo = "";
		double deductionAmount = 0;
		String raBillDate = "";
		String workOrderDate = "";
		String advBillDate = "";
		String sdBillDate = "";
		String paymentRequestDate = "";
		String paymentRequestReceivedDate = "";
		String qsWorkorderIssueId = "";
		String nmrBillNo = "";
		double nmrBillAmount = 0; 
		String nmrBillDate = "";
		String billNumber = "";
		double billAmount = 0;
		String billDate = "";
		
		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			if(intTotalNoOfRecords > 0){


				strPendingEmpId = objContractorPaymentDao.getApproverEmpIdInAccounts(strUserId);	
				strPendingDeptId = objContractorPaymentDao.getApproverDeptIdInAccounts(strUserId);	


				for(int i = 1;i<=intTotalNoOfRecords;i++){
					String strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					if(StringUtils.isNotBlank(strPaymentIntiateType)){
						cntPaymentId = Integer.parseInt(request.getParameter("cntPaymentId"+i));
						intTempPaymentTransactionId = Integer.parseInt(request.getParameter("tempPaymentTransactionId"+i));
						comments = request.getParameter("comments"+i);
						remarks = request.getParameter("remarks"+i);
						requestAmount = Double.valueOf(request.getParameter("requestAmount"+i));
						paymentMode = request.getParameter("paymentMode"+i);
						utrOrChqNo = request.getParameter("utrOrChqNo"+i);
						paymentType = request.getParameter("paymentType"+i);
						siteId = request.getParameter("siteId"+i);
						siteName = request.getParameter("siteName"+i);
						contractorId = request.getParameter("contractorId"+i);
						contractorName = request.getParameter("contractorName"+i);
						accDeptPmtProcessId = Integer.parseInt(request.getParameter("accDeptPmtProcessId"+i));
						siteWisePaymentId = request.getParameter("siteWisePaymentId"+i);
						requestReceiveFrom = request.getParameter("requestReceiveFrom"+i);
						workOrderNo = request.getParameter("workOrderNo"+i);
						raBillNo = request.getParameter("raBillNo"+i);
						workOrderAmount = Double.valueOf(request.getParameter("workOrderAmount"+i));
						raBillAmount = Double.valueOf(request.getParameter("raBillAmount"+i));
						billId = request.getParameter("billId"+i);
						tempBillId = request.getParameter("tempBillId"+i);
						advBillAmount = Double.valueOf(request.getParameter("advBillAmount"+i));
						advBillNo = request.getParameter("advBillNo"+i);
						sdBillAmount = Double.valueOf(request.getParameter("sdBillAmount"+i));
						sdBillNo = request.getParameter("sdBillNo"+i);
						deductionAmount = Double.valueOf(request.getParameter("deductionAmount"+i));
						raBillDate = request.getParameter("raBillDate"+i);
						workOrderDate = request.getParameter("workOrderDate"+i);
						advBillDate = request.getParameter("advBillDate"+i);
						sdBillDate = request.getParameter("sdBillDate"+i);
						paymentRequestDate = request.getParameter("paymentRequestDate"+i);
						paymentRequestReceivedDate = request.getParameter("paymentRequestReceivedDate"+i);
						qsWorkorderIssueId = request.getParameter("qsWorkorderIssueId"+i);
						billNumber = request.getParameter("billNumber"+i);
						billAmount = Double.valueOf(request.getParameter("billAmount"+i));
						billDate = request.getParameter("billDate"+i);
						
						PaymentBean objPaymentBean = new PaymentBean();
						
						objPaymentBean.setWorkOrderNo(workOrderNo);
						objPaymentBean.setRaBillNo(raBillNo);
						objPaymentBean.setDoubleAmountToBeReleased(requestAmount);
						objPaymentBean.setStrContractorId(contractorId);
						objPaymentBean.setStrContractorName(contractorName);
						objPaymentBean.setStrSiteId(siteId);
						objPaymentBean.setStrSiteName(siteName);
						objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
						objPaymentBean.setDoubleRaBillAmount(raBillAmount);
						objPaymentBean.setIntTempPaymentTransactionId(intTempPaymentTransactionId);
						objPaymentBean.setIntAccDeptPaymentProcessId(accDeptPmtProcessId);
						objPaymentBean.setIntCntPaymentId(cntPaymentId);
						objPaymentBean.setRequestReceiveFrom(requestReceiveFrom);
						objPaymentBean.setStrRemarks(comments);
						objPaymentBean.setStrBillId(billId);
						objPaymentBean.setStrTempBillId(tempBillId);
						objPaymentBean.setPaymentType(paymentType);
						
						if(paymentType.equals("RA")){
							raBillNo = billNumber;
							raBillAmount = billAmount; 
							raBillDate = billDate;
						}
						if(paymentType.equals("ADV")){
							advBillNo = billNumber;
							advBillAmount = billAmount;  
							advBillDate = billDate;
						}
						if(paymentType.equals("SEC")){
							sdBillNo = billNumber;
							sdBillAmount = billAmount; 
							sdBillDate = billDate; 
						}
						if(paymentType.equals("NMR")){
							nmrBillNo = billNumber;
							nmrBillAmount = billAmount; 
							nmrBillDate = billDate;
						}
						
						objPaymentBean.setBillNumber(billNumber);
						objPaymentBean.setDoubleBillAmount(billAmount);
						objPaymentBean.setStrBillDate(billDate);
						objPaymentBean.setStrNmrBillNo(nmrBillNo);
						objPaymentBean.setDoubleNmrBillAmount(nmrBillAmount);
						objPaymentBean.setStrNmrBillDate(nmrBillDate);
					    
						objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
						objPaymentBean.setStrAdvBillNo(advBillNo);
						objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
						objPaymentBean.setStrSdBillNo(sdBillNo);
						objPaymentBean.setDoubleDeductionAmount(deductionAmount);
						objPaymentBean.setStrRaBillDate(raBillDate);
						objPaymentBean.setStrWorkOrderDate(workOrderDate);
						objPaymentBean.setStrAdvBillDate(advBillDate);
						objPaymentBean.setStrSdBillDate(sdBillDate);
						objPaymentBean.setStrPaymentReqDate(paymentRequestDate);
						objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);
						objPaymentBean.setPaymentMode(paymentMode);
						objPaymentBean.setUtrChequeNo(utrOrChqNo);
						objPaymentBean.setIntSiteWisePaymentId(Integer.valueOf(siteWisePaymentId));
						objPaymentBean.setStrPendingEmpId(strPendingEmpId);
						objPaymentBean.setStrPendingDeptId(strPendingDeptId);
						objPaymentBean.setStatus(strPaymentIntiateType);
						objPaymentBean.setQsWorkorderIssueId(qsWorkorderIssueId);
						
						if(strPaymentIntiateType.equalsIgnoreCase("Approved")){
							objContractorPaymentDao.saveCntAccountApprovalDetails(objPaymentBean,strUserId,intTempPaymentTransactionId,siteId);
							//if next level is not a vendor (i.e. not a final level)
							if(StringUtils.isBlank(strPendingEmpId) && StringUtils.isNotBlank(strPendingDeptId)){
								objContractorPaymentDao.updateCntTempPaymentTransactionsTbl(objPaymentBean,strPendingDeptId);
							}
							//if next level is vendor (i.e. if it is final level)
							else if(StringUtils.isBlank(strPendingDeptId)&& strPendingEmpId.equals("VND")){
								objContractorPaymentDao.updateCntTempPaymentTransactionsTbl(objPaymentBean,strPendingEmpId);
								objContractorPaymentDao.updateAllocatedAmountInCntAccDeptPaymentProcesstbl(objPaymentBean);
								int intPaymentTransactionId = objContractorPaymentDao.getAccCntPaymentTransactionSeqNo();
								//inserting payment in final Transaction table.
								objContractorPaymentDao.insertCntPaymentTransactionsTbl(objPaymentBean,intPaymentTransactionId);
								Map<String,String> deductionAmounts= objContractorPaymentDao.getDeductionAmountDetails(tempBillId);
								if(!deductionAmounts.isEmpty()){
									if(deductionAmounts.containsKey("ADV")){
										double advanceDeductionAmount = Double.valueOf(deductionAmounts.get("ADV"));
										objContractorPaymentDao.insertCntAdvanceDebitHistory(objPaymentBean,advanceDeductionAmount);
										objContractorPaymentDao.minusDeductionAmountFromAdvanceAmountInCntAdvanceDtlsTable(objPaymentBean,advanceDeductionAmount);
									}
									if(deductionAmounts.containsKey("SEC")){
										double securityDepositDeductionAmount = Double.valueOf(deductionAmounts.get("SEC"));
										objContractorPaymentDao.insertCntSecDepositDebitHistory(objPaymentBean,securityDepositDeductionAmount);
										objContractorPaymentDao.depositDeductionAmountUnderSecDepositInCntSecDepositTable(objPaymentBean,securityDepositDeductionAmount);
									}
								}
								if(paymentType.equals("ADV")){
									objContractorPaymentDao.insertCntAdvanceCreditHistory(objPaymentBean);
									objContractorPaymentDao.insertOrUpdateAdvancePaidAmountInCntAdvanceDtlsTable(objPaymentBean);
								}
								if(paymentType.equals("RA")||paymentType.equals("NMR")){
									objContractorPaymentDao.insertCntInvoiceHistory(objPaymentBean,intPaymentTransactionId);
								}
								if(paymentType.equals("SEC")){
									objContractorPaymentDao.insertCntSecDepositCreditHistory(objPaymentBean);
									objContractorPaymentDao.inactiveSecDepositInCntSecDepositTable(objPaymentBean);
								}
								objContractorPaymentDao.inactivePaymentAfterCheck(objPaymentBean);
								
							}
							paymentDetailsList.add(objPaymentBean);
						}
						else if(strPaymentIntiateType.equalsIgnoreCase("Rejected")){
							objContractorPaymentDao.saveCntAccountRejectDetails(objPaymentBean,strUserId,siteId);
							String strLowerEmpId = objContractorPaymentDao.getLowerEmpIdInAccounts(strUserId);
							if(strLowerEmpId.equals("997_B_1")||strLowerEmpId.equals("997_H_1")){
								objContractorPaymentDao.updateRowInCntAccTempPaymentTransactions(objPaymentBean);
								objContractorPaymentDao.updateIntiateAmountInCntAccDeptPaymentProcesstbl(objPaymentBean);
							}
							else{
								objContractorPaymentDao.revertPendingApprovalToLowerEmployee(strLowerEmpId,intTempPaymentTransactionId,comments);
							}
							paymentDetailsList.add(objPaymentBean);
						}
						
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return paymentDetailsList;
	}


	@Override
	public List<PaymentBean> getViewContractorPaymentDetails(String fromDate, String toDate, String contractorId,
			String user_id, String dropdown_SiteId, String workOrderNo) {
		return objContractorPaymentDao.getViewContractorPaymentDetails(fromDate, toDate,contractorId,user_id,dropdown_SiteId,workOrderNo);
	}


	@Override
	public List<PaymentBean> viewMyContractorPayment(String fromDate, String toDate, String site_id, String user_id,String contractorId, String workOrderNo,String dropdown_SiteId) {
		return objContractorPaymentDao.viewMyContractorPayment(fromDate, toDate, site_id, user_id,contractorId,workOrderNo,dropdown_SiteId);
	}


	@Override
	public List<PaymentBean> getContractorPaymentDetailsToUpdate(String user_id, String fromDate,String toDate, String contractorId, String workOrderNo, String dropdown_SiteId) {
		return objContractorPaymentDao.getContractorPaymentDetailsToUpdate(user_id,fromDate,toDate,contractorId,workOrderNo,dropdown_SiteId);
	}


	@Override
	public String updateRefNoInAccDeptContractorTransaction(HttpServletRequest request, String strUserId) {


		String strResponse = "";
		int intPaymentTransactionId=0;
		String strRefNo = "";
		int intTotalNoOfRecords = 0;
		String paymentMode = "";
		String paymentDate = "";

		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			if(intTotalNoOfRecords > 0){


				for(int i = 1;i<=intTotalNoOfRecords;i++){

					if(request.getParameter("checkboxname"+i)!=null){
						intPaymentTransactionId = Integer.parseInt(request.getParameter("paymentTransactionId"+i));
						strRefNo = request.getParameter("utrChequeNo"+i);
						paymentMode = request.getParameter("paymentMode"+i);
						paymentDate = request.getParameter("paymentDate"+i);
						objContractorPaymentDao.updateRefNoInAccDeptContractorTransaction(strRefNo,intPaymentTransactionId,paymentMode,paymentDate);
						objContractorPaymentDao.updateRefNoInContractorInvoiceHistory(strRefNo,paymentMode,intPaymentTransactionId);
					}

				}

			}
			strResponse = "Success";
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return strResponse;

	}


	@Override
	public List<PaymentBean> downloadPaymentsToExcelFileForUpdate(String user_id, String fromDate, String toDate,
			String contractorId, String workOrderNo, String dropdown_SiteId) {
		
		List<PaymentBean> list = objContractorPaymentDao.downloadInvoicePaymentsToExcelFileForUpdate(user_id,fromDate,toDate);
		List<PaymentBean> list2 = objContractorPaymentDao.downloadContractorPaymentsToExcelFileForUpdate(user_id,fromDate,toDate);
		list.addAll(list2);
		
		return list;
		}


	@Override
	public void writeDataToExcelSheet(XSSFWorkbook workbook, List<PaymentBean> list) {
		XSSFSheet spreadsheet = workbook.createSheet("Payment Details");
        //spreadsheet.setDefaultColumnWidth(20);
		spreadsheet.createFreezePane(0, 1); 
        spreadsheet.setColumnWidth(0, 9500);
        spreadsheet.setColumnWidth(1, 4500);
        spreadsheet.setColumnWidth(2, 9500);
        spreadsheet.setColumnWidth(3, 4500);
        spreadsheet.setColumnWidth(4, 4500);
        spreadsheet.setColumnWidth(5, 4500);
        spreadsheet.setColumnWidth(6, 9000);
        spreadsheet.setColumnWidth(7, 6000);
        spreadsheet.setColumnWidth(8, 3000);
        spreadsheet.setColumnWidth(9, 3500);
        
        // create style for header cells
		XSSFCellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);
		
		// create header row
		XSSFRow header = spreadsheet.createRow(0);
		
		header.createCell(0).setCellValue("Vendor / Contractor");
		header.getCell(0).setCellStyle(style);
		header.getCell(0).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(1).setCellValue("Invoice No");
		header.getCell(1).setCellStyle(style);
		header.getCell(1).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(2).setCellValue("PO Number");
		header.getCell(2).setCellStyle(style);
		header.getCell(2).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(3).setCellValue("WorkOrder No");
		header.getCell(3).setCellStyle(style);
		header.getCell(3).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(4).setCellValue("Paid Amount");
		header.getCell(4).setCellStyle(style);
		header.getCell(4).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(5).setCellValue("UTR/Cheque No");
		header.getCell(5).setCellStyle(style);
		header.getCell(5).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(6).setCellValue("Payment Done Date(dd/Month/yy)");
		header.getCell(6).setCellStyle(style);
		header.getCell(6).setCellType ( Cell.CELL_TYPE_STRING ) ; 

		header.createCell(7).setCellValue("PaymentTransaction Id");
		header.getCell(7).setCellStyle(style);
		header.getCell(7).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		header.createCell(8).setCellValue("Payment Id");
		header.getCell(8).setCellStyle(style);
		header.getCell(8).setCellType ( Cell.CELL_TYPE_STRING ) ; 

		header.createCell(9).setCellValue("VND / CNT");
		header.getCell(9).setCellStyle(style);
		header.getCell(9).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		
		// create data rows
		int rowCount = 1;
		
		for (PaymentBean PBobj : list) {
			XSSFRow aRow = spreadsheet.createRow(rowCount++);
			if(PBobj.getIsVNDorCNT().equals("VND")){
				aRow.createCell(0).setCellValue(PBobj.getStrVendorName());
				aRow.createCell(1).setCellValue(PBobj.getStrInvoiceNo());
				aRow.createCell(2).setCellValue(PBobj.getStrPONo());
				//aRow.createCell(3).setCellValue("");
				aRow.createCell(4).setCellValue(PBobj.getDoublePaidAmount());
				aRow.createCell(5).setCellValue(PBobj.getUtrChequeNo());
				aRow.createCell(6).setCellValue(PBobj.getStrPaymentDate());
				aRow.createCell(7).setCellValue(PBobj.getIntPaymentTransactionId());
				aRow.createCell(8).setCellValue(PBobj.getIntPaymentDetailsId());
				aRow.createCell(9).setCellValue("VND");
			}
			if(PBobj.getIsVNDorCNT().equals("CNT")){
				aRow.createCell(0).setCellValue(PBobj.getStrContractorName());
				//aRow.createCell(1).setCellValue("");
				//aRow.createCell(2).setCellValue("");
				aRow.createCell(3).setCellValue(PBobj.getWorkOrderNo());
				aRow.createCell(4).setCellValue(PBobj.getDoublePaidAmount());
				aRow.createCell(5).setCellValue(PBobj.getUtrChequeNo());
				aRow.createCell(6).setCellValue(PBobj.getStrPaymentDate());
				aRow.createCell(7).setCellValue(PBobj.getIntPaymentTransactionId());
				aRow.createCell(8).setCellValue(PBobj.getIntCntPaymentId());
				aRow.createCell(9).setCellValue("CNT");
			}
		}

		
	}


	@Override
	public void writeExcelDataToResponse(HttpServletResponse response,File file) {

		response.setContentType("application/vnd.ms-excel");
        response.addHeader("content-disposition", "attachment; filename="+file.getName());
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        final int size = 1024;
        try {
            response.setContentLength(fin.available());
            final byte[] buffer = new byte[size];
            ServletOutputStream outputStream = null;

            outputStream = response.getOutputStream();
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            fin.close();
            outputStream.flush();
            outputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
	}


	@Override
	public String generateFileName(String user_id) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");  
	    Date date = new Date();  
	    return "Update Payments "+formatter.format(date); 
	}


	@Override
	public String updatePaymentRefNoFromExcel(MultipartFile multipartFile,List<PaymentBean> successPayments, List<PaymentBean> failedPayments,List<String> errMsg) throws IOException {

		String response = "Success";
		int total_rows_updated = 0;
		try{
			InputStream inp =  new BufferedInputStream(multipartFile.getInputStream());

			Workbook wb = null;
			try {
				wb = WorkbookFactory.create(inp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Sheet sheet = wb.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();
			int rowsCount = sheet.getLastRowNum();
			//validations
			/*for (int i = 1; i <= rowsCount; i++) {
				Row row = sheet.getRow(i);
				String isVNDorCNT = formatter.formatCellValue(row.getCell(0));
				//row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				//String paymentTransactionId = row.getCell(1).getStringCellValue();
				String paymentTransactionId = "";
				if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
					paymentTransactionId = String.valueOf((int)row.getCell(1).getNumericCellValue()); 
				}
				//String paymentTransactionId = String.valueOf(Integer.parseInt(formatter.formatCellValue(row.getCell(1))));
				
				if(StringUtils.isBlank(paymentTransactionId)){
					continue;
				}
				String utrChequeNo = formatter.formatCellValue(row.getCell(7));
				if(StringUtils.isBlank(utrChequeNo)){
					errMsg.add(0,"please give utr/chequeNo for "+isVNDorCNT+",Tr_Id-"+paymentTransactionId);
					return "validations";
				}String strPaymentDate = formatter.formatCellValue(row.getCell(8));
				if(StringUtils.isBlank(strPaymentDate)){
					errMsg.add(0,"please give payment date for "+isVNDorCNT+",Tr_Id-"+paymentTransactionId);
					return "validations";
				}
			}*/
			for (int i = 1; i <= rowsCount; i++) {
				Row row = sheet.getRow(i);
				/****************** checking not null******************/
				String givenUtrChequeNo = null; 
				if(row.getCell(5)!=null){
					givenUtrChequeNo = formatter.formatCellValue(row.getCell(5));
				}
				String givenPaymentDate = null;
				if(row.getCell(6)!=null){
					givenPaymentDate = formatter.formatCellValue(row.getCell(6));
				}
				if(StringUtils.isBlank(givenUtrChequeNo)||StringUtils.isBlank(givenPaymentDate)){
					continue;
				}
				/******************************************************/
				
				String isVNDorCNT = row.getCell(9).getStringCellValue(); 
				int intPaymentTransactionId = (int)row.getCell(7).getNumericCellValue(); 
				
				/*int intPaymentId = Integer.parseInt(formatter.formatCellValue(row.getCell(2)));
            	String strInvoiceNo = formatter.formatCellValue(row.getCell(3));
            	String strPONo = formatter.formatCellValue(row.getCell(4));
            	String workOrderNo = formatter.formatCellValue(row.getCell(5));
            	double doublePaidAmount = Double.valueOf(formatter.formatCellValue(row.getCell(6)));*/
				String utrChequeNo = "";
				if(row.getCell(5).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
					utrChequeNo = String.valueOf((int)row.getCell(5).getNumericCellValue()); 
				}
				else{
					utrChequeNo = row.getCell(5).getStringCellValue(); 
				}
				Date paymentDate = null; 
				if(row.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
					paymentDate = row.getCell(6).getDateCellValue();
				}
				else{
					String strPaymentDate = row.getCell(6).getStringCellValue();
					SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yy");
					paymentDate = dt.parse(strPaymentDate);
					
				}
				String invoiceNo = "";
				if(row.getCell(1)!=null){
					if(row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
						invoiceNo = String.valueOf((int)row.getCell(1).getNumericCellValue()); 
					}
					else{
						invoiceNo = row.getCell(1).getStringCellValue(); 
					}
				}
				
				String poNo = "";
				if(row.getCell(2)!=null){
					if(row.getCell(2).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
						poNo = String.valueOf((int)row.getCell(2).getNumericCellValue()); 
					}
					else{
						poNo = row.getCell(2).getStringCellValue(); 
					}
				}
				String workOrderNo = "";
				if(row.getCell(3)!=null){
					if(row.getCell(3).getCellType() == Cell.CELL_TYPE_NUMERIC) { 
						workOrderNo = String.valueOf((int)row.getCell(3).getNumericCellValue()); 
					}
					else{
						workOrderNo = row.getCell(3).getStringCellValue(); 
					}
				}

				PaymentBean objPaymentBean = null;
				try{
					objPaymentBean = new PaymentBean();
					objPaymentBean.setIsVNDorCNT(isVNDorCNT);
					objPaymentBean.setIntPaymentTransactionId(intPaymentTransactionId);
					objPaymentBean.setUtrChequeNo(utrChequeNo);
					//objPaymentBean.setStrPaymentDate(strPaymentDate);
					objPaymentBean.setStrInvoiceNo(invoiceNo);
					objPaymentBean.setStrPONo(poNo);
					objPaymentBean.setWorkOrderNo(workOrderNo);
					
					if(isVNDorCNT.equals("VND")){
						int count = objContractorPaymentDao.updateVendorPaymentRefNoFromExcel(utrChequeNo, intPaymentTransactionId, paymentDate);
						total_rows_updated += count;
					}
					if(isVNDorCNT.equals("CNT")){
						int count = objContractorPaymentDao.updateContractorPaymentRefNoFromExcel(utrChequeNo,intPaymentTransactionId,paymentDate);
						total_rows_updated += count;
					}
					successPayments.add(objPaymentBean);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					failedPayments.add(objPaymentBean);
					response = "Failed";
				}
			}
			if(total_rows_updated==0){
				response = "Already";
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			response = "Exception";
		}
		return response;
	}
}


