package com.sumadhura.in;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.MenuDetails;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.service.ContrctorBillGeneraterService;
import com.sumadhura.service.WorkOrderService;
import com.sumadhura.util.AESDecrypt;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
/**
 * 
 * @author home
 * @description this controller is used for all work order operation's and here the data will be loaded by site wise
 */
@Controller
public class WorkOrderController {
	static Logger log = Logger.getLogger(WorkOrderController.class);
	@Autowired
	@Qualifier("workControllerService")
	WorkOrderService workControllerService;
	
	@Autowired
	@Qualifier("wobill_generater_service")
	ContrctorBillGeneraterService workBillControllerServiceObject;
	
	
/*	--data base query for selecting  allocated quantity for particular area using work area id, pass work area id
	select WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY ,QWI.QS_WORKORDER_ISSUE_ID
	from  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_ISSUE_DETAILS DTLS ,QS_WORKORDER_ISSUE QWI 
	WHERE  DTLS.WO_WORK_ISSUE_DTLS_ID=QWIA.WO_WORK_ISSUE_DTLS_ID AND DTLS.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID
	--and  DTLS.QS_WORKORDER_ISSUE_ID=1929  
	and QWIA.STATUS='A'  AND QWI.STATUS='A' AND WO_WORK_AREA_ID='WOAREA643901' ;


	SELECT WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY,QWI.PER_WORK_ORDER_NUMBER 
	FROM  QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS   ,QS_WORKORDER_TEMP_ISSUE QWI
	WHERE  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QWIA.WO_WORK_TEMP_ISSUE_DTLS_ID AND DTLS.QS_WO_TEMP_ISSUE_ID=QWI.QS_WO_TEMP_ISSUE_ID 
	--AND  QS_WO_TEMP_ISSUE_ID=?
	AND QWIA.STATUS='A' AND DTLS.STATUS='A' 
	AND QWI.STATUS='A' AND WO_WORK_AREA_ID='WOAREA643901' ;*/
	
	/**
	 * @author Aniket Chavan
	 * @description this controller(workOrder.spring) will forward to the work order view page, taking with the necessary data like major head names
	 * @param model is object which is holding the data is may be work order number or temporary work order number
	 * @param request object used for adding the data in request scope
	 * @param session is holding the user data which is used valid the user
	 * @return view name 
	 */
	@RequestMapping(value = "/workOrder.spring", method = RequestMethod.GET)
	public String doWorkOrder(Model model, HttpServletRequest request, HttpSession session) {
	
		WorkOrderBean workOrderBean = new WorkOrderBean();
		String woTypeOfWork="";
		StringBuffer workOrderType=new StringBuffer("");
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		woTypeOfWork=request.getParameter("woTypeOfWork")==null?"":request.getParameter("woTypeOfWork");
		workOrderBean.setSiteId(siteId);
		//workOrderType=UIProperties.validateParams.getProperty("workOrderType") == null ? "" : UIProperties.validateParams.getProperty("workOrderType").toString();
		
		model.addAttribute("urlForActionTag", "workOrder.spring");
   		model.addAttribute("pageName", "Work Order");
   		
		if(woTypeOfWork.length()==0){
			
			List<Map<String, Object>> list=workControllerService.userAbleToCreateWOTypes(user_id,workOrderBean);
			 for (Map<String, Object> map : list) {
				String moduleType=map.get("MODULE_TYPE")==null?"":map.get("MODULE_TYPE").toString();
				if(moduleType.equals("WOC")){
					workOrderType.append("<option value='PIECEWORK'>Piece Work</option>");
				}else if(moduleType.equals("WOM")){
					workOrderType.append("<option value='MATERIAL'>Material</option>");
				}else if(moduleType.equals("CONSULTANT")){
					workOrderType.append("<option value='CONSULTANT'>Consultant</option>");
				}else{
					
				}
	 		}
			 model.addAttribute("workOrderType", workOrderType.toString());
			return "WorkOrder/SelectWOTypeOfWork";
		}
		
		String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
		String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
		//here loading all the site id's available in properties
		String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
		
		List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
		//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg 
		if(!enableWOSubModulesSiteList.contains(siteId)){
			model.addAttribute("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
			return "response";
		}
		
		workOrderBean.setSiteId(siteId);
		workOrderBean.setTypeOfWork(woTypeOfWork);
		//loading the next level approver using this method with the help of bean object and user_id
		workOrderBean = workControllerService.getWorkOrderFromAndToDetails(user_id, workOrderBean);
		//loading all the major head names
		Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,woTypeOfWork);
		//loading the temporary work order number site wise
		int QSWorkOrderNo = workControllerService.getMaxQSWorkOrderNoSiteWise(siteId);
		//loading permanent work order number while creating the work order
		String workOrderNo = workControllerService.getWorkOrderNoSiteWise(siteId,siteName,woTypeOfWork);
		
		workOrderBean.setSiteWiseWONO(String.valueOf(QSWorkOrderNo));
		workOrderBean.setWorkOrderNo(workOrderNo);
	
		model.addAttribute("WorkOrderBean", workOrderBean);
		model.addAttribute("workMajorHead", WO_QSList);
		//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));

		// if the approval employee id is null then redirect to work order
		if (workOrderBean.getApproverEmpId() == null ) {
			model.addAttribute("response1", "You Cannot Create Work Order.");
			//model.addAttribute("javaScriptProp","<script>document.getElementById('saveBtnId').style.display='none';document.getElementById('workOrderFormId').style.display='none';</script>");
			return "response";
		}else if (workOrderBean.getApproverEmpId().equals("-")) {
			workOrderBean.setApproverEmpId("END");
		}
		//loading default terms and condition's while creating work order number
		List<WorkOrderBean> listTermsAndCondition = workControllerService.getTermsAndConditions(siteId);
		//model.addAttribute("optionalCCmails", UIProperties.validateParams.getProperty(siteId+"_WORKORDER_CCMAILS") == null ? "00" : UIProperties.validateParams.getProperty(siteId+"_WORKORDER_CCMAILS").toString());
		
		model.addAttribute("listTermsAndCondition", listTermsAndCondition);
		model.addAttribute("TC_listSize", listTermsAndCondition.size());

		//SaveAuditLogDetails audit = new SaveAuditLogDetails();
		SaveAuditLogDetails.auditLog("", user_id, "New Work Order View", "success", siteId);
		return "WorkOrder/WorkOrder";
	}
/**
 * @author Aniket Chavan
 * @description this controller is used to load the minor head names
 * @param majorHeadId is the request @param which is holding 
 * @param request object is used to get the data from client side
 * @param session object is used to get the site id for loading the work order minor head data site wise
 * @return returning response as part of JSON object
 */
	@RequestMapping(value = "/workOrderSubProducts.spring", method = RequestMethod.POST)
	@ResponseBody
	public String workOrderSubProducts(@RequestParam("mainProductId") String majorHeadId, HttpServletRequest request, HttpSession session) {
	
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
		
		String requestFrom=request.getParameter("requestFrom")==null?"":request.getParameter("requestFrom");
		String isApproveWOPage=request.getParameter("isApproveWOPage");
		if(isApproveWOPage!=null){
			 siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		}else{
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
		}
		if(requestFrom.equals("BOQ")){
			if(typeOfWork.equals("PIECEWORK")){ typeOfWork = "PIECEWORK"; }
			siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		}
		
		return workControllerService.loadWOSubProds(majorHeadId,siteId,typeOfWork);
	}

	/**
	 * @author Aniket Chavan
     * @description this controller is used to load the work description of the work order
	 * @param minorHeadId is used for load it's child product's
	 * @param request object is used to get the data from client side
	 * @param session object is used to get the site id for loading the work order work desc data site wise	
	 * @return returning response as part of JSON object
	 */
	@RequestMapping(value = "/workOrderChildProducts.spring", method = RequestMethod.POST)
	@ResponseBody
	public String workOrderChildProducts(@RequestParam("subProductId") String minorHeadId,HttpServletRequest request, HttpSession session) {
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
		
		String requestFrom=request.getParameter("requestFrom")==null?"":request.getParameter("requestFrom");
		String isApproveWOPage=request.getParameter("isApproveWOPage");
		if(isApproveWOPage!=null){
			 siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		}else{
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
		}
		if(requestFrom.equals("BOQ")){
			if(typeOfWork.equals("PIECEWORK")){ typeOfWork = "PIECEWORK"; }
			siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		}
		
		return workControllerService.loadWOChildProds(minorHeadId,siteId,typeOfWork);
	}
	
	/**
	 * @author Aniket Chavan
	 * @param workDescId is used to load it's measurement types
	 * @return  returning response as part of JSON object
	 */
	@RequestMapping(value = "/listOfWOmesurment.spring", method = RequestMethod.POST)
	@ResponseBody
	public String workOrderlistUnitsOfSubProducts(@RequestParam("childProductId") String workDescId,HttpServletRequest request, HttpSession session) {
		String isApproveWOPage=request.getParameter("isApproveWOPage");
		String siteId="";
		if(isApproveWOPage!=null){
			 siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		}else{
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();	
		}
		String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
		return workControllerService.loadWorkOrderMeasurements(workDescId,siteId,typeOfWork);
	}
	
