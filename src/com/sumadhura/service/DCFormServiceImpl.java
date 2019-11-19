package com.sumadhura.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.DCFormViewBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.CreditNoteDto;
import com.sumadhura.dto.DCFormDto;
import com.sumadhura.dto.DCToInvoiceDto;
import com.sumadhura.transdao.DCFormDao;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.transdao.InwardGetInvoiceDetailsDao;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.UIProperties;

@Service("dcFormClass")
public class DCFormServiceImpl extends UIProperties implements DCFormService {

	@Autowired
	private IndentReceiveDao ird;
	@Autowired
	private InwardGetInvoiceDetailsDao inwardGetInvoiceDetailsClass;
	
	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;
	

	@Autowired
	private DCFormDao dcFormDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Override
	public Map<String, String> loadProds(String siteId) {
		return dcFormDao.loadProds(siteId);
	}

	@Override
	public Map<String, String> loadProdsByPONumber(String poNumber, String reqSiteId) {
		return dcFormDao.loadProdsByPONumber(poNumber,reqSiteId);
	}

	@Override
	public String loadSubProds(String prodId) {
		return dcFormDao.loadSubProds(prodId);
	}

	@Override
	public String loadChildProds(String subProductId) {
		return dcFormDao.loadChildProds(subProductId);
	}

	@Override
	public String loadDCFormMeasurements(String childProdId) {
		return dcFormDao.loadDCFormMeasurements(childProdId);
	}

	@Override
	public Map<String, String> getGSTSlabs() {
		return dcFormDao.getGSTSlabs();
	}




	@Override
	public String getVendorInfo(String vendName) {
		return dcFormDao.getVendorInfo(vendName);
	}
	@Override
	public List<Map<String, Object>> getVendorInfoByID(String vendorId) {
		return dcFormDao.getVendorInfoByID(vendorId);
	}

	@Override
	public Map<String, String> getOtherCharges() {
		return dcFormDao.getOtherCharges();
	}

	@Override
	public int insertDCFormData(int dcformSeqNum,DCFormDto dcFormReceiveDto) throws Exception {
		return dcFormDao.insertDCFormData(dcformSeqNum, dcFormReceiveDto);
	}


	@Override
	public int updateDCFormAvalibility(DCFormDto dcFormDto, String site_id) {
		return dcFormDao.updateIndentAvalibility(dcFormDto,site_id);
	}

	@Override
	public void updateDCFormWithNewDCForm(DCFormDto irdto, String siteId) {
		// TODO Auto-generated method stub

	}
	@Override
	public String dcFormProcess(Model model, HttpServletRequest request, HttpSession session) {
		String response = "";
		String prod = "Product";
		String subProd = "SubProduct";
		String childProd = "ChildProduct";
		String qty = "Quantity";
		String expireDate = "expireDate";
		String price = "Price";
		String basicAmount = "BasicAmount";
		String measurements = "UnitsOfMeasurement";				
		String gstTax = "Tax";
		String hsnCode = "HSNCode";
		String taxAmount = "TaxAmount";				
		String amountAfterTax = "AmountAfterTaxId";				
		String otherOrTransportCharges = "OtherOrTransportCharges";				
		String taxOnOtherOrTransportCharges = "TaxOnOtherOrTransportCharges";				
		String otherOrTransportChargesAfterTax = "OtherOrTransportChargesAfterTax";				
		String totalAmount = "TotalAmount";
		String otherCharges = "OtherCharges";
		
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in DcFo_dcFPro, ");
		WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , DCNumber:"+request.getParameter("DCNumber"));
		
		try {
			
			//String typeOfPurchase=request.getParameter("typeOfPurchase");
			map.put("typeOfPurchase",request.getParameter("typeOfPurchase"));
			map.put("dcNo",request.getParameter("DCNumber"));
			map.put("vendorId",request.getParameter("VendorId"));
			map.put("receviedDate",request.getParameter("receivedDate"));
			map.put("invoiceNum",request.getParameter("InvoiceNumber"));
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");/**/
			map.put("recordsCount",recordsCount);
			map.put("poNo",request.getParameter("poNo"));
			map.put("dcDate",request.getParameter("DCDate"));
			map.put("vendorName",request.getParameter("VendorName"));
			map.put("vendorAddress",request.getParameter("VendorAddress"));
			map.put("gstinNumber",request.getParameter("GSTINNumber"));
			map.put("note",request.getParameter("Note"));
			map.put("state",request.getParameter("state"));
			map.put("ttlAmntForIncentEntryTbl",request.getParameter("ttlAmntForIncentEntry"));
			map.put("poDate",request.getParameter("poDate"));
			map.put("eWayBillNo",request.getParameter("eWayBillNo"));
			map.put("vehileNo",request.getParameter("vehileNo"));
			map.put("transporterName",request.getParameter("transporterName"));
			map.put("otherChrgs",request.getParameter(otherCharges));
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");/**/
			map.put("chargesRecordsCount",transRows);
			
			int trans_rows = transRows.split("\\|").length;
			for(int charNum=1;charNum<=trans_rows;charNum++){
			Map<String,String> transMap = new HashMap<String,String>();
			transMap.put("transactionDts",request.getParameter("Conveyance"+charNum));
			transMap.put("gstPercentage",request.getParameter("GSTTax"+charNum));
			transMap.put("gstAmount",request.getParameter("GSTAmount"+charNum));
			transMap.put("totAmtAfterGSTTax",request.getParameter("AmountAfterTax"+charNum));
			transMap.put("transactionInvoiceId",request.getParameter("TransportInvoice"+charNum));
			transMap.put("transAmount",request.getParameter("ConveyanceAmount"+charNum));
			transMapList.add(transMap);
			}
							
			int records_Count = recordsCount.split("\\|").length;
			for(int num=1;num<=records_Count;num++){
			
			Map<String,String> prodMap = new HashMap<String,String>();
			prodMap.put("product",request.getParameter(prod+num));
			prodMap.put("subProduct",request.getParameter(subProd+num));
			prodMap.put("expiryDate",request.getParameter(expireDate+num));
			prodMap.put("childProduct",request.getParameter(childProd+num));
			
			prodMap.put("quantity",request.getParameter(qty+num));
			prodMap.put("prc",request.getParameter(price+num));
			prodMap.put("basicAmnt",request.getParameter(basicAmount+num));

			prodMap.put("unitsOfMeasurement",request.getParameter(measurements+num));
			prodMap.put("tax",request.getParameter(gstTax+num));
			prodMap.put("hsnCd",request.getParameter(hsnCode+num));
			prodMap.put("taxAmnt",request.getParameter(taxAmount+num));
			prodMap.put("amntAfterTax",request.getParameter(amountAfterTax+num));
			prodMap.put("otherOrTranportChrgs",request.getParameter(otherOrTransportCharges+num));
			prodMap.put("taxOnOtherOrTranportChrgs",request.getParameter(taxOnOtherOrTransportCharges+num));
			prodMap.put("otherOrTransportChrgsAfterTax",request.getParameter(otherOrTransportChargesAfterTax+num));
			prodMap.put("totalAmnt",request.getParameter(totalAmount+num));
			prodMap.put("otherChrgss",request.getParameter("OtherCharges"+num));
			prodMapList.add(prodMap);	
			}
			System.out.println(map);
			System.out.println(transMapList);
			System.out.println(prodMapList);
			response = dcFormProcessCommon(map,transMapList,prodMapList,request,session);
			if(response.equals("Success"))
			{
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
			}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}
		return response;
	}
	@SuppressWarnings("null")
	public String dcFormProcessCommon(Map<String,String> map,List<Map<String,String>> transMapList,List<Map<String,String>> prodMapList,HttpServletRequest request, HttpSession session) {

		String dcNo = map.get("dcNo");
		String vendorId = map.get("vendorId");
		String receviedDate = map.get("receviedDate");
		int dcCount = dcFormDao.getDCCount( dcNo, vendorId, receviedDate );
		if(dcCount>0)
			return "existed";

		//String viewToBeSelected = "";
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
		String invoiceNum = map.get("invoiceNum");
		String transportId ="Conveyance";
		String transportGSTPercentage = "GSTTax";
		String transportGSTAmount = "GSTAmount";
	//	String totalAmountAfterGSTTax = "TransportChargesAmountAfterTax";
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
		String typeOfPurchase="";

		//int indent_entry_id = 0;
		int dc_entry_id = 0;
		Map<String, String> viewGrnPageDataMap = null;
		DCFormDto dcFormDto = null;
		
		String trasportorId="";
		String strTrsnportorName="";
		String result = "";

		try {
			viewGrnPageDataMap = new HashMap<String, String>();
			String recordsCount = map.get("recordsCount");
			logger.info("Records To Be Processed = "+recordsCount);

			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}

			if(numOfRec != null && numOfRec.length > 0) {

				String prod = "Product";
				String subProd = "SubProduct";
				String childProd = "ChildProduct";
				String qty = "Quantity";
				String expireDate = "expireDate";
				String price = "Price";
				String basicAmount = "BasicAmount";
				String measurements = "UnitsOfMeasurement";				
				String gstTax = "Tax";
				String hsnCode = "HSNCode";
				String taxAmount = "TaxAmount";				
				String amountAfterTax = "AmountAfterTaxId";				
				String otherOrTransportCharges = "OtherOrTransportCharges";				
				String taxOnOtherOrTransportCharges = "TaxOnOtherOrTransportCharges";				
				String otherOrTransportChargesAfterTax = "OtherOrTransportChargesAfterTax";				
				String totalAmount = "TotalAmount";
				String otherCharges = "OtherCharges";				
				String poNo = map.get("poNo");
				//String dcNo = request.getParameter("DCNumber");
				//				String invoiceNumber = request.getParameter("InvoiceNumber");
				String dcDate = map.get("dcDate");
				String vendorName = map.get("vendorName");
				DCFormDto dcformdto = new DCFormDto();
				//String vendorId = request.getParameter("VendorId");
				String vendorAddress = map.get("vendorAddress");
				String gstinNumber = map.get("gstinNumber");
				String note = map.get("note");
				String state = map.get("state");
				System.out.println("state: "+state);
				String ttlAmntForIncentEntryTbl = map.get("ttlAmntForIncentEntryTbl");
				String poDate = map.get("poDate");
				String eWayBillNo = map.get("eWayBillNo");
				String vehileNo = map.get("vehileNo");
				String transporterName = map.get("transporterName");
				String otherChrgs = map.get("otherChrgs");
				 typeOfPurchase=map.get("typeOfPurchase");
				
				//String receviedDate = request.getParameter("receivedDate");
				logger.info(dcNo+" <--> "+dcDate+" <--> "+vendorName+" <--> "+vendorId+" <--> "+vendorAddress+" <--> "+gstinNumber+" <--> "+hsnCode+" <--> "+ttlAmntForIncentEntryTbl+" <--> "+note);
				String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
				//	int getDcCount = dcFormDao.getInvoiceCount(  dcNo,  vendorId, "01-10-17",  receviedDate);
				//if(getDcCount == 0){
				if(transporterName.contains("$")){
					
					 trasportorId=transporterName.split("\\$")[0];
					 strTrsnportorName=transporterName.split("\\$")[1];
				}else{
					trasportorId=strTrsnportorName=transporterName;
				}
				
				dcformdto.setStrDcDate(dcDate);
				dcformdto.setStrVendorId(vendorId);
				dcformdto.setTotalAmnt(ttlAmntForIncentEntryTbl);
				dcformdto.setStrRemarks(note);
				dcformdto.setStrReceiveDate(receviedDate);
				dcformdto.setTransporterName(trasportorId);
				dcformdto.seteWayBillNo(eWayBillNo);
				dcformdto.setVehileNo(vehileNo);
				dcformdto.setPoDate(poDate);
				dcformdto.setDcNo(dcNo);
				dcformdto.setPoNo(poNo);
				//dcformdto.setStrRemarks(strRemarks);

				//userId = 1013
				//site_id = 021

				userId = String.valueOf(session.getAttribute("UserId"));
				//logger.info("User Id = "+userId);

				site_id = String.valueOf(session.getAttribute("SiteId"));
				//logger.info("Site Id = "+site_id);
				if (StringUtils.isNotBlank(site_id)){

				} else {
					
					return result = "SessionFailed";
				}
				//indent_entry_id = dcFormDao.getIndentEntrySequenceNumber();
				dc_entry_id = dcFormDao.getDCEntrySequenceNumber();
                session.setAttribute("indentEntrySeqNum",dc_entry_id);
                request.setAttribute("dcEntryNo",dc_entry_id);

				/*start 01-setp-17*/
				String chargesRecordsCount =  map.get("chargesRecordsCount");
				System.out.println("chargesRecordsCount: "+chargesRecordsCount);
				String numOfChargeRec[] = null;
				if((chargesRecordsCount != null) && (!chargesRecordsCount.equals(""))) {
					numOfChargeRec = chargesRecordsCount.split("\\|");
					if(numOfChargeRec != null && numOfChargeRec.length > 0) {
						for(String charNum : numOfChargeRec) {
							int index = Integer.parseInt(charNum)-1;
							String transactionDts = transMapList.get(index).get("transactionDts");
							System.out.println("transactionDts: "+transactionDts);
							String transDts[] = transactionDts.split("\\$");
							String transactionId = transDts[0];
							tblChargeName = transDts[1];
							String gstPercentage = transMapList.get(index).get("gstPercentage");
							String arraypercent[] = gstPercentage.split("\\$");
							String gstId = arraypercent[0];
							gstChargePercentage = arraypercent[1];
							gstChargePercentage = gstChargePercentage.replace("%", "").trim();
							gstAmount = transMapList.get(index).get("gstAmount");
							System.out.println("gstAmount: "+gstAmount);
							totAmtAfterGSTTax = transMapList.get(index).get("totAmtAfterGSTTax");
							double aftergstamt = Double.parseDouble(totAmtAfterGSTTax);
							
							NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
							String totAmtAfterGSTTax1=numberFormat.format(aftergstamt);
							String transactionInvoiceId = transMapList.get(index).get("transactionInvoiceId");
							transAmount = transMapList.get(index).get("transAmount");

							dcFormDao.saveTransactionDetails(dcNo,transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId, transAmount, site_id, String.valueOf(dc_entry_id));

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
							System.out.println("state: "+state);
							if (state.equals("1")) {
								tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+totAmtAfterGSTTax1+"@@"+"-"+"&&");
							} else {
								tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+chargeIGST+"@@"+chargeIGSTAMT+"@@"+totAmtAfterGSTTax1+"@@"+"-"+"&&");
							}
						}

					}
				}
				/*end 01-sept-17*/ 
				logger.info("DC Form Seq. Num. = "+dc_entry_id);

