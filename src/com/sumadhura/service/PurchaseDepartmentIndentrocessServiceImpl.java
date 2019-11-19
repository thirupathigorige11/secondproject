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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.write.DateTime;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import sun.misc.BASE64Encoder;

import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.PriceMasterDTO;
import com.sumadhura.dto.TransportChargesDto;
import com.sumadhura.transdao.CentralSiteIndentProcessDao;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.IndentIssueDao;
import com.sumadhura.transdao.IssueToOtherSiteDao;
import com.sumadhura.transdao.MarketingDepartmentDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import java.lang.reflect.Method;



@Service("purchaseDeptIndentrocess")
public class PurchaseDepartmentIndentrocessServiceImpl  extends UIProperties implements PurchaseDepartmentIndentrocessService  {
	@Autowired
	private UtilDao utilDao;

	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;

	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	@Qualifier("guiReportService")
	ReportsService reportsService;

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;

	@Autowired
	private IndentCreationDao icd;

	@Autowired
	@Qualifier("MarketingDepartmentDao")
	MarketingDepartmentDao objMarketingDepartmentIndentProcessDao;

	@Autowired
	private IndentIssueDao iid;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	@Autowired
	CommonUtilities objCommonUtilities;

	@Override
	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = objPurchaseDepartmentIndentProcessDao.getIndentCreationDetailsLists(indentNumber);
		return list;
	}


	@Override
	public List<ProductDetails> createPO(Model model, HttpServletRequest request,HttpSession session)
	{
		//int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		//System.out.println(indentNumber);
		//	List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		//String strResponse = "failed";
		String vendorNumber=request.getParameter("vendorNumber")==null ? "" : request.getParameter("vendorNumber");
		String vendorId = request.getParameter("vendorId"+vendorNumber);
		String vendorName = request.getParameter("vendorName"+vendorNumber);
		String strGSTINNumber = request.getParameter("strGSTINNumber"+vendorNumber);
		String vendorAddress = request.getParameter("vendorAddress"+vendorNumber);
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("vendorName", vendorName);
		model.addAttribute("strGSTINNumber", strGSTINNumber);
		model.addAttribute("vendorAddress", vendorAddress);


		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;

		/*	if(noofrows > 0){

				String strPONumber = request.getParameter("poNumber");
				String strVendorId = request.getParameter("vendorId");
				String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

				ProductDetails productDetails = new ProductDetails();


				int intPoEntrySeqNo = icd.getPoEnterSeqNo();

				productDetails.setUserId(userId);
				productDetails.setVendorId(strVendorId);
				productDetails.setSite_Id(siteId);
				productDetails.setPoNumber(strPONumber);
				productDetails.setPoEntrySeqNumber(intPoEntrySeqNo);


				icd.insertPOEntry(productDetails);*/


		//	productDetails = null; 

		List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();
		for(int num=1;num<=noofrows;num++){
			if(request.getParameter("checkboxname"+num)!=null)
			{

				String productId = request.getParameter("productId"+num);
				String subProductId = request.getParameter("subProductId"+num);
				String childProductId = request.getParameter("childProductId"+num);
				String productName = request.getParameter("product"+num);
				String subProductName = request.getParameter("subProduct"+num);
				String childProductName = request.getParameter("childProductPuchaseDeptDisc"+num);
				String setMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
				String setMeasurementName = request.getParameter("unitsOfMeasurement"+num);
				String strPending = request.getParameter("pendingQuantity"+num);
				//String strPoIntiatedQuantity = request.getParameter("poIntiatedQuantity"+num);
				String strPoIntiatedQuantity = request.getParameter("POPendingQuantity"+num);
				String purchaseDepartmentIndentProcessSeqId = request.getParameter("purchaseDepartmentIndentProcessSeqId"+num);
				String indentNo = request.getParameter("indentNumber");
				String requestQuantity = request.getParameter("strRequestQuantity"+num);
				String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);

				ProductDetails productDetails = new ProductDetails();
				productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setProductId(productId);
				productDetails.setProductName(productName);
				productDetails.setSub_ProductId(subProductId);
				productDetails.setSub_ProductName(subProductName);
				productDetails.setChild_ProductId(childProductId);
				productDetails.setChild_ProductName(childProductName);
				productDetails.setMeasurementId(setMeasurementId);
				productDetails.setMeasurementName(setMeasurementName);
				//	productDetails.setQuantity(strPending);
				productDetails.setStrIndentId(indentNo);
				productDetails.setPurchaseDeptIndentProcessSeqId(purchaseDepartmentIndentProcessSeqId);
				productDetails.setQuantity(strPoIntiatedQuantity);
				productDetails.setRequestQantity(requestQuantity);

				productDetails.setPendingQuantity(strPending);
				productDetails.setIndentCreationDetailsId(indentCreationDetailsId);

				sno++;
				productDetails.setStrSerialNumber(String.valueOf(sno));
				listProductDetails.add(productDetails);

				/*boolean isAlreadyThere = false;
				for(int i=0;i<listProductDetails.size();i++)
				{
					if(listProductDetails.get(i).getChild_ProductName().equals(childProductName))
					{
						isAlreadyThere = true;
						int temp = Integer.parseInt(listProductDetails.get(i).getQuantity());
						temp+=Integer.parseInt(strPending);
						listProductDetails.get(i).setQuantity(String.valueOf(temp));
						//	listProductDetails.add(productDetails);
						break;
					}
				}
				if(!isAlreadyThere)
				{
					sno++;
					System.out.println("the java file serila no "+sno);
					productDetails.setStrSerialNumber(String.valueOf(sno));
					listProductDetails.add(productDetails);
				}*/




			}
		}

		return listProductDetails;	
	}

	// this method calling from different controllers like sitelevel po,revised po,normal po call this method
	@Override
	public  synchronized String SavePoDetails(Model model, HttpServletRequest request,HttpSession session,String strFinacialYear)
	{
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_savePO, ");
		ProductDetails productDetails =null;
		List<String> allEmployeeEmailsOfAcc = new ArrayList<String>();
		String strResponse = "";
		String strPONumber = "";
		String strVendorId =""; 
		String strDeliveryDate = "";
		String strVendorAddress =""; 
		String strVendorName =""; 
		String vendorState =""; 
		String strVendorGSTIN =""; 
		String receiverState =""; 
		String strReceiverAddress =""; 
		String strReceiverName =""; 
		String strReceiverGSTIN =""; 
		String strReceiverMobileNo = "";
		String strTableTwoDate = "";
		String strTableThreeData = "";
		String strIndentNo = "";
		String siteWiseIndentNo = "";
		String strUserId = "";
		String strSiteId = "";
		String strToSite;
		String finalamount="";
		String approvalEmpId="";
		int noofrows=0;
		String subject="";
		String ccEmailId="";
		String contactPersonName="";
		String billingAddress="";
		String strIndentCreationDate="";
		String strVendorMobilNo = "";
		String strLandLineNo = "";
		int poEntryId=0;
		String strEmail = "";
		String poState="";
		String strPoDate="";
		String strBillingAddressGSTIN = "";
		String strBillingCompanyName="";
		String editPonumber=""; 
		String serviceState=""; 
		String strReceiveSideContactPerson = "";
		String strReceiverLandLine = "";
		String siteName="";
		String sessionSiteId="";
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String version_no="";
		String refferenceNo="";
		String strPoPrintRefdate="";
		String recordcount = request.getParameter("numbeOfRowsToBeProcessed");
		String recordscount[]=recordcount.split("\\|");
		int temprevision_no=0;
		String isSiteLevelPo="";
		String preparedBy="";
		double grandtotal=0.0;
		String passwdForMail="";
		String strGrandTotalVal="";
		String revisedOrNot="";
		String strPODate="";
		String oldPOEntryId="";// when the user receive from inwards from po rceiced quantity taking into revised purpose
		String revisedPoForTemp="";// this is for temp revised po purpose
		int portNumber=request.getLocalPort();
		String acc_Note_Email="";
		String termsAndCondition = "";
		// this is check condition for 1 lakh amount 
		String tempApprovalEmpId=validateParams.getProperty("ACROPOLIS_PENDING_EMP_ID") == null ? "" : validateParams.getProperty("ACROPOLIS_PENDING_EMP_ID").toString();
		String tempApprovalEmpIdtoDevelopment=validateParams.getProperty("DEVELOPMENT_PENDING_EMP_ID") == null ? "" : validateParams.getProperty("DEVELOPMENT_PENDING_EMP_ID").toString();
		String strfinalAmount="";
		String remarks=""; // this is for employee given remarks at the time of po creation 
		String payment_Req_Days=""; // this is used in the payment level like no of days payment done before that
		String operation_Type="";
		boolean paymentStatus=false;
		String result="";
		double paidAmount=0.0;
		String changeDate=""; // this is used in the update or revised po time old po date save and normal po system date save
		String cotactPersonTwo="";
		String mobile_Number_Two="";
		String receiverContactPersonTwo="";
		String receiverMobileNumberTwo="";
		List<String> state=new ArrayList<String>();
		try{
			for(int i=1;i<=recordscount.length;i++){

				noofrows=i;
			}
			version_no=request.getAttribute("versionNo").toString();
			refferenceNo=request.getAttribute("refferenceNo").toString();
			strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
			remarks=request.getParameter("note");
			strVendorId = request.getParameter("vendorId");
			strDeliveryDate = request.getParameter("deliveryDate")==null ? "-" : request.getParameter("deliveryDate");
			siteName=request.getParameter("SiteName");
			strfinalAmount=request.getParameter("ttlAmntForIncentEntry");
			int sno = 0;
			List<String> listOfTermsAndConditions = new ArrayList<String>();
			if(noofrows > 0){
				session = request.getSession(true);
				strSiteId = request.getParameter("toSiteId") == null ? "" : request.getParameter("toSiteId").toString();
				strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();	
				sessionSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

				strIndentNo = request.getParameter("indentNumber");
				siteWiseIndentNo = request.getParameter("siteWiseIndentNo");				
				subject = request.getParameter("subject");
				strToSite = request.getParameter("toSiteId") == null ? "" : request.getParameter("toSiteId").toString();
				strIndentCreationDate = request.getParameter("strCreationDate")== null ? "" : request.getParameter("strCreationDate").toString();
				payment_Req_Days=request.getParameter("days")== null ? "" : request.getParameter("days").toString();
				
				/********************************************siteLevelPo start********************************************/
				//site level PO not having indentNumber time so it will written
				if((strIndentCreationDate==null || strIndentCreationDate.equals("")) && (strSiteId==null || strSiteId.equals(""))){
					Date date = new Date();
					 strIndentCreationDate= new SimpleDateFormat("dd-MM-yyyy").format(date);
					 strSiteId=sessionSiteId; // sie level po session id assign to site id indent internally created 
					 isSiteLevelPo="true";
					 preparedBy=siteName;
					 siteWiseIndentNo=request.getAttribute("siteWiseIndentNumber").toString();
					}
				
				/****************************************************siteLevelPo end*********************************************/
				//	request.getParameter("vendorId");

				productDetails = new ProductDetails();


				List<Map<String, Object>> listVendorDtls =  objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(strVendorId);
				for(Map<String, Object> prods : listVendorDtls) {
					strVendorName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
					strVendorAddress = prods.get("ADDRESS")==null ? "" :   prods.get("ADDRESS").toString();
					vendorState = prods.get("STATE")==null ? "" :   prods.get("STATE").toString();
					strVendorGSTIN = prods.get("GSIN_NUMBER")==null ? "" :   prods.get("GSIN_NUMBER").toString();
					contactPersonName = prods.get("VENDOR_CON_PER_NAME")==null ? "" :   prods.get("VENDOR_CON_PER_NAME").toString();
					strLandLineNo = prods.get("LANDLINE_NO")==null ? "" :   prods.get("LANDLINE_NO").toString();
					strVendorMobilNo = prods.get("MOBILE_NUMBER")==null ? "" :   prods.get("MOBILE_NUMBER").toString();
					strEmail = prods.get("EMP_EMAIL")==null ? " " :   prods.get("EMP_EMAIL").toString();
					cotactPersonTwo=prods.get("VENDOR_CON_PER_NAME_TWO")==null ? "" :   prods.get("VENDOR_CON_PER_NAME_TWO").toString();
					mobile_Number_Two=prods.get("MOBILE_NUMBER_TWO")==null ? "" :   prods.get("MOBILE_NUMBER_TWO").toString();
				}
			if(!cotactPersonTwo.equals("") && !contactPersonName.equals("")){
				contactPersonName = contactPersonName+","+cotactPersonTwo;
			}if(!cotactPersonTwo.equals("") && contactPersonName.equals("")){
				contactPersonName=cotactPersonTwo;
			}
			if(!strVendorMobilNo.equals("")){
					contactPersonName = contactPersonName+" ( "+strVendorMobilNo;
				}
			vendorState=vendorState+". Email Id : "+strEmail;
			if(!mobile_Number_Two.equals("")){
					contactPersonName = contactPersonName+","+mobile_Number_Two;
				}
				contactPersonName = contactPersonName +" )";
				// receiver address getting in this
				List<Map<String, Object>> listReceiverDtls =  objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(strSiteId);//strToSite
				for(Map<String, Object> prods : listReceiverDtls) {
					strReceiverName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
					strReceiverAddress = prods.get("ADDRESS")==null ? "" :   prods.get("ADDRESS").toString();
					strReceiverMobileNo = prods.get("MOBILE_NUMBER")==null ? "" :   prods.get("MOBILE_NUMBER").toString();
					strReceiverGSTIN = prods.get("GSIN_NUMBER")==null ? "" :   prods.get("GSIN_NUMBER").toString();
					receiverState = prods.get("STATE")==null ? "" :   prods.get("STATE").toString();
					strReceiveSideContactPerson = prods.get("VENDOR_CON_PER_NAME")==null ? "" :   prods.get("VENDOR_CON_PER_NAME").toString();
					strReceiverLandLine = prods.get("MOBILE_NUMBER_TWO")==null ? "" :   prods.get("MOBILE_NUMBER_TWO").toString();
					receiverContactPersonTwo=prods.get("VENDOR_CON_PER_NAME_TWO")==null ? "" :   prods.get("VENDOR_CON_PER_NAME_TWO").toString();
					receiverMobileNumberTwo=prods.get("MOBILE_NUMBER_TWO")==null ? "" :   prods.get("MOBILE_NUMBER_TWO").toString();
					
				}
				if(!receiverContactPersonTwo.equals("")){
					strReceiveSideContactPerson=strReceiveSideContactPerson+","+receiverContactPersonTwo;
				}
				// po date using in mail time show the date
				Date date =new java.sql.Date(System.currentTimeMillis());
				SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yy");
				strPoDate=dt1.format(date);
				changeDate=dt2.format(date);
				approvalEmpId=objPurchaseDepartmentIndentProcessDao.getTemproryuser(strUserId);
				// as per acropolis requirement written this one approval employee Id middle person create po then taken from properties file
				if(tempApprovalEmpId.equals(approvalEmpId) && Double.parseDouble(strfinalAmount)<=100000 &&  strUserId.equals("5033")){
					approvalEmpId=validateParams.getProperty("ACROPOLIS_EMP_ID") == null ? "" : validateParams.getProperty("ACROPOLIS_EMP_ID").toString();
				}if(tempApprovalEmpIdtoDevelopment.equals(approvalEmpId) && Double.parseDouble(strfinalAmount)<=100000 && strUserId.equals("BPUR2.1")){
					approvalEmpId=validateParams.getProperty("DEVELOPMENT_EMP_ID") == null ? "" : validateParams.getProperty("DEVELOPMENT_EMP_ID").toString();
				}
				// check whether po is Revised then take old Po Number
				editPonumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString(); 
				
				// check condition of next approval
				if(approvalEmpId!=null && approvalEmpId.equals("VND")){
					
					if(isSiteLevelPo.equalsIgnoreCase("true")){ //for site Level po execute this
						revisedOrNot="false";
						strPONumber=objPurchaseDepartmentIndentProcessDao.getPermenentPoNumber("PO/SIPL/",siteName,sessionSiteId,strFinacialYear);
						preparedBy=siteName;operation_Type="CREATION";
						
					}else{ // this is for normal po and revised po
				
						List<String> poNumberList=getPoNumberForRevisedAndNormalPo(session,request,editPonumber,receiverState,strSiteId,strFinacialYear);
						strPONumber=poNumberList.get(0);serviceState=poNumberList.get(4);
						preparedBy=poNumberList.get(1);strPODate=poNumberList.get(5);
						revisedOrNot=poNumberList.get(2);oldPOEntryId=poNumberList.get(6);
						poState=poNumberList.get(3);temprevision_no=Integer.parseInt(poNumberList.get(7));
						operation_Type=poNumberList.get(8);
					}
					poEntryId = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNo();
				}
				else{
					// to save temp po details in temp tables
					List<String> tempPoDetails=getAndSaveTempPoDetails(isSiteLevelPo,siteName,editPonumber);
					preparedBy=tempPoDetails.get(0);strPODate=tempPoDetails.get(2);
					strPONumber=tempPoDetails.get(1);revisedPoForTemp=tempPoDetails.get(3);
					operation_Type=tempPoDetails.get(4);
				}
				/*****************************************revised purpose it is used start*******************************************************/
				if(editPonumber!=null && !editPonumber.equals("")){
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
				//to get billingaddress and gstin and company name
				List<String> billAddress=objPurchaseDepartmentIndentProcessDao.getBillingAddGstin(receiverState);
				billingAddress=	billAddress.get(0);
				strBillingAddressGSTIN =billAddress.get(1);
				strBillingCompanyName=billAddress.get(2);
				
				
				request.setAttribute("strPONumber",strPONumber);// this is for download pdf file
				// to generate the password for mail purpose
				Random rand = new Random();
				int rand_Number = rand.nextInt(1000000);
				passwdForMail=String.valueOf(rand_Number);

				productDetails.setUserId(strUserId);
				productDetails.setVendorId(strVendorId);
				productDetails.setStrDeliveryDate(strDeliveryDate);
				productDetails.setSite_Id(strSiteId);
				productDetails.setIndentNo(strIndentNo);
				productDetails.setPoNumber(strPONumber);
				productDetails.setSubject(subject);
				productDetails.setBillingAddress(billingAddress);
				productDetails.setPoEntryId(poEntryId);
				productDetails.setVersionNo(version_no);
				productDetails.setRefferenceNo(refferenceNo);
				productDetails.setStrPoPrintRefdate(strPoPrintRefdate);
				productDetails.setEdit_Po_Number(editPonumber);
				productDetails.setRevision_Number(temprevision_no);
				productDetails.setPreparedBy(preparedBy);
				productDetails.setPasswdForMail(passwdForMail);
				productDetails.setPayment_Req_days(payment_Req_Days);
				productDetails.setOperation_Type(operation_Type);
				productDetails.setPoDate(changeDate);
				
				WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+strPONumber);

				ccEmailId=request.getParameter("ccEmailId")==null ? request.getParameter("ccEmailId2")==null ? "" :request.getParameter("ccEmailId2") :request.getParameter("ccEmailId") ; // from properties file get these mails and save in temp poentry or poentry table
				
				if(approvalEmpId!=null && approvalEmpId.equals("VND")){
					objPurchaseDepartmentIndentProcessDao.insertPOEntry(productDetails);
				}
				else{
					objPurchaseDepartmentIndentProcessDao.insertTempPOEntry(productDetails,approvalEmpId,ccEmailId,subject);
				}
				String[] terms=request.getParameterValues("termsAndCond");
				int count=0;
				int length=(terms.length);
				length=length-1;
				for(int i=0;i<terms.length;i++){
					//int val=0;
					if(terms[i]!= null && !terms[i].equals("")){
						if(approvalEmpId!=null && approvalEmpId.equals("VND")){
							
							/*if(terms[i].contains("days after date of delivery.")){
								terms[i]=terms[i].replace(terms[i],"Payment:"+payment_Req_Days+" days after date of delivery.");
								count++;
							}
							if(!payment_Req_Days.equalsIgnoreCase("0") && i==2){ // this is because of requirement terms and conditions added in 3 rd point 
								objPurchaseDepartmentIndentProcessDao.saveTermsconditions("Payment:"+payment_Req_Days+"days after date of delivery.",poEntryId,strVendorId,strIndentNo);
								listOfTermsAndConditions.add("Payment:"+payment_Req_Days+"days after date of delivery.");
							}*/
							objPurchaseDepartmentIndentProcessDao.saveTermsconditions(terms[i],poEntryId,strVendorId,strIndentNo);
							}else{
								/*	if(terms[i].contains("days after date of delivery.") && !payment_Req_Days.equals("")  && !payment_Req_Days.equals("-")){
									terms[i]=terms[i].replace(terms[i],"Payment:"+payment_Req_Days+" days after date of delivery.");
									count++;
								}
							if(!payment_Req_Days.equalsIgnoreCase("0") && i==2){ // this is because of requirement terms and conditions added in 3 rd point 
									objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions("Payment:"+payment_Req_Days+"days after date of delivery.",strPONumber,strVendorId,strIndentNo);
									listOfTermsAndConditions.add("Payment:"+payment_Req_Days+"days after date of delivery.");
								}*/
							objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions(terms[i],strPONumber,strVendorId,strIndentNo);
							}
							termsAndCondition=terms[i];
							listOfTermsAndConditions.add(String.valueOf(termsAndCondition));
					}

				}
				// this is for add terms and contion for the payment request date given that one
			/*	if(!payment_Req_Days.equals("") && count==0 && !payment_Req_Days.equals("-")){
					if(approvalEmpId!=null && approvalEmpId.equals("VND")){
						objPurchaseDepartmentIndentProcessDao.saveTermsconditions("Payment:"+payment_Req_Days+" days after date of delivery.",poEntryId,strVendorId,strIndentNo);
						
					}else{
						objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions("Payment:"+payment_Req_Days+" days after date of delivery.",strPONumber,strVendorId,strIndentNo);
					}
					listOfTermsAndConditions.add("Payment:"+payment_Req_Days+"days after date of delivery.");
				}*/
				//Conclusions author - Rafi
				String[] conclusionsArray=request.getParameterValues("conclusionDesc");
				if(conclusionsArray!=null && !conclusionsArray.equals("")){
				String[] clean_conclusions = objCommonUtilities.cleanArray(conclusionsArray);
				List<String> conclusions = Arrays.asList(clean_conclusions);
				if(approvalEmpId!=null && approvalEmpId.equals("VND")){
					objPurchaseDepartmentIndentProcessDao.insertPoConclusions(conclusions,String.valueOf(poEntryId),"0",strIndentNo);
				}
				else{
					objPurchaseDepartmentIndentProcessDao.insertTempPoConclusions(conclusions,strPONumber,strIndentNo);
				}
				}
				//Conclusions - End
				// to save Transportaion details and assign to table three 
				strTableThreeData=saveTransportDetailsForPo(request,productDetails,approvalEmpId,strVendorGSTIN,isSiteLevelPo,receiverState);
			}
			productDetails = null;
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){ // delivery date given then it will convert into date format
				Date deliveryDate = DateUtil.convertToJavaDateFormat(strDeliveryDate);
				strDeliveryDate = new SimpleDateFormat("dd-MM-yyyy").format(deliveryDate);
			}else if(strDeliveryDate.equals("")){
				strDeliveryDate="-";
			}
			String strTableOneData = strPONumber+"@@"+strPoDate+"@@"+strIndentNo+"@@"+strVendorName+"@@"+strVendorAddress+"@@"+vendorState+"@@"+strVendorGSTIN+
			"@@"+strReceiverName+"@@"+strReceiverAddress+"@@"+strReceiverMobileNo+"@@"+strReceiverGSTIN+"@@"+finalamount+"@@"+subject+"@@"+contactPersonName+"@@"+ccEmailId+"@@"+billingAddress+"@@"+" "+"@@"+" "+"@@"+strUserName+"@@"+new java.sql.Date(System.currentTimeMillis())+"@@"+" "+"@@"+" "+"@@"+strIndentCreationDate+"@@"+siteWiseIndentNo+"@@"+
			strBillingAddressGSTIN+"@@"+strReceiveSideContactPerson+"@@"+receiverMobileNumberTwo+"@@"+strEmail+"@@"+strBillingCompanyName+"@@"+strSiteId+"@@"+siteName+"@@"+strDeliveryDate+"@@"+strVendorId;

			strTableTwoDate=saveProductDetailsForPo(session,request,strVendorGSTIN,noofrows,approvalEmpId,recordscount,receiverState,editPonumber,strPONumber,oldPOEntryId,poEntryId,isSiteLevelPo,revisedPoForTemp);
			strGrandTotalVal=request.getAttribute("pototal").toString();// for payment and poentry to update total amount mail purpose written it
			int intstrIndentNo = Integer.parseInt(strIndentNo);
			if(!isSiteLevelPo.equalsIgnoreCase("true")){ // check the status of products all initiated then it inactive it
				objPurchaseDepartmentIndentProcessDao.getupdatePurchaseDeptIndentProcess(intstrIndentNo,strUserId,strSiteId,approvalEmpId,sessionSiteId) ;
				}
			//objPurchaseDepartmentIndentProcessDao.getupdatePurchaseDeptIndentProcess(intstrIndentNo, strUserId,strSiteId,approvalEmpId,sessionSiteId) ;
			objPurchaseDepartmentIndentProcessDao.updateTotalAmt(String.valueOf(strGrandTotalVal),approvalEmpId,strPONumber);
			if(approvalEmpId!=null && approvalEmpId.equals("VND")  ){
				if(editPonumber!=null && !editPonumber.equals("")){ // created Po is Revisedpo then payment tables updated i.e amount is decreased
					paidAmount =objPurchaseDepartmentIndentProcessDao.updateAdvancePaidAmount(request,editPonumber,strSiteId,strVendorId,Double.valueOf(strGrandTotalVal));
					objPurchaseDepartmentIndentProcessDao.updateAccPayment(editPonumber,strPONumber,String.valueOf(strGrandTotalVal),false);
					if(paidAmount>Double.valueOf(strGrandTotalVal)){ // paid amount is more than the total amount then check condition and  trigger the mail
						 allEmployeeEmailsOfAcc=(List<String>) request.getAttribute("emailList");
						 acc_Note_Email=" For the difference in amount Paid and amount in Revised PO, Our Purchase Team will contact you.";
					
					}
					//String resp = inactivePendingApprovalsInAccDeptIfPoAmountDecreased(editPonumber,strSiteId,strVendorId,Double.valueOf(strGrandTotalVal),allEmployeeEmailsOfAcc,receiverState);
					/*if(!resp.equals("NoData")){ // update payment tables while revising po time along with comment it is send to vendor any change in payment tables
						//objPurchaseDepartmentIndentProcessDao.updateAccPayment(editPonumber,strPONumber,String.valueOf(strGrandTotalVal),false);
						if(resp.equals("Success")){ 
						
					}
					}*/
					// po total amount updated in dc,indent entry tables
					objPurchaseDepartmentIndentProcessDao.updateIndentAndDcPONumber(editPonumber,strPONumber,String.valueOf(strGrandTotalVal));
				}
				strResponse="POPrintPage";
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls("0",String.valueOf(poEntryId),strUserId, strSiteId, "C",remarks);
			} // save approvals details in pocrtApproval details
			else{
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(String.valueOf(strPONumber),"0",strUserId, strSiteId, "C",remarks);
				strResponse="CreatePOFinalPage";
			}
			final VendorDetails vd = new VendorDetails();
			
			String nameEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(strUserId);
			vd.setVendor_Id(strVendorId);
			final String poNumber =strPONumber;
			final String ccEmailTo = ccEmailId;
			final String subject1 = subject;
			final String finalSiteId = strSiteId;
			final String poCreatedEmpName=nameEmail;
			final int getLocalPort = portNumber;
			final String strRevisedOrNot=revisedOrNot;
			final String old_Po_Number=editPonumber;//this is for revised purpose to show old number in mail
			final String emailPODate=strPODate;
			final List<String> allEmployeeEmailsOfAcc1=allEmployeeEmailsOfAcc;
			final String RevsiedpoNote=acc_Note_Email;
			
			// mail send fast than compare to normal flow
			if(approvalEmpId!=null && approvalEmpId.equals("VND")){
				ExecutorService executorService = Executors.newFixedThreadPool(10);
				try{
					executorService.execute(new Runnable() {
						public void run() {
							sendEmailForPo( "", 0,finalSiteId, "siteName",vd,poNumber,ccEmailTo,subject1,poCreatedEmpName,getLocalPort,strRevisedOrNot,old_Po_Number,emailPODate,allEmployeeEmailsOfAcc1,RevsiedpoNote);
						}
					});
					executorService.shutdown();
				}catch(Exception e){
					e.printStackTrace();
					executorService.shutdown();
				}
			}else{
				// if approvals are thre then mail send to next approval person
				String approveEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(approvalEmpId);
				String data[] = approveEmail.split(",");
				String strApproveName=data[0];
				String approveToEmailAddress[]={data[1]};
				Date dateobj = new Date();
			    String CreationDate=new SimpleDateFormat("dd-MM-yyyy").format(dateobj);
			    Object ApproveData[]={strIndentNo,approvalEmpId,strSiteId,String.valueOf(temprevision_no),editPonumber,CreationDate,poNumber,siteWiseIndentNo,siteName,
			    					strVendorName,String.valueOf(strGrandTotalVal),strApproveName,preparedBy,passwdForMail,String.valueOf(getLocalPort),strVendorId,oldPOEntryId,strPODate};
				EmailFunction objEmailFunction = new EmailFunction();
				objEmailFunction.sendMailForTempApprove(strApproveName,approveToEmailAddress,getLocalPort,ApproveData);
				}
			request.setAttribute("listOfTermsAndConditions", listOfTermsAndConditions);

			objviewPOPageDataMap.put("AddressDetails", strTableOneData);
			objviewPOPageDataMap.put("productDetails", strTableTwoDate);
			objviewPOPageDataMap.put("TransportDetails", strTableThreeData);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
			request.setAttribute("receiverState", receiverState); // this is for success page generated then it display based on state below print button
			//	strResponse = "Success";
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//if close
		}//try close

		catch(Exception e){
			//	strResponse = "Fail";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}

		return strResponse;
	}

	
