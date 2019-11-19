package com.sumadhura.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.transdao.WorkOrderDao;
import com.sumadhura.util.AESDecrypt;
import com.sumadhura.util.UIProperties;
import com.sumadhura.util.ValidateParams;

@Service(value = "workControllerService")
public class WorkOrderServiceImpl implements WorkOrderService {
	static Logger log = Logger.getLogger(WorkOrderServiceImpl.class);
	@Autowired
	@Qualifier("workControllerDao")
	WorkOrderDao workControllerDao;
	@Autowired
	PlatformTransactionManager transactionManager;

	/*@Override
	public List<Map<String, Object>> loadChildProduct(String prodId, String prodName) {
		List<Map<String, Object>> list = workControllerDao.loadChildProduct(prodId, prodName);
		return list;
	}*/
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the major head name and id for work order
	 * @param siteId is used to select major head data by site wise
	 * @param typeOFWork is used select data based on type of the work
	 */
	@Override
	public Map<String, String> loadQSProducts(String siteId, String typeOFWork) {
		Map<String, String> list = workControllerDao.loadQSProducts(siteId,typeOFWork);
		return list;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the minor head name and id for work order
	 * @param siteId is used to select major head data by site wise
	 * @param majorHeadId is used to select the it's sub products
	 * @param typeOFWork is used select data based on type of the work
	 */
	@Override
	public String loadWOSubProds(String majorHeadId, String siteId, String typeOfWork) {
		return workControllerDao.loadWOSubProds(majorHeadId,siteId,typeOfWork);
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the work description name and id for work order
	 * @param minorHeadId is used to select his sub products
	 * @param siteId is used to select the work description by site wise
	 * @param typeOfWork is used to select the work description based on type
	 */
	@Override
	public String loadWOChildProds(String minorHeadId, String siteId, String typeOfWork) {
		return workControllerDao.loadWOChildProds( minorHeadId,siteId,typeOfWork);
	}

	@Override
	public String loadWorkOrderMeasurements(String childProductId, String siteId, String typeOfWork) {
		return workControllerDao.loadWorkOrderMeasurements( childProductId,siteId,typeOfWork);
	}
	
	@Override
	public String loadScopeOfWork(WorkOrderBean bean) {
		return  workControllerDao.loadScopeOfWork(bean);
	}
	
	@Override
	public String loadScopeOfWorkAndBOQAmount(WorkOrderBean bean) {
		return  workControllerDao.loadScopeOfWorkAndBOQAmount(bean);

	}
	/**
	 * calling dao object to check this work order exits or not
	 */
	@Override
	public String checkWorkOrderNoExistsOrNot(String workOrderNo, String siteId) {
		return workControllerDao.checkWorkOrderNoExistsOrNot(workOrderNo,siteId);
	 }
	
	@Override
	public String checkIsDraftWorkOrderExistsOrNot(HttpServletRequest request, HttpSession session) {
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		String contractorId=request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
	    String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
	    return workControllerDao.checkIsDraftWorkOrderExistsOrNot(workOrderNo,contractorId,typeOfWork,siteId);
	}
	
	/**
	 * @throws Exception 
	 * @description by using this method validating the Material BOQ details
	 */
	@Override
	public Map<String, String> validateWOMaterialBOQDetails(HttpServletRequest request) throws Exception {
		String contractorId="";
		String workOrderNumber="";
		String siteId="";
		String materialData="";
		String condition="";
	
		List<WorkOrderBean> list=new ArrayList<WorkOrderBean>();
		
		siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		contractorId = request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
		workOrderNumber=request.getParameter("workOrderNumber")==null?"":request.getParameter("workOrderNumber");
		materialData=request.getParameter("data")==null?"":request.getParameter("data");
		condition=request.getParameter("condition")==null?"":request.getParameter("condition");
		JSONArray jsonArr = new JSONArray(materialData);
		for (int index = 0; index < jsonArr.length(); index++)
        {
            JSONObject jsonObj = jsonArr.getJSONObject(index);
            log.info(jsonObj);
            ObjectMapper mapper = new ObjectMapper();
            WorkOrderBean bean = mapper.readValue(jsonObj.toString(), WorkOrderBean.class);
            list.add(bean);
        }
        return workControllerDao.validateWOMaterialBOQDetails(list,siteId,contractorId,workOrderNumber,"",condition);
	}
	
	
	/**
	 * @return 
	 * @description this method will return the material details like issued quantity and availble quantity
	 */
	@Override
	public  List<Map<String, Object>>  getMaterialBOQProductDetails(WorkOrderBean workOrderBean) {
		
		 return workControllerDao.getMaterialBOQProductDetails(workOrderBean);
	}
	@Override
	public List<Map<String, Object>> getTempMaterialBOQProductDetails(WorkOrderBean workOrderBean) {
		 return workControllerDao.getTempMaterialBOQProductDetails(workOrderBean);
	}
	@Override
	public List<Map<String, Object>> getTempWorkDescMaterialBOQProductDetails(HttpServletRequest request, HttpSession session) {
		 String workDescId=request.getParameter("workDescId")==null?"":request.getParameter("workDescId");
		 String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
		 String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
		 WorkOrderBean workOrderBean=new WorkOrderBean();
		 workOrderBean.setSiteId(siteId);
		 workOrderBean.setWO_Desc1(workDescId);
		 workOrderBean.setTypeOfWork(typeOfWork);
		 return workControllerDao.getTempWorkDescMaterialBOQProductDetails(workOrderBean);
	}
	
	@Override
	public List<Map<String, Object>> getTempWorkAreaWiseMaterialDetails(HttpServletRequest request,
			HttpSession session) {
		 WorkOrderBean workOrderBean=new WorkOrderBean();
		 String workDescId=request.getParameter("workDescId")==null?"":request.getParameter("workDescId");
		 String materialGroupId=request.getParameter("materialGroupId")==null?"":request.getParameter("materialGroupId");
		 String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
		 String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
		 
		 workOrderBean.setSiteId(siteId);
		 workOrderBean.setWO_Desc1(workDescId);
		 workOrderBean.setTypeOfWork(typeOfWork);
		 workOrderBean.setMaterialGroupId(materialGroupId);
		return workControllerDao.getTempWorkAreaWiseMaterialDetails(workOrderBean);
	}
	
	@Override
	public List<String> getContractorInfo(String contractorName, String loadVendorData, String fromIndentReturns) {
		boolean flag=Boolean.valueOf(loadVendorData);
		return workControllerDao.getContractorInfo(contractorName,flag,fromIndentReturns);
	}

	@Override
	public List<Map<String, Object>> loadWOAreaMapping(String siteId, String mesuremnt, String unitsOfMeasurementId, String typeOfWork) {
		return workControllerDao.loadWOAreaMapping(siteId,mesuremnt,unitsOfMeasurementId,typeOfWork);
		
	}
	@Override
	public List<Map<String, Object>> loadWOAreaMappingForRevise(WorkOrderBean bean) {

		return workControllerDao.loadWOAreaMappingForRevise(bean);
	}

	@Override
	public int getQS_WO_Temp_Issue_Dtls() {
		
		return workControllerDao.getQS_WO_Temp_Issue_Dtls();
	}

	/**
	 * calling dao to get the next level approver of the current employee
	 */
	@Override
	public WorkOrderBean getWorkOrderFromAndToDetails(String user_id1, WorkOrderBean workOrderBean) {
		return workControllerDao.getWorkOrderFromAndToDetails(user_id1, workOrderBean);
	}
	
	@Override
	public List<Map<String, Object>> userAbleToCreateWOTypes(String user_id, WorkOrderBean bean) {
		List<Map<String, Object>> list= workControllerDao.userAbleToCreateWOTypes(user_id,bean);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getNewRevisedWorkOrderName(String user_id, WorkOrderBean bean) {
	 	return workControllerDao.getNewRevisedWorkOrderName(user_id, bean);
	}
	
	@Override
	public boolean checkIsAnyBillisInActiveMode(WorkOrderBean bean) {
		return workControllerDao.checkIsAnyBillisInActiveMode( bean);
	}
	
	/**
	 * @description this method will call the dao and load the max temporary work order number site wise
	 * (non-Javadoc)
	 * @see com.sumadhura.service.WorkOrderService#getMaxQSWorkOrderNoSiteWise(java.lang.String)
	 */
	
	@Override
	public int getMaxQSWorkOrderNoSiteWise(String siteId) {
		return workControllerDao.getMaxQSWorkOrderNo(siteId);
	}
	/**
	 * @description this method is used for getting the last first two characters of any string
	 * @param str
	 * @return
	 */
	public static String getfirstTwoChar(String str) {
	    return str.length() < 2 ? str : str.substring(0, 3);
	}
	/**
	 * this method is for generating permanent work order
	 */
	@Override
	public String getWorkOrderNoSiteWise(String siteId, String siteName, String woTypeOfWork) {
		String strSiteName="";
		StringBuffer initials = new StringBuffer ();
		
		if(siteName!=null)
		if(siteName.contains(" ")){
			//if site name contains space then retrieve the first char from this string
			for (String s : siteName.split(" ")) {
			  initials.append(s.charAt(0));
			}
		}else{
			 initials.append(getfirstTwoChar(siteName));
		}
		
		return workControllerDao.getWorkOrderNoSiteWise(siteId,initials.toString(),woTypeOfWork);
	}
	 /**
	  * this method is used store NMR work order details 
	  */
	@Override
	public String doNMRWorkOrderEntry(WorkOrderBean workOrderBean, HttpServletRequest request, Model model,
			String siteId,String user_id, HttpSession session) {
		String response ="";
		String siteName = "";
		String strUserName =  "";
		List<WorkOrderBean> orderBeans = new ArrayList<WorkOrderBean>();
		String siteWiseTempWorkOrderNo = workOrderBean.getSiteWiseWONO();
		List<String> storeDuplicateworkOrderDetailsList=new ArrayList<String>();
		boolean isSendMail = true;
		String workOrderCreateEmpId = user_id;
		List<WorkOrderBean> showWorkOrderBean=new ArrayList<WorkOrderBean>();
		String workOrderTMPIssurPK = "";
		String woROWCodeFormat="";
		String actionType="";
		String workOrderStatus="";
		String isWorkOrderDataChanged="";
		String optionalCCmails="";
		List<Number> reponse1 = null;
		//getting how many rows are created while creating of work order
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null?"":request.getParameter("numbeOfRowsToBeProcessed");
		WriteTrHistory.write("Tr_Opened in NMR WO, ");
		try {
			strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
			siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String actualWorkOrderNumber= request.getParameter("actualWorkOrderNumber" )==null?"":request.getParameter("actualWorkOrderNumber");
			String TotalAmountOfWorkOrder=request.getParameter("TotalAmountOfWorkOrder")==null?"0":request.getParameter("TotalAmountOfWorkOrder");
			 
			optionalCCmails=request.getParameter("optionalCCmails");
			workOrderBean.setSiteName(siteName);
			workOrderBean.setTypeOfWork("NMR");
			workOrderBean.setActualWorkOrderNo(actualWorkOrderNumber);
			workOrderBean.setTotalAmount1(TotalAmountOfWorkOrder);
			String boqNo= request.getParameter("boqNo")== null ? "" :request.getParameter("boqNo") ;
			workOrderStatus=workOrderBean.getWorkOrderStatus()==null?"":workOrderBean.getWorkOrderStatus();
			workOrderBean.setWorkOrderStatus(workOrderStatus);
			workOrderBean.setBoqRecordType("LABOR");
			workOrderBean.setBoqNo(boqNo);
			isWorkOrderDataChanged= request.getParameter("isWorkOrderDataChanged" )==null?"":request.getParameter("isWorkOrderDataChanged");

			 synchronized (this) {
			//inserting NMR details like work order name, date,temp work order number,permanent work order number
				reponse1 = workControllerDao.insertWorkOrderDetail(workOrderBean, workOrderCreateEmpId);					
			 }
			 
				log.info(workOrderBean.getSiteName()+"\t"+
						workOrderBean.getContractorName() +"\t"+
			        	workOrderBean.getWorkOrderDate()+"\t"+
			            workOrderBean.getSiteWiseWONO()+"\t"+
			            workOrderBean.getWorkOrderNo()+"\t"+
			            workOrderBean.getWorkOrderName()+"\t"+
			            workOrderBean.getLaborWoAmount()+"\t"+
			            workOrderBean.getTypeOfWork()+"\t");
		} catch (Exception e) {
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			if(e.getMessage()!=null&&e.getMessage().equals("Work Order Number already exists")){
				response ="Work Order Number already exists";	
			}
			if(e.getMessage()!=null&&e.getMessage().equals("not changed work order")){
				response="not changed work order";
			}
			e.printStackTrace();
			isSendMail = false;
			return response;
		}
		
		
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in NMR WO, ");
	
		String[] termsAndConditions=request.getParameterValues("termsAndCOnditions");
		
		try {
			String workOrderResult = "";
			try {
				workOrderResult = reponse1.get(0).toString();
				//this variable is holding the QS_WORKORDER_TEMP_ISSUE table primary key
				//so we are storing this primary key in QS_WORKORDER_TEMP_ISSUE_DTLS table
				workOrderTMPIssurPK = reponse1.get(1).toString();
				workOrderBean.setQS_Temp_Issue_Id(workOrderTMPIssurPK);
			} catch (Exception e) {
				e.printStackTrace();
			}
	
		 
			String[] strWONoPart = workOrderBean.getWorkOrderNo().split("/");
			if (strWONoPart[strWONoPart.length - 1].contains("R")) {
				woROWCodeFormat = strWONoPart[strWONoPart.length - 2];
			} else {
				woROWCodeFormat = strWONoPart[strWONoPart.length - 1];
			}
			if (workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")) {
				actionType = "DF";
			} else {
				actionType = "C";
			}
			//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "C" means Create 
			int response2 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, actionType);
			if (workOrderStatus.equals("ModifyWorkOrder")) {
				//termsAndConditions=request.getParameterValues("changedTC");
				try {
					//inserting another message if this is work order modify operation
					 //cloning the object so main object should not get any affect so we can use it with actual values
					WorkOrderBean orderBean = (WorkOrderBean) workOrderBean.clone();
					orderBean.setPurpose("Temporary work order number " + siteWiseTempWorkOrderNo + " changed to " + workOrderBean.getSiteWiseWONO());
					response2 = workControllerDao.inserWorkOrderCreationDetail(orderBean, "M");
				} catch (CloneNotSupportedException e1) {
					log.info(e1.getMessage());
				}
			}
			
			if (termsAndConditions != null) {
				// inserting terms and conditions
				response2 = workControllerDao.insertTermsAndConditions(workOrderBean, workOrderTMPIssurPK,termsAndConditions);
			}
			List<Map<String, String>> termasAndCon = new ArrayList<Map<String, String>>();
			if (termsAndConditions != null)
				for (String string : termsAndConditions) {
					Map<String, String> map = new HashMap<String, String>();
					if (string.length() != 0) {
						map.put("TERMS_CONDITION_DESC", string);
						termasAndCon.add(map);
					}
				}
			//adding terms and condition in model object so we can print this terms and condition in work order print page
			model.addAttribute("listTermsAndCondition", termasAndCon);
			String numOfRec[] = null;
			if ((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			//if this is modify Work Order then take row num from displayed rows request parameter	
			if (workOrderStatus != null && workOrderStatus.equals("ModifyWorkOrder")) {
				numOfRec = request.getParameterValues("dispplayedRows");
			}
			//this is the number of work created in work order
			if (numOfRec != null && numOfRec.length > 0) {
				for (String num : numOfRec) {
					WorkOrderBean printWO = new WorkOrderBean();
					num = num.trim();
					//this variable used for Modify Work Order because we are again inserting records
					String DeleteColVal = request.getParameter("isDelete" + num)==null?"":request.getParameter("isDelete" + num);
					boolean isThisNewRow=request.getParameter("isNewRowAdded" + num)==null?false:Boolean.valueOf(request.getParameter("isNewRowAdded" + num));
					
					String WO_MajorHead1= request.getParameter("WO_MajorHead" + num)==null?"":request.getParameter("WO_MajorHead" + num);
					String WO_MinorHead1 =request.getParameter("WO_MinorHead" + num)==null?"":request.getParameter("WO_MinorHead" + num);
					String woDesc= request.getParameter("WO_Desc" + num)==null?"":request.getParameter("WO_Desc" + num);
					String UnitsOfMeasurement1=request.getParameter("UnitsOfMeasurement" + num)==null?"":request.getParameter("UnitsOfMeasurement" + num);
					String Quantity = request.getParameter("Quantity" + num);
					String TotalAmount = request.getParameter("TotalAmount" + num);
					String note = request.getParameter("comments" + num)==null?"":request.getParameter("comments" + num).trim();
					String woManualDesc = request.getParameter("woManualDesc" + num)==null?"": request.getParameter("woManualDesc" + num);
					//String acceptedRate = request.getParameter("AcceptedRate" + num)==null?"": request.getParameter("AcceptedRate" + num);
					try {
						//Spiting the variable bcoz in variable major head id and name is available so we need only name
						//for printing purpose
						printWO.setWO_MajorHead1(WO_MajorHead1.split("\\$")[1].replace("#","\""));
						printWO.setWO_MinorHead1(WO_MinorHead1.split("\\$")[1]);
						printWO.setWO_Desc1(woDesc.split("\\$")[1]);
						printWO.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[1]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					printWO.setTotalAmount1(TotalAmount);
					printWO.setQuantity(Quantity);
					Double acceptedRate1=0.0;
					printWO.setAcceptedRate1(String.valueOf(acceptedRate1));
					
					WorkOrderBean tempIssueDetailsData=new WorkOrderBean();
					//Spiting the variable bcoz in variable major head id and name is available so we need only id 
					//[0] index holding the id of the major head,minor head and work description
					tempIssueDetailsData.setWO_MajorHead1(WO_MajorHead1.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_MinorHead1(WO_MinorHead1.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_Desc1(woDesc.split("\\$")[0]);//work desc id
					tempIssueDetailsData.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[0]); //UOM id
					tempIssueDetailsData.setTotalAmount1(TotalAmount.trim());
					tempIssueDetailsData.setQuantity1(Quantity.trim());
					tempIssueDetailsData.setwOManualDescription(woManualDesc);
					if(note.length()!=0){
						note=strUserName+" - "+note;
					}
					tempIssueDetailsData.setComments1(note);
					
					String woRowCode="";
					log.info(workOrderBean.getWorkOrderNo()+"  "+tempIssueDetailsData.getWO_MajorHead1()+"  "+tempIssueDetailsData.getWO_MinorHead1()+"  "+tempIssueDetailsData.getWO_Desc1());
					
					
					if(!storeDuplicateworkOrderDetailsList.contains(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
						storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
						woRowCode="MH-"+woROWCodeFormat+"-1";
						tempIssueDetailsData.setWoRowCode(woRowCode);
					}else{
						int countSameObjects=1;
						//counting same object's and based on count it will generate work order row code
						//so we have this unique value, to manage bill's with same WD 
						for (String sameObjectCOunt : storeDuplicateworkOrderDetailsList) {
							if(sameObjectCOunt.equals(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
								countSameObjects++;
							}
						}
						woRowCode="MH-"+woROWCodeFormat+"-"+countSameObjects;
						tempIssueDetailsData.setWoRowCode(woRowCode);
						storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
					}

					if(!DeleteColVal.equals("d")){//if this row is deleted in work order then don't add row details
						//this method used to insert the row's data in QS_WORKORDER_TEMP_ISSUE_DTLS table 
						List<Integer> tempDetailresp1 = workControllerDao.insertWorkOrderTempIssueDetails(tempIssueDetailsData,workOrderTMPIssurPK);
			 			showWorkOrderBean.add(printWO);
					}	
				}
			}
			String s=null;
			//s.trim();
			model.addAttribute("workOrderCreationList", showWorkOrderBean);
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			response = "failed";
			//execute this query if trancsaction management is not applied 
			int result=	workControllerDao.deleteWOCommitedData(workOrderTMPIssurPK,workOrderBean.getSiteId());
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			isSendMail = false;
		}
		if (isSendMail) {
			StringBuffer emailSubject = new StringBuffer("");
			emailSubject.append("Regarding Approval of Work Order.");
			constructEmailContent(workOrderBean, request,null, optionalCCmails);
		}
	return response;
}
	
	@SuppressWarnings("unused")
	@Override
	public String doWorkOrderEntry(WorkOrderBean workOrderBean, HttpServletRequest request, Model model, String siteId,
			String user_id, HttpSession session) {
		String response = "";
		String siteName = "";
		String strUserName = "";
		String workOrderTMPIssurPK = "";
		String TotalAmountOfWorkOrder = "";
		String actualWorkOrderNumber = "";
		String actionType = "";
		String workOrderStatus = "";
		String workOrderCreateEmpId = user_id;
		String isWorkOrderDataChanged = "";
		String optionalCCmails = "";
		//String subject = "";
		String typeOfWork="";
		String siteWiseTempWorkOrderNo = workOrderBean.getSiteWiseWONO();
		String nextApprovelEmpId = workOrderBean.getApproverEmpId();
		String tempIssueIdForModifyWO = request.getParameter("actualtempIssueId");
		// this variable holding button name e.g Submit or Draft Work Order
		// etc...
		String isSaveOrUpdateOperation = workOrderBean.getIsSaveOrUpdateOperation();
		boolean isSendMail = true;
		double labourAmount = 0.0;
		double materialAmount = 0.0;
		List<WorkOrderBean> workOrderRowDetailsBean = new ArrayList<WorkOrderBean>();
		List<WorkOrderBean> orderBeans = new ArrayList<WorkOrderBean>();

		List<Number> reponse1 = null;
		WriteTrHistory.write("Tr_Opened in InCr_indCre, ");
		
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null?"":request.getParameter("numbeOfRowsToBeProcessed");
		
		try {
			strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String[] boqNo= request.getParameterValues("boq_no" +recordsCount.split("\\|")[0]);
			TotalAmountOfWorkOrder=request.getParameter("TotalAmountOfWorkOrder")==null?"":request.getParameter("TotalAmountOfWorkOrder");
			actualWorkOrderNumber= request.getParameter("actualWorkOrderNumber" )==null?"":request.getParameter("actualWorkOrderNumber");
			isWorkOrderDataChanged= request.getParameter("isWorkOrderDataChanged" )==null?"":request.getParameter("isWorkOrderDataChanged");
			if(boqNo!=null){
				workOrderBean.setBoqNo(boqNo[0]);
			}
			workOrderBean.setSiteName(siteName);
			// workOrderBean.setTypeOfWork("PIECEWORK");
			workOrderBean.setActualWorkOrderNo(actualWorkOrderNumber);
			workOrderBean.setTotalAmount1(TotalAmountOfWorkOrder);
			workOrderStatus = workOrderBean.getWorkOrderStatus() == null ? "" : workOrderBean.getWorkOrderStatus();
			workOrderBean.setWorkOrderStatus(workOrderStatus);
				/*if(workOrderStatus.equals("ModifyWorkOrder")){
					if(isWorkOrderDataChanged.equals("false")){
							throw new Exception("not changed work order");
					}
				}*/
			// create instance of Random class
/*			Random rand = new Random();
			// Generate random integers in range 0 to 999
			int rand_int1 = rand.nextInt(1000);
			int tokenForMailApproval = rand_int1 * 1000;
			workOrderBean.setTokenForMailApproval(String.valueOf(tokenForMailApproval));*/
			log.info("Inserting work order details : " + workOrderBean.getTypeOfWork() + " " + TotalAmountOfWorkOrder);
			synchronized (this) {
				reponse1 = workControllerDao.insertWorkOrderDetail(workOrderBean, workOrderCreateEmpId);
			}
			log.info(workOrderBean.getSiteName()+"\t"+
					workOrderBean.getContractorName() +"\t"+
		        	workOrderBean.getWorkOrderDate()+"\t"+
		            workOrderBean.getSiteWiseWONO()+"\t"+
		            workOrderBean.getWorkOrderNo()+"\t"+
		            workOrderBean.getWorkOrderName()+"\t"+
		            workOrderBean.getLaborWoAmount()+"\t"+
		            workOrderBean.getTypeOfWork()+"\t");
			} catch (Exception e) {
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			if (e.getMessage()!=null&&e.getMessage().equals("Work Order Number already exists")) {
				response = "Work Order Number already exists";
			}
			if (e.getMessage()!=null&&e.getMessage().equals("not changed work order")) {
				response = "not changed work order";
			}
			e.printStackTrace();
			isSendMail = false;
			return response;
		}
			
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String workOrderResult = "";
			try {
				workOrderResult = reponse1.get(0).toString();
				workOrderTMPIssurPK = reponse1.get(1).toString();
				workOrderBean.setQS_Temp_Issue_Id(workOrderTMPIssurPK);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String purpose = workOrderBean.getPurpose();
			workOrderBean.setSiteName(siteName);
			if (isSaveOrUpdateOperation.equals("Draft Work Order")) {
				actionType = "DF";
			} else {
				actionType = "C";
			}
			//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "C" means Create 	
			int response2 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean,actionType);
			//String[] termsAndConditions=request.getParameterValues("tc");
			if(workOrderStatus.equals("ModifyWorkOrder")){
				//termsAndConditions=request.getParameterValues("changedTC");
				try {
					//inserting another message if this is work order modify operation
					 //cloning the object so main object should not get any affect so we can use it with actual values
					WorkOrderBean	orderBean = (WorkOrderBean) workOrderBean.clone();
					orderBean.setPurpose("Temporary work order number "+siteWiseTempWorkOrderNo+" changed to "+workOrderBean.getSiteWiseWONO());
						response2 = workControllerDao.inserWorkOrderCreationDetail(orderBean,"M");
					} catch (CloneNotSupportedException e1) {
						log.info(e1.getMessage());
					}
			}
			String[] termsAndConditions=request.getParameterValues("termsAndCOnditions");
			optionalCCmails=request.getParameter("optionalCCmails");
			//subject=request.getParameter("subject");
			typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
			
			if(termsAndConditions!=null){
				int response3=workControllerDao.insertTermsAndConditions(workOrderBean,workOrderTMPIssurPK,termsAndConditions);
			}
			List<Map<String, String>> termasAndCon=new ArrayList<Map<String, String>>();
			if(termsAndConditions!=null)
			for (String string : termsAndConditions) {
				Map<String, String > map=new HashMap<String, String>();
				string=string.trim();
				if(string.length()!=0){
					map.put("TERMS_CONDITION_DESC", string);
					termasAndCon.add(map);
				}
			}
			//adding terms and condition in model object so we can print this terms and condition in work order print page
			model.addAttribute("listTermsAndCondition", termasAndCon);
		
			String numOfRec[] = null;
			if ((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			if(workOrderStatus!=null&&workOrderStatus.equals("ModifyWorkOrder")){
				numOfRec= request.getParameterValues("dispplayedRows");
			}
			//this is processing number of rows created in Work order
			if (numOfRec != null && numOfRec.length > 0) {
				for (String num : numOfRec) {
					WorkOrderBean printWO=new WorkOrderBean();
					List<WorkOrderBean> updateWorkAreaDetails=new ArrayList<WorkOrderBean>();
					num = num.trim();
					//this variable used for Modify Work Order because we are again inserting records
					String DeleteColVal = request.getParameter("isDelete" + num)==null?"":request.getParameter("isDelete" + num);
					boolean isThisNewRow=request.getParameter("isNewRowAdded" + num)==null?false:Boolean.valueOf(request.getParameter("isNewRowAdded" + num));
					
					String WO_MajorHead1= request.getParameter("WO_MajorHead" + num)==null?"":request.getParameter("WO_MajorHead" + num);
					String WO_MinorHead1 =request.getParameter("WO_MinorHead" + num)==null?"":request.getParameter("WO_MinorHead" + num);
					String woDesc= request.getParameter("WO_Desc" + num)==null?"":request.getParameter("WO_Desc" + num);
					String UnitsOfMeasurement1=request.getParameter("UnitsOfMeasurement" + num)==null?"":request.getParameter("UnitsOfMeasurement" + num);
					String Quantity = request.getParameter("Quantity" + num);
					String TotalAmount = request.getParameter("TotalAmount" + num);
					String note = request.getParameter("comments" + num)==null?"":request.getParameter("comments" + num).trim();
					String woManualDesc = request.getParameter("scopeOfWOrk" + num);
					String woRecordContains=request.getParameter("WoRecordContains"+num);
					String[] scopeOfWork=request.getParameterValues("ScopeOfWork" + num);
					String ScopeOfWork="";
					if(scopeOfWork!=null)
					for (String scopeWork : scopeOfWork) {
						scopeWork=scopeWork.trim();
						if(scopeWork!=null&&scopeWork.length()!=0){
						
							ScopeOfWork+=scopeWork+"@@";
						}
					}
					
					woManualDesc=ScopeOfWork;
					try {
						printWO.setWO_MajorHead1(WO_MajorHead1.split("\\$")[1].replace("#","\""));
						printWO.setWO_MinorHead1(WO_MinorHead1.split("\\$")[1]);
						printWO.setWO_Desc1(woDesc.split("\\$")[1]);
						printWO.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[1]);
						printWO.setwOManualDescription(woManualDesc);
					} catch (Exception e) {
						e.printStackTrace();
					}
					printWO.setTotalAmount1(TotalAmount);
					printWO.setQuantity(Quantity);
					Double acceptedRate1=0.0;
					
					/* 
						 this code is for getting all the work area data
						  as a part of array
					 */
					
					String[] acceptedRate= request.getParameterValues("accepted_rate" + num);
					if(workOrderStatus!=null&&workOrderStatus.equals("ModifyWorkOrder")){	
						//if this Modify Work Order, then take all the area quantity back
						String[] actualAreaAlocatedQTYPrice=request.getParameterValues("actualAreaAlocatedQTYPrice" + num);
						String[] actualworkAreaIds = request.getParameterValues("actualWorkAreaId" + num);
						String[] actualSelectedArea = request.getParameterValues("actualAreaAlocatedQTY" + num);
						String[] actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID = request.getParameterValues("actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID" + num);
						String[] actualrecordType = request.getParameterValues("actualrecordType" + num);
						String[] actualBlockName1 = request.getParameterValues("BLOCK_NAME1" + num);
						String[] actualFloor_name1 = request.getParameterValues("FLOOR_NAME1" + num);
						String[] actualFlat_id1 = request.getParameterValues("FLAT_ID1" + num);
					
						if(actualworkAreaIds!=null){
							for (int i = 0; i < actualworkAreaIds.length; i++) {
								if (actualSelectedArea[i] != null && actualworkAreaIds[i] != null) {
									WorkOrderBean bean = new WorkOrderBean();
									bean.setSelectedArea(actualSelectedArea[i].trim());
									bean.setWorkAreaId(actualworkAreaIds[i]);
									bean.setPercentage("0");
									updateWorkAreaDetails.add(bean);
									//delete means take the current quantity or work area back
									//workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"deleted");
								}
							}
						}
						workControllerDao.batchUpdateWorkAreaMapping(updateWorkAreaDetails,"deleted");	
					}
					
					if(acceptedRate!=null)
					for (String rate : acceptedRate) {
						acceptedRate1=Double.valueOf(rate);
					}
					
					printWO.setAcceptedRate1(String.valueOf(acceptedRate1));
					WorkOrderBean tempIssueDetailsData=new WorkOrderBean();
					tempIssueDetailsData.setWO_MajorHead1(WO_MajorHead1.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_MinorHead1(WO_MinorHead1.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_Desc1(woDesc.split("\\$")[0]);//work desc id
					tempIssueDetailsData.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[0]); //uom id
					tempIssueDetailsData.setTotalAmount1(TotalAmount.trim());
					tempIssueDetailsData.setQuantity1(Quantity.trim());
					tempIssueDetailsData.setwOManualDescription(woManualDesc);
					tempIssueDetailsData.setWoRecordContains(woRecordContains);
						if(note.length()!=0){
							note=strUserName+" - "+note;
						}
					tempIssueDetailsData.setComments1(note);
					if(!DeleteColVal.equals("d")){//if this row is deleted in work order then don't add row details
						insertWorkOrderTempIssueDetialsData(workOrderBean,request,session,tempIssueDetailsData,workOrderTMPIssurPK,num);
						workOrderRowDetailsBean.add(printWO);
					}
					
				} // for loop
			} // if
			//adding the work order details in model object for the print page
			model.addAttribute("workOrderCreationList", workOrderRowDetailsBean);
			//this method is for only one level approval, inserting data directly in permanent table
			if(nextApprovelEmpId.equals("END")&&!isSaveOrUpdateOperation.equals("Draft Work Order")){
			  //response2 = workControllerDao.insertCompletedWorkOrder(workOrderBean, workOrderTMPIssurPK,workOrderBean.getSiteWiseWONO(), nextApprovelEmpId);
				workControllerDao.updatePendingWorkOrder(workOrderBean, workOrderTMPIssurPK,workOrderBean.getSiteWiseWONO(), nextApprovelEmpId,"insert");
			}
			
			String s=null;
			//s.trim();
			transactionManager.commit(status);
		
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
		//	workControllerDao.deleteWOCommitedData(workOrderTMPIssurPK);
			e.printStackTrace();
			transactionManager.rollback(status);
			isSendMail = false;
			//execute this query if transaction management is not applied 
			int result=	workControllerDao.deleteWOCommitedData(workOrderTMPIssurPK,workOrderBean.getSiteId());
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			
		}
		if (isSendMail&&!isSaveOrUpdateOperation.equals("Draft Work Order")) {
			StringBuffer emailSubject = new StringBuffer("");
			emailSubject.append("Regarding Approval of Work Order.");
			constructEmailContent(workOrderBean,request,null,optionalCCmails);
			/*
			
			// workOrderBean
			List<String> to_EmailAddress = new ArrayList<String>();
			to_EmailAddress.add("chaniket@amaravadhis.com");

			List<String> ccMailAddressOfEmployee = new ArrayList<String>();
			ccMailAddressOfEmployee.add("aniketchavan75077@gmail.com");
			if (optionalCCmails != null) {
				ccMailAddressOfEmployee.addAll(Arrays.asList(optionalCCmails));
			}
			EmailFunction objEmailFunction = new EmailFunction();
			StringBuffer emailSubject = new StringBuffer("");
			emailSubject.append("Regarding Approval of Work Order.");
			
			StringBuffer emailBodyContent=new StringBuffer("");
			emailBodyContent.append("<tr>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getSiteName()+"</td>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getContractorName()+"");
        	emailBodyContent.append("</td>");
        	emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderDate()+"</td>");
            emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getSiteWiseWONO()+"</td>");
            emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderNo()+"</td>");
            emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderName()+"</td>");
            emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getLaborWoAmount()+"</td>");
            emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getTypeOfWork()+"</td>");
            emailBodyContent.append("</tr>");

			objEmailFunction.sendMailWorkOrderCreation(workOrderBean,request,emailSubject,emailBodyContent,to_EmailAddress,ccMailAddressOfEmployee);
		*/}
		return response;
	}
	//========================= this method using for revise NMR and PIECE Work work order======================================= 
	
	
	String insertWorkOrderTempIssueDetialsData(WorkOrderBean workOrderBean, HttpServletRequest request, HttpSession session, WorkOrderBean tempIssueDetailsData, String workOrderTMPIssurPK, String num){					
		List<Integer> tempDetailresp1 = workControllerDao.insertWorkOrderTempIssueDetails(tempIssueDetailsData,workOrderTMPIssurPK);
		String tempIssueDetailsId=String.valueOf(tempDetailresp1.get(0));
		
		if(workOrderBean.getTypeOfWork().equals("NMR")){
			return  tempIssueDetailsId;
		}
		
		//String strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
		String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
		String[] selectedArea = request.getParameterValues("selectedArea" + num);
		String[] actualArea = request.getParameterValues("availableArea" + num);
		String[] workAreaId = request.getParameterValues("workAreaId" + num);
		String[] wo_measurmen_name = request.getParameterValues("wo_measurmen_name" + num);
		String[] percentage = request.getParameterValues("percentage" + num);
		String[] record_type=request.getParameterValues("record_type"+num);
		String[] boq_no= request.getParameterValues("boq_no" + num);
		String[] boq_rate= request.getParameterValues("boqRate" + num);
		String[] acceptedRate= request.getParameterValues("accepted_rate" + num);
		
		int index = Integer.valueOf(num);
		//Processing all the data of work area details row wise
		for (int i = 0; i < workAreaId.length; i++) {
			//Checking the record type and based on type adding data in table
		
				if (actualArea[i] != null && selectedArea[i] != null && workAreaId[i] != null) {
					WorkOrderBean bean = new WorkOrderBean();
					//bean.setTotalAmount1(TotalAmount.trim());
					bean.setAcceptedRate1(acceptedRate[i]);
					bean.setSiteId(siteId);
					//bean.setQuantity1(Quantity.trim());
					//bean.setwOManualDescription(woManualDesc);
					bean.setSelectedArea(selectedArea[i].trim());
					bean.setActualArea(actualArea[i]);
					bean.setWorkAreaId(workAreaId[i]);
					bean.setPercentage(percentage[i]);
					//bean.setBoqNo(boq_no[i]);
					//bean.setComments1(strUserName+" - "+note);
					bean.setBoqRate(boq_rate[i]);
					bean.setBoqRecordType(record_type[i]);
						
					//inserting new added work area
					List<Integer> tempDetailresp = workControllerDao.WorkOrderTempIssueAreaDetails(bean,tempIssueDetailsId);
					//Updating BOQ area 
					workControllerDao.updateWorkAreaMapping(bean,"","");
					
				/*	bean.setWorkAreaId(workAreaId[i]);
					bean.setSelectedArea(selectedArea[i+1].trim());
					//here updating the material quantity
					workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"");
					bean.setWorkAreaId(workAreaId[i]);
					bean.setSelectedArea(selectedArea[i].trim());*/
					List<WorkOrderBean> listOfMaterialBoq=new ArrayList<WorkOrderBean>();
					if(record_type[i].equals("MATERIAL")){
						bean.setWorkAreaId(workAreaId[i]);//Loading material BOQ details 
						bean.setTempIssueAreaDetailsId(tempDetailresp.get(0).toString());
						listOfMaterialBoq=insertMaterialData(bean,listOfMaterialBoq);
						/*List<Map<String, Object>>  listOfMaterialBOQ=	workControllerDao.loadMaterialBoqDetailsForWO(bean);
						 
						for (Map<String, Object> map : listOfMaterialBOQ) {
							bean = new WorkOrderBean();
							String perUnitQuantity=	map.get("PER_UNIT_QUANTITY").toString(); 
							String perUnitAmount=map.get("PER_UNIT_AMOUNT").toString();
							String materialGroupId=map.get("MATERIAL_GROUP_ID").toString();
							String materialUOM=map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString();
							bean.setPerUnitQuantity(perUnitQuantity);
							bean.setPerUnitAmount(perUnitAmount);
							bean.setTempIssueAreaDetailsId(tempDetailresp.get(0).toString());
							bean.setMaterialGroupId(materialGroupId);
							bean.setUnitsOfMeasurement1(materialUOM);
							bean.setSelectedArea(selectedArea[i].trim());
							Double totalQuantity=Double.valueOf(bean.getSelectedArea())*Double.valueOf(perUnitQuantity);
							Double totalAmount=Double.valueOf(bean.getSelectedArea())*Double.valueOf(perUnitAmount);
							
							totalQuantity =Double.parseDouble(new DecimalFormat("##.###").format(totalQuantity));
							totalAmount =Double.parseDouble(new DecimalFormat("##.###").format(totalAmount));
							bean.setTotalQuantity(totalQuantity.toString());
							bean.setTotalAmount(totalAmount.toString());
							
						 listOfMaterialBoq.add(bean);
						}*/
						
						//inserting all the rows using spring JDBC batch update
						int result=	workControllerDao.insertDataIntoWorkOrderTempProductDtls(listOfMaterialBoq,"insert");	
						
					}else{
					 
					}
				}
		}
		return tempIssueDetailsId;
	}
	
	
	
	/**
	 * this method is used for inserting the revise work order 
	 * revise work order means modification in existing work order
	 * some changed that is not done while creating work order 
	 * @throws Exception 
	 */
	@Override
	public String doReviseWorkOrder (WorkOrderBean workOrderBean, HttpServletRequest request, Model model,
			String siteId, String user_id, HttpSession session) throws Exception {
		boolean isWorkOrderChanged=false;
		boolean isSendMail = true;
		String response ="";
		String siteName = "";
		String strUserName =  "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		List<WorkOrderBean> orderBeans = new ArrayList<WorkOrderBean>();
		List<String> errorListForMaterialBOQ = new ArrayList<String>();
		List<WorkOrderBean> updateWorkAreaDetails=null;
		String siteWiseTempWorkOrderNo = workOrderBean.getSiteWiseWONO();
		String workOrderNo = "";
		String nextApprovelEmpId=workOrderBean.getApproverEmpId();
		String workOrderCreateEmpId = user_id;
		List<WorkOrderBean> workOrderRowDetailsList=new ArrayList<WorkOrderBean>();
		String workOrderTMPIssurPK = "";
		List<Number> reponse1 = null;
		String UnitsOfMeasurement1="";
		String[] termsAndConditions;
		String optionalCCmails="";
		boolean isThisWOSaveOperation=false;
		double materialAmount=0.0,labourAmount=0.0;
		String actionType="";
		String holdRowNumber="";
		String isSaveOrUpdateOperation=workOrderBean.getIsSaveOrUpdateOperation();
		String workOrderStatus=workOrderBean.getWorkOrderStatus()==null?"":workOrderBean.getWorkOrderStatus();
		workOrderBean.setWorkOrderStatus(workOrderStatus);
		try {
			strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String revisedWorkOrderNumber=request.getParameter("reviseWorkOrderNumber1")==null?"":request.getParameter("reviseWorkOrderNumber1");
			String actualPurpose=request.getParameter("actualPurpose")==null?"":request.getParameter("actualPurpose");
			String boqNo= request.getParameter("boqNo")== null ? "" :request.getParameter("boqNo") ;
			if(boqNo!=null){
				workOrderBean.setBoqNo(boqNo);
			}
			workOrderBean.setSiteName(siteName);
			String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
			workOrderBean.setTypeOfWork(typeOfWork+"@@NMR");
			if(!workOrderStatus.equals("ModifyWorkOrder")){
				workOrderBean.setWorkOrderNo(revisedWorkOrderNumber);
			}else{
				String totalAmountOfWorkOrder=request.getParameter("TotalAmountOfWorkOrder")==null?"":request.getParameter("TotalAmountOfWorkOrder");
				workOrderBean.setTotalAmount1(totalAmountOfWorkOrder);
			}
			//workOrderBean.setFromEmpName(strUserName);
			workOrderBean.setUserId(user_id);
			synchronized (this) {
				//inserting revise NMR work order main data like work order number, name, date, contractor id
				reponse1 = workControllerDao.insertReviseWorkOrderDetail(workOrderBean, workOrderCreateEmpId);
				workOrderNo=workOrderBean.getOldWorkOrderNo();
			}
		
			WriteTrHistory.write("Tr_Opened in InCr_indCre, ");
			String workOrderResult = "";
				
			try {
				workOrderResult = reponse1.get(0).toString();
				workOrderTMPIssurPK = reponse1.get(1).toString();
				workOrderBean.setQS_Temp_Issue_Id(workOrderTMPIssurPK);
			} catch (Exception e) {
				e.printStackTrace();
			}
			workOrderBean.setSiteName(siteName);
		
			if(isSaveOrUpdateOperation.equals("Draft Work Order")){
				actionType="DF";//draft record
				isThisWOSaveOperation=true;
			}else if(isSaveOrUpdateOperation.equals("Approve")){
				actionType="A";
			}else if(isSaveOrUpdateOperation.equals("Submit")||isSaveOrUpdateOperation.equals("Revise")){
				actionType="C";
			}else if(isSaveOrUpdateOperation.equals("Modify WorkOrder")){
				actionType="C";
			}else {actionType="C";}		
			//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "C" means Create 
			int response2 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean,actionType);
			if(workOrderStatus.equals("ModifyWorkOrder")){
				try {
					 //cloning the object so main object should not get any affect so we can use it with actual values
					WorkOrderBean	orderBean = (WorkOrderBean) workOrderBean.clone();
					orderBean.setPurpose("Temporary work order number "+siteWiseTempWorkOrderNo+" changed to "+workOrderBean.getSiteWiseWONO());
						response2 = workControllerDao.inserWorkOrderCreationDetail(orderBean,"M");
					} catch (CloneNotSupportedException e1) {
						log.info(e1.getMessage());
					}
			}
			
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			String [] noOfRowToProcess= request.getParameterValues("dispplayedRows");
		    termsAndConditions=request.getParameterValues("termsAndCOnditions");
			//optionalCCmails=request.getParameter("optionalCCmails");
			//String 	subject=request.getParameter("subject");
			
			
			if(termsAndConditions!=null){
				//inserting the terms and condition's
				 response2=workControllerDao.insertTermsAndConditions(workOrderBean,workOrderTMPIssurPK,termsAndConditions);
			}
			List<Map<String, String>> termasAndCon=new ArrayList<Map<String, String>>();
			if(termsAndConditions!=null)
			for (String string : termsAndConditions) {
				Map<String, String > map=new HashMap<String, String>();
				string=string.trim();
				if(string.length()!=0){
					map.put("TERMS_CONDITION_DESC", string);
					termasAndCon.add(map);
				}
			}
			//adding terms and condition in model object so we can print this terms and condition in work order print page
			model.addAttribute("listTermsAndCondition", termasAndCon);
			List<String> listOfDeletedRowRecords=new ArrayList<String>();
			List<Integer> tempDetailresp = new ArrayList<Integer>();
			
			//this is processing number of rows created in Workorder
			if (noOfRowToProcess != null && noOfRowToProcess.length > 0) {
				for (String num : noOfRowToProcess) {
					WorkOrderBean printWO=new WorkOrderBean();
					updateWorkAreaDetails=new ArrayList<WorkOrderBean>();
					num = num.trim();
					holdRowNumber=num;
					String WO_MajorHead1= request.getParameter("WO_MajorHead" + num)==null?"":request.getParameter("WO_MajorHead" + num);
					String WO_MinorHead1 =request.getParameter("WO_MinorHead" + num)==null?"":request.getParameter("WO_MinorHead" + num);
					String woDesc= request.getParameter("WO_Desc" + num)==null?"":request.getParameter("WO_Desc" + num);
					//String
					UnitsOfMeasurement1=request.getParameter("UnitsOfMeasurement" + num)==null?"":request.getParameter("UnitsOfMeasurement" + num);
					String Quantity = request.getParameter("Quantity" + num);
					//String AcceptedRate12 = request.getParameter("Quantity" + num);
					String TotalAmount = request.getParameter("TotalAmount" + num);
					String actualComments=request.getParameter("actualComments" + num)==null?"":request.getParameter("actualComments" + num);
					String note = request.getParameter("comments" + num)==null?"":request.getParameter("comments" + num);
					String woManualDesc = request.getParameter("scopeOfWOrk" + num)==null?"":request.getParameter("scopeOfWOrk" + num);
					String woRecordContains=request.getParameter("WoRecordContains"+num)==null?"":request.getParameter("WoRecordContains"+num);
					//String labourAmount=request.getParameter("labourAmount"+num)==null?"":request.getParameter("labourAmount"+num);
					//String materialAmount=request.getParameter("materialAmount"+num)==null?"":request.getParameter("materialAmount"+num);
					String[] scopeOfWork=request.getParameterValues("ScopeOfWork" + num);
					String ScopeOfWork="";
					if(scopeOfWork!=null)
					for (String scopeWork : scopeOfWork) {
						scopeWork=scopeWork.trim();
						if(scopeWork!=null&&scopeWork.length()!=0){
						
							ScopeOfWork+=scopeWork+"@@";
						}
					}
					woManualDesc=ScopeOfWork;
					try {
						//this printWO object is for printing the names in work order print page
						printWO.setWO_MajorHead1(WO_MajorHead1.split("\\$")[1].replace("#","\""));
						printWO.setWO_MinorHead1(WO_MinorHead1.split("\\$")[1]);
						printWO.setWO_Desc1(woDesc.split("\\$")[1]);
						printWO.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[1]);
						printWO.setwOManualDescription(woManualDesc);
					} catch (Exception e) {
						e.printStackTrace();
					}
					printWO.setTotalAmount1(TotalAmount);
					printWO.setQuantity(Quantity);
					Double acceptedRate1=0.0;
					
					/* you will get work area details in array type what ever you have entered in work order creation page in popup model,
						you can search this getParameterValues names in java script addWorkArea() method
					*/ 
					
					String[] selectedArea = request.getParameterValues("selectedArea" + num);
					String[] actualArea = request.getParameterValues("availableArea" + num);
					String[] workAreaId = request.getParameterValues("workAreaId" + num);
					String[] recordType = request.getParameterValues("record_type" + num);
				  //String[] available = request.getParameterValues("available" + num);
				  //String[] wo_measurmen_name = request.getParameterValues("wo_measurmen_name" + num);
				  //String[] percentage = request.getParameterValues("percentage" + num);
					String[] acceptedRate= request.getParameterValues("accepted_rate" + num);
					String[] block_name=request.getParameterValues("BLOCK_NAME"+num);
					String[] floor_name=request.getParameterValues("FLOOR_NAME"+num);
					String[] flat_id=request.getParameterValues("FLAT_ID"+num);
					String[] boq_rate= request.getParameterValues("boqRate" + num);
					String[] WO_WORK_TEMP_ISSUE_AREA_DTLS_ID= request.getParameterValues("QS_WO_TEMP_ISSUE_DTLS_ID" + num);
				  //String[] boq_no= request.getParameterValues("boq_no" + num);
					String[] oldWOIssueAreaDetailsId=request.getParameterValues("OLD_QS_WO_ISSUE_DTLS_ID"+num);
				  //this data is for actual work Area data before changing any quantity and rate
					String[] actualAreaAlocatedQTYPrice=request.getParameterValues("actualAreaAlocatedQTYPrice" + num);
					String[] actualworkAreaIds = request.getParameterValues("actualWorkAreaId" + num);
					String[] actualSelectedArea = request.getParameterValues("actualAreaAlocatedQTY" + num);
					String[] actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID = request.getParameterValues("actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID" + num);
					String[] actualrecordType = request.getParameterValues("actualrecordType" + num);
					String[] actualBlockName1 = request.getParameterValues("BLOCK_NAME1" + num);
					String[] actualFloor_name1 = request.getParameterValues("FLOOR_NAME1" + num);
					String[] actualFlat_id1 = request.getParameterValues("FLAT_ID1" + num);
				
					WorkOrderBean changedWorkOderDetail=new WorkOrderBean();
					WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
					
					// reallocating already initiated work area quantity in BOQ
				   
					if(actualworkAreaIds!=null){
						for (int i = 0; i < actualworkAreaIds.length; i++) {
							if (actualSelectedArea[i] != null && actualworkAreaIds[i] != null) {
								WorkOrderBean bean = new WorkOrderBean();
								bean.setSelectedArea(actualSelectedArea[i].trim());
								bean.setWorkAreaId(actualworkAreaIds[i]);
								bean.setPercentage("0");
								//delete means take the current quantity or work area back
								updateWorkAreaDetails.add(bean);
								//workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"deleted");
							}
						}
					}
					workControllerDao.batchUpdateWorkAreaMapping(updateWorkAreaDetails,"deleted");
					
				  /*  String actualtempIssueId=request.getParameter("QS_Temp_Issue_Dtls_Id"+num)==null?"":request.getParameter("QS_Temp_Issue_Dtls_Id"+num);
					  if(actualworkAreaIds!=null&&actualtempIssueId.length()!=0){
						response2=workControllerDao.revertAlltheAllocatedQuantity(actualtempIssueId);
					  }
				  */
					
					List<String> listOfModifiedRecords=new ArrayList<String>();
					List<String> modifiedMaterialRecordsList=new ArrayList<String>();
					
					boolean isRowDataChanged = false;
					String DeleteColVal = request.getParameter("isDelete" + num)==null?"":request.getParameter("isDelete" + num);
					boolean isThisNewRow=request.getParameter("isNewRowAdded" + num)==null?false:Boolean.valueOf(request.getParameter("isNewRowAdded" + num));
					String strChangedComments="";
					boolean isDelete = false;
					//if any row deleted while revise work order then maintain the log
					if (DeleteColVal.equals("d")) {
						isDelete = true;
						strChangedComments=strUserName +"-  WD (\""+printWO.getWO_Desc1()+"\")  deleted.";
						listOfDeletedRowRecords.add(strChangedComments);
						isWorkOrderChanged=true;
						if(num.equals(noOfRowToProcess[noOfRowToProcess.length-1]))
						for (String strModifyComment : listOfDeletedRowRecords) {
							changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
							changedWorkOderDetail.setTypeOfWork("PIECEWORK");
							actualWorkOderDetail.setTypeOfWork("PIECEWORK");
							actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
							workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, "", nextApprovelEmpId, "M", user_id,strModifyComment);
							listOfDeletedRowRecords=new ArrayList<String>();
							isWorkOrderChanged=true;
						}
						//if the row is deleted then make loop to next iteration
						continue;
					}
					
					//if the row is new then maintain the log in DB
					if(isThisNewRow){
						strChangedComments=strUserName +"- Added new WD (\""+printWO.getWO_MajorHead1()+", "+printWO.getWO_MinorHead1()+", "+printWO.getWO_Desc1()+"\").";
						listOfModifiedRecords.add(strChangedComments);
						isWorkOrderChanged=true;
					}
					
					//in this List all the Actual WorkAreaId will come
					//List<String> actualWorkAreaIdList = actualworkAreaIds != null ? Arrays.asList(actualworkAreaIds): new ArrayList<String>();
					List<String> actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID_LIST=  actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID != null ? Arrays.asList( actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID): new ArrayList<String>();
					//in this List all the Current WorkAreaId will come
					//List<String> changedWorkAreaIdList = workAreaId != null ? Arrays.asList(workAreaId): new ArrayList<String>();
					List<String> changedISSUE_DTLS_IDlIST = WO_WORK_TEMP_ISSUE_AREA_DTLS_ID != null? Arrays.asList(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID) : new ArrayList<String>();
					
				/* 
				        Checking here work area selected area is changed or not, and also checking work area rate is changed or not
				    	if changed update work area in BOQ_AREA_Mapping and   QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
				*/

				if(actualworkAreaIds!=null&&workAreaId!=null)
					for (int i = 0; i < actualworkAreaIds.length; i++) {
						for (int j = 0; j < workAreaId.length; j++) {
							//checking Actual WorkAreaId and current WorkAreaID is same or not if same enter into condition
							if (workAreaId[j].equals(actualworkAreaIds[i])  && actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i].equals( WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[j])){
								// 	Checking here is Actual quantity is differ from current quantity, if changed then update the BOQ_AREA_MAPPING and QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
								//	Current means the values you are seeing in revise work order
								double tempActualSelectedArea=Double.valueOf(actualSelectedArea[i]);
								double tempSelectedArea=Double.valueOf(selectedArea[j]);
								double tempActualAreaAlocatedQTYPrice=Double.valueOf(actualAreaAlocatedQTYPrice[i]);
								double tempAcceptedRate=Double.valueOf(acceptedRate[j]);
								// if(!actualSelectedArea[i].equals(selectedArea[j])){
								 if(tempActualSelectedArea!=tempSelectedArea){
									  String strModifyComment = strUserName + " - Modified quantity "+actualSelectedArea[i]+" to "+ selectedArea[j]+" for(\""+printWO.getWO_Desc1()+"\"), "
											 +block_name[i];
										if(!floor_name[i].equals("-")){	strModifyComment+=", "+floor_name[i];}
										if(!flat_id[i].equals("-")){  strModifyComment+=", "+flat_id[i]+"\n";}
										strModifyComment+="@@"+actualSelectedArea[i]+"##"+selectedArea[j]+"##"+workAreaId[j]+"##"+recordType[i];
									listOfModifiedRecords.add(strModifyComment);
									//Checking is actual rate and current rate is same or not, if not same update the message
									//if(!actualAreaAlocatedQTYPrice[i].equals(acceptedRate[j])){
									if(tempActualAreaAlocatedQTYPrice!=tempAcceptedRate){
										 strModifyComment="";//strUserName + " - (\""+	printWO.getWO_Desc1()+"\")  Accepted rate changed from "+actualAreaAlocatedQTYPrice[i]+"  to "+ acceptedRate[j]+" for "+block_name[i]+", "+floor_name[i]+", "+flat_id[i]+"\n" ;
										 strModifyComment = strUserName + " - Modified Accepted Rate "+actualAreaAlocatedQTYPrice[i]+" to "+ acceptedRate[j]+" for (\""+	printWO.getWO_Desc1()+"\"), "
												 +block_name[i];
											if(!floor_name[i].equals("-"))	{strModifyComment+=", "+floor_name[i];}
											if(!flat_id[i].equals("-"))   {strModifyComment+=", "+flat_id[i]+"\n";}
											strModifyComment+="@@"+actualAreaAlocatedQTYPrice[i]+"##"+acceptedRate[j]+"##"+workAreaId[j]+"##"+recordType[i];
										listOfModifiedRecords.add(strModifyComment);
									}
									isWorkOrderChanged=true;
								 //}else if(Double.valueOf(actualAreaAlocatedQTYPrice[i])!=Double.valueOf(acceptedRate[j])){
								//}else if(!actualAreaAlocatedQTYPrice[i].equals(acceptedRate[j])){
								 }else if(tempActualAreaAlocatedQTYPrice!=tempAcceptedRate){
									String strModifyComment="";//strUserName + " - (\""+	printWO.getWO_Desc1()+"\")  Accepted rate changed from "+actualAreaAlocatedQTYPrice[i]+"  to "+ acceptedRate[j]+" for "+block_name[i]+", "+floor_name[i]+", "+flat_id[i]+"\n" ;
									 strModifyComment = strUserName + " - Modified Accepted Rate "+actualAreaAlocatedQTYPrice[i]+" to "+ acceptedRate[j]+" for (\""+	printWO.getWO_Desc1()+"\"), "
											 +block_name[i];
										if(!floor_name[i].equals("-")){	strModifyComment+=", "+floor_name[i];}
										if(!flat_id[i].equals("-")){  strModifyComment+=", "+flat_id[i]+"\n";}
										strModifyComment+="@@"+actualAreaAlocatedQTYPrice[i]+"##"+acceptedRate[j]+"##"+workAreaId[j]+"##"+recordType[i];
									listOfModifiedRecords.add(strModifyComment);
									isWorkOrderChanged=true;
								}
							}
						}
					}
				
				/*
				 	Checking here is any work area removed from work order,
					if removed then update in  BOQ_AREA_Mapping and   QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
					and make the work area inactive in QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
				*/
				if(actualworkAreaIds!=null&&workAreaId!=null)
				for (int i = 0; i < actualworkAreaIds.length; i++) {
					//checking here is any work removed or not if removed then maintains the log in DB
					if( !changedISSUE_DTLS_IDlIST.contains(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]) ){
						String strDeleteComment ="";// strUserName + " - (\""+	printWO.getWO_Desc1()+"\") Deleted work allocated area  "+actualSelectedArea[i]+" "+printWO.getUnitsOfMeasurement1()+"  for  "+actualBlockName1[i]+", "+actualFloor_name1[i] +", "+actualFlat_id1[i]+"\n";
						 strDeleteComment= strUserName + " - Deleted Work (\""+	printWO.getWO_Desc1()+"\")  having quantity("+actualSelectedArea[i]+") and accepted rate("+actualAreaAlocatedQTYPrice[i]+") for  "
						 +actualBlockName1[i];
							if(!actualFloor_name1[i].equals("-")){	strDeleteComment+=", "+actualFloor_name1[i];}
							if(!actualFlat_id1[i].equals("-")){   strDeleteComment+=", "+actualFlat_id1[i]+"\n";}
							strDeleteComment+="@@"+actualSelectedArea[i]+"##"+actualworkAreaIds[i]+"##"+actualrecordType[i];
						listOfModifiedRecords.add(strDeleteComment);
						isWorkOrderChanged=true;
					}
				}
				
			/*
			 	checking here is any work area is added in Work Order for Current Row Processing
				if added then insert the work area and update BOQ_AREA_Mapping
			*/
				 
				if(workAreaId!=null&&!isThisNewRow)
				for (int i = 0; i < WO_WORK_TEMP_ISSUE_AREA_DTLS_ID.length; i++) {
					//checking here is any row has added or not	
					if (!actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID_LIST.contains(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i])) {
						if (actualArea[i] != null && selectedArea[i] != null && workAreaId[i] != null) {
							String strAddedComment ="";// strUserName + " - Added work for (\""+printWO.getWO_Desc1()+"\") allocated area  "+selectedArea[i]+" "+printWO.getUnitsOfMeasurement1()+" for  "+block_name[i]+", "+floor_name[i]+", "+flat_id[i]+"\n" ;
							 strAddedComment = strUserName + " - Added Work for (\""+printWO.getWO_Desc1()+"\") with qunatity("+selectedArea[i]+") and accepted rate ("+acceptedRate[i]+") for  "
							 +block_name[i];
								if(!floor_name[i].equals("-")){	strAddedComment+=", "+floor_name[i];}
								if(!flat_id[i].equals("-")){  strAddedComment+=", "+flat_id[i]+"\n";}
								
								strAddedComment+="@@"+ selectedArea[i]+"##"+workAreaId[i]+"##"+recordType[i];
						
								listOfModifiedRecords.add(strAddedComment);
							isWorkOrderChanged=true;
						}
					}
				}
				
				if(acceptedRate!=null)
				for (String rate : acceptedRate) {
					acceptedRate1=Double.valueOf(rate);
				}
					
				printWO.setAcceptedRate1(String.valueOf(acceptedRate1));
					
				WorkOrderBean tempIssueDetailsData=new WorkOrderBean();
				tempIssueDetailsData.setWO_MajorHead1(WO_MajorHead1.split("\\$")[0]);//major head desc id
				tempIssueDetailsData.setWO_MinorHead1(WO_MinorHead1.split("\\$")[0]);//major head desc id
				tempIssueDetailsData.setWO_Desc1(woDesc.split("\\$")[0]);//work desc id
				tempIssueDetailsData.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[0]); //UOM id
				if(TotalAmount==null||Quantity==null||TotalAmount==null){
					log.error("Exception got while processing RowNumber "+ holdRowNumber+" "+workOrderBean.getWorkOrderNo()+" "+workOrderBean.getSiteId());
				}
				tempIssueDetailsData.setTotalAmount1(TotalAmount.trim());
				tempIssueDetailsData.setQuantity1(Quantity.trim());
				tempIssueDetailsData.setwOManualDescription(woManualDesc);
				tempIssueDetailsData.setWoRecordContains(woRecordContains);
				
				/*if(actualComments.length()!=0){
					strChangedComments=actualComments;
					if(note.length()!=0){
						strChangedComments+="-- "+strUserName+" - "+note;
					}
				}else if(note.length()!=0){
					strChangedComments=strUserName+" - "+note;
				}*/
				if(note.length()!=0){
					note=strUserName+" - "+note;
				}
				tempIssueDetailsData.setComments1(note);
				//strChangedComments="";
				//inserting the row's details like major head, minor head, work desc and quantity and remarks
				List<Integer> tempDetailresp1 = workControllerDao.insertWorkOrderTempIssueDetails(tempIssueDetailsData,workOrderTMPIssurPK);
				//getting the primary key of the QS_WORKORDER_TEMP_ISSUE_DTLS table so we can store this primary key QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS table
				String tempIssueDetailsId=String.valueOf(tempDetailresp1.get(0));
			
				if(typeOfWork.equals("NMR")){
					continue;
				}
				
				int index = Integer.valueOf(num);
					//processing all the work area details row wise
				for (int i = 0; i < workAreaId.length; i++) {
						if (actualArea[i] != null && selectedArea[i] != null && workAreaId[i] != null) {
							WorkOrderBean bean = new WorkOrderBean();
							bean.setTotalAmount1(TotalAmount.trim());
							bean.setAcceptedRate1(acceptedRate[i]);
							bean.setQuantity1(Quantity.trim());
							bean.setwOManualDescription(woManualDesc);
							bean.setSelectedArea(selectedArea[i].trim());
							bean.setActualArea(actualArea[i]);
							bean.setBoqRecordType(recordType[i]);
							bean.setWorkAreaId(workAreaId[i]);
							bean.setPercentage("100");
							bean.setBoqRate(boq_rate[i]);
							bean.setComments1(strUserName+" - "+note);
							//if WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i] not contains A then insert the old work order area details id
							//else insert the dummy data
							if(!WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i].contains("A")){
								bean.setOldQSWorkOrderAreaDTLSId(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
								bean.setTempIssueAreaDetailsId(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
								//if this is Modify Work Order then take the old work order area details issue id from another variable
								if(workOrderStatus.equals("ModifyWorkOrder")){
									bean.setOldQSWorkOrderAreaDTLSId(oldWOIssueAreaDetailsId[i]);
									bean.setTempIssueAreaDetailsId(oldWOIssueAreaDetailsId[i]);
								}
							}
							
							//inserting new added, work area
							tempDetailresp = workControllerDao.WorkOrderTempIssueAreaDetails(bean,tempIssueDetailsId);
							workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"");
							
							List<WorkOrderBean> listOfMaterialBoq=new ArrayList<WorkOrderBean>();
							Map<String, WorkOrderBean> oldMaterialProductDetail=new HashMap<String, WorkOrderBean>();//old means already inserted data in (QS_WORKORDER_PRODUCT_DTLS)
							Map<String, WorkOrderBean> newMaterialProductDetail=new HashMap<String, WorkOrderBean>();//new means revised Material BOQ data
							
							if(recordType[i].equals("MATERIAL")){
								StringBuffer errorListComment=null;
								bean.setWorkAreaId(workAreaId[i]);//Loading material BOQ details
								bean.setSiteId(workOrderBean.getSiteId());
								if(workOrderStatus.equals("ModifyWorkOrder")){
									bean.setTempIssueAreaDetailsId(oldWOIssueAreaDetailsId[i]);
								}
								StringBuffer strChangedDetail=new StringBuffer("");
								List<WorkOrderBean> list=new ArrayList<WorkOrderBean>();
								list.add(bean);
								//validating material data, is current quantity is less than material issued quantity then throw the data
								Map<String, String> errorMessage=workControllerDao.validateWOMaterialBOQDetails(list,siteId,workOrderBean.getContractorId(),workOrderNo,"reviseWO","");
								String issuedMaterialForWorkArea="";String workDescName="";
								if(errorMessage.size()!=0){
									for (Entry<String, String>  errorMsg: errorMessage.entrySet()) {
										String tempStr=errorMsg.getValue();
										String tempArr[]=tempStr.split("@@")[1].split("##");
										if(tempStr.contains("Some Material Issued")){
											issuedMaterialForWorkArea=tempArr[2];
											workDescName=tempArr[3];
											//@@"+workDescId+"##"+inputBoxWorkAreaGroupId+"##"+issuedMaterialForWorkArea+"##"+workDescName
											errorListComment=new StringBuffer("Material is Issued for "+issuedMaterialForWorkArea+" quantity, current quantity is "+selectedArea[i]+" can not decrease material quantity for "+block_name[i]);
											if(!floor_name[i].equals("-")){	errorListComment.append(", "+floor_name[i]);}
											if(!flat_id[i].equals("-")){  	errorListComment.append(", "+flat_id[i]+"\n");}
											errorListComment.append(" WD "+workDescName+". Please check the WD before sumitting work order.");
											errorListForMaterialBOQ.add(errorListComment.toString());	
										}else if(tempStr.contains("can't revise work order")){
											issuedMaterialForWorkArea=tempArr[2];
											workDescName=tempArr[3];
											errorListComment=new StringBuffer("Material is Issued for "+issuedMaterialForWorkArea+" quantity for "+block_name[i]);
											if(!floor_name[i].equals("-")){	errorListComment.append(", "+floor_name[i]);}
											if(!flat_id[i].equals("-")){ errorListComment.append(", "+flat_id[i]+"\n");}
											errorListComment.append(" WD "+workDescName+". got new per unit quantity less than old per unit quantity, so unable to revise work order.");
											errorListForMaterialBOQ.add(errorListComment.toString());
										}
									}
									log.info("error in material area error list key is "+workOrderBean.getWorkOrderNo());
									request.setAttribute(workOrderBean.getWorkOrderNo(), errorListForMaterialBOQ);
								 	throw new Exception("error in material area"); 
								}
								
								List<Map<String, Object>>  listOfMaterialBOQ=	workControllerDao.loadMaterialBoqDetailsForWO(bean);
								List<Map<String, Object>>  productDtls=	new ArrayList<Map<String, Object>>();
								//productDtls is holding already inserted data in table (QS_WORKORDER_PRODUCT_DTLS)
								//listOfMaterialBOQ is holding the material BOQ Latest data 
								bean.setTempIssueAreaDetailsId(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
								if(workOrderStatus.equals("ModifyWorkOrder")){
									bean.setTempIssueAreaDetailsId(oldWOIssueAreaDetailsId[i]);
								}
								if(!WO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i].contains("A")){
									productDtls=workControllerDao.loadWorkOrderMaterialBOQ(bean);	
								}
								//here we have to compare already stored product details table(QS_WORKORDER_PRODUCT_DTLS)
								//and revised BOQ product details
								int index1=0;
								for (Map<String, Object> map : listOfMaterialBOQ) {
									bean = new WorkOrderBean();
									materialAmount+=Double.valueOf(selectedArea[i])*Double.valueOf(acceptedRate[i]);
									
									String newPerUnitQuantity=	map.get("PER_UNIT_QUANTITY").toString(); 
									String newPerUnitAmount=map.get("PER_UNIT_AMOUNT").toString();
									String newMaterialGroupId=map.get("MATERIAL_GROUP_ID").toString();
									String newMaterialUOM=map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString();
									String newMaterialGroupName=map.get("MATERIAL_GROUP_NAME").toString();
									
									bean.setMaterialGroupName(newMaterialGroupName);
									bean.setPerUnitQuantity(newPerUnitQuantity);
									bean.setPerUnitAmount(newPerUnitAmount);
									bean.setTempIssueAreaDetailsId(tempDetailresp.get(0).toString());
									bean.setMaterialGroupId(newMaterialGroupId);
									bean.setUnitsOfMeasurement1(newMaterialUOM);
									bean.setSelectedArea(selectedArea[i].trim());
									Double newtotalQuantity=Double.valueOf(bean.getSelectedArea())*Double.valueOf(newPerUnitQuantity);
									Double newtotalAmount=Double.valueOf(bean.getSelectedArea())*Double.valueOf(newPerUnitAmount);
									
									bean.setTotalQuantity(newtotalQuantity.toString());
									bean.setTotalAmount(newtotalAmount.toString());
									bean.setWorkAreaId(workAreaId[i]);
									newMaterialProductDetail.put(newMaterialGroupId+"##"+newMaterialUOM,bean);
									
									if(productDtls.size()!=0){//here processing all the existing data of work order before revising
										//(productDtls) this data is already exist in work order 
										for (int j = 0; j < productDtls.size(); j++) {
											Map<String, Object> workOrderProdList=productDtls.get(j);
											
											String woMaterialGroupId=workOrderProdList.get("MATERIAL_GROUP_ID").toString();
											String woMateralGroupName=workOrderProdList.get("MATERIAL_GROUP_NAME").toString();
											String woMaterialUOM=workOrderProdList.get("UOM").toString();
											String woPerUnitQuantity=workOrderProdList.get("PER_UNIT_QUANTITY").toString(); 
											String woPerUnitAmount=workOrderProdList.get("PER_UNIT_AMOUNT").toString();
											String woTotalQuantity=workOrderProdList.get("TOTAL_QUANTITY").toString();
											String woTotalAmount=workOrderProdList.get("TOTAL_AMOUNT").toString();
											//holding the unique data in oldMaterialProductDetail
											//here no duplicate data will be get entered
											//this is holding material name and UOM 
											if(!oldMaterialProductDetail.containsKey(woMaterialGroupId+"##"+woMaterialUOM)){
												WorkOrderBean woBean = new WorkOrderBean();
												woBean.setPerUnitQuantity(woPerUnitQuantity);
												woBean.setPerUnitAmount(woPerUnitAmount);
												woBean.setMaterialGroupId(woMaterialGroupId);
												woBean.setUnitsOfMeasurement1(woMaterialUOM);
												woBean.setTotalQuantity(woTotalQuantity.toString());
												woBean.setTotalAmount(woTotalAmount.toString());
												woBean.setMaterialGroupName(woMateralGroupName);
												oldMaterialProductDetail.put(woMaterialGroupId+"##"+woMaterialUOM,woBean);
											}
											//checking here changed per unit quantity and amount and maintaining log's in DB
											//so we can come to know which data has been changed
											if(woMaterialGroupId.equals(newMaterialGroupId)&&woMaterialUOM.equals(newMaterialUOM)){
												if(!newPerUnitQuantity.equals(woPerUnitQuantity)){
													strChangedDetail=new StringBuffer("Material per unit quantity, actual quantity "+woPerUnitQuantity+" changed to "+newPerUnitQuantity+" for group name "+woMateralGroupName+"\n@@");
													strChangedDetail.append(woPerUnitQuantity+"##"+newPerUnitQuantity+"##"+woMaterialGroupId+"##"+woMaterialUOM+"##"+woMateralGroupName);
													modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list 
												} if(!newPerUnitAmount.equals(woPerUnitAmount)){
													strChangedDetail=new StringBuffer("Material per unit amount, actual amount "+woPerUnitAmount+" changed to "+newPerUnitAmount+" for group name "+woMateralGroupName+"\n@@");
													strChangedDetail.append(woPerUnitAmount+"##"+newPerUnitAmount+"##"+woMaterialGroupId+"##"+woMaterialUOM+"##"+woMateralGroupName);
													modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list
												}/* if(newtotalQuantity.equals(woTotalQuantity)){
													strChangedDetail=new StringBuffer("Material total qnatity, actual quantity "+woTotalQuantity+" changed to "+newtotalQuantity+" for group name "+woMateralGroupName+"\n@@");
													strChangedDetail.append(woTotalQuantity+"##"+newtotalQuantity+"##"+woMaterialGroupId+"##"+woMaterialUOM+"##"+woMateralGroupName);
													modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list
												} if(newtotalAmount.equals(woTotalAmount)){
													strChangedDetail=new StringBuffer("Material total amount, actual amount "+woTotalAmount+" changed to "+newtotalAmount+" for group name "+woMateralGroupName+"\n@@");
													strChangedDetail.append(woTotalAmount+"##"+newtotalAmount+"##"+woMaterialGroupId+"##"+woMaterialUOM+"##"+woMateralGroupName);
													modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list
												}*/
											}
										}
								}
								 listOfMaterialBoq.add(bean);
								}
								WorkOrderBean woBean = new WorkOrderBean();
								//checking here is any material BOQ product details added or not AND deleted or not 
								//if added or deleted maintain the log
								//product data removed
								 for (Entry<String , WorkOrderBean>  oldMaterialData: oldMaterialProductDetail.entrySet()) {
									if(!newMaterialProductDetail.containsKey(oldMaterialData.getKey())){
										woBean=oldMaterialData.getValue();//this bean material product details like per unit quantity and amount
										strChangedDetail=new StringBuffer("Material product removed group name "+woBean.getMaterialGroupName()+"@@"+oldMaterialData.getKey());
										modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list
									}
								 }
								 
								 //product data added if oldMaterialProductDetail don't have the info about the new material product
								 //we are maintaining log's that new material product added
								 if(productDtls.size()!=0)
								 for (Entry<String, WorkOrderBean> newMaterialData : newMaterialProductDetail.entrySet()) {
									 if(!oldMaterialProductDetail.containsKey(newMaterialData.getKey())){
										 woBean=newMaterialData.getValue();//this bean material product details like per unit quantity and amount
										 strChangedDetail=new StringBuffer("Material product added group name "+woBean.getMaterialGroupName()+"@@"+newMaterialData.getKey());
											modifiedMaterialRecordsList.add(strChangedDetail.toString());//adding the modification detail in list
									} 
								}
								//inserting all the rows using spring JDBC batch update
								int result=	workControllerDao.insertDataIntoWorkOrderTempProductDtls(listOfMaterialBoq,"insert");
								//recordType[i].equals("MATERIAL") condition is ending here 
							}else{
								labourAmount+=Double.valueOf(selectedArea[i])*Double.valueOf(acceptedRate[i]);
							}
						}
					}
					actionType="";
					//inserting all the deleted row's  of PEICE WORK
					//all deleted row data storing only once at last
					for (String strModifyComment : listOfDeletedRowRecords) {
						changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
						changedWorkOderDetail.setTypeOfWork("PIECEWORK");
						actualWorkOderDetail.setTypeOfWork("PIECEWORK");
						actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
						workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueDetailsId, nextApprovelEmpId, "NEW", user_id,strModifyComment);
						listOfDeletedRowRecords=new ArrayList<String>();
						isWorkOrderChanged=true;
					}
					//inserting all the modified records of PEICE WORK
					//storing all the modification details only once while revising work order 
					//storing actual details and changed details
					if(listOfModifiedRecords.size()!=0){
						for (String strModifyComment : listOfModifiedRecords) {
							isWorkOrderChanged=true;
							changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
							changedWorkOderDetail.setTypeOfWork("PIECEWORK");
							actualWorkOderDetail.setTypeOfWork("PIECEWORK");
							if(strModifyComment.contains("Modified Accepted")){
								actionType="EDIT";
								String temp=strModifyComment.split("@@")[1];
								String str[]=temp.split("##");
								actualWorkOderDetail.setSelectedArea(str[0]);
								changedWorkOderDetail.setSelectedArea(str[1]);
								actualWorkOderDetail.setWorkAreaId(str[2]);
								changedWorkOderDetail.setWorkAreaId(str[2]);
								actualWorkOderDetail.setBoqRecordType(str[3]);
								changedWorkOderDetail.setBoqRecordType(str[3]);
								strModifyComment=strModifyComment.split("@@")[0];
							}else if(strModifyComment.contains("Modified quantity")){
								actionType="EDIT";
								String temp=strModifyComment.split("@@")[1];
								String str[]=temp.split("##");
								actualWorkOderDetail.setSelectedArea(str[0]);
								changedWorkOderDetail.setSelectedArea(str[1]);
								actualWorkOderDetail.setWorkAreaId(str[2]);
								changedWorkOderDetail.setWorkAreaId(str[2]);
								actualWorkOderDetail.setBoqRecordType(str[3]);
								changedWorkOderDetail.setBoqRecordType(str[3]);
								strModifyComment=strModifyComment.split("@@")[0];
							}else if(strModifyComment.contains("Added Work")){
								actionType="NEW";
								String temp=strModifyComment.split("@@")[1];
								String str[]=temp.split("##");
								actualWorkOderDetail.setSelectedArea(str[0]);
								changedWorkOderDetail.setSelectedArea(str[0]);
								actualWorkOderDetail.setWorkAreaId(str[1]);
								changedWorkOderDetail.setWorkAreaId(str[1]);
								actualWorkOderDetail.setBoqRecordType(str[2]);
								changedWorkOderDetail.setBoqRecordType(str[2]);
								strModifyComment=strModifyComment.split("@@")[0];
							}else if(strModifyComment.contains("Deleted Work")){
								actionType="DEL";
								String temp=strModifyComment.split("@@")[1];
								String str[]=temp.split("##");
								actualWorkOderDetail.setSelectedArea(str[0]);
								changedWorkOderDetail.setSelectedArea(str[0]);
								actualWorkOderDetail.setWorkAreaId(str[1]);
								changedWorkOderDetail.setWorkAreaId(str[1]);
								actualWorkOderDetail.setBoqRecordType(str[2]);
								changedWorkOderDetail.setBoqRecordType(str[2]);
								strModifyComment=strModifyComment.split("@@")[0];
							}else if(strModifyComment.contains("Added new")){
								actionType="NEW";
							}
							actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempDetailresp.get(0).toString());
							//holding the changed details primary key for table(QS_WORKORDER_QTY_CHG_DTLS)
							int  changedDtlsPK=workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueDetailsId, nextApprovelEmpId, actionType, user_id,strModifyComment);
							if(modifiedMaterialRecordsList.size()!=0){
								isWorkOrderChanged=true;
								insertMaterialChangedDetails(modifiedMaterialRecordsList,changedDtlsPK);
							}
						}
					}//labor modification condition
					else{
						
						/*if(modifiedMaterialRecordsList.size()!=0){
							isWorkOrderChanged=true;
							insertMaterialChangedDetails(modifiedMaterialRecordsList,changedDtlsPK);
						}*/
					}
					workOrderRowDetailsList.add(printWO);
				} // for loop
			} // if
			if(isWorkOrderChanged==false&&!workOrderStatus.equals("ModifyWorkOrder")){
				throw new Exception("not changed work order");
			}
			
			if(nextApprovelEmpId.equals("END")&&!isSaveOrUpdateOperation.equals("Draft Work Order")){
				  //response2 = workControllerDao.insertCompletedWorkOrder(workOrderBean, workOrderTMPIssurPK,workOrderBean.getSiteWiseWONO(), nextApprovelEmpId);
				  workControllerDao.updatePendingWorkOrder(workOrderBean, workOrderTMPIssurPK,workOrderBean.getSiteWiseWONO(), nextApprovelEmpId,"insert");
			}
			if(workOrderStatus!=null&&workOrderStatus.equals("ModifyWorkOrder")){
			 	//response2=workControllerDao.updateChangedDetailsId(tempIssueIdForModifyWO);
			}
			//adding work order row's in model object for the print page 
			model.addAttribute("workOrderCreationList", workOrderRowDetailsList);
			String s=null;
			//s.trim();
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			log.info("Exception got while processing RowNumber "+ holdRowNumber);
			transactionManager.rollback(status);
			e.printStackTrace();		
			isSendMail = false;
			//handling explicitly thrown exception
			if(e.getMessage()!=null&&e.getMessage().equals("error in material area")){
				response="error in material area";
				return response;
			}
			if(e.getMessage()!=null&&e.getMessage().equals("not changed work order")){
				response="not changed work order";
				return response;
			}
			
			WriteTrHistory.write("Tr_Completed");
			response = "failed";

		}
		if (isSendMail&&!isSaveOrUpdateOperation.equals("Draft Work Order")) {
			StringBuffer emailSubject = new StringBuffer("");
			emailSubject.append("Regarding Approval of Work Order.");
			constructEmailContent(workOrderBean,request,null,optionalCCmails);
		}
		return response;	
	}
	
	/**
	 * @description this code is for only inserting the modification details that's it nothing more in this code
	 * adding action type edited or deleted
	 * @param modifiedMaterialRecordsList this list object holding modification details
	 * @param changedDtlsPK this variable holding primary key of the work order changed details table 
	 * @return
	 */
	private int insertMaterialChangedDetails(List<String> modifiedMaterialRecordsList, int changedDtlsPK) {
		WorkOrderBean changedWorkOderDetail=new WorkOrderBean();
		WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
		String actionType="";
		int result=0;
		//inserting material changed details
		for (String materialModified : modifiedMaterialRecordsList) {
			changedWorkOderDetail=new WorkOrderBean();
			actualWorkOderDetail=new WorkOrderBean();
	
			//checking specific string in (materialModified) and based on that adding the actual value and changed value
			if(materialModified.contains("unit quantity")){
				actionType="EDIT";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setPerUnitQuantity(str[0]);//actual value
				changedWorkOderDetail.setPerUnitQuantity(str[1]);//changed value
				actualWorkOderDetail.setMaterialGroupId(str[2]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[3]);
				
				materialModified=materialModified.split("@@")[0];
			}else if(materialModified.contains("unit amount")){
				actionType="EDIT";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setPerUnitAmount(str[0]);//actual value
				changedWorkOderDetail.setPerUnitAmount(str[1]);//changed value
				actualWorkOderDetail.setMaterialGroupId(str[2]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[3]);
				materialModified=materialModified.split("@@")[0];
			}else if(materialModified.contains("total qnatity")){
				actionType="EDIT";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setTotalQuantity(str[0].toString());//actual value
				changedWorkOderDetail.setTotalQuantity(str[1].toString());//changed value
				actualWorkOderDetail.setMaterialGroupId(str[2]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[3]);
				materialModified=materialModified.split("@@")[0];
			}else if(materialModified.contains("total amount")){
				actionType="EDIT";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setTotalAmount(str[0].toString());
				changedWorkOderDetail.setTotalAmount(str[1].toString());
				actualWorkOderDetail.setMaterialGroupId(str[2]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[3]);
				materialModified=materialModified.split("@@")[0];
			}else if(materialModified.contains("Product Added")){
				actionType="New";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setMaterialGroupId(str[0]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[1]);
				materialModified=materialModified.split("@@")[0];
			}else if(materialModified.contains("Product Removed")){
				actionType="DEL";
				String temp=materialModified.split("@@")[1];
				String str[]=temp.split("##");
				actualWorkOderDetail.setMaterialGroupId(str[0]);
				actualWorkOderDetail.setUnitsOfMeasurement1(str[1]);
				materialModified=materialModified.split("@@")[0];
			}
			actualWorkOderDetail.setWoChangedDetailsId(changedDtlsPK);
			changedWorkOderDetail.setWoChangedDetailsId(changedDtlsPK);
			changedWorkOderDetail.setRemarks(materialModified);
			result=workControllerDao.insertWOmaterialChangedDetails(actualWorkOderDetail,changedWorkOderDetail,materialModified,changedDtlsPK,actionType);	
		}
		return result;
	}

	/**
	 * this method is used for inserting the revise NMR work order 
	 * revise work order means modification in existing work order
	 */
	@Override
	public String doReviseNMRWorkOrderEntry(WorkOrderBean workOrderBean, HttpServletRequest request, Model model,
			String siteId, String user_id, HttpSession session) {
		boolean isWorkOrderChanged=false;
		String response ="";
		String siteName = "";
		String strUserName =  "";
		String optionalCCmails="";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		boolean isSendMail = true;
		boolean isThisWOSaveOperation=false;
		//String workOrderNo = workOrderBean.getSiteWiseWONO();
		String nextApprovelEmpId=workOrderBean.getApproverEmpId();
		String workOrderCreateEmpId = user_id;
		List<WorkOrderBean> workOrderDetailsList=new ArrayList<WorkOrderBean>();
		List<String> storeDuplicateworkOrderDetailsList=new ArrayList<String>();
		List<Number> reponse1 = null;
		String workOrderTMPIssurPK = "";String actionType="";
		String isSaveOrUpdateOperation=workOrderBean.getIsSaveOrUpdateOperation();
		String siteWiseTempWorkOrderNo = workOrderBean.getSiteWiseWONO();
		
		String workOrderStatus=workOrderBean.getWorkOrderStatus()==null?"":workOrderBean.getWorkOrderStatus();
		workOrderBean.setWorkOrderStatus(workOrderStatus);
		//String UnitsOfMeasurement1=request.getParameter("UnitsOfMeasurement" + 1)==null?"":request.getParameter("UnitsOfMeasurement" + 1);
		//String[] termsAndConditions=request.getParameterValues("termsAndCOnditions");
		int result=0;
		try {
			 strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			 siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			 String revisedWorkOrderNumber=request.getParameter("reviseWorkOrderNumber1")==null?"":request.getParameter("reviseWorkOrderNumber1");
			String boqNo= request.getParameter("boqNo")== null ? "" :request.getParameter("boqNo") ;
			String totalAmountOfWorkOrder=request.getParameter("TotalAmountOfWorkOrder")==null?"":request.getParameter("TotalAmountOfWorkOrder");
			String typeOfWork=request.getParameter("typeOfWork")==null?"":request.getParameter("typeOfWork");
			workOrderBean.setTotalAmount1(totalAmountOfWorkOrder);
			workOrderBean.setBoqRecordType("LABOR");
			if(boqNo!=null){
				workOrderBean.setBoqNo(boqNo);
			}
			workOrderBean.setSiteName(siteName);
			workOrderBean.setTypeOfWork(typeOfWork+"@@NMR");
			
			if(!workOrderStatus.equals("ModifyWorkOrder")){
				workOrderBean.setWorkOrderNo(revisedWorkOrderNumber);
			}

			//workOrderBean.setFromEmpName(strUserName);
			workOrderBean.setUserId(user_id);
			synchronized (this) {
				//inserting revise NMR work order main data like work order number, name, date
				reponse1 = workControllerDao.insertReviseWorkOrderDetail(workOrderBean, workOrderCreateEmpId);					
			}
		
			WriteTrHistory.write("Tr_Opened in InCr_indCre, ");
			String workOrderResult = "";
				
			try {
				workOrderResult = reponse1.get(0).toString();
				workOrderTMPIssurPK = reponse1.get(1).toString();
				workOrderBean.setQS_Temp_Issue_Id(workOrderTMPIssurPK);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//String workOrderDate=workOrderBean.getWorkOrderDate();
			
			workOrderBean.setSiteName(siteName);
			if(isSaveOrUpdateOperation.equals("Draft Work Order")){
				actionType="DF";//draft record
				isThisWOSaveOperation=true;
			}else if(isSaveOrUpdateOperation.equals("Approve")){
				actionType="A";
			}else if(isSaveOrUpdateOperation.equals("Submit")){
				actionType="C";
			}else if(isSaveOrUpdateOperation.equals("Revise")){
				actionType="C";
			}else if(isSaveOrUpdateOperation.equals("Modify WorkOrder")){actionType="C";}else {actionType="C";}	
			//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "C" means Create 
			int response2 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean,actionType);
			if(workOrderStatus.equals("ModifyWorkOrder")){
				try {
					 //cloning the object so main object should not get any affect so we can use it with actual values
					WorkOrderBean	orderBean = (WorkOrderBean) workOrderBean.clone();
					orderBean.setPurpose("Temporary work order number "+siteWiseTempWorkOrderNo+" changed to "+workOrderBean.getSiteWiseWONO());
						response2 = workControllerDao.inserWorkOrderCreationDetail(orderBean,"M");
					} catch (CloneNotSupportedException e1) {
						log.info(e1.getMessage());
					}
			}

			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			String [] noOfRowToProcess= request.getParameterValues("dispplayedRows");
			String termsAndConditions[]=request.getParameterValues("termsAndCOnditions");
			//optionalCCmails=request.getParameter("optionalCCmails");
			//String 	subject=request.getParameter("subject");
			
			if(termsAndConditions!=null){
				//Inserting terms and conditions
				result=workControllerDao.insertTermsAndConditions(workOrderBean,workOrderTMPIssurPK,termsAndConditions);
			}
			List<Map<String, String>> termasAndCon=new ArrayList<Map<String, String>>();
			if(termsAndConditions!=null)
			for (String string : termsAndConditions) {
				Map<String, String > map=new HashMap<String, String>();
				string=string.trim();
				if(string.length()!=0){
					map.put("TERMS_CONDITION_DESC", string);
					termasAndCon.add(map);
				}
			}
			//adding terms and condition in model object so we can print this terms and condition in work order print page
			model.addAttribute("listTermsAndCondition", termasAndCon);
			List<String> listOfDeletedRowRecords=new ArrayList<String>();
			List<Integer> tempDetailresp = new ArrayList<Integer>();
			
			//this is processing number of rows created in Workorder
			if (noOfRowToProcess != null && noOfRowToProcess.length > 0) {
				for (String num : noOfRowToProcess) {
					WorkOrderBean printWO=new WorkOrderBean();
					num = num.trim();
					
					String WO_MajorHead1= request.getParameter("WO_MajorHead" + num)==null?"":request.getParameter("WO_MajorHead" + num);
					String WO_MinorHead1 =request.getParameter("WO_MinorHead" + num)==null?"":request.getParameter("WO_MinorHead" + num);
					String woDesc= request.getParameter("WO_Desc" + num)==null?"":request.getParameter("WO_Desc" + num);
					String UnitsOfMeasurement1=request.getParameter("UnitsOfMeasurement" + num)==null?"":request.getParameter("UnitsOfMeasurement" + num);
					String actualQuantity = request.getParameter("actualQuantity" + num)==null?"": request.getParameter("actualQuantity" + num);
					String changedQuantity = request.getParameter("Quantity" + num)==null?"": request.getParameter("Quantity" + num);
					String actualAcceptedRate= request.getParameter("actualAcceptedRate" + num)==null?"":  request.getParameter("actualAcceptedRate" + num);
					String changedAcceptedRate = request.getParameter("AcceptedRate" + num)==null?"":  request.getParameter("AcceptedRate" + num);
					String TotalAmount = request.getParameter("TotalAmount" + num);
					String note = request.getParameter("comments" + num)==null?"":request.getParameter("comments" + num).trim();
					String woManualDesc = request.getParameter("scopeOfWOrk" + num);
					
					String actualWORowId=request.getParameter("actualWORowId" + num)==null?"":request.getParameter("actualWORowId" + num);
				
					try {
						//this printWO object is for printing the names in work order print page
						printWO.setWO_MajorHead1(WO_MajorHead1.split("\\$")[1].replace("#","\""));
						printWO.setWO_MinorHead1(WO_MinorHead1.split("\\$")[1]);
						printWO.setWO_Desc1(woDesc.split("\\$")[1]);
						printWO.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[1]);
						printWO.setwOManualDescription(woManualDesc);
					} catch (Exception e) {
						e.printStackTrace();
					}
					printWO.setTotalAmount1(TotalAmount);
					printWO.setQuantity(changedQuantity);
					Double acceptedRate1=0.0;
					
					//changedWorkOderDetail object holding the values that has been changed while revising NMR work order
					WorkOrderBean changedWorkOderDetail=new WorkOrderBean();
					//actualWorkOderDetail object holding the values that are not being changed that are constant
					WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
				
					List<String> listOfModifiedRecords=new ArrayList<String>();
					
					@SuppressWarnings("unused")
					boolean isRowDataChanged = false;
					String DeleteColVal = request.getParameter("isDelete" + num)==null?"":request.getParameter("isDelete" + num);
					boolean isThisNewRow=request.getParameter("isNewRowAdded" + num)==null?false:Boolean.valueOf(request.getParameter("isNewRowAdded" + num));
					String strChangedComments="";
					@SuppressWarnings("unused")
					boolean isDelete = false;
					if (DeleteColVal.equals("d")) {
						isDelete = true;
						isWorkOrderChanged=true;
						strChangedComments=strUserName +" - WD  (\""+printWO.getWO_MajorHead1()+","+printWO.getWO_MinorHead1()+","+printWO.getWO_Desc1()+"\") deleted.";
						listOfDeletedRowRecords.add(strChangedComments);
						
						//this CODE is for if last row deleted then the rest of code will not execute as we are writing the continue keyword in loop
						//so we are inserting last row deleted data here only means inserting changed details data
						if(num.equals(noOfRowToProcess[noOfRowToProcess.length-1]))
							for (String strModifyComment : listOfDeletedRowRecords) {
								changedWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
								changedWorkOderDetail.setTypeOfWork("NMR");
								actualWorkOderDetail.setTypeOfWork("NMR");
								actualWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
								workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, "", nextApprovelEmpId, "M", user_id,strModifyComment);
								listOfDeletedRowRecords=new ArrayList<String>();
							}
						//if the row is deleted then make loop to next iteration						
						continue;
					}
					
					if(isThisNewRow){
						//if this is the new row while revising work order
						strChangedComments=strUserName +" - Added new WD (\""+printWO.getWO_MajorHead1()+","+printWO.getWO_MinorHead1()+","+printWO.getWO_Desc1()+"\").";
						//isWorkOrderChanged=true;
						listOfModifiedRecords.add(strChangedComments);
					}else if(!changedQuantity.equals(actualQuantity)&&!changedAcceptedRate.equals(actualAcceptedRate)&&(changedAcceptedRate.length()!=0&&actualAcceptedRate.length()!=0)){
						//if while revising work order quantity and rate changed
						strChangedComments= strUserName + " - Modifed quantity "+actualQuantity+" to "+ changedQuantity +" and accepted rate "+actualAcceptedRate+" to "+ changedAcceptedRate+" for "+printWO.getWO_MinorHead1()+", "+printWO.getWO_Desc1()+"<br>";
						isRowDataChanged = true;///isWorkOrderChanged=true;
						listOfModifiedRecords.add(strChangedComments);
					}else if (!changedQuantity.equals(actualQuantity)) {
						strChangedComments= strUserName + " - Modifed quantity "+actualQuantity+"  to "+ changedQuantity
								+" for "+printWO.getWO_MinorHead1()+", "+printWO.getWO_Desc1()+"<br>";
						isRowDataChanged = true;//isWorkOrderChanged=true;
						listOfModifiedRecords.add(strChangedComments);
					}else if (!changedAcceptedRate.equals(actualAcceptedRate)&&(changedAcceptedRate.length()!=0&&actualAcceptedRate.length()!=0)) {
							isRowDataChanged = true;//isWorkOrderChanged=true;
						strChangedComments += strUserName + " - Modifed accepted rate "+actualAcceptedRate+" to "+ changedAcceptedRate
									+" for "+printWO.getWO_MinorHead1()+", "+printWO.getWO_Desc1()+"<br>";
						listOfModifiedRecords.add(strChangedComments);
					}
				printWO.setAcceptedRate1(String.valueOf(acceptedRate1));
					
				WorkOrderBean tempIssueDetailsData=new WorkOrderBean();
				tempIssueDetailsData.setWO_MajorHead1(WO_MajorHead1.split("\\$")[0]);//major head desc id
				tempIssueDetailsData.setWO_MinorHead1(WO_MinorHead1.split("\\$")[0]);//major head desc id
				tempIssueDetailsData.setWO_Desc1(woDesc.split("\\$")[0]);//work desc id
				String woRowCode="";
				log.info(workOrderBean.getWorkOrderNo()+" "+tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
			
			//	if (!DeleteColVal.equals("d"))
				if(!storeDuplicateworkOrderDetailsList.contains(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
					storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
					woRowCode="MH-"+workOrderBean.getWorkOrderNo().split("/")[3]+"-1";
					tempIssueDetailsData.setWoRowCode(woRowCode);
				}else{
					int countSameObjects=1;
					for (String sameObjectCOunt : storeDuplicateworkOrderDetailsList) {
						if(sameObjectCOunt.equals(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
							countSameObjects++;
						}
					}
					woRowCode="MH-"+workOrderBean.getWorkOrderNo().split("/")[3]+"-"+countSameObjects;
					tempIssueDetailsData.setWoRowCode(woRowCode);
					storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
					if(!woRowCode.equals(actualWORowId)){
						log.debug(actualWORowId+" "+woRowCode);
					}
				}
				
				tempIssueDetailsData.setUnitsOfMeasurement1(UnitsOfMeasurement1.split("\\$")[0]); //uom id
				tempIssueDetailsData.setTotalAmount1(TotalAmount.trim());
				tempIssueDetailsData.setQuantity1(changedQuantity.trim());
				tempIssueDetailsData.setwOManualDescription(woManualDesc);
				if(note.length()!=0){
					note=strUserName+" - "+note;
				}
				tempIssueDetailsData.setComments1(note);
				//inserting NMR row's Details 
				tempDetailresp = workControllerDao.insertWorkOrderTempIssueDetails(tempIssueDetailsData,workOrderTMPIssurPK);
				String tempIssueDetailsId=String.valueOf(tempDetailresp.get(0));
			
				//adding object for print page
				workOrderDetailsList.add(printWO);
				
				//maintaining the modified work description log in DB
				for (String strModifyComment : listOfModifiedRecords) {
					isWorkOrderChanged=true;
					changedWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
					changedWorkOderDetail.setTypeOfWork("NMR");
					actualWorkOderDetail.setTypeOfWork("NMR");
					actualWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
					//inserting all the changed details of NMR work order
					workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueDetailsId, nextApprovelEmpId, "NEW", user_id,strModifyComment);
					//listOfModifiedRecords making the empty with creating new object, because we need to insert again next row changed details
					listOfModifiedRecords=new ArrayList<String>();
				}
				//maintaining the deleted work description log in DB
				for (String strModifyComment : listOfDeletedRowRecords) {
					isWorkOrderChanged=true;
					changedWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
					changedWorkOderDetail.setTypeOfWork("NMR");
					actualWorkOderDetail.setTypeOfWork("NMR");
					actualWorkOderDetail.setQS_Temp_Issue_Id(tempDetailresp.get(0).toString());
					workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, "", nextApprovelEmpId, "NEW", user_id,strModifyComment);
					listOfDeletedRowRecords=new ArrayList<String>();
				}
			  } // for loop
			} // if
			
			if(isWorkOrderChanged==false&&!workOrderStatus.equals("ModifyWorkOrder")){
				throw new Exception("not changed work order");
			}
			
			//adding workOrderDetailsList object in model object for work order print page after work order created successfully. 
			model.addAttribute("workOrderCreationList", workOrderDetailsList);
			//String s=null;
			//s.trim();
			transactionManager.commit(status);

			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			isSendMail = false;
			if(e.getMessage()!=null&&e.getMessage().equals("not changed work order")){
				response="not changed work order";
				return response;
			}
		}
		if (isSendMail) {
			StringBuffer emailSubject = new StringBuffer("");
			emailSubject.append("Regarding Approval of Work Order.");
			constructEmailContent(workOrderBean,request,null,optionalCCmails);
		}
		return response;	
	}
	
	@Override
	public List<WorkOrderBean> getPendingWorkOrder(String user_id, String siteId,String statusType) {
		return workControllerDao.getPendingWorkOrder(user_id, siteId,statusType);
	}

	@Override
	public boolean checkWorkOrderNumberIsValidForEmployee(String workOrderNo, String user_id, boolean status, String statusType) {
		return workControllerDao.checkWorkOrderNumberIsValidForEmployee(workOrderNo, user_id,status,statusType);
	}

	@Override
	public String getWorkOrderNumber(String siteWiseWorkOrderNo, int site_Id, String user_id) {
		return workControllerDao.getWorkOrderNumber(siteWiseWorkOrderNo, site_Id, user_id);
	}

	@Override
	public WorkOrderBean getWorkOrderDetailsByWorkOrderId(String workOrderNo) {
		return workControllerDao.getWorkOrderDetailsByWorkOrderId(workOrderNo);
	}

	@Override
	public List<WorkOrderBean> getCreatedWorkOrderDetails(WorkOrderBean workOrderBean) {
		return workControllerDao.getCreatedWorkOrderDetails(workOrderBean);
	}

	@Override
	public List<Map<String, Object>> loadWOAreaMappingByWorkOrderNo(String workOrderNo, String siteId,
			String selectALLData, String operType, String mesurment, String unitsOfMeasurementId, String typeOfWork) {

		return workControllerDao.loadWOAreaMappingByWorkOrderNo(workOrderNo, siteId, selectALLData,operType,mesurment,unitsOfMeasurementId,typeOfWork);
	}
	
	@Override
	public List<Map<String, Object>> loadWOAreaMappingForReviseByWorkOrderNo(WorkOrderBean bean) {
		return workControllerDao.loadWOAreaMappingForReviseByWorkOrderNo(bean);
	}
	
