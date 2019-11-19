package com.sumadhura.in;


import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


//import com.itextpdf.text.pdf.codec.Base64;


import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.IndentReceiveBean;

import com.sumadhura.bean.MarketingDeptBean;

import com.sumadhura.bean.ProductDetails;

import com.sumadhura.service.DCFormService;

import com.sumadhura.service.IndentCreationService;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.MarketingDepartmentService;
import com.sumadhura.service.PurchaseDepartmentIndentrocessService;
import com.sumadhura.transdao.IndentCreationDao;

import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.transdao.UtilDao;


import com.sumadhura.util.AESDecrypt;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.transdao.MarketingDepartmentDao;


@Controller
public class MarketingDeptController extends UIProperties {

	public @interface EnableAutoConfiguration {

	}

	@Autowired
	@Qualifier("dcFormClass")
	DCFormService dcFormService;

	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;

	@Autowired
	@Qualifier("purchaseDeptIndentrocess")
	PurchaseDepartmentIndentrocessService objPurchaseDeptIndentrocess;

	@Autowired
	@Qualifier("MarketingDepartmentService")
	MarketingDepartmentService objMarketingDeptService;

	@Autowired
	@Qualifier("MarketingDepartmentDao")
	MarketingDepartmentDao objMarketingDepartmentIndentProcessDao;


	@Autowired
	@Qualifier("posClass")
	IndentCreationService ics;



	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;

	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;
	
	@Autowired
	private UtilDao utilDao;

	/************************************************************************ Marketing Po *****************************/

	/*@RequestMapping(value = "/marketingCreatePoPage.spring", method = {RequestMethod.POST, RequestMethod.GET })
	public String createPoPage(Model model, HttpServletRequest request,HttpSession session) {

		String response = "";
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		Map<String, String> siteDetails = null;
		try {

			model.addAttribute("indentReceiveModelForm",new IndentReceiveBean());
			model.addAttribute("productsMap", irs.loadProds());
			model.addAttribute("columnHeadersMap",ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", irs.getGSTSlabs());
			model.addAttribute("chargesMap", irs.getOtherCharges());
			model.addAttribute("siteName", iis.getProjectName(session));
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails", siteDetails);

			List<ProductDetails> listTermsAndCondition = objPurchaseDeptIndentrocess.getTermsAndConditions(site_id);
			model.addAttribute("listTermsAndCondition", listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());
			String ccEmails = objPurchaseDeptIndentrocess.getDefaultCCEmails(site_id);
			model.addAttribute("ccEmails",ccEmails);

		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0", user_id, "Marketing PO Page Show",response,String.valueOf(site_id));

		return "Marketing_Create_PO";
	}*/