				dcformdto.setStrUserId(userId);
				dcformdto.setStrSiteId(site_id);
				if (state.equals("1")) {
					dcformdto.setState("Local");
				} else {
					dcformdto.setState("Non Local");
				}
				String grn_no = session.getAttribute("SiteName")+ird.getindentEntrySerialNo(receviedDate);
				dcformdto.setDcGrnNumber(grn_no);
				int insertDceReult = dcFormDao.insertDCFormData(dc_entry_id, dcformdto);
				logger.info("Insert Requester Reult = "+insertDceReult);

				if(insertDceReult >= 1) {

					int count = 1;

					StringBuffer tblOneData = new StringBuffer();
					Date grnDate = new Date();
					double totalAmt=Double.valueOf(ttlAmntForIncentEntryTbl);
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=Math.ceil(totalAmt);
					
					
							int total=(int)totalAmt;
							int gtotal=(int)grandtotal;
					NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
					
					
					String strtotal=String.valueOf(totalAmt);
					//String strcontotal=numberFormat.format(total);
					String strcontotal=numberFormat.format(totalAmt);
					String strroundoff=String.format("%.2f",roundoff);
					String strgrandtotal=String.valueOf((int)grandtotal);
					String strcongrdtotal=numberFormat.format(gtotal);
					


					StringBuffer tblTwoData = new StringBuffer();
					int intPriceListSeqNo = 0;
					int intDCFormSeqNo = 0;
					int insertDcformResult = 0;
				
					for(String num : numOfRec) {

						dcformdto = new DCFormDto();

						num = num.trim();
						
						int index = Integer.parseInt(num)-1;

						String product = prodMapList.get(index).get("product");					
						String prodsInfo[] = product.split("\\$");					
						String prodId = prodsInfo[0];
						String prodName = prodsInfo[1];
						//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);

						String subProduct = prodMapList.get(index).get("subProduct");

						if(subProduct == null){
							subProduct = request.getParameter("subProduct"+num);
						}

						String subProdsInfo[] = subProduct.split("\\$");					
						String subProdId = subProdsInfo[0];
						String subProdName = subProdsInfo[1];					
						//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
						String expiryDate = prodMapList.get(index).get("expiryDate");
						String childProduct = prodMapList.get(index).get("childProduct");
						if(childProduct == null){
							childProduct = request.getParameter("childProduct"+num);
						}


						String childProdsInfo[] = childProduct.split("\\$");					
						String childProdId = childProdsInfo[0];
						String childProdName = childProdsInfo[1];
						//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);

						String quantity = prodMapList.get(index).get("quantity");
						String prc = prodMapList.get(index).get("prc");
						String basicAmnt = prodMapList.get(index).get("basicAmnt");

						String unitsOfMeasurement = prodMapList.get(index).get("unitsOfMeasurement");
						String measurementsInfo[] = unitsOfMeasurement.split("\\$");
						String measurementId = measurementsInfo[0];
						String measurementName = measurementsInfo[1];
						//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);
						String strRemarks = prodMapList.get(index).get("InvoiceRemarks");
						String tax = prodMapList.get(index).get("tax");
						String taxId =  "";
						String taxPercentage = "";
						if(tax.contains("$")){
							String taxInfo[] = tax.split("\\$");
							taxId = taxInfo[0];
							taxPercentage = taxInfo[1];
						}
						//logger.info("Tax Id = "+taxId+" and Tax Percentage = "+taxPercentage);


						if (taxPercentage.contains("%")) {
							taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);
						}

						String hsnCd = prodMapList.get(index).get("hsnCd");
						String taxAmnt = prodMapList.get(index).get("taxAmnt");
						String amntAfterTax = prodMapList.get(index).get("amntAfterTax");
						String otherOrTranportChrgs = prodMapList.get(index).get("otherOrTranportChrgs");
						String taxOnOtherOrTranportChrgs = prodMapList.get(index).get("taxOnOtherOrTranportChrgs");
						String otherOrTransportChrgsAfterTax = prodMapList.get(index).get("otherOrTransportChrgsAfterTax");
						String totalAmnt = prodMapList.get(index).get("totalAmnt");
						String otherChrgss = prodMapList.get(index).get("otherChrgss");
						String amtaftertax2=String.valueOf(amntAfterTax);
						String basicamt2=String.valueOf(basicAmnt);
					double baiscamt=Double.parseDouble(basicamt2);
					double totalamt=Double.parseDouble(amtaftertax2);
				String	basicAmnt1=numberFormat.format(baiscamt);
					String amntAfterTax1=numberFormat.format(totalamt);	
						logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+" <--> "+measurementId+" <--> "+measurementName+" <--> "+hsnCd);

