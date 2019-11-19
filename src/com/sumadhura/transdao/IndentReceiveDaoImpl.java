package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.DebitNoteBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.CreditNoteDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Repository
public class IndentReceiveDaoImpl extends UIProperties  implements IndentReceiveDao {

	static Logger log = Logger.getLogger(IndentReceiveDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private IndentCreationDao icd;

	public Map<String, String> loadProds(String siteId) {
		String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String product_Dept="";

		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;

		products = new HashMap<String, String>();
		if(siteId.equals(marketing_Dept_Id)){product_Dept="MARKETING";}
		else{product_Dept="STORE";}

		String prodsQry = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','"+product_Dept+"')";//AND PRODUCT_DEPT='STORE'
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
		}
		return  printMap(products);
	}



	public String loadSubProds(String prodId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;	

		log.debug("Product Id = "+prodId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT SUB_PRODUCT_ID, NAME FROM SUB_PRODUCT WHERE PRODUCT_ID = ? AND STATUS = 'A' ORDER BY NAME ASC";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{prodId});

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
		}		
		return sb.toString();
	}
	@Override
	public String loadSubProdsByPONumber(String prodId, String poNumber, String reqSiteId) {
		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;	

		log.debug("Product Id = "+prodId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT SP.SUB_PRODUCT_ID , max(SP.NAME) as SUB_PRODUCT_NAME "
			+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,SUB_PRODUCT SP  "
			+ " WHERE SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID AND  "
			+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' AND SPED.PRODUCT_ID = '"+prodId+"' group by SP.SUB_PRODUCT_ID";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{});

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("SUB_PRODUCT_NAME"))+"|");
		}		
		return sb.toString();
	}

	public String loadChildProds(String subProductId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbChildProductsList = null;

		log.debug("Sub Product Id = "+subProductId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT CHILD_PRODUCT_ID, NAME,MATERIAL_GROUP_ID FROM CHILD_PRODUCT WHERE SUB_PRODUCT_ID = ? AND STATUS = 'A' ORDER BY NAME ASC";
		log.debug("Query to fetch child product = "+subProdsQry);

		dbChildProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{subProductId});

		for(Map<String, Object> childProds : dbChildProductsList) {
			String groupId=String.valueOf(childProds.get("MATERIAL_GROUP_ID")== null ? "0" : childProds.get("MATERIAL_GROUP_ID"));
			if(groupId.equalsIgnoreCase("NA")){
				groupId="0";
			}
			sb = sb.append(String.valueOf(childProds.get("CHILD_PRODUCT_ID"))+"_"+String.valueOf(childProds.get("NAME"))+"_"+groupId+"|");
		}		
		return sb.toString();
	}
	@Override
	public String loadChildProdsByPONumber(String subProductId, String poNumber, String reqSiteId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbChildProductsList = null;

		log.debug("Sub Product Id = "+subProductId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT CP.CHILD_PRODUCT_ID , max(CP.NAME) as CHILD_PRODUCT_NAME "
			+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,CHILD_PRODUCT CP  "
			+ " WHERE SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND "
			+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' AND SPED.SUB_PRODUCT_ID = '"+subProductId+"' group by CP.CHILD_PRODUCT_ID ";
		log.debug("Query to fetch child product = "+subProdsQry);

		dbChildProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{});

		for(Map<String, Object> childProds : dbChildProductsList) {
			sb = sb.append(String.valueOf(childProds.get("CHILD_PRODUCT_ID"))+"_"+String.valueOf(childProds.get("CHILD_PRODUCT_NAME"))+"|");
		}		
		return sb.toString();
	}

	public String loadIndentReceiveMeasurements(String childProdId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementsList = null;

		log.debug("Child Product Id = "+childProdId);

		sb = new StringBuffer();

		String measurementsQry = "SELECT M.MEASUREMENT_ID,M.NAME,C.MATERIAL_GROUP_ID FROM MEASUREMENT M,CHILD_PRODUCT C WHERE M.CHILD_PRODUCT_ID = ? and M.STATUS = ? AND C.CHILD_PRODUCT_ID=M.CHILD_PRODUCT_ID";
		log.debug("Query to fetch measurement(s) = "+measurementsQry);

		dbMeasurementsList = jdbcTemplate.queryForList(measurementsQry, new Object[]{childProdId,"A"});

		for(Map<String, Object> measurements : dbMeasurementsList) {
			String groupId=String.valueOf(measurements.get("MATERIAL_GROUP_ID")== null ? "0" : measurements.get("MATERIAL_GROUP_ID"));
			if(groupId.equalsIgnoreCase("NA")){
				groupId="0";
			}
			sb = sb.append(String.valueOf(measurements.get("MEASUREMENT_ID"))+"_"+String.valueOf(measurements.get("NAME"))+"_"+groupId+"|");
		log.info("group id"+groupId);
		}			
		return sb.toString();
	}

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
	public Map<String, String> getOtherCharges() {

		Map<String, String> otherCharges = null;
		List<Map<String, Object>> dbOtherChargesSlabList = null;

		otherCharges = new TreeMap<String, String>();

		String getOtherCharges = "SELECT CHARGE_ID, CHARGE_NAME FROM SUMADHURA_TRNS_OTHR_CHRGS_MST WHERE STATUS='A'";
		log.debug("Query to fetch other charges = "+getOtherCharges);

		dbOtherChargesSlabList = jdbcTemplate.queryForList(getOtherCharges, new Object[]{});

		for(Map<String, Object> otherChargesSlabs : dbOtherChargesSlabList) {
			otherCharges.put(String.valueOf(otherChargesSlabs.get("CHARGE_ID")).trim(), String.valueOf(otherChargesSlabs.get("CHARGE_NAME")).trim());
		}
		return otherCharges;
	}

	public int getIndentEntrySequenceNumber() {

		int indentEntrySeqNum = 0;

		indentEntrySeqNum = jdbcTemplate.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");

		return indentEntrySeqNum;
	}


	//pavan changed method
	public int insertInvoiceData(int indentEntrySeqNum, IndentReceiveDto objIndentReceiveDto ) {

		//IndentReceiveDto objIndentReceiveDto = null;

		String indentType="IN";
		if(objIndentReceiveDto.getIndentType()!=null){
			 indentType="INU";
		}
		
		int result = 0;
		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		
		String invoiceInsertionQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, INVOICE_ID, ENTRY_DATE, VENDOR_ID, TOTAL_AMOUNT, INDENT_TYPE, NOTE, RECEIVED_OR_ISSUED_DATE, INVOICE_DATE," +
		" TRANSPORTERNAME,EWAYBILLNO,VEHICLENO,PODATE,DC_NUMBER, STATE,PO_ID, GRN_NO,INDENT_CREATION_ID,REQUESTER_ID,REQUESTER_NAME)" +
		" VALUES (?, ?, ?,?, sysdate, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		log.debug("Query for invoice insertion = "+invoiceInsertionQry);

		result = jdbcTemplate.update(invoiceInsertionQry, new Object[] {indentEntrySeqNum,
				objIndentReceiveDto.getStrUserId(), objIndentReceiveDto.getStrSiteId(), objIndentReceiveDto.getStrInvoiceNo(),
				objIndentReceiveDto.getStrVendorId(), objIndentReceiveDto.getTotalAmnt(), indentType, objIndentReceiveDto.getStrRemarks(),
				objIndentReceiveDto.getStrReceiveDate()+" "+sdf.format(cal1.getTime()), objIndentReceiveDto.getStrInoviceDate()+" "+sdf.format(cal1.getTime()),
				objIndentReceiveDto.getTransporterName(),objIndentReceiveDto.geteWayBillNo(),objIndentReceiveDto.getVehileNo(),objIndentReceiveDto.getPoDate(),
				objIndentReceiveDto.getDcNo(),objIndentReceiveDto.getState(),objIndentReceiveDto.getPoNo(),objIndentReceiveDto.getGrnNumber(),objIndentReceiveDto.getIndentNumber(),
				objIndentReceiveDto.getRequesterId(),objIndentReceiveDto.getRequesterName()
		});
		//if(!indentType.equals("INU"))//ACP
		if(result>0 && !objIndentReceiveDto.getPayment_Req_Date().equals("") && objIndentReceiveDto.getPayment_Req_Date()!=null && !objIndentReceiveDto.getPayment_Req_Date().equals("0") && !objIndentReceiveDto.getPayment_Req_Date().equals("null")){
			String update = "UPDATE INDENT_ENTRY SET SYS_PAYMENT_REQ_DATE =? WHERE INDENT_ENTRY_ID =?";
			log.debug("Query for invoice insertion = "+update);

			result = jdbcTemplate.update(update, new Object[] {objIndentReceiveDto.getPayment_Req_Date()+" "+sdf.format(cal1.getTime()),indentEntrySeqNum});
		}



		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> insertInvoiceData() --> Result = "+result);

		return result;
	}

	public String getVendorInfo(String vendName) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbVendorList = null;		


		vendName = vendName.replace("$$", "&");

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


	public int insertIndentReceiveData(int indentEntrySeqNum,int intIndentEntryDetailsSeqNo, IndentReceiveDto irdto, String userId, String siteId,int intPriceListSeqNo) {

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		//String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, PRICE, BASIC_AMOUNT, TAX, HSN_CODE, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT, ENTRY_DATE,EXPIRY_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?)";
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME,  ENTRY_DATE,EXPIRY_DATE,REMARKS,PRICE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?)";
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
				//	irdto.getHsnCd(), 
				//	irdto.getTaxAmnt(), 
				//.getAmntAfterTax(), 
				//	irdto.getOtherOrTranportChrgs(), 
				//	irdto.getTaxOnOtherOrTranportChrgs(), 
				//	irdto.getOtherOrTransportChrgsAfterTax(), 
				//	irdto.getTotalAmnt(),
				irdto.getExpiryDate(),irdto.getStrRemarks(),intPriceListSeqNo
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



	//pavan changed the query
	/*public int insertIndentReceiveData(int indentEntrySeqNum, IndentReceiveDto irdto, String userId, String siteId) {

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		//String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, PRICE, BASIC_AMOUNT, TAX, HSN_CODE, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT, INVOICE_OTHER_CHG, ENTRY_DATE,PO_NUMBER,DC_NUMBER,EXPIRY_DATE,PODATE,EWAYBILLNO,VEHICLENO,TRANSPORTERNAME) VALUES (INDENT_ENTRY_DETAILS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?,?, ?, ?,?)";
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, PRICE, BASIC_AMOUNT, TAX, HSN_CODE, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT, INVOICE_OTHER_CHG, ENTRY_DATE,EXPIRY_DATE) VALUES (INDENT_ENTRY_DETAILS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,sysdate,?)";
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[] { 
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
				irdto.getPrice(), 
				irdto.getBasicAmnt(), 
				irdto.getTax(), 
				irdto.getHsnCd(), 
				irdto.getTaxAmnt(), 
				irdto.getAmntAfterTax(), 
				irdto.getOtherOrTranportChrgs(), 
				irdto.getTaxOnOtherOrTranportChrgs(), 
				irdto.getOtherOrTransportChrgsAfterTax(), 
				irdto.getTotalAmnt(), 
				irdto.getOtherChrgs(),
				irdto.getExpiryDate()
		}
		);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentReceiveData() --> Result = "+result);

		return result;
	}
	 */

	public int updateIndentAvalibility(IndentReceiveDto irdto, String siteId) {

		List<Map<String, Object>> dbProductDetailsList = null;
		int result = 0;
		String availability_Id="";
		double receive_Form_Quantity=0.0;
		double quantity=0.0;
		
		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {

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
		log.debug("Query for update indent avalibility = "+updateIndentAvalibilityQry);

		result = jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
		log.debug("Result = "+result);
		logger.info("IndentReceiveDao --> updateIndentAvalibility() --> Result = "+result);
		return result;
	}

	public void updateIndentAvalibilityWithNewIndent(IndentReceiveDto irdto, String siteId) {

		int result = 0;		

		String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID) VALUES (INDENT_AVAILABILITY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
		log.debug("Query for new indent entry in indent availability = "+requesterQry);

		result = jdbcTemplate.update(requesterQry, new Object[] {
				irdto.getProdId(), 
				irdto.getSubProdId(), 
				irdto.getChildProdId(), 
				irdto.getQuantity(), 
				irdto.getMeasurementId(), 
				siteId
		}
		);
		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> updateIndentAvalibilityWithNewIndent() --> Result = "+result);
	}

	@Override
	public String getProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, String siteId) {

		List<Map<String, Object>> dbProductAvailabilityList = null;		

		log.debug("Product Id = "+prodId+", Sub Product Id = "+subProductId+", Child Product Id = "+childProdId+", Measurement Id = "+measurementId+" and Site Id = "+siteId);

		String indentAvaQry = "SELECT PRODUT_QTY FROM INDENT_AVAILABILITY WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND MESURMENT_ID = ? AND SITE_ID = ?";
		log.debug("Query to fetch product availability = "+indentAvaQry);

		dbProductAvailabilityList = jdbcTemplate.queryForList(indentAvaQry, new Object[] {
				prodId,
				subProductId,
				childProdId,
				measurementId,
				siteId
		}
		);

		String qty = "";
		for(Map<String, Object> availableQuantity : dbProductAvailabilityList) {
			String measurementName = getMeasurementName(measurementId);
			qty = String.valueOf(availableQuantity.get("PRODUT_QTY")+" "+measurementName);
			break;
		}
		return qty;
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

	// written by Madhu on 27-July-2017 start
	@Override
	public void saveReciveDetailsIntoSumduraPriceList(IndentReceiveDto irdto,
			String invoiceNumber, String siteId, String id,int entryDetailssequenceId,int intPriceListSeqNo,String typeOfPurchase) {
		String query = "";
		Double perPiceAmt = 0.0;
		Double totalAmt = 0.0;
		Double quantity = 0.0;
		String strQuantity="";
		//String entryDetailssequenceId = getEntryDetailsSequenceNumber();


		totalAmt = Double.valueOf(irdto.getTotalAmnt());
		BigDecimal bigDecimal = new BigDecimal(irdto.getQuantity());
		strQuantity=String.valueOf(bigDecimal.setScale(2,RoundingMode.CEILING));
		quantity = Double.valueOf(strQuantity);
		//perPiceAmt = Double.valueOf(totalAmt/quantity);

		if(quantity > 0){
			perPiceAmt = Double.valueOf(totalAmt/quantity);
		}else{
			perPiceAmt = 0.0;
		}
		query = "INSERT INTO SUMADHURA_PRICE_LIST(PRICE_ID, INVOICE_NUMBER, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, INDENT_AVAILABILITY_ID, STATUS, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES, SITE_ID, UNITS_OF_MEASUREMENT,INDENT_ENTRY_DETAILS_ID,CREATED_DATE"
			+ "   , AMOUNT_PER_UNIT_BEFORE_TAXES, BASIC_AMOUNT, TAX, TAX_AMOUNT, AMOUNT_AFTER_TAX, OTHER_CHARGES, TAX_ON_OTHER_TRANSPORT_CHG, OTHER_CHARGES_AFTER_TAX, TOTAL_AMOUNT,HSN_CODE,RECEVED_QTY,TYPE_OF_PURCHASE) VALUES"
			+ "    (?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?,?)";
		log.debug("   Query for save recive details into sumadhura price list table " + query);

		jdbcTemplate.update(query, new Object[] {intPriceListSeqNo,invoiceNumber, irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(),id , "A", 
				strQuantity, perPiceAmt, siteId, irdto.getMeasurementId(),entryDetailssequenceId, irdto.getDate(),
				irdto.getPrice(),  irdto.getBasicAmnt(),  irdto.getTax(),  irdto.getTaxAmnt(),  irdto.getAmntAfterTax(), 
				irdto.getOtherOrTranportChrgs(),  irdto.getTaxOnOtherOrTranportChrgs(),  irdto.getOtherOrTransportChrgsAfterTax(), 
				irdto.getTotalAmnt(),irdto.getHsnCd(),irdto.getQuantity(),typeOfPurchase


		});


	}

	public int getIndentEntryDtails_SeqNumber(){
		int intIndentEntryDetailsSeqNo = getEntryDetailsSequenceNumber();
		return intIndentEntryDetailsSeqNo;
	}

	public int getPriceList_SeqNumber(){

		String query = "select SUMADHU_PRICE_LIST.NEXTVAL from dual";

		return jdbcTemplate.queryForInt(query);
		//return getPriceList_SeqNumber;
	}

	@Override
	public String getIndentAvailableId(IndentReceiveDto irdto, String site_id) {

		String query = "SELECT INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND MESURMENT_ID = ? AND SITE_ID = ? ";
		return String.valueOf(jdbcTemplate.queryForInt(query, new Object[] {
				irdto.getProdId(), 
				irdto.getSubProdId(), 
				irdto.getChildProdId(), 
				irdto.getMeasurementId(), 
				site_id}));

	}

	@Override
	public String getProductAvailabilitySequenceNumber() {

		String query = "SELECT INDENT_AVAILABILITY_SEQ.NEXTVAL FROM DUAL";
		return String.valueOf(jdbcTemplate.queryForInt(query));
	}
	@Override
	public int getEntryDetailsSequenceNumber() {

		//String query = "SELECT MAX(INDENT_ENTRY_DETAILS_ID) FROM INDENT_ENTRY_DETAILS";
		String query = "select INDENT_ENTRY_DETAILS_SEQ.NEXTVAL from dual";

		return jdbcTemplate.queryForInt(query);
	}
	// written by Madhu on 27-July-2017 end	


	@Override
	public String getindentEntrySerialNo(String reciveDate) throws ParseException {
		int indentEntrySeqNum = 0;
		String fianlVal = "";
		indentEntrySeqNum = jdbcTemplate.queryForInt("SELECT SerialNo_ID_SEQ.NEXTVAL FROM DUAL");
		boolean flag=true;
		int monthNo=0;
		int year=0;
		try {
			if(reciveDate.length()>12){
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
				Date formattedDate = df.parse(reciveDate);
				DateFormat df2 = new SimpleDateFormat("dd-MM-yy");
				String formattedDate2 = df2.format(formattedDate);
				String[] strArrayReciveDate=formattedDate2.split("-");
				monthNo=Integer.valueOf(strArrayReciveDate[1]);
				year=Integer.valueOf(strArrayReciveDate[2]);
				flag=false;
			}
		} catch (Exception e) {
			flag=true;
			log.debug(e.getMessage());
		}
	
		if(flag){
			try {
				String[] tempArray=reciveDate.split("-");	
				monthNo=Integer.valueOf(tempArray[1]);
				year=Integer.valueOf(tempArray[2]);
				flag=false;
			} catch (Exception e) {
				flag=true;
				log.debug(e.getMessage());
			}
		}
		
		String grnNoYear="";
		if(flag){
			DateFormat df = new SimpleDateFormat("dd-MMM-yy"); 
			Date formattedDate = df.parse(reciveDate);
			DateFormat df2 = new SimpleDateFormat("dd-MM-yy");
			String formattedDate2 = df2.format(formattedDate);
			String[] strArrayReciveDate=formattedDate2.split("-");
			monthNo=Integer.valueOf(strArrayReciveDate[1]);
			year=Integer.valueOf(strArrayReciveDate[2]);
		}
		
		if(monthNo<=3){
			System.out.println("The current Month is less than the april");
			grnNoYear+=(year-1)+"-"+year;
		}else if(monthNo>=4){
			System.out.println("The current Month is greater than the april");
			grnNoYear+=(year)+"-"+(year+1);
		}
		
		if (indentEntrySeqNum >9) {
			fianlVal = "/"+indentEntrySeqNum+"/"+grnNoYear;
		} else {
			fianlVal = "/0"+indentEntrySeqNum+"/"+grnNoYear;
		}
		logger.info(fianlVal);
		return fianlVal;
	}

	public static <K, V> Map<K, V> printMap(Map<K, V> map) {
		Map<K, V> mapObje = new TreeMap<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			mapObje.put( entry.getKey(), entry.getValue());

		}

		return mapObje;
	}


	// 11-AUG MADHU START
	@Override
	public void saveReciveDetailsIntoSumadhuraCloseBalance(IndentReceiveDto irdto, String siteId) {

		String query = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		List<Map<String, Object>> dbClosingBalancesList = null;
		query = "SELECT CLOSING_BALANCE_ID,QUANTITY, PRICE, TOTAL_AMOUNT FROM SUMADHURA_CLOSING_BALANCE WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) = TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
		dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
		if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
			query = "";
			query = "UPDATE SUMADHURA_CLOSING_BALANCE SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"'WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
			jdbcTemplate.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

		} else {
			query = "";
			query = "SELECT CLOSING_BALANCE_ID,QUANTITY, PRICE, TOTAL_AMOUNT FROM SUMADHURA_CLOSING_BALANCE WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) <= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')  AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				for (Map<String, Object> prods : dbClosingBalancesList) {

					quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
					totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
				}
			}

			query = "";
			query = "INSERT INTO SUMADHURA_CLOSING_BALANCE(CLOSING_BALANCE_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE,PRICE,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMADHURA_CLOSING_BALANCE_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity + Double.parseDouble(irdto.getQuantity()),siteId, irdto.getAmntAfterTax(),Double.parseDouble(irdto.getTotalAmnt())+totalAmt,irdto.getDate(), irdto.getMeasurementId()});
			query = "";
			query = "UPDATE SUMADHURA_CLOSING_BALANCE SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"'WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
			jdbcTemplate.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

		} 

	}

	@Override
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
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				query = "";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"'WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				jdbcTemplate.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

			} else {
				query = "";
				String dateTime = "";
				query = "SELECT DATE_AND_TIME, CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) <= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')  AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
				dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {
						//dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
						quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
						totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
					}
				}

				query = "";
				query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
				jdbcTemplate.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity + Double.parseDouble(irdto.getQuantity()),siteId,Double.parseDouble(irdto.getTotalAmnt())+totalAmt,irdto.getDate(), irdto.getMeasurementId()});
				query = "";

				query = "SELECT TO_CHAR(TRUNC(DATE_AND_TIME)) AS DATE_AND_TIME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"','dd-MM-yy') AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND ROWNUM <= 1";
				dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});

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
							jdbcTemplate.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity, siteId,totalAmt,yourDate1, irdto.getMeasurementId()});

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
						jdbcTemplate.update(query,new Object[] {irdto.getProdId(),irdto.getSubProdId(), irdto.getChildProdId(),quantity ,siteId,totalAmt,yourDate1, irdto.getMeasurementId()});
					}
				}
				query="";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity().trim()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmnt().trim()+"' WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				jdbcTemplate.update(query, new Object[] {irdto.getProdId(), irdto.getSubProdId(), irdto.getChildProdId(), siteId,  irdto.getMeasurementId()});
			}
		}

	}
	// 11-AUG MADHU END



	@Override
	public void saveTransactionDetails(String invoiceNum, String transactionId, String gstId,
			String gstAmount, String totAmtAfterGSTTax,
			String transactionInvoiceId, String transAmount, String siteId, String indentEntrySeqNum) {

		String query = "INSERT INTO SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID, TRANSPORT_ID, TRANSPORT_GST_PERCENTAGE, TRANSPORT_GST_AMOUNT, TOTAL_AMOUNT_AFTER_GST_TAX, TRANSPORT_INVOICE_ID, INDENT_ENTRY_INVOICE_ID, DATE_AND_TIME, TRANSPORT_AMOUNT, SITE_ID, INDENT_ENTRY_ID) VALUES(TRANS_SEQ_ID.NEXTVAL,?,?,?,?,?,?,SYSDATE,?,?,?)";
		jdbcTemplate.update(query, new Object[] {transactionId, gstId, gstAmount, totAmtAfterGSTTax, transactionInvoiceId,invoiceNum, transAmount,siteId, indentEntrySeqNum});

	}



	public int getInvoiceCount(String  strInvoiceNmber, String vendorId,String strReceiveStartDate, String receiveDate){
		
		List<Map<String, Object>> dbProductDetailsList = null;
		int receivedate=0;
		int result=0;
		int strMonth=0;
		String receiveDate2="";
		//boolean value=false;
		try{
			Date receiveDate1;
			receiveDate1 = new SimpleDateFormat("dd-MMM-yy").parse(receiveDate);
			 receiveDate2 = new SimpleDateFormat("yy").format(receiveDate1);
			 strMonth=Integer.valueOf(new SimpleDateFormat("MM").format(receiveDate1));	
			
		String sql="select to_char(RECEIVED_OR_ISSUED_DATE, 'MM') as receivedate from  INDENT_ENTRY where INVOICE_ID = ? and VENDOR_ID = ?  and TRUNC(RECEIVED_OR_ISSUED_DATE,'yy') = TRUNC(to_date(?,'dd-MM-yy'),'yy')";
			
		dbProductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {strInvoiceNmber, vendorId,receiveDate});	
			
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				receivedate =Integer.valueOf(prods.get("receivedate") == null ? "0" : prods.get("receivedate").toString());

				if(receivedate>3 && strMonth>3){
					result=1;
				}/*else{
					result=1;
				}*/

			}
		}				
		else{
			
			//Date checkDate;
			
				/*receiveDate1 = new SimpleDateFormat("dd-MMM-yy").parse(receiveDate);
				String receiveDate2 = new SimpleDateFormat("yy").format(receiveDate1);
				int strMonth=Integer.valueOf(new SimpleDateFormat("MM").format(receiveDate1));*/
				
				String year=String.valueOf(Integer.valueOf(receiveDate2)-1);
				receiveDate=receiveDate.replace(receiveDate2,year);
				
				if(strMonth<=3){
				String query="select to_char(RECEIVED_OR_ISSUED_DATE, 'MM') as receivedate from  INDENT_ENTRY where INVOICE_ID = ? and VENDOR_ID = ?  and TRUNC(RECEIVED_OR_ISSUED_DATE,'yy') = TRUNC(to_date(?,'dd-MM-yy'),'yy')";

				dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {strInvoiceNmber, vendorId,receiveDate});	
				if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
					for (Map<String, Object> prods : dbProductDetailsList) {
						receivedate =Integer.valueOf(prods.get("receivedate") == null ? "0" : prods.get("receivedate").toString());
						if(receivedate>3){
							result=1;
						}
					}
				}
				}//first if
			}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		
		//String query  = "select count(1) from  INDENT_ENTRY where INVOICE_ID = ? and VENDOR_ID = ? AND SITE_ID=? and TRUNC(RECEIVED_OR_ISSUED_DATE,'yy') = TRUNC(to_date(?,'dd-MM-yy'),'yy')";
		//int intCount =    jdbcTemplate.queryForInt(query, new Object[] {strInvoiceNmber, vendorId, siteId,receiveDate});
		
		return result;
	}

	// 11-AUG MADHU END

	public int getInvoiceSaveCount(String  strInvoiceNmber, String vendorId,String receiveDate){

		int intCount =  0;
		try{

			String query  = "select INDENT_ENTRY_ID from  INDENT_ENTRY where INVOICE_ID = ? and VENDOR_ID = ? and TRUNC(RECEIVED_OR_ISSUED_DATE) = to_date(?,'dd-MM-yy')";
			intCount =    jdbcTemplate.queryForInt(query, new Object[] {strInvoiceNmber, vendorId, receiveDate});
		}catch(Exception e){
			intCount = 0;
			e.printStackTrace();
		}

		return intCount;
	}


	@Override
	public List<ViewIndentIssueDetailsBean> getSiteWiseInvoiceDetails(String fromDate, String toDate, String siteId, String selectIndentType) {

		String query = "";
		String convertpodate = "";
		JdbcTemplate template = null;
		String total="";
		String strNumber="";
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if(siteId.equals("All")){
				if(selectIndentType==null){
						selectIndentType="IN";	
				}else if(selectIndentType.length()==0){
						selectIndentType="IN";	
				}
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT  IE.INVOICE_ID, min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME, min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE,min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD,SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"'   AND IE.VENDOR_ID=VD.VENDOR_ID AND S.SITE_ID = IE.SITE_ID AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT  IE.INVOICE_ID,min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME,min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE, min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD , SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"'  AND IE.VENDOR_ID=VD.VENDOR_ID AND AND S.SITE_ID = IE.SITE_ID TRUNC(IE.INVOICE_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT  IE.INVOICE_ID,min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME,min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE, min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD ,SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"'  AND IE.VENDOR_ID=VD.VENDOR_ID AND S.SITE_ID = IE.SITE_ID AND TRUNC(IE.INVOICE_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
				}
			}else{
				if(selectIndentType==null){
					selectIndentType="IN";	
				}else if(selectIndentType.length()==0){
						selectIndentType="IN";	
				}
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					
					query = "SELECT  IE.INVOICE_ID,min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME, min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE,min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD,SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"' AND IE.SITE_ID='"+siteId+"'  AND IE.VENDOR_ID=VD.VENDOR_ID AND S.SITE_ID = IE.SITE_ID AND TRUNC(IE.INVOICE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT  IE.INVOICE_ID,min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME,min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE, min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD,SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND S.SITE_ID = IE.SITE_ID AND TRUNC(IE.INVOICE_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT  IE.INVOICE_ID,min(IE.INDENT_ENTRY_ID) as INDENT_ENTRY_ID,min(S.SITE_NAME) as SITE_NAME,min(S.SITE_ID) as SITE_ID,min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE, min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID,min(IE.PO_ID) as PO_ID,min(IE.PODATE) as PODATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD,SITE S WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='"+selectIndentType+"' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND S.SITE_ID = IE.SITE_ID AND TRUNC(IE.INVOICE_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.VENDOR_ID,IE.SITE_ID,TRUNC( IE.INVOICE_DATE,'yy') order by INVOICE_DATE";
				}
			}



			dbIndentDts = template.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> prods : dbIndentDts) {
				serialNo++;
				indentObj = new ViewIndentIssueDetailsBean();
				indentObj.setSerialNo(serialNo);
				indentObj.setRequesterId(prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString());
				//this three line was commented in CUG but in UAT this are uncommented
				strNumber=prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString();
				if(strNumber.contains("&")){strNumber=strNumber.replace('&','@');}
				indentObj.setStrNumber(strNumber);
				
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				String receviedDate=prods.get("RECEVED_DATE")==null ? "" : prods.get("RECEVED_DATE").toString();
				String poDate = prods.get("PODATE")==null ? "" : prods.get("PODATE").toString();
				String strIndentEntryId = prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString();
				String strSiteId = prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();



				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String convertreceive_date = "";
				String convertreceive_time = "";
				//	String convertinvoice_date = "";
				try{

					
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");


					if(!poDate.equals("")){

						Date po_date = dt.parse(poDate);
						poDate = dt1.format(po_date);
					}
					if(!invoicedate.equals("")){
						Date invoice_date = dt.parse(invoicedate);
						invoicedate = dt1.format(invoice_date);
						}
					if(!receviedDate.equals("")){
						Date receive_date = dt.parse(receviedDate);
						receviedDate = dt1.format(receive_date);
						convertreceive_time = time1.format(receive_date);
					}
					// System.out.println("Time: " + time1.format(receviedDate));
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
				//System.out.println("the date is "+date2);
				indentObj.setIndentEntryId(strIndentEntryId);
				indentObj.setStrInvoiceDate(invoicedate);
				indentObj.setDate(receviedDate);
				indentObj.setTime(convertreceive_time);
				indentObj.setPoDate(poDate);
				indentObj.setSiteId(strSiteId);
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setPoNo(prods.get("PO_ID")==null ? "" : prods.get("PO_ID").toString());


				String date=prods.get("RECEVED_DATE")==null ? "" : prods.get("RECEVED_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setReceivedDate(date);
				String strInvoiceNo = prods.get("INVOICE_ID") == null ? "" : prods.get("INVOICE_ID").toString();
				String vendorId = prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString();
				String invoiceDate = prods.get("INVOICE_DATE") == null ? "" : prods.get("INVOICE_DATE").toString();
				if(!invoicedate.equals("")){
					invoiceDate = DateUtil.dateConversion(invoiceDate);
				}
				//System.out.println("invoiceDate: "+invoiceDate);
				String query1 = "select sum(TOTAL_AMOUNT) as SUM_TOTAL_AMOUNT from INDENT_ENTRY where INVOICE_ID = ? AND VENDOR_ID = ?  "+ " AND trunc(INVOICE_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				List<Map<String, Object>> dbIndentDts1 = null;
			//	System.out.println("invoice No : ---> " + strInvoiceNo);
				dbIndentDts1 = template.queryForList(query1, new Object[]{ strInvoiceNo,vendorId,invoiceDate});

				for(Map<String, Object> prods1 : dbIndentDts1) {
					total = prods1.get("SUM_TOTAL_AMOUNT")==null ? "" : prods1.get("SUM_TOTAL_AMOUNT").toString();

					double converttotal=0.0;
					try {
						converttotal = Double.parseDouble(total);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					indentObj.setStrTotalAmount(format.format(converttotal).replaceAll("Rs",""));
				}
				list.add(indentObj);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			log.debug("Exception = "+ex.getMessage());
			logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}


	@Override
	public List<Map<String, Object>> getListOfActivePOs(String site_id){

		List<Map<String, Object>> poList = null;
		String sql="";
		if(site_id.equals("998")){
			 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY,SPE.PAYMENT_REQ_DAYS "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
				+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
				+ " and SPE.SITE_ID not in ('996')";

			
		}else{
		
		 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY,SPE.PAYMENT_REQ_DAYS "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " and SPE.SITE_ID = '"+site_id+"'";
		}
		poList = jdbcTemplate.queryForList(sql, new Object[] {});
		return poList;

	}

	@Override
	public List<Map<String, Object>> getListOfActiveMarketingPOs(String site_id,String status){

		List<Map<String, Object>> poList = null;
		String sql="";
		if(status.equalsIgnoreCase("CNL")){
		 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SPE.PO_ENTRY_ID,S.SITE_NAME,TO_CHAR(SPE.PO_DATE,'dd-mm-yyyy') as PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY,SCP.ENTRY_DATE "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_CANCEL_PO SCP "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = '"+status+"' AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID"
			+ " and SPE.SITE_ID = '"+site_id+"'";
		}else{
			 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SPE.PO_ENTRY_ID,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = '"+status+"' "
				+ " and SPE.SITE_ID = '"+site_id+"'";
		}
		poList = jdbcTemplate.queryForList(sql, new Object[] {});
		return poList;

	}
	@Override
	public List<ProductDetails> getPODetails(String poNumber, String reqSiteId) {
		JdbcTemplate template = null;
		String marketingDept=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String query ="";
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbPODts = null;
		List<Map<String, Object>> vendor = null;
		boolean isMarketing=false;
		String typePo="";
		String poDetails="";
		try{
		if(marketingDept.equals(reqSiteId)){
			isMarketing=true;
			query = "select VD.VENDOR_ID,VD.VENDOR_NAME,VD.GSIN_NUMBER,VD.ADDRESS,SPE.INDENT_NO,VD.STATE,SPE.SUBJECT,SPE.PO_DATE,S.SITE_ID,S.SITE_NAME," 
				+ " SPE.DELIVERY_DATE,SPE.PAYMENT_REQ_DAYS,SPE.VERSION_NUMBER,SPE.PO_ISSUE_DATE,SPE.REFFERENCE_NO,SPE.PREPARED_BY,SPE.PO_ENTRY_ID,SPE.TOTAL_AMOUNT,MPAWD.TYPE_OF_PO,MPAWD.TYPE_OF_PO_DETAILS "
				+ " from VENDOR_DETAILS VD,SITE S,SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN MRKT_PO_AREA_WISE_DTLS MPAWD ON MPAWD.PO_ENTRY_ID=SPE.PO_ENTRY_ID "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID "
				+ " AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' and SPE.PO_STATUS='A'";
			
		}else{
			query = "select VD.VENDOR_ID,VD.VENDOR_NAME,VD.GSIN_NUMBER,VD.ADDRESS,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,VD.STATE,SPE.SUBJECT,SPE.PO_DATE,S.SITE_ID,S.SITE_NAME," 
			+ " SIC.CREATE_DATE,SPE.DELIVERY_DATE,SPE.PAYMENT_REQ_DAYS,SPE.VERSION_NUMBER,SPE.PO_ISSUE_DATE,SPE.REFFERENCE_NO,SPE.PREPARED_BY,SPE.PO_ENTRY_ID,SPE.TOTAL_AMOUNT,MPAWD.TYPE_OF_PO,MPAWD.TYPE_OF_PO_DETAILS "
			+ " from VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN MRKT_PO_AREA_WISE_DTLS MPAWD ON MPAWD.PO_ENTRY_ID=SPE.PO_ENTRY_ID "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID "
			+ " AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' and SIC.INDENT_CREATION_ID = SPE.INDENT_NO  and SPE.PO_STATUS='A'";
		}
		dbPODts = template.queryForList(query, new Object[]{});

		for(Map<String, Object> prods : dbPODts) {
			ProductDetails pd = new ProductDetails();
			String vendorId = prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString();
			String state = getState(reqSiteId, vendorId);
			pd.setVendorId(prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString());
			pd.setVendorName(prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString());
			pd.setStrGSTINNumber(prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString());
			pd.setVendorAddress(prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString());
			pd.setIndentNo(prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString());
			
			pd.setSite_Id(prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString());
			pd.setSiteName(prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString());
			pd.setPayment_Req_days(prods.get("PAYMENT_REQ_DAYS") == null ? "" : prods.get("PAYMENT_REQ_DAYS").toString());
			pd.setState(state);//(prods.get("STATE") == null ? "" : prods.get("STATE").toString());
			pd.setSubject(prods.get("SUBJECT") == null ? "" : prods.get("SUBJECT").toString());
			pd.setPoEntryId(Integer.valueOf(prods.get("PO_ENTRY_ID") == null ? "0" : prods.get("PO_ENTRY_ID").toString()));
			pd.setFinalamtdiv(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
			
			pd.setVersionNo(prods.get("VERSION_NUMBER") == null ? "" : prods.get("VERSION_NUMBER").toString());
			pd.setRefferenceNo(prods.get("REFFERENCE_NO") == null ? "" : prods.get("REFFERENCE_NO").toString());
			pd.setStrPoPrintRefdate(prods.get("PO_ISSUE_DATE") == null ? "" : prods.get("PO_ISSUE_DATE").toString());
			pd.setPreparedBy(prods.get("PREPARED_BY") == null ? "" : prods.get("PREPARED_BY").toString());
			typePo=prods.get("TYPE_OF_PO") == null ? "" : prods.get("TYPE_OF_PO").toString();
			pd.setType_Of_Po(typePo);
			poDetails=prods.get("TYPE_OF_PO_DETAILS") == null ? "" : prods.get("TYPE_OF_PO_DETAILS").toString();
			
			
			String strDeliveryDate = prods.get("DELIVERY_DATE") == null ? "-" : prods.get("DELIVERY_DATE").toString();
			String poDate = prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			if(isMarketing && typePo.equalsIgnoreCase("SiteWise")){
				String query1="SELECT SITE_NAME FROM SITE where SITE_ID='"+poDetails+"'";
				vendor = template.queryForList(query1, new Object[]{});
				for(Map<String, Object> prod : vendor) {
					poDetails=prod.get("SITE_NAME") == null ? "" : prod.get("SITE_NAME").toString();
				}
				
			}
			pd.setType_Of_Po_Details(poDetails);
			if(!isMarketing){
			pd.setSiteWiseIndentNo(prods.get("SITEWISE_INDENT_NO") == null ? "0" : prods.get("SITEWISE_INDENT_NO").toString());
			String strCreateDate = prods.get("CREATE_DATE") == null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			
			Date createDate = null;
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			pd.setStrCreateDate(strCreateDate);
			}
			Date date = null;
			Date deliveryDate = null;
			try {
				date = format.parse(poDate);
				if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){
					deliveryDate = format.parse(strDeliveryDate);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			poDate = new SimpleDateFormat("dd-MMM-yy").format(date);
			if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){
				strDeliveryDate = new SimpleDateFormat("dd-MMM-yy").format(deliveryDate);
			}
			pd.setPoDate(poDate);
			pd.setStrDeliveryDate(strDeliveryDate);
			list.add(pd);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public List<ProductDetails> getMarketingPODetails(String poNumber, String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbPODts = null;
		try{
		String query = "select VD.VENDOR_ID,VD.VENDOR_NAME,VD.GSIN_NUMBER,VD.ADDRESS,SPE.PO_DATE,S.SITE_ID,S.SITE_NAME,SPE.DELIVERY_DATE,SPE.PAYMENT_REQ_DAYS "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID "
			+ " AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' and SPE.PO_STATUS='A' ";
		dbPODts = template.queryForList(query, new Object[]{});

		for(Map<String, Object> prods : dbPODts) {
			ProductDetails pd = new ProductDetails();
			pd.setStrPoEntryId(prods.get("PO_ENTRY_ID") == null ? "" : prods.get("PO_ENTRY_ID").toString());
			pd.setVendorId(prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString());
			pd.setVendorName(prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString());
			pd.setStrGSTINNumber(prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString());
			pd.setVendorAddress(prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString());
			pd.setIndentNo(prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString());
			pd.setPayment_Req_days(prods.get("PAYMENT_REQ_DAYS") == null ? "" : prods.get("PAYMENT_REQ_DAYS").toString());
			//pd.setSiteWiseIndentNo(prods.get("SITEWISE_INDENT_NO") == null ? "0" : prods.get("SITEWISE_INDENT_NO").toString());
			pd.setSite_Id(prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString());
			pd.setSiteName(prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString());
			String strDeliveryDate = prods.get("DELIVERY_DATE") == null ? "" : prods.get("DELIVERY_DATE").toString();
			String poDate = prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			
			//String strCreateDate = prods.get("CREATE_DATE") == null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			
			//Date createDate = null;
			//createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
			//strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			//pd.setStrCreateDate(strCreateDate);
			Date date = null;
			Date deliveryDate = null;
			try {
				date = format.parse(poDate);
				if(StringUtils.isNotBlank(strDeliveryDate)){
					deliveryDate = format.parse(strDeliveryDate);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			poDate = new SimpleDateFormat("dd-MMM-yy").format(date);
			if(StringUtils.isNotBlank(strDeliveryDate)){
				strDeliveryDate = new SimpleDateFormat("dd-MMM-yy").format(deliveryDate);
			}
			pd.setPoDate(poDate);
			pd.setStrDeliveryDate(strDeliveryDate);
			list.add(pd);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public List<ProductDetails> getProductDetailsLists(String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbPODts = null;
		double doublePOTotalAmount = 0;
		double doubleTotalAmount = 0;
		String groupId="";
		String availablequantity="";
		String boqQuantity="";
		/*String query = "SELECT P.PRODUCT_ID,SP.SUB_PRODUCT_ID,CP.CHILD_PRODUCT_ID,MST.MEASUREMENT_ID,"
			+ " P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"
			+ " SPED.PO_QTY,SPED.PRICE,SPED.BASIC_AMOUNT,"
			+ " SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.HSN_CODE,SPED.TAX,SPED.TAX_AMOUNT,"
			+ " SPED.AMOUNT_AFTER_TAX,SPED.OTHER_CHARGES,SPED.TAX_ON_OTHER_TRANSPORT_CHG,"
			+ " SPED.OTHER_CHARGES_AFTER_TAX,SPED.TOTAL_AMOUNT,SPED.INDENT_CREATION_DTLS_ID,SPED.PO_ENTRY_DETAILS_ID"
			+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,"
			+ " PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST WHERE SPED.PRODUCT_ID = P.PRODUCT_ID "
			+ " AND SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID AND "
			+ " SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND "
			+ " SPED.MEASUR_MNT_ID = MST.MEASUREMENT_ID AND"
			+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"'";
		dbPODts = template.queryForList(query, new Object[]{});*/
		
		String query = "SELECT P.PRODUCT_ID,SP.SUB_PRODUCT_ID,CP.CHILD_PRODUCT_ID,MST.MEASUREMENT_ID,"
		+ " P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,CP.MATERIAL_GROUP_ID,"
		+ " SPED.PO_QTY,SPED.RECEIVED_QUANTITY,SPED.PRICE,SPED.BASIC_AMOUNT,SPED.VENDOR_PRODUCT_DESC,"
		+ " SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.HSN_CODE,SPED.TAX,SPED.TAX_AMOUNT,"
		+ " SPED.AMOUNT_AFTER_TAX,SPED.OTHER_CHARGES,SPED.TAX_ON_OTHER_TRANSPORT_CHG,"
		+ " SPED.OTHER_CHARGES_AFTER_TAX,SPED.TOTAL_AMOUNT,SPED.INDENT_CREATION_DTLS_ID," +
		   "SPED.PO_ENTRY_DETAILS_ID,TAX_PERCENTAGE "
		+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,"
		+ " PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST,INDENT_GST INGST WHERE SPED.PRODUCT_ID = P.PRODUCT_ID "
		+ " AND SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID AND "
		+ " SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND "
		+ " SPED.MEASUR_MNT_ID = MST.MEASUREMENT_ID AND  INGST.TAX_ID = SPED.TAX and"
		+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"'  and SPE.PO_STATUS='A'  order by SPED.INDENT_CREATION_DTLS_ID " ;
		//+ "AND PO_STATUS = 'A'";
	
		
		int serialno = 0;
		dbPODts = template.queryForList(query, new Object[]{});
		
		for(Map<String, Object> prods : dbPODts) {
			ProductDetails pd = new ProductDetails();
			serialno++;
			pd.setSerialno(serialno);
			pd.setProductId(prods.get("PRODUCT_ID") == null ? "" : prods.get("PRODUCT_ID").toString());
			pd.setSub_ProductId(prods.get("SUB_PRODUCT_ID") == null ? "" : prods.get("SUB_PRODUCT_ID").toString());
			pd.setChild_ProductId(prods.get("CHILD_PRODUCT_ID") == null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
			pd.setMeasurementId(prods.get("MEASUREMENT_ID") == null ? "" : prods.get("MEASUREMENT_ID").toString());
			pd.setProductName(prods.get("PRODUCT_NAME") == null ? "" : prods.get("PRODUCT_NAME").toString());
			pd.setSub_ProductName(prods.get("SUB_PRODUCT_NAME") == null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
			pd.setChild_ProductName(prods.get("CHILD_PRODUCT_NAME") == null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
			pd.setMeasurementName(prods.get("MEASUREMENT_NAME") == null ? "" : prods.get("MEASUREMENT_NAME").toString());
			pd.setRequiredQuantity(prods.get("PO_QTY") == null ? "" : prods.get("PO_QTY").toString());
			pd.setRecivedQty(prods.get("RECEIVED_QUANTITY") == null ? "0" : prods.get("RECEIVED_QUANTITY").toString());
			pd.setPrice(prods.get("PRICE") == null ? "" : prods.get("PRICE").toString());
			pd.setBasicAmt(prods.get("BASIC_AMOUNT") == null ? "" : prods.get("BASIC_AMOUNT").toString());
			pd.setStrDiscount(prods.get("DISCOUNT") == null ? "" : prods.get("DISCOUNT").toString());
			pd.setStrAmtAfterDiscount(prods.get("AMOUNT_AFTER_DISCOUNT") == null ? "" : prods.get("AMOUNT_AFTER_DISCOUNT").toString());
			pd.setHsnCode(prods.get("HSN_CODE") == null ? "" : prods.get("HSN_CODE").toString());
			pd.setChildProductCustDisc(prods.get("VENDOR_PRODUCT_DESC") == null ? "-" : prods.get("VENDOR_PRODUCT_DESC").toString());
			String taxId = prods.get("TAX") == null ? "" : prods.get("TAX").toString();
			//String query1 = "select TAX_PERCENTAGE from INDENT_GST where TAX_ID = "+taxId+ " ";
		//	String taxValue = template.queryForObject(query1,String.class);
			String taxValue = prods.get("TAX_PERCENTAGE") == null ? "" : prods.get("TAX_PERCENTAGE").toString();
			
			groupId=prods.get("MATERIAL_GROUP_ID") == null ? "0" : prods.get("MATERIAL_GROUP_ID").toString();
			
			if(groupId!=null && !groupId.equals("0") && !groupId.equals("")){
				String data=gettingReqBoqQuantityAjax(groupId,reqSiteId);
				availablequantity=data.split("_")[0];
				boqQuantity=data.split("_")[1];
			}
			if(groupId.equalsIgnoreCase("NA")){
				groupId="0";
			}
			pd.setGroupId(groupId);
			pd.setTaxId(taxId);
			pd.setTax(taxValue);
			// this is for boq purpose written this one
			pd.setBoqQuantity(boqQuantity);
			pd.setAvailableQuantity(availablequantity);
			pd.setTaxAmount(prods.get("TAX_AMOUNT") == null ? "" : prods.get("TAX_AMOUNT").toString());
			pd.setAmountAfterTax(prods.get("AMOUNT_AFTER_TAX") == null ? "" : prods.get("AMOUNT_AFTER_TAX").toString());
			pd.setOthercharges1(prods.get("OTHER_CHARGES") == null ? "" : prods.get("OTHER_CHARGES").toString());
			pd.setTaxonothertranportcharge1(prods.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
			pd.setOtherchargesaftertax1(prods.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
			
			
			pd.setTotalAmount(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
			pd.setIndentCreationDetailsId(prods.get("INDENT_CREATION_DTLS_ID") == null ? "" : prods.get("INDENT_CREATION_DTLS_ID").toString());
			pd.setPoEntryDetailsId(prods.get("PO_ENTRY_DETAILS_ID") == null ? "" : prods.get("PO_ENTRY_DETAILS_ID").toString());
			
		//	doubleTotalAmount = Double.valueOf(prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString());
		//	doublePOTotalAmount = doublePOTotalAmount+doubleTotalAmount;
			
			
			
			list.add(pd);
		}
		
		
	
		return list;
	}

	@Override
	public List<ProductDetails> getTransChrgsDtls(String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbTransDts = null;

		String query = "select SPTOCD.ID,SPTOCD.TRANSPORT_ID,STOCM.CHARGE_NAME,SPTOCD.TRANSPORT_GST_PERCENTAGE,IG.TAX_PERCENTAGE,SPTOCD.TRANSPORT_GST_AMOUNT,"
			+ " SPTOCD.TOTAL_AMOUNT_AFTER_GST_TAX,SPTOCD.TRANSPORT_AMOUNT from SUMADHURA_PO_TRNS_O_CHRGS_DTLS  SPTOCD, "
			+ " SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM,INDENT_GST IG,SUMADHURA_PO_ENTRY SPE where  SPTOCD.TRANSPORT_ID = STOCM.CHARGE_ID and "
			+ " IG.TAX_ID = SPTOCD.TRANSPORT_GST_PERCENTAGE and SPE.PO_ENTRY_ID = SPTOCD.PO_NUMBER and SPE.PO_NUMBER = ? and SPE.SITE_ID = ? ";
		dbTransDts = template.queryForList(query, new Object[]{poNumber,reqSiteId});
		int sno = 0;
		for(Map<String, Object> prods : dbTransDts) {
			ProductDetails pd = new ProductDetails();
			sno++;
			pd.setStrSerialNumber(String.valueOf(sno));
			pd.setConveyanceId1(prods.get("TRANSPORT_ID") == null ? "" : prods.get("TRANSPORT_ID").toString());
			pd.setConveyance1(prods.get("CHARGE_NAME") == null ? "" : prods.get("CHARGE_NAME").toString());
			pd.setConveyanceAmount1(prods.get("TRANSPORT_AMOUNT") == null ? "" : prods.get("TRANSPORT_AMOUNT").toString());
			pd.setGSTTaxId1(prods.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : prods.get("TRANSPORT_GST_PERCENTAGE").toString());
			pd.setGSTTax1(prods.get("TAX_PERCENTAGE") == null ? "" : prods.get("TAX_PERCENTAGE").toString());
			pd.setGSTAmount1(prods.get("TRANSPORT_GST_AMOUNT") == null ? "" : prods.get("TRANSPORT_GST_AMOUNT").toString());
			pd.setAmountAfterTaxx1(prods.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "0" : prods.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
			pd.setPoTransChrgsDtlsSeqNo(prods.get("ID") == null ? "" : prods.get("ID").toString());

			list.add(pd);
		}
		return list;

	}

	@Override
	public int updateReceiveQuantityInIndentCreationDtls(String receiveQuantity,String indentCreationDetailsId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String query = "update  SUMADHURA_INDENT_CREATION_DTLS set RECEIVE_QUANTITY = RECEIVE_QUANTITY+? "
			+ " where INDENT_CREATION_DETAILS_ID = ? ";
		int result = template.update(query, new Object[]{Double.valueOf(receiveQuantity),indentCreationDetailsId});
		//System.out.print(result+",");
		return result;
	}
	@Override
	public int updateReceivedQuantityInPoEntryDetails(String totalQuantity, String poEntryDetailsId, HttpServletRequest request) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String query1 = "select RECEIVED_QUANTITY from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_DETAILS_ID = ? ";
		String strReceivedQuantity = template.queryForObject(query1, new Object[]{poEntryDetailsId},String.class);
		//double receivedQuantity = Double.valueOf(strReceivedQuantity==null?"0":strReceivedQuantity)+Double.valueOf(totalQuantity);
		BigDecimal receivedQuantity = new BigDecimal(strReceivedQuantity==null?"0":strReceivedQuantity).add(new BigDecimal(totalQuantity));
		
		String query2 = "select PO_QTY from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_DETAILS_ID = ? ";
		String strPoQuantity = template.queryForObject(query2, new Object[]{poEntryDetailsId},String.class);
		// if 'receivedQuantity'is greater than 'strPoQuantity' returns 1.
		if(receivedQuantity.compareTo(new BigDecimal(strPoQuantity))==1){ 
			request.setAttribute("exceptionMsg", "Received Quantity exceeding PO Quantity");
			throw new RuntimeException("Received Quantity exceeding PO Quantity"); 
		}
		
		
		String query = "update SUMADHURA_PO_ENTRY_DETAILS set RECEIVED_QUANTITY = ? where PO_ENTRY_DETAILS_ID = ?";
		int result = template.update(query, new Object[]{receivedQuantity,poEntryDetailsId});
		
		return result;
	}
	@Override
	public int setPOInactive(String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
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
	@Override
	public int setIndentInactiveAfterChecking(String indentNumber) {
		int result = 0;
		boolean doInactive=true;
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
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

	@Override
	public int insertCreditNote(CreditNoteDto creditNoteDto) {
		int result = 0;
		String query = "insert into SUMADHURA_CREDIT_NOTE(CREDIT_NOTE_SEQ_ID,CREDIT_NOTE_NUMBER,"
			+ " INDENT_ENTRY_NUMBER,ENTRY_DATE,INVOICE_NUMBER,CREDIT_TOTAL_AMOUNT,DC_NUMBER,DC_ENTRY_ID,TYPE,DOCUMENT_NO) "
			+ " values(?, ?, ?, sysdate, ?, ?, ?, ?, ?, ?)";

		result = jdbcTemplate.update(query, new Object[] {
				creditNoteDto.getCreditSeqNo(),
				creditNoteDto.getCreditNoteNumber(),
				creditNoteDto.getIndentEntryId(),
				creditNoteDto.getInvoiceNumber(),
				creditNoteDto.getCreditTotalAmount(),
				creditNoteDto.getDcNumber(),
				creditNoteDto.getDcEntryId(),
				creditNoteDto.getCreditFor(),
				"DN/SIPL/"+creditNoteDto.getCreditSeqNo()
		});
		//System.out.println("insert credit note: "+result);
		return result;
	}
	@Override
	public int insertCreditNoteDetails(CreditNoteDto creditNoteDetailsDto,ProductDetails productDetails) {
		int result = 0;
		String query = "insert into SUMADHURA_CREDIT_NOTE_DETAILS(CREDIT_NOTE_DTLS_SEQ_ID,CREDIT_NOTE_SEQ_ID,"
			+ " INDENT_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUR_MNT_ID,RECEIVED_QTY,"
			+ " ENTRY_DATE,PRICE,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,"
			+ " TOTAL_AMOUNT,HSN_CODE,BASIC_AMOUNT,TAX_ON_OTHER_TRANSPORT_CHG) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		result = jdbcTemplate.update(query, new Object[] {
				creditNoteDetailsDto.getCreditNoteDtlsSeqId(),
				creditNoteDetailsDto.getCreditSeqNo(),
				creditNoteDetailsDto.getIndentEntryId(),
				productDetails.getProductId(),
				productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),
				productDetails.getMeasurementId(),
				productDetails.getRecivedQty(),
				productDetails.getPrice(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),
				productDetails.getAmountAfterTax(),
				productDetails.getOtherOrTransportCharges1(),
				productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),
				productDetails.getHsnCode(),
				productDetails.getBasicAmt(),
				productDetails.getTaxOnOtherOrTransportCharges1()
		});
	//	System.out.println("insert credit note detail: "+result);
		return result;
	}
	@Override
	public int getCreditNoteSequenceNumber() {
		return jdbcTemplate.queryForInt("SELECT SUMADHURA_CREDIT_NOTE_SEQ.NEXTVAL FROM DUAL");
	}
	@Override
	public int getCreditNoteDetailsSequenceNumber() {
		return jdbcTemplate.queryForInt("SELECT SUMADHURA_CREDIT_NOTE_DTLS_SEQ.NEXTVAL FROM DUAL");
	}

	@Override
	public String getPriceRatesByChildProduct(String childProdId,String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		List<Map<String, Object>> dbPODts = null;

		String query = "SELECT SPED.PO_QTY,SPED.PRICE,SPED.BASIC_AMOUNT,"
			+ " SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.HSN_CODE,SPED.TAX,SPED.TAX_AMOUNT,"
			+ " SPED.AMOUNT_AFTER_TAX,SPED.OTHER_CHARGES,SPED.TAX_ON_OTHER_TRANSPORT_CHG,"
			+ " SPED.OTHER_CHARGES_AFTER_TAX,SPED.TOTAL_AMOUNT,SPED.INDENT_CREATION_DTLS_ID,SPED.PO_ENTRY_DETAILS_ID " 
			+ " FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE,CHILD_PRODUCT CP WHERE "
			+ " SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND " 
			+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' AND SPED.CHILD_PRODUCT_ID = '"+childProdId+"'";
		dbPODts = template.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbPODts) {
			sb.append(prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString()+":");
			sb.append(prods.get("PRICE") == null ? "0" : prods.get("PRICE").toString()+":");
			sb.append(prods.get("BASIC_AMOUNT") == null ? "0" : prods.get("BASIC_AMOUNT").toString()+":");
			sb.append(prods.get("DISCOUNT") == null ? "0" : prods.get("DISCOUNT").toString()+":");
			sb.append(prods.get("AMOUNT_AFTER_DISCOUNT") == null ? "0" : prods.get("AMOUNT_AFTER_DISCOUNT").toString()+":");
			sb.append(prods.get("HSN_CODE") == null ? "0" : prods.get("HSN_CODE").toString()+":");
			String taxId = prods.get("TAX") == null ? "0" : prods.get("TAX").toString();
			String query1 = "select TAX_PERCENTAGE from INDENT_GST where TAX_ID = ? ";
			String taxValue = template.queryForObject(query1, new Object[]{taxId},String.class);
			sb.append(taxId+":");
			sb.append(taxValue.substring(0, taxValue.length()-1)+":");
			sb.append(prods.get("TAX_AMOUNT") == null ? "0" : prods.get("TAX_AMOUNT").toString()+":");
			sb.append(prods.get("AMOUNT_AFTER_TAX") == null ? "0" : prods.get("AMOUNT_AFTER_TAX").toString()+":");
			sb.append(prods.get("OTHER_CHARGES") == null ? "0" : prods.get("OTHER_CHARGES").toString()+":");
			sb.append(prods.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString()+":");
			sb.append(prods.get("OTHER_CHARGES_AFTER_TAX") == null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString()+":");
			sb.append(prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString()+":");
			sb.append(prods.get("INDENT_CREATION_DTLS_ID") == null ? "0" : prods.get("INDENT_CREATION_DTLS_ID").toString()+":");
			sb.append(prods.get("PO_ENTRY_DETAILS_ID") == null ? "0" : prods.get("PO_ENTRY_DETAILS_ID").toString());

			break;
		}
		return sb.toString();
	}



	@Override
	public int updateAllocatedQuantityInPurchaseDeptTable(String receiveQuantity,String indentCreationDetailsId,HttpServletRequest request) {
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String query = "update  SUM_PURCHASE_DEPT_INDENT_PROSS set ALLOCATED_QUANTITY = ALLOCATED_QUANTITY+? "
				+ ", PENDING_QUANTIY = PENDING_QUANTIY-? , PO_INTIATED_QUANTITY = PO_INTIATED_QUANTITY-? "
			+ " where INDENT_CREATION_DETAILS_ID = ? ";
		int result = template.update(query, new Object[]{Double.valueOf(receiveQuantity),Double.valueOf(receiveQuantity),Double.valueOf(receiveQuantity),indentCreationDetailsId});
	
		return result;
	}


	@Override
	public int updateInvoiceNoInAccPaymentTbl(String invoiceNumber, String invoiceAmount, String invoiceDate,String receviedDate,
			String poNo, String vendorId, String creditNoteNumber,String creditTotalAmount,String indentEntryNo,String site_id) {
		List<Map<String, Object>> dbPODts = null;
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		int count = jdbcTemplate.queryForInt("select count(*) from ACC_PAYMENT where PO_NUMBER = '"+poNo+"'");
		
		if(count==1){
			String query1 = "select INVOICE_NUMBER,PO_AMOUNT,PO_DATE,REMARKS from ACC_PAYMENT where PO_NUMBER = '"+poNo+"'";
			dbPODts = template.queryForList(query1, new Object[]{});
			String invoiceNoIn_DB_AP = null;
			String po_amount = null;
			String po_date = null;
			String remarks = null;
			for(Map<String, Object> prods : dbPODts) {
				invoiceNoIn_DB_AP = prods.get("INVOICE_NUMBER") == null ? null : prods.get("INVOICE_NUMBER").toString();
				po_amount = prods.get("PO_AMOUNT") == null ? null : prods.get("PO_AMOUNT").toString();
				po_date = prods.get("PO_DATE") == null ? null : prods.get("PO_DATE").toString();
				remarks = prods.get("REMARKS") == null ? null : prods.get("REMARKS").toString();
			}
			
			if(invoiceNoIn_DB_AP==null){
				String query = "update  ACC_PAYMENT set INVOICE_NUMBER = ? "
						+ ", INVOICE_AMOUNT = ? , INVOICE_DATE = ?, INVOICE_RECEIVED_DATE = ? ,CREDIT_NOTE_NUMBER = ?,CREDIT_TOTAL_AMOUNT = ?,INDENT_ENTRY_ID = ? "
						+ " where PO_NUMBER = ? and VENDOR_ID = ? ";
				count= template.update(query, new Object[]{ 
						invoiceNumber, invoiceAmount, invoiceDate, receviedDate, creditNoteNumber,creditTotalAmount,indentEntryNo, poNo, vendorId});
			}
			else{
				String query = "insert into ACC_PAYMENT(PAYMENT_ID,INVOICE_NUMBER,INVOICE_AMOUNT " +
						",SITE_ID,CREATED_DATE, PAYMENT_DONE_UPTO, DC_NUMBER, PAYMENT_REQ_UPTO, STATUS, " +
						" REMARKS, VENDOR_ID, PO_NUMBER,PO_AMOUNT,INVOICE_DATE,PO_DATE,DC_DATE,INVOICE_RECEIVED_DATE,INDENT_ENTRY_ID,CREDIT_TOTAL_AMOUNT,CREDIT_NOTE_NUMBER) " +
						"values(PAYMENT_SEQ_ID.nextval,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

				count = jdbcTemplate.update(query, new Object[] {
						invoiceNumber, invoiceAmount,
						site_id,0.0,null,0.0,"A",
						remarks,vendorId,poNo,po_amount,DateUtil.convertToJavaDateFormat(invoiceDate),DateUtil.convertToJavaDateFormat(po_date),null,receviedDate,indentEntryNo,creditTotalAmount,creditNoteNumber
				});

			}
		}
		
		return count;
	}



	@Override
	public int checkPOisActive(String poNumber) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		int count = template.queryForInt("select count(*) from SUMADHURA_PO_ENTRY where PO_NUMBER = '"+poNumber+"' and PO_STATUS = 'A'");
		return count;
	}

	public int getCheckIndentAvailable(String  indentNumber){
		String query  = "select count(1) from  SUMADHURA_INDENT_CREATION where SITEWISE_INDENT_NO =? AND STATUS in('A','C')";
		int intCount =    jdbcTemplate.queryForInt(query, new Object[] {indentNumber});
		return intCount;
	}
	@Override
	public String getPoEntryDetailsandIndentCreationDetails(String poNumber,String childProdId,String indentNumber) {
		JdbcTemplate template = null;
		String value="";
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
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
	@Override
	public String getIndentCreationDetailsId(String indentNumber,String childProdId) {
		List<Map<String, Object>> dbPODts = null;
		JdbcTemplate template = null;
		String result="";
		try {
			 template =new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	@Override
	public int getCheckPoAvailable(String poNumber,String vendorId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		int count = template.queryForInt("select count(*) from SUMADHURA_PO_ENTRY where PO_NUMBER = '"+poNumber+"' and PO_STATUS = 'A' AND VENDOR_ID='"+vendorId+"'");
		if(count==0){
		count=template.queryForInt("select count(*) from SUMADHURA_OLD_PO_NUMBERS where PO_NUMBER = '"+poNumber+"'  AND VENDOR_ID='"+vendorId+"'");
		}
		return count;
	}



	@Override
	public List<MarketingDeptBean> getPOProductLocationDetails(String poNumber) {
		List<Map<String, Object>> ProdsMap = null;
		List<MarketingDeptBean> list = new ArrayList<MarketingDeptBean>();
		
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String query = "select MPPLD.CHILD_PRODUCT_ID,CP.NAME,MPPLD.LOCATION_ID,MHD.HOARDING_AREA,"
					+" MPPLD.TIME,MPPLD.PO_ENTRY_DETAILS_ID,MPPLD.PO_NUMBER,"
					+" MPPLD.QUANTITY,min(MPPLD.HOARDING_DATE) as FROM_DATE,"
					+" max(MPPLD.HOARDING_DATE) as TO_DATE,MPPLD.SITE_ID,S.SITE_NAME, "
					+" MPPLD.AMOUNT_PER_UNIT_AFTER_TAX,MPPLD.TOTAL_AMOUNT,MPPLD.PRODUCT_SERIAL_NO " 	
					+" from CHILD_PRODUCT CP,SITE S,MRKT_PO_PROD_LOC_DTLS MPPLD "
					+" LEFT OUTER JOIN MRKT_HOARDING_DTLS MHD  ON  MHD.HOARDING_ID = MPPLD.LOCATION_ID "
					+" where PO_NUMBER =? and CP.CHILD_PRODUCT_ID = MPPLD.CHILD_PRODUCT_ID " 
					+" and MPPLD.SITE_ID = S.SITE_ID "
					+" group by MPPLD.CHILD_PRODUCT_ID,CP.NAME,MPPLD.LOCATION_ID,MHD.HOARDING_AREA,MPPLD.TIME,MPPLD.PO_ENTRY_DETAILS_ID,"
					+" MPPLD.PO_NUMBER,MPPLD.QUANTITY,MPPLD.SITE_ID,S.SITE_NAME,MPPLD.AMOUNT_PER_UNIT_AFTER_TAX,MPPLD.TOTAL_AMOUNT,MPPLD.PRODUCT_SERIAL_NO";
		ProdsMap = template.queryForList(query, new Object[]{poNumber});
		int serialno=0;
		for(Map<String, Object> prods : ProdsMap) {
			MarketingDeptBean bean = new MarketingDeptBean();
			bean.setChild_ProductId(prods.get("CHILD_PRODUCT_ID")==null?"":prods.get("CHILD_PRODUCT_ID").toString());
			bean.setChild_ProductName(prods.get("NAME")==null?"":prods.get("NAME").toString());
			bean.setHoardingId(prods.get("LOCATION_ID")==null?"":prods.get("LOCATION_ID").toString());
			bean.setLocation(prods.get("HOARDING_AREA")==null?"":prods.get("HOARDING_AREA").toString());
			bean.setTime(prods.get("TIME")==null?"":prods.get("TIME").toString());
			bean.setPoEntryDetailsId(prods.get("PO_ENTRY_DETAILS_ID")==null?"":prods.get("PO_ENTRY_DETAILS_ID").toString());
			bean.setPoNumber(prods.get("PO_NUMBER")==null?"":prods.get("PO_NUMBER").toString());
			bean.setQuantity(prods.get("QUANTITY")==null?"":prods.get("QUANTITY").toString());
			bean.setSiteId(prods.get("SITE_ID")==null?"":prods.get("SITE_ID").toString());
			bean.setSiteName(prods.get("SITE_NAME")==null?"":prods.get("SITE_NAME").toString());
			bean.setRate(prods.get("AMOUNT_PER_UNIT_AFTER_TAX")==null?"":prods.get("AMOUNT_PER_UNIT_AFTER_TAX").toString());
			bean.setTotalAmount(prods.get("TOTAL_AMOUNT")==null?"":prods.get("TOTAL_AMOUNT").toString());
			bean.setStrSerialNumber(String.valueOf(++serialno));
			
			String fromDate = prods.get("FROM_DATE")==null?"":prods.get("FROM_DATE").toString();
			String toDate = prods.get("TO_DATE")==null?"":prods.get("TO_DATE").toString();
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			Date fromdate = null;
			Date todate = null;
			try {
				fromdate = dt.parse(fromDate); 
				todate = dt.parse(toDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			


			SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yy");
			
			bean.setFromDate(dt1.format(fromdate));
			bean.setToDate(dt1.format(todate));
			
			list.add(bean);
		}
	return list;
	}



	
	@Override
	public List<Map<String, Object>> loadInvoiceNumberByVendorId(String site_id, String vendorId) {
		StringBuffer query=new StringBuffer("SELECT IE.INDENT_TYPE,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,TO_CHAR(IE.INVOICE_DATE,'dd/MM/yyyy') as INVOICE_DATE,trim(to_char(IE.TOTAL_AMOUNT,'9,999,999,999.99')) as TOTAL_AMOUNT")
				.append(" FROM INDENT_ENTRY IE WHERE IE.INVOICE_ID IS NOT NULL AND IE.INDENT_TYPE='IN' AND IE.VENDOR_ID=? AND IE.SITE_ID=? ")
				.append(" AND IE.INDENT_ENTRY_ID not in (SELECT INDENT_ENTRY_ID from ACC_TAXINVOICE_DETAILS ATD WHERE  ATD.VENDOR_ID=IE.VENDOR_ID AND ATD.SITE_ID=IE.SITE_ID AND ATD.STATUS not in ('R')  )");
		
		List<Map<String, Object>> listOfInvoiceId=jdbcTemplate.queryForList(query.toString(),vendorId,site_id);
		return listOfInvoiceId;
	}
	
	@Override
	public List<Map<String, Object>> receiveTaxInvoicesDetails(GetInvoiceDetailsBean bean,String status,String condition) {
		StringBuffer query=new StringBuffer("select   (SELECT LISTAGG(REMARKS,' , ')   WITHIN GROUP (ORDER BY TAXINVOICE_CRETAION_APPROVAL_DTLS_ID)   FROM ACC_TAXINVOICE_APP_REJ_DETAILS WHERE INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID) AS REMARKS,SI.SITE_NAME,to_char(ATARD.CREATION_DATE,'dd-Mon-yyyy') as CREATION_DATE,trim(to_char(IE.TOTAL_AMOUNT,'9,999,999,999.99')) as TOTAL_AMOUNT,")
				.append("to_char(IE.INVOICE_DATE,'dd/MM/yyyy') as INVOICE_DATE,IE.INDENT_TYPE,ATD.ACC_TAXINVOICE_DETAILS_ID,ATD.INDENT_ENTRY_ID,ATD.INVOICE_NO,ATD.SITE_ID,ATD.VENDOR_ID,ATD.TAXINVOICE_SUBMITTED_DATE,ATD.PENDING_EMPLOYEE_ID,  ATD.TAXINVOICE_STATUS,VD.VENDOR_NAME ")
				.append(",(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =ATD.PENDING_EMPLOYEE_ID) as PENDING_EMP_NAME")
				.append(",IE.PO_ID,to_char(IE.PODATE,'dd/MM/yyyy') as PODATE,IE.INVOICE_ID,to_char(IE.RECEIVED_OR_ISSUED_DATE,'dd/MM/yyyy') as RECEIVED_OR_ISSUED_DATE    ")
				.append(" from ACC_TAXINVOICE_DETAILS ATD")
				.append(" LEFT OUTER JOIN INDENT_ENTRY IE ON IE.INDENT_ENTRY_ID=ATD.INDENT_ENTRY_ID")
				.append(" LEFT OUTER JOIN VENDOR_DETAILS VD ON VD.VENDOR_ID=ATD.VENDOR_ID")
				.append(" ,ACC_TAXINVOICE_APP_REJ_DETAILS ATARD,SITE SI ")
				.append(" where ATARD.INDENT_ENTRY_ID=ATD.INDENT_ENTRY_ID AND SI.SITE_ID=ATD.SITE_ID AND ATD.STATUS in ('A','R') ");
			if(status.equals("approvePage")){
			     query.append(" and ATD.PENDING_EMPLOYEE_ID='"+bean.getUserId()+"' ");
			}
			//if(!condition.equals("siteWise")){
				query.append(" AND ATD.SITE_ID=? ");
			//}
			 
			 query.append(" and ATARD.CREATION_DATE=(select MAX (CREATION_DATE) from  ACC_TAXINVOICE_APP_REJ_DETAILS where INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID)"); 

		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),bean.getSiteId());
		return list;
	}
	
	/**
	 * @description storing all the details of the tax invoices
	 * 
	 */
	@Override
	public int saveTaxInvoicesData(final List<GetInvoiceDetailsBean> invoiceDetialsBean, final String nextLevelApprovalEmpId) {
	int updateResult=0;
		StringBuffer query=new StringBuffer("INSERT INTO ACC_TAXINVOICE_DETAILS ")
			.append("(ACC_TAXINVOICE_DETAILS_ID,ENTRY_DATE,TAXINVOICE_STATUS,STATUS,INDENT_ENTRY_ID,INVOICE_NO,SITE_ID,VENDOR_ID,PENDING_EMPLOYEE_ID,CREATED_BY) ")
			.append("VALUES (ACC_TAXINVOICE_DETAILS_SEQ.NEXTVAL,sysdate,'PENDING','A',?,?,?,?,?,?)");
	
	for (GetInvoiceDetailsBean bean : invoiceDetialsBean) {
		String str="DELETE FROM ACC_TAXINVOICE_DETAILS  WHERE INDENT_ENTRY_ID=?";
		updateResult=jdbcTemplate.update(str,bean.getIndentEntryId());
	}
			int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
				
				@Override
				public int getBatchSize() {
					return invoiceDetialsBean.size();
				}
				
				@Override
				public void setValues(PreparedStatement ps, int index) throws SQLException {
					GetInvoiceDetailsBean bean=invoiceDetialsBean.get(index);
					 ps.setString(1, bean.getIndentEntryId());
					 ps.setString(2, bean.getInvoiceNumber());
					 ps.setString(3, bean.getSiteId());
					 ps.setString(4, bean.getVendorId());
					 ps.setString(5, nextLevelApprovalEmpId);
					 ps.setString(6, bean.getUserId());
				}
			});
	
		return result.length;
	}
	
	@Override
	public List<Map<String, Object>> loadAllTaxInvoicesDetails(GetInvoiceDetailsBean bean) {
		StringBuffer query=new StringBuffer("SELECT IE.SITE_ID,VD.ADDRESS,VD.GSIN_NUMBER,VD.VENDOR_NAME,VD.VENDOR_ID,IE.INDENT_TYPE,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,TO_CHAR(IE.INVOICE_DATE,'dd/MM/yyyy') as INVOICE_DATE,trim(to_char(IE.TOTAL_AMOUNT,'9,999,999,999.99')) as TOTAL_AMOUNT")
				.append(",IE.PO_ID,TO_CHAR(IE.PODATE,'dd/MM/yyyy') as PODATE,TO_CHAR(IE.RECEIVED_OR_ISSUED_DATE,'dd/MM/yyyy') as RECEIVED_OR_ISSUED_DATE")
				.append(" FROM INDENT_ENTRY IE,VENDOR_DETAILS VD WHERE VD.VENDOR_ID=IE.VENDOR_ID AND IE.INVOICE_ID IS NOT NULL AND IE.INDENT_TYPE='IN'  AND IE.SITE_ID=? ")
				.append(" AND IE.INDENT_ENTRY_ID not in (SELECT INDENT_ENTRY_ID from ACC_TAXINVOICE_DETAILS ATD WHERE  ATD.VENDOR_ID=IE.VENDOR_ID AND ATD.SITE_ID=IE.SITE_ID AND ATD.STATUS not in ('R')  )");
		List<Map<String, Object>> listOfInvoiceId=jdbcTemplate.queryForList(query.toString() ,bean.getSiteId());
		return listOfInvoiceId;
	}
	
	@Override
	public int approveTaxInvoicesData(final List<GetInvoiceDetailsBean> invoiceDetialsBean, final String nextLevelApprovalEmpId) {
		final String statusOftheTaxInvoice;
		final String statusTaxInvoice;
		StringBuffer query=new StringBuffer("UPDATE ACC_TAXINVOICE_DETAILS SET PENDING_EMPLOYEE_ID =?,TAXINVOICE_STATUS=?,STATUS=? ");
				
		
		if(nextLevelApprovalEmpId.equals("END")){
			statusOftheTaxInvoice="SUBMITTED";
			statusTaxInvoice="I";
			query.append(" ,TAXINVOICE_SUBMITTED_DATE=sysdate");
		}else{
			statusOftheTaxInvoice="PENDING";
			statusTaxInvoice="A";
		}
		
		query.append(" WHERE SITE_ID=? AND ACC_TAXINVOICE_DETAILS_ID=?");
		int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return invoiceDetialsBean.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				GetInvoiceDetailsBean bean=invoiceDetialsBean.get(index);
				 ps.setString(1, nextLevelApprovalEmpId);
				 ps.setString(2, statusOftheTaxInvoice);
				 ps.setString(3, statusTaxInvoice);
				 ps.setString(4, bean.getSiteId());
				 ps.setString(5, bean.getAccTaxInvoiceDetailsID());
			}
		});
		return result.length;
	}
	
	@Override
	public int rejectTaxInvoicesData(final List<GetInvoiceDetailsBean> invoiceDetialsBean, final String nextLevelApprovalEmpId) {
		String query="UPDATE ACC_TAXINVOICE_DETAILS ATD SET ATD.PENDING_EMPLOYEE_ID=(SELECT EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL WHERE APPROVER_EMP_ID =? AND SITE_ID=? AND MODULE_TYPE='TAX_INV'), ATD.TAXINVOICE_STATUS='REJECTED',STATUS='R'  WHERE ATD.SITE_ID=? AND ATD.ACC_TAXINVOICE_DETAILS_ID=?";
		
		int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return invoiceDetialsBean.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				GetInvoiceDetailsBean bean=invoiceDetialsBean.get(index);
				
				 ps.setString(1, bean.getUserId());
				 ps.setString(2, bean.getSiteId());
				
				 ps.setString(3, bean.getSiteId());
				 ps.setString(4, bean.getAccTaxInvoiceDetailsID());
			}
		});
		return result.length;
	}
	
	@Override
	public GetInvoiceDetailsBean getSubmitTaxInvoiceFromAndToDetails(String user_id, GetInvoiceDetailsBean bean) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = " select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =?)) as emp_name, SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id, SAMD.SITE_ID FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and SAMD.EMP_ID = ? and  SAMD.SITE_ID=? AND SAMD.MODULE_TYPE='TAX_INV'";
		 Object[] obj={ user_id, user_id,bean.getSiteId() };
		dbIndentDts = jdbcTemplate.queryForList(query,obj);
		for (Map<String, Object> prods : dbIndentDts) {
			log.info("Mapping Details in Tax Invoice :: "+prods);
			bean.setApproverEmpId(prods.get("APPROVER_ID") == null ? "" : prods.get("APPROVER_ID").toString());
			bean.setTaxInvoiceFromEmp(prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString());
			bean.setTaxInvoiceToEmpName(prods.get("APPROVER_NAME") == null ? "" : prods.get("APPROVER_NAME").toString());
			bean.setSiteId(prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString());
		}
		return bean;
	}
	
	@Override
	public int saveTaxInvoicesApprRejctDetails(final  List<GetInvoiceDetailsBean> invoiceDetialsBean,	final String operationType) {
	 
		StringBuffer query=new StringBuffer("INSERT INTO ACC_TAXINVOICE_APP_REJ_DETAILS ")
			.append("(TAXINVOICE_CRETAION_APPROVAL_DTLS_ID,CREATION_DATE,INDENT_ENTRY_ID,TAXINVOICE_APP_EMP_ID,OPERATION_TYPE,REMARKS) ")
			.append("VALUES(ACC_TAXINVOICE_APP_REJ_DETAILS_SEQ.NEXTVAL,sysdate,?,?,?,?)");
	
		
		int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return invoiceDetialsBean.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int index) throws SQLException {
				GetInvoiceDetailsBean bean=invoiceDetialsBean.get(index);
				ps.setString(1, bean.getIndentEntryId());
				ps.setString(2, bean.getUserId());
				ps.setString(3, operationType);
				ps.setString(4, bean.getRemarks());
				 
			}
		});
		
	 /*	int result=jdbcTemplate.update(query.toString(), new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, bean.getIndentEntryId());
				ps.setString(2, bean.getUserId());
				ps.setString(3, operationType);
				ps.setString(4, bean.getRemarks());
			}
		});*/
	return result.length;
	}

	@Override
	public String getEmployeeDetails(String userId){
		String query = "select EMP_ID,EMP_NAME,EMP_DESIGNATION from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = ? ";
		Map<String,Object> map = jdbcTemplate.queryForMap(query,new Object[]{userId});
		String empName = map.get("EMP_NAME")==null?"-":map.get("EMP_NAME").toString();
		String empDesignation = map.get("EMP_DESIGNATION")==null?"-":map.get("EMP_DESIGNATION").toString();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		Date date = new Date();
		String dateAndTime = dateFormat.format(date); // 29/07/2016 02:08 PM
		
		return empName+", "+empDesignation+", "+dateAndTime; //  &#013;  not worked
	}
	
	
	@Override
	public void setApprovalEmployeeDetails(List<String> listOfIndentEntryIds, List<GetInvoiceDetailsBean> invoiceDetialsBean){
		Map<String,List<String>> IndentEntryIdWiseApprovals = new HashMap<String,List<String>>();
		StringBuffer queueOfIndentEntryIds = new StringBuffer();
		if(listOfIndentEntryIds.size()==0){
			queueOfIndentEntryIds.append("''");
		}else{
			String prefix = "";
			for (String id : listOfIndentEntryIds) {
				queueOfIndentEntryIds.append(prefix);
				prefix = ",";
				queueOfIndentEntryIds.append("'"+id+"'");
			}
		}
		StringBuffer query = new StringBuffer("select ATARD.INDENT_ENTRY_ID,ATARD.CREATION_DATE,ATARD.OPERATION_TYPE,SED.EMP_NAME,SED.EMP_DESIGNATION ")
				.append("from ACC_TAXINVOICE_APP_REJ_DETAILS ATARD,SUMADHURA_EMPLOYEE_DETAILS SED ")
				.append("where ATARD.INDENT_ENTRY_ID in ("+queueOfIndentEntryIds+") ")
				.append("and SED.EMP_ID = ATARD.TAXINVOICE_APP_EMP_ID ")
				.append("order by ATARD.INDENT_ENTRY_ID,ATARD.CREATION_DATE asc");
		List<Map<String,Object>> list = jdbcTemplate.queryForList(query.toString()); 
		String indentEntryId = "";
		String empName = "";
		String empDesignation = "";
		String dateAndTime = "";
		String operationType = "";
		for(Map<String,Object> map : list){
			indentEntryId = map.get("INDENT_ENTRY_ID")==null? "": map.get("INDENT_ENTRY_ID").toString();
			empName = map.get("EMP_NAME")==null? "-": map.get("EMP_NAME").toString();
			empDesignation = map.get("EMP_DESIGNATION")==null? "-": map.get("EMP_DESIGNATION").toString();
			dateAndTime = map.get("CREATION_DATE")==null? "-": map.get("CREATION_DATE").toString();
			operationType = map.get("OPERATION_TYPE")==null? "-": map.get("OPERATION_TYPE").toString();
			dateAndTime = DateUtil.convertDBDateInAnotherFormat(dateAndTime,"dd/MM/yyyy hh:mm a");
			
			if(!IndentEntryIdWiseApprovals.containsKey(indentEntryId)){
				IndentEntryIdWiseApprovals.put(indentEntryId,new ArrayList<String>());
			}
			if(operationType.equals("C")){
				IndentEntryIdWiseApprovals.get(indentEntryId).add(empName+", "+empDesignation+", "+dateAndTime);
			}
			else if(operationType.equals("A")){
				IndentEntryIdWiseApprovals.get(indentEntryId).add(empName+", "+empDesignation+", "+dateAndTime);
			}
			else if(operationType.equals("R")){
				if(IndentEntryIdWiseApprovals.get(indentEntryId).size()>0){
					IndentEntryIdWiseApprovals.get(indentEntryId).remove(IndentEntryIdWiseApprovals.get(indentEntryId).size()-1);
				}
			}
		}
		
		for(GetInvoiceDetailsBean bean : invoiceDetialsBean){
			indentEntryId = bean.getIndentEntryId();
			List<String> approvalsList = IndentEntryIdWiseApprovals.get(indentEntryId);
			bean.setSubmittedBy(approvalsList.size()<=0?"":approvalsList.get(0));
			bean.setReceivedBy1(approvalsList.size()<=1?"":approvalsList.get(1));
			bean.setReceivedBy2(approvalsList.size()<=2?"":approvalsList.get(2));
			bean.setReceivedBy3(approvalsList.size()<=3?"":approvalsList.get(3));
			bean.setReceivedBy4(approvalsList.size()<=4?"":approvalsList.get(4));
			
		}
		
	}
	@Override
	public String gettingReqBoqQuantityAjax(String groupId,String siteId) {
		Map<String,String> childSortMap = new HashMap<String,String>();
		double receivedQuantity=0.0;
		double issuedQuantity=0.0;
		double poPendingQuantity=0.0;
		double totalBOQQuantity=0.0;
		double resultQuantity=0.0;
		String childProductList="";
		String measurementList="";
		StringBuffer totalData=new StringBuffer();
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
		 receivedQuantity=(icd.getindentAndDcReceivedQuantity(childProductList,siteId,measurementList));
		 issuedQuantity=(icd.getIssuedQuantity(childProductList,siteId,measurementList));
		 totalBOQQuantity=(icd.getBOQQuantity(groupId,siteId));
		// totalQuantity=receivedQuantity-(issuedQuantity)
		 resultQuantity=totalBOQQuantity-(receivedQuantity-issuedQuantity);
		 if(resultQuantity<0){ resultQuantity=0;}
		 totalData=totalData.append(resultQuantity+"_"+totalBOQQuantity);
		return totalData.toString();
	}


	@Override
	public List<DebitNoteBean> getCreditNoteDetails(String indentEntryId,String dcEntryId, DebitNoteBean Totals) {
		List<DebitNoteBean>  beanlist = new ArrayList<DebitNoteBean>();
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append(" select IG.TAX_PERCENTAGE,SCND.CHILD_PRODUCT_ID,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MEASUREMENT_NAME,SCND.RECEIVED_QTY,SCND.PRICE,SCND.TAX,SCND.TAX_AMOUNT,SCND.AMOUNT_AFTER_TAX,")
			 .append(" SCND.OTHER_CHARGES,SCND.OTHER_CHARGES_AFTER_TAX,SCND.TOTAL_AMOUNT,SCND.HSN_CODE,SCND.BASIC_AMOUNT,SCND.TAX_ON_OTHER_TRANSPORT_CHG") 
			 .append(" from SUMADHURA_CREDIT_NOTE_DETAILS SCND left outer join INDENT_GST IG on SCND.TAX=IG.TAX_ID ")
			 .append(",PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M ")
			 .append(" where P.PRODUCT_ID = SCND.PRODUCT_ID and S.SUB_PRODUCT_ID = SCND.SUB_PRODUCT_ID ")
			 .append(" and C.CHILD_PRODUCT_ID = SCND.CHILD_PRODUCT_ID and M.MEASUREMENT_ID = SCND.MEASUR_MNT_ID ");
			 
		if(StringUtils.isNotBlank(indentEntryId)){ 
			System.out.println("EXCEYTED: indentEntryId: >>"+indentEntryId+"<<");
			query.append(" and SCND.CREDIT_NOTE_SEQ_ID in (select CREDIT_NOTE_SEQ_ID from SUMADHURA_CREDIT_NOTE SCN where SCN.INDENT_ENTRY_NUMBER = ? ) ");
		}
		else{ 
			System.out.println("EXCEYTED: dcEntryId:>>"+dcEntryId+"<<");
			query.append(" and SCND.CREDIT_NOTE_SEQ_ID in (select CREDIT_NOTE_SEQ_ID from SUMADHURA_CREDIT_NOTE SCN where SCN.DC_ENTRY_ID = ? ) ");
		}
		System.out.println(query.toString());
		System.out.println(StringUtils.isNotBlank(indentEntryId)?indentEntryId:(StringUtils.isNotBlank(dcEntryId)?dcEntryId:"-"));
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{StringUtils.isNotBlank(indentEntryId)?indentEntryId:(StringUtils.isNotBlank(dcEntryId)?dcEntryId:"-")});
		
		String state = "";
		String subquery = "";
		if(StringUtils.isNotBlank(indentEntryId)){
			subquery = "select state from indent_entry where INDENT_ENTRY_ID = ?";
		}else{ subquery = "select state from dc_entry where DC_ENTRY_ID = ?";}
		state = jdbcTemplate.queryForObject(subquery,new Object[]{StringUtils.isNotBlank(indentEntryId)?indentEntryId:(StringUtils.isNotBlank(dcEntryId)?dcEntryId:"-")},String.class);
		
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String childProductId = "";
		String measurementName = "";
		String receivedQty = "";
		String price = "";
		String tax = "";
		String taxPercentage = "";
		String taxAmount = "";
		String amountAfterTax = "";
		String otherCharges = "";
		String otherChargesAfterTax = "";
		String totalAmount = "";
		String hsnCode = "";
		String basicAmount = "";
		String taxOnOtherTransportChg = "";
		String cgst = "";
		String sgst = "";
		String igst = "";
		String cgstRate = "";
		String sgstRate = "";
		String igstRate = "";
		int sno = 0;
		DecimalFormat df2 = new DecimalFormat("#.##"); 
		double addCgst = 0;
		double addSgst = 0;
		double addIgst = 0;
		double totalTaxAmount = 0;
		double totalAmountAfterTax = 0;
		double totalQty=0;
		double totalBasicAmt=0;
		
		
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			//state = map.get("STATE")==null ? "" : map.get("STATE").toString();
			productName = map.get("PRODUCT_NAME")==null ? "" : map.get("PRODUCT_NAME").toString();
			subProductName = map.get("SUB_PRODUCT_NAME")==null ? "" : map.get("SUB_PRODUCT_NAME").toString();
			childProductName = map.get("CHILD_PRODUCT_NAME")==null ? "" : map.get("CHILD_PRODUCT_NAME").toString();
			childProductId = map.get("CHILD_PRODUCT_ID")==null ? "" : map.get("CHILD_PRODUCT_ID").toString();
			measurementName = map.get("MEASUREMENT_NAME")==null ? "" : map.get("MEASUREMENT_NAME").toString();
			receivedQty = map.get("RECEIVED_QTY")==null ? "0" : map.get("RECEIVED_QTY").toString();
			price = map.get("PRICE")==null ? "0" : map.get("PRICE").toString();
			tax = map.get("TAX_PERCENTAGE")==null ? "0" : map.get("TAX_PERCENTAGE").toString();
			taxAmount = map.get("TAX_AMOUNT")==null ? "0" : map.get("TAX_AMOUNT").toString();
			amountAfterTax = map.get("AMOUNT_AFTER_TAX")==null ? "0" : map.get("AMOUNT_AFTER_TAX").toString();
			otherCharges = map.get("OTHER_CHARGES")==null ? "0" : map.get("OTHER_CHARGES").toString();
			otherChargesAfterTax = map.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : map.get("OTHER_CHARGES_AFTER_TAX").toString();
			totalAmount = map.get("TOTAL_AMOUNT")==null ? "0" : map.get("TOTAL_AMOUNT").toString();
			hsnCode = map.get("HSN_CODE")==null ? "" : map.get("HSN_CODE").toString();
			basicAmount = map.get("BASIC_AMOUNT")==null ? "0" : map.get("BASIC_AMOUNT").toString();
			taxOnOtherTransportChg = map.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "0" : map.get("TAX_ON_OTHER_TRANSPORT_CHG").toString();
			bean.setSno(++sno);
			bean.setProductName(productName);
			bean.setSubProductName(subProductName);
			bean.setChildProductName(childProductName);
			bean.setChildProductId(childProductId);
			bean.setMeasurementName(measurementName);
			bean.setReceivedQty(receivedQty);
			bean.setPrice(price);
			bean.setTaxAmount(taxAmount);
			bean.setAmountAfterTax(amountAfterTax);
			bean.setOtherCharges(otherCharges);
			bean.setOtherChargesAfterTax(otherChargesAfterTax);
			bean.setTotalAmount(totalAmount);
			bean.setHsnCode(hsnCode);
			//bean.setTaxableValue(basicAmount);
			bean.setTaxOnOtherTransportChg(taxOnOtherTransportChg);
			
			
			
			
			
			
			double doubleBasicAmount = Double.valueOf(receivedQty)*Double.valueOf(price);
			bean.setBasicAmount(df2.format(doubleBasicAmount));
			totalBasicAmt += doubleBasicAmount;
			totalQty += Double.valueOf(receivedQty);
			
			tax = tax.replace("%", "");
			if(state.equals("Local")){
				double taxAmtIncludeTransport = Double.valueOf(taxAmount)+Double.valueOf(taxOnOtherTransportChg);
				cgst = sgst = df2.format(taxAmtIncludeTransport/2);
				cgstRate = sgstRate = df2.format(Double.valueOf(tax)/2);
				igst = igstRate = "-";
				addCgst += Double.valueOf(cgst);
				addSgst += Double.valueOf(sgst);
				totalTaxAmount += taxAmtIncludeTransport;
				
			}
			if(state.equals("Non Local")){
				double taxAmtIncludeTransport = Double.valueOf(taxAmount)+Double.valueOf(taxOnOtherTransportChg);
				igst = df2.format(taxAmtIncludeTransport);
				igstRate = df2.format(Double.valueOf(tax));
				cgst = sgst = cgstRate = sgstRate = "-";
				addIgst += Double.valueOf(igst);
				totalTaxAmount += taxAmtIncludeTransport;
				
			}
			totalAmountAfterTax += Double.valueOf(totalAmount);
			
			bean.setCgst(cgst);
			bean.setSgst(sgst);
			bean.setIgst(igst);
			bean.setCgstRate(cgstRate);
			bean.setSgstRate(sgstRate);
			bean.setIgstRate(igstRate);
			
			
			
			
			
			beanlist.add(bean);
		}
		Totals.setTotalQty(df2.format(totalQty));
		Totals.setTotalBasicAmt(df2.format(totalBasicAmt));
		Totals.setAddCgst(df2.format(addCgst));
		Totals.setAddSgst(df2.format(addSgst));
		Totals.setAddIgst(df2.format(addIgst));
		Totals.setTotalTaxAmount(df2.format(totalTaxAmount));
		Totals.setTotalAmountAfterTax(df2.format(totalAmountAfterTax));
		return beanlist;
		
	}
	@Override
	public DebitNoteBean getCreditNote(String indentEntryId,String dcEntryId) {
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append(" select SCN.CREDIT_NOTE_NUMBER,SCN.INDENT_ENTRY_NUMBER,SCN.INVOICE_NUMBER,SCN.CREDIT_TOTAL_AMOUNT,SCN.DC_NUMBER,SCN.DC_ENTRY_ID,SCN.TYPE,SCN.ENTRY_DATE,SCN.CREDIT_NOTE_SEQ_ID,SCN.DOCUMENT_NO ")
			 .append(" from SUMADHURA_CREDIT_NOTE SCN "); 
		if(StringUtils.isNotBlank(indentEntryId)){ 
			query.append(" where SCN.INDENT_ENTRY_NUMBER = ? ");
		}
		else{ 
			query.append(" where SCN.DC_ENTRY_ID = ? ");
		}
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{StringUtils.isNotBlank(indentEntryId)?indentEntryId:(StringUtils.isNotBlank(dcEntryId)?dcEntryId:"-")});
		
		String creditNoteNumber = "";
		String indentEntryNumber = "";
		String invoiceNumber = "";
		String creditTotalAmount = "";
		String dcNumber = "";
		String dcEntryNumber = "";
		String type = "";
		String entryDate = "";
		String creditNoteSeqId = "";
		String documentNo = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			creditNoteNumber = map.get("CREDIT_NOTE_NUMBER")==null ? "" : map.get("CREDIT_NOTE_NUMBER").toString();
			indentEntryNumber = map.get("INDENT_ENTRY_NUMBER")==null ? "" : map.get("INDENT_ENTRY_NUMBER").toString();
			invoiceNumber = map.get("INVOICE_NUMBER")==null ? "" : map.get("INVOICE_NUMBER").toString();
			creditTotalAmount = map.get("CREDIT_TOTAL_AMOUNT")==null ? "" : map.get("CREDIT_TOTAL_AMOUNT").toString();
			dcNumber = map.get("DC_NUMBER")==null ? "" : map.get("DC_NUMBER").toString();
			dcEntryNumber = map.get("DC_ENTRY_ID")==null ? "" : map.get("DC_ENTRY_ID").toString();
			type = map.get("TYPE")==null ? "" : map.get("TYPE").toString();
			entryDate = map.get("ENTRY_DATE")==null ? "" : map.get("ENTRY_DATE").toString();
			creditNoteSeqId = map.get("CREDIT_NOTE_SEQ_ID")==null ? "" : map.get("CREDIT_NOTE_SEQ_ID").toString();
			documentNo = map.get("DOCUMENT_NO")==null ? "" : map.get("DOCUMENT_NO").toString();
			bean.setCreditNoteNumber(creditNoteNumber);
			bean.setIndentEntryNumber(indentEntryNumber);
			bean.setInvoiceNumber(invoiceNumber);
			bean.setCreditTotalAmount(creditTotalAmount);
			bean.setDcNumber(dcNumber);
			bean.setDcEntryNumber(dcEntryNumber);
			bean.setType(type);
			bean.setEntryDate(DateUtil.convertDBDateInAnotherFormat(entryDate,"dd-MM-yyyy"));
			bean.setCreditNoteSeqId(creditNoteSeqId);
			bean.setDocumentNo(documentNo);
			break;
		}
		return bean;
	}
	@Override
	public DebitNoteBean getVendorDetails(String vendorId){
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,VD.STATE,VD.STATE_CODE from VENDOR_DETAILS VD where VD.VENDOR_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{vendorId});
		String vendorName = "";
		String address = "";
		String gsinNumber = "";
		String state = "";
		String stateCode = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			vendorName = map.get("VENDOR_NAME")==null ? "" : map.get("VENDOR_NAME").toString();
			address = map.get("ADDRESS")==null ? "" : map.get("ADDRESS").toString();
			gsinNumber = map.get("GSIN_NUMBER")==null ? "" : map.get("GSIN_NUMBER").toString();
			state = map.get("STATE")==null ? "" : map.get("STATE").toString();
			stateCode = map.get("STATE_CODE")==null ? "" : map.get("STATE_CODE").toString();
			bean.setVendorName(vendorName);
			bean.setAddress(address);
			bean.setGsinNumber(gsinNumber);
			bean.setState(state);
			bean.setStateCode(stateCode);
			break;
		}
		return bean;
	}
	@Override
	public DebitNoteBean getInvoiceDetails(String indentEntryId){
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select IE.INVOICE_ID,IE.INVOICE_DATE from INDENT_ENTRY IE where IE.INDENT_ENTRY_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{indentEntryId});
		String invoiceId = "";
		String invoiceDate = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			invoiceId = map.get("INVOICE_ID")==null ? "" : map.get("INVOICE_ID").toString();
			invoiceDate = map.get("INVOICE_DATE")==null ? "" : map.get("INVOICE_DATE").toString();
			bean.setInvoiceId(invoiceId);
			bean.setInvoiceDate(DateUtil.convertDBDateInAnotherFormat(invoiceDate,"dd-MM-yyyy"));
			break;
		}
		return bean;
	}
	@Override
	public DebitNoteBean getDCDetails(String dcEntryId){
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select DE.DC_NUMBER,DE.DC_DATE from DC_ENTRY DE where DE.DC_ENTRY_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{dcEntryId});
		String dcNo = "";
		String dcDate = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			dcNo = map.get("DC_NUMBER")==null ? "" : map.get("DC_NUMBER").toString();
			dcDate = map.get("DC_DATE")==null ? "" : map.get("DC_DATE").toString();
			bean.setDcNo(dcNo);
			bean.setDcDate(DateUtil.convertDBDateInAnotherFormat(dcDate,"dd-MM-yyyy"));
			break;
		}
		return bean;
	}
	@Override
	public DebitNoteBean getSiteDetailsFromVendorTable(String siteId){
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select VD.STATE,VD.STATE_CODE from VENDOR_DETAILS VD where VD.VENDOR_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{siteId});
		String siteState = "";
		String siteStateCode = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			siteState = map.get("STATE")==null ? "" : map.get("STATE").toString();
			siteStateCode = map.get("STATE_CODE")==null ? "" : map.get("STATE_CODE").toString();
			bean.setSiteState(siteState);
			bean.setSiteStateCode(siteStateCode);
			break;
		}
		return bean;
	}
	@Override
	public DebitNoteBean getSiteDetails(String siteId){
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select S.SITE_NAME from SITE S where S.SITE_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{siteId});
		String siteName = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			siteName = map.get("SITE_NAME")==null ? "" : map.get("SITE_NAME").toString();
			bean.setSiteName(siteName);
			break;
		}
		return bean;
	}
	@Override
	public List<DebitNoteBean> getPOEntryDetails(String poEntryId){
		List<DebitNoteBean>  beanlist = new ArrayList<DebitNoteBean>();
		DebitNoteBean bean = null;
		StringBuilder query = new StringBuilder();
		query.append("select SPED.DISCOUNT,SPED.CHILD_PRODUCT_ID from SUMADHURA_PO_ENTRY_DETAILS SPED where SPED.PO_ENTRY_ID = ? ");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), new Object[]{poEntryId});
		String discount = "";
		String childProductId = "";
		for(Map<String, Object> map : list){
			bean = new DebitNoteBean();
			discount = map.get("DISCOUNT")==null ? "0" : map.get("DISCOUNT").toString();
			childProductId = map.get("CHILD_PRODUCT_ID")==null ? "" : map.get("CHILD_PRODUCT_ID").toString();
			bean.setDiscount(discount);
			bean.setChildProductId(childProductId);
			beanlist.add(bean);
		}
		return beanlist;
	}


	@Override
	public boolean isIndentEntryIdHasCreditNote(String strIndentEntryId) {
		String query = "select count(*) from SUMADHURA_CREDIT_NOTE where INDENT_ENTRY_NUMBER = ? ";
		int count = jdbcTemplate.queryForInt(query,new Object[]{strIndentEntryId});
		if(count>0){return true;}
		else {return false;}
	}
	@Override
	public String getSiteAddressByUsingSiteId(String reqSiteId){
		
		String query = "SELECT ADDRESS FROM SITE where SITE_ID ='"+reqSiteId+"'";
		String address = jdbcTemplate.queryForObject(query, String.class);	
		return address;
	}
	public List<Map<String, Object>> getVendorOrSiteAddress(String siteId) {

		List<Map<String, Object>> dbIndentDts = null;
		String query = "select VENDOR_NAME,STATE,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,VENDOR_CON_PER_NAME,LANDLINE_NO,EMP_EMAIL from VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] { siteId });
		return dbIndentDts;
	}
	public String getState(String strSiteId, String strVendorId){
		String state = "";
		String receiverState = "";
		String strVendorGSTIN = "";
		// receiver address taken here so using same methode like getVendorOrSiteAddress
		List<Map<String, Object>> listReceiverDtls = getVendorOrSiteAddress(strSiteId);// strToSite
		for (Map<String, Object> prods : listReceiverDtls) {
			receiverState = prods.get("STATE") == null ? "" : prods.get("STATE").toString();

		}
		// getting vendor details 
		List<Map<String, Object>> listVendorDtls = getVendorOrSiteAddress(strVendorId);
		for (Map<String, Object> prods : listVendorDtls) {
			strVendorGSTIN = prods.get("GSIN_NUMBER") == null ? "": prods.get("GSIN_NUMBER").toString();

		}

		if (receiverState.equalsIgnoreCase("Telangana")) {
			if (strVendorGSTIN.startsWith("36")) {//hyd-36
				state = "1";
			}else {
				state = "2";
			}
		}
		else{
			if (strVendorGSTIN.startsWith("29")) {//karnataka-29 
				state = "1";
			}
			else{
				state = "2";
			}
		}
		System.out.println(strSiteId);
		System.out.println(strVendorId);
		System.out.println(state);
		return state;
	}
	
}

