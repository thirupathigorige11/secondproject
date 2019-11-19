package com.sumadhura.in;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.MenuDetails;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.MarketingDepartmentService;
import com.sumadhura.service.PurchaseDepartmentIndentrocessService;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.transdao.UtilDao;

import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.service.IndentCreationApprovalEmailFunction;
import com.sumadhura.service.IndentCreationService;
import com.sumadhura.util.CommonUtilities;

@Controller
public class IndentCreationController extends UIProperties{

	@Autowired
	@Qualifier("posClass")
	IndentCreationService ics;

	@Autowired
	private UtilDao utilDao;
	
	@Autowired
	@Qualifier("MarketingDepartmentService")
	MarketingDepartmentService objMarketingDeptService;
	

	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;

	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService objIndentReceiveService;

	@Autowired
	@Qualifier("purchaseDeptIndentrocess")
	PurchaseDepartmentIndentrocessService purchaseDeptIndentrocess;
	
	@Autowired
	IndentCreationApprovalEmailFunction IndentCreationApproveMail;

	/************************************************** PO***************************/



	/*	***********************END PO**3.
	 * 
	 * *********************/
	/*	********************************** Get PO Dateials****************************/


	@RequestMapping(value = "/indentCreation.spring", method = RequestMethod.GET)
	public String indentCreation(Model model, HttpServletRequest request,HttpSession session) {
		IndentCreationBean icb = new IndentCreationBean();
		//IndentCreationBean icb1 = new IndentCreationBean();

		int SiteId = 0;
		String user_id = "";
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		try{
			session = request.getSession(true);
			SiteId = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			model.addAttribute("indentCreationModelForm", icb);
			model.addAttribute("productsMap", iis.loadProds(String.valueOf(SiteId)));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("siteId",SiteId);
			model.addAttribute("Allsites",allSites);
			//model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
			icb = ics.getIndentFromAndToDetails(user_id,icb,SiteId);
			if(icb.getApproverEmpId()==null){
				model.addAttribute("responseMessage","You Cannot Create Indent");
				return "IndentCreation";
			}
			int siteWiseIndentNo = ics.getMaxOfSiteWiseIndentNumber(SiteId);
			icb.setIndentNumber(ics.getIndentCreationSequenceNumber());
			icb.setSiteWiseIndentNo(siteWiseIndentNo);
			/*icb.setIndentFrom(icb1.getIndentFrom());
		icb.setIndentTo(icb1.getIndentTo());
		icb.setApproverEmpId(icb1.getApproverEmpId());*/
			//iib.setProjectName(iis.getProjectName(session))
		}catch(Exception e){e.printStackTrace();}
		SaveAuditLogDetails.auditLog("0",user_id,"Indent Creation Form View","Success",String.valueOf(SiteId));return "IndentCreation";
	}	
	@RequestMapping(value = "/submitIndentCreation.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public String submitIndentCreation(Model model, HttpServletRequest request,HttpSession session) {
		System.out.println("IndentCreationController.submitIndentCreation()");
		
		IndentCreationBean icb = new IndentCreationBean();
		int site_id = 0;
		String user_id = "";
		String response = "";
		String user_Name="";
		String indentNumber="";
		String strIndentNumber="";
		try{
			session = request.getSession(true);
			indentNumber=request.getParameter("IndentNumber")==null ? "" : request.getParameter("IndentNumber");
			
			strIndentNumber=session.getAttribute("strIndentNumber") == null ? "" : session.getAttribute("strIndentNumber").toString();
			
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			user_Name=session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			if(indentNumber!=null && !indentNumber.equals("")){// if the user press refresh button again then it will check
			if(!indentNumber.equals(strIndentNumber)){
			response=getAndCheckBOQQuantity(request,session,false);
			
			if(!response.contains("BOQ")){ // to check when the quantity of boq is equal or not i.e nt equal then if condition executed
				response = ics.indentCreation(model, request, site_id, user_id,user_Name);
			}
			if(response.equals("success")){

				//System.out.println("sucessfully updated");
				request.setAttribute("message", "Indent Created successfully.");
			}else if(response.contains("BOQ")){
				request.setAttribute("message1",response);
			}
			else{
				//System.out.println("not sucessfully updated");
				request.setAttribute("message1", "Indent Not Created.");

			}//model.addAttribute("responseMessage",response);
			}else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			}
			}else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			}
			session.setAttribute("strIndentNumber",indentNumber);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Indent Created",response,String.valueOf(site_id));
		return "response";

	}
	@RequestMapping(value = "/getIndentCreationDetails.spring", method = RequestMethod.GET)
	public String getIndentCreationDetails(Model model, HttpServletRequest request,HttpSession session) {
		List<IndentCreationDto> pendingIndents = null;
		List<IndentCreationBean> listofCentralIndents = null;
		Map<String, String> siteDetails = null;
		String strSiteId = "";
		String user_id = "";
		String central_dept_id = "";
		String purchase_dept_id = "";
		String pendingEmpId="";
		String pendingDeptId ="";
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			central_dept_id = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
			purchase_dept_id = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();

			siteDetails = new IndentSummaryDao().getSiteDetails();
			
			pendingEmpId = IndentCreationApproveMail.getPendingEmployeeId(user_id,Integer.parseInt(strSiteId));
			// pendingDeptId = IndentCreationApproveMail.getPendingDeptId(user_id);
 			model.addAttribute("siteDetails", siteDetails);


		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if(strSiteId.equals(central_dept_id) && (pendingEmpId.equals("") || pendingEmpId.equals("-")))*/
		// because of above conditions central indents also displaying in send to purchase dept screen without approvals.
		if(strSiteId.equals(central_dept_id) )
		{
			try {
				listofCentralIndents = ics.getAllCentralIndents();
				model.addAttribute("listofCentralIndents",listofCentralIndents);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaveAuditLogDetails.auditLog("0",user_id,"Getting List of All Pending Central Indents","Success",strSiteId);
			return "ViewAllCentralIndents";
		}

		else if(strSiteId.equals(purchase_dept_id) && (pendingEmpId.equals("") || pendingEmpId.equals("-")) )
		{
			try {
				listofCentralIndents = purchaseDeptIndentrocess.getAllPurchaseIndents();
				model.addAttribute("listofCentralIndents",listofCentralIndents);
				request.setAttribute("totalProductsList",purchaseDeptIndentrocess.getAllProducts());
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaveAuditLogDetails.auditLog("0",user_id,"Getting List of All Pending Purchase Indents","Success",strSiteId);
			return "PendingIndents";
		}
		else
		{
			try {
				pendingIndents = ics.getPendingIndents(user_id,strSiteId);
				model.addAttribute("pendingIndents",pendingIndents);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaveAuditLogDetails.auditLog("0",user_id,"getting List of All Pending Indents For Approval","Success",strSiteId);
			return "getIndentCreationDetails";
		}

	}
	/*new*/
	@RequestMapping(value = "/IndentCreationDetailsShow.spring", method = RequestMethod.GET)
	public String IndentCreationDetailsShow(Model model, HttpServletRequest request,HttpSession session) {
System.out.println("IndentCreationController.IndentCreationDetailsShow()");
		IndentCreationBean icb = new IndentCreationBean();
		int indentNumber = 0;
		int siteWiseIndentNumber = 0;
		String user_id = "";
		int site_Id = 0;
		String strSiteId = "";
		// checking whether indentNumber is valid or not
		boolean isValid = false;
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		try {
			session = request.getSession(true);
			siteWiseIndentNumber = Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			site_Id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			model.addAttribute("Allsites",allSites);
			indentNumber = ics.getIndentNumber(siteWiseIndentNumber,site_Id);
			isValid = ics.checkIndentNumberIsValidForEmployee(indentNumber, user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!isValid){
			try {
				model.addAttribute("errorMessage", "Indent Number is Not Valid");
				List<IndentCreationDto> pendingIndents = null;
				pendingIndents = ics.getPendingIndents(user_id,String.valueOf(site_Id));
				model.addAttribute("pendingIndents",pendingIndents);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "getIndentCreationDetails";
		}
		try {
			model.addAttribute("indentCreationModelForm", icb);
			model.addAttribute("productsMap", iis.loadProds(String.valueOf(site_Id)));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("blocksMap", iis.loadBlockDetails(String.valueOf(site_Id)));
			model.addAttribute("Allsites",allSites);
			List<IndentCreationBean> list = ics.getIndentCreationLists(indentNumber);
			ics.getIndentFromDetails(indentNumber,list);
			ics.getIndentToDetails(strSiteId,user_id,list);
			
			List<IndentCreationBean> editList = new ArrayList<IndentCreationBean>();
			String strEditComments = "";
			for(int i =0 ;i< list.size();i++ ){
				
				IndentCreationBean objIndentCreationBean = list.get(i);
				model.addAttribute("materialEditComment", objIndentCreationBean.getMaterialEditComment());
				strEditComments = objIndentCreationBean.getMaterialEditComment();
				
				if(strEditComments.contains("@@@")){
					String strEditCommentsArr [] = strEditComments.split("@@@");
					for(int j = 0; j< strEditCommentsArr.length;j++){
						IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
						objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
						editList.add(objCommentIndentCreationBean);
					}
					
					model.addAttribute("materialEditCommentList", editList);
				}
				
			}
			
			String strPurpose = ics.getIndentLevelComments(indentNumber);
			
			model.addAttribute("IndentLevelCommentsList",strPurpose);
			model.addAttribute("IndentCreationList",list);
			model.addAttribute("IndentCreationDetailsList",ics.getIndentCreationDetailsLists(indentNumber));
			model.addAttribute("deletedProductDetailsList",ics.getDeletedProductDetailsLists(indentNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Showing Indent Details for Approval","Success",String.valueOf(site_Id));
		return "IndentCreationDetailsShow";
	}
	@RequestMapping(value = "/approveIndentCreation.spring", method = RequestMethod.POST)
	public String approveIndentCreation(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String result="";
		String response = "";
		String strSiteId="";
		String indentNumber="";
		String viewToselected="";
		
		try {
			session = request.getSession(true);
			indentNumber=request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber");
			
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			strSiteId=request.getParameter("siteId")== null ? "" : request.getParameter("siteId"); // check the boq quantity purpose written this one
			
			
			if(indentNumber!=null && !indentNumber.equals("")){
			boolean status=ics.checkIndentNumberIsValidForEmployee(Integer.valueOf(indentNumber),user_id);
			if(status){
			response=getAndCheckBOQQuantity(request,session,true);
				
			if(!response.contains("BOQ")){ 
			response = ics.approveIndentCreation(model, request, site_id, user_id,session);
			}
			if(response=="Indent Approved Successfully"){
				model.addAttribute("responseMessage",response);
				result="success";
			}else if(response.contains("BOQ")){
				model.addAttribute("responseMessage1",response);
			}
			else{
				model.addAttribute("responseMessage1",response);
			result="failure";
			}
			List<IndentCreationDto> pendingIndents = null;
			pendingIndents = purchaseDeptIndentrocess.getPendingIndents(user_id,String.valueOf(site_id));
			model.addAttribute("pendingIndents",pendingIndents);
			
			viewToselected="getIndentCreationDetails";
			}
			else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				viewToselected="response";
			}
			}else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				viewToselected="response";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Approving Indent",result,String.valueOf(site_id));
		return viewToselected;

	}
	@RequestMapping(value = "/approveIndentCreationFromMail.spring", method = RequestMethod.POST)
	public String approveIndentCreationFromMail(Model model, HttpServletRequest request,HttpSession session) {
		String responseMessage = "";
		int site_id = 0;
		String user_id = "";  
		String response = "";
		String user_name="";
		try {
			site_id = Integer.parseInt(request.getParameter("siteId"));
			user_id = request.getParameter("userId"); 
			user_name=session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//int indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			response = ics.approveIndentCreationFromMail( request, site_id, user_id,user_name);
			if(response.equals("WrongPassword"))
			{
				responseMessage = "Already Approved";
			}
			else if(response.equals("Success"))
			{
				responseMessage = "Indent Approved Successfully";
			}
			else{
				responseMessage = "Failed. Indent NOT Approved";
			}
		} catch (Exception e) {
			responseMessage = "Failed. Indent NOT Approved";
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Approving Indent via Mail",response,String.valueOf(site_id));
		List<MenuDetails> list = new ArrayList<MenuDetails>();
		session.setAttribute("menu", list);
		request.setAttribute("message",responseMessage);
		return "response";
	}
	@RequestMapping(value = "/rejectIndentCreationFromMail.spring", method = RequestMethod.POST)
	public String rejectIndentCreationFromMail(Model model, HttpServletRequest request,HttpSession session) {
		String responseMessage = ""; 
		int site_id = 0;
		String user_id = "";  
		String response = "";
		try {
			site_id = Integer.parseInt(request.getParameter("siteId"));
			user_id = request.getParameter("userId");  
			response = ics.rejectIndentCreationFromMail(model,request,site_id, user_id);
			if(response.equals("WrongPassword"))
			{
				responseMessage = "Already Rejected";
			}
			else if(response.equals("Success"))
			{
				responseMessage = "Indent Rejected Successfully";
			}
			else{
				responseMessage = "Failed. Indent NOT Rejected";
			}
		} catch (Exception e) {
			responseMessage = "Failed. Indent NOT Rejected";
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Rejecting Indent via Mail",response,String.valueOf(site_id));
		List<MenuDetails> list = new ArrayList<MenuDetails>();
		session.setAttribute("menu", list);
		request.setAttribute("message",responseMessage);
		return "response";
	}
	@RequestMapping(value = "/rejectIndentCreation.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String rejectIndentCreation(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		String strSiteId="";
		String strReturn="";
		String site_Name="";
		String indentNumber="";
		String siteWiseIndentNo="";
		String reqSiteName = "";
		String indentName="";
		try {
			
			indentNumber= request.getParameter("indentNumber");
			siteWiseIndentNo=request.getParameter("siteWiseIndentNo");
			reqSiteName = request.getParameter("siteName");
			indentName=request.getParameter("indentName");
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			boolean status=ics.checkIndentNumberIsValidForEmployee(Integer.valueOf(indentNumber),user_id);
			if(indentNumber==null&&indentName==null&&reqSiteName==null&&siteWiseIndentNo==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				if(strSiteId.equals("999")){
					model.addAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
				}
				return "response";
			}
			if(indentNumber!=null){
			//boolean status=ics.checkIndentNumberIsValidForEmployee(Integer.valueOf(indentNumber),user_id);
			if(status){
				
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			
			site_Name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			strSiteId=request.getParameter("strSiteId")== null ? "" : request.getParameter("strSiteId").toString();
			String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			response = ics.rejectIndentCreation(model, request, site_id, user_id,user_name,site_Name);
			
			if(response.equals("Success")){
				model.addAttribute("responseMessage","Indent Rejected Successfully");
				model.addAttribute("response","Indent Rejected Successfully");
			}else{
				model.addAttribute("responseMessage","Failed Indent Rejection");
				model.addAttribute("response1","Indent Rejected Successfully");
			}
			if(!strSiteId.equals("999")){
			List<IndentCreationDto> pendingIndents = null;
			pendingIndents = purchaseDeptIndentrocess.getPendingIndents(user_id,String.valueOf(site_id));
			model.addAttribute("pendingIndents",pendingIndents);
			strReturn="getIndentCreationDetails";
			}else{
				/*List<IndentCreationBean> listofCentralIndents = null;
				listofCentralIndents = ics.getAllCentralIndents();
				model.addAttribute("listofCentralIndents",listofCentralIndents);*/
				model.addAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
				strReturn="response";
			}
			}else{
				model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				strReturn="response";
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Rejecting Indent",response,String.valueOf(site_id));
		return strReturn; //strReturn is string type it contains JSP page

	}

	/*	***********************END PO************************/
	/* *****************************************************************************/
	/* ********************* NEWLY ADDED CONTROLLERS *******************************/
	/************************************************** CENTRAL ***************************/
	/*@RequestMapping(value = "/centralIndent.spring", method = RequestMethod.GET)
	public String centralIndent(Model model, HttpServletRequest request,HttpSession session) {
		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		List<IndentCreationBean> listofCentralIndents = null;
		/**/
	/*listofCentralIndents = ics.getAllCentralIndents();
		model.addAttribute("listofCentralIndents",listofCentralIndents);
		return "ViewAllCentralIndents";
	}*/




	/*	***************************************************************/
	/************************************************** Purchase Department ***************************/


	/*@RequestMapping(value = "/ViewPurchaseIndents.spring", method = RequestMethod.GET)
	public String purchaseIndent(Model model, HttpServletRequest request,HttpSession session) {
		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		List<IndentCreationBean> listofCentralIndents = null;
		/**/
	/*listofCentralIndents = ics.getAllPurchaseIndents();
		model.addAttribute("listofCentralIndents",listofCentralIndents);

		request.setAttribute("totalProductsList",ics.getAllProducts());

		return "PendingIndents";//ViewAllPurchaseIndents
	}*/



	@RequestMapping("/getSubProductsOfIndents.spring")
	public  @ResponseBody List<ProductDetails> getAllSubProductList(@RequestParam(value = "prodId") String prodId){
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		try{
			if(prodId.contains("@@")){
				String productArr[] = prodId.split("@@");
				if(productArr != null && productArr.length>=1 ){
					prodId = productArr[0].trim();
					//strProductName = productArr[1].trim();
				}
			}
			listAllProductsList = ics.getAllSubProducts(prodId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listAllProductsList;
	}
	@RequestMapping("/getChildProductsOfIndents.spring")
	public  @ResponseBody List<ProductDetails> getAllChildProductList(@RequestParam(value = "subProdId") String strSubProdId, HttpServletResponse response) throws Exception{
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		try{
			if(strSubProdId.contains("@@")){
				String productArr[] = strSubProdId.split("@@");
				if(productArr != null && productArr.length>=1 ){
					strSubProdId = productArr[0].trim();
					//strProductName = productArr[1].trim();
				}
			}
			listAllProductsList = ics.getAllChildProducts(strSubProdId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return listAllProductsList;
	}


	/*	***************************************************************/
	/*@RequestMapping(value = "/subCentralIndent.spring", method = RequestMethod.GET)
	public String subCentralIndent(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds());
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		return "subCentralIndent";
	}*/
	/************************************************** PO***************************/


	/*****************************Create Central Indent****************************/
	/*@RequestMapping(value = "/createCentralIndent.spring", method = RequestMethod.GET)
	public String createCentralIndent(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds());
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		return "CreateCentralIndent";
	}*/
	/**************************************************************************/

	/*@RequestMapping(value = "/CreateIndents.spring", method = RequestMethod.GET)
	public String CreateIndents(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds());
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		return "CreateIndents";
	}*/


	/******************************************************************************/

	/*@RequestMapping(value = "/sendenquiry.spring", method = RequestMethod.GET)
	public String sendenquiry(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds());
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		return "SendEnquiry";
	}*/
	/**********************************************************************************************/
	/*@RequestMapping(value = "/CreatePO_waste.spring", method = RequestMethod.GET)
	public String CreatePO_waste(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();
		String indentNumber= request.getParameter("indentNumber");
		model.addAttribute("indentNumber",indentNumber);

		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds());
	//	model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
	//	model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
	//	iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		return "CreateIndentPO";
	}*/

	/***************************************************************************************/
	/*	***********************END PO************************/

	@RequestMapping(value = "/ViewMyRequest.spring", method = RequestMethod.GET)
	public String ViewMyRequestIndent(Model model, HttpServletRequest request,HttpSession session) {
		String strSiteId = "";
		String user_id = "";
		Map<String, String> siteDetails = null;

		try {
			session = request.getSession(true);
			//for activating Sub Module
			model.addAttribute("urlForActivateSubModule", "ViewMyRequest.spring");
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
			List<IndentCreationBean> listofCentralIndents = null;

			//listofCentralIndents = ics.getViewMyRequestIndents(request);
			//request.getAttribute("allSiteIndents");
			//ACP
			if(siteId.length()!=0){
				listofCentralIndents = ics.getViewMyRequestIndents(request,"siteWiseIndents");
				model.addAttribute("listofSiteWiseCentralIndents",listofCentralIndents);
				model.addAttribute("showRequestData", "SiteWiseIndentsData");
				model.addAttribute("strSiteId", siteId);
			}
			
				listofCentralIndents = ics.getViewMyRequestIndents(request,"AllIndents");
				model.addAttribute("listofCentralIndents",listofCentralIndents);
				//model.addAttribute("showRequestData", "AllIndentsData");
						
			
			siteDetails = new IndentSummaryDao().getSiteDetails();
			model.addAttribute("siteDetails", siteDetails);

			//request.setAttribute("totalProductsList",ics.getAllProducts());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Showing List of All Requests Came","Success",strSiteId);
		return "Viewmyrequest";//ViewAllPurchaseIndents
	}

	@RequestMapping(value = "/ViewIndentissuedDetails.spring", method = RequestMethod.GET)
	public String ViewIndentissuedDetails(Model model, HttpServletRequest request,HttpSession session) {
		IndentCreationBean icb = new IndentCreationBean();
		String strSiteId = "";
		String user_id = "";
		String urlForActivateSubModule="";
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			int siteWiseIndentNo = Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			int reqSiteName = Integer.parseInt(request.getParameter("siteId"));
			urlForActivateSubModule=request.getParameter("url")==null?"":request.getParameter("url");
			model.addAttribute("urlForActivateSubModule", urlForActivateSubModule);
			//System.out.println("*@reqSiteName: "+reqSiteName);
			int indentNumber = ics.getIndentNumber(siteWiseIndentNo,reqSiteName);
			model.addAttribute("indentCreationModelForm", icb);
			List<IndentCreationBean> IndentDetails = null;
			List<IndentCreationBean> IndentDtls = null;
			model.addAttribute("IndentDtls",IndentDtls);
			model.addAttribute("productsMap", iis.loadProds(strSiteId));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));


			DateFormat df = new SimpleDateFormat("dd-MMM-yy");
			String date = df.format(new Date());
			model.addAttribute("IndentEntrySeqNo",objIndentReceiveService.getIndentEntrySequenceNumber());
			model.addAttribute("todayDate",date);
			IndentDtls = ics.getViewAllMyRequestIndents(request,indentNumber);
			model.addAttribute("IndentDtls",IndentDtls);
			IndentDetails = ics.getViewissedIndentDetailsLists(indentNumber,strSiteId);
			model.addAttribute("IndentDetails",IndentDetails);
			String numberOfRows = "";
			if(IndentDetails!=null){
				for(int i=1;i<=IndentDetails.size();i++)
				{
					numberOfRows += i+"|"; 
				}
			}
			model.addAttribute("numberOfRows",numberOfRows);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Showing Request Details","Success",strSiteId);
		return "CreateissueIndent";
	}

	@RequestMapping(value = "/sendissued.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public String sendIssuedToVendor(Model model, HttpServletRequest request,HttpSession session) {
		String site_Name = "";
		String user_id = "";
		String response = "";
		try {
			String indentNumber=  request.getParameter("indentNumber");
			String siteWiseIndentNo=request.getParameter("siteWiseIndentNo");
			String raisedIndetSiteName=request.getParameter("siteName");
			String 	reqSiteId=request.getParameter("Site");
			
			if(indentNumber==null&&siteWiseIndentNo==null&&raisedIndetSiteName==null&&reqSiteId==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				model.addAttribute("urlForActivateSubModule", "ViewMyRequest.spring");
				return "response";
			}
			
			int site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			site_Name = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			response = ics.sendissued(model, request, session,site_id, user_id,site_Name);
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(response.equals("success")){
			request.setAttribute("Message", "Success. sending successfully.");
			return  "DCviewGRN";
		}else{
			request.setAttribute("Message", "Failed. Sending not sucessful.");
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Sending Issued Quantity to Requested Site",response,site_Name);
		return "CreateIssuedResponse";
	}



	@RequestMapping(value = "/Reject.spring", method = RequestMethod.POST)
	public String Reject(Model model, HttpServletRequest request,HttpSession session) {
		/*session = request.getSession(true);
		int site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String indentNumber= request.getParameter("indentNumber");
		model.addAttribute("indentNumber",indentNumber);*/
		int site_id = 0;
		String user_id = "";
		String response ="";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			response = ics.RejectQuantity(model, request, site_id, user_id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		if(response.equals("success")){

			//System.out.println("sucessfully updated");
			request.setAttribute("Mesage", "Success. Rejected successfully.");
		}
		else{
			//System.out.println("not sucessfully updated");
			request.setAttribute("Mesage", "Failed. not Rejected.");

		}


		SaveAuditLogDetails.auditLog("0",user_id,"Rejected to Issue Quantity to Other Site",response,String.valueOf(site_id));
		return "Viewmyrequest";
	}

	@RequestMapping(value = "/ViewMyReceive.spring", method = RequestMethod.GET)
	public String ViewMyReceiveIndent(Model model, HttpServletRequest request,HttpSession session) {
		String strSiteId = "";
		String user_id = "";
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			List<IndentCreationBean> listofCentralIndents = null;

			listofCentralIndents = ics.getViewMyReceiveIndents(request);
			model.addAttribute("listofCentralIndents",listofCentralIndents);

			//	request.setAttribute("totalProductsList",ics.getAllProducts());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Showing List of All Receives Came","Success",strSiteId);
		return "ViewMyReceive";//ViewAllPurchaseIndents
	}
	@RequestMapping(value = "/ViewIndentSenderDetails.spring", method = RequestMethod.GET)
	public String ViewIndentSenderDetails(Model model, HttpServletRequest request,HttpSession session) {
		IndentCreationBean icb = new IndentCreationBean();
		String strSiteId = "";
		String user_id = "";
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			int indentProcessno = Integer.parseInt(request.getParameter("indentNumber"));
			String reqSiteName = request.getParameter("siteName");
			model.addAttribute("indentCreationModelForm", icb);
			List<IndentCreationBean> IndentDetails = null;
			List<IndentCreationBean> IndentDtls = null;
			//model.addAttribute("IndentDtls",IndentDtls);
			//model.addAttribute("productsMap", iis.loadProds());
			//model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			//model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));

			IndentDtls = ics.getViewAllMyRequestedIndent(request,indentProcessno);
			model.addAttribute("IndentDtls",IndentDtls);
			IndentDetails = ics.getViewReceivedIndentDetailsLists(indentProcessno);
			model.addAttribute("IndentDetails",IndentDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}


		SaveAuditLogDetails.auditLog("0",user_id,"Showing Receive Details","Success",strSiteId);
		return "CreateReceiveIndent";
	}

	@RequestMapping(value = "/sendReceived.spring", method = RequestMethod.POST)
	public String senderReceivedQuantity(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			response = ics.sendReceivedQuantity(model, request, site_id, user_id);
			if(response.equals("success")){

				//System.out.println("sucessfully updated");
				request.setAttribute("Message", "Success. sending successfully.");
			}
			else{
				//System.out.println("not sucessfully updated");
				request.setAttribute("Message", "Failed. Sending not sucessful.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Accepted Received Quantity",response,String.valueOf(site_id));
		return "ViewMyReceive";
	}
	@RequestMapping(value = "/RejectQuantity.spring", method = RequestMethod.POST)
	public String RejectQuantity(Model model, HttpServletRequest request,HttpSession session) {

		int site_id = 0;
		String user_id = "";
		String response = "";
		try {
			session = request.getSession(true);
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			response = ics.RejectIssueQuantity(model, request, site_id, user_id);

			if(response.equals("success")){

				//System.out.println("sucessfully updated");
				request.setAttribute("Mesage", "Success. Rejected successfully.");
			}
			else{
				//System.out.println("not sucessfully updated");
				request.setAttribute("Mesage", "Failed. not Rejected.");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Rejected Received Quantity",response,String.valueOf(site_id));
		return "ViewMyReceive";
	}

	@RequestMapping(value = "/getAllIndentCreationDetails.spring", method = RequestMethod.GET)
	public String getAllIndentCreationDetails(Model model,HttpServletRequest request,HttpSession session) {
		String site = "";

		List<IndentCreationBean> listofCentralIndents = null;
		Map<String, String> siteDetails = null;

		try {


			site=request.getParameter("site");
			//	System.out.println(site);
			
			listofCentralIndents = purchaseDeptIndentrocess.getAllSitePurchaseIndents(site);
			model.addAttribute("url","getIndentCreationDetails.spring");
			if(listofCentralIndents.size()>0){
				model.addAttribute("listofCentralIndents",listofCentralIndents);
			}
			else
				model.addAttribute("message","No Indents are Available");	
			//request.setAttribute("totalProductsList",purchaseDeptIndentrocess.getAllProducts());
			//	request.setAttribute("showGrid", "true");

		}catch(Exception e){
			e.printStackTrace();
		}

		return "ProjectWiseIndent";

	}

	@RequestMapping(value ="ViewMyRaisedIndents.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ViewMyRaisedIndents(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String indentNumber = "";
		String response="";
		ModelAndView model = null;
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			indentNumber=request.getParameter("indentNumber");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(indentNumber)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentIssueData = ics.getRaisedIndentDetails(fromDate, toDate, site_id,indentNumber);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","No Data Available");
						response="failure";

					}
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("indentNumber", indentNumber);
					model.setViewName("ViewAllIndentsDetails");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("indentNumber", indentNumber);
				response="success";
				model.setViewName("ViewAllIndentsDetails");
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

	@RequestMapping(value = "/ViewMyRaisedIndentsDetails.spring", method = RequestMethod.GET)
	public String ViewMyRaisedIndentsDetails(Model model, HttpServletRequest request,HttpSession session) {
		IndentCreationBean icb = new IndentCreationBean();
		int indentNumber = 0;
		String user_id = "";
		int site_Id = 0;
		String siteId="";
		int strAuditlogSiteId = 0;
		// checking whether indentNumber is valid or not
		boolean isValid = false;
		try {
			session = request.getSession(true);
			site_Id=Integer.parseInt(request.getParameter("siteId"));
			indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			strAuditlogSiteId = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		//	user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			model.addAttribute("indentCreationModelForm", icb);
			model.addAttribute("productsMap", iis.loadProds(String.valueOf(site_Id)));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("blocksMap", iis.loadBlockDetails(String.valueOf(site_Id)));
			List<IndentCreationBean> list = ics.getIndentCreationLists(indentNumber);
			List<IndentCreationBean> editList = new ArrayList<IndentCreationBean>();
			String strEditComments = "";
			for(int i =0 ;i< list.size();i++ ){
				
				IndentCreationBean objIndentCreationBean = list.get(i);
				model.addAttribute("materialEditComment", objIndentCreationBean.getMaterialEditComment());
				strEditComments = objIndentCreationBean.getMaterialEditComment();
				
				if(strEditComments.contains("@@@")){
					String strEditCommentsArr [] = strEditComments.split("@@@");
					for(int j = 0; j< strEditCommentsArr.length;j++){
						IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
						objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
						editList.add(objCommentIndentCreationBean);
					}
					
					model.addAttribute("materialEditCommentList", editList);
				}
			
			}
			
			String strPurpose = ics.getIndentLevelComments(indentNumber);
			request.setAttribute("print","true");
			model.addAttribute("IndentLevelCommentsList",strPurpose);
			model.addAttribute("print","true");
			model.addAttribute("IndentGetList",list);
			model.addAttribute("IndentCreationDetailsList",ics.getIndentCreationDetailsLists(indentNumber));
			
			model.addAttribute("deletedProductDetailsList",ics.getDeletedProductDetailsLists(indentNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails.auditLog("0",user_id,"Showing Indent Details for Approval","Success",String.valueOf(strAuditlogSiteId));
		return "getIndentDetails";
	}

	/**
	 * @description this method is for getting site wise printing the PO details
	 * @param request
	 * @param session
	 */
	@RequestMapping(value = "/getSiteWisePoDetails.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getSiteWisePoDetails(HttpServletRequest request, HttpSession session) {
		System.out.println("IndentCreationController.getSiteWisePoDetails()");
		ModelAndView model = null;
		try {
			model = new ModelAndView();
			String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
			model.addObject("POSite_id", site_id);
			model.addObject("sitePO",true);
			model.addObject("DeptPo",false);
			model.addObject("PurchasePO","View My PO's");
			
			model.setViewName("ViewPoDetails");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	@RequestMapping(value ="/ViewAllPo.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ViewAllPo(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		String poNumber="";
		ModelAndView model = null;
		List<IndentCreationBean> indentgetData = null;
		String vendorName="";
		String siteIds="";
		
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate")==null ? "" : request.getParameter("fromDate").trim();
			toDate = request.getParameter("toDate")==null ? "" :request.getParameter("toDate").trim();
			poNumber = request.getParameter("poNumber")==null ? "" : request.getParameter("poNumber").trim();
			String siteIForPO=request.getParameter("POSite_id")==null ? "" : request.getParameter("POSite_id");
			String sessionId=(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			vendorName=request.getParameter("VendorId")==null ? "" : request.getParameter("VendorId").trim();
			 siteIds=request.getParameter("siteIds")==null ? "" : request.getParameter("siteIds");
			
			if(siteIds!=""){ // here user select no of sites then it come as comma added to it then it will use in query like in so use this to add quatation here
				siteIds=siteIds.replace(",","','");
			}
			Map<String, String> siteDetails = new IndentSummaryDao().getSiteDetails(); // getting the all active site details 
			request.setAttribute("siteDetails",siteDetails);
			model.addObject("PurchasePO","View All Po's");
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(poNumber) ||  StringUtils.isNotBlank(vendorName) ||  StringUtils.isNotBlank(siteIds)) {
				session = request.getSession(false);
				site_id = siteIForPO == null ? "" :siteIForPO.toString();
					System.out.println("Site is is "+site_id+" ..");
					if (StringUtils.isNotBlank(site_id)) {
						model.addObject("POSite_id", site_id);	
					}
					indentgetData = ics.getPoDetails(fromDate, toDate, site_id,poNumber,sessionId,true,siteIds,vendorName);
					if(indentgetData != null && indentgetData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failure";

					}
					model.addObject("indentgetData",indentgetData);
					request.setAttribute("PODetails",indentgetData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("poNumber", poNumber);
					model.addObject("allPoOrNot", true);
					model.setViewName("ViewPoDetails");

				/*} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}*/
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or PO Number!");
				model.addObject("indentgetData",indentgetData);
				request.setAttribute("PODetails",indentgetData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("allPoOrNot", true);
				model.addObject("poNumber", poNumber);
				response="success";
				model.setViewName("ViewPoDetails");
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
	/*================================================this is for all pos show for site login start===========================================*/
	@RequestMapping(value ="/getPoDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getPoDetails(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		String poNumber="";
		ModelAndView model = null;
		List<IndentCreationBean> indentgetData = null;
		String vendorName="";
		String purchaseDeptId="";
		String marketingDeptId="";
		Map<String, String> strSiteDetails=new HashMap<String,String>();
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate")==null ? "" : request.getParameter("fromDate").trim();
			toDate = request.getParameter("toDate")==null ? "" :request.getParameter("toDate").trim();
			poNumber = request.getParameter("poNumber")==null ? "" : request.getParameter("poNumber").trim();
			String siteIForPO=request.getParameter("POSite_id")==null ? "" : request.getParameter("POSite_id");
			String sessionId=(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			vendorName=request.getParameter("VendorId")==null ? "" : request.getParameter("VendorId").trim();
			String siteIds=request.getParameter("siteIds")==null ? "" : request.getParameter("siteIds");
			purchaseDeptId=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString(); 
			marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString(); 
			
			if(siteIds!=""){ // here user select no of sites then it come as comma added to it then it will use in query like in so use this to add quatation here
				siteIds=siteIds.replace(",","','");
			}
			Map<String, String> siteDetails = new IndentSummaryDao().getSiteDetails(); // getting the all active site details 
			for (String name : siteDetails.keySet())  {
				if(purchaseDeptId.equals(sessionId) && !name.equals(marketingDeptId)){
					model.addObject("PurchasePO",true);
					
					strSiteDetails.put(name, siteDetails.get(name));
				}else if(marketingDeptId.equals(sessionId) && name.equals(marketingDeptId)){
					
					strSiteDetails.put(name, siteDetails.get(name));
				}
				
	           // System.out.println("key: " + name); 
			}
			if(sessionId.equals(purchaseDeptId) || sessionId.equals(marketingDeptId) ){
				model.addObject("PurchasePO","Print PO");
				
			}else{
				model.addObject("PurchasePO","View My PO's");
				
			}
			request.setAttribute("siteDetails",strSiteDetails);
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(poNumber) || StringUtils.isNotBlank(vendorName) ||  StringUtils.isNotBlank(siteIds)) {
				session = request.getSession(false);
				//site_id = request.getParameter("dropdown_SiteId") == null ? "": request.getParameter("dropdown_SiteId");
					site_id = siteIForPO == null ? "" :siteIForPO.toString();
					System.out.println("Site is is "+site_id+" ..");
					if (StringUtils.isNotBlank(site_id)) {
						model.addObject("POSite_id", site_id);	
					}
					indentgetData = ics.getPoDetails(fromDate, toDate, site_id,poNumber,sessionId,false,siteIds,vendorName);
					if(indentgetData != null && indentgetData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","Data Not Available");
						response="failure";

					}
					model.addObject("indentgetData",indentgetData);
					request.setAttribute("PODetails",indentgetData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("poNumber", poNumber);
					model.addObject("sitePO",true);
					if(purchaseDeptId.equals(sessionId) || marketingDeptId.equals(sessionId)){
					model.addObject("DeptPo",true);}
					model.setViewName("ViewPoDetails");

				
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or PO Number or vendorName or Site!");
				model.addObject("indentgetData",indentgetData);
				request.setAttribute("PODetails",indentgetData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("sitePO",true);
				if(purchaseDeptId.equals(sessionId) || marketingDeptId.equals(sessionId)){
				model.addObject("DeptPo",true);}
				model.addObject("poNumber", poNumber);
				response="success";
				model.setViewName("ViewPoDetails");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 

		
		return model;
	}
	
	
	/*=================================================all pos show for site login end========================================================*/

	@RequestMapping(value = "/getPoDetailsList.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getPoDetailsList(HttpServletRequest request, HttpSession session,Model model){

		String poNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String versionNo="";
		String refferenceNo="";
		String strPoPrintRefdate = "";
		boolean imageClick = false;
		
		String vendorId="";
		Calendar cal = Calendar.getInstance();
		String site_Id="";
		int strCount=0;
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
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber) && !site_Id.equals("996")){




				/*int currentYear = Calendar.getInstance().get(Calendar.YEAR);
				int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;*/
				
				/*if(currentMonth <=3){
					strFinacialYear = currentYear-1+"_"+currentYear;
				}else{
					strFinacialYear = currentYear+"_"+currentYear+1;
				}*/
			//	System.out.println(strFinacialYear);
				
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=ics.getPoDetailsList(poNumber, site_Id,request);

				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				poentryId = request.getAttribute("poentryId").toString();
				request.setAttribute("PoLevelComments",ics.getPoLevelComments(poentryId,"true"));
				model.addAttribute("site_idForPriceMaster", site_Id);
			

				model.addAttribute("versionNo",versionNo);
			//	DateFormat newDate = new SimpleDateFormat("MM/dd/yyyy");
				//System.out.println("the current year "+newDate.format(cal));
				model.addAttribute("refferenceNo",refferenceNo);
				model.addAttribute("strPoPrintRefdate",strPoPrintRefdate);
				request.setAttribute("poPage","true");
				model.addAttribute("poNumber",poNumber);
				/***********************************for pdf purpose****************************************************/
				
				//String rootFilePath = validateParams.getProperty("UPLOAD_PDF") == null ? "" : validateParams.getProperty("UPLOAD_PDF").toString(); 
				String filepath=rootFilePath+"PO//";
				PurchaseDepartmentIndentrocessController.loadPoImgAndPdfFiles(filepath,poNumber,"PO",model,request); // call the getting pdf and images for showing purpose
				
			/**************************************************for pdf purpose end********************************************/	
				
				//System.out.println("poNumber "+poNumber);

				if(response.equalsIgnoreCase("Success")) {

					request.setAttribute("imageClick",imageClick);
					viewToBeSelected = "POPrintPage";

				}

			}
			/******************************************************Marketing po detsils to show account dept show**************************************/
			else if(StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(poNumber) && site_Id.equals("996")){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				String response=objMarketingDeptService.getMarketingPoDetailsList(poNumber, site_Id,request);

				versionNo=request.getAttribute("versionNo").toString();
				refferenceNo=request.getAttribute("refferenceNo").toString();
				strPoPrintRefdate=request.getAttribute("strPoPrintRefdate").toString();
				poentryId = request.getAttribute("poentryId").toString();
				model.addAttribute("vendorMail",true);
				request.setAttribute("PoLevelComments",ics.getPoLevelComments(poentryId,"true"));

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
				if(response.equalsIgnoreCase("Success")) {

					request.setAttribute("imageClick",imageClick);
					viewToBeSelected = "marketing/Marketing_POPrintPage";

				}
				
			}
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		} 


		return viewToBeSelected;
	}
	
		@RequestMapping(value ="/ClosedIndentDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
		public ModelAndView ClosedIndentDetails(HttpServletRequest request,HttpSession session) {
			String deptId = "";
			String toDate = "";
			String fromDate = "";
			String indentNumber = "";
			String response="";
			ModelAndView model = null;
			List<IndentCreationBean> indentClosedData = null;
			Map<String, String> siteDetails = null;
			String siteWiseIndentNo= "";
			String reqSiteId = "";
			String purchaseDeptId=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				siteWiseIndentNo=request.getParameter("indentNumber")==null ? "" : request.getParameter("indentNumber").trim();
				String sessionId=(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
				
				String sites=request.getParameter("site")==null ? "" : request.getParameter("site");// USER CAN SELECT ANY SITE TAKING THIS ONE I.E PURCHASE DEPT ONLY
				if(!sessionId.equals(purchaseDeptId)){
					sites=sessionId;
				}
				if(siteWiseIndentNo!=null && sites!=null && !siteWiseIndentNo.equals("") && !sites.equals("")){
					//ics.checkIndentAvailableOrNot(siteWiseIndentNo);
					indentNumber = String.valueOf(ics.getIndentNumber(Integer.parseInt(siteWiseIndentNo),Integer.parseInt(sites)));
				}
				if(sessionId.equals(purchaseDeptId) || sessionId.equals("119")){
					model.addObject("purchase",true);
				}
				siteDetails = new IndentSummaryDao().getSiteDetails();
				model.addObject("siteDetails", siteDetails);
				if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(indentNumber)) {
					 session = request.getSession(false);
					 deptId = validateParams.getProperty("SUMADHURA_PURCHASE_DEPT_MNGR");			
					//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+deptId);
					/* if(indentNumber.equals("0")){
						 model.addObject("displayErrMsg","Please Enter valid Indent Number.");
						 response="failure";
						 model.setViewName("ClosedIndents");
						 return model;
					 }*/
					  if (StringUtils.isNotBlank(deptId)) {
						indentClosedData = ics.getClosedIndents(fromDate, toDate, deptId,indentNumber,sessionId,sites);
						if(indentClosedData != null && indentClosedData.size() >0){
							request.setAttribute("showGrid", "true");
							response="success";
						} else {
							model.addObject("succMessage","Data Not Available.");
							response="failure";
							
						}
						model.addObject("indentClosedData",indentClosedData);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.addObject("indentNumber", indentNumber);
						model.setViewName("ClosedIndents");

					} else {
						model.addObject("Message","Session Expired, Please Login Again");
						model.setViewName("index");
						response="failure";
						return model;
					}
				} else {
					model.addObject("displayErrMsg", "Please Select From Date or To Date or Indent Number!");
					model.addObject("indentClosedData",indentClosedData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("indentNumber", indentNumber);
					response="success";
					model.setViewName("ClosedIndents");
				}
			} catch (Exception ex) {
				response="failure";
				ex.printStackTrace();
			} 
			
		
			return model;
		}
	
		@RequestMapping(value = "/ClosedIndentsList.spring", method = RequestMethod.GET)
		public String ClosedIndentsList(Model model, HttpServletRequest request,HttpSession session) {
			IndentCreationBean icb = new IndentCreationBean();
			int indentNumber = 0;
			String user_id = "";
			int site_Id = 0;
			// checking whether indentNumber is valid or not
			boolean isValid = false;
			try {
				session = request.getSession(true);
				indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
				site_Id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			try {
				
				String strPurpose = ics.getIndentLevelComments(indentNumber);
				
				model.addAttribute("IndentLevelCommentsList",strPurpose);
				model.addAttribute("indentCreationModelForm", icb);
				model.addAttribute("productsMap", iis.loadProds(String.valueOf(site_Id)));
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("blocksMap", iis.loadBlockDetails(String.valueOf(site_Id)));
				List<IndentCreationBean> list = ics.getIndentCreationLists(indentNumber);
				model.addAttribute("print","true");
				model.addAttribute("IndentGetList",list);
				model.addAttribute("IndentCreationDetailsList",ics.getIndentCreationDetailsLists(indentNumber));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			SaveAuditLogDetails.auditLog("0",user_id,"Showing Indent Details for Approval","Success",String.valueOf(site_Id));
			return "getIndentDetails";
		}

		/*@RequestMapping("/ViewIndentProductDetails.spring")
		public String ViewIndentProductDetails(HttpServletRequest request,  HttpSession session) throws Exception {
			String StrResponse="";
			String siteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			 List<Map<String,Object>> listAllProductList = new ArrayList<Map<String,Object>>();
			 listAllProductList=ics.viewIndentProductDetails(siteId);
			 request.setAttribute("allProductlist", listAllProductList);
			StrResponse="ViewIndentProductDetails";
			
			return StrResponse;
		}	*/
		
		@RequestMapping("/ViewIndentProductDetails.spring")
		public String ViewIndentProductDetails(HttpServletRequest request,  HttpSession session) throws Exception {

			String purchase_dept_id = "";
			String toDate = "";
			String fromDate = "";
			String response="";
			ModelAndView model = null;
			String central_Dept_Id="";
			List<Map<String,Object>> listAllProductList = new ArrayList<Map<String,Object>>();
			List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
			Map<String, String> siteDetails = null;
			String reqSiteId = "";
			String siteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			purchase_dept_id = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			central_Dept_Id=validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
			try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				reqSiteId=request.getParameter("site");
				siteDetails = new IndentSummaryDao().getSiteDetails();
				request.setAttribute("siteDetails",siteDetails);
			//	model.addObject("siteDetails", siteDetails);
				
				System.out.println("the data is "+siteDetails);
				
				

				if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
					session = request.getSession(false);

					if (siteId.equalsIgnoreCase(purchase_dept_id) || siteId.equalsIgnoreCase(central_Dept_Id)) {

						
						reqSiteId=request.getParameter("site");

						list=ics.viewIndentProductDetails(fromDate,toDate,reqSiteId);
						request.setAttribute("allProductlist", list);


						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.addObject("site",siteId);
						response="ViewIndentProductDetails";

					} else {
					//	request.setAttribute("purchase","false");
						list=ics.viewIndentProductDetails(fromDate,toDate,siteId);
						request.setAttribute("allProductlist", list);
						response="ViewIndentProductDetails";
					}
				} else {
					
					if(siteId.equalsIgnoreCase(purchase_dept_id) || siteId.equalsIgnoreCase(central_Dept_Id)){
						request.setAttribute("DEPT_ID","true");
					}

					response="ViewIndentDetailsPage";

				}
			} catch (Exception ex) {
				response="failure";
				ex.printStackTrace();
			} 


			return response;
		}
		
	/*	**********************************************indent rejected details show start**************************************************/
		
		@RequestMapping("/getRejectedIndentsList.spring")
		public String  getRejectedIndentsList(HttpServletRequest request,HttpSession session,Model model) {

			List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
			String user_id="";
			String site_id="";
			String retValue="";
			try {
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				site_id =(request.getParameter("siteId") == null ? "" : request.getParameter("siteId").toString());
				 list=ics.getRejectedIndentsList(user_id);
				
				if(list.size()>0){
					
					model.addAttribute("listOfIndents",list);
					retValue="ViewRejectedIndentList";
					
				}else{
					
					model.addAttribute("response1","Rejected Indents Not Available");
					retValue="response";
				}
				} 
			catch (Exception e) {
				e.printStackTrace();
			}
			SaveAuditLogDetails.auditLog("0",user_id,"getRejectedIndentsList","Success",site_id);
			
			return retValue;
			
		}
		
		/***************************************************reject from mail************************************************************************/
		
		@RequestMapping(value = "/IndentRejectFromMail.spring", method ={ RequestMethod.POST,RequestMethod.GET})
		public String IndentRejectFromMail(Model model, HttpServletRequest request,HttpSession session) {
			
			String responseStatus=IndentCreationApproveMail.rejectIndentCreationFromMail(session,request);
			if(responseStatus.equalsIgnoreCase("Success")){
				model.addAttribute("message","Indent Successfully Rejected");
			}else{
				model.addAttribute("message1","Indent Not Rejected");
			}
			
			return "response";
		}
		//This is For newly added Or Delete Or edit data and for rejected indents
		
		@RequestMapping(value = "/getProductDetailsForNewIndent.spring", method = RequestMethod.GET)
		public String getProductDetailsForNewIndent(Model model, HttpServletRequest request,HttpSession session) {
			System.out.println("getProductDetailsForNewIndent()");
			IndentCreationBean icb = new IndentCreationBean();
			int newIndentNumber = 0;
			String oldSiteWiseIndentNumber ="";
			String user_id = "";
			int site_Id = 0;
			int newSiteWiseIndentNo =0;
			int oldIndentNumber=0;
			String indentName="";
			// checking whether indentNumber is valid or not
			boolean isValid = false;
			String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			try {
				session = request.getSession(true);
				oldSiteWiseIndentNumber = request.getParameter("siteWiseIndentNo");
				oldIndentNumber=Integer.parseInt(request.getParameter("indentNumber"));
				indentName=request.getParameter("indentName")==null ? "-" : request.getParameter("indentName").toString();
				site_Id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				icb = ics.getIndentFromAndToDetails(user_id,icb,site_Id);
				newSiteWiseIndentNo = ics.getMaxOfSiteWiseIndentNumber(site_Id);
				newIndentNumber=ics.getIndentCreationSequenceNumber();
				model.addAttribute("Allsites",allSites);
				icb.setOld_Indent_Number(String.valueOf(oldIndentNumber));
				icb.setIndentNumber(newIndentNumber);
				icb.setSiteWiseIndentNo(newSiteWiseIndentNo);
				icb.setOld_SiteWiseIndent_Number(oldSiteWiseIndentNumber);
				icb.setIndentName(indentName);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				model.addAttribute("indentCreationModelForm", icb);
				model.addAttribute("productsMap", iis.loadProds(String.valueOf(site_Id)));
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("blocksMap", iis.loadBlockDetails(String.valueOf(site_Id)));
				List<IndentCreationBean> list = ics.getIndentCreationLists(oldIndentNumber);
				//ics.getIndentFromDetails(newIndentNumber,list);
				//ics.getIndentToDetails(user_id,list);
				
				List<IndentCreationBean> editList = new ArrayList<IndentCreationBean>();
				String strEditComments = "";
				
				
				String strPurpose = ics.getIndentRejectedComments(oldIndentNumber);
				
				model.addAttribute("IndentLevelCommentsList",strPurpose);
				model.addAttribute("IndentCreationList",list);
				model.addAttribute("IndentCreationDetailsList",ics.getIndentCreationDetailsLists(oldIndentNumber));
				//model.addAttribute("deletedProductDetailsList",ics.getDeletedProductDetailsLists(oldIndentNumber));
			} catch (Exception e) {
				e.printStackTrace();
			}

			SaveAuditLogDetails.auditLog("0",user_id,"Showing Indent Details for Approval","Success",String.valueOf(site_Id));
			return "IndentRejectedDetailsShow";
		}
		//approveRejectedIndentCreation
		@RequestMapping(value = "/approveRejectedIndentCreation.spring", method ={RequestMethod.POST,RequestMethod.GET})
		public String approveRejectedIndentCreation(Model model, HttpServletRequest request,HttpSession session) {
			System.out.println("approveRejectedIndentCreation()");
			
			IndentCreationBean icb = new IndentCreationBean();
			int site_id = 0;
			String user_id = "";
			String response = "";
			String user_Name="";
			String indentNumber="";
			String siteWiseIndentNo="";
			try{
				session = request.getSession(true);
				site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
				user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				user_Name=session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
				indentNumber= request.getParameter("IndentNumber")== null ? "" : request.getParameter("IndentNumber");
				siteWiseIndentNo=request.getParameter("siteWiseIndentNo")== null ? "" : request.getParameter("siteWiseIndentNo");
				
				if(indentNumber==null || indentNumber.equals("") || siteWiseIndentNo==null || siteWiseIndentNo.equals("")){
					model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
					return "response";
				}
				response=getAndCheckBOQQuantity(request,session,true);
				if(!response.contains("BOQ")){
					response = ics.approveRejectedIndentCreation(model,request, site_id, user_id,user_Name);
				}
				
				if(response.equals("success")){
					request.setAttribute("message", "Indent Created And Approved successfully.");
				}else if(response.contains("BOQ")){
					request.setAttribute("message1",response);
				}
				else{
					
					request.setAttribute("message1", "Indent Not Created.");
				}//model.addAttribute("responseMessage",response);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			SaveAuditLogDetails.auditLog("0",user_id,"Indent Created",response,String.valueOf(site_id));
			return "response";

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
		
		/*========================================== material boq start ==============================================================*/
		@RequestMapping(value = "/getAndCheckBOQQuantity", method ={RequestMethod.POST,RequestMethod.GET})
		@ResponseBody
		//public String getAndCheckBOQQuantity() {
		//@RequestMapping(value = "/getAndCheckBOQQuantity.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String getAndCheckBOQQuantity(HttpServletRequest request,HttpSession session,boolean approveOrNot) {
			String response="";
			String site_id=request.getParameter("siteId")== null ? "" : request.getParameter("siteId"); // check the boq quantity purpose written this one
			String indentAllowSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			//List<String> List=Arrays.asList(indentAllowSites.split(","));
			if(!indentAllowSites.contains(site_id)){
			response=ics.getAndCheckBOQQuantity(session,request,site_id,approveOrNot);
			}
			return response;//ics.loadSubProds(mainProductId);
		}
		
		/*================================================ getting the BOQ data for check the ajax call start=====================================*/
		@RequestMapping(value = "/gettingBoqQuantityAjax", method = RequestMethod.POST)
		@ResponseBody
		public String gettingBoqQuantityAjax(@RequestParam("childProductId") String childProductId,@RequestParam("groupId") String groupId,@RequestParam("siteId") String siteId) {
			return ics.gettingBoqQuantityAjax(groupId,siteId);
		}
		/*================================================ getting the BOQ data for check the ajax call end=====================================*/
	
	}
