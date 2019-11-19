package com.sumadhura.transdao;

import java.beans.PropertyDescriptor;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.ContractorTaxReportBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.PurchaseTaxReportBean;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.PaymentDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

@Repository
public class PaymentProcessDaoImpl extends UIProperties implements PaymentProcessDao{


	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;							//Rafi
	}
	
	
	@Override
	public List<PaymentBean> getInvoiceDetails(String siteId,Map<String,String> reqParamsMap,String selectAll, String ispaymentReqDateChecked, boolean isItFirstRequest) {

		String fromDate = reqParamsMap.get("fromDate");//"paymentReqDatefrom","paymentReqDateto"
		String toDate = reqParamsMap.get("toDate");
		String paymentReqDatefrom = reqParamsMap.get("paymentReqDatefrom");
		String paymentReqDateto = reqParamsMap.get("paymentReqDateto");
		String invoiceNumber = reqParamsMap.get("InvoiceNumber");
		String selectedVendorName = reqParamsMap.get("vendorName");
		String dropdown_SiteId = "";
		String SiteIdAndName = reqParamsMap.get("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)&&!SiteIdAndName.equals("@@")){
			dropdown_SiteId = SiteIdAndName.split("@@")[0];
		}
		double TotalInvoiceAmount = 0.0;
		double TotalTillReqAmount = 0.0;
		double TotalPaidAmount = 0.0;
		String query = "";
		String orderBy = "";
		String frontQuery = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbInvoiceList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 
		String strInvoice="";//get invoice purpose it will created
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if(siteId.equals("998")||siteId.equals("997")){

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,IE.SYS_PAYMENT_REQ_DATE,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE,IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
						+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
						+ ",(select sum(INNER_AP1.PAYMENT_REQ_UPTO) from ACC_PAYMENT INNER_AP1 where INNER_AP1.PO_NUMBER = IE.PO_ID) as PAYMENT_REQ_ON_PO "
						+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
						+ "FROM  VENDOR_DETAILS VD ,SITE S, INDENT_ENTRY IE  "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID AND IE.INDENT_TYPE='IN'   ";
						if(!selectAll.equals("true")){
							if(StringUtils.isNotBlank(dropdown_SiteId)){
								query = query+" and IE.SITE_ID='"+dropdown_SiteId+"'  ";
							}
						}
						query = query+ " and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			}else{

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,IE.SYS_PAYMENT_REQ_DATE,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE, IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
						+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
						+ ",(select sum(INNER_AP1.PAYMENT_REQ_UPTO) from ACC_PAYMENT INNER_AP1 where INNER_AP1.PO_NUMBER = IE.PO_ID) as PAYMENT_REQ_ON_PO "
						+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
						+ "FROM  VENDOR_DETAILS VD , SITE S ,INDENT_ENTRY IE "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID  AND IE.INDENT_TYPE='IN' AND  IE.SITE_ID='"+siteId+"'  "
						+ "and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			}
			
			/** when Vendor Id is Site Id don't show in Payments **/
			List<String> siteIdList = template.queryForList("select SITE_ID from SITE", new Object[]{},String.class);
			if(siteIdList.size()>0){
				String groupOfSites = "";
				for(String site:siteIdList){
					groupOfSites +="'"+site+"',";
				}
				groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
				query = query + " and IE.VENDOR_ID not in ("+groupOfSites+")";
			}
			//when user click on payment for invoice
			if(isItFirstRequest){
				query = query + " and IE.SYS_PAYMENT_REQ_DATE is not null ";
				orderBy = " order by IE.SYS_PAYMENT_REQ_DATE asc ";
			}
			else if(!selectAll.equals("true")){
				if(!ispaymentReqDateChecked.equals("true")){
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						query = query + "  AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
						//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					} else if (StringUtils.isNotBlank(fromDate)) {
						query = query + "  AND TRUNC(IE.INVOICE_DATE)  =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(toDate)) {
						query = query + "  AND TRUNC(IE.INVOICE_DATE)  <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
					}
					orderBy = " order by IE.INVOICE_DATE asc ";
				}
				else{
					if (StringUtils.isNotBlank(paymentReqDatefrom) && StringUtils.isNotBlank(paymentReqDateto)) {
						query = query + "  AND TRUNC(IE.SYS_PAYMENT_REQ_DATE)  BETWEEN TO_DATE('"+paymentReqDatefrom+"','dd-MM-yy') AND TO_DATE('"+paymentReqDateto+"','dd-MM-yy')";
						//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					} else if (StringUtils.isNotBlank(paymentReqDatefrom)) {
						query = query + "  AND TRUNC(IE.SYS_PAYMENT_REQ_DATE)  =TO_DATE('"+paymentReqDatefrom+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(paymentReqDateto)) {
						query = query + "  AND TRUNC(IE.SYS_PAYMENT_REQ_DATE)  <=TO_DATE('"+paymentReqDateto+"', 'dd-MM-yy')";
					}
					orderBy = " order by IE.SYS_PAYMENT_REQ_DATE asc ";
				}

				if(StringUtils.isNotBlank(selectedVendorName)){
					query = query+" and VD.VENDOR_NAME='"+selectedVendorName+"'";
				}
				if(StringUtils.isNotBlank(invoiceNumber)){
					query = query+" and IE.INVOICE_ID='"+invoiceNumber+"'";
				}
			}
			//for not taking 'localPurchase'
			//query = query+" AND (SELECT min(TOP)  FROM (select SPL.TYPE_OF_PURCHASE as TOP from SUMADHURA_PRICE_LIST SPL where SPL.INDENT_ENTRY_DETAILS_ID in (select IED.INDENT_ENTRY_DETAILS_ID from INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID ) ) )!='localPurchase' ";
			
			//for not taking 'If Payment is initiated full'
			query = query+ " and ( AP.PAYMENT_REQ_UPTO IS NULL or AP.PAYMENT_REQ_UPTO < AP.INVOICE_AMOUNT ) ";
			
			query = query+orderBy;

			dbInvoiceList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> invoices : dbInvoiceList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String invoiceId = invoices.get("INVOICE_ID")==null ? "" : invoices.get("INVOICE_ID").toString();
				strInvoice=invoices.get("INVOICE_ID")==null ? "" : invoices.get("INVOICE_ID").toString();
				if(invoiceId.contains("&")){ strInvoice=invoiceId.replace('&','@');}
				//objPaymentBean.setStrNumber(strInvoice);
				objPaymentBean.setStrInvoiceNo(invoiceId);
				objPaymentBean.setStrInvoiceNoInAP(invoices.get("INVOICE_NUMBER")==null ? "" : invoices.get("INVOICE_NUMBER").toString());
				objPaymentBean.setStrCreditNoteNumber(invoices.get("CREDIT_NOTE_NUMBER")==null ? "" : invoices.get("CREDIT_NOTE_NUMBER").toString());
				//String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String vendorName = invoices.get("VENDOR_NAME")==null ? "" : invoices.get("VENDOR_NAME").toString();
				objPaymentBean.setStrVendorName(vendorName);
				objPaymentBean.setStrVendorId(invoices.get("VENDOR_ID")==null ? "" : invoices.get("VENDOR_ID").toString());
				//String receviedDate=prods.get("RECEVED_DATE")==null ? "" : prods.get("RECEVED_DATE").toString();
				//String poDate = prods.get("PODATE")==null ? "" : prods.get("PODATE").toString();
				//String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString();
				//String strSiteId = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
				objPaymentBean.setStrRemarks(invoices.get("NOTE")==null ? "" : invoices.get("NOTE").toString());


				
				objPaymentBean.setIntPaymentId(Integer.valueOf(invoices.get("PAYMENT_ID")==null ? "0" : invoices.get("PAYMENT_ID").toString()));
				    
				String invoiceDate = invoices.get("INVOICE_DATE")==null ? "" : invoices.get("INVOICE_DATE").toString();
				String receivedDate = invoices.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : invoices.get("RECEIVED_OR_ISSUED_DATE").toString();
				String paymentReqDate = invoices.get("SYS_PAYMENT_REQ_DATE")==null ? "" : invoices.get("SYS_PAYMENT_REQ_DATE").toString();
				
				objPaymentBean.setStrPODate(invoices.get("PODATE")==null ? "" : invoices.get("PODATE").toString());
				String site_id = invoices.get("SITE_ID")==null ? "" : invoices.get("SITE_ID").toString();
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(invoices.get("SITE_NAME")==null ? "" : invoices.get("SITE_NAME").toString());
				objPaymentBean.setStrPONo(invoices.get("PO_ID")==null ? "" : invoices.get("PO_ID").toString());
				double invoiceAmount = Math.round(Double.valueOf(invoices.get("TOTAL_AMOUNT")==null ? "0" : invoices.get("TOTAL_AMOUNT").toString()));
			    objPaymentBean.setDoubleInvoiceAmount(invoiceAmount);
			    objPaymentBean.setStrInvoiceAmount(String.format("%.2f",invoiceAmount));
			    TotalInvoiceAmount += invoiceAmount;
			    objPaymentBean.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
			    objPaymentBean.setDoublePOTotalAmount(Math.round(Double.valueOf(invoices.get("PO_TOTAL_AMOUNT")==null ? "0" : invoices.get("PO_TOTAL_AMOUNT").toString())));
			    double paymentDoneUpto = Double.valueOf(invoices.get("PAYMENT_DONE_UPTO")==null ? "0" : invoices.get("PAYMENT_DONE_UPTO").toString());
			    double paymentReqUpto = Double.valueOf(invoices.get("PAYMENT_REQ_UPTO")==null ? "0" : invoices.get("PAYMENT_REQ_UPTO").toString());
			    objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto);
			    objPaymentBean.setDoublePaymentRequestedUpto(paymentReqUpto);
			    objPaymentBean.setPaymentDoneUpto_WithCommas(format.format(paymentDoneUpto));
			    objPaymentBean.setPaymentRequestedUpto_WithCommas(format.format(paymentReqUpto));
			    TotalPaidAmount +=paymentDoneUpto;
			    TotalTillReqAmount += paymentReqUpto;
			    String strIndentEntryId = invoices.get("INDENT_ENTRY_ID")==null ? "0" : invoices.get("INDENT_ENTRY_ID").toString();
			    objPaymentBean.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			    double creditAmount = Double.valueOf(invoices.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : invoices.get("CREDIT_TOTAL_AMOUNT").toString());
			    objPaymentBean.setDoubleCreditTotalAmount(creditAmount);
			    objPaymentBean.setCreditTotalAmount_WithCommas(format.format(creditAmount));
			    objPaymentBean.setVendorInvoiceTotalAmount_WithCommas(format.format(invoiceAmount+creditAmount));
			    double securityDeposit = Double.valueOf(invoices.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : invoices.get("SECURITY_DEPOSIT_AMOUNT").toString());
			    double poAdvancePayment = Double.valueOf(invoices.get("REMAINING_AMOUNT")==null ? "0" : invoices.get("REMAINING_AMOUNT").toString());
			    String payBalanceInPo = invoices.get("PAY_BALANCE_IN_PO")==null ? "NO_ADVANCE" : invoices.get("PAY_BALANCE_IN_PO").toString();
			    objPaymentBean.setDoubleSecurityDeposit(securityDeposit);
			    objPaymentBean.setDoublePOAdvancePayment(poAdvancePayment);
			    objPaymentBean.setPayBalanceInPo(payBalanceInPo);
			    String paymentDoneOnMultipleInvoices = invoices.get("PAYMENT_DONE_ON_MULTI_INV")==null ? "0" : invoices.get("PAYMENT_DONE_ON_MULTI_INV").toString();
			    objPaymentBean.setPaymentDoneOnMultipleInvoices(paymentDoneOnMultipleInvoices);
			    
			    String paymentRequestedOnPO = invoices.get("PAYMENT_REQ_ON_PO")==null ? "0" : invoices.get("PAYMENT_REQ_ON_PO").toString();
			    objPaymentBean.setPaymentRequestedOnPO(paymentRequestedOnPO);
			    String adjustedAmountFromPo = invoices.get("ADJUSTED_AMOUNT_FROM_PO")==null ? "0" : invoices.get("ADJUSTED_AMOUNT_FROM_PO").toString();
			    objPaymentBean.setAdjustedAmountFromPo(adjustedAmountFromPo);
			    objPaymentBean.setSecurityDeposit_WithCommas(format.format(securityDeposit));
			    objPaymentBean.setPoAdvancePayment_WithCommas(format.format(poAdvancePayment));
			    
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
			    SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yy");
				
			    try{
			    	
			    	if(!invoiceDate.equals("")){
			    		Date invoice_date = dt.parse(invoiceDate);
			    		invoiceDate = dt1.format(invoice_date);
			    		}
			    	if(!receivedDate.equals("")){
			    		Date received_date = dt.parse(receivedDate);
			    		receivedDate = dt1.format(received_date);
			    		}
			    	if(!paymentReqDate.equals("")){
			    		Date paymentReq_Date = dt.parse(paymentReqDate);
			    		paymentReqDate = dt2.format(paymentReq_Date);
			    		}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrInvoiceDate(invoiceDate);
			    objPaymentBean.setStrInvoiceReceivedDate(receivedDate);
			    objPaymentBean.setStrPaymentReqDate(paymentReqDate);
			    /*** display image ***/
			    for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
				String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
			    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					objPaymentBean.setHasImage(true);break;
					
				}
			    }
			    /**** END ***/
			    list.add(objPaymentBean);
			}
			reqParamsMap.put("TotalInvoiceAmount",format.format(TotalInvoiceAmount));
			reqParamsMap.put("TotalTillReqAmount",format.format(TotalTillReqAmount));
			reqParamsMap.put("TotalPaidAmount",format.format(TotalPaidAmount));





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}
	@Override
	public List<PaymentBean> getPODetails(String siteId,Map<String,String> reqParamsMap,String selectAll) {

		String fromDate = reqParamsMap.get("fromDate");
		String toDate = reqParamsMap.get("toDate");
		String poNumber = reqParamsMap.get("PONumber");
		String selectedVendorName = reqParamsMap.get("vendorName");
		String dropdown_SiteId = "";
		String SiteIdAndName = reqParamsMap.get("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)&&!SiteIdAndName.equals("@@")){
			dropdown_SiteId = SiteIdAndName.split("@@")[0];
		}
		double TotalPOAmount = 0.0;
		double TotalTillReqAmount = 0.0;
		double TotalPaidAmount = 0.0;
		
		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbPOsList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			
			if(siteId.equals("998")){

				query = "SELECT DISTINCT AP.PAYMENT_ID,SPE.PO_NUMBER,SPE.PO_ENTRY_ID,S.SITE_NAME,S.SITE_ID,SPE.PO_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AAPP.PAID_AMOUNT,AAPP.REMAINING_AMOUNT,AAPP.INTIATED_AMOUNT,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO,"
						  + "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID ) as TOTAL_AMOUNT,AVSD.SECURITY_DEPOSIT_AMOUNT  "
						  + "FROM  VENDOR_DETAILS VD , SITE S , SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN ACC_PAYMENT AP on AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID = SPE.VENDOR_ID and AP.SITE_ID = SPE.SITE_ID "
						  + " LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = SPE.VENDOR_ID AND AVSD.SITE_ID = SPE.SITE_ID AND AVSD.STATUS = 'A' "
						  + " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on SPE.PO_NUMBER = AAPP.PO_NUMBER  "
						  + " WHERE   SPE.VENDOR_ID=VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID  ";
						if(!selectAll.equals("true")){		  
							if(StringUtils.isNotBlank(dropdown_SiteId)){
								query = query+" and SPE.SITE_ID='"+dropdown_SiteId+"'  ";
							}
						}
							
						  query = query+ " and (((AP.STATUS='A' or AP.STATUS IS NULL)  and SPE.PO_STATUS = 'I') or (SPE.PO_STATUS = 'A')) ";
						  //for not taking if po amount fully paid
						  query = query+ " and (AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT is null or (AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT is not null and AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT > 0)) ";
			
			}else{
				query = "SELECT DISTINCT AP.PAYMENT_ID,SPE.PO_NUMBER,SPE.PO_ENTRY_ID,S.SITE_NAME,S.SITE_ID,SPE.PO_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AAPP.PAID_AMOUNT,AAPP.REMAINING_AMOUNT,AAPP.INTIATED_AMOUNT,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO,"
								  + "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID ) as TOTAL_AMOUNT,AVSD.SECURITY_DEPOSIT_AMOUNT  "
								  + "FROM  VENDOR_DETAILS VD , SITE S , SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN ACC_PAYMENT AP on AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID = SPE.VENDOR_ID and AP.SITE_ID = SPE.SITE_ID "
								  + " LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = SPE.VENDOR_ID AND AVSD.SITE_ID = SPE.SITE_ID AND AVSD.STATUS = 'A' "
								  + " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on SPE.PO_NUMBER = AAPP.PO_NUMBER  "
								  + " WHERE   SPE.VENDOR_ID=VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID  AND   SPE.SITE_ID='"+siteId+"'  "
								  + " and (((AP.STATUS='A' or AP.STATUS IS NULL)  and SPE.PO_STATUS = 'I') or (SPE.PO_STATUS = 'A')) ";
				
							//for not taking if po amount fully paid
				  			query = query+ " and (AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT is null or (AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT is not null and AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT > 0)) ";
			}
			
			if(!selectAll.equals("true")){
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query + " AND TRUNC(SPE.PO_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query + " AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query + " AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}

				if(StringUtils.isNotBlank(selectedVendorName)){
					query = query+" and VD.VENDOR_NAME='"+selectedVendorName+"'";
				}

				if(StringUtils.isNotBlank(poNumber)){
					query = query+" and SPE.PO_NUMBER='"+poNumber+"'";
				}
			}
			query = query+" order by SPE.PO_DATE asc ";

			dbPOsList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			List<String> addedPoNumbersList = new ArrayList<String>();
			for(Map<String, Object> po : dbPOsList) {
				String poNo = po.get("PO_NUMBER")==null ? "" : po.get("PO_NUMBER").toString();
				if(addedPoNumbersList.contains(poNo)){
					for(PaymentBean bean : list){
						if(bean.getStrPONo().equals(poNo)){
							bean.setIntPaymentId(Integer.valueOf(po.get("PAYMENT_ID")==null ? "0" : po.get("PAYMENT_ID").toString()));
							double paymentReqUpto = Double.valueOf(po.get("PAYMENT_REQ_UPTO")==null ? "0" : po.get("PAYMENT_REQ_UPTO").toString());
							double paymentDoneUpto = Double.valueOf(po.get("PAYMENT_DONE_UPTO")==null ? "0" : po.get("PAYMENT_DONE_UPTO").toString());
							double reqUptoAfterAddition = bean.getDoublePaymentRequestedUpto()+paymentReqUpto;
							bean.setDoublePaymentRequestedUpto(reqUptoAfterAddition);
							bean.setPaymentRequestedUpto_WithCommas(format.format(reqUptoAfterAddition));
						    double reqDonetoAfterAddition = bean.getDoublePaymentDoneUpto()+paymentDoneUpto;
							bean.setDoublePaymentDoneUpto(reqDonetoAfterAddition);
							bean.setPaymentDoneUpto_WithCommas(format.format(reqDonetoAfterAddition));
							TotalPaidAmount +=paymentDoneUpto;
						    TotalTillReqAmount += paymentReqUpto;
						    
						}
					}
					continue;
				}
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				objPaymentBean.setStrPoEntryId(po.get("PO_ENTRY_ID")==null ? "" : po.get("PO_ENTRY_ID").toString());
				objPaymentBean.setStrPONo(po.get("PO_NUMBER")==null ? "" : po.get("PO_NUMBER").toString());
				String poDate = po.get("PO_DATE")==null ? "" : po.get("PO_DATE").toString();
				double poAmount = Math.round(Double.valueOf(po.get("TOTAL_AMOUNT")==null ? "0" : po.get("TOTAL_AMOUNT").toString()));
				objPaymentBean.setDoublePOTotalAmount(poAmount);
				objPaymentBean.setStrPOAmount(String.format("%.2f",poAmount));
				TotalPOAmount += poAmount;
				objPaymentBean.setPoAmount_WithCommas(format.format(poAmount));
				
				
				objPaymentBean.setStrVendorName(po.get("VENDOR_NAME")==null ? "" : po.get("VENDOR_NAME").toString());
				objPaymentBean.setStrVendorId(po.get("VENDOR_ID")==null ? "" : po.get("VENDOR_ID").toString());
				
				objPaymentBean.setStrSiteId(po.get("SITE_ID")==null ? "" : po.get("SITE_ID").toString());
				objPaymentBean.setStrSiteName(po.get("SITE_NAME")==null ? "" : po.get("SITE_NAME").toString());
				
				objPaymentBean.setIntPaymentId(Integer.valueOf(po.get("PAYMENT_ID")==null ? "0" : po.get("PAYMENT_ID").toString()));
				double remainingAmount = Double.valueOf(po.get("REMAINING_AMOUNT")==null ? "0" : po.get("REMAINING_AMOUNT").toString());
				double initiatedAmount = Double.valueOf(po.get("INTIATED_AMOUNT")==null ? "0" : po.get("INTIATED_AMOUNT").toString());
				double paymentDoneUpto = Double.valueOf(po.get("PAYMENT_DONE_UPTO")==null ? "0" : po.get("PAYMENT_DONE_UPTO").toString());
				double paymentReqUpto = Double.valueOf(po.get("PAYMENT_REQ_UPTO")==null ? "0" : po.get("PAYMENT_REQ_UPTO").toString());
				objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto+remainingAmount);//-initiatedAmount);//delete initaited amt . because we are already taking paymentReqUpto
			    objPaymentBean.setDoublePaymentRequestedUpto(paymentReqUpto);
			    objPaymentBean.setPaymentDoneUpto_WithCommas(format.format(paymentDoneUpto+remainingAmount));//-initiatedAmount));//remove initiated amount bacause we are showing only paid amount
			    objPaymentBean.setPaymentRequestedUpto_WithCommas(format.format(paymentReqUpto));
			    
			    TotalPaidAmount +=paymentDoneUpto+paymentDoneUpto+remainingAmount-initiatedAmount;
			    TotalTillReqAmount += paymentReqUpto;
			    
			    objPaymentBean.setDoubleSecurityDeposit(Double.valueOf(po.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : po.get("SECURITY_DEPOSIT_AMOUNT").toString()));
			    
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    try{
			    	Date po_date = null;
			    	if(!poDate.equals("")){po_date = dt.parse(poDate);}
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					if(po_date!=null){poDate = dt1.format(po_date);}
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrPODate(poDate);
				list.add(objPaymentBean);
				addedPoNumbersList.add(poNo);
			}
			reqParamsMap.put("TotalPOAmount",format.format(TotalPOAmount));
			reqParamsMap.put("TotalTillReqAmount",format.format(TotalTillReqAmount));
			reqParamsMap.put("TotalPaidAmount",format.format(TotalPaidAmount));





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}
	
	@Override
	public int updatePaymentTable(PaymentDto objPaymentDto){
		int count = 0;
		
		String query = "update ACC_PAYMENT set  UPDATED_DATE =sysdate , " +
		"PAYMENT_REQ_UPTO = PAYMENT_REQ_UPTO+?,REMARKS = ? where PAYMENT_ID = ? ";

		count = jdbcTemplate.update(query, new Object[] {

				objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getDoubleAdjustAmountFromAdvance(),objPaymentDto.getStrRemarks(),  objPaymentDto.getIntPaymentId()


		});
		

		return count;
	}
	
	@Override
	public int updatePaymentTableOnPoAdvance(PaymentDto objPaymentDto){
		int count = 0;
		
		String query = "update ACC_PAYMENT set  UPDATED_DATE =sysdate , " +
		"PAYMENT_REQ_UPTO = PAYMENT_REQ_UPTO+?,REMARKS = ? where PAYMENT_ID = ? ";

		count = jdbcTemplate.update(query, new Object[] {

				objPaymentDto.getDoubleAmountToBeReleased(),objPaymentDto.getStrRemarks(),  objPaymentDto.getIntPaymentId()


		});
		

		return count;
	}
	
	@Override
	public boolean checkingIsRequestAmountExceedLimitOfPoAmount(double doubleAmountToBeReleased, int poPaymentId) {
		double paymentReqUpto=0.0;
		double paymentDoneUpto=0.0;
		double poAmount=0.0;
		
		String getPoPaymentIdQry = "select PAYMENT_REQ_UPTO,PAYMENT_DONE_UPTO,PO_AMOUNT from ACC_PAYMENT  where PAYMENT_ID = ? ";
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(getPoPaymentIdQry,new Object[] {poPaymentId});
			paymentReqUpto = map.get("PAYMENT_REQ_UPTO")==null? 0.0 : Double.parseDouble(map.get("PAYMENT_REQ_UPTO").toString());
			paymentDoneUpto = map.get("PAYMENT_DONE_UPTO")==null? 0.0 : Double.parseDouble(map.get("PAYMENT_DONE_UPTO").toString());
			poAmount = map.get("PO_AMOUNT")==null? 0.0 : Double.parseDouble(map.get("PO_AMOUNT").toString());
			if((paymentReqUpto+paymentDoneUpto+doubleAmountToBeReleased)>poAmount){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public int returnPaymentIdIfThereIsAnyRowWithCurrentPO_NUMBER(PaymentDto objPaymentDto){
		/* checking in ACC_PAYMENT is there any row with current PO_NUMBER with empty INVOICE_NUMBER */
		int poPaymentId = 0;
		String getPoPaymentIdQry = "select AP.PAYMENT_ID from ACC_PAYMENT AP where AP.PO_NUMBER = ? "
				+ "and AP.VENDOR_ID = ? and AP.SITE_ID = ? and AP.INVOICE_NUMBER IS NULL";
		try {
			poPaymentId = jdbcTemplate.queryForInt(getPoPaymentIdQry,new Object[] {
					objPaymentDto.getStrPONo(),objPaymentDto.getStrVendorId(),objPaymentDto.getStrSiteId()});
		} catch (EmptyResultDataAccessException e) {
			poPaymentId = 0;
		}
		return poPaymentId;
	}
	
	@Override
	public int updatePaymentTableOnPoNumber(PaymentDto objPaymentDto,int poPaymentId){

		int count = 0;


			String query = "update ACC_PAYMENT set INVOICE_NUMBER = ?, INVOICE_AMOUNT = ?,"
					+ "INVOICE_DATE = ?,INVOICE_RECEIVED_DATE = ?, UPDATED_DATE =sysdate , " +
					"PAYMENT_REQ_UPTO = PAYMENT_REQ_UPTO+?,REMARKS = ?,INDENT_ENTRY_ID=?,CREDIT_TOTAL_AMOUNT=?,CREDIT_NOTE_NUMBER=? where PAYMENT_ID = ? ";

			count = jdbcTemplate.update(query, new Object[] {objPaymentDto.getStrInvoiceNo(),
					objPaymentDto.getDoubleInvoiceAmount(),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrInvoiceDate()),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrInvoiceReceivedDate()),
					objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getDoubleAdjustAmountFromAdvance(),
					objPaymentDto.getStrRemarks(), 
					String.valueOf(objPaymentDto.getIntIndentEntryId()),
					objPaymentDto.getDoubleCreditTotalAmount(),
					objPaymentDto.getStrCreditNoteNumber(),
					poPaymentId


			});
		


		return count;

	}
	
	@Override
	public int savePaymentTable(PaymentDto objPaymentDto,int PaymentId){

		int count = 0;
		
			String query = "insert into ACC_PAYMENT(PAYMENT_ID,INVOICE_NUMBER,INVOICE_AMOUNT " +
					",SITE_ID,CREATED_DATE, PAYMENT_DONE_UPTO, DC_NUMBER, PAYMENT_REQ_UPTO, STATUS, " +
					" REMARKS, VENDOR_ID, PO_NUMBER,PO_AMOUNT,INVOICE_DATE,PO_DATE,DC_DATE,INVOICE_RECEIVED_DATE,INDENT_ENTRY_ID,CREDIT_TOTAL_AMOUNT,CREDIT_NOTE_NUMBER) " +
					"values(?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			count = jdbcTemplate.update(query, new Object[] {
					PaymentId,
					objPaymentDto.getStrInvoiceNo(),objPaymentDto.getDoubleInvoiceAmount(),
					objPaymentDto.getStrSiteId(),objPaymentDto.getDoublePaymentDoneUpto(),
					objPaymentDto.getStrDCNo(),objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getDoubleAdjustAmountFromAdvance(),
					"A",objPaymentDto.getStrRemarks(),objPaymentDto.getStrVendorId(),
					objPaymentDto.getStrPONo(),objPaymentDto.getDoublePOAmount(),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrInvoiceDate()),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrPODate()),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrDCDate()),
					DateUtil.convertToJavaDateFormat(objPaymentDto.getStrInvoiceReceivedDate()),
					String.valueOf(objPaymentDto.getIntIndentEntryId()),
					objPaymentDto.getDoubleCreditTotalAmount(),
					objPaymentDto.getStrCreditNoteNumber(),
			});
			
		



		return count;

	}
	@Override
	public int savePaymentTableOnPoAdvance(PaymentDto objPaymentDto,int PaymentId){

		int count = 0;

		String query = "insert into ACC_PAYMENT(PAYMENT_ID ,SITE_ID,CREATED_DATE, " +
				" PAYMENT_DONE_UPTO, DC_NUMBER, PAYMENT_REQ_UPTO, STATUS, " +
				" REMARKS, VENDOR_ID, PO_NUMBER,PO_AMOUNT,PO_DATE,DC_DATE,INVOICE_AMOUNT,INDENT_ENTRY_ID) " +
				"values(?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?)";

		count = jdbcTemplate.update(query, new Object[] {
				PaymentId,
				objPaymentDto.getStrSiteId(),objPaymentDto.getDoublePaymentDoneUpto(),
				objPaymentDto.getStrDCNo(),objPaymentDto.getDoubleAmountToBeReleased(),
				"A",objPaymentDto.getStrRemarks(),objPaymentDto.getStrVendorId(),
				objPaymentDto.getStrPONo(),objPaymentDto.getDoublePOAmount(),
				DateUtil.convertToJavaDateFormat(objPaymentDto.getStrPODate()),
				DateUtil.convertToJavaDateFormat(objPaymentDto.getStrDCDate()),
				"0","0"
		});



		return count;

	}
	
	@Override
	public int savePaymentProcessDtlsTable(PaymentDto objPaymentDto,int intPaymentId,int paymentEntrySeqId){

		int count = 0;
		
			String query = "insert into ACC_PAYMENT_DTLS(PAYMENT_DETAILS_ID,PAYMENT_ID,REQ_AMOUNT,REQUEST_PENDING_EMP_ID," +
					       "STATUS,PAYMENT_TYPE, "+
                           "PAYMENT_REQ_DATE,REMARKS,SITEWISE_PAYMENT_NO,CREATION_DATE,ADJUST_AMOUNT_FROM_ADVANCE)values(?,?,?,?,?,?,?,?,?,sysdate,?)";

			count = jdbcTemplate.update(query, new Object[] {
					paymentEntrySeqId,
					intPaymentId,objPaymentDto.getDoubleAmountToBeReleased(),
					objPaymentDto.getStrPendingEmpId(),"A",objPaymentDto.getPaymentType(),
					objPaymentDto.getStrPaymentReqDate(),objPaymentDto.getStrRemarks(),
					objPaymentDto.getIntSiteWisePaymentId(),
					objPaymentDto.getDoubleAdjustAmountFromAdvance()});
			return count;
	}
	@Override
	public int updatePoAdvanceTable(PaymentDto objPaymentDto){

					
			
			String query1 = "update ACC_ADVANCE_PAYMENT_PO set INTIATED_AMOUNT=INTIATED_AMOUNT+? where PO_NUMBER = ? ";

					int count1 = jdbcTemplate.update(query1, new Object[] {
							objPaymentDto.getDoubleAdjustAmountFromAdvance(),
							objPaymentDto.getStrPONo()
					});
					
					
		return count1;

	}
	@Override
	public int savePaymentProcessDtlsTableOnPoAdvance(PaymentDto objPaymentDto,int intPaymentId,int paymentEntrySeqId){

		int count = 0;
		int count1 = 0;
		
			String query = "insert into ACC_PAYMENT_DTLS(PAYMENT_DETAILS_ID,PAYMENT_ID,REQ_AMOUNT,REQUEST_PENDING_EMP_ID," +
					       "STATUS,PAYMENT_TYPE, "+
                           "PAYMENT_REQ_DATE,REMARKS,SITEWISE_PAYMENT_NO,CREATION_DATE,ADJUST_AMOUNT_FROM_ADVANCE)values(?,?,?,?,?,?,?,?,?,sysdate,?)";

			count = jdbcTemplate.update(query, new Object[] {
					paymentEntrySeqId,
					intPaymentId,objPaymentDto.getDoubleAmountToBeReleased(),
					objPaymentDto.getStrPendingEmpId(),"A",objPaymentDto.getPaymentType(),
					objPaymentDto.getStrPaymentReqDate(),objPaymentDto.getStrRemarks(),
					objPaymentDto.getIntSiteWisePaymentId(),
					objPaymentDto.getDoubleAdjustAmountFromAdvance()});
			
					
		return count;

	}

	
	@Override
	public int savePaymentApproveRejectTable(PaymentDto objPaymentDto,int intPaymentId,int intPaymentDetailsId){

		int count = 0;

		
			String query = "insert into ACC_SITE_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,PAYMENT_ID,EMP_ID,STATUS," +
					"REMARKS,PAYMENT_DETAILS_ID,SITE_ID,CREATED_DATE,OPERATION_TYPE)values" +
					"(PAYMENT_APPROVE_REJECT_SEQ_ID.nextval,?,?,?,?,?,?,sysdate,?)";

			count = jdbcTemplate.update(query, new Object[] {
					intPaymentId,objPaymentDto.getStrEmployeeId(),"A",objPaymentDto.getStrRemarks(),
					intPaymentDetailsId,objPaymentDto.getStrSiteId(),"C"});

		return count;

	}
	
	
	@Override
	public int getPaymentId()
	{
		int paymentId = 0;

		String query = "select PAYMENT_SEQ_ID.nextval from dual";

		paymentId = jdbcTemplate.queryForInt(query);


		return paymentId;

	}
	
	@Override
	public int getIntPaymentTransactionId()
	{
		int paymentId = 0;

		String query = "select ACC_PMT_TRANSACTIONS_SEQ.nextval from dual";

		paymentId = jdbcTemplate.queryForInt(query);


		return paymentId;

	}
	
	@Override
	public int getPaymentDetailsId()
	{
		int enquiryFormSeqId = 0;

		String query = "select PAYMENT_ENTRY_DTLS_SEQ_ID.nextval from dual";

		enquiryFormSeqId = jdbcTemplate.queryForInt(query);


		return enquiryFormSeqId;

	}
	
	@Override
	public List<PaymentBean> getPaymentApprovalDetails(String siteId, String user_id){

		String query = "";
		JdbcTemplate template = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean indentObj = null; 
		String strInvoiceNumber="";// this is for get invoice details for '&' given in invoice number
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

		
				if (siteId.equals("998")) {
					query = "select DISTINCT(AP.INVOICE_NUMBER),VD.VENDOR_NAME,AP.INVOICE_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,AP.SITE_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,APD.REQ_AMOUNT,"
							+" APD.PAYMENT_REQ_DATE,APD.REMARKS,APD.PAYMENT_DETAILS_ID,APD.PAYMENT_ID,VD.VENDOR_ID,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.INDENT_ENTRY_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,APD.PAYMENT_TYPE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO " 
							+" ,(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT,AAPP.PAID_AMOUNT as PO_ADVANCE ,APD.ADJUST_AMOUNT_FROM_ADVANCE "
							+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
							+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
							+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
							+ " FROM VENDOR_DETAILS VD,ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP " 
							+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE on ( AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID=SPE.VENDOR_ID) "
							+"  LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A' "
							+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
							+ " WHERE AP.PAYMENT_ID=APD.PAYMENT_ID and APD.STATUS='A'  AND AP.VENDOR_ID=VD.VENDOR_ID "  
							+" AND APD.REQUEST_PENDING_EMP_ID = '"+user_id+"'"
							+" order by VD.VENDOR_NAME asc, APD.PAYMENT_REQ_DATE asc ";
				}
				else{
					query = "select DISTINCT(AP.INVOICE_NUMBER),VD.VENDOR_NAME,AP.INVOICE_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,AP.SITE_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,APD.REQ_AMOUNT,"
							+" APD.PAYMENT_REQ_DATE,APD.REMARKS,APD.PAYMENT_DETAILS_ID,APD.PAYMENT_ID,VD.VENDOR_ID,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.INDENT_ENTRY_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,APD.PAYMENT_TYPE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO " 
							+" ,(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT,AAPP.PAID_AMOUNT as PO_ADVANCE ,APD.ADJUST_AMOUNT_FROM_ADVANCE "
							+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
							+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
							+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
							+ " FROM VENDOR_DETAILS VD,ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP " 
							+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE on ( AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID=SPE.VENDOR_ID) "
							+"  LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A' "
							+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
							+ " WHERE AP.PAYMENT_ID=APD.PAYMENT_ID and APD.STATUS='A' AND AP.SITE_ID='"+siteId+"' AND AP.VENDOR_ID=VD.VENDOR_ID "  
							+" AND APD.REQUEST_PENDING_EMP_ID = '"+user_id+"'"
							+" order by VD.VENDOR_NAME asc, APD.PAYMENT_REQ_DATE asc ";
					
				}
				//In query we are taking results as per VendorName ascending order to do grouping easily.
				//WARNING: If results are not in vendor name ascending order, below code does not work.
			dbIndentDts = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			int vendorGroupSerialNo = 0;
			String currentVendorId="-";
			List<PaymentBean> invoiceIdList = new ArrayList<PaymentBean>();
			List<String> poIdList = new ArrayList<String>();
			for(Map<String, Object> prods : dbIndentDts) {
				String vendorId = prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString();
				String vendorName = prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString();
				String invoiceId = prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString();
				strInvoiceNumber=prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString();
				if(invoiceId.contains("&")){strInvoiceNumber=strInvoiceNumber.replace('&','@');}
				
				invoiceAmount=Math.round(Double.parseDouble(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString()));
				String poId = prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString();
				poAmount=Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString());
				String reqAmount = prods.get("REQ_AMOUNT")==null ? "" : prods.get("REQ_AMOUNT").toString();
				String site_id = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
				String paymentType = prods.get("PAYMENT_TYPE")==null ? "" :   prods.get("PAYMENT_TYPE").toString();
				double poAdvance = Double.valueOf(prods.get("PO_ADVANCE")==null ? "0" : prods.get("PO_ADVANCE").toString());
				// if vendor is different, creating separate Group object storing details of whole payments related to that vendor & add it to list.
				if(!currentVendorId.equals(vendorId))
				{
					vendorGroupSerialNo++;
					//this object stores for vendorGroup details.
					indentObj = new PaymentBean();
					indentObj.setStrVendorId(vendorId);
					indentObj.setStrVendorName(vendorName);
					indentObj.setVendorHeader(true);
					indentObj.setVendorGroupSerialNo(vendorGroupSerialNo);
					list.add(indentObj);
					currentVendorId=vendorId;
					
				}
				//taking vendorGroup object related to this vendor in list.
				//add invoice amount of this vendor, to the vendorGroup object
				for(PaymentBean e:list){
					if(e.isVendorHeader()&&e.getStrVendorId().equals(vendorId)){
						//add req amount of this vendor, to the vendorGroup object
						double getReqAmountAfterAddition = Double.valueOf(e.getRequestedAmount()==null?"0":e.getRequestedAmount())+Double.valueOf(reqAmount);
						e.setRequestedAmount(String.valueOf(getReqAmountAfterAddition));
						e.setRequestedAmount_WithCommas(format.format(getReqAmountAfterAddition));
						//if it is Advance, take po amount
						if(paymentType.equals("ADVANCE")){
							//this below if block executes one time for each po number
							if(!poIdList.contains(poId)){
								//add po amount of this vendor, to the vendorGroup object
								double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+poAmount;
								e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
								//System.out.println(String.format("%.2f",getInvoiceAmountAfterAddition));
								//double value=e.getDoubleInvoiceAmount()+poAmount;
								e.setStrInvoiceAmount((String.format("%.2f",getInvoiceAmountAfterAddition)));
								e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
								poIdList.add(poId);
							}
						}
						//this below if block executes one time for each invoice number
						else if(!isInvoiceIdListContainsinvoiceId(invoiceIdList,invoiceId,vendorId,site_id)){
							//add invoice amount of this vendor, to the vendorGroup object
							double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+invoiceAmount;
							e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
							//System.out.println(String.format("%.2f",getInvoiceAmountAfterAddition));
							//String value=String.format("%.2f",(e.getDoubleInvoiceAmount()+invoiceAmount));
							e.setStrInvoiceAmount(String.format("%.2f",getInvoiceAmountAfterAddition));//jsp purpose crore rupees
							e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
							PaymentBean obj = new PaymentBean();
							obj.setStrInvoiceNo(invoiceId);
							//obj.setStrNumber(strInvoiceNumber);
							obj.setStrVendorId(vendorId);
							obj.setStrSiteId(site_id);
							invoiceIdList.add(obj);
						}
					}
				}
				
				serialNo++;
				//this object stores Individual payment details.
				indentObj = new PaymentBean();
				indentObj.setIntSerialNo(serialNo);
				indentObj.setVendorGroupSerialNo(vendorGroupSerialNo);
				indentObj.setStrInvoiceNo(prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString());
				//indentObj.setStrNumber(strInvoiceNumber);
				
				
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				indentObj.setStrVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setStrVendorId(vendorId);
				
				poAmount=Math.round(Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString()));
				indentObj.setRequestedAmount(reqAmount);
				String  paymentDate =(prods.get("PAYMENT_REQ_DATE")==null ? "" : prods.get("PAYMENT_REQ_DATE").toString());
				String receviedDate=prods.get("INVOICE_RECEIVED_DATE")==null ? "" : prods.get("INVOICE_RECEIVED_DATE").toString();
				String remarks = prods.get("REMARKS")==null ? "" : prods.get("REMARKS").toString();
				indentObj.setStrRemarks(StringEscapeUtils.escapeHtml(remarks));
				indentObj.setStrRemarksForView(StringEscapeUtils.escapeHtml(remarks.replace("@@@", ",")));
				indentObj.setIntPaymentId(Integer.parseInt(prods.get("PAYMENT_ID")==null ? "" : prods.get("PAYMENT_ID").toString()));
				indentObj.setIntPaymentDetailsId(Integer.parseInt(prods.get("PAYMENT_DETAILS_ID")==null ? "" : prods.get("PAYMENT_DETAILS_ID").toString()));
				indentObj.setStrSiteId(site_id);
				indentObj.setStrPONo(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());				
				String  poDate =(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setDoubleInvoiceAmount(invoiceAmount);
				indentObj.setStrInvoiceAmount(String.format("%.2f",invoiceAmount));
				indentObj.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
				indentObj.setDoublePOTotalAmount(poAmount);
				indentObj.setStrPOAmount(String.format("%.2f",poAmount));//jsp crore purpose
				indentObj.setPoAmount_WithCommas(format.format(poAmount));
				double creditTotalAmount = Double.valueOf(prods.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : prods.get("CREDIT_TOTAL_AMOUNT").toString());
				indentObj.setDoubleCreditTotalAmount(creditTotalAmount);
				indentObj.setCreditTotalAmount_WithCommas(format.format(creditTotalAmount));
				indentObj.setVendorInvoiceTotalAmount_WithCommas(format.format(invoiceAmount+creditTotalAmount));
				indentObj.setStrCreditNoteNumber(prods.get("CREDIT_NOTE_NUMBER")==null ? "" : prods.get("CREDIT_NOTE_NUMBER").toString());
				double securityDeposit = Double.valueOf(prods.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : prods.get("SECURITY_DEPOSIT_AMOUNT").toString());
				indentObj.setDoubleSecurityDeposit(securityDeposit);
				indentObj.setSecurityDeposit_WithCommas(format.format(securityDeposit));
			    String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "0" : prods.get("INDENT_ENTRY_ID").toString();
				indentObj.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
				double poAdvancePayment = Double.valueOf(prods.get("REMAINING_AMOUNT")==null ? "0" : prods.get("REMAINING_AMOUNT").toString());
			    indentObj.setDoublePOAdvancePayment(poAdvancePayment);
			    indentObj.setPoAdvancePayment_WithCommas(format.format(poAdvancePayment));
			    String payBalanceInPo = prods.get("PAY_BALANCE_IN_PO")==null ? "NO_ADVANCE" : prods.get("PAY_BALANCE_IN_PO").toString();
			    indentObj.setPayBalanceInPo(payBalanceInPo);
			    String paymentDoneOnMultipleInvoices = prods.get("PAYMENT_DONE_ON_MULTI_INV")==null ? "0" : prods.get("PAYMENT_DONE_ON_MULTI_INV").toString();
			    indentObj.setPaymentDoneOnMultipleInvoices(paymentDoneOnMultipleInvoices);
			    String adjustedAmountFromPo = prods.get("ADJUSTED_AMOUNT_FROM_PO")==null ? "0" : prods.get("ADJUSTED_AMOUNT_FROM_PO").toString();
			    indentObj.setAdjustedAmountFromPo(adjustedAmountFromPo);
			    indentObj.setDoubleAdjustAmountFromAdvance(Double.valueOf(prods.get("ADJUST_AMOUNT_FROM_ADVANCE")==null ? "0" : prods.get("ADJUST_AMOUNT_FROM_ADVANCE").toString()));
			    indentObj.setPaymentType(paymentType);
			    double paymentDoneUpto = Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" : prods.get("PAYMENT_DONE_UPTO").toString());
			    indentObj.setDoublePaymentDoneUpto(paymentDoneUpto);
			    indentObj.setDoublePaymentRequestedUpto(Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" : prods.get("PAYMENT_REQ_UPTO").toString()));
			    if(paymentType.equals("ADVANCE")){
					indentObj.setDoublePaidAmount(poAdvance);
					indentObj.setPaidAmount_WithCommas(format.format(poAdvance));
				}
			    else {
			    	indentObj.setDoublePaidAmount(paymentDoneUpto);
					indentObj.setPaidAmount_WithCommas(format.format(paymentDoneUpto));
			    }
			    

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String convertreceive_time = "";
				
				try{
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
					if(!receviedDate.equals("")){

					Date receive_date = dt.parse(receviedDate);
					receviedDate = dt1.format(receive_date);
					convertreceive_time = time1.format(receive_date);
					}
					if(!invoicedate.equals("")){

					Date invoice_date = dt.parse(invoicedate);
					invoicedate = dt1.format(invoice_date);
					}
					if(!poDate.equals("")){

						Date po_date = dt.parse(poDate);
						poDate = dt1.format(po_date);
						}
					
					if(!paymentDate.equals("")){

						Date payment_date = dt.parse(paymentDate);
						paymentDate = dt1.format(payment_date);
					}

					
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
			
				indentObj.setStrInvoiceDate(invoicedate);
				indentObj.setStrReceiveDate(receviedDate);
			//	indentObj.setTime(convertreceive_time);
				indentObj.setRequestedDate(paymentDate);
				indentObj.setStrPODate(poDate);
				//in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					indentObj.setStrInvoiceNo("");
					indentObj.setStrInvoiceDate("");
					indentObj.setStrReceiveDate("");
					indentObj.setStrInvoiceAmount("");
					indentObj.setStrCreditNoteNumber("");
					indentObj.setDoubleCreditTotalAmount(0);
					indentObj.setVendorInvoiceTotalAmount_WithCommas("0");
				    
				}
				if(paymentType.equals("DIRECT")){
				/*** display image ***/
			    for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
			    String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
			    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					indentObj.setHasImage(true);break;
					
					
				}
			    }
			    /**** END ***/
				}
				list.add(indentObj);
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;

	}
	
	
	@Override
	public List<PaymentBean> getAccDeptPaymentApprovalDetails(String siteId, String user_id, Map<String, String> map){

		String fromDate = map.get("fromDate");
		String toDate = map.get("toDate");
		String input_vendorId = map.get("vendorId");
		String invoiceNumber = map.get("invoiceNumber");
		String poNumber = map.get("poNumber");
		String dropdown_SiteId = map.get("dropdown_SiteId");
		
		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean indentObj = null;
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			//Because we changed dept id 997 to 997_B_1 & 997_H_1  , so we have to know which dept id is specifically.
			String  accDeptId = template.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", String.class);
			
		
				if (StringUtils.isNotBlank(siteId)) {
					query = " select ATPT.TEMP_PAYMENT_TRANSACTIONS_ID,ATPT.ACCOUNTS_DEPT_PMT_PROSS_ID,ATPT.PAYMENT_DETAILS_ID,ATPT.REQ_AMOUNT,ATPT.PAYMENT_TYPE,"
						   +" ATPT.PAYMENT_REQ_DATE,ATPT.PAYMENT_MODE,APM.NAME as PAYMENT_MODE_NAME,ATPT.UTR_CHQNO,ATPT.REMARKS,ATPT.SITEWISE_PAYMENT_NO,"
						   +"S.SITE_NAME,VD.VENDOR_NAME,AP.INVOICE_NUMBER,AP.INVOICE_AMOUNT,AP.PAYMENT_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO,"
						   +"AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,"
						   +"AP.VENDOR_ID,AP.SITE_ID,AP.INDENT_ENTRY_ID,AVSD.SECURITY_DEPOSIT_AMOUNT  "  
						   + ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_MINUS_INTIATED_AMOUNT ,AAPP.PAID_AMOUNT as PO_ADVANCE,AAPP.REMAINING_AMOUNT,ATPT.ADJUST_AMOUNT_FROM_ADVANCE,AADPP.CREATION_DATE  "
						   + ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
						   + ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
						   + ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
						   + ",ATID.TAXINVOICE_STATUS "
							+"from ACC_TEMP_PAYMENT_TRANSACTIONS ATPT LEFT OUTER JOIN ACC_PAYMENT_MODES APM on APM.VALUE = ATPT.PAYMENT_MODE "
						   + ",ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP,ACC_PAYMENT_DTLS APD,SITE S,VENDOR_DETAILS VD,"
						   +"ACC_PAYMENT AP "
						   + " LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A'  "
						   + " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
						   + " LEFT OUTER JOIN ACC_TAXINVOICE_DETAILS ATID on ATID.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID AND ATID.INVOICE_NO=AP.INVOICE_NUMBER AND ATID.SITE_ID=AP.SITE_ID "
						   
						   +"where ATPT.status='A' and ATPT.REQUEST_PENDING_EMP_ID = '"+accDeptId+"' "
						   +"and AP.PAYMENT_ID = APD.PAYMENT_ID "
						   +"and APD.PAYMENT_DETAILS_ID = ATPT.PAYMENT_DETAILS_ID "
						   +"and S.SITE_ID = AP.SITE_ID "
						   +"and VD.VENDOR_ID = AP.VENDOR_ID  "
						   
						   +"and AADPP.ACCOUNTS_DEPT_PMT_PROSS_ID = ATPT.ACCOUNTS_DEPT_PMT_PROSS_ID  ";
							
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					} else if (StringUtils.isNotBlank(fromDate)) {
						query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(toDate)) {
						query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
					}

					if (StringUtils.isNotBlank(input_vendorId)){
						query = query + " and AP.VENDOR_ID = '"+input_vendorId+"'";
					}
					if (StringUtils.isNotBlank(invoiceNumber)){
						query = query + " and AP.INVOICE_NUMBER = '"+invoiceNumber+"'";
					}
					if (StringUtils.isNotBlank(poNumber)){
						query = query + " and AP.PO_NUMBER = '"+poNumber+"'";
					}
					if (StringUtils.isNotBlank(dropdown_SiteId)){
						query = query + " and AP.SITE_ID = '"+dropdown_SiteId+"'";
					}
					
					query = query +" order by VD.VENDOR_NAME asc, S.SITE_NAME asc, ATPT.PAYMENT_REQ_DATE asc ";
					
							//In query we are taking results as per VendorName & site name ascending order to do grouping easily.
							//WARNING: If results are not in vendor name & site name ascending order, below code does not work.
			dbIndentDts = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			int vendorGroupSerialNo = 0;
			String currentVendorId="-";
			String currentSiteId="-";
			List<PaymentBean> invoiceIdList = new ArrayList<PaymentBean>();
			List<String> poIdList = new ArrayList<String>();
			for(Map<String, Object> prods : dbIndentDts) {
				String invoiceId = prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString();
				String poId = prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString();
				invoiceAmount=Math.round(Double.parseDouble(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString()));
				poAmount=Math.round(Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString()));
				String vendorName = prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString();
				String vendorId = prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString();
				String reqAmount = prods.get("REQ_AMOUNT")==null ? "" : prods.get("REQ_AMOUNT").toString();
				double paymentDoneUpto = Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" : prods.get("PAYMENT_DONE_UPTO").toString());
				double remainingAmountInAdvance = Double.valueOf(prods.get("REMAINING_AMOUNT")==null ? "0" : prods.get("REMAINING_AMOUNT").toString());
				double adjustAmountFromAdvance = Double.valueOf(prods.get("ADJUST_AMOUNT_FROM_ADVANCE")==null ? "0" : prods.get("ADJUST_AMOUNT_FROM_ADVANCE").toString());
				String site_id = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
				String site_name = prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString();
				String paymentType = prods.get("PAYMENT_TYPE")==null ? "" : prods.get("PAYMENT_TYPE").toString();
				double poAdvance = Double.valueOf(prods.get("PO_ADVANCE")==null ? "0" : prods.get("PO_ADVANCE").toString());
				String paymentDoneOnMultipleInvoices = prods.get("PAYMENT_DONE_ON_MULTI_INV")==null ? "0" : prods.get("PAYMENT_DONE_ON_MULTI_INV").toString();
			    // if vendor & site is different, creating separate Group object storing details of whole payments related to that vendor & add it to list.
				if(!currentVendorId.equals(vendorId)||!currentSiteId.equals(site_id))
				{
					vendorGroupSerialNo++;
					//this object stores for vendorGroup details.
					indentObj = new PaymentBean();
					indentObj.setStrVendorId(vendorId);
					indentObj.setStrVendorName(vendorName);
					indentObj.setStrSiteId(site_id);
					indentObj.setStrSiteName(site_name);
					indentObj.setVendorHeader(true);
					indentObj.setVendorGroupSerialNo(vendorGroupSerialNo);
					list.add(indentObj);
					currentVendorId=vendorId;
					currentSiteId=site_id;
					
				}
				//taking vendorGroup object related to this vendor in list.
				//add invoice amount of this vendor, to the vendorGroup object
				for(PaymentBean e:list){
					if(e.isVendorHeader()&&e.getStrVendorId().equals(vendorId)&&e.getStrSiteId().equals(site_id)){
						e.setIntNoofPaymentsVendorWise(e.getIntNoofPaymentsVendorWise()+1);
						//add req amount of this vendor, to the vendorGroup object
						double getReqAmountAfterAddition = Double.valueOf(e.getRequestedAmount()==null?"0":e.getRequestedAmount())+Double.valueOf(reqAmount);
						e.setRequestedAmount(String.valueOf(getReqAmountAfterAddition));
						e.setRequestedAmount_WithCommas(format.format(getReqAmountAfterAddition));
						//if it is Advance, take po amount
						if(paymentType.equals("ADVANCE")){
							//this below if block executes one time for each po number
							if(!poIdList.contains(poId)){
								//add po amount of this vendor, to the vendorGroup object
								double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+poAmount;
								e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
								e.setStrInvoiceAmount(String.format("%.2f",getInvoiceAmountAfterAddition));
								e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
								//add balance amount of this vendor, to the vendorGroup object
								double getBalanceAmountAfterAddition = e.getDoubleBalanceAmount()+poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)-remainingAmountInAdvance;//paymentDoneUpto-poAdvance;
								e.setDoubleBalanceAmount(getBalanceAmountAfterAddition);
								e.setBalanceAmt(String.format("%.2f",getBalanceAmountAfterAddition));
								e.setBalanceAmount_WithCommas(format.format(getBalanceAmountAfterAddition));
								poIdList.add(poId);
							}
						}
						//this below if block executes one time for each invoice number
						else if(!isInvoiceIdListContainsinvoiceId(invoiceIdList,invoiceId,vendorId,site_id)){
							//add invoice amount of this vendor, to the vendorGroup object
							double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+invoiceAmount;
							e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
							e.setStrInvoiceAmount(String.format("%.2f",getInvoiceAmountAfterAddition));
							e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
							//add balance amount of this vendor, to the vendorGroup object
							double getBalanceAmountAfterAddition = e.getDoubleBalanceAmount()+invoiceAmount-paymentDoneUpto;
							e.setDoubleBalanceAmount(getBalanceAmountAfterAddition);
							e.setBalanceAmt(String.format("%.2f",getBalanceAmountAfterAddition));
							e.setBalanceAmount_WithCommas(format.format(getBalanceAmountAfterAddition));
							PaymentBean obj = new PaymentBean();
							obj.setStrInvoiceNo(invoiceId);
							obj.setStrVendorId(vendorId);
							obj.setStrSiteId(site_id);
							invoiceIdList.add(obj);
						}
						//add balance amount of this vendor, to the vendorGroup object
						double finalBalAmt = e.getDoubleBalanceAmount()-Double.valueOf(reqAmount)-adjustAmountFromAdvance;
						e.setDoubleBalanceAmount(finalBalAmt);
						e.setBalanceAmt(String.format("%.2f",finalBalAmt));
						e.setBalanceAmount_WithCommas(format.format(finalBalAmt));
						//note: balance amount = invoiceAmount - paymentDoneUpto - reqAmount - adjustAmountFromAdvance
					}
				}
				
				serialNo++;
				indentObj = new PaymentBean();
				indentObj.setIntSerialNo(serialNo);
				indentObj.setVendorGroupSerialNo(vendorGroupSerialNo);
				indentObj.setStrInvoiceNo(invoiceId);
				indentObj.setStrPONo(poId);
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String podate=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				indentObj.setStrVendorName(vendorName);
				indentObj.setStrVendorId(vendorId);
				indentObj.setStrSiteId(site_id);
				indentObj.setRequestedAmount(reqAmount);
				String  paymentReqDate =(prods.get("PAYMENT_REQ_DATE")==null ? "" : prods.get("PAYMENT_REQ_DATE").toString());
				String receviedDate=prods.get("INVOICE_RECEIVED_DATE")==null ? "" : prods.get("INVOICE_RECEIVED_DATE").toString();
				String remarks = prods.get("REMARKS")==null ? "" : prods.get("REMARKS").toString();
				indentObj.setStrRemarks(remarks);
				indentObj.setStrRemarksForView(remarks.replace("@@@", ","));
				indentObj.setIntPaymentId(Integer.parseInt(prods.get("PAYMENT_ID")==null ? "" : prods.get("PAYMENT_ID").toString()));
				indentObj.setIntPaymentDetailsId(Integer.parseInt(prods.get("PAYMENT_DETAILS_ID")==null ? "0" : prods.get("PAYMENT_DETAILS_ID").toString()));
				indentObj.setIntSiteWisePaymentId(Integer.parseInt(prods.get("SITEWISE_PAYMENT_NO")==null ? "0" : prods.get("SITEWISE_PAYMENT_NO").toString()));
				indentObj.setStrSiteName(site_name);
				indentObj.setPaymentType(paymentType);
				String paymentMode = prods.get("PAYMENT_MODE")==null ? "" : prods.get("PAYMENT_MODE").toString();
				indentObj.setPaymentMode(paymentMode);
				if(StringUtils.isNotBlank(paymentMode)){
				indentObj.setPaymentModeName(prods.get("PAYMENT_MODE_NAME")==null ? "" : prods.get("PAYMENT_MODE_NAME").toString());
				}
				else{
					indentObj.setPaymentModeName("--SELECT--");
				}
				indentObj.setUtrChequeNo(prods.get("UTR_CHQNO")==null ? "" : prods.get("UTR_CHQNO").toString());
								
				indentObj.setDoubleInvoiceAmount(invoiceAmount);
				indentObj.setDoublePOTotalAmount(poAmount);
				indentObj.setStrPOAmount(String.format("%.2f",poAmount));
				indentObj.setPoAmount_WithCommas(format.format(poAmount));
				indentObj.setStrInvoiceAmount(String.format("%.2f",invoiceAmount));
				indentObj.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
				
				indentObj.setIntTempPaymentTransactionId(Integer.parseInt(prods.get("TEMP_PAYMENT_TRANSACTIONS_ID")==null ? "" : prods.get("TEMP_PAYMENT_TRANSACTIONS_ID").toString()));
				indentObj.setIntAccDeptPaymentProcessId(Integer.parseInt(prods.get("ACCOUNTS_DEPT_PMT_PROSS_ID")==null ? "" : prods.get("ACCOUNTS_DEPT_PMT_PROSS_ID").toString()));
				double creditTotalAmount = Double.valueOf(prods.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : prods.get("CREDIT_TOTAL_AMOUNT").toString());
				indentObj.setDoubleCreditTotalAmount(creditTotalAmount);
				indentObj.setCreditTotalAmount_WithCommas(format.format(creditTotalAmount));
				indentObj.setVendorInvoiceTotalAmount_WithCommas(format.format(invoiceAmount+creditTotalAmount));
				indentObj.setStrCreditNoteNumber(prods.get("CREDIT_NOTE_NUMBER")==null ? "" : prods.get("CREDIT_NOTE_NUMBER").toString());
				double securityDeposit = Double.valueOf(prods.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : prods.get("SECURITY_DEPOSIT_AMOUNT").toString());
				indentObj.setDoubleSecurityDeposit(securityDeposit);
				indentObj.setSecurityDeposit_WithCommas(format.format(securityDeposit));
				String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "0" : prods.get("INDENT_ENTRY_ID").toString();
			    indentObj.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			    double poAdvancePayment = Double.valueOf(prods.get("REMAINING_MINUS_INTIATED_AMOUNT")==null ? "0" : prods.get("REMAINING_MINUS_INTIATED_AMOUNT").toString());
			    indentObj.setDoublePOAdvancePayment(poAdvancePayment);
			    indentObj.setPoAdvancePayment_WithCommas(format.format(poAdvancePayment));
			    String payBalanceInPo = prods.get("PAY_BALANCE_IN_PO")==null ? "NO_ADVANCE" : prods.get("PAY_BALANCE_IN_PO").toString();
			    indentObj.setPayBalanceInPo(payBalanceInPo);
			    indentObj.setPaymentDoneOnMultipleInvoices(paymentDoneOnMultipleInvoices);
			    String adjustedAmountFromPo = prods.get("ADJUSTED_AMOUNT_FROM_PO")==null ? "0" : prods.get("ADJUSTED_AMOUNT_FROM_PO").toString();
			    indentObj.setAdjustedAmountFromPo(adjustedAmountFromPo);
			    String taxInvoiceStatus = prods.get("TAXINVOICE_STATUS")==null ? "" : prods.get("TAXINVOICE_STATUS").toString();
			    indentObj.setTaxInvoiceStatus(taxInvoiceStatus);
			    
			    indentObj.setDoubleAdjustAmountFromAdvance(adjustAmountFromAdvance);
			    indentObj.setAdjustAmountFromAdvance_WithCommas(format.format(adjustAmountFromAdvance));
			    double paymentRequestedUpto = Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" : prods.get("PAYMENT_REQ_UPTO").toString());
			    indentObj.setDoublePaymentRequestedUpto(paymentRequestedUpto);
			    indentObj.setPaymentRequestedUpto_WithCommas(format.format(paymentRequestedUpto));
			    indentObj.setDoublePaymentDoneUpto(paymentDoneUpto);
		    	if(paymentType.equals("ADVANCE")){
					indentObj.setDoublePaidAmount(poAdvance);
					indentObj.setDoubleBalanceAmount(poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)/*paymentDoneUpto*/-Double.valueOf(reqAmount)-remainingAmountInAdvance); //adjustAmountFromAdvance-poAdvance);
					indentObj.setBalanceAmt(String.format("%.2f",(poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)/*paymentDoneUpto*/-Double.valueOf(reqAmount)-remainingAmountInAdvance)));
			    	indentObj.setPaidAmount_WithCommas(format.format(poAdvance));
					indentObj.setBalanceAmount_WithCommas(format.format(poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)/*paymentDoneUpto*/-Double.valueOf(reqAmount)-remainingAmountInAdvance));
			    }
			    else {
			    	indentObj.setDoublePaidAmount(paymentDoneUpto);
			    	indentObj.setDoubleBalanceAmount(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance);
			    	indentObj.setBalanceAmt(String.format("%.2f",(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance)));
			    	indentObj.setPaidAmount_WithCommas(format.format(paymentDoneUpto));
					indentObj.setBalanceAmount_WithCommas(format.format(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance));
			    }
			    String paymentRequestReceivedDate = prods.get("CREATION_DATE")==null ? "" : prods.get("CREATION_DATE").toString();
				

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try{

					/*Date receive_date = dt.parse(receviedDate);*/
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");
					if(!paymentReqDate.equals("")){

						Date p_date = dt.parse(paymentReqDate);
						paymentReqDate = dt1.format(p_date);
					}
					if(!podate.equals("")){
						Date po_date = dt.parse(podate);
						podate = dt1.format(po_date);
					}
					if(!invoicedate.equals("")){
						Date invoice_date = dt.parse(invoicedate);
						invoicedate = dt1.format(invoice_date);
					}
					if(!receviedDate.equals("")){
						Date recevied_Date = dt.parse(receviedDate);
						receviedDate = dt1.format(recevied_Date);
					}
					if(!paymentRequestReceivedDate.equals("")){

						Date paymentRequest_ReceivedDate = dt.parse(paymentRequestReceivedDate);
						paymentRequestReceivedDate = dt1.format(paymentRequest_ReceivedDate);
						paymentRequestReceivedDate +=" "+ time1.format(paymentRequest_ReceivedDate);
						}
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
			
				indentObj.setStrInvoiceDate(invoicedate);
				indentObj.setStrPODate(podate);
				indentObj.setRequestedDate(paymentReqDate);
				indentObj.setStrInvoiceReceivedDate(receviedDate);
				indentObj.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);			
				//in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					indentObj.setStrInvoiceNo("");
					indentObj.setStrInvoiceDate("");
					indentObj.setStrReceiveDate("");
					indentObj.setStrInvoiceAmount("");
					indentObj.setStrCreditNoteNumber("");
					indentObj.setDoubleCreditTotalAmount(0);
					indentObj.setVendorInvoiceTotalAmount_WithCommas("0");
				    
				}
				if(paymentType.equals("DIRECT")){
				/*** display image ***/
			    for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
			    String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
			    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					indentObj.setHasImage(true);break;
					
				}
			    }
			    /**** END ***/
				}
				list.add(indentObj);
			}

		}//if
		} catch (Exception ex) {
			ex.printStackTrace();
		  } finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;

}
	@Override
	public String getPendingEmpId(String strUserId, String session_siteId){
		
		String pendingEmpId="";
		String query = "select APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  EMP_ID='"+strUserId+"'  and STATUS = 'A' AND MODULE_TYPE='PAYMENT' and SITE_ID='"+session_siteId+"'";
		pendingEmpId = jdbcTemplate.queryForObject(query, String.class);

		
		
		
		return pendingEmpId;
	}
	@Override
	public String getPendingDeptId(String strUserId, String session_siteId){
		
		String pendingDeptId="";
		String query = "select APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  EMP_ID='"+strUserId+"'  and STATUS = 'A' AND MODULE_TYPE='PAYMENT' and SITE_ID='"+session_siteId+"'";
		pendingDeptId = jdbcTemplate.queryForObject(query, String.class);

		
		
		
		return pendingDeptId;
	}
	@Override
	public int  saveApprovalDetails(String paymentId,String strUserId,String comments,String paymentDetailsId,String site_id){
	
		int count=0;
		int returnValue=0;
		
		String query = "insert into ACC_SITE_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,PAYMENT_ID,EMP_ID,OPERATION_TYPE,REMARKS,PAYMENT_DETAILS_ID,SITE_ID,CREATED_DATE,STATUS)values(PAYMENT_APPROVE_REJECT_SEQ_ID.nextval,"+Integer.parseInt(paymentId)+",'"+strUserId+"',"+"'A'"+",'"+comments+"',"+Integer.parseInt(paymentDetailsId)+",'"+site_id+"',sysdate,'A')";

 		count = jdbcTemplate.update(query, new Object[] {
				
				});
		/*if(count>0){
			
			String sql="update ACC_PAYMENT_DTLS set REQUEST_PENDING_EMP_ID = '"+pendingEmpId+"'  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
			
			returnValue=jdbcTemplate.update(sql, new Object[]  {paymentDetailsId});
			
		}*/

	
	return count;
	
	
}
	@Override
	public int  saveRejectDetails(String paymentId,String strUserId,String comments,String paymentDetailsId,String site_id){
		
		int count=0;
		int returnValue=0;
		
		String query = "insert into ACC_SITE_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,PAYMENT_ID,EMP_ID,OPERATION_TYPE,REMARKS,PAYMENT_DETAILS_ID,SITE_ID,CREATED_DATE)values(PAYMENT_APPROVE_REJECT_SEQ_ID.nextval,"+Integer.parseInt(paymentId)+",'"+strUserId+"','R','"+comments+"',"+Integer.parseInt(paymentDetailsId)+",'"+site_id+"',sysdate)";

		count = jdbcTemplate.update(query, new Object[] {});
		if(count>0){
			
			String sql="update ACC_PAYMENT_DTLS set STATUS = 'I'  where PAYMENT_DETAILS_ID=?";
			returnValue = jdbcTemplate.update(sql, new Object[]  {paymentDetailsId});
			
		}
		return returnValue;
	
	
}
	@Override
	public int saveChangedDetails(PaymentBean objPaymentBean,String paymentChangedType){
		
		int count=0;
		String query = "insert into ACC_SITE_PAYMENT_CHANGED_DTLS(PAYMENT_CHNG_DTLS_ID,PAYMENT_DETAILS_ID,ACTUAL_REQ_AMOUNT,CHANGED_REQ_AMOUNT,ACTUAL_PAYMENT_DATE,CHANGED_PAYMENT_DATE,CREATION_DATE,PAYMENT_CHANGE_ACTION,EMP_ID,REMARKS,ACTUAL_ADJUST_AMOUNT,CHANGED_ADJUST_AMOUNT)"
					+" values (PAYMENT_CHANGED_DTLS_SEQ.nextval,?,?,?,?,?,sysdate,?,?,?,?,?)";

		count = jdbcTemplate.update(query, new Object[] {
				objPaymentBean.getIntPaymentDetailsId(),objPaymentBean.getActualRequestedAmount(),
				objPaymentBean.getRequestedAmount(),objPaymentBean.getActualRequestedDate(),
				objPaymentBean.getRequestedDate(),paymentChangedType,objPaymentBean.getStrEmployeeId(),objPaymentBean.getStrRemarks(),
				objPaymentBean.getActualDoubleAdjustAmountFromAdvance(),objPaymentBean.getDoubleAdjustAmountFromAdvance()});
		
		return count;
		
	}
	@Override
	public int UpDatePaymentDetails(String paymentDetailsId,double changedAmount){
		
		String query="";
		int responseCount=0;
		
		query = "update ACC_PAYMENT_DTLS set REQ_AMOUNT = '"+changedAmount+"'  ,UPDATE_DATE=sysdate  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
		responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});
		
		return responseCount;
	}
	@Override
	public int updatePaymentDetailsAdjustAmount(String paymentDetailsId, double doubleAdjustAmountFromAdvance) {
		
		String query="";
		int responseCount=0;
		
		query = "update ACC_PAYMENT_DTLS set  ADJUST_AMOUNT_FROM_ADVANCE = '"+doubleAdjustAmountFromAdvance+"' ,UPDATE_DATE=sysdate  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
		responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});
		
		return responseCount;
	}
	