	// save marketing details
	@RequestMapping(value = "/SaveMarketingPoDetails.spring",  method ={RequestMethod.POST,RequestMethod.GET})
	public String SaveSiteLevelPoDetails(Model model,HttpServletRequest request, HttpSession session,
			@RequestParam(value="file",required=false) MultipartFile[] files) { // files parameter used to take the files and save

		int site_id = 0;
		String user_id = "";
		String response = "";
		String versionNo = "";
		String refferenceNo = "";
		String strPoPrintRefdate = "";
		String strValue = "";
		String filePath = "";
		String po_Number = "1";
		int portNumber = request.getLocalPort();
		String rootFilePath = "";
		String serverFile = "";
		int pdfCount=0;
		int imgCount=0;
		String type="";
		try {
			if (portNumber == 80) {
				rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "": validateParams.getProperty("UPLOAD_PDF").toString();
			} else {
				rootFilePath = validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "": validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			}
			String strPONumber = session.getAttribute("sessionPoNumber")==null ? "" :session.getAttribute("sessionPoNumber").toString();
			if(strPONumber!=null && !strPONumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return strValue="response";
			}
			
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "": session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
			Calendar cal = Calendar.getInstance();
			String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());
			String strFinacialYear = "";
			if (currentMonth <= 3) {
				strFinacialYear = (currentYear - 1) + "-"+ Integer.parseInt(currentYearYY);
			} else {
				strFinacialYear = currentYear + "-"+ (Integer.parseInt(currentYearYY) + 1);
			}

			versionNo = validateParams.getProperty("PO_versionNo");
			refferenceNo = validateParams.getProperty("PO_Refference");
			strPoPrintRefdate = validateParams.getProperty("PO_issuedate");

			model.addAttribute("versionNo", versionNo);
			model.addAttribute("refferenceNo", refferenceNo);
			model.addAttribute("strPoPrintRefdate", strPoPrintRefdate);
			request.setAttribute("poPage", "true");
			model.addAttribute("vendorMail",true);
			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm",objProductDetails);
			strValue = objMarketingDeptService.SaveMarketingPoDetails(model,request, session, strFinacialYear);
			String poNumber = request.getAttribute("strPONumber").toString();
			model.addAttribute("poNumber", poNumber);
			model.addAttribute("site_idForPriceMaster",site_id);
			
			//request.getAttribute("isTempPoOrNot").toString();
			if (request.getAttribute("isTempPoOrNot").toString().equalsIgnoreCase("true")) {
				model.addAttribute("tempPOOrNot",true);
				filePath = rootFilePath + "TEMP_PO//";
				po_Number = poNumber;
				type="TEMP_PO";
			} else {
				model.addAttribute("tempPOOrNot",false);
				filePath = rootFilePath + "PO//";
				po_Number = poNumber.replace('/', '$');
				type="PO";
			}
			// String message = "";

			for (int i = 0, j = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					File dir = new File(filePath +File.separator);
					if (!dir.exists())
						dir.mkdirs();
					if (file.getOriginalFilename().endsWith(".pdf")) {
						serverFile = (dir.getAbsolutePath() + File.separator+ po_Number + "_Part" + pdfCount + ".pdf");
						pdfCount++;
					} else {
						serverFile = dir.getAbsolutePath() + File.separator+ po_Number + "_Part" + imgCount + ".jpg";
						//file.transferTo(new File(serverFile));
						imgCount++;
					}
					File convFile = new File(serverFile);
					convFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(file.getBytes());
					fos.close();

				}
			}
			PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filePath,po_Number,type,model,request); // call the getting pdf and images for showing purpose
			session.setAttribute("sessionPoNumber",poNumber);
			/************************************************* retrive pdf start *****************************************************************/
			//loadVendorImgAndPdfFiles(filePath, po_Number, model, request); // for display pdf or images purpose written this one
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0", user_id, "Marketing PO Created",response, String.valueOf(site_id));
		return strValue;

	}

	/*	====================================================get siteWise Locations===================================================================*/
	@RequestMapping(value = "/loadAndSetSiteNameInfo", method = RequestMethod.GET)
	@ResponseBody
	public String loadAndSetSiteInfo(@RequestParam("siteName") String siteName) {

		String siteData = objMarketingDeptService.loadAndSetSiteInfo(siteName);
		//System.out.println("In controller resulted data :"+empData+"|");
		return siteData;
	}

	/*	==========================================================show temp Po details start==================================================================*/
	@EnableAutoConfiguration
	@RequestMapping(value = "/getDetailsforMarketingPoApproval.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getDetailsforPoApproval(HttpServletRequest request, HttpSession session,Model model,HttpServletResponse response){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		String strNewSiteId = "";
		String isRequestFromEmailFunction="";
		String Mailpasswd=""; //this is for mail purpose
		String fromDate="";
		String toDate="";

		int portNumber=request.getLocalPort();
		String rootFilePath =""; 
		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			strNewSiteId = request.getParameter("siteId");
			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber").toString();
			siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString();
			//isRequestFromEmailFunction=request.getParameter("mail") == null ? "" : request.getParameter("mail").toString();
			Mailpasswd=request.getParameter("password") == null ? "0" : request.getParameter("password").toString();
			fromDate=request.getParameter("fromdate") == null ? "" : request.getParameter("fromdate").toString();
			toDate=request.getParameter("toDate") == null || request.getParameter("toDate").equalsIgnoreCase("null") ? "" : request.getParameter("toDate").toString();

			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response1=objMarketingDeptService.getDetailsforPoApproval(poNumber, siteId,request);
				String ccEmails = objPurchaseDeptIndentrocess.getTempPoCCEmails(poNumber);

				model.addAttribute("ccEmails",ccEmails);
				model.addAttribute("url","ViewPoPendingforApproval.spring");
				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();

				model.addAttribute("site_idForPriceMaster", siteId);
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				model.addAttribute("response",true);
				model.addAttribute("poNumber",poNumber);// for po pdf download
				model.addAttribute("mailPasswd",Mailpasswd); //mail password prevous per not open
				model.addAttribute("fromDate",fromDate); // from date
				model.addAttribute("toDate",toDate); // for todate
				String filepath=rootFilePath+"TEMP_PO//";
				/*	******************************************file download**********************************************************************/
				PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"TEMP_PO",model,request); // call the getting pdf and images for showing purpose
				//loadVendorImgAndPdfFiles(filepath, poNumber, model, request);

				/*************************************************file download end************************************************************************/		
				if(response1.equalsIgnoreCase("Success")) {

					viewToBeSelected = "marketing/Marketing_Appoval_PO";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}
	/*	==========================================================show temp Po Approve details end==================================================================*/

	/************************************************************************************ sample page end ******************************************************/

	/*=================================================show only temp po===================================================================*/

	@RequestMapping(value = "/ViewMarketingTempPoPageShow.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String ViewTempPoPageShow(HttpServletRequest request, HttpSession session,Model model){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		//Calendar cal = Calendar.getInstance();
		String site_Id="";
		//int strCount=0;
		int portNumber=request.getLocalPort();
		String rootFilePath="";
		String fromDate="";
		String toDate="";
		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			site_Id=request.getParameter("siteId");
			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			fromDate=request.getParameter("fromdate") == null ? "" : request.getParameter("fromdate").toString();
			toDate=request.getParameter("toDate") == null ? "" : request.getParameter("toDate").toString();
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				//	String response=ics.getTempPoDetailsList(poNumber, site_Id,request);
				String response=objMarketingDeptService.getDetailsforPoApproval(poNumber,site_Id,request);
				//request.setAttribute("viewTempPO","true");
				String strFinacialYear =request.getAttribute("strFinacialYear").toString();
				versionNo = validateParams.getProperty("PO_"+strFinacialYear+"_versionNo");
				refferenceNo = validateParams.getProperty("PO_"+strFinacialYear+"_Refference");
				strPoPrintRefdate = validateParams.getProperty("PO_"+strFinacialYear+"_issuedate");

				/*	******************************************file download**********************************************************************/
				String filepath=rootFilePath+"TEMP_PO//";
				PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"TEMP_PO",model,request); // call the getting pdf and images for showing purpose
				//loadVendorImgAndPdfFiles(filepath, poNumber, model, request);

				/*************************************************file download end************************************************************************/		
				// get and set the data version,refference,strporefference number
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				model.addAttribute("response",false);
				model.addAttribute("fromDate",fromDate);
				model.addAttribute("toDate",toDate);

				System.out.println("poNumber "+poNumber);

				if(response.equalsIgnoreCase("Success")) {

					viewToBeSelected = "marketing/Marketing_Appoval_PO";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}

	/*=========================================================show perminent po details start=======================================================*/
	// from ims tool user click on the po print page it will execute below method
	@RequestMapping(value = "/getMarketingPoDetailsList.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getPoDetailsList(HttpServletRequest request, HttpSession session,Model model){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		boolean imageClick = false;
		String site_Id="";
		int portNumber=request.getLocalPort();
		String rootFilePath ="";
		String poentryId="";
		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}


			site_Id=request.getParameter("siteId");


			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			imageClick = request.getParameter("imageClick") == null ? false : Boolean.valueOf(request.getParameter("imageClick"));
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){

				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=objMarketingDeptService.getMarketingPoDetailsList(poNumber, site_Id,request);

				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				poentryId = request.getAttribute("poentryId").toString();
				model.addAttribute("vendorMail",true);
				request.setAttribute("PoLevelComments",ics.getPoLevelComments(poentryId,"true"));

				model.addAttribute("site_idForPriceMaster", site_Id);
				request.setAttribute("poPage","true");
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				request.setAttribute("poPage","true");
				model.addAttribute("poNumber",poNumber);
				/***********************************for pdf purpose****************************************************/

				//String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString(); 
				String filepath=rootFilePath+"PO//";
				String strPO_Number=poNumber.replace('/','$');
				PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
				//loadVendorImgAndPdfFiles(filepath,strPO_Number,model, request); // getting the attachment i.e pdf,images
				if(response.equalsIgnoreCase("Success")) { //in this below condition used for home button show or not check there

					request.setAttribute("imageClick",imageClick);
					viewToBeSelected = "marketing/Marketing_POPrintPage";

				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}

	// save for approve data in 

	@RequestMapping(value = "/SaveMarketingPoApproveDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public synchronized String SaveMarketingPoApproveDetails(Model model, HttpServletRequest request,HttpSession session) {
		List<IndentCreationBean> indentgetData = null;
		String sessionSite_id ="";
		String user_id = "";
		String response = "";
		String ponumber="";
		String siteId="";
		String strMessage = "";
		String viewToSelected="";
		String checkActiveOrNot="";
		String pendingEmpId="";
		String fromDate="";
		String toDate="";
		boolean ismailOk=true;
		String tempPoNumber="";
		try {
			ponumber=request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber");
			String checkMail=request.getParameter("mail")==null ? "" : request.getParameter("mail");
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if(user_id.equals("")){
				user_id=request.getParameter("userId") == null ? "" :request.getParameter("userId").toString();}
			int portNumber=request.getLocalPort();
			List<String> multiplePendingEmpList = new ArrayList<String>();
			boolean MarketingHead = false;
			if(portNumber==80){//LIVE
				if(user_id.equals("M008")){
					MarketingHead = true;
					multiplePendingEmpList.add("M008"); //Srinivas Moramchetty	VP-Sales and Marketing, Arun	Vice president operatoins
					multiplePendingEmpList.add("HYDMKT2");
				}//('M008','HYDMKT2')
			}else{
				if(user_id.equals("BMKT3.1")){
					MarketingHead = true;
					multiplePendingEmpList.add("BMKT3.1"); 
					multiplePendingEmpList.add("HMKT2.1");
				}
			}
			
			if(ponumber!=null && !ponumber.equals("")){
			checkActiveOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
			pendingEmpId=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(ponumber,true);
			}
			if(!checkActiveOrNot.equals("A") || (!user_id.equals(pendingEmpId) && !user_id.equals("BMKT3.1") && !user_id.equals(""))){
				model.addAttribute("response1","Already Approved or Modified or Cancelled");
				return "response";
			}
			if(!checkActiveOrNot.equals("A") && !checkActiveOrNot.equalsIgnoreCase("CANCEL") && !(checkMail.equalsIgnoreCase("true")) || ponumber==null || ponumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			siteId=request.getParameter("siteId");
			ProductDetails objProductDetails = new ProductDetails();
			String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();

			fromDate=request.getParameter("fromdate")==null ? "" : request.getParameter("fromdate").toString();
			toDate=request.getParameter("toDate")==null ? "" :request.getParameter("fromdate").toString();
			
			session = request.getSession(true);
			sessionSite_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			model.addAttribute("urlForActivateSubModule","ViewPoPendingforApproval.spring");
			
			//user_id=request.getParameter("userId");
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){
				
				user_id=request.getParameter("userId");
				
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);
				if(passwdForMail.equals(dbPasswd)){
					
					model.addAttribute("CreatePOModelForm", objProductDetails);
					request.setAttribute("sessionSite_id",sessionSite_id);
					boolean empSameOrNot = false;
					if(MarketingHead){
						empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNotForMarketingHead(user_id,ponumber,multiplePendingEmpList);
					}
					else{
						empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,ponumber);
					}
					if(empSameOrNot){
					response=objMarketingDeptService.SaveMarketingPoApproveDetails(ponumber,siteId,user_id,request,"false");}
					
					// permPoNumber=request.getAttribute("permentPoNumber").toString();
					strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
					if(response=="success"){

						model.addAttribute("response",strMessage);
						viewToSelected="response";
					}
					else{
						model.addAttribute("response1",strMessage);

					}

					
				}else{
					model.addAttribute("response1","Already Approved or Modified or Cancelled");
					viewToSelected="response";
				}
				
			}else{
			//String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);

		    if(user_id.equals("")){
		    ismailOk=false;
			user_id=request.getParameter("userId");}
			model.addAttribute("CreatePOModelForm", objProductDetails);
			request.setAttribute("sessionSite_id",sessionSite_id);
			boolean empSameOrNot = false;
			if(MarketingHead){
				empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNotForMarketingHead(user_id,ponumber,multiplePendingEmpList);
			}else{
				empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,ponumber);
			}
			if(empSameOrNot){
			response=objMarketingDeptService.SaveMarketingPoApproveDetails(ponumber,siteId,user_id,request,"false");}
			model.addAttribute("url","ViewPoPendingforApproval.spring");
			// permPoNumber=request.getAttribute("permentPoNumber").toString();
			strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
			if(ismailOk)
			{if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || (fromDate==null && toDate==null) || fromDate.equals("") && toDate.equals("")){//tempPoNumber=ponumber;
			fromDate="";toDate="";
			indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,tempPoNumber,"All");
			}else{
			indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,tempPoNumber,""); }//}
			request.setAttribute("indentgetData",indentgetData);
			request.setAttribute("PendingPoDetails",indentgetData);
			request.setAttribute("fromDate",fromDate);
			request.setAttribute("toDate", toDate);
			if(indentgetData.size()>0){
			request.setAttribute("showGrid", "true");}
			request.setAttribute("tempPoNumber","");
			}else{viewToSelected="response"; }
			if(response=="success"){

				model.addAttribute("message",strMessage);
				viewToSelected="PendingPoApproval";
				
			}
			else{
				model.addAttribute("response1",strMessage);
				viewToSelected="response";
				if(!empSameOrNot){
					request.setAttribute("message","");
					model.addAttribute("message1","Already Approved or Modified or Cancelled");
					viewToSelected="PendingPoApproval";
				}
			}

			}
			//}else{

			//}

			/*}else{
			if(user_id.equals("")){
			user_id=request.getParameter("userId");}
			model.addAttribute("CreatePOModelForm", objProductDetails);
			request.setAttribute("sessionSite_id",sessionSite_id);
			response=objPurchaseDeptIndentrocess.SavePoApproveDetails(ponumber,siteId,user_id,request,"false");
			// permPoNumber=request.getAttribute("permentPoNumber").toString();
			strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
			if(response=="success"){

				model.addAttribute("response",strMessage);

			}
			else{
				model.addAttribute("response1",strMessage);

			}


		}*/
		}	
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"save Marketing PO Approval ",response,String.valueOf(sessionSite_id));
		return viewToSelected;

	}


	/****************************************************** pdf and images download purpose start *****************************************************************/
	public static void loadVendorImgAndPdfFiles(String filePath,String po_Number, Model model, HttpServletRequest request) {
		try {
			int imageCount = 0;
			int pdfCount = 0;
			DataInputStream dis = null;

			//String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "": validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
			for (int i = 0; i < 8; i++) {
				// File dir = new File(rootFilePath+siteId+"\\"+siteWiseNO);
				File f = new File(filePath + po_Number +"_Part" + i + ".jpg");
				File imageFile = new File(filePath + po_Number + "_Part" + i + ".jpg");
				String pdfFileName = filePath + po_Number+ "_Part" + i + ".pdf";
				File pdfFile = new File(filePath + po_Number + "_Part" + i + ".pdf");
				pdfFileName = pdfFile.toString();
				if (pdfFile.exists()) {
					System.out.println(pdfFile);
					pdfCount++;
					try {
						dis = new DataInputStream(new FileInputStream(pdfFile));
						String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "$");
						byte[] barray = new byte[(int) pdfFile.length()];
						dis.readFully(barray);
						byte[] encodeBase64 = Base64.encodeBase64(barray);
						String base64Encoded = new String(encodeBase64, "UTF-8");
						String encoded = CommonUtilities.encodeFileToBase64Binary(pdfFileName);
						model.addAttribute("pdf" + i, encoded);
						model.addAttribute("PathdeletePdf" + i,pathForDeleteFile);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							dis.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}

				if (imageFile.exists()) {
					System.out.println("f.getName() " + imageFile.getName());
					imageCount++;
					try {
						dis = new DataInputStream(new FileInputStream(imageFile));
						String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "$");
						byte[] barray = new byte[(int) imageFile.length()];
						dis.readFully(barray);
						byte[] encodeBase64 = Base64.encodeBase64(barray);
						String base64Encoded = new String(encodeBase64, "UTF-8");
						model.addAttribute("image" + i, base64Encoded);
						model.addAttribute("delete" + i, pathForDeleteFile);

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							dis.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}

				}
			}
			request.setAttribute("imagecount", imageCount);
			request.setAttribute("pdfcount", pdfCount);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/MarketingInwardsfromCreatePO.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String MarketingInwardsfromCreatePO(Model model, HttpServletRequest request,HttpSession session) {
		ProductDetails objProductDetails = new ProductDetails();
		final StringBuffer xmlData = new StringBuffer();
		//StringBuffer bufferLocationdata = new StringBuffer();
		String bufferLocationdata="";
		model.addAttribute("CreatePOModelForm", objProductDetails);

		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		//model.addAttribute("productsMap", irs.loadProds());
		model.addAttribute("gstMap", irs.getGSTSlabs());
		model.addAttribute("chargesMap", irs.getOtherCharges());
		Set <String> locationName =objMarketingDepartmentIndentProcessDao.getLocationDetails(); // site wise location details
		request.setAttribute("siteLocationDetails", locationName);
		xmlData.append("<xml>");
		xmlData.append("<locationname>");
		for(String stock : locationName){
			xmlData.append("<area>" + stock + "</area>");
			System.out.println(stock);
		}
		xmlData.append("</locationname>");
		xmlData.append("</xml>");
		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj;
		try {
			xmlJSONObj = XML.toJSONObject(xmlData.toString());
			bufferLocationdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("xml data"+bufferLocationdata);

		return "marketing/MarketingInwardsfromCreatePO";
	}

	@RequestMapping(value = "/loadAndSetLocationData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> loadAndSetLocationData(@RequestParam("childProductId") String childProductId) {

		Map<String, String> areaData = objMarketingDeptService.loadAndSetLocationData(childProductId);
		//System.out.println("In controller resulted data :"+empData+"|");
		return areaData;
	}

	@RequestMapping(value = "/loadAndSetMarketingLocationData", method = RequestMethod.GET)
	@ResponseBody
	public String loadAndSetMarketingLocationData() {

		//String siteData = objMarketingDeptIndentrocess.loadAndSetLocationDatachildProductId);
		//System.out.println("In controller resulted data :"+empData+"|");
		return CommonUtilities.loadAndSetMarketingLocationData();
	}

	/*====================================================vendor show mail details=============================================================*/
	@RequestMapping(value = "/showMarketingPODetailsToVendor.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String showPODetailsToVendor(HttpServletRequest request, HttpSession session,Model model){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		String strResponse="";
		String vendorId="";
		String siteName="";
		int portNumber=request.getLocalPort();
		String rootFilePath ="";  

		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			strResponse=request.getParameter("submitButton")== null ? "0" : request.getParameter("submitButton");
			if(strResponse.equalsIgnoreCase("true")){

				poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
				siteId = request.getParameter("indentSiteId") == null ? "" : request.getParameter("indentSiteId").toString();

			}
			else{
				AESDecrypt decrypt=new AESDecrypt();
				String strData = request.getParameter("data");

				strData = strData.replace(" ","+");
				String strDecrypt =decrypt.decrypt("AMARAVADHIS12345",strData);

				String strPoNumber[] = null;
				String strSiteId[] = null;
				String strSiteName[]=null;

				if(strDecrypt.contains("&")){
					String data[] = strDecrypt.split("&");

					poNumber = data[0];
					siteId=data[1];
					strPoNumber = poNumber.split("=");
					strSiteId=siteId.split("=");
					strSiteName=siteName.split("=");
					poNumber=strPoNumber[1];
					siteId=strSiteId[1];
				}

			}
			// here vendor open an po at that time that one is revised or permanent cancel po or normal po checking here
			String checkRevisedOrNot=objPurchaseDeptIndentrocess.checkRevisedOrNot(poNumber);
			if(checkRevisedOrNot.contains("@@@")){
				String data[] = checkRevisedOrNot.split("@@@");
				String status=data[0];
				String revised=data[1];
				model.addAttribute("message","Revised Po Number :"+revised);
				viewToBeSelected="response";
			}else if(checkRevisedOrNot.contentEquals("CNL")){ // check this condition when the user click on the after cancelled po it will check and execute
				List<String> list=objPurchaseDeptIndentrocess.getCancelPoData(poNumber,siteId);
				model.addAttribute("message2","Purchase Order from Sumadhura Infracon Pvt Lmtd with reference number " +poNumber+ " Dated :"+list.get(0)
						+" of our Project "+list.get(1)+" had been cancelled.");
				model.addAttribute("response2","REMARKS :"+list.get(2));
				model.addAttribute("iscancel",true);
				
				viewToBeSelected="response";
			} // normal flow execute with below condition 
			else if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber) && !checkRevisedOrNot.contentEquals("CANCEL_PO")){

				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=objMarketingDeptService.getMarketingPoDetailsList(poNumber,siteId,request);

				String strFinacialYear =request.getAttribute("strFinacialYear").toString();

				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				//request.setAttribute("PoLevelComments","");// this is for vendor not show child product comments (created and approve emploees given)

				request.setAttribute("poPage","false");
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);

				/********************************************************this is for pdf file download purpose start********************************************/			
				model.addAttribute("poNumber",poNumber);
				model.addAttribute("vendorMail",false);
				String filepath=rootFilePath+"PO//";
				String strPO_Number=poNumber.replace('/','$'); // this is for convert $ to / in po
				PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
				//loadVendorImgAndPdfFiles(filepath,strPO_Number,model,request); // get and show the attachments so call this method
				System.out.println("poNumber "+poNumber);
				/********************************************************this is for pdf file download purpose end********************************************/			

				if(response.equalsIgnoreCase("Success")) {

					viewToBeSelected = "marketing/Marketing_POPrintPage";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}

	/*==========================================set area details================================================*/

	@RequestMapping(value = "/loadAndSetAreaData", method = RequestMethod.GET)
	@ResponseBody
	public String loadAndSetAreaData(@RequestParam("childProductId") String childProductId) {

		//String siteData = objMarketingDeptService.loadAndSetLocationData(childProductId);
		//System.out.println("In controller resulted data :"+empData+"|");
		return CommonUtilities.loadAndSetMarketingAreaData(childProductId);
	}



	@RequestMapping(value = "/getLocationData.spring", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getLocationData(@RequestParam("site") String site) {
		Map<String, String> areaData = objMarketingDeptService.getLocationData(site);
		return areaData;
	}
	@RequestMapping(value = "/brandingWiseDetails.spring", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> brandingWiseDetails(HttpServletRequest request, HttpSession session) {
		Map<String, String> siteDetails = null;

		return new IndentSummaryDao().getSiteDetails();

	}
	@RequestMapping(value = "/ViewExpenditure.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String ViewExpenditure(Model model, HttpServletRequest request,HttpSession session) {
		
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
		Map<String, String> siteDetails = null;
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean()); 
		siteDetails = new IndentSummaryDao().getSiteDetails();// get all active site names with ids to show in view expenditure sub module click on that
		List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(marketingDeptId);
		request.setAttribute("siteDetails",siteDetails);
		request.setAttribute("totalProductsList", totalProductList);
		
		model.addAttribute("AvailableDateOrNot","");
		return "marketing/ViewExpenditure";
	}

	/*
	 * This method is useful to get the all ViewExpenditures based on the invoiceid,fromdate,todate. 
	 */
	@RequestMapping(value = "/getAllViewExpenditures.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getAllViewExpenditures(Model model, HttpServletRequest request,HttpSession session) {

		String invoiceId  = request.getParameter("invoiceid");
		String invoiceToDate = request.getParameter("datepickerto");
		String invoiceFromDate = request.getParameter("datepickerfrom");
		String vendorId=request.getParameter("VendorId");
		String invoiceDate=request.getParameter("invoiceDate");
		String siteIds=request.getParameter("siteIds"); // here we need to take which site user can selected that one check and take here
		LinkedList expListObj = new LinkedList();
		String strnvoiceDateHiddenId  = request.getParameter("invoiceDateHiddenId");
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		if(strnvoiceDateHiddenId != null && !strnvoiceDateHiddenId.equals("")){ // here we need to take update expenditure time month taken month and year  
			strnvoiceDateHiddenId  =  strnvoiceDateHiddenId.substring(3,10);
		}
		
		
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		
		Map<String, String> siteDetails = null;
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		siteDetails = new IndentSummaryDao().getSiteDetails(); // here also taken sites same page using here for input given dates and data shown in that page if user refresh then show the sites
		request.setAttribute("siteDetails",siteDetails);
		
		logger.info("***** The input invoiceid is---->"+invoiceId);
		logger.info("***** The input invoiceFromDate is---->"+invoiceFromDate);
		logger.info("***** The input invoiceToDate is---->"+invoiceToDate);

		String hiddenFormField = request.getParameter("referer");
// this is call and get the Expenditure Id to use in update Expenditure time
		expListObj = objMarketingDeptService.getAllViewExpenditures(invoiceId, invoiceToDate, invoiceFromDate );

		double totalAmount = 0.0;


		if(invoiceId != "") {
			if(expListObj.size()>0){
			logger.info("**** The expListObj is *****"+expListObj);

			Map<Double,List<MarketingDeptBean>> getAllExpendaturesMap = objMarketingDeptService.getAllViewExpendituresWithInvoiceData(invoiceId,vendorId,invoiceDate,"","","","","","");//objMarketingDeptService.getAllViewExpenditures(invoiceId);
			logger.info("**** The control is inside the invoiceId block *****");
			logger.info("**** The marketingPOProductDetails list in MarketingController *******"+getAllExpendaturesMap);
			Iterator itr = getAllExpendaturesMap.keySet().iterator(); // here we need to get total amount by using key
			if(itr.hasNext()) {
				totalAmount =(Double) itr.next();
				
			}
	
			List<MarketingDeptBean> expendaturesList =  getAllExpendaturesMap.get(totalAmount);
			//getAllExpendaturesMap.get(totalAmount).
			//String hiddenInvoiceDate=expendaturesList.get(2).toString();
			model.addAttribute("expendaturesList", expendaturesList);
			BigDecimal bigDecimalTotalAmount = new BigDecimal(totalAmount); // total amount come in decimals more than 2 then it will set to two decimals after that
			model.addAttribute("totalAmount",format.format(Double.valueOf(String.valueOf(bigDecimalTotalAmount.setScale(2,RoundingMode.CEILING)))));
			//model.addAttribute("totalAmount", totalAmount);
			if(getAllExpendaturesMap.size()>0 && totalAmount!=0){
			model.addAttribute("expendatureId", expListObj.getLast()); // if it is contain expenditure id then set and use in update expenditure time
			model.addAttribute("invoiceId", invoiceId);
			model.addAttribute("isShowAll", true);
			model.addAttribute("message",""); // if user click on the submit button no data found then it will show error message so here taken empty
			model.addAttribute("message1","");
			//model.addAttribute("strInvoiceDateHiddenId", strnvoiceDateHiddenId);
			}else{
				model.addAttribute("message","No Data Available");
				model.addAttribute("message1","");
			}
			}else{
				model.addAttribute("message","Invalid Invoice Number");
				model.addAttribute("message1","");
			}
			
		}else if(invoiceDate != "" || vendorId != "" && invoiceToDate == "" && invoiceFromDate == "" && invoiceToDate == "") {
			List<MarketingDeptBean> marketingPOProductDetails  = objMarketingDeptService.getAllViewExpendituresForVendorName(invoiceId,vendorId,invoiceDate);
			logger.info("**** The control is inside the invoiceFromDate & invoiceToDate block *****");
			logger.info("**** The marketingPOProductDetails list in MarketingController *******"+marketingPOProductDetails);
			if(marketingPOProductDetails.size()>0){
				model.addAttribute("marketingPOProductDetailsList", marketingPOProductDetails);
				model.addAttribute("isShow", true);
				model.addAttribute("message","");
				model.addAttribute("message1","");
				//model.addAttribute("strInvoiceDateHiddenId", invoiceDate);
				
			}else{
				model.addAttribute("message","No Data Available");
				model.addAttribute("message1","");
			}
			
		}
		// only from and to dates are given in below condition executed
		else if(invoiceId == "" || invoiceFromDate != "" && invoiceToDate != "" ) {
			List<MarketingDeptBean> marketingPOProductDetails  = objMarketingDeptService.getAllViewExpendituresDates(invoiceFromDate,invoiceToDate,"",invoiceDate,vendorId);
			logger.info("**** The control is inside the invoiceFromDate & invoiceToDate block *****");
			logger.info("**** The marketingPOProductDetails list in MarketingController *******"+marketingPOProductDetails);
			if(marketingPOProductDetails.size()>0){
				model.addAttribute("marketingPOProductDetailsList", marketingPOProductDetails);
				model.addAttribute("isShow", true); // only dates given then it will shown to user why because which invoices are there in that dates it shown
				model.addAttribute("message","");
				model.addAttribute("message1","");
				//model.addAttribute("strInvoiceDateHiddenId", invoiceDate);
				
			}else{
				model.addAttribute("message","No Data Available");
				model.addAttribute("message1","");
			}
			
		}
		return "marketing/UpdateExpenditure";
		
		
		//UpdateExpenditures
		
	}

	// user click on sub module then it will call and executed
	@RequestMapping(value = "/updateExpenditure.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String updateExpenditure(Model model, HttpServletRequest request,HttpSession session) {
		logger.info("***** The control is inside the updateExpenditure service in MarketingController ***** ");
		model.addAttribute("message",""); // if error getting then it will set but here first time it will open so use it
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		model.addAttribute("dataAvailable","");
		model.addAttribute("message1","");
		return "marketing/UpdateExpenditure";
	}

	// any modifications done on the update like add,delete,edit then it will check and executed 
	@RequestMapping(value = "/addupdateExpenditure.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String addUpdateExpenditure(Model model, HttpServletRequest request,HttpSession session) {

		logger.info("***** The control is inside the addUpdateExpenditure service in MarketingController ***** ");

		// update expenditure code
		String noOfRows[]=null;;
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		String  expenditureFor =  request.getParameter("expendituredropdown");
		String previousexpendatureId = request.getParameter("expendatureId"); // by using this expenditureId getting the previous EXPENDITUREiD DATA taken here
		logger.info("****** The input values is expenditureFor & count & previousexpendatureId ********"+expenditureFor+"------->"+"------------->"+previousexpendatureId);
		MarketingDeptBean prevExpendatureDetails = objMarketingDeptService.expenditureDetails(Integer.valueOf(previousexpendatureId));
		logger.info("***** The previousexpendatureId MarketingPOProductDetails values is *****"+prevExpendatureDetails);
		MarketingDeptBean objMarketingExpenditure = new MarketingDeptBean();
		int expenditureId = objMarketingDeptService.getExpenditureId(); // newly created Expenditure Id taken here


		objMarketingExpenditure.setPreviousexpendatureId(Integer.valueOf(previousexpendatureId)); // previous ExpenditureId taken here and use it 
		objMarketingExpenditure.setExpenditureId(expenditureId); 
		objMarketingExpenditure.setInvoiceNumber(prevExpendatureDetails.getInvoiceNumber());
		objMarketingExpenditure.setInvoiceAmount(prevExpendatureDetails.getInvoiceAmount());
		objMarketingExpenditure.setCreatedBy(request.getSession(false).getAttribute("UserName").toString()); // getting the user name from session and set it
		objMarketingExpenditure.setExpenditureMonth(prevExpendatureDetails.getExpendatureMonth());
		objMarketingExpenditure.setExpenditureType(expenditureFor);
		objMarketingExpenditure.setInvoiceDate(prevExpendatureDetails.getInvoiceDate());
		objMarketingExpenditure.setDeliveryStatus("A");
		int result = objMarketingDeptService.insertAndUpdatePrevMarketExpenditure(objMarketingExpenditure);

		logger.info("***** The Expenduture table is updated *****"+result);


		String rowsCount =(request.getParameter("expendatureTableCount"));
		if(rowsCount!=null && !rowsCount.equals("")){
			noOfRows=rowsCount.split(",");
		}
		int records_Count =noOfRows.length;
		/*int singlesitecount = Integer.valueOf(request.getParameter("singleSiteRowcount"));
		int multiSiteCont = Integer.valueOf(request.getParameter("multiSiteRowcount"));
		int locationwiseCont = Integer.valueOf(request.getParameter("locationRowcount"));
		int brandwiseCont = Integer.valueOf(request.getParameter("brandwiseRowcount"));
		 */


		// logger.info("***** The count of the addupdateExpendituredetails *****"+count);	

		String childProdInfo[] = null;
		String quantity = null;
		String rate = null;
		String amount = null;
		String hoardingId = null;
		String fromDate = null;
		String toDate = null;
		String time = null;
		String siteId = null;
		String siteName = null;
		String siteAdd = null;
		String hoardingInfo[]=null;
		String hoardingData = null;
		String hoardingName = null;
		String childProdId = null;
		String childProdName = null;
		String childProdData = null;
		String siteData = null;
		String strSiteData[] = null;



		List<MarketingDeptBean> marketingDeptBeans = new ArrayList(); 

		try{
			if(expenditureFor.equalsIgnoreCase("sitewise")) {

				for(int i = 0 ; i <records_Count ; i++) {
					int num=Integer.parseInt(noOfRows[i]);
					logger.info("***** The control is inside of the singlesitecount *****"+num);
					siteAdd = request.getParameter("siteAdd" + num);
					childProdData = request.getParameter("hiddensingleSiteChild" + num);
					hoardingData = request.getParameter("singleSiteAreaName" + num);
					if(hoardingData.contains("$")){
						hoardingInfo=hoardingData.split("\\$");
						hoardingId=hoardingInfo[0];
						hoardingName=hoardingInfo[1];
					}
					if(childProdData.contains("$")){
						childProdInfo=childProdData.split("\\$");
						childProdId=childProdInfo[0];
						childProdName=childProdInfo[1];
					}
					fromDate = request.getParameter("SingleSiteFromDate" + num);
					toDate = request.getParameter("singleSiteToDate" + num);
					quantity = request.getParameter("singleSiteQuantity" + num);
					rate = request.getParameter("singleSiteRate" + num);
					amount = request.getParameter("singleSiteAmount" +num);
					//siteName = request.getParameter("sitename" + num);
					siteData=request.getParameter("siteWiseSitename");
					if(siteData.contains("$")){
						strSiteData=siteData.split("\\$");
						siteId=strSiteData[0];
						siteName=strSiteData[1];
					}
					time=request.getParameter("sitewisetime" + num);

					logger.info("****** The input values siteAdd is ******"+siteAdd);
					logger.info("****** The input values childProdId is ******"+childProdId);
					logger.info("****** The input values hoardingId is ******"+hoardingId);
					logger.info("****** The input values  fromDate is ******"+fromDate);
					logger.info("****** The input values toDate is ******"+toDate);
					logger.info("****** The input values quantity  is ******"+quantity);
					logger.info("****** The input values  rate is ******"+rate);
					logger.info("****** The input values  amount is ******"+amount);
					logger.info("****** The input values siteName is ******"+siteName);
					logger.info("****** The input values siteId is ******"+siteId);

					MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
					int expenditureDetailsId = objMarketingDeptService.getExpenditureDetailsId();
					objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
					objMarketingExpenditureDtls.setExpenditureId(expenditureId);
					objMarketingExpenditureDtls.setChild_ProductId(childProdId);
					objMarketingExpenditureDtls.setQuantity(quantity);
					objMarketingExpenditureDtls.setPrice(rate);
					objMarketingExpenditureDtls.setAmount(amount);
					objMarketingExpenditureDtls.setHoardingId(hoardingId);
					objMarketingExpenditureDtls.setFromDate(fromDate);
					objMarketingExpenditureDtls.setToDate(toDate);
					objMarketingExpenditureDtls.setTime(time);
					objMarketingExpenditureDtls.setSiteId(siteId);

					marketingDeptBeans.add(objMarketingExpenditureDtls);

					logger.info("****** The objMarketingExpenditureDtlsList is *******"+objMarketingExpenditureDtls);

					logger.info("****** The objMarketingExpenditureDtlsList size  is *******"+objMarketingExpenditureDtls);

				}


			}
			else if(expenditureFor.equalsIgnoreCase("MultipleSite")) {

				for(int i = 0 ; i < records_Count ; i++) {
					int num=Integer.parseInt(noOfRows[i]);
					logger.info("***** The control is inside of the singlesitecount *****"+num);
					siteAdd = request.getParameter("siteAdd" + num);
					childProdData = request.getParameter("exapendChildproduct" + num);
					hoardingData = request.getParameter("multiareaname" + num);
					if(hoardingData.contains("$")){
						hoardingInfo=hoardingData.split("\\$");
						hoardingId=hoardingInfo[0];
						hoardingName=hoardingInfo[1];
					}
					if(childProdData.contains("$")){
						childProdInfo=childProdData.split("\\$");
						childProdId=childProdInfo[0];
						childProdName=childProdInfo[1];
					}
					fromDate = request.getParameter("multiSiteFromDate" +num);
					toDate = request.getParameter("multiSiteTodate" +num);
					quantity = request.getParameter("multiSiteQuanty" + num);
					rate = request.getParameter("multiSiteRate" +num );
					amount = request.getParameter("multiSiteTotalAmount" + num);
					siteData=request.getParameter("multiSieSiteName" +num);
					if(siteData.contains("$")){
						strSiteData=siteData.split("\\$");
						siteId=strSiteData[0];
						siteName=strSiteData[1];
					}

					/*siteName = request.getParameter("multiSieSiteName" +num);
				siteId = request.getParameter("multiSieSiteId" +num );*/
					time=request.getParameter("multisitewisetime" + num);

					logger.info("****** The input values siteAdd is ******"+siteAdd);
					logger.info("****** The input values childProdId is ******"+childProdId);
					logger.info("****** The input values hoardingId is ******"+hoardingId);
					logger.info("****** The input values  fromDate is ******"+fromDate);
					logger.info("****** The input values toDate is ******"+toDate);
					logger.info("****** The input values quantity  is ******"+quantity);
					logger.info("****** The input values  rate is ******"+rate);
					logger.info("****** The input values  amount is ******"+amount);
					logger.info("****** The input values siteName is ******"+siteName);
					logger.info("****** The input values siteId is ******"+siteId);

					MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
					int expenditureDetailsId = objMarketingDeptService.getExpenditureDetailsId();
					objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
					objMarketingExpenditureDtls.setExpenditureId(expenditureId);
					objMarketingExpenditureDtls.setChild_ProductId(childProdId);
					objMarketingExpenditureDtls.setQuantity(quantity);
					objMarketingExpenditureDtls.setPrice(rate);
					objMarketingExpenditureDtls.setAmount(amount);
					objMarketingExpenditureDtls.setHoardingId(hoardingId);
					objMarketingExpenditureDtls.setFromDate(fromDate);
					objMarketingExpenditureDtls.setToDate(toDate);
					objMarketingExpenditureDtls.setTime(time);
					objMarketingExpenditureDtls.setSiteId(siteId);

					marketingDeptBeans.add(objMarketingExpenditureDtls);


					logger.info("****** The objMarketingExpenditureDtlsList is *******"+objMarketingExpenditureDtls);

					logger.info("****** The objMarketingExpenditureDtlsList size  is *******"+objMarketingExpenditureDtls);

				}


			}
			else if(expenditureFor.equalsIgnoreCase("LocationWise") ) {

				for(int i = 0 ; i < records_Count ; i++) { 
					int num=Integer.parseInt(noOfRows[i]);
					logger.info("***** The control is inside of the multisitecount *****"+num);
					siteAdd = request.getParameter("siteAdd" + num);
					childProdData = request.getParameter("LocationChildId" +num );
					hoardingData = request.getParameter("LocationLocationId" +num);
					if(hoardingData.contains("$")){
						hoardingInfo=hoardingData.split("\\$");
						hoardingId=hoardingInfo[0];
						hoardingName=hoardingInfo[1];
					}
					if(childProdData.contains("$")){
						childProdInfo=childProdData.split("\\$");
						childProdId=childProdInfo[0];
						childProdName=childProdInfo[1];
					}
					fromDate = request.getParameter("locationWiseFromDate" +num );
					toDate = request.getParameter("locationWiseToDate" + num);
					quantity = request.getParameter("locationWiseQuantity" + num);
					rate = request.getParameter("locationWistRate" + num);
					amount = request.getParameter("locationWiseTotalAmount" + num);
					siteName=request.getParameter("locationWiseSiteName" + num);
					siteId=request.getParameter("locationWiseSiteId"+num);

					time=request.getParameter("locationwisetime" + num);

					logger.info("****** The input values siteAdd is ******"+siteAdd);
					logger.info("****** The input values childProdId is ******"+childProdId);
					logger.info("****** The input values hoardingId is ******"+hoardingId);
					logger.info("****** The input values  fromDate is ******"+fromDate);
					logger.info("****** The input values toDate is ******"+toDate);
					logger.info("****** The input values quantity  is ******"+quantity);
					logger.info("****** The input values  rate is ******"+rate);
					logger.info("****** The input values  amount is ******"+amount);
					logger.info("****** The input values siteName is ******"+siteName);
					logger.info("****** The input values siteId is ******"+siteId);

					MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
					int expenditureDetailsId = objMarketingDeptService.getExpenditureDetailsId();
					objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
					objMarketingExpenditureDtls.setExpenditureId(expenditureId);
					objMarketingExpenditureDtls.setChild_ProductId(childProdId);
					objMarketingExpenditureDtls.setQuantity(quantity);
					objMarketingExpenditureDtls.setPrice(rate);
					objMarketingExpenditureDtls.setAmount(amount);
					objMarketingExpenditureDtls.setHoardingId(hoardingId);
					objMarketingExpenditureDtls.setFromDate(fromDate);
					objMarketingExpenditureDtls.setToDate(toDate);
					objMarketingExpenditureDtls.setTime(time);
					objMarketingExpenditureDtls.setSiteId(siteId);

					marketingDeptBeans.add(objMarketingExpenditureDtls);


					logger.info("****** The objMarketingExpenditureDtlsList is *******"+objMarketingExpenditureDtls);

					logger.info("****** The objMarketingExpenditureDtlsList size  is *******"+objMarketingExpenditureDtls);

				}

			}/*else if(expenditureFor.equalsIgnoreCase("LocationWise_1")) {

			for(int i = 1 ; i <= locationwiseCont ; i++) {
				logger.info("***** The control is inside of the locationwise *****"+i);
				siteAdd = request.getParameter("siteAdd" + i);
				childProdId = request.getParameter("childdata" + i);
				hoardingId = request.getParameter("areaname" + i);
				fromDate = request.getParameter("sitewisefromDate" + i);
				toDate = request.getParameter("sitewisetoDate" + i);
				quantity = request.getParameter("sitequanty" + i);
				rate = request.getParameter("splitamount" + i);
				amount = request.getParameter("totalamountsite" + i);
				siteName = request.getParameter("sitename" + i);
				siteId = request.getParameter("siteId" + i);

				logger.info("****** The input values siteAdd is ******"+siteAdd);
				logger.info("****** The input values childProdId is ******"+childProdId);
				logger.info("****** The input values hoardingId is ******"+hoardingId);
				logger.info("****** The input values  fromDate is ******"+fromDate);
				logger.info("****** The input values toDate is ******"+toDate);
				logger.info("****** The input values quantity  is ******"+quantity);
				logger.info("****** The input values  rate is ******"+rate);
				logger.info("****** The input values  amount is ******"+amount);
				logger.info("****** The input values siteName is ******"+siteName);
				logger.info("****** The input values siteId is ******"+siteId);

				MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
				int expenditureDetailsId = objMarketingDeptService.getExpenditureDetailsId();
				objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
				objMarketingExpenditureDtls.setExpenditureId(expenditureId);
				objMarketingExpenditureDtls.setChild_ProductId(childProdId);
				objMarketingExpenditureDtls.setQuantity(quantity);
				objMarketingExpenditureDtls.setPrice(rate);
				objMarketingExpenditureDtls.setAmount(amount);
				objMarketingExpenditureDtls.setHoardingId("");
				objMarketingExpenditureDtls.setFromDate(fromDate);
				objMarketingExpenditureDtls.setToDate(toDate);
				objMarketingExpenditureDtls.setTime("");
				objMarketingExpenditureDtls.setSiteId(siteId);

				marketingDeptBeans.add(objMarketingExpenditureDtls);

				logger.info("****** The objMarketingExpenditureDtlsList is *******"+objMarketingExpenditureDtls);

				logger.info("****** The objMarketingExpenditureDtlsList size  is *******"+objMarketingExpenditureDtls);

			}
		}*/
			else if(expenditureFor.equalsIgnoreCase("BrandingWise")) {

				for(int i = 0 ; i < records_Count ; i++) {
					int num=Integer.parseInt(noOfRows[i]);
					logger.info("***** The control is inside of the brandwiseCont *****"+num);
					siteAdd = request.getParameter("siteAdd" + num);
					childProdData = request.getParameter("BrandingChildId" + num);
					hoardingData = request.getParameter("brandingWiseLocationName" + num);
					if(hoardingData.contains("$")){
						hoardingInfo=hoardingData.split("\\$");
						hoardingId=hoardingInfo[0];
						hoardingName=hoardingInfo[1];
					}
					if(childProdData.contains("$")){
						childProdInfo=childProdData.split("\\$");
						childProdId=childProdInfo[0];
						childProdName=childProdInfo[1];
					}
					fromDate = request.getParameter("brandWiseFromDate" + num);
					toDate = request.getParameter("brandwiseToDate" + num);
					quantity = request.getParameter("brandWiseQuantity" + num);
					rate = request.getParameter("brandWiseProductRate" + num);
					amount = request.getParameter("brandwiseTotalAmount" + num);
					siteName = request.getParameter("brandwiseSiteName" + num);
					siteId = request.getParameter("brandwiseSiteId" + num);
					time=request.getParameter("brandingwisetime" + num);

					logger.info("****** The input values siteAdd is ******"+siteAdd);
					logger.info("****** The input values childProdId is ******"+childProdId);
					logger.info("****** The input values hoardingId is ******"+hoardingId);
					logger.info("****** The input values  fromDate is ******"+fromDate);
					logger.info("****** The input values toDate is ******"+toDate);
					logger.info("****** The input values quantity  is ******"+quantity);
					logger.info("****** The input values  rate is ******"+rate);
					logger.info("****** The input values  amount is ******"+amount);
					logger.info("****** The input values siteName is ******"+siteName);
					logger.info("****** The input values siteId is ******"+siteId);

					MarketingDeptBean objMarketingExpenditureDtls = new MarketingDeptBean();
					int expenditureDetailsId = objMarketingDeptService.getExpenditureDetailsId();
					objMarketingExpenditureDtls.setExpenditureDetailsId(expenditureDetailsId);
					objMarketingExpenditureDtls.setExpenditureId(expenditureId);
					objMarketingExpenditureDtls.setChild_ProductId(childProdId);
					objMarketingExpenditureDtls.setQuantity(quantity);
					objMarketingExpenditureDtls.setPrice(rate);
					objMarketingExpenditureDtls.setAmount(amount);
					objMarketingExpenditureDtls.setHoardingId(hoardingId);
					objMarketingExpenditureDtls.setFromDate(fromDate);
					objMarketingExpenditureDtls.setToDate(toDate);
					objMarketingExpenditureDtls.setTime(time);
					objMarketingExpenditureDtls.setSiteId(siteId);

					marketingDeptBeans.add(objMarketingExpenditureDtls);


					logger.info("****** The objMarketingExpenditureDtlsList is *******"+objMarketingExpenditureDtls);

					logger.info("****** The objMarketingExpenditureDtlsList size  is *******"+objMarketingExpenditureDtls);

				}




			}
			objMarketingDeptService.insertMarketExpenditureDtls(marketingDeptBeans); // here data added or inserted into table
			model.addAttribute("message1","Expenditure SuccessFully Updated");
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("message","Expenditure Not Updated");
		}

		return "marketing/UpdateExpenditure";

	}








	// here user click on View hoaring Details it will show View hoardoing details call this method 
	@RequestMapping(value = "/ViewMyHoardingDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String ViewMyHoardingDetails(Model model, HttpServletRequest request,HttpSession session) {
		logger.info("***** The control is inside the ViewMyHoardingDetails service in MarketingController ***** ");
		Map<String, String> siteDetails = new IndentSummaryDao().getSiteDetails();
		logger.info("***** The site details are ******"+siteDetails);
		model.addAttribute("site", siteDetails);
		return "marketing/ViewMyHoardingDetails";
	}
	
	// here user enter from or to date and select any one of site it wilol display the respective data
	@RequestMapping(value = "/getViewMyHoardingDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getViewMyHoardingDetails(Model model, HttpServletRequest request,HttpSession session) {
		logger.info("***** The control is inside the getViewMyHoardingDetails service in MarketingController ***** ");
		double totalAmount = 0.0;
		DecimalFormat formatter = new DecimalFormat("#.##");
		String fromDate = request.getParameter("sitewisefromDate");
		String toDate = request.getParameter("sitewisetoDate");
		String site = request.getParameter("site");
		logger.info("**** The fromDate value is ****"+fromDate);
		logger.info("**** The toDate value is ****"+toDate);
		logger.info("**** The site value is ****"+site);

		Map<String, String> siteDetails = new IndentSummaryDao().getSiteDetails();
		logger.info("***** The site details are ******"+siteDetails);
		model.addAttribute("site", siteDetails);


		Map<Double,List<MarketingDeptBean>> marketingPOProductDetailsMap  = objMarketingDeptService.getViewMyHoardingDetails(fromDate,toDate,site);
		logger.info("**** The marketingPOProductDetails list in MarketingController *******"+marketingPOProductDetailsMap);

		Iterator itr =  marketingPOProductDetailsMap.keySet().iterator(); // here dat set to map we need to retrive data by using key get total amount
		if(itr.hasNext()) {
			totalAmount =(Double) itr.next();
		}
		List<MarketingDeptBean> marketingPOProductDetailsList =  marketingPOProductDetailsMap.get(totalAmount); // here getting the total products details by using the total amount key value

		model.addAttribute("marketingPOProductDetailsList", marketingPOProductDetailsList);
		model.addAttribute("totalAmount",formatter.format(totalAmount)); // here set after the decimal value only some particular values
		model.addAttribute("isShow", true); // in the jsp from and to dates given then data display below so this attribute set here


		return "marketing/ViewMyHoardingDetails";
	}


	// this is calling user selected in inwards from po time calculate expenditure select the location or multiple or branding wise
	@RequestMapping(value = "/getAvailableAreaForSale", method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public   String getAvailableAreaForSale( @RequestParam("location") String strLocation,
			@RequestParam("expendatureType") String strExpendatureType,@RequestParam("month_year") String strMonth_year) throws JSONException {


		System.out.println("location : "+strLocation + " , strExpendatureType "+strExpendatureType);


		if(strExpendatureType.equals("BrandingWise")){ //required brand,location wise
			strLocation = "All";
		}

		String strResponse = objMarketingDeptService.getAvailableAreaForSale( strLocation, strMonth_year);// here we need to set the which data getting i.e brand or location or site Wise

		//strResponse = "<xml><site><siteId>102</siteId><siteName>Acropolis</siteName><availableArea>50000</availableArea><totalArea>400000</totalArea></site><site><siteId>107</siteId><siteName>EdenGarden</siteName><availableArea>800000</availableArea><totalArea>1700000</totalArea></site></xml>";




		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj = XML.toJSONObject(strResponse);
		strResponse = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

		return strResponse;
	}

	@RequestMapping(value = "/getAvailableArea.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getAvailableArea( HttpServletRequest request,HttpSession session) {


		ModelAndView model = null;
		try {
			model = new ModelAndView();

			String strMonthToBeUpdate =  request.getParameter("monthYear"); // here month only taken as month and year

			/*if(strMonthToBeUpdate != null){
				strMonthToBeUpdate = "11-2018";
			}*/

			if(strMonthToBeUpdate != null && !strMonthToBeUpdate.equals("")){
				List<MarketingDeptBean> marketingBeanList = objMarketingDeptService.getAvailableAreaForSaleOnMonthWise(strMonthToBeUpdate);

				if(marketingBeanList != null && marketingBeanList.size() >0){

					model.addObject("marketingBeanList", marketingBeanList);
					model.addObject("totalNoOfRecords", marketingBeanList.size());
					model.addObject("month_year",strMonthToBeUpdate);
					model.addObject("showGrid", true); // it have data then show the all the data 

				}else{
					model.addObject("message", "Sorry No Data available");
					model.addObject("showGrid", false); // no data avilable user given the input then it will set to false
				}

				request.setAttribute("message","");
			}
			model.setViewName("marketing/updateAvailablearea");


		}catch(Exception e){
			e.printStackTrace();
		}
		return model;
		//return "marketing/updateAvailablearea";
	}

	// here update available area add,delete,edit then it will call and executed
	@RequestMapping(value = "/updateAvailableArea.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updateAvailableArea( HttpServletRequest request,HttpSession session) {


		ModelAndView model = null;
		try {
			model = new ModelAndView();
			String strMonth_Year = request.getParameter("month_year") == null ? "" : request.getParameter("month_year").toString();
			String month_year=session.getAttribute("strMonth_Year")==null ? "" : session.getAttribute("strMonth_Year").toString();
			if(strMonth_Year==null || strMonth_Year.equals("") || strMonth_Year.equals(month_year)){
				request.setAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
				model.setViewName("response");
			}else{
				String marketingBeanList = objMarketingDeptService.updateAvailableArea(session, request);
				if(marketingBeanList.equalsIgnoreCase("falied")){
					model.setViewName("response");
				}else{
				model.setViewName("marketing/updateAvailablearea");}
				
			}

			session.setAttribute("strMonth_Year",strMonth_Year);

		}catch(Exception e){
			e.printStackTrace();
		}
		return model;
		//return "marketing/updateAvailablearea";
	}
	// getting  siteLocation details by call thro9ugh ajax call
	@RequestMapping(value = "/siteLocationDetails.spring", method = RequestMethod.GET)
	public @ResponseBody String siteLocationDetails(HttpServletRequest request, HttpSession session) {
		return CommonUtilities.getsiteLocationDetails();

	}
	// getting siteNames from ajax call 
	@RequestMapping(value = "/siteNameDetails.spring", method = RequestMethod.GET)
	public @ResponseBody String siteNameDetails(HttpServletRequest request, HttpSession session) {
		return CommonUtilities.getSiteDetails();

	}
	// this is calling at the time of inwards from po which is converted into dc
	@RequestMapping(value = "/convertMarketingPOtoInvoice", method={RequestMethod.POST,RequestMethod.GET})
	public String convertPOtoInvoice(@ModelAttribute("CreatePOModelForm")ProductDetails objProductDetails, BindingResult result, Model model, HttpServletRequest request, HttpSession session, 
			@RequestParam(required=false,value="file") MultipartFile[] files) throws IllegalStateException, IOException {
		
		String invoiceNumber=request.getParameter("InvoiceorDCNumber");
		String po=request.getParameter("poNo")==null ? "" : request.getParameter("poNo");
		String invoice_Number=session.getAttribute("strInvoiceNumber")==null ? "" : session.getAttribute("strInvoiceNumber").toString();
		
		String strPo_Number=session.getAttribute("poNumber")==null ? "" : session.getAttribute("poNumber").toString();
		String vendorName=request.getParameter("vendorName");
		if(po==null || po.equals("") ){
			model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
			return "response";
		}
		if(po.equals(strPo_Number)){
			model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
			return "response";
		}
		  
		String response1 = objMarketingDeptService.processingMarketingPOasInvoice(model, request, session);
		String viewToBeSelected = "";
		String serverFile = "";
		int imgCount=0;
		int pdfCount=0;
		//-----------------------
		String[] response_array = response1.split("@@");
		String response = response_array [0];
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("urlName","inwardsFromPO.spring");
		String imgname = response_array [1];//"vname_invoiceno_entryid";
		imgname = imgname.replace("/","$$"); //if the invoice number contain the / then it will converted to $ and save images
		String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
		for (int i = 0, j = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			if (!file.isEmpty()) {
				File dir = new File(rootFilePath +site_id);
				if (!dir.exists())
					dir.mkdirs();
				if (file.getOriginalFilename().endsWith(".pdf")) {
					serverFile = (dir.getAbsolutePath() + File.separator+ imgname + "_Part" + pdfCount + ".pdf");
					pdfCount++;
				} else {
					serverFile = dir.getAbsolutePath() + File.separator+ imgname + "_Part" + imgCount + ".jpg";
					//file.transferTo(new File(serverFile));
					imgCount++;
				}
				File convFile = new File(serverFile);
				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);
				fos.write(file.getBytes());
				fos.close();

			}
		}
		//------------------------

		//-------------------------

		/*if(response.equalsIgnoreCase("Success")) {

			int intSavedCount = irs.isInvoicesaved(  request );

			if(intSavedCount == 0){
				response="Failed";
				request.setAttribute("exceptionMsg", "This Invoice not inserted");
			}
		}*/


		if(response.equalsIgnoreCase("Success")) {

			viewToBeSelected  = "viewGRN";
		}
		else if(response.equalsIgnoreCase("Failed")){
			viewToBeSelected = "indentReceiveResponse";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			request.setAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}
		
		session.setAttribute("strInvoiceNumber",invoiceNumber);
		session.setAttribute("poNumber",po);

		SaveAuditLogDetails audit=new SaveAuditLogDetails();


		String user_id=String.valueOf(session.getAttribute("UserId"));

		String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));

		audit.auditLog(indentEntrySeqNum,user_id,"Converting PO to Invoice ",response,site_id);

		return viewToBeSelected;
	}

	/*=====================================================Modify Marketig Po start==============================================================*/
	// modifying the temp po details it will call and execute it
	@RequestMapping(value = "/modifyMarketingPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String modifyMarketingPO(Model model, HttpServletRequest request,HttpSession session) {
		String  site_id = "";
		String user_id = "";
		String value="";
		String strResponse="";
		String temp_Po_Number="";
		String fromDate="";
		String toDate="";
		//List<IndentCreationBean> indentgetData = null;

		String mailComments=request.getParameter("Remarks_cancel")==null ? "" : request.getParameter("Remarks_cancel");
		try {
			final int getLocalPort = request.getLocalPort();
			temp_Po_Number=request.getParameter("strPONumber");
			session = request.getSession(true);
			site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			fromDate=request.getParameter("fromdate");
			toDate=request.getParameter("toDate");
			String passwdForMail=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			//  canceling from mail. for mail approvals/rejecting we are genarating temppassword
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){

				user_id=request.getParameter("userId");
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(temp_Po_Number);
				if(passwdForMail.equals(dbPasswd)){
					value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);	
					strResponse="response";
				}else{
					strResponse="failed";
				}
			}else{
				//  login  from IMS tool and cancel the PO.
				value=objMarketingDeptService.modifyMarketingPo(session,request,temp_Po_Number,user_id,site_id);
				//if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || fromDate.equals("") && toDate.equals("")){//tempPoNumber=ponumber;
				//fromDate="";toDate="";
				//indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,temp_Po_Number,"All");
				//}else{
				//indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,temp_Po_Number,""); }//}
				//request.setAttribute("indentgetData",indentgetData);
				//request.setAttribute("PendingPoDetails",indentgetData);
				request.setAttribute("fromDate",fromDate);
				request.setAttribute("toDate", toDate);
				/*if(indentgetData.size()>0){
					request.setAttribute("showGrid", "true");}*/
				request.setAttribute("tempPoNumber",temp_Po_Number);
				strResponse="PendingPoApproval";

			}

			if(value.equalsIgnoreCase("success")){

				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id);

				/*List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(temp_Po_Number,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Temp Po Has been cancelled";
				objPurchaseDeptIndentrocess.sendTempPoMailCommonData(temp_Po_Number,mailComments,listOfDetails,subject,"cancelled",getLocalPort);*/
				//if(isImsCancelOrNot){request.setAttribute("response","Temp PO Cancelled Successfully");}
				model.addAttribute("message","Temp PO Cancelled Successfully");
			}
			else{

				model.addAttribute("message1","Already Approved or Rejected or Cancelled");
				strResponse="response";
			}



		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"ViewOrCancelPo","Success",site_id);
		return strResponse;
	}
	/*================================================show modifiyng data start===========================================================================*/
	// this code used at the time of cancel po(modifying temp po) it will use it
	@RequestMapping(value = "/getModifyingMarketingPo.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getModifyTempMarketingPo(HttpServletRequest request, HttpSession session,Model model){

		int site_id = 0;
		String user_id = "";
		int portNumber=request.getLocalPort();
		String rootFilePath =""; 
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}

			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			String poNumber = request.getParameter("poNumber");
			//String oldPONumber=request.getParameter("oldPONumber");
			model.addAttribute("poNumber",poNumber);
			String reqSiteId = request.getParameter("reqSiteId");
			String indentNumber = request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber");
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));

			model.addAttribute("tempPoProd",dcFormService.TempPoloadProds(indentNumber,reqSiteId));

			model.addAttribute("productsMap", dcFormService.loadProds(String.valueOf(site_id)));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			
			List<ProductDetails> poDetails = objMarketingDeptService.getModifyMarketingTempPODetails(poNumber,reqSiteId );
			model.addAttribute("poDetails",poDetails);
			model.addAttribute("listOfGetProductDetails",objPurchaseDeptIndentrocess.getProductDetailsListsForCancelPo(poNumber,reqSiteId));
			model.addAttribute("listOfTransChrgsDtls",objPurchaseDeptIndentrocess.getTransChrgsDtlsForCancelPo(poNumber,reqSiteId));
			List<ProductDetails> listLocationandFielddetails=objMarketingDeptService.getLocationFieldData(poNumber,true);
			if(listLocationandFielddetails.size()>0){
				request.setAttribute("locationSize",true);
			}
			// THIS IS FOR REVISED PO TIME USER CLICK ON MODIFY SO GETTING THE DATA
			String oldPoNumber=poDetails.get(0).getEdit_Po_Number();
			if(oldPoNumber!=null && !oldPoNumber.equals("") && oldPoNumber.startsWith("PO/SIPL")){
				List<String> list=objMarketingDepartmentIndentProcessDao.gettingPOEntryId(oldPoNumber);
				request.setAttribute("oldPOEntryId", list.get(0));
				request.setAttribute("oldPODate", list.get(1));
			}
			request.setAttribute("locationSizeValue", listLocationandFielddetails.size());
			model.addAttribute("listLocationandFielddetails",listLocationandFielddetails);
			Set <String> locationName =objMarketingDepartmentIndentProcessDao.getLocationDetails(); // site wise location details
			request.setAttribute("siteLocationDetails", locationName);
			
			String strPurpose = objPurchaseDeptIndentrocess.getCancelPoComments(poNumber);
			model.addAttribute("IndentLevelCommentsList",strPurpose);

			List<ProductDetails> listTermsAndCondition  = objPurchaseDeptIndentrocess.getTempTermsAndConditions(poNumber,"false",reqSiteId); // here false is used to check whether it is revised or normal po
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());
			String ccEmails = objPurchaseDeptIndentrocess.getTempPoCCEmails(poNumber);
			String subject = objPurchaseDeptIndentrocess.getTempPOSubject(poNumber);
			model.addAttribute("ccEmails",ccEmails);
			model.addAttribute("subject",subject);
			//******************************************this is for pdf download start******************************************************************//*	
			String filepath=rootFilePath+"TEMP_PO//";
			PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"TEMP_PO",model,request); 
			//loadVendorImgAndPdfFiles(filepath,poNumber,model,request);

			//******************************************this is for pdf download end******************************************************************//*	


		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Showing PO cancle details ","Success",String.valueOf(site_id));
		return "marketing/Marketing_Cancel_PO";
	}
	/*===================================================create Available Area start===========================================================*/
	
	// add newly available area then it will call to this method then getting the all active sites
	@RequestMapping(value = "/createAvailableArea.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView createAvailableArea( HttpServletRequest request,HttpSession session) {

		ModelAndView model = null;
		Map<String, String> siteDetails = null;
		String sitesList="";
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();;
		try {
			model = new ModelAndView();
			model.addObject("message","");
			String strMonthToBeCreate =  request.getParameter("monthYear");

			boolean status=objMarketingDeptService.checkAvailableAreaCreatedOrnot(strMonthToBeCreate);
			if(!status){
			if(strMonthToBeCreate != null && !strMonthToBeCreate.equals("")){

				siteDetails = new IndentSummaryDao().getSiteDetails();
				for(Map.Entry<String,String> entry:siteDetails.entrySet()){
					if(!entry.getKey().equals(marketingDeptId)){
					sitesList+=entry.getValue()+","+" ";
					}
				}
				sitesList=sitesList.substring(0, sitesList.length()-2);
				request.setAttribute("site",siteDetails);
				model.addObject("month_year",strMonthToBeCreate);
				model.addObject("showGrid",true);
				model.addObject("sitesList",sitesList);
			}
			model.setViewName("marketing/CreateAvailablearea");
			}else{
				model.addObject("message1","Oops !!! Already Created Available Area For this Month.");	
				model.setViewName("response");
			}
			

		}catch(Exception e){
			e.printStackTrace();
		}
		return model;

	}

	// add available area
	@RequestMapping(value = "/addAvailableArea.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView addAvailableArea( HttpServletRequest request,HttpSession session) {

		ModelAndView model = new ModelAndView();
		
		String strMonthToBeCreate = request.getParameter("month_year")==null ? "" :request.getParameter("month_year");
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String strMonth=session.getAttribute("month")==null ? "" : session.getAttribute("month").toString();
		String strUserId=session.getAttribute("strUserId")==null ? "" : session.getAttribute("strUserId").toString();
		
		
		if(strMonthToBeCreate!=null && !strMonthToBeCreate.equals("")){
		
		if(user_id.equals(strUserId) && strMonthToBeCreate.equals(strMonth)){
			model.addObject("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");	
			model.setViewName("response");
		}else {
		String response= objMarketingDeptService.addAvailableArea(session,request);	
		if(response.equalsIgnoreCase("success")){

			model.addObject("message","Monthly Data SuccessFully Added");
			session.setAttribute("strUserId",user_id);
			session.setAttribute("month",strMonthToBeCreate);
		}else{
			model.addObject("message","Monthly Data Not Added");
		}
		model.setViewName("marketing/CreateAvailablearea");
		}
		}else{
			model.addObject("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");	
			model.setViewName("response");
		}
		return model;

	}
	/*==================================================cancel Temp Po page start===========================================================*/
	// this is calling at time of user click on Reject (cancel PO) it will called
	@RequestMapping(value = "/cancelMarketingPoDetails.spring", method = RequestMethod.POST)
	public synchronized String cancelMarketingPoDetails(Model model, HttpServletRequest request,HttpSession session) {
		String user_id ="";
		String response = "";
		String site_id ="";
		String passwd="";//this is for mail passwd
		String ponumber=request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber");
		String fromDate="";
		String toDate="";
		List<IndentCreationBean> indentgetData = null;
		String strResponse="";
		boolean isImsOrNot=false;
		String checkval="";
		String checkActiveOrNot="";
		String strpendingEmpId="";
		try {

			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			user_id=request.getParameter("userId")== null ? "" : request.getParameter("userId").toString();
			String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
			fromDate=request.getParameter("fromdate")== null ? "" : request.getParameter("fromdate").toString();
			toDate=request.getParameter("toDate")== null ? "" : request.getParameter("toDate").toString();
			checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
			
			if(ponumber!=null && !ponumber.equals("")){
				checkActiveOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
				strpendingEmpId=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(ponumber,true);
			}
			if(!checkActiveOrNot.equals("A") || (!user_id.equals(strpendingEmpId) && !user_id.equals("BMKT3.1") && !user_id.equals(""))){
				model.addAttribute("response1","Already Approved or Modified or Cancelled");
				return "response";
			}
			
			
			// this is calling from mail i.e user did not have the session userId so taken from request object
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){  // this is from mail when the click on approve then it will call

				user_id=request.getParameter("userId");
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);
								if(passwdForMail.equals(dbPasswd)){ // check the password with database password
									response=objMarketingDeptService.cancelMarketingPoDetails(session,request);	
									strResponse="response";
								}else{
									response="failed";
									strResponse="response";
								}
			}else{	//  login and rejecting from IMS tool then back to the previous page after rejecting the marketing po
			boolean empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,ponumber);
			if(empSameOrNot){
			response=objMarketingDeptService.cancelMarketingPoDetails(session,request);}
			if(user_id==null || user_id.equals("")){ 
				model.addAttribute("message1","Oops !!! There was a improper request found.");
				strResponse="response";}else{ // which is taken and  call  from the mail
			if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || fromDate.equals("") && toDate.equals("") || (fromDate.equalsIgnoreCase("null") && toDate.equals(""))){//tempPoNumber=ponumber;
				fromDate="";toDate="";
				indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,ponumber,"All"); // here all records getting 
			}else{
				indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,ponumber,""); }//} // only from and to date records are taken 
			//request.setAttribute("indentgetData",indentgetData);
			request.setAttribute("PendingPoDetails",indentgetData);
			request.setAttribute("fromDate",fromDate); // if the user reject then the data from and todate set to default 
			request.setAttribute("toDate", toDate); // if the user reject then the data to date getting set to default
			if(indentgetData.size()>0){
			request.setAttribute("showGrid","true");} // to show the entire data for that here set the showgrid as true
			request.setAttribute("tempPoNumber",ponumber);
			model.addAttribute("message","Temp PO Cancelled Successfully");
			strResponse="PendingPoApproval";
			isImsOrNot=true;
			}

			if(response=="Success"){ 

				model.addAttribute("response","Temp PO Cancelled Successfully");
				//strResponse="response";

			}
			else{
				model.addAttribute("response1"," Already Approved or Cancelled or ModifyingTempPO ");
				if(isImsOrNot){
					model.addAttribute("message","");
					model.addAttribute("message1","Already Approved or Rejected or Cancelled");}
			}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
		return strResponse;

	}
	// this is for view expenditure when the user click on the invoice link then it will call this method and getting the invoice details and updated also use this method

	@RequestMapping(value = "/getAllMarketingExpenditures.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getAllMarketingExpenditures(Model model, HttpServletRequest request,HttpSession session) {
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String invoiceId  = request.getParameter("invoiceid");
		String invoiceToDate =""; //request.getParameter("datepickerto");
		String invoiceFromDate ="";// request.getParameter("datepickerfrom");
		String vendorId="";//request.getParameter("VendorId");
		String invoiceDate="";//request.getParameter("invoiceDate");
		//String siteIds=request.getParameter("siteIds");
		LinkedList expListObj = new LinkedList();
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(marketingDeptId);
		//request.setAttribute("siteDetails",siteDetails);
		request.setAttribute("totalProductsList", totalProductList);


		logger.info("***** The input invoiceid is---->"+invoiceId);
		//logger.info("***** The input invoiceFromDate is---->"+invoiceFromDate);
		//logger.info("***** The input invoiceToDate is---->"+invoiceToDate);

		String hiddenFormField = request.getParameter("referer");

		expListObj = objMarketingDeptService.getAllViewExpenditures(invoiceId, invoiceToDate, invoiceFromDate );

		double totalAmount = 0.0;

		//if(hiddenFormField.equalsIgnoreCase("ViewExpenditures")){
		if(invoiceId != "") {
			logger.info("**** The expListObj is *****"+expListObj);

			Map<Double,List<MarketingDeptBean>> getAllExpendaturesMap = objMarketingDeptService.getAllViewExpendituresWithVendorData(invoiceId,vendorId,invoiceDate);
			logger.info("**** The control is inside the invoiceId block *****");
			logger.info("**** The marketingPOProductDetails list in MarketingController *******"+getAllExpendaturesMap);
			Iterator itr = getAllExpendaturesMap.keySet().iterator();
			if(itr.hasNext()) { // here getting the data from map key value is total amount then value is product details
				totalAmount =(Double) itr.next();
			}
			List<MarketingDeptBean> expendaturesList =  getAllExpendaturesMap.get(totalAmount); // if the resultent bean contain data then it will check execute below condition
			if(expendaturesList.size()>0){
				model.addAttribute("expendaturesList", expendaturesList);
				BigDecimal bigDecimalTotalAmount = new BigDecimal(totalAmount); // here we need to set the after decimal values to the two places
				model.addAttribute("totalAmount",String.valueOf(bigDecimalTotalAmount.setScale(2,RoundingMode.FLOOR)));
				model.addAttribute("expendatureId", expListObj.getLast()); // here set the expenditure id by using thye expListObj get the value
				model.addAttribute("invoiceId", invoiceId);
				model.addAttribute("isShowAll", true); // show all the data which available with the given in voice id then it will shown so set here true 
				model.addAttribute("AvailableDateOrNot","");
			}else{
				model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
				model.addAttribute("AvailableDateOrNot","Data NOt Available");
			}

		}	

		return "marketing/ViewExpenditure";


	}	

	// this is for only view purpose for expenditure	

	@RequestMapping(value = "/getAllViewOnlyExpenditures.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getAllViewOnlyExpenditures(Model model, HttpServletRequest request,HttpSession session) {
		
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String productId="";
		//String productName="";
		String subProductId="";
		//String subProductName="";
		String childProductId="";
		//String childProductName="";
		
		String invoiceId  = request.getParameter("invoiceid");
		String invoiceToDate = request.getParameter("datepickerto");
		String invoiceFromDate = request.getParameter("datepickerfrom");
		String vendorId=request.getParameter("VendorId");
		String invoiceDate=request.getParameter("invoiceDate");
		String siteIds=request.getParameter("siteIds");
		String prod=request.getParameter("combobox_Product");
		String subProd=request.getParameter("subproduct");
		String ChildProd=request.getParameter("childproduct");
		if(prod!=null && !prod.equals("")){
			productId=prod.split("@@")[0];
			//productName=prod.split("\\$")[1];
		}if(subProd!=null && !subProd.equals("") && !subProd.equals("@@")){
			subProductId=subProd.split("@@")[0];
			//subProductName=subProd.split("\\$")[1];
		}if(ChildProd!=null && !ChildProd.equals("") && !ChildProd.equals("@@")){
			childProductId=ChildProd.split("@@")[0];
			//childProductName=ChildProd.split("\\$")[1];
		}
		
		
		Map<String, String> siteDetails = null;
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
		
		siteDetails = new IndentSummaryDao().getSiteDetails(); // getting the all active site details 
		request.setAttribute("siteDetails",siteDetails);
		List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(marketingDeptId);
		//request.setAttribute("siteDetails",siteDetails);
		request.setAttribute("totalProductsList", totalProductList);
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		if(siteIds!=""){ // here user select no of sites then it come as comma added to it then it will use in query like in so use this to add quatation here
			siteIds=siteIds.replace(",","','");
		}
		LinkedList expListObj = new LinkedList();
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());

		logger.info("***** The input invoiceid is---->"+invoiceId);
		logger.info("***** The input invoiceFromDate is---->"+invoiceFromDate);
		logger.info("***** The input invoiceToDate is---->"+invoiceToDate);

		String hiddenFormField = request.getParameter("referer");
		double totalAmount = 0.0;

		if(hiddenFormField.equalsIgnoreCase("ViewExpenditures")){
			if(invoiceFromDate == "" && invoiceToDate=="") {
				//expListObj = objMarketingDeptService.getAllViewExpenditures(invoiceId, invoiceToDate, invoiceFromDate );
				logger.info("**** The expListObj is *****"+expListObj);

				Map<Double,List<MarketingDeptBean>> getAllExpendaturesMap = objMarketingDeptService.getAllViewExpendituresWithInvoiceData(invoiceId,vendorId,invoiceDate,siteIds,productId,subProductId,childProductId,"","");
				logger.info("**** The control is inside the invoiceId block *****");
				logger.info("**** The marketingPOProductDetails list in MarketingController *******"+getAllExpendaturesMap);
				Iterator itr = getAllExpendaturesMap.keySet().iterator();
				if(itr.hasNext()) {
					totalAmount =(Double) itr.next();
				}
				List<MarketingDeptBean> expendaturesList =  getAllExpendaturesMap.get(totalAmount); // get the expenditure details by using key i.e total amount as key
				if(expendaturesList.size()>0){
					model.addAttribute("expendaturesList", expendaturesList);
					BigDecimal bigDecimalTotalAmount = new BigDecimal(totalAmount);
					double strTotalAmount=Double.valueOf(String.valueOf(bigDecimalTotalAmount.setScale(2,RoundingMode.FLOOR)));
					model.addAttribute("totalAmount",format.format(strTotalAmount));
					//model.addAttribute("expendatureId", expListObj.getLast());
					model.addAttribute("invoiceId", invoiceId);
					model.addAttribute("isShowAll", true); // here we need to use this is to show the all the data i.e data there then it will send 
					model.addAttribute("AvailableDateOrNot",""); // if no data available then it will set in below else condition so here set to empty
				}else{
					model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
					model.addAttribute("AvailableDateOrNot","Data NOt Available");
				}

			}else if((invoiceId == "") && (invoiceFromDate != "" || invoiceToDate != "") && productId == "" && subProductId == "" && childProductId == "") { // here only from and to dates given it will execute in this invoice number along with link will display it

				List<MarketingDeptBean> marketingPOProductDetails  = objMarketingDeptService.getAllViewExpendituresDates(invoiceFromDate,invoiceToDate,siteIds,invoiceDate,vendorId);
				logger.info("**** The control is inside the invoiceFromDate & invoiceToDate block *****");
				logger.info("**** The marketingPOProductDetails list in MarketingController *******"+marketingPOProductDetails);
				if(marketingPOProductDetails.size()>0){
					model.addAttribute("marketingPOProductDetailsList", marketingPOProductDetails);
					model.addAttribute("isShow", true); // here display the data if data available then it will show
					model.addAttribute("AvailableDateOrNot","");
				}else{
					model.addAttribute("AvailableDateOrNot","Data NOt Available");
				}

			}
			else if((invoiceId == "") || invoiceFromDate != "" || invoiceToDate != "" && (productId != "" || subProductId != "" || childProductId != "")){
					
				Map<Double,List<MarketingDeptBean>> getAllExpendaturesMap = objMarketingDeptService.getAllViewExpendituresWithInvoiceData(invoiceId,vendorId,invoiceDate,siteIds,
						productId,subProductId,childProductId,invoiceFromDate,invoiceToDate);
				logger.info("**** The control is inside the invoiceId block *****");
				logger.info("**** The marketingPOProductDetails list in MarketingController *******"+getAllExpendaturesMap);
				Iterator itr = getAllExpendaturesMap.keySet().iterator();
				if(itr.hasNext()) {
					totalAmount =(Double) itr.next();
				}
				List<MarketingDeptBean> expendaturesList =  getAllExpendaturesMap.get(totalAmount); // get the expenditure details by using key i.e total amount as key
				if(expendaturesList.size()>0){
					model.addAttribute("expendaturesList", expendaturesList);
					BigDecimal bigDecimalTotalAmount = new BigDecimal(totalAmount);
					double strTotalAmount=Double.valueOf(String.valueOf(bigDecimalTotalAmount.setScale(2,RoundingMode.FLOOR)));
					model.addAttribute("totalAmount",format.format(strTotalAmount));
					//model.addAttribute("expendatureId", expListObj.getLast());
					model.addAttribute("invoiceId", invoiceId);
					model.addAttribute("isShowAll", true); // here we need to use this is to show the all the data i.e data there then it will send 
					model.addAttribute("AvailableDateOrNot",""); // if no data available then it will set in below else condition so here set to empty
				}else{
					model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean());
					model.addAttribute("AvailableDateOrNot","Data NOt Available");
				}
				
			}
			
		}
		return "marketing/ViewExpenditure";	
	}
	//UpdateExpenditures


  public static void main(String [] args){
	  
	  String date = "12-12-2018";
	  date = date.substring(3,10);
	  System.out.println(date);
  }

  /************************************************************************Reject for Temp Po******************************************************************/
	
  // reject (Cancel )marketing po then it send to mail for rejecting the po
  @RequestMapping(value = "/CancelMailMarketingTempPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String RejectMailTempPO(Model model, HttpServletRequest request,HttpSession session) {
		String user_id ="";
		String response = "";
		String site_id ="";
		String poNumber="";
		
		try {
			
			response=objMarketingDeptService.RejectMailTempPO(session,request);
			if(response=="Success"){ 

				model.addAttribute("response","Temp PO Rejected Successfully ");

			}
			else{
				model.addAttribute("response1","Already Approved or Rejected or Cancelled ");

			}

		} catch (Exception e) {

			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
		return "response";

	}
	/****************************************** Marketing Revised Po Started************************************************************************/

//user click on poNumber at the time of revised po list shown 
	@RequestMapping(value = "/showPODetailsToMarketingRevised.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String showPODetailsToMarketingRevised(HttpServletRequest request, HttpSession session,Model model){

		int site_id = 0;
		String user_id = "";
		int strCount=0;
		String KILO_BYTE=""; // used to take the length of pdf below 1 mb so take this
		String rootFilePath =""; 
		int portNumber=request.getLocalPort();
		String poSiteId="";
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("productsMap", dcFormService.loadProds(String.valueOf(site_id)));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			//ics.createPO(model, request);
			//List<ProductDetails> IndentDetails = null;
			String poNumber = request.getParameter("poNumber");
			//model.addAttribute("poNumber",poNumber);
			request.setAttribute("poNumber",poNumber); // this is use in below tables
			model.addAttribute("POType","View PO's to Revise");
			String reqSiteId = request.getParameter("reqSiteId");
			model.addAttribute("url","RevisedPO.spring");
			List<ProductDetails> poDetails = irs.getPODetails(poNumber,reqSiteId );
			String strPoEntryId =String.valueOf(poDetails.get(0).getPoEntryId());
			String subject=poDetails.get(0).getSubject();
			poSiteId=poDetails.get(0).getSite_Id();
			String siteWise=poDetails.get(0).getType_Of_Po();
			String siteWiseNumber=objMarketingDepartmentIndentProcessDao.gettingSiteNameForSiteWise(poDetails);
			boolean isThisPOGoingToBeCanceled = objPurchaseDeptIndentrocess.isThisPOGoingToBeCanceled(strPoEntryId);
			if(isThisPOGoingToBeCanceled){
				model.addAttribute("response1"," This PO is Going To Be Canceled in Approvals. ");
				return "response";
			}
			if(siteWise.equalsIgnoreCase("SiteWise")){
				model.addAttribute("siteWise",true);
			}
			model.addAttribute("strSubject",subject);
			model.addAttribute("poDetails",poDetails);
			model.addAttribute("siteWiseNumber",siteWiseNumber);
			
			Set <String> locationName =objMarketingDepartmentIndentProcessDao.getLocationDetails(); // site wise location details
			request.setAttribute("siteLocationDetails", locationName);
			
			model.addAttribute("listOfGetProductDetails",irs.getProductDetailsLists(poNumber,reqSiteId));
			model.addAttribute("listOfTransChrgsDtls",irs.getTransChrgsDtls(poNumber,reqSiteId));
			List<ProductDetails> listLocationandFielddetails=objMarketingDeptService.getLocationFieldData(poNumber,false);
			if(listLocationandFielddetails.size()>0){
				request.setAttribute("locationSize",true);
			}
			request.setAttribute("locationSizeValue", listLocationandFielddetails.size());
			model.addAttribute("listLocationandFielddetails",listLocationandFielddetails);
			request.setAttribute("isPoUpdated","false"); // this is use in jsp same jsp using for update and revised so it will take
			// true is given in this why because Revised po Time terms and conditions take from permanent table
			List<ProductDetails> listTermsAndCondition  = objPurchaseDeptIndentrocess.getTempTermsAndConditions(poNumber,"true",reqSiteId);
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());
			KILO_BYTE=validateParams.getProperty("KILO_BYTE") == null ? "" : validateParams.getProperty("KILO_BYTE").toString();
			model.addAttribute("KILO_BYTE",KILO_BYTE);
			String ccEmails =objPurchaseDepartmentIndentProcessDao.getCCmails(strPoEntryId,poSiteId,false);//objPurchaseDeptIndentrocess.getDefaultCCEmails(reqSiteId);
			model.addAttribute("ccEmails",ccEmails);
		/******************************************this is for pdf download start******************************************************************/	
			String filepath=rootFilePath+"PO//";
			PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
	/******************************************this is for pdf download end******************************************************************/	
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Showing Revised PO Details","Success",String.valueOf(site_id));
		return "marketing/Marketing_Revised_PO";
	}
	/*********************************** revised marketing po start*********************************************************************/
	// user click on revised po it will executed
	@RequestMapping(value = "/editAndSaveMarketingRevisedPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String editAndSavePO(HttpServletRequest request, HttpSession session,Model model,
			@RequestParam(value="file",required = false) MultipartFile[] files){
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate="";
		String po_Number="";
		
		int site_id =0;
		String user_id ="";
		String response ="";
		String oldPoNumber="";
		String tempOrNot="";
		
		String moveFilePath="";
		String poLevelComments="";
		
		String rootFilePath =""; 
		String strFilePath="";
		int getLocalPort = request.getLocalPort();
		double finlaAmt=0.0;
		double oldAmount=0.0;
		
		String indentNumber="";
		//String indentNo="";
		String old_Po_Number="";
		String strfinalAmount="";
		String finalAmt="";
		
		if(getLocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
		moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();}
		try{
			
			oldPoNumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString(); // old normal PoNumber taken so it will take it
			strfinalAmount=request.getParameter("ttlAmntForIncentEntry");
			old_Po_Number=session.getAttribute("PoNumber")== null ? "" : session.getAttribute("PoNumber").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			if(oldPoNumber==null || oldPoNumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
		if(oldPoNumber.equals(old_Po_Number) && strfinalAmount.equals(finalAmt)){
			model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
			return "response";
		}else{
		String activeOrNot=objPurchaseDepartmentIndentProcessDao.checkRevisedOrNot(oldPoNumber);
		if(activeOrNot.equals("A")){
		 site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		 user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		 poLevelComments=request.getParameter("note") == null ? "" : request.getParameter("note").toString(); // user any change in product given comments at the time of po creation
		 finlaAmt=Double.valueOf(request.getParameter("ttlAmntForIncentEntry")==null ? "" : request.getParameter("ttlAmntForIncentEntry").toString());
		 oldAmount=Double.valueOf(request.getParameter("POTotal")==null ? "" : request.getParameter("POTotal").toString());
		 
		int count=files.length;
		 if(count==1){
			 boolean strStatus=objMarketingDeptService.checkIsMarketingUpdateOrNot(session,request,true,false);
				if(strStatus){
					model.addAttribute("message1","No changes has made, this PO cannot be revised.");
					return "Comparison_response"; 
		 }
		 }
		// for financial year taken in poNumber so below condition use below one take 
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		Calendar cal = Calendar.getInstance();
		String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());
		String strFinacialYear = "";
		//String FinacialYear = "";

		if(currentMonth <=3){

			strFinacialYear = (currentYear-1)+"-"+Integer.parseInt(currentYearYY);
		}else{
			strFinacialYear = currentYear+"-"+(Integer.parseInt(currentYearYY)+1);
		}
		//	request.setAttribute("strFinacialYear",strFinacialYear);
		versionNo = validateParams.getProperty("PO_versionNo");
		refferenceNo = validateParams.getProperty("PO_Refference");
		strPoPrintRefdate = validateParams.getProperty("PO_issuedate");
		// these are setting data use i service class and save in temp or permanent table
		request.setAttribute("versionNo", versionNo);
		request.setAttribute("refferenceNo", refferenceNo);
		request.setAttribute("strPoPrintRefdate", strPoPrintRefdate);
		// in success page we show the version,reference,poRef 
		model.addAttribute("versionNo",versionNo);
		model.addAttribute("refferenceNo",refferenceNo);
		model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
		request.setAttribute("poPage","true"); // po level comments we can use it in jsp
		request.setAttribute("PoLevelComments",poLevelComments);
		ProductDetails objProductDetails = new ProductDetails();
		//ics.createPO(model, request);
		
		model.addAttribute("CreatePOModelForm", objProductDetails);
		//String response1 = objPurchaseDeptIndentrocess.getNoOfRowsForUpdatePO(model, request, session);

		 response = objMarketingDeptService.SaveMarketingPoDetails(model, request, session,strFinacialYear);
		// this is for pdf purpose start
		if(!response.equalsIgnoreCase("response")){
		String poNumber=request.getAttribute("strPONumber").toString();
		model.addAttribute("poNumber",poNumber); // taken poumber for attachments purpose
		
		//int count=files.length;
		if (request.getAttribute("isTempPoOrNot").toString().equalsIgnoreCase("true")) {
			model.addAttribute("tempPOOrNot",true);
			strFilePath = rootFilePath + "TEMP_PO//";
			po_Number = poNumber;
			tempOrNot="TEMP_PO";
		} else {
			model.addAttribute("tempPOOrNot",false);
			strFilePath = rootFilePath + "PO//";
			po_Number = poNumber.replace('/', '$');
			tempOrNot="PO";
		}
		boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
		int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
		//boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);
		String strOldPoNumber=oldPoNumber.replace('/', '$'); // for files path taken time replace / with $ 
		for (int i = 0,j=0; i < imagesAlreadyPresent; i++) {
			File img = new File(rootFilePath+"PO//"+strOldPoNumber+"_Part"+i+".jpg");
			if(img.exists()){
				Files.copy(Paths.get(moveFilePath+"\\PO\\"+strOldPoNumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\"+tempOrNot+"\\"+po_Number+"_Part"+i+".jpg"));
			}
		}
		for (int i = 0,j=0; i < pdfAlreadyPresent; i++) {
			File file = new File(rootFilePath+"PO//"+strOldPoNumber+"_Part"+i+".pdf");
			if(file.exists()){
			Files.copy(Paths.get(moveFilePath+"\\PO\\"+strOldPoNumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\"+tempOrNot+"\\"+po_Number+"_Part"+i+".pdf"));
			}
		}
		/******************************************FOR UPLOAD FILE**************************************************/
		/***************************************file move start************************************************/
		//int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
		//int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
		int imgCount=imagesAlreadyPresent;
		int pdfCount=pdfAlreadyPresent;
		//String strFilePath=rootFilePath+"TEMP_PO//";
		for (int i =0; i < files.length; i++) {
			
			MultipartFile multipartFile = files[i];
			if(!multipartFile.isEmpty()){
			try {
				//file directory name is constructing here 
					File dir = new File(strFilePath+File.separator);
				//checking is the directory exits or not if not create the directory for string images
					if (!dir.exists())
						dir.mkdirs();
					
					String filePath ="";
					System.out.println(multipartFile.getOriginalFilename());
					//checking the extension of file and creating the file based on extension 
					if(multipartFile.getOriginalFilename().endsWith(".pdf")){
						filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+pdfCount+".pdf";
						pdfCount++;
					}else{
						filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+imgCount+".jpg";
						imgCount++;
					}
				
					//giving file path to file object
					File convFile = new File(filePath);
					//creating the image or pdf file 
				    convFile.createNewFile(); 
				    FileOutputStream fos = new FileOutputStream(convFile); 
				    fos.write(multipartFile.getBytes());
				    fos.close(); 
				
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Image NOT Uploaded");
					//return "You failed to upload " ;
					/*model.addAttribute("message1", "Temp PO Approved Successfully. But Image NOT Uploaded");
					request.setAttribute("message1","Temp PO Approved Successfully. But Image NOT Uploaded");*/
				}
			}
		}//For Loop
		PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(strFilePath,poNumber,tempOrNot,model,request); // call the getting pdf and images for showing purpose
		
		// move files why because old poNumber to RevisedPONumber 	
		
		/********************************************file move end************************************************/
		// this is any extra pdf upload except already existed one use below code
		
		session.setAttribute("PoNumber",oldPoNumber);
		session.setAttribute("strfinalAmount",strfinalAmount);
		}// if condition end
		else{
			if(!response.equalsIgnoreCase("response")){
			model.addAttribute("message1","We found a Malfunction, Please once logout and login for further operations.");
			return "response";	}	}
		}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails.auditLog("0",user_id,"submit Revised PO","sucess",String.valueOf(site_id));
		return response;
	}
	/***************************************************************cancel temp po page for mail start*******************************************************************/
	// this is calling from mail 
	@RequestMapping(value = "/ModifyTempPoMail.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String ModifyTempPoMail(Model model, HttpServletRequest request,HttpSession session) {
		String  site_id = "";
		String user_id = "";
		String value="";
		String strResponse="";
		try {
			final int getLocalPort = request.getLocalPort();
			String mailComments=request.getParameter("Remarks")==null ? "" : request.getParameter("Remarks");
			String temp_Po_Number=request.getParameter("strPONumber"); // temp po number but in jsp strPONumber given taken to temp po number
			site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			String passwdForMail=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			user_id=request.getParameter("userId");
			String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(temp_Po_Number);
			if(passwdForMail.equals(dbPasswd)){ // check database password to mail password
			value=objMarketingDeptService.modifyTempPo(session,request,temp_Po_Number,user_id,site_id);
		  	}
			//value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);}
			if(value.equalsIgnoreCase("success")){
				
				String pendingEmpId=objMarketingDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id); // previous employeeId taken in this 
				int result=objPurchaseDepartmentIndentProcessDao.updateEmpId(pendingEmpId,temp_Po_Number);
				List<String> listOfccmailsDetails=objMarketingDepartmentIndentProcessDao.getApprovalEmpMails(temp_Po_Number); // this is for getting the ccmails
				String ccTo [] = new String[listOfccmailsDetails.size()];
				listOfccmailsDetails.toArray(ccTo);
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(temp_Po_Number,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Temp Po has been cancelled";
				objMarketingDeptService.sendMarketingTempPoMailCommonData(temp_Po_Number,mailComments,listOfDetails,subject,"Cancelled",getLocalPort,ccTo); // mail send to previous person to know the po was cancelled 
				model.addAttribute("message","Temp PO Cancelled Successfully");
				}
				else{
					
					model.addAttribute("message1","Already Approved or Rejected or Cancelled");
					
				}
			
			strResponse="response";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"MailTempPoCancelPo","Success",site_id);
		return strResponse;
	}
	// from ims tool cancel po 
	@RequestMapping(value = "/MarketingModifyTempPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public synchronized String MarketingModifyTempPO(Model model, HttpServletRequest request,HttpSession session) {
		String  site_id = "";
		String user_id = "";
		String value="";
		String strResponse="";
		String temp_Po_Number="";
		String fromDate="";
		String toDate="";
		List<IndentCreationBean> indentgetData = null;
		boolean isImsOrNot=false;
		String checkActiveOrNot="";
		String strPendingEmpId="";
		
		String mailComments=request.getParameter("Remarks_cancel")==null ? "" : request.getParameter("Remarks_cancel");
		try {
			final int getLocalPort = request.getLocalPort();
			temp_Po_Number=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
			session = request.getSession(true);
			site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			int portNumber=request.getLocalPort();
			List<String> multiplePendingEmpList = new ArrayList<String>();
			boolean MarketingHead = false;
			if(portNumber==80){//LIVE
				if(user_id.equals("M008")){
					MarketingHead = true;
					multiplePendingEmpList.add("M008"); //Srinivas Moramchetty	VP-Sales and Marketing, Arun	Vice president operatoins
					multiplePendingEmpList.add("HYDMKT2");
				}//('M008','HYDMKT2')
			}else{
				if(user_id.equals("BMKT3.1")){
					MarketingHead = true;
					multiplePendingEmpList.add("BMKT3.1"); 
					multiplePendingEmpList.add("HMKT2.1");
				}
			}
			if(temp_Po_Number!=null && !temp_Po_Number.equals("")){
				checkActiveOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(temp_Po_Number);
				strPendingEmpId=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(temp_Po_Number,true);
				}
				if(!checkActiveOrNot.equals("A") || (!user_id.equals(strPendingEmpId) && !user_id.equals("BMKT3.1") && !user_id.equals(""))){
					
					model.addAttribute("response1","Already Approved or Modified or Cancelled");
					return "response";
					
				}
			fromDate=request.getParameter("fromdate")== null ? "" : request.getParameter("fromdate").toString(); // from and to dates used to back to previous screen i.e pending po for approval 
			toDate=request.getParameter("toDate")== null ? "" : request.getParameter("toDate").toString();
			String passwdForMail=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			//  canceling from mail. for mail approvals/rejecting we are genarating temppassword
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){// from mail cancel po this one take and check condition
				
				user_id=request.getParameter("userId");
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(temp_Po_Number);// getting password from database then check with request.getParameter password
								if(passwdForMail.equals(dbPasswd)){
									
									value=objMarketingDeptService.modifyTempPo(session,request,temp_Po_Number,user_id,site_id);	
									strResponse="response";
								}else{
									strResponse="failed";
								}
			}else{
				//  login  from IMS tool and cancel the PO.
				boolean empSameOrNot = false;
				if(MarketingHead){
					empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNotForMarketingHead(user_id,request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber"),multiplePendingEmpList);
				}else{
					empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber"));
				}
				if(empSameOrNot){
				value=objMarketingDeptService.modifyTempPo(session,request,temp_Po_Number,user_id,site_id);}
				// here check from and to date for after approval why because go back to previous screen
				if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || fromDate.equals("") && toDate.equals("") || fromDate.equalsIgnoreCase("null") && toDate.equals("")){//tempPoNumber=ponumber;
					fromDate="";toDate="";
					indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,temp_Po_Number,"All");
					}else{
					indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,temp_Po_Number,""); }//}
					request.setAttribute("indentgetData",indentgetData);
					request.setAttribute("PendingPoDetails",indentgetData);
					request.setAttribute("fromDate",fromDate);
					request.setAttribute("toDate", toDate);
					if(indentgetData.size()>0){
					request.setAttribute("showGrid", "true");}
					request.setAttribute("tempPoNumber",temp_Po_Number);
					model.addAttribute("url","ViewPoPendingforApproval.spring");
					strResponse="PendingPoApproval";
					isImsOrNot=true;
				
			}
		
			
			
			if(value.equalsIgnoreCase("success")){
				
				List<String> listOfccmailsDetails=objMarketingDepartmentIndentProcessDao.getApprovalEmpMails(temp_Po_Number);
				String ccTo [] = new String[listOfccmailsDetails.size()];
				listOfccmailsDetails.toArray(ccTo);
				String pendingEmpId=objMarketingDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id);
				int result=objPurchaseDepartmentIndentProcessDao.updateEmpId(pendingEmpId,temp_Po_Number);
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(temp_Po_Number,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Temp Po Has been Modified";
				objMarketingDeptService.sendMarketingTempPoMailCommonData(temp_Po_Number,mailComments,listOfDetails,subject,"Modified",getLocalPort,ccTo);
				
				model.addAttribute("message","Temp PO sent for Modification.");
			}
				else{
					
					model.addAttribute("message1","Already Approved or Modified or Cancelled");
					if(isImsOrNot){model.addAttribute("message1","Already Approved or Modified or Cancelled");
					model.addAttribute("message","");
					}
					strResponse="response";
				}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"ModifyMarketingTempPo","Success",site_id);
		return strResponse;
	}
	/*********************************** update the modify temp po start**********************************************************************/
	@RequestMapping(value = "/updateMarketingTempPoPage.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String updateMarketingTempPoPage(HttpServletRequest request, HttpSession session,Model model,
			@RequestParam(value="file",required = false) MultipartFile[] files){
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate="";
		String po_Number="";
		
		int site_id =0;
		String user_id ="";
		String response ="";
		String oldPoNumber="";
		String tempOrNot="";
		
		String moveFilePath="";
		String poLevelComments="";
		
		String rootFilePath =""; 
		String strFilePath="";
		int getLocalPort = request.getLocalPort();
		double finlaAmt=0.0;
		double oldAmount=0.0;
		String old_Po_Number="";
		String strfinalAmount="";
		String finalAmt="";
		
		if(getLocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
		moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();}
		try{
			oldPoNumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString(); // old normal PoNumber taken so it will take it
			strfinalAmount=request.getParameter("ttlAmntForIncentEntry");
			old_Po_Number=session.getAttribute("PoNumber")== null ? "" : session.getAttribute("PoNumber").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			if(oldPoNumber==null || oldPoNumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			if(oldPoNumber.equals(old_Po_Number) ){
				model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
				return "response";
			}else{
				String activeOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(oldPoNumber);
				if(activeOrNot.equals("A")){
					site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
					user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
					int count=files.length;
					if(count==1){
						boolean strStatus=objMarketingDeptService.checkIsMarketingUpdateOrNot(session,request,true,false);
						if(strStatus){
							model.addAttribute("message1","Sorry,Unable to Modify PO.");
							return "response"; 
						}
					}
					// for financial year taken in poNumber so below condition use below one take 
					int currentYear = Calendar.getInstance().get(Calendar.YEAR);
					int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
					Calendar cal = Calendar.getInstance();
					String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());
					String strFinacialYear = "";
					//String FinacialYear = "";

					if(currentMonth <=3){

						strFinacialYear = (currentYear-1)+"-"+Integer.parseInt(currentYearYY);
					}else{
						strFinacialYear = currentYear+"-"+(Integer.parseInt(currentYearYY)+1);
					}
					//	request.setAttribute("strFinacialYear",strFinacialYear);
					versionNo = validateParams.getProperty("PO_versionNo");
					refferenceNo = validateParams.getProperty("PO_Refference");
					strPoPrintRefdate = validateParams.getProperty("PO_issuedate");
					// these are setting data use i service class and save in temp or permanent table
					request.setAttribute("versionNo", versionNo);
					request.setAttribute("refferenceNo", refferenceNo);
					request.setAttribute("strPoPrintRefdate", strPoPrintRefdate);
					// in success page we show the version,reference,poRef 
					model.addAttribute("versionNo",versionNo);
					model.addAttribute("refferenceNo",refferenceNo);
					model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
					request.setAttribute("poPage","true"); // po level comments we can use it in jsp
					request.setAttribute("PoLevelComments",poLevelComments);
					ProductDetails objProductDetails = new ProductDetails();
					//ics.createPO(model, request);

					model.addAttribute("CreatePOModelForm", objProductDetails);
					//String response1 = objPurchaseDeptIndentrocess.getNoOfRowsForUpdatePO(model, request, session);

					response = objMarketingDeptService.SaveMarketingPoDetails(model, request, session,strFinacialYear);

					// this is for pdf purpose start
					if(!response.equalsIgnoreCase("response")){
						objMarketingDepartmentIndentProcessDao.updateMarketingTempPo(oldPoNumber,String.valueOf(site_id));
						String poNumber=request.getAttribute("strPONumber").toString();
						model.addAttribute("poNumber",poNumber); // taken poumber for attachments purpose

						//int count=files.length;
						if (request.getAttribute("isTempPoOrNot").toString().equalsIgnoreCase("true")) {
							model.addAttribute("tempPOOrNot",true);
							strFilePath = rootFilePath + "TEMP_PO//";
							po_Number = poNumber;
							tempOrNot="TEMP_PO";
						} else {
							model.addAttribute("tempPOOrNot",false);
							strFilePath = rootFilePath + "PO//";
							po_Number = poNumber.replace('/', '$');
							tempOrNot="PO";
						}
						boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);
						int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
						int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
						//boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);
						//String strOldPoNumber=oldPoNumber.replace('/', '$'); // for files path taken time replace / with $ 
						for (int i = 0,j=0; i < imagesAlreadyPresent; i++) {
							File img = new File(rootFilePath+"TEMP_PO//"+oldPoNumber+"_Part"+i+".jpg");
							if(img.exists()){
								Files.copy(Paths.get(moveFilePath+"\\TEMP_PO\\"+oldPoNumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\"+tempOrNot+"\\"+po_Number+"_Part"+i+".jpg"));
							}
						}
						for (int i = 0,j=0; i < pdfAlreadyPresent; i++) {
							File file = new File(rootFilePath+"TEMP_PO//"+oldPoNumber+"_Part"+i+".pdf");
							if(file.exists()){
								Files.copy(Paths.get(moveFilePath+"\\TEMP_PO\\"+oldPoNumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\"+tempOrNot+"\\"+po_Number+"_Part"+i+".pdf"));
							}
						}
						/******************************************FOR UPLOAD FILE**************************************************/
						/***************************************file move start************************************************/
						//int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
						//int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
						int imgCount=imagesAlreadyPresent;
						int pdfCount=pdfAlreadyPresent;
						//String strFilePath=rootFilePath+"TEMP_PO//";
						for (int i =0; i < files.length; i++) {

							MultipartFile multipartFile = files[i];
							if(!multipartFile.isEmpty()){
								try {
									//file directory name is constructing here 
									File dir = new File(strFilePath+File.separator);
									//checking is the directory exits or not if not create the directory for string images
									if (!dir.exists())
										dir.mkdirs();

									String filePath ="";
									System.out.println(multipartFile.getOriginalFilename());
									//checking the extension of file and creating the file based on extension 
									if(multipartFile.getOriginalFilename().endsWith(".pdf")){
										filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+pdfCount+".pdf";
										pdfCount++;
									}else{
										filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+imgCount+".jpg";
										imgCount++;
									}

									//giving file path to file object
									File convFile = new File(filePath);
									//creating the image or pdf file 
									convFile.createNewFile(); 
									FileOutputStream fos = new FileOutputStream(convFile); 
									fos.write(multipartFile.getBytes());
									fos.close(); 

								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Image NOT Uploaded");
									
								}
							}
						}//For Loop
						PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(strFilePath,poNumber,tempOrNot,model,request); // call the getting pdf and images for showing purpose
						session.setAttribute("PoNumber",oldPoNumber);
						session.setAttribute("strfinalAmount",strfinalAmount);
					}// if condition end
					else{
						if(!response.equalsIgnoreCase("response")){
							model.addAttribute("message1","We found a Malfunction, Please once logout and login for further operations.");
							return "response";	}	}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails.auditLog("0",user_id,"submit Modified PO","success",String.valueOf(site_id));
		return response;
	}
	/*@RequestMapping(value = "/updateMarketingTempPoPage.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String updateMarketingTempPoPage(HttpServletRequest request, HttpSession session,Model model,
			@RequestParam(value="file",required = false) MultipartFile[] files){
		
		String strResponse="";
		//int noOfPdfs=0;
		//String imgNumber="";
		String moveFilePath="";
		String rootFilePath = "";
		String strSiteId="";//request.getParameter("siteId");
		int site_id =0;
		String user_id =""; 
		String poNumber =""; //request.getParameter("poNo");
		//String oldPONumber=request.getParameter("oldPONumber");
		int getLocalPort = request.getLocalPort();
		String response ="";
		poNumber=request.getParameter("poNo")==null ? "" : request.getParameter("poNo");
		user_id=session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		strSiteId=request.getParameter("siteId")==null ? "" : request.getParameter("siteId");
		String sessionSite_id=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		request.setAttribute("sessionSite_id",sessionSite_id);
		if(poNumber==null || poNumber.equals("")){
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		site_id=Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		if(getLocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}
		else{
			rootFilePath = validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();}
		 	 // if any attachments already existed deleted use below code
		int count=files.length;
		if(count==1){
			boolean strStatus=objMarketingDeptService.checkIsMarketingUpdateOrNot(session,request,true,false);
			//boolean status=objPurchaseDeptIndentrocess.checkIsUpdateOrNot(session,request,true,true);
			if(strStatus){
				model.addAttribute("message1","Unable to modify PO.");
				return "response";
			}
		}
		
		//response=getAndCheckPOBOQQuantity(request,session,true,true);
		//if(!response.contains("BOQ")){
		response = objPurchaseDeptIndentrocess.updateTempPoPage(model, request, session);
		boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);//}
		//if(!response.contains("BOQ")){
		//if(response.equalsIgnoreCase("Success")){
			// normal po approve methode executed ,below true is use to check the cancel po condition
			strResponse=objPurchaseDeptIndentrocess.SavePoApproveDetails(poNumber,strSiteId,user_id,request,"true"); 
			
			 if(strResponse.equalsIgnoreCase("Success")){

					model.addAttribute("response",request.getAttribute("result") == null ? "" : request.getAttribute("result").toString());

				}
				else{
					model.addAttribute("response1","Already Approved or Rejected or Cancelled successfully ");

				}
		*//******************************************************this is for pdf download*************************************************************//*
				int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
				int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
				int imgCount=imagesAlreadyPresent;
				int pdfCount=pdfAlreadyPresent;
				String strFilePath=rootFilePath+"TEMP_PO//";
				for (int i =0; i < files.length; i++) {
					
					MultipartFile multipartFile = files[i];
					if(!multipartFile.isEmpty()){
					try {
						//file directory name is constructing here 
							File dir = new File(strFilePath+File.separator);
						//checking is the directory exits or not if not create the directory for string images
							if (!dir.exists())
								dir.mkdirs();
							
							String filePath ="";
							System.out.println(multipartFile.getOriginalFilename());
							//checking the extension of file and creating the file based on extension 
							if(multipartFile.getOriginalFilename().endsWith(".pdf")){
								filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+pdfCount+".pdf";
								pdfCount++;
							}else{
								filePath = dir.getAbsolutePath()+ File.separator + poNumber+"_Part"+imgCount+".jpg";
								imgCount++;
							}
						
							//giving file path to file object
							File convFile = new File(filePath);
							//creating the image or pdf file 
						    convFile.createNewFile(); 
						    FileOutputStream fos = new FileOutputStream(convFile); 
						    fos.write(multipartFile.getBytes());
						    fos.close(); 
						
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Image NOT Uploaded");
							
						}
					}
				}//For Loop
		
		*//*********************************************************this is for pdf download end*******************************************************//* 
		//}
		//else{
			model.addAttribute("response1"," not successfully updated ");

		//}
		//}else{ // this is for checking the quantity and getting the result more than boq
		//	model.addAttribute("response1",response);
		//}
		
		SaveAuditLogDetails.auditLog("0",user_id,"updateTempPoPage ","Success",String.valueOf(site_id));
	return "response";
	}*/
	
	@RequestMapping(value = "/MarketingReport.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String MarketingReport(Model model, HttpServletRequest request,HttpSession session) {
		
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
		Map<String, String> siteDetails = null;
		model.addAttribute("marketingExpenditureModelForm", new MarketingDeptBean()); 
		siteDetails = new IndentSummaryDao().getSiteDetails();// get all active site names with ids to show in view expenditure sub module click on that
		//List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(marketingDeptId);
		/*Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateOnly = new SimpleDateFormat("dd-MM-yy");
		String toDate=dateOnly.format(cal.getTime());
		String tempData=toDate.split("-")[0];
		String fromDate=toDate.replace(tempData,"01");*/
		//request.setAttribute("productData",objMarketingDeptService.gettingProductData(fromDate,toDate));
		request.setAttribute("siteDetails",siteDetails);
		
		//request.setAttribute("totalProductsList", totalProductList);
		
		model.addAttribute("AvailableDateOrNot","");
		return "marketing/marketing_report";
	}
	@RequestMapping(value = "/defaultProductDetais.spring", method = RequestMethod.GET)
	@ResponseBody
	public String defaultProductDetais() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateOnly = new SimpleDateFormat("dd-MM-yy");
		String toDate=dateOnly.format(cal.getTime());
		String tempData=toDate.split("-")[0];
		String fromDate=toDate.replace(tempData,"01");
		//String siteData = objMarketingDeptIndentrocess.loadAndSetLocationDatachildProductId);
		//System.out.println("In controller resulted data :"+empData+"|");
		return objMarketingDeptService.gettingProductData(fromDate,toDate);
	}
	// getting the data when the user entered it will gave the result
	@RequestMapping(value = "/selectedProductDetailsForGraph.spring", method = RequestMethod.GET)
	@ResponseBody
	public String selectedProductDetailsForGraph(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate,
			@RequestParam("SiteData") String SiteData,HttpServletRequest request) {
		//Calendar cal = Calendar.getInstance();
		//SimpleDateFormat dateOnly = new SimpleDateFormat("dd-MM-yy");
		//String toDate=dateOnly.format(cal.getTime());
		//String tempData=toDate.split("-")[0];
		//String fromDate=toDate.replace(tempData,"01");
		//String siteData = objMarketingDeptIndentrocess.loadAndSetLocationDatachildProductId);
		//System.out.println("In controller resulted data :"+empData+"|");
		return objMarketingDeptService.selectedProductDetailsForGraph(fromDate,toDate,SiteData);
	}
}