/*	*************************************************when po was revised at that time Acc dept tables inactive start**********************************/
	private String inactivePendingApprovalsInAccDeptIfPoAmountDecreased(String poNo,String siteId,String vendorId,double revisedPoAmount,List<String> allEmployeeEmailsOfAcc,String receiverState) {
		Map<String,Object> AccPaymentDetails = objPurchaseDepartmentIndentProcessDao.getAccPaymentDetailsByPoNo(poNo, siteId, vendorId);
		if(AccPaymentDetails==null){
			return "NoData";
		}
		int paymentId = Integer.parseInt(AccPaymentDetails.get("PAYMENT_ID").toString());
		double poAmount = Double.parseDouble(AccPaymentDetails.get("PO_AMOUNT").toString());
		double paymentReqUpto = Double.parseDouble(AccPaymentDetails.get("PAYMENT_REQ_UPTO").toString());
		if(paymentReqUpto==0){
			return "NoPaymentInitiated";
		}
		if(revisedPoAmount>poAmount){
			return "RevisedPoAmountIncreased";
		}
		List<Integer> listOfInitiatedPaymentDtlsIds = objPurchaseDepartmentIndentProcessDao.getInitiatedPaymentDtlsIdsByPaymentId(paymentId);
		if(listOfInitiatedPaymentDtlsIds.isEmpty()){
			return "NoAdvanceInitiated";
		}
		List<Integer> listOfPaymentCompletedPaymentDtlsIds = objPurchaseDepartmentIndentProcessDao.getPaymentCompletedPaymentDtlsIdsByPaymentId(paymentId);
		if(!listOfPaymentCompletedPaymentDtlsIds.isEmpty()){
			for(Integer id : listOfPaymentCompletedPaymentDtlsIds){
				listOfInitiatedPaymentDtlsIds.remove(id);
			}
		}
		if(listOfInitiatedPaymentDtlsIds.isEmpty()){
			return "InitiatedAdvancePaymentDone";
		}
		
		//Inactive PendingApprovals In Acc Dept
		for(Integer paymentDetailsId : listOfInitiatedPaymentDtlsIds){
			boolean isPaymentCameToAccDept=false;
			double reqAmount = Double.parseDouble(objPurchaseDepartmentIndentProcessDao.getRequestedAmount(paymentDetailsId));
			if(objPurchaseDepartmentIndentProcessDao.isAccTempPaymentTransactionTblHasPaymentDetailsId(paymentDetailsId)){
				isPaymentCameToAccDept=true;
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccTempPaymentTransactions(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccDeptPmtProcessTbl(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccPaymentDtls(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.updateReqUptoInAccPayment(reqAmount,paymentId);
			}
			else if(objPurchaseDepartmentIndentProcessDao.isAccDeptPmtProcessTblHasPaymentDetailsId(paymentDetailsId)){
				isPaymentCameToAccDept=true;
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccDeptPmtProcessTbl(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccPaymentDtls(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.updateReqUptoInAccPayment(reqAmount,paymentId);
			}
			else if(objPurchaseDepartmentIndentProcessDao.isAccPaymentDtlsHasPaymentDetailsId(paymentDetailsId)){
				objPurchaseDepartmentIndentProcessDao.inactiveRowInAccPaymentDtls(paymentDetailsId);
				objPurchaseDepartmentIndentProcessDao.updateReqUptoInAccPayment(reqAmount,paymentId);
			}
			//
			List<String>  list=objPurchaseDepartmentIndentProcessDao.getSiteLevelPoAmountInitiateEmail(paymentDetailsId);
			if(list.size()>0){
			for(int i=0;i<list.size();i++){
				allEmployeeEmailsOfAcc.add(list.get(i));
			}
			}
			if(isPaymentCameToAccDept){
				List<String> accMails=null;
				if(receiverState.equalsIgnoreCase("Telangana")){
					 accMails=objPurchaseDepartmentIndentProcessDao.getAccDeptEmails("997_H_");
				}else{accMails=objPurchaseDepartmentIndentProcessDao.getAccDeptEmails("997_B_");}
				if(accMails.size()>0){
					for(int i=0;i<accMails.size();i++){
						allEmployeeEmailsOfAcc.add(accMails.get(i));
					}
					}
				
			}
			//
				
		}
		
		return "Success";
		
	}
	@Override
	public String SaveEnquiryForm(Model model, HttpServletRequest request,HttpSession session)
	{

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_saveEF, ");

		ProductDetails productDetails =null;
		String strResponse = "";
		int strPONumber = 0;
		String strVendorId = ""; 
		String vendorName = ""; 
		String strIndentNo = "";
		String strToSite="";
		String SiteName="";
		String siteWiseIndentNo = "";
		String numOfRec[] = null;

		try{

			int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
			strVendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			siteWiseIndentNo = request.getParameter("siteWiseIndentNo");
			SiteName = request.getParameter("SiteName");
			String pass = request.getParameter("pass");
			request.setAttribute("vendorName",vendorName);
			//	System.out.println("the vendor id is "+strVendorId);
			int sno = 0;
			if(noofrows > 0){
				strIndentNo = request.getParameter("indentNumber");
				strToSite = request.getParameter("toSiteId"); //toSiteId
				WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , IndentNumber:"+strIndentNo+" , Vendor:"+vendorName);


				String acceptTermsAndConditionTotal =(request.getParameter("countOftermsandCondsfeilds"));
				if((acceptTermsAndConditionTotal != null) && (!acceptTermsAndConditionTotal.equals(""))) {
					numOfRec = acceptTermsAndConditionTotal.split(",");
				}
				int records_Count =(numOfRec.length);
				String termsAndCondition = "";
				//int val=0;
				objPurchaseDepartmentIndentProcessDao.deleteVndTermsAdnConditions(strIndentNo,strVendorId);
				for(int i=0;i<records_Count;i++){
					int num=Integer.parseInt(numOfRec[i]);
					termsAndCondition =  request.getParameter("termsAndCond"+num);
					if(termsAndCondition!=null && !termsAndCondition.equals("")){
					objPurchaseDepartmentIndentProcessDao.saveVendorTermsconditions(termsAndCondition,strVendorId,strIndentNo);
					}
					//System.out.println("the resulting value is "+val);

				}



				List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
				for(int num=1;num<=noofrows;num++){


					if(noofrows!=0)
					{
						sno++;
						String productId = request.getParameter("productId"+num);
						String subProductId = request.getParameter("subProductId"+num);
						String childProductId = request.getParameter("childProductId"+num);
						String productName = request.getParameter("ProductName"+num);
						String subProductName = request.getParameter("SubProductName"+num);
						String childProductName = request.getParameter("ChildProductName"+num);
						String childProductCustomerDesc = request.getParameter("childProductCustomerDesc"+num);


						String setMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
						String setMeasurementName = request.getParameter("unitsOfMeasurement"+num);
						String purchaseDepartmentIndentProcessSeqId = request.getParameter("purchaseDepartmentIndentProcessSeqId"+num);
						String poIntiatedQuantity = request.getParameter("poIntiatedQuantity"+num);
						String requestQuantity = request.getParameter("requestQantity"+num);
						String pendingQuantity = request.getParameter("pendingQuantity"+num);
						String quantity = request.getParameter("stQantity"+num);
						//String quantity = request.getParameter("stQantity"+num);
						String indentNo = request.getParameter("indentNumber");


						String hsnCode = request.getParameter("hsnCode"+num);
						String price = request.getParameter("price"+num);
						String basicAmount = request.getParameter("basicAmount"+num);
						String Discount=request.getParameter("Discount"+num);
						String amountAfterDiscount=request.getParameter("amtAfterDiscount"+num);
						String tax = request.getParameter("tax"+num);
						String taxAmount = request.getParameter("taxAmount"+num);
						String amountAfterTax = request.getParameter("amountAfterTax"+num);
						String totalAmount = request.getParameter("totalAmount"+num);
						String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
						String strTaxId = "";
						String taxArr []  = tax.split("\\$");
						tax = taxArr[1];
						strTaxId = taxArr[0];
						String strStatus = "A";


						double otherOrTransportCharges = Double.valueOf(request.getParameter("OtherOrTransportChargesId"+num) == null ? "0" : request.getParameter("OtherOrTransportChargesId"+num).toString());
						double taxOnOtherOrTransportCharges =  Double.valueOf(request.getParameter("TaxOnOtherOrTransportChargesId"+num)== null ? "0" : request.getParameter("TaxOnOtherOrTransportChargesId"+num).toString());
						double otherOrTransportChargesAfterTax =  Double.valueOf(request.getParameter("OtherOrTransportChargesAfterTaxId"+num)== null ? "0" : request.getParameter("OtherOrTransportChargesAfterTaxId"+num).toString());

						String productBatchData = request.getParameter("productBatchData"+num);
						if(productBatchData!=null&&!productBatchData.equals("")){
							String[] icdArray = productBatchData.split("@@");
							int noofsplitproducts = icdArray.length;
							for(int i=0;i<noofsplitproducts;i++)
							{
								String currentProdData = icdArray[i];
								String individualICDId = currentProdData.split("=")[0];
								String individualQuantity = currentProdData.split("=")[1];


								productDetails = new ProductDetails();
								productDetails.setStrSerialNumber(String.valueOf(sno));
								productDetails.setProductId(productId);
								productDetails.setProductName(productName);
								productDetails.setSub_ProductId(subProductId);
								productDetails.setSub_ProductName(subProductName);
								productDetails.setChild_ProductId(childProductId);
								productDetails.setChild_ProductName(childProductName);
								productDetails.setMeasurementId(setMeasurementId);
								productDetails.setMeasurementName(setMeasurementName);
								//productDetails.setPendingQuantity(pendingQuantity);//po quantity

								productDetails.setQuantity(individualQuantity); //customer menioned Quantity
								productDetails.setHsnCode(hsnCode);
								productDetails.setPricePerUnit(price);
								productDetails.setBasicAmt(doCalculation(basicAmount,individualQuantity,quantity));
								productDetails.setStrDiscount(Discount);
								productDetails.setStrAmtAfterDiscount(doCalculation(amountAfterDiscount,individualQuantity,quantity));
								productDetails.setTax(tax);
								productDetails.setTaxId(strTaxId);
								productDetails.setTaxAmount(doCalculation(taxAmount,individualQuantity,quantity));
								productDetails.setAmountAfterTax(doCalculation(amountAfterTax,individualQuantity,quantity));
								productDetails.setTotalAmount(doCalculation(totalAmount,individualQuantity,quantity));
								productDetails.setStrIndentId(indentNo);
								productDetails.setVendorId(strVendorId);
								productDetails.setSite_Id(strToSite);
								productDetails.setChildProductCustDisc(childProductCustomerDesc);

								productDetails.setOtherOrTransportCharges1(0.0);
								productDetails.setTaxOnOtherOrTransportCharges1(0.0);
								productDetails.setOtherOrTransportChargesAfterTax1(0.0);
								productDetails.setIndentCreationDetailsId(individualICDId);


								objPurchaseDepartmentIndentProcessDao.updateEnquiryFormDetails(productDetails);

							} 
						}
						else
						{
							productDetails = new ProductDetails();
							productDetails.setStrSerialNumber(String.valueOf(sno));
							productDetails.setProductId(productId);
							productDetails.setProductName(productName);
							productDetails.setSub_ProductId(subProductId);
							productDetails.setSub_ProductName(subProductName);
							productDetails.setChild_ProductId(childProductId);
							productDetails.setChild_ProductName(childProductName);
							productDetails.setMeasurementId(setMeasurementId);
							productDetails.setMeasurementName(setMeasurementName);
							//productDetails.setPendingQuantity(pendingQuantity);//po quantity
							productDetails.setQuantity(quantity); //customer menioned Quantity

							productDetails.setHsnCode(hsnCode);
							productDetails.setPricePerUnit(price);
							productDetails.setBasicAmt(basicAmount);
							productDetails.setStrDiscount(Discount);
							productDetails.setStrAmtAfterDiscount(amountAfterDiscount);
							productDetails.setTax(tax);
							productDetails.setTaxId(strTaxId);
							productDetails.setTaxAmount(taxAmount);
							productDetails.setAmountAfterTax(amountAfterTax);
							productDetails.setTotalAmount(totalAmount);
							productDetails.setStrIndentId(indentNo);
							productDetails.setVendorId(strVendorId);
							productDetails.setSite_Id(strToSite);
							productDetails.setChildProductCustDisc(childProductCustomerDesc);

							productDetails.setOtherOrTransportCharges1(otherOrTransportCharges);
							productDetails.setTaxOnOtherOrTransportCharges1(taxOnOtherOrTransportCharges);
							productDetails.setOtherOrTransportChargesAfterTax1(otherOrTransportChargesAfterTax);
							productDetails.setIndentCreationDetailsId(indentCreationDetailsId);


							objPurchaseDepartmentIndentProcessDao.updateEnquiryFormDetails(productDetails);
						}



					}

				}
			}//for close
			String newPassword = getNewPasswordAfterRemove(strVendorId,strIndentNo,pass);
			objPurchaseDepartmentIndentProcessDao.setVendorPasswordInDB(newPassword,strVendorId);



			strResponse = "Success";


			if(strResponse.equalsIgnoreCase("success")){


				String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
				List<String> allEmployeeEmailsOfPD = new ArrayList<String>();
				if(!strToSite.equals("112")){

					allEmployeeEmailsOfPD = objPurchaseDepartmentIndentProcessDao.getAllEmployeeEmailsUnderDepartment(purchaseDeptId);

					String ccTo [] = new String[allEmployeeEmailsOfPD.size()];
					allEmployeeEmailsOfPD.toArray(ccTo);

					EmailFunction objEmailFunction = new EmailFunction();



					objEmailFunction.sendEnquiryFormFillMailToPurchaseDept(ccTo,siteWiseIndentNo,vendorName,SiteName);
				}
			}


			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//if close
		}//try close

		catch(Exception e){
			strResponse = "Fail";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}

		return strResponse;

	}
	public String doCalculation(String strInput,String strIndividualQuantity,String strQuantity)
	{
		if(strQuantity.equals("0")||strQuantity.equals("0.0")){return "0";}
		else{}


		if(strInput == null || strInput.equals("")){

			return "0";
		}
		else{}

		if(strIndividualQuantity == null || strIndividualQuantity.equals("")){

			return "0";
		}
		else{}

		if(strQuantity == null || strQuantity.equals("")){

			return "0";
		}
		else{}


		double input = Double.parseDouble(strInput);
		double individualQuantity = Double.parseDouble(strIndividualQuantity);
		double quantity = Double.parseDouble(strQuantity);
		double output = (input*individualQuantity)/quantity;

		return String.valueOf(output);
	}
	public String getNewPasswordAfterRemove(String strVendorId,String strIndentNo, String pass)
	{
		String newIndentWisePasswords = "";
		String indentWisePasswords= "";
		indentWisePasswords = objPurchaseDepartmentIndentProcessDao.getVendorPasswordInDB(strVendorId);
		if(indentWisePasswords!=null&&indentWisePasswords!="")
		{

			indentWisePasswords = indentWisePasswords.substring(0, indentWisePasswords.length() - 2);
			String[] indentWisePasswordArray = indentWisePasswords.split("@@");
			for(String indentWisePwd : indentWisePasswordArray)
			{
				String[] indentAndPwd = indentWisePwd.split("=");
				String indent = indentAndPwd[0];
				String pwd = indentAndPwd[1];
				if(indent.equals(strIndentNo)){
					if(pass.equals(pwd))
					{

					}
				}
				else{
					newIndentWisePasswords+=indent+"="+pwd+"@@";
				}
			}

		}
		return newIndentWisePasswords;

	}

	@Override
	public void printIndent(Model model, HttpServletRequest request, int site_id, String user_id) {
		int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		int siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
		String strCreateDate="";
		int  orderQuantity=0;
		//System.out.println(indentNumber);
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		String siteId=request.getParameter("siteId");

		 String versionNo=request.getParameter("versionNo");
		 String reference_No=request.getParameter("reference_No");
		 String issue_date=request.getParameter("issue_date");
		
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		for(int num=1;num<=noofrows;num++){

			sno++;
			String product = request.getParameter("product"+num);
			String subProduct = request.getParameter("subProduct"+num);
			//String childProduct = request.getParameter("childProduct"+num);               //actual name
			String childProduct = request.getParameter("childProductPuchaseDeptDisc"+num); 

			if(childProduct == null ){
				childProduct = request.getParameter("childProduct"+num); 
			}


			//changed name 
			String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);

			//what is exactly meaning by requiredQuantity as per JSP it is different(element.pendingQuantity)

			String requiredQuantity = request.getParameter("requiredQuantity1"+num);
			String productAvailability=request.getParameter("productAvailability"+num);
			String remarks=request.getParameter("remarks"+num);
			double strRequiredQty=Double.parseDouble(requiredQuantity);
			double strProductAvail=Double.parseDouble(productAvailability);
			double strrequiredQuantity=0;

			strrequiredQuantity=(strRequiredQty)+(strProductAvail);
			requiredQuantity=String.valueOf(strrequiredQuantity);


			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			indentCreationBean.setSubProduct1(subProduct);
			indentCreationBean.setChildProduct1(childProduct);
			indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
			indentCreationBean.setRequiredQuantity1(requiredQuantity);
			indentCreationBean.setProductAvailability(productAvailability);
			indentCreationBean.setOrderQuantity(strRequiredQty);
			indentCreationBean.setRemarks1(remarks);
			IndentCreationBean icb = new IndentCreationBean();
			icb = cntlIndentrocss.getCreateAndRequiredDates(indentNumber);
			indentCreationBean.setStrRequiredDate(icb.getStrRequiredDate());
			strCreateDate = icb.getStrCreateDate();
			list.add(indentCreationBean);

		}
		request.setAttribute("IndentDetails",list);
		//	System.out.println(list.get(0).getChildProduct1());
		List<IndentCreationBean> list1 = new ArrayList<IndentCreationBean>();
		IndentCreationBean icb1 = new IndentCreationBean();
		icb1.setIndentNumber(indentNumber);
		icb1.setSiteWiseIndentNo(siteWiseIndentNo);
		icb1.setVersionNo(versionNo);
		icb1.setIssue_date(issue_date);
		icb1.setReference_No(reference_No);
		
		icb1.setStrCreateDate(strCreateDate);
		list1.add(icb1);
		request.setAttribute("IndentDtls",list1);
		model.addAttribute("createdSiteName",cntlIndentrocss.getSiteNameWhereIndentCreated(indentNumber));
		model.addAttribute("siteAddress",cntlIndentrocss.getAddressOfSite(site_id));
		model.addAttribute("listCreatedName",objPurchaseDepartmentIndentProcessDao.getApproveCreateEmp(indentNumber,request));
		//	 model.addAttribute("createEmpName",createEmpName);
		objPurchaseDepartmentIndentProcessDao.getVerifiedEmpNames(indentNumber,request,siteId);


	}


	@Override
	public List<ProductDetails> getIndentsProductWise(String product, String subProduct, String childProduct) {
		return objPurchaseDepartmentIndentProcessDao.getIndentsProductWise(product,subProduct,childProduct);

	}

	@Override
	public List<ProductDetails> loadCreatePOPage(Model model, HttpServletRequest request) {
		//int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		//System.out.println(indentNumber);
		//List<ProductDetails> list = new ArrayList<ProductDetails>();
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		for(int num=1;num<=noofrows;num++){
			if(request.getParameter("checkboxname"+num)!=null)
			{




				String productId = request.getParameter("productId"+num);
				String subProductId = request.getParameter("subProductId"+num);
				String childProductId = request.getParameter("childProductId"+num);
				String productName = request.getParameter("productName"+num);
				String subProductName = request.getParameter("subProductName"+num);
				String childProductName = request.getParameter("childProductName"+num);
				String setMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
				String setMeasurementName = request.getParameter("unitsOfMeasurementName"+num);
				String requiredQuantity = request.getParameter("requiredQuantity"+num);
				String indentNo = request.getParameter("indentNo"+num);
				ProductDetails productDetails = new ProductDetails();
				productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setProductId(productId);
				productDetails.setProductName(productName);
				productDetails.setSub_ProductId(subProductId);
				productDetails.setSub_ProductName(subProductName);
				productDetails.setChild_ProductId(childProductId);
				productDetails.setChild_ProductName(childProductName);
				productDetails.setMeasurementId(setMeasurementId);
				productDetails.setMeasurementName(setMeasurementName);
				productDetails.setQuantity(requiredQuantity);
				boolean isAlreadyThere = false;
				for(int i=0;i<listProductDetails.size();i++)
				{
					if(listProductDetails.get(i).getChild_ProductName().equals(childProductName))
					{
						isAlreadyThere = true;
						int temp = Integer.parseInt(listProductDetails.get(i).getQuantity());
						temp+=Integer.parseInt(requiredQuantity);
						listProductDetails.get(i).setQuantity(String.valueOf(temp));
						break;
					}
				}
				if(!isAlreadyThere)
				{
					sno++;
					productDetails.setStrSerialNumber(String.valueOf(sno));
					listProductDetails.add(productDetails);
				}




			}
		}

		return listProductDetails;
		//request.setAttribute("IndentDetails",list);

	}

	@Override
	public List<Map<String, Object>> getAllProducts() {
		String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();

		return objPurchaseDepartmentIndentProcessDao.getAllProducts(purchaseDeptId);
	}


	@Override
	public List<Map<String, Object>> sendenquiry2(Model model, HttpServletRequest request, int site_id, String user_id,String strSiteId) {
		int enquiryFormdetailsId = 0;
		final List<VendorDetails>  listOfVendorDetails =new ArrayList<VendorDetails>();
		int noOfVendors= Integer.parseInt(request.getParameter("noofvendorsproccessed"));
		for(int vnum=1;vnum<=noOfVendors;vnum++){
			String VendorName = request.getParameter("vendorName"+vnum);
			String GSTINNo = request.getParameter("strGSTINNumber"+vnum);
			String VendorAddress = request.getParameter("vendorAddress"+vnum);
			String vendorId= request.getParameter("vendorId"+vnum);
			String vendorPass = CommonUtilities.getStan();
			VendorDetails vendorDetails = new VendorDetails();
			vendorDetails.setVendor_name(VendorName);
			vendorDetails.setGsin_number(GSTINNo);
			vendorDetails.setAddress(VendorAddress);
			vendorDetails.setVendor_Id(vendorId);
			vendorDetails.setVendor_Pass(vendorPass);
			listOfVendorDetails.add(vendorDetails);
		}
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		String indentCreationDetailsIdForenquiry = "";
		for(int num=1;num<=noofrows;num++){
			if(request.getParameter("checkboxname"+num)!=null)
			{

				String productId = request.getParameter("productId"+num);
				String subProductId = request.getParameter("subProductId"+num);
				String childProductId = request.getParameter("childProductId"+num);
				String unitsOfMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
				String product = request.getParameter("productName"+num);
				String subProduct = request.getParameter("subProductName"+num);
				//String childProduct = request.getParameter("childProductName"+num);          //actual name
				String childProduct = request.getParameter("childProductPuchaseDeptDisc"+num); //changed name
				String unitsOfMeasurement = request.getParameter("unitsOfMeasurementName"+num);
				String requiredQuantity = request.getParameter("requiredQuantity"+num);
				int indentCreationDetailsId = Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));
				int indentNumber = Integer.parseInt(request.getParameter("indentNo"+num));
				int reqSiteId = Integer.parseInt(request.getParameter("strSiteId"+num));



				String strChildProductPurchasedisc = request.getParameter("childProductPuchaseDeptDisc"+num); 



				indentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry+indentCreationDetailsId+",";

				IndentCreationBean indentCreationBean = new IndentCreationBean();
				indentCreationBean.setProductId1(productId);
				indentCreationBean.setSubProductId1(subProductId);
				indentCreationBean.setChildProductId1(childProductId);
				indentCreationBean.setUnitsOfMeasurementId1(unitsOfMeasurementId);
				indentCreationBean.setSubProduct1(subProduct);
				indentCreationBean.setChildProduct1(childProduct);
				indentCreationBean.setPurchaseDeptChildProdDisc(strChildProductPurchasedisc);
				indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
				indentCreationBean.setRequiredQuantity1(requiredQuantity);
				indentCreationBean.setIndentCreationDetailsId(indentCreationDetailsId);
				indentCreationBean.setSiteId(reqSiteId);
				indentCreationBean.setIndentNumber(indentNumber);
				sno++;
				indentCreationBean.setStrSerialNumber(String.valueOf(sno));
				for(VendorDetails vendorDetails : listOfVendorDetails){
					indentCreationBean.setVendorId(vendorDetails.getVendor_Id());
					enquiryFormdetailsId = objPurchaseDepartmentIndentProcessDao.getEnquiryFormDetailsId();
					indentCreationBean.setEnquiryFormDetailsId(enquiryFormdetailsId);
					int result = objPurchaseDepartmentIndentProcessDao.insertVendorEnquiryFormDetails(indentCreationBean);
				}
				list.add(indentCreationBean);

			}
		}
		indentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry.substring(0,indentCreationDetailsIdForenquiry.length()-1);

		List<Map<String, Object>> listReceiverDtls=objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(strSiteId);
		request.setAttribute("IndentDetails",list);
		final String sendEnquiryEmpDetails=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(user_id);
		final int getLocalPort = request.getLocalPort();
		//
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		try{

			final String finalIndentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry;


			executorService.execute(new Runnable() {
				public void run() {
					sendEmailForEnquiry( "", 0, "Sumadhura Purchase Department", "",listOfVendorDetails,finalIndentCreationDetailsIdForenquiry,"-",0,"",sendEnquiryEmpDetails,getLocalPort,"","");
				}
			});
			executorService.shutdown();
		}catch(Exception e){
			e.printStackTrace();
			executorService.shutdown();
		}
		//
		return listReceiverDtls;
	}



	@Override
	public List<Map<String, Object>> sendenquiry(Model model, HttpServletRequest request, int site_id, String user_id,final String siteId) {
		final int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		final String siteName= request.getParameter("siteName");
		final int siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
		String strsiteId= request.getParameter("siteId");

		final List<VendorDetails>  listOfVendorDetails =new ArrayList<VendorDetails>();
		int noOfVendors= Integer.parseInt(request.getParameter("noofvendorsproccessed"));
		for(int vnum=1;vnum<=noOfVendors;vnum++){
			String VendorName = request.getParameter("vendorName"+vnum);
			String GSTINNo = request.getParameter("strGSTINNumber"+vnum);
			String VendorAddress = request.getParameter("vendorAddress"+vnum);
			String vendorId= request.getParameter("vendorId"+vnum);
			String vendorPass = CommonUtilities.getStan();
			VendorDetails vendorDetails = new VendorDetails();
			vendorDetails.setVendor_name(VendorName);
			vendorDetails.setGsin_number(GSTINNo);
			vendorDetails.setAddress(VendorAddress);
			vendorDetails.setVendor_Id(vendorId);
			vendorDetails.setVendor_Pass(vendorPass);
			listOfVendorDetails.add(vendorDetails);
		}

		final String userMailId=objPurchaseDepartmentIndentProcessDao.getUserMailIds(user_id); // this is used for getting the mail id for who send sendenquiry
		//System.out.println(indentNumber);
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String unitsOfMeasurementName = "";
		String productId = "";
		String subProductId = "";
		String childProductId = "";
		String unitsOfMeasurementId = "";
		String requiredQuantity = "";
		String strRequestQuantity = "";
		int indentCreationDetailsId = 0;
		String indentCreationDetailsIdForenquiry = "";
		int enquiryFormdetailsId = 0;
		String strChildProductPurchasedisc = "";
		final String ccmails=request.getParameter("ccmails") == null ? "" : request.getParameter("ccmails");
		for(int num=1;num<=noofrows;num++){
			if(request.getParameter("checkboxname"+num)!=null)
			{
				sno++;
				productName = request.getParameter("product"+num);
				subProductName = request.getParameter("subProduct"+num);
				//childProductName = request.getParameter("childProduct"+num);               //actual name
				childProductName = request.getParameter("childProductPuchaseDeptDisc"+num);  //changed name 
				unitsOfMeasurementName = request.getParameter("unitsOfMeasurement"+num);
				productId = request.getParameter("productId"+num);
				subProductId = request.getParameter("subProductId"+num);
				childProductId = request.getParameter("childProductId"+num);
				unitsOfMeasurementId = request.getParameter("unitsOfMeasurementId"+num);

				requiredQuantity = request.getParameter("POPendingQuantity"+num);//requiredQuantity
				//strRequestQuantity = request.getParameter("strRequestQuantity"+num);
				indentCreationDetailsId = Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));



				strChildProductPurchasedisc = request.getParameter("childProductPuchaseDeptDisc"+num); 



				indentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry+indentCreationDetailsId+",";

				IndentCreationBean indentCreationBean = new IndentCreationBean();
				indentCreationBean.setStrSerialNumber(String.valueOf(sno));
				indentCreationBean.setSubProduct1(subProductName);
				indentCreationBean.setChildProduct1(childProductName);
				indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurementName);
				indentCreationBean.setRequiredQuantity1(requiredQuantity);
				indentCreationBean.setProductId1(productId);
				indentCreationBean.setSubProductId1(subProductId);
				indentCreationBean.setChildProductId1(childProductId);
				indentCreationBean.setUnitsOfMeasurementId1(unitsOfMeasurementId);


				indentCreationBean.setSiteId(Integer.parseInt(siteId));
				indentCreationBean.setIndentNumber(indentNumber);

				indentCreationBean.setPurchaseDeptChildProdDisc(strChildProductPurchasedisc);
				indentCreationBean.setIndentCreationDetailsId(indentCreationDetailsId);

				for(VendorDetails vendorDetails : listOfVendorDetails){
					indentCreationBean.setVendorId(vendorDetails.getVendor_Id());
					enquiryFormdetailsId = objPurchaseDepartmentIndentProcessDao.getEnquiryFormDetailsId();
					indentCreationBean.setEnquiryFormDetailsId(enquiryFormdetailsId);
					int result = objPurchaseDepartmentIndentProcessDao.insertVendorEnquiryFormDetails(indentCreationBean);
				}

				list.add(indentCreationBean);
			}
		}
		indentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry.substring(0,indentCreationDetailsIdForenquiry.length()-1);


		//String vendorId = request.getParameter("vendorId");

		List<Map<String, Object>> listReceiverDtls=objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(siteId);
		request.setAttribute("IndentDetails",list);
		request.setAttribute("receiverState",objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(siteId));
		final String address= objPurchaseDepartmentIndentProcessDao.getSiteAddress(strsiteId);
		final String sendEnquiryEmpDetails=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(user_id);
		final int getLocalPort = request.getLocalPort();




		boolean isFillForm = false;
		if(request.getAttribute("isFillForm")!=null){
			if(request.getAttribute("isFillForm").toString().equals("yes")){isFillForm = true;}
		}
		if(isFillForm){}
		else{
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{

				final String finalIndentCreationDetailsIdForenquiry = indentCreationDetailsIdForenquiry;


				executorService.execute(new Runnable() {
					public void run() {
						sendEmailForEnquiry( "", indentNumber, "Sumadhura Purchase Department", siteName,listOfVendorDetails,finalIndentCreationDetailsIdForenquiry,address,siteWiseIndentNo,siteId,sendEnquiryEmpDetails,
								getLocalPort,ccmails,userMailId);
					}
				});
				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}



		}
		return listReceiverDtls;
	}
	public void sendEmailForEnquiry(String pendingEmpId,int indentNumber,String indentFrom,String indentTo,List<VendorDetails> listOfVendorDetails,String indentCreationDetailsIdForenquiry,String address, int siteWiseIndentNo,String strIndentSiteId,String sendEnquiryEmpDetails,
			int getLocalPort,String ccmails,String userMailId){


		try{
			for(VendorDetails vendorDetails : listOfVendorDetails){
				//List<Map<String, Object>> indentCreationDtls = null;
				List<String> toMailListArrayList = new ArrayList<String>();
				String emailto [] = null ;
				/*if(!pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber, "approvalToEmployee");
			}else if(pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber,"approvalToDept");

				toMailListArrayList = icd.getAllEmployeeEmailsUnderDepartment(999);
			}*/
				String newIndentWisePasswords= "";
				String indentWisePasswords= "";
				indentWisePasswords = objPurchaseDepartmentIndentProcessDao.getVendorPasswordInDB(vendorDetails.getVendor_Id());

				newIndentWisePasswords+=indentWisePasswords+indentNumber+"="+vendorDetails.getVendor_Pass()+"@@";
				/*if(indentWisePasswords!=null&&indentWisePasswords!="")
			{
				boolean isAlreadyThere = false;
				indentWisePasswords = indentWisePasswords.substring(0, indentWisePasswords.length() - 2);
				String[] indentWisePasswordArray = indentWisePasswords.split("@@");
				for(String indentWisePwd : indentWisePasswordArray)
				{
					String[] indentAndPwd = indentWisePwd.split("=");
					String indent = indentAndPwd[0];
					String pwd = indentAndPwd[1];
					if(indent.equals(String.valueOf(indentNumber))){
						pwd = vendorDetails.getVendor_Pass();
						isAlreadyThere = true;
					}
					newIndentWisePasswords+=indent+"="+pwd+"@@";
				}

				if(!isAlreadyThere)
				{
					newIndentWisePasswords+=indentNumber+"="+vendorDetails.getVendor_Pass()+"@@";
				}

			}
			else
			{
				newIndentWisePasswords+=indentNumber+"="+vendorDetails.getVendor_Pass()+"@@";
			}*/

				objPurchaseDepartmentIndentProcessDao.setVendorPasswordInDB(newIndentWisePasswords,vendorDetails.getVendor_Id());
				String strIndentFromSite = "";
				//String strIndentFromDate = "";
				//String strEmailAddress =   "";
				/*for(Map<String, Object> objIndentCreationDtls : indentCreationDtls) {



				strIndentFromSite = objIndentCreationDtls.get("SITE_NAME")==null ? "" :   objIndentCreationDtls.get("SITE_NAME").toString();
				strIndentFromDate = objIndentCreationDtls.get("CREATE_DATE")==null ? "" :   objIndentCreationDtls.get("CREATE_DATE").toString();
				strEmailAddress = objIndentCreationDtls.get("Email_id")==null ? "" :   objIndentCreationDtls.get("Email_id").toString();

			}*/
				String vendoremail="";
				String contact_person_Name="";//for vendor send mail show vendor name
				String createDate = objPurchaseDepartmentIndentProcessDao.getIndentCreationDate(indentNumber);
				Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorEmail(vendorDetails.getVendor_Id());

				for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
					contact_person_Name=retVal.getKey();
					vendoremail=retVal.getValue(); 
				}
				String[] EmailToArray = vendoremail.split(",");
				for(int i=0;i<EmailToArray.length;i++){
					toMailListArrayList.add(EmailToArray[i]);
				}
				//	toMailListArrayList.add(vendoremail);
				/*=======================================this is used for send enquiry purpose start=================================================*/
				Set<String> allEmployeeEmailsOfPD=new HashSet<String>();
				//List<String> allEmployeeEmailsOfPD = new ArrayList<String>();
				if(!ccmails.equals("")&&ccmails!=null){
					String[] ccEmailToArray = ccmails.split(",");
					for(int i=0;i<ccEmailToArray.length;i++){
						allEmployeeEmailsOfPD.add(ccEmailToArray[i]);
					}
				}if(!userMailId.equals("")&&userMailId!=null){
					allEmployeeEmailsOfPD.add(userMailId);
				}
				String ccTo [] = new String[allEmployeeEmailsOfPD.size()];
				allEmployeeEmailsOfPD.toArray(ccTo);
				/*===========================================this is used in send enquiry purpose end================================================*/
				emailto = new String[toMailListArrayList.size()];
				toMailListArrayList.toArray(emailto);

				EmailFunction objEmailFunction = new EmailFunction();



				objEmailFunction.sendMailToVendor( indentTo, indentNumber, indentFrom, 	strIndentFromSite, createDate, emailto , vendorDetails,indentCreationDetailsIdForenquiry,address,siteWiseIndentNo,strIndentSiteId,sendEnquiryEmpDetails,getLocalPort,ccTo);

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// this is common methode for all final po sending time vendor get mails
	public void sendEmailForPo(String pendingEmpId,int indentNumber,String indentFrom,String indentTo,VendorDetails vendorDetails,String poNumber,String ccEmailTo,String subject,String poCreatedEmpName,int getLocalPort,String revisedOrNot,String oldPONumber,String emailPoDate,
								List<String> allEmployeeEmailsOfAcc,String acc_Note_Email){


		try{
			//List<Map<String, Object>> indentCreationDtls = null;
			List<String> toMailListArrayList = new ArrayList<String>();
			String emailto [] = null ;
			String strIndentFromSite = "";
			String createDate = "now";//objPurchaseDepartmentIndentProcessDao.getIndentCreationDate(indentNumber);
			String vendoremail="";
			String contact_person_Name="";//for vendor send mail show vendor name
			Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorEmail(vendorDetails.getVendor_Id());
			for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
				contact_person_Name=retVal.getKey();
				vendoremail=retVal.getValue(); 
			}	

			if(vendoremail != null &&! vendoremail.equals("")){
				if(vendoremail.contains(",")){
					for(int i=0;i<vendoremail.split(",").length;i++){
						toMailListArrayList.add(vendoremail.split(",")[i]);
					}
				}else{
					toMailListArrayList.add(vendoremail);
				}
			}



			emailto = new String[toMailListArrayList.size()];
			toMailListArrayList.toArray(emailto);

			String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			List<String> allEmployeeEmailsOfPD = new ArrayList<String>();
			if(!indentFrom.equals("112")){
				allEmployeeEmailsOfPD = objPurchaseDepartmentIndentProcessDao.getAllEmployeeEmailsUnderDepartment( purchaseDeptId);
			}
			//	System.out.println("allEmployeeEmailsOfPD: "+allEmployeeEmailsOfPD);
			if(!ccEmailTo.equalsIgnoreCase("null") && ccEmailTo!=null && !ccEmailTo.equals("")){
				String[] ccEmailToArray = ccEmailTo.split(",");
				for(int i=0;i<ccEmailToArray.length;i++){
					allEmployeeEmailsOfPD.add(ccEmailToArray[i]);
				}
			}
			// account dept table any changes done then add mails to that
			if(!acc_Note_Email.equals("")&&acc_Note_Email!=null){
				for(int i=0;i<allEmployeeEmailsOfAcc.size();i++){
					allEmployeeEmailsOfPD.add(allEmployeeEmailsOfAcc.get(i));
				}
			}
			String ccTo [] = new String[allEmployeeEmailsOfPD.size()];
			allEmployeeEmailsOfPD.toArray(ccTo);

			EmailFunction objEmailFunction = new EmailFunction();



			objEmailFunction.sendMailToVendorForPO( indentTo, indentNumber, indentFrom,strIndentFromSite, createDate, emailto , vendorDetails,poNumber,ccTo,subject,poCreatedEmpName,getLocalPort,contact_person_Name,revisedOrNot,oldPONumber,emailPoDate,acc_Note_Email);

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	@Override
	public List<IndentCreationDto> getPendingIndents(String user_id,String site_id) {
		List<IndentCreationDto> list = objPurchaseDepartmentIndentProcessDao.getPendingIndents(user_id,site_id);
		return list;
	}


	@Override
	public List<IndentCreationBean> getAllPurchaseIndents() {
		String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		return cntlIndentrocss.getIndentFromAndToDetails(purchaseDeptId);
	}


	@Override
	public boolean getVendorPasswordInDB(String uname,int indentNumber,String pass) {
		boolean isValidPassword= false;
		String newIndentWisePasswords = "";
		String indentWisePasswords= "";
		indentWisePasswords = objPurchaseDepartmentIndentProcessDao.getVendorPasswordInDB(uname);
		boolean isThere = false;
		if(indentWisePasswords!=null&&indentWisePasswords!="")
		{

			indentWisePasswords = indentWisePasswords.substring(0, indentWisePasswords.length() - 2);
			String[] indentWisePasswordArray = indentWisePasswords.split("@@");
			for(String indentWisePwd : indentWisePasswordArray)
			{
				String[] indentAndPwd = indentWisePwd.split("=");
				String indent = indentAndPwd[0];
				String pwd = indentAndPwd[1];
				if(indent.equals(String.valueOf(indentNumber))){
					if(pass.trim().equals(pwd))
					{
						isValidPassword = true;
					}
				}
			}
		}
		return isValidPassword;
	}


	@Override
	public List<Map<String, Object>> getComparisionDtls(HttpServletRequest request,HttpSession session)
	{
		String strResponse = "failed";
		List<Map<String, Object>>  objproducts= null;
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		//CHILD_PRODUCT_ID
		List<String> childProductList = new ArrayList<String>();
		List<String> tempChildProductList = new ArrayList<String>();
		String childProductId =  "";
		String strChildProductList = "";
		String siteId=request.getParameter("siteId");
		boolean isSelect=false;
		int length=0;
		try{
			String indentNo = request.getParameter("indentNumber");
			String vendorId=request.getParameter("vendorId1");
			for(int num=1;num<=noofrows;num++){
				if(request.getParameter("checkboxname"+num)!=null)
				{
					childProductId = request.getParameter("childProductId"+num);
					strChildProductList = strChildProductList+"'"+childProductId+"',";
					childProductList.add(childProductId);
					tempChildProductList.add(childProductId);
					isSelect=true;

				}
			}if(strChildProductList.length()==0){
				Map<String,String> map=objPurchaseDepartmentIndentProcessDao.getChildProductsForCompare(indentNo);
				if(map!=null && map.size()>0){
					for ( Map.Entry<String,String> entry : map.entrySet()) {
						strChildProductList = strChildProductList+"'"+entry.getKey()+"',";
						childProductList.add(entry.getKey());
					}
				
				}
			}
			length=childProductList.size();
			if(isSelect){
				Map<String,String> map=objPurchaseDepartmentIndentProcessDao.getChildProductsForCompare(indentNo);
				for(String childId : tempChildProductList){
					if(map.containsKey(childId)){
						
					}else{
						childProductList.remove(childId);
						//num=length--;
					}
				}
				
			}
			
			request.setAttribute("childProductList", childProductList);

			if(strChildProductList.length() >0){
				strChildProductList =  strChildProductList.substring(0,strChildProductList.length()-1);


				objproducts= objPurchaseDepartmentIndentProcessDao.getComparisionDetails( indentNo, strChildProductList,vendorId);
				objproducts = equalizeQuantityInDifferentVendorsForSameProduct(objproducts);
				objproducts = getLeastPriceVendorForEachProduct(objproducts,request);
				objproducts = initializeZeroPriceForVendorNotGivenProducts(objproducts);
				objproducts = getPriceMasterDetailsForToolTip(objproducts,request);
				getConclusions(request);
				
			}

			request.setAttribute("receiverState",objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(siteId));

		}

		catch(Exception e){
			e.printStackTrace();
		}
		return objproducts;	
	}
	private void getConclusions(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		String poType = request.getParameter("poType");
		String poNumber = request.getParameter("poNumber");
		if(StringUtils.isBlank(poType)){
			//view comparison on indent
			request.setAttribute("conclusions", null);
		}
		else{
			if(poType.equals("temporary")){
				//view comparison on temp po
				List<String> conclusions = objPurchaseDepartmentIndentProcessDao.getTempPoConclusions(poNumber);
				request.setAttribute("conclusions", conclusions);
			}
			else if(poType.equals("permanent")){
				//view comparison on po
				List<String> conclusions = objPurchaseDepartmentIndentProcessDao.getPoConclusionsByPoNumber(poNumber);
				request.setAttribute("conclusions", conclusions);
			}
			else{
				request.setAttribute("conclusions", null);
			}
		}
		
		
	}


	private List<Map<String, Object>> getPriceMasterDetailsForToolTip(List<Map<String, Object>> objproducts, HttpServletRequest request) {
		String poType = request.getParameter("poType")==null ? "" : request.getParameter("poType");
		if(StringUtils.isBlank(poType)){
			return objproducts;
		}
		Map<String,String> PricesInPresentPO = getPricesInPresentPO(request);
		
		List<String> allChildProducts = new ArrayList<String>();
		for(Map<String, Object> prods : objproducts){
			if(prods.get("CHILD_PRODUCT_ID")!=null){
				String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				String childProductName = prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString();
				
				if(!allChildProducts.contains(childProductId+"@@"+childProductName)){
					allChildProducts.add(childProductId+"@@"+childProductName);
				}
			}
		}
		List<Map<String, Object>> allSitesList = utilDao.getAllSites();
		int tool_tip_id = 0;
		HashMap<Integer,List<String>> firstRowOfsitesMap = new HashMap<Integer,List<String>>();
		HashMap<Integer,List<String>> secondRowOfMonthsMap = new HashMap<Integer,List<String>>();
		HashMap<Integer,List<List<String>>> lastRowsOfPricesMap = new HashMap<Integer,List<List<String>>>();
		for(String childProduct : allChildProducts){
			tool_tip_id++;
			String childProductId = childProduct.split("@@")[0];
			String childProductName = childProduct.split("@@")[1];
			List<String> firstRowOfsites = new ArrayList<String>();
			List<String> secondRowOfMonths = new ArrayList<String>();
			List<List<String>> lastRowsOfPrices = new ArrayList<List<String>>();
			List<List<String>> transposeOfPrices = new ArrayList<List<String>>();
			
			List<String> POpriceList = new ArrayList<String>();
			POpriceList.add(PricesInPresentPO.get(childProductId)==null?"":PricesInPresentPO.get(childProductId).toString());
			transposeOfPrices.add(POpriceList);
			
			for(Map<String, Object> siteDetails : allSitesList){
				String siteId = siteDetails.get("SITE_ID")==null ? "" :   siteDetails.get("SITE_ID").toString();
				String siteName = siteDetails.get("SITE_NAME")==null ? "" :   siteDetails.get("SITE_NAME").toString();
				int colspan = 0;
				Map<String, Collection<PriceMasterDTO>> lastThreeMonthsPriceMasterDetail = reportsService.getLastThreeMonthPriceMasterDetail(childProductId, childProductName, "", siteId,"");
				List <String> months = Arrays.asList("firstMonth", "secondMonth", "thirdMonth");
				for(String month : months){
					Collection<PriceMasterDTO> oneMonthData = lastThreeMonthsPriceMasterDetail.get(month);
					if(oneMonthData.size()!=0){
						String month_name = "";
						List<String> priceList = new ArrayList<String>();
						for(PriceMasterDTO dto : oneMonthData){
							String price = dto.getAmount_per_unit_after_taxes();
							month_name = dto.getMonth_name();
							priceList.add(price);
						}
						transposeOfPrices.add(priceList);
						secondRowOfMonths.add(month_name);
						colspan++;
					}
				}
				if(colspan!=0){
					firstRowOfsites.add(siteName+"@@"+colspan);
				}
				
				
			}//end of - for loop (site - wise)
			transposePrices(lastRowsOfPrices,transposeOfPrices);
			firstRowOfsitesMap.put(tool_tip_id,firstRowOfsites);
			secondRowOfMonthsMap.put(tool_tip_id,secondRowOfMonths);
			lastRowsOfPricesMap.put(tool_tip_id,lastRowsOfPrices);
			
			for(Map<String, Object> prods : objproducts){
				String CPID = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				if(childProductId.equals(CPID)){
					prods.put("TOOL_TIP_ID", tool_tip_id);
				}

			}
		}//end of - for loop (product wise)
		
		request.setAttribute("firstRowOfsitesMap", firstRowOfsitesMap);
		request.setAttribute("secondRowOfMonthsMap", secondRowOfMonthsMap);
		request.setAttribute("lastRowsOfPricesMap", lastRowsOfPricesMap);
		request.setAttribute("noOfProducts",tool_tip_id);
		
		return objproducts;
	}


	private Map<String,String> getPricesInPresentPO(HttpServletRequest request) {
		Map<String,String> map = new HashMap<String,String>();
		String poType = request.getParameter("poType");
		String poNumber = request.getParameter("poNumber");
		DecimalFormat df = new DecimalFormat("#.##"); 
		if(StringUtils.isBlank(poType)){
			//view comparison on indent
			return map;
		}
		else{
			if(poType.equals("temporary")){
				//view comparison on temp po
				List<Map<String, Object>> objProdDtls = objPurchaseDepartmentIndentProcessDao.getPricesOfTempPoProducts(poNumber);
				for(Map<String, Object> prod : objProdDtls){
					String childProductId = prod.get("CHILD_PRODUCT_ID")==null ? "" :   prod.get("CHILD_PRODUCT_ID").toString();
					double amountAfterTax = Double.valueOf(prod.get("AMOUNT_AFTER_TAX")==null ? "0" :   prod.get("AMOUNT_AFTER_TAX").toString());
					double qty = Double.valueOf(prod.get("PO_QTY")==null ? "1" :   prod.get("PO_QTY").toString());
					map.put(childProductId, df.format(amountAfterTax/qty));					
				}
				return map;
			}
			else if(poType.equals("permanent")){
				//view comparison on po
				List<Map<String, Object>> objProdDtls = objPurchaseDepartmentIndentProcessDao.getPricesOfPermanentPoProducts(poNumber);
				for(Map<String, Object> prod : objProdDtls){
					String childProductId = prod.get("CHILD_PRODUCT_ID")==null ? "" :   prod.get("CHILD_PRODUCT_ID").toString();
					double amountAfterTax = Double.valueOf(prod.get("AMOUNT_AFTER_TAX")==null ? "0" :   prod.get("AMOUNT_AFTER_TAX").toString());
					double qty = Double.valueOf(prod.get("PO_QTY")==null ? "1" :   prod.get("PO_QTY").toString());
					map.put(childProductId, df.format(amountAfterTax/qty));					
				}
				return map;
			}
			else{
				return map;
			}
		}
		
		
	}


	private void transposePrices(List<List<String>> lastRowsOfPrices, List<List<String>> transposeOfPrices) {
		int maxListSize = 0;
		for(List<String> list : transposeOfPrices){
			int list_size = list.size();
			if(list_size>maxListSize){
				maxListSize = list_size;
			}
		}
		for(List<String> list : transposeOfPrices){
			int sizeNeedToBeAdded = maxListSize-list.size();
			for(int i=1;i<=sizeNeedToBeAdded;i++){
				list.add("");
			}
		}
		for(int i=0;i<maxListSize;i++){
			List<String> list1 = new ArrayList<String>();
			for(List<String> list : transposeOfPrices){
				list1.add(list.get(i));
			}
			lastRowsOfPrices.add(list1);
		}
		
	}


	public List<Map<String, Object>> initializeZeroPriceForVendorNotGivenProducts(List<Map<String, Object>>  objproducts){
		List<String> allChildProducts = new ArrayList<String>();
		for(Map<String, Object> prods : objproducts){
			if(prods.get("CHILD_PRODUCT_ID")!=null){
				String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				if(!allChildProducts.contains(childProductId)){
					allChildProducts.add(childProductId);
				}
			}
		}
		List<String> allVendors = new ArrayList<String>();
		for(Map<String, Object> prods : objproducts){
			if(prods.get("VENDOR_NAME")!=null){
				String vendorName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
				if(!allVendors.contains(vendorName)){
					allVendors.add(vendorName);
				}
			}
		}
		List<String> combinationOfProductsAndVendors = new ArrayList<String>();
		for(Map<String, Object> prods : objproducts){
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String vendorName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
			combinationOfProductsAndVendors.add(childProductId+"@@"+vendorName);
		}
		
		for(String childProductId : allChildProducts){
			Map<String, Object> mapToCopy = new HashMap<String, Object>();
			for(Map<String, Object> prods : objproducts){
				if(prods.get("CHILD_PRODUCT_ID")!=null){
					String CPID = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
					if(childProductId.equals(CPID)){
						mapToCopy.putAll(prods);
						break;
					}
				}
			}
			for(String vendorName : allVendors){
				if(!combinationOfProductsAndVendors.contains(childProductId+"@@"+vendorName)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.putAll(mapToCopy);
					map.put("VENDOR_NAME",vendorName);
					map.put("VENDOR_MENTIONED_QTY","0");
					map.put("PRICE","0");
					map.put("BASIC_AMOUNT","0");
					map.put("DISCOUNT","0");
					map.put("AMOUNT_AFTER_DISCOUNT","0");
					map.put("TAX","0");
					map.put("TAX_AMOUNT","0");
					map.put("AMOUNT_AFTER_TAX","0");
					map.put("TOTAL_AMOUNT","0");
					map.put("CHILDPROD_CUST_DESC","");
					objproducts.add(map);
				}

			}
		}
		
		return objproducts;
	}
	public List<Map<String, Object>> getLeastPriceVendorForEachProduct(List<Map<String, Object>>  objproducts, HttpServletRequest request){

		for(Map<String, Object> prods : objproducts){
			prods.put("LEAST_PRICE_VENDOR", "");
			prods.put("LEAST_DISC_RATE_PER_UNIT", "");
			prods.put("LEAST_AMOUNT_AFTER_DISCOUNT", "");
			prods.put("TOOL_TIP_ID", "");
		}
		List<String> allChildProducts = new ArrayList<String>();
		for(Map<String, Object> prods : objproducts){
			if(prods.get("CHILD_PRODUCT_ID")!=null){
				String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				if(!allChildProducts.contains(childProductId)){
					allChildProducts.add(childProductId);
				}
			}
		}
		Map<String,String> leastPriceVendorForEachProduct = new HashMap<String,String>();
		Map<String,String> leastDiscRatePerUnitForEachProduct = new HashMap<String,String>();
		Map<String,String> leastAmountAfterDiscountForEachProduct = new HashMap<String,String>();
		Map<String,String> leastTaxAmountForEachProduct = new HashMap<String,String>();
		Map<String,String> leastAmountAfterTaxForEachProduct = new HashMap<String,String>();
		for(String childProductId : allChildProducts){
			leastPriceVendorForEachProduct.put(childProductId, "");
			leastDiscRatePerUnitForEachProduct.put(childProductId, "0");
			leastAmountAfterDiscountForEachProduct.put(childProductId, "0");
			leastTaxAmountForEachProduct.put(childProductId, "0");
			leastAmountAfterTaxForEachProduct.put(childProductId, "0");
		}
		for(String childProductId : allChildProducts){
			double childProductLowCost = 1000000000;
			for(Map<String, Object> prods : objproducts){
				String CPID = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				if(childProductId.equals(CPID)){
					String vendorName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
					double amountAfterTax = Double.valueOf(prods.get("AMOUNT_AFTER_TAX")==null ? "0" :   prods.get("AMOUNT_AFTER_TAX").toString());
					double amountAfterDiscount = Double.valueOf(prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "0" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString());
					double indentQuantity = Double.valueOf(prods.get("PURCHASE_DEPT_REQ_QUANTITY")==null ? "0" :   prods.get("PURCHASE_DEPT_REQ_QUANTITY").toString());
					double taxAmount = Double.valueOf(prods.get("TAX_AMOUNT")==null ? "0" :   prods.get("TAX_AMOUNT").toString());
					
					if(amountAfterTax!=0&&amountAfterTax<childProductLowCost){
						childProductLowCost = amountAfterTax;
						leastPriceVendorForEachProduct.put(childProductId, vendorName);
						leastDiscRatePerUnitForEachProduct.put(childProductId, String.valueOf(amountAfterDiscount/indentQuantity));
						leastAmountAfterDiscountForEachProduct.put(childProductId, String.valueOf(amountAfterDiscount));
						leastTaxAmountForEachProduct.put(childProductId, String.valueOf(taxAmount));
						leastAmountAfterTaxForEachProduct.put(childProductId, String.valueOf(amountAfterTax));
						
					}
					else if(amountAfterTax!=0&&amountAfterTax==childProductLowCost){
						leastPriceVendorForEachProduct.put(childProductId, leastPriceVendorForEachProduct.get(childProductId)+", "+vendorName);
					}
				}
			}
		}
		System.out.println(leastPriceVendorForEachProduct);
		for(Map<String, Object> prods : objproducts){
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String vendorName = leastPriceVendorForEachProduct.get(childProductId);
			String discRatePerUnit = leastDiscRatePerUnitForEachProduct.get(childProductId);
			String amountAfterDiscount = leastAmountAfterDiscountForEachProduct.get(childProductId);
			prods.put("LEAST_PRICE_VENDOR", vendorName);
			prods.put("LEAST_DISC_RATE_PER_UNIT", discRatePerUnit);
			prods.put("LEAST_AMOUNT_AFTER_DISCOUNT", amountAfterDiscount);
		}
		double LeastAmountAfterDiscountForAllProducts = 0;
		double LeastGSTAmountForAllProducts=0;
		double LeastTotalAmountForAllProducts=0;
		for(String childProductId : allChildProducts){
			double amountAfterDiscount = Double.valueOf(leastAmountAfterDiscountForEachProduct.get(childProductId));
			double taxAmount = Double.valueOf(leastTaxAmountForEachProduct.get(childProductId));
			double amountAfterTax = Double.valueOf(leastAmountAfterTaxForEachProduct.get(childProductId));
			LeastAmountAfterDiscountForAllProducts+=amountAfterDiscount;
			LeastGSTAmountForAllProducts+=taxAmount;
			LeastTotalAmountForAllProducts+=amountAfterTax;
		}
		request.setAttribute("LeastAmountAfterDiscountForAllProducts", String.valueOf(LeastAmountAfterDiscountForAllProducts));
		request.setAttribute("LeastGSTAmountForAllProducts", String.valueOf(LeastGSTAmountForAllProducts));
		request.setAttribute("LeastTotalAmountForAllProducts", String.valueOf(LeastTotalAmountForAllProducts));
		
		return objproducts;
	}
	public List<Map<String, Object>> equalizeQuantityInDifferentVendorsForSameProduct(List<Map<String, Object>>  objproducts){

		Map<String,String> childProductIdAndIndentQuantityMap = new HashMap<String,String>();
		for(Map<String, Object> prods : objproducts){
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String strQuantity = prods.get("PURCHASE_DEPT_REQ_QUANTITY")==null ? "" :   prods.get("PURCHASE_DEPT_REQ_QUANTITY").toString();
			if(!childProductIdAndIndentQuantityMap.containsKey(childProductId)){
				childProductIdAndIndentQuantityMap.put(childProductId, strQuantity);
			}
		}
		//
		for(int i=0;i<objproducts.size();i++){
			Map<String, Object> prods = objproducts.get(i);
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String vendorQuantity = prods.get("VENDOR_MENTIONED_QTY")==null ? "" :   prods.get("VENDOR_MENTIONED_QTY").toString();
			String indentQuantity = childProductIdAndIndentQuantityMap.get(childProductId);
			prods.put("INDENT_QTY",indentQuantity);
			//doCalculation
			if(!vendorQuantity.equals(indentQuantity)){
				String basicAmount = prods.get("BASIC_AMOUNT")==null ? "0" :   prods.get("BASIC_AMOUNT").toString();
				String amountAfterDiscount = prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "0" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString();
				String taxAmount = prods.get("TAX_AMOUNT")==null ? "0" :   prods.get("TAX_AMOUNT").toString();
				String amountAfterTax = prods.get("AMOUNT_AFTER_TAX")==null ? "0" :   prods.get("AMOUNT_AFTER_TAX").toString();
				String totalAmount = prods.get("TOTAL_AMOUNT")==null ? "0" :   prods.get("TOTAL_AMOUNT").toString();

				prods.put("VENDOR_MENTIONED_QTY", indentQuantity);
				prods.put("BASIC_AMOUNT", doCalculation(basicAmount,indentQuantity,vendorQuantity));
				prods.put("AMOUNT_AFTER_DISCOUNT", doCalculation(amountAfterDiscount,indentQuantity,vendorQuantity));
				prods.put("TAX_AMOUNT", doCalculation(taxAmount,indentQuantity,vendorQuantity));
				prods.put("AMOUNT_AFTER_TAX", doCalculation(amountAfterTax,indentQuantity,vendorQuantity));
				prods.put("TOTAL_AMOUNT", doCalculation(totalAmount,indentQuantity,vendorQuantity));
			}

		}
		return objproducts;
	}

	@Override
	public List<IndentCreationBean> getAllSitePurchaseIndents(String siteId) {
		String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		return cntlIndentrocss.getSpecificSiteIndentFromAndToDetails(purchaseDeptId,siteId);
	}

	@Override
	public List<ProductDetails> getTermsAndConditions(String site_id) {
		String state = objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(site_id);
		state = state.toUpperCase();
		//	String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		List<ProductDetails> listTermsAndcondtion = new ArrayList<ProductDetails>();
		ProductDetails objProductDetails = null;

		int count =0;
		while(true) {
			objProductDetails = new ProductDetails();
			count = count+1;

			String condition = validateParams.getProperty(site_id+"_"+state+"_TC"+count);
			if(condition==null){break;}
			else{
				objProductDetails.setStrTermsConditionName(condition);
				objProductDetails.setStrIndentId(String.valueOf(count));
				listTermsAndcondtion.add(objProductDetails); 
			}

		}
		return listTermsAndcondtion;
	}
	@Override
	public String getDefaultCCEmails(String site_id) {
		String state = objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(site_id);
		state = state.toUpperCase();

		String strCCEmails="";
		int count =0;
		while(true) {

			count = count+1;

			String ccEmail = validateParams.getProperty(site_id+"_"+state+"_CCEMAIL"+count);
			if(ccEmail==null){break;}
			else{
				strCCEmails =strCCEmails + ccEmail + ",";
			}

		}
		if(!strCCEmails.equals("")){strCCEmails = strCCEmails.substring(0,strCCEmails.length()-1);}
		return strCCEmails;
	}	

	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber,String AllOrNot){
		List<IndentCreationBean> list = null;
		try{
			list = objPurchaseDepartmentIndentProcessDao.ViewPoPendingforApproval( fromDate,  toDate,  strUserId,tempPoNumber,AllOrNot);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

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
		String poLevelComments="";
		//String strIndentNumber = "";
		try {
			//strIndentNumber = request.getParameter("indentNumber") == null ? "" : request.getParameter("indentNumber").toString();
			tblOneData+= objPurchaseDepartmentIndentProcessDao.getPendingVendorDetails(poNumber, siteId,request,siteName);
			deliverySiteState=request.getAttribute("deliverySiteState").toString();
			tbltwoData+= objPurchaseDepartmentIndentProcessDao.getPendingProductDetails(poNumber,siteId,request,deliverySiteState);
			gstinumber=request.getAttribute("gstinumber").toString();
			//terms=request.getAttribute("terms").toString();
			tblCharges+=objPurchaseDepartmentIndentProcessDao.getPendingTransportChargesList(poNumber,siteId,gstinumber,request,deliverySiteState);
			request.setAttribute("receiverState",objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(siteId));
			request.setAttribute("PoLevelComments",objPurchaseDepartmentIndentProcessDao.getPoLevelComments(poNumber,"false"));
			objviewPOPageDataMap.put("AddressDetails", tblOneData);
			request.setAttribute("siteNameForPriceMaster", siteName);
			objviewPOPageDataMap.put("productDetails", tbltwoData);
			objviewPOPageDataMap.put("TransportDetails", tblCharges);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
		}catch (Exception e) {
			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");

			result = "Failed";
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();

			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");

			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String SavePoApproveDetails(final String tempPONumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPO) {

		
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="failed";

		 //indentnumber="";
		String permPoNumber="";
		int val=0;
		String approvalEmpId = "";
		String state="";
		String sessionSite_id="";
		int intPoEntrySeqId =0;
		String revision_No="";
		String old_Po_Number="";
		 //siteLevelPoPreparedBy="";
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
		String siteLevelPoPreparedBy=request.getParameter("siteLevelPoPreparedBy") == null ? "-" : request.getParameter("siteLevelPoPreparedBy").toString();
		//String strIndentNo=request.getParameter("strIndentNo") == null ? "-" : request.getParameter("strIndentNo").toString();
		String indentnumber=request.getParameter("strIndentNo") == null ? "" : request.getParameter("strIndentNo").toString();
		revision_No=request.getParameter("revision_No") == null ? "" : request.getParameter("revision_No").toString();	
		old_Po_Number=request.getParameter("oldPoNumber") == null ? "-" : request.getParameter("oldPoNumber").toString();//to get revised po number
		String mailPasswd=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
		String deliveryDate=request.getParameter("deliveryDate") == null ? "" : request.getParameter("deliveryDate").toString();
		String remarks=request.getParameter("note") == null ? "": request.getParameter("note").toString();
		
		String vendorId=request.getParameter("vendorId") == null ? "": request.getParameter("vendorId").toString();
		String oldPODate=request.getParameter("oldPODate") == null ? "": request.getParameter("oldPODate").toString();
		String oldPOEntryId=request.getParameter("oldPOEntryId") == null ? "": request.getParameter("oldPOEntryId").toString();
		//String dbPasswd=getTempPOPassword(tempPONumber);
		String checkval="";
		String revisedOrNot="";
		String strState="";
		String acc_Note_Email="";
		int LocalPort = request.getLocalPort();
		boolean paymentStatus=false;
		double paidAmount=0.0;
		request.setAttribute("oldPODate",oldPODate);
		// getting approval empid for acropolis to check below 1lakh or above 1lakh
		String tempApprovalEmpId=validateParams.getProperty("ACROPOLIS_EMP_ID") == null ? "" : validateParams.getProperty("ACROPOLIS_EMP_ID").toString();
		String tempApprovalEmpIdtoDevelopment=validateParams.getProperty("DEVELOPMENT_EMP_ID") == null ? "" : validateParams.getProperty("DEVELOPMENT_EMP_ID").toString();
		// this is for check and take path  pdf or images move from one folder to another folder-->move file path
		// root path used to save images or pdf in that path
		if(LocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
			rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();}

		paymentStatus=objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(old_Po_Number,siteId,vendorId,oldPODate,siteName,oldPOEntryId,tempGetLocalPort,false,true);
		if(paymentStatus){
			request.setAttribute("message1","Sorry ! Unable to Revise PO because payment had been initiated.");
			//transactionManager.rollback(status);
			//WriteTrHistory.write("Tr_Completed");
			return result="response";
		}
		
		request.setAttribute("indentnumber",indentnumber);// this is using in the compare purpose to quantity in revised po in getAndsavePendingProductDetails

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_save approval POA, ");

		try{
			
			String strCheckTotal=objPurchaseDepartmentIndentProcessDao.getTempPoTotalAmt(userId,Integer.parseInt(tempPONumber));
			if(Double.parseDouble(strCheckTotal)<=100000  && !tempApprovalEmpId.equals(userId) && userId.equals("5033"))// this is foir acropolis requirement
			//if(Double.parseDouble(strCheckTotal)<=100000 && siteId.equals("112") && !tempApprovalEmpId.equals(userId)  && userId.equals("5033"))// this is for acropolis requirement to check below or above 1 lakh
			{approvalEmpId=tempApprovalEmpId;}
			else if(Double.parseDouble(strCheckTotal)<=100000  && !tempApprovalEmpIdtoDevelopment.equals(userId) && userId.equals("BPUR2.1"))// this is for acropolis requirement to check below or above 1 lakh
			{approvalEmpId=tempApprovalEmpId;}
			else{	
			approvalEmpId = objPurchaseDepartmentIndentProcessDao.getTemproryuser(userId);}
		
			 checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(tempPONumber); // get value of active or inactive
			 String cancel=objPurchaseDepartmentIndentProcessDao.getCancelOrNot(tempPONumber);// whether po is cancel or not 
		
			 
			 HttpSession session = request.getSession(true);
			 strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			 //strUserId=request.getParameter("userId");
			 if(!cancel.equalsIgnoreCase("CANCEL") && checkval.equalsIgnoreCase("A") && !approvalEmpId.equals("VND")){ // THIS IS FOR MAIL PURPOSE 
			
				//user Approve from mail it is check condition session useridnot taken so take from request object				
			if((strUserId==null || strUserId.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){strUserId=request.getParameter("userId");}
				
			if(approvalEmpId!=null && !approvalEmpId.equals("") && !approvalEmpId.equals("VND")){
				WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber);
				
				val=objPurchaseDepartmentIndentProcessDao.updateTempPoEntry(approvalEmpId,tempPONumber,ccmailId,siteLevelPoPreparedBy,indentnumber,passwdForMail,deliveryDate);
				// if user can click on cancel (or) modify temp po button this condition executed 
				if(isCancelTempPO.equals("true")){

					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,"0",strUserId, siteId, "E&S",remarks);

				}else{

					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,"0",strUserId, siteId, "A",remarks);}
				/*	*******************************************ForMail*******************************************************/
				// if user click on approve button then send mail also to next approve Person
					List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(tempPONumber,approvalEmpId);
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
					// send mail to next approval person
					approveToEmailAddress = new String[toMailListArrayList.size()];
					toMailListArrayList.toArray(approveToEmailAddress);
				
					Object ApproveData[]={indentnumber,approvalEmpId,siteId,revision_No,old_Po_Number,listOfDetails.get(4),tempPONumber,listOfDetails.get(3),
							listOfDetails.get(6),listOfDetails.get(2),listOfDetails.get(5),listOfDetails.get(0),siteLevelPoPreparedBy,passwdForMail,String.valueOf(tempGetLocalPort),vendorId,oldPOEntryId,oldPODate};
				
					EmailFunction objEmailFunction = new EmailFunction();
					objEmailFunction.sendMailForTempApprove(strApproveName,approveToEmailAddress,tempGetLocalPort,ApproveData);
			
					/**************************************************For Mail End *******************************************************************/

				// display message after approve button clicked
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
			else{ // if approval from mail then it will check condition userId not getting from session so take from request object
				if(strUserId.equals("")){strUserId=request.getParameter("userId");} // from mail userId empty then take from request object

				if(checkval.equals("A")){
					request.setAttribute("userId",userId);// for get product details to show indent creaation details id
					objPurchaseDepartmentIndentProcessDao.getAndsaveVendorDetails(tempPONumber, siteId,userId,request,revision_No,old_Po_Number,siteLevelPoPreparedBy,siteName,deliveryDate);
					//request.getAttribute("tempPoNumber").toString();
					request.setAttribute("siteLevelPoPreparedBy",siteLevelPoPreparedBy); // this is used in getandsave vendor details page
					permPoNumber=request.getAttribute("permentPoNumber").toString();
					WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber+" , PermanentPONumber:"+permPoNumber);

				//state=request.getAttribute("State").toString();
				intPoEntrySeqId = Integer.parseInt(request.getAttribute("poEntrySeqID") == null ? "0" : request.getAttribute("poEntrySeqID").toString());
				objPurchaseDepartmentIndentProcessDao.gettermsconditions(tempPONumber,String.valueOf(intPoEntrySeqId));
				//Conclusions author - Rafi
				List<String> conclusions = objPurchaseDepartmentIndentProcessDao.getTempPoConclusions(tempPONumber);
				objPurchaseDepartmentIndentProcessDao.insertPoConclusions(conclusions,String.valueOf(intPoEntrySeqId),tempPONumber,indentnumber);
				//Conclusions - End
				objPurchaseDepartmentIndentProcessDao.getAndsavePendingProductDetails(tempPONumber,siteId,request,permPoNumber,intPoEntrySeqId,old_Po_Number);
				
				objPurchaseDepartmentIndentProcessDao.getAndsavePendingTransportChargesList(tempPONumber,siteId,request,intPoEntrySeqId);
				result="success";
				request.setAttribute("result", "Temp Po approved Successfully and PO Number is : "+permPoNumber);
				request.setAttribute("viewPoPageData", objviewPOPageDataMap);
				revisedOrNot=request.getAttribute("RevisedOrNot").toString();// this is for pdf purpose to revised or not
				 strState=request.getAttribute("state").toString();
				if(result.equals("success")){

					String filepath=rootFilePath+"TEMP_PO//";
					//String strPO_Number=poNumber.replace('/','$');
					for (int i = 0; i < 8; i++) {
					File file = new File(filepath+tempPONumber+"_Part"+i+".pdf");	
					File img = new File(filepath+tempPONumber+"_Part"+i+".jpg");
					//File file = new File(filepath+tempPONumber+".pdf");
					long count=file.length();
					if(file.exists()){
					String strpermPoNumber=permPoNumber.replace('/','$');
					Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".pdf")); //this is for pdf purpose to move temp po to pemenent po
					}
					if(img.exists()){
						String strpermPoNumber=permPoNumber.replace('/','$');
						Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".jpg")); //this is for pdf purpose to move temp po to pemenent po
						}
					
					}
					sessionSite_id=request.getAttribute("sessionSite_id").toString(); // temp po entry details inactive after do this 

					//String vendorId = "";
					String totalAmt=request.getAttribute("totalAmt").toString(); // use this to update in acc dept table so take this
					vendorId = objPurchaseDepartmentIndentProcessDao.getVendorDetails(tempPONumber, siteId,userId,request);
				if(old_Po_Number!=null && !old_Po_Number.equals("-") && !old_Po_Number.equals("")){
					//below statment: revised po to update in account dept table,indent entry,dc_entry tables 
					
/*********************************************revised po done at the time acc dept send mail*******************************************/
					paidAmount =objPurchaseDepartmentIndentProcessDao.updateAdvancePaidAmount(request,old_Po_Number,siteId,vendorId,Double.valueOf(totalAmt));
					objPurchaseDepartmentIndentProcessDao.updateAccPayment(old_Po_Number,permPoNumber,String.valueOf(totalAmt),false);
					if(paidAmount>Double.valueOf(totalAmt)){ // paid amount is more than the total amount then check condition and  trigger the mail
						 allEmployeeEmailsOfAcc=(List<String>) request.getAttribute("emailList");
						 acc_Note_Email=" For the difference in amount Paid and amount in Revised PO, Our Purchase Team will contact you.";
					
					}
					
					/*int intResult=0;
					
					String resp =inactivePendingApprovalsInAccDeptIfPoAmountDecreased(old_Po_Number,siteId,vendorId,Double.valueOf(totalAmt),allEmployeeEmailsOfAcc,strState);
					if(!resp.equals("NoData")){ // to check acc dept table data in when the amount below that decreased along with note laso send it
						intResult=objPurchaseDepartmentIndentProcessDao.updateAccPayment(old_Po_Number,permPoNumber,totalAmt,false);
						if(resp.equals("Success")){ // extra added to mail any changes done in payment tables comment goes to along with mail
							acc_Note_Email="<b>Note</b>:The Payment Initated for Old PO:" +old_Po_Number+" is Deleted.Please Reinitate Payment for Revised PO: "+permPoNumber+".";
						}
					}*/
					// update dc or indent entry table 
					objPurchaseDepartmentIndentProcessDao.updateIndentAndDcPONumber(old_Po_Number,permPoNumber,totalAmt);
				}
					
				/*********************************************revised po done at the time acc dept send mail*******************************************/		
					objPurchaseDepartmentIndentProcessDao.updateTotalAmt(totalAmt,approvalEmpId,permPoNumber); // after revising po or normal po amount  it will updated to table
					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,String.valueOf(intPoEntrySeqId),strUserId, siteId, "A",remarks);
					// inactive temp po entry details table
					int i=objPurchaseDepartmentIndentProcessDao.updatepoEntrydetails(tempPONumber,indentnumber,siteId,userId,sessionSite_id,siteLevelPoPreparedBy,passwdForMail,deliveryDate);

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
					ExecutorService executorService = Executors.newFixedThreadPool(10);
					executorService.execute(new Runnable() {
						public void run() {
							sendEmailForPo( "", 0, finalSiteId, "",vd,finalPermPoNumber,ccEmailTo,subject,poCreatedEmpName,getLocalPort,strRevisedOrNot,
									strOldPoNumber,strPoDate,allEmployeeEmailsOfAcc1,RevsiedpoNote);
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

	public String RejectPoDetails(HttpSession session ,HttpServletRequest request) {

		String user_id ="";
		String ponumber = "";
		String response = "";
		String indentNumber="";
		String quantity="";
		String site_id ="";
		String indentCreationdtlsId="";
		int intTotalNoOfRecords = 0;
		String old_po_NumberStatus="";
		try {

			final int getLocalPort = request.getLocalPort();
			String checkStatus=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(request.getParameter("strPONumber"));
			intTotalNoOfRecords = Integer.parseInt(request.getParameter("totalNoOfRecords") == null ? "0" : request.getParameter("totalNoOfRecords").toString());
			String mailComments=request.getParameter("Remarks")==null ? "" : request.getParameter("Remarks");
			String passwdForMail=request.getParameter("password")== null ? "0" : request.getParameter("password").toString();
			ponumber=request.getParameter("strPONumber");
			old_po_NumberStatus=objPurchaseDepartmentIndentProcessDao.activeOldPOTable(ponumber); // old po quantity take back to table so take this
			if(intTotalNoOfRecords > 0 && checkStatus.equals("A") && !old_po_NumberStatus.equalsIgnoreCase("success")){
				for(int i =0; i<=intTotalNoOfRecords-1;i++){

					indentNumber=request.getParameter("strIndentNo");
					quantity=request.getParameter("quantity"+i);
					
					indentCreationdtlsId=request.getParameter("indentCreationdtlsId"+i);
					//	session = request.getSession(true);
					site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
					user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
					
					// po quantity back to purchase_indent_proces table so use this condition
					if(!indentCreationdtlsId.equals("-")){
					objPurchaseDepartmentIndentProcessDao.updatePurchaseDeptIndentProcestbl(indentNumber,quantity,ponumber,indentCreationdtlsId,false);}

				}

				objPurchaseDepartmentIndentProcessDao.updateTablesOnTempPORejection(indentNumber, ponumber, indentCreationdtlsId);
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);
				
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Temp Po Has been Rejected";
				sendTempPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Rejected",getLocalPort); // send mail to revious person whom approval it
	
				response = "Success";
			}else if(old_po_NumberStatus.equalsIgnoreCase("success")){response = "Success";// revised po was rejected so it will active
			String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);// to send the mail for revised po rejected time
			
			List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
			listOfDetails.add(String.valueOf(request.getLocalPort()));
			String subject="Your Temp Po Has been Rejected";
			sendTempPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Rejected",getLocalPort);}
			
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




	public List<ProductDetails> getQuatationProductDetails(HttpServletRequest request,HttpSession session){

		List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();

		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno=0;

		for(int num=1;num<=noofrows;num++){
			if(request.getParameter("checkboxname"+num)!=null)
			{
				++sno;
				String product = request.getParameter("product"+num);
				String subProduct = request.getParameter("subProduct"+num);
				String childProduct = request.getParameter("childProduct"+num);
				String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
				String requiredQuantity = request.getParameter("requiredQuantity"+num);
				String pendingQuantity = request.getParameter("pendingQuantity"+num);
				String poIntiatedQuantity = request.getParameter("poIntiatedQuantity"+num);

				//	strChildProductList = strChildProductList+"'"+childProductId+"',";
				ProductDetails productDetails = new ProductDetails();

				productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setProductName(product);
				productDetails.setSub_ProductName(subProduct);
				productDetails.setChild_ProductName(childProduct);
				productDetails.setMeasurementName(unitsOfMeasurement);
				//	productDetails.setQuantity(strPending);

				productDetails.setPendingQuantity(pendingQuantity);
				productDetails.setRequiredQuantity(requiredQuantity);
				productDetails.setPoIntiatedQuantity(poIntiatedQuantity);




				listProductDetails.add(productDetails);
			}
		}

		return listProductDetails;

	}

	public List<ProductDetails> getQuatationDetails(HttpServletRequest request){
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();

		List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();
		String strTableOneData="";
		String termsconditions="";
		String VendorName=request.getParameter("vendorName1");
		String vendorId=request.getParameter("vendorId1");
		String strGSTINNumber=request.getParameter("strGSTINNumber1");
		String vendorAddress=request.getParameter("vendorAddress1");
		String indentNo = request.getParameter("indentNumber");
		String siteWiseIndentNo = request.getParameter("siteWiseIndentNo");
		String SiteName=request.getParameter("siteName");
		String siteId=request.getParameter("siteId");
		int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));


		ProductDetails productDetails = new ProductDetails();
		productDetails.setVendorName(VendorName);
		productDetails.setStrGSTINNumber(strGSTINNumber);
		productDetails.setVendorAddress(vendorAddress);
		productDetails.setSiteName(SiteName);
		productDetails.setStrIndentId(indentNo);
		/*productDetails.setSiteWiseIndentNo(siteWiseIndentNo);*/

		listProductDetails.add(productDetails);


		strTableOneData +=indentNo+"@@"+new java.sql.Date(System.currentTimeMillis())+"@@"+VendorName+"@@"+vendorAddress+"@@"+strGSTINNumber+"@@"+siteWiseIndentNo+"@@"+SiteName;

		//	 objviewPOPageDataMap.put("AddressDetails",strTableOneData);

		List<String> listOfTermsAndConditions=objPurchaseDepartmentIndentProcessDao.getVendortermsconditions(indentNo,vendorId);
		//System.out.println("the value is "+listOfTermsAndConditions);
		request.setAttribute("listOfTermsAndConditions",listOfTermsAndConditions);

		//String state=;
		//System.out.println("the value is"+state);
		request.setAttribute("receiverState",objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(siteId));

		request.setAttribute("AddressDetails",strTableOneData);

		return listProductDetails;
	}

	//check val true for Temp Po
	@Override
	public List<ProductDetails> getProductDetailsLists(String indentNo, String vendorName,HttpServletRequest request,String checkVal) {
		List<ProductDetails> listGetInvoiceDetailsList = null;
		List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();
		int sno=0;
		String productId ="";
		String subProductId ="";
		String childProductId ="";
		String productName ="";
		String subProductName ="";
		String childProductName ="";
		String setMeasurementId ="";
		String setMeasurementName ="";
		String indentCreationDetailsId ="";
		String strQuantity ="";
		String strPOIntiatedQuantity ="";
		String strPurchaseDepatRequstReceivedQuantity ="";
		String groupId=""; // this is for material boq purpose written this one
		String siteId="";
		// these are used to checjk the boq quantity
		String boqQuantity="0";
		String availablequantity="0";

		int noofrows = Integer.parseInt(request.getParameter("numberOfRows") == null ? "0" : request.getParameter("numberOfRows").toString());
		try {

			if(checkVal.equalsIgnoreCase("true")){

				productId = request.getParameter("prodId");
				subProductId = request.getParameter("subProductId");
				childProductId = request.getParameter("childProdId");
				productName = request.getParameter("productName");
				subProductName = request.getParameter("subProductName");
				childProductName = request.getParameter("childProductName");//childProduct
				setMeasurementId = request.getParameter("measurementId");
				setMeasurementName = request.getParameter("measurementName");
				indentCreationDetailsId = request.getParameter("indent_Creation_dtls_Id");
				strQuantity = request.getParameter("pending_Quan");
				strPOIntiatedQuantity = request.getParameter("init_Quan");
				strPurchaseDepatRequstReceivedQuantity = request.getParameter("req_Quan");

				ProductDetails productDetails = new ProductDetails();
				productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setProductId(productId);
				productDetails.setProductName(productName);
				productDetails.setSub_ProductId(subProductId);
				productDetails.setSub_ProductName(subProductName);
				productDetails.setChild_ProductId(childProductId);
				productDetails.setChild_ProductName(childProductName);
				productDetails.setMeasurementId(setMeasurementId);
				productDetails.setMeasurementName(setMeasurementName);
				productDetails.setIndentCreationDetailsId(indentCreationDetailsId);
				productDetails.setQuantity(strQuantity);
				productDetails.setPoIntiatedQuantity(strPOIntiatedQuantity);
				productDetails.setPurchasedeptRequestReceiveQuantity(strPurchaseDepatRequstReceivedQuantity);

				listProductDetails.add(productDetails);




			}else{

				for(int num=1;num<=noofrows;num++){
					if(request.getParameter("checkboxname"+num)!=null)
					{
						sno++;
						productId = request.getParameter("productId"+num);
						subProductId = request.getParameter("subProductId"+num);
						childProductId = request.getParameter("childProductId"+num);
						productName = request.getParameter("product"+num);
						subProductName = request.getParameter("subProduct"+num);
						childProductName = request.getParameter("childProductPuchaseDeptDisc"+num);//childProduct
						setMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
						setMeasurementName = request.getParameter("unitsOfMeasurement"+num);
						indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
						strQuantity = request.getParameter("POPendingQuantity"+num);
						strPOIntiatedQuantity = request.getParameter("poIntiatedQuantity"+num);
						strPurchaseDepatRequstReceivedQuantity = request.getParameter("requestedQuan"+num);
						groupId=request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
						siteId=request.getParameter("siteId")==null ? "" : request.getParameter("siteId");//request.getParameter("siteId");
						if(groupId!=null && !groupId.equals("0") && !groupId.equals("")){
							String data=gettingPOBoqQuantityAjax(groupId,siteId);
							availablequantity=data.split("_")[0];
							boqQuantity=data.split("_")[1];
						}
						ProductDetails productDetails = new ProductDetails();
						productDetails.setStrSerialNumber(String.valueOf(sno));
						productDetails.setProductId(productId);
						productDetails.setProductName(productName);
						productDetails.setSub_ProductId(subProductId);
						productDetails.setSub_ProductName(subProductName);
						productDetails.setChild_ProductId(childProductId);
						productDetails.setChild_ProductName(childProductName);
						productDetails.setMeasurementId(setMeasurementId);
						productDetails.setMeasurementName(setMeasurementName);
						productDetails.setIndentCreationDetailsId(indentCreationDetailsId);
						productDetails.setQuantity(strQuantity);
						productDetails.setPoIntiatedQuantity(strPOIntiatedQuantity);
						productDetails.setPurchasedeptRequestReceiveQuantity(strPurchaseDepatRequstReceivedQuantity);
						productDetails.setGroupId(groupId);
						productDetails.setBoqQuantity(boqQuantity);
						productDetails.setAvailableQuantity(availablequantity);
						//productDetails.set

						listProductDetails.add(productDetails);
					}
				}
			}//else
			listGetInvoiceDetailsList = objPurchaseDepartmentIndentProcessDao.getProductDetailsLists(indentNo,vendorName,listProductDetails,request);

			//System.out.println("the list of details"+listGetInvoiceDetailsList);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listGetInvoiceDetailsList;
	}	

	@Override
	public String closeIndent(Model model, HttpServletRequest request, int site_id, String user_id,String siteId/*created site Id*/) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_cloInd, ");
		String response = "";
		int result1=0;
		int sno=0;
		int result3=0;
		String indentCreationDetailsId ="";
		String responseMessage = "";
		String quantity="";
		try {
			String indentNumber= request.getParameter("indentNumber");
			String siteWiseIndentNo=request.getParameter("siteWiseIndentNo");
			String typeOfPurchase = request.getParameter("typeOfPurchase");
			if(typeOfPurchase.equalsIgnoreCase("others")){

				typeOfPurchase=request.getParameter("typeOfPurchaseOthers");
			}
			//String type=request.getParameter("typeOfPurchaseOthers");
			int noOfRows=Integer.parseInt(request.getParameter("numberOfRows"));

			for(int num=1;num<=noOfRows;num++){
				if(request.getParameter("checkboxname"+num)!=null)
				{
					sno++;
					//indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
					//objPurchaseDepartmentIndentProcessDao.productWiseInactiveInPurchaseTable(indentCreationDetailsId);

				}
			}

			// if all products are closed
			if(sno==noOfRows){

				result1 = objPurchaseDepartmentIndentProcessDao.doInactiveInIndentCreation(indentNumber);
				int indentCreationApprovalSeqNum = objPurchaseDepartmentIndentProcessDao.getIndentCreationApprovalSequenceNumber();
				IndentCreationDto indentCreationDto = new IndentCreationDto();
				indentCreationDto.setSiteId(site_id);
				indentCreationDto.setUserId(user_id);
				indentCreationDto.setPurpose(typeOfPurchase);
				int result2 = objPurchaseDepartmentIndentProcessDao.insertIndentCreationApprovalDtls(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
				for(int i=1;i<=sno;i++){

					indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+i);
					quantity=request.getParameter("POPendingQuantity"+i);
					result3 =objPurchaseDepartmentIndentProcessDao.doInactiveInPurchaseTable(indentCreationDetailsId,typeOfPurchase,quantity);

				}


				//result3 = objPurchaseDepartmentIndentProcessDao.doInactiveInPurchaseTable(indentNumber,typeOfPurchase);
				responseMessage = "indent "+siteWiseIndentNo+" closed successfully.";
			}else{// if few products are closed (not all)
				for(int num=1;num<=noOfRows;num++){
					if(request.getParameter("checkboxname"+num)!=null)
					{

						indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
						quantity=request.getParameter("POPendingQuantity"+num);
						result3=objPurchaseDepartmentIndentProcessDao.productWiseInactiveInPurchaseTable(indentCreationDetailsId,typeOfPurchase,quantity);

					}
				}
				responseMessage ="successfully "+sno +" Products  closed in indent number "+siteWiseIndentNo+" ";
			}

			//	System.out.println("Query results: "+result1+","+result2+","+result3);
			if(result1>0 || result3>0){
				response = "Success";
				System.out.println(indentNumber+" Indent Closed");
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
				return responseMessage;
			}
			else{
				responseMessage = "Soory, indent doesnt closed please try again after some time";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return responseMessage;
			}
		} catch (Exception e) {
			response = "Failure";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
			return responseMessage;
		}	

	}	


	@Override
	public String getSiteIdByPONumber(String poNumber) {
		int siteId = objPurchaseDepartmentIndentProcessDao.getSiteIdByPONumber(poNumber);
		return String.valueOf(siteId);
	}	

	public List<IndentCreationBean> ViewTempPo(String fromDate, String toDate,String tempPoNumber){
		List<IndentCreationBean> list = null;
		try{
			list = objPurchaseDepartmentIndentProcessDao.ViewTempPo( fromDate,  toDate,tempPoNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	/*================================================get site Wise temp Po start============================*/
	
	public List<IndentCreationBean> ViewSiteTempPo(String fromDate, String toDate,String tempPoNumber,String siteId){
		List<IndentCreationBean> list = null;
		try{
			list = objPurchaseDepartmentIndentProcessDao.ViewSiteTempPo( fromDate,  toDate,tempPoNumber,siteId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	
	
  /* ====================================================get site Wise Temp Po End=============================*/
	@Override
	public List<ProductDetails> combinedDetailsChildProductWise(List<ProductDetails> indentDetails) {
		//
		List<ProductDetails> requiredIndentDetails = new ArrayList<ProductDetails>();
		int strSerialNumber = 0;
		ProductDetails objProductDetails = null;

		Iterator itr = indentDetails.iterator();

		while (itr.hasNext()) {
			objProductDetails = (ProductDetails) itr.next();

			String childProductinIndentDetails = objProductDetails.getChild_ProductId() == null ? ""	: objProductDetails.getChild_ProductId().toString();
			int pendingQuantityinIndentDetails = Integer.parseInt(objProductDetails.getPendingQuantity() == null ? "" : objProductDetails.getPendingQuantity().toString());
			String indentCreationDetailsIdinIndentDetails = objProductDetails.getIndentCreationDetailsId() == null ? "" : objProductDetails.getIndentCreationDetailsId().toString();

			boolean isAlreadyThere = false;
			for(ProductDetails one_in_requiredIndentDetails : requiredIndentDetails)
			{	String childproductId = one_in_requiredIndentDetails.getChild_ProductId();
			if(childproductId.equals(childProductinIndentDetails))
			{	
				isAlreadyThere = true;
				int pendingQuantity = Integer.parseInt(one_in_requiredIndentDetails.getPendingQuantity());
				one_in_requiredIndentDetails.setPendingQuantity(String.valueOf((pendingQuantity+pendingQuantityinIndentDetails)));
				String productBatchData = one_in_requiredIndentDetails.getProductBatchData();
				if(productBatchData!=null&&!productBatchData.equals(""))
				{
					one_in_requiredIndentDetails.setProductBatchData(productBatchData+"@@"+indentCreationDetailsIdinIndentDetails+"="+String.valueOf((pendingQuantityinIndentDetails)));
				}
				else
				{
					one_in_requiredIndentDetails.setProductBatchData(indentCreationDetailsIdinIndentDetails+"="+String.valueOf((pendingQuantityinIndentDetails)));
				}
			}
			}
			if(!isAlreadyThere)
			{
				ProductDetails pd = objProductDetails; 
				strSerialNumber++;
				pd.setStrSerialNumber(String.valueOf(strSerialNumber));
				pd.setPendingQuantity(String.valueOf(pendingQuantityinIndentDetails));
				pd.setProductBatchData(indentCreationDetailsIdinIndentDetails+"="+String.valueOf(pendingQuantityinIndentDetails));
				requiredIndentDetails.add(pd);
			}
		}

		//
		return requiredIndentDetails;
	}


	@Override
	public List<Map<String, Object>> getListOfActivePOs(String site_id,String poNumber) {

		return objPurchaseDepartmentIndentProcessDao.getListOfActivePOs(site_id,poNumber);
	}
/***********************************************************Update Po******************************************/
	@SuppressWarnings("unused")
	@Override
	public String editAndSaveUpdatePO(Model model, HttpServletRequest request,HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_ed&SPO, ");
		String response = "";
		String productRow="";
		String transportRow="";
		String productAndTransport="";
		boolean isAnyUpdateFailed = false;
		String ccEmailId="";
		String subject="";
		String strTotalAmount="";
		String oldPOEntryId="";
		List<String> listOfTermsAndConditions = new ArrayList<String>();
		List<String> updateOfDetails = new ArrayList<String>();//revised details of po purpose
		String strPODate="";
		String strPONumber="";
		int temprevision_no=0;
		int poEntryId=0;
		String strReceiverName="";
		String strReceiverAddress="";
		String strReceiverMobileNo="";
		String strReceiverGSTIN="";
		String receiverState="";
		String strReceiveSideContactPerson="";
		String strReceiverLandLine="";
		String billingAddress="";
		String strBillingAddressGSTIN="";
		String strBillingCompanyName="";
		String strVendorName="";
		String strVendorAddress="";
		String vendorState="";
		String strVendorGSTIN="";
		String contactPersonName="";
		String strLandLineNo="";
		String strVendorMobilNo="";
		String strEmail="";
		String isSiteLevelPo="";
		String strResponse="";
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		final List<ProductDetails> SuccessDataListToMail = new ArrayList<ProductDetails>();
		List<String> toMailListArrayList = new ArrayList<String>();
		List<String> allEmployeeEmailsOfAcc=new ArrayList<String>();
		boolean ispaymentUpdate=false;
		int portNumber=request.getLocalPort();
		boolean paymentStatus=false;
		double paidAmount=0.0;
		String note="";
		
		try{
			int noofRows = Integer.parseInt(request.getParameter("numberOfRows"));
			String poNumber = request.getParameter("poNo");
			String oldPonumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString();
			//System.out.println("updating poNumber: "+poNumber);
			String toSite = request.getParameter("toSite");
			String indentNumber = request.getParameter("indentNumber"); 
			String vendorId=request.getParameter("vendorId"); // this is changed happen that one take here
			String strVendorId=request.getParameter("strVendorId"); // this is vendorName
			String strDeliveryDate=request.getParameter("deliveryDate")==null ? "" : request.getParameter("deliveryDate");
			subject=request.getParameter("subject") == null ? "" : request.getParameter("subject").toString();
			String version_no=request.getParameter("versionNumber") == null ? "" : request.getParameter("versionNumber").toString();
			String strPoPrintRefdate=request.getParameter("printRefferenceNo") == null ? "" : request.getParameter("printRefferenceNo").toString();
			String refferenceNo=request.getParameter("refferenceNo") == null ? "" : request.getParameter("refferenceNo").toString();
			String preparedBy=request.getParameter("preparedBy") == null ? "" : request.getParameter("preparedBy").toString();
			String ccmails=request.getParameter("ccEmailId") == null ? "" : request.getParameter("ccEmailId").toString();
			String siteName=request.getParameter("SiteName") == null ? "" : request.getParameter("SiteName").toString();
			String mailSiteName=request.getParameter("strSiteName") == null ? "" : request.getParameter("strSiteName").toString(); // mail time use
			String siteWiseIndentNo=request.getParameter("siteWiseIndentNo") == null ? "" : request.getParameter("siteWiseIndentNo").toString();
			String strIndentCreationDate=request.getParameter("strCreationDate") == null ? "" : request.getParameter("strCreationDate").toString();
			oldPOEntryId=request.getParameter("poEntryId") == null ? "" : request.getParameter("poEntryId").toString();
			String payment_Req_Days=request.getParameter("payment_Req_Days") == null ? "" : request.getParameter("payment_Req_Days").toString();
			String oldPODate=request.getParameter("oldPODate") == null ? "" : request.getParameter("oldPODate").toString();
			String finalAmount=request.getParameter("ttlAmntForIncentEntry") == null ? "" : request.getParameter("ttlAmntForIncentEntry").toString();
			String chargesRecordsCount =  request.getParameter("numbeOfChargesRowsToBeProcessed")== null ? "" : request.getParameter("numbeOfChargesRowsToBeProcessed").toString();
			String numOfChargeRec[] = chargesRecordsCount.split("\\|");
			String recordcount = request.getParameter("numbeOfRowsToBeProcessed");
			String recordscount[]=recordcount.split("\\|");
			String indentCreationDetailsId ="";
			String indentCreation="";
			String	strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();	
			String sessionSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String deliveryDate=request.getParameter("deliveryDate") == null ? "" : request.getParameter("deliveryDate").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+poNumber);
			
			// getting the vendor details for displaying and save purpose
			List<Map<String, Object>> listVendorDtls =  objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(strVendorId);
			for(Map<String, Object> prods : listVendorDtls) {
					strVendorName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
					strVendorAddress = prods.get("ADDRESS")==null ? "" :   prods.get("ADDRESS").toString();
					vendorState = prods.get("STATE")==null ? "" :   prods.get("STATE").toString();
					strVendorGSTIN = prods.get("GSIN_NUMBER")==null ? "" :   prods.get("GSIN_NUMBER").toString();
					contactPersonName = prods.get("VENDOR_CON_PER_NAME")==null ? "" :   prods.get("VENDOR_CON_PER_NAME").toString();
					strLandLineNo = prods.get("LANDLINE_NO")==null ? "" :   prods.get("LANDLINE_NO").toString();
					strVendorMobilNo = prods.get("MOBILE_NUMBER")==null ? "" :   prods.get("MOBILE_NUMBER").toString();
					strEmail = prods.get("EMP_EMAIL")==null ? " " :   prods.get("EMP_EMAIL").toString();
				}	
			if(!strVendorMobilNo.equals("")){
					contactPersonName = contactPersonName+" ( "+strVendorMobilNo;
				}if(!strLandLineNo.equals("")){
					contactPersonName = contactPersonName+","+strLandLineNo;
				}
			contactPersonName = contactPersonName +" )";
			ProductDetails productDetails = new ProductDetails();
			paymentStatus=objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(oldPonumber,toSite,strVendorId,oldPODate,mailSiteName,oldPOEntryId,portNumber,true,false);
			//String resp = inactivePendingApprovalsInAccDeptIfPoAmountDecreased(oldPonumber,toSite,strVendorId,Double.valueOf(finalAmount),allEmployeeEmailsOfAcc,receiverState);
			if(paymentStatus){
				model.addAttribute("message1","Sorry ! Unable to update PO, as payment initiated..");
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return strResponse="response";
			}
			String approvalEmpId=objPurchaseDepartmentIndentProcessDao.getCancelPOPendinfEmpId(strUserId,"UPDATE_PO"); // cancel po methode using here
			if(approvalEmpId!=null && approvalEmpId.equals("VND")){
				poEntryId = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNo();
				if(oldPonumber!=null && !oldPonumber.equals("")){
					updateOfDetails=objPurchaseDepartmentIndentProcessDao.getRevisionNumber(oldPonumber);
					
					//oldPOEntryId=updateOfDetails.get(1);
					//strPODate=oldPODate;
					//strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(oldPonumber,"false");
					objPurchaseDepartmentIndentProcessDao.inactiveUpdatePO(oldPonumber);
					String tempPoNumber=oldPonumber;
					if(tempPoNumber.contains("/U")){
						String data=tempPoNumber.split("/U")[1];
						if(data.contains("/R")){
							String data1=data.split("/R")[0];
							temprevision_no=Integer.valueOf(data1)+1;
							strPONumber=tempPoNumber.replace("U"+Integer.valueOf(data1), "U"+temprevision_no);
							//System.out.print("the split data1  "+data1);
						}else{
							temprevision_no=Integer.valueOf(data)+1;
							strPONumber=tempPoNumber.replace("U"+Integer.valueOf(data), "U"+temprevision_no);
							
						}
						
						}else{
							strPONumber=tempPoNumber+"/U"+"1";
							System.out.print("else the split data ");
						}
				}
					//String response1=getNoOfRowsForUpdatePO(request,session);}
					//strPONumber=oldPonumber;
			}else{
				List<String> tempPoDetails=getAndSaveTempPoDetails("",siteName,oldPonumber);
				 strPONumber =String.valueOf(objPurchaseDepartmentIndentProcessDao.getTempPoEnterSeqNoOrMaxId());
				//strPODate=oldPODate;
			}
			
			List<Map<String, Object>> listReceiverDtls =  objPurchaseDepartmentIndentProcessDao.getVendorOrSiteAddress(toSite);//strToSite
			for(Map<String, Object> prods : listReceiverDtls) {
				strReceiverName = prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
				strReceiverAddress = prods.get("ADDRESS")==null ? "" :   prods.get("ADDRESS").toString();
				strReceiverMobileNo = prods.get("MOBILE_NUMBER")==null ? "" :   prods.get("MOBILE_NUMBER").toString();
				strReceiverGSTIN = prods.get("GSIN_NUMBER")==null ? "" :   prods.get("GSIN_NUMBER").toString();
				receiverState = prods.get("STATE")==null ? "" :   prods.get("STATE").toString();
				strReceiveSideContactPerson = prods.get("VENDOR_CON_PER_NAME")==null ? " " :   prods.get("VENDOR_CON_PER_NAME").toString();
				strReceiverLandLine = prods.get("LANDLINE_NO")==null ? " " :   prods.get("LANDLINE_NO").toString();
			}
			List<String> billAddress=objPurchaseDepartmentIndentProcessDao.getBillingAddGstin(receiverState);
			
			billingAddress=	billAddress.get(0);
			strBillingAddressGSTIN =billAddress.get(1);
			strBillingCompanyName=billAddress.get(2);
			if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){ // delivery date given then it will convert into date format
				Date deliveryDate1 = DateUtil.convertToJavaDateFormat(strDeliveryDate);
				strDeliveryDate = new SimpleDateFormat("dd-MM-yyyy").format(deliveryDate1);
			}
			//Date date =new java.sql.Date(System.currentTimeMillis());
			//SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
			//SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yy");
			Date poDate1 = DateUtil.convertToJavaDateFormat(oldPODate);
			String strPoDate = new SimpleDateFormat("dd-MM-yyyy").format(poDate1);
			
			//String strPoDate=dt1.format(oldPODate);
			
			/*String ccmail=objPurchaseDepartmentIndentProcessDao.gettingEmpId(oldPOEntryId);
			ccmails=ccmail.concat(ccmails);*/
			Random rand = new Random();
			int rand_Number = rand.nextInt(1000000);
			String passwdForMail=String.valueOf(rand_Number);
			request.setAttribute("strPONumber",strPONumber);
			productDetails.setUserId(strUserId);
			productDetails.setVendorId(strVendorId);
			productDetails.setStrDeliveryDate(strDeliveryDate);
			productDetails.setSite_Id(toSite);
			productDetails.setIndentNo(indentNumber);
			productDetails.setPoNumber(strPONumber);
			productDetails.setSubject(subject);
			productDetails.setBillingAddress(billingAddress);
			productDetails.setPoEntryId(poEntryId);
			productDetails.setVersionNo(version_no);
			productDetails.setRefferenceNo(refferenceNo);
			productDetails.setStrPoPrintRefdate(strPoPrintRefdate);
			productDetails.setEdit_Po_Number(oldPonumber);
			productDetails.setRevision_Number(temprevision_no);
			productDetails.setPreparedBy(preparedBy);
			productDetails.setPasswdForMail(passwdForMail);
			productDetails.setPayment_Req_days(payment_Req_Days);
			productDetails.setOperation_Type("UPDATE");
			productDetails.setPoDate(oldPODate);
			
			if(approvalEmpId!=null && approvalEmpId.equals("VND")){
				objPurchaseDepartmentIndentProcessDao.insertPOEntry(productDetails);
			}
			else{
				objPurchaseDepartmentIndentProcessDao.insertTempPOEntry(productDetails,approvalEmpId,ccmails,subject);
			}
			
			//int poEntrySeqNumber = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNoByPONumber(poNumber,toSite);
			// terms and conditions start here
			String[] terms=request.getParameterValues("termsAndCond");
			if(terms!=null && !terms.equals("") && terms.length!=0){
			for(int i=0;i<terms.length;i++){
				if(terms[i]!= null && !terms[i].equals("")){
					if(approvalEmpId!=null && approvalEmpId.equals("VND")){
						objPurchaseDepartmentIndentProcessDao.saveTermsconditions(terms[i],poEntryId,strVendorId,indentNumber);
					}else{

						objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions(terms[i],strPONumber,strVendorId,indentNumber);
					}

					listOfTermsAndConditions.add(String.valueOf(terms[i]));
				}

			}
			}
			String[] conclusionsArray=request.getParameterValues("conclusionDesc");
			if(conclusionsArray!=null && !conclusionsArray.equals("")){
			String[] clean_conclusions = objCommonUtilities.cleanArray(conclusionsArray);
			List<String> conclusions = Arrays.asList(clean_conclusions);
			if(approvalEmpId!=null && approvalEmpId.equals("VND")){
				objPurchaseDepartmentIndentProcessDao.insertPoConclusions(conclusions,String.valueOf(poEntryId),"0",indentNumber);
			}
			else{
				objPurchaseDepartmentIndentProcessDao.insertTempPoConclusions(conclusions,strPONumber,indentNumber);
			}
			}
			String strTableThreeData=saveTransportDetailsForPo(request,productDetails,approvalEmpId,strVendorGSTIN,"false",receiverState);
			
			
			
			String strTableOneData = strPONumber+"@@"+strPoDate+"@@"+indentNumber+"@@"+strVendorName+"@@"+strVendorAddress+"@@"+vendorState+"@@"+strVendorGSTIN+
			"@@"+strReceiverName+"@@"+strReceiverAddress+"@@"+strReceiverMobileNo+"@@"+strReceiverGSTIN+"@@"+""+"@@"+subject+"@@"+contactPersonName+"@@"+ccEmailId+"@@"+billingAddress+"@@"+" "+"@@"+" "+"@@"+strUserName+"@@"+new java.sql.Date(System.currentTimeMillis())+"@@"+" "+"@@"+" "+"@@"+strIndentCreationDate+"@@"+siteWiseIndentNo+"@@"+
			strBillingAddressGSTIN+"@@"+strReceiveSideContactPerson+"@@"+strReceiverLandLine+"@@"+strEmail+"@@"+strBillingCompanyName+"@@"+toSite+"@@"+siteName+"@@"+strDeliveryDate+"@@"+strVendorId;
			
			String strTableTwoDate=saveProductDetailsForPo(session,request,strVendorGSTIN,noofRows,approvalEmpId,recordscount,receiverState,oldPonumber,strPONumber,oldPOEntryId,poEntryId,"false","true");
			String strGrandTotalVal=request.getAttribute("pototal").toString();// for payment and poentry to update total amount mail purpose written it
			int intstrIndentNo = Integer.parseInt(indentNumber);
			// mail purpose written this one to send data to the mail 
			for(int i=0;i<noofRows;i++){
				ProductDetails productDetailsToMail = new ProductDetails();
				int	num=Integer.parseInt(recordscount[i]);
				String childProduct = request.getParameter("ChildProduct"+num);
				String childProductName = childProduct.split("\\$")[1];					
				String mesurement = request.getParameter("UnitsOfMeasurement"+num);
				String setMeasurementName = mesurement.split("\\$")[1];					
				String quantity = request.getParameter("quantity"+num);
				String editStrQuantity = request.getParameter("strQuantity"+num);// revised po time quantity changed so we take old Quantity
				
				productDetailsToMail.setChild_ProductName(childProductName);
				productDetailsToMail.setMeasurementName(setMeasurementName);
				productDetailsToMail.setPoNumber(strPONumber);
				productDetailsToMail.setRequestQantity(quantity);
				productDetailsToMail.setRecivedQty(editStrQuantity);
				SuccessDataListToMail.add(productDetailsToMail);
				
			}
			
			if(preparedBy.equalsIgnoreCase("PURCHASE_DEPT")){ // check the status of products all initiated then it inactive it
				objPurchaseDepartmentIndentProcessDao.getupdatePurchaseDeptIndentProcess(intstrIndentNo,strUserId,toSite,approvalEmpId,sessionSiteId) ;
				}
			//objPurchaseDepartmentIndentProcessDao.getupdatePurchaseDeptIndentProcess(intstrIndentNo, strUserId,strSiteId,approvalEmpId,sessionSiteId) ;
			objPurchaseDepartmentIndentProcessDao.updateTotalAmt(String.valueOf(strGrandTotalVal),approvalEmpId,strPONumber);
			
			if(approvalEmpId!=null && approvalEmpId.equals("VND")  ){
				strResponse="POPrintPage";
				paidAmount =objPurchaseDepartmentIndentProcessDao.updateAdvancePaidAmount(request,oldPonumber,toSite,strVendorId,Double.valueOf(strGrandTotalVal));
				int result=objPurchaseDepartmentIndentProcessDao.updateAccPayment(oldPonumber,strPONumber,String.valueOf(strGrandTotalVal),true);
				if(paidAmount>Double.valueOf(strGrandTotalVal)){ // paid amount is more than the total amount then check condition and  trigger the mail
					 allEmployeeEmailsOfAcc=(List<String>) request.getAttribute("emailList");
					 note="System had updated Paid amount, same as amount in Updated PO.";
					//allEmployeeEmailsOfAcc=gettingaccMailsForUpdatePo(receiverState);
					ispaymentUpdate=true;
				}
				objPurchaseDepartmentIndentProcessDao.updateIndentAndDcPONumber(oldPonumber,strPONumber,String.valueOf(strGrandTotalVal));
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls("0",String.valueOf(poEntryId),strUserId,toSite, "C","");
			} // save approvals details in pocrtApproval details
			else{
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(String.valueOf(strPONumber),"0",strUserId,toSite, "C","");
				strResponse="CreatePOFinalPage";
			}
			
			final VendorDetails vd = new VendorDetails();
			
			String nameEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(strUserId);
			vd.setVendor_Id(strVendorId);
			final String mailPoNumber =oldPonumber;
			final String ccEmailTo = ccmails;
			final int getLocalPort = request.getLocalPort();
			final String emailPODate=oldPODate;
			final String strSiteName=mailSiteName;
			// mail send fast than compare to normal flow
			if(ccEmailTo != null &&! ccEmailTo.equals("")){
				if(ccEmailTo.contains(",")){
					for(int i=0;i<ccEmailTo.split(",").length;i++){
						toMailListArrayList.add(ccEmailTo.split(",")[i]);
					}
				}else{
					toMailListArrayList.add(ccEmailTo);
				}
			}
			if(ispaymentUpdate){
				for(int i=0;i<allEmployeeEmailsOfAcc.size();i++){
					toMailListArrayList.add(allEmployeeEmailsOfAcc.get(i));
				}
			}

			String emailto [] = new String[toMailListArrayList.size()];
			toMailListArrayList.toArray(emailto);
			if(approvalEmpId!=null && approvalEmpId.equals("VND") && emailto!=null && !emailto.equals("")){
				
				EmailFunction objEmailFunction = new EmailFunction();
				 Object ApproveData[]={oldPonumber,emailPODate,strPONumber,strVendorName,String.valueOf(getLocalPort),note};
				objEmailFunction.sendMailForUpdatePo(SuccessDataListToMail,ApproveData,emailto);
				
			}else if(emailto!=null && !emailto.equals("")){
				// if approvals are thre then mail send to next approval person
				String approveEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(approvalEmpId);
				String data[] = approveEmail.split(",");
				String strApproveName=data[0];
				String approveToEmailAddress[]={data[1]};
				Date dateobj = new Date();
			    String CreationDate=new SimpleDateFormat("dd-MM-yyyy").format(dateobj);
			    Object ApproveData[]={indentNumber,approvalEmpId,toSite,oldPonumber,CreationDate,strPONumber,siteWiseIndentNo,siteName,
			    					strVendorName,String.valueOf(strGrandTotalVal),strApproveName,preparedBy,passwdForMail,String.valueOf(getLocalPort),oldPOEntryId,deliveryDate,ccmails,strVendorId};
				EmailFunction objEmailFunction = new EmailFunction();
				objEmailFunction.sendUpdatePOApproval(SuccessDataListToMail,approveToEmailAddress,getLocalPort,mailPoNumber,emailPODate,strSiteName,strApproveName,strVendorName,ApproveData);
				}
			request.setAttribute("listOfTermsAndConditions",listOfTermsAndConditions);

			objviewPOPageDataMap.put("AddressDetails", strTableOneData);
			objviewPOPageDataMap.put("productDetails", strTableTwoDate);
			objviewPOPageDataMap.put("TransportDetails", strTableThreeData);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);
			request.setAttribute("receiverState", receiverState); // this is for success page generated then it display based on state below print button
			transactionManager.commit(status);
			response="Success";
		}
		catch(Exception e)
		{
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response="Failure";
			e.printStackTrace();
		}
		/*if(response.equals("Success")&&isAnyUpdateFailed==false)
		{
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Committed");
		}
		else
		{
			response="Failure";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Rollbacked");
		}*/
		return strResponse;
	}

	@Override
	public List<ProductDetails> getProductDetailsListsForQuatation(String indentNo, String vendorName,HttpServletRequest request) {
		List<ProductDetails> listGetInvoiceDetailsList = null;
		List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();
		//	List<ProductDetails> getPoDetails = new ArrayList<ProductDetails>();
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		ProductDetails objGetDetails=null;
		int sno=0;
		String strTableTwoDate="";
		String productId =""; 
		String subProductId ="";
		String childProductId ="";
		String setMeasurementId ="";
		String productName ="";		
		String subProductName ="";		
		String setMeasurementName ="";	
		String strChildProductName="";

		String requiredQuantity="";
		String price="";
		String basicAmount="";
		String discount="";
		String amountAfterDiscount="";
		String tax="";
		String taxAmount="";
		String amountAfterTax="";
		String otherCharges="";
		String otherChargesAfterTax="";
		String totalAmount="";
		String hsnCode="";
		String taxOnOtherTransportCharges="";
		//	String finalAmount="";
		String strIndentCreationDetailsId="";
		String ChildProductDescription="";
		int strno=0;
		double totalAmt=0.0;
		double totChargesVal=0.0;
		int val = 0;
		double roundoff = 0;
		double grandtotal = 0;
		double doubletaxAmt=0.0;
		double doubleAmountAfterTax=0.0;
		double doubleTotalAmt=0.0;
		double doubleBasicAmt=0.0;
		double doubleAmtAfterDiscount=0.0;
		String vendorId=request.getParameter("vendorId1");
		int noofrows = 0;
		try {
			noofrows = Integer.parseInt(request.getParameter("numberOfRows"));

			for(int num=1;num<=noofrows;num++){
				if(request.getParameter("checkboxname"+num)!=null)
				{
					sno++;
					productId = request.getParameter("productId"+num);
					subProductId = request.getParameter("subProductId"+num);
					childProductId = request.getParameter("childProductId"+num);
					productName = request.getParameter("product"+num);
					subProductName = request.getParameter("subProduct"+num);
					String childProductName = request.getParameter("childProduct"+num);
					setMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
					setMeasurementName = request.getParameter("unitsOfMeasurement"+num);
					String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
					String strQuantity = request.getParameter("strRequestQuantity"+num);

					ProductDetails productDetails = new ProductDetails();
					productDetails.setStrSerialNumber(String.valueOf(sno));
					productDetails.setProductId(productId);
					productDetails.setProductName(productName);
					productDetails.setSub_ProductId(subProductId);
					productDetails.setSub_ProductName(subProductName);
					productDetails.setChild_ProductId(childProductId);
					productDetails.setChild_ProductName(childProductName);
					productDetails.setMeasurementId(setMeasurementId);
					productDetails.setMeasurementName(setMeasurementName);
					productDetails.setIndentCreationDetailsId(indentCreationDetailsId);
					productDetails.setQuantity(strQuantity);
					boolean value=objPurchaseDepartmentIndentProcessDao.checkVendorQuantity(vendorId,indentCreationDetailsId);
					if(!value){
					listProductDetails.add(productDetails);}
				}
			}


			listGetInvoiceDetailsList = objPurchaseDepartmentIndentProcessDao.getProductDetailsLists(indentNo,vendorName,listProductDetails,request);

			for(int i =0 ; i < listGetInvoiceDetailsList.size() ; i++){

				objGetDetails = listGetInvoiceDetailsList.get(i);


				productName=objGetDetails.getProductName();

				subProductName=objGetDetails.getSub_ProductName();

				strChildProductName=objGetDetails.getChild_ProductName();

				setMeasurementName=objGetDetails.getMeasurementName();
				requiredQuantity=objGetDetails.getRequiredQuantity();
				price=objGetDetails.getPrice();
				basicAmount=objGetDetails.getBasicAmt();
				discount=objGetDetails.getStrDiscount();
				amountAfterDiscount=objGetDetails.getStrAmtAfterDiscount();
				tax=objGetDetails.getTax();
				taxAmount=objGetDetails.getTaxAmount();
				amountAfterTax=objGetDetails.getAmountAfterTax();
				otherChargesAfterTax=objGetDetails.getOtherchargesaftertax1();

				//totalAmount=objGetDetails.getTotalAmount();
				totalAmount=objGetDetails.getAmountAfterTax();
				hsnCode=objGetDetails.getHsnCode();

				strno=objGetDetails.getSerialno();

				double totalvalue=Double.valueOf(totalAmount);
				double strOtherChargesAfterTax=Double.valueOf(otherChargesAfterTax);
				totChargesVal=totChargesVal+strOtherChargesAfterTax;
				doubleBasicAmt=Double.valueOf(basicAmount);
				doubletaxAmt=Double.valueOf(taxAmount);
				doubleAmountAfterTax=Double.valueOf(amountAfterTax);
				doubleTotalAmt=Double.valueOf(totalAmount);
				doubleAmtAfterDiscount=Double.valueOf(amountAfterDiscount);

				doubleBasicAmt=Double.parseDouble(new DecimalFormat("##.##").format(doubleBasicAmt));
				BigDecimal doubleBasicAmtt=new BigDecimal(doubleBasicAmt).setScale(2,RoundingMode.CEILING);
				
				doubletaxAmt=Double.parseDouble(new DecimalFormat("##.##").format(doubletaxAmt));
				doubleAmountAfterTax=Double.parseDouble(new DecimalFormat("##.##").format(doubleAmountAfterTax));
				doubleTotalAmt=Double.parseDouble(new DecimalFormat("##.##").format(doubleTotalAmt));
				doubleAmtAfterDiscount=Double.parseDouble(new DecimalFormat("##.##").format(doubleAmtAfterDiscount));
				
				BigDecimal bigDoubletaxAmt=new BigDecimal(doubletaxAmt).setScale(2,RoundingMode.CEILING);
				BigDecimal bigDoubleAmountAfterTax=new BigDecimal(doubleAmountAfterTax).setScale(2,RoundingMode.CEILING);
				BigDecimal bigDoubleAmtAfterDiscount=new BigDecimal(doubleAmtAfterDiscount).setScale(2,RoundingMode.CEILING);



				totalAmt=totalAmt+totalvalue;

				if(i == listGetInvoiceDetailsList.size()-1){
					totalAmt = totalAmt+totChargesVal;
					totalAmt = Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					//BigDecimal bigTotalAmt=new BigDecimal(totalAmt).setScale(2,RoundingMode.CEILING);
					
					val = (int) Math.ceil(totalAmt);
					roundoff=Math.ceil(totalAmt)-totalAmt;
					roundoff = Double.parseDouble(new DecimalFormat("##.##").format(roundoff));
					grandtotal=Math.ceil(totalAmt);


				}
				strTableTwoDate+=strno+"@@"+productName+"@@"+subProductName+"@@"+strChildProductName+"@@"+setMeasurementName
				+"@@"+hsnCode+"@@"+requiredQuantity+"@@"+price+"@@"+doubleBasicAmtt+"@@"+discount+"@@"+bigDoubleAmtAfterDiscount
				+"@@"+tax+"@@"+bigDoubletaxAmt+"@@"+bigDoubleAmountAfterTax+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+bigDoubleAmountAfterTax+"@@"+new BigDecimal(roundoff).setScale(2,RoundingMode.CEILING)+"@@"+new BigDecimal(grandtotal).setScale(2,RoundingMode.CEILING)+"@@"
				+totChargesVal+"@@"+new BigDecimal(totalAmt).setScale(2,RoundingMode.CEILING)+"&&";

			}

			objviewPOPageDataMap.put("productDetails", strTableTwoDate);

			request.setAttribute("viewPoPageData", objviewPOPageDataMap);

			//System.out.println("the list of details"+listGetInvoiceDetailsList.size());		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listGetInvoiceDetailsList;
	}


	@Override
	public int getSiteWiseIndentNo(int indentNumber) {
		return objPurchaseDepartmentIndentProcessDao.getSiteWiseIndentNo(indentNumber);
	}
	// print indent time it will call in purchase dept 
	public void printIndentForPurchase(Model model, HttpServletRequest request, int site_id, String user_id) {

		int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		int siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
		String strCreateDate="";

		//System.out.println(indentNumber);
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		String siteId=request.getParameter("siteId");
		// get the version no and set in success page
		 String versionNo=request.getParameter("versionNo");
		 String reference_No=request.getParameter("reference_No");
		 String issue_date=request.getParameter("issue_date");
		
		list=objPurchaseDepartmentIndentProcessDao.purchasePrintIndent(indentNumber);



		request.setAttribute("IndentDetails",list);
		System.out.println(list.get(0).getChildProduct1());
		IndentCreationBean icb = new IndentCreationBean();
		icb = cntlIndentrocss.getCreateAndRequiredDates(indentNumber);
		//indentCreationBean.setStrRequiredDate(icb.getStrRequiredDate());
		strCreateDate = icb.getStrCreateDate();
		List<IndentCreationBean> list1 = new ArrayList<IndentCreationBean>();
		IndentCreationBean icb1 = new IndentCreationBean();// here we need to set creation date,sitewise number,referrence no,issue date
		icb1.setIndentNumber(indentNumber);
		icb1.setSiteWiseIndentNo(siteWiseIndentNo);
		icb1.setStrCreateDate(strCreateDate);
		icb1.setVersionNo(versionNo);
		icb1.setReference_No(reference_No);
		icb1.setIssue_date(issue_date);
		
		list1.add(icb1);
		request.setAttribute("IndentDtls",list1);
		model.addAttribute("createdSiteName",cntlIndentrocss.getSiteNameWhereIndentCreated(indentNumber));// this one used in signature purpose
		model.addAttribute("siteAddress",cntlIndentrocss.getAddressOfSite(site_id));
		model.addAttribute("listCreatedName",objPurchaseDepartmentIndentProcessDao.getApproveCreateEmp(indentNumber,request));
		//	 model.addAttribute("createEmpName",createEmpName);
		objPurchaseDepartmentIndentProcessDao.getVerifiedEmpNames(indentNumber,request,siteId); // this is also used in sigbnature also


	}


	@Override
	public String CancelPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String site_id) {

		String pendingEmpId="";
		String response="";
		int result=0;
		boolean chckEmp=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,temp_Po_Number);
		String checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(temp_Po_Number);	// check whether po status is active or not
		String remarks=request.getParameter("Remarks_cancel")==null ? (request.getParameter("Remarks")==null? "": request.getParameter("Remarks"))
				: request.getParameter("Remarks_cancel");
		pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id); // get previous emp id for mail purpose

		if((pendingEmpId!=null || pendingEmpId.equals("")) && (checkval.equals("A") && chckEmp)){

			result=objPurchaseDepartmentIndentProcessDao.updateEmpId(pendingEmpId,temp_Po_Number);
			objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(temp_Po_Number,"0",user_id, site_id, "CAN",remarks); // save Po_crt table status
			//	int i=objPurchaseDepartmentIndentProcessDao.deleteTemppoTermsAdnConditions(temp_Po_Number);
			response="success";
			//	}
		}

		return response;
	}

	@Override
	public List<ProductDetails> getListOfCancelPo(String userId,String siteId) {
		return objPurchaseDepartmentIndentProcessDao.getListOfCancelPo(userId,siteId);
	}
	@Override
	public List<ProductDetails> getViewCancelPoDetails(String poNumber, String reqSiteId) {
		return objPurchaseDepartmentIndentProcessDao.getViewCancelPoDetails(poNumber, reqSiteId);
	}
	@Override
	public List<ProductDetails> getProductDetailsListsForCancelPo(String poNumber,String reqSiteId) {
		return objPurchaseDepartmentIndentProcessDao.getProductDetailsListsForCancelPo(poNumber,reqSiteId);
	}

	@Override
	public List<ProductDetails> getTransChrgsDtlsForCancelPo(String poNumber,String reqSiteId) {
		return objPurchaseDepartmentIndentProcessDao.getTransChrgsDtlsForCancelPo(poNumber,reqSiteId);
	}


	/*==================================update temp po save================================================*/

	@Override
	public String updateTempPoPage(Model model, HttpServletRequest request,HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDup_tempPo&SPO, ");
		String response = "";
		boolean isAnyUpdateFailed = false;
		String strQuantity="";
		String strProdEdit="";
		String otherOrTransportCharges ="";
		String taxOnOtherOrTransportCharges ="";
		String otherOrTransportChargesAfterTax ="";
		String totalAmount ="";
		String poEntryDetailsId ="";
		String strTotalAmount="";
		int val=0;
		String strStatus="A";
		double intPoIntiatedQuantity =0.0;
		double intRequestQuantity =0.0;
		List<String> listOfTermsAndConditions = new ArrayList<String>();
		try{
			int noofRows = Integer.parseInt(request.getParameter("numberOfRows"));
			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();	
			String sessionSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

			String poNumber = request.getParameter("poNo");
			String oldPONumber=request.getParameter("oldPONumber");
			//System.out.println("updating poNumber: "+poNumber);
			String vendorId=request.getParameter("vendorId");
			String strVendorId=request.getParameter("strVendorId");
			String toSite = request.getParameter("toSite");
			String strSiteId=request.getParameter("siteId");
			String indentNumber = request.getParameter("strIndentNo");
			String ccEmailId=request.getParameter("ccEmailId2");
			String subject=request.getParameter("subject")== null ? "-" : request.getParameter("subject").toString();
			String payment_Req_Days=request.getParameter("days")== null ? "" : request.getParameter("days").toString(); // this is for payment no of days adding to the i.e modified then save the value
			
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+poNumber);

			if(!vendorId.equals(strVendorId) || !ccEmailId.equals("") || !subject.equals("-") || !payment_Req_Days.equals("")){

				objPurchaseDepartmentIndentProcessDao.updateTempPOVendorDetails(vendorId,poNumber,ccEmailId,subject,"false","",payment_Req_Days);

			}

			int poEntrySeqNumber = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNoByTempPONumber(poNumber,strSiteId);
			//updating Product Details
			//	System.out.println("---Product Details---");
			for(int num=1;num<=noofRows;num++){
				String actionValue = request.getParameter("actionValue"+num);

				if(actionValue==null&&request.getParameter("ChildProduct"+num)==null)
				{
					//System.out.println(num+" row newly added & deleted at front end.");
					continue;
				}else{}


				String product = request.getParameter("Product"+num);
				String prodsInfo[] = product.split("\\$");	
				String productId = prodsInfo[0];
				String productName = prodsInfo[1];

				String subProduct = request.getParameter("SubProduct"+num);
				String subProdsInfo[] = subProduct.split("\\$");					
				String subProductId = subProdsInfo[0];
				String subProductName = subProdsInfo[1];				

				String childProduct = request.getParameter("ChildProduct"+num);
				String childProdsInfo[] = childProduct.split("\\$");					
				String childProductId = childProdsInfo[0];
				String childProductName = childProdsInfo[1];

				String mesurement = request.getParameter("UnitsOfMeasurement"+num);
				String measureInfo[] = mesurement.split("\\$");					
				String setMeasurementId = measureInfo[0];
				String setMeasurementName = measureInfo[1];

				String quantity = request.getParameter("quantity"+num);
				strQuantity=request.getParameter("strQuantity"+num);
				String price = request.getParameter("PriceId"+num);
				String basicAmount = request.getParameter("BasicAmountId"+num);

				String Discount=request.getParameter("Discount"+num);
				String amountAfterDiscount=request.getParameter("amtAfterDiscount"+num);

				String tax = request.getParameter("tax"+num);
				String taxId = tax;
				if(tax.contains ("$")){
					String parts[] = tax.split("\\$");
					taxId=  parts[0];
				}
				/***************************************** for convert the child product measurement start************************/
				String methodName  = validateParams.getProperty(childProductId) == null ? "" : validateParams.getProperty(childProductId).toString();
				// here checking the child product which available in properties files then 
				
					//methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();
					// here checking the child product which available in properties files then 
					if(!methodName.equals("")) {	
						Map<String, String> values = null;
						String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();
						//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
						Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
						Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor

						double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProductId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProductId+"ActualQuantity").toString());
						double doubleInputQuantity =  Double.valueOf(quantity);
						String strConversionMesurmentId  =  validateParams.getProperty(childProductId+"ID") == null ? "" : validateParams.getProperty(childProductId+"ID").toString();
						// with multiple parameters
						//methodName = "convertCHP00536";
						Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
						Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
						System.out.println("the printdogmethod class is "+printDogMethod);
						values = (Map<String, String>) printDogMethod.invoke(mesurment,basicAmount,doubleActualQuantity,doubleInputQuantity,setMeasurementName);
						
						for(Map.Entry<String, String> retVal : values.entrySet()) {
							BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
							quantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
							//quantity=retVal.getKey();
							price=retVal.getValue(); 
						}	
						setMeasurementId = strConversionMesurmentId;
						setMeasurementName = validateParams.getProperty(childProductId+"IDMNAME") == null ? "" : validateParams.getProperty(childProductId+"IDMNAME").toString();
					}
				
				/***************************************** for convert the child product measurement end************************/
				
				
				String taxAmount = request.getParameter("taxAmount"+num);
				String amountAfterTax = request.getParameter("amountAfterTax"+num);

				String hsnCode = request.getParameter("hsnCode"+num);
				String vedorDescription = request.getParameter("vendorDescription"+num);

				 otherOrTransportCharges = request.getParameter("otherOrTransportCharges"+num)==null ? "0.0" : request.getParameter("otherOrTransportCharges"+num);
				 taxOnOtherOrTransportCharges = request.getParameter("taxOnOtherOrTransportCharges"+num)==null ? "0.0" : request.getParameter("taxOnOtherOrTransportCharges"+num);
				 otherOrTransportChargesAfterTax = request.getParameter("otherOrTransportChargesAfterTax"+num)==null ? "0.0" : request.getParameter("otherOrTransportChargesAfterTax"+num);


				 totalAmount = request.getParameter("totalAmount"+num);
				 strTotalAmount= request.getParameter("strTotalAmount"+num);
				String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
				 poEntryDetailsId = request.getParameter("poEntryDetailsId"+num);

				ProductDetails productDetails = new ProductDetails();
				productDetails.setProductId(productId);
				productDetails.setSub_ProductId(subProductId);
				productDetails.setChild_ProductId(childProductId);
				productDetails.setChild_ProductName(childProductName);
				productDetails.setMeasurementId(setMeasurementId);
				productDetails.setQuantity(quantity);
				productDetails.setPricePerUnit(price);
				productDetails.setBasicAmt(basicAmount);
				productDetails.setStrDiscount(Discount);
				productDetails.setStrAmtAfterDiscount(amountAfterDiscount);
				productDetails.setTax(taxId);
				productDetails.setTaxAmount(taxAmount);
				productDetails.setAmountAfterTax(amountAfterTax);
				productDetails.setHsnCode(hsnCode);
				productDetails.setOtherOrTransportCharges1(Double.parseDouble(otherOrTransportCharges));
				productDetails.setTaxOnOtherOrTransportCharges1(Double.parseDouble(taxOnOtherOrTransportCharges));
				productDetails.setOtherOrTransportChargesAfterTax1(Double.parseDouble(otherOrTransportChargesAfterTax));

				productDetails.setTotalAmount(totalAmount);
				productDetails.setIndentCreationDetailsId(indentCreationDetailsId);
				productDetails.setPoEntryDetailsId(poEntryDetailsId);
				productDetails.setChildProductCustDisc(vedorDescription);
				
				
				if(actionValue==null&&request.getParameter("ChildProduct"+num)!=null) // this is for newly added product write this or action come null time
				{
					int result1 = objPurchaseDepartmentIndentProcessDao.insertTempPOEntryDetails(productDetails,poEntrySeqNumber);
					// here we can do inactive why because in indent any product there then that product added newly in this time so indent inactive if last product then check and inactive
					if(indentCreationDetailsId!=null && !indentCreationDetailsId.equals("")){

						String value=objPurchaseDepartmentIndentProcessDao.getPoInitiateQuan(indentCreationDetailsId);
						//double intPoIntiatedQuantity = objPurchaseDepartmentIndentProcessDao.getIntiatedQuantityInPurchaseTable(indentCreationDetailsId);
						if(value.contains("|")){

							String data[]=value.split("\\|");
							intPoIntiatedQuantity =Double.valueOf(data[0]);
							intRequestQuantity =Double.valueOf(data[1]);

						}
						double	doubleQuantity = Double.valueOf(quantity ==null ? "0" : quantity.toString());

						if(intPoIntiatedQuantity+doubleQuantity >= intRequestQuantity){

							strStatus = "I";

						}   if(result1==1){
							int intIndentCreationDetailsId = Integer.parseInt(indentCreationDetailsId);
							objPurchaseDepartmentIndentProcessDao.updatePurchaseDeptIndentProcesstbl(doubleQuantity, intIndentCreationDetailsId,strStatus);
						}

						String approvalEmpId=objPurchaseDepartmentIndentProcessDao.getTemproryuser(strUserId);
						int intstrIndentNo = Integer.parseInt(indentNumber);
						objPurchaseDepartmentIndentProcessDao.getupdatePurchaseDeptIndentProcess(intstrIndentNo, strUserId,strSiteId,approvalEmpId,sessionSiteId) ;

					}
					
					if(result1==0){isAnyUpdateFailed = true;break;} // if data not insert in tempPo entry details break it
					continue;
				}
				/*  S-Same ,  E-Edit ,  R-Remove  */
				if(actionValue.equals("S"))
				{	
					if(!strTotalAmount.equals(totalAmount)){ // here transportation added then total amount change so check this condition

						int result2 = objPurchaseDepartmentIndentProcessDao.updateTempPOEntryDetails(productDetails);	
					}
					//System.out.println(num+" row does not changed. so Remains same.");
					continue;
				}//else{}
				if(actionValue.equals("E") && !indentCreationDetailsId.equals("-"))
				{

					strProdEdit="true";
					int result2 = objPurchaseDepartmentIndentProcessDao.updateTempPOEntryDetails(productDetails);
					int result5=objPurchaseDepartmentIndentProcessDao.updateTempPOQuantityDetails(indentCreationDetailsId,quantity,strQuantity);
					
					//System.out.println(num+" row Edited. so it is updated in PO_ENTRY_DETALS: "+result2);
					if(result2==0){isAnyUpdateFailed = true;break;} // transaction management use here so written isAnyUpdateFailed is set to value
					continue;
				}//else{}
				if(actionValue.equals("R") && !indentCreationDetailsId.equals("-"))
				{
					int result3 = objPurchaseDepartmentIndentProcessDao.deleteTempPOEntryDetails(poEntryDetailsId);
					int result4 =0;
					//	System.out.print(num+" row removed. so it is deleted from PO_ENTRY_DETAILS: "+result3);
					// update poQuantity for normal po i.e back Quantity purchase_dept_indent_process table
					if(oldPONumber.equals("")){ result4 = objPurchaseDepartmentIndentProcessDao.updatePOIntiatedQuantityInPDTable(indentCreationDetailsId,quantity);}
					
					//	System.out.println("  & updated po_intiated_quantity in SUM_PURCHASE_DEPT_INDENT_PROSS: "+result4);
					if(result3==0||result4==0){isAnyUpdateFailed = true;break;}
					continue;
				}//else{}
			}
			//updating Transport Charges
			//System.out.println("---Transport Details---");
			int noofTransRows = Integer.parseInt(request.getParameter("noofTransRows"));
			if(isAnyUpdateFailed==false){
				for(int num=1;num<=noofTransRows;num++){
					String transactionActionValue = request.getParameter("transactionActionValue"+num);
					if(transactionActionValue==null&&request.getParameter("Conveyance"+num)==null)
					{
						//System.out.println(num+" row newly added & deleted at front end.");
						continue;
					}//else{}
					String Conveyance = request.getParameter("Conveyance"+num);
					String ConveyanceId = Conveyance.split("\\$")[0];
					String ConveyanceAmount = request.getParameter("ConveyanceAmount"+num);
					String GSTTaxId = request.getParameter("GSTTax"+num).split("\\$")[0];
					String GSTAmount = request.getParameter("GSTAmount"+num);
					String AmountAfterTax = request.getParameter("AmountAfterTax"+num);

					TransportChargesDto transportChargesDto = new TransportChargesDto();
					transportChargesDto.setTransportId(ConveyanceId);
					transportChargesDto.setTransportAmount(ConveyanceAmount);
					transportChargesDto.setTransportGSTPercentage(GSTTaxId);
					transportChargesDto.setTransportGSTAmount(GSTAmount);
					transportChargesDto.setTotalAmountAfterGSTTax(AmountAfterTax);
					if(transactionActionValue==null&&Conveyance!=null)
					{	
						ProductDetails productDetails = new ProductDetails();
						productDetails.setSite_Id(toSite);
						productDetails.setPoEntrySeqNumber(poEntrySeqNumber);
						productDetails.setIndentNo(indentNumber);
						int poTransChrgsSeqNo = objPurchaseDepartmentIndentProcessDao.getPoTransChrgsEntrySeqNo();

						int result5 = objPurchaseDepartmentIndentProcessDao.insertTempPOTransportDetails(poTransChrgsSeqNo,productDetails,transportChargesDto,poNumber);
						System.out.println(num+" row newly added. so inserted in SUMADHURA_PO_TRNS_O_CHRGS_DTLS: "+result5);
						if( result5==0){isAnyUpdateFailed = true;break;} // for transaction management use here so use isupdateFailed
						continue;
					}//else{}
					/*  S-Same ,  E-Edit ,  R-Remove  */
					if(transactionActionValue.equals("S"))
					{
						//System.out.println(num+" row does not changed. so Remains same.");
						continue;
					}//else{}
					if(transactionActionValue.equals("E"))
					{
						int poTransChrgsDtlsSeqNo = Integer.parseInt(request.getParameter("poTransChrgsDtlsSeqNo"+num));

						int result6 = objPurchaseDepartmentIndentProcessDao.updateTempPOTransportChargesDetails(transportChargesDto,poTransChrgsDtlsSeqNo);
						//System.out.println(num+" row edited. so updated in SUMADHURA_PO_TRNS_O_CHRGS_DTLS: "+result6);
						if( result6==0){isAnyUpdateFailed = true;break;}// for transaction management use here so use isupdateFailed
						continue;
					}//else{}
					if(transactionActionValue.equals("R"))
					{
						int poTransChrgsDtlsSeqNo = Integer.parseInt(request.getParameter("poTransChrgsDtlsSeqNo"+num));
						int result7 = objPurchaseDepartmentIndentProcessDao.deleteTempPOTransportChargesDetails(poTransChrgsDtlsSeqNo);
						//System.out.println(num+" row removed. so it is deleted from SUMADHURA_PO_TRNS_O_CHRGS_DTLS: "+result7);
						if(result7==0){isAnyUpdateFailed = true;break;}// for transaction management use here so use isupdateFailed
						continue;
					}//else{}
				}// end of FOR loop
			}// end of IF block
			String total=objPurchaseDepartmentIndentProcessDao.getTempPoTotalAmt(strUserId,poEntrySeqNumber); // from temp table amount take and update in permanent table if no approval the
			objPurchaseDepartmentIndentProcessDao.updateTotalAmt(total,strUserId,poNumber); // temp po total amount updated in temp_po_entry table
			int j=objPurchaseDepartmentIndentProcessDao.deleteTemppoTermsAdnConditions(poNumber,"false"); // here false is using for delete temp Po terms and conditons
			//String termsAndCondition = "";
			int value=0;int count=0;
			String[] terms=request.getParameterValues("termsAndCond");
			for(int i=0;i<terms.length;i++){

				if(terms[i]!= null && !terms[i].equals("")){
					/*if(terms[i].contains("days after date of delivery.") && !payment_Req_Days.equals("") && !payment_Req_Days.equals("0")){ // if it is already existed then it will replace it
					terms[i]=terms[i].replace(terms[i],"Payment:"+payment_Req_Days+ " days after date of delivery.");
					count++;
					}*/
					value=objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions(terms[i],String.valueOf(poEntrySeqNumber),strVendorId,indentNumber);
					listOfTermsAndConditions.add(String.valueOf(terms[i]));

				}
			}
			// this is for terms and conditions to add in the last one i.e payment request date is given in that
			/*if(!payment_Req_Days.equals("") && !payment_Req_Days.equals("0") && count==0){
				objPurchaseDepartmentIndentProcessDao.saveTempTermsconditions("Payment:"+payment_Req_Days+" days after date of delivery.",String.valueOf(poEntrySeqNumber),strVendorId,indentNumber);
				listOfTermsAndConditions.add("Payment:"+payment_Req_Days+" days after date of delivery.");
			}*/
			//Conclusions author - Rafi
			objPurchaseDepartmentIndentProcessDao.deleteTempPoConclusions(poNumber);
			String[] conclusionsArray=request.getParameterValues("conclusionDesc");
			String[] clean_conclusions = objCommonUtilities.cleanArray(conclusionsArray);
			List<String> conclusions = Arrays.asList(clean_conclusions);
			objPurchaseDepartmentIndentProcessDao.insertTempPoConclusions(conclusions,poNumber,indentNumber);
			//Conclusions - End
			if(value>0){
				// update the approval status previous it set Cancel below that set to modified
				objPurchaseDepartmentIndentProcessDao.updateTempPoVieworCancel(poNumber,strSiteId);	
				//objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(poNumber,"0",strUserId, sessionSiteId, "E"," ");
			}

			response="Success";
		}
		
		catch(Exception e)
		{
			response="Failure";
			e.printStackTrace();
		}
		if(response.equals("Success")&&isAnyUpdateFailed==false)
		{
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Committed");
		}
		else
		{
			response="Failure";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Rollbacked");
		}
		return response;
	}

	@Override
	public String tempPoSubProducts(String prodId,String indentNumber,String reqSiteId) {
		return objPurchaseDepartmentIndentProcessDao.tempPoSubProducts(prodId,indentNumber,reqSiteId);
	}

	@Override
	public String tempPoChildProducts(String subProdId,String indentNumber,String reqSiteId) {
		return objPurchaseDepartmentIndentProcessDao.tempPoChildProducts(subProdId,indentNumber,reqSiteId);
	}

	@Override
	public List<ProductDetails> getTempTermsAndConditions(String poNumber,String isRevised,String reqSiteId) {


		return objPurchaseDepartmentIndentProcessDao.getTempTermsAndConditions(poNumber,isRevised,reqSiteId);
	}


	/*************************************************get comments for Po****************************************/
	@Override
	public String  getCancelPoComments(String poNumber) {

		List<String> list = null;
		String strPurpose = "";
		try{
			strPurpose = objPurchaseDepartmentIndentProcessDao.getCancelPoComments(poNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return strPurpose;
	}

	public String getTempPOSubject(String poNumber) {

		return objPurchaseDepartmentIndentProcessDao.getTempPOSubject(poNumber);
	}
	public String getTempPoCCEmails(String poNumber) {
		
		return objPurchaseDepartmentIndentProcessDao.getTempPoCCEmails(poNumber);
	}
	
	@SuppressWarnings("unused")
	@Override
	public String getNoOfRowsForUpdatePO(HttpServletRequest request,HttpSession session) {
		String response = "";
		String temp_No="";
		boolean isAnyUpdateFailed = false;
		try{
			int noofRows = Integer.parseInt(request.getParameter("numberOfRows"));
			String poNumber = request.getParameter("poNo");
			String toSite = request.getParameter("toSiteId");
			String indentNumber = request.getParameter("indentNumber"); 
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+poNumber);
			
			for(int num=1;num<=noofRows;num++){
				String actionValue = request.getParameter("actionValue"+num);
				if(actionValue==null&&request.getParameter("ChildProduct"+num)==null)
				{
					//System.out.println(num+" row newly added & deleted at front end.");
					continue;
				}else{}

				String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
				String oldquantity = request.getParameter("strQuantity"+num);
				String actualQuantity=request.getParameter("quantity"+num);
				if(actionValue==null&&request.getParameter("ChildProduct"+num)!=null)
				{
						continue;
				}else{}
				if(actionValue.equals("E"))
				{ objPurchaseDepartmentIndentProcessDao.updateTempPOQuantityDetails(indentCreationDetailsId,actualQuantity,oldquantity); 
				double strQuantity=(Double.parseDouble(oldquantity)-Double.parseDouble(actualQuantity));
				if(strQuantity>0){objPurchaseDepartmentIndentProcessDao.checkIOndentCreationtbl(indentNumber);}
				
				}
				if(actionValue.equals("R"))
				{
					int result4 = objPurchaseDepartmentIndentProcessDao.updatePOIntiatedQuantityInPDTable(indentCreationDetailsId,oldquantity);
					objPurchaseDepartmentIndentProcessDao.checkIOndentCreationtbl(indentNumber);
					
					//	System.out.println("  & updated po_intiated_quantity in SUM_PURCHASE_DEPT_INDENT_PROSS: "+result4);
					if(result4==0){isAnyUpdateFailed = true;break;}
					continue;
				}//else{ objPurchaseDepartmentIndentProcessDao.checkIOndentCreationtbl(indentNumber);}
			}
		response="success";
		}
		catch(Exception e)
		{
			response="Failure";
			e.printStackTrace();
		}
		/*if(response.equalsIgnoreCase("success")&&isAnyUpdateFailed==false)
		{
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Committed");
		}
		else
		{
			response="Failure";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Data Rollbacked");
		}*/
		return response;
	}
	
	public int insertSiteLevelIndentData(int site_Id,String user_Id,int indent_Number,String siteWiseIndent){
		
		return objPurchaseDepartmentIndentProcessDao.insertSiteLevelIndentData(site_Id,user_Id,indent_Number,siteWiseIndent);
	}
	
	
		public static void main(String[] args){


		String thirdTableData = "None@@0@@0@@0@@0&&";
		String [] third = thirdTableData.split("&&");
		double i=-2;
		System.out.println(Math.abs(i));
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date dateobj = new Date();
		String convertedPoDate = new SimpleDateFormat("dd-MM-yyyy").format(dateobj);
	   //String CreationDate=(df.format(dateobj));
	    System.out.println("the current date is"+convertedPoDate);
	   // Object ApproveData[]={CreationDate,"10","11","12","13"};
	   // String emailBodyMsgTxt=String.valueOf(ApproveData);
		//System.out.println("the length of String"+ApproveData[0]+""+ApproveData[1]+""+ApproveData[2]+""+ApproveData[3]); //currentTime = 
		
		
	}


		@Override
		public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentNumber, int site_id,
				String user_id) {
			// TODO Auto-generated method stub
			return objPurchaseDepartmentIndentProcessDao.insertIndentCreationApproval(indentCreationApprovalSeqNum,indentNumber,site_id,user_id);
		}
		
		/*****************************************************reject Mail Temp Po*****************************************************************************/
		public String RejectMailTempPO(HttpSession session ,HttpServletRequest request) {

			String user_id ="";
			String ponumber =request.getParameter("strPONumber");
			String site_id =request.getParameter("siteId");
			String response = "";
			int result=0;
			String old_po_NumberStatus="";
			final int getLocalPort = request.getLocalPort(); // get port number
			String mailComments=request.getParameter("Remarks")==null ? " " : request.getParameter("Remarks");
			old_po_NumberStatus=objPurchaseDepartmentIndentProcessDao.activeOldPOTable(ponumber); // check whether Revised Or Normal Po it is Revised then set Previous NormalPo
			try {
				if(!old_po_NumberStatus.equalsIgnoreCase("success")){ // check condition which is not revised po do it
				String checkStatus=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber); // get the status of po
				String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
				String dbPasswd=getTempPOPassword(ponumber);
				 user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
				if(passwdForMail.equals(dbPasswd) && checkStatus.equals("A")){ // compare password and check whether po active or not also
					result=objPurchaseDepartmentIndentProcessDao.getDataForMailForTempPo(ponumber,site_id); // update sum_purchase_indent process tbl 
					String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);
					
					List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId); // send mail along with data
					listOfDetails.add(String.valueOf(getLocalPort));
					String subject="Your Temp Po Has been Rejected";
					sendTempPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Rejected",getLocalPort);	// mails send to previous employee
				}
				if(result > 0){
					response = "Success";
				}else{
					response = "failure";
				}
				}else if(old_po_NumberStatus.equalsIgnoreCase("success")){response = "Success";
				user_id=(request.getParameter("userId") == null ? "" : request.getParameter("userId").toString());
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(ponumber,user_id);// to send the mail for revised po rejected time
				
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(ponumber,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Temp Po Has been Rejected";
				sendTempPoMailCommonData(ponumber,mailComments,listOfDetails,subject,"Rejected",getLocalPort);
				
				}
				
				
			} catch (Exception e) {
				//System.out.println("temp po number "+ponumber);
				e.printStackTrace();
				response = "failure";
			}	
			SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
			return response;
		}

 /*********************************************************************reject Temp Mail Po********************************************************************/
	// check the password while po Approve or Reject or Cancel or Permanent Po cancel time form 
		public String getTempPOPassword(String tempPONumber) {
			return objPurchaseDepartmentIndentProcessDao.getTempPOPassword(tempPONumber,false);
		}
		
/*	*******************************************************************tempPo mail Common Method****************************************************************/	
	// send mail while rejecting normal po or Revised Po 
		public void sendTempPoMailCommonData(String temp_Po_Number,String mailComments,List<String> listOfDetails,String subject,String type,int intPortNo){
		
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
		objEmailFunction.sendMailForTempPoReject(strApproveName,approveToEmailAddress,ApproveData,subject,intPortNo);
		
	}
/*	**********************************************this is for porevised or not start*******************************************************************/	
	public String checkRevisedOrNot(String poNumber) {
		// TODO Auto-generated method stub
		return objPurchaseDepartmentIndentProcessDao.checkRevisedOrNot(poNumber);
	}
	
	
	/*	**********************************************this is for porevised or not end*******************************************************************/	
/*	***************************************************saveTranportDetails For Po********************************************************************/	
	
	private String saveTransportDetailsForPo(HttpServletRequest request,ProductDetails productDetails,String approvalEmpId,String strVendorGSTIN,String isSiteLevelPo,String receiverState) {
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
		char firstLetterChar=0;
		char secondLetterChar=0;
		String finalamount="";
		double CGSTAMT=0.0;
		double SGSTAMT=0.0;
		double IGSTAMT=0.0;
		String CGST="";String SGST="";String IGST="";
		double percent=0.0;
		double amt=0.0;
		String numOfChargeRec[] = null;
		String chargesRecordsCount =  request.getParameter("numbeOfChargesRowsToBeProcessed")== null ? "" : request.getParameter("numbeOfChargesRowsToBeProcessed").toString();
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
					if(isSiteLevelPo.equalsIgnoreCase("true")){
						
						AmountAfterTax = request.getParameter("AmountAfterTaxx"+charNum)==null ? "0" : request.getParameter("AmountAfterTaxx"+charNum);
					}else{
						AmountAfterTax = request.getParameter("AmountAfterTax"+charNum)==null ? "0" : request.getParameter("AmountAfterTax"+charNum);
					}
					TransportInvoice = request.getParameter("TransportInvoice"+charNum);
					transportChargesDto.setTransportId(ConveyanceId);
					transportChargesDto.setTransportAmount(ConveyanceAmount);
					transportChargesDto.setTransportGSTPercentage(GSTTaxId);
					transportChargesDto.setTransportGSTAmount(GSTAmount);
					transportChargesDto.setTotalAmountAfterGSTTax(AmountAfterTax);
					transportChargesDto.setTransportInvoice(TransportInvoice);
					finalamount = request.getParameter("finalAmntDiv");
					transportChargesDto.setTotalamount(finalamount);

					firstLetterChar = strVendorGSTIN.charAt(0);
					secondLetterChar=strVendorGSTIN.charAt(1);

					double doubleConveyanAmt=Double.parseDouble(ConveyanceAmount);
					doubleConveyanAmt=Double.parseDouble(new DecimalFormat("##.##").format(doubleConveyanAmt));
					AmountAfterTax=String.format("%.2f",Double.valueOf(AmountAfterTax));
					// receiver state telanaga then vendor gsti number start with 36 take cgst,sgstelse igst divide
					if(receiverState.equalsIgnoreCase("Telangana")){ //compare receiver state then vendor gstin number also check in below for local or non local 
						if (firstLetterChar=='3' && secondLetterChar=='6') {
							if (GSTTax.equals("0")) {
								CGST = "0";
								SGST = "0";
							} else {
								percent = Double.parseDouble(GSTTax)/2;
								amt = Double.parseDouble(GSTAmount)/2;
								CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								CGST = String.valueOf(percent);
								SGST = String.valueOf(percent);
							}
							//to set tranport details to table three
							strTableThreeData += ConveyanceName+"@@"+doubleConveyanAmt+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+CGSTAMT+"@@"+SGST+"%"+"@@"+SGSTAMT+"@@"+" "+"@@"+" "+"@@"+"&&";
							} else { //receive from other state 
							
							percent = Double.parseDouble(GSTTax);
							amt = Double.parseDouble(GSTAmount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							//to set table three data for tanport details
							strTableThreeData += ConveyanceName+"@@"+doubleConveyanAmt+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+IGSTAMT+"&&";	
							}

					}else{
						// vendor state karnataka then gsti start with 29 take cgst,sgst else igst take it
						if (firstLetterChar=='2' && secondLetterChar=='9') {
							if (GSTTax.equals("0")) {
								CGST = "0";
								SGST = "0";
							} else {
								percent = Double.parseDouble(GSTTax)/2;
								amt = Double.parseDouble(GSTAmount)/2;
								CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								CGST = String.valueOf(percent);
								SGST = String.valueOf(percent);
							}
							//to set data in table three
							strTableThreeData += ConveyanceName+"@@"+doubleConveyanAmt+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+CGSTAMT+"@@"+SGST+"%"+"@@"+SGSTAMT+"@@"+" "+"@@"+" "+"@@"+"&&";
							
						} else { //receive from other state 
							percent = Double.parseDouble(GSTTax);
							amt = Double.parseDouble(GSTAmount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							//to set data in table three 
							strTableThreeData += ConveyanceName+"@@"+doubleConveyanAmt+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+IGSTAMT+"&&";				
							}
						}
					int poTransChrgsSeqNo = objPurchaseDepartmentIndentProcessDao.getPoTransChrgsEntrySeqNo();
					int result2=0;
					if(approvalEmpId!=null && approvalEmpId.equals("VND")){

						objPurchaseDepartmentIndentProcessDao.insertPOTransportDetails(poTransChrgsSeqNo,productDetails,transportChargesDto);

					}else{
						result2 = objPurchaseDepartmentIndentProcessDao.insertPOTempTransportDetails(poTransChrgsSeqNo,productDetails,transportChargesDto);

					}
				
				}
			}
		}

		return strTableThreeData;
	}
	
/********************************************Transportation Details Save in Po table End**************************************************************/ 	
//*******************************************************Product Details For Po Start****************************************************************
	private String saveProductDetailsForPo(HttpSession session,HttpServletRequest request,String strVendorGSTIN,int noofrows,String approvalEmpId,String []recordscount,
			String receiverState,String oldPonumber,String strPONumber,String oldPOEntryId,int poEntryId,String isSiteLevelPo,String revisedPoForTemp) {	
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		int sno=0;
		String action="";
		String indentCreation="";
		ProductDetails productDetails =null;
		String hsnCode ="";
		String price ="";
		String basicAmount =""; 
		String taxAmount ="";
		String amountAfterTax =""; 
		String totalAmount ="";
		String quantity ="";
		String editStrQuantity ="";
		String tax ="";
		double grandtotal=0.0;
		String strGrandTotalVal="";
		String taxId="";
		String isRevision="";
		double CGSTAMT=0.0;
		double SGSTAMT=0.0;
		double IGSTAMT=0.0;
		String CGST="";String SGST="";String IGST="";
		double percent=0.0;
		double amt=0.0;
		char firstLetterChar=0;
		char secondLetterChar=0;
		String strTableTwoDate="";String strCGSTAMT="0";String strSGSTAMT="0";
		double receiveQuanPO=0.0;
		double totalAmt=0.0;
		String indentCreationDetailsId ="";
		String strIGSTAMT="0";
		String strgrandtotal="";
		String createdBy="";
		String centralDeptId = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
		String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		String strIndentNo = request.getParameter("indentNumber");
		String sessionSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
		// when the user enterd any new product it will know new product so here add created by
		if(sessionSiteId.equals(purchaseDeptId)){
			createdBy="PURCHASE";
		}else if(sessionSiteId.equals(centralDeptId) ){
			createdBy="CENTRAL";
		}
		
		for(int i=0;i<noofrows;i++){
		int	num=Integer.parseInt(recordscount[i]);
		if(noofrows!=0)
			{	
				sno++;
				String actionValue = request.getParameter("actionValue"+num);
				String product = request.getParameter("Product"+num);
				String prodsInfo[] = product.split("\\$");	
				String productId = prodsInfo[0];
				String productName = prodsInfo[1];

				String subProduct = request.getParameter("SubProduct"+num);
				String subProdsInfo[] = subProduct.split("\\$");					
				String subProductId = subProdsInfo[0];
				String subProductName = subProdsInfo[1];				

				String childProduct = request.getParameter("ChildProduct"+num);
				String childProdsInfo[] = childProduct.split("\\$");					
				String childProductId = childProdsInfo[0];
				String childProductName = childProdsInfo[1];

				String mesurement = request.getParameter("UnitsOfMeasurement"+num);
				String measureInfo[] = mesurement.split("\\$");					
				String setMeasurementId = measureInfo[0];
				String setMeasurementName = measureInfo[1];
				String groupId=request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
				String vendorProductDesc=request.getParameter("childProductVendorDesc"+num);
				if(vendorProductDesc.equals("")){
					vendorProductDesc="-";

				}
				String purchaseDepartmentIndentProcessSeqId1 = request.getParameter("purchaseDepartmentIndentProcessSeqId"+num);
				String newProductCreated=request.getParameter("isNewOrOld"+num);
				if(newProductCreated.equalsIgnoreCase("new")){ // indent creation details id saved in Approve complete purpose
					if((approvalEmpId!=null && approvalEmpId.equals("VND"))){int indentCreationDetailsSeqNum = icd.getIndentCreationDetailsSequenceNumber();
					indentCreationDetailsId=String.valueOf(indentCreationDetailsSeqNum);}else{
					indentCreationDetailsId="-";}
					indentCreation="true";

				}else{
					indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
				}
				if(isSiteLevelPo.equalsIgnoreCase("true")){
					hsnCode =request.getParameter("HSNCode"+num);
					price = request.getParameter("Price"+num);
					basicAmount = request.getParameter("BasicAmount"+num);

					taxAmount = request.getParameter("TaxAmount"+num);
					amountAfterTax = request.getParameter("AmountAfterTax"+num);
					totalAmount = request.getParameter("TotalAmount"+num);
					quantity = request.getParameter("Quantity"+num);
					tax = request.getParameter("Tax"+num);
				
				}else{
				 hsnCode = request.getParameter("hsnCode"+num);
				 price = request.getParameter("PriceId"+num);
				 basicAmount = request.getParameter("BasicAmountId"+num);
				
				 taxAmount = request.getParameter("taxAmount"+num);
				 amountAfterTax = request.getParameter("amountAfterTax"+num);
				 totalAmount = request.getParameter("totalAmount"+num);
				 quantity = request.getParameter("quantity"+num);
				  editStrQuantity = request.getParameter("strQuantity"+num);// revised po time quantity changed so we take old Quantity
				   tax = request.getParameter("tax"+num);
				}
				if(tax.contains("%")){
					taxId = tax.split("\\$")[0];
					tax = tax.split("\\$")[1].substring(0,tax.split("\\$")[1].length()-1);
				}else{
					taxId = tax.split("\\$")[0];
					tax = tax.split("\\$")[0];
				} 
				/***************************************** for convert the child product measurement start************************/
				String methodName  = validateParams.getProperty(childProductId) == null ? "" : validateParams.getProperty(childProductId).toString();
				// here checking the child product which available in properties files then 
				try{
					//methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();
					// here checking the child product which available in properties files then 
					if(!methodName.equals("")) {	
						Map<String, String> values = null;

						String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();

						//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
						Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
						Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


						double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProductId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProductId+"ActualQuantity").toString());
						double doubleInputQuantity =  Double.valueOf(quantity);
						String strConversionMesurmentId  =  validateParams.getProperty(childProductId+"ID") == null ? "" : validateParams.getProperty(childProductId+"ID").toString();
						// with multiple parameters
						//methodName = "convertCHP00536";
						Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
						Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
						System.out.println("the printdogmethod class is "+printDogMethod);
						values = (Map<String, String>) printDogMethod.invoke(mesurment,basicAmount,doubleActualQuantity,doubleInputQuantity,setMeasurementName);
						
						for(Map.Entry<String, String> retVal : values.entrySet()) {
							BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
							quantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
							//quantity=retVal.getKey();
							price=retVal.getValue(); 
						}	
						//quantity =  String.valueOf(doubleQuantity);
						/*if(measurementName.equalsIgnoreCase("PCS")){
							
							double price=Double.parseDouble(basicAmnt)/doubleQuantity;
							prc=String.valueOf(price);
						}*/
						setMeasurementId = strConversionMesurmentId;
						setMeasurementName = validateParams.getProperty(childProductId+"IDMNAME") == null ? "" : validateParams.getProperty(childProductId+"IDMNAME").toString();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				/***************************************** for convert the child product measurement end************************/
				
				String indentNo = request.getParameter("indentNumber");
				String poIntiatedQuantity = request.getParameter("poIntiatedQuantity"+num);
				String requestQuantity = request.getParameter("strRequestQuantity"+num);
				/*==========================================this is for edit po get initiatedquan & requestedQuan for Revised Po===============*/
				if((poIntiatedQuantity==null || poIntiatedQuantity.equals("")) && (requestQuantity==null || requestQuantity.equals("")) && !newProductCreated.equalsIgnoreCase("new")){

					poIntiatedQuantity=String.valueOf(objPurchaseDepartmentIndentProcessDao.getIntiatedQuantityInPurchaseTable(indentCreationDetailsId));
					requestQuantity=String.valueOf(objPurchaseDepartmentIndentProcessDao.getRequestedQuantityInPurchaseTable(indentCreationDetailsId));
					if(actionValue.equals("S")){
						action="true";
					}else if(actionValue.equals("E")){
						if(editStrQuantity != quantity){
							objPurchaseDepartmentIndentProcessDao.updateTempPOQuantityDetails(indentCreationDetailsId,String.valueOf(quantity),String.valueOf(editStrQuantity)); 	
							double resultQuantity=Double.valueOf(editStrQuantity)-Double.valueOf(quantity);	
							if(resultQuantity>0){objPurchaseDepartmentIndentProcessDao.checkIOndentCreationtbl(indentNo);}

						}
						
						isRevision="true";
					}
				}
				if((approvalEmpId!=null && approvalEmpId.equals("VND"))&&(oldPonumber!=null && !oldPonumber.equals("")) && (!newProductCreated.equalsIgnoreCase("new"))){
					String retValue=objPurchaseDepartmentIndentProcessDao.getOldPoQuantitytoRevised(oldPOEntryId,indentCreationDetailsId);	
					receiveQuanPO=Double.parseDouble(retValue);
				}
				String Discount=request.getParameter("Discount"+num);
				String amountAfterDiscount=request.getParameter("amtAfterDiscount"+num);
				String strStatus = "A";
				productDetails = new ProductDetails();
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
				productDetails.setStrPONumber(indentNo);
				productDetails.setIndentCreationDetailsId(indentCreationDetailsId);
				productDetails.setStrPoAlreadyReceivedQuantity(receiveQuanPO);
				productDetails.setCreatedBy(createdBy);
				productDetails.setGroupId(groupId);
				double doubleQuantity = 0;
				
				firstLetterChar = strVendorGSTIN.charAt(0);
				secondLetterChar=strVendorGSTIN.charAt(1);
				double totalvalue=Double.valueOf(totalAmount);
				
				totalAmt=totalAmt+totalvalue;
				totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
				int val = (int) Math.ceil(totalAmt);
				double roundoff=Math.ceil(totalAmt)-totalAmt;
				 grandtotal=Math.ceil(totalAmt);

				NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
				String strtotal = numberFormat.format(totalAmt);

				
				String strroundoff=String.format("%.2f",roundoff);
				String strTotalVal=String.format("%.2f",totalAmt);
				strGrandTotalVal=String.format("%.2f",grandtotal);
				 strgrandtotal=numberFormat.format(grandtotal);

				double doublePrice=Double.parseDouble(price);
				double doubleBasicAmount=Double.parseDouble(basicAmount);
				double doubleDiscount=Double.parseDouble(Discount);
				doublePrice=Double.parseDouble(new DecimalFormat("##.##").format(doublePrice));
				doubleBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format(doubleBasicAmount));
				String strBasicAmt=String.format("%.2f",doubleBasicAmount);
				doubleDiscount=Double.parseDouble(new DecimalFormat("##.##").format(doubleDiscount));
				quantity=String.format("%.2f",Double.valueOf(quantity));
				amountAfterTax=String.format("%.2f",Double.valueOf(amountAfterTax));
				amountAfterDiscount=String.format("%.2f",Double.valueOf(amountAfterDiscount));
				
	// check whether  receiver state start with 36 then cgst,sgst else igst
				if(receiverState.equalsIgnoreCase("Telangana")){
					if (firstLetterChar=='3' && secondLetterChar=='6') {
						request.setAttribute("isCGSTSGST","true");//this is for in jsp only show which gst local or non-local
						if (tax.equals("0")) {
							CGST = "0";
							SGST = "0";
							strCGSTAMT="0.00";
							strSGSTAMT="0.00";
						} else {
							percent = Double.parseDouble(tax)/2;
							amt = Double.parseDouble(taxAmount)/2;
							CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							SGSTAMT =Double.parseDouble(new DecimalFormat("##.##").format(amt));
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
							strCGSTAMT =String.format("%.2f",CGSTAMT);
							 strSGSTAMT =String.format("%.2f",SGSTAMT);
						}
						// to set data in table two
						strTableTwoDate += sno+"@@"+subProductName+"@@"+childProductName+"@@"+hsnCode+"@@"+setMeasurementName+"@@"+
						quantity+"@@"+doublePrice+"@@"+strBasicAmt+"@@"+doubleDiscount+"@@"+amountAfterDiscount+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountAfterTax+"@@"+amountAfterTax+"@@"+strroundoff+"@@"+strGrandTotalVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"
						+productName+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+setMeasurementId+"@@"+indentCreationDetailsId+"&&";
					
					} else {

						request.setAttribute("isCGSTSGST","false");//this is for in jsp only show which gst local or non-local
						percent = Double.parseDouble(tax);
						amt = Double.parseDouble(taxAmount);
						IGST = String.valueOf(percent);
						IGSTAMT =Double.parseDouble(new DecimalFormat("##.##").format(amt));
						strIGSTAMT=String.format("%.2f",IGSTAMT);
						// to set data in table two
						
						strTableTwoDate += sno+"@@"+subProductName+"@@"+childProductName+"@@"+hsnCode+"@@"+setMeasurementName+"@@"+
						quantity+"@@"+doublePrice+"@@"+strBasicAmt+"@@"+doubleDiscount+"@@"+amountAfterDiscount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountAfterTax+"@@"+amountAfterTax+"@@"+strroundoff+"@@"+strGrandTotalVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"
						+productName+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+setMeasurementId+"@@"+indentCreationDetailsId+"&&";
					}	
				} else{

					if (firstLetterChar=='2' && secondLetterChar=='9') {

						request.setAttribute("isCGSTSGST","true");//this is for in jsp only show  gst local or non-local

						if (tax.equals("0")) {
							CGST = "0";
							SGST = "0";
							strCGSTAMT="0";
							strSGSTAMT="0";
						} else {
							percent = Double.parseDouble(tax)/2;
							amt = Double.parseDouble(taxAmount)/2;
							CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
							strCGSTAMT =String.format("%.2f",CGSTAMT);
							 strSGSTAMT =String.format("%.2f",SGSTAMT);
						}
						// to set data for table two
						strTableTwoDate += sno+"@@"+subProductName+"@@"+childProductName+"@@"+hsnCode+"@@"+setMeasurementName+"@@"+
						quantity+"@@"+doublePrice+"@@"+strBasicAmt+"@@"+doubleDiscount+"@@"+amountAfterDiscount+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountAfterTax+"@@"+amountAfterTax+"@@"+strroundoff+"@@"+strGrandTotalVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"
						+productName+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+setMeasurementId+"@@"+indentCreationDetailsId+"&&";
						
					} else {

						request.setAttribute("isCGSTSGST","false");//this is for in jsp only show which gst local or non-local
						percent = Double.parseDouble(tax);
						amt = Double.parseDouble(taxAmount);
						IGST = String.valueOf(percent);
						IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
						strIGSTAMT=String.format("%.2f",IGSTAMT);
						
						// to set data in table two
						strTableTwoDate += sno+"@@"+subProductName+"@@"+childProductName+"@@"+hsnCode+"@@"+setMeasurementName+"@@"+
						quantity+"@@"+doublePrice+"@@"+strBasicAmt+"@@"+doubleDiscount+"@@"+amountAfterDiscount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountAfterTax+"@@"+amountAfterTax+"@@"+strroundoff+"@@"+strGrandTotalVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"
						+productName+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+setMeasurementId+"@@"+indentCreationDetailsId+"&&";
					}
				}
				int result=0;
				if(approvalEmpId!=null && approvalEmpId.equals("VND")){
					result=objPurchaseDepartmentIndentProcessDao.insertPOEntryDetails(productDetails,poEntryId);	
				}else{
					result=objPurchaseDepartmentIndentProcessDao.insertTempPOEntryDetails(productDetails,strPONumber);
				}
				if(indentCreationDetailsId != null && !indentCreationDetailsId.equals("") ){
					double intPoIntiatedQuantity = Double.valueOf(poIntiatedQuantity ==null ? "0" : poIntiatedQuantity.toString());
					double intRequestQuantity = Double.valueOf(requestQuantity ==null ? "0" : requestQuantity.toString());
					doubleQuantity = Double.valueOf(quantity ==null ? "0" : quantity.toString());
					if(intPoIntiatedQuantity+doubleQuantity >= intRequestQuantity){ // here it will taken total Quantity and inactive status 
						strStatus = "I";}
					if(result==1){
						if(indentCreation.equalsIgnoreCase("true")){
							if((approvalEmpId!=null && approvalEmpId.equals("VND"))){// perminently save indent creation table
							int purchaseIndentProcessId = cntlIndentrocss.getPurchaseIndentProcessSequenceNumber();
							if(!revisedPoForTemp.equalsIgnoreCase("true") ){ // normal po add new product then in create po page
								objPurchaseDepartmentIndentProcessDao.insertIndentCreationtbl(productDetails,strIndentNo,isSiteLevelPo);}// this is for revised po purpose 
							objPurchaseDepartmentIndentProcessDao.insertPurchaseDepttbl(purchaseIndentProcessId,sessionSiteId,strUserId,productDetails);}
						}else{
							if(!isRevision.equalsIgnoreCase("true")&& !action.equals("true")){ // normal po purchase_dept_indent_process updated
								int intIndentCreationDetailsId = Integer.parseInt(indentCreationDetailsId);
								objPurchaseDepartmentIndentProcessDao.updatePurchaseDeptIndentProcesstbl(doubleQuantity, intIndentCreationDetailsId,strStatus);}
						}
					}
				 }
			}
		}	
		request.setAttribute("pototal",strGrandTotalVal);// for payment and poentry to update total amount
		return strTableTwoDate;
	}
	//*******************************************************Product Details For Po End****************************************************************
