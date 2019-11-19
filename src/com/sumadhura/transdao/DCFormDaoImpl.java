package com.sumadhura.transdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.DCFormBean;
import com.sumadhura.bean.DCFormViewBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.DCFormDto;
import com.sumadhura.dto.DCToInvoiceDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Repository
public class DCFormDaoImpl extends UIProperties  implements DCFormDao{
	static Logger log = Logger.getLogger(DCFormDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IndentReceiveDao ird;
	
	@Override
	public Map<String, String> loadProds(String siteId) {

		String product_Dept="";
		String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;

		products = new HashMap<String, String>();

		if(siteId.equals(marketing_Dept_Id)){product_Dept="MARKETING";}
		else{product_Dept="STORE";}
		
		String prodsQry = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','"+product_Dept+"')";
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
		}
		return  printMap(products);
	}
	@Override
	public Map<String, String> loadProdsByPONumber(String poNumber, String reqSiteId) {
		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;

		products = new HashMap<String, String>();

		String prodsQry = "SELECT P.PRODUCT_ID,  max(P.NAME) as PRODUCT_NAME"
				+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,PRODUCT P "
				+ " WHERE SPED.PRODUCT_ID = P.PRODUCT_ID AND"
				+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' group by P.PRODUCT_ID ";
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("PRODUCT_NAME")));
		}
		return  printMap(products);
	}

	@Override
	public String loadSubProds(String prodId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;	

		log.debug("Product Id = "+prodId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT SUB_PRODUCT_ID, NAME FROM SUB_PRODUCT WHERE PRODUCT_ID = ? AND STATUS ='A' ORDER BY NAME ASC";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{prodId});

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
		}		
		return sb.toString();
	}

	@Override
	public String loadChildProds(String subProductId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbChildProductsList = null;

		log.debug("Sub Product Id = "+subProductId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT CHILD_PRODUCT_ID, NAME FROM CHILD_PRODUCT WHERE SUB_PRODUCT_ID = ? AND STATUS ='A' ORDER BY NAME ASC";
		log.debug("Query to fetch child product = "+subProdsQry);

		dbChildProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{subProductId});

		for(Map<String, Object> childProds : dbChildProductsList) {
			sb = sb.append(String.valueOf(childProds.get("CHILD_PRODUCT_ID"))+"_"+String.valueOf(childProds.get("NAME"))+"|");
		}
		return sb.toString();
	}

	@Override
	public String loadDCFormMeasurements(String childProdId) {
		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementsList = null;

		log.debug("Child Product Id = "+childProdId);

		sb = new StringBuffer();

		String measurementsQry = "SELECT MEASUREMENT_ID, NAME FROM MEASUREMENT WHERE CHILD_PRODUCT_ID = ? and STATUS = ?";
		log.debug("Query to fetch measurement(s) = "+measurementsQry);

		dbMeasurementsList = jdbcTemplate.queryForList(measurementsQry, new Object[]{childProdId,"A"});

		for(Map<String, Object> measurements : dbMeasurementsList) {
			sb = sb.append(String.valueOf(measurements.get("MEASUREMENT_ID"))+"_"+String.valueOf(measurements.get("NAME"))+"|");
		}			
		return sb.toString();

	}

	@Override
	public Map<String, String> getGSTSlabs() {

		Map<String, String> gst = null;
		List<Map<String, Object>> dbGstSlabList = null;

		gst = new TreeMap<String, String>();

		String gstSlabsQry = "SELECT TAX_ID, TAX_PERCENTAGE FROM INDENT_GST";
		log.debug("Query to fetch gst slab = "+gstSlabsQry);

		dbGstSlabList = jdbcTemplate.queryForList(gstSlabsQry, new Object[]{});

		for(Map<String, Object> gstSlabs : dbGstSlabList) {
			gst.put(String.valueOf(gstSlabs.get("TAX_ID")).trim(), String.valueOf(gstSlabs.get("TAX_PERCENTAGE")).trim());
		}
		return gst;
	}

	@Override
	public int getIndentEntrySequenceNumber() {
		int dcformSeqNum = 0;

		dcformSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");

		return dcformSeqNum;
	}

	@Override
	public int insertDCFormData(int dcformSeqNum,DCFormDto dcformdto) throws Exception {
		int result = 0;
		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");


		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
		String dateInString = dcformdto.getStrReceiveDate();
		Date receivedate = formatter.parse(dateInString);
		//logger.info(receivedate);
		logger.info(formatter.format(receivedate));
		//        Date date=new Date(System.currentTimeMillis());
		//        String s=date.toString();

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		String rd = dcformdto.getStrDcDate();
		Date dcDate = dateFormat.parse(rd);
		logger.info(""+dcDate);
		logger.info(dateFormat.format(dcDate));
		Date podate = null;
		try{



			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
			String pd = dcformdto.getPoDate();

			if(pd!=""&&pd!=null)
				podate = format.parse(pd);

			//logger.info("Pd date"+podate);
			//logger.info(format.format(podate));
		}
		catch(Exception e){e.printStackTrace();}



		String invoiceInsertionQry = "INSERT INTO DC_ENTRY (DC_ENTRY_ID,USER_ID, SITE_ID, VENDOR_ID," +
				" TOTAL_AMOUNT, NOTE,RECEIVED_DATE,DC_DATE,TRANSPORTERNAME,EWAYBILLNO,VEHICLENO,PODATE,DC_NUMBER, STATE,PO_ID,ENTRY_DATE,DC_GRN_NO) " +
		"VALUES (?,?, ?, ?,?,?,?,?, ?, ?, ?,?,?,?,?,sysdate,?)";
		log.debug("Query for invoice insertion = "+invoiceInsertionQry);
		result = jdbcTemplate.update(invoiceInsertionQry, new Object[] {

				dcformSeqNum,
				dcformdto.getStrUserId(), dcformdto.getStrSiteId(),
				dcformdto.getStrVendorId(), dcformdto.getTotalAmnt(),  dcformdto.getStrRemarks(),
				receivedate,
				dcDate,
				dcformdto.getTransporterName(),dcformdto.geteWayBillNo(),dcformdto.getVehileNo(),
				podate,
				dcformdto.getDcNo(),dcformdto.getState(),dcformdto.getPoNo(),dcformdto.getDcGrnNumber()


		});

		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> insertInvoiceData() --> Result = "+result);

		return result;

	}

	@Override
	public String getVendorInfo(String vendName) {


		StringBuffer sb = null;
		List<Map<String, Object>> dbVendorList = null;		

		log.debug("Vendor Name = "+vendName);

		sb = new StringBuffer();

		String vendorInfoQry = "SELECT VENDOR_ID, ADDRESS, GSIN_NUMBER FROM VENDOR_DETAILS WHERE VENDOR_NAME = ? AND STATUS='A'";
		log.debug("Query to fetch vendor info = "+vendorInfoQry);

		dbVendorList = jdbcTemplate.queryForList(vendorInfoQry, new Object[]{vendName.trim()});

		for(Map<String, Object> vendInfo : dbVendorList) {
			sb = sb.append(String.valueOf(vendInfo.get("VENDOR_ID"))+"|"+String.valueOf(vendInfo.get("ADDRESS"))+"|"+String.valueOf(vendInfo.get("GSIN_NUMBER")));
		}			
		return sb.toString();

	}
	@Override
	public List<Map<String, Object>> getVendorInfoByID(String vendorId) {
		String vendorInfoQry = "SELECT  VENDOR_ID,VENDOR_NAME, ADDRESS, GSIN_NUMBER  FROM VENDOR_DETAILS where VENDOR_ID = '"+vendorId+"' AND STATUS='A'";
		log.debug("Query to fetch vendor info = "+vendorInfoQry);
		List<Map<String, Object>> dbVendorList = jdbcTemplate.queryForList(vendorInfoQry);
		return dbVendorList;
	}
	@Override
	public int insertDCFormReceiveData(int DCEntrySeqNum,int intDCFormSeqNo,DCFormDto dcformdto,String userId, String siteId,int intSumadhuraPriceListSeqNo) {


		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		String dcformQry = "INSERT INTO DC_FORM (DC_FORM_ID,PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME,CHILD_PRODUCT_ID," +
				"CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME," +
				" EXPIRY_DATE,DC_NUMBER,STATUS,DC_ENTRY_ID,PRICE_ID,REMARKS)" +
				" VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?,?,?, ?, ?,?,?)";
		log.debug("Query for indent issue = "+dcformQry);
		if(dcformdto !=null){
			result = jdbcTemplate.update(dcformQry, new Object[] {

					intDCFormSeqNo,
					dcformdto.getProdId(), 
					dcformdto.getProdName(), 
					dcformdto.getSubProdId(), 
					dcformdto.getSubProdName(), 
					dcformdto.getChildProdId(), 
					dcformdto.getChildProdName(), 
					dcformdto.getQuantity(), 
					dcformdto.getMeasurementId(), 
					dcformdto.getMeasurementName(),
					//dcformdto.getHsnCd(),
					dcformdto.getExpiryDate(),
					dcformdto.getDcNo(),
					"A",DCEntrySeqNum,intSumadhuraPriceListSeqNo,
					dcformdto.getStrRemarks()


			}
					);

		}

	/*	auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(IndentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);*/
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentReceiveData() --> Result = "+result);

		return result;

	}

	@Override
	public Map<String, String> getOtherCharges() {
		Map<String, String> otherCharges = null;
		List<Map<String, Object>> dbOtherChargesSlabList = null;

		otherCharges = new TreeMap<String, String>();

		String getOtherCharges = "SELECT CHARGE_ID, CHARGE_NAME FROM SUMADHURA_TRNS_OTHR_CHRGS_MST WHERE STATUS=?";
		log.debug("Query to fetch other charges = "+getOtherCharges);

		dbOtherChargesSlabList = jdbcTemplate.queryForList(getOtherCharges, new Object[]{

				"A"
		});

		for(Map<String, Object> otherChargesSlabs : dbOtherChargesSlabList) {
			otherCharges.put(String.valueOf(otherChargesSlabs.get("CHARGE_ID")).trim(), String.valueOf(otherChargesSlabs.get("CHARGE_NAME")).trim());
		}
		return otherCharges;
	}

	public static <K, V> Map<K, V> printMap(Map<K, V> map) {
		Map<K, V> mapObje = new TreeMap<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			mapObje.put( entry.getKey(), entry.getValue());

		}

		return mapObje;
	}
	public String getMeasurementName(String measurementId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementList = null;

		log.debug("Measurement Id = "+measurementId);

		sb = new StringBuffer();

		String measurementQry = "SELECT NAME FROM MEASUREMENT WHERE MEASUREMENT_ID = ?";
		log.debug("Query to fetch measurement name = "+measurementQry);

		dbMeasurementList = jdbcTemplate.queryForList(measurementQry, new Object[]{measurementId});

		for(Map<String, Object> subProds : dbMeasurementList) {
			sb = sb.append(String.valueOf(subProds.get("NAME")));
			break;
		}
		return sb.toString();
	}

	@Override
	public void saveTransactionDetails(String dcNO, String transactionId,
			String gstId, String gstAmount, String totAmtAfterGSTTax,
			String transactionInvoiceId, String transAmount, String siteId,
			String dcformSeqNum) {

		String query = "INSERT INTO SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID, TRANSPORT_ID, TRANSPORT_GST_PERCENTAGE, TRANSPORT_GST_AMOUNT, TOTAL_AMOUNT_AFTER_GST_TAX, TRANSPORT_INVOICE_ID, INDENT_ENTRY_DC_NUMBER, DATE_AND_TIME, TRANSPORT_AMOUNT, SITE_ID, DC_ENTRY_ID) VALUES(TRANS_SEQ_ID.NEXTVAL,?,?,?,?,?,?,SYSDATE,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId,dcNO, transAmount,siteId, dcformSeqNum});
		System.out.println("SUMADHURA_TRNS_OTHR_CHRGS_DTLS: "+result);
	}
	/*@Override
	public int updateTransactionDetails(String dcNO, String transactionId,
			String gstId, String gstAmount, String totAmtAfterGSTTax,
			String transactionInvoiceId, String transAmount, String siteId,
			String dcformSeqNum) {

		String query = "UPDATE SUMADHURA_TRNS_OTHR_CHRGS_DTLS set TRANSPORT_GST_PERCENTAGE = ? , TRANSPORT_GST_AMOUNT = ? , TOTAL_AMOUNT_AFTER_GST_TAX = ? , TRANSPORT_INVOICE_ID = ? , DATE_AND_TIME = sysdate , TRANSPORT_AMOUNT = ?  where INDENT_ENTRY_INVOICE_ID = ? ";
		return jdbcTemplate.update(query, new Object[] { gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId, transAmount,dcNO});

	}*/
	public int updateIndentAvalibility(DCFormDto dcformdto, String siteId) {
		
		List<Map<String, Object>> dbProductDetailsList = null;
		int result=0;
		String availability_Id="";
		double dc_Form_Quantity=0.0;
		double quantity=0.0;

		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {

				dcformdto.getProdId(), 
				dcformdto.getSubProdId(), 
				dcformdto.getChildProdId(), 
				dcformdto.getMeasurementId(), 
				siteId
		});
	
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				quantity = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}
			dc_Form_Quantity=Double.parseDouble(dcformdto.getQuantity());
			quantity=quantity+dc_Form_Quantity;

		String updateDCFormAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+quantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		log.debug("Query for update indent avalibility = "+updateDCFormAvalibilityQry);

		result = jdbcTemplate.update(updateDCFormAvalibilityQry, new Object[] {});
		log.debug("Result = "+result);
		logger.info("IndentReceiveDao --> updateDCFormAvalibility() --> Result = "+result);
		
		return result;
		}

	public void updateIndentAvailabulityWithNewDCForm(DCFormDto dcformdto, String siteId) {

		int result = 0;		

		String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID) VALUES (INDENT_AVAILABILITY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";//INDENT_AVAILABILITY_SEQ.NEXTVAL
		log.debug("Query for new DCForm  in DCForm availability = "+requesterQry);

		result = jdbcTemplate.update(requesterQry, new Object[] {
				dcformdto.getProdId(), 
				dcformdto.getSubProdId(), 
				dcformdto.getChildProdId(), 
				dcformdto.getQuantity(), 
				dcformdto.getMeasurementId(), 
				siteId
		}
				);
		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> updateDCFormWithNewDCForm() --> Result = "+result);
	}

	@Override
	public String getIndentAvailableId(DCFormDto dcformdto, String site_id) {

		String query = "SELECT INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND MESURMENT_ID = ? AND SITE_ID = ? ";
		return String.valueOf(jdbcTemplate.queryForInt(query, new Object[] {
				dcformdto.getProdId(), 
				dcformdto.getSubProdId(), 
				dcformdto.getChildProdId(), 
				dcformdto.getMeasurementId(), 
				site_id}));

	}
	@Override
	public void saveReciveDetailsIntoSumduraPriceList(DCFormDto dcformdto, String dcNumber, String siteId, String id,int dcformSeqNum,int intPriceListSeqNo,String typeOfPurchase) {
		String query = "";
		Double perPiceAmt = 0.0;
		Double totalAmt = 0.0;
		Double quantity = 0.0;
		//int entryDetailssequenceId1 = getDCFormSequenceNumber();

		totalAmt = Double.valueOf(dcformdto.getTotalAmnt() == "" ? "0" : dcformdto.getTotalAmnt().toString());

		totalAmt = Double.valueOf(totalAmt);
		quantity = Double.valueOf(dcformdto.getQuantity());
		perPiceAmt = Double.valueOf(totalAmt/quantity);

		Timestamp  timestamp = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
			Date parsedDate = dateFormat.parse(dcformdto.getDate());
			timestamp = new java.sql.Timestamp(parsedDate.getTime());
		} catch(Exception e) { //this generic but you can control another types of exception
			// look the origin of excption 
		}


		query = "INSERT INTO SUMADHURA_PRICE_LIST(PRICE_ID,DC_NUMBER,PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID," +
				" INDENT_AVAILABILITY_ID, STATUS, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES, SITE_ID, UNITS_OF_MEASUREMENT,DC_FORM_ENTRY_ID," +
				"CREATED_DATE,AMOUNT_PER_UNIT_BEFORE_TAXES, BASIC_AMOUNT, TAX,TAX_AMOUNT, AMOUNT_AFTER_TAX,OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT,HSN_CODE,RECEVED_QTY,TYPE_OF_PURCHASE)"+
				"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.debug("Query for save recive details into sumadhura price list table " + query);
		logger.info("Query for save recive details into sumadhura price list table " + query);
		jdbcTemplate.update(query, new Object[] {
				intPriceListSeqNo,
				dcNumber,
				dcformdto.getProdId(), dcformdto.getSubProdId(),
				dcformdto.getChildProdId(),id , "A", dcformdto.getQuantity(), 
				perPiceAmt, siteId, dcformdto.getMeasurementId(),dcformSeqNum,timestamp,dcformdto.getPrice(),
				dcformdto.getBasicAmnt(),
				dcformdto.getTax(),
				dcformdto.getTaxAmnt(),
				dcformdto.getAmntAfterTax(),
				dcformdto.getOtherOrTranportChrgs(),
				dcformdto.getTaxOnOtherOrTranportChrgs(),
				dcformdto.getOtherOrTransportChrgsAfterTax(), 
				dcformdto.getTotalAmnt(),dcformdto.getHsnCd(),dcformdto.getQuantity(),typeOfPurchase
		});
	}


	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(DCFormDto dcformdto, String site_id) throws Exception {String query = "";
	Double quantity = 0.0;
	Double totalAmt = 0.0;
	List<Map<String, Object>> dbClosingBalancesList = null;


	Date today = new Date();                   
	@SuppressWarnings("deprecation")

	DateFormat df = new SimpleDateFormat("dd-MM-yy");
	String dateInString = dcformdto.getDate();
	Date receivedate = df.parse(dateInString);
	df.format(receivedate);



	SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy");//dd/MM/yyyy
	Date now = new Date();
	String strDate = sdfDate.format(now);
	Date podate1 = df.parse(strDate);

	boolean flag = DateUtil.isReceiveDateLesstheTodayDate( sdfDate, receivedate, podate1);



	//if (today.compareTo(receivedate)>0) {
	if (flag) {
		query = "SELECT CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE " +
				"PRODUCT_ID=? AND  SUB_PRODUCT_ID = ?  " +
				"AND CHILD_PRODUCT_ID=?  AND SITE_ID=? AND MEASUREMENT_ID = ?  AND DC_NUMBER=?" +
				"AND TRUNC(DATE_AND_TIME) = TO_DATE('"+dcformdto.getDate()+"', 'dd-MM-yy')";
		dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {

				dcformdto.getProdId(), 
				dcformdto.getSubProdId(),
				dcformdto.getChildProdId(), 
				site_id,  
				dcformdto.getMeasurementId(),
				dcformdto.getDcNo()
		});

		if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
			query = "";
			query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+dcformdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+dcformdto.getTotalAmnt().trim()+"'WHERE " +
					"PRODUCT_ID=? AND  SUB_PRODUCT_ID = ? AND  CHILD_PRODUCT_ID=? " +
					" AND SITE_ID=? AND MEASUREMENT_ID = ?  AND DC_NUMBER=? AND " +
					"TRUNC(DATE_AND_TIME) >= TO_DATE('"+dcformdto.getDate()+"', 'dd-MM-yy')";
			int result_1 = jdbcTemplate.update(query, new Object[] {

					dcformdto.getProdId(), 
					dcformdto.getSubProdId(),
					dcformdto.getChildProdId(), 
					site_id,  
					dcformdto.getMeasurementId(),
					dcformdto.getDcNo()
			});

		} else {
			query = "";
			String dateTime = "";
			query = "SELECT DATE_AND_TIME, CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT" +
					" WHERE PRODUCT_ID=?  AND SUB_PRODUCT_ID = ?  AND CHILD_PRODUCT_ID=? " +
					"  AND SITE_ID=? AND MEASUREMENT_ID = ?  AND DC_NUMBER=? AND" +
					" TRUNC(DATE_AND_TIME) <= TO_DATE('"+dcformdto.getDate()+"', 'dd-MM-yy')  AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {
					dcformdto.getProdId(),
					dcformdto.getSubProdId(),
					dcformdto.getChildProdId(), 
					site_id,  
					dcformdto.getMeasurementId(),
					dcformdto.getDcNo()
			});

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				for (Map<String, Object> prods : dbClosingBalancesList) {
					//dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
					quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
					totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
				}
			}

			query = "";

			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
			String dateofProduct = dcformdto.getDate();
			Date poDate = formatter.parse(dateofProduct);
			logger.info("Podate"+poDate);
			logger.info(formatter.format(poDate));
			Double DCtotalAmt = Double.valueOf(dcformdto.getTotalAmnt() == "" ? "0" : dcformdto.getTotalAmnt().toString());

			query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID," +
					"CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID," +
					"DC_NUMBER)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?,?)";
			int result_2 = jdbcTemplate.update(query,new Object[] {
					dcformdto.getProdId(),
					dcformdto.getSubProdId(),
					dcformdto.getChildProdId(),
					quantity + Double.parseDouble(dcformdto.getQuantity()),
					site_id,
					DCtotalAmt+totalAmt,
					poDate, 
					dcformdto.getMeasurementId(),
					dcformdto.getDcNo()
			});

			query = "";
			query = "SELECT TO_CHAR(TRUNC(DATE_AND_TIME)) AS DATE_AND_TIME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE " +
					"TRUNC(DATE_AND_TIME) > TO_DATE('"+dcformdto.getDate()+"','dd-MM-yy') AND " +
					"PRODUCT_ID=?  AND SUB_PRODUCT_ID = ? AND  " +
					"CHILD_PRODUCT_ID=?  AND SITE_ID=? AND MEASUREMENT_ID = ? " +
					" AND DC_NUMBER=? AND ROWNUM <= 1";

			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {

					dcformdto.getProdId(), 
					dcformdto.getSubProdId(),
					dcformdto.getChildProdId(),
					site_id,  
					dcformdto.getMeasurementId(),
					dcformdto.getDcNo()
			});

			Date curDate = null;
			Date curDate1 = null;
			Calendar c = Calendar.getInstance();

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				for (Map<String, Object> prods : dbClosingBalancesList) {

					dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
					curDate = new Date(dateTime+" 23:59:59");
					curDate1 = new Date(dcformdto.getDate()+" 23:59:59");


					long diff = curDate.getTime() - curDate1.getTime();
					Long ii = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					int k = ii.intValue();
					for (int h=1; h<=k-1; h++) {

						c.setTime(receivedate); 
						c.add(Calendar.DATE, h);
						Date yourDate1 = c.getTime();
						query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID," +
								"SUB_PRODUCT_ID,CHILD_PRODUCT_ID," +
								"QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID,DC_NUMBER)" +
								"VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?,?)";

						int result_3 = jdbcTemplate.update(query,new Object[] {
								dcformdto.getProdId(),
								dcformdto.getSubProdId(),
								dcformdto.getChildProdId(),
								quantity, 
								site_id,
								totalAmt,
								yourDate1, 
								dcformdto.getMeasurementId(),
								dcformdto.getDcNo()
						});
					}
				}
			} else {

				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy");
				String dateConversion = dcformdto.getDate();
				Date prsntDate = dateFormatter.parse(dateConversion);
				//logger.info(prsntDate);
				logger.info(dateFormatter.format(prsntDate));


				curDate = new Date();
				long diff = curDate.getTime() - prsntDate.getTime();
				Long ii = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				int k = ii.intValue();
				for (int h=1; h<=k; h++) {

					c.setTime(receivedate); 
					c.add(Calendar.DATE, h);
					Date yourDate1 = c.getTime();

					query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID," +
							"SUB_PRODUCT_ID,CHILD_PRODUCT_ID," +
							"QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID," +
							"DC_NUMBER)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?,?)";

					int result_4 = jdbcTemplate.update(query,new Object[] {
							dcformdto.getProdId(),
							dcformdto.getSubProdId(),
							dcformdto.getChildProdId(),
							quantity ,
							site_id,
							totalAmt,
							yourDate1, 
							dcformdto.getMeasurementId(),
							dcformdto.getDcNo()
					});
				}
			}

			query="";
			query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+dcformdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+dcformdto.getTotalAmnt().trim()+"' WHERE " +
					"PRODUCT_ID=? AND  SUB_PRODUCT_ID = ? AND  CHILD_PRODUCT_ID=?" +
					"  AND SITE_ID=? AND MEASUREMENT_ID = ? AND  " +
					"DC_NUMBER=? AND TRUNC(DATE_AND_TIME) > TO_DATE('"+dcformdto.getDate()+"', 'dd-MM-yy')";

			int result_4 = jdbcTemplate.update(query, new Object[] {

					dcformdto.getProdId(), 
					dcformdto.getSubProdId(), 
					dcformdto.getChildProdId(),
					site_id,  
					dcformdto.getMeasurementId(),
					dcformdto.getDcNo()
			});
		}
	}
	}
	@Override
	public String getProductAvailabilitySequenceNumber() {

		String query = "SELECT INDENT_AVAILABILITY_SEQ.NEXTVAL FROM DUAL";
		return String.valueOf(jdbcTemplate.queryForInt(query));
	}


	@Override
	public List<DCFormViewBean> getDcFormDetails(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryId) {
		List<Map<String, Object>> dcFormDetails = null;

		List<DCFormViewBean> GetListofDcFormDetails = new ArrayList<DCFormViewBean>();
		DCFormViewBean objGetListofDcFormDetails=null;
		JdbcTemplate template = null;
		StringBuilder sql = new StringBuilder();

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dCNumber) ) {
				sql.append("SELECT IE.TOTAL_AMOUNT,IE.NOTE, IE.INDENT_ENTRY_ID,IE.RECEIVED_DATE,IE.TRANSPORTERNAME, IE.VEHICLENO,IE.EWAYBILLNO, ");
				sql.append("IE.PO_ID,IE.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE,IE.VEHICLENO,IE.TRANSPORTERNAME ,IE.STATE ");
				sql.append("from DC_ENTRY IE,VENDOR_DETAILS VD,DC_FORM DC where VD.VENDOR_ID = IE.VENDOR_ID and DC.DC_NUMBER = IE.DC_NUMBER and ");
				sql.append("IE.DC_NUMBER = ? and IE.VENDOR_ID = ? and SITE_ID = ?  and DC.STATUS = ? and IE.DC_ENTRY_ID =? AND IE.DC_ENTRY_ID=DC.DC_ENTRY_ID ");

				dcFormDetails = template.queryForList(sql.toString(), new Object[] {dCNumber,strVendorId,siteId,"A",strIndentEntryId});
			} 
			if (null != dcFormDetails && dcFormDetails.size() > 0) {
				for (Map<?, ?> DCFormBean : dcFormDetails) {
					objGetListofDcFormDetails = new DCFormViewBean();
					//objGetListofDcFormDetails.setState(DCFormBean.get("STATE") == null ? "" : DCFormBean.get("STATE").toString());
					objGetListofDcFormDetails.setVendorName(DCFormBean.get("VENDOR_NAME") == null ? "" : DCFormBean.get("VENDOR_NAME").toString());
					objGetListofDcFormDetails.setGstinNumber(DCFormBean.get("GSIN_NUMBER") == null ? "" : DCFormBean.get("GSIN_NUMBER").toString());
					objGetListofDcFormDetails.setVendorAddress(DCFormBean.get("ADDRESS") == null ? "" : DCFormBean.get("ADDRESS").toString());
					objGetListofDcFormDetails.setReceivedDate(DCFormBean.get("RECEIVED_DATE") == null ? "" : DCFormBean.get("RECEIVED_DATE").toString());
					objGetListofDcFormDetails.setVendorId(DCFormBean.get("VENDOR_ID") == null ? "" : DCFormBean.get("VENDOR_ID").toString());
					objGetListofDcFormDetails.setVehileNo(DCFormBean.get("VEHICLENO") == null ? "" : DCFormBean.get("VEHICLENO").toString());
					objGetListofDcFormDetails.setTransporterName(DCFormBean.get("TRANSPORTERNAME") == null ? "" : DCFormBean.get("TRANSPORTERNAME").toString());
					objGetListofDcFormDetails.setState(DCFormBean.get("STATE") == null ? "" : DCFormBean.get("STATE").toString());

					GetListofDcFormDetails.add(objGetListofDcFormDetails);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetListofDcFormDetails;
	}

	@Override
	public List<DCFormViewBean> getGetProductDetailsLists(HttpSession session, String dCNumber, String siteId,String strVendorId,String strIndentEntryID) {
		List<Map<String, Object>> dcFormproductList = null;
		List<DCFormViewBean> GetDcFormProductDetails  = new ArrayList<DCFormViewBean>();
		DCFormViewBean objDcFormProductDetails= null;
		JdbcTemplate template = null;
		StringBuilder sql = new StringBuilder();
		double doubleFinalAmount = 0.0;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dCNumber) ) {


				sql.append("select DCF.PRODUCT_NAME,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.SUB_PRODUCT_NAME,DCF.CHILD_PRODUCT_ID,DCF.RECEVED_QTY , ");
				sql.append("DCF.CHILD_PRODUCT_NAME,DCF.MEASUR_MNT_NAME,DCF.MEASUR_MNT_ID,SP.AMOUNT_PER_UNIT_AFTER_TAXES,SP.BASIC_AMOUNT, ");
				sql.append("SP.TAX,SP.HSN_CODE,SP.TAX_AMOUNT,SP.AMOUNT_AFTER_TAX,SP.OTHER_CHARGES,");
				sql.append("SP.TAX_ON_OTHER_TRANSPORT_CHG,SP.OTHER_CHARGES_AFTER_TAX,SP.TOTAL_AMOUNT,DCF.EXPIRY_DATE ,SP.AMOUNT_PER_UNIT_BEFORE_TAXES ");
				sql.append("from DC_FORM DCF ,DC_ENTRY IE,SUMADHURA_PRICE_LIST SP where SP.DC_FORM_ENTRY_ID = DCF.DC_FORM_ID and ");
				sql.append("DCF.DC_NUMBER = IE.DC_NUMBER and IE.DC_NUMBER = ? ");
				sql.append("and IE.SITE_ID = ?  and DCF.STATUS = ? and IE.VENDOR_ID = ? and IE.DC_ENTRY_ID = ? AND IE.DC_ENTRY_ID=DCF.DC_ENTRY_ID");
				dcFormproductList = template.queryForList(sql.toString(), new Object[] {dCNumber, siteId,"A",strVendorId,strIndentEntryID});

			} 
			if (null != dcFormproductList && dcFormproductList.size() > 0) {

				int i=1;
				double doubleTotalAmount = 0.0;
				double doubleQuantity = 0.0;
				double doublePrice = 0.0 ;

				for (Map<?, ?> DCFormBean : dcFormproductList) {

					objDcFormProductDetails = new DCFormViewBean();
					/*	doubleQuantity = Double.valueOf(IssueToOtherSiteInwardBean.get("QUANTITY") == null ? "" : IssueToOtherSiteInwardBean.get("QUANTITY").toString());
						doublePrice = Double.valueOf(IssueToOtherSiteInwardBean.get("AMOUNT") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT").toString());
						doubleTotalAmount = doubleQuantity*doublePrice;
						doubleFinalAmount = doubleFinalAmount+doubleTotalAmount;*/

					objDcFormProductDetails.setProductId(DCFormBean.get("PRODUCT_ID") == null ? "" : DCFormBean.get("PRODUCT_ID").toString());
					objDcFormProductDetails.setSubProductId(DCFormBean.get("SUB_PRODUCT_ID") == null ? "" : DCFormBean.get("SUB_PRODUCT_ID").toString());
					objDcFormProductDetails.setChildProductId(DCFormBean.get("CHILD_PRODUCT_ID") == null ? "" : DCFormBean.get("CHILD_PRODUCT_ID").toString());
					objDcFormProductDetails.setUnitsOfMeasurementId(DCFormBean.get("MEASUR_MNT_ID") == null ? "" : DCFormBean.get("MEASUR_MNT_ID").toString());
					objDcFormProductDetails.setProduct1(DCFormBean.get("PRODUCT_NAME") == null ? "" : DCFormBean.get("PRODUCT_NAME").toString());
					objDcFormProductDetails.setSubProduct1(DCFormBean.get("SUB_PRODUCT_NAME") == null ? "": DCFormBean.get("SUB_PRODUCT_NAME").toString());
					objDcFormProductDetails.setChildProduct1(DCFormBean.get("CHILD_PRODUCT_NAME") == null ? "" : DCFormBean.get("CHILD_PRODUCT_NAME").toString());
					objDcFormProductDetails.setUnitsOfMeasurement1(DCFormBean.get("MEASUR_MNT_NAME") == null ? "" : DCFormBean.get("MEASUR_MNT_NAME").toString());
					objDcFormProductDetails.setQuantity1(DCFormBean.get("RECEVED_QTY") == null ? "" : DCFormBean.get("RECEVED_QTY").toString());
					objDcFormProductDetails.setHsnCode1(DCFormBean.get("HSN_CODE") == null ? "" : DCFormBean.get("HSN_CODE").toString());
					objDcFormProductDetails.setPrice1(DCFormBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : DCFormBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
					//objDcFormProductDetails.setTotalAmount(String.valueOf(doubleTotalAmount));
					objDcFormProductDetails.setBasicAmount1(DCFormBean.get("BASIC_AMOUNT") == null ? "0" : DCFormBean.get("BASIC_AMOUNT").toString());
					objDcFormProductDetails.setTax1(DCFormBean.get("TAX") == null ? " 0" : DCFormBean.get("TAX").toString());
					objDcFormProductDetails.setTaxAmount1(DCFormBean.get("TAX_AMOUNT") == null ? "0" : DCFormBean.get("TAX_AMOUNT").toString());
					objDcFormProductDetails.setAmountAfterTax1(DCFormBean.get("AMOUNT_AFTER_TAX") == null ? "0" : DCFormBean.get("AMOUNT_AFTER_TAX").toString());
					objDcFormProductDetails.setOtherOrTransportCharges1(DCFormBean.get("OTHER_CHARGES") == null ? "0" : DCFormBean.get("OTHER_CHARGES").toString());
					objDcFormProductDetails.setTaxOnOtherOrTransportCharges1(DCFormBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0" : DCFormBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
					objDcFormProductDetails.setOtherOrTransportChargesAfterTax1(DCFormBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "0" : DCFormBean.get("OTHER_CHARGES_AFTER_TAX").toString());
					objDcFormProductDetails.setExpireDate1(DCFormBean.get("EXPIRY_DATE") == null ? "0" : DCFormBean.get("EXPIRY_DATE").toString());
					objDcFormProductDetails.setTotalAmount1(DCFormBean.get("TOTAL_AMOUNT") == null ? "0" : DCFormBean.get("TOTAL_AMOUNT").toString());
					objDcFormProductDetails.setPriceId(DCFormBean.get("PRICE_ID") == null ? "0" : DCFormBean.get("PRICE_ID").toString());

					objDcFormProductDetails.setStrSerialNo(String.valueOf(i));
					GetDcFormProductDetails.add(objDcFormProductDetails);

					i++;
				}
			}
			session.setAttribute("doubleFinalAmount", doubleFinalAmount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetDcFormProductDetails;
	}

	@Override
	public List<DCFormViewBean> getTransportChargesList(String dCNumber, String siteId) {
		List<Map<String, Object>> transportList = null;

		List<DCFormViewBean> GetDCFormTransportChargesList = new ArrayList<DCFormViewBean>();
		DCFormViewBean objGetDcFormTransportCharges=null;
		JdbcTemplate template = null;
		String sql = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dCNumber) ) {
				sql = "SELECT * FROM SUMADHURA_TRNS_OTHR_CHRGS_DTLS WHERE SITE_ID=? AND INDENT_ENTRY_INVOICE_ID=?";
				transportList = template.queryForList(sql, new Object[] {dCNumber, siteId});
			} 
			if (null != transportList && transportList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : transportList) {

					objGetDcFormTransportCharges = new DCFormViewBean();

					objGetDcFormTransportCharges.setTransportId(GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString());
					objGetDcFormTransportCharges.setConveyance1(GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString());
					objGetDcFormTransportCharges.setConveyanceAmount1(GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString());
					objGetDcFormTransportCharges.setGstTax1(GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
					objGetDcFormTransportCharges.setGstAmount1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					//objGetDcFormTransportCharges.setAmountAfterTax(GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString());


					GetDCFormTransportChargesList.add(objGetDcFormTransportCharges);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetDCFormTransportChargesList;


	}

	@Override
	public int getDCFormSequenceNumber() {
		int dcformSeqNum = 0;

		dcformSeqNum = jdbcTemplate.queryForInt("SELECT DC_FORM_SEQ.NEXTVAL FROM DUAL");

		return dcformSeqNum;
	}


	public int getInvoiceCount(String  strDCNumber, String vendorId,String strReceiveStartDate, String receiveDate){
		String query  = "select count(1) from  INDENT_ENTRY where DC_NUMBER = ? and VENDOR_ID = ? and RECEIVED_OR_ISSUED_DATE between  ? and ?";
		int intCount =    jdbcTemplate.queryForInt(query, new Object[] {strDCNumber, vendorId,strReceiveStartDate, receiveDate});
		return intCount;
	}
	@Override
	public int getDCCount(String  dcNumber, String vendorId, String receiveDate){
		System.out.println("1------>>>>"+dcNumber+","+vendorId+","+receiveDate);
		int intCount = 0;
		try{
			String query  = "select count(1) from  DC_ENTRY where DC_NUMBER = ? and VENDOR_ID = ? and TRUNC(RECEIVED_DATE,'MM') =  TRUNC(to_date(?,'dd-MM-yy'),'MM')";
			intCount =    jdbcTemplate.queryForInt(query, new Object[] {dcNumber, vendorId, receiveDate});
		}catch(Exception e){

		}
		System.out.println("DC Number : "+dcNumber+"entry id "+intCount);
		return intCount;
	}
	@Override
	public int getSavedDCCountForVerification(String  dcNumber, String vendorId, String receiveDate){
		System.out.println("1------>>>>"+dcNumber+","+vendorId+","+receiveDate);
		int intCount = 0;
		try{
			String query  = "select INDENT_ENTRY_ID from  INDENT_ENTRY where DC_NUMBER = ? and VENDOR_ID = ? and TRUNC(RECEIVED_OR_ISSUED_DATE) =  to_date(?,'dd-MM-yy')";
			intCount =    jdbcTemplate.queryForInt(query, new Object[] {dcNumber, vendorId, receiveDate});
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("DC Number : "+dcNumber+"entry id "+intCount);
		return intCount;
	}
	@Override
	public int updateDCFormData(String dcNumber, DCToInvoiceDto dctoinvoicedto) throws Exception 
	{
		int result=0;
		String query = "UPDATE INDENT_ENTRY SET INVOICE_ID = ? ,INVOICE_DATE = ? , TOTAL_AMOUNT = TOTAL_AMOUNT+? INDENT_TYPE = 'IN' where DC_NUMBER = ? ";


		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {

				dctoinvoicedto.getStrInvoiceNo(),dctoinvoicedto.getStrInoviceDate(),dctoinvoicedto.getTotalAmnt(),dcNumber
		});

		System.out.println(dcNumber);
		return result;

	}
	@Override
	public int doDcInactive(int dc_entry_id)
	{
		int result=0;
		String query = "UPDATE DC_FORM SET STATUS = 'I' where DC_ENTRY_ID = ? ";


		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {dc_entry_id });


		return result;	
	}
	
	@Override
	public String getIndentIdNo(String strVendorId, String dCNumber, String strSiteId) {
		String result="";
		String query = "SELECT INDENT_ENTRY_ID FROM INDENT_ENTRY where VENDOR_ID= ? and DC_NUMBER = ? and SITE_ID = ?";


		log.debug("Query for DC Update = "+query);
		int result1 = jdbcTemplate.queryForInt(query,new Object[]{strVendorId,dCNumber,strSiteId});
		//result=result1.get(0);
		result = String.valueOf(result1);
		return result;
	}
	@Override
	public int getIndentIdByInvoiceNo(String vendorId, String invoiceNumber, String site_id,String invoiceDate) {
		String query = "SELECT INDENT_ENTRY_ID FROM INDENT_ENTRY where VENDOR_ID= ? and INVOICE_ID = ? and SITE_ID = ? and TRUNC(INVOICE_DATE,'yy') =  TRUNC(to_date(?,'dd-MM-yy'),'yy')";


		log.debug("Query for DC Update = "+query);
		int result;
		try {
			result = jdbcTemplate.queryForInt(query,new Object[]{vendorId,invoiceNumber,site_id,invoiceDate});
		} catch (EmptyResultDataAccessException e) {
			result=0;
		}
		
		return result;
	}
	@Override
	public String getDcEntryIdNo(String strVendorId,String dCNumber,String strSiteId,String dcDate) {
		String result="";
		String query = "SELECT DC_ENTRY_ID FROM DC_ENTRY where VENDOR_ID= ? and DC_NUMBER = ? and SITE_ID = ? and DC_DATE = TO_DATE(?,'dd-mm-yy')";


		log.debug("Query for DC Update = "+query);
		int result1 = jdbcTemplate.queryForInt(query,new Object[]{strVendorId,dCNumber,strSiteId,dcDate});
		//result=result1.get(0);
		result = String.valueOf(result1);
		return result;
	}

	@Override
	public int updateDCFormDetails(String dcNumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DCToInvoiceDto  getPriceDetails(String dcNumber, DCToInvoiceDto DCFormDto, int intIndentEntryDetailsSeqNo,String invoiceNum) {
		int result=0;
		double totalAmount = 0.0;
		double totalAmountPerUnit = 0.0;
		double otherCharges = 0.0;
		double taxOnOtherCharges = 0.0;
		double otherChargesAfetrTax = 0.0;

		List<Map<String, Object>> priceDetils = null;
		//if transport charge already add for inwards from other site
		String query  = "SELECT TOTAL_AMOUNT,AMOUNT_PER_UNIT_AFTER_TAXES,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TAX_ON_OTHER_TRANSPORT_CHG FROM SUMADHURA_PRICE_LIST WHERE  DC_NUMBER = ? and PRICE_ID = ?";
		priceDetils = jdbcTemplate.queryForList(query, new Object[] {dcNumber, DCFormDto.getPriceId()});

		if (null != priceDetils && priceDetils.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : priceDetils) {



				totalAmount = Double.valueOf(GetTransportChargesDetails.get("TOTAL_AMOUNT") == null ? "0.0" : GetTransportChargesDetails.get("TOTAL_AMOUNT").toString());
				totalAmountPerUnit = Double.valueOf(GetTransportChargesDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0.0" : GetTransportChargesDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				otherCharges = Double.valueOf(GetTransportChargesDetails.get("OTHER_CHARGES") == null ? "0.0" : GetTransportChargesDetails.get("OTHER_CHARGES").toString());
				taxOnOtherCharges = Double.valueOf(GetTransportChargesDetails.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0.0" : GetTransportChargesDetails.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
				otherChargesAfetrTax = Double.valueOf(GetTransportChargesDetails.get("OTHER_CHARGES_AFTER_TAX") == null ? "0.0" : GetTransportChargesDetails.get("OTHER_CHARGES_AFTER_TAX").toString());


			}
		}

		totalAmount = totalAmount+Double.valueOf(DCFormDto.getTotalAmnt());
		totalAmountPerUnit = totalAmount/Integer.parseInt(DCFormDto.getQuantity());

		totalAmountPerUnit = ((int)(totalAmountPerUnit*100))/100;

		otherCharges = otherCharges+Double.valueOf(DCFormDto.getOtherOrTranportChrgs());
		taxOnOtherCharges = taxOnOtherCharges+Double.valueOf(DCFormDto.getTaxOnOtherOrTranportChrgs());
		otherChargesAfetrTax = otherChargesAfetrTax+Double.valueOf(DCFormDto.getOtherOrTransportChrgsAfterTax());


		DCFormDto.setTotalAmnt(String.valueOf(totalAmount));
		DCFormDto.setAmountPerUnit(String.valueOf(totalAmountPerUnit));
		DCFormDto.setOtherChrgs(String.valueOf(otherCharges));
		DCFormDto.setTaxOnOtherOrTranportChrgs(String.valueOf(taxOnOtherCharges));
		DCFormDto.setOtherOrTransportChrgsAfterTax(String.valueOf(otherChargesAfetrTax));

		return DCFormDto;
	}



	@Override
	public int updateDCPriceList(String dcNumber, DCToInvoiceDto DCFormDto, int intIndentEntryDetailsSeqNo,String invoiceNum) {
		int result=0;



		String query = "";

		List<Map<String, Object>> priceDetils = null;
		String strInvoiceNo  = "";
		String siteId = "";
		//if transport charge already add for inwards from other site

		//String query  = "select count(1) from SUMADHURA_PRICE_LIST  where DC_NUMBER = ? and   CHILD_PRODUCT_ID = ? and UNITS_OF_MEASUREMENT = ? ";
		//int intCount  = jdbcTemplate.queryForInt(query, new Object[] {dcNumber,DCFormDto.getChildProdId(),DCFormDto.getMeasurementId()});

		//for(int i  = 0;  i <= intCount;i++  ){



		query  = "SELECT SPL.SITE_ID,SPL.INVOICE_NUMBER,SPL.PRICE_ID,SPL.TOTAL_AMOUNT,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,SPL.OTHER_CHARGES,SPL.OTHER_CHARGES_AFTER_TAX,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.BASIC_AMOUNT,IED.RECEVED_QTY,SPL.TAX,SPL.TAX_AMOUNT FROM SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED WHERE  SPL.DC_NUMBER = ? and   SPL.CHILD_PRODUCT_ID = ? and SPL.UNITS_OF_MEASUREMENT = ? and SPL.INDENT_ENTRY_DETAILS_ID = IED.INDENT_ENTRY_DETAILS_ID ";
		priceDetils = jdbcTemplate.queryForList(query, new Object[] {dcNumber, DCFormDto.getChildProdId(),DCFormDto.getMeasurementId()});

		if (null != priceDetils && priceDetils.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : priceDetils) {
				double quantity = 0.0;
				double totalAmount = 0.0;
				double totalAmountPerUnit = 0.0;
				double otherCharges = 0.0;
				double taxOnOtherCharges = 0.0;
				double otherChargesAfetrTax = 0.0;
				double basicAmount = 0.0;
				double doubleBasicAmountFromJsp = 0.0;
				double tax = 0.0;
				double taxAmount = 0.0;
				//double basicAmountForCalTotalAmount = 0.0;

				int priceId = Integer.parseInt(GetTransportChargesDetails.get("PRICE_ID") == null ? "0.0" : GetTransportChargesDetails.get("PRICE_ID").toString());
				tax = Double.valueOf(GetTransportChargesDetails.get("TAX") == null ? "0.0" : GetTransportChargesDetails.get("TAX").toString());
				taxAmount = Double.valueOf(GetTransportChargesDetails.get("TAX_AMOUNT") == null ? "0.0" : GetTransportChargesDetails.get("TAX_AMOUNT").toString());
				quantity = Double.valueOf(GetTransportChargesDetails.get("RECEVED_QTY") == null ? "0.0" : GetTransportChargesDetails.get("RECEVED_QTY").toString());
				totalAmount = Double.valueOf(GetTransportChargesDetails.get("TOTAL_AMOUNT") == null ? "0.0" : GetTransportChargesDetails.get("TOTAL_AMOUNT").toString());
				totalAmountPerUnit = Double.valueOf(GetTransportChargesDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0.0" : GetTransportChargesDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				otherCharges = Double.valueOf(GetTransportChargesDetails.get("OTHER_CHARGES") == null ? "0.0" : GetTransportChargesDetails.get("OTHER_CHARGES").toString());
				taxOnOtherCharges = Double.valueOf(GetTransportChargesDetails.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0.0" : GetTransportChargesDetails.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
				otherChargesAfetrTax = Double.valueOf(GetTransportChargesDetails.get("OTHER_CHARGES_AFTER_TAX") == null ? "0.0" : GetTransportChargesDetails.get("OTHER_CHARGES_AFTER_TAX").toString());
				basicAmount =  Double.valueOf(GetTransportChargesDetails.get("BASIC_AMOUNT") == null ? "0.0" : GetTransportChargesDetails.get("BASIC_AMOUNT").toString());
				strInvoiceNo = GetTransportChargesDetails.get("INVOICE_NUMBER") == null ? "" : GetTransportChargesDetails.get("INVOICE_NUMBER").toString();
				siteId = GetTransportChargesDetails.get("SITE_ID") == null ? "" : GetTransportChargesDetails.get("SITE_ID").toString();
				//basicAmountForCalTotalAmount =  Double.valueOf(GetTransportChargesDetails.get("BASIC_AMOUNT") == null ? "0.0" : GetTransportChargesDetails.get("BASIC_AMOUNT").toString());
				otherCharges = otherCharges+Double.valueOf(DCFormDto.getOtherOrTranportChrgs());
				taxOnOtherCharges = taxOnOtherCharges+Double.valueOf(DCFormDto.getTaxOnOtherOrTranportChrgs());

				//double otherChrgsAfterTax = (Double.valueOf(DCFormDto.getOtherOrTranportChrgs())/100) * Double.valueOf(DCFormDto.getTaxOnOtherOrTranportChrgs());
				//taxAmount = taxAmount+taxAmnt;
				//otherChrgsAfterTax = ((int)(otherChrgsAfterTax*100))/100;
				//otherChargesAfetrTax = otherChargesAfetrTax+otherChrgsAfterTax+Double.valueOf(DCFormDto.getTaxOnOtherOrTranportChrgs());
				otherChargesAfetrTax = otherChargesAfetrTax+Double.valueOf(DCFormDto.getOtherOrTransportChrgsAfterTax());

				doubleBasicAmountFromJsp =  Double.valueOf(DCFormDto.getBasicAmnt())/Double.valueOf(DCFormDto.getQuantity());

				//it will differ for issue to other site
				basicAmount = basicAmount+doubleBasicAmountFromJsp*quantity;

				double taxAmnt = (doubleBasicAmountFromJsp*quantity/100) * Double.valueOf(DCFormDto.getTax());
				taxAmount = taxAmount+taxAmnt;
				taxAmount = ((int)(taxAmount*100))/100;
				double AmountAfterTax = basicAmount+taxAmount;

				totalAmount = totalAmount+doubleBasicAmountFromJsp*quantity+taxAmount+ Double.valueOf(DCFormDto.getOtherOrTransportChrgsAfterTax());
				totalAmountPerUnit = totalAmount/quantity;
				totalAmountPerUnit = ((int)(totalAmountPerUnit*100))/100;

				query = "UPDATE SUMADHURA_PRICE_LIST set AMOUNT_PER_UNIT_AFTER_TAXES  = ?,UPDATED_DATE = sysdate,AMOUNT_PER_UNIT_BEFORE_TAXES = ?"
						+ "     ,TAX = ?,TAX_AMOUNT = ? ,AMOUNT_AFTER_TAX = ? ,OTHER_CHARGES = ? ,OTHER_CHARGES_AFTER_TAX = ?,TOTAL_AMOUNT = ? ,BASIC_AMOUNT = ? ,TAX_ON_OTHER_TRANSPORT_CHG = ?"
						+ "      where DC_NUMBER = ? and  CHILD_PRODUCT_ID = ? and UNITS_OF_MEASUREMENT = ? and PRICE_ID = ? ";


				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {
						String.valueOf(totalAmountPerUnit),DCFormDto.getPrice(), 
						//DCFormDto.getBasicAmnt(), 
						DCFormDto.getTax(), 
						//DCFormDto.getHsnCd(), 
						taxAmount, 
						AmountAfterTax, 
						otherCharges, 

						otherChargesAfetrTax, 
						totalAmount,
						basicAmount,
						taxOnOtherCharges, 
						dcNumber,
						DCFormDto.getChildProdId(),DCFormDto.getMeasurementId(),priceId
				});



				query = "UPDATE INDENT_ENTRY SET  TOTAL_AMOUNT = TOTAL_AMOUNT+? where INVOICE_ID = ? ";


				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {totalAmount,strInvoiceNo	});

				query = "update SUMADHU_CLOSING_BAL_BY_PRODUCT set  PRICE = ?,TOTAL_AMOUNT = QUANTITY*?  where "+
						"DC_NUMBER = ? and  CHILD_PRODUCT_ID   = ? and MEASUREMENT_ID  = ? and SITE_ID = ? and INVOICE_NUMBER = ?";



				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {
						totalAmountPerUnit,totalAmountPerUnit,dcNumber, DCFormDto.getChildProdId(),DCFormDto.getMeasurementId(),
						siteId,strInvoiceNo

				});
			}
		}
		//if(priceDetils.size() == 0){

		query  = "SELECT SPL.SITE_ID,SPL.PRICE_ID,SPL.TOTAL_AMOUNT,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,SPL.OTHER_CHARGES,SPL.OTHER_CHARGES_AFTER_TAX,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.BASIC_AMOUNT,DF.RECEVED_QTY,SPL.TAX,SPL.TAX_AMOUNT FROM SUMADHURA_PRICE_LIST SPL,DC_FORM DF WHERE  SPL.DC_NUMBER = ? and   SPL.CHILD_PRODUCT_ID = ? and SPL.UNITS_OF_MEASUREMENT = ? and SPL.DC_FORM_ENTRY_ID = DF.DC_ENTRY_ID ";
		priceDetils = jdbcTemplate.queryForList(query, new Object[] {dcNumber, DCFormDto.getChildProdId(),DCFormDto.getMeasurementId()});

		if (null != priceDetils && priceDetils.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : priceDetils) {

				int priceId = Integer.parseInt(GetTransportChargesDetails.get("PRICE_ID") == null ? "0.0" : GetTransportChargesDetails.get("PRICE_ID").toString());
				siteId = GetTransportChargesDetails.get("SITE_ID") == null ? "" : GetTransportChargesDetails.get("SITE_ID").toString();

				double totalAmountPerUnit = Double.valueOf(DCFormDto.getTotalAmnt())/Integer.parseInt(DCFormDto.getQuantity());
				totalAmountPerUnit = ((int)(totalAmountPerUnit*100))/100;

				query = "UPDATE SUMADHURA_PRICE_LIST set AMOUNT_PER_UNIT_AFTER_TAXES  = ?, INDENT_ENTRY_DETAILS_ID = ?,INVOICE_NUMBER = ?,UPDATED_DATE = sysdate,AMOUNT_PER_UNIT_BEFORE_TAXES = ?"
						+ "     ,TAX = ?,TAX_AMOUNT = ? ,AMOUNT_AFTER_TAX = ? ,OTHER_CHARGES = ? ,OTHER_CHARGES_AFTER_TAX = ?,TOTAL_AMOUNT = ? ,BASIC_AMOUNT = ? ,TAX_ON_OTHER_TRANSPORT_CHG = ?"
						+ "      where DC_NUMBER = ? and  CHILD_PRODUCT_ID = ? and UNITS_OF_MEASUREMENT = ? and PRICE_ID = ?";


				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {
						totalAmountPerUnit,intIndentEntryDetailsSeqNo,invoiceNum,DCFormDto.getPrice(), 
						//DCFormDto.getBasicAmnt(), 
						DCFormDto.getTax(), 
						//DCFormDto.getHsnCd(), 
						DCFormDto.getTaxAmnt(), 
						DCFormDto.getAmntAfterTax(), 
						DCFormDto.getOtherOrTranportChrgs(), 

						DCFormDto.getOtherOrTransportChrgsAfterTax(), 
						DCFormDto.getTotalAmnt(),
						DCFormDto.getBasicAmnt(),
						DCFormDto.getTaxOnOtherOrTranportChrgs(), 
						dcNumber,
						DCFormDto.getChildProdId(),DCFormDto.getMeasurementId(),priceId
				});

				query = "UPDATE INDENT_ENTRY SET INVOICE_ID = ? ,INVOICE_DATE = ? , TOTAL_AMOUNT = ? where INDENT_ENTRY_ID = ( "+
						" select INDENT_ENTRY_ID from INDENT_ENTRY_DETAILS IED,SUMADHURA_PRICE_LIST SPL where  SPL.INDENT_ENTRY_DETAILS_ID = IED.INDENT_ENTRY_DETAILS_ID "+
						" and SPL.PRICE_ID = ?) ";


				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {

						DCFormDto.getStrInvoiceNo(),DCFormDto.getStrInoviceDate(),DCFormDto.getTotalAmnt(),priceId
				});


				query = "update SUMADHU_CLOSING_BAL_BY_PRODUCT set  PRICE = ?,TOTAL_AMOUNT = QUANTITY*?  where "+
						"DC_NUMBER = ? and  CHILD_PRODUCT_ID   = ? and MEASUREMENT_ID  = ? and SITE_ID = ? ";



				log.debug("Query for DC Update = "+query);
				result = jdbcTemplate.update(query, new Object[] {
						totalAmountPerUnit,totalAmountPerUnit,dcNumber, DCFormDto.getChildProdId(),DCFormDto.getMeasurementId(),
						siteId

				});
			}


		}





		return result;
	}
	@Override
	public int updatePriceInIndentEntryDetails(String price,String quantity,String dcNumber) {
		int result=0;
		System.out.println("Price in string:>>>"+price+"<<<");
		String query = "update INDENT_ENTRY_DETAILS set PRICE = ?,TOTAL_AMOUNT  = ? where PRICE_ID in "+
				"(select PRICE_ID from SUMADHURA_PRICE_LIST  where DC_NUMBER = ?  )";


		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {
				price,String.valueOf(Integer.parseInt(quantity)*Double.parseDouble(price)),dcNumber

		});

		return result;
	}
	@Override
	public int updateSumadhuraClosingBalance(String price,String quantity,String invoiceNum,String dcNumber,String siteid,DCToInvoiceDto DCToInvoicedto) {
		int result=0;
		String query = "update SUMADHU_CLOSING_BAL_BY_PRODUCT set  PRICE = ?,TOTAL_AMOUNT = ? ,INVOICE_NUMBER = ? where "+
				"DC_NUMBER = ? and PRODUCT_ID  = ? and SUB_PRODUCT_ID  = ? and CHILD_PRODUCT_ID   = ? and MEASUREMENT_ID  = ? and SITE_ID = ? ";



		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {
				price,String.valueOf(Integer.parseInt(quantity)*Double.parseDouble(price)),invoiceNum,dcNumber,
				DCToInvoicedto.getProdId(),DCToInvoicedto.getSubProdId(),DCToInvoicedto.getChildProdId(),DCToInvoicedto.getMeasurementId(),
				siteid

		});

		return result;
	}


	public int insertIndentReceiveData(int indentEntrySeqNum,int intIndentEntryDetailsSeqNo, DCToInvoiceDto irdto, String userId, String siteId) {


		String text = irdto.getDate();
		Timestamp receiveDate = Timestamp.valueOf(text);
		System.out.println(receiveDate);

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, ENTRY_DATE,EXPIRY_DATE,PRICE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)"; //sysdate
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[] { 
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
				//irdto.getHsnCd(), 
				//irdto.getTaxAmnt(), 
				//irdto.getAmntAfterTax(), 
				//irdto.getOtherOrTranportChrgs(), 
				//irdto.getTaxOnOtherOrTranportChrgs(), 
				//irdto.getOtherOrTransportChrgsAfterTax(), 
				receiveDate,
				irdto.getExpiryDate(),
				irdto.getPriceId()

		}
				);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		//new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentReceiveData() --> Result = "+result);

		return result;
	}
	public int isDCPresentAndInactive(String strVendorId,String strSiteId,String strDCNumber,String dcDate){
		int result=0;
		try{

			String query = "select count(distinct(DE.DC_ENTRY_ID)) from DC_FORM DF,DC_ENTRY DE  where DE.DC_ENTRY_ID = DF.DC_ENTRY_ID and DE.VENDOR_ID = ? and DE.SITE_ID = ? and DF.DC_NUMBER = ? and STATUS = 'A' and DC_DATE = TO_DATE(?,'dd-mm-yy')";


			//log.debug("Query for DC Update = "+query);
			result = jdbcTemplate.queryForInt(query, new Object[] { strVendorId,strSiteId,strDCNumber,dcDate });
		}catch(Exception e){
			result=0;
			//e.printStackTrace();
		}

		return result;
	}

	@Override
	public int updateInvoiceNumber(String dcNumber, String invoiceNumber, String dcDate, String grn_no,String strSiteId,String strVendorId) {

		int result=0;
		String query = "UPDATE INDENT_ENTRY SET INVOICE_ID = ? ,INVOICE_DATE = TO_DATE(?,'dd-mm-yy'), INDENT_TYPE = 'IN', GRN_NO = ? where DC_NUMBER = ? and SITE_ID = ?  and VENDOR_ID = ?";


		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {

				invoiceNumber,dcDate,grn_no,dcNumber,strSiteId,strVendorId
		});


		return result;
	}
	@Override
	public int insertIndentEntry(String dcNumber, String invoiceNumber, String invoiceDate, String grn_no,
			String site_id, String vendorId, int indent_entry_id, DCFormDto objDCFormDto,String Note,String after_change_Modified_date) {
		int result=0;
		String query = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID,INVOICE_ID ,INVOICE_DATE, INDENT_TYPE , GRN_NO ,"
		+ "USER_ID,	SITE_ID,PO_ID,TOTAL_AMOUNT,RECEIVED_OR_ISSUED_DATE,NOTE,VENDOR_ID,TRANSPORTERNAME,EWAYBILLNO,VEHICLENO,PODATE,STATE,ENTRY_DATE)"
		+" values(?,?,?,'IN',?   ,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {

				String.valueOf(indent_entry_id),invoiceNumber,invoiceDate,grn_no,
				objDCFormDto.getStrUserId(),objDCFormDto.getStrSiteId(),objDCFormDto.getPoNo(),objDCFormDto.getTotalAmnt(),/*DateUtil.convertToJavaDateFormat(objDCFormDto.getStrReceiveDate())*/invoiceDate,
				Note,objDCFormDto.getStrVendorId(),objDCFormDto.getTransporterName(),objDCFormDto.geteWayBillNo(),objDCFormDto.getVehileNo(),
				DateUtil.convertToJavaDateFormat(objDCFormDto.getPoDate()),objDCFormDto.getState()
		});
		if(result>0 && !after_change_Modified_date.equals("")){
			String update = "UPDATE INDENT_ENTRY SET SYS_PAYMENT_REQ_DATE =? WHERE INDENT_ENTRY_ID =?";
			log.debug("Query for invoice insertion = "+update);

			result = jdbcTemplate.update(update, new Object[] {DateUtil.convertToJavaDateFormat(after_change_Modified_date),String.valueOf(indent_entry_id)});
		}

		return result;
	}
	@Override
	public int updateIndentEntry(int indent_entry_id, DCFormDto objDCFormDto) {
		int result=0;
		/*String query1 = "select RECEIVED_OR_ISSUED_DATE from INDENT_ENTRY  where INDENT_ENTRY_ID = '"+indent_entry_id+"'";
		String receivedDateInDB = jdbcTemplate.queryForObject(query1,String.class);
		*/
		/*if(DateUtil.convertToJavaDateFormat(objDCFormDto.getStrReceiveDate()).after(DateUtil.convertToJavaDateFormat(receivedDateInDB))){
			String query = "UPDATE INDENT_ENTRY set TOTAL_AMOUNT=TOTAL_AMOUNT+? , RECEIVED_OR_ISSUED_DATE=?  where INDENT_ENTRY_ID=?";
			result = jdbcTemplate.update(query, new Object[] {

					objDCFormDto.getTotalAmnt(),DateUtil.convertToJavaDateFormat(objDCFormDto.getStrReceiveDate()),String.valueOf(indent_entry_id)
					});
		}
		else{*/
		//commented because when dc convert into invoice, received date is same as invoice date
		
			String query = "UPDATE INDENT_ENTRY set TOTAL_AMOUNT=TOTAL_AMOUNT+? where INDENT_ENTRY_ID=?";
			result = jdbcTemplate.update(query, new Object[] {

					objDCFormDto.getTotalAmnt(),String.valueOf(indent_entry_id)
					});
			
		/*}*/
		return result;
	}
	@Override
	public int updateDcEntry(int indent_entry_id, int dc_entry_id, String invoiceNumber, String invoiceDate) {
		int result=0;
		/* to delete active DC's in old data */
		String query1 = "SELECT INDENT_ENTRY_ID FROM DC_ENTRY  where DC_ENTRY_ID = "+dc_entry_id+" ";
		String db_indent_entry_id = jdbcTemplate.queryForObject(query1,String.class);
		if(StringUtils.isNotBlank(db_indent_entry_id)&&Integer.parseInt(db_indent_entry_id)!=indent_entry_id){
			String query2 = "DELETE FROM INDENT_ENTRY where INDENT_ENTRY_ID = ? ";
			int result2 = jdbcTemplate.update(query2, new Object[] {db_indent_entry_id});
		}

		
		String query = "UPDATE DC_ENTRY SET INDENT_ENTRY_ID = ?,INVOICE_ID = ?,INVOICE_DATE = ?  where DC_ENTRY_ID = ? ";


		log.debug("Query for DC Update = "+query);
		result = jdbcTemplate.update(query, new Object[] {

				indent_entry_id,invoiceNumber,invoiceDate,dc_entry_id
		});


		return result;
	}

	@Override
	public List<DCToInvoiceDto>  getDetailsInPriceTableByDcnumber(String dcNumber,String strSiteId,String vendorId,int dc_entry_id) {
		int result=0;
		List<DCToInvoiceDto> dcProductsList = new ArrayList<DCToInvoiceDto>();

		List<Map<String, Object>> DcDetails = null;
		//if transport charge already add for inwards from other site
		//String query  = "SELECT PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, EXPIRY_DATE FROM DC_FORM WHERE  DC_NUMBER = ? ";
		
		/*
		String query  = "SELECT PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, "+
                "MEASUR_MNT_ID, MEASUR_MNT_NAME, EXPIRY_DATE,DF.DC_ENTRY_ID as DC_FORM_ENTRY_ID FROM DC_FORM DF ,INDENT_ENTRY IE  WHERE DF.STATUS = 'A' and IE.DC_NUMBER = DF.DC_NUMBER and "+
                "IE.DC_NUMBER = ? and IE.SITE_ID = ? and IE.VENDOR_ID = ? and DF.INDENT_ENTRY_ID = ?";
*/
		String query  = "SELECT PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY,   MEASUR_MNT_ID, MEASUR_MNT_NAME, EXPIRY_DATE,DF.DC_FORM_ID ,PRICE_ID FROM DC_FORM DF  WHERE  DF.DC_ENTRY_ID = "+dc_entry_id+"";
		
		DcDetails = jdbcTemplate.queryForList(query);
		
		
		
		if (null != DcDetails && DcDetails.size() > 0) {
			for (Map<?, ?> productDetails : DcDetails) {

				DCToInvoiceDto DCFormDto = new DCToInvoiceDto();

				String prodId = productDetails.get("PRODUCT_ID") == null ? "" : productDetails.get("PRODUCT_ID").toString();
				String prodName = productDetails.get("PRODUCT_NAME") == null ? "" : productDetails.get("PRODUCT_NAME").toString();
				String subProdId = productDetails.get("SUB_PRODUCT_ID") == null ? "" : productDetails.get("SUB_PRODUCT_ID").toString();
				String subProdName = productDetails.get("SUB_PRODUCT_NAME") == null ? "" : productDetails.get("SUB_PRODUCT_NAME").toString();
				String childProdId = productDetails.get("CHILD_PRODUCT_ID") == null ? "" : productDetails.get("CHILD_PRODUCT_ID").toString();
				String childProdName = productDetails.get("CHILD_PRODUCT_NAME") == null ? "" : productDetails.get("CHILD_PRODUCT_NAME").toString();
				String measurementId = productDetails.get("MEASUR_MNT_ID") == null ? "" : productDetails.get("MEASUR_MNT_ID").toString();
				String measurementName = productDetails.get("MEASUR_MNT_NAME") == null ? "" : productDetails.get("MEASUR_MNT_NAME").toString();
				String receivedQty = productDetails.get("RECEVED_QTY") == null ? "" : productDetails.get("RECEVED_QTY").toString();
				String expiryDate = productDetails.get("EXPIRY_DATE") == null ? "" : productDetails.get("EXPIRY_DATE").toString();
				String dcFormEntryId = productDetails.get("DC_FORM_ID") == null ? "" : productDetails.get("DC_FORM_ID").toString();
				String strPriceListSeqNo = productDetails.get("PRICE_ID") == null ? "" : productDetails.get("PRICE_ID").toString();

				
				DCFormDto.setStrPriceListSeqNo(strPriceListSeqNo);
				DCFormDto.setProdId(prodId);
				DCFormDto.setProdName(prodName);
				DCFormDto.setSubProdId(subProdId);
				DCFormDto.setSubProdName(subProdName);
				DCFormDto.setChildProdId(childProdId);
				DCFormDto.setChildProdName(childProdName);
				DCFormDto.setMeasurementId(measurementId);
				DCFormDto.setMeasurementName(measurementName);
				DCFormDto.setQuantity(receivedQty);
				DCFormDto.setExpiryDate(expiryDate);
				DCFormDto.setStrDcForEntryId(dcFormEntryId);

				dcProductsList.add(DCFormDto);
			}
		}
		return dcProductsList;
	}

	@Override
	public int insertIndentEntryDetailsWithoutPricing(int indentEntrySeqNum,int intIndentEntryDetailsSeqNo, DCToInvoiceDto irdto, String userId, String siteId) {

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, ENTRY_DATE,EXPIRY_DATE,PRICE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate ,?,?)"; //sysdate
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[] { 
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
				//irdto.getHsnCd(), 
				//irdto.getTaxAmnt(), 
				//irdto.getAmntAfterTax(), 
				//irdto.getOtherOrTranportChrgs(), 
				//irdto.getTaxOnOtherOrTranportChrgs(), 
				//irdto.getOtherOrTransportChrgsAfterTax(), 
				irdto.getExpiryDate(),
				irdto.getStrPriceListSeqNo()
				//irdto.getPriceId()

		}
				);



		return result;
	}



	@Override
	public int updatePriceListWithoutPricing(String dcNumber,String invoiceNumber, int intIndentEntryDetailsSeqNo,
			DCToInvoiceDto dCToInvoicedto, String userId, String site_id) {
		int result=0;
		String query = "UPDATE SUMADHURA_PRICE_LIST set INDENT_ENTRY_DETAILS_ID = ?,INVOICE_NUMBER = ?,UPDATED_DATE = sysdate "
				+ "      where DC_NUMBER = ? and  CHILD_PRODUCT_ID = ? and UNITS_OF_MEASUREMENT = ? and SITE_ID = ? and DC_FORM_ENTRY_ID = ?";


		log.debug("Query for DC Update without pricing = "+query);
		result = jdbcTemplate.update(query, new Object[] {
				intIndentEntryDetailsSeqNo, invoiceNumber,
				dcNumber,
				dCToInvoicedto.getChildProdId(),
				dCToInvoicedto.getMeasurementId(),site_id,dCToInvoicedto.getStrDcForEntryId()
		});
		return result;
	}

	@Override
	public int updateSumadhuraClosingBalByProduct(String dcNumber, String invoiceNumber,
			DCToInvoiceDto dCToInvoicedto, String userId, String site_id) {
		int result=0;
		String query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT set INVOICE_NUMBER = ? "
				+ "      where DC_NUMBER = ? and  CHILD_PRODUCT_ID = ? and MEASUREMENT_ID = ? and SITE_ID = ?";

		result = jdbcTemplate.update(query, new Object[] {
				invoiceNumber,
				dcNumber,
				dCToInvoicedto.getChildProdId(),
				dCToInvoicedto.getMeasurementId(),site_id
		});
		return result;
	}

	
	@Override
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId) {

		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 
		String status="";
			
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "select DISTINCT(DE.DC_ENTRY_ID) AS DC_ENTRY_ID,DE.RECEIVED_DATE,DE.DC_NUMBER,DE.DC_DATE,VENDOR_NAME,VD.VENDOR_ID,DF.STATUS from DC_ENTRY DE, VENDOR_DETAILS VD,DC_FORM DF  where VD.VENDOR_ID = DE.VENDOR_ID AND    DE.SITE_ID = '"+siteId+"' AND DF.DC_ENTRY_ID=DE.DC_ENTRY_ID and TRUNC(DE.RECEIVED_DATE) between TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";

				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = " select DISTINCT(DE.DC_ENTRY_ID) AS DC_ENTRY_ID,DE.RECEIVED_DATE,DE.DC_NUMBER,DE.DC_DATE,VENDOR_NAME,VD.VENDOR_ID,DF.STATUS from DC_ENTRY DE, VENDOR_DETAILS VD,DC_FORM DF  where VD.VENDOR_ID = DE.VENDOR_ID AND    DE.SITE_ID = '"+siteId+"' AND DF.DC_ENTRY_ID=DE.DC_ENTRY_ID and TRUNC(DE.RECEIVED_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
			} else if(StringUtils.isNotBlank(toDate)) {
				query = " select DISTINCT(DE.DC_ENTRY_ID) AS DC_ENTRY_ID,DE.RECEIVED_DATE,DE.DC_NUMBER,DE.DC_DATE,VENDOR_NAME,VD.VENDOR_ID,DF.STATUS from DC_ENTRY DE, VENDOR_DETAILS VD,DC_FORM DF  where VD.VENDOR_ID = DE.VENDOR_ID AND    DE.SITE_ID = '"+siteId+"' AND DF.DC_ENTRY_ID=DE.DC_ENTRY_ID and TRUNC(DE.RECEIVED_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy') ";
			}


			dbIndentDts = template.queryForList(query, new Object[]{});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new ViewIndentIssueDetailsBean();
				strDCNumber=prods.get("DC_NUMBER")==null ? "" : prods.get("DC_NUMBER").toString();
				if(strDCNumber.contains("&")){strDCNumber=strDCNumber.replace('&','@');}
				System.out.println("the dc number"+strDCNumber);
				indentObj.setStrNumber(strDCNumber);
				indentObj.setRequesterId(prods.get("DC_NUMBER")==null ? "" : prods.get("DC_NUMBER").toString());
				indentObj.setStrInvoiceDate(prods.get("DC_DATE")==null ? "" : prods.get("DC_DATE").toString());
				indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setDcEntryId(prods.get("DC_ENTRY_ID")==null ? "" : prods.get("DC_ENTRY_ID").toString());
				
				status=(prods.get("STATUS")==null ? "" : prods.get("STATUS").toString());
				if(status.equals("A")){
					indentObj.setStatus("ACTIVE");
				}else{
				indentObj.setStatus("INACTIVE");}

			String date=prods.get("DC_DATE")==null ? "" : prods.get("DC_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setReceivedDate(date);
				String query1 = "select sum(TOTAL_AMOUNT) as SUM_TOTAL_AMOUNT from INDENT_ENTRY where DC_NUMBER = ?";
				List<Map<String, Object>> dbIndentDts1 = null;
				dbIndentDts1 = template.queryForList(query1, new Object[]{prods.get("DC_NUMBER").toString()});
				for(Map<String, Object> prods1 : dbIndentDts1) {
				indentObj.setStrTotalAmount(prods1.get("SUM_TOTAL_AMOUNT")==null ? "" : prods1.get("SUM_TOTAL_AMOUNT").toString());
				}

				list.add(indentObj);
			}


		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}	
	
	public String getDcVendorDetails(String dcnumber, String siteId,String strVendorId,String dcDateParam,String dcEntryId) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> transportorList = null;

		List<GetInvoiceDetailsBean> GetGrnDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;
		String tblOneData="";
		String state="";
		String dcnumber1="";
		String gstinNumber="";
		String dcDate="";
		String vendorName="";
		String poNo="";
		String vendorAddress="";
		String poDate="";
		String eWayBillNo="";
		String vehileNo="";
		String transporterName="";
		String receviedDate="";
		String totalamount="";
		String note="";
		String dcNo="";
		String doubleSumOfOtherCharges="";
		String indententryid="";
		JdbcTemplate template = null;
		String sql = "";
		String query="";
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dcnumber) ) {
				//sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.RECEVED_DATE,IED.TRANSPORTERNAME, IED.VEHICLENO,IED.EWAYBILLNO, IED.PO_NUMBER,IED.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN'";
				sql = "SELECT  DE.STATE,DE.TOTAL_AMOUNT,DE.NOTE, DE.DC_ENTRY_ID,DE.RECEIVED_DATE, CONCAT(TD.FIRST_NAME,' '||TD.LAST_NAME)  as TRANSPORTERNAME,DE.VEHICLENO,DE.EWAYBILLNO,"
						+"DE.PO_ID,DE.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,DE.DC_NUMBER, DE.DC_DATE,SED.EMP_NAME,DE.DC_GRN_NO " 
						+"FROM DC_FORM DF,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,DC_ENTRY DE  " 
						+"LEFT OUTER JOIN TRANSPORTOR_DETAILS TD ON TD.TRANSPORTOR_ID=DE.TRANSPORTERNAME "
						+"WHERE DE.DC_ENTRY_ID =DF.DC_ENTRY_ID AND DE.VENDOR_ID=VD.VENDOR_ID AND SED.EMP_ID = DE.USER_ID AND DE.DC_NUMBER= ? AND DE.SITE_ID= ?  and DE.VENDOR_ID = ? and  DF.DC_ENTRY_ID=? " 
						+"AND trunc(DE.DC_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				productList = template.queryForList(sql, new Object[] {dcnumber, siteId,strVendorId,dcEntryId,dcDateParam});
				System.out.println("in dao class data"+productList);
			} 
			if (null != productList && productList.size() > 0) {

				//int i = 1;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : productList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					state=GetInvoiceDetailsInwardBean.get("STATE") == null ? "" : GetInvoiceDetailsInwardBean.get("STATE").toString();
					dcnumber1=GetInvoiceDetailsInwardBean.get("DC_NUMBER") == null ? "" : GetInvoiceDetailsInwardBean.get("DC_NUMBER").toString();

					dcDate=GetInvoiceDetailsInwardBean.get("DC_DATE") == null ? "": GetInvoiceDetailsInwardBean.get("DC_DATE").toString();
					vendorName =GetInvoiceDetailsInwardBean.get("VENDOR_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("VENDOR_NAME").toString();
					gstinNumber=GetInvoiceDetailsInwardBean.get("GSIN_NUMBER") == null ? "" : GetInvoiceDetailsInwardBean.get("GSIN_NUMBER").toString();
					poNo=GetInvoiceDetailsInwardBean.get("PO_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("PO_ID").toString();
					vendorAddress=GetInvoiceDetailsInwardBean.get("ADDRESS") == null ? "" : GetInvoiceDetailsInwardBean.get("ADDRESS").toString();
					poDate=GetInvoiceDetailsInwardBean.get("PODATE") == null ? "" : GetInvoiceDetailsInwardBean.get("PODATE").toString();
					eWayBillNo=GetInvoiceDetailsInwardBean.get("EWAYBILLNO") == null ? "" : GetInvoiceDetailsInwardBean.get("EWAYBILLNO").toString();
					vehileNo=GetInvoiceDetailsInwardBean.get("VEHICLENO") == null ? "" : GetInvoiceDetailsInwardBean.get("VEHICLENO").toString();
					transporterName=GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME") == null ? "" : GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME").toString();
					receviedDate=GetInvoiceDetailsInwardBean.get("RECEIVED_DATE") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEIVED_DATE").toString();
					totalamount=(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					System.out.println("amount"+totalamount);	
					note=GetInvoiceDetailsInwardBean.get("NOTE") == null ? "" : GetInvoiceDetailsInwardBean.get("NOTE").toString();
					indententryid=GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID").toString();
					String strEmpName = indententryid=GetInvoiceDetailsInwardBean.get("EMP_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("EMP_NAME").toString();
					String steReceiveDate = GetInvoiceDetailsInwardBean.get("RECEIVED_DATE") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEIVED_DATE").toString();
					String grn_no = GetInvoiceDetailsInwardBean.get("DC_GRN_NO") == null ? "" : GetInvoiceDetailsInwardBean.get("DC_GRN_NO").toString();

					if(transporterName.equals("") || transporterName.equals(" ")){
						query="SELECT TRANSPORTERNAME FROM DC_ENTRY DE WHERE DE.DC_NUMBER=? AND DE.SITE_ID=?  and DE.VENDOR_ID =? and  DE.DC_ENTRY_ID=? AND trunc(DE.DC_DATE,'yy') = trunc(TO_DATE(?,'dd-MM-yy'),'yy')";
						transportorList = template.queryForList(query, new Object[] {dcnumber, siteId,strVendorId,dcEntryId,dcDateParam});
						
						for (Map<?, ?> getData : transportorList) {
							transporterName=getData.get("TRANSPORTERNAME") == null ? "" : getData.get("TRANSPORTERNAME").toString();
						}
					}
					
					
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = null;
					if(!receviedDate.equals("")){date = dt.parse(receviedDate); }
					Date date1 = null;
					if(!dcDate.equals("")){date1 = dt.parse(dcDate);}
					Date date2 = null;
					if(!poDate.equals("")){date2 = dt.parse(poDate); }
					SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
					if(date!=null){receviedDate=dt1.format(date);}
					if(date1!=null){dcDate=dt1.format(date1);}
					if(date2!=null){poDate=dt1.format(date2);}
					//GetGrnDetailsInward.add(objGetInvoiceDetailsInward);

					double totalAmt=Double.valueOf(totalamount);
					Date grnDate = new Date();
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=Math.ceil(totalAmt);
					
					Locale locale = new Locale("en","IN");
					
					NumberFormat numberFormat = NumberFormat.getInstance();
				    // String strtotal = numberFormat.format(totalAmt);
						//	NumberFormat.getNumberInstance(Locale.US);
					
					String strtotal=numberFormat.format(totalAmt);
					String strroundoff=String.format("%.2f",roundoff);
					String strgrandtotal=numberFormat.format(grandtotal);
					//StringBuffer tbldata=new tbldata();

	//	tblOneData+= gstinNumber+"@@@@"+dcnumber1+"@@"+dcDate+"@@"+"Sumadhura"+"@@"+""+strtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+ird.getindentEntrySerialNo()+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strgrandtotal;

					
					

		tblOneData+=gstinNumber+"@@"+changeDateFormat(grnDate)+"@@"+dcnumber1+"@@"+dcDate+"@@"+"Sumadhura"+"@@"+strtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcnumber1+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strgrandtotal+"@@"+strEmpName+"@@"+steReceiveDate+"@@"+steReceiveDate+"@@"+steReceiveDate;
		int length = tblOneData.split("@@").length;
					
								System.out.println(tblOneData);
				}

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return  tblOneData;
	}
	
	
	private String changeDateFormat(Date grnDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDcProductDetails(String dcnumber, String siteId,String strVendorId,HttpServletRequest request,String dcDate,String dcEntryId) {
		List<Map<String, Object>> GetInvoiceDetailsList = null;
		
		//List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;JdbcTemplate template = null;
		String sql = "";	
		String tblTwoData="";
		try{

			String product="";
			String subProduct="";

			String childProduct="";
			String price="";
			String basicAmnt="";
			String unitsOfMeasurement="";
			String tax="";
			String hsnCd="";
			String taxAmnt="";
			String amntAfterTax="";

			String numberAsString="";
			String quantity="";
			String state="";
			double amt=0.0;
			double CGSTAMT=0.0;
			double SGSTAMT=0.0;
			double percent=0.0;
			String CGST="";
			String SGST="";
			String IGST="";

			double IGSTAMT=0.0;
			String note="";
			int count=0;
			String strCGSTAMT = "";
			String strSGSTAMT = "";
			String strIGSTAMT = "";
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dcnumber) ) {

				sql+="select DF.PRODUCT_NAME,DE.STATE,DF.SUB_PRODUCT_NAME,DF.CHILD_PRODUCT_NAME,DF.MEASUR_MNT_NAME,DF.RECEVED_QTY,SPL.TAX,SPL.TAX_AMOUNT,"
						+"SPL.AMOUNT_AFTER_TAX,SPL.BASIC_AMOUNT,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.HSN_CODE " 
						+"from SUMADHURA_PRICE_LIST SPL,DC_ENTRY DE,DC_FORM DF "
						+"where SPL.DC_FORM_ENTRY_ID = DF.DC_FORM_ID and DF.DC_ENTRY_ID = DE.DC_ENTRY_ID and DE.DC_NUMBER = ? AND SPL.SITE_ID= ? and DE.VENDOR_ID = ? and DF.DC_ENTRY_ID= ? "
						+"AND trunc(DE.DC_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";


				GetInvoiceDetailsList = template.queryForList(sql, new Object[] {dcnumber, siteId,strVendorId,dcEntryId,dcDate});
				//System.out.println("second product data in service"+GetInvoiceDetailsList);
			} 
			if (null != GetInvoiceDetailsList && GetInvoiceDetailsList.size() > 0) {
				for (Map<?, ?> GetInvoiceDetailsInwardBean : GetInvoiceDetailsList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					product=GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString();
					subProduct=GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString();
					childProduct=GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString();		
					unitsOfMeasurement=GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString();	
					hsnCd=GetInvoiceDetailsInwardBean.get("HSN_CODE") == null ? "-" : GetInvoiceDetailsInwardBean.get("HSN_CODE").toString();	
					quantity=GetInvoiceDetailsInwardBean.get("RECEVED_QTY") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEVED_QTY").toString();
					price=GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "" : GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
				String 	basicAmnt1=GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT").toString();
					tax = GetInvoiceDetailsInwardBean.get("TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX").toString();
					taxAmnt = GetInvoiceDetailsInwardBean.get("TAX_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX_AMOUNT").toString();
					 
			String total=GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString();
					
					double number=Double.parseDouble(total);
					double basicnumber=Double.parseDouble(basicAmnt1);
					basicnumber=Double.parseDouble(new DecimalFormat("##.##").format((basicnumber)));
					number=Double.parseDouble(new DecimalFormat("##.##").format((number)));
					quantity=String.format("%.2f",Double.valueOf(quantity));
					price=String.format("%.2f",Double.valueOf(price));
					NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
					 amntAfterTax=numberFormat.format(number);
					 basicAmnt=numberFormat.format(basicnumber);
					 
					state=GetInvoiceDetailsInwardBean.get("STATE") == null ? "" : GetInvoiceDetailsInwardBean.get("STATE").toString();
					request.setAttribute("state",state);

					if (state.equals("1") || state.equals("Local")) {
						/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/
						if (tax.equals("0")) {
							CGST = "0.00";
							SGST = "0.00";
							strCGSTAMT="0.00";
							strSGSTAMT="0.00";
						} else {
							percent = Double.parseDouble(tax)/2;
							amt = Double.parseDouble(taxAmnt)/2;
							CGSTAMT = amt;
							SGSTAMT = amt;
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
							strCGSTAMT =String.format("%.2f",CGSTAMT);
							 strSGSTAMT =String.format("%.2f",SGSTAMT);
							
						}
					} else {
						percent = Double.parseDouble(tax);
						amt = Double.parseDouble(taxAmnt);
						IGST = String.valueOf(percent);
						IGSTAMT = amt;
						strIGSTAMT=String.format("%.2f",IGSTAMT);
					}

					count =++count;

					if (state.equals("1") || state.equals("Local")) {

						tblTwoData+=count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+quantity+"@@"+quantity+"@@"+price+"@@"+basicAmnt+"@@"+CGST+"@@"+strCGSTAMT+"@@"+SGST+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&";
					} else {
						tblTwoData+=count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+quantity+"@@"+quantity+"@@"+price+"@@"+basicAmnt+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+strIGSTAMT+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&";
						//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
					}

				}

			}	

		}catch (Exception e) {
			e.printStackTrace();
		}
		return tblTwoData;
	}		
	
	public String getDcTransportChargesListForGRN(String dcnumber,String strSiteId,String state,String vendorId,String dcDate,String dcEntryId) {
		List<Map<String, Object>> productList = null;

		List<GetInvoiceDetailsBean> GetTransportChargesListDetails = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetTransportChargesDetails=null;
		JdbcTemplate template = null;
		String sql = "";
		String tblCharges = "";
		String tblChargeName = "";
		String strConveyanceAmountChrg = "";
		double chargeCGST = 0.00;
		double chargeCGSTAMT = 0.00;
		double chargeSGST = 0.00;
		double chargeSGSTAMT = 0.00;
		String strAmountAfterTaxChrg =   "";
		double chargeIGST = 0.00;
		double chargeIGSTAMT = 0.00;
		String strGSTPercentage = "";
		String strGSTAmount = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(dcnumber) ) {
				sql += "select TRANSPORT_GST_AMOUNT,TAX_PERCENTAGE,TRANSPORT_GST_AMOUNT,TRANSPORT_AMOUNT,CHARGE_NAME,TOTAL_AMOUNT_AFTER_GST_TAX " 
						+"from SUMADHURA_TRNS_OTHR_CHRGS_DTLS STCD, SUMADHURA_TRNS_OTHR_CHRGS_MST STCM , INDENT_GST IG,DC_ENTRY DE "
						+"where STCM.CHARGE_ID = STCD.TRANSPORT_ID and IG.TAX_ID = STCD.TRANSPORT_GST_PERCENTAGE and DE.DC_ENTRY_ID = STCD.DC_ENTRY_ID and DE.DC_NUMBER = ? and DE.SITE_ID = ? and DE.VENDOR_ID = ? and STCD.DC_ENTRY_ID=? " 
						+"AND trunc(DE.DC_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";

				productList = template.queryForList(sql, new Object[] {dcnumber, strSiteId, vendorId,dcEntryId,dcDate});
			} 
			if (null != productList && productList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : productList) {

					objGetTransportChargesDetails = new GetInvoiceDetailsBean();

					tblChargeName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
					strGSTPercentage = GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString();
					strGSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "0" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
					strAmountAfterTaxChrg = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
					objGetTransportChargesDetails.setTransportInvoice1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setIndentEntryInvoiceId(GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setDateAndTime(GetTransportChargesDetails.get("DATE_AND_TIME") == null ? "": GetTransportChargesDetails.get("DATE_AND_TIME").toString());
					strConveyanceAmountChrg = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();
					strGSTAmount=String.format("%.2f",Double.valueOf(strGSTAmount));
					strAmountAfterTaxChrg=String.format("%.2f",Double.valueOf(strAmountAfterTaxChrg));
					strConveyanceAmountChrg=String.format("%.2f",Double.valueOf(strConveyanceAmountChrg));
					if(strGSTPercentage.contains("%")){
						strGSTPercentage = strGSTPercentage.replace("%", "");
					}


					if (state.equals("1") || state.equals("Local")) {

						chargeCGST = Double.valueOf(strGSTPercentage)/2; 
						chargeSGST = Double.valueOf(strGSTPercentage)/2;
						chargeCGSTAMT = Double.valueOf(strGSTAmount)/2;
						chargeSGSTAMT =  Double.valueOf(strGSTAmount)/2;
						
						 chargeCGST=Double.parseDouble(new DecimalFormat("##.##").format((chargeCGST)));
                         chargeSGST=Double.parseDouble(new DecimalFormat("##.##").format((chargeSGST)));
                         chargeSGSTAMT=Double.parseDouble(new DecimalFormat("##.##").format((chargeSGSTAMT)));
                         chargeCGSTAMT=Double.parseDouble(new DecimalFormat("##.##").format((chargeCGSTAMT)));

						tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
					} else {
						tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+strGSTPercentage+"@@"+strGSTAmount+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return tblCharges;
		
	}
	
		public int updateInvoiceNumberTrans(String invoiceNumber,int indentEntryId,int dc_entry_id) {

			int result1=0;
			String query = "UPDATE SUMADHURA_TRNS_OTHR_CHRGS_DTLS SET INDENT_ENTRY_INVOICE_ID = ?  , INDENT_ENTRY_ID = ? "
					+ " where DC_ENTRY_ID = ? ";


			log.debug("Query for DC Update = "+query);
			result1 = jdbcTemplate.update(query, new Object[] {

					invoiceNumber,indentEntryId,dc_entry_id
			});


			return result1;
		}
		public int updateIndentEntryInCreditNote(String invoiceNumber,int indentEntryId,int dc_entry_id) {

			int result1=0;
			String query = "UPDATE SUMADHURA_CREDIT_NOTE SET INVOICE_NUMBER = ?  , INDENT_ENTRY_NUMBER = ? "
					+ " where DC_ENTRY_ID = ? ";


			log.debug("Query for DC Update = "+query);
			result1 = jdbcTemplate.update(query, new Object[] {

					invoiceNumber,indentEntryId,dc_entry_id
			});


			return result1;
		}
		@Override
		public Map<String, String> TempPoloadProds(String indentNumber,String reqSiteId) {

			Map<String, String> products = null;
			List<Map<String, Object>> dbProductsList = null;

			products = new HashMap<String, String>();

			String prodsQry = "SELECT SPDIP.PRODUCT_ID, P.NAME FROM PRODUCT P,SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,SUMADHURA_INDENT_CREATION SIC,"
							+" SUMADHURA_INDENT_CREATION_DTLS SICD WHERE SPDIP.STATUS = 'A' AND SPDIP.PRODUCT_ID=P.PRODUCT_ID AND SIC.INDENT_CREATION_ID ='"+indentNumber+"' AND SIC. SITE_ID ='"+reqSiteId+"'"
							+" AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID";

			log.debug("Query to fetch product = "+prodsQry);

			dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

			for(Map<String, Object> prods : dbProductsList) {
				products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
			}
			return  printMap(products);
		}
		
		
		
		@Override
		public int getDCEntrySequenceNumber() {
			int dcEntrySeqNum = 0;

			dcEntrySeqNum = jdbcTemplate.queryForInt("SELECT DC_ENTRY_SEQ.NEXTVAL FROM DUAL");

			return dcEntrySeqNum;
		}
		@Override
		public DCFormDto getDB_DCEntry_Record(int dc_entry_id) {
			List<Map<String, Object>> dbProductsList = null;		
			String prodsQry = "select USER_ID,SITE_ID,PO_ID,TOTAL_AMOUNT,RECEIVED_DATE,NOTE,VENDOR_ID,"
					+ "TRANSPORTERNAME,EWAYBILLNO,VEHICLENO,PODATE,STATE,DC_GRN_NO,INDENT_ENTRY_ID from DC_ENTRY where DC_ENTRY_ID=?";

			dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{dc_entry_id});
			DCFormDto objDCFormDto = new DCFormDto();
			for(Map<String, Object> prods : dbProductsList) {
				
				objDCFormDto.setStrUserId(prods.get("USER_ID")==null ? "" : prods.get("USER_ID").toString());
				objDCFormDto.setStrSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				objDCFormDto.setPoNo(prods.get("PO_ID")==null ? "" : prods.get("PO_ID").toString());
				objDCFormDto.setTotalAmnt(prods.get("TOTAL_AMOUNT")==null ? "" : prods.get("TOTAL_AMOUNT").toString());
				objDCFormDto.setStrReceiveDate(prods.get("RECEIVED_DATE")==null ? "" : prods.get("RECEIVED_DATE").toString());
				objDCFormDto.setNote(prods.get("NOTE")==null ? "" : prods.get("NOTE").toString());
				objDCFormDto.setStrVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				objDCFormDto.setTransporterName(prods.get("TRANSPORTERNAME")==null ? "" : prods.get("TRANSPORTERNAME").toString());
				objDCFormDto.seteWayBillNo(prods.get("EWAYBILLNO")==null ? "" : prods.get("EWAYBILLNO").toString());
				objDCFormDto.setVehileNo(prods.get("VEHICLENO")==null ? "" : prods.get("VEHICLENO").toString());
				objDCFormDto.setPoDate(prods.get("PODATE")==null ? "" : prods.get("PODATE").toString());
				objDCFormDto.setState(prods.get("STATE")==null ? "" : prods.get("STATE").toString());
				objDCFormDto.setDcGrnNumber(prods.get("DC_GRN_NO")==null ? "" : prods.get("DC_GRN_NO").toString());
				objDCFormDto.setStrIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString());
				
				
			}
			return objDCFormDto;
		}
		
	
/***********************************************to check the all pos are same or not for convert dc to invoice***********************************/
	
		public String checkAllPosameOrNot(String strVendorId,String strSiteId,String strDCNumber,String dcDate){
			String strPoNumber="";
			List<Map<String, Object>> dbProductsList = null;		
			try{
				
					String query = "select DE.PO_ID from DC_FORM DF,DC_ENTRY DE  where DE.DC_ENTRY_ID = DF.DC_ENTRY_ID and DE.VENDOR_ID = ? and DE.SITE_ID = ? and DF.DC_NUMBER = ? and STATUS = 'A' and DC_DATE = TO_DATE(?,'dd-mm-yy')";
					//log.debug("Query for DC Update = "+query);
					dbProductsList = jdbcTemplate.queryForList(query, new Object[]{strVendorId,strSiteId,strDCNumber,dcDate});
					for(Map<String, Object> prods : dbProductsList) {
						strPoNumber=(prods.get("PO_ID")==null ? "" : prods.get("PO_ID").toString());
					}
				
			}catch(Exception e){
				//strPoNumber=0;
				e.printStackTrace();
			}
			return strPoNumber;
		}

		public String getPaymentRequestDays(String poNumber){
			String payment_Req_Days="";
			List<Map<String, Object>> dbProductsList = null;		
			try{
				
					String query = "SELECT PAYMENT_REQ_DAYS FROM SUMADHURA_PO_ENTRY WHERE PO_NUMBER=?";
					//log.debug("Query for DC Update = "+query);
					dbProductsList = jdbcTemplate.queryForList(query, new Object[]{poNumber});
					for(Map<String, Object> prods : dbProductsList) {
						payment_Req_Days=(prods.get("PAYMENT_REQ_DAYS")==null ? "" : prods.get("PAYMENT_REQ_DAYS").toString());
					}
				
			}catch(Exception e){
				//strPoNumber=0;
				e.printStackTrace();
			}
			return payment_Req_Days;
		}
	
	
	/************************************** THIS IS USED TO CHCEK WHETHER PAYMENT OR TAX INVOICE DONBE OR NOT*****************************/
	
		public boolean checkPaymentAndTaxInvoice(String invoiceNumber,String invoiceDate,String vendorId,String siteId) {
			String sql="";
			String indentEntryId="";
			List<Map<String, Object>> productList = null;
			List<Map<String, Object>> accList = null;
			List<Map<String, Object>> taxList = null;
			//JdbcTemplate template = null;
			String strInvoiceNumber="";
			boolean status=false;
			
			sql =new StringBuffer("select") 
			 		.append(" IE.INDENT_ENTRY_ID from INDENT_ENTRY IE,VENDOR_DETAILS VD ")
			 		.append(" WHERE IE.INVOICE_ID='"+invoiceNumber+"' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID " )
			 		.append(" AND IE.VENDOR_ID='"+vendorId+"' AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE('"+invoiceDate+"','dd-MM-yy'),'yy')")
			 		.toString();
			productList = jdbcTemplate.queryForList(sql);// {invoiceNumber,siteId,vendorName,invoiceDate});
			
			logger.info("**** check avaolable data is there or not *****"+productList);	
			
			if(productList!=null && !productList.equals("")){
				for (Map<?, ?> GetIndentEntryDetails : productList) {
					indentEntryId=GetIndentEntryDetails.get("INDENT_ENTRY_ID") == null ? "" : GetIndentEntryDetails.get("INDENT_ENTRY_ID").toString();		
				}
			}
			if(indentEntryId!=null && !indentEntryId.equals("")){
				String query="select INVOICE_NUMBER from ACC_PAYMENT WHERE INDENT_ENTRY_ID=?";
				accList = jdbcTemplate.queryForList(query, new Object[] {indentEntryId});
				if(accList.size()>0 && accList!=null && !accList.equals("")){
					for (Map<?, ?> GetIndentEntryDetails : accList) {
						strInvoiceNumber=GetIndentEntryDetails.get("INVOICE_NUMBER") == null ? "" : GetIndentEntryDetails.get("INVOICE_NUMBER").toString();		
					}
				}else{
					String tax="select INVOICE_NO from ACC_TAXINVOICE_DETAILS WHERE INDENT_ENTRY_ID=?";
					taxList = jdbcTemplate.queryForList(tax, new Object[] {indentEntryId});
					if(taxList!=null && !taxList.equals("")){
						for (Map<?, ?> GetIndentEntryDetails : taxList) {
							strInvoiceNumber=GetIndentEntryDetails.get("INVOICE_NO") == null ? "" : GetIndentEntryDetails.get("INVOICE_NO").toString();		
						}

					}

				}


			}
			if(strInvoiceNumber!=null && !strInvoiceNumber.equals("")){
				status=true;
			}

			return status;
		}

	/*********************** check the invoice date and vendor name and invoice id start********************************************/
	
		public boolean checkInvoiceDateAndNumber(String strInvoiceNumber,String strinvoiceDate,String vendorId,String site_id) {
			List<Map<String, Object>> productList = null;
			List<Map<String, Object>> checkList = null;
			String sql = "";
			String query="";
			boolean status=false;
			
			query =new StringBuilder("select")
					.append(" IE.INDENT_ENTRY_ID from INDENT_ENTRY IE,VENDOR_DETAILS VD ")
					.append(" WHERE IE.INVOICE_ID='"+strInvoiceNumber+"' AND IE.SITE_ID='"+site_id+"' AND IE.VENDOR_ID=VD.VENDOR_ID") 
					.append(" and IE.VENDOR_ID='"+vendorId+"' AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE('"+strinvoiceDate+"','dd-MM-yy'),'yy')")
					.toString();
			
			checkList = jdbcTemplate.queryForList(query);
			// IF USER ENTER NEW INVOICE NUMBER AT THAT TIME BLOW ONE NOT EXECUTED
			// BELOW ONE EXECUTED WITH SAME INVOICE,DATE,VENDOR,SITE GIVEN THEN EXECUTED
			if(checkList.size()>0 && checkList!=null && !checkList.equals("")){
			sql =new StringBuilder("select")
					.append(" IE.INDENT_ENTRY_ID from INDENT_ENTRY IE,VENDOR_DETAILS VD ")
					.append(" WHERE IE.INVOICE_ID='"+strInvoiceNumber+"' AND IE.SITE_ID='"+site_id+"' AND IE.VENDOR_ID=VD.VENDOR_ID") 
					.append(" and IE.VENDOR_ID='"+vendorId+"' AND trunc(IE.INVOICE_DATE) = trunc(TO_DATE('"+strinvoiceDate+"','dd-MM-yy'))")
					.toString();
			productList = jdbcTemplate.queryForList(sql);
			
			logger.info("**** check the data avilable or not query is *****"+productList);	
			if(productList.size()>0 && productList!=null && !productList.equals("")){
				
			}
			else{status=true;
			}
			
			}
			
			
			
			return status;
		}
	
	// check the where dc existed or not and is it active or not
	
		public String checkDcNumberExisted(String dcNumber,String vendorId,String DcDate,String siteId){
			String query="";
			String status="";
			String intCount="1_0";
			List<Map<String, Object>> DbList = null;
			query  = new StringBuilder("select ")
								.append(" STATUS from  DC_ENTRY DE,DC_FORM DF where DE.DC_NUMBER ='"+dcNumber+"' and DE.VENDOR_ID ='"+vendorId+"' and ")
								.append("DE.SITE_ID='"+siteId+"' and TRUNC(DE.DC_DATE) = TRUNC(TO_DATE('"+DcDate+"','dd-MM-yy')) AND DE.DC_ENTRY_ID=DF.DC_ENTRY_ID")
								.toString();
			
			DbList = jdbcTemplate.queryForList(query, new Object[] {});
			
			logger.info("**** check the data is dc already existed or not check *****"+DbList);	
			
			if(DbList.size()>0 && DbList!=null && !DbList.equals("")){
				for (Map<?, ?> GetIndentEntryDetails : DbList) {
					status=GetIndentEntryDetails.get("STATUS") == null ? "" : GetIndentEntryDetails.get("STATUS").toString();		
				}if(status.equalsIgnoreCase("A")){
					intCount="0_0";
				}else if(status.equalsIgnoreCase("I")){
					intCount="0_1";
				}
			}
			return intCount;
		}

	
	
	
	
	
	
	
		
		public void getGrnViewDetails1(Model model,String dcEntryId, HttpServletRequest request) {

			String query = "";
			String strDCFormQuery = "";
			String strDCNumber = "";
			JdbcTemplate template = null;
			List<Map<String, Object>> dbIndentDts = null;
			List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
			ViewIndentIssueDetailsBean indentObj = null; 
				
			try {
				template = new JdbcTemplate(DBConnection.getDbConnection());

				query = "select RECEIVED_DATE,DC_NUMBER,DC_DATE ,DC_ENTRY_ID,VENDOR_ID,SITE_ID from DC_ENTRY DE where DC_ENTRY_ID = ? ";

				dbIndentDts = template.queryForList(query, new Object[]{dcEntryId});

				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					strDCNumber=prods.get("DC_NUMBER")==null ? "" : prods.get("DC_NUMBER").toString();
					if(strDCNumber.contains("&")){strDCNumber=strDCNumber.replace('&','@');}
					System.out.println("the dc number"+strDCNumber);
					model.addAttribute("invoiceNumber", strDCNumber);
					model.addAttribute("vendorId", prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
					model.addAttribute("dcEntryId", prods.get("DC_ENTRY_ID")==null ? "" : prods.get("DC_ENTRY_ID").toString());
					model.addAttribute("siteId", prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
					String poEntryId = request.getParameter("poEntryId");
					model.addAttribute("poEntryId",poEntryId);
					
					String urlName = request.getParameter("urlName");
					if(StringUtils.isBlank(urlName)){
					urlName= "getGrnAndCreditNoteGrnViewDetails.spring?dcEntryId="+dcEntryId+"&poEntryId="+poEntryId;
					}model.addAttribute("urlName",urlName);
					
					String date=prods.get("DC_DATE")==null ? "" : prods.get("DC_DATE").toString();
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					model.addAttribute("dcDate", date);
			
					list.add(indentObj);
				}


			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				query = "";
				indentObj = null; 
				template = null;
				dbIndentDts = null;
			}
			
		}	
		public String getTransportorData(String transportorName) {
			//System.out.println("hai");
			StringBuffer sb = null;
			List<Map<String, Object>> dbTransportorList = null;		


			transportorName = transportorName.replace("$$", "&");
			transportorName = transportorName.toUpperCase();

			log.debug("transportor Name = "+transportorName);

			sb = new StringBuffer();

			String transportorInfoQry = "SELECT CONCAT(FIRST_NAME,' '||LAST_NAME)  as TRANSPORTOR_NAME,TRANSPORTOR_ID FROM TRANSPORTOR_DETAILS WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME)) like upper('%"+transportorName+"%') and STATUS='A'  and rownum<50";
		//	log.debug("Query to fetch contractor info = "+contractorInfoQry);

			dbTransportorList = jdbcTemplate.queryForList(transportorInfoQry, new Object[]{});

			for(Map<String, Object> transportorInfo : dbTransportorList) {
				sb = sb.append(String.valueOf(transportorInfo.get("TRANSPORTOR_NAME")));
				sb = sb.append("@@");
				//sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
			}	
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 2);
			}
			//System.out.println("Hai "+sb.toString());

			return sb.toString();
		}
	
		// this is used to get the transportor idan dname
		public String getTransportorId(String transportorName) {
			//System.out.println("hai");
			StringBuffer sb = null;
			List<Map<String, Object>> dbTransportorList = null;		


			transportorName = transportorName.replace("$$", "&");
			transportorName = transportorName.toUpperCase();

			log.debug("transportor Name = "+transportorName);

			sb = new StringBuffer();

			String transportorInfoQry = "SELECT CONCAT(FIRST_NAME,' '||LAST_NAME)  as TRANSPORTOR_NAME,TRANSPORTOR_ID FROM TRANSPORTOR_DETAILS WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME)) like upper('%"+transportorName+"%') and STATUS='A'  and rownum<50";
		//	log.debug("Query to fetch contractor info = "+contractorInfoQry);

			dbTransportorList = jdbcTemplate.queryForList(transportorInfoQry, new Object[]{});

			for(Map<String, Object> transportorInfo : dbTransportorList) {
				sb = sb.append(String.valueOf(transportorInfo.get("TRANSPORTOR_ID"))+"$"+String.valueOf(transportorInfo.get("TRANSPORTOR_NAME")));
				sb = sb.append("@@");
				break;
				//sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
			}	
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 2);
			}
			//System.out.println("Hai "+sb.toString());

			return sb.toString();
		}
	
	
	
	
	}