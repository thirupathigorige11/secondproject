package com.sumadhura.service;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

 
import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.dto.IndentIssueDto;
import com.sumadhura.dto.IndentIssueRequesterDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.dto.IssueToOtherSiteDto;
import com.sumadhura.transdao.IndentIssueDao;
import com.sumadhura.transdao.IssueToOtherSiteDao;
import com.sumadhura.transdao.WorkOrderDao;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.util.ValidateParams;

@Service("iisClass")
public class IndentIssueServiceImpl extends UIProperties implements IndentIssueService {

	@Autowired
	private IndentIssueDao iid;
	
	@Autowired
	@Qualifier("workControllerDao")
	WorkOrderDao workControllerDao;
	
	@Autowired
	private IssueToOtherSiteDao itosd;


	@Autowired
	PlatformTransactionManager transactionManager;

	@Override
	public Map<String, String> loadProds(String siteId) {
		return iid.loadProds(siteId);
	}

	@Override
	public String loadSubProds(String prodId) {
		return iid.loadSubProds(prodId);
	}

	@Override
	public String loadChildProds(String subProductId) {
		return iid.loadChildProds(subProductId);
	}

	@Override
	public String loadIndentIssueMeasurements(String childProdId,String strSiteId) {
		return iid.loadIndentIssueMeasurements(childProdId,strSiteId);
	}