@Override
public String getDeptId(String strUserId,String session_siteId){
		
		String pendingDeptId="";
		String query = "SELECT APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID ='"+strUserId+"' AND MODULE_TYPE='PAYMENT' and STATUS='A' and SITE_ID='"+session_siteId+"'";
		pendingDeptId = jdbcTemplate.queryForObject(query, String.class);
	
		return pendingDeptId;
	}

@Override
public String getSiteAddressByUsingSiteId(String reqSiteId){
		
		String query = "SELECT ADDRESS FROM SITE where SITE_ID ='"+reqSiteId+"'";
		String address = jdbcTemplate.queryForObject(query, String.class);	
		return address;
	}
@Override
public int updateRequestedPaymentEmpId(String paymentDetailsId,String pendingEmpId){
	
	String query="";
	int responseCount=0;
	
	query = "update ACC_PAYMENT_DTLS set REQUEST_PENDING_EMP_ID = '"+pendingEmpId+"'  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
	responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});

	return responseCount;
}
@Override
public int updateRequestedPaymentDeptId(String paymentDetailsId,String pendingDeptId){
	
	String query="";
	int responseCount=0;
	
	query = "update ACC_PAYMENT_DTLS set REQUEST_PENDING_DEPT_ID = '"+pendingDeptId+"', REQUEST_PENDING_EMP_ID = '-'  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
	responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});

	return responseCount;
}
@Override
public int updateRequestedPaymentDeptIdOnAccDeptReject(String paymentDetailsId,String pendingEmpId){
	
	String query="";
	int responseCount=0;
	// make payment pending at site 3rd level 
	/*query = "update ACC_PAYMENT_DTLS set REQUEST_PENDING_DEPT_ID = '-', REQUEST_PENDING_EMP_ID = '"+pendingEmpId+"'  where PAYMENT_DETAILS_ID=?";
	responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});*/

	return responseCount;
}
@Override
public int updatePaymentDetailsDate(String paymentDetailsId,String  changedDate){
	
	String query="";
	int responseCount=0;
	
	query = "update ACC_PAYMENT_DTLS set PAYMENT_REQ_DATE = ? ,UPDATE_DATE=sysdate  where STATUS ='A' and PAYMENT_DETAILS_ID=?";
	responseCount = jdbcTemplate.update(query, new Object[]  {DateUtil.convertToJavaDateFormat(changedDate),Integer.parseInt(paymentDetailsId)});
	
	return responseCount;
}


