package com.sumadhura.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
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
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.transdao.InwardGetInvoiceDetailsDao;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Service("getInvoiceDetailsServiceClass")
public class InwardToGetInvoiceDetailsServiceImpl extends UIProperties implements InwardToGetInvoiceDetailsService {

	@Autowired
	private InwardGetInvoiceDetailsDao inwardGetInvoiceDetailsClass;


	@Autowired
	PlatformTransactionManager transactionManager;
	
	@Autowired
	IndentReceiveDao ird;
	
	@Override
	public String getGrnDetails(String invoiceNumber, String siteId,HttpServletRequest request) {
		Map<String, String> viewGrnPageDataMap = null;
		viewGrnPageDataMap =new HashMap<String, String>();
		String result="Success";
		String tblOneData="";
		String tbltwoData="";
		String tblCharges="";
		String state="";
		String strVendorId = "";
		String invoiceDate = "";
		String indentEntryId="";
		//	List<GetInvoiceDetailsBean> GrnIssueToOtherSiteList = null;
		try {
			strVendorId = request.getParameter("vendorId") == null ? "" : request.getParameter("vendorId");
			invoiceDate = request.getParameter("invoiceDate") == null ? "" : request.getParameter("invoiceDate");
			indentEntryId=request.getParameter("indentEntryId") == null ? "" : request.getParameter("indentEntryId");
			//ACP
			String type=request.getParameter("type")==null?"":request.getParameter("type");
			String selectIndentType=request.getParameter("indentType")==null?"":request.getParameter("indentType");
			if(type.equalsIgnoreCase("invoicePriceMaster")){siteId=request.getParameter("siteId");}
			//get GRN number
			tblOneData+= inwardGetInvoiceDetailsClass.getVendorDetails(indentEntryId, siteId,strVendorId,invoiceDate,selectIndentType);
			tbltwoData+= inwardGetInvoiceDetailsClass.getProductDetails(invoiceNumber,siteId,strVendorId,invoiceDate);
			state=request.getAttribute("state").toString();
			tblCharges+=inwardGetInvoiceDetailsClass.getTransportChargesListForGRN(invoiceNumber,siteId,state,strVendorId,invoiceDate,indentEntryId);
			viewGrnPageDataMap.put("tblOneData", tblOneData.toString());

			String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);
			viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
				
				//table three data
			String fnlStr = tbltwoData.toString().substring(0, tbltwoData.toString().length() - 2);
			viewGrnPageDataMap.put("tblTwoData", fnlStr);
			request.setAttribute("viewGrnPageData", viewGrnPageDataMap);

			//transactionManager.commit(status);		
			 viewGrnPageDataMap.put("tblOneData", tblOneData.toString());
			
		}catch (Exception e) {
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
		public List<GetInvoiceDetailsBean> getIssuesToOtherLists(String indentEntryId, String siteId,String vendorName,String invoiceDate) {
		List<GetInvoiceDetailsBean> listIssueToOtherSiteList = null;

		try {

			listIssueToOtherSiteList = inwardGetInvoiceDetailsClass.getIssueLists(indentEntryId,  siteId,vendorName,invoiceDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listIssueToOtherSiteList;
	}
	@Override
	public List<GetInvoiceDetailsBean> getInvoiceDetails(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {
	List<GetInvoiceDetailsBean> listIssueToOtherSiteList = null;

	try {

		listIssueToOtherSiteList = inwardGetInvoiceDetailsClass.getInvoiceDetails(invoiceNumber,  siteId,vendorId,invoiceDate,indentType);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return listIssueToOtherSiteList;
}

	@Override
	public List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists(String indentEntryId, String siteId,String vendorName) {
		List<GetInvoiceDetailsBean> listGetInvoiceDetailsList = null;

		try {

			listGetInvoiceDetailsList = inwardGetInvoiceDetailsClass.getGetInvoiceDetailsLists(indentEntryId,  siteId,vendorName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listGetInvoiceDetailsList;
	}
	@Override
	public List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists2(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {
		List<GetInvoiceDetailsBean> listGetInvoiceDetailsList = null;

		try {

			listGetInvoiceDetailsList = inwardGetInvoiceDetailsClass.getGetInvoiceDetailsLists2(invoiceNumber,  siteId,vendorId,invoiceDate,indentType);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listGetInvoiceDetailsList;
	}

	@Override
	public List<GetInvoiceDetailsBean> getTransportChargesList(String invoiceNumber, String siteId,String indentEntryId) {
		List<GetInvoiceDetailsBean> transportChargesList = null;

		try {


			transportChargesList = inwardGetInvoiceDetailsClass.getTransportChargesList(invoiceNumber,  siteId,indentEntryId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportChargesList;
	}
	@Override
	public List<GetInvoiceDetailsBean> getTransportChargesList2(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {
		List<GetInvoiceDetailsBean> transportChargesList = null;

		try {


			transportChargesList = inwardGetInvoiceDetailsClass.getTransportChargesList2(invoiceNumber,  siteId,vendorId,invoiceDate,indentType);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportChargesList;
	}
	//Pavan Code Added
	public String updateInvoice(Model model, HttpServletRequest request, HttpSession session) {


		StringBuffer tblOneData=new StringBuffer();
		StringBuffer tblTwoData=new StringBuffer();
		StringBuffer tblCharges=new StringBuffer();
		String userId = "";
		String site_id = "";
		String state="";
		String CGST="";
		String SGST="";
		double CGSTAMT=0.0;
		double SGSTAMT=0.0;
		double amt=0.0;
		double percent=0.0;
		String IGST="";
		double IGSTAMT=0.0;
		String note="";
		int count= 0;
		String invoiceNumber="";
		String invoiceDate="";
		int indentEntrySeqNum = 0;
		String vendorName="";
		String poNo="";
		String vendorAddress="";
		String gstinNumber="";
		String poDate="";
		String transporterName="";
		String vehileNo="";
		String eWayBillNo="";
		String ttlAmntForIncentEntryTbl="";
		double doubleSumOfOtherCharges = 0.0;
		String receviedDate="";
		String dcNo="";
		String vendorId="";
		//Date grnDate = new Date();
		Map<String, String> viewGrnPageDataMap = null;
		GetInvoiceDetailsBean objGetInvoiceDetailsBean = null;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InwI_updInv, ");
		String result = "";
		String indentEntryId = request.getParameter("indentEntryId");
		String strCGSTTotal="";
		String strSGSTTotal="";
		String strIGSTTotal="";
		String siteName="";
		int portNumber=request.getLocalPort();
		boolean paymentStrStatus=false;
		String indentEntry_Id="";
		String strIndentEntryId=""; // for cheking purpose;
		boolean AmountIncreaseOrNot=false;
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			//System.out.println( LocalDateTime.now() );
		//	System.out.println(dateFormat.format(cal)); //2016/11/16 12:08:43
			//grnDate = dtf.foformat(now);  


			viewGrnPageDataMap = new HashMap<String, String>();

			String recordsCount = request.getParameter("numbeOfRowsToBeProcessed");
			logger.info("Records To Be Processed = "+recordsCount);
			String numOfRec[] = null;
			if((recordsCount != null) && (!recordsCount.equals(""))) {
				numOfRec = recordsCount.split("\\|");
			}
			if(numOfRec != null && numOfRec.length > 0) {	

				invoiceNumber = request.getParameter("invoiceNumber");
				WriteTrHistory.write("Site:"+session.getAttribute("SiteName")+" , User:"+session.getAttribute("UserName")+" , Date:"+new java.util.Date()+" , InvoiceNumber:"+invoiceNumber);

				siteName=String.valueOf(session.getAttribute("SiteName"));
				invoiceDate = request.getParameter("invoiceDate");
				vendorName = request.getParameter("vendorName");
				poNo = request.getParameter("poNo");

				vendorAddress = request.getParameter("vendorAdress");
				gstinNumber = request.getParameter("gsinNo");
				note = request.getParameter("Note");
				state = request.getParameter("state");
				vendorId=request.getParameter("vendorId");

				poDate = request.getParameter("poDate");
				eWayBillNo = request.getParameter("eWayBillNo");
				vehileNo = request.getParameter("vehileNo");
				transporterName = request.getParameter("transporterName");
				receviedDate=request.getParameter("receivedDate");
				indentEntry_Id=request.getParameter("indentEntry_Id");
				


				String otherOrTransportCharges = "otherOrTransportCharges";				
				String taxOnOtherOrTransportCharges = "taxOnOtherOrTransportCharges";				
				String otherOrTransportChargesAfterTax = "otherOrTransportChargesAfterTax";				
				String totalAmount = "totalAmount";
				String indentEntryDetailsId = "indentEntryDetailsId";	
				//String invoiceNumber = request.getParameter("invoiceNumber");
				//String invoiceDate = request.getParameter("invoiceDate");
				String quantity = "quantity";	
				objGetInvoiceDetailsBean = new GetInvoiceDetailsBean();
				//String ttlAmntForIncentEntryTbl = request.getParameter("ttlAmntForIncentEntryId") ;
				//pavan settings objects
				
				
				ttlAmntForIncentEntryTbl = request.getParameter("invoiceTotalAmount")== null ? "0" : request.getParameter("invoiceTotalAmount");
				double oldInvoiceAmount=Double.valueOf(request.getParameter("totalAmount")== null ? "0" : request.getParameter("totalAmount"));
				objGetInvoiceDetailsBean.setInvoiceNumber(invoiceNumber);
				objGetInvoiceDetailsBean.setInvoiceDate(invoiceDate);
				objGetInvoiceDetailsBean.setTotalAmount(ttlAmntForIncentEntryTbl);
				objGetInvoiceDetailsBean.setNote(note);
				userId = String.valueOf(session.getAttribute("UserId"));
				//logger.info("User Id = "+userId);
				site_id = String.valueOf(session.getAttribute("SiteId"));
				//logger.info("Site Id = "+site_id);
				if (StringUtils.isNotBlank(site_id)){
				} else {
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return result = "SessionFailed";
				}
			/*	============================== this is for if any payment in approval or done then chcek and execute ==================================*/
				
				if(oldInvoiceAmount!=Double.valueOf(ttlAmntForIncentEntryTbl)){
					 paymentStrStatus=inwardGetInvoiceDetailsClass.checkPaymentDoneOrNotOnInvoice(invoiceNumber,site_id,vendorId,receviedDate,siteName,portNumber,indentEntry_Id);
					 if(!paymentStrStatus){
						 AmountIncreaseOrNot=inwardGetInvoiceDetailsClass.updateInvoiceAccPaymenttbl(request,invoiceNumber,site_id,vendorId,ttlAmntForIncentEntryTbl,indentEntry_Id);
				}
				}
				
				if(paymentStrStatus){
					model.addAttribute("message1","Unable to update invoice,as payment already initiated.");
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return "response";
				}
				if(!AmountIncreaseOrNot && oldInvoiceAmount!=Double.valueOf(ttlAmntForIncentEntryTbl)){
					model.addAttribute("message1","Unable to decrease invoice amount as it was already paid.");
					transactionManager.rollback(status);
					WriteTrHistory.write("Tr_Completed");
					return "response";
				}
			/*====================================== this is any payment done or not check end==================================================	*/
				
				//pavan added object
				objGetInvoiceDetailsBean.setUserId(userId);
				objGetInvoiceDetailsBean.setSiteId(site_id);
				inwardGetInvoiceDetailsClass.updateInvoiceIndentEntry(objGetInvoiceDetailsBean,vendorId);
				for(String num : numOfRec) {
					objGetInvoiceDetailsBean = new GetInvoiceDetailsBean();
					num = num.trim();

					String product = request.getParameter("product"+num);					
					String subProduct = request.getParameter("subProduct"+num);
					String expiryDate = request.getParameter("expireDate"+num);
					String childProduct = request.getParameter("childProduct"+num);

					String prc = request.getParameter("price"+num);
					String basicAmnt = request.getParameter("BasicAmountId"+num);

					String unitsOfMeasurement = request.getParameter("unitsOfMeasurement"+num);
					String tax = request.getParameter("tax"+num);
					String hsnCd = request.getParameter("hsnCode"+num);
					String taxAmnt = request.getParameter("taxAmount"+num);
					String amntAfterTax = request.getParameter("amountAfterTax"+num);
					double oldQuantity=Double.parseDouble(request.getParameter("oldQuantity"+num));

					
					String otherOrTranportChrgs = request.getParameter(otherOrTransportCharges+num);
					String taxOnOtherOrTranportChrgs = request.getParameter(taxOnOtherOrTransportCharges+num);
					String otherOrTransportChrgsAfterTax = request.getParameter(otherOrTransportChargesAfterTax+num);
					String totalAmnt = request.getParameter(totalAmount+num);
					String indentEntryDetailsIdchrgs = request.getParameter(indentEntryDetailsId+num);
					String strQunatity = request.getParameter(quantity+num);
					double totalAmntForGRN =  Double.valueOf(basicAmnt) + Double.valueOf(amntAfterTax);  
					
					
					objGetInvoiceDetailsBean.setQuantity(strQunatity);
					objGetInvoiceDetailsBean.setOtherOrTransportCharges(otherOrTranportChrgs);
					objGetInvoiceDetailsBean.setTaxOnOtherOrTransportCharges(taxOnOtherOrTranportChrgs);
					objGetInvoiceDetailsBean.setOtherOrTransportChargesAfterTax(otherOrTransportChrgsAfterTax);
					objGetInvoiceDetailsBean.setInvoiceNumber(invoiceNumber);
					objGetInvoiceDetailsBean.setTotalAmount(totalAmnt);		
					objGetInvoiceDetailsBean.setIndentEntryDetailsId(indentEntryDetailsIdchrgs);
					objGetInvoiceDetailsBean.setSiteId(site_id);
					objGetInvoiceDetailsBean.setHsnCode(hsnCd);
					inwardGetInvoiceDetailsClass.updateInvoiceIndentEntryDetails(objGetInvoiceDetailsBean);
					//double tempQuan=(Double.parseDouble(strQunatity))-oldQuantity;
					
						int updateIndentAvalibilityResult=inwardGetInvoiceDetailsClass.updateIndentIndentAvailability(objGetInvoiceDetailsBean,(oldQuantity));
						//int updatePOdata=inwardGetInvoiceDetailsClass.updateIndentIndentAvailability(objGetInvoiceDetailsBean);
						if(poNo!=null && !poNo.equals("")){
				
							inwardGetInvoiceDetailsClass.updatePoentryDetailsQuantity(objGetInvoiceDetailsBean,poNo,String.valueOf(oldQuantity));
						}
				

					if (state.equals("1") || state.equals("Local")) {
						/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/
						if (tax.equals("0")) {
							CGST = "0";
							SGST = "0";
						} else {
							percent = Double.parseDouble(tax)/2;
							amt = Double.parseDouble(taxAmnt)/2;
							CGSTAMT = amt;
							
							SGSTAMT = amt;
							strCGSTTotal=String.format("%.2f",CGSTAMT);
							strSGSTTotal=String.format("%.2f",SGSTAMT);
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
						}
					} else {
						percent = Double.parseDouble(tax);
						amt = Double.parseDouble(taxAmnt);
						IGST = String.valueOf(percent);
						IGSTAMT = amt;
						strIGSTTotal=String.format("%.2f",IGSTAMT);
					}
					count = ++count;
					
					
					if (state.equals("1") || state.equals("Local")) {
						
						tblTwoData.append(count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+strQunatity+"@@"+strQunatity+"@@"+prc+"@@"+basicAmnt+"@@"+CGST+"@@"+strCGSTTotal+"@@"+SGST+"@@"+strSGSTTotal+"@@"+""+"@@"+""+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&");
					} else {
						tblTwoData.append(count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+strQunatity+"@@"+strQunatity+"@@"+prc+"@@"+basicAmnt+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+strIGSTTotal+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&");
						//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
					}





				}
				if(poNo!=null && !poNo.equals("")){
				inwardGetInvoiceDetailsClass.inActivePOentrytbl(indentEntryId,poNo);
				}
				result = "Success";
			}
			else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				//viewToBeSelected = "indentReceiveResponse";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return result = "Failed";
			}

			String tansRecordsCount = request.getParameter("chargesLength");
			logger.info("Records To Be Processed = "+tansRecordsCount);
			String numOfTransRec[] = null;
			if((tansRecordsCount != null) && (!tansRecordsCount.equals(""))) {
				numOfTransRec = tansRecordsCount.split(",");
			}
			//int records_Count =Integer.parseInt(numOfTransRec);
			if(tansRecordsCount != null && tansRecordsCount.length() > 0) {	
				String strConveyance = "Conveyance";				
				String strConveyanceAmount = "ConveyanceAmount";				
				String strGSTTax = "GSTTax";				
				String strGSTAmount = "GSTAmount";
				String strAmountAfterTax = "AmountAfterTax";				
				String strTransportInvoice = "TransportInvoice";
				String strConveyanceTypeCharg = "";				
				String strConveyanceAmountChrg = "";				
				String strGSTTaxChrg = "";				
				String strGSTAmountChrg = "";
				String strAmountAfterTaxChrg = "";				
				String strTransportInvoiceChrg = "";
				String transactionActionValue="";
				String strTransactionDtlsSeqId="";
				int retValue=0;
				//String indentEntryId = request.getParameter("indentEntryId");
				int records_Count =(numOfTransRec.length);
				for(int i=0;i<records_Count;i++){
					int num=Integer.parseInt(numOfTransRec[i]);
				
				//for(String num : numOfTransRec) {
					objGetInvoiceDetailsBean = new GetInvoiceDetailsBean();
					//num = num.trim();
					//IndentReceiveDto objIndentReceiveDto = new IndentReceiveDto();
					transactionActionValue=request.getParameter("transactionActionValue"+num);
					strTransactionDtlsSeqId=request.getParameter("iD"+num);
					strConveyanceTypeCharg = request.getParameter(strConveyance+num);
					strConveyanceAmountChrg = request.getParameter(strConveyanceAmount+num);
					strGSTTaxChrg = request.getParameter(strGSTTax+num);
					strGSTAmountChrg = request.getParameter(strGSTAmount+num);
					strAmountAfterTaxChrg = request.getParameter(strAmountAfterTax+num);
					strTransportInvoiceChrg = request.getParameter(strTransportInvoice+num);
					//pavan settings objects

					if(strConveyanceTypeCharg.contains("$")){

						String [] convycharg =strConveyanceTypeCharg.split("\\$");
						strConveyanceTypeCharg = convycharg[0];

					}
					if(strGSTTaxChrg.contains("%") && strGSTTaxChrg.contains("$")){


						String [] strGSTTaxChrgArr =strGSTTaxChrg.split("\\$");
						strGSTTaxChrg = strGSTTaxChrgArr[0];


						//strGSTTaxChrg = strGSTTaxChrg.replace("%", "");
					}
					
					objGetInvoiceDetailsBean.setConveyance1(strConveyanceTypeCharg);
					objGetInvoiceDetailsBean.setConveyanceAmount1(strConveyanceAmountChrg);
					objGetInvoiceDetailsBean.setGSTTax1(strGSTTaxChrg);
					objGetInvoiceDetailsBean.setGSTAmount1(strGSTAmountChrg);
					objGetInvoiceDetailsBean.setAmountAfterTax1(strAmountAfterTaxChrg);
					objGetInvoiceDetailsBean.setTransportInvoice1(strTransportInvoiceChrg);	
					objGetInvoiceDetailsBean.setInvoiceNumber(invoiceNumber);
					if(transactionActionValue==null  && strTransactionDtlsSeqId==null){
						inwardGetInvoiceDetailsClass.updateInvoiceOtherCharges(objGetInvoiceDetailsBean,site_id,indentEntryId);
						continue;
					}if(transactionActionValue.equals("E")){
						retValue=inwardGetInvoiceDetailsClass.updateInvoiceOtherChargesEdited(objGetInvoiceDetailsBean,strTransactionDtlsSeqId);
						continue;
					}
					if(transactionActionValue.equals("S")){continue;}
					if(transactionActionValue.equals("R")){
						retValue=inwardGetInvoiceDetailsClass.updateInvoiceOtherChargesDelete(strTransactionDtlsSeqId);
						continue;
					}

				}




				String grn_no = inwardGetInvoiceDetailsClass.getGrnNumberForGRN(indentEntry_Id,site_id,vendorId);
				tblCharges.append(inwardGetInvoiceDetailsClass.getTransportChargesListForGRN( invoiceNumber,  site_id, state,"","",indentEntryId));
				double totalAmt=Double.valueOf(ttlAmntForIncentEntryTbl);
				totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
				int val = (int) Math.ceil(totalAmt);
				double roundoff=Math.ceil(totalAmt)-totalAmt;
				double grandtotal=Math.ceil(totalAmt);
				String strtotal=String.format("%.2f",totalAmt);//String.valueOf(totalAmt);
				String strroundoff=String.format("%.2f",roundoff);
				String strgrandtotal=String.valueOf((int)grandtotal);
				String strEmpName=String.valueOf(session.getAttribute("UserName"));

				tblOneData.append(gstinNumber+"@@@@"+invoiceNumber+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+""+strtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strgrandtotal+"@@"+strEmpName+"@@"+new Timestamp(System.currentTimeMillis()));

				viewGrnPageDataMap.put("tblOneData", tblOneData.toString());

				//start 03-sept
				String NamesOfCharges = tblCharges.toString().substring(0, tblCharges.toString().length() - 2);;
				viewGrnPageDataMap.put("NamesOfCharges", NamesOfCharges);
				//end 03-sept


				//table three data

				String fnlStr = tblTwoData.toString().substring(0, tblTwoData.toString().length() - 2);
				viewGrnPageDataMap.put("tblTwoData", fnlStr);
				request.setAttribute("viewGrnPageData", viewGrnPageDataMap);





				transactionManager.commit(status);	
				WriteTrHistory.write("Tr_Completed");
			}
			else {
				request.setAttribute("exceptionMsg", "Sorry!, No Records Were Found To Be Processed.");
				//viewToBeSelected = "indentReceiveResponse";
				transactionManager.rollback(status);
				WriteTrHistory.write("Tr_Completed");
				return result = "Failed";
			}
		}
		catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			request.setAttribute("exceptionMsg", "Exception occured while processing the Indent Receive.");
			//viewToBeSelected = "indentReceiveResponse";
			result = "Failed";
		/*	AuditLogDetailsBean auditBean = new AuditLogDetailsBean();
			auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
			auditBean.setLoginId(userId);
			auditBean.setOperationName("update Recive");
			auditBean.setStatus("error");
			auditBean.setSiteId(site_id);
			new SaveAuditLogDetails().saveAuditLogDetails(auditBean);*/
			e.printStackTrace();
		}
		//logger.info("Final View To Be Selected = "+viewToBeSelected);
		return result;
	}

	@Override
	public String getVendorNameAndIndentId(String invoiceNumber, String siteId, String vendorName) {
		
		String vendorNameAndIndentId = "";
		try {


			vendorNameAndIndentId = inwardGetInvoiceDetailsClass.getVendorNameAndIndentId(invoiceNumber,  siteId, vendorName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendorNameAndIndentId;
	}
	
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId){
		List<ViewIndentIssueDetailsBean> list = null;
		try{
			list = inwardGetInvoiceDetailsClass.getGrnViewDetails( fromDate,  toDate,  siteId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public String  getVendorNameAndIndentEntryId(String invoiceNumber,String vendorName1,String invoiceDate) {
		
		
		return inwardGetInvoiceDetailsClass.getVendorNameAndIndentEntryId(invoiceNumber,vendorName1,invoiceDate);
	}
	
	@Override
	public List<GetInvoiceDetailsBean> getTaxInvoiceSubmissionDetails(String indentEntryId, String invoiceNumber, String siteId,
			String vendorId) {                                                                                                                                                            
		List<GetInvoiceDetailsBean> list=inwardGetInvoiceDetailsClass.getTaxInvoiceSubmissionDetails(indentEntryId,invoiceNumber,siteId);
		
		return list;
	}
	public static void main(String [] args){

		String strConveyanceTypeCharg = "1$other chrg";

		if(strConveyanceTypeCharg.contains("$")){

			String [] convycharg =strConveyanceTypeCharg.split("\\$");
			strConveyanceTypeCharg = convycharg[0];

		}

	//	logger.info(strConveyanceTypeCharg);

	}


	@Override
	public List<String> loadAllVendorNames(String vendorName) {
	
		return 	inwardGetInvoiceDetailsClass.loadAllVendorNames(vendorName);
	}
}
