package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.UIProperties;

public class ExcelUploadProductsReceiveIssueDao extends UIProperties {



	//private PlatformTransactionManager transactionManager;
	private JdbcTemplate template;
	public ExcelUploadProductsReceiveIssueDao(JdbcTemplate template) {
		//this.transactionManager = transactionManager;
		this.template = template;
	}


	public String getPoEntryDetailsandIndentCreationDetails(String poNumber,String childProdId,String indentNumber) {
		String value="";
		StringBuffer sb = new StringBuffer();
		List<Map<String, Object>> dbPODts = null;


		String query = "select STPE.PO_ENTRY_ID,STPE.INDENT_CREATION_DTLS_ID,STPE.PO_ENTRY_DETAILS_ID,SPE.SITE_ID,SPE.INDENT_NO"
			+" from  SUMADHURA_PO_ENTRY SPE,SUMADHURA_PO_ENTRY_DETAILS STPE "
			+" where SPE.PO_NUMBER =? AND SPE.PO_ENTRY_ID=STPE.PO_ENTRY_ID AND STPE.CHILD_PRODUCT_ID=?";
		dbPODts = template.queryForList(query, new Object[]{poNumber,childProdId});
		for(Map<String, Object> prods : dbPODts) {
			String po_Entry_Id=prods.get("PO_ENTRY_ID") == null ? "0" : prods.get("PO_ENTRY_ID").toString();
			String inedent_Creation_Details_Id=prods.get("INDENT_CREATION_DTLS_ID") == null ? "0" : prods.get("INDENT_CREATION_DTLS_ID").toString();
			String po_Entry_detailsId=prods.get("PO_ENTRY_DETAILS_ID") == null ? "0" : prods.get("PO_ENTRY_DETAILS_ID").toString();
			String site_Id=prods.get("SITE_ID") == null ? "0" : prods.get("SITE_ID").toString();
			String indent_No=prods.get("INDENT_NO") == null ? "0" : prods.get("INDENT_NO").toString();
			value=po_Entry_Id+"@@"+inedent_Creation_Details_Id+"@@"+po_Entry_detailsId+"@@"+site_Id+"@@"+indent_No;


		}
		return value;
	}

	public int updateReceiveQuantityInIndentCreationDtls(String receiveQuantity,String indentCreationDetailsId) {

		String query = "update  SUMADHURA_INDENT_CREATION_DTLS set RECEIVE_QUANTITY = RECEIVE_QUANTITY+? "
			+ " where INDENT_CREATION_DETAILS_ID = ? ";
		int result = template.update(query, new Object[]{Double.valueOf(receiveQuantity),indentCreationDetailsId});
		//System.out.print(result+",");
		return result;
	}


	public int updateAllocatedQuantityInPurchaseDeptTable(String receiveQuantity,String indentCreationDetailsId,HttpServletRequest request) {

		List<Map<String, Object>> dbIndentDts = null;

		String query = "update  SUM_PURCHASE_DEPT_INDENT_PROSS set ALLOCATED_QUANTITY = ALLOCATED_QUANTITY+? "
			+ ", PENDING_QUANTIY = PENDING_QUANTIY-? , PO_INTIATED_QUANTITY = PO_INTIATED_QUANTITY-? "
			+ " where INDENT_CREATION_DETAILS_ID = ? ";
		int result = template.update(query, new Object[]{Double.valueOf(receiveQuantity),Double.valueOf(receiveQuantity),Double.valueOf(receiveQuantity),indentCreationDetailsId});

		return result;
	}


	public int updateReceivedQuantityInPoEntryDetails(String totalQuantity, String poEntryDetailsId) {

		String query1 = "select RECEIVED_QUANTITY from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_DETAILS_ID = ? ";
		String strReceivedQuantity = template.queryForObject(query1, new Object[]{poEntryDetailsId},String.class);
		//double receivedQuantity = Double.valueOf(strReceivedQuantity==null?"0":strReceivedQuantity)+Double.valueOf(totalQuantity);
		BigDecimal receivedQuantity = new BigDecimal(strReceivedQuantity==null?"0":strReceivedQuantity).add(new BigDecimal(totalQuantity));

		String query = "update SUMADHURA_PO_ENTRY_DETAILS set RECEIVED_QUANTITY = ? where PO_ENTRY_DETAILS_ID = ?";
		int result = template.update(query, new Object[]{receivedQuantity,poEntryDetailsId});

		return result;
	}