@Override
public int getSiteWisePaymentNo(String site_id) {
	String query = "select max(SITEWISE_PAYMENT_NO) from ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP where APD.PAYMENT_ID = AP.PAYMENT_ID and AP.SITE_ID = '"+site_id+"'";

	int value = jdbcTemplate.queryForInt(query);

	return value+1;
}

@Override//not completed
public int saveAccountsDeptTable(PaymentBean objPaymentBean){
	
	int count=0;
	String query = "insert into ACC_ACCOUNTS_DEPT_PMT_PROSS(ACCOUNTS_DEPT_PMT_PROSS_ID,ACCOUNT_DEPT_REQ_REC_AMOUNT,ALLOCATED_AMOUNT,PENDING_AMOUNT,PROCESS_INTIATE_AMOUNT,STATUS,PAYMENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,PAYMENT_DETAILS_ID,ADJUST_AMOUNT_FROM_ADVANCE,ADJUST_INTIATE_AMOUNT)"
				+" values (ACCOUNTS_DEPT_PMT_PROSS_SEQ.nextval,?,?,?,?,?,?,?,sysdate,?,?,?)";

	count = jdbcTemplate.update(query, new Object[] {
			
			objPaymentBean.getRequestedAmount(),0,objPaymentBean.getRequestedAmount(),0,
			
			"A",objPaymentBean.getStrSiteId(),objPaymentBean.getStrEmployeeId(),
			
			objPaymentBean.getIntPaymentDetailsId(),objPaymentBean.getDoubleAdjustAmountFromAdvance(),0
	});
	
	return count;
	
}




	@Override
	public List<PaymentBean> getAccDeptPaymentPendingDetails(String strUserId,Map<String, String> map) {
		
		String fromDate = map.get("fromDate");
		String toDate = map.get("toDate");
		String input_vendorId = map.get("vendorId");
		String invoiceNumber = map.get("invoiceNumber");
		String poNumber = map.get("poNumber");
		String dropdown_SiteId = map.get("dropdown_SiteId");
		
		//Because we changed dept id 997 to 997_B_1 & 997_H_1  , so we have to know which dept id is specifically.
		String  accDeptId = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+strUserId+"'", String.class);
		
		
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<Map<String, Object>> paymentDtls = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		int i = 0;

		String query = "select AP.INVOICE_NUMBER,AP.INVOICE_AMOUNT,AP.PAYMENT_DONE_UPTO,AP.INVOICE_DATE,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,VND.VENDOR_NAME,AVSD.SECURITY_DEPOSIT_AMOUNT ,AP.INVOICE_RECEIVED_DATE,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.VENDOR_ID,AP.SITE_ID,AP.INDENT_ENTRY_ID,AP.PAYMENT_ID, "
					+ " APD.PAYMENT_REQ_DATE,APD.PAYMENT_DETAILS_ID,APD.SITEWISE_PAYMENT_NO,APD.PAYMENT_TYPE,S.SITE_NAME,AADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT,AADPP.PROCESS_INTIATE_AMOUNT,APD.REMARKS ,AADPP.ACCOUNTS_DEPT_PMT_PROSS_ID,APD.CREATION_DATE,AADPP.REQ_RECEIVE_FROM,AADPP.PAYMENT_DETAILS_ID,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO  "
					+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_MINUS_INTIATED_AMOUNT ,AAPP.PAID_AMOUNT as PO_ADVANCE,AAPP.REMAINING_AMOUNT ,AADPP.ADJUST_AMOUNT_FROM_ADVANCE,AADPP.ADJUST_INTIATE_AMOUNT  "
					+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
					+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
					+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
					+ " from VENDOR_DETAILS VND,SITE S ,ACC_PAYMENT_DTLS APD,ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP," 
					+ " ACC_PAYMENT AP "
					+ " LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A'  " 
					+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
					+ " where APD.PAYMENT_ID = AP.PAYMENT_ID and  APD.PAYMENT_DETAILS_ID = AADPP.PAYMENT_DETAILS_ID "
					+ " and AP.VENDOR_ID = VND.VENDOR_ID " 
					+ " and AP.SITE_ID = S.SITE_ID and APD.REQUEST_PENDING_DEPT_ID = ? and AADPP.STATUS = 'A' ";
		
		
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
		} else if (StringUtils.isNotBlank(fromDate)) {
			query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
		} else if(StringUtils.isNotBlank(toDate)) {
			query = query + " AND TRUNC(APD.PAYMENT_REQ_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
		}

		if (StringUtils.isNotBlank(input_vendorId)){
			query = query + " and AP.VENDOR_ID = '"+input_vendorId+"'";
		}
		if (StringUtils.isNotBlank(invoiceNumber)){
			query = query + " and AP.INVOICE_NUMBER = '"+invoiceNumber+"'";
		}
		if (StringUtils.isNotBlank(poNumber)){
			query = query + " and AP.PO_NUMBER = '"+poNumber+"'";
		}
		if (StringUtils.isNotBlank(dropdown_SiteId)){
			query = query + " and AP.SITE_ID = '"+dropdown_SiteId+"'";
		}
		/*+ "and AADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT > AADPP.PROCESS_INTIATE_AMOUNT";*/
		query = query +" order by VND.VENDOR_NAME asc, S.SITE_NAME asc, APD.PAYMENT_REQ_DATE asc, AADPP.CREATION_DATE asc  ";
					//In query we are taking results as per VendorName & site name ascending order to do grouping easily.
					//WARNING: If results are not in vendor name & site name ascending order, below code does not work.
		paymentDtls = jdbcTemplate.queryForList(query, new Object[] {accDeptId});
		int vendorGroupSerialNo = 0;
		String currentVendorId="-";
		String currentSiteId="-";
		String strInvoiceNumber="";// this is for get grn details time invoice number contain '&'
		List<PaymentBean> invoiceIdList = new ArrayList<PaymentBean>();
		List<String> poIdList = new ArrayList<String>();
		for(Map<String, Object> paymentReqs : paymentDtls) {
			double reqRecAmount = Double.valueOf(paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT")==null ? "0" :   paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT").toString());
			double procIntiateAmount = Double.valueOf(paymentReqs.get("PROCESS_INTIATE_AMOUNT")==null ? "0" :   paymentReqs.get("PROCESS_INTIATE_AMOUNT").toString());
			double adjustAmountFromAdvance = Double.valueOf(paymentReqs.get("ADJUST_AMOUNT_FROM_ADVANCE")==null ? "0" : paymentReqs.get("ADJUST_AMOUNT_FROM_ADVANCE").toString());
			double adjustIntiateAmount = Double.valueOf(paymentReqs.get("ADJUST_INTIATE_AMOUNT")==null ? "0" : paymentReqs.get("ADJUST_INTIATE_AMOUNT").toString());
			if((reqRecAmount<=procIntiateAmount)&&(adjustAmountFromAdvance<=adjustIntiateAmount)){continue;}
			i = i+1;
			String invoiceId = paymentReqs.get("INVOICE_NUMBER")==null ? "" : paymentReqs.get("INVOICE_NUMBER").toString();
			strInvoiceNumber=paymentReqs.get("INVOICE_NUMBER")==null ? "" : paymentReqs.get("INVOICE_NUMBER").toString();
			if(strInvoiceNumber.contains("&")){strInvoiceNumber=strInvoiceNumber.replace('&','@');}
			String poId = paymentReqs.get("PO_NUMBER")==null ? "" : paymentReqs.get("PO_NUMBER").toString();
			invoiceAmount=Math.round(Double.parseDouble(paymentReqs.get("INVOICE_AMOUNT")==null ? "" : paymentReqs.get("INVOICE_AMOUNT").toString()));
			poAmount=Math.round(Double.parseDouble(paymentReqs.get("PO_AMOUNT")==null ? "" : paymentReqs.get("PO_AMOUNT").toString()));
			String vendorName = paymentReqs.get("VENDOR_NAME")==null ? "" : paymentReqs.get("VENDOR_NAME").toString();
			String vendorId = paymentReqs.get("VENDOR_ID")==null ? "" : paymentReqs.get("VENDOR_ID").toString();
			double reqAmount = reqRecAmount-procIntiateAmount;
			double paymentDoneUpto = Double.valueOf(paymentReqs.get("PAYMENT_DONE_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_DONE_UPTO").toString());
			double remainingAmountInAdvance = Double.valueOf(paymentReqs.get("REMAINING_AMOUNT")==null ? "0" : paymentReqs.get("REMAINING_AMOUNT").toString());
			String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
			String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
			String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
			double poAdvance = Double.valueOf(paymentReqs.get("PO_ADVANCE")==null ? "0" : paymentReqs.get("PO_ADVANCE").toString());
			String paymentDoneOnMultipleInvoices = paymentReqs.get("PAYMENT_DONE_ON_MULTI_INV")==null ? "0" : paymentReqs.get("PAYMENT_DONE_ON_MULTI_INV").toString();
		    // if vendor & site is different, creating separate Group object storing details of whole payments related to that vendor & add it to list.
			if(!currentVendorId.equals(vendorId)||!currentSiteId.equals(site_id))
			{
				vendorGroupSerialNo++;
				//this object stores for vendorGroup details.
				PaymentBean indentObj = new PaymentBean();
				indentObj.setStrVendorId(vendorId);
				indentObj.setStrVendorName(vendorName);
				indentObj.setStrSiteId(site_id);
				indentObj.setStrSiteName(site_name);
				indentObj.setVendorHeader(true);
				indentObj.setVendorGroupSerialNo(vendorGroupSerialNo);
				list.add(indentObj);
				currentVendorId=vendorId;
				currentSiteId=site_id;
				
			}
			//taking vendorGroup object related to this vendor in list.
			//add invoice amount of this vendor, to the vendorGroup object
			for(PaymentBean e:list){
				if(e.isVendorHeader()&&e.getStrVendorId().equals(vendorId)&&e.getStrSiteId().equals(site_id)){
					e.setIntNoofPaymentsVendorWise(e.getIntNoofPaymentsVendorWise()+1);
					//add req amount of this vendor, to the vendorGroup object
					double getReqAmountAfterAddition = Double.valueOf(e.getRequestedAmount()==null?"0":e.getRequestedAmount())+reqAmount;
					e.setRequestedAmount(String.valueOf(getReqAmountAfterAddition));
					e.setRequestedAmount_WithCommas(format.format(getReqAmountAfterAddition));
					//if it is Advance, take po amount
					if(paymentType.equals("ADVANCE")){
						//this below if block executes one time for each po number
						if(!poIdList.contains(poId)){
							//add po amount of this vendor, to the vendorGroup object
							double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+poAmount;
							e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
							e.setStrInvoiceAmount(String.format("%.2f",getInvoiceAmountAfterAddition));
							e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
							//add balance amount of this vendor, to the vendorGroup object
							double getBalanceAmountAfterAddition = e.getDoubleBalanceAmount()+poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)-remainingAmountInAdvance;//paymentDoneUpto-poAdvance;
							e.setDoubleBalanceAmount(getBalanceAmountAfterAddition);
							e.setBalanceAmt(String.format("%.2f",getBalanceAmountAfterAddition));
							e.setBalanceAmount_WithCommas(format.format(getBalanceAmountAfterAddition));
							poIdList.add(poId);
						}
					}
					//this below if block executes one time for each invoice number
					else if(!isInvoiceIdListContainsinvoiceId(invoiceIdList,invoiceId,vendorId,site_id)){
						//add invoice amount of this vendor, to the vendorGroup object
						double getInvoiceAmountAfterAddition = e.getDoubleInvoiceAmount()+invoiceAmount;
						e.setDoubleInvoiceAmount(getInvoiceAmountAfterAddition);
						e.setStrInvoiceAmount(String.format("%.2f",getInvoiceAmountAfterAddition));
						e.setInvoiceAmount_WithCommas(format.format(getInvoiceAmountAfterAddition));
						//add balance amount of this vendor, to the vendorGroup object
						double getBalanceAmountAfterAddition = e.getDoubleBalanceAmount()+invoiceAmount-paymentDoneUpto;
						e.setDoubleBalanceAmount(getBalanceAmountAfterAddition);
						e.setBalanceAmt(String.format("%.2f",getBalanceAmountAfterAddition));
						e.setBalanceAmount_WithCommas(format.format(getBalanceAmountAfterAddition));
						PaymentBean obj = new PaymentBean();
						obj.setStrInvoiceNo(invoiceId);
						//obj.setStrNumber(strInvoiceNumber);
						obj.setStrVendorId(vendorId);
						obj.setStrSiteId(site_id);
						invoiceIdList.add(obj);
					}
					//add balance amount of this vendor, to the vendorGroup object
					double finalBalAmt = e.getDoubleBalanceAmount()-reqAmount-adjustAmountFromAdvance;
					e.setDoubleBalanceAmount(finalBalAmt);
					e.setBalanceAmt(String.format("%.2f",finalBalAmt));
					e.setBalanceAmount_WithCommas(format.format(finalBalAmt));
					//note: balance amount = invoiceAmount - paymentDoneUpto - reqAmount - adjustAmountFromAdvance					
				}
			}
			

			PaymentBean objPaymentBean = new PaymentBean();


			objPaymentBean.setVendorGroupSerialNo(vendorGroupSerialNo);
			objPaymentBean.setStrInvoiceNo(invoiceId);
			//objPaymentBean.setStrNumber(strInvoiceNumber);
			objPaymentBean.setDoubleInvoiceAmount(invoiceAmount);
			objPaymentBean.setStrInvoiceAmount(String.format("%.2f",invoiceAmount));
			objPaymentBean.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
			objPaymentBean.setDoublePaymentDoneUpto(Double.valueOf(paymentReqs.get("PAYMENT_DONE_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_DONE_UPTO").toString()));
			objPaymentBean.setStrPONo(poId);
			String invoicedate = paymentReqs.get("INVOICE_DATE")==null ? "" :   paymentReqs.get("INVOICE_DATE").toString();
			objPaymentBean.setStrVendorName(vendorName);
			String receviedDate = paymentReqs.get("INVOICE_RECEIVED_DATE")==null ? "" :   paymentReqs.get("INVOICE_RECEIVED_DATE").toString();
			String paymentDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
			objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" :   paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
			objPaymentBean.setStrSiteName(site_name);
			objPaymentBean.setDoubleAmountToBeReleased(reqRecAmount-procIntiateAmount);
			
			objPaymentBean.setPaymentType(paymentType);
			String remarks = paymentReqs.get("REMARKS")==null ? "" :   paymentReqs.get("REMARKS").toString();
			objPaymentBean.setStrRemarks(remarks);
			objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
			objPaymentBean.setIntPaymentId(Integer.parseInt(paymentReqs.get("PAYMENT_ID")==null ? "" :   paymentReqs.get("PAYMENT_ID").toString()));
			objPaymentBean.setIntPaymentDetailsId(Integer.parseInt(paymentReqs.get("PAYMENT_DETAILS_ID")==null ? "" :   paymentReqs.get("PAYMENT_DETAILS_ID").toString()));
			objPaymentBean.setIntAccDeptPaymentProcessId(Integer.parseInt(paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID")==null ? "" :   paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID").toString()));
			objPaymentBean.setIntSerialNo(i);
			objPaymentBean.setDoublePOTotalAmount(poAmount);
			objPaymentBean.setStrPOAmount(String.format("%.2f",poAmount));
			objPaymentBean.setPoAmount_WithCommas(format.format(poAmount));
			String  poDate =(paymentReqs.get("PO_DATE")==null ? "" : paymentReqs.get("PO_DATE").toString());
			double creditTotalAmount = Double.valueOf(paymentReqs.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : paymentReqs.get("CREDIT_TOTAL_AMOUNT").toString());
			objPaymentBean.setDoubleCreditTotalAmount(creditTotalAmount);
			objPaymentBean.setCreditTotalAmount_WithCommas(format.format(creditTotalAmount));
			objPaymentBean.setVendorInvoiceTotalAmount_WithCommas(format.format(invoiceAmount+creditTotalAmount));
			objPaymentBean.setStrCreditNoteNumber(paymentReqs.get("CREDIT_NOTE_NUMBER")==null ? "" : paymentReqs.get("CREDIT_NOTE_NUMBER").toString());
			double securityDeposit = Double.valueOf(paymentReqs.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : paymentReqs.get("SECURITY_DEPOSIT_AMOUNT").toString());
			objPaymentBean.setDoubleSecurityDeposit(securityDeposit);
			objPaymentBean.setSecurityDeposit_WithCommas(format.format(securityDeposit));
		    objPaymentBean.setStrVendorId(vendorId);
			objPaymentBean.setStrSiteId(site_id);
			String strIndentEntryId = paymentReqs.get("INDENT_ENTRY_ID")==null ? "0" : paymentReqs.get("INDENT_ENTRY_ID").toString();
			objPaymentBean.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			String paymentRequestReceivedDate = paymentReqs.get("CREATION_DATE")==null ? "" : paymentReqs.get("CREATION_DATE").toString();
			double poAdvancePayment = Double.valueOf(paymentReqs.get("REMAINING_MINUS_INTIATED_AMOUNT")==null ? "0" : paymentReqs.get("REMAINING_MINUS_INTIATED_AMOUNT").toString());
			objPaymentBean.setDoublePOAdvancePayment(poAdvancePayment);
			objPaymentBean.setPoAdvancePayment_WithCommas(format.format(poAdvancePayment));
			String payBalanceInPo = paymentReqs.get("PAY_BALANCE_IN_PO")==null ? "NO_ADVANCE" : paymentReqs.get("PAY_BALANCE_IN_PO").toString();
			objPaymentBean.setPayBalanceInPo(payBalanceInPo);
			objPaymentBean.setPaymentDoneOnMultipleInvoices(paymentDoneOnMultipleInvoices);
		    String adjustedAmountFromPo = paymentReqs.get("ADJUSTED_AMOUNT_FROM_PO")==null ? "0" : paymentReqs.get("ADJUSTED_AMOUNT_FROM_PO").toString();
		    objPaymentBean.setAdjustedAmountFromPo(adjustedAmountFromPo);
		    double paymentRequestedUpto = Double.valueOf(paymentReqs.get("PAYMENT_REQ_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_REQ_UPTO").toString());
			objPaymentBean.setDoublePaymentRequestedUpto(paymentRequestedUpto);
			objPaymentBean.setPaymentRequestedUpto_WithCommas(format.format(paymentRequestedUpto));
		    objPaymentBean.setDoubleAdjustAmountFromAdvance(adjustAmountFromAdvance-adjustIntiateAmount);
		    objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto);
		    if(paymentType.equals("ADVANCE")){
				objPaymentBean.setDoublePaidAmount(poAdvance);
				objPaymentBean.setDoubleBalanceAmount(poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)/*paymentDoneUpto*/-Double.valueOf(reqAmount)-remainingAmountInAdvance); //adjustAmountFromAdvance-poAdvance);
				objPaymentBean.setPaidAmount_WithCommas(format.format(poAdvance));
				objPaymentBean.setBalanceAmount_WithCommas(format.format(poAmount-Double.valueOf(paymentDoneOnMultipleInvoices)-Double.valueOf(reqAmount)-remainingAmountInAdvance)); //adjustAmountFromAdvance-poAdvance));
			}
		    else {
		    	objPaymentBean.setDoublePaidAmount(paymentDoneUpto);
		    	objPaymentBean.setDoubleBalanceAmount(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance);
		    	objPaymentBean.setPaidAmount_WithCommas(format.format(paymentDoneUpto));
				objPaymentBean.setBalanceAmount_WithCommas(format.format(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance));
			}
		    objPaymentBean.setRequestReceiveFrom(paymentReqs.get("REQ_RECEIVE_FROM")==null ? "" : paymentReqs.get("REQ_RECEIVE_FROM").toString());
		    
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{
				
				SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");
				
				if(!receviedDate.equals("")){

					Date receive_date = dt.parse(receviedDate);
					receviedDate = dt1.format(receive_date);
					}
				if(!invoicedate.equals("")){

				Date invoice_date = dt.parse(invoicedate);
				invoicedate = dt1.format(invoice_date);
				}
				if(!poDate.equals("")){

					Date po_date = dt.parse(poDate);
					poDate = dt1.format(po_date);
					}
				if(!paymentDate.equals("")){

					Date payment_date = dt.parse(paymentDate);
					paymentDate = dt1.format(payment_date);
				}
				if(!paymentRequestReceivedDate.equals("")){

					Date paymentRequest_ReceivedDate = dt.parse(paymentRequestReceivedDate);
					paymentRequestReceivedDate = dt1.format(paymentRequest_ReceivedDate);
					paymentRequestReceivedDate +=" "+ time1.format(paymentRequest_ReceivedDate);
					}
				
			}
		
			catch(Exception e){
				e.printStackTrace();
			}
		
			objPaymentBean.setStrInvoiceReceivedDate(receviedDate);
			objPaymentBean.setStrInvoiceDate(invoicedate);
			objPaymentBean.setStrPODate(poDate);
			objPaymentBean.setStrPaymentReqDate(paymentDate);
			objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);			
			//in ADVANCE, emptying Invoice details
			if(paymentType.equals("ADVANCE")){
				objPaymentBean.setStrInvoiceNo("");
				objPaymentBean.setStrInvoiceDate("");
				objPaymentBean.setStrReceiveDate("");
				objPaymentBean.setStrInvoiceAmount("");
				objPaymentBean.setStrCreditNoteNumber("");
				objPaymentBean.setDoubleCreditTotalAmount(0);
				objPaymentBean.setVendorInvoiceTotalAmount_WithCommas("0");
			}
			if(paymentType.equals("DIRECT")){
			/*** display image ***/
			try{
		    for(int index=0;index<8;index++){
		    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
		    String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
			imgname = imgname.replace("/","$$");
		    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+index+".jpg");
		    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

			if(f.exists()||f1.exists()){
				
				objPaymentBean.setHasImage(true);break;
				
			}
		    }
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		    /**** END ***/
			}
			list.add(objPaymentBean);
		}
		return list;
	}


	public int updateAccDeptPaymentProcsstbl(String strAccDeptProcessSeqId, String strPaymentDetailsId,String strPendingEmpId,double payingAmount,double processIntiateAmount,double doubleReqAmount) {


		double tatalProcessIntiateAmount = 0;
		String	query = "";
		int result = 0;
		tatalProcessIntiateAmount = payingAmount+tatalProcessIntiateAmount;


		if(tatalProcessIntiateAmount >doubleReqAmount){


			query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set STATUS= ?,PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT + '"+payingAmount+"' where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {"I",strAccDeptProcessSeqId});

		}else{

			query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set  PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT + '"+payingAmount+"' where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
			result = jdbcTemplate.update(query, new Object[] { strAccDeptProcessSeqId});

		}


		return result;

	}

	/*public int updatePaymentsDetailstbl(String strAccDeptProcessSeqId, double payingAmount,double processIntiateAmount,double doubleReqAmount) {

		double tatalProcessIntiateAmount = 0;
		String	query = "";
		int result = 0;
		tatalProcessIntiateAmount = payingAmount+tatalProcessIntiateAmount;


		if(tatalProcessIntiateAmount >doubleReqAmount){


			query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set STATUS= ?,PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT - '"+payingAmount+"' where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {"A",strAccDeptProcessSeqId});

		}else{

			query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set  PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT - '"+payingAmount+"' where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
			result = jdbcTemplate.update(query, new Object[] { strAccDeptProcessSeqId});

		}


		return result;

	}*/

	@Override
	public int getAccTempPaymentTransactionSeqNo()

	{
		int TempPaymentId = 0;

		String query = "select  ACC_TEMP_PMT_TRANSACTIONS_SEQ.nextval from dual";

		TempPaymentId = jdbcTemplate.queryForInt(query);
		
		return TempPaymentId;
	}
	@Override
	public int getAccDeptPaymentProssSeqNo()

	{

		String query = "select  ACCOUNTS_DEPT_PMT_PROSS_SEQ.nextval from dual";

		return jdbcTemplate.queryForInt(query);
		
		
	}

	
	@Override
	public int insertTempPaymentTransactionsTbl(PaymentDto objPaymentDto, int tempPaymentId)

	{



		


		String query = "INSERT INTO ACC_TEMP_PAYMENT_TRANSACTIONS(TEMP_PAYMENT_TRANSACTIONS_ID,ACCOUNTS_DEPT_PMT_PROSS_ID,PAYMENT_DETAILS_ID,REQ_AMOUNT,REQUEST_PENDING_EMP_ID,"+
		"STATUS,PAYMENT_TYPE,PAYMENT_REQ_DATE,PAYMENT_MODE,UTR_CHQNO,REMARKS,CREATION_DATE,SITEWISE_PAYMENT_NO,UPDATE_DATE,ADJUST_AMOUNT_FROM_ADVANCE)" +
		" values(? , ?, ?, ?, ?, ?, ?, ?,?,?,?,sysdate, ?,sysdate,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				tempPaymentId,objPaymentDto.getIntAccDeptPaymentProcessId(),
				objPaymentDto.getIntPaymentDetailsId(),
				objPaymentDto.getDoubleAmountToBeReleased(),objPaymentDto.getStrPendingDeptId(),"A",
				objPaymentDto.getPaymentType(),
				DateUtil.convertToJavaDateFormat(objPaymentDto.getStrPaymentReqDate()),objPaymentDto.getPaymentMode(),
				objPaymentDto.getStrRefrenceNo(),objPaymentDto.getStrRemarks(),
				objPaymentDto.getIntSiteWisePaymentId(),
				objPaymentDto.getDoubleAdjustAmountFromAdvance()

				//	productDetails.getStrTermsConditionId()

		});




		return result;
	}


	@Override
	public String getApproverEmpIdInAccounts(String strEmpId) {

		List<Map<String, Object>> pendingEmpId = null;
		String strPendingEmpId = "";
		try {
			jdbcTemplate = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and STATUS='A' and MODULE_TYPE = 'ACCOUNTS'";

		pendingEmpId = jdbcTemplate.queryForList(query, new Object[] {strEmpId});
		for(Map<String, Object> pendingEmployeeId : pendingEmpId) {

			strPendingEmpId = pendingEmployeeId.get("APPROVER_EMP_ID")==null ? "" :   pendingEmployeeId.get("APPROVER_EMP_ID").toString();
			}
			
		
		return strPendingEmpId;
	}
	@Override
	public String getApproverDeptIdInAccounts(String strEmpId) {

		List<Map<String, Object>> pendingEmpId = null;
		String strPendingDeptId = "";
		try {
			jdbcTemplate = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "select APPROVER_DEPT_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and STATUS='A' and MODULE_TYPE = 'ACCOUNTS'";

		pendingEmpId = jdbcTemplate.queryForList(query, new Object[] {strEmpId});
		for(Map<String, Object> pendingEmployeeId : pendingEmpId) {
			strPendingDeptId = pendingEmployeeId.get("APPROVER_DEPT_ID")==null ? "" :   pendingEmployeeId.get("APPROVER_DEPT_ID").toString();
		}
		return strPendingDeptId;
	}
	@Override
	public int updateAccDeptPaymentProcsstbl(PaymentDto objPaymentDto) {
		String query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT+? ,"
				+ "ADJUST_INTIATE_AMOUNT = ADJUST_INTIATE_AMOUNT+? "
				+ "where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentDto.getDoubleAmountToBeReleased(),objPaymentDto.getDoubleAdjustAmountFromAdvance(),
						objPaymentDto.getIntAccDeptPaymentProcessId()

						

				});
		return result;
	}
	@Override
	public int updateTempPaymentTransactionsTbl(PaymentDto objPaymentDto,String strPendingDeptId) {
		String query = "update ACC_TEMP_PAYMENT_TRANSACTIONS set REQUEST_PENDING_EMP_ID = ?,PAYMENT_MODE=?,UTR_CHQNO=?,ADJUST_AMOUNT_FROM_ADVANCE=?,REMARKS=? "
				+ "where TEMP_PAYMENT_TRANSACTIONS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						strPendingDeptId,
						objPaymentDto.getPaymentMode(),
						objPaymentDto.getUtrChequeNo(),
						objPaymentDto.getDoubleAdjustAmountFromAdvance(),
						objPaymentDto.getStrRemarks(),
						objPaymentDto.getIntTempPaymentTransactionId()
						

						//	productDetails.getStrTermsConditionId()

				});
		return result;
	}
	@Override
	public int insertPaymentTransactionsTbl(PaymentDto objPaymentDto,int intPaymentTransactionId) {
		
		String query = "INSERT INTO ACC_PAYMENT_TRANSACTIONS(PAYMENT_TRANSACTIONS_ID,PAYMENT_DETAILS_ID,PAYMENT_ID,PAID_AMOUNT,"
				+ "PAYMENT_TYPE,PAYMENT_MODE,UTR_CHQNO,REMARKS,CREATION_DATE,PAYMENT_DATE,SITEWISE_PAYMENT_NO,TYPE_OF_PAYMENT_SETTLEMENT,UPDATION,ADJUSTFROMPO,PAYMENT_DONE_BY,TEMP_PAYMENT_TRANSACTIONS_ID)" +
				" values(?, ?, ?, ?, ?, ?,?,?,sysdate,sysdate, ?,?,?,?,?,?)";

				int result = jdbcTemplate.update(query, new Object[] {
						intPaymentTransactionId,
						objPaymentDto.getIntPaymentDetailsId(),objPaymentDto.getIntPaymentId(),
						objPaymentDto.getDoubleAmountToBeReleased(),
						objPaymentDto.getPaymentType(),
						objPaymentDto.getPaymentMode(),
						objPaymentDto.getUtrChequeNo(),objPaymentDto.getStrRemarks(),
						objPaymentDto.getIntSiteWisePaymentId(),
						"REGULAR",
						"A",
						objPaymentDto.getDoubleAdjustAmountFromAdvance(),
						"ACCOUNTS",
						objPaymentDto.getIntTempPaymentTransactionId()
				});
		
		
				/********************************************************************/
				

		return result;
	}

	
	@Override
	public int updatePaymentReqUptoInAccPaymentTable(PaymentDto objPaymentDto) {
		int result1=0;
		
			//updating PAYMENT_REQ_UPTO in ACC_PAYMENT table
			String query1 = "update ACC_PAYMENT set PAYMENT_REQ_UPTO=PAYMENT_REQ_UPTO-? where PAYMENT_ID = ? ";

							result1 = jdbcTemplate.update(query1, new Object[] {
									objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getActualDoubleAdjustAmountFromAdvance(),
									objPaymentDto.getIntPaymentId()
									
							});
			
							
		
		return result1;
	}
	@Override
	public int updatePaymentReqUptoAndPaymentDoneUptoInAccPaymentTable(PaymentDto objPaymentDto) {
		int result1=0;
		
		
		//updating PAYMENT_DONE_UPTO & PAYMENT_REQ_UPTO in ACC_PAYMENT table
			String query1 = "update ACC_PAYMENT set PAYMENT_DONE_UPTO=PAYMENT_DONE_UPTO+?,PAYMENT_REQ_UPTO=PAYMENT_REQ_UPTO-? where PAYMENT_ID = ? ";

							result1 = jdbcTemplate.update(query1, new Object[] {
						(objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getDoubleAdjustAmountFromAdvance()),
						objPaymentDto.getDoubleAmountToBeReleased()+objPaymentDto.getActualDoubleAdjustAmountFromAdvance(),
						objPaymentDto.getIntPaymentId()
						
				});	
		
		return result1;
	}
	
	@Override
	public int insertPoHistory(PaymentDto objPaymentDto) {
		
				
					String query = "INSERT INTO ACC_PO_HISTORY(PO_HISTORY_ID,PO_NUMBER,PO_AMOUNT,ENTRY_DATE,PAID_AMOUNT)" +
							" values( PO_HISTORY_SEQ.nextval,?, ?,sysdate, ?)";

							int result = jdbcTemplate.update(query, new Object[] {
									objPaymentDto.getStrPONo(),
									objPaymentDto.getDoublePOAmount(),
									objPaymentDto.getDoubleAmountToBeReleased()
									

							});
					
									
				
		return result;
	}
	@Override
	public int insertInvoiceHistory(PaymentDto objPaymentDto,int intPaymentTransactionId) {
		String query = "INSERT INTO ACC_INVOICE_HISTORY(INVOICE_HISTORY_ID,INVOICE_NUMBER,INVOICE_AMOUNT,ENTRY_DATE,PAID_AMOUNT,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID)" +
				" values( INVOICE_HISTORY_SEQ.nextval,?,?,sysdate, ?,?,?,?)";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentDto.getStrInvoiceNo(),
						objPaymentDto.getDoubleInvoiceAmount(),
						objPaymentDto.getDoubleAmountToBeReleased(),
						objPaymentDto.getPaymentMode(),
						objPaymentDto.getUtrChequeNo(),
						intPaymentTransactionId

				});
		
					
		return result;
	}
	@Override
	public int insertInvoiceHistoryAdjust(PaymentDto objPaymentDto,int intPaymentTransactionId) {
		
			String query1 = "INSERT INTO ACC_INVOICE_HISTORY(INVOICE_HISTORY_ID,INVOICE_NUMBER,INVOICE_AMOUNT,ENTRY_DATE,PAID_AMOUNT,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID)" +
					" values( INVOICE_HISTORY_SEQ.nextval,?,?,sysdate, ?,?,?,?)";

					int result1 = jdbcTemplate.update(query1, new Object[] {
							objPaymentDto.getStrInvoiceNo(),
							objPaymentDto.getDoubleInvoiceAmount(),
							objPaymentDto.getDoubleAdjustAmountFromAdvance(),
							"PO_ADJUSTMENT",
							objPaymentDto.getStrPONo(),
							intPaymentTransactionId

					});
		
		return result1;
	}
	public List<PaymentBean> getViewPaymentDetails(String fromDate, String toDate, String vendorId, String user_id, String dropdown_SiteId, String invoiceNumber, String poNumber, String selectAll) {

		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbPOsList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 
		String strInvoiceNumber="";
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			
				query = "select APT.PAYMENT_TYPE,APT.PAYMENT_ID,APT.PAYMENT_DETAILS_ID,APT.PAID_AMOUNT,AP.VENDOR_ID,VD.VENDOR_NAME,AP.INVOICE_NUMBER,AP.INVOICE_DATE,AP.PO_NUMBER,AP.PO_DATE,AP.INDENT_ENTRY_ID,AP.SITE_ID,S.SITE_NAME,APT.CREATION_DATE,APT.PAYMENT_DATE,APT.UTR_CHQNO,APT.REMARKS "
						+ "from ACC_PAYMENT_TRANSACTIONS APT,ACC_PAYMENT AP,VENDOR_DETAILS VD,SITE S "
						+ "where APT.PAYMENT_ID = AP.PAYMENT_ID and VD.VENDOR_ID = AP.VENDOR_ID and S.SITE_ID = AP.SITE_ID and APT.TYPE_OF_PAYMENT_SETTLEMENT = 'REGULAR' ";
				
				if(!selectAll.equals("true")){
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						query = query + " and TRUNC(APT.PAYMENT_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
						//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					} else if (StringUtils.isNotBlank(fromDate)) {
						query = query + " and TRUNC(APT.PAYMENT_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(toDate)) {
						query = query + " and TRUNC(APT.PAYMENT_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
					}

					if(StringUtils.isNotBlank(vendorId)){
						query = query+" and AP.VENDOR_ID='"+vendorId+"'";
					}
					if(StringUtils.isNotBlank(dropdown_SiteId)){
						query = query+" and AP.SITE_ID='"+dropdown_SiteId+"'";
					}
					if (StringUtils.isNotBlank(invoiceNumber)){
						query = query + " and AP.INVOICE_NUMBER = '"+invoiceNumber+"'";
					}
					if (StringUtils.isNotBlank(poNumber)){
						query = query + " and AP.PO_NUMBER = '"+poNumber+"'";
					}
				}
				// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
				/** take dept_id of login employee.
				 ** decide address hyd or banglore.
				 ** take list of sites in that address.
				 ** form a String using sites to pass into query.
				 *
				 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
				 *Site level also using same query site accountent will not available dept_id
				 */
				String dept_id = template.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
				if(dept_id != null && dept_id.contains("H")){
					String address="HYD";
					List<String> siteIdList = template.queryForList("select SITE_ID from SITE where ADDRESS = '"+address+"'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and AP.SITE_ID in("+groupOfSites+")";
					}
				}
				/***************************************/


			dbPOsList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> po : dbPOsList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String paymentType = po.get("PAYMENT_TYPE")==null ? "" : po.get("PAYMENT_TYPE").toString();
				objPaymentBean.setStrPONo(po.get("PO_NUMBER")==null ? "" : po.get("PO_NUMBER").toString());
				objPaymentBean.setStrInvoiceNo(po.get("INVOICE_NUMBER")==null ? "" : po.get("INVOICE_NUMBER").toString());
				strInvoiceNumber=po.get("INVOICE_NUMBER")==null ? "" : po.get("INVOICE_NUMBER").toString();
				if(strInvoiceNumber.contains("&")){strInvoiceNumber=strInvoiceNumber.replace('&','@');}
				//objPaymentBean.setStrNumber(strInvoiceNumber);
				String poDate = po.get("PO_DATE")==null ? "" : po.get("PO_DATE").toString();
				String invoiceDate = po.get("INVOICE_DATE")==null ? "" : po.get("INVOICE_DATE").toString();
				String paymentDate = po.get("PAYMENT_DATE")==null ? "" : po.get("PAYMENT_DATE").toString();
				String creationDate = po.get("CREATION_DATE")==null ? "" : po.get("CREATION_DATE").toString();
				
				double paidAmount = Double.valueOf(po.get("PAID_AMOUNT")==null ? "0" : po.get("PAID_AMOUNT").toString());
				objPaymentBean.setDoublePaidAmount(paidAmount);
				objPaymentBean.setPaidAmount_WithCommas(format.format(paidAmount));
				
				objPaymentBean.setStrVendorName(po.get("VENDOR_NAME")==null ? "" : po.get("VENDOR_NAME").toString());
				objPaymentBean.setStrVendorId(po.get("VENDOR_ID")==null ? "" : po.get("VENDOR_ID").toString());
				
				objPaymentBean.setIntPaymentId(Integer.valueOf(po.get("PAYMENT_ID")==null ? "0" : po.get("PAYMENT_ID").toString()));
				objPaymentBean.setIntPaymentDetailsId(Integer.valueOf(po.get("PAYMENT_DETAILS_ID")==null ? "0" : po.get("PAYMENT_DETAILS_ID").toString()));
				objPaymentBean.setIntIndentEntryId(Integer.valueOf(po.get("INDENT_ENTRY_ID")==null ? "0" : po.get("INDENT_ENTRY_ID").toString()));
				objPaymentBean.setStrSiteId(po.get("SITE_ID")==null ? "0" : po.get("SITE_ID").toString());
				objPaymentBean.setStrSiteName(po.get("SITE_NAME")==null ? "0" : po.get("SITE_NAME").toString());
				objPaymentBean.setUtrChequeNo(po.get("UTR_CHQNO")==null ? "" : po.get("UTR_CHQNO").toString());
				String remarks = po.get("REMARKS")==null ? "" : po.get("REMARKS").toString();
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
				
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				try{
			    	if(!poDate.equals("")){
			    		Date po_date = dt.parse(poDate);
			    		poDate = dt1.format(po_date);
			    	}
			    	if(!invoiceDate.equals("")){
			    		Date invoice_Date = dt.parse(invoiceDate);
			    		invoiceDate = dt1.format(invoice_Date);
			    	}
			    	if(!paymentDate.equals("")){
			    		Date payment_Date = dt.parse(paymentDate);
			    		paymentDate = dt1.format(payment_Date);
			    	}
			    	if(!creationDate.equals("")){
			    		Date creation_Date = dt.parse(creationDate);
			    		creationDate = dt1.format(creation_Date);
			    	}
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrPODate(poDate);
			    objPaymentBean.setStrInvoiceDate(invoiceDate);
			    objPaymentBean.setStrPaymentDate(paymentDate);
			    objPaymentBean.setStrCreatedDate(creationDate);
			    //in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					objPaymentBean.setStrInvoiceNo("");
					objPaymentBean.setStrInvoiceDate("");
					objPaymentBean.setStrReceiveDate("");
					objPaymentBean.setStrInvoiceAmount("");
					objPaymentBean.setStrCreditNoteNumber("");
					objPaymentBean.setDoubleCreditTotalAmount(0);
				}
				list.add(objPaymentBean);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}
	public List<PaymentBean> viewMyPayment(String fromDate, String toDate,String site_id, String user_id, String vendorId, String invoiceNumber, String poNumber, String selectAll) {

		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbPOsList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null;
		String strInvoiceNumber="";
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			
				/*String frontQuery = "select APD.CREATION_DATE,APD.PAYMENT_ID,AP.VENDOR_ID,VD.VENDOR_NAME,AP.INVOICE_NUMBER,"
						+ "AP.PO_NUMBER,APD.PAYMENT_DETAILS_ID,APD.REQUEST_PENDING_EMP_ID,APD.REQ_AMOUNT,APD.REQUEST_PENDING_DEPT_ID,"
						+ "ATPT.REQUEST_PENDING_EMP_ID as ACC_PENDING_EMP_ID,ATPT.REQ_AMOUNT as ACC_REQ_AMOUNT"
						+ ",SED.EMP_NAME,SDD.DEPT_NAME,ATPT.REMARKS,APD.REMARKS as APD_REMARKS,AP.INDENT_ENTRY_ID,AP.INVOICE_DATE,AP.SITE_ID  " 
						+ "from VENDOR_DETAILS VD,ACC_PAYMENT AP,ACC_PAYMENT_DTLS APD LEFT OUTER JOIN ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID = ATPT.PAYMENT_DETAILS_ID "
						+ "LEFT OUTER JOIN SUMADHURA_EMPLOYEE_DETAILS SED on (SED.EMP_ID = APD.REQUEST_PENDING_EMP_ID or SED.EMP_ID = ATPT.REQUEST_PENDING_EMP_ID) "
						+ "LEFT OUTER JOIN SUMADHURA_DEPARTMENT_DETAILS SDD on SDD.DEPT_ID = APD.REQUEST_PENDING_DEPT_ID "
						+ "where AP.PAYMENT_ID = APD.PAYMENT_ID and AP.VENDOR_ID = VD.VENDOR_ID "
						+ "and (ATPT.REQUEST_PENDING_EMP_ID NOT IN ('VND') OR ATPT.REQUEST_PENDING_EMP_ID IS NULL) "
						+ "and TRUNC(APD.CREATION_DATE) ";*/
				query = "select APD.PAYMENT_TYPE,APD.CREATION_DATE,APD.PAYMENT_REQ_DATE,AADPP.CREATION_DATE as ACC_DEPT_CAME_DATE,APD.PAYMENT_ID,AP.VENDOR_ID,VD.VENDOR_NAME,AP.INVOICE_NUMBER,"
						+ "AP.PO_NUMBER,APD.PAYMENT_DETAILS_ID,APD.REQUEST_PENDING_EMP_ID,APD.REQ_AMOUNT,APD.REQUEST_PENDING_DEPT_ID,"
						+ "ATPT.REQUEST_PENDING_EMP_ID as ACC_PENDING_EMP_ID,ATPT.REQ_AMOUNT as ACC_REQ_AMOUNT"
						+ ",SED.EMP_NAME,ATPT.REMARKS as ATPT_REMARKS,APD.REMARKS as APD_REMARKS,AP.INDENT_ENTRY_ID,AP.INVOICE_DATE,AP.SITE_ID,S.SITE_NAME,APD.STATUS,ATPT.STATUS as ATPT_STATUS  " 
						+ "from VENDOR_DETAILS VD,ACC_PAYMENT AP,SITE S,ACC_PAYMENT_DTLS APD LEFT OUTER JOIN ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID = ATPT.PAYMENT_DETAILS_ID and ATPT.STATUS='A'  "
						+ "LEFT OUTER JOIN ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP on AADPP.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID "
						+ "LEFT OUTER JOIN SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID = APD.REQUEST_PENDING_EMP_ID "
						+ "where AP.PAYMENT_ID = APD.PAYMENT_ID and AP.VENDOR_ID = VD.VENDOR_ID and AP.SITE_ID = S.SITE_ID "
						+ "and (ATPT.REQUEST_PENDING_EMP_ID NOT IN ('VND') OR ATPT.REQUEST_PENDING_EMP_ID IS NULL) ";
				
				if(!selectAll.equals("true")){
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						query = query + " and TRUNC(APD.CREATION_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
						//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					} else if (StringUtils.isNotBlank(fromDate)) {
						query = query + " and TRUNC(APD.CREATION_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(toDate)) {
						query = query + " and TRUNC(APD.CREATION_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
					}

					if (StringUtils.isNotBlank(vendorId)){
						query = query + " and AP.VENDOR_ID = '"+vendorId+"'";
					}
					if (StringUtils.isNotBlank(invoiceNumber)){
						query = query + " and AP.INVOICE_NUMBER = '"+invoiceNumber+"'";
					}
					if (StringUtils.isNotBlank(poNumber)){
						query = query + " and AP.PO_NUMBER = '"+poNumber+"'";
					}
				}
			String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();

			if(!site_id.equals(accDeptId)&&!site_id.equals("998")){
				query = query + " and AP.SITE_ID = '"+site_id+"'";
			}
			else if(site_id.equals(accDeptId)){
				// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
				/** take dept_id of login employee.
				 ** decide address hyd or banglore.
				 ** take list of sites in that address.
				 ** form a String using sites to pass into query.
				 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
				 *Site level also using same query site accountent will not available dept_id
				*/
				String dept_id = template.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
				
				if(dept_id.contains("H")){
					String address="HYD";
					List<String> siteIdList = template.queryForList("select SITE_ID from SITE where ADDRESS = '"+address+"'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and AP.SITE_ID in("+groupOfSites+")";
					}
				}
			}
			
			dbPOsList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> po : dbPOsList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String paymentType = po.get("PAYMENT_TYPE")==null ? "" : po.get("PAYMENT_TYPE").toString();
				objPaymentBean.setStrPONo(po.get("PO_NUMBER")==null ? "" : po.get("PO_NUMBER").toString());
				objPaymentBean.setStrInvoiceNo(po.get("INVOICE_NUMBER")==null ? "" : po.get("INVOICE_NUMBER").toString());
				strInvoiceNumber=po.get("INVOICE_NUMBER")==null ? "" : po.get("INVOICE_NUMBER").toString();
				if(strInvoiceNumber.contains("&")){strInvoiceNumber=strInvoiceNumber.replace('&','@');}
				//objPaymentBean.setStrNumber(strInvoiceNumber);
				String poDate = po.get("PO_DATE")==null ? "" : po.get("PO_DATE").toString();//not in query
				String invoiceDate = po.get("INVOICE_DATE")==null ? "" : po.get("INVOICE_DATE").toString();//not in query
				String createdDate = po.get("CREATION_DATE")==null ? "" : po.get("CREATION_DATE").toString();
				String paymentReqDate = po.get("PAYMENT_REQ_DATE")==null ? "" : po.get("PAYMENT_REQ_DATE").toString();
				String accDeptCameDate = po.get("ACC_DEPT_CAME_DATE")==null ? "" : po.get("ACC_DEPT_CAME_DATE").toString();
				
				
				objPaymentBean.setStrVendorName(po.get("VENDOR_NAME")==null ? "" : po.get("VENDOR_NAME").toString());
				objPaymentBean.setStrVendorId(po.get("VENDOR_ID")==null ? "" : po.get("VENDOR_ID").toString());
				
				objPaymentBean.setIntIndentEntryId(Integer.valueOf(po.get("INDENT_ENTRY_ID")==null ? "" : po.get("INDENT_ENTRY_ID").toString()));
				objPaymentBean.setStrInvoiceDate(po.get("INVOICE_DATE")==null ? "" : po.get("INVOICE_DATE").toString());
				objPaymentBean.setStrSiteId(po.get("SITE_ID")==null ? "" : po.get("SITE_ID").toString());
				objPaymentBean.setStrSiteName(po.get("SITE_NAME")==null ? "" : po.get("SITE_NAME").toString());
				String status=po.get("ATPT_STATUS")==null ? "" : po.get("ATPT_STATUS").toString();
				if(StringUtils.isBlank(status)){
					status=po.get("STATUS")==null ? "" : po.get("STATUS").toString();
				}
				objPaymentBean.setStatus(status.equals("I")?"REJECTED":"ACTIVE");
				
				String remarks = po.get("ATPT_REMARKS")==null ? "" : po.get("ATPT_REMARKS").toString();
				if(StringUtils.isBlank(remarks)){
					remarks = po.get("APD_REMARKS")==null ? "" : po.get("APD_REMARKS").toString();
				}
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
				
				objPaymentBean.setIntPaymentId(Integer.valueOf(po.get("PAYMENT_ID")==null ? "0" : po.get("PAYMENT_ID").toString()));
				objPaymentBean.setIntPaymentDetailsId(Integer.valueOf(po.get("PAYMENT_DETAILS_ID")==null ? "0" : po.get("PAYMENT_DETAILS_ID").toString()));
				String pendingEmpId = po.get("REQUEST_PENDING_EMP_ID")==null ? "" : po.get("REQUEST_PENDING_EMP_ID").toString();
				String pendingEmpName = po.get("EMP_NAME")==null ? "" : po.get("EMP_NAME").toString();
				double reqAmount = Double.valueOf(po.get("REQ_AMOUNT")==null ? "0" : po.get("REQ_AMOUNT").toString());
				String pendingDeptId = po.get("REQUEST_PENDING_DEPT_ID")==null ? "" : po.get("REQUEST_PENDING_DEPT_ID").toString();
				String accPendingEmpId = po.get("ACC_PENDING_EMP_ID")==null ? "" : po.get("ACC_PENDING_EMP_ID").toString();
				double accTransactionAmount = Double.valueOf(po.get("ACC_REQ_AMOUNT")==null ? "0" : po.get("ACC_REQ_AMOUNT").toString());
				
				objPaymentBean.setDoubleAmountToBeReleased(reqAmount);
				objPaymentBean.setAmountToBeReleased_WithCommas(format.format(reqAmount));
				objPaymentBean.setDoubleTransactionAmount(accTransactionAmount);
				objPaymentBean.setTransactionAmount_WithCommas(format.format(accTransactionAmount));
				if(StringUtils.isNotBlank(pendingEmpId) && !pendingEmpId.equals("-")){
					objPaymentBean.setStrPendingDeptName("SITE");
					objPaymentBean.setStrPendingEmpName(pendingEmpName);
				}
				else if(StringUtils.isNotBlank(pendingDeptId) && pendingDeptId.contains(accDeptId)){
					objPaymentBean.setStrPendingDeptName("Accounts Department");
					
					if(accPendingEmpId.contains("2")){
						objPaymentBean.setStrPendingEmpName("Second Level");
					}
					else if(accPendingEmpId.contains("3")){
						objPaymentBean.setStrPendingEmpName("Third Level");
					}
					else {
						objPaymentBean.setStrPendingEmpName("First Level");
					}
					
					/*objPaymentBean.setStrPendingEmpName(pendingEmpName);
					if(StringUtils.isBlank(pendingEmpName)){
						String query1 = "select SED.EMP_NAME from SUMADHURA_APPROVER_MAPPING_DTL SAMD join SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID=SAMD.EMP_ID where SAMD.MODULE_TYPE = 'ACCOUNTS' and  SAMD.EMP_ID not in(select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where MODULE_TYPE = 'ACCOUNTS')";
						try {
							pendingEmpName = template.queryForObject(query1, String.class);
						} catch (IncorrectResultSizeDataAccessException e) {
							pendingEmpName = "First Level";
							objPaymentBean.setStrPendingEmpName(pendingEmpName);
						}
						if(!pendingEmpName.equals("First Level")){
							objPaymentBean.setStrPendingEmpName(pendingEmpName);
						}
					}*/
					if(accPendingEmpId.equals("VND")){
						objPaymentBean.setStrPendingDeptName("VND");
						objPaymentBean.setStrPendingEmpName("VND");
					}
				}
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				try{
			    	if(!poDate.equals("")){
			    		Date po_date = dt.parse(poDate);
			    		poDate = dt1.format(po_date);
			    	}
			    	if(!invoiceDate.equals("")){
			    		Date invoice_Date = dt.parse(invoiceDate);
			    		invoiceDate = dt1.format(invoice_Date);
			    	}
			    	if(!createdDate.equals("")){
			    		Date created_Date = dt.parse(createdDate);
			    		createdDate = dt1.format(created_Date);
			    	}
			    	if(!paymentReqDate.equals("")){
			    		Date paymentReq_Date = dt.parse(paymentReqDate);
			    		paymentReqDate = dt1.format(paymentReq_Date);
			    	}
			    	if(!accDeptCameDate.equals("")){
			    		Date accDeptCame_Date = dt.parse(accDeptCameDate);
			    		accDeptCameDate = dt1.format(accDeptCame_Date);
			    	}
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrPODate(poDate);
			    objPaymentBean.setStrInvoiceDate(invoiceDate);
			    objPaymentBean.setStrCreatedDate(createdDate);
			    objPaymentBean.setStrPaymentReqDate(paymentReqDate);
			    objPaymentBean.setStrPaymentRequestReceivedDate(accDeptCameDate);
			  //in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					objPaymentBean.setStrInvoiceNo("");
					objPaymentBean.setStrInvoiceDate("");
					objPaymentBean.setStrReceiveDate("");
					objPaymentBean.setStrInvoiceAmount("");
					objPaymentBean.setStrCreditNoteNumber("");
					objPaymentBean.setDoubleCreditTotalAmount(0);
				}
				list.add(objPaymentBean);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}
	
	@Override
	public boolean checkingIsTotalPoAmountIsPaid(String poNo,double reqAmount){

		String payableAmt="";
		try{
			String query = "select PO_AMOUNT-PAID_AMOUNT from ACC_ADVANCE_PAYMENT_PO where PO_NUMBER='"+poNo+"' ";
			payableAmt = jdbcTemplate.queryForObject(query, String.class);
			if(reqAmount>Double.parseDouble(payableAmt)){
				return true;
			}
			else{
				return false;
			}
		}catch(EmptyResultDataAccessException e){
			return false;
		}
		
	}
	
	@Override
	public String getRemainingAmountInAdvance(String poNo){

		String remainingAmountInAdvance="";
		try{
			String query = "select REMAINING_AMOUNT-INTIATED_AMOUNT from ACC_ADVANCE_PAYMENT_PO where PO_NUMBER='"+poNo+"' ";
			remainingAmountInAdvance = jdbcTemplate.queryForObject(query, String.class);
		}catch(EmptyResultDataAccessException e){
			remainingAmountInAdvance = "0";
		}
		return remainingAmountInAdvance;
	}
	
	@Override
	public int updateReqUptoInAccPayment(String paymentId, String actualRequestedAmount,String changedRequestedAmount, double actualDoubleAdjustAmountFromAdvance, double doubleAdjustAmountFromAdvance) {
		int count=0;
		//minus actual values and plus changed values in PAYMENT_REQ_UPTO
		String query = "update ACC_PAYMENT set PAYMENT_REQ_UPTO=PAYMENT_REQ_UPTO-?+?-?+? where PAYMENT_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {Double.valueOf(actualRequestedAmount),Double.valueOf(changedRequestedAmount),actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance,paymentId});

		return count;
	}
	@Override
	public int updateReqUptoInAccPaymentOnReject(String paymentId, String actualRequestedAmount, double actualDoubleAdjustAmountFromAdvance) {
		int count=0;
		//minus actual values in PAYMENT_REQ_UPTO
		String query = "update ACC_PAYMENT set PAYMENT_REQ_UPTO=PAYMENT_REQ_UPTO-?-? where PAYMENT_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {Double.valueOf(actualRequestedAmount),actualDoubleAdjustAmountFromAdvance,paymentId});

		return count;
	}
	@Override
	public int updateIntiateAmountInAdvancePaymentPO(String poNo, double actualDoubleAdjustAmountFromAdvance,double doubleAdjustAmountFromAdvance) {
		int count=0;
		String query = "update ACC_ADVANCE_PAYMENT_PO set INTIATED_AMOUNT=INTIATED_AMOUNT-?+? where PO_NUMBER=? ";
		count = jdbcTemplate.update(query, new Object[] {actualDoubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance,poNo});

		return count;
	}
	@Override
	public int updateIntiateAmountInAdvancePaymentPOOnReject(String poNo, double actualDoubleAdjustAmountFromAdvance) {
		int count=0;
		//minus actual values in ACC_ADVANCE_PAYMENT_PO
		String query = "update ACC_ADVANCE_PAYMENT_PO set INTIATED_AMOUNT=INTIATED_AMOUNT-? where PO_NUMBER=? ";
		count = jdbcTemplate.update(query, new Object[] {actualDoubleAdjustAmountFromAdvance,poNo});

		return count;
	}
	
	@Override
	public int insertPaidAmountInAdvancePaymentPoTable(PaymentDto objPaymentDto) {
		
		int result=0;
		//checking is there any row on this po number already. 
		String query = "select count(*) from ACC_ADVANCE_PAYMENT_PO where PO_NUMBER=? ";	
		int count = jdbcTemplate.queryForInt(query,objPaymentDto.getStrPONo());
		if(count==0){
		
		//if there is no row, inserting ACC_ADVANCE_PAYMENT_PO  table.
			String query1 = "insert into ACC_ADVANCE_PAYMENT_PO(PO_ADVANCE_PAYMENT_ID,PO_NUMBER ,PO_AMOUNT ,PAID_AMOUNT ,ADJUSTED_AMOUNT,REMAINING_AMOUNT ,INTIATED_AMOUNT ) "
					+ " values(PO_ADVANCE_PAYMENT_SEQ.nextval,?,?,?,?,?,?)";

							result = jdbcTemplate.update(query1, new Object[] {
									objPaymentDto.getStrPONo(),objPaymentDto.getDoublePOAmount(),objPaymentDto.getDoubleAmountToBeReleased(),
									0.0,objPaymentDto.getDoubleAmountToBeReleased(),0.0
									
							});	
		}
		else{
			//if there is a row already, updating ACC_ADVANCE_PAYMENT_PO  table on that Po number.
			String query2 = "update ACC_ADVANCE_PAYMENT_PO set PAID_AMOUNT=PAID_AMOUNT+? ,REMAINING_AMOUNT=REMAINING_AMOUNT+?  "
					+ " where PO_NUMBER = ?";

							result = jdbcTemplate.update(query2, new Object[] {
									objPaymentDto.getDoubleAmountToBeReleased(),objPaymentDto.getDoubleAmountToBeReleased(),
									objPaymentDto.getStrPONo()
									
							});	
		}
			
		
		
		return result;
	}
	
	@Override
	public int updateAdvancePaymentPoTable(String poNo, double doubleAdjustAmountFromAdvance) {
		int count=0;
		String query = "update ACC_ADVANCE_PAYMENT_PO set ADJUSTED_AMOUNT=ADJUSTED_AMOUNT+?, REMAINING_AMOUNT=REMAINING_AMOUNT-? ,INTIATED_AMOUNT=INTIATED_AMOUNT-?  where PO_NUMBER=? ";
		count = jdbcTemplate.update(query, new Object[] {doubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance,doubleAdjustAmountFromAdvance,poNo});

		
		
		return count;
	}
	@Override
	public String getLowerEmpIdInAccounts(String strEmpId) {
		String lowerEmpId="";
		String query =    "select distinct(DEPT_ID) from ("
						+ "select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ("
						+ "select EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where APPROVER_DEPT_ID in("
						+ "select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID= '"+strEmpId+"') and STATUS='A'))";
		lowerEmpId = jdbcTemplate.queryForObject(query, String.class);
		
		return lowerEmpId;
	}
	/*@Override
	public List<String> getPaymentInitiatorInAccounts() {
		List<String> paymentInitiator=new ArrayList<String>();
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where MODULE_TYPE = 'ACCOUNTS' and EMP_ID  not in (select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where MODULE_TYPE = 'ACCOUNTS')";
		paymentInitiator = jdbcTemplate.queryForList(query, String.class);
		
		return paymentInitiator;
	}*/
	@Override
	public int revertPendingApprovalToLowerEmployee(String strLowerEmpId, int intTempPaymentTransactionId,String remarks) {
		int count=0;
		String query = "update ACC_TEMP_PAYMENT_TRANSACTIONS set REQUEST_PENDING_EMP_ID=?,REMARKS=? where TEMP_PAYMENT_TRANSACTIONS_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {strLowerEmpId,remarks,intTempPaymentTransactionId});

		
		
		return count;
	}
	@Override
	public int updateRowInAccTempPaymentTransactions(int intTempPaymentTransactionId,String remarks,int intPaymentDetailsId) {
		int count=0;
		String query = "update ACC_TEMP_PAYMENT_TRANSACTIONS set STATUS = 'I' , REMARKS = ? where TEMP_PAYMENT_TRANSACTIONS_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {remarks,intTempPaymentTransactionId});

		String query2 = "update ACC_PAYMENT_DTLS set REMARKS = ? where PAYMENT_DETAILS_ID=? ";
		count = jdbcTemplate.update(query2, new Object[] {remarks,intPaymentDetailsId});

		
		return count;
	}
	@Override
	public int updateIntiateAmountInAccDeptPaymentProcesstbl(String requestedAmount, int intAccDeptPaymentProcessId,double actualDoubleAdjustAmountFromAdvance) {
		String query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT-? ,"
				+ "ADJUST_INTIATE_AMOUNT = ADJUST_INTIATE_AMOUNT-? "
				+ "where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						requestedAmount,actualDoubleAdjustAmountFromAdvance, intAccDeptPaymentProcessId

				});
		return result;
	}
	
	@Override
	public int saveAccountsApproveRejectTable(PaymentDto objPaymentDto,int intAccDeptPaymentProcessId,int intTempPaymentTransactionId){

		int count = 0;

		
			String query = "insert into ACC_PMT_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,ACCOUNTS_DEPT_PMT_PROSS_ID,EMP_ID,STATUS," +
					"REMARKS,TEMP_PAYMENT_TRANSACTIONS_ID,SITE_ID,CREATED_DATE,OPERATION_TYPE)values" +
					"(PMT_APPROVE_REJECT_SEQ_ID.nextval,?,?,?,?,?,?,sysdate,?)";

			count = jdbcTemplate.update(query, new Object[] {
					intAccDeptPaymentProcessId,objPaymentDto.getStrEmployeeId(),"A",objPaymentDto.getStrRemarks(),
					intTempPaymentTransactionId,objPaymentDto.getStrSiteId(),"C"});

		return count;

	}
	@Override
	public int  saveAccountApprovalDetails(int intAccDeptPaymentProcessId,String strUserId,String comments,int intTempPaymentTransactionId,String site_id){
	
		int count=0;
		
		String query = "insert into ACC_PMT_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,ACCOUNTS_DEPT_PMT_PROSS_ID,EMP_ID,OPERATION_TYPE,REMARKS,TEMP_PAYMENT_TRANSACTIONS_ID,SITE_ID,CREATED_DATE,STATUS)values(PMT_APPROVE_REJECT_SEQ_ID.nextval,"+intAccDeptPaymentProcessId+",'"+strUserId+"',"+"'A'"+",'"+comments+"',"+intTempPaymentTransactionId+",'"+site_id+"',sysdate,'A')";
														
 		count = jdbcTemplate.update(query, new Object[] {
				
				});
		
	return count;
	
	
}
	
	@Override
	public int saveAccountRejectDetails(int intAccDeptPaymentProcessId, String strEmpId, String comments,
			int intTempPaymentTransactionId, String siteId) {	
		int count=0;
		
		String query = "insert into ACC_PMT_APPR_REJECT_DTLS(PAYMENT_APROV_REJECT_SEQ,ACCOUNTS_DEPT_PMT_PROSS_ID,EMP_ID,OPERATION_TYPE,REMARKS,TEMP_PAYMENT_TRANSACTIONS_ID,SITE_ID,CREATED_DATE,STATUS)values(PMT_APPROVE_REJECT_SEQ_ID.nextval,"+intAccDeptPaymentProcessId+",'"+strEmpId+"','R','"+comments+"',"+intTempPaymentTransactionId+",'"+siteId+"',sysdate,'A')";

		count = jdbcTemplate.update(query, new Object[] {});
		
		return count;
	
	
}
	@Override
	public int saveAccountChangedDetails(PaymentBean objPaymentBean,String paymentChangedType){
		
		int count=0;
		String query = "insert into ACC_PAYMENT_CHANGED_DTLS(PAYMENT_CHNG_DTLS_ID,TEMP_PAYMENT_TRANSACTIONS_ID,ACTUAL_ADJUST_AMOUNT,CHANGED_ADJUST_AMOUNT,ACTUAL_PAYMENT_MODE,CHANGED_PAYMENT_MODE,ACTUAL_REF_NO,CHANGED_REF_NO,CREATION_DATE,PAYMENT_CHANGE_ACTION,EMP_ID,REMARKS)"
					+" values (ACC_PAYMENT_CHANGED_DTLS_SEQ.nextval,?,?,?,?,?,?,?,sysdate,?,?,?)";

		count = jdbcTemplate.update(query, new Object[] {
				objPaymentBean.getIntTempPaymentTransactionId(),objPaymentBean.getActualDoubleAdjustAmountFromAdvance(),objPaymentBean.getDoubleAdjustAmountFromAdvance(),
				objPaymentBean.getActualPaymentMode(),objPaymentBean.getPaymentMode(),
				objPaymentBean.getActualUtrChequeNo(),objPaymentBean.getUtrChequeNo(),
				paymentChangedType,objPaymentBean.getStrEmployeeId(),objPaymentBean.getStrRemarks()
				});
		
		return count;
		
	}
	@Override
	public int removeRowInAccDeptPmtProcessTbl(int accDeptPmtProcessId) {
		int count=0;
		String query = "delete from ACC_ACCOUNTS_DEPT_PMT_PROSS where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
		
		count = jdbcTemplate.update(query, new Object[] {
				accDeptPmtProcessId
				});
		
		return count;
	}
	@Override
	public int setInactiveRowInAccDeptPmtProcessTbl(int accDeptPmtProcessId) {
		int count=0;
		
		String query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set STATUS='I'  where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
		count = jdbcTemplate.update(query, new Object[] {
				accDeptPmtProcessId
				});
		
		return count;
	}
	@Override
	public List<PaymentBean> getAccDeptPaymentDetailsToUpdate(String siteId, String user_id, String fromDate, String toDate, String vendorId, String invoiceNumber, String poNumber, String dropdown_SiteId, String selectAll){

		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean indentObj = null;
		String strInvoiceNumber="";
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

		
				if (StringUtils.isNotBlank(siteId)) {
					query = " select APT.PAYMENT_TRANSACTIONS_ID,APT.PAYMENT_DETAILS_ID,APT.PAID_AMOUNT,APT.PAYMENT_TYPE,"
							+"APT.PAYMENT_DATE,APT.PAYMENT_MODE,APT.UTR_CHQNO,APT.REMARKS,APT.SITEWISE_PAYMENT_NO,"
							+"S.SITE_NAME,VD.VENDOR_NAME,AP.INVOICE_NUMBER,AP.INVOICE_AMOUNT,AP.PAYMENT_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO,"
							+"AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,"
							+"AP.VENDOR_ID,AP.SITE_ID,AP.INDENT_ENTRY_ID " 
							+"from ACC_PAYMENT_TRANSACTIONS APT,ACC_PAYMENT_DTLS APD,SITE S,VENDOR_DETAILS VD,"
							+"ACC_PAYMENT AP " 
							+"where  AP.PAYMENT_ID = APD.PAYMENT_ID " 
							+"and APD.PAYMENT_DETAILS_ID = APT.PAYMENT_DETAILS_ID " 
							+"and S.SITE_ID = AP.SITE_ID " 
							+"and VD.VENDOR_ID = AP.VENDOR_ID "
							+ "and APT.TYPE_OF_PAYMENT_SETTLEMENT = 'REGULAR'"
							+ "and APT.UPDATION = 'A'";
					
					if(!selectAll.equals("true")){
						if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
							query = query + " AND TRUNC(APT.CREATION_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
							//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
						} else if (StringUtils.isNotBlank(fromDate)) {
							query = query + " AND TRUNC(APT.CREATION_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
						} else if(StringUtils.isNotBlank(toDate)) {
							query = query + " AND TRUNC(APT.CREATION_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
						}

						if (StringUtils.isNotBlank(vendorId)){
							query = query + " and AP.VENDOR_ID = '"+vendorId+"'";
						}
						if (StringUtils.isNotBlank(invoiceNumber)){
							query = query + " and AP.INVOICE_NUMBER = '"+invoiceNumber+"'";
						}
						if (StringUtils.isNotBlank(poNumber)){
							query = query + " and AP.PO_NUMBER = '"+poNumber+"'";
						}
						if (StringUtils.isNotBlank(dropdown_SiteId)){
							query = query + " and AP.SITE_ID = '"+dropdown_SiteId+"'";
						}
					}
					// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
					/** take dept_id of login employee.
					 ** decide address hyd or banglore.
					 ** take list of sites in that address.
					 ** form a String using sites to pass into query.
					 **for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
					 *Site level also using same query site accountent will not available dept_id
					 */
					String dept_id = template.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
					String address = "";
					if(dept_id.contains("B")){
						address="BANGLORE";
					}
					else if(dept_id.contains("H")){
						address="HYD";
					}
					List<String> siteIdList = template.queryForList("select SITE_ID from SITE where ADDRESS = '"+address+"'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and AP.SITE_ID in("+groupOfSites+")";
					}
					/***************************************/

					query = query +" order by APT.CREATION_DATE asc";

			dbIndentDts = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> prods : dbIndentDts) {
				serialNo++;
				indentObj = new PaymentBean();
				indentObj.setIntSerialNo(serialNo);
				String paymentType = prods.get("PAYMENT_TYPE")==null ? "" : prods.get("PAYMENT_TYPE").toString();
				indentObj.setStrInvoiceNo(prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString());
				strInvoiceNumber=prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString();
				if(strInvoiceNumber.contains("&")){strInvoiceNumber=strInvoiceNumber.replace('&','@');}
				//indentObj.setStrNumber(strInvoiceNumber);
				indentObj.setStrPONo(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String podate=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				indentObj.setStrVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setStrVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setStrSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				invoiceAmount=Double.parseDouble(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString());
				poAmount=Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString());
				double paidAmount = Double.parseDouble(prods.get("PAID_AMOUNT")==null ? "" : prods.get("PAID_AMOUNT").toString());
				indentObj.setDoublePaidAmount(paidAmount);
				indentObj.setPaidAmount_WithCommas(format.format(paidAmount));
				//payment date in db is not updated by client. it is 3rd level approval date
				//that why we commented below line. later it will update in excel by client
				String  paymentDate = ""; //(prods.get("PAYMENT_DATE")==null ? "" : prods.get("PAYMENT_DATE").toString());
				String receviedDate=prods.get("INVOICE_RECEIVED_DATE")==null ? "" : prods.get("INVOICE_RECEIVED_DATE").toString();
				String remarks = prods.get("REMARKS")==null ? "" : prods.get("REMARKS").toString();
				indentObj.setStrRemarks(remarks);
				indentObj.setStrRemarksForView(remarks.replace("@@@", ","));
				indentObj.setIntPaymentId(Integer.parseInt(prods.get("PAYMENT_ID")==null ? "0" : prods.get("PAYMENT_ID").toString()));
				
				indentObj.setIntPaymentDetailsId(Integer.parseInt(prods.get("PAYMENT_DETAILS_ID")==null ? "0" : prods.get("PAYMENT_DETAILS_ID").toString()));
				indentObj.setIntSiteWisePaymentId(Integer.parseInt(prods.get("SITEWISE_PAYMENT_NO")==null ? "0" : prods.get("SITEWISE_PAYMENT_NO").toString()));
				indentObj.setStrSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setPaymentType(prods.get("PAYMENT_TYPE")==null ? "" : prods.get("PAYMENT_TYPE").toString());
				String paymentMode = prods.get("PAYMENT_MODE")==null ? "" : prods.get("PAYMENT_MODE").toString();
				indentObj.setPaymentMode(paymentMode);
				
				indentObj.setUtrChequeNo(prods.get("UTR_CHQNO")==null ? "" : prods.get("UTR_CHQNO").toString());
								
				indentObj.setDoubleInvoiceAmount(invoiceAmount);
				indentObj.setDoublePOTotalAmount(poAmount);
				indentObj.setStrInvoiceAmount(String.format("%.2f",invoiceAmount));
				indentObj.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
				indentObj.setStrPOAmount(String.format("%.2f",poAmount));
				indentObj.setPoAmount_WithCommas(format.format(poAmount));
				indentObj.setIntPaymentTransactionId(Integer.parseInt(prods.get("PAYMENT_TRANSACTIONS_ID")==null ? "" : prods.get("PAYMENT_TRANSACTIONS_ID").toString()));
				
				double creditTotalAmount = Double.valueOf(prods.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : prods.get("CREDIT_TOTAL_AMOUNT").toString());
				indentObj.setDoubleCreditTotalAmount(creditTotalAmount);
				indentObj.setCreditTotalAmount_WithCommas(format.format(creditTotalAmount));
				indentObj.setStrCreditNoteNumber(prods.get("CREDIT_NOTE_NUMBER")==null ? "" : prods.get("CREDIT_NOTE_NUMBER").toString());
				
				indentObj.setIntIndentEntryId(Integer.parseInt(prods.get("INDENT_ENTRY_ID")==null ? "0" : prods.get("INDENT_ENTRY_ID").toString()));
			    
				
				indentObj.setDoublePaymentDoneUpto(Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" : prods.get("PAYMENT_DONE_UPTO").toString()));
			    indentObj.setDoublePaymentRequestedUpto(Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" : prods.get("PAYMENT_REQ_UPTO").toString()));
			    

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try{

					/*Date receive_date = dt.parse(receviedDate);*/
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
					if(!paymentDate.equals("")){

						Date p_date = dt.parse(paymentDate);
						paymentDate = dt1.format(p_date);
					}
					if(!podate.equals("")){
						Date po_date = dt.parse(podate);
						podate = dt1.format(po_date);
					}
					if(!invoicedate.equals("")){
						Date invoice_date = dt.parse(invoicedate);
						invoicedate = dt1.format(invoice_date);
					}
					if(!receviedDate.equals("")){
						Date recevied_Date = dt.parse(receviedDate);
						receviedDate = dt1.format(recevied_Date);
					}
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
			
				indentObj.setStrInvoiceDate(invoicedate);
				indentObj.setStrPODate(podate);
				indentObj.setStrPaymentDate(paymentDate);
				indentObj.setStrInvoiceReceivedDate(receviedDate);
				//in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					indentObj.setStrInvoiceNo("");
					indentObj.setStrInvoiceDate("");
					indentObj.setStrReceiveDate("");
					indentObj.setStrInvoiceAmount("");
					indentObj.setStrCreditNoteNumber("");
					indentObj.setDoubleCreditTotalAmount(0);
				}
				list.add(indentObj);
			}

		}//if
		} catch (Exception ex) {
			ex.printStackTrace();
		  } finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;

}
	@Override
	public int updateRefNoInAccDeptTransaction(String strRefNo, int intPaymentTransactionId, String paymentMode,
			String paymentDate) {
		String query = "update ACC_PAYMENT_TRANSACTIONS set UTR_CHQNO = ?,PAYMENT_MODE = ?,PAYMENT_DATE = ?,UPDATION='I'  where PAYMENT_TRANSACTIONS_ID = ? ";

			int	count = jdbcTemplate.update(query, new Object[] {
					strRefNo, paymentMode, DateUtil.convertToJavaDateFormat(paymentDate), intPaymentTransactionId

				});
		return count;
	}
	@Override
	public int UpDatePaymentDetailsRemarks(String paymentDetailsId, String remarks) {
		String query="";
		int responseCount=0;
		
		query = "update ACC_PAYMENT_DTLS set REMARKS = '"+remarks+"'   where STATUS ='A' and PAYMENT_DETAILS_ID=?";
		responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});
		
		return responseCount;
	}
	@Override
	public int UpDatePaymentDetailsRemarksAndSetInactive(String paymentDetailsId, String comments) {
		String query="";
		int responseCount=0;
		
		query = "update ACC_PAYMENT_DTLS set REMARKS = REMARKS|| '@@@"+comments+"' , STATUS ='I'  where PAYMENT_DETAILS_ID=?";
		responseCount = jdbcTemplate.update(query, new Object[]  {paymentDetailsId});
		
		return responseCount;
	}
	@Override
	public int setInactiveAccPaymentAfterCheck(int intPaymentId) {
		String query="";
		int responseCount=0;
		String query1="";
		List<Map<String, Object>> dbIndentDts = null;
		query1 = "select INVOICE_AMOUNT,PAYMENT_DONE_UPTO from ACC_PAYMENT  where  PAYMENT_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query1, new Object[]{intPaymentId});
		for(Map<String, Object> prods : dbIndentDts) {
			double invoiceAmount = Math.round(Double.valueOf(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString()));
			double paymentDoneUpto = Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "" : prods.get("PAYMENT_DONE_UPTO").toString());
		
			if(paymentDoneUpto>=invoiceAmount){


				query = "update ACC_PAYMENT set STATUS = 'I'   where  PAYMENT_ID=?";
				responseCount = jdbcTemplate.update(query, new Object[]  {intPaymentId});
			}
		}
		
		return responseCount;
	}
	@Override
	public int updateRefNoInInvoiceHistory(String strRefNo, String paymentMode, int intPaymentTransactionId) {
		String query = "update ACC_INVOICE_HISTORY set REF_NO = ?,PAYMENT_MODE = ?  where PAYMENT_TRANSACTIONS_ID = ? ";

		int	count = jdbcTemplate.update(query, new Object[] {
				strRefNo, paymentMode, intPaymentTransactionId

			});
		return count;
	}
	@Override
	public String[] getPaymentApproverEmailId(String strUserId,String session_siteId) {
		List<String> emailList = new ArrayList<String>();
		List<Map<String, Object>> dbIndentDts = null;

		String query = "select SAMD.APPROVER_EMP_ID,SAMD.APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SAMD "
				+"WHERE  SAMD.EMP_ID='"+strUserId+"'  and SAMD.STATUS = 'A' AND SAMD.MODULE_TYPE='PAYMENT' and SAMD.SITE_ID='"+session_siteId+"'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbIndentDts) {
			String approverEmpId = prods.get("APPROVER_EMP_ID")==null ? "" : prods.get("APPROVER_EMP_ID").toString();
			String approverDeptId = prods.get("APPROVER_DEPT_ID")==null ? "" : prods.get("APPROVER_DEPT_ID").toString();

			if(StringUtils.isNotBlank(approverEmpId)&&!approverEmpId.equals("-")){
				String query1 = " select SED.EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where SED.EMP_ID ='"+approverEmpId+"'";
				String empEmail = jdbcTemplate.queryForObject(query1, String.class);
				if(StringUtils.isNotBlank(empEmail)){
				if(empEmail.contains(",")){
					String[] emailArr= empEmail.split(",");
					for(String email:emailArr){
						emailList.add(email);
					}
				}
				else{emailList.add(empEmail);}
				}
			}
			else if(StringUtils.isNotBlank(approverEmpId)&&approverEmpId.equals("-")&&StringUtils.isNotBlank(approverDeptId)&&approverDeptId.equals("997")){
				List<Map<String, Object>> dbIndentDts2 = null;
				String query2 = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.DEPT_ID = '997'";
				dbIndentDts2 = jdbcTemplate.queryForList(query, new Object[]{});
				for(Map<String, Object> prods2 : dbIndentDts2) {
					String empEmail = prods2.get("EMP_EMAIL")==null ? "" : prods2.get("EMP_EMAIL").toString();
					if(StringUtils.isNotBlank(empEmail)){
					if(empEmail.contains(",")){
						String[] emailArr= empEmail.split(",");
						for(String email:emailArr){
							emailList.add(email);
						}
					}
					else{emailList.add(empEmail);}
					}
				}
			}
		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	@Override
	public List<PaymentModesBean> getPaymentModes() {
		JdbcTemplate template = null;
		try {
			template  = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentModesBean> list = new ArrayList<PaymentModesBean>();
		String query = "select NAME,VALUE from ACC_PAYMENT_MODES where STATUS='A' order by ID asc";
		dbIndentDts = template.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbIndentDts) {
			PaymentModesBean objBean = new PaymentModesBean();
			objBean.setName(prods.get("NAME")==null ? "" : prods.get("NAME").toString());
			objBean.setValue(prods.get("VALUE")==null ? "" : prods.get("VALUE").toString());
			list.add(objBean);
		}
			
		return list;
	}
	@Override
	public VendorDetails getVendorAccountDetails(String vendorId) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select VENDOR_NAME,EMP_EMAIL,BANK_ACC_NUMBER,ACC_HOLDER_NAME,BANK_NAME,IFSC_CODE from VENDOR_DETAILS  where  VENDOR_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{vendorId});
		VendorDetails vd = new VendorDetails();
		for(Map<String, Object> prods : dbIndentDts) {
			vd.setVendor_name(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
			vd.setVendor_email(prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString());
			try {
				vd.setAcc_Number(Long.valueOf(prods.get("BANK_ACC_NUMBER")==null ? "0" : prods.get("BANK_ACC_NUMBER").toString()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			vd.setAcc_Holder_Name(prods.get("ACC_HOLDER_NAME")==null ? "" : prods.get("ACC_HOLDER_NAME").toString());
			vd.setBank_name(prods.get("BANK_NAME")==null ? "" : prods.get("BANK_NAME").toString());
			vd.setIfsc_Code(prods.get("IFSC_CODE")==null ? "" : prods.get("IFSC_CODE").toString());
			break;
		}
		
		return vd;
	}
	
	/* FOR MAIL */
	@Override
	public List<PaymentBean> getPaymentApprovalDetailsForMail(String siteId, String user_id,String groupOfPaymentDetailsId){


		String query = "";
		JdbcTemplate template = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean indentObj = null; 

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

		
				if (siteId.equals("998")) {
					query = "select DISTINCT(AP.INVOICE_NUMBER),VD.VENDOR_NAME,AP.INVOICE_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,AP.SITE_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,APD.REQ_AMOUNT,"
							+" APD.PAYMENT_REQ_DATE,APD.REMARKS,APD.PAYMENT_DETAILS_ID,APD.PAYMENT_ID,VD.VENDOR_ID,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.INDENT_ENTRY_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,APD.PAYMENT_TYPE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO " 
							+" ,(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT ,APD.ADJUST_AMOUNT_FROM_ADVANCE "
							+ " FROM VENDOR_DETAILS VD,ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP " 
							+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE on ( AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID=SPE.VENDOR_ID) "
							+"  LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A' "
							+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
							+ " WHERE AP.PAYMENT_ID=APD.PAYMENT_ID and APD.STATUS='A'  AND AP.VENDOR_ID=VD.VENDOR_ID "  
							+" AND APD.REQUEST_PENDING_EMP_ID = '"+user_id+"'"
							+" AND APD.PAYMENT_DETAILS_ID in ("+groupOfPaymentDetailsId+") "
							+" order by VD.VENDOR_NAME asc, APD.PAYMENT_REQ_DATE asc ";
				}
				else{
					query = "select DISTINCT(AP.INVOICE_NUMBER),VD.VENDOR_NAME,AP.INVOICE_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,AP.SITE_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,APD.REQ_AMOUNT,"
							+" APD.PAYMENT_REQ_DATE,APD.REMARKS,APD.PAYMENT_DETAILS_ID,APD.PAYMENT_ID,VD.VENDOR_ID,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.INDENT_ENTRY_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,APD.PAYMENT_TYPE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO " 
							+" ,(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT ,APD.ADJUST_AMOUNT_FROM_ADVANCE "
							+ " FROM VENDOR_DETAILS VD,ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP " 
							+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE on ( AP.PO_NUMBER = SPE.PO_NUMBER and AP.VENDOR_ID=SPE.VENDOR_ID) "
							+"  LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A' "
							+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
							+ " WHERE AP.PAYMENT_ID=APD.PAYMENT_ID and APD.STATUS='A' AND AP.SITE_ID='"+siteId+"' AND AP.VENDOR_ID=VD.VENDOR_ID "  
							+" AND APD.REQUEST_PENDING_EMP_ID = '"+user_id+"'"
							+" AND APD.PAYMENT_DETAILS_ID in ("+groupOfPaymentDetailsId+") "
							+" order by VD.VENDOR_NAME asc, APD.PAYMENT_REQ_DATE asc ";
					
				}
			dbIndentDts = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			List<String> invoiceIdList = new ArrayList<String>();
			List<String> poIdList = new ArrayList<String>();
			for(Map<String, Object> prods : dbIndentDts) {
				String vendorId = prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString();
				String vendorName = prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString();
				String invoiceId = prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString();
				invoiceAmount=Math.round(Double.parseDouble(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString()));
				String poId = prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString();
				poAmount=Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString());
				String reqAmount = prods.get("REQ_AMOUNT")==null ? "" : prods.get("REQ_AMOUNT").toString();
				
				serialNo++;
				indentObj = new PaymentBean();
				indentObj.setIntSerialNo(serialNo);
				indentObj.setStrInvoiceNo(prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString());
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				indentObj.setStrVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setStrVendorId(vendorId);
				
				poAmount=Math.round(Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString()));
				indentObj.setRequestedAmount(reqAmount);
				String  paymentDate =(prods.get("PAYMENT_REQ_DATE")==null ? "" : prods.get("PAYMENT_REQ_DATE").toString());
				String receviedDate=prods.get("INVOICE_RECEIVED_DATE")==null ? "" : prods.get("INVOICE_RECEIVED_DATE").toString();
				String remarks = prods.get("REMARKS")==null ? "" : prods.get("REMARKS").toString();
				indentObj.setStrRemarks(StringEscapeUtils.escapeHtml(remarks));
				indentObj.setStrRemarksForView(StringEscapeUtils.escapeHtml(remarks.replace("@@@", ",")));
				indentObj.setIntPaymentId(Integer.parseInt(prods.get("PAYMENT_ID")==null ? "" : prods.get("PAYMENT_ID").toString()));
				indentObj.setIntPaymentDetailsId(Integer.parseInt(prods.get("PAYMENT_DETAILS_ID")==null ? "" : prods.get("PAYMENT_DETAILS_ID").toString()));
				indentObj.setStrSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				indentObj.setStrPONo(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());				
				String  poDate =(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setDoubleInvoiceAmount(invoiceAmount);
				indentObj.setDoublePOTotalAmount(poAmount);
				indentObj.setDoubleCreditTotalAmount(Double.valueOf(prods.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : prods.get("CREDIT_TOTAL_AMOUNT").toString()));
				indentObj.setStrCreditNoteNumber(prods.get("CREDIT_NOTE_NUMBER")==null ? "" : prods.get("CREDIT_NOTE_NUMBER").toString());
				indentObj.setDoubleSecurityDeposit(Double.valueOf(prods.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : prods.get("SECURITY_DEPOSIT_AMOUNT").toString()));
			    indentObj.setIntIndentEntryId(Integer.parseInt(prods.get("INDENT_ENTRY_ID")==null ? "0" : prods.get("INDENT_ENTRY_ID").toString()));
			    indentObj.setDoublePOAdvancePayment(Double.valueOf(prods.get("REMAINING_AMOUNT")==null ? "0" : prods.get("REMAINING_AMOUNT").toString()));
			    indentObj.setDoubleAdjustAmountFromAdvance(Double.valueOf(prods.get("ADJUST_AMOUNT_FROM_ADVANCE")==null ? "0" : prods.get("ADJUST_AMOUNT_FROM_ADVANCE").toString()));
			    indentObj.setPaymentType(prods.get("PAYMENT_TYPE")==null ? "" :   prods.get("PAYMENT_TYPE").toString());
			    indentObj.setDoublePaymentDoneUpto(Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" : prods.get("PAYMENT_DONE_UPTO").toString()));
			    indentObj.setDoublePaymentRequestedUpto(Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" : prods.get("PAYMENT_REQ_UPTO").toString()));
			    
			

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String convertreceive_time = "";
				
				try{
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
					if(!receviedDate.equals("")){

					Date receive_date = dt.parse(receviedDate);
					receviedDate = dt1.format(receive_date);
					convertreceive_time = time1.format(receive_date);
					}
					if(!invoicedate.equals("")){

					Date invoice_date = dt.parse(invoicedate);
					invoicedate = dt1.format(invoice_date);
					}
					if(!poDate.equals("")){

						Date po_date = dt.parse(poDate);
						poDate = dt1.format(po_date);
						}
					
					if(!paymentDate.equals("")){

						Date payment_date = dt.parse(paymentDate);
						paymentDate = dt1.format(payment_date);
					}

					
					
					
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
			
				indentObj.setStrInvoiceDate(invoicedate);
				indentObj.setStrReceiveDate(receviedDate);
			//	indentObj.setTime(convertreceive_time);
				indentObj.setRequestedDate(paymentDate);
				indentObj.setStrPODate(poDate);
				list.add(indentObj);
			}

		
		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;

	
	}
	public boolean isInvoiceIdListContainsinvoiceId(List<PaymentBean> invoiceIdList,String invoiceId,String vendorId,String site_id){
		for(PaymentBean pb:invoiceIdList){
			if(pb.getStrInvoiceNo().equals(invoiceId)){
				if(pb.getStrVendorId().equals(vendorId)){
					if(pb.getStrSiteId().equals(site_id)){
						return true;
					}
				}	
			}
		}
		return false;

	}
	
	
	@Override
	public String[] getAllSiteLevelEmails(String siteId) {
		List<String> emailList = new ArrayList<String>();
		List<Map<String, Object>> dbIndentDts = null;

		String query = "select SED.EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_LOGIN SL WHERE SED.EMP_ID=SL.EMPLOYEEID AND SL.SITE_ID='"+siteId+"'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
				if(empEmail.contains(",")){
					String[] emailArr= empEmail.split(",");
					for(String email:emailArr){
						emailList.add(email);
					}
				}
				else{emailList.add(empEmail);}
			}

		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	
	public  List<Map<String, Object>> getAllSites() {
		JdbcTemplate template = null;
		List<Map<String, Object>> totalProductList = null;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select SITE_ID, SITE_NAME  from  SITE where STATUS = 'ACTIVE'";
			//System.out.println(sql);

			totalProductList = template.queryForList(sql, new Object[] {});

		} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}


	@Override
	public List<PaymentBean> getPaymentAccountsFirstLevelApprovalDetailsForMail(String strDeptId,
			String groupOfPaymentDetailsId) {
		
		//Because we changed dept id 997 to 997_B_1 & 997_H_1  , so we have to know which dept id is specifically.
		//String  accDeptId = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+strUserId+"'", String.class);
		
		
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<Map<String, Object>> paymentDtls = null;
		double invoiceAmount=0.0;
		double poAmount=0.0;
		int i = 0;

		String query = "select AP.INVOICE_NUMBER,AP.INVOICE_AMOUNT,AP.PAYMENT_DONE_UPTO,AP.INVOICE_DATE,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,VND.VENDOR_NAME,AVSD.SECURITY_DEPOSIT_AMOUNT ,AP.INVOICE_RECEIVED_DATE,AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.VENDOR_ID,AP.SITE_ID,AP.INDENT_ENTRY_ID,AP.PAYMENT_ID, "
					+ " APD.PAYMENT_REQ_DATE,APD.PAYMENT_DETAILS_ID,APD.SITEWISE_PAYMENT_NO,APD.PAYMENT_TYPE,S.SITE_NAME,AADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT,AADPP.PROCESS_INTIATE_AMOUNT,APD.REMARKS ,AADPP.ACCOUNTS_DEPT_PMT_PROSS_ID,AADPP.CREATION_DATE,AADPP.REQ_RECEIVE_FROM,AADPP.PAYMENT_DETAILS_ID,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO  "
					+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_MINUS_INTIATED_AMOUNT ,AAPP.REMAINING_AMOUNT ,AADPP.ADJUST_AMOUNT_FROM_ADVANCE,AADPP.ADJUST_INTIATE_AMOUNT  "
					+ " from VENDOR_DETAILS VND,SITE S ,ACC_PAYMENT_DTLS APD,ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP," 
					+ " ACC_PAYMENT AP "
					+ " LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = AP.VENDOR_ID AND AVSD.SITE_ID = AP.SITE_ID AND AVSD.STATUS = 'A'  " 
					+ " LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = AP.PO_NUMBER "
					+ " where APD.PAYMENT_ID = AP.PAYMENT_ID and  APD.PAYMENT_DETAILS_ID = AADPP.PAYMENT_DETAILS_ID "
					+ " and AP.VENDOR_ID = VND.VENDOR_ID " 
					+ " and AP.SITE_ID = S.SITE_ID and APD.REQUEST_PENDING_DEPT_ID = ? and AADPP.STATUS = 'A' "
					+" AND APD.PAYMENT_DETAILS_ID in ("+groupOfPaymentDetailsId+") "
		/*+ "and AADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT > AADPP.PROCESS_INTIATE_AMOUNT";*/
					+" order by VND.VENDOR_NAME asc, S.SITE_NAME asc, APD.PAYMENT_REQ_DATE asc, AADPP.CREATION_DATE asc  ";
		paymentDtls = jdbcTemplate.queryForList(query, new Object[] {strDeptId});
		int vendorGroupSerialNo = 0;
		String currentVendorId="-";
		String currentSiteId="-";
		List<PaymentBean> invoiceIdList = new ArrayList<PaymentBean>();
		List<String> poIdList = new ArrayList<String>();
		for(Map<String, Object> paymentReqs : paymentDtls) {
			double reqRecAmount = Double.valueOf(paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT")==null ? "0" :   paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT").toString());
			double procIntiateAmount = Double.valueOf(paymentReqs.get("PROCESS_INTIATE_AMOUNT")==null ? "0" :   paymentReqs.get("PROCESS_INTIATE_AMOUNT").toString());
			if(reqRecAmount<=procIntiateAmount){continue;}
			double adjustAmountFromAdvance = Double.valueOf(paymentReqs.get("ADJUST_AMOUNT_FROM_ADVANCE")==null ? "0" : paymentReqs.get("ADJUST_AMOUNT_FROM_ADVANCE").toString());
			double adjustIntiateAmount = Double.valueOf(paymentReqs.get("ADJUST_INTIATE_AMOUNT")==null ? "0" : paymentReqs.get("ADJUST_INTIATE_AMOUNT").toString());
			i = i+1;
			String invoiceId = paymentReqs.get("INVOICE_NUMBER")==null ? "" : paymentReqs.get("INVOICE_NUMBER").toString();
			String poId = paymentReqs.get("PO_NUMBER")==null ? "" : paymentReqs.get("PO_NUMBER").toString();
			invoiceAmount=Math.round(Double.parseDouble(paymentReqs.get("INVOICE_AMOUNT")==null ? "" : paymentReqs.get("INVOICE_AMOUNT").toString()));
			poAmount=Math.round(Double.parseDouble(paymentReqs.get("PO_AMOUNT")==null ? "" : paymentReqs.get("PO_AMOUNT").toString()));
			String vendorName = paymentReqs.get("VENDOR_NAME")==null ? "" : paymentReqs.get("VENDOR_NAME").toString();
			String vendorId = paymentReqs.get("VENDOR_ID")==null ? "" : paymentReqs.get("VENDOR_ID").toString();
			double reqAmount = reqRecAmount-procIntiateAmount;
			double paymentDoneUpto = Double.valueOf(paymentReqs.get("PAYMENT_DONE_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_DONE_UPTO").toString());
			double remainingAmountInAdvance = Double.valueOf(paymentReqs.get("REMAINING_AMOUNT")==null ? "0" : paymentReqs.get("REMAINING_AMOUNT").toString());
			String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
			String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
		
			PaymentBean objPaymentBean = new PaymentBean();


			objPaymentBean.setVendorGroupSerialNo(vendorGroupSerialNo);
			objPaymentBean.setStrInvoiceNo(invoiceId);
			objPaymentBean.setDoubleInvoiceAmount(invoiceAmount);
			objPaymentBean.setDoublePaymentDoneUpto(Double.valueOf(paymentReqs.get("PAYMENT_DONE_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_DONE_UPTO").toString()));
			objPaymentBean.setStrPONo(poId);
			String invoicedate = paymentReqs.get("INVOICE_DATE")==null ? "" :   paymentReqs.get("INVOICE_DATE").toString();
			objPaymentBean.setStrVendorName(vendorName);
			String receviedDate = paymentReqs.get("INVOICE_RECEIVED_DATE")==null ? "" :   paymentReqs.get("INVOICE_RECEIVED_DATE").toString();
			String paymentDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
			objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" :   paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
			objPaymentBean.setStrSiteName(site_name);
			objPaymentBean.setDoubleAmountToBeReleased(reqRecAmount-procIntiateAmount);
			
			objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
			String remarks = paymentReqs.get("REMARKS")==null ? "" :   paymentReqs.get("REMARKS").toString();
			objPaymentBean.setStrRemarks(remarks);
			objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
			objPaymentBean.setIntPaymentId(Integer.parseInt(paymentReqs.get("PAYMENT_ID")==null ? "" :   paymentReqs.get("PAYMENT_ID").toString()));
			objPaymentBean.setIntPaymentDetailsId(Integer.parseInt(paymentReqs.get("PAYMENT_DETAILS_ID")==null ? "" :   paymentReqs.get("PAYMENT_DETAILS_ID").toString()));
			objPaymentBean.setIntAccDeptPaymentProcessId(Integer.parseInt(paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID")==null ? "" :   paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID").toString()));
			objPaymentBean.setIntSerialNo(i);
			objPaymentBean.setDoublePOTotalAmount(poAmount);
			String  poDate =(paymentReqs.get("PO_DATE")==null ? "" : paymentReqs.get("PO_DATE").toString());
			objPaymentBean.setDoubleCreditTotalAmount(Double.valueOf(paymentReqs.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : paymentReqs.get("CREDIT_TOTAL_AMOUNT").toString()));
			objPaymentBean.setStrCreditNoteNumber(paymentReqs.get("CREDIT_NOTE_NUMBER")==null ? "" : paymentReqs.get("CREDIT_NOTE_NUMBER").toString());
			objPaymentBean.setDoubleSecurityDeposit(Double.valueOf(paymentReqs.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : paymentReqs.get("SECURITY_DEPOSIT_AMOUNT").toString()));
		    objPaymentBean.setStrVendorId(vendorId);
			objPaymentBean.setStrSiteId(site_id);
			String strIndentEntryId = paymentReqs.get("INDENT_ENTRY_ID")==null ? "0" : paymentReqs.get("INDENT_ENTRY_ID").toString();
			objPaymentBean.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			String paymentRequestReceivedDate = paymentReqs.get("CREATION_DATE")==null ? "" : paymentReqs.get("CREATION_DATE").toString();
			objPaymentBean.setDoublePOAdvancePayment(Double.valueOf(paymentReqs.get("REMAINING_MINUS_INTIATED_AMOUNT")==null ? "0" : paymentReqs.get("REMAINING_MINUS_INTIATED_AMOUNT").toString()));
			objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto);
		    objPaymentBean.setDoublePaymentRequestedUpto(Double.valueOf(paymentReqs.get("PAYMENT_REQ_UPTO")==null ? "0" : paymentReqs.get("PAYMENT_REQ_UPTO").toString()));
		    objPaymentBean.setDoubleAdjustAmountFromAdvance(adjustAmountFromAdvance-adjustIntiateAmount);
		    if(invoiceAmount==0.0){objPaymentBean.setDoubleBalanceAmount(poAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance);}
		    else {objPaymentBean.setDoubleBalanceAmount(invoiceAmount-paymentDoneUpto-Double.valueOf(reqAmount)-adjustAmountFromAdvance);}
		    objPaymentBean.setRequestReceiveFrom(paymentReqs.get("REQ_RECEIVE_FROM")==null ? "" : paymentReqs.get("REQ_RECEIVE_FROM").toString());
		    
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{
				
				SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");
				
				if(!receviedDate.equals("")){

					Date receive_date = dt.parse(receviedDate);
					receviedDate = dt1.format(receive_date);
					}
				if(!invoicedate.equals("")){

				Date invoice_date = dt.parse(invoicedate);
				invoicedate = dt1.format(invoice_date);
				}
				if(!poDate.equals("")){

					Date po_date = dt.parse(poDate);
					poDate = dt1.format(po_date);
					}
				if(!paymentDate.equals("")){

					Date payment_date = dt.parse(paymentDate);
					paymentDate = dt1.format(payment_date);
				}
				if(!paymentRequestReceivedDate.equals("")){

					Date paymentRequest_ReceivedDate = dt.parse(paymentRequestReceivedDate);
					paymentRequestReceivedDate = dt1.format(paymentRequest_ReceivedDate);
					paymentRequestReceivedDate +=" "+ time1.format(paymentRequest_ReceivedDate);
					}
				
			}
		
			catch(Exception e){
				e.printStackTrace();
			}
		
			objPaymentBean.setStrInvoiceReceivedDate(receviedDate);
			objPaymentBean.setStrInvoiceDate(invoicedate);
			objPaymentBean.setStrPODate(poDate);
			objPaymentBean.setStrPaymentReqDate(paymentDate);
			objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);			
			list.add(objPaymentBean);
		}
		return list;
	}


	@Override
	public String[] getEmailsOfEmpByDeptId(String pendingDeptId) {
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.DEPT_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{pendingDeptId});
		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
			if(empEmail.contains(",")){
				String[] emailArr= empEmail.split(",");
				for(String email:emailArr){
					emailList.add(email);
				}
			}
			else{emailList.add(empEmail);}
			}
		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	@Override
	public String[] getEmailsOfEmpByEmpId(String empId) {
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.EMP_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{empId});
		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
			if(empEmail.contains(",")){
				String[] emailArr= empEmail.split(",");
				for(String email:emailArr){
					emailList.add(email);
				}
			}
			else{emailList.add(empEmail);}
			}
		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	@Override
	public String[] getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(String user_id,String session_siteId) {
		
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		while(true){
			String query1 = "select SAMD.EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL SAMD "
					+" where SAMD.MODULE_TYPE = 'PAYMENT' and SAMD.STATUS='A' and SAMD.APPROVER_EMP_ID = ? and SAMD.SITE_ID='"+session_siteId+"'";
			
			String EmpId=null;
			try {
				EmpId = jdbcTemplate.queryForObject(query1, new Object[]{user_id},String.class);
			} catch (EmptyResultDataAccessException e) {
				break;
			}
			
			String query = "select SED1.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED1 where SED1.EMP_ID "
					+ " IN ('"+EmpId+"') ";
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
			for(Map<String, Object> prods : dbIndentDts) {
				String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
				if(StringUtils.isNotBlank(empEmail)){
					if(empEmail.contains(",")){
						String[] emailArr= empEmail.split(",");
						for(String email:emailArr){
							emailList.add(email);
						}
					}
					else{emailList.add(empEmail);}
				}
			}
			user_id = EmpId;
		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	@Override
	public String[] getEmailsOfEmployeesInLowerDeptOfThisEmployee(String user_id) {
		
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED1.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED1 where SED1.EMP_ID "
					 + " IN (select SAMD.EMP_ID from SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_APPROVER_MAPPING_DTL SAMD "
					 + " where SAMD.MODULE_TYPE = 'ACCOUNTS' and SAMD.STATUS='A' and SAMD.APPROVER_DEPT_ID = SED.DEPT_ID  and SED.EMP_ID = ? ) ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{user_id});
		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
			if(empEmail.contains(",")){
				String[] emailArr= empEmail.split(",");
				for(String email:emailArr){
					emailList.add(email);
				}
			}
			else{emailList.add(empEmail);}
			}
		}
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	@Override
	public String getEmpNameByEmpId(String empId) {
		String query = "select SED.EMP_NAME  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.EMP_ID = ? ";
		return jdbcTemplate.queryForObject(query, new Object[]{empId},String.class);
	}
	@Override
	public String getSiteNameBySiteId(String siteId) {
		String query = "select SITE_NAME  from SITE where SITE_ID = ? ";
		return jdbcTemplate.queryForObject(query, new Object[]{siteId},String.class);
	}

	/********************************************/
	/***** start of queries for already paid Payments ****************/
	
	@Override
	public int insertInACC_PAYMENT(PaymentBean objPaymentBean, int paymentId) {
		String query = "insert into ACC_PAYMENT(PAYMENT_ID,INVOICE_NUMBER,INVOICE_AMOUNT " +
				",SITE_ID,CREATED_DATE, PAYMENT_DONE_UPTO, DC_NUMBER, PAYMENT_REQ_UPTO, STATUS, " +
				" REMARKS, VENDOR_ID, PO_NUMBER,PO_AMOUNT,INVOICE_DATE,PO_DATE,DC_DATE,INVOICE_RECEIVED_DATE,INDENT_ENTRY_ID,CREDIT_TOTAL_AMOUNT,CREDIT_NOTE_NUMBER) " +
				"values(?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				int count = jdbcTemplate.update(query, new Object[] {
						paymentId,
						objPaymentBean.getStrInvoiceNo(),objPaymentBean.getDoubleInvoiceAmount(),
						objPaymentBean.getStrSiteId(),objPaymentBean.getDoubleAmountToBeReleased(),
						objPaymentBean.getStrDCNo(),"0",
						"A",//objPaymentBean.getDoubleInvoiceAmount()>objPaymentBean.getDoubleAmountToBeReleased()?"A":"I",
						objPaymentBean.getStrRemarks(),objPaymentBean.getStrVendorId(),
						objPaymentBean.getStrPONo(),objPaymentBean.getDoublePOTotalAmount(),
						DateUtil.convertToJavaDateFormat(objPaymentBean.getStrInvoiceDate()),
						DateUtil.convertToJavaDateFormat(objPaymentBean.getStrPODate()),
						DateUtil.convertToJavaDateFormat(objPaymentBean.getStrDCDate()),
						DateUtil.convertToJavaDateFormat(objPaymentBean.getStrInvoiceReceivedDate()),
						String.valueOf(objPaymentBean.getIntIndentEntryId()),
						objPaymentBean.getDoubleCreditTotalAmount(),
						objPaymentBean.getStrCreditNoteNumber(),
						});

		return count;
	}
	@Override
	public int updateACC_PAYMENT(PaymentBean objPaymentBean, int paymentId) {
		String query = "update ACC_PAYMENT set  UPDATED_DATE =sysdate , " +
				"PAYMENT_DONE_UPTO = PAYMENT_DONE_UPTO+?,REMARKS = ? where PAYMENT_ID = ? ";

			int	count = jdbcTemplate.update(query, new Object[] {

					objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getStrRemarks(), paymentId


				});
				return count;
	}
	
	@Override
	public int insertInACC_PAYMENT_DTLS(PaymentBean objPaymentBean, int paymentId, int paymentEntryDetailsSeqID) {
		String query = "insert into ACC_PAYMENT_DTLS(PAYMENT_DETAILS_ID,PAYMENT_ID,REQ_AMOUNT,REQUEST_PENDING_EMP_ID," +
			       "STATUS,PAYMENT_TYPE, "+
                "PAYMENT_REQ_DATE,REMARKS,SITEWISE_PAYMENT_NO,CREATION_DATE,ADJUST_AMOUNT_FROM_ADVANCE)values(?,?,?,?,?,?,?,?,?,sysdate,?)";

		int count = jdbcTemplate.update(query, new Object[] {
			paymentEntryDetailsSeqID,
			paymentId,objPaymentBean.getDoubleAmountToBeReleased(),
			"-",//objPaymentBean.getStrPendingEmpId(),
			"I",objPaymentBean.getPaymentType(),
			objPaymentBean.getStrPaymentReqDate(),objPaymentBean.getStrRemarks(),
			objPaymentBean.getIntSiteWisePaymentId(),
			objPaymentBean.getDoubleAdjustAmountFromAdvance()});
		return 0;
	}


	@Override
	public int insertInACC_ACCOUNTS_DEPT_PMT_PROSS(PaymentBean objPaymentBean,int accDeptPaymentProssSeqNo) {
		int count=0;
		String query = "insert into ACC_ACCOUNTS_DEPT_PMT_PROSS(ACCOUNTS_DEPT_PMT_PROSS_ID,ACCOUNT_DEPT_REQ_REC_AMOUNT,ALLOCATED_AMOUNT,PENDING_AMOUNT,PROCESS_INTIATE_AMOUNT,STATUS,PAYMENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,PAYMENT_DETAILS_ID,ADJUST_AMOUNT_FROM_ADVANCE,ADJUST_INTIATE_AMOUNT)"
					+" values (?,?,?,?,?,?,?,?,sysdate,?,?,?)";

		count = jdbcTemplate.update(query, new Object[] {
				accDeptPaymentProssSeqNo,
				
				objPaymentBean.getRequestedAmount(),objPaymentBean.getRequestedAmount(),0,objPaymentBean.getRequestedAmount(),
				
				"I",objPaymentBean.getStrSiteId(),objPaymentBean.getStrEmployeeId(),
				
				objPaymentBean.getIntPaymentDetailsId(),objPaymentBean.getDoubleAdjustAmountFromAdvance(),0
		});
		
		return count;
	}


	@Override
	public int insertInACC_TEMP_PAYMENT_TRANSACTIONS(PaymentBean objPaymentBean, int tempPaymentId) {

		String query = "INSERT INTO ACC_TEMP_PAYMENT_TRANSACTIONS(TEMP_PAYMENT_TRANSACTIONS_ID,ACCOUNTS_DEPT_PMT_PROSS_ID,PAYMENT_DETAILS_ID,REQ_AMOUNT,REQUEST_PENDING_EMP_ID,"+
		"STATUS,PAYMENT_TYPE,PAYMENT_REQ_DATE,PAYMENT_MODE,UTR_CHQNO,REMARKS,CREATION_DATE,SITEWISE_PAYMENT_NO,UPDATE_DATE,ADJUST_AMOUNT_FROM_ADVANCE)" +
		" values(? , ?, ?, ?, ?, ?, ?, ?,?,?,?,sysdate, ?,sysdate,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				tempPaymentId,objPaymentBean.getIntAccDeptPaymentProcessId(),
				objPaymentBean.getIntPaymentDetailsId(),
				objPaymentBean.getDoubleAmountToBeReleased(),
				"VND",//objPaymentBean.getStrPendingEmpId(),
				"I",
				objPaymentBean.getPaymentType(),
				DateUtil.convertToJavaDateFormat(objPaymentBean.getStrPaymentReqDate()),objPaymentBean.getPaymentMode(),
				objPaymentBean.getStrRefrenceNo(),objPaymentBean.getStrRemarks(),
				objPaymentBean.getIntSiteWisePaymentId(),
				objPaymentBean.getDoubleAdjustAmountFromAdvance()

				
		});




		return result;
	}


	@Override
	public int insertInACC_PAYMENT_TRANSACTIONS(PaymentBean objPaymentBean, int intPaymentTransactionId) {
		String query = "INSERT INTO ACC_PAYMENT_TRANSACTIONS(PAYMENT_TRANSACTIONS_ID,PAYMENT_DETAILS_ID,PAYMENT_ID,PAID_AMOUNT,"
				+ "PAYMENT_TYPE,PAYMENT_DATE,PAYMENT_MODE,UTR_CHQNO,REMARKS,CREATION_DATE,SITEWISE_PAYMENT_NO,TYPE_OF_PAYMENT_SETTLEMENT,UPDATION,PAYMENT_DONE_BY,TEMP_PAYMENT_TRANSACTIONS_ID)" +
				" values(?, ?, ?, ?, ?, ?,?,?,?,sysdate, ?,?,?,?,?)";

				int result = jdbcTemplate.update(query, new Object[] {
						intPaymentTransactionId,
						objPaymentBean.getIntPaymentDetailsId(),objPaymentBean.getIntPaymentId(),
						objPaymentBean.getDoubleAmountToBeReleased(),
						objPaymentBean.getPaymentType(),
						objPaymentBean.getStrPaymentReqDate(),
						objPaymentBean.getPaymentMode(),
						objPaymentBean.getUtrChequeNo(),objPaymentBean.getStrRemarks(),
						objPaymentBean.getIntSiteWisePaymentId(),
						"REGULAR",
						"I",
						"SITE",
						objPaymentBean.getIntTempPaymentTransactionId()

				});
		
		
				/********************************************************************/
				

		return result;
	}


	@Override
	public int insertInvoiceHistory(PaymentBean objPaymentBean, int intPaymentTransactionId) {
		String query = "INSERT INTO ACC_INVOICE_HISTORY(INVOICE_HISTORY_ID,INVOICE_NUMBER,INVOICE_AMOUNT,ENTRY_DATE,PAID_AMOUNT,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID)" +
				" values( INVOICE_HISTORY_SEQ.nextval,?,?,sysdate, ?,?,?,?)";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentBean.getStrInvoiceNo(),
						objPaymentBean.getDoubleInvoiceAmount(),
						objPaymentBean.getDoubleAmountToBeReleased(),
						objPaymentBean.getPaymentMode(),
						objPaymentBean.getUtrChequeNo(),
						intPaymentTransactionId

				});
		
					
		return result;
	}


	@Override
	public int insertPoHistory(PaymentBean objPaymentBean) {
		String query = "INSERT INTO ACC_PO_HISTORY(PO_HISTORY_ID,PO_NUMBER,PO_AMOUNT,ENTRY_DATE,PAID_AMOUNT)" +
				" values( PO_HISTORY_SEQ.nextval,?, ?,sysdate, ?)";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentBean.getStrPONo(),
						objPaymentBean.getDoublePOTotalAmount(),
						objPaymentBean.getDoubleAmountToBeReleased()
						

				});
		
						
	
