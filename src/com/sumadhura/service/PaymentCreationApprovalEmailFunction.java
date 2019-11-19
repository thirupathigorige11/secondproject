package com.sumadhura.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.PaymentDto;
import com.sumadhura.transdao.PaymentProcessDao;
import com.sumadhura.transdao.PaymentProcessDaoImpl;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.UIProperties;

public class PaymentCreationApprovalEmailFunction extends UIProperties {

	private JdbcTemplate jdbcTemplate;
	public PaymentCreationApprovalEmailFunction(JdbcTemplate template) {

		this.jdbcTemplate = template;
	}

	@Autowired
	PaymentProcessDao objPaymentProcessDao = new PaymentProcessDaoImpl();
	


	public List<String> approvePaymentFromMail( HttpServletRequest request,HttpServletResponse hs_response,String site_id, String strUserId,String groupOfPaymentDetailsId,String strOperationType,String session_siteId) throws ServletException, IOException {
		
		objPaymentProcessDao.setJdbcTemplate(jdbcTemplate);
		List<PaymentBean> list = objPaymentProcessDao.getPaymentApprovalDetailsForMail(site_id, strUserId, groupOfPaymentDetailsId);
		
		//checking is there any already approved?
		/*List<String> alreadyApprovedList = Arrays.asList(groupOfPaymentDetailsId.split(","));
		*/
		List<String> alreadyApprovedList = new ArrayList<String>();
		Collections.addAll(alreadyApprovedList, groupOfPaymentDetailsId.split("\\s*,\\s*"));
		for(PaymentBean objPB : list){
			alreadyApprovedList.remove(String.valueOf(objPB.getIntPaymentDetailsId()));
		}
		List<String> successList = new ArrayList<String>();
		Map<String,PaymentBean> mapOfInvNoAndPoNo = null;
		if(alreadyApprovedList.size()>0){
			mapOfInvNoAndPoNo = objPaymentProcessDao.getInvoiceNoAndPoNoByPaymentDtlsId(alreadyApprovedList);
		}
		for(String paymentDetailsId : alreadyApprovedList){
			PaymentBean objPaymentBean = mapOfInvNoAndPoNo.get(paymentDetailsId);
			String invoiceNo = objPaymentBean.getStrInvoiceNo();
			String poNo = objPaymentBean.getStrPONo();
			successList.add("Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+" is Already Approved (or) Rejected.AlreadyApproved");
		}
		/********************************************/
		if(strOperationType.equals("Edit")){
			if(list.size()>0){
				String homePage = request.getParameter("homePage");
				hs_response.sendRedirect(homePage);
			}
		}
		/********************************************/
		PaymentBean  objPaymentBean = null;
		final List<PaymentBean> SuccessDataListToMail = new ArrayList<PaymentBean>();
		final Map<String,List<PaymentBean>> PaymentRejectMapToMail = new HashMap<String,List<PaymentBean>>();
		TransactionDefinition def = new DefaultTransactionDefinition();
		//	TransactionStatus status = transactionManager.getTransaction(def);
		String actualRequestedAmount="";
		String actualRequestedDate="";
		String changedRequestedAmount="";
		String changedRequestedDate="";
		String comments="";
		//	String strStatus="";
		int intTotalNoOfRecords =list.size();
		String strPaymentIntiateType=strOperationType;
		String paymentDetailsId="";
		String pendingEmpId="";
		String paymentId="";
		double tmpactualRequestedAmount=0.0;
		double tmpRequestedAmount=0.0;
		boolean isRowChanged = false;

		double amtAfterModified=0.0;
		double invoiceAmount=0.0;
		String response="success";
		String pendingDeptId="";
		String receivedDate="";
		String remarks="";
		String reqSiteId="";
		double doubleAdjustAmountFromAdvance=0.0;
		double actualDoubleAdjustAmountFromAdvance=0.0;
		String poNo="";
		String paymentType="";
		double poAmount=0.0;
		String invoiceNo="";
		String strVendorName="";
		
		//String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
		//	String accountsDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
		String site_name = objPaymentProcessDao.getSiteNameBySiteId(site_id);
		String user_name = objPaymentProcessDao.getEmpNameByEmpId(strUserId);
		
		try{

			pendingEmpId=objPaymentProcessDao.getPendingEmpId(strUserId,session_siteId);

			if(pendingEmpId.equals("-")){

				pendingDeptId=objPaymentProcessDao.getDeptId(strUserId,session_siteId);
			}

			//intTotalNoOfRecords = Integer.parseInt(request.getParameter("listTotalInvoices") == null ? "0" : request.getParameter("listTotalInvoices").toString());
			//	if(accountsDeptId.equals(deptEmpId)){

			if(intTotalNoOfRecords > 0){
				for(int i = 0;i<intTotalNoOfRecords;i++){
					//strPaymentIntiateType = request.getParameter("paymentIntiateType"+i);
					if(strPaymentIntiateType.equalsIgnoreCase("Approve")||strPaymentIntiateType.equalsIgnoreCase("Reject")){
						objPaymentBean = new PaymentBean();
						changedRequestedAmount=list.get(i).getRequestedAmount();//request.getParameter("RequestedAmt"+i);

						changedRequestedDate=list.get(i).getRequestedDate();//request.getParameter("RequestedDate"+i);
						comments="";
						paymentDetailsId=String.valueOf(list.get(i).getIntPaymentDetailsId());
						paymentId=String.valueOf(list.get(i).getIntPaymentId());
						actualRequestedAmount=list.get(i).getRequestedAmount();
						actualRequestedDate=list.get(i).getRequestedDate();
						invoiceAmount = list.get(i).getDoubleInvoiceAmount();
						poAmount = list.get(i).getDoublePOTotalAmount();
						receivedDate=list.get(i).getStrReceiveDate();
						remarks=list.get(i).getStrRemarks();
						reqSiteId=list.get(i).getStrSiteId();
						doubleAdjustAmountFromAdvance=list.get(i).getDoubleAdjustAmountFromAdvance();
						actualDoubleAdjustAmountFromAdvance=list.get(i).getDoubleAdjustAmountFromAdvance();//Double.valueOf(request.getParameter("actualAdjustAmountFromAdvance"+i));
						poNo = list.get(i).getStrPONo();//request.getParameter("poNo"+i);
						invoiceNo = list.get(i).getStrInvoiceNo();//request.getParameter("invoiceNo"+i);
						paymentType = list.get(i).getPaymentType();//request.getParameter("paymentType"+i);
						strVendorName =  list.get(i).getStrVendorName();//request.getParameter("vendorName"+i);

						if(remarks !=null && !remarks.equals("")){
							if(StringUtils.isNotBlank(comments)){
								remarks = remarks + "@@@" + comments;
							}
						}else{
							remarks = comments;
						}

						String remainingAmountInAdvanceInDB = objPaymentProcessDao.getRemainingAmountInAdvance(poNo);
						if((doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance)>Double.valueOf(remainingAmountInAdvanceInDB)){
							//successList.add("Failed");
							successList.add("Failed to "+strPaymentIntiateType+" Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".Failed");
							continue;
						}

						if(paymentType.equals("DIRECT")){
							double paymentReqUpto = list.get(i).getDoublePaymentRequestedUpto();
							double paymentDoneUpto = list.get(i).getDoublePaymentDoneUpto();

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(changedRequestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>invoiceAmount){
								//successList.add("Failed");
								successList.add("Failed to "+strPaymentIntiateType+" Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".Failed");
								continue;}
						}
						if(paymentType.equals("ADVANCE")){
							double paymentReqUpto = list.get(i).getDoublePaymentRequestedUpto();
							double paymentDoneUpto = list.get(i).getDoublePaymentDoneUpto();

							if((paymentReqUpto+paymentDoneUpto+(Double.valueOf(changedRequestedAmount)-Double.valueOf(actualRequestedAmount))+(doubleAdjustAmountFromAdvance-actualDoubleAdjustAmountFromAdvance))>poAmount){
								//successList.add("Failed");
								successList.add("Failed to "+strPaymentIntiateType+" Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".Failed");
								continue;}
						}


						tmpactualRequestedAmount=Double.parseDouble(actualRequestedAmount);
						tmpRequestedAmount=Double.parseDouble(changedRequestedAmount);
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
						SimpleDateFormat format1 = new SimpleDateFormat("dd-M-y");
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


						if(strPaymentIntiateType.equalsIgnoreCase("Approve")){
							if(StringUtils.isNotBlank(pendingEmpId) && !pendingEmpId.equals("-")){
								int intResponse=0;

								intResponse=objPaymentProcessDao.updateRequestedPaymentEmpId(paymentDetailsId,pendingEmpId);	 


							} else if(StringUtils.isNotBlank(pendingDeptId) && !pendingDeptId.equals("-")){

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
								objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPO(poNo,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);
							}
							if((!actualRequestedAmount.equals(changedRequestedAmount))||(doubleAdjustAmountFromAdvance!=actualDoubleAdjustAmountFromAdvance)){

								objPaymentProcessDao.updateReqUptoInAccPayment(paymentId,actualRequestedAmount,changedRequestedAmount,actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance);

							}

							objPaymentProcessDao.saveApprovalDetails(paymentId,strUserId,comments,paymentDetailsId,site_id);
							if(isRowChanged){

								objPaymentProcessDao.saveChangedDetails(objPaymentBean,"M"); 


							}
							successList.add("Approved Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".Success");
						}
						else if(strPaymentIntiateType.equalsIgnoreCase("Reject")){

							objPaymentProcessDao.saveRejectDetails(paymentId,strUserId,comments,paymentDetailsId,site_id);

							objPaymentProcessDao.updateReqUptoInAccPaymentOnReject(paymentId,actualRequestedAmount,actualDoubleAdjustAmountFromAdvance);

							objPaymentProcessDao.updateIntiateAmountInAdvancePaymentPOOnReject(poNo,actualDoubleAdjustAmountFromAdvance);

							PaymentBean objPB = new PaymentBean();
							objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
							objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
							objPB.setStrPONo(poNo.equals("")?"-":poNo);
							objPB.setDoubleAmountToBeReleased(Double.valueOf(changedRequestedAmount));
							objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
							objPB.setStrSiteName(site_name.equals("")?"-":site_name);
							objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
							objPB.setStrRemarks(comments.equals("")?"-":comments);
							if(PaymentRejectMapToMail.containsKey(reqSiteId)){
								PaymentRejectMapToMail.get(reqSiteId).add(objPB);
							}
							else{
								List<PaymentBean> list2 = new ArrayList<PaymentBean>();
								list2.add(objPB);
								PaymentRejectMapToMail.put(reqSiteId,list2);
							}
							successList.add("Rejected Payment Request for "+(StringUtils.isNotBlank(invoiceNo)?("Invoice No: "+invoiceNo):("PO No: "+poNo))+" with Payment Details Id: "+String.valueOf(paymentDetailsId)+".Success");

						}
						
						if(strPaymentIntiateType.equalsIgnoreCase("Approve")){
							PaymentBean objPB = new PaymentBean();
							objPB.setIntPaymentDetailsId(Integer.parseInt(paymentDetailsId));
							objPB.setStrInvoiceNo(invoiceNo.equals("")?"-":invoiceNo);
							objPB.setStrPONo(poNo.equals("")?"-":poNo);
							objPB.setDoubleAmountToBeReleased(Double.valueOf(changedRequestedAmount));
							objPB.setStrVendorName(strVendorName.equals("")?"-":strVendorName);
							objPB.setStrSiteName(site_name.equals("")?"-":site_name);
							objPB.setPaymentType(paymentType.equals("")?"-":paymentType);
							objPB.setStrRemarks(comments.equals("")?"-":comments);
							SuccessDataListToMail.add(objPB);
						}
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



		}catch(Exception e){
			successList.add("Exception");
			response="failure";

			e.printStackTrace();
		}
		final String user_id_final=strUserId;
		final String user_name_final=user_name;
		final String strSiteId_final=reqSiteId;
		final String session_siteId_final=session_siteId;
		final String pendingEmpId_final=pendingEmpId;
		final String pendingDeptId_final=pendingDeptId;
		final int getLocalPort = request.getLocalPort();
		
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
						objEmailFunction.sendPaymentApprovalMailToAccountsAllLevels(SuccessDataListToMail,emailToAddress,getLocalPort,user_name_final);
						
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
							String [] emailToAddress = getAllSiteLevelEmails(entry.getKey());
							String EmailBodyMessage = "This payment(s) Rejected in Site Level";
							objEmailFunction.sendPaymentRejectedStatusMail(entry.getValue(), emailToAddress,getLocalPort,user_name_final,EmailBodyMessage );
						}
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

	private String[] getAllSiteLevelEmails(String siteId) {
		return objPaymentProcessDao.getAllSiteLevelEmails(siteId);
	}
	protected String[] getEmailsOfEmpByDeptId(String pendingDeptId) {
		return objPaymentProcessDao.getEmailsOfEmpByDeptId(pendingDeptId);
	}
	private String[] getPaymentApproverEmailId(String strUserId,String session_siteId_final) {
		return objPaymentProcessDao.getPaymentApproverEmailId(strUserId,session_siteId_final);

	}


	
}