	public int setPOInactive(String poNumber,String reqSiteId) {

		//=========checking all po_quantity received?==========
		boolean doInactive = true;
		List<Map<String, Object>> dbIndentDts = null;

		String query1 = "select SPED.PO_QTY,SPED.RECEIVED_QUANTITY from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE "
			+ "where SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID and SPE.PO_NUMBER = ? and SPE.SITE_ID = ? ";
		dbIndentDts = template.queryForList(query1, new Object[]{poNumber,reqSiteId});
		try {
			for(Map<String, Object> prods : dbIndentDts) {
				String strPOQuantity = prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString();
				String strReceivedQuantity = prods.get("RECEIVED_QUANTITY") == null ? "0" : prods.get("RECEIVED_QUANTITY").toString();
				double poQuantity = Double.parseDouble(strPOQuantity);
				double receivedQuantity = Double.parseDouble(strReceivedQuantity);
				if(receivedQuantity<poQuantity){
					doInactive = false;
				}
			}
		} catch (Exception e) {
			doInactive = false;
			e.printStackTrace();
		}
		//==============================================================
		int result=0;
		if(doInactive){
			String query = "update SUMADHURA_PO_ENTRY set PO_STATUS = 'I' where PO_NUMBER = ? and SITE_ID = ? ";
			result = template.update(query, new Object[]{poNumber,reqSiteId});
		}
		//System.out.println("PO inactive: "+result);
		return result;
	}


	public int setIndentInactiveAfterChecking(String indentNumber) {
		int result = 0;
		boolean doInactive=true;

		List<Map<String, Object>> dbIndentDts = null;

		String query = "select REQ_QUANTITY,RECEIVE_QUANTITY from SUMADHURA_INDENT_CREATION_DTLS "
			+ " where INDENT_CREATION_ID = ? ";
		dbIndentDts = template.queryForList(query, new Object[]{indentNumber});
		try {
			for(Map<String, Object> prods : dbIndentDts) {
				String strRequiredQuantity = prods.get("REQ_QUANTITY") == null ? "" : prods.get("REQ_QUANTITY").toString();
				String strReceivedQuantity = prods.get("RECEIVE_QUANTITY") == null ? "" : prods.get("RECEIVE_QUANTITY").toString();
				double requiredQuantity = Double.parseDouble(strRequiredQuantity);
				double receivedQuantity = Double.parseDouble(strReceivedQuantity);
				if(receivedQuantity<requiredQuantity){
					doInactive = false;
				}
			}
		} catch (Exception e) {
			doInactive = false;
			e.printStackTrace();
		}
		if(doInactive){
			String query1 = "update SUMADHURA_INDENT_CREATION set STATUS = 'I' , PENDIND_DEPT_ID = 'REC' where INDENT_CREATION_ID = ? ";
			result = template.update(query1, new Object[]{Integer.parseInt(indentNumber)});

		}
		//System.out.println("Indent inactive: "+result);
		return result;
	}


	public String getIndentCreationDetailsId(String indentNumber,String childProdId) {
		List<Map<String, Object>> dbPODts = null;

		String result="";

		String Query = "select SICD.INDENT_CREATION_DETAILS_ID,SICD.INDENT_CREATION_ID from SUMADHURA_INDENT_CREATION_DTLS SICD,SUMADHURA_INDENT_CREATION SIC"
			+" where SICD.CHILD_PRODUCT_ID='"+childProdId+"' and SIC.SITEWISE_INDENT_NO='"+indentNumber+"' AND SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID"; 
		dbPODts = template.queryForList(Query, new Object[]{});
		for(Map<String, Object> prods : dbPODts) {
			String indentCreationDetailsId=prods.get("INDENT_CREATION_DETAILS_ID") == null ? "0" : prods.get("INDENT_CREATION_DETAILS_ID").toString();
			String indent_Id=prods.get("INDENT_CREATION_ID") == null ? "0" : prods.get("INDENT_CREATION_ID").toString();
			result=indentCreationDetailsId+"@@"+indent_Id;
		}
		return result;

	}

	public int getInvoiceCount(String  strInvoiceNmber, String vendorId,String strReceiveStartDate, String receiveDate){
		String query  = "select count(1) from  INDENT_ENTRY where INVOICE_ID = ? and VENDOR_ID = ? and TRUNC(RECEIVED_OR_ISSUED_DATE,'yy') = TRUNC(to_date(?,'dd-MM-yy'),'yy')";
		int intCount =    template.queryForInt(query, new Object[] {strInvoiceNmber, vendorId, receiveDate});
		return intCount;
	}


