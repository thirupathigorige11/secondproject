package com.sumadhura.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.ContractorTaxReportBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PurchaseTaxReportBean;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.dto.PaymentDto;
import com.sumadhura.transdao.PaymentProcessDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.DedupTransactionHandler;
import com.sumadhura.util.UIProperties;


@Service
public class PaymentProcessServiceImpl extends UIProperties implements PaymentProcessService{

	@Autowired
	PaymentProcessDao objPaymentProcessDao;
	
	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
	DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();

	@Override
	public List<String> savePaymentIntiateDetails( HttpServletRequest request,HttpSession session){
		//this list is for to send payments pending for next level(site 3rd level or Accounts 1st level).
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		ModelAndView model = null;
		String strInvoiceNo = "";
		String strVendorId = "";
		String strVendorName = "";
		String strPaymentReqDate = "";
		double doubleAmountToBeReleased = 0.0;
		String strRemarks = "";
		String strInvoiceDate = "";
		int paymentId = 0;
		int PaymentEntryDetailsSeqID = 0;
		double doubleInvoiceAmount = 0.0;
		double doublePOAmount = 0.0;
		String strSiteId = "";
		String dcNo = "";
		String poNo = "";
		String pendingEmpId = "";
		String strPODate = "";
		String strInvoiceReceivedDate = "";
		String strDCDate = "";
		String strInvoiceNoInAP = "";
		String paymentType = "";
		String indentEntryId = "";
		double doubleCreditTotalAmount = 0.0;
		String strCreditNoteNumber = "";
		double doubleAdjustAmountFromAdvance = 0.0;
		List<String> successList = new ArrayList<String>();
		String user_id="";
		String site_name ="";
		String user_name = "";
		String session_siteId = "";
		String StrRoleId = "";
		String responseForTaxSubmit="";
		String trankey = "";
		try {
			StrRoleId = String.valueOf(session.getAttribute("Roleid"));
			site_name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();	
			user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			session_siteId = String.valueOf(session.getAttribute("SiteId"));
			user_id=String.valueOf(session.getAttribute("UserId"));
			pendingEmpId = objPaymentProcessDao.getPendingEmpId(user_id,session_siteId);
			
			model = new ModelAndView();
			int intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfRecords") == null ? "0" : request.getParameter("noOfRecords").toString());
			String rollIdForInvoiceTaxSubmit = UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit") == null ? "00" : UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit").toString();
			if(intTotalNoOfRecords > 0){
				PaymentDto  objPaymentDto = null;
				for(int i = 1;i<=intTotalNoOfRecords;i++){
					if(request.getParameter("checkboxname"+i)!=null){

						if(StringUtils.isBlank(request.getParameter("AmountToBeReleased"+i))||StringUtils.isBlank(request.getParameter("PaymentreqDateId"+i)))
							{
								successList.add("Failed. You not entered the Amount (or) Req.Date.");
								continue;
							}

						objPaymentDto = new PaymentDto();




						paymentType = request.getParameter("paymentType"+i);
						doubleAmountToBeReleased = Double.valueOf(request.getParameter("AmountToBeReleased"+i) == null ? "0" : request.getParameter("AmountToBeReleased"+i));

						poNo = request.getParameter("poNo"+i);
						strVendorId = request.getParameter("vendorId"+i);
						indentEntryId = request.getParameter("indentEntryId"+i)==null ? "0" : request.getParameter("indentEntryId"+i);
						doubleAdjustAmountFromAdvance = Double.valueOf(request.getParameter("AdjustAmountFromAdvance"+i)==null ? "0" : request.getParameter("AdjustAmountFromAdvance"+i));
						
						strInvoiceNo = request.getParameter("invoiceNo"+i);
						
						//ACP stopping the payment to initiate if the tax invoice is not submitted
						if(paymentType.equals("DIRECT")){
							if(StrRoleId.equals(rollIdForInvoiceTaxSubmit)){
								try	{
									responseForTaxSubmit=objPaymentProcessDao.checkIsTaxInvoiceIsSubmitedOrNot(strVendorId,indentEntryId,strInvoiceNo,session_siteId);
									if(!responseForTaxSubmit.equals("SUBMITTED")){
										//successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Tax invoice is not submitted.");
										successList.add("Payment request is failed as tax invoice no: "+strInvoiceNo+" is not submitted");
										continue;
									}
								}catch(Exception e){
									e.printStackTrace();
									//successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Tax invoice is not submitted.");
									successList.add("Payment request is failed as tax invoice no: "+strInvoiceNo+" is not submitted");
									continue;
								}
							}
						}
						//SUBMITTED
						
						//checking is Total Po Amount is Paid
						boolean isTotalPoAmountIsPaid = objPaymentProcessDao.checkingIsTotalPoAmountIsPaid(poNo,doubleAmountToBeReleased);
						if(isTotalPoAmountIsPaid){
							successList.add("Failed. "+"InvoiceNumber: "+strInvoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
							continue;
						}
						//checking Adjust Amount is greater than Advance or not
						String remainingAmountInAdvanceInDB = objPaymentProcessDao.getRemainingAmountInAdvance(poNo);
						if(doubleAdjustAmountFromAdvance>Double.valueOf(remainingAmountInAdvanceInDB)){
							successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Adjust Amount is greater than Advance.");
							continue;
						}
						//checking   Is the total requesting amount exceeded the limit of Invoice amount.
						if(paymentType.equals("DIRECT")){
							double invoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i) == null ? "0" : request.getParameter("invoiceAmount"+i));
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+doubleAmountToBeReleased+doubleAdjustAmountFromAdvance)>invoiceAmount){
								successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Total of Initiated Amount & Paid Amount is greater than Invoice Amount.");
								continue;
							}
						}
						//Do not pay more than PO amount
						String payBalanceInPo = request.getParameter("payBalanceInPo"+i);
						String paymentDoneOnMultipleInvoices = request.getParameter("paymentDoneOnMultipleInvoices"+i);
						String adjustedAmountFromPo = request.getParameter("adjustedAmountFromPo"+i);

						if(!payBalanceInPo.equals("NO_ADVANCE")){
							//validating current requested amount to balance in PO
							if(doubleAmountToBeReleased > Double.valueOf(payBalanceInPo)){
								successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
								continue;
							}
							//validating current requested amount, balance in PO & total payment done on multiple invoices of PO
							if(doubleAmountToBeReleased > Double.valueOf(payBalanceInPo) - (Double.valueOf(paymentDoneOnMultipleInvoices) - Double.valueOf(adjustedAmountFromPo))){
								successList.add("Failed. "+"Invoice No: "+strInvoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
								continue;
							}
						}


						strInvoiceDate = request.getParameter("invoiceDate"+i);
						strInvoiceNoInAP = request.getParameter("invoiceNoInAP"+i);
						strVendorId = request.getParameter("vendorId"+i);
						strVendorName =  request.getParameter("vendorName"+i);
						strPaymentReqDate = request.getParameter("PaymentreqDateId"+i);
						strRemarks = request.getParameter("remarks"+i);
						paymentId = Integer.parseInt(request.getParameter("paymentSeqId"+i) == null ? "0" : request.getParameter("paymentSeqId"+i));
						doubleInvoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i));
						strSiteId = request.getParameter("siteId"+i);
						dcNo = request.getParameter("dcNo"+i);

						doublePOAmount = Double.valueOf(request.getParameter("poAmount"+i));
						strPODate = request.getParameter("poDate"+i);
						strInvoiceReceivedDate = request.getParameter("invoiceReceivedDate"+i);
						strDCDate = request.getParameter("dcDate"+i);
						indentEntryId = request.getParameter("indentEntryId"+i)==null ? "0" : request.getParameter("indentEntryId"+i);
						doubleCreditTotalAmount = Double.valueOf(request.getParameter("creditTotalAmount"+i)==null ? "0" : request.getParameter("creditTotalAmount"+i));
						strCreditNoteNumber = request.getParameter("creditNoteNumber"+i)==null ? "0" : request.getParameter("creditNoteNumber"+i);
						doubleAdjustAmountFromAdvance = Double.valueOf(request.getParameter("AdjustAmountFromAdvance"+i)==null ? "0" : request.getParameter("AdjustAmountFromAdvance"+i));

						WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+strInvoiceNo+" , PONumber:"+poNo);

						
						trankey = indentEntryId+"|"+doubleAmountToBeReleased;
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						
						objPaymentDto.setDoubleAmountToBeReleased(doubleAmountToBeReleased);
						objPaymentDto.setStrInvoiceNo(strInvoiceNo);
						objPaymentDto.setStrInvoiceNoInAP(strInvoiceNoInAP);
						objPaymentDto.setStrVendorId(strVendorId);
						objPaymentDto.setStrPaymentReqDate(strPaymentReqDate);
						objPaymentDto.setStrInvoiceDate(strInvoiceDate);
						objPaymentDto.setStrRemarks(strRemarks);
						objPaymentDto.setDoubleInvoiceAmount(doubleInvoiceAmount);
						objPaymentDto.setStrSiteId(strSiteId);
						objPaymentDto.setStrDCNo(dcNo);
						objPaymentDto.setStrPONo(poNo);
						objPaymentDto.setStrPendingEmpId(pendingEmpId);
						objPaymentDto.setIntPaymentId(paymentId);
						objPaymentDto.setDoublePOAmount(doublePOAmount);
						objPaymentDto.setStrPODate(strPODate);
						objPaymentDto.setStrInvoiceReceivedDate(strInvoiceReceivedDate);
						objPaymentDto.setStrDCDate(strDCDate);
						int intSiteWisePaymentNo = 0;
						intSiteWisePaymentNo = objPaymentProcessDao.getSiteWisePaymentNo(strSiteId);
						objPaymentDto.setIntSiteWisePaymentId(intSiteWisePaymentNo);
						objPaymentDto.setPaymentType(paymentType);
						objPaymentDto.setIntIndentEntryId(Integer.valueOf(indentEntryId));
						objPaymentDto.setDoubleCreditTotalAmount(doubleCreditTotalAmount);
						objPaymentDto.setStrCreditNoteNumber(strCreditNoteNumber);
						objPaymentDto.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);

						
						//if paymentId is ZERO means there is no row in with this InvoiceNo or PONumber payment table.
						if(paymentId == 0){
							paymentId = objPaymentProcessDao.getPaymentId();
						}
						
						//if paymentId is NOT ZERO means already there is a row in payment table.
						if( objPaymentDto.getIntPaymentId() > 0){
							objPaymentProcessDao.updatePaymentTable(objPaymentDto);
						}
						else{
							/* checking in ACC_PAYMENT is there any row with current PO_NUMBER with empty INVOICE_NUMBER */
							int poPaymentId = objPaymentProcessDao.returnPaymentIdIfThereIsAnyRowWithCurrentPO_NUMBER(objPaymentDto);
							if(poPaymentId>0){
								//checking   Is the total requesting amount exceeded the limit of PO amount.
								/*boolean isLimitExceed = objPaymentProcessDao.checkingIsRequestAmountExceedLimitOfPoAmount(doubleAmountToBeReleased,poPaymentId);
								if(isLimitExceed){
									successList.add("Failed. "+"InvoiceNumber: "+strInvoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
									continue;
								}*/
								paymentId = poPaymentId;
								objPaymentProcessDao.updatePaymentTableOnPoNumber(objPaymentDto, poPaymentId);
							}
							else{
								objPaymentProcessDao.savePaymentTable(objPaymentDto, paymentId);
							}
						}
						
						PaymentEntryDetailsSeqID = objPaymentProcessDao.getPaymentDetailsId();
						
						objPaymentProcessDao.savePaymentProcessDtlsTable(objPaymentDto, paymentId,PaymentEntryDetailsSeqID);
						
						//updating Initiated advance amount in Po Advance table.
						objPaymentProcessDao.updatePoAdvanceTable(objPaymentDto);


						objPaymentDto.setStrEmployeeId(user_id);
						objPaymentProcessDao.savePaymentApproveRejectTable(objPaymentDto, paymentId,PaymentEntryDetailsSeqID);

						//strResponse = "Success";
						//successList.add("PaymentDetailsId: "+String.valueOf(PaymentEntryDetailsSeqID)+" ,"+(StringUtils.isNotBlank(strInvoiceNo)?("InvoiceNumber: "+strInvoiceNo):("PONumber: "+poNo))+"  => Success");
						successList.add("Initiated Payment Request for Invoice No: "+(StringUtils.isNotBlank(strInvoiceNo)?strInvoiceNo:"-")+" with Payment Details Id: "+String.valueOf(PaymentEntryDetailsSeqID)+".");
						PaymentBean objPB = new PaymentBean();
						objPB.setIntPaymentDetailsId(PaymentEntryDetailsSeqID);
						objPB.setStrInvoiceNo(strInvoiceNo.equals("")?"-":strInvoiceNo);
						objPB.setStrPONo(poNo.equals("")?"-":poNo);
						objPB.setDoubleAmountToBeReleased(doubleAmountToBeReleased);
						objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
						objPB.setStrSiteName(site_name.equals("")?"-":site_name);
						objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
						objPB.setStrRemarks(strRemarks.equals("")?"-":strRemarks);
						objPB.setStrSiteId(strSiteId);
						objPB.setStrVendorId(strVendorId);
						objPB.setIntIndentEntryId(Integer.parseInt(indentEntryId));
						objPB.setStrInvoiceDate(strInvoiceDate);
						SuccessDataListToMail.add(objPB);

					}
				}
			}else{
				model.setViewName("payment/payment");
			}
			


		}catch(Exception e){
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			successList.add("Failed. Exception Occured.");
			e.printStackTrace();
		}
		final String user_id_final=user_id;
		final String user_name_final=user_name;
		final String strSiteId_final=strSiteId;
		final String session_siteId_final=session_siteId;
		final String pendingEmpId_final=pendingEmpId;
		final int getLocalPort = request.getLocalPort();
		if(SuccessDataListToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress = getPaymentApproverEmailId(user_id_final,session_siteId_final);
						objEmailFunction.sendPaymentApprovalMailToSiteSecondAndThirdLevel(SuccessDataListToMail,emailToAddress,getLocalPort,strSiteId_final,pendingEmpId_final,user_name_final,session_siteId_final);
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}

		return successList;
	}
	private String[] getPaymentApproverEmailId(String strUserId,String session_siteId) {
		return objPaymentProcessDao.getPaymentApproverEmailId(strUserId,session_siteId);

	}
	@Override
	public List<String> savePaymentIntiateDetailsOnPoAdvance( HttpServletRequest request,HttpSession session){
		//this list is for to send payments pending for next level(site 3rd level or Accounts 1st level).
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		ModelAndView model = null;
		String strInvoiceNo = "";
		String strVendorId = "";
		String strVendorName = "";
		String strPaymentReqDate = "";
		double doubleAmountToBeReleased = 0.0;
		String strRemarks = "";
		String strInvoiceDate = "";
		int paymentId = 0;
		int PaymentEntryDetailsSeqID = 0;
		double doubleInvoiceAmount = 0.0;
		double doublePOAmount = 0.0;
		String strSiteId = "";
		String dcNo = "";
		String poNo = "";
		String poEntryId = "";
		String pendingEmpId = "";
		String strPODate = "";
		String strInvoiceReceivedDate = "";
		String strDCDate = "";
		String strInvoiceNoInAP = "";
		String paymentType = "";
		String indentEntryId = "";
		double doubleCreditTotalAmount = 0.0;
		String strCreditNoteNumber = "";
		List<String> successList = new ArrayList<String>();
		String user_id="";
		String site_name ="";
		String user_name = "";
		String session_siteId = "";
		String trankey = "";
		try {
			
			site_name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();	
			user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			session_siteId = String.valueOf(session.getAttribute("SiteId"));
			user_id=String.valueOf(session.getAttribute("UserId"));
			pendingEmpId = objPaymentProcessDao.getPendingEmpId(user_id,session_siteId);
			
			model = new ModelAndView();
			int intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfRecords") == null ? "0" : request.getParameter("noOfRecords").toString());

			if(intTotalNoOfRecords > 0){
				PaymentDto  objPaymentDto = null;
				for(int i = 1;i<=intTotalNoOfRecords;i++){
					if(request.getParameter("checkboxname"+i)!=null){
						
						poEntryId = request.getParameter("poEntryId"+i);
						poNo = request.getParameter("poNo"+i);
						//checking is This PO Going To Be Canceled
						boolean isThisPOGoingToBeCanceled = objPurchaseDepartmentIndentProcessDao.isThisPOGoingToBeCanceled(poEntryId);
						if(isThisPOGoingToBeCanceled){
							successList.add("Unable to initiate payment on PO No:"+poNo+",as Cancel PO was in approvals.");
							continue;
						}
						
						if(StringUtils.isBlank(request.getParameter("AmountToBeReleased"+i))||StringUtils.isBlank(request.getParameter("PaymentreqDateId"+i)))
							{
								successList.add("Failed. You not entered the Amount (or) Req.Date.");
								continue;
							}

						objPaymentDto = new PaymentDto();




						paymentType = request.getParameter("paymentType"+i);
						doubleAmountToBeReleased = Double.valueOf(request.getParameter("AmountToBeReleased"+i) == null ? "0" : request.getParameter("AmountToBeReleased"+i));
						
						//checking   Is the total requesting amount exceeded the limit of PO amount.
						if(paymentType.equals("ADVANCE")){
							double poAmount = Double.valueOf(request.getParameter("poAmount"+i) == null ? "0" : request.getParameter("poAmount"+i));
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+doubleAmountToBeReleased)>poAmount){
								successList.add("Failed. "+"PO No: "+poNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount.");
								continue;
							}
						}


						strInvoiceDate = request.getParameter("invoiceDate"+i);
						strInvoiceNo = request.getParameter("invoiceNo"+i);
						strInvoiceNoInAP = request.getParameter("invoiceNoInAP"+i);
						strVendorId = request.getParameter("vendorId"+i);
						strVendorName =  request.getParameter("vendorName"+i);
						strPaymentReqDate = request.getParameter("PaymentreqDateId"+i);
						strRemarks = request.getParameter("remarks"+i);
						paymentId = Integer.parseInt(request.getParameter("paymentSeqId"+i) == null ? "0" : request.getParameter("paymentSeqId"+i));
						doubleInvoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i));
						strSiteId = request.getParameter("siteId"+i);
						dcNo = request.getParameter("dcNo"+i);

						doublePOAmount = Double.valueOf(request.getParameter("poAmount"+i));
						strPODate = request.getParameter("poDate"+i);
						strInvoiceReceivedDate = request.getParameter("invoiceReceivedDate"+i);
						strDCDate = request.getParameter("dcDate"+i);
						indentEntryId = request.getParameter("indentEntryId"+i)==null ? "0" : request.getParameter("indentEntryId"+i);
						doubleCreditTotalAmount = Double.valueOf(request.getParameter("creditTotalAmount"+i)==null ? "0" : request.getParameter("creditTotalAmount"+i));
						strCreditNoteNumber = request.getParameter("creditNoteNumber"+i)==null ? "0" : request.getParameter("creditNoteNumber"+i);
						
						WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+strInvoiceNo+" , PONumber:"+poNo);

						
						
						trankey = poEntryId+"|"+doubleAmountToBeReleased;
						DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						

						objPaymentDto.setDoubleAmountToBeReleased(doubleAmountToBeReleased);
						objPaymentDto.setStrInvoiceNo(strInvoiceNo);
						objPaymentDto.setStrInvoiceNoInAP(strInvoiceNoInAP);
						objPaymentDto.setStrVendorId(strVendorId);
						objPaymentDto.setStrPaymentReqDate(strPaymentReqDate);
						objPaymentDto.setStrInvoiceDate(strInvoiceDate);
						objPaymentDto.setStrRemarks(strRemarks);
						objPaymentDto.setDoubleInvoiceAmount(doubleInvoiceAmount);
						objPaymentDto.setStrSiteId(strSiteId);
						objPaymentDto.setStrDCNo(dcNo);
						objPaymentDto.setStrPONo(poNo);
						objPaymentDto.setStrPendingEmpId(pendingEmpId);
						objPaymentDto.setIntPaymentId(paymentId);
						objPaymentDto.setDoublePOAmount(doublePOAmount);
						objPaymentDto.setStrPODate(strPODate);
						objPaymentDto.setStrInvoiceReceivedDate(strInvoiceReceivedDate);
						objPaymentDto.setStrDCDate(strDCDate);
						int intSiteWisePaymentNo = 0;
						intSiteWisePaymentNo = objPaymentProcessDao.getSiteWisePaymentNo(strSiteId);
						objPaymentDto.setIntSiteWisePaymentId(intSiteWisePaymentNo);
						objPaymentDto.setPaymentType(paymentType);
						objPaymentDto.setIntIndentEntryId(Integer.valueOf(indentEntryId));
						objPaymentDto.setDoubleCreditTotalAmount(doubleCreditTotalAmount);
						objPaymentDto.setStrCreditNoteNumber(strCreditNoteNumber);
						
						
						//if paymentId is ZERO means there is no row in with this InvoiceNo or PONumber payment table.
						if(paymentId == 0){
							paymentId = objPaymentProcessDao.getPaymentId();
						}
						
						//if paymentId is NOT ZERO means already there is a row in payment table.
						if( objPaymentDto.getIntPaymentId() > 0){
							objPaymentProcessDao.updatePaymentTableOnPoAdvance(objPaymentDto);
						}
						else{
							objPaymentProcessDao.savePaymentTableOnPoAdvance(objPaymentDto, paymentId);
						}
						
						PaymentEntryDetailsSeqID = objPaymentProcessDao.getPaymentDetailsId();
						
						objPaymentProcessDao.savePaymentProcessDtlsTableOnPoAdvance(objPaymentDto, paymentId,PaymentEntryDetailsSeqID);



						objPaymentDto.setStrEmployeeId(user_id);
						objPaymentProcessDao.savePaymentApproveRejectTable(objPaymentDto, paymentId,PaymentEntryDetailsSeqID);

						//strResponse = "Success";
						//successList.add("PaymentDetailsId: "+String.valueOf(PaymentEntryDetailsSeqID)+" ,"+("PONumber: "+poNo)+"  => Success");
						successList.add("Initiated Payment Request for PO No: "+poNo+" with Payment Details Id: "+String.valueOf(PaymentEntryDetailsSeqID)+".");
						PaymentBean objPB = new PaymentBean();
						objPB.setIntPaymentDetailsId(PaymentEntryDetailsSeqID);
						objPB.setStrInvoiceNo(strInvoiceNo.equals("")?"-":strInvoiceNo);
						objPB.setStrPONo(poNo.equals("")?"-":poNo);
						objPB.setDoubleAmountToBeReleased(doubleAmountToBeReleased);
						objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
						objPB.setStrSiteName(site_name.equals("")?"-":site_name);
						objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
						objPB.setStrRemarks(strRemarks.equals("")?"-":strRemarks);
						objPB.setStrSiteId(strSiteId);
						objPB.setStrVendorId(strVendorId);
						objPB.setIntIndentEntryId(Integer.parseInt(indentEntryId));
						objPB.setStrInvoiceDate(strInvoiceDate);
						SuccessDataListToMail.add(objPB);

					}
				}
			}else{
				model.setViewName("payment/payment");
			}
			


		}catch(Exception e){
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			successList.add("Failed. Exception Occured.");
			e.printStackTrace();
		}
		final String user_id_final=user_id;
		final String user_name_final=user_name;
		final String strSiteId_final=strSiteId;
		final String session_siteId_final=session_siteId;
		final String pendingEmpId_final=pendingEmpId;
		final int getLocalPort = request.getLocalPort();
		if(SuccessDataListToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress = getPaymentApproverEmailId(user_id_final,session_siteId_final);
						objEmailFunction.sendPaymentApprovalMailToSiteSecondAndThirdLevel(SuccessDataListToMail,emailToAddress,getLocalPort,strSiteId_final,pendingEmpId_final,user_name_final,session_siteId_final);
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}

		return successList;
	}

	@Override
	public List<PaymentBean> getInvoiceDetails(String siteId,Map<String,String> reqParamsMap,String selectAll,String ispaymentReqDateChecked, boolean isItFirstRequest){

		List<PaymentBean> listPaymentBean =  null;
		try{
			listPaymentBean = objPaymentProcessDao.getInvoiceDetails( siteId, reqParamsMap, selectAll, ispaymentReqDateChecked, isItFirstRequest);
		}catch(Exception e){
			e.printStackTrace();
		}

		return listPaymentBean;
	}
	@Override
	public List<PaymentBean> getPODetails(String siteId,Map<String,String> reqParamsMap,String selectAll){

		List<PaymentBean> listPaymentBean =  null;
		try{
			listPaymentBean = objPaymentProcessDao.getPODetails(siteId, reqParamsMap,selectAll);
		}catch(Exception e){
			e.printStackTrace();
		}

		return listPaymentBean;
	}
	public List<String> savePaymentApprovalAndRejectDetails( HttpServletRequest request,HttpSession session,String strUserId){
		PaymentBean  objPaymentBean = null;
		//this list is for to send payments pending for next level(site 3rd level or Accounts 1st level).
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		//this map is for to send rejected payments To all lower site level Employee.
		final List<PaymentBean> RejectDataListToMail = new ArrayList<PaymentBean>();
		//final List<PaymentBean> SuccessDataListToMailMarketing = new ArrayList<PaymentBean>();
		final Set<String> marketingPos = new HashSet <String>();
		String actualRequestedAmount="";
		String actualRequestedDate="";
		String changedRequestedAmount="";
		String changedRequestedDate="";
		String comments="";
		//	String strStatus="";
		int intTotalNoOfRecords =0;
		String strPaymentIntiateType="";
		String paymentDetailsId="";
		String pendingEmpId="";
		String paymentId="";
		boolean isRowChanged = false;

		double invoiceAmount=0.0;
		String pendingDeptId="";
		String remarks="";
		String reqSiteId="";
		double doubleAdjustAmountFromAdvance=0.0;
		double actualDoubleAdjustAmountFromAdvance=0.0;
		String poNo="";
		String paymentType="";
		double poAmount=0.0;
		String invoiceNo="";
		String strVendorName="";
		String strVendorId="";
		String strInvoiceDate="";
		String indentEntryId="";
		List<String> successList = new ArrayList<String>();
		String trankey = "";
		String StrRoleId = "";
		String responseForTaxSubmit="";
		String rollIdForInvoiceTaxSubmit = "";
		String[] emailArrayListForMarketing;
		String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
		String site_name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();	
		String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
		String strMarketingId=UIProperties.validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : UIProperties.validateParams.getProperty("MARKETING_DEPT_ID").toString();
		
		try{
			rollIdForInvoiceTaxSubmit = UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit") == null ? "00" : UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit").toString();
			StrRoleId = String.valueOf(session.getAttribute("Roleid"));
			pendingEmpId=objPaymentProcessDao.getPendingEmpId(strUserId,site_id);


			intTotalNoOfRecords = Integer.parseInt(request.getParameter("listTotalInvoices") == null ? "0" : request.getParameter("listTotalInvoices").toString());
			//	if(accountsDeptId.equals(deptEmpId)){

			if(intTotalNoOfRecords > 0){
				for(int i = 1;i<=intTotalNoOfRecords;i++){
					strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					if(strPaymentIntiateType.equalsIgnoreCase("Approved")||strPaymentIntiateType.equalsIgnoreCase("Rejected")){
						objPaymentBean = new PaymentBean();
						changedRequestedAmount=request.getParameter("RequestedAmt"+i);

						changedRequestedDate=request.getParameter("RequestedDate"+i);
						comments=request.getParameter("comment"+i);
						paymentDetailsId=request.getParameter("paymentDetailsId"+i);
						paymentId=request.getParameter("PaymentId"+i);
						actualRequestedAmount=request.getParameter("actualRequestedAmt"+i);
						actualRequestedDate=request.getParameter("actualRequestedDate"+i);
						invoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i) == null ? "0" : request.getParameter("invoiceAmount"+i));
						poAmount = Double.valueOf(request.getParameter("poAmount"+i) == null ? "0" : request.getParameter("poAmount"+i));
						remarks=request.getParameter("remarks"+i);
						reqSiteId=request.getParameter("siteId"+i);
						doubleAdjustAmountFromAdvance=Double.valueOf(request.getParameter("AdjustAmountFromAdvance"+i));
						actualDoubleAdjustAmountFromAdvance=Double.valueOf(request.getParameter("actualAdjustAmountFromAdvance"+i));
						poNo = request.getParameter("poNo"+i);
						invoiceNo = request.getParameter("invoiceNo"+i);
						paymentType = request.getParameter("paymentType"+i);
						strVendorName =  request.getParameter("vendorName"+i);
						strVendorId =  request.getParameter("VendorId"+i);
						strInvoiceDate =  request.getParameter("invoiceDate"+i);
						indentEntryId = request.getParameter("indentEntryId"+i)==null ? "0" : request.getParameter("indentEntryId"+i);
						

						if(remarks !=null && !remarks.equals("")){
							if(StringUtils.isNotBlank(comments)){
								remarks = remarks + "@@@" + comments;
							}
						}else{
							remarks = comments;
						}

						if(paymentType.equals("DIRECT")){
							if(StrRoleId.equals(rollIdForInvoiceTaxSubmit)){
								try	{
									responseForTaxSubmit=objPaymentProcessDao.checkIsTaxInvoiceIsSubmitedOrNot(strVendorId,indentEntryId,invoiceNo,site_id);
									if(!responseForTaxSubmit.equals("SUBMITTED")){
										//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
										successList.add("Payment request is failed as tax invoice no: "+invoiceNo+" is not submitted");
										continue;
									}
								}catch(Exception e){
									e.printStackTrace();
									//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
									successList.add("Payment request is failed as tax invoice no: "+invoiceNo+" is not submitted");
									continue;
								}
							}
						}
						
						String remainingAmountInAdvanceInDB = objPaymentProcessDao.getRemainingAmountInAdvance(poNo);
						if((doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance)>Double.valueOf(remainingAmountInAdvanceInDB)){
							successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Adjust Amount is greater than Advance.");
							continue;
						}
						//if payment against invoice, checking   Is the total requesting amount exceeded the limit of Invoice amount.
						if(paymentType.equals("DIRECT")){
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(changedRequestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>invoiceAmount){
								successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than Invoice Amount.");
								continue;
							}
						}
						//if payment against po, checking   Is the total requesting amount exceeded the limit of PO amount.
						if(paymentType.equals("ADVANCE")){
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(changedRequestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>poAmount){
								successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount.");
								continue;
							}
						}
						
						//Do not pay more than PO amount
						String payBalanceInPo = request.getParameter("payBalanceInPo"+i);
						String paymentDoneOnMultipleInvoices = request.getParameter("paymentDoneOnMultipleInvoices"+i);
						String adjustedAmountFromPo = request.getParameter("adjustedAmountFromPo"+i);
						if(paymentType.equals("DIRECT")){
							if(!payBalanceInPo.equals("NO_ADVANCE")){
								//validating current requested amount to balance in PO
								if(Double.valueOf(changedRequestedAmount) > Double.valueOf(payBalanceInPo)){
									successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
									continue;
							 	}
								//validating current requested amount, balance in PO & total payment done on multiple invoices of PO
							 	if(Double.valueOf(changedRequestedAmount) > Double.valueOf(payBalanceInPo) - (Double.valueOf(paymentDoneOnMultipleInvoices) - Double.valueOf(adjustedAmountFromPo))){
							 		successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
									continue;
							 	}
							}
						}
						
						
						trankey = paymentDetailsId;
						DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						

						objPaymentBean.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));	
						objPaymentBean.setActualRequestedAmount(actualRequestedAmount);
						objPaymentBean.setActualRequestedDate(actualRequestedDate);
						objPaymentBean.setRequestedAmount(changedRequestedAmount);
						objPaymentBean.setRequestedDate(changedRequestedDate);
						objPaymentBean.setStrRemarks(comments);
						objPaymentBean.setStrEmployeeId(strUserId);
						objPaymentBean.setStrSiteId(reqSiteId);
						objPaymentBean.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);
						objPaymentBean.setActualDoubleAdjustAmountFromAdvance(actualDoubleAdjustAmountFromAdvance);


						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat requestDateFormat = new SimpleDateFormat("dd/MM/yyyy");

						java.util.Date d1 = null;
						java.util.Date d2 = null;

						d1 = format.parse(actualRequestedDate);
						d2 = requestDateFormat.parse(changedRequestedDate);

						//in milliseconds
						long diff = d2.getTime() - d1.getTime();
						long diffDays = diff / (24 * 60 * 60 * 1000);

						java.sql.Timestamp  timestamp = null;
						try {
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
							java.util.Date parsedDate = dateFormat.parse(changedRequestedDate);
							timestamp = new java.sql.Timestamp(parsedDate.getTime());
							//System.out.println(timestamp);
						} catch(Exception e) { 
							e.printStackTrace();
						}

						if(StringUtils.isNotBlank(comments)){
							objPaymentProcessDao.UpDatePaymentDetailsRemarks(paymentDetailsId,remarks);
						}

						if(diffDays != 0){

							isRowChanged=true;	 
							objPaymentProcessDao.updatePaymentDetailsDate(paymentDetailsId,changedRequestedDate);


						} 


						if(strPaymentIntiateType.equalsIgnoreCase("Approved")){
							//if pending employee is '-' then it goes for Accounts Dept(HYD or BANGLORE).
							//this if block code determines, to which Accounts dept it goes.
							if(StringUtils.isNotBlank(pendingEmpId) && pendingEmpId.equals("-")){
								/*String address=objPaymentProcessDao.getSiteAddressByUsingSiteId(reqSiteId);
								//if site address belongs to HYD Then it goes to the HYD Accounts Dept.
								//if site address belongs to BANG Then it goes to the BANG Accounts Dept.
								if(address.contains("HYD")){
									pendingDeptId = "997_H_1";
								}
								else if(address.equalsIgnoreCase("BANGLORE")){
									pendingDeptId = "997_B_1";
								}*/
								
								//above line commented on 8-AUG-2019 & this below line alternative for above commented code.
								//reason: account department decided based on approval mapping details. not based on requested site address.
								pendingDeptId=objPaymentProcessDao.getPendingDeptId(strUserId,site_id);
								
							}
							//in this if block code, just updating table with Approver Employee Id.
							if(StringUtils.isNotBlank(pendingEmpId) && !pendingEmpId.equals("-")){
								
								objPaymentProcessDao.updateRequestedPaymentEmpId(paymentDetailsId,pendingEmpId);	 


							} 
							//in this if block code, Payment goes to the Accounts Dept.
							else if(StringUtils.isNotBlank(pendingDeptId) && !pendingDeptId.equals("-")){


								objPaymentProcessDao.updateRequestedPaymentDeptId(paymentDetailsId,pendingDeptId);	 
								objPaymentProcessDao.saveAccountsDeptTable(objPaymentBean);

							}
							if(!actualRequestedAmount.equals(changedRequestedAmount))
							{
								isRowChanged=true;
								objPaymentProcessDao.UpDatePaymentDetails(paymentDetailsId,Double.valueOf(changedRequestedAmount));




							}
							if(doubleAdjustAmountFromAdvance!=actualDoubleAdjustAmountFromAdvance){
								isRowChanged=true;
								objPaymentProcessDao.updatePaymentDetailsAdjustAmount(paymentDetailsId,doubleAdjustAmountFromAdvance);
								//updating intiated advance amount in AdvancePaymentPO
								objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPO(poNo,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);
							}
							//if ReqAmount or AdjustedAmount changed, then PaymentReqUpto has to update with new values in ACC_PAYMENT table.
							if((!actualRequestedAmount.equals(changedRequestedAmount))||(doubleAdjustAmountFromAdvance!=actualDoubleAdjustAmountFromAdvance)){

								objPaymentProcessDao.updateReqUptoInAccPayment(paymentId,actualRequestedAmount,changedRequestedAmount,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);

							}

							objPaymentProcessDao.saveApprovalDetails(paymentId,strUserId,comments,paymentDetailsId,site_id);
							if(isRowChanged){

								objPaymentProcessDao.saveChangedDetails(objPaymentBean,"M"); 


							}

							//this object stores Approve details for Mail purpose.
							PaymentBean objPB = new PaymentBean();
							objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
							objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
							objPB.setStrPONo(poNo.equals("")?"-":poNo);
							objPB.setDoubleAmountToBeReleased(Double.valueOf(changedRequestedAmount));
							objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
							objPB.setStrSiteName(site_name.equals("")?"-":site_name);
							objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
							objPB.setStrRemarks(comments.equals("")?"-":comments);
							objPB.setStrSiteId(reqSiteId);
							objPB.setStrVendorId(strVendorId);
							objPB.setIntIndentEntryId(Integer.parseInt(indentEntryId));
							objPB.setStrInvoiceDate(strInvoiceDate);
							SuccessDataListToMail.add(objPB);
							marketingPos.add(poNo);
							successList.add("Approved Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".");
						}//If-approve
						else if(strPaymentIntiateType.equalsIgnoreCase("Rejected")){

							objPaymentProcessDao.saveRejectDetails(paymentId,strUserId,comments,paymentDetailsId,site_id);

							//minus actual values in PaymentReqUpto in ACC_PAYMENT table
							objPaymentProcessDao.updateReqUptoInAccPaymentOnReject(paymentId,actualRequestedAmount,actualDoubleAdjustAmountFromAdvance);

							objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPOOnReject(poNo,actualDoubleAdjustAmountFromAdvance);
							
							//this object stores Reject details for Mail purpose.
							PaymentBean objPB = new PaymentBean();
							objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
							objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
							objPB.setStrPONo(poNo.equals("")?"-":poNo);
							objPB.setDoubleAmountToBeReleased(Double.valueOf(changedRequestedAmount));
							objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
							objPB.setStrSiteName(site_name.equals("")?"-":site_name);
							objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
							objPB.setStrRemarks(comments.equals("")?"-":comments);
							RejectDataListToMail.add(objPB);
								
							successList.add("Rejected Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".");
							
						}
						//successList.add("PaymentDetailsId: "+String.valueOf(paymentDetailsId)+" ,"+(StringUtils.isNotBlank(invoiceNo)?("InvoiceNumber: "+invoiceNo):("PONumber: "+poNo))+"  => Success");
					}//if-approve OR reject
				}//for


			}

			/*	}else{

			int intResponse=0;
			for(int i = 1;i<=intTotalNoOfRecords;i++){

			paymentDetailsId=request.getParameter("paymentDetailsId"+i);

			intResponse=objPaymentProcessDao.updateRequestedPaymentEmpId(paymentDetailsId,pendingEmpId);

			}

			if(intResponse>0){
				response="success";
			}

		}*/
			
			// ravi written this code for marketing executives getting the mails
			
			
			
		}catch(Exception e){
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			successList.add("Failed. Exception Occured.");
			e.printStackTrace();
		}
		final String user_id_final=strUserId;
		final String user_name_final=user_name;
		final String strSiteId_final=reqSiteId;
		final String session_siteId_final=site_id;
		final String pendingEmpId_final=pendingEmpId;
		final String pendingDeptId_final=pendingDeptId;
		final int getLocalPort = request.getLocalPort();
		final String strMarketingIds=strMarketingId;
		final String poNumber=poNo;
	
		if(SuccessDataListToMail.size()!=0 && StringUtils.isNotBlank(pendingEmpId) && !pendingEmpId.equals("-")){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress = getPaymentApproverEmailId(user_id_final,session_siteId_final);
						objEmailFunction.sendPaymentApprovalMailToSiteSecondAndThirdLevel(SuccessDataListToMail,emailToAddress,getLocalPort,strSiteId_final,pendingEmpId_final,user_name_final,session_siteId_final);
						
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		else if(SuccessDataListToMail.size()!=0 && StringUtils.isNotBlank(pendingEmpId) && pendingEmpId.equals("-")){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress = getEmailsOfEmpByDeptId(pendingDeptId_final);
						// for marketing executives gtting mails i.e after approve final level in marketingSite
						if(strSiteId_final.equals(strMarketingIds) && marketingPos.size()>0 && marketingPos!=null){
							List<String> list=objPaymentProcessDao.getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevelForMarketing(marketingPos,strMarketingIds);
						if(list!=null && list.size()>0){
							list.addAll(java.util.Arrays.asList(emailToAddress));
							emailToAddress = new String[list.size()];
							list.toArray(emailToAddress);
						}
						}
						objEmailFunction.sendPaymentApprovalMailToAccountsAllLevels(SuccessDataListToMail,emailToAddress,getLocalPort,user_name_final);
						
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		if(RejectDataListToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						
						
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress = getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(user_id_final,session_siteId_final);
						String EmailBodyMessage = "This payment(s) Rejected in Site Level";
						// for marketing purpose written this one
						if(strSiteId_final.equals(strMarketingIds) && marketingPos.size()>0 && marketingPos!=null){
							List<String> list=objPaymentProcessDao.getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevelForMarketing(marketingPos,strMarketingIds);
						if(list!=null && list.size()>0){
							list.addAll(java.util.Arrays.asList(emailToAddress));
							emailToAddress = new String[list.size()];
							list.toArray(emailToAddress);
						}
						}
						objEmailFunction.sendPaymentRejectedStatusMail(RejectDataListToMail, emailToAddress,getLocalPort,user_name_final,EmailBodyMessage);
						
					}

				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		return successList;


	}
	

	private String[] getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(String user_id_final,String session_siteId) {
		return objPaymentProcessDao.getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(user_id_final,session_siteId);
	}
	protected String[] getEmailsOfEmpByDeptId(String pendingDeptId) {
		return objPaymentProcessDao.getEmailsOfEmpByDeptId(pendingDeptId);
	}
	@Override
	public List<PaymentBean> getAccDeptPaymentPendingDetails(String strUserId,HttpServletRequest request,ModelAndView model) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		//String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
		
		String fromDate = "";
		String toDate = "";
		String vendorId = "";
		String vendorName = "";
		String invoiceNumber = "";
		String poNumber = "";
		String dropdown_SiteId = "";
		
		String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
		if(!selectAll.equals("true")){
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			vendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			if(StringUtils.isBlank(vendorName)){vendorId="";}
			invoiceNumber = request.getParameter("invoiceNumber");
			poNumber = request.getParameter("poNumber");
			dropdown_SiteId = request.getParameter("dropdown_SiteId");
		}
		
		model.addObject("fromDate",fromDate);
		model.addObject("toDate", toDate);
		model.addObject("vendorId",vendorId);
		model.addObject("vendorName",vendorName);
		model.addObject("invoiceNumber",invoiceNumber);
		model.addObject("poNumber", poNumber);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("fromDate",fromDate);
		map.put("toDate", toDate);
		map.put("vendorId",vendorId);
		map.put("vendorName",vendorName);
		map.put("invoiceNumber",invoiceNumber);
		map.put("poNumber", poNumber);
		map.put("dropdown_SiteId", dropdown_SiteId);
		
		if(selectAll.equals("true")||CommonUtilities.returnTrueIfAnyValueInMapIsNotBlank(map)){
			list = objPaymentProcessDao.getAccDeptPaymentPendingDetails(strUserId,map);
		}
		else{
			model.addObject("firstRequest", true);
		}
		return list;
	}
	@Override
	public List<PaymentBean> getAccDeptPaymentApprovalDetails(String site_id, String user_id, HttpServletRequest request, ModelAndView model) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		//String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
		
		String fromDate = "";
		String toDate = "";
		String vendorId = "";
		String vendorName = "";
		String invoiceNumber = "";
		String poNumber = "";
		String dropdown_SiteId = "";
		
		String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
		if(!selectAll.equals("true")){
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			vendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			if(StringUtils.isBlank(vendorName)){vendorId="";}
			invoiceNumber = request.getParameter("invoiceNumber");
			poNumber = request.getParameter("poNumber");
			dropdown_SiteId = request.getParameter("dropdown_SiteId");
		}
		
		model.addObject("fromDate",fromDate);
		model.addObject("toDate", toDate);
		model.addObject("vendorId",vendorId);
		model.addObject("vendorName",vendorName);
		model.addObject("invoiceNumber",invoiceNumber);
		model.addObject("poNumber", poNumber);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("fromDate",fromDate);
		map.put("toDate", toDate);
		map.put("vendorId",vendorId);
		map.put("vendorName",vendorName);
		map.put("invoiceNumber",invoiceNumber);
		map.put("poNumber", poNumber);
		map.put("dropdown_SiteId", dropdown_SiteId);
		
		if(selectAll.equals("true")||CommonUtilities.returnTrueIfAnyValueInMapIsNotBlank(map)){
			list = objPaymentProcessDao.getAccDeptPaymentApprovalDetails(site_id,user_id,map);
		}
		else{
			model.addObject("firstRequest", true);
		}
		return list;
	}
	@Override
	public List<String> createAccountDeptTransaction(HttpServletRequest request , String strEmpId, String user_name, HttpSession session) {

		//this list is for to send payments pending for Accounts 2nd level.
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		//this map is for to send rejected payments in Accounts 1st level To All site level Employees.
		final Map<String,List<PaymentBean>> PaymentRejectMapToMail = new HashMap<String,List<PaymentBean>>();
		//this map is for to send status of Approved payments in Accounts 1st level To each requestRecieveFrom. 
		final Map<String,List<PaymentBean>> SendApproveStatusToRequestRecieveFromMapToMail = new HashMap<String,List<PaymentBean>>();
		PaymentDto objPaymentDto = null;
		String requestedAmount="";
		String requestedDate="";
		int intTotalNoOfRecords =0;
		String paymentDetailsId="";
		String remarks="";
		int intAccDeptPaymentProcessId = 0;
		String strTransactionRefrenceno = "";
		String strSiteWisePaymentId = "";
		String paymentType = "";
		String poNo="";
		String invoiceNo="";
		double doubleAdjustAmountFromAdvance = 0.0;
		double actualDoubleAdjustAmountFromAdvance = 0.0;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		String actualRequestedAmount="";
		String paymentId="";
		String siteId="";
		String siteName="";
		String requestReceiveFrom = "";
		String comments = "";
		String strVendorName = "";
		String vendorId="";
		String indentEntryId="";
		List<String> successList = new ArrayList<String>();
		String strPendingEmpId = "";
		String strPendingDeptId = "";
		String trankey = "";
		String StrRoleId = "";
		String responseForTaxSubmit="";
		String rollIdForInvoiceTaxSubmit = "";

		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			rollIdForInvoiceTaxSubmit = UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit") == null ? "00" : UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit").toString();
			StrRoleId = String.valueOf(session.getAttribute("Roleid"));	
			if(intTotalNoOfRecords > 0){


				strPendingEmpId = objPaymentProcessDao.getApproverEmpIdInAccounts(strEmpId);	
				strPendingDeptId = objPaymentProcessDao.getApproverDeptIdInAccounts(strEmpId);	
				

				for(int i = 1;i<=intTotalNoOfRecords;i++){
					paymentType = request.getParameter("paymentType"+i);
					paymentDetailsId = request.getParameter("paymentDetailsId"+i);
					requestedDate = request.getParameter("paymentRequestDate"+i);
					requestedAmount = request.getParameter("requestAmount"+i);
					intAccDeptPaymentProcessId = Integer.parseInt(request.getParameter("accDeptPmtProcessId"+i));
					remarks = request.getParameter("remarks"+i);
					comments = request.getParameter("comments"+i);
					strTransactionRefrenceno = request.getParameter("utrOrChqNo"+i);
					strSiteWisePaymentId = request.getParameter("siteWisePaymetId"+i);
					doubleAdjustAmountFromAdvance = Double.valueOf(request.getParameter("AdjustAmountFromAdvance"+i));
					actualDoubleAdjustAmountFromAdvance=Double.valueOf(request.getParameter("actualAdjustAmountFromAdvance"+i));
					poNo = request.getParameter("poNo"+i);
					invoiceNo = request.getParameter("invoiceNo"+i);
					invoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i) == null ? "0" : request.getParameter("invoiceAmount"+i));
					poAmount = Double.valueOf(request.getParameter("poAmount"+i) == null ? "0" : request.getParameter("poAmount"+i));
					actualRequestedAmount=request.getParameter("actualRequestedAmt"+i);
					paymentId = request.getParameter("paymentSeqId"+i) == null ? "0" : request.getParameter("paymentSeqId"+i);
					siteId = request.getParameter("siteId"+i);
					siteName = request.getParameter("siteName"+i);
					requestReceiveFrom = request.getParameter("requestReceiveFrom"+i);
					strVendorName = request.getParameter("vendorName"+i);
					vendorId = request.getParameter("vendorId"+i);
					indentEntryId=request.getParameter("indentEntryId"+i);
					
					String strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					if(strPaymentIntiateType.equalsIgnoreCase("Approved")){

						String paymentMode = request.getParameter("paymentMode"+i);

						objPaymentDto = new PaymentDto();

						

						if(remarks !=null && !remarks.equals("")){
							if(StringUtils.isNotBlank(comments)){
								remarks = remarks + "@@@" + comments;
							}
						}else{
							remarks = comments;
						}

					 			
						if(paymentType.equals("DIRECT")){
							if(StrRoleId.equals(rollIdForInvoiceTaxSubmit)){
								try	{
									responseForTaxSubmit=objPaymentProcessDao.checkIsTaxInvoiceIsSubmitedOrNot(vendorId,indentEntryId,invoiceNo,siteId);
									if(!responseForTaxSubmit.equals("SUBMITTED")){
										//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
										successList.add("Payment request is failed as tax invoice no: "+invoiceNo+" is not submitted");
										continue;
									}
								}catch(Exception e){
									e.printStackTrace();
									//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
									successList.add("Payment request is failed as tax invoice no: "+invoiceNo+" is not submitted");
									continue;
								}
							}
						}
						
						//AccDept 1st level can Adjust more amount from Po Advance. but cant less the amount. it is complex to manage from back end.
						if(doubleAdjustAmountFromAdvance<actualDoubleAdjustAmountFromAdvance){
							doubleAdjustAmountFromAdvance = actualDoubleAdjustAmountFromAdvance;
						}
						
						//checking if Adjust Amount is greater than Po Advance or not.
						String remainingAmountInAdvanceInDB = objPaymentProcessDao.getRemainingAmountInAdvance(poNo);
						if((doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance)>Double.valueOf(remainingAmountInAdvanceInDB)){
							successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Adjust Amount is greater than Advance.");
							continue;
						}
						//if payment against invoice, checking   Is the total requesting amount exceeded the limit of Invoice amount.
						if(paymentType.equals("DIRECT")){
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(requestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>invoiceAmount){
								successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than Invoice Amount.");
								continue;
							}
						}
						//if payment against po, checking   Is the total requesting amount exceeded the limit of PO amount.
						if(paymentType.equals("ADVANCE")){
							double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
							double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(requestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>poAmount){
								successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount.");
								continue;
							}
						}
						
						


					
						
						
						
						
						//Do not pay more than PO amount
						String payBalanceInPo = request.getParameter("payBalanceInPo"+i);
						String paymentDoneOnMultipleInvoices = request.getParameter("paymentDoneOnMultipleInvoices"+i);
						String adjustedAmountFromPo = request.getParameter("adjustedAmountFromPo"+i);
						if(paymentType.equals("DIRECT")){
							if(!payBalanceInPo.equals("NO_ADVANCE")){
								//validating current requested amount to balance in PO
								if(Double.valueOf(requestedAmount) > Double.valueOf(payBalanceInPo)){
									successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
									continue;
							 	}
								//validating current requested amount, balance in PO & total payment done on multiple invoices of PO
							 	if(Double.valueOf(requestedAmount) > Double.valueOf(payBalanceInPo) - (Double.valueOf(paymentDoneOnMultipleInvoices) - Double.valueOf(adjustedAmountFromPo))){
							 		successList.add("Failed. "+"PaymentDetailsId: "+String.valueOf(paymentDetailsId)+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
									continue;
							 	}
							}
						}

						
						trankey = paymentDetailsId+"|"+requestedAmount;
						DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						
						objPaymentDto.setIntAccDeptPaymentProcessId(intAccDeptPaymentProcessId);
						objPaymentDto.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
						objPaymentDto.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
						objPaymentDto.setStrPendingDeptId(strPendingDeptId);
						objPaymentDto.setStrPaymentReqDate(requestedDate);
						objPaymentDto.setStrRefrenceNo(strTransactionRefrenceno);
						objPaymentDto.setStrRemarks(remarks);
						objPaymentDto.setIntSiteWisePaymentId(Integer.parseInt(strSiteWisePaymentId));
						objPaymentDto.setPaymentMode(paymentMode);
						objPaymentDto.setPaymentType(paymentType);
						objPaymentDto.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);
						objPaymentDto.setStrEmployeeId(strEmpId);
						objPaymentDto.setStrSiteId(siteId);


						if(StringUtils.isNotBlank(strPendingDeptId) && !strPendingEmpId.equals("VND")){


							int TempPaymentId = objPaymentProcessDao.getAccTempPaymentTransactionSeqNo();
							objPaymentProcessDao.insertTempPaymentTransactionsTbl(objPaymentDto,TempPaymentId);



							objPaymentProcessDao.updateAccDeptPaymentProcsstbl(objPaymentDto);

							objPaymentProcessDao.saveAccountsApproveRejectTable(objPaymentDto,intAccDeptPaymentProcessId,TempPaymentId);

							//objPaymentProcessDao.saveAccountApprovalDetails(intAccDeptPaymentProcessId,strEmpId,comments,0,siteId);
							if(doubleAdjustAmountFromAdvance!=actualDoubleAdjustAmountFromAdvance){
								//updating initiated advance amount in AdvancePaymentPO table.
								objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPO(poNo,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);
							
								PaymentBean objPaymentBean = new PaymentBean();
								objPaymentBean.setStrEmployeeId(strEmpId);
								objPaymentBean.setActualDoubleAdjustAmountFromAdvance(actualDoubleAdjustAmountFromAdvance);
								objPaymentBean.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);
								objPaymentBean.setPaymentMode(paymentMode);
								objPaymentProcessDao.saveAccountChangedDetails(objPaymentBean,"M"); 


							}

						}

						//successList.add("PaymentDetailsId: "+String.valueOf(paymentDetailsId)+" ,"+(StringUtils.isNotBlank(invoiceNo)?("InvoiceNumber: "+invoiceNo):("PONumber: "+poNo))+" Approved Successfully.");
						successList.add("Approved Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".");
						
						//this object stores Approve details for Mail purpose.
						PaymentBean objPB = new PaymentBean();
						objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
						objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
						objPB.setStrPONo(poNo.equals("")?"-":poNo);
						objPB.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
						objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
						objPB.setStrSiteName(siteName.equals("")?"-":siteName);
						objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
						objPB.setStrRemarks(comments.equals("")?"-":comments);
						SuccessDataListToMail.add(objPB);
						//requestReceiveFrom will be different for each site.
						//here storing each requestReceiveFrom(as key) & its payment status list(as value) in a map.
						if(SendApproveStatusToRequestRecieveFromMapToMail.containsKey(requestReceiveFrom)){
							SendApproveStatusToRequestRecieveFromMapToMail.get(requestReceiveFrom).add(objPB);
						}
						else{
							List<PaymentBean> listForParticularRequestReceiveFrom = new ArrayList<PaymentBean>();
							listForParticularRequestReceiveFrom.add(objPB);
							SendApproveStatusToRequestRecieveFromMapToMail.put(requestReceiveFrom,listForParticularRequestReceiveFrom);
						}
					
					}//if-approve
					else if(strPaymentIntiateType.equalsIgnoreCase("Rejected")){
						
						trankey = paymentDetailsId+"|"+requestedAmount;
						DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						
						objPaymentProcessDao.setInactiveRowInAccDeptPmtProcessTbl(intAccDeptPaymentProcessId);
						objPaymentProcessDao.UpDatePaymentDetailsRemarksAndSetInactive(paymentDetailsId,comments);
						objPaymentProcessDao.saveAccountRejectDetails(intAccDeptPaymentProcessId,strEmpId,comments,0,siteId);

						//minus actual values in PAYMENT_REQ_UPTO
						objPaymentProcessDao.updateReqUptoInAccPaymentOnReject(paymentId,actualRequestedAmount,actualDoubleAdjustAmountFromAdvance);
						//minus actual values in ACC_ADVANCE_PAYMENT_PO
						objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPOOnReject(poNo,actualDoubleAdjustAmountFromAdvance);


					
						//successList.add("PaymentDetailsId: "+String.valueOf(paymentDetailsId)+" ,"+(StringUtils.isNotBlank(invoiceNo)?("InvoiceNumber: "+invoiceNo):("PONumber: "+poNo))+" Rejected Successfully.");
						successList.add("Rejected Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".");
						
						//this object stores Reject details for Mail purpose.
						PaymentBean objPB = new PaymentBean();
						objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
						objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
						objPB.setStrPONo(poNo.equals("")?"-":poNo);
						objPB.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
						objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
						objPB.setStrSiteName(siteName.equals("")?"-":siteName);
						objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
						objPB.setStrRemarks(comments.equals("")?"-":comments);
						if(PaymentRejectMapToMail.containsKey(requestReceiveFrom+"@@"+siteId)){
							PaymentRejectMapToMail.get(requestReceiveFrom+"@@"+siteId).add(objPB);
						}
						else{
							List<PaymentBean> list = new ArrayList<PaymentBean>();
							list.add(objPB);
							PaymentRejectMapToMail.put(requestReceiveFrom+"@@"+siteId,list);
						}
						
					}
					
				}//for loop end

			}
			
		} catch (Exception e) {
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			successList.add("Failed.");
			e.printStackTrace();
		}
		final String user_name_final=user_name;
		final String pendingDeptId_final=strPendingDeptId;
		final int getLocalPort = request.getLocalPort();
		//final String requestReceiveFrom_final = requestReceiveFrom;
		if(SuccessDataListToMail.size()!=0 && StringUtils.isNotBlank(strPendingDeptId)){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						//sending mail for Approval to next level
						String [] emailToAddress = getEmailsOfEmpByDeptId(pendingDeptId_final);
						objEmailFunction.sendPaymentApprovalMailToAccountsAllLevels(SuccessDataListToMail,emailToAddress,getLocalPort,user_name_final);
						//sending status mail to lower level 
						for(Map.Entry<String, List<PaymentBean>> entry : SendApproveStatusToRequestRecieveFromMapToMail.entrySet()){
							String [] emailToAddress1 = getEmailsOfEmpByEmpId(entry.getKey());
							objEmailFunction.sendPaymentApprovalStatusMailToLowerEmployee(entry.getValue(),emailToAddress1,getLocalPort,user_name_final);
						}
					}

					
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		if(PaymentRejectMapToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						for(Map.Entry<String, List<PaymentBean>> entry : PaymentRejectMapToMail.entrySet()){
							String [] emailToAddress = getEmailsOfEmpByEmpIdAndEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(entry.getKey());
							String EmailBodyMessage = "Account Department Rejected this payment(s) ";
							objEmailFunction.sendPaymentRejectedStatusMail(entry.getValue(), emailToAddress, getLocalPort,user_name_final,EmailBodyMessage);
						}
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		//strResponse = objPaymentProcessDao.getAccDeptPaymentPendingDetails(steEmpId);
		return successList;
	}
	public String[] getEmailsOfEmpByEmpIdAndEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(String requestReceiveFrom_siteId) {
		String user_id = requestReceiveFrom_siteId.split("@@")[0];
		String siteId = requestReceiveFrom_siteId.split("@@")[1];
		return objPaymentProcessDao.getEmailsOfEmpByEmpIdAndEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(user_id,siteId);
	}
	private String[] getAllSiteLevelEmails(String siteId) {
		return objPaymentProcessDao.getAllSiteLevelEmails(siteId);
	}
	private String[] getEmailsOfEmpByEmpId(String requestReceiveFrom) {
		return objPaymentProcessDao.getEmailsOfEmpByEmpId(requestReceiveFrom);
	}

	@Override
	public List<PaymentBean> updateAccountDeptTransaction(HttpServletRequest request, HttpServletResponse response , String strEmpId,String user_name,HttpSession session) {

		//this list is for to send payments pending for next Account Dept level.
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		final List<PaymentBean> SuccessDataListToMailMarketing = new ArrayList<PaymentBean>();
		final Set<String> marketingPos = new HashSet <String>();
		//this map is for to send rejected payments in This Account level To its Lower Account Dept level Employees.
		final List<PaymentBean> PaymentRejectListToMail = new ArrayList<PaymentBean>();
		List<PaymentBean> paymentDetailsList = new ArrayList<PaymentBean>();
		int intTempPaymentTransactionId=0;
		int intPaymentDetailsId=0;
		int intPaymentId=0;
		String paymentType="";
		String requestedDate="";
		String utrChequeNo="";
		String remarks="";
		int intSiteWisePaymentId=0;
		PaymentDto objPaymentDto = null;
		PaymentBean paymentBean = null;
		int intTotalNoOfRecords = 0;
		String requestedAmount="";
		String invoiceNo="";
		String poNo="";
		double doublePOAmount=0.0;
		double doubleAdjustAmountFromAdvance=0.0;
		double actualDoubleAdjustAmountFromAdvance=0.0;
		double doubleInvoiceAmount=0.0;
		int intAccDeptPaymentProcessId=0;
		String siteId = "";
		String actualUtrChequeNo="";
		String actualPaymentMode="";
		String invoiceDate = "";
		String invoiceReceiveDate = "";
		String poDate = "";
		String creditNoteNumber = "";
		String creditTotalAmount = "";
		String vendorName = "";
		String vendorId = "";
		int intSerialNo = 0;
		Double doublePaidAmount = 0.0;
		String strPaymentRequestReceivedDate = "";
		String siteName="";
		String comments = "";
		boolean isDownload = false;
		Map<String,String> requestedDateMap=new HashMap<String,String>();
		String strPendingEmpId = "";
		String strPendingDeptId = "";
		String trankey ="";
		String indentEntryId="";
		String StrRoleId = "";
		String responseForTaxSubmit="";
		String rollIdForInvoiceTaxSubmit = "";
		String strMarketingSiteId="";
		String strMarketingId=UIProperties.validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : UIProperties.validateParams.getProperty("MARKETING_DEPT_ID").toString();
 		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			rollIdForInvoiceTaxSubmit = UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit") == null ? "00" : UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit").toString();
			StrRoleId = String.valueOf(session.getAttribute("Roleid"));	
			if(intTotalNoOfRecords > 0){


				strPendingEmpId = objPaymentProcessDao.getApproverEmpIdInAccounts(strEmpId);	
				strPendingDeptId = objPaymentProcessDao.getApproverDeptIdInAccounts(strEmpId);

				for(int i = 1;i<=intTotalNoOfRecords;i++){

					String strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					intTempPaymentTransactionId=Integer.parseInt(request.getParameter("tempPaymentTransactionId"+i));
					intAccDeptPaymentProcessId=Integer.parseInt(request.getParameter("accDeptPmtProcessId"+i));
					intPaymentDetailsId=Integer.parseInt(request.getParameter("paymentDetailsId"+i));
					intPaymentId=Integer.parseInt(request.getParameter("paymentId"+i));
					paymentType=request.getParameter("paymentType"+i);
					requestedDate=request.getParameter("requestedDate"+i);
					utrChequeNo=request.getParameter("utrChequeNo"+i);
					actualUtrChequeNo=request.getParameter("actualUtrChequeNo"+i);
					actualPaymentMode=request.getParameter("actualPaymentMode"+i);
					remarks=request.getParameter("remarks"+i);
					comments=request.getParameter("comments"+i);
					intSiteWisePaymentId=Integer.parseInt(request.getParameter("siteWisePaymentId"+i));
					requestedAmount = request.getParameter("requestAmount"+i);
					poNo = request.getParameter("poNo"+i);
					invoiceNo = request.getParameter("invoiceNo"+i);
					doubleAdjustAmountFromAdvance = Double.valueOf(request.getParameter("AdjustAmountFromAdvance"+i));
					actualDoubleAdjustAmountFromAdvance=Double.valueOf(request.getParameter("actualAdjustAmountFromAdvance"+i));
					doubleInvoiceAmount = Double.valueOf(request.getParameter("invoiceAmount"+i) == null ? "0" : request.getParameter("invoiceAmount"+i));
					doublePOAmount = Double.valueOf(request.getParameter("poAmount"+i) == null ? "0" : request.getParameter("poAmount"+i));
					siteId = request.getParameter("siteId"+i);
					siteName = request.getParameter("siteName"+i);
					invoiceDate=request.getParameter("invoiceDate"+i);
					invoiceReceiveDate=request.getParameter("invoiceReceiveDate"+i);
					poDate=request.getParameter("poDate"+i);
					creditNoteNumber=request.getParameter("creditNoteNumber"+i);
					creditTotalAmount=request.getParameter("creditTotalAmount"+i);
					vendorName=request.getParameter("vendorName"+i);
					vendorId=request.getParameter("vendorId"+i);
					indentEntryId=request.getParameter("indentEntryId"+i);
					strPaymentRequestReceivedDate=request.getParameter("paymentRequestReceivedDate"+i);
					doublePaidAmount = Double.valueOf(request.getParameter("paymentDoneUpto"+i));

					if(remarks !=null && !remarks.equals("")){
						if(StringUtils.isNotBlank(comments)){
							remarks = remarks + "@@@" + comments;
						}
					}else{
						remarks = comments;
					}
					
					//this object used to print values in table in success page.
					paymentBean = new PaymentBean();
					paymentBean.setStrPONo(poNo);
					paymentBean.setStrInvoiceNo(invoiceNo);
					paymentBean.setDoublePOTotalAmount(doublePOAmount);
					paymentBean.setDoubleInvoiceAmount(doubleInvoiceAmount);
					paymentBean.setStrInvoiceAmount(String.format("%.2f",doubleInvoiceAmount));
					paymentBean.setStrPOAmount(String.format("%.2f",doublePOAmount));
					paymentBean.setStrInvoiceDate(invoiceDate);
					paymentBean.setStrInvoiceReceivedDate(invoiceReceiveDate);
					paymentBean.setStrPODate(poDate);
					paymentBean.setStrCreditNoteNumber(creditNoteNumber);
					paymentBean.setDoubleCreditTotalAmount(Double.valueOf(creditTotalAmount));
					paymentBean.setStrRemarks(comments);
					paymentBean.setStrVendorName(vendorName);
					paymentBean.setStatus(strPaymentIntiateType);
					paymentBean.setStrPaymentRequestReceivedDate(strPaymentRequestReceivedDate);
					paymentBean.setRequestedAmount(requestedAmount);
					paymentBean.setStrSiteId(siteId);
					paymentBean.setStrSiteName(siteName);
					paymentBean.setPaymentType(paymentType);
					/*if(paymentType.equals("DIRECT")){
						paymentBean.setDoublePaidAmount(doublePaidAmount);
						paymentBean.setDoubleBalanceAmount(doubleInvoiceAmount-doublePaidAmount);
					}*/
					
					
					if(paymentType.equals("DIRECT")){
						
						if(StrRoleId.equals(rollIdForInvoiceTaxSubmit)){
							try	{
								responseForTaxSubmit=objPaymentProcessDao.checkIsTaxInvoiceIsSubmitedOrNot(vendorId,indentEntryId,invoiceNo,siteId);
									if(!responseForTaxSubmit.equals("SUBMITTED")){
										//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
										continue;
									}
								}catch(Exception e){
									e.printStackTrace();
									//successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submitted.");
									continue;
								}
							}
					}
					
					//if payment against invoice 
					if(paymentType.equals("DIRECT")){
						paymentBean.setDoublePaidAmount(doublePaidAmount+Double.valueOf(requestedAmount)+doubleAdjustAmountFromAdvance);
						paymentBean.setDoubleBalanceAmount(doubleInvoiceAmount-doublePaidAmount-Double.valueOf(requestedAmount)-doubleAdjustAmountFromAdvance);
						paymentBean.setBalanceAmt(String.format("%.2f",(doubleInvoiceAmount-doublePaidAmount-Double.valueOf(requestedAmount)-doubleAdjustAmountFromAdvance)));
					}
					//if payment against po
					if(paymentType.equals("ADVANCE")){
						paymentBean.setDoublePaidAmount(doublePaidAmount+Double.valueOf(requestedAmount)+doubleAdjustAmountFromAdvance);
						paymentBean.setDoubleBalanceAmount(doublePOAmount-doublePaidAmount-Double.valueOf(requestedAmount)-doubleAdjustAmountFromAdvance);
						paymentBean.setBalanceAmt(String.format("%.2f",(doublePOAmount-doublePaidAmount-Double.valueOf(requestedAmount)-doubleAdjustAmountFromAdvance)));
						/*paymentBean.setDoubleBalanceAmount(doublePOAmount);*/
					}

					if(strPaymentIntiateType.equalsIgnoreCase("Approved")||strPaymentIntiateType.equalsIgnoreCase("Rejected")){
						
						if(strPaymentIntiateType.equalsIgnoreCase("Approved")){

							String paymentMode = request.getParameter("paymentMode"+i);
							if(StringUtils.isNotBlank(paymentMode)|| (StringUtils.isBlank(strPendingEmpId) && StringUtils.isNotBlank(strPendingDeptId))){
								
								trankey = String.valueOf(intTempPaymentTransactionId);
								DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
								boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
								if(isdedup){
									request.setAttribute("IsAnyTransactionDedUp", true);
									continue;
								}
								objDedupTransactionHandler.saveTransaction(trankey, session);
								

								boolean isRowChanged = false;
								objPaymentDto = new PaymentDto();



								//checking if Adjust Amount is greater than Po Advance or not.
								String remainingAmountInAdvanceInDB = objPaymentProcessDao.getRemainingAmountInAdvance(poNo);
								if((doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance)>Double.valueOf(remainingAmountInAdvanceInDB)){
									continue;
								}
								//if payment against invoice, checking   Is the total requesting amount exceeded the limit of Invoice amount.
								if(paymentType.equals("DIRECT")){
									double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
									double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

									if((paymentReqUpto+paymentDoneUpto+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>doubleInvoiceAmount){continue;}
								}
								//if payment against invoice, checking   Is the total requesting amount exceeded the limit of PO amount.
								if(paymentType.equals("ADVANCE")){
									double paymentReqUpto = Double.valueOf(request.getParameter("paymentReqUpto"+i) == null ? "0" : request.getParameter("paymentReqUpto"+i));
									double paymentDoneUpto = Double.valueOf(request.getParameter("paymentDoneUpto"+i) == null ? "0" : request.getParameter("paymentDoneUpto"+i));

									if((paymentReqUpto+paymentDoneUpto+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>doublePOAmount){continue;}
								}
								//Do not pay more than PO amount
								String payBalanceInPo = request.getParameter("payBalanceInPo"+i);
								String paymentDoneOnMultipleInvoices = request.getParameter("paymentDoneOnMultipleInvoices"+i);
								String adjustedAmountFromPo = request.getParameter("adjustedAmountFromPo"+i);
								if(paymentType.equals("DIRECT")){
									if(!payBalanceInPo.equals("NO_ADVANCE")){
										//validating current requested amount to balance in PO
										if(Double.valueOf(requestedAmount) > Double.valueOf(payBalanceInPo)){
											continue;
									 	}
										//validating current requested amount, balance in PO & total payment done on multiple invoices of PO
									 	if(Double.valueOf(requestedAmount) > Double.valueOf(payBalanceInPo) - (Double.valueOf(paymentDoneOnMultipleInvoices) - Double.valueOf(adjustedAmountFromPo))){
									 		continue;
									 	}
									}
								}


								objPaymentDto.setIntTempPaymentTransactionId(intTempPaymentTransactionId);
								objPaymentDto.setIntPaymentDetailsId(intPaymentDetailsId);
								objPaymentDto.setIntPaymentId(intPaymentId);
								objPaymentDto.setPaymentType(paymentType);
								objPaymentDto.setStrPaymentReqDate(requestedDate);
								objPaymentDto.setPaymentMode(paymentMode);
								objPaymentDto.setUtrChequeNo(utrChequeNo);
								objPaymentDto.setStrRemarks(remarks);
								objPaymentDto.setIntSiteWisePaymentId(intSiteWisePaymentId);
								objPaymentDto.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
								objPaymentDto.setStrPONo(poNo);
								objPaymentDto.setStrInvoiceNo(invoiceNo);
								objPaymentDto.setDoublePOAmount(doublePOAmount);
								objPaymentDto.setDoubleInvoiceAmount(doubleInvoiceAmount);
								objPaymentDto.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);
								objPaymentDto.setActualDoubleAdjustAmountFromAdvance(actualDoubleAdjustAmountFromAdvance);
								objPaymentDto.setStrSiteId(siteId);



								if(doubleAdjustAmountFromAdvance!=actualDoubleAdjustAmountFromAdvance){
									isRowChanged = true;
									objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPO(poNo,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);
								}
								if(!paymentMode.equals(actualPaymentMode)||!utrChequeNo.equals(actualUtrChequeNo)){
									isRowChanged = true;
								}

								if(StringUtils.isBlank(strPendingEmpId) && StringUtils.isNotBlank(strPendingDeptId)){


									objPaymentProcessDao.updateTempPaymentTransactionsTbl(objPaymentDto,strPendingDeptId);

									if(actualDoubleAdjustAmountFromAdvance!=doubleAdjustAmountFromAdvance){
										objPaymentProcessDao.updateReqUptoInAccPayment(String.valueOf(intPaymentId),"0","0",actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);
									}



								}
								else if(StringUtils.isBlank(strPendingDeptId)&& strPendingEmpId.equals("VND")){

									objPaymentProcessDao.updateTempPaymentTransactionsTbl(objPaymentDto,strPendingEmpId);

									int intPaymentTransactionId = objPaymentProcessDao.getIntPaymentTransactionId();
									//inserting payment in final Transaction table.
									objPaymentProcessDao.insertPaymentTransactionsTbl(objPaymentDto,intPaymentTransactionId);
									//if payment against invoice, inserting in Invoice History
									if(paymentType.equals("DIRECT")){
										objPaymentProcessDao.insertInvoiceHistory(objPaymentDto,intPaymentTransactionId);
									}
									//if adjusting amount is there, insert in Transaction table & Invoice History table.
									if(objPaymentDto.getDoubleAdjustAmountFromAdvance()>0){
										if(paymentType.equals("DIRECT")){
											objPaymentProcessDao.insertInvoiceHistoryAdjust(objPaymentDto,intPaymentTransactionId);
										}
									}
									//if payment against invoice, updating PAYMENT_DONE_UPTO & PAYMENT_REQ_UPTO in ACC_PAYMENT table
									if(paymentType.equals("DIRECT")){
										objPaymentProcessDao.updatePaymentReqUptoAndPaymentDoneUptoInAccPaymentTable(objPaymentDto);
									}
									//if payment against po, updating PAYMENT_REQ_UPTO in ACC_PAYMENT table
									if(paymentType.equals("ADVANCE")){
										objPaymentProcessDao.updatePaymentReqUptoInAccPaymentTable(objPaymentDto);
									}

									/*if(paymentType.equals("DIRECT")){
									paymentBean.setDoublePaidAmount(doublePaidAmount+Double.valueOf(requestedAmount)+doubleAdjustAmountFromAdvance);
									paymentBean.setDoubleBalanceAmount(doubleInvoiceAmount-doublePaidAmount-Double.valueOf(requestedAmount)-doubleAdjustAmountFromAdvance);
								}*/


									if(paymentType.equals("ADVANCE")){
										objPaymentProcessDao.insertPoHistory(objPaymentDto);
									}


									//if payment against po, insert payment as advance in Po Advance table.
									if(paymentType.equals("ADVANCE")){
										objPaymentProcessDao.insertPaidAmountInAdvancePaymentPoTable(objPaymentDto);
									}
									//if payment against invoice, adjust amount in Po Advance table.
									if(paymentType.equals("DIRECT")){
										objPaymentProcessDao.updateAdvancePaymentPoTable(poNo,doubleAdjustAmountFromAdvance);
									}
									//if payment against invoice, check all the payment done or not. if Done, then inactive Payment In AccPayment Table. 
									if(paymentType.equals("DIRECT")){
										objPaymentProcessDao.setInactiveAccPaymentAfterCheck(intPaymentId);
									}

									//if payment mode is cash (or) cheque, dont do download vendor Payment details.
									if(StringUtils.isNotBlank(paymentMode)){
										if(!paymentMode.equalsIgnoreCase("CASH")&&!paymentMode.equalsIgnoreCase("CHEQUE")){
											if(StringUtils.isNotBlank(strPendingEmpId) && strPendingEmpId.equals("VND")){
												request.setAttribute("isDownload", "true");
												createTxtDocOfVendorPaymentDetails(vendorName,requestedAmount,intPaymentDetailsId,vendorId,paymentMode,requestedDate,request,response,strEmpId,requestedDateMap);
												isDownload = true;
											}
										}
									}
								}

								objPaymentProcessDao.saveAccountApprovalDetails(intAccDeptPaymentProcessId,strEmpId,comments,intTempPaymentTransactionId,siteId);
								if(isRowChanged){
									PaymentBean objPaymentBean = new PaymentBean();
									objPaymentBean.setIntTempPaymentTransactionId(intTempPaymentTransactionId);
									objPaymentBean.setStrEmployeeId(strEmpId);
									objPaymentBean.setStrRemarks(comments);
									objPaymentBean.setActualDoubleAdjustAmountFromAdvance(actualDoubleAdjustAmountFromAdvance);
									objPaymentBean.setDoubleAdjustAmountFromAdvance(doubleAdjustAmountFromAdvance);
									objPaymentBean.setActualPaymentMode(actualPaymentMode);
									objPaymentBean.setPaymentMode(paymentMode);
									objPaymentBean.setActualUtrChequeNo(actualUtrChequeNo);
									objPaymentBean.setUtrChequeNo(utrChequeNo);
									objPaymentProcessDao.saveAccountChangedDetails(objPaymentBean,"M"); 


								}
								paymentBean.setIntSerialNo(++intSerialNo);
								paymentDetailsList.add(paymentBean);

								//this object stores Approve details for Accounts next level.
								PaymentBean objPB = new PaymentBean();
								objPB.setIntPaymentDetailsId(intPaymentDetailsId);
								objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
								objPB.setStrPONo(poNo.equals("")?"-":poNo);
								objPB.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
								objPB.setStrVendorName(vendorName.equals("")?"-":vendorName);
								objPB.setStrSiteName(siteName.equals("")?"-":siteName);
								objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
								objPB.setStrRemarks(comments.equals("")?"-":comments);
								SuccessDataListToMail.add(objPB);
								if(siteId.equals(strMarketingId)){
									SuccessDataListToMailMarketing.add(objPB);
									strMarketingSiteId=strMarketingId;
									marketingPos.add(poNo);
								}


							}//if-paymentMode
						}//if-approve
						else if(strPaymentIntiateType.equalsIgnoreCase("Rejected")){
							
							trankey = String.valueOf(intTempPaymentTransactionId);
							DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
							boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
							if(isdedup){
								request.setAttribute("IsAnyTransactionDedUp", true);
								continue;
							}
							objDedupTransactionHandler.saveTransaction(trankey, session);
							

							String strLowerEmpId = objPaymentProcessDao.getLowerEmpIdInAccounts(strEmpId);
							/*List<String> paymentInitiatorList = new ArrayList<String>(Arrays.asList("997_B_1","997_H_1"));//objPaymentProcessDao.getPaymentInitiatorInAccounts();

						boolean matchFound = false;
						if(paymentInitiatorList.contains(strLowerEmpId)){
							matchFound = true;
							break;
						}*/

							if(strLowerEmpId.equals("997_B_1")||strLowerEmpId.equals("997_H_1")){
								objPaymentProcessDao.updateRowInAccTempPaymentTransactions(intTempPaymentTransactionId,remarks,intPaymentDetailsId);
								objPaymentProcessDao.updateIntiateAmountInAccDeptPaymentProcesstbl(requestedAmount,intAccDeptPaymentProcessId,actualDoubleAdjustAmountFromAdvance);
							}
							else{
								objPaymentProcessDao.revertPendingApprovalToLowerEmployee(strLowerEmpId,intTempPaymentTransactionId,remarks);
							}
							objPaymentProcessDao.saveAccountRejectDetails(intAccDeptPaymentProcessId,strEmpId,comments,intTempPaymentTransactionId,siteId);
							paymentBean.setIntSerialNo(++intSerialNo);
							paymentDetailsList.add(paymentBean);
							PaymentBean objPB = new PaymentBean();
							objPB.setIntPaymentDetailsId(intPaymentDetailsId);
							objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
							objPB.setStrPONo(poNo.equals("")?"-":poNo);
							objPB.setDoubleAmountToBeReleased(Double.valueOf(requestedAmount));
							objPB.setStrVendorName(vendorName.equals("")?"-":vendorName);
							objPB.setStrSiteName(siteName.equals("")?"-":siteName);
							objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
							objPB.setStrRemarks(comments.equals("")?"-":comments);
							PaymentRejectListToMail.add(objPB);

						}//if-reject
					}//if-approve or reject
					
				}//For loop
				
				//Generating zip file for final payment approval
				try {
						if (null != requestedDateMap) {
						if (requestedDateMap.size() > 0) {

							String zipFileName = "PaymentFiles.zip";

							FileOutputStream out = new FileOutputStream(zipFileName);
							ZipOutputStream zos = new ZipOutputStream(out);

							Set<Entry<String, String>> requestedDateFile = requestedDateMap.entrySet();
							for (Entry<String, String> entry : requestedDateFile) {
								String fileName = entry.getValue();
								//String paymentDate = entry.getKey();
								//System.out.println(entry);
								File file = new File(fileName);

								InputStream in = new FileInputStream(file);
								BufferedInputStream bis = new BufferedInputStream(in);
								zos.putNextEntry(new ZipEntry(file.getName()));
								// Get the file
								FileInputStream fis = null;
								try {
									fis = new FileInputStream(file);
								} catch (FileNotFoundException fnfe) {
									// If the file does not exists, write an
									// error entry instead of
									// file
									// contents
									zos.write(("ERRORld not find file " + file.getName()).getBytes());
									zos.closeEntry();
									//System.out.println("Couldfind file " + file.getAbsolutePath());
									continue;
								}
								BufferedInputStream fif = new BufferedInputStream(fis);
								// Write the contents of the file
								int data = 0;
								while ((data = fif.read()) != -1) {
									zos.write(data);
								}

								fif.close();
								fis.close();
								in.close();
								bis.close();

								boolean flag = file.delete();
								zos.closeEntry();
								try {
									if (file.exists())
									file.delete();
								} catch (Exception e) {

								}			
						//System.out.println("Finishedng file " + file.getName() + " and deleted " + flag);
					} // for loop
					
					zos.close();
					out.close();
					}
				}
					request.removeAttribute("requestedDatePaymentList");
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				requestedDateMap=null;
			}
			
			//strResponse = "Success";
		} catch (Exception e) {
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			e.printStackTrace();
		}
		final String user_id_final=strEmpId;
		final String user_name_final=user_name;
		final String pendingDeptId_final=strPendingDeptId;
		final int getLocalPort = request.getLocalPort();
		final String strStrPendingDeptId=strPendingDeptId;
		final String strstrPendingEmpId=strPendingEmpId;
		final String poNumber=poNo;
		final String strMarketingIds=strMarketingId;
		final String strSiteId_final=strMarketingSiteId;
		
		//final String requestReceiveFrom_final = getLowerDeptIdOfGivenEmpId(strEmpId);
		if(SuccessDataListToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						if(StringUtils.isNotBlank(pendingDeptId_final)){
						//sending mail for Approval to next level
						String [] emailToAddress = getEmailsOfEmpByDeptId(pendingDeptId_final);
						
						objEmailFunction.sendPaymentApprovalMailToAccountsAllLevels(SuccessDataListToMail,emailToAddress,getLocalPort,user_name_final);
						}
						//sending status mail to lower level 
						String [] emailToAddress1 = getEmailsOfEmployeesInLowerDeptOfThisEmployee(user_id_final);
						if(strstrPendingEmpId.equals("VND")){
							String[] marketingMails=null; 
							if(strSiteId_final.equals(strMarketingIds) && marketingPos.size()>0 && marketingPos!=null){
								List<String> list=objPaymentProcessDao.getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevelForMarketing(marketingPos,strMarketingIds);
							if(list!=null && list.size()>0){
								//list.addAll(java.util.Arrays.asList(emailToAddress1));
								//emailToAddress1 = new String[list.size()];
								//list.toArray(emailToAddress1);
								marketingMails=new String[list.size()];
								list.toArray(marketingMails);
							}
							objEmailFunction.sendPaymentApprovalStatusMailToLowerEmployee(SuccessDataListToMailMarketing,marketingMails,getLocalPort,user_name_final);
							}
							
						}
						objEmailFunction.sendPaymentApprovalStatusMailToLowerEmployee(SuccessDataListToMail,emailToAddress1,getLocalPort,user_name_final);
						
					}

					
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		if(PaymentRejectListToMail.size()!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						String [] emailToAddress1 = getEmailsOfEmployeesInLowerDeptOfThisEmployee(user_id_final);
						String EmailBodyMessage = "Account Department Rejected this payment(s) ";
						objEmailFunction.sendPaymentRejectedStatusMail(PaymentRejectListToMail, emailToAddress1, getLocalPort,user_name_final,EmailBodyMessage);
						
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}

		final HttpServletRequest request_final = request;
		final HttpServletResponse response_final = response;
		final String strEmpId_final = strEmpId;
		
		if(isDownload){
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{


				executorService.execute(new Runnable() {
					public void run() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						downloadTxtFileToClientMachine(request_final,response_final,strEmpId_final);
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}
		}
		return paymentDetailsList;
	}

	protected String[] getEmailsOfEmployeesInLowerDeptOfThisEmployee(String user_id) {
		return objPaymentProcessDao.getEmailsOfEmployeesInLowerDeptOfThisEmployee(user_id);
	}
	@Override
	public String getPendingEmpId(String user_id,String site_id) {
		return objPaymentProcessDao.getPendingEmpId(user_id,site_id);
	}	

	public static void main(String [] args){


		double val = 425.58*87895.25;

		//System.out.println( val);

		//System.out.printf("dexp: %f\n", val);

		String firstNumberAsString = String.format ("%.2f", val);

		//System.out.println( firstNumberAsString);

	}

	@Override
	public List<String> updateRefNoInAccDeptTransaction(HttpServletRequest request, String strUserId, HttpSession session) {


		String strResponse = "";
		int intPaymentTransactionId=0;
		String strRefNo = "";
		int intTotalNoOfRecords = 0;
		String paymentMode = "";
		String paymentDate = "";
		List<String> successList = new ArrayList<String>();
		String paymentDetailsId = "";
		String poNo = "";
		String invoiceNo = "";
		String trankey = "";
		
		try {
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfPendingPayments") == null ? "0" : request.getParameter("noOfPendingPayments").toString());
			if(intTotalNoOfRecords > 0){


				for(int i = 1;i<=intTotalNoOfRecords;i++){

					if(request.getParameter("checkboxname"+i)!=null){
						intPaymentTransactionId = Integer.parseInt(request.getParameter("paymentTransactionId"+i));
						
						trankey = String.valueOf(intPaymentTransactionId);
						DedupTransactionHandler objDedupTransactionHandler = new DedupTransactionHandler();
						boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
						if(isdedup){
							request.setAttribute("IsAnyTransactionDedUp", true);
							continue;
						}
						objDedupTransactionHandler.saveTransaction(trankey, session);
						
						strRefNo = request.getParameter("utrChequeNo"+i);
						paymentMode = request.getParameter("paymentMode"+i);
						paymentDate = request.getParameter("paymentDate"+i);
						if(StringUtils.isBlank(strRefNo)||StringUtils.isBlank(paymentDate)){continue;}
						objPaymentProcessDao.updateRefNoInAccDeptTransaction(strRefNo,intPaymentTransactionId,paymentMode,paymentDate);
						objPaymentProcessDao.updateRefNoInInvoiceHistory(strRefNo,paymentMode,intPaymentTransactionId);
						
						paymentDetailsId=request.getParameter("paymentDetailsId"+i);
						invoiceNo = request.getParameter("invoiceNo"+i);
						poNo = request.getParameter("poNo"+i);
						
						successList.add("Payment updated successfully for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".");
						
					}

				}

			}
			//strResponse = "Success";
		} catch (NumberFormatException e) {
			objDedupTransactionHandler.deleteTransaction(trankey, session);
			e.printStackTrace();
		}

		return successList;

	}
	


	public void createTxtDocOfVendorPaymentDetails(String vendorName,String requestedAmount, int intPaymentDetailsId, String vendorId, String paymentMode, String requestedDate, HttpServletRequest request, HttpServletResponse response, String strEmpId, Map<String, String> requestedDateMap){
		VendorDetails vd = objPaymentProcessDao.getVendorAccountDetails(vendorId);
		int portNo = request.getLocalPort();
		String path = "";
		if(portNo==80){
			path = validateParams.getProperty("LIVE_PAYMENTS_FILE_PATH");
		} else if (portNo == 8079) {
			path = validateParams.getProperty("CUG_PAYMENTS_FILE_PATH");
		}  else if (portNo == 8078) {
			path = validateParams.getProperty("CUG_PAYMENTS_FILE_PATH");
		}else {
			path = validateParams.getProperty("LOCAL_PAYMENTS_FILE_PATH");
		}

		// this request attribute for loading file names when they are going to
		// download
        File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                //System.out.println("Multiple directories are created!");
            } else {
                //System.out.println("Failed to create multiple directories!");
            }
        }
	
		request.setAttribute("requestedDatePaymentList", requestedDateMap);
		String tempRequestedDate = requestedDate.replace("/", "-");
		String fileName = path+"/PaymentsTextfile-"+tempRequestedDate + ".txt";
		if (requestedDateMap.containsKey(tempRequestedDate)) {
			fileName =requestedDateMap.get(tempRequestedDate);	
		}
	
		File file = new File(fileName);
		try {
			if (!file.exists())
				file.createNewFile();
			else{
				//System.out.println(file.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			requestedDateMap.put(tempRequestedDate, fileName);
			String ifsc_code = vd.getIfsc_Code();
			if(StringUtils.isNotBlank(ifsc_code)){
				if(ifsc_code.length()>3){
					if(ifsc_code.substring(0, 4).equalsIgnoreCase("hdfc")){
						bw.write("I"+",");
						bw.write(vd.getVendor_name().substring(0, 4).toUpperCase()+",");
					}
					else{
						bw.write("N"+",,");
					}
					
				}
				else{
					bw.write("N"+",,");
				}
			}
			else{
				bw.write("N"+",,");
			}
			bw.write(vd.getAcc_Number() + ",");
			bw.write(get_17point2_lengthNumber(requestedAmount) + ",");
			if(vd.getVendor_name().length()>40){
				bw.write(vd.getVendor_name().substring(0,40)+",");
			}
			else{
				bw.write(vd.getVendor_name()+",");
			}bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			if(vd.getVendor_name().length()>20){
				bw.write(vd.getVendor_name().substring(0,20)+",");
			}
			else{
				bw.write(vd.getVendor_name()+",");
			}bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(",");
			bw.write(getRequiredFormatDate(requestedDate) + ",");
			bw.write(",");
			bw.write(vd.getIfsc_Code() + ",");
			bw.write(vd.getBank_name() + ",");
			bw.write(",");
			if(vd.getVendor_email().contains(",")){
				bw.write(vd.getVendor_email().split(",")[0]);
			}
			else{
				bw.write(vd.getVendor_email());
			}
			bw.newLine();

		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException el) {
				el.printStackTrace();
			}
		}

		
		
		
	}
	private void downloadTxtFileToClientMachine(HttpServletRequest request, HttpServletResponse response, String strEmpId) {
		//System.out.println("PaymentProcessServiceImpl.downloadTxtFileToClientMachine()");
	}
	private String getRequiredFormatDate(String requestedDate) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(DateUtil.convertToJavaDateFormat(requestedDate));
		
	}
	public String get_17point2_lengthNumber(String requestedAmount) {
		
		DecimalFormat df = new DecimalFormat("00000000000000000.00");
		requestedAmount = String.valueOf(df.format(Double.parseDouble(requestedAmount))); 
		return requestedAmount;
	}
	
	public List<String> updatePreviousPayments(HttpServletRequest request){

		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<String> successList = new ArrayList<String>();

		int intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfRecords") == null ? "0" : request.getParameter("noOfRecords").toString());

		if(intTotalNoOfRecords > 0){
			for(int i = 1;i<=intTotalNoOfRecords;i++){
				if(request.getParameter("checkboxname"+i)!=null){

					PaymentBean objPaymentBean = new PaymentBean(); 
					String invoiceNo = request.getParameter("invoiceNo"+i);
					if(StringUtils.isBlank(invoiceNo)){break;}
					String invoiceAmount = request.getParameter("invoiceAmount"+i);
					//String siteName = request.getParameter("");
					String siteId = request.getParameter("siteId"+i);//give site_id here
					String receivedDate = request.getParameter("invoiceReceivedDate"+i);
					String invoiceDate = request.getParameter("invoiceDate"+i);
					String dcNo = request.getParameter("dcNo"+i);
					String dcDate = request.getParameter("dcDate"+i);
					//String vendorName = request.getParameter("");
					String vendorId = request.getParameter("vendorId"+i);//give vendor_id here
					String poId = request.getParameter("poNo"+i);
					String poDate = request.getParameter("poDate"+i);
					String poAmount = request.getParameter("poAmount"+i);
					String paidAmount = request.getParameter("AmountToBeReleased"+i);
					String adjustedAmount = "0";
					//String intiatedAmount = request.getParameter("");//sh.getCell(14, row).getContents().trim();
					//String remainingAmount = request.getParameter("");//sh.getCell(15, row).getContents().trim();
					//String paymentDoneUpto = request.getParameter("");//sh.getCell(16, row).getContents().trim();
					//String paymentReqUpto = request.getParameter("");//sh.getCell(17, row).getContents().trim();
					//String status = request.getParameter("");//sh.getCell(18, row).getContents().trim();
					String remarks = request.getParameter("remarks"+i);
					String creditTotalAmount = request.getParameter("creditTotalAmount"+i);
					String creditNoteNumber = request.getParameter("creditNoteNumber"+i);
					String paymentType = "DIRECT";
					String paymentDate = request.getParameter("paymentDate"+i);
					String paymentMode = request.getParameter("paymentMode"+i);
					String refNo = request.getParameter("utrChequeNo"+i);
					//String typeOfPayment = request.getParameter("");//sh.getCell(26, row).getContents().trim();
					//String refNo = sh.getCell(27, row).getContents().trim();
					//set elements to object
					objPaymentBean.setStrInvoiceNo(invoiceNo);
					objPaymentBean.setDoubleInvoiceAmount(Double.valueOf(invoiceAmount));
					objPaymentBean.setStrSiteId(siteId);
					objPaymentBean.setDoubleAmountToBeReleased(Double.valueOf(paidAmount));
					objPaymentBean.setStrDCNo(dcNo);
					objPaymentBean.setStrRemarks(remarks);
					objPaymentBean.setStrVendorId(vendorId);
					objPaymentBean.setStrPONo(poId);
					objPaymentBean.setDoublePOTotalAmount(Double.valueOf(poAmount));
					objPaymentBean.setStrInvoiceDate(invoiceDate);
					objPaymentBean.setStrPODate(poDate);
					objPaymentBean.setStrDCDate(dcDate);
					objPaymentBean.setStrInvoiceReceivedDate(receivedDate);
					int setIntIndentEntryId = objPaymentProcessDao.getIndentEntryIdByInvoiceNo(invoiceNo,siteId,vendorId);
					objPaymentBean.setIntIndentEntryId(setIntIndentEntryId);
					objPaymentBean.setDoubleCreditTotalAmount(Double.valueOf(creditTotalAmount));
					objPaymentBean.setStrCreditNoteNumber(creditNoteNumber);
					//dtls
					objPaymentBean.setPaymentType(paymentType);
					objPaymentBean.setStrPaymentReqDate(paymentDate);
					objPaymentBean.setDoubleAdjustAmountFromAdvance(Double.valueOf(adjustedAmount));
					//pross
					objPaymentBean.setRequestedAmount(paidAmount);//same as amount to be released
					//temp
					objPaymentBean.setPaymentMode(paymentMode);
					objPaymentBean.setStrRefrenceNo(refNo);
					//transactions
					objPaymentBean.setUtrChequeNo(refNo);//same as ref no

					//add object to list
					list.add(objPaymentBean);
				}
			}
		}
		/****************************************************/
		try{
			for(PaymentBean objPaymentBean:list){
				/*************/
				int paymentId = objPaymentProcessDao.getPaymentIdByInvoiceNo(objPaymentBean);
				if(paymentId==0){
					paymentId = objPaymentProcessDao.getPaymentId();
					objPaymentProcessDao.insertInACC_PAYMENT(objPaymentBean,paymentId);
				}
				else{
					objPaymentProcessDao.updateACC_PAYMENT(objPaymentBean,paymentId);
				}
				objPaymentBean.setIntPaymentId(paymentId);
				/*************/
				int intSiteWisePaymentNo = 0;
				intSiteWisePaymentNo = objPaymentProcessDao.getSiteWisePaymentNo(objPaymentBean.getStrSiteId());
				objPaymentBean.setIntSiteWisePaymentId(intSiteWisePaymentNo);
				int PaymentEntryDetailsSeqID = objPaymentProcessDao.getPaymentDetailsId();
				objPaymentProcessDao.insertInACC_PAYMENT_DTLS(objPaymentBean, paymentId,PaymentEntryDetailsSeqID);
				objPaymentBean.setIntPaymentDetailsId(PaymentEntryDetailsSeqID);
				/*************/
				int accDeptPaymentProssSeqNo = objPaymentProcessDao.getAccDeptPaymentProssSeqNo();
				objPaymentProcessDao.insertInACC_ACCOUNTS_DEPT_PMT_PROSS(objPaymentBean,accDeptPaymentProssSeqNo);
				objPaymentBean.setIntAccDeptPaymentProcessId(accDeptPaymentProssSeqNo);
				/*************/
				int TempPaymentId = objPaymentProcessDao.getAccTempPaymentTransactionSeqNo();
				objPaymentProcessDao.insertInACC_TEMP_PAYMENT_TRANSACTIONS(objPaymentBean,TempPaymentId);
				objPaymentBean.setIntTempPaymentTransactionId(TempPaymentId);
				/*************/
				int intPaymentTransactionId = objPaymentProcessDao.getIntPaymentTransactionId();
				objPaymentProcessDao.insertInACC_PAYMENT_TRANSACTIONS(objPaymentBean,intPaymentTransactionId);
				/*************/

				if(objPaymentBean.getPaymentType().equalsIgnoreCase("DIRECT")){
					objPaymentProcessDao.insertInvoiceHistory(objPaymentBean,intPaymentTransactionId);
				}
				/*************/
				if(objPaymentBean.getPaymentType().equalsIgnoreCase("ADVANCE")){
					objPaymentProcessDao.insertPoHistory(objPaymentBean);
				}
				/*************/
				if(objPaymentBean.getPaymentType().equalsIgnoreCase("ADVANCE")){
					objPaymentProcessDao.insertPaidAmountInAdvancePaymentPoTable(objPaymentBean);
				}
				/*************/
				objPaymentProcessDao.setInactiveAccPaymentAfterCheck(paymentId);
				successList.add("PaymentDetailsId: "+String.valueOf(PaymentEntryDetailsSeqID)+" ,"+"InvoiceNumber: "+objPaymentBean.getStrInvoiceNo()+"  => Success");
			}
			/************/
			
		}catch(Exception e){
			successList.add("Failed. Exception Occured");
			e.printStackTrace();
		}

		return successList;

	}
	@Override
	public List<PaymentBean> getInvoiceDetailsForOldPayments(String siteId,Map<String,String> reqParamsMap){

		List<PaymentBean> listPaymentBean =  null;
		try{
			listPaymentBean = objPaymentProcessDao.getInvoiceDetailsForOldPayments( siteId, reqParamsMap);
		}catch(Exception e){
			e.printStackTrace();
		}

		return listPaymentBean;
	}
	
	/************************************************Contractor Bill Requset Amount start**************************************************************/
	
	@Override
	public List<PaymentBean> getContractorBillReqDetails(String user_id,String site_id,Map<String,String> reqParamsMap){

		List<PaymentBean> listPaymentBean =  null;
		try{
			listPaymentBean = objPaymentProcessDao.getContractorBillReqDetails(user_id, site_id, reqParamsMap);
		}catch(Exception e){
			e.printStackTrace();
		}

		return listPaymentBean;
	}
	/**********************************************Contractor Bill Req For Payment end*******************************************************************/
	
	/***********************************************save Contractor Data For Payment*******************************************************************/
	

		@Override
		public List<String> saveContractorBillPaymentDetails( HttpServletRequest request,HttpSession session){
			//this list is for to send payments pending for next level(site 3rd level or Accounts 1st level).
			//final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
			List<PaymentBean> list = new ArrayList<PaymentBean>();
			PaymentBean objPaymentBean = null; 
			objPaymentBean = new PaymentBean();
			
			ModelAndView model = null;
			String cntPaymentId = "";
			String PaymentReqAmount = "";
			
			List<String> successList = new ArrayList<String>();
			String user_id="";
			String session_site_id = "";
			String site_name ="";
			String user_name = "";
			String strUserId="";
			String strSiteId="";
			String pendingDeptId="-";
			String pendingEmpId="-";
			
			try {
				
				site_name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();	
				user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
				user_id=String.valueOf(session.getAttribute("UserId"));
				session_site_id = String.valueOf(session.getAttribute("SiteId"));	
				//pendingEmpId = objPaymentProcessDao.getPendingEmpId(user_id);
				
				model = new ModelAndView();
				int intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfRecords") == null ? "0" : request.getParameter("noOfRecords").toString());
				strUserId=request.getParameter("userId")==null ? "" : request.getParameter("userId").toString();
				 pendingEmpId=objPaymentProcessDao.getPendingContractorEmp(strUserId);
				if(pendingEmpId.equals("-"))
				{
					pendingDeptId = objPaymentProcessDao.getPendingContractorDeptId(user_id,session_site_id);
				}
				if(pendingEmpId.equals("-") && !pendingDeptId.equals("-")){
				if(intTotalNoOfRecords > 0){
					
					for(int i = 1;i<=intTotalNoOfRecords;i++){
						if(request.getParameter("checkboxname"+i)!=null){

							objPaymentBean = new PaymentBean();
							//objPaymentDto = new PaymentDto();

							cntPaymentId = request.getParameter("cntPaymentId"+i);
							PaymentReqAmount = request.getParameter("PaymentReqAmt"+i);
							String WorkOrderNo=request.getParameter("WorkOrderNo"+i);
							String billId=request.getParameter("BillId"+i);
							String tempBillId=request.getParameter("TempBillId"+i);
							String contractorName=request.getParameter("contractorName"+i);
							String comments = request.getParameter("comments"+i);
							int acc_pross_pmt_Id=objPaymentProcessDao.getAccDeptPaymentProssSeqNo();
							strSiteId=request.getParameter("siteId"+i);
							WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , cntPaymentId:"+cntPaymentId);
							objPaymentBean.setIntCntPaymentId(Integer.parseInt(cntPaymentId));
							objPaymentBean.setPaymentReqAmt(PaymentReqAmount);
							objPaymentBean.setUserId(strUserId);
							objPaymentBean.setStrSiteId(strSiteId);
							objPaymentBean.setStrComments(comments);
							objPaymentBean.setStrTempBillId(tempBillId);
							successList.add("Payment Initiated on Bill No:"+billId+" for "+contractorName+" successfully.");
							objPaymentProcessDao.saveContractorBillPaymentProcessDtls(objPaymentBean,acc_pross_pmt_Id,pendingDeptId);
							objPaymentProcessDao.insertCntPaymentApprRejDetail(objPaymentBean,  "C");
						}
					}
				}
				else{
					model.setViewName("payment/payment");
				}
				}else if(!pendingEmpId.equals("-") && pendingDeptId.equals("-")){
					if(intTotalNoOfRecords > 0){
						
						for(int i = 1;i<=intTotalNoOfRecords;i++){
							if(request.getParameter("checkboxname"+i)!=null){
								objPaymentBean = new PaymentBean();
								cntPaymentId = request.getParameter("cntPaymentId"+i);
								String WorkOrderNo=request.getParameter("WorkOrderNo"+i);
								String billId=request.getParameter("BillId"+i);
								String tempBillId=request.getParameter("TempBillId"+i);
								String contractorName=request.getParameter("contractorName"+i);
								String comments = request.getParameter("comments"+i);
								WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , cntPaymentId:"+cntPaymentId);
								objPaymentBean.setIntCntPaymentId(Integer.parseInt(cntPaymentId));
								objPaymentBean.setPaymentReqAmt(PaymentReqAmount);
								objPaymentBean.setUserId(strUserId);
								objPaymentBean.setStrSiteId(strSiteId);
								objPaymentBean.setStrComments(comments);
								objPaymentBean.setStrTempBillId(tempBillId);
								successList.add("Payment Initiated on Bill No:"+billId+" for "+contractorName+" successfully.");
								objPaymentProcessDao.updateContractorBillPayment(objPaymentBean,pendingEmpId);
								objPaymentProcessDao.insertCntPaymentApprRejDetail(objPaymentBean,  "C");
							}
						}
					}
					else{
						model.setViewName("payment/payment");
					}
					
				}


			}catch(Exception e){
				successList.add("Failed. Exception Occured");
				e.printStackTrace();
			}
			
			return successList;
		}

		@Override
		public List<PaymentBean> getLocalPurchaseInvoiceDetails(String siteId,Map<String,String> reqParamsMap, String selectAll){

			List<PaymentBean> listPaymentBean =  null;
			try{
				listPaymentBean = objPaymentProcessDao.getLocalPurchaseInvoiceDetails( siteId, reqParamsMap, selectAll);
			}catch(Exception e){
				e.printStackTrace();
			}

			return listPaymentBean;
		}
		@Override
		public List<String> updateLocalPurchasePayments(HttpServletRequest request,HttpSession session){

			List<PaymentBean> list = new ArrayList<PaymentBean>();
			List<String> successList = new ArrayList<String>();
			String trankey = "";
			String responseForTaxSubmit="";

			int intTotalNoOfRecords = Integer.parseInt(request.getParameter("noOfRecords") == null ? "0" : request.getParameter("noOfRecords").toString());

			if(intTotalNoOfRecords > 0){
				for(int i = 1;i<=intTotalNoOfRecords;i++){
					if(request.getParameter("checkboxname"+i)!=null){

						PaymentBean objPaymentBean = new PaymentBean(); 
						String invoiceNo = request.getParameter("invoiceNo"+i);
						if(StringUtils.isBlank(invoiceNo)){break;}
						String invoiceAmount = request.getParameter("invoiceAmount"+i);
						//String siteName = request.getParameter("");
						String siteId = request.getParameter("siteId"+i);//give site_id here
						String receivedDate = request.getParameter("invoiceReceivedDate"+i);
						String invoiceDate = request.getParameter("invoiceDate"+i);
						String dcNo = request.getParameter("dcNo"+i);
						String dcDate = request.getParameter("dcDate"+i);
						//String vendorName = request.getParameter("");
						String vendorId = request.getParameter("vendorId"+i);//give vendor_id here
						String indentEntryId=request.getParameter("indentEntryId"+i);
						String poId = request.getParameter("poNo"+i);
						String poDate = request.getParameter("poDate"+i);
						String poAmount = request.getParameter("poAmount"+i);
						String paidAmount = request.getParameter("AmountToBeReleased"+i);
						String adjustedAmount = request.getParameter("AdjustAmountFromAdvance"+i);
						//String intiatedAmount = request.getParameter("");//sh.getCell(14, row).getContents().trim();
						//String remainingAmount = request.getParameter("");//sh.getCell(15, row).getContents().trim();
						//String paymentDoneUpto = request.getParameter("");//sh.getCell(16, row).getContents().trim();
						//String paymentReqUpto = request.getParameter("");//sh.getCell(17, row).getContents().trim();
						//String status = request.getParameter("");//sh.getCell(18, row).getContents().trim();
						String remarks = request.getParameter("remarks"+i);
						String creditTotalAmount = request.getParameter("creditTotalAmount"+i);
						String creditNoteNumber = request.getParameter("creditNoteNumber"+i);
						String paymentType = "DIRECT";
						String paymentDate = request.getParameter("paymentDate"+i);
						String paymentMode = request.getParameter("paymentMode"+i);
						String refNo = request.getParameter("utrChequeNo"+i);
						
						//validations
						String paymentReqUpto = request.getParameter("paymentReqUpto"+i);
						String paymentDoneUpto = request.getParameter("paymentDoneUpto"+i);
						String remainingPOAdvance = request.getParameter("remainingPOAdvance"+i);
						String payBalanceInPo = request.getParameter("payBalanceInPo"+i);
						String paymentDoneOnMultipleInvoices = request.getParameter("paymentDoneOnMultipleInvoices"+i);
						String adjustedAmountFromPo = request.getParameter("adjustedAmountFromPo"+i);
						double payingAmt = (Double.valueOf(paymentReqUpto) + Double.valueOf(paymentDoneUpto) + Double.valueOf(paidAmount) + Double.valueOf(adjustedAmount));
					
						try	{
							responseForTaxSubmit=objPaymentProcessDao.checkIsTaxInvoiceIsSubmitedOrNot(vendorId,indentEntryId,invoiceNo,siteId);
							if(!responseForTaxSubmit.equals("SUBMITTED")){
								successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submited.");
								continue;
							}
						}catch(Exception e){
							e.printStackTrace();
							successList.add("Failed. "+"Invoice No: "+invoiceNo+". Tax invoice is not submited.");
							continue;
						}
						
						
						if(payingAmt > Double.valueOf(invoiceAmount)){
							successList.add("Failed. "+"Invoice No: "+invoiceNo+". Total of Initiated Amount & Paid Amount is greater than Invoice Amount.");
							continue;
						}
						if(Double.valueOf(adjustedAmount) > Double.valueOf(remainingPOAdvance)){
							successList.add("Failed. "+"Invoice No: "+invoiceNo+". Adjust Amount is greater than Advance.");
							continue;
						}
						if(!payBalanceInPo.equals("NO_ADVANCE")){
						 	if(Double.valueOf(paidAmount) > Double.valueOf(payBalanceInPo)){
						 		successList.add("Failed. "+"InvoiceNumber: "+invoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
								continue;
						 	}
						 	if(Double.valueOf(paidAmount) > Double.valueOf(payBalanceInPo) - (Double.valueOf(paymentDoneOnMultipleInvoices) - Double.valueOf(adjustedAmountFromPo))){
						 		successList.add("Failed. "+"InvoiceNumber: "+invoiceNo+". Total of Initiated Amount & Paid Amount is greater than PO Amount");
								continue;
						 	}
						 }
						
						objPaymentBean.setStrInvoiceNo(invoiceNo);
						objPaymentBean.setDoubleInvoiceAmount(Double.valueOf(invoiceAmount));
						objPaymentBean.setStrSiteId(siteId);
						objPaymentBean.setDoubleAmountToBeReleased(Double.valueOf(paidAmount));
						objPaymentBean.setStrDCNo(dcNo);
						objPaymentBean.setStrRemarks(remarks);
						objPaymentBean.setStrVendorId(vendorId);
						objPaymentBean.setStrPONo(poId);
						objPaymentBean.setDoublePOTotalAmount(Double.valueOf(poAmount));
						objPaymentBean.setStrInvoiceDate(invoiceDate);
						objPaymentBean.setStrPODate(poDate);
						objPaymentBean.setStrDCDate(dcDate);
						objPaymentBean.setStrInvoiceReceivedDate(receivedDate);
						int setIntIndentEntryId = objPaymentProcessDao.getIndentEntryIdByInvoiceNo(invoiceNo,siteId,vendorId);
						objPaymentBean.setIntIndentEntryId(setIntIndentEntryId);
						objPaymentBean.setDoubleCreditTotalAmount(Double.valueOf(creditTotalAmount));
						objPaymentBean.setStrCreditNoteNumber(creditNoteNumber);
						//dtls
						objPaymentBean.setPaymentType(paymentType);
						objPaymentBean.setStrPaymentReqDate(paymentDate);
						objPaymentBean.setDoubleAdjustAmountFromAdvance(Double.valueOf(adjustedAmount));
						//pross
						objPaymentBean.setRequestedAmount(paidAmount);//same as amount to be released
						//temp
						objPaymentBean.setPaymentMode(paymentMode);
						objPaymentBean.setStrRefrenceNo(refNo);
						//transactions
						objPaymentBean.setUtrChequeNo(refNo);//same as ref no

						//add object to list
						list.add(objPaymentBean);
					}
				}
			}
			/****************************************************/
			try{
				for(PaymentBean objPaymentBean:list){
					trankey = objPaymentBean.getIntIndentEntryId()+"|"+objPaymentBean.getRequestedAmount();
					boolean isdedup = objDedupTransactionHandler.isDedupTransaction(trankey, session);
					if(isdedup){
						request.setAttribute("IsAnyTransactionDedUp", true);
						continue;
					}
					objDedupTransactionHandler.saveTransaction(trankey, session);
					
					/*************/
					int paymentId = objPaymentProcessDao.getPaymentIdByInvoiceNo(objPaymentBean);
					if(paymentId==0){
						paymentId = objPaymentProcessDao.getPaymentId();
						objPaymentProcessDao.insertInACC_PAYMENT(objPaymentBean,paymentId);
					}
					else{
						objPaymentProcessDao.updateACC_PAYMENT(objPaymentBean,paymentId);
					}
					objPaymentBean.setIntPaymentId(paymentId);
					/*************/
					int intSiteWisePaymentNo = 0;
					intSiteWisePaymentNo = objPaymentProcessDao.getSiteWisePaymentNo(objPaymentBean.getStrSiteId());
					objPaymentBean.setIntSiteWisePaymentId(intSiteWisePaymentNo);
					int PaymentEntryDetailsSeqID = objPaymentProcessDao.getPaymentDetailsId();
					objPaymentProcessDao.insertInACC_PAYMENT_DTLS(objPaymentBean, paymentId,PaymentEntryDetailsSeqID);
					objPaymentBean.setIntPaymentDetailsId(PaymentEntryDetailsSeqID);
					/*************/
					int accDeptPaymentProssSeqNo = objPaymentProcessDao.getAccDeptPaymentProssSeqNo();
					objPaymentProcessDao.insertInACC_ACCOUNTS_DEPT_PMT_PROSS(objPaymentBean,accDeptPaymentProssSeqNo);
					objPaymentBean.setIntAccDeptPaymentProcessId(accDeptPaymentProssSeqNo);
					/*************/
					int TempPaymentId = objPaymentProcessDao.getAccTempPaymentTransactionSeqNo();
					objPaymentProcessDao.insertInACC_TEMP_PAYMENT_TRANSACTIONS(objPaymentBean,TempPaymentId);
					objPaymentBean.setIntTempPaymentTransactionId(TempPaymentId);
					/*************/
					int intPaymentTransactionId = objPaymentProcessDao.getIntPaymentTransactionId();
					objPaymentProcessDao.insertInACC_PAYMENT_TRANSACTIONS(objPaymentBean,intPaymentTransactionId);
					/*************/

					if(objPaymentBean.getPaymentType().equalsIgnoreCase("DIRECT")){
						objPaymentProcessDao.insertInvoiceHistory(objPaymentBean,intPaymentTransactionId);
					}
					/*************/
					if(objPaymentBean.getPaymentType().equalsIgnoreCase("ADVANCE")){
						objPaymentProcessDao.insertPoHistory(objPaymentBean);
					}
					/*************/
					if(objPaymentBean.getPaymentType().equalsIgnoreCase("ADVANCE")){
						objPaymentProcessDao.insertPaidAmountInAdvancePaymentPoTable(objPaymentBean);
					}
					/*************/
					if(objPaymentBean.getPaymentType().equalsIgnoreCase("DIRECT")){
						if(objPaymentBean.getDoubleAdjustAmountFromAdvance()>0){
							objPaymentProcessDao.updateRemainingAdvanceAmountInAdvancePaymentPoTable(objPaymentBean);
						}
					}
					/*************/
					objPaymentProcessDao.setInactiveAccPaymentAfterCheck(paymentId);
					//successList.add("PaymentDetailsId: "+String.valueOf(PaymentEntryDetailsSeqID)+" ,"+"InvoiceNumber: "+objPaymentBean.getStrInvoiceNo()+"  => Success");
					successList.add("Updated Payment Request for Invoice No: "+(StringUtils.isNotBlank(objPaymentBean.getStrInvoiceNo())?objPaymentBean.getStrInvoiceNo():"-")+" with Payment Details Id: "+String.valueOf(PaymentEntryDetailsSeqID)+".");
					
				}
				/************/
				
			}catch(Exception e){
				objDedupTransactionHandler.deleteTransaction(trankey, session);
				successList.add("Failed. Exception Occured");
				e.printStackTrace();
			}

			return successList;

		}
		
		@Override
		public List<PurchaseTaxReportBean> getPurchaseTaxReport(HttpServletRequest request, ModelAndView model) {
			List<PurchaseTaxReportBean> list = new ArrayList<PurchaseTaxReportBean>();
			
			String invoiceFromDate = "";
			String invoiceToDate = "";
			String grnFromDate = "";
			String grnToDate = "";
			String vendorId = "";
			String vendorName = "";
			String siteIds = "";
			String siteAddress = "";
			
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			if(!selectAll.equals("true")){
				invoiceFromDate = request.getParameter("invoiceFromDate");
				invoiceToDate = request.getParameter("invoiceToDate");
				grnFromDate = request.getParameter("grnFromDate");
				grnToDate = request.getParameter("grnToDate");
				vendorId = request.getParameter("vendorId");
				vendorName = request.getParameter("vendorName");
				if(StringUtils.isBlank(vendorName)){vendorId="";}
				siteIds = request.getParameter("siteIds");
				siteAddress = request.getParameter("siteAddress");
			}
			
			model.addObject("invoiceFromDate",invoiceFromDate);
			model.addObject("invoiceToDate", invoiceToDate);
			model.addObject("grnFromDate",grnFromDate);
			model.addObject("grnToDate", grnToDate);
			model.addObject("vendorId",vendorId);
			model.addObject("vendorName",vendorName);
			model.addObject("siteIds",siteIds);
			model.addObject("siteAddress",siteAddress);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("invoiceFromDate",invoiceFromDate);
			map.put("invoiceToDate", invoiceToDate);
			map.put("grnFromDate",grnFromDate);
			map.put("grnToDate", grnToDate);
			map.put("vendorId",vendorId);
			map.put("vendorName",vendorName);
			map.put("siteIds", siteIds);
			map.put("siteAddress", siteAddress);
			
			if(selectAll.equals("true")||CommonUtilities.returnTrueIfAnyValueInMapIsNotBlank(map)){
				list = objPaymentProcessDao.getPurchaseTaxReport(map);
			}
			else{
				model.addObject("firstRequest", true);
			}
			return list;
		}
		@Override
		public void setExtraDetailsForExcel(HttpServletRequest request, HashMap<String, Object> extraDataMap) {
			String siteAddress = request.getParameter("siteAddress");
			String tinNo = UIProperties.validateParams.getProperty(siteAddress+"_TIN_NO") == null ? "" : UIProperties.validateParams.getProperty(siteAddress+"_TIN_NO").toString();
			String gtinNo = UIProperties.validateParams.getProperty(siteAddress+"_GTIN_NO") == null ? "" : UIProperties.validateParams.getProperty(siteAddress+"_GTIN_NO").toString();
			extraDataMap.put("tinNo", tinNo);
			extraDataMap.put("gtinNo", gtinNo);
			
			String invoiceFromDate = "";
			String invoiceToDate = "";
			String grnFromDate = "";
			String grnToDate = "";
			
			
				invoiceFromDate = request.getParameter("invoiceFromDate");
				invoiceToDate = request.getParameter("invoiceToDate");
				grnFromDate = request.getParameter("grnFromDate");
				grnToDate = request.getParameter("grnToDate");
			
			
			String dates = "";
			
			if (StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)) {
				dates = "From "+invoiceFromDate+" to "+invoiceToDate;
			} else if (StringUtils.isNotBlank(invoiceFromDate)) {
				dates = "Date :"+invoiceFromDate;
			} else if(StringUtils.isNotBlank(invoiceToDate)) {
				dates = "Till Date :"+invoiceToDate;
			}
			
			if (StringUtils.isNotBlank(grnFromDate) && StringUtils.isNotBlank(grnToDate)) {
				dates = "From "+grnFromDate+" to "+grnToDate;
			} else if (StringUtils.isNotBlank(grnFromDate)) {
				dates = "Date :"+grnFromDate;
			} else if(StringUtils.isNotBlank(grnToDate)) {
				dates = "Till Date :"+grnToDate;
			}
			
			extraDataMap.put("dates", dates);
			
			
			
		}
		
		
		
		@Override
		public List<ContractorTaxReportBean> getContractorTaxReport(HttpServletRequest request, ModelAndView model) {
			List<ContractorTaxReportBean> list = new ArrayList<ContractorTaxReportBean>();
			
			String billFromDate = "";
			String billToDate = "";
			String ContractorId = "";
			String ContractorName = "";
			String siteIds = "";
			String siteAddress = "";
			
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			if(!selectAll.equals("true")){
				billFromDate = request.getParameter("billFromDate");
				billToDate = request.getParameter("billToDate");
				ContractorId = request.getParameter("ContractorId");
				ContractorName = request.getParameter("ContractorName");
				if(StringUtils.isBlank(ContractorName)){ContractorId="";}
				siteIds = request.getParameter("siteIds");
				siteAddress = request.getParameter("siteAddress");
			}
			
			model.addObject("billFromDate",billFromDate);
			model.addObject("billToDate", billToDate);
			model.addObject("ContractorId",ContractorId);
			model.addObject("ContractorName",ContractorName);
			model.addObject("siteIds",siteIds);
			model.addObject("siteAddress",siteAddress);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("billFromDate",billFromDate);
			map.put("billToDate", billToDate);
			map.put("ContractorId",ContractorId);
			map.put("ContractorName",ContractorName);
			map.put("siteIds", siteIds);
			map.put("siteAddress", siteAddress);
			
			if(selectAll.equals("true")||CommonUtilities.returnTrueIfAnyValueInMapIsNotBlank(map)){
				list = objPaymentProcessDao.getContractorTaxReport(map);
			}
			else{
				model.addObject("firstRequest", true);
			}
			return list;
		}
		
		@Override
		public void setExtraDetailsForContractorTaxReportExcel(HttpServletRequest request, HashMap<String, Object> extraDataMap) {
			String siteAddress = request.getParameter("siteAddress");
			String tinNo = UIProperties.validateParams.getProperty(siteAddress+"_TIN_NO") == null ? "" : UIProperties.validateParams.getProperty(siteAddress+"_TIN_NO").toString();
			String gtinNo = UIProperties.validateParams.getProperty(siteAddress+"_GTIN_NO") == null ? "" : UIProperties.validateParams.getProperty(siteAddress+"_GTIN_NO").toString();
			extraDataMap.put("tinNo", tinNo);
			extraDataMap.put("gtinNo", gtinNo);
			
			String billFromDate = "";
			String billToDate = "";
			
			
			billFromDate = request.getParameter("billFromDate");
			billToDate = request.getParameter("billToDate");
				
			
			String dates = "";
			
			if (StringUtils.isNotBlank(billFromDate) && StringUtils.isNotBlank(billToDate)) {
				dates = "From "+billFromDate+" to "+billToDate;
			} else if (StringUtils.isNotBlank(billFromDate)) {
				dates = "Date :"+billFromDate;
			} else if(StringUtils.isNotBlank(billToDate)) {
				dates = "Till Date :"+billToDate;
			}
			
			extraDataMap.put("dates", dates);
			
			
			
		}
	
}

//save site approval code
/*if(invoiceAmount>=tmpRequestedAmount){


	amtAfterModified=tmpactualRequestedAmount-tmpRequestedAmount;

	if(amtAfterModified<0){

		amtAfterModified=Math.abs(amtAfterModified);
		tmpactualRequestedAmount +=amtAfterModified;
		objPaymentProcessDao.UpDatePaymentDetails(paymentDetailsId,tmpactualRequestedAmount);

	}else if(amtAfterModified>0){

		tmpactualRequestedAmount =(tmpactualRequestedAmount)-(amtAfterModified); 
		objPaymentProcessDao.UpDatePaymentDetails(paymentDetailsId,tmpactualRequestedAmount);
	}

}*/