/*	@Override
	public String updateTempWorkOrder(WorkOrderBean workOrderBean, HttpSession session, HttpServletRequest request) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_appInd, ");
		boolean isSendMail = true;
		List<WorkOrderBean> workOrderBeans=new ArrayList<WorkOrderBean>();
		return null;
	}*/
	
	/**
	 * @description approveWorkOrderFromMail method is used for approve mail from email
	 */
	@Override
	public String approveWorkOrderFromMail(WorkOrderBean workOrderBean, HttpServletRequest request, HttpSession session) {
		String response = "";
		String optionalCCmails = "";
		String siteWiseWorkOrderNo = "";
		String tempWorkOrderIssueId = "";
		String approverEmpId = "";
		int result = 0;
		boolean isSendMail = true;
		BigDecimal totalAmount = null;
		List<Map<String, Object>> listOfVerifiedEmpNames = null;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order mail approval, ");
		
		Map<String, String> approvingWorkOrderNumbers = null;
		try {
			//this object holding approved work order numbers, if work order approved successfully then we will remove the work order key from object
 			approvingWorkOrderNumbers = (Map<String, String>) session.getServletContext().getAttribute("ApprovingWorkOrderNumber");
			if (approvingWorkOrderNumbers == null) {
				approvingWorkOrderNumbers = new HashMap<String, String>();
			}
			String strData ="";
			strData = workOrderBean.getSiteWiseWONO().replace(" ", "+");
			siteWiseWorkOrderNo = AESDecrypt.decrypt("AMARAVADHIS12345", strData);
			strData = workOrderBean.getQS_Temp_Issue_Id().replace(" ", "+");
			tempWorkOrderIssueId = AESDecrypt.decrypt("AMARAVADHIS12345", strData);
			strData = workOrderBean.getApproverEmpId().replace(" ", "+");
			approverEmpId = AESDecrypt.decrypt("AMARAVADHIS12345", strData);

			workOrderBean.setUserId(approverEmpId);// approver employee id is nothing but current user id
			workOrderBean.setQS_Temp_Issue_Id(tempWorkOrderIssueId);
			workOrderBean.setSiteWiseWONO(siteWiseWorkOrderNo);
			workOrderBean.setApproverEmpId(null);
			//validating work order is valid or not for this user, if not valid throw message to user
			boolean isValid = workControllerDao.checkWorkOrderNumberIsValidForEmployee(tempWorkOrderIssueId,
					approverEmpId, false, workOrderBean.getWorkOrderStatus());

			
			// loading the next level approver using this method with the help of bean object and user_id
			workOrderBean = workControllerDao.getWorkOrderFromAndToDetails(workOrderBean.getUserId(), workOrderBean);
			if (workOrderBean.getApproverEmpId() == null || workOrderBean.getApproverEmpId().equals("-")) {
				optionalCCmails=UIProperties.validateParams.getProperty(workOrderBean.getSiteId()+"_WORKORDER_CCMAILS") == null ? "" : UIProperties.validateParams.getProperty(workOrderBean.getSiteId()+"_WORKORDER_CCMAILS").toString();
				log.info(" final data... last approval of Work Order");
				workOrderBean.setApproverEmpId("END");
			}
			
			// loading data from temporary table, because this is the e-mail
			// approval so we need some data which is not coming from mail
			// so loading from table, that is require to put in email
			WorkOrderBean bean = workControllerDao.getWorkOrderDetailsByWorkOrderId(workOrderBean.getQS_Temp_Issue_Id());
			workOrderBean.setWorkOrderDate(bean.getWorkOrderDate());
			workOrderBean.setTypeOfWork(bean.getTypeOfWork());
			//totalAmount = new BigDecimal(bean.getLaborWoAmount()).setScale(2, RoundingMode.CEILING);
			workOrderBean.setLaborWoAmount(bean.getLaborWoAmount());
			workOrderBean.setWorkOrderName(bean.getWorkOrderName());
			workOrderBean.setSiteName(bean.getSiteName());
			workOrderBean.setContractorName(bean.getContractorName());
			workOrderBean.setWorkOrderNo(bean.getWorkOrderNo());
			workOrderBean.setRevision(bean.getRevision());
			workOrderBean.setOldWorkOrderNo(bean.getOldWorkOrderNo());
			workOrderBean.setContractorId(bean.getContractorId());

			if(!isValid){
				approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
				throw new RuntimeException("Work Order Number is Not Valid");
			}
			//Approving work order checking in session object, if the work order is exists in session object means this work order already approved, we can't approve this work order again 
			if (!approvingWorkOrderNumbers.containsKey(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId())) {
				approvingWorkOrderNumbers.put(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId(),workOrderBean.getWorkOrderNo());
			} else {
				approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
				throw new RuntimeException("got duplicate work order for approve");
			}
			session.getServletContext().setAttribute("ApprovingWorkOrderNumber", approvingWorkOrderNumbers);
			
			// inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "A" means approve
			result = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, "A");
			result = workControllerDao.updateWorkOrderDetail(workOrderBean);
			// loading work order created user names and designation and operation performed name
			listOfVerifiedEmpNames = workControllerDao.getWorkOrderVerifiedEmpNames(workOrderBean);
			approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			isSendMail = false;
			e.printStackTrace();
			if(e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")){
				throw new RuntimeException("Work Order Number is Not Valid");
			}
		}
		
		if (isSendMail) {
			constructEmailContent(workOrderBean,request,listOfVerifiedEmpNames,optionalCCmails);
		}
		
		return response;
	}
	
	@Override
	public String rejectOrModifyWorkOrderFromMail(WorkOrderBean workOrderBean, HttpServletRequest request, HttpSession session) {
		String response = "";
		String optionalCCmails = "";
		int result = 0;
		boolean isSendMail = true;
		BigDecimal totalAmount = null;
		List<Map<String, Object>> listOfVerifiedEmpNames = null;
		Map<String, String> reviseWorkOrderNumbers = null;
		Map<String, String> approvingWorkOrderNumbers =null;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in work order mail approval, ");
		try {
			
			reviseWorkOrderNumbers = (Map<String, String>) session.getAttribute("RevisedWorkOrderNumber");
			if (reviseWorkOrderNumbers == null) { reviseWorkOrderNumbers = new HashMap<String, String>(); }
			approvingWorkOrderNumbers = (Map<String, String>) session.getServletContext().getAttribute("ApprovingWorkOrderNumber");
			if (approvingWorkOrderNumbers == null) { approvingWorkOrderNumbers = new HashMap<String, String>(); }
			//workOrderBean.setApproverEmpId(null);
			workOrderBean = workControllerDao.getWorkOrderFromAndToDetails(workOrderBean.getUserId(), workOrderBean);
			if (workOrderBean.getApproverEmpId() == null || workOrderBean.getApproverEmpId().equals("-")) {
				optionalCCmails=UIProperties.validateParams.getProperty(workOrderBean.getSiteId()+"_WORKORDER_CCMAILS") == null ? "" : UIProperties.validateParams.getProperty(workOrderBean.getSiteId()+"_WORKORDER_CCMAILS").toString();
				log.info(" final data... last approval of Work Order");
				workOrderBean.setApproverEmpId("END");
			}
			// loading work order created user names and designation and operation performed name
			listOfVerifiedEmpNames = workControllerDao.getWorkOrderVerifiedEmpNames(workOrderBean);
			
			// loading data from temporary table, because this is the e-mail
			// approval so we need some data which is not coming from mail
			// so loading from table, that is require to put in email
			WorkOrderBean bean = workControllerDao.getWorkOrderDetailsByWorkOrderId(workOrderBean.getQS_Temp_Issue_Id());
			workOrderBean.setWorkOrderDate(bean.getWorkOrderDate());
			workOrderBean.setTypeOfWork(bean.getTypeOfWork());
			totalAmount = new BigDecimal(bean.getLaborWoAmount()).setScale(2, RoundingMode.CEILING);
			workOrderBean.setLaborWoAmount(String.valueOf(totalAmount));
			workOrderBean.setWorkOrderName(bean.getWorkOrderName());
			workOrderBean.setSiteName(bean.getSiteName());
			workOrderBean.setContractorName(bean.getContractorName());
			workOrderBean.setWorkOrderNo(bean.getWorkOrderNo());
			workOrderBean.setRevision(bean.getRevision());
			workOrderBean.setContractorId(bean.getContractorId());
			log.info("bean.getWorkorderFrom() "+bean.getWorkorderFrom()+" "+workOrderBean.getApproverEmpId());
			//if(workOrderBean.getApproverEmpId().equals("END")){
				workOrderBean.setWorkorderFrom(bean.getWorkorderFrom());
			//}
			log.info("bean.getWorkorderFrom() "+workOrderBean.getWorkorderFrom()+" "+workOrderBean.getApproverEmpId());
			boolean isValid = checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),
					workOrderBean.getUserId(), false, workOrderBean.getWorkOrderStatus());
			if(!isValid){
				throw new RuntimeException("Work Order Number is Not Valid");
			}

			//removing revised work order number from session if exist 
			if (!reviseWorkOrderNumbers.containsKey(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId())) {
				reviseWorkOrderNumbers.put(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId(),workOrderBean.getWorkOrderNo());
			} else {
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + workOrderBean.getSiteId() + "**");
				reviseWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
				throw new RuntimeException("Work Order Number is Not Valid");
			}
			session.setAttribute("RevisedWorkOrderNumber", reviseWorkOrderNumbers);
			//removing approving work order number from web application context if exist 
			
			if (!approvingWorkOrderNumbers.containsKey(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId())) {
				approvingWorkOrderNumbers.put(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId(),workOrderBean.getWorkOrderNo());
			} else {
				log.info("** Invalid temporary work order number " + workOrderBean.getSiteWiseWONO() + " and site id=" + workOrderBean.getSiteId() + "**");
				approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
				throw new RuntimeException("Work Order Number is Not Valid");
			}
			session.getServletContext().setAttribute("ApprovingWorkOrderNumber", approvingWorkOrderNumbers);
				
			if (workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")) {
				result = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, "M");
				result = workControllerDao.sendToModifyWorkOrder(workOrderBean);
			} else {
				// inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "A" means approve
				result = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, "R");
				result = workControllerDao.rejectWorkOrderFromMail(workOrderBean);
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
			reviseWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			isSendMail = false;
			e.printStackTrace();
			approvingWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
			reviseWorkOrderNumbers.remove(workOrderBean.getWorkOrderNo() + workOrderBean.getContractorId() + workOrderBean.getSiteId()+workOrderBean.getUserId());
			if(e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")){
				throw new RuntimeException("Work Order Number is Not Valid");
			}
		}

		if (isSendMail) {
			constructEmailContentForRejetAndModify(workOrderBean, request,listOfVerifiedEmpNames, optionalCCmails);
		}
		return response;
	}
	
	/**
	 * this method is for approving the work order details like 
	 * if it has modified any quantity or rate then we have to update the quantity and rate
	 * this method will use both NMR and PIECE WORK time
	 */
	@Override
	public String approveWorkOrderCreation(WorkOrderBean workOrderBean, HttpServletRequest request, HttpSession session,
			Model model, final String site_Id, final String user_id, MultipartFile[] files) {
		String response = "";
		String optionalCCmails="";
		String displayedRows[]; 
		String changedTC[];
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_appInd, ");
		boolean isSendMail = true;
		List<WorkOrderBean> workOrderBeans=new ArrayList<WorkOrderBean>();
		List<String> storeDuplicateworkOrderDetailsList=new ArrayList<String>();
		List<String> errorListForMaterialBOQ = new ArrayList<String>();
		List<Map<String, Object>> listOfVerifiedEmpNames = null;
		try {
			Double woTotalAmount = 0.0;
			Double materialAmount = 0.0;
			Double labourAmount = 0.0;
			boolean isThisWOSaveOperation = false;
			
			String changedWoMajorHead = "", actualwoMajorHead = "";
			String changedWO_MinorHead = "", actualWO_MinorHead = "";
			String changedWO_Desc = "", actualWO_Desc = "";
			String changedUnitsOfMeasurement = "", actualunitsOfMeasurement = "";

			String changedQuantity = "", actualQuantity = "";
			String changedAcceptedRate = "", actualAcceptedRate = "";
			String changedTotalAmount = "", actualTotalAmount = "";
			String changedComments = "", actualComments = "";
			String actionType = "A";
			String tempIssueDetailsIdForNewRow = "";
			String woRowCode = "";String workOrderStatus="";
			String woROWCodeFormat = "";
			String revisionOfWorkOrder = "";
			String tempIssueId = "";
			String workOrderNoSiteWise = "";
			String nextApprovelEmpId = "";
			String typeOfWork ="";
			String actualWorkOrderName = "";
			String workOrderName = "";

			workOrderStatus = workOrderBean.getWorkOrderStatus() == null ? "" : workOrderBean.getWorkOrderStatus();
			workOrderBean.setWorkOrderStatus(workOrderStatus);
			revisionOfWorkOrder = workOrderBean.getRevision() == null ? "" : workOrderBean.getRevision().trim();
			tempIssueId = request.getParameter("actualtempIssueId");
			workOrderNoSiteWise = request.getParameter("actualWorkOrderNo");
			nextApprovelEmpId = request.getParameter("nextApprovelEmpId");
			typeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork");
			actualWorkOrderName = request.getParameter("actualWorkOrderName");
			workOrderName = request.getParameter("workOrderName") == null ? "" : request.getParameter("workOrderName");
			changedTC = request.getParameterValues("termsAndCOnditions");
			displayedRows = request.getParameterValues("dispplayedRows");
			optionalCCmails = request.getParameter("optionalCCmails");
			
			String[] strWONoPart = workOrderBean.getWorkOrderNo().split("/");
			if (strWONoPart[strWONoPart.length - 1].contains("R")) {
				woROWCodeFormat = strWONoPart[strWONoPart.length - 2];
			} else {
				woROWCodeFormat = strWONoPart[strWONoPart.length - 1];
			}
			if (!workOrderName.equals(actualWorkOrderName)) {
				log.info("Work Order Name Changed ");
			}
			
	
			List<Map<String, String>> termasAndCon = new ArrayList<Map<String, String>>();
			if (changedTC != null)
				for (String string : changedTC) {
					Map<String, String> map = new HashMap<String, String>();
					string = string.trim();
					if (string.length() != 0) {
						map.put("TERMS_CONDITION_DESC", string);
						termasAndCon.add(map);
					}
				}
			//adding terms and condition in model object so we can print this terms and condition in work order print page
			session.setAttribute("listTermsAndCondition", termasAndCon);
			//deleting all the terms and conditions
			int result = workControllerDao.updateTheTermsAndCondition(tempIssueId);
			if (changedTC != null) {
				// inserting all the terms and condition
				result = workControllerDao.insertTermsAndConditions(workOrderBean, tempIssueId, changedTC);
			}
			String strUserName = session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();				  
			//int result1=0;
			//workOrderBean.setFromEmpId(user_id);
			workOrderBean.setUserId(user_id);
			
			if (workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")) {
				actionType = "DF";// draft record
				if (workOrderBean.getRevision().equals("0")) {
					isThisWOSaveOperation = true;
				}
			} else if (workOrderBean.getIsSaveOrUpdateOperation().equals("Approve")) {
				actionType = "A";
			} else if (workOrderBean.getIsSaveOrUpdateOperation().equals("Submit")) {
				actionType = "C";
				if (workOrderBean.getRevision().equals("0")) {
					isThisWOSaveOperation = true;
				}
			}
			//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "A" means approve 
			result = workControllerDao.inserWorkOrderCreationDetail(workOrderBean,actionType);

			//processing number of rows of Work Order Approve Page
			for (String num : displayedRows) {
				boolean isRowDataChanged = false;
				String DeleteColVal = request.getParameter("isDelete" + num) == null ? "" : request.getParameter("isDelete" + num);
				boolean isDelete = false;
				// checking is this row is deleted by approving work order time
				if (DeleteColVal.equals("d")) {
					isDelete = true;
				}
				String isNewRow = request.getParameter("isNewRowAdded" + num)==null?"":request.getParameter("isNewRowAdded" + num);
				String QS_Temp_Issue_Dtls_Id = request.getParameter("QS_Temp_Issue_Dtls_Id" + num)==null?"":request.getParameter("QS_Temp_Issue_Dtls_Id" + num);
				changedWoMajorHead = request.getParameter("WO_MajorHead" + num);
				actualwoMajorHead = request.getParameter("actualwoMajorHead" + num);
				changedWO_MinorHead = request.getParameter("WO_MinorHead" + num);
				actualWO_MinorHead = request.getParameter("actualWO_MinorHead" + num);
				changedWO_Desc = request.getParameter("WO_Desc" + num);
				actualWO_Desc = request.getParameter("actualWO_Desc" + num);
				changedUnitsOfMeasurement = request.getParameter("UnitsOfMeasurement" + num);
				actualunitsOfMeasurement = request.getParameter("actualunitsOfMeasurement" + num);
				changedQuantity = request.getParameter("Quantity" + num).trim();
				actualQuantity = request.getParameter("actualQuantity" + num);
				changedAcceptedRate = request.getParameter("AcceptedRate" + num)==null?"":request.getParameter("AcceptedRate" + num);
				actualAcceptedRate = request.getParameter("actualAcceptedRate" + num)==null?"":request.getParameter("actualAcceptedRate" + num);
				changedTotalAmount = request.getParameter("TotalAmount" + num);
				if (!isDelete && !typeOfWork.equals("NMR")) {
					materialAmount += Double.valueOf(request.getParameter("materialAmount" + num) == null ? "0" : request.getParameter("materialAmount" + num));
					labourAmount += Double.valueOf(request.getParameter("labourAmount" + num) == null ? "0" : request.getParameter("labourAmount" + num));
				}
				actualTotalAmount = request.getParameter("actualTotalAmount" + num);
				changedComments = request.getParameter("Comments" + num) == null ? "" : request.getParameter("Comments" + num).trim();
				actualComments = request.getParameter("actualComments" + num) == null ? "" : request.getParameter("actualComments" + num).trim();
				String woManualDesc = request.getParameter("woManualDesc" + num);
				String woRecordContains = request.getParameter("WoRecordContains" + num);
				workOrderBean.setTotalAmount1(changedTotalAmount);
				
				String[] scopeOfWork = request.getParameterValues("ScopeOfWork" + num);
				String ScopeOfWork = "";
				if (scopeOfWork != null) {
					for (String scopeWork : scopeOfWork) {
						scopeWork = scopeWork.trim();
						if (scopeWork != null && scopeWork.length() != 0) {
							ScopeOfWork += scopeWork + "@@";
						}
					}
				}
				woManualDesc = ScopeOfWork;
				if (!isDelete && isNewRow.length() == 0) {
					String[] tempIssueAreaDetailsId = request.getParameterValues("QS_WO_TEMP_ISSUE_DTLS_ID" + num);
					if (tempIssueAreaDetailsId != null) {
						tempIssueDetailsIdForNewRow = tempIssueAreaDetailsId[0];
					} else if (typeOfWork.equals("NMR")) {
						if (tempIssueDetailsIdForNewRow.length() == 0)
							tempIssueDetailsIdForNewRow = QS_Temp_Issue_Dtls_Id;
					}

				}

				if (!isDelete) {
					woTotalAmount += Double.valueOf(changedTotalAmount);
					// tempIssueDetailsIdForNewRow=QS_Temp_Issue_Dtls_Id[Integer.valueOf(num)-1];
					if (typeOfWork.equals("NMR")) {
						labourAmount = woTotalAmount;
					}
				}
				
				
				WorkOrderBean actualWorkOderDetail = new WorkOrderBean();
				List<String> actualSelectedAreaList = null;

				actualWorkOderDetail.setTypeOfWork(typeOfWork);
				actualWorkOderDetail.setSiteWiseWONO(workOrderNoSiteWise);
				actualWorkOderDetail.setQS_Temp_Issue_Id(tempIssueId);
				actualWorkOderDetail.setWoMajorHead(actualwoMajorHead);
				actualWorkOderDetail.setWoMinorHeads(actualWO_MinorHead);
				actualWorkOderDetail.setwODescription(actualWO_Desc);
				actualWorkOderDetail.setUnitsOfMeasurement(actualunitsOfMeasurement);
				actualWorkOderDetail.setQuantity(actualQuantity);
				actualWorkOderDetail.setAcceptedRate1(actualAcceptedRate);
				actualWorkOderDetail.setTotalAmount1(actualTotalAmount);
				actualWorkOderDetail.setComments1(actualComments);
				//actualWorkOderDetail.setFromEmpName(strUserName);

				WorkOrderBean changedWorkOderDetail = new WorkOrderBean();
				List<String> changedSelectedAreaList = null;
				changedWorkOderDetail.setTypeOfWork(typeOfWork);
				changedWorkOderDetail.setSiteWiseWONO(workOrderNoSiteWise);
				changedWorkOderDetail.setQS_Temp_Issue_Id(tempIssueId);

				try {
					// Minor Head and Major Head For Printing Purpose
					changedWorkOderDetail.setWO_MajorHead1(changedWoMajorHead.split("\\$")[1]);
					changedWorkOderDetail.setWO_MinorHead1(changedWO_MinorHead.split("\\$")[1]);
					changedWorkOderDetail.setWO_Desc1(changedWO_Desc.split("\\$")[1]);
					changedWorkOderDetail.setUnitsOfMeasurement1(changedUnitsOfMeasurement.split("\\$")[1]);
					changedWorkOderDetail.setwOManualDescription(woManualDesc);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(typeOfWork.equals("NMR")&&!isNewRow.equals("true")){if(!isDelete)
					storeDuplicateworkOrderDetailsList.add(changedWoMajorHead.split("\\$")[0]+""+changedWO_MinorHead.split("\\$")[0]+""+changedWO_Desc.split("\\$")[0]);
				}
				changedWorkOderDetail.setWoMinorHeads(changedWO_MinorHead);
				changedWorkOderDetail.setwODescription(changedWO_Desc);
				changedWorkOderDetail.setQuantity(changedQuantity);
				changedWorkOderDetail.setAcceptedRate1(changedAcceptedRate);
				changedWorkOderDetail.setTotalAmount1(changedTotalAmount);
				changedWorkOderDetail.setComments1(changedComments);
				//changedWorkOderDetail.setFromEmpName(strUserName);
				String strChangedComments = "";
				
				if(isNewRow.equals("true")){
					//this method is common for create work order and approve work order, this method will execute only if this is new row while approval of work order
					
					WorkOrderBean tempIssueDetailsData=new WorkOrderBean();
					tempIssueDetailsData.setWO_MajorHead1(changedWoMajorHead.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_MinorHead1(changedWO_MinorHead.split("\\$")[0]);//major head desc id
					tempIssueDetailsData.setWO_Desc1(changedWO_Desc.split("\\$")[0]);//work desc id
					tempIssueDetailsData.setUnitsOfMeasurement1(changedUnitsOfMeasurement.split("\\$")[0]); //uom id
					tempIssueDetailsData.setTotalAmount1(changedTotalAmount.trim());
					tempIssueDetailsData.setQuantity1(changedQuantity.trim());
					tempIssueDetailsData.setwOManualDescription(woManualDesc);
					tempIssueDetailsData.setAcceptedRate1(changedAcceptedRate);
					if (changedComments.length() != 0) {
						changedComments = strUserName + " - " + changedComments;
					}
					tempIssueDetailsData.setComments1(changedComments);
					tempIssueDetailsData.setWoRecordContains(woRecordContains);
					
				if(typeOfWork.equals("NMR")){
					if(!storeDuplicateworkOrderDetailsList.contains(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
						storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
						woRowCode="MH-"+woROWCodeFormat+"-1";
						tempIssueDetailsData.setWoRowCode(woRowCode);
					}else{
						int countSameObjects=1;
						//counting same object's and based on count it will generate work order row code
						//so we have this unique value in to manage bill's with same 
						for (String sameObjectCOunt : storeDuplicateworkOrderDetailsList) {
							if(sameObjectCOunt.equals(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1())){
								countSameObjects++;
							}
						}
						woRowCode="MH-"+woROWCodeFormat+"-"+countSameObjects;
						tempIssueDetailsData.setWoRowCode(woRowCode);
						storeDuplicateworkOrderDetailsList.add(tempIssueDetailsData.getWO_MajorHead1()+""+tempIssueDetailsData.getWO_MinorHead1()+""+tempIssueDetailsData.getWO_Desc1());
					}
				}
					
					
					String pk=	insertWorkOrderTempIssueDetialsData(workOrderBean,request,session,tempIssueDetailsData,tempIssueId,num);
					workOrderBeans.add(changedWorkOderDetail);
					strChangedComments=strUserName +"- Added new WD (\""+changedWorkOderDetail.getWO_MajorHead1()+","+changedWorkOderDetail.getWO_MinorHead1()+","+changedWorkOderDetail.getWO_Desc1()+"\").";
				 
					//if there no key to store changed details take the current inserted record primary key and insert in chnaged details
					if (typeOfWork.equals("NMR")) {
						tempIssueDetailsIdForNewRow = pk;
						actualWorkOderDetail.setQS_Temp_Issue_Id(tempIssueDetailsIdForNewRow);
						changedWorkOderDetail.setQS_Temp_Issue_Id(tempIssueDetailsIdForNewRow);
					} else {
						actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueDetailsIdForNewRow);
						changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueDetailsIdForNewRow);
					}
					if(isThisWOSaveOperation==false){
						//inserting any modified details while approving the work order
						workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "M", user_id,strChangedComments);	
					}	
					continue;
				}

				//checking here is quantity or changed while approve work time 
				if(!changedQuantity.equals(actualQuantity)&&!changedAcceptedRate.equals(actualAcceptedRate)&&(changedAcceptedRate.length()!=0&&actualAcceptedRate.length()!=0)){
					strChangedComments= strUserName + "- Modifed quantity "+actualQuantity+" to "+ changedQuantity +" and accepted rate "+actualAcceptedRate+" to "+ changedAcceptedRate+" for "+changedWorkOderDetail.getWO_MinorHead1()+", "+changedWorkOderDetail.getWO_Desc1()+"<br>";
					isRowDataChanged = true;
				}else if (!changedQuantity.equals(actualQuantity)) {
					strChangedComments= strUserName + " - Modifed quantity "+actualQuantity+"  to "+ changedQuantity
							+" for "+changedWorkOderDetail.getWO_MinorHead1()+", "+changedWorkOderDetail.getWO_Desc1()+"<br>";
					isRowDataChanged = true;
				}else if (!changedAcceptedRate.equals(actualAcceptedRate)&&(changedAcceptedRate.length()!=0&&actualAcceptedRate.length()!=0)) {
						isRowDataChanged = true;
						strChangedComments +=    strUserName + " - Modifed accepted rate "+actualAcceptedRate+" to "+ changedAcceptedRate
								+" for "+changedWorkOderDetail.getWO_MinorHead1()+", "+changedWorkOderDetail.getWO_Desc1()+"<br>";
				}else if (!changedTotalAmount.equals(actualTotalAmount)) {
					isRowDataChanged = true;
				} else {
					log.info("row is not changed " + isRowDataChanged);
				}
				// if the Work Order is NMR then execute if condition
				if (typeOfWork.equals("NMR") && isRowDataChanged) {
					actualWorkOderDetail.setQS_Temp_Issue_Id(QS_Temp_Issue_Dtls_Id);
					changedWorkOderDetail.setQS_Temp_Issue_Id(QS_Temp_Issue_Dtls_Id);
					//inserting any modified details while approving the work order
					if (isThisWOSaveOperation == false) {
						workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "M", user_id,strChangedComments);
					}
				
					if (!changedTotalAmount.equalsIgnoreCase(actualTotalAmount)) {
							//if amount is changed update the quantity and rate
							 workControllerDao.updateWorkOrderPriceByTmpIssuId("",changedAcceptedRate,QS_Temp_Issue_Dtls_Id,actualTotalAmount,changedTotalAmount,changedQuantity);
					}
				}
			
				//boolean canWorkOrderUpdate=false;
				if(!isDelete){
					workOrderBeans.add(changedWorkOderDetail);
				}
				
				//if isDelete is true then reallocate all the given work order quantity and make the work order row inactive
				  if (isDelete) { 
					  	strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();			
					  	String[] str=actualWO_Desc.split("\\$");
					  	String strDeleteComment = strUserName + " deleted work order row, details as Following : "+changedWoMajorHead.split("\\$")[1]+","+changedWO_MinorHead.split("\\$")[1]+", "+str[1] + " quantity "+actualQuantity+" \n";
					if (typeOfWork.equals("NMR")) {
								  strDeleteComment = strUserName + " deleted work order row, details as Following : "+changedWO_MinorHead.split("\\$")[1]+", "+str[1] + " quantity "+actualQuantity+" and accepted rate "+changedAcceptedRate+" \n";
					}
					   String strFinalChangedComments = strDeleteComment;
					  
					//if row is removed while approval of work order we have to in activate that particular row
					int rerult = workControllerDao.doInActiveWorkOder(QS_Temp_Issue_Dtls_Id,actualTotalAmount);
					List<Map<String, Object>> tmpIsssueDtlsId = workControllerDao.getTempIssDTLSId(QS_Temp_Issue_Dtls_Id, actualTotalAmount, true);
					if (typeOfWork.equals("NMR")) {
						actualWorkOderDetail.setQS_Temp_Issue_Id(QS_Temp_Issue_Dtls_Id);
						changedWorkOderDetail.setQS_Temp_Issue_Id(QS_Temp_Issue_Dtls_Id);
						// inserting NMR changed details
						if (isThisWOSaveOperation == false) {
							workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail, changedWorkOderDetail,tempIssueId, nextApprovelEmpId, "D", user_id, strDeleteComment);
						}
					}
					for (Map<String, Object> map : tmpIsssueDtlsId) {
						String id = String.valueOf(map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID"));
						actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
						changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
						// inserting changed details
						if (isThisWOSaveOperation == false) {
							workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail, changedWorkOderDetail,tempIssueId, nextApprovelEmpId, "D", user_id, strDeleteComment);
						}
						break;
					}
				  }// dnt touch
				  
				
				//if row is not changed
				if (!isRowDataChanged&&!isNewRow.equals("true")) {
				} else if (isRowDataChanged&&isDelete==false&&!typeOfWork.equals("NMR")&&!isNewRow.equals("true")) {
				/*
				 	the work area data is  coming in Array, 
				 	How many rows are selected in work area all the data will come in 
				 	their respective variable's
				 */
					String[] workAreaId = request.getParameterValues("workAreaId" + num);
					String[] selectedArea = request.getParameterValues("selectedArea" + num);
					String[] actualArea = request.getParameterValues("availableArea" + num);
					String[] percentage = request.getParameterValues("percentage" + num);
					String[] block_name = request.getParameterValues("BLOCK_NAME" + num);
					String[] floor_name = request.getParameterValues("FLOOR_NAME" + num);
					String[] flat_name = request.getParameterValues("FLAT_ID" + num);
					String[] acceptedRate = request.getParameterValues("accepted_rate" + num);
					String[] recordType = request.getParameterValues("record_type" + num);
					String[] boqRate = request.getParameterValues("boqRate" + num);
					String[] tempIssueAreaDetailsId = request.getParameterValues("QS_WO_TEMP_ISSUE_DTLS_ID" + num);
					String[] oldWOIssueAreaDetailsId = request.getParameterValues("OLD_QS_WO_ISSUE_DTLS_ID" + num);
					// this data is for actual work Area data which are created
					// in Create Work Order
					String[] actualAreaAlocatedQTYPrice = request.getParameterValues("actualAreaAlocatedQTYPrice" + num);
					String[] actualworkAreaIds = request.getParameterValues("actualWorkAreaId" + num);
					String[] actualSelectedArea = request.getParameterValues("actualAreaAlocatedQTY" + num);
					String[] actualWOWorkTempIssueAreaDtlsId = request.getParameterValues("actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID" + num);
					String[] actualrecordType = request.getParameterValues("actualrecordType" + num);
					String[] actualBlockName1 = request.getParameterValues("BLOCK_NAME1" + num);
					String[] actualFloor_Name1 = request.getParameterValues("FLOOR_NAME1" + num);
					String[] actualFlat_Id1 = request.getParameterValues("FLAT_ID1" + num);
					log.info(num+" Rownum Actual Record Type : "+Arrays.toString(actualrecordType)+" current record Type "+Arrays.toString(recordType));
					//converting work area in List type Collection it will use for comparing the actual work area and changed work area
					changedSelectedAreaList = selectedArea != null ? Arrays.asList(selectedArea): new ArrayList<String>();
					//converting actual work area in List type Collection it will use for comparing the actual work area and changed work area
					actualSelectedAreaList = actualSelectedArea != null ? Arrays.asList(actualSelectedArea)	: new ArrayList<String>();
					
					//in this List all the Actual WorkAreaId will come
					List<String> actualWorkAreaIdList = actualworkAreaIds != null ? Arrays.asList(actualworkAreaIds): new ArrayList<String>();
					//in this List all the Current WorkAreaId will come
					//List<String> changedWorkAreaIdList = workAreaId != null ? Arrays.asList(workAreaId): new ArrayList<String>();
					List<String> actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID_LIST=  actualWOWorkTempIssueAreaDtlsId != null ? Arrays.asList(actualWOWorkTempIssueAreaDtlsId): new ArrayList<String>();
					//List<String> actualISSUE_DTLS_IDlIST = actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID != null? Arrays.asList(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID) : new ArrayList<String>();
					List<String> changedIssueAreaDetailsId = tempIssueAreaDetailsId != null? Arrays.asList(tempIssueAreaDetailsId) : new ArrayList<String>();
					
					if (!changedTotalAmount.equalsIgnoreCase(actualTotalAmount)) {
						workControllerDao.updateWorkOrderPriceByTmpIssuId("", changedAcceptedRate,QS_Temp_Issue_Dtls_Id, actualTotalAmount, changedTotalAmount,changedQuantity);
					}
				
					/* 
					    Checking here is work area selected area is changed or not and also checking is work area rate is changed or not
						if changed update work area in BOQ_AREA_Mapping and   QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
					*/
				if(actualworkAreaIds!=null&&workAreaId!=null)
					for (int i = 0; i < actualworkAreaIds.length; i++) {
						for (int j = 0; j < workAreaId.length; j++) {
							//checking Actual WorkAreaId and current WorkAreaID is same or not if same enter into condition
							if (workAreaId[j].equals(actualworkAreaIds[i])  && actualWOWorkTempIssueAreaDtlsId[i].equals( tempIssueAreaDetailsId[j])){
								/*  Checking here is Actual Quantity is differ from current Quantity if changed then update the BOQ_AREA_MAPPING and QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
									Current means the values you are seeing in Approve Work Order
								*/log.info("got Null pointer exx in "+i+" "+j);
									double tempActualSelectedArea = Double.valueOf(actualSelectedArea[i]);
									double tempSelectedArea = Double.valueOf(selectedArea[j]);

									double tempActualAreaAlocatedQTYPrice = Double.valueOf(actualAreaAlocatedQTYPrice[i]);
									double tempAcceptedRate = Double.valueOf(acceptedRate[j]);
								
								//if(!actualSelectedArea[i].equals(selectedArea[j])){
								if(tempActualSelectedArea!=tempSelectedArea){//checking if current entered area and actual area is different that means value is changed
										changedWorkOderDetail.setSelectedArea(selectedArea[j]);
										changedWorkOderDetail.setWorkAreaId(workAreaId[j]);
										changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueAreaDetailsId[j]);
										changedWorkOderDetail.setPercentage(percentage[j]);
										changedWorkOderDetail.setAcceptedRate1(acceptedRate[j]);
										changedWorkOderDetail.setBoqRecordType(recordType[j]);	
										
										actualWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
										actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
										actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
										actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWOWorkTempIssueAreaDtlsId[i]);
										actualWorkOderDetail.setBoqRecordType(actualrecordType[i]);	
										
										// updating the Quantity and price
										workControllerDao.updateWorkOrderAreaDetails(changedWorkOderDetail,actualWorkOderDetail);

										double actual = Double.parseDouble(actualSelectedArea[i]);
										double changed = Double.parseDouble(selectedArea[j]);
									
									//checking here is work area value is increased or decreased 
										if (actual < changed) {
											changedWorkOderDetail.setSelectedArea(String.valueOf(Math.abs(actual - changed)));
											changedWorkOderDetail.setCondition("1");
											changedWorkOderDetail.setActualArea(String.valueOf(actual));
										} else if (actual > changed) {
											changedWorkOderDetail.setCondition("2");
											changedWorkOderDetail.setSelectedArea(String.valueOf(Math.abs(actual - changed)));
											changedWorkOderDetail.setActualArea(String.valueOf(actual));
										}
									
									String strModifyComment = strUserName + " - Modified quantity "+actualSelectedArea[i]+" to "+ selectedArea[j]+" for (\""+actualWO_Desc.split("\\$")[1]+"\"), "+actualBlockName1[i];
									if(!actualFloor_Name1[i].equals("-")) {strModifyComment+=", "+actualFloor_Name1[i];}
									if(!actualFlat_Id1[i].equals("-"))    {strModifyComment+=", "+actualFlat_Id1[i];}
									
									//strModifyComment+=" and type "+recordType[i]+"\n";
									//update the work order allocated area 
									workControllerDao.updateWorkAreaMapping(changedWorkOderDetail,nextApprovelEmpId,"modify");
									//inserting the changed detail's
									if(isThisWOSaveOperation==false){
										workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "EDIT", user_id,strModifyComment);			
									}
									//if(!actualAreaAlocatedQTYPrice[i].equals(acceptedRate[j])){
									if(tempActualAreaAlocatedQTYPrice!=tempAcceptedRate){
											changedWorkOderDetail.setSelectedArea(selectedArea[j]);
											changedWorkOderDetail.setWorkAreaId(workAreaId[j]);
											changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueAreaDetailsId[j]);
											changedWorkOderDetail.setPercentage(percentage[j]);
											changedWorkOderDetail.setAcceptedRate1(acceptedRate[j]);
											changedWorkOderDetail.setBoqRecordType(recordType[j]);	
											
											actualWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
											actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
											actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
											actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWOWorkTempIssueAreaDtlsId[i]);
											actualWorkOderDetail.setBoqRecordType(actualrecordType[i]);
											
											strModifyComment = strUserName + " - Modified Accepted Rate "+actualAreaAlocatedQTYPrice[i]+" to "+ acceptedRate[j]+" for (\""+actualWO_Desc.split("\\$")[1]+"\"), "
												+actualBlockName1[i];
											if (!actualFloor_Name1[i].equals("-")) {
												strModifyComment += ", " + actualFloor_Name1[i];
											}
											if (!actualFlat_Id1[i].equals("-")) {
												strModifyComment += ", " + actualFlat_Id1[i];
											}
										
										//strModifyComment+=" and type "+recordType[i]+"\n";
											if (isThisWOSaveOperation == false) {
												workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,
														changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "EDIT",
														user_id, strModifyComment);
											}
										//workControllerDao.updateWorkOrderPriceByTmpIssuId(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i],acceptedRate[i],"","",changedTotalAmount,changedQuantity);
								}
							
							WorkOrderBean bean=new WorkOrderBean();
							List<WorkOrderBean> listOfMaterialBoq=new ArrayList<WorkOrderBean>();
							//if the record type is material then we need to update the product details
							if(recordType[j].equals("MATERIAL")){
									log.info("WorkOrderServiceImpl.approveWorkOrderCreation() MATERIAL");
									StringBuffer errorListComment=null;
									bean.setWorkAreaId(workAreaId[j]);
									bean.setSiteId(workOrderBean.getSiteId());
									if(revisionOfWorkOrder!=null&&!revisionOfWorkOrder.equals("0")&&revisionOfWorkOrder.length()!=0){
												bean.setSelectedArea(selectedArea[j]);
												bean.setTempIssueAreaDetailsId(oldWOIssueAreaDetailsId[j]);
												List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
												list.add(bean);

												Map<String, String> errorMessage = workControllerDao.validateWOMaterialBOQDetails(list, workOrderBean.getSiteId(),
																workOrderBean.getContractorId(),workOrderBean.getOldWorkOrderNo(), "approveOperation","");
												if (errorMessage.size() != 0) {
													// return "error for material area";
													throw new Exception("error in material area");
												}
									}
									bean.setSelectedArea(selectedArea[j]);
									bean.setTempIssueAreaDetailsId(actualWOWorkTempIssueAreaDtlsId[i]);
									//common method for inserting material BOQ data
									listOfMaterialBoq=insertMaterialData(bean,listOfMaterialBoq);
									//inserting all the rows using spring JDBC batch update
								result= workControllerDao.insertDataIntoWorkOrderTempProductDtls(listOfMaterialBoq,"Del_insert");	
							}else{
								//labourAmount+=Double.valueOf(selectedArea[i])*Double.valueOf(acceptedRate[i]);
							}//material condition		
									
							break;
							/* Checking here if accepted rate is changed or not if changed, update rate in QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS*/ 
							} else if(tempActualAreaAlocatedQTYPrice!=tempAcceptedRate){
								//else if(!actualAreaAlocatedQTYPrice[i].equals(acceptedRate[j])){
									changedWorkOderDetail.setSelectedArea(selectedArea[j]);
									changedWorkOderDetail.setWorkAreaId(workAreaId[j]);
									changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueAreaDetailsId[j]);
									changedWorkOderDetail.setPercentage(percentage[j]);
									changedWorkOderDetail.setAcceptedRate1(acceptedRate[j]);
									changedWorkOderDetail.setBoqRecordType(recordType[j]);
										
									actualWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
									actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
									actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
									actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWOWorkTempIssueAreaDtlsId[i]);
									actualWorkOderDetail.setBoqRecordType(actualrecordType[i]);	
									
									String strModifyComment = strUserName + " - Modified Accepted Rate "+actualAreaAlocatedQTYPrice[i]+" to "+ acceptedRate[j]+" for (\""+actualWO_Desc.split("\\$")[1]+"\"), "	+actualBlockName1[i];
									if(!actualFloor_Name1[i].equals("-")) {strModifyComment+=", "+actualFloor_Name1[i];}
									if(   !actualFlat_Id1[i].equals("-")) {strModifyComment+=", "+actualFlat_Id1[i];}
									
									//strModifyComment+=" and type "+recordType[i]+"\n";
									
									if(isThisWOSaveOperation==false){
										workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "EDIT", user_id,strModifyComment);
									}
									workControllerDao.updateWorkOrderPriceByTmpIssuId(actualWOWorkTempIssueAreaDtlsId[i],acceptedRate[j],"","",changedTotalAmount,changedQuantity);
							}
						}
					}
				}//if condition
					
				/*
				 	Checking here is any work area removed from work order,
					if removed then update in  BOQ_AREA_Mapping and   QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
					and make the work area inactive in QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS
				*/
				if(actualworkAreaIds!=null&&workAreaId!=null)
					for (int i = 0; i < actualworkAreaIds.length; i++) {
						if (!changedIssueAreaDetailsId.contains(actualWOWorkTempIssueAreaDtlsId[i]) ) {
								//if both actual and current areaDetils
								result=workControllerDao.doInActiveWorkOder(actualworkAreaIds[i],tempIssueId,actualWOWorkTempIssueAreaDtlsId[i]);
								
								changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWOWorkTempIssueAreaDtlsId[i]);
								//String strDeleteComment = strUserName + " Deleted Work "+actualWO_Desc.split("\\$")[1]+" allocated area  "+actualSelectedArea[i]+" for  "+actualBlockName1[i]+"\n";
								String strDeleteComment= strUserName + " - Deleted (\""+actualWO_Desc.split("\\$")[1]+"\") having quantity("+actualSelectedArea[i]+") and accepted rate("+actualAreaAlocatedQTYPrice[i]+") for  "+actualBlockName1[i];
								if(!actualFloor_Name1[i].equals("-")) {strDeleteComment+=", "+actualFloor_Name1[i];}
								if(!actualFlat_Id1[i].equals("-"))    {strDeleteComment+=", "+actualFlat_Id1[i];}
								
								//strDeleteComment+=" and type "+recordType[i]+"\n";
								changedWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
								changedWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
								changedWorkOderDetail.setBoqRecordType(actualrecordType[i]);
								changedWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
								
								actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
								actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
								actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWOWorkTempIssueAreaDtlsId[i]);
								actualWorkOderDetail.setBoqRecordType(actualrecordType[i]);
								actualWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
								//canWorkOrderUpdate=true;
								//deleted the work order and  reallocate the allocated area
								workControllerDao.updateWorkAreaMapping(actualWorkOderDetail,nextApprovelEmpId,"deleted");
								if(isThisWOSaveOperation==false){
									workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "DEL", user_id,strDeleteComment);
								}
								//inserting all the rows using spring JDBC batch update
								result=	workControllerDao.deleteWorkOrderTempProductDtls(actualWOWorkTempIssueAreaDtlsId[i],"Delete");	
						}
					}
					
				/*
				 	checking here is any work area is added in Work Order for Current Row Processing
					if added then insert the work area and update BOQ_AREA_Mapping
				*/
					if(workAreaId!=null)
					for (int i = 0; i < workAreaId.length; i++) {
						//if (!actualWorkAreaIdList.contains(workAreaId[i])) {
						if (!actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID_LIST.contains(tempIssueAreaDetailsId[i])) {
							if (actualArea[i] != null && selectedArea[i] != null && workAreaId[i] != null) {
								WorkOrderBean bean = new WorkOrderBean();
								//bean.setTotalAmount1(changedTotalAmount.trim());
								bean.setAcceptedRate1(acceptedRate[i]);
								bean.setQuantity1(changedQuantity.trim());
								bean.setSelectedArea(selectedArea[i]);
								bean.setActualArea(actualArea[i]);
								bean.setWorkAreaId(workAreaId[i]);
								bean.setBoqRecordType(recordType[i]);
								bean.setBoqRate(boqRate[i]);

								//inserting new added, work area
								List<Integer> tempDetailresp = workControllerDao.WorkOrderTempIssueAreaDetails(bean,QS_Temp_Issue_Dtls_Id);
								String TEMP_ISSUE__AREA_DTLS_ID=tempDetailresp.get(0).toString();
								changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(TEMP_ISSUE__AREA_DTLS_ID);
								changedWorkOderDetail.setSelectedArea(selectedArea[i]);
								changedWorkOderDetail.setWorkAreaId(workAreaId[i]);
								changedWorkOderDetail.setBoqRecordType(recordType[i]);
								
								actualWorkOderDetail.setSelectedArea(selectedArea[i]);
								actualWorkOderDetail.setWorkAreaId(workAreaId[i]);
								actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(TEMP_ISSUE__AREA_DTLS_ID);		
								actualWorkOderDetail.setBoqRecordType(recordType[i]);
								
								String strAddedComment = strUserName + " - Added Work for (\""+actualWO_Desc.split("\\$")[1]+"\") with qunatity("+selectedArea[i]+") and accepted rate ("+acceptedRate[i]+") for "+block_name[i];
								if(!floor_name[i].equals("-")) {strAddedComment+=", "+floor_name[i];}
								if(!flat_name[i].equals("-"))  {strAddedComment+=", "+flat_name[i];}
								//strAddedComment+=" and type "+recordType[i]+"\n";
								if(isThisWOSaveOperation==false){
									workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "NEW", user_id,strAddedComment);
								}
								//update the BOQ_AREA_Mapping work area
								workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"");
								
								List<WorkOrderBean> listOfMaterialBoq=new ArrayList<WorkOrderBean>();
								//if the record type is material then we need to update the product details
									if (recordType[i].equals("MATERIAL")) {
										log.info("WorkOrderServiceImpl.approveWorkOrderCreation() MATERIAL Added Work in approve work order");
										bean.setWorkAreaId(workAreaId[i]);
										bean.setSiteId(workOrderBean.getSiteId());
										bean.setTempIssueAreaDetailsId(TEMP_ISSUE__AREA_DTLS_ID);
										bean.setSelectedArea(selectedArea[i]);

										listOfMaterialBoq = insertMaterialData(bean, listOfMaterialBoq);
										// inserting all the rows using spring
										// JDBC batch update
										if (listOfMaterialBoq.size() != 0) {
											result = workControllerDao.insertDataIntoWorkOrderTempProductDtls(listOfMaterialBoq, "insert");
										}
									}
								}
						}
					}
				}// isRowDataChangedcondition end
				
				if (!changedComments.equals(actualComments)) {
					if (actualComments != null && !changedComments.equals("")) {
						changedComments = actualComments + " --@@ " + strUserName + " - " + changedComments + " @@";
						result = workControllerDao.updateWorkOrderComments(QS_Temp_Issue_Dtls_Id, changedComments, changedAcceptedRate);
					}
				}
				if (woManualDesc.length() != 0) {
					result = workControllerDao.updateWorkOrderRowScope(QS_Temp_Issue_Dtls_Id, woManualDesc);
				}
			}//for loop end 
		
			materialAmount = Double.parseDouble(new DecimalFormat("##.##").format(materialAmount));
			labourAmount = Double.parseDouble(new DecimalFormat("##.##").format(labourAmount));
			workOrderBean.setMaterialWoAmount(materialAmount.toString());
			workOrderBean.setLaborWoAmount(labourAmount.toString());
			workOrderBean.setTotalWoAmount(String.valueOf(materialAmount + labourAmount));
			//workOrderBean.setFromEmpName(strUserName);
			result = workControllerDao.updatePendingWorkOrder(workOrderBean, tempIssueId, workOrderNoSiteWise,
					nextApprovelEmpId, "insert");
			model.addAttribute("workOrderCreationList", workOrderBeans);
			listOfVerifiedEmpNames = workControllerDao.getWorkOrderVerifiedEmpNames(workOrderBean);
			String s = null;
			// s.trim();
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response = "failed";
			e.printStackTrace();
			if (e.getMessage()!=null&&e.getMessage().equals("error in material area")) {
				response = "error in material area";
				return response;
			}
			if(e.getMessage()!=null&&e.getMessage().equals("Work Order Number is Not Valid")){
				throw new RuntimeException("Work Order Number is Not Valid");
			}

			isSendMail = false;
		}

		if (isSendMail&&!workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")) {
			constructEmailContent(workOrderBean, request,listOfVerifiedEmpNames, optionalCCmails);
		}
		return response;
	}
	
	
	private void constructEmailContent(WorkOrderBean workOrderBean, HttpServletRequest request, List<Map<String, Object>> listOfVerifiedEmpNames, String optionalCCmails){
		List<String> to_EmailAddress = new ArrayList<String>();
		//boolean isFinalLevelApproval=workOrderBean.getApproverEmpId().equals("END");
		if (workOrderBean.getApproverEmpId().equals("END")) {
		log.info(listOfVerifiedEmpNames);
			if (listOfVerifiedEmpNames != null) {
				for (Map<String, Object> map : listOfVerifiedEmpNames) {
					String email=map.get("EMP_EMAIL") == null ? "" : map.get("EMP_EMAIL").toString();
					if(email!=null&&email.length()!=0)		
						to_EmailAddress.add(email);
				}
			}
		} else {
			if(workOrderBean.getApproverEmpMail()!=null&&workOrderBean.getApproverEmpMail().contains(",")){
				to_EmailAddress.addAll(Arrays.asList(workOrderBean.getApproverEmpMail().split(",")));	
			}else{
				to_EmailAddress.add(workOrderBean.getApproverEmpMail());
			}
			
		}

		List<String> ccMailAddressOfEmployee = new ArrayList<String>();
		//ccMailAddressOfEmployee.add("aniketchavan75077@gmail.com");
		if (!workOrderBean.getApproverEmpId().equals("END")) {
			 optionalCCmails="";	
		}
		if (optionalCCmails != null && optionalCCmails != "") {
			ccMailAddressOfEmployee.addAll(Arrays.asList(optionalCCmails.split(",")));
		}
		for (Iterator<String> iterator = ccMailAddressOfEmployee.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			if (string == null || string.isEmpty()) {
				iterator.remove();
			}
		}
		EmailFunction objEmailFunction = new EmailFunction();
		StringBuffer emailSubject = new StringBuffer("");
		if (workOrderBean.getApproverEmpId().equals("END") && !workOrderBean.getRevision().equals("0")) {
			// if this is final level of work order and this work order is
			// revised then we are generating another subject for mail
			emailSubject.append("Work Order created for ");
			emailSubject.append(workOrderBean.getContractorName());
			emailSubject.append(", for project ");
			emailSubject.append(workOrderBean.getSiteName());
			emailSubject.append(" had been revised");
		} else if (workOrderBean.getApproverEmpId().equals("END")) {
			// if this is final level of work order we are generating mail subject
			emailSubject.append("Work Order created for ");
			emailSubject.append(workOrderBean.getContractorName());
			emailSubject.append(", for project ");
			emailSubject.append(workOrderBean.getSiteName());
		} else {
			emailSubject.append("Regarding Approval of Work Order.");
		}
			BigDecimal	totalAmount = new BigDecimal(workOrderBean.getLaborWoAmount()).setScale(2, RoundingMode.CEILING);
			workOrderBean.setLaborWoAmount(String.valueOf(totalAmount));
			
			totalAmount = new BigDecimal(workOrderBean.getOldWorkOrderLaborAmount()==null?"0":workOrderBean.getOldWorkOrderLaborAmount()).setScale(2, RoundingMode.CEILING);
			workOrderBean.setOldWorkOrderLaborAmount(String.valueOf(totalAmount));
			
			StringBuffer emailBodyContent=new StringBuffer("");
			emailBodyContent.append("<tr>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getSiteName()+"</td>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getContractorName()+"</td>");
	    	emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderDate()+"</td>");
		if (!workOrderBean.getApproverEmpId().equals("END")) {//if this is not the final level of work order approval than only add temporary work order number
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+ workOrderBean.getSiteWiseWONO() + "</td>");
		}
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderNo()+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+(workOrderBean.getWorkOrderName()==null?"-":workOrderBean.getWorkOrderName())+"</td>");
		if (workOrderBean.getApproverEmpId().equals("END") && !workOrderBean.getRevision().equals("0")) {
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+ workOrderBean.getOldWorkOrderLaborAmount() + "</td>");
		}
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getLaborWoAmount()+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getTypeOfWork()+"</td>");
	        emailBodyContent.append("</tr>");
		if (to_EmailAddress.size() == 0) {

		} else {
			log.info("to Address ("+to_EmailAddress +") CC email address ("+ccMailAddressOfEmployee+")");
			objEmailFunction.sendMailWorkOrderCreation(workOrderBean, request, emailSubject, emailBodyContent,
					to_EmailAddress, ccMailAddressOfEmployee, "approveOrCompletedWO");
		}
	}
