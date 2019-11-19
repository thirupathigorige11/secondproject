package com.sumadhura.in;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.service.CentralSiteIndentrocessService;
import com.sumadhura.service.IndentCreationService;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;


@Controller
public class CentralSiteIndentProcessController extends UIProperties{
	
	@Autowired
	@Qualifier("cntlIndentprocess")
	CentralSiteIndentrocessService csips;
	
	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;
	
	@Autowired
	@Qualifier("posClass")
	IndentCreationService ics;
	
	@RequestMapping(value = "/sendCentralIndentToPD.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String sendCentralIndentToPD(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		String userName="";
		String indentNumber="";
		String indentName="";
		String reqSiteName ="";
		String siteWiseIndentNo="";
		try {
			session = request.getSession(true);
			
			indentNumber=  request.getParameter("indentNumber");
			indentName=request.getParameter("indentName");
			reqSiteName = request.getParameter("siteName");
			siteWiseIndentNo= request.getParameter("siteWiseIndentNo");
			//for activating Sub Module
			model.addAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
			if(indentNumber==null&&indentName==null&&reqSiteName==null&&siteWiseIndentNo==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			
			
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			userName=session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			response = csips.sendToPD(model, request, site_id, user_id,userName);
			if(response.equals("Indent Sent To Purchase Dept Successfully")){
				model.addAttribute("response",response);
			}else{				
				model.addAttribute("response1",response);
			}
			
			/*List<IndentCreationBean> listofCentralIndents = null;
			listofCentralIndents = csips.getAllCentralIndents();
			model.addAttribute("listofCentralIndents",listofCentralIndents);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		String strAuditResponse = request.getAttribute("AuditResponse") == null ? "" : request.getAttribute("AuditResponse").toString();
		SaveAuditLogDetails.auditLog("0",user_id,"Sending Cntl To Purchase Dept",strAuditResponse,String.valueOf(site_id));
		return "response";
	}
	
	@RequestMapping(value = "/viewIndent.spring", method = RequestMethod.GET)
	public String viewIndent(Model model, HttpServletRequest request,HttpSession session) {
		String strSiteId = "";
		String user_id = "";
		IndentCreationBean icb = new IndentCreationBean();
		try {
			session = request.getSession(true);
			//for activating Sub Module
			model.addAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			int indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			String reqSiteName = request.getParameter("siteName");
			String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
			model.addAttribute("indentCreationModelForm", icb);
			model.addAttribute("productsMap", iis.loadProds(strSiteId));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
			icb.setIndentNumber(indentNumber);	
			icb.setSiteName(reqSiteName);	
			
			//iib.setProjectName(iis.getProjectName(session));
			List<IndentCreationBean> IndentDetails = null;
			List<IndentCreationBean> IndentDtls = null;
			IndentDtls = csips.getIndentCreationLists(indentNumber);
			model.addAttribute("IndentDtls",IndentDtls);
			IndentDetails = csips.getCentralIndentDetailsLists(indentNumber,siteId);
			model.addAttribute("siteId",strSiteId);
			model.addAttribute("IndentDetails",IndentDetails);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails.auditLog("0",user_id,"Showing Central Indent","Success",strSiteId);
		return "CentralIndent";
	}
	
	@RequestMapping(value = "/requestCentralIndentToOtherSite.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String requestCentralIndentToOtherSite(Model model, HttpServletRequest request,HttpSession session) {
		int site_id = 0;
		String user_id = "";
		String response = "";
		String	indentNumber ="";
		String	reqSiteId = ""; 
		String noofrows = "";
		String siteWiseIndentNo="";
		String reqSiteName="";
		try {
			
			indentNumber = request.getParameter("bfIndentNumber"); 
			reqSiteId = request.getParameter("bfSiteId"); 
			noofrows = request.getParameter("bfRows");
			siteWiseIndentNo= request.getParameter("siteWiseIndentNo");
			reqSiteName=request.getParameter("siteName");
			//for activating Sub Module
			model.addAttribute("urlForActivateSubModule", "getIndentCreationDetails.spring");
			if(indentNumber==null&&reqSiteId==null&&noofrows==null&&reqSiteName==null&&siteWiseIndentNo==null){
				model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				return "response";
			}
			
			site_id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			response = csips.requestToOtherSite(model, request, site_id, user_id,session);
			if(response.contains("Allocating")){
				model.addAttribute("response1", response);
			}else if(response.equals("Sucess")){
				response = "Request Sent To Other Site Sucessfully";
				model.addAttribute("response", response);
			}else if(response.equals("Failed")){
				response ="Failed To Sent Request To Other Site";
				model.addAttribute("response1", response);
			} 
		 	model.addAttribute("responseMessage",response);
			/*List<IndentCreationBean> listofCentralIndents = null;
			listofCentralIndents = csips.getAllCentralIndents();
			model.addAttribute("listofCentralIndents",listofCentralIndents);*/
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
//		SaveAuditLogDetails.auditLog("0",user_id,"Req ToOtherSiteFrom Central",response,String.valueOf(site_id));
		return "response";
	}
	
	@RequestMapping(value = "/getIndentCreationDetailsForCentral.spring", method = RequestMethod.GET)
	public String getIndentCreationDetails(Model model, HttpServletRequest request,HttpSession session) {
		List<IndentCreationDto> pendingIndents = null;
		
		Map<String, String> siteDetails = null;
		String strSiteId = "";
		String user_id = "";
		
		try {
			session = request.getSession(true);
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			siteDetails = new IndentSummaryDao().getSiteDetails();

			//pendingEmpId = IndentCreationApproveMail.getPendingEmployeeId(user_id,Integer.parseInt(strSiteId));
			// pendingDeptId = IndentCreationApproveMail.getPendingDeptId(user_id);
			model.addAttribute("siteDetails", siteDetails);


		} catch (Exception e) {
			e.printStackTrace();
		}

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
