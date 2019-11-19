package com.sumadhura.in;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.FileUtils;
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
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.service.ContrctorBillGeneraterService;
import com.sumadhura.service.DCFormService;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.service.WorkOrderService;
import com.sumadhura.service.WorkOrderServiceImpl;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
//import com.sumadhura.util.EmailService_CustomerMails;
import com.sumadhura.util.UIProperties;

/**
 * @description this controller is used for the work order bill's
 * @author Aniket Chavan
 *
 */
@Controller
public class ContractorBillGenerateController {
	static Logger log = Logger.getLogger(ContractorBillGenerateController.class);
	@Autowired
	@Qualifier("wobill_generater_service")
	ContrctorBillGeneraterService workBillControllerService;

	@Autowired
	@Qualifier("workControllerService")
	WorkOrderService workControllerService;

	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;
	 
/*	query for checking amount difference of NMR bill's
	Select sum((HR.ALLOCATED_QTY*HR.NO_OF_HOURS*HR.RATE)/8) as Amount,QCB.CERTIFIED_AMOUNT,(QCB.CERTIFIED_AMOUNT-sum((HR.ALLOCATED_QTY*HR.NO_OF_HOURS*HR.RATE)/8)) as AMT_DIFF,HR.BILL_ID,QWI.WORK_ORDER_NUMBER,QCB.QS_WORKORDER_ISSUE_ID,QCB.SITE_ID  
	from QS_INV_AGN_WORK_PMT_DTL_HR HR,QS_CONTRACTOR_BILL QCB,QS_WORKORDER_ISSUE QWI
	Where QCB.BILL_ID=HR.BILL_ID AND HR.QS_WORKORDER_ISSUE_ID=QCB.QS_WORKORDER_ISSUE_ID   AND QWI.QS_WORKORDER_ISSUE_ID=QCB.QS_WORKORDER_ISSUE_ID  
	--AND  HR.QS_WORKORDER_ISSUE_ID='1613' 
	--AND HR.BILL_ID='NMR/55'
	and ALLOCATED_QTY!=0 group by HR.BILL_ID, QCB.CERTIFIED_AMOUNT,QWI.WORK_ORDER_NUMBER,QCB.QS_WORKORDER_ISSUE_ID,QCB.SITE_ID  order by BILL_ID;

	query for checking amount difference of RA, ADV bill's
	Select sum( ALLOCATED_QTY*RATE) as Amount,QCB.CERTIFIED_AMOUNT,(QCB.CERTIFIED_AMOUNT-sum(ALLOCATED_QTY*RATE)) as AMT_DIFF,HR.BILL_ID,QCB.QS_WORKORDER_ISSUE_ID,QWI.WORK_ORDER_NUMBER,QCB.SITE_ID
	from QS_INV_AGN_AREA_PMT_DTL HR,QS_CONTRACTOR_BILL QCB,QS_WORKORDER_ISSUE QWI
	Where QCB.BILL_ID=HR.BILL_ID AND HR.QS_WORKORDER_ISSUE_ID=QCB.QS_WORKORDER_ISSUE_ID    AND QWI.QS_WORKORDER_ISSUE_ID=QCB.QS_WORKORDER_ISSUE_ID  
	--AND  HR.QS_WORKORDER_ISSUE_ID='1613' 
	--AND HR.BILL_ID='NMR/55'
	and ALLOCATED_QTY!=0 group by HR.BILL_ID, QCB.CERTIFIED_AMOUNT,QCB.QS_WORKORDER_ISSUE_ID,QWI.WORK_ORDER_NUMBER,QCB.SITE_ID  order by BILL_ID;

*/	
	/**
	 * @description this controller used to load the deduction types for bill's
	 */
	@RequestMapping(value = "/getRABillDuductionType.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> getRABillDuductionType(HttpServletRequest request) {
		String ids = request.getParameter("id");
	///	log.info("ContractorQSBillGenerateController.getRABillDuductionType() " + ids);
		// calling service method to load the deduction types for work order bill's
		return workBillControllerService.loadDeductionTypes(ids);
	}