	@Override
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IndentIssueRequesterDto iiReqDto) {
		return iid.insertRequesterData(indentEntryId, userId, siteId, iiReqDto);
	}

	@Override
	public int insertIndentIssueData(int indentEntrySeqNum, IndentIssueDto indIssDto) {		
		return iid.insertIndentIssueData(indentEntrySeqNum, indIssDto,"","","", "", "","");
	}

	@Override
	public int updateIndentAvalibility(IndentIssueDto indIssDto, String siteId) {
		return iid.updateIndentAvalibility(indIssDto, siteId);
	}

	@Override
	public void updateIndentAvalibilityWithNewIndent(IndentIssueDto indIssDto, String siteId) {
		iid.updateIndentAvalibilityWithNewIndent(indIssDto, siteId);
	}

	@Override
	public int getIndentEntrySequenceNumber() {
		return iid.getIndentEntrySequenceNumber();
	}
	@Override
	public String isWorkOrderExistsInSite(IndentIssueBean iib) {
			String result=iid.isWorkOrderExistsInSite(iib);
//			if(result.equals("")){
//				result="No work order in site";
//			}
		return result;
	}
	@Override
	public String indentIssueProcess(Model model, HttpServletRequest request, HttpSession session) {

		//String viewToBeSelected = null;
		String result = "";
		IndentIssueRequesterDto iird = null;
		String strUserId = "";
		String strSiteId = "";
		String strState = "Local";
		int indentEntrySeqNum = 0;
		IndentIssueDto issueDto = null;
		StringBuffer tblTwoData = new StringBuffer();
		Map<String, String>  viewGrnPageDataMap = new HashMap<String, String>();
		Map<Object,Object> transportChargesMap = new HashMap<Object,Object>();
		StringBuffer tblOneData = new StringBuffer();
		int count = 1;
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InIs_indIss, ");

		try {
			//ACP
			String versionNo=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_version_No");
			String reference_No=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_reference_No");
			String issue_date=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_issue_date");
			String strUserName = session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();				  
			
			viewGrnPageDataMap.put("tblVersionTwoData", versionNo+"@@"+reference_No+"@@"+issue_date+"@@"+strUserName);
			
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			logger.info("Rows To Be Processed = "+recordsCount);
			//int countOfRecords = Integer.valueOf(recordsCount);
			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			
			
			if(numOfRec != null && numOfRec.length > 0) {

				String prod = "Product";
				String subProd = "SubProduct";
				String childProd = "ChildProduct";
				//String hsnCode = "HSNCode";
				String qty = "Quantity";
				String measurements = "UnitsOfMeasurement";
				String uOrF = "UOrF";
				String remarks = "Remarks";
				String isWorkOrderExistsInSite=request.getParameter("isWorkOrderExistsInSite")==null?"":request.getParameter("isWorkOrderExistsInSite");
				String reqId = request.getParameter("ReqId");
				String reqDate = request.getParameter("ReqDate");
				String requesterName = request.getParameter("RequesterName");
				String contractorName = request.getParameter("ContractorName");
				String contractorId=request.getParameter("contractorId");
				String requesterId = request.getParameter("RequesterId");
				//for material management 
				String vendorId= request.getParameter("VendorId");
				//String note = request.getParameter("Note");
				String purpose = request.getParameter("Purpose");
				String slipNumber = request.getParameter("SlipNumber");
				//ACP
				String workOrderNo= request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo").split("_")[0];
				String typeOfWork=request.getParameter("woTypeOfWork")==null?"":request.getParameter("woTypeOfWork");
				if(typeOfWork.equals("MATERIAL")){
					workOrderNo= request.getParameter("txtWorkOrderNo")==null?"":request.getParameter("txtWorkOrderNo");
				}
				String indentType= request.getParameter("vendorissuedTypeName");
				
				String vehicleNo=request.getParameter("vehicleNo")==null?"0":request.getParameter("vehicleNo");
				String moduleName=request.getParameter("moduleName")==null?"":request.getParameter("moduleName");
				String issueType="";//checking indent type and storing in issue type
				if(indentType!=null){
					if(indentType.equals("OUTR")){
						issueType="Repair";
					}else if(indentType.equals("OUTS")){
						issueType="Scrap";
					}else if(indentType.equals("OUTT")){
						issueType="Loss-Theft";
					}
				}
				
				WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , SlipNumber:"+slipNumber);
				//used method while mateterial management
				
				strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				
				if(moduleName.equals("MaterialManagement")){
					//getting site id from request object if it is Material Management or Site Wise Material Management 
					strSiteId=request.getParameter("SiteId")==null?"":request.getParameter("SiteId");
					//getting the type of state 
					strState =  itosd.getVendorOrContractorState( strSiteId,  contractorId,moduleName,vendorId,indentType);
				}
				double strPriceListQuantity = 0.0;
				if(requesterName != null && !requesterName.equalsIgnoreCase(""))
				{
					requesterName = requesterName.toUpperCase();
				} if(StringUtils.isNotBlank(contractorName)){
					contractorName = contractorName.toUpperCase();

				}

				logger.info(reqId+" <--> "+reqDate+" <--> "+requesterName+" <--> "+requesterId+" <--> "+purpose+" <--> "+slipNumber+contractorName);


				session = request.getSession(true);
				strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				if(moduleName.equals("MaterialManagement")){
					strSiteId=request.getParameter("SiteId")==null?"":request.getParameter("SiteId");
				}
				strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();				

				String strReqMonthStart = "";
				if(reqDate.contains("-")){
					String arr[] = reqDate.split("-");
					String strMonth = arr[1];
					String strYear = arr[2];
					strReqMonthStart = "01"+"-"+strMonth+"-"+strYear;
					int intIssueCount = iid.getIssuesCount( slipNumber, strSiteId, strReqMonthStart,reqDate);
					if(intIssueCount>0){
						request.setAttribute("exceptionMsg", "Issue slip already entered");
						result = "Failed";
						transactionManager.rollback(status);
						WriteTrHistory.write("Tr_Completed");
						return result;
					}
				}else{
					request.setAttribute("exceptionMsg", "Date Format is wrong");
					result = "Failed";
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result;
				}



				/*else {
					request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Issue.");
					//viewToBeSelected = "indentIssueResponse";
					result = "Failed";
				}	*/


			//	logger.info("Flat Id = "+flatId+" and Flat Name = "+flatName);

				iird = new IndentIssueRequesterDto();

				iird.setReqId(reqId);
				iird.setReqDate(reqDate);
				iird.setRequesterName(requesterName);
				iird.setContractorName(contractorName);
				iird.setContractorId(contractorId);
				iird.setRequesterId(requesterId);
				//iird.setNote(note);
				iird.setPurpose(purpose);
				iird.setSlipNumber(slipNumber);
				iird.setWorkOrderNo(workOrderNo);
				iird.setIndentType(indentType);
				iird.setIssueType(issueType);
				iird.setVendorId(vendorId);
				iird.setVehicleNo(vehicleNo);
				//userId = 1013
				//site_id = 021
				//int indentEntrySeqNum = iid.getIndentEntrySequenceNumber();
				indentEntrySeqNum = Integer.valueOf(reqId);
				session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
				//logger.info("Indent Entry Seq. Num. = "+indentEntrySeqNum);				

				if (StringUtils.isNoneBlank(strSiteId)) {

				} else {
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result = "SessionFailed";
				}
				//int insertRequesterReult = iid.insertRequesterData(indentEntrySeqNum, strUserId, strSiteId, reqId, reqDate, requesterName, requesterId, note);
				int insertRequesterReult = iid.insertRequesterData(indentEntrySeqNum, strUserId, strSiteId, iird);
				String holdPreviousRowNum="";
				if(insertRequesterReult >= 1) {
					Double totalAmt = 0.0;Double dummyTotalAmt=0.0;
					
				//	ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
					
					for(String num : numOfRec) {
						  
						   
						num = num.trim();

						//PROD101$Cement <--> SUBPROD101$KCP <--> CHLDPROD101$TetraBag <--> tyty <--> 43 <--> 101$KG
						String product = request.getParameter(prod+num);				

						String prodsInfo[] = product.split("\\$");

						String prodId = prodsInfo[0];
						String prodName = prodsInfo[1];
						//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);					

						String subProduct = request.getParameter(subProd+num);

						String subProdsInfo[] = subProduct.split("\\$");

						String subProdId = subProdsInfo[0];
						String subProdName = subProdsInfo[1];					
						//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);

						String childProduct = request.getParameter(childProd+num);

						String childProdsInfo[] = childProduct.split("\\$");

						String childProdId = childProdsInfo[0];
						String childProdName = childProdsInfo[1];					
						//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);

						String quantity = request.getParameter(qty+num);

						String unitsOfMeasurement = request.getParameter(measurements+num);

						String measurementsInfo[] = unitsOfMeasurement.split("\\$");

						String measurementId = measurementsInfo[0];
						String measurementName = measurementsInfo[1];
						String groupId = request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
						
						String blk = request.getParameter("Block"+num);
						String wd = request.getParameter("WD"+num);

						String blockId = "";
						String blockName = "";

						if((blk != null) && (!blk.equals(""))) {
							String blockInfo[] = blk.split("\\$");				
							blockId = blockInfo[0];
							blockName = blockInfo[1];
						}
						// ravi written this code
						String wdId = "";
						String wdName = "";
						
						if(wd!=null && (!wd.equals(""))){
							String wdInfo[] = wd.split("\\$");				
							wdId = wdInfo[0];
							wdName = wdInfo[1];
						}
						
						logger.info("Block Id = "+blockId+" and Block Name = "+blockName);	

						String flr = request.getParameter("Floor"+num);

						String floorId = "";
						String floorName = "";

						if((flr != null) && (!flr.equals(""))) {					
							String floorInfo[] = flr.split("\\$");					
							floorId = floorInfo[0];
							floorName = floorInfo[1];
						}

						logger.info("Floor Id = "+floorId+" and Floor Name = "+floorName);

						String flt = request.getParameter("flatNumber"+num);

						String flatId = "";
						String flatName = "";

						if((flt != null) && (!flt.equals(""))) {					
							String flatInfo[] = flt.split("\\$");					
							flatId = flatInfo[0];
							flatName = flatInfo[1];
						}
						
						
						//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);

						//String hsnCd = request.getParameter(hsnCode+num);
						String hsnCd = "";

						String uf = request.getParameter(uOrF+num);

						String rems = request.getParameter(remarks+num);
						String isRecoverable=request.getParameter("isRecoverable"+num);
						//logger.info(product+" <--> "+subProduct+" <--> "+childProduct+" <--> "+hsnCd+" <--> "+quantity+" <--> "+unitsOfMeasurement);
						logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+" <--> "+measurementId+" <--> "+measurementName+" <--> "+hsnCd+" <--> "+uf+" <--> "+rems);

						if (StringUtils.isNotBlank(prodId) && StringUtils.isNotBlank(subProdId) &&StringUtils.isNotBlank(childProdId)) {

						} else {
							transactionManager.rollback(status);
							WriteTrHistory.write("Tr_Completed");
							return result = "Failed";
						}
						String methodName = "";
						double doubleQuantity = 0.0;
						methodName  = validateParams.getProperty(childProdId) == null ? "" : validateParams.getProperty(childProdId).toString();

						if(!methodName.equals("")) {	
							Map<String, String> values = null;

							String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();

							//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
							Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
							Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


							double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
							double doubleInputQuantity =  Double.valueOf(quantity);
							String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
							// with multiple parameters
							//methodName = "convertCHP00536";
							Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
							Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
							values=(Map<String, String>) printDogMethod.invoke(mesurment,"0",doubleActualQuantity,doubleInputQuantity,measurementName);	            
							for(Map.Entry<String, String> retVal : values.entrySet()) {
								quantity=retVal.getKey();
								//prc=retVal.getValue(); 
							}	
							//quantity =  String.valueOf(doubleQuantity);
							measurementId = strConversionMesurmentId;
							measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
						}
						//taking all the block floor data if exists
						//this data coming in array 
						String[] BlockQty=request.getParameterValues("BlockQty"+num);
						String[] BlockId=request.getParameterValues("BlockId"+num);
						String[] BlockAvailQty=request.getParameterValues("BlockAvailQty"+num);
						
						String[] FloorQTy=request.getParameterValues("FloorQty"+num);
						String[] FloorId=request.getParameterValues("FloorId"+num);
						String[] FloorAvailQty=request.getParameterValues("FloorAvailQty"+num);
						//common variable for block floor flat time
						String[] blockOrFloor=request.getParameterValues("typeOfRecords"+num);
						
						String[] FlatQTy=request.getParameterValues("FlatQty"+num);
						String[] FlatId=request.getParameterValues("FlatId"+num);
						String[] FlatAvailQty=request.getParameterValues("FlatAvailQty"+num);
						//String[] BlockId=request.getParameterValues("BlockId"+num);
						//String[] FloorId=request.getParameterValues("FloorId"+num);
						//if module type is material management giving default values so material time for loop will execute one time 
						if(moduleName.equals("MaterialManagement")){
							 BlockQty=request.getParameterValues("BlockQty"+num)==null?new String[] {"0"}:request.getParameterValues("BlockQty"+num);
							 BlockId=request.getParameterValues("BlockId"+num)==null?new String[] {"0"}:request.getParameterValues("BlockId"+num);
							 BlockAvailQty=request.getParameterValues("BlockAvailQty"+num)==null?new String[] {"0"}:request.getParameterValues("BlockAvailQty"+num);
							
							 FloorQTy=request.getParameterValues("FloorQty"+num)==null?new String[] {"0"}:request.getParameterValues("FloorQty"+num);
							 FloorId=request.getParameterValues("FloorId"+num)==null?new String[] {"0"}:request.getParameterValues("FloorId"+num);
							 FloorAvailQty=request.getParameterValues("FloorAvailQty"+num)==null?new String[] {"0"}:request.getParameterValues("FloorAvailQty"+num);
						}
						int index=0;
						 //taking length of block floor flat 
						if(BlockQty!=null||FloorAvailQty!=null||FlatQTy!=null){
							index=BlockQty==null?(FloorQTy==null?(FlatQTy==null?0:FlatQTy.length):FloorQTy.length):BlockQty.length;
							if(blockOrFloor!=null){
								if(blockOrFloor[0].equals("Block")){
									index=BlockQty.length;
								}else if(blockOrFloor[0].equals("Floor")){
									index=FloorQTy.length;
								}else if(blockOrFloor[0].equals("Flat")){
									index=FlatQTy.length;
								}
							}
						}else{
							index=1;
						}
						
						issueDto = new IndentIssueDto();

						issueDto.setProdId(prodId);
						issueDto.setProdName(prodName);
						issueDto.setSubProdId(subProdId);
						issueDto.setSubProdName(subProdName);
						issueDto.setChildProdId(childProdId);
						issueDto.setChildProdName(childProdName);
						issueDto.setQuantity(quantity);
						issueDto.setMeasurementId(measurementId);
						issueDto.setMeasurementName(measurementName);
						issueDto.setHsnCd(hsnCd);
						issueDto.setuOrF(uf);
						issueDto.setRemarks(rems);

				
						
						issueDto.setDate(reqDate);
						issueDto.setIsRecoverable(isRecoverable);
						// these are added by ravi
						issueDto.setGroupId(groupId);
						issueDto.setWorkorderNumber(workOrderNo);
						issueDto.setSiteId(strSiteId);
						issueDto.setWd(wdId);
						
					 
						IndentIssueDto cloneOfissueDto=new IndentIssueDto();
						try {
							//cloning the object so main object should not get any affect so we can use it 
							cloneOfissueDto = (IndentIssueDto) issueDto.clone();
							cloneOfissueDto.setQuantity(quantity);
						} catch (CloneNotSupportedException e1) {
							logger.info("Got exception while cloning object "+e1.getMessage());
						}
						List<IndentIssueDto> list = null;
						for (int indexOfBlockFloor = 0; indexOfBlockFloor < index; indexOfBlockFloor++) {
							if(!moduleName.equals("MaterialManagement") && workOrderNo.length()!=0){
								//based on the type of block floor name getting quantity
								if(blockOrFloor!=null){
									if(blockOrFloor[0].equals("Block")){
										quantity=BlockQty[indexOfBlockFloor];
									}else if(blockOrFloor[0].equals("Floor")){
										quantity=FloorQTy[indexOfBlockFloor];
									}else if(blockOrFloor[0].equals("Flat")){
										quantity=FlatQTy[indexOfBlockFloor];
									}
								}
								//if quantity is empty or null or 0 then continue the loop don't process to next lines
								if(quantity.equals("")||quantity.equals("0")||quantity.equals("0.0")||quantity.equals("0.00")||quantity.equals("0.00")){
									continue;
								}else{
									if(!methodName.equals("")) {	
										Map<String, String> values = null;
										String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();
										//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
										Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
										Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor

										double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
										double doubleInputQuantity =  Double.valueOf(quantity);
										String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
										// with multiple parameters
										//methodName = "convertCHP00536";
										Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
										Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
										values=(Map<String, String>) printDogMethod.invoke(mesurment,"0",doubleActualQuantity,doubleInputQuantity,measurementName);	            
										for(Map.Entry<String, String> retVal : values.entrySet()) {
											quantity=retVal.getKey();
											//prc=retVal.getValue(); 
										}	
										//quantity =  String.valueOf(doubleQuantity);
										measurementId = strConversionMesurmentId;
										measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
									}
								}
								//if block floor flat is not null then assign the values to object 
								if(BlockId!=null){issueDto.setBlockId(BlockId[indexOfBlockFloor]);}
								if(FloorId!=null){issueDto.setFloorId(FloorId[indexOfBlockFloor]);}
								if(FlatId!=null){issueDto.setFlatId(FlatId[indexOfBlockFloor]);}
								//issueDto.setFlatId(flatId);
								
								issueDto.setQuantity(quantity);
							/*============================ test the condition with work order area start==========================================*/
								if(!isWorkOrderExistsInSite.equals("LABOR")&&isWorkOrderExistsInSite.length()!=0){
									double workOrderQuantity=iid.gettingWorkorderQuantity(issueDto);
									if(workOrderQuantity<Double.valueOf(quantity)){
										result="You can not initiate Child Product \""+childProdName+"\"more than "+workOrderQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
										transactionManager.rollback(status);
										WriteTrHistory.write("Tr_Completed");
										return result;
									}
								}
								/*if(true){
									result="You can not initiate Child Product \""+childProdName+"\"more than BOQ";
									transactionManager.rollback(status);
									WriteTrHistory.write("Tr_Completed");
									return result;
								}*/
						}
						
						if(isWorkOrderExistsInSite.length()==0||isWorkOrderExistsInSite.equals("LABOR")){
							issueDto.setBlockId(blockId);
							issueDto.setFloorId(floorId);
							issueDto.setFlatId(flatId);
						}
						/*============================ test the condition with work order area end==========================================*/
						//int indentEntryDetailsSeqNum = IndentIssueDao.getIndentEntryDetailsSeqNum();
						//logger.info("Indent Entry Details Seq. Num. = "+indentEntryDetailsSeqNum);					

						//29-july-2017 written by Madhu start
						//used method while new issue and material management
					//	if(list==null){
						 list = iid.getPriceListDetails(issueDto, strSiteId);
					//	}
						//16-aug iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId);
						if (null != list && list.size() > 0) {
							for (int i = 0; i < list.size(); i++) {
								IndentIssueDto dto = list.get(i);
								Double issuQuantiy = Double.parseDouble(issueDto.getQuantity());
								Double availQty = Double.parseDouble(dto.getQuantity());
								String strHsSNCode = dto.getHsnCd();
								issueDto.setHsnCd(strHsSNCode);
								String expiryDate=dto.getExpiryDate();
								boolean expiryStatus=CheckExpirydate(reqDate,expiryDate);
								
								if (Math.abs(availQty) == Math.abs(issuQuantiy)) {
									if(expiryStatus){
										totalAmt +=  Double.valueOf(dto.getAmount())*issuQuantiy;
									
									//iid.updateIndentEntryAmountColumn(String.valueOf(Double.valueOf(dto.getAmount())*issuQuantiy), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
									int insertIndentIssueResult = iid.insertIndentIssueData(indentEntrySeqNum, issueDto, dto.getAmount(), String.valueOf(issuQuantiy), dto.getPriceId(), strUserId, strSiteId,dto.getExpiryDate());
									//iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
									int updateIndentAvalibilityResult = 0;
									if (insertIndentIssueResult >= 1) {
										//here we are updating indent availability quantity only one time
										//no need to update every time row wise
										if(!holdPreviousRowNum.equals(num)){
											holdPreviousRowNum=num;
											
											
											updateIndentAvalibilityResult = iid.updateIndentAvalibility(cloneOfissueDto, strSiteId);
											result = "Success";
											if (updateIndentAvalibilityResult == 0) {
												request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
												transactionManager.rollback(status);
												WriteTrHistory.write("Tr_Completed");
												return result = "Failed";
												/*// Making a new entry if new
												// product.
												//below line commented by Rafi
												//iid.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
												// viewToBeSelected =
												// "indentReceiveResponse";
												result = "Success";*/
											}
										} 
										
									 
										// transactionManager.commit(status);
										iid.updatePriceListDetails("0", "I", dto.getPriceId());
										//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, dto.getAmount(), issuQuantiy);
										//iid.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, dto.getAmount(), issuQuantiy);
									}
									// if this request coming from material management then only execute
									if(moduleName.equals("MaterialManagement")){
									//using the time on material management like scrap,repair
										Map<String,Double> map = getPriceAndTaxes(dto , strState, issuQuantiy);
										getOtherCharges( dto, strState,issuQuantiy,transportChargesMap,i);
										 
										if (strState.equals("Local")) {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										} else {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										}
									}
									count++;
									break;
								}else{transactionManager.rollback(status);
								request.setAttribute("childProduct",childProdName);
								return result = "Expired";}
								}
									
									else if (Math.abs(availQty) > Math.abs(issuQuantiy)) {
									if(expiryStatus){
								 
											totalAmt +=  Double.valueOf(dto.getAmount())*issuQuantiy;
								 
									//iid.updateIndentEntryAmountColumn(String.valueOf(Math.abs(Double.valueOf(dto.getAmount().replace(",","").trim())) * Math.abs(issuQuantiy)), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
									int insertIndentIssueResult = iid.insertIndentIssueData(indentEntrySeqNum, issueDto, dto.getAmount(), String.valueOf(issuQuantiy),dto.getPriceId(), strUserId, strSiteId,dto.getExpiryDate());
									//	iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
									int updateIndentAvalibilityResult =0;
									if (insertIndentIssueResult >= 1) {
										//executing this code only one time for updating quantity
										//this time we are getting more than one ,block floor data so that's why updating the total quantity not the individual quantity
										if(!holdPreviousRowNum.equals(num)){//ACP
											holdPreviousRowNum=num;
											 
											/*IndentIssueDto cloneOfissueDto=null;
											try {
												//cloning the object so main object should not get any affect so we can use it 
												cloneOfissueDto = (IndentIssueDto) issueDto.clone();
												cloneOfissueDto.setQuantity(quantity);
											} catch (CloneNotSupportedException e1) {
												logger.info("Got exception while cloning object "+e1.getMessage());
												BeanUtils.copyProperties(issueDto, cloneOfissueDto);
											}*/
											updateIndentAvalibilityResult = iid.updateIndentAvalibility(cloneOfissueDto, strSiteId);
											 
											result = "Success";
											if (updateIndentAvalibilityResult == 0) {
												request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
												transactionManager.rollback(status);
												WriteTrHistory.write("Tr_Completed");
												return result = "Failed";
												// Making a new entry if new
												// product.
												//below line commented by Rafi
												//iid.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
												// viewToBeSelected =
												// "indentReceiveResponse";
												//result = "Success";
											}
										}
										
										
										// transactionManager.commit(status);
										Double remaingQty = availQty - issuQuantiy;
										iid.updatePriceListDetails(String.valueOf(remaingQty), "A", dto.getPriceId());
										//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, dto.getAmount(), issuQuantiy);
										//iid.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, dto.getAmount(), issuQuantiy);

									}
									//got two lines of code in this code but actual row created one
									// if this request coming from material management then only execute
									if(moduleName.equals("MaterialManagement")){
										Map<String,Double> map = getPriceAndTaxes(dto , strState, issuQuantiy);
										getOtherCharges( dto, strState,issuQuantiy,transportChargesMap,i);
										 
										if (strState.equals("Local")) {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										} else {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										}
									}
									count++;
									break;

								} else{transactionManager.rollback(status);
								request.setAttribute("childProduct",childProdName);
								return result = "Expired";}
									
							}else {
								
								if(expiryStatus){
									Double remainQty = issuQuantiy;
									List<IndentIssueDto> objList = iid.getPriceListDetails(issueDto, strSiteId, "");
									if (null != objList && objList.size() > 0) {
										for (int j = 0; j < objList.size(); j++) {
											if(remainQty > 0){	

												IndentIssueDto objDTO = objList.get(j);
												Double availQty1 = Double.parseDouble(objDTO.getQuantity());
												String strExpiryDate=objDTO.getExpiryDate();
												
												//ACP
												strHsSNCode = objDTO.getHsnCd();
												issueDto.setHsnCd(strHsSNCode);
												
												boolean strExpiryStatus=CheckExpirydate(reqDate,strExpiryDate);
												if (Math.abs(remainQty) > Math.abs(availQty1)) {
													if(strExpiryStatus){
													remainQty = remainQty - availQty1;
													issueDto.setQuantity(String.valueOf(availQty1));
													int insertIndentIssueResult = iid.insertIndentIssueData(indentEntrySeqNum, issueDto, objDTO.getAmount(), String.valueOf(availQty1), objDTO.getPriceId(), strUserId, strSiteId,objDTO.getExpiryDate());
													//	iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
													int updateIndentAvalibilityResult =0;
													if (insertIndentIssueResult >= 1) {
														//if(!moduleName.equals("MaterialManagement")){
															totalAmt += Double.parseDouble(objDTO.getAmount()) * Double.parseDouble(objDTO.getQuantity()) ;
														//}
														issueDto.setQuantity(String.valueOf(availQty1));
													
														if(!holdPreviousRowNum.equals(num)){
															holdPreviousRowNum=num;
															 
															/*IndentIssueDto cloneOfissueDto=null;
															try {
																//cloning the object so main object should not get any affect so we can use it 
																cloneOfissueDto = (IndentIssueDto) issueDto.clone();
																cloneOfissueDto.setQuantity(quantity);
															} catch (CloneNotSupportedException e1) {
																logger.info("Got exception while cloning object "+e1.getMessage());
															}*/
															updateIndentAvalibilityResult = iid.updateIndentAvalibility(cloneOfissueDto, strSiteId);
														   	result = "Success";
															if (updateIndentAvalibilityResult == 0) {
																request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
																transactionManager.rollback(status);
																WriteTrHistory.write("Tr_Completed");
																return result = "Failed";
																// Making a new entry if
																// new product.
																//below line commented by Rafi
																//iid.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
																// viewToBeSelected =
																// "indentReceiveResponse";
																//result = "Success";
															}
														}
													 
														
														// transactionManager.commit(status);
														iid.updatePriceListDetails("0", "I", objDTO.getPriceId());
														//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, objDTO.getAmount(), availQty1);
														//iid.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, objDTO.getAmount(), availQty1);

													}
													// if this request coming from material management then only exceute
													if(moduleName.equals("MaterialManagement")){
														Map<String,Double> map = getPriceAndTaxes(objDTO , strState, availQty1);
														getOtherCharges( objDTO, strState,availQty1,transportChargesMap,i);
														//dummyTotalAmt = Double.valueOf(map.get("totalAmount")) ;
														//dummyTotalAmt=((double)((int)(totalAmt*100)))/100;
														//totalAmt += map.get("totalAmount") ;
														//totalAmt=((double)((int)(totalAmt*100)))/100;
														
													//	tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@0.00@@0.00@@0.00@@0.00@@0.00@@0.00@@"+""+"@@"+""+"@@0.00@@"+"note"+"@@"+"-"+"&&");
														//using local or non local we are calculating IGST and CGST
														if (strState.equals("Local")) {
															tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(availQty1)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
														} else {
															tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(availQty1)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
														}
														
														
														count++;
													}
													
													}else{transactionManager.rollback(status);
													request.setAttribute("childProduct",childProdName);
													return result = "Expired";}
													
													
												} else {
													if(strExpiryStatus){
													Double remin = remainQty;
													//if(!moduleName.equals("MaterialManagement")){
														totalAmt += Double.valueOf(objDTO.getAmount()) * remainQty ;
													//}
													
													//remainQty = Math.abs(availQty1) - Math.abs(remainQty);
													strPriceListQuantity = Math.abs(availQty1) - Math.abs(remainQty);
													//nt in use
													remainQty = Math.abs(remainQty) - Math.abs(availQty1);


													issueDto.setQuantity(String.valueOf(remin));
													//iid.updateIndentEntryAmountColumn(objDTO.getAmount(), String.valueOf(remin), String.valueOf(indentEntrySeqNum));
													int insertIndentIssueResult = iid.insertIndentIssueData(indentEntrySeqNum, issueDto, objDTO.getAmount(), String.valueOf(remin), objDTO.getPriceId(), strUserId, strSiteId,objDTO.getExpiryDate());
													//iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
													int updateIndentAvalibilityResult =0;
													if (insertIndentIssueResult >= 1) {
														issueDto.setQuantity(String.valueOf(remin));
														if(!holdPreviousRowNum.equals(num)){
															holdPreviousRowNum=num;
															 
															/*IndentIssueDto cloneOfissueDto=null;
															try {
																//cloning the object so main object should not get any affect so we can use it 
																cloneOfissueDto = (IndentIssueDto) issueDto.clone();
																cloneOfissueDto.setQuantity(quantity);
															} catch (CloneNotSupportedException e1) {
																logger.info("Got exception while cloning object "+e1.getMessage());
															}*/
															updateIndentAvalibilityResult = iid.updateIndentAvalibility(cloneOfissueDto, strSiteId);
															result = "Success";
															if (updateIndentAvalibilityResult == 0) {
																request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
																transactionManager.rollback(status);
																WriteTrHistory.write("Tr_Completed");
																return result = "Failed";
																// Making a new entry if
																// new product.
																//below line commented by Rafi
																//iid.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
																//result = "Success";
															}
														}
														
														 
														

														if(strPriceListQuantity == 0.0){
															iid.updatePriceListDetails(String.valueOf(strPriceListQuantity), "I", objDTO.getPriceId());
														}else{
															iid.updatePriceListDetails(String.valueOf(strPriceListQuantity), "A", objDTO.getPriceId());
														}
														//	iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, objDTO.getAmount(), remin);
														//iid.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, objDTO.getAmount(), remin);
													}
													if(moduleName.equals("MaterialManagement")){
														Map<String,Double> map = getPriceAndTaxes(objDTO , strState, remin);
														getOtherCharges( objDTO, strState,remin,transportChargesMap,i);
														//dummyTotalAmt = Double.valueOf(map.get("totalAmount")) ;
														//dummyTotalAmt=((double)((int)(totalAmt*100)))/100;
														
														//totalAmt += map.get("totalAmount") ;
														//totalAmt=((double)((int)(totalAmt*100)))/100;
														
														if (strState.equals("Local")) {
															tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(remin)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
														} else {
															tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(remin)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
														}
													}
													count++;
												
												}else{transactionManager.rollback(status);
												request.setAttribute("childProduct",childProdName);
												return result = "Expired";}
												}

											}else{
												break;
											}
										}
										//iid.updateIndentEntryAmountColumn(String.valueOf(totalAmt), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
									}
									}else{transactionManager.rollback(status);
									request.setAttribute("childProduct",childProdName);
									return result = "Expired";}
								}

							}
						} else {
							transactionManager.rollback(status);
							WriteTrHistory.write("Tr_Completed");
							return result = "No Stock";
						}
					}//multiple block floor condition

						//29-july-2017 written by Madhu end
						/*29-july-2017 written by Madhu start int insertIndentIssueResult = iid.insertIndentIssueData(indentEntrySeqNum, issueDto,"","");
						if(insertIndentIssueResult >= 1) {
							int updateIndentAvalibilityResult = iid.updateIndentAvalibility(issueDto, strSiteId);
							//viewToBeSelected = "indentReceiveResponse";
							result = "Success";
							if(updateIndentAvalibilityResult == 0) {
								//Making a new entry if new product.
								iid.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
								//viewToBeSelected = "indentReceiveResponse";
								result = "Success";
							}
							//transactionManager.commit(status);
						} 29-july-2017 written by Madhu end */
					}

					iid.updateIndentEntryAmountColumn(String.valueOf(totalAmt), "", String.valueOf(indentEntrySeqNum));
					
					if(moduleName.equals("MaterialManagement")){	
							//ACP
							String strFromSiteName = "";
							String strFromSiteAddress = "";
							String strFromSitGSTIN = "";
							String strToSiteName = "";
							String strToSiteAddress = "";
							String strToSitGSTIN = "";
		
		//loading the site address
							List<Map<String, Object>>  dbClosingBalancesList = itosd.getSiteDetails(strSiteId);
							if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
								for (Map<String, Object> prods : dbClosingBalancesList) {
									strFromSiteName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
									strFromSiteAddress =prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
									strFromSitGSTIN = prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString();
								}
							}
							//ACP
							if(moduleName.equals("MaterialManagement")){
								if(indentType.equals("OUTR")){
									//loading contractor details for showing in page
									List<String> contractorDetails=workControllerDao.getContractorInfo(contractorName,false,"getContractorId");
									String contractorData[]=contractorDetails.get(0).split("@@");
								
										strToSiteName = contractorName;
										strToSiteAddress =contractorData[2];
										strToSitGSTIN = contractorData[7];
		
								}else if(indentType.equals("OUTS")){
									//loading vendor details for showing in page
									dbClosingBalancesList = itosd.getSiteDetails(vendorId);
									if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
										for (Map<String, Object> prods : dbClosingBalancesList) {
		
											strToSiteName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
											strToSiteAddress =prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
											strToSitGSTIN = prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString();
		
										}}					
								}
							}
							
							totalAmt=((double)((int)(totalAmt*100)))/100;
		
							double transportAmount = Double.valueOf(transportChargesMap.get("OtherOrTransportChargeAfterTax").toString());
							transportAmount=((double)((int)(transportAmount*100)))/100;
							
							totalAmt = totalAmt+transportAmount;
						//	itosd.updateIndentEntryAmountColumn(String.valueOf(totalAmt), "", String.valueOf(indentEntrySeqNum));
							int val = (int) Math.ceil(totalAmt);
							double roundoff=Math.ceil(totalAmt)-totalAmt;
							double grandtotal=Math.ceil(totalAmt);
							String strtotal=String.valueOf(totalAmt);
							String strroundoff=String.format("%.2f",roundoff);
							String strgrandtotal=String.valueOf((int)grandtotal);
		
		
							tblOneData.append(strFromSitGSTIN+"@@"+reqDate+"@@"+indentEntrySeqNum+"@@"+reqDate+"@@"+"Sumadhura"+"@@"+
									strgrandtotal+"@@"+new NumberToWord().convertNumberToWords(Integer.parseInt(strgrandtotal))+" Rupees Only."+
									"@@"+strFromSiteAddress+"@@"+indentEntrySeqNum+"@@"+"poNo"+"@@"+"dcNo"+"@@"+
									strFromSiteName+"@@"+"poDate"+"@@"+"eWayBillNo"+"@@"+"vehileNo"+"@@"+"transporterName"+
									"@@"+"doubleSumOfOtherCharges"+"@@"+strToSiteName+"@@"+strToSiteAddress+"@@"+strToSitGSTIN+"@@"+strtotal+"@@"+strroundoff+"@@"+vehicleNo+"@@"+strState);
		
							viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
							//start 03-sept
							//String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
							//viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
							//end 03-sept
		
							String fnlStr = tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
							
							viewGrnPageDataMap.put("tblTwoData", fnlStr);
							request.setAttribute("tblThreeData", transportAmount);
							request.setAttribute("viewGrnPageData", viewGrnPageDataMap);
					}//ending id module name condition
				}else {
					request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Issue.");
					//viewToBeSelected = "indentIssueResponse";
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result = "Failed";
				}	
				
				

				
			} else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				//viewToBeSelected = "indentIssueResponse";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return result = "Failed";
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		}
		catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Issue.");
			//viewToBeSelected = "indentIssueResponse";
			result = "Failed";
			AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
			auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
			auditBean.setLoginId(strUserId);
			auditBean.setOperationName("New Issue");
			auditBean.setStatus("error");
			auditBean.setSiteId(strSiteId);
			new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
			e.printStackTrace();
		}
		return result;
	}
	private Map<Object, Object> getOtherCharges(IndentIssueDto dto, String strState, Double issuQuantiy, Map<Object,Object> map, int i) {


		double receivedQuantity = Double.valueOf(dto.getReceivedQuantity());
		double doubleOtherOrTransportCharge = Double.valueOf(dto.getAmountAfterTaxotherOrTransportCharges());
		double tax = Double.valueOf((dto.getTaxotherOrTransportCharges()==null||dto.getTaxotherOrTransportCharges().equals(""))?"0":dto.getTaxotherOrTransportCharges());
		double pricePerUnit = 0.0;

		//double otherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getOtherOrTransportCharges());
		//double taxOnOtherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getTaxotherOrTransportCharges());
		/*double CGSTTax = 0;
		double SGSTTax = 0;
		double IGSTTax = 0;
		double CGSTAmount = 0;
		double SGSTAmount = 0;
		double IGSTAmount = 0;*/
		double totalAmount = 0;
		double otherCharges = 0;
		double prevOtherCharge = 0;
		double prevTotalAmount = 0;
		

		pricePerUnit = doubleOtherOrTransportCharge/receivedQuantity;
		otherCharges = issuQuantiy*pricePerUnit;
		
		prevOtherCharge = Double.valueOf(map.get("OtherOrTransportChargeAfterTax") == null ? "0.0" : map.get("OtherOrTransportChargeAfterTax").toString());
		prevTotalAmount = Double.valueOf(map.get("totalAmount") == null ? "0.0" : map.get("totalAmount").toString());
		otherCharges = otherCharges+prevOtherCharge;
		
		
		
		map.put("OtherOrTransportChargeAfterTax", otherCharges);
		//map.put("CGSTTax", CGSTTax);
		//map.put("CGSTAmount", CGSTAmount);
		//map.put("SGSTTax", SGSTTax);
		//map.put("SGSTAmount", SGSTAmount);
		//map.put("IGSTTax", IGSTTax);
		//map.put("IGSTAmount", IGSTAmount);
		map.put("totalAmount", totalAmount);
		
		
		//String strTableThreeData = doublePrice+"@@"+CGSTTax+"@@"+CGSTAmount+"@@"+SGSTTax+"@@"+SGSTAmount+"@@"+IGSTTax+"@@"+IGSTAmount+"@@"+totalAmount;


		//map.put("strTableThreeData"+intCount, strTableThreeData);


		return map;	
		
	}

	private Map<String, Double> getPriceAndTaxes(IndentIssueDto meterialManagementDto, String strState, Double issuQuantiy) {
		Map<String,Double> map = new HashMap<String,Double>();
		//double quantity = objIssueToOtherSiteDto.getQuantity1();
		double pricePerUnitBeforeTax = Double.valueOf(meterialManagementDto.getAmountPerUnitBeforeTax());
		double tax = Double.valueOf(meterialManagementDto.getTax());
		//double otherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getOtherOrTransportCharges());
		//double taxOnOtherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getTaxotherOrTransportCharges());
		double CGSTTax = 0;
		double SGSTTax = 0;
		double IGSTTax = 0;
		double CGSTAmount = 0;
		double SGSTAmount = 0;
		double IGSTAmount = 0;
		double totalAmount = 0;
		double doublePrice = 0;

		doublePrice = issuQuantiy*pricePerUnitBeforeTax;
		doublePrice=((double)((int)(doublePrice*100)))/100;
		if(strState.equals("Local")){
			CGSTTax = tax/2;

			CGSTAmount = doublePrice*CGSTTax/100;

			SGSTTax = tax/2;
			SGSTAmount = doublePrice*SGSTTax/100;
		}else{
			IGSTTax = tax;
			IGSTAmount = doublePrice*IGSTTax/100;
		}
		totalAmount = doublePrice+CGSTAmount+SGSTAmount+IGSTAmount;
		totalAmount=((double)((int)(totalAmount*100)))/100;
		
		map.put("pricePerUnitBeforeTax", pricePerUnitBeforeTax);
		map.put("totalAmountBeforetax", doublePrice);
		map.put("CGSTTax", CGSTTax);
		map.put("CGSTAmount", CGSTAmount);
		map.put("SGSTTax", SGSTTax);
		map.put("SGSTAmount", SGSTAmount);
		map.put("IGSTTax", IGSTTax);
		map.put("IGSTAmount", IGSTAmount);
		map.put("totalAmount", totalAmount);

		return map;	

	}

	public int getIssueCount1(HttpServletRequest request ){


		String strSlipNumber=request.getParameter("SlipNumber");
		HttpSession session = request.getSession(true);
		String strSiteId = (String) session.getAttribute("SiteId");
		String strReqDate=request.getParameter("ReqDate");
		int indent_entry_id = iid.getIssuesCount1(  strSlipNumber, strSiteId, strReqDate);
		
		logger.info("slip nu "+strSlipNumber+" indent entry id "+indent_entry_id);
		return indent_entry_id;
	}
	public int getIssueCount(String strSlipNumber,String strSiteId,String strMonthStartString,String strReqDate){


		int intIssueCount = iid.getIssuesCount(  strSlipNumber, strSiteId, strMonthStartString,strReqDate);
		return intIssueCount;
	}

	@Override
	public String loadProductAvailability(String prodId, String subProductId, String childProdId, String measurementId,String requesteddate,HttpServletRequest request, HttpSession session,String groupid,String isReceived) {

		String isMaterialAdjustment=request.getParameter("isMaterialAdjustment")==null?"":request.getParameter("isMaterialAdjustment");
		String isMaterialManagement=request.getParameter("isMaterialManagement")==null?"":request.getParameter("isMaterialManagement");
		String site_id="";
		if(isMaterialAdjustment.length()!=0){
			site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
		}else if(isMaterialManagement.length()!=0){
			site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
		}else{
			site_id=String.valueOf(request.getSession(true).getAttribute("SiteId").toString());
		}
		
		
		return iid.getProductAvailability(prodId, subProductId, childProdId, measurementId,requesteddate,site_id,groupid,isReceived);
	}

	
	public String checkExpiryDate(String prodId, String subProductId, String childProdId, String siteId,String requesteddate,double issuedQuan,HttpServletRequest request, HttpSession session) {
		
		String result="Success";
		//double remainQuantity=0.0;
		IssueToOtherSiteDto issueDto = new IssueToOtherSiteDto();
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		int count=0;
		try{
		issueDto.setProdId1(prodId);
		issueDto.setSubProdId1(subProductId);
		issueDto.setChildProdId1(childProdId);
		issueDto.setDate(requesteddate);
		
		List<IssueToOtherSiteDto> list=itosd.getPriceListDetails(issueDto,strSiteId);
		if (null != list && list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {
				IssueToOtherSiteDto dto = list.get(i);
				Double availQty = Double.valueOf(dto.getQuantity1());
				String expiryDate=dto.getExpiryDate();
				boolean status=CheckExpirydate(requesteddate,expiryDate);// to check the product is expire or not
				
				if (Math.abs(availQty) == Math.abs(issuedQuan)) {
					if(status){
					count++;
					break;
					}else{
						return result = "Failed";
					}
					
				}else if (Math.abs(availQty) > Math.abs(issuedQuan)) {
					if(status){
						count++;
						break;
						}else{return result = "Failed";}
						
				}else {
					if(status){ // to check the product is expire or not
					count++;
					issuedQuan=issuedQuan-availQty; // decrease the quantity
					continue;
					}else{
						return result = "Failed";
					}
					
					
					
				}
			}
			
			
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	
	
	@Override
	public Map<String, String> loadBlockDetails(String strSiteId) {		
		return iid.loadBlockDetails(strSiteId);
	}

	@Override
	public String getProjectName(HttpSession session) {		
		return iid.getProjectName(session);
	}

	@Override
	public String getFloorDetails(String blockId) {
		return iid.getFloorDetails(blockId);
	}

	@Override
	public String getFlatDetails(String floorId) {
		return iid.getFlatDetails(floorId);
	}

	@Override
	public String getWdDetails(String workOrderId, String siteId) {
		return iid.getWdDetails(workOrderId,siteId);
	}
	
	@Override
	public String getWDblockDetails(HttpServletRequest request) {
		String wdId,materialGroupId,workOrderNo,siteId;
		wdId=request.getParameter("wdId")==null?"":request.getParameter("wdId");
		materialGroupId=request.getParameter("materialGroupId")==null?"":request.getParameter("materialGroupId");
		workOrderNo=request.getParameter("workOrderNo")==null?"":request.getParameter("workOrderNo");
		siteId=request.getParameter("SiteId")==null?"":request.getParameter("SiteId");
		return iid.getWDblockDetails(wdId,materialGroupId,workOrderNo,siteId);
	}
	@Override
	public String getFloorDataDetails(String wdId) {
		return iid.getFloorDataDetails(wdId);
	}
	@Override
	public String getFlatDataDetails(String wdId) {
		return iid.getFlatDataDetails(wdId);
	}
	@Override
	public void updateIssueDetailsIntoSumadhuraCloseBalance(
			IndentReceiveDto irdto, String siteId) {

	}

	@Override
	public void updateIssueDetailsSumadhuClosingBalByProduct(IndentIssueDto issueDto, String strSiteId, String priceId, Double IssueQty) {

	}
	
	public static void main(String[] args){
		double d = 0.0;
		
		if(d == 0){
			System.out.println("ok");
		}
	}

	@Override
	public String getContractorInfo(String contractorName) {
		// TODO Auto-generated method stub
		return iid.getContractorInfo(contractorName);
	}
	@Override
	public String getEmployerInfo(String employeeName) {
		// TODO Auto-generated method stub
		return iid.getEmployerInfo(employeeName);
	}
	public String getEmployerid(String employeeName) {
		// TODO Auto-generated method stub
	//	System.out.println("in service "+employeeName);
		return iid.getEmployerid(employeeName);
	}
	
	public String getEmployerName(String employeeid) {
		// TODO Auto-generated method stub
	//	System.out.println("in service id data "+employeeid);
		return iid.getEmployerName(employeeid);
	}
	
	public boolean CheckExpirydate(String requesteddate,String expiryDate){
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		DateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");
		//Date anotherDate = outputFormat.parse(requesteddate);
		boolean status=true;
		Date afterexpiryDate  =null;
		try{
			Date afterReqDate  = format.parse(requesteddate);
			requesteddate=outputFormat.format(afterReqDate);
			
		if(!expiryDate.equalsIgnoreCase("N/A") && !expiryDate.equalsIgnoreCase("NA")){
			afterexpiryDate  = format.parse(expiryDate);
			expiryDate=outputFormat.format(afterexpiryDate);
			}
		if(expiryDate.equalsIgnoreCase("N/A")){
			status=true;
		}else if(expiryDate.equals("-")){
			status=true;
		}else if(expiryDate.equalsIgnoreCase("NA")){
			status=true;
		}
		else if((afterexpiryDate).compareTo((afterReqDate)) >= 0){
			status=true;
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	
	
}
