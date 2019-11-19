package com.sumadhura.in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.sumadhura.bean.ContractorTaxReportBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.PurchaseTaxReportBean;
import com.sumadhura.bean.SiteDetails;
import com.sumadhura.service.PaymentProcessService;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.transdao.PaymentProcessDaoImpl;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DownloadExcel;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class PaymentProcessController extends UIProperties {

	@Autowired
	PaymentProcessService objPaymentProcessService;
	@Autowired
	CommonUtilities objCommonUtilities;


	@RequestMapping(value = "/IntiatePayment.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView intiatePayment( HttpServletRequest request,HttpSession session) {
		
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		List<PaymentBean> listTotalInvoices = null;
		try {
			model = new ModelAndView();
			String site_id = String.valueOf(session.getAttribute("SiteId"));				
			String user_id = String.valueOf(session.getAttribute("UserId"));

			if(!user_id.equals("1029")){
				//checking user is authorized for initiate Payment or not.
				String pendingEmpId = objPaymentProcessService.getPendingEmpId(user_id,site_id);
				if(pendingEmpId.equals("-")){
					model.addObject("message1","You cannot Intiate Payment");
					model.setViewName("response");
					return model;
				}
			}
			
			//setting request parameters to Map object.
			Map<String,String> reqParamsMap = getRequestParametersMap(request,"fromDate","toDate","InvoiceNumber","vendorName","siteIdAndName","paymentReqDatefrom","paymentReqDateto"); //add new JSP input text box name here
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			String ispaymentReqDateChecked = request.getParameter("hiddenpaymentReqDateCheck")==null?"":request.getParameter("hiddenpaymentReqDateCheck").toString();
			String paymentReqDatefrom = request.getParameter("paymentReqDatefrom")==null?"":request.getParameter("paymentReqDatefrom").toString();
			String paymentReqDateto = request.getParameter("paymentReqDateto")==null?"":request.getParameter("paymentReqDateto").toString();
			boolean isItFirstRequest = false;
			// If purchase dept login, show sitewise selection in JSP.
			List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
			request.setAttribute("allSitesList", allSitesList);
			String purcaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			request.setAttribute("showDropdownSite", (site_id.equals(purcaseDeptId)?true:false));
			
			
			int portNumber=request.getLocalPort();
			String strPOPath = "";
			String strInvoicePath = "";
			if(portNumber == 8078){ 
				//path="C:\\testing\\MailTemplates\\reject.html";
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_UAT");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_UAT");										
			}
			else if(portNumber == 8079){ 
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_CUG");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_CUG");			
			}
			else if(portNumber == 80){ 
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_LIVE");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_LIVE");
			}
			model.addObject("POPATH",strPOPath);
			model.addObject("INVOICEPATH",strInvoicePath);
			
			//checking request is empty or not
			if (isDataInRequest(request)||selectAll.equals("true")||(ispaymentReqDateChecked.equals("true")&&(StringUtils.isNotBlank(paymentReqDatefrom)||StringUtils.isNotBlank(paymentReqDateto)))) {
				session = request.getSession(false);
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					listTotalInvoices = objPaymentProcessService.getInvoiceDetails( site_id,reqParamsMap,selectAll,ispaymentReqDateChecked,false);
					model.addObject("isItFirstRequest", false);
					if(listTotalInvoices != null && listTotalInvoices.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					
					
					model.addObject("listTotalInvoicesSize",listTotalInvoices.size());
					model.addObject("listTotalInvoices",listTotalInvoices);
					model.addObject("TotalInvoiceAmount",reqParamsMap.get("TotalInvoiceAmount"));
					model.addObject("TotalTillReqAmount",reqParamsMap.get("TotalTillReqAmount"));
					model.addObject("TotalPaidAmount",reqParamsMap.get("TotalPaidAmount"));
					addObjectsToModel(model,request);//keep user submitted data unchanged in result page
					model.setViewName("payment/GetInvoiceDetailsForIntiatePayment");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			} else {
				listTotalInvoices = objPaymentProcessService.getInvoiceDetails( site_id,reqParamsMap,selectAll,ispaymentReqDateChecked,true);
				model.addObject("isItFirstRequest", true);
				if(listTotalInvoices != null && listTotalInvoices.size() >0){
					request.setAttribute("showGrid", "true");
				}
				model.addObject("listTotalInvoicesSize",listTotalInvoices.size());
				model.addObject("displayErrMsg", "Please Select From Date or To Date or InvoiceNumber!");
				model.addObject("listTotalInvoices",listTotalInvoices);
				model.addObject("TotalInvoiceAmount",reqParamsMap.get("TotalInvoiceAmount"));
				model.addObject("TotalTillReqAmount",reqParamsMap.get("TotalTillReqAmount"));
				model.addObject("TotalPaidAmount",reqParamsMap.get("TotalPaidAmount"));
				addObjectsToModel(model,request);//keep user submitted data unchanged in result page
				model.setViewName("payment/GetInvoiceDetailsForIntiatePayment");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return  model;

		//return "payment/payment";


	}

//keep user submitted data unchanged in result page
	private void addObjectsToModel(ModelAndView model, HttpServletRequest request) {
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		model.addObject("fromDate",request.getParameter("fromDate"));
		model.addObject("toDate", request.getParameter("toDate"));
		model.addObject("paymentReqDatefrom",request.getParameter("paymentReqDatefrom"));
		model.addObject("paymentReqDateto", request.getParameter("paymentReqDateto"));
		model.addObject("invoiceNumber",request.getParameter("InvoiceNumber"));
		model.addObject("vendorName", request.getParameter("vendorName"));
		model.addObject("workOrderNo",request.getParameter("workOrderNo"));
		model.addObject("contractorName", request.getParameter("contractorName"));
		String SiteIdAndName = request.getParameter("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)){
			if(!SiteIdAndName.equals("@@")){
				dropdown_SiteId = SiteIdAndName.split("@@")[0];
				dropdown_SiteName = SiteIdAndName.split("@@")[1];
			}
		}
		model.addObject("site_id",dropdown_SiteId);
		model.addObject("site_name", dropdown_SiteName);
		
	}
	//setting request parameters to Map object.
	public Map<String,String> getRequestParametersMap(HttpServletRequest request,String... inputTextNames) {
		Map<String,String> reqParamsMap = new HashMap<String,String>();
	    for (String name : inputTextNames) {
	    	reqParamsMap.put(name, request.getParameter(name));
	    }
		return reqParamsMap;
	}
	public boolean isDataInRequest(HttpServletRequest request){
		boolean IsDataInRequest = false;
		Map<?, ?> params = request.getParameterMap();
		Iterator<?> i = params.keySet().iterator();
		while ( i.hasNext() )
		{
			String key = (String) i.next();
			String value = ((String[]) params.get( key ))[ 0 ];
			if(StringUtils.isNotBlank(value)){IsDataInRequest = true;break;}
		}
		return IsDataInRequest;
	}
	@RequestMapping(value = "/savePaymentIntiateDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView savePaymentIntiateDetails( HttpServletRequest request,HttpSession session) {
		
		ModelAndView modelAndView = new ModelAndView();
		List<String> successList = objPaymentProcessService.savePaymentIntiateDetails(request, session);

		modelAndView.addObject("successList",successList);
		if(successList.size()==0){
			modelAndView.addObject("message1", "No Payments Selected");
		}
		modelAndView.setViewName("payment/PaymentResponse");
		modelAndView.addObject("pageHighlightURL","IntiatePayment.spring");
		return modelAndView;

	}
	@RequestMapping(value = "/savePaymentIntiateDetailsOnPOAdvance.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView savePaymentIntiateDetailsOnPOAdvance( HttpServletRequest request,HttpSession session) {
		
		ModelAndView modelAndView = new ModelAndView();
		List<String> successList = objPaymentProcessService.savePaymentIntiateDetailsOnPoAdvance(request, session);

		modelAndView.addObject("successList",successList);
		if(successList.size()==0){
			modelAndView.addObject("message1", "No Payments Selected");
		}
		modelAndView.setViewName("payment/PaymentResponse");
		modelAndView.addObject("pageHighlightURL","IntiatePaymentFromPO.spring");
		return modelAndView;

	}
	@RequestMapping(value = "/PaymentForApproval.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView paymentForApproval( HttpServletRequest request,HttpSession session) {
		
		String site_id = "";
		
		ModelAndView model = null;
		List<PaymentBean> listTotalInvoices = null;
		try {
				model = new ModelAndView();
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				
				if (StringUtils.isNotBlank(site_id)) {
					listTotalInvoices = new PaymentProcessDaoImpl().getPaymentApprovalDetails(site_id,user_id);
					if(listTotalInvoices != null && listTotalInvoices.size() >0){
						request.setAttribute("showGrid", "true");
					}
					int size=0;
					for(PaymentBean element:listTotalInvoices){
						if(!element.isVendorHeader()){size++;}
					}
					
					
					int portNumber=request.getLocalPort();
					String strPOPath = "";
					String strInvoicePath = "";
					if(portNumber == 8078){ 
						//path="C:\\testing\\MailTemplates\\reject.html";
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_UAT");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_UAT");										
					}
					else if(portNumber == 8079){ 
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_CUG");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_CUG");			
					}
					else if(portNumber == 80){ 
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_LIVE");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_LIVE");
					}
					
					
					model.addObject("listTotalInvoicesSize",size);
					model.addObject("listTotalInvoices",listTotalInvoices);
					model.addObject("POPATH",strPOPath);
					model.addObject("INVOICEPATH",strInvoicePath);
					
					model.setViewName("payment/PaymentApprovalSiteLevel");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			
		}
			catch (Exception ex) {
				ex.printStackTrace();
			} 
	
	return model;
	}
	@RequestMapping(value = "/PaymentApprovalForAccDept.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView paymentApprovalForAccDept( HttpServletRequest request,HttpSession session) {
		
		String site_id = "";
		
		ModelAndView model = null;
		List<PaymentBean> listofPendingPayments = null;
		List<PaymentModesBean> PaymentModes = new ArrayList<PaymentModesBean>();
		try {
				model = new ModelAndView();
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				String rollIdForInvoiceTaxSubmit = UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit") == null ? "00" : UIProperties.validateParams.getProperty("rollIdForInvoiceTaxSubmit").toString();
				String StrRoleId = String.valueOf(session.getAttribute("Roleid"));	
				model.addObject("rollIdForInvoiceTaxSubmit",rollIdForInvoiceTaxSubmit);
				model.addObject("StrRoleId",StrRoleId);
				model.addObject("firstRequest", false);
				
				if (StringUtils.isNotBlank(site_id)) {
					PaymentModes = new PaymentProcessDaoImpl().getPaymentModes();
					model.addObject("PaymentModes",PaymentModes);
					
					String PendingEmpId  = new PaymentProcessDaoImpl().getApproverEmpIdInAccounts(user_id);
					model.addObject("PendingEmpId",PendingEmpId);
					
					//listofPendingPayments = new PaymentProcessDaoImpl().getAccDeptPaymentApprovalDetails(site_id,user_id);
					listofPendingPayments = objPaymentProcessService.getAccDeptPaymentApprovalDetails(site_id,user_id,request,model);
					if(listofPendingPayments != null && listofPendingPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} 
					int size=0;
					for(PaymentBean element:listofPendingPayments){
						if(!element.isVendorHeader()){size++;}
					}
					
					
					int portNumber=request.getLocalPort();
					String strPOPath = "";
					String strInvoicePath = "";
					if(portNumber == 8078){ 
						//path="C:\\testing\\MailTemplates\\reject.html";
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_UAT");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_UAT");										
					}
					else if(portNumber == 8079){ 
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_CUG");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_CUG");			
					}
					else if(portNumber == 80){ 
						strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_LIVE");
						strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_LIVE");
					}
					
					model.addObject("listTotalInvoicesSize",size);
					model.addObject("listTotalInvoices",listofPendingPayments);
					model.addObject("POPATH",strPOPath);
					model.addObject("INVOICEPATH",strInvoicePath);
					
					/*String approverEmpId = new PaymentProcessDaoImpl().getApproverEmpIdInAccounts(user_id);
					if(approverEmpId.equals("VND")){
						model.addObject("isShowPaymentDoneDate","true");
					}*/
					
					model.setViewName("payment/PaymentApprovalAccountLevel");//AccDeptPaymentForApproval

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			
		}
			catch (Exception ex) {
				ex.printStackTrace();
			} 
	
	return model;
	}
	
	
	@RequestMapping(value = "/savePaymentApprovalDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String  savePaymentApprovalDetails(HttpServletRequest request,HttpSession session,Model model) {
		
		String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
	//	String site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	

		List<String> successList = objPaymentProcessService.savePaymentApprovalAndRejectDetails(request,session,strUserId);
		
		model.addAttribute("successList",successList);
		if(successList.size()==0){
			model.addAttribute("message1", "No Payments Selected");
		}
		model.addAttribute("pageHighlightURL","PaymentForApproval.spring");
		return "payment/PaymentResponse";

	}
	
	@RequestMapping(value = "/IntiatePaymentFromPO.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView paymentFromPO( HttpServletRequest request,HttpSession session) {


		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String vendorName = "";
		String poNumber = "";
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		ModelAndView model = null;
		List<PaymentBean> listTotalPOs = null;
		try {
			model = new ModelAndView();
			model.addObject("firstRequest", false);
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			vendorName = request.getParameter("vendorName");
			poNumber = request.getParameter("PONumber");
			String SiteIdAndName = request.getParameter("siteIdAndName");
			if(StringUtils.isNotBlank(SiteIdAndName)){
				if(!SiteIdAndName.equals("@@")){
					dropdown_SiteId = SiteIdAndName.split("@@")[0];
					dropdown_SiteName = SiteIdAndName.split("@@")[1];
				}
			}
			//setting request parameters to Map object.
			Map<String,String> reqParamsMap = getRequestParametersMap(request,"fromDate","toDate","PONumber","vendorName","siteIdAndName"); //add new JSP input text box name here
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
			String purcaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(poNumber) || StringUtils.isNotBlank(vendorName) || StringUtils.isNotBlank(dropdown_SiteId)||selectAll.equals("true")) {
				session = request.getSession(false);
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					listTotalPOs = objPaymentProcessService.getPODetails( site_id, reqParamsMap,selectAll);
					if(listTotalPOs != null && listTotalPOs.size() >0){
						request.setAttribute("showGridForPO", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					model.addObject("listTotalPOsSize",listTotalPOs.size());
					model.addObject("listTotalPOs",listTotalPOs);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("poNumber",poNumber);
					model.addObject("vendorName", vendorName);
					model.addObject("site_id",dropdown_SiteId);
					model.addObject("site_name", dropdown_SiteName);
					model.addObject("TotalPOAmount",reqParamsMap.get("TotalPOAmount"));
					model.addObject("TotalTillReqAmount",reqParamsMap.get("TotalTillReqAmount"));
					model.addObject("TotalPaidAmount",reqParamsMap.get("TotalPaidAmount"));
					List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					request.setAttribute("showDropdownSite", (site_id.equals(purcaseDeptId)?true:false));
					model.setViewName("payment/GetPODetailsForIntiatePayment");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			} else {
				model.addObject("firstRequest", true);
				model.addObject("displayErrMsg", "Please Select From Date or To Date or PONumber!");
				model.addObject("listTotalPOs",listTotalPOs);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("poNumber",poNumber);
				model.addObject("vendorName", vendorName);
				model.addObject("site_id",dropdown_SiteId);
				model.addObject("site_name", dropdown_SiteName);
				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("showDropdownSite", (site_id.equals(purcaseDeptId)?true:false));
				model.setViewName("payment/GetPODetailsForIntiatePayment");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return  model;

		//return "payment/payment";


	}
	
	@RequestMapping(value = "/getAccountDeptPendingRequests.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView  getAccountDeptPendingRequests(HttpServletRequest request,HttpSession session,ModelAndView model) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<PaymentModesBean> PaymentModes = new ArrayList<PaymentModesBean>();
		
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();


			PaymentModes = new PaymentProcessDaoImpl().getPaymentModes();
			model.addObject("PaymentModes",PaymentModes);
			model.addObject("firstRequest", false);
			
			list = objPaymentProcessService.getAccDeptPaymentPendingDetails(strUserId,request,model);

			if(list != null && list.size() >0){
				model.addObject("showSubmit",true);
			} else {
				model.addObject("showSubmit",false);
			}
			model.addObject("listTotalInvoices",list);
			
			int size=0;
			for(PaymentBean element:list){
				if(!element.isVendorHeader()){size++;}
			}
			
			
			int portNumber=request.getLocalPort();
			String strPOPath = "";
			String strInvoicePath = "";
			if(portNumber == 8078){ 
				//path="C:\\testing\\MailTemplates\\reject.html";
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_UAT");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_UAT");										
			}
			else if(portNumber == 8079){ 
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_CUG");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_CUG");			
			}
			else if(portNumber == 80){ 
				strPOPath=validateParams.getProperty("PAYMENTS_PO_PATH_LIVE");
				strInvoicePath=validateParams.getProperty("PAYMENTS_INVOICE_PATH_LIVE");
			}
			
			model.addObject("POPATH",strPOPath);
			model.addObject("INVOICEPATH",strInvoicePath);
			model.addObject("listTotalInvoicesSize",size);
			model.setViewName("payment/PaymentPendingAccountLevel");//AccDeptPaymentPending

		}catch(Exception e){
			e.printStackTrace();
			model.addObject("listofPendingPayments",list);
			model.setViewName("payment/PaymentPendingAccountLevel");

		}

		return model;

	}
	
	@RequestMapping(value = "/createAccDeptPaymentTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView  createAccDeptPaymentTransaction(HttpServletRequest request,HttpSession session,ModelAndView model) {
		
		//List<PaymentBean> list = new ArrayList<PaymentBean>();
		ModelAndView modelAndView = new ModelAndView();
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			


			List<String> successList = objPaymentProcessService.createAccountDeptTransaction(request,strUserId,user_name,session);

			modelAndView.addObject("successList",successList);
			if(successList.size()==0){
				modelAndView.addObject("message1", "No Payments Selected");
			}
			modelAndView.setViewName("payment/PaymentResponse");
			modelAndView.addObject("pageHighlightURL","getAccountDeptPendingRequests.spring");
			
		}catch(Exception e){
			e.printStackTrace();

		}

		return modelAndView;

	}
	
	
	private static final String APPLICATION_TEXT = "application/zip";

	@RequestMapping(value = "/generatePaymentTextFile.spring", method = RequestMethod.GET, produces = APPLICATION_TEXT)
	public @ResponseBody String generateTextFile(HttpServletResponse response, HttpServletRequest request,
			HttpSession session) {
		//System.out.println("PaymentProcessController.generateTextFile()");
		//String str = request.getParameter("generateFileifTrue");
		//boolean flag = Boolean.valueOf(str);
		try {
			String zipFileName = "PaymentFiles.zip";
			FileInputStream fileInputStream = new FileInputStream(zipFileName);
			BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
			
			File file = new File(zipFileName);
			// Set the content type based to zip
			request.setAttribute("refreshOneTime", "true");
			response.setContentType(APPLICATION_TEXT);
			response.setHeader("Content-Disposition","attachment; filename=PaymentFile.zip");
			FileCopyUtils.copy(bufferedInputStream, response.getOutputStream());
			boolean flag1=file.delete();
			//System.out.println("file deleted on Controller "+flag1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}

		session.removeAttribute("requestedDatePaymentMap");
		return "payment/AccntPrint";
	}
	
	@RequestMapping(value = "/updateAccDeptPaymentTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView  updateAccDeptPaymentTransaction(HttpServletRequest request,HttpServletResponse response,HttpSession session,ModelAndView model) {
		
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			
			

			list=objPaymentProcessService.updateAccountDeptTransaction(request,response,strUserId,user_name,session);

			
			if(list != null && list.size() >0){
				request.setAttribute("showGrid", "true");
			}
			boolean flag = Boolean.valueOf(String.valueOf(request.getAttribute("isDownload")));
			//set this object to session for downloading the files
			Map<String,String> requestedDateMap =(Map<String, String>) request.getAttribute("requestedDatePaymentList");
			session.setAttribute("requestedDatePaymentMap", requestedDateMap);
			if (flag) {
				//System.out.println("File is downloading \n");
				model.addObject("generateFile", true);
			}
			model.addObject("listofPendingPayments",list);
			
			model.addObject("listTotalPaymentReqSize",list.size());
			if(list.size()>0){
				model.setViewName("payment/AccntPrint");
			}
			else {
				model.setViewName("payment/PaymentResponse");
				model.addObject("pageHighlightURL","PaymentApprovalForAccDept.spring");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}

		return model;

	}
	
	//this is for site level
	@RequestMapping(value ="viewMyTransactionDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewMyTransactionDetails(HttpServletRequest request,HttpSession session) {

		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		//String response="";
		String vendorId = "";
		String site_id = "";
		String dropdown_SiteId="";
		String dropdown_SiteName="";
		String vendorName = "";
		String invoiceNumber = "";
		String poNumber = "";
		
		List<PaymentBean> listOfPayments = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
		/*	String SiteIdAndName = request.getParameter("siteIdAndName");
			if(StringUtils.isNotBlank(SiteIdAndName)){
				if(!SiteIdAndName.equals("@@")){
					dropdown_SiteId = SiteIdAndName.split("@@")[0];
					dropdown_SiteName = SiteIdAndName.split("@@")[1];
				}
			}*/
			invoiceNumber = request.getParameter("invoiceNumber");
			poNumber = request.getParameter("poNumber");
			vendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(vendorId)|| StringUtils.isNotBlank(invoiceNumber)|| StringUtils.isNotBlank(poNumber)|| StringUtils.isNotBlank(dropdown_SiteId)||selectAll.equals("true")) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				
				if (StringUtils.isNotBlank(site_id))  {
					listOfPayments = new PaymentProcessDaoImpl().getViewPaymentDetails(fromDate, toDate,vendorId,user_id,site_id,invoiceNumber,poNumber,selectAll);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						//response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("vendorId",vendorId);
					model.addObject("vendorName",vendorName);
					model.addObject("invoiceNumber",invoiceNumber);
					model.addObject("poNumber", poNumber);
					model.addObject("site_id",dropdown_SiteId);
					model.addObject("site_name", dropdown_SiteName);
					List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					model.setViewName("payment/GetTransactionDetails");
					//response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					//response="failed";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfPayments",listOfPayments);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("vendorId",vendorId);
				model.addObject("vendorName",vendorName);
				model.addObject("invoiceNumber",invoiceNumber);
				model.addObject("poNumber", poNumber);
				
				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				model.setViewName("payment/GetTransactionDetails");
				//response="failed";
			}
		} catch (Exception ex) {
			//response="failed";
			ex.printStackTrace();
		} 

		

		return model;
	}
	
	
	//this is for department level
	@RequestMapping(value ="viewSiteWiseTransactionDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewSiteWiseTransactionDetails(HttpServletRequest request,HttpSession session) {

		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String response="";
		String vendorId = "";
		String site_id = "";
		String dropdown_SiteId="";
		String dropdown_SiteName="";
		String vendorName = "";
		String invoiceNumber = "";
		String poNumber = "";
		
		List<PaymentBean> listOfPayments = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			String SiteIdAndName = request.getParameter("siteIdAndName");
			if(StringUtils.isNotBlank(SiteIdAndName)){
				if(!SiteIdAndName.equals("@@")){
					dropdown_SiteId = SiteIdAndName.split("@@")[0];
					dropdown_SiteName = SiteIdAndName.split("@@")[1];
				}
			}
			invoiceNumber = request.getParameter("invoiceNumber");
			poNumber = request.getParameter("poNumber");
			vendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(vendorId)|| StringUtils.isNotBlank(invoiceNumber)|| StringUtils.isNotBlank(poNumber)|| StringUtils.isNotBlank(dropdown_SiteId)||selectAll.equals("true")) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
				
				if (StringUtils.isNotBlank(site_id))  {
					listOfPayments = new PaymentProcessDaoImpl().getViewPaymentDetails(fromDate, toDate,vendorId,user_id,dropdown_SiteId,invoiceNumber,poNumber,selectAll);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("vendorId",vendorId);
					model.addObject("vendorName",vendorName);
					model.addObject("invoiceNumber",invoiceNumber);
					model.addObject("poNumber", poNumber);
					model.addObject("site_id",dropdown_SiteId);
					model.addObject("site_name", dropdown_SiteName);
					List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					model.setViewName("payment/GetSiteWiseTransactionDetails");
					response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failed";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfPayments",listOfPayments);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("vendorId",vendorId);
				model.addObject("vendorName",vendorName);
				model.addObject("invoiceNumber",invoiceNumber);
				model.addObject("poNumber", poNumber);
				
				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				model.setViewName("payment/GetSiteWiseTransactionDetails");
				response="failed";
			}
		} catch (Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"get invoice Details View data",response,site_id1);

		return model;
	}
	
	@RequestMapping(value ="viewMyPayment.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewMyPaymentRequest(HttpServletRequest request,HttpSession session) {

		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String response="";
		String site_id = "";
		String vendorId = "";
		String vendorName = "";
		String invoiceNumber = "";
		String poNumber = "";
		List<PaymentBean> listOfPayments = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			invoiceNumber = request.getParameter("invoiceNumber");
			poNumber = request.getParameter("poNumber");
			vendorId = request.getParameter("vendorId");
			vendorName = request.getParameter("vendorName");
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(vendorId)|| StringUtils.isNotBlank(invoiceNumber)|| StringUtils.isNotBlank(poNumber)||selectAll.equals("true")) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				if (StringUtils.isNotBlank(site_id)) {
					listOfPayments = new PaymentProcessDaoImpl().viewMyPayment(fromDate, toDate, site_id, user_id,vendorId,invoiceNumber,poNumber,selectAll);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("vendorId",vendorId);
					model.addObject("vendorName",vendorName);
					model.addObject("invoiceNumber",invoiceNumber);
					model.addObject("poNumber", poNumber);
					String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
					String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
					request.setAttribute("isAccounts", (site_id.equals(accDeptId)||site_id.equals(purchaseDeptId))?true:false);
					
					model.setViewName("payment/ViewMyPayment");
					response="success";

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failed";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfPayments",listOfPayments);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.addObject("vendorId",vendorId);
				model.addObject("vendorName",vendorName);
				model.addObject("invoiceNumber",invoiceNumber);
				model.addObject("poNumber", poNumber);
				model.setViewName("payment/ViewMyPayment");
				response="failed";
			}
		} catch (Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"get invoice Details View data",response,site_id1);




		return model;
	}

	

	
	
	
	
	@RequestMapping(value = "/updatePaymentDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updatePaymentDetails( HttpServletRequest request,HttpSession session) {
		String site_id = "";
		String fromDate = "";
		String toDate = "";
		String vendorId = "";
		String vendorName = "";
		String vendorAddress = "";
		String invoiceNumber = "";
		String poNumber = "";
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		ModelAndView model = null;
		List<PaymentBean> listofPendingPayments = new ArrayList<PaymentBean>();
		String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
		try {
				model = new ModelAndView();
				model.addObject("firstRequest", false);
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				invoiceNumber = request.getParameter("invoiceNumber");
				poNumber = request.getParameter("poNumber");
				String SiteIdAndName = request.getParameter("siteIdAndName");
				if(StringUtils.isNotBlank(SiteIdAndName)){
					if(!SiteIdAndName.equals("@@")){
						dropdown_SiteId = SiteIdAndName.split("@@")[0];
						dropdown_SiteName = SiteIdAndName.split("@@")[1];
					}
				}
				vendorId = request.getParameter("vendorId");
				vendorName = request.getParameter("vendorName");
				vendorAddress = request.getParameter("vendorAddress");
				
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				
				if (StringUtils.isNotBlank(site_id)) {
					if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(vendorId)|| StringUtils.isNotBlank(invoiceNumber)|| StringUtils.isNotBlank(poNumber)|| StringUtils.isNotBlank(dropdown_SiteId)||selectAll.equals("true")) {
						listofPendingPayments = new PaymentProcessDaoImpl().getAccDeptPaymentDetailsToUpdate(site_id,user_id,fromDate,toDate,vendorId,invoiceNumber,poNumber,dropdown_SiteId,selectAll);

						if(listofPendingPayments != null && listofPendingPayments.size() >0){
							request.setAttribute("showGrid", "true");
						} 
						else {
							model.addObject("succMessage","The above Dates Data Not Available");
						}
						model.addObject("listTotalPaymentReqSize",listofPendingPayments.size());
						model.addObject("listofPendingPayments",listofPendingPayments);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.addObject("vendorId",vendorId);
						model.addObject("vendorName",vendorName);
						model.addObject("vendorAddress",vendorAddress);
						model.addObject("invoiceNumber",invoiceNumber);
						model.addObject("poNumber", poNumber);
						model.addObject("site_id",dropdown_SiteId);
						model.addObject("site_name", dropdown_SiteName);
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/AccDeptPaymentUpdate");
					}
					else{
						model.addObject("firstRequest", true);
						model.addObject("displayErrMsg", "Please Select From Date or To Date!");
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/AccDeptPaymentUpdate");
					}
				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			
		}
			catch (Exception ex) {
				ex.printStackTrace();
			} 
	
	return model;
	
	}
	
	@RequestMapping(value = "/updateRefNoInAccDeptTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView  updateRefNoInAccDeptTransaction(HttpServletRequest request,HttpSession session,ModelAndView model) {
		//List<PaymentBean> list = new ArrayList<PaymentBean>();
		ModelAndView modelAndView = new ModelAndView();
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();



			List<String> successList=objPaymentProcessService.updateRefNoInAccDeptTransaction(request,strUserId,session);

			modelAndView.addObject("successList",successList);
			if(successList.size()==0){
				modelAndView.addObject("message1", "No Payments Updated");
			}
			modelAndView.setViewName("payment/PaymentResponse");
			modelAndView.addObject("pageHighlightURL","updatePaymentDetails.spring");
			/*if(strResponse.equals("Success")){

				modelAndView.addObject("message", "Payment Updated Successfully");
				modelAndView.setViewName("response");
			}else{
				modelAndView.addObject("message1", "Sorry, Payment Updation Failed");
				modelAndView.setViewName("response");
			}*/
			
		}catch(Exception e){
			e.printStackTrace();
			
		}

		return modelAndView;

	}
	@RequestMapping(value = "/searchPreviousPayments.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView searchPreviousPayments( HttpServletRequest request,HttpSession session) {
		
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		List<PaymentBean> listTotalInvoices = null;
		try {
			model = new ModelAndView();
			String site_id = String.valueOf(session.getAttribute("SiteId"));				
			//String user_id = String.valueOf(session.getAttribute("UserId"));
			
			//setting request parameters to Map object.
			Map<String,String> reqParamsMap = getRequestParametersMap(request,"fromDate","toDate","InvoiceNumber","vendorName","siteIdAndName"); //add new JSP input text box name here
			
			// If purchase dept login, show sitewise selection in JSP.
			List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
			request.setAttribute("allSitesList", allSitesList);
			//String purcaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			if(site_id.equals("997")){
				request.setAttribute("showDropdownSite", true);
			}
			
			
			//checking request is empty or not
			if (isDataInRequest(request)) {
				session = request.getSession(false);
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					listTotalInvoices = objPaymentProcessService.getInvoiceDetailsForOldPayments( site_id,reqParamsMap);
					if(listTotalInvoices != null && listTotalInvoices.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					model.addObject("listTotalInvoicesSize",listTotalInvoices.size());
					model.addObject("listTotalInvoices",listTotalInvoices);
					addObjectsToModel(model,request);//keep user submitted data unchanged in result page
					model.addObject("TotalInvoiceAmount",reqParamsMap.get("TotalInvoiceAmount"));
					model.addObject("TotalTillReqAmount",reqParamsMap.get("TotalTillReqAmount"));
					model.addObject("TotalPaidAmount",reqParamsMap.get("TotalPaidAmount"));
					model.setViewName("payment/SearchPreviousPayments");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or InvoiceNumber!");
				model.addObject("listTotalInvoices",listTotalInvoices);
				addObjectsToModel(model,request);//keep user submitted data unchanged in result page
				model.setViewName("payment/SearchPreviousPayments");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return  model;

		//return "payment/payment";


	}
	@RequestMapping(value = "/updatePreviousPayments.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updatePreviousPayments( HttpServletRequest request,HttpSession session) {
	
		ModelAndView modelAndView = new ModelAndView();
		List<String> successList = objPaymentProcessService.updatePreviousPayments(request);
		modelAndView.addObject("successList",successList);
		if(successList.size()==0){
			modelAndView.addObject("message1", "No Payments Selected");
		}
		modelAndView.setViewName("payment/PaymentResponse");
		modelAndView.addObject("pageHighlightURL","searchPreviousPayments.spring");
		return modelAndView;
	}
	
	/********************************************Contractor Bill Requset  Payment start*******************************************************************/
	@RequestMapping(value = "/ViewMyContractorBillRequest.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView ViewMyContractorBillRequest( HttpServletRequest request,HttpSession session) {
		
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		List<PaymentBean> listTotalList = null;
		try {
			model = new ModelAndView();
			String site_id = String.valueOf(session.getAttribute("SiteId"));				
			String user_id = String.valueOf(session.getAttribute("UserId"));
			//
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			if(!enableWOSubModulesSiteList.contains(site_id)){
				model.addObject("message1", "Hello "+strUserName+" As of now "+siteName+" Site can not access WORK ORDER & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			//
			//setting request parameters to Map object.
			Map<String,String> reqParamsMap = getRequestParametersMap(request,"fromDate","toDate","all","workOrderNo","contractorName"); //add new JSP input text box name here
			
		
			//checking request is empty or not
			if (isDataInRequest(request)) {
				session = request.getSession(false);
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					listTotalList = objPaymentProcessService.getContractorBillReqDetails(user_id,site_id,reqParamsMap);
					if(listTotalList != null && listTotalList.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						
					}
					model.addObject("listTotalInvoicesSize",listTotalList.size());
					model.addObject("listTotalInvoices",listTotalList);
					model.addObject("user_id",user_id);
					addObjectsToModel(model,request);//keep user submitted data unchanged in result page
					model.setViewName("payment/GetDetailsForIntiatePaymentForWorkOrder");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date or select option!");
				model.addObject("listTotalInvoices",listTotalList);
				addObjectsToModel(model,request);//keep user submitted data unchanged in result page
				model.setViewName("payment/GetDetailsForIntiatePaymentForWorkOrder");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"Get Contractpor Bill Viewed Details Viewed","success",site_id1);

		return  model;

		//return "payment/payment";


	}
	
	/**************************************************save Contractor For Payment data start*****************************************************/
	@RequestMapping(value = "/saveContractorBillPaymentDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String saveContractorBillPaymentDetails( HttpServletRequest request,HttpSession session,  RedirectAttributes redir) {

		/*ModelAndView modelAndView = new ModelAndView();
		List<String> successList = objPaymentProcessService.saveContractorBillPaymentDetails(request, session);

		modelAndView.addObject("successList",successList);
		if(successList.size()==0){
			modelAndView.addObject("message1", "No Payments Selected");
		}
		modelAndView.setViewName("payment/CntPaymentResponse");
		modelAndView.addObject("pageHighlightURL","ViewMyContractorBillRequest.spring");
		return modelAndView;*/
		
		List<String> successList = objPaymentProcessService.saveContractorBillPaymentDetails(request, session);

		redir.addFlashAttribute("successList",successList);
		if(successList.size()==0){
			redir.addFlashAttribute("message1", "No Payments Selected");
		}
		redir.addFlashAttribute("pageHighlightURL","ViewMyContractorBillRequest.spring");
		return "redirect:/CntPaymentResponse.spring";

	}
	@RequestMapping(value = "/CntPaymentResponse.spring",  method = RequestMethod.GET)
	public ModelAndView category(){
	    return new ModelAndView("payment/CntPaymentResponse");
	}
	
	@RequestMapping(value = "/getLocalPurchaseInvoiceDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getLocalPurchaseInvoiceDetails( HttpServletRequest request,HttpSession session) {
		
		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		List<PaymentBean> listTotalInvoices = null;
		try {
			model = new ModelAndView();
			model.addObject("firstRequest", false);
			String site_id = String.valueOf(session.getAttribute("SiteId"));				
			//String user_id = String.valueOf(session.getAttribute("UserId"));
			
			//setting request parameters to Map object.
			Map<String,String> reqParamsMap = getRequestParametersMap(request,"fromDate","toDate","InvoiceNumber","vendorName","siteIdAndName"); //add new JSP input text box name here
			String selectAll = request.getParameter("hiddenSelectAll")==null?"":request.getParameter("hiddenSelectAll").toString();
			
			// If purchase dept login, show sitewise selection in JSP.
			List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
			request.setAttribute("allSitesList", allSitesList);
			//String purcaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			/*if(site_id.equals("997")){
				request.setAttribute("showDropdownSite", false);
			}*/
			
			
			//checking request is empty or not
			if (isDataInRequest(request)||selectAll.equals("true")) {
				session = request.getSession(false);
				//System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					listTotalInvoices = objPaymentProcessService.getLocalPurchaseInvoiceDetails( site_id,reqParamsMap,selectAll);
					if(listTotalInvoices != null && listTotalInvoices.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
					}
					model.addObject("listTotalInvoicesSize",listTotalInvoices.size());
					model.addObject("listTotalInvoices",listTotalInvoices);
					addObjectsToModel(model,request);//keep user submitted data unchanged in result page
					model.addObject("TotalInvoiceAmount",reqParamsMap.get("TotalInvoiceAmount"));
					model.addObject("TotalTillReqAmount",reqParamsMap.get("TotalTillReqAmount"));
					model.addObject("TotalPaidAmount",reqParamsMap.get("TotalPaidAmount"));
					model.setViewName("payment/LocalPurchaseInvoiceDetails");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					return model;
				}
			} else {
				model.addObject("firstRequest", true);
				model.addObject("displayErrMsg", "Please Select From Date or To Date or InvoiceNumber!");
				model.addObject("listTotalInvoices",listTotalInvoices);
				addObjectsToModel(model,request);//keep user submitted data unchanged in result page
				model.setViewName("payment/LocalPurchaseInvoiceDetails");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		//SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("0",user_id,"Get Invoice Details Viewed","success",site_id1);

		return  model;

		//return "payment/payment";


	}
	
	@RequestMapping(value = "/updateLocalPurchasePayments.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updateLocalPurchasePayments( HttpServletRequest request,HttpSession session) {
	
		ModelAndView modelAndView = new ModelAndView();
		List<String> successList = objPaymentProcessService.updateLocalPurchasePayments(request, session);
		modelAndView.addObject("successList",successList);
		if(successList.size()==0){
			modelAndView.addObject("message1", "No Payments Selected");
		}
		modelAndView.setViewName("payment/PaymentResponse");
		modelAndView.addObject("pageHighlightURL","getLocalPurchaseInvoiceDetails.spring");
		return modelAndView;
	}
	
	@RequestMapping(value = "/purchaseTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView purchaseTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("firstRequest", true);
		//List<PurchaseTaxReportBean> list = objPaymentProcessService.getPurchaseTaxReport(request,model);
		
		List<SiteDetails> siteDetails = objCommonUtilities.getAllSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		
		
		//model.addObject("list", list);
		model.setViewName("payment/PurchaseTaxReportOptions");
		/*if(list != null && list.size() >0){
			request.setAttribute("showGrid", "true");
		} */
		return model;
	
	}
	@RequestMapping(value = "/viewPurchaseTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewPurchaseTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("firstRequest", true);     
		List<PurchaseTaxReportBean> list = objPaymentProcessService.getPurchaseTaxReport(request,model);
		
		List<SiteDetails> siteDetails = objCommonUtilities.getAllSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		
		
		model.addObject("list", list);
		if(list != null && list.size() >0){
			model.setViewName("payment/PurchaseTaxReportView");
			request.setAttribute("showGrid", "true");
		} else {
			model.setViewName("payment/PurchaseTaxReportOptions");
			request.setAttribute("succMessage","Data Not Available");
		}
		return model;
	
	}
	@RequestMapping(value = "/downloadPurchaseTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public void downloadPurchaseTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		try {
			
			List<PurchaseTaxReportBean> list = objPaymentProcessService.getPurchaseTaxReport(request,model);
			if(list != null && list.size() >0){
				HashMap<String,Object> extraDataMap = new HashMap<String,Object>();
				objPaymentProcessService.setExtraDetailsForExcel(request,extraDataMap);
				
				// download excel
				String[] columnHeadings = new String[]{"Project","GRN. No.","GRN. Date","PO No.","PO Date","Type of purchase","Name of Supplier","Supplier's Address","Supplier's State","Supplier's GST No","Product Name","Sub Product Name","Child Product Name","HSN CODE","UOM","Qty","Rate","Invoice No.","Invoice Date","Basic","Conveyance","CGST","SGST","IGST","Total Tax","Gross Amount"}; 
				int[] columnWidths = new int[]{5500,9000,4700,9500,4700,4700,9500,9500,4500,5500,5500,9500,9500,5500,4000,4000,4000,8500,4700,4000,4000,2500,2500,2500,3000,5000};
				String beanClassName = "com.sumadhura.bean.PurchaseTaxReportBean";
				String[] beanProperties = new String[]{"siteName","grnNo","receivedOrIssuedDate","poId","podate","typeOfPurchase","vendorName","address","state","gsinNumber","productName","subProductName","childProductName","hsnCode","measurMntName","recevedQty","amountPerUnitBeforeTaxes","invoiceId","invoiceDate","basicAmount","otherCharges","cgst","sgst","igst","totalTax","totalAmount"};
				String fileName = "Purchase Tax Report";
				new DownloadExcel().downloadExcel(response, fileName, list, columnHeadings, columnWidths, beanProperties, beanClassName, extraDataMap );
				//---------------
			} 
			else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	
	
	@RequestMapping(value = "/contractorTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView contractorTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("firstRequest", true);
		
		List<SiteDetails> siteDetails = objCommonUtilities.getAllSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		
		
		model.setViewName("reports/ContractorTaxReportOptions");
		return model;
	
	}
	@RequestMapping(value = "/viewContractorTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewContractorTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		model.addObject("firstRequest", true);     
		List<ContractorTaxReportBean> list = objPaymentProcessService.getContractorTaxReport(request,model);
		
		List<SiteDetails> siteDetails = objCommonUtilities.getAllSiteDetails();
		request.setAttribute("siteDetails",siteDetails);
		
		
		model.addObject("list", list);
		if(list != null && list.size() >0){
			model.setViewName("reports/ContractorTaxReportView");
			request.setAttribute("showGrid", "true");
		} else {
			model.setViewName("reports/ContractorTaxReportOptions");
			request.setAttribute("succMessage","Data Not Available");
		}
		return model;
	
	}
	@RequestMapping(value = "/downloadContractorTaxReport.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public void downloadContractorTaxReport( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		ModelAndView model = new ModelAndView();
		
		try {
			
			List<ContractorTaxReportBean> list = objPaymentProcessService.getContractorTaxReport(request,model);
			if(list != null && list.size() >0){
				HashMap<String,Object> extraDataMap = new HashMap<String,Object>();
				objPaymentProcessService.setExtraDetailsForContractorTaxReportExcel(request,extraDataMap);
				
				// download excel
				String[] columnHeadings = new String[]{"Project","Tax Invoice No.","Tax Invoice Date","WO Number","WO Date","Name of Contractor","Address","Contractor PAN No","Contractor GST No","Type of Work","RA Bill No.","RA Bill Date","Bill Amount","Adv Deduction","Security Deposit","Other Deductions","Basic Amount","CGST","SGST","IGST","Total Tax","Gross Amount"}; 
				int[] columnWidths = new int[]{5500,  4500,5000,  5000,4500,     9500,9500,9500,9500,      9500,4000,4000,5000,5000,5000,5000,       4000,4000,4000,4000,4000,4000};
				String beanClassName = "com.sumadhura.bean.ContractorTaxReportBean";
				String[] beanProperties = new String[]{"siteName","taxInvoiceNo","taxInvoiceDate","woNumber","woDate","contractorName","address","panNumber","gstin","typeOfWork","billId","billDate","billAmount","advanceDeduction","securityDeposit","otherDeductions","basicAmount","cgst","sgst","igst","totalTax","grossAmount"};
				String fileName = "Contractor Tax Report";
				new DownloadExcel().downloadExcel(response, fileName, list, columnHeadings, columnWidths, beanProperties, beanClassName, extraDataMap );
				//---------------
			} 
			else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
}