	/**
	 * @description this method will render the page to the physical page of
	 *              create Bill with sending some necessary data
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/certificateOfPayment.spring", method = RequestMethod.GET)
	public String certificateOfPayment(Model model, HttpServletRequest request, HttpSession session) {
	//	log.info("WorkController.work()");
		try {
			WorkOrderBean workOrderBean = new WorkOrderBean();
			//ContractorQSBillBean contractorQSBillBean=new ContractorQSBillBean();
			String woTypeOfWork="";
			StringBuffer workOrderType=new StringBuffer("");
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();

			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			woTypeOfWork=request.getParameter("woTypeOfWork")==null?"":request.getParameter("woTypeOfWork");
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below message
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}
			
			model.addAttribute("urlForActionTag", "certificateOfPayment.spring");
	   		model.addAttribute("pageName", "Certificate Of Payment");
	   		workOrderBean.setSiteId(siteId);
			if(woTypeOfWork.length()==0){
				List<Map<String, Object>> list=	getBillTypes(userId,workOrderBean,model,workOrderType);
				return "WorkOrder/SelectWOTypeOfWork";
			}else{
				workOrderBean.setTypeOfWork(woTypeOfWork);	
			}
	   		
			
			// loading the next level approver using this method with the help
			// of bean object and user_id
			workOrderBean = workBillControllerService.getWorkOrderFromAndToDetails(userId, workOrderBean);
			if (workOrderBean.getApproverEmpId() == null||workOrderBean.getApproverEmpId().equals("-")) {
				model.addAttribute("response1", "You can't initiate bill.");
				return "response";
			}
			// adding workOrderBean object with taking necessary data like next level approval
			model.addAttribute("WorkOrderBean", workOrderBean);
		 	model.addAttribute("SiteName", session.getAttribute("SiteName"));
			model.addAttribute("SiteId", session.getAttribute("SiteId"));
			model.addAttribute("indentReceiveModelForm", new IndentReceiveBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/CreateAdvBill";
	}

	
	public List<Map<String, Object>> getBillTypes(String userId, WorkOrderBean workOrderBean, Model model, StringBuffer workOrderType){
		List<Map<String, Object>> list=workBillControllerService.userAbleToCreateBillTypes(userId,workOrderBean);
		 for (Map<String, Object> map : list) {
			String moduleType=map.get("MODULE_TYPE")==null?"":map.get("MODULE_TYPE").toString();
			if(moduleType.equals("WOB")){
				workOrderType.append("<option value='WOB'>Piece Work</option>");
			}/*else if(moduleType.equals("WOM")){
				workOrderType.append("<option value='MATERIAL'>Material</option>");
			}*/else if(moduleType.equals("WOB_CONSULTANT")){
				workOrderType.append("<option value='WOB_CONSULTANT'>Consultant</option>");
			}else{
				
			}
		}
		 workOrderBean.setTypeOfWork(workOrderType.toString());
		 model.addAttribute("workOrderType", workOrderType.toString());
		return list;
	}
	
	/*
	 * @RequestMapping(value = "/BillOfQuantities.spring", method =
	 * RequestMethod.GET) public String CertificateOfPaymentRA(Model model,
	 * HttpSession session) { Map<String, String> workDescType =
	 * service.loadWorkDsc(null); model.addAttribute("SiteName",
	 * session.getAttribute("SiteName")); model.addAttribute("SiteId",
	 * session.getAttribute("SiteId"));
	 * model.addAttribute("indentReceiveModelForm", new IndentReceiveBean());
	 * model.addAttribute("workTypeDesc", workDescType); return
	 * "WorkOrder/CertificateOfPaymentRA"; }
	 */

	/**
	 * @description this controller is used for loading permanent bill no while
	 *              creating bill RA,ADV,NMR
	 */
	@RequestMapping(value = "/loadAdvRAPermanentBillNo.spring", method = RequestMethod.GET)
	public @ResponseBody String loadAdvRAPermanentBillNo(HttpServletRequest request, HttpSession session) {

		String permanentBillNo = "";
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
			String contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");
			String contractorGSTIN = request.getParameter("contractorGSTIN") == null ? "" : request.getParameter("contractorGSTIN");
			String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
			String billNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
			workOrderNo = workOrderNo.split("\\$")[0].trim();
			String approvePage = request.getParameter("approvePage") == null ? "false": request.getParameter("approvePage");

			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setContractorId(contractorId);
			billBean.setBillNo(billNo);
			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setPaymentType(billType);
			billBean.setSiteName(siteName);
			billBean.setGSTIN(contractorGSTIN);
			// loading permanent bill no, using the type of the bill
			permanentBillNo = workBillControllerService.loadAdvRAPermanentBillNo(billBean);
			// Checking if this is not approve page then check is any pending
			// bill's is there or not if not don't return any error message
			if (approvePage.equals("false")) {
				// checking if any bill is pending for the contractor then we
				// can't initiate another bill
				boolean flag = workBillControllerService.isAnyRaAndAdvBillPending(billBean);
				if (flag) {
					return "PendingRAAdv@@Please check the previous running account bills.";
				}
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return permanentBillNo;
	}

	/**
	 * @description this controller used to store the data of advance bill and
	 *              this advance bill don't have any deduction and we have to
	 *              fill the payment area abstract, wile creating advance bill
	 *              we can't enter manually amount we have select the payment
	 *              area
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/generateAdvancePaymentBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView generateAdvancePaymentBill(HttpServletRequest request, HttpSession session) {
		log.info("ContractorBillGenerateController.generateAdvancePaymentBill()");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("response");
		String response ="";
		//this URl is for highlighting sub module
		mav.addObject("urlForActivateSubModule", "certificateOfPayment.spring");
		String contractorId = request.getParameter("ContractorId")==null?"":request.getParameter("ContractorId");
		String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String permanentBillNo = request.getParameter("advBillNo") == null ? "" : request.getParameter("advBillNo");
		
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&permanentBillNo.length()==0&&contractorId.length()==0){
			mav.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			mav.setViewName("response");
			return mav;
		}
		try {
			// calling service layer to store the advance bill details
			response = workBillControllerService.generateAdvancePaymentBill(request, session);
			if (response.equals("success")) {
				response = "Work order Adv bill generated successfully.Temp Bill no is "
						+ request.getAttribute("TempAdvBillNo") + ",\n" + " Bill no is " + request.getAttribute("AdvBillNo");
				// adding response in model object so we can show the success
			 	mav.addObject("message", response);
			} else {
				// adding response in model object so we can show error message in the view page
				response = "Work order Adv bill generation failed.";
				mav.addObject("message1", response);
			}
			// setting the view name so that view resolver will render this
			// logical view name into the physical address
			mav.setViewName("response");
		} catch (Exception e) {
			e.printStackTrace();
			response = "Work order Adv bill generation failed.";
			if(e.getMessage().equals("missing work order issue id")){
				response = "Work order number not found please try again later. or contact to support team.";
			}
			if(e.getMessage().equals("missing bill number")){
				response = "Work order bill number not found please try again later. or contact to support team.";
			}
			
			mav.addObject("message1", response);
		}
		return mav;
	}

	/**
	 * @description this controller will use to load all the details of the bill
	 *              and the previous bill details by using the work order
	 *              number. using work order number we will get all the created
	 *              bill's details
	 * @return
	 */
	@RequestMapping(value = "/loadAdvBillCertificatateDetails.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadAdvBillCertificatateDetails(Model model,
			HttpServletRequest request, HttpSession session) {
		String contractorId = request.getParameter("contractorId");
		String workOrderNo = request.getParameter("workOrderNo");
		String billDate = request.getParameter("billDate") == null ? "" : request.getParameter("billDate");
		String approvePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage");
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo");
 
		String siteId = "";//session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		siteId = request.getParameter("site_id")==null?"":request.getParameter("site_id");

		ContractorQSBillBean billBean = new ContractorQSBillBean();
		billBean.setSiteId(siteId);
		billBean.setUserId(userId);
		billBean.setFromEmpId(userId);
		billBean.setContractorId(contractorId);
		billBean.setWorkOrderNo(workOrderNo);
		billBean.setBillDate(billDate);
		billBean.setTempBillNo(tempBillNo);
		billBean.setIsApprovePage(approvePage);
		try {
			// calling service layer to return the current bill details and the
			// previous bill details if exist
			List<Map<String, Object>> advPaymentDetaillist = workBillControllerService.getAdvPaymentCertificateDetail(billBean);
			return advPaymentDetaillist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description this controller used for checking the user entered bill no
	 *              exist or not if exist show the error message in view page,
	 *              and this controller called by ajax call so that's why we
	 *              have used @ResponseBody annotation of the spring
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/checkBillNoExistsOrNot.spring", method = RequestMethod.GET)
	@ResponseBody
	public String checkBillNoExistsOrNot(HttpServletRequest request, HttpSession session) {
		String flag = "";
		try {
			
			// calling service method to check is this bill number is valid or not
			flag = workBillControllerService.checkBillNoExistsOrNot(request,session);
		} catch (Exception e) {
			log.info("Got excpetion"+e.getMessage());
		}
		return flag;
	}

	/**
	 * @description this controller is used to load the permanent bill recovery details
	 * @return
	 */
	@RequestMapping(value = "/loadPermanentRecoveryAreaDetails.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadpermanentRecoveryAreaDetails(Model model, HttpServletRequest request, HttpSession session) {
		String contractorId = request.getParameter("contractorId");
		String workOrderNo = request.getParameter("workOrderNo");
		ContractorQSBillBean billBean = new ContractorQSBillBean();
		billBean.setWorkOrderNo(workOrderNo.split("\\$")[0]);
		billBean.setContractorId(contractorId);
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		siteId = request.getParameter("site_id");
		String approvePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage");
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		String billNo = request.getParameter("billNo") == null ? "" : request.getParameter("billNo").trim();

		billBean.setSiteId(siteId.trim());
		billBean.setPaymentType(billType.trim());
		String[] data = { contractorId, workOrderNo.split("\\$")[0], siteId, approvePage, tempBillNo, billNo };
		List<Map<String, Object>> list = null;
		//calling service layer to load the permanent details of bill's
		list = workBillControllerService.loadPermanentRecoveryAreaDetails(data);
		return list;
	}

	/**
	 * @description this controller will load the work order recovery details means given item to use while work order is going
	 * and this controller will load the recovered amount while creating bill's
	 */
	@RequestMapping(value = "/loadRecoveryAreaDetails.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadRecoveryAreaDetails(Model model, HttpServletRequest request, HttpSession session) {
		String contractorId = request.getParameter("contractorId");
		String workOrderNo = request.getParameter("workOrderNo");
		ContractorQSBillBean billBean = new ContractorQSBillBean();
		billBean.setWorkOrderNo(workOrderNo.split("\\$")[0]);
		billBean.setContractorId(contractorId);
		String siteId = "";
		siteId = request.getParameter("site_id")==null?"":request.getParameter("site_id");

		String approvePage = request.getParameter("approvePage") == null ? "false": request.getParameter("approvePage");
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		billBean.setSiteId(siteId.trim());
		billBean.setPaymentType(billType.trim());
		
		String[] data = { contractorId, workOrderNo.split("\\$")[0], siteId, approvePage, tempBillNo };
		List<Map<String, Object>> list = null;
		/*if (approvePage.equals("false")) {
			//
			list = workBillControllerService.loadRecoveryAreaDetails(data);
		} else {*/
			//calling service layer to load the work order bills recovery details
			list = workBillControllerService.loadRecoveryAreaDetails(data);
		//}
		return list;
	}
/**
 * @description this controller used to load the permanent bill abstract data 
 * bill wise
 */
	@RequestMapping(value = "/loadPermanentWorkOrderArea.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadPermanentWorkOrderArea(Model model, HttpServletRequest request,
			HttpSession session) {
		//String contractorId = request.getParameter("contractorId");
		String workOrderNo = request.getParameter("workOrderNo");
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		//String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		String billNo = request.getParameter("billNo") == null ? "" : request.getParameter("billNo").trim();
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		siteId = request.getParameter("site_id");
		try {
			//calling service layer to load the permanent bill abstract data
			List<Map<String, Object>> list = workBillControllerService.loadPermanentWorkOrderArea(billNo, "", workOrderNo, siteId, userId, billType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description this controller used to print the abstract data of bill
	 * @param billBean object is modelAttribute object holding necessary data that to be print in abstract page
	 * @return
	 */
	@RequestMapping(value = "/printAbstract.spring", method = RequestMethod.POST)
	public String printAbstract(@ModelAttribute("billBean") ContractorQSBillBean billBean, Model model,
			HttpServletRequest request, HttpSession session) {

		String tempBillNo = request.getParameter("tempBillNo");
		String approvePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage");
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		String nextLevelApprovelEmpID = request.getParameter("nextLevelApprovelEmpID") == null ? "" : request.getParameter("nextLevelApprovelEmpID");
		String billDate = request.getParameter("billDate") == null ? "" : request.getParameter("billDate");
		String siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
		//String ContractorName = request.getParameter("ContractorName") == null ? "" : request.getParameter("ContractorName");
		String billNo = request.getParameter("permanentBillNo") == null ? "" : request.getParameter("permanentBillNo");
		if (billNo.length() != 0)
			billBean.setBillNo(billNo);
		model.addAttribute("billBean", billBean);
		billBean.setSiteId(siteId);
		model.addAttribute("billType", billType);
		model.addAttribute("approvePage", approvePage);
		model.addAttribute("tempBillNo", tempBillNo);
		model.addAttribute("billDate", billDate);
		model.addAttribute("nextLevelApprovelEmpID", nextLevelApprovelEmpID);
		return "WorkOrder/PrintAbstract";
	}
/**
 * @description this controller used to print the abstract of whole work order
 * @param billBean object is modelAttribute object holding necessary data that to be print in abstract page
 * @return
 */
	@RequestMapping(value = "/printWOAbstract.spring", method = RequestMethod.POST)
	public String printWOAbstract(@ModelAttribute("WorkOrderBean") WorkOrderBean billBean, Model model,
			HttpServletRequest request, HttpSession session) {
		
		String tempBillNo = request.getParameter("tempBillNo");
		String approvePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage");
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		String nextLevelApprovelEmpID = request.getParameter("nextLevelApprovelEmpID") == null ? "" : request.getParameter("nextLevelApprovelEmpID");
		String billDate = request.getParameter("billDate") == null ? "" : request.getParameter("billDate");
	
		model.addAttribute("billBean", billBean);
		model.addAttribute("billType", billType);
		model.addAttribute("approvePage", approvePage);
		model.addAttribute("tempBillNo", tempBillNo);
		model.addAttribute("billDate", billDate);
		model.addAttribute("nextLevelApprovelEmpID", nextLevelApprovelEmpID);
		return "WorkOrder/PrintWOAbstract";
	}

	/**
	 * @description this controller used to load the work order work area details for the ADV and RA bills in Abstract popup
	 */
	@RequestMapping(value = "/loadWorkOrderArea.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadWorkOrderAreaForBill(Model model, HttpServletRequest request, HttpSession session) {
		String contractorId = request.getParameter("ContractorId");
		String workOrderNo = request.getParameter("workOrderNo");
		String workDesc = request.getParameter("workDesc");
		String approvePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage");
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");

		boolean isReqFromApprovePage = Boolean.valueOf(approvePage);
		String siteId = "";
		String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		siteId = request.getParameter("site_id");
		try {
			if (isReqFromApprovePage) {
				String tempBillNo = request.getParameter("tempBillNo");
				//if the request from approve page or status page to load the selected data of the bill abstract,
				//while creation of the bill
				List<Map<String, Object>> list = workBillControllerService.loadWorkOrderAreaForApprovelBill(tempBillNo,
						billType, workOrderNo, siteId, userId);
				return list;
			} else {
				//request from create RA bill or ADV bill
				//calling to service layer to load the previous allocated quantity if any there and also total qty,rate of the work order
				List<Map<String, Object>> list = workBillControllerService.loadWorkOrderAreaForBill(contractorId,
						workDesc, workOrderNo, siteId, userId, billType);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @description this controller will load all the pending bill's which are pending in particular level 
	 */
	@RequestMapping(value = "/approveContractorBills.spring", method = RequestMethod.GET)
	public ModelAndView approveContractorBills(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderBillGenerateController.approveContractorBills()");
		ModelAndView model = new ModelAndView();
		String siteId = "";
		String user_id = "";
 		try {
			siteId = (String) CheckSessionValidation.validateUserSession(model, session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
												? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addObject("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//calling service layer to load all the pending bill's
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(user_id, siteId);
			//adding all the pending bill's in model object so we can show all the pending work order bill's
			model.addObject("workOrderList", workOrderBeans);
			model.addObject("isCommonApproval", false);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			String responseMessage = request.getParameter("responseMessage");
			model.addObject("responseMessage", responseMessage);
		}
		model.setViewName("WorkOrder/ApproveContractorBills");
		return model;
	}

	
	/**
	 * @description this controller will load all the pending bill's which are pending in particular level 
	 */
	@RequestMapping(value = "/siteWiseApproveContractorBills.spring", method = RequestMethod.GET)
	public ModelAndView siteWiseApproveContractorBills(HttpServletRequest request, HttpSession session) {
		
		ModelAndView model = new ModelAndView();
		String siteId = "";
		String user_id = "";

		String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
		model.addObject("urlForActionTag", "siteWiseApproveContractorBills.spring");
   		model.addObject("pageName", "Site Wise Bill Approve");
		if(dropdown_SiteId.length()==0){
			model.setViewName("SiteSelection");
			return model;
		}
 		try {
			siteId = (String) CheckSessionValidation.validateUserSession(model, session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
												? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below MSG
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addObject("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}
			
			siteId=dropdown_SiteId;
			user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			//calling service layer to load all the pending bill's
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(user_id, siteId);
			//adding all the pending bill's in model object so we can show all the pending work order bill's
			model.addObject("workOrderList", workOrderBeans);
			model.addObject("strSiteId", siteId);
			model.addObject("isCommonApproval", true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			String responseMessage = request.getParameter("responseMessage");
			model.addObject("responseMessage", responseMessage);
		}
		model.setViewName("WorkOrder/ApproveContractorBills");
		return model;
	}
	
	/**
	 * @description this controller used to reject RA and ADV bill
	 */
	@RequestMapping(value = "/rejectWorkOrderRABill.spring", method = RequestMethod.POST)
	public ModelAndView rejectWorkOrderRABill(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		String response = "";
		String siteId = "";
		String user_id = "";
	//	String billType = "RA";
		siteId = (String) CheckSessionValidation.validateUserSession(model,
				session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
		user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		try {
			// calling service method to reject the bill
			response = workBillControllerService.rejectRABill(request, session);
			if (response.equals("success")) {
				response = "Work order bill rejected successfully.";
				//adding success message in model object for view purpose
				model.addObject("responseMessage", response);
			} else {
				response = "Work order bill rejection failed.";
				//adding failed message in model object for view purpose
				model.addObject("responseMessage1", response);
			}
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(user_id, siteId);
			model.addObject("workOrderList", workOrderBeans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.setViewName("WorkOrder/ApproveContractorBills");
	return model;
	}

	/**
	 * @description this controller used to load the completed bill details
	 * the completed bill data will be loaded using bill number and site id and work order number
	 */
	@RequestMapping(value = "/showWOCompltedBillsDetails.spring", method = RequestMethod.GET)
	public ModelAndView showWOCompltedBillsDetails(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		String siteId = "";
		String userId = "";
		//String response = "";
		String billNo = "";
		String billType = "";
		String status = "";
		String workOrderNo = "";
		String isBillUpdatePageRequest = "";
		String printName = "";
		String isSiteWiseStatusPage="";
		try {
	
			siteId = request.getParameter("site_id");
			if(siteId==null){
				siteId = (String) CheckSessionValidation.validateUserSession(model,
						session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			}
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			status = request.getParameter("status") == null ? "false" : request.getParameter("status");
			billNo = request.getParameter("BillNo") == null ? "" : request.getParameter("BillNo");
			billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
			workOrderNo = request.getParameter("WorkOrderNo") == null ? "" : request.getParameter("WorkOrderNo");
			isBillUpdatePageRequest = request.getParameter("isBillUpdatePage") == null ? "false": request.getParameter("isBillUpdatePage");
			printName = request.getParameter("printName") == null ? "" : request.getParameter("printName");
			isSiteWiseStatusPage=request.getParameter("isSiteWiseStatusPage")==null?"":request.getParameter("isSiteWiseStatusPage").trim();
			String[] data = { billNo.trim(), userId, siteId.trim(), status, workOrderNo };
			//checking is this processing bill number is valid or not
			boolean isValid = workBillControllerService.isValidCompltedTempBillNo(data);
			//if the bill not valid return the error message in view page
			if (!isValid) {
				//load again completed bill data with showing the erir message
				List<ContractorQSBillBean> workOrderBeans = workBillControllerService .getCompletdWorkOrderBills( siteId, null);
				model.addObject("workOrderList", workOrderBeans);
				model.setViewName("WorkOrder/ViewMyCompltedRAAdvBill");
				//returning error message
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			if(billType.length()!=0){
				billType=billNo.split("/")[0].trim();
			}
			
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			qsBillBean.setBillNo(billNo.trim());
			qsBillBean.setUserId(userId);
			qsBillBean.setStatus(status);
			qsBillBean.setSiteId(siteId);
			qsBillBean.setWorkOrderNo(workOrderNo.trim());
			qsBillBean.setPaymentType(billType);
			//loading bill data like contractor name,work order number, bill number, bill date
			List<ContractorQSBillBean> list = workBillControllerService .getCompltedWorkOrderContractorBillDetailById(qsBillBean);
			ContractorQSBillBean bean = list.get(0);
			//loadSiteAddress is used to load the site address for work order print page
			List<String> loadSiteAddress = workBillControllerService.loadSiteAddress(qsBillBean);
			//loading all the deduction's of completed RA bill
			List<Map<String, Object>> deductionList = workBillControllerService .getRaAdvCompletedDeductionDetails(qsBillBean);
			// getting the deductions

			if (list.size() == 0) {
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			
			bean.setIsSiteWiseStatusPage(isSiteWiseStatusPage);
			if(billType.length()==0){
				billType=bean.getPaymentType();
			}
			
			List<Map<String, Object>> listOfVerifiedEmpNames=workBillControllerService.getTempBillVerifiedEmpNames(bean);
			//adding all the list object's in model object  
			model.addObject("listOfVerifiedEmpNames", listOfVerifiedEmpNames);

			//adding all the necessary data in model object for the view purpose
			model.addObject("deductionList", deductionList);
			model.addObject("SiteAddress", loadSiteAddress);
			model.addObject("printName", printName);
			model.addObject("woLevelPurpose", bean.getRemarks());
			WorkOrderBean workOrderBean = new WorkOrderBean();
			workOrderBean.setSiteId(list.get(0).getSiteId());
			model.addObject("billBean", bean);
			model.addObject("approvePage", true);
			model.addObject("nextLevelApprovedId", "END");
			model.addObject("SiteName", session.getAttribute("SiteName"));
			model.addObject("SiteId", session.getAttribute("SiteId"));
 		} catch (Exception e) {
			//if any exception is occurring while loading the bill data
			//then load the completed bill's data 
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getCompletdWorkOrderBills(siteId, null);
			model.addObject("workOrderList", workOrderBeans);
			model.setViewName("WorkOrder/ViewMyCompltedRAAdvBill");
			model.addObject("responseMessage1", "Invalid temp bill number.");
			e.printStackTrace();
			return model;
		}
//		adding isBillUpdatePageRequest in model object so we can recognize which operation view have to
//		update operation or only view operation
		model.addObject("isBillUpdatePageRequest", isBillUpdatePageRequest);
//based bill type we are redirecting request to the there respective pages
		if (billType.equals("NMR")) {
			model.setViewName("WorkOrder/ViewCompletedNMRBills");
			model.addObject("status", status);
			// if status is false then pass to NMR Bill Contractor Copy
			if (status.equals("false")) {
				// send request to print contractor copy
				model.addObject("billType", billType);
				model.setViewName("WorkOrder/NMRBillSumadhuraCopy");
				// if this is NMR Details Print then forward to NMRDetailsPrint
				// page to Print
				if (printName.equals("NMR Details")) {
					model.addObject("billType", billType);
					model.setViewName("WorkOrder/NMRDetailsPrint");
				}

				return model;
			}
			//loading all the attachments that are related to the bill
			loadWOBillImgAndPdfFiles(billNo, siteId, model, request, workOrderNo);
			return model;
		}
		// loadWOBillImgAndPdfFiles(billNo, siteId, model, request,
		// workOrderNo);
		if (status.equals("true")) {
			if (billType.equals("RA")) {
				model.addObject("raPage", true);
			} else {
				model.addObject("raPage", false);
			}
			model.addObject("billType", billType);
			loadWOBillImgAndPdfFiles(billNo, siteId, model, request, workOrderNo);
			model.setViewName("WorkOrder/ViewCompletedBillDetails");
			return model;
		} else if (status.equals("false")) {
			// send request to print contractor copy
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/ContractorCopy");
		}
		model.addObject("billType", billType);
		return model;
	}
/**
 * @description this controller is for showing completed RA/ADV bill contractor print page
*/
	@RequestMapping(value = "/contractorCopy.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String contractorCopy(Model model, HttpServletRequest request, HttpSession session) {
		//String status = request.getParameter("status") == null ? "false" : request.getParameter("status");
		String BillNo = request.getParameter("BillNo") == null ? "" : request.getParameter("BillNo").trim();
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType").trim();
		String siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id").trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
	return "redirect:/showWOCompltedBillsDetails.spring?BillNo=" + BillNo + "&WorkOrderNo=" + workOrderNo
				+ "&billType=" + billType + "&site_id=" + siteId + "&status=false";
	}

	/**
	 * @description this controller is for showing completed NMR bill contractor print page
	*/
	@RequestMapping(value = "/nmrContractorCopy.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String nmrContractorCopy(Model model, HttpServletRequest request, HttpSession session) {
		//String status = request.getParameter("status") == null ? "false" : request.getParameter("status");
		String BillNo = request.getParameter("BillNo") == null ? "" : request.getParameter("BillNo").trim();
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType").trim();
		String siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id").trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
		String nameOfThePrintCopy = request.getParameter("printName") == null ? "" : request.getParameter("printName");
		return "redirect:/showWOCompltedBillsDetails.spring?BillNo=" + BillNo + "&WorkOrderNo=" + workOrderNo
				+ "&billType=" + billType + "&site_id=" + siteId + "&status=false&printName=" + nameOfThePrintCopy;

	}

/**
 * @description this controller for pending RA/ADV bill contractor copy page
 */
	@RequestMapping(value = "/contractorStatusCopy.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String contractorStatusCopy(Model model, HttpServletRequest request, HttpSession session) {
//		String status = request.getParameter("status") == null ? "false" : request.getParameter("status");
		String BillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType").trim();
		String workOrderNo = request.getParameter("WorkOrderNo") == null ? "" : request.getParameter("WorkOrderNo");
		String siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
		//redirecting to showWOBillsForContracorCopy controller
		return "redirect:/showWOBillsForContracorCopy.spring?tmpBillNo=" + BillNo + "&WorkOrderNo=" + workOrderNo
				+ "&site_id=" + siteId + "&billType=" + billType + "&status=true";
	}
	/**
	 * @description this controller for pending NMR bill contractor copy page
	 */
	@RequestMapping(value = "/nmrBillContractorCopy.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String nmrBillBontractorCopy(Model model, HttpServletRequest request, HttpSession session) {
		log.info("ContractorBillGenerateController.nmrBillBontractorCopy()");
	//	String status = request.getParameter("status") == null ? "false" : request.getParameter("status");
		String BillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType").trim();
		String workOrderNo = request.getParameter("WorkOrderNo") == null ? "" : request.getParameter("WorkOrderNo");
		String siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
		String nameOfThePrintCopy = request.getParameter("printName") == null ? "" : request.getParameter("printName");
		//redirecting this request to showWOBillsForContracorCopy 
		return "redirect:/showWOBillsForContracorCopy.spring?tmpBillNo=" + BillNo + "&WorkOrderNo=" + workOrderNo
				+ "&site_id=" + siteId + "&billType=" + billType + "&status=true&printName=" + nameOfThePrintCopy;
	}

	
	@RequestMapping(value = "/showWOBillsForContracorCopy.spring", method = RequestMethod.GET)
	public ModelAndView showWOBillsForContracorCopy(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		String siteId = "";
		String userId = "";
		//String response = "";
		String tmpBillNo = "";
		String billType = "";
		String status = "";
		String printName = "";
		try {
			// siteId = session.getAttribute("SiteId") == null ? "" :
			// session.getAttribute("SiteId").toString();
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			status = request.getParameter("status") == null ? "false" : request.getParameter("status");
			tmpBillNo = request.getParameter("tmpBillNo") == null ? "" : request.getParameter("tmpBillNo");
			billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
			siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
			printName = request.getParameter("printName") == null ? "" : request.getParameter("printName");
			String[] data = { tmpBillNo.trim(), userId, siteId, status };
			//checking is this processing bill number is valid or not
			boolean isValid = workBillControllerService.isValidTempBillNo(data);
			// if this bill number is not valid then show the error message
			if (!isValid) {
				List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId, siteId);
				model.addObject("workOrderList", workOrderBeans);
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			//loading the main detials of the bill like work order number,amount ,work order date etc....
			List<ContractorQSBillBean> list = workBillControllerService .getPendingWorkOrderContractorBillDetailById(tmpBillNo, userId, billType, status);
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			qsBillBean.setSiteId(siteId);
			//loadSiteAddress is used to load the site address for work order print page
			List<String> loadSiteAddress = workBillControllerService.loadSiteAddress(qsBillBean);
			//loading all the deduction of RA bill
			List<Map<String, Object>> deductionList = workBillControllerService.getRaAdvDeductionDetails(tmpBillNo, userId, status);
			//if we got list size empty then return the error message
			if (list.size() == 0) {
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			String woLevelPurpose = "";
			ContractorQSBillBean bean = list.get(0);

			if (status.equals("true")) {
				List<Map<String, Object>> listOfVerifiedEmpNames=workBillControllerService.getTempBillVerifiedEmpNames(bean);
				//adding all the list object's in model object  
				model.addObject("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
			}
			
			list.get(0).setRemarks(woLevelPurpose);
			//loadBillChangedDetails method will load all the changed details of the bills
			List<Map<String, Object>> changedListDtls = workBillControllerService.loadBillChangedDetails(tmpBillNo, siteId, userId);
			model.addObject("woLevelPurpose", bean.getRemarks());
			WorkOrderBean workOrderBean = new WorkOrderBean();
			workOrderBean.setSiteId(list.get(0).getSiteId());
			// loading the next level approver using this method with the help
			// of bean object and user_id
			workOrderBean = workBillControllerService.getWorkOrderFromAndToDetails(userId, workOrderBean);
			if (workOrderBean.getApproverEmpId() == null||workOrderBean.getApproverEmpId().equals("-")) {
				workOrderBean.setApproverEmpId("END");
			}
			//adding all the necessary data in model object so we can use them in view page
			model.addObject("billBean", bean);
			model.addObject("printName", printName);
			model.addObject("deductionList", deductionList);
			model.addObject("changedListDtls", changedListDtls);
			model.addObject("SiteAddress", loadSiteAddress);
			model.addObject("approvePage", true);
			if (status.equals("true")) {
				model.addObject("nextLevelApprovedId", bean.getPendingEmpId());
			} else {
				model.addObject("nextLevelApprovedId", workOrderBean.getApproverEmpId());
			}
			model.addObject("SiteName", session.getAttribute("SiteName"));
			model.addObject("SiteId", session.getAttribute("SiteId"));
			//model.addObject("indentReceiveModelForm", new IndentReceiveBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//checking the bill type 
		//based on the bill type we are changing the view name
		if (billType.equals("NMR")) {
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/NMRBillSumadhuraCopy");
			
			if (printName.equals("NMR Details")) {
				model.addObject("billType", billType);
				model.setViewName("WorkOrder/NMRDetailsPrint");
			}
			return model;
		}
		//checking the status if the status is true then check the bill typr
		if (status.equals("true")) {
			//check bill type and add the raPage in model that saying is this RA page or ADV page
			if (billType.equals("RA")) {
				model.addObject("raPage", true);
			} else {
				model.addObject("raPage", false);
			}
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/ContractorCopyForStatus");
			return model;
		}
		model.addObject("billType", billType);
		return model;
	}

	/**
	 * @description this controller used for updating the bill's
	 * means upload the attchment's
	 */
	@RequestMapping(value = "/updateContractorBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView updateContractorBill(HttpServletRequest request, HttpSession session,
			@RequestParam(value="file",required = false) MultipartFile[] files) {
		ModelAndView model = new ModelAndView();
		//String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		/*if(request.getMethod().equals("GET")){log.info("Already Updated");}*/	
		
		String site_id = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo");
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
		String nextLevelApproverEmpId = request.getParameter("nextLevelApprovelEmpID") == null ? "" : request.getParameter("nextLevelApprovelEmpID");
	
		String permanentBillNo=request.getParameter("permanentBillNo") == null ? "" : request.getParameter("permanentBillNo");
	
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&permanentBillNo.length()==0&&tempBillNo.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			model.addObject("urlForActivateSubModule",session.getAttribute("subModuleUrlForUpdateBill"));
			return model;
		}
		
		
		
		String printBillNo = tempBillNo;
		String printWONo = workOrderNo;
		//this condition is for permanent update
		if (nextLevelApproverEmpId.equals("END")) {
			//this url is for highlighting sub module
			model.addObject("urlForActivateSubModule", "updateContractorBills.spring");
			session.setAttribute("subModuleUrlForUpdateBill", "updateContractorBills.spring");
			tempBillNo = request.getParameter("permanentBillNo") == null ? "" : request.getParameter("permanentBillNo");
			printBillNo = tempBillNo;
			tempBillNo = tempBillNo.replace("/", "-");
		} else {
			//this url is for highlighting sub module
			model.addObject("urlForActivateSubModule", "updateTempContractorBills.spring");
			session.setAttribute("subModuleUrlForUpdateBill", "updateTempContractorBills.spring");
			printBillNo = request.getParameter("permanentBillNo") == null ? "" : request.getParameter("permanentBillNo");
		}
		workOrderNo = workOrderNo.replace("/", "-");
		//loading path of the drive 
		String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
								: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();
		tempBillNo = tempBillNo.trim();
		//getting how many images of pdf already uploaded before this update temporary work order
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
		int pdfAlreadyPresent = Integer.parseInt(request.getParameter("pdfAlreadyPresent"));
		int imgIncre = imagesAlreadyPresent;
		int pdfIncre = pdfAlreadyPresent;
		for (int i = imagesAlreadyPresent; i < files.length; i++) {
			MultipartFile multipartFile = files[i];
			if (!multipartFile.isEmpty()) {
				try {
					//creating the path for storing images and pdf in hard disk
					File dir = new File(rootFilePath + site_id + "\\" + workOrderNo + "\\" + tempBillNo);
					if (!dir.exists())
						dir.mkdirs();
					String filePath = "";
				
					// checking the extension of file, and creating based on extension
					if (multipartFile.getOriginalFilename().endsWith(".pdf")) {
						filePath = dir.getAbsolutePath() + File.separator + tempBillNo + "_Part" + pdfIncre + ".pdf";
						pdfIncre++;
					} else {
						filePath = dir.getAbsolutePath() + File.separator + tempBillNo + "_Part" + imgIncre + ".jpg";
						imgIncre++;
					}

					File convFile = new File(filePath);
					convFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(convFile);
					fos.write(multipartFile.getBytes());
					fos.close();

					log.info("Image Uploaded "+filePath);

				} catch (Exception e) {
					e.printStackTrace();
					//adding error message in 
					model.addObject("message1", "Bill Updated Successfully. \n Bill no is " + printBillNo
							+ ",\n Work Order No " + printWONo + "\n But Image NOT Uploaded");
					
					//request.setAttribute("message1", "Bill Updateded Successfully. But Image NOT Uploaded");
				}
			}
		} // For Loop

		model.addObject("message","Bill Updated Successfully. Bill no is " + printBillNo + "\n Work Order No " + printWONo);

		model.setViewName("response");
		return model;
	}

	@RequestMapping(value = "/showWOBillstoUpdate.spring", method = RequestMethod.GET)
	public ModelAndView showWOBillstoUpdate(HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView();
		String siteId = "";
		String userId = "";
	//	String response = "";
		String tempBillNo = "";
		String billType = "";
		String status = "";
		String isBillUpdatePageRequest = "";
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			status = request.getParameter("status") == null ? "false" : request.getParameter("status");
			tempBillNo = request.getParameter("tmpBillNo") == null ? "" : request.getParameter("tmpBillNo");
			billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
		
			isBillUpdatePageRequest = request.getParameter("isBillUpdatePage") == null ? "false" : request.getParameter("isBillUpdatePage");
			String[] data = { tempBillNo.trim(), userId, siteId, status };
			//checking is this processing bill number is valid or not
			boolean isValid = workBillControllerService.isValidTempBillNo(data);
			//if this bill is not valid then redirect to approve contractor page with the error message
			if (!isValid){
				List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
				model.addObject("workOrderList", workOrderBeans);
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			//loading main details of the bill like bill number,bill date, work order numer, work order amount etc...
			List<ContractorQSBillBean> list = workBillControllerService .getPendingWorkOrderContractorBillDetailById(tempBillNo, userId, billType, status);
			//this method will load the the deduciton of the bill's
			List<Map<String, Object>> deductionList = workBillControllerService.getRaAdvDeductionDetails(tempBillNo, userId, status);
			if (list.size() == 0) {
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
		//	String woLevelPurpose = "";
			ContractorQSBillBean billBean = list.get(0);
			list.get(0).setRemarks(billBean.getRemarks());
			//load all the changed details of the bill's like qunatity, amout
			List<Map<String, Object>> changedListDtls = workBillControllerService.loadBillChangedDetails(tempBillNo,siteId, userId);
			//adding all the necessary data in model object for the view
			model.addObject("woLevelPurpose", billBean.getRemarks());
			WorkOrderBean workOrderBean = new WorkOrderBean();
			workOrderBean.setSiteId(list.get(0).getSiteId());
			model.addObject("billBean", billBean);
			model.addObject("deductionList", deductionList);
			model.addObject("changedListDtls", changedListDtls);
			model.addObject("approvePage", true);

			if (status.equals("true")) {
				model.addObject("nextLevelApprovedId", billBean.getPendingEmpId());
			} else {
				model.addObject("nextLevelApprovedId", workOrderBean.getApproverEmpId());
			}
			model.addObject("SiteName", session.getAttribute("SiteName"));
			model.addObject("SiteId", session.getAttribute("SiteId"));
			model.addObject("indentReceiveModelForm", new IndentReceiveBean());
			//loading all the attachments of the bill's like images, pdf's
			//using bill number, work order number and site_id 
			loadWOBillImgAndPdfFiles(tempBillNo, billBean.getSiteId(), model, request, billBean.getWorkOrderNo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//checking bill type based on that then adding with some necessary attribute's
		//raPage is the attribute added the calculation, means which code should be executed while loading the bill data 
		if (billType.equals("RA")) {
			model.addObject("raPage", true);
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/UpdateRaAdvBills");
		} else {
			model.setViewName("WorkOrder/UpdateRaAdvBills");
			model.addObject("raPage", false);
		}

		return model;
	}

	/**
	 * @description this controller is used to delete the work order bill's attahcments like image and pdf's
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteWOBillsImage.spring", method = RequestMethod.GET)
	public @ResponseBody boolean deleteWOPermanentImage(HttpServletRequest request) {
		String filePath = request.getParameter("imagePath") == null ? "" : request.getParameter("imagePath").replace("$", "/").trim();
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").replace("/", "-");
		String siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId").replace("/", "-");
		String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
		workOrderNo = workOrderNo.replace("/", "-");

		File file = new File(filePath);
		// getting the extension to delete the particular files
		String fileExtenstion = WorkOrderController.getFileExtension(file);

		boolean isFileDeleted = false;
		try {
			isFileDeleted = Files.deleteIfExists(Paths.get(filePath));
		} catch (NoSuchFileException e) {
			log.info("No such file/directory exists");
		} catch (DirectoryNotEmptyException e) {
			log.info("Directory is not empty.");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("Invalid permissions.");
		}

		isFileDeleted = file.delete();

		String billNo = tempBillNo.trim();
		if (billNo.contains("/")) {
			billNo = billNo.replace("/", "-");
		}
		String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
				: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();

		File source = null;
		File destination = null;
		//here we have to changed the file names 
		//if part0.jpg is deleted then we have to change the name of the part1.jpg as part0.jpg the same thing we have to do for next files
		//we have swap the names
		for (int i = 0; i < 8; i++) {
			source = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "/" + billNo + "_Part" + i+ "." + fileExtenstion);
			if (!source.exists()) {
				destination = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "/" + billNo + "_Part"+ (i + 1) + "." + fileExtenstion);
			//checking here if destination file is exist then only rename the file name
				if (destination.exists()) {
					if (destination.renameTo(source)) {
						log.info("File rename success \n" + destination + " \n" + source);
					} else {
						log.info("File rename failed");
					}
				}
			}/* else if (!source.exists() && i == 0) {
				destination = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "/" + billNo + "_Part"	+ (i + 1) + "." + fileExtenstion);
				if (destination.exists())
					if (destination.renameTo(source)) {
						log.info("File rename success \n" + destination + " \n" + source);
					} else {
						log.info("File rename failed");
					}
			}*/
		}
		return isFileDeleted;
	}
/**
 * @description this is the common method for loading all the attahcment's of the work order bill's
 * @param billNo used in path of the folder
 * @param siteId used in path of the folder
 * @param model used to set all the images model object so we can view this attacments in view page
 * @param request
 * @param workOrderNo used in path of the folder
 */
	public static void loadWOBillImgAndPdfFiles(String billNo, String siteId, ModelAndView model,
			HttpServletRequest request, String workOrderNo) {
		try {
			int imageCount = 0;
			int pdfCount = 0;
			DataInputStream dis = null;

			workOrderNo = workOrderNo.replace("/", "-");
			String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null
					? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();
			billNo = billNo.trim();
			if (billNo.contains("/")) {
				billNo = billNo.replace("/", "-");
			}
			
			
			int getLocalPort=request.getLocalPort();
			String strContextAndPort = "";
			String path = "";
			if(getLocalPort == 8080){ //Local
				strContextAndPort = UIProperties.validateParams.getProperty("WO_BILL_LOCAL_IP_PORT");

			}
			else if(getLocalPort == 8078){ //local machine
				strContextAndPort = UIProperties.validateParams.getProperty("WO_BILL_UAT_IP_PORT");

			}
			else if(getLocalPort == 8079){ //CUG
				strContextAndPort = UIProperties.validateParams.getProperty("WO_BILL_CUG_IP_PORT");

			}
			else if(getLocalPort == 80){ //LIVE
				strContextAndPort = UIProperties.validateParams.getProperty("WO_BILL_LIVE_IP_PORT");

			}

			
			//this loop used to get all the attahced files
			for (int i = 0; i < 8; i++) {
			
				File imageFile = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "\\" + billNo
						+ "_Part" + i + ".jpg");
				String pdfFileName = rootFilePath + siteId + "/" + workOrderNo + "/" + billNo + "/" + billNo
						+ "_Part" + i + ".pdf";

				File pdfFile = new File(pdfFileName);
				pdfFileName = pdfFile.toString();
				//if file pdf file is exists then convert the file into base64 and add in model object 
				if (pdfFile.exists()) {
					pdfCount++;
					try {
						String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "$");
						path= strContextAndPort + siteId + "/" + workOrderNo + "/" + billNo + "/" + billNo
								+ "_Part" + i + ".pdf";
						/*dis = new DataInputStream(new FileInputStream(pdfFile));
					
						byte[] barray = new byte[(int) pdfFile.length()];
						dis.readFully(barray);
						//converting pdf in base64 format
						//so we 
						byte[] encodeBase64 = Base64.encodeBase64(barray);
						String base64Encoded = new String(encodeBase64, "UTF-8");
						//adding pdf file in model object  so show this image in view page*/
						model.addObject("pdf" + i, path);

						//adding pdf file path, if user want's to delete the file
						model.addObject("PathdeletePdf" + i, pathForDeleteFile);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						
					}
				}
				
				//if file image file is exists then convert the file into base64 and add in model object
				if (imageFile.exists()) {
					log.info("f.getName() " + imageFile.getName());
					
					imageCount++;
					try {
						String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "$");
						path= strContextAndPort+ siteId + "/" + workOrderNo + "/" + billNo + "/" + billNo
								+ "_Part" + i + ".jpg";
						
						/*dis = new DataInputStream(new FileInputStream(imageFile));
					
						byte[] barray = new byte[(int) imageFile.length()];
						dis.readFully(barray);
						//converting image in base64 format
						byte[] encodeBase64 = Base64.encodeBase64(barray);
						String base64Encoded = new String(encodeBase64, "UTF-8");*/
						//adding converted image into model objet so we can access in view page
						model.addObject("image" + i, path);
						//adding the path of the file, may user wan't delete this file that purpose we are storing the file path
						
						model.addObject("delete" + i, pathForDeleteFile);
					} finally {
					
					}

				}
			}
			request.setAttribute("imagecount", imageCount);
			request.setAttribute("pdfcount", pdfCount);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
 * @description this controller used to load the temporary bill details using temporary bill number
 *  here temporary bill number is unique so there no need to add more condition's in SQL Query while selecting bill data
 *  so this controller used while showing the status of the temporary bill and also used in approve time to show all the details of the bills
 */
	@RequestMapping(value = "/showWOBillsDetails.spring", method = RequestMethod.GET)
	public ModelAndView showWOBillsDetails(HttpServletRequest request, HttpSession session) {
		ModelAndView model = new ModelAndView();
		String siteId = "";
		String userId = "";
		//String response = "";
		String tempBillNo = "";
		String billType = "";
		String status = "";
		String typeOfWork="";
		String isBillUpdatePage = "";
		String isCommonApproval="";
		String isSiteWiseStatusPage="";
		List<ContractorQSBillBean> workOrderBeans = null;
		try {
			//siteId = session.getAttribute("SiteId") == null ? "" :
			//session.getAttribute("SiteId").toString();
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			status = request.getParameter("status") == null ? "false" : request.getParameter("status");
			tempBillNo = request.getParameter("tmpBillNo") == null ? "" : request.getParameter("tmpBillNo");
			billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
			isBillUpdatePage = request.getParameter("isBillUpdatePage") == null ? "false": request.getParameter("isBillUpdatePage");
			siteId = request.getParameter("site_id") == null ? "" : request.getParameter("site_id");
			isCommonApproval=request.getParameter("isCommonApproval")==null?"":request.getParameter("isCommonApproval");
			isSiteWiseStatusPage=request.getParameter("isSiteWiseStatusPage")==null?"":request.getParameter("isSiteWiseStatusPage");
			if(isCommonApproval.equals("true")||isSiteWiseStatusPage.equals("true")){
				siteId = request.getParameter("dropdown_SiteId") == null ? siteId : request.getParameter("dropdown_SiteId");
			}
			tempBillNo = tempBillNo.trim();
			//adding all the necessary data in data[] object so we are passing only one argument in method
			String[] data = { tempBillNo, userId, siteId, status };
			//checking is this processing bill number is valid or not
			boolean isValid = workBillControllerService.isValidTempBillNo(data);
			//if the current processing bill number is not valid then return the error message to the view page
			if (!isValid) {
				//load again all the pending bill's
				//add workOrderBeans object in model object to view page
				model.addObject("workOrderList", workOrderBeans);
				//forwarding response to based on condition
				if(status.equals("false")){
					workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
					model.setViewName("WorkOrder/ApproveContractorBills");	
					model.addObject("urlForActivateSubModule", "approveContractorBills.spring");
				}else if(isCommonApproval.equals("true")){
					workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
					model.setViewName("WorkOrder/ApproveContractorBills");
					model.addObject("urlForActivateSubModule", "siteWiseApproveContractorBills.spring");
				}else if(isSiteWiseStatusPage.equals("true")){
					workOrderBeans = workBillControllerService.getPendingWorkOrderBils("showBillStatus",siteId);
					model.setViewName("WorkOrder/ViewRAStatus");
					model.addObject("urlForActivateSubModule", "siteWiseContractorBillStatus.spring");
				}else if(isBillUpdatePage.equals("true")){
					workOrderBeans = workBillControllerService.getPendingWorkOrderBils("showBillStatus",siteId);
					model.setViewName("WorkOrder/ViewRAStatus");
					model.addObject("urlForActivateSubModule", "updateTempContractorBills.spring");
				}else{
					model.setViewName("WorkOrder/ApproveContractorBills");
				}
				//adding error message
				//holding the key's and values so we will check from which request we got
				model.addObject("isCommonApproval", isCommonApproval);
				model.addObject("isSiteWiseStatusPage", isSiteWiseStatusPage);
				model.addObject("strSiteId", siteId);
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			ContractorQSBillBean qsBillBean = new ContractorQSBillBean();
			qsBillBean.setSiteId(siteId);
			//loading the main details of the bill like bill number, bill date, work order number, work order amount
			List<ContractorQSBillBean> list = workBillControllerService.getPendingWorkOrderContractorBillDetailById(tempBillNo, userId, billType, status);
			ContractorQSBillBean billBean = list.get(0);
			//loadSiteAddress is used to load the site address for work order print page
			List<String> loadSiteAddress = workBillControllerService.loadSiteAddress(qsBillBean);
			//loading all the deductions of the bill's like security, advance etc...
			List<Map<String, Object>> deductionList = workBillControllerService.getRaAdvDeductionDetails(tempBillNo, userId, status);
			//if the list size is zero ten return the error message
			if (list.size() == 0) {
				model.setViewName("WorkOrder/ApproveContractorBills");
				model.addObject("responseMessage1", "Invalid temp bill number.");
				return model;
			}
			typeOfWork=billBean.getTypeOfWork();
			billBean.setTempBillNo(tempBillNo);
 			billType=billBean.getPermanentBillNo().split("/")[0];
		 	//loading all the changed details of the bill
			List<Map<String, Object>> changedListDtls = workBillControllerService.loadBillChangedDetails(tempBillNo,siteId, userId);
			model.addObject("woLevelPurpose", billBean.getRemarks());
			WorkOrderBean workOrderBean = new WorkOrderBean();
			workOrderBean.setSiteId(list.get(0).getSiteId());
			workOrderBean.setTypeOfWork(typeOfWork);
			// loading the next level approver using this method with the help
			// of bean object and user_id
			workOrderBean = workBillControllerService.getWorkOrderFromAndToDetails(userId, workOrderBean);
			if (workOrderBean.getApproverEmpId() == null||workOrderBean.getApproverEmpId().equals("-")) {
				workOrderBean.setApproverEmpId("END");
			}
 
			List<Map<String, Object>> listOfVerifiedEmpNames=workBillControllerService.getTempBillVerifiedEmpNames(billBean);
			//adding all the list object's in model object  
			model.addObject("listOfVerifiedEmpNames", listOfVerifiedEmpNames);
 			
			//adding all the necessary details in model object to view page
			model.addObject("billBean", billBean);
			model.addObject("deductionList", deductionList);
			model.addObject("changedListDtls", changedListDtls);
			model.addObject("SiteAddress", loadSiteAddress);
			model.addObject("approvePage", true);
			model.addObject("isCommonApproval", isCommonApproval);
			model.addObject("isSiteWiseStatusPage", isSiteWiseStatusPage);
			if (status.equals("true")) {
				model.addObject("nextLevelApprovedId", billBean.getPendingEmpId());
			} else {
				model.addObject("nextLevelApprovedId", workOrderBean.getApproverEmpId());
			}
			model.addObject("SiteName", session.getAttribute("SiteName"));
			model.addObject("SiteId", session.getAttribute("SiteId"));
			model.addObject("indentReceiveModelForm", new IndentReceiveBean());
			//loading all the attachments of the bill
			loadWOBillImgAndPdfFiles(tempBillNo, billBean.getSiteId(), model, request, billBean.getWorkOrderNo());

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addObject("isBillUpdatePage", isBillUpdatePage);

		// if status is true then return the status page
		if (status.equals("true")) {
			//checking bill type and adding the attribute value based on the page type
			//based on the bill type forwarding the request to the it's respective page
			if (billType.equals("RA")) {
				model.addObject("raPage", true);
			} else {
				model.addObject("raPage", false);
			}
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/ShowRaAdvStatus");
			
			if (billType.equals("NMR")) {
				model.setViewName("WorkOrder/ApproveNMRBills");
				model.addObject("status", status);
			}
			return model;
		}
		model.addObject("billType", billType);
		//if bill type is RA then add attribute raPage true else false 
		//based on raPage attribute we are doing calculation's in front end
		if (billType.equals("RA")) {
			model.addObject("raPage", true);
			model.addObject("billType", billType);
			model.setViewName("WorkOrder/ApproveRABills");
		} else {
			model.setViewName("WorkOrder/ApproveAdvBills");
			model.addObject("raPage", false);
		}
		model.addObject("status", status);
		if (billType.equals("NMR")) {
			model.addObject("blocksMap", iis.loadBlockDetails(siteId));
			model.setViewName("WorkOrder/ApproveNMRBills");
		}
		return model;
	}

	/**
	 * @description this controller is used to reject the bill RA or ADV 
	 * while rejecting the bill user need to enter the comments, why this bill is rejecting
	 */
	@RequestMapping(value = "/rejectWorkOrderBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView rejectWorkOrderBill(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderBillGenerateController.rejectWorkOrderBill()");
		ModelAndView model = new ModelAndView();
		String response = "";
		String isCommonApproval="";
		String urlForActivateSubModule="";
		isCommonApproval= request.getParameter("isCommonApproval") == null ? "": request.getParameter("isCommonApproval").trim();
		String tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("\\$")[0];
		String changedRemarks = request.getParameter("remarks") == null ? "" : request.getParameter("remarks");
		
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&tempBillNo.length()==0&&changedRemarks.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			//model.addObject("urlForActivateSubModule",session.getAttribute("subModuleUrlForApproveBill"));
			return model;
		}
		
		try {
			if(isCommonApproval.equals("true")){
				urlForActivateSubModule="siteWiseApproveContractorBills.spring";
			}else{
				urlForActivateSubModule= "approveContractorBills.spring";
			}
			//this is for activating sub module
			model.addObject("urlForActivateSubModule", urlForActivateSubModule);
			session.setAttribute("subModuleUrlForApproveBill",urlForActivateSubModule);
			
			// reject RA Bill as well as Adv Bill
			//calling service method to reject the bill
			response = workBillControllerService.rejctAdvanceBill(request, session);
			//checking the response, based on the response message adding success message and rejection message in model object for the view page
			if (response.equals("success")) {
				response = "Work order bill rejected successfully.";
				model.addObject("message", response);
			} else {
				response = "Work order bill rejection failed.";
				model.addObject("message1", response);
			}
		} catch (Exception e) {
			model.addObject("message1", "Failed to reject bill.");
		}
		model.setViewName("response");
		return model;
	}

/**
 * @description this controller used for approving the RA bill
 * while approving RA bill if any modification happens so we need to maintain the log's of modification details in DB so the next level person can see the modification 
 * 
 */
	@RequestMapping(value = "/approveWoRABill.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView approveWoRABill(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderBillGenerateController.approveWorkOrderBill()");
		ModelAndView model = new ModelAndView();
		
		//this URL is for highlighting sub module
		String urlForActivateSubModule="";
		String siteId = "";
		String userId = "";
		String response = "";
		String isCommonApproval="";
		
		String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String raBillNo = request.getParameter("raBillNo") == null ? "" : request.getParameter("raBillNo");
		String contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");
		isCommonApproval= request.getParameter("isCommonApproval") == null ? "": request.getParameter("isCommonApproval").trim();
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&raBillNo.length()==0&&contractorId.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			//model.addObject("urlForActivateSubModule",session.getAttribute("subModuleUrlForApproveBill"));
			return model;
		}
		
		
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String tempBillNo = request.getParameter("tempBillNo") == null ? "": request.getParameter("tempBillNo").trim();
			// calling service method to move this temporary bill to the next level  
			response = workBillControllerService.approveWoRABill(request, session);
			model.addObject("responseMessage", response);
			if(isCommonApproval.equals("true")){
				urlForActivateSubModule="siteWiseApproveContractorBills.spring";
			}else{
				urlForActivateSubModule= "approveContractorBills.spring";
			}
			//this is for activating sub module
			model.addObject("urlForActivateSubModule", urlForActivateSubModule);
			//session.setAttribute("subModuleUrlForApproveBill",urlForActivateSubModule);
			
			//checking response, if success add the success message in model object else add error message in model object to view page 
			if (response.equals("success")) {
				 workOrderNo = request.getParameter("workOrderNo") == null ? "": request.getParameter("workOrderNo");
				String billNo = String.valueOf(request.getAttribute("BillNo"));
				String approverEmpId = request.getParameter("nextLevelApprovelEmpID");
				response = "Work order RA bill approved successfully. Temp bill no is " + tempBillNo + ",\n Bill no is "+ billNo;
				String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? "": UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH").toString();
				//if this is final level approval so we need to move temporary attachments into permanent table
				//by changing the file name 
				if (approverEmpId.equals("END")) {
					response = "Work order RA bill approved successfully. \n Bill no is "+ billNo+", Work Order No "+workOrderNo;
					//here replacing all the slash to hyphen, because folder name not accepting slash so we need to convert slash to hyphen
					workOrderNo = workOrderNo.replace("/", "-");
					billNo = billNo.replace("/", "-");
					//moving all the temporary bill attachments into the permanent bill using this common method
					doTempImageIntoPermanentImage(rootFilePath, siteId, workOrderNo, tempBillNo, billNo);
				}
				//adding success message to model object for view page
				model.addObject("message", response);
			} else {
				//adding error message to model object for view page
				response = "Work order RA bill approve failed.";
				model.addObject("message1", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "Work order RA bill approve failed.";
			if(e.getMessage().equals("check site account mappings")){
				response = "Work order site accountant mapping not found, please check mappings.";
			}
			model.addObject("message1", response);
		}

/*	try {
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
			model.addObject("workOrderList", workOrderBeans);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
*/
		model.setViewName("response");
		return model;
	}

	/**
	 * 
	 * @param rootFilePath this parameter holding the cloud drive path
	 * @param siteId used in folder name, there is a chance the work order number, bill number can be duplicate so we used site_id in folder
	 * so when we are selecting the attachments of the bill there we will not get any ambiguity problem
	 * @param workOrderNo used to folder name 
	 * @param tempBillNo will used when temporary bill is converting into permanent bill, 
	 * using this temporary bill we will load all the temporary attachmrents and we will rename this file names with the permanent file name 
	 * @param billNo is holding permanent bill number
	 */
	private static void doTempImageIntoPermanentImage(String rootFilePath, String siteId, String workOrderNo,
			String tempBillNo, String billNo) {

		boolean flag=false;
		File source = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + tempBillNo);
		File destination = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo);
		//if destination folder is not exists then create the folder
		if (!destination.exists()){
			//here permanent bill directory created, here we will store all the attachments of the bill's
			destination.mkdirs();
		}
		try {
			//Copying all the temporary attachment into the permanent folder which is just created 
			FileUtils.copyDirectory(source, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int imageCount = 0;
		int pdfCount = 0;
		try {
	
			imageCount = 0;
			for (int i = 0; i < 8; i++) {
				//here we are giving the dummy file name to the file object  
				File tempPdfFileName = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + tempBillNo + "\\"
						+ tempBillNo + "_Part" + i + ".pdf");

				File tempImageFileName = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + tempBillNo + "\\"
						+ tempBillNo + "_Part" + i + ".jpg");
				
				//if file is exists then rename temporary filename with the permanent file name  
				if (tempPdfFileName.exists()) {
					String pdffilePath = destination.getAbsolutePath() + File.separator + billNo + "_Part" + pdfCount + ".pdf";
					//counting the uploaded pdf 
					pdfCount++;
					File convFile = new File(pdffilePath);
					if (tempPdfFileName.renameTo(convFile)) {
						System.out.println("File rename success");
						/*pdfFile = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "\\"
								+ tempBillNo + "_Part" + i + ".pdf");*/
					} else {
						System.out.println("File rename failed");
					}
				}
				if (tempImageFileName.exists()) {
					String imagefilePath = destination.getAbsolutePath() + File.separator + billNo + "_Part" + imageCount+ ".jpg";
					imageCount++;
					File convFile = new File(imagefilePath);
					if (tempImageFileName.renameTo(convFile)) {
						System.out.println("File rename success");
						/*imageFile = new File(rootFilePath + siteId + "\\" + workOrderNo + "\\" + billNo + "\\"
								+ tempBillNo + "_Part" + i + ".jpg");*/
					} else {
						System.out.println("File rename failed");
					}
				}
				//deleting the temporary files
				flag = tempImageFileName.delete();
				flag = tempPdfFileName.delete();
			}

		} catch (Exception e) {
		e.printStackTrace();
		}
		//deleting the temporary folder
		flag = source.delete();

	}

	/**
	 * @description this controller used to approve advance bill
	 * 	if in bill any modification happens we are maintaining the log's in DB
	 *  
	 */
	@RequestMapping(value = "/approveWoAdvBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView approveWorkOrderBill(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderBillGenerateController.approveWorkOrderBill()");
		
		ModelAndView model = new ModelAndView();
		//this URL is for highlighting sub module
		
		
		String siteId = "";
		String userId = "";
		String response = "";
		String isCommonApproval="";
		
		isCommonApproval= request.getParameter("isCommonApproval") == null ? "": request.getParameter("isCommonApproval").trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "": request.getParameter("workOrderNo").trim();
		String permanentBillNo= request.getParameter("permanentBillNo") == null ? "": request.getParameter("permanentBillNo").trim();
		String tempBillNo = request.getParameter("tempBillNo") == null ? ""	: request.getParameter("tempBillNo").trim();
		
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&permanentBillNo.length()==0&&tempBillNo.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			//model.addObject("urlForActivateSubModule",session.getAttribute("subModuleUrlForApproveBill"));
			return model;
		}
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(model,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String tempBillNum = request.getParameter("tempBillNo") == null ? "": request.getParameter("tempBillNo").trim();
			if(isCommonApproval.equals("true")){
				model.addObject("urlForActivateSubModule", "siteWiseApproveContractorBills.spring");
				session.setAttribute("subModuleUrlForApproveBill","siteWiseApproveContractorBills.spring");
			}else{
				model.addObject("urlForActivateSubModule", "approveContractorBills.spring");
				session.setAttribute("subModuleUrlForApproveBill","approveContractorBills.spring");
			}
			response = workBillControllerService.approveWorkOrderBill(request, session);
			
			String nextLevelApprovelEmpID = request.getParameter("nextLevelApprovelEmpID");
			String billNo = "";
			workOrderNo = "";
			if (response.equals("success")) {
				 tempBillNo = String.valueOf(request.getAttribute("TempBillNo"));
				String permanentbillNO = String.valueOf(request.getAttribute("BillNo"));
				//if this is final approval then move the temporary bill attachments to permanent bill
				//and change the respose message 
				if (nextLevelApprovelEmpID.equals("END")) {
					 workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
					response = "Work order Advance bill approved successfully.\n Bill no is "
							+ request.getAttribute("BillNo") + ", Work Order No is " + workOrderNo;
					workOrderNo = workOrderNo.replace("/", "-");

					String rootFilePath = UIProperties.validateParams .getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
									: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") .toString();
					billNo = permanentbillNO;
					billNo = billNo.replace("/", "-");
					//moving all the temporary bill attachments into the permanent bill using this common method
					doTempImageIntoPermanentImage(rootFilePath, siteId, workOrderNo, tempBillNum, billNo);
				} else {
					response = "Work order Advance bill approved successfully. Temp bill no is " + tempBillNo
							+ " Bill No is " + permanentbillNO;
				}
				//adding success message to model object for view page
				model.addObject("message", response);
			} else {
				response = "Work order Advance bill approve failed";
				//adding error message to model object for view page
				model.addObject("message1", response);
			}
			/*List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
			model.addObject("workOrderList", workOrderBeans);*/
		} catch (Exception e) {
			e.printStackTrace();
			response = "Work order Advance bill generation failed";
			//adding error message to model object for view page
			if(e.getMessage().equals("check site account mappings")){
				response = "Work order site accountant mapping not found, please check mappings.";
			}
			
			model.addObject("message1", response);
		}
		model.setViewName("response");

		return model;
	}
	
	/**
	 * @description this controller loading all the work description for the bill using work order number 
	 * 
	 */
	@RequestMapping(value = "/loadWOWorkTypeDesc.spring", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> loadWOWorkTypeDesc(HttpServletRequest request, HttpSession session) {
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String workOrderNo = request.getParameter("workOrderNo") == null ? "" : request.getParameter("workOrderNo");
			String contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");
			String billType = request.getParameter("billType") == null ? "" : request.getParameter("billType");
			String billNo = request.getParameter("oldBillNo") == null ? "" : request.getParameter("oldBillNo");
			workOrderNo = workOrderNo.split("\\$")[0].trim();
			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setContractorId(contractorId);
			billBean.setBillNo(billNo);
			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setPaymentType(billType);
			Map<String, String> workDescType = workControllerService.loadWorkDsc(billBean);
			return workDescType;
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;
	}

	/**
	 * @description this controller will show the RA bill page from there we have to create the RA bill 
	 * in this bill we have deduction types
	 * while creating RA Bill we need to enter Contractor name 
	 * after entering contractor name we will get all the work order number
	 * there we have to select work order number after selecting work order number are  doing ajax call for loading permanent work order number bill number
	 * in payment area abstract we have to enter the values, for which are they are giving initiating the bill to contractor
	 */
	@RequestMapping(value = "/createrabill.spring", method = RequestMethod.GET)
	public ModelAndView createrabill(HttpServletRequest request, HttpSession session,Model mav) {
		ModelAndView model = new ModelAndView();
		try {
			String woTypeOfWork="";
			StringBuffer workOrderType=new StringBuffer("");
			woTypeOfWork=request.getParameter("woTypeOfWork")==null?"":request.getParameter("woTypeOfWork");
			String siteId = (String) CheckSessionValidation.validateUserSession(model,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below message
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addObject("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				model.setViewName("response");
				return model;
			}

			
			WorkOrderBean workOrderBean = new WorkOrderBean();
			workOrderBean.setSiteId(siteId);
			
			
			model.addObject("urlForActionTag", "createrabill.spring");
	   		model.addObject("pageName", "Certificate Of Payment");
	   		workOrderBean.setSiteId(siteId);
			if(woTypeOfWork.length()==0){
				List<Map<String, Object>> list=	getBillTypes(userId,workOrderBean,mav,workOrderType);
				model.setViewName("WorkOrder/SelectWOTypeOfWork");
				return model;
			}else{
				workOrderBean.setTypeOfWork(woTypeOfWork);	
			}
			
			// loading the next level approver using this method with the help
			// of bean object and user_id
			workOrderBean = workBillControllerService.getWorkOrderFromAndToDetails(userId, workOrderBean);
			model.addObject("raPage", true);
			if (workOrderBean.getApproverEmpId() == null||workOrderBean.getApproverEmpId().equals("-")) {
				model.addObject("response1", "You can't initiate RA bill");
			 	model.setViewName("response");
				return model;
			}
			
			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setSiteId(siteId);
			//adding all the necessary data like next level approval of this bill 
			model.addObject("WorkOrderBean", workOrderBean);
			// Map<String, String> workDescType = service.loadWorkDsc(billBean);
			model.addObject("SiteName", session.getAttribute("SiteName"));
			model.addObject("SiteId", session.getAttribute("SiteId"));
			//model.addObject("indentReceiveModelForm", new IndentReceiveBean());
			// model.addObject("workTypeDesc", workDescType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.setViewName("WorkOrder/CreateRaBill");
		return model;
	}

	/**
	 * @description this controller used to generate the RA bill after entering all the values in RA page
	 * 
	 */
	@RequestMapping(value = "/generateRAPaymentBill.spring",  method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView generateRAPaymentBill(HttpServletRequest request, HttpSession session) {
		log.info("WorkOrderBillGenerateController.createrabill()");
		ModelAndView model = new ModelAndView();
		String response = "";
		model.setViewName("response");
		
		//this url is for highlighting sub module
		model.addObject("urlForActivateSubModule", "createrabill.spring");
		
		String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String raBillNo = request.getParameter("raBillNo") == null ? "" : request.getParameter("raBillNo");
		String contractorId = request.getParameter("ContractorId") == null ? "" : request.getParameter("ContractorId");

		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&raBillNo.length()==0&&contractorId.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			return model;
		}
		
		
		try {
			//calling service layer to insert RA bill details 
			response = workBillControllerService.createWorkOrderRABill(request, session);
			if (response.equals("success")) {
				//Work order RA bill generated successfully.<br>Temp bill no is
				//1464<br>Bill No is RA/01
				response = "Work order RA bill generated successfully.\n Temp bill no is "
						   + request.getAttribute("TempRABillNo") + "\n" + ", Bill No is "
						   + request.getAttribute("RABillNo");
				model.addObject("message", response);
			} else {
				response = "Work order RA bill generation failed.";
				model.addObject("message1", response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = "Work order bill generation failed.";
			if(e.getMessage().equals("missing work order issue id")){
				response = "Work order number not found please try again later. or contact to support team.";
			}
			if(e.getMessage().equals("missing bill number")){
				response = "Work order bill number not found please try again later. or contact to support team.";
			}
			model.addObject("message1", response);
		
		}
		return model;
	}

	/**
	 * @description this controller used to update the temporary contractor bill's
	 * so here we have to upload attacments
	 */
	@RequestMapping(value = "/updateTempContractorBills.spring", method = RequestMethod.GET)
	public String updateTempContractorBills(Model model, HttpSession session,HttpServletRequest request) {
		String billType = "RA";
		try {
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below message
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}

			//userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils("showBillStatus", siteId);
			//adding all the pending bill's in model object which we have to update
			model.addAttribute("workOrderList", workOrderBeans);
			model.addAttribute("billType", billType);
			model.addAttribute("isTempBillUpdatePage", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewRAStatus";
	}

	/**
	 * @description this controller used to load all the pending bill's for status
	 * here data will be selected by using site_id not using by user_id
	 */
	@RequestMapping(value = "/viewContractorBillStatus.spring", method = RequestMethod.GET)
	public String viewMyRaBillStatus(Model model, HttpSession session,HttpServletRequest request) {
		String siteId = "";
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}

			//userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils("showBillStatus", siteId);
			//adding all the temporary bill's in model object for view page and adding all the necessary data 
			model.addAttribute("workOrderList", workOrderBeans);
		/*	model.addAttribute("isRAStatusPage", true);
			model.addAttribute("isAdvStatusPage", false);*/
			model.addAttribute("isSiteWiseStatusPage",false);
			model.addAttribute("isTempBillUpdatePage", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewRAStatus";
	}

	/**
	 * @description this controller used to load all the pending bill's for status site wise
	 * here data will be selected by using site_id not using by user_id
	 */
	@RequestMapping(value = "/siteWiseContractorBillStatus.spring", method = RequestMethod.GET)
	public String siteWiseContractorBillStatus(Model model, HttpSession session,HttpServletRequest request) {
		String siteId = "";
		String siteName = "";
		String strUserName = "";
		String enableWOSubModules = "";
		try {
			String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
			model.addAttribute("urlForActionTag", "siteWiseContractorBillStatus.spring");
	   		model.addAttribute("pageName", "Site Wise Bill Status");
			if(dropdown_SiteId.length()==0){
				return "SiteSelection";
			}
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}
			siteId=dropdown_SiteId;
			//userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils("showBillStatus", siteId);
			//adding all the temporary bill's in model object for view page and adding all the necessary data 
			model.addAttribute("workOrderList", workOrderBeans);
			model.addAttribute("strSiteId", siteId);
			model.addAttribute("isTempBillUpdatePage", false);
			model.addAttribute("isSiteWiseStatusPage", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewRAStatus";
	}
	
	/**
	 * @description this controller is used for update completed bill's
	 * here we are loading all the competed bill's by using site_id
	 */
	@RequestMapping(value = "/updateContractorBills.spring", method = RequestMethod.GET)
	public String updateContractorBills(@ModelAttribute("ContractorQSBillBean") ContractorQSBillBean billBean,Model model, HttpSession session) {
		String billType = "RA";
		String siteId = "";
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}
			if(billBean.getContractorName()==null||billBean.getContractorName().length()==0){
				billBean.setContractorId("");
			}

			if (StringUtils.isNotBlank(billBean.getFromDate()) || StringUtils.isNotBlank(billBean.getToDate())|| StringUtils.isNotBlank(billBean.getWorkOrderNo()) || StringUtils.isNotBlank(billBean.getContractorId()) ) {
			//calling service layer to load all the completed bill's
				billBean.setSiteId(siteId);
				List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getCompletdWorkOrderBills(siteId,	billBean);
				model.addAttribute("workOrderList", workOrderBeans);
				model.addAttribute("billType", billType);
				model.addAttribute("isCompletedRABill", true);
				model.addAttribute("isCompletedRABillUpdatePage", true);
				model.addAttribute("showTable", true);
			} else {
				model.addAttribute("ContractorQSBillBean", new ContractorQSBillBean());
				model.addAttribute("showTable", false);
				return "WorkOrder/ViewMyCompltedRAAdvBill";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewMyCompltedRAAdvBill";
	}

	/**
	 * @description this controller used to load all the completed bill's for view purpose
	 * this data will be loaded using site_id
	 */
	@RequestMapping(value = "/viewCompletedBills.spring", method = RequestMethod.GET)
	public String viewCmpltedRaBill(@ModelAttribute("ContractorQSBillBean") ContractorQSBillBean billBean,Model model, HttpSession session) {
		String billType = "RA";
		String siteId = "";
		//String userId = "";
		model.addAttribute("urlForFormTag", "viewCompletedBills.spring");
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			//userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			// here loading all the site id's available in properties
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}	
			if(billBean.getContractorName()==null||billBean.getContractorName().length()==0){
				billBean.setContractorId("");
			}
			if (StringUtils.isNotBlank(billBean.getFromDate()) || StringUtils.isNotBlank(billBean.getToDate())|| StringUtils.isNotBlank(billBean.getWorkOrderNo()) || StringUtils.isNotBlank(billBean.getContractorId()) ) {
				//loading all the completed bill's
				billBean.setSiteId(siteId);
				List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getCompletdWorkOrderBills(siteId, billBean);
				//adding all the bill's in model object
				model.addAttribute("workOrderList", workOrderBeans);
				model.addAttribute("billType", billType);
				model.addAttribute("isCompletedRABill", true);
				model.addAttribute("isCompletedRABillUpdatePage", false);
				model.addAttribute("showTable", true);
			}else{
				model.addAttribute("ContractorQSBillBean", new ContractorQSBillBean());
				model.addAttribute("showTable", false);
				return "WorkOrder/ViewMyCompltedRAAdvBill";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewMyCompltedRAAdvBill";
	}
	/**
	 * @description this controller used to load completed bill's using site id
	 * this controller is for common approval person
	 */
	@RequestMapping(value = "/viewSitewiseCompletedBills.spring", method = RequestMethod.GET)
	public String viewSitewiseCmpltedRaBill(Model model, HttpSession session, HttpServletRequest request) {
		String billType = "RA";
		String siteId = "";
		String dropdown_SiteId=request.getParameter("dropdown_SiteId")==null?"":request.getParameter("dropdown_SiteId");
		model.addAttribute("urlForActionTag", "viewSitewiseCompletedBills.spring");
   		model.addAttribute("pageName", "View Site Wise Bill");
		if(dropdown_SiteId.length()==0){
			return "SiteSelection";
		}
		try {
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			//userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null
					? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();

			List<String> enableWOSubModulesSiteList = Arrays.asList(enableWOSubModules.split(","));
			// checking here the current login site id, is matching with loaded
			// site id's from properties file or not if not then forward to
			// response page and show the below msg
			if (!enableWOSubModulesSiteList.contains(siteId)) {
				model.addAttribute("customMsg", "Hello " + strUserName + ", <br>&emsp;&emsp; As of now " + siteName
						+ " Site can not access Work Order & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}
			String strSiteId = request.getParameter("dropdown_SiteId");
			//loading all the completed bill's using site_id
			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getSitewiseCompletdWorkOrderBills("N/ACOMP", strSiteId, billType);
			//adding all the bill's in model object for view page
			model.addAttribute("workOrderList", workOrderBeans);
			//model.addAttribute("billType", billType);
			//model.addAttribute("isCompletedRABill", true);
			model.addAttribute("strSiteId", strSiteId);
			model.addAttribute("isCompletedRABillUpdatePage", false);
			model.addAttribute("isSiteWiseStatusPage", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "WorkOrder/ViewSitewiseCompltedRAAdvBill";
	}

	/**
	 * @description this controller will show the NMR bill create page
	 * this controller validating the current site_id if matching with properties variable site_id then only the user can see the NMR create page
	 * else user will get error message that you can't access this submodule
	 */
	@RequestMapping(value = "/nmrBills.spring", method = RequestMethod.GET)
	public String dummy(Model model, HttpSession session) {
	
		String siteId = "";
		String userId = "";
		siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		//String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();

		WorkOrderBean bean = new WorkOrderBean();
		bean.setSiteId(siteId);
		// loading the next level approver using this method with the help of
		// bean object and user_id
		bean = workBillControllerService.getWorkOrderFromAndToDetails(userId, bean);
		model.addAttribute("raPage", true);
		if (bean.getApproverEmpId() == null||bean.getApproverEmpId().equals("-")) {
			model.addAttribute("response1", "You can't initiate NMR bill.");
 			return "response";
		}
		model.addAttribute("blocksMap", iis.loadBlockDetails(siteId));
		model.addAttribute("nextLevelApproverEmpID", bean.getApproverEmpId());
		model.addAttribute("siteId", siteId);
		return "WorkOrder/CreateNMRBill";
	}

	/**
	 * @description this controller used to load NMR details using bill id 
	 */
	@RequestMapping(value = "/loadPermanentNMRBillDetailsData.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadPermanentNMRBillDetailsData(Model model, HttpSession session, HttpServletRequest request) {
		String strControctorId = request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId").toString();
		String strTypeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").toString();
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "0" : request.getParameter("workOrderNo").toString().trim();
		//String isApprovePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage").toString();
		String tempBillNo = request.getParameter("tempBillNo") == null ? "0" : request.getParameter("tempBillNo").toString();
		String billNo = request.getParameter("BillNo") == null ? "0" : request.getParameter("BillNo").toString();
		String requestFrom = request.getParameter("requestFrom") == null ? "" : request.getParameter("requestFrom").toString();
		try {
			workOrderNo = workOrderNo.split("\\$")[0];
			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setContractorId(strControctorId);
			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setCondition(requestFrom);
			billBean.setTypeOfWork(strTypeOfWork);
			billBean.setTempBillNo(tempBillNo);
			billBean.setBillNo(billNo);
			//calling service layer to return permanent NMR details
			List<Map<String, Object>> list = workBillControllerService.loadPermanentNMRBillData(billBean);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description this controller will return all the completed bill's of the work order,
	 * the data will be loaded using work order number
	 */
	@RequestMapping(value = "/loadCompletedNMRBillData.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadCompletedNMRBillData(Model model, HttpSession session,
			HttpServletRequest request) {
		String strControctorId = request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId").toString();
		String strTypeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").toString();
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "0" : request.getParameter("workOrderNo").toString().trim();
		String billNo = request.getParameter("billNo") == null ? "0" : request.getParameter("billNo").toString().trim();
		String isApprovePage= request.getParameter("isApprovePage") == null ? "true" : request.getParameter("isApprovePage").toString().trim();
		try {
			workOrderNo = workOrderNo.split("\\$")[0];
			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setContractorId(strControctorId);
			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setTypeOfWork(strTypeOfWork);
			billBean.setBillNo(billNo);
			billBean.setIsApprovePage(isApprovePage);
			//calling service layer to return the completed NMR bill's data
			List<Map<String, Object>> list = workBillControllerService.loadPermanentNMRBillDetailsData(billBean);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description this controller used for, load work order minor head, work description and also
	 * load the previous NMR bill values for NMR landing page
	 */
	@RequestMapping(value = "/loadNMRBillData.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadNMRBillData(Model model, HttpSession session,HttpServletRequest request) {
		String strControctorId = request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId").toString();
		String strTypeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").toString();
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "0" : request.getParameter("workOrderNo").toString().trim();
		String isApprovePage = request.getParameter("approvePage") == null ? "false" : request.getParameter("approvePage").toString();
		String tempBillNo = request.getParameter("tempBillNo") == null ? "0" : request.getParameter("tempBillNo").toString();
		String billNo = request.getParameter("BillNo") == null ? "0" : request.getParameter("BillNo").toString();
		String requestFrom = request.getParameter("requestFrom") == null ? "" : request.getParameter("requestFrom").toString();
		try {
			workOrderNo = workOrderNo.split("\\$")[0];
			ContractorQSBillBean billBean = new ContractorQSBillBean();
			billBean.setContractorId(strControctorId);
			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setCondition(requestFrom);
			billBean.setTypeOfWork(strTypeOfWork);
			if (isApprovePage.equals("true")) {
				billBean.setTempBillNo(tempBillNo);
				billBean.setBillNo(billNo);
				//this method for loading data in approval level using temp bill number
				List<Map<String, Object>> list = workBillControllerService.loadNMRBillDataForApproval(billBean);
				return list;
			} else {
				//this method is for new NMR bill, loading data for creating the bill from work order details table
				List<Map<String, Object>> list = workBillControllerService.loadNMRBillData(billBean);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*@RequestMapping(value = "/loadNMRBillDetails.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadNMRBillDetails(Model model, HttpSession session,
			HttpServletRequest request) {
		String minorHeadId = request.getParameter("minorHeadId") == null ? "" : request.getParameter("minorHeadId");
		String workDeskId = request.getParameter("workDeskId") == null ? "" : request.getParameter("workDeskId");
		String workDate = request.getParameter("workDate") == null ? "" : request.getParameter("workDate");
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "0"
				: request.getParameter("workOrderNo").toString().trim();
		String tempBillNo = request.getParameter("tempBillNo") == null ? "0" : request.getParameter("tempBillNo");

		try {
			workOrderNo = workOrderNo.split("\\$")[0];

			ContractorQSBillBean billBean = new ContractorQSBillBean();

			billBean.setWorkOrderNo(workOrderNo);
			billBean.setSiteId(siteId);
			billBean.setTempBillNo(tempBillNo);
			billBean.setMinorHeadId1(minorHeadId);
			billBean.setWorkDate1(workDate);
			billBean.setWorkDescId1(workDeskId);
			List<Map<String, Object>> list = workBillControllerService.loadNMRBillDeatilsByBillNo(billBean);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/**
	 * @description this controller is used for selecting work description name and id 
	 * for enabling and disabling the text box in NMR details popup
	 * while selecting the minor head names
	 */
	@RequestMapping(value = "/loadWorkDescNMRBills.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadWorkDescNMRBills(Model model, HttpSession session,
			HttpServletRequest request) {
		String minorHeadId = request.getParameter("minorHeadId") == null ? "" : request.getParameter("minorHeadId");
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim();
		String workOrderNo = request.getParameter("workOrderNo") == null ? "0"
				: request.getParameter("workOrderNo").toString().trim();
		String billNo= request.getParameter("billNo") == null ? "0" : request.getParameter("billNo").toString().trim();
		ContractorQSBillBean billBean = new ContractorQSBillBean();
		billBean.setWorkOrderNo(workOrderNo.split("\\$")[0]);
		billBean.setSiteId(siteId);
		billBean.setMinorHeadId1(minorHeadId.split("@@")[1]);
		billBean.setWoRowCode(minorHeadId.split("@@")[2]);
		billBean.setBillNo(billNo);
		//calling service layer to return all the work description name and id using minor head
		//the work description data will be loaded using minor head id
		List<Map<String, Object>> list = workBillControllerService.loadWorkDescNMRBills(billBean);
		return list;
	}

/**
 * @description this controller is used to insert all the data of NMR bill
 */
	@RequestMapping(value = "/generateNMRBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView generateNMRBill(Model model, HttpSession session, HttpServletRequest request) {
		log.info("ContractorBillGenerateController.generateNMRBill()");
		String response = "";
		//this URL is for highlighting sub module
		model.addAttribute("urlForActivateSubModule", "nmrBills.spring");
		ModelAndView modelAndView = new ModelAndView();
	
		String workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("\\$")[0];
		String permanentBillNo=request.getParameter("NMRBillNo")==null?"":request.getParameter("NMRBillNo");
		String ContractorId=request.getParameter("ContractorId")==null?"":request.getParameter("ContractorId");

		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&permanentBillNo.length()==0&&ContractorId.length()==0){
			model.addAttribute("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			modelAndView.setViewName("response");
			return modelAndView;
		}
 		try {
			//calling service layer to insert the NMR bill data
			 response = workBillControllerService.generateNMRBill(request, session);
			if (response.equals("success")) {
				response = "NMR  bill generated successfully. Temp Bill No is " + request.getAttribute("TempNMRBillNo")
						+ ",\n Bill no is " + request.getAttribute("NMRBillNo");
				modelAndView.addObject("message", response);
			} else {
				response = "NMR bill generation failed.";
				modelAndView.addObject("message1", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "NMR bill generation failed.";
			if(e.getMessage().equals("missing work order issue id")){
				response = "Work order number not found please try again later. or contact to support team.";
			}
			if(e.getMessage().equals("missing bill number")){
				response = "Work order bill number not found please try again later. or contact to support team.";
			}
			modelAndView.addObject("message1", response);
		
		}
		modelAndView.setViewName("response");
		return modelAndView;
	}
/**
 * @description this controller used to reject the NMR bill
 * while rejecting the NMR bill user need enter the reason for the rejecting the bill\
 */
	@RequestMapping(value = "/rejectNMRBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView rejectNMRBill(Model mav, HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		//this URL is for highlighting sub module
		String urlForActivateSubModule="";
		String tempBillNo = "";
		String approverEmpId ="";
		String workOrderNo = "";
		String isCommonApproval="";
		isCommonApproval= request.getParameter("isCommonApproval") == null ? "": request.getParameter("isCommonApproval").trim();
		tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
		approverEmpId = request.getParameter("nextLevelApproverEmpID")==null?"": request.getParameter("nextLevelApproverEmpID");
		workOrderNo = request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("\\$")[0];
		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&approverEmpId.length()==0&&tempBillNo.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			return model;
		}
 		String response = "";
		try {
			String	siteId = (String) CheckSessionValidation.validateUserSession(model,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
 
			if(isCommonApproval.equals("true")){
				urlForActivateSubModule="siteWiseApproveContractorBills.spring";
			}else{
				urlForActivateSubModule= "approveContractorBills.spring";
			}
			//this is for activating sub module
			model.addObject("urlForActivateSubModule", urlForActivateSubModule);
			//session.setAttribute("subModuleUrlForApproveBill",urlForActivateSubModule);
			
			response = workBillControllerService.rejectNMRBill(request, session);
			//checking response message and adding success message or error message to the model object for view
			if (response.equals("success")) {
				response = "NMR bill rejected successfully.";
				model.addObject("message", response);
			} else {
				response = "NMR bill rejection failed.";
				model.addObject("message1", response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = "NMR bill rejection failed.";
			model.addObject("message1", response);
		}
		model.setViewName("response");
		return model;
	}
	/**
	 * @description this controller is used for approving NMR Details, and move the bill into the next level
	 * if any modification happen's we need to maintain the log's in DB
	 * so we can show the modification's log's to next level approval
	 */
	@RequestMapping(value = "/approveNMRBill.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView approveNMRBill(Model mav, HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		//this URL is for highlighting sub module
		String urlForActivateSubModule="";
		String workOrderNo = "";
		String permanentBillNo="";
		String tempBillNo = "";
		String isCommonApproval="";
		
		isCommonApproval= request.getParameter("isCommonApproval") == null ? "": request.getParameter("isCommonApproval").trim();

		 workOrderNo = request.getParameter("WorkOrderNo") == null ? "" : request.getParameter("WorkOrderNo");
		 permanentBillNo=request.getParameter("NMRBillNo")==null?"":request.getParameter("NMRBillNo");
		 tempBillNo = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();

		//if this values are null means this is the error so we need to forward this request to again to request page with error message
		if(workOrderNo.length()==0&&permanentBillNo.length()==0&&tempBillNo.length()==0){
			model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			model.setViewName("response");
			return model;
		}
		
		String siteId = "";
		String userId = "";
		String response = "";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(model,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if(isCommonApproval.equals("true")){
				urlForActivateSubModule="siteWiseApproveContractorBills.spring";
			}else{
				urlForActivateSubModule= "approveContractorBills.spring";
			}
			//this is for activating sub module
			model.addObject("urlForActivateSubModule", urlForActivateSubModule);
			//session.setAttribute("subModuleUrlForApproveBill",urlForActivateSubModule);
			
			//String tempBillNum = request.getParameter("tempBillNo") == null ? "" : request.getParameter("tempBillNo").trim();
			 workOrderNo = request.getParameter("WorkOrderNo") == null ? "" : request.getParameter("WorkOrderNo");
			//calling service layer to approve NMR Bill and maintain modification log's if any
			response = workBillControllerService.approveNMRBill(request, session);

			String billNo = "";
			//checking the response type and adding the success message or error message for the view page
			if (response.equals("success")) {
				 tempBillNo = String.valueOf(request.getAttribute("TempNMRBillNo"));
				String approverEmpId = request.getParameter("nextLevelApproverEmpID");
				//if this is the final level approval modify the response message 
				if (approverEmpId.equals("END")) {
					response = "NMR bill Approved successfully. Bill No is " + request.getAttribute("NMRBillNo")
							+ ", Work Order No is " + workOrderNo;
				} else {
					response = "NMR bill Approved successfully. Temp bill no is " + tempBillNo + ",\n Bill No is "
							+ request.getAttribute("NMRBillNo");
				}
				//adding success message to the model object
				model.addObject("message", response);
				workOrderNo = workOrderNo.replace("/", "-");
				billNo = String.valueOf(request.getAttribute("NMRBillNo"));
				String rootFilePath = UIProperties.validateParams .getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") == null ? ""
								: UIProperties.validateParams.getProperty("UPDATE_WORKORDER_BILLS_IMAGE_PATH") .toString();
				//if this is the final level approval, we need to move temporary bill attachments into permanent bill
				if (approverEmpId.equals("END")) {
					billNo = billNo.replace("/", "-");
					//moving all the temporary bill attachments into the permanent bill using this common method
					doTempImageIntoPermanentImage(rootFilePath, siteId, workOrderNo, tempBillNo, billNo);
				}

			} else {
				response = "NMR bill Approve failed";
				//adding error message to model object for view page
				model.addObject("message1", response);
			}

			List<ContractorQSBillBean> workOrderBeans = workBillControllerService.getPendingWorkOrderBils(userId,siteId);
			model.addObject("workOrderList", workOrderBeans);
		} catch (Exception e) {
			e.printStackTrace();
			response = "NMR bill approve failed.";
			if(e.getMessage().equals("check site account mappings")){
				response = "Work order site accountant mapping not found, please check mappings.";
			}
			model.addObject("message1", response);
		}
		
		model.setViewName("response");
		return model;
	}
	@RequestMapping(value = "/woBillLedger.spring", method = RequestMethod.POST)
	public ModelAndView woBillLedger(Model mav, HttpSession session, HttpServletRequest request,@ModelAttribute("billBean") ContractorQSBillBean billBean) {
	//	log.info("ContractorBillGenerateController.woBillLedger() "+billBean);
		ModelAndView model = new ModelAndView();
		model.setViewName("WorkOrder/BillsLedgerPieceWork");
		return model;
	}
	
	@RequestMapping(value = "/woBillInvoice.spring", method = RequestMethod.POST)
	public ModelAndView woBillInvoice(HttpSession session, HttpServletRequest request,@ModelAttribute("billBean") ContractorQSBillBean billBean) {
	//	log.info("ContractorBillGenerateController.woBillLedger() "+billBean);
		//https://cleartax.in/s/tax-calculation-gst
		ModelAndView model = new ModelAndView();
		
		//billBean=workBillControllerService.getBillInvoiceGSTAmunt(billBean,model);
		
		model.addObject("billBean", billBean);
		model.setViewName("WorkOrder/BillInvoice");
		return model;
	}
	
}