return result;
	}


	@Override
	public int insertPaidAmountInAdvancePaymentPoTable(PaymentBean objPaymentBean) {
		String query1 = "insert into ACC_ADVANCE_PAYMENT_PO(PO_ADVANCE_PAYMENT_ID,PO_NUMBER ,PO_AMOUNT ,PAID_AMOUNT ,ADJUSTED_AMOUNT,REMAINING_AMOUNT ,INTIATED_AMOUNT ) "
				+ " values(PO_ADVANCE_PAYMENT_SEQ.nextval,?,?,?,?,?,?)";

					int	result = jdbcTemplate.update(query1, new Object[] {
							objPaymentBean.getStrPONo(),objPaymentBean.getDoublePOTotalAmount(),objPaymentBean.getDoubleAmountToBeReleased(),
								0.0,objPaymentBean.getDoubleAmountToBeReleased(),0.0
								
						});	
		return result;
	}
	@Override
	public int updateRemainingAdvanceAmountInAdvancePaymentPoTable(PaymentBean objPaymentBean) {
		int count=0;
		String query = "update ACC_ADVANCE_PAYMENT_PO set ADJUSTED_AMOUNT=ADJUSTED_AMOUNT+?, REMAINING_AMOUNT=REMAINING_AMOUNT-?  where PO_NUMBER=? ";
		count = jdbcTemplate.update(query, new Object[] {objPaymentBean.getDoubleAdjustAmountFromAdvance(),objPaymentBean.getDoubleAdjustAmountFromAdvance(),objPaymentBean.getStrPONo()});

		
		
		return count;
	}


	@Override
	public int getPaymentIdByInvoiceNo(PaymentBean objPaymentBean) {
		String query = "select PAYMENT_ID from ACC_PAYMENT where INVOICE_NUMBER=? and SITE_ID = ? and VENDOR_ID = ? ";	
		int paymentId;
		try {
			paymentId = jdbcTemplate.queryForInt(query,objPaymentBean.getStrInvoiceNo(),objPaymentBean.getStrSiteId(),objPaymentBean.getStrVendorId());
		} catch (DataAccessException e) {
			paymentId=0;
		}
		return paymentId;
		
	}

	@Override
	public int getIndentEntryIdByInvoiceNo(String invoiceNo, String siteId, String vendorId) {
		String query = "select INDENT_ENTRY_ID from INDENT_ENTRY where INVOICE_ID=? and SITE_ID = ? and VENDOR_ID = ? ";
		
		int paymentId;
		try {
			paymentId = jdbcTemplate.queryForInt(query,invoiceNo,siteId,vendorId);
		} catch (EmptyResultDataAccessException e) {
			paymentId=0;
		}
		return paymentId;
		
	}
	@Override
	public List<PaymentBean> getInvoiceDetailsForOldPayments(String siteId,Map<String,String> reqParamsMap) {

		String fromDate = reqParamsMap.get("fromDate");
		String toDate = reqParamsMap.get("toDate");
		String invoiceNumber = reqParamsMap.get("InvoiceNumber");
		String selectedVendorName = reqParamsMap.get("vendorName");
		String dropdown_SiteId = "";
		String SiteIdAndName = reqParamsMap.get("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)&&!SiteIdAndName.equals("@@")){
			dropdown_SiteId = SiteIdAndName.split("@@")[0];
		}
		double TotalInvoiceAmount = 0.0;
		double TotalTillReqAmount = 0.0;
		double TotalPaidAmount = 0.0;
		String query = "";
		String frontQuery = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbInvoiceList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if(siteId.equals("997")){

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE,IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ "FROM  VENDOR_DETAILS VD ,SITE S, INDENT_ENTRY IE  "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID AND IE.INDENT_TYPE='IN'   "
						+ "and ( AP.PAYMENT_DONE_UPTO IS NULL or AP.PAYMENT_DONE_UPTO =0 ) and ( AP.PAYMENT_REQ_UPTO IS NULL or AP.PAYMENT_REQ_UPTO =0 ) ";
						if(StringUtils.isNotBlank(dropdown_SiteId)){
							query = query+" and IE.SITE_ID='"+dropdown_SiteId+"'  ";
						}
						
						query = query+ " and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			}
			if(!siteId.equals("997")){

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE,IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ "FROM  VENDOR_DETAILS VD ,SITE S, INDENT_ENTRY IE  "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID AND IE.INDENT_TYPE='IN'   "
						+ "and ( AP.PAYMENT_DONE_UPTO IS NULL or AP.PAYMENT_DONE_UPTO =0 ) and ( AP.PAYMENT_REQ_UPTO IS NULL or AP.PAYMENT_REQ_UPTO =0 ) ";
						if(StringUtils.isNotBlank(siteId)){
							query = query+" and IE.SITE_ID='"+siteId+"'  ";
						}
						
						query = query+ " and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			}
			/** when Vendor Id is Site Id don't show in Payments **/
			List<String> siteIdList = template.queryForList("select SITE_ID from SITE", new Object[]{},String.class);
			if(siteIdList.size()>0){
				String groupOfSites = "";
				for(String site:siteIdList){
					groupOfSites +="'"+site+"',";
				}
				groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
				query = query + " and IE.VENDOR_ID not in ("+groupOfSites+")";
			}
			
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = query + "  AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = query + "  AND TRUNC(IE.INVOICE_DATE)  =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
			} else if(StringUtils.isNotBlank(toDate)) {
				query = query + "  AND TRUNC(IE.INVOICE_DATE)  <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
			}
			
			if(StringUtils.isNotBlank(selectedVendorName)){
				query = query+" and VD.VENDOR_NAME='"+selectedVendorName+"'";
			}
			if(StringUtils.isNotBlank(invoiceNumber)){
				query = query+" and IE.INVOICE_ID='"+invoiceNumber+"'";
			}
			
			query = query+" order by IE.INVOICE_DATE asc ";

			dbInvoiceList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> invoices : dbInvoiceList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String invoiceId = invoices.get("INVOICE_ID")==null ? "" : invoices.get("INVOICE_ID").toString();
				objPaymentBean.setStrInvoiceNo(invoiceId);
				objPaymentBean.setStrInvoiceNoInAP(invoices.get("INVOICE_NUMBER")==null ? "" : invoices.get("INVOICE_NUMBER").toString());
				objPaymentBean.setStrCreditNoteNumber(invoices.get("CREDIT_NOTE_NUMBER")==null ? "" : invoices.get("CREDIT_NOTE_NUMBER").toString());
				//String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String vendorName = invoices.get("VENDOR_NAME")==null ? "" : invoices.get("VENDOR_NAME").toString();
				objPaymentBean.setStrVendorName(vendorName);
				objPaymentBean.setStrVendorId(invoices.get("VENDOR_ID")==null ? "" : invoices.get("VENDOR_ID").toString());
				//String receviedDate=prods.get("RECEVED_DATE")==null ? "" : prods.get("RECEVED_DATE").toString();
				//String poDate = prods.get("PODATE")==null ? "" : prods.get("PODATE").toString();
				//String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString();
				//String strSiteId = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
				objPaymentBean.setStrRemarks(invoices.get("NOTE")==null ? "" : invoices.get("NOTE").toString());


				
				objPaymentBean.setIntPaymentId(Integer.valueOf(invoices.get("PAYMENT_ID")==null ? "0" : invoices.get("PAYMENT_ID").toString()));
				    
				String invoiceDate = invoices.get("INVOICE_DATE")==null ? "" : invoices.get("INVOICE_DATE").toString();
				String receivedDate = invoices.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : invoices.get("RECEIVED_OR_ISSUED_DATE").toString();
				
				objPaymentBean.setStrPODate(invoices.get("PODATE")==null ? "" : invoices.get("PODATE").toString());
				String site_id = invoices.get("SITE_ID")==null ? "" : invoices.get("SITE_ID").toString();
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(invoices.get("SITE_NAME")==null ? "" : invoices.get("SITE_NAME").toString());
				objPaymentBean.setStrPONo(invoices.get("PO_ID")==null ? "" : invoices.get("PO_ID").toString());
				double invoiceAmount = Math.round(Double.valueOf(invoices.get("TOTAL_AMOUNT")==null ? "0" : invoices.get("TOTAL_AMOUNT").toString()));
			    objPaymentBean.setDoubleInvoiceAmount(invoiceAmount);
			    TotalInvoiceAmount += invoiceAmount;
			    objPaymentBean.setDoublePOTotalAmount(Math.round(Double.valueOf(invoices.get("PO_TOTAL_AMOUNT")==null ? "0" : invoices.get("PO_TOTAL_AMOUNT").toString())));
			    double paymentDoneUpto = Double.valueOf(invoices.get("PAYMENT_DONE_UPTO")==null ? "0" : invoices.get("PAYMENT_DONE_UPTO").toString());
			    double paymentReqUpto = Double.valueOf(invoices.get("PAYMENT_REQ_UPTO")==null ? "0" : invoices.get("PAYMENT_REQ_UPTO").toString());
			    objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto);
			    objPaymentBean.setDoublePaymentRequestedUpto(paymentReqUpto);
			    TotalPaidAmount +=paymentDoneUpto;
			    TotalTillReqAmount += paymentReqUpto;
			    String strIndentEntryId = invoices.get("INDENT_ENTRY_ID")==null ? "0" : invoices.get("INDENT_ENTRY_ID").toString();
			    objPaymentBean.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			    objPaymentBean.setDoubleCreditTotalAmount(Double.valueOf(invoices.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : invoices.get("CREDIT_TOTAL_AMOUNT").toString()));
			    objPaymentBean.setDoubleSecurityDeposit(Double.valueOf(invoices.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : invoices.get("SECURITY_DEPOSIT_AMOUNT").toString()));
			    objPaymentBean.setDoublePOAdvancePayment(Double.valueOf(invoices.get("REMAINING_AMOUNT")==null ? "0" : invoices.get("REMAINING_AMOUNT").toString()));
			    
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				
			    try{
			    	
			    	if(!invoiceDate.equals("")){
			    		Date invoice_date = dt.parse(invoiceDate);
			    		invoiceDate = dt1.format(invoice_date);
			    		}
			    	if(!receivedDate.equals("")){
			    		Date received_date = dt.parse(receivedDate);
			    		receivedDate = dt1.format(received_date);
			    		}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrInvoiceDate(invoiceDate);
			    objPaymentBean.setStrInvoiceReceivedDate(receivedDate);
			    /*** display image ***/
			    for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
				String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
			    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					objPaymentBean.setHasImage(true);break;
					/*DataInputStream dis = new DataIn;putStream(new FileInputStream(f));
					byte[] barray = new byte[(int) f.length()];
					dis.readFully(barray); 
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					switch(i){
					case 0: objPaymentBean.setInvoiceImage0(base64Encoded);break;
					case 1: objPaymentBean.setInvoiceImage1(base64Encoded);break;
					case 2: objPaymentBean.setInvoiceImage2(base64Encoded);break;
					case 3: objPaymentBean.setInvoiceImage3(base64Encoded);break;
					}*/
					
					
				}
			    }
			    /**** END ***/
			    list.add(objPaymentBean);
			}
			reqParamsMap.put("TotalInvoiceAmount",String.valueOf(TotalInvoiceAmount));
			reqParamsMap.put("TotalTillReqAmount",String.valueOf(TotalTillReqAmount));
			reqParamsMap.put("TotalPaidAmount",String.valueOf(TotalPaidAmount));





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}
	/***** End queries for already paid Payments ****************/
	/********************************************/
	
	/*********************************************Contractor Bill Req For Payment start**********************************************************************/
	@Override
	public List<PaymentBean> getContractorBillReqDetails(String user_id,String site_id,Map<String,String> reqParamsMap) {

		String fromDate = reqParamsMap.get("fromDate");
		String toDate = reqParamsMap.get("toDate");
		String workOrderNo = reqParamsMap.get("workOrderNo");
		String contractorName = reqParamsMap.get("contractorName");
		
		String selectAll =reqParamsMap.get("all");
		/*String SiteIdAndName = reqParamsMap.get("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)&&!SiteIdAndName.equals("@@")){
			dropdown_SiteId = SiteIdAndName.split("@@")[0];
		}*/
		
		String query = "";
		String frontQuery = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbInvoiceList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			query = " SELECT ACP.CREATED_DATE,ACP.BILL_AMOUNT,ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.CNT_PAYMENT_ID,ACP.PAYMENT_REQ_AMOUNT,ACP.QS_WORKORDER_ISSUE_ID,"
					+" ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,ACP.REMARKS,ACP.SITE_ID,ACP.STATUS,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.REQUEST_PENDING_DEPT_ID,ACP.REQUEST_PENDING_EMP_ID,"
					+" ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,ACP.PAYMENT_REQ_DATE,ACP.CONTRACTOR_ID,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME,QCB.TEMP_BILL_ID,"
					/*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
					 + "(SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
					 + "WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
					 + "from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1 " 	
					 + "where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.QS_WORKORDER_ISSUE_ID = ACP.QS_WORKORDER_ISSUE_ID and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]') ) as QS_APR_REJ_REMARKS  "
					 + ""
					 
					+ " from QS_CONTRACTOR_BILL QCB,ACC_CNT_PAYMENT ACP "
					+" left outer join SUMADHURA_CONTRACTOR SC on SC.CONTRACTOR_ID=ACP.CONTRACTOR_ID"
					+" WHERE QCB.BILL_ID = ACP.BILL_ID  and QCB.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER"
					+" and ACP.REQUEST_PENDING_EMP_ID='"+user_id+"' AND ACP.STATUS='A'";
			
			if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){

				query = query+" AND TRUNC(CREATED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
			}else if (StringUtils.isNotBlank(fromDate)&& StringUtils.isBlank(toDate)) {
				query = query+" AND TRUNC(CREATED_DATE)  =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				
			}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = query+" AND TRUNC(CREATED_DATE)  <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
							
			}else{
				
			}
			if(StringUtils.isNotBlank(workOrderNo)){
				query = query+" and ACP.WO_NUMBER='"+workOrderNo+"'";
			}
			if(StringUtils.isNotBlank(contractorName)){
				query = query+" and SC.FIRST_NAME||' '||SC.LAST_NAME='"+contractorName+"'";
			}
			if(StringUtils.isNotBlank(site_id)){
				query = query+" and ACP.SITE_ID='"+site_id+"'";
			}
			
			if(selectAll.equals("RA")){
				query = query+" and ACP.PAYMENT_TYPE='RA'";
			}
			if(selectAll.equals("ADV")){
				query = query+" and ACP.PAYMENT_TYPE='ADV'";
			}
			
			
			dbInvoiceList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> invoices : dbInvoiceList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String billNumber = invoices.get("BILL_NUMBER")==null ? "" : invoices.get("BILL_NUMBER").toString();
				double billAmount = Double.valueOf(invoices.get("BILL_AMOUNT")==null ? "0" : invoices.get("BILL_AMOUNT").toString());
				String billDate = invoices.get("BILL_DATE")==null ? "" : invoices.get("BILL_DATE").toString();
				String paymentType = invoices.get("PAYMENT_TYPE")==null ? "" :   invoices.get("PAYMENT_TYPE").toString();
				String raBillNo,advBillNo,sdBillNo,nmrBillNo,raBillDate,advBillDate,sdBillDate,nmrBillDate;
				raBillNo=advBillNo=sdBillNo=nmrBillNo=raBillDate=advBillDate=sdBillDate=nmrBillDate = "";
				double raBillAmount,advBillAmount,sdBillAmount,nmrBillAmount;
				raBillAmount=advBillAmount=sdBillAmount=nmrBillAmount = 0;
				if(paymentType.equals("RA")){
					raBillNo = billNumber;
					raBillAmount = billAmount; 
					raBillDate = billDate;
				}
				if(paymentType.equals("ADV")){
					advBillNo = billNumber;
					advBillAmount = billAmount;  
					advBillDate = billDate;
				}
				if(paymentType.equals("SEC")){
					sdBillNo = billNumber;
					sdBillAmount = billAmount; 
					sdBillDate = billDate; 
				}
				if(paymentType.equals("NMR")){
					nmrBillNo = billNumber;
					nmrBillAmount = billAmount; 
					nmrBillDate = billDate;
				}
				objPaymentBean.setBillNumber(billNumber);
				objPaymentBean.setDoubleBillAmount(billAmount);
				objPaymentBean.setStrBillAmount(String.format("%.2f",billAmount));
				
				objPaymentBean.setRaBillNo(raBillNo);
				objPaymentBean.setDoubleRaBillAmount(raBillAmount);
				objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
				objPaymentBean.setStrAdvBillNo(advBillNo);
				objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
				objPaymentBean.setStrSdBillNo(sdBillNo);
				objPaymentBean.setStrNmrBillNo(nmrBillNo);
				objPaymentBean.setDoubleNmrBillAmount(nmrBillAmount);
				
				String creationDate = invoices.get("CREATED_DATE")==null ? "" : invoices.get("CREATED_DATE").toString();
				String remarks="";
				String qs_app_rej_remarks = invoices.get("QS_APR_REJ_REMARKS")==null ? "" :   invoices.get("QS_APR_REJ_REMARKS").toString();
				if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
				objPaymentBean.setStrRemarks(remarks);
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", "&#013;"));
				objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
				String woDate =(invoices.get("WO_DATE")==null ? "" : invoices.get("WO_DATE").toString());
				String paymentReqDate =(invoices.get("PAYMENT_REQ_DATE")==null ? "" : invoices.get("PAYMENT_REQ_DATE").toString());
				objPaymentBean.setStrSiteId(invoices.get("SITE_ID")==null ? "" : invoices.get("SITE_ID").toString());
				
				/*String invoiceId = invoices.get("INVOICE_ID")==null ? "" : invoices.get("INVOICE_ID").toString();
				objPaymentBean.setStrInvoiceNo(invoiceId);*/
				objPaymentBean.setIntCntPaymentId(Integer.parseInt(invoices.get("CNT_PAYMENT_ID")==null ? "" : invoices.get("CNT_PAYMENT_ID").toString()));
				objPaymentBean.setPaymentReqAmt(invoices.get("PAYMENT_REQ_AMOUNT")==null ? "" : invoices.get("PAYMENT_REQ_AMOUNT").toString());
				double woAmount = Double.parseDouble(invoices.get("WO_AMOUNT")==null ? "0" : invoices.get("WO_AMOUNT").toString());
				objPaymentBean.setDoubleWorkOrderAmount(woAmount);
				objPaymentBean.setStrWorkOrderAmount(String.format("%.2f",woAmount));
				objPaymentBean.setWorkOrderNo(invoices.get("WO_NUMBER")==null ? "" : invoices.get("WO_NUMBER").toString());
				objPaymentBean.setStatus(invoices.get("STATUS")==null ? "" : invoices.get("STATUS").toString());
				objPaymentBean.setStrContractorId(invoices.get("CONTRACTOR_ID")==null ? "" : invoices.get("CONTRACTOR_ID").toString());
				objPaymentBean.setStrContractorName(invoices.get("CONTRACTOR_NAME")==null ? "" : invoices.get("CONTRACTOR_NAME").toString());
				objPaymentBean.setStrBillId(invoices.get("BILL_ID")==null ? "" : invoices.get("BILL_ID").toString());
				objPaymentBean.setStrTempBillId(invoices.get("TEMP_BILL_ID")==null ? "" : invoices.get("TEMP_BILL_ID").toString());
				objPaymentBean.setPaymentType(paymentType);
				objPaymentBean.setReqDeptId(invoices.get("REQUEST_PENDING_DEPT_ID")==null ? "" : invoices.get("REQUEST_PENDING_DEPT_ID").toString());
				objPaymentBean.setReqEmpId(invoices.get("REQUEST_PENDING_EMP_ID")==null ? "" : invoices.get("REQUEST_PENDING_EMP_ID").toString());
				objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(invoices.get("SITEWISE_PAYMENT_NO")==null ? "0" : invoices.get("SITEWISE_PAYMENT_NO").toString()));
				objPaymentBean.setParticular(invoices.get("PARTICULAR")==null ? "" : invoices.get("PARTICULAR").toString());
				objPaymentBean.setQsWorkorderIssueId(invoices.get("QS_WORKORDER_ISSUE_ID")==null ? "" : invoices.get("QS_WORKORDER_ISSUE_ID").toString());
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				
			    try{
			    	
			    	if(!creationDate.equals("")){
			    		Date creation_Date = dt.parse(creationDate);
			    		creationDate = dt1.format(creation_Date);
			    		}
			    	if(!raBillDate.equals("")){
			    		Date received_date = dt.parse(raBillDate);
			    		raBillDate = dt1.format(received_date);
			    		}
			    	if(!advBillDate.equals("")){
			    		Date received_date = dt.parse(advBillDate);
			    		advBillDate = dt1.format(received_date);
			    		}
			    	if(!sdBillDate.equals("")){
			    		Date received_date = dt.parse(sdBillDate);
			    		sdBillDate = dt1.format(received_date);
			    		}
			    	if(!nmrBillDate.equals("")){
			    		Date received_date = dt.parse(nmrBillDate);
			    		nmrBillDate = dt1.format(received_date);
			    		}
			    	if(!billDate.equals("")){
			    		Date received_date = dt.parse(billDate);
			    		billDate = dt1.format(received_date);
			    		}
			    	if(!woDate.equals("")){
			    		Date received_date = dt.parse(woDate);
			    		woDate = dt1.format(received_date);
			    		}
			    	if(!paymentReqDate.equals("")){
			    		Date received_date = dt.parse(paymentReqDate);
			    		paymentReqDate = dt1.format(received_date);
			    		}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrInvoiceDate(creationDate);
			    objPaymentBean.setStrRaBillDate(raBillDate);
			    objPaymentBean.setStrAdvBillDate(advBillDate);
			    objPaymentBean.setStrSdBillDate(sdBillDate);
			    objPaymentBean.setStrNmrBillDate(nmrBillDate);
			    objPaymentBean.setStrBillDate(billDate);
			    objPaymentBean.setStrWorkOrderDate(woDate);
			    objPaymentBean.setPayentReqdate(paymentReqDate);
			    /*** display image ***/
			   /* for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
				String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
				File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					objPaymentBean.setHasImage(true);break;
					
				}
			    }*/
			    /**** END ***/
			    list.add(objPaymentBean);
			}
			/*reqParamsMap.put("TotalInvoiceAmount",String.valueOf(TotalInvoiceAmount));
			reqParamsMap.put("TotalTillReqAmount",String.valueOf(TotalTillReqAmount));
			reqParamsMap.put("TotalPaidAmount",String.valueOf(TotalPaidAmount));
*/




		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}

	/*********************************************Contractor Payment Data Save start*********************************************************************/
	@Override
	public int saveContractorBillPaymentProcessDtls(PaymentBean objPaymentBean,int acc_pross_pmt_Id,String pendingDeptId){

		int count = 0;
		
			String query = "insert into ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS(ACCOUNTS_DEPT_PMT_PROSS_ID,ACCOUNT_DEPT_REQ_REC_AMOUNT,ALLOCATED_AMOUNT,CREATION_DATE,"
						+" CNT_PAYMENT_ID,PAYMENT_REQ_SITE_ID,PENDING_AMOUNT,PROCESS_INTIATE_AMOUNT,REQ_RECEIVE_FROM,STATUS)values(?,?,?,sysdate,?,?,?,?,?,?)";

			count = jdbcTemplate.update(query, new Object[] {acc_pross_pmt_Id,objPaymentBean.getPaymentReqAmt(),"0",
					objPaymentBean.getIntCntPaymentId(),objPaymentBean.getStrSiteId(),objPaymentBean.getPaymentReqAmt(),"0",
					objPaymentBean.getUserId(),"A"
					});
			
			if(count>0){
				String sql = "update ACC_CNT_PAYMENT set REQUEST_PENDING_DEPT_ID=?,REQUEST_PENDING_EMP_ID=? where CNT_PAYMENT_ID = ? ";

				count = jdbcTemplate.update(sql, new Object[] {pendingDeptId,"-",objPaymentBean.getIntCntPaymentId()
				});

			}
			
			return count;
			
			
	}
	@Override
	public int insertCntPaymentApprRejDetail(PaymentBean bean, String operType) {
		int ACC_CNT_SITE_APPR_REJ_SEQ = jdbcTemplate.queryForObject("SELECT ACC_CNT_SITE_APPR_REJ_SEQ.NEXTVAL FROM DUAL", Integer.class);
		String query = "INSERT INTO  ACC_CNT_PMT_SITE_APPR_REJ_DTLS"
				+ "(PAYMENT_APROV_REJECT_SEQ,CNT_PAYMENT_ID,EMP_ID,STATUS,REMARKS,SITE_ID,CREATED_DATE,OPERATION_TYPE) "
				+ "VALUES(?,?,?,?,?,?,sysdate,?)";
		Object[] params = { ACC_CNT_SITE_APPR_REJ_SEQ, bean.getIntCntPaymentId(), bean.getUserId(), "A",
				bean.getStrComments(), bean.getStrSiteId(),operType  };
		int result = jdbcTemplate.update(query, params);
		return result;
	}
	
	/**************************************************Contractor Payment data Save End*************************************************************/
	/****************************************************Contractor check dept emp id******************************************************************/
	
	@Override
	public String getPendingContractorEmp(String strUserId){
		
		String pendingEmpId="";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  EMP_ID=?  and STATUS = 'A' AND MODULE_TYPE='LABOURPAYMENT'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strUserId}); 
		for(Map<String, Object> prods : dbIndentDts) {
			pendingEmpId = prods.get("APPROVER_EMP_ID")==null ? "" :   prods.get("APPROVER_EMP_ID").toString();
					
		}
		
		return pendingEmpId;
	}
	
	public String getPendingContractorDeptId(String strUserId,String session_site_id){
		String pendingDeptId="";
		String query = "select APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  EMP_ID='"+strUserId+"'  and STATUS = 'A' AND MODULE_TYPE='LABOURPAYMENT' AND SITE_ID='"+session_site_id+"'";
		pendingDeptId = jdbcTemplate.queryForObject(query, String.class); 
		
		return pendingDeptId;
	}
	
	public int updateContractorBillPayment(PaymentBean objPaymentBean,String pendingEmpId){
	int count=0;
	String sql = "update ACC_CNT_PAYMENT set  REQUEST_PENDING_EMP_ID=? where CNT_PAYMENT_ID = ? and STATUS =? ";

	count = jdbcTemplate.update(sql, new Object[] {pendingEmpId,objPaymentBean.getIntCntPaymentId(),"A"
	});
	return count;
	}
	
	@Override
	public String[] getEmailsOfEmpByEmpIdAndEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevel(String user_id,String siteId) {
		
		List<String> emailList = new ArrayList<String>();
		
		/********************************************************/
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.EMP_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{user_id});
		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
			if(empEmail.contains(",")){
				String[] emailArr= empEmail.split(",");
				for(String email:emailArr){
					emailList.add(email);
				}
			}
			else{emailList.add(empEmail);}
			}
		}
	
		/*******************************************************/
		
		List<Map<String, Object>> dbIndentDts1 = null;
		while(true){
			String query1 = "select SAMD.EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL SAMD "
					+" where SAMD.MODULE_TYPE = 'PAYMENT' and SAMD.STATUS='A' and SAMD.APPROVER_EMP_ID = ? and SAMD.SITE_ID='"+siteId+"'";
			
			String EmpId=null;
			try {
				EmpId = jdbcTemplate.queryForObject(query1, new Object[]{user_id},String.class);
			} catch (EmptyResultDataAccessException e) {
				break;
			} catch (IncorrectResultSizeDataAccessException e){
				getEmailsOfEmployeesInLowerDeptOfThisEmployeeInSiteLevel(emailList,user_id,siteId);
				break;
			}
			
			String query2 = "select SED1.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED1 where SED1.EMP_ID "
					+ " IN ('"+EmpId+"') ";
			dbIndentDts1 = jdbcTemplate.queryForList(query2, new Object[]{});
			for(Map<String, Object> prods : dbIndentDts1) {
				String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
				if(StringUtils.isNotBlank(empEmail)){
					if(empEmail.contains(",")){
						String[] emailArr= empEmail.split(",");
						for(String email:emailArr){
							emailList.add(email);
						}
					}
					else{emailList.add(empEmail);}
				}
			}
			user_id = EmpId;
		}
		/*****************************************************/

		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	
	}
	
	@Override
	public String[] getEmailsOfEmployeesInLowerDeptOfThisEmployeeInSiteLevel(List<String> emailList,String user_id,String siteId) {
		
		
		
		List<Map<String, Object>> dbIndentDts = null;
		
			String query1 = "select SAMD.EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL SAMD "
					+" where SAMD.MODULE_TYPE = 'PAYMENT' and SAMD.STATUS='A' and SAMD.APPROVER_EMP_ID = ? and SAMD.SITE_ID='"+siteId+"'";
			
			List<String> listOfEmpId=null;
			try {
				listOfEmpId = jdbcTemplate.queryForList(query1, new Object[]{user_id},String.class);
			} catch (EmptyResultDataAccessException e) {
				e.printStackTrace();
			}
			for(String EmpId : listOfEmpId){
				String query = "select SED1.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED1 where SED1.EMP_ID "
						+ " IN ('"+EmpId+"') ";
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				for(Map<String, Object> prods : dbIndentDts) {
					String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
					if(StringUtils.isNotBlank(empEmail)){
						if(empEmail.contains(",")){
							String[] emailArr= empEmail.split(",");
							for(String email:emailArr){
								emailList.add(email);
							}
						}
						else{emailList.add(empEmail);}
					}
				}
			}
		
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}
	
	@Override
	public List<PaymentBean> getLocalPurchaseInvoiceDetails(String siteId,Map<String,String> reqParamsMap, String selectAll) {

		String fromDate = reqParamsMap.get("fromDate");
		String toDate = reqParamsMap.get("toDate");
		String invoiceNumber = reqParamsMap.get("InvoiceNumber");
		String selectedVendorName = reqParamsMap.get("vendorName");
		String dropdown_SiteId = "";
		String SiteIdAndName = reqParamsMap.get("siteIdAndName");
		if(StringUtils.isNotBlank(SiteIdAndName)&&!SiteIdAndName.equals("@@")){
			dropdown_SiteId = SiteIdAndName.split("@@")[0];
		}
		double TotalInvoiceAmount = 0.0;
		double TotalTillReqAmount = 0.0;
		double TotalPaidAmount = 0.0;
		String query = "";
		String frontQuery = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		List<Map<String, Object>> dbInvoiceList = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			/*if(siteId.equals("997")){

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE,IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ "FROM  VENDOR_DETAILS VD ,SITE S, INDENT_ENTRY IE  "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on (AP.INVOICE_NUMBER = IE.INVOICE_ID) and AP.VENDOR_ID = IE.VENDOR_ID and AP.SITE_ID = IE.SITE_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID AND IE.INDENT_TYPE='IN'   "
						+ "and ( AP.PAYMENT_DONE_UPTO IS NULL or AP.PAYMENT_DONE_UPTO =0 ) and ( AP.PAYMENT_REQ_UPTO IS NULL or AP.PAYMENT_REQ_UPTO =0 ) ";
						if(StringUtils.isNotBlank(dropdown_SiteId)){
							query = query+" and IE.SITE_ID='"+dropdown_SiteId+"'  ";
						}
						
						query = query+ " and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			}
			if(!siteId.equals("997")){*/

				query = " SELECT DISTINCT AP.PAYMENT_ID,AP.INVOICE_NUMBER,SCN.CREDIT_NOTE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,IE.INVOICE_ID,"
						+ "IE.INDENT_ENTRY_ID,S.SITE_NAME,S.SITE_ID,IE.RECEIVED_OR_ISSUED_DATE, IE.INVOICE_DATE, VD.VENDOR_NAME,VD.VENDOR_ID,AVSD.SECURITY_DEPOSIT_AMOUNT,IE.PO_ID,"
						+ "IE.PODATE ,AP.PAYMENT_DONE_UPTO,AP.PAYMENT_REQ_UPTO,IE.TOTAL_AMOUNT,IE.DC_NUMBER,IE.NOTE, "
						+ "(select sum(SPED.TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.PO_NUMBER = IE.PO_ID ) as PO_TOTAL_AMOUNT   "
						+ ",(AAPP.REMAINING_AMOUNT-AAPP.INTIATED_AMOUNT) as REMAINING_AMOUNT "
						+ ",(AAPP.PO_AMOUNT-AAPP.PAID_AMOUNT) as PAY_BALANCE_IN_PO "
						+ ",(select sum(INNER_AP.PAYMENT_DONE_UPTO) from ACC_PAYMENT INNER_AP where INNER_AP.PO_NUMBER = AP.PO_NUMBER) as PAYMENT_DONE_ON_MULTI_INV "
						+ ",AAPP.ADJUSTED_AMOUNT as ADJUSTED_AMOUNT_FROM_PO "
						+ "FROM  VENDOR_DETAILS VD , SITE S ,INDENT_ENTRY IE "
						+ "LEFT OUTER JOIN ACC_PAYMENT AP on IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN SUMADHURA_CREDIT_NOTE SCN on SCN.INDENT_ENTRY_NUMBER = IE.INDENT_ENTRY_ID "
						+ "LEFT OUTER JOIN ACC_VENDOR_SECURITY_DEPOSIT AVSD on AVSD.VENDOR_ID = IE.VENDOR_ID AND AVSD.SITE_ID = IE.SITE_ID AND AVSD.STATUS = 'A' "
						+ "LEFT OUTER JOIN ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER = IE.PO_ID "
						+ "WHERE   IE.VENDOR_ID=VD.VENDOR_ID AND IE.SITE_ID = S.SITE_ID  AND IE.INDENT_TYPE='IN' AND  IE.SITE_ID='"+siteId+"'  "
						+ "and ( AP.PAYMENT_DONE_UPTO IS NULL or AP.PAYMENT_DONE_UPTO =0 ) and ( AP.PAYMENT_REQ_UPTO IS NULL or AP.PAYMENT_REQ_UPTO =0 ) "
						+ "and (AP.STATUS='A' or AP.STATUS IS NULL)  ";

			/*}*/
			/** when Vendor Id is Site Id don't show in Payments **/
			List<String> siteIdList = template.queryForList("select SITE_ID from SITE", new Object[]{},String.class);
			if(siteIdList.size()>0){
				String groupOfSites = "";
				for(String site:siteIdList){
					groupOfSites +="'"+site+"',";
				}
				groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
				query = query + " and IE.VENDOR_ID not in ("+groupOfSites+")";
			}
			
			if(!selectAll.equals("true")){
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query + "  AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query + "  AND TRUNC(IE.INVOICE_DATE)  =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query + "  AND TRUNC(IE.INVOICE_DATE)  <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}

				if(StringUtils.isNotBlank(selectedVendorName)){
					query = query+" and VD.VENDOR_NAME='"+selectedVendorName+"'";
				}
				if(StringUtils.isNotBlank(invoiceNumber)){
					query = query+" and IE.INVOICE_ID='"+invoiceNumber+"'";
				}
			}
			query = query+" order by IE.INVOICE_DATE asc ";

			dbInvoiceList = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> invoices : dbInvoiceList) {
				serialNo++;
				objPaymentBean = new PaymentBean();
				objPaymentBean.setIntSerialNo(serialNo);
				String invoiceId = invoices.get("INVOICE_ID")==null ? "" : invoices.get("INVOICE_ID").toString();
				objPaymentBean.setStrInvoiceNo(invoiceId);
				objPaymentBean.setStrInvoiceNoInAP(invoices.get("INVOICE_NUMBER")==null ? "" : invoices.get("INVOICE_NUMBER").toString());
				objPaymentBean.setStrCreditNoteNumber(invoices.get("CREDIT_NOTE_NUMBER")==null ? "" : invoices.get("CREDIT_NOTE_NUMBER").toString());
				//String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String vendorName = invoices.get("VENDOR_NAME")==null ? "" : invoices.get("VENDOR_NAME").toString();
				objPaymentBean.setStrVendorName(vendorName);
				objPaymentBean.setStrVendorId(invoices.get("VENDOR_ID")==null ? "" : invoices.get("VENDOR_ID").toString());
				//String receviedDate=prods.get("RECEVED_DATE")==null ? "" : prods.get("RECEVED_DATE").toString();
				//String poDate = prods.get("PODATE")==null ? "" : prods.get("PODATE").toString();
				//String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString();
				//String strSiteId = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
				objPaymentBean.setStrRemarks(invoices.get("NOTE")==null ? "" : invoices.get("NOTE").toString());


				
				objPaymentBean.setIntPaymentId(Integer.valueOf(invoices.get("PAYMENT_ID")==null ? "0" : invoices.get("PAYMENT_ID").toString()));
				    
				String invoiceDate = invoices.get("INVOICE_DATE")==null ? "" : invoices.get("INVOICE_DATE").toString();
				String receivedDate = invoices.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : invoices.get("RECEIVED_OR_ISSUED_DATE").toString();
				
				objPaymentBean.setStrPODate(invoices.get("PODATE")==null ? "" : invoices.get("PODATE").toString());
				String site_id = invoices.get("SITE_ID")==null ? "" : invoices.get("SITE_ID").toString();
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(invoices.get("SITE_NAME")==null ? "" : invoices.get("SITE_NAME").toString());
				objPaymentBean.setStrPONo(invoices.get("PO_ID")==null ? "" : invoices.get("PO_ID").toString());
				double invoiceAmount = Math.round(Double.valueOf(invoices.get("TOTAL_AMOUNT")==null ? "0" : invoices.get("TOTAL_AMOUNT").toString()));
			    objPaymentBean.setDoubleInvoiceAmount(invoiceAmount);
			    objPaymentBean.setInvoiceAmount_WithCommas(format.format(invoiceAmount));
			    TotalInvoiceAmount += invoiceAmount;
			    objPaymentBean.setDoublePOTotalAmount(Math.round(Double.valueOf(invoices.get("PO_TOTAL_AMOUNT")==null ? "0" : invoices.get("PO_TOTAL_AMOUNT").toString())));
			    double paymentDoneUpto = Double.valueOf(invoices.get("PAYMENT_DONE_UPTO")==null ? "0" : invoices.get("PAYMENT_DONE_UPTO").toString());
			    double paymentReqUpto = Double.valueOf(invoices.get("PAYMENT_REQ_UPTO")==null ? "0" : invoices.get("PAYMENT_REQ_UPTO").toString());
			    objPaymentBean.setDoublePaymentDoneUpto(paymentDoneUpto);
			    objPaymentBean.setDoublePaymentRequestedUpto(paymentReqUpto);
			    objPaymentBean.setPaymentDoneUpto_WithCommas(format.format(paymentDoneUpto));
			    objPaymentBean.setPaymentRequestedUpto_WithCommas(format.format(paymentReqUpto));
			    TotalPaidAmount +=paymentDoneUpto;
			    TotalTillReqAmount += paymentReqUpto;
			    String strIndentEntryId = invoices.get("INDENT_ENTRY_ID")==null ? "0" : invoices.get("INDENT_ENTRY_ID").toString();
			    objPaymentBean.setIntIndentEntryId(Integer.parseInt(strIndentEntryId));
			    double creditTotalAmount = Double.valueOf(invoices.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : invoices.get("CREDIT_TOTAL_AMOUNT").toString());
			    objPaymentBean.setDoubleCreditTotalAmount(creditTotalAmount);
			    objPaymentBean.setCreditTotalAmount_WithCommas(format.format(creditTotalAmount));
			    objPaymentBean.setVendorInvoiceTotalAmount_WithCommas(format.format(invoiceAmount+creditTotalAmount));
				double securityDepositAmount =  Double.valueOf(invoices.get("SECURITY_DEPOSIT_AMOUNT")==null ? "0" : invoices.get("SECURITY_DEPOSIT_AMOUNT").toString());
			    objPaymentBean.setDoubleSecurityDeposit(securityDepositAmount);
			    objPaymentBean.setSecurityDeposit_WithCommas(format.format(securityDepositAmount));
			    double poAdvancePayment =  Double.valueOf(invoices.get("REMAINING_AMOUNT")==null ? "0" : invoices.get("REMAINING_AMOUNT").toString());
			    objPaymentBean.setDoublePOAdvancePayment(poAdvancePayment);
			    objPaymentBean.setPoAdvancePayment_WithCommas(format.format(poAdvancePayment));
			    String payBalanceInPo = invoices.get("PAY_BALANCE_IN_PO")==null ? "NO_ADVANCE" : invoices.get("PAY_BALANCE_IN_PO").toString();
			    objPaymentBean.setPayBalanceInPo(payBalanceInPo);
			    String paymentDoneOnMultipleInvoices = invoices.get("PAYMENT_DONE_ON_MULTI_INV")==null ? "0" : invoices.get("PAYMENT_DONE_ON_MULTI_INV").toString();
			    objPaymentBean.setPaymentDoneOnMultipleInvoices(paymentDoneOnMultipleInvoices);
			    String adjustedAmountFromPo = invoices.get("ADJUSTED_AMOUNT_FROM_PO")==null ? "0" : invoices.get("ADJUSTED_AMOUNT_FROM_PO").toString();
			    objPaymentBean.setAdjustedAmountFromPo(adjustedAmountFromPo);
			    
			    
			    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				
			    try{
			    	
			    	if(!invoiceDate.equals("")){
			    		Date invoice_date = dt.parse(invoiceDate);
			    		invoiceDate = dt1.format(invoice_date);
			    		}
			    	if(!receivedDate.equals("")){
			    		Date received_date = dt.parse(receivedDate);
			    		receivedDate = dt1.format(received_date);
			    		}
					
				}catch(Exception e){
					e.printStackTrace();
				}
			    objPaymentBean.setStrInvoiceDate(invoiceDate);
			    objPaymentBean.setStrInvoiceReceivedDate(receivedDate);
			    /*** display image ***/
			    for(int i=0;i<8;i++){
			    String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
				String imgname = vendorName+"_"+invoiceId+"_"+strIndentEntryId;
				imgname = imgname.replace("/","$$");
			    File f = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".jpg");
			    File f1 = new File(rootFilePath+site_id+"/"+imgname+"_Part"+i+".pdf");

				if(f.exists()||f1.exists()){
					
					objPaymentBean.setHasImage(true);break;
					/*DataInputStream dis = new DataIn;putStream(new FileInputStream(f));
					byte[] barray = new byte[(int) f.length()];
					dis.readFully(barray); 
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					switch(i){
					case 0: objPaymentBean.setInvoiceImage0(base64Encoded);break;
					case 1: objPaymentBean.setInvoiceImage1(base64Encoded);break;
					case 2: objPaymentBean.setInvoiceImage2(base64Encoded);break;
					case 3: objPaymentBean.setInvoiceImage3(base64Encoded);break;
					}*/
					
					
				}
			    }
			    /**** END ***/
			    list.add(objPaymentBean);
			}
			reqParamsMap.put("TotalInvoiceAmount",format.format(TotalInvoiceAmount));//String.valueOf(TotalInvoiceAmount));
			reqParamsMap.put("TotalTillReqAmount",format.format(TotalTillReqAmount));
			reqParamsMap.put("TotalPaidAmount",format.format(TotalPaidAmount));





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			template = null;
			
		}
		return list;
	}


	@Override
	public Map<String, PaymentBean> getInvoiceNoAndPoNoByPaymentDtlsId(List<String> alreadyApprovedList) {
		Map<String, PaymentBean> map = new HashMap<String, PaymentBean>();
		String groupOfPaymentDtlsId = "";
		for(String id:alreadyApprovedList){
			groupOfPaymentDtlsId +="'"+id+"',";
		}
		groupOfPaymentDtlsId = groupOfPaymentDtlsId.substring(0,groupOfPaymentDtlsId.length()-1);
		List<Map<String, Object>> list = null;
		String sql = "select APD.PAYMENT_DETAILS_ID,AP.INVOICE_NUMBER,AP.PO_NUMBER from ACC_PAYMENT_DTLS APD,ACC_PAYMENT AP where AP.PAYMENT_ID=APD.PAYMENT_ID and APD.PAYMENT_DETAILS_ID in ("+groupOfPaymentDtlsId+")";
		list = jdbcTemplate.queryForList(sql, new Object[]{});
		for(Map<String, Object> invoices : list) {
			PaymentBean objPaymentBean = new PaymentBean();
			String invoiceNo = invoices.get("INVOICE_NUMBER")==null ? "" : invoices.get("INVOICE_NUMBER").toString();
			String poNo = invoices.get("PO_NUMBER")==null ? "" : invoices.get("PO_NUMBER").toString();
			String paymentDetailsId = invoices.get("PAYMENT_DETAILS_ID")==null ? "" : invoices.get("PAYMENT_DETAILS_ID").toString();
			objPaymentBean.setStrInvoiceNo(invoiceNo);
			objPaymentBean.setStrPONo(poNo);
			map.put(paymentDetailsId, objPaymentBean);
		}
		
		
		return map;
	}
	@Override
	public String checkIsTaxInvoiceIsSubmitedOrNot(String strVendorId, String indentEntryId, String invoiceNo, String siteId) {
		String response="";
		String query="SELECT TAXINVOICE_STATUS FROM ACC_TAXINVOICE_DETAILS WHERE INDENT_ENTRY_ID='"+indentEntryId+"' AND INVOICE_NO=? AND SITE_ID=?";
		if(indentEntryId.length()==0){
			//query="SELECT TAXINVOICE_STATUS FROM ACC_TAXINVOICE_DETAILS WHERE   INVOICE_NO=? AND SITE_ID=?";
			throw new RuntimeException("Indent Entry Id Not Found");
		}
		 response=jdbcTemplate.queryForObject(query,new Object[] {invoiceNo,siteId},String.class);
		return response;
	}


	@Override
	public List<PurchaseTaxReportBean> getPurchaseTaxReport(Map<String, String> requestMap) {
		String invoiceFromDate = requestMap.get("invoiceFromDate");
		String invoiceToDate = requestMap.get("invoiceToDate");
		String grnFromDate = requestMap.get("grnFromDate");
		String grnToDate = requestMap.get("grnToDate");
		String vendorId = requestMap.get("vendorId");
		String siteIds = requestMap.get("siteIds");
		
		String query = "select S.SITE_NAME,IE.GRN_NO,IE.RECEIVED_OR_ISSUED_DATE,IE.PO_ID,IE.PODATE,IE.STATE,SPL.TAX,VD.VENDOR_NAME,VD.ADDRESS,VD.STATE as VENDOR_STATE,VD.GSIN_NUMBER,IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME,SPL.HSN_CODE,IED.MEASUR_MNT_NAME,SPL.RECEVED_QTY,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,IE.INVOICE_ID,IE.INVOICE_DATE,SPL.BASIC_AMOUNT,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.TAX_AMOUNT,SPL.TOTAL_AMOUNT "   
				+ "from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,SUMADHURA_PRICE_LIST SPL, SITE S,VENDOR_DETAILS VD "   
				+ "where IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID and S.SITE_ID=IE.SITE_ID and IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID and VD.VENDOR_ID=IE.VENDOR_ID ";
		if (StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)) {
			query = query + "  AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+invoiceFromDate+"','dd-MM-yy') AND TO_DATE('"+invoiceToDate+"','dd-MM-yy')";
		} else if (StringUtils.isNotBlank(invoiceFromDate)) {
			query = query + "  AND TRUNC(IE.INVOICE_DATE)  =TO_DATE('"+invoiceFromDate+"', 'dd-MM-yy')";
		} else if(StringUtils.isNotBlank(invoiceToDate)) {
			query = query + "  AND TRUNC(IE.INVOICE_DATE)  <=TO_DATE('"+invoiceToDate+"', 'dd-MM-yy')";
		}
		
		if (StringUtils.isNotBlank(grnFromDate) && StringUtils.isNotBlank(grnToDate)) {
			query = query + "  AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+grnFromDate+"','dd-MM-yy') AND TO_DATE('"+grnToDate+"','dd-MM-yy')";
		} else if (StringUtils.isNotBlank(grnFromDate)) {
			query = query + "  AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  =TO_DATE('"+grnFromDate+"', 'dd-MM-yy')";
		} else if(StringUtils.isNotBlank(grnToDate)) {
			query = query + "  AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  <=TO_DATE('"+grnToDate+"', 'dd-MM-yy')";
		}
		
		if(StringUtils.isNotBlank(vendorId)) {
			query = query + "  AND IE.VENDOR_ID  = '"+vendorId+"'";
		}
		
		if(StringUtils.isNotBlank(siteIds)) {
			query = query + "  AND IE.SITE_ID in ("+siteIds+")";
		}
		

		List<PurchaseTaxReportBean> beanlist = new ArrayList<PurchaseTaxReportBean>(); 
		List<Map<String,Object>> list = jdbcTemplate.queryForList(query);
		String siteName = "";
		String grnNo = "";
		String receivedOrIssuedDate = "";
		String poId = "";
		String podate = "";
		String state = "";
		String tax = "";
		String vendorName = "";
		String address = "";
		String vendorState = "";
		String gsinNumber = "";
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String hsnCode = "";
		String measurMntName = "";
		String recevedQty = "";
		String amountPerUnitBeforeTaxes = "";
		String invoiceId = "";
		String invoiceDate = "";
		String basicAmount = "";
		String otherCharges = "";
		String taxOnOtherTransportChg = "";
		String taxAmount = "";
		String totalAmount = "";
		String typeOfPurchase = "";
		String cgst = "";
		String sgst = "";
		String igst = "";
		String totalTax = "";
		DecimalFormat df2 = new DecimalFormat("#.##"); 
		DecimalFormat df3 = new DecimalFormat("#.###"); 
		
		for(Map<String,Object> map : list){
			siteName = map.get("SITE_NAME")==null ? "-" : map.get("SITE_NAME").toString();
			grnNo = map.get("GRN_NO")==null ? "-" : map.get("GRN_NO").toString();
			receivedOrIssuedDate = map.get("RECEIVED_OR_ISSUED_DATE")==null ? "-" : map.get("RECEIVED_OR_ISSUED_DATE").toString();
			poId = map.get("PO_ID")==null ? "-" : map.get("PO_ID").toString();
			podate = map.get("PODATE")==null ? "-" : map.get("PODATE").toString();
			state = map.get("STATE")==null ? "-" : map.get("STATE").toString();
			tax = map.get("TAX")==null ? "0" : map.get("TAX").toString();
			vendorName = map.get("VENDOR_NAME")==null ? "-" : map.get("VENDOR_NAME").toString();
			address = map.get("ADDRESS")==null ? "-" : map.get("ADDRESS").toString();
			vendorState = map.get("VENDOR_STATE")==null ? "-" : map.get("VENDOR_STATE").toString();
			gsinNumber = map.get("GSIN_NUMBER")==null ? "-" : map.get("GSIN_NUMBER").toString();
			productName = map.get("PRODUCT_NAME")==null ? "-" : map.get("PRODUCT_NAME").toString();
			subProductName = map.get("SUB_PRODUCT_NAME")==null ? "-" : map.get("SUB_PRODUCT_NAME").toString();
			childProductName = map.get("CHILD_PRODUCT_NAME")==null ? "-" : map.get("CHILD_PRODUCT_NAME").toString();
			hsnCode = map.get("HSN_CODE")==null ? "-" : map.get("HSN_CODE").toString();
			measurMntName = map.get("MEASUR_MNT_NAME")==null ? "-" : map.get("MEASUR_MNT_NAME").toString();
			recevedQty = map.get("RECEVED_QTY")==null ? "0" : map.get("RECEVED_QTY").toString();
			amountPerUnitBeforeTaxes = map.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "0" : map.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
			invoiceId = map.get("INVOICE_ID")==null ? "-" : map.get("INVOICE_ID").toString();
			invoiceDate = map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString();
			basicAmount = map.get("BASIC_AMOUNT")==null ? "0" : map.get("BASIC_AMOUNT").toString();
			otherCharges = map.get("OTHER_CHARGES")==null ? "0" : map.get("OTHER_CHARGES").toString();
			taxOnOtherTransportChg = map.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "0" : map.get("TAX_ON_OTHER_TRANSPORT_CHG").toString();
			taxAmount = map.get("TAX_AMOUNT")==null ? "0" : map.get("TAX_AMOUNT").toString();
			totalAmount = map.get("TOTAL_AMOUNT")==null ? "0" : map.get("TOTAL_AMOUNT").toString();
			typeOfPurchase = state.equals("Local") ? "GST - "+tax+"%" : ( state.equals("Non Local") ? "IGST - "+tax+"%" : "-" );
			if(state.equals("Local")){
				/*cgst = sgst = df2.format((Double.valueOf(tax)*Double.valueOf(basicAmount))/(100*2));
				igst = "-";*/
				cgst = sgst = df2.format((Double.valueOf(taxAmount)+Double.valueOf(taxOnOtherTransportChg))/2);
				igst = "-";
			}
			if(state.equals("Non Local")){
				/*igst = df2.format((Double.valueOf(tax)*Double.valueOf(basicAmount))/100);
				cgst = sgst = "-";*/
				igst = df2.format(Double.valueOf(taxAmount)+Double.valueOf(taxOnOtherTransportChg));
				cgst = sgst = "-";
			}
			//totalTax = df2.format((Double.valueOf(tax)*Double.valueOf(basicAmount))/100 + (Double.valueOf(taxOnOtherTransportChg)*Double.valueOf(otherCharges))/100);
			totalTax = df2.format(Double.valueOf(taxAmount)+Double.valueOf(taxOnOtherTransportChg));
			
			
		    PurchaseTaxReportBean bean = new PurchaseTaxReportBean();
			//initialize all properties to '-'
			/*Class<?> clazz = bean.getClass();
		    if (clazz != null) {
		        try {
		        	Field[] fields = clazz.getDeclaredFields();
		        	for(Field field : fields){
		        		field.setAccessible(true);
		        		field.set(bean, "-");
		        	}
		        } catch (Exception e) {} 
		    }*/
			
			bean.setSiteName(siteName);
			bean.setGrnNo(grnNo);
			bean.setReceivedOrIssuedDate(DateUtil.convertDBDateInAnotherFormat(receivedOrIssuedDate,"dd-MMM-yy"));
			bean.setPoId(poId);
			bean.setPodate(DateUtil.convertDBDateInAnotherFormat(podate,"dd-MMM-yy"));
			bean.setState(state);
			bean.setTax(tax);
			bean.setVendorName(vendorName);
			bean.setAddress(address);
			bean.setVendorState(vendorState);
			bean.setGsinNumber(gsinNumber);
			bean.setProductName(productName);
			bean.setSubProductName(subProductName);
			bean.setChildProductName(childProductName);
			bean.setHsnCode(hsnCode);
			bean.setMeasurMntName(measurMntName);
			bean.setRecevedQty(df3.format(Double.valueOf(recevedQty)));
			bean.setAmountPerUnitBeforeTaxes(df2.format(Double.valueOf(amountPerUnitBeforeTaxes)));
			bean.setInvoiceId(invoiceId);
			bean.setInvoiceDate(DateUtil.convertDBDateInAnotherFormat(invoiceDate,"dd-MMM-yy"));
			bean.setBasicAmount(df2.format(Double.valueOf(basicAmount)));
			bean.setOtherCharges(df2.format(Double.valueOf(otherCharges)));
			bean.setTaxOnOtherTransportChg(df2.format(Double.valueOf(taxOnOtherTransportChg)));
			bean.setTotalAmount(df2.format(Double.valueOf(totalAmount)));
			bean.setTypeOfPurchase(typeOfPurchase);
			bean.setCgst(cgst);
			bean.setSgst(sgst);
			bean.setIgst(igst);
			bean.setTotalTax(totalTax);
			beanlist.add(bean);
		}

		return beanlist;
	}
	
	// getting the bank details for transsaction time 
	
	public List<PaymentModesBean> getBankDetails() {
		JdbcTemplate template = null;
		try {
			template  = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentModesBean> list = new ArrayList<PaymentModesBean>();
		String query = "select BANK_NAME,BANK_ID from BANK_DETAILS where STATUS='A' order by BANK_ID asc";
		dbIndentDts = template.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbIndentDts) {
			PaymentModesBean objBean = new PaymentModesBean();
			objBean.setName(prods.get("BANK_NAME")==null ? "" : prods.get("BANK_NAME").toString());
			objBean.setValue(prods.get("BANK_ID")==null ? "" : prods.get("BANK_ID").toString());
			list.add(objBean);
		}
			
		return list;
	}
	
	// getting the mails ids and emp ids for marketing executivs mails  based on the po number
	public List<String> getEmailsOfEmployeesInAllLowerDeptOfThisEmployeeInSiteLevelForMarketing(Set<String> marlketingPos,String reqSiteId) {

		
		List<Map<String, Object>> dbIndentDts = null;
		Set<String> emailSet = new HashSet<String>();
		
		for(String poNumber:marlketingPos){
		if(poNumber!=null && !poNumber.equals("")){
		String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in("
			+" select PO_CREATE_APPROVE_EMP_ID from SUMADHURA_PO_CRT_APPRL_DTLS where TEMP_PO_NUMBER in("
			+"	select (TEMP_PO_NUMBER) from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID in(" 
			+"	select PO_ENTRY_ID from SUMADHURA_PO_ENTRY where PO_NUMBER=? and SITE_ID=?)))";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poNumber,reqSiteId});

		for(Map<String, Object> prods : dbIndentDts) {
			String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
			if(StringUtils.isNotBlank(empEmail)){
				if(empEmail.contains(",")){
					String[] emailArr= empEmail.split(",");
					for(String email:emailArr){
						emailSet.add(email);
						//emailList.add(email);
					}
				}
				else{emailSet.add(empEmail);//emailList.add(empEmail);}
				
				}
			}
		}
		}
		}
		List<String> emailList = new ArrayList<String>(emailSet); // to convert the set to list 
		//String[] emailArrayList = new String[emailList.size()];
		//emailList.toArray(emailArrayList);
		return emailList;
	}
	
		@Override
	public List<ContractorTaxReportBean> getContractorTaxReport(Map<String, String> requestMap) {
		String billFromDate = requestMap.get("billFromDate");
		String billToDate = requestMap.get("billToDate");
		String ContractorId = requestMap.get("ContractorId");
		String siteIds = requestMap.get("siteIds");
		
		String query = " select S.SITE_NAME,ACP.WO_NUMBER,ACP.WO_DATE "
				+ " ,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME,SC.ADDRESS,SC.PAN_NUMBER,SC.GSTIN "
				+ " ,ACP.PAYMENT_TYPE,ACP.BILL_ID,ACP.BILL_DATE,ACP.BILL_AMOUNT,QCB.PAYMENT_TYPE_OF_WORK "

				+ " ,(select LISTAGG(concat(concat(QBDD.TYPE_OF_DEDUCTION,':'),QBDD.DEDUCTION_AMOUNT),';') WITHIN GROUP (ORDER BY QBDD.DEDUCTION_ID) from QS_BILL_DEDUCTION_DTLS QBDD"   
				+ " where QBDD.CONTRACTOR_BILL_ID=ACP.BILL_ID and QBDD.QS_WORKORDER_ISSUE_ID=ACP.QS_WORKORDER_ISSUE_ID and QBDD.TYPE_OF_DEDUCTION is not null"
				+ " ) as DEDUCTION_DTLS"

				+ " from ACC_CNT_PAYMENT ACP,QS_CONTRACTOR_BILL QCB,SITE S,SUMADHURA_CONTRACTOR SC "  
				+ " where ACP.STATUS!='D' and ACP.SITE_ID=S.SITE_ID and SC.CONTRACTOR_ID=ACP.CONTRACTOR_ID "
				+ " and QCB.BILL_ID=ACP.BILL_ID and QCB.QS_WORKORDER_ISSUE_ID=ACP.QS_WORKORDER_ISSUE_ID ";
		
		if (StringUtils.isNotBlank(billFromDate) && StringUtils.isNotBlank(billToDate)) {
			query = query + "  AND TRUNC(ACP.BILL_DATE)  BETWEEN TO_DATE('"+billFromDate+"','dd-MM-yy') AND TO_DATE('"+billToDate+"','dd-MM-yy')";
		} else if (StringUtils.isNotBlank(billFromDate)) {
			query = query + "  AND TRUNC(ACP.BILL_DATE)  =TO_DATE('"+billFromDate+"', 'dd-MM-yy')";
		} else if(StringUtils.isNotBlank(billToDate)) {
			query = query + "  AND TRUNC(ACP.BILL_DATE)  <=TO_DATE('"+billToDate+"', 'dd-MM-yy')";
		}
		
		
		if(StringUtils.isNotBlank(ContractorId)) {
			query = query + "  AND ACP.CONTRACTOR_ID  = '"+ContractorId+"'";
		}
		
		if(StringUtils.isNotBlank(siteIds)) {
			query = query + "  AND ACP.SITE_ID in ("+siteIds+")";
		}
		query = query + " order by ACP.BILL_DATE asc ";

		List<ContractorTaxReportBean> beanlist = new ArrayList<ContractorTaxReportBean>(); 
		List<Map<String,Object>> list = jdbcTemplate.queryForList(query);

		String siteName = "";
		String woNumber = "";
		String woDate = "";
		String contractorName = "";
		String address = "";
		String panNumber = "";
		String gstin = "";
		String paymentType = "";
		String paymentTypeOfWork = "";
		String billId = "";
		String billDate = "";
		String billAmount = "";
		String deductionDtls = "";
		
		DecimalFormat df2 = new DecimalFormat("#.##"); 
		DecimalFormat df3 = new DecimalFormat("#.###"); 
		
		for(Map<String,Object> map : list){
			siteName = map.get("SITE_NAME")==null ? "" : map.get("SITE_NAME").toString();
			woNumber = map.get("WO_NUMBER")==null ? "" : map.get("WO_NUMBER").toString();
			woDate = map.get("WO_DATE")==null ? "" : map.get("WO_DATE").toString();
			contractorName = map.get("CONTRACTOR_NAME")==null ? "" : map.get("CONTRACTOR_NAME").toString();
			address = map.get("ADDRESS")==null ? "" : map.get("ADDRESS").toString();
			panNumber = map.get("PAN_NUMBER")==null ? "" : map.get("PAN_NUMBER").toString();
			gstin = map.get("GSTIN")==null ? "" : map.get("GSTIN").toString();
			paymentType = map.get("PAYMENT_TYPE")==null ? "" : map.get("PAYMENT_TYPE").toString();
			paymentTypeOfWork = map.get("PAYMENT_TYPE_OF_WORK")==null ? "" : map.get("PAYMENT_TYPE_OF_WORK").toString();
			billId = map.get("BILL_ID")==null ? "" : map.get("BILL_ID").toString();
			billDate = map.get("BILL_DATE")==null ? "" : map.get("BILL_DATE").toString();
			billAmount = map.get("BILL_AMOUNT")==null ? "" : map.get("BILL_AMOUNT").toString();
			deductionDtls = map.get("DEDUCTION_DTLS")==null ? "" : map.get("DEDUCTION_DTLS").toString();
			
			double advanceDeduction = 0;
			double securityDeposit = 0;
			double otherDeductions = 0;
			String typeOfWork = "";
			
			ContractorTaxReportBean bean = new ContractorTaxReportBean();
			
			bean.setSiteName(siteName);
			bean.setWoNumber(woNumber);
			bean.setWoDate(DateUtil.convertDBDateInAnotherFormat(woDate,"dd-MMM-yy"));
			bean.setContractorName(contractorName);
			bean.setAddress(address);
			bean.setPanNumber(panNumber);
			bean.setGstin(gstin);
			bean.setBillId(billId);
			bean.setBillDate(DateUtil.convertDBDateInAnotherFormat(billDate,"dd-MMM-yy"));
			bean.setBillAmount(df2.format(Double.valueOf(billAmount)));
			bean.setBasicAmount(df2.format(Double.valueOf(billAmount)));
			
			if(paymentType.equals("NMR")){
				typeOfWork = "NMR Works";
			}
			else{
				if(paymentTypeOfWork.contains("#")){
					String[] workList = paymentTypeOfWork.split("#");
					for(String s : workList){
						if(s.contains("@@")){
							String work = s.split("@@")[1];
							typeOfWork += work+", ";
						}
					}
					typeOfWork = typeOfWork.substring(0,typeOfWork.length()-2);
				}
			}
			bean.setTypeOfWork(typeOfWork);
			
			
			if(deductionDtls.contains(";")){
				String[] deductionDtlsList = deductionDtls.split(";");
				for(String s : deductionDtlsList){
					if(s.contains(":")){
						String type = s.split(":")[0];
						String amt = s.split(":")[1];
						if(type.equals("ADV")){
							advanceDeduction+=Double.valueOf(amt);
						}
						if(type.equals("SEC")){
							securityDeposit+=Double.valueOf(amt);
						}
						if(type.equals("OTHER")){
							otherDeductions+=Double.valueOf(amt);
						}
						if(type.equals("PETTY")){
							otherDeductions+=Double.valueOf(amt);
						}
						if(type.equals("RECOVERY")){
							otherDeductions+=Double.valueOf(amt);
						}
					}
				}
			}
			bean.setAdvanceDeduction(df2.format(advanceDeduction));
			bean.setSecurityDeposit(df2.format(securityDeposit));
			bean.setOtherDeductions(df2.format(otherDeductions));
			/*bean.setSiteName(siteName);
			bean.setReceivedOrIssuedDate(DateUtil.convertDBDateInAnotherFormat(receivedOrIssuedDate,"dd-MMM-yy"));
			bean.setRecevedQty(df3.format(Double.valueOf(recevedQty)));
			bean.setBasicAmount(df2.format(Double.valueOf(basicAmount)));*/
			beanlist.add(bean);
		}

		return beanlist;
	}
	
}