	public int getIndentEntrySequenceNumber() {

		int indentEntrySeqNum = 0;

		indentEntrySeqNum = template.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");

		return indentEntrySeqNum;
	}


	public void saveTransactionDetails(String invoiceNum, String transactionId, String gstId,
			String gstAmount, String totAmtAfterGSTTax,
			String transactionInvoiceId, String transAmount, String siteId, String indentEntrySeqNum) {

		String query = "INSERT INTO SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID, TRANSPORT_ID, TRANSPORT_GST_PERCENTAGE, TRANSPORT_GST_AMOUNT, TOTAL_AMOUNT_AFTER_GST_TAX, TRANSPORT_INVOICE_ID, INDENT_ENTRY_INVOICE_ID, DATE_AND_TIME, TRANSPORT_AMOUNT, SITE_ID, INDENT_ENTRY_ID) VALUES(TRANS_SEQ_ID.NEXTVAL,?,?,?,?,?,?,SYSDATE,?,?,?)";
		template.update(query, new Object[] {transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId,invoiceNum, transAmount,siteId, indentEntrySeqNum});

	}

	public int insertInvoiceData(int indentEntrySeqNum, IndentReceiveDto objIndentReceiveDto ) {

		//IndentReceiveDto objIndentReceiveDto = null;

		int result = 0;
		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

		String invoiceInsertionQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, INVOICE_ID, ENTRY_DATE, VENDOR_ID, TOTAL_AMOUNT, INDENT_TYPE, NOTE, RECEIVED_OR_ISSUED_DATE, INVOICE_DATE," +
		" TRANSPORTERNAME,EWAYBILLNO,VEHICLENO,PODATE,DC_NUMBER, STATE,PO_ID, GRN_NO,INDENT_CREATION_ID)" +
		" VALUES (?, ?, ?,?, sysdate, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println("Query for invoice insertion = "+invoiceInsertionQry);

		result = template.update(invoiceInsertionQry, new Object[] {indentEntrySeqNum,
				objIndentReceiveDto.getStrUserId(), objIndentReceiveDto.getStrSiteId(), objIndentReceiveDto.getStrInvoiceNo(),
				objIndentReceiveDto.getStrVendorId(), objIndentReceiveDto.getTotalAmnt(), "IN", objIndentReceiveDto.getStrRemarks(),
				objIndentReceiveDto.getStrReceiveDate()+" "+sdf.format(cal1.getTime()), objIndentReceiveDto.getStrInoviceDate()+" "+sdf.format(cal1.getTime()),
				objIndentReceiveDto.getTransporterName(),objIndentReceiveDto.geteWayBillNo(),objIndentReceiveDto.getVehileNo(),objIndentReceiveDto.getPoDate(),
				objIndentReceiveDto.getDcNo(),objIndentReceiveDto.getState(),objIndentReceiveDto.getPoNo(),objIndentReceiveDto.getGrnNumber(),objIndentReceiveDto.getIndentNumber()
		});





		System.out.println("Result = "+result);			
		logger.info("IndentReceiveDao --> insertInvoiceData() --> Result = "+result);

		return result;
	}

	public String getindentEntrySerialNo() {

		int indentEntrySeqNum = 0;

		String fianlVal = "";
		indentEntrySeqNum = template.queryForInt("SELECT SerialNo_ID_SEQ.NEXTVAL FROM DUAL");

		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());

		int i = Integer.parseInt(formattedDate);
		if (indentEntrySeqNum >9) {
			fianlVal = "/"+indentEntrySeqNum+"/"+formattedDate+"-"+(i+1);
		} else {
			fianlVal = "/0"+indentEntrySeqNum+"/"+formattedDate+"-"+(i+1);
		}

		logger.info(fianlVal);

