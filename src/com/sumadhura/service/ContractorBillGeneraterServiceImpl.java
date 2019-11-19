package com.sumadhura.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.in.ContractorBillGenerateController;
import com.sumadhura.transdao.ContrctorBillGeneraterDao;

@Service("wobill_generater_service")
public class ContractorBillGeneraterServiceImpl implements ContrctorBillGeneraterService {
	static Logger log = Logger.getLogger(ContractorBillGeneraterServiceImpl.class);
	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("wo_bill_generator_repo")
	ContrctorBillGeneraterDao workOrderBillDao;

	/**
	 * @throws Exception 
	 * @description this controller used to store the Advance bill details like a payment abstract area,work order number, bill date, contractor name
	 */
	@Override
	public String generateAdvancePaymentBill(HttpServletRequest request, HttpSession session) throws Exception {
		String response = "";
		String advBillInvoiceNo="";
		String state="";
		boolean isSendMail = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order bill generation, ");
		ContractorQSBillBean bean = null;
		WorkOrderBean workOrderBean = null;
		try {
			bean = new ContractorQSBillBean();
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String contractorId = request.getParameter("ContractorId");
			String workOrderNo = request.getParameter("workOrderNo").split("\\$")[0];
			String advanceAmount = request.getParameter("advanceAmount");
			String typeOfWork = request.getParameter("typeOfWork");
			
			String billDate = request.getParameter("billDate");// Date format from HTML 2018-07-24
			String releaseAdvTotalAmt = request.getParameter("releaseAdvTotalAmt");
			String releaseAdvPrevAmt = request.getParameter("releaseAdvPrevAmt");
			String billInvoiceNo= request.getParameter("billInvoiceNo") == null ? "" : request.getParameter("billInvoiceNo");
			
			String outstandingAdvPrevAmt = request.getParameter("outstandingAdvPrevAmt");
			String outstandingAdvTotalAmt = request.getParameter("outstandingAdvTotalAmt");
			String paybleAmt = request.getParameter("finalAmt");

			String totalAmtCumulativeCertified = request.getParameter("totalAmtCumulativeCertified");
			String totalAmtPreviousCertified = request.getParameter("totalAmtPreviousCertified");
			String totalAmount = request.getParameter("totalAmtToPay");
			String approverEmpId = request.getParameter("nextLevelApprovelEmpID");
			String remarks = request.getParameter("remarks");
			
			String isOldOrNewBill = request.getParameter("isOldOrNewBill") == null ? "" : request.getParameter("isOldOrNewBill");
			String oldBillNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
			
			String paymentTYpeOfWork="";
			String paymentTYpeOfWork1[]=request.getParameterValues("paymentTypeOfWork");
			if(paymentTYpeOfWork1!=null)
			for (String workType : paymentTYpeOfWork1) {
				paymentTYpeOfWork+=workType+"#";
			}
			
			bean.setPaymentTYpeOfWork1(paymentTYpeOfWork);
			//checking here the permanent bill number manually entered or not
			//if entered we need to store this permanent bill number
			if(isOldOrNewBill.equals("Old")&&oldBillNo.length()!=0){
				String str=oldBillNo.split("/")[1];
				int num=Integer.valueOf(str);
				String formatted = String.format("%02d", num);
				oldBillNo="ADV/"+formatted;
				bean.setOldBillNo(oldBillNo);
			}else{
				String permanentBillNo = request.getParameter("advBillNo") == null ? "" : request.getParameter("advBillNo");
				bean.setOldBillNo(permanentBillNo);
			}
			
			StringBuffer billLeadingPart=new StringBuffer("");
			if(billInvoiceNo.length()!=0){
				String[] billNumberParts=billInvoiceNo.split("/");
				int num=Integer.valueOf(billNumberParts[billNumberParts.length-1]);
				String formatted = String.format("%03d", num);
				for (int i = 0; i < billNumberParts.length-1; i++) {
					billLeadingPart.append(billNumberParts[i]+"/");
				}
			 	billInvoiceNo=billLeadingPart+formatted;
			}
			
			bean.setPermanentBillNo(bean.getOldBillNo());
			bean.setBillInvoiceNumber(billInvoiceNo);
			bean.setApproverEmpId(approverEmpId);
			bean.setFromEmpId(userId);
			bean.setSiteId(siteId);
			bean.setUserId(userId);
			bean.setContractorId(contractorId);
			bean.setWorkOrderNo(workOrderNo);
			bean.setAdvanceBillAmt(advanceAmount);
			bean.setTypeOfWork(typeOfWork);
			bean.setBillDate(billDate);
			bean.setReleaseAdvTotalAmt(releaseAdvTotalAmt);
			bean.setReleaseAdvPrevAmt(releaseAdvPrevAmt);
			bean.setOutstandingAdvPrevAmt(outstandingAdvPrevAmt);
			bean.setOutstandingAdvTotalAmt(outstandingAdvTotalAmt);
			bean.setPaybleAmt(paybleAmt);
			bean.setTotalAmount(totalAmount);
			bean.setTotalAmtCumulativeCertified(totalAmtCumulativeCertified);
			bean.setTotalAmtPreviousCertified(totalAmtPreviousCertified);
			bean.setRemarks(remarks);
			bean.setPaymentType("ADV");
			List<Object> response1 = workOrderBillDao.generateAdvanceRAPaymentBill(bean);
			int tempBllSeq = Integer.parseInt(String.valueOf(response1.get(0)));
			request.setAttribute("AdvBillNo", bean.getOldBillNo());
			request.setAttribute("TempAdvBillNo",tempBllSeq);

			String[] workAreaId = request.getParameterValues("workAreaId");
			String[] qty = request.getParameterValues("qty");
			String[] price = request.getParameterValues("price");
			String[] initiatedArea = request.getParameterValues("initiatedQunatity");

			String[] WOQuantityRateArray= request.getParameterValues("WOQuantityRate");
			//String[] totalWOQuantityArray= request.getParameterValues("totalWOQuantity");
			String[] wo_work_issue_area_dtls_id=request.getParameterValues("wo_work_issue_area_dtls_id");
			
			//String[] actualWorkAreaId = request.getParameterValues("actualWorkAreaId");
			String[] actualQty = request.getParameterValues("ActualQTY");

			bean.setTempBillNo(String.valueOf(tempBllSeq));
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			int result = workOrderBillDao.insertTempBillApprRejDetail(bean, "C");
			if (workAreaId != null && qty != null) {
				//Inserting all the payment abstract details
				result = workOrderBillDao.insertPaymentAreaDetails(workAreaId, qty, tempBllSeq, paybleAmt,	"create",
						 actualQty,initiatedArea,WOQuantityRateArray,wo_work_issue_area_dtls_id);
			}
			String s=null;
	//	s.trim();
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			isSendMail = false;
			response = "failed";
			e.printStackTrace();
			if(e.getMessage().equals("missing work order issue id")){
				 throw new Exception("missing work order issue id");
			}
			if(e.getMessage().equals("missing bill number")){
				throw new Exception("missing bill number");
			}
		

		}
		if(isSendMail){
			//constructEmailContent(bean,workOrderBean,request,null,"");
		}
		
		return response;
	}
	
/**
 * @throws Exception 
 * @description this method is used to insert all the details of the RA bill like deduction details
 * RA Payment abstract area details
 * Insetting all the recovery details if any there
 */
	@Override
	public String createWorkOrderRABill(HttpServletRequest request, HttpSession session) throws Exception {
		String response = "";
		boolean isSendMail = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order ra bill generation, ");
		String raBillNo = "";
		String contractorId = "";
		String typeOfWork = "";
		String raBillDate = "";
		String amountToPay = "";
		String totalAmtToPay = "";
		String raDeductionAmt = "";
		String secDepositCurrentCerti = "";
		String pettyExpensesCurrentCerti = "";
		String otherAmout = "";
		String raCumulaticeCerti = "";
		String raPrevCerti = "";
		String totalCurrentCerti = "";
		String totalCurrntDeducAmt = "";
		String approverEmpId = "";
		String workOrderNo = "";
		String currentRecoveryAmount="";
		String oldBillNo="";
		String isOldOrNewBill="";
		String billInvoiceNo="";
		
		ContractorQSBillBean bean = new ContractorQSBillBean();
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
		
			String securityPer= request.getParameter("securityPer") == null ? "" : request.getParameter("securityPer");
			raBillNo = request.getParameter("raBillNo") == null ? "" : request.getParameter("raBillNo");
			billInvoiceNo= request.getParameter("billInvoiceNo") == null ? "" : request.getParameter("billInvoiceNo");
			contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");
			typeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").trim();
			totalAmtToPay = request.getParameter("totalAmtToPay") == null ? ""	: request.getParameter("totalAmtToPay").trim();
			raBillDate = request.getParameter("billDate") == null ? "" : request.getParameter("billDate");
			String remarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
			workOrderNo = request.getParameter("workOrderNo");
			approverEmpId = request.getParameter("nextLevelApprovelEmpID");
			
			totalCurrentCerti = request.getParameter("totalCc") == null ? "" : request.getParameter("totalCc");
			raCumulaticeCerti = request.getParameter("raCc") == null ? "" : request.getParameter("raCc");
			totalCurrntDeducAmt = request.getParameter("totalAmtCurrnt") == null ? ""	: request.getParameter("totalAmtCurrnt");
			raPrevCerti = request.getParameter("raPc") == null ? "" : request.getParameter("raPc");
			raDeductionAmt = request.getParameter("raDeductionAmt") == null ? "0": request.getParameter("raDeductionAmt");
		
			secDepositCurrentCerti = request.getParameter("secDepositCurrentCerti") == null ? "0": request.getParameter("secDepositCurrentCerti");
			pettyExpensesCurrentCerti = request.getParameter("pettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("pettyExpensesCurrentCerti");
			otherAmout = request.getParameter("other") == null ? "0": request.getParameter("other");
			currentRecoveryAmount= request.getParameter("recoverycurrentAmount") == null ? "0": request.getParameter("recoverycurrentAmount");
			amountToPay = request.getParameter("finalAmt") == null ? "" : request.getParameter("finalAmt");
			
			isOldOrNewBill = request.getParameter("isOldOrNewBill") == null ? "" : request.getParameter("isOldOrNewBill");
			oldBillNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
			
			
			String paymentTYpeOfWork="";
			String paymentTYpeOfWork1[]=request.getParameterValues("paymentTypeOfWork");
			if(paymentTYpeOfWork1!=null)
			for (String workType : paymentTYpeOfWork1) {
				paymentTYpeOfWork+=workType+"#";
			}
			
			bean.setPaymentTYpeOfWork1(paymentTYpeOfWork);
			//checking here the permanent bill number manually entered or not
			//if entered we need to store this permanent bill number
			if(isOldOrNewBill.equals("Old")&&oldBillNo.length()!=0){
				String str=oldBillNo.split("/")[1];
				int num=Integer.valueOf(str);
				String formatted = String.format("%02d", num);
				oldBillNo="RA/"+formatted;
				bean.setOldBillNo(oldBillNo);
			}else{
			//if this condition executed means the Bill No is permanent
				String permanentBillNo = request.getParameter("raBillNo") == null ? "" : request.getParameter("raBillNo");
				bean.setOldBillNo(permanentBillNo);
			}
			
			StringBuffer billLeadingPart=new StringBuffer("");
			if(billInvoiceNo.length()!=0){
				String[] billNumberParts=billInvoiceNo.split("/");
				int num=Integer.valueOf(billNumberParts[billNumberParts.length-1]);
				String formatted = String.format("%03d", num);
				for (int i = 0; i < billNumberParts.length-1; i++) {
					billLeadingPart.append(billNumberParts[i]+"/");
				}
			 	billInvoiceNo=billLeadingPart+formatted;
			}
			
			//if the values is empty we need to add zero 
			secDepositCurrentCerti=secDepositCurrentCerti==""?"0":secDepositCurrentCerti;
			pettyExpensesCurrentCerti=pettyExpensesCurrentCerti==""?"0":pettyExpensesCurrentCerti;
			otherAmout=otherAmout==""?"0":otherAmout;
			currentRecoveryAmount=currentRecoveryAmount==""?"0":currentRecoveryAmount;
			raDeductionAmt=raDeductionAmt==""?"0":raDeductionAmt;
			bean.setPermanentBillNo(bean.getOldBillNo());
			bean.setBillInvoiceNumber(billInvoiceNo);
			bean.setRecovery_amount(currentRecoveryAmount);
			bean.setFromEmpId(userId);
			bean.setSiteId(siteId);
			bean.setRaCumulaticeCerti(raCumulaticeCerti);
			bean.setRaPrevCerti(raPrevCerti);
			bean.setSecDepositCurrentCerti(secDepositCurrentCerti);
			bean.setPettyExpensesCurrentCerti(pettyExpensesCurrentCerti);
			bean.setOtherAmout(otherAmout);
			bean.setTotalCurrentCerti(totalCurrentCerti);
			bean.setTotalCurrntDeducAmt(totalCurrntDeducAmt);
			bean.setPaybleAmt(amountToPay);
			bean.setSecurityDepPer(securityPer);
			bean.setRaBillNo(raBillNo);
			bean.setRemarks(remarks);
			bean.setContractorId(contractorId);
			bean.setTypeOfWork(typeOfWork);
			bean.setBillDate(raBillDate);
			
			bean.setPaymentType("RA");
			bean.setTotalAmtToPay(totalAmtToPay);
			bean.setRaDeductionAmt(raDeductionAmt);
			bean.setWorkOrderNo(workOrderNo);
			bean.setApproverEmpId(approverEmpId);
			bean.setUserId(userId);
			// generating advance payment bill and RA Bill
			List<Object> response1 = workOrderBillDao.generateAdvanceRAPaymentBill(bean);
			int	result=0;
			int tempBillNo = Integer.parseInt(String.valueOf(response1.get(0)));
			bean.setTempBillNo(String.valueOf(tempBillNo));
			//adding permanent bill number in request object so we can access this attribute in controller and can show in success message
			request.setAttribute("RABillNo", bean.getOldBillNo());
			//adding temporary bill number in request object so we can access this attribute in controller and can show in success message
			request.setAttribute("TempRABillNo",tempBillNo);
			
			//getting number of recovery rows to be process
			int rowsToIterate=Integer.valueOf(request.getParameter("rowsToIterate1")==null?0:Integer.valueOf( request.getParameter("rowsToIterate1")));
			
			for (int j = 1; j <= rowsToIterate; j++) {
				ContractorQSBillBean billBean=new ContractorQSBillBean();
				String child_product_id =request.getParameter("child_product_id"+j);
				String childProdName=request.getParameter("childProdName"+j);
				String measurement_id=request.getParameter("measurement_id"+j);
				String recovery_amount=request.getParameter("recovery_amount"+j);
				String recovery_quantity=request.getParameter("recovery_quantity"+j);
				billBean.setChild_product_id(child_product_id);
				billBean.setMeasurement_id(measurement_id);
				billBean.setRecovery_amount(recovery_amount);
				billBean.setChildProdName(childProdName);
				billBean.setRecovery_quantity(recovery_quantity);
				billBean.setWorkOrderNo(workOrderNo.split("\\$")[0].trim());
				billBean.setTempBillNo(String.valueOf(tempBillNo));
				billBean.setSiteId(siteId.trim());
				//inserting all the work order recovery details 
				result=workOrderBillDao.insertIntoBoqRecovery(billBean);
				result =workOrderBillDao.insertIntoBoqRecoveryHistory(billBean);
				System.out.println(result);
			}
			
			//holding all the work area id
			String[] workAreaIdArray = request.getParameterValues("workAreaId");
			//holding the actual quantity
			String[] qtyArray = request.getParameterValues("qty");
			//holding initiated quantity
			String[] initiatedAreaArray= request.getParameterValues("initiatedQunatity");
			String[] actualQtyArray = request.getParameterValues("ActualQTY");
			//holding rate
			String[] WOQuantityRateArray= request.getParameterValues("WOQuantityRate");
			//String[] totalWOQuantityArray= request.getParameterValues("totalWOQuantity");
			
			String[] wo_work_issue_area_dtls_id=request.getParameterValues("wo_work_issue_area_dtls_id");
			
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			 result = workOrderBillDao.insertTempBillApprRejDetail(bean,"C");
			// inserting payment area details to work
			if (workAreaIdArray != null && qtyArray != null) {
				//Inserting all the payment abstract area details
				 result = workOrderBillDao.insertPaymentAreaDetails(workAreaIdArray, qtyArray, tempBillNo, amountToPay,"create",
						 actualQtyArray,initiatedAreaArray,WOQuantityRateArray,wo_work_issue_area_dtls_id);
			}
			
			//String s=null;s.trim();
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			if(e.getMessage().equals("missing work order issue id")){
				 throw new Exception("missing work order issue id");
			}
			if(e.getMessage().equals("missing bill number")){
				throw new Exception("missing bill number");
			}
			isSendMail = false;
		}
		return response;
	}
	// *********************************************RA Bill Creation Method

	