						logger.info(product+" <--> "+subProduct+" <--> "+childProduct+" <--> "+quantity+" <--> "+unitsOfMeasurement+" <--> "+prc+" <--> "+basicAmnt+" <--> "+taxId+" <--> "+taxPercentage+"<-->"+taxAmnt+"<-->"+amntAfterTax+"<-->"+otherOrTranportChrgs+"<-->"+taxOnOtherOrTranportChrgs+"<-->"+otherOrTransportChrgsAfterTax+"<-->"+totalAmnt+"<-->"+otherChrgs);
						if (StringUtils.isNotBlank(prodId) && StringUtils.isNotBlank(subProdId) && StringUtils.isNotBlank(childProdId)) {

						} else {
							return result = "Failed";
						}


						if(dcformdto !=null){
							dcformdto.setProdId(prodId);
							dcformdto.setProdName(prodName);
							dcformdto.setSubProdId(subProdId);
							dcformdto.setSubProdName(subProdName);
							dcformdto.setChildProdId(childProdId);
							dcformdto.setChildProdName(childProdName);
							//dcformdto.setQuantity(quantity);
							//dcformdto.setPrice(prc);
							dcformdto.setBasicAmnt(basicAmnt);
							dcformdto.setMeasurementId(measurementId);
							//dcformdto.setMeasurementName(measurementName);
							dcformdto.setTax(taxPercentage);
							dcformdto.setHsnCd(hsnCd);
							dcformdto.setTaxAmnt(taxAmnt);
							dcformdto.setAmntAfterTax(amntAfterTax);
							dcformdto.setOtherOrTranportChrgs(otherOrTranportChrgs);
							dcformdto.setTaxOnOtherOrTranportChrgs(taxOnOtherOrTranportChrgs);
							dcformdto.setOtherOrTransportChrgsAfterTax(otherOrTransportChrgsAfterTax);
							dcformdto.setTotalAmnt(totalAmnt);
							dcformdto.setOtherChrgs(otherChrgss);
							dcformdto.setPoNo(poNo);
							//							dcFormDto.setDcNo(dcNo);
							//							dcFormDto.setDate(dcDate);
							dcformdto.setDate(receviedDate);
							dcformdto.setExpiryDate(expiryDate);
							//irdto.setPoDate(poDate);
							//irdto.seteWayBillNo(eWayBillNo);
							//irdto.setVehileNo(vehileNo);
							//irdto.setTransporterName(transporterName);
							dcformdto.setDcNo(dcNo);
							dcformdto.setStrDcDate(dcDate);
							dcformdto.setStrRemarks(strRemarks);
							
						}


						if(otherOrTransportChrgsAfterTax != null && !otherOrTransportChrgsAfterTax.equals("")){
							doubleSumOfOtherCharges = doubleSumOfOtherCharges + Double.valueOf(otherOrTransportChrgsAfterTax);
						}else{
							otherOrTransportChrgsAfterTax = "";
						}

						//tblTwoData.append(count+"@@"+subProdName+" "+childProdName+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+prc+"@@"+basicAmnt+"@@"+"-"+"&&");

