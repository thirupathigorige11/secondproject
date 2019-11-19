package com.sumadhura.in;

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

import com.sumadhura.bean.DCFormBean;
import com.sumadhura.bean.DCFormViewBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.service.DCFormService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.transdao.DCFormDao;
import com.sumadhura.transdao.DCFormDaoImpl;
import com.sumadhura.transdao.InwardGetInvoiceDetailsDaoImpl;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class DCFormController extends UIProperties{
	@Autowired
	@Qualifier("dcFormClass")
	DCFormService dcFormService;
	
	@Autowired
	private DCFormDao dcFormDao;
	
	
	
	@Autowired
	IndentReceiveController irc;

	@Autowired
	HttpSession session;

	@Autowired
	IndentReceiveService irs;

	@RequestMapping(value = "/launchDCForm", method = RequestMethod.GET)
	public String launchDCFormPage(Model model) {
		model.addAttribute("DCModelForm", new DCFormBean());
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("productsMap", dcFormService.loadProds(site_id));
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		model.addAttribute("gstMap", dcFormService.getGSTSlabs());
		model.addAttribute("chargesMap", dcFormService.getOtherCharges());
		model.addAttribute("site_id",site_id);
		
		String allSites=validateParams.getProperty("materialIndentAllowSites") == null ? "" : validateParams.getProperty("materialIndentAllowSites").toString();
		
		model.addAttribute("siteId",site_id);
		model.addAttribute("Allsites",allSites);
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		
		audit.auditLog("0",user_id,"DC FORM View","success",site_id);
		
		
		
		return "DCForm";
	}
	@RequestMapping(value = "/dcformSubProducts", method = RequestMethod.POST)
	@ResponseBody
	public String dcformSubProducts(@RequestParam("mainProductId") String mainProductId) {
		return dcFormService.loadSubProds(mainProductId);
	}

	@RequestMapping(value = "/dcformChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String dcformChildProducts(@RequestParam("subProductId") String subProductId) {
		return dcFormService.loadChildProds(subProductId);
	}

	@RequestMapping(value = "/listdcformChildProducts", method = RequestMethod.POST)
	@ResponseBody
	public String listUnitsOfSubProducts(@RequestParam("productId") String productId) {
		return dcFormService.loadDCFormMeasurements(productId);
	}

	@RequestMapping(value = "/loadAndSetVendorInformation", method = RequestMethod.POST)
	@ResponseBody
	public String loadAndSetVendorInfo(@RequestParam("vendName") String vendName) {
		return dcFormService.getVendorInfo(vendName);
	}

	@RequestMapping(value = "/getDCCount", method = RequestMethod.POST)
	@ResponseBody
	public int getDCCount(@RequestParam("DCNumber") String strDCNO,
			@RequestParam("vendorname") String strVendorName,
			@RequestParam("receiveDate") String strReceiveDate
	) {
		return dcFormService.getDcCount(  strDCNO,  strVendorName,  strReceiveDate);
	}

	@RequestMapping(value = "/submitDCForm", method = {RequestMethod.POST,RequestMethod.GET})
	public String doIndentReceive(@ModelAttribute("DCModelForm")IndentReceiveBean indentRecModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session) {
		
		String strValue="";
		String viewToBeSelected = "";
		String site_id ="";
		String user_id="";
		String DcNumber=request.getParameter("DCNumber")==null ? "" : request.getParameter("DCNumber");
		if(DcNumber!=null && !DcNumber.equals("")){
		strValue=irc.getAndCheckReceiveBOQQuantity(request,session);
		if(!strValue.contains("BOQ")){
		String response = dcFormService.dcFormProcess(model, request, session);
	
		/*if(response.equalsIgnoreCase("Success")) {
			int saveCount = dcFormService.getDCSaveCountForVerification( model,  request, session);
			if(saveCount == 0){
				model.addAttribute("Message", "Due to backend issue DC getting failed");
				model.addAttribute("DCModelForm", new DCFormBean());
				model.addAttribute("productsMap", dcFormService.loadProds());
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("gstMap", dcFormService.getGSTSlabs());
				model.addAttribute("chargesMap", dcFormService.getOtherCharges());
				response = "Fail";
				return "DCForm";

			}
		}*/

		if(response.equalsIgnoreCase("existed"))
		{
			System.out.println("==========>>>> DCNumber already existed");
			
			SaveAuditLogDetails audit=new SaveAuditLogDetails();
			 user_id=String.valueOf(session.getAttribute("UserId"));
			 site_id = String.valueOf(session.getAttribute("SiteId"));
		//	String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
			audit.auditLog("0",user_id,"DC FORM DcNumber Existed","failed",site_id);
			
			
		
			request.setAttribute("exceptionMsg", "DCNumber Already Existed. Enter Another DCNumber.");
		}
		else if(response.equalsIgnoreCase("Success")) {


			model.addAttribute("Message", "Inwards done successfuly");
			//viewToBeSelected = "dcFormResponse";
			model.addAttribute("DCModelForm", new DCFormBean());
			model.addAttribute("productsMap", dcFormService.loadProds(site_id));
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("gstMap", dcFormService.getGSTSlabs());
			model.addAttribute("chargesMap", dcFormService.getOtherCharges());
			
			SaveAuditLogDetails audit=new SaveAuditLogDetails();
			
			//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
				 user_id=String.valueOf(session.getAttribute("UserId"));
				 site_id = String.valueOf(session.getAttribute("SiteId"));
				String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
				audit.auditLog(indentEntrySeqNum,user_id,"DC FORM Save data","success",site_id);
				
				
			
			return "viewGRN_DC";
		}
		}else{
			model.addAttribute("message1",strValue);
			return "response";
		}
		}else{
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		return viewToBeSelected;
	}


	@RequestMapping(value = "/convertDCToInvoice", method = RequestMethod.GET)
	public String covertDCToInvoice(Model model) {

		System.out.println("convertDCToInvoice");
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		model.addAttribute("url","convertDCToInvoice.spring");
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			model.addAttribute("siteId",site_id1);
			audit.auditLog("0",user_id,"ConvertDc to Invoice View","success",site_id1);
			
		

		return "ConvertDCTOInvoice";
	}


	@RequestMapping(value = "/ShowDCIdPage", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView doIndentGetDcFormId(){

		ModelAndView model = null;

		try {
			model = new ModelAndView();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("DCIdPage_Show");
		}
		return model;
	}

	@RequestMapping(value = "/InwardsFromDcForm", method ={ RequestMethod.POST,RequestMethod.GET})
	public String doIndentIssueToOtherSiteInwards(HttpServletRequest request, HttpSession session,Model model){


		String indentEntryId = "";
		String siteId = "";
		try {
			String DCNumber = request.getParameter("DCNumber") == null ? "" : request.getParameter("DCNumber");
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String vendorIdId = request.getParameter("VendorIdDCSearch") == null ? "" : request.getParameter("VendorIdDCSearch");
			String dcDate=request.getParameter("DCDate1") == null ? "" : request.getParameter("DCDate1");
			
			
			if (StringUtils.isNotBlank(siteId)&& StringUtils.isNotBlank(DCNumber)){
				indentEntryId = (String) dcFormDao.getDcEntryIdNo(vendorIdId,DCNumber,siteId,dcDate);
				List<DCFormViewBean> list= dcFormService.getDcFormLists(session,DCNumber, siteId,vendorIdId,indentEntryId);

				if(list.size() > 0){
					//indentEntryId = (String) dcFormService.getIndentIdNo(vendorIdId,DCNumber,siteId);
					session.setAttribute("dcNumber",DCNumber);
					session.setAttribute("indentEntryId",indentEntryId);

					model.addAttribute("DCFormInwardList",list);

					model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
					model.addAttribute("DCViewModelForm", new DCFormViewBean());

					model.addAttribute("DCFormProductDetails",dcFormService.getDcFormProductDetailsLists(session,DCNumber, siteId, vendorIdId,indentEntryId));
					//model.addAttribute("listOfIssueToOtherSiteTransportChargesDetails",dcFormService.getDcFormTransportChargesList(DCNumber, siteId));
					model.addAttribute("gstMap", dcFormService.getGSTSlabs());
					model.addAttribute("chargesMap", dcFormService.getOtherCharges());
					double doubleFinalAmount = Double.valueOf(session.getAttribute("doubleFinalAmount") == null ? "" : session.getAttribute("doubleFinalAmount").toString());
					model.addAttribute("doubleFinalAmount",doubleFinalAmount);
					System.out.println("indentEntryId "+indentEntryId);

				}else{


					int count = dcFormService.isDCPresentAndInactive(vendorIdId, siteId,DCNumber,dcDate);

					if(count == 1){

						request.setAttribute("Message", "DC already converted to invoice");
					}else{
						request.setAttribute("Message", "DC number does not available in our record");
					}
					return "DCIdPage_Show";
				}


			}else{

				request.setAttribute("Message", "Data Not Available With Given RequestedId");
				return "DCIdPage_Show";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 	
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("111",user_id,"ConvertedDc to Invoice","success",site_id);
			
		
		return "DcFormView";

	}

	@RequestMapping(value = "/getReceiveInvoiceCount", method = RequestMethod.POST)
	@ResponseBody
	public int getReceiveCount(@RequestParam("invoiceNumber") String strInvoicNo,
			@RequestParam("vendorname") String strVendorName,
			@RequestParam("receiveDate") String strReceiveDate
	) {
		return irs.getInvoiceCount(  strInvoicNo,  strVendorName,  strReceiveDate);
	}

	
	@RequestMapping(value = "/CovertDcWithoutPricing", method = {RequestMethod.POST,RequestMethod.GET})
	public String covertDcWithoutPricing(@ModelAttribute("DCModelForm")IndentReceiveBean indentRecModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session) {

		String response="";
		String isStatus="";
		response = dcFormService.covertDcWithoutPricing(model, request, session);
		request.setAttribute("url","convertDCToInvoice.spring");
		if(response.equals("success")){
			//request.setAttribute("Message", "<font color=green >Success. DC converted into Invoice successfully.</font>");
			isStatus="success";

		}/*else if(response.equalsIgnoreCase("PONOTSAME")){
			//request.setAttribute("Message", "<font color=red >Oops!!! DC,s from Multiple PO,s can’t be converted into a Single Invoice.</font>");
			isStatus="Error";
		}else if(response.equalsIgnoreCase("TaxOrPayment")){
			//request.setAttribute("Message", "<font color=red >It seems TaxInvoice Or paymented With This InvoiceNumber.</font>");
			isStatus="Error";
		}*/
		else{
			//request.setAttribute("Message", "<font color=red >It seems Dc already conveted to invoice or Enter Details are invalid.</font>");
			isStatus="Error";
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Convert dc to invoice click submit",isStatus,site_id);
		return "ConvertDCTOInvoice";
		
	}

	@RequestMapping(value = "/updateDCForm", method = {RequestMethod.POST,RequestMethod.GET})
	public String doIndentUpdate(@ModelAttribute("DCModelForm")IndentReceiveBean indentRecModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session) throws Exception {

		String response = dcFormService.dcFormUpdate(model, request, session);
		if(response.equals("success")){
			return "viewGRN_DC2IN";}
		else{return "";}
	}

	/*@RequestMapping(value = "/checkDCInactive", method = RequestMethod.POST)
	@ResponseBody
	public int checkDCInactive(@RequestParam("DCNumber") String strDCNO) {
		return dcFormService.checkDCInactive(strDCNO);
	}*/



@RequestMapping(value ="getDcGrnViewDts.spring", method = {RequestMethod.GET, RequestMethod.POST})
public ModelAndView getDcGrnViewDts(HttpServletRequest request) {

	String site_id = "";
	String toDate = "";
	String fromDate = "";
	ModelAndView model = null;
	String strAuditResponse ="";
	List<ViewIndentIssueDetailsBean> indentIssueData = null;
	try {
		model = new ModelAndView();
		fromDate = request.getParameter("fromDate");
		toDate = request.getParameter("toDate");
		if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
			HttpSession session = request.getSession(false);
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
			System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
			if (StringUtils.isNotBlank(site_id)) {
				indentIssueData = dcFormService.getGrnViewDetails(fromDate, toDate, site_id);
				if(indentIssueData != null && indentIssueData.size() >0){
					request.setAttribute("showGrid", "true");
					strAuditResponse = "Success";
				} else {
					model.addObject("succMessage","The above Dates Data Not Available");
					strAuditResponse = "failure";
				}
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				model.setViewName("reports/ViewDcGrn");
				//strAuditResponse = "Success";

			} else {
				model.addObject("Message","Session Expired, Please Login Again");
				model.setViewName("index");
				strAuditResponse = "Failure";
				return model;
			}
		} else {
			model.addObject("displayErrMsg", "Please Select From Date or To Date!");
			model.addObject("indentIssueData",indentIssueData);
			model.addObject("fromDate",fromDate);
			model.addObject("toDate", toDate);
			model.setViewName("reports/ViewDcGrn");
			strAuditResponse = "success";
		}
	} catch (Exception ex) {
		strAuditResponse = "Failure";
		ex.printStackTrace();
	} 
	
   
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		new SaveAuditLogDetails().auditLog("0",user_id,"Get Dc GrnDetails Viewed ",strAuditResponse,site_id1);
		
	
	
	
	
	return model;
}

@RequestMapping(value = "/getDcFormGrnViewDts.spring", method ={ RequestMethod.POST,RequestMethod.GET})
public String getDcFormGrnViewDts(HttpServletRequest request, HttpSession session,Model model){

	String dcnumber = "";
	String siteId = "";
	String viewToBeSelected = "";
	try{
		dcnumber = request.getParameter("invoiceNumber") == null ? "" : request.getParameter("invoiceNumber");
		if(dcnumber.contains("@")){dcnumber=dcnumber.replace('@','&');}
		siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(dcnumber)){

			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
			String response=dcFormService.getDcGrnDetails(dcnumber, siteId,request);
			//System.out.println("InvoiceNumber "+invoiceNumber);

			if(response.equalsIgnoreCase("Success")) {

				viewToBeSelected = "viewGRN_DC";
			}

		}	
	}catch(Exception ex) {
		ex.printStackTrace();
	} 


	return viewToBeSelected;
}

@RequestMapping(value = "/convertPOtoDC", method = {RequestMethod.POST,RequestMethod.GET})
public String convertPOtoDC(@ModelAttribute("CreatePOModelForm")ProductDetails objProductDetails, BindingResult result, Model model, HttpServletRequest request, HttpSession session) {
	String strValue="";
	String poNumber=request.getParameter("poNo")==null ? "" : request.getParameter("poNo");
	if(poNumber!=null && !poNumber.equals("")){
	strValue=irc.getAndCheckReceiveBOQQuantity(request,session);
	if(!strValue.contains("BOQ")){
	String response = dcFormService.processingPOasDC(model, request, session);
	String viewToBeSelected = "";


	if(response.equalsIgnoreCase("existed"))
	{
		System.out.println("==========>>>> DCNumber already existed");
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"DC FORM DcNumber Existed","failed",site_id);
		
		
	
		model.addAttribute("response1", "DCNumber Already Existed. Enter Another DCNumber.");
		 return "response";
	}
	else if(response.equalsIgnoreCase("Success")) {


		model.addAttribute("Message", "Inwards done successfuly");
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			String indentEntrySeqNum=String.valueOf(session.getAttribute("indentEntrySeqNum"));
			audit.auditLog(indentEntrySeqNum,user_id,"Converting PO to DC","success",site_id);
			
			
			if(request.getAttribute("hasCreditNote")==null){
				return "viewGRN_DC";
			}
			else{
				String dcEntryId = request.getAttribute("dcEntryNo").toString();
				new DCFormDaoImpl().getGrnViewDetails1(model,dcEntryId,request);
				//return "ViewGrnButtonsForDC"; //"viewGRN_DC";
				String poEntryId = request.getParameter("poEntryId");
				
				return "redirect:/getDcGrnAndCreditNoteGrnViewDetails.spring?dcEntryId="+dcEntryId+"&poEntryId="+poEntryId;
				
			}
	}
	
	 else{
		 model.addAttribute("response1"," Error. PO not converted into DC ");
		 return "response";
				 }
	
	
	}
	else{
		model.addAttribute("message1",strValue);
		return "response";
	}
	}
	else{
		model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
		return "response";
	}
	}

@RequestMapping(value = "/getDcGrnAndCreditNoteGrnViewDetails", method = RequestMethod.GET)
public String getGrnAndCreditNoteGrnViewDetails(Model model, HttpServletRequest request, HttpSession session) {
	String viewToBeSelected = "";
	String dcEntryId = request.getParameter("dcEntryId");
	new DCFormDaoImpl().getGrnViewDetails1(model,dcEntryId,request);
	viewToBeSelected  = "ViewGrnButtonsForDC"; 
	return viewToBeSelected;
}
// this is used to test whether dc is existed or not and active oir not
@RequestMapping(value = "/checkDcNumberExisted", method = RequestMethod.POST)
@ResponseBody
public String checkDcNumberExisted(@RequestParam("dcNumber") String dcNumber,
		@RequestParam("vendorId") String vendorId,
		@RequestParam("DcDate") String DcDate,@RequestParam("siteId") String siteId
) {
	return dcFormService.checkDcNumberExisted(dcNumber,vendorId, DcDate,siteId);
}
@RequestMapping(value = "/getTransportorData", method = RequestMethod.POST)
@ResponseBody
public String getTransportorData(@RequestParam("transportorName") String transportorName) {
	return dcFormService.getTransportorData(transportorName);
}



@RequestMapping(value = "/getTransportorId", method = RequestMethod.POST)
@ResponseBody
public String getTransportorId(@RequestParam("transportorName") String transportorName) {
	return dcFormService.getTransportorId(transportorName);
}


}







































