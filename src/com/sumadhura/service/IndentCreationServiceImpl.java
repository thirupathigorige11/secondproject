package com.sumadhura.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.transdao.CentralSiteIndentProcessDao;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.PurchaseDepartmentIndentProcessDao;
import com.sumadhura.util.BoxToNos;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.MobileSMS;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.util.ValidateParams;

@Service("posClass")
public class IndentCreationServiceImpl extends UIProperties implements IndentCreationService {

	@Autowired
	private IndentCreationDao icd;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;

	@Autowired
	MobileSMS mobileSMS;	

	@Autowired
	@Qualifier("itosc")
	IssueToOtherSiteServiceImpl objIssueToOtherSiteServiceImpl;

	
	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;
	
	@Override
	public String indentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String user_Name) {

		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_indCre, ");
		
		
		boolean isSendMail = true;
		//String strIndentTo = "";
		//String strIndentFrom = "";
		//int indentNumber = 0; 

		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final IndentCreationDto indentCreationDto = new IndentCreationDto();
		//IndentCreationBean changedIndentDetails = new IndentCreationBean();
		int indentNumber= 0;
		String strIndentFrom= "";
		String strIndentTo = "";
		String purpose = "";
		int siteWiseIndentNo = 0;
		int portNo =0; 
		String siteName = "";
		String strpendingEmpId ="";
		String pendingEmployeeId="";
		String pendingEmpId ="";
		String checkCentralIndentOrNot="";
		String indentName="";
		String central_dept_id = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
		String purchase_dept_id = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		String createdBy="STORE";
		boolean isMeasurementConvert=false;
		/*if(site_id==999){
		 strpendingEmpId = icd.getPendingEmployeeId(user_id,site_id);//this is for central indent
		 pendingEmployeeId=icd.getPendingEmployeeId(strpendingEmpId,site_id);
		}*/
		try
		{
			
			
			indentNumber= Integer.parseInt(request.getParameter("IndentNumber"));
			siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			strIndentFrom= request.getParameter("IndentFrom");
			strIndentTo = request.getParameter("IndentTo");
			purpose = request.getParameter("Purpose");
			indentName=request.getParameter("indentName");
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);

			Date scheduleDate= new SimpleDateFormat("dd-MMM-yy").parse(request.getParameter("ScheduleDate"));
			Date requiredDate= new SimpleDateFormat("dd-MMM-yy").parse(request.getParameter("RequiredDate"));



			String versionNo=ValidateParams.validateParams.getProperty("indent_print_version_No");
			String reference_No=ValidateParams.validateParams.getProperty("indent_print_reference_No");
			String issue_date=ValidateParams.validateParams.getProperty("indent_print_issue_date");

			System.out.println("versionNo "+versionNo+" reference_No "+reference_No+" issue_date "+issue_date);
			// for cancel po purpose if user add any new product at the time of product creation to identify which added in purchase dept write below code
			if(site_id ==Integer.valueOf(purchase_dept_id)){
				createdBy="PURCHASE";
			}else if(site_id ==Integer.valueOf(central_dept_id) ){
				createdBy="CENTRAL";
			}
			
			indentCreationDto.setVersionNo(versionNo);
			indentCreationDto.setReference_No(reference_No);
			indentCreationDto.setIssue_date(issue_date);

			indentCreationDto.setSiteWiseIndentNo(siteWiseIndentNo);
			indentCreationDto.setIndentNumber(indentNumber);
			indentCreationDto.setScheduleDate(scheduleDate);
			indentCreationDto.setSiteId(site_id);
			indentCreationDto.setUserId(user_id);
			indentCreationDto.setPurpose(purpose);
			indentCreationDto.setRequiredDate(requiredDate);
			indentCreationDto.setTempPass(CommonUtilities.getStan());
			indentCreationDto.setIndentName(indentName);
			pendingEmpId = request.getParameter("approverEmpId");
			indentCreationDto.setPendingEmpId(pendingEmpId);
			indentCreationDto.setPendingDeptId("-");
			int response1 =  icd.insertIndentCreation(indentNumber, indentCreationDto);
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			int response3 =  icd.insertIndentCreationApproval(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			int response2 = 0;
			double doubleAvailableQuantity = 0.0;
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			logger.info("Rows To Be Processed = "+recordsCount);
			//int countOfRecords = Integer.valueOf(recordsCount);
			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			if(numOfRec != null && numOfRec.length > 0) {
				for(String num : numOfRec) {
					num = num.trim();

					//PROD101$Cement <--> SUBPROD101$KCP <--> CHLDPROD101$TetraBag <--> tyty <--> 43 <--> 101$KG
					String product = request.getParameter("Product"+num);				
					String prodsInfo[] = product.split("\\$");
					String prodId = prodsInfo[0];
					String prodName = prodsInfo[1];
					//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);					
					String subProduct = request.getParameter("SubProduct"+num);
					String subProdsInfo[] = subProduct.split("\\$");
					String subProdId = subProdsInfo[0];
					String subProdName = subProdsInfo[1];					
					//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
					String childProduct = request.getParameter("ChildProduct"+num);

					String childProdsInfo[] = childProduct.split("\\$");
					String childProdId = childProdsInfo[0];
					String childProdName = childProdsInfo[1];					
					//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);
					String unitsOfMeasurement = request.getParameter("UnitsOfMeasurement"+num);
					String measurementsInfo[] = unitsOfMeasurement.split("\\$");
					String measurementId = measurementsInfo[0];
					String measurementName = measurementsInfo[1];
					//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);
					String requiredQuantity = request.getParameter("Quantity"+num);
					String availableQuantity = request.getParameter("ProductAvailability"+num);
					String groupId=request.getParameter("groupId"+num)==null ? "0" : request.getParameter("groupId"+num);;
					String remarks = request.getParameter("Remarks"+num);
					Map<String,String> map =null;
					if(measurementName.equals("Box"))
					{
						BoxToNos btn = new BoxToNos();
						map = btn.convertBoxToNos(childProdId,requiredQuantity,measurementName,measurementId);
						measurementId = map.get("MeasurementId");
						measurementName = map.get("MeasurementName");
						requiredQuantity = map.get("Quantity");
					}
					/***************************************** for convert the child product measurement start************************/
					String methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();
					// here checking the child product which available in properties files then 
					if(!methodName.equals("")) {	
						Map<String, String> values = null;

						String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassNameForIndent") == null ? "" : validateParams.getProperty("MesurmentConversionClassNameForIndent").toString();

						//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
						Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
						Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


						double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
						double doubleInputQuantity =  Double.valueOf(requiredQuantity);
						String strAvailableQuantityToConvert=requiredQuantity;
						String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
						// with multiple parameters
						//methodName = "convertCHP00536";
						Class<?>[] paramTypes = {double.class,double.class, String.class};
						Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
						values = (Map<String, String>) printDogMethod.invoke(mesurment,doubleActualQuantity,doubleInputQuantity,measurementName);
						
						for(Map.Entry<String, String> retVal : values.entrySet()) {
							BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
							requiredQuantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
							//quantity=retVal.getKey();
							//prc=retVal.getValue(); 
						}
						if(!requiredQuantity.equals(strAvailableQuantityToConvert)){
						isMeasurementConvert=true;
						doubleAvailableQuantity = Double.valueOf(availableQuantity);
						doubleAvailableQuantity=(doubleAvailableQuantity)*(doubleActualQuantity);
						doubleAvailableQuantity  = Double.parseDouble(new DecimalFormat("##.##").format(doubleAvailableQuantity));
						availableQuantity = String.valueOf(doubleAvailableQuantity);
						}
						//quantity =  String.valueOf(doubleQuantity);
						/*if(measurementName.equalsIgnoreCase("PCS")){
							
							double price=Double.parseDouble(basicAmnt)/doubleQuantity;
							prc=String.valueOf(price);
						}*/
						measurementId = strConversionMesurmentId;
						measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
					}
					/***************************************** for convert the child product measurement end************************/
					if(!isMeasurementConvert){
					doubleAvailableQuantity = Double.valueOf(availableQuantity);
					doubleAvailableQuantity  = Double.parseDouble(new DecimalFormat("##.##").format(doubleAvailableQuantity));
					availableQuantity = String.valueOf(doubleAvailableQuantity);
					}
					int indentCreationDetailsSeqNum = icd.getIndentCreationDetailsSequenceNumber();

					IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
					indentCreationDetailsDto.setProdId(prodId);
					indentCreationDetailsDto.setProdName(prodName);
					indentCreationDetailsDto.setSubProdId(subProdId);
					indentCreationDetailsDto.setSubProdName(subProdName);
					indentCreationDetailsDto.setChildProdId(childProdId);
					indentCreationDetailsDto.setChildProdName(childProdName);
					indentCreationDetailsDto.setMeasurementId(measurementId);
					indentCreationDetailsDto.setMeasurementName(measurementName);
					indentCreationDetailsDto.setGroupId(groupId); // for material boq purpose written one
					indentCreationDetailsDto.setRequiredQuantity(requiredQuantity);
					indentCreationDetailsDto.setAvailableQuantity(availableQuantity);
					indentCreationDetailsDto.setRemarks(remarks);
					indentCreationDetailsDto.setCreatedBy(createdBy);// this one is added newly for which one created
					listProductDetails.add(indentCreationDetailsDto);

					response2 +=  icd.insertIndentCreationDetails(indentCreationDetailsSeqNum, indentNumber, indentCreationDetailsDto);
					
					/*if((site_id==999 && (pendingEmployeeId.equals("") || pendingEmployeeId.equals("-")))){
						IndentCreationBean changedIndentDetails = new IndentCreationBean();
						changedIndentDetails.setProductId1(prodId);
						changedIndentDetails.setSubProductId1(subProdId);
						changedIndentDetails.setChildProductId1(childProdId);
						changedIndentDetails.setUnitsOfMeasurementId1(measurementId);
						changedIndentDetails.setRequiredQuantity1(requiredQuantity);
						
						int centralIndentProcessId = icd.getCentralIndentProcessSequenceNumber();
						icd.insertCentralIndentProcess(centralIndentProcessId,changedIndentDetails,indentCreationDetailsSeqNum,site_id);
						icd.updateIndentCreationForCentral(pendingEmployeeId,site_id,indentNumber);//for central indent 
						indentCreationDto.setPendingDeptId(String.valueOf(site_id));
						indentCreationDto.setPendingEmpId("-");
						 checkCentralIndentOrNot="true";
						
					}*/



				}//end of FOR loop

			}//end of IF block
		//	System.out.println("Indent creation Success");
			//System.out.println("responses: "+response1+", "+response2+", "+response3);
			transactionManager.commit(status);
		//	System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			portNo=request.getLocalPort();

		}//end of TRY block
		catch(Exception e){
			transactionManager.rollback(status);
			//System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "Failed";
			e.printStackTrace();
			isSendMail = false;
		}
		


		if(isSendMail){






			//purpose = getIndentLevelComments(indentNumber);
			//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
			//sendEmailDetails( "", indentNumber, strIndentFrom, strIndentTo,listProductDetails, indentCreationDto,purpose,null);


			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{
				final String finalPendingEmpId = pendingEmpId;
				final String finalcheckCentralIndentOrNot=checkCentralIndentOrNot;
				final int indentNumber_final = indentNumber;
				final String strIndentFrom_final = strIndentFrom;
				final String strIndentTo_final = strIndentTo;
				final String purpose_final = purpose;
				final int siteWiseIndentNo_final = siteWiseIndentNo;
				final int portNo_final=portNo;
				final String strUserName = user_Name;
				final String strindentName=indentName;
				
				executorService.execute(new Runnable() {
					public void run() {

						if(finalcheckCentralIndentOrNot.equalsIgnoreCase("true")){
							sendEmailDetails( "-", indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDto,purpose_final,null,siteWiseIndentNo_final,portNo_final,strUserName,strindentName);

						}else{

						//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
						sendEmailDetails( finalPendingEmpId, indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDto,purpose_final,null,siteWiseIndentNo_final,portNo_final,strUserName,strindentName);
						}

					}


				});




				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}


		//this try catch for emil genaration

		return response;
	}

	@Override
	public String approveIndentCreationFromMail( HttpServletRequest request,int site_id, String user_id,String userName) {
		String response = "";
		boolean isSendMail = true;
		String strIndentTo = "";
		String strIndentFrom = "";
		int reqSiteId = 0;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_appInM, ");
		//int indentNumber = 0;
		String pendingEmpId = "";
		String strPurpose = "";
		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final List<IndentCreationBean> editList = new ArrayList<IndentCreationBean>();
		final IndentCreationDto indentCreationDtoForMail = new IndentCreationDto();
		int indentNumber = 0;
		int siteWiseIndentNo = 0;
		int portNo=0;
		try {
			indentNumber = Integer.parseInt(request.getParameter("indentNumber"));
			siteWiseIndentNo = icd.getSiteWiseIndentNo(indentNumber);
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			//System.out.println(request.getParameter("tempPass")+"-"+icd.getTempPasswordOfIndent(indentNumber));
			if(request.getParameter("tempPass").equals(icd.getTempPasswordOfIndent(indentNumber))){}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return "WrongPassword";
				}

			strIndentFrom= icd.getIndentFrom(indentNumber);
			strIndentTo= icd.getIndentTo(user_id);
			portNo=request.getLocalPort();
			List<IndentCreationBean> indentDetails = icd.getIndentCreationLists(indentNumber);
			List<IndentCreationBean> productslist = icd.getIndentCreationDetailsLists(indentNumber);
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			reqSiteId = indentDetails.get(0).getSiteId();


			String strEditComments = indentDetails.get(0).getMaterialEditComment();

			if(strEditComments.contains("@@@")){
				String strEditCommentsArr [] = strEditComments.split("@@@");
				for(int j = 0; j< strEditCommentsArr.length;j++){
					IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
					objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
					editList.add(objCommentIndentCreationBean);
				}


			}


			indentCreationDto.setSiteId(reqSiteId);//reqSiteId
			indentCreationDtoForMail.setSiteId(reqSiteId);
			indentCreationDto.setUserId(user_id);
			String comment=request.getParameter("comment");
			indentCreationDto.setPurpose(comment);//purpose
			String temppass = CommonUtilities.getStan();
			indentCreationDto.setTempPass(temppass);
			indentCreationDtoForMail.setTempPass(temppass);
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();

			int response1 = icd.insertIndentCreationApprovalAsApprove(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			pendingEmpId = icd.getPendingEmployeeId(user_id,site_id);
			indentCreationDtoForMail.setPendingEmpId(pendingEmpId);
			String pendingDeptId = "-";
			if(pendingEmpId.equals("-"))
			{
				pendingDeptId = icd.getPendingDeptId(user_id,site_id);
			}
			int response2 = icd.updateIndentCreation(indentNumber, pendingEmpId, pendingDeptId, indentCreationDto,"approving from Mail");
			int noofrows = Integer.parseInt(request.getParameter("noofRowsTobeProcessed"));

			String centralDeptId = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
			String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			for(int num=1;num<=noofrows;num++)
			{
				IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
				indentCreationDetailsDto.setProdId(productslist.get(num-1).getProductId1());
				indentCreationDetailsDto.setProdName(productslist.get(num-1).getProduct1());
				indentCreationDetailsDto.setSubProdId(productslist.get(num-1).getSubProductId1());
				indentCreationDetailsDto.setSubProdName(productslist.get(num-1).getSubProduct1());
				indentCreationDetailsDto.setChildProdId(productslist.get(num-1).getChildProductId1());
				indentCreationDetailsDto.setChildProdName(productslist.get(num-1).getChildProduct1());
				indentCreationDetailsDto.setMeasurementId(productslist.get(num-1).getUnitsOfMeasurementId1());
				indentCreationDetailsDto.setMeasurementName(productslist.get(num-1).getUnitsOfMeasurement1());
				indentCreationDetailsDto.setGroupId(productslist.get(num-1).getGroupId1());
				
				indentCreationDetailsDto.setRequiredQuantity(productslist.get(num-1).getRequiredQuantity1());
				indentCreationDetailsDto.setRemarks(productslist.get(num-1).getRemarks1());
				listProductDetails.add(indentCreationDetailsDto);




				strPurpose = icd.getIndentLevelComments(indentNumber);
				request.setAttribute("indentLevelComments", "strPurpose");



				// To Give Indent to Central
				if(pendingEmpId.equals("-")&&pendingDeptId.equals(centralDeptId))
				{
					int centralIndentProcessId = icd.getCentralIndentProcessSequenceNumber();
					icd.insertCentralIndentProcess(centralIndentProcessId,productslist.get(num-1),productslist.get(num-1).getIndentCreationDetailsId(),reqSiteId);
				}else if(pendingEmpId.equals("-")&&pendingDeptId.equals(purchaseDeptId))
				{
					int indentProcessId = cntlIndentrocss.getPurchaseIndentProcessSequenceNumber();
					objPurchaseDepartmentIndentProcessDao.insertPurchaseIndentProcess(indentProcessId,productslist.get(num-1),productslist.get(num-1).getIndentCreationDetailsId(),reqSiteId,user_id);
				}
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Indent Approval Success via EMail");
			response = "Success";
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("Indent Approval Failed via EMail");
			response = "Failed";
			isSendMail = false;
			e.printStackTrace();
		}
		if(isSendMail){
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{

				final String finalPendingEmpId = pendingEmpId;
				final String finalStrIndentFrom =strIndentFrom;
				final String finalStrIndentTo = strIndentTo;
				final String finalStrPurpose = strPurpose;
				final int indentNumber_final = indentNumber;
				final int siteWiseIndentNo_final = siteWiseIndentNo;
				final int portNo_final=portNo;
				final String strUserName = userName;
				executorService.execute(new Runnable() {
					public void run() {



						//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
						sendEmailDetails( finalPendingEmpId, indentNumber_final, finalStrIndentFrom, finalStrIndentTo,listProductDetails, indentCreationDtoForMail,finalStrPurpose,editList,siteWiseIndentNo_final,portNo_final,strUserName,"");


					}


				});




				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}
		}
		/*if(isSendMail){


			//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
			sendEmailDetails( pendingEmpId, indentNumber, strIndentFrom, strIndentTo,listProductDetails, indentCreationDtoForMail,strPurpose,editList);
		}*/
		return response;
	}	

	@SuppressWarnings("unchecked")
	@Override
	public String approveIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,HttpSession session) {
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_appInd, ");
		String user_Name=session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
		boolean isSendMail = true;
		//String strIndentTo = "";
		//String strIndentFrom = "";
		
		String pendingEmpId = "";
		String purpose = "";
		String strFinalChangedComments = "";
		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final IndentCreationDto indentCreationDtoForMail = new IndentCreationDto();
		int indentNumber = 0;
		String strIndentFrom = "";
		String strIndentTo = "";
		int siteWiseIndentNo = 0;
		int intPOrtNo = 0;
		String centralstrpendingEmpId="";//central indent approve when indent create in centralk
		String centralpendingEmployeeId="";//central indent approve when indent create in centralk
		String checkCentralIndentOrNot="";
		String indentName="";
		try
		{
			indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			strIndentFrom = request.getParameter("IndentFrom");
			strIndentTo = request.getParameter("IndentTo");
		
			Date scheduleDate= new SimpleDateFormat("dd-MMM-yy").parse("01-JAN-18");
			intPOrtNo = request.getLocalPort();
			
			Date requiredDate= new SimpleDateFormat("dd-MMM-yy").parse("01-JAN-18");
			purpose = request.getParameter("Note");
			indentName=request.getParameter("indentName");
			int reqSiteId = Integer.parseInt(request.getParameter("reqSiteId"));
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			indentCreationDto.setSiteId(reqSiteId);
			indentCreationDtoForMail.setSiteId(reqSiteId);
			indentCreationDto.setUserId(user_id);
			indentCreationDto.setPurpose(purpose);
			String temppass = CommonUtilities.getStan();
			indentCreationDto.setTempPass(temppass);
			indentCreationDtoForMail.setTempPass(temppass);
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			int response1 = icd.insertIndentCreationApprovalAsApprove(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			pendingEmpId = icd.getPendingEmployeeId(user_id,reqSiteId);
			
			String pendingDeptId = "-";
			if(pendingEmpId.equals("-"))
			{
				pendingDeptId = icd.getPendingDeptId(user_id,site_id);
			}
			/*if(site_id==999){
				centralstrpendingEmpId = icd.getPendingEmployeeId(user_id,site_id);//this is for central indent
				centralpendingEmployeeId=icd.getPendingEmployeeId(centralstrpendingEmpId,site_id);
				}*/
			
			indentCreationDtoForMail.setPendingEmpId(pendingEmpId);
			indentCreationDtoForMail.setPendingDeptId(pendingDeptId);

			int noofrows = Integer.parseInt(request.getParameter("noofRowsTobeProcessed"));//2;//give this value dynamically
			//System.out.println("noofrows: >>>"+noofrows+"<<<");
			String centralDeptId = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
			String purchaseDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();

			strFinalChangedComments = request.getParameter("materialEditComment");
			for(int num=1;num<=noofrows;num++)
			{	
				String DeleteColVal = request.getParameter("isDelete"+num);
				boolean isDelete = false;
				if(DeleteColVal.equals("d"))
				{
					isDelete = true;
				}

				String actualProdId = request.getParameter("actualProductId"+num);
				String actualSubProdId = request.getParameter("actualSubProductId"+num);
				String actualChildProdId = request.getParameter("actualChildProductId"+num);
				String actualMeasurementId = request.getParameter("actualUnitsOfMeasurementId"+num);
				String actualRequiredQuantity = request.getParameter("actualRequiredQuantity"+num);
				int IndentCreationDetailsId = Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));


				String actualProductName = request.getParameter("actualProductName"+num);
				String actualSubProductName = request.getParameter("actualSubProductName"+num);
				String actualChildProductName = request.getParameter("actualChildProductName"+num);
				String actualMesurmentName = request.getParameter("actualUnitsOfMeasurementName"+num);
				String actualQuantity =request.getParameter("actualRequiredQuantity"+num);
				String strChangedComments = "";




				String product = request.getParameter("Product"+num);	
				//System.out.println("product: >>>"+product+"<<<");
				String prodsInfo[] = product.split("\\$");
				String changedProdId = prodsInfo[0];
				String changedProdName = prodsInfo[1];
				String subProduct = request.getParameter("SubProduct"+num);
				String subProdsInfo[] = subProduct.split("\\$");
				String changedSubProdId = subProdsInfo[0];
				String changedSubProdName = subProdsInfo[1];
				String childProduct = request.getParameter("ChildProduct"+num);
				String childProdsInfo[] = childProduct.split("\\$");
				String changedChildProdId = childProdsInfo[0];
				String changedChildProdName = childProdsInfo[1];
				String unitsOfMeasurement = request.getParameter("UnitsOfMeasurement"+num);
				String measurementsInfo[] = unitsOfMeasurement.split("\\$");
				String changedMeasurementId = measurementsInfo[0];
				String changedMeasurementName = measurementsInfo[1];
				String changedRequiredQuantity = request.getParameter("RequiredQuantity"+num);
				String groupId=request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
				String changedRemarks = request.getParameter("DBRemarks"+num);
				String strComments = request.getParameter("Comments"+num);

				if(changedRemarks !=null && !changedRemarks.equals("")){
					changedRemarks = changedRemarks + "@@@" + strComments;
				}else{
					changedRemarks = strComments;
				}



				if(!isDelete)
				{
					IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
					indentCreationDetailsDto.setProdId(changedProdId);
					indentCreationDetailsDto.setProdName(changedProdName);
					indentCreationDetailsDto.setSubProdId(changedSubProdId);
					indentCreationDetailsDto.setSubProdName(changedSubProdName);
					indentCreationDetailsDto.setChildProdId(changedChildProdId);
					indentCreationDetailsDto.setChildProdName(changedChildProdName);
					indentCreationDetailsDto.setMeasurementId(changedMeasurementId);
					indentCreationDetailsDto.setMeasurementName(changedMeasurementName);
					indentCreationDetailsDto.setRequiredQuantity(changedRequiredQuantity);
					indentCreationDetailsDto.setRemarks(changedRemarks);
					listProductDetails.add(indentCreationDetailsDto);
				}



				IndentCreationBean actualIndentDetails = new IndentCreationBean();
				actualIndentDetails.setProductId1(actualProdId);
				actualIndentDetails.setSubProductId1(actualSubProdId);
				actualIndentDetails.setChildProductId1(actualChildProdId);
				actualIndentDetails.setUnitsOfMeasurementId1(actualMeasurementId);
				actualIndentDetails.setRequiredQuantity1(actualRequiredQuantity);
				IndentCreationBean changedIndentDetails = new IndentCreationBean();
				if(!isDelete)
				{
					changedIndentDetails.setProductId1(changedProdId);
					changedIndentDetails.setSubProductId1(changedSubProdId);
					changedIndentDetails.setChildProductId1(changedChildProdId);
					changedIndentDetails.setUnitsOfMeasurementId1(changedMeasurementId);
					changedIndentDetails.setRequiredQuantity1(changedRequiredQuantity);
					changedIndentDetails.setGroupId1(groupId);
				}
				boolean isRowChanged = false;

				if(strComments !=null && !strComments.equals("")){

					icd.updateProductsComments(IndentCreationDetailsId,changedRemarks);
				}

				String strDeleteComment = "";

				if(isDelete)
				{
					String strUserName =  session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();

					strDeleteComment = strUserName +" was  child product "+actualChildProductName +" deleted  @@@";
					strFinalChangedComments += strDeleteComment;

					int indentChangedDetailsSeqNum = icd.getIndentChangedDetailsSequenceNumber();
					changedIndentDetails.setRemarks1(strComments);
					icd.insertIndentChangedDetails(actualIndentDetails, changedIndentDetails, indentNumber,indentChangedDetailsSeqNum,IndentCreationDetailsId,"D",user_id);
					icd.deleteRowInIndentCreationDetails(IndentCreationDetailsId);
				}
				else if(!actualProdId.equals(changedProdId))
				{
					isRowChanged = true;
				}
				else if(!actualSubProdId.equals(changedSubProdId))
				{
					isRowChanged = true;
				}
				else if(!actualChildProdId.equals(changedChildProdId))
				{
					isRowChanged = true;
				}
				else if(!actualMeasurementId.equals(changedMeasurementId))
				{
					isRowChanged = true;
				}
				else if(!actualRequiredQuantity.equals(changedRequiredQuantity))
				{
					isRowChanged = true;
				}


				if(isRowChanged)
				{
					int indentChangedDetailsSeqNum = icd.getIndentChangedDetailsSequenceNumber();
					icd.insertIndentChangedDetails(actualIndentDetails, changedIndentDetails, indentNumber,indentChangedDetailsSeqNum,IndentCreationDetailsId,"M",user_id);
					icd.updateIndentCreationDetails(IndentCreationDetailsId,changedIndentDetails);
				}
				// To Give Indent to Central
				if(pendingEmpId.equals("-")&&pendingDeptId.equals(centralDeptId))
				{
					if(!isDelete){
						int centralIndentProcessId = icd.getCentralIndentProcessSequenceNumber();
						icd.insertCentralIndentProcess(centralIndentProcessId,changedIndentDetails,IndentCreationDetailsId,reqSiteId);
					}
				}else if(pendingEmpId.equals("-")&&pendingDeptId.equals(purchaseDeptId))
				{
					if(!isDelete){
						int indentProcessId = icd.getCentralIndentProcessSequenceNumber();
						objPurchaseDepartmentIndentProcessDao.insertPurchaseIndentProcess(indentProcessId,changedIndentDetails,IndentCreationDetailsId,reqSiteId,user_id);
					}
				}







				if(!actualProdId.equals(changedProdId))
				{
					strChangedComments =    " Product "+actualProductName+" name changed to "+changedProdName+" ,";
				}
				if(!actualSubProdId.equals(changedSubProdId))
				{
					strChangedComments += " Sub Product "+actualSubProductName+" name changed to "+changedSubProdName+" ,";
				}
				if(!actualChildProdId.equals(changedChildProdId))
				{
					strChangedComments += " Child Product "+actualChildProductName + " changed to "+changedChildProdName +" ,";
				}
				if(!actualMeasurementId.equals(changedMeasurementId))
				{
					strChangedComments += " Mesurment "+actualMesurmentName + " changed to "+changedMeasurementName+" ,";
				}
				if(!actualRequiredQuantity.equals(changedRequiredQuantity))
				{
					
					
					
					
					strChangedComments +=  changedChildProdName+" Quantity "+actualQuantity + " "+changedMeasurementName+" changed to "+changedRequiredQuantity +" "+changedMeasurementName+" ,";
				}


				if(!strChangedComments.equals("")){
					String strUserName =  session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
					strChangedComments = strChangedComments.substring(0, strChangedComments.length()-1); 
					strFinalChangedComments += strUserName+ " : " + strChangedComments+"@@@";
				}
				/*if((site_id==999 && (centralpendingEmployeeId.equals("") || centralpendingEmployeeId.equals("-")))){
					
					IndentCreationBean changedIndentDetails = new IndentCreationBean();
					changedIndentDetails.setProductId1(prodId);
					changedIndentDetails.setSubProductId1(subProdId);
					changedIndentDetails.setChildProductId1(childProdId);
					changedIndentDetails.setUnitsOfMeasurementId1(measurementId);
					changedIndentDetails.setRequiredQuantity1(requiredQuantity);
					
					int centralIndentProcessId = icd.getCentralIndentProcessSequenceNumber();
					icd.insertCentralIndentProcess(centralIndentProcessId,changedIndentDetails,IndentCreationDetailsId,site_id);
					//icd.updateIndentCreationForCentral(pendingEmployeeId,site_id,indentNumber);//for central indent 
					indentCreationDtoForMail.setPendingDeptId(String.valueOf(site_id));
					indentCreationDtoForMail.setPendingEmpId("-");
					pendingEmpId="-";pendingDeptId=String.valueOf(site_id);
					 checkCentralIndentOrNot="true";
					
				}*/


			}

			int response2 = icd.updateIndentCreation(indentNumber, pendingEmpId, pendingDeptId, indentCreationDto,strFinalChangedComments);


			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Indent Approve Updated");
			response = "Indent Approved Successfully";
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("Indent Approve Failed");
			response = "Failed Indent Approve";
			e.printStackTrace();
			isSendMail = false;
		}

		if(isSendMail){
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{

				final String strPendingEmpId = pendingEmpId;
				final int indentNumber_final = indentNumber;
				final String strIndentFrom_final = strIndentFrom;
				final String strIndentTo_final = strIndentTo;
				final int siteWiseIndentNo_final = siteWiseIndentNo;
				final int finalintPOrtNo =  intPOrtNo;
				final String strUserName = user_Name;
				final String strindentName=indentName;
				executorService.execute(new Runnable() {
					public void run() {

						String  purpose = getIndentLevelComments(indentNumber_final);



						List<IndentCreationBean> list =  getndentChangedDetails(indentNumber_final);




						sendEmailDetails( strPendingEmpId, indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDtoForMail,purpose,list,siteWiseIndentNo_final,finalintPOrtNo,strUserName,strindentName);



					}


				});




				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}
		}

		/*if(isSendMail){

			purpose=getIndentLevelComments(indentNumber);



			List<IndentCreationBean> list =  getndentChangedDetails(indentNumber);

			//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
			sendEmailDetails( pendingEmpId, indentNumber, strIndentFrom, strIndentTo,listProductDetails, indentCreationDtoForMail,purpose,list);
		}*/


		return response;

	}

	public void sendEmailDetails(String pendingEmpId,int indentNumber,String indentFrom,String indentTo,List<IndentCreationDetailsDto> listProductDetails, IndentCreationDto indentCreationDto,String strIndentLevelComments,List<IndentCreationBean> strProductsChangedComments, int siteWiseIndentNo,int finalintPOrtNo,String userName,String indentName){


		try{
			List<Map<String, Object>> indentCreationDtls = null;
			List<String> toMailListArrayList = new ArrayList<String>();

			if(!pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber, "approvalToEmployee");
			}else if(pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber,"approvalToDept");

				toMailListArrayList = icd.getAllEmployeeEmailsUnderDepartment(indentCreationDto.getPendingDeptId());
			}




			String strIndentFromSite = "";
			String strIndentFromDate = "";
			String strEmailAddress =   "";
			String strScheduleDate = "";
			for(Map<String, Object> objIndentCreationDtls : indentCreationDtls) {



				strIndentFromSite = objIndentCreationDtls.get("SITE_NAME")==null ? "" :   objIndentCreationDtls.get("SITE_NAME").toString();
				strIndentFromDate = objIndentCreationDtls.get("CREATE_DATE")==null ? "" :   objIndentCreationDtls.get("CREATE_DATE").toString();
				strEmailAddress = objIndentCreationDtls.get("Email_id")==null ? "" :   objIndentCreationDtls.get("Email_id").toString();
				strScheduleDate = objIndentCreationDtls.get("scheduleDate")==null ? "" :   objIndentCreationDtls.get("scheduleDate").toString();
			}

			if(strEmailAddress.contains(",")){
				for(String strEmailAddress1 : strEmailAddress.split(","))
				{
					toMailListArrayList.add(strEmailAddress1);
				}
			}
			else{
			toMailListArrayList.add(strEmailAddress);
			}
			if(toMailListArrayList.size() > 0){
				String emailto [] = null ;
				emailto = new String[toMailListArrayList.size()];
				toMailListArrayList.toArray(emailto);



				List<IndentCreationBean> list =  getndentChangedDetails(indentNumber);


				EmailFunction objEmailFunction = new EmailFunction();

				objEmailFunction.sendIndentCreationApprovalMailDetails( indentTo, indentNumber, indentFrom,strIndentFromSite, strIndentFromDate, strScheduleDate,emailto,listProductDetails,indentCreationDto,strIndentLevelComments,list,siteWiseIndentNo,finalintPOrtNo,userName,indentName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}









/*	public void sendEmail(String pendingEmpId,int indentNumber,String indentFrom,String indentTo){


		try{
			List<Map<String, Object>> indentCreationDtls = null;
			List<String> toMailListArrayList = new ArrayList<String>();
			String emailto [] = null ;
			if(!pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber, "approvalToEmployee");
			}else if(pendingEmpId.equals("-"))
			{
				indentCreationDtls = icd.getIndentCreationDetails(indentNumber,"approvalToDept");

				toMailListArrayList = icd.getAllEmployeeEmailsUnderDepartment(999);
			}




			String strIndentFromSite = "";
			String strIndentFromDate = "";
			String strEmailAddress =   "";
			for(Map<String, Object> objIndentCreationDtls : indentCreationDtls) {



				strIndentFromSite = objIndentCreationDtls.get("SITE_NAME")==null ? "" :   objIndentCreationDtls.get("SITE_NAME").toString();
				strIndentFromDate = objIndentCreationDtls.get("CREATE_DATE")==null ? "" :   objIndentCreationDtls.get("CREATE_DATE").toString();
				strEmailAddress = objIndentCreationDtls.get("Email_id")==null ? "" :   objIndentCreationDtls.get("Email_id").toString();

			}


			toMailListArrayList.add(strEmailAddress);

			emailto = new String[toMailListArrayList.size()];
			toMailListArrayList.toArray(emailto);

			EmailFunction objEmailFunction = new EmailFunction();



			objEmailFunction.sendIndentCreationApprovalMail( indentTo, indentNumber, indentFrom, 	strIndentFromSite, strIndentFromDate, emailto );

		}catch(Exception e){
			e.printStackTrace();
		}
	}*/



	@Override
	public String rejectIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String user_name,String site_Name) {
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_rejInd, ");
		List<String> List = new ArrayList<String> ();
		String strSiteId="";
		String indentName="";
		try
		{
			int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			String siteWiseIndentNo=(request.getParameter("siteWiseIndentNo"));
			strSiteId=request.getParameter("siteId")== null ? "-" : request.getParameter("siteId").toString();
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			String Remarks=request.getParameter("indentRemarks")== null ? "" : request.getParameter("indentRemarks").toString();
			indentName=request.getParameter("indentName");
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			indentCreationDto.setTempPass(CommonUtilities.getStan());
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			indentCreationDto.setSequenccce_Number(indentCreationApprovalSeqNum);
			indentCreationDto.setUserId(user_id);
			indentCreationDto.setSiteId(site_id);
			indentCreationDto.setPurpose(Remarks);
			int response2 = icd.rejectIndentCreation(indentNumber,indentCreationDto);
			/*************************************************Indent reject For Mail start******************************************************/
			if(response2>0){
				//List=icd.getPendingEmployeeIdForIndent(user_id,site_id);
				EmailFunction objEmailFunction = new EmailFunction();
				final String user_name_final=user_name;
				final int getLocalPort = request.getLocalPort();
				final String strIndentNumber=String.valueOf(indentNumber);
				final String strRemarks=Remarks;
				
				String [] emailToAddress =icd.getEmailsOfEmployeesInAllLowerDeptOfThisEmployee(String.valueOf(indentNumber));
				String rejectEmpDesignation=icd.getEmpdesignation(user_id);
				objEmailFunction.sendMailForRejectIndent(siteWiseIndentNo,emailToAddress,getLocalPort,user_name_final,strRemarks,rejectEmpDesignation,site_Name,indentName);
				
				
				
			}
			
			
			/*************************************************Indent reject For Mail End******************************************************/
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Indent Rejected");
			response = "Success";
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("Indnet Rejection Failed");
			response = "Failed";
			e.printStackTrace();
		}
		return response;

	}

	@Override
	public String rejectIndentCreationFromMail(Model model, HttpServletRequest request,int site_id, String user_id) {
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_rejInM, ");
		try
		{
			int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			if(request.getParameter("tempPass").equals(icd.getTempPasswordOfIndent(indentNumber))){}else{return "WrongPassword";}

			IndentCreationDto indentCreationDto = new IndentCreationDto();
			indentCreationDto.setTempPass(CommonUtilities.getStan());
			int response2 = icd.rejectIndentCreation(indentNumber,indentCreationDto);
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Indent Rejected via EMail");
			response = "Success";
		}
		catch(Exception e)
		{
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Indent Rejection Failed via Email");
			response = "Failed";
			e.printStackTrace();
		}
		return response;
	}









	@Override
	public List<ProductDetails> getAllSubProducts(String prodId) {
		return icd.getAllSubProducts(prodId);
	}

	@Override
	public List<ProductDetails> getAllChildProducts(String strSubProdId) {
		return icd.getAllChildProducts(strSubProdId);
	}



	@Override
	public String RejectQuantity(Model model, HttpServletRequest request, int site_id, String user_id){

		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_rejQua, ");
		int response=0;
		int issuedQuantity=0;
		int noofrows = 0;
		int sno = 0;
		String reponse="";
		try{
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+request.getParameter("indentNumber"));
			
			noofrows =Integer.parseInt(request.getParameter("numberOfRows"));
			for(int num=1;num<=noofrows;num++){

				String Quantity= request.getParameter("issuedquantity"+num) == null ? "0" :  request.getParameter("issuedquantity"+num).toString();

				if(Quantity.equals("")){
					Quantity= "0";
				}

				issuedQuantity=Integer.parseInt(Quantity);

				if(issuedQuantity !=0 )

				{
					sno++;

					String product = request.getParameter("product"+num);
					String subProduct = request.getParameter("subProduct"+num);
					String childProduct = request.getParameter("childProduct"+num);
					String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
					String requiredQuantity = request.getParameter("requiredQuantity"+num);
					IndentCreationBean indentCreationBean = new IndentCreationBean();
					indentCreationBean.setStrSerialNumber(String.valueOf(sno));
					indentCreationBean.setSubProduct1(subProduct);
					indentCreationBean.setChildProduct1(childProduct);
					indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
					indentCreationBean.setRequiredQuantity1(requiredQuantity);
					list.add(indentCreationBean);


					request.setAttribute("IndentDetails",list);

					request.setAttribute("nums",num);

					response=icd.getRejectedDetails(model,request,site_id,user_id);



				}
			}
			
			if(response==1){

				reponse="success";
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
			}else	{
				reponse="failure";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
			
		}


		catch(Exception e){

			reponse="failure";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();

		}

		



		return reponse;

	}

	public List<IndentCreationBean> getViewMyReceiveIndents(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		//	session.setAttribute("SiteId", ud.getStrSiteId());

		String siteId =session.getAttribute("SiteId").toString();
		return icd.getViewReceiveIndentDetails(siteId);
	}

	@Override
	public List<IndentCreationBean> getViewAllMyRequestedIndent(HttpServletRequest request,int indentNumber) {
		HttpSession session = request.getSession(true);
		String DeptId =session.getAttribute("SiteId").toString();
		return icd.getViewReceiveDetails(indentNumber);
	}

	@Override
	public List<IndentCreationBean> getViewReceivedIndentDetailsLists(int indentNumber) {
		return icd.getViewReceivedIndentDetailsLists(indentNumber);
	}

	@Override
	public String sendReceivedQuantity(Model model, HttpServletRequest request, int site_id, String user_id){
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_senReQ, ");
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int intIndentNo  = 0;
		int noofrows = 0;
		int sno = 0;
		int issuedQuantity=0;
		String reponse="";
		try{
			intIndentNo  = Integer.parseInt(request.getParameter("indentNumber"));
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+intIndentNo);
			
			noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
			for(int num=1;num<=noofrows;num++){
				String Quantity= request.getParameter("issuedquantity"+num) == null ? "0" :  request.getParameter("issuedquantity"+num).toString();

				if(Quantity.equals("")){
					Quantity= "0";
				}

				issuedQuantity=Integer.parseInt(Quantity);

				if(issuedQuantity !=0 )

				{
					sno++;
					//	int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
					//	String issuedQuantity=String.valueOf(request.getParameter("issuedquantity"));
					//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"));

					String product = request.getParameter("product"+num);
					String subProduct = request.getParameter("subProduct"+num);
					String childProduct = request.getParameter("childProduct"+num);
					String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
					String requiredQuantity = request.getParameter("requiredQuantity"+num);
					String requestQuantity = request.getParameter("strRequestQuantity"+num);
					String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
					String centralIndentReqDtlsId = request.getParameter("centralIndentReqDtlsId"+num);
					String issuedquantity = request.getParameter("issuedquantity"+num);
					String indentProcessId = request.getParameter("indentProcessId"+num);
					//sString indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);





					//int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
					//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

					int intIndentCreationDetailsId = Integer.parseInt(indentCreationDetailsId == null ? "0" : indentCreationDetailsId.toString()); 
					int intCentralIndentReqDtlsId = Integer.parseInt(centralIndentReqDtlsId == null ? "0" : centralIndentReqDtlsId.toString()); 
					int intIndentProcessId = Integer.parseInt(indentProcessId == null ? "0" : indentProcessId.toString()); 

					IndentCreationBean indentCreationBean = new IndentCreationBean();
					indentCreationBean.setStrSerialNumber(String.valueOf(sno));
					indentCreationBean.setSubProduct1(subProduct);
					indentCreationBean.setChildProduct1(childProduct);
					indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
					indentCreationBean.setRequiredQuantity1(requiredQuantity);
					indentCreationBean.setStrRequestQuantity(requestQuantity);
					indentCreationBean.setIndentCreationDetailsId(intIndentCreationDetailsId);
					indentCreationBean.setCentralIndentReqDtlsId(intCentralIndentReqDtlsId);
					indentCreationBean.setIssuedquantity(issuedquantity);
					indentCreationBean.setIndentProcessId(intIndentProcessId);
					list.add(indentCreationBean);


					request.setAttribute("IndentDetails",list);
					request.setAttribute("nums",num);
					int response=icd.getReceivedDetails(indentCreationBean,site_id,user_id);

					if(response == 1){

						response = icd.updateReceivedDetails(issuedQuantity,intCentralIndentReqDtlsId);
					}
					if(response == 1){

						response = icd.updateIndentCreationDetials(issuedQuantity,intIndentCreationDetailsId);
					}


					reponse="success";

				}
			}

			icd.getAndUpdateCentralIndentRequestDetails(intIndentNo);

			icd.getAndUpdateCentralIndentProcess(intIndentNo);

			icd.getAndUpdateIndentNo(intIndentNo);



			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		}

		catch(Exception e){
			reponse="failure";
			e.printStackTrace();
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");


		}
		return reponse;

	}



	@Override
	public String acceptIndentReceive( HttpServletRequest request, int site_id, String user_id,String num){
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int intIndentNo  = 0;
		//int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		//int issuedQuantity=0;
		String reponse="";
		try{
			intIndentNo  = Integer.parseInt(request.getParameter("indentNumber"));
			/*	for(int num=1;num<=noofrows;num++){
				String Quantity= request.getParameter("issuedquantity"+num) == null ? "0" :  request.getParameter("issuedquantity"+num).toString();

				if(Quantity.equals("")){
					Quantity= "0";
				}

				issuedQuantity=Integer.parseInt(Quantity);

				if(issuedQuantity !=0 )

				{
					sno++;
			 */		//	int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			//	String issuedQuantity=String.valueOf(request.getParameter("issuedquantity"));
			//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"));

			String product = request.getParameter("productId"+num);
			String subProduct = request.getParameter("subProductId"+num);
			String childProduct = request.getParameter("childProductId"+num);
			String unitsOfMeasurement = request.getParameter("measurmentId"+num);
			String issuedquantity = request.getParameter("quantity"+num);
			String indentCreationDetailsId = "";  //request.getParameter("indentCreationDetailsId"+num);
			String centralIndentReqDtlsId = "";  //request.getParameter("centralIndentReqDtlsId"+num);
			String indentProcessId = "";  //request.getParameter("indentProcessId"+num);

			List<Map<String, Object>> dbIndentProcessDts =  icd.getCentralIndentProcessDetails(  childProduct , unitsOfMeasurement, String.valueOf(intIndentNo));


			for(Map<String, Object> prods : dbIndentProcessDts) {


				indentCreationDetailsId = prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString();
				centralIndentReqDtlsId = prods.get("CNTL_INDENT_REQ_DTLS_SEQ")==null ? "" :   prods.get("CNTL_INDENT_REQ_DTLS_SEQ").toString();
				indentProcessId = prods.get("INDENT_PROCESS_ID")==null ? "" :   prods.get("INDENT_PROCESS_ID").toString();


			}
			String requiredQuantity = request.getParameter("requiredQuantity"+num);
			String requestQuantity = request.getParameter("strRequestQuantity"+num);
			/*String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
			String centralIndentReqDtlsId = request.getParameter("centralIndentReqDtlsId"+num);
			String indentProcessId = request.getParameter("indentProcessId"+num);
			 *///sString indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);






			//int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
			//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

			int intIndentCreationDetailsId = Integer.parseInt(indentCreationDetailsId.equals("") ? "0" : indentCreationDetailsId.toString()); 
			int intCentralIndentReqDtlsId = Integer.parseInt(centralIndentReqDtlsId.equals("") ? "0" : centralIndentReqDtlsId.toString()); 
			int intIndentProcessId = Integer.parseInt(indentProcessId.equals("") ? "0" : indentProcessId.toString()); 

			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			indentCreationBean.setSubProduct1(subProduct);
			indentCreationBean.setChildProduct1(childProduct);
			indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
			indentCreationBean.setRequiredQuantity1(requiredQuantity);
			indentCreationBean.setStrRequestQuantity(requestQuantity);
			indentCreationBean.setIndentCreationDetailsId(intIndentCreationDetailsId);
			indentCreationBean.setCentralIndentReqDtlsId(intCentralIndentReqDtlsId);
			indentCreationBean.setIssuedquantity(issuedquantity);
			indentCreationBean.setIndentProcessId(intIndentProcessId);
			list.add(indentCreationBean);


			request.setAttribute("IndentDetails",list);
			request.setAttribute("nums",num);
			int response=icd.getReceivedDetails(indentCreationBean,site_id,user_id);

			double intIssuedquantity = Double.valueOf(issuedquantity);

			if(response == 1){

				response = icd.updateReceivedDetails(intIssuedquantity,intCentralIndentReqDtlsId);
			}
			if(response == 1){

				response = icd.updateIndentCreationDetials(intIssuedquantity,intIndentCreationDetailsId);
			}


			reponse="success";

			/*	}
			}

			icd.getAndUpdateCentralIndentRequestDetails(intIndentNo);

			icd.getAndUpdateCentralIndentProcess(intIndentNo);

			icd.getAndUpdateIndentNo(intIndentNo);



			transactionManager.commit(status);*/
		}

		catch(Exception e){
			reponse="failure";
			e.printStackTrace();
			//if(status != null)
			//	transactionManager.rollback(status);

		}
		return reponse;

	}


	@Override
	public String checkisIndentClose(HttpServletRequest request, int site_id, String user_id){
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//TransactionStatus status = transactionManager.getTransaction(def);
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int intIndentNo  = 0;
		//int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		int sno = 0;
		int issuedQuantity=0;
		String reponse="";
		try{
			intIndentNo  = Integer.parseInt(request.getParameter("indentNumber"));
			/*	for(int num=1;num<=noofrows;num++){
				String Quantity= request.getParameter("issuedquantity"+num) == null ? "0" :  request.getParameter("issuedquantity"+num).toString();

				if(Quantity.equals("")){
					Quantity= "0";
				}

				issuedQuantity=Integer.parseInt(Quantity);

				if(issuedQuantity !=0 )

				{
					sno++;
			 	//	int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			//	String issuedQuantity=String.valueOf(request.getParameter("issuedquantity"));
			//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"));

			String product = request.getParameter("product"+num);
			String subProduct = request.getParameter("subProduct"+num);
			String childProduct = request.getParameter("childProduct"+num);
			String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
			String requiredQuantity = request.getParameter("requiredQuantity"+num);
			String requestQuantity = request.getParameter("strRequestQuantity"+num);
			String indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);
			String centralIndentReqDtlsId = request.getParameter("centralIndentReqDtlsId"+num);
			String issuedquantity = request.getParameter("issuedquantity"+num);
			String indentProcessId = request.getParameter("indentProcessId"+num);
			//sString indentCreationDetailsId = request.getParameter("indentCreationDetailsId"+num);





			//int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
			//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

			int intIndentCreationDetailsId = Integer.parseInt(indentCreationDetailsId == null ? "0" : indentCreationDetailsId.toString()); 
			int intCentralIndentReqDtlsId = Integer.parseInt(centralIndentReqDtlsId == null ? "0" : centralIndentReqDtlsId.toString()); 
			int intIndentProcessId = Integer.parseInt(indentProcessId == null ? "0" : indentProcessId.toString()); 

			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			indentCreationBean.setSubProduct1(subProduct);
			indentCreationBean.setChildProduct1(childProduct);
			indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
			indentCreationBean.setRequiredQuantity1(requiredQuantity);
			indentCreationBean.setStrRequestQuantity(requestQuantity);
			indentCreationBean.setIndentCreationDetailsId(intIndentCreationDetailsId);
			indentCreationBean.setCentralIndentReqDtlsId(intCentralIndentReqDtlsId);
			indentCreationBean.setIssuedquantity(issuedquantity);
			indentCreationBean.setIndentProcessId(intIndentProcessId);
			list.add(indentCreationBean);


			request.setAttribute("IndentDetails",list);
					request.setAttribute("nums",num);
					int response=icd.getReceivedDetails(indentCreationBean,site_id,user_id);

					if(response == 1){

						response = icd.updateReceivedDetails(issuedQuantity,intCentralIndentReqDtlsId);
					}
					if(response == 1){

						response = icd.updateIndentCreationDetials(issuedQuantity,intIndentCreationDetailsId);
					}


					reponse="success";*

			}
			}*/


			icd.getAndUpdateCentralIndentRequestDetails(intIndentNo);

			icd.getAndUpdateCentralIndentProcess(intIndentNo);

			icd.getAndUpdateIndentNo(intIndentNo);



			//transactionManager.commit(status);
		}

		catch(Exception e){
			reponse="failure";
			e.printStackTrace();
			//	if(status != null)
			//		transactionManager.rollback(status);

		}
		return reponse;

	}

	@Override
	public String RejectIssueQuantity(Model model, HttpServletRequest request, int site_id, String user_id){

		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_rejIsQ, ");
		int response=0;
		//int issuedQuantity=0;
		//int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
		//int sno = 0;
		String reponse="";
		try {
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+request.getParameter("indentNumber"));
			
			response=icd.getRejectedQuantityDetails(model,request,site_id,user_id);
			if(response==1){

				reponse="success";
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
			}else	{
				reponse="failure";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
		}
		/*	try{
			for(int num=1;num<=noofrows;num++){

				String Quantity= request.getParameter("issuedquantity"+num) == null ? "0" :  request.getParameter("issuedquantity"+num).toString();

				if(Quantity.equals("")){
					Quantity= "0";
				}

				issuedQuantity=Integer.parseInt(Quantity);

				if(issuedQuantity !=0 )

				{
					sno++;

					String product = request.getParameter("product"+num);
					String subProduct = request.getParameter("subProduct"+num);
					String childProduct = request.getParameter("childProduct"+num);
					String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
					String requiredQuantity = request.getParameter("requiredQuantity"+num);
					IndentCreationBean indentCreationBean = new IndentCreationBean();
					indentCreationBean.setStrSerialNumber(String.valueOf(sno));
					indentCreationBean.setSubProduct1(subProduct);
					indentCreationBean.setChildProduct1(childProduct);
					indentCreationBean.setUnitsOfMeasurement1(unitsOfMeasurement);
					indentCreationBean.setRequiredQuantity1(requiredQuantity);
					list.add(indentCreationBean);


					request.setAttribute("IndentDetails",list);

					request.setAttribute("nums",num);



				}
			}
		}


		catch(Exception e){

			reponse="failure";
			transactionManager.rollback(status);
			e.printStackTrace();

		}*/

		


		return reponse;

	}






	public List<IndentCreationBean> getViewMyRequestIndents(HttpServletRequest request, String loadType) {
		HttpSession session = request.getSession(true);
		//	session.setAttribute("SiteId", ud.getStrSiteId());

		String DeptId =session.getAttribute("SiteId").toString();
		String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
		String siteWiseIndentNo=request.getParameter("siteWiseIndentNo")==null?"":request.getParameter("siteWiseIndentNo");
		
		if(loadType.equals("AllIndents")){
			siteId="";
			siteWiseIndentNo="";
		}
		
		return icd.getViewMyRequestIndents(DeptId,siteId,siteWiseIndentNo);
	}
	@Override
	public List<IndentCreationBean> getViewissedIndentDetailsLists(int indentNumber,String siteId) {
		return icd.getViewissuedIndentDetailsLists(indentNumber,siteId);
	}
	@Override
	public List<IndentCreationBean> getViewAllMyRequestIndents(HttpServletRequest request,int indentNumber) {
		HttpSession session = request.getSession(true);
		String DeptId =session.getAttribute("SiteId").toString();
		return icd.getViewAllMyRequestIndents(DeptId,indentNumber);
	}

	@Override
	public String sendissued(Model model, HttpServletRequest request,HttpSession session, int site_id, String user_id, String site_Name){

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_senIss, ");
		int indentNumber= 0;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int noofrows = 0;
		int sno = 0;
		String reponse="";
		String siteWiseIndentNo="";
		String senderSiteName="";
		String raisedIndetSiteName="";
		String reqSiteId="";
		String strUserName="";
		String currentUserMobileNo="";
		String currentUserEmail="";
		
		
		double issuedQuantity=0.0;
		double intPendingQuantity = 0.0;
		String strStatus = "A";
		boolean isSendMail = true;
		try{
			indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			siteWiseIndentNo=request.getParameter("siteWiseIndentNo")==null?"0":request.getParameter("siteWiseIndentNo");

			raisedIndetSiteName=request.getParameter("siteName")==null?"":request.getParameter("siteName");
			reqSiteId=request.getParameter("Site")==null?"":request.getParameter("Site").split("\\$")[0];
			senderSiteName=session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			currentUserEmail =session.getAttribute("EMP_EMAIL") == null ? "" :  session.getAttribute("EMP_EMAIL").toString();
			currentUserMobileNo=session.getAttribute("MOBILE_NUMBER") == null ? "" :  session.getAttribute("MOBILE_NUMBER").toString();

			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			String numbeOfRowsToBeProcessed = request.getParameter("numbeOfRowsToBeProcessed");		//e.g.  numbeOfRowsToBeProcessed = 1|2|3|
			noofrows = numbeOfRowsToBeProcessed.split("\\|").length;
			// used for indent entry details module (1st module)
			request.setAttribute("RequestFrom","IndentCreationApprovalModule"); 


			String strResponse= objIssueToOtherSiteServiceImpl.doIndentIssueToOtherSite( model,  request,  session);
 			request.setAttribute("viewGrnPageData", request.getAttribute("viewGrnPageData"));

			// used for indent entry details module (1st module)
			String isErrorOccured = request.getAttribute("exceptionMsg") == null ? "" : request.getAttribute("exceptionMsg").toString();
			if(!isErrorOccured.equals("") || strResponse.equals("No Stock") || strResponse.equals("Failed")){
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return "failure";
			}

			for(int num=1;num<=noofrows;num++){

				//issuing Quantity
				String Quantity= request.getParameter("Quantity"+num) == null ? "0" :  request.getParameter("Quantity"+num).toString();
				if(Quantity.equals("")){Quantity= "0";}
				issuedQuantity=Double.parseDouble(Quantity);

				if(issuedQuantity !=0 )
				{
					sno++;
					//	int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
					//	String issuedQuantity=String.valueOf(request.getParameter("issuedquantity"));
					//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"));

					String product = request.getParameter("product"+num);
					String subProduct = request.getParameter("subProduct"+num);
					String childProduct = request.getParameter("childProduct"+num);
					String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
					String allProdNames=request.getParameter("wholeRowProdIds"+num);
					
					String[] bulkarrayProdName = allProdNames.split("@@");
					
					String requiredQuantity = request.getParameter("requiredQuantity"+num);
					String pendingQuantityId = request.getParameter("pendingQuantity"+num);
					int indentProcessId = Integer.parseInt(request.getParameter("indentProcessId"+num));
					int centralIndentReqDtlsId = Integer.parseInt(request.getParameter("centralIndentReqDtlsId"+num));

					if(pendingQuantityId.equals("")){
						requiredQuantity= "0";
					}

					intPendingQuantity = Double.parseDouble(pendingQuantityId);
					if(issuedQuantity >= intPendingQuantity){
						strStatus = "I";
					}
					IndentCreationBean indentCreationBean = new IndentCreationBean();
					indentCreationBean.setStrSerialNumber(String.valueOf(sno));
					indentCreationBean.setProductId1(product);
					indentCreationBean.setSubProductId1(subProduct);
					indentCreationBean.setChildProductId1(childProduct);
					indentCreationBean.setUnitsOfMeasurementId1(unitsOfMeasurement);
					
					indentCreationBean.setProduct1(bulkarrayProdName[0]);
					indentCreationBean.setSubProduct1(bulkarrayProdName[1]);
					indentCreationBean.setChildProduct1(bulkarrayProdName[2]);
					indentCreationBean.setUnitsOfMeasurement1(bulkarrayProdName[3]);
					indentCreationBean.setSenderSiteName(senderSiteName);
					indentCreationBean.setSiteName(raisedIndetSiteName);
					indentCreationBean.setSiteWiseIndentNumber(siteWiseIndentNo);
					
					indentCreationBean.setRequiredQuantity1(requiredQuantity);
					indentCreationBean.setStatus(strStatus);
					indentCreationBean.setAllocatedQuantity(Quantity);
					indentCreationBean.setIndentProcessId(indentProcessId);
					indentCreationBean.setCentralIndentReqDtlsId(centralIndentReqDtlsId);
					indentCreationBean.setAllocatedQuantity(String.valueOf(issuedQuantity));
					
					indentCreationBean.setIndentSettledEmployeeName(strUserName);
					indentCreationBean.setCurrentUserEemail(currentUserEmail);
					indentCreationBean.setCurrentUserMobileNumber(currentUserMobileNo);

					list.add(indentCreationBean);
					request.setAttribute("IndentDetails",list);

					int response = icd.updateCentralProcessReqDtlsTable(site_id,user_id,indentCreationBean,indentNumber);
					reponse="success";
				}
			}

			String s=null;
			//s.trim();
			
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		}catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			reponse="failure";
			e.printStackTrace();
			isSendMail = false;
		}
		
		if(isSendMail){
			//holding the HTML page content or data
			StringBuffer indentSettlementDetailsForMail=new StringBuffer();
			//holding the mail subject
			StringBuffer subjectForMailIndentSettlement=new StringBuffer();
			IndentCreationBean indentCreationBean12=new IndentCreationBean();
			int serialNo=1;
			//holding mobile number for sending the message to users
			List<String> mobilesList=new ArrayList<String>();
			//holding message content 
			String smsMessageContent="";
			
			for (IndentCreationBean indentCreationBean : list) {
				indentCreationBean12=indentCreationBean;
				if(serialNo==1){
					subjectForMailIndentSettlement.append("Request for Material Adjustment from Site "+indentCreationBean.getSenderSiteName()+" to Site "+indentCreationBean.getSiteName()+".");
					smsMessageContent="Material was adjusted for Indent No "+siteWiseIndentNo+" of Site "+indentCreationBean.getSiteName()+" from "+indentCreationBean.getSenderSiteName()+". For more details follow http://129.154.74.18/Sumadhura/";
				}
				indentSettlementDetailsForMail.append(
						"<tr>"
						+ "<td>"+serialNo+"</td> "
						+ "<td>"+siteWiseIndentNo+"</td> "
						+ "<td>"+indentCreationBean.getProduct1()+"</td> "
						+ "<td>"+indentCreationBean.getSubProduct1()+"</td> "
						+ "<td>"+indentCreationBean.getChildProduct1()+"</td>  "
						+ "<td>"+indentCreationBean.getUnitsOfMeasurement1()+"</td>  "
						+ "<td>"+indentCreationBean.getRequiredQuantity1()+"</td>  "
						+ "<td>"+indentCreationBean.getAllocatedQuantity()+"</td>  "
						+ "</tr>");
				
				serialNo++;
			}
			//holding CC mail's email's
			List<String > ccMailsOfIndentEmployee=new ArrayList<String>();
			
			//holding the sender site email and phone number, using this we have to trigger mail to them
			List<String> senderSiteMail=new ArrayList<String>();
			//loading sender site email and phone number
			String emailUsername=UIProperties.validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername2=UIProperties.validateParams.getProperty(site_id+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(site_id+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername3=UIProperties.validateParams.getProperty("999_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
	
			String ccEmailUsername1=UIProperties.validateParams.getProperty(reqSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String ccEmailUsername2=UIProperties.validateParams.getProperty(site_id+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(site_id+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String ccEmailUsername3=UIProperties.validateParams.getProperty("999_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			
			String mobileNumber1=UIProperties.validateParams.getProperty(reqSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			String mobileNumber2=UIProperties.validateParams.getProperty(site_id+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(site_id+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			String mobileNumber3=UIProperties.validateParams.getProperty("999_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();

			
			 
			if(emailUsername.length()!=0)
				senderSiteMail.addAll(Arrays.asList(emailUsername.split(",")));
			if(emailUsername2.length()!=0)
				senderSiteMail.addAll(Arrays.asList(emailUsername2.split(",")));
			if(emailUsername3.length()!=0)
				senderSiteMail.addAll(Arrays.asList(emailUsername3.split(",")));
			
			
			if(ccEmailUsername1.length()!=0)
				ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername1.split(",")));
			if(ccEmailUsername2.length()!=0)
				ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername2.split(",")));
			if(ccEmailUsername3.length()!=0)
				ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername3.split(",")));
			
			 
			if(mobileNumber1.length()!=0)
				mobilesList.addAll(Arrays.asList(mobileNumber1.split(",")));
			if(mobileNumber2.length()!=0)
				mobilesList.addAll(Arrays.asList(mobileNumber2.split(",")));
			if(mobileNumber3.length()!=0)
				mobilesList.addAll(Arrays.asList(mobileNumber3.split(",")));
			
			//loading the indent created and approved employee names and email's and phone number
			/*List<Map<String, Object>> approveEmail=icd.getIndentCreatedEmpName(reqSiteId,indentNumber);
			for (Map<String, Object> map : approveEmail) {
				String mobileNumber=map.get("MOBILE_NUMBER")==null?"":map.get("MOBILE_NUMBER").toString();
				String email=map.get("EMP_EMAIL")==null?"":map.get("EMP_EMAIL").toString();
				if(mobileNumber.length()!=0)
					mobilesList.add(Long.valueOf(mobileNumber));
					ccMailsOfIndentEmployee.put(email,mobileNumber);
			}*/
			
			try {
				//sending SMS to user's, who are involved in indent settlement
				if(mobilesList.size()!=0){
					mobileSMS.sendSMS(mobilesList, smsMessageContent);
				}
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			EmailFunction objEmailFunction = new EmailFunction();
			//sending mail to user's , who are involved in indent settlement
			objEmailFunction.sendMailForIndentSettlement(indentCreationBean12,Integer.valueOf(reqSiteId),ccMailsOfIndentEmployee,senderSiteMail,indentSettlementDetailsForMail,subjectForMailIndentSettlement,"ViewMyReqPage");
		}
		return reponse;

	}



	@Override
	public void getIndentFromDetails(int indentNumber, List<IndentCreationBean> list) {
		icd.getIndentFromDetails(indentNumber,list.get(0));

	}
	@Override
	public void getIndentToDetails(String strSite_id,String user_id, List<IndentCreationBean> list) {
		icd.getIndentToDetails(strSite_id,user_id,list.get(0));
		if(list.get(0).getToEmpId()==null)
		{
			icd.getIndentToDetails2(strSite_id,user_id,list.get(0));
		}

	}
	@Override
	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb,int SiteId) {
		return icd.getIndentFromAndToDetails(user_id, icb,SiteId);
	}
	@Override
	public int getIndentCreationSequenceNumber() {
		int indentCreationSeqNum = icd.getIndentCreationSequenceNumber();
		return indentCreationSeqNum;
	}
	@Override
	public List<IndentCreationBean> getAllCentralIndents() {
		String centralDeptId = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
		return icd.getIndentFromAndToDetails(centralDeptId);
	}
	@Override
	public List<IndentCreationDto> getPendingIndents(String user_id,String strSiteId) {
		List<IndentCreationDto> list = objPurchaseDepartmentIndentProcessDao.getPendingIndents(user_id,strSiteId);
		return list;
	}
	@Override
	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = objPurchaseDepartmentIndentProcessDao.getIndentCreationDetailsLists(indentNumber);
		return list;
	}
	@Override
	public boolean checkIndentNumberIsValidForEmployee(int indentNumber, String user_id) {
		return icd.checkIndentNumberIsValidForEmployee(indentNumber, user_id);
	}
	@Override
	public List<IndentCreationBean> getIndentCreationLists(int indentNumber) {
		List<IndentCreationBean> list = icd.getIndentCreationLists(indentNumber);
		return list;
	}

	@Override
	public List<IndentCreationBean> getAllSiteCentralIndents(String site) {

		return icd.getIndentFromAndToDetails(site);
	}

	public List<ViewIndentIssueDetailsBean> getRaisedIndentDetails(String fromDate, String toDate, String siteId,String indentNumber){
		List<ViewIndentIssueDetailsBean> list = null;
		try{
			list = icd.getRaisedIndentDetails( fromDate,  toDate,  siteId,indentNumber);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public String getPoDetailsList(String poNumber, String siteId,HttpServletRequest request) {
		Map<String, String> viewGrnPageDataMap = null;
		viewGrnPageDataMap =new HashMap<String, String>();
		Map<String,String> objviewPOPageDataMap = new HashMap<String,String>();
		String result="Success";
		String tblOneData="";
		String tbltwoData="";
		String tblCharges="";
		String gstinumber="";
		String terms="";
		String poentryId="";
		String receiverState="";
		//	List<GetInvoiceDetailsBean> GrnIssueToOtherSiteList = null;


		try {
			
			String siteName=request.getParameter("siteName");
			String vendorId=request.getParameter("vendorId1");
			tblOneData+= icd.getVendorDetails(poNumber, siteId,request,siteName,vendorId);
			poentryId=request.getAttribute("poentryId").toString();
			receiverState=request.getAttribute("receiverState").toString();
			tbltwoData+= icd.getProductDetails(poentryId,siteId,request,receiverState);
			gstinumber=request.getAttribute("gstinumber").toString();
			//	terms=request.getAttribute("terms").toString();
			tblCharges+=icd.getTransportChargesListForGRN(poentryId,siteId,gstinumber,request,poNumber,receiverState);

			request.setAttribute("siteNameForPriceMaster", siteName);
			objviewPOPageDataMap.put("AddressDetails", tblOneData);
			objviewPOPageDataMap.put("productDetails", tbltwoData);
			objviewPOPageDataMap.put("TransportDetails", tblCharges);
			request.setAttribute("viewPoPageData", objviewPOPageDataMap);


			//	viewGrnPageDataMap.put("tblOneData", tblOneData.toString());

			//	String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);
			//	viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);



			//table three data

			//	String fnlStr = tbltwoData.toString().substring(0, tbltwoData.toString().length() - 2);
			//	viewGrnPageDataMap.put("tblTwoData", fnlStr);
			//	request.setAttribute("viewGrnPageData", viewGrnPageDataMap);

			//transactionManager.commit(status);		
			//	 viewGrnPageDataMap.put("tblOneData", tblOneData.toString());

		} 

		catch (Exception e) {

			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");

			result = "Failed";
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();

			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");

			e.printStackTrace();
		}

		return result;
	}

	public List<IndentCreationBean> getPoDetails(String fromDate, String toDate, String siteId,String poNumber,String sessionId,boolean AllPoOrNot,String siteids,String vendorName){
		List<IndentCreationBean> list = null;
		try{
			list = icd.getPoDetails( fromDate,  toDate,  siteId,poNumber,sessionId,AllPoOrNot,siteids,vendorName);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public List<IndentCreationBean> getClosedIndents(String fromDate, String toDate, String deptId,String indentNumber,String siteId,String sites){
		List<IndentCreationBean> list = null;
		try{
			list = icd.getClosedIndents( fromDate,  toDate,  deptId,indentNumber,siteId,sites);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public String  getIndentLevelComments(int indentNo) {

		List<IndentCreationBean> list = null;
		String strPurpose = "";
		try{
			strPurpose = icd.getIndentLevelComments( indentNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return strPurpose;
	}




	public List<IndentCreationBean>   getndentChangedDetails(int indentNo) {

		List<IndentCreationBean> list = null;
		String strPurpose = "";
		try{
			list = icd.getndentChangedDetails( indentNo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<IndentCreationBean> getDeletedProductDetailsLists(int indentNumber) {
		return objPurchaseDepartmentIndentProcessDao.getDeletedProductDetailsLists(indentNumber);
	}

	@Override
	public int getMaxOfSiteWiseIndentNumber(int siteId) {
		return icd.getMaxOfSiteWiseIndentNumber(siteId);
	}

	@Override
	public int getIndentNumber(int siteWiseIndentNumber, int site_Id) {
		return icd.getIndentNumber(siteWiseIndentNumber, site_Id);
	}


	@Override
	public List<IndentCreationBean> viewIndentProductDetails(String fromDate,String toDate,String siteId) {
		List<IndentCreationBean> list = icd.viewIndentProductDetails(fromDate,toDate,siteId);
		return list;
	}


/******************************************************Rejected indents start***********************************************************************/

	@Override
	public List<IndentCreationBean> getRejectedIndentsList(String userId) {
		return icd.getRejectedIndentsList(userId);
	}

//Rejected Comments
	@Override
	public String  getIndentRejectedComments(int oldIndentNo) {
		return icd.getIndentRejectedComments(oldIndentNo);
				 
	}

	/****************************************************Rejected indent Purpose Start****************************************************************/

	@Override
	public String approveRejectedIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String user_Name) {

		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InCr_indCre, ");
		boolean isSendMail = true;
		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final IndentCreationDto indentCreationDto = new IndentCreationDto();
		//IndentCreationBean changedIndentDetails = new IndentCreationBean();
		int indentNumber= 0;
		String strIndentFrom= "";
		String strIndentTo = "";
		String purpose = "";
		int siteWiseIndentNo = 0;
		int portNo =0; 
		String siteName = "";
		String strpendingEmpId ="";
		String pendingEmployeeId="";
		String pendingEmpId ="";
		String checkCentralIndentOrNot="";
		int indentCreationDetailsSeqNum =0;
		String oldIndentNumber="";
		String indentName="";
		/*if(site_id==999){
		 strpendingEmpId = icd.getPendingEmployeeId(user_id,site_id);//this is for central indent
		 pendingEmployeeId=icd.getPendingEmployeeId(strpendingEmpId,site_id);
		}*/
		try
		{
			indentNumber= Integer.parseInt(request.getParameter("IndentNumber"));
			siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			strIndentFrom= request.getParameter("IndentFrom");
			strIndentTo = request.getParameter("IndentTo");
			oldIndentNumber=request.getParameter("old_Indent_Number");
			purpose = request.getParameter("Purpose");
			indentName=request.getParameter("indentName");
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			/*for(int num=1;num<=recordsCount;num++) {
			String actionValue = request.getParameter("actionValue"+num);
			}*/
			Date scheduleDate= new SimpleDateFormat("dd-MMM-yy").parse(request.getParameter("ScheduleDate"));
			Date requiredDate= new SimpleDateFormat("dd-MMM-yy").parse(request.getParameter("RequiredDate"));

			String versionNo=ValidateParams.validateParams.getProperty("indent_print_version_No");
			String reference_No=ValidateParams.validateParams.getProperty("indent_print_reference_No");
			String issue_date=ValidateParams.validateParams.getProperty("indent_print_issue_date");

			System.out.println("versionNo "+versionNo+" reference_No "+reference_No+" issue_date "+issue_date);
			indentCreationDto.setVersionNo(versionNo);
			indentCreationDto.setReference_No(reference_No);
			indentCreationDto.setIssue_date(issue_date);

			indentCreationDto.setSiteWiseIndentNo(siteWiseIndentNo);
			indentCreationDto.setIndentNumber(indentNumber);
			indentCreationDto.setScheduleDate(scheduleDate);
			indentCreationDto.setSiteId(site_id);
			indentCreationDto.setUserId(user_id);
			indentCreationDto.setPurpose(purpose);
			indentCreationDto.setRequiredDate(requiredDate);
			indentCreationDto.setTempPass(CommonUtilities.getStan());
			indentCreationDto.setIndentName(indentName);

			pendingEmpId = request.getParameter("approverEmpId");
			indentCreationDto.setPendingEmpId(pendingEmpId);
			indentCreationDto.setPendingDeptId("-");
			int response1 =  icd.insertIndentCreation(indentNumber, indentCreationDto);
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			int response3 =  icd.insertIndentCreationApproval(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			int response2 = 0;
			double doubleAvailableQuantity = 0.0;
			int recordsCount = Integer.parseInt(request.getParameter("numberOfRows"));
			logger.info("Rows To Be Processed = "+recordsCount);
			//int countOfRecords = Integer.valueOf(recordsCount);
			String numOfRec[] = null;
			/*if((int num=1;num<=noofRows;num++) {
				numOfRec = recordsCount.split("\\|");
			}*/
			for(int num=1;num<=recordsCount;num++) {
				//for(String num : numOfRec) {
					//num = num.trim();
					String actionValue = request.getParameter("actionValue"+num);
					//PROD101$Cement <--> SUBPROD101$KCP <--> CHLDPROD101$TetraBag <--> tyty <--> 43 <--> 101$KG
					if(actionValue==null || !actionValue.equals("R")){
					String product = request.getParameter("ProductId"+num);				
					String prodsInfo[] = product.split("\\$");
					String prodId = prodsInfo[0];
					String prodName = prodsInfo[1];
					//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);					
					String subProduct = request.getParameter("SubProductId"+num);
					String subProdsInfo[] = subProduct.split("\\$");
					String subProdId = subProdsInfo[0];
					String subProdName = subProdsInfo[1];					
					//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
					String childProduct = request.getParameter("ChildProductId"+num);

					String childProdsInfo[] = childProduct.split("\\$");
					String childProdId = childProdsInfo[0];
					String childProdName = childProdsInfo[1];					
					//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);
					String unitsOfMeasurement = request.getParameter("UnitsOfMeasurementIdval"+num);
					String measurementsInfo[] = unitsOfMeasurement.split("\\$");
					String measurementId = measurementsInfo[0];
					String measurementName = measurementsInfo[1];
					//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);
					String requiredQuantity = request.getParameter("RequiredQuantity"+num);
					if(requiredQuantity==null || requiredQuantity.equals("")){requiredQuantity = request.getParameter("Quantity"+num);}
					String availableQuantity = request.getParameter("ProductAvailability"+num)== null ? "" : request.getParameter("ProductAvailability"+num).toString();
					String remarks = request.getParameter("Remarks"+num);
					Map<String,String> map =null;
					if(measurementName.equals("Box"))
					{
						BoxToNos btn = new BoxToNos();
						map = btn.convertBoxToNos(childProdId,requiredQuantity,measurementName,measurementId);
						measurementId = map.get("MeasurementId");
						measurementName = map.get("MeasurementName");
						requiredQuantity = map.get("Quantity");
					}

					/***************************************** for convert the child product measurement start************************/
					String methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();
					// here checking the child product which available in properties files then 
					if(!methodName.equals("")) {	
						Map<String, String> values = null;

						String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassNameForIndent") == null ? "" : validateParams.getProperty("MesurmentConversionClassNameForIndent").toString();

						//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
						Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
						Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


						double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
						double doubleInputQuantity =  Double.valueOf(requiredQuantity);
						String strAvailableQuantityToConvert=requiredQuantity;
						String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
						// with multiple parameters
						//methodName = "convertCHP00536";
						Class<?>[] paramTypes = {double.class,double.class,String.class};
						Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
						values = (Map<String, String>) printDogMethod.invoke(mesurment,doubleActualQuantity,doubleInputQuantity,measurementName);
						
						for(Map.Entry<String, String> retVal : values.entrySet()) {
							BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
							requiredQuantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
							//quantity=retVal.getKey();
							//prc=retVal.getValue(); 
						}
						if(!requiredQuantity.equals(strAvailableQuantityToConvert)){
							
							doubleAvailableQuantity = Double.valueOf(availableQuantity);
							doubleAvailableQuantity=(doubleAvailableQuantity)*(doubleActualQuantity);
							doubleAvailableQuantity  = Double.parseDouble(new DecimalFormat("##.##").format(doubleAvailableQuantity));
							availableQuantity = String.valueOf(doubleAvailableQuantity);
							
						}
						//quantity =  String.valueOf(doubleQuantity);
						/*if(measurementName.equalsIgnoreCase("PCS")){
							
							double price=Double.parseDouble(basicAmnt)/doubleQuantity;
							prc=String.valueOf(price);
						}*/
						measurementId = strConversionMesurmentId;
						measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
					}
					/***************************************** for convert the child product measurement end************************/
					//doubleAvailableQuantity = Double.valueOf(availableQuantity);
					//doubleAvailableQuantity  = Double.parseDouble(new DecimalFormat("##.##").format(doubleAvailableQuantity));
					//availableQuantity = String.valueOf(doubleAvailableQuantity);
					

					IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
					indentCreationDetailsDto.setProdId(prodId);
					indentCreationDetailsDto.setProdName(prodName);
					indentCreationDetailsDto.setSubProdId(subProdId);
					indentCreationDetailsDto.setSubProdName(subProdName);
					indentCreationDetailsDto.setChildProdId(childProdId);
					indentCreationDetailsDto.setChildProdName(childProdName);
					indentCreationDetailsDto.setMeasurementId(measurementId);
					indentCreationDetailsDto.setMeasurementName(measurementName);

					indentCreationDetailsDto.setRequiredQuantity(requiredQuantity);
					indentCreationDetailsDto.setAvailableQuantity(availableQuantity);
					indentCreationDetailsDto.setRemarks(remarks);
					listProductDetails.add(indentCreationDetailsDto);
					
					 indentCreationDetailsSeqNum = icd.getIndentCreationDetailsSequenceNumber();
					response2 +=  icd.insertIndentCreationDetails(indentCreationDetailsSeqNum, indentNumber, indentCreationDetailsDto);
					
					/*if((site_id==999 && (pendingEmployeeId.equals("") || pendingEmployeeId.equals("-")))){
						IndentCreationBean changedIndentDetails = new IndentCreationBean();
						changedIndentDetails.setProductId1(prodId);
						changedIndentDetails.setSubProductId1(subProdId);
						changedIndentDetails.setChildProductId1(childProdId);
						changedIndentDetails.setUnitsOfMeasurementId1(measurementId);
						changedIndentDetails.setRequiredQuantity1(requiredQuantity);
						//if(actionValue==null || !actionValue.equals("R")){
						int centralIndentProcessId = icd.getCentralIndentProcessSequenceNumber();
						icd.insertCentralIndentProcess(centralIndentProcessId,changedIndentDetails,indentCreationDetailsSeqNum,site_id);
						icd.updateIndentCreationForCentral(pendingEmployeeId,site_id,indentNumber);//for central indent 
						//}
						indentCreationDto.setPendingDeptId(String.valueOf(site_id));
						indentCreationDto.setPendingEmpId("-");
						 checkCentralIndentOrNot="true";
						 
					}*/
					}
				//end of FOR loop

			}//end of IF block
			if(response2>0 || checkCentralIndentOrNot.equalsIgnoreCase("true")){
				icd.updateIndentCreationApprovetbl(oldIndentNumber);
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "success";
			portNo=request.getLocalPort();

		}//end of TRY block
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response = "Failed";
			e.printStackTrace();
			isSendMail = false;
		}
		
		if(isSendMail){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{
				final String finalPendingEmpId = pendingEmpId;
				final String finalcheckCentralIndentOrNot=checkCentralIndentOrNot;
				final int indentNumber_final = indentNumber;
				final String strIndentFrom_final = strIndentFrom;
				final String strIndentTo_final = strIndentTo;
				final String purpose_final = purpose;
				final int siteWiseIndentNo_final = siteWiseIndentNo;
				final int portNo_final=portNo;
				final String strUserName = user_Name;
				final String strindentName=indentName;
				executorService.execute(new Runnable() {
					public void run() {

						if(finalcheckCentralIndentOrNot.equalsIgnoreCase("true")){
							sendEmailDetails( "-", indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDto,purpose_final,null,siteWiseIndentNo_final,portNo_final,strUserName,strindentName);

						}else{

						//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
						sendEmailDetails( finalPendingEmpId, indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDto,purpose_final,null,siteWiseIndentNo_final,portNo_final,strUserName,strindentName);
						}
					}
				});

				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}

		}
		//this try catch for emil genaration
		return response;
	}

	
	public String getPoLevelComments(String poNumber,String FinalPoOrNot){
		return objPurchaseDepartmentIndentProcessDao.getPoLevelComments(poNumber,FinalPoOrNot);
	}
/*************************************************************Rejected Indent indent purpose End****************************************************/
	/*========================================material BOQ start ======================================================================*/

@SuppressWarnings("null")
// this is for getting and check the quantity for boq purpose
@Override
public String getAndCheckBOQQuantity(HttpSession session,HttpServletRequest request,String siteId,boolean approveOrNot) {

	
	String recordsCount = request.getParameter("materialRows")==null ? "" : request.getParameter("materialRows");
	//String val=request.getParameter("numberOfRows")==null ? "" : request.getParameter("numberOfRows");
	String childProduct="";
	double receivedQuantity=0.0;
	double issuedQuantity=0.0;
	double indentPendingQuantity=0.0;
	double totalBOQQuantity=0.0;
	String groupId="0";
	double enteredQuantity=0.0;
	String numOfRec[] = null;
	String childProdId ="";
	String childProdName ="";
	String childProductList="";
	String measurementList="";
	double totalQuantity=0.0;
	String response="";
	Map<String,Double> groupIdsSortMap = new HashMap<String,Double>();
	Map<String,String> childSortMap = new HashMap<String,String>();
	Map<String,String> childProductMap = new HashMap<String,String>();
	String measurement="";
	//String strGroupId="";
	double tempQuantity = 0.0;
	String measurementId="";
	String measurementName="";
	double oldQuantity=0.0;
	if(approveOrNot){
		numOfRec = new 	String[Integer.parseInt(recordsCount)];
		for(int k=0;k<Integer.parseInt(recordsCount);k++){
		numOfRec [k]=String.valueOf(k+1);
		}
	//	Arrays.toString(numOfRec);
	}
	if((recordsCount != null) && (!recordsCount.equals("") && !approveOrNot)) {
		numOfRec = recordsCount.split("\\|");
	}
	
	if(numOfRec != null && numOfRec.length > 0) {
		for(String num : numOfRec) {
			num = num.trim();
			String actionValue = request.getParameter("actionValue"+num)==null ? "" : request.getParameter("actionValue"+num);
			childProduct=request.getParameter("ChildProduct"+num)==null ? request.getParameter("ChildProductId"+num)==null ? "" : request.getParameter("ChildProductId"+num) : request.getParameter("ChildProduct"+num);
			measurement=request.getParameter("UnitsOfMeasurement"+num)==null ? "" : request.getParameter("UnitsOfMeasurement"+num);
			
			childProdId = childProduct.split("\\$")[0];
			childProdName= childProduct.split("\\$")[1];
			measurementId=measurement.split("\\$")[0];
			measurementName=measurement.split("\\$")[1];
			
			
			 if(!actionValue.equals("R")){
				 groupId=request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
				 oldQuantity=Double.valueOf(request.getParameter("oldQuantity"+num)==null ? "0" : request.getParameter("oldQuantity"+num));
				 if(oldQuantity==0){
				 enteredQuantity=Double.valueOf(request.getParameter("Quantity"+num)==null ? "" : request.getParameter("Quantity"+num));}
				 else{enteredQuantity=Double.valueOf(request.getParameter("RequiredQuantity"+num)==null ? "" : request.getParameter("RequiredQuantity"+num));}
				
			 }
			if(!groupId.equals("0") && groupId!=null){
				//			/childProductList
				if(!childProductMap.containsKey(childProdId)){
				childSortMap=icd.getChildProductsWithGroupId(groupId);
				for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
					childProductList += entry.getKey() +",";
					measurementList += entry.getValue() +",";
				}
				
				if(childProductList!=null && !childProductList.equals("")){
					childProductList =  childProductList.substring(0,childProductList.length()-1);
				}if(measurementList!=null && !measurementList.equals("")){
					measurementList =  measurementList.substring(0,measurementList.length()-1);
				}
					receivedQuantity=icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList);
					issuedQuantity=icd.getIssuedQuantity(childProductList,siteId,measurementList);
					indentPendingQuantity=icd.getIndentPendingQuantity(childProductList,siteId,measurementList);
					totalBOQQuantity=icd.getBOQQuantity(groupId,siteId);
					for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
						childProductMap.put(entry.getKey(),receivedQuantity+"_"+issuedQuantity+"_"+indentPendingQuantity+"_"+totalBOQQuantity);
					}
				}else{
					String temp_Quantity =(childProductMap.get(childProdId));
					String info[]=temp_Quantity.split("_");
					 receivedQuantity=Double.valueOf(info[0]);
					 issuedQuantity=Double.valueOf(info[1]);
					 indentPendingQuantity=Double.valueOf(info[2]);
					 totalBOQQuantity=Double.valueOf(info[3]);
				}
				if(!groupIdsSortMap.containsKey(groupId)){
					
					if(oldQuantity!=enteredQuantity && oldQuantity!=0){
						double checkQuantity=oldQuantity-enteredQuantity;
						indentPendingQuantity=indentPendingQuantity-checkQuantity;
						groupIdsSortMap.put(groupId,((checkQuantity)));
						}else if(oldQuantity==0){
						indentPendingQuantity+=enteredQuantity;//-checkQuantity;
						groupIdsSortMap.put(groupId,(enteredQuantity));
						}
					//tempQuantity=enteredQuantity; // curretly entered quantity
				}else{
					if(oldQuantity!=enteredQuantity && oldQuantity!=0){
						double value=0.0;
						double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
						double checkQuantity=oldQuantity-enteredQuantity;
						value=DoublePrevQuantity+checkQuantity;
						groupIdsSortMap.put(groupId,(value));
						indentPendingQuantity=indentPendingQuantity-value;
					}else if(oldQuantity==0){
						double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
						tempQuantity = Double.valueOf(enteredQuantity)+DoublePrevQuantity; // currently entered quantity
						indentPendingQuantity+=tempQuantity;
						groupIdsSortMap.put(groupId,(tempQuantity));}
				}
				
				totalQuantity=receivedQuantity-(issuedQuantity)+indentPendingQuantity;//<=totalBOQQuantity;

				if(totalQuantity<=totalBOQQuantity){
					continue;
				}else{
					response="You can not initiate Child Product \""+childProdName+"\"more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
					break;
				}

			}
			
			
		}
	}
	return response;
}

@Override
public String gettingBoqQuantityAjax(String groupId,String siteId) {
	Map<String,String> childSortMap = new HashMap<String,String>();
	double receivedQuantity=0.0;
	double issuedQuantity=0.0;
	double indentPendingQuantity=0.0;
	double totalBOQQuantity=0.0;
	double resultQuantity=0.0;
	String childProductList="";
	String measurementList="";
	StringBuffer totalData=new StringBuffer();
	childSortMap=icd.getChildProductsWithGroupId(groupId);
	for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
		childProductList += entry.getKey() +",";
		measurementList += entry.getValue() +",";
	}
	
	if(childProductList!=null && !childProductList.equals("")){
		childProductList =  childProductList.substring(0,childProductList.length()-1);
	}if(measurementList!=null && !measurementList.equals("")){
		measurementList =  measurementList.substring(0,measurementList.length()-1);
	}
	 receivedQuantity=(icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList));
	 issuedQuantity=(icd.getIssuedQuantity(childProductList,siteId,measurementList));
	 indentPendingQuantity=(icd.getIndentPendingQuantity(childProductList,siteId,measurementList));
	 totalBOQQuantity=(icd.getBOQQuantity(groupId,siteId));
	 resultQuantity=totalBOQQuantity-(receivedQuantity-issuedQuantity+indentPendingQuantity);
	 if(resultQuantity<0){ resultQuantity=0;}
	 totalData=totalData.append(resultQuantity+"_"+totalBOQQuantity);
	return totalData.toString();
}


	//String childProductId=request.getParameter("ChildProductId")==null ? "" : request.getParameter("ChildProductId");
	//String groupId=request.getParameter("groupId")==null ? "" : request.getParameter("groupId");
	
}
/*public static void main(String ar[]){
	System.out.println("buchiki");
	int max=0;
	String chilN="";
	String mase="";
	
	String child="CHP0041@@MST0041,CHP0253@@MST0253,CHP0253@@MST0253";
	String info[]=child.split(",");
	for (int i = 0; i < info.length; i++) {
		String jk[]=info[i].split("@@");
		chilN=jk[0];mase=jk[1];
		System.out.println("child name"+chilN+"measurement"+mase);
	}
	String name="hyderabad";
	String reverse="";
	for(int i=name.length();i<0;i--){
		reverse=reverse+name.charAt(i);
		
	}
	System.out.println("the reverse ofv string is:"+reverse);
	
	int[] arr = {7,3,1,2,9};
	max=arr[0];
	for(int i=0;i<arr.length;i++){
		//for(int j=i+1;j<arr.length;j++){
		if(arr[i]>max){
			max=arr[i];
		}else{
			//max=arr[j];
			//System.out.Prinln("max value"+);
		}
	//}
}
	System.out.println("max number"+max);
}
}*/

class InitiatedNotReturnedException extends RuntimeException
{
	InitiatedNotReturnedException(String s)
	{
		super(s);
	}
}