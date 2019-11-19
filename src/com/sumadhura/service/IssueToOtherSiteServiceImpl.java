package com.sumadhura.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.IssueToOtherSiteInwardBean;
import com.sumadhura.dto.IssueToOtherSiteDto;
import com.sumadhura.transdao.IndentReceiveDaoImpl;
import com.sumadhura.transdao.IssueToOtherSiteDao;
import com.sumadhura.util.MobileSMS;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.util.ValidateParams;

@Service("itosc")
public class IssueToOtherSiteServiceImpl extends UIProperties implements IssueToOtherSiteService {
	
@Autowired
private IssueToOtherSiteDao itosd;

@Autowired
PlatformTransactionManager transactionManager;


@Autowired
IndentCreationServiceImpl objIndentCreationServiceImpl;

@Autowired
IndentReceiveDaoImpl ird;

@Autowired
IndentIssueServiceImpl iisi;

@Autowired
MobileSMS mobileSMS;	

@Override
public Map<String, String> issueToOtherSiteProducts(String siteId) {
	return itosd.issueToOtherSiteProducts(siteId);
}

@Override
public String issueToOtherSiteSubProducts(String prodId) {
	return itosd.issueToOtherSiteSubProducts(prodId);
}

@Override
public String issueToOtherSiteChildProducts(String subProductId) {
	return itosd.issueToOtherSiteChildProducts(subProductId);
}

@Override
public String issueToOtherSiteMeasurements(String childProdId) {
	return itosd.issueToOtherSiteMeasurements(childProdId);
}

@Override
public int insertRequesterData(int indentEntryId, String userId, String siteId, IssueToOtherSiteDto iiReqDto,String strToSite) {
	return itosd.insertRequesterData(indentEntryId, userId, siteId, iiReqDto,strToSite);
}

@Override
public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto indIssDto) {
	return itosd.insertIndentIssueData(indentEntrySeqNum, indIssDto);
}

@Override
public int updateIndentAvalibility(IssueToOtherSiteDto indIssDto, String siteId) {
	return itosd.updateIndentAvalibility(indIssDto, siteId);
}

@Override
public void updateIndentAvalibilityWithNewIndent(IssueToOtherSiteDto indIssDto, String siteId) {
	itosd.updateIndentAvalibilityWithNewIndent(indIssDto, siteId);
}

@Override
public int getIndentEntrySequenceNumber() {
	return itosd.getIndentEntrySequenceNumber();
}

@Override
public String getIssueToOtherSiteProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, HttpServletRequest request, HttpSession session) {
	return itosd.getIssueToOtherSiteProductAvailability(prodId, subProductId, childProdId, measurementId, String.valueOf(request.getSession(true).getAttribute("SiteId").toString()));
}