		return fianlVal;
	}

	public int getPriceList_SeqNumber(){

		String query = "select SUMADHU_PRICE_LIST.NEXTVAL from dual";

		return template.queryForInt(query);
		//return getPriceList_SeqNumber;
	}

	public int getIndentEntryDtails_SeqNumber(){
		int intIndentEntryDetailsSeqNo = getEntryDetailsSequenceNumber();
		return intIndentEntryDetailsSeqNo;
	}


	public int insertIndentReceiveData(int indentEntrySeqNum,int intIndentEntryDetailsSeqNo, IndentReceiveDto irdto, String userId, String siteId,int intPriceListSeqNo) {

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		//String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, PRICE, BASIC_AMOUNT, TAX, HSN_CODE, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT, ENTRY_DATE,EXPIRY_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?)";
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME,  ENTRY_DATE,EXPIRY_DATE,REMARKS,PRICE_ID,HSN_CODE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?,?)";
		System.out.println("Query for indent issue = "+indentIssueQry);

		result = template.update(indentIssueQry, new Object[] { 
				intIndentEntryDetailsSeqNo,
				indentEntrySeqNum, 
				irdto.getProdId(), 
				irdto.getProdName(), 
				irdto.getSubProdId(), 
				irdto.getSubProdName(), 
				irdto.getChildProdId(), 
				irdto.getChildProdName(), 
				irdto.getQuantity(), 
				irdto.getMeasurementId(), 
				irdto.getMeasurementName(), 
				//irdto.getPrice(), 
				//irdto.getBasicAmnt(), 
				//irdto.getTax(), 
				//	irdto.getHsnCd(), 
				//	irdto.getTaxAmnt(), 
				//.getAmntAfterTax(), 
				//	irdto.getOtherOrTranportChrgs(), 
				//	irdto.getTaxOnOtherOrTranportChrgs(), 
				//	irdto.getOtherOrTransportChrgsAfterTax(), 
				//	irdto.getTotalAmnt(),
				irdto.getExpiryDate(),irdto.getStrRemarks(),intPriceListSeqNo,irdto.getHsnCd()
		}
		);

		/*auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentReceiveData() --> Result = "+result);*/

		return result;
	}



	public int updateIndentAvalibility(IndentReceiveDto irdto, String siteId) {

		List<Map<String, Object>> dbProductDetailsList = null;
		int result = 0;
		String availability_Id="";
		double receive_Form_Quantity=0.0;
		double quantity=0.0;

		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";

		dbProductDetailsList = template.queryForList(query, new Object[] {

				irdto.getProdId(), 
				irdto.getSubProdId(), 
				irdto.getChildProdId(), 
				irdto.getMeasurementId(), 
				siteId
		});

		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				quantity = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "0" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}

		receive_Form_Quantity=Double.parseDouble(irdto.getQuantity());
		quantity=quantity+receive_Form_Quantity;


		String updateIndentAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+quantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		System.out.println("Query for update indent avalibility = "+updateIndentAvalibilityQry);

		result = template.update(updateIndentAvalibilityQry, new Object[] {});
		System.out.println("Result = "+result);
		logger.info("IndentReceiveDao --> updateIndentAvalibility() --> Result = "+result);
		return result;
	}
	public void updateIndentAvalibilityWithNewIndent(IndentReceiveDto irdto, String siteId) {

		int result = 0;		

		String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID) VALUES (INDENT_AVAILABILITY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
		System.out.println("Query for new indent entry in indent availability = "+requesterQry);

		result = template.update(requesterQry, new Object[] {
				irdto.getProdId(), 
				irdto.getSubProdId(), 
				irdto.getChildProdId(), 
				irdto.getQuantity(), 
				irdto.getMeasurementId(), 
				siteId
		}
		);
		System.out.println("Result = "+result);			
		logger.info("IndentReceiveDao --> updateIndentAvalibilityWithNewIndent() --> Result = "+result);
	}

	public String getIndentAvailableId(IndentReceiveDto irdto, String site_id) {

		String query = "SELECT INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND MESURMENT_ID = ? AND SITE_ID = ? ";
		return String.valueOf(template.queryForInt(query, new Object[] {
				irdto.getProdId(), 
				irdto.getSubProdId(), 
				irdto.getChildProdId(), 
				irdto.getMeasurementId(), 
				site_id}));

	}

	public void saveReciveDetailsIntoSumduraPriceList(IndentReceiveDto irdto,
			String invoiceNumber, String siteId, String id,int entryDetailssequenceId,int intPriceListSeqNo,String typeOfPurchase) {
		String query = "";
		Double perPiceAmt = 0.0;
		Double totalAmt = 0.0;
		Double quantity = 0.0;
		String strQuantity="";
		//String entryDetailssequenceId = getEntryDetailsSequenceNumber();


		totalAmt = Double.valueOf(irdto.getTotalAmnt());

		try{
			BigDecimal bigDecimal = new BigDecimal(irdto.getQuantity().trim());
			strQuantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
			quantity = Double.valueOf(strQuantity);
		}catch(Exception e){
			e.printStackTrace();
		}
		//perPiceAmt = Double.valueOf(totalAmt/quantity);

		if(quantity > 0){
			perPiceAmt = Double.valueOf(totalAmt/quantity);
		}else{
			perPiceAmt = 0.0;
		}
		query = "INSERT INTO SUMADHURA_PRICE_LIST(PRICE_ID, INVOICE_NUMBER, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, INDENT_AVAILABILITY_ID, STATUS, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES, SITE_ID, UNITS_OF_MEASUREMENT,INDENT_ENTRY_DETAILS_ID,CREATED_DATE"
			+ "   , AMOUNT_PER_UNIT_BEFORE_TAXES, BASIC_AMOUNT, TAX, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT,HSN_CODE,RECEVED_QTY,TYPE_OF_PURCHASE) VALUES"
			+ "    (?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?,?)";
		System.out.println("   Query for save recive details into sumadhura price list table " + query);

		template.update(query, new Object[] {intPriceListSeqNo,invoiceNumber, irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(),id , "A", 
				strQuantity, perPiceAmt, siteId, irdto.getMeasurementId(),entryDetailssequenceId, irdto.getDate(),
				irdto.getPrice(),  irdto.getBasicAmnt(),  irdto.getTax(),  irdto.getTaxAmnt(),  irdto.getAmntAfterTax(), 
				irdto.getOtherOrTranportChrgs(),  irdto.getTaxOnOtherOrTranportChrgs(),  irdto.getOtherOrTransportChrgsAfterTax(), 
				irdto.getTotalAmnt(),irdto.getHsnCd(),irdto.getQuantity(),typeOfPurchase


		});


	}

	public String getProductAvailabilitySequenceNumber() {

		String query = "SELECT INDENT_AVAILABILITY_SEQ.NEXTVAL FROM DUAL";
		return String.valueOf(template.queryForInt(query));
	}

	public void saveReceivedDataIntoSumadhuClosingBalByProduct(IndentReceiveDto irdto, String siteId) {
		String query = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		List<Map<String, Object>> dbClosingBalancesList = null;


		Date today = new Date();                   
		@SuppressWarnings("deprecation")
		Date myDate = new Date(irdto.getDate()+" 23:59:59");


		if (today.compareTo(myDate)>0) {

			query = "SELECT CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) = TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
			dbClosingBalancesList = template.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				query = "";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"'WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				template.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

			} else {
				query = "";
				String dateTime = "";
				query = "SELECT DATE_AND_TIME, CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) <= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')  AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
				dbClosingBalancesList = template.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {
						//dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
						quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
						totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
					}
				}

				query = "";
				query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
				template.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity + Double.parseDouble(irdto.getQuantity()),siteId,Double.parseDouble(irdto.getTotalAmnt())+totalAmt,irdto.getDate(), irdto.getMeasurementId()});
				query = "";

				query = "SELECT TO_CHAR(TRUNC(DATE_AND_TIME)) AS DATE_AND_TIME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"','dd-MM-yy') AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND ROWNUM <= 1";
				dbClosingBalancesList = template.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

				Date curDate = null;
				Date curDate1 = null;
				Calendar c = Calendar.getInstance(); 
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {
						dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
						curDate = new Date(dateTime+" 23:59:59");
						curDate1 = new Date(irdto.getDate()+" 23:59:59");


						long diff = curDate.getTime() - curDate1.getTime();
						Long ii = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
						int k = ii.intValue();
						for (int h=1; h<=k-1; h++) {

							c.setTime(myDate); 
							c.add(Calendar.DATE, h);
							Date yourDate1 = c.getTime();
							query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
							template.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity, siteId,totalAmt,yourDate1, irdto.getMeasurementId()});

						}
					}
				} else {
					curDate1 = new Date(irdto.getDate()+" 23:59:59");

					curDate = new Date();
					long diff = curDate.getTime() - curDate1.getTime();
					Long ii = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					int k = ii.intValue();
					for (int h=1; h<=k; h++) {

						c.setTime(myDate); 
						c.add(Calendar.DATE, h);
						Date yourDate1 = c.getTime();
						query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
						template.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity ,siteId,totalAmt,yourDate1, irdto.getMeasurementId()});
					}
				}
				query="";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"' WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				template.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
			}
		}

	}

	public int getEntryDetailsSequenceNumber() {

		//String query = "SELECT MAX(INDENT_ENTRY_DETAILS_ID) FROM INDENT_ENTRY_DETAILS";
		String query = "select INDENT_ENTRY_DETAILS_SEQ.NEXTVAL from dual";

		return template.queryForInt(query);
	}
}
