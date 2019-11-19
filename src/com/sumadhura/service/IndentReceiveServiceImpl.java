package com.sumadhura.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.StringUtils;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.DebitNoteBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.CreditNoteDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Service("irsClass")
public class IndentReceiveServiceImpl extends UIProperties implements IndentReceiveService {

	@Autowired
	private IndentReceiveDao ird;
	
	@Autowired
	private IndentCreationDao icd;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Override
	public Map<String, String> loadProds(String siteId) {
		return ird.loadProds(siteId);
	}

	@Override
	public String loadSubProds(String prodId) {
		return ird.loadSubProds(prodId);
	}
	@Override
	public String loadSubProdsByPONumber(String prodId,String poNumber,String reqSiteId) {
		return ird.loadSubProdsByPONumber(prodId,poNumber,reqSiteId);
	}

	@Override
	public String loadChildProds(String subProductId) {
		return ird.loadChildProds(subProductId);
	}
	@Override
	public String loadChildProdsByPONumber(String subProductId, String poNumber, String reqSiteId) {
		return ird.loadChildProdsByPONumber(subProductId, poNumber, reqSiteId);
	}

	@Override
	public String loadIndentReceiveMeasurements(String childProdId) {
		return ird.loadIndentReceiveMeasurements(childProdId);
	}

	@Override
	public Map<String, String> getGSTSlabs() {
		return ird.getGSTSlabs();
	}

	@Override
	public int getIndentEntrySequenceNumber() {
		return ird.getIndentEntrySequenceNumber();
	}

	@Override
	public int insertInvoiceData(int indentEntrySeqNum, IndentReceiveDto objIndentReceiveDto) {
		return ird.insertInvoiceData(indentEntrySeqNum,  objIndentReceiveDto);
	}

	@Override
	public String getVendorInfo(String vendName) {
		return ird.getVendorInfo(vendName);
	}

	

	@Override
	public int updateIndentAvalibility(IndentReceiveDto irdto, String siteId) {
		return ird.updateIndentAvalibility(irdto, siteId);
	}

	@Override
	public void updateIndentAvalibilityWithNewIndent(IndentReceiveDto irdto, String siteId) {
		ird.updateIndentAvalibilityWithNewIndent(irdto, siteId);
	}


