package com.sumadhura.in;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.service.DCFormService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.InwardToGetInvoiceDetailsService;
import com.sumadhura.service.PurchaseDepartmentIndentrocessService;
import com.sumadhura.transdao.IndentIssueDaoImpl;
import com.sumadhura.transdao.IndentReceiveDaoImpl;
import com.sumadhura.transdao.InwardGetInvoiceDetailsDaoImpl;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;


@Controller
public class IndentReceiveController extends UIProperties {

	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;
	
	@Autowired
	private UtilDao utilDao;

	@Autowired
	CommonUtilities objCommonUtilities;
	@Autowired
	@Qualifier("dcFormClass")
	DCFormService dcFormService;
	
	@Autowired
	@Qualifier("purchaseDeptIndentrocess")
	PurchaseDepartmentIndentrocessService objPurchaseDeptIndentrocess;

	@Autowired
	InwardToGetInvoiceDetailsService inwardToGetInvoiceDetails;

	@RequestMapping(value = "/launchIndentReceivePage", method = {RequestMethod.GET,RequestMethod.POST})
	public String launchIndentReceivePage(Model model,HttpSession session) {
		String StrSiteId= String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("indentReceiveModelForm", new IndentReceiveBean());
		model.addAttribute("productsMap", irs.loadProds(StrSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("gstMap", irs.getGSTSlabs());
		model.addAttribute("chargesMap", irs.getOtherCharges());
		String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		
		model.addAttribute("siteId",StrSiteId);
		model.addAttribute("Allsites",allSites);
		//System.out.println("the role id of  "+no);

		if(StrRoleId.equals("2") && !StrSiteId.equals("102") && !StrSiteId.equals("107")){
			model.addAttribute("noOfDays",1000);
		}else if(StrRoleId.equals("2") && StrSiteId.equals("102")){
			model.addAttribute("noOfDays",14);
		}else if(StrRoleId.equals("2") && StrSiteId.equals("107")){
			model.addAttribute("noOfDays",56);
		}else if(StrRoleId.equals("2") && StrSiteId.equals("112")){
			model.addAttribute("noOfDays",55);
		}
		else if(StrRoleId.equals("4")){
			model.addAttribute("noOfDays",30);
		}else if(StrRoleId.equals("5")){
			model.addAttribute("noOfDays",100);
		}else if(StrRoleId.equals("1")){
			model.addAttribute("noOfDays",10);
		}else if(StrRoleId.equals("12")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("13")){
			model.addAttribute("noOfDays",20);
		}else{
			model.addAttribute("noOfDays",0);
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();

		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("site_id",site_id);
		//String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
		audit.auditLog("0",user_id,"New Receive View","success",site_id);
		return "indentReceive";
	}
	
	@RequestMapping(value = "/materialAdjustment", method = {RequestMethod.GET,RequestMethod.POST})
	public String materialAdjustment(Model model,HttpSession session,HttpServletRequest request) {
	String viewToBeSelected="";
	
		String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
		//add the current url controller so after selecting site the request will come to same controller
		model.addAttribute("pageName", "Material Adjustment");
		if(dropdown_SiteId.length()==0){
			model.addAttribute("urlForActionTag", "materialAdjustment.spring");
			return "SiteSelection";
		}
		String vendorId="";
		String vendorName="";
		String vendorAddress="";
		String vendorGSTINNo="";
		
	List<Map<String, Object>> vendorDetails=	dcFormService.getVendorInfoByID(dropdown_SiteId);
		if(vendorDetails!=null||vendorDetails.size()>0){
			for (Map<String, Object> map : vendorDetails) {
				vendorId=map.get("VENDOR_ID")==null?"":map.get("VENDOR_ID").toString();
				vendorName=map.get("VENDOR_NAME")==null?"":map.get("VENDOR_NAME").toString();
				vendorAddress=map.get("ADDRESS")==null?"":map.get("ADDRESS").toString();
				vendorGSTINNo=map.get("GSIN_NUMBER")==null?"":map.get("GSIN_NUMBER").toString();
			}
		}	
		model.addAttribute("vendorId", vendorId);
		model.addAttribute("vendorName", vendorName);
		model.addAttribute("vendorAddress", vendorAddress);
		model.addAttribute("vendorGSTINNo", vendorGSTINNo);
		
		String StrSiteId= String.valueOf(session.getAttribute("SiteId"));
		
		model.addAttribute("indentReceiveModelForm", new IndentReceiveBean());
		model.addAttribute("productsMap", irs.loadProds(StrSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("gstMap", irs.getGSTSlabs());
		model.addAttribute("chargesMap", irs.getOtherCharges());
		String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
		
		//System.out.println("the role id of  "+no);

		if(StrRoleId.equals("2") && !StrSiteId.equals("102")){
			model.addAttribute("noOfDays",7);
		}else if(StrRoleId.equals("2") && StrSiteId.equals("102")){
			model.addAttribute("noOfDays",14);
		}else if(StrRoleId.equals("4")){
			model.addAttribute("noOfDays",30);
		}else if(StrRoleId.equals("5")){
			model.addAttribute("noOfDays",100);
		}else if(StrRoleId.equals("1")){
			model.addAttribute("noOfDays",10);
		}else if(StrRoleId.equals("12")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("13")){
			model.addAttribute("noOfDays",20);
		}else{
			model.addAttribute("noOfDays",0);
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();

		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		//String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
		audit.auditLog("0",user_id,"New Material Adjustment Receive View","success",site_id);
		
		viewToBeSelected= "MaterrialAdjustmentIndentReceive";	
		return viewToBeSelected;
	}

	@RequestMapping(value = "/indentReceiveSubProducts", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueSubProducts(@RequestParam("mainProductId") String mainProductId) {
		return irs.loadSubProds(mainProductId);
	}
	@RequestMapping(value = "/indentReceiveSubProductsByPONumber", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueSubProductsByPONumber(@RequestParam("mainProductId") String mainProductId,
			@RequestParam("poNumber") String poNumber,@RequestParam("reqSiteId") String reqSiteId) {
		String s = irs.loadSubProdsByPONumber(mainProductId,poNumber,reqSiteId);
		//System.out.println(s);
		return s;
	}

	@RequestMapping(value = "/indentReceiveChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueChildProducts(@RequestParam("subProductId") String subProductId) {
		return irs.loadChildProds(subProductId);
	}
	@RequestMapping(value = "/indentReceiveChildProductsByPONumber", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueChildProductsByPONumber(@RequestParam("subProductId") String subProductId,
			@RequestParam("poNumber") String poNumber,@RequestParam("reqSiteId") String reqSiteId) {
		String s = irs.loadChildProdsByPONumber(subProductId,poNumber,reqSiteId);
		//System.out.println(s);
		return s;
	}
	@RequestMapping(value = "/getPriceRatesByChildProduct", method = RequestMethod.POST)
	@ResponseBody
	public String getPriceRatesByChildProduct(@RequestParam("childProdId") String childProdId,
			@RequestParam("poNumber") String poNumber,@RequestParam("reqSiteId") String reqSiteId) {
		String productDetails = irs.getPriceRatesByChildProduct( childProdId,  poNumber,  reqSiteId);
		//System.out.println(productDetails);
		return productDetails;
	}

	@RequestMapping(value = "/listIndentReciveUnitsOfChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String listUnitsOfSubProducts(@RequestParam("productId") String productId) {
		return irs.loadIndentReceiveMeasurements(productId);
	}

	@RequestMapping(value = "/loadAndSetVendorInfo", method = RequestMethod.POST)
	@ResponseBody
	public String loadAndSetVendorInfo(@RequestParam("vendName") String vendName) {
		return irs.getVendorInfo(vendName);
	}
	@RequestMapping(value = "/getReceiveCount", method = RequestMethod.POST)
	@ResponseBody
	public int getReceiveCount(@RequestParam("invoiceNumber") String strInvoicNo,
			@RequestParam("vendorname") String strVendorName,
			@RequestParam("receiveDate") String strReceiveDate
	) {
		return irs.getInvoiceCount(  strInvoicNo,  strVendorName,  strReceiveDate);
	}

	@RequestMapping(value = "/getCheckIndentAvailable", method = RequestMethod.POST)
	@ResponseBody
	public int getCheckIndentAvailable(@RequestParam("indentNumber") String indentNumber
			
	) {
		return irs.getCheckIndentAvailable(indentNumber);
	}
	
	@RequestMapping(value = "/getCheckPoAvailable", method = RequestMethod.POST)
	@ResponseBody
	public int getCheckPoAvailable(@RequestParam("poNumber") String poNumber,@RequestParam("vendorId") String vendorId
			
			
	) {
		return irs.getCheckPoAvailable(poNumber,vendorId);
	}
	
	@RequestMapping(value = "/doIndentReceive",  method = { RequestMethod.GET, RequestMethod.POST })
	public String doIndentReceive(@ModelAttribute("indentReceiveModelForm")IndentReceiveBean indentRecModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session, @RequestParam(value="file",required = false) MultipartFile[] files) throws IllegalStateException, IOException {
		//System.out.println("IndentReceiveController.doIndentReceive()");
	 
	String strValue="";
		String viewToBeSelected = "";
		String response ="";
		String site_id ="";
		String invoiceNumber=request.getParameter("InvoiceNumber");
		String invoiceDate=request.getParameter("InvoiceDate");
		String vendorName=request.getParameter("VendorName");
		String reciveDate=request.getParameter("receivedDate");
		if(invoiceNumber==null&&invoiceDate==null&&vendorName==null&&reciveDate==null){
			model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";  
		}
		if(invoiceNumber!=null && !invoiceNumber.equals("")){
		strValue=getAndCheckReceiveBOQQuantity(request,session);
		if(!strValue.contains("BOQ")){
		String response1 = irs.indentProcess(model, request, session);
		String moduleName=request.getParameter("moduleName")==null?"":request.getParameter("moduleName");
				
		model.addAttribute("urlName","launchIndentReceivePage.spring");
		
		//-----------------------
		String[] response_array = response1.split("@@");
		response = response_array [0];
		site_id = String.valueOf(session.getAttribute("SiteId"));

		String imgname = response_array [1];//"vname_invoiceno_entryid";
		imgname = imgname.replace("/","$$");
		for (int i = 0,j = 0; i < files.length; i++) {
			MultipartFile multipartFile = files[i];



			if (response.equalsIgnoreCase("Success")&&!multipartFile.isEmpty()) {
				try {

					String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
					File dir = new File(rootFilePath+site_id);
					if (!dir.exists())
						dir.mkdirs();

					String filePath = dir.getAbsolutePath()
					+ File.separator + imgname+"_Part"+j+".jpg"; 
					j++;
					multipartFile.transferTo(new File(filePath));


					//System.out.println("Image Uploaded");
					//return "You successfully uploaded file" ;
				} catch (Exception e) {
					//System.out.println("Image NOT Uploaded");
					//return "You failed to upload " ;
				}
			} else {
				//return "You failed to upload " + " because the file was empty.";
			}

		}

		//------------------------

		//-------------------------

		if(response.equalsIgnoreCase("Success")) {

			int intSavedCount = irs.isInvoicesaved(  request );

			if(intSavedCount == 0){
				response="Failed";
				request.setAttribute("exceptionMsg", "This Invoice not inserted");
			}
		}

		if(response.equalsIgnoreCase("Success")) {
			if(moduleName.equals("materialAdjustment")){
				model.addAttribute("response", "Materials Received Successfully...");
				viewToBeSelected = "response";	
			}else{
				viewToBeSelected = "viewGRN";
			}
		}
		else if(response.equalsIgnoreCase("Failed")){
			viewToBeSelected = "indentReceiveResponse";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			request.setAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}

		}else{
			model.addAttribute("message1",strValue);
			return "response";
		}
		}else{
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		SaveAuditLogDetails audit=new SaveAuditLogDetails();


		String user_id=String.valueOf(session.getAttribute("UserId"));

		String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
		audit.auditLog(indentEntrySeqNum,user_id,"New Receive Save Data ",response,site_id);

		return viewToBeSelected;
	}

	@RequestMapping(value="viewReciveDetails.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewIndentIssueDetails(HttpServletRequest request,HttpSession session) {

		ModelAndView model = null;

		try {
			model = new ModelAndView();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("ViewIndentReciveDetails");
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"View Received Details","success",site_id);

		return model;
	}
/**
 * @author Aniket Chavan
 * @param request
 * @param session
 * @since 04-may-2018
 * @return
 */
	@RequestMapping(value = "/getSiteWiseReceiveDetails.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getSiteWiseReceiveDetails(HttpServletRequest request,HttpSession session) {
		//System.out.println("IndentReceiveController.getSiteWiseReceiveDetails()");
		ModelAndView mav = new ModelAndView();
		String userid = "0";
		try {
			userid = (String) CheckSessionValidation.validateUserSession(mav,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!userid.equals("0")) {
				List<Map<String, Object>> allSitesList = utilDao.getAllSites();
				request.setAttribute("SEARCHTYPE", "ADMIN");
				System.out.println("SearchType is admin");
				request.setAttribute("allSitesList", allSitesList);
				mav.setViewName("ViewIndentReciveDetails");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"View Received Details","success",site_id);


		return mav;
	}
	
	
	
	@RequestMapping(value ="getIndentReciveViewDts.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getIndentViewDts(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String response="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				session = request.getSession(false);
				//  site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				//ACP
				site_id = request.getParameter("dropdown_SiteId") == null ? "" : request.getParameter("dropdown_SiteId");
				if (!site_id.equals("")) {
					List<Map<String, Object>> allSitesList = utilDao.getAllSites();
					request.setAttribute("SEARCHTYPE", "ADMIN");
					System.out.println("SearchType is admin");
					request.setAttribute("allSitesList", allSitesList);
					System.out.println("IndentReceiveController.getIndentViewDts() DropDown value is not empty");
				} else if (site_id.equals("")) {
					System.out.println("IndentReceiveController.getIndentViewDts() DropDown value is empty");
					site_id =  session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				}
				//ACP
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentIssueData = new IndentIssueDaoImpl().getViewIndentIssueDetails(fromDate, toDate, site_id, "Val",null);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.setViewName("ViewIndentReciveDetails");
					response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failed";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.setViewName("ViewIndentReciveDetails");
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
		audit.auditLog("0",user_id,"get invoice Details View data",response,site_id1);

		return model;
	}
	@SuppressWarnings("static-access")
	@RequestMapping(value ="getInvoiceReciveViewDts.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getInvoiceViewDts(HttpServletRequest request,HttpSession session ) {
		String selectIndentType="IN";//ACP
		String site_id = "";
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String invoiceNumber="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			invoiceNumber=request.getParameter("strInvoiceNumber");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentIssueData = new IndentReceiveDaoImpl().getSiteWiseInvoiceDetails(fromDate, toDate, site_id,selectIndentType);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("indentType", selectIndentType);
					model.setViewName("ViewSecificSiteInvoices");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			}
			
			else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.setViewName("ViewSecificSiteInvoices");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return model;
	}

	
	//ACP
	@SuppressWarnings("static-access")
	@RequestMapping(value ="getMaterialAdjInvoiceReciveDtls.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getMaterialAdjInvoiceReciveDtls(HttpServletRequest request,HttpSession session ) {
		String selectIndentType="INU";
		String site_id = "";
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String invoiceNumber="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			invoiceNumber=request.getParameter("strInvoiceNumber");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentIssueData = new IndentReceiveDaoImpl().getSiteWiseInvoiceDetails(fromDate, toDate, site_id,selectIndentType);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("indentType", selectIndentType);
					model.setViewName("ViewMaterialAdjustedSiteInvoices");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			}
			
			else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.setViewName("ViewMaterialAdjustedSiteInvoices");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return model;
	}


	/**
	 * @description this controller is for site wise  invoice details, it will show only invoice details
	 * @param request
	 * @param session
	 * @return
	 */
	
	@SuppressWarnings("static-access")
	@RequestMapping(value ="getAllSiteReciveInvoiceDts.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getAllSiteReciveInvoiceDts(HttpServletRequest request,HttpSession session) {
		String selectIndentType="IN";

		String toDate = "";
		String fromDate = "";
		String siteId =  "";
		ModelAndView model = null;
		String response="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			siteId = request.getParameter("siteId");
			if ((StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) && StringUtils.isNotBlank(siteId)) {
				session = request.getSession(false);
				//site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+siteId);
				if (StringUtils.isNotBlank(siteId)) {
					indentIssueData = new IndentReceiveDaoImpl().getSiteWiseInvoiceDetails(fromDate, toDate, siteId,selectIndentType);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					//String strIndentEntryId = request.getAttribute("INDENT_ENTRY_ID") == null ? "" : request.getAttribute("INDENT_ENTRY_ID").toString();
					//model.addObject("indentEntryId",strIndentEntryId);
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("getSiteList", objCommonUtilities.getSiteList());
					model.addObject("url","getAllSiteReciveInvoiceDts.spring?fromDate="+fromDate+"&toDate="+toDate+"&siteId"+siteId);
					model.setViewName("ViewAllSiteInvoices");
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					//acp
					model.addObject("strSiteId", siteId);
					model.addObject("indentType", selectIndentType);
					//using same page for showing the data so showing difference between them used moduleName
					model.addObject("moduleName", "allSiteInvoiceDetails");
					response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failed";
					return model;
				}
			} else {
				//model.addObject("displayErrMsg", "Please Select From Date or To Date and site!");
				model.addObject("getSiteList", objCommonUtilities.getSiteList());
				model.setViewName("ViewAllSiteInvoices");
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("moduleName", "allSiteInvoiceDetails");
				response="success";
			}
		} catch (Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Get All site Invoice Details View",response,site_id1);

		return model;
	}
	//ACP
	/**
	 * @description this controller is for site wise material adjustment details, it will show only adjusted material details
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value ="getSiteWiseMaterialAdjDtls.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getSiteWiseMaterialAdjDtls(HttpServletRequest request,HttpSession session) {
		String selectIndentType="INU";

		String toDate = "";
		String fromDate = "";
		String siteId =  "";
		ModelAndView model = null;
		String response="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			siteId = request.getParameter("siteId");
			if ((StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) && StringUtils.isNotBlank(siteId)) {
				session = request.getSession(false);
				//site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+siteId);
				if (StringUtils.isNotBlank(siteId)) {
					indentIssueData = new IndentReceiveDaoImpl().getSiteWiseInvoiceDetails(fromDate, toDate, siteId,selectIndentType);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					//String strIndentEntryId = request.getAttribute("INDENT_ENTRY_ID") == null ? "" : request.getAttribute("INDENT_ENTRY_ID").toString();
					//model.addObject("indentEntryId",strIndentEntryId);
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("getSiteList", objCommonUtilities.getSiteList());
					model.setViewName("ViewAllSiteInvoices");
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					//ACP
					model.addObject("strSiteId", siteId);
					model.addObject("indentType", selectIndentType);
					//using same page for showing the data so showing difference between them used moduleName
					model.addObject("moduleName", "allSiteMaterialAdjustmentInvoice");
					response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failed";
					return model;
				}
			} else {
				//model.addObject("displayErrMsg", "Please Select From Date or To Date and site!");
				model.addObject("getSiteList", objCommonUtilities.getSiteList());
				model.setViewName("ViewAllSiteInvoices");
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("moduleName", "allSiteMaterialAdjustmentInvoice");
				response="success";
			}
		} catch (Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Get All site Invoice Details View",response,site_id1);

		return model;
	}
	

	@RequestMapping(value = "/inwardsFromPO.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String inwardsFromPO(HttpServletRequest request, HttpSession session,Model model){

		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		String view = "";
		if(site_id1.equals("996")){
			model.addAttribute("listOfPOs",irs.getListOfActiveMarketingPOs(site_id1,"A"));
			view = "ShowPOListForMarketing";
		}else{
			model.addAttribute("listOfPOs",irs.getListOfActivePOs(site_id1));
			view = "ShowPOListToConvertToInvoice";
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Inwards From PO","success",site_id1);


		return view;

	}

	@RequestMapping(value = "/submitTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String submitTaxInvoices(Model model, HttpServletRequest request,HttpSession session) {
		String viewName="";
		String site_id="";
		String user_id="";
		GetInvoiceDetailsBean bean=null;
		try {
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "submitTaxInvoices.spring");
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			bean=new GetInvoiceDetailsBean();
			bean.setSiteId(site_id);
			bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
			
			if (bean.getApproverEmpId() == null) {
				model.addAttribute("response1", "You can not submit tax invoices, no mapping found.");
				model.addAttribute("nextLevelApproverEmpId",bean.getApproverEmpId());
				return "response";
			}	
			List<Map<String, Object>> list=	irs.loadAllTaxInvoicesDetails(bean);
			model.addAttribute("InvoiceDetails", list);
			model.addAttribute("InvoiceDetailsBean", bean);
			viewName="SubmitTaxInvoices/submitTaxInvoices";	
			model.addAttribute("SiteId", site_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewName;
	}
	
	  String path = "/error";

	  /* @RequestMapping(value="/404")
	   public String error404(HttpSession session){
	      // DO stuff here 
		   if(session==null){
			   
		   }
	       return "error404";
	   }*/
	   
	   /**
	    * @author Aniket Chavan
	    * @description this controller used to show all invoices related to current site
	    * @param model
	    * @param request
	    * @param session
	    * @return
	    */
	   @RequestMapping(value = "/viewTaxInvoiceStatus.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String viewTaxInvoiceStatus(Model model, HttpServletRequest request,HttpSession session) {
			String viewName="";
			String site_id="";
			String user_id="";
			GetInvoiceDetailsBean bean=null;
			try {
				//this URl is for highlighting sub module
				model.addAttribute("urlForActivateSubModule", "viewTaxInvoiceStatus.spring");
				model.addAttribute("pageName", "View Tax Invoice");
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				user_id=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
				bean=new GetInvoiceDetailsBean();
				bean.setSiteId(site_id);
				bean.setUserId(user_id);
				/*bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
				
				if (bean.getApproverEmpId() == null) {
					bean.setApproverEmpId("END");
				}*/
				
				List<Map<String, Object>> list=	irs.receiveTaxInvoicesDetails(bean,"statusPage","");
				model.addAttribute("InvoiceDetails", list);
				model.addAttribute("InvoiceDetailsBean", bean);
				model.addAttribute("isApprovePage", "false");
				viewName="SubmitTaxInvoices/receiveTaxInvoices";	
				model.addAttribute("SiteId", site_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return viewName;
		}
	   
	   /**
	    * @author Aniket Chavan
	    * @description this controller used to show the tax invoices site wise
	    * @param model
	    * @param request
	    * @param session
	    * @return
	    */
	   @RequestMapping(value = "/viewAllsiteTaxInvoiceStatus.spring", method = {RequestMethod.GET,RequestMethod.POST})
		public String viewAllsiteTaxInvoiceStatus(Model model, HttpServletRequest request,HttpSession session) {
			String viewName="";
			String site_id="";
			String user_id="";
			GetInvoiceDetailsBean bean=null;
		   	try {
		   		String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
				//add the current url controller so after selecting site the request will come to same controller
		   		model.addAttribute("urlForActionTag", "viewAllsiteTaxInvoiceStatus.spring");
		   		model.addAttribute("pageName", "View Site Wise Tax Invoice");
				if(dropdown_SiteId.length()==0){
					return "SiteSelection";
				}
				
				model.addAttribute("isApprovePage", "false");

				
				//this URl is for highlighting sub module
				model.addAttribute("urlForActivateSubModule", "viewAllsiteTaxInvoiceStatus.spring");
				site_id = dropdown_SiteId;
				user_id=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
				bean=new GetInvoiceDetailsBean();
				bean.setSiteId(site_id);
				bean.setUserId(user_id);
				/*bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
				
				if (bean.getApproverEmpId() == null) {
				 	bean.setApproverEmpId("END");
				}*/
				
				List<Map<String, Object>> list=	irs.receiveTaxInvoicesDetails(bean,"statusPage","");
				if(list.size()==0){
					model.addAttribute("infoMessage", "No records found for approve.");	
					model.addAttribute("showTable", "false");
				}else{
					model.addAttribute("showTable", "true");
				}
				model.addAttribute("isCommonApproval", "true");
				model.addAttribute("InvoiceDetails", list);
				model.addAttribute("strSiteId", site_id);
				model.addAttribute("InvoiceDetailsBean", bean);
				model.addAttribute("isApprovePage", "false");
				viewName="SubmitTaxInvoices/receiveTaxInvoices";	
				model.addAttribute("SiteId", site_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return viewName;
	   }
	   
	   
	/**
	 * @author Aniket Chavan
	 * @description this controller is used to approve tax invoice site wise
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/receiveSiteiseTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String receiveSiteiseTaxInvoices(Model model, HttpServletRequest request,HttpSession session) {
		String viewName="";
		String site_id="";
		String user_id="";
		GetInvoiceDetailsBean bean=null;
		try {
			
			String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
			//add the current url controller so after selecting site the request will come to same controller
			model.addAttribute("urlForActionTag", "receiveSiteiseTaxInvoices.spring");
			model.addAttribute("pageName", "Recieve Site Wise Tax Invoice");
			if(dropdown_SiteId.length()==0){
				return "SiteSelection";
			}
			
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "receiveSiteiseTaxInvoices.spring");
			site_id = dropdown_SiteId;
			user_id=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			bean=new GetInvoiceDetailsBean();
			bean.setSiteId(site_id);
			bean.setUserId(user_id);
			bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
			
			if (bean.getApproverEmpId() == null) {
			 	bean.setApproverEmpId("END");
			}
			
			List<Map<String, Object>> list=	irs.receiveTaxInvoicesDetails(bean,"approvePage","siteWise");
			/*Map<String, String> siteIdWithApprovalEmpId=new HashMap<>();
			for (Map<String, Object> map : list) {
				String siteId=map.get("SITE_ID").toString();
				if(!siteIdWithApprovalEmpId.containsKey(siteId)){
					bean.setSiteId(site_id);
					bean.setUserId(user_id);
					bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
					
					if (bean.getApproverEmpId() == null) {
							bean.setApproverEmpId("END");
							siteIdWithApprovalEmpId.put(site_id, "END");
					}else{
							siteIdWithApprovalEmpId.put(site_id, bean.getApproverEmpId());
					}
				}
			}*/
			
			if(list.size()==0){
				model.addAttribute("infoMessage", "No records found for approve.");	
				model.addAttribute("showTable", "false");
			}else{
				model.addAttribute("showTable", "true");
			}
			//model.addAttribute("siteIdWithApprovalEmpId", siteIdWithApprovalEmpId);
			model.addAttribute("InvoiceDetails", list);
			model.addAttribute("strSiteId", site_id);
			model.addAttribute("InvoiceDetailsBean", bean);
			model.addAttribute("isApprovePage", "true");
			model.addAttribute("isCommonApproval", "true");
			viewName="SubmitTaxInvoices/receiveTaxInvoices";	
			model.addAttribute("SiteId", site_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewName;
	}
	 
	@RequestMapping(value = "/receiveTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String receiveTaxInvoices(Model model, HttpServletRequest request,HttpSession session) {
		String viewName="";
		String site_id="";
		String user_id="";
		GetInvoiceDetailsBean bean=null;
		try {
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "receiveTaxInvoices.spring");
			model.addAttribute("pageName", "Recieve Tax Invoice");
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			bean=new GetInvoiceDetailsBean();
			bean.setSiteId(site_id);
			bean.setUserId(user_id);
			bean=irs.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);
			
			if (bean.getApproverEmpId() == null) {
			 
				bean.setApproverEmpId("END");
			}
			
			List<Map<String, Object>> list=	irs.receiveTaxInvoicesDetails(bean,"approvePage","");
			model.addAttribute("InvoiceDetails", list);
			model.addAttribute("isApprovePage", "true");
			model.addAttribute("InvoiceDetailsBean", bean);
			viewName="SubmitTaxInvoices/receiveTaxInvoices";	
			model.addAttribute("SiteId", site_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewName;
	}
	
	
	
	
	
	
	
	/**
	 * @description this controller used to store the tax invoice bill's
	 * This Module is to maintain Physical Invoice Bills. As Original Bills are important and as they have
		to submit to Government, we are maintaining this module. Generally along with physical
		material Invoice will be received to Stores, From Stores to Site accountant and then to Account
		Department.
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/saveTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String saveTaxInvoices(Model model, HttpServletRequest request,HttpSession session,RedirectAttributes redir) {
		String response="";
		String site_id="";
		String view = "";
		String siteName = "";
		try {
			siteName = session.getAttribute("SiteName")==null?"":session.getAttribute("SiteName").toString();
			
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "submitTaxInvoices.spring");
			response=irs.saveTaxInvoicesDetails(request,session, redir);
			if(response.equals("Success")){
				//model.addAttribute("response", "Successfully saved tax invoice information.");
				redir.addFlashAttribute("showGrid", "true");
				redir.addFlashAttribute("siteName", siteName);
				return "redirect:/PrintTaxInvoices.spring";
			}else{
				model.addAttribute("response1", "Failed to save tax invoice information.");
				view = "response";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	@RequestMapping(value = "/PrintTaxInvoices.spring",  method = RequestMethod.GET)
	public ModelAndView printTaxInvoices(){
	    return new ModelAndView("SubmitTaxInvoices/PrintTaxInvoices");
	}
	
	@RequestMapping(value = "/approveTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String approveTaxInvoices(Model model, HttpServletRequest request,HttpSession session,  RedirectAttributes redir) {
		String response="";
		String site_id="";
		String nextLevelApprovalEmpId="";
		String view = "";
		try {
			
			String[] recordToProceed=request.getParameterValues("recordsToproceed");
		 	site_id=request.getParameter("site_id");
			nextLevelApprovalEmpId=request.getParameter("approverEmpId");
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "receiveTaxInvoices.spring");
			if(recordToProceed==null&&site_id==null&&nextLevelApprovalEmpId==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			response=irs.approveTaxInvoicesDetails(request,session, redir);
			if(response.equals("Success")){
				//model.addAttribute("response", "Successfully approved tax invoice information.");
				redir.addFlashAttribute("showGrid", "true");
				return "redirect:/PrintTaxInvoices.spring";
			}else{
				model.addAttribute("response1", "Failed to approve tax invoice information.");
				view = "response";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	@RequestMapping(value = "/rejectTaxInvoices.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String rejectTaxInvoices(Model model, HttpServletRequest request,HttpSession session) {
		String response="";
		String site_id="";
		String nextLevelApprovalEmpId="";
		try {
			String[] recordToProceed=request.getParameterValues("recordsToproceed");
		 	site_id=request.getParameter("site_id");
			nextLevelApprovalEmpId=request.getParameter("approverEmpId");
			//this URl is for highlighting sub module
			model.addAttribute("urlForActivateSubModule", "receiveTaxInvoices.spring");
			if(recordToProceed==null&&site_id==null&&nextLevelApprovalEmpId==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
		
			response=irs.rejectTaxInvoicesDetails(request,session);
			if(response.equals("Success")){
				model.addAttribute("response", "Successfully rejected tax invoice information.");
			}else{
				model.addAttribute("response1", "Failed to reject tax invoice information.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "response";
	}
	
	@RequestMapping(value = "/checkAttachmentForInvoice.spring", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String isInvoiceHasAttachments(Model model, HttpServletRequest request,HttpSession session) {
		String response="";
		String site_id="";
		String invoiceNumber="";
		String indentEntryId="";
		String vendorId="";
		String vendorName="";
		int imageCount=0;
		int pdfCount=0;
		try {
			  site_id =request.getParameter("site_id")==null?"":request.getParameter("site_id");
			  invoiceNumber=request.getParameter("invoiceNumber")==null?"":request.getParameter("invoiceNumber");
			  indentEntryId=request.getParameter("indentEntryId")==null?"":request.getParameter("indentEntryId");	 	
			  vendorId=request.getParameter("vendorId")==null?"":request.getParameter("vendorId");
			  vendorName=request.getParameter("vendorName")==null?"":request.getParameter("vendorName");
			  
			  String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
			  String imgname = vendorName+"_"+invoiceNumber+"_"+indentEntryId;
			  GetInvoiceDetailsController.loadInvoiceImgAndPdfFiles(rootFilePath,imgname,site_id,model,request); // getting the images and pdf for showing purpose
			  imageCount=Integer.parseInt(request.getAttribute("imagecount")==null?"0":request.getAttribute("imagecount").toString());
			  pdfCount=Integer.parseInt(request.getAttribute("pdfcount")==null?"0":request.getAttribute("pdfcount").toString()); 
			  
			  if(imageCount>0||pdfCount>0){
				  response="true";
			  }else{
				  response="false";
			  }
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this controller is used to load the invoice number using vendor id and site id
	 * @return
	 */
	@RequestMapping(value = "/loadInvoiceNumberForTaxSubmit.spring", method = RequestMethod.GET )
	@ResponseBody
	public List<Map<String, Object>> loadInvoiceNumberByVendorId(Model model, HttpServletRequest request,HttpSession session) {
		String site_id="";
		String vendorId="";
		try {
			site_id =request.getParameter("site_id")==null?"":request.getParameter("site_id");
			vendorId =request.getParameter("vendorId")==null?"":request.getParameter("vendorId");
			List<Map<String, Object>> listOfInvoiceId=irs.loadInvoiceNumberByVendorId(site_id,vendorId);
			return listOfInvoiceId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@RequestMapping(value = "/showPODetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String showPODetails(Model model, HttpServletRequest request,HttpSession session) {

		int site_id = 0;
		String user_id = "";
		String preparedBy = request.getParameter("preparedBy")==null?"":request.getParameter("preparedBy");
		
		try {
			session = request.getSession(true);
			String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
			String StrSiteId = String.valueOf(session.getAttribute("SiteId"));
			String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
			// System.out.println("the role id of  "+no);
			if (StrRoleId.equals("27") && StrSiteId.equals("996")) {
				model.addAttribute("noOfDays",6);
			}else if (StrRoleId.equals("30") && StrSiteId.equals("996")) {
				model.addAttribute("noOfDays",6);
			}else if(StrRoleId.equals("2") && StrSiteId.equals("107")){
				model.addAttribute("noOfDays",55);
			}
			else if (StrRoleId.equals("25") && StrSiteId.equals("996")) {
				model.addAttribute("noOfDays",70);
			}else if (StrRoleId.equals("2") && !StrSiteId.equals("102")) {
				model.addAttribute("noOfDays",1000);
			} else if (StrRoleId.equals("2") && StrSiteId.equals("102")) {
				model.addAttribute("noOfDays", 14);
			} else if (StrRoleId.equals("4")) {
				model.addAttribute("noOfDays", 30);
			} else if (StrRoleId.equals("5")) {
				model.addAttribute("noOfDays", 100);
			} else if (StrRoleId.equals("1")) {
				model.addAttribute("noOfDays", 10);
			} else if (StrRoleId.equals("12")) {
				model.addAttribute("noOfDays", 20);
			} else if (StrRoleId.equals("13")) {
				model.addAttribute("noOfDays", 20);
			} else {
				model.addAttribute("noOfDays", 0);
			}
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			String poNumber = request.getParameter("poNumber");
			int count = irs.checkPOisActive(poNumber);
			if(count==0){
				model.addAttribute("listOfPOs",irs.getListOfActivePOs(String.valueOf(site_id)));
				model.addAttribute("errorMessage","Invalid PO Number");
				SaveAuditLogDetails.auditLog("0",user_id,"Inwards From PO","success",String.valueOf(site_id));
				return "ShowPOListToConvertToInvoice";
			}

			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));


			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());

			ProductDetails objProductDetails = new ProductDetails();
			model.addAttribute("CreatePOModelForm", objProductDetails);
			
			model.addAttribute("siteId",StrSiteId);
			model.addAttribute("Allsites",allSites);
	model.addAttribute("url","inwardsFromPO.spring");

			List<ProductDetails> IndentDetails = null;
			model.addAttribute("poNumber",poNumber);
			String reqSiteId = String.valueOf(site_id);
			model.addAttribute("productsMap", dcFormService.loadProdsByPONumber(poNumber,reqSiteId));
			List<ProductDetails> poDetails = null;
			if(preparedBy.equals("MARKETING_DEPT")){
				poDetails = irs.getMarketingPODetails(poNumber,reqSiteId);
			}
			else{
				poDetails = irs.getPODetails(poNumber,reqSiteId);
			}
			String strPoEntryId = poDetails.get(0).getStrPoEntryId();
			boolean isThisPOGoingToBeCanceled = objPurchaseDeptIndentrocess.isThisPOGoingToBeCanceled(strPoEntryId);
			if(isThisPOGoingToBeCanceled){
				model.addAttribute("response1"," This PO is Going To Be Canceled in Approvals. ");
				return "response";
			}
			model.addAttribute("poDetails",poDetails);
			
			List<ProductDetails>  ProductDetailslist = irs.getProductDetailsLists(poNumber,reqSiteId);
			List<ProductDetails>  transportChargesList = irs.getTransChrgsDtls(poNumber,reqSiteId);

			model.addAttribute("listOfGetProductDetails",ProductDetailslist);
			model.addAttribute("listOfTransChrgsDtls",transportChargesList);

			List<MarketingDeptBean>  POProductLocationDetails = irs.getPOProductLocationDetails(poNumber);
			if(POProductLocationDetails!=null && POProductLocationDetails.size()>0){
				model.addAttribute("LocationDetails",true);
			}
			model.addAttribute("POProductLocationDetails",POProductLocationDetails);
			

			//List<ProductDetails>  list = irs.getProductDetailsLists(poNumber,reqSiteId);
			double productTotalAmount = 0.0;
			double totalAmount = 0.0;
			for(int i = 0;i<ProductDetailslist.size();i++){

				productTotalAmount = Double.valueOf(ProductDetailslist.get(i).getAmountAfterTax());
				totalAmount = totalAmount+productTotalAmount;
			}
			double transportIndividualAmount = 0.0;
			double transportTotalAmount = 0.0;
			for(int i = 0;i<transportChargesList.size();i++){

				transportIndividualAmount = Double.valueOf(transportChargesList.get(i).getAmountAfterTaxx1());
				transportTotalAmount = transportTotalAmount+transportIndividualAmount;
			}


			totalAmount = Double.parseDouble(new DecimalFormat("##.##").format(totalAmount));
			transportTotalAmount = Double.parseDouble(new DecimalFormat("##.##").format(transportTotalAmount));
			String strTotalVal=String.format("%.2f",((Double)(totalAmount+transportTotalAmount)));

			model.addAttribute("totalAmount",strTotalVal);

		} catch (Exception e) {
			e.printStackTrace();
		}



		SaveAuditLogDetails.auditLog("0",user_id,"Showing PO Detailss","Success",String.valueOf(site_id));
		if(preparedBy.equals("MARKETING_DEPT")){
			return "marketing/InwardsfromCreatePO";
		}else{
			return "InwardsfromCreatePO";
		}


	}

	@RequestMapping(value = "/convertPOtoInvoice", method = {RequestMethod.POST,RequestMethod.GET})
	public String convertPOtoInvoice(@ModelAttribute("CreatePOModelForm")ProductDetails objProductDetails, BindingResult result, Model model, HttpServletRequest request, HttpSession session, @RequestParam(value="file",required=false) MultipartFile[] files,RedirectAttributes redir) throws IllegalStateException, IOException {
		String viewToBeSelected = "";
		String response ="";
		String site_id ="";
		String strValue="";
		String poNumber=request.getParameter("poNo")==null ? "" : request.getParameter("poNo");
		String vendorId=request.getParameter("VendorId")==null ? "" : request.getParameter("VendorId");
		if(poNumber!=null && !poNumber.equals("")){
		strValue=getAndCheckReceiveBOQQuantity(request,session);
		if(!strValue.contains("BOQ")){
		String response1 = irs.processingPOasInvoice(model, request, session);
		String serverFile="";
		int imgCount=0;
		int pdfCount=0;
		//-----------------------
		String[] response_array = response1.split("@@");
		 response = response_array [0];
		 site_id = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("urlName","inwardsFromPO.spring");

		String imgname = response_array [1];//"vname_invoiceno_entryid";
		imgname = imgname.replace("/","$$");
		String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
		//String imgname = vendorName+"_"+invoiceNumber+"_"+strIndentEntryId;
		//GetInvoiceDetailsController.loadInvoiceImgAndPdfFiles(rootFilePath,imgname,site_id,model,request);
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
		/*for (int i = 0,j = 0; i < files.length; i++) {
			MultipartFile multipartFile = files[i];



			if (response.equalsIgnoreCase("Success")&&!multipartFile.isEmpty()) {
				try {

					String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
					File dir = new File(rootFilePath+site_id);
					if (!dir.exists())
						dir.mkdirs();

					String filePath = dir.getAbsolutePath()
					+ File.separator + imgname+"_Part"+j+".jpg"; 
					j++;
					multipartFile.transferTo(new File(filePath));


					//System.out.println("Image Uploaded");
					//return "You successfully uploaded file" ;
				} catch (Exception e) {
					System.out.println("Image NOT Uploaded");
					//return "You failed to upload " ;
				}
			} else {
				//return "You failed to upload " + " because the file was empty.";
			}

		}*/

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
			if(request.getAttribute("hasCreditNote")==null){
				viewToBeSelected  = "viewGRN";
			}
			else{
				String indentEntryId = request.getAttribute("indentEntryNo").toString();
				new InwardGetInvoiceDetailsDaoImpl().getGrnViewDetails1(model,indentEntryId,request);
				//return "ViewGrnButtonsForInvoice"; //"viewGRN";"
				String poEntryId = request.getParameter("poEntryId");
				
				return "redirect:/getGrnAndCreditNoteGrnViewDetails.spring?indentEntryId="+indentEntryId+"&poEntryId="+poEntryId;
				
			}
		}
		else if(response.equalsIgnoreCase("Failed")){
			viewToBeSelected = "indentReceiveResponse";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			request.setAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}
		
		} // this is for if end
		else{
			model.addAttribute("message1",strValue);
			return "response";
		}
		
		}// end if
		else{
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		SaveAuditLogDetails audit=new SaveAuditLogDetails();


		String user_id=String.valueOf(session.getAttribute("UserId"));

		String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));

		audit.auditLog(indentEntrySeqNum,user_id,"Converting PO to Invoice ",response,site_id);

		return viewToBeSelected;
	}
	@RequestMapping(value = "/getGrnAndCreditNoteGrnViewDetails", method = RequestMethod.GET)
	public String getGrnAndCreditNoteGrnViewDetails(Model model, HttpServletRequest request, HttpSession session) {
		String viewToBeSelected = "";
		String indentEntryId = request.getParameter("indentEntryId");
		new InwardGetInvoiceDetailsDaoImpl().getGrnViewDetails1(model,indentEntryId,request);
		viewToBeSelected  = "ViewGrnButtonsForInvoice"; 
		return viewToBeSelected;
	}
	
	@RequestMapping(value = "/getCreditNoteGrn.spring", method = {RequestMethod.GET,RequestMethod.POST}/*RequestMethod.POST*/)
	public String getCreditNoteGrn(HttpServletRequest request, HttpSession session,Model model){
		
		irs.getCreditNoteDetails(request, model);
		return "CreditNotePrintPage";//"DummyCreditNoteGrn";
	}
	
	/*********************************************** this is for receiving the materials quantity time it is using start*****************************/
	@RequestMapping(value = "/getAndCheckReceiveBOQQuantity", method ={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String getAndCheckReceiveBOQQuantity(HttpServletRequest request,HttpSession session) {
		String response="";
		String site_id=request.getParameter("toSiteId")== null ? "" : request.getParameter("toSiteId"); // check the boq quantity purpose written this one
		String indentAllowSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		//List<String> List=Arrays.asList(indentAllowSites.split(","));
		if(!indentAllowSites.contains(site_id)){
		response=irs.getAndCheckReceiveBOQQuantity(session,request,site_id);
			
		}
		return response;//ics.loadSubProds(mainProductId);
	}

}
