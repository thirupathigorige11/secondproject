package com.sumadhura.in;


import java.awt.PageAttributes.MediaType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import com.sumadhura.util.CommonUtilities;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.DateFormat;
import com.ibm.wsdl.util.IOUtils;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.text.pdf.codec.Base64.InputStream;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.MenuDetails;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.bean.userDetails;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.service.CentralSiteIndentrocessService;
import com.sumadhura.service.DCFormService;
import com.sumadhura.service.EmailFunction;
import com.sumadhura.service.IndentCreationService;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.MarketingDepartmentService;
import com.sumadhura.service.PurchaseDepartmentIndentrocessService;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.IndentIssueDaoImpl;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.AESDecrypt;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DateUtil;
//import com.sumadhura.util.EmailService_CustomerMails;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.transdao.MarketingDepartmentDao;

@Controller
public class PurchaseDepartmentIndentrocessController extends UIProperties {

	public @interface EnableAutoConfiguration {

	}

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;


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
	@Qualifier("cntlIndentprocess")
	CentralSiteIndentrocessService csips;

	@Autowired
	@Qualifier("MarketingDepartmentService")
	MarketingDepartmentService objMarketingDeptService;

	@Autowired
	@Qualifier("posClass")
	IndentCreationService ics;

	@Autowired
	private IndentCreationDao icd;

	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;

	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;

	@Autowired
	UtilDao objUtilDao;
	
	@Autowired
	@Qualifier("MarketingDepartmentDao")
	MarketingDepartmentDao objMarketingDepartmentIndentProcessDao;

	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	IndentCreationController IndentCreationController;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private Object poRefference_Date;


	@RequestMapping(value = "/vendorlogin.spring", method =  {RequestMethod.POST,RequestMethod.GET})
	public String vendorlogin(Model model, HttpServletRequest request,HttpSession session) throws Exception {


		String StrResponse=request.getParameter("submitButton")==null ? "0":request.getParameter("submitButton");
		if(StrResponse.equalsIgnoreCase("true")){


			request.setAttribute("indentNumber",request.getParameter("indentNumber"));
			request.setAttribute("indentCreationDetailsIdForenquiry",request.getParameter("indentCreationDetailsIdForenquiry"));
			request.setAttribute("vendordata",request.getParameter("vendordata"));


		}
		else{

			AESDecrypt decrypt=new AESDecrypt();
			String strData = request.getParameter("data");

			strData = strData.replace(" ","+");
			String strDecrypt =decrypt.decrypt("AMARAVADHIS12345",strData);

			/*("indentNumber=101&vendordata=pavan@@1234@@nlh@@VND01"+
	                   "&uname=VND01&pass=xyz&indentCreationDetailsIdForenquiry=101");*/
			String indentNum[] = null;
			String indentCreationId[] = null;
			String strVendorData[] = null;
			if(strDecrypt.contains("&")){
				String data[] = strDecrypt.split("&");

				String strIndentNumberbefore = data[0];
				String vendorData=data[1];
				String uName = data[2];
				String password = data[3];
				String indentCreationIdForEnquiry = data[4];
				indentNum = strIndentNumberbefore.split("=");
				indentCreationId=indentCreationIdForEnquiry.split("=");
				strVendorData=vendorData.split("=");			


			}



			request.setAttribute("indentNumber",indentNum[1]);
			request.setAttribute("indentCreationDetailsIdForenquiry",indentCreationId[1]);
			request.setAttribute("vendordata",strVendorData[1]);

		}
		return "vendorlogin";
	}

