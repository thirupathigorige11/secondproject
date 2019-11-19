package com.sumadhura.in;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.util.SaveAuditLogDetails;

@Controller
public class IndentIssueController {

	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;

	@RequestMapping(value = "/launchIndIssuePage", method = RequestMethod.GET)
	public String launchLogInPage(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		model.addAttribute("SiteId", strSiteId);
		String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
		//System.out.println("the role id of  "+no);
		if(StrRoleId.equals("2") && strSiteId.equals("102")){
			model.addAttribute("noOfDays",15);
		}
		else if(StrRoleId.equals("2"))
			model.addAttribute("noOfDays",5);
		else if(StrRoleId.equals("4")){
			model.addAttribute("noOfDays",30);
		}else if(StrRoleId.equals("5")){
			model.addAttribute("noOfDays",100);
		}else if(StrRoleId.equals("12")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("13")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("1")){
			model.addAttribute("noOfDays",5);
		}
		else{
			model.addAttribute("noOfDays",0);
		}
		iib.setSiteId(strSiteId);
		String isWorkOrderExistsInSite=iis.isWorkOrderExistsInSite(iib);
		String indentEntrySeqNum=String.valueOf(iis.getIndentEntrySequenceNumber());
		
		iib.setReqId(indentEntrySeqNum);
		iib.setProjectName(iis.getProjectName(session));
		model.addAttribute("isWorkOrderExistsInSite", isWorkOrderExistsInSite);
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog(indentEntrySeqNum,user_id,"New Issue View","success",site_id1);
		return "indentIssuePage";
	}

	
	@RequestMapping(value = "/materialManagement.spring", method = RequestMethod.GET)
	public String launchLogInPage1(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();

		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		model.addAttribute("SiteId", strSiteId);
		model.addAttribute("isThisSiteWiseModule", false);
		String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
		//System.out.println("the role id of  "+no);

		if(StrRoleId.equals("2"))
			model.addAttribute("noOfDays",10);
		else if(StrRoleId.equals("4")){
			model.addAttribute("noOfDays",30);
		}else if(StrRoleId.equals("5")){
			model.addAttribute("noOfDays",100);
		}else if(StrRoleId.equals("12")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("13")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("1")){
			model.addAttribute("noOfDays",10);
		}
		else{
			model.addAttribute("noOfDays",0);
		}
		//model.addAttribute("noOfDays",7);
		String indentEntrySeqNum=String.valueOf(iis.getIndentEntrySequenceNumber());
		
		iib.setReqId(indentEntrySeqNum);
		iib.setProjectName(iis.getProjectName(session));
		
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog(indentEntrySeqNum,user_id,"New Issue View","success",site_id1);
			return "MaterialManagement/materialManagement";
	}

	@RequestMapping(value = "/siteWiseMaterialManagement.spring", method = RequestMethod.GET)
	public String siteWiseMaterialManagement(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();

		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
		model.addAttribute("urlForActionTag", "siteWiseMaterialManagement.spring");
   		model.addAttribute("pageName", "Site Wise Material Management");
		if(dropdown_SiteId.length()==0){
			return "SiteSelection";
		}
		strSiteId=dropdown_SiteId;
		model.addAttribute("strSiteId", strSiteId);
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		model.addAttribute("SiteId", strSiteId);
		model.addAttribute("isThisSiteWiseModule", true);
		String StrRoleId = String.valueOf(session.getAttribute("Roleid"));
		//System.out.println("the role id of  "+no);

		if(StrRoleId.equals("2"))
			model.addAttribute("noOfDays",10);
		else if(StrRoleId.equals("4")){
			model.addAttribute("noOfDays",30);
		}else if(StrRoleId.equals("5")){
			model.addAttribute("noOfDays",100);
		}else if(StrRoleId.equals("12")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("13")){
			model.addAttribute("noOfDays",20);
		}else if(StrRoleId.equals("1")){
			model.addAttribute("noOfDays",10);
		}
		else{
			model.addAttribute("noOfDays",0);
		}
		//model.addAttribute("noOfDays",7);
		String indentEntrySeqNum=String.valueOf(iis.getIndentEntrySequenceNumber());
		
		iib.setReqId(indentEntrySeqNum);
		iib.setProjectName(iis.getProjectName(session));
		
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
			//String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog(indentEntrySeqNum,user_id,"New Material Management","success",site_id1);
			return "MaterialManagement/materialManagement";
	}
	
	
	/************************************************** PO***************************/
	