	@Override
	public List<Map<String, Object>> loadWorkOrderAreaForBill(String contractorId, String workDesc, String workOrderNo,
			String siteId, String userId, String billType) {
		List<Map<String, Object>> list = workOrderBillDao.loadWorkOrderAreaForBill(contractorId, workDesc, workOrderNo,
				siteId, userId, billType);
		return list;
	}

	/**
	 * calling dao layer to get all the bills data by work order no
	 */
	@Override
	public List<Map<String, Object>> getAdvPaymentCertificateDetail(ContractorQSBillBean bean) {
		List<Map<String, Object>> list = workOrderBillDao.getAdvPaymentCertificateDetail(bean);
		return list;
	}

	/**
	 * calling dao layer to return the result of pending bill's of the current contractor
	 */
	@Override
	public boolean isAnyRaAndAdvBillPending(ContractorQSBillBean billBean) {
		return workOrderBillDao.isAnyRaAndAdvBillPending(billBean);
	}
	
	/*@Override
	public List<ContractorQSBillBean> getBillsForUpdate(String siteId, String billType) {
		return workOrderBillDao.getBillsForUpdate(siteId, billType);
	}
*/
	@Override
	public List<ContractorQSBillBean> getPendingWorkOrderBils(String user_id, String siteId) {
		return workOrderBillDao.getPendingWorkOrderBils(user_id, siteId);
	}

	@Override
	public List<ContractorQSBillBean> getPendingWorkOrderContractorBillDetailById(String tmpBillNo, String userId,	String billType, String status) {

		return workOrderBillDao.getPendingWorkOrderContractorBillDetailById(tmpBillNo, userId, billType, status);
	}
	/**
	 * calling dao object to get the site address for printing purpose int bill's or in work order print page
	 */
	@Override
	public List<String> loadSiteAddress(ContractorQSBillBean qsBillBean) {
		return workOrderBillDao.loadSiteAddress(qsBillBean);
	}
	
	@Override
	public List<ContractorQSBillBean> getCompltedWorkOrderContractorBillDetailById(ContractorQSBillBean qsBillBean) {
		return workOrderBillDao.getCompltedWorkOrderContractorBillDetailById(qsBillBean);
	}

	@Override
	public List<Map<String, Object>> loadWorkOrderAreaForApprovelBill(String contractorId, String billType,
			String workOrderNo, String siteId, String userId) {
		List<Map<String, Object>> list = workOrderBillDao.loadWorkOrderAreaForApprovelBill(contractorId, billType,
				workOrderNo, siteId, userId);
		return list;
	}

	@Override
	public List<Map<String, Object>> loadPermanentWorkOrderArea(String tempBillNo, String workDesc, String workOrderNo,
			String siteId, String userId, String billType) {
		return workOrderBillDao.loadPermanentWorkOrderArea(tempBillNo, workDesc, workOrderNo, siteId, userId, billType);
	}