	@RequestMapping(value = "/vendorloginsubmit.spring", method = {RequestMethod.POST,RequestMethod.GET})
	public String vendorloginsubmit(Model model, HttpServletRequest request,HttpSession session) throws Exception {

		List<ProductDetails> IndentDetails = null;
		List<IndentCreationBean> IndentDtls = null;

		int indentNumber = 0;
		int siteWiseIndentNo = 0;
		String vendordata = "";
		String[] vdata = new String[0];
		String uname = "";
		String pass = "";
		String address = "";
		String indentCreationDetailsIdForenquiry = "";

		String StrResponse=request.getParameter("submitButton")==null ? "0":request.getParameter("submitButton");
		if(StrResponse.equalsIgnoreCase("true")){
			indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			siteWiseIndentNo = objPurchaseDeptIndentrocess.getSiteWiseIndentNo(indentNumber);
			vendordata = request.getParameter("vendordata");
			vdata = vendordata.split("@@");


			uname = request.getParameter("uname");
			pass = request.getParameter("pass");
			address=request.getParameter("address");

			indentCreationDetailsIdForenquiry = request.getParameter("indentCreationDetailsIdForenquiry");
		}
		else{


			AESDecrypt decrypt=new AESDecrypt();
			String strData = request.getParameter("data");

			strData = strData.replace(" ","+");
			String strDecrypt =decrypt.decrypt("AMARAVADHIS12345",strData);

			/*("indentNumber=101&vendordata=pavan@@1234@@nlh@@VND01"+
		                   "&uname=VND01&pass=xyz&indentCreationDetailsIdForenquiry=101");*/
			String indentNum[] = null;
			String indentCreationId[] = null;
			String strVendorData[] = null;

			if(strDecrypt.contains("$$$")){
				String data[] = strDecrypt.split("\\$\\$\\$");
				//System.out.println("strDecrypt  "+strDecrypt);
				String strIndentNumberbefore = data[0];
				String vendorData=data[1];
				uname = data[2].split("=")[1];
				pass = data[3].split("=")[1];
				String indentCreationIdForEnquiry = data[4];
				if(data[5] != null){
					address=data[5].split("=")[1];
				}
				indentNum = strIndentNumberbefore.split("=");
				indentCreationId=indentCreationIdForEnquiry.split("=");
				strVendorData=vendorData.split("=");			


			}


			//System.out.println("after vendor submit"+indentNumber);
			indentNumber = Integer.parseInt(indentNum[1]);
			siteWiseIndentNo = objPurchaseDeptIndentrocess.getSiteWiseIndentNo(indentNumber);
			indentCreationDetailsIdForenquiry = indentCreationId[1];
			vendordata = strVendorData[1];
			vdata = vendordata.split("@@");


		}

		boolean isValidPassword = objPurchaseDeptIndentrocess.getVendorPasswordInDB(uname,indentNumber,pass);

		List<MenuDetails> list = new ArrayList<MenuDetails>();
		session.setAttribute("menu", list);

		if(isValidPassword)
		{

			//int indentNumber = 0;	

			//String indentCreationDetailsIdForenquiry = request.getParameter("indentCreationDetailsIdForenquiry");


			ProductDetails objProductDetails = new ProductDetails();			
			model.addAttribute("CreatePOModelForm", objProductDetails);


			model.addAttribute("indentNo", indentNumber);
			model.addAttribute("siteWiseIndentNo", siteWiseIndentNo);
			model.addAttribute("vendorName", vdata[0]);
			model.addAttribute("strGSTINNumber", vdata[1]);
			model.addAttribute("vendorAddress", vdata[2]);
			model.addAttribute("vendorId", vdata[3]);
			model.addAttribute("address", address);
			request.setAttribute("vendorName", vdata[0]);
			//model.addAttribute("productsMap", dcFormService.loadProds());
			model.addAttribute("pass",pass);
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());
			IndentDetails = csips.getPurchaseIndentDtlsLists(indentCreationDetailsIdForenquiry,vdata[3]);

			if(indentNumber==0){
				model.addAttribute("SiteName", "");
				model.addAttribute("SiteId", "");
				model.addAttribute("productList",objPurchaseDeptIndentrocess.combinedDetailsChildProductWise(IndentDetails));
			}
			else{
				IndentDtls = ics.getIndentCreationLists(indentNumber);
				model.addAttribute("SiteName", IndentDtls.get(0).getSiteName());
				model.addAttribute("SiteId", IndentDtls.get(0).getSiteId());
				model.addAttribute("productList",IndentDetails);
			}
			return "CreatePO_Vendor";
		}
		request.setAttribute("message","Already submitted Quotation.");
		return "VendorSubmit";

	}








	/*@RequestMapping(value = "/OLDviewPurchaseIndent.spring", method = RequestMethod.GET)
	public String OLDviewPurchaseIndent(Model model, HttpServletRequest request,HttpSession session) {
		IndentCreationBean icb = new IndentCreationBean();
		String strSiteId = "";
		String user_id = "";
		int indentNumber = 0;
		String reqSiteName = "";
		//iib.setProjectName(iis.getProjectName(session));
		List<IndentCreationBean> IndentDetails = null;
		List<IndentCreationBean> IndentDtls = null;
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			reqSiteName = request.getParameter("siteName");
			System.out.println("the site name "+reqSiteName);
			model.addAttribute("indentCreationModelForm", icb);
			model.addAttribute("productsMap", iis.loadProds());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			//model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
			icb.setIndentNumber(indentNumber);	
			icb.setSiteName(reqSiteName);	

			IndentDetails = null;
			IndentDtls = null;
			IndentDtls = ics.getIndentCreationLists(indentNumber);// even though it is a list of objects but it has only one Object.
			//IndentDtls.get(0).setSiteName(reqSiteName);	//so that i added like this to the object at 1st index.
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(IndentDtls.size()==0){
			model.addAttribute("responseMessage","Enter Correct Number");
			List<IndentCreationBean> listofCentralIndents = null;
			listofCentralIndents = objPurchaseDeptIndentrocess.getAllPurchaseIndents();
			model.addAttribute("listofCentralIndents",listofCentralIndents);
			request.setAttribute("totalProductsList",objPurchaseDeptIndentrocess.getAllProducts());
			SaveAuditLogDetails.auditLog("0",user_id,"Entered Wrong Purchase Indent Number","Failure",strSiteId);
			return "PendingIndents";
			}
		model.addAttribute("IndentDtls",IndentDtls);
		model.addAttribute("reqSiteName",reqSiteName);
		IndentDetails = csips.getPurchaseIndentDetailsLists(indentNumber);
		model.addAttribute("IndentDetails",IndentDetails);
		SaveAuditLogDetails.auditLog("0",user_id,"Showing Purchase Indent","Success",strSiteId);
		return "AllIndent";
	}*/

	@RequestMapping(value = "/viewPurchaseIndent.spring", method = {RequestMethod.POST,RequestMethod.GET})
	public String viewPurchaseIndent(Model model, HttpServletRequest request,HttpSession session) {
		int indentNumber = 0;
		int site_id = 0;
		String user_id = "";
		List<IndentCreationBean> IndentDtls = null;
		List<IndentCreationBean> IndentDetails = null;
		String strUrlName = "";
		String siteId="";// this is used to send enquiry mails check and take here
		String childProduct="";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			strUrlName =  request.getParameter("url");
			siteId=request.getParameter("siteId") == null ? "" : request.getParameter("siteId");
			
			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			String strCreationDate = request.getParameter("creationDate");
			model.addAttribute("strCreationDate",strCreationDate);
			model.addAttribute("url","getIndentCreationDetails.spring");
			model.addAttribute("strIndentNumber",String.valueOf(indentNumber));
			IndentDtls = ics.getIndentCreationLists(indentNumber);
			if(IndentDtls.size()==0){
				model.addAttribute("responseMessage","Enter Correct Number");
				List<IndentCreationBean> listofCentralIndents = null;
				listofCentralIndents = objPurchaseDeptIndentrocess.getAllPurchaseIndents();
				model.addAttribute("listofCentralIndents",listofCentralIndents);
				request.setAttribute("totalProductsList",objPurchaseDeptIndentrocess.getAllProducts());
				SaveAuditLogDetails.auditLog("0",user_id,"Entered Wrong Purchase Indent Number","Failure",String.valueOf(site_id));
				return "PendingIndents";
			}
			String strRequestSiteId = request.getParameter("siteId"); 
			model.addAttribute("IndentDtls",IndentDtls);
			String ccEmails = objPurchaseDeptIndentrocess.getVendorCCEmails(siteId); // based on site Id get CcMails from properties files
			model.addAttribute("ccEmails",ccEmails);
			IndentDetails = csips.getPurchaseIndentDetailsLists(indentNumber,strRequestSiteId);
			// this is for closed indents written by ravi
			for(int i=0;i<IndentDetails.size();i++){
				if(!IndentDetails.get(i).getClosed_Indent_Quan().equals("0")){
					childProduct+="Closed "+IndentDetails.get(i).getChildProduct1()+" By "+IndentDetails.get(i).getClosed_Indent_Quan()+" Quantity "+",";
				}
			}
			if(childProduct!=null && !childProduct.equals("")){
				childProduct =  childProduct.substring(0,childProduct.length()-1);
			}
			// this is for closed indent show in note place
			model.addAttribute("IndentDetails",IndentDetails);
			model.addAttribute("urlName",strUrlName+"?site="+strRequestSiteId);
			model.addAttribute("closedProducts",childProduct);

		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Showing Purchase Indent","Success",String.valueOf(site_id));
		return "AllIndent";
	}

	// this is for create PO user click on create po Button then it will call normal po from purchase dept
	@RequestMapping(value = "/createPODetails.spring", method = RequestMethod.POST)
	public String createPODetails(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String KILO_BYTE="";
		String allSites="";
		try {
			// from session we taken these two userId and siteId for 
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			ProductDetails objProductDetails = new ProductDetails();

			//ics.createPO(model, request);
			List<ProductDetails> IndentDetails = null;
			String indentNo = request.getParameter("indentNumber");
			String siteWiseIndentNo = request.getParameter("siteWiseIndentNo");
			String SiteId=request.getParameter("siteId");
			String SiteName=request.getParameter("siteName");
			String vendorNumber=request.getParameter("vendorNumber")==null ? "" : request.getParameter("vendorNumber");
			String VendorName=request.getParameter("vendorName"+vendorNumber);
			String strCreationDate = request.getParameter("strCreationDate");

			model.addAttribute("CreatePOModelForm", objProductDetails);
			model.addAttribute("indentNo", indentNo);
			model.addAttribute("siteWiseIndentNo", siteWiseIndentNo);
			model.addAttribute("SiteId", SiteId);
			model.addAttribute("SiteName", SiteName);
			model.addAttribute("strCreationDate", strCreationDate);
			model.addAttribute("Allsites",allSites);
			model.addAttribute("url","getIndentCreationDetails.spring");
			
			model.addAttribute("listOfGetProductDetails",objPurchaseDeptIndentrocess.getProductDetailsLists(indentNo, VendorName,request,"false"));
			model.addAttribute("url","getIndentCreationDetails.spring");
			model.addAttribute("productsMap", dcFormService.loadProds(String.valueOf(site_id)));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			//what is the use of this code ?

			IndentDetails= objPurchaseDeptIndentrocess.createPO(model, request,session);	
			model.addAttribute("productList",IndentDetails);	


			List<ProductDetails> listTermsAndCondition = objPurchaseDeptIndentrocess.getTermsAndConditions(SiteId);
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());// count the no of condition we need to iterate loop
			KILO_BYTE=validateParams.getProperty("KILO_BYTE") == null ? "" : validateParams.getProperty("KILO_BYTE").toString(); // for size of pdf to check upload time
			model.addAttribute("KILO_BYTE",KILO_BYTE);
			String ccEmails = objPurchaseDeptIndentrocess.getDefaultCCEmails(SiteId); // based on site Id get CcMails from properties files
			model.addAttribute("ccEmails",ccEmails);


			//System.out.println(IndentDetails);
			//System.out.println("the site name is "+SiteId);
			objProductDetails.setStrProjectName(iis.getProjectName(session));
		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Entering PO Details","Success",String.valueOf(site_id));
		return "CreatePO";
	}
	@RequestMapping(value = "/printIndent.spring", method = RequestMethod.POST)
	public String printIndent(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		try {
			session = request.getSession(true);
			//site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			site_id = Integer.parseInt(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());



			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objPurchaseDeptIndentrocess.printIndent(model, request, site_id, user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Printing Indent","Success",String.valueOf(site_id));
		return "PrintIndent";
	}
	/*@RequestMapping(value = "/CreatePO.spring", method = RequestMethod.POST)
	public String createPO(Model model, HttpServletRequest request,HttpSession session) {
		/*session = request.getSession(true);
		int site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String indentNumber= request.getParameter("indentNumber");
		model.addAttribute("indentNumber",indentNumber);*/


	/*	ProductDetails objProductDetails = new ProductDetails();

		//ics.createPO(model, request);

		model.addAttribute("CreatePOModelForm", objProductDetails);
		model.addAttribute("productList", objPurchaseDeptIndentrocess.createPO(model, request,session));	
		objProductDetails.setStrProjectName(iis.getProjectName(session));




		return "CreateIndentPO";
	}*/



	@RequestMapping(value = "/sendenquiry.spring", method = RequestMethod.POST)
	public String sendPurchaseIndentToVendor(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		try {
			session = request.getSession(true);

			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String indentNumber= request.getParameter("indentNumber");
			String siteId= request.getParameter("siteId");
			String siteWiseIndentNo= request.getParameter("siteWiseIndentNo");
			String siteName= request.getParameter("siteName");
			model.addAttribute("indentNumber",indentNumber);
			model.addAttribute("siteId",siteId);
			model.addAttribute("siteWiseIndentNo",siteWiseIndentNo);
			model.addAttribute("siteName",siteName);
			List<Map<String, Object>> listReceiverDtls=objPurchaseDeptIndentrocess.sendenquiry(model, request, site_id, user_id,siteId);

			//	List<Map<String, Object>> listReceiverDtls=ics.getVendorOrSiteAddress(siteId);
			model.addAttribute("listReceiverDtls",listReceiverDtls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Sending Enquiry","Success",String.valueOf(site_id));
		return "SendEnquiry";
	}
	@RequestMapping(value = "/fillForm.spring", method = RequestMethod.POST)
	public String fillForm(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			ProductDetails objProductDetails = new ProductDetails();

			//ics.createPO(model, request);
			List<ProductDetails> IndentDetails = null;
			String indentNo = request.getParameter("indentNumber");
			String SiteId=request.getParameter("siteId");
			String SiteName=request.getParameter("siteName");
			String siteWiseIndentNo = request.getParameter("siteWiseIndentNo");

			model.addAttribute("url","getIndentCreationDetails.spring");
			model.addAttribute("CreatePOModelForm", objProductDetails);
			model.addAttribute("indentNo", indentNo);
			model.addAttribute("siteWiseIndentNo", siteWiseIndentNo);
			model.addAttribute("SiteId", SiteId);
			model.addAttribute("SiteName", SiteName);
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			//what is the use of below code ?


			request.setAttribute("isFillForm","yes");
			objPurchaseDeptIndentrocess.sendenquiry(model, request, site_id, user_id,SiteId);

			IndentDetails= objPurchaseDeptIndentrocess.createPO(model, request,session);	
			model.addAttribute("productList",IndentDetails);	


			/*List<ProductDetails> listTermsAndCondition = objPurchaseDeptIndentrocess.getTermsAndConditions();
				model.addAttribute("listTermsAndCondition",listTermsAndCondition);*/



			//System.out.println(IndentDetails);
			//System.out.println("the site name is "+SiteId);
			objProductDetails.setStrProjectName(iis.getProjectName(session));
		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Filling Enquiry Form","Success",String.valueOf(site_id));
		return "CreatePO_FillForm";
	}
	@RequestMapping(value = "/sendenquiry2.spring", method = RequestMethod.POST)
	public String sendPurchaseIndentToVendor2(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			String indentNumber= request.getParameter("indentNo1");
			String strSiteId= request.getParameter("strSiteId1");
		//	System.out.println("the site name in controller"+strSiteId);
			model.addAttribute("indentNumber",indentNumber);
			List<Map<String, Object>> listReceiverDtls=objPurchaseDeptIndentrocess.sendenquiry2(model, request, site_id, user_id,strSiteId);
			model.addAttribute("listReceiverDtls",listReceiverDtls);
		} catch (NumberFormatException e) {

			e.printStackTrace();//TODO Auto-generated catch block
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Sending Enquiry","Success",String.valueOf(site_id));
		return "SendEnquiry";
	}
	@RequestMapping(value = "/SavePoDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String SavePoDetails(Model model, HttpServletRequest request,HttpSession session,
			@RequestParam(value="file",required=false) MultipartFile[] files) {
		int site_id = 0;
		String user_id = "";
		String response = "";

		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		String strValue="";
		String po_Number="";
		String filePath="";
		int strCount=0;
		int portNumber=request.getLocalPort();
		String rootFilePath = "";
		String poLevelComments="";
		String serverFile="";
		int imgCount=0;
		int pdfCount=0;
		String type="";
		
		String  indentNumber="";
		String vendorId="";
		String finalAmt="";
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String strIndentNo = request.getParameter("indentNumber")==null ? "" :request.getParameter("indentNumber");
			String strVendorId = request.getParameter("vendorId")==null ? "" :request.getParameter("vendorId");
			String strfinalAmount=request.getParameter("ttlAmntForIncentEntry")==null ? "" :request.getParameter("ttlAmntForIncentEntry");
			indentNumber=session.getAttribute("strIndentNo")== null ? "" : session.getAttribute("strIndentNo").toString();
			vendorId=session.getAttribute("strVendorId")== null ? "" : session.getAttribute("strVendorId").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			
			//allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			// this is used to take from financial year for po number ex:2018-19
			if(strIndentNo==null || strIndentNo.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return strValue="response";
			}
			if(strIndentNo.equals(indentNumber) && strVendorId.equals(vendorId) && strfinalAmount.equals(finalAmt)){
				model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
				return strValue="response";
			}else{
			// boq purpose written thi sone
			
			//int k=files.length;
			poLevelComments=request.getParameter("note") == null ? "" : request.getParameter("note").toString(); // user any change in product given comments at the time of po creation
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
			
			// these are taken from service methode save in table
			request.setAttribute("versionNo", versionNo);
			request.setAttribute("refferenceNo", refferenceNo);
			request.setAttribute("strPoPrintRefdate", strPoPrintRefdate);
			// display in success page
			model.addAttribute("versionNo",versionNo);
			model.addAttribute("refferenceNo",refferenceNo);
			model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
			request.setAttribute("poPage","true");
			request.setAttribute("PoLevelComments",poLevelComments);
			
			ProductDetails objProductDetails = new ProductDetails();
			//ics.createPO(model, request);
			model.addAttribute("CreatePOModelForm", objProductDetails);
			
			strValue=getAndCheckPOBOQQuantity(request,session,true,false);
			if(!strValue.contains("BOQ")){
			strValue=objPurchaseDeptIndentrocess.SavePoDetails(model, request,session,strFinacialYear);}
			
			
			//int count=files.length;
			//if(count==0){request.setAttribute("uploadorNot","false");}else{request.setAttribute("uploadorNot","true");}
			/******************************************FOR UPLOAD FILE**************************************************/
			if(!strValue.contains("BOQ")){
				String poNumber=request.getAttribute("strPONumber").toString();
				model.addAttribute("poNumber",poNumber);
			if(strValue.equalsIgnoreCase("CreatePOFinalPage")){ // temp po generate then if condition executed
				filePath=rootFilePath+"TEMP_PO//";
				po_Number=poNumber;
				type="TEMP_PO";
			}else{
				filePath=rootFilePath+"PO//";
				po_Number=poNumber.replace('/','$');
				type="PO";
			}
			
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
		/*	************************************************retrive and show the images or pdf start*****************************************************************/
			// to show in success page the images or pdf so written this	
			
			loadPoImgAndPdfFiles(filePath,po_Number,type,model,request); // call the getting pdf and images for showing purpose
			
				/*********************************************FOR UPLOAD FILE*****************************************************/
		
		}
		session.setAttribute("strIndentNo",strIndentNo);
		session.setAttribute("strVendorId",strVendorId);
		session.setAttribute("strfinalAmount",strfinalAmount);
		}/*else{
			model.addAttribute("message1",strValue);
			strValue="response";
		}*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"PO Created",response,String.valueOf(site_id));
		
		return strValue;

	}



	@RequestMapping(value = "/SaveEnquiryForm.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String SaveVendorPODetails(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		boolean fillform=false;
		String strVendorId ="";
		String siteWiseIndentNo ="";
		try {
			session = request.getSession(true);
			//site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			//user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			ProductDetails objProductDetails = new ProductDetails();
			//ics.createPO(model, request);
			model.addAttribute("CreatePOModelForm", objProductDetails);
			fillform = request.getParameter("fillform") == null ? false : Boolean.valueOf(request.getParameter("fillform"));
			strVendorId = request.getParameter("vendorId")== null ? "" : request.getParameter("vendorId");
			siteWiseIndentNo = request.getParameter("siteWiseIndentNo")==null ? "" : request.getParameter("siteWiseIndentNo");
			request.setAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
			if(strVendorId==null || strVendorId.equals("") || siteWiseIndentNo==null || siteWiseIndentNo.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			response = objPurchaseDeptIndentrocess.SaveEnquiryForm(model, request,session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if(response.equals("Success"))
		{
			SaveAuditLogDetails.auditLog("0",user_id,"Save Enquiry Form",response,String.valueOf(site_id));
			request.setAttribute("message", "Thank You");
			request.setAttribute("response", "For submitting your Quotation");
			request.setAttribute("msg", "Hopefully we will get back to you soon.");
			if(fillform){
				return "response";
			}else{
			return "Vendor_Response";}
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Save Enquiry Form",response,String.valueOf(site_id));
		request.setAttribute("message", "Error Occured. Click Link and Try Again...");
		return "Vendor_Response";
	}

	/*@RequestMapping(value = "/loadCreatePOPage.spring", method = RequestMethod.POST)
	public String loadCreatePOPage(Model model, HttpServletRequest request,HttpSession session) {
		/*session = request.getSession(true);
		int site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String indentNumber= request.getParameter("indentNumber");
		model.addAttribute("indentNumber",indentNumber);*/


	/*ProductDetails objProductDetails = new ProductDetails();

		//ics.createPO(model, request);

		model.addAttribute("CreatePOModelForm", objProductDetails);
		model.addAttribute("productList", objPurchaseDeptIndentrocess.loadCreatePOPage(model, request));	
		objProductDetails.setStrProjectName(iis.getProjectName(session));


		//request.setAttribute("productList", productList);

		return "CreatePO";

	}*/

	@RequestMapping(value = "/getIndentsProductWise.spring", method = RequestMethod.POST)
	public String getIndentsProductWise(Model model, HttpServletRequest request,HttpSession session)
			
	{

		int site_id = 0;
		String user_id = "";
		String product="";
		String subProduct="";
		String childProduct="";
		try {
			session = request.getSession(true);
			product=request.getParameter("combobox_Product")==null ? "" : request.getParameter("combobox_Product");
			subProduct=request.getParameter("combobox_SubProduct")==null ? "" : request.getParameter("combobox_SubProduct");
			childProduct=request.getParameter("combobox_ChildProduct")==null ? "" : request.getParameter("combobox_ChildProduct");
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			model.addAttribute("getIndentsProductWiseModelForm",  new ProductDetails());
			//System.out.println(product+","+subProduct+","+childProduct);

			List<ProductDetails> productList = objPurchaseDeptIndentrocess.getIndentsProductWise(product,subProduct,childProduct);

			request.setAttribute("productList", productList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Sending Enquiry","Success",String.valueOf(site_id));
		return "ProductWiseIndents";
	}

	//This method is called in 3 places
	//1. Get indent details - view comparison
	//2. View pending PO for approval - view comparison
	//3. Print PO - view comparison
	@RequestMapping(value = "/getComparisionStatement.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getComaprisionStatement(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		List<Map<String, Object>> productList = null;
		String url="";
		String indentDate="";
		try {

			//session = request.getSession(true);
			//site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			//user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			//ProductDetails objProductDetails = new ProductDetails();

			//ics.createPO(model, request);
			//List<ProductDetails> IndentDetails = null;
			String indentNo = request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber");
			//String SiteId=request.getParameter("siteId");
			String SiteName=request.getParameter("siteName")==null ? "" : request.getParameter("siteName");
			if(indentNo==null || indentNo.equals("") || SiteName==null || SiteName.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			indentDate=request.getParameter("indentDate")==null ? "-" : request.getParameter("indentDate");
			url=request.getParameter("url")==null ? "" : request.getParameter("url");


			productList = objPurchaseDeptIndentrocess.getComparisionDtls( request,session);	
			//model.addAttribute("productList",productDetails);	
			if(productList.size()>0){



				request.setAttribute("productList", productList);
			}else{
				request.setAttribute("productList1", productList);
			}

			model.addAttribute("indentNo", indentNo);
			model.addAttribute("siteName", SiteName);
			model.addAttribute("indentDate", indentDate);
			//System.out.println(IndentDetails);


		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Entering PO Details","Success",String.valueOf(site_id));
		if(productList != null && productList.size() > 0){
			return "Comparison";
		}else{

			model.addAttribute("message1", "Sorry ! Comparison not available");
			model.addAttribute("urlForActivateSubModule",url);
			model.addAttribute("comparison",true);

			return "Comparison_response";
		}




	}

	@RequestMapping(value ="/ViewPoPendingforApproval.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ViewPoPendingforApproval(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		String tempPoNumber = "";
		ModelAndView model = null;
		String user_id="";
		String vendorId="";
		String siteIds="";
		
		List<IndentCreationBean> indentgetData = null;
		
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempPoNumber=request.getParameter("tempPoNumber");
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			vendorId=request.getParameter("vendorName") == null ? "" :request.getParameter("vendorName").toString(); 
			
			
			request.setAttribute("url","ViewPoPendingforApproval.spring");
			int portNumber=request.getLocalPort();
			String multiplePendingEmpForQuery = "";
			boolean MarketingHead = false;
			Map<String, String>  siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			if(portNumber==80){//LIVE
				if(user_id.equals("M008")){
					MarketingHead = true;
					multiplePendingEmpForQuery = "('M008'"+","+"'HYDMKT2')";//Srinivas Moramchetty	VP-Sales and Marketing, Arun	Vice president operatoins
				}//('M008','HYDMKT2')
			}else{
				if(user_id.equals("BMKT3.1")){
					MarketingHead = true;
					multiplePendingEmpForQuery = "('BMKT3.1'"+","+"'HMKT2.1')";
				}
			}
			// user can give from ot to date or ponumber then it will check and executed 
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(tempPoNumber)) {
				session = request.getSession(false);

				if (StringUtils.isNotBlank(site_id)) {
					
					
					if(MarketingHead){
						indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApprovalForMarketingHead(fromDate, toDate, user_id,tempPoNumber,"",multiplePendingEmpForQuery);//}
					}else{
						indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,tempPoNumber,"");//}
					}
					if(indentgetData != null && indentgetData.size() >0){
						request.setAttribute("showGrid", "true");
						
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failure";

					}
					model.addObject("indentgetData",indentgetData);
					request.setAttribute("PendingPoDetails",indentgetData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("tempPoNumber", tempPoNumber);
					model.addObject("message","");// this is used in approval page if any error occured then it will set 
					model.setViewName("ViewPendingPoForApproval");
					//model.setViewName("PendingPoApproval");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}
			} else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate)){ // when the user click on pending po approval then which are avilable that po are display
				if(MarketingHead){
					indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApprovalForMarketingHead(fromDate, toDate, user_id,tempPoNumber,"All",multiplePendingEmpForQuery);//}
				}else{
					indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,tempPoNumber,"All");
				}
				request.setAttribute("PendingPoDetails",indentgetData);
				if(indentgetData != null && indentgetData.size() >0){
				request.setAttribute("showGrid", "true"); // it will set then data will display 
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("message",""); // this is used in approval page if any error occured then it will set 
				model.addObject("tempPoNumber", tempPoNumber);
				response="success";
				//model.setViewName("PendingPoApproval");
				model.setViewName("ViewPendingPoForApproval");
			}
			else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or Temp Po Number!");
				model.addObject("indentgetData",indentgetData);
				request.setAttribute("PendingPoDetails",indentgetData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("message","");// this is used in approval page if any error occured then it will set 
				model.addObject("tempPoNumber", tempPoNumber);
				response="success";
				//model.setViewName("PendingPoApproval");
				model.setViewName("ViewPendingPoForApproval");
			}
		} 
		}	catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 

		//	SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		//	String user_id=String.valueOf(session.getAttribute("UserId"));
		//	String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		//	audit.auditLog("0",user_id,"Get Grn Details View",response,site_id1);

		return model;
	}
	@EnableAutoConfiguration
	@RequestMapping(value = "/getDetailsforPoApproval.spring", method ={ RequestMethod.POST,RequestMethod.GET})
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
		int strCount=0;
		int portNumber=request.getLocalPort();
		String rootFilePath =""; 
		String fromDate="";
		String toDate="";
		boolean isUpdate=false;
		try{

			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}

			strNewSiteId = request.getParameter("siteId");
			fromDate=request.getParameter("fromdate") == null ? "" : request.getParameter("fromdate").toString();
			toDate=request.getParameter("toDate") == null ? "" : request.getParameter("toDate").toString();
			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber").toString();
			siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString();
			isRequestFromEmailFunction=request.getParameter("mail") == null ? "" : request.getParameter("mail").toString();
			Mailpasswd=request.getParameter("password") == null ? "0" : request.getParameter("password").toString();
			isUpdate=Boolean.valueOf(request.getParameter("isUpdate") == null ? false : Boolean.valueOf(request.getParameter("isUpdate")));
			//imageClick = request.getParameter("imageClick") == null ? false : Boolean.valueOf(request.getParameter("imageClick"));
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){

				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response1=objPurchaseDeptIndentrocess.getDetailsforPoApproval(poNumber, siteId,request);
				String ccEmails = objPurchaseDeptIndentrocess.getTempPoCCEmails(poNumber);
				model.addAttribute("site_idForPriceMaster", siteId);
				model.addAttribute("ccEmails",ccEmails);
				// which was set in vendor details table
				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				
				
				// this will show in success page i.e approval page
				model.addAttribute("url","ViewPoPendingforApproval.spring");
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				model.addAttribute("response",true); // to show the buttons like approval,rject,cancel to approve persons
				model.addAttribute("poNumber",poNumber);// for po pdf download
				model.addAttribute("mailPasswd",Mailpasswd); //mail password prevous per not open
				model.addAttribute("fromDate",fromDate);
				model.addAttribute("toDate",toDate);
				if(isRequestFromEmailFunction.equalsIgnoreCase("true")){model.addAttribute("Email",isRequestFromEmailFunction);}
			/*	******************************************file download**********************************************************************/
				
				String filepath=rootFilePath+"TEMP_PO//";
				String type="TEMP_PO";
				loadPoImgAndPdfFiles(filepath,poNumber,type,model,request); // call the getting pdf and images for showing purpose
				
		/*************************************************file download end************************************************************************/		
				//System.out.println("poNumber "+poNumber);

				if(response1.equalsIgnoreCase("Success")  && !isUpdate) {

					viewToBeSelected = "Appoval_PO";
				}else if(isUpdate){
					viewToBeSelected = "Update_Approval_PO";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}

	
	/***********************************************************file download po purpose ***********************************************************/
	@RequestMapping(value = "/downloadPdf.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	//@RequestMapping(method = { RequestMethod.GET }, value = { "/downloadPdf"})
    public ResponseEntity<InputStreamResource> downloadPdf(HttpServletRequest request)
    {
		String filepath="";
		String strPO_Number="";
		String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString(); 
		String strPONumber=request.getParameter("strPONumber");
		String type=request.getParameter("type");
		if(type.equalsIgnoreCase("temp")){
			filepath=rootFilePath+"TEMP_PO//";
			strPO_Number=strPONumber;
		}else{filepath=rootFilePath+"PO//";
			strPO_Number=strPONumber.replace('/','$');
		}
        try
        {
        	//String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString(); 
            File file = new File(filepath+strPO_Number+".pdf");
            HttpHeaders respHeaders = new HttpHeaders();
            org.springframework.http.MediaType mediaType =org.springframework.http.MediaType.parseMediaType("application/pdf");
            respHeaders.setContentType(mediaType);
            respHeaders.setContentLength(file.length());
            respHeaders.setContentDispositionFormData("attachment", file.getName());
            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
        }
        catch (Exception e)
        {
        	
            String message = "Errore nel download del file "+strPONumber+".pdf; "+e.getMessage();
            logger.error(message, e);
            return new ResponseEntity<InputStreamResource>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	/*********************************************************file download end ***********************************************************************/
	// when the user click on the getting link call to this method
	@RequestMapping(value = "/showPODetailsToVendor.spring", method ={ RequestMethod.POST,RequestMethod.GET})
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
	//	string strvendorId="";
		int portNumber=request.getLocalPort();
		Calendar cal = Calendar.getInstance();
		int strCount=0;
		String rootFilePath ="";  
		 String poentryId="";
		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}

			strResponse=request.getParameter("submitButton")== null ? "0" : request.getParameter("submitButton");
			if(strResponse.equalsIgnoreCase("true")){ // vendor click on button check this condition

				poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
				siteId = request.getParameter("indentSiteId") == null ? "" : request.getParameter("indentSiteId").toString();
			//	vendorId=request.getParameter("vendorId") == null ? "" : request.getParameter("vendorId").toString();
			}
			else{
				// when the user click on link encrypted data send and it will decrypted in below method
				AESDecrypt decrypt=new AESDecrypt();
				String strData = request.getParameter("data");

				strData = strData.replace(" ","+");
				String strDecrypt =decrypt.decrypt("AMARAVADHIS12345",strData);

				String strPoNumber[] = null;
				String strSiteId[] = null;
				String strVendorId[]=null;
				String strSiteName[]=null;
				
				//	String indentCreationId[] = null;
				//	String strVendorData[] = null;
				if(strDecrypt.contains("&")){
					String data[] = strDecrypt.split("&");

					poNumber = data[0];
					siteId=data[1];
					/*siteName=data[2];
					if(data.length > 3){
						vendorId=data[3];
					}*/
					strPoNumber = poNumber.split("=");
					strSiteId=siteId.split("=");

					strSiteName=siteName.split("=");
					poNumber=strPoNumber[1];
					siteId=strSiteId[1];

					/*if(vendorId != null && !vendorId.equals("")){
						strVendorId=vendorId.split("=");
						vendorId=strVendorId[1];
						//	siteName=strSiteName[1];
					}*/
				}

			}
				String checkRevisedOrNot=objPurchaseDeptIndentrocess.checkRevisedOrNot(poNumber);
				if(checkRevisedOrNot.contains("@@@")){ // check if user open normal po after revised that po it will check condition and show below the message 
					String data[] = checkRevisedOrNot.split("@@@");
					String status=data[0];
					String revised=data[1];
					if(status.equalsIgnoreCase("UPDATE")){
						model.addAttribute("message","Updated Po Number :"+revised);
					}else{
					model.addAttribute("message","Revised Po Number :"+revised);
					}
					viewToBeSelected="response";
				}
				else if(checkRevisedOrNot.contentEquals("CNL")){ // check this condition when the user click on the after cancelled po it will check and execute
					List<String> list=objPurchaseDeptIndentrocess.getCancelPoData(poNumber,siteId);
					model.addAttribute("message","Purchase Order from Sumadhura Infracon Pvt Lmtd with reference number " +poNumber+ " Dated :"+list.get(0)
							+" of our Project "+list.get(1)+" had been cancelled.\n REMARKS :"+list.get(2));
					viewToBeSelected="response";
					
				}
				else if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber) && !checkRevisedOrNot.contentEquals("CANCEL_PO")){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=ics.getPoDetailsList(poNumber, siteId,request);

				String strFinacialYear =request.getAttribute("strFinacialYear").toString();
				// which was show in success page so get the data from vendor details table below code
				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				request.setAttribute("PoLevelComments","");// this is for vendor not show child product comments (created and approve emploees given)
				

				request.setAttribute("poPage","false");
				model.addAttribute("versionNo",versionNo);
				//	DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
				//System.out.println("the current year "+newDate.format(cal));
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				
	/********************************************************this is for pdf file download purpose start********************************************/			
				model.addAttribute("poNumber",poNumber);
				String filepath=rootFilePath+"PO//";
				loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
				System.out.println("poNumber "+poNumber);
				/********************************************************this is for pdf file download purpose end********************************************/			

				if(response.equalsIgnoreCase("Success")) {

					viewToBeSelected = "POPrintPage";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;


		/*

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		String strNewSiteId = "";
		request.setAttribute("showPODetailsToVendor", "yes");


		try{

			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber").toString();
			siteId = objPurchaseDeptIndentrocess.getSiteIdByPONumber(poNumber);

			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){

				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=objPurchaseDeptIndentrocess.getDetailsforPoApproval(poNumber, siteId,request);
				String strFinacialYear =request.getAttribute("strFinacialYear").toString();
				versionNo = validateParams.getProperty("PO_"+strFinacialYear+"_versionNo");
				refferenceNo = validateParams.getProperty("PO_"+strFinacialYear+"_Refference");
				strPoPrintRefdate = validateParams.getProperty("PO_"+strFinacialYear+"_issuedate");
				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				System.out.println("poNumber "+poNumber);

				if(response.equalsIgnoreCase("Success")) {

					viewToBeSelected = "PoApproval";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
		 */}


	@RequestMapping(value = "/SavePoApproveDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public synchronized String SavePoApproveDetails(Model model, HttpServletRequest request,HttpSession session) {
		List<IndentCreationBean> indentgetData = null;
		String sessionSite_id ="";
		String user_id = "";
		String response = "";
		String ponumber="";
		String siteId="";
		String strMessage = "";
		String fromDate="";
		String toDate="";	
		String viewToSelected="";
		String tempPoNumber="";
		String checkActiveOrNot="";
		String pendingEmpId="";
		boolean ismailOk=true;
		
		try {
			ponumber=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String checkMail=request.getParameter("mail")==null ? "" : request.getParameter("mail");
			if(user_id.equals("")){
				user_id=request.getParameter("userId");}
			if(ponumber!=null && !ponumber.equals("")){
			checkActiveOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
			pendingEmpId=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(ponumber,true);
			}if(!checkActiveOrNot.equals("A") || !user_id.equals(pendingEmpId)){
				model.addAttribute("response1","Already Approved or Modified or Cancelled");
				return "response";
			}
			if(!(checkMail.equalsIgnoreCase("true")) || ponumber==null || ponumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			siteId=request.getParameter("siteId");
			ProductDetails objProductDetails = new ProductDetails();
			String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
			//String old_Po_Number=request.getParameter("oldPoNumber") == null ? "#" : request.getParameter("oldPoNumber");
			fromDate=request.getParameter("fromdate");
			toDate=request.getParameter("toDate");
			response=objPurchaseDeptIndentrocess.getAndCheckApprovalPOBOQQuantity(session,request);
			//if(!response.contains("BOQ")){
			session = request.getSession(true);

			sessionSite_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){
				
				user_id=request.getParameter("userId");
				
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);
				if(passwdForMail.equals(dbPasswd)){
					
					model.addAttribute("CreatePOModelForm", objProductDetails);
					request.setAttribute("sessionSite_id",sessionSite_id);
					response=objPurchaseDeptIndentrocess.SavePoApproveDetails(ponumber,siteId,user_id,request,"false");
					// permPoNumber=request.getAttribute("permentPoNumber").toString();
					model.addAttribute("url","ViewPoPendingforApproval.spring");
					strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
					if(response=="success"){

						model.addAttribute("response",strMessage);
						viewToSelected="response";
					}
					else{
						model.addAttribute("response1",strMessage);

					}

					
				}else{
					model.addAttribute("response1","Already Approved or Rejected or Cancelled");
					viewToSelected="response";
				}
				
			}else{
			if(user_id.equals("") && !response.contains("BOQ")){
			user_id=request.getParameter("userId");}
			model.addAttribute("CreatePOModelForm", objProductDetails);
			request.setAttribute("sessionSite_id",sessionSite_id);
			response=objPurchaseDeptIndentrocess.SavePoApproveDetails(ponumber,siteId,user_id,request,"false");
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
								//model.setViewName("PendingPoApproval");
			
			if(response=="success"){

				request.setAttribute("message",strMessage);
				viewToSelected="PendingPoApproval";

			}
			else{
				model.addAttribute("response1",strMessage);
				viewToSelected="response";

			}


		}
		}	
			catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"save PO Approval ",response,String.valueOf(sessionSite_id));
		return viewToSelected;

	}


	@RequestMapping(value = "/RejectPoDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public synchronized String RejectPoDetails(Model model, HttpServletRequest request,HttpSession session) {
		String user_id ="";
		String response = "";
		String site_id ="";
		String passwd="";//this is for mail passwd
		String ponumber=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
		String fromDate="";
		String toDate="";
		List<IndentCreationBean> indentgetData = null;
		String strResponse="";
		boolean isImsOrNot=false;
		String checkval="";
		try {
			if(ponumber!=null && !ponumber.equals("")){
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
			fromDate=request.getParameter("fromdate");
			toDate=request.getParameter("toDate");
			 checkval=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals("") && checkval.equals("A"))){ // from mail Reject user get empty or null so we check here
				
				user_id=request.getParameter("userId");
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber); // get password and check request object
								if(passwdForMail.equals(dbPasswd)){
									boolean empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber"));
									if(empSameOrNot){
									response=objPurchaseDeptIndentrocess.RejectPoDetails(session,request);	}
									strResponse="response";
								}else{
									response="failed";
									strResponse="response";
								}
			}else{ 	//  login and rejecting from IMS tool
				if(user_id.equals("")){
					model.addAttribute("message1","Oops !!! There was a improper request found.");
					return "response";
				}
			boolean empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,request.getParameter("strPONumber")==null ? "" :request.getParameter("strPONumber"));
			if(empSameOrNot){
			response=objPurchaseDeptIndentrocess.RejectPoDetails(session,request);}
			// if the user rejected po then success screen back to pending po approval page so use below condition
			if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || fromDate.equals("") && toDate.equals("")){//tempPoNumber=ponumber;
				fromDate="";toDate="";
				indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,ponumber,"All");
				}else{
				indentgetData = objPurchaseDeptIndentrocess.ViewPoPendingforApproval(fromDate, toDate, user_id,ponumber,""); }//}
				request.setAttribute("indentgetData",indentgetData);
				request.setAttribute("PendingPoDetails",indentgetData);
				request.setAttribute("fromDate",fromDate);
				request.setAttribute("toDate", toDate);
				if(indentgetData.size()>0){
				request.setAttribute("showGrid", "true");}
				request.setAttribute("tempPoNumber",ponumber);
				model.addAttribute("message","Temp PO Cancelled Successfully");
				model.addAttribute("url","ViewPoPendingforApproval.spring");
				strResponse="PendingPoApproval";
				isImsOrNot=true;
			
			}
			if(response=="Success"){ 

				model.addAttribute("response","Temp PO Rejected Successfully");

			}
			else{
				model.addAttribute("response1"," Already Approved or Rejected or Cancelled ");
				if(isImsOrNot){model.addAttribute("message1","Already Approved or Rejected or Cancelled");
				model.addAttribute("message","");
				}
			}

			}else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
		} catch (Exception e) {

			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"PO rejected",response,String.valueOf(site_id));
		return strResponse;

	}

	@RequestMapping(value = "/getQuatationDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String getQuatationDetails(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String vendorName="";
		String indentnumber="";
		String strResponse="";
		String retValue="";
		String url="";
		try {

			vendorName=request.getParameter("vendorName1");
			indentnumber=request.getParameter("indentNumber");
			String poNumber=request.getParameter("poNumber");
			String siteId=request.getParameter("siteId");
			String vendorId=request.getParameter("vendorId1");
			url=request.getParameter("url")==null ? "" : request.getParameter("url");
			//ProductDetails objProductDetails = new ProductDetails();
			//List<ProductDetails> IndentDetails = null;
			if(indentnumber==null || indentnumber.equals("") || vendorName==null || vendorName.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			boolean  value=objPurchaseDeptIndentrocess.getQuatationDetailsAndCheck(indentnumber,vendorId,siteId);
			if(!value){
				model.addAttribute("message1","sorry !Quotation not available.");
				model.addAttribute("urlForActivateSubModule",url);
				return "Comparison_response";
			}
			model.addAttribute("productsMap", dcFormService.loadProds(siteId));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			
			model.addAttribute("listIssueToOtherSiteInwardLists",objPurchaseDeptIndentrocess.getQuatationDetails(request));
			model.addAttribute("listOfGetProductDetails",objPurchaseDeptIndentrocess.getProductDetailsListsForQuatation(indentnumber, vendorName,request));	
			strResponse=request.getAttribute("falg")==null ? "" :request.getAttribute("falg").toString();
		//	System.out.println(strResponse);
			if(poNumber!=null && !poNumber.equals("")){
				
				if(strResponse.equalsIgnoreCase("false")){
					retValue="ViewQuatation";
					}	else{
						
						model.addAttribute("message1","sorry !Quotation not available.");
						model.addAttribute("urlForActivateSubModule",url);
						retValue="Comparison_response";
						
					}
				
			}
			
			else{
				if(strResponse.equalsIgnoreCase("false")){
					retValue="ViewQuatation";
					}	else{
						
						model.addAttribute("message1","sorry !Quotation not available.");
						model.addAttribute("urlForActivateSubModule",url);
						retValue="Comparison_response";
						
					}
				}
			
			
			//	List<String> listOfTermsAndConditions =(List<String>)request.getAttribute("listOfTermsAndConditions");
			//	System.out.println("the value of list"+listOfTermsAndConditions);
			//	model.addAttribute("listOfTermsAndConditions",listOfTermsAndConditions);



		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"View Quatation Details","Success",String.valueOf(site_id));
		return retValue;
	}

	@RequestMapping(value = "/closeIndent.spring", method = RequestMethod.POST)
	public String closeIndent(Model model, HttpServletRequest request,HttpSession session) {

		List<IndentCreationBean> listofCentralIndents = null;
		int site_id = 0;
		String user_id = "";
		String responseMessage = "";
		userDetails  objuserDetails = new userDetails();
		String[] ccTo = null;
		String strResponse="";
		try {
			session = request.getSession(true);
			//	String typeOfPurchase = request.getParameter("typeOfPurchase");

			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteId= request.getParameter("siteId");
			responseMessage=objPurchaseDeptIndentrocess.closeIndent(model, request, site_id, user_id,siteId);
			if(responseMessage.contains("successfully")){					 

				EmailFunction objEmailFunction = new EmailFunction();



				objuserDetails = objUtilDao.getUserDetails(user_id);
				objuserDetails.setStrMesage(responseMessage);

				ccTo =   objUtilDao.getEmployeesEmailId(siteId);

				objEmailFunction.sendMailToClosedIndents(  ccTo, objuserDetails );

				model.addAttribute("response",responseMessage);
				strResponse="success";
			}
			else{
				model.addAttribute("response1",responseMessage);
				strResponse="failed";
			}


			listofCentralIndents = objPurchaseDeptIndentrocess.getAllPurchaseIndents();
			model.addAttribute("listofCentralIndents",listofCentralIndents);
			request.setAttribute("totalProductsList",objPurchaseDeptIndentrocess.getAllProducts());


		} catch (Exception e) {
			strResponse="failed";
			e.printStackTrace();
			
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Closing Indent",strResponse,String.valueOf(site_id));
		return "PendingIndents";
	}

	@RequestMapping(value ="/ViewTempPoPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ViewTempPo(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		String tempPONumber="";
		ModelAndView model = null;
		String user_id="";
		List<IndentCreationBean> indentgetData = null;
		String purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();	
		String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
		String typePO="PURCHASE_PO";
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempPONumber = request.getParameter("tempPoNumber");
			request.setAttribute("isAllPO",false);
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(tempPONumber)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//	System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					if(site_id.equals(marketing_Dept_Id)){typePO="MARKETING_PO";}
					indentgetData = objPurchaseDeptIndentrocess.ViewSiteTempPo(fromDate, toDate,tempPONumber,site_id);
					if(indentgetData != null && indentgetData.size() >0){
						request.setAttribute("showGrid", "true");
						request.setAttribute("AllPOs",typePO);
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failure";

					}
					model.addObject("indentgetData",indentgetData);
					request.setAttribute("PendingPoDetails",indentgetData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("tempPONumber", tempPONumber);
					
					model.setViewName("ViewTempPoPage_Dates");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or Temp po Number!");
				model.addObject("indentgetData",indentgetData);
				request.setAttribute("PendingPoDetails",indentgetData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				//model.addObject("AllPOs",typePO);
				model.addObject("tempPONumber", tempPONumber);
				response="success";
				model.setViewName("ViewTempPoPage_Dates");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 

		//	SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		//	String user_id=String.valueOf(session.getAttribute("UserId"));
		//	String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		//	audit.auditLog("0",user_id,"Get Grn Details View",response,site_id1);

		return model;
	}

	/*==========================================================view All Temp Pos start=================================================================*/
	@RequestMapping(value ="/ViewAllTempPoPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ViewAllTempPoPage(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		String tempPONumber="";
		ModelAndView model = null;
		String user_id="";
		List<IndentCreationBean> indentgetData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempPONumber = request.getParameter("tempPoNumber");
			request.setAttribute("isAllPO",true);
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(tempPONumber)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//	System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentgetData = objPurchaseDeptIndentrocess.ViewTempPo(fromDate, toDate,tempPONumber);
					if(indentgetData != null && indentgetData.size() >0){
						request.setAttribute("showGrid", "true");
						request.setAttribute("AllPOs","All");
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failure";

					}
					model.addObject("indentgetData",indentgetData);
					request.setAttribute("PendingPoDetails",indentgetData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("tempPONumber", tempPONumber);
					model.setViewName("ViewTempPoPage_Dates");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or Temp po Number!");
				model.addObject("indentgetData",indentgetData);
				request.setAttribute("PendingPoDetails",indentgetData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				//model.addObject("AllPOs","All");
				model.addObject("tempPONumber", tempPONumber);
				response="success";
				model.setViewName("ViewTempPoPage_Dates");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 

		
		return model;
	}

	@RequestMapping(value = "/ViewTempPoPageShow.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String ViewTempPoPageShow(HttpServletRequest request, HttpSession session,Model model){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		Calendar cal = Calendar.getInstance();
		String site_Id="";
		int strCount=0;
		int portNumber=request.getLocalPort();
		String rootFilePath ="";
		try{
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			site_Id=request.getParameter("siteId");
			poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber)){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				//	String response=ics.getTempPoDetailsList(poNumber, site_Id,request);
				String response=objPurchaseDeptIndentrocess.getDetailsforPoApproval(poNumber, site_Id,request);
				String strFinacialYear =request.getAttribute("strFinacialYear").toString();
				versionNo = validateParams.getProperty("PO_"+strFinacialYear+"_versionNo");
				refferenceNo = validateParams.getProperty("PO_"+strFinacialYear+"_Refference");
				strPoPrintRefdate = validateParams.getProperty("PO_"+strFinacialYear+"_issuedate");
				model.addAttribute("site_idForPriceMaster", site_Id);
/*	******************************************file download**********************************************************************/
				
				//String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString(); 
				String filepath=rootFilePath+"TEMP_PO//";
				loadPoImgAndPdfFiles(filepath,poNumber,"TEMP_PO",model,request); // call the getting pdf and images for showing purpose
				//String strPO_Number=poNumber.replace('/','$');
				
		/*************************************************file download end************************************************************************/		

				model.addAttribute("versionNo",versionNo);
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				model.addAttribute("response",false);

				System.out.println("poNumber "+poNumber);

				if(response.equalsIgnoreCase("Success")) {

					viewToBeSelected = "ViewTempPo";
				}

			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}

	// to show list of active po's for revising purpose
	@RequestMapping(value = "/RevisedPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String RevisedPO(HttpServletRequest request, HttpSession session,Model model){

		String  site_id ="";
		String user_id = "";
		String result="";
		Map<String, String> siteDetails = new HashMap<String,String>();
		List<Map<String, Object>> listOfPOs=null;
		String poNumber="";
		String str_SiteId="";
		String purchase_Dept_Id="";
		String marketing_dept_Id="";
		try {
			//site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			marketing_dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
			
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			site_id=request.getParameter("site")== null ? "" : request.getParameter("site").toString();
			str_SiteId = (session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			poNumber=request.getParameter("PONumber")== null ? "" : request.getParameter("PONumber").toString();
			
			if(str_SiteId.equals(marketing_dept_Id)){
				request.setAttribute("isMarketingRevised","false");
				siteDetails.put(marketing_dept_Id,"MARKETING");
				request.setAttribute("siteDetails",siteDetails);
			}else{
				
				siteDetails = new IndentSummaryDao().getSiteDetails();
				request.setAttribute("siteDetails",siteDetails);
			}
			request.setAttribute("isPoUpdated","false");
			model.addAttribute("POType","View PO's to revise");
			
			if(StringUtils.isNotBlank(String.valueOf(site_id))&& (purchase_Dept_Id.equals(str_SiteId) || marketing_dept_Id.equals(str_SiteId)) || (StringUtils.isNotBlank(poNumber) && (purchase_Dept_Id.equals(str_SiteId) || marketing_dept_Id.equals(str_SiteId)))){
				
				 listOfPOs=objPurchaseDeptIndentrocess.getListOfActivePOs(site_id,poNumber);
					if(listOfPOs.size()>0){
						model.addAttribute("listOfPOs",listOfPOs);
						
						result="ShowPOListToRevised";
					}else{
						
						model.addAttribute("displayErrMsg","Data not available in selected site.");
						result="RevisedOrUpdatePOPages";
					}
					
			}else if(!purchase_Dept_Id.equals(str_SiteId) && (!marketing_dept_Id.equals(str_SiteId)) || StringUtils.isNotBlank(poNumber)){
			
				listOfPOs=objPurchaseDeptIndentrocess.getListOfActivePOs(str_SiteId,poNumber);
				if(listOfPOs.size()>0){
					model.addAttribute("listOfPOs",listOfPOs);
					result="ShowPOListToRevised";
			
			}else{
				
				model.addAttribute("displayErrMsg","PO not avaliable in selected site.");
				result="RevisedOrUpdatePOPages";
			}
			}
			
			else{
				
				result="RevisedOrUpdatePOPages";
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Revised PO","Success",String.valueOf(site_id));
		return result;
	}

	// user click on poNumber at the time of revised po list shown 
	@RequestMapping(value = "/showPODetailsToRevised.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String showPODetailsToRevised(HttpServletRequest request, HttpSession session,Model model){

		int site_id = 0;
		String user_id = "";
		int strCount=0;
		String KILO_BYTE=""; // used to take the length of pdf below 1 mb so take this
		String rootFilePath =""; 
		int portNumber=request.getLocalPort();
		String poSiteId="";
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
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
			List<ProductDetails> IndentDetails = null;
			String poNumber = request.getParameter("poNumber");
			model.addAttribute("poNumber",poNumber);
			request.setAttribute("poNumber",poNumber); // this is use in below tables
			model.addAttribute("POType","View PO's to Revise");
			String reqSiteId = request.getParameter("reqSiteId");
			model.addAttribute("url","RevisedPO.spring");
			List<ProductDetails> poDetails = irs.getPODetails(poNumber,reqSiteId );
			String strPoEntryId =String.valueOf(poDetails.get(0).getPoEntryId());
			String subject=poDetails.get(0).getSubject();
			poSiteId=poDetails.get(0).getSite_Id();
			model.addAttribute("Allsites",allSites);
			boolean isThisPOGoingToBeCanceled = objPurchaseDeptIndentrocess.isThisPOGoingToBeCanceled(strPoEntryId);
			if(isThisPOGoingToBeCanceled){
				model.addAttribute("response1"," This PO is Going To Be Canceled in Approvals. ");
				return "response";
			}
			model.addAttribute("strSubject",poDetails.get(0).getSubject());
			model.addAttribute("poDetails",poDetails);
			model.addAttribute("listOfGetProductDetails",irs.getProductDetailsLists(poNumber,reqSiteId));
			model.addAttribute("listOfTransChrgsDtls",irs.getTransChrgsDtls(poNumber,reqSiteId));
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
			loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
	/******************************************this is for pdf download end******************************************************************/	
		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Showing Revised PO Details for Update","Success",String.valueOf(site_id));
		return "ShowPODetailsToRevised";
	}

	// user click on revised po it will executed
	@RequestMapping(value = "/editAndSaveRevisedPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String editAndSavePO(HttpServletRequest request, HttpSession session,Model model,
			@RequestParam(value="file",required = false) MultipartFile[] files){
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate="";
		String po_Number="";
		//String filePath="";
		int strCount=0;
		int site_id =0;
		String user_id ="";
		String response ="";
		String oldPoNumber="";
		String tempOrNot="";
		int  noOfPdfs=0;
		String imgNumber="";
		String moveFilePath="";
		String poLevelComments="";
		
		String rootFilePath =""; 
		String strFilePath="";
		int getLocalPort = request.getLocalPort();
		double finlaAmt=0.0;
		double oldAmount=0.0;
		
		String indentNumber="";
		String indentNo="";
		String old_Po_Number="";
		String strfinalAmount="";
		String finalAmt="";
		
		if(getLocalPort == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
		moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();
		rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();}
		try{
			indentNumber = request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber");
			oldPoNumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString(); // old normal PoNumber taken so it will take it
			strfinalAmount=request.getParameter("ttlAmntForIncentEntry");
			indentNo=session.getAttribute("strIndentNo")== null ? "" : session.getAttribute("strIndentNo").toString();
			old_Po_Number=session.getAttribute("PoNumber")== null ? "" : session.getAttribute("PoNumber").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			if(indentNumber==null || indentNumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
		if(indentNumber.equals(indentNo) && oldPoNumber.equals(old_Po_Number) && strfinalAmount.equals(finalAmt)){
			model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
			return "response";
		}else{
		String activeOrNot=objPurchaseDepartmentIndentProcessDao.checkRevisedOrNot(oldPoNumber);
		if(activeOrNot.equals("A")){
		 site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		 user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		 poLevelComments=request.getParameter("note") == null ? "" : request.getParameter("note").toString(); // user any change in product given comments at the time of po creation
		 finlaAmt=Double.valueOf(request.getParameter("ttlAmntForIncentEntry")==null ? "0" : request.getParameter("ttlAmntForIncentEntry").toString());
		 oldAmount=Double.valueOf(request.getParameter("POTotal")==null ? "0" : request.getParameter("POTotal").toString());
		 
		int count=files.length;
		 if(count==1){
			 boolean strStatus=objPurchaseDeptIndentrocess.checkIsUpdateOrNot(session,request,true,false);
				if(strStatus){
					model.addAttribute("message1","Sorry,Unable to Revise PO.");
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
		response=getAndCheckPOBOQQuantity(request,session,false,false);
		if(!response.contains("BOQ")){
		 response = objPurchaseDeptIndentrocess.SavePoDetails(model, request, session,strFinacialYear);}
		// this is for pdf purpose start
		if(!response.contains("BOQ")){
		String poNumber=request.getAttribute("strPONumber").toString();
		model.addAttribute("poNumber",poNumber); // taken poumber for attachments purpose
		
		//int count=files.length;
		if(response.equalsIgnoreCase("CreatePOFinalPage")){
			strFilePath=rootFilePath+"TEMP_PO//";
			po_Number=poNumber;
			tempOrNot="TEMP_PO";
			
			
		}else{
			strFilePath=rootFilePath+"PO//";
			po_Number=poNumber.replace('/','$');
			tempOrNot="PO";
		}
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
		loadPoImgAndPdfFiles(strFilePath,poNumber,tempOrNot,model,request); // call the getting pdf and images for showing purpose
		}
		session.setAttribute("strIndentNo",indentNumber);
		session.setAttribute("PoNumber",oldPoNumber);
		session.setAttribute("strfinalAmount",strfinalAmount);
		}// if condition end
		else{
			model.addAttribute("message1","We found a Malfunction, Please once logout and login for further operations.");
			return "response";		}
		}
		
		//}// if condition end
		
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails.auditLog("0",user_id,"Showing Revised PO Details",response,String.valueOf(site_id));
		return response;
	}
	
	@RequestMapping(value = "/printIndentForPurchase.spring", method = RequestMethod.POST)
	public String printIndentForPurchase(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objPurchaseDeptIndentrocess.printIndentForPurchase(model, request, site_id, user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Printing Indent","Success",String.valueOf(site_id));
		return "PrintIndent";
	}
	// from ims tool cancel po 
	@RequestMapping(value = "/cancelPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String viewOrCancelPO(Model model, HttpServletRequest request,HttpSession session) {
		String  site_id = "";
		String user_id = "";
		String value="";
		String strResponse="";
		String temp_Po_Number="";
		String fromDate="";
		String toDate="";
		List<IndentCreationBean> indentgetData = null;
		
		String mailComments=request.getParameter("Remarks_cancel")==null ? "" : request.getParameter("Remarks_cancel");
		try {
			
			final int getLocalPort = request.getLocalPort();
			temp_Po_Number=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
			if(temp_Po_Number!=null && !temp_Po_Number.equals("")){
			session = request.getSession(true);
			site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			fromDate=request.getParameter("fromdate")== null ? "" : request.getParameter("fromdate").toString(); // from and to dates used to back to previous screen i.e pending po for approval 
			toDate=request.getParameter("toDate")== null ? "" : request.getParameter("toDate").toString();
			String passwdForMail=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			//  canceling from mail. for mail approvals/rejecting we are genarating temppassword
			if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals(""))){// from mail cancel po this one take and check condition
				
				user_id=request.getParameter("userId");
				String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(temp_Po_Number);// getting password from database then check with request.getParameter password
								if(passwdForMail.equals(dbPasswd)){
									value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);	
									strResponse="response";
								}else{
									strResponse="failed";
								}
			}else{
				//  login  from IMS tool and cancel the PO.
				value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);
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
				
			}
		
			
			
			if(value.equalsIgnoreCase("success")){
				
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id);
				
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(temp_Po_Number,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Your Temp Po Has been cancelled";
				objPurchaseDeptIndentrocess.sendTempPoMailCommonData(temp_Po_Number,mailComments,listOfDetails,subject,"cancelled",getLocalPort);
				
				model.addAttribute("message","Temp PO Cancelled Successfully");
			}
				else{
					
					model.addAttribute("message1","Already Approved or Rejected or Cancelled");
					strResponse="response";
				}
			
			
		}else{
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"ViewOrCancelPo","Success",site_id);
		return strResponse;
	}
	
	@RequestMapping("/getCanceledPoList.spring")
	public String  viewCancelPoList(HttpServletRequest request,HttpSession session,Model model) {

		List<ProductDetails> list = new ArrayList<ProductDetails>();
		String user_id="";
		String site_id="";
		String retValue="";
		String strSiteId="";
		try {
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
			strSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			list=objPurchaseDeptIndentrocess.getListOfCancelPo(user_id,strSiteId);
			
			if(list.size()>0){
				
				model.addAttribute("listOfPOs",list);
				retValue="ViewCancelPo";
				
			}else{
				
				model.addAttribute("response1","Pending Temp Po's Not Available");
				retValue="response";
				
			}
			
			
		
		
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"ViewOrCancelPoList","Success",site_id);
		
		return retValue;
		
	}
	@RequestMapping(value = "/getCanceledPo.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String ViewOrCancelPo(HttpServletRequest request, HttpSession session,Model model){

		int site_id = 0;
		String user_id = "";
		int strCount=0;
		String KILO_BYTE="";
		int portNumber=request.getLocalPort();
		String rootFilePath ="";
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}

			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			String poNumber = request.getParameter("poNumber");
			String oldPONumber=request.getParameter("oldPONumber"); // use in revised time old PoNumber taken it
			model.addAttribute("poNumber",poNumber);
			String reqSiteId = request.getParameter("reqSiteId");
			String indentNumber = request.getParameter("indentNumber");
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			
			model.addAttribute("tempPoProd",dcFormService.TempPoloadProds(indentNumber,reqSiteId));  // which product arecancelled then it will take that so wrirte this
			
			model.addAttribute("productsMap", dcFormService.loadProds(String.valueOf(site_id)));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			//ics.createPO(model, request);
			List<ProductDetails> IndentDetails = null;
			
		//	model.addAttribute("productsMapForCancel", dcFormService.loadProdsByPONumber(poNumber,reqSiteId));
			model.addAttribute("poDetails",objPurchaseDeptIndentrocess.getViewCancelPoDetails(poNumber,reqSiteId ));
			model.addAttribute("listOfGetProductDetails",objPurchaseDeptIndentrocess.getProductDetailsListsForCancelPo(poNumber,reqSiteId));
			model.addAttribute("listOfTransChrgsDtls",objPurchaseDeptIndentrocess.getTransChrgsDtlsForCancelPo(poNumber,reqSiteId));
			String strPurpose = objPurchaseDeptIndentrocess.getCancelPoComments(poNumber);
			model.addAttribute("IndentLevelCommentsList",strPurpose);
			
			List<ProductDetails> listTermsAndCondition  = objPurchaseDeptIndentrocess.getTempTermsAndConditions(poNumber,"false",reqSiteId);
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());
			//Conclusions author - Rafi
			List<String> conclusions = objPurchaseDeptIndentrocess.getTempPoConclusions(poNumber);
			model.addAttribute("conclusions", conclusions);
			//Conclusions - End
			String ccEmails = objPurchaseDeptIndentrocess.getTempPoCCEmails(poNumber);
			String subject = objPurchaseDeptIndentrocess.getTempPOSubject(poNumber);
			model.addAttribute("ccEmails",ccEmails);
			model.addAttribute("subject",subject);
			model.addAttribute("Allsites",allSites);
			KILO_BYTE=validateParams.getProperty("KILO_BYTE") == null ? "" : validateParams.getProperty("KILO_BYTE").toString();
			model.addAttribute("KILO_BYTE",KILO_BYTE);
		/******************************************this is for pdf download start******************************************************************/	
			// show the pdf already attached to this 
			String filepath=rootFilePath+"TEMP_PO//";
			loadPoImgAndPdfFiles(filepath,poNumber,"TEMP_PO",model,request); // call the getting pdf and images for showing purpose
			
	
	/******************************************this is for pdf download end******************************************************************/	
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	SaveAuditLogDetails.auditLog("0",user_id,"Showing PO cancle details ","Success",String.valueOf(site_id));
		return "ViewCancelTempPo";
	}
	
	@RequestMapping(value = "/updateTempPoPage.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String updateTempPoPage(HttpServletRequest request, HttpSession session,Model model,
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
			boolean status=objPurchaseDeptIndentrocess.checkIsUpdateOrNot(session,request,true,true);
			if(status){
				model.addAttribute("message1","Unable to modify PO.");
				return "response";
			}
		}
		
		response=getAndCheckPOBOQQuantity(request,session,true,true);
		if(!response.contains("BOQ")){
		response = objPurchaseDeptIndentrocess.updateTempPoPage(model, request, session);
		boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);}
		if(!response.contains("BOQ")){
		if(response.equalsIgnoreCase("Success")){
			// normal po approve methode executed ,below true is use to check the cancel po condition
			strResponse=objPurchaseDeptIndentrocess.SavePoApproveDetails(poNumber,strSiteId,user_id,request,"true"); 
			
			 if(strResponse.equalsIgnoreCase("Success")){

					model.addAttribute("response",request.getAttribute("result") == null ? "" : request.getAttribute("result").toString());

				}
				else{
					model.addAttribute("response1","Already Approved or Rejected or Cancelled successfully ");

				}
		/******************************************************this is for pdf download*************************************************************/
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
		
		/*********************************************************this is for pdf download end*******************************************************/ 
		}
		
		
		else{
			model.addAttribute("response1"," not successfully updated ");

		}
		}else{ // this is for checking the quantity and getting the result more than boq
			model.addAttribute("response1",response);
		}
		
		SaveAuditLogDetails.auditLog("0",user_id,"updateTempPoPage ","Success",String.valueOf(site_id));
	return "response";
	}
	 // get cancel temp po pages then add new product it will take from indent tables
	@RequestMapping(value = "/tempPoSubProducts.spring", method = RequestMethod.POST)
	@ResponseBody
	public String tempPoSubProducts(@RequestParam("mainProductId") String mainProductId,
			@RequestParam("indentNumber") String indentNumber,@RequestParam("reqSiteId") String reqSiteId) {
		
		System.out.println("indent number"+indentNumber);
		
		return objPurchaseDeptIndentrocess.tempPoSubProducts(mainProductId,indentNumber,reqSiteId);
	}
	 // get cancel temp po pages then add new product it will take from indent tables
	@RequestMapping(value = "/tempPoChildProducts.spring", method = RequestMethod.POST)
	@ResponseBody
	public String tempPoChildProducts(@RequestParam("subProductId") String subProductId,
			@RequestParam("indentNumber") String indentNumber,@RequestParam("reqSiteId") String reqSiteId) {
		
		return objPurchaseDeptIndentrocess.tempPoChildProducts(subProductId,indentNumber,reqSiteId);
	}
	 // get cancel temp po pages then add new product it will take from indent tables
	@RequestMapping(value = "/listTempPoUnitsOfChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String listUnitsOfSubProducts(@RequestParam("prodId") String productId)
			 {
		String measurement="";
		
	
		measurement=irs.loadIndentReceiveMeasurements(productId);
		
		return measurement;
	}
	 // get cancel temp po pages then add new product it will take from indent tables
	@RequestMapping(value = "/getTempPoProductAvailability.spring", method = RequestMethod.POST)
	@ResponseBody
	public String getTempPoProductAvailability(@RequestParam("prodId") String productId,
			@RequestParam("indentNumber") String indentNumber,@RequestParam("VendorName") String VendorName,
			@RequestParam("subProductId") String subProductId,@RequestParam("childProdId") String childProdId,
			@RequestParam("measurementId") String measurementId,@RequestParam("productName") String productName,
			@RequestParam("subProductName") String subProductName,@RequestParam("childProductName") String childProductName,
			@RequestParam("measurementName") String measurementName,@RequestParam("req_Quan") String req_Quan,
			@RequestParam("init_Quan") String init_Quan,@RequestParam("indent_Creation_dtls_Id") String indent_Creation_dtls_Id,
			@RequestParam("pending_Quan") String pending_Quan,HttpServletRequest request) {
		String measurement="";
		
		request.getParameter("productId");
		
		request.setAttribute("listOfGetProductDetails",objPurchaseDeptIndentrocess.getProductDetailsLists(indentNumber,VendorName,request,"true"));
		String value=request.getAttribute("productvalues").toString();
		
		return value;
	}
	
	@RequestMapping(value = "/UpdatePO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String UpdatePO(HttpServletRequest request, HttpSession session,Model model){

		String site_id ="";
		String user_id = "";
		String result="";
		Map<String, String> siteDetails = null;
		String purchase_Dept_Id="";
		String str_SiteId="";
		List<Map<String, Object>> listOfPOs=null;
		String poNumber="";
		try {
			//site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			site_id=request.getParameter("site")== null ? "" : request.getParameter("site").toString();
			str_SiteId = (session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			poNumber=request.getParameter("PONumber")== null ? "" : request.getParameter("PONumber").toString();
			
			request.setAttribute("isPoUpdated","true");
			model.addAttribute("POType","View PO's to update");
			model.addAttribute("url","UpdatePO.spring");
			if(purchase_Dept_Id.equals(str_SiteId)){
			siteDetails = new IndentSummaryDao().getSiteDetails();
			
			}else if(!purchase_Dept_Id.equals(str_SiteId)){
				siteDetails = new IndentSummaryDao().getSingleSiteDetails(str_SiteId);	
			}
			request.setAttribute("siteDetails",siteDetails);
			if(StringUtils.isNotBlank(String.valueOf(site_id)) && purchase_Dept_Id.equals(str_SiteId) || (StringUtils.isNotBlank(poNumber) && purchase_Dept_Id.equals(str_SiteId))){
				
				listOfPOs=objPurchaseDeptIndentrocess.getListOfActivePOs(site_id,poNumber);
				
				if(listOfPOs.size()>0){
					model.addAttribute("listOfPOs",listOfPOs);
					
					result="ShowPOListToRevised";
				}else{
					
					model.addAttribute("displayErrMsg","PO not avaliable in selected site.");
					result="RevisedOrUpdatePOPages";
				}
				
				
			}else if((!purchase_Dept_Id.equals(str_SiteId) && StringUtils.isNotBlank(String.valueOf(site_id))) || StringUtils.isNotBlank(poNumber)){
			
				listOfPOs=objPurchaseDeptIndentrocess.getListOfActivePOs(str_SiteId,poNumber);
				if(listOfPOs.size()>0){
					model.addAttribute("listOfPOs",listOfPOs);
					result="ShowPOListToRevised";
			
			}else{
				
				model.addAttribute("displayErrMsg","PO not avaliable in selected site.");
				result="RevisedOrUpdatePOPages";
			}
			}
			else{
				
				result="RevisedOrUpdatePOPages";

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Update PO List","Success",String.valueOf(site_id));
		return result;
	}
	@RequestMapping(value = "/showPODetailsToUpdate.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String showPODetailsToUpdate(HttpServletRequest request, HttpSession session,Model model){

		int site_id = 0;
		String user_id = "";
		int strCount=0;
		int portNumber=request.getLocalPort();
		String rootFilePath ="";
		String increase_Quantity="";
		String decrease_Quantity="";
		String poSiteId="";
		String updatePOEmpIds="";
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();	
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			increase_Quantity=validateParams.getProperty("QUANTITY_INCREASE") == null ? "" : validateParams.getProperty("QUANTITY_INCREASE").toString();	
			decrease_Quantity=validateParams.getProperty("QUANTITY_DECREASE") == null ? "" : validateParams.getProperty("QUANTITY_DECREASE").toString();	
			updatePOEmpIds=validateParams.getProperty("UPDATE_PO_QUANTITY_EMPID") == null ? "" : validateParams.getProperty("UPDATE_PO_QUANTITY_EMPID").toString();
			
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("productsMap", dcFormService.loadProds(String.valueOf(site_id)));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			//ics.createPO(model, request);
			List<ProductDetails> IndentDetails = null;
			String poNumber = request.getParameter("poNumber");
			String poEntryId=request.getParameter("poEntryId");
			model.addAttribute("poNumber",poNumber); // show the po number on the screen
			String reqSiteId = request.getParameter("reqSiteId");
			model.addAttribute("Allsites",allSites);
			model.addAttribute("url","UpdatePO.spring");
			model.addAttribute("POType","View PO to update");
			request.setAttribute("updatePOEmpIds",updatePOEmpIds);
			request.setAttribute("currentUsereId",user_id);
			
			List<ProductDetails> poDetails = irs.getPODetails(poNumber,reqSiteId );
			String strPoEntryId =String.valueOf(poDetails.get(0).getPoEntryId());
			String subject=poDetails.get(0).getSubject();
			poSiteId=poDetails.get(0).getSite_Id();
			model.addAttribute("strSubject",subject);
			
			boolean isThisPOGoingToBeCanceled = objPurchaseDeptIndentrocess.isThisPOGoingToBeCanceled(strPoEntryId);
			if(isThisPOGoingToBeCanceled){
				model.addAttribute("response1"," This PO is Going To Be Canceled in Approvals. ");
				return "response";
			}
			model.addAttribute("poDetails",poDetails);
			model.addAttribute("listOfGetProductDetails",irs.getProductDetailsLists(poNumber,reqSiteId));
			model.addAttribute("listOfTransChrgsDtls",irs.getTransChrgsDtls(poNumber,reqSiteId));
			request.setAttribute("isPoUpdated","true"); // this is used in jsp single jsp used in revised and update po  
			// if true mention in this it will check permanent po or temp po both using same method
			List<ProductDetails> listTermsAndCondition  = objPurchaseDeptIndentrocess.getTempTermsAndConditions(poNumber,"true",reqSiteId);
			// newly added for getting the ccmails so written this
			String ccEmails =objPurchaseDepartmentIndentProcessDao.getCCmails(strPoEntryId,poSiteId,false);//objPurchaseDeptIndentrocess.getDefaultCCEmails(reqSiteId); // based on site Id get CcMails from properties files
			String ccmail=objPurchaseDepartmentIndentProcessDao.gettingEmpId(strPoEntryId);
			ccEmails=ccmail.concat(ccEmails);
			model.addAttribute("ccEmails",ccEmails);
			
			List<String> conclusions = objPurchaseDepartmentIndentProcessDao.getPoConclusions(poEntryId);
			model.addAttribute("conclusions", conclusions);
			
			model.addAttribute("increase_Quantity",increase_Quantity);
			model.addAttribute("decrease_Quantity",decrease_Quantity);
			
			System.out.println("the conditions size"+listTermsAndCondition.size());
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());
			
			/******************************************this is for pdf download start******************************************************************/	
			//model.addAttribute("poNumber",poNumber);
			 // show the pdf or images for the user
			String filepath=rootFilePath+"PO//";
			String type="PO";
			loadPoImgAndPdfFiles(filepath,poNumber,type,model,request); // call the getting pdf and images for showing purpose
			
	
	/******************************************this is for pdf download end******************************************************************/	
			/*List<ProductDetails> listTermsAndCondition = objPurchaseDeptIndentrocess.getTermsAndConditions(reqSiteId);
			model.addAttribute("listTermsAndCondition",listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());*/

			/*String ccEmails = objPurchaseDeptIndentrocess.getDefaultCCEmails(reqSiteId);
			model.addAttribute("ccEmails",ccEmails);*/

		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Showing updated PO Details for Update","Success",String.valueOf(site_id));
		return "ShowPODetailsToRevised";
	}

	@RequestMapping(value = "/editAndSaveUpdatePO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String editAndSaveUpdatedPO(HttpServletRequest request, HttpSession session,Model model,@RequestParam(value="file",required = false) MultipartFile[] files){
	
		String filePath="";
		int pdfCount=0;
		int imgCount=0;
		String po_Number="";
		String type="";
		String rootFilePath="";
		int portNumber=request.getLocalPort();
		String serverFile="";
		String strResponse="";
		String response ="";
		String oldPoNumber="";
		String moveFilePath="";
		String indentNumber ="";
		String indentNo="";
		String old_Po_Number="";
		String strfinalAmount="";
		String finalAmt="";
		///boolean status=true;
		
		int site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		try{
			if(portNumber == 80){moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH").toString();
			rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
			moveFilePath=validateParams.getProperty("UPLOAD_MOVE_PATH_CUG") == null ? "" : validateParams.getProperty("UPLOAD_MOVE_PATH_CUG").toString();
			rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();
			}
			indentNumber = request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber");
			oldPoNumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString();
			strfinalAmount=request.getParameter("ttlAmntForIncentEntry") == null ? "" : request.getParameter("ttlAmntForIncentEntry").toString();
			indentNo=session.getAttribute("strIndentNo")== null ? "" : session.getAttribute("strIndentNo").toString();
			old_Po_Number=session.getAttribute("PoNumber")== null ? "" : session.getAttribute("PoNumber").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			
			if(indentNumber==null || indentNumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			String updatePoEmpIds=(request.getParameter("updatePoEmpIds") == null ? "" : request.getParameter("updatePoEmpIds").toString());
			String currentEmpId=request.getParameter("currentEmpId") == null ? "" : request.getParameter("currentEmpId").toString();
			if(!updatePoEmpIds.contains(currentEmpId)){
				boolean strStatus=objPurchaseDeptIndentrocess.checkConditionForIncreaseDecreaseQuantity(request);
				if(!strStatus){
					
					return "response";
				}
				
			}
		if(indentNumber.equals(indentNo) && oldPoNumber.equals(old_Po_Number) && strfinalAmount.equals(finalAmt)){
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}else{
		String activeOrNot=objPurchaseDepartmentIndentProcessDao.checkRevisedOrNot(oldPoNumber);
		if(activeOrNot.equals("A")){
		request.setAttribute("poPage","true");
		ProductDetails objProductDetails = new ProductDetails();
		//ics.createPO(model, request);
		String version_no=request.getParameter("versionNumber") == null ? "" : request.getParameter("versionNumber").toString();
		String strPoPrintRefdate=request.getParameter("printRefferenceNo") == null ? "" : DateUtil.dateConversionForPO(request.getParameter("printRefferenceNo").toString());
		String refferenceNo=request.getParameter("refferenceNo") == null ? "" : request.getParameter("refferenceNo").toString();
		
		int count=files.length;
		if(count==1){
			boolean status=objPurchaseDeptIndentrocess.checkIsUpdateOrNot(session,request,false,false);
			if(status){
				model.addAttribute("message1","Sorry,Unable to update PO");
				return "Comparison_response";
			}
		}/*else{
			model.addAttribute("message1","Sorry,Unable to update PO.");
			return "response";
		}*/
		
		
		request.setAttribute("versionNo", version_no);
		request.setAttribute("refferenceNo", refferenceNo);
		request.setAttribute("strPoPrintRefdate", strPoPrintRefdate);
		
		model.addAttribute("CreatePOModelForm", objProductDetails);
		response=getAndCheckPOBOQQuantity(request,session,false,false);
		if(!response.contains("BOQ")){
		response = objPurchaseDeptIndentrocess.editAndSaveUpdatePO(model, request, session);}

		if(!response.equalsIgnoreCase("response") && !response.contains("BOQ")){ // if any payment done on this then it will executed
		String poNumber=request.getAttribute("strPONumber").toString();
		model.addAttribute("poNumber",poNumber);
		boolean status=GetInvoiceDetailsController.deleteInvoicePdfandImage(request);
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
		int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
		 imgCount=imagesAlreadyPresent;
		 pdfCount=pdfAlreadyPresent;
		if(response.equalsIgnoreCase("CreatePOFinalPage")){ // temp po generate then if condition executed
			filePath=rootFilePath+"TEMP_PO//";
			po_Number=poNumber;
			type="TEMP_PO";
		}else{
			filePath=rootFilePath+"PO//";
			po_Number=poNumber.replace('/','$');
			type="PO";
		}
		String strOldPoNumber=oldPoNumber.replace('/', '$'); // for files path taken time replace / with $ 
		for (int i = 0; i < imagesAlreadyPresent; i++) {
			File img = new File(rootFilePath+"PO//"+strOldPoNumber+"_Part"+i+".jpg");
			if(img.exists()){
				Files.copy(Paths.get(moveFilePath+"\\PO\\"+strOldPoNumber+"_Part"+i+".jpg"),Paths.get(moveFilePath+"\\"+type+"\\"+po_Number+"_Part"+i+".jpg"));
				
			}
		}
		for (int i = 0; i < pdfAlreadyPresent; i++) {
			File file = new File(rootFilePath+"PO//"+strOldPoNumber+"_Part"+i+".pdf");
			if(file.exists()){
			Files.copy(Paths.get(moveFilePath+"\\PO\\"+strOldPoNumber+"_Part"+i+".pdf"),Paths.get(moveFilePath+"\\"+type+"\\"+po_Number+"_Part"+i+".pdf"));
			}
		}
		for (int i = 0; i < files.length; i++) {
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
			strResponse="Success";
		}
		loadPoImgAndPdfFiles(filePath,po_Number,type,model,request); // call the getting pdf and images for showing purpose
		}
		session.setAttribute("strIndentNo",indentNumber);
		session.setAttribute("PoNumber",oldPoNumber);
		session.setAttribute("strfinalAmount",strfinalAmount);

		}// if condition
		
		else{
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";	
		}
		}
		}catch(Exception e){
			strResponse="failed";
			e.printStackTrace();
		}
		/*if(response=="Success"){

			model.addAttribute("response","Successfully Updated ");

		}
		else{
			model.addAttribute("response1"," not successfully updated ");

		}*/
		SaveAuditLogDetails.auditLog("0",user_id,"Save updated PO Details",strResponse,String.valueOf(site_id));
		return response;
	}

	
	/************************************************************************siteLevelPo*****************************/
	
	

	@RequestMapping(value = "/createPoPage.spring", method = {RequestMethod.POST, RequestMethod.GET })
	public String createPoPage(Model model, HttpServletRequest request,HttpSession session) {

		String response = "";
		String retValue=""; // this is for return type jsp
		String dept_Id=session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		List<ProductDetails> IndentDetails = null;
		try {
			
			// int siteWiseIndentNo =
			model.addAttribute("indentReceiveModelForm",new IndentReceiveBean());
			model.addAttribute("productsMap", irs.loadProds(site_id));
			model.addAttribute("columnHeadersMap",ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", irs.getGSTSlabs());
			model.addAttribute("chargesMap", irs.getOtherCharges());
			model.addAttribute("siteName", iis.getProjectName(session));
			if(!site_id.equals(marketingDeptId)){ // to check the site Id which is marketing then it will create Marketing po Page
			model.addAttribute("productsMap", irs.loadProds(site_id));
			model.addAttribute("siteWiseIndentNo",ics.getMaxOfSiteWiseIndentNumber(Integer.parseInt(site_id)));
			model.addAttribute("indentNumber",ics.getIndentCreationSequenceNumber());
			model.addAttribute("productList", IndentDetails);
			model.addAttribute("site_id",site_id);
			model.addAttribute("Allsites",allSites);
			SaveAuditLogDetails.auditLog("0", user_id, "create PO SitelevelPage",response, String.valueOf(site_id));
			retValue="CreatePoSiteLevel";
			}else{ // which is normal po can create PO 
				//model.addAttribute("productsMap", objMarketingDepartmentIndentProcessDao.loadProds());
				Set <String> locationName =objMarketingDepartmentIndentProcessDao.getLocationDetails(); // site wise location details
				request.setAttribute("siteLocationDetails", locationName);
				
				SaveAuditLogDetails.auditLog("0", user_id, "create PO MarketlevelPage",response, String.valueOf(site_id));
				retValue="marketing/Marketing_Create_PO";
			}
			
			List<ProductDetails> listTermsAndCondition = objPurchaseDeptIndentrocess.getTermsAndConditions(site_id);
			model.addAttribute("listTermsAndCondition", listTermsAndCondition);
			model.addAttribute("TC_listSize", listTermsAndCondition.size());

			String ccEmails = objPurchaseDeptIndentrocess.getDefaultCCEmails(site_id);
			model.addAttribute("ccEmails", ccEmails);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//SaveAuditLogDetails.auditLog("0", user_id, "create PO SitelevelPage",response, String.valueOf(site_id));
		
		
		return retValue;
	}
	
	
	@RequestMapping(value = "/SaveSiteLevelPoDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String SaveSiteLevelPoDetails(Model model, HttpServletRequest request,HttpSession session,
			@RequestParam(value="file",required = false) MultipartFile[] files) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		model.addAttribute("isSiteLevelPo",true);
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		String strValue="";
		String filePath="";
		int strCount=0;
		String po_Number="";
		int portNumber=request.getLocalPort();
		String rootFilePath =""; 
		int pdfCount=0;
		int imgCount=0;
		String type="";
		String serverFile="";
		String finalAmt="";
		String poNumber="";
		int response3=0;
		int val=0;
		try {
			if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
				rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
			}
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			String siteWiseIndent=request.getParameter("siteWiseIndentNo")==null ? "" : request.getParameter("siteWiseIndentNo");
			String strIndentNo=request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber").toString();
			String strVendorId = request.getParameter("vendorId")==null ? "" : request.getParameter("vendorId");
			String strfinalAmount=request.getParameter("ttlAmntForIncentEntry")==null ? "" :request.getParameter("ttlAmntForIncentEntry");
			String indentNumber=session.getAttribute("strIndentNo")== null ? "" : session.getAttribute("strIndentNo").toString();
			String vendorId=session.getAttribute("strVendorId")== null ? "" : session.getAttribute("strVendorId").toString();
			finalAmt=session.getAttribute("strfinalAmount")== null ? "" : session.getAttribute("strfinalAmount").toString();
			
			// current financial year taken here for the use in po Number
			if(strIndentNo==null || strIndentNo.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			if(strIndentNo.equals(indentNumber) && strVendorId.equals(vendorId) && strfinalAmount.equals(finalAmt)){
				model.addAttribute("message1","Oops !!! We found a Malfunction, Please once logout and login for further operations.");
				return "response";
			}else{
			int indent_Number =Integer.parseInt(request.getParameter("indentNumber").toString());
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
			// here set the data use in service class
			request.setAttribute("versionNo", versionNo);
			request.setAttribute("refferenceNo", refferenceNo);
			request.setAttribute("strPoPrintRefdate", strPoPrintRefdate);
			// these are used to show in success page
			model.addAttribute("versionNo",versionNo);
			model.addAttribute("refferenceNo",refferenceNo);
			model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
			request.setAttribute("poPage","true"); // which is used in jsp page for comments show 
			ProductDetails objProductDetails = new ProductDetails();
			//ics.createPO(model, request);
			model.addAttribute("CreatePOModelForm", objProductDetails);
			strValue=getAndCheckPOBOQQuantity(request,session,false,false);
			if(!strValue.contains("BOQ")){
			 val=objPurchaseDeptIndentrocess.insertSiteLevelIndentData(site_id,user_id,indent_Number,siteWiseIndent);
			// used for site level po , will get from service layer
			request.setAttribute("siteWiseIndentNumber",val);
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			// save internally indent created and save 
			 response3 =  objPurchaseDeptIndentrocess.insertIndentCreationApproval(indentCreationApprovalSeqNum, indent_Number,site_id,user_id);
			}
			if(val>0 && response3>0 && !strValue.contains("BOQ")){
			strValue=objPurchaseDeptIndentrocess.SavePoDetails(model,request,session,strFinacialYear);
			 poNumber=request.getAttribute("strPONumber").toString(); // this is for get attachments  and show
			model.addAttribute("poNumber",poNumber);
			
			
			
			//int count=files.length;
			//if(count==0){request.setAttribute("uploadorNot","false");}else{request.setAttribute("uploadorNot","true");}
			/****************************************for file upload option***************************************************/
			if(strValue.equalsIgnoreCase("CreatePOFinalPage")){
				filePath=rootFilePath+"TEMP_PO//";
				po_Number=poNumber;
				type="TEMP_PO";
			}else{
				filePath=rootFilePath+"PO//";
				po_Number=poNumber.replace('/','$');
				type="PO";
			}
			
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
			loadPoImgAndPdfFiles(filePath,po_Number,type,model,request); // call the getting pdf and images for showing purpose
			/*****************************************for file upload option end****************************************************/
		}
		session.setAttribute("strIndentNo",strIndentNo);
		session.setAttribute("strVendorId",strVendorId);
		session.setAttribute("strfinalAmount",strfinalAmount);
		
		}
		}	catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		SaveAuditLogDetails.auditLog("0",user_id,"SiteLevel PO Created",response,String.valueOf(site_id));
		//System.out.println("return page name: "+strValue);
		return strValue;

	}
	/************************************************************************************sample page end******************************************************/
	
	/************************************************************************Reject for Temp Po******************************************************************/
	// this is calling from mail 
	@RequestMapping(value = "/RejectMailTempPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String RejectMailTempPO(Model model, HttpServletRequest request,HttpSession session) {
		String user_id ="";
		String response = "";
		String site_id ="";
		//String poNumber="";
		
		try {
			
			response=objPurchaseDeptIndentrocess.RejectMailTempPO(session,request);
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
	
	
	
	
	/**************************************************************************Reject Temp Po End******************************************************************/

	/***************************************************************cancel temp po page for mail start*******************************************************************/
	// this is calling from mail 
	@RequestMapping(value = "/TempPoMailcancelPO.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String TempPoMailcancelPO(Model model, HttpServletRequest request,HttpSession session) {
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
			value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);
		  	}
			//value=objPurchaseDeptIndentrocess.CancelPo(session,request,temp_Po_Number,user_id,site_id);}
			if(value.equalsIgnoreCase("success")){
				
				String pendingEmpId=objPurchaseDepartmentIndentProcessDao.getpendingEmpId(temp_Po_Number,user_id); // previous employeeId taken in this 
				
				List<String> listOfDetails=objPurchaseDepartmentIndentProcessDao.getApproveMailDetails(temp_Po_Number,pendingEmpId);
				listOfDetails.add(String.valueOf(request.getLocalPort()));
				String subject="Temp Po has been cancelled";
				objPurchaseDeptIndentrocess.sendTempPoMailCommonData(temp_Po_Number,mailComments,listOfDetails,subject,"Cancelled",getLocalPort); // mail send to previous person to know the po was cancelled 
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
	
	
	
	/****************************************************************cancel temp po page end******************************************************************************/
	
	/************************************************************for pdf file purpose****************************************************************/
	@RequestMapping(value = "/deletePoFile", method = RequestMethod.POST)
	@ResponseBody
	public String deletePoFile(Model model, HttpServletRequest request,HttpSession session,
			@RequestParam("po_Number") String poNumber,@RequestParam("imgNumber") String imgNumber) {
		
		String strOldPoNumber="";
		String filePath="";
		String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();
		//String oldPoNumber=request.getParameter("poNo") == null ? "" : request.getParameter("poNo").toString();
		String response="";
		if(poNumber.contains("/")){
			strOldPoNumber=poNumber.replace('/', '$');
			filePath=rootFilePath+"PO//"+strOldPoNumber;
		}else{
			filePath=rootFilePath+"TEMP_PO//"+poNumber;
		}
		
		try{
			
            File file = new File(filePath+"_part"+imgNumber+".pdf");
			
            if(file.delete()){
                System.out.println(file.getName() + " Was deleted!");
                response="success";
            }else{
                System.out.println("Delete Operation Failed. Check: " + file);
                response="fail";
            }
        }catch(Exception e1){
            e1.printStackTrace();
        }
		return response;
	}
	//to get data for MIS PO DETAILS 
	
	@RequestMapping(value ="MISReportsPoDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISReportsPoDetails(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		
		Map<String, String> siteDetails = null;
		siteDetails = new IndentSummaryDao().getSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		model.addObject("succMessage","");
		//request.setAttribute("Error","false");
		model.addObject("PODate","PO Date");
		model.addObject("MISPODETAILS","MIS PO Details");
		model.addObject("PurchasePoReport","PurchasePoReport");
		model.setViewName("reports/MIS_PODetails");
		return model;
	}
	@RequestMapping(value ="MISPoPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISPoPage(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		List<ProductDetails> poDetails = null;
		String response="";
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		//String checked=request.getParameter("checkbox_site_name");
		String siteIds=request.getParameter("siteIds");
		
		if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
			poDetails =objPurchaseDeptIndentrocess.getViewPoDetailsDetails(request,fromDate,toDate);	
		}
		if(poDetails != null && poDetails.size() >0){
			String PO_Total=request.getAttribute("Po_GrandTotal").toString();
			String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
			
			model.addObject("Po_GrandTotal",PO_Total);
			model.addObject("Invoice_GrandTotal",Invoice_Total);
			request.setAttribute("showGrid", "true");
			request.setAttribute("poDetails",poDetails);
			model.addObject("succMessage","");
			model.addObject("fromDate",fromDate);
			model.addObject("toDate", toDate);
			model.addObject("siteIds", siteIds);
			model.setViewName("reports/MIS_PODetailsPage");
			
		}else {
			model.addObject("succMessage","The above Dates Data Not Available");
			response="failed";
			model.addObject("PODate","PO Date");
			//request.setAttribute("Error","true");
			Map<String, String> siteDetails = null;
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			model.setViewName("reports/MIS_PODetails");
		}
		//request.setAttribute("showGrid", "true");
		
		return model;
	}
	
		//to set market purchase data 
		//to get data for MIS PO DETAILS 
	
	@RequestMapping(value ="MISMarketPurchaseDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISMarketPurchaseDetails(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		Map<String, String> siteDetails = null;
		siteDetails = new IndentSummaryDao().getSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		model.addObject("succMessage","");
		model.addObject("PODate","");
		model.addObject("MISPODETAILS","MIS Market Purchase Details");
		model.addObject("PurchasePoReport","MarketPurchase");
		model.setViewName("reports/MIS_PODetails");
		return model;
	}
	
	
	@RequestMapping(value ="MISMarketPurchasePage.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISLocalPurchasePage(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		List<ProductDetails> MarketPurchaseDetails = null;
		String response="";
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		
		
		if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
			MarketPurchaseDetails =objPurchaseDeptIndentrocess.getViewMarketPurchaseDetails(request,fromDate,toDate,"marketPurchase");	
		}
		if(MarketPurchaseDetails != null && MarketPurchaseDetails.size() >0){
			String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
			model.addObject("Invoice_GrandTotal",Invoice_Total);
			request.setAttribute("showGrid", "true");
			request.setAttribute("MarketPurchaseDetails",MarketPurchaseDetails);
			model.setViewName("reports/MIS_MarketPurchase");
			
		}else {
			model.addObject("succMessage","The above Dates Data Not Available");
			response="failed";
			model.addObject("PODate","");
			Map<String, String> siteDetails = null;
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			model.setViewName("reports/MIS_PODetails");
		}
		//request.setAttribute("showGrid", "true");
		
		return model;
	}
	
	
	// this is for local purchase purpose
	@RequestMapping(value ="MISLocalPurchaseDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISLocalPurchaseDetails(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		Map<String, String> siteDetails = null;
		siteDetails = new IndentSummaryDao().getSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		model.addObject("succMessage","");
		model.addObject("PODate","");
		model.addObject("MISPODETAILS","MIS Local Purchase Details");
		model.addObject("PurchasePoReport","LocalPurchase");
		model.setViewName("reports/MIS_PODetails");
		return model;
	}
	
	
	@RequestMapping(value ="MISLocalPurchasePage.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView MISMarketPurchasePage(HttpServletRequest request,HttpSession session) {
		ModelAndView model =  new ModelAndView();
		List<ProductDetails> LocalPurchaseDetails = null;
		String response="";
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		
		
		
		if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
			LocalPurchaseDetails =objPurchaseDeptIndentrocess.getViewMarketPurchaseDetails(request,fromDate,toDate,"localPurchase");	
		}
		if(LocalPurchaseDetails != null && LocalPurchaseDetails.size() >0){
			String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
			model.addObject("Invoice_GrandTotal",Invoice_Total);
			request.setAttribute("showGrid", "true");
			request.setAttribute("LocalPurchaseDetails",LocalPurchaseDetails);
			model.setViewName("reports/MIS_LocalPurchase");
			
		}else {
			model.addObject("succMessage","The above Dates Data Not Available");
			response="failed";
			model.addObject("PODate","");
			Map<String, String> siteDetails = null;
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			model.setViewName("reports/MIS_PODetails");
		}
		//request.setAttribute("showGrid", "true");
		
		return model;
	}
	
	
	
		/*@RequestMapping(value ="MISMarketPurchaseDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISMarketPurchaseDetails(HttpServletRequest request,HttpSession session) {

			String site_id = "";
			String toDate = "";
			String fromDate = "";
			ModelAndView model = null;
			String response="";
			List<ProductDetails> MarketPurchaseDetails = null;
			//Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
			try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
					session = request.getSession(false);
				 site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
					
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
					if (StringUtils.isNotBlank(site_id)) {
						MarketPurchaseDetails =objPurchaseDeptIndentrocess.getViewMarketPurchaseDetails(fromDate,toDate);
						if(MarketPurchaseDetails != null && MarketPurchaseDetails.size() >0){
							request.setAttribute("showGrid", "true");
							
						} else {
							model.addObject("succMessage","The above Dates Data Not Available");
							response="failed";
						}
						model.addObject("MarketPurchaseDetails",MarketPurchaseDetails);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.setViewName("reports/MIS_MarketPurchase");
						response="success";

					} else {
						model.addObject("Message","Session Expired, Please Login Again");
						model.setViewName("index");
						response="failed";
						return model;
					}
				} else {
					model.addObject("displayErrMsg", "Please Select From Date or To Date!");
					model.addObject("MarketPurchaseDetails",MarketPurchaseDetails);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.setViewName("reports/MIS_MarketPurchase");
					response="failed";
				}
			} catch (Exception ex) {
				response="failed";
				ex.printStackTrace();
			} 

			SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"get MIS PO Details View data",response,site_id1);

			return model;
			
	}*/
/*	*****************************************************MIS Report SiteLevel Po start************************************************/ 
		
		@RequestMapping(value ="MISSiteLevelPo.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISSiteLevelPo(HttpServletRequest request,HttpSession session) {
			ModelAndView model =  new ModelAndView();
			Map<String, String> siteDetails = null;
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			model.addObject("succMessage","");
			model.addObject("PODate","PO Date");
			model.addObject("MISPODETAILS","MIS Site PO Details");
			model.addObject("PurchasePoReport","SiteLevelPoReport");
			model.setViewName("reports/MIS_PODetails");
			return model;
		}	
		
		@RequestMapping(value ="MISSieLevelPoPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISSieLevelPoPage(HttpServletRequest request,HttpSession session) {
			ModelAndView model =  new ModelAndView();
			List<ProductDetails> siteLevelPoDetails = null;
			String response="";
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			//String checked=request.getParameter("checkbox_site_name");
			String siteIds=request.getParameter("siteIds");
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				siteLevelPoDetails =objPurchaseDeptIndentrocess.getSiteLevelPoDetails(request,fromDate,toDate);	
			}
			if(siteLevelPoDetails != null && siteLevelPoDetails.size() >0){
				String PO_Total=request.getAttribute("Po_GrandTotal").toString();
				String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
				
				model.addObject("Po_GrandTotal",PO_Total);
				model.addObject("Invoice_GrandTotal",Invoice_Total);
				request.setAttribute("showGrid", "true");
				request.setAttribute("siteLevelPoDetails",siteLevelPoDetails);
				//model.addObject("fromDate",fromDate);
				//model.addObject("toDate", toDate);
				//model.addObject("siteIds", siteIds);
				model.setViewName("reports/MIS_SiteLevel_PODetails");
				
			}else {
				model.addObject("succMessage","The above Dates Data Not Available");
				response="failed";
				model.addObject("PODate","PO Date");
				Map<String, String> siteDetails = null;
				siteDetails = new IndentSummaryDao().getSiteDetails();
				request.setAttribute("siteDetails",siteDetails);
				model.setViewName("reports/MIS_PODetails");
			}
			//request.setAttribute("showGrid", "true");
			
			return model;
		}
			
		
		
		
		/*@RequestMapping(value ="MISSiteLevelPo.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISSiteLevelPoAndLocalPurchase(HttpServletRequest request,HttpSession session) {
			String site_id = "";
			String toDate = "";
			String fromDate = "";
			ModelAndView model = null;
			String response="";
			List<ProductDetails> siteLevelPoDetails = null;// this is used for to get sitelevel 
			//Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
			try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
					session = request.getSession(false);
				 site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
					
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
					if (StringUtils.isNotBlank(site_id)) {
						siteLevelPoDetails =objPurchaseDeptIndentrocess.getSiteLevelPoDetails(request,fromDate,toDate);
						if(siteLevelPoDetails != null && siteLevelPoDetails.size() >0){
							String PO_Total=request.getAttribute("Po_GrandTotal").toString();
							String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
							model.addObject("Po_GrandTotal",PO_Total);
							model.addObject("Invoice_GrandTotal",Invoice_Total);
							request.setAttribute("showGrid", "true");
							
						} else {
							model.addObject("succMessage","The above Dates Data Not Available");
							response="failed";
						}
						model.addObject("siteLevelPoDetails",siteLevelPoDetails);
						request.setAttribute("siteLevelPoDetails",siteLevelPoDetails);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.setViewName("reports/MIS_SiteLevel_PODetails");
						response="success";

			SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"get MIS SiteLevel LocalPurchase Details View data",response,site_id1);

			return model;
			
	}*/
		
		
	/*	*****************************************************MIS Report SiteLevel Po end************************************************/ 
/*************************************************************MIS Report VendorPayment Details start*************************************************************/

		@RequestMapping(value ="MISVendorPaymentDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISVendorPaymentDetails(HttpServletRequest request,HttpSession session) {
			ModelAndView model =  new ModelAndView();
			Map<String, String> siteDetails = null;
			siteDetails = new IndentSummaryDao().getSiteDetails();
			request.setAttribute("siteDetails",siteDetails);
			model.addObject("MISPODETAILS","MIS Vendor Payment Details");
			model.addObject("succMessage","");
			model.addObject("PODate","");
			model.addObject("PurchasePoReport","VendorPayment");
			model.setViewName("reports/MIS_PODetails");
			return model;
		}
		
		
		@RequestMapping(value ="MISVendorPaymentPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISVendorPaymentPage(HttpServletRequest request,HttpSession session) {
			ModelAndView model =  new ModelAndView();
			List<ProductDetails> VendorPaymentDetails = null;
			String response="";
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			
			
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				VendorPaymentDetails =objPurchaseDeptIndentrocess.getVendorPaymentDetails(request,fromDate,toDate);	
			}
			if(VendorPaymentDetails != null && VendorPaymentDetails.size() >0){
				//String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
				//model.addObject("Invoice_GrandTotal",Invoice_Total);
				request.setAttribute("showGrid", "true");
				request.setAttribute("VendorPaymentDetails",VendorPaymentDetails);
				model.setViewName("reports/MIS_Vendor_Payment_Details");
				
			}else {
				model.addObject("succMessage","The above Dates Data Not Available");
				response="failed";
				model.addObject("PODate","");
				Map<String, String> siteDetails = null;
				siteDetails = new IndentSummaryDao().getSiteDetails();
				request.setAttribute("siteDetails",siteDetails);
				model.setViewName("reports/MIS_PODetails");
			}
			//request.setAttribute("showGrid", "true");
			
			return model;
		}
		
		
		
		
		/*@RequestMapping(value ="MISVendorPaymentDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISVendorPaymentDetails(HttpServletRequest request,HttpSession session) {
			String site_id = "";
			String toDate = "";
			String fromDate = "";
			ModelAndView model = null;
			String response="";
			List<ProductDetails> VendorPaymentDetails = null;// this is used for to get VendorPaymentDetails 
		
			try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
					session = request.getSession(false);
				 site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
					
				
					if (StringUtils.isNotBlank(site_id)) {
						VendorPaymentDetails =objPurchaseDeptIndentrocess.getVendorPaymentDetails(fromDate,toDate);
						if(VendorPaymentDetails != null && VendorPaymentDetails.size() >0){
							request.setAttribute("showGrid", "true");
							
						} else {
							model.addObject("succMessage","The above Dates Data Not Available");
							response="failed";
						}
						model.addObject("VendorPaymentDetails",VendorPaymentDetails);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.setViewName("reports/MIS_Vendor_Payment_Details");
						response="success";

					} else {
						model.addObject("Message","Session Expired, Please Login Again");
						model.setViewName("index");
						response="failed";
						return model;
					}
				} else {
					model.addObject("displayErrMsg", "Please Select From Date or To Date!");
					model.addObject("VendorPaymentDetails",VendorPaymentDetails);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.setViewName("reports/MIS_Vendor_Payment_Details");
					response="failed";
				}
			} catch (Exception ex) {
				response="failed";
				ex.printStackTrace();
			} 

			SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"get MIS SiteLevel LocalPurchase Details View data",response,site_id1);

			return model;
			
	}
		*/
		/*====================================================MIS Report VendorPayment Details end*************************************************************/
		
	/*	====================================================MIS PO Report Print Page===========================================================*/
	
		@RequestMapping(value ="MISPoReportPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISPoReportPage(HttpServletRequest request,HttpSession session) {
			ModelAndView model = new ModelAndView();
			List<ProductDetails> poDetails = null;
			String fromDate=request.getParameter("fromDate");
			String toDate=request.getParameter("toDate");
			String siteIds=request.getParameter("siteIds");
			
			poDetails=objPurchaseDeptIndentrocess.getViewPoDetailsDetails(request,fromDate,toDate);
			String PO_Total=request.getAttribute("Po_GrandTotal").toString();
			String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
			
			Date date = new Date();
			String currentDate= new SimpleDateFormat("dd-MM-yyyy").format(date);
			SimpleDateFormat fromdate=new SimpleDateFormat("dd-MMM-yyyy");
			model.addObject("Po_GrandTotal",PO_Total);
			model.addObject("Invoice_GrandTotal",Invoice_Total);
			
			if(fromDate.equals("")){fromDate="Starting";}
			
			if(toDate.equals("")){toDate=currentDate;}
			model.addObject("fromDate",fromDate);
			model.addObject("toDate",toDate);
			model.addObject("poDetails",poDetails);
			model.addObject("currentDate",currentDate);
			model.setViewName("reports/MISPO-PrintIndent");
			
			return model;
		}
		
		/*@RequestMapping(value ="MISPoPage.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView MISPoPage(HttpServletRequest request,HttpSession session) {
			ModelAndView model = new ModelAndView();
			List<ProductDetails> poDetails = null;
			String fromDate=request.getParameter("fromDate");
			String toDate=request.getParameter("toDate");
			poDetails=objPurchaseDeptIndentrocess.getViewPoDetailsDetails(request,fromDate,toDate);
			String PO_Total=request.getAttribute("Po_GrandTotal").toString();
			String Invoice_Total=request.getAttribute("Invoice_GrandTotal").toString();// or grand total purpose
			
			model.addObject("Po_GrandTotal",PO_Total);
			model.addObject("Invoice_GrandTotal",Invoice_Total);
			
			model.addObject("poDetails",poDetails);
			model.setViewName("reports/MISPO-PrintIndent");
			return model;
		}*/
		
		
		// after create permanent po then cancel it
		/*****************************************cancel PO start*************************************************/
		
		@RequestMapping(value = "/cancelPerminentPO.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String cancelPerminentPO(HttpServletRequest request, HttpSession session,Model model){

			String user_id=String.valueOf(session.getAttribute("UserId"));
			String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String poNumber=request.getParameter("poNumber");
			
			List<Map<String, Object>> poList = null;
			String view = "";
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(poNumber)) {
				poList=objPurchaseDeptIndentrocess.getListOfActivePOForCancelPermanentPOs(sessionSite_id,fromDate,toDate,poNumber);
				if(poList.size()>0){
				model.addAttribute("listOfPOs",poList); // here change requirement so written A
				model.addAttribute("showData",true);}
				else{
					model.addAttribute("responseMessage", "Data Not Available");	
				}
			if(sessionSite_id.equals("996")){ // here we need to check marketing or normal po other sites can open else part 
				model.addAttribute("isMarketing",true); // here use true same page using in two places
				
				
			}else{
				//model.addAttribute("listOfPOs",objPurchaseDeptIndentrocess.getListOfActivePOs(sessionSite_id));
				model.addAttribute("isPurchase",true);// here use true same page using in two places
				//model.addAttribute("showData",true);
				
			}
			}else{
				//model.addAttribute("responseMessage", "Please Select From Date or To Date!");
				model.addAttribute("fromDate",fromDate);
				model.addAttribute("toDate", toDate);
			
			}
			view="CancelPerminentPO";
			SaveAuditLogDetails.auditLog("0",user_id,"Cancel Permanent PO","success",sessionSite_id);


			return view;

		}

		// when the user click on the link then show the permanent po details
		
		@RequestMapping(value = "/showPerminentPODetailsToCancel.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String showPerminentPODetailsToCancel(HttpServletRequest request, HttpSession session,Model model){

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
			String poType="";
			String response="";
			int strCount=0;
			String isApprove="";
			String previousEmpComments="";
			String ccEmails="";
			String chekFromMail="";
			String password="";
			String userId="";
			String fromDate="";
			String toDate="";
			try{
				if(portNumber==80){rootFilePath=validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString();}else{
					rootFilePath=validateParams.getProperty("UPLOAD_CUG_PDF") == null ? "" : validateParams.getProperty("UPLOAD_CUG_PDF").toString();	
				}
				//site_Id=request.getParameter("siteId");
				 
				poNumber = request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber");
				site_Id = request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString();
				poType=request.getParameter("poType") == null ? "" : request.getParameter("poType"); // here which type po marketing or other po 
				isApprove=request.getParameter("isApprove") == null ? "" : request.getParameter("isApprove"); // first time and another time checking purpose written this
				chekFromMail=request.getParameter("isMail") == null ? "" : request.getParameter("isMail");
				password=request.getParameter("mailPassword") == null ? "" : request.getParameter("mailPassword");
				userId=session.getAttribute("UserId")== null ? "" : session.getAttribute("UserId").toString();
				fromDate=request.getParameter("fromdate") == null ? "" : request.getParameter("fromdate");
				toDate=request.getParameter("toDate") == null ? "" : request.getParameter("toDate");
				model.addAttribute("urlName","cancelPerminentPO.spring");
				if(fromDate.equalsIgnoreCase("null")){
					fromDate="";
				}if(toDate.equalsIgnoreCase("null")){
					toDate="";
				}
				
				if(userId==null || userId.equals("")){
					userId=request.getParameter("userId") == null ? "" : request.getParameter("userId"); //this is for mail purpose
				}
				if(isApprove.equalsIgnoreCase("false")){ // here is used to check check in second time user click on cancel at that time it will check below condition
					boolean check=objPurchaseDeptIndentrocess.checkAlreadyCancelOrnot(poNumber);
					if(check){ model.addAttribute("message1","Already Permanent Cancel Po Initiated.");
						viewToBeSelected="response";
						return viewToBeSelected;}
				}
				
				imageClick = request.getParameter("imageClick") == null ? false : Boolean.valueOf(request.getParameter("imageClick"));
				if(isApprove.equalsIgnoreCase("true")){
					model.addAttribute("urlName","ViewPoPendingforApproval.spring?fromDate="+fromDate+"&toDate="+toDate);
					//request.setAttribute("PoLevelComments",ics.getPoLevelComments(poentryId,"true"));
					model.addAttribute("isApprove",true);} // this is for approve purpose written this i.e same methode using so written
				if (StringUtils.isNotBlank(site_Id) && StringUtils.isNotBlank(poNumber)){
						
					model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
					model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
					String filepath=rootFilePath+"PO//";
					String strPO_Number=poNumber.replace('/','$');
					
					// this is for marketing po purpose to check it 
					if(poType.equalsIgnoreCase("MARKETING_DEPT")){
						response=objMarketingDeptService.getMarketingPoDetailsList(poNumber,site_Id,request);
						loadPoImgAndPdfFiles(filepath,strPO_Number,"PO",model,request); // call the getting pdf and images for showing purpose
						//IndentCreationController.loadVendorImgAndPdfFiles(filepath,strPO_Number,model, request);
						
						viewToBeSelected="marketing/Marketing_POCancelPrintPage";
						
					}else{ // this is for other po's like sitelevel,revised,purchase po
						response=ics.getPoDetailsList(poNumber,site_Id,request);
						for (int i = 0; i < 4; i++) {
							
							File file = new File(filepath+strPO_Number+"_Part"+i+".pdf");
							long count=file.length();
							if(file.exists()){request.setAttribute("uploadorNot","true");
							String encoded=CommonUtilities.encodeFileToBase64Binary(filepath+strPO_Number+"_Part"+i+".pdf");
							model.addAttribute("pdf"+i,encoded);
							strCount++;
							}else{
								//request.setAttribute("uploadorNot","false");
							}
							model.addAttribute("count",strCount);
						}
						viewToBeSelected="PurchasePermanentPOSuccessPage";
					}
					 // here we need to get and set the data in success page so used
					versionNo=request.getAttribute("versionNo").toString();
					refferenceNo=request.getAttribute("refferenceNo").toString();
					strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
					poentryId = request.getAttribute("poentryId").toString();
					String internalComments=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoInternalComments(poentryId);
					String vendorComments=objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoComments(poentryId);
					if(isApprove.equalsIgnoreCase("true")){
					ccEmails=objPurchaseDepartmentIndentProcessDao.getCancelPoApprovalCCmails(poentryId); // ccmails purpose written this one approval time else is using 
					}else{
					ccEmails=objPurchaseDepartmentIndentProcessDao.getCCmails(poentryId,site_Id,true);
					}
					request.setAttribute("cancelPOinternalComments",internalComments); // get comments of created po time given that
					request.setAttribute("cancelPOVendorComments",vendorComments);// get the vendor comments
					
					model.addAttribute("ccEmails",ccEmails);// cc mails purpose written this one
					model.addAttribute("versionNo",versionNo);
					model.addAttribute("refferenceNo",refferenceNo);
					model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
					request.setAttribute("poPage","true");
					model.addAttribute("poNumber",poNumber);
					model.addAttribute("isCancelPo",true);
					model.addAttribute("poType",poType); // mail from approval written this one
					model.addAttribute("chekFromMail",chekFromMail); // mail from approval written this onechekFromMail
					model.addAttribute("password",password); // mail from approval written this onechekFromMail
					model.addAttribute("userId",userId); // mail from approval written this onechekFromMail
					if(response.equalsIgnoreCase("Success")) {
						request.setAttribute("imageClick",imageClick);
					}
					model.addAttribute("isPerminentCancelPo",true);
					
				}
				
			}catch(Exception ex) {
				ex.printStackTrace();
			} 
			return viewToBeSelected;
		}
		// save the user click on permanent po button cancel po details 
		@RequestMapping(value = "/saveCancelPerminentPODetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String saveCancelPerminentPODetails(HttpServletRequest request, HttpSession session,Model model){
			
			/*TransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(def);*/
			String activeOrNot="";
			String user_id=session.getAttribute("UserId")== null ? "" : session.getAttribute("UserId").toString();
			String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
			if(user_id==null || user_id.equals("")){
				user_id=request.getParameter("userId")== null ? "" : request.getParameter("userId").toString();
			}
			
			String vendorComment=request.getParameter("vendorComment");
			String normalComment=request.getParameter("normalComment");
			String sessionPOEntryid=session.getAttribute("strPOentryId")== null ? "" : session.getAttribute("strPOentryId").toString();
			
			String poNumber=request.getParameter("poNumber");
			String siteId=request.getParameter("siteId");
			String vendorId=request.getParameter("vendorId");
			String poentryId=request.getParameter("poentryId");
			String siteName=request.getParameter("siteName");
			String ccmails=request.getParameter("ccComment");
			String poType=request.getParameter("poType");
			
			String previousEmpId="";
			int portNumber=request.getLocalPort();
			
			
			String indentNumber=request.getParameter("indentNumber")== null ? "" : request.getParameter("indentNumber").toString();
			String poDate=request.getParameter("poDate");
			String pendingEmpId="";
			String approveStatus="C";
			boolean paymentStrStatus=false;
			int intTotalNoOfRecords = Integer.parseInt(request.getParameter("totalNoOfRecords") == null ? "0" : request.getParameter("totalNoOfRecords").toString());
			String isApprove=request.getParameter("isApprove") == null ? "0" : request.getParameter("isApprove").toString();
			String viewToSelected="";
			try{// for mail purpose i.e password we need to take this one random number
			Random rand = new Random();
			int rand_Number = rand.nextInt(1000000);
			String passwdForMail=String.valueOf(rand_Number);
			String password="";
			// here check the po already received or not belkow condition
			String chekFromMail=request.getParameter("chekFromMail") == null ? "" : request.getParameter("chekFromMail").toString();
			String strPassword=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			String urlName=request.getParameter("urlName") == null ? "" : request.getParameter("urlName").toString();
			if(poNumber==null || poNumber.equals("")){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			// this is for user click on refresh again
			if(!isApprove.equalsIgnoreCase("true")){
				activeOrNot=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(poNumber,false);
				if(!activeOrNot.equals("A")){
					model.addAttribute("message1","Already Submited.");
					return "response";
				}
			}
			model.addAttribute("iscancel",true);
			String dbPasswd=objPurchaseDepartmentIndentProcessDao.getTempPOPassword(poNumber,true);
			//String userID=
			if(chekFromMail.equalsIgnoreCase("true")){
				
				if(!dbPasswd.equalsIgnoreCase(strPassword)){
					model.addAttribute("message1","Already Approved or Rejected Permanent Cancel PO");
					model.addAttribute("iscancel",false);
					return viewToSelected="response";
				}
			}
			boolean strStatus=objPurchaseDeptIndentrocess.checkPoDoneInvoiceOrDcPayment(poNumber,siteId,vendorId);
			if(!strStatus){
			 paymentStrStatus=objPurchaseDeptIndentrocess.checkPoPaymentDoneOrNot(poNumber,siteId,vendorId,poDate,siteName,poentryId,portNumber,false,false);
			}
			if(strStatus){
				if(isApprove.equalsIgnoreCase("true")){
					objPurchaseDepartmentIndentProcessDao.updateCancelPoDetailsInCancelTbl(poentryId,"VND","I","0",false);	
				}
				model.addAttribute("message1","Sorry ! Unable to Cancel PO, as material inwarded.");
				model.addAttribute("urlName",urlName);
				return viewToSelected="response";
			}
			if(paymentStrStatus){ // check po payment done or not i.e initiated or not
				model.addAttribute("message1","Sorry ! Unable to Cancel PO, as payment had been initiated.");
				model.addAttribute("urlName",urlName);
				return viewToSelected="response";
			}
			//EmailFunction objEmailFunction = new EmailFunction();
			

			if((!indentNumber.equalsIgnoreCase("null")) && indentNumber!=null && !indentNumber.equals("")){ // here we take purchse po purpose take indent number else part is marketing knowing purpose
				pendingEmpId=objPurchaseDeptIndentrocess.getCancelPOPendinfEmpId(user_id,"CANCEL_PO");}
			else{ pendingEmpId=objPurchaseDeptIndentrocess.getCancelPOPendinfEmpId(user_id,"MARKETING_CANCEL_PO");}
			
			if(pendingEmpId.equals("") || sessionPOEntryid.equals(poentryId)){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				model.addAttribute("iscancel",false);
				return viewToSelected="response";
			}
			// this is for approval time we can check the condition why because normal and approve time use same method
			if(isApprove.equalsIgnoreCase("true")){
				objPurchaseDeptIndentrocess.updateCancelPoDetailsInCancelTbl(poentryId,pendingEmpId,"A",passwdForMail,false);
				//model.addAttribute("iscancel",true);
				approveStatus="A"; // here approval there then take status A
				model.addAttribute("urlForActivateSubModule","ViewPoPendingforApproval.spring");
			}else{
				model.addAttribute("urlForActivateSubModule","cancelPerminentPO.spring");
			}
			

			if(pendingEmpId!=null && pendingEmpId.equals("VND")){ // no aproval there then permanent po cancel execute this
				objPurchaseDeptIndentrocess.inActivePOTable(poNumber,siteId);
				boolean paymentStatus=objPurchaseDeptIndentrocess.updatePaymentDtls(request,poNumber,siteId,vendorId);// check the payment tables
				if(!paymentStatus){
					return viewToSelected="response";
				}
				if(!approveStatus.equals("C")){ // here approval time it was executed why because send mail to previous emp he know approve or not
					previousEmpId=objPurchaseDepartmentIndentProcessDao.previousEmpId(poentryId);
					objPurchaseDeptIndentrocess.sendPreviousApproveEmpMail(poentryId,poNumber,poDate,siteName,previousEmpId,vendorId,user_id);}
				if(!isApprove.equalsIgnoreCase("true")){ // no approval time save in cancel po table

				objPurchaseDeptIndentrocess.saveCancelPoDetailsInCancelTbl(poentryId,poNumber,pendingEmpId,"A",passwdForMail);} // this is for sumadhura_cancel_po Perminently cancelled
				objPurchaseDeptIndentrocess.saveCancelPoApproveDetails(poentryId,siteId,user_id,approveStatus,vendorComment,normalComment,ccmails); // this is for approval detals save in Approval details table 
				// here when the other than marketing po it will taken and checkcondition 
				if((!indentNumber.equalsIgnoreCase("null")) && indentNumber!=null && !indentNumber.equals("") && intTotalNoOfRecords>0){ 
					objPurchaseDeptIndentrocess.getPoQuantityIndentCreationDetailsForCancelPermanentPo(indentNumber,poentryId,poNumber);

				} //mail send to vendor along with purchase or marketing dept people get mails
				objPurchaseDeptIndentrocess.sendMailToVendorForCancelPO(request,poentryId,poNumber,vendorId,siteId,poDate,siteName,portNumber,ccmails);
				model.addAttribute("message","PO Cancelled Successfully.");
				model.addAttribute("urlName",urlName);
			}
			
			
			else{ // approval time it will executed 
				objPurchaseDeptIndentrocess.saveCancelPoApproveDetails(poentryId,siteId,user_id,approveStatus,vendorComment,normalComment,ccmails); // this is for approval detals save in Approval details table 
				objPurchaseDeptIndentrocess.sendMailPerminentPoCancel(request,pendingEmpId,poentryId,poNumber,vendorId,siteId,poDate,siteName,passwdForMail,user_id);
				if(!isApprove.equalsIgnoreCase("true")){ // check if anthoter approval there or not
					objPurchaseDepartmentIndentProcessDao.inactiveOldPo(poNumber,"true");
					objPurchaseDeptIndentrocess.saveCancelPoDetailsInCancelTbl(poentryId,poNumber,pendingEmpId,"A",passwdForMail);}
				if(!approveStatus.equals("C")){previousEmpId=objPurchaseDepartmentIndentProcessDao.previousEmpId(poentryId); // send mail to prevoius person
				objPurchaseDeptIndentrocess.sendPreviousApproveEmpMail(poentryId,poNumber,poDate,siteName,previousEmpId,vendorId,user_id);
				} // this is for sumadhura_cancel_po Perminently cancelled	
				model.addAttribute("message","Cancel PO is sent for approvals.");
				model.addAttribute("urlName",urlName);
				//model.addAttribute("urlName","cancelPerminentPO.spring");
				
			}
			session.setAttribute("strPOentryId",poentryId);
			//transactionManager.commit(status);
			viewToSelected="response";
			}catch(Exception e){
				e.printStackTrace();
				//transactionManager.rollback(status);
			}
			
			SaveAuditLogDetails.auditLog("0",user_id,"Save Cancel Perminent PO","success",sessionSite_id);
			
		return viewToSelected;
	}
		@RequestMapping(value = "/CancelPerminentPODetailsReject.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String CancelPerminentPODetailsReject(HttpServletRequest request, HttpSession session,Model model){
			String viewToSelected="";
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
			String poentryId=request.getParameter("poentryId");
			String poNumber=request.getParameter("poNumber") == null ? "" : request.getParameter("poNumber").toString();
			
			String chekFromMail=request.getParameter("chekFromMail") == null ? "" : request.getParameter("chekFromMail").toString();
			String strPassword=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			String urlName=request.getParameter("urlName") == null ? "" : request.getParameter("urlName").toString();
			String dbPasswd=objPurchaseDepartmentIndentProcessDao.getTempPOPassword(poNumber,true);
			String sessionPOEntryid=session.getAttribute("strPOentryId")== null ? "" : session.getAttribute("strPOentryId").toString();
			model.addAttribute("iscancel",true);
			if(chekFromMail.equalsIgnoreCase("true")){
				if(!dbPasswd.equalsIgnoreCase(strPassword)){
					model.addAttribute("message1","Already Approved or Rejected Permanent Cancel PO");
					model.addAttribute("iscancel",false);
					return viewToSelected="response";
				}
			}
			if(sessionPOEntryid.equals(poentryId)){
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				model.addAttribute("iscancel",false);
				return viewToSelected="response";
			}
			
			//String isMail=request.getParameter("poentryId");
			String status=objPurchaseDeptIndentrocess.CancelPerminentPODetailsReject(session,request,poentryId,"");// here we need to check which from mail or normal reject 
			model.addAttribute("urlName",urlName);
			session.setAttribute("strPOentryId",poentryId);
			return status;
		}
		
		/*******************************************POerminent cancel Po Rejected from Mail************************************************************/
		@RequestMapping(value = "/CancelPerminentPODetailsRejectFromMail.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String CancelPerminentPODetailsRejectFromMail(HttpServletRequest request, HttpSession session,Model model){
			String status="";
			String poEntryId=request.getParameter("poEntryId");
			String poNumber=request.getParameter("poNumber");
			String mailPassword=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			String dbPasswd=objPurchaseDepartmentIndentProcessDao.getTempPOPassword(poNumber,true); // this is used to click again it is used to check 
			if(!dbPasswd.equalsIgnoreCase(mailPassword)){
				model.addAttribute("message1","Oops !!! There was a improper request found.");
				model.addAttribute("iscancel",false);
				return status="response";
			}
			 status=objPurchaseDeptIndentrocess.CancelPerminentPODetailsReject(session,request,poEntryId,"true");
			
			return status;
			
		}
		
		@RequestMapping(value = "/saveApprovalCancelPermanentPoFromMail.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String saveApprovalCancelPermanentPo(HttpServletRequest request, HttpSession session,Model model){
			/*TransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(def);*/
			Random rand = new Random(); // random number generated using like password 
			int rand_Number = rand.nextInt(1000000);
			String passwdForMail=String.valueOf(rand_Number);
			String pendingEmpId="";
			String viewToSelected="";
			String user_id=request.getParameter("userId");
			String indentNumber=request.getParameter("indentNumber")== null ? "" : request.getParameter("indentNumber").toString();
			String poEntryId=request.getParameter("poEntryId");
			String siteId=request.getParameter("siteId");
			String siteName=request.getParameter("siteName");
			String poNumber=request.getParameter("poNumber");
			String poDate=request.getParameter("poDate");
			String vendorId=request.getParameter("vendorId");
			String vendorComment=request.getParameter("vendorComments");
			String normalComment=request.getParameter("Remarks");
			String ccmails=request.getParameter("ccComment");
			String mailPassword=request.getParameter("password") == null ? "" : request.getParameter("password").toString();
			model.addAttribute("iscancel",false);
			String dbPasswd=objPurchaseDepartmentIndentProcessDao.getTempPOPassword(poNumber,true); // this is used to click again it is used to check 
			
			if(!dbPasswd.equalsIgnoreCase(mailPassword)){
				model.addAttribute("message1","Oops !!! There was a improper request found.");
				
				return viewToSelected="response";
			}
			String previousEmpId="";
			boolean paymentStrStatus=false;
			int portNumber=request.getLocalPort();
			try{
				boolean strStatus=objPurchaseDeptIndentrocess.checkPoDoneInvoiceOrDcPayment(poNumber,siteId,vendorId);
				if(!strStatus){
				 paymentStrStatus=objPurchaseDeptIndentrocess.checkPoPaymentDoneOrNot(poNumber,siteId,vendorId,poDate,siteName,poEntryId,portNumber,false,false);
				}
				if(strStatus){ // check invoice or dc do in middle po approval time 
					objPurchaseDepartmentIndentProcessDao.updateCancelPoDetailsInCancelTbl(poEntryId,"VND","I","0",false);	
					model.addAttribute("message1","Sorry ! Unable to Cancel PO, as material inwarded.");
					return viewToSelected="response";
				}
				if(paymentStrStatus){ // check po payment done or not i.e initiated or not
					model.addAttribute("message1","Sorry ! Unable to Cancel PO, as payment had been initiated.");
					return viewToSelected="response";
				}
				
				if((!indentNumber.equalsIgnoreCase("null")) && indentNumber!=null && !indentNumber.equals("")){ // two separate marketing and other po so taken based on indent number condition
					pendingEmpId=objPurchaseDeptIndentrocess.getCancelPOPendinfEmpId(user_id,"CANCEL_PO");}
				else{ pendingEmpId=objPurchaseDeptIndentrocess.getCancelPOPendinfEmpId(user_id,"MARKETING_CANCEL_PO");}
				if(pendingEmpId!=null && pendingEmpId.equals("VND")){
					objPurchaseDeptIndentrocess.inActivePOTable(poNumber,siteId);
					objPurchaseDeptIndentrocess.updateCancelPoDetailsInCancelTbl(poEntryId,pendingEmpId,"A",passwdForMail,false); // this is for sumadhura_cancel_po Perminently cancelled
					boolean paymentStatus=objPurchaseDeptIndentrocess.updatePaymentDtls(request,poNumber,siteId,vendorId); // check paytment initiated or advance pay check here
					if(!paymentStatus){ // check if it is false then it will initiate or advance
						return viewToSelected="response";
					}
					if((!indentNumber.equalsIgnoreCase("null")) && indentNumber!=null && !indentNumber.equals("")){ // if cancel one is other than marketing po it will take indent number 

						objPurchaseDeptIndentrocess.getPoQuantityIndentCreationDetailsForCancelPermanentPo(indentNumber,poEntryId,poNumber);}

					previousEmpId=objPurchaseDepartmentIndentProcessDao.previousEmpId(poEntryId);
					objPurchaseDeptIndentrocess.sendPreviousApproveEmpMail(poEntryId,poNumber,poDate,siteName,previousEmpId,vendorId,user_id);
					objPurchaseDeptIndentrocess.saveCancelPoApproveDetails(poEntryId,siteId,user_id,"A",vendorComment,normalComment,ccmails);// this is for approval detals save in Approval details table 
					objPurchaseDeptIndentrocess.sendMailToVendorForCancelPO(request,poEntryId,poNumber,vendorId,siteId,poDate,siteName,portNumber,ccmails);
					model.addAttribute("message","PO Cancelled Successfully.");
					viewToSelected="response";

				}else{

					//objPurchaseDeptIndentrocess.saveCancelPoDetailsInCancelTbl(poentryId,poNumber,pendingEmpId,"A",passwdForMail); // this is for sumadhura_cancel_po Perminently cancelled
					// this is for approval detals save in Approval details table an dsend mail to previous and next person
					objPurchaseDeptIndentrocess.sendMailPerminentPoCancel(request,pendingEmpId,poEntryId,poNumber,vendorId,siteId,poDate,siteName,passwdForMail,user_id);
					previousEmpId=objPurchaseDepartmentIndentProcessDao.previousEmpId(poEntryId);
					objPurchaseDeptIndentrocess.updateCancelPoDetailsInCancelTbl(poEntryId,pendingEmpId,"A",passwdForMail,false);
					objPurchaseDeptIndentrocess.sendPreviousApproveEmpMail(poEntryId,poNumber,poDate,siteName,previousEmpId,vendorId,user_id);
					objPurchaseDeptIndentrocess.saveCancelPoApproveDetails(poEntryId,siteId,user_id,"A",vendorComment,normalComment,ccmails);
					model.addAttribute("message","Cancel PO is sent for approvals.");
					viewToSelected="response";
				}
				//transactionManager.commit(status);
			}catch(Exception e){
				//transactionManager.rollback(status);
			}
		return viewToSelected;
		}
		
		// to start display the cancel po's for all purchase and marketing po's
		
		
		@RequestMapping(value = "/getCancelPermanentPo.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String inwardsFromPO(HttpServletRequest request, HttpSession session,Model model){

			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			String marketingId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
			String view = "";
			if(site_id.equals(marketingId)){
				model.addAttribute("listOfPOs",irs.getListOfActiveMarketingPOs(site_id,"CNL"));
				model.addAttribute("isMarketing",true);
				view = "CancelPerminentPOShow";
			}else{
				model.addAttribute("listOfPOs",objPurchaseDeptIndentrocess.getListOfCancelPoShow(site_id));
				model.addAttribute("isPurchase",true);
				view = "CancelPerminentPOShow";
			}
			SaveAuditLogDetails.auditLog("0",user_id,"Cancel Permanent PO show","success",site_id);
			return view;
	}	
		
		// jsp pages are creating separate so write this one why beacuse normal po jsp not using in cancelPermanent Po
		@RequestMapping(value = "/PrintCancelPOData.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String PrintCancelPOData(HttpServletRequest request, HttpSession session,Model model){
			String viewToBeSelected="";
			String site_Id=request.getParameter("siteId");
			String poEntryId=request.getParameter("poEntryId");
			String marketingId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
			IndentCreationController.getPoDetailsList(request,session,model);
			request.setAttribute("cancelPOinternalComments",objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoInternalComments(poEntryId)); // get comments of created po time given that
			request.setAttribute("cancelPOVendorComments",objPurchaseDepartmentIndentProcessDao.getCancelPerminentPoComments(poEntryId));
			model.addAttribute("isCancelPo",false);
			model.addAttribute("Comments",true);
			model.addAttribute("poType","");
			if(site_Id.equals(marketingId)){
				viewToBeSelected="marketing/Marketing_POCancelPrintPage";
				
			}else{
				viewToBeSelected="PurchasePermanentPOSuccessPage";
			}
			
			return viewToBeSelected;
		}

		/*******************************************only view permanent cancel po show*********************************************************/
		@RequestMapping(value = "/ViewCancelPoStatus.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public ModelAndView ViewCancelPoStatus(HttpServletRequest request, HttpSession session){
				String site_id = "";
				String toDate = "";
				String fromDate = "";
				String response="";
				String PONumber="";
				ModelAndView model = null;
				String user_id="";
				String viewToSelected="";
				List<IndentCreationBean> indentgetData = null;//ModelAndView
				//String purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();	
				//String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
				String typePO="CANCEL PO";
				try {
					model = new ModelAndView();
					fromDate = request.getParameter("fromDate");
					toDate = request.getParameter("toDate");
					PONumber = request.getParameter("tempPoNumber"); // this is permanent po but this page using in two places
					if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(PONumber)) {
						session = request.getSession(false);
						site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
						user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
					//	System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
						if (StringUtils.isNotBlank(site_id)) {
							//if(site_id.equals(marketing_Dept_Id)){typePO="MARKETING_PO";}
							indentgetData = objPurchaseDeptIndentrocess.ViewandGetCancelPo(fromDate,toDate,PONumber);
							if(indentgetData != null && indentgetData.size() >0){
								request.setAttribute("showGrid", "true");
								request.setAttribute("AllPOs",typePO);
								response="success";
							} else {
								model.addObject("succMessage","The above Dates Data Not Available");
								response="failure";

							}
							model.addObject("indentgetData",indentgetData);
							request.setAttribute("PendingPoDetails",indentgetData);
							model.addObject("fromDate",fromDate);
							model.addObject("toDate", toDate);
							model.addObject("tempPONumber", PONumber);
							
							model.setViewName("ViewCancelPoPage_Dates");

						} else {
							model.addObject("Message","Session Expired, Please Login Again");
							model.setViewName("index");
							response="failure";
							return model;
						}
					} else {
						model.addObject("displayErrMsg", "Please Select From Date or To Date or Po Number!");
						model.addObject("indentgetData",indentgetData);
						request.setAttribute("PendingPoDetails",indentgetData);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						//model.addObject("AllPOs",typePO);
						model.addObject("tempPONumber",PONumber);
						response="success";
						model.setViewName("ViewCancelPoPage_Dates");
					}
				} catch (Exception ex) {
					response="failure";
					ex.printStackTrace();
				} 
				SaveAuditLogDetails.auditLog("0",user_id,"View Cancel PO status","success",site_id);
				
				return model;
			}

		/******************************************************to get the images and pdf for show purpose **************************************************/
		public static void loadPoImgAndPdfFiles(String rootFilePath,String po_Number,String type,Model model, HttpServletRequest request){
			try{
			int imageCount = 0;
			int pdfCount=0;
		
			DataInputStream dis=null;
		
			int getLocalPort=request.getLocalPort();
			String strContextAndPort = "";
			String path = "";
			if(getLocalPort == 8087){ //Local
				strContextAndPort = UIProperties.validateParams.getProperty("PURCHASE_PDFIMG_PATH_UAT");
			}else if(getLocalPort == 8078){ //local machine
				strContextAndPort = UIProperties.validateParams.getProperty("PURCHASE_PDFIMG_PATH_UAT");
			}else if(getLocalPort == 8079){ //CUG
				strContextAndPort = UIProperties.validateParams.getProperty("PURCHASE_PDFIMG_PATH_CUG");
			}else if(getLocalPort == 80){ //LIVE
				strContextAndPort = UIProperties.validateParams.getProperty("PURCHASE_PDFIMG_PATH_LIVE");
			}

			po_Number = po_Number.replace("/","$"); // if the invoice number contain the '/' then it will replacxe with $$
			
			for(int i=0;i<8;i++){
				File dir = new File(rootFilePath+type+"/"+po_Number);
				
				File imageFile = new File(rootFilePath+po_Number+"_Part"+i+".jpg");
				//String pdfFileName=rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf";
				File pdfFile = new File(rootFilePath+po_Number+"_Part"+i+".pdf");
				
				//if pdf is exists the pdf will load and converted into the Base64 object and it will add in model object so we can carry this images and pdf so we can show in view page
				if(pdfFile.exists()){
					pdfCount++;
					try {
						String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "@@"); // here in jsp delete function call time // not taken so replace to @@
						path=strContextAndPort+type+"/"+po_Number+"_Part"+i+".pdf";
						//adding pdf base64 format into model attribute so we can show the image in view and can download it
						model.addAttribute("pdf" + i, path);
						//adding the path of the pdf so if user deleting the pdf by path they can delete
						model.addAttribute("PathdeletePdf" + i, pathForDeleteFile);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				
				}
			
				if(imageFile.exists()){
					imageCount++;
					try {
						String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "@@");
						path=strContextAndPort+type+"/"+po_Number+"_Part"+i+".jpg";
						model.addAttribute("image" + i, path);
						//adding the path of the image so if user deleting the image or pdf by path they can delete
						model.addAttribute("delete" + i, pathForDeleteFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
			//adding the image count in request object so we can control the images update file type in view while updating temporary or permanent work order
			request.setAttribute("imagecount", imageCount);	
			request.setAttribute("pdfcount", pdfCount);	
			System.out.print("images count"+imageCount);
			System.out.print("pdf count"+pdfCount);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/************************************************************this is used to delete and add the images and pdf start**********************************/
	
		/*=====================================================update po start=================================================================*/
		/*==================================================UPDATE PO APPROVAL START============================================================*/
		
		@RequestMapping(value = "/UpdatedTempPoApproval.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String UpdatedTempPoApproval(Model model, HttpServletRequest request,HttpSession session) {
			List<IndentCreationBean> indentgetData = null;
			String sessionSite_id ="";
			String user_id = "";
			String response = "";
			String ponumber="";
			String siteId="";
			String strMessage = "";
			String fromDate="";
			String toDate="";	
			String viewToSelected="";
			String tempPoNumber="";
			String checkActiveOrNot="";
			String pendingEmpId="";
			try {
				
				ponumber=request.getParameter("strPONumber")==null ? "" : request.getParameter("strPONumber");
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				String checkMail=request.getParameter("mail")==null ? "" : request.getParameter("mail");
				if(user_id.equals("")){
					user_id=request.getParameter("userId")==null ? "" : request.getParameter("userId");}
				if(ponumber!=null && !ponumber.equals("")){
					checkActiveOrNot=objPurchaseDepartmentIndentProcessDao.checkApproveStatus(ponumber);
					pendingEmpId=objPurchaseDepartmentIndentProcessDao.checkApprovePendingEmp(ponumber,true);
					}
					/*if(!checkActiveOrNot.equals("A") && (!checkMail.equalsIgnoreCase("true")) || ponumber==null || ponumber.equals("")){
						model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
						return "response";
					}*/
				siteId=request.getParameter("siteId");
				ProductDetails objProductDetails = new ProductDetails();
				String passwdForMail=request.getParameter("password")== null ? "" : request.getParameter("password").toString();
				String userID=session.getAttribute("userID")== null ? "" : session.getAttribute("userID").toString();
				//String old_Po_Number=request.getParameter("oldPoNumber") == null ? "#" : request.getParameter("oldPoNumber");
				fromDate=request.getParameter("fromdate")==null ? "" : request.getParameter("fromdate");
				toDate=request.getParameter("toDate")==null ? "" :request.getParameter("toDate");
				
				
				session = request.getSession(true);
				response=objPurchaseDeptIndentrocess.getAndCheckApprovalPOBOQQuantity(session,request);
				sessionSite_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				
				if((user_id==null || user_id.equals("")) && (passwdForMail!=null && !passwdForMail.equals("") && !response.contains("BOQ"))){
					
					user_id=request.getParameter("userId");
					
					String dbPasswd=objPurchaseDeptIndentrocess.getTempPOPassword(ponumber);
					if(passwdForMail.equals(dbPasswd)){
						/*if(userID.equals(user_id)){
							model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
							return viewToSelected="response";
						}*/
						model.addAttribute("CreatePOModelForm", objProductDetails);
						request.setAttribute("sessionSite_id",sessionSite_id);
						response=objPurchaseDeptIndentrocess.SaveUpdatePoApproveDetails(ponumber,siteId,user_id,request,"false");
						model.addAttribute("url","ViewPoPendingforApproval.spring");
						// permPoNumber=request.getAttribute("permentPoNumber").toString();
						strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
						if(response=="success"){

							model.addAttribute("response",strMessage);
							viewToSelected="response";
						}
						else{
							model.addAttribute("response1",strMessage);
							viewToSelected="response";
						}

						
					}else{
						model.addAttribute("response1","Already Approved or Rejected");
						viewToSelected="response";
					}
					
				}else if(!response.contains("BOQ")){
				if(user_id.equals("")){
				user_id=request.getParameter("userId") == null ? "" : request.getParameter("userId").toString();}
				if(user_id.equals("")){
					model.addAttribute("message1","Oops !!! There was a improper request found.");
					return viewToSelected="response";
				}
				
				model.addAttribute("CreatePOModelForm", objProductDetails);
				request.setAttribute("sessionSite_id",sessionSite_id);
				boolean empSameOrNot=objPurchaseDepartmentIndentProcessDao.checkSameEmpOrNot(user_id,ponumber);
				if(empSameOrNot){
				response=objPurchaseDeptIndentrocess.SaveUpdatePoApproveDetails(ponumber,siteId,user_id,request,"false");}
				model.addAttribute("url","ViewPoPendingforApproval.spring");
				// permPoNumber=request.getAttribute("permentPoNumber").toString();
				strMessage = request.getAttribute("result") == null ? "" : request.getAttribute("result").toString();
				if(fromDate.equalsIgnoreCase("null") && toDate.equalsIgnoreCase("null") || fromDate.equals("") && toDate.equals("")){//tempPoNumber=ponumber;
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
				request.setAttribute("tempPoNumber",ponumber);
									//model.setViewName("PendingPoApproval");
				
				if(response=="success"){

					request.setAttribute("message",strMessage);
					viewToSelected="PendingPoApproval";

				}
				else{
					model.addAttribute("response1",strMessage);
					viewToSelected="response";
					if(!empSameOrNot){
						request.setAttribute("message","");
						model.addAttribute("message1","Already Approved or Rejected or Cancelled");
						viewToSelected="PendingPoApproval";
					}
					

				}


			}
			else if(response.contains("BOQ")){
				model.addAttribute("message1",response);
				return viewToSelected="response";
			}
				
				session.setAttribute("userID",user_id);
			}	
				catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			SaveAuditLogDetails.auditLog("0",user_id,"save Update PO Approval ",response,String.valueOf(sessionSite_id));
			return viewToSelected;
		}
		
		/*========================================== Material BOQ Quantity Check Start ==============================================*/
		/*========================================== material boq start ==============================================================*/
		@RequestMapping(value = "/getAndCheckPOBOQQuantity", method ={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		public String getAndCheckPOBOQQuantity(HttpServletRequest request,HttpSession session,boolean approveOrNot,boolean isCancelled) {
			String response="";
			String site_id=request.getParameter("siteId")== null ? "" : request.getParameter("siteId"); // check the boq quantity purpose written this one
			String indentAllowSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			//List<String> List=Arrays.asList(indentAllowSites.split(","));
			if(!indentAllowSites.contains(site_id)){
			response=objPurchaseDeptIndentrocess.getAndCheckPOBOQQuantity(session,request,site_id,approveOrNot,isCancelled);
				
			}
			return response;//ics.loadSubProds(mainProductId);
		}
		
		/*================================================ getting the BOQ data for check the ajax call start=====================================*/
		@RequestMapping(value = "/gettingPOBoqQuantityAjax", method = RequestMethod.POST)
		@ResponseBody
		public String gettingBoqQuantityAjax(@RequestParam("childProductId") String childProductId,@RequestParam("groupId") String groupId,@RequestParam("siteId") String siteId) {
			return objPurchaseDeptIndentrocess.gettingPOBoqQuantityAjax(groupId,siteId);
		}
		/*================================================ getting the BOQ data for check the ajax call end=====================================*/
		
		
		
}