/*********************************************************to get po Number and Revised Po start********************************************************/
	private List<String> getPoNumberForRevisedAndNormalPo(HttpSession session,HttpServletRequest request,String editPonumber,String receiverState,
			String strSiteId,String strFinacialYear) {	
	
	/******************************************RevisedPostart**********************************************************/
		List<String> poNumberList=new ArrayList<String>();
		List<String> revisedOfDetails = new ArrayList<String>();//revised details of po purpose
		String strPONumber="";
		String preparedBy="";
		String revisedOrNot="";
		String serviceState="";
		String poState="";
		String strPODate="";
		String oldPOEntryId="";
		int temprevision_no=0;
		String type="";
		if(editPonumber!=null && !editPonumber.equals("")){
		revisedOrNot="true";
		type="REVISED";
		preparedBy="PURCHASE_DEPT";
		//String strRevisionPOEntry=
		revisedOfDetails=objPurchaseDepartmentIndentProcessDao.getRevisionNumber(editPonumber);
		//for(int i=0;i<revisedOfDetails.size();i++){
		int revision_no=Integer.parseInt(revisedOfDetails.get(0));
		oldPOEntryId=revisedOfDetails.get(1);
		String strRevisedTypePurchase=revisedOfDetails.get(2);
		strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(editPonumber,"false");
			String tempPoNumber=editPonumber;
			if(tempPoNumber.contains("/R")){
				String data=tempPoNumber.split("/R")[1];
				if(data.contains("/U")){
					String data1=data.split("/U")[0];
					temprevision_no=Integer.valueOf(data1)+1;
					strPONumber=tempPoNumber.replace("R"+Integer.valueOf(data1), "R"+temprevision_no);
					//System.out.print("the split data1  "+data1);
				}else{
					temprevision_no=Integer.valueOf(data)+1;
					strPONumber=tempPoNumber.replace("R"+Integer.valueOf(data), "R"+temprevision_no);
					
				}
				
				}else{
					strPONumber=tempPoNumber+"/R"+"1";
					System.out.print("else the split data ");
				}
			/*if(tempPoNumber.contains("R")){ 
				temprevision_no=revision_no+1;
				strPONumber=tempPoNumber.replace("R"+revision_no, "R"+temprevision_no);
				
			}else{
				temprevision_no=revision_no+1;
				strPONumber=tempPoNumber+"/R"+temprevision_no;
				
			}*/if(strRevisedTypePurchase.equalsIgnoreCase("PURCHASE_DEPT")){
			String response1=getNoOfRowsForUpdatePO(request,session);}
			}
		/******************************************RevisedPoend**********************************************************/	
		else{
		preparedBy="PURCHASE_DEPT";
		type="CREATION";
		revisedOrNot="false";
		if(receiverState.equalsIgnoreCase("Telangana")){
			poState =validateParams.getProperty("PO_NUM_TELANGANA");
			strPONumber=objPurchaseDepartmentIndentProcessDao.getPermenentPoNumber(poState,preparedBy,strSiteId,strFinacialYear);
			serviceState="PO_TELANGANA";

		}else{
			poState =validateParams.getProperty("PO_NUM_KARNATAKA");
			strPONumber=objPurchaseDepartmentIndentProcessDao.getPermenentPoNumber(poState,preparedBy,strSiteId,strFinacialYear);
			serviceState="PO_KARNATAKA";
		}

		}
		poNumberList.add(strPONumber);poNumberList.add(preparedBy);poNumberList.add(revisedOrNot);poNumberList.add(poState);
		poNumberList.add(serviceState);poNumberList.add(strPODate);poNumberList.add(oldPOEntryId);poNumberList.add(String.valueOf(temprevision_no));
		poNumberList.add(type);
		//	strPONumber = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNoOrMaxId();
		
	
	return poNumberList;
}
	
	/*********************************************************to get po Number and Revised Po start********************************************************/