						if (state.equals("1")) {
							/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/

							if (taxPercentage.equals("0") && taxPercentage !=null && !taxPercentage.equals("")) {
								CGST = "0";
								SGST = "0";							
							} else if(taxPercentage !=null && !taxPercentage.equals("")){
								percent = Double.parseDouble(taxPercentage)/2;
								amt = Double.parseDouble(taxAmnt)/2;
								CGSTAMT = amt;
								SGSTAMT = amt;
								CGST = SGST = String.valueOf(percent);//String.valueOf((Double.valueOf(gstAmount)/2));

							}
						} else if(taxPercentage !=null && !taxPercentage.equals("")){
							percent = Double.parseDouble(taxPercentage);
							amt = Double.parseDouble(taxAmnt);
							IGST = String.valueOf(percent);
							IGSTAMT = amt;
						}
						if (state.equals("1")) {
							tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+basicAmnt1+"@@"+CGST+"@@"+CGSTAMT+"@@"+SGST+"@@"+SGSTAMT+"@@"+""+"@@"+""+"@@"+amntAfterTax1+"@@"+strRemarks+"@@"+"-"+"&&");
							System.out.println(basicAmnt+","+CGST+","+CGSTAMT+","+SGST+","+SGSTAMT);
						} else {
							tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+basicAmnt1+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+IGSTAMT+"@@"+amntAfterTax1+"@@"+strRemarks+"@@"+"-"+"&&");
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
							/*doubleQuantity = */values=(Map<String, String>) printDogMethod.invoke(mesurment,basicAmnt, doubleActualQuantity,doubleInputQuantity,measurementName);	            
							for(Map.Entry<String, String> retVal : values.entrySet()) {
								BigDecimal bigDecimal = new BigDecimal(retVal.getKey());
								quantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
								//quantity=retVal.getKey();
								prc=retVal.getValue(); 
							}	
							//quantity =  String.valueOf(doubleQuantity);
							measurementId = strConversionMesurmentId;
							measurementName = validateParams.getProperty(childProdId+"IDMNAME") == null ? "" : validateParams.getProperty(childProdId+"IDMNAME").toString();
						}
						dcformdto.setMeasurementId(measurementId);
						dcformdto.setQuantity(quantity);
						dcformdto.setMeasurementName(measurementName);
						dcformdto.setPrice(prc);



						//						public  double convertCHP0016(double doubleActualQuantity,double inputQuantity,String inputMesurment)
						 intPriceListSeqNo = ird.getPriceList_SeqNumber();
						 intDCFormSeqNo = dcFormDao.getDCFormSequenceNumber();
						 insertDcformResult = dcFormDao.insertDCFormReceiveData(dc_entry_id,intDCFormSeqNo, dcformdto, userId, site_id,intPriceListSeqNo);
						if(insertDcformResult >= 1) {
							int updateDCFormAvalibilityResult = dcFormDao.updateIndentAvalibility(dcformdto, site_id);
							//viewToBeSelected = "viewGRN";
							result = "Success";
							//Making a new entry if new product.
							if(updateDCFormAvalibilityResult == 0) {
								dcFormDao.updateIndentAvailabulityWithNewDCForm(dcformdto, site_id);
								//viewToBeSelected = "viewGRN";
								//result = "Success";
							}

							String id = dcFormDao.getIndentAvailableId(dcformdto, site_id);
							if (StringUtils.isNotBlank(id)) {
								dcFormDao.saveReciveDetailsIntoSumduraPriceList(dcformdto, dcNo, site_id, id,intDCFormSeqNo,intPriceListSeqNo,typeOfPurchase);
							} else {
								String id1 = dcFormDao.getProductAvailabilitySequenceNumber();
								dcFormDao.saveReciveDetailsIntoSumduraPriceList(dcformdto, dcNo, site_id, id1,intDCFormSeqNo,intPriceListSeqNo,typeOfPurchase);
							}
							//ird.saveReciveDetailsIntoSumadhuraCloseBalance(irdto, site_id);
							dcFormDao.saveReceivedDataIntoSumadhuClosingBalByProduct(dcformdto, site_id);
						}
						else {
							request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");	
							//viewToBeSelected = "indentReceiveResponse";
							return result = "Failed";							
						}
						count++;
					}

					// user341 start
					String strUserName  = String.valueOf(session.getAttribute("UserName"));
					SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//dd/MM/yyyy
         		    Date now = new Date();
         		    String strDate = sdfDate.format(now);
         		    
					tblOneData.append(gstinNumber+"@@"+changeDateFormat(grnDate)+"@@"+dcNo+"@@"+dcDate+"@@"+"Sumadhura"+"@@"+strcontotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+strTrsnportorName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strcongrdtotal+"@@"+strUserName+"@@"+strDate);
					System.out.println(tblCharges.toString()+","+tblOneData.toString()+","+tblOneData.toString());

					viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
					//start 03-sept
					String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
					viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
					//end 03-sept

					String fnlStr = tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
					viewGrnPageDataMap.put("tblTwoData", fnlStr);
					request.setAttribute("viewGrnPageData", viewGrnPageDataMap);
					//user341 end
					result = "Success";
				}
				else {
					request.setAttribute("exceptionMsg", "Exception occured while processing the DCForm");
					//viewToBeSelected = "indentReceiveResponse";
					return result = "Failed";
				}
				/*}else {
					request.setAttribute("exceptionMsg", "This DC is already entered");
					//viewToBeSelected = "indentReceiveResponse";
					result = "Failed";
				}
				 */
			}
			else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				//viewToBeSelected = "indentReceiveResponse";
				return result = "Failed";
			}
		}
		catch (Exception e) {
			request.setAttribute("exceptionMsg", "Exception occured while processing the DCForm.");
			//viewToBeSelected = "indentReceiveResponse";
			result = "Failed";
		/*	AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
			auditBean.setEntryDetailsId(String.valueOf(indent_entry_id));
			auditBean.setLoginId(userId);
			auditBean.setOperationName("New Recive");
			auditBean.setStatus("error");
			auditBean.setSiteId(site_id);
			new SaveAuditLogDetails().saveAuditLogDetails(auditBean);*/
			e.printStackTrace();
		}
		//logger.info("Final View To Be Selected = "+viewToBeSelected);
		return result;

	}

	@Override
	public List<DCFormViewBean> getDcFormLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId) {
		List<DCFormViewBean> listOfDcFormDetails = null;

		try {

			listOfDcFormDetails = dcFormDao.getDcFormDetails(session,dCNumber,siteId,strVendorId,strIndentEntryId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfDcFormDetails;
	}

	@Override
	public List<DCFormViewBean> getDcFormProductDetailsLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId) {
		List<DCFormViewBean> listOfDcFormProductDetailsList = null;

		try {

			listOfDcFormProductDetailsList  = dcFormDao.getGetProductDetailsLists(session,dCNumber, siteId,strVendorId,strIndentEntryId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfDcFormProductDetailsList ;
	}

	@Override
	public List<DCFormViewBean> getDcFormTransportChargesList(String dCNumber, String siteId) {
		List<DCFormViewBean> DCFormtransportChargesList = null;

		try {


			DCFormtransportChargesList = dcFormDao.getTransportChargesList(dCNumber,siteId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return DCFormtransportChargesList;
	}
	@Override
	public int getDCSaveCountForVerification(Model model, HttpServletRequest request,HttpSession session) {
		int dcCount = 0;
		try{
			String dcNo = request.getParameter("DCNumber");
			String vendorId = request.getParameter("VendorId");
			String receviedDate = request.getParameter("receivedDate");
			dcCount = dcFormDao.getSavedDCCountForVerification( dcNo, vendorId, receviedDate );



		}catch(Exception e){
			e.printStackTrace();
		}
		return dcCount;
	}
	@Override
	public String doIndentGetDcFormDetails(Model model, HttpServletRequest request, HttpSession session) {
		//String viewToBeSelected = "";
		/*String CGST = "";
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
		String totalAmountAfterGSTTax = "AmountAfterTaxx";
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

		int indentEntrySeqNum = 0;
		Map<String, String> viewGrnPageDataMap = null;
		IssueToOtherSiteDto objIssueToOtherSiteDto = null;

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		String result = "";

		try {
			viewGrnPageDataMap = new HashMap<String, String>();
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			logger.info("Records To Be Processed = "+recordsCount);

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



				String qty = "quantity";
				String expireDate = "expireDate";
				String price = "price";			
				String hsnCode = "hsnCode";
				String otherOrTransportCharges = "otherOrTransportCharges";				
				String taxOnOtherOrTransportCharges = "taxOnOtherOrTransportCharges";				
				String otherOrTransportChargesAfterTax = "otherOrTransportChargesAfterTax";				
				String totalAmount = "totalAmount";
				String dcNo = request.getParameter("dcNo");
				String invoiceNumber = request.getParameter("invoiceNumber");
				String invoiceDate = request.getParameter("invoiceDate");
				String vendorName = request.getParameter("vendorName");
				String receviedDate = request.getParameter("receivedDate");

				String strIntrmidiatendentEntryId = request.getParameter("intrmidiatetEntryId"); 


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
					return result = "SessionFailed";
				}
			//	indentEntrySeqNum = dcFormDao.getIndentEntrySequenceNumber();

				start 01-setp-17
				String chargesRecordsCount =  request.getParameter("numbeOfChargesRowsToBeProcessed");
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
				end 01-sept-17 
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
			//	itosd.updateIntermidiatoryTabledata(strIntrmidiatendentEntryId) ;
				if(insertReceiveReult >= 1) {

					int count = 1;


					Date grnDate = new Date();


					StringBuffer tblTwoData = new StringBuffer();

					for(String num : numOfRec) {

						objIssueToOtherSiteDto = new IssueToOtherSiteDto();

						num = num.trim();

						String prodId = request.getParameter(productId+num);					

						String prodName = request.getParameter(productName+num);

						String subProdId = request.getParameter(subProductId+num);

						String subProdName = request.getParameter(subProductName+num);		


						String childProdId = request.getParameter(childProductId+num);

						String childProdName =  request.getParameter(childProductName+num);


						String unitsOfMeasurementId = request.getParameter(measurementsId+num);

						String measurementName = request.getParameter(measurementsName+num);


						String expiryDate = request.getParameter(expireDate+num);


						int quantity = Integer.parseInt(request.getParameter(qty+num));
						String prc = request.getParameter(price+num);






						String hsnCd = request.getParameter(hsnCode+num);

						String strOtherOrTransportCharg = request.getParameter(otherOrTransportCharges+num);
						String strTaxOtherOrTransportCharg = request.getParameter(taxOnOtherOrTransportCharges+num);
						String strAmountAfterOtherOrTransportCharg = request.getParameter(otherOrTransportChargesAfterTax+num);






						String totalAmnt = request.getParameter(totalAmount+num);


						logger.info(prodId+" <--> "+prodName+" <--> "+subProdId+" <--> "+subProdName+" <--> "+childProdId+" <--> "+childProdName+" <--> "+quantity+"  <--> "+measurementName+" <--> "+hsnCd);

						if (StringUtils.isNotBlank(prodId) && StringUtils.isNotBlank(subProdId) && StringUtils.isNotBlank(childProdId)) {

						} else {
							return result = "Failed";
						}


						if (state.equals("1")) {
							tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"basic rate"+"@@"+"CGST"+"@@"+"CGSTAMT"+"@@"+"SGST"+"@@"+"SGSTAMT"+"@@"+""+"@@"+""+"@@"+totalAmnt+"@@"+note+"@@"+"-"+"&&");
						} else {
							tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"basicAmnt"+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+IGSTAMT+"@@"+totalAmnt+"@@"+note+"@@"+"-"+"&&");
							//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
						}
						objIssueToOtherSiteDto.setProdId1(prodId);
						objIssueToOtherSiteDto.setProdName1(prodName);
						objIssueToOtherSiteDto.setSubProdId1(subProdId);
						objIssueToOtherSiteDto.setSubProdName1(subProdName);
						objIssueToOtherSiteDto.setChildProdId1(childProdId);
						objIssueToOtherSiteDto.setChildProdName1(childProdName);
						objIssueToOtherSiteDto.setQuantity1(quantity);
						objIssueToOtherSiteDto.setPrice1(prc);
						objIssueToOtherSiteDto.setMeasurementId1(unitsOfMeasurementId);
						objIssueToOtherSiteDto.setMeasurementName1(measurementName);
						objIssueToOtherSiteDto.setHSNCode1(hsnCd);
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


							String strMesurmentConversionClassName  = validateParams.getProperty("MesurmentConversionClassName") == null ? "" : validateParams.getProperty("MesurmentConversionClassName").toString();

							//String strMesurmentConversionClassName = "comsumadhura.util.MesurmentConversions";
							Class<?> strMesurmentConversionClass = Class.forName(strMesurmentConversionClassName); // convert string classname to class
							Object mesurment = strMesurmentConversionClass.newInstance(); // invoke empty constructor


							double doubleActualQuantity  =  Double.valueOf(validateParams.getProperty(childProdId+"ActualQuantity") == null ? "0" : validateParams.getProperty(childProdId+"ActualQuantity").toString());
							double doubleInputQuantity =  Double.valueOf(quantity);
							String strConversionMesurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
							Class<?>[] paramTypes = {double.class,double.class, String.class};
							Method printDogMethod = mesurment.getClass().getMethod(methodName, paramTypes);
							doubleQuantity = (Double) printDogMethod.invoke(mesurment, doubleActualQuantity,doubleInputQuantity,measurementName);	            
							quantity = (int) doubleQuantity;
							unitsOfMeasurementId = strConversionMesurmentId;
						}


						int insertIndentReceiveResult = itosd.insertIndentReceiveData(indentEntrySeqNum, objIssueToOtherSiteDto, userId, site_id);



						if(insertIndentReceiveResult >= 1) {
							int updateIndentAvalibilityResult = itosd.updateIndentAvalibilityInwards(objIssueToOtherSiteDto, site_id);
							result = "Success";
							if(updateIndentAvalibilityResult == 0) {
								itosd.updateIndentAvalibilityWithNewIndent(objIssueToOtherSiteDto, site_id);
								result = "Success";
							}
							String id = itosd.getIndentAvailableId(objIssueToOtherSiteDto, site_id);
							if (StringUtils.isNotBlank(id)) {
								itosd.saveReciveDetailsIntoSumduraPriceList(objIssueToOtherSiteDto, invoiceNumber, site_id, id);
							} else {
								String id1 = itosd.getProductAvailabilitySequenceNumber();
								itosd.saveReciveDetailsIntoSumduraPriceList(objIssueToOtherSiteDto, invoiceNumber, site_id, id1);
							}
							itosd.saveReceivedDataIntoSumadhuClosingBalByProduct(objIssueToOtherSiteDto, site_id);




						}
						else {
							request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
							result = "Failed";
						}
						count++;
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

				if(numOfTransRec != null && numOfTransRec.length > 0) {


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








				}
				else {
					request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
					result = "Failed";
				}


				transactionManager.commit(status);
			}
			else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				result = "Failed";
			}
		}
		catch (Exception e) {
			transactionManager.rollback(status);
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
		}*/



		return null;
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


	@Override
	public int getDcCount(String dcNo, String strVendorName, String strReceiveDate) {
		String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
		int intCount = dcFormDao.getDCCount(dcNo, strVendorName, strReceiveDate);
		return intCount;
	}
	
	@Override
	public String dcFormUpdate(Model model, HttpServletRequest request, HttpSession session) {
		
		String CGST = "";
		String SGST = "";
		String IGST = "";
		Double percent = 0.0;
		Double CGSTAMT = 0.0;
		Double SGSTAMT = 0.0;
		Double IGSTAMT = 0.0;
		Double amt = 0.0;
		Date grnDate = new Date();
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		String state = request.getParameter("state"); 
		
		
		
		StringBuffer tblCharges = new StringBuffer();
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in DcFo_dcFUpd, ");
		
		String indentEntryId = "";
		String dcNumber = "";
		String invoiceNum = "";
		String invoiceDate = "";
		String ttlAmntForIncentEntryTbl = "";
		String convttlAmntForIncentEntryTbl = "";
		String gstinNumber = "";
		String vehileNo = "";
		String transporterName = "";
		String receviedDate = "";
		String site_id = "";
		try {
			indentEntryId = (String) session.getAttribute("indentEntryId");
			dcNumber = (String) session.getAttribute("dcNumber");
			System.out.println("indentEntryId: "+indentEntryId);
			System.out.println("dcNumber: "+dcNumber);
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , DCNumber:"+dcNumber);
			
			invoiceNum = request.getParameter("invoiceNumber");
			invoiceDate = request.getParameter("invoiceDate");
			ttlAmntForIncentEntryTbl = request.getParameter("ttlAmntForIncentEntryId").toString();
			
			double conttlAmntForIncentEntryTbl=Double.parseDouble(ttlAmntForIncentEntryTbl);
			
			convttlAmntForIncentEntryTbl = numberFormat.format(conttlAmntForIncentEntryTbl);
			
			
			
			System.out.println("ttlAmntForIncentEntryTbl"+ttlAmntForIncentEntryTbl);
			String vendorId = request.getParameter("VendorId");
			gstinNumber = request.getParameter("gstinNumber");
			vehileNo = request.getParameter("vehileNo");
			transporterName = request.getParameter("transporterName");
			String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
			receviedDate = request.getParameter("receivedDate");
			
			DCToInvoiceDto dctoinvoicedto=new DCToInvoiceDto();
			dctoinvoicedto.setStrInvoiceNo(invoiceNum);
			dctoinvoicedto.setStrInoviceDate(invoiceDate);
			dctoinvoicedto.setTotalAmnt(ttlAmntForIncentEntryTbl);
			
			int response1=0,response2=0;
			site_id = "";
			site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			//response1=dcFormDao.updateDCFormData(dcNumber,dctoinvoicedto);
			response2=dcFormDao.doDcInactive(Integer.parseInt(indentEntryId));

			System.out.println(response1);
			System.out.println(response2);
			if(response1==1){System.out.println("Records Updated in INDENT_ENTRY... user341");}
			else {System.out.println("records NOT Updated in INDENT_ENTRY... user341");}
			if(response2==1){System.out.println("records Updated in DC_FORM... user341");}
			else {System.out.println("records NOT Updated in DC_FORM... user341");}
		} catch (Exception e1) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e1.printStackTrace();
			return "";
		}
		
		

		String userId = "";
		
		String transportId ="conveyance";
		String transportGSTPercentage = "gstTax";
		String transportGSTAmount = "gstAmount";
		String totalAmountAfterGSTTax = "otherOrTransportChargesAfterTax";
		String transportInvoiceId = "transportInvoice";
		String transportAmount = "conveyanceAmount";
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

		
		try {
			String chargesRecordsCount =  request.getParameter("numbeOfTransRowsToBeProcessed");
			//	String chargesRecordsCount = "1|";
			System.out.println("chargesRecordsCount: "+chargesRecordsCount);
			String numOfChargeRec[] = null;
			if((chargesRecordsCount != null) && (!chargesRecordsCount.equals(""))) {
				numOfChargeRec = chargesRecordsCount.split("\\|");
				if(numOfChargeRec != null && numOfChargeRec.length > 0) {
					for(String charNum : numOfChargeRec) {
						String transactionDts = request.getParameter(transportId+charNum);
						System.out.println("transactionDts: "+transactionDts);
						String transDts[] = transactionDts.split("\\$");
						String transactionId = transDts[0];
						tblChargeName = transDts[1];
						String gstPercentage = request.getParameter(transportGSTPercentage+charNum);
						String arraypercent[] = gstPercentage.split("\\$");
						String gstId = arraypercent[0];
						gstChargePercentage = arraypercent[1];
						gstChargePercentage = gstChargePercentage.replace("%", "").trim();
						gstAmount = request.getParameter(transportGSTAmount+charNum);
						System.out.println("gstAmount: "+gstAmount);
						totAmtAfterGSTTax = request.getParameter(totalAmountAfterGSTTax+charNum).toString();
						

						double totAmtAfterGSTTax1=Double.parseDouble(totAmtAfterGSTTax);
						
						String convtotAmtAfterGSTTax=numberFormat.format(totAmtAfterGSTTax1);
						
						
						
						String transactionInvoiceId = request.getParameter(transportInvoiceId+charNum);
						transAmount = request.getParameter(transportAmount+charNum);

						dcFormDao.saveTransactionDetails(dcNumber,transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId, transAmount, site_id, indentEntryId);

						if (state.equals("1") || state.equalsIgnoreCase("Local")) {
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
							tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+convtotAmtAfterGSTTax+"@@"+"-"+"&&");
						} else {
							tblCharges.append(tblChargeName+"@@"+transAmount+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+chargeIGST+"@@"+chargeIGSTAMT+"@@"+convtotAmtAfterGSTTax+"@@"+"-"+"&&");
						}

					}
				}
			}
		} catch (Exception e1) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e1.printStackTrace();
			return "";
		}
		Map<String, String> viewGrnPageDataMap = null;
		try {
			viewGrnPageDataMap = new HashMap<String, String>();
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			logger.info("Records To Be Processed = "+recordsCount);

			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			StringBuffer tblOneData = new StringBuffer();
			StringBuffer tblTwoData = new StringBuffer();

			if(numOfRec != null && numOfRec.length > 0) {
				int count=1;
				for(String num : numOfRec) {

					String productName = "product";
					String subProductName = "subProduct";
					String childProductName = "childProduct";
					String productId = "productId";
					String subProductId = "subProductId";
					String childProductId = "childProductId";
					String measurementsName = "unitsOfMeasurement";
					String measurementsId = "measurmentId";
					String qty = "quantity";
					String expireDate = "expireDate";
					String price = "price";
					String basicAmount = "BasicAmountId";
					String gstTax = "tax";
					String hsnCode = "hsnCode";
					String taxAmount = "TaxAmountId";				
					String amountAfterTax = "AmountAfterTaxId";				
					String otherOrTransportCharges = "otherOrTransportCharges";				
					String taxOnOtherOrTransportCharges = "taxOnOtherOrTransportCharges";				
					String otherOrTransportChargesAfterTax = "otherOrTransportChargesAfterTax";				
					String totalAmount = "totalAmount";
					String otherCharges = "otherCharges";				
					String priceId = "priceId";

					String poNo = request.getParameter("poNo");
					String note = request.getParameter("Note");

					String otherChrgs = request.getParameter(otherCharges);

					num = num.trim();
					System.out.println("prod,num:"+productName+","+num);
					String product = request.getParameter(productName+num);
					System.out.println("prod info"+product);
					//String prodsInfo[] = product.split("\\$");	
					String prodId = request.getParameter(productId+num);
					String prodName = product;
					//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);

					String subProduct = request.getParameter(subProductName+num);

					if(subProduct == null){
						subProduct = request.getParameter("subProduct"+num);
					}

					//String subProdsInfo[] = subProduct.split("\\$");					
					String subProdId = request.getParameter(subProductId+num);
					String subProdName = subProduct;					
					//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
					String expiryDate = request.getParameter(expireDate+num);
					String childProduct = request.getParameter(childProductName+num);
					if(childProduct == null){
						childProduct = request.getParameter("childProduct"+num);
					}


					//String childProdsInfo[] = childProduct.split("\\$");					
					String childProdId = request.getParameter(childProductId+num);
					String childProdName = childProduct;
					//logger.info("Child Product Id = "+childProdId+" and Child Product Name = "+childProdName);

					String quantity = request.getParameter(qty+num);
					String prc = request.getParameter(price+num);
					String basicAmnt = request.getParameter(basicAmount+num).toString();
					
					double basicAmnt1=Double.parseDouble(basicAmnt);
					
					String conbasicAmnt=numberFormat.format(basicAmnt1);
					

					String unitsOfMeasurement = request.getParameter(measurementsName+num);
					//String measurementsInfo[] = unitsOfMeasurement.split("\\$");
					String measurementId = request.getParameter(measurementsId+num);
					String measurementName = unitsOfMeasurement;
					//logger.info("Measurement Id = "+measurementId+" and Measurement Name = "+measurementName);
					String tax = request.getParameter(gstTax+num);
					String taxId = "";
					String taxPercentage = "";
					String [] taxInfo = null;
					if(tax != null && !tax.equals("")){

						taxInfo = tax.split("\\$");
						taxId = taxInfo[0];
						taxPercentage = taxInfo[1];
					}
					else{
						taxId = "0";
						taxPercentage = "0";
					}
					System.out.println(taxPercentage);
					//logger.info("Tax Id = "+taxId+" and Tax Percentage = "+taxPercentage);
					if (taxPercentage.contains("%")) {
						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);
					}

					String hsnCd = request.getParameter(hsnCode+num);
					String taxAmnt = request.getParameter(taxAmount+num);
					String amntAfterTax = request.getParameter(amountAfterTax+num).toString();
					
					double amntAfterTax1=Double.parseDouble(amntAfterTax);
					
					String conamntAfterTax=numberFormat.format(amntAfterTax1);
					
					String otherOrTranportChrgs = request.getParameter(otherOrTransportCharges+num);
					String taxOnOtherOrTranportChrgs = request.getParameter(taxOnOtherOrTransportCharges+num);
					String otherOrTransportChrgsAfterTax = request.getParameter(otherOrTransportChargesAfterTax+num);
					String totalAmnt = request.getParameter(totalAmount+num);
					String otherChrgss = request.getParameter(otherOrTransportCharges+num);
					String strPriceId = request.getParameter(priceId+num);
					DCToInvoiceDto  DCToInvoicedto=new DCToInvoiceDto();
					DCToInvoicedto.setProdId(prodId);
					DCToInvoicedto.setProdName(prodName);
					DCToInvoicedto.setSubProdId(subProdId);
					DCToInvoicedto.setSubProdName(subProdName);
					DCToInvoicedto.setChildProdId(childProdId);
					DCToInvoicedto.setChildProdName(childProdName);
					DCToInvoicedto.setQuantity(quantity);
					DCToInvoicedto.setMeasurementId(measurementId);
					System.out.println("haabnxavda: "+prodId+","+subProductId+","+childProdId+","+measurementId);
					DCToInvoicedto.setMeasurementName(measurementName);
					DCToInvoicedto.setPrice(prc);
					DCToInvoicedto.setBasicAmnt(basicAmnt);

					DCToInvoicedto.setTax(taxPercentage);
					DCToInvoicedto.setHsnCd(hsnCd);
					DCToInvoicedto.setTaxAmnt(taxAmnt);
					DCToInvoicedto.setAmntAfterTax(amntAfterTax);
					DCToInvoicedto.setOtherOrTranportChrgs(otherOrTranportChrgs);
					DCToInvoicedto.setTaxOnOtherOrTranportChrgs(taxOnOtherOrTranportChrgs);
					DCToInvoicedto.setOtherOrTransportChrgsAfterTax(otherOrTransportChrgsAfterTax);
					DCToInvoicedto.setTotalAmnt(totalAmnt);
					DCToInvoicedto.setOtherChrgs(otherChrgss);
					DCToInvoicedto.setPoNo(poNo);
					//DCFdto.setDcNo(dcNo);
					//DCFdto.setDate(invoiceDate);
					DCToInvoicedto.setDate(receviedDate);
					DCToInvoicedto.setExpiryDate(expiryDate);
					DCToInvoicedto.setPriceId(strPriceId);
					DCToInvoicedto.setStrInvoiceNo(invoiceNum);
					DCToInvoicedto.setStrInoviceDate(invoiceDate);
					//irdto.setPoDate(poDate);
					//irdto.seteWayBillNo(eWayBillNo);
					//irdto.setVehileNo(vehileNo);
					//irdto.setTransporterName(transporterName);
					if (state.equals("1") || state.equals("Local")) {
						/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/
						if (taxPercentage.equals("0")) {
							CGST = "0";
							SGST = "0";
						} else {
							percent = Double.parseDouble(taxPercentage)/2;
							amt = Double.parseDouble(taxAmnt)/2;
							CGSTAMT = amt;
							SGSTAMT = amt;
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
						}
					} else {
						percent = Double.parseDouble(taxPercentage);
						amt = Double.parseDouble(taxAmnt);
						IGST = String.valueOf(percent);
						IGSTAMT = amt;
					}

					int response3=0;
					int intIndentEntryDetailsSeqNo = ird.getIndentEntryDtails_SeqNumber(); 

					response3=dcFormDao.insertIndentReceiveData(Integer.parseInt(indentEntryId), intIndentEntryDetailsSeqNo, DCToInvoicedto, userId, site_id);

					System.out.println(response3);


					//	int response4=0;
					//int intUpdateIndentEntryDtls = dcFormDao.updatePriceInIndentEntryDetails(prc,quantity,dcNumber);

					//System.out.println(response6);


					//	DCToInvoicedto = dcFormDao.getPriceDetails(dcNumber, DCToInvoicedto, intIndentEntryDetailsSeqNo, invoiceNum);

					int intUpdatePriceList = dcFormDao.updateDCPriceList(dcNumber, DCToInvoicedto, intIndentEntryDetailsSeqNo, invoiceNum);

					//System.out.println(response4);






					//int intUpdateClosingBal = dcFormDao.updateSumadhuraClosingBalance(prc,quantity,invoiceNum,dcNumber,site_id,DCToInvoicedto);

					if (state.equals("1") || state.equals("Local")) {
						tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+conbasicAmnt+"@@"+CGST+"@@"+CGSTAMT+"@@"+SGST+"@@"+SGSTAMT+"@@"+""+"@@"+""+"@@"+conamntAfterTax+"@@"+note+"@@"+"-"+"&&");
					} else {
						tblTwoData.append(count+"@@"+childProdName+"@@"+hsnCd+"@@"+note+"@@"+measurementName+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+conbasicAmnt+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+IGSTAMT+"@@"+conamntAfterTax+"@@"+note+"@@"+"-"+"&&");
						//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
					}
					count++;
				}//end-of-for-loop
			}//end-of-if-block
			// user341 start

			tblOneData.append(gstinNumber+"@@"+changeDateFormat(grnDate)+"@@"+invoiceNum+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+convttlAmntForIncentEntryTbl+"@@"+new NumberToWord().convertNumberToWords((int)Double.parseDouble(ttlAmntForIncentEntryTbl))+" Rupees Only."+"@@"+"vendorAddress"+"@@"+ird.getindentEntrySerialNo(receviedDate)+"@@"+""+"@@"+dcNumber+"@@"+"vendorName"+"@@"+""+"@@"+""+"@@"+vehileNo+"@@"+transporterName+"@@"+"doubleSumOfOtherCharges"+"@@"+receviedDate+"@@"+dcNumber);

			viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
			//start 03-sept
			String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
			viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
			//end 03-sept

			String fnlStr = tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
			viewGrnPageDataMap.put("tblTwoData", fnlStr);
			request.setAttribute("viewGrnPageData", viewGrnPageDataMap);
			//user341 end
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			return "success"; 

		}//end-of-try-block
		catch(Exception e){
			e.printStackTrace();
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			return ""; }


	}

	@Override
	public String getIndentIdNo(String strVendorId,String DCNumber,String strSiteId) {
		String dcNum=dcFormDao.getIndentIdNo( strVendorId, DCNumber, strSiteId);
		return dcNum;
	}
	@Override
	public int isDCPresentAndInactive(String strVendorId,String strDiteId,String strDCNumber,String dcDate) {
		int intCount = dcFormDao.isDCPresentAndInactive(strVendorId,strDiteId,strDCNumber,dcDate);
		return intCount;
	}
	
	@Override
	public String covertDcWithoutPricing(Model model, HttpServletRequest request, HttpSession session) {
		String userId = "";
		String site_id = "";
		String tansRecordsCount = request.getParameter("length");
		logger.info("Records To Be Processed = "+tansRecordsCount);
		String numOfTransRec[] = null;
		if((tansRecordsCount != null) && (!tansRecordsCount.equals(""))) {
			numOfTransRec = tansRecordsCount.split(",");
		}
		int isDcInactive=0;
 		
 		String dcDate="";
 		String Note="";
		site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String poNumber="";
		String strInvoiceDate="";
		List<String> list=new ArrayList<String>();
		String payment_Req_Days="";
		String after_change_Modified_date="";
		String invoiceNumber=request.getParameter("invoice_Number")==null ? "" :request.getParameter("invoice_Number");
		String invoiceDate=request.getParameter("invoice_Date")==null ? "" :request.getParameter("invoice_Date");
		String vendorId=request.getParameter("VendorId")==null ? "" :request.getParameter("VendorId");
		boolean valStatus=dcFormDao.checkPaymentAndTaxInvoice(invoiceNumber,invoiceDate,vendorId,site_id);
		
		if(valStatus){
			request.setAttribute("Message", "<font color=red >It seems TaxInvoice Or Paymented With This InvoiceNumber.</font>");
			return "TaxOrPayment";
		}
		boolean invoiceStatus=dcFormDao.checkInvoiceDateAndNumber(invoiceNumber,invoiceDate,vendorId,site_id);
		if(invoiceStatus){
			request.setAttribute("Message", "<font color=red > Invoice Number Not Matched With This Date Or VendorName.</font>");
			return "InvalidInvoiceNumber";
		}
		int records_Count =(numOfTransRec.length);
		for(int i=0;i<records_Count;i++){
		int num=Integer.parseInt(numOfTransRec[i]);
		
			String dcNumber=request.getParameter("DCNumber"+num);
			dcDate=request.getParameter("DCDate"+num);
			 
			Note=request.getParameter("Note");
			
			isDcInactive=dcFormDao.isDCPresentAndInactive(vendorId,site_id,dcNumber,dcDate);
			poNumber=dcFormDao.checkAllPosameOrNot(vendorId,site_id,dcNumber,dcDate);// this is used to get the po number to check all are same or not
			list.add(poNumber);
			
			if(isDcInactive==0){
			request.setAttribute("Message", "<font color=red > This \""+dcNumber+"\" Dc Number already converted to invoice.</font>");
			return "existed";
			}
		}
		//int i=1;
		for(int i=1;i<list.size();i++){
		if(!list.get(0).equals(list.get(i))){
			request.setAttribute("Message", "<font color=red >Oops!!! DC,s from Multiple PO,s can’t be converted into a Single Invoice.</font>");
			return "PONOTSAME";
		}
		}
		payment_Req_Days=dcFormDao.getPaymentRequestDays(poNumber);
		/*************************when the user given receive date then add po no of days to that one  start*********************************************/
		if(payment_Req_Days!=null && !payment_Req_Days.equals("null") && !payment_Req_Days.equals("") && !payment_Req_Days.equals("0")){
			//String oldDate = "2017-01-29";  
			System.out.println("Date before Addition: "+strInvoiceDate);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
			Calendar c = Calendar.getInstance();
			try{
			   //Setting the date to the given date
			   c.setTime(sdf.parse(strInvoiceDate));
			}catch(ParseException e){
				e.printStackTrace();
			 }
			   
			//Number of Days to add
			c.add(Calendar.DAY_OF_MONTH,Integer.valueOf(payment_Req_Days));  
			//Date after adding the days to the given date
			after_change_Modified_date= sdf.format(c.getTime());  
			//Displaying the new Date after addition of Days
			System.out.println("Date after Addition: "+after_change_Modified_date);
			
		}
		/****************************************getting the date and add no of days**************************************/
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in DcFo_conDcO, ");
		try{
			for(int i=0;i<records_Count;i++){
				int num=Integer.parseInt(numOfTransRec[i]);
				String dcNumber=request.getParameter("DCNumber"+num);
				dcDate=request.getParameter("DCDate"+num);
				WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , DCNumber:"+dcNumber+" , InvoiceNumber:"+invoiceNumber);
				
				//String indentEntryId = dcFormDao.getIndentIdNo(vendorId,dcNumber,site_id);//old
				int dc_entry_id = Integer.parseInt(dcFormDao.getDcEntryIdNo(vendorId,dcNumber,site_id,dcDate));
				
				int indent_entry_id;
				
				//checking whether InvoiceNumber is already present in INDENT_ENTRY ?
				indent_entry_id = dcFormDao.getIndentIdByInvoiceNo(vendorId,invoiceNumber,site_id,invoiceDate);
				
				if(indent_entry_id==0){
					indent_entry_id = dcFormDao.getIndentEntrySequenceNumber();

				//	String grn_no = session.getAttribute("SiteName")+ird.getindentEntrySerialNo();

					int response1;
					DCFormDto db_DCEntry_Record = null;
					db_DCEntry_Record = dcFormDao.getDB_DCEntry_Record(dc_entry_id);
					String grn_no = session.getAttribute("SiteName")+ird.getindentEntrySerialNo(db_DCEntry_Record.getStrReceiveDate());
					//response1=dcFormDao.updateInvoiceNumber(dcNumber,invoiceNumber,dcDate,grn_no,site_id,vendorId);//old
					response1=dcFormDao.insertIndentEntry(dcNumber,invoiceNumber,invoiceDate,grn_no,site_id,vendorId,indent_entry_id,db_DCEntry_Record,Note,after_change_Modified_date);
					
				}
				else{
					DCFormDto db_DCEntry_Record = null;
					db_DCEntry_Record = dcFormDao.getDB_DCEntry_Record(dc_entry_id);
					int response1=dcFormDao.updateIndentEntry(indent_entry_id,db_DCEntry_Record);
				}
				
				dcFormDao.updateDcEntry(indent_entry_id,dc_entry_id,invoiceNumber,invoiceDate);
				
				int Response1=dcFormDao.updateInvoiceNumberTrans(invoiceNumber,indent_entry_id,dc_entry_id);
				System.out.println("Response1:"+Response1);
				Response1=dcFormDao.updateIndentEntryInCreditNote(invoiceNumber,indent_entry_id,dc_entry_id);
				int response2=dcFormDao.doDcInactive(dc_entry_id);
				System.out.println("response2:"+response2);
				
				int response3=0;
				int response4=0;
				int response5=0;
				List<DCToInvoiceDto> dcProductsList = null;
				dcProductsList = dcFormDao.getDetailsInPriceTableByDcnumber(dcNumber,site_id,vendorId,dc_entry_id);
				
				for(int j=0;j<dcProductsList.size();j++)
				{
					int intIndentEntryDetailsSeqNo = ird.getIndentEntryDtails_SeqNumber(); 
					DCToInvoiceDto  DCToInvoicedto = dcProductsList.get(j);
					
					response3=dcFormDao.insertIndentEntryDetailsWithoutPricing(indent_entry_id, intIndentEntryDetailsSeqNo, DCToInvoicedto, userId, site_id);
					System.out.println("response3:"+response3);
					
					response4=dcFormDao.updatePriceListWithoutPricing(dcNumber,invoiceNumber,intIndentEntryDetailsSeqNo, DCToInvoicedto, userId, site_id);
					System.out.println("response4:"+response4);
					
					response5=dcFormDao.updateSumadhuraClosingBalByProduct(dcNumber,invoiceNumber, DCToInvoicedto, userId, site_id);
					System.out.println("response5:"+response5);
				}

				System.out.println("DC Form "+dcNumber+" Converted");
			}

			request.setAttribute("Message", "<font color=green >Success. DC converted into Invoice successfully.</font>");
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			return "success";
		}
		catch(Exception e){
			e.printStackTrace();
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			System.out.println("DC Form NOT Converted");
			return "failure";
		}

	}

	@Override
	public int checkDCInactive(String strDCNO) {
		int isDcInactive = 0;
		
		//isDcInactive=dcFormDao.isDCPresentAndInactive(strDCNO);
		return isDcInactive;
	}


	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId){
		List<ViewIndentIssueDetailsBean> list = null;
		try{
			list = dcFormDao.getGrnViewDetails( fromDate,  toDate,  siteId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public String getDcGrnDetails(String dcnumber, String siteId,HttpServletRequest request) {
		Map<String, String> viewGrnPageDataMap = null;
		viewGrnPageDataMap =new HashMap<String, String>();
		
		String result="Success";
		String tblOneData="";
		 String tbltwoData="";
		 String tblCharges="";
	String state="";
	String dcEntryId="";
	//	List<GetInvoiceDetailsBean> GrnIssueToOtherSiteList = null;

		try {
			
			String strVendorId = request.getParameter("vendorId");
			String dcDate = request.getParameter("dcDate");
			dcEntryId=request.getParameter("dcEntryId");
			String type=request.getParameter("type")==null?"":request.getParameter("type");// this for site taken from po reports or dc grn knowing purpose
			if(type.equalsIgnoreCase("MarketPurchase")){siteId=request.getParameter("SiteId");}
			if(type.equalsIgnoreCase("dcGrnPriceMaster")){siteId=request.getParameter("SiteId");}
			if(type.equalsIgnoreCase("dcGrnForReceiveDtls")){siteId=request.getParameter("SiteId");}
			tblOneData+= dcFormDao.getDcVendorDetails(dcnumber, siteId,strVendorId,dcDate,dcEntryId);
			tbltwoData+= dcFormDao.getDcProductDetails(dcnumber,siteId,strVendorId,request,dcDate,dcEntryId);
			state=request.getAttribute("state").toString();
			tblCharges+=dcFormDao.getDcTransportChargesListForGRN(dcnumber,siteId,state,strVendorId,dcDate,dcEntryId);
			viewGrnPageDataMap.put("tblOneData", tblOneData.toString());

				
			String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);
			viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
				//table three data

				
			String fnlStr = tbltwoData.toString().substring(0, tbltwoData.toString().length() - 2);
			viewGrnPageDataMap.put("tblTwoData", fnlStr);
			request.setAttribute("viewGrnPageData", viewGrnPageDataMap);

					//transactionManager.commit(status);		
			 viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
			
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

	@Override
	public String processingPOasDC(Model model, HttpServletRequest request, HttpSession session) {
		String response = "";
		String prod = "mainProduct";
		String subProd = "mainSubProduct";
		String childProd = "mainChildProduct";
		String qty = "mainquantity";
		String expireDate = "expireDate";
		String price = "mainPrice";
		String basicAmount = "mainamtAfterDiscount";
		String measurements = "mainUnitsOfMeasurement";				
		String gstTax = "maintax";
		String hsnCode = "mainhsnCode";
		String taxAmount = "maintaxAmount";				
		String amountAfterTax = "mainamountAfterTax";				
		String otherOrTransportCharges = "mainotherOrTransportCharges";				
		String taxOnOtherOrTransportCharges = "maintaxOnOtherOrTransportCharges";				
		String otherOrTransportChargesAfterTax = "mainotherOrTransportChargesAfterTax";				
		String totalAmount = "maintotalAmount";
		String otherCharges = "mainOtherCharges";
		
		
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in DcFo_proPOD, ");
		
		try {
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+request.getParameter("poNo")+" , DCNumber:"+request.getParameter("InvoiceorDCNumber"));
			map.put("typeOfPurchase","PO");
			map.put("dcNo",request.getParameter("InvoiceorDCNumber"));
			map.put("dcDate",changeDateFormat(request.getParameter("InvoiceorDCDate")));
			map.put("vendorId",request.getParameter("VendorId"));
			map.put("receviedDate",changeDateFormat(request.getParameter("receivedDate")));
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");/**/
			map.put("poNo",request.getParameter("poNo"));
			map.put("vendorName",request.getParameter("vendorName"));
			map.put("vendorAddress",request.getParameter("vendorAddress"));
			map.put("gstinNumber",request.getParameter("strGSTINNumber"));
			map.put("note",request.getParameter("Note"));
			map.put("state",request.getParameter("state"));
			map.put("ttlAmntForIncentEntryTbl",request.getParameter("ttlAmntForIncentEntry"));
			map.put("poDate",changeDateFormat(request.getParameter("poDate")));
			map.put("eWayBillNo",request.getParameter("eWayBillNo"));
			map.put("vehileNo",request.getParameter("vehileNo"));
			map.put("transporterName",request.getParameter("transporterName"));
			map.put("otherChrgs",request.getParameter(otherCharges));
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");/**/
			map.put("chargesRecordsCount",transRows);
			String[] recordsCountArray = recordsCount.split("\\|1\\|");
			recordsCount = recordsCountArray[0];
			recordsCount = recordsCount + "|";
			String creditRows="";
			if(recordsCountArray.length>1){
				creditRows = recordsCountArray[1]; 
			}
			creditRows = "1|" + creditRows;
			
			map.put("creditRows", creditRows);
			
			int trans_rows = transRows.split("\\|").length;
			for(int charNum=1;charNum<=trans_rows;charNum++){
			Map<String,String> transMap = new HashMap<String,String>();
			transMap.put("transactionDts",request.getParameter("Conveyance"+charNum));
			transMap.put("gstPercentage",request.getParameter("GSTTax"+charNum));
			transMap.put("gstAmount",request.getParameter("GSTAmount"+charNum));
			transMap.put("totAmtAfterGSTTax",request.getParameter("AmountAfterTax"+charNum));
			transMap.put("transactionInvoiceId",request.getParameter("TransportInvoice"+charNum));
			transMap.put("transAmount",request.getParameter("ConveyanceAmount"+charNum));
			transMapList.add(transMap);
			}
				
			String recordsCountModified = "";
			int records_Count = recordsCount.split("\\|").length;
			String[] records_Array = recordsCount.split("\\|");
			for(int index=0;index<records_Count;index++){
				int num=Integer.parseInt(records_Array[index]);
			Map<String,String> prodMap = new HashMap<String,String>();
			prodMap.put("product",request.getParameter(prod+num));
			prodMap.put("subProduct",request.getParameter(subProd+num));
			prodMap.put("expiryDate",request.getParameter(expireDate+num));
			prodMap.put("childProduct",request.getParameter(childProd+num));
			
			String quantity = request.getParameter(qty+num);
			prodMap.put("quantity",quantity);
			String basicAmnt = request.getParameter(basicAmount+num);
			prodMap.put("basicAmnt",basicAmnt);
			String prc = String.valueOf(Double.valueOf(basicAmnt)/Double.valueOf(quantity));
			prodMap.put("prc",prc); 

			prodMap.put("unitsOfMeasurement",request.getParameter(measurements+num));
			prodMap.put("tax",request.getParameter(gstTax+num));
			prodMap.put("hsnCd",request.getParameter(hsnCode+num));
			prodMap.put("taxAmnt",request.getParameter(taxAmount+num));
			prodMap.put("amntAfterTax",request.getParameter(amountAfterTax+num));
			prodMap.put("otherOrTranportChrgs",request.getParameter(otherOrTransportCharges+num));
			prodMap.put("taxOnOtherOrTranportChrgs",request.getParameter(taxOnOtherOrTransportCharges+num));
			prodMap.put("otherOrTransportChrgsAfterTax",request.getParameter(otherOrTransportChargesAfterTax+num));
			prodMap.put("totalAmnt",request.getParameter(totalAmount+num));
			prodMap.put("otherChrgss",request.getParameter("OtherCharges"+num));
			prodMap.put("InvoiceRemarks",request.getParameter("InvoiceRemarks"+num));
			prodMap.put("indentCreationDetailsId",request.getParameter("mainindentCreationDetailsId"+num));
			prodMap.put("poEntryDetailsId",request.getParameter("mainPoEntryDetailsId"+num));	
			prodMapList.add(prodMap);
			recordsCountModified=recordsCountModified+(index+1)+"|";
			}
			System.out.println(map);
			System.out.println(transMapList);
			System.out.println(prodMapList);
			map.put("recordsCount",recordsCountModified);
			
			response = dcFormProcessCommon(map,transMapList,prodMapList,request,session);

			String indentNumber = request.getParameter("indentNumber");
			String reqSiteId = request.getParameter("toSiteId");
			String poNo = map.get("poNo");
			
			if (response.equalsIgnoreCase("Success")){

				//doing Credit Note
				String credit_for = request.getParameter("credit_for")==null?"":request.getParameter("credit_for").toString();
				if(StringUtils.isNotBlank(credit_for)){
				irs.doCreditNote(map,request,credit_for,"DC");
				request.setAttribute("hasCreditNote", "true");
				}
				else{
					System.out.println("credit not done");
				}


				for(Map<String,String> productMap:prodMapList){
					String childProduct = productMap.get("childProduct");
					String childProdsInfo[] = childProduct.split("\\$");					
					String childProdId = childProdsInfo[0];
					double creditQuantity = request.getAttribute(childProdId)==null ? 0.0 : Double.parseDouble(request.getAttribute(childProdId).toString());
					double takenQuantity = Double.parseDouble(productMap.get("quantity").toString());
					//before 2-AUG-2019, the below line like in the comment below. But after that changed. Because damaged(credit) products does not consider as received. so PO still remain Active.
					//String totalQuantity = String.valueOf(creditQuantity+takenQuantity);
					String totalQuantity = String.valueOf(takenQuantity);
					
					ird.updateReceiveQuantityInIndentCreationDtls(totalQuantity,productMap.get("indentCreationDetailsId"));
					ird.updateAllocatedQuantityInPurchaseDeptTable(totalQuantity,productMap.get("indentCreationDetailsId"),request);
					ird.updateReceivedQuantityInPoEntryDetails(totalQuantity,productMap.get("poEntryDetailsId"),request);
				}
				ird.setPOInactive(poNo,reqSiteId);
				ird.setIndentInactiveAfterChecking(indentNumber);
				
				

			}
			if (response.equalsIgnoreCase("Success")){
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");		
			}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
			}
			
		} catch (Exception e) {
			response = "Failed";
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}
		
		return response;
	}

	//this method is not using. using the common method for inv & dc is in IndentReceiveServiceImpl.java
	public void doCreditNote(Map<String, String> map, HttpServletRequest request){
		int creditSeqNo = ird.getCreditNoteSequenceNumber();
		String creditNoteNumber = request.getParameter("creditNoteNumber");
		String dcEntryNo = request.getAttribute("dcEntryNo").toString();
		String dcNumber = map.get("dcNo");
		String creditTotalAmount = request.getParameter("creditTotalAmount");
		CreditNoteDto creditNoteDto = new CreditNoteDto();
		creditNoteDto.setCreditSeqNo(creditSeqNo);
		creditNoteDto.setCreditNoteNumber(creditNoteNumber);
		creditNoteDto.setDcEntryId(dcEntryNo);
		creditNoteDto.setDcNumber(dcNumber);
		
		creditNoteDto.setCreditTotalAmount(creditTotalAmount);
		ird.insertCreditNote(creditNoteDto);
		
		String creditRows = map.get("creditRows");
		int creditRowsCount = creditRows.split("\\|").length;
		for(int num=1;num<=creditRowsCount;num++)
		{
			int creditNoteDtlsSeqId = ird.getCreditNoteDetailsSequenceNumber();
			CreditNoteDto creditNoteDetailsDto = new CreditNoteDto();
			creditNoteDetailsDto.setCreditNoteDtlsSeqId(creditNoteDtlsSeqId);
			creditNoteDetailsDto.setCreditSeqNo(creditSeqNo);
			//creditNoteDetailsDto.setIndentEntryId(indentEntryNo);

			String product = request.getParameter("Product"+num);
			String productId = product.split("\\$")[0];
			String sub_Product = request.getParameter("SubProduct"+num);
			String sub_ProductId = sub_Product.split("\\$")[0];
			String child_Product = request.getParameter("ChildProduct"+num);
			String child_ProductId = child_Product.split("\\$")[0];
			String measurement = request.getParameter("UnitsOfMeasurement"+num);
			String measurementId = measurement.split("\\$")[0];
			String recivedQty = request.getParameter("quantity"+num);
			request.setAttribute(child_ProductId, recivedQty);
			String Price = request.getParameter("PriceId"+num);
			String basicAmount = request.getParameter("BasicAmountId"+num);
			String tax = request.getParameter("TaxId"+num);
			String taxId = tax.split("\\$")[0];
			String taxAmount = request.getParameter("TaxAmountId"+num);
			String amountAfterTax = request.getParameter("AmountAfterTaxId"+num);
			String totalAmount = request.getParameter("TotalAmountId"+num);
			String hsnCode = request.getParameter("HSNCodeId"+num);
			Double OtherOrTransportCharges1 = Double.parseDouble((request.getParameter("OtherOrTransportChargesId"+num)==null||request.getParameter("OtherOrTransportChargesId"+num).equals(""))?"0":request.getParameter("OtherOrTransportChargesId"+num));
			Double TaxOnOtherOrTransportCharges1 = Double.parseDouble((request.getParameter("TaxOnOtherOrTransportChargesId"+num)==null||request.getParameter("TaxOnOtherOrTransportChargesId"+num).equals(""))?"0":request.getParameter("TaxOnOtherOrTransportChargesId"+num));
			Double OtherOrTransportChargesAfterTax1 = Double.parseDouble((request.getParameter("OtherOrTransportChargesAfterTaxId"+num)==null||request.getParameter("OtherOrTransportChargesAfterTaxId"+num).equals(""))?"0":request.getParameter("OtherOrTransportChargesAfterTaxId"+num));

			ProductDetails productDetails = new ProductDetails();
			productDetails.setProductId(productId);
			productDetails.setSub_ProductId(sub_ProductId);
			productDetails.setChild_ProductId(child_ProductId);
			productDetails.setMeasurementId(measurementId);
			productDetails.setRecivedQty(recivedQty);
			productDetails.setPrice(Price);
			productDetails.setBasicAmt(basicAmount);
			productDetails.setTax(taxId);
			productDetails.setTaxAmount(taxAmount);
			productDetails.setAmountAfterTax(amountAfterTax);
			productDetails.setTotalAmount(totalAmount);
			productDetails.setHsnCode(hsnCode);
			productDetails.setOtherOrTransportCharges1(OtherOrTransportCharges1);
			productDetails.setTaxOnOtherOrTransportCharges1(TaxOnOtherOrTransportCharges1);
			productDetails.setOtherOrTransportChargesAfterTax1(OtherOrTransportChargesAfterTax1);
			ird.insertCreditNoteDetails(creditNoteDetailsDto,productDetails);
		}
	}
	
	public String changeDateFormat(String input)
	{
		Date date = null;
		try {
			date = new SimpleDateFormat("dd-MMM-yy").parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");  
		String strDate = dateFormat.format(date); 
		return strDate;
	}
	
	
	@Override
	public Map<String, String> TempPoloadProds(String indentNumber,String reqSiteId) {
		return dcFormDao.TempPoloadProds( indentNumber,reqSiteId);
	}
	
	public String checkDcNumberExisted(String dcNumber,String vendorId,String DcDate,String siteId){
		//String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
		return dcFormDao.checkDcNumberExisted(dcNumber,vendorId, DcDate,siteId);
		//return intCount;
	}
	
	public String getTransportorData(String transportorName){
		return dcFormDao.getTransportorData(transportorName);
	}
	public String getTransportorId(String transportorName){
		return dcFormDao.getTransportorId(transportorName);
	}
	
	

}