	@RequestMapping(value = "/createPurcghaseOrder.spring", method = RequestMethod.GET)
	public String createPurcghaseOrder(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Create PO Viewed","success",site_id);
			
		
		
		
		return "createPO";
	}
	
/*	***********************END PO************************/
	

	/************************************************** PO***************************/
	
	@RequestMapping(value = "/PODetailsShow.spring", method = RequestMethod.GET)
	public String PODetailsShow(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
		iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
		iib.setProjectName(iis.getProjectName(session));
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Create PO Viewed clicked Submit","success",site_id);
		
		
		
		
		return "PODetailsShow";
	}
		@RequestMapping(value = "/loadAndSetContractorInfo", method = RequestMethod.GET)
	@ResponseBody
	public String loadAndSetContractorInfo(@RequestParam("contractorName") String contractorName) {
		//System.out.println("In controller contractorName is :"+contractorName+"|");
		String contData = iis.getContractorInfo(contractorName);
		//System.out.println("In controller resulted data :"+contData+"|");
		return contData;
	}
	
	@RequestMapping(value = "/loadAndSetEmployerInfo", method = RequestMethod.GET)
	@ResponseBody
	public String loadAndSetEmployerInfo(@RequestParam("employeeName") String employeeName) {
		//System.out.println("In controller employeeName is :"+employeeName+"|");
		String empData = iis.getEmployerInfo(employeeName);
		//System.out.println("In controller resulted data :"+empData+"|");
		return empData;
	}
	
	@RequestMapping(value = "/getEmployerid", method = RequestMethod.GET)
	@ResponseBody
	public String gettEmployerid(@RequestParam("employeeName") String employeeName) {
		//System.out.println("onblur employeeName is :"+employeeName+"|");
		String empData = iis.getEmployerid(employeeName);
		//System.out.println(" resulted empid :"+empData+"|");
		return empData;
	}
	
	@RequestMapping(value = "/getEmployerName", method = RequestMethod.GET)
	@ResponseBody
	public String getEmployerName(@RequestParam("employeeid") String employeeid) {
	//	System.out.println("onblur employeeid is :"+employeeid+"|");
		String empId = iis.getEmployerName(employeeid);
		//System.out.println(" resulted empdata :"+empId+"|");
		return empId;
	}
	
	
	
	
	
	
	
	
	
/*	***********************END PO************************/
/*	********************************** Get PO Dateials****************************/