/*	****************************************************to get View MIS PO Report data************************************************************/
	public List<ProductDetails> getViewPoDetailsDetails(HttpServletRequest request,String fromDate,String toDate){
		return objPurchaseDepartmentIndentProcessDao.getViewPoDetailsDetails(request,fromDate,toDate);
	}
	/*	****************************************************to get View MIS MarketPurchase Report data************************************************************/
	public List<ProductDetails> getViewMarketPurchaseDetails(HttpServletRequest request,String fromDate,String toDate,String purchaseType){
		return objPurchaseDepartmentIndentProcessDao.getViewMarketPurchaseDetails(request,fromDate,toDate,purchaseType);
	}
	/*********************************************************to get MIS LocalPurchase Data*************************************************************/
	
	/*public List<ProductDetails> getViewLocalPurchaseDetails(HttpServletRequest request,String fromDate,String toDate){
		return objPurchaseDepartmentIndentProcessDao.getViewLocalPurchaseDetails(request,fromDate,toDate);
	}*/
	// to save temp po detauls for normal and revised po 
	private List<String> getAndSaveTempPoDetails(String isSiteLevelPo,String siteName,String editPonumber) {	
		String preparedBy="";String strPONumber="";
		String strPODate="";String revisedPoForTemp="false";
		List<String> poList=new ArrayList<String>();
		String type="CREATION";
		if(isSiteLevelPo.equalsIgnoreCase("true")){

			preparedBy=siteName;
		}else 
		{
			preparedBy="PURCHASE_DEPT";
		}
		int intPONumber = objPurchaseDepartmentIndentProcessDao.getTempPoEnterSeqNoOrMaxId();
		strPONumber = String.valueOf(intPONumber);
		if(editPonumber!=null && !editPonumber.equals("")){
			type="REVISED";
			strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(editPonumber,"true");//here we r using true to show po in po print page when po is revised in temporary(approve there)
			revisedPoForTemp="true";}//this is for temp revisedpo}
		poList.add(preparedBy);poList.add(strPONumber);poList.add(strPODate);poList.add(revisedPoForTemp);poList.add(type);
		return poList;
	}
	/*===========================================Vendor Payment Details start======================================================================*/
	public List<ProductDetails> getVendorPaymentDetails(HttpServletRequest request,String fromDate,String toDate){
		return objPurchaseDepartmentIndentProcessDao.getVendorPaymentDetails(request,fromDate,toDate);
	}
	/*==============================================Vendor Payment Details End=====================================================================*/	

	/*===========================================Sitelevel Po Details start======================================================================*/
	public List<ProductDetails> getSiteLevelPoDetails(HttpServletRequest request,String fromDate,String toDate){
		return objPurchaseDepartmentIndentProcessDao.getSiteLevelPoDetails(request,fromDate,toDate);
	}
	/*==============================================SiteLevel Po Details End=====================================================================*/	

	// perminent po cancel for purchase and marketing po
	public String getCancelPOPendinfEmpId(String pendingEmpId,String typeOfPo){
		return objPurchaseDepartmentIndentProcessDao.getCancelPOPendinfEmpId(pendingEmpId,typeOfPo);
	}
	public int inActivePOTable(String poNumber,String siteId){
		return objPurchaseDepartmentIndentProcessDao.inActivePOTable(poNumber,siteId);
	}
	public boolean checkPoDoneInvoiceOrDcPayment(String poNumber,String siteId,String vendorId){
		return objPurchaseDepartmentIndentProcessDao.checkPoDoneInvoiceOrDcPayment(poNumber,siteId,vendorId);
	}
	public int saveCancelPoDetailsInCancelTbl(String poentryId,String poNumber,String pendingEmpId,String status,String passwdForMail){
		return objPurchaseDepartmentIndentProcessDao.saveCancelPoDetailsInCancelTbl(poentryId,poNumber,pendingEmpId,status,passwdForMail);
	}
	public int saveCancelPoApproveDetails(String poentryId,String siteId,String userEmpId,String status,String vendorComments,String normalComments,String ccmails){
		return objPurchaseDepartmentIndentProcessDao.saveCancelPoApproveDetails(poentryId,siteId,userEmpId,status,vendorComments,normalComments,ccmails);
	}
	
	public int updateCancelPoDetailsInCancelTbl(String poentryId,String pendingEmpId,String status,String passwdForMail,boolean RejectOrNot){
		return objPurchaseDepartmentIndentProcessDao.updateCancelPoDetailsInCancelTbl(poentryId,pendingEmpId,status,passwdForMail,RejectOrNot);
	}
	public boolean checkPoPaymentDoneOrNot(String poNumber,String siteId,String vendorId,String poDate,String siteName,String poEntryId,int portNumber,boolean isUpdatePO,boolean isRevised){
		return objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(poNumber,siteId,vendorId,poDate,siteName,poEntryId,portNumber,isUpdatePO,isRevised);
	}
	
	/*************************************************this is for mail purpose write data start***************************************************/
	public void sendMailToVendorForCancelPO(HttpServletRequest request,String poentryId,String poNumber,String vendorId,String siteId,String poDate,
			String siteName,int portNumber,String ccmails){

	try{
	//List<Map<String, Object>> indentCreationDtls = null;
	List<String> toMailListArrayList = new ArrayList<String>();
	String emailto [] = null ;
	String vendoremail="";
	String contact_person_Name="";//for vendor send mail show vendor name
	String vendorLevelComments="";
	String paymentLevelComment="-";
	Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorEmail(vendorId);
	for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
	contact_person_Name=retVal.getKey();
	vendoremail=retVal.getValue(); 
	}	
	
	if(vendoremail != null &&! vendoremail.equals("")){
	if(vendoremail.contains(",")){
	for(int i=0;i<vendoremail.split(",").length;i++){
		toMailListArrayList.add(vendoremail.split(",")[i]);
	}
	}else{
	toMailListArrayList.add(vendoremail);
	}
	}
	
	
	
	emailto = new String[toMailListArrayList.size()];
	toMailListArrayList.toArray(emailto);
	
	String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
	String marketingDeptId =validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
	List<String> allEmployeeEmailsOfPD = new ArrayList<String>();
	if(!siteId.equals("112") && !siteId.equals(marketingDeptId)){
	allEmployeeEmailsOfPD = objPurchaseDepartmentIndentProcessDao.getAllEmployeeEmailsUnderDepartment(purchaseDeptId);
	}
	if(siteId.equals(marketingDeptId)){
		allEmployeeEmailsOfPD = objMarketingDepartmentIndentProcessDao.getAllEmployeeEmailsMarketingDepartment(marketingDeptId);
		}
	
	
	vendorLevelComments=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoComments(poentryId);
	if(!ccmails.equals("")&&ccmails!=null){
		String[] ccEmailToArray = ccmails.split(",");
		for(int i=0;i<ccEmailToArray.length;i++){
			allEmployeeEmailsOfPD.add(ccEmailToArray[i]);
		}
	}
	//allEmployeeEmailsOfPD.add(ccmails);
	String	mailData=objPurchaseDepartmentIndentProcessDao.getCreatedEmpIdandMail(poentryId);
	
	List<String> paymentEmail=(List<String>) request.getAttribute("emailList");
	if(paymentEmail!=null && paymentEmail.size()>0){
	for(int i=0;i<paymentEmail.size();i++){
		allEmployeeEmailsOfPD.add(paymentEmail.get(i));
	}
	paymentLevelComment="System Updated Advance Payment Converted To Security Deposite";
	}
	String ccTo [] = new String[allEmployeeEmailsOfPD.size()];
	allEmployeeEmailsOfPD.toArray(ccTo);
	
	EmailFunction objEmailFunction = new EmailFunction();
	
	
	
	objEmailFunction.sendMailForVendorCancelPo(poNumber,ccTo,contact_person_Name,poDate,siteName,vendorLevelComments,emailto,paymentLevelComment,mailData,portNumber);
	
	}catch(Exception e){
	e.printStackTrace();
	}
	}
	
	public String CancelPerminentPODetailsReject(HttpSession session,HttpServletRequest request,String poentryId,String isMail) {
		
		String viewToSelected="";
		String userName="";
		String VendorName="";
		String comments=request.getParameter("Remarks");
		String poNumber=request.getParameter("poNumber");
		String siteName=request.getParameter("siteName");
		String poDate=request.getParameter("poDate");
		String vendorId=request.getParameter("vendorId");
		String user_id=session.getAttribute("UserId")== null ? "" : session.getAttribute("UserId").toString();
		if(isMail.equalsIgnoreCase("true") || user_id==null || user_id.equals("")){ // from mail user id taken from request object
			user_id=request.getParameter("userId");
		}
		//String userId=request.getParameter("userId");
		EmailFunction objEmailFunction = new EmailFunction();
		String[] emailToAddress=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoEmails(poentryId);
		Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorNameEmail(vendorId);
		for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
			VendorName=retVal.getKey();
		}
		String userData=iid.getEmployerName(user_id);
		if(userData.contains("@@")){
			userName=userData.split("@@")[0];
		}else{userName=userData;}
		Object RejectedData[]={poNumber,poDate,siteName,"Sir","Status of PO cancellation request - Rejected.",VendorName,userName};
		int i=objPurchaseDepartmentIndentProcessDao.updateCancelPoDetailsInCancelTbl(poentryId,"VND","I","0",true);
		
		objEmailFunction.sendMailForRejectCancelPerminentPo(emailToAddress,comments,RejectedData,"rejected"); // mail send to previous employess 
		if(i>0){
			request.setAttribute("message","Permanent Cancel Po Rejected");
			viewToSelected="response";
			
		}else{
			request.setAttribute("message1","Permanent Cancel Po Not Rejected");
			viewToSelected="response";
		}
				return viewToSelected;
	}	
	
