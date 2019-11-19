	package com.sumadhura.in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.service.ContractorPaymentProcessService;
import com.sumadhura.transdao.PaymentProcessDaoImpl;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class ContractorPaymentProcessController extends UIProperties {
	
	@Autowired
	ContractorPaymentProcessService objContractorPaymentService;


	@RequestMapping(value = "/getAccountDeptPendingContractorPayments.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView  getAccountDeptPendingRequests(HttpServletRequest request,HttpSession session,ModelAndView model) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<PaymentModesBean> PaymentModes = new ArrayList<PaymentModesBean>();
		
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();


			PaymentModes = objContractorPaymentService.getPaymentModes();
			model.addObject("PaymentModes",PaymentModes);
			
			list = objContractorPaymentService.getAccDeptContractorPaymentPendingDetails(strUserId);

			if(list != null && list.size() >0){
				request.setAttribute("showGrid", "true");
			} else {
				model.addObject("succMessage","The above Dates Data Not Available");
			}
			model.addObject("listTotalInvoices",list);
			
			int size=0;
			for(PaymentBean element:list){
				if(!element.isContractorHeader()){size++;}
			}
			
			model.addObject("listTotalInvoicesSize",size);
			model.setViewName("payment/ContractorPaymentPendingAccountLevel");

		}catch(Exception e){
			e.printStackTrace();
			model.addObject("listofPendingPayments",list);
			model.setViewName("payment/ContractorPaymentPendingAccountLevel");

		}

		return model;

	}
	
	@RequestMapping(value = "/createCntAccDeptPaymentTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String  createAccDeptPaymentTransaction(HttpServletRequest request,HttpSession session,ModelAndView model,  RedirectAttributes redir) {
		//List<PaymentBean> list = new ArrayList<PaymentBean>();
		//ModelAndView modelAndView = new ModelAndView();
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			


			List<String> successList = objContractorPaymentService.createAccDeptContractorPaymentTransaction(request,strUserId,user_name);

			/*modelAndView.addObject("successList",successList);
			if(successList.size()==0){
				modelAndView.addObject("message1", "No Payments Selected");
			}
			modelAndView.setViewName("payment/PaymentResponse");
			modelAndView.addObject("pageHighlightURL","getAccountDeptPendingContractorPayments.spring");*/
			
			redir.addFlashAttribute("successList",successList);
			if(successList.size()==0){
				redir.addFlashAttribute("message1", "No Payments Selected");
			}
			//modelAndView.setViewName("payment/PaymentResponse");
			redir.addFlashAttribute("pageHighlightURL","getAccountDeptPendingContractorPayments.spring");
			
		}catch(Exception e){
			e.printStackTrace();

		}

		//return modelAndView;
		return "redirect:/CntPaymentResponse.spring";

	}
	
	@RequestMapping(value = "/ContractorPaymentApprovalForAccDept.spring", method = {RequestMethod.GET,RequestMethod.POST})
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
				
				if (StringUtils.isNotBlank(site_id)) {
					PaymentModes = new PaymentProcessDaoImpl().getPaymentModes();
					model.addObject("PaymentModes",PaymentModes);
					
					String PendingEmpId  = new PaymentProcessDaoImpl().getApproverEmpIdInAccounts(user_id);
					model.addObject("PendingEmpId",PendingEmpId);
					
					listofPendingPayments = objContractorPaymentService.getCntAccDeptPaymentApprovalDetails(site_id,user_id);
					if(listofPendingPayments != null && listofPendingPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} 
					int size=0;
					for(PaymentBean element:listofPendingPayments){
						if(!element.isContractorHeader()){size++;}
					}
					model.addObject("listTotalInvoicesSize",size);
					model.addObject("listTotalInvoices",listofPendingPayments);
			
					model.setViewName("payment/ContractorPaymentApprovalAccountLevel");

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

	@RequestMapping(value = "/updateCntAccDeptPaymentTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String  updateAccDeptPaymentTransaction(HttpServletRequest request,HttpServletResponse response,HttpSession session,ModelAndView model,  RedirectAttributes redir) {
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		String  redirectURL = "";
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String user_name = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();	
			
			

			list=objContractorPaymentService.updateAccountDeptTransaction(request,response,strUserId,user_name);

			
			if(list != null && list.size() >0){
				request.setAttribute("showGrid", "true");
			}
			boolean flag = Boolean.valueOf(String.valueOf(request.getAttribute("isDownload")));
			//set this object to session for downloading the files
			Map<String,String> requestedDateMap =(Map<String, String>) request.getAttribute("requestedDatePaymentList");
			session.setAttribute("requestedDatePaymentMap", requestedDateMap);
			if (flag) {
				System.out.println("File is downloading \n");
				redir.addFlashAttribute("generateFile", true);
			}
			redir.addFlashAttribute("listofPendingPayments",list);
			if(list.size()==0){
				// no action
			}else{
				
			}
			if(list.size()>0){
				//redirectURL = "payment/CntAccntPrint";
				redir.addFlashAttribute("showGrid", "true");
				redirectURL = "redirect:/CntAccntPrint.spring";
			}
			else {
				//redirectURL = "payment/CntPaymentResponse";
				redir.addFlashAttribute("message1", "No Approvals Done");
				redirectURL = "redirect:/CntPaymentResponse.spring";
				redir.addFlashAttribute("pageHighlightURL","ContractorPaymentApprovalForAccDept.spring");
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}

		//return model;
		return redirectURL;
		//"redirect:/CntPaymentResponse.spring";
		
	}
	@RequestMapping(value = "/CntAccntPrint.spring",  method = RequestMethod.GET)
	public ModelAndView category(){
	    return new ModelAndView("payment/CntAccntPrint");
	}
	

	//this is for Site level
	@RequestMapping(value ="viewMyContractorTransactionDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewMyContractorTransactionDetails(HttpServletRequest request,HttpSession session) {

		ModelAndView model = null;
		model = new ModelAndView();
		//
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		
		String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
		String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
		String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
		
		List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
		if(!enableWOSubModulesSiteList.contains(siteId)){
			model.addObject("message1", "Hello "+strUserName+" As of now "+siteName+" Site can not access WORK ORDER & Contractor Bills. We will get back to you as soon as possible.");
			model.setViewName("response");
			return model;
		}
		//
		String toDate = "";
		String fromDate = "";
		String response="";
		String contractorId = "";
		String site_id = "";
		String contractorName = "";
		String workOrderNo = "";

		List<PaymentBean> listOfPayments = null;
		try {
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			/*String SiteIdAndName = request.getParameter("siteIdAndName");
				if(StringUtils.isNotBlank(SiteIdAndName)){
					if(!SiteIdAndName.equals("@@")){
						dropdown_SiteId = SiteIdAndName.split("@@")[0];
						dropdown_SiteName = SiteIdAndName.split("@@")[1];
					}
				}*/
			workOrderNo = request.getParameter("workOrderNo");
			contractorId = request.getParameter("contractorId");
			contractorName = request.getParameter("contractorName");
			if(StringUtils.isBlank(contractorName)){contractorId="";}
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

				if (StringUtils.isNotBlank(site_id))  {
					listOfPayments = objContractorPaymentService.getViewContractorPaymentDetails(fromDate, toDate,contractorId,user_id,site_id,workOrderNo);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("contractorId",contractorId);
					model.addObject("contractorName",contractorName);
					model.addObject("workOrderNo",workOrderNo);
					List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					model.setViewName("payment/GetContractorTransactionDetails");
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
				model.addObject("contractorId",contractorId);
				model.addObject("contractorName",contractorName);
				model.addObject("workOrderNo",workOrderNo);

				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				model.setViewName("payment/GetContractorTransactionDetails");
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

	//this is for department level
	@RequestMapping(value ="viewSiteWiseContractorTransactionDetails.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewSiteWiseContractorTransactionDetails(HttpServletRequest request,HttpSession session) {

		String toDate = "";
		String fromDate = "";
		ModelAndView model = null;
		String response="";
		String contractorId = "";
		String site_id = "";
		String dropdown_SiteId="";
		String dropdown_SiteName="";
		String contractorName = "";
		String workOrderNo = "";

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
			workOrderNo = request.getParameter("workOrderNo");
			contractorId = request.getParameter("contractorId");
			contractorName = request.getParameter("contractorName");
			if(StringUtils.isBlank(contractorName)){contractorId="";}
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)|| StringUtils.isNotBlank(dropdown_SiteId)) {
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

				if (StringUtils.isNotBlank(site_id))  {
					listOfPayments = objContractorPaymentService.getViewContractorPaymentDetails(fromDate, toDate,contractorId,user_id,dropdown_SiteId,workOrderNo);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("contractorId",contractorId);
					model.addObject("contractorName",contractorName);
					model.addObject("workOrderNo",workOrderNo);
					model.addObject("site_id",dropdown_SiteId);
					model.addObject("site_name", dropdown_SiteName);
					List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					model.setViewName("payment/GetSiteWiseContractorTransactionDetails");
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
				model.addObject("contractorId",contractorId);
				model.addObject("contractorName",contractorName);
				model.addObject("workOrderNo",workOrderNo);

				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				model.setViewName("payment/GetSiteWiseContractorTransactionDetails");
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

	@RequestMapping(value ="viewMyContractorPaymentRequestStatus.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewMyContractorPaymentRequestStatus(HttpServletRequest request,HttpSession session) {

		ModelAndView model = null;
		model = new ModelAndView();
		//
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		
		String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
		String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
		String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
		enableWOSubModules=enableWOSubModules+",997";
		
		List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
		if(!enableWOSubModulesSiteList.contains(siteId)){
			model.addObject("message1", "Hello "+strUserName+" As of now "+siteName+" Site can not access WORK ORDER & Contractor Bills. We will get back to you as soon as possible.");
			model.setViewName("response");
			return model;
		}
		//
		String toDate = "";
		String fromDate = "";
		String response="";
		String site_id = "";
		String contractorId = "";
		String contractorName = "";
		String workOrderNo = "";
		String dropdown_SiteId="";
		String dropdown_SiteName="";
		List<PaymentBean> listOfPayments = null;
		try {
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			workOrderNo = request.getParameter("workOrderNo");
			contractorId = request.getParameter("contractorId");
			contractorName = request.getParameter("contractorName");
			if(StringUtils.isBlank(contractorName)){contractorId="";}
			String SiteIdAndName = request.getParameter("siteIdAndName");
			if(StringUtils.isNotBlank(SiteIdAndName)){
				if(!SiteIdAndName.equals("@@")){
					dropdown_SiteId = SiteIdAndName.split("@@")[0];
					dropdown_SiteName = SiteIdAndName.split("@@")[1];
				}
			}
			session = request.getSession(false);
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();
			request.setAttribute("isAccounts", site_id.equals(accDeptId)?true:false);
			List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
			request.setAttribute("allSitesList", allSitesList);
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)|| StringUtils.isNotBlank(dropdown_SiteId)) {
				if (StringUtils.isNotBlank(site_id)) {
					listOfPayments = objContractorPaymentService.viewMyContractorPayment(fromDate, toDate, site_id, user_id,contractorId,workOrderNo,dropdown_SiteId);
					if(listOfPayments != null && listOfPayments.size() >0){
						request.setAttribute("showGrid", "true");
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failed";
					}
					model.addObject("listOfPayments",listOfPayments);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("contractorId",contractorId);
					model.addObject("contractorName",contractorName);
					model.addObject("workOrderNo",workOrderNo);
					
					model.setViewName("payment/ViewMyContractorPayment");
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
				model.addObject("contractorId",contractorId);
				model.addObject("contractorName",contractorName);
				model.addObject("workOrderNo",workOrderNo);
				model.setViewName("payment/ViewMyContractorPayment");
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

	@RequestMapping(value = "/getContractorPaymentDetailsToUpdate.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getContractorPaymentDetailsToUpdate( HttpServletRequest request,HttpSession session) {
		String site_id = "";
		String fromDate = "";
		String toDate = "";
		String contractorId = "";
		String contractorName = "";
		String workOrderNo = "";
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		ModelAndView model = null;
		List<PaymentBean> listofPendingPayments = new ArrayList<PaymentBean>();
		try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				workOrderNo = request.getParameter("workOrderNo");
				String SiteIdAndName = request.getParameter("siteIdAndName");
				if(StringUtils.isNotBlank(SiteIdAndName)){
					if(!SiteIdAndName.equals("@@")){
						dropdown_SiteId = SiteIdAndName.split("@@")[0];
						dropdown_SiteName = SiteIdAndName.split("@@")[1];
					}
				}
				contractorId = request.getParameter("contractorId");
				contractorName = request.getParameter("contractorName");
				if(StringUtils.isBlank(contractorName)){contractorId="";}
				
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				
				if (StringUtils.isNotBlank(site_id)) {
					if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)|| StringUtils.isNotBlank(dropdown_SiteId)) {
						listofPendingPayments = objContractorPaymentService.getContractorPaymentDetailsToUpdate(user_id,fromDate,toDate,contractorId,workOrderNo,dropdown_SiteId);

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
						model.addObject("contractorId",contractorId);
						model.addObject("contractorName",contractorName);
						model.addObject("workOrderNo",workOrderNo);
						model.addObject("site_id",dropdown_SiteId);
						model.addObject("site_name", dropdown_SiteName);
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/AccDeptContractorPaymentUpdate");
					}
					else{
						model.addObject("displayErrMsg", "Please Select From Date or To Date!");
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/AccDeptContractorPaymentUpdate");
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

	@RequestMapping(value = "/updateRefNoInAccDeptContractorTransaction.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String  updateRefNoInAccDeptContractorTransaction(HttpServletRequest request,HttpSession session,ModelAndView model,  RedirectAttributes redir) {
		//List<PaymentBean> list = new ArrayList<PaymentBean>();
		//ModelAndView modelAndView = new ModelAndView();
		String  redirectURL = "";
		try{


			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();



			String strResponse=objContractorPaymentService.updateRefNoInAccDeptContractorTransaction(request,strUserId);

			
			if(strResponse.equals("Success")){

				redir.addFlashAttribute("message", "Payment Updated Successfully");
				//modelAndView.setViewName("response");
				redirectURL = "redirect:/response.spring";
			}else{
				redir.addFlashAttribute("message1", "Sorry, Payment Updation Failed");
				//modelAndView.setViewName("response");
				redirectURL = "redirect:/response.spring";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}

		return redirectURL;

	}
	@RequestMapping(value = "/response.spring",  method = RequestMethod.GET)
	public ModelAndView response(){
	    return new ModelAndView("response");
	}
	@RequestMapping(value = "/beforeDownloadExcel.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView beforeDownloadExcel( HttpServletRequest request,HttpSession session) {
		String site_id = "";
		ModelAndView model = null;
		try {
			model = new ModelAndView();
			session = request.getSession(false);
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));

			if (StringUtils.isNotBlank(site_id)) {
				List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				model.setViewName("payment/DownloadPaymentsToExcelFileForUpdate");
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
	
	@RequestMapping(value = "/viewDownloadablePayments.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewDownloadablePayments( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		String site_id = "";
		String fromDate = "";
		String toDate = "";
		String contractorId = "";
		String contractorName = "";
		String workOrderNo = "";
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		ModelAndView model = null;
		List<PaymentBean> listofPendingPayments = new ArrayList<PaymentBean>();
		try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				workOrderNo = request.getParameter("workOrderNo");
				String SiteIdAndName = request.getParameter("siteIdAndName");
				if(StringUtils.isNotBlank(SiteIdAndName)){
					if(!SiteIdAndName.equals("@@")){
						dropdown_SiteId = SiteIdAndName.split("@@")[0];
						dropdown_SiteName = SiteIdAndName.split("@@")[1];
					}
				}
				contractorId = request.getParameter("contractorId");
				contractorName = request.getParameter("contractorName");
				if(StringUtils.isBlank(contractorName)){contractorId="";}
				
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				
				if (StringUtils.isNotBlank(site_id)) {
					if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)|| StringUtils.isNotBlank(dropdown_SiteId)) {
						listofPendingPayments = objContractorPaymentService.downloadPaymentsToExcelFileForUpdate(user_id,fromDate,toDate,contractorId,workOrderNo,dropdown_SiteId);

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
						model.addObject("contractorId",contractorId);
						model.addObject("contractorName",contractorName);
						model.addObject("workOrderNo",workOrderNo);
						model.addObject("site_id",dropdown_SiteId);
						model.addObject("site_name", dropdown_SiteName);
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/DownloadPaymentsToExcelFileForUpdate");
						return model;
						
					}
					else{
						model.addObject("succMessage", "Please Select From Date or To Date!");
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/DownloadPaymentsToExcelFileForUpdate");
						return model;
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
	
	@RequestMapping(value = "/downloadPaymentsToExcelFileForUpdate.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView downloadPaymentsToExcelFileForUpdate( HttpServletRequest request,HttpSession session,HttpServletResponse response) {
		String site_id = "";
		String fromDate = "";
		String toDate = "";
		String contractorId = "";
		String contractorName = "";
		String workOrderNo = "";
		String dropdown_SiteId = "";
		String dropdown_SiteName = "";
		ModelAndView model = null;
		List<PaymentBean> listofPendingPayments = new ArrayList<PaymentBean>();
		try {
				model = new ModelAndView();
				fromDate = request.getParameter("fromDate");
				toDate = request.getParameter("toDate");
				workOrderNo = request.getParameter("workOrderNo");
				String SiteIdAndName = request.getParameter("siteIdAndName");
				if(StringUtils.isNotBlank(SiteIdAndName)){
					if(!SiteIdAndName.equals("@@")){
						dropdown_SiteId = SiteIdAndName.split("@@")[0];
						dropdown_SiteName = SiteIdAndName.split("@@")[1];
					}
				}
				contractorId = request.getParameter("contractorId");
				contractorName = request.getParameter("contractorName");
				if(StringUtils.isBlank(contractorName)){contractorId="";}
				
				session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String user_id=String.valueOf(session.getAttribute("UserId"));
				
				if (StringUtils.isNotBlank(site_id)) {
					if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate) || StringUtils.isNotBlank(contractorId)|| StringUtils.isNotBlank(workOrderNo)|| StringUtils.isNotBlank(dropdown_SiteId)) {
						listofPendingPayments = objContractorPaymentService.downloadPaymentsToExcelFileForUpdate(user_id,fromDate,toDate,contractorId,workOrderNo,dropdown_SiteId);

						if(listofPendingPayments != null && listofPendingPayments.size() >0){
							String filename = objContractorPaymentService.generateFileName(user_id);
							File file = new File(filename+".xlsx"); 
							XSSFWorkbook workbook = new XSSFWorkbook(); 
							objContractorPaymentService.writeDataToExcelSheet(workbook,listofPendingPayments);
							FileOutputStream out = new FileOutputStream(file);
					        workbook.write(out);
					        try {
					        	objContractorPaymentService.writeExcelDataToResponse(response,file);
					        }catch (final Exception ex){
					            ex.printStackTrace();
					        }
					        out.close();
					        //workbook.close(); 
					        
					    } 
						else {
						model.addObject("succMessage","The above Dates Data Not Available");
						model.addObject("listTotalPaymentReqSize",listofPendingPayments.size());
						model.addObject("listofPendingPayments",listofPendingPayments);
						model.addObject("fromDate",fromDate);
						model.addObject("toDate", toDate);
						model.addObject("contractorId",contractorId);
						model.addObject("contractorName",contractorName);
						model.addObject("workOrderNo",workOrderNo);
						model.addObject("site_id",dropdown_SiteId);
						model.addObject("site_name", dropdown_SiteName);
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/DownloadPaymentsToExcelFileForUpdate");
						return model;
						}
					}
					else{
						model.addObject("succMessage", "Please Select From Date or To Date!");
						List<Map<String, Object>> allSitesList = new PaymentProcessDaoImpl().getAllSites();
						request.setAttribute("allSitesList", allSitesList);
						model.setViewName("payment/DownloadPaymentsToExcelFileForUpdate");
						return model;
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
	
	return null;
	
	}
	
	@RequestMapping(value = "/UploadPaymentExcel.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadExcel( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.setViewName("payment/UploadPaymentExcel");
		return model;
	
	}
	@RequestMapping(value = "/updatePaymentRefNoFromExcel", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView updatePaymentRefNoFromExcel(  HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile[] files) throws IllegalStateException, IOException {
		String response = "";
		List<String> errMsg = new ArrayList<String>();
		List<PaymentBean> successPayments = new ArrayList<PaymentBean>();
		List<PaymentBean> failedPayments = new ArrayList<PaymentBean>();
		
		ModelAndView model = new ModelAndView();
		
		for (int index = 0; index < files.length; index++) {
			MultipartFile multipartFile = files[index];
			
			if(!multipartFile.isEmpty()){
				if(multipartFile.getOriginalFilename().endsWith("xlsx")){
					response = objContractorPaymentService.updatePaymentRefNoFromExcel(multipartFile,successPayments,failedPayments,errMsg);
					if(response.equals("validations")){
						model.setViewName("payment/UploadPaymentExcel");
						model.addObject("displayErrMsg", errMsg.get(0));
					}
					else{
						model.setViewName("payment/UploadExcelResponse");
						model.addObject("response", response);
						model.addObject("successPayments", successPayments);
						model.addObject("failedPayments", failedPayments);
					}
				}
				else{
					model.setViewName("payment/UploadPaymentExcel");
					model.addObject("displayErrMsg", "Upload Excel File of type (.xlsx)");
				}
			}
			else{
				model.setViewName("payment/UploadPaymentExcel");
				model.addObject("displayErrMsg", "Please Upload Excel File.");
			}

		}
		
		return model;
	}
}
