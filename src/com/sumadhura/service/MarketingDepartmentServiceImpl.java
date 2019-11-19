package com.sumadhura.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.write.DateTime;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.TransportChargesDto;
import com.sumadhura.transdao.CentralSiteIndentProcessDao;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.transdao.MarketingDepartmentDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.bean.MarketingDeptBean;

@Service("MarketingDepartmentService")
public class MarketingDepartmentServiceImpl extends UIProperties implements
MarketingDepartmentService {

	@Autowired
	@Qualifier("MarketingDepartmentDao")
	MarketingDepartmentDao objMarketingDepartmentIndentProcessDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;
	@Autowired
	private IndentCreationDao icd;
	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;
	

	@Autowired
	@Qualifier("purchaseDeptIndentrocess")
	PurchaseDepartmentIndentrocessService objPurchaseDeptIndentrocess;
		@Autowired
	@Qualifier("irsClass")
	IndentReceiveServiceImpl irsi;

	@Autowired
	private IndentReceiveDao ird;
	private Logger logger = Logger.getLogger(MarketingDepartmentServiceImpl.class);

	// multiple users call this method at the same time so use synchronized single user can access it
	@Override
	public synchronized String SaveMarketingPoDetails(Model model,HttpServletRequest request, HttpSession session,String strFinacialYear) {
		// transaction management using here for multiple table data saved 
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in MDIn_savePO, ");
		MarketingDeptBean productDetails = null;
		List<String> allEmployeeEmailsOfAcc = new ArrayList<String>();
		String strResponse = "";
		String strPONumber = "";
		String strVendorId = "";
		String strDeliveryDate = "";
		String strVendorAddress = "";
		String strVendorName = "";
		String vendorState = "";
		String strVendorGSTIN = "";
		String receiverState = "";
		String strTableTwoDate = "";
		String strTableThreeData = "";
		String strIndentNo = "";
		String siteWiseIndentNo = "";
		String strUserId = "";
		String strSiteId = "";
		String strToSite;
		String finalamount = "";
		String approvalEmpId = "";
		int noofrows = 0;
		String subject = "";
		String ccEmailId = "";
		String contactPersonName = "";
		String billingAddress = "";
		String strIndentCreationDate = "";
		String strVendorMobilNo = "";
		String strLandLineNo = "";
		int poEntryId = 0;
		String strEmail = "";
		String poState = "";
		String strPoDate = "";
		String strBillingAddressGSTIN = "";
		String strBillingCompanyName = "";
		String editPonumber = "";
		String strReceiveSideContactPerson = "";
		String strReceiverLandLine = "";
		String siteName = "";
		String sessionSiteId = "";
		Map<String, String> objviewPOPageDataMap = new HashMap<String, String>();
		String version_no = "";
		String refferenceNo = "";
		String strPoPrintRefdate = "";
		String recordcount = request.getParameter("numbeOfRowsToBeProcessed"); // total no of records taken here
		String recordscount[] = recordcount.split("\\|"); 
		int temprevision_no = 0;
		String isSiteLevelPo = "";
		String preparedBy = "";
		double grandtotal = 0.0;
		String passwdForMail = "";
		String strGrandTotalVal = "";
		String revisedOrNot = "";
		String strPODate = "";
		String oldPOEntryId = "";
		String revisedPoForTemp = "";
		int portNumber = request.getLocalPort();
		String acc_Note_Email = "";
		String strLocationTableData = "";
		String seletedSite="";
		String strSite="";
		String siteId="";
		String siteNumber="";
		String temppoOrNot="";
		String receiverName="";
		String strReceiverAddress="";
		String payment_Req_Days="";
		String operation_Type="";
		boolean paymentStatus=false;
		String result="";
		String changeDate="";
		try {
			for (int i = 1; i <= recordscount.length; i++) { // here take no of rows taken to sequence order like 1,2,3 etc.

				noofrows = i;
			}
			version_no = validateParams.getProperty("PO_versionNo");
			refferenceNo = validateParams.getProperty("PO_Refference");
			strPoPrintRefdate = validateParams.getProperty("PO_issuedate");
			strVendorId = request.getParameter("vendorId");
			strDeliveryDate = request.getParameter("deliveryDate");
			siteName = request.getParameter("SiteName");
			seletedSite=request.getParameter("poTo");
			request.setAttribute("siteNameForPriceMaster", siteName); // this is for getting the data user click on the child product name
			//int sno = 0;
			List<String> listOfTermsAndConditions = new ArrayList<String>();
			if (noofrows > 0) {
				// session = request.getSession(true);
				//strSiteId = request.getParameter("toSiteId") == null ? "": request.getParameter("toSiteId").toString();
				strUserId = session.getAttribute("UserId") == null ? "": session.getAttribute("UserId").toString();
				strSiteId=sessionSiteId = session.getAttribute("SiteId") == null ? "": session.getAttribute("SiteId").toString();
				subject = request.getParameter("subject")== null ? "-": request.getParameter("subject").toString();
				strToSite = request.getParameter("toSiteId") == null ? "": request.getParameter("toSiteId").toString();
				payment_Req_Days=request.getParameter("days")== null ? "" : request.getParameter("days").toString();
				productDetails = new MarketingDeptBean();
				// getting vendor details 
				List<Map<String, Object>> listVendorDtls = objMarketingDepartmentIndentProcessDao.getVendorOrSiteAddress(strVendorId);
				for (Map<String, Object> prods : listVendorDtls) {
					strVendorName = prods.get("VENDOR_NAME") == null ? "": prods.get("VENDOR_NAME").toString();
					strVendorAddress = prods.get("ADDRESS") == null ? "": prods.get("ADDRESS").toString();
					vendorState = prods.get("STATE") == null ? "" : prods.get("STATE").toString();
					strVendorGSTIN = prods.get("GSIN_NUMBER") == null ? "": prods.get("GSIN_NUMBER").toString();
					contactPersonName = prods.get("VENDOR_CON_PER_NAME") == null ? "": prods.get("VENDOR_CON_PER_NAME").toString();
					strLandLineNo = prods.get("LANDLINE_NO") == null ? "": prods.get("LANDLINE_NO").toString();
					strVendorMobilNo = prods.get("MOBILE_NUMBER") == null ? "": prods.get("MOBILE_NUMBER").toString();
					strEmail = prods.get("EMP_EMAIL") == null ? " " : prods.get("EMP_EMAIL").toString();
				}
				// below contact person along with mobile number given here
				if (!strVendorMobilNo.equals("")) {
					contactPersonName = contactPersonName + " ( "
					+ strVendorMobilNo;
				}
				if (!strLandLineNo.equals("")) {
					contactPersonName = contactPersonName + "," + strLandLineNo;
				}
				contactPersonName = contactPersonName + " )";
				// billing address taken based on user id so taken userId below
				String billingState=objMarketingDepartmentIndentProcessDao.getDeptId(strUserId);
				request.setAttribute("strReceiverState",billingState);
				request.setAttribute("billingState",billingState);
				// for future referrence which po is sitelevel,location wise,brand wise so below check and save in po_area_wise details table
				if(seletedSite.equalsIgnoreCase("SiteWise")){
					strSite=request.getParameter("site");
					siteNumber=strSiteId=String.valueOf(cntlIndentrocss.getSiteIdByName(strSite));// for receive address purpose this is changed to as per client requirement
					
				}if(seletedSite.equalsIgnoreCase("LocationWise")){
					
					siteNumber=request.getParameter("siteNames");
				}if(seletedSite.equalsIgnoreCase("BrandingWise")){
					siteNumber="BrandingWise";
				}
				// receiver address taken here so using same methode like getVendorOrSiteAddress
				List<Map<String, Object>> listReceiverDtls = objMarketingDepartmentIndentProcessDao.getVendorOrSiteAddress(strSiteId);// strToSite
				for (Map<String, Object> prods : listReceiverDtls) {
					receiverName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
					receiverState = prods.get("STATE") == null ? "" : prods.get("STATE").toString();
					strReceiverAddress=prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
				}
				// based on the billing state project Name taken
				if(!seletedSite.equalsIgnoreCase("SiteWise")){
				List<String> projectData=objMarketingDepartmentIndentProcessDao.getProjectAddGstin(billingState); // to get receiver address
				receiverName=projectData.get(1);
				strReceiverAddress=projectData.get(0);
				receiverState=billingState;
				}
				/*if(seletedSite.equalsIgnoreCase("SiteWise")){
				List<Map<String, Object>> listReceiverDtls = objMarketingDepartmentIndentProcessDao.getVendorOrSiteAddress(strSiteId);// strToSite
				for (Map<String, Object> prods : listReceiverDtls) {
					receiverName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
					receiverState = prods.get("STATE") == null ? "" : prods.get("STATE").toString();
					strReceiverAddress=prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
				}
				}*/
				
				
				
				Date date = new java.sql.Date(System.currentTimeMillis());
				SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
				strPoDate = dt1.format(date);
				approvalEmpId = objMarketingDepartmentIndentProcessDao.getTemproryuser(strUserId);// here getting the next approval empId

				editPonumber = request.getParameter("oldPoNumber") == null ? request.getParameter("poNo") == null ? "": request.getParameter("poNo"): request.getParameter("oldPoNumber").toString();// which was used in revised po time for old po number taken here

				// this will create the Perminent PO time
				if (approvalEmpId != null && approvalEmpId.equals("VND")) {
					preparedBy=siteName = "MARKETING_DEPT"; // which is used in pending po approval time show on that page type of po
					operation_Type="CREATION";
					if(editPonumber!=null && !editPonumber.equals("") && editPonumber.startsWith("PO/SIPL")){
						operation_Type="REVISED";
						//strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(editPonumber,"false");
						strPONumber = objMarketingDepartmentIndentProcessDao.getPermenentRevisedPoNumber(editPonumber);
					}else{
						strPONumber = objMarketingDepartmentIndentProcessDao.getPermenentPoNumber(billingState, siteName,sessionSiteId, strFinacialYear);
					}
					temppoOrNot="Ref No"; // same jsp using for permanent and temp po so use it
					poEntryId = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNo();
				}

				else {
					//List<String> tempPoDetails = getAndSaveTempPoDetails(isSiteLevelPo, siteName, editPonumber);// to save
					preparedBy = "MARKETING_DEPT";
					temppoOrNot="Temp Po Number";
					operation_Type="CREATION";
					if(editPonumber!=null && !editPonumber.equals("") && editPonumber.startsWith("PO/SIPL")){
						operation_Type="REVISED";
						strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(editPonumber,"true");
					}
					strPONumber = String.valueOf(objPurchaseDepartmentIndentProcessDao.getTempPoEnterSeqNoOrMaxId());
				
				}
				/*****************************************revised purpose it is used start*******************************************************/
				if(editPonumber!=null && !editPonumber.equals("") && editPonumber.startsWith("PO/SIPL")){
				
				oldPOEntryId= request.getParameter("poEntryId")== null ? "" : request.getParameter("poEntryId").toString();
				changeDate=strPODate=request.getParameter("oldPODate")== null ? "" : request.getParameter("oldPODate").toString();
				String strSiteName=request.getParameter("strSiteName")== null ? "" : request.getParameter("strSiteName").toString();
				Date tempPoDate = DateUtil.convertToJavaDateFormat(strPODate);
				strPoDate = new SimpleDateFormat("dd-MM-yyyy").format(tempPoDate);
				
				//strPoDate=dt1.format(changeDate);
				//strSiteId=request.getParameter("strCreationDate")== null ? "" : request.getParameter("strCreationDate").toString();
				paymentStatus=objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(editPonumber,strSiteId,strVendorId,strPODate,strSiteName,oldPOEntryId,portNumber,false,true);
				if(paymentStatus){
					request.setAttribute("message1","Sorry ! Unable to Revise PO because payment had been initiated.");
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result="response";
				}
				}
				/*********************************************revised po purpose write this one end**********************************************/
				//operation_Type="CREATION";
				List<String> billAddress = objMarketingDepartmentIndentProcessDao.getBillingAddGstin(billingState);// to get billing address
															
				billingAddress = billAddress.get(0);
				strBillingAddressGSTIN = billAddress.get(1);
				strBillingCompanyName = billAddress.get(2);
				
				request.setAttribute("strPONumber", strPONumber);// this is for getting and save attachments
				// here random number used for mail purpose like password													
				Random rand = new Random();
				int rand_Number = rand.nextInt(1000000);
				passwdForMail = String.valueOf(rand_Number);
				productDetails.setUserId(strUserId);
				productDetails.setVendorId(strVendorId);
				productDetails.setStrDeliveryDate(strDeliveryDate);
				productDetails.setSiteId(sessionSiteId);
				
				productDetails.setPoNumber(strPONumber);
				productDetails.setSubject(subject);
				productDetails.setBillingAddress(billingAddress);
				productDetails.setPoEntryId(poEntryId);
				productDetails.setVersionNo(version_no);
				productDetails.setRefferenceNo(refferenceNo);
				productDetails.setStrPoPrintRefdate(strPoPrintRefdate);
				productDetails.setRevision_Number(temprevision_no);
				productDetails.setPreparedBy(preparedBy);
				productDetails.setPasswdForMail(passwdForMail);
				productDetails.setPayment_Req_days(payment_Req_Days);
				productDetails.setOperation_Type(operation_Type);
				productDetails.setEdit_Po_Number(editPonumber);
				//WriteTrHistory.write("Site:" + session.getAttribute("SiteName")+ " , User:" + session.getAttribute("UserName")+ " , Date:" + new java.util.Date() + " , PONumber:"+ strPONumber);

				ccEmailId = request.getParameter("ccEmailId"); // in the submit form user given mail Ids those are take and save in temp table if po is have approval
				String[] terms = request.getParameterValues("termsAndCond");
				
				if (approvalEmpId != null && approvalEmpId.equals("VND")) {
					objMarketingDepartmentIndentProcessDao.insertPOEntry(productDetails);
					objMarketingDepartmentIndentProcessDao.insertPoAreaWiseData(poEntryId,seletedSite,siteNumber);// to save data in area Wise for future use like report i.e sitelevel,locationwise,branding wise we know the po's
				} else {
					objMarketingDepartmentIndentProcessDao.insertTempPOEntry(productDetails, approvalEmpId, ccEmailId, subject,seletedSite,siteNumber);
				}

				for (int i = 0; i < terms.length; i++) {
					if (terms[i] != null && !terms[i].equals("")) {
						if (approvalEmpId != null&& approvalEmpId.equals("VND")) {
							objPurchaseDepartmentIndentProcessDao.saveTermsconditions(terms[i], poEntryId,strVendorId, strIndentNo);
						} else {
							objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions(terms[i],strPONumber, strVendorId,strIndentNo);
						}
						listOfTermsAndConditions.add(terms[i]);
					}

				}
				// to save Transportaion details and assign to table three
			 strTableThreeData=saveTransportDetailsForPo(request,productDetails,approvalEmpId,strVendorGSTIN,receiverState);
			}
			
			productDetails = null;
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			if (StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")) { // here we need to take delivery date convert to  normal format
				Date deliveryDate = DateUtil.convertToJavaDateFormat(strDeliveryDate);
				strDeliveryDate = new SimpleDateFormat("dd-MM-yyyy").format(deliveryDate);
			}
			String strTableOneData = strPONumber + "@@" + strPoDate + "@@"+ strVendorName + "@@"+ strVendorAddress + "@@" + vendorState + "@@"
			+ strVendorGSTIN + "@@" + receiverName + "@@"
			+ strReceiverAddress + "@@" + "" + "@@"+ "" + "@@" + finalamount + "@@" + subject+ "@@" + contactPersonName + "@@" + ccEmailId + "@@"+ billingAddress + "@@" + " " + "@@" + " " + "@@"+ strUserName + "@@"+ new java.sql.Date(System.currentTimeMillis()) + "@@"+ " " + "@@" + " " + "@@" + strIndentCreationDate + "@@"
			+ strBillingAddressGSTIN + "@@"
			+ strReceiveSideContactPerson + "@@" + strReceiverLandLine+ "@@" + strEmail + "@@" + strBillingCompanyName +","+ "@@"+ strSiteId + "@@" + siteName + "@@" + strDeliveryDate+ "@@" + strVendorId+"@@"+temppoOrNot;

			// here we need to take poentry details id so table two data taken to request object
			Map<String, String> locationDetails= saveProductDetailsForPo(session, request,strVendorGSTIN, noofrows, approvalEmpId, recordscount,
							receiverState, editPonumber, strPONumber, oldPOEntryId,poEntryId, isSiteLevelPo, revisedPoForTemp);
			
			strGrandTotalVal = request.getAttribute("pototal").toString();// for payment and poentry to update total amount so taken here
			strTableTwoDate=request.getAttribute("strTableTwoDate").toString();
			
			
			strLocationTableData=saveLocationDetailsForPo(request,approvalEmpId,locationDetails,strPONumber);
			
			if(approvalEmpId != null && approvalEmpId.equals("VND") && editPonumber!=null && !editPonumber.equals("") && editPonumber.startsWith("PO/SIPL")){
				objMarketingDepartmentIndentProcessDao.updateReceivedQuantity(locationDetails,oldPOEntryId);
			}
	
			objPurchaseDepartmentIndentProcessDao.updateTotalAmt(String.valueOf(strGrandTotalVal),approvalEmpId,strPONumber);// set total amount in temp or permanent po table
			if (approvalEmpId != null && approvalEmpId.equals("VND")) {
				
				request.setAttribute("isTempPoOrNot","false");
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls("0",String.valueOf(poEntryId), strUserId,strSiteId, "C", "");
				
			} else {
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(String.valueOf(strPONumber), "0", strUserId,strSiteId, "C", "");
				
				request.setAttribute("isTempPoOrNot","true");
			}
			strResponse = "marketing/Marketing_POPrintPage";
			final VendorDetails vd = new VendorDetails();
			// permanent po time mail send to vendor and this condition executed other wise else condition executed
			String nameEmail = objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(strUserId);
			vd.setVendor_Id(strVendorId);
			final String poNumber = strPONumber;
			final String ccEmailTo = ccEmailId;
			final String subject1 = subject;
			final String finalSiteId = strSiteId;
			final String poCreatedEmpName = nameEmail;
			final int getLocalPort = portNumber;
			final String strRevisedOrNot = revisedOrNot;
			final String old_Po_Number = editPonumber;// this is for revised purpose to show old number in mail
			final String emailPODate = strPODate;
			final List<String> allEmployeeEmailsOfAcc1 = allEmployeeEmailsOfAcc; // it will use in revised po time any changes done in revised po then taken acc_dept table
			final String RevsiedpoNote = acc_Note_Email;
			final String strUser_Id=strUserId;
			if (approvalEmpId != null && approvalEmpId.equals("VND")) {
				ExecutorService executorService = Executors.newFixedThreadPool(10);
				try {executorService.execute(new Runnable() {
						public void run() {sendEmailForPo(strUser_Id, 0, finalSiteId, "siteName", vd,
									poNumber, ccEmailTo, subject1,
									poCreatedEmpName, getLocalPort,
									strRevisedOrNot, old_Po_Number,
									emailPODate, allEmployeeEmailsOfAcc1,
									RevsiedpoNote,"");
						}
					});
					executorService.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
					executorService.shutdown();
				}
			} else {

				String approveEmail = objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(approvalEmpId);
				String data[] = approveEmail.split(",");
				String strApproveName = data[0];
				String approveToEmailAddress[] = { data[1] };
				Date dateobj = new Date();
				String CreationDate = new SimpleDateFormat("dd-MM-yyyy").format(dateobj);
				Object ApproveData[] = { strIndentNo, approvalEmpId, sessionSiteId,
						String.valueOf(temprevision_no), editPonumber,
						CreationDate, poNumber, siteWiseIndentNo, siteName,
						strVendorName, String.valueOf(strGrandTotalVal),
						strApproveName, preparedBy, passwdForMail,
						String.valueOf(getLocalPort),strVendorId,oldPOEntryId,strPODate};
				EmailFunction objEmailFunction = new EmailFunction();
				objEmailFunction.sendMailForMarketingTempApprove(strApproveName,
						approveToEmailAddress, getLocalPort, ApproveData);
			}
			request.setAttribute("listOfTermsAndConditions",listOfTermsAndConditions);

			objviewPOPageDataMap.put("AddressDetails", strTableOneData);
			objviewPOPageDataMap.put("productDetails", strTableTwoDate);
			objviewPOPageDataMap.put("TransportDetails", strTableThreeData);
			objviewPOPageDataMap.put("locationDetails", strLocationTableData);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
			request.setAttribute("receiverState", receiverState);
			// strResponse = "Success";
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			// if close
		}// try close

		catch (Exception e) {
			// strResponse = "Fail";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}

		return strResponse;
	}

	/*
	 * =====================================================send mail for
	 * Marketing Po
	 * start====================================================================
	 */
	public void sendEmailForPo(String userEmpId, int indentNumber,String indentFrom, String indentTo, VendorDetails vendorDetails,
								String poNumber, String ccEmailTo, String subject,String poCreatedEmpName, int getLocalPort, String revisedOrNot,
								String oldPONumber, String emailPoDate,
								List<String> allEmployeeEmailsOfAcc, String acc_Note_Email,String tempPoNumber) {

		try {
			List<String> toMailListArrayList = new ArrayList<String>();
			String emailto[] = null;
			String strIndentFromSite = "";
			String createDate = "now";
			String vendoremail = "";
			String contact_person_Name = "";
											
			Map<String, String> vendordata = (Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorEmail(vendorDetails.getVendor_Id());
			for (Map.Entry<String, String> retVal : vendordata.entrySet()) {
				contact_person_Name = retVal.getKey();
				vendoremail = retVal.getValue();
			}

			if (vendoremail != null && !vendoremail.equals("")) {
				if (vendoremail.contains(",")) {
					for (int i = 0; i < vendoremail.split(",").length; i++) {
						toMailListArrayList.add(vendoremail.split(",")[i]);
					}
				} else {
					toMailListArrayList.add(vendoremail);
				}
			}

			emailto = new String[toMailListArrayList.size()];
			toMailListArrayList.toArray(emailto);

			String marketingDeptId = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
			List<String> allEmployeeEmailsOfMD = new ArrayList<String>();
			List<String> previousEmployeesEmails=new ArrayList<String>();
			//if (!indentFrom.equals("112")) {
				//allEmployeeEmailsOfMD = objMarketingDepartmentIndentProcessDao.getAllEmployeeEmailsMarketingDepartment(marketingDeptId);
			//}
				
			if(tempPoNumber==null || tempPoNumber.equals("")){
				String Email=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(userEmpId); // this is for who can approve it final then take it email 
				
				allEmployeeEmailsOfMD.add(Email.split(",")[1]);
			}
				if(tempPoNumber!=null && !tempPoNumber.equals("")){
					 previousEmployeesEmails=objMarketingDepartmentIndentProcessDao.getEmployeesEmailsForMD(tempPoNumber);
				}
				
				for(int i=0;i<previousEmployeesEmails.size();i++){
					allEmployeeEmailsOfMD.add(previousEmployeesEmails.get(i));
				}
				
				
			if (!ccEmailTo.equals("") && ccEmailTo != null) {
				String[] ccEmailToArray = ccEmailTo.split(",");
				for (int i = 0; i < ccEmailToArray.length; i++) {
					allEmployeeEmailsOfMD.add(ccEmailToArray[i]);
				}
			}
			
			if (!acc_Note_Email.equals("") && acc_Note_Email != null) {
				for (int i = 0; i < allEmployeeEmailsOfAcc.size(); i++) {
					allEmployeeEmailsOfMD.add(allEmployeeEmailsOfAcc.get(i));
				}
			}
			String ccTo[] = new String[allEmployeeEmailsOfMD.size()];
			allEmployeeEmailsOfMD.toArray(ccTo);

			EmailFunction objEmailFunction = new EmailFunction();

			objEmailFunction.sendMailToVendorForMarketingPO(indentTo, indentNumber,
					indentFrom, strIndentFromSite, createDate, emailto,
					vendorDetails, poNumber, ccTo, subject, poCreatedEmpName,
					getLocalPort, contact_person_Name, revisedOrNot,
					oldPONumber, emailPoDate, acc_Note_Email);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// to save temp po detauls for normal and revised po
	private List<String> getAndSaveTempPoDetails(String isSiteLevelPo,String siteName, String editPonumber) {
		String preparedBy = "";
		String strPONumber = "";
		String strPODate = "";
		String revisedPoForTemp = "false";
		List<String> poList = new ArrayList<String>();
		preparedBy = "MARKETING_DEPT";

		int intPONumber = objPurchaseDepartmentIndentProcessDao.getTempPoEnterSeqNoOrMaxId();
		strPONumber = String.valueOf(intPONumber);
		
		poList.add(preparedBy);
		poList.add(strPONumber);
		poList.add(strPODate);
		poList.add(revisedPoForTemp);
		return poList;
	}

	// *******************************************************Product Details For Po Start****************************************************************
	private Map<String ,String> saveProductDetailsForPo(HttpSession session,HttpServletRequest request, String strVendorGSTIN, int noofrows,
						String approvalEmpId, String[] recordscount, String receiverState,String oldPonumber, String strPONumber, String oldPOEntryId,
						int poEntryId, String isSiteLevelPo, String revisedPoForTemp) {
		
		int sno = 0;
		MarketingDeptBean productDetails = null;
		Map<String, String> productData = new HashMap<String, String>();
		String hsnCode = "";
		String price = "";
		String basicAmount = "";
		String taxAmount = "";
		String amountAfterTax = "";
		String totalAmount = "";
		String quantity = "";
		String tax = "";
		double grandtotal = 0.0;
		String strGrandTotalVal = "";
		String taxId = "";
		String strTableTwoDate = "";
		double totalAmt = 0.0;
		String indentCreationDetailsId = "";
		String strgrandtotal = "";
		
		int strPoEntryDetailsId =0; // this is for data store in location details
		String childProductId = "";
		for (int i = 0; i < noofrows; i++) {
			int num = Integer.parseInt(recordscount[i]);
			if (noofrows != 0) {
				sno++;
				//String actionValue = request.getParameter("actionValue" + num);
				String product = request.getParameter("Product" + num);
				String prodsInfo[] = product.split("\\$");
				String productId = prodsInfo[0];
				String productName = prodsInfo[1];

				String subProduct = request.getParameter("SubProduct" + num);
				String subProdsInfo[] = subProduct.split("\\$");
				String subProductId = subProdsInfo[0];
				String subProductName = subProdsInfo[1];

				String childProduct = request.getParameter("ChildProduct" + num);
				String childProdsInfo[] = childProduct.split("\\$");
				childProductId = childProdsInfo[0];
				String childProductName = childProdsInfo[1];

				String mesurement = request.getParameter("UnitsOfMeasurement"+ num);
				String measureInfo[] = mesurement.split("\\$");
				String setMeasurementId = measureInfo[0];
				String setMeasurementName = measureInfo[1];
				String vendorProductDesc = request.getParameter("childProductVendorDesc" + num);
				if (vendorProductDesc.equals("")) {
					vendorProductDesc = "-";

				}
					hsnCode = request.getParameter("HSNCode" + num);
					price = request.getParameter("Price" + num);
					basicAmount = request.getParameter("BasicAmount" + num);

					taxAmount = request.getParameter("TaxAmount" + num);
					amountAfterTax = request.getParameter("AmountAfterTax"
							+ num);
					totalAmount = request.getParameter("TotalAmount" + num);
					quantity = request.getParameter("Quantity" + num);
					tax = request.getParameter("Tax" + num);
					
					if (tax.contains("%")) {
					taxId = tax.split("\\$")[0];
					tax = tax.split("\\$")[1].substring(0,
							tax.split("\\$")[1].length() - 1);
					} else {
					taxId = tax.split("\\$")[0];
					tax = tax.split("\\$")[0];
					}
				
				String Discount = request.getParameter("Discount" + num);
				String amountAfterDiscount = request.getParameter("amtAfterDiscount" + num);
				
				productDetails = new MarketingDeptBean();
				productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setProductId(productId);
				productDetails.setProductName(productName);
				productDetails.setSub_ProductId(subProductId);
				productDetails.setSub_ProductName(subProductName);
				productDetails.setChild_ProductId(childProductId);
				productDetails.setChild_ProductName(childProductName);
				productDetails.setMeasurementId(setMeasurementId);
				productDetails.setMeasurementName(setMeasurementName);
				productDetails.setChildProductCustDisc(vendorProductDesc);
				productDetails.setQuantity(quantity);

				productDetails.setHsnCode(hsnCode);
				productDetails.setPricePerUnit(price);
				productDetails.setBasicAmt(basicAmount);
				productDetails.setStrDiscount(Discount);
				productDetails.setStrAmtAfterDiscount(amountAfterDiscount);
				productDetails.setTax(taxId);
				productDetails.setTaxAmount(taxAmount);
				productDetails.setAmountAfterTax(amountAfterTax);
				productDetails.setTotalAmount(totalAmount);
				
				double totalvalue = Double.valueOf(totalAmount);

				totalAmt = totalAmt + totalvalue;
				totalAmt = Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
				int val = (int) Math.ceil(totalAmt);
				double roundoff = Math.ceil(totalAmt) - totalAmt;
				grandtotal = Math.ceil(totalAmt);
				NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
				String strroundoff = String.format("%.2f", roundoff);
				String strTotalVal = String.format("%.2f", totalAmt);
				strGrandTotalVal = String.format("%.2f", grandtotal);
				strgrandtotal = numberFormat.format(grandtotal);

				double doublePrice = Double.parseDouble(price);
				double doubleBasicAmount = Double.parseDouble(basicAmount);
				double doubleDiscount = Double.parseDouble(Discount);
				doublePrice = Double.parseDouble(new DecimalFormat("##.##").format(doublePrice));
				doubleBasicAmount = Double.parseDouble(new DecimalFormat("##.##").format(doubleBasicAmount));
				String strBasicAmt = String.format("%.2f", doubleBasicAmount);
				doubleDiscount = Double.parseDouble(new DecimalFormat("##.##").format(doubleDiscount));
				List<String> gstDetails=objMarketingDepartmentIndentProcessDao.calculateGstAmount(request,tax,strVendorGSTIN,receiverState,taxAmount);
				
				strTableTwoDate += sno + "@@" + subProductName + "@@"+ childProductName + "@@" + hsnCode + "@@"+ setMeasurementName + "@@" + quantity + "@@"
			      				+ doublePrice + "@@" + strBasicAmt + "@@"+ doubleDiscount + "@@" + amountAfterDiscount+ "@@" + gstDetails.get(0) + "%" + "@@" + gstDetails.get(1) + "@@" + gstDetails.get(2) + "%" + "@@" + gstDetails.get(3)
			      				+ "@@" + gstDetails.get(4) + "%" + "@@" + gstDetails.get(5) + "@@"+ amountAfterTax + "@@" + amountAfterTax + "@@"
			      				+ strroundoff + "@@" + strGrandTotalVal + "@@"+ new NumberToWord().convertNumberToWords(val)+ " Rupees Only." + "@@" + strTotalVal + "@@"
			      				+ vendorProductDesc + "@@" + productName + "@@"+ productId + "@@" + subProductId + "@@"+ childProductId + "@@" + setMeasurementId+ "@@" + indentCreationDetailsId + "&&";
				
				
				
				if (approvalEmpId != null && approvalEmpId.equals("VND")) {
					 strPoEntryDetailsId = objMarketingDepartmentIndentProcessDao.insertPOEntryDetails(productDetails, poEntryId);
				} else {
					strPoEntryDetailsId = objMarketingDepartmentIndentProcessDao.insertTempPOEntryDetails(productDetails,strPONumber);
				}
				
			}
			productData.put(childProductId,String.valueOf(strPoEntryDetailsId));
		}
		request.setAttribute("pototal", strGrandTotalVal);// for payment and
		request.setAttribute("strTableTwoDate", strTableTwoDate); // 
															
		return productData;
	}
	/*========================================================auto complete siteLocations===============================================*/
	public String loadAndSetSiteInfo(String siteName) {
		// TODO Auto-generated method stub
		return objMarketingDepartmentIndentProcessDao.loadAndSetSiteInfo(siteName);
	}
	/*	***************************************************saveTranportDetails For Po********************************************************************/	
	private String saveTransportDetailsForPo(HttpServletRequest request,MarketingDeptBean productDetails,String approvalEmpId,String strVendorGSTIN,String receiverState) {
		// TODO Auto-generated method stub
		String strTableThreeData="";
		String Conveyance="";
		String ConveyanceId="";
		String ConveyanceName="";
		String ConveyanceAmount="";
		String GSTTax="";
		String GSTTaxId="";
		String GSTAmount="";
		String AmountAfterTax="";
		String TransportInvoice="";
		String finalamount="";
		
		String numOfChargeRec[] = null;
		String chargesRecordsCount =  request.getParameter("numbeOfChargesRowsToBeProcessed");
		if((chargesRecordsCount != null) && (!chargesRecordsCount.equals(""))) {
			numOfChargeRec = chargesRecordsCount.split("\\|");
			if(numOfChargeRec != null && numOfChargeRec.length > 0) {
				for(String charNum : numOfChargeRec) {
					TransportChargesDto transportChargesDto = new TransportChargesDto();
					Conveyance = request.getParameter("Conveyance"+charNum);
					ConveyanceId = Conveyance.split("\\$")[0];
					ConveyanceName = Conveyance.split("\\$")[1];
					ConveyanceAmount = request.getParameter("ConveyanceAmount"+charNum);
					GSTTax= request.getParameter("GSTTax"+charNum);
					GSTTaxId = GSTTax.split("\\$")[0];
					GSTTax = GSTTax.split("\\$")[1].substring(0,GSTTax.split("\\$")[1].length()-1);
					GSTAmount = request.getParameter("GSTAmount"+charNum);
					
					AmountAfterTax = request.getParameter("AmountAfterTaxx"+charNum);
					TransportInvoice = request.getParameter("TransportInvoice"+charNum)==null ? "" : request.getParameter("TransportInvoice"+charNum);
					transportChargesDto.setTransportId(ConveyanceId);
					transportChargesDto.setTransportAmount(ConveyanceAmount);
					transportChargesDto.setTransportGSTPercentage(GSTTaxId);
					transportChargesDto.setTransportGSTAmount(GSTAmount);
					transportChargesDto.setTotalAmountAfterGSTTax(AmountAfterTax);
					transportChargesDto.setTransportInvoice(TransportInvoice);
					finalamount = request.getParameter("finalAmntDiv");
					transportChargesDto.setTotalamount(finalamount);

					double doubleConveyanAmt=Double.parseDouble(ConveyanceAmount);
					doubleConveyanAmt=Double.parseDouble(new DecimalFormat("##.##").format(doubleConveyanAmt));
					List<String> gstDetails=objMarketingDepartmentIndentProcessDao.calculateGstAmount(request,GSTTax,strVendorGSTIN,receiverState,GSTAmount);
					
					strTableThreeData += ConveyanceName+"@@"+doubleConveyanAmt+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+gstDetails.get(0)+"%"+"@@"+gstDetails.get(1)+"@@"+gstDetails.get(2)+"%"+"@@"+gstDetails.get(3)+"@@"+gstDetails.get(4)+"%"+"@@"+gstDetails.get(5)+"&&";
					int poTransChrgsSeqNo = objPurchaseDepartmentIndentProcessDao.getPoTransChrgsEntrySeqNo();
					int result2=0;
					if(approvalEmpId!=null && approvalEmpId.equals("VND")){

						objMarketingDepartmentIndentProcessDao.insertPOTransportDetails(poTransChrgsSeqNo,productDetails,transportChargesDto);

					}else{
						result2 = objMarketingDepartmentIndentProcessDao.insertPOTempTransportDetails(poTransChrgsSeqNo,productDetails,transportChargesDto);

					}
				
				}
			}
		}

		return strTableThreeData;
	}
	
	// for temp location details
	private String saveLocationDetailsForPo(HttpServletRequest request,String approvalEmpId,Map<String,String> LocationDetailsForPo,String strPONumber) {
			
			int noofrows=0;
			String po_entryDetailsId="";
			MarketingDeptBean productDetails = null;
			String locationData="";
			double DividedAmount=0.0;
			String locationCount=request.getParameter("locationLength"); // total no of records taken here it contain comma then split it
			
			if(locationCount==null || locationCount.equals("")){
				return locationData;
			}
			
			String recordscount[] = locationCount.split(",");
			List<Date> dates =null; 
			String siteName="";
			String siteId="";
			
			for (int i = 1; i <= recordscount.length; i++) {
				noofrows = i;
			}
			for (int i = 0; i < noofrows; i++) {
				dates=new ArrayList<Date>();
				String locationName="";
				String locationId="";
				int num = Integer.parseInt(recordscount[i]);
				productDetails = new MarketingDeptBean();
				String childProduct = request.getParameter("locationChildProduct"+num);
				// this one is call when the user click on location and field details then not enter data 
				if(childProduct==null || childProduct.equals("") || childProduct.equalsIgnoreCase("--select--")){
					return locationData;
				}
				String childProdsInfo[] = childProduct.split("\\$");
				String child_Prod_Id = childProdsInfo[0];
				String child_Product_Name = childProdsInfo[1];
				String location=request.getParameter("location_mar"+num);
				//locationName=request.getParameter("location_mar"+num);
				if(location.contains("$")){
				String locationInfo[]=location.split("\\$");
				 locationId=locationInfo[0];
				 locationName=locationInfo[1];
				}
				//String locationId=request.getParameter("location_Id"+num);
				String fromDate=request.getParameter("from_date_location"+num);
				String toDate=request.getParameter("to_date_location"+num);
				String time=request.getParameter("timepicker"+num);
				String quantity=request.getParameter("locationQuantity"+num);
				String site=request.getParameter("site_Name"+num);
				// if the user not select site in location and field details then checck below condition then taken marketing site id and name in else block
				if(!site.equals("--select--")){
				if(!site.contains("$")){
					siteId=String.valueOf(cntlIndentrocss.getSiteIdByName(site));
					siteName=site;
				}else{
					String siteInfo[]=site.split("\\$");
					 siteId=siteInfo[0];
					 siteName=siteInfo[1];
				}
				}else{ 
					 siteId="996";
					 siteName="MARKETING";
				}
				String amount_After_Tax=request.getParameter("price_Aftertax"+num);
				String total_Amount=request.getParameter("total_Amount"+num);
				DateFormat DateFormat = new SimpleDateFormat("dd-MMM-yyyy");
				
				productDetails.setChild_ProductId(child_Prod_Id);
				productDetails.setLocation(locationId);
				productDetails.setSiteId(siteId);
				productDetails.setPricePerUnit(amount_After_Tax);
				productDetails.setIntSerialNo(num);
				//productDetails.setFromDate(fromDate);
				//productDetails.setToDate(toDate);
				productDetails.setTime(time);
				productDetails.setQuantity(quantity);
				
				locationData +=child_Product_Name+"@@"+locationName+"@@"+fromDate+"@@"+toDate+"@@"+time+"@@"+quantity+"@@"+siteName+"&&";
				
				/*String str_date ="27/08/2010";
				  String end_date ="02/09/2010";*/

				  DateFormat formatter ; 
				  
				  if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					  toDate=fromDate;
					 }
				 /* if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					  toDate=fromDate;
					 }*/
				  formatter = new SimpleDateFormat("dd-MMM-yyyy");
				  Date startDate = null;
				  try {
				   startDate = (Date)formatter.parse(fromDate);
				  } catch (ParseException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
				  } 
				  Date endDate = null;
				  try {
				   endDate = (Date)formatter.parse(toDate);
				  } catch (ParseException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
				  }
				  long diff = endDate.getTime() - startDate.getTime();
				  long interval = 24*1000 * 60 * 60; // 1 hour in millis
				  long days=diff/interval;
				  DividedAmount=(Double.valueOf(total_Amount))/(days+1);
				  long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
				  long curTime = startDate.getTime();
				  while (curTime <= endTime) {
				      dates.add(new Date(curTime));
				      curTime += interval;
				  }
				  for(int j=0;j<dates.size();j++){
				      Date lDate =(Date)dates.get(j);
				      String ds = formatter.format(lDate);  
				      productDetails.setFromDate(ds);
				      System.out.println(" Date is ..." + ds);
				      productDetails.setTotalAmount(String.valueOf(DividedAmount));

				/*Date dateBefore;
				try {
					dateBefore = DateFormat.parse(fromDate);
					Date dateAfter = DateFormat.parse(toDate);
					long diff = dateAfter.getTime() - dateBefore.getTime();
					long days=diff / (24 * 60 * 60 * 1000);
					long endTime =dateAfter.getTime() ; // create your endtime here, possibly using Calendar or Date
					  long curTime = dateBefore.getTime();
					  while (curTime <= endTime) {
					      dates.add(new Date(curTime));
					      curTime += days;
					  }
					  for(int j=0;j<dates.size();j++){
					      Date lDate =(Date)dates.get(j);
					      String ds = DateFormat.format(lDate);    
					      System.out.println(" Date is ..." + ds);
					  }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			   if(LocationDetailsForPo.containsKey(child_Prod_Id)){
					po_entryDetailsId=LocationDetailsForPo.get(child_Prod_Id).toString();
				if(approvalEmpId!=null && approvalEmpId.equalsIgnoreCase("VND")){
					objMarketingDepartmentIndentProcessDao.saveLocationdetailsData(productDetails,po_entryDetailsId,strPONumber);
					
				}else{
					objMarketingDepartmentIndentProcessDao.saveTempLocationdetailsData(productDetails,po_entryDetailsId,strPONumber);
				}
				}
				}
			}
			
			return locationData;
	}
/*	========================================================get save Location details============================================================*/
	public Map<String, String> loadAndSetLocationData(String childProductId) {
		// TODO Auto-generated method stub
		return objMarketingDepartmentIndentProcessDao.loadAndSetLocationData(childProductId);
	}
	
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber){
		List<IndentCreationBean> list = null;
		try{
			list = objMarketingDepartmentIndentProcessDao.ViewPoPendingforApproval( fromDate,toDate,strUserId,tempPoNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

/*	=======================================================temp po details show=====================================================================*/
	@Override
	public String getDetailsforPoApproval(String poNumber, String siteId,HttpServletRequest request) {

		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="Success";
		String tblOneData="";
		String tbltwoData="";
		String tblCharges="";
		String gstinumber="";
		String deliverySiteState="";
		String siteName=request.getParameter("siteName");
		//String poLevelComments="";
		String locationDetails="";
		
		try {
			
			tblOneData+= objMarketingDepartmentIndentProcessDao.getPendingVendorDetails(poNumber,siteId,request,siteName);
			deliverySiteState=request.getAttribute("deliverySiteState").toString();
			tbltwoData+= objMarketingDepartmentIndentProcessDao.getPendingProductDetails(poNumber,siteId,request,deliverySiteState);
			gstinumber=request.getAttribute("gstinumber").toString();
			
			tblCharges+=objMarketingDepartmentIndentProcessDao.getPendingTransportChargesList(poNumber,siteId,gstinumber,request,deliverySiteState);
			request.setAttribute("receiverState",objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(siteId));
			//request.setAttribute("PoLevelComments",objPurchaseDepartmentIndentProcessDao.getPoLevelComments(poNumber,"false"));
			locationDetails+=objMarketingDepartmentIndentProcessDao.getPendingTempLocationList(poNumber);
			
			request.setAttribute("siteNameForPriceMaster", siteName);
			objviewPOPageDataMap.put("AddressDetails", tblOneData);
			objviewPOPageDataMap.put("productDetails", tbltwoData);
			objviewPOPageDataMap.put("TransportDetails", tblCharges);
			objviewPOPageDataMap.put("locationDetails", locationDetails);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
		} 
		catch (Exception e) {
			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
			result = "Failed";
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");
			e.printStackTrace();
		}
		return result;
	}
	
	/*================================================show perminent po details list============================================================
*/	
	@Override
	public String getMarketingPoDetailsList(String poNumber, String siteId,HttpServletRequest request) {
		
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="Success";
		String tblOneData="";
		String tbltwoData="";
		String tblCharges="";
		String gstinumber="";
		String poentryId="";
		String receiverState="";
		String locationDetails="";
	


		try {
			
			String siteName=request.getParameter("siteName");
			String vendorId=request.getParameter("vendorId1");
			
			tblOneData+= objMarketingDepartmentIndentProcessDao.getMarketingVendorDetails(poNumber,siteId,request,siteName,vendorId);
			poentryId=request.getAttribute("poentryId").toString();
			receiverState=request.getAttribute("receiverState").toString();
			tbltwoData+= objMarketingDepartmentIndentProcessDao.getMarketingProductDetails(poentryId,siteId,request,receiverState);
			gstinumber=request.getAttribute("gstinumber").toString();
			//	terms=request.getAttribute("terms").toString();
			tblCharges+=objMarketingDepartmentIndentProcessDao.getMarketingTransportChargesListForGRN(poentryId,siteId,gstinumber,request,poNumber,receiverState);
			locationDetails+=objMarketingDepartmentIndentProcessDao.getPendingperminentLocationList(poNumber);

			request.setAttribute("siteNameForPriceMaster", siteName);
			objviewPOPageDataMap.put("AddressDetails", tblOneData);
			objviewPOPageDataMap.put("productDetails", tbltwoData);
			objviewPOPageDataMap.put("TransportDetails", tblCharges);
			objviewPOPageDataMap.put("locationDetails", locationDetails);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
			
		} 

		catch (Exception e) {

			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");

			result = "Failed";
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();

			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");

			e.printStackTrace();
		}

		return result;
	}

	/*=================================================to get approve pending data start===============================================================*/
	@Override
	public String SaveMarketingPoApproveDetails(final String tempPONumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPO) {

		
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="failed";

		 String permPoNumber="";
		int val=0;
		String approvalEmpId = "";
		String state="";
		String sessionSite_id="";
		int intPoEntrySeqId =0;
		String revision_No="";
		String old_Po_Number="";
		 
		String strUserId = "";
		String moveFilePath="";
		String rootFilePath ="";
		Random rand = new Random();
		int rand_Number = rand.nextInt(1000000);
		String passwdForMail=String.valueOf(rand_Number);
		List<String> toMailListArrayList = new ArrayList<String>();
		List<String> allEmployeeEmailsOfAcc = new ArrayList<String>();
		 int tempGetLocalPort = request.getLocalPort();
		String siteName=request.getParameter("siteName") == null ? "" : request.getParameter("siteName").toString();
		String ccmailId=request.getParameter("ccEmailId2")== null ? "" : request.getParameter("ccEmailId2").toString();	
		
		revision_No=request.getParameter("revision_No") == null ? "" : request.getParameter("revision_No").toString();	
		old_Po_Number=request.getParameter("oldPoNumber") == null ? "-" : request.getParameter("oldPoNumber").toString();//to get revised po number
		String mailPasswd=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
		String deliveryDate=request.getParameter("deliveryDate") == null ? "" : request.getParameter("deliveryDate").toString();
		
		String vendorId=request.getParameter("vendorId") == null ? "": request.getParameter("vendorId").toString();
		String oldPODate=request.getParameter("oldPODate") == null ? "": request.getParameter("oldPODate").toString();
		String oldPOEntryId=request.getParameter("oldPOEntryId") == null ? "": request.getParameter("oldPOEntryId").toString();
		
		String checkval="";
		String revisedOrNot="";
		String strState="";
		String acc_Note_Email="";
		int LocalPort = request.getLocalPort();
		String remarks="";
		boolean paymentStatus=false;
		double paidAmount=0.0;
		if(LocalPort ==8080){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
			rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();}
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_savPOA, ");
		
		paymentStatus=objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(old_Po_Number,siteId,vendorId,oldPODate,siteName,oldPOEntryId,tempGetLocalPort,false,true);
		if(paymentStatus){
			request.setAttribute("message1","Sorry ! Unable to Revise PO because payment had been initiated.");
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			return result="response";
		}
		
		try{
			
			approvalEmpId = objMarketingDepartmentIndentProcessDao.getTemproryuser(userId);	
			checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(tempPONumber); // to chaeck whether po active or not
			String cancel=objPurchaseDepartmentIndentProcessDao.getCancelOrNot(tempPONumber); // to check whether po is cancel or not
			
			HttpSession session = request.getSession(true);
			strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			 
			if(!cancel.equalsIgnoreCase("CANCEL") && checkval.equalsIgnoreCase("A") && !approvalEmpId.equals("VND")){ // THIS IS FOR MAIL PURPOSE 
			if((strUserId==null || strUserId.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){strUserId=request.getParameter("userId");}//user Approve from mail it is check condition
			if(approvalEmpId!=null && !approvalEmpId.equals("") && !approvalEmpId.equals("VND")){
				WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber);
				val=objMarketingDepartmentIndentProcessDao.updateTempMarketingPoEntry(approvalEmpId,tempPONumber,ccmailId,passwdForMail,deliveryDate);
				if(isCancelTempPO.equals("true")){

					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,"0",strUserId, siteId, "E&S",remarks);

				}else{

					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,"0",strUserId, siteId, "A","");}
				/*	*******************************************ForMail*******************************************************/
					List<String> listOfDetails=objMarketingDepartmentIndentProcessDao.getApproveMailDetails(tempPONumber,approvalEmpId);
					String strApproveName=listOfDetails.get(0);
					String approveToEmailAddress[]=null;
					if(listOfDetails.get(1) != null &&! listOfDetails.get(1).equals("")){
						if(listOfDetails.get(1).contains(",")){
							for(int i=0;i<listOfDetails.get(1).split(",").length;i++){
								toMailListArrayList.add(listOfDetails.get(1).split(",")[i]);
							}
						}else{
							toMailListArrayList.add(listOfDetails.get(1));
						}
					}
					// here we need to send the mail to the next approval user
					approveToEmailAddress = new String[toMailListArrayList.size()];
					toMailListArrayList.toArray(approveToEmailAddress);
				
					Object ApproveData[]={"",approvalEmpId,siteId,revision_No,old_Po_Number,listOfDetails.get(4),tempPONumber,listOfDetails.get(3),
							listOfDetails.get(6),listOfDetails.get(2),listOfDetails.get(5),listOfDetails.get(0),"",passwdForMail,String.valueOf(tempGetLocalPort),vendorId,oldPOEntryId,oldPODate};
				
					EmailFunction objEmailFunction = new EmailFunction();
					objEmailFunction.sendMailForMarketingTempApprove(strApproveName,approveToEmailAddress,tempGetLocalPort,ApproveData);
			
					/**************************************************For Mail End *******************************************************************/

				
				if(val>0){
					result="success";
					request.setAttribute("result", "Temp Po approved Successfully");
				}				
				else{
					result="failed";
					request.setAttribute("result", "Temp Po approved Failed");
				}

			}
			 }//if for mail
			else{
				if(strUserId.equals("")){strUserId=request.getParameter("userId");} // when the user can Approve from mail then taken userId from request Object

				if(checkval.equals("A")){
					request.setAttribute("userId",userId);//this one is used + for get product details to show indent creaation details id
					objMarketingDepartmentIndentProcessDao.getAndMarketingsaveVendorDetails(tempPONumber, siteId,userId,request,revision_No,old_Po_Number,siteName,deliveryDate);
					//request.getAttribute("tempPoNumber").toString();
					//request.setAttribute("siteLevelPoPreparedBy",siteLevelPoPreparedBy);
					permPoNumber=request.getAttribute("permentPoNumber").toString(); // get the poNumber for set in tranportation,location details tables
					WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber+" , PermanentPONumber:"+permPoNumber);

				state=request.getAttribute("State").toString();
				intPoEntrySeqId = Integer.parseInt(request.getAttribute("poEntrySeqID") == null ? "0" : request.getAttribute("poEntrySeqID").toString());
				objPurchaseDepartmentIndentProcessDao.gettermsconditions(tempPONumber,String.valueOf(intPoEntrySeqId)); // get and save terms and conditions from temp to permanent table 
				Map<String, String> locationDetails=objMarketingDepartmentIndentProcessDao.getAndsaveMarketingPoProductDetails(tempPONumber,siteId,request,permPoNumber,intPoEntrySeqId,old_Po_Number);
				
				objMarketingDepartmentIndentProcessDao.getAndsaveMarketingTransportChargesList(tempPONumber,siteId,request,intPoEntrySeqId);
				objMarketingDepartmentIndentProcessDao.getAndsaveMarketingLocationDetailsList(tempPONumber,locationDetails,permPoNumber);
	
				objMarketingDepartmentIndentProcessDao.updateReceivedQuantity(locationDetails,oldPOEntryId);
				
				result="success";
				request.setAttribute("result", "Temp Po approved Successfully and PO Number is : "+permPoNumber);
				request.setAttribute("viewPoPageData", objviewPOPageDataMap);
				strState=request.getAttribute("state").toString();
				if(result.equals("success")){
					// here we need to change from temp po to permanent po i.e  attachments 
					String filepath=rootFilePath+"TEMP_PO//";
					for (int i = 0; i < 8; i++) {
					File file = new File(filepath+tempPONumber+"_Part"+i+".pdf");
					File imageFiles = new File(filepath+tempPONumber+"_Part"+i+".jpg");	
					
					long count=file.length();
					String strpermPoNumber=permPoNumber.replace('/','$');
					if(file.exists()){
					Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".pdf")); //this is for pdf purpose to move temp po to pemenent po
					}
					if(imageFiles.exists()){
						Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".jpg")); //this is for jpg purpose to move temp po to pemenent po
						}
					}
					sessionSite_id=request.getAttribute("sessionSite_id").toString();

					//String vendorId = "";
					String totalAmt=request.getAttribute("totalAmt").toString();
					vendorId = objPurchaseDepartmentIndentProcessDao.getVendorDetails(tempPONumber, siteId,userId,request);
				if(old_Po_Number!=null && !old_Po_Number.equals("-") && !old_Po_Number.equals("") && old_Po_Number.startsWith("PO/SIPL")){
					//below statment: revised po to update in account dept table,indent entry,dc_entry tables 
					
				/*********************************************revised po done at the time acc dept send mail*******************************************/
					revisedOrNot="true";
					paidAmount =objPurchaseDepartmentIndentProcessDao.updateAdvancePaidAmount(request,old_Po_Number,siteId,vendorId,Double.valueOf(totalAmt));
					objPurchaseDepartmentIndentProcessDao.updateAccPayment(old_Po_Number,permPoNumber,String.valueOf(totalAmt),false);
					if(paidAmount>Double.valueOf(totalAmt)){ // paid amount is more than the total amount then check condition and  trigger the mail
						 allEmployeeEmailsOfAcc=(List<String>) request.getAttribute("emailList");
						 acc_Note_Email=" For the difference in amount Paid and amount in Revised PO, Our Marketing Team will contact you.";
					
					}
					
					//int intResult=0;
					/*String resp =inactivePendingApprovalsInAccDeptIfPoAmountDecreased(old_Po_Number,siteId,vendorId,Double.valueOf(totalAmt),allEmployeeEmailsOfAcc,strState);
					if(!resp.equals("NoData")){
						intResult=objPurchaseDepartmentIndentProcessDao.updateAccPayment(old_Po_Number,permPoNumber,totalAmt);
						if(resp.equals("Success")){
							acc_Note_Email="<b>Note</b>:The Payment Initated for Old PO:" +old_Po_Number+" is Deleted.Please Reinitate Payment for Revised PO: "+permPoNumber+".";
						}
					}*/
					objPurchaseDepartmentIndentProcessDao.updateIndentAndDcPONumber(old_Po_Number,permPoNumber,totalAmt);
				}
					
				/*********************************************revised po done at the time acc dept send mail*******************************************/		
					objPurchaseDepartmentIndentProcessDao.updateTotalAmt(totalAmt,approvalEmpId,permPoNumber);
					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,String.valueOf(intPoEntrySeqId),strUserId, siteId, "A","");

					int i=objMarketingDepartmentIndentProcessDao.updateMarketingpoEntrydetails(tempPONumber,passwdForMail,deliveryDate); // here we need to set po as inactive in temp po table

					final VendorDetails vd = new VendorDetails();
					vd.setVendor_Id(vendorId);

					String ccEmailId = request.getParameter("ccEmailId2")== null ? "" : request.getParameter("ccEmailId2").toString();
					if(!ccEmailId.equals("")){
						if(ccEmailId.charAt(ccEmailId.length() - 1)==','){
							ccEmailId = ccEmailId.substring(0,ccEmailId.length()-1);
						}
						if(ccEmailId.charAt(0)=='-'){
							ccEmailId = ccEmailId.substring(1,ccEmailId.length());
						}
					}

					String empId=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpId(tempPONumber);
					String nameEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(empId);
					final String ccEmailTo = ccEmailId;
					final String subject = request.getParameter("subject");
					final String finalSiteId = siteId;
					final String finalPermPoNumber = permPoNumber;
					final String poCreatedEmpName=nameEmail;
					final int getLocalPort = request.getLocalPort();
					final String strRevisedOrNot=revisedOrNot;
					final String strOldPoNumber=old_Po_Number;
					final String strPoDate=request.getAttribute("strPOdate").toString();
					final List<String> allEmployeeEmailsOfAcc1=allEmployeeEmailsOfAcc;
					final String RevsiedpoNote=acc_Note_Email;
					final String strUser_Id=strUserId;
					final String tempPoNumber=tempPONumber;
					ExecutorService executorService = Executors.newFixedThreadPool(10);
					executorService.execute(new Runnable() {
						public void run() {
							sendEmailForPo(strUser_Id, 0, finalSiteId, "",vd,finalPermPoNumber,ccEmailTo,subject,poCreatedEmpName,getLocalPort,strRevisedOrNot,
									strOldPoNumber,strPoDate,allEmployeeEmailsOfAcc1,RevsiedpoNote,tempPoNumber);
						}
					});
					executorService.shutdown();


				}

			}//else
			}//IF FOR SATUS
			
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		}catch (Exception e) {

			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");

			result = "failed";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();

			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");

			e.printStackTrace();
		}
		return result;
	}
	// this is used in ajax call time
	public Map<String, String> getLocationData(String siteName) {
		// TODO Auto-generated method stub
		return objMarketingDepartmentIndentProcessDao.getLocationData(siteName);
	}

	public Map<Double,List<MarketingDeptBean>> getAllViewExpenditures(String invoiceId) {
		logger.info("***** The control is inside the getAllViewExpenditures in MarketingDepartmentServiceImpl ******");
		int serialNo = 1;
		double totalAmount = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-yyyy");
		List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
		Map<Double,List<MarketingDeptBean>> marketingPOProductDetailsMap = new HashMap();
		
		List<Map<String,Object>> expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpenditures(invoiceId);
		for(Map<String,Object>map : expendituresList ) {
			MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setSerialno(serialNo);
			//marketingPOProductDetails.setExpendatureId(map.get("EXPENDATURE_ID").toString());
			marketingPOProductDetails.setChild_ProductName((String)map.get("CHILD_PRODUCT_NAME"));
			marketingPOProductDetails.setChild_ProductId((String)map.get("CHILD_PRODUCT_ID"));
			
			marketingPOProductDetails.setInvoiceDate(sdf1.format((Date)map.get("INVOICE_DATE")));
			
			logger.info("***** setInvoiceDate ***** "+sdf1.format((Date)map.get("INVOICE_DATE")));
			
			marketingPOProductDetails.setSiteId(map.get("SITE_ID").toString());
			marketingPOProductDetails.setSiteName((String)map.get("SITE_NAME"));
			marketingPOProductDetails.setLocation((String)map.get("LOCATION"));
			marketingPOProductDetails.setHoardingId((String)map.get("LOCATION_ID"));
			marketingPOProductDetails.setQuantity((String)map.get("QUANTITY"));
			
			if((map.get("FROM_DATE") != null ) && !map.get("FROM_DATE").equals("")){
			   marketingPOProductDetails.setFromDate(sdf.format(map.get("FROM_DATE")));
			}
			if((map.get("TO_DATE") != null ) && !map.get("TO_DATE").equals("")){
			      marketingPOProductDetails.setToDate(sdf.format(map.get("TO_DATE")));
			
			}
			if((map.get("TIME") != null ) && !map.get("TIME").equals("")){
				marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString() ));
			
			}
		    
		    //marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString().substring(1) ));
		   // marketingPOProductDetails.setTime(new SimpleDateFormat("hh:mm aa").format(((Date)map.get("TIME")).getTime()));  
		    double	amount = Double.valueOf((String)map.get("AMOUNT"));
		    totalAmount = totalAmount + amount;
			marketingPOProductDetails.setAmount((String)map.get("AMOUNT"));
			marketingPOProductDetailsList.add(marketingPOProductDetails);
			serialNo++;
			logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
			logger.info("**** The MarketingPOProductDetails object totalAmount is *****"+totalAmount);
		}
	logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
	 marketingPOProductDetailsMap.put(totalAmount,marketingPOProductDetailsList);
	 logger.info("**** The marketingPOProductDetailsMap object data is ******"+marketingPOProductDetailsMap);
		return marketingPOProductDetailsMap;
	}	
	
	public List<MarketingDeptBean> getAllViewExpenditures(String invoiceFromDate,String invoiceToDate){
		
		logger.info("***** The control is inside the getAllViewExpenditures in invoiceFromDate&invoiceToDate in MarketingDepartmentServiceImpl ******");
		int serialNo = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
		List<Map<String,Object>> expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpenditures((invoiceFromDate),(invoiceToDate));
		for(Map<String,Object>map : expendituresList ) {
			MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setSerialno(serialNo);
			marketingPOProductDetails.setInvoiceNumber((String)map.get("INVOICE_ID"));
			marketingPOProductDetails.setInvoiceAmount((String)map.get("INVOICE_AMOUNT"));
		    marketingPOProductDetails.setInvoiceDate( sdf.format((Date)map.get("INVOICE_DATE")));
		    marketingPOProductDetailsList.add(marketingPOProductDetails);
			serialNo++;
			logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
		}
		
		logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
		return marketingPOProductDetailsList;
		
		}

	public Map<Double,List<MarketingDeptBean>> getViewMyHoardingDetails(String fromDate, String toDate, String Site) {
		logger.info("***** The control is inside the ViewMyHoardingDetails in MarketingDepartmentServiceImpl ******");
		int serialNo = 1;
		double totalAmount = 0.0;
		logger.info("**** The fromDate value is ****" + fromDate);
		logger.info("**** The toDate value is ****" + toDate);
		logger.info("**** The site value is ****" + Site);
		 // here date will convert into dd-mm-yy
		if(fromDate != "") {
		fromDate = DateUtil.DD_MMM_YYToDD_MM_YY(fromDate);
		}
		if(toDate != "") {
		toDate = DateUtil.DD_MMM_YYToDD_MM_YY(toDate);
		}
		DecimalFormat formatter = new DecimalFormat("#.##");  // after two decimals set two values
		
		logger.info("**** The fromDate after change value is ****" + fromDate);
		logger.info("**** The toDate after change value is ****" + toDate);
		logger.info("**** The site after change value is ****" + Site);

		List<Map<String, Object>> HoardingDetailsList = objMarketingDepartmentIndentProcessDao.getViewMyHoardingDetails(fromDate,
				toDate, Site);
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Map<Double,List<MarketingDeptBean>> marketingPOProductDetailsMap = new HashMap();
		List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
		for (Map<String, Object> map : HoardingDetailsList) {
			MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setSerialno(serialNo);
			marketingPOProductDetails.setChild_ProductName((String) map.get("CHILD_PRODUCT_NAME"));
			marketingPOProductDetails.setLocation((String) map.get("HOARDING_AREA"));
			marketingPOProductDetails.setHoardingDate(sdf.format((Date) map.get("HOARDING_DATE")));
			marketingPOProductDetails.setQuantity((String) map.get("QUANTITY"));
			double	amount = Double.valueOf((String)map.get("AMOUNT"));
			String tempAmount=String.format("%.2f",amount);
		    totalAmount = totalAmount +Double.valueOf(tempAmount);
			marketingPOProductDetails.setAmount(tempAmount);
			marketingPOProductDetails.setRate((String) map.get("RATE"));
			marketingPOProductDetails.setSiteName((String) map.get("SITE_NAME"));

			marketingPOProductDetailsList.add(marketingPOProductDetails);
			serialNo++;
			logger.info("**** The MarketingPOProductDetails object is *****" + marketingPOProductDetails);

		}
		System.out.print(formatter.format(totalAmount));
		BigDecimal bigDecimalPoTotal = new BigDecimal(totalAmount); // this is for exponetial value come so write this
		String tempTotalAmount=String.valueOf(bigDecimalPoTotal.setScale(2,RoundingMode.CEILING));
		//totalAmount=bigDecimalPoTotal.setScale(2,RoundingMode.CEILING);
		String sValue = (String) String.format("%.2f", totalAmount);
		 Double newValue = Double.parseDouble(sValue);
		 System.out.print("the value of double is"+newValue); 
		logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
		 marketingPOProductDetailsMap.put(Double.valueOf(tempTotalAmount),marketingPOProductDetailsList);
		 logger.info("**** The marketingPOProductDetailsMap object data is ******"+marketingPOProductDetailsMap);
		return marketingPOProductDetailsMap;
	}
	
	public void updateExpenditure() {
		logger.info("***** The control is inside the updateExpenditure service  in MarketingDepartmentServiceImpl ******");
		objMarketingDepartmentIndentProcessDao.updateExpenditure();
		
		
	}
	public List<MarketingDeptBean> getAvailableAreaForSaleOnMonthWise(String strMonth){

		return objMarketingDepartmentIndentProcessDao.getAvailableAreaForSaleOnMonthWise(strMonth,"");
	}

	public   String getAvailableAreaForSale( String strLocation,String month_year){

		return objMarketingDepartmentIndentProcessDao.getAvailableAreaForSale( strLocation,month_year);
	}
	// user click on the updatre available area them it will calla
	public   String updateAvailableArea(HttpSession session,HttpServletRequest request){

		String strResponse = "";
		boolean isAnyActionPerformedinJSP = false;
		List<String> list=new ArrayList<String>(); // this is for update availablearea purpose
		List<String> removeList=new ArrayList<String>(); // this is for update availablearea remove purpose
		String user_id=String.valueOf(session.getAttribute("UserId"));
		List<String> hydList=new ArrayList<String>(); 
		List<String> bngList=new ArrayList<String>(); 
		boolean deleteList=false;
		String site_Ids="";
		String  expendatureType="";
		Map<String,Double> deldata=new HashMap<String,Double>();
		//Map<String,String> delmap=new HashMap<String,String>();
		//String bngState=validateParams.getProperty("STATE_BNG") == null ? "" : validateParams.getProperty("STATE_BNG").toString();
		String state=validateParams.getProperty("STATE_HYD") == null ? "" : validateParams.getProperty("STATE_HYD").toString();
		String locationSites=validateParams.getProperty("HYD_SITES_LOCATION") == null ? "" : validateParams.getProperty("HYD_SITES_LOCATION").toString();
		try{

			//we are geting 2,3,4
			String strRows = request.getParameter("totalNoOfRows") == null ? "0" : request.getParameter("totalNoOfRows").toString();


			String [] strTablerows = strRows.split(",");



			//reading from hiddenform field
			String strMonth_Year = request.getParameter("month_year") == null ? "0" : request.getParameter("month_year").toString();

			//strMonth_Year = "11-2018";

			int intRowNo  = 0;
			String strUSreId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String strSiteId = "";
			for(int i = 0 ; i<strTablerows.length; i++){

				intRowNo = Integer.valueOf(strTablerows[i]);
				
				strSiteId = request.getParameter("siteId"+intRowNo) == null ? "0" : request.getParameter("siteId"+intRowNo).toString();
				String strUOM = request.getParameter("UOM"+intRowNo) == null ? "0" : request.getParameter("UOM"+intRowNo).toString();
				double doubleTotalArea = Double.valueOf(request.getParameter("totalArea"+intRowNo) == null ? "0" : request.getParameter("totalArea"+intRowNo).toString());
				double doubleAvailableArea = Double.valueOf(request.getParameter("availableArea"+intRowNo) == null ? "0" : request.getParameter("availableArea"+intRowNo).toString());
				String strStatus = request.getParameter("status"+intRowNo) == null ? "0" : request.getParameter("status"+intRowNo).toString();
				String strAction = request.getParameter("areaActionValue"+intRowNo) == null ? "" : request.getParameter("areaActionValue"+intRowNo).toString();
				
				if(strAction.equals("N")){
					
					String strSiteName = request.getParameter("siteName"+intRowNo) == null ? "0" : request.getParameter("siteName"+intRowNo).toString();
					strSiteId = strSiteName.split("\\$")[0];
					
					
				}
				if(!strAction.equals("R")){
				list.add(strSiteId);
				}
				//strAction = "M";
				//strAction = "S"; i.e data is not modified
				if(strAction != null && !strAction.equals("") && !strAction.equals("S")){ //

					if(strAction.equals("N")){// added new row
						//list.add(strSiteId);
						objMarketingDepartmentIndentProcessDao.insertNewAvailableArea(strUSreId, strSiteId, strUOM, doubleTotalArea, doubleAvailableArea, strMonth_Year,strStatus);
					}else if(strAction.equals("E")){  // data modified
						//list.add(strSiteId);
						objMarketingDepartmentIndentProcessDao.updateAvailableArea(strUSreId, strSiteId, strUOM, doubleTotalArea, doubleAvailableArea, strMonth_Year, strStatus);
					}
					else if(strAction.equals("R")){  // data deleted i.e inactive
						removeList.add(strSiteId);
						if(locationSites.contains(strSiteId)){
							
							//delmap.put(state,strSiteId);
							hydList.add(strSiteId);
						}else{
							//delmap.put(bngState,strSiteId);
							bngList.add(strSiteId);
						}
						//objMarketingDepartmentIndentProcessDao.inactiveAvailableArea(strUSreId, strSiteId, strMonth_Year,"I");
					}
					/*if(removeList!=null && removeList.size()>0){
						objMarketingDepartmentIndentProcessDao.inactiveAvailableArea(strUSreId, strSiteId, strMonth_Year,"I",delmap);
					}*/
					isAnyActionPerformedinJSP = true;
				}


			}

			/******************************** this is used to take the location and branding wise start***********************************/
			if(isAnyActionPerformedinJSP){
			if(list.size()>0){
			Map<String,String> map=objMarketingDepartmentIndentProcessDao.getLocationBrandingData(list);
			logger.info("***********************list of sites for location and branding wise *****************************"+map);
			
			//int count=0;
			boolean location=false;
			boolean branding=false;
			
		//	List<String> siteIds=new ArrayList<String>();
			for ( String key : map.keySet() ) {
				if(map.get(key).equalsIgnoreCase(state)){
					location=true;
					site_Ids+=key+",";
				}else{
					branding=true;
					site_Ids+=key+",";
					//count++;
				}
			    //System.out.println( key );
			}
			site_Ids=site_Ids.substring(0,site_Ids.length()-1);
			site_Ids=site_Ids.replace(",","','");
			if((location)&&(branding)){
				expendatureType="Locationwise,Brandwise";
			}else if((location)&&(!branding) || (!location)&&(branding)){
				expendatureType="Locationwise";
			}
			}// if condition end
			logger.info("**********************expenditure type in update available area ************************"+expendatureType);
			/******************************** this is used to take the location and branding wise end***********************************/
			
			if(removeList!=null && removeList.size()>0){
				deleteList=true;
				boolean status=objMarketingDepartmentIndentProcessDao.inactiveAvailableArea(strUSreId, strSiteId, strMonth_Year,"I",hydList,bngList,request);
				if(status){
					request.setAttribute("message1","Already Received as Invoice.");
					strResponse = "falied";
					
					return strResponse;
				}
			}
			
			
			//if(isAnyActionPerformedinJSP){

				// getLatMonth Invoices data
				List<MarketingDeptBean> listOfAvailArea = objMarketingDepartmentIndentProcessDao.getAvailableAreaForSaleOnMonthWise(strMonth_Year,site_Ids);

				double totalAvailArea = objMarketingDepartmentIndentProcessDao.getTotalAvailableAreaInMonth(strMonth_Year,site_Ids); // getting total area

				
				double hydLocation=objMarketingDepartmentIndentProcessDao.getTotalAvailableAreaInMonthForHyd(strMonth_Year,true);
				double bngLocation=objMarketingDepartmentIndentProcessDao.getTotalAvailableAreaInMonthForHyd(strMonth_Year,false);
				
				logger.info("**********************update available area upto given month total availablem area ************************"+totalAvailArea);
				logger.info("**********************update available area upto given taken state hyd sites************************"+hydLocation);
				logger.info("*********************update available area upto given taken state bng sites************************"+bngLocation);
				
				//String [] expendatureType = {"Locationwise","Brandwise"};
				  //expendatureType = " Locationwise , Brandwise";
				List<Map<String, Object>> listLocationwiseInvoice = objMarketingDepartmentIndentProcessDao.getMonthlyWiseExpendatureInvoices(expendatureType,strMonth_Year);

				
				if(listLocationwiseInvoice != null && listLocationwiseInvoice.size() > 0){

					int intExpendatureId = 0;
					List<Map<String, Object>> productDtls =  null;
					String strChildProductId = "";
					double doubleTotalAmount = 0.0;
					double doubleAvailArea = 0.0;
					//double updtedTotalAmountProductwise = 0.0;

					double doubleQty = 0;
				//	double doubleProdcutRate = 0;
					String locationType="";
					
					for(Map<String, Object> prods : listLocationwiseInvoice) {

						intExpendatureId =  Integer.parseInt(prods.get("EXPENDATURE_ID")==null ? "0" : prods.get("EXPENDATURE_ID").toString());
						//strSiteName =  prods.get("SITE_NAME")==null ? "0" : prods.get("SITE_NAME").toString();

						
						productDtls =  objMarketingDepartmentIndentProcessDao.getExpendatureDetilsInvoiceProductWise(intExpendatureId,removeList); //based on childproduct id getting total amount of invoices and child product
						if(deleteList){
						deldata=objMarketingDepartmentIndentProcessDao.getRemoveAreaProductWise(intExpendatureId,removeList);} // delete the area and update the expenditure
						for(Map<String, Object> proddtls : productDtls) { // here list of childproduct,invoice,quantity,totalamount taken here and update based on formula

							strChildProductId =  proddtls.get("CHILD_PRODUCT_ID")==null ? "0" : proddtls.get("CHILD_PRODUCT_ID").toString();
							doubleTotalAmount = Double.valueOf( proddtls.get("TOTAL_AMOUNT")==null ? "0" : proddtls.get("TOTAL_AMOUNT").toString());
							doubleQty = Double.valueOf(proddtls.get("QTY")==null ? "0" : proddtls.get("QTY").toString());
							locationType=proddtls.get("EXPENDATURE_TYPE")==null ? "0" : proddtls.get("EXPENDATURE_TYPE").toString();
							if(deleteList && deldata.containsKey(strChildProductId)){
								doubleTotalAmount+=deldata.get(strChildProductId);
							}
							
							
							Iterator itr = listOfAvailArea.iterator();
							while(itr.hasNext()){

								MarketingDeptBean objMarketingDeptBean = (MarketingDeptBean) itr.next();
								strSiteId = objMarketingDeptBean.getSiteId();
								doubleAvailArea = objMarketingDeptBean.getDoubleAVailableArea();
								if(locationType.equalsIgnoreCase("LocationWise")){
									if(locationSites.contains(strSiteId)){ // this is for hyd location purpose
										calculateUpdateExpdtlsForLocationAndBrandWise(intExpendatureId,strChildProductId,doubleTotalAmount,doubleAvailArea,hydLocation,doubleQty,strSiteId,locationType,user_id);
									}else{ // this is for bng location purpose
										calculateUpdateExpdtlsForLocationAndBrandWise(intExpendatureId,strChildProductId,doubleTotalAmount,doubleAvailArea,bngLocation,doubleQty,strSiteId,locationType,user_id);
									}
									
								}else{ // this is for branding wise 
								calculateUpdateExpdtlsForLocationAndBrandWise(intExpendatureId,strChildProductId,doubleTotalAmount,doubleAvailArea,totalAvailArea,doubleQty,strSiteId,locationType,user_id);
								}
								
							}// while condition
						}// for loop product dtls end

					} // for loop end for list invoice details 

				}// if condition for list of invoices end
				
			} // any action performed this if end
			strResponse = "success";
			request.setAttribute("message","Updated SuccessFully");
		}catch(Exception e){
			e.printStackTrace();
			strResponse = "exception";  
		}

		return strResponse;
	}
	public int getExpenditureId() {
		return objMarketingDepartmentIndentProcessDao.getExpenditureId();
	}

	public int getExpenditureDetailsId() {
	return	objMarketingDepartmentIndentProcessDao.getExpenditureDetailsId();
	}
	
	public int insertMarketExpenditure(MarketingDeptBean objMarketingExpenditure) {
		logger.info("***** The control is inside the insertMarketExpenditure in MarketingDepartmentServiceImpl ******");
		return objMarketingDepartmentIndentProcessDao.insertMarketExpenditure(objMarketingExpenditure);
	}
	public int insertMarketExpenditureDtls(MarketingDeptBean objMarketingExpenditureDtls) {
		return objMarketingDepartmentIndentProcessDao.insertMarketExpenditureDtls(objMarketingExpenditureDtls);
	}
	public int insertAndUpdatePrevMarketExpenditure(MarketingDeptBean objMarketingExpenditure) {
		return objMarketingDepartmentIndentProcessDao.insertAndUpdatePrevMarketExpenditure(objMarketingExpenditure);
	}
	/*public int insertMarketExpenditureDtls(List<MarketingDeptBean> objMarketingExpenditureDtls) {
		return marketingDepartmentDaoImpl.insertMarketExpenditureDtls(objMarketingExpenditureDtls);
	}*/
	
	
	public MarketingDeptBean expenditureDetails(Integer expendatureId) {
		logger.info("***** The control is inside the expenditureDetails in MarketingDepartmentServiceImpl ******");
		logger.info("**** The input values for the expendatureId *****"+expendatureId);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		MarketingDeptBean marketingPOProductDetails = null;
		List<Map<String,Object>> expendituresList	= objMarketingDepartmentIndentProcessDao.expenditureDetails(expendatureId);
		for(Map<String,Object>map : expendituresList ) {
			
		    marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setInvoiceNumber(map.get("INVOICE_ID").toString());
			marketingPOProductDetails.setInvoiceAmount(map.get("INVOICE_AMOUNT").toString());
			marketingPOProductDetails.setExpendatureMonth(map.get("EXPENDATURE_MONTH").toString());
			marketingPOProductDetails.setInvoiceDate(sdf.format((Date)map.get("INVOICE_DATE")));
			// new java.sql.Timestamp(new java.util.Date().getTime()),
			
			
			logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
			logger.info("**** The MarketingPOProductDetails List object size is *****"+expendituresList.size());
			
		}
		
		return marketingPOProductDetails;
	}
	
	@Override
	public LinkedList getAllViewExpenditures(String invoiceId,String invoiceToDate,String invoiceFromDate ){
		
		return objMarketingDepartmentIndentProcessDao.getAllViewExpenditures(invoiceId,invoiceToDate,invoiceFromDate);
	}
	
	
	
	@Override
	public int[] insertMarketExpenditureDtls( final List<MarketingDeptBean> objMarketingExpenditureDtlsList){
		
		return objMarketingDepartmentIndentProcessDao.insertMarketExpenditureDtls(objMarketingExpenditureDtlsList);
	}
	
	
	
	public static void main(String [] args){

		String strSiteName = "107$$$soham";
		strSiteName = strSiteName.split("\\$$$")[0];
		System.out.println(strSiteName.replace("$$$", "-").split("-")[0]);
	}
	
	// inwards from po time it will call and executed
		@Override
	public String processingMarketingPOasInvoice(Model model, HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InRe_proPOI, ");
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
		String finalresponse = "Failed@@_";
		
		try{
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+request.getParameter("poNo")+" , InvoiceNumber:"+request.getParameter("InvoiceorDCNumber"));

			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			
			map.put("typeOfPurchase","PO");
			map.put("poNo",request.getParameter("poNo"));
			map.put("invoiceNumber",request.getParameter("InvoiceorDCNumber"));
			map.put("invoiceDate",request.getParameter("InvoiceorDCDate"));
			map.put("vendorName",request.getParameter("vendorName"));
			map.put("vendorId",request.getParameter("VendorId"));
			map.put("vendorAddress",request.getParameter("vendorAddress"));
			map.put("gstinNumber",request.getParameter("strGSTINNumber"));
			map.put("note",request.getParameter("Note"));
			map.put("state",request.getParameter("state"));
			map.put("ttlAmntForIncentEntryTbl",request.getParameter("ttlAmntForIncentEntry"));
			map.put("poDate",request.getParameter("poDate"));
			map.put("eWayBillNo",request.getParameter("eWayBillNo"));
			map.put("vehileNo",request.getParameter("vehileNo"));
			map.put("transporterName",request.getParameter("transporterName"));
			map.put("otherChrgs",request.getParameter("otherCharges"));//quotes placed
			map.put("receviedDate",request.getParameter("receivedDate"));
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");
			map.put("payment_Req_Date",request.getParameter("payment_Req_Date")); // no of days for the payment request days 
			map.put("chargesRecordsCount", transRows);
			String[] recordsCountArray = recordsCount.split("\\|1\\|"); // here one is given for user select the service type as invoice so here given 1
			recordsCount = recordsCountArray[0];
			recordsCount = recordsCount + "|";
			String creditRows="";
			if(recordsCountArray.length>1){
				creditRows = recordsCountArray[1]; 
			}
			creditRows = "1|" + creditRows;
			
			map.put("creditRows", creditRows);
			
			
			String transportId ="Conveyance";
			String transportGSTPercentage = "GSTTax";
			String transportGSTAmount = "GSTAmount";
			String totalAmountAfterGSTTax = "AmountAfterTax";
			String transportInvoiceId = "TransportInvoice";
			String transportAmount = "ConveyanceAmount";

			String trans_rows[] = transRows.split("\\|");
			for(String trans : trans_rows){
				int charNum=Integer.valueOf(trans);
				Map<String,String> transMap = new HashMap<String,String>();
				String action=request.getParameter("TransportInvoicehidden"+charNum);
				transMap.put("transactionDts",request.getParameter(transportId+charNum));
				transMap.put("gstPercentage",request.getParameter(transportGSTPercentage+charNum));
				transMap.put("gstAmount",request.getParameter(transportGSTAmount+charNum));
				transMap.put("totAmtAfterGSTTax",request.getParameter(totalAmountAfterGSTTax+charNum));
				transMap.put("transactionInvoiceId",request.getParameter(transportInvoiceId+charNum));
				transMap.put("transAmount",request.getParameter(transportAmount+charNum));
				transMap.put("action",request.getParameter("TransportInvoicehidden"+charNum));
				
				transMapList.add(transMap);
			}


			String prod = "mainProduct";
			String subProd = "mainSubProduct";
			String childProd = "mainChildProduct";
			String qty = "mainquantity";
			//String expireDate = "mainexpireDate";
			String price = "mainPrice";
			String basicAmount = "mainamtAfterDiscount";
			String measurements = "mainUnitsOfMeasurement";				
			String gstTax = "maintax";
			String hsnCode = "mainhsnCode";
			String taxAmount = "maintaxAmount";				
			String amountAfterTax = "mainamountAfterTax";				
			String otherOrTransportCharges = "mainotherOrTransportCharges";				
			String taxOnOtherOrTransportCharges = "maintaxOnOtherOrTransportCharges";				
			String otherOrTransportChargesAfterTax = "mainotherOrTransportChargesAfterTax";				
			String totalAmount = "maintotalAmount";
			String otherCharges = "mainOtherCharges";
			String expireDate = "expireDate";
			
			
			String recordsCountModified = "";
			int records_Count = recordsCount.split("\\|").length;
			String[] records_Array = recordsCount.split("\\|");
			for(int index=0;index<records_Count;index++){
				int num=Integer.parseInt(records_Array[index]);
				Map<String,String> prodMap = new HashMap<String,String>();
				prodMap.put("product",request.getParameter(prod+num));					
				prodMap.put("subProduct",request.getParameter(subProd+num));
				prodMap.put("expiryDate",request.getParameter(expireDate+num));
				prodMap.put("childProduct",request.getParameter(childProd+num));
				
				String quantity = request.getParameter(qty+num);
				prodMap.put("quantity",quantity);
				String basicAmnt = request.getParameter(basicAmount+num);
				prodMap.put("basicAmnt",basicAmnt);
				String prc = String.valueOf(Double.valueOf(basicAmnt)/Double.valueOf(quantity));
				prodMap.put("prc",prc); 
				
				prodMap.put("unitsOfMeasurement",request.getParameter(measurements+num));
				prodMap.put("tax",request.getParameter(gstTax+num));
				prodMap.put("hsnCd",request.getParameter(hsnCode+num));
				prodMap.put("taxAmnt",request.getParameter(taxAmount+num));
				prodMap.put("amntAfterTax",request.getParameter(amountAfterTax+num));
				prodMap.put("otherOrTranportChrgs",request.getParameter(otherOrTransportCharges+num));
				prodMap.put("taxOnOtherOrTranportChrgs",request.getParameter(taxOnOtherOrTransportCharges+num));
				prodMap.put("otherOrTransportChrgsAfterTax",request.getParameter(otherOrTransportChargesAfterTax+num));
				prodMap.put("totalAmnt",request.getParameter(totalAmount+num));
				prodMap.put("otherChrgss",request.getParameter("otherChrgs"+num));//quotes placed
				prodMap.put("remarks",request.getParameter("InvoiceRemarks"+num));
				prodMap.put("indentCreationDetailsId",request.getParameter("mainindentCreationDetailsId"+num));	
				prodMap.put("poEntryDetailsId",request.getParameter("mainPoEntryDetailsId"+num));	
				
				prodMapList.add(prodMap);
				recordsCountModified=recordsCountModified+(index+1)+"|"; // it will contain the and add no of products i.e count
			}
		//	System.out.println(map);
		//	System.out.println(transMapList);
		//	System.out.println(prodMapList);
			map.put("recordsCount",recordsCountModified);
			
			String indentNumber = request.getParameter("indentNumber");
			String reqSiteId = request.getParameter("toSiteId");
			String poNo = map.get("poNo");
			
			int indentEntrySeqNum = ird.getIndentEntrySequenceNumber();
			session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
			request.setAttribute("indentEntryNo",indentEntrySeqNum);
			/*************** MARKETING *************/
			saveMarketingExpenditure(map,request,session); // save marketing expenditure details 
			/*************************************/
//int i=9/0;
			//============================

			String response1 = irsi.indentProcessCommmon(map,transMapList,prodMapList,request,session);

			//============================
			String[] response_array = response1.split("@@");
			String response = response_array [0];
			String response2 = "Failed";

			//String response = "Success",response1 = "Success",response2 = "Success"; //remove this line
			if (response.equalsIgnoreCase("Success")){
				try {
					//doing Credit Note
					if(request.getParameter("ChildProduct1")!=null){
					irsi.doCreditNote(map,request,"QUANTITY","INV");
					}
					else{
						System.out.println("credit not done");
					}
					
				
				
					
					for(Map<String,String> productMap:prodMapList){
						String childProduct = productMap.get("childProduct");
						String childProdsInfo[] = childProduct.split("\\$");					
						String childProdId = childProdsInfo[0];
						double creditQuantity = request.getAttribute(childProdId)==null ? 0.0 : Double.parseDouble(request.getAttribute(childProdId).toString());
						double takenQuantity = Double.parseDouble(productMap.get("quantity").toString());
						String totalQuantity = String.valueOf(creditQuantity+takenQuantity);
						
						ird.updateReceiveQuantityInIndentCreationDtls(totalQuantity,productMap.get("indentCreationDetailsId"));
						ird.updateAllocatedQuantityInPurchaseDeptTable(totalQuantity,productMap.get("indentCreationDetailsId"),request);
						ird.updateReceivedQuantityInPoEntryDetails(totalQuantity,productMap.get("poEntryDetailsId"),request);
					}
					ird.setPOInactive(poNo,reqSiteId);
					
					//ird.setIndentInactiveAfterChecking(indentNumber);
					String creditNoteNumber = request.getParameter("creditNoteNumber");
					String creditTotalAmount = request.getParameter("creditTotalAmount");
					String indentEntryNo = request.getAttribute("indentEntryNo").toString();
					String site_id = String.valueOf(session.getAttribute("SiteId"));
					ird.updateInvoiceNoInAccPaymentTbl(map.get("invoiceNumber"),map.get("ttlAmntForIncentEntryTbl"),map.get("invoiceDate"),map.get("receviedDate"),map.get("poNo"),map.get("vendorId"),creditNoteNumber,creditTotalAmount,indentEntryNo,site_id);
					response2 = "Success";
				} catch (Exception e) {
					response2 = "Failed";
					e.printStackTrace();
				}
				
			}else{}
			if(response2.equalsIgnoreCase("Success")){
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
				finalresponse = response1;
			}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				finalresponse = "Failed@@_";
			}
			//===============================
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			finalresponse = "Failed@@_";
			e.printStackTrace();
		}
		return finalresponse;
	
	}
	
		public int saveMarketingExpenditure(Map<String, String> map, HttpServletRequest request, HttpSession session) {
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			int value=1;
			String expenditureFor = request.getParameter("expenditureFor");
			String noOfRows=request.getParameter("multiplesitesrowcount");
			if(expenditureFor.equalsIgnoreCase("select") && noOfRows!=null || noOfRows.equals("")){
				return value;
			}
			MarketingDeptBean objMarketingExpenditure = new MarketingDeptBean();
			int expenditureId = objMarketingDepartmentIndentProcessDao.getExpenditureId();
			objMarketingExpenditure.setExpenditureId(expenditureId);
			objMarketingExpenditure.setInvoiceNumber(map.get("invoiceNumber"));
			objMarketingExpenditure.setInvoiceAmount(map.get("ttlAmntForIncentEntryTbl"));
			objMarketingExpenditure.setCreatedBy(userId);
			objMarketingExpenditure.setIndentEntryId(request.getAttribute("indentEntryNo")==null ? "" : request.getAttribute("indentEntryNo").toString());
			objMarketingExpenditure.setExpenditureType(expenditureFor);
			Date invoiceDate = DateUtil.convertToJavaDateFormat(map.get("invoiceDate"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(invoiceDate);
			objMarketingExpenditure.setExpenditureMonth((calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR));//11-2018
			objMarketingExpenditure.setInvoiceDate(map.get("invoiceDate"));
			objMarketingDepartmentIndentProcessDao.insertMarketExpenditure(objMarketingExpenditure);
			List<MarketingDeptBean> objMarketingExpenditureDtlsList = new ArrayList<MarketingDeptBean>();
			String childDataParam = "";
			String locationdataParam = "";
			String fromDateParam = "";
			String toDateParam = "";
			String siteQuantityParam = "";
			String splitAmountParam = "";
			String siteTotalAmountParam = "";
			String sitedataParam = "";
			String timeParam = "";
			
			//String noOfRows = null;
			String childData = null;
			String childProductId = null;
			String locationdata = null;
			String locationId = null;
			String fromDate = null;
			String toDate = null;
			String siteQuantity = null;
			String splitAmount = null;
			String siteTotalAmount = null;
			String sitedata = null;
			String siteId = null;
			String time = null;//"23:59:59";//time is manually given
			
			
			if(expenditureFor.equals("SiteWise")){
				//noOfRows = "1";
				childDataParam = "childdata";
				locationdataParam = "siteareaid";
				fromDateParam = "sitewisefromDate";
				toDateParam = "sitewisetoDate";
				siteQuantityParam = "sitequanty";
				splitAmountParam = "splitamount";
				siteTotalAmountParam = "totalamountsite";
				sitedataParam = "singleSiteWiseSite";
				timeParam = "SiteWiseTime";
				
			}
			else if(expenditureFor.equals("MultipleSite")){
				//noOfRows = request.getParameter("multiplesitesrowcount");
				childDataParam = "exapendChildproduct";
				locationdataParam = "multisiteareaid";
				fromDateParam = "multisitefromDate";
				toDateParam = "multisitetoDate";
				siteQuantityParam = "multisitequanty";
				splitAmountParam = "multisplitamount";
				siteTotalAmountParam = "totalamountmultsite";
				sitedataParam = "sitenameid";
				timeParam = "MultiSiteWiseTime";
			}
			else if(expenditureFor.equals("LocationWise")){
				//noOfRows = request.getParameter("multiplesitesrowcount");
				childDataParam = "LocationChildId";
				locationdataParam = "LocationLocationId";
				fromDateParam = "locwisefromDate";
				toDateParam = "locwisetoDate";
				siteQuantityParam = "locationquantity";
				splitAmountParam = "locationtotalAmount";
				siteTotalAmountParam = "locationsplitamount";
				sitedataParam = "locationSiteId";
				timeParam = "LocationTime";
			}
			else if(expenditureFor.equals("BrandingWise")){
				//noOfRows = request.getParameter("multiplesitesrowcount");
				childDataParam = "BrandingChildId";
				locationdataParam = "BrandinglocationId";
				fromDateParam = "brandwisefromDate";
				toDateParam = "brandwisetoDate";
				siteQuantityParam = "brandingquantity";
				splitAmountParam = "brandingtotalAmount";
				siteTotalAmountParam = "brandingsplitamount";
				sitedataParam = "BrandingSiteId";
				timeParam = "BrandingTime";
			}
			else{
				throw new RuntimeException("Expenditure For not matched for any given options..?!");
			}
			String[] rowIndexArray = noOfRows.split(",");
			for(String i : rowIndexArray){
				
				childData = request.getParameter(childDataParam+i);
				locationdata = request.getParameter(locationdataParam+i);
				fromDate = request.getParameter(fromDateParam+i);
				toDate = request.getParameter(toDateParam+i);
				siteQuantity = request.getParameter(siteQuantityParam+i);
				splitAmount = request.getParameter(splitAmountParam+i);
				siteTotalAmount = request.getParameter(siteTotalAmountParam+i);
				if(expenditureFor.equals("SiteWise")){
					sitedata = request.getParameter(sitedataParam);
				}else{
				sitedata = request.getParameter(sitedataParam+i);}
				if(sitedata.equals("MARKETING ")){sitedata="996$MARKETING ";}
				time = request.getParameter(timeParam+i);
				
				
				MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
				int expenditureDetailsId = objMarketingDepartmentIndentProcessDao.getExpenditureDetailsId();
				objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
				objMarketingExpenditureDtls.setExpenditureId(expenditureId);
				if(StringUtils.isNotBlank(childData)){
					childProductId = childData.split("\\$")[0];
				}
				if(StringUtils.isNotBlank(locationdata) && !locationdata.equals("$")){
					locationId = locationdata.split("\\$")[0];
				}
				if(StringUtils.isNotBlank(sitedata)){
					siteId = sitedata.split("\\$")[0];
				}
				if(StringUtils.isNotBlank(time) && !time.equals("-")){
				String AMorPM = time.split(" ")[1];
				String timeHoursAndMins = time.split(" ")[0];
				String timeHours = timeHoursAndMins.split(":")[0];
				String timeMins = timeHoursAndMins.split(":")[1];
				// here convert the user given time to check the 12 hours then add time set to 24 hours but it is not work in batch update query so set to 0:23:59:59
				if(timeHours.equals("12")&&AMorPM.equals("AM")){ 
					timeHours = "00";
				}
				if(AMorPM.equals("PM")){
					if(!timeHours.equals("12")){
						timeHours = String.valueOf(Integer.parseInt(timeHours)+12);
					}
				}
				if(timeHours.length()==1){
					timeHours = "0"+timeHours;
				}
				time = timeHours+":"+timeMins+":00"; // here we need to addhours and minutes along with 00 added to this 
				}
				objMarketingExpenditureDtls.setChild_ProductId(childProductId);
				objMarketingExpenditureDtls.setQuantity(siteQuantity);
				objMarketingExpenditureDtls.setPrice(splitAmount);
				objMarketingExpenditureDtls.setAmount(siteTotalAmount);
				objMarketingExpenditureDtls.setHoardingId(locationId);
				objMarketingExpenditureDtls.setFromDate(fromDate);
				objMarketingExpenditureDtls.setToDate(toDate);
				objMarketingExpenditureDtls.setSiteId(siteId);
				objMarketingExpenditureDtls.setTime(time);
				objMarketingExpenditureDtlsList.add(objMarketingExpenditureDtls);
			}
			objMarketingDepartmentIndentProcessDao.insertMarketExpenditureDtls(objMarketingExpenditureDtlsList); // add all xependiture data to the table
			
			return value;
		}

/*================================modify marketing po start=========================================*/
	
	public String modifyMarketingPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String site_id) {

		String pendingEmpId="";
		String response="";
		int result=0;
		String checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(temp_Po_Number);	
		String remarks=request.getParameter("Remarks_cancel")==null ? (request.getParameter("Remarks")==null? "": request.getParameter("Remarks"))
				: request.getParameter("Remarks_cancel");
		pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id);

		if((pendingEmpId!=null || pendingEmpId.equals("")) && (checkval.equals("A"))){

			result=objPurchaseDepartmentIndentProcessDao.updateEmpId(pendingEmpId,temp_Po_Number);
			objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(temp_Po_Number,"0",user_id, site_id, "CAN",remarks);
			//	int i=objPurchaseDepartmentIndentProcessDao.deleteTemppoTermsAdnConditions(temp_Po_Number);
			response="success";
			//	}
		}

		return response;
	}
	/*================================modify marketing po END=========================================*/
	
	/*===========================================add available area to marketing dept start=====================================================*/
	public   String addAvailableArea(HttpSession session,HttpServletRequest request){
		String response="";
		try{
		String strMonthToBeCreate = request.getParameter("month_year");
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
		String totalRows=request.getParameter("totalNoOfRows");
		String numOfTransRec[] = null;
		MarketingDeptBean objMarketingAreaDtls=null;
		
		if((totalRows != null) && (!totalRows.equals(""))) {
			numOfTransRec = totalRows.split(",");
		}
		
		if(totalRows!=null && !totalRows.equals("")){
		int records_Count =(numOfTransRec.length);
		for(int i=0;i<records_Count;i++){
			int num=Integer.parseInt(numOfTransRec[i]);
			objMarketingAreaDtls = new MarketingDeptBean();
			
			String site=request.getParameter("site"+num);
			String siteName[]=site.split("\\$");
			String siteId=siteName[0];
			String unitOfMeasurement=request.getParameter("unitOfMeasurement"+num).toUpperCase();
			double totalArea=Double.valueOf(request.getParameter("totalArea"+num)==null ? "0" : request.getParameter("totalArea"+num));
			double availableArea=Double.valueOf(request.getParameter("availableArea"+num)==null ? "0" : request.getParameter("availableArea"+num));
			String status=request.getParameter("status"+num)==null ? "" : request.getParameter("status"+num);
			/*objMarketingAreaDtls.setSiteId(siteId);
			objMarketingAreaDtls.setMeasurementName(unitOfMeasurement);
			objMarketingAreaDtls.setTotalArea(totalArea);objMarketingAreaDtls.setAvailableArea(availableArea);*/
			objMarketingDepartmentIndentProcessDao.insertNewAvailableArea(user_id,siteId,unitOfMeasurement,totalArea,availableArea,strMonthToBeCreate,status);
			response="success";
			
		}
		}
		
		}catch(Exception e){
			response="failed";
			e.printStackTrace();
		}
		return response;
	}
	/*===========================================add available area to marketing dept start=====================================================*/	