	/**
	 * @author Aniket Chavan
     * @description this controller is used to checking user manually entered work order is exits or not
     * @param request object is used to get the data from client side
	 * @param session object is used to get user data for operation
	 * @return returning true if work order number exits else false
	 */
	@RequestMapping(value = "/checkWorkOrderNoExistsOrNot.spring", method = RequestMethod.GET)
	@ResponseBody
	public String checkWorkOrderNo(HttpServletRequest request, HttpSession session) {
		String flag="";
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
			flag= workControllerService.checkWorkOrderNoExistsOrNot(workOrderNo,siteId);
		} catch (Exception e) {
			log.info(e.getMessage());	
		}
		return flag;
	}

	/**
	 * @description this controller is used
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkIsDraftWorkOrderExistsOrNot.spring", method = RequestMethod.GET)
	@ResponseBody
	public String checkIsDraftWorkOrderExistsOrNot(HttpServletRequest request, HttpSession session) {
		String flag="";
		try {
		
			flag= workControllerService.checkIsDraftWorkOrderExistsOrNot(request,session);
		} catch (Exception e) {
			log.info(e.getMessage());	
		}
		return flag;
	}

	
	/**
	 * @description this controller used to return all the work order which
	 *              related to contractor
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getWorkOrderNo.spring", method = RequestMethod.POST)
	@ResponseBody
	public String getWorkOrderDetails(HttpServletRequest request) {
		String workDescId = request.getParameter("workDescId") == null ? "" : request.getParameter("workDescId").toString();
		String strControctorId = request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId").toString();
		String strTypeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").toString();
		int siteId = Integer.parseInt(request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim());
		// calling service layer to get work order number's
		return workControllerService.getWorkOrderDetails(workDescId, strControctorId, siteId, strTypeOfWork);
	}

	
	/**
	 * @description this controller used to return all the work order for work order
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/autoCompleteWorkOrderNo.spring", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> autoCompleteWorkOrderNo(HttpServletRequest request) {
			// calling service layer to get work order number's
		return workControllerService.autoCompleteWorkOrderNo(request);
	}
	
	/**
	 * @description this controller is used for showing material issued details for work order
	 * it will show material group name ,total quantity and issued quantity available quantity etc...
	 * @param workOrderBean
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/viewProductDetails.spring", method = RequestMethod.POST)
	public ModelAndView viewProductDetails(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,
			HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		log.info("inside  viewProductDetails controller");
		List<Map<String, Object>> list=	new ArrayList<Map<String, Object>>();
		//calling service layer to return the work order material issued data
		String nextLevelApprovalEmpId=workOrderBean.getApproverEmpId();
		String isApproveWorkOrder=request.getParameter("isApproveWorkOrder")==null?"":request.getParameter("isApproveWorkOrder");
		if(nextLevelApprovalEmpId.equals("END")&&isApproveWorkOrder.length()==0){
			list=workControllerService.getMaterialBOQProductDetails(workOrderBean);
			model.addObject("urlForActivateSubModule", "viewMyWorkOrders.spring");
		}else{
			list=workControllerService.getTempMaterialBOQProductDetails(workOrderBean);
			if(workOrderBean.getIsCommonApproval()!=null){
				if(workOrderBean.getIsCommonApproval().equals("true")){
					model.addObject("urlForActivateSubModule", "viewWOforApproval.spring");	
				}else if(workOrderBean.getIsCommonApproval().equals("false")){
					model.addObject("urlForActivateSubModule", "viewPendingWOforApprove.spring");
				}else{
					model.addObject("urlForActivateSubModule", "ViewMyWOStatus.spring");
				}
			}
		}
	 	model.addObject("MaterialBOQProductDetails", list);
		model.setViewName("WorkOrder/ViewMaterialBOQDetials");
		return model;
	}
	//Show the data WD Wise
	@RequestMapping(value = "/viewMaterialProductWDDetails.spring", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> viewProductDetails(HttpServletRequest request, HttpSession session) {		 
		log.info("WorkOrderController.viewProductDetails()");
		List<Map<String, Object>> list=	new ArrayList<Map<String, Object>>();
		list=workControllerService.getTempWorkDescMaterialBOQProductDetails(request,session);
		return list;
	}
	
	//Show the data WD Area Wise
	@RequestMapping(value = "/viewMaterialAreaWiseDetails.spring", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> viewMaterialAreaWiseDetails(HttpServletRequest request, HttpSession session) {		 
		log.info("WorkOrderController.viewMaterialAreaWiseDetails()");
		List<Map<String, Object>> list=	new ArrayList<Map<String, Object>>();
		list=workControllerService.getTempWorkAreaWiseMaterialDetails(request,session);
		return list;
	}

	
	
	/**
	 * @description this controller used to validate material BOQ 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/validateWOMaterialBOQDetails.spring", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> checkMaterialDetails(HttpServletRequest request, HttpSession session) {
		Map<String, String> errorMessages = null;
		
		
		try {
			 errorMessages= workControllerService.validateWOMaterialBOQDetails(request);
		} catch (Exception e) {
			log.info(e.getMessage());	
			e.printStackTrace();
		}
		return errorMessages;
	}
	
	/**
	 * @description this controller used for loading the scope of work and the amount of BOQ
	 * @return scope of work and the BOQ amount
	 */
	@RequestMapping(value = "/loadScopeOfWork_AmountAndQty.spring", method = RequestMethod.GET)
	@ResponseBody
	public String scopeOfWorkAndBOQAmount(HttpServletRequest request, HttpSession session) {
		String childProductId=request.getParameter("childProductId");
		String minorProductId=request.getParameter("WO_MinorHead");
		String mesurmentId=request.getParameter("mesumentId");
		String typeOfWork=request.getParameter("typeOfWork");
		String site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
		
		String isApproveOrReviseWOPage=request.getParameter("isApproveOrRevisePage")==null?"false":request.getParameter("isApproveOrRevisePage");
		String scopeOfWorkAndBOQAmount= " @@0@@0";
		WorkOrderBean bean=new WorkOrderBean();
		bean.setTypeOfWork(typeOfWork);
		bean.setSiteId(site_id);
		bean.setUnitsOfMeasurement1(mesurmentId);
		bean.setWO_Desc1(childProductId);
		bean.setWO_MinorHead1(minorProductId);
		try {
			//if this request coming from creation of any work order time then this mwthod will execute
			if(isApproveOrReviseWOPage.equals("false")){
			 scopeOfWorkAndBOQAmount= workControllerService.loadScopeOfWork(bean);
			}else{
				String boqNo=request.getParameter("boqNo")==null?"":request.getParameter("boqNo");
				bean.setBoqNo(boqNo);
				scopeOfWorkAndBOQAmount= workControllerService.loadScopeOfWorkAndBOQAmount(bean);
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return scopeOfWorkAndBOQAmount;
	}	
	
	/**@description this controller is used for auto complete event of browser and also load the whole data of the contractor
	 * @param contractorName is holding the name of the contractor using this we can load the whole data of the contractor
	 * @param loadcontractorData is the variable that is indicating which data should be load
	 * @return contractor data
	 */
	@RequestMapping(value = "/loadAndSetVendorInfoForWO", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getContractorInfo(@RequestParam("contractorName") String contractorName,
			@RequestParam("loadcontractorData") String loadcontractorData, HttpServletRequest request) {
		
		String fromIndentReturns = request.getParameter("indentReturns") == null ? ""	: request.getParameter("indentReturns");
		List<String> vendorData = workControllerService.getContractorInfo(contractorName, loadcontractorData,fromIndentReturns);
	
		return vendorData;
	}

	@RequestMapping(value = "/loadWOAreaForRevise.spring", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> loadWOAreaForRevise(HttpServletRequest request, HttpSession session) {
		//ModelAndView mav = new ModelAndView();
		String wo_work_issue_dtls_id = request.getParameter("workOrderID");
		String workOrderNo= request.getParameter("workOrderNo");
		//String acceptedRate = request.getParameter("acceptedRate");
		String typeOfWork = request.getParameter("typeOfWork");
		String workDescId = request.getParameter("mesurmentId");
		String siteId=request.getParameter("site_id");
		String boqNo=request.getParameter("boqNo");
		String unitsOfMeasurementId=request.getParameter("UnitsOfMeasurementId")==null?"":request.getParameter("UnitsOfMeasurementId");
		String loadNewData=request.getParameter("loadNewData")==null?"false":request.getParameter("loadNewData");
		WorkOrderBean  bean=new WorkOrderBean();
		bean.setWorkOrderNo(workOrderNo);
		//bean.setAcceptedRate1(acceptedRate);
		bean.setWO_Desc1(workDescId);
		bean.setSiteId(siteId);
		bean.setBoqNo(boqNo);
		bean.setTypeOfWork(typeOfWork);
		bean.setUnitsOfMeasurement1(unitsOfMeasurementId);
		bean.setQS_Temp_Issue_Dtls_Id(wo_work_issue_dtls_id);
		
		if(loadNewData.equals("true")){
			List<Map<String, Object>> qsAreaMapping = workControllerService.loadWOAreaMappingForRevise(bean);
			return qsAreaMapping;
		}
		//this method is for approve work order area 
		List<Map<String, Object>> qsAreaMapping = workControllerService.loadWOAreaMappingForReviseByWorkOrderNo(bean);
		
		return qsAreaMapping;
	}
	
	/**
	 * @description this controller used for loading the work area mapping for work order
	 * @return returning response as part of JSON object
	 */
	@RequestMapping(value = "/loadWOAreaMapping.spring", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> loadWOAreaMapping(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String workOrderIssueId = request.getParameter("workOrderID");
		String typeOfWork = request.getParameter("typeOfWork");
		String operType = request.getParameter("operType");
		String workDescId = request.getParameter("mesurmentId");
		String siteId1=request.getParameter("siteId");
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String unitsOfMeasurementId=request.getParameter("UnitsOfMeasurementId")==null?"":request.getParameter("UnitsOfMeasurementId");
	
		String siteId = (String) CheckSessionValidation.validateUserSession(mav, session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
		
		String isApproveWOPage=request.getParameter("isApproveWOPage");
		if(isApproveWOPage!=null){
			 siteId = siteId1;
		} 
	   
		// if workOrderIssueId and acceptedRate ==null then select the work
		// order area for new work order
		if (workOrderIssueId == null) {
			//this method is for new work order area 
			List<Map<String, Object>> qsAreaMapping = workControllerService.loadWOAreaMapping(siteId, workDescId,unitsOfMeasurementId,typeOfWork);
			return qsAreaMapping;
		} else {
		
				//this method is for if the work order is in approval level then we have select the selected data when the client has created work order
			List<Map<String, Object>> qsAreaMapping = workControllerService.loadWOAreaMappingByWorkOrderNo(workOrderNo,
					siteId1, workOrderIssueId, operType, workDescId,unitsOfMeasurementId,typeOfWork);
			return qsAreaMapping;
		}
	}
	
	public boolean isValidAccessForLoggedUser(HttpServletRequest request, HttpSession session, List<MenuDetails> list, String urlForValidation) {
		 boolean isValidUrl=false;
	 	for (MenuDetails menuDetails : list) {
			 if(menuDetails.getMappingLink().equals(urlForValidation)){
				 isValidUrl=true;
				 break;
			 }
		}
		return isValidUrl;
	}
	
/**
 * @description this controller is used save the PIECE WORK work order details
 * @param workOrderBean is holding the work order details

 */
	@RequestMapping(value = "/saveWorkOrder.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveWorkOrder(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,
			HttpServletRequest request, HttpSession session, Model model) {

		String response = "";
		String urlForActivateSubModule="";
		String isSaveOrUpdateOperation=workOrderBean.getIsSaveOrUpdateOperation();
		ModelAndView modelAndView = new ModelAndView();
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id1 = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null?"":request.getParameter("numbeOfRowsToBeProcessed");
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		if(recordsCount.length()==0&&workOrderNo.length()==0&&contractorId.length()==0){
			modelAndView.addObject("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			modelAndView.setViewName("response");
			return modelAndView;
		}
		 boolean isValidUrl=false;
		 List<MenuDetails> list  = (List<MenuDetails>)session.getAttribute("menu");
		 if(list==null){
			modelAndView.addObject("message1", "Oops !!! There was a improper request found. session invalidated please login again.");
			modelAndView.setViewName("response");
			return modelAndView;
		 }
		 //checking is url is available in menu session attribute or not if not available then wrong user trying to access sub module
		 isValidUrl=isValidAccessForLoggedUser(request,session,list,"workOrder.spring");
		
		 if(!isValidUrl){
				modelAndView.addObject("message1", "Oops !!! There was a improper request found. You can not access this module.");
				modelAndView.setViewName("response");
				return modelAndView;
		 }
		
		WorkOrderBean orderBean = null;
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		//loadSiteAddress is used to load the site address for work order print page
		List<String> billingSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		request.setAttribute("SiteAddress", billingSiteAddress);
		try {
			//cloning the object so main object should not get any affect so we can use it with actual values
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
	

		try {
			orderBean.setSiteId(siteId);
			orderBean.setUserId(user_id1);
			response = workControllerService.doWorkOrderEntry(orderBean, request, model, siteId, user_id1, session);
			if (response.equals("success")) {
				model.addAttribute("WorkOrderBean", orderBean);
				//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				
				//loading work order created user names and designation and operation performed name
				List<Map<String, Object>>	listOfVerifiedEmpNames=workControllerService.getWorkOrderVerifiedEmpNames(orderBean);
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
				modelAndView.setViewName("WorkOrder/workorderPrintpage");
				if(isSaveOrUpdateOperation!=null&&isSaveOrUpdateOperation.equals("Modify WorkOrder")){
					urlForActivateSubModule="openWorkOrderToModify.spring";
				}else{
					urlForActivateSubModule="workOrder.spring";
				}
				//this url is for highlighting sub module
				model.addAttribute("urlForActivateSubModule", urlForActivateSubModule);
				return modelAndView;
			}else if("not changed work order".equals(response)){
				response="No changes has made, this work order cannot be modify.";
				model.addAttribute("response1", response);
			} else if(response.equals("Work Order Number already exists")){
				response = "Work Order Creation Failed, You manually entered work order number ("+workOrderBean.getWorkOrderNo()+") is already exists in record please try with another work order number.";
				model.addAttribute("response1", response);
			} else {
				response = "Work Order Creation Failed.";
				model.addAttribute("response1", response);
			}
			model.addAttribute("responseMessage", response);
		} catch (Exception e) {
			response = "Work order creation failed.";
			model.addAttribute("response1", response);
			e.printStackTrace();
		}
 
		modelAndView.setViewName("response");
		return modelAndView;
	}
	
	/**
	 * @description this controller is for revise work order 
	 * @param workOrderBean object holding the data related to work order creation like work order date, work order name,contractor name
	 */
	@RequestMapping(value = "/saveReviseWorkOrder.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveReviseWorkOrder(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,
			HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("urlForActivateSubModule", "reviseWorkOrder.spring");
		ModelAndView modelAndView = new ModelAndView();
		boolean isValidWorkOrderForRevise = true;
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id1 = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String isSaveOrUpdateOperation=workOrderBean.getIsSaveOrUpdateOperation();
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null?"":request.getParameter("numbeOfRowsToBeProcessed");
		String workOrderNo=request.getParameter("reviseWorkOrderNumber1")==null?"":request.getParameter("reviseWorkOrderNumber1");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		
		if(recordsCount.length()==0&&workOrderNo.length()==0&&contractorId.length()==0){
			modelAndView.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			modelAndView.setViewName("response");
			return modelAndView;
		}

		@SuppressWarnings("unchecked")
		Map<String, String> holdingReviseWorkOrderNumbers = (Map<String, String>) session.getAttribute("RevisedWorkOrderNumber");
		if (holdingReviseWorkOrderNumbers == null) {
			holdingReviseWorkOrderNumbers = new HashMap<String, String>();
		}

		
		session.setAttribute("RevisedWorkOrderNumber", holdingReviseWorkOrderNumbers);
		
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		//loadSiteAddress is used to load the site address for work order print page
		List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		request.setAttribute("SiteAddress", loadSiteAddress);
		WorkOrderBean orderBean = null;
		String workOrderNumber=request.getParameter("siteWiseWONO")==null?"":request.getParameter("siteWiseWONO");
		try {
			//cloning the object so main object should not get any affect so we can use it
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
		String response = "";
		String operationType="";
		try {
			orderBean.setSiteId(siteId);
			orderBean.setUserId(user_id1);
			if (isSaveOrUpdateOperation.equals("Modify WorkOrder")) {
				operationType = "modifyReviseWorkOrder";
			} else {
				operationType = "reviseWorkOrder";
			}
			isValidWorkOrderForRevise = workControllerService.checkCompletedWorkOrderNumberIsValidForEmployee(workOrderBean.getSiteWiseWONO(), user_id1,false, siteId,operationType);
			if (!isValidWorkOrderForRevise) {
				modelAndView.addObject("message1", "Work order number is invalid for revise. might be work order already revised.");
				modelAndView.setViewName("response");
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + siteId+user_id1);
				return modelAndView;
			}
			if (!holdingReviseWorkOrderNumbers.containsKey(workOrderNo + contractorId + siteId+user_id1)) {
				holdingReviseWorkOrderNumbers.put(workOrderNo + contractorId + siteId+user_id1, workOrderNo);
			} else {
				modelAndView.addObject("message1", "Oops !!! got duplicate work order entry, your work order might be revised please check in view work order status, if not revised please try again.");
				modelAndView.setViewName("response");
				return modelAndView;
			}
			response = workControllerService.doReviseWorkOrder(orderBean, request, model, siteId, user_id1, session);
			if (response.equals("success")) {
				model.addAttribute("WorkOrderBean", orderBean);
				//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				
				//loading work order created user names and designation and operation performed name
				List<Map<String, Object>>	listOfVerifiedEmpNames=workControllerService.getWorkOrderVerifiedEmpNames(orderBean);
				
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
				String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
				if("END".equals("END")){
					//fileSource variable holding the temporary work order file path
					File fileSource = new File(rootFilePath+siteId+"\\"+workOrderNumber.replace("/", "-"));
					
					//destination variable holding the permanent work order file path	
					//destination object is modified in service layer
					File destination = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-"));
					//here constructing new folder for permanent work order
					if (!destination.exists())
						destination.mkdirs();
					
					try {
						//copying all the temporary work order attachments to permanent work order attachments bcoz this is this final approval of work order
					    FileUtils.copyDirectory(fileSource, destination);
					} catch (IOException e) {
					    e.printStackTrace();
					}
					//counting image and pdf 
					int imageCount=0;
					int pdfCount=0;
					try {
						int count = 0;
						String siteWiseNO=workOrderBean.getSiteWiseWONO().replace("/", "-");
						imageCount=0;
						for(int i=0;i<8;i++){
							File tempimageFile = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".jpg");
							File temppdfFile = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".pdf");
							//checking pdf file is exits or not if exits then rename the file name from temp to permanent file
							if(temppdfFile.exists()){
								String permanentpdffilePath = destination.getAbsolutePath()+ File.separator + orderBean.getSiteWiseWONO().replace("/", "-")+"_Part"+pdfCount+".pdf";
								pdfCount++;
								File convFile = new File(permanentpdffilePath);
								 convFile = new File(permanentpdffilePath);
								 if(temppdfFile.renameTo(convFile)){
							            log.info("File rename success");;
							     }else{
							            log.info("File rename failed");
							     }
							}
							//checking image file is exits or not if exits then rename the file name from temp to permanent file
							if(tempimageFile.exists()){
								String permanentImagefilePath = destination.getAbsolutePath()+ File.separator + orderBean.getSiteWiseWONO().replace("/", "-")+"_Part"+imageCount+".jpg";
								imageCount++;
								File convFile = new File(permanentImagefilePath);
								 convFile = new File(permanentImagefilePath);
								 if(tempimageFile.renameTo(convFile)){
							            log.info("File rename success");
							        }else{
							            log.info("File rename failed");
							        }
							}
						}
						//add image count in model attribute so we can know that how many images uploaded before this
						//request.setAttribute("imagecount", count);	
						//deleting the temporary folder permanently
						//boolean falg=fileSource.delete();
						} catch (Exception e) {
							e.printStackTrace();
						} 
				}
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + siteId+user_id1);
				modelAndView.setViewName("WorkOrder/workorderPrintpage");
				return modelAndView;
			}else if("not changed work order".equals(response)){
				response="No changes has made, this work order cannot be revised.";
				if(isSaveOrUpdateOperation.equals("Draft Work Order")){
					response="No changes has made, this work order cannot be draft.";
				}else if(isSaveOrUpdateOperation.equals("Modify WorkOrder")){
					response="No changes has made, this work order cannot be modify.";
				}
				model.addAttribute("response1", response);
			} else if(response.equals("error in material area")){
				response = "Revise Work Order Failed, Got an issue in work order Material. please recheck the work order material area.";
				model.addAttribute("response1", response);
				model.addAttribute("materialWOErrorListKey", request.getAttribute(orderBean.getWorkOrderNo()));
			} else {
				if(isSaveOrUpdateOperation.equals("Draft Work Order")){
					response = "Revise work order draft operation failed.";
				}else if(isSaveOrUpdateOperation.equals("Modify WorkOrder")){
					response = "Revise work order modify operation failed.";
				}else{
					response = "Revise work order failed.";
				}
				model.addAttribute("response1", response);
			}
			model.addAttribute("responseMessage", response);
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info(isSaveOrUpdateOperation);
			if(e.getMessage()!=null&&e.getMessage().equals("not changed work order")){
				response="You hav'nt changed work order. Work order will not able to revise.";
			}else{ 
				if(isSaveOrUpdateOperation.equals("Draft Work Order")){
					response = "Revise work order draft operation failed.";
				}else{
					response = "Revise Work Order  Failed.";
				}
			}
			model.addAttribute("message", response);
			e.printStackTrace();
		}
		holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + siteId+user_id1);
		
		//loading default terms and condition's while creating work orde number
/*		List<WorkOrderBean> listTermsAndCondition = workControllerService.getTermsAndConditions(siteId);
		model.addAttribute("listTermsAndCondition", listTermsAndCondition);
		model.addAttribute("TC_listSize", listTermsAndCondition.size());

		WorkOrderBean bean = new WorkOrderBean();
		//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		//loading the temporary work order number site wise
		int QSWorkOrderNo = workControllerService.getMaxQSWorkOrderNoSiteWise(siteId);
		Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,"PIECEWORK");
		bean.setSiteWiseWONO(String.valueOf(QSWorkOrderNo));
		model.addAttribute("workMajorHead", WO_QSList);
		model.addAttribute("WorkOrderBean", bean);*/

		modelAndView.setViewName("response");
		return modelAndView;
	}
	
	/**
	 * @description this controller is for revise NMR work order 
	 * @param workOrderBean object holding the data related to work order creation like work order date, work order name,contractor name
	 */
	@RequestMapping(value = "/saveReviseNMRWorkOrder.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveReviseNMRWorkOrder(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,HttpServletRequest request, HttpSession session, Model model) {
		//this url is for highlighting sub module
		model.addAttribute("urlForActivateSubModule", "reviseNMRWorkOrder.spring");
		ModelAndView modelAndView = new ModelAndView();
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id1 = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
		String contractorName = request.getParameter("contractorName")==null?"":request.getParameter("contractorName");
		String workOrderNo=request.getParameter("reviseWorkOrderNumber1")==null?"":request.getParameter("reviseWorkOrderNumber1");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		if(contractorName.length()==0&&workOrderNo.length()==0&&contractorId.length()==0){
			modelAndView.addObject("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			modelAndView.setViewName("response");
			return modelAndView;
		}
		
		WorkOrderBean orderBean = null;
		String workOrderNumber=request.getParameter("siteWiseWONO")==null?"":request.getParameter("siteWiseWONO");
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		//loadSiteAddress is used to load the site address for work order print page
		List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		request.setAttribute("SiteAddress", loadSiteAddress);
		try {
			//cloning the object so main object should not get any affect so we can use it
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
	
		String response = "";
		try {
			orderBean.setSiteId(siteId);
			orderBean.setUserId(user_id1);
			response = workControllerService.doReviseNMRWorkOrderEntry(orderBean, request, model, siteId, user_id1, session);
			if (response.equals("success")) {
				model.addAttribute("WorkOrderBean", orderBean);
				//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));

				//loading work order created user names and designation and operation performed name
				List<Map<String, Object>>	listOfVerifiedEmpNames=workControllerService.getWorkOrderVerifiedEmpNames(orderBean);
				
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
				String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
				if("END".equals("END")){
					//fileSource variable holding the temporary work order file path
					File fileSource = new File(rootFilePath+siteId+"\\"+workOrderNumber.replace("/", "-"));
					
					//destination variable holding the permanent work order file path	
					//destination object is modified in service layer
					File destination = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-"));
					//here constructing new folder for permanent work order
					if (!destination.exists())
						destination.mkdirs();
					
					try {
						//copying all the temporary work order attachments to permanent work order attachments bcoz this is this final approval of work order
					    FileUtils.copyDirectory(fileSource, destination);
					} catch (IOException e) {
					    e.printStackTrace();
					}
					//counting image and pdf 
					int imageCount=0;
					int pdfCount=0;
					try {
						int count = 0;
						String siteWiseNO=workOrderBean.getSiteWiseWONO().replace("/", "-");
						imageCount=0;
						for(int i=0;i<8;i++){
							File tempimageFile = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".jpg");
							File temppdfFile = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".pdf");
							//checking pdf file is exits or not if exits then rename the file name from temp to permanent file
							if(temppdfFile.exists()){
								String permanentpdffilePath = destination.getAbsolutePath()+ File.separator + orderBean.getSiteWiseWONO().replace("/", "-")+"_Part"+pdfCount+".pdf";
								pdfCount++;
								File convFile = new File(permanentpdffilePath);
								 convFile = new File(permanentpdffilePath);
								 if(temppdfFile.renameTo(convFile)){
							            log.info("File rename success");
							     }else{
							            log.info("File rename failed");
							     }
							}
							//checking image file is exits or not if exits then rename the file name from temp to permanent file
							if(tempimageFile.exists()){
								String permanentImagefilePath = destination.getAbsolutePath()+ File.separator + orderBean.getSiteWiseWONO().replace("/", "-")+"_Part"+imageCount+".jpg";
								imageCount++;
								File convFile = new File(permanentImagefilePath);
								 convFile = new File(permanentImagefilePath);
								 if(tempimageFile.renameTo(convFile)){
							            log.info("File rename success");
							        }else{
							            log.info("File rename failed");
							        }
							}
						}
						//add image count in model attribute so we can know that how many images uploaded before this
						//request.setAttribute("imagecount", count);	
						//deleting the temporary folder permanently
						boolean falg=fileSource.delete();
						} catch (Exception e) {
							e.printStackTrace();
						} 
				}
				
				modelAndView.setViewName("WorkOrder/workorderPrintpage");
				return modelAndView;
			}else if("not changed work order".equals(response)){
				response="No changes has made, this work order cannot be revised.";
				model.addAttribute("response1", response);
			} else {
				response = "Revise Work Order  Failed.";
				model.addAttribute("response1", response);
			}
			model.addAttribute("responseMessage", response);
		} catch (Exception e) {
			response = "Revise Work Order  Failed.";
			model.addAttribute("message", response);
			e.printStackTrace();
		}
/*		//loading default terms and condition's while creating work orde number
		List<WorkOrderBean> listTermsAndCondition = workControllerService.getTermsAndConditions(siteId);
		model.addAttribute("listTermsAndCondition", listTermsAndCondition);
		model.addAttribute("TC_listSize", listTermsAndCondition.size());

		WorkOrderBean bean = new WorkOrderBean();
		//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		//loading the temporary work order number site wise
		int QSWorkOrderNo = workControllerService.getMaxQSWorkOrderNoSiteWise(siteId);
		Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,"PIECEWORK");
		bean.setSiteWiseWONO(String.valueOf(QSWorkOrderNo));
		model.addAttribute("workMajorHead", WO_QSList);
		model.addAttribute("WorkOrderBean", bean);*/

		modelAndView.setViewName("response");
		return modelAndView;
	}

	//opening all drafted work order using this sub module
	@RequestMapping(value = "/openDraftWorkOrders.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getWorkOrderForModification(HttpServletRequest request, HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		String siteId = "";
		String user_id = "";
		String response = "";
		String statusType="";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//loading all the site id's
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(siteId)){
				mav.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				mav.setViewName("response");
				return mav;
			}
			
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			statusType="DF";
			List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id, siteId,statusType);
			 
			mav.addObject("SEARCHTYPE", "");
			mav.addObject("statusType", statusType);
			mav.addObject("isCommonApproval", "false");
			model.addAttribute("ShowHideTempWO", "false");
			mav.addObject("workOrderList", workOrderBeans);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			String responseMessage = request.getParameter("responseMessage");
			mav.addObject("responseMessage", responseMessage);
			mav.setViewName("WorkOrder/ViewMyPendingApprovels");
			model.addAttribute("ShowDataTable", "false");
		return mav;
	}
	//opening all the work order which is going to modify
	@RequestMapping(value = "/openWorkOrderToModify.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView openWorkOrderToModify(HttpServletRequest request, HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		String siteId = "";
		String user_id = "";
		String response = "";
		String statusType="";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//loading all the site id's
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(siteId)){
				mav.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				mav.setViewName("response");
				return mav;
			}
			
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			statusType="M";
			List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id, siteId,statusType);
			/*WorkOrderBean workOrderBean=new WorkOrderBean();
			workOrderBean.setSiteId(siteId);
			workOrderBean = workControllerService.getWorkOrderFromAndToDetails(user_id, workOrderBean);*/
			mav.addObject("SEARCHTYPE", "");
			mav.addObject("statusType", statusType);
			mav.addObject("isCommonApproval", "false");
			mav.addObject("ShowHideTempWO", "false");
			mav.addObject("workOrderList", workOrderBeans);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
			String responseMessage = request.getParameter("responseMessage");
			mav.addObject("responseMessage", responseMessage);
			mav.setViewName("WorkOrder/ViewMyPendingApprovels");
			mav.addObject("ShowDataTable", "false");
		return mav;
	}
	
	
	/**
	 * @description this controller is used for showing all the work orders that are pending in particular level
	 * 
	 */
	@RequestMapping(value = "/viewPendingWOforApprove.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewPendingWoforApprove(HttpServletRequest request, HttpSession session, Model model) {
	
		ModelAndView mav = new ModelAndView();
		String siteId = "";
		String user_id = "";
		String response = "";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//loading all the site id's
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(siteId)){
				mav.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				mav.setViewName("response");
				return mav;
			}
			
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String statusType="A";
			List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id, siteId,statusType);
			 
			mav.addObject("SEARCHTYPE", "");
			mav.addObject("statusType", statusType);
			mav.addObject("isCommonApproval", "false");
			model.addAttribute("ShowHideTempWO", "false");
			mav.addObject("workOrderList", workOrderBeans);
			response = "";
		} catch (Exception e) {
			e.printStackTrace();
		} 
			String responseMessage = request.getParameter("responseMessage");
			mav.addObject("responseMessage", responseMessage);
			mav.setViewName("WorkOrder/ViewMyPendingApprovels");
			model.addAttribute("ShowDataTable", "false");
		return mav;
	}// ViewPendingWoforApprove

	/**
	 * @description this controller is used for showing all the pending work order's which are pending for approval 
	 * and the common work order approval will use this controller load the data site wise
	 */
	@RequestMapping(value = "/viewWOforApproval.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewWOforApproval(HttpServletRequest request, HttpSession session, Model model) {
	
		ModelAndView mav = new ModelAndView();
		String siteId = "";
		String user_id = "";
		String response = "";
		String strSiteId ="";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		
			strSiteId = request.getParameter("dropdown_SiteId") == null ? "": request.getParameter("dropdown_SiteId");
			String temoWONumber=request.getParameter("siteWiseWorkOrderNo") == null ? "": request.getParameter("siteWiseWorkOrderNo");
			//if temporary work order number is not empty then forward to direct to approve page there it will check again is this work order number is valid or not 
			if(temoWONumber.length()!=0){
				mav.setViewName("redirect:/showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="+temoWONumber+"&tempWorkOrderIssueNo=&dropdown_SiteId="+strSiteId+"");
				return mav;
			}
			// if site id is not empty then load whole site data by site wise 
			/*			if (strSiteId.length()!=0){
			request.setAttribute("selectBoxOption", strSiteId);
			siteId=strSiteId;*/
			//selecting the data for Common Work Order Approver
			String statusType="A";
			List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id, "ALL",statusType);
			mav.addObject("workOrderList", workOrderBeans);
			mav.addObject("strSiteId", strSiteId);
			mav.addObject("statusType", statusType);
			model.addAttribute("ShowHideTempWO", "false");
			model.addAttribute("ShowDataTable", "false");
