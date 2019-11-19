package com.sumadhura.in;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
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
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.IssueToOtherSiteBean;
import com.sumadhura.bean.IssueToOtherSiteInwardBean;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.IssueToOtherSiteService;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class IssueToOtherSiteController extends UIProperties{

	@Autowired
	@Qualifier("itosc")
	IssueToOtherSiteService itoss;
	
	@Autowired
	IndentReceiveController irc;
	
	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;

	@RequestMapping(value = "/indentIssueToOtherSite", method = RequestMethod.GET)
	public String indentIssueToOtherSite(Model model, HttpSession session) {
		IssueToOtherSiteBean itosb = new IssueToOtherSiteBean();
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("indentIssueOtherSiteForm", itosb);
		model.addAttribute("productsMap", itoss.issueToOtherSiteProducts(site_id1));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		 String reqid=String.valueOf(itoss.getIndentEntrySequenceNumber());	
		 itosb.setReqId(reqid);
		//itosb.setProjectName(itoss.getProjectName(session));
		model.addAttribute("sitesMap", itoss.loadSitesInformationForIssueToOtherSite());

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			//String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
			audit.auditLog(reqid,user_id,"Issue to Other Site","success",site_id1);
			
		
		return "IssueToOtherSite";
	}

	@RequestMapping(value = "/issueToOtherSiteSubProducts", method = RequestMethod.POST)
	@ResponseBody
	public String issueToOtherSiteSubProducts(@RequestParam("mainProductId") String mainProductId) {
		return itoss.issueToOtherSiteSubProducts(mainProductId);
	}

	@RequestMapping(value = "/issueToOtherSiteChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String issueToOtherSiteChildProducts(@RequestParam("subProductId") String subProductId) {
		return itoss.issueToOtherSiteChildProducts(subProductId);
	}

	@RequestMapping(value = "/issueToOtherSiteMeasurements", method = RequestMethod.POST)
	@ResponseBody
	public String issueToOtherSiteMeasurements(@RequestParam("childProductId") String childProductId) {
		return itoss.issueToOtherSiteMeasurements(childProductId);
	}
	
	@RequestMapping(value = "/getIssueToOtherSiteProductAvailability", method = RequestMethod.POST)
	@ResponseBody
	public String getIssueToOtherSiteProductAvailability(@RequestParam("prodId") String prodId, @RequestParam("subProductId") String subProductId, @RequestParam("childProdId") String childProdId, @RequestParam("measurementId") String measurementId, HttpServletRequest request, HttpSession session) {
		return itoss.getIssueToOtherSiteProductAvailability(prodId, subProductId, childProdId, measurementId, request, session);
	}
	@RequestMapping(value = "/doIndentIssueToOtherSite", method = { RequestMethod.GET, RequestMethod.POST })
	public String doIndentIssueToOtherSite(@ModelAttribute("indentIssueOtherSiteForm")IssueToOtherSiteBean issueToOtherSiteModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session) {
	
		String Returnable=request.getParameter("returnable");
		String NonReturnable=request.getParameter("nonReturnable");
		String issueType=request.getParameter("issueType");
		String vehicleNo=request.getParameter("vehicleNo");
		String strIndentNo = request.getParameter("indentNumber");
		if(Returnable==null&&NonReturnable==null&&issueType==null&&vehicleNo==null&&strIndentNo==null){
			model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response"; 
		}
		
		String response = itoss.doIndentIssueToOtherSite(model, request, session);
		String viewToBeSelected = "";
		if(response.equalsIgnoreCase("Success")) {
			viewToBeSelected = "DCviewGRN";
		}
		else if(response.equalsIgnoreCase("Failed")){
			viewToBeSelected = "IndentIssueToOtherSiteResponse";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			request.setAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}else if(response.equalsIgnoreCase("Expired")){
			String childProduct=request.getAttribute("childProduct").toString();
			model.addAttribute("message1",childProduct+" Child Product Expired");
			viewToBeSelected = "response";
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
		audit.auditLog(indentEntrySeqNum,user_id,"Issue to Other Site clicked Submit","success",site_id1);
			
		
		return viewToBeSelected;
	}
	
	
	/*============== inwards get invoice details*/
	
	@RequestMapping(value = "/IssueToOtherSitGetInvoiceId", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView doIndentIssueToOtherSite(HttpSession session){

		ModelAndView model = null;

		try {
			model = new ModelAndView();
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			List<IssueToOtherSiteInwardBean> listSiteDetail= itoss.getIssuesToOtherLists(session,"", siteId,"loadAllData");
			model.addObject("urlForActivateSubModule", "IssueToOtherSitGetInvoiceId.spring");
			//loading all the request
			if(listSiteDetail != null && listSiteDetail.size() > 0){
				model.addObject("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addObject("issueToOtherSiteModelForm", new IssueToOtherSiteInwardBean());
				model.addObject("listOfIssueToOtherSiteInwardList",listSiteDetail);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if(session.getAttribute("InwardsFromOtherSite_RequestId")!=null){
				session.removeAttribute("InwardsFromOtherSite_RequestId");
			}
			model.setViewName("issueToOtherSite_GetInvoiceId");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Inwards From Other Site View","success",site_id1);
			
		return model;
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/IssueToOtherSitGetInvoiceIdDetails", method ={ RequestMethod.POST,RequestMethod.GET})
	public String doIndentIssueToOtherSiteInwards(HttpServletRequest request, HttpSession session,Model model){

		String RequestId = "";
		String siteId = "";
		String strIssueType = "";
		String view = "";
		String response="";
		String urlForActivateSubModule="";
		try {
			RequestId = request.getParameter("RequestId") == null ? "" : request.getParameter("RequestId");
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();

			if ( StringUtils.isNotBlank(RequestId)){


				List<IssueToOtherSiteInwardBean> listSiteDetail= itoss.getIssuesToOtherLists(session,RequestId, siteId,strIssueType);

				if(listSiteDetail != null && listSiteDetail.size() > 0){

					model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
					model.addAttribute("issueToOtherSiteModelForm", new IssueToOtherSiteInwardBean());
					model.addAttribute("listOfIssueToOtherSiteInwardList",listSiteDetail);

					strIssueType = session.getAttribute("IssueType") == null ? "" : session.getAttribute("IssueType").toString();

					model.addAttribute("listOfIssueToOtherSiteProductDetails",itoss.getGetProductDetailsLists(session,RequestId, siteId,strIssueType));
					model.addAttribute("listOfIssueToOtherSiteTransportChargesDetails",itoss.getTransportChargesList(RequestId, siteId));
					model.addAttribute("gstMap", irs.getGSTSlabs());
					model.addAttribute("chargesMap", irs.getOtherCharges());
					double doubleFinalAmount = Double.valueOf(session.getAttribute("doubleFinalAmount") == null ? "" : session.getAttribute("doubleFinalAmount").toString());
					model.addAttribute("doubleFinalAmount",doubleFinalAmount);
					model.addAttribute("strIssueType", strIssueType);
					model.addAttribute("urlForActivateSubModule", "IssueToOtherSitGetInvoiceId.spring");
	model.addAttribute("siteId",siteId);
					model.addAttribute("Allsites",allSites);
					if(strIssueType.equals("NonReturnable")){
						response="success";
						view = "IssueToOtherSiteNonReturnableView";
					}else if(strIssueType.equals("Returnable")){
						response="success";
						view = "IssueToOtherSiteReturableView";
					}else{
						view = "IssueToOtherSiteReturableView";
						response="failed";
					}

				}else{
					model.addAttribute("Message", "Sorry no data found with specified request number");
					if(session.getAttribute("InwardsFromOtherSite_RequestId")!=null){
						if(session.getAttribute("InwardsFromOtherSite_RequestId").toString().equals(RequestId)){
							model.addAttribute("Message", "");
							session.removeAttribute("InwardsFromOtherSite_RequestId");
						}
					}
					response="failure";
					SaveAuditLogDetails audit=new SaveAuditLogDetails();
						String indentEntrySeqNum=RequestId;
						String user_id=String.valueOf(session.getAttribute("UserId"));
						String site_id1 = String.valueOf(session.getAttribute("SiteId"));
						audit.auditLog("0",user_id,"Inwards from Other Site entered requested id click submit",response,site_id1);
						
						listSiteDetail= itoss.getIssuesToOtherLists(session,"", siteId,"loadAllData");

						if(listSiteDetail != null && listSiteDetail.size() > 0){
							model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
							model.addAttribute("issueToOtherSiteModelForm", new IssueToOtherSiteInwardBean());
							model.addAttribute("listOfIssueToOtherSiteInwardList",listSiteDetail);
						}

					
					view = "issueToOtherSite_GetInvoiceId";
				}

			}else{
				model.addAttribute("Message", "*Please enter the requestID");
				response="failed";
				view = "issueToOtherSite_GetInvoiceId";
			}
		} catch (Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Inwards from Other Site entered requested id click submit",response,site_id1);
		

		return view ;
	}

	@RequestMapping(value = "/doinwardsFromOtherSite",  method = { RequestMethod.GET, RequestMethod.POST })
	public String doinwardsFromOtherSite(@ModelAttribute("issueToOtherSiteModelForm")IssueToOtherSiteInwardBean objIssueToOtherSiteInwardBean, BindingResult result, Model model, HttpServletRequest request, HttpSession session) {

		//the site that is receiving the indent 
		String siteWiseIndentNo=request.getParameter("siteWiseIndentNo");
	 	String indentNumber=request.getParameter("indentNumber");
		String invoiceNumber = request.getParameter("invoiceNumber");
		String invoiceDate = request.getParameter("invoiceDate");
		model.addAttribute("urlForActivateSubModule", "IssueToOtherSitGetInvoiceId.spring");
		
		if(siteWiseIndentNo==null&&indentNumber==null&&invoiceNumber==null&&invoiceDate==null){
			model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "InwardsFromOtherSiteResponse";
		}
		
		String strValue="";
		String viewToBeSelected = "";
		String response ="";
		String invoiceNum = request.getParameter("invoiceNumber")==null ? "" : request.getParameter("invoiceNumber");
		if(invoiceNum!=null && !invoiceNum.equals("")){
		strValue=irc.getAndCheckReceiveBOQQuantity(request,session);
		if(!strValue.contains("BOQ")){
			response = itoss.doIndentInwardsFromOtherSite(model, request, session);
		
		if(response.equalsIgnoreCase("Success")) {
			model.addAttribute("response", "Received Material Successfully.");
			viewToBeSelected = "InwardsFromOtherSiteResponse";
			
			/*String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			List<IssueToOtherSiteInwardBean> listSiteDetail= itoss.getIssuesToOtherLists(session,"", siteId,"loadAllData");
			//loading all the request
			if(listSiteDetail != null && listSiteDetail.size() > 0){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("issueToOtherSiteModelForm", new IssueToOtherSiteInwardBean());
				model.addAttribute("listOfIssueToOtherSiteInwardList",listSiteDetail);
			}*/
		}
		else if(response.equalsIgnoreCase("Failed")){
			model.addAttribute("response1", "error occured during the inwards");
			viewToBeSelected = "InwardsFromOtherSiteResponse";
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
		String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog(indentEntrySeqNum,user_id,"Inwards from Other Site click submit to save data",response,site_id1);
		return viewToBeSelected;
	}
	
	public static void main(String [] args) throws UnknownHostException{
        InetAddress ip;
        String hostname;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
	}
}