/*======================================================get invoceId and vendorId for  view expenditure strat================================================
*/
	public Map<Double,List<MarketingDeptBean>> getAllViewExpendituresWithVendorData(String invoiceId,String vendorId,String invoiceDate) {
		logger.info("***** The control is inside the getAllViewExpendituresWithVendorData in MarketingDepartmentServiceImpl ******");
		int serialNo = 1;
		double totalAmount = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-yyyy");
		List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
		Map<Double,List<MarketingDeptBean>> marketingPOProductDetailsMap = new HashMap();
		List<Map<String,Object>> expendituresList =null;
		expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpendituresWithVendorData(invoiceId,vendorId,invoiceDate);	
		
		for(Map<String,Object>map : expendituresList ) {
			MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setSerialno(serialNo);
			//marketingPOProductDetails.setExpendatureId(map.get("EXPENDATURE_ID").toString());
			marketingPOProductDetails.setChild_ProductName((String)map.get("CHILD_PRODUCT_NAME"));
			marketingPOProductDetails.setChild_ProductId((String)map.get("CHILD_PRODUCT_ID"));
			
			marketingPOProductDetails.setProductName((String)map.get("PRODUCT_NAME"));
			marketingPOProductDetails.setSub_ProductName((String)map.get("SUB_PRODUCT_NAME"));
			marketingPOProductDetails.setMeasurementName((String)map.get("MEASUR_MNT_NAME"));
			marketingPOProductDetails.setVendorName((String)map.get("VENDOR_NAME"));
			
			marketingPOProductDetails.setInvoiceDate(sdf1.format((Date)map.get("INVOICE_DATE")));
			
			logger.info("***** setInvoiceDate ***** "+sdf1.format((Date)map.get("INVOICE_DATE")));
			
			marketingPOProductDetails.setSiteId(map.get("SITE_ID").toString());
			marketingPOProductDetails.setSiteName((String)map.get("SITE_NAME"));
			marketingPOProductDetails.setLocation((String)map.get("LOCATION"));
			marketingPOProductDetails.setHoardingId((String)map.get("LOCATION_ID"));
			marketingPOProductDetails.setQuantity((String)map.get("QUANTITY"));
			
			if((map.get("FROM_DATE") != null ) && !map.get("FROM_DATE").equals("")){
			   marketingPOProductDetails.setFromDate(sdf.format(map.get("FROM_DATE")));
			}
			if((map.get("TO_DATE") != null ) && !map.get("TO_DATE").equals("")){
			      marketingPOProductDetails.setToDate(sdf.format(map.get("TO_DATE")));
			
			}
			if((map.get("TIME") != null ) && !map.get("TIME").equals("")){
				marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString() ));
			
			}
		    
		    //marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString().substring(1) ));
		   // marketingPOProductDetails.setTime(new SimpleDateFormat("hh:mm aa").format(((Date)map.get("TIME")).getTime()));  
		    double	amount = Double.valueOf((String)map.get("AMOUNT"));
		    totalAmount = totalAmount + amount;
			marketingPOProductDetails.setAmount((String)map.get("AMOUNT"));
			marketingPOProductDetailsList.add(marketingPOProductDetails);
			serialNo++;
			logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
			logger.info("**** The MarketingPOProductDetails object totalAmount is *****"+totalAmount);
		}
	logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
	 marketingPOProductDetailsMap.put(totalAmount,marketingPOProductDetailsList);
	 logger.info("**** The marketingPOProductDetailsMap object data is ******"+marketingPOProductDetailsMap);
		return marketingPOProductDetailsMap;
	}	
	
	/*============================================view expenditure for invoice number and vendor Id end=========================================*/

	/*==================================================cancel temp po page Start==================================================================*/
	public String cancelMarketingPoDetails(HttpSession session ,HttpServletRequest request) {

		String user_id ="";
		String ponumber = "";
		String response = "";
		//String quantity="";
		String site_id ="";
		String old_po_NumberStatus="";
		try {

			final int getLocalPort = request.getLocalPort();
			ponumber=request.getParameter("strPONumber");
			String checkStatus=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
			//intTotalNoOfRecords = Integer.parseInt(request.getParameter("totalNoOfRecords") == null ? "0" : request.getParameter("totalNoOfRecords").toString());
			String mailComments=request.getParameter("Remarks")==null ? "" : request.getParameter("Remarks");
			old_po_NumberStatus=objPurchaseDepartmentIndentProcessDao.activeOldPOTable(ponumber);
			if(checkStatus.equals("A") && !old_po_NumberStatus.equalsIgnoreCase("success")){
				user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
				objMarketingDepartmentIndentProcessDao.updateTablesOnTempPORejection(ponumber,"CANCEL");
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);
				
				List<String> listOfDetails=objMarketingDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Marketing Temp Po Has been Cancelled";
				sendTempMarketingPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Cancelled",getLocalPort);
	
				response = "Success";
			}else if(old_po_NumberStatus.equalsIgnoreCase("success")){response = "Success";// revised po was rejected so it will active
			user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
			String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);// to send the mail for revised po rejected time
			
			List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
			listOfDetails.add(String.valueOf(request.getLocalPort()));
			String subject="Your Temp Po Has been Cancel";
			sendTempMarketingPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Cancelled",getLocalPort);}
			
			else{
				
				response = "failure";
			}
		} catch (Exception e) {
			//System.out.println("temp po number "+ponumber);
			e.printStackTrace();
			response = "failure";
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
		return response;
	}

