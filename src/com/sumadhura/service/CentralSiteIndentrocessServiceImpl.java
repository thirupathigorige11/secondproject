package com.sumadhura.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.transdao.CentralSiteIndentProcessDao;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.util.MobileSMS;
import com.sumadhura.util.UIProperties;


@Service("cntlIndentprocess")
public class CentralSiteIndentrocessServiceImpl extends UIProperties implements CentralSiteIndentrocessService{

	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;

	@Autowired
	MobileSMS mobileSMS;	
	
	@Autowired
	private IndentCreationDao icd;
	
	@Override
	public String sendToPD(Model model, HttpServletRequest request, int site_id, String user_id,String userName) {
		boolean isSendMail = true;
		String response = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in CeIn_senTPD, ");
		String pendingEmpId = "";
		int indentNumber = 0;
		String strIndentFrom = "";
		String strIndentTo = "";
		int siteWiseIndentNo = 0;
		int portNo=0;
		String indentName="";
		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final IndentCreationDto indentCreationDtoForMail = new IndentCreationDto();
		try
		{
			indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
			indentName=request.getParameter("indentName");
			pendingEmpId = "-";
			String pendingDeptId = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			String reqSiteName = request.getParameter("siteName");
			int indentReqSiteId = cntlIndentrocss.getSiteIdByName(reqSiteName);
			String reqReceiveFrom = user_id;
			strIndentFrom = "Central";
			strIndentTo = "Purchase Department";
			siteWiseIndentNo= Integer.parseInt(request.getParameter("siteWiseIndentNo"));
			indentCreationDtoForMail.setSiteId(indentReqSiteId);
			indentCreationDtoForMail.setPendingEmpId(pendingEmpId);
			portNo=request.getLocalPort();
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			

			int response1 = icd.updateIndentCreation(indentNumber, pendingEmpId, pendingDeptId);
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			indentCreationDto.setSiteId(site_id);
			indentCreationDto.setUserId(user_id);
			indentCreationDto.setPurpose("");
			int indentCreationApprovalSeqNum = icd.getIndentCreationApprovalSequenceNumber();
			int response2 = icd.insertIndentCreationApprovalAsApprove(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			int noofrows = Integer.parseInt(request.getParameter("numberOfRows"));
			for(int num=1;num<=noofrows;num++){
				int IndentCreationDetailsId = Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));
				String productId = request.getParameter("productId"+num);
				String subProductId = request.getParameter("subProductId"+num);
				String childProductId = request.getParameter("childProductId"+num);
				String unitsOfMeasurementId = request.getParameter("unitsOfMeasurementId"+num);
				String requiredQuantity = request.getParameter("requiredQuantity"+num);
				IndentCreationBean purchaseIndentDetails = new IndentCreationBean();
				purchaseIndentDetails.setProductId1(productId);
				purchaseIndentDetails.setSubProductId1(subProductId);
				purchaseIndentDetails.setChildProductId1(childProductId);
				purchaseIndentDetails.setUnitsOfMeasurementId1(unitsOfMeasurementId);
				purchaseIndentDetails.setRequiredQuantity1(requiredQuantity);
				IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
				indentCreationDetailsDto.setProdName(request.getParameter("productName"+num));
				indentCreationDetailsDto.setSubProdName(request.getParameter("subProductName"+num));
				indentCreationDetailsDto.setChildProdName(request.getParameter("childProductName"+num));
				indentCreationDetailsDto.setMeasurementName(request.getParameter("unitsOfMeasurementName"+num));
				indentCreationDetailsDto.setRequiredQuantity(requiredQuantity);
				
				String strComments=request.getParameter("comments"+num);
				String  changedRemarks=request.getParameter("remarks"+num);
				
				indentCreationDetailsDto.setRemarks(request.getParameter("remarks"+num));
				listProductDetails.add(indentCreationDetailsDto);
				
				if(changedRemarks !=null && !changedRemarks.equals("")){
					changedRemarks = changedRemarks + "@@@" + strComments;
				}else{
					changedRemarks = strComments;
				}
				
				icd.updateProductsComments(IndentCreationDetailsId,changedRemarks);
				//taking data from table 
				IndentCreationDto centralTableData = cntlIndentrocss.getCentralIndentProcessData(IndentCreationDetailsId);

				int purchaseIndentProcessId = cntlIndentrocss.getPurchaseIndentProcessSequenceNumber();
				/*if(!centralTableData.getIntiatedQuantity().equals("0")){throw new InitiatedNotReturnedException("Purchase Order Cannot be Processed. Because Intiated Quantity NOT Returned");}*/
				cntlIndentrocss.insertPurchaseIndentProcess(purchaseIndentProcessId,purchaseIndentDetails,IndentCreationDetailsId,indentReqSiteId,reqReceiveFrom,centralTableData);
			}
			//String s=null;s.trim();
			
			System.out.println("Indent Sent To Purchase Dept");
			System.out.println("responses: "+response1+","+response2);
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response = "Indent Sent To Purchase Dept Successfully";
			request.setAttribute("AuditResponse", "Success");

		}//end of TRY block
		catch(InitiatedNotReturnedException e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
			System.out.println("Indent Sent To Purchase Dept Failed");
			response = "Sent Indent To Purchase Dept failed ";
			System.out.println(e.getMessage());
			request.setAttribute("AuditResponse", "Fail");
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("Indent Sent To Purchase Dept Failed");
			response = "Failed To Sent Indent To Purchase Dept ";
			isSendMail = false;
			e.printStackTrace();
			request.setAttribute("AuditResponse", "Fail");
		}
		//---------------------
		if(isSendMail){
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		try{

			final String strPendingEmpId = pendingEmpId;
			final int indentNumber_final = indentNumber;
			final String strIndentFrom_final = strIndentFrom;
			final String strIndentTo_final = strIndentTo;
			final int siteWiseIndentNo_final = siteWiseIndentNo;
			final int portNo_final=portNo;
			final String strUserName = userName;
			final String strindentName=indentName;
			executorService.execute(new Runnable() {
				public void run() {

					String  purpose = getIndentLevelComments(indentNumber_final);



					List<IndentCreationBean> list =  getndentChangedDetails(indentNumber_final);




					sendEmailDetails( strPendingEmpId, indentNumber_final, strIndentFrom_final, strIndentTo_final,listProductDetails, indentCreationDtoForMail,purpose,list,siteWiseIndentNo_final,portNo_final,strUserName,strindentName);



				}


			});




			executorService.shutdown();
		}catch(Exception e){
			e.printStackTrace();
			executorService.shutdown();
		}
		}
		//---------------------
		return response;
	}
	
	public void sendEmailDetails(String pendingEmpId,int indentNumber,String indentFrom,String indentTo,List<IndentCreationDetailsDto> listProductDetails, IndentCreationDto indentCreationDto,String strIndentLevelComments,List<IndentCreationBean> strProductsChangedComments, int siteWiseIndentNo,int portNo,String strUserName,String indentName){


		try{
			List<Map<String, Object>> indentCreationDtls = null;
			List<String> toMailListArrayList = new ArrayList<String>();

			
			indentCreationDtls = icd.getIndentCreationDetails(indentNumber,"approvalToDept");
			String purchaseDeptId  = validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			toMailListArrayList = icd.getAllEmployeeEmailsUnderDepartment(purchaseDeptId);
			




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

				objEmailFunction.sendIndentCreationApprovalMailDetails( indentTo, indentNumber, indentFrom,strIndentFromSite, strIndentFromDate, strScheduleDate,emailto,listProductDetails,indentCreationDto,strIndentLevelComments,list,siteWiseIndentNo,portNo,strUserName,indentName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public List<IndentCreationBean> getCentralIndentDetailsLists(int indentNumber,String siteId) {
		return cntlIndentrocss.getCentralIndentDetailsLists(indentNumber,siteId);
	}
	@Override
	public List<IndentCreationBean> getPurchaseIndentDetailsLists(int indentNumber,String strReqSiteId) {
		return cntlIndentrocss.getPurchaseIndentDetailsLists(indentNumber,strReqSiteId);
	}
	@Override
	public List<ProductDetails> getPurchaseIndentDtlsLists(String  indentCreationDetailsIdForenquiry,String strVendorId) {
		return cntlIndentrocss.getPurchaseIndentDtlsLists(indentCreationDetailsIdForenquiry,strVendorId);
	}
	@Override
	public String requestToOtherSite(Model model, HttpServletRequest request, int site_id, String user_id, HttpSession session) {
		String response = "";
		boolean isSendMail = true;
		int indentNumber = 0;
		String siteWiseIndentNo="";
		String strUserName="";
		String currentUserMobileNo="";
		String currentUserEmail="";
		String currentSiteId="";
		
		String reqSiteName="";
		int reqSiteId = 0;
		int noofrows = 0;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		Map<String, List<IndentCreationBean>> holdTheSettelMentDataBySite=new HashMap<String, List<IndentCreationBean>>();
		
		List<IndentCreationBean> holdTheSettelMentData=new ArrayList<IndentCreationBean>();
		
		WriteTrHistory.write("Tr_Opened in CeIn_reqOth, ");
		try
		{
			strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			currentSiteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			currentUserEmail =session.getAttribute("EMP_EMAIL") == null ? "" :  session.getAttribute("EMP_EMAIL").toString();
			currentUserMobileNo=session.getAttribute("MOBILE_NUMBER") == null ? "" :  session.getAttribute("MOBILE_NUMBER").toString();
			
			indentNumber = Integer.parseInt(request.getParameter("bfIndentNumber")); 
			reqSiteId = Integer.parseInt(request.getParameter("bfSiteId")); 
			noofrows = Integer.parseInt(request.getParameter("bfRows"));
			siteWiseIndentNo=request.getParameter("siteWiseIndentNo12")==null?"":request.getParameter("siteWiseIndentNo12");
			reqSiteName=request.getParameter("siteName")==null?"":request.getParameter("siteName");
			WriteTrHistory.write("Site:"+site_id+" , User:"+user_id+" , Date:"+new java.util.Date()+" , IndentNumber:"+indentNumber);
			
			String[] numberOfRowsToProceed=request.getParameterValues("settlementRowNumber");
			for (String num : numberOfRowsToProceed) {	num = num.trim();
			
		//	for(int num=1;num<=noofrows;num++)	{
				
				
				List<IndentCreationBean> tempSettelMentData=new ArrayList<IndentCreationBean>();
				String strSenderSite = request.getParameter("sitename"+num);
				int senderSite;
				String aQuantity = request.getParameter("aquantity"+num);
				try {
					//Double.parseDouble(aQuantity);
					//Integer.parseInt(aQuantity);
					senderSite = cntlIndentrocss.getSiteIdByName(strSenderSite);
				} catch(Exception e) { return "inputwrong";}

				String bulkdata = request.getParameter("bulkdata"+num);
				String bulkdataProdName=request.getParameter("bulkdataProdName"+num);
				String tempSenderSite=String.valueOf(senderSite);
				//System.out.println("sendersite: >>"+senderSite+"<<");
				//System.out.println("bulkdata: "+bulkdata);
				String[] bulkarrayProdId = bulkdata.split("@@");
				String[] bulkarrayProdName = bulkdataProdName.split("@@");
				IndentCreationBean indentCreationBean = new IndentCreationBean();
				
				indentCreationBean.setProductId1(bulkarrayProdId[0]);
				indentCreationBean.setSubProductId1(bulkarrayProdId[1]);
				indentCreationBean.setChildProductId1(bulkarrayProdId[2]);
				indentCreationBean.setUnitsOfMeasurementId1(bulkarrayProdId[3]);
				
				indentCreationBean.setProduct1(bulkarrayProdName[0]);
				indentCreationBean.setSubProduct1(bulkarrayProdName[1]);
				indentCreationBean.setChildProduct1(bulkarrayProdName[2]);
				indentCreationBean.setUnitsOfMeasurement1(bulkarrayProdName[3]);
			
				indentCreationBean.setIndentSettledEmployeeName(strUserName);
				indentCreationBean.setCurrentUserEemail(currentUserEmail);
				indentCreationBean.setCurrentUserMobileNumber(currentUserMobileNo);
				
				int indentProcessId = Integer.parseInt(bulkarrayProdId[4]);
				
				
				indentCreationBean.setIndentProcessId(indentProcessId);
				indentCreationBean.setIndentNumber(indentNumber);
				indentCreationBean.setSiteId(reqSiteId);
				indentCreationBean.setSiteName(reqSiteName);
				indentCreationBean.setRequiredQuantity1(aQuantity);
				
				indentCreationBean.setSenderSite(tempSenderSite);
				indentCreationBean.setSenderSiteName(strSenderSite);
				
				indentCreationBean.setSiteWiseIndentNumber(siteWiseIndentNo);
				
				
				//this method is for validating the quantity of site, we can not send the quantity if the requested qunaity is more than that site stock
				try {
					boolean isQuanityAvailable=	cntlIndentrocss.validateSiteQuantity(indentCreationBean,indentProcessId,senderSite);
					if(isQuanityAvailable){
						//if isQuanityAvailable is true then Allocating quantity should be below Site quantity if it is more throw the error
						return "Allocating quantity should be below Site quantity.";
					}
				} catch (Exception e) {
					e.printStackTrace();
						return "Allocating quantity should be below Site quantity.";	
				}
			
				//holdTheSettelMentDataBySite this map object holding the site wise settlement data
				//because we have to send the mails to the sites that are involved in indent settlement
				if(holdTheSettelMentDataBySite.containsKey(tempSenderSite)){
					tempSettelMentData=	holdTheSettelMentDataBySite.get(tempSenderSite);
					tempSettelMentData.add(indentCreationBean);
					holdTheSettelMentDataBySite.put(tempSenderSite,tempSettelMentData);
				}else{
					tempSettelMentData.add(indentCreationBean);
					holdTheSettelMentDataBySite.put(tempSenderSite,tempSettelMentData);
				}
				
				//loading sequence number
				int centralIndentReqDetailsSeqNum = cntlIndentrocss.getCentralIndentRequestDetailsSequenceNumber();
				if(cntlIndentrocss.getIndentProcessIdCount(indentProcessId,senderSite)>0){
					//if the sender site already sent the material last time , then this method will update the requested quantity
					cntlIndentrocss.updateRequestToOtherSite(indentCreationBean,centralIndentReqDetailsSeqNum,senderSite);
				}
				else
				{
					//if the site is sending first time material to the spercific site the we are inserting the new record
					cntlIndentrocss.requestToOtherSite(indentCreationBean,centralIndentReqDetailsSeqNum,senderSite);
				}
				
				cntlIndentrocss.updateInitiatedQuantityInCentralTable(aQuantity,indentProcessId);
				
				holdTheSettelMentData.add(indentCreationBean);
				/*if(bulkdata1!=null){bulkdata = bulkdata1;}
				  if(sendersite1==null){continue;}else{if(sendersite1.equals(""))	{continue;}}*/
			}//end of FOR loop

			 
			
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("Request Sent To Other Site");
			response = "Sucess";

		}catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			//System.out.println("Request Sending To Other Site Failed");
			response ="Failed";
			e.printStackTrace();
			isSendMail = false;
		}
		
	if(isSendMail){		
			
		List<String > ccMailsOfIndentEmployee=new ArrayList<String>();
		//holding the HTML page content, this content will show in mail
		StringBuffer indentSitetable_DataIndentSettlementForMail=new StringBuffer();
		//holding the mail subject
		StringBuffer toIndentSite_subjectForMailIndentSettlement=new StringBuffer();
		//holding the mail subject
		toIndentSite_subjectForMailIndentSettlement.append("Indent No -  "+siteWiseIndentNo+" material adjustment ");
		int senderSiteSize=holdTheSettelMentDataBySite.size();
		int countSenderSiteSize=0;
		for (Entry<String, List<IndentCreationBean>> siteWiseData : holdTheSettelMentDataBySite.entrySet()) {
			StringBuffer tableDataIndentSettlementForMail=new StringBuffer();
			StringBuffer subjectForMailIndentSettlement=new StringBuffer();
			IndentCreationBean indentCreationBean12=new IndentCreationBean();
			
			//holding mobile number for sending the message to users
			List<String> mobilesList=new ArrayList<String>();
			//holding message content 
			String smsMessageContent="";
			int serialNo=1;
			countSenderSiteSize++;
			for (IndentCreationBean indentCreationBean : siteWiseData.getValue()) {
				//System.out.println(indentCreationBean);
				indentCreationBean12=indentCreationBean;
				
				if(serialNo==1){
					indentSitetable_DataIndentSettlementForMail.append("<br>You will receive below materials from "+indentCreationBean.getSenderSiteName()+"<br>");
					
					indentSitetable_DataIndentSettlementForMail.append("")
							.append("<div style='overflow-x:auto;margin-top:10px;'>")
							.append("<table cellspacing='0' cellpadding='0'>")
							.append("<thead>")
							.append("<tr>")
							.append("<th>S.No</th>")
							.append("<th>Indent Number</th>")
							.append("<th>Product Name</th>")
							.append("<th>Sub Product Name</th>")
							.append("<th>Child Product Name</th>")
							.append("<th>UOM</th>")
							.append("<th>Quantity</th>")
							.append("</tr>")
							.append("</thead>")
							.append("<tbody>");
				}
				
				subjectForMailIndentSettlement=new StringBuffer();
				//this variable is holding the data for mail site wise means sender site data 
				tableDataIndentSettlementForMail.append("<tr>")
						.append("<td>"+serialNo+"</td> ")
						.append("<td>"+siteWiseIndentNo+"</td> ")
						.append("<td>"+indentCreationBean.getProduct1()+"</td> ")
						.append("<td>"+indentCreationBean.getSubProduct1()+"</td> ")
						.append("<td>"+indentCreationBean.getChildProduct1()+"</td>  ")
						.append("<td>"+indentCreationBean.getUnitsOfMeasurement1()+"</td>  ")
						.append("<td>"+indentCreationBean.getRequiredQuantity1()+"</td>  ")
						.append("</tr>");
				
				//this variable holding the data for indent site mail data(who created the indent)
				indentSitetable_DataIndentSettlementForMail.append("<tr>")
						.append("<td>"+serialNo+"</td> ")
						.append("<td>"+siteWiseIndentNo+"</td> ")
						.append("<td>"+indentCreationBean.getProduct1()+"</td> ")
						.append("<td>"+indentCreationBean.getSubProduct1()+"</td> ")
						.append("<td>"+indentCreationBean.getChildProduct1()+"</td>  ")
						.append("<td>"+indentCreationBean.getUnitsOfMeasurement1()+"</td>  ")
						.append("<td>"+indentCreationBean.getRequiredQuantity1()+"</td>  ")
						.append("</tr>");
				
				if(siteWiseData.getValue().size()==serialNo){
					indentSitetable_DataIndentSettlementForMail.append("</tbody>")
							.append("</table>")
							.append("</div>");
				}
				
				subjectForMailIndentSettlement.append("Request for Material Adjustment from Site "+indentCreationBean.getSenderSiteName()+" to Site "+indentCreationBean.getSiteName()+".");
				smsMessageContent="Central Store had requested Material for Site "+indentCreationBean.getSiteName()+". A Material request from Site "+indentCreationBean.getSenderSiteName()+" to Site "+indentCreationBean.getSiteName()+". For more details follow http://129.154.74.18/Sumadhura/";
				serialNo++;
			}
			
			//holding the sender site email and phone number, using this we have to trigger mail to them
			List<String> senderSiteMail=new ArrayList<String>();
			//loading sender site email and phone number of employee
			String emailUsername=UIProperties.validateParams.getProperty(siteWiseData.getKey()+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(siteWiseData.getKey()+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername2=UIProperties.validateParams.getProperty(currentSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(currentSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername3=UIProperties.validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			
			String ccEmailUsername1=UIProperties.validateParams.getProperty(siteWiseData.getKey()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(siteWiseData.getKey()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String ccEmailUsername2=UIProperties.validateParams.getProperty(currentSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(currentSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String ccEmailUsername3=UIProperties.validateParams.getProperty(reqSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			
			String mobileNumber1=UIProperties.validateParams.getProperty(siteWiseData.getKey()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(siteWiseData.getKey()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			String mobileNumber2=UIProperties.validateParams.getProperty(currentSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(currentSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			String mobileNumber3=UIProperties.validateParams.getProperty(reqSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
	
			
		 
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
			
			
			try {
				if(mobilesList.size()!=0)
				mobileSMS.sendSMS(mobilesList, smsMessageContent);
			} catch (Exception e) {
	 			e.printStackTrace();
			}
			
			
			//String emailPassord=UIProperties.validateParams.getProperty(siteWiseData.getKey()+"_PASSWORD_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(siteWiseData.getKey()+"_PASSWORD_FOR_CENTRAL_SETTLEMENT").toString();
			//String mobileNumber=UIProperties.validateParams.getProperty(siteWiseData.getKey()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(siteWiseData.getKey()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			
			//senderSiteMail.add(emailPassord);
			//senderSiteMail.add(mobileNumber);
			
			//System.out.println("indentSettlementTableDataForMail\n "+tableDataIndentSettlementForMail);
			//here we are sending the mails to the site, which are involved in product settlement
			EmailFunction objEmailFunction = new EmailFunction();
			//here are sending the mail to (Indent Site,Central site, and the site who is giving material to Indent Site)
			objEmailFunction.sendMailForIndentSettlement(indentCreationBean12,reqSiteId,ccMailsOfIndentEmployee,senderSiteMail,tableDataIndentSettlementForMail,subjectForMailIndentSettlement,"true");
			
			//sending sms to indent site people
			//code dnt remve
			if(countSenderSiteSize==senderSiteSize){/*
				senderSiteMail=new ArrayList<String>();
				ccMailsOfIndentEmployee=new HashMap<String, String>();
				mobilesList=new ArrayList<Long>();
				emailUsername=UIProperties.validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(reqSiteId+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
				if(emailUsername.contains(",")){
					senderSiteMail.addAll(Arrays.asList(emailUsername.split(",")));
				}else{
					senderSiteMail.add(emailUsername);	
				}
				
				//loading the indent created and approved employee names and email's and phone number
				List<Map<String, Object>> approveEmail=cntlIndentrocss.getIndentCreatedEmpName(reqSiteId,indentNumber);
				for (Map<String, Object> map : approveEmail) {
					String mobileNumber=map.get("MOBILE_NUMBER")==null?"":map.get("MOBILE_NUMBER").toString();
					String email=map.get("EMP_EMAIL")==null?"":map.get("EMP_EMAIL").toString();
					if(mobileNumber.length()!=0)
						mobilesList.add(Long.valueOf(mobileNumber));
					ccMailsOfIndentEmployee.put(email,mobileNumber);
				}
				
				try {
					if(mobilesList.size()!=0){
						//sending SMS to user's, who are involved in indent creation and approval
						mobileSMS.sendOTP(mobilesList, smsMessageContent);
					}
				} catch ( Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				indentCreationBean12.setIndentSettledEmployeeName(strUserName);
				indentCreationBean12.setCurrentUserEemail(currentUserEmail);
				indentCreationBean12.setCurrentUserMobileNumber(currentUserMobileNo);
				
				indentCreationBean12.setIndentNumber(indentNumber);
				indentCreationBean12.setSiteId(reqSiteId);
				indentCreationBean12.setSiteName(reqSiteName);
				indentCreationBean12.setSiteWiseIndentNumber(siteWiseIndentNo);
				//sending mail indent site this mail template is different this mail will go only to Indent Site
				//in this mail content how many sites are sending material to this indent site we are showing in this mail
				//objEmailFunction.sendMailForIndentSettlement(indentCreationBean12,reqSiteId,ccMailsOfIndentEmployee,senderSiteMail,indentSitetable_DataIndentSettlementForMail,toIndentSite_subjectForMailIndentSettlement,"false");
			*/}
		}
}
		return response;

	}
	@Override
	public List<IndentCreationBean> getAllCentralIndents() {
		String centralDeptId = validateParams.getProperty("CENTRAL_DEPT_ID") == null ? "" : validateParams.getProperty("CENTRAL_DEPT_ID").toString();
		return icd.getIndentFromAndToDetails(centralDeptId);
	}



	@Override
	public List<IndentCreationBean> getIndentCreationLists(int indentNumber) {
		List<IndentCreationBean> list = icd.getIndentCreationLists(indentNumber);
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
}