	@RequestMapping(value = "/PurcghaseOrderDetails.spring", method = RequestMethod.GET)
	public String getPurcghaseOrderDetails(Model model, HttpServletRequest request,HttpSession session) {
		IndentIssueBean iib = new IndentIssueBean();


		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		model.addAttribute("indentIssueModelForm", iib);
		model.addAttribute("productsMap", iis.loadProds(strSiteId));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));



	 String reqid=String.valueOf(iis.getIndentEntrySequenceNumber());	
		iib.setReqId(reqid);
		iib.setProjectName(iis.getProjectName(session));
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog(reqid,user_id,"Get PO Details Viewed","success",site_id);
			
		
		
		
		return "getPODetails";
	}
	
	
	/************************************END PO Details*****************************************/
	
	@RequestMapping(value = "/indentIssueSubProducts", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueSubProducts(@RequestParam("mainProductId") String mainProductId) {
		return iis.loadSubProds(mainProductId);
	}

	@RequestMapping(value = "/indentIssueChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String indentIssueChildProducts(@RequestParam("subProductId") String subProductId) {
		return iis.loadChildProds(subProductId);
	}

	@RequestMapping(value = "/listUnitsOfSubProducts", method = RequestMethod.POST)
	@ResponseBody
	public String listUnitsOfSubProducts(@RequestParam("childProductId") String childProductId, HttpSession session) {
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		return iis.loadIndentIssueMeasurements(childProductId,strSiteId);
	}

	@RequestMapping(value = "/getProductAvailability", method = RequestMethod.POST)
	@ResponseBody
	public String getProductAvailability(@RequestParam("prodId") String prodId, @RequestParam("subProductId") String subProductId, @RequestParam("childProdId") String childProdId, @RequestParam("measurementId") String measurementId,@RequestParam("requesteddate") String requesteddate, @RequestParam(value="groupId",required = false) String groupId,@RequestParam(value="isReceive",required = false) String isReceive,HttpServletRequest request, 
			HttpSession session) {
		//System.out.println("controller class");
		//System.out.println("requesteddate - "+requesteddate);
		
		return iis.loadProductAvailability(prodId, subProductId, childProdId, measurementId,requesteddate,request, session,groupId,isReceive);
	}
	// Expiry date start
	@RequestMapping(value = "/checkExpiryDate", method = RequestMethod.POST)
	@ResponseBody
	public String checkExpiryDate(@RequestParam("prodId") String prodId, @RequestParam("subProductId") String subProductId, @RequestParam("childProdId") String childProdId, @RequestParam("siteId") String siteId,@RequestParam("requesteddate") String requesteddate,@RequestParam("Quantity") String issuedQuan, HttpServletRequest request, HttpSession session) {
		//System.out.println("controller class");
		//System.out.println("requesteddate - "+requesteddate);
		
		return iis.checkExpiryDate(prodId, subProductId, childProdId, siteId,requesteddate,Double.valueOf(issuedQuan),request, session);
	}
	
	
	
	// expiry date end 
	@RequestMapping(value = "/getProductAvailability2", method = RequestMethod.POST)
	@ResponseBody
	public String getProductAvailability2(@RequestParam("prodId") String prodId, @RequestParam("subProductId") String subProductId, 
			@RequestParam("childProdId") String childProdId, @RequestParam("measurementId") String measurementId,@RequestParam("groupId") String groupId,@RequestParam("isReceive") String isReceive,
			HttpServletRequest request, HttpSession session) {
		//System.out.println("controller class");
		Date myDate = new Date();
		String requesteddate = new SimpleDateFormat("dd-MMM-YY").format(myDate);
		//System.out.println("requesteddate "+requesteddate);
		return iis.loadProductAvailability(prodId, subProductId, childProdId, measurementId,requesteddate,request, session,groupId,isReceive);
	}

	@RequestMapping(value = "/getFloorDetails", method = RequestMethod.POST)
	@ResponseBody
	public String getFloorDetails(@RequestParam("blockId") String blockId) {
		return iis.getFloorDetails(blockId);
	}
	
	//ACP
	@RequestMapping(value = "/getBlockDetails.spring", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getBlockDetails(@RequestParam("siteId") String strSiteId) {
		return  iis.loadBlockDetails(strSiteId);
	}
	
	
	 

	@RequestMapping(value = "/getWdDetails.spring", method = RequestMethod.POST)
	@ResponseBody
	public String getWdDetails(@RequestParam("workOrderNumber") String workOrderId,@RequestParam("siteId") String siteId) {
		return iis.getWdDetails(workOrderId,siteId);
	}
	@RequestMapping(value = "/getWorkOrderWDBlockDetails.spring", method = RequestMethod.POST)
	@ResponseBody
	public String getWDblockDetails(HttpServletRequest request) {
		return iis.getWDblockDetails(request);
	}
	
	@RequestMapping(value = "/getFloorDataDetails", method = RequestMethod.POST)
	@ResponseBody
	public String getFloorDataDetails(@RequestParam("wdId") String wdId) {
		return iis.getFloorDataDetails(wdId);
	}
	@RequestMapping(value = "/getFlatDataDetails", method = RequestMethod.POST)
	@ResponseBody
	public String getFlatDataDetails(@RequestParam("wdId") String wdId) {
		return iis.getFlatDataDetails(wdId);
	}
	/*======================================= this is for work order description purpose written start======================================*/
	@RequestMapping(value = "/getFlatDetails", method = RequestMethod.POST)
	@ResponseBody
	public String getFlatDetails(@RequestParam("floorId") String floorId) {
		return iis.getFlatDetails(floorId);
	}
	@RequestMapping(value = "/getIssueCount", method = RequestMethod.POST)
	@ResponseBody
	public int getReceiveCount(@RequestParam("slipNumber") String strSlipNumber,
			@RequestParam("receiveDate") String strReceiveDate,HttpServletRequest request
	) {
		HttpSession session = null;
		session = request.getSession(true);
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String strReqMonthStart = "";
		if(strReceiveDate.contains("-")){
			String arr[] = strReceiveDate.split("-");
			String strMonth = arr[1];
			String strYear = arr[2];
			strReqMonthStart = "01"+"-"+strMonth+"-"+strYear;
		}
		return iis.getIssueCount(  strSlipNumber,  strSiteId,strReqMonthStart,  strReceiveDate);
	}
	/**
	 * @description this controller used for New issue and Material Management
	 * @return
	 */
	@RequestMapping(value = "/doIndentIssue", method ={ RequestMethod.POST,RequestMethod.GET})
	public String doIndentIssue(@ModelAttribute("indentIssueModelForm") IndentIssueBean issueModel,	BindingResult result, Model model, HttpServletRequest request, HttpSession session) {
	
		String moduleName=request.getParameter("moduleName")==null?"":request.getParameter("moduleName");
		String reqId = request.getParameter("ReqId")==null?"":request.getParameter("ReqId");
		String reqDate = request.getParameter("ReqDate")==null?"":request.getParameter("ReqDate");
		String isThisSiteWiseModule= request.getParameter("isThisSiteWiseModule")==null?"":request.getParameter("isThisSiteWiseModule");
		//if this variable having empty string means the user refreshed page so we need to provide bad request
		if(moduleName.length()==0&&reqId.length()==0&&reqDate.length()==0){
			model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";

		}
		String response = iis.indentIssueProcess(model, request, session);
		
		String viewToBeSelected = "";
		
		
	/*	if(response.equals("Success")){
			int isIssueSaved = iis.getIssueCount1(  request);

			System.out.println("response....."+response);

			if(isIssueSaved==0)
			{
				response="Failed";
				request.setAttribute("exceptionMsg", "Record Not Inserted. Try Again...");
			}
		}
*/
		model.addAttribute("moduleName", moduleName);
		if(response.equalsIgnoreCase("Success") ) {
			viewToBeSelected = "indentIssueResponse";
			//ACP
			if(moduleName.equals("MaterialManagement")){
				//if this is the request from material management then show the GRN page
				String indentType= request.getParameter("vendorissuedTypeName");
				if(indentType.equals("OUTR")||indentType.equals("OUTS")){
					viewToBeSelected = "DCviewGRN";
					if(isThisSiteWiseModule.equals("true")){
						model.addAttribute("urlForActivateSubModule", "siteWiseMaterialManagement.spring");
					}else{
						model.addAttribute("urlForActivateSubModule", "materialManagement.spring");	
					}
				}
			}
			if(moduleName.equals("indentIssue")){
				model.addAttribute("urlForActivateSubModule", "launchIndIssuePage.spring");	
			}
		} else if(response.equalsIgnoreCase("Failed")) {
			viewToBeSelected = "indentIssueResponse";
		}else if (response.equalsIgnoreCase("No Stock")  ) {

			session = request.getSession(true);
			String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

			IndentIssueBean iib = new IndentIssueBean();
			model.addAttribute("indentIssueModelForm", iib);
			model.addAttribute("productsMap", iis.loadProds(strSiteId));
			model.addAttribute("noStock", "Sorry issue failed ! Some of the item stock is insufficient on mention date");
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("blocksMap", iis.loadBlockDetails(strSiteId));
			iib.setReqId(String.valueOf(iis.getIndentEntrySequenceNumber()));		
			iib.setProjectName(iis.getProjectName(session));
			viewToBeSelected = "indentIssuePage";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			model.addAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}else if(response.equalsIgnoreCase("Expired")){
			String childProduct =request.getAttribute("childProduct").toString();
			request.setAttribute("exceptionMsg",childProduct+" Child Product Expired");
			viewToBeSelected = "indentIssueResponse";
		}else if(response.contains("BOQ")){ // this is for workorder quantity more than the issued Quantity it will executed
			request.setAttribute("exceptionMsg",response);
			viewToBeSelected = "indentIssueResponse";
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
			String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog(indentEntrySeqNum,user_id,"New Issue clicked Submit","success",site_id1);
	
		return viewToBeSelected;
	}
}