/*======================================================== cancel temp po page End===========================================================*/
	/*	*******************************************************************tempPo mail Common Method****************************************************************/	
	public void sendTempMarketingPoMailCommonData(String temp_Po_Number,String mailComments,List<String> listOfDetails,String subject,String type,int intPortNo){
		
		String strApproveName=listOfDetails.get(0);
		String approveToEmailAddress[]=null;
		
		List<String> toMailListArrayList = new ArrayList<String>();
		if(listOfDetails.get(1) != null &&! listOfDetails.get(1).equals("")){
			if(listOfDetails.get(0).contains(",")){
				for(int i=0;i<listOfDetails.get(1).split(",").length;i++){
					toMailListArrayList.add(listOfDetails.get(1).split(",")[i]);
				}
			}else{
				toMailListArrayList.add(listOfDetails.get(1));
			}
		}

		approveToEmailAddress = new String[toMailListArrayList.size()];
		toMailListArrayList.toArray(approveToEmailAddress);
		
		Object ApproveData[]={"",mailComments,listOfDetails.get(7),listOfDetails.get(3),"",listOfDetails.get(4),temp_Po_Number,listOfDetails.get(3),
				listOfDetails.get(6),listOfDetails.get(2),listOfDetails.get(5),listOfDetails.get(0),type,"",listOfDetails.get(8),"","",""};
		EmailFunction objEmailFunction = new EmailFunction();
		objEmailFunction.sendMailForTempMarketingPoReject(strApproveName,approveToEmailAddress,ApproveData,subject,intPortNo);
		
	}