/*	*****************************************************temp mail send to permanent cancel po*********************************************************/
	public void sendMailPerminentPoCancel(HttpServletRequest request,String pendingEmpId,String poentryId,String poNumber,String vendorId,String siteId,String poDate,String siteName,String passwdForMail,String user_id) {
	
		int getLocalPort = request.getLocalPort();
		String VendorName="";
		String previousEmpComments="";
		String initiateEmpName="";
		String indentNumber=request.getParameter("indentNumber");
		String previousEmpId="";
		String poType=request.getParameter("poType");
		String poTotal=objPurchaseDepartmentIndentProcessDao.getTempPoTotalAmt("VND",Integer.valueOf(poentryId)); // here we need use old methode for temp and perminent table so use vnd
		//String poTotal=request.getParameter("poTotal");
		String approveEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(pendingEmpId);
		String comments=request.getParameter("normalComment");
		
		String data[] = approveEmail.split(",");
		String strApproveName=data[0];
		String approveToEmailAddress[]={data[1]};
		previousEmpId=objPurchaseDepartmentIndentProcessDao.previousEmpId(poentryId);
		String userData=iid.getEmployerName(user_id);
		if(userData.contains("@@")){
			initiateEmpName=userData.split("@@")[0];
		}else{initiateEmpName=userData;}
		
		Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorNameEmail(vendorId);
		for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
			VendorName=retVal.getKey();
		}	
		String vendorData=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoInternalComments(poentryId);
		/*for(int i=0;i<vendorData.size();i++){
			previousEmpComments +=" "+(i+1)+") "+vendorData.get(i);
		}
		*/
		Object ApproveData[]={poNumber,poDate,siteName,poTotal,siteId,passwdForMail,poentryId,pendingEmpId,vendorId,indentNumber,VendorName,vendorData,initiateEmpName,poType};
		EmailFunction objEmailFunction = new EmailFunction();
		objEmailFunction.sendMailForPerminentPOCancelApprove(strApproveName,approveToEmailAddress,getLocalPort,ApproveData);
		//Object RejectedData[]={poNumber,poDate,siteName,userName};
		//objEmailFunction.sendMailForRejectCancelPerminentPo(emailToAddress,comments,RejectedData);
		
		
	}
	public boolean checkAlreadyCancelOrnot(String poNumber) {
		return objPurchaseDepartmentIndentProcessDao.checkAlreadyCancelOrnot(poNumber);
	 
	}
	public String getPoQuantityIndentCreationDetailsForCancelPermanentPo(String indentNumber,String poentryId,String poNumber){
		
		return objPurchaseDepartmentIndentProcessDao.getPoQuantityIndentCreationDetailsForCancelPermanentPo(indentNumber,poentryId,poNumber);
		 
	}
	
	// send mail for previous employee
	public void sendPreviousApproveEmpMail(String poentryId,String poNumber,String poDate,String siteName,String previous_EmpId,String vendorId,String userId) {
		String userName="";
		String previousEmpComments="";
		String VendorName="";
		String approveUserName="";
		String userData=iid.getEmployerName(previous_EmpId);
		String approveUserData=iid.getEmployerName(userId);
		if(userData.contains("@@")){
			userName=userData.split("@@")[0];
		}else{userName=userData;}
		
		if(approveUserData.contains("@@")){
			approveUserName=approveUserData.split("@@")[0];
		}else{approveUserName=approveUserData;}
		
		String nameEmail=objPurchaseDepartmentIndentProcessDao.getPoCreatedEmpName(previous_EmpId);
		String data[] = nameEmail.split(",");
		//String strEmpName=data[0];
		String strEmpEmailId[]={data[1]};
		//String strMobiloeNumber=data[2];
		//String[] emailToAddress=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoEmails(poentryId);
		
		String vendorData=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoInternalComments(poentryId);
		/*for(int i=0;i<vendorData.size();i++){
			previousEmpComments +=" "+(i+1)+")  "+vendorData.get(i);
		}*/
		Map<String, String> vendordata =(Map<String, String>) objPurchaseDepartmentIndentProcessDao.getVendorNameEmail(vendorId);
		for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
			VendorName=retVal.getKey();
		}	
		Object RejectedData[]={poNumber,poDate,siteName,userName,"Status of PO cancellation request - Approved.",VendorName,approveUserName};
		EmailFunction objEmailFunction = new EmailFunction();
		objEmailFunction.sendMailForRejectCancelPerminentPo(strEmpEmailId,vendorData,RejectedData,"Approved");
		
		
	}
	public boolean  updatePaymentDtls(HttpServletRequest request,String poNumber,String siteId,String vendorId){
		return objPurchaseDepartmentIndentProcessDao.updatePaymentDtls(request,poNumber,siteId,vendorId);
	}
	
	@Override
	public List<Map<String, Object>> getListOfCancelPoShow(String site_id) {
		return objPurchaseDepartmentIndentProcessDao.getListOfCancelPoShow(site_id);
	}
	public List<Map<String, Object>> getListOfActivePOForCancelPermanentPOs(String site_id,String fromdate,String todate,String poNumber){
		return objPurchaseDepartmentIndentProcessDao.getListOfActivePOForCancelPermanentPOs(site_id,fromdate,todate,poNumber);
	}