@Override
public String doIndentIssueToOtherSite(Model model, HttpServletRequest request, HttpSession session) {

	//String viewToBeSelected = null;
	String result = "";
	String grdtotal="";
	IssueToOtherSiteDto iird = null;
	IssueToOtherSiteDto issueDto = null;
	StringBuffer tblOneData = new StringBuffer();

	TransactionStatus status = null;
	String strRequestFrom  = request.getAttribute("RequestFrom") == null ? "" : request.getAttribute("RequestFrom").toString(); 
	if(strRequestFrom.equals("")){
		TransactionDefinition def = new DefaultTransactionDefinition();
		status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in IsOS_dIIOth, ");
	}

	Map<String, String> viewGrnPageDataMap = null;
	StringBuffer tblCharges = new StringBuffer();
	StringBuffer tblTwoData = new StringBuffer();
	try {
		viewGrnPageDataMap = new HashMap<String, String>();
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
		String toSite = request.getParameter("Site");
		logger.info("Rows To Be Processed = "+recordsCount);
		//int countOfRecords = Integer.valueOf(recordsCount);
		String numOfRec[] = null;
		if((recordsCount != null) && (!recordsCount.equals(""))) {
			numOfRec = recordsCount.split("\\|");
		}
		String strState = "";
		String reqDate = "";
		String strPrice = "";
		int count = 1;
		//ACP (Anikth)


		String versionNo=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_version_No");
		String reference_No=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_reference_No");
		String issue_date=ValidateParams.validateParams.getProperty("ISSUETOOTHERSITE_PRINT_issue_date");
	
		String strUserName = session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();				  
		
		viewGrnPageDataMap.put("tblVersionTwoData", versionNo+"@@"+reference_No+"@@"+issue_date+"@@"+strUserName);
		
		Map<Object,Object> transportChargesMap = new HashMap<Object,Object>();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		DateFormat outputFormat = new SimpleDateFormat("dd-MM-yy");
		if(numOfRec != null && numOfRec.length > 0) {

			String prod = "Product";
			String subProd = "SubProduct";
			String childProd = "ChildProduct";
			String qty = "Quantity";
			String measurements = "UnitsOfMeasurement";
			String uOrF = "UOrF";
			String remarks = "Remarks";

			String reqId = request.getParameter("ReqId");
			reqDate = request.getParameter("ReqDate");
			//String requesterName = request.getParameter("RequesterName");
			//String requesterId = request.getParameter("RequesterId");
			String purpose = request.getParameter("Purpose");
			String Returnable=request.getParameter("returnable");
			String NonReturnable=request.getParameter("nonReturnable");
			String issueType=request.getParameter("issueType");
			String vehicleNo=request.getParameter("vehicleNo");
			String strIndentNo = request.getParameter("indentNumber");
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , RequestId:"+reqId);

			/*if(issueType != null){
				issueType="Returanble";
			}else{
				issueType="NonReturable";
			}*/



			//	String toSite = request.getParameter("Site");
			String siteInfo[] = toSite.split("\\$");	
			toSite = siteInfo[0];

			String requesterId = siteInfo[0];
			String requesterName = siteInfo[1];
			//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);	

			//String slipNumber = request.getParameter("SlipNumber");

			logger.info(reqId+" <--> "+reqDate+" <--> "+requesterName+" <--> "+requesterId+" <--> "+purpose);

			iird = new IssueToOtherSiteDto();

			iird.setReqId(reqId);
			iird.setReqDate(reqDate);
			iird.setRequesterName(requesterName);
			iird.setRequesterId(requesterId);
			iird.setVehicleNo(vehicleNo);
			iird.setIndentNumber(strIndentNo);

			//iird.setNote(note);
			iird.setPurpose(purpose);
			//			iird.setReturnable(Returnable);
			//			iird.setNonReturnable(NonReturnable);
			iird.setIssueType(issueType);
			//iird.setSlipNumber(slipNumber);
			//userId = 1013
			//site_id = 021
			//int indentEntrySeqNum = itosd.getIndentEntrySequenceNumber();
			int indentEntrySeqNum = Integer.valueOf(reqId);
			session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
			//logger.info("Indent Entry Seq. Num. = "+indentEntrySeqNum);				

			session = request.getSession(true);

			String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();				




			//int insertRequesterReult = itosd.insertRequesterData(indentEntrySeqNum, strUserId, strSiteId, reqId, reqDate, requesterName, requesterId, note);

			
			iird.setVehicleNo(vehicleNo);
			iird.setReference_No(reference_No);
			iird.setIssue_date(issue_date);
			iird.setPrepared_by(strUserName);
			strState =  itosd.getVendorState( strSiteId,  toSite);

			int insertRequesterReult = itosd.insertRequesterData(indentEntrySeqNum, strUserId, strSiteId, iird,strSiteId);

			if(insertRequesterReult >= 1) {
				double  totalAmt = 0.0;
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
					// is it possible to get 0 from indent create approval module
					if(!quantity.equals("0")  && !quantity.equals("") && quantity != null){


						String unitsOfMeasurement = request.getParameter(measurements+num);

						String measurementsInfo[] = unitsOfMeasurement.split("\\$");

						String measurementId = measurementsInfo[0];
						String measurementName = measurementsInfo[1];
						//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);

						//String hsnCd = request.getParameter(hsnCode+num);
						//String hsnCd = "";

						String uf = request.getParameter(uOrF+num);

						String rems = request.getParameter(remarks+num);

						//logger.info(product+" <--> "+subProduct+" <--> "+childProduct+" <--> "+hsnCd+" <--> "+quantity+" <--> "+unitsOfMeasurement);
						logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+" <--> "+measurementId+" <--> "+measurementName+" <--> "+uf+" <--> "+rems);


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
							values=(Map<String, String>)  printDogMethod.invoke(mesurment,"0",doubleActualQuantity,doubleInputQuantity,measurementName);	            
							for(Map.Entry<String, String> retVal : values.entrySet()) {
								quantity=retVal.getKey();
								//prc=retVal.getValue(); 
							}
							//quantity =  String.valueOf(doubleQuantity);
							measurementId = strConversionMesurmentId;
							measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
						}

						issueDto = new IssueToOtherSiteDto();

						issueDto.setProdId1(prodId);
						issueDto.setProdName1(prodName);
						issueDto.setSubProdId1(subProdId);
						issueDto.setSubProdName1(subProdName);
						issueDto.setChildProdId1(childProdId);
						issueDto.setChildProdName1(childProdName);
						issueDto.setQuantity1(Double.valueOf(quantity));
						issueDto.setMeasurementId1(measurementId);
						issueDto.setMeasurementName1(measurementName);
						issueDto.setUOrF1(uf);
						issueDto.setRemarks1(rems);
						//issueDto.setHsnCd(hsnCd);

						issueDto.setUserId(strUserId);
						issueDto.setFromSite(strSiteId);
						issueDto.setToSite(toSite);
						issueDto.setDate(reqDate);
						issueDto.setVehicleNo(vehicleNo);
						//issueDto.setBlockId(blockId);
						//issueDto.setFloorId(floorId);
						//issueDto.setFlatId(flatId);


						//int indentEntryDetailsSeqNum = IndentIssueDao.getIndentEntryDetailsSeqNum();
						//logger.info("Indent Entry Details Seq. Num. = "+indentEntryDetailsSeqNum);					
						
						List<IssueToOtherSiteDto> list = itosd.getPriceListDetails(issueDto, strSiteId);
						//16-aug iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId);
						if (null != list && list.size() > 0) {

							for (int i = 0; i < list.size(); i++) {
								IssueToOtherSiteDto dto = list.get(i);
								double issuQuantiy = issueDto.getQuantity1();
								Double availQty = Double.valueOf(dto.getQuantity1());
								
								String strHsSNCode = dto.getStrHSNCode1();
								String expiryDate=dto.getExpiryDate();
								issueDto.setStrHSNCode1(strHsSNCode);
								boolean expiryStatus=iisi.CheckExpirydate(reqDate,expiryDate);
								
								/*if(!expiryDate.equalsIgnoreCase("N/A")){
								Date afterexpiryDate  = format.parse(expiryDate);
								expiryDate=outputFormat.format(afterexpiryDate);
								}*/
								if (Math.abs(availQty) == Math.abs(issuQuantiy) ) {
									if(expiryStatus){

										//itosd.updateIndentEntryAmountColumn(String.valueOf(Double.valueOf(dto.getPrice1())*issuQuantiy), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
										int insertIndentIssueResult = itosd.insertIndentIssueData(indentEntrySeqNum, issueDto, dto.getPrice1(), String.valueOf(issuQuantiy), dto.getStrPriceId(), strUserId, strSiteId,expiryDate);

										itosd.insertIntremidiatoryTabbleData( insertIndentIssueResult,  issueDto,  dto.getPrice1(),  quantity,  dto.getStrPriceId(),indentEntrySeqNum);


									//iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
									if (insertIndentIssueResult >= 1) {
										int updateIndentAvalibilityResult = itosd.updateIndentAvalibility(issueDto, strSiteId);
										// viewToBeSelected =
										// "indentReceiveResponse";
										result = "Success";
										if (updateIndentAvalibilityResult == 0) {
											request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
											transactionManager.rollback(status);
											WriteTrHistory.write("Tr_Completed");
											return result = "Failed";
											// Making a new entry if new
											// product.
											//below line commented by Rafi
											//itosd.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
											// viewToBeSelected =
											// "indentReceiveResponse";
											//result = "Success";
										}
										// transactionManager.commit(status);
										itosd.updatePriceListDetails("0", "I", dto.getStrPriceId());
										//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, dto.getAmount(), issuQuantiy);
										itosd.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, dto.getPrice1(), issuQuantiy);
									}

										//double doubleTotalAmountDc = issuQuantiy*Double.valueOf(dto.getPrice1());
										//doubleTotalAmountDc=((double)((int)(doubleTotalAmountDc*100)))/100;
										//NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
										//grdtotal=numberFormat.format(doubleTotalAmountDc);

										Map<String,Double> map = getPriceAndTaxes(dto , strState, issuQuantiy);
										getOtherCharges( dto, strState,issuQuantiy,transportChargesMap,i);
										totalAmt += Double.valueOf(map.get("totalAmount")) ;
										totalAmt=((double)((int)(totalAmt*100)))/100;

										if (strState.equals("Local")) {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										} else {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
											//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
										}
										count++;
										break;
									}else{ 
										transactionManager.rollback(status);
										request.setAttribute("childProduct",childProdName);
										return result = "Expired";
									}
								} else if (Math.abs(availQty) > Math.abs(issuQuantiy)) {
									if(expiryStatus){
										//itosd.updateIndentEntryAmountColumn(String.valueOf(Math.abs(Double.valueOf(dto.getPrice1().replace(",","").trim())) * Math.abs(issuQuantiy)), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
										int insertIndentIssueResult = itosd.insertIndentIssueData(indentEntrySeqNum, issueDto, dto.getPrice1(), String.valueOf(issuQuantiy), dto.getStrPriceId(), strUserId, strSiteId,expiryDate);

									itosd.insertIntremidiatoryTabbleData( insertIndentIssueResult,  issueDto,  dto.getPrice1(),  String.valueOf(issuQuantiy),  dto.getStrPriceId(),indentEntrySeqNum);
									//	iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
									if (insertIndentIssueResult >= 1) {
										int updateIndentAvalibilityResult = itosd.updateIndentAvalibility(issueDto, strSiteId);
										// viewToBeSelected =
										// "indentReceiveResponse";
										result = "Success";
										if (updateIndentAvalibilityResult == 0) {
											request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
											transactionManager.rollback(status);
											WriteTrHistory.write("Tr_Completed");
											return result = "Failed";
											// Making a new entry if new
											// product.
											//below line commented by Rafi
											//itosd.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
											// viewToBeSelected =
											// "indentReceiveResponse";
											//result = "Success";
										}
										// transactionManager.commit(status);
										Double remaingQty = availQty - issuQuantiy;
										BigDecimal bigDecimal = new BigDecimal(remaingQty);
										itosd.updatePriceListDetails(String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING)), "A", dto.getStrPriceId());
										//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, dto.getAmount(), issuQuantiy);
										itosd.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, dto.getPrice1(), issuQuantiy);

										}
										//double doubleTotalAmountDc = issuQuantiy*Double.valueOf(dto.getPrice1());
										//doubleTotalAmountDc=((double)((int)(doubleTotalAmountDc*100)))/100;


										Map<String,Double> map = getPriceAndTaxes(dto , strState, issuQuantiy);
										getOtherCharges( dto, strState,issuQuantiy,transportChargesMap,i);

										if (strState.equals("Local")) {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
										} else {
											tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(issuQuantiy)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
											//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
										}
										count++;
										totalAmt += Double.valueOf(map.get("totalAmount"))  ;
										totalAmt=((double)((int)(totalAmt*100)))/100;


										break;
									}else{
										transactionManager.rollback(status);
										request.setAttribute("childProduct",childProdName);
										return result = "Expired";}	

								} else {
									if(expiryStatus){
										//noOfTimes++;
										double remainQty = issuQuantiy;


										List<IssueToOtherSiteDto> objList = itosd.getPriceListDetails(issueDto, strSiteId, "");
										if (null != objList && objList.size() > 0) {


											for (int j = 0; j < objList.size(); j++) {
												if(remainQty > 0){	

												IssueToOtherSiteDto objDTO = objList.get(j);
												double availQty1 = objDTO.getQuantity1();
												String strExpiryDate=objDTO.getExpiryDate();
												boolean expiryStatus1=iisi.CheckExpirydate(reqDate,strExpiryDate);
												if (Math.abs(remainQty) > Math.abs(availQty1)) {
													
													remainQty = remainQty - availQty1;
													issueDto.setQuantity1(availQty1);
													int insertIndentIssueResult = itosd.insertIndentIssueData(indentEntrySeqNum, issueDto, objDTO.getPrice1(), String.valueOf(availQty1), dto.getStrPriceId(), strUserId, strSiteId,expiryDate);
													if(expiryStatus1){
													itosd.insertIntremidiatoryTabbleData( insertIndentIssueResult,  issueDto,  dto.getPrice1(),  String.valueOf(availQty1),  dto.getStrPriceId(),indentEntrySeqNum);
													//	iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
													if (insertIndentIssueResult >= 1) {
														issueDto.setQuantity1(availQty1);
														int updateIndentAvalibilityResult = itosd.updateIndentAvalibility(issueDto, strSiteId);
														// viewToBeSelected =
														// "indentReceiveResponse";
														result = "Success";
														if (updateIndentAvalibilityResult == 0) {
															request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
															transactionManager.rollback(status);
															WriteTrHistory.write("Tr_Completed");
															return result = "Failed";
															// Making a new entry if
															// new product.
															//below line commented by Rafi
															//itosd.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
															// viewToBeSelected =
															// "indentReceiveResponse";
															//result = "Success";
														}
														// transactionManager.commit(status);
														itosd.updatePriceListDetails("0", "I", objDTO.getStrPriceId());
														//iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, objDTO.getAmount(), availQty1);
														itosd.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, objDTO.getPrice1(), availQty1);

															}
															double doubleTotalAmountDc = availQty1*Double.valueOf(dto.getPrice1());
															doubleTotalAmountDc=((double)((int)(doubleTotalAmountDc*100)))/100;
															Map<String,Double> map = getPriceAndTaxes(dto , strState, availQty1);
															getOtherCharges( dto, strState,availQty1,transportChargesMap,i);
															totalAmt += map.get("totalAmount") ;
															totalAmt=((double)((int)(totalAmt*100)))/100;

															if (strState.equals("Local")) {
																tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(availQty1)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
															} else {
																tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(availQty1)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
																//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
															}

															count++;

														}else{
															transactionManager.rollback(status);
															request.setAttribute("childProduct",childProdName);
															return result = "Expired";
														}
													} else {
														if(expiryStatus1){
															double remin = remainQty;
															double DBAvailQuantity = 0;
															//remainQty = Math.abs(availQty1) - Math.abs(remainQty);
															remainQty = Math.abs(remainQty) - Math.abs(availQty1);

															if(remainQty <0){
																DBAvailQuantity = Math.abs(availQty1) - Math.abs(remainQty);
															}
															issueDto.setQuantity1(remin);
															//iid.updateIndentEntryAmountColumn(objDTO.getAmount(), String.valueOf(remin), String.valueOf(indentEntrySeqNum));
															int insertIndentIssueResult = itosd.insertIndentIssueData(indentEntrySeqNum, issueDto, objDTO.getPrice1(), String.valueOf(remin), dto.getStrPriceId(), strUserId, strSiteId,expiryDate);

													itosd.insertIntremidiatoryTabbleData( insertIndentIssueResult,  issueDto,  dto.getPrice1(),  String.valueOf(remin),  dto.getStrPriceId(),indentEntrySeqNum);
													//iid.updateSumadhuraPriceListIndentEntryDetailsId(dto.getPriceId(), String.valueOf(indentEntrySeqNum));
													if (insertIndentIssueResult >= 1) {
														issueDto.setQuantity1(remin);
														int updateIndentAvalibilityResult = itosd.updateIndentAvalibility(issueDto, strSiteId);
														// viewToBeSelected =
														// "indentReceiveResponse";
														result = "Success";
														if (updateIndentAvalibilityResult == 0) {
															request.setAttribute("exceptionMsg", "Oops! Something went wrong, please try again.");
															transactionManager.rollback(status);
															WriteTrHistory.write("Tr_Completed");
															return result = "Failed";
															// Making a new entry if
															// new product.
															//below line commented by Rafi
															//itosd.updateIndentAvalibilityWithNewIndent(issueDto, strSiteId);
															//result = "Success";
														}
														itosd.updatePriceListDetails(String.valueOf(DBAvailQuantity), "A", objDTO.getStrPriceId());
														//	iid.updateIssueDetailsIntoSumadhuraCloseBalance(issueDto, strSiteId, objDTO.getAmount(), remin);
														itosd.updateIssueDetailsSumadhuClosingBalByProduct(issueDto, strSiteId, objDTO.getPrice1(), remin);
													}

															double doubleTotalAmountDc = availQty1*Double.valueOf(dto.getPrice1());
															doubleTotalAmountDc=((double)((int)(doubleTotalAmountDc*100)))/100;
															Map<String,Double> map = getPriceAndTaxes(dto , strState, remin);
															getOtherCharges( dto, strState,remin,transportChargesMap,i);
															totalAmt += map.get("totalAmount");
															totalAmt=((double)((int)(totalAmt*100)))/100;

															if (strState.equals("Local")) {
																tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(remin)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax")+"@@"+map.get("totalAmountBeforetax")+"@@"+map.get("CGSTTax")+"@@"+map.get("CGSTAmount")+"@@"+map.get("SGSTTax")+"@@"+map.get("SGSTAmount")+"@@"+""+"@@"+""+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
															} else {
																tblTwoData.append(count+"@@"+childProdName+"@@"+strHsSNCode+"@@"+rems+"@@"+measurementName+"@@"+String.valueOf(remin)+"@@"+quantity+"@@"+map.get("pricePerUnitBeforeTax") +"@@"+map.get("totalAmountBeforetax")+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+map.get("IGSTTax")+"@@"+map.get("IGSTAmount")+"@@"+map.get("totalAmount")+"@@"+"note"+"@@"+"-"+"&&");
																//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
															}
															count++;
														}else{
															transactionManager.rollback(status);
															request.setAttribute("childProduct",childProdName);
															return result = "Expired";
														}
													}
													//remainQty = remainQty - availQty1;
													issueDto.setQuantity1(remainQty);

												}else{
													break;
												}
											}
											//itosd.updateIndentEntryAmountColumn(String.valueOf(totalAmt), String.valueOf(issuQuantiy), String.valueOf(indentEntrySeqNum));
										}
									}
									else{
										transactionManager.rollback(status);
										request.setAttribute("childProduct",childProdName);
										return result = "Expired";}
								}

							}

						} else {
							if(strRequestFrom.equals("")){
								transactionManager.rollback(status);
								WriteTrHistory.write("Tr_Completed");
							}
							return result = "No Stock";
						}
						/*int insertIndentIssueResult = itosd.insertIndentIssueData(indentEntrySeqNum, issueDto);
					if(insertIndentIssueResult >= 1) {
						int updateIndentAvalibilityResult = itosd.updateIndentAvalibility(issueDto, strSiteId);
						//viewToBeSelected = "indentReceiveResponse";
						result = "Success";

						//transactionManager.commit(status);
					}*/
					}
					//count++;


				}
				//itosd.updateIndentEntryAmountColumn(String.valueOf(totalAmt), "", String.valueOf(indentEntrySeqNum));

				String strFromSiteName = "";
				String strFromSiteAddress = "";
				String strFromSitGSTIN = "";
				String strToSiteName = "";
				String strToSiteAddress = "";
				String strToSitGSTIN = "";
				String strFromState="";
				String strToState="";
				String fromStateCode="";
				String toStateCode="";

				List<Map<String, Object>>  dbClosingBalancesList = itosd.getSiteDetails(strSiteId);
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {

						strFromSiteName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
						strFromSiteAddress =prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
						strFromSitGSTIN = prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString();
						strFromState=prods.get("STATE") == null ? "" : prods.get("STATE").toString();
						if(!strFromSitGSTIN.equals("") && !strFromSitGSTIN.equals("-")){
						fromStateCode=strFromSitGSTIN.substring(0,2);}

					}}
				dbClosingBalancesList = itosd.getSiteDetails(toSite);
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {

						strToSiteName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
						strToSiteAddress =prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString();
						strToSitGSTIN = prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString();
						strToState=prods.get("STATE") == null ? "" : prods.get("STATE").toString();
						if(strToSitGSTIN.equals("") && strToSitGSTIN.equals("-")){
						toStateCode=strToSitGSTIN.substring(0,2);}

					}}
				// ravi written this one for dynamic state and code
				request.setAttribute("strFromstate",strFromState);
				request.setAttribute("strTostate",strToState);
				request.setAttribute("fromStateCode",fromStateCode);
				request.setAttribute("toStateCode",toStateCode);
				
				//int intTotalAmount = Integer.parseInt(String.valueOf(totalAmt));
				//totalAmt=Double.valueOf((String.format("%.2f",totalAmt)));
				/*totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));*/
				totalAmt=((double)((int)(totalAmt*100)))/100;
				/*double val =  (double) Math.ceil(Double.valueOf(totalAmt));

				String strRoundOf = String.valueOf(Math.ceil(totalAmt)-totalAmt);
				String strtotalAmt=String.format("%.2f",totalAmt);

				String totalAmtAfterRoundOff = String.valueOf((int)Math.ceil(totalAmt));*/

				double transportAmount = Double.valueOf(transportChargesMap.get("OtherOrTransportChargeAfterTax").toString());
				transportAmount=((double)((int)(transportAmount*100)))/100;

				totalAmt = totalAmt+transportAmount;
				itosd.updateIndentEntryAmountColumn(String.valueOf(totalAmt), "", String.valueOf(indentEntrySeqNum));
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


			}
			else {
				request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Issue.");
				//viewToBeSelected = "indentIssueResponse";
				if(strRequestFrom.equals("")){
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
				}
				return result = "Failed";
			}				
		} else {
			request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
			//viewToBeSelected = "indentIssueResponse";
			if(strRequestFrom.equals("")){
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
			return result = "Failed";
		}

		if(strRequestFrom.equals("")){
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		}
	}
	catch (Exception e) {
		if(strRequestFrom.equals("")){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
		}
		request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Issue.");
		//viewToBeSelected = "indentIssueResponse";
		result = "Failed";
		e.printStackTrace();
	}
	return result;
}