/*	**********************************************this is for porevised or not start*******************************************************************/	

// this is the only expenditure
	public List<MarketingDeptBean> getAllViewExpendituresDates(String invoiceFromDate,String invoiceToDate,String siteIds,String invoiceDate,String vendorId){
		
		logger.info("***** The control is inside the getAllViewExpenditures in invoiceFromDate&invoiceToDate in MarketingDepartmentServiceImpl ******");
		int serialNo = 1;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		List<Map<String,Object>> expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpendituresDates((invoiceFromDate),(invoiceToDate),siteIds,invoiceDate,vendorId);
		for(Map<String,Object>map : expendituresList ) {
			MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
			marketingPOProductDetails.setSerialno(serialNo);
			marketingPOProductDetails.setInvoiceNumber((String)map.get("INVOICE_ID"));
			marketingPOProductDetails.setInvoiceAmount(format.format(Double.valueOf((String)map.get("INVOICE_AMOUNT"))));
		    marketingPOProductDetails.setInvoiceDate( sdf.format((Date)map.get("INVOICE_DATE")));
		    marketingPOProductDetails.setSiteName( (String)map.get("SITENAME"));
		    marketingPOProductDetails.setVendorName((String)map.get("VENDOR_NAME"));
		    marketingPOProductDetailsList.add(marketingPOProductDetails);
			serialNo++;
			logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
		}
		
		logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
		return marketingPOProductDetailsList;
		
		}

	
	/*======================================================get invoceId  for  view expenditure strat================================================
	*/
		public Map<Double,List<MarketingDeptBean>> getAllViewExpendituresWithInvoiceData(String invoiceId,String vendorId,String invoiceDate,String siteIds,
				String productId,String subProductId,String childProductId,String fromDate,String toDate) {
			logger.info("***** The control is inside the getAllViewExpendituresWithVendorData in MarketingDepartmentServiceImpl ******");
			int serialNo = 1;
			double totalAmount = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("MM-yyyy");
			List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
			Map<Double,List<MarketingDeptBean>> marketingPOProductDetailsMap = new HashMap();
			List<Map<String,Object>> expendituresList =null;
			double	amount =0.0;
			com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
			expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpendituresWithInvoiceData(invoiceId,vendorId,invoiceDate,siteIds,productId,subProductId,childProductId,fromDate,toDate);	
			if(expendituresList!=null && expendituresList.size()>0){
			for(Map<String,Object>map : expendituresList ) {
				MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
				marketingPOProductDetails.setSerialno(serialNo);
				//marketingPOProductDetails.setExpendatureId(map.get("EXPENDATURE_ID").toString());
				marketingPOProductDetails.setChild_ProductName((String)map.get("CHILD_PRODUCT_NAME"));
				marketingPOProductDetails.setChild_ProductId((String)map.get("CHILD_PRODUCT_ID"));
				
				marketingPOProductDetails.setProductName((String)map.get("PRODUCT_NAME"));
				marketingPOProductDetails.setSub_ProductName((String)map.get("SUB_PRODUCT_NAME"));
				marketingPOProductDetails.setMeasurementName((String)map.get("MEASUR_MNT_NAME"));
				marketingPOProductDetails.setVendorName((String)map.get("VENDOR_NAME"));
				
				marketingPOProductDetails.setInvoiceDate(sdf1.format((Date)map.get("INVOICE_DATE")));
				
				logger.info("***** setInvoiceDate ***** "+sdf1.format((Date)map.get("INVOICE_DATE")));
				
				marketingPOProductDetails.setSiteId(map.get("SITE_ID").toString());
				marketingPOProductDetails.setSiteName((String)map.get("SITE_NAME"));
				marketingPOProductDetails.setLocation((String)map.get("LOCATION"));
				marketingPOProductDetails.setHoardingId((String)map.get("LOCATION_ID"));
				marketingPOProductDetails.setQuantity((String)map.get("QUANTITY"));
				
				if((map.get("FROM_DATE") != null ) && !map.get("FROM_DATE").equals("")){ // in CreatePo time user given the location data in that from date not given then it will executed
				   marketingPOProductDetails.setFromDate(sdf.format(map.get("FROM_DATE")));
				}
				if((map.get("TO_DATE") != null ) && !map.get("TO_DATE").equals("")){ // some times to date not given then it will check the condition 
				      marketingPOProductDetails.setToDate(sdf.format(map.get("TO_DATE")));
				
				}
				if((map.get("TIME") != null ) && !map.get("TIME").equals("")){ // some times time not given so check here
					marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString() ));
				
				}
			    
			    //marketingPOProductDetails.setTime(DateUtil.Time((map.get("TIME")).toString().substring(1) ));
			   // marketingPOProductDetails.setTime(new SimpleDateFormat("hh:mm aa").format(((Date)map.get("TIME")).getTime()));  
				if((map.get("AMOUNT") != null ) && !map.get("AMOUNT").equals("")){
					amount = Double.valueOf((String)map.get("AMOUNT"));
			    totalAmount = totalAmount + amount;
				}
				marketingPOProductDetails.setAmount(format.format(amount)); 
				marketingPOProductDetailsList.add(marketingPOProductDetails); // bean values set to list
				serialNo++;
				logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
				logger.info("**** The MarketingPOProductDetails object totalAmount is *****"+totalAmount);
			}
			}
		logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
		 marketingPOProductDetailsMap.put(totalAmount,marketingPOProductDetailsList); //after adding list values it will added to map
			
		 logger.info("**** The marketingPOProductDetailsMap object data is ******"+marketingPOProductDetailsMap);
			return marketingPOProductDetailsMap;
		}	
		
		/*============================================view expenditure for invoice number and vendor Id end=========================================*/

		// this is the only update Expenditure
		public List<MarketingDeptBean> getAllViewExpendituresForVendorName(String invoiceId,String vendorId,String invoiceDate){
			
			logger.info("***** The control is inside the getAllViewExpenditures in invoiceFromDate&invoiceToDate in MarketingDepartmentServiceImpl ******");
			int serialNo = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			List<MarketingDeptBean> marketingPOProductDetailsList = new ArrayList();
			com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
			List<Map<String,Object>> expendituresList = objMarketingDepartmentIndentProcessDao.getAllViewExpendituresForVendorName(invoiceId,vendorId,invoiceDate);
			for(Map<String,Object>map : expendituresList ) {
				MarketingDeptBean marketingPOProductDetails = new MarketingDeptBean();
				marketingPOProductDetails.setSerialno(serialNo);
				marketingPOProductDetails.setInvoiceNumber((String)map.get("INVOICE_ID"));
				marketingPOProductDetails.setInvoiceAmount(format.format(Double.valueOf((String)map.get("INVOICE_AMOUNT"))));
			    marketingPOProductDetails.setInvoiceDate( sdf.format((Date)map.get("INVOICE_DATE")));
			    marketingPOProductDetails.setSiteName( (String)map.get("SITENAME"));
			    marketingPOProductDetailsList.add(marketingPOProductDetails);
				serialNo++;
				logger.info("**** The MarketingPOProductDetails object is *****"+marketingPOProductDetails);
			}
			
			logger.info("****** The size of the marketingPOProductDetailsList object size is ********"+marketingPOProductDetailsList.size());
			return marketingPOProductDetailsList;
			
			}

		/*****************************************************cancel Marketing Mail Temp Po*****************************************************************************/
		public String RejectMailTempPO(HttpSession session ,HttpServletRequest request) {

			String user_id ="";
			String ponumber =request.getParameter("strPONumber");
			String site_id =request.getParameter("siteId");
			String response = "";
			int result=0;
			//int intTotalNoOfRecords = 0;
			String old_po_NumberStatus="";
			final int getLocalPort = request.getLocalPort();
			String mailComments=request.getParameter("Remarks")==null ? " " : request.getParameter("Remarks");
			user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
			old_po_NumberStatus=objPurchaseDepartmentIndentProcessDao.activeOldPOTable(ponumber);
			try {
				if(!old_po_NumberStatus.equalsIgnoreCase("success")){ // here check the po is revised or not check i.e revised po time set old po number to new one  
					String checkStatus=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
					String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
					String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);
					user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
					if(passwdForMail.equals(dbPasswd) && checkStatus.equals("A")){ // here check the password for database and request object password and whether po is active or not check here
						result=objMarketingDepartmentIndentProcessDao.updateTablesOnTempPORejection(ponumber,"CANCEL");
						String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);

						List<String> listOfDetails=objMarketingDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
						listOfDetails.add(String.valueOf(getLocalPort));
						String subject="Your Temp Po Has been Cancelled";
						sendTempMarketingPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Cancelled",getLocalPort);

					}
				
			
				if(result > 0){
					
					response = "Success";
				}else{
					response = "failure";
				}
				}else if(old_po_NumberStatus.equalsIgnoreCase("success")){response = "Success";
				
				result=objMarketingDepartmentIndentProcessDao.updateTablesOnTempPORejection(ponumber,"CANCEL");
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);

				List<String> listOfDetails=objMarketingDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
				listOfDetails.add(String.valueOf(getLocalPort));
				String subject="Your Temp Po Has been Cancelled";
				sendTempMarketingPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Cancelled",getLocalPort);
				
				} //which is revised po then it will exceiute this method 
				
				
			} catch (Exception e) {
				//System.out.println("temp po number "+ponumber);
				e.printStackTrace();
				response = "failure";
			}	
			SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
			return response;
		}

 /*********************************************************************reject Temp Mail Po********************************************************************/
	
	/***************************************** this is for common methode for start update availble area****************************************************/	
		
	public int calculateUpdateExpdtlsForLocationAndBrandWise(int intExpendatureId,String strChildProductId,double doubleTotalAmount,double doubleAvailArea,double totalAvailArea,
			double doubleQty,String strSiteId,String locationType,String user_id){
		int result=0;
		logger.info("******************* calculate update expenditures start *****************************************");
		
		double updtedTotalAmountProductwise = doubleTotalAmount * (doubleAvailArea/totalAvailArea); // here calculate the expenditure details based on formula
		BigDecimal bigDecimaltemptotalAmount = new BigDecimal(updtedTotalAmountProductwise);
		updtedTotalAmountProductwise=Double.valueOf(String.valueOf(bigDecimaltemptotalAmount.setScale(2,RoundingMode.CEILING)));

		logger.info("******************* calculate update expenditures start *****************************************"+updtedTotalAmountProductwise);
		
		double doubleProdcutRate = updtedTotalAmountProductwise/doubleQty;
		BigDecimal bigDecimaltemp_doubleProdcutRate = new BigDecimal(doubleProdcutRate);
		doubleProdcutRate=Double.valueOf(String.valueOf(bigDecimaltemp_doubleProdcutRate.setScale(2,RoundingMode.CEILING)));

		logger.info("******************* update expenditure time single product rate *****************************************"+doubleProdcutRate);
		//update invoice
		if(locationType.equalsIgnoreCase("LocationWise")){
		result=objMarketingDepartmentIndentProcessDao.insertOrUpdateExpendaturedtlsTableLocation( intExpendatureId, strChildProductId, doubleQty, doubleProdcutRate, updtedTotalAmountProductwise, strSiteId,user_id);	
		}else{
		result=objMarketingDepartmentIndentProcessDao.insertOrUpdateExpendaturedtlsTable( intExpendatureId, strChildProductId, doubleQty, doubleProdcutRate, updtedTotalAmountProductwise, strSiteId,user_id);
		}
		//public int insertOrUpdateExpendaturedtlsTable( int intExpendatureId,String strChildProductId,double doubleQty,double doubleRate,double doubleAmount,String strSiteId)
		logger.info("******************* after insert the data into expenditure table result status*****************************************"+result);
		return result;
	}
	
	public List<ProductDetails> getLocationFieldData(String poNumber,boolean isModify)throws ParseException {
		return objMarketingDepartmentIndentProcessDao.getLocationFieldData(poNumber,isModify);
	}
	public   boolean checkAvailableAreaCreatedOrnot(String month){
		return objMarketingDepartmentIndentProcessDao.checkAvailableAreaCreatedOrnot(month);
	}
	/******************************************* modify temp po start*******************************************************************************/
	@Override
	public String modifyTempPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String site_id) {

		String pendingEmpId="";
		String response="";
		int result=0;
		String checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(temp_Po_Number);	// check whether po status is active or not
		String remarks=request.getParameter("Remarks_cancel")==null ? (request.getParameter("Remarks")==null? "": request.getParameter("Remarks"))
				: request.getParameter("Remarks_cancel");
		pendingEmpId=objMarketingDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id); // get previous emp id for mail purpose

		if((pendingEmpId!=null || pendingEmpId.equals("")) && (checkval.equals("A"))){

			//result=objPurchaseDepartmentIndentProcessDao.updateEmpId(pendingEmpId,temp_Po_Number);
			objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(temp_Po_Number,"0",user_id, site_id, "CAN",remarks); // save Po_crt table status
			//	int i=objPurchaseDepartmentIndentProcessDao.deleteTemppoTermsAdnConditions(temp_Po_Number);
			response="success";
			//	}
		}

		return response;
	}
	/*==================================================== this is for check the is without update or revised start=================================*/
	public boolean checkIsMarketingUpdateOrNot(HttpSession session,HttpServletRequest request,boolean isRevised,boolean isModifyPo) {
		boolean status=true;
		int noofRows = Integer.parseInt(request.getParameter("numberOfRows"));
		String recordcount = request.getParameter("numbeOfRowsToBeProcessed");//numbeOfRowsToBeProcessed
		String ccEmailId=request.getParameter("ccEmailId")==null ? request.getParameter("ccEmailId2")==null ? "" : request.getParameter("ccEmailId2") : request.getParameter("ccEmailId");
		String oldccmailId=request.getParameter("oldccEmailId")==null ? "" : request.getParameter("oldccEmailId");
		
		String deliveryDate=request.getParameter("deliveryDate")==null ? "" : request.getParameter("deliveryDate");
		String oldDeliveryDate=request.getParameter("olddeliveryDate")==null ? "" : request.getParameter("olddeliveryDate");
		String noOfDays=request.getParameter("days")==null ? "" : request.getParameter("days");
		String oldNoOfDays=request.getParameter("olddays")==null ? "" : request.getParameter("olddays");
		String subject=request.getParameter("subject")==null ? "" : request.getParameter("subject");
		String strSubject=request.getParameter("strSubject")==null ? "" : request.getParameter("strSubject");
		
		
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
		int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
		int imagesAlreadyexisted=Integer.parseInt(request.getParameter("imagesAlreadyexisted"));
		int pdfAlreadyExised=Integer.parseInt(request.getParameter("pdfAlreadyExised"));
		
		double poTotal=Double.valueOf(request.getParameter("ttlAmntForIncentEntry")==null ? "0" : request.getParameter("ttlAmntForIncentEntry"));
		double oldPOTotal=Double.valueOf(request.getParameter("POTotal")==null ? "0" : request.getParameter("POTotal"));
		
		double strTotal=Math.ceil(poTotal);
		
		String recordscount[]=recordcount.split("\\|");
		
		if((imagesAlreadyexisted != imagesAlreadyPresent || pdfAlreadyExised !=pdfAlreadyPresent)){
			return false;
		}
		int rows=0;
		String locationCount=request.getParameter("locationLength"); // total no of records taken here it contain comma then split it
		String oldnoofCount=request.getParameter("oldNoofRecords");
		
		String strrecordscount[] = locationCount.split(",");
		if(strrecordscount.length==1 && Integer.valueOf(oldnoofCount)==0){
			oldnoofCount="1";
		}
		if(Integer.valueOf(oldnoofCount)==strrecordscount.length){
		for (int i = 0; i < strrecordscount.length; i++) {
			int num = Integer.parseInt(strrecordscount[i]);
			
			String childProduct = request.getParameter("locationChildProduct"+num)==null ? "" : request.getParameter("locationChildProduct"+num);
			String oldChildProdId=request.getParameter("oldChildProduct"+num)==null ? "" : request.getParameter("oldChildProduct"+num);
			String editOrnot=request.getParameter("locationActionValue"+num)==null ? "" : request.getParameter("locationActionValue"+num);
			if(editOrnot.equals("E") || !oldChildProdId.equals(childProduct) || editOrnot.equals("R")){
				status=false;
				break;
			}
		}
		}else{
			return false;
		}
		/*if(!isRevised){
			for(int i=0;i<noofRows;i++){
				int	num=Integer.parseInt(recordscount[i]);
				double quantity =Double.valueOf(request.getParameter("quantity"+num)==null ? "0" :request.getParameter("quantity"+num));
				double editStrQuantity =Double.valueOf(request.getParameter("strQuantity"+num)==null ? "0" :request.getParameter("strQuantity"+num));// revised po time quantity changed so we take old Quantity
				if(quantity!=editStrQuantity || !ccEmailId.equalsIgnoreCase(oldccmailId)){
					status=false;
					break;
				}
			}
		}*/
		//else{
			
			if(!deliveryDate.equals(oldDeliveryDate) || !noOfDays.equals(oldNoOfDays) || (strTotal!=oldPOTotal) || !ccEmailId.equals(oldccmailId) || !subject.equals(strSubject)){
				return false;
			}
			else{
				String[] terms=request.getParameterValues("termsAndCond");
				String[] oldterms=request.getParameterValues("termsAndCondold");
				//String[] conclusionsArray=request.getParameterValues("conclusionDesc");
				//String[] conclusionsOldArray=request.getParameterValues("conclusionDescold");

				/*if((conclusionsArray[0]!=null && !conclusionsArray[0].equals("")) && !isModifyPo){
					return false;}
				// this is for modify temp po purposenwritten 
				if((conclusionsArray[0]!=null && !conclusionsArray[0].equals("")) || (conclusionsOldArray[0]!=null && !conclusionsOldArray[0].equals("")) && isModifyPo){
					int strlength=conclusionsArray.length;
					if(conclusionsArray.length!=conclusionsOldArray.length){
						return false;
					}else{
						for(int i=0;i<conclusionsArray.length;i++){
							
							if(conclusionsArray[i]!= null && !conclusionsArray[i].equals("")){
								
								if(!conclusionsArray[i].equalsIgnoreCase(conclusionsOldArray[i])){
									return false;
								}
								
							}
						}
							for(int i=0;i<conclusionsOldArray.length;i++){
							
							if(conclusionsOldArray[i]!= null && !conclusionsOldArray[i].equals("")){
								if(!conclusionsOldArray[i].equalsIgnoreCase(conclusionsArray[i])){
									return false;
								}
								
							}
						}
					}
					
					return false;
				}*/
				// end of modify po
				int length=0;//(terms.length);
				int j=0;
				for(int i=0;i<terms.length;i++){
					if(terms[i]!= null && !terms[i].equals("")){
						length=++j;
					}
				}
				int oldLength=1;
				if(length==0){
					length=1;
				}
				if(oldterms!=null && oldterms[0]!=null && !oldterms[0].equals("")){
				oldLength=oldterms.length;}
				// this is for revised po purpose written this one 
				if(length!=oldLength){
					return false;
				}else{
					for(int i=0;i<terms.length;i++){
						
						if(terms[i]!= null && !terms[i].equals("")){
							
							if(!terms[i].equalsIgnoreCase(oldterms[i])){
								return false;
							}
							
						}
					}
				}
				// this is for revised po purpose written end 

			}

		//}
		return status;
	}
	// send mail while rejecting normal po or Revised Po 
	public void sendMarketingTempPoMailCommonData(String temp_Po_Number,String mailComments,List<String> listOfDetails,String subject,String type,int intPortNo,String [] ccMails){
	
	String strApproveName=listOfDetails.get(0);
	String approveToEmailAddress[]=null;
	
	List<String> toMailListArrayList = new ArrayList<String>();
	if(listOfDetails.get(1) != null &&! listOfDetails.get(1).equals("")){
		if(listOfDetails.get(0).contains(",")){
			for(int i=0;i<listOfDetails.get(1).split(",").length;i++){
				toMailListArrayList.add(listOfDetails.get(1).split(",")[i]);
			}
		}else{
			toMailListArrayList.add(listOfDetails.get(1));
		}
	}

	approveToEmailAddress = new String[toMailListArrayList.size()];
	toMailListArrayList.toArray(approveToEmailAddress);
	
	Object ApproveData[]={"",mailComments,listOfDetails.get(7),listOfDetails.get(3),"",listOfDetails.get(4),temp_Po_Number,listOfDetails.get(3),
			listOfDetails.get(6),listOfDetails.get(2),listOfDetails.get(5),listOfDetails.get(0),type,"",listOfDetails.get(8),"","",""};
	EmailFunction objEmailFunction = new EmailFunction();
	objEmailFunction.sendMailForMarketingTempPoReject(strApproveName,approveToEmailAddress,ApproveData,subject,intPortNo,ccMails);
	
}
	// thi is for getting the result ogf the modify temp po data
	@Override
	public List<ProductDetails> getModifyMarketingTempPODetails(String poNumber, String reqSiteId) {
		return objMarketingDepartmentIndentProcessDao.getModifyMarketingTempPODetails(poNumber, reqSiteId);
	}
	// getting the data when user click side module
	public String gettingProductData(String fromDate,String toDate) {
		return objMarketingDepartmentIndentProcessDao.gettingProductData(fromDate,toDate);
	}
	// getting the data user gave the input start
	public String selectedProductDetailsForGraph(String fromDate,String toDate,String SiteData) {
		return objMarketingDepartmentIndentProcessDao.selectedProductDetailsForGraph(fromDate,toDate,SiteData);
	}
}