/*================================================get cancel po status start============================*/
	
	public List<IndentCreationBean> ViewandGetCancelPo(String fromDate, String toDate,String PoNumber){
		return objPurchaseDepartmentIndentProcessDao.ViewandGetCancelPo( fromDate,toDate,PoNumber);
	}

	
	
  /* ====================================================get Cancel Po status End=============================*/
	public List<String> getCancelPoData(String poNumber,String siteId){
		return objPurchaseDepartmentIndentProcessDao.getCancelPoData(poNumber,siteId);
	}


	@Override
	public boolean isThisPOGoingToBeCanceled(String strPoEntryId) {
		return objPurchaseDepartmentIndentProcessDao.isThisPOGoingToBeCanceled(strPoEntryId);
	}


	@Override
	public List<String> getTempPoConclusions(String poNumber) {
		return objPurchaseDepartmentIndentProcessDao.getTempPoConclusions(poNumber);
	}
/*=========================================vendor ccmails written this on start===================================================*/
	@Override
	public String getVendorCCEmails(String site_id) {
		String ccEmail ="";
		String state = objPurchaseDepartmentIndentProcessDao.getStateforSendEnquiry(site_id);
		//String usermaildId=objPurchaseDepartmentIndentProcessDao.getUserMailIds(user_id);
		state = state.toUpperCase();

		String strCCEmails="";
		//int count =0;
		if(state.equalsIgnoreCase("HYD")){
			ccEmail = validateParams.getProperty("VENDOR_CC_MAILS_TELANAGANA")==null ? "" : validateParams.getProperty("VENDOR_CC_MAILS_TELANAGANA");
		}else if(state.equalsIgnoreCase("BANGLORE")){
			ccEmail = validateParams.getProperty("VENDOR_CC_MAILS_KARNATAKA")==null ? "" :validateParams.getProperty("VENDOR_CC_MAILS_KARNATAKA");
		}
			if(ccEmail!=null){}
				strCCEmails =strCCEmails + ccEmail + ",";
		
		if(!strCCEmails.equals("")){strCCEmails = strCCEmails.substring(0,strCCEmails.length()-1);}
		return strCCEmails;
	}	
	/*=====================================================vendor ccmails written this end=================================================*/
	
	/*=======================================================update po Approvals Start======================================================== */
	@Override
	public String SaveUpdatePoApproveDetails(final String tempPONumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPO) {

		
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="failed";

		 //indentnumber="";
		String permPoNumber="";
		int val=0;
		String approvalEmpId = "";
		String state="";
		String sessionSite_id="";
		int intPoEntrySeqId =0;
		String revision_No="";
		String old_Po_Number="";
		 //siteLevelPoPreparedBy="";
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
		String siteLevelPoPreparedBy=request.getParameter("siteLevelPoPreparedBy") == null ? "-" : request.getParameter("siteLevelPoPreparedBy").toString();
		//String strIndentNo=request.getParameter("strIndentNo") == null ? "-" : request.getParameter("strIndentNo").toString();
		String indentnumber=request.getParameter("strIndentNo") == null ? "" : request.getParameter("strIndentNo").toString();
		revision_No=request.getParameter("revision_No") == null ? "" : request.getParameter("revision_No").toString();	
		old_Po_Number=request.getParameter("oldPoNumber") == null ? "-" : request.getParameter("oldPoNumber").toString();//to get revised po number
		//String mailPasswd=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
		String deliveryDate=request.getParameter("deliveryDate") == null ? "" : request.getParameter("deliveryDate").toString();
		String oldPOEntryId=request.getParameter("oldPOEntryId") == null ? "" : request.getParameter("oldPOEntryId").toString();
		String oldPODate=request.getParameter("oldPODate") == null ? "" : request.getParameter("oldPODate").toString();
		String strVendorName=request.getParameter("strVendorName") == null ? "" : request.getParameter("strVendorName").toString();
		String vendorId=request.getParameter("vendorId") == null ? "" : request.getParameter("vendorId").toString();
		
		String remarks=request.getParameter("note") == null ? "": request.getParameter("note").toString();
		String note="";
		//String dbPasswd=getTempPOPassword(tempPONumber);
		String checkval="";
		//String revisedOrNot="";
		String strState="";
		//String acc_Note_Email="";
		int LocalPort = request.getLocalPort();
		 List<ProductDetails> SuccessDataListToMail = new ArrayList<ProductDetails>();
		 boolean ispaymentUpdate=false;
		 boolean paymentStatus=false;
		 double paidAmount =0.0;
		// List<String> allEmployeeEmailsOfAcc=new ArrayList<String>();
		 //request.setAttribute("oldPODate",oldPODate); // this is used to set the current date for po date and creation date 
		if(LocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
			rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();}

		request.setAttribute("indentnumber",indentnumber);// this is using in the compare purpose to quantity in revised po in getAndsavePendingProductDetails
		paymentStatus=objPurchaseDepartmentIndentProcessDao.checkPoPaymentDoneOrNot(old_Po_Number,siteId,vendorId,oldPODate,siteName,oldPOEntryId,tempGetLocalPort,true,false);
		if(paymentStatus){
			request.setAttribute("message1","Sorry ! Unable to update PO, as payment initiated.");
			//transactionManager.rollback(status);
			//WriteTrHistory.write("Tr_Completed");
			return result="response";
		}

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in PDIn_savPOA, ");

		try{
			String strCheckTotal=objPurchaseDepartmentIndentProcessDao.getTempPoTotalAmt(userId,Integer.parseInt(tempPONumber)); // to check below condition purpose we take total amount 
			 approvalEmpId=objPurchaseDepartmentIndentProcessDao.getCancelPOPendinfEmpId(userId,"UPDATE_PO"); 
		
			 checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(tempPONumber); // get value of active or inactive
			 SuccessDataListToMail=objPurchaseDepartmentIndentProcessDao.gettingProductsForUpdatePO(indentnumber,oldPOEntryId,tempPONumber);
			 //String cancel=objPurchaseDepartmentIndentProcessDao.getCancelOrNot(tempPONumber);// whether po is cancel or not 
			 HttpSession session = request.getSession(true);
			 strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			 //strUserId=request.getParameter("userId");
			 if(checkval.equalsIgnoreCase("A") && !approvalEmpId.equals("VND")){ // THIS IS FOR MAIL PURPOSE 
			
				//user Approve from mail it is check condition session useridnot taken so take from request object				
			if((strUserId==null || strUserId.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){strUserId=request.getParameter("userId");}
				
			if(approvalEmpId!=null && !approvalEmpId.equals("") && !approvalEmpId.equals("VND")){
				WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber);
				val=objPurchaseDepartmentIndentProcessDao.updateTempPoEntry(approvalEmpId,tempPONumber,ccmailId,siteLevelPoPreparedBy,indentnumber,passwdForMail,deliveryDate);
				objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,"0",strUserId, siteId, "A",remarks);
				/*	*******************************************ForMail*******************************************************/
					
				// if user click on approve button then send mail also to next approve Person
					
					
					List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(tempPONumber,approvalEmpId);
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
					// send mail to next approval person
					approveToEmailAddress = new String[toMailListArrayList.size()];
					toMailListArrayList.toArray(approveToEmailAddress);
					
					 Object ApproveData[]={indentnumber,approvalEmpId,siteId,old_Po_Number,listOfDetails.get(4),tempPONumber,listOfDetails.get(3),
								listOfDetails.get(6),listOfDetails.get(2),listOfDetails.get(5),listOfDetails.get(0),siteLevelPoPreparedBy,passwdForMail,String.valueOf(tempGetLocalPort),
								oldPOEntryId,deliveryDate,ccmailId,vendorId};
					 
					 EmailFunction objEmailFunction = new EmailFunction();
					 objEmailFunction.sendUpdatePOApproval(SuccessDataListToMail,approveToEmailAddress,tempGetLocalPort,old_Po_Number,oldPODate,listOfDetails.get(6),strApproveName,listOfDetails.get(2),ApproveData);
					
			
		/**************************************************For Mail End *******************************************************************/

				// display message after approve button clicked
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
			else{ // if approval from mail then it will check condition userId not getting from session so take from request object
				if(strUserId.equals("")){strUserId=request.getParameter("userId");} // from mail userId empty then take from request object

				if(checkval.equals("A")){
					request.setAttribute("userId",userId);// for get product details to show indent creaation details id
					objPurchaseDepartmentIndentProcessDao.getAndsaveVendorUPdatePoDetails(tempPONumber, siteId,userId,request,revision_No,old_Po_Number,siteLevelPoPreparedBy,siteName,deliveryDate,oldPODate);
					permPoNumber=request.getAttribute("permentPoNumber").toString();
					request.setAttribute("siteLevelPoPreparedBy",siteLevelPoPreparedBy); // this is used in getandsave vendor details page
					request.setAttribute("oldPOEntryId",oldPOEntryId);
					WriteTrHistory.write("Site:"+siteId+" , User:"+userId+" , Date:"+new java.util.Date()+" , TempPONumber:"+tempPONumber+" , PermanentPONumber:"+permPoNumber);

				//state=request.getAttribute("State").toString();
				intPoEntrySeqId = Integer.parseInt(request.getAttribute("poEntrySeqID") == null ? "0" : request.getAttribute("poEntrySeqID").toString());
				objPurchaseDepartmentIndentProcessDao.gettermsconditions(tempPONumber,String.valueOf(intPoEntrySeqId));
				
				List<String> conclusions = objPurchaseDepartmentIndentProcessDao.getTempPoConclusions(tempPONumber);
				objPurchaseDepartmentIndentProcessDao.insertPoConclusions(conclusions,String.valueOf(intPoEntrySeqId),tempPONumber,indentnumber);
				
				objPurchaseDepartmentIndentProcessDao.getAndsavePendingProductDetails(tempPONumber,siteId,request,permPoNumber,intPoEntrySeqId,old_Po_Number);
				
				objPurchaseDepartmentIndentProcessDao.getAndsavePendingTransportChargesList(tempPONumber,siteId,request,intPoEntrySeqId);
				result="success";
				request.setAttribute("result", "Temp Po approved Successfully and PO Number is : "+permPoNumber);
				request.setAttribute("viewPoPageData", objviewPOPageDataMap);
				//revisedOrNot=request.getAttribute("RevisedOrNot").toString();// this is for pdf purpose to revised or not
				strState=request.getAttribute("state").toString();
				if(result.equals("success")){

					String filepath=rootFilePath+"TEMP_PO//";
					//String strPO_Number=poNumber.replace('/','$');
					for (int i = 0; i < 8; i++) {
					File file = new File(filepath+tempPONumber+"_Part"+i+".pdf");	
					File img = new File(filepath+tempPONumber+"_Part"+i+".jpg");
					//File file = new File(filepath+tempPONumber+".pdf");
					long count=file.length();
					if(file.exists()){
					String strpermPoNumber=permPoNumber.replace('/','$');
					Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".pdf")); //this is for pdf purpose to move temp po to pemenent po
					}
					if(img.exists()){
						String strpermPoNumber=permPoNumber.replace('/','$');
						Files.move(Paths.get(moveFilePath+"\\TEMP_PO\\"+tempPONumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\PO\\"+strpermPoNumber+"_Part"+i+".jpg")); //this is for pdf purpose to move temp po to pemenent po
						}
					
					}
					sessionSite_id=request.getAttribute("sessionSite_id").toString(); // temp po entry details inactive after do this 

					//String vendorId = "";
					String totalAmt=request.getAttribute("totalAmt").toString(); // use this to update in acc dept table so take this
					//vendorId = objPurchaseDepartmentIndentProcessDao.getVendorDetails(tempPONumber, siteId,userId,request);
				if(old_Po_Number!=null && !old_Po_Number.equals("-") && !old_Po_Number.equals("")){
					//below statment: update po to update in account dept table,indent entry,dc_entry tables 
			/*********************************************revised po done at the time acc dept send mail*******************************************/
					int intResult=0;
					 paidAmount =objPurchaseDepartmentIndentProcessDao.updateAdvancePaidAmount(request,old_Po_Number,siteId,vendorId,Double.valueOf(totalAmt));
					intResult=objPurchaseDepartmentIndentProcessDao.updateAccPayment(old_Po_Number,permPoNumber,totalAmt,true);
					if(paidAmount>Double.valueOf(totalAmt)){ // paid amount is more than the total amount then check condition and  trigger the mail
						 allEmployeeEmailsOfAcc=(List<String>) request.getAttribute("emailList");
						 note="System had updated Paid amount, same as amount in Updated PO.";
						ispaymentUpdate=true;
					}
					objPurchaseDepartmentIndentProcessDao.updateIndentAndDcPONumber(old_Po_Number,permPoNumber,totalAmt);

					}
					
			/*********************************************revised po done at the time acc dept send mail*******************************************/		
					objPurchaseDepartmentIndentProcessDao.updateTotalAmt(totalAmt,approvalEmpId,permPoNumber); // after revising po or normal po amount  it will updated to table
					objPurchaseDepartmentIndentProcessDao.insertTempPOorPOCreateApproverDtls(tempPONumber,String.valueOf(intPoEntrySeqId),strUserId, siteId, "A",remarks);
					// inactive temp po entry details table
					int i=objPurchaseDepartmentIndentProcessDao.updatepoEntrydetails(tempPONumber,indentnumber,siteId,userId,sessionSite_id,siteLevelPoPreparedBy,passwdForMail,deliveryDate);
					String ccEmailId = request.getParameter("ccEmailId2")== null ? "" : request.getParameter("ccEmailId2").toString();
					if(!ccEmailId.equals("")){
						if(ccEmailId.charAt(ccEmailId.length() - 1)==','){
							ccEmailId = ccEmailId.substring(0,ccEmailId.length()-1);
						}
						if(ccEmailId.charAt(0)=='-'){
							ccEmailId = ccEmailId.substring(1,ccEmailId.length());
						}
					}
					final String ccEmailTo = ccEmailId;
					final int getLocalPort = request.getLocalPort();
					//final List<String> allEmployeeEmailsOfAcc1=allEmployeeEmailsOfAcc;
					//final String RevsiedpoNote=acc_Note_Email;
					if(ccEmailTo != null &&! ccEmailTo.equals("")){
						if(ccEmailTo.contains(",")){
							for(int k=0;k<ccEmailTo.split(",").length;k++){
								toMailListArrayList.add(ccEmailTo.split(",")[k]);
							}
						}else{
							toMailListArrayList.add(ccEmailTo);
						}
					}
					if(ispaymentUpdate){
						for(int j=0;j<allEmployeeEmailsOfAcc.size();j++){
							toMailListArrayList.add(allEmployeeEmailsOfAcc.get(i));
						}
					}
					String emailto [] = new String[toMailListArrayList.size()];
					toMailListArrayList.toArray(emailto);
					
					EmailFunction objEmailFunction = new EmailFunction();
					 Object ApproveData[]={old_Po_Number,oldPODate,permPoNumber,strVendorName,String.valueOf(getLocalPort),note};
					objEmailFunction.sendMailForUpdatePo(SuccessDataListToMail,ApproveData,emailto);
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

			auditBean.setOperationName("update Receive");
			auditBean.setStatus("error");

			e.printStackTrace();
		}
		return result;
	}
	/*===========================================getting the acc dept table mails start==========================================================*/
	private List<String> gettingaccMailsForUpdatePo(String receiverState){
		List<String> accMails=null;
		List<String> allEmployeeEmailsOfAcc1 = new ArrayList<String>();
	if(receiverState.equalsIgnoreCase("Telangana")){
		 accMails=objPurchaseDepartmentIndentProcessDao.getAccDeptEmails("997_H_");
	}else{accMails=objPurchaseDepartmentIndentProcessDao.getAccDeptEmails("997_B_");}
	if(accMails.size()>0){
		for(int i=0;i<accMails.size();i++){
			allEmployeeEmailsOfAcc1.add(accMails.get(i));
		}
		}
	return allEmployeeEmailsOfAcc1;
	}
	/*=============================================getting the acc dept table mails end ========================================================*/
	// ravi written this methode
	// common methode for create po/sitelevel po/revised po/updatepo/ getcancell po pages 
	@Override
	public String getAndCheckPOBOQQuantity(HttpSession session,HttpServletRequest request,String siteId,boolean approveOrNot,boolean isCancelled) {

		
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null ? request.getParameter("numberOfRows")==null ? "" : request.getParameter("numberOfRows") : request.getParameter("numbeOfRowsToBeProcessed");
		//String val=request.getParameter("numberOfRows")==null ? "" : request.getParameter("numberOfRows");
		String childProduct="";
		double receivedQuantity=0.0;
		double issuedQuantity=0.0;
		//double indentPendingQuantity=0.0;
		double totalBOQQuantity=0.0;
		String groupId="0";
		double enteredQuantity=0.0;
		String numOfRec[] = null;
		String childProdId ="";
		String childProdName ="";
		
		double totalQuantity=0.0;
		String response="";
		Map<String,Double> groupIdsSortMap = new HashMap<String,Double>();
		Map<String,String> childSortMap = new HashMap<String,String>();
		Map<String,String> childProductMap = new HashMap<String,String>();
		String measurement="";
		//String strGroupId="";
		double tempQuantity = 0.0;
		String measurementId="";
		String measurementName="";
		double poPendingQuantity=0.0;
		double oldQuantity=0.0;
		String ponumber=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
		/*if(isCancelled){
			numOfRec = new 	String[Integer.parseInt(recordsCount)];
			for(int k=0;k<Integer.parseInt(recordsCount);k++){
			numOfRec [k]=String.valueOf(k+1);
			}
		//	Arrays.toString(numOfRec);
		}*/
		if((recordsCount != null) && (!recordsCount.equals("") /*&& !isCancelled*/)) {
			numOfRec = recordsCount.split("\\|");
		}
		
		if(numOfRec != null && numOfRec.length > 0) {
			for(String num : numOfRec) {
				num = num.trim();
				String childProductList="";
				String measurementList="";
				String actionValue = request.getParameter("actionValue"+num)==null ? "" : request.getParameter("actionValue"+num);
				childProduct=request.getParameter("ChildProduct"+num)==null ? "" : request.getParameter("ChildProduct"+num);
				measurement=request.getParameter("UnitsOfMeasurement"+num)==null ? "" : request.getParameter("UnitsOfMeasurement"+num);
				childProdId = childProduct.split("\\$")[0];
				childProdName= childProduct.split("\\$")[1];
				measurementId=measurement.split("\\$")[0];
				measurementName=measurement.split("\\$")[1];
				 if(!actionValue.equals("R") || !actionValue.equals("D")){
					 groupId=request.getParameter("groupId"+num)==null ? "0" : request.getParameter("groupId"+num);
					 oldQuantity=Double.valueOf(request.getParameter("oldQuantity"+num)==null ? "0" : request.getParameter("oldQuantity"+num));
					 enteredQuantity=Double.valueOf(request.getParameter("quantity"+num)==null ? request.getParameter("Quantity"+num)==null ? "" : request.getParameter("Quantity"+num) : request.getParameter("quantity"+num));
				 }
				if(!groupId.equals("0") && groupId!=null){
					//			/childProductList
					if(!childProductMap.containsKey(childProdId)){
					childSortMap=icd.getChildProductsWithGroupId(groupId);
					for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
						childProductList += entry.getKey() +",";
						measurementList += entry.getValue() +",";
						
					}
					if(childProductList!=null && !childProductList.equals("")){
						childProductList =  childProductList.substring(0,childProductList.length()-1);
					}if(measurementList!=null && !measurementList.equals("")){
						measurementList =  measurementList.substring(0,measurementList.length()-1);
					}
						receivedQuantity=icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList);
						issuedQuantity=icd.getIssuedQuantity(childProductList,siteId,measurementList);
						//indentPendingQuantity=icd.getIndentPendingQuantity(childProductList,siteId);
						poPendingQuantity=objPurchaseDepartmentIndentProcessDao.getPOPendingQuantity(childProductList,siteId,measurementList);
						totalBOQQuantity=icd.getBOQQuantity(groupId,siteId);
						for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
							childProductMap.put(entry.getKey(),receivedQuantity+"_"+issuedQuantity+"_"+poPendingQuantity+"_"+totalBOQQuantity);
						}
						
					}else{
						String temp_Quantity =(childProductMap.get(childProdId));
						String info[]=temp_Quantity.split("_");
						 receivedQuantity=Double.valueOf(info[0]);
						 issuedQuantity=Double.valueOf(info[1]);
						 poPendingQuantity=Double.valueOf(info[2]);
						 totalBOQQuantity=Double.valueOf(info[3]);
					}
					if(!groupIdsSortMap.containsKey(groupId)){
						
						if(oldQuantity!=enteredQuantity && oldQuantity!=0){
							double checkQuantity=oldQuantity-enteredQuantity;
							poPendingQuantity=poPendingQuantity-checkQuantity;
							groupIdsSortMap.put(groupId,((checkQuantity)));
							}else if(oldQuantity==0 || (oldQuantity==enteredQuantity && approveOrNot)){
							poPendingQuantity+=enteredQuantity;//-checkQuantity;
							groupIdsSortMap.put(groupId,(enteredQuantity));
							}
						//tempQuantity=enteredQuantity; // curretly entered quantity
					}else{
						if(oldQuantity!=enteredQuantity && oldQuantity!=0){
							double value=0.0;
							double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
							double checkQuantity=oldQuantity-enteredQuantity;
							value=DoublePrevQuantity+checkQuantity;
							groupIdsSortMap.put(groupId,(value));
							poPendingQuantity=poPendingQuantity-value;
						}else if(oldQuantity==0 || (oldQuantity==enteredQuantity && approveOrNot)){
							double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
							tempQuantity = Double.valueOf(enteredQuantity)+DoublePrevQuantity; // currently entered quantity
							poPendingQuantity+=tempQuantity;
							groupIdsSortMap.put(groupId,(tempQuantity));}
					}
					totalQuantity=receivedQuantity-(issuedQuantity)+poPendingQuantity;//<=totalBOQQuantity;
					if(totalQuantity<=totalBOQQuantity){
						continue;
					}else{
						response="You can not initiate Child Product \""+childProdName+"\" more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
						//request.setAttribute("message1",response);
						break;
					}
				}
				
			}
		}
		return response;
	}
	/*==================================================== Approval time we need to get the data and check start====================================*/ 
	@Override
	public String getAndCheckApprovalPOBOQQuantity(HttpSession session,HttpServletRequest request) {
		List<Map<String, Object>> getTemPoProductDetailsList = null;
		Map<String,String> groupIdsSortMap = new HashMap<String,String>();
		Map<String,String> childSortMap = new HashMap<String,String>();
		Map<String,String> childProductMap = new HashMap<String,String>();
		String childProductId="";
		String measurementId="";
		String childProductName="";
		String measurementName="";
		String groupId="";
		double receivedQuantity=0.0;
		double issuedQuantity=0.0;
		//double indentPendingQuantity=0.0;
		double totalBOQQuantity=0.0;
		
		double tempQuantity=0.0;
		double totalQuantity=0.0;
		double poPendingQuantity=0.0;
		String response="";
		double enteredQuantity=0.0;
		String poNumber=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
		String siteId=request.getParameter("siteId")==null ? "" : request.getParameter("siteId");
		String allSitesOrNot=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		if(!allSitesOrNot.contains(siteId)){
			getTemPoProductDetailsList=objPurchaseDepartmentIndentProcessDao.getAndCheckApprovalPOBOQQuantity(poNumber,siteId);
			for(Map<String, Object> prods : getTemPoProductDetailsList) {
				String childProductList="";
				String measurementList="";
				childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				measurementId=prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString();
				childProductName=prods.get("CHILDPRODUCTNAME")==null ? "" :   prods.get("CHILDPRODUCTNAME").toString();
				measurementName=prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString();
				groupId=prods.get("MATERIAL_GROUP_ID")==null ? "0" :   prods.get("MATERIAL_GROUP_ID").toString();
				enteredQuantity=Double.valueOf(prods.get("PO_QTY")==null ? "" :   prods.get("PO_QTY").toString());
				if(!groupId.equals("0") && groupId!=null && !groupId.equals("")){
					//			/childProductList
					if(!childProductMap.containsKey(childProductId)){
					childSortMap=icd.getChildProductsWithGroupId(groupId);
					for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
						childProductList += entry.getKey() +",";
						measurementList += entry.getValue() +",";
					}
					if(childProductList!=null && !childProductList.equals("")){
						childProductList =  childProductList.substring(0,childProductList.length()-1);
					}if(measurementList!=null && !measurementList.equals("")){
						measurementList =  measurementList.substring(0,measurementList.length()-1);
					}
						receivedQuantity=icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList);
						issuedQuantity=icd.getIssuedQuantity(childProductList,siteId,measurementList);
						poPendingQuantity=objPurchaseDepartmentIndentProcessDao.getPOPendingQuantity(childProductList,siteId,measurementList);
						totalBOQQuantity=icd.getBOQQuantity(groupId,siteId);
						for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
							childProductMap.put(entry.getKey(),receivedQuantity+"_"+issuedQuantity+"_"+poPendingQuantity+"_"+totalBOQQuantity);
						}
					}
					else{
						String temp_Quantity =(childProductMap.get(childProductId));
						String info[]=temp_Quantity.split("_");
						 receivedQuantity=Double.valueOf(info[0]);
						 issuedQuantity=Double.valueOf(info[1]);
						 poPendingQuantity=Double.valueOf(info[2]);
						 totalBOQQuantity=Double.valueOf(info[3]);
					}
					if(!groupIdsSortMap.containsKey(groupId)){
						groupIdsSortMap.put(groupId,String.valueOf(enteredQuantity));
						tempQuantity=enteredQuantity; // curretly entered quantity
					}else{
						double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
						tempQuantity = Double.valueOf(enteredQuantity)+DoublePrevQuantity; // currently entered quantity
						groupIdsSortMap.put(groupId, String.valueOf(tempQuantity));
					}
					totalQuantity=receivedQuantity-(issuedQuantity)+poPendingQuantity+tempQuantity;//<=totalBOQQuantity;
					if(totalQuantity<=totalBOQQuantity){
						continue;
					}else{
						response="You can not initiate Child Product \""+childProductName+"\" more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
						//request.setAttribute("message1",response);
						break;
					}
				}
		
		}
		}
		return response;
	}
	/*======================================= check the condition for the in ajax call start=============================================*/
	
	@Override
	public String gettingPOBoqQuantityAjax(String groupId,String siteId) {
		Map<String,String> childSortMap = new HashMap<String,String>();
		double receivedQuantity=0.0;
		double issuedQuantity=0.0;
		double poPendingQuantity=0.0;
		double totalBOQQuantity=0.0;
		double resultQuantity=0.0;
		String childProductList="";
		String measurementList="";
		StringBuffer totalData=new StringBuffer();
		childSortMap=icd.getChildProductsWithGroupId(groupId);
		for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
			childProductList += entry.getKey() +",";
			measurementList += entry.getValue() +",";
		}
		
		if(childProductList!=null && !childProductList.equals("")){
			childProductList =  childProductList.substring(0,childProductList.length()-1);
		}if(measurementList!=null && !measurementList.equals("")){
			measurementList =  measurementList.substring(0,measurementList.length()-1);
		}
		 receivedQuantity=(icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList));
		 issuedQuantity=(icd.getIssuedQuantity(childProductList,siteId,measurementList));
		 //indentPendingQuantity=(icd.getIndentPendingQuantity(childProductList,siteId,measurementList));
		 poPendingQuantity=objPurchaseDepartmentIndentProcessDao.getPOPendingQuantity(childProductList,siteId,measurementList);
		 totalBOQQuantity=(icd.getBOQQuantity(groupId,siteId));
		 resultQuantity=totalBOQQuantity-(receivedQuantity-issuedQuantity+poPendingQuantity);
		 if(resultQuantity<0){ resultQuantity=0;}
		 totalData=totalData.append(resultQuantity+"_"+totalBOQQuantity);
		return totalData.toString();
	}
	
	/*==================================================== this is for check the is without update or revised start=================================*/
	public boolean checkIsUpdateOrNot(HttpSession session,HttpServletRequest request,boolean isRevised,boolean isModifyPo) {
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
		
		String recordscount[]=recordcount.split("\\|");
		
		if((imagesAlreadyexisted != imagesAlreadyPresent || pdfAlreadyExised !=pdfAlreadyPresent)){
			return false;
		}
		
		if(!isRevised){
			for(int i=0;i<noofRows;i++){
				int	num=Integer.parseInt(recordscount[i]);
				double quantity =Double.valueOf(request.getParameter("quantity"+num)==null ? "0" :request.getParameter("quantity"+num));
				double editStrQuantity =Double.valueOf(request.getParameter("strQuantity"+num)==null ? "0" :request.getParameter("strQuantity"+num));// revised po time quantity changed so we take old Quantity
				String oldhsnCode=request.getParameter("oldhsnCode"+num)==null ? "" : request.getParameter("oldhsnCode"+num);
				String hsnCode=request.getParameter("hsnCode"+num)==null ? "" : request.getParameter("hsnCode"+num);
				if(quantity!=editStrQuantity || !ccEmailId.equalsIgnoreCase(oldccmailId) || (poTotal!=oldPOTotal) || !hsnCode.equals(oldhsnCode)){
					status=false;
					break;
				}
			}
		}
		else{
			
			if(!deliveryDate.equals(oldDeliveryDate) || !noOfDays.equals(oldNoOfDays) || (poTotal!=oldPOTotal) || !ccEmailId.equals(oldccmailId) || !subject.equals(strSubject)){
				return false;
			}
			else{
				String[] terms=request.getParameterValues("termsAndCond");
				String[] oldterms=request.getParameterValues("termsAndCondold");
				String[] conclusionsArray=request.getParameterValues("conclusionDesc");
				String[] conclusionsOldArray=request.getParameterValues("conclusionDescold");

				if((conclusionsArray!=null && !conclusionsArray[0].equals("")) && !isModifyPo){
					return false;}
				// this is for modify temp po purposenwritten 
				if((conclusionsArray!=null && !conclusionsArray[0].equals("")) || (conclusionsOldArray!=null && !conclusionsOldArray[0].equals("")) && isModifyPo){
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
				}
				// end of modify po
				int length=(terms.length);
				int oldLength=oldterms.length;
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

		}
		return status;
	}
	/*================================== for checking the vendor available or not in view quotation start================================*/
	@Override
	public boolean getQuatationDetailsAndCheck(String indentNumber,String vendorId,String siteId) {
		return objPurchaseDepartmentIndentProcessDao.getQuatationDetailsAndCheck(indentNumber,vendorId,siteId);
	}
	
	public boolean checkConditionForIncreaseDecreaseQuantity(HttpServletRequest request) {
		
		
		boolean status=true;
		double increase_Quantity=Double.valueOf(request.getParameter("increase_Quantity") == null ? "0" : request.getParameter("increase_Quantity").toString());
		double decrease_Quantity=Double.valueOf(request.getParameter("decrease_Quantity") == null ? "0" : request.getParameter("decrease_Quantity").toString());
		int noofRows = Integer.parseInt(request.getParameter("numberOfRows"));
		String recordcount = request.getParameter("numbeOfRowsToBeProcessed");
		String recordscount[]=recordcount.split("\\|");
		String childProductname="";
		for(int i=0;i<noofRows;i++){
			double oldQuantity=0.0;
			//ProductDetails productDetailsToMail = new ProductDetails();
			int	num=Integer.parseInt(recordscount[i]);
			String childProduct = request.getParameter("ChildProduct"+num)==null ? "" : request.getParameter("ChildProduct"+num);
			if(childProduct.contains("$")){
				childProductname=childProduct.split("\\$")[1];
			}else{
				childProductname=childProduct;
			}
			double quantity =Double.valueOf(request.getParameter("quantity"+num)==null ? "0" : request.getParameter("quantity"+num));
			double editStrQuantity =Double.valueOf(request.getParameter("strQuantity"+num)==null ? "0" : request.getParameter("strQuantity"+num));// revised po time quantity changed so we take old Quantity
			if(quantity!=editStrQuantity){
			if(editStrQuantity<quantity){
				oldQuantity=editStrQuantity*(increase_Quantity/100);
				oldQuantity=oldQuantity+editStrQuantity;
				if(oldQuantity<quantity){
					request.setAttribute("message1","\""+childProductname+"\" Unable to update more than required quantity.");
					status=false;
					break;
				}
				
			}else{
				oldQuantity=editStrQuantity*(decrease_Quantity/100);
				oldQuantity=editStrQuantity-oldQuantity;
				if(oldQuantity>quantity){
					request.setAttribute("message1","\""+childProductname+"\" Unable to update less than required quantity.");
					status=false;
					break;
				}
			}
			}
			
		}
		return status;

	}
	@Override
	public List<IndentCreationBean> ViewPoPendingforApprovalForMarketingHead(String fromDate, String toDate, String strUserId,String tempPoNumber,String AllOrNot, String multiplePendingEmpForQuery){
		List<IndentCreationBean> list = null;
		try{
			list = objPurchaseDepartmentIndentProcessDao.ViewPoPendingforApprovalForMarketingHead( fromDate,  toDate,  strUserId,tempPoNumber,AllOrNot, multiplePendingEmpForQuery);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
