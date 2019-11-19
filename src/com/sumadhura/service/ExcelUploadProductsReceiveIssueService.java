package com.sumadhura.service;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.transdao.ExcelUploadProductsReceiveIssueDao;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.UIProperties;

public class ExcelUploadProductsReceiveIssueService  extends UIProperties {


	private PlatformTransactionManager transactionManager;
	private ExcelUploadProductsReceiveIssueDao objExcelUploadProductsReceiveIssueDao;

	public ExcelUploadProductsReceiveIssueService(PlatformTransactionManager transactionManager,
			ExcelUploadProductsReceiveIssueDao objExcelUploadProductsReceiveIssueDao) {
		this.transactionManager = transactionManager;
		this.objExcelUploadProductsReceiveIssueDao = objExcelUploadProductsReceiveIssueDao;
	}



	public String indentProcess(HttpServletRequest request, HttpSession session) {

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InRe_indPro, ");
		String response1 = "Failed@@_";
		String poNumber="";
		String indentNumber="";


		try{




			String	FilePath = "C:\\Users\\Administrator\\Desktop\\Central_adjustment_data_2.xls";


			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("Add in IMS Stock");

			// To get the number of rows present in sheet
			int totalNoOfRows = sh.getRows();



			// To get the number of columns present in sheet
			int totalNoOfCols = sh.getColumns();




			Map<String,String> map = new HashMap<String,String>();
			List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
			List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+request.getParameter("InvoiceNumber"));
			String typeOfPurchase= ""; //request.getParameter("typeOfPurchase");
			if(typeOfPurchase.equalsIgnoreCase("PO")){

				poNumber=request.getParameter("poNumber");



			}else{
				indentNumber=request.getParameter("indentNumber");
			}

			map.put("typeOfPurchase","Local Purchase");
			map.put("indentNumber",indentNumber);

			map.put("invoiceNum","SIPL260119");
			//String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			
			map.put("poNo",poNumber);
			map.put("dcNo",request.getParameter("dcNo"));
			map.put("invoiceNumber","SIPL290119_10");
			map.put("invoiceDate","290119");
			map.put("vendorName","SUmadhura_Central");
			map.put("vendorId","999");
			map.put("vendorAddress","");
			map.put("gstinNumber",request.getParameter("GSTINNumber"));
			map.put("note","central store stock settelement");
			map.put("state","1");
			map.put("ttlAmntForIncentEntryTbl"," 5026281");
			map.put("poDate","");
			map.put("eWayBillNo","");
			map.put("vehileNo","");
			map.put("transporterName","");
			map.put("otherChrgs","");//quotes placed
			map.put("receviedDate","290119");
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");
			map.put("chargesRecordsCount", transRows);


			int trans_rows = 1;
			for(int charNum=1;charNum<=trans_rows;charNum++){
				Map<String,String> transMap = new HashMap<String,String>();
				transMap.put("transactionDts","999$None");
				transMap.put("gstPercentage","1$0%");
				transMap.put("gstAmount","0");
				transMap.put("totAmtAfterGSTTax","0");
				transMap.put("transactionInvoiceId","");
				transMap.put("transAmount","0");
				transMapList.add(transMap);
			}


			String strPrductId = "";
			double doubleBasicAmount = 0.0;
			double doubleTaxAmount = 0.0;
			double amountAfterTax = 0.0;
			String strTotalAmnt = "";
			for(int row=1; row<totalNoOfRows;row++){
				//int num=Integer.parseInt(strRecordsCount[i]);
				Map<String,String> prodMap = new HashMap<String,String>();

				strPrductId = sh.getCell(0, row).getContents();
				if(strPrductId == null || strPrductId.equals("")){
					break;
				}

				prodMap.put("product",sh.getCell(0, row).getContents() +"\\\\$"+sh.getCell(1, row).getContents());					
				prodMap.put("subProduct",sh.getCell(2, row).getContents() +"\\\\$"+sh.getCell(3, row).getContents());
				prodMap.put("expiryDate","NA");
				prodMap.put("childProduct",sh.getCell(4, row).getContents() +"\\\\$"+sh.getCell(5, row).getContents());
				prodMap.put("quantity",sh.getCell(9, row).getContents());
				prodMap.put("prc",sh.getCell(10, row).getContents());
				
				//doubleBasicAmount= (Double.valueOf(sh.getCell(10, row).getContents()) * Double.valueOf(sh.getCell(9, row).getContents() ));
				//doubleTaxAmount = (Double.valueOf(sh.getCell(11, row).getContents())+Double.valueOf(sh.getCell(12, row).getContents()));
				//amountAfterTax = doubleBasicAmount+doubleTaxAmount;
					
				
				strTotalAmnt=  sh.getCell(15, row).getContents();
				strTotalAmnt = strTotalAmnt.replace(",","");
				
				prodMap.put("basicAmnt",sh.getCell(13, row).getContents());
				prodMap.put("unitsOfMeasurement",sh.getCell(6, row).getContents() +"\\\\$"+sh.getCell(7, row).getContents());
				prodMap.put("tax",sh.getCell(12, row).getContents());
				prodMap.put("hsnCd",sh.getCell(8, row).getContents());
				prodMap.put("taxAmnt",sh.getCell(14, row).getContents());
				
				
				prodMap.put("amntAfterTax",strTotalAmnt);
				prodMap.put("otherOrTranportChrgs","0");
				prodMap.put("taxOnOtherOrTranportChrgs","0");
				prodMap.put("otherOrTransportChrgsAfterTax","0");
				prodMap.put("totalAmnt",strTotalAmnt);
				prodMap.put("otherChrgss","0");//quotes placed
				prodMap.put("remarks","");
				prodMapList.add(prodMap);		
			}
			map.put("recordsCount",String.valueOf(prodMapList.size()));
			map.put("chargesRecordsCount","1|");
			
			//	System.out.println(map);
			//	System.out.println(transMapList);
			//	System.out.println(prodMapList);

			response1 = indentProcessCommmon(map,transMapList,prodMapList,request,session);
			String[] response_array = response1.split("@@");
			String response = response_array [0];
			/************************************************************check for po number and inactive*************************************/	


			if (response.equalsIgnoreCase("Success")){
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
			}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}
		return response1;

	}




	public String indentProcessCommmon(Map<String,String> map,List<Map<String,String>> transMapList,List<Map<String,String>> prodMapList,HttpServletRequest request, HttpSession session) {
		//String viewToBeSelected = "";
		String imgname = "_";//"vname_invoiceno_entryid";
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
		String invoiceNum = map.get("invoiceNumber");

		String chargeIGST = "";
		Double chargeIGSTAMT = 0.0;
		Double doubleTaxPercentage = 0.0;
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
		String typeOfPurchase="";

		StringBuffer tblCharges = new StringBuffer();
		double doubleSumOfOtherCharges = 0.0;

		int indentEntrySeqNum = 0;
		Map<String, String> viewGrnPageDataMap = null;
		IndentReceiveDto irdto = null;



		String result = "";
		String strCGSTAMT="";
		String strSGSTAMT="";
		String strIGSTAMT="";

		try {
			viewGrnPageDataMap = new HashMap<String, String>();
			String recordsCount = map.get("recordsCount");
			//logger.info("");
			System.out.println("Records To Be Processed = "+recordsCount);

			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}

			if(numOfRec != null && numOfRec.length > 0) {

				typeOfPurchase=map.get("typeOfPurchase");			
				String poNo = map.get("poNo");
				String dcNo = map.get("dcNo");
				String invoiceNumber = map.get("invoiceNumber");
				String invoiceDate = map.get("invoiceDate");
				String vendorName = map.get("vendorName");

				String vendorId = map.get("vendorId");
				String vendorAddress = map.get("vendorAddress");
				String gstinNumber = map.get("gstinNumber");
				String note = map.get("note");
				String state = map.get("state");
				String ttlAmntForIncentEntryTbl = map.get("ttlAmntForIncentEntryTbl");
				String poDate = map.get("poDate");
				String eWayBillNo = map.get("eWayBillNo");
				String vehileNo = map.get("vehileNo");
				String transporterName = map.get("transporterName");
				String otherChrgs = map.get("otherChrgs");
				String receviedDate = map.get("receviedDate");
				String hsnCode = map.get("hsnCode");
				String indentNumber= map.get("indentNumber");
				logger.info(invoiceNumber+" <--> "+invoiceDate+" <--> "+vendorName+" <--> "+vendorId+" <--> "+vendorAddress+" <--> "+gstinNumber+" <--> "+hsnCode+" <--> "+ttlAmntForIncentEntryTbl+" <--> "+note);

				String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
				int invoiceCount = objExcelUploadProductsReceiveIssueDao.getInvoiceCount(  invoiceNumber,  vendorId, strReceiveStartDate, receviedDate);

				if(invoiceCount == 0){

					IndentReceiveDto objIndentReceiveDto = new IndentReceiveDto();

					//pavan settings objects
					objIndentReceiveDto.setStrInvoiceNo(invoiceNumber);
					objIndentReceiveDto.setStrInoviceDate(invoiceDate);
					objIndentReceiveDto.setStrVendorId(vendorId);
					objIndentReceiveDto.setTotalAmnt(ttlAmntForIncentEntryTbl);
					objIndentReceiveDto.setStrRemarks(note);
					objIndentReceiveDto.setStrReceiveDate(receviedDate);
					objIndentReceiveDto.setTransporterName(transporterName);
					objIndentReceiveDto.seteWayBillNo(eWayBillNo);
					objIndentReceiveDto.setVehileNo(vehileNo);
					objIndentReceiveDto.setPoDate(poDate);
					objIndentReceiveDto.setDcNo(dcNo);
					objIndentReceiveDto.setPoNo(poNo);
					objIndentReceiveDto.setIndentNumber(indentNumber);


					//userId = 1013
					//site_id = 021

					userId = String.valueOf(session.getAttribute("UserId"));
					//logger.info("User Id = "+userId);

					site_id = String.valueOf(session.getAttribute("SiteId"));
					//logger.info("Site Id = "+site_id);
					if (StringUtils.isNotBlank(site_id)){

					} else {
						//transactionManager.rollback(status);
						return result = "SessionFailed";
					}
					indentEntrySeqNum = objExcelUploadProductsReceiveIssueDao.getIndentEntrySequenceNumber();
					session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
					request.setAttribute("indentEntryNo",indentEntrySeqNum);
					imgname=vendorName+"_"+invoiceNumber+"_"+indentEntrySeqNum;

					/*start 01-setp-17*/
					String chargesRecordsCount =  map.get("chargesRecordsCount");

					String numOfChargeRec[] = null;
					if((chargesRecordsCount != null) && (!chargesRecordsCount.equals(""))) {
						numOfChargeRec = chargesRecordsCount.split("\\|");
						int index=0;
						if(numOfChargeRec != null && numOfChargeRec.length > 0) {
							for(String charNum : numOfChargeRec) {
								//int index = Integer.parseInt(charNum)-1;
								String transactionDts = transMapList.get(index).get("transactionDts");
								String transDts[] = transactionDts.split("\\$");
								String transactionId = transDts[0];
								tblChargeName = transDts[1];
								String gstPercentage = transMapList.get(index).get("gstPercentage");
								String arraypercent[] = gstPercentage.split("\\$");
								String gstId = arraypercent[0];
								gstChargePercentage = arraypercent[1];
								gstChargePercentage = gstChargePercentage.replace("%", "").trim();
								gstAmount = transMapList.get(index).get("gstAmount");
								totAmtAfterGSTTax = transMapList.get(index).get("totAmtAfterGSTTax");
								String action=transMapList.get(index).get("action");
								NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

								String totamtaftergst=String.valueOf(totAmtAfterGSTTax);
								double ttamt=Double.parseDouble(totamtaftergst);
								String totAmtAfterGSTTax1=numberFormat.format(ttamt);
								String transactionInvoiceId = transMapList.get(index).get("transactionInvoiceId");
								transAmount = transMapList.get(index).get("transAmount");
								if(action==null || action.equalsIgnoreCase("null") || action.equals("") ||  !action.equals("D") ){
									objExcelUploadProductsReceiveIssueDao.saveTransactionDetails(invoiceNum,transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId, transAmount, site_id, String.valueOf(indentEntrySeqNum));

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
										tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+totAmtAfterGSTTax1+"@@"+"-"+"&&");
									} else {
										tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+chargeIGST+"@@"+chargeIGSTAMT+"@@"+totAmtAfterGSTTax1+"@@"+"-"+"&&");
									}
								}
								index++;
							}

						}
					}
					/*end 01-sept-17*/ 
					logger.info("Indent Entry Seq. Num. = "+indentEntrySeqNum);

					//pavan added object
					objIndentReceiveDto.setStrUserId(userId);
					objIndentReceiveDto.setStrSiteId(site_id);
					if (state.equals("1")) {
						objIndentReceiveDto.setState("Local");
					} else {
						objIndentReceiveDto.setState("Non Local");
					}
					String grn_no = session.getAttribute("SiteName")+objExcelUploadProductsReceiveIssueDao.getindentEntrySerialNo();
					objIndentReceiveDto.setGrnNumber(grn_no);
					int insertReceiveReult = objExcelUploadProductsReceiveIssueDao.insertInvoiceData(indentEntrySeqNum, objIndentReceiveDto);
					logger.info("Insert Requester Reult = "+insertReceiveReult);

					if(insertReceiveReult >= 1) {

						int count = 1;

						StringBuffer tblOneData = new StringBuffer();
						Date grnDate = new Date();
						double totalAmt=Double.valueOf(ttlAmntForIncentEntryTbl);
						totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
						int val = (int) Math.ceil(totalAmt);
						double roundoff=Math.ceil(totalAmt)-totalAmt;
						double grandtotal=Math.ceil(totalAmt);

						int gtotal=(int)grandtotal;
						int total=(int)(totalAmt);
						NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);


						//String strcontotal=numberFormat.format(total);//String.valueOf(totalAmt);
						String strcontotal=numberFormat.format(totalAmt);//String.valueOf(totalAmt);

						String strroundoff=String.format("%.2f",roundoff);
						String strcongrdtotal=numberFormat.format(gtotal);//String.valueOf((int)grandtotal);


						StringBuffer tblTwoData = new StringBuffer();
						int intPriceListSeqNo = 0; 
						int intIndentEntryDetailsSeqNo = 0; 
						int insertIndentReceiveResult = 0;
						int index =0;
						int intNumOfRec = Integer.valueOf(recordsCount);
						for(int  i = 0; i< intNumOfRec; i++){

							irdto = new IndentReceiveDto();

							//num = num.trim();

							// index = Integer.parseInt(num)-1;

							String product = prodMapList.get(i).get("product");				
							String prodsInfo[] = product.split("\\$");					
							String prodId = prodsInfo[0];
							prodId = prodId.replace("\\", "");
							String prodName = prodsInfo[1];
							//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);

							String subProduct = prodMapList.get(index).get("subProduct");	
							String subProdsInfo[] = subProduct.split("\\$");					
							String subProdId = subProdsInfo[0];
							subProdId = subProdId.replace("\\", "");
							String subProdName = subProdsInfo[1];					
							//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
							String expiryDate = prodMapList.get(index).get("expiryDate");	
							String childProduct = prodMapList.get(index).get("childProduct");	
							String childProdsInfo[] = childProduct.split("\\$");					
							String childProdId = childProdsInfo[0];
							childProdId = childProdId.replace("\\", "");
							String childProdName = childProdsInfo[1];
							//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);

							String quantity = prodMapList.get(index).get("quantity");	
							String prc = prodMapList.get(index).get("prc");	
							String basicAmnt = prodMapList.get(index).get("basicAmnt");	

							String unitsOfMeasurement = prodMapList.get(index).get("unitsOfMeasurement");	
							String measurementsInfo[] = unitsOfMeasurement.split("\\$");
							String measurementId = measurementsInfo[0];
							measurementId = measurementId.replace("\\", "");
							String measurementName = measurementsInfo[1];
							//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);

						//	String tax = prodMapList.get(index).get("tax");	
						//	String taxInfo[] = tax.split("\\$");
							String taxId = "";
							String taxPercentage =  prodMapList.get(index).get("tax");
							//logger.info("Tax Id = "+taxId+" and Tax Percentage = "+taxPercentage);
							/*if (taxPercentage.contains("%")) {
								taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);
							}*/

							String hsnCd = prodMapList.get(index).get("hsnCd");	
							String taxAmnt = prodMapList.get(index).get("taxAmnt");	
							String amntAfterTax = prodMapList.get(index).get("amntAfterTax");	
							String otherOrTranportChrgs = prodMapList.get(index).get("otherOrTranportChrgs");	
							String taxOnOtherOrTranportChrgs = prodMapList.get(index).get("taxOnOtherOrTranportChrgs");	
							String otherOrTransportChrgsAfterTax = prodMapList.get(index).get("otherOrTransportChrgsAfterTax");	
							String totalAmnt = prodMapList.get(index).get("totalAmnt");	
							String otherChrgss = request.getParameter(otherChrgs+i);
							String remarks = prodMapList.get(index).get("remarks");	
							String baiscamt=String.valueOf(basicAmnt);
							double baiscat=Double.parseDouble(baiscamt);
							String 	basicAmnt1=numberFormat.format(baiscat);
							//amntAfterTax=numberFormat.format(amntAfterTax);



							//doubleTaxPercentage = (Double.valueOf(taxAmnt)* 100)/Double.valueOf(amntAfterTax);
							//taxPercentage = String.valueOf(doubleTaxPercentage);

							logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+" <--> "+measurementId+" <--> "+measurementName+" <--> "+hsnCd);

							logger.info(product+" <--> "+subProduct+" <--> "+childProduct+" <--> "+quantity+" <--> "+unitsOfMeasurement+" <--> "+prc+" <--> "+basicAmnt+" <--> "+taxId+" <--> "+taxPercentage+"<-->"+taxAmnt+"<-->"+amntAfterTax+"<-->"+otherOrTranportChrgs+"<-->"+taxOnOtherOrTranportChrgs+"<-->"+otherOrTransportChrgsAfterTax+"<-->"+totalAmnt+"<-->"+otherChrgs);
							if (StringUtils.isNotBlank(prodId) && StringUtils.isNotBlank(subProdId) && StringUtils.isNotBlank(childProdId)) {

							} else {
								//transactionManager.rollback(status);
								return result = "Failed";
							}
							irdto.setProdId(prodId);
							irdto.setProdName(prodName);
							irdto.setSubProdId(subProdId);
							irdto.setSubProdName(subProdName);
							irdto.setChildProdId(childProdId);
							irdto.setChildProdName(childProdName);
							//irdto.setPrice(prc);
							irdto.setBasicAmnt(basicAmnt);
							//irdto.setQuantity(quantity);
							//irdto.setMeasurementId(measurementId);
							//irdto.setMeasurementName(measurementName);
							irdto.setTax(taxPercentage);
							irdto.setHsnCd(hsnCd);
							irdto.setTaxAmnt(taxAmnt);
							irdto.setAmntAfterTax(amntAfterTax);
							irdto.setOtherOrTranportChrgs(otherOrTranportChrgs);
							irdto.setTaxOnOtherOrTranportChrgs(taxOnOtherOrTranportChrgs);
							irdto.setOtherOrTransportChrgsAfterTax(otherOrTransportChrgsAfterTax);
							irdto.setTotalAmnt(totalAmnt);
							irdto.setOtherChrgs(otherChrgss);
							irdto.setPoNo(poNo);
							irdto.setStrRemarks(remarks);
							//irdto.setDcNo(dcNo);
							//irdto.setDate(invoiceDate);
							irdto.setDate(receviedDate);
							irdto.setExpiryDate(expiryDate);
							//irdto.setPoDate(poDate);
							//irdto.seteWayBillNo(eWayBillNo);
							//irdto.setVehileNo(vehileNo);
							//irdto.setTransporterName(transporterName);



							doubleSumOfOtherCharges = doubleSumOfOtherCharges + Double.valueOf(otherOrTransportChrgsAfterTax);

							//tblTwoData.append(count+"@@"+subProdName+" "+childProdName+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+prc+"@@"+basicAmnt+"@@"+"-"+"&&");
							if (state.equals("1")) {
								/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/
								if (taxPercentage.equals("0")) {
									CGST = "0";
									SGST = "0";
									strCGSTAMT="0";
									strSGSTAMT="0";
								} else {
									percent = Double.parseDouble(taxPercentage)/2;
									amt = Double.parseDouble(taxAmnt)/2;
									CGSTAMT = amt;
									SGSTAMT = amt;
									CGST = String.valueOf(percent);
									SGST = String.valueOf(percent);
									strCGSTAMT=String.format("%.2f",CGSTAMT);
									strSGSTAMT=String.format("%.2f",SGSTAMT);
								}
							} else {
								percent = Double.parseDouble(taxPercentage);
								amt = Double.parseDouble(taxAmnt);
								IGST = String.valueOf(percent);

								IGSTAMT = amt;
								strIGSTAMT=String.format("%.2f",IGSTAMT);
							}
							if (state.equals("1")) {
								tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+basicAmnt1+"@@"+CGST+"@@"+strCGSTAMT+"@@"+SGST+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amntAfterTax+"@@"+remarks+"@@"+"-"+"&&");
							} else {
								tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+basicAmnt1+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+strIGSTAMT+"@@"+amntAfterTax+"@@"+remarks+"@@"+"-"+"&&");
								//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
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
								values = (Map<String, String>) printDogMethod.invoke(mesurment,basicAmnt,doubleActualQuantity,doubleInputQuantity,measurementName);

								for(Map.Entry<String, String> retVal : values.entrySet()) {
									BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
									quantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
									//quantity=retVal.getKey();
									prc=retVal.getValue(); 
								}	
								//quantity =  String.valueOf(doubleQuantity);
								/*if(measurementName.equalsIgnoreCase("PCS")){

									double price=Double.parseDouble(basicAmnt)/doubleQuantity;
									prc=String.valueOf(price);
								}*/
								measurementId = strConversionMesurmentId;
								measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
							}
							irdto.setQuantity(quantity);
							irdto.setMeasurementId(measurementId);
							irdto.setMeasurementName(measurementName);
							irdto.setPrice(prc);

							//public  double convertCHP0016(double doubleActualQuantity,double inputQuantity,String inputMesurment)


							intPriceListSeqNo = objExcelUploadProductsReceiveIssueDao.getPriceList_SeqNumber(); 
							intIndentEntryDetailsSeqNo = objExcelUploadProductsReceiveIssueDao.getIndentEntryDtails_SeqNumber(); 
							insertIndentReceiveResult = objExcelUploadProductsReceiveIssueDao.insertIndentReceiveData(indentEntrySeqNum, intIndentEntryDetailsSeqNo,irdto, userId, site_id,intPriceListSeqNo);


							if(insertIndentReceiveResult >= 1) {
								int updateIndentAvalibilityResult = objExcelUploadProductsReceiveIssueDao.updateIndentAvalibility(irdto, site_id);
								//viewToBeSelected = "viewGRN";
								result = "Success";
								//Making a new entry if new product.
								if(updateIndentAvalibilityResult == 0) {
									objExcelUploadProductsReceiveIssueDao.updateIndentAvalibilityWithNewIndent(irdto, site_id);
									//viewToBeSelected = "viewGRN";
									result = "Success";
								}
								//27-July-2017 added by Madhu start
								String id = objExcelUploadProductsReceiveIssueDao.getIndentAvailableId(irdto, site_id);
								if (StringUtils.isNotBlank(id)) {
									objExcelUploadProductsReceiveIssueDao.saveReciveDetailsIntoSumduraPriceList(irdto, invoiceNumber, site_id, id,intIndentEntryDetailsSeqNo,intPriceListSeqNo,typeOfPurchase);
								} else {
									String id1 = objExcelUploadProductsReceiveIssueDao.getProductAvailabilitySequenceNumber();
									objExcelUploadProductsReceiveIssueDao.saveReciveDetailsIntoSumduraPriceList(irdto, invoiceNumber, site_id, id1,intIndentEntryDetailsSeqNo,intPriceListSeqNo,typeOfPurchase);
								}
								//ird.saveReciveDetailsIntoSumadhuraCloseBalance(irdto, site_id);
								//objExcelUploadProductsReceiveIssueDao.saveReceivedDataIntoSumadhuClosingBalByProduct(irdto, site_id);
								//27-July-2017 added by Madhu end




							}
							else {
								request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
								//viewToBeSelected = "indentReceiveResponse";
								//transactionManager.rollback(status);
								return result = "Failed";
							}
							count++;
							index++;
						}
						String strUserName  = String.valueOf(session.getAttribute("UserName"));
						SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//dd/MM/yyyy
						Date now = new Date();
						String strDate = sdfDate.format(now);

						tblOneData.append(gstinNumber+"@@"+changeDateFormat(grnDate)+"@@"+invoiceNumber+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+strcontotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strcongrdtotal+"@@"+strUserName+"@@"+strDate);

						viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
						//start 03-sept
						String NamesOfCharges =   "";//tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
						viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
						//end 03-sept

						String fnlStr = ""; //tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
						viewGrnPageDataMap.put("tblTwoData", fnlStr);
						request.setAttribute("viewGrnPageData", viewGrnPageDataMap);
						//transactionManager.commit(status);
					}
					else {
						request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
						//viewToBeSelected = "indentReceiveResponse";
						//transactionManager.rollback(status);
						return result = "Failed";
					}

				}//invoice validation
				else {
					request.setAttribute("exceptionMsg", "This Invoice already exist");
					//viewToBeSelected = "indentReceiveResponse";
					//transactionManager.rollback(status);
					return result = "Failed";
				}
			}


			//invoice trans charges need to save

			else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				//viewToBeSelected = "indentReceiveResponse";
				//transactionManager.rollback(status);
				//return result = "Failed";
			}
		}
		catch (Exception e) {
			//transactionManager.rollback(status);
			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
			//viewToBeSelected = "indentReceiveResponse";
			result = "Failed";
			/*	AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
			auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
			auditBean.setLoginId(userId);
			auditBean.setOperationName("New Recive");
			auditBean.setStatus("error");
			auditBean.setSiteId(site_id);
			new SaveAuditLogDetails().saveAuditLogDetails(auditBean);*/
			e.printStackTrace();
		}
		//logger.info("Final View To Be Selected = "+viewToBeSelected);
		return result+"@@"+imgname;
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

		System.out.println(175* 100/500);
	}
}