	/**
	 * @description this method will approve the Advance bill and move the bill to the next level if next level is available for the bill,
	 * this method will check the modification happens in approve time and it will maintain the log's of modification in DB 
	 */
	@Override
	public String approveWorkOrderBill(HttpServletRequest request, HttpSession session) {

		boolean isSendMail = false;
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order bill generation, ");
		boolean isDataChanged = false;
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String strUserName = session.getAttribute("UserName") == null ? ""	: session.getAttribute("UserName").toString();
			siteId=request.getParameter("site_id")==null?siteId:request.getParameter("site_id");
			ContractorQSBillBean actualBill = new ContractorQSBillBean();
			ContractorQSBillBean changedBill = new ContractorQSBillBean();
			changedBill.setUserId(userId);
			actualBill.setUserId(userId);
			String advanceCurrAmount = request.getParameter("advanceCurrAmount");
			String actualAdvanceCurrAmount1 = request.getParameter("advanceCurrAmount1");
			String nextLevelApprovelEmpID = request.getParameter("nextLevelApprovelEmpID");
			String tempBillNo = request.getParameter("tempBillNo") == null ? ""	: request.getParameter("tempBillNo").trim();
			String workDesc = request.getParameter("workDesc") == null ? "" : request.getParameter("workDesc").trim();
			String billRemarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks").trim();
			String actualremarks = request.getParameter("actualremarks") == null ? "": request.getParameter("actualremarks").trim();
			String workOrderNo = request.getParameter("workOrderNo") == null ? "": request.getParameter("workOrderNo").trim();
			String permanentBillNo= request.getParameter("permanentBillNo") == null ? "": request.getParameter("permanentBillNo").trim();
			
			actualBill.setPaybleAmt(actualAdvanceCurrAmount1);
			changedBill.setPaybleAmt(advanceCurrAmount);
			
			actualBill.setPermanentBillNo(permanentBillNo);
			changedBill.setPermanentBillNo(permanentBillNo);
			
			actualBill.setTempBillNo(tempBillNo);
			changedBill.setTempBillNo(tempBillNo);
			actualBill.setSiteId(siteId);
			changedBill.setSiteId(siteId);
			actualBill.setWorkOrderNo(workOrderNo);
			changedBill.setWorkOrderNo(workOrderNo);
			actualBill.setPendingEmpId(nextLevelApprovelEmpID);
			changedBill.setPendingEmpId(nextLevelApprovelEmpID);
			changedBill.setPaymentType("ADV");
			actualBill.setPaymentType("ADV");
			changedBill.setRemarks(billRemarks);
			actualBill.setRemarks(billRemarks);
			int result = 0;
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			result = workOrderBillDao.insertTempBillApprRejDetail(changedBill, "A");
			String remarks = "";

			if (!advanceCurrAmount.equals(actualAdvanceCurrAmount1)) {
				isDataChanged = true;
				remarks = strUserName + " changed the actual  payble amount " + actualBill.getPaybleAmt() + " to "
						+ changedBill.getPaybleAmt();
				changedBill.setRemarks(remarks);
				result = workOrderBillDao.saveChangedAdvBillDetails(actualBill, changedBill);
			}
			
			String[] workAreaId = request.getParameterValues("workAreaId");
			String[] qty = request.getParameterValues("qty");
			String[] actualWorkAreaId = request.getParameterValues("actualWorkAreaId");
			String[] initiatedArea = request.getParameterValues("initiatedQunatity");

			String[] WOQuantityRateArray= request.getParameterValues("WOQuantityRate");
			String[] totalWOQuantityArray= request.getParameterValues("totalWOQuantity");
			String[] wo_work_issue_area_dtls_id=request.getParameterValues("wo_work_issue_area_dtls_id");
			String[] actualQty = request.getParameterValues("ActualQTY");

			// adding payment work order area
			if (workAreaId != null && qty != null) {
				//Inserting all the payment abstract area details
				result = workOrderBillDao.insertPaymentAreaDetails(workAreaId, qty, Integer.valueOf(tempBillNo),
						advanceCurrAmount, "approve", actualQty, initiatedArea,WOQuantityRateArray,wo_work_issue_area_dtls_id);
			}

			//giving the bill to next level
			int result2 = workOrderBillDao.updateBillLevel(changedBill);
			request.setAttribute("TempBillNo",tempBillNo);
			request.setAttribute("BillNo",permanentBillNo);
	//	String s=null;s.trim();
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			isSendMail = false;
		}
		return response;
	}

	@Override
	public boolean isValidTempBillNo(String[] data) {
	
		return workOrderBillDao.isValidTempBillNo(data);
	}

	@Override
	public boolean isValidCompltedTempBillNo(String[] data) {
		return workOrderBillDao.isValidCompltedTempBillNo(data);
	}

	/**
	 * @description this method will approve the RA bill and move the bill to the next level if next level is available for the bill,
	 * this method will check the modification happens in approve time and it will maintain the log's of modification in DB 
	 */
	@Override
	public String approveWoRABill(HttpServletRequest request, HttpSession session) {
		String response = "";
		boolean isSendMail = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order ra bill approving, ");
		String raBillNo = "";
		String strUserName = "";
		String contractorId = "";
		String workDescId = "";
		String raBillDate = "";
		String paybleAmt = "";
		String totalAmtToPay = "";
		String raDeductionAmt = "";
		String secDepositCurrentCerti = "";
		String pettyExpensesCurrentCerti = "";
		String otherAmout = "";
		String raCumulaticeCerti = "";
		String raPrevCerti = "";
		String totalCurrentCerti = "";
		String totalCurrntDeducAmt = "";
		String approverEmpId = "";
		String workOrderNo = "";
		String tempBillNo = "";
		String permanentBillNo="";
		String typeOfWork="";
		ContractorQSBillBean bean = new ContractorQSBillBean();
		try {
			strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();

			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			siteId=request.getParameter("site_id")==null?siteId:request.getParameter("site_id");
			ContractorQSBillBean actualBill = new ContractorQSBillBean();
			ContractorQSBillBean changedBill = new ContractorQSBillBean();
			bean.setFromEmpId(userId);
			bean.setSiteId(siteId);
			typeOfWork=request.getParameter("typeOfWork") == null ? ""	: request.getParameter("typeOfWork");
			workOrderNo = request.getParameter("workOrderNo");
			approverEmpId = request.getParameter("nextLevelApprovelEmpID");
			permanentBillNo= request.getParameter("permanentBillNo");
			raBillNo = request.getParameter("raBillNo") == null ? "" : request.getParameter("raBillNo");
			contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");
			workDescId = request.getParameter("workDesc") == null ? "" : request.getParameter("workDesc").trim();
			totalAmtToPay = request.getParameter("totalAmtToPay") == null ? ""	: request.getParameter("totalAmtToPay").trim();
			String remarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
			pettyExpensesCurrentCerti = request.getParameter("pettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("pettyExpensesCurrentCerti");
			otherAmout = request.getParameter("other") == null ? "0": request.getParameter("other");
			String actualPettyExpensesCurrentCerti=request.getParameter("actualPettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("actualPettyExpensesCurrentCerti");
			String actualOtherAmt=request.getParameter("actualOtherAmt") == null ? "" : request.getParameter("actualOtherAmt").trim();
			
			paybleAmt = request.getParameter("finalAmt") == null ? "" : request.getParameter("finalAmt").trim();
			totalCurrentCerti = request.getParameter("totalCc") == null ? "" : request.getParameter("totalCc").trim();
			secDepositCurrentCerti = request.getParameter("secDepositCurrentCerti") == null ? "0"	: request.getParameter("secDepositCurrentCerti");
			raDeductionAmt = request.getParameter("raDeductionAmt") == null ? "0": request.getParameter("raDeductionAmt");
			totalCurrntDeducAmt = request.getParameter("totalActualDeductAmt") == null ? ""	: request.getParameter("totalActualDeductAmt");
			String actualRaAmountToPay = request.getParameter("actualRaAmountToPay") == null ? ""	: request.getParameter("actualRaAmountToPay");
			String ActualCertified= request.getParameter("ActualCertified") == null ? "": request.getParameter("ActualCertified");
			String changedsecDepositCurrentCerti = request.getParameter("changedsecDepositCurrentCerti") == null ? "0": request.getParameter("changedsecDepositCurrentCerti");
			String changedRaDeductionAmt = request.getParameter("changedRaDeductionAmt") == null ? ""	: request.getParameter("changedRaDeductionAmt");
			String actualTotalCurrntDeducAmt = request.getParameter("actualTotalDeductAmt") == null ? ""	: request.getParameter("actualTotalDeductAmt");
			String totalAmtCurntDeduc=request.getParameter("totalAmtCurntDeduc") == null ? ""	: request.getParameter("totalAmtCurntDeduc");
			
			String recoveryCurrentAmount=request.getParameter("recoverycurrentAmount") == null ? ""	: request.getParameter("recoverycurrentAmount");
			String actaulRecoveryCurrentAmount=request.getParameter("actualrecoverycurrentAmount") == null ? ""	: request.getParameter("actualrecoverycurrentAmount");
			
			
			raDeductionAmt=raDeductionAmt==""?"0":raDeductionAmt;
			pettyExpensesCurrentCerti=	pettyExpensesCurrentCerti==""?"0":pettyExpensesCurrentCerti;
			otherAmout=otherAmout==""?"0":otherAmout;
			secDepositCurrentCerti=secDepositCurrentCerti==""?"0.00":secDepositCurrentCerti;
			
			actualBill.setRecovery_amount(actaulRecoveryCurrentAmount);
			changedBill.setRecovery_amount(recoveryCurrentAmount);
			actualBill.setPettyExpensesCurrentCerti(actualPettyExpensesCurrentCerti);
			changedBill.setPettyExpensesCurrentCerti(pettyExpensesCurrentCerti);
			changedBill.setOtherAmout(otherAmout);
			actualBill.setUserId(userId);
			changedBill.setUserId(userId);
			actualBill.setPermanentBillNo(permanentBillNo);
			changedBill.setPermanentBillNo(permanentBillNo);
			actualBill.setOtherAmout(actualOtherAmt);
			actualBill.setWorkOrderNo(workOrderNo);
			
			changedBill.setWorkOrderNo(workOrderNo);
			actualBill.setSiteId(siteId);
			
			changedBill.setSiteId(siteId);
			actualBill.setTempBillNo(tempBillNo);
			changedBill.setTempBillNo(tempBillNo);
			actualBill.setPaybleAmt(paybleAmt);
			changedBill.setPaybleAmt(paybleAmt);
			actualBill.setTotalCurrentCerti(actualRaAmountToPay);
			changedBill.setTotalCurrentCerti(totalCurrentCerti);
			actualBill.setSecDepositCurrentCerti(changedsecDepositCurrentCerti);
			changedBill.setSecDepositCurrentCerti(secDepositCurrentCerti);
			actualBill.setRaDeductionAmt(changedRaDeductionAmt);
			changedBill.setRaDeductionAmt(raDeductionAmt);
			actualBill.setTotalCurrntDeducAmt(actualTotalCurrntDeducAmt);
			changedBill.setTotalCurrntDeducAmt(totalCurrntDeducAmt);
			actualBill.setPaymentType("RA");
			changedBill.setPaymentType("RA");
			actualBill.setApproverEmpId(approverEmpId);
			changedBill.setApproverEmpId(approverEmpId);
			actualBill.setPendingEmpId(approverEmpId);
			changedBill.setPendingEmpId(approverEmpId);
			actualBill.setRemarks(remarks);
			changedBill.setRemarks(remarks);
			int count=0;
			int result =0;
			String	changedDetails="";
			
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			result = workOrderBillDao.insertTempBillApprRejDetail(changedBill,"A");
			
			
			//checking the actual values (means before changing the values) and current values  (means after changing the values) 
			//if they are not equal maintain the log in DB
			if (!raDeductionAmt.equals(changedRaDeductionAmt)) {
				changedDetails += strUserName+" - changed the advance deduction amount "+ actualBill.getRaDeductionAmt() + " to " + changedBill.getRaDeductionAmt() +  "<br>";
				actualBill.setBillWiseAdvanceDeductionAmt(actualBill.getRaDeductionAmt());
				changedBill.setBillWiseAdvanceDeductionAmt(changedBill.getRaDeductionAmt());
				count++;
			}
			if(!secDepositCurrentCerti.equals(changedsecDepositCurrentCerti)){
				changedDetails +=  strUserName+" - changed the security deposit amount "+actualBill.getSecDepositCurrentCerti() + " to " + changedBill.getSecDepositCurrentCerti()+ "<br>";
				
				count++;
			}
			if(!pettyExpensesCurrentCerti.equals(actualPettyExpensesCurrentCerti)){
				changedDetails +=  strUserName+" - changed the petty expenses amount "+actualBill.getPettyExpensesCurrentCerti() + " to " + changedBill.getPettyExpensesCurrentCerti()+ "<br>";
				count++;
			}
			if (!actualOtherAmt.equals(otherAmout)){
				changedDetails +=  strUserName+" - changed the other charges amount "+actualBill.getOtherAmout() + " to " + changedBill.getOtherAmout()+"<br>";
				count++;
			}
			if(!recoveryCurrentAmount.equals(actaulRecoveryCurrentAmount)){
				changedDetails +=  strUserName+" - changed the recovery  amount "+actaulRecoveryCurrentAmount+ " to " + recoveryCurrentAmount+"<br>";
				count++;
			}
			
			// checking is certified amount is changed or not if changed the
			
			if (!totalCurrentCerti.equals(actualRaAmountToPay)) {
					 count++;
					 changedDetails += strUserName+" - changed the Certified amount " + actualBill.getTotalCurrentCerti()	+ " to " + changedBill.getTotalCurrentCerti() + "<br>";
			}
			if(count!=0){
				changedBill.setRemarks(changedDetails);
				 //inserting all the changed details of the bill in DB, so we can show this log's to the next level approval
				 result = workOrderBillDao.saveChangedAdvBillDetails(actualBill, changedBill);
			}

 			//updating the deduction values in DB  
			if(count!=0){
			 result = workOrderBillDao.updateChangedRADeductBillDetails(actualBill, changedBill);
			}
			String[] workAreaId = request.getParameterValues("workAreaId");
			String[] qty = request.getParameterValues("qty");
			String[] price= request.getParameterValues("price");
			String[] initiatedArea= request.getParameterValues("initiatedQunatity");

			String[] WOQuantityRateArray= request.getParameterValues("WOQuantityRate");
			String[] totalWOQuantityArray= request.getParameterValues("totalWOQuantity");
			String[] wo_work_issue_area_dtls_id=request.getParameterValues("wo_work_issue_area_dtls_id");
			
			String[] actualWorkAreaId = request.getParameterValues("actualWorkAreaId");
			String[] actualQty = request.getParameterValues("ActualQTY");

			// adding payment work order area
			if (workAreaId != null && qty != null&&price!=null&&initiatedArea!=null) {
				//inserting payment area details
				int result1 = workOrderBillDao.insertPaymentAreaDetails(workAreaId, qty, Integer.valueOf(tempBillNo),paybleAmt, "approve",
						actualQty,initiatedArea,WOQuantityRateArray,wo_work_issue_area_dtls_id);
			}
			
			//getting number of recovery rows to be process
			int rowsToIterate=Integer.valueOf(request.getParameter("rowsToIterate1")==null?0:Integer.valueOf( request.getParameter("rowsToIterate1")));
			//processing the recovery number of rows
			for (int j = 1; j <= rowsToIterate; j++) {
				ContractorQSBillBean billBean = new ContractorQSBillBean();
				String child_product_id = request.getParameter("child_product_id" + j);
				String childProdName = request.getParameter("childProdName" + j);
				String measurement_id = request.getParameter("measurement_id" + j);
				String recovery_amount = request.getParameter("recovery_amount" + j) == null ? "0": request.getParameter("recovery_amount" + j);
				String recovery_quantity = request.getParameter("recovery_quantity" + j);

				String actualrecovery_amount1 = request.getParameter("recovery_amount1" + j) == null ? "0": request.getParameter("recovery_amount1" + j);
				
				billBean.setChild_product_id(child_product_id);
				billBean.setMeasurement_id(measurement_id);
				billBean.setRecovery_amount(recovery_amount);
				billBean.setRecovery_amount1(recovery_amount);
				billBean.setChildProdName(childProdName);
				billBean.setRecovery_quantity(recovery_quantity);
				billBean.setSiteId(siteId.trim());
				billBean.setWorkOrderNo(workOrderNo.split("\\$")[0]);
				billBean.setTempBillNo(tempBillNo);

				double actualAmount = Double.valueOf(actualrecovery_amount1);
				double currentAmount = Double.valueOf(recovery_amount);
				
				int condition = 0;
				//checking actual values and current values if they are not equal then maintain the log in DB
				//and update the record
				if(actualAmount<currentAmount){
					condition=1;
					String temp=String.valueOf(Math.abs(actualAmount-currentAmount));
					System.out.println(actualAmount+" Actual amount is less than currentAmt "+currentAmount+" "+temp);
					billBean.setRecovery_amount(temp);
				}else if(actualAmount>currentAmount){
					condition=2;
					String temp=String.valueOf(Math.abs(actualAmount-currentAmount));
					System.out.println(actualAmount+" Actual amount is grater than currentAmt "+currentAmount+" "+temp);
					billBean.setRecovery_amount(temp);
				}else{
					condition=0;
					System.out.println("value not changed");
				}
					//updating BOQ recovery details
					result=workOrderBillDao.updateBoqRecovery(billBean,condition);
					result =workOrderBillDao.updateBoqRecoveryHistory(billBean,condition);
			}
			
			//moving current bill to next level approval
			int result2 = workOrderBillDao.updateBillLevel(changedBill);
			//adding permanent bill number into request object so we can access this bill number in controller
			//,and we will show this bill number in success message
			request.setAttribute("BillNo",permanentBillNo);
			
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			isSendMail = false;
		}
		return response;
	}

/**
 * @description this method is used for reject the bill
 * while rejecting bill user need to enter the comments why he is rejecting the bill
 * so we can show the rejection message to all the user's those who are accessing this bill number site wise
 */
	@Override
	public String rejctAdvanceBill(HttpServletRequest request, HttpSession session) {
		String response = "";
		String purpose = "";
		boolean isSendMail = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();

			String changedRemarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
			//String actualBillLevelPurpose = request.getParameter("actualremarks") == null ? "": request.getParameter("actualremarks");
			String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo");
			String 	workOrderNo = request.getParameter("workOrderNo").split("\\$")[0];
			
			ContractorQSBillBean bean=new ContractorQSBillBean();
			bean.setFromEmpId(userId);
			bean.setUserId(userId);
			bean.setSiteId(siteId);
			bean.setWorkOrderNo(workOrderNo);
			bean.setRemarks(changedRemarks);
			bean.setApproverEmpId(userId);
			bean.setTempBillNo(tempBillNo);
			
			WriteTrHistory.write("Site:" + siteId + " , User:" + userId + " , Date:" + new java.util.Date()+ " , RA Bill No:" + tempBillNo);
				
			//getting number of recovery rows to be process
			int rowsToIterate=Integer.valueOf(request.getParameter("rowsToIterate1")==null?0:Integer.valueOf( request.getParameter("rowsToIterate1")));
			/*for (int j = 1; j <= rowsToIterate; j++) {
				ContractorQSBillBean billBean=new ContractorQSBillBean();
				String child_product_id =request.getParameter("child_product_id"+j);
				String childProdName=request.getParameter("childProdName"+j);
				String measurement_id=request.getParameter("measurement_id"+j);
				String recovery_amount=request.getParameter("recovery_amount"+j);
				String recovery_quantity=request.getParameter("recovery_quantity"+j);
				billBean.setChild_product_id(child_product_id);
				billBean.setMeasurement_id(measurement_id);
				billBean.setRecovery_amount(recovery_amount.trim());
				billBean.setChildProdName(childProdName);
				billBean.setRecovery_quantity(recovery_quantity);
				billBean.setWorkOrderNo(workOrderNo.trim());
				billBean.setTempBillNo(String.valueOf(tempBillNo.trim()));
				billBean.setSiteId(siteId);
				int result=0;
				result=workOrderBillDao.removeAmountFromBoqRecovery(billBean);
				result =workOrderBillDao.deleteFromBoqRecoveryHistory(billBean);
			}*/
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			int result = workOrderBillDao.insertTempBillApprRejDetail(bean,"R");
			//this method is used for rejecting Both RA/ADV bill 
			 result = workOrderBillDao.rejectAdvanceGeneratedBill(tempBillNo);
			// String s=null;s.trim(); 
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			isSendMail = false;
		}
		return response;
	}

	/**
	 * @description this method is used to reject RA and ADV bill
	 */
	@Override
	public String rejectRABill(HttpServletRequest request, HttpSession session) {
		String response = "";
		String purpose = "";
		boolean isSendMail = false;
		String siteId = "";
		String userId = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
				 siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				 userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				//String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
				 siteId=request.getParameter("site_id")==null?siteId:request.getParameter("site_id");
				String changedRemarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
				//String actualBillLevelPurpose = request.getParameter("actualremarks") == null ? "": request.getParameter("actualremarks");
				String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo");
				
				ContractorQSBillBean bean=new ContractorQSBillBean();
				bean.setFromEmpId(userId);
				bean.setSiteId(siteId);
				bean.setRemarks(changedRemarks);
				bean.setApproverEmpId(userId);
				bean.setTempBillNo(tempBillNo);
				
				WriteTrHistory.write("Site:" + siteId + " , User:" + userId + " , Date:" + new java.util.Date()	+ " , Work Order Number:" + tempBillNo);
				//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
				int result = workOrderBillDao.insertTempBillApprRejDetail(bean,"R");
				//calling dao layer to reject the bill by updating status with "R"
				 result = workOrderBillDao.rejectAdvanceGeneratedBill(tempBillNo);
				
				transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			isSendMail = false;
		}
		return response;
	}

	@Override
	public List<Map<String, Object>> loadBillChangedDetails(String tmpBillNo, String siteId, String userId) {
		
		return workOrderBillDao.loadBillChangedDetails(tmpBillNo, siteId, userId);
	}

	@Override
	public List<Map<String, Object>> getRaAdvDeductionDetails(String tmpBillNo, String userId,String status) {
		
		return workOrderBillDao.getRaAdvDeductionDetails(tmpBillNo, userId,status);
	}
	@Override
	public List<Map<String, Object>> userAbleToCreateBillTypes(String user_id, WorkOrderBean workOrderBean) {
		// TODO Auto-generated method stub
		return workOrderBillDao.userAbleToCreateBillTypes(user_id,workOrderBean);
	}	
	
	@Override
	public List<Map<String, Object>> getRaAdvCompletedDeductionDetails(ContractorQSBillBean bean) {
		
		return  workOrderBillDao.getRaAdvCompletedDeductionDetails(bean);
	}
	@Override
	public List<ContractorQSBillBean> getCompletdWorkOrderBills( String siteId, ContractorQSBillBean billBean) {
		
		return workOrderBillDao.getCompletdWorkOrderBills(siteId,billBean);
	}
	@Override
	public List<ContractorQSBillBean> getSitewiseCompletdWorkOrderBills(String string, String siteId, String billType) {
		return workOrderBillDao.getSitewiseCompletdWorkOrderBills(string,siteId,billType);
	}
	
	@Override
	public WorkOrderBean getWorkOrderFromAndToDetails(String userId, WorkOrderBean workOrderBean) {
		return workOrderBillDao.getWorkOrderFromAndToDetails(userId, workOrderBean);

	}
	@Override
	public List<Map<String, Object>> getTempBillVerifiedEmpNames(ContractorQSBillBean billBean) {
		// TODO Auto-generated method stub
		return workOrderBillDao.getTempBillVerifiedEmpNames(billBean);
	}
	
	@Override
	public String uploadBOQ() {
		
		return null;
	}
	@Override
	public List<Map<String, Object>> getAllDedcutedAmount(ContractorQSBillBean bean) {
		return workOrderBillDao.getAllDedcutedAmount(bean);
	}
	@Override
	public int getContractorPaidAmount(ContractorQSBillBean bean) {
		
		return workOrderBillDao.getContractorPaidAmount(bean);
	}
	@Override
	public List<Map<String, Object>> loadDeductionTypes(String data) {
		
		return workOrderBillDao.loadDeductionTypes(data);
	}
	
	/**
	 * calling dao layer to load the bill recovery details
	 */
	@Override
	public List<Map<String, Object>> loadRecoveryAreaDetails(String[] data) {
		return workOrderBillDao.loadRecoveryAreaDetails(data);
	}
	
	/**
	 * calling dao layer to load the bill recovery details
	 */
	@Override
	public List<Map<String, Object>> loadPermanentRecoveryAreaDetails(String[] data) {
		return workOrderBillDao.loadPermanentRecoveryAreaDetails(data);
	}
	
	/**
	 * Calling dao layer to get the result of bill no
	 */
	@Override
	public String checkBillNoExistsOrNot(HttpServletRequest request, HttpSession session) {
		log.info("ContractorBillGeneraterServiceImpl.checkBillNoExistsOrNot()");
		String formatted = "";
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
		String contractorId = request.getParameter("ContractorId") == null ? ""	: request.getParameter("ContractorId");
		String billNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
		String isInvoiceOrBillNumberValidation= request.getParameter("isInvoiceOrBillNumberValidation") == null ? "" : request.getParameter("isInvoiceOrBillNumberValidation");
		workOrderNo = workOrderNo.split("\\$")[0].trim();

		ContractorQSBillBean billBean = new ContractorQSBillBean();
		billBean.setContractorId(contractorId);
		billBean.setBillNo(billNo);
		billBean.setWorkOrderNo(workOrderNo);
		billBean.setSiteId(siteId);
		billBean.setCondition(isInvoiceOrBillNumberValidation);
		billNo=billBean.getBillNo().trim();
		StringBuffer billLeadingPart=new StringBuffer("");
		
		String[] billNumberParts=billNo.split("/");
		int num=Integer.valueOf(billNumberParts[billNumberParts.length-1]);
		if(isInvoiceOrBillNumberValidation.equals("invoiceNumber")){
			formatted = String.format("%03d", num);
		}else{
			formatted = String.format("%02d", num);
		}
		for (int i = 0; i < billNumberParts.length-1; i++) {
			billLeadingPart.append(billNumberParts[i]+"/");
		}
		billNo=billLeadingPart+formatted;
		billBean.setBillNo(billNo);
		if(isInvoiceOrBillNumberValidation.equals("invoiceNumber")){
			return workOrderBillDao.checkInvoiceBillNoExistsOrNot(billBean);
		}else{
			return workOrderBillDao.checkBillNoExistsOrNot(billBean);
		}
	}
	/**
	 * @description this method is used for getting the last first two characters of any string
	 * @param str
	 * @return
	 */
	public static String getfirstTwoChar(String str) {
	    return str.length() < 2 ? str : str.substring(0, 3);
	}
	@Override
	public String loadAdvRAPermanentBillNo(ContractorQSBillBean billBean) {
		String strSiteName="";
		StringBuffer initials = new StringBuffer ();
		String siteName=billBean.getSiteName();
		
		if(siteName!=null)
		if(siteName.contains(" ")){
			//if site name contains space then retrieve the first char from this string
			for (String s : siteName.split(" ")) {
			  initials.append(s.charAt(0));
			}
		}else{
			 initials.append(getfirstTwoChar(siteName));
		}
		return workOrderBillDao.loadAdvRAPermanentBillNo(billBean,initials.toString());
	}
	
	@Override
	public  List<Map<String, Object>> loadNMRBillData(ContractorQSBillBean billBean) {
		return workOrderBillDao.loadNMRBillData(billBean);
	}
	
	@Override
	public List<Map<String, Object>> loadPermanentNMRBillDetailsData(ContractorQSBillBean billBean) {
		
		return workOrderBillDao.loadPermanentNMRBillDetailsData(billBean);
	}
	
	@Override
	public List<Map<String, Object>> loadNMRBillDataForApproval(ContractorQSBillBean billBean) {
		
		return workOrderBillDao.loadNMRBillDataForApproval(billBean);
	}
	
	@Override
	public List<Map<String, Object>> loadPermanentNMRBillData(ContractorQSBillBean billBean) {
		return workOrderBillDao.loadPermanentNMRBillData(billBean);
	}
	/**
	 * not in use
	 */
	@Override
	public List<Map<String, Object>> loadNMRBillDeatilsByBillNo(ContractorQSBillBean billBean) {
		return workOrderBillDao.loadNMRBillDeatilsByBillNo(billBean);
	}
	
	@Override
	public List<Map<String, Object>> loadWorkDescNMRBills(ContractorQSBillBean billBean) {
		
		return workOrderBillDao.loadWorkDescNMRBills(billBean);
	}
	/**
	 * @throws Exception 
	 * @description this method is used to insert NMR bill data 
	 * 
	 */
	@Override
	public String generateNMRBill(HttpServletRequest request, HttpSession session) throws Exception {
		String response ="";
		boolean isSendMail = true;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String approverEmpId = request.getParameter("nextLevelApproverEmpID");
			String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("\\$")[0];
			String noOfRowsToIterate=request.getParameter("noOfRowsToIterate")==null?"0":request.getParameter("noOfRowsToIterate");
			String CertifiedAmount = request.getParameter("CertifiedAmount");
			String paybleAmount = request.getParameter("paybleAmount");
			String ContractorId=request.getParameter("ContractorId");
			String billDate=request.getParameter("billDate");
			String billRemarks=request.getParameter("remarks")==null?"":request.getParameter("remarks");
			String permanentBillNo=request.getParameter("NMRBillNo");
			String currentRecoveryAmount=request.getParameter("currentRecoveryAmount")==null?"":request.getParameter("currentRecoveryAmount");
				currentRecoveryAmount=currentRecoveryAmount==""?"0":currentRecoveryAmount;
			String[] blocks=request.getParameterValues("Blocks");//==null?"":request.getParameterValues("Blocks");
			String	isOldOrNewBill = request.getParameter("isOldOrNewBill") == null ? "" : request.getParameter("isOldOrNewBill");
			String oldBillNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
			String actualBillNumber= request.getParameter("actualBillNumber") == null ? "" : request.getParameter("actualBillNumber");
			String billInvoiceNo= request.getParameter("billInvoiceNo") == null ? "" : request.getParameter("billInvoiceNo");
		    String 	pettyExpensesCurrentCerti = request.getParameter("pettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("pettyExpensesCurrentCerti");
	     	String 	otherAmout = request.getParameter("other") == null ? "0": request.getParameter("other");
	     	
	     	pettyExpensesCurrentCerti=pettyExpensesCurrentCerti==""?"0":pettyExpensesCurrentCerti;
	     	otherAmout=otherAmout==""?"0":otherAmout;
	     	
			ContractorQSBillBean bean = new ContractorQSBillBean();
			//checking here the permanent bill number manually entered or not
			//if entered we need to store this permanent bill number
			if(isOldOrNewBill.equals("Old")&&oldBillNo.length()!=0){
				String str=oldBillNo.split("/")[1];
				int num=Integer.valueOf(str);
				String formatted = String.format("%02d", num);
				oldBillNo="NMR/"+formatted;
				bean.setOldBillNo(oldBillNo);
				permanentBillNo=oldBillNo;
			}else{
				 permanentBillNo = request.getParameter("NMRBillNo") == null ? "" : request.getParameter("NMRBillNo");
				bean.setOldBillNo(permanentBillNo);
			}
			StringBuffer billLeadingPart=new StringBuffer("");
			if(billInvoiceNo.length()!=0){
				String[] billNumberParts=billInvoiceNo.split("/");
				int num=Integer.valueOf(billNumberParts[billNumberParts.length-1]);
				String formatted = String.format("%03d", num);
				for (int i = 0; i < billNumberParts.length-1; i++) {
					billLeadingPart.append(billNumberParts[i]+"/");
				}
			 	billInvoiceNo=billLeadingPart+formatted;
			}
			String blockNames="";
			if(blocks!=null)
			for (String block : blocks) {
				blockNames=blockNames+block.split("@@")[1]+"@#";
			}
			bean.setActualBillNumber(actualBillNumber);
			bean.setPaymentType("NMR");
			bean.setBillInvoiceNumber(billInvoiceNo);
			bean.setNMRBillBlocks(blockNames);
			bean.setSiteId(siteId);
			bean.setWorkOrderNo(workOrderNo);
			bean.setApproverEmpId(approverEmpId);
			bean.setFromEmpId(userId);
			bean.setContractorId(ContractorId);
			bean.setBillDate(billDate);
			bean.setPaybleAmt(paybleAmount);
			bean.setTotalAmtCumulativeCertified(CertifiedAmount);
			bean.setPermanentBillNo(permanentBillNo);
			bean.setRecovery_amount(currentRecoveryAmount);
			bean.setPettyExpensesCurrentCerti(pettyExpensesCurrentCerti);
			bean.setOtherAmout(otherAmout);
			//inserting NMR data
			List<Object> list=workOrderBillDao.insertNMRBillData(bean);
			
			String tempBillNo=list.get(0).toString();
			
			bean = new ContractorQSBillBean();
			List<String> rateOfWorkList=new ArrayList<String>();
			List<ContractorQSBillBean> listOfNMRData=new ArrayList<ContractorQSBillBean>();
			String nmrRowNumber="NMRRow";
		
		String[] noOfRowsToIterate1=noOfRowsToIterate.split(",");
		for (String index:noOfRowsToIterate1) {
			//adding nmrRowNumber variabale value in DB because there is no unique values for selecing the data
			//here all the data can be duplicate
			//because of "OT" and "Regular work"
				nmrRowNumber=nmrRowNumber+index;
				String workDate=request.getParameter("WorkDate"+index);
				String description1=request.getParameter("Description"+index);
				String currentRowminorHeadId=description1.split("@@")[1];
				String currentWORowCode=description1.split("@@")[2];
				String manualDesc1=request.getParameter("ManuvalDescription"+index);
				
				String fromTime1=request.getParameter("FromTime"+index);
				String toTime1=request.getParameter("toTime"+index);
				String noOfHours1=request.getParameter("Noofhours"+index);
				
				//String [] noOfWorkers=request.getParameterValues("noOfWorkers"+index);
				//String workDescId[]=request.getParameterValues("workDescId"+index);
				
				//String [] noOfWorkers_hrs=request.getParameterValues("noOfWorkers_hrs"+index);
				String [] allMinorHeadAndWD_ID=request.getParameterValues("minorWDId"+index);
				String [] rateOfWork=request.getParameterValues("rate"+index);
				
				
				//List<String> listOfCurrentRowMinorHeadId=Arrays.asList(currentRowminorHeadId.split("@@"));
				
				if(rateOfWork!=null){
					rateOfWorkList=Arrays.asList(rateOfWork);	
				}
				
				String remarks=request.getParameter("Remarks"+index);
				String workDeskId="";
				String rate="";
				double amount1=0;
				String noOfWorker="";
				double noOfHour=0;
				int increseWDRow=0;
				int noOfWDToIterate=request.getParameter("noOfWDToIterate")==null?0:Integer.valueOf(request.getParameter("noOfWDToIterate"));
				//minorHeadWDId is holding all the minor head id, we have to match the current processing minor head id with minorHeadWDId
				for (int i = 0; i < allMinorHeadAndWD_ID.length; i++) {
					String tempMH_WDStr[]=allMinorHeadAndWD_ID[i].split("@@");
					String tempMinorHeadId=tempMH_WDStr[0];
					String tempWORowCode=tempMH_WDStr[2];
					workDeskId=tempMH_WDStr[1];
					String TempworkDescId="0";
					String noOfWorkerrs="";
					try {
						ContractorQSBillBean bean2=new ContractorQSBillBean();
						bean2.setWorkDescId1(workDeskId);
						bean2.setWorkOrderNo(workOrderNo);
						bean2.setMinorHeadId1(currentRowminorHeadId);
						bean2.setWoRowCode(currentWORowCode);
						bean2.setSiteId(siteId);
						//getting the name of work description names using minor head id and work order number
						String workDeskName=workOrderBillDao.getWordDescNameAndId(bean2);
						String str[]=workDeskName.split("@@");
						String workDescName=str[0].replaceAll("[^a-zA-Z0-9]", "");
						rate=str[3];
						//loading the no of workers 
						//here we used dynamic names in front end there we have used input box name as a work description name 
						//so here we are giving the dynamic name in getParameter method so we will get the actual values
						noOfWorkerrs=request.getParameter(workDescName+(index))==null?"0":request.getParameter(workDescName+(index));
						noOfWorkerrs=noOfWorkerrs==""?"0":noOfWorkerrs;
						TempworkDescId=str[1];
					}catch (Exception e) {
						System.out.println(e.getMessage());
						TempworkDescId="0";
					}
					//minorHeadId is holding the current processing row minor head id
					//and minorHeadId is checking with all the minorHeadWDId  
					//minorHeadWDId is one array which is holding all the minor head id and work description id
					if(tempMinorHeadId.equals(currentRowminorHeadId)&&workDeskId.equals(TempworkDescId)&&tempWORowCode.equals(currentWORowCode)){
						bean = new ContractorQSBillBean();
						noOfWDToIterate++;
						increseWDRow++;
						
						noOfWorker=noOfWorkerrs;
						noOfHour=Double.valueOf(noOfHours1)*Double.valueOf(noOfWorker);
						System.out.println(workDeskId+" "+tempMinorHeadId);
						
						amount1=Double.valueOf(noOfWorker)*Double.valueOf(rate);
					
						bean.setAmount1(String.valueOf(amount1));
						bean.setRate1(rate);
						bean.setWorkDate1(workDate);
						bean.setNMRRowIndex1(nmrRowNumber);
						bean.setMinorHeadId1(currentRowminorHeadId);
						bean.setWoRowCode(currentWORowCode);
						bean.setWorkDescId1(workDeskId);
						bean.setDescription1(description1);
						bean.setManualDesc1(manualDesc1);
						bean.setFromTime1(fromTime1);
						bean.setToTime1(toTime1);
						bean.setNoOfHours1(noOfHours1);
						bean.setNoOfworkers1(noOfWorker);
						bean.setWorkDays1(String.valueOf(noOfHour/8));
						bean.setTempBillNo(tempBillNo);
						bean.setRemarks(remarks);
						bean.setWorkOrderNo(workOrderNo);
						bean.setSiteId(siteId);
					 
							listOfNMRData.add(bean);
					}
				}
				nmrRowNumber="NMRRow";
			}
		
		//getting number of recovery rows to be process
		int rowsToIterate=Integer.valueOf(request.getParameter("rowsToIterate1")==null?0:Integer.valueOf( request.getParameter("rowsToIterate1")));
		
		for (int j = 1; j <= rowsToIterate; j++) {
			ContractorQSBillBean billBean=new ContractorQSBillBean();
			String child_product_id =request.getParameter("child_product_id"+j);
			String childProdName=request.getParameter("childProdName"+j);
			String measurement_id=request.getParameter("measurement_id"+j);
			String recovery_amount=request.getParameter("recovery_amount"+j);
			String recovery_quantity=request.getParameter("recovery_quantity"+j);
			billBean.setChild_product_id(child_product_id);
			billBean.setMeasurement_id(measurement_id);
			billBean.setRecovery_amount(recovery_amount);
			billBean.setChildProdName(childProdName);
			billBean.setRecovery_quantity(recovery_quantity);
			billBean.setWorkOrderNo(workOrderNo.split("\\$")[0].trim());
			billBean.setTempBillNo(String.valueOf(tempBillNo));
			billBean.setSiteId(siteId.trim());
			int result=0;
			//inserting recovery data
			result=workOrderBillDao.insertIntoBoqRecovery(billBean);
			result =workOrderBillDao.insertIntoBoqRecoveryHistory(billBean);
		}
		
		
		
			/*System.out.println();
			for (ContractorQSBillBean contractorQSBillBean : listOfNMRData) {
				System.out.println(contractorQSBillBean);
			}*/
			//inserting all the NMR details popup row data
			int	 result=workOrderBillDao.insertNMRData(listOfNMRData);
			bean = new ContractorQSBillBean();
			bean.setApproverEmpId(approverEmpId);
			bean.setFromEmpId(userId);
			bean.setSiteId(siteId);
			bean.setUserId(userId);
			bean.setRemarks(billRemarks);
			bean.setTempBillNo(tempBillNo);
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			result = workOrderBillDao.insertTempBillApprRejDetail(bean, "C");
			request.setAttribute("TempNMRBillNo", tempBillNo);
			request.setAttribute("NMRBillNo", permanentBillNo);
			//String s=null;s.trim();
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			isSendMail = true;
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			if(e.getMessage().equals("missing work order issue id")){
				throw new Exception("missing work order issue id");
			}
			if(e.getMessage().equals("missing bill number")){
				throw new Exception("missing bill number");
			}
			isSendMail = false;
		}
		
		return response;
	}
	/**
	 * @throws Exception 
	 * @description this method used for approving the NMR bill
	 * and also used for maintain the modification details log's in DB
	 */
	@Override
	public String approveNMRBill(HttpServletRequest request, HttpSession session) throws Exception {
		String response ="";
		boolean isSendMail = true;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			siteId=request.getParameter("site_id")==null?siteId:request.getParameter("site_id");
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
			String permanentBillNo=request.getParameter("NMRBillNo");
			String approverEmpId = request.getParameter("nextLevelApproverEmpID");
			String workOrderNo = request.getParameter("WorkOrderNo")==null?"":request.getParameter("WorkOrderNo").split("\\$")[0];
			int noOfRowsToIterate=request.getParameter("noOfRowsToIterate")==null?0:Integer.parseInt(request.getParameter("noOfRowsToIterate"));
			String paybleAmount = request.getParameter("paybleAmount")==null?"0":request.getParameter("paybleAmount");
			String CertifiedAmount = request.getParameter("CertifiedAmount");
			String totalDeductAmt=request.getParameter("totalActualDeductAmt")==null?"0":request.getParameter("totalActualDeductAmt");
			//String actualPaybleAmount = request.getParameter("actualPaybleAmount")==null?"0":request.getParameter("actualPaybleAmount");
			String currentRecoveryAmount=request.getParameter("currentRecoveryAmount")==null?"":request.getParameter("currentRecoveryAmount");
			String billRemarks=request.getParameter("remarks")==null?"":request.getParameter("remarks");
		
			String actualPettyExpensesCurrentCerti=request.getParameter("actualPettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("actualPettyExpensesCurrentCerti");
			String actualOtherAmt=request.getParameter("actualOtherAmt") == null ? "" : request.getParameter("actualOtherAmt").trim();

			String 	pettyExpensesCurrentCerti = request.getParameter("pettyExpensesCurrentCerti") == null ? "0"	: request.getParameter("pettyExpensesCurrentCerti");
			String 	otherAmout = request.getParameter("other") == null ? "0": request.getParameter("other");

			double cuurentPetty=Double.valueOf(pettyExpensesCurrentCerti);
			double actualPetty=Double.valueOf(actualPettyExpensesCurrentCerti);
			
			double actualOther =Double.valueOf(actualOtherAmt);
			double currentOther=Double.valueOf(otherAmout);
			String changedDetails="";

			if(cuurentPetty!=actualPetty){
				changedDetails +=  strUserName+" - changed the petty expenses amount "+actualPettyExpensesCurrentCerti + " to " + pettyExpensesCurrentCerti+ "<br>";
			}
			if (actualOther!=currentOther){
				changedDetails +=  strUserName+" - changed the other charges amount "+actualOtherAmt+ " to " +otherAmout+"<br>";
			}
			
		
		
			ContractorQSBillBean actualNMRBillData=new ContractorQSBillBean();
			ContractorQSBillBean changedNMRBillData=new ContractorQSBillBean();
			int result=0;
			
			if(changedDetails.length()!=0){
				
				actualNMRBillData.setPettyExpensesCurrentCerti(actualPettyExpensesCurrentCerti);
				changedNMRBillData.setPettyExpensesCurrentCerti(pettyExpensesCurrentCerti);
				actualNMRBillData.setOtherAmout(otherAmout);
				changedNMRBillData.setOtherAmout(otherAmout);
				actualNMRBillData.setRemarks(changedDetails);
				changedNMRBillData.setRemarks(changedDetails);
			//	actualNMRBillData.setRecovery_amount(currentRecoveryAmount);
				changedNMRBillData.setRecovery_amount(currentRecoveryAmount);
				actualNMRBillData.setPaymentType("NMR");
				changedNMRBillData.setPaymentType("NMR");
				actualNMRBillData.setPaybleAmt(paybleAmount);
				changedNMRBillData.setPaybleAmt(paybleAmount);
				actualNMRBillData.setTempBillNo(tempBillNo);
				changedNMRBillData.setTempBillNo(tempBillNo);
				
				
				result=workOrderBillDao.saveChangedAdvBillDetails(actualNMRBillData,changedNMRBillData);
				result = workOrderBillDao.updateChangedRADeductBillDetails(actualNMRBillData, changedNMRBillData);
			}
			String[] blocks=request.getParameterValues("Blocks");
			
			//String ContractorId=request.getParameter("ContractorId");
			//String billDate=request.getParameter("billDate");
			String strChangdDetails="";
			String blockNames="";
			if(blocks!=null)
			for (String block : blocks) {
				blockNames=blockNames+block.split("@@")[1]+"@#";
			}
			
		
			ContractorQSBillBean NMRbean=new ContractorQSBillBean();
			
			NMRbean.setApproverEmpId(approverEmpId);
			NMRbean.setFromEmpId(userId);
			NMRbean.setSiteId(siteId);
			NMRbean.setUserId(userId);
			NMRbean.setRemarks(billRemarks);
			NMRbean.setTempBillNo(tempBillNo);
			NMRbean.setBillNo(permanentBillNo);
			NMRbean.setWorkOrderNo(workOrderNo);
			NMRbean.setPaybleAmt(paybleAmount);
			NMRbean.setTotalAmtCumulativeCertified(CertifiedAmount);
			NMRbean.setPaymentType("NMR");
			NMRbean.setNMRBillBlocks(blockNames);
			NMRbean.setRecovery_amount(currentRecoveryAmount);
			NMRbean.setTotalCurrntDeducAmt(totalDeductAmt);
			NMRbean.setPettyExpensesCurrentCerti(pettyExpensesCurrentCerti);
			NMRbean.setOtherAmout(otherAmout);
			
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			result = workOrderBillDao.insertTempBillApprRejDetail(NMRbean, "A");
		
			List<String> rateOfWorkList=new ArrayList<String>();
			List<ContractorQSBillBean> listOfNMRData=new ArrayList<ContractorQSBillBean>();
			//processing number of rows of NMR details popup
			for (int index = 1; index <= noOfRowsToIterate; index++) {
				boolean isRowChanged=false;
				//actualNMRBillData means before changing the NMR bill data, actualNMRBillData will hold the actual bill data
				 	actualNMRBillData = new ContractorQSBillBean();
				//changedNMRBillData means after changing the nmr bill data, changedNMRBillData will hold the changed bill data
				 	changedNMRBillData = new ContractorQSBillBean();
				String QS_INV_AGN_WORK_PMT_DTL_ID=request.getParameter("QS_INV_AGN_WORK_PMT_DTL_ID"+index);
				String workDate=request.getParameter("WorkDate"+index);
				String NMRRowNumber=request.getParameter("nmrRowNumber"+index);
				String description1=request.getParameter("Description"+index);
				String tempminorHeadNAme[]=description1.split("@@");
				String minorHeadName=tempminorHeadNAme[0];
				String minorHeadId=tempminorHeadNAme[1];
				String manualDesc1=request.getParameter("ManuvalDescription"+index);
				
				String fromTime1=request.getParameter("FromTime"+index);
				String actualFromTime=request.getParameter("actualFromTime"+index);
				String toTime1=request.getParameter("toTime"+index);
				String actualToTime=request.getParameter("actualToTime"+index);
				String noOfHours1=request.getParameter("Noofhours"+index);
				String actualNoofhours=request.getParameter("actualNoofhours"+index);
				String remarks=request.getParameter("Remarks"+index);
				String ActualRemarks=request.getParameter("ActualRemarks"+index);
				//getting all the workers in one array variable, row by row
				String [] noOfWorkers=request.getParameterValues("noOfWorkers"+index);
				String [] actualNoOfWorkers=request.getParameterValues("actualnoOfWorkers"+index);
				String [] noOfWorkers_hrs=request.getParameterValues("noOfWorkers_hrs"+index);
				
				//String workDescId[]=request.getParameterValues("workDescId"+index);
				//getting all the minor head and work description id in one array variable, row by row
				String [] minorHeadWDId=request.getParameterValues("minorWDId"+index);
				String [] rateOfWork=request.getParameterValues("rate"+index);
				
				if(rateOfWork!=null){
					rateOfWorkList=Arrays.asList(rateOfWork);	
				}
				//if the remarks is not empty then update the remarks to nmr detials row
				if(remarks.length()!=0)
				if(!remarks.equals(ActualRemarks)){
					remarks=ActualRemarks+" - "+remarks;
					changedNMRBillData.setRemarks(remarks);
					changedNMRBillData.setNMRRowIndex1(NMRRowNumber);
					changedNMRBillData.setTempBillNo(tempBillNo);
					result=workOrderBillDao.updateNMRROWDataRemarks(changedNMRBillData);
					//isRowChanged=true;
				}
				//checking number of hours is changed or not if changed maintain modification details
				if(!noOfHours1.equals(actualNoofhours)){
					strChangdDetails+=strUserName+" - changed "+minorHeadName+" actual No Of Hour's "+actualNoofhours+" changed to "+noOfHours1+"<br>";
					isRowChanged=true;
				}

				//checking number of fromTime1 is changed or not if changed maintain modification details
				if(!fromTime1.equals(actualFromTime)){
					strChangdDetails+=strUserName+" - changed "+minorHeadName+" Work actual From-Time "+actualFromTime+" changed to "+fromTime1+"<br> ";
					isRowChanged=true;
				}
				
				//checking toTime1 of hours is changed or not if changed maintain modification details
				if(!toTime1.equals(actualToTime)){
					strChangdDetails+=strUserName+" - changed  "+minorHeadName+" Work actual TO-Time "+actualToTime+" changed to "+toTime1+"<br>";
					isRowChanged=true;
				}
				
				
				String workDeskId="";
				String rate="";
				double amount1=0;
				String noOfWorker="";
				double noOfHour=0;
			//double actualamount1=0;
				String actualnoOfWorker="";
			//double actualnoOfHour=0;
			//	int increseWDRow=0;
	
			//minorHeadWDId is holding all the minor head id and work description id of the work order 
				for (int i = 0; i < minorHeadWDId.length; i++) {
					
					String tempStr[]=minorHeadWDId[i].split("@@");
					String tempMinorHeadId=tempStr[0];
					//if the tempMinorHeadId is matching with NMR details row, then enter into the condition  
					if(tempMinorHeadId.equals(minorHeadId)){
						String chnegedDetails="";
						workDeskId=tempStr[1];
						rate=rateOfWorkList.get(i);
						noOfWorker=noOfWorkers[i];
						actualnoOfWorker=actualNoOfWorkers[i];
						chnegedDetails+=strChangdDetails;
						//checking here is actual worker(means before changing the value of worker) 
						//and current work worker is not equal the maintain the log's in DB and update the NMR details record
						if(!actualnoOfWorker.equals(noOfWorker)){
							chnegedDetails+=strUserName+" - changed "+minorHeadName+" Actual Worker's "+actualnoOfWorker+" changed to "+noOfWorker+"<br>";
							isRowChanged=true;
						}
						
					//if row changed then update the record
						if(isRowChanged){
							//strChangdDetails="";	
							noOfHour=Double.valueOf(noOfWorkers_hrs[i]);
							System.out.println(workDeskId+" "+tempMinorHeadId);
							amount1=Double.valueOf(noOfWorker)*Double.valueOf(rate);
							changedNMRBillData.setFromTime1(fromTime1);
							changedNMRBillData.setToTime1(toTime1);
							changedNMRBillData.setNoOfHours1(noOfHours1);
							changedNMRBillData.setNoOfworkers1(noOfWorker);
							changedNMRBillData.setWorkDays1(String.valueOf(noOfHour/8));
							changedNMRBillData.setTempBillNo(tempBillNo);
							changedNMRBillData.setRemarks(remarks);
							changedNMRBillData.setMinorHeadId1(minorHeadId);
							changedNMRBillData.setWorkDescId1(workDeskId);
							changedNMRBillData.setWorkDate1(workDate);
							changedNMRBillData.setWorkOrderNo(workOrderNo);
							changedNMRBillData.setSiteId(siteId);
							changedNMRBillData.setAmount1(String.valueOf(amount1));
							changedNMRBillData.setTempWorkPMTDtlId(QS_INV_AGN_WORK_PMT_DTL_ID);
							changedNMRBillData.setNMRRowIndex1(NMRRowNumber);
							
							actualNMRBillData.setNMRRowIndex1(NMRRowNumber);
							actualNMRBillData.setTempWorkPMTDtlId(QS_INV_AGN_WORK_PMT_DTL_ID);
							actualNMRBillData.setAmount1(String.valueOf(amount1));
							actualNMRBillData.setRate1(rate);
							actualNMRBillData.setWorkDate1(workDate);
							actualNMRBillData.setMinorHeadId1(minorHeadId);
							actualNMRBillData.setWorkDescId1(workDeskId);
							actualNMRBillData.setDescription1(description1);
							actualNMRBillData.setManualDesc1(manualDesc1);
							actualNMRBillData.setFromTime1(fromTime1);
							actualNMRBillData.setToTime1(toTime1);
							actualNMRBillData.setNoOfHours1(noOfHours1);
							actualNMRBillData.setNoOfworkers1(noOfWorker);
							actualNMRBillData.setWorkDays1(String.valueOf(noOfHour/8));
							actualNMRBillData.setTempBillNo(tempBillNo);
							actualNMRBillData.setRemarks(remarks);
							actualNMRBillData.setWorkOrderNo(workOrderNo);
							actualNMRBillData.setSiteId(siteId);
							
							//updating the NMR details row data
							result=workOrderBillDao.updateNMRROWData(changedNMRBillData);
							changedNMRBillData.setPaymentType("RA");//It;s NMR But Chnaged to RA to use existing Code
							actualNMRBillData.setPaymentType("RA");
							actualNMRBillData.setRemarks(chnegedDetails);
							changedNMRBillData.setRemarks(chnegedDetails);
							//if any quantity changed we need to maintain the log, to showing the next level approval
							if(chnegedDetails.length()!=0)
							result=workOrderBillDao.saveChangedAdvBillDetails(actualNMRBillData,changedNMRBillData);
							isRowChanged=false;
						}
					}
					
				}
				listOfNMRData.add(actualNMRBillData);
				strChangdDetails="";
			}
			
			//getting number of recovery rows to be process
			int rowsToIterate=Integer.valueOf(request.getParameter("rowsToIterate1")==null?0:Integer.valueOf( request.getParameter("rowsToIterate1")));
			String recoveryChangedMsg="";
			for (int j = 1; j <= rowsToIterate; j++) {
				ContractorQSBillBean billBean = new ContractorQSBillBean();
				String child_product_id = request.getParameter("child_product_id" + j);
				String childProdName = request.getParameter("childProdName" + j);
				String measurement_id = request.getParameter("measurement_id" + j);
				String recovery_amount = request.getParameter("recovery_amount" + j) == null ? "0": request.getParameter("recovery_amount" + j);
				String recovery_quantity = request.getParameter("recovery_quantity" + j);

				String actualrecovery_amount1 = request.getParameter("actualrecovery_amount1" + j) == null ? "0": request.getParameter("actualrecovery_amount1" + j);
				
				billBean.setChild_product_id(child_product_id);
				billBean.setMeasurement_id(measurement_id);
				billBean.setRecovery_amount(recovery_amount);
				billBean.setRecovery_amount1(recovery_amount);
				billBean.setChildProdName(childProdName);
				billBean.setRecovery_quantity(recovery_quantity);
				billBean.setSiteId(siteId.trim());
				billBean.setWorkOrderNo(workOrderNo.split("\\$")[0]);
				billBean.setTempBillNo(tempBillNo);

				double actualAmount = Double.valueOf(actualrecovery_amount1);
				double currentAmount = Double.valueOf(recovery_amount);
				
				int condition = 0;
				//checking amount is changed or not, if changed we need to maintain the log
				if(actualAmount<currentAmount){
					condition=1;
					String temp="";//String.valueOf(Math.abs(actualAmount-currentAmount));
					temp = request.getParameter("currentRecoveryAmount11" + j) == null ? "0": request.getParameter("currentRecoveryAmount11" + j);
					billBean.setRecovery_amount(temp);
					recoveryChangedMsg=strUserName+" - Changed recovery actual amount "+actualAmount+" changed to "+currentAmount;
					billBean.setRemarks(recoveryChangedMsg);
					billBean.setPaymentType("NMR");
					billBean.setPaybleAmt(paybleAmount);
					//inserting changed details
					result=workOrderBillDao.saveChangedAdvBillDetails(billBean,billBean);
				}else if(actualAmount>currentAmount){
					condition=2;
					String temp = request.getParameter("currentRecoveryAmount11" + j) == null ? "0": request.getParameter("currentRecoveryAmount11" + j);
					recoveryChangedMsg=strUserName+" - Changed recovery actual amount "+actualAmount+" changed to "+currentAmount;
					billBean.setRecovery_amount(temp);
					billBean.setRemarks(recoveryChangedMsg);
					billBean.setPaymentType("NMR");
					billBean.setPaybleAmt(paybleAmount);
					//inserting changed details
					result=workOrderBillDao.saveChangedAdvBillDetails(billBean,billBean);
				}else{
					condition=0;
					System.out.println("value not changed");
				}
				//if any recovery vlaues not changed then update the recovery data
				if(condition!=0){
					//updating the recovery details
					result=workOrderBillDao.updateBoqRecovery(billBean,condition);
					result =workOrderBillDao.updateBoqRecoveryHistory(billBean,condition);
				    System.out.println(result);
				}
			}
			result=workOrderBillDao.approveNMRBill(NMRbean);
			//setting temp bill number for showing in view page
			request.setAttribute("TempNMRBillNo", NMRbean.getTempBillNo());
			//setting permanent bill number for showing in view page, we can access this attribute in controller using request.getAttribute()
			request.setAttribute("NMRBillNo", NMRbean.getBillNo());
			//String s=null;s.trim();
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			if(e.getMessage().equals("check site account mappings")){
				throw new Exception("check site account mappings");
			}
			isSendMail = false;
		}
	
	return response;
	}
	
	/**
	 * @description this method is used for rejecting the NMR bill
	 * while rejecting bill user need to enter the comment why he is rejecting the bill
	 */
	@Override
	public String rejectNMRBill(HttpServletRequest request, HttpSession session) {
		String response ="";
		boolean isSendMail = true;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			siteId=request.getParameter("site_id")==null?siteId:request.getParameter("site_id");
	//		String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
			String approverEmpId = request.getParameter("nextLevelApproverEmpID");
			String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("\\$")[0];
			String changedRemarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
			int result = 0;
			ContractorQSBillBean NMRbean=new ContractorQSBillBean();
			NMRbean.setApproverEmpId(approverEmpId);
			NMRbean.setFromEmpId(userId);
			NMRbean.setSiteId(siteId);
			NMRbean.setUserId(userId);
			NMRbean.setRemarks(changedRemarks);
			NMRbean.setTempBillNo(tempBillNo);
			NMRbean.setWorkOrderNo(workOrderNo);
			//inserting approve reject details ("C"-Create,"R"-rejected,"A"-approve)
			result = workOrderBillDao.insertTempBillApprRejDetail(NMRbean, "R");
			//calling dao layer to in activate the NMR bill data
			result=workOrderBillDao.rejectNMRBill(NMRbean);
			//setting the temporary bill number for view page
			request.setAttribute("NMRBillNo", tempBillNo);
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
	} catch (Exception e) {
		transactionManager.rollback(status);
		// System.out.println("Indent creation Failed");
		WriteTrHistory.write("Tr_Completed");
		response = "failed";
		e.printStackTrace();
		isSendMail = false;
	}
		return response;
	}
}