/*			}else{
			//this condition executed ShowHideTempWO will use in java script validation
			model.addAttribute("ShowHideTempWO", "true");
		}
*/		} catch (Exception e) {
		e.printStackTrace();
	}
	//this condition will hide the data table if site id is empty
	if (strSiteId.length()==0){
		model.addAttribute("ShowDataTable", "true");
	}
	
	String responseMessage = request.getParameter("responseMessage");
	mav.addObject("responseMessage", responseMessage);
	mav.setViewName("WorkOrder/ViewMyPendingApprovels");//ViewCommonWOPendingApprovels
	//model.addAttribute("SEARCHTYPE", "ADMIN");
	mav.addObject("isCommonApproval", "true");
	return mav;
	}// ViewPendingWoforApprove

	/**
	 * @Description this controller is used to load the completed work order's by site wise
	 * so this controller used in Update work order also for loading completed work order's
	 */
	@RequestMapping(value = "/getMyCompletedWorkOrder.spring")
	public ModelAndView ViewMyWorkOrderCompletedDetails(Model model, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String site_Id = "";
		String user_id = "";
		boolean isValid = false;
		boolean status = false;
		String isUpdateWOPage="";
		String typeOfWork="";
		String isRequestFromMail="";
		StringBuffer responseMessage=new StringBuffer("");
		//String isHideOrShowData="";
		try {
			String workOrderNo = request.getParameter("WorkOrderNo").trim();
			String workOrderIssueNo = request.getParameter("workOrderIssueNo");
			String operType = request.getParameter("operType") == null ? "" : request.getParameter("operType");
			isUpdateWOPage=request.getParameter("isUpdateWOPage") == null ? "" : request.getParameter("isUpdateWOPage");
			isRequestFromMail=request.getParameter("requestFromMail") == null ? "" : request.getParameter("requestFromMail");
			site_Id=request.getParameter("site_id")==null?"": request.getParameter("site_id");
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			
			model.addAttribute("operType", operType);
			model.addAttribute("isUpdateWOPage", isUpdateWOPage);
			try {
				//if work order issue number is empty then load the work order issue number 
				//by combination of work order number and site id
				if (workOrderIssueNo.length() == 0) {
					workOrderIssueNo = workControllerService.getPermanentWorkOrderIssueIdNumber(workOrderNo, site_Id,user_id);
				}
				if(isRequestFromMail.equals("true")){
					isValid = workControllerService.checkCompletedWorkOrderNumberIsValidForEmployee(workOrderNo, user_id,status, site_Id, "requestFromMail");
					if (isValid == false) {
						WorkOrderBean bean = workControllerService.getCompletedWorkOrderDetailsByWorkOrderNumber(workOrderNo,workOrderIssueNo);
						
						Map<String, Object> latestWorkOrderDetails = workControllerService.loadActiveWorkOrderDetails(bean);
						responseMessage.append("Work Order of ").append(bean.getContractorName())
								.append(" for project ")
								.append(bean.getSiteName())
								.append(" with reference number ")
								.append(bean.getWorkOrderNo())
								.append(" dated ").append(bean.getWorkOrderCreadeDate())
								.append(" had been revised to ")
								.append(latestWorkOrderDetails.get("WORK_ORDER_NUMBER"))
								.append(" dated ")
								.append(latestWorkOrderDetails.get("WORK_ORDER_CREATED_DATE"));
						model.addAttribute("response1", responseMessage);
						mav.setViewName("WorkOrder/WorkOrderMailResponsePage");
						return mav;
					}
				}else{
					isValid = workControllerService.checkCompletedWorkOrderNumberIsValidForEmployee(workOrderNo, user_id,status, site_Id, operType);
				}
				// here 
			} catch (Exception e) {
				log.info(e.getMessage());
				if (isRequestFromMail.equals("true")) {
					model.addAttribute("response1", "Got an error please try opening work order with tool");
					mav.setViewName("WorkOrder/WorkOrderMailResponsePage");
					return mav;
				}
			}

			// checking work order number is valid or not for loading permanent work order
			//if not send error message to client
			if (isValid == false) {
				try {
					model.addAttribute("succMessage", "Work Order Number is Not Valid");
					String response = "";
					//method for fetching completed work order 
					List<WorkOrderBean> listOfCompletedWorkOrder = workControllerService.getMyWOCompltedDetail("", "",	site_Id, "", user_id,"");
					if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
						request.setAttribute("showGrid", "true");
						response = "success";
					} else {
						mav.addObject("succMessage", "No Data Available");
						response = "failure";
					}
					mav.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
					mav.setViewName("WorkOrder/viewMyWorkOrders");
					return mav;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//Method for Fetching Work order detail's like a work order number and contractor name and work order date
			WorkOrderBean bean = workControllerService.getCompletedWorkOrderDetailsByWorkOrderNumber(workOrderNo,workOrderIssueNo);
			typeOfWork=bean.getTypeOfWork();
			model.addAttribute("WorkOrderLevelPurpose", bean.getPurpose());
			//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			//method for loading permanent table terms and conditions
			List<Map<String, Object>> termsAndConditionList = workControllerService.loadTermsAndConditions(workOrderIssueNo, "permanent");
			//this method is used for loading all the created rows of work order
			List<WorkOrderBean> workOrderList = workControllerService.getALLCompltedWorkOrderS(workOrderIssueNo,user_id,operType, status);
			//this method is used for loading the modification details of Revise work order
			if(bean.getWorkOrderNo().contains("/R")&&!bean.getRevision().equals("0")){
				//this method is for loading modification details of permanent work order
				List<Map<String, Object>> modificationDetailsList = workControllerService.getModificationDetailsList(workOrderIssueNo,typeOfWork);
				model.addAttribute("showWOModificationDetails",true);
				model.addAttribute("modificationDetailsList",modificationDetailsList);
				
				 //workControllerService.getAllRevisedWorkOrderList(workOrderIssueNo,typeOfWork);
			}
			
			
			model.addAttribute("workOrderCreationList", workOrderList);
			model.addAttribute("listTermsAndCondition", termsAndConditionList);
			//setting the view name
			mav.setViewName("WorkOrder/GetMyWorkOrderStatusDetail");
			
			//if isUpdateWOPage value is true then render the request to UpdatePermanantWorkOrder.jsp page
			if(isUpdateWOPage.equals("true")){
				mav.setViewName("WorkOrder/UpdatePermanantWorkOrder");
			}
			//if the operation type is revising work order then generate the new work order number
			if(operType.equals("reviseWorkOrder")){
				//checking is any bill is pending or not if pending can not revise the work order
				boolean isAnyBillIsPending=workControllerService.checkIsAnyBillisInActiveMode(bean);
				
				if(isAnyBillIsPending){
					mav.addObject("response1", "Can not revise work order, Bill's are in approval need to approve or reject the bill's.");
					if(typeOfWork.equals("NMR")){
						//this url is for highlighting sub module
						model.addAttribute("urlForActivateSubModule", "reviseNMRWorkOrder.spring");
					}else{
						//this url is for highlighting sub module
						model.addAttribute("urlForActivateSubModule", "reviseWorkOrder.spring");
					}
					mav.setViewName("response");
					return mav;
				}
				
				//loading the next level approver using this method with the help of bean object and user_id
				bean = workControllerService.getWorkOrderFromAndToDetails(user_id, bean);
				if (bean.getApproverEmpId() == null) {
					mav.addObject("response1", "You Cannot Create Work Order.");
					mav.setViewName("response");
					return mav;
				}else if(bean.getApproverEmpId().equals("-")){
					log.info("final data...");
					bean.setApproverEmpId("END");
				}

				
				List<Map<String, Object>> listOfRevisedWorkOrder= workControllerService.getNewRevisedWorkOrderName(user_id, bean);
				boolean canWeReviseThisWorkOrderNo=true;
			
				if(listOfRevisedWorkOrder.size()!=0){
				//here getting the last work order number
				Map<String, Object> map1 = listOfRevisedWorkOrder.get(listOfRevisedWorkOrder.size()-1);
				
				String strWorkOrderNumber=new String(map1.get("WORK_ORDER_NUMBER")==null?"":map1.get("WORK_ORDER_NUMBER").toString());
				//if the last work order number in the list matches with the current work order number then generate the new work order number 
				if(bean.getWorkOrderNo().equals(strWorkOrderNumber)){
						canWeReviseThisWorkOrderNo=false;
						String[] strWONoPart=strWorkOrderNumber.split("/");
						
						if(strWONoPart[strWONoPart.length-1].contains("R")){
							//this string holding the work order number before the /R1 or R2 etc...
							strWorkOrderNumber=Arrays.deepToString(Arrays.copyOfRange(strWONoPart, 0, strWONoPart.length-1)).replace(",", "/").replaceAll("[\\[\\]]", "");
						 		//here splitting the fourth index string and getting the last character 
						 		//using split method
								String tempNum=strWONoPart[strWONoPart.length-1].split("R")[1]; 
								//Concatenating the strWorkOrderNumber with (tempNum+1)//previous revision number+1
								strWorkOrderNumber=strWorkOrderNumber+"/R"+(Integer.valueOf(tempNum)+1);
							 }else{
								 strWorkOrderNumber=Arrays.deepToString(Arrays.copyOfRange(strWONoPart, 0, strWONoPart.length)).replace(",", "/").replaceAll("[\\[\\]]", "");
								strWorkOrderNumber= strWorkOrderNumber+"/R1";
							}
						strWorkOrderNumber=strWorkOrderNumber.replaceAll(" ", "");
						log.info("Revise work order number "+strWorkOrderNumber);
						model.addAttribute("strWorkOrderNumber1", strWorkOrderNumber);
						}else{
							model.addAttribute("isThisCorrectRevisedWorkOrder", false);
						}
					}else{
						model.addAttribute("strWorkOrderNumber1",  bean.getWorkOrderNo()+"/R1");
					}
				
				//while revise work order checking the type of work
				if(typeOfWork.equals("NMR")){
					String[] data={typeOfWork,bean.getSiteId()};
					//loading NMR major head names
					Map<String, String> WO_QSList = workControllerService.loadNMRProducts(data);
					model.addAttribute("workMajorHead", WO_QSList);
					//if operType value is reviseWorkOrder then render the request to ReviseNMRWorkOrder.jsp page
					mav.setViewName("WorkOrder/ReviseNMRWorkOrder");
				}else{
					//load PIECE WORK major head names for revise PIECE WORK
					Map<String, String> WO_QSList = workControllerService.loadQSProducts(site_Id,typeOfWork);
					model.addAttribute("workMajorHead", WO_QSList);
					//if operType value is reviseWorkOrder then render the request to RevisePermanantWorkOrder.jsp page
					mav.setViewName("WorkOrder/RevisePieceWork_WorkOrder");	
				}
				//model.addAttribute("optionalCCmails", UIProperties.validateParams.getProperty(site_Id+"_WORKORDER_CCMAILS") == null ? "00" : UIProperties.validateParams.getProperty(site_Id+"_WORKORDER_CCMAILS").toString());
			}else{
				bean.setApproverEmpId("END");
				List<Map<String, Object>> listOfVerifiedEmpNames=workControllerService.getPermanentWorkOrderVerifiedEmpNames(bean);
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
			}
			
			model.addAttribute("WorkOrderBean", bean);
			model.addAttribute("isCompletedWO", true);
			int count = 0;
			int images=0;
			String siteWiseNO=bean.getSiteWiseWONO().replace("/", "-");
			String siteId=bean.getSiteId();
			//this method is used for loading the attachments of work order it may temporary or permanent
			loadWOImgAndPdfFiles(siteWiseNO, siteId, model, request);

			return mav;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

/**
 * @description this method is used for delete the images by using the path of image
 * 
 */
	@RequestMapping(value = "/deleteWOPermanentImage.spring", method = RequestMethod.GET)
	public @ResponseBody boolean deleteWOPermanentImage( HttpServletRequest request) {
		String filePath=request.getParameter("imagePath")==null?"":request.getParameter("imagePath").replace("$", "/").trim();
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").replace("/", "-");
		String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId").replace("/", "-");
		String isRequestFromController=request.getAttribute("isRequestFromController")==null?"":request.getAttribute("isRequestFromController").toString();
		if(isRequestFromController.equals("true")){
			filePath=request.getAttribute("imagePath")==null?"":request.getAttribute("imagePath").toString().replace("$", "/").trim();
			//for temporary work order number
			workOrderNo=request.getAttribute("tempWONumber")==null?"":request.getAttribute("tempWONumber").toString().replace("$", "/").trim();
		}
		File  file=new File(filePath);
		//getting the extension type to delete the particular files
		String fileExtenstion=getFileExtension(file);
		//deleting the file
		boolean isFileDeleted=file.delete();
		String siteWiseNO=workOrderNo.trim();
		//loading the path of the images
		String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
		
		File fileSource = null;
		File destinaiton = null;
		//if file is deleting then next file name is rename with the this deleted file
		//eg. if file part1.jpg is deleted then part2.jpg will rename into part1.jpg same like the next file name will rename
		for (int i = 0; i < 8; i++) {
			//this the current file name which is deleted (isFileDeleted variable )
			 fileSource = new File(rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+"."+fileExtenstion);
			if(!fileSource.exists()){
				destinaiton = new File(rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+(i+1)+"."+fileExtenstion);
			//if destination file exists then rename the file 
				 if(destinaiton.exists()){
					  if(destinaiton.renameTo(fileSource)){
				            log.info("File name \n"+destinaiton+" changed to \n"+fileSource);
				      }else{
				            log.info("File rename failed");
				      }
				 }
			}/*else if(!fileSource.exists()&&i==0){
				destinaiton = new File(rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+(i+1)+"."+fileExtenstion);
				 if(destinaiton.exists())
				 if(destinaiton.renameTo(fileSource)){
					   log.info("File rename success \n"+destinaiton+" \n"+fileSource);
			      }else{
			            log.info("File rename failed");
			      }
			}*/
		}
		
	  
/*
 //this is new way to delete the file		
 try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (NoSuchFileException e) {
			log.info("No such file/directory exists");
		} catch (DirectoryNotEmptyException e) {
			log.info("Directory is not empty.");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Invalid permissions.");
		}
*/
        //log.info("Deletion successful."); 
		
		return isFileDeleted;
	}

	/**
	 * @description this controller is used to show the data for approval time 
	 *              and also used while showing the status of the work order
	 */
	@RequestMapping(value = "/showWorkOrderCreationDetails.spring", method = RequestMethod.GET)
	public String showWorkOrderCreationDetails(@RequestParam(value = "siteWiseWorkOrderNo") String tempsiteWiseWorkOrderNo,
			@RequestParam(value = "tempWorkOrderIssueNo") String tempWorkOrderIssueNo, Model model, HttpServletRequest request,
			HttpSession session) {
		
		log.info("**************** control inside the Load details of temporary work order ******************");
		
 		//String tempIssueId = tempWorkOrderIssueNo;
		int site_Id = 0;
		String user_id = "";
		boolean isValid = false;
		boolean status = false;
		String statusType="";
		String typeOfWork="";
		try {
			site_Id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			tempWorkOrderIssueNo=request.getParameter("tempWorkOrderIssueNo") == null ? "": request.getParameter("tempWorkOrderIssueNo");
			statusType=request.getParameter("statusType") == null ? "": request.getParameter("statusType");
			status = Boolean.valueOf(request.getParameter("status"));
			
			//site_Id=request.getParameter("site_id");
			//if tempWorkOrderIssueNo is empty then load the tempWorkOrderIssueNo
			if (tempWorkOrderIssueNo.length() == 0) {
				String strSiteId = request.getParameter("dropdown_SiteId") == null ? "": request.getParameter("dropdown_SiteId");
			//this condition for site wise approval people
			//if the strSiteId is not empty then it means this request coming from site wise approval people
				if (!strSiteId.equals("")) {
					request.setAttribute("selectBoxOption", strSiteId);
					site_Id=Integer.valueOf(strSiteId);
					model.addAttribute("ShowDataTable", "false");
					model.addAttribute("isCommonApproval", "true");
					model.addAttribute(tempWorkOrderIssueNo, "viewWOforApproval.spring");
				}
				tempWorkOrderIssueNo = workControllerService.getWorkOrderNumber(tempsiteWiseWorkOrderNo, site_Id, user_id);
			}//tempWorkOrderIssueNo condition
			//here validating the temporary work order number is valid for this current user or not
			isValid = workControllerService.checkWorkOrderNumberIsValidForEmployee(tempWorkOrderIssueNo, user_id, status,statusType);
			
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		//checking the temp work order number is valid for this current user not
		if (!isValid && status == false) {
			try {
				model.addAttribute("errorMessage", "Work Order Number is Not Valid");
			 	List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id,String.valueOf(site_Id),statusType);
				model.addAttribute("workOrderList", workOrderBeans);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("** Invalid temporary work order number "+tempWorkOrderIssueNo+" and site id="+site_Id+"**");
			return "WorkOrder/ViewMyPendingApprovels";
		}
		WorkOrderBean bean =null;
		try {
			//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			 //loading the work order details 
			bean = workControllerService.getWorkOrderDetailsByWorkOrderId(tempWorkOrderIssueNo);
			//loading the next level approver using this method with the help of bean object and user_id 
			bean = workControllerService.getWorkOrderFromAndToDetails(user_id, bean);
		//if the next level approver id empty means there no next level approver so insert temporary tables data into permanent table
			if (bean.getApproverEmpId() == null||bean.getApproverEmpId().equals("-")) {
				log.info(" final data... last approval of Work Order");
				model.addAttribute("optionalCCmails", UIProperties.validateParams.getProperty(bean.getSiteId()+"_WORKORDER_CCMAILS") == null ? "00" : UIProperties.validateParams.getProperty(bean.getSiteId()+"_WORKORDER_CCMAILS").toString());
				bean.setApproverEmpId("END");
			}
			typeOfWork=bean.getTypeOfWork();
			if(bean.getTypeOfWork().equals("NMR")){
				String[] data={typeOfWork,bean.getSiteId()};
				//loading NMR major head names
				Map<String, String> WO_QSList = workControllerService.loadNMRProducts(data);
				model.addAttribute("workMajorHead", WO_QSList);
			}else{
				Map<String, String> WO_QSList = workControllerService.loadQSProducts(bean.getSiteId(),typeOfWork);
				model.addAttribute("workMajorHead", WO_QSList);
			}
			model.addAttribute("WorkOrderBean", bean);
			model.addAttribute("NextApprovelId", bean.getApproverEmpId());
			model.addAttribute("WorkOrderLevelPurpose", bean.getPurpose());

			List<WorkOrderBean> workOrderRowDetailsList = new ArrayList<WorkOrderBean>();
			List<WorkOrderBean> deletedWorkOrderDetailsList = new ArrayList<WorkOrderBean>();
			List<Map<String, Object>> termsAndConditionList = new ArrayList<Map<String, Object>>();

			//loading all the created rows of work order
			workOrderRowDetailsList = workControllerService.getCreatedWorkOrderDetails(bean);
			//loading all the changed and deleted details of work order
			deletedWorkOrderDetailsList = workControllerService.getDeletedProductDetailsLists(tempWorkOrderIssueNo, user_id, site_Id,bean.getTypeOfWork());
			//loading the terms and condition's
			termsAndConditionList = workControllerService.loadTermsAndConditions(tempWorkOrderIssueNo, "");
			
			if (status==true) {
				//loading work order created user names and designation and operation performed name
				List<Map<String, Object>> listOfVerifiedEmpNames=workControllerService.getWorkOrderVerifiedEmpNames(bean);
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
			}
			model.addAttribute("workOrderCreationList", workOrderRowDetailsList);
			model.addAttribute("deletedWorkOrderDetailsList", deletedWorkOrderDetailsList);
			model.addAttribute("listTermsAndCondition", termsAndConditionList);
			model.addAttribute("isCommonApproval", request.getParameter("isCommonApproval")==null?"":request.getParameter("isCommonApproval"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//loading image
		String siteWiseNO=tempsiteWiseWorkOrderNo;
		String siteId=bean.getSiteId();
		//this method is used for loading the attachments of work order it may temporary or permanent
		loadWOImgAndPdfFiles(siteWiseNO,siteId,model,request);
		//adding attribute to check, is this completed work order or not 
		model.addAttribute("isCompletedWO", false);
 		//if status value is true, then show only details of work order details in read only mode
		if (status) {
			model.addAttribute("operType", "statusPage");
			return "WorkOrder/GetMyWorkOrderStatusDetail";
		} else {
			return "WorkOrder/ApproveWorkOrder";
		}
	}
	
	/**
	 * @description used to get the extension of the file eg. (.pdf,.png)
	 * @param file
	 * @return extension 
	 */
	 public static String getFileExtension(File file) {
	        String fileName = file.getName();
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
	
	 /**
	  * @description this method is used for loading the attachments of work order
	  *  it may temporary or permanent work order so here we need to pass appropriate values in parameter of methods.
	  *  And this is the common method for loading the images
	  * @param siteWiseNO is holding the temporary and permanent work order number based on the situation if request from temporary work order number then it will hold temp work order number else permanent work order number
	  * @param siteId is used for the site wise load the images 
	  * @param model
	  * @param request
	  */
	public static void loadWOImgAndPdfFiles(String siteWiseNO, String siteId, Model model, HttpServletRequest request){
		try{
			int imageCount = 0;
			int pdfCount=0;
		
			DataInputStream dis=null;
			//loading the path of images
			String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
			
			int getLocalPort=request.getLocalPort();
			String strContextAndPort = "";
			String path = "";
			if(getLocalPort == 8080){ //Local
				strContextAndPort = UIProperties.validateParams.getProperty("WO_LOCAL_IP_PORT");
			}else if(getLocalPort == 8078){ //local machine
				strContextAndPort = UIProperties.validateParams.getProperty("WO_UAT_IP_PORT");
			}else if(getLocalPort == 8079){ //CUG
				strContextAndPort = UIProperties.validateParams.getProperty("WO_CUG_IP_PORT");
			}else if(getLocalPort == 80){ //LIVE
				strContextAndPort = UIProperties.validateParams.getProperty("WO_LIVE_IP_PORT");
			}
	
			
			
			for(int i=0;i<8;i++){
				File dir = new File(rootFilePath+siteId+"\\"+siteWiseNO);
				
				File imageFile = new File(rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".jpg");
				//String pdfFileName=rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf";
				File pdfFile = new File(rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf");
				//converting file object into the String Object
				//pdfFileName=pdfFile.toString();
				
				//if PDF is exists the PDF will load and converted into the Base64 object and it will add in model object so we can carry this images and PDF so we can show in view page
				if(pdfFile.exists()){
					pdfCount++;
					try {
						String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "$");
						path=strContextAndPort+siteId+"/"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf";
	 
						model.addAttribute("pdf" + i, path);
						//adding the path of the PDF so if user deleting the PDF by path they can delete
						model.addAttribute("PathdeletePdf" + i, pathForDeleteFile);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			
				if(imageFile.exists()){
					imageCount++;
					try {
						String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "$");
						path=strContextAndPort+siteId+"/"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".jpg";
						//adding image path
						model.addAttribute("image" + i, path);
						model.addAttribute("delete" + i, pathForDeleteFile);
					} catch (Exception e) {
							e.printStackTrace();
					}
				}
			}
			//adding the image count in request object so we can control the images update file type in view while updating temporary or permanent work order
			request.setAttribute("imagecount", imageCount);	
			request.setAttribute("pdfcount", pdfCount);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description this control will use to update the work order 
	 * @param siteWiseWorkOrderNo holding the temporary work order number
	 * @param workOrderNo
	 */
	@RequestMapping(value = "/viewMyWoToUpdate.spring", method = RequestMethod.GET)
	public String viewMyWoToUpdate(@RequestParam(value = "siteWiseWorkOrderNo") String sitetempWiseWorkOrderNo,
			@RequestParam(value = "tempWorkOrderIssueNo") String tempworkOrderIssueNo, Model model, HttpServletRequest request,
			HttpSession session) {
 
		int site_Id = 0;
		String user_id = "";
		boolean isValid = false;
		boolean status = false;
		//String state="";
		try {
			site_Id = Integer.parseInt(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String statusType=request.getParameter("statusType")==null?"A":request.getParameter("statusType");
			status = Boolean.valueOf(request.getParameter("status"));
			//if tempworkOrderIssueNo number is empty then we need the tempWorkOrderIssueNumber
			if (tempworkOrderIssueNo.length() == 0) {
				tempworkOrderIssueNo = workControllerService.getWorkOrderNumber(sitetempWiseWorkOrderNo, site_Id, user_id);
			}
			//here validating the temporary work order number is valid for this current user or not
			isValid = workControllerService.checkWorkOrderNumberIsValidForEmployee(tempworkOrderIssueNo, user_id, status,statusType);
			
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		//checking the temp work order number is valid for this current user not
		if (!isValid && status == false) {
			try {
				//if the temp work order is not valid then redirect the same page from where request came
				model.addAttribute("errorMessage", "Work Order Number is Not Valid");
				//loading all the pending work order's
				String statusType="A";
				List<WorkOrderBean> workOrderBeans = workControllerService.getPendingWorkOrder(user_id,	String.valueOf(site_Id),statusType);
				//Adding work order list to model object, so model object will carry this object to view page
				model.addAttribute("workOrderList", workOrderBeans);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "WorkOrder/ViewMyPendingApprovels";
		}
		WorkOrderBean bean = null;
		try {
			//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(String.valueOf(site_Id),"PIECEWORK");
			model.addAttribute("workMajorHead", WO_QSList);
			 //loading the work order details like a contractor name and work order name, date , work order number
			bean = workControllerService.getWorkOrderDetailsByWorkOrderId(tempworkOrderIssueNo);
			model.addAttribute("WorkOrderBean", bean);
			model.addAttribute("NextApprovelId", bean.getApproverEmpId());
			model.addAttribute("WorkOrderLevelPurpose", bean.getPurpose());

			List<WorkOrderBean> workOrderRowDetailsList = new ArrayList<WorkOrderBean>();
			List<WorkOrderBean> deletedWorkOrderList = new ArrayList<WorkOrderBean>();
			List<Map<String, Object>> termsAndConditionList = new ArrayList<Map<String, Object>>();
			//loading all the created rows of work order
			workOrderRowDetailsList = workControllerService.getCreatedWorkOrderDetails(bean);
			//loading all the changed and deleted details of work order
			deletedWorkOrderList = workControllerService.getDeletedProductDetailsLists(tempworkOrderIssueNo, user_id, site_Id,bean.getTypeOfWork());
			//loading the terms and condition's
			termsAndConditionList = workControllerService.loadTermsAndConditions(tempworkOrderIssueNo, "");
			//adding all the list object's in model object  
			model.addAttribute("workOrderCreationList", workOrderRowDetailsList);
			model.addAttribute("deletedWorkOrderDetailsList", deletedWorkOrderList);
			model.addAttribute("listTermsAndCondition", termsAndConditionList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		//loading image
		String siteWiseNO=sitetempWiseWorkOrderNo;
		String siteId=bean.getSiteId();
		//this method is used for loading the attachments of work order it may temporary or permanent
		loadWOImgAndPdfFiles(siteWiseNO, siteId, model, request);
		
		return "WorkOrder/updateTempWO";
	}
	
	/**
	 * 
	 * @description this controller is used to reject permanent work order but this controller not in use
	 */
	@RequestMapping(value = "/rejectPermanentWorkOrder.spring", method = RequestMethod.POST)
	public String rejectWorkOrder( HttpServletRequest request, HttpSession session, Model mav) {
	

		String response = "";
		try {
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

		//	response = workControllerService.rejectPermanentWorkOrder(model, session, request, site_Id, user_id);
			if (response.equals("success")) {
				response = "Work order rejected successfully.";
				mav.addAttribute("message", response);
			} else {
				response = "Failed work order rejection.";
				mav.addAttribute("message1", response);
			}
			mav.addAttribute("responseMessage", response);
		} catch (Exception e) {
			response = "failed work order rejection.";
			mav.addAttribute("message1", response);
			e.printStackTrace();
		}
		return "response";
	}
	
/**
 * @description this controller is used to reject the temporary work order 
 * in approval level, if the work order is rejected then the work order number
 * will store in   work_order_reserved table,
 * so we can use that work order number in next creation of work order number
 */
	@RequestMapping(value = "/rejectWorkOrderCreation.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public String rejectWorkOrderCreation(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,Model model, HttpServletRequest request, HttpSession session, Model mav) {
		String operationTypeForWorkOrder="";
		String response = "";
		@SuppressWarnings("unchecked")
		Map<String, String> approvingWorkOrderNumbers = null;
		//String urlForActivateSubModule="";
		try {
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String tempIssueId = request.getParameter("actualtempIssueId")==null?"": request.getParameter("actualtempIssueId");
			String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
			String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			//operationTypeForWorkOrder=request.getParameter("operationTypeForWorkOrder")==null?"":request.getParameter("operationTypeForWorkOrder");
			operationTypeForWorkOrder=workOrderBean.getIsSaveOrUpdateOperation();
			//if this values are null means this is the error so we need to forward this request to again to request page with error message
			//this url is for highlighting sub module
			//model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
			if(workOrderNo.length()==0&&tempIssueId.length()==0&&contractorId.length()==0){
				model.addAttribute("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
				return "response";
			}	
			
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			qsBillBean.setSiteId(workOrderBean.getSiteId());
			workOrderBean.setUserId(user_id);
			//loadSiteAddress is used to load the site address for work order print page
			List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
			request.setAttribute("SiteAddress", loadSiteAddress);
			//calling service method for work order rejection
			approvingWorkOrderNumbers = (Map<String, String>) session.getServletContext().getAttribute("ApprovingWorkOrderNumber");
			if (approvingWorkOrderNumbers == null) {
				approvingWorkOrderNumbers = new HashMap<String, String>();
			}

			if (!approvingWorkOrderNumbers.containsKey(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId())) {
				approvingWorkOrderNumbers.put(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId(),workOrderBean.getWorkOrderNo());
			} else {
				model.addAttribute("response1", "Work order number is not valid for reject.");
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + workOrderBean.getSiteId() + "**");
				approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
				return "response";
			}
			session.getServletContext().setAttribute("ApprovingWorkOrderNumber", approvingWorkOrderNumbers);

			response = workControllerService.rejectWorkOrderCreation(workOrderBean,model, session, request, site_Id, user_id);
			if (response.equals("success")) {
				if (operationTypeForWorkOrder.equals("Discard")) {
					response = "Work order removed from draft successfully.";
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
				} else if (operationTypeForWorkOrder.equals("Modify")) {
					response = "Work order sent to modify successfully.";
					model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
				} else if (operationTypeForWorkOrder.equals("Modify WorkOrder")) {
					response = "Modify work order removed successfully.";
					model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
				} else {
					response = "Work order rejected successfully.";
				}
				mav.addAttribute("message", response);
			}else if(response.equals("Work order number is not valid.")){
				response = "Work order number is not valid.";
				mav.addAttribute("message1", response);
			} else {
				if (operationTypeForWorkOrder.equals("Discard")) {
					response = "Failed discarding work order .";
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
				} else if (operationTypeForWorkOrder.equals("Modify")) {
					response = "Failed to send work order to modification.";
					model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
				} else {
					response = "Failed work order rejection.";
				}
				mav.addAttribute("message1", response);
			}
			model.addAttribute("responseMessage", response);
		} catch (Exception e) {
			
			// adding response message to model object for showing to the client
			e.printStackTrace();
			if (operationTypeForWorkOrder.equals("Discard")) {
				response = "Failed discarding work order .";
				model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
			} else if (operationTypeForWorkOrder.equals("Modify")) {
				response = "Failed to send work order to modification.";
				model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
			} else {
				response = "Failed work order rejection.";
			}
			mav.addAttribute("message1", response);
		}
		approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
		return "response";
	}

/**
 * 
 * @param workOrderBean object holding necessary data for updating temp work order number
 * @param files array object is holding all the files data that are uploaded by client
 * @return
 */
	@RequestMapping(value = "/updateTempWorkOrder.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public String updateTempWorkOrder(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean, Model model,
			HttpServletRequest request, HttpSession session,@RequestParam(value="file",required = false) MultipartFile[] files) {
		String response = "";
		String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		model.addAttribute("urlForActivateSubModule","updateTempWO.spring");
		
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		String siteWiseWONO=request.getParameter("siteWiseWONO")==null?"":request.getParameter("siteWiseWONO");
		
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&siteWiseWONO.length()==0&&contractorId.length()==0){
			model.addAttribute("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			return "response";
		}
		try {
			String PdfPathtodelete[]=request.getParameterValues("PdfPathtodelete");
			String imgPathtoDelete[]=request.getParameterValues("imgPathtoDelete");
			if(imgPathtoDelete!=null)
			for (String imgPath : imgPathtoDelete) {
				request.setAttribute("imagePath", imgPath.split("&")[0].split("=")[1]);
				request.setAttribute("tempWONumber", siteWiseWONO);
				request.setAttribute("isRequestFromController", true);
				deleteWOPermanentImage(request);
				
			}
			if(PdfPathtodelete!=null)
			for (String pdfPath : PdfPathtodelete) {
				request.setAttribute("imagePath", pdfPath.split("&")[0].split("=")[1]);
				request.setAttribute("tempWONumber", siteWiseWONO);
				request.setAttribute("isRequestFromController", true);
				deleteWOPermanentImage(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "Faild to delte images, please try again later.";
			//returning the response to the client
			model.addAttribute("message1", response);
			return "response";
		}
		

		
		
		WorkOrderBean orderBean = null;
		try {
			//cloning the actual object so actual object will not get any changes so we can get necessary data from the actual object
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}

		try {
			response = "success";
			if (response.equals("success")) {
				response = "Temp Work Order Updated Successfully.Temp Work Order No is "+ orderBean.getSiteWiseWONO()+", Work Order No is "+orderBean.getWorkOrderNo();
				model.addAttribute("message", response);
				//model.addAttribute("WorkOrderBean");
				//adding work order bean object in model object so the model will carry this object to view page
				model.addAttribute("WorkOrderBean", orderBean);
				String approverEmpId=request.getParameter("approverEmpId")==null?"":request.getParameter("approverEmpId");
				//loading the path of the images
				String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
				//replacing the slash to hyphen symbol,because folder will not accept slash so converting to hyphen symbol
				String workOrderNum=workOrderBean.getSiteWiseWONO().replace("/", "-");
				String siteId=workOrderBean.getSiteId();
				
				//getting how many images of PDF already uploaded before this update temp work order
				int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
				int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent"));
				int imgCount=imagesAlreadyPresent;
				int pdfCount=pdfAlreadyPresent;
				
				for (int i = imagesAlreadyPresent; i < files.length; i++) {
					
					MultipartFile multipartFile = files[i];
					if(!multipartFile.isEmpty()){
					try {
						//file directory name is constructing here 
							File dir = new File(rootFilePath+siteId+"\\"+workOrderNum);
						//checking is the directory exits or not if not create the directory for string images
							if (!dir.exists())
								dir.mkdirs();
							
							String filePath ="";
							log.info(multipartFile.getOriginalFilename());
							//checking the extension of file and creating the file based on extension 
							if(multipartFile.getOriginalFilename().endsWith(".pdf")){
								filePath = dir.getAbsolutePath()+ File.separator + workOrderNum+"_Part"+pdfCount+".pdf";
								pdfCount++;
							}else{
								filePath = dir.getAbsolutePath()+ File.separator + workOrderNum+"_Part"+imgCount+".jpg";
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
							log.info("Image NOT Uploaded");
							//return "You failed to upload " ;
							model.addAttribute("message1", "Work order approved successfully. But image not uploaded");
							request.setAttribute("message1","Work order approved successfully. But image not uploaded");
						}
					}
				}//For Loop
			
				return "response";
			} else {
				response = "Work order failed to update.";
				//returning the response to the client
				model.addAttribute("message1", response);
			}
		} catch (Exception e) {
			response = "Work order failed to update.";
			//returning the response to the client
			model.addAttribute("message1", response);
		}
		
		return "response";
	}

	
	@RequestMapping(value = "/approveWorkOrderFromMail.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String approveWorkOrderFromMail(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean, Model model,
			HttpServletRequest request, HttpSession session, HttpServletResponse resp) {
		log.info("WorkOrderController.approveWorkOrderFromMail()");
		String response = "";

		try {
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			qsBillBean.setSiteId(workOrderBean.getSiteId());
			List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
			request.setAttribute("SiteAddress", loadSiteAddress);
			
			response = workControllerService.approveWorkOrderFromMail(workOrderBean, request, session);
			
			if(workOrderBean.getApproverEmpId().equals("END")){
				copyTemporaryWorkOrderImageToPermanentWorkOrder(workOrderBean,workOrderBean.getWorkOrderNo(),workOrderBean.getOldWorkOrderNo(),workOrderBean.getWorkOrderNo());
			}
			if (response.equals("success")) {
				model.addAttribute("response", "Work order approved successfully.");
			} else {
				model.addAttribute("response1", "Work order failed to approve, please try with tool.");
			}

		} catch (Exception e) {
			model.addAttribute("response1", "Work order failed to approve, please try with tool.");
			e.printStackTrace();
			if (e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")) {
				model.addAttribute("response1", "Work order number is not valid, It might be approved or rejected please check work order status.");
			}
		}

		return "WorkOrder/WorkOrderMailResponsePage";
	}
	
	@RequestMapping(value = "/rejectOrModifyWorkOrderFromMail.spring",method = { RequestMethod.GET, RequestMethod.POST })
	public String rejectOrModifyWorkOrderFromMail(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean, Model model,
			HttpServletRequest request, HttpSession session) {
		String response = "";
		String strData ="";
		String siteWiseWorkOrderNo = "";
		String tempWorkOrderIssueId = "";
		String approverEmpId = "";
		log.info("WorkOrderController.rejetWorkOrderFromMail()");
		try {
			String remarks = workOrderBean.getRemarks();
			if (remarks==null||remarks.length() == 0) {
				//decrypting data which is encrypted in URL
				strData = workOrderBean.getSiteWiseWONO().replace(" ", "+");
				siteWiseWorkOrderNo = AESDecrypt.decrypt("AMARAVADHIS12345", strData);
				strData = workOrderBean.getQS_Temp_Issue_Id().replace(" ", "+");
				tempWorkOrderIssueId = AESDecrypt.decrypt("AMARAVADHIS12345", strData);
				strData = workOrderBean.getApproverEmpId().replace(" ", "+");
				approverEmpId = AESDecrypt.decrypt("AMARAVADHIS12345", strData);

				workOrderBean.setUserId(approverEmpId);// approver employee id is nothing but current user id
				workOrderBean.setQS_Temp_Issue_Id(tempWorkOrderIssueId);
				workOrderBean.setSiteWiseWONO(siteWiseWorkOrderNo);
				workOrderBean.setApproverEmpId(approverEmpId);
			}

			//validating work order is valid or not for this user, if not valid throw message to user
			boolean isValid = workControllerService.checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),
					workOrderBean.getUserId(), false, workOrderBean.getWorkOrderStatus());
			if (!isValid) {
				model.addAttribute("response1", "Work order number is not valid.");
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + workOrderBean.getSiteId() + "**");
				return "WorkOrder/WorkOrderMailResponsePage";
			}
			//if remarks is empty then redirect to jsp page and take the remarks 
			//in email we cannot pass remarks so we created one page that will take remarks
			if (remarks==null||remarks.length() == 0) {
				model.addAttribute("action", "rejectOrModifyWorkOrderFromMail.spring");
				model.addAttribute("WorkOrderBean", workOrderBean);
				model.addAttribute("buttonType", workOrderBean.getIsSaveOrUpdateOperation());
				return "WorkOrder/WorkOrderMailResponsePage";
			}
	
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			workOrderBean.setPurpose(remarks);
			qsBillBean.setSiteId(workOrderBean.getSiteId());

			List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
			request.setAttribute("SiteAddress", loadSiteAddress);
			//reject work order from mail
			response = workControllerService.rejectOrModifyWorkOrderFromMail(workOrderBean, request, session);
			
			if (response.equals("success")) {
				if(workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")){
					model.addAttribute("response","Work order send to modify successfully.");
				}else{
					model.addAttribute("response", "Work order rejected successfully.");
				}
			} else {
				if(workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")){
					model.addAttribute("response1", "Failed to send work order to modification, Please try with tool.");
				}else{
					model.addAttribute("response1", "Failed work order rejection, Please try with tool.");
				}
			}
		} catch (Exception e) {
			if(workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")){
				model.addAttribute("response1", "Failed to send work order to modification, Please try with tool.");
			}else{
				model.addAttribute("response1", "Failed work order rejection, Please try with tool.");
			}
			e.printStackTrace();
			if (e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")) {
				//model.addAttribute("response1", "Work order number is not valid, Please check work order");
				model.addAttribute("response1", "Work order number is not valid, It might be approved or rejected please check work order status.");
			}
		}

		return "WorkOrder/WorkOrderMailResponsePage";
	}

	
	/**
	 * @description this controller is used approve operation of work order or nmr work order 
	 * @param workOrderBean object is a model attribute  holding the necessary data for the approve operation
	 */
	@RequestMapping(value = "/approveWorkOrderCreation.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String approveWorkOrderCreation(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean, Model model,
			HttpServletRequest request, HttpSession session) {
		String response = "";
		//this URL is for highlighting sub module
		model.addAttribute("urlForActivateSubModule", "viewPendingWOforApprove.spring");
		String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

		String tempIssueId = request.getParameter("actualtempIssueId") == null ? "" : request.getParameter("actualtempIssueId");
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
		String contractorId = request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId");
		String oldWorkOrderNo = request.getParameter("oldWorkOrderNo") == null ? "" : request.getParameter("oldWorkOrderNo");	
		
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&tempIssueId.length()==0&&contractorId.length()==0){
			model.addAttribute("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			model.addAttribute("urlForActivateSubModule", session.getAttribute("subModuleUrlForApproveWO"));
			return "response";
		}
		
		WorkOrderBean orderBean = null;
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(workOrderBean.getSiteId());
		// loadSiteAddress is used to load the site address for work order print page
		List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		request.setAttribute("SiteAddress", loadSiteAddress);

		@SuppressWarnings("unchecked")
		Map<String, String> holdingReviseWorkOrderNumbers = (Map<String, String>) session.getServletContext().getAttribute("ApprovingWorkOrderNumber");
		if (holdingReviseWorkOrderNumbers == null) {
			holdingReviseWorkOrderNumbers = new HashMap<String, String>();
		}

		session.getServletContext().setAttribute("ApprovingWorkOrderNumber", holdingReviseWorkOrderNumbers);
	
		String workOrderNumber = workOrderBean.getWorkOrderNo();
		try {
			// cloning the object so main object should not get any affect so we can use it
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
		try {
			String statusOfWO = "";
			if (workOrderBean.getWorkOrderStatus() != null && workOrderBean.getWorkOrderStatus().equals("DraftModify")) {
				statusOfWO = "DF";
			} else {
				statusOfWO = "A";
			}
			boolean isValid = workControllerService.checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),user_id, false, statusOfWO);
			if (!isValid) {
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
				model.addAttribute("response1", "Work order number is not valid.");
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + site_Id + "**");
				return "response";
			}
			if (!holdingReviseWorkOrderNumbers.containsKey(workOrderNo + contractorId + site_Id+user_id)) {
				holdingReviseWorkOrderNumbers.put(workOrderNo + contractorId + site_Id+user_id, workOrderNo);
			} else {
				model.addAttribute("response1", "Work order number is not valid for approve.");
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + site_Id + "**");
				//holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
				return "response";
			}
			//calling service method for updating the details of work order and proceeding to next level
			response = workControllerService.approveWorkOrderCreation(orderBean, request, session, model, site_Id,user_id,null);
			//approveWorkOrderCreation method is returning the success msg means the work order is approved successfully...
		 	if (response.equals("success")) {
			
				response = "Work order approved successfully.";
				//this code is for providing related message in front end and activating sub module 
				if(workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")){
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
					session.setAttribute("subModuleUrlForApproveWO","openDraftWorkOrders.spring");
					response = "Work order drafted successfully.";
				}else if(workOrderBean.getIsSaveOrUpdateOperation().equals("Submit")){
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
					session.setAttribute("subModuleUrlForApproveWO", "openDraftWorkOrders.spring");
					response = "Work order created successfully.";
				}
				if(workOrderBean.getIsCommonApproval()!=null)
					if(workOrderBean.getIsCommonApproval().equals("true")){
						session.setAttribute("subModuleUrlForApproveWO", "viewWOforApproval.spring");
					}
				
				model.addAttribute("message", response);
				// adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("WorkOrderBean", orderBean);
				String approverEmpId=request.getParameter("approverEmpId")==null?"":request.getParameter("approverEmpId");
			
				try {
					// loading work order created user names and designation and operation performed name
					List<Map<String, Object>> listOfVerifiedEmpNames = workControllerService.getWorkOrderVerifiedEmpNames(workOrderBean);
					// adding all the list object's in model object
					model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
				} catch (Exception e) {

				}
				
				String siteId=workOrderBean.getSiteId();
				// if approver employee id if equals to "End" means we have to copy temporary work order attachments to permanent work order
				if(approverEmpId.equals("END")){
					copyTemporaryWorkOrderImageToPermanentWorkOrder(workOrderBean,workOrderNumber,oldWorkOrderNo,workOrderNo);
					/*
					
					//
					
					//loading the path of the images
					String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();

					//fileSource variable holding the temporary work order file path
					File fileSource = new File(rootFilePath+siteId+"\\"+workOrderBean.getSiteWiseWONO().replace("/", "-"));
					
					//destination variable holding the permanent work order file path	
					//destination object is modified in service layer
					File destination = new File(rootFilePath + siteId + "\\" + workOrderNumber.replace("/", "-"));
					// here constructing new folder for permanent work order
					if (!destination.exists())
						destination.mkdirs();
					
					try {
						// copying all the temporary work order attachments to permanent work order attachments bcoz this is this final approval of work order
						FileUtils.copyDirectory(fileSource, destination);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//counting image and PDF 
					int imageCount=0;
					int pdfCount=0;
					try {
						int count = 0;
						String siteWiseNO=workOrderBean.getSiteWiseWONO();
						imageCount=0;
						for(int i=0;i<8;i++){
							File tempimageFile = new File(rootFilePath+siteId+"\\"+workOrderNumber.replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".jpg");
							File temppdfFile = new File(rootFilePath+siteId+"\\"+workOrderNumber.replace("/", "-")+"/"+siteWiseNO+"_Part"+i+".pdf");
							//checking PDF file is exits or not if exits then rename the file name from temp to permanent file
							if (temppdfFile.exists()) {
								String permanentpdffilePath = destination.getAbsolutePath() + File.separator + workOrderNumber.replace("/", "-") + "_Part" + pdfCount + ".pdf";
								pdfCount++;
								File convFile = new File(permanentpdffilePath);
								convFile = new File(permanentpdffilePath);
								if (temppdfFile.renameTo(convFile)) {
									log.info("File rename success");
									
								} else {
									log.info("File rename failed");
								}
							}
							//checking image file is exits or not if exits then rename the file name from temp to permanent file
							if (tempimageFile.exists()) {
								String permanentImagefilePath = destination.getAbsolutePath() + File.separator+ workOrderNumber.replace("/", "-") + "_Part" + imageCount + ".jpg";
								imageCount++;
								File convFile = new File(permanentImagefilePath);
								convFile = new File(permanentImagefilePath);
								if (tempimageFile.renameTo(convFile)) {
									log.info("File rename success");
								} else {
									log.info("File rename failed");
								}
							}
						}
					
						//deleting the temporary folder permanently
						boolean falg=fileSource.delete();
						String rootFilePathOfBill = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
										: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();
						String dirPath = rootFilePathOfBill + workOrderBean.getSiteId() + "\\"+ oldWorkOrderNo.replace("/", "-");

						File dir = new File(dirPath);
						File newDir = new File(rootFilePathOfBill + workOrderBean.getSiteId() + "\\" + workOrderNo.replace("/", "-"));
						if (!dir.isDirectory()) {
							System.err.println("There is no directory @ given path");
						} else {
							dir.renameTo(newDir);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					*/}
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
				//after update successfully returning to the print page of work order
				return "WorkOrder/workorderPrintpage";
			}else if(response.equals("error in material area")){
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
				response = "Work order failed to  approve. Error occured in material area please check work order again.";
				//adding the response in model object for view purpose
				model.addAttribute("message1", response);
			} else {
				holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
				response = "Work Order Failed to  Approve.";
				if (workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")) {
					response = "Work order faild to draft.";
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
					session.setAttribute("subModuleUrlForApproveWO", "openDraftWorkOrders.spring");
				} else if (workOrderBean.getIsSaveOrUpdateOperation().equals("Submit")) {
					response = "Work order faild to create.";
					model.addAttribute("urlForActivateSubModule", "openDraftWorkOrders.spring");
					session.setAttribute("subModuleUrlForApproveWO", "openDraftWorkOrders.spring");
				}
				//adding the response in model object for view purpose
				model.addAttribute("message1", response);
			}
		} catch (Exception e) {
			holdingReviseWorkOrderNumbers.remove(workOrderNo + contractorId + site_Id+user_id);
			response = "Work Order failed to  Approve.";
			// adding the response in model object for view purpose
			if (e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")) {
				response = "Work order number is not valid, Please check work order";
			}
			model.addAttribute("message1", response);
		}

		return "response";
	}
	
	private void copyTemporaryWorkOrderImageToPermanentWorkOrder(WorkOrderBean workOrderBean, String workOrderNumber,
			String oldWorkOrderNo, String workOrderNo12343) {
		String siteId = workOrderBean.getSiteId();
		// loading the path of the images
		String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? ""
				: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();

		// fileSource variable holding the temporary work order file path
		File fileSource = new File(rootFilePath + siteId + "\\" + workOrderBean.getSiteWiseWONO().replace("/", "-"));

		// destination variable holding the permanent work order file path
		// destination object is modified in service layer
		File destination = new File(rootFilePath + siteId + "\\" + workOrderNumber.replace("/", "-"));
		// here constructing new folder for permanent work order
		if (!destination.exists())
			destination.mkdirs();

		try {
			// copying all the temporary work order attachments to permanent
			// work order attachments bcoz this is this final approval of work
			// order
			FileUtils.copyDirectory(fileSource, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// counting image and PDF
		int imageCount = 0;
		int pdfCount = 0;
		try {
			int count = 0;
			String siteWiseNO = workOrderBean.getSiteWiseWONO();
			imageCount = 0;
			for (int i = 0; i < 8; i++) {
				File tempimageFile = new File(rootFilePath + siteId + "\\" + workOrderNumber.replace("/", "-") + "/"
						+ siteWiseNO + "_Part" + i + ".jpg");
				File temppdfFile = new File(rootFilePath + siteId + "\\" + workOrderNumber.replace("/", "-") + "/"
						+ siteWiseNO + "_Part" + i + ".pdf");
				// checking PDF file is exits or not if exits then rename the
				// file name from temp to permanent file
				if (temppdfFile.exists()) {
					String permanentpdffilePath = destination.getAbsolutePath() + File.separator
							+ workOrderNumber.replace("/", "-") + "_Part" + pdfCount + ".pdf";
					pdfCount++;
					File convFile = new File(permanentpdffilePath);
					convFile = new File(permanentpdffilePath);
					if (temppdfFile.renameTo(convFile)) {
						log.info("File rename success");
						;
					} else {
						log.info("File rename failed");
					}
				}
				// checking image file is exits or not if exits then rename the
				// file name from temp to permanent file
				if (tempimageFile.exists()) {
					String permanentImagefilePath = destination.getAbsolutePath() + File.separator
							+ workOrderNumber.replace("/", "-") + "_Part" + imageCount + ".jpg";
					imageCount++;
					File convFile = new File(permanentImagefilePath);
					convFile = new File(permanentImagefilePath);
					if (tempimageFile.renameTo(convFile)) {
						log.info("File rename success");
					} else {
						log.info("File rename failed");
					}
				}
			}

			// deleting the temporary folder permanently 
			boolean falg = fileSource.delete();
			//this code is for changing work order bill image folder name 
			String rootFilePathOfBill = UIProperties.validateParams .getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
									  : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();
			String dirPath = rootFilePathOfBill + workOrderBean.getSiteId() + "\\" + oldWorkOrderNo.replace("/", "-");

			File dir = new File(dirPath);
			File newDir = new File(rootFilePathOfBill + workOrderBean.getSiteId() + "\\" + workOrderNumber.replace("/", "-"));
			if (!dir.isDirectory()) {
				System.err.println("There is no directory @ given path");
			} else {
				dir.renameTo(newDir);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * @description this controller is for showing the status of work order, where this work order
	 *  is pending or which level this work order is pending
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/ViewMyWOStatus.spring", method = RequestMethod.GET)
	public ModelAndView viewMyCreatedWorkOrderStatus(HttpServletRequest request, HttpSession session) {
		String toDate = "";
		String fromDate = "";
		String tempworkOrderNumber = "";
		String response="";
		String typeOfSelection="";
		ModelAndView model = null;
		List<WorkOrderBean> listOfWoPendingStatus = null;
		
		try {
			model = new ModelAndView();
			String site_Id = session.getAttribute("SiteId") == null ? "": session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			
			
			//here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
		
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempworkOrderNumber = request.getParameter("siteWiseWorkOrderNo");
			//calling service layer to load the pending temporary work order details
			listOfWoPendingStatus = workControllerService.getMyWOPendingStatusDetail(fromDate, toDate, site_Id,tempworkOrderNumber);
			//if listOfWoPendingStatus is not empty then show the table in view page
			//else show the error message
			if (listOfWoPendingStatus != null && listOfWoPendingStatus.size() > 0) {
				request.setAttribute("showGrid", "true");
				response = "success";
			} else {
				model.addObject("succMessage", "No Data Available");
				response = "failure";
			}	
			//adding listOfWoPendingStatus object into the model object for the view purpose
			model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
			/*
			model.addObject("fromDate", fromDate);
			model.addObject("toDate", toDate);
			*/
			model.addObject("workOrderNumber", tempworkOrderNumber);
			model.setViewName("WorkOrder/ViewMyWorkOrderPendingStatus");
			
			if (StringUtils.isNotBlank(tempworkOrderNumber)) {
				//if temp work order number is not empty check this temp work order is exits in list or not if exits then forward to the status page
				for (WorkOrderBean workOrderBean : listOfWoPendingStatus) {
					if (workOrderBean.getSiteWiseWONO().equals(tempworkOrderNumber)) {
						String tempIssueId=workOrderBean.getQS_Temp_Issue_Id();
						String url="redirect:/showWorkOrderCreationDetails.spring?"+ "siteWiseWorkOrderNo="+tempworkOrderNumber+"&tempWorkOrderIssueNo="+tempIssueId+"&siteId="+site_Id+"&status=true";
						model.setViewName(url);
						return model;
					}
				}
			}
			
/*			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber)) {
			
				String user_id = session.getAttribute("UserId") == null ? "": session.getAttribute("UserId").toString();
				if (StringUtils.isNotBlank(site_Id)) {
					
					listOfWoPendingStatus = workControllerService.getMyWOPendingStatusDetail(fromDate, toDate, site_Id,workOrderNumber);

					if (listOfWoPendingStatus != null && listOfWoPendingStatus.size() > 0) {
						request.setAttribute("showGrid", "true");
						response = "success";

					} else {
						model.addObject("succMessage", "No Data Available");
						response = "failure";
					}
					if (StringUtils.isNotBlank(workOrderNumber)) {
						log.info("redirected from ");

						for (WorkOrderBean workOrderBean : listOfWoPendingStatus) {
							if (workOrderBean.getSiteWiseWONO().equals(workOrderNumber)) {
								model.setViewName("redirect:/showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="
										+ workOrderNumber + "&tempWorkOrderIssueNo=&siteId=" + site_Id + "&status=true");
								return model;
							}
						}
					}

					model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
					model.addObject("fromDate", fromDate);
					model.addObject("toDate", toDate);
					model.addObject("workOrderNumber", workOrderNumber);
					model.setViewName("WorkOrder/ViewMyWorkOrderPendingStatus");

				} else {
					model.addObject("Message", "Session Expired, Please Login Again");
					model.setViewName("index");
					response = "failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/ViewMyWorkOrderPendingStatus");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			model.addObject("succMessage", "No data exists.");
			response = "failure";
			model.setViewName("WorkOrder/ViewMyWorkOrderPendingStatus");
		}
		
		model.addObject("pageName", "View My Work Order Status");
		model.addObject("pageTitle","View My Work Order Status");
		model.addObject("pageAction", "ViewMyWOStatus.spring");
		return model;
	}
/**
 * @description this controller used to get the work order data site wise, this controller only can access who are are the common approval of the work order
 * @return
 */
	@RequestMapping(value = "/ViewSitewiseWOStatus.spring", method = RequestMethod.GET)
	public ModelAndView viewSitewiseCreatedWorkOrderStatus(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderController.viewMyCreatedWorkOrderStatus()");
		String toDate = "";
		String fromDate = "";
		String tempworkOrderNumber = "";
		String response = "";
		ModelAndView model = null;
		List<WorkOrderBean> listOfWoPendingStatus = null;
		
		try {
			model = new ModelAndView();
			String site_Id = session.getAttribute("SiteId") == null ? "": session.getAttribute("SiteId").toString();
			
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
		
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempworkOrderNumber = request.getParameter("siteWiseWorkOrderNo");

			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(tempworkOrderNumber)) {
			
				String user_id = session.getAttribute("UserId") == null ? "": session.getAttribute("UserId").toString();
				//checking is session site_id is empty that means session is expired so redirect user to login page
				if (StringUtils.isNotBlank(site_Id)) {
					listOfWoPendingStatus = workControllerService.getSitewiseWOPendingStatusDetail(fromDate, toDate, site_Id,tempworkOrderNumber);

					if (listOfWoPendingStatus != null && listOfWoPendingStatus.size() > 0) {
						//if list size is not empty show the table in view page
						request.setAttribute("showGrid", "true");
						response = "success";
					} else {
						//if list size is zero then don't show the table only show the below msg
						model.addObject("succMessage", "No Data Available");
						response = "failure";
					}
					if (StringUtils.isNotBlank(tempworkOrderNumber)) {
						//if temp work order number is not empty check this temp work order is exits in list or not if exits then forward to the status page
						for (WorkOrderBean workOrderBean : listOfWoPendingStatus) {
							if (workOrderBean.getSiteWiseWONO().equals(tempworkOrderNumber)) {
								model.setViewName("redirect:/showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="+ tempworkOrderNumber+ "&tempWorkOrderIssueNo=&siteId=" + site_Id + "&status=true");
								return model;
							}
						}
					}
					//adding the listOfWoPendingStatus object to model object to carry from controller to jsp page
					model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
					//adding user entered fromDate value to model object we can show the value again to user 
					model.addObject("fromDate", fromDate);
					//adding user entered toDate value to model object we can show the value again to user
					model.addObject("toDate", toDate);
					//adding user entered tempworkOrderNumber value to model object we can show the value again to user
					model.addObject("workOrderNumber", tempworkOrderNumber);
					model.setViewName("WorkOrder/ViewSitewiseWorkOrderPendingStatus");
				} else {
					model.addObject("Message", "Session Expired, Please Login Again");
					model.setViewName("index");
					response = "failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", tempworkOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/ViewSitewiseWorkOrderPendingStatus");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addObject("succMessage", "Please enter work order valid temp number.");
			response = "failure";
			model.setViewName("WorkOrder/ViewSitewiseWorkOrderPendingStatus");
		}
		//adding dynamically page name to the view page
		model.addObject("pageName", "View My Work Order Status");
		//adding dynamically page title to the view page
		model.addObject("pageTitle","View My Work Order Status");
		//adding dynamically pageAction to the view page
		model.addObject("pageAction", "ViewSitewiseWOStatus.spring");
		return model;
	}

	/**
	 * @description showing all the temp work order number for updating purpose
	 */
	@RequestMapping(value = "/updateTempWO.spring", method = RequestMethod.GET)
	public ModelAndView updateTempWO(HttpServletRequest request, HttpSession session) {
	
		String toDate = "";
		String fromDate = "";
		String tempworkOrderNumber = "";
		String response = "";
		ModelAndView model = new ModelAndView();
		List<WorkOrderBean> listOfWoPendingStatus = null;
	
		try {
			String site_Id = session.getAttribute("SiteId") == null ? ""		: session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName( "response");
				return model;
			}
		
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			tempworkOrderNumber = request.getParameter("siteWiseWorkOrderNo");
			String user_id = session.getAttribute("UserId") == null ? ""	: session.getAttribute("UserId").toString();
			listOfWoPendingStatus = workControllerService.getMyTempWOForUpdateDetail(fromDate, toDate, site_Id,tempworkOrderNumber,user_id);

			if (listOfWoPendingStatus != null && listOfWoPendingStatus.size() > 0) {
				//if list size is not empty show the table in view page
				request.setAttribute("showGrid", "true");
				response = "success";
			} else {
				//if list size is zero then don't show the table only show the below msg
				model.addObject("succMessage", "No Data Available");
				response = "failure";
			}
			if (StringUtils.isNotBlank(tempworkOrderNumber)) {
				//if temp work order number is not empty check this temp work order is exits in list or not if exits then forward to the status page
				for (WorkOrderBean workOrderBean : listOfWoPendingStatus) {
					if (workOrderBean.getSiteWiseWONO().equals(tempworkOrderNumber)) {
						String tempIssueId=workOrderBean.getQS_Temp_Issue_Id();
						model.setViewName("redirect:/viewMyWoToUpdate.spring?siteWiseWorkOrderNo="+ tempworkOrderNumber + "&tempWorkOrderIssueNo="+tempIssueId+"&siteId=" + site_Id + "&status=true");
						return model;
					}
				}
			}
			//adding the listOfWoPendingStatus object to model object to carry from controller to jsp page
			model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
			//adding user entered fromDate value to model object we can show the value again to user
			model.addObject("fromDate", fromDate);
			//adding user entered toDate value to model object we can show the value again to user
			model.addObject("toDate", toDate);
			//adding user entered tempworkOrderNumber value to model object we can show the value again to user
			model.addObject("workOrderNumber", tempworkOrderNumber);
			model.setViewName("WorkOrder/ViewWoToUpdate");
			
			
/*			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber)) {
				
				String user_id = session.getAttribute("UserId") == null ? ""	: session.getAttribute("UserId").toString();
				if (StringUtils.isNotBlank(site_Id)) {
					listOfWoPendingStatus = workControllerService.getMyTempWOForUpdateDetail(fromDate, toDate, site_Id,workOrderNumber,user_id);

					if (listOfWoPendingStatus != null && listOfWoPendingStatus.size() > 0) {
						request.setAttribute("showGrid", "true");
						response = "success";

					} else {
						model.addObject("succMessage", "No Data Available");
						response = "failure";
					}
					if (StringUtils.isNotBlank(workOrderNumber)) {
					

						for (WorkOrderBean workOrderBean : listOfWoPendingStatus) {
							if (workOrderBean.getSiteWiseWONO().equals(workOrderNumber)) {
								//model.setViewName("redirect:/showWorkOrderCreationDetails.spring?siteWiseWorkOrderNo="+ workOrderNumber + "&tempWorkOrderIssueNo=&siteId=" + site_Id + "&status=true");
								model.setViewName("redirect:/viewMyWoToUpdate.spring?siteWiseWorkOrderNo="+ workOrderNumber + "&tempWorkOrderIssueNo=&siteId=" + site_Id + "&status=true");
								return model;
							}
						}
					}

					model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
					model.addObject("fromDate", fromDate);
					model.addObject("toDate", toDate);
					model.addObject("workOrderNumber", workOrderNumber);
					model.setViewName("WorkOrder/ViewWoToUpdate");

				} else {
					model.addObject("Message", "Session Expired, Please Login Again");
					model.setViewName("index");
					response = "failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("listOfWoPendingStatus", listOfWoPendingStatus);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/ViewWoToUpdate");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			model.addObject("succMessage", "No Data Available");
			response = "failure";
			model.setViewName("WorkOrder/ViewWoToUpdate");
		}
		model.addObject("pageName", "Update Temp Work Order");
		model.addObject("pageTitle", "Update Temp Work Order");
		model.addObject("pageAction", "updateTempWO.spring");
		return model;
	}
/**
 *@description this controller used to show all the completed work order's by site wise 
 */
	@RequestMapping(value = "/viewMyWorkOrders.spring", method = RequestMethod.GET)
	public ModelAndView viewMyWorkOrders(HttpServletRequest request, HttpSession session) {
		String toDate = "";
		String fromDate = "";
		String workOrderNumber = "";
		String contractorId="";
		String contracterName="";
		String response = "";
		ModelAndView model = null;
		List<WorkOrderBean> listOfCompletedWorkOrder = null;
		try {
			model = new ModelAndView();
			model.addObject("urlForFormTag", "viewMyWorkOrders.spring");
			workOrderNumber = request.getParameter("siteWiseWorkOrderNo")==null?"":request.getParameter("siteWiseWorkOrderNo").trim();
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			contracterName=request.getParameter("ContracterName")==null?"":request.getParameter("ContracterName");
			//operationType if one means select the permanent data
			model.addObject("operType", "1");
			model.addObject("isUpdateWOPage",false);
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below message that can't access this module
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber) || StringUtils.isNotBlank(contractorId) ) {
				listOfCompletedWorkOrder = workControllerService.getMyWOCompltedDetail(fromDate, toDate, site_Id,workOrderNumber, user_id,contractorId);
				if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
					request.setAttribute("showGrid", "true");
					response = "success";
				} else {
					model.addObject("succMessage", "No Data Available");
					response = "failure";
				}
				//adding the listOfWoPendingStatus object to model object to carry from controller to jsp page
				model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				model.addObject("contractorId", contractorId);
				model.addObject("ContracterName", contracterName);
				
				model.setViewName("WorkOrder/viewMyWorkOrders");
			} else {
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/viewMyWorkOrders");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @description this controller used to show the the site wise PERMANENT work order, 
	 * but this sub module is for only common approval of work order
	 */
	@RequestMapping(value = "/viewSitewiseWorkOrders.spring", method = RequestMethod.GET)
	public ModelAndView viewSitewiseWorkOrders(HttpServletRequest request, HttpSession session) {
		String toDate = "";
		String fromDate = "";
		String workOrderNumber = "";
		String response = "";
		String contractorId="";
		String contracterName="";
		String strSiteId="";
		ModelAndView model = null;
		List<WorkOrderBean> listOfCompletedWorkOrder = null;
		try {
			model = new ModelAndView();
			workOrderNumber = request.getParameter("siteWiseWorkOrderNo")==null?"":request.getParameter("siteWiseWorkOrderNo");
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			contracterName=request.getParameter("ContracterName")==null?"":request.getParameter("ContracterName");
			 strSiteId = request.getParameter("dropdown_SiteId");
			if(contracterName.length()==0){contractorId="";}
			//operationType if one means select the permanent data
			model.addObject("operType", "1");
			model.addObject("isUpdateWOPage",false);
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//here loading all the site id's available in properties, this for checking the current site_id is available in the properties file or for enabling this sub module to user
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//if (StringUtils.isNotBlank(site_Id)) {
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber) || StringUtils.isNotBlank(contractorId)||StringUtils.isNotBlank(strSiteId)) {
				
				listOfCompletedWorkOrder = workControllerService.getSitewiseWOCompltedDetail(fromDate, toDate, strSiteId,workOrderNumber, user_id,contractorId);
				if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
					request.setAttribute("showGrid", "true");
					response = "success";
				} else {
					model.addObject("succMessage", "No Data Available");
					response = "failure";
				}	
				//adding the listOfWoPendingStatus object to model object to carry from controller to jsp page
				model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
				//adding user entered fromDate value to model object we can show the value again to user
				model.addObject("fromDate", fromDate);
				//adding user entered toDate value to model object we can show the value again to user
				model.addObject("toDate", toDate);
				//adding user entered workOrderNumber value to model object we can show the value again to user
				model.addObject("workOrderNumber", workOrderNumber);
				model.addObject("contractorId", contractorId);
				model.addObject("ContracterName", contracterName);
				model.addObject("strSiteId", strSiteId);
				model.setViewName("WorkOrder/viewSitewiseWorkOrders");
			}else {
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "failure";
				model.setViewName("WorkOrder/viewSitewiseWorkOrders");
				return model;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}

	/**
	 * @description this controller used to call the work order print page
	 * @return
	 */
	@RequestMapping(value = "/printWorkOrderDetail.spring", method = RequestMethod.GET)
	public ModelAndView printWorkOrderDetail(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(request.getParameter("site_id")==null?"":request.getParameter("site_id"));
		String requestFromMail=request.getParameter("requestFromMail")==null?"":request.getParameter("requestFromMail");
		//loadSiteAddress is used to load the site address for work order print page
		List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		mav.addObject("SiteAddress", loadSiteAddress);
		//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code 
		mav.addObject("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		mav.addObject("requestFromMail", requestFromMail);
		mav.setViewName("WorkOrder/workorderPrintpage");
		return mav;
	}
	
	/**
	 * @description in this controller we are showing the permanent work order number for the update
	 * so here we are adding the attachments to the permanent work order
	 * @return
	 */
	@RequestMapping(value = "/updateWorkOrder.spring", method = RequestMethod.GET)
	public ModelAndView updateWorkOrder(HttpServletRequest request, HttpSession session) {
		String toDate = "";
		String fromDate = "";
		String workOrderNumber = "";
		String response = "";
		String contractorId="";
		String contracterName="";
		ModelAndView model = null;
		List<WorkOrderBean> listOfCompletedWorkOrder = null;
		try {
			model = new ModelAndView();
			model.addObject("urlForFormTag", "updateWorkOrder.spring");
			workOrderNumber = request.getParameter("siteWiseWorkOrderNo")==null?"":request.getParameter("siteWiseWorkOrderNo");
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			contracterName=request.getParameter("ContracterName")==null?"":request.getParameter("ContracterName");
			model.addObject("operType", "1");
			model.addObject("isUpdateWOPage",true);
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			//checking here the current login site id, is matching with loaded site id's from properties file or not if not then forward to response page and show the below msg
			if(!enableWOSubModulesSiteList.contains(site_Id)){
				//adding message to the model object to show the msg client side that this module you can't access
				model.addObject("customMsg","Hello "+strUserName+", <br>&emsp;&emsp; As of now "+siteName+" Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber) || StringUtils.isNotBlank(contractorId) ) {
				listOfCompletedWorkOrder = workControllerService.getMyWOCompltedDetail(fromDate, toDate, site_Id,workOrderNumber, user_id,contractorId);
				if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
					request.setAttribute("showGrid", "true");
					response = "success";
				} else {
					model.addObject("succMessage", "No Data Available");
					response = "failure";
				}
				//adding the listOfCompletedWorkOrder object to model object to carry from controller to jsp page
				//for the view purpose
				model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				model.addObject("contractorId", contractorId);
				model.addObject("ContracterName", contracterName);
				model.setViewName("WorkOrder/viewMyWorkOrders");
			} else {
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/viewMyWorkOrders");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
/**
 * @description this controller is used to load all the completed work order's ,
 * for revising the work order
 */
	@RequestMapping(value = "/reviseWorkOrder.spring", method = RequestMethod.GET)
	public ModelAndView revoiceWorkOrder(HttpServletRequest request, HttpSession session) {		
		String toDate = "";
		String fromDate = "";
		String workOrderNumber = "";
		String contracterName="";
		String response = "";
		String contractorId="";
		String typeOfWork="'PIECEWORK','MATERIAL','CONSULTANT'";
		ModelAndView model = null;
		List<WorkOrderBean> listOfCompletedWorkOrder = null;
		try {
			model = new ModelAndView();
			model.addObject("urlForFormTag", "reviseWorkOrder.spring");
			workOrderNumber = request.getParameter("siteWiseWorkOrderNo")==null?"":request.getParameter("siteWiseWorkOrderNo");
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			contracterName=request.getParameter("ContracterName")==null?"":request.getParameter("ContracterName");
	
			//adding the operation type which operation we are performing
			model.addObject("operType", "reviseWorkOrder");
			//adding the attribute that this the work order update page or not 
			model.addObject("isUpdateWOPage",false);
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber) || StringUtils.isNotBlank(contractorId) ) {
				//loading all the completed work order's
				listOfCompletedWorkOrder = workControllerService.getCompltedWorkOrderDetailForRevise(fromDate, toDate, site_Id,workOrderNumber, typeOfWork,contractorId);
				if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
					//if size of the list is not zero then dont't show the table
					request.setAttribute("showGrid", "true");
					response = "success";
				} else {
					//if size of the list is zero then show the error msg
					model.addObject("succMessage", "No Data Available");
					response = "failure";
				}
				//adding all the loaded work order's in model object
				//and displaying in view page 
				model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				model.addObject("contractorId", contractorId);
				model.addObject("ContracterName", contracterName);
				model.setViewName("WorkOrder/viewMyWorkOrders");
			} else {
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/viewMyWorkOrders");
			}
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	return model;
}

/**
 * @description this work order is for loading all the completed NMR work order's 
 * for the revise NMR work order purpose based on the type of work
 */
	@RequestMapping(value = "/reviseNMRWorkOrder.spring", method = RequestMethod.GET)
	public ModelAndView reviseNMRWorkOrder(HttpServletRequest request, HttpSession session) {
		String toDate = "";
		String fromDate = "";
		String workOrderNumber = "";
		String response = "";
		String contractorId="";
		String contracterName="";
		//setting type of work 
		String typeOfWork="'NMR'";
		ModelAndView model = null;
		List<WorkOrderBean> listOfCompletedWorkOrder = null;
		try {
			model = new ModelAndView();
			model.addObject("urlForFormTag", "reviseNMRWorkOrder.spring");
			workOrderNumber = request.getParameter("siteWiseWorkOrderNo");
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			contracterName=request.getParameter("ContracterName")==null?"":request.getParameter("ContracterName");
			//setting the operation type revise work order
			model.addObject("operType", "reviseWorkOrder");
			//setting the page type is this work order update page or not
			model.addObject("isUpdateWOPage",false);
			String site_Id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)|| StringUtils.isNotBlank(workOrderNumber) || StringUtils.isNotBlank(contractorId) ) {
				//loading all the completed NMR work order's
				listOfCompletedWorkOrder = workControllerService.getCompltedWorkOrderDetailForRevise(fromDate, toDate, site_Id,workOrderNumber, typeOfWork,contractorId);
				//if listOfCompletedWorkOrder is not empty or zero then show the table in view page
				//else show the error message if listOfCompletedWorkOrder is empty then throw the error message
				if (listOfCompletedWorkOrder != null && listOfCompletedWorkOrder.size() > 0) {
					request.setAttribute("showGrid", "true");
					response = "success";
				} else {
					model.addObject("succMessage", "No Data Available");
					response = "failure";
				}
				//adding listOfCompletedWorkOrder object into the model object for view purpose 
				model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				model.addObject("contractorId", contractorId);
				model.addObject("ContracterName", contracterName);
				model.setViewName("WorkOrder/viewMyWorkOrders");
			} else {
				//model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject("workOrderNumber", workOrderNumber);
				response = "success";
				model.setViewName("WorkOrder/viewMyWorkOrders");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * @description this controller used to update permanent work order
	 * @param workOrderBean object is holding all the necessary detail of the permanent work order
	 * @param files array are holding all the uploaded attachments in front end side
	 */
	@RequestMapping(value = "/updatePermantWorkOrderDetail.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateWorkOrderDetail(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean, Model model,
			HttpServletRequest request, HttpSession session,@RequestParam(value="file",required = false) MultipartFile[] files) {
		String response = "";
		model.addAttribute("urlForActivateSubModule", "updateWorkOrder.spring");
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		String actualtempIssueId=request.getParameter("actualtempIssueId")==null?"":request.getParameter("actualtempIssueId");
		String siteId=workOrderBean.getSiteId();
		//if this values are null means this is the error(user refreshed the page after success) so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&actualtempIssueId.length()==0&&contractorId.length()==0){
			model.addAttribute("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			return "response";
		}
		String PdfPathtodelete[]=request.getParameterValues("PdfPathtodelete");
		String imgPathtoDelete[]=request.getParameterValues("imgPathtoDelete");
		if(imgPathtoDelete!=null)
		for (String imgPath : imgPathtoDelete) {
			request.setAttribute("imagePath", imgPath.split("&")[0].split("=")[1]);
			request.setAttribute("isRequestFromController", true);
			deleteWOPermanentImage(request);
			
		}
		if(PdfPathtodelete!=null)
		for (String pdfPath : PdfPathtodelete) {
			request.setAttribute("imagePath", pdfPath.split("&")[0].split("=")[1]);
			request.setAttribute("isRequestFromController", true);
			deleteWOPermanentImage(request);
		}
		
		
		WorkOrderBean orderBean = null;
		try {
			//cloning the object so main object should not get any affect so we can use it
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
	
		WorkOrderBean bean = new WorkOrderBean();
		try {
			String operType = request.getParameter("operType");
			if(operType.equals("reviseWorkOrder")){
				//update permanent work order
				//response = workControllerService.updatePermanentWorkOrderDetails(orderBean, request, session, model, site_Id,user_id,files);
			}
			
			response="success";
			if (response.equals("success")) {
				response = "Work Order Updated Successfully. Work Order No is  "+workOrderBean.getWorkOrderNo();
				
				//model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				//String siteId=workOrderBean.getSiteId();
				
				//model.addAttribute("WorkOrderBean", orderBean);
				//loading the path of the images
				String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
				
				File dir = new File(rootFilePath+siteId+"\\"+orderBean.getSiteWiseWONO().replace("/", "-"));
			
				String filePath ="";
				//getting how many images of PDF already uploaded before this update temporary work order
				int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
				int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent"));
				int imgCount=imagesAlreadyPresent;
				int pdfCount=pdfAlreadyPresent;
		    	String workOrderNum=orderBean.getSiteWiseWONO().replace("/", "-");
				//files is a reference of MultipartFile object
				for (int i = imagesAlreadyPresent; i < files.length; i++) {
					MultipartFile multipartFile = files[i];
					//if multipartFile not empty then only enter into the condition
					if(!multipartFile.isEmpty()){
					try {
						//if directory is not exist for this work order number then create the folder
							if (!dir.exists())
								dir.mkdirs();
						//if the file name end's with the PDF then create the file name file path with the extension of PDF same as JPG
							if(multipartFile.getOriginalFilename().endsWith(".pdf")){
								filePath = dir.getAbsolutePath()+ File.separator + workOrderNum+"_Part"+pdfCount+".pdf";
								//increment pdf count, means how many pdf has been uploaded till now
								pdfCount++;
							}else{
								filePath = dir.getAbsolutePath()	+ File.separator + workOrderNum+"_Part"+imgCount+".jpg";
								//increment image count, means how many pdf has been uploaded till now
								imgCount++;
							}
							
							File convFile = new File(filePath);
						    convFile.createNewFile(); 
						    FileOutputStream fos = new FileOutputStream(convFile); 
						    fos.write(multipartFile.getBytes());
						    fos.close(); 
							
							model.addAttribute("message", "Work Order Updated Successfully. Work Order No is  "+workOrderBean.getWorkOrderNo());
						} catch (Exception e) {
							e.printStackTrace();
							log.info("Image NOT Uploaded");
							//return "You failed to upload " ;
							model.addAttribute("message1", "Work order image not uploaded successfully.");
						}
					}
				}//For Loop
				//adding in model object attribute response the client 
				model.addAttribute("message", response);
				//adding in model object attribute is this work order page or not
				model.addAttribute("isUpdateWOPage",true);
				if(operType.equals("reviseWorkOrder")){
					return "WorkOrder/workorderPrintpage";
				}
			} else {
				response = "Work order failed to  approve.";
				model.addAttribute("message1", response);
			}
		} catch (Exception e) {
			response = "Work Order failed to  Approve.";
			model.addAttribute("message1", response);
		}
		return "response";
	}

	/**
	 * @description this controller used give necessary data to NMR work order creation page like major head names, 
	 * temporary work order number, permanent work order number
	 * 
	 */
	@RequestMapping(value = "/nmrWorkOrder.spring", method = RequestMethod.GET)
	public String nmrWorkOrder(Model model, HttpServletRequest request, HttpSession session) {
		WorkOrderBean workOrderBean = new WorkOrderBean();
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
		String user_id1 = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		workOrderBean.setSiteId(siteId);
		//loading the next level approver using this method with the help of bean object and user_id
		workOrderBean = workControllerService.getWorkOrderFromAndToDetails(user_id1, workOrderBean);
		String typeOfWork="NMR";
		String[] data={typeOfWork,siteId};
		//loading all the major head's of NMR work order
		Map<String, String> WO_QSList = workControllerService.loadNMRProducts(data);
		//loading the temporary work order number site wise
		int QSWorkOrderNo = workControllerService.getMaxQSWorkOrderNoSiteWise(siteId);
		//loading permanent work order number while creating the work order
		String workOrderNo = workControllerService.getWorkOrderNoSiteWise(siteId,siteName,typeOfWork);
		
		workOrderBean.setSiteWiseWONO(String.valueOf(QSWorkOrderNo));
		workOrderBean.setWorkOrderNo(workOrderNo);
		model.addAttribute("WorkOrderBean", workOrderBean);
		model.addAttribute("workMajorHead", WO_QSList);
		//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
		model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		// if the approval emp id is null then redirect to work order
		if (workOrderBean.getApproverEmpId() == null||workOrderBean.getApproverEmpId().equals("-")) {
			model.addAttribute("response1", "You Cannot Create Work Order.");
			//model.addAttribute("javaScriptProp","<script>document.getElementById('saveBtnId').style.display='none';document.getElementById('workOrderFormId').style.display='none';</script>");
			return "response";
		}/*else if (workOrderBean.getApproverEmpId().equals("-")) {
			workOrderBean.setApproverEmpId("END");
		}*/
		//loading default terms and condition's while creating work orde number
		List<WorkOrderBean> listTermsAndCondition = workControllerService.getTermsAndConditions(siteId);
		model.addAttribute("listTermsAndCondition", listTermsAndCondition);
		model.addAttribute("TC_listSize", listTermsAndCondition.size());
		//model.addAttribute("optionalCCmails", UIProperties.validateParams.getProperty(siteId+"_WORKORDER_CCMAILS") == null ? "00" : UIProperties.validateParams.getProperty(siteId+"_WORKORDER_CCMAILS").toString());
		
		String user_id = String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		SaveAuditLogDetails.auditLog("", user_id, "New NMR Work Order View", "success", site_id1);
		return "WorkOrder/NMRWorkOrder";
	}
	
	/**
	 * @description this controller used to store the NMR work order details
	 * @param workOrderBean object is holding the work order details like work order name, temp work order number, permanent work order number
	 */
	@RequestMapping(value = "/saveNMRWorkOrder.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveNMRWorkOrder(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,
			HttpServletRequest request, HttpSession session, Model model) {
		model.addAttribute("urlForActivateSubModule", "nmrWorkOrder.spring");
		ModelAndView modelAndView = new ModelAndView();
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String user_id1 = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null?"":request.getParameter("numbeOfRowsToBeProcessed");
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		if(recordsCount.length()==0&&workOrderNo.length()==0&&contractorId.length()==0){
			modelAndView.addObject("message1", "Oops !!! There was a improper request found. Please click on the sub-module and continue your Operation.");
			modelAndView.setViewName("response");
			return modelAndView;
		}
		
		WorkOrderBean orderBean = null;
		ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
		qsBillBean.setSiteId(session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString());
		//loadSiteAddress is used to load the site address for work order print page
		List<String> loadSiteAddress = workBillControllerServiceObject.loadSiteAddress(qsBillBean);
		//adding object to modelAndView object so it can carry this object to view for printing operation
		request.setAttribute("SiteAddress", loadSiteAddress);		
		try {
			//cloning the object so main object should not get any affect so we can use it with out any modification
			orderBean = (WorkOrderBean) workOrderBean.clone();
		} catch (CloneNotSupportedException e1) {
			log.info(e1.getMessage());
		}
		
		String response = "";
		try {
			orderBean.setSiteId(siteId);
			orderBean.setUserId(user_id1);
			//calling service to insert the NMR details
			response = workControllerService.doNMRWorkOrderEntry(orderBean, request, model, siteId, user_id1, session);
			if (response.equals("success")) {
				model.addAttribute("WorkOrderBean", orderBean);
				response = "NMR Work Order Created Successfully.";
				//adding the properties file data into the model object to access the properties file data in a jsp scriplet java code
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("response1", response);
		
				//loading work order created user names and designation and operation performed name
			   List<Map<String, Object>>	listOfVerifiedEmpNames=workControllerService.getWorkOrderVerifiedEmpNames(workOrderBean);
				//adding all the list object's in model object  
				model.addAttribute("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
				//setting the view page name
				modelAndView.setViewName("WorkOrder/workorderPrintpage");
				return modelAndView;
			}else if("not changed work order".equals(response)){
				response="No changes has made, this work order cannot be modify.";
				model.addAttribute("response1", response);
			}else if(response.equals("Work Order Number already exists")){
				response = "NMR work order creation failed, You manually entered work order number ("+workOrderBean.getWorkOrderNo()+") is already exists in record please try with another work order number.";
				model.addAttribute("response1", response);
			}  else {
				response = "NMR work order creation failed.";
				//adding response to model object for printing in the view page
				model.addAttribute("response1", response);
			}
			//adding response to model object for printing in the view page
			model.addAttribute("responseMessage", response);
		} catch (Exception e) {
			response = "Work order creation failed.";
			model.addAttribute("response1", response);
			e.printStackTrace();
		}
 		modelAndView.setViewName("response");
		return modelAndView;
	}
	
	/**
	 * @description this controller is used for showing all the version's of work order
	 * @param workOrderBean object is holding the work order details like work order name, temp work order number, permanent work order number
	 */
	@RequestMapping(value = "/showAllWorkOrderVersion.spring", method = RequestMethod.POST)
	public ModelAndView showAllWorkOrderVersion(@ModelAttribute("WorkOrderBean") WorkOrderBean workOrderBean,HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		try {
			List<WorkOrderBean> listOfCompletedWorkOrder=	workControllerService.getAllRevisedWorkOrderList(workOrderBean,"");
			model.addObject("listOfCompletedWorkOrder", listOfCompletedWorkOrder);
			model.setViewName("WorkOrder/ShowAllWOVersion");
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}
}