//
	/**
	 * @description sending reject or modify mail using this method
	 * @param workOrderBean holding work order data which used in printing the data in email;
	 * @param request holding site address
	 * @param listOfVerifiedEmpNames holding email of employees
	 * @param optionalCCmails holding CC mails for email
	 * @return
	 */
	private String constructEmailContentForRejetAndModify(WorkOrderBean workOrderBean, HttpServletRequest request, List<Map<String, Object>> listOfVerifiedEmpNames, String optionalCCmails){
		List<String> to_EmailAddress = new ArrayList<String>();
		//to_EmailAddress.add("aniketchavan75077@gmail.com");
		List<String> ccMailAddressOfEmployee = new ArrayList<String>();
		
		for (Map<String, Object> map : listOfVerifiedEmpNames) {
			String actionType = map.get("OPERATION_TYPE") == null ? "" : map.get("OPERATION_TYPE").toString().trim();
			if (workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")) {
				if (actionType.equals("C")) {
					to_EmailAddress.add(map.get("EMP_EMAIL") == null ? "" : map.get("EMP_EMAIL").toString());
				} else {
					ccMailAddressOfEmployee.add(map.get("EMP_EMAIL") == null ? "" : map.get("EMP_EMAIL").toString());
				}
			}else{
				to_EmailAddress.add(map.get("EMP_EMAIL") == null ? "" : map.get("EMP_EMAIL").toString());
			}
		}

		if (optionalCCmails != null && optionalCCmails != "") {
			// ccMailAddressOfEmployee.addAll(Arrays.asList(optionalCCmails.split(",")));
		}
		for (Iterator<String> iterator = ccMailAddressOfEmployee.iterator(); iterator.hasNext();) {
			String email = iterator.next();
			if (email == null || email.isEmpty()) {
				iterator.remove();
			}
		}
		
		EmailFunction objEmailFunction = new EmailFunction();
		StringBuffer emailSubject = new StringBuffer("");
		if (workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")) {
			emailSubject.append("Regarding Request for Work Order modification.");
		} else if (workOrderBean.getIsSaveOrUpdateOperation().equals("Reject")) {
			emailSubject.append("Regarding Rejection of Work Order.");
		}
			BigDecimal	totalAmount = new BigDecimal(workOrderBean.getLaborWoAmount()).setScale(2, RoundingMode.CEILING);
			workOrderBean.setLaborWoAmount(String.valueOf(totalAmount));
			StringBuffer emailBodyContent=new StringBuffer("");
			emailBodyContent.append("<tr>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getSiteName()+"</td>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getContractorName()+"</td>");
	    	emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderDate()+"</td>");
			emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getSiteWiseWONO() + "</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getWorkOrderNo()+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+(workOrderBean.getWorkOrderName()==null?"-":workOrderBean.getWorkOrderName())+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getLaborWoAmount()+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+workOrderBean.getTypeOfWork()+"</td>");
	        emailBodyContent.append("<td style='border:1px solid #000;text-align:center;font-size: 13px;padding: 5px;'>"+(workOrderBean.getRemarks()==null?"-":workOrderBean.getRemarks())+"</td>");
	        emailBodyContent.append("</tr>");
		if (to_EmailAddress.size() == 0) {
			return "";
		}
		log.info("to Address ("+to_EmailAddress +") CC email address ("+ccMailAddressOfEmployee+")");
			objEmailFunction.sendMailWorkOrderCreation(workOrderBean,request,emailSubject,emailBodyContent,to_EmailAddress,ccMailAddressOfEmployee,"rejectOrModify");
		return "";
	}
	
	/**
	 * @description common method for , approve work order, create work order , revise work order 
	 * @param beanParam
	 * @param listOfMaterialBoq
	 * @return
	 */
	private List<WorkOrderBean> insertMaterialData(WorkOrderBean beanParam, List<WorkOrderBean> listOfMaterialBoq) {	
		//Loading material BOQ details
		List<Map<String, Object>>  listOfMaterialBOQ=	workControllerDao.loadMaterialBoqDetailsForWO(beanParam);
		
		for (Map<String, Object> map : listOfMaterialBOQ) {
			WorkOrderBean	bean = new WorkOrderBean();
			bean.setSelectedArea(beanParam.getSelectedArea());
			//materialAmount+=Double.valueOf(selectedArea[i])*Double.valueOf(acceptedRate[i]);
			
			String perUnitQuantity=	map.get("PER_UNIT_QUANTITY").toString(); 
			String perUnitAmount=map.get("PER_UNIT_AMOUNT").toString();
			String materialGroupId=map.get("MATERIAL_GROUP_ID").toString();
			String materialUOM=map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString();
			bean.setPerUnitQuantity(perUnitQuantity);
			bean.setPerUnitAmount(perUnitAmount);
			bean.setTempIssueAreaDetailsId(beanParam.getTempIssueAreaDetailsId());

			bean.setMaterialGroupId(materialGroupId);
			bean.setUnitsOfMeasurement1(materialUOM);
			Double totalQuantity=Double.valueOf(bean.getSelectedArea())*Double.valueOf(perUnitQuantity);
			Double totalAmount=Double.valueOf(bean.getSelectedArea())*Double.valueOf(perUnitAmount);
			
			totalQuantity =Double.parseDouble(new DecimalFormat("##.###").format(totalQuantity));
			totalAmount =Double.parseDouble(new DecimalFormat("##.###").format(totalAmount));
			
			bean.setTotalQuantity(totalQuantity.toString());
			bean.setTotalAmount(totalAmount.toString());
			
		 listOfMaterialBoq.add(bean);
		}
		return listOfMaterialBoq;
	}

	/**
	 * @description this method is for rejecting work order 
	 */
	@Override
	public String rejectWorkOrderCreation(WorkOrderBean workOrderBean, Model model, HttpSession session, HttpServletRequest request,
			String site_Id, String user_id) {
		boolean isMailSend=true;
		String response = "";
		String changedWorkOrderLevelPurpose = "";
		String actualWorkOrderLevelPurpose = "";
		String strUserName = "";
		String actionType = "";
		String optionalCCmails = "";
		int result1;
		List<Map<String, Object>> listOfVerifiedEmpNames = null;
		String isSaveModifyOrUpdateOperation = workOrderBean.getIsSaveOrUpdateOperation()==null?"":workOrderBean.getIsSaveOrUpdateOperation();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in WOCr_rejWO, ");
		try
		{
			changedWorkOrderLevelPurpose = request.getParameter("Purpose");
			actualWorkOrderLevelPurpose = request.getParameter("actualPurpose");
			strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			//WorkOrderBean bean = new WorkOrderBean();
			workOrderBean.setUserId(user_id);
			workOrderBean.setPurpose(changedWorkOrderLevelPurpose);
			workOrderBean.setWorkorderFrom(strUserName);
			workOrderBean.setRemarks(changedWorkOrderLevelPurpose);
			if(actualWorkOrderLevelPurpose!=null){
				changedWorkOrderLevelPurpose = actualWorkOrderLevelPurpose + "@@" + strUserName + " - "	+ changedWorkOrderLevelPurpose + "@@";
			}else{
				changedWorkOrderLevelPurpose = strUserName + " - "	+ changedWorkOrderLevelPurpose + "@@";
			}
			if(isSaveModifyOrUpdateOperation.equals("Discard Modify WorkOrder")){
				actionType="M";
			}else if(isSaveModifyOrUpdateOperation.equals("Discard")){
				actionType="DF";
			}else{
				actionType="A";
			}
			boolean isValid = workControllerDao.checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),user_id, false, actionType);
			if (!isValid) {
				throw new RuntimeException("Work order number is not valid.");
			}
			
			if (isSaveModifyOrUpdateOperation.equals("Modify")) {
				result1 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, "M");
				result1 = workControllerDao.sendToModifyWorkOrder(workOrderBean);
			}else{
				if (isSaveModifyOrUpdateOperation.equals("Modify WorkOrder")) {
					actionType = "RMW";
				} else {
					actionType = "R";
				}
				//inserting approve reject details in QS_WORKORDER_CRT_APPRL_DTLS table "R" means reject 
				result1 = workControllerDao.inserWorkOrderCreationDetail(workOrderBean, actionType);
				
				//updating the status of temp work order
				result1 = workControllerDao.rejectWorkOrderCreationCreation(workOrderBean);
				log.info("WorkOrderServiceImpl.rejectWorkOrderCreation() operationTypeForWorkOrder : "+isSaveModifyOrUpdateOperation+" nextApprovelEmpId :" +workOrderBean.getApproverEmpId());
				//bean.setSiteId(siteId);
				//if the work order number is not the revise work order then only insert the work order in work order reserved table
				//why bcoz we are generating the permanent work order number while creating time only 
				if (!workOrderBean.getWorkOrderNo().contains("/R")&&workOrderBean.getRevision().equals("0")) {
					result1 = workControllerDao.insertIntoWOReservedNum(workOrderBean);
				}
			}
			
			listOfVerifiedEmpNames = workControllerDao.getWorkOrderVerifiedEmpNames(workOrderBean);
			String s=null;
			//s.trim();
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			WriteTrHistory.write("Site:"+site_Id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , Work Order Number:"+workOrderBean.getWorkOrderNo()+" rejected");
			response = "success";
			isMailSend = true;
		}catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			log.info("Work Order Rejection Failed");
			response = "Failed";
			e.printStackTrace();
			isMailSend = false;
			if(e.getMessage()!=null&&e.getMessage().equals("Work order number is not valid.")){
				response = "Work order number is not valid.";
			}
		}
		if(isMailSend&&!isSaveModifyOrUpdateOperation.equals("Discard")){
			constructEmailContentForRejetAndModify(workOrderBean, request,listOfVerifiedEmpNames, optionalCCmails);
		}
		return response;
	}
	@Override
	public String rejectPermanentWorkOrder(Model model, HttpSession session, HttpServletRequest request, String site_Id,
			String user_id) {
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in WOCr_rejWO, ");
		try
		{
			String workOrderNoSiteWise = request.getParameter("actualWorkOrderNo");
			String tempIssueId = request.getParameter("actualtempIssueId");
			String nextApprovelEmpId = request.getParameter("nextApprovelEmpId");
			String changedWorkOrderLevelPurpose= request.getParameter("Purpose");
			String actualWorkOrderLevelPurpose=request.getParameter("actualPurpose");
			String strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			changedWorkOrderLevelPurpose=actualWorkOrderLevelPurpose+"@@"+strUserName+" - "+changedWorkOrderLevelPurpose+"@@";
			
			int result1=workControllerDao.updatePermanentWOPurpose(tempIssueId,changedWorkOrderLevelPurpose);
			WriteTrHistory.write("Site:"+site_Id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , Work Order Number:"+workOrderNoSiteWise);
			//workControllerDao.updateWorkAreaMappingArea();
			WorkOrderBean bean=new WorkOrderBean();
			bean.setSiteWiseWONO(workOrderNoSiteWise);
			bean.setQS_Temp_Issue_Id(tempIssueId);
			bean.setApproverEmpId(nextApprovelEmpId);
			bean.setPurpose(changedWorkOrderLevelPurpose);
			int response2 = workControllerDao.rejectPermanentWorkOrderCreationCreation(bean);
String s=null;
//s.trim();
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
		}catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			log.info("Work Order Rejection Failed");
			response = "Failed";
			e.printStackTrace();
		}
		return response;
	}
	
	