	@Override
	public String indentProcess(Model model, HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InRe_indPro, ");
		String response1 = "Failed@@_";
		String poNumber="";
		String indentNumber="";
		int poEntryId=0;
		String strPoEntryId="";
		String indentCreationDetailsId="";
		String po_EntryDetailsId="";
		String req_SiteId="";
		
		try{
			Map<String,String> map = new HashMap<String,String>();
			List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
			List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+request.getParameter("InvoiceNumber"));

			String typeOfPurchase=request.getParameter("typeOfPurchase");
			if(typeOfPurchase.equalsIgnoreCase("PO")){
				
				poNumber=request.getParameter("poNumber");
				
			
				
			}else{
				indentNumber=request.getParameter("indentNumber");
			}
			
			map.put("typeOfPurchase",typeOfPurchase);
			map.put("indentNumber",indentNumber);

			map.put("invoiceNum",request.getParameter("InvoiceNumber"));
			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			map.put("recordsCount",recordsCount);
			map.put("poNo",poNumber);
			map.put("dcNo",request.getParameter("dcNo"));
			map.put("invoiceNumber",request.getParameter("InvoiceNumber"));
			map.put("invoiceDate",request.getParameter("InvoiceDate"));
			map.put("vendorName",request.getParameter("VendorName"));
			map.put("vendorId",request.getParameter("VendorId"));
			map.put("vendorAddress",request.getParameter("VendorAddress"));
			map.put("gstinNumber",request.getParameter("GSTINNumber"));
			map.put("note",request.getParameter("Note"));
			map.put("state",request.getParameter("state"));
			map.put("ttlAmntForIncentEntryTbl",request.getParameter("ttlAmntForIncentEntry"));
			map.put("poDate",request.getParameter("poDate"));
			map.put("eWayBillNo",request.getParameter("eWayBillNo"));
			map.put("vehileNo",request.getParameter("vehileNo"));
			map.put("transporterName",request.getParameter("transporterName"));
			map.put("otherChrgs",request.getParameter("otherCharges"));//quotes placed
			map.put("receviedDate",request.getParameter("receivedDate"));
			//ACP
			map.put("moduleName", request.getParameter("moduleName"));
			map.put("requesterName", request.getParameter("RequesterName"));
			map.put("requesterId", request.getParameter("RequesterId"));
			
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");
			map.put("chargesRecordsCount", transRows);

			String transportId ="Conveyance";
			String transportGSTPercentage = "GSTTax";
			String transportGSTAmount = "GSTAmount";
			String totalAmountAfterGSTTax = "AmountAfterTaxx";
			String transportInvoiceId = "TransportInvoice";
			String transportAmount = "ConveyanceAmount";

			int trans_rows = transRows.split("\\|").length;
			for(int charNum=1;charNum<=trans_rows;charNum++){
				Map<String,String> transMap = new HashMap<String,String>();
				transMap.put("transactionDts",request.getParameter(transportId+charNum));
				transMap.put("gstPercentage",request.getParameter(transportGSTPercentage+charNum));
				transMap.put("gstAmount",request.getParameter(transportGSTAmount+charNum));
				transMap.put("totAmtAfterGSTTax",request.getParameter(totalAmountAfterGSTTax+charNum));
				transMap.put("transactionInvoiceId",request.getParameter(transportInvoiceId+charNum));
				transMap.put("transAmount",request.getParameter(transportAmount+charNum));
				transMapList.add(transMap);
			}


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
			String amountAfterTax = "AmountAfterTax";				
			String otherOrTransportCharges = "OtherOrTransportCharges";				
			String taxOnOtherOrTransportCharges = "TaxOnOtherOrTransportCharges";				
			String otherOrTransportChargesAfterTax = "OtherOrTransportChargesAfterTax";				
			String totalAmount = "TotalAmount";
			String otherCharges = "OtherCharges";
			String strRecordsCount[]=recordsCount.split("\\|");
			int records_Count = recordsCount.split("\\|").length;
			for(int i=0;i<records_Count;i++){
				int num=Integer.parseInt(strRecordsCount[i]);
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
				prodMap.put("otherChrgss",request.getParameter("otherChrgs"+num));//quotes placed
				prodMap.put("remarks",request.getParameter("Remarks"+num));
				prodMapList.add(prodMap);		
			}
		//	System.out.println(map);
		//	System.out.println(transMapList);
		//	System.out.println(prodMapList);
			int indentEntrySeqNum = ird.getIndentEntrySequenceNumber();
			session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
			request.setAttribute("indentEntryNo",indentEntrySeqNum);
		
			response1 = indentProcessCommmon(map,transMapList,prodMapList,request,session);
			String[] response_array = response1.split("@@");
			String response = response_array [0];
		/************************************************************check for po number and inactive*************************************/	
		for(Map<String,String> productMap:prodMapList){
				String childProduct = productMap.get("childProduct");
				String childProdsInfo[] = childProduct.split("\\$");					
				String childProdId = childProdsInfo[0];
				double creditQuantity = request.getAttribute(childProdId)==null ? 0.0 : Double.parseDouble(request.getAttribute(childProdId).toString());
				double takenQuantity = Double.parseDouble(productMap.get("quantity").toString());
				String totalQuantity = String.valueOf(takenQuantity);
				
				if(poNumber!=null && !poNumber.equals("") && indentNumber.equals("")){
					
					String retValue=ird.getPoEntryDetailsandIndentCreationDetails(poNumber,childProdId,indentNumber);	
					if(retValue!=null && !retValue.equals("")){
						String data[]=retValue.split("@@");
						 strPoEntryId=data[0];
						 indentCreationDetailsId=data[1];
						 po_EntryDetailsId=data[2];
						 req_SiteId=data[3];
						 String strIndentNumber=data[4];
			
						ird.updateReceiveQuantityInIndentCreationDtls(totalQuantity,indentCreationDetailsId);
						ird.updateAllocatedQuantityInPurchaseDeptTable(totalQuantity,indentCreationDetailsId,request);
						ird.updateReceivedQuantityInPoEntryDetails(totalQuantity,po_EntryDetailsId,request);
						ird.setPOInactive(poNumber,req_SiteId);
						ird.setIndentInactiveAfterChecking(strIndentNumber);
						}
				}
				
				else if(indentNumber!=null && !indentNumber.equals("")){
					String result=ird.getIndentCreationDetailsId(indentNumber,childProdId);
					if(result!=null && !result.equals("")){
					String data[]=result.split("@@");
					indentCreationDetailsId=data[0];
					String strIndentNumber=data[1];
					if(indentCreationDetailsId!=null && !indentCreationDetailsId.equals("")){
					ird.updateReceiveQuantityInIndentCreationDtls(totalQuantity,indentCreationDetailsId);
					ird.updateAllocatedQuantityInPurchaseDeptTable(totalQuantity,indentCreationDetailsId,request);
					ird.setIndentInactiveAfterChecking(strIndentNumber);
					}
					}
				}
		}
			
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
	// this is the common method to execute the receive form,inwards from po,marketing inwards from po
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
		String trasportorId="";
		String strTrsnportorName="";

		try {
			viewGrnPageDataMap = new HashMap<String, String>();
			String recordsCount = map.get("recordsCount");
			//logger.info("");
			logger.info("Records To Be Processed = "+recordsCount);

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
				
				String payment_Req_Date=map.get("payment_Req_Date")==null?"":map.get("payment_Req_Date"); // this is for no of days for the receive from material
				String after_change_Modified_date="";
				
				String moduleName=map.get("moduleName")==null?"":map.get("moduleName");
				
				String requesterName=map.get("requesterName");
				if(requesterName!=null){
					requesterName=requesterName.toUpperCase();
				}
				String requesterId=map.get("requesterId");
				logger.info(invoiceNumber+" <--> "+invoiceDate+" <--> "+vendorName+" <--> "+vendorId+" <--> "+vendorAddress+" <--> "+gstinNumber+" <--> "+hsnCode+" <--> "+ttlAmntForIncentEntryTbl+" <--> "+note);

				String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
				int invoiceCount = ird.getInvoiceCount(  invoiceNumber,  vendorId, strReceiveStartDate, receviedDate);

				if(invoiceCount == 0){

					IndentReceiveDto objIndentReceiveDto = new IndentReceiveDto();
					
				/*************************when the user given receive date then add po no of days to that one  start*********************************************/
					if(payment_Req_Date!=null && !payment_Req_Date.equals("null") && !payment_Req_Date.equals("-") && !payment_Req_Date.equals("") && !payment_Req_Date.equals("0")){
						//String oldDate = "2017-01-29";  
						System.out.println("Date before Addition: "+receviedDate);
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
						Calendar c = Calendar.getInstance();
						try{
						   //Setting the date to the given date
						   c.setTime(sdf.parse(receviedDate));
						}catch(ParseException e){
							e.printStackTrace();
						 }
						   
						//Number of Days to add
						c.add(Calendar.DAY_OF_MONTH,Integer.valueOf(payment_Req_Date));  
						//Date after adding the days to the given date
						after_change_Modified_date= sdf.format(c.getTime());  
						//Displaying the new Date after addition of Days
						System.out.println("Date after Addition: "+after_change_Modified_date);
						
					}
					/****************************************getting the date and add no of days**************************************/
					if(transporterName.contains("$")){
						
						 trasportorId=transporterName.split("\\$")[0];
						 strTrsnportorName=transporterName.split("\\$")[1];
					}else{
						trasportorId=strTrsnportorName=transporterName;
					}
					//pavan settings objects
					objIndentReceiveDto.setStrInvoiceNo(invoiceNumber);
					objIndentReceiveDto.setStrInoviceDate(invoiceDate);
					objIndentReceiveDto.setStrVendorId(vendorId);
					objIndentReceiveDto.setTotalAmnt(ttlAmntForIncentEntryTbl);
					objIndentReceiveDto.setStrRemarks(note);
					objIndentReceiveDto.setStrReceiveDate(receviedDate);
					objIndentReceiveDto.setTransporterName(trasportorId);
					objIndentReceiveDto.seteWayBillNo(eWayBillNo);
					objIndentReceiveDto.setVehileNo(vehileNo);
					objIndentReceiveDto.setPoDate(poDate);
					objIndentReceiveDto.setDcNo(dcNo);
					objIndentReceiveDto.setPoNo(poNo);
					objIndentReceiveDto.setIndentNumber(indentNumber);
					objIndentReceiveDto.setRequesterId(requesterId);
					objIndentReceiveDto.setRequesterName(requesterName);
					objIndentReceiveDto.setPayment_Req_Date(after_change_Modified_date);
					if(moduleName.equals("materialAdjustment")){
						objIndentReceiveDto.setIndentType("INU");	
					    vendorId = map.get("vendorId");
					}
					

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
					indentEntrySeqNum =Integer.valueOf(request.getAttribute("indentEntryNo")==null ? "" : request.getAttribute("indentEntryNo").toString());//ird.getIndentEntrySequenceNumber();
					//session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
					//request.setAttribute("indentEntryNo",indentEntrySeqNum);
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
								if(action==null || action.equalsIgnoreCase("null") || action.equals("") ||  !action.equals("D") ){ // here we need to check the new or delete the tranportation details
								ird.saveTransactionDetails(invoiceNum,transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId, transAmount, site_id, String.valueOf(indentEntrySeqNum));

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
								index++; // which is increase the no of transportation details executed
							}

						}
					}
					/*end 01-sept-17*/ 
					logger.info("Indent Entry Seq. Num. = "+indentEntrySeqNum);

					//pavan added object
					objIndentReceiveDto.setStrUserId(userId);
					objIndentReceiveDto.setStrSiteId(site_id);//change the site id if this is material adjustment 
					if(moduleName.equals("materialAdjustment")){
						objIndentReceiveDto.setStrSiteId(vendorId);//change the site id if this is material adjustment 
					}
					if (state.equals("1")) {
						objIndentReceiveDto.setState("Local");
					} else {
						objIndentReceiveDto.setState("Non Local");
					}
					String grn_no = session.getAttribute("SiteName")+ird.getindentEntrySerialNo(receviedDate);
					objIndentReceiveDto.setGrnNumber(grn_no);
					int insertReceiveReult = ird.insertInvoiceData(indentEntrySeqNum, objIndentReceiveDto);
					
					
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
						for(String num : numOfRec) {

							irdto = new IndentReceiveDto();

							num = num.trim();
							
							//int index = Integer.parseInt(num)-1;

							String product = prodMapList.get(index).get("product");				
							String prodsInfo[] = product.split("\\$");					
							String prodId = prodsInfo[0];
							String prodName = prodsInfo[1];
							//logger.info("Product Id = "+prodId+" and Product Name = "+prodName);

							String subProduct = prodMapList.get(index).get("subProduct");	
							String subProdsInfo[] = subProduct.split("\\$");					
							String subProdId = subProdsInfo[0];
							String subProdName = subProdsInfo[1];					
							//logger.info("Sub Product Id = "+subProdId+" and Sub Product Name = "+subProdName);
							String expiryDate = prodMapList.get(index).get("expiryDate");	
							String childProduct = prodMapList.get(index).get("childProduct");	
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

							String tax = prodMapList.get(index).get("tax");	
							String taxInfo[] = tax.split("\\$");
							String taxId = taxInfo[0];
							String taxPercentage = taxInfo[1];
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
							String otherChrgss = request.getParameter(otherChrgs+num);
							String remarks = prodMapList.get(index).get("remarks");	
							String baiscamt=String.valueOf(basicAmnt);
							double baiscat=Double.parseDouble(baiscamt);
						     String 	basicAmnt1=numberFormat.format(baiscat);
							//amntAfterTax=numberFormat.format(amntAfterTax);

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
							// here checking the child product which available in properties files then 
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
							
							
							 intPriceListSeqNo = ird.getPriceList_SeqNumber(); 
							 intIndentEntryDetailsSeqNo = ird.getIndentEntryDtails_SeqNumber(); 
							 insertIndentReceiveResult = ird.insertIndentReceiveData(indentEntrySeqNum, intIndentEntryDetailsSeqNo,irdto, userId, site_id,intPriceListSeqNo);
							
							
							if(insertIndentReceiveResult >= 1) {
								int updateIndentAvalibilityResult = ird.updateIndentAvalibility(irdto, site_id);
								//viewToBeSelected = "viewGRN";
								result = "Success";
								//Making a new entry if new product.
								if(updateIndentAvalibilityResult == 0) {
									ird.updateIndentAvalibilityWithNewIndent(irdto, site_id);
									//viewToBeSelected = "viewGRN";
									result = "Success";
								}
								//27-July-2017 added by Madhu start
								String id = ird.getIndentAvailableId(irdto, site_id);
								if (StringUtils.isNotBlank(id)) {
									ird.saveReciveDetailsIntoSumduraPriceList(irdto, invoiceNumber, site_id, id,intIndentEntryDetailsSeqNo,intPriceListSeqNo,typeOfPurchase);
								} else {
									String id1 = ird.getProductAvailabilitySequenceNumber();
									ird.saveReciveDetailsIntoSumduraPriceList(irdto, invoiceNumber, site_id, id1,intIndentEntryDetailsSeqNo,intPriceListSeqNo,typeOfPurchase);
								}
								//ird.saveReciveDetailsIntoSumadhuraCloseBalance(irdto, site_id);
								ird.saveReceivedDataIntoSumadhuClosingBalByProduct(irdto, site_id);
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

						tblOneData.append(gstinNumber+"@@"+changeDateFormat(grnDate)+"@@"+invoiceNumber+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+strcontotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+strTrsnportorName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strcongrdtotal+"@@"+strUserName+"@@"+strDate);

						viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
						//start 03-sept
						String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
						viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
						//end 03-sept

						String fnlStr = tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
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
	@Override
	public String loadProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, HttpServletRequest request, HttpSession session) {
		return ird.getProductAvailability(prodId, subProductId, childProdId, measurementId, String.valueOf(request.getSession(true).getAttribute("SiteId").toString()));
	}

	@Override
	public void saveReciveDetailsIntoSumadhuraCloseBalance(
			IndentReceiveDto irdto, String siteId) {

	}

	@Override
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(
			IndentReceiveDto irdto, String siteId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, String> getOtherCharges() {

		return ird.getOtherCharges();
	}



	@Override
	public void saveTransactionDetails(String invoiceNum, String transactionId,
			String gstId, String gstAmount, String totAmtAfterGSTTax,
			String transactionInvoiceId, String transAmount, String siteId,
			String indentEntrySeqNum) {

	}
	
	public int getInvoiceCount(String  strInvoiceNmber, String vendorId, String receiveDate){
		String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
		int intCount = ird.getInvoiceCount(strInvoiceNmber, vendorId, strReceiveStartDate, receiveDate);
		return intCount;
	}
	
	public int isInvoicesaved(HttpServletRequest request){
		String invoiceNumber = request.getParameter("InvoiceNumber");
		String vendorId = request.getParameter("VendorId");
		String receviedDate = request.getParameter("receivedDate");
		//String strReceiveStartDate  = validateParams.getProperty("INDENTRECSTARTDT") == null ? "" : validateParams.getProperty("INDENTRECSTARTDT").toString();
		int intCount = ird.getInvoiceSaveCount(  invoiceNumber,  vendorId, receviedDate);
		logger.info("Indent_entry id "+intCount +"invoice no "+invoiceNumber);
		return intCount;
	}

	@Override
	public List<Map<String, Object>> getListOfActivePOs(String site_id) {
		return ird.getListOfActivePOs(site_id);
	}
	@Override
	public List<Map<String, Object>> getListOfActiveMarketingPOs(String site_id,String status) {
		return ird.getListOfActiveMarketingPOs(site_id,status);
	}
	@Override
	public List<ProductDetails> getMarketingPODetails(String poNumber, String reqSiteId) {
		return ird.getMarketingPODetails(poNumber, reqSiteId);
	}
	@Override
	public List<ProductDetails> getPODetails(String poNumber, String reqSiteId) {
		return ird.getPODetails(poNumber, reqSiteId);
	}
	@Override
	public List<ProductDetails> getProductDetailsLists(String poNumber,String reqSiteId) {
		return ird.getProductDetailsLists(poNumber,reqSiteId);
	}
	@Override
	public List<ProductDetails> getTransChrgsDtls(String poNumber,String reqSiteId) {
		return ird.getTransChrgsDtls(poNumber,reqSiteId);
	}
	
	@Override
	public String processingPOasInvoice(Model model, HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InRe_proPOI, ");
		Map<String,String> map = new HashMap<String,String>();
		List<Map<String,String>> transMapList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> prodMapList = new ArrayList<Map<String,String>>();
		String finalresponse = "Failed@@_";
		try{
			WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , PONumber:"+request.getParameter("poNo")+" , InvoiceNumber:"+request.getParameter("InvoiceorDCNumber"));

			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			
			map.put("typeOfPurchase","PO");
			map.put("poNo",request.getParameter("poNo"));
			map.put("invoiceNumber",request.getParameter("InvoiceorDCNumber"));
			map.put("invoiceDate",request.getParameter("InvoiceorDCDate"));
			map.put("vendorName",request.getParameter("vendorName"));
			map.put("vendorId",request.getParameter("VendorId"));
			map.put("vendorAddress",request.getParameter("vendorAddress"));
			map.put("gstinNumber",request.getParameter("strGSTINNumber"));
			map.put("note",request.getParameter("Note"));
			map.put("state",request.getParameter("state"));
			map.put("ttlAmntForIncentEntryTbl",request.getParameter("ttlAmntForIncentEntry"));
			map.put("poDate",request.getParameter("poDate"));
			map.put("eWayBillNo",request.getParameter("eWayBillNo"));
			map.put("vehileNo",request.getParameter("vehileNo"));
			map.put("transporterName",request.getParameter("transporterName"));
			map.put("otherChrgs",request.getParameter("otherCharges"));//quotes placed
			map.put("receviedDate",request.getParameter("receivedDate"));
			map.put("payment_Req_Date",request.getParameter("payment_Req_Date")); // no of days for the payment request days 
			
			String transRows = request.getParameter("numbeOfChargesRowsToBeProcessed");
			map.put("chargesRecordsCount", transRows);
			String[] recordsCountArray = recordsCount.split("\\|1\\|");
			recordsCount = recordsCountArray[0];
			recordsCount = recordsCount + "|";
			String creditRows="";
			if(recordsCountArray.length>1){
				creditRows = recordsCountArray[1]; 
			}
			creditRows = "1|" + creditRows;
			
			map.put("creditRows", creditRows);
			
			
			String transportId ="Conveyance";
			String transportGSTPercentage = "GSTTax";
			String transportGSTAmount = "GSTAmount";
			String totalAmountAfterGSTTax = "AmountAfterTax";
			String transportInvoiceId = "TransportInvoice";
			String transportAmount = "ConveyanceAmount";

			String trans_rows[] = transRows.split("\\|");
			for(String trans : trans_rows){
				int charNum=Integer.parseInt(trans);
				Map<String,String> transMap = new HashMap<String,String>();
				transMap.put("transactionDts",request.getParameter(transportId+charNum));
				transMap.put("gstPercentage",request.getParameter(transportGSTPercentage+charNum));
				transMap.put("gstAmount",request.getParameter(transportGSTAmount+charNum));
				transMap.put("totAmtAfterGSTTax",request.getParameter(totalAmountAfterGSTTax+charNum));
				transMap.put("transactionInvoiceId",request.getParameter(transportInvoiceId+charNum));
				transMap.put("transAmount",request.getParameter(transportAmount+charNum));
				transMap.put("action",request.getParameter("TransportconveyanceInvoice"+charNum));
				transMapList.add(transMap);
			}


			String prod = "mainProduct";
			String subProd = "mainSubProduct";
			String childProd = "mainChildProduct";
			String qty = "mainquantity";
			//String expireDate = "mainexpireDate";
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
			String expireDate = "expireDate";
			
			
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
				prodMap.put("otherChrgss",request.getParameter("otherChrgs"+num));//quotes placed
				prodMap.put("remarks",request.getParameter("InvoiceRemarks"+num));
				prodMap.put("indentCreationDetailsId",request.getParameter("mainindentCreationDetailsId"+num));	
				prodMap.put("poEntryDetailsId",request.getParameter("mainPoEntryDetailsId"+num));	
				
				prodMapList.add(prodMap);
				recordsCountModified=recordsCountModified+(index+1)+"|";
			}
		//	System.out.println(map);
		//	System.out.println(transMapList);
		//	System.out.println(prodMapList);
			map.put("recordsCount",recordsCountModified);
			
			String indentNumber = request.getParameter("indentNumber");
			String reqSiteId = request.getParameter("toSiteId");
			String poNo = map.get("poNo");

			//============================
			int indentEntrySeqNum = ird.getIndentEntrySequenceNumber();
			session.setAttribute("indentEntrySeqNum",indentEntrySeqNum);
			request.setAttribute("indentEntryNo",indentEntrySeqNum);
			
			String response1 = indentProcessCommmon(map,transMapList,prodMapList,request,session);

			//============================
			String[] response_array = response1.split("@@");
			String response = response_array [0];
			String response2 = "Failed";

			//String response = "Success",response1 = "Success",response2 = "Success"; //remove this line
			if (response.equalsIgnoreCase("Success")){
				try {
					//doing Credit Note
					String credit_for = request.getParameter("credit_for")==null?"":request.getParameter("credit_for").toString();
					if(StringUtils.isNotBlank(credit_for)){
					doCreditNote(map,request,credit_for,"INV");
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
					String creditNoteNumber = request.getParameter("creditNoteNumber");
					String creditTotalAmount = request.getParameter("creditTotalAmount");
					String indentEntryNo = request.getAttribute("indentEntryNo").toString();
					String site_id = String.valueOf(session.getAttribute("SiteId"));
					ird.updateInvoiceNoInAccPaymentTbl(map.get("invoiceNumber"),map.get("ttlAmntForIncentEntryTbl"),map.get("invoiceDate"),map.get("receviedDate"),map.get("poNo"),map.get("vendorId"),creditNoteNumber,creditTotalAmount,indentEntryNo,site_id);
					response2 = "Success";
				} catch (Exception e) {
					response2 = "Failed";
					e.printStackTrace();
				}
				
			}else{}
			if(response2.equalsIgnoreCase("Success")){
				transactionManager.commit(status);
				WriteTrHistory.write("Tr_Completed");
				finalresponse = response1;
			}
			else{
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				finalresponse = "Failed@@_";
			}
			//===============================
		}
		catch(Exception e){
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			finalresponse = "Failed@@_";
			e.printStackTrace();
		}
		return finalresponse;
	
	}

	public void doCreditNote(Map<String, String> map, HttpServletRequest request, String credit_for, String INVorDC){
		int creditSeqNo = ird.getCreditNoteSequenceNumber();
		String creditNoteNumber = request.getParameter("creditNoteNumber");
		
		String indentEntryNo = "";
		String invoiceNumber = "";
		String dcEntryNo = "";
		String dcNumber = "";
		if(INVorDC.equals("INV")){
			indentEntryNo = request.getAttribute("indentEntryNo").toString();
			invoiceNumber = map.get("invoiceNumber");
		}
		if(INVorDC.equals("DC")){
			dcEntryNo = request.getAttribute("dcEntryNo").toString();
			dcNumber = map.get("dcNo");
		}
		
		String creditTotalAmount = "";
		if(credit_for.equals("QUANTITY")){
			creditTotalAmount  = request.getParameter("creditTotalAmount");
		}
		else if(credit_for.equals("PRICE")){
			creditTotalAmount  = request.getParameter("creditNotePrice");
		}
		else if(credit_for.equals("CONVEYANCE")){
			creditTotalAmount  = request.getParameter("creditNoteConveyanceAmount");
		}
		else{
			throw new RuntimeException("Credit Note Type Does not Match.");
		}
		CreditNoteDto creditNoteDto = new CreditNoteDto();
		creditNoteDto.setCreditSeqNo(creditSeqNo);
		creditNoteDto.setCreditNoteNumber(creditNoteNumber);
		if(INVorDC.equals("INV")){
		creditNoteDto.setIndentEntryId(indentEntryNo);
		creditNoteDto.setInvoiceNumber(invoiceNumber);
		}
		if(INVorDC.equals("DC")){
		creditNoteDto.setDcEntryId(dcEntryNo);
		creditNoteDto.setDcNumber(dcNumber);
		}
		creditNoteDto.setCreditTotalAmount(creditTotalAmount);
		creditNoteDto.setCreditFor(credit_for);
		ird.insertCreditNote(creditNoteDto);
		
		if(!credit_for.equals("QUANTITY")){
			return;
		}
		
		String creditRows = map.get("creditRows");
		int creditRowsCount = creditRows.split("\\|").length;
		for(int num=1;num<=creditRowsCount;num++)
		{
			int creditNoteDtlsSeqId = ird.getCreditNoteDetailsSequenceNumber();
			CreditNoteDto creditNoteDetailsDto = new CreditNoteDto();
			creditNoteDetailsDto.setCreditNoteDtlsSeqId(creditNoteDtlsSeqId);
			creditNoteDetailsDto.setCreditSeqNo(creditSeqNo);
			if(INVorDC.equals("INV")){
			creditNoteDetailsDto.setIndentEntryId(indentEntryNo);
			}
			
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

	@Override
	public String getPriceRatesByChildProduct(String childProdId, String poNumber, String reqSiteId) {
		return ird.getPriceRatesByChildProduct( childProdId,  poNumber,  reqSiteId);
	}

	@Override
	public int checkPOisActive(String poNumber) {
		return ird.checkPOisActive(poNumber);
	}
	
	public int getCheckIndentAvailable(String  indentNumber){
		
		 
		return ird.getCheckIndentAvailable(indentNumber);
	}
	@Override
	public int getCheckPoAvailable(String poNumber,String vendorId) {
		return ird.getCheckPoAvailable(poNumber,vendorId);
	}

	@Override
	public List<MarketingDeptBean> getPOProductLocationDetails(String poNumber) {
		return ird.getPOProductLocationDetails(poNumber);
	}
	
	@Override
	public List<Map<String, Object>> loadInvoiceNumberByVendorId(String site_id, String vendorId) {
		return ird.loadInvoiceNumberByVendorId(site_id,vendorId);	
	}
	@Override
	public GetInvoiceDetailsBean getSubmitTaxInvoiceFromAndToDetails(String user_id, GetInvoiceDetailsBean bean) {
		return ird.getSubmitTaxInvoiceFromAndToDetails(user_id,bean);	
	}
	@Override
	public List<Map<String, Object>> loadAllTaxInvoicesDetails(GetInvoiceDetailsBean bean) {
		return ird.loadAllTaxInvoicesDetails(bean);
		}
	@Override
	public List<Map<String, Object>> receiveTaxInvoicesDetails(GetInvoiceDetailsBean bean,String status,String condition){
		List<Map<String, Object>> list=	ird.receiveTaxInvoicesDetails(bean,status,condition);
		return list;
	}
	
	@Override
	public String saveTaxInvoicesDetails(HttpServletRequest request, HttpSession session,RedirectAttributes redir) {
		String response="";
		String site_id="";
		String userId="";
		String userName="";
		String invoiceNumber="";
		String indentEntryId="";
		String vendorId="";
		String nextLevelApprovalEmpId="";
		String remarks="";
		int countInsertedRecord=0;
		String poId = "";
		String poDate = "";
		String receivedOrIssuedDate = "";
		String invoiceDate = "";
		String invoiceAmount = "";
		String vendorName = "";
		String siteName = "";
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			WriteTrHistory.write("Tr_Opened in  Submit Tax Invoices, ");
			 
			String[] recordToProceed=request.getParameterValues("recordsToproceed")==null?new String[]{}:request.getParameterValues("recordsToproceed");
			userId=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			userName=session.getAttribute("UserName")==null?"":session.getAttribute("UserName").toString();
			
			String emp = ird.getEmployeeDetails(userId);
			
			site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
			String strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			nextLevelApprovalEmpId=request.getParameter("approverEmpId")==null?"":request.getParameter("approverEmpId");
			List<GetInvoiceDetailsBean> invoiceDetialsBean=new ArrayList<GetInvoiceDetailsBean>();
			GetInvoiceDetailsBean bean=null;
			int sno = 0;
			for (String rowNum : recordToProceed) {
				rowNum=rowNum.trim();
				 bean=new  GetInvoiceDetailsBean();
				invoiceNumber=request.getParameter("invoiceNumber"+rowNum)==null?"":request.getParameter("invoiceNumber"+rowNum).split("@@")[0];
				indentEntryId=request.getParameter("indentEntryId"+rowNum)==null?"":request.getParameter("indentEntryId"+rowNum);
				vendorId=request.getParameter("vendorId"+rowNum)==null?"":request.getParameter("vendorId"+rowNum);
				remarks=request.getParameter("remarks"+rowNum)==null?"":request.getParameter("remarks"+rowNum);
				poId=request.getParameter("poId"+rowNum)==null?"":request.getParameter("poId"+rowNum);
				poDate=request.getParameter("poDate"+rowNum)==null?"":request.getParameter("poDate"+rowNum);
				receivedOrIssuedDate=request.getParameter("receivedOrIssuedDate"+rowNum)==null?"":request.getParameter("receivedOrIssuedDate"+rowNum);
				invoiceDate=request.getParameter("invoiceDate"+rowNum)==null?"":request.getParameter("invoiceDate"+rowNum);
				invoiceAmount=request.getParameter("invoiceAmount"+rowNum)==null?"":request.getParameter("invoiceAmount"+rowNum);
				vendorName=request.getParameter("vendorName"+rowNum)==null?"":request.getParameter("vendorName"+rowNum);
				siteName=request.getParameter("siteName"+rowNum)==null?"":request.getParameter("siteName"+rowNum);
				
				bean.setInvoiceNumber(invoiceNumber);
				bean.setIndentEntryId(indentEntryId);
				bean.setVendorId(vendorId);
				if(remarks.length()!=0){
					remarks=strUserName+" - "+remarks;
				}
				bean.setRemarks(remarks);
				bean.setSiteId(site_id);
				bean.setUserId(userId);
				bean.setPoNo(poId);
				bean.setPoDate(poDate);
				bean.setReceivedDate(receivedOrIssuedDate);
				bean.setInvoiceDate(invoiceDate);
				bean.setTotalAmount(invoiceAmount);
				bean.setVendorName(vendorName);
				bean.setStrSerialNo(++sno);
				bean.setSubmittedBy(emp);
				bean.setReceivedBy1("");
				bean.setReceivedBy2("");
				bean.setReceivedBy3("");
				bean.setReceivedBy4("");
				
				invoiceDetialsBean.add(bean);
			}
			
		 
			
			countInsertedRecord=ird.saveTaxInvoicesApprRejctDetails(invoiceDetialsBean,"C");
			countInsertedRecord= ird.saveTaxInvoicesData(invoiceDetialsBean,nextLevelApprovalEmpId);	
			
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			redir.addFlashAttribute("invoiceDetialsBean", invoiceDetialsBean);
			redir.addFlashAttribute("siteName", siteName);
			response="Success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
			response="Failed";
		}
		
		return response;
	}
	
	@Override
	public String approveTaxInvoicesDetails(HttpServletRequest request, HttpSession session, RedirectAttributes redir) {
		String response="";
		String site_id="";
		String userId="";
		String invoiceNumber="";
		String indentEntryId="";
		String vendorId="";
		String accTaxInvoiceDetailsID="";
		String approveReject="";
		String nextLevelApprovalEmpId="";
		String remarks="";
		int countInsertedRecord=0;
		String poId = "";
		String poDate = "";
		String receivedOrIssuedDate = "";
		String invoiceDate = "";
		String invoiceAmount = "";
		String vendorName = "";
		String siteName = "";
		List<String> listOfIndentEntryIds = new ArrayList<String>();
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			WriteTrHistory.write("Tr_Opened in  Submit Tax Invoices, ");
			 
			String[] recordToProceed=request.getParameterValues("recordsToproceed")==null?new String[]{}:request.getParameterValues("recordsToproceed");
			userId=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
			String strUserName =  session.getAttribute("UserName") == null ? "" :  session.getAttribute("UserName").toString();
			nextLevelApprovalEmpId=request.getParameter("approverEmpId")==null?"":request.getParameter("approverEmpId");
			List<GetInvoiceDetailsBean> invoiceDetialsBean=new ArrayList<GetInvoiceDetailsBean>();
			GetInvoiceDetailsBean bean=null;
			int sno = 0;
			for (String rowNum : recordToProceed) {
				rowNum=rowNum.trim();
				bean=new  GetInvoiceDetailsBean();
				approveReject=request.getParameter("approveReject"+rowNum)==null?"":request.getParameter("approveReject"+rowNum);
				if(!approveReject.equals("Approve")){
					continue;
				}
				invoiceNumber=request.getParameter("invoiceNumber"+rowNum)==null?"":request.getParameter("invoiceNumber"+rowNum).split("@@")[0];
				indentEntryId=request.getParameter("indentEntryId"+rowNum)==null?"":request.getParameter("indentEntryId"+rowNum);
				accTaxInvoiceDetailsID=request.getParameter("accTaxInvoiceDetailsID"+rowNum)==null?"":request.getParameter("accTaxInvoiceDetailsID"+rowNum);
				//nextLevelApprovalEmpId=request.getParameter("approverEmpId"+rowNum)==null?"":request.getParameter("approverEmpId"+rowNum);
				remarks=request.getParameter("remarks"+rowNum)==null?"":request.getParameter("remarks"+rowNum);
				poId=request.getParameter("poId"+rowNum)==null?"":request.getParameter("poId"+rowNum);
				poDate=request.getParameter("poDate"+rowNum)==null?"":request.getParameter("poDate"+rowNum);
				receivedOrIssuedDate=request.getParameter("receivedOrIssuedDate"+rowNum)==null?"":request.getParameter("receivedOrIssuedDate"+rowNum);
				invoiceDate=request.getParameter("invoiceDate"+rowNum)==null?"":request.getParameter("invoiceDate"+rowNum);
				invoiceAmount=request.getParameter("invoiceAmount"+rowNum)==null?"":request.getParameter("invoiceAmount"+rowNum);
				vendorName=request.getParameter("vendorName"+rowNum)==null?"":request.getParameter("vendorName"+rowNum);
				siteName=request.getParameter("siteName"+rowNum)==null?"":request.getParameter("siteName"+rowNum);
				
				
				bean.setInvoiceNumber(invoiceNumber);
				bean.setIndentEntryId(indentEntryId);
				bean.setAccTaxInvoiceDetailsID(accTaxInvoiceDetailsID);
				if(remarks.length()!=0){
					remarks=strUserName+" - "+remarks;
				}
				bean.setRemarks(remarks);
				bean.setSiteId(site_id);
				bean.setUserId(userId);
				bean.setPoNo(poId);
				bean.setPoDate(poDate);
				bean.setReceivedDate(receivedOrIssuedDate);
				bean.setInvoiceDate(invoiceDate);
				bean.setTotalAmount(invoiceAmount);
				bean.setVendorName(vendorName);
				bean.setStrSerialNo(++sno);
				listOfIndentEntryIds.add(indentEntryId);
				invoiceDetialsBean.add(bean);
			}
			
			countInsertedRecord=ird.saveTaxInvoicesApprRejctDetails(invoiceDetialsBean,"A");
			countInsertedRecord= ird.approveTaxInvoicesData(invoiceDetialsBean,nextLevelApprovalEmpId);	
			ird.setApprovalEmployeeDetails(listOfIndentEntryIds, invoiceDetialsBean);
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			redir.addFlashAttribute("invoiceDetialsBean", invoiceDetialsBean);
			redir.addFlashAttribute("siteName", siteName);
			response="Success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
			response="Failed";
		}
		
		return response;
	}
	
	@Override
	public String rejectTaxInvoicesDetails(HttpServletRequest request, HttpSession session) {
		String response="";
		String site_id="";
		String userId="";
		String invoiceNumber="";
		String indentEntryId="";
		String vendorId="";
		String accTaxInvoiceDetailsID="";
		String approveReject="";
		String nextLevelApprovalEmpId="";
		String remarks="";
		int countInsertedRecord=0;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			WriteTrHistory.write("Tr_Opened in  Submit Tax Invoices, ");
			 
			String[] recordToProceed=request.getParameterValues("recordsToproceed")==null?new String[]{}:request.getParameterValues("recordsToproceed");
			userId=session.getAttribute("UserId")==null?"":session.getAttribute("UserId").toString();
			site_id=request.getParameter("site_id")==null?"":request.getParameter("site_id");
			nextLevelApprovalEmpId=request.getParameter("approverEmpId")==null?"":request.getParameter("approverEmpId");
			List<GetInvoiceDetailsBean> invoiceDetialsBean=new ArrayList<GetInvoiceDetailsBean>();
			GetInvoiceDetailsBean bean=null;
			for (String rowNum : recordToProceed) {
				rowNum=rowNum.trim();
				bean=new  GetInvoiceDetailsBean();
				approveReject=request.getParameter("approveReject"+rowNum)==null?"":request.getParameter("approveReject"+rowNum);
				if(!approveReject.equals("Reject")){
					continue;
				}
				invoiceNumber=request.getParameter("invoiceNumber"+rowNum)==null?"":request.getParameter("invoiceNumber"+rowNum).split("@@")[0];
				indentEntryId=request.getParameter("indentEntryId"+rowNum)==null?"":request.getParameter("indentEntryId"+rowNum);
				accTaxInvoiceDetailsID=request.getParameter("accTaxInvoiceDetailsID"+rowNum)==null?"":request.getParameter("accTaxInvoiceDetailsID"+rowNum);
				remarks=request.getParameter("remarks"+rowNum)==null?"":request.getParameter("remarks"+rowNum);
				
				
				bean.setInvoiceNumber(invoiceNumber);
				bean.setIndentEntryId(indentEntryId);
				bean.setAccTaxInvoiceDetailsID(accTaxInvoiceDetailsID);
				bean.setRemarks(remarks);
				bean.setSiteId(site_id);
				bean.setUserId(userId);
				invoiceDetialsBean.add(bean);
			}
			
			countInsertedRecord=ird.saveTaxInvoicesApprRejctDetails(invoiceDetialsBean,"R");
			countInsertedRecord= ird.rejectTaxInvoicesData(invoiceDetialsBean,nextLevelApprovalEmpId);	
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			response="Success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
			response="Failed";
		}
		
		return response;
	}
	/********************************************* this is for receiving quantity start***************************************************/
	
	public String getAndCheckReceiveBOQQuantity(HttpSession session,HttpServletRequest request,String siteId) {

		
		String recordsCount = request.getParameter("numbeOfRowsToBeProcessed")==null ? "" : request.getParameter("numbeOfRowsToBeProcessed");
		String[] recordsCountArray = recordsCount.split("\\|1\\|");
		recordsCount = recordsCountArray[0];
		recordsCount = recordsCount + "|";
		int records_Count = recordsCount.split("\\|").length;
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
		double receiveQuantity=0.0;
		/*if(approveOrNot){
			numOfRec = new 	String[Integer.parseInt(recordsCount)];
			for(int k=0;k<Integer.parseInt(recordsCount);k++){
			numOfRec [k]=String.valueOf(k+1);
			}
		//	Arrays.toString(numOfRec);
		}*/
		if((recordsCount != null) && (!recordsCount.equals(""))) {
			numOfRec = recordsCount.split("\\|");
		}
		
		/*String[] records_Array = recordsCount.split("\\|");
		for(int index=0;index<records_Count;index++){
			int num=Integer.parseInt(records_Array[index]);
		}*/
		
		if(numOfRec != null && numOfRec.length > 0) {
			for(String num : numOfRec) {
				num = num.trim();
				//String actionValue = request.getParameter("actionValue"+num)==null ? "" : request.getParameter("actionValue"+num);
				childProduct=request.getParameter("mainChildProduct"+num)==null ? request.getParameter("ChildProduct"+num)==null ? "" : request.getParameter("ChildProduct"+num) : request.getParameter("mainChildProduct"+num);
				measurement=request.getParameter("mainUnitsOfMeasurement"+num)==null ? request.getParameter("UnitsOfMeasurement"+num)==null ? "" : request.getParameter("UnitsOfMeasurement"+num) : request.getParameter("mainUnitsOfMeasurement"+num);
				groupId=request.getParameter("groupId"+num)==null ? "" : request.getParameter("groupId"+num);
				oldQuantity=Double.valueOf(request.getParameter("mainquantity"+num)==null ? request.getParameter("Quantity"+num)==null ? request.getParameter("quantity"+num)==null ?  "0": request.getParameter("quantity"+num) : request.getParameter("Quantity"+num) : request.getParameter("mainquantity"+num));
				//if(childProduct.contains("$")){
				childProdId = childProduct.split("\\$")[0];
				childProdName= childProduct.split("\\$")[1];
				measurementId=measurement.split("\\$")[0];
				measurementName=measurement.split("\\$")[1];
			//	}
				
				// if(!actionValue.equals("R")){
					
					 
					/* if(oldQuantity==0){
					 enteredQuantity=Double.valueOf(request.getParameter("Quantity"+num)==null ? "" : request.getParameter("Quantity"+num));}
					 else{enteredQuantity=Double.valueOf(request.getParameter("RequiredQuantity"+num)==null ? "" : request.getParameter("RequiredQuantity"+num));}*/
					
				// }
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
						//indentPendingQuantity=icd.getIndentPendingQuantity(childProductList,siteId,measurementList);
						totalBOQQuantity=icd.getBOQQuantity(groupId,siteId);
						for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
							childProductMap.put(entry.getKey(),receivedQuantity+"_"+issuedQuantity+"_"+totalBOQQuantity);
						}
					}else{
						String temp_Quantity =(childProductMap.get(childProdId));
						String info[]=temp_Quantity.split("_");
						 receivedQuantity=Double.valueOf(info[0]);
						 issuedQuantity=Double.valueOf(info[1]);
						// indentPendingQuantity=Double.valueOf(info[2]);
						 totalBOQQuantity=Double.valueOf(info[2]);
					}
					if(!groupIdsSortMap.containsKey(groupId)){
						
						if(oldQuantity!=0){
							//double checkQuantity=oldQuantity;
							receiveQuantity=oldQuantity;
							//indentPendingQuantity=indentPendingQuantity-checkQuantity;
							groupIdsSortMap.put(groupId,((receiveQuantity)));
							}/*else if(oldQuantity==0){
							indentPendingQuantity+=enteredQuantity;//-checkQuantity;
							groupIdsSortMap.put(groupId,(enteredQuantity));
							}*/
						//tempQuantity=enteredQuantity; // curretly entered quantity
					}else{
						if(oldQuantity!=0){
							//double value=0.0;
							double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
							double checkQuantity=oldQuantity;
							receiveQuantity=DoublePrevQuantity+checkQuantity;
							groupIdsSortMap.put(groupId,(receiveQuantity));
							//receiveQuantity=value;
						}/*else if(oldQuantity==0){
							double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
							tempQuantity = Double.valueOf(enteredQuantity)+DoublePrevQuantity; // currently entered quantity
							indentPendingQuantity+=tempQuantity;
							groupIdsSortMap.put(groupId,(tempQuantity));}*/
					}
						//totalQuantity=totalBOQQuantity-receivedQuantity+issuedQuantity;
					totalQuantity=receivedQuantity-(issuedQuantity)+receiveQuantity;//<=totalBOQQuantity;

					if(totalQuantity<=totalBOQQuantity){
						continue;
					}else{
						response="You can not initiate Child Product \""+childProdName+"\" more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
						break;
					}

				}
				
				
			}
		}
		return response;
	}

	@Override
	public void getCreditNoteDetails(HttpServletRequest request, Model model) {
		
		String indentEntryId = request.getParameter("indentEntryId")==null?"":request.getParameter("indentEntryId");
		String dcEntryId = request.getParameter("dcEntryId")==null?"":request.getParameter("dcEntryId");
		String vendorId = request.getParameter("vendorId")==null?"":request.getParameter("vendorId");
		String siteId = request.getParameter("siteId")==null?"":request.getParameter("siteId");
		String poEntryId = request.getParameter("poEntryId")==null?"":request.getParameter("poEntryId");
		
		DebitNoteBean Totals = new DebitNoteBean();
		DebitNoteBean CreditNote = ird.getCreditNote(indentEntryId, dcEntryId);
		List<DebitNoteBean> CreditNoteDetails = null;
		if(CreditNote.getType().equals("QUANTITY")){
			CreditNoteDetails = ird.getCreditNoteDetails(indentEntryId, dcEntryId, Totals);
		}else{
			CreditNoteDetails = null;
		}
		DebitNoteBean VendorDetails = ird.getVendorDetails(vendorId);
		DebitNoteBean InvoiceDetails = ird.getInvoiceDetails(indentEntryId);
		DebitNoteBean DCDetails = ird.getDCDetails(dcEntryId);
		DebitNoteBean SiteDetailsFromVendor = ird.getSiteDetailsFromVendorTable(siteId);
		DebitNoteBean SiteDetails = ird.getSiteDetails(siteId);
		List<DebitNoteBean> POEntryDetails = ird.getPOEntryDetails(poEntryId);
		if(CreditNote.getType().equals("QUANTITY")){
			setDiscountForProducts(CreditNoteDetails,POEntryDetails,Totals);
		}
		else{
			Totals.setTotalAmountAfterTax(CreditNote.getCreditTotalAmount());
		}
		model.addAttribute("CreditNoteDetails", CreditNoteDetails);
		model.addAttribute("CreditNote", CreditNote);
		model.addAttribute("VendorDetails", VendorDetails);
		model.addAttribute("InvoiceDetails", InvoiceDetails);
		model.addAttribute("DCDetails", DCDetails);
		model.addAttribute("SiteDetailsFromVendor", SiteDetailsFromVendor);
		model.addAttribute("SiteDetails", SiteDetails);
		model.addAttribute("POEntryDetails", POEntryDetails);
		model.addAttribute("Totals", Totals);
		
		
		String addr = ird.getSiteAddressByUsingSiteId(siteId);                  //HYD //BANGLORE
		String addrLoc = "";
		if(addr==null){addrLoc="";}
		if(addr.equals("HYD")){addrLoc="HYDERABAD";}
		if(addr.equals("BANGLORE")){addrLoc="BENGALORE";}
        String address = validateParams.getProperty("BILLING_ADDRESSS_"+addrLoc) == null ? "" : validateParams.getProperty("BILLING_ADDRESSS_"+addrLoc).toString();
   		String gstinNo = validateParams.getProperty("GSTIN_"+addrLoc) == null ? "" : validateParams.getProperty("GSTIN_"+addrLoc).toString();
   		
   		model.addAttribute("address", address);
		model.addAttribute("gstinNo", gstinNo);
		
		
		String urlName = request.getParameter("urlName");
		model.addAttribute("urlName",urlName);
		
		
	}

	private void setDiscountForProducts(List<DebitNoteBean> CreditNoteDetails, List<DebitNoteBean> POEntryDetails, DebitNoteBean Totals) {
		DecimalFormat df2 = new DecimalFormat("#.##"); 
		double totalDiscount = 0;
		double totalAmountBeforeTax = 0;
		for(DebitNoteBean bean : CreditNoteDetails){
			String childProductId = bean.getChildProductId();
			for(DebitNoteBean innerbean : POEntryDetails){
				if(innerbean.getChildProductId().equals(childProductId)){
					double basic = Double.valueOf(bean.getBasicAmount());
					double discountPercentage = Double.valueOf(innerbean.getDiscount());
					double discount = (basic*discountPercentage)/100;
					double taxableValue = (basic*(100-discountPercentage))/100;
					bean.setDiscount(df2.format(discount));
					bean.setTaxableValue(df2.format(taxableValue));
					totalDiscount += discount;
					totalAmountBeforeTax += taxableValue;
					break;
				}
			}
		}
		Totals.setTotalDiscount(df2.format(totalDiscount));
		Totals.setTotalAmountBeforeTax(df2.format(totalAmountBeforeTax));
	}

	@Override
	public boolean isIndentEntryIdHasCreditNote(String strIndentEntryId) {
		return ird.isIndentEntryIdHasCreditNote(strIndentEntryId);
	}
	
}