public Map getPriceAndTaxes(IssueToOtherSiteDto objIssueToOtherSiteDto,String strState,double issuQuantiy){

	Map<String,Double> map = new HashMap<String,Double>();
	//double quantity = objIssueToOtherSiteDto.getQuantity1();
	double pricePerUnitBeforeTax = Double.valueOf(objIssueToOtherSiteDto.getAmountPerUnitBeforeTax());
	double tax = Double.valueOf(objIssueToOtherSiteDto.getTax());
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

public Map<Object,Object> getOtherCharges(IssueToOtherSiteDto objIssueToOtherSiteDto,String strState,double issuQuantiy , Map<Object,Object> map,int intCount){


	double receivedQuantity = Double.valueOf(objIssueToOtherSiteDto.getReceivedQuantity());
	double doubleOtherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getAmountAfterTaxotherOrTransportCharges());
	double tax = Double.valueOf((objIssueToOtherSiteDto.getTaxotherOrTransportCharges()==null||objIssueToOtherSiteDto.getTaxotherOrTransportCharges().equals(""))?"0":objIssueToOtherSiteDto.getTaxotherOrTransportCharges());
	double pricePerUnit = 0.0;

	//double otherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getOtherOrTransportCharges());
	//double taxOnOtherOrTransportCharge = Double.valueOf(objIssueToOtherSiteDto.getTaxotherOrTransportCharges());
	double CGSTTax = 0;
	double SGSTTax = 0;
	double IGSTTax = 0;
	double CGSTAmount = 0;
	double SGSTAmount = 0;
	double IGSTAmount = 0;
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


@Override
public Map<String, String> loadSites() {
	return itosd.loadSitesInformation();
}

@Override
public List<IssueToOtherSiteInwardBean> getIssuesToOtherLists(HttpSession session,String RequestId, String siteId,String str) {
	List<IssueToOtherSiteInwardBean> listIssueToOtherSiteList = null;

	try {

		listIssueToOtherSiteList = itosd.getIssueLists(session,RequestId,  siteId,str);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return listIssueToOtherSiteList;

}

@Override
public List<IssueToOtherSiteInwardBean> getGetProductDetailsLists(HttpSession session,String RequestId, String siteId,String strIssueType) {
	List<IssueToOtherSiteInwardBean> listIssueToOtherSiteInvoiceList = null;

	try {

		listIssueToOtherSiteInvoiceList  = itosd.getGetProductDetailsLists(session,RequestId,  siteId,strIssueType);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return listIssueToOtherSiteInvoiceList ;
}

@Override
public List<IssueToOtherSiteInwardBean> getTransportChargesList(String RequestId, String siteId) {
	List<IssueToOtherSiteInwardBean> transportChargesList = null;

	try {


		transportChargesList = itosd.getTransportChargesList(RequestId,  siteId);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return transportChargesList;
}



public String doIndentInwardsFromOtherSite(Model model, HttpServletRequest request, HttpSession session) {

	String invoiceNumber = "";
	//String viewToBeSelected = "";
	boolean isSendMail = true;
	String siteWiseIndentNo="";
	String indentNumber="";
	String strUserName="";
	String currentUserMobileNo="";
	String currentUserEmail="";
	String reqSiteName="";
	int reqSiteId = 0;
	
	String CGST = "";
	String SGST = "";
	String IGST = "";
	Double percent = 0.0;
	Double CGSTAMT = 0.0;
	Double SGSTAMT = 0.0;
	Double IGSTAMT = 0.0;
	Double amt = 0.0;
	String userId = "";
	String site_id = "";
	String invoiceNum = request.getParameter("InvoiceNumber");
	String transportId ="Conveyance";
	String transportGSTPercentage = "GSTTax";
	String transportGSTAmount = "GSTAmount";
	String totalAmountAfterGSTTax = "AmountAfterTax";
	String transportInvoiceId = "TransportInvoice";
	String transportAmount = "ConveyanceAmount";
	String chargeIGST = "";
	Double chargeIGSTAMT = 0.0;
	Double taxChargesPercentage = 0.0;
	Double chargeSGSTAMT = 0.0;
	Double chargeCGSTAMT =  0.0;
	Double chargeAmt = 0.0;
	String chargeSGST = "";
	String chargeCGST = "";
	String tblChargeName = "";
	String gstChargePercentage = "";
	String transAmount = "";
	String totAmtAfterGSTTax = "";
	String gstAmount = "";
	StringBuffer tblCharges = new StringBuffer();
	double doubleSumOfOtherCharges = 0.0;
	BigDecimal bigDecimal=null;
	int indentEntrySeqNum = 0;
	Map<String, String> viewGrnPageDataMap = null;
	IssueToOtherSiteDto objIssueToOtherSiteDto = null;
	List<IssueToOtherSiteDto> listOfInwardsObj=new ArrayList<IssueToOtherSiteDto>();
	Map<String,List<IssueToOtherSiteDto>> listOfUniqueObj=new HashMap<String,List<IssueToOtherSiteDto>>();
	TransactionDefinition def = new DefaultTransactionDefinition();
	TransactionStatus status = transactionManager.getTransaction(def);
	WriteTrHistory.write("Tr_Opened in IsOS_dIWOth, ");

	String result = "";

	try {
		viewGrnPageDataMap = new HashMap<String, String>();
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
		logger.info("Records To Be Processed = "+recordsCount);
		
		strUserName = session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
		currentUserEmail = session.getAttribute("EMP_EMAIL") == null ? "" :  session.getAttribute("EMP_EMAIL").toString();
		currentUserMobileNo = session.getAttribute("MOBILE_NUMBER") == null ? "" :  session.getAttribute("MOBILE_NUMBER").toString();

		//site name and id who sends the indent quantity 
		String senderSiteName=request.getParameter("senderSiteName");
		String senderSiteId=request.getParameter("senderSiteId");
		
		//the site that is receiving the indent 
		String siteName=session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();;
		String siteId=session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();;
		siteWiseIndentNo=request.getParameter("siteWiseIndentNo")==null?"0":request.getParameter("siteWiseIndentNo").trim();
		indentNumber=request.getParameter("indentNumber")==null?"":request.getParameter("indentNumber");
		String numOfRec[] = null;
		if((recordsCount != null) && (!recordsCount.equals(""))) {
			numOfRec = recordsCount.split("\\|");
		}

		if(numOfRec != null && numOfRec.length > 0) {

			String productId = "productId";
			String subProductId = "subProductId";
			String childProductId = "childProductId";
			String measurementsId = "measurmentId";	


			String productName = "product";
			String subProductName = "subProduct";
			String childProductName = "childProduct";
			String measurementsName = "unitsOfMeasurement";	


			String basicAmount = "BasicAmountId";

			String qty = "quantity";
			String expireDate = "expireDate";
			String price = "price";			
			String hsnCode = "hsnCode";
			String otherOrTransportCharges = "otherOrTransportCharges";				
			String taxOnOtherOrTransportCharges = "taxOnOtherOrTransportCharges";				
			String otherOrTransportChargesAfterTax = "otherOrTransportChargesAfterTax";				
			String totalAmount = "totalAmount";
			String dcNo = request.getParameter("dcNo");
			invoiceNumber = request.getParameter("invoiceNumber");
			String invoiceDate = request.getParameter("invoiceDate");
			String vendorName = request.getParameter("vendorName");
			String receviedDate = request.getParameter("receivedDate");
			String strIssueType = request.getParameter("strIssueType");

			String strIntrmidiatendentEntryId = request.getParameter("intrmidiatetEntryId"); 
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+invoiceNumber+" , IntrmidiatendentEntryId:"+strIntrmidiatendentEntryId);


			objIssueToOtherSiteDto = new IssueToOtherSiteDto();

			String vendorId = request.getParameter("VendorId");
			String vendorAddress = request.getParameter("vendorAdress");
			String gstinNumber = request.getParameter("gsinNo");
			String note = request.getParameter("Note");
			String state = request.getParameter("state");
			String ttlAmntForIncentEntryTbl = request.getParameter("strTotalInvoice");

			if(ttlAmntForIncentEntryTbl == null){
				ttlAmntForIncentEntryTbl = request.getParameter("ttlAmntForIncentEntryId");
			}

			int val = (int) Math.round(Double.valueOf(ttlAmntForIncentEntryTbl));

			String vehileNo = request.getParameter("vehileNo");
			String transporterName = request.getParameter("transporterName");
			String indentCreationId =  request.getParameter("indentNumber"); // indent number

			logger.info(invoiceNumber+" <-->  <--> "+vendorName+" <--> "+vendorId+" <--> "+vendorAddress+" <--> "+gstinNumber+" <--> "+hsnCode+" <--> "+ttlAmntForIncentEntryTbl+" <--> "+note);

			if(invoiceDate.contains(" ")){
				String [] invoiceDatearr = invoiceDate.split(" ");
				invoiceDate = invoiceDatearr[0];
			}
			//   invoiceDate = "17-sep-2017";
			//strIntrmidiatendentEntryId = "549";
			//pavan settings objects
			objIssueToOtherSiteDto.setInvoiceNo(invoiceNumber);
			objIssueToOtherSiteDto.setInvoiceDate(invoiceDate);
			objIssueToOtherSiteDto.setStrVendorId(vendorId);
			objIssueToOtherSiteDto.setTotalAmount1(ttlAmntForIncentEntryTbl);
			objIssueToOtherSiteDto.setRemarks1(note);
			objIssueToOtherSiteDto.setReceviedDate(receviedDate);
			objIssueToOtherSiteDto.setTransportInvoice1(transporterName);
			objIssueToOtherSiteDto.setVehicleNo(vehileNo);





			userId = String.valueOf(session.getAttribute("UserId"));

			site_id = String.valueOf(session.getAttribute("SiteId"));
			if (StringUtils.isNotBlank(site_id)){

			} else {
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return result = "SessionFailed";
			}
			indentEntrySeqNum = itosd.getIndentEntrySequenceNumber();
			session.setAttribute("indentEntrySeqNum", indentEntrySeqNum);

			/*start 01-setp-17*/
			String chargesRecordsCount =  request.getParameter("numbeOfTransRowsToBeProcessed");
			String numOfChargeRec[] = null;
			if((chargesRecordsCount != null) && (!chargesRecordsCount.equals(""))) {
				numOfChargeRec = chargesRecordsCount.split("\\|");
				if(numOfChargeRec != null && numOfChargeRec.length > 0) {
					for(String charNum : numOfChargeRec) {
						String transactionDts = request.getParameter(transportId+charNum);
						String transDts[] = transactionDts.split("\\$");
						String transactionId = transDts[0];
						tblChargeName = transDts[1];
						String gstPercentage = request.getParameter(transportGSTPercentage+charNum);
						String arraypercent[] = gstPercentage.split("\\$");
						String gstId = arraypercent[0];
						gstChargePercentage = arraypercent[1];
						gstChargePercentage = gstChargePercentage.replace("%", "").trim();
						gstAmount = request.getParameter(transportGSTAmount+charNum);
						totAmtAfterGSTTax = request.getParameter(totalAmountAfterGSTTax+charNum);
						String transactionInvoiceId = request.getParameter(transportInvoiceId+charNum);
						transAmount = request.getParameter(transportAmount+charNum);


						if (state.equals("1")) {
							if (gstChargePercentage.equals("0")) {
								chargeCGST = "0";
								chargeSGST = "0";
							} else {
								taxChargesPercentage = Double.parseDouble(gstChargePercentage)/2;
								chargeAmt = Double.parseDouble(gstAmount)/2;
								chargeCGSTAMT = chargeAmt;
								chargeSGSTAMT = chargeAmt;
								chargeCGST = String.valueOf(taxChargesPercentage);
								chargeSGST = String.valueOf(taxChargesPercentage);
							}
						}else {
							taxChargesPercentage = Double.parseDouble(gstChargePercentage);
							chargeAmt = Double.parseDouble(gstAmount);
							chargeIGST = String.valueOf(taxChargesPercentage);
							chargeIGSTAMT = chargeAmt;
						}
						if (state.equals("1")) {
							tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+totAmtAfterGSTTax+"@@"+"-"+"&&");
						} else {
							tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+chargeIGST+"@@"+chargeIGSTAMT+"@@"+totAmtAfterGSTTax+"@@"+"-"+"&&");
						}
					}

				}
			}
			/*end 01-sept-17*/ 
			logger.info("Indent Entry Seq. Num. = "+indentEntrySeqNum);

			//pavan added object
			objIssueToOtherSiteDto.setUserId(userId);
			objIssueToOtherSiteDto.setFromSite(site_id);
			if (state.equals("1")) {
				objIssueToOtherSiteDto.setState("Local");
			} else {
				objIssueToOtherSiteDto.setState("Non Local");
			}
			int insertReceiveReult = itosd.insertInvoiceData(indentEntrySeqNum, objIssueToOtherSiteDto);
			logger.info("Insert Requester Reult = "+insertReceiveReult);
			itosd.updateIntermidiatoryTabledata(strIntrmidiatendentEntryId) ;
			if(insertReceiveReult >= 1) {
				int count = 1;
				Date grnDate = new Date();
				StringBuffer tblTwoData = new StringBuffer();

				for(String num : numOfRec) {

					objIssueToOtherSiteDto = new IssueToOtherSiteDto();

					num = num.trim();

					String dcNumber = request.getParameter("dcNumber"+num);
					String prodId = request.getParameter(productId+num);					
					String prodName = request.getParameter(productName+num);
					String subProdId = request.getParameter(subProductId+num);
					String subProdName = request.getParameter(subProductName+num);		
					String childProdId = request.getParameter(childProductId+num);
					String childProdName =  request.getParameter(childProductName+num);
					String unitsOfMeasurementId = request.getParameter(measurementsId+num);
					String measurementName = request.getParameter(measurementsName+num);
					String expiryDate = request.getParameter(expireDate+num);
					double quantity = Double.valueOf(request.getParameter(qty+num));
					String prc = request.getParameter(price+num);
					String basicAmnt = request.getParameter(basicAmount+num);
					String hsnCd = request.getParameter(hsnCode+num);
					String strOtherOrTransportCharg = request.getParameter(otherOrTransportCharges+num);
					String strTaxOtherOrTransportCharg = request.getParameter(taxOnOtherOrTransportCharges+num);
					String strAmountAfterOtherOrTransportCharg = request.getParameter(otherOrTransportChargesAfterTax+num);
					String totalAmnt = request.getParameter(totalAmount+num);

					logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+"  <--> "+measurementName+" <--> "+hsnCd);

					if (StringUtils.isNotBlank(prodId) && StringUtils.isNotBlank(subProdId) && StringUtils.isNotBlank(childProdId)) {

					} else {
						transactionManager.rollback(status);
						WriteTrHistory.write("Tr_Completed");
						return result = "Failed";
					}


					if (state.equals("1")) {
						tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"basic rate"+"@@"+"CGST"+"@@"+"CGSTAMT"+"@@"+"SGST"+"@@"+"SGSTAMT"+"@@"+""+"@@"+""+"@@"+totalAmnt+"@@"+note+"@@"+"-"+"&&");
					} else {
						tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"basicAmnt"+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+IGSTAMT+"@@"+totalAmnt+"@@"+note+"@@"+"-"+"&&");
						//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
					}
					objIssueToOtherSiteDto.setDcNumber(dcNumber);
					objIssueToOtherSiteDto.setProdId1(prodId);
					objIssueToOtherSiteDto.setProdName1(prodName);
					objIssueToOtherSiteDto.setSubProdId1(subProdId);
					objIssueToOtherSiteDto.setSubProdName1(subProdName);
					objIssueToOtherSiteDto.setChildProdId1(childProdId);
					objIssueToOtherSiteDto.setChildProdName1(childProdName);
					objIssueToOtherSiteDto.setPrice1(prc);
					//objIssueToOtherSiteDto.setQuantity1(quantity);
					
					//objIssueToOtherSiteDto.setMeasurementId1(unitsOfMeasurementId);
					//objIssueToOtherSiteDto.setMeasurementName1(measurementName);
					
					objIssueToOtherSiteDto.setStrHSNCode1(hsnCd);
					objIssueToOtherSiteDto.setTotalAmount1(totalAmnt);
					objIssueToOtherSiteDto.setDate(invoiceDate);
					objIssueToOtherSiteDto.setDate(receviedDate);
					objIssueToOtherSiteDto.setExpiryDate1(expiryDate);
					objIssueToOtherSiteDto.setOtherOrTransportCharges(strOtherOrTransportCharg);
					objIssueToOtherSiteDto.setTaxotherOrTransportCharges(strTaxOtherOrTransportCharg);
					objIssueToOtherSiteDto.setAmountAfterTaxotherOrTransportCharges(strAmountAfterOtherOrTransportCharg);


					
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
						Class<?>[] paramTypes = {String.class,double.class,double.class, String.class};
						Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
						//in below line, basicAmnt is passing in the method. it is newly added because of live issue.
						values = (Map<String, String>) printDogMethod.invoke(mesurment,basicAmnt, doubleActualQuantity,doubleInputQuantity,measurementName);	            
						for(Map.Entry<String, String> retVal : values.entrySet()) {
							quantity=(int)Double.parseDouble(retVal.getKey());
							prc=retVal.getValue(); 
						}
						unitsOfMeasurementId = strConversionMesurmentId;
						measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
					}
					//quantity=Double.parseDouble(new DecimalFormat("##.##").format(quantity));
					objIssueToOtherSiteDto.setQuantity1(quantity);
					objIssueToOtherSiteDto.setPrice1(prc);
					objIssueToOtherSiteDto.setMeasurementId1(unitsOfMeasurementId);
					objIssueToOtherSiteDto.setMeasurementName1(measurementName);
					objIssueToOtherSiteDto.setTypeOfPurchase("OtherSite");
					
					objIssueToOtherSiteDto.setIndentSettledEmployeeName(strUserName);
					objIssueToOtherSiteDto.setCurrentUserEemail(currentUserEmail);
					objIssueToOtherSiteDto.setCurrentUserMobileNumber(currentUserMobileNo);
					objIssueToOtherSiteDto.setSenderSiteName(senderSiteName);
					objIssueToOtherSiteDto.setSenderSiteId(senderSiteId);
					objIssueToOtherSiteDto.setSiteName(siteName);
					objIssueToOtherSiteDto.setSiteId(siteId);
					objIssueToOtherSiteDto.setSiteWiseIndentNumber(siteWiseIndentNo);
					
					//loading the actual requested quantity for mail
					try {
						String receivedQuantity=itosd.loadIndentRequestedQuantity(objIssueToOtherSiteDto,invoiceNumber);
						 bigDecimal=new BigDecimal(receivedQuantity).setScale(2,RoundingMode.CEILING);
						objIssueToOtherSiteDto.setReceivedQuantity(bigDecimal.toString());
						if(receivedQuantity.length()==0){
							objIssueToOtherSiteDto.setReceivedQuantity(String.valueOf(quantity));
						}
					} catch (Exception e) {
						e.printStackTrace();
						objIssueToOtherSiteDto.setReceivedQuantity(String.valueOf(quantity));e.printStackTrace();
					}
					
					List<IssueToOtherSiteDto> tempSettelMentData=new ArrayList<IssueToOtherSiteDto>();
					if(listOfUniqueObj.containsKey(prodId+subProdId+childProdId)){
						tempSettelMentData=	listOfUniqueObj.get(prodId+subProdId+childProdId);
						IssueToOtherSiteDto tempIssueToOtherSiteDto=tempSettelMentData.get(0);
						tempIssueToOtherSiteDto.setQuantity1(tempIssueToOtherSiteDto.getQuantity1()+objIssueToOtherSiteDto.getQuantity1());
					}else{
						listOfInwardsObj.add(objIssueToOtherSiteDto);
						listOfUniqueObj.put(prodId+subProdId+childProdId,listOfInwardsObj);
					}
					
					//for mail content
					

					int intSumadhuraPriceListSeqNo = ird.getPriceList_SeqNumber();
					int intIndentEntryDetailsSeqNo =  itosd.getEntryDetailsSequenceNumber();

					int insertIndentReceiveResult = itosd.insertIndentReceiveData(intIndentEntryDetailsSeqNo,indentEntrySeqNum, objIssueToOtherSiteDto, userId, site_id,intSumadhuraPriceListSeqNo);



					if(insertIndentReceiveResult >= 1) {
						int updateIndentAvalibilityResult = itosd.updateIndentAvalibilityInwards(objIssueToOtherSiteDto, site_id);
						result = "Success";
						if(updateIndentAvalibilityResult == 0) {
							itosd.updateIndentAvalibilityWithNewIndent(objIssueToOtherSiteDto, site_id);
							result = "Success";
						}
						String id = itosd.getIndentAvailableId(objIssueToOtherSiteDto, site_id);
						if (StringUtils.isNotBlank(id)) {
							itosd.saveReciveDetailsIntoSumduraPriceList(intIndentEntryDetailsSeqNo,objIssueToOtherSiteDto, invoiceNumber, site_id, id,intSumadhuraPriceListSeqNo);
						} else {
							String id1 = itosd.getProductAvailabilitySequenceNumber();
							itosd.saveReciveDetailsIntoSumduraPriceList(intIndentEntryDetailsSeqNo,objIssueToOtherSiteDto, invoiceNumber, site_id, id1,intSumadhuraPriceListSeqNo);
						}
						itosd.saveReceivedDataIntoSumadhuClosingBalByProduct(objIssueToOtherSiteDto, site_id);

					}   else {
						request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
						transactionManager.rollback(status);
						WriteTrHistory.write("Tr_Completed");
						return result = "Failed";
					}
					count++;



					if(indentCreationId != null && !indentCreationId.equals("") && !indentCreationId.equals("0")){

						int intSiteId = Integer.parseInt(site_id);
						//int intUserId = Integer.parseInt(userId);

						objIndentCreationServiceImpl.acceptIndentReceive( request,  intSiteId,  userId, num);
					}
				}
			}
			else {
				request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
				result = "Failed";
			}


			String tansRecordsCount = request.getParameter("numbeOfTransRowsToBeProcessed");
			logger.info("Records To Be Processed = "+tansRecordsCount);

			String numOfTransRec[] = null;
			if((tansRecordsCount != null) && (!tansRecordsCount.equals(""))) {
				numOfTransRec = tansRecordsCount.split("\\|");
			}

			if(numOfTransRec != null && numOfTransRec.length > 0  && strIssueType.equals("NonReturable")) {


				String strConveyance = "Conveyance";				
				String strConveyanceAmount = "ConveyanceAmount";				
				String strGSTTax = "GSTTax";				
				String strGSTAmount = "GSTAmount";
				String strAmountAfterTax = "AmountAfterTax";				
				String strTransportInvoice = "TransportInvoice";
				String strIndentEntryDetailsId = "IndentEntryDetailsId";

				userId = String.valueOf(session.getAttribute("UserId"));

				site_id = String.valueOf(session.getAttribute("SiteId"));
				if (StringUtils.isNotBlank(site_id)){

				} else {
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result = "SessionFailed";
				}
				int count = 1;
				String strConveyanceTypeCharg = "";				
				String strConveyanceAmountChrg = "";				
				String strGSTTaxChrg = "";				
				String strGSTAmountChrg = "";
				String strAmountAfterTaxChrg = "";				
				String strTransportInvoiceChrg = "";


				for(String num : numOfTransRec) {
					num = num.trim();
					objIssueToOtherSiteDto = new IssueToOtherSiteDto();
					strConveyanceTypeCharg = request.getParameter(strConveyance+num);
					strConveyanceAmountChrg = request.getParameter(strConveyanceAmount+num);
					strGSTTaxChrg = request.getParameter(strGSTTax+num);
					strGSTAmountChrg = request.getParameter(strGSTAmount+num);
					strAmountAfterTaxChrg = request.getParameter(strAmountAfterTax+num);
					strTransportInvoiceChrg = request.getParameter(strTransportInvoice+num);

					if(strConveyanceTypeCharg.contains("$")){
						String [] convycharg =strConveyanceTypeCharg.split("\\$");
						strConveyanceTypeCharg = convycharg[0];
					}
					if(strGSTTaxChrg.contains("%") && strGSTTaxChrg.contains("$")){
						String [] strGSTTaxChrgArr =strGSTTaxChrg.split("\\$");
						strGSTTaxChrg = strGSTTaxChrgArr[0];
					}

					objIssueToOtherSiteDto.setConveyance1(strConveyanceTypeCharg);
					objIssueToOtherSiteDto.setConveyanceAmount1(strConveyanceAmountChrg);
					objIssueToOtherSiteDto.setGSTTax1(strGSTTaxChrg);
					objIssueToOtherSiteDto.setGSTAmount1(strGSTAmountChrg);
					objIssueToOtherSiteDto.setAmountAfterTax1(strAmountAfterTaxChrg);
					objIssueToOtherSiteDto.setTransportInvoice1(strTransportInvoiceChrg);	
					itosd.updateInvoiceOtherCharges(objIssueToOtherSiteDto,site_id,indentEntrySeqNum);
				count++;
				}	
			
			
			}else {
				if(strIssueType.equals("NonReturable")){
					request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result = "Failed";
				}

			}

			// this is for indent created and receiving through indent creation
			if(indentCreationId != null && !indentCreationId.equals("") && !indentCreationId.equals("0")){
				int intSiteId = Integer.parseInt(site_id);
				//int intUserId = Integer.parseInt(userId);

				//objIndentCreationServiceImpl.acceptIndentReceive( request,  intSiteId,  userId, num);
				objIndentCreationServiceImpl.checkisIndentClose( request,  intSiteId,  userId);
			}
				
				
				
				String s=null;
				//s.trim();
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			
		} else {
			request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			return result = "Failed";
		}
	} catch (Exception e) {
		isSendMail = false;
		transactionManager.rollback(status);
		WriteTrHistory.write("Tr_Completed");
		request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
		result = "Failed";
		AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive");
		auditBean.setStatus("error");
		auditBean.setSiteId(site_id);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		e.printStackTrace();
	}
	
	if(isSendMail&&!siteWiseIndentNo.equals("0")){
		//mail code part started
		List<String> ccMailsOfIndentEmployee=new ArrayList<String>();
		//holding the HTML page content, this content will show in mail
		StringBuffer indentSitetable_DataIndentSettlementForMail=new StringBuffer();
		//holding the mail subject
		StringBuffer subjectForMailIndentSettlement=new StringBuffer();
		IssueToOtherSiteDto issueToOtherSiteDtoForMail =new IssueToOtherSiteDto();
		List<String> mobilesList=new ArrayList<String>();
		String smsMessageContent="";
			//listOfInwardsObj
			int serialNo=1;
			for (IssueToOtherSiteDto issueToOtherSiteDto : listOfInwardsObj) {
				issueToOtherSiteDtoForMail=issueToOtherSiteDto;
				 bigDecimal=new BigDecimal(issueToOtherSiteDto.getQuantity1()).setScale(2,RoundingMode.CEILING);
				indentSitetable_DataIndentSettlementForMail.append("<tr>")
				.append("<td>"+serialNo+"</td> ")
				.append("<td>"+siteWiseIndentNo+"</td> ")
				.append("<td>"+issueToOtherSiteDto.getProdName1()+"</td> ")
				.append("<td>"+issueToOtherSiteDto.getSubProdName1()+"</td> ")
				.append("<td>"+issueToOtherSiteDto.getChildProdName1()+"</td>  ")
				.append("<td>"+issueToOtherSiteDto.getMeasurementName1()+"</td>  ")
				.append("<td>"+issueToOtherSiteDto.getReceivedQuantity()+"</td> ")
				.append("<td>"+bigDecimal+"</td> ")
				.append("</tr>");
				if(serialNo==1)
					subjectForMailIndentSettlement=new StringBuffer("Request for Material Adjustment from Site "+issueToOtherSiteDto.getSenderSiteName()+" to Site "+issueToOtherSiteDto.getSiteName()+".");
				smsMessageContent="Stores "+issueToOtherSiteDto.getSiteName()+" had received material that was requested in Indent Number "+siteWiseIndentNo+". Material was sent from Stores  "+issueToOtherSiteDto.getSenderSiteName()+" to "+issueToOtherSiteDto.getSiteName()+". For more details follow http://129.154.74.18/Sumadhura/";
				serialNo++;
			}
			
			//holding the sender site email and phone number, using this we have to trigger mail to them
			List<String> senderSite_ToMail=new ArrayList<String>();
			//loading sender site email and phone number
			String emailUsername =UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername1=UIProperties.validateParams.getProperty("999_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String emailUsername2=UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			
			String ccEmailUsername1=UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
			String ccEmailUsername2=UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
		
			
			String mobileNumber1=UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSiteId()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			if(!issueToOtherSiteDtoForMail.getSenderSiteId().equals("999")){
				String mobileNumber2=UIProperties.validateParams.getProperty("999_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
				String ccEmailUsername3=UIProperties.validateParams.getProperty("999_CC_USERNAME_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty("999_CC_USERNAME_FOR_CENTRAL_SETTLEMENT").toString();
				
				if(ccEmailUsername3.length()!=0)
					ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername3.split(",")));
				if(mobileNumber2.length()!=0)
					mobilesList.addAll(Arrays.asList(mobileNumber2.split(",")));
			}
			String mobileNumber3=UIProperties.validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT") == null ? "" : validateParams.getProperty(issueToOtherSiteDtoForMail.getSenderSiteId()+"_MOBILE_NO_FOR_CENTRAL_SETTLEMENT").toString();
			
			 
			if(emailUsername.length()!=0)
				senderSite_ToMail.addAll(Arrays.asList(emailUsername.split(",")));
			if(emailUsername1.length()!=0)
				senderSite_ToMail.addAll(Arrays.asList(emailUsername1.split(",")));
			if(emailUsername2.length()!=0)
				senderSite_ToMail.addAll(Arrays.asList(emailUsername2.split(",")));
		 	
			
			if(ccEmailUsername1.length()!=0)
				ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername1.split(",")));
			if(ccEmailUsername2.length()!=0)
				ccMailsOfIndentEmployee.addAll(Arrays.asList(ccEmailUsername2.split(",")));
		
			 
			if(mobileNumber1.length()!=0)
				mobilesList.addAll(Arrays.asList(mobileNumber1.split(",")));
			if(mobileNumber3.length()!=0)
				mobilesList.addAll(Arrays.asList(mobileNumber3.split(",")));
			
		/*	try {
				//sending SMS to user's, who are involved in indent settlement
				sms.sendOTP(mobilesList, smsMessageContent);
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}*/
			
			//mobilesList=new ArrayList<>();
			
			//loading the indent created and approved employee names and email's and phone number
		/*	List<Map<String, Object>> approveEmail=itosd.getIndentCreatedEmpName("",indentNumber);
			for (Map<String, Object> map : approveEmail) {
				String mobileNumber=map.get("MOBILE_NUMBER")==null?"":map.get("MOBILE_NUMBER").toString();
				String email=map.get("EMP_EMAIL")==null?"":map.get("EMP_EMAIL").toString();
				if(mobileNumber.length()!=0){
					mobilesList.add(Long.valueOf(mobileNumber));
				}
				ccMailsOfIndentEmployee.put(email,mobileNumber);
			}*/
			
			if(mobilesList.size()!=0){
				try {
					mobileSMS.sendSMS(mobilesList, smsMessageContent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			//here we are sending the mails to the site, which are involved in product settlement
			EmailFunction objEmailFunction = new EmailFunction();
			//sending mail to user's , who are involved in indent settlement
			objEmailFunction.sendMailForIndentSettlement(issueToOtherSiteDtoForMail,0,ccMailsOfIndentEmployee,senderSite_ToMail,indentSitetable_DataIndentSettlementForMail,subjectForMailIndentSettlement,"true");
		
}
	if(result.equals("Success")){session.setAttribute("InwardsFromOtherSite_RequestId", invoiceNumber);}
	return result;
}


public String changeDateFormat(Date date) {

	String returnDate = "";

	try {
		returnDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	return returnDate;
}


public static void main(String [] args){
	double totalAmt = 30.251;
	//totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
	totalAmt=((double)((int)(totalAmt*100)))/100;
	System.out.println(totalAmt);
}

@Override
public Map<String, String> loadSitesInformationForIssueToOtherSite() {
	return itosd.loadSitesInformationForIssueToOtherSite();
}
}