@Override
public String updatePermanentWorkOrderDetails(WorkOrderBean workOrderBean, HttpServletRequest request,
		HttpSession session, Model model, String site_Id, String user_id, MultipartFile[] files) {
	
	
	String response = "";
	TransactionDefinition def = new DefaultTransactionDefinition();
	TransactionStatus status = transactionManager.getTransaction(def);
	WriteTrHistory.write("Tr_Opened in InCr_appInd, ");
	boolean isSendMail = true;

	workOrderBean.setWorkOrderNo(workOrderBean.getSiteWiseWONO());
	List<WorkOrderBean> workOrderRowDetailsBeans=new ArrayList<WorkOrderBean>();
	try {
		 String isUpdatePage="";
		Double woTotalAmount=0.0;
		String[] displayedRows = request.getParameterValues("dispplayedRows");
		String changedWoMajorHead = "", actualwoMajorHead = "";
		String changedWO_MinorHead = "", actualWO_MinorHead = "";
		String changedWO_Desc = "", actualWO_Desc = "";
		String changedUnitsOfMeasurement = "", actualunitsOfMeasurement = "";

		String changedQuantity = "", actualQuantity = "";
		String changedAcceptedRate = "", actualAcceptedRate = "";
		String changedTotalAmount = "", actualTotalAmount = "";
		String changedComments = "", actualComments = "";

		String tempIssueId = request.getParameter("actualtempIssueId");
		String actualQS_Temp_Issue_Dtls_Id = "";
		
		String workOrderNoSiteWise = request.getParameter("actualWorkOrderNo");
		String nextApprovelEmpId = request.getParameter("nextApprovelEmpId");
		isUpdatePage= request.getParameter("isUpdatePage");
		
		
		//String pendingEmpID = workControllerDao.getWorkOrderPendingEmployeeId(user_id);
		String actualWorkOrderLevelPurpose=request.getParameter("actualPurpose");
		String changedWorkOrderLevelPurpose=request.getParameter("Purpose")==null?"":request.getParameter("Purpose");
		String[] changedTC=request.getParameterValues("termsAndCOnditions");
		
		List<String> changedTCList=changedTC!=null?Arrays.asList(changedTC):new ArrayList<String>();
		List<Map<String, String>> termasAndCon=new ArrayList<Map<String, String>>();
		
		for (String string : changedTCList) {
			Map<String, String > map=new HashMap<String, String>();
			string=string.trim();
			if(string.length()!=0){
				map.put("TERMS_CONDITION_DESC", string);
				termasAndCon.add(map);
			}
		}
		//adding terms and condition in model object so we can print this terms and condition in work order print page
		session.setAttribute("listTermsAndCondition", termasAndCon);
		if(changedTC!=null){
			int response2=workControllerDao.insertPermanentTermsAndConditions(workOrderBean,tempIssueId,changedTC);	
		}
		
		String strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();				  
		
//		changedWorkOrderLevelPurpose=workOrderBean.getPurpose();
		int result1=0;
		
		if(!changedWorkOrderLevelPurpose.equals(""))
		if(!changedWorkOrderLevelPurpose.equals(actualWorkOrderLevelPurpose)){				
			changedWorkOrderLevelPurpose=actualWorkOrderLevelPurpose+"@@"+strUserName+" - "+changedWorkOrderLevelPurpose+"@@";
			
			 result1=workControllerDao.updatePermanentWOPurpose(tempIssueId,changedWorkOrderLevelPurpose);
		}
		
		for (String num : displayedRows) {
		boolean isRowDataChanged = false;
		String DeleteColVal = request.getParameter("isDelete" + num);
		boolean isDelete = false;
		if (DeleteColVal.equals("d")) {
			isDelete = true;
		}
		
		String QS_Temp_Issue_Dtls_Id=request.getParameter("QS_Temp_Issue_Dtls_Id"+num);
		
		changedWoMajorHead = request.getParameter("Product" + num);
		actualwoMajorHead = request.getParameter("actualwoMajorHead" + num);
		changedWO_MinorHead = request.getParameter("SubProduct" + num);
		actualWO_MinorHead = request.getParameter("actualWO_MinorHead" + num);
		changedWO_Desc = request.getParameter("ChildProduct" + num);
		actualWO_Desc = request.getParameter("actualWO_Desc" + num);
		changedUnitsOfMeasurement = request.getParameter("UnitsOfMeasurement" + num);
		actualunitsOfMeasurement = request.getParameter("actualunitsOfMeasurement" + num);
		changedQuantity = request.getParameter("Quantity" + num).trim();
		actualQuantity = request.getParameter("actualQuantity" + num).trim();
		changedAcceptedRate = request.getParameter("AcceptedRate" + num);
		actualAcceptedRate = request.getParameter("actualAcceptedRate" + num);
		changedTotalAmount = request.getParameter("TotalAmount" + num);
		actualTotalAmount = request.getParameter("actualTotalAmount" + num);
		changedComments = request.getParameter("Comments" + num);
		actualComments = request.getParameter("actualComments" + num);
		String woManualDesc = request.getParameter("woManualDesc" + num);
		
		workOrderBean.setTotalAmount1(changedTotalAmount);
			String[] scopeOfWork=request.getParameterValues("ScopeOfWork" + num);
				String ScopeOfWork="";
				if(scopeOfWork!=null)
				for (String scopeWork : scopeOfWork) {
					scopeWork=scopeWork.trim();
					if(scopeWork!=null&&scopeWork.length()!=0){
						ScopeOfWork+=scopeWork+"@@";
					}
				}
				
				woManualDesc=ScopeOfWork;
		
		if(!isDelete)
			woTotalAmount+=Double.valueOf(changedTotalAmount);
		
		
		WorkOrderBean actualWorkOderDetail = new WorkOrderBean();
		
		List<String> actualSelectedAreaList = null;
		actualWorkOderDetail.setSiteWiseWONO(workOrderNoSiteWise);
		actualWorkOderDetail.setQS_Temp_Issue_Id(tempIssueId);
		actualWorkOderDetail.setWoMajorHead(actualwoMajorHead);
		actualWorkOderDetail.setWoMinorHeads(actualWO_MinorHead);
		actualWorkOderDetail.setwODescription(actualWO_Desc);
		actualWorkOderDetail.setUnitsOfMeasurement(actualunitsOfMeasurement);
		actualWorkOderDetail.setQuantity(actualQuantity);
		actualWorkOderDetail.setAcceptedRate1(actualAcceptedRate);
		actualWorkOderDetail.setTotalAmount1(actualTotalAmount);
		actualWorkOderDetail.setComments1(actualComments);

		WorkOrderBean changedWorkOderDetail = new WorkOrderBean();
		List<String> changedSelectedAreaList =null;
		changedWorkOderDetail.setSiteWiseWONO(workOrderNoSiteWise);
		changedWorkOderDetail.setQS_Temp_Issue_Id(tempIssueId);
		try {
			changedWorkOderDetail.setWO_MajorHead1(changedWoMajorHead.split("\\$")[1]);
			changedWorkOderDetail.setWO_MinorHead1(changedWO_MinorHead.split("\\$")[1]);
			changedWorkOderDetail.setWO_Desc1(actualWO_Desc.split("\\$")[1]);//used actual values in changed
			changedWorkOderDetail.setUnitsOfMeasurement1(actualunitsOfMeasurement.split("\\$")[1]);
			changedWorkOderDetail.setwOManualDescription(woManualDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		changedWorkOderDetail.setWoMinorHeads(changedWO_MinorHead);
		changedWorkOderDetail.setwODescription(changedWO_Desc);
		
		changedWorkOderDetail.setQuantity(changedQuantity);
		changedWorkOderDetail.setAcceptedRate1(changedAcceptedRate);
		changedWorkOderDetail.setTotalAmount1(changedTotalAmount);
		changedWorkOderDetail.setComments1(changedComments);
		
		
		
		String strChangedComments="";
/*		if (!changedWoMajorHead.equalsIgnoreCase(actualwoMajorHead)) {
			isRowDataChanged = true;
			strChangedComments =    " Major Head "+actualwoMajorHead+" name changed to "+changedWoMajorHead+" ,";
		} else if (!changedWO_MinorHead.equals(actualWO_MinorHead)) {
			strChangedComments =    " Minor Head "+actualWO_MinorHead+" name changed to "+changedWO_MinorHead+" ,";
			isRowDataChanged = true;
		} else if (!changedWO_Desc.equals(actualWO_Desc)) {
			strChangedComments =" Work Order Description "+actualWO_Desc+" name changed to "+changedWO_Desc+" ,";
			isRowDataChanged = true;
		} */
		if (!changedQuantity.equals(actualQuantity)) {
			strChangedComments =    " Quantity "+actualQuantity+"  changed to "+changedQuantity+" ,";
			isRowDataChanged = true;
		} /*else if (!changedAcceptedRate.equalsIgnoreCase(actualAcceptedRate)) {
			String strModifyComment = strUserName + " was  changed Work Order actual accepted rate "+actualAcceptedRate		+" changed to "+ changedAcceptedRate+"" ;
			workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "M", user_id,strModifyComment);			
			isRowDataChanged = true;
		}*/ else if (!changedTotalAmount.equals(actualTotalAmount)) {
			isRowDataChanged = true;
		} else if (!changedComments.equals(actualComments)&&changedComments.length()!=0) {
			/*if (actualComments != null && !changedComments.equals("")) {
				actualComments = actualComments + "@@" + changedComments;
			} else {
				actualComments = changedComments;
			}*/
			isRowDataChanged = true;
		} else {
			log.info("row is not changed" + isRowDataChanged);
		}
		
		//boolean canWorkOrderUpdate=false;
		if(!isDelete){
			workOrderRowDetailsBeans.add(changedWorkOderDetail);
		}
		//if isDelete is true the make the work order inactive and changeQty is 0 then also make the product inactive
		  if (isDelete) { 
			  String[] tempIssueDtlsId=request.getParameterValues("actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID"+num);
			  
			  strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();			
			  String[] str=actualWO_Desc.split("\\$");
			  String	 strDeleteComment = strUserName + "- was deleted  order " +	  str[1] + " quantity "+actualQuantity;
			  String  strFinalChangedComments = strDeleteComment;
				int rerult=workControllerDao.doInActivePermanentWorkOder(QS_Temp_Issue_Dtls_Id,actualTotalAmount);
				List<Map<String, Object>> tmpIsssueDtlsId=workControllerDao.getPermanentTempIssDTLSId(QS_Temp_Issue_Dtls_Id,actualTotalAmount,true);
				for (Map<String, Object> map : tmpIsssueDtlsId) {
					String id=String.valueOf(map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID"));
					actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
					changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
					workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "D", user_id,strDeleteComment);			
					break;
				}
		  }// dnt touch
		  
		if (!isRowDataChanged) {
			int responce1 = workControllerDao.updatePermanentWorkOrder(workOrderBean, tempIssueId,workOrderNoSiteWise, nextApprovelEmpId,"");
		} else if (isRowDataChanged&&isDelete==false) {

			String[] workAreaId = request.getParameterValues("workAreaId" + num);
			String[] selectedArea = request.getParameterValues("selectedArea" + num);
			String[] actualArea = request.getParameterValues("actualArea" + num);
			String[] available = request.getParameterValues("available" + num);
			String[] percentage = request.getParameterValues("percentage" + num);
			String[] block_name=request.getParameterValues("BLOCK_NAME"+num);
			String[] floor_name=request.getParameterValues("FLOOR_NAME"+num);
			String[] acceptedRate= request.getParameterValues("accepted_rate" + num);
			
			String[] actualAreaAlocatedQTYPrice=request.getParameterValues("actualAreaAlocatedQTYPrice" + num);
			String[] actualworkAreaIds = request.getParameterValues("actualWorkAreaId" + num);
			String[] actualSelectedArea = request.getParameterValues("actualAreaAlocatedQTY" + num);
			String[] actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID = request.getParameterValues("actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID" + num);
			String[] actualBlockName1 = request.getParameterValues("BLOCK_NAME1" + num);

			String[] actualWorkOrderAreaData = request.getParameterValues("actualWorkOrderAreaDataId" + num);
			String[] changedWorkOrderAreaDataId = request.getParameterValues("changedWorkOrderAreaDataId" + num);

		changedSelectedAreaList = selectedArea != null ? Arrays.asList(selectedArea) : new ArrayList<String>();
		actualSelectedAreaList = actualSelectedArea != null ? Arrays.asList(actualSelectedArea)	: new ArrayList<String>();

		List<String> actualWorkAreaIdList = actualworkAreaIds != null ? Arrays.asList(actualworkAreaIds): new ArrayList<String>();
		List<String> changedWorkAreaIdList = workAreaId != null ? Arrays.asList(workAreaId): new ArrayList<String>();
		List<String> actualISSUE_DTLS_IDlIST = actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID != null? Arrays.asList(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID) : new ArrayList<String>();
		
			 if (!changedTotalAmount.equalsIgnoreCase(actualTotalAmount)) {
				 String[] str=actualWO_Desc.split("\\$");
				 String strModifyComment = strUserName + "- was  changed Work Order "+str[1]+" actual Total Amount "+actualTotalAmount		+" changed to "+ changedTotalAmount+"" ;
			/*for (int i = 0; i < actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID.length; i++) {
				String id = actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i];
				String strWorkAreaId=actualworkAreaIds[i];
				actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
				changedWorkOderDetail.setWorkAreaId(strWorkAreaId);
				changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(id);
				actualWorkOderDetail.setWorkAreaId(strWorkAreaId);
				
			}*///
			//workControllerDao.updatePermanentWorkOrderPriceByTmpIssuId("",changedAcceptedRate,tempIssueId,actualTotalAmount,changedTotalAmount,changedQuantity);
			workControllerDao.updatePermanentWorkOrderPriceByTmpIssuId("",changedAcceptedRate,QS_Temp_Issue_Dtls_Id,actualTotalAmount,changedTotalAmount,changedQuantity);
		}
			if(actualworkAreaIds!=null&&workAreaId!=null)
			for (int i = 0; i < actualworkAreaIds.length; i++) {
				for (int j = 0; j < workAreaId.length; j++) {
					//check the work area id is equal to actual workIds from changed WorkIds
					if (workAreaId[j].equals(actualworkAreaIds[i])) {
						//if area quantity changed
						if(!actualSelectedArea[i].equals(selectedArea[j])){
					
							changedWorkOderDetail.setSelectedArea(selectedArea[i]);
							changedWorkOderDetail.setWorkAreaId(workAreaId[j]);
							changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
							changedWorkOderDetail.setPercentage(percentage[i]);
							changedWorkOderDetail.setAcceptedRate1(acceptedRate[i]);
							
							actualWorkOderDetail.setAcceptedRate1(acceptedRate[i]);
							actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
							actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
							actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
							//updating the qty and price
							
							workControllerDao.updatePermanentWorkOrderCreationDetails(changedWorkOderDetail,actualWorkOderDetail);
							
							int actual=Integer.parseInt(actualSelectedArea[i]);
							int  changed=Integer.parseInt(selectedArea[i]);
							if(actual<changed){
								changedWorkOderDetail.setSelectedArea(String.valueOf(Math.abs(actual-changed)));
								changedWorkOderDetail.setCondition("1");
							}else if(actual>changed){
								changedWorkOderDetail.setCondition("2");
								changedWorkOderDetail.setSelectedArea(String.valueOf(Math.abs(actual-changed)));
							}
							
							String strModifyComment = strUserName + "- was  changed Work Order "+actualWO_Desc.split("\\$")[1]+" actual quantity "+actualSelectedArea[i]+" changed to "+ selectedArea[j]+"" ;
							//	modified the work order allocated area 
							workControllerDao.updateWorkAreaMapping(changedWorkOderDetail,nextApprovelEmpId,"modify");
							workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "M", user_id,strModifyComment);			
						
								break;
							}else if(!actualAreaAlocatedQTYPrice[i].equals(acceptedRate[j])){
								changedWorkOderDetail.setSelectedArea(selectedArea[i]);
								changedWorkOderDetail.setWorkAreaId(workAreaId[j]);
								changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
								changedWorkOderDetail.setPercentage(percentage[i]);
								changedWorkOderDetail.setAcceptedRate1(acceptedRate[i]);
								
								actualWorkOderDetail.setAcceptedRate1(actualAreaAlocatedQTYPrice[i]);
								actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
								actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
								actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
								
								String strModifyComment = strUserName + " was  changed Work Order "+actualWO_Desc.split("\\$")[1]+" actual Accepted Rate "+actualAreaAlocatedQTYPrice[i]+" changed to "+ acceptedRate[j]+"\n" ;
								workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "M", user_id,strModifyComment);
								workControllerDao.updatePermanentWorkOrderPriceByTmpIssuId(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i],acceptedRate[i],"","",changedTotalAmount,changedQuantity);
						}
					}
				}
			}
			
			//if the actualworkAreaIds is not available in changed changedWorkAreaIdList that means the work is removed while approving the work order
			if(actualworkAreaIds!=null)
			for (int i = 0; i < actualworkAreaIds.length; i++) {
				if (!changedWorkAreaIdList.contains(actualworkAreaIds[i])) {
					int i1=workControllerDao.doInActivePermanentCurrentWorkOder(actualworkAreaIds[i],tempIssueId,actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
					changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
					String strDeleteComment = strUserName + "- was deleted Work Order "+actualWO_Desc.split("\\$")[1]+" allocated area  "+actualSelectedArea[i]+" fro  "+actualBlockName1[i];
					actualWorkOderDetail.setSelectedArea(actualSelectedArea[i]);
					actualWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
					changedWorkOderDetail.setWorkAreaId(actualworkAreaIds[i]);
					actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(actualWO_WORK_TEMP_ISSUE_AREA_DTLS_ID[i]);
					//deleted the work order and  reallocate the allocated area
					workControllerDao.updateWorkAreaMapping(actualWorkOderDetail,nextApprovelEmpId,"deleted");
					workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "D", user_id,strDeleteComment);
				}
			}
			
			//the workAreaId[i] is not available in actual workAreaId that means this is the new work area added while revise work order
			if(workAreaId!=null)
			for (int i = 0; i < workAreaId.length; i++) {
				if (!actualWorkAreaIdList.contains(workAreaId[i])) {
					if (actualArea[i] != null && selectedArea[i] != null && workAreaId[i] != null) {
						WorkOrderBean bean = new WorkOrderBean();
						bean.setTotalAmount1(changedTotalAmount.trim());
						bean.setAcceptedRate1(acceptedRate[i]);
						bean.setQuantity1(changedQuantity.trim());
						bean.setSelectedArea(selectedArea[i]);
						bean.setActualArea(actualArea[i]);
						bean.setWorkAreaId(workAreaId[i]);
						bean.setPercentage(percentage[i]);
						bean.setComments1(changedComments);
						bean.setwOManualDescription(woManualDesc);

						List<Integer> tempDetailresp = workControllerDao.insertPermanentQSTEMPDetails(bean,QS_Temp_Issue_Dtls_Id);
						String TEMP_ISSUE_DTLS_ID=tempDetailresp.get(0).toString();
						changedWorkOderDetail.setQS_Temp_Issue_Dtls_Id(TEMP_ISSUE_DTLS_ID);
						changedWorkOderDetail.setSelectedArea(selectedArea[i]);
						changedWorkOderDetail.setWorkAreaId(workAreaId[i]);
						actualWorkOderDetail.setWorkAreaId("0");
						actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(TEMP_ISSUE_DTLS_ID);		
						//canWorkOrderUpdate=true;
						String strAddedComment = strUserName + "- was  added Work Order "+actualWO_Desc.split("\\$")[1]+" allocated area  "+selectedArea[i]+" for  "+block_name[i];
						workControllerDao.insertWorkOrderChangedDetails(actualWorkOderDetail,changedWorkOderDetail, tempIssueId, nextApprovelEmpId, "A", user_id,strAddedComment);
						//new added work order area 
						workControllerDao.updateWorkAreaMapping(bean,nextApprovelEmpId,"");
					}
				}
			}
				//int responce1 = workControllerDao.updatePermanentWorkOrder(workOrderBean, tempIssueId,workOrderNoSiteWise, nextApprovelEmpId,"");
		}// isRowDataChangedcondition end
			
			if (!changedComments.equals(actualComments)) {
				if (actualComments != null && !changedComments.equals("")) {
					changedComments = actualComments + "--@@" + changedComments + "@@";//
					//workControllerDao.updatePermanentWorkOrderComments(tempIssueId, changedComments,changedAcceptedRate);
					workControllerDao.updatePermanentWorkOrderComments(QS_Temp_Issue_Dtls_Id, changedComments,changedAcceptedRate);
				} 
			}

		}//for loop end 
		
		workOrderBean.setTotalWoAmount(String.valueOf(woTotalAmount));
		//updating the next level approver
		int responce1 = workControllerDao.updatePermanentWorkOrder(workOrderBean, tempIssueId,workOrderNoSiteWise, nextApprovelEmpId,"insert");
		session.setAttribute("workOrderCreationList", workOrderRowDetailsBeans);
		
		transactionManager.commit(status);
		WriteTrHistory.write("Tr_Completed");
		
		
		response = "success";
	} catch (Exception e) {
		transactionManager.rollback(status);
		WriteTrHistory.write("Tr_Completed");
		log.info(" Approve Failed");
		response = "failed";
		e.printStackTrace();
		isSendMail = false;
	}
	return response;
}
	
	@Override
	public List<WorkOrderBean> getMyWOPendingStatusDetail(String fromDate, String toDate, String site_Id,String workOrderNumber) {
		return workControllerDao.getMyWorkOrderStatus(fromDate,toDate,site_Id,workOrderNumber);
	}
	
	/**
	 * calling dao to get the site wise work order data 
	 */
	@Override
	public List<WorkOrderBean> getSitewiseWOPendingStatusDetail(String fromDate, String toDate, String site_Id,String workOrderNumber) {
		return workControllerDao.getSitewiseWorkOrderStatus(fromDate,toDate,site_Id,workOrderNumber);
	}
	/**
	 *calling to dao layer to load the temporary data for update purpose
	 */
	@Override
	public List<WorkOrderBean> getMyTempWOForUpdateDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String user_id) {	
		return workControllerDao.getMyTempWOForUpdateDetail(fromDate,toDate,site_Id,workOrderNumber,user_id);
	}

	@Override
	public List<WorkOrderBean> getDeletedProductDetailsLists(String workOrderNo, String user_id, int site_Id, String TypeOfWork) {
		return workControllerDao.getDeletedProductDetailsLists( workOrderNo,  user_id,  site_Id,TypeOfWork);
	}

	@Override
	public List<WorkOrderBean> getMyWOCompltedDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber,String typeOfWork, String contractorId) {
		
		return workControllerDao.getMyWOCompltedDetail( fromDate,  toDate,  site_Id, workOrderNumber,typeOfWork,contractorId);
	}
	@Override
	public List<WorkOrderBean> getCompltedWorkOrderDetailForRevise(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String typeOfWork, String contractorId) {
		return workControllerDao.getCompltedWorkOrderDetailForRevise( fromDate,  toDate,  site_Id, workOrderNumber,typeOfWork,contractorId);
	}

	@Override
	public List<WorkOrderBean> getSitewiseWOCompltedDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber,String user_id, String contractorId) {
		
		return workControllerDao.getSitewiseWOCompltedDetail( fromDate,  toDate,  site_Id, workOrderNumber,user_id,contractorId);
	}
	@Override
	public boolean checkCompletedWorkOrderNumberIsValidForEmployee(String siteWiseWorkOrderNo, String user_id,
			boolean status, String site_Id, String operType) {
		
		return workControllerDao.checkCompletedWorkOrderNumberIsValidForEmployee( siteWiseWorkOrderNo,  user_id,status,site_Id,  operType);
	}

	@Override
	public Map<String, Object> loadActiveWorkOrderDetails(WorkOrderBean bean) {
		return workControllerDao.loadActiveWorkOrderDetails(bean);
		
	}
	@Override
	public List<WorkOrderBean> getALLCompltedWorkOrderS(String workOrderIssueNo, String user_id, String operType,
			boolean status) {
	
		return workControllerDao.getALLCompltedWorkOrderS( workOrderIssueNo,  user_id,  operType,	 status) ;
	}

	@Override
	public WorkOrderBean getCompletedWorkOrderDetailsByWorkOrderNumber(String workOrderNo, String workOrderIssueNo) {
	return workControllerDao.getCompletedWorkOrderDetailsByWorkOrderNumber( workOrderNo,workOrderIssueNo);
	}

	@Override
	public String getPermanentWorkOrderIssueIdNumber(String workOrderNo, String site_Id, String user_id) {
		return workControllerDao.getPermanentWorkOrderIssueIdNumber( workOrderNo,  site_Id,  user_id);
	}

	/**
	 * loading the terms and condition from properties file for first time while creating work order
	 */
	@Override
	public List<WorkOrderBean> getTermsAndConditions(String siteId) {
		List<WorkOrderBean> listTermsAndcondtion = new ArrayList<WorkOrderBean>();
		WorkOrderBean objWODetails = null;

		int count =0;
		while(true) {
			objWODetails = new WorkOrderBean();
			count = count+1;
			//112_TELANGANA_QS_TC1
			//loading terms and condition's one by one 
			String condition = ValidateParams.validateParams.getProperty(siteId+"_TELANGANA_QS_TC"+count);
			if(condition==null){break;}
			else{
				objWODetails.setIndexNumber(count);
				objWODetails.setStrTermsConditionName(condition);
				listTermsAndcondtion.add(objWODetails); 
			}
		}
		return listTermsAndcondtion;
	}

	/**
	 * loading work order terms and conditions 
	 * calling to dao to return the terms and conditions
	 */
	@Override
	public List<Map<String, Object>> loadTermsAndConditions(String tempIssueId,String opertype) {
		return workControllerDao.loadTermsAndConditions(tempIssueId,opertype);
	}
	
	@Override
	public List<Map<String, Object>> getWorkOrderVerifiedEmpNames(WorkOrderBean bean) {
		return  workControllerDao.getWorkOrderVerifiedEmpNames(bean);
	}
	@Override
	public List<Map<String, Object>> getPermanentWorkOrderVerifiedEmpNames(WorkOrderBean bean) {
	
		return   workControllerDao.getPermanentWorkOrderVerifiedEmpNames(bean);
	}
	@Override
	public List<Map<String, Object>> getModificationDetailsList(String workOrderIssueNo, String user_id) {
		
		return workControllerDao.getModificationDetailsList(workOrderIssueNo,user_id);
	}
	
	@Override
	public List<WorkOrderBean> getAllRevisedWorkOrderList(WorkOrderBean workOrderBean, String typeOfWork) {
		
		return workControllerDao.getAllRevisedWorkOrderList(workOrderBean,typeOfWork);
	}
	
	@Override
	public Map<String,String> loadWorkDsc(ContractorQSBillBean billBean){
		return workControllerDao.loadWorkDsc(billBean);
	}

	/**
	 * this method will call to dao layer to get all the work order number using contractor name
	 */
	@Override
	public String getWorkOrderDetails(String steWorkDescId,String strControctorId, int siteId, String strTypeOfWork){
		return workControllerDao.getWorkOrderDetails( steWorkDescId,strControctorId,siteId,strTypeOfWork);
	}
	
	@Override
	public List<Map<String, Object>> autoCompleteWorkOrderNo(HttpServletRequest request) {
	 	String strTypeOfWork = request.getParameter("typeOfWork") == null ? "" : request.getParameter("typeOfWork").toString();
	 	String workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		int siteId = Integer.parseInt(request.getParameter("siteId") == null ? "0" : request.getParameter("siteId").toString().trim());
		return workControllerDao.autoCompleteWorkOrderNo(siteId,strTypeOfWork,workOrderNo);
	}

	//NMR Methods
	@Override
	public Map<String, String> loadNMRProducts(String[] data) {
		
		return workControllerDao.loadNMRProducts(data);
	}
 
}
