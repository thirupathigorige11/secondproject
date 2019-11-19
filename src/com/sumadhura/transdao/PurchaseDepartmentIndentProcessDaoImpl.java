package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.TransportChargesDto;
import com.sumadhura.service.EmailFunction;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.UIProperties;


@Repository("purchaseDeptIndentrocessDao")
public class PurchaseDepartmentIndentProcessDaoImpl extends UIProperties implements PurchaseDepartmentIndentProcessDao{

	static Logger log = Logger.getLogger(DCFormDaoImpl.class);
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;
	
	
	@Autowired
	private IndentCreationDao icd;


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<IndentCreationDto> getPendingIndents(String pendingEmpId,String strSiteId) {
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationDto> list = new ArrayList<IndentCreationDto>();
		IndentCreationDto indentObj = null; 

		String query = "SELECT SITEWISE_INDENT_NO,INDENT_CREATION_ID, CREATE_DATE, SCHEDULE_DATE, REQUIRED_DATE,INDENT_NAME FROM SUMADHURA_INDENT_CREATION where PENDING_EMP_ID = ? AND STATUS = 'A' and SITE_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {
				pendingEmpId,strSiteId
		});
		for(Map<String, Object> prods : dbIndentDts) {
			indentObj = new IndentCreationDto();

			indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "0" :   prods.get("INDENT_CREATION_ID").toString()));
			indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentObj.setIndentName(prods.get("INDENT_NAME")==null ? "-" :   prods.get("INDENT_NAME").toString());
			String strCreateDate = prods.get("CREATE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			String strScheduleDate = prods.get("SCHEDULE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("SCHEDULE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			try {
				indentObj.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate));
				indentObj.setScheduleDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strScheduleDate));
				indentObj.setRequiredDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			list.add(indentObj);
		}

		return list;
	}



	@Override
	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String strRemarks = "";
		String strdetailedRemarks = "";
		String dbRemarks="";
		//Date myDate = new Date();
		//String requesteddate = new SimpleDateFormat("dd-MMM-YY").format(myDate);
		//String siteIdQuery = "SELECT SITE_ID FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = "+indentNumber; 
		//String siteId = jdbcTemplate.queryForObject(siteIdQuery, String.class);

		String query ="SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,CP.MATERIAL_GROUP_ID,MST.NAME as MEASUREMENT_NAME,"
					+" SICD.PRODUCT_ID,SICD.SUB_PRODUCT_ID,SICD.CHILD_PRODUCT_ID,SICD.MEASUREMENT_ID,SICD.REQ_QUANTITY,SICD.AVAIL_QUANTITY_AT_CREATION,SICD.REMARKS,"
					+" SICD.INDENT_CREATION_DETAILS_ID,SICD.RECEIVE_QUANTITY,(select PO_INTIATED_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP " 
					+" where SPDIP.INDENT_CREATION_DETAILS_ID=SICD.INDENT_CREATION_DETAILS_ID)AS PO_INIT_QUANTITY,(select TYPE_OF_PURCHASE from SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP " 
					+" where SPDIP.INDENT_CREATION_DETAILS_ID=SICD.INDENT_CREATION_DETAILS_ID)AS TYPE_OF_PURCHASE FROM SUMADHURA_INDENT_CREATION_DTLS SICD, PRODUCT P," 
					+" SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST WHERE SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "
					+" AND SICD.MEASUREMENT_ID=MST.MEASUREMENT_ID  AND SICD.INDENT_CREATION_ID= ? order by SICD.INDENT_CREATION_DETAILS_ID";
		
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			String prodId = prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString();
			String subProductId = prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString();
			String childProdId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String measurementId = prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString();

			indentCreationBean.setProductId1(prodId);
			indentCreationBean.setSubProductId1(subProductId);
			indentCreationBean.setChildProductId1(childProdId);
			indentCreationBean.setUnitsOfMeasurementId1(measurementId);
			indentCreationBean.setGroupId1(prods.get("MATERIAL_GROUP_ID")==null ? "0" :   prods.get("MATERIAL_GROUP_ID").toString()); // this is for material boq purpose written this one

			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setRequiredQuantity1(prods.get("REQ_QUANTITY")==null ? "" :   prods.get("REQ_QUANTITY").toString());
			indentCreationBean.setProductAvailability1(prods.get("AVAIL_QUANTITY_AT_CREATION")==null ? "0" :   prods.get("AVAIL_QUANTITY_AT_CREATION").toString());
			indentCreationBean.setReceivedQuantity(prods.get("RECEIVE_QUANTITY")==null ? "0" :   prods.get("RECEIVE_QUANTITY").toString());
			indentCreationBean.setPoIntiatedQuantity(prods.get("PO_INIT_QUANTITY")==null ? "0" :   prods.get("PO_INIT_QUANTITY").toString());
			indentCreationBean.setType_Of_Purchase(prods.get("TYPE_OF_PURCHASE")==null ? "-" :   prods.get("TYPE_OF_PURCHASE").toString());

			/**/


			//As per rafi code this is not available 

			/*String dbProductAvailability = "";	
			String indentAvaQry = "SELECT  SUM(AVAILABLE_QUANTITY) FROM SUMADHURA_PRICE_LIST  WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND UNITS_OF_MEASUREMENT = ? AND SITE_ID = ? AND CREATED_DATE<= TO_DATE(?,'dd-MM-yy')";
			dbProductAvailability = jdbcTemplate.queryForObject(indentAvaQry, new Object[] {
					prodId,
					subProductId,
					childProdId,
					measurementId,siteId,requesteddate

			},String.class

			);

			if( dbProductAvailability == null || dbProductAvailability.equals("") ){

				dbProductAvailability = "0";
			}




			indentCreationBean.setProductAvailability1(dbProductAvailability);*/

			/**/
			strRemarks = prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString();
			dbRemarks=strRemarks;
			indentCreationBean.setRemarks(dbRemarks);

			if(strRemarks.contains("@@@")){
				String strRemarksArr[] = strRemarks.split("@@@");

				for(int i =0 ; i< strRemarksArr.length;i++){

					strdetailedRemarks += " "+(i+1)+")  "+strRemarksArr [i];

				}
				strRemarks = strdetailedRemarks;
			}
			strdetailedRemarks = "";

			//	strRemarks=HtmlUtils.htmlEscape(strRemarks);
			indentCreationBean.setRemarks1(strRemarks);
			//indentCreationBean.setRemarks1(prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString());
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}	
		return list;
	}



	@Override
	public int insertTempPOEntryDetails(ProductDetails productDetails,String poEntrySeqNo)
	{
		String query = "INSERT INTO SUMADHURA_TEMP_PO_ENTRY_DTLS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
		"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,INDENT_CREATION_DTLS_ID,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC,MATERIAL_GROUP_ID"
		+ ") values(SUMADHURA_T_PO_ENTRY_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poEntrySeqNo,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),"0","0",
				productDetails.getTotalAmount(),productDetails.getHsnCode(),"0",
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getIndentCreationDetailsId(),productDetails.getChildProductCustDisc(),productDetails.getChild_ProductName(),productDetails.getGroupId()

		});
		return result;
	}
	@Override
	public int insertPOEntryDetails(ProductDetails productDetails,int poEntrySeqNo)
	{
		String query = "INSERT INTO SUMADHURA_PO_ENTRY_DETAILS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
		"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,INDENT_CREATION_DTLS_ID,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC,RECEIVED_QUANTITY,MATERIAL_GROUP_ID"
		+ ") values(SUMADHURA_PO_ENTRY_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poEntrySeqNo,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),
				productDetails.getOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),productDetails.getHsnCode(),productDetails.getTaxOnOtherOrTransportCharges1(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getIndentCreationDetailsId(),productDetails.getChildProductCustDisc(),productDetails.getChild_ProductName(),productDetails.getStrPoAlreadyReceivedQuantity(),productDetails.getGroupId()

		});
		return result;
	}







	@Override
	public int updateEnquiryFormDetails(ProductDetails productDetails)
	{

		String query =  "update SUMADHURA_ENQUIRY_FORM_DETAILS set VENDOR_MENTIONED_QTY = ?, PRICE = ?, TAX= ?,TAX_AMOUNT = ?,AMOUNT_AFTER_TAX = ?, "+
		" OTHER_CHARGES = ?,OTHER_CHARGES_AFTER_TAX = ?,TOTAL_AMOUNT = ?,HSN_CODE = ?,BASIC_AMOUNT = ?,TAX_ON_OTHER_TRANSPORT_CHG = ?,"+
		" DISCOUNT = ?,AMOUNT_AFTER_DISCOUNT = ?,CHILDPROD_CUST_DESC = ? where INDENT_CREATION_DETAILS_ID = ? and VENDOR_ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {

				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getTaxId(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),productDetails.getOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),productDetails.getHsnCode(),productDetails.getBasicAmt(),productDetails.getTaxOnOtherOrTransportCharges1(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),

				productDetails.getChildProductCustDisc(),productDetails.getIndentCreationDetailsId(),
				productDetails.getVendorId()


		});
		return result;
	}

	public int getCountInEnquiry(int indentCreationDetailsId,String vendorId ){
		String query  = "select count(1) from  SUMADHURA_ENQUIRY_FORM_DETAILS where INDENT_CREATION_DETAILS_ID = ? and VENDOR_ID = ? ";
		int intCount =    jdbcTemplate.queryForInt(query, new Object[] {indentCreationDetailsId,vendorId});
		return intCount;
	}

	@Override
	public int insertVendorEnquiryFormDetails(IndentCreationBean indentCreationBean)
	{
		int count = getCountInEnquiry(indentCreationBean.getIndentCreationDetailsId(),indentCreationBean.getVendorId());
		if(count>0){
			String query = "UPDATE SUMADHURA_ENQUIRY_FORM_DETAILS set INDENT_QTY = ? ,"+
			" PURCHASE_DEPT_CHILD_PROD_DISC = ?  where INDENT_CREATION_DETAILS_ID = ? and VENDOR_ID = ?  ";

			int result = jdbcTemplate.update(query, new Object[] {
					indentCreationBean.getRequiredQuantity1(),
					indentCreationBean.getPurchaseDeptChildProdDisc(),indentCreationBean.getIndentCreationDetailsId(),
					indentCreationBean.getVendorId()


			});
			return result;
		}
		else{
			String query = "INSERT INTO SUMADHURA_ENQUIRY_FORM_DETAILS(ENQUIRY_FORM_DETAILS_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
			"MEASUREMENT_ID, INDENT_QTY,ENTRY_DATE,"+
			" VENDOR_ID ,SITE_ID, INDENT_NO,PURCHASE_DEPT_CHILD_PROD_DISC,INDENT_CREATION_DETAILS_ID) "+
			"values(?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?,?,?)";

			int result = jdbcTemplate.update(query, new Object[] {
					indentCreationBean.getEnquiryFormDetailsId(),
					indentCreationBean.getProductId1(),indentCreationBean.getSubProductId1(),
					indentCreationBean.getChildProductId1(),indentCreationBean.getUnitsOfMeasurementId1(),
					indentCreationBean.getRequiredQuantity1(),
					indentCreationBean.getVendorId(),indentCreationBean.getSiteId(),indentCreationBean.getIndentNumber(),
					indentCreationBean.getPurchaseDeptChildProdDisc(),indentCreationBean.getIndentCreationDetailsId()


			});
			return result;
		}

	}




	@Override
	public int getEnquiryFormDetailsId()
	{
		int enquiryFormSeqId = 0;

		String query = "select SUM_ENQUIRY_FORM_DETAILS_SEQ.nextval from dual";

		enquiryFormSeqId = jdbcTemplate.queryForInt(query);


		return enquiryFormSeqId;

	}

	@Override
	public List<Map<String, Object>> getVendorOrSiteAddress(String siteId) {

		List<Map<String, Object>> dbIndentDts = null;
		String query = "select VENDOR_NAME,STATE,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,VENDOR_CON_PER_NAME,LANDLINE_NO,EMP_EMAIL,VENDOR_CON_PER_NAME_TWO,MOBILE_NUMBER_TWO from VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {siteId});
		return dbIndentDts;
	}


	@Override
	public int updatePurchaseDeptIndentProcesstbl(double intQuantity,int intIndentCreationDetailsId,String strStatus) {



		double poIntiatedQuantity  = 0.0;
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationDto> list = new ArrayList<IndentCreationDto>();
		IndentCreationDto indentObj = null; 

		String query = "select PO_INTIATED_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {
				intIndentCreationDetailsId
		});
		for(Map<String, Object> prods : dbIndentDts) {
			//indentObj = new IndentCreationDto();

			poIntiatedQuantity  = Double.valueOf(prods.get("PO_INTIATED_QUANTITY")==null ? "0" :   prods.get("PO_INTIATED_QUANTITY").toString());

		} 



		/*String query = "select PO_INTIATED_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID = ?";

		double poIntiatedQuantity = jdbcTemplate.queryForInt(query, new Object[] {intIndentCreationDetailsId});
		 */
		poIntiatedQuantity = poIntiatedQuantity+intQuantity;

		poIntiatedQuantity = Double.parseDouble(new DecimalFormat("##.##").format(poIntiatedQuantity));
		String strPoIntiatedQuantity = String.valueOf(poIntiatedQuantity);
		

		query = "update SUM_PURCHASE_DEPT_INDENT_PROSS set " +
		"PO_INTIATED_QUANTITY = ?,STATUS=?"+
		" where INDENT_CREATION_DETAILS_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				strPoIntiatedQuantity,strStatus,intIndentCreationDetailsId
		});
		return result;

	}

	@Override
	public List<Map<String, Object>> getAllProducts(String purchaseDeptId) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT distinct(P.PRODUCT_ID),P.NAME as PRODUCT_NAME FROM PRODUCT P, SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC "+
		"WHERE SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID "+
		"AND SIC.PENDIND_DEPT_ID = ? AND SIC.STATUS = 'A' "+
		"AND SICD.PRODUCT_ID = P.PRODUCT_ID ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {purchaseDeptId});
		return dbIndentDts;
	}

	/*@Override
	public int getPoEnterSeqNoOrMaxId(){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String intSeqNum = "select max(PO_NUMBER) from SUMADHURA_PO_ENTRY";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result+1;
	}*/


	public int getPoEnterSeqNoOrMaxId(String poState){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String intSeqNum = "select YEARWISE_NUMBER from SUMADHURA_HO_WISE_PO_NUMBER where SERVICE_NAME='"+poState+"' ";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result;
	}

	public String getSiteWisePoNumber(String siteWise_Number) {
		String query = "SELECT SITE_WISE_NUMBER FROM SUMADHURA_SITE_WISE_PO_NUMBER where SITE_ID = '"+siteWise_Number+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}

	public String getStateWiseYearPoNumber(String siteWise_Number) {
		String query = "SELECT YEARWISE_NUMBER FROM SUMADHURA_SITE_WISE_PO_NUMBER where SITE_ID = '"+siteWise_Number+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}







	@Override
	public int getPoEnterSeqNo(){

		String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		//String intSeqNum = "select max(PO_ENTRY_ID) from SUMADHURA_PO_ENTRY";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result;
	}

	@Override
	public int getTempPoEnterSeqNoOrMaxId(){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String intSeqNum = "select max(PO_ENTRY_ID) from SUMADHURA_TEMP_PO_ENTRY";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result+1;
	}


	@Override
	public int getPoTransChrgsEntrySeqNo(){

		String intSeqNum = "select  SUMADHURA_PO_TRNS_CHRGS_SEQ.nextval from dual";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result;
	}

	@Override
	public int getEnquiryFormSeqNo(){

		String intSeqNum = "select  SUMADHURA_ENQUIRY_FORM_SEQ.nextval from dual";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result;
	}


	@Override
	public int insertTempPOEntry(ProductDetails productDetails,String tempuserId,String ccEmailId,String subject)

	{



		//String userId=session.getAttribute("UserId");
		//Date refferenceDate=DateUtil.convertToJavaDateFormat(productDetails.getStrPoPrintRefdate());
		//SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
		//String strPoDate=dt1.format(refferenceDate);
		String query = "INSERT INTO SUMADHURA_TEMP_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"+
		"PO_ENTRY_USER_ID, SITE_ID,INDENT_NO,TEMP_PO_PENDING_EMP_ID,CC_EMAIL_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,PASSWORD_MAIL,DELIVERY_DATE,PAYMENT_REQ_DAYS,OPERATION_TYPE) values(? , ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getPoNumber(),
				productDetails.getPoNumber(),
				StringUtils.isBlank(productDetails.getPoDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getPoDate()),
				productDetails.getVendorId(),"A",
				productDetails.getUserId(),
				productDetails.getSite_Id(),
				productDetails.getIndentNo(),tempuserId,ccEmailId,productDetails.getSubject(),productDetails.getBillingAddress(),
				productDetails.getVersionNo(),DateUtil.convertToJavaDateFormat(productDetails.getStrPoPrintRefdate()),productDetails.getRefferenceNo(),
				productDetails.getRevision_Number(),productDetails.getEdit_Po_Number(),productDetails.getPreparedBy(),productDetails.getPasswdForMail()
				//	productDetails.getStrTermsConditionId()
				, StringUtils.isBlank(productDetails.getStrDeliveryDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getStrDeliveryDate()),productDetails.getPayment_Req_days(),productDetails.getOperation_Type()
		});




		return result;
	}

	@Override
	public int insertPOEntry(ProductDetails productDetails)
	{
		String query = "INSERT INTO SUMADHURA_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"+
		"PO_ENTRY_USER_ID, SITE_ID,INDENT_NO,TERMS_CONDITIONS_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,DELIVERY_DATE,PAYMENT_REQ_DAYS,OPERATION_TYPE,CREATION_DATE) values(? , ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?, sysdate)";
		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getPoEntryId(),
				productDetails.getPoNumber(),
				StringUtils.isBlank(productDetails.getPoDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getPoDate()),
				productDetails.getVendorId(),"A",
				productDetails.getUserId(),
				productDetails.getSite_Id(),
				productDetails.getIndentNo(),"0",productDetails.getSubject(),productDetails.getBillingAddress(),
				productDetails.getVersionNo(),DateUtil.convertToJavaDateFormat(productDetails.getStrPoPrintRefdate()),productDetails.getRefferenceNo(),
				productDetails.getRevision_Number(),productDetails.getEdit_Po_Number(),productDetails.getPreparedBy()
				, StringUtils.isBlank(productDetails.getStrDeliveryDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getStrDeliveryDate()),productDetails.getPayment_Req_days(),productDetails.getOperation_Type()
				
		});
		return result;
	}




	@Override
	public int insertVendorEnquiryForm(ProductDetails productDetails)
	{
		String query = "INSERT INTO SUMADHURA_ENQUIRY_FORM(ENQUIRY_FORM_ID,VENDOR_ID,"+
		" SITE_ID,INDENT_NO,ENTRY_DATE) values(?, ?, ?, ?,sysdate)";
		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getPoEntrySeqNumber(),
				productDetails.getVendorId(),
				productDetails.getSite_Id(),
				productDetails.getIndentNo()

		});
		return result;
	}

	@Override
	public List<IndentCreationBean> getupdatePurchaseDeptIndentProcess(int indentNumber,String strUserId,String siteId,String approvalEmpId,String sessionSiteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		int strSerialNumber = 0;
	//	double doublePendingQuantity = 0;
	//	double doublePOIntatedQuantity = 0;
	//	String strStatus = "I";
		int responseCount = 0;
		String status="";

		String query = "  select SPDIP.PENDING_QUANTIY,SPDIP.PO_INTIATED_QUANTITY,SPDIP.STATUS  from SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP ,SUMADHURA_INDENT_CREATION SIC, "+
		"SUMADHURA_INDENT_CREATION_DTLS SICD "+
		"where  SPDIP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID and "+
		" SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID and SIC.INDENT_CREATION_ID = ? ";

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {


			status=prods.get("STATUS")==null ? "0" :   prods.get("STATUS").toString();
		//	doublePendingQuantity = Double.valueOf(prods.get("PENDING_QUANTIY")==null ? "0" :   prods.get("PENDING_QUANTIY").toString());
		//	doublePOIntatedQuantity = Double.valueOf(prods.get("PO_INTIATED_QUANTITY")==null ? "0" :   prods.get("PO_INTIATED_QUANTITY").toString());

			if(status.equals("A")){
				//strStatus = "A";
				break;
			}



			//	list.add(indentCreationBean);
		}	


		if(status.equals("I")){

			if(!approvalEmpId.equals("VND")){
				approvalEmpId = "998_PDM";
			}

			/*query = "	   insert into SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID,INDENT_CREATION_ID,INDENT_TYPE, "+
			" CREATION_DATE,SITE_ID,INDENT_CREATE_APPROVE_EMP_ID) values(INDENT_CREATION_APPROVAL_SEQ.NEXTVAL,?,?,"+
			"sysdate,?,?)";
			jdbcTemplate.update(query, new Object[]  {indentNumber,"A",sessionSiteId,strUserId});*/

			query = "update SUMADHURA_INDENT_CREATION set PENDIND_DEPT_ID = ?, MODIFYDATE= sysdate where INDENT_CREATION_ID = ?";
			responseCount = jdbcTemplate.update(query, new Object[]  {approvalEmpId,indentNumber});



		}	
		jdbcTemplate.setLazyInit(false);
		return list;
	}
	@Override
	public List<ProductDetails> getIndentsProductWise(String product, String subProduct, String childProduct) {
		List<Map<String, Object>> productList = null;
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();

		/*String sql = "SELECT P.PRODUCT_ID,P.NAME as PRODUCT_NAME, SP.SUB_PRODUCT_ID, SP.NAME as SUB_PRODUCT_NAME, CP.CHILD_PRODUCT_ID,CP.NAME as CHILD_PRODUCT_NAME,"+
				"M.NAME as MEASUREMENT_NAME, SICD.REQ_QUANTITY, S.SITE_NAME,SIC.INDENT_CREATION_ID "+
				"FROM SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC, "+ 
				"PRODUCT P, SUB_PRODUCT SP, CHILD_PRODUCT CP, MEASUREMENT M, SITE S "+
				"WHERE "+
				"SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID "+
				"AND SIC.PENDIND_DEPT_ID = '998' AND SIC.STATUS = 'A' "+
				"AND SICD.PRODUCT_ID = P.PRODUCT_ID "+
				"AND SICD.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID "+
				"AND SICD.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID "+
				"AND SICD.MEASUREMENT_ID = M.MEASUREMENT_ID "+
				"AND SIC.SITE_ID = S.SITE_ID ";
		String [] prod = product.split("@@");
		String prodId = prod[0];
		String [] subProd;
		String subProdId;
		String [] childProd;
		String childProdId;
		if(subProduct.equals("@@"))
		{
			sql+="AND SICD.PRODUCT_ID = ? ";
			productList = jdbcTemplate.queryForList(sql, new Object[] { prodId });
		}
		else{
			subProd = subProduct.split("@@");
			subProdId = subProd[0];

			if(childProduct.equals("@@"))
			{
				sql+="AND SICD.PRODUCT_ID = ? "+
						"AND SICD.SUB_PRODUCT_ID = ? ";
				productList = jdbcTemplate.queryForList(sql, new Object[] { prodId, subProdId });
			}
			else
			{
				childProd = childProduct.split("@@");
				childProdId = childProd[0];
				sql+="AND SICD.PRODUCT_ID = ? "+
						"AND SICD.SUB_PRODUCT_ID = ? "+
						"AND SICD.CHILD_PRODUCT_ID = ? ";
				productList = jdbcTemplate.queryForList(sql, new Object[] { prodId, subProdId, childProdId });
			}
		}*/
		String prodId = "";
		String subProdId = "";
		String childProdId = "";


		String [] childProd;

		if(!product.equals("@@")){

			String [] prod = product.split("@@");
			prodId = prod[0];
		}

		if(!subProduct.equals("@@") && !subProduct.equals("")){
			String [] subProd;
			subProd = subProduct.split("@@");
			subProdId = subProd[0];

		}

		if(!childProduct.equals("@@") && !childProduct.equals("")){
			childProd = childProduct.split("@@");
			childProdId = childProd[0];

		}
		if(!prodId.equals("") && subProdId.equals("") && childProdId.equals("")){

			String sql = " SELECT SPDP.PRODUCT_ID,P.NAME as PRODUCT_NAME, SPDP.SUB_PRODUCT_ID, SP.NAME as SUB_PRODUCT_NAME, SPDP.CHILD_PRODUCT_ID, SPDP.MEASUREMENT_ID ,  CP.NAME as CHILD_PRODUCT_NAME, 	M.NAME as MEASUREMENT_NAME, SPDP.PENDING_QUANTIY, S.SITE_NAME,S.SITE_ID,SPDP.INDENT_CREATION_DETAILS_ID ,  SPDP.PURCHASE_DEPT_INDENT_PROSS_SEQ ,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.INDENT_NAME	FROM  SUM_PURCHASE_DEPT_INDENT_PROSS SPDP,  PRODUCT P, SUB_PRODUCT SP, CHILD_PRODUCT CP, MEASUREMENT M, SITE S , SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC 	WHERE   SPDP.STATUS = 'A'  AND SPDP.PRODUCT_ID = P.PRODUCT_ID  	AND SPDP.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID  AND SPDP.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID  AND SPDP.MEASUREMENT_ID = M.MEASUREMENT_ID  AND SPDP.INDENT_REQ_SITE_ID = S.SITE_ID  AND SPDP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID  AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID"
				+ " and SPDP.PRODUCT_ID = ? ";

			productList = jdbcTemplate.queryForList(sql, new Object[] { prodId });

		}
		if(!prodId.equals("") && !subProdId.equals("") && childProdId.equals("")){

			String sql = " SELECT SPDP.PRODUCT_ID,P.NAME as PRODUCT_NAME, SPDP.SUB_PRODUCT_ID, SP.NAME as SUB_PRODUCT_NAME, SPDP.CHILD_PRODUCT_ID, SPDP.MEASUREMENT_ID ,  CP.NAME as CHILD_PRODUCT_NAME, 	M.NAME as MEASUREMENT_NAME, SPDP.PENDING_QUANTIY, S.SITE_NAME,S.SITE_ID,SPDP.INDENT_CREATION_DETAILS_ID ,  SPDP.PURCHASE_DEPT_INDENT_PROSS_SEQ ,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.INDENT_NAME	FROM  SUM_PURCHASE_DEPT_INDENT_PROSS SPDP,  PRODUCT P, SUB_PRODUCT SP, CHILD_PRODUCT CP, MEASUREMENT M, SITE S , SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC 	WHERE   SPDP.STATUS = 'A'  AND SPDP.PRODUCT_ID = P.PRODUCT_ID  	AND SPDP.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID  AND SPDP.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID  AND SPDP.MEASUREMENT_ID = M.MEASUREMENT_ID  AND SPDP.INDENT_REQ_SITE_ID = S.SITE_ID  AND SPDP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID  AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID"
				+ " and SPDP.PRODUCT_ID = ?  and  SPDP.SUB_PRODUCT_ID = ? ";

			productList = jdbcTemplate.queryForList(sql, new Object[] { prodId, subProdId });

		}
		if(!prodId.equals("") && !subProdId.equals("") && !childProdId.equals("")){

			String sql = " SELECT SPDP.PRODUCT_ID,P.NAME as PRODUCT_NAME, SPDP.SUB_PRODUCT_ID, SP.NAME as SUB_PRODUCT_NAME, SPDP.CHILD_PRODUCT_ID,SPDP.MEASUREMENT_ID ,  CP.NAME as CHILD_PRODUCT_NAME, 	M.NAME as MEASUREMENT_NAME, SPDP.PENDING_QUANTIY, S.SITE_NAME,S.SITE_ID,SPDP.INDENT_CREATION_DETAILS_ID ,  SPDP.PURCHASE_DEPT_INDENT_PROSS_SEQ ,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.INDENT_NAME	FROM  SUM_PURCHASE_DEPT_INDENT_PROSS SPDP,  PRODUCT P, SUB_PRODUCT SP, CHILD_PRODUCT CP, MEASUREMENT M, SITE S , SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC 	WHERE   SPDP.STATUS = 'A'  AND SPDP.PRODUCT_ID = P.PRODUCT_ID  	AND SPDP.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID  AND SPDP.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID  AND SPDP.MEASUREMENT_ID = M.MEASUREMENT_ID  AND SPDP.INDENT_REQ_SITE_ID = S.SITE_ID  AND SPDP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID  AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID"
				+ " and SPDP.PRODUCT_ID = ? and  SPDP.SUB_PRODUCT_ID = ? and SPDP.CHILD_PRODUCT_ID = ?";

			productList = jdbcTemplate.queryForList(sql, new Object[] { prodId, subProdId, childProdId });

		}

		int sno = 0;
		for (Map ProductDetails : productList) {

			ProductDetails objProductDetails  = new ProductDetails();
			sno++;
			objProductDetails.setProductId(ProductDetails.get("PRODUCT_ID") == null ? "" : ProductDetails.get("PRODUCT_ID").toString());
			objProductDetails.setSub_ProductId(ProductDetails.get("SUB_PRODUCT_ID") == null ? "" : ProductDetails.get("SUB_PRODUCT_ID").toString());
			objProductDetails.setChild_ProductId(ProductDetails.get("CHILD_PRODUCT_ID") == null ? "" : ProductDetails.get("CHILD_PRODUCT_ID").toString());
			objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
			objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "" : ProductDetails.get("SUB_PRODUCT_NAME").toString());
			objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
			objProductDetails.setMeasurementId(ProductDetails.get("MEASUREMENT_ID") == null ? "" : ProductDetails.get("MEASUREMENT_ID").toString());
			objProductDetails.setMeasurementName(ProductDetails.get("MEASUREMENT_NAME") == null ? "" : ProductDetails.get("MEASUREMENT_NAME").toString());
			objProductDetails.setQuantity(ProductDetails.get("PENDING_QUANTIY") == null ? "" 	: ProductDetails.get("PENDING_QUANTIY").toString());
			objProductDetails.setSiteName(ProductDetails.get("SITE_NAME") == null ? "" 	: ProductDetails.get("SITE_NAME").toString());
			objProductDetails.setStrIndentId(ProductDetails.get("INDENT_CREATION_ID") == null ? "" 	: ProductDetails.get("INDENT_CREATION_ID").toString());
			objProductDetails.setSite_Id(ProductDetails.get("SITE_ID") == null ? "" 	: ProductDetails.get("SITE_ID").toString());
			objProductDetails.setSiteWiseIndentNo(ProductDetails.get("SITEWISE_INDENT_NO") == null ? "0" 	: ProductDetails.get("SITEWISE_INDENT_NO").toString());
			objProductDetails.setIndentCreationDetailsId(ProductDetails.get("INDENT_CREATION_DETAILS_ID") == null ? "" 	: ProductDetails.get("INDENT_CREATION_DETAILS_ID").toString());
			objProductDetails.setIndentName(ProductDetails.get("INDENT_NAME") == null ? "-" 	: ProductDetails.get("INDENT_NAME").toString());
			objProductDetails.setStrSerialNumber(String.valueOf(sno));

			listProductDetails.add(objProductDetails);

		}
		return listProductDetails;
	}

	@Override
	public int insertPurchaseIndentProcess(int purchaseIndentProcessId,IndentCreationBean purchaseIndentDetails,int IndentCreationDetailsId,int indentReqSiteId,String reqReceiveFrom)
	{
		String query = "INSERT INTO SUM_PURCHASE_DEPT_INDENT_PROSS(PURCHASE_DEPT_INDENT_PROSS_SEQ,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,"+
		"PURCHASE_DEPT_REQ_QUANTITY, ALLOCATED_QUANTITY, PENDING_QUANTIY, PO_INTIATED_QUANTITY,"+
		"STATUS,INDENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,INDENT_CREATION_DETAILS_ID,INDENT_REQ_QUANTITY,MATERIAL_GROUP_ID,CLOSED_INDENT_QUANT) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				purchaseIndentProcessId,
				purchaseIndentDetails.getProductId1(),
				purchaseIndentDetails.getSubProductId1(),
				purchaseIndentDetails.getChildProductId1(),
				purchaseIndentDetails.getUnitsOfMeasurementId1(),
				purchaseIndentDetails.getRequiredQuantity1(),"0",
				purchaseIndentDetails.getRequiredQuantity1(),"0",
				"A",indentReqSiteId,reqReceiveFrom,IndentCreationDetailsId,
				purchaseIndentDetails.getRequiredQuantity1(),purchaseIndentDetails.getGroupId1(),"0"
		});
		return result;
	}



	@Override
	public Map<String, String> getVendorEmail(String vendor_Id) {
		List<Map<String, Object>> dbIndentDts = null;
		Map<String, String> map = new TreeMap<String, String>();
		String email="";
		String vendor_Contact_Person="";
		
		String query = "SELECT EMP_EMAIL,VENDOR_CON_PER_NAME FROM VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {vendor_Id});
		for(Map<String, Object> prods : dbIndentDts) {
			email=prods.get("EMP_EMAIL")==null ? "0" :   prods.get("EMP_EMAIL").toString();
			vendor_Contact_Person=prods.get("VENDOR_CON_PER_NAME")==null ? "0" :   prods.get("VENDOR_CON_PER_NAME").toString();
			
			map.put(vendor_Contact_Person,email);

		}
		return map;

	}

	@Override
	public 	String getIndentCreationDate( int intIndentNumber) {
		if(intIndentNumber==0){return "";}
		String query = "select CREATE_DATE FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = '"+intIndentNumber+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}



	@Override
	public int setVendorPasswordInDB(String vendor_Pass, String vendor_Id) {
		String query = "update VENDOR_DETAILS set PASSWORD = ? where VENDOR_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				vendor_Pass,vendor_Id
		});
		return result;

	}



	@Override
	public String getVendorPasswordInDB(String vendor_Id) {
		List<Map<String, Object>> productList = null;

		String query = "select PASSWORD FROM VENDOR_DETAILS where VENDOR_ID = '"+vendor_Id+"'";
		String result = "";
		try {
			productList = jdbcTemplate.queryForList(query);



			for (Map ProductDetails : productList) {


				result = ProductDetails.get("PASSWORD") == null ? "" : ProductDetails.get("PASSWORD").toString();
			}
		}
		catch (DataAccessException e) {
			result="";
			e.printStackTrace();
		}  
		return result;
	}


	@Override
	public List<Map<String, Object>> getComparisionDetails(String indentNumber,String strProductDtls,String vendorId) {

		List<Map<String, Object>> dbIndentDts = null;
		String query = "select VD.VENDOR_NAME ,CP.NAME as CHILD_PRODUCT_NAME,M.NAME as MEASUREMENT_NAME,SEFD.INDENT_QTY,SEFD.VENDOR_MENTIONED_QTY,SPDIP.PURCHASE_DEPT_REQ_QUANTITY,"+
		" SEFD.PRICE,SEFD.BASIC_AMOUNT,SEFD.DISCOUNT,SEFD.AMOUNT_AFTER_DISCOUNT,SEFD.TAX,SEFD.TAX_AMOUNT,SEFD.AMOUNT_AFTER_TAX,SEFD.TOTAL_AMOUNT,SEFD.CHILDPROD_CUST_DESC"+
		"  ,SEFD.CHILD_PRODUCT_ID from VENDOR_DETAILS VD,CHILD_PRODUCT CP,SUMADHURA_ENQUIRY_FORM_DETAILS SEFD , MEASUREMENT M,SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP "+
		" where SEFD.VENDOR_ID = VD.VENDOR_ID and SEFD.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID and  M.MEASUREMENT_ID =SEFD.MEASUREMENT_ID"+
		" and SEFD.INDENT_NO = ?  and SEFD.VENDOR_MENTIONED_QTY is not  null AND SEFD.INDENT_CREATION_DETAILS_ID=SPDIP.INDENT_CREATION_DETAILS_ID and CP.CHILD_PRODUCT_ID in("+strProductDtls+") order by SEFD.CHILD_PRODUCT_ID ,SEFD.PRICE ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});

		return dbIndentDts;
	}



	@Override
	public double getIntiatedQuantityInPurchaseTable(String indentCreationDetailsId) {
		String query = "select PO_INTIATED_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID = '"+indentCreationDetailsId+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return Double.valueOf(result);
	}



	@Override
	public int insertPOTempTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails, TransportChargesDto transportChargesDto) {
		String query = "INSERT INTO SUMADHURA_TEMP_PO_TRNS_O_CHRGS(ID,TRANSPORT_ID,TRANSPORT_GST_PERCENTAGE,TRANSPORT_GST_AMOUNT,"
			+ "TOTAL_AMOUNT_AFTER_GST_TAX,DATE_AND_TIME,TRANSPORT_AMOUNT,SITE_ID,PO_NUMBER,INDENT_NUMBER) "
			+ "values( ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poTransChrgsSeqNo,
				transportChargesDto.getTransportId(),
				transportChargesDto.getTransportGSTPercentage(),
				transportChargesDto.getTransportGSTAmount(),
				transportChargesDto.getTotalAmountAfterGSTTax(),
				transportChargesDto.getTransportAmount(),
				productDetails.getSite_Id(),
				productDetails.getPoNumber(),
				productDetails.getIndentNo()
		});
		return result;

	}

	@Override
	public int insertPOTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails, TransportChargesDto transportChargesDto) {
		String query = "INSERT INTO SUMADHURA_PO_TRNS_O_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_GST_PERCENTAGE,TRANSPORT_GST_AMOUNT,"
			+ "TOTAL_AMOUNT_AFTER_GST_TAX,DATE_AND_TIME,TRANSPORT_AMOUNT,SITE_ID,PO_NUMBER,INDENT_NUMBER) "
			+ "values( ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poTransChrgsSeqNo,
				transportChargesDto.getTransportId(),
				transportChargesDto.getTransportGSTPercentage(),
				transportChargesDto.getTransportGSTAmount(),
				transportChargesDto.getTotalAmountAfterGSTTax(),
				transportChargesDto.getTransportAmount(),
				productDetails.getSite_Id(),
				productDetails.getPoEntryId(),
				productDetails.getIndentNo()
		});
		return result;

	}








	@Override
	public String  getTemproryuser(String strUserId)

	{
		int result=0;
		String tempUserId="";
		List<Map<String, Object>> dbIndentDts = null;
		//String userId=session.getAttribute("UserId");
		String query = " select SPAMD.APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  SPAMD.EMP_ID= ? and SPAMD.STATUS = 'A' AND SPAMD.MODULE_TYPE='PO'";

		dbIndentDts=jdbcTemplate.queryForList(query, new Object[] {strUserId});
		//	ails.getIndentNo()
		//	productDetails.getStrTermsConditionId()


		if(dbIndentDts!= null){
			for(Map<String, Object> prods : dbIndentDts) {

				tempUserId =prods.get("APPROVER_EMP_ID")==null ? "" :  prods.get("APPROVER_EMP_ID").toString();

			}	} 
		return tempUserId;


	}


	@Override
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber,String AllorNot) {

		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbMarketingPODts = null;
		List<Map<String, Object>> dbCancelPoDts = null;
		List<Map<String, Object>> dbMarketingCancelPoDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj = null; 
		String old_Po_Number="";
		String type_Of_Purchase="";
		String sql="";
		String cancelPO="";// to get data for perminent cancel po purpose werite it
		String marketingCancelPo="";
		String operationType="";
		int i=0;
		//String data="";
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			 
			template = new JdbcTemplate(DBConnection.getDbConnection());
			
			
			if(AllorNot.equalsIgnoreCase("All")){
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				sql= "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+" WHERE  STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID "
					+" and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1" 
					+" WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
				marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
								+" WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
	
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
				
			}

			else if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				sql= "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and TRUNC(SCP.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
		
				marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' and TRUNC(SCP.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
		
				
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and  SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

				cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID"
					+" and TRUNC(SCP.ENTRY_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
				marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID  AND SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT'"
					+" and TRUNC(SCP.ENTRY_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});

			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,STPE.OPERATION_TYPE,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID"
						+" and TRUNC(SCP.ENTRY_DATE)<=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
			
				marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT'"
					+" and TRUNC(SCP.ENTRY_DATE)<=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
				
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
				
			}else if(StringUtils.isNotBlank(tempPoNumber)) {
				if(!tempPoNumber.contains("PO")){
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and  STPE.PO_NUMBER='"+tempPoNumber+"' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and  SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and  STPE.PO_NUMBER='"+tempPoNumber+"' AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
				
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				}
				cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND   SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.PO_NUMBER='"+tempPoNumber+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
					+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.PO_NUMBER='"+tempPoNumber+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
				
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
				
			
			}


			/*dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
			dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
			dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
			dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});*/
			
			/*================================================Marketing po Details start===================================================================*/
			if(dbMarketingPODts!=null && dbMarketingPODts.size()>0){
			for(Map<String, Object> markPo : dbMarketingPODts) {
				indentObj = new IndentCreationBean();
				indentObj.setIntSerialNo(++i);
				indentObj.setPonumber(markPo.get("PO_NUMBER")==null ? "" : markPo.get("PO_NUMBER").toString());
				indentObj.setStatus(markPo.get("PO_STATUS")==null ? "" : markPo.get("PO_STATUS").toString());
				indentObj.setSiteName(markPo.get("SITE_NAME")==null ? "" : markPo.get("SITE_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(markPo.get("INDENT_NO")==null ? "0" : markPo.get("INDENT_NO").toString()));
				indentObj.setSiteId(Integer.parseInt(markPo.get("SITE_ID")==null ? "0" : markPo.get("SITE_ID").toString()));
				type_Of_Purchase=(markPo.get("PREPARED_BY")==null ? "" : markPo.get("PREPARED_BY").toString());
				operationType=(markPo.get("OPERATION_TYPE")==null ? "" : markPo.get("OPERATION_TYPE").toString());
				//old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
				String date=markPo.get("PO_DATE")==null ? "" : markPo.get("PO_DATE").toString();
				indentObj.setVendorName(markPo.get("VENDOR_NAME")==null ? "" : markPo.get("VENDOR_NAME").toString());
				indentObj.setPoAmount(markPo.get("TOTAL_AMOUNT")==null ? "" : markPo.get("TOTAL_AMOUNT").toString());
				indentObj.setCreatedBY(markPo.get("EMP_NAME")==null ? "" : markPo.get("EMP_NAME").toString());
				indentObj.setPending_Emp_Name(markPo.get("PENDING_EMP_NAME")==null ? "-" : markPo.get("PENDING_EMP_NAME").toString());
				indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
				if(!operationType.equalsIgnoreCase("REVISED")){
				indentObj.setType_Of_Purchase("MARKETING DEPT");
				}else{
					indentObj.setType_Of_Purchase("REVISED PO");
				}
				indentObj.setSiteWiseIndentNumber("-");
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}
			}
			/*================================================Marketing po Details end===================================================================*/
			if(dbIndentDts!=null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();
				indentObj.setIntSerialNo(++i);
				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				//	indentObj.setStrInvoiceDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setStatus(prods.get("PO_STATUS")==null ? "" : prods.get("PO_STATUS").toString());
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
				indentObj.setSiteWiseIndentNumber((prods.get("SITEWISE_INDENT_NO")==null ? "-" : prods.get("SITEWISE_INDENT_NO").toString()));
				operationType=(prods.get("OPERATION_TYPE")==null ? "" : prods.get("OPERATION_TYPE").toString());
				//indentObj.setOperationType();
				type_Of_Purchase=(prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString());
				old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setPoAmount(prods.get("TOTAL_AMOUNT")==null ? "" : prods.get("TOTAL_AMOUNT").toString());
				indentObj.setCreatedBY(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
				indentObj.setPending_Emp_Name(prods.get("PENDING_EMP_NAME")==null ? "-" : prods.get("PENDING_EMP_NAME").toString());
				
				indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
				if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("MARKETING DEPT");
				}else if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("REVISED PO");
				}
				
				else{
				if((type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("REVISED"))){
					
					indentObj.setType_Of_Purchase("REVISED PO");
				}else if(type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("UPDATE")){
					indentObj.setType_Of_Purchase("UPDATE PO");
				}
				else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("CREATION"))){
					
					indentObj.setType_Of_Purchase("PURCHASE DEPT PO");
				} else{
					
					indentObj.setType_Of_Purchase("SITELEVEL PO");
				}
				}

				String date=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}
			}
			/****************************************************this is for cancel po details taken from perminent table********************************/
			if(dbCancelPoDts!=null && dbCancelPoDts.size()>0){
			for(Map<String, Object> markCancelPo : dbCancelPoDts) {
				indentObj = new IndentCreationBean();
				indentObj.setIntSerialNo(++i);
				indentObj.setPonumber(markCancelPo.get("PO_NUMBER")==null ? "" : markCancelPo.get("PO_NUMBER").toString());
				indentObj.setSiteId(Integer.parseInt(markCancelPo.get("SITE_ID")==null ? "0" : markCancelPo.get("SITE_ID").toString()));
				indentObj.setSiteName(markCancelPo.get("SITE_NAME")==null ? "" : markCancelPo.get("SITE_NAME").toString());
				indentObj.setPoentryId(markCancelPo.get("PO_ENTRY_ID")==null ? "" : markCancelPo.get("PO_ENTRY_ID").toString());
				indentObj.setPreparedBy(markCancelPo.get("PREPARED_BY")==null ? "" : markCancelPo.get("PREPARED_BY").toString());
				String date=markCancelPo.get("ENTRY_DATE")==null ? "" : markCancelPo.get("ENTRY_DATE").toString();
				indentObj.setSiteWiseIndentNumber((markCancelPo.get("SITEWISE_INDENT_NO")==null ? "-" : markCancelPo.get("SITEWISE_INDENT_NO").toString()));
				indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
				indentObj.setVendorName(markCancelPo.get("VENDOR_NAME")==null ? "" : markCancelPo.get("VENDOR_NAME").toString());
				indentObj.setPoAmount(markCancelPo.get("TOTAL_AMOUNT")==null ? "" : markCancelPo.get("TOTAL_AMOUNT").toString());
				indentObj.setCreatedBY(markCancelPo.get("EMP_NAME")==null ? "" : markCancelPo.get("EMP_NAME").toString());
				indentObj.setPending_Emp_Name(markCancelPo.get("PENDING_EMP_NAME")==null ? "-" : markCancelPo.get("PENDING_EMP_NAME").toString());
				indentObj.setType_Of_Purchase("CANCEL PO");
				//indentObj.setSiteWiseIndentNumber("-");
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}
			}
			/**************************************************Marketing cancel po's show in that start*********************************************** */
			if(dbMarketingCancelPoDts!=null && dbMarketingCancelPoDts.size()>0){
			for(Map<String, Object> markPo : dbMarketingCancelPoDts) {
				indentObj = new IndentCreationBean();
				indentObj.setIntSerialNo(++i);
				indentObj.setPonumber(markPo.get("PO_NUMBER")==null ? "" : markPo.get("PO_NUMBER").toString());
				//indentObj.setStatus(markPo.get("PO_STATUS")==null ? "" : markPo.get("PO_STATUS").toString());
				indentObj.setSiteName(markPo.get("SITE_NAME")==null ? "" : markPo.get("SITE_NAME").toString());
				indentObj.setPoentryId(markPo.get("PO_ENTRY_ID")==null ? "" : markPo.get("PO_ENTRY_ID").toString());
				//indentObj.setIndentNumber(Integer.parseInt(markPo.get("INDENT_NO")==null ? "0" : markPo.get("INDENT_NO").toString()));
				indentObj.setSiteId(Integer.parseInt(markPo.get("SITE_ID")==null ? "0" : markPo.get("SITE_ID").toString()));
				indentObj.setPreparedBy(markPo.get("PREPARED_BY")==null ? "" : markPo.get("PREPARED_BY").toString());
				//old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
				String date=markPo.get("ENTRY_DATE")==null ? "" : markPo.get("ENTRY_DATE").toString();
				indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
				indentObj.setVendorName(markPo.get("VENDOR_NAME")==null ? "" : markPo.get("VENDOR_NAME").toString());
				indentObj.setPoAmount(markPo.get("TOTAL_AMOUNT")==null ? "" : markPo.get("TOTAL_AMOUNT").toString());
				indentObj.setCreatedBY(markPo.get("EMP_NAME")==null ? "" : markPo.get("EMP_NAME").toString());
				indentObj.setPending_Emp_Name(markPo.get("PENDING_EMP_NAME")==null ? "-" : markPo.get("PENDING_EMP_NAME").toString());
				indentObj.setType_Of_Purchase("CANCEL PO");
				indentObj.setSiteWiseIndentNumber("-");
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}
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

	@Override
	public int saveTempTermsconditions(String termsAndCondition,String strPONumber,String strVendorId,String strIndentNo)

	{



		String query1 = "INSERT INTO SUMADHURA_TEMP_PO_TERMS_COND(TERMS_CONDITION_ID,VENDOR_ID,INDENT_NO,PO_NUMBER,TERMS_CONDITION_DESC) values(SUMADHURA_PD_TERMS_COND_SEQ.NEXTVAL,?,?,?,?)";
		int result1 = jdbcTemplate.update(query1, new Object[] {
				strVendorId,
				strIndentNo,
				strPONumber,
				termsAndCondition
		});



		return result1;
	}

	@Override
	public int saveTermsconditions(String termsAndCondition,int strPONumber,String strVendorId,String strIndentNo)

	{

		//	int intPoNumber=Integer.parseInt(strPONumber);

		String query1 = "INSERT INTO SUMADHURA_PD_TERMS_CONDITIONS(TERMS_CONDITION_ID,VENDOR_ID,INDENT_NO,PO_ENTRY_ID,TERMS_CONDITION_DESC,ENTRY_DATE) values(SUMADHURA_PD_TERMS_COND_SEQ.NEXTVAL,?,?,?,?,sysdate)";
		int result1 = jdbcTemplate.update(query1, new Object[] {
				strVendorId,
				strIndentNo,
				strPONumber,
				termsAndCondition
		});



		return result1;
	}

	public String getPendingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName) throws ParseException {
		//	List<Map<String, Object>> productList = null;
		//	JdbcTemplate template;
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> deleiverAddress = null;
		Map<String, String> Names = null;
		String tblOneData="";
		String indentnumber="";
		String ponumber="";
		String vendorid="";
		String vendorname="";
		String address="";
		String mobilenumber="";
		String gstinnumber="";
		String state="";
		String podate="";
		int strPOYear=0;
		int strPOMonth=0;
		String subject="";
		String contactPersonName="";
		String ccEmailId="";
		String billingAddress="";
		String preparedName="";
		String preparedDate="";
		String verifyName="";
		String verifyDate="";
		String strSiteId = "";
		String IndentCreatedDate = "";//request.getParameter("indentNumber") == null ? "" : request.getParameter("indentNumber").toString();
		String deliverySiteName="";
		String deliverySiteAddress="";
		String deliverySiteMobileNo="";
		String deliverySiteGSTIN="";
		String deliverySiteState="";
		String landLineNumber="";
		String emailId="";
		String siteWiseIndentNo = request.getParameter("siteWiseIndentNo");
		String billingAddressCompanyName="";
		String strDeliveryAddressContactPerson="";
		String deliveryAdddressLandLineNumber="";
		String strBillingAddressGSTIN="";
		String version_Number="";
		String poRefferenceDate="";
		String refference_No="";
		String revision_No="";
		String old_Po_Number="";
		String preparedBy="";
		String strDeliveryDate ="";
		String old_PO_EntryId="";
		String old_PO_Date="";
		String deliveryDateForUpdatePO="";
		String receiverContactPersonTwo="";
		String receiverMobileNumberTwo="";
		String contactPersonTwo="";
		String mobileNumberTwo="";
		
		if (StringUtils.isNotBlank(poNumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.BILLING_ADDRESS,STPE.SUBJECT,STPE.PO_DATE,STPE.VENDOR_ID,VD.VENDOR_CON_PER_NAME,VD.VENDOR_NAME,VD.ADDRESS, VD.MOBILE_NUMBER,VD.GSIN_NUMBER,VD.STATE,STPE.CC_EMAIL_ID,STPE.SITE_ID,VD.LANDLINE_NO,VD.EMP_EMAIL,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.REVISION,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.DELIVERY_DATE,VD.VENDOR_CON_PER_NAME_TWO,VD.MOBILE_NUMBER_TWO FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";


			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poNumber});

		}
		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {

				indentnumber=IndentGetBean.get("INDENT_NO") == null ? "" : IndentGetBean.get("INDENT_NO").toString();
				ponumber=IndentGetBean.get("PO_ENTRY_ID") == null ? "" : IndentGetBean.get("PO_ENTRY_ID").toString();
				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();
				vendorname=IndentGetBean.get("VENDOR_NAME") == null ? "" : IndentGetBean.get("VENDOR_NAME").toString();
				address=IndentGetBean.get("ADDRESS") == null ? "" : IndentGetBean.get("ADDRESS").toString();
				mobilenumber=IndentGetBean.get("MOBILE_NUMBER") == null ? "" : IndentGetBean.get("MOBILE_NUMBER").toString();
				gstinnumber=IndentGetBean.get("GSIN_NUMBER") == null ? "" : IndentGetBean.get("GSIN_NUMBER").toString();
				state=IndentGetBean.get("STATE") == null ? "-" : IndentGetBean.get("STATE").toString();
				podate=IndentGetBean.get("PO_DATE") == null ? "-" : IndentGetBean.get("PO_DATE").toString();
				subject=IndentGetBean.get("SUBJECT") == null ? "-" : IndentGetBean.get("SUBJECT").toString();
				contactPersonName=IndentGetBean.get("VENDOR_CON_PER_NAME") == null ? "-" : IndentGetBean.get("VENDOR_CON_PER_NAME").toString();
				ccEmailId=IndentGetBean.get("CC_EMAIL_ID") == null ? "-" : IndentGetBean.get("CC_EMAIL_ID").toString();		
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "-" : IndentGetBean.get("BILLING_ADDRESS").toString();
				strSiteId =IndentGetBean.get("SITE_ID") == null ? "-" : IndentGetBean.get("SITE_ID").toString();
				landLineNumber=IndentGetBean.get("LANDLINE_NO") == null ? "-" : IndentGetBean.get("LANDLINE_NO").toString();
				emailId=IndentGetBean.get("EMP_EMAIL") == null ? "-" : IndentGetBean.get("EMP_EMAIL").toString();
				
				version_Number=IndentGetBean.get("VERSION_NUMBER") == null ? "-" : IndentGetBean.get("VERSION_NUMBER").toString();
				poRefferenceDate=IndentGetBean.get("PO_ISSUE_DATE") == null ? "-" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "-" : IndentGetBean.get("REFFERENCE_NO").toString();
				
				revision_No=IndentGetBean.get("REVISION") == null ? " " : IndentGetBean.get("REVISION").toString();
				old_Po_Number=IndentGetBean.get("OLD_PO_NUMBER") == null ? "-" : IndentGetBean.get("OLD_PO_NUMBER").toString();
				preparedBy=IndentGetBean.get("PREPARED_BY") == null ? "-" : IndentGetBean.get("PREPARED_BY").toString();
				strDeliveryDate=IndentGetBean.get("DELIVERY_DATE") == null ? "-" : IndentGetBean.get("DELIVERY_DATE").toString();
				contactPersonTwo=IndentGetBean.get("VENDOR_CON_PER_NAME_TWO") == null ? "" : IndentGetBean.get("VENDOR_CON_PER_NAME_TWO").toString();
				mobileNumberTwo=IndentGetBean.get("MOBILE_NUMBER_TWO") == null ? "" : IndentGetBean.get("MOBILE_NUMBER_TWO").toString();
				
				if(!poRefferenceDate.equals("-")){
				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Date poReffDate = inputFormat.parse(poRefferenceDate);
			    poRefferenceDate=outputFormat.format(poReffDate);
			    if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){
			    	Date deliveryDate = inputFormat.parse(strDeliveryDate);
			    	strDeliveryDate=outputFormat.format(deliveryDate);
			    	deliveryDateForUpdatePO = new SimpleDateFormat("dd-MMM-yy").format(deliveryDate);
			    	
			    }
				}
				
				
				request.setAttribute("versionNo", version_Number);
				request.setAttribute("refferenceNo", refference_No);
				request.setAttribute("strPoPrintRefdate",poRefferenceDate);
				
				
				billingAddressCompanyName=validateParams.getProperty("BILLING_NAME");
				
				if(billingAddress.contains("Sumadhura Infracon pvt Ltd")){
					
					billingAddress=billingAddress.replace("Sumadhura Infracon pvt Ltd,","");
		
					
				}
				if(!contactPersonTwo.equals("") && !contactPersonName.equals("") && !contactPersonTwo.equals("-") && !contactPersonName.equals("-")){
					contactPersonName = contactPersonName+","+contactPersonTwo;
				}
				if(!mobilenumber.equals("")){
					contactPersonName = contactPersonName+" ( "+mobilenumber;
				}if(!mobileNumberTwo.equals("")){
					contactPersonName = contactPersonName+","+mobileNumberTwo;
				}

				contactPersonName = " "+contactPersonName +" )";
				state=state+". Email Id : "+emailId;


				String indentCreationdtsId=getIndentCreationDetailsId(indentnumber);

				int tempiPOnumber = Integer.parseInt(ponumber);
				Names=getPOApproveCreateEmp(tempiPOnumber,request);

				getPOVerifiedEmpNames(tempiPOnumber,request);
				Map<String, String> verifyNames = (Map<String, String>)request.getAttribute("listOfVerifiedNames");

				for(Map.Entry<String, String> tax : Names.entrySet()) {
					preparedName=tax.getKey().toUpperCase();
					preparedDate=tax.getValue(); 
				}	

				for(Map.Entry<String, String> verify : verifyNames.entrySet()) {
					verifyName=verify.getKey().toUpperCase();
					verifyDate=verify.getValue(); 
				}	


				try{

					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(podate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
					SimpleDateFormat dt2 = new SimpleDateFormat("yy");
					podate=dt1.format(date);

					dt1.applyPattern("yyyy");

					strPOYear =Integer.parseInt((dt1.format(date)));
					dt1.applyPattern("MM");
					strPOMonth = Integer.parseInt((dt1.format(date)));
					int strPoYearYY=Integer.parseInt(dt2.format(date));
					String strFinacialYear = "";
					if(strPOMonth <=3){
						strFinacialYear = (strPOYear-1)+"-"+strPoYearYY;
					}else{
						strFinacialYear = strPOYear+"-"+(strPoYearYY+1);
					}

					request.setAttribute("strFinacialYear",strFinacialYear);





				}catch(Exception e){
					e.printStackTrace();
				}
				if (StringUtils.isNotBlank(poNumber) ) {
					String query = "SELECT CREATE_DATE FROM SUMADHURA_INDENT_CREATION WHERE INDENT_CREATION_ID=?";

					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{indentnumber});




					if (null != dbIndentDts && dbIndentDts.size() > 0) {
						for (Map<?, ?> siteVendoraddress : dbIndentDts) {

							//	indentnumber = siteVendoraddress.get("INDENT_NO") == null ? "" : siteVendoraddress.get("INDENT_NO").toString();
							//	ponumber = siteVendoraddress.get("PO_ENTRY_ID") == null ? "" : siteVendoraddress.get("PO_ENTRY_ID").toString();
							IndentCreatedDate = siteVendoraddress.get("CREATE_DATE") == null ? "" : siteVendoraddress.get("CREATE_DATE").toString();



							SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
							Date date = dt.parse(IndentCreatedDate); 


							SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
							IndentCreatedDate = dt1.format(date);
						}

					}

				}

				/******************************************update po time need po date along with poentry id so written this one**********************/
				if(old_Po_Number!=null && !old_Po_Number.equals("") && !old_Po_Number.equals("-")){
				Date date = null;
				
				String query = "select PO_ENTRY_ID,PO_DATE from SUMADHURA_PO_ENTRY where PO_NUMBER=? ";
				deleiverAddress = jdbcTemplate.queryForList(query, new Object[]{old_Po_Number});
				if (null != deleiverAddress && deleiverAddress.size() > 0) {
					for (Map<?, ?> deleiverAddressMap : deleiverAddress) {
						old_PO_EntryId=deleiverAddressMap.get("PO_ENTRY_ID") == null ? "" : deleiverAddressMap.get("PO_ENTRY_ID").toString();
						old_PO_Date=deleiverAddressMap.get("PO_DATE") == null ? "" : deleiverAddressMap.get("PO_DATE").toString();
						
					}
				}
				DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
				date = inputFormat.parse(old_PO_Date);
				old_PO_Date = new SimpleDateFormat("dd-MMM-yy").format(date);
				
				}
				/*********************************************update po time it was used to end*********************************************************/
				if (StringUtils.isNotBlank(poNumber) ) {
					String query = "select VENDOR_NAME,STATE,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,VENDOR_CON_PER_NAME,LANDLINE_NO,VENDOR_CON_PER_NAME_TWO,MOBILE_NUMBER_TWO from VENDOR_DETAILS where VENDOR_ID = ?";


					deleiverAddress = jdbcTemplate.queryForList(query, new Object[]{strSiteId});

				}
				if (null != dbIndentDts && dbIndentDts.size() > 0) {
					for (Map<?, ?> deleiverAddressMap : deleiverAddress) {



						deliverySiteName = deleiverAddressMap.get("VENDOR_NAME") == null ? "" : deleiverAddressMap.get("VENDOR_NAME").toString();
						deliverySiteAddress = deleiverAddressMap.get("ADDRESS") == null ? "" : deleiverAddressMap.get("ADDRESS").toString();
						deliverySiteMobileNo = deleiverAddressMap.get("MOBILE_NUMBER") == null ? "" : deleiverAddressMap.get("MOBILE_NUMBER").toString();
						deliverySiteGSTIN = deleiverAddressMap.get("GSIN_NUMBER") == null ? "" : deleiverAddressMap.get("GSIN_NUMBER").toString();
						deliverySiteState=	 deleiverAddressMap.get("STATE") == null ? "" : deleiverAddressMap.get("STATE").toString();
						strDeliveryAddressContactPerson=deleiverAddressMap.get("VENDOR_CON_PER_NAME") == null ? "-" : deleiverAddressMap.get("VENDOR_CON_PER_NAME").toString();
						deliveryAdddressLandLineNumber=deleiverAddressMap.get("LANDLINE_NO") == null ? "-" : deleiverAddressMap.get("LANDLINE_NO").toString();
						receiverContactPersonTwo=deleiverAddressMap.get("VENDOR_CON_PER_NAME_TWO")==null ? "" :   deleiverAddressMap.get("VENDOR_CON_PER_NAME_TWO").toString();
						receiverMobileNumberTwo=deleiverAddressMap.get("MOBILE_NUMBER_TWO")==null ? "" :   deleiverAddressMap.get("MOBILE_NUMBER_TWO").toString();
						request.setAttribute("deliverySiteState",deliverySiteState);

					}
					if(!receiverContactPersonTwo.equals("")){
						strDeliveryAddressContactPerson=strDeliveryAddressContactPerson+","+receiverContactPersonTwo;
					}


				}

				if(deliverySiteState.equalsIgnoreCase("Telangana")){
					strBillingAddressGSTIN=validateParams.getProperty("GSTIN_HYDERABAD");
					
				}else{
					strBillingAddressGSTIN=validateParams.getProperty("GSTIN_BENGALORE");
				}


				//strIndentNumber = request.getParameter("indentNumber") == null ? "" : request.getParameter("indentNumber").toString();

				tblOneData+= ponumber+"@@"+podate+"@@"+indentnumber+"@@"+vendorname+"@@"+address+"@@"+state+"@@"+gstinnumber+
				"@@"+deliverySiteName+"@@"+deliverySiteAddress+"@@"+deliverySiteMobileNo+"@@"+deliverySiteGSTIN+"@@"+""+"@@"+subject+"@@"+contactPersonName+
				"@@"+ccEmailId+"@@"+billingAddress+"@@"+preparedName+"@@"+preparedDate+"@@"+indentCreationdtsId+"@@"+siteId+"@@"+verifyName+"@@"+verifyDate+"@@"+IndentCreatedDate+"@@"+siteWiseIndentNo+"@@"+billingAddressCompanyName+
				"@@"+strDeliveryAddressContactPerson+"@@"+receiverMobileNumberTwo+"@@"+strBillingAddressGSTIN+"@@"+siteId+"@@"+siteName+"@@"+vendorid+"@@"+revision_No+"@@"+old_Po_Number+"@@"+strDeliveryDate+"@@"+preparedBy;
				
			}

			request.setAttribute("vendorName",vendorname);
			request.setAttribute("strDeliveryDate",deliveryDateForUpdatePO);
			//request.setAttribute("preparedBy",preparedBy);
			request.setAttribute("old_PO_EntryId",old_PO_EntryId);
			request.setAttribute("old_PO_Date",old_PO_Date);
			
			request.setAttribute("gstinumber",gstinnumber);
		}

		return tblOneData;

	}

	public String getPendingProductDetails(String poNumber, String siteId,HttpServletRequest request,String deliverySiteState) {
		List<Map<String, Object>> GetproductDetailsList = null;

		//List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		//	IndentCreationBean objGetInvoiceDetailsInward=null;

		//	JdbcTemplate template = null;
		String sql = "";	
		String tblTwoData="";
		String subproduct="";
		String childproduct="";
		String measurement="";
		String quantity="";
		String tax="";
		String taxamount="";
		String amountaftertax="";
		String basicamount="";
		String price="";
		String hsncode="";
		String totalamount="";
		String gstinumber="";
		String CGST = "";
		String SGST = "";
		String IGST = "";
		Double percent = 0.0;
		Double CGSTAMT = 0.0;
		Double SGSTAMT = 0.0;
		Double IGSTAMT = 0.0;
		Double amt = 0.0;
		int sno=0;
		String discount="";
		String discountaftertax="";
		double totalAmt=0.0;
		String vendorProductDesc="";
		String strIndentCreationDetailsId ="";
		String pd_Product_Desc="";
		String product_name="";
		String productId="";
		String sub_productId="";
		String child_productId="";
		String measurementId="";
		String indentCreationDetailsId="";
		String userId="";
		String strCGSTAMT ="0";
		String strSGSTAMT ="0";
		String strIGSTAMT ="0";
		
		try{
			if (StringUtils.isNotBlank(poNumber) ) {

				sql+="select (P.NAME)AS PRODUCT_NAME,SP.NAME AS SUB_PRODUCT,CP.NAME AS CHILD_PRODUCT,M.NAME,SPED.PRODUCT_ID,SPED.SUB_PRODUCT_ID,SPED.CHILD_PRODUCT_ID,SPED.MEASUR_MNT_ID, "
					+" SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX,SPED.BASIC_AMOUNT,SPED.PRICE, SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.VENDOR_PRODUCT_DESC ,IGST.TAX_PERCENTAGE , "
					+" SPED.INDENT_CREATION_DTLS_ID,SPED.PD_PRODUCT_DESC,SPE.TEMP_PO_PENDING_EMP_ID  from SUMADHURA_TEMP_PO_ENTRY SPE,SUMADHURA_TEMP_PO_ENTRY_DTLS SPED, SUB_PRODUCT SP,"
					+ " CHILD_PRODUCT CP,MEASUREMENT M ,INDENT_GST IGST,PRODUCT P where IGST.TAX_ID = SPED.TAX and  SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID "
					+ " AND P.PRODUCT_ID=SPED.PRODUCT_ID and  SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID "
					+" and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? order by SPED.INDENT_CREATION_DTLS_ID";

				GetproductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {poNumber, siteId});
				//System.out.println("second product data in service"+GetproductDetailsList);
			} 
			if (null != GetproductDetailsList && GetproductDetailsList.size() > 0) {
				for (Map<?, ?> GetDetailsInwardBean : GetproductDetailsList) {
					//	objGetInvoiceDetailsInward = new IndentCreationBean();
					sno++;
					subproduct=GetDetailsInwardBean.get("SUB_PRODUCT") == null ? "": GetDetailsInwardBean.get("SUB_PRODUCT").toString();
					childproduct=GetDetailsInwardBean.get("CHILD_PRODUCT") == null ? "": GetDetailsInwardBean.get("CHILD_PRODUCT").toString();
					measurement=GetDetailsInwardBean.get("NAME") == null ? "": GetDetailsInwardBean.get("NAME").toString();
					quantity=GetDetailsInwardBean.get("PO_QTY") == null ? "": GetDetailsInwardBean.get("PO_QTY").toString();
					tax=GetDetailsInwardBean.get("TAX_PERCENTAGE") == null ? "": GetDetailsInwardBean.get("TAX_PERCENTAGE").toString();
					taxamount=GetDetailsInwardBean.get("TAX_AMOUNT") == null ? "": GetDetailsInwardBean.get("TAX_AMOUNT").toString();
					amountaftertax=GetDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "": GetDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString();
					basicamount=GetDetailsInwardBean.get("BASIC_AMOUNT") == null ? "": GetDetailsInwardBean.get("BASIC_AMOUNT").toString();
					price=GetDetailsInwardBean.get("PRICE") == null ? "-": GetDetailsInwardBean.get("PRICE").toString();
					hsncode=GetDetailsInwardBean.get("HSN_CODE") == null ? "": GetDetailsInwardBean.get("HSN_CODE").toString();
					totalamount=GetDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "": GetDetailsInwardBean.get("TOTAL_AMOUNT").toString();
					//gstinumber=GetDetailsInwardBean.get("GSIN_NUMBER") == null ? "": GetDetailsInwardBean.get("GSIN_NUMBER").toString();
					discount=GetDetailsInwardBean.get("DISCOUNT") == null ? "": GetDetailsInwardBean.get("DISCOUNT").toString();
					discountaftertax=GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT") == null ? "": GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT").toString();
					vendorProductDesc=GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC") == null ? "-": GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC").toString();
					strIndentCreationDetailsId =(GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "0": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString());
					pd_Product_Desc=GetDetailsInwardBean.get("PD_PRODUCT_DESC") == null ? "": GetDetailsInwardBean.get("PD_PRODUCT_DESC").toString();
					userId=GetDetailsInwardBean.get("TEMP_PO_PENDING_EMP_ID") == null ? "": GetDetailsInwardBean.get("TEMP_PO_PENDING_EMP_ID").toString();
					
					product_name=GetDetailsInwardBean.get("PRODUCT_NAME") == null ? "": GetDetailsInwardBean.get("PRODUCT_NAME").toString();
					productId=GetDetailsInwardBean.get("PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("PRODUCT_ID").toString();
					sub_productId=GetDetailsInwardBean.get("SUB_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("SUB_PRODUCT_ID").toString();
					child_productId=GetDetailsInwardBean.get("CHILD_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("CHILD_PRODUCT_ID").toString();
					measurementId=GetDetailsInwardBean.get("MEASUR_MNT_ID") == null ? "": GetDetailsInwardBean.get("MEASUR_MNT_ID").toString();
					indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();
					
					if(pd_Product_Desc!=null && !pd_Product_Desc.equals("")){
						
						childproduct=pd_Product_Desc;
					}
					
					
					gstinumber=request.getAttribute("gstinumber").toString();
					char firstLetterChar = gstinumber.charAt(0);
					char secondLetterChar=gstinumber.charAt(1);

					if(tax.contains("%")){
						String data[] = tax.split("%");
						tax=data[0];

					}
					double totalvalue=Double.valueOf(totalamount);

					totalAmt=totalAmt+totalvalue;
					//	double totalAmt=Double.valueOf(totalamount);
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=Math.ceil(totalAmt);
					String strroundoff=String.format("%.2f",roundoff);
					String strTotalVal=String.format("%.2f",totalAmt);
					String strGrandVal=String.format("%.2f",grandtotal);
					quantity=String.format("%.2f",Double.valueOf(quantity));
					price=String.format("%.2f",Double.valueOf(price));
					basicamount=String.format("%.2f",Double.valueOf(basicamount));
					amountaftertax=String.format("%.2f",Double.valueOf(amountaftertax));
					discountaftertax=String.format("%.2f",Double.valueOf(discountaftertax));

					if(deliverySiteState.equalsIgnoreCase("Telangana")){

						if (firstLetterChar=='3' && secondLetterChar=='6') {
							request.setAttribute("isCGSTSGST","true");


							if (tax.equals("0")) {
								CGST = "0";
								SGST = "0";
								strCGSTAMT="0.00";
								strSGSTAMT="0.00";
							} else {
								percent = Double.parseDouble(tax)/2;
								amt = Double.parseDouble(taxamount)/2;
								CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								CGST = String.valueOf(percent);
								SGST = String.valueOf(percent);
								strCGSTAMT =String.format("%.2f",CGSTAMT);
								 strSGSTAMT =String.format("%.2f",SGSTAMT);
							}
							//to set data in table two
							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGrandVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"+strIndentCreationDetailsId+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+userId+"&&";
							
						} else {
							
							request.setAttribute("isCGSTSGST","false");
							percent = Double.parseDouble(tax);
							amt = Double.parseDouble(taxamount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							strIGSTAMT=String.format("%.2f",IGSTAMT);
							//to set data for table two
							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGrandVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"+strIndentCreationDetailsId+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+userId+"&&";
							
						}


					}else{

						if (firstLetterChar=='2' && secondLetterChar=='9') {

							request.setAttribute("isCGSTSGST","true");

							if (tax.equals("0")) {
								CGST = "0";
								SGST = "0";
								strCGSTAMT="0";
								strSGSTAMT="0";
							} else {
								percent = Double.parseDouble(tax)/2;
								amt = Double.parseDouble(taxamount)/2;
								CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								CGST = String.valueOf(percent);
								SGST = String.valueOf(percent);
								strCGSTAMT =String.format("%.2f",CGSTAMT);
								 strSGSTAMT =String.format("%.2f",SGSTAMT);
							}
							//to set data in table two
							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGrandVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"+strIndentCreationDetailsId+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+userId+"&&";
							
						} else {
							
							request.setAttribute("isCGSTSGST","false");
							percent = Double.parseDouble(tax);
							amt = Double.parseDouble(taxamount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							strIGSTAMT=String.format("%.2f",IGSTAMT);
							//set data in table two
							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGrandVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"+strIndentCreationDetailsId+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+userId+"&&";

							
							
						}
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tblTwoData;

	}

	@Override
	public String getPendingTransportChargesList(String poNumber,String strSiteId,String gstinumber,HttpServletRequest request,String deliverySiteState) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> termList = null;
		List<String> listOfTermsAndConditions = new ArrayList<String>();

		String sql="";
		String ConveyanceName="";
		String ConveyanceAmount="";
		String GSTTax="";
		String GSTAmount="";
		String AmountAfterTax="";
		String CGST = "";
		String SGST = "";
		String IGST = "";
		Double percent = 0.0;
		Double CGSTAMT = 0.0;
		Double SGSTAMT = 0.0;
		Double IGSTAMT = 0.0;
		Double amt = 0.0;
		String strTableThreeData="";
		String response="";
		String termscondition="";
		
		String strCGST="0.0";
		String strSGST="0.0";
		String strIGST="0.0";
		

		try {

			if (StringUtils.isNotBlank(poNumber) ) {
				sql += "SELECT STOCM.CHARGE_NAME,STOCD.TRANSPORT_AMOUNT,STOCD.TRANSPORT_GST_PERCENTAGE,STOCD.TRANSPORT_GST_AMOUNT,STOCD.TOTAL_AMOUNT_AFTER_GST_TAX,IGST.TAX_PERCENTAGE FROM SUMADHURA_TEMP_PO_TRNS_O_CHRGS STOCD,SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM,INDENT_GST IGST WHERE  IGST.TAX_ID=STOCD.TRANSPORT_GST_PERCENTAGE AND STOCD.TRANSPORT_ID=STOCM.CHARGE_ID and STOCD.PO_NUMBER=?";	

				productList = jdbcTemplate.queryForList(sql, new Object[] {poNumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetTransportChargesDetails : productList) {
						ConveyanceAmount = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "0" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();	
						GSTTax = GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "0" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString();	
						GSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "0" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
						AmountAfterTax = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "0" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
						ConveyanceName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "0" : GetTransportChargesDetails.get("CHARGE_NAME").toString();

						char firstLetterChar = gstinumber.charAt(0);
						char secondLetterChar=gstinumber.charAt(1);
						if(GSTTax.contains("%")){
							String data[] = GSTTax.split("%");
							GSTTax=data[0];

						}
						ConveyanceAmount=String.format("%.2f",Double.valueOf(ConveyanceAmount));
						AmountAfterTax=String.format("%.2f",Double.valueOf(AmountAfterTax));

						if(deliverySiteState.equalsIgnoreCase("Telangana")){


							if (firstLetterChar=='3' && secondLetterChar=='6') {

								if (GSTTax.equals("0")) {
									CGST = "0";
									SGST = "0";
								} else {
									percent = Double.parseDouble(GSTTax)/2;
									amt = Double.parseDouble(GSTAmount)/2;
									CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
									SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
									 strCGST=String.format("%.2f",Double.valueOf(CGSTAMT));
									 strSGST=String.format("%.2f",Double.valueOf(SGSTAMT));
									
									CGST = String.valueOf(percent);
									SGST = String.valueOf(percent);
								}
								//to set data in table
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+strCGST+"@@"+SGST+"%"+"@@"+strSGST+"@@"+" "+"@@"+" "+"&&";
								response="success";
								
							} else {
								percent = Double.parseDouble(GSTTax);
								amt = Double.parseDouble(GSTAmount);
								IGST = String.valueOf(percent);
								IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								strIGST=String.format("%.2f",Double.valueOf(IGSTAMT));
								//to set data in table three
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+strIGST+"&&";				
								response="success";
								
							}

						}else{

							if (firstLetterChar=='2' && secondLetterChar=='9') {

								if (GSTTax.equals("0")) {
									CGST = "0";
									SGST = "0";
								} else {
									percent = Double.parseDouble(GSTTax)/2;
									amt = Double.parseDouble(GSTAmount)/2;
									CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
									SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
									CGST = String.valueOf(percent);
									SGST = String.valueOf(percent);
								}
								//to set data in table three
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+CGSTAMT+"@@"+SGST+"%"+"@@"+SGSTAMT+"@@"+" "+"@@"+" "+"&&";
								response="success";
								
							} else {
								percent = Double.parseDouble(GSTTax);
								amt = Double.parseDouble(GSTAmount);
								IGST = String.valueOf(percent);
								IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
								//to set data in table three
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+IGSTAMT+"&&";				
								response="success";
							}
						}

						
						

						/*if(deliverySiteState.equalsIgnoreCase("Telangana")){

							if (firstLetterChar=='3' && secondLetterChar=='6') {

								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+CGSTAMT+"@@"+SGST+"%"+"@@"+SGSTAMT+"@@"+" "+"@@"+" "+"&&";

								response="success";
							}else{
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+IGSTAMT+"&&";				
								response="success";
							}
						}else{


							if (firstLetterChar=='2' && secondLetterChar=='9') {

								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+CGST+"%"+"@@"+CGSTAMT+"@@"+SGST+"%"+"@@"+SGSTAMT+"@@"+" "+"@@"+" "+"&&";

								response="success";
							}else{
								strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+" "+"@@"+IGST+"%"+"@@"+IGSTAMT+"&&";				
								response="success";
							}
						}*/
						
						
					}
				}

				if(response=="success"){

					String sql1="SELECT TERMS_CONDITION_DESC,TERMS_CONDITION_ID FROM  SUMADHURA_TEMP_PO_TERMS_COND WHERE PO_NUMBER=? order by TERMS_CONDITION_ID";
					termList = jdbcTemplate.queryForList(sql1, new Object[] {poNumber});
					//System.out.println("the list size is "+termList.size());
					if (null != termList && termList.size() > 0) {
						for (Map<?, ?> GetTransportChargesDetails : termList) {

							termscondition = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();


							listOfTermsAndConditions.add(String.valueOf(termscondition));

						}
					}
				}
				request.setAttribute("listOfTermsAndConditions", listOfTermsAndConditions);

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return strTableThreeData;
	}	

	// approval time when user click on approval get and save data
	public String getAndsaveVendorDetails(String tempPONumber, String siteId,String user_id,HttpServletRequest request,String revision_No,String oldPoNumber,String siteLevelPoPreparedBy,String siteName,String deliveryDate) {

		List<Map<String, Object>> dbIndentDts = null;
		
		List<String> listRevisedDetails = new ArrayList<String>();
		String indentnumber="";
		String ponumber ="";
		String vendorid="";
		String response="";
		int result=0;
		String subject="";
		String billingAddress="";
		String State="";
		String poState="";
		
		String strIndentFromSiteId = "";
		String version_Number="";
		String porefferenceDate="";
		String refference_No="";
		int temprevision_no=0;
		int revision_no=0;
		
		String preparedBy="";
		String strPOdate="";
		String strFinacialYear="";
		String oldPOEntryId="";//this is for revised po purpose to get received quantity receive from inwards from po
		String strRevisedTypePurchase="";//this is for revised po purpose to get received quantity receive from inwards from po
		String poEntryId="";
		String payment_Req_Days="";
		String operation_Type="";
		String groupId="";
		if (StringUtils.isNotBlank(tempPONumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.VENDOR_ID,STPE.SUBJECT,STPE.BILLING_ADDRESS,VD.STATE,STPE.SITE_ID,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.PAYMENT_REQ_DAYS,STPE.OPERATION_TYPE FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{tempPONumber});
		}
		String query = "select STATE from VENDOR_DETAILS where VENDOR_ID='"+siteId+"'";
		State = jdbcTemplate.queryForObject(query, String.class);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		Calendar cal = Calendar.getInstance();
		String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());

		request.setAttribute("state",State); // used in revised time account dept to get mails hyd or bng

		if(currentMonth <=3){ // before march or after march set the year to check condition

			strFinacialYear = (currentYear-1)+"-"+Integer.parseInt(currentYearYY);
		}else{
			strFinacialYear = currentYear+"-"+(Integer.parseInt(currentYearYY)+1);
		}
		//	strFinacialYear=request.getAttribute("strFinacialYear").toString();
		// to check whether it is site level po then if condition executed
		Date date =new java.sql.Date(System.currentTimeMillis());
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yy");
		String strPoDate=dt1.format(date);
		
		if(!siteLevelPoPreparedBy.equals("-") && !siteLevelPoPreparedBy.equalsIgnoreCase("PURCHASE_DEPT") && !siteLevelPoPreparedBy.equals("")){
			request.setAttribute("RevisedOrNot","false");
			request.setAttribute("strPOdate",strPOdate);
			ponumber=getPermenentPoNumber("PO/SIPL/",siteName,siteId,strFinacialYear);
			preparedBy=siteName;

		}else{ // revised or normal po executed to get po number

			preparedBy="PURCHASE_DEPT";
			if(oldPoNumber!=null && !oldPoNumber.equals("-") && !oldPoNumber.equals("")){

				listRevisedDetails=getRevisionNumber(oldPoNumber);
				revision_no=Integer.parseInt(listRevisedDetails.get(0));
				oldPOEntryId=listRevisedDetails.get(1);
				strRevisedTypePurchase=listRevisedDetails.get(2);

				strPOdate=inactiveOldPo(oldPoNumber,"false"); // revised then it will inactive old po
				request.setAttribute("RevisedOrNot","true");  // in mail check condition whether it is normal or revised i.e use common methode inh emailfunction.java
				request.setAttribute("strPOdate",strPOdate); // send date and show through mail so here set date
				String tempPoNumber=oldPoNumber;
				
				if(tempPoNumber.contains("/R")){
					String data=tempPoNumber.split("/R")[1];
					if(data.contains("/U")){
						String data1=data.split("/U")[0];
						temprevision_no=Integer.valueOf(data1)+1;
						ponumber=tempPoNumber.replace("R"+Integer.valueOf(data1), "R"+temprevision_no);
						//System.out.print("the split data1  "+data1);
					}else{
						temprevision_no=Integer.valueOf(data)+1;
						ponumber=tempPoNumber.replace("R"+Integer.valueOf(data), "R"+temprevision_no);
						
					}
					
					}else{
						ponumber=tempPoNumber+"/R"+"1";
						System.out.print("else the split data ");
					}
				
				strPoDate=String.valueOf(request.getAttribute("oldPODate"));	
				/*if(tempPoNumber.contains("R")){ //already revised then this condition
					temprevision_no=revision_no+1;
					ponumber=tempPoNumber.replace("R"+revision_no, "R"+temprevision_no);
				}else{ // first time revised then it will add r1
					temprevision_no=revision_no+1;
					ponumber=tempPoNumber+"/R"+temprevision_no;

				}*/

			}else {

				if(State.equalsIgnoreCase("Telangana")){
					request.setAttribute("RevisedOrNot","false");
					request.setAttribute("strPOdate",strPOdate);
					poState =validateParams.getProperty("PO_NUM_TELANGANA");
					ponumber=getPermenentPoNumber(poState,preparedBy,siteId,strFinacialYear); // getPermanent poNumber 
					
				}else{
					poState =	validateParams.getProperty("PO_NUM_KARNATAKA");
					request.setAttribute("strPOdate",strPOdate);
					request.setAttribute("RevisedOrNot","false");
					ponumber=getPermenentPoNumber(poState,preparedBy,siteId,strFinacialYear);
				}

			}

		}
		request.setAttribute("oldPOEntryId",oldPOEntryId);
		//	ponumber = getPoEnterSeqNoOrMaxId();
		request.setAttribute("strRevisedTypePurchase",strRevisedTypePurchase);
		int poSeqNo = getPoEnterSeqNo();

		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {

				indentnumber=IndentGetBean.get("INDENT_NO") == null ? "" : IndentGetBean.get("INDENT_NO").toString();
				//ponumber=IndentGetBean.get("PO_ENTRY_ID") == null ? "" : IndentGetBean.get("PO_ENTRY_ID").toString();
				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();
				subject=IndentGetBean.get("SUBJECT") == null ? "" : IndentGetBean.get("SUBJECT").toString();
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "" : IndentGetBean.get("BILLING_ADDRESS").toString();
				strIndentFromSiteId=IndentGetBean.get("SITE_ID") == null ? "" : IndentGetBean.get("SITE_ID").toString();
				poEntryId=IndentGetBean.get("PO_ENTRY_ID") == null ? "0" : IndentGetBean.get("PO_ENTRY_ID").toString();
				
				version_Number=IndentGetBean.get("VERSION_NUMBER") == null ? "" : IndentGetBean.get("VERSION_NUMBER").toString();
				porefferenceDate=IndentGetBean.get("PO_ISSUE_DATE") == null ? "" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "" : IndentGetBean.get("REFFERENCE_NO").toString();
				payment_Req_Days=IndentGetBean.get("PAYMENT_REQ_DAYS") == null ? "" : IndentGetBean.get("PAYMENT_REQ_DAYS").toString();
				operation_Type=IndentGetBean.get("OPERATION_TYPE") == null ? "" : IndentGetBean.get("OPERATION_TYPE").toString();
				//preparedBy=IndentGetBean.get("PREPARED_BY") == null ? "" : IndentGetBean.get("PREPARED_BY").toString();
				
				//	State=IndentGetBean.get("STATE") == null ? "" : IndentGetBean.get("STATE").toString();

				String query1 = "INSERT INTO SUMADHURA_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"+
				"PO_ENTRY_USER_ID, SITE_ID,INDENT_NO,TERMS_CONDITIONS_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,DELIVERY_DATE,TEMP_PO_ENTRY_ID,PAYMENT_REQ_DAYS,OPERATION_TYPE,CREATION_DATE) values(? , ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
				result = jdbcTemplate.update(query1, new Object[] {
						poSeqNo,ponumber,StringUtils.isBlank(strPoDate)?null:DateUtil.convertToJavaDateFormat(strPoDate),
						vendorid,"A",user_id,
						siteId,indentnumber,"0" ,subject,billingAddress,version_Number,DateUtil.convertToJavaDateFormat(porefferenceDate),refference_No,temprevision_no,oldPoNumber,preparedBy,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poEntryId,payment_Req_Days,operation_Type});

			}
			request.setAttribute("tempPoNumber",tempPONumber);
			request.setAttribute("poEntrySeqID",poSeqNo); // it is used in terms and condition and tranportation details time user to get data
			request.setAttribute("permentPoNumber",ponumber); // to take po number for pdf or images save or move revisedc time also used
			request.setAttribute("State",State);
			
			response="success";
		}


		return "response";

	}
	@Override
	public String getVendorDetails(String poNumber, String siteId,String user_id,HttpServletRequest request) {

		List<Map<String, Object>> dbIndentDts = null;
		String indentnumber="";
		String ponumber="";
		String vendorid="";
		String response="";
		int result=0;

		if (StringUtils.isNotBlank(poNumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.VENDOR_ID FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";


			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poNumber});

		}
		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {

				indentnumber=IndentGetBean.get("INDENT_NO") == null ? "" : IndentGetBean.get("INDENT_NO").toString();
				ponumber=IndentGetBean.get("PO_ENTRY_ID") == null ? "" : IndentGetBean.get("PO_ENTRY_ID").toString();
				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();

			}


		}


		return vendorid;

	}
	public String getAndsavePendingProductDetails(String tempPONumber, String siteId,HttpServletRequest request,String premPONumber,int intPOEntrySeqId,String old_Po_Number) {
		List<Map<String, Object>> getTemPoProductDetailsList = null;
		//List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		String sql = "";	
		String product="";
		String subproduct="";
		String childproduct="";
		String measurement="";
		String quantity="";
		String tax="";
		String taxamount="";
		String amountaftertax="";
		String basicamount="";
		String price="";
		String hsncode="";
		String totalamount="";
		int sno=0;
		String discount="";
		String discountaftertax="";
		String podetailsid="";
		String response="";
		String strTempPoindentCreationDetailsId="";
		String strOldPoindentCreationDetailsId="";
		String vendorProductDesc="";
		String pd_Product_Desc="";
		String totalAmt="";
		String oldPoEntryId="";
		double doubleTempPoProdQuantity = 0;
		double doublOldPoProdQuantity = 0;
		Map<String,Double> tempPOProductDetials = new HashMap<String,Double>();
		String indentCreationDetailsId="";
		double finalAmount=0.0; // to update po_entry table total amount
		String userId=request.getAttribute("userId").toString();// this is using for the new indent creation table purpose
		String indentNumber=request.getAttribute("indentnumber").toString();// this is using in the compare purpose to quantity in revised po
		String isSiteLevelPo=request.getAttribute("siteLevelPoPreparedBy").toString();
		//String revisedTypeOfPo=request.getAttribute("strRevisedTypePurchase").toString(); //O/P : is PURCHASE_DEPT.   To know purchased department PO Or Site level PO

		try{
			if (StringUtils.isNotBlank(tempPONumber) ) {

				sql+="select DISTINCT SP.SUB_PRODUCT_ID,SPE.TOTAL_AMOUNT,SPED.PO_ENTRY_DETAILS_ID,CP.CHILD_PRODUCT_ID,M.MEASUREMENT_ID,SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX,SPED.BASIC_AMOUNT,SPED.PRICE,SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,P.PRODUCT_ID,SPED.INDENT_CREATION_DTLS_ID,SPED.VENDOR_PRODUCT_DESC,SPED.PD_PRODUCT_DESC from SUMADHURA_TEMP_PO_ENTRY SPE,SUMADHURA_TEMP_PO_ENTRY_DTLS SPED,VENDOR_DETAILS VD,PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M "  
					+"where P.PRODUCT_ID=SPED.PRODUCT_ID AND SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.VENDOR_ID = VD.VENDOR_ID and SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? order by SPED.INDENT_CREATION_DTLS_ID"; 

				getTemPoProductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {tempPONumber, siteId});
				//System.out.println("second product data in service"+GetproductDetailsList);
			} 
			// tocheck Revised or Not site Level Po Not do site level Po if Revised Po do below condition
			if(old_Po_Number!=null && !old_Po_Number.equals("-") && !old_Po_Number.equals("")){

				oldPoEntryId = request.getAttribute("oldPOEntryId").toString();//this is for set in getvandor data for the purpose of compare
				Map<String,Double> oldPoProductDetails = getTotalNumberOfRecords(oldPoEntryId);//this is for revised po purpose to compare quantity and child product

				for (Map<?, ?> getTemPoProductDetailsMap : getTemPoProductDetailsList) {
					
					strTempPoindentCreationDetailsId = getTemPoProductDetailsMap.get("INDENT_CREATION_DTLS_ID") == null ? "": getTemPoProductDetailsMap.get("INDENT_CREATION_DTLS_ID").toString();
					doubleTempPoProdQuantity = Double.parseDouble(getTemPoProductDetailsMap.get("PO_QTY") == null ? "": getTemPoProductDetailsMap.get("PO_QTY").toString());

					
					tempPOProductDetials.put(strTempPoindentCreationDetailsId, doubleTempPoProdQuantity);

				}
				
				// for knowing deleted or modifed in new/temp pO
				for (Map.Entry<String,Double> mapOldPoProductDetails : oldPoProductDetails.entrySet())  {


					strOldPoindentCreationDetailsId = mapOldPoProductDetails.getKey();
					doublOldPoProdQuantity = mapOldPoProductDetails.getValue();

					// comparing old po key with new PO key for knowing is it delete product or not.   
					if(!tempPOProductDetials.containsKey(strOldPoindentCreationDetailsId)){
						updatePOIntiatedQuantityInPDTable(strOldPoindentCreationDetailsId,String.valueOf(doublOldPoProdQuantity));
						checkIOndentCreationtbl(indentNumber);
						// this product is deleted
					}//else{

						//doubleTempPoProdQuantity = tempPOProductDetials.get(strOldPoindentCreationDetailsId);

					//	if(doublOldPoProdQuantity != doubleTempPoProdQuantity){
						//	updateTempPOQuantityDetails(strOldPoindentCreationDetailsId,String.valueOf(doubleTempPoProdQuantity),String.valueOf(doublOldPoProdQuantity)); 
						//	double resultQuantity=Double.valueOf(doublOldPoProdQuantity)-doubleTempPoProdQuantity;	
						//	if(resultQuantity>0){checkIOndentCreationtbl(indentNumber);}

							// quantity modified
					//	}

				//	}

				} 


				/*	if(getTemPoProductDetailsList.size()>oldPoQuantity.size()){
					for (Map<?, ?> GetDetailsInwardBean : GetproductDetailsList) {

						childproduct=GetDetailsInwardBean.get("CHILD_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("CHILD_PRODUCT_ID").toString();
						double actual_Quantity=Double.parseDouble(GetDetailsInwardBean.get("PO_QTY") == null ? "": GetDetailsInwardBean.get("PO_QTY").toString());
						indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();
						String strPOQuantity=oldPoQuantity.get(indentCreationDetailsId);

						double  old_po_Quantity = 0.0;//Double.valueOf(strPOQuantity);
						if(strPOQuantity!=null && !strPOQuantity.equals("")){old_po_Quantity=Double.valueOf(strPOQuantity);}
						//this is to get old po quantity from map

						if(old_po_Quantity > 0){
							double resultQuantity=old_po_Quantity-actual_Quantity;
							if(old_po_Quantity!=actual_Quantity){
								updateTempPOQuantityDetails(indentCreationDetailsId,String.valueOf(actual_Quantity),String.valueOf(old_po_Quantity)); 
								if(resultQuantity>0){checkIOndentCreationtbl(indentNumber);}

							}

						}else{

						}
					}//for loop end
				}else{	
					int i=0;//iteration purpose using temp table data get
					for(String old_po_indentCreationId : oldPoQuantity.keySet()){
						Map<?, ?> getDetails =null;
						if(i<GetproductDetailsList.size()){
							getDetails = GetproductDetailsList.get(i);
							double actual_Quantity=Double.parseDouble(getDetails.get("PO_QTY") == null ? "0": getDetails.get("PO_QTY").toString());
							indentCreationDetailsId=getDetails.get("INDENT_CREATION_DTLS_ID") == null ? "": getDetails.get("INDENT_CREATION_DTLS_ID").toString();

							if(actual_Quantity > 0){
								String value=oldPoQuantity.get(old_po_indentCreationId);
								if(!indentCreationDetailsId.equals(old_po_indentCreationId)){updatePOIntiatedQuantityInPDTable(old_po_indentCreationId,String.valueOf(value));
								checkIOndentCreationtbl(indentNumber);}else{
									if(Double.valueOf(value)!=actual_Quantity){
										double resultQuantity=Double.valueOf(value)-actual_Quantity;
										updateTempPOQuantityDetails(old_po_indentCreationId,String.valueOf(actual_Quantity),String.valueOf(value)); 	
										if(resultQuantity>0){checkIOndentCreationtbl(indentNumber);}
									}
								}
							}
						}
						else{
							String value=oldPoQuantity.get(old_po_indentCreationId);
							updatePOIntiatedQuantityInPDTable(old_po_indentCreationId,String.valueOf(value));
							checkIOndentCreationtbl(indentNumber);
						}
						i++; //	
					}
				}*///else
			}//if


			if (null != getTemPoProductDetailsList && getTemPoProductDetailsList.size() > 0) {
				for (Map<?, ?> GetDetailsInwardBean : getTemPoProductDetailsList) {
					double receiveQuanPO=0.0;
					sno++;
					subproduct=GetDetailsInwardBean.get("SUB_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("SUB_PRODUCT_ID").toString();
					childproduct=GetDetailsInwardBean.get("CHILD_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("CHILD_PRODUCT_ID").toString();
					measurement=GetDetailsInwardBean.get("MEASUREMENT_ID") == null ? "": GetDetailsInwardBean.get("MEASUREMENT_ID").toString();
					quantity=GetDetailsInwardBean.get("PO_QTY") == null ? "": GetDetailsInwardBean.get("PO_QTY").toString();
					tax=GetDetailsInwardBean.get("TAX") == null ? "": GetDetailsInwardBean.get("TAX").toString();
					taxamount=GetDetailsInwardBean.get("TAX_AMOUNT") == null ? "": GetDetailsInwardBean.get("TAX_AMOUNT").toString();
					amountaftertax=GetDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "": GetDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString();
					basicamount=GetDetailsInwardBean.get("BASIC_AMOUNT") == null ? "": GetDetailsInwardBean.get("BASIC_AMOUNT").toString();
					price=GetDetailsInwardBean.get("PRICE") == null ? "-": GetDetailsInwardBean.get("PRICE").toString();
					hsncode=GetDetailsInwardBean.get("HSN_CODE") == null ? "": GetDetailsInwardBean.get("HSN_CODE").toString();
					totalamount=GetDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "": GetDetailsInwardBean.get("TOTAL_AMOUNT").toString();
					product=GetDetailsInwardBean.get("PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("PRODUCT_ID").toString();
					discount=GetDetailsInwardBean.get("DISCOUNT") == null ? "": GetDetailsInwardBean.get("DISCOUNT").toString();
					discountaftertax=GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT") == null ? "": GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT").toString();
					podetailsid=GetDetailsInwardBean.get("PO_ENTRY_DETAILS_ID") == null ? "": GetDetailsInwardBean.get("PO_ENTRY_DETAILS_ID").toString();
					indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();
					vendorProductDesc=GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC") == null ? "": GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC").toString();
					pd_Product_Desc=GetDetailsInwardBean.get("PD_PRODUCT_DESC") == null ? "-": GetDetailsInwardBean.get("PD_PRODUCT_DESC").toString();
					totalAmt=GetDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "-": GetDetailsInwardBean.get("TOTAL_AMOUNT").toString();
					finalAmount=finalAmount+Double.valueOf(totalAmt);
					
					if(indentCreationDetailsId.equals("-")){
						ProductDetails productDetails = new ProductDetails();
						productDetails.setStrSerialNumber(String.valueOf(sno));
						productDetails.setProductId(product);
						productDetails.setSub_ProductId(subproduct);
						productDetails.setChild_ProductId(childproduct);
						productDetails.setMeasurementId(measurement);
						productDetails.setQuantity(String.valueOf(quantity));
						productDetails.setCreatedBy("PURCHASE");
						
						int purchaseIndentProcessId = cntlIndentrocss.getPurchaseIndentProcessSequenceNumber();
						int indentCreationDetailsSeqNum =icd.getIndentCreationDetailsSequenceNumber();
						indentCreationDetailsId=String.valueOf(indentCreationDetailsSeqNum);
						productDetails.setIndentCreationDetailsId(String.valueOf(indentCreationDetailsSeqNum));
						insertIndentCreationtbl(productDetails,indentNumber,isSiteLevelPo);
						insertPurchaseDepttbl(purchaseIndentProcessId,siteId,userId,productDetails);

					}

					if(old_Po_Number!=null && !old_Po_Number.equals("-") && !old_Po_Number.equals("")){
						String retValue=getOldPoQuantitytoRevised(oldPoEntryId,indentCreationDetailsId);	
						receiveQuanPO=Double.parseDouble(retValue);
					}
					
					String query1 = "INSERT INTO SUMADHURA_PO_ENTRY_DETAILS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
					"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,INDENT_CREATION_DTLS_ID,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC,RECEIVED_QUANTITY"
					+ ") values(SUMADHURA_PO_ENTRY_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";

					int result = jdbcTemplate.update(query1, new Object[] {
							intPOEntrySeqId,product,subproduct,childproduct,measurement,
							quantity,price,basicamount,tax,taxamount,amountaftertax,
							"0","0",totalamount,hsncode,"0",discount,discountaftertax,indentCreationDetailsId,vendorProductDesc,pd_Product_Desc,receiveQuanPO

					});
					response="success";

				}
			}
			request.setAttribute("totalAmt",finalAmount); // using in revised po amount taken  for payment tables and dc,indent entry tables update it
		}catch(Exception e){
			e.printStackTrace();
		}
		return "response";

	}

	public String getAndsavePendingTransportChargesList(String tempPONumber,String strSiteId,HttpServletRequest request,int poEntryId) {
		List<Map<String, Object>> productList = null;

		String sql="";
		String ConveyanceName="";
		String ConveyanceAmount="";
		String GSTTax="";
		String GSTAmount="";
		String AmountAfterTax="";
		String indentnumber="";
		String id="";
		String transportId="";
		String reponse="";

		try {

			if (StringUtils.isNotBlank(tempPONumber) ) {
				sql += "SELECT STOCD.ID,STOCD.INDENT_NUMBER,STOCD.TRANSPORT_ID,STOCM.CHARGE_NAME,STOCD.TRANSPORT_AMOUNT,STOCD.TRANSPORT_GST_PERCENTAGE,STOCD.TRANSPORT_GST_AMOUNT,STOCD.TOTAL_AMOUNT_AFTER_GST_TAX FROM SUMADHURA_TEMP_PO_TRNS_O_CHRGS STOCD,SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM " 
					+" WHERE  STOCD.TRANSPORT_ID=STOCM.CHARGE_ID and STOCD.PO_NUMBER=?";	

				productList = jdbcTemplate.queryForList(sql, new Object[] {tempPONumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetTransportChargesDetails : productList) {
						ConveyanceAmount = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();	
						GSTTax = GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString();	
						GSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
						AmountAfterTax = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
						ConveyanceName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
						indentnumber = GetTransportChargesDetails.get("INDENT_NUMBER") == null ? "" : GetTransportChargesDetails.get("INDENT_NUMBER").toString();
						id = GetTransportChargesDetails.get("ID") == null ? "" : GetTransportChargesDetails.get("ID").toString();
						transportId = GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString();
						request.setAttribute("indentnumber","indentnumber");


						//	strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"&&";

						String query1 = "INSERT INTO SUMADHURA_PO_TRNS_O_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_GST_PERCENTAGE,TRANSPORT_GST_AMOUNT,"
							+ "TOTAL_AMOUNT_AFTER_GST_TAX,DATE_AND_TIME,TRANSPORT_AMOUNT,SITE_ID,PO_NUMBER,INDENT_NUMBER) "
							+ "values( ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?)";

						int result = jdbcTemplate.update(query1, new Object[] {id,
								transportId,GSTTax,GSTAmount,AmountAfterTax,ConveyanceAmount,
								strSiteId,poEntryId,indentnumber	});

						reponse="success";
					}
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return "reponse";
	}	
	// temp po inactive after all approval completes
	public int updatepoEntrydetails(String poNumber,String indentnumber,String strSiteId,String strUserId,String sessionSite_id,String siteLevelPoPreparedBy,String passwdForMail,String deliveryDate) {
		int result=0;

		String query1 = "UPDATE SUMADHURA_TEMP_PO_ENTRY set PO_STATUS = 'I',PASSWORD_MAIL=?,DELIVERY_DATE=? WHERE PO_NUMBER = ?";
		result = jdbcTemplate.update(query1, new Object[] {passwdForMail,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poNumber });

		List<Map<String, Object>> dbIndentDts = null;

		double doublePendingQuantity = 0;
		double doublePOIntatedQuantity = 0;
		String strStatus = "I";
		int responseCount = 0;
		// below condition indent automatically created internally so We can set with vnd after approve completes
		if(!siteLevelPoPreparedBy.equalsIgnoreCase("PURCHASE_DEPT") && !siteLevelPoPreparedBy.equals("")){
			
			String query = "update SUMADHURA_INDENT_CREATION set PENDING_EMP_ID=?,PENDIND_DEPT_ID=? where INDENT_CREATION_ID=?";
			int result1 = jdbcTemplate.update(query, new Object[] {"-","VND",indentnumber});
		}
		

		String query = "  select SPDIP.PENDING_QUANTIY,SPDIP.PO_INTIATED_QUANTITY  from SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP ,SUMADHURA_INDENT_CREATION SIC, "+
		"SUMADHURA_INDENT_CREATION_DTLS SICD "+
		"where  SPDIP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID and "+
		" SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID and SIC.INDENT_CREATION_ID = ? ";

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentnumber});
		for(Map<String, Object> prods : dbIndentDts) {

			doublePendingQuantity = Double.parseDouble(prods.get("PENDING_QUANTIY")==null ? "0" :   prods.get("PENDING_QUANTIY").toString());
			doublePOIntatedQuantity = Double.parseDouble(prods.get("PO_INTIATED_QUANTITY")==null ? "0" :   prods.get("PO_INTIATED_QUANTITY").toString());

			if(doublePendingQuantity > doublePOIntatedQuantity){
				strStatus = "A";
				break;
			}

		}	


		if(strStatus.equals("I")){

			/*query = " insert into SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID,INDENT_CREATION_ID,INDENT_TYPE, "+
			" CREATION_DATE,SITE_ID,INDENT_CREATE_APPROVE_EMP_ID) values(INDENT_CREATION_APPROVAL_SEQ.NEXTVAL,?,?,"+
			"sysdate,?,?)";
			jdbcTemplate.update(query, new Object[]  {indentnumber,"A",sessionSite_id,strUserId});*/

			query = "update SUMADHURA_INDENT_CREATION set PENDIND_DEPT_ID = ?, MODIFYDATE= sysdate where INDENT_CREATION_ID = ?";
			responseCount = jdbcTemplate.update(query, new Object[]  {"VND",indentnumber});

		}	

		return result;
	}

	// get temp po terms and condition save in permanent terms & Conditions
	public String gettermsconditions(String tempPONumber,String permPoNumber) {

		List<Map<String, Object>> termList = null;

		String response="";

		String sql1="SELECT TERMS_CONDITION_ID,VENDOR_ID,INDENT_NO,PO_NUMBER,TERMS_CONDITION_DESC FROM  SUMADHURA_TEMP_PO_TERMS_COND WHERE PO_NUMBER=?";

		termList = jdbcTemplate.queryForList(sql1, new Object[] {tempPONumber});
		//System.out.println("the list size is "+termList.size());
		if (null != termList && termList.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : termList) {

				String termsandconditions = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();
				String terms=GetTransportChargesDetails.get("TERMS_CONDITION_ID") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_ID").toString();
				String temppono=GetTransportChargesDetails.get("PO_NUMBER") == null ? "" : GetTransportChargesDetails.get("PO_NUMBER").toString();
				String tempindentno=GetTransportChargesDetails.get("INDENT_NO") == null ? "" : GetTransportChargesDetails.get("INDENT_NO").toString();
				String tempvenidno=GetTransportChargesDetails.get("VENDOR_ID") == null ? "" : GetTransportChargesDetails.get("VENDOR_ID").toString();


				int val=savetermconditions(termsandconditions,terms,permPoNumber,tempindentno,tempvenidno);

				response="success";
			}	
		}


		return "response";
	}

	public int savetermconditions(String termsandconditions,String terms,String temppono,String tempindentno,String tempvenidno)
	{

		int	result=0;
		String query2 ="INSERT INTO SUMADHURA_PD_TERMS_CONDITIONS(TERMS_CONDITION_ID,VENDOR_ID,INDENT_NO,PO_ENTRY_ID,TERMS_CONDITION_DESC,ENTRY_DATE) values(? , ?, ?, ?, ?,sysdate)";

		result = jdbcTemplate.update(query2, new Object[] {
				terms,tempvenidno,tempindentno,temppono,termsandconditions
		});						 


		return result;
	}

	public int updatePurchaseDeptIndentProcestbl(String indentNumber, String quantity,String ponumber,String indentCreationdtlsId,boolean type) {

		
		List<Map<String, Object>> dbProductDetailsList = null;
		
		String availability_Id="";
		double purchase_Form_Quantity=0.0;
		double purchase_Init_Quantity=0.0;
		String siteLevelPo ="";
		
		String sql="select PO_INTIATED_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID=? ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {indentCreationdtlsId});
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				
				purchase_Init_Quantity = Double.parseDouble(prods.get("PO_INTIATED_QUANTITY") == null ? "" : prods.get("PO_INTIATED_QUANTITY").toString());
				
			}
		}
		purchase_Form_Quantity=Double.parseDouble(quantity);
		purchase_Init_Quantity=purchase_Init_Quantity-purchase_Form_Quantity;
		
		if(type){ // for cancel po purpose i can write it to reduce code like not written separeted method
			
			String sql1="select PREPARED_BY from SUMADHURA_PO_ENTRY where PO_NUMBER='"+ponumber+"'";
			 siteLevelPo = jdbcTemplate.queryForObject(sql1, String.class);
			
		}else{
		String sql1="select PREPARED_BY from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER='"+ponumber+"'";
		 siteLevelPo = jdbcTemplate.queryForObject(sql1, String.class);
		}
		if(!siteLevelPo.equalsIgnoreCase("PURCHASE_DEPT") && !siteLevelPo.equals("")){
			
			String query1="update SUMADHURA_INDENT_CREATION set STATUS='I' where INDENT_CREATION_ID ='"+indentNumber+"'";
			int result = jdbcTemplate.update(query1, new Object[] {});
		}else{
			String query1="update SUMADHURA_INDENT_CREATION set STATUS='A',PENDIND_DEPT_ID='998' where INDENT_CREATION_ID ='"+indentNumber+"'";
			int result = jdbcTemplate.update(query1, new Object[] {});
		}
		
		purchase_Init_Quantity=Double.parseDouble(new DecimalFormat("##.##").format(purchase_Init_Quantity));		
		String	query = "update SUM_PURCHASE_DEPT_INDENT_PROSS set STATUS= ?,PO_INTIATED_QUANTITY ='"+purchase_Init_Quantity+"' where INDENT_CREATION_DETAILS_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {"A",indentCreationdtlsId});



		return result;

	}

	public int updateTablesOnTempPORejection(String indentNumber, String ponumber, String indentCreationdtlsId) {

		String query1 = "update SUMADHURA_TEMP_PO_ENTRY set PO_STATUS =?,PASSWORD_MAIL=? where PO_NUMBER=?";
		int result1 = jdbcTemplate.update(query1, new Object[] { "I","0",ponumber });





		String  deptnumber=getpendingdepId(indentNumber);
		if(deptnumber.equals("VND")){
			String query2="update SUMADHURA_INDENT_CREATION set PENDIND_DEPT_ID =? where INDENT_CREATION_ID=?";		
			jdbcTemplate.update(query2, new Object[] {"998_PDM",indentNumber});
		}
		else if(deptnumber.equals("998_PDM")){

			String query3= "update SUMADHURA_INDENT_CREATION set PENDIND_DEPT_ID =? where INDENT_CREATION_ID=?";		

			int val1=jdbcTemplate.update(query3, new Object[] {"998",indentNumber});

		}

		return result1;

	}



	public String getpendingdepId(String indentNumber){
		List<Map<String, Object>> dbIndentDts = null;
		String deptnumber="";

		String query="select PENDIND_DEPT_ID from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID=?";
		dbIndentDts=jdbcTemplate.queryForList(query, new Object[] {indentNumber});

		if(dbIndentDts!= null){
			for(Map<String, Object> prods : dbIndentDts) {

				deptnumber =prods.get("PENDIND_DEPT_ID")==null ? "" :  prods.get("PENDIND_DEPT_ID").toString();

			}	} 

		return deptnumber;
	}

	@Override
	public int saveVendorTermsconditions(String termsAndCondition,String strVendorId,String strIndentNo)

	{

		String query1 = "INSERT INTO SUMADHURA_VND_TERMS_CONDITIONS(TERMS_CONDITION_ID,VENDOR_ID,INDENT_NO,ENTRY_DATE,TERMS_CONDITION_DESC) values(SUMADHURA_VND_TERMS_COND_SEQ.NEXTVAL,?,?,sysdate,?)";
		int result1 = jdbcTemplate.update(query1, new Object[] {
				strVendorId,
				strIndentNo,

				termsAndCondition
		});



		return result1;
	}






	public List<String> getVendortermsconditions(String indentNo,String vendorId) {

		List<Map<String, Object>> termList = null;
		List<String> listOfTermsAndConditions = new ArrayList<String>();
		String response="";

		String sql1="SELECT SVTC.TERMS_CONDITION_DESC FROM  SUMADHURA_VND_TERMS_CONDITIONS SVTC WHERE  SVTC.VENDOR_ID=? AND SVTC.INDENT_NO=?";


		termList = jdbcTemplate.queryForList(sql1, new Object[] {vendorId,indentNo});
	//	System.out.println("the list size is "+termList.size());
		if (null != termList && termList.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : termList) {

				String termsandconditions = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();

				listOfTermsAndConditions.add(String.valueOf(termsandconditions));


				response="success";
			}	
		}


		return listOfTermsAndConditions;
	}


	@Override
	public int doInactiveInIndentCreation(String indentNumber) { //'C' FOR CLOSE INDENT STATUS --I-->INACTIVE,A-->ACTIVE
		String query = "UPDATE SUMADHURA_INDENT_CREATION  set STATUS = 'C' "
			+ " where INDENT_CREATION_ID = ? ";
		int result = jdbcTemplate.update(query, new Object[] {
				indentNumber
		});
		return result;

	}
	@Override
	public int getIndentCreationApprovalSequenceNumber() {
		int indentCreationDetailsSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CREATION_APPROVAL_SEQ.NEXTVAL FROM DUAL");
		return indentCreationDetailsSeqNum;
	}



	@Override
	public int insertIndentCreationApprovalDtls(int indentCreationApprovalSeqNum, String indentNumber,
			IndentCreationDto indentCreationDto) {
		String query = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
			+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "+
			"VALUES(?, ?, ?, sysdate, ?, ?, ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationApprovalSeqNum, 	indentNumber,
				"S", indentCreationDto.getSiteId(),indentCreationDto.getUserId(),indentCreationDto.getPurpose()
		});
		return result;

	}



	@Override
	public int doInactiveInPurchaseTable(String indentNumber,String typeOfPurchase,String quantity) {
		String query = "UPDATE SUM_PURCHASE_DEPT_INDENT_PROSS set STATUS = 'I',TYPE_OF_PURCHASE='"+typeOfPurchase+"',CLOSED_INDENT_QUANT=CLOSED_INDENT_QUANT +'"+quantity+"' "
			+ " where INDENT_CREATION_DETAILS_ID=? ";
			
		int result = jdbcTemplate.update(query, new Object[] {
				indentNumber
		});
		return result;

	}

	@Override
	public List<ProductDetails> getProductDetailsLists(String indentNo, String vendorName,List<ProductDetails> listProductDetails,HttpServletRequest request) {
		List<Map<String, Object>> getDetailsList = null;
		//	List<ProductDetails> listProductDetails  =  new ArrayList<ProductDetails>();

		List<ProductDetails> getPoDetails = new ArrayList<ProductDetails>();
		ProductDetails objGetDetails=null;

		//	JdbcTemplate template = null;
		String sql = "SELECT SEFD.INDENT_NO,(P.NAME) AS PRODUCTNAME,(SP.NAME) AS SUBPRODUCTNAME,(CP.NAME) AS CHILDPRODUCTNAME,CP.MATERIAL_GROUP_ID,M.NAME,SEFD.VENDOR_MENTIONED_QTY,"+
		" P.PRODUCT_ID,SP.SUB_PRODUCT_ID,CP.CHILD_PRODUCT_ID,M.MEASUREMENT_ID,"+
		" SEFD.PRICE,SEFD.BASIC_AMOUNT,SEFD.DISCOUNT,SEFD.AMOUNT_AFTER_DISCOUNT,SEFD.TAX,SEFD.TAX_AMOUNT,SEFD.AMOUNT_AFTER_TAX,SEFD.OTHER_CHARGES,"+
		" SEFD.OTHER_CHARGES_AFTER_TAX,SEFD.TOTAL_AMOUNT,SEFD.HSN_CODE,SEFD.TAX_ON_OTHER_TRANSPORT_CHG,SEFD.INDENT_CREATION_DETAILS_ID,SEFD.CHILDPROD_CUST_DESC,SEFD.PURCHASE_DEPT_CHILD_PROD_DISC,INGST.TAX_PERCENTAGE "+
		" FROM  SUMADHURA_ENQUIRY_FORM_DETAILS SEFD,PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M,VENDOR_DETAILS VD,INDENT_GST INGST "+
		" WHERE  SEFD.PRODUCT_ID=P.PRODUCT_ID AND SEFD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SEFD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID"+
		" AND SEFD.VENDOR_ID=VD.VENDOR_ID AND SEFD.MEASUREMENT_ID=M.MEASUREMENT_ID AND INGST.TAX_ID = SEFD.TAX and SEFD.INDENT_NO=? AND VD.VENDOR_NAME=?";
		getDetailsList = jdbcTemplate.queryForList(sql, new Object[] {indentNo,vendorName});		

		try {

			//	template = new JdbcTemplate(DBConnection.getDbConnection())	

			/*if (null != getDetailsList && getDetailsList.size() > 0) {
				int i = 1;
				double finalamountdiv=0.0;
				for (Map<?, ?> GetDetailsBean : getDetailsList) {

					objGetDetails = new ProductDetails();
					objGetDetails.setIndentNo(GetDetailsBean.get("INDENT_NO") == null ? "" : GetDetailsBean.get("INDENT_NO").toString());
					objGetDetails.setProductId(GetDetailsBean.get("PRODUCT_ID") == null ? "" : GetDetailsBean.get("PRODUCT_ID").toString());
					objGetDetails.setSub_ProductId(GetDetailsBean.get("SUB_PRODUCT_ID") == null ? "" : GetDetailsBean.get("SUB_PRODUCT_ID").toString());
					objGetDetails.setChild_ProductId(GetDetailsBean.get("CHILD_PRODUCT_ID") == null ? "" : GetDetailsBean.get("CHILD_PRODUCT_ID").toString());
					objGetDetails.setMeasurementId(GetDetailsBean.get("MEASUREMENT_ID") == null ? "" : GetDetailsBean.get("MEASUREMENT_ID").toString());

					objGetDetails.setProductName(GetDetailsBean.get("PRODUCTNAME") == null ? "" : GetDetailsBean.get("PRODUCTNAME").toString());
					objGetDetails.setSub_ProductName(GetDetailsBean.get("SUBPRODUCTNAME") == null ? "" : GetDetailsBean.get("SUBPRODUCTNAME").toString());
					objGetDetails.setChild_ProductName(GetDetailsBean.get("CHILDPRODUCTNAME") == null ? "" : GetDetailsBean.get("CHILDPRODUCTNAME").toString());
					objGetDetails.setMeasurementName(GetDetailsBean.get("NAME") == null ? "" : GetDetailsBean.get("NAME").toString());
					objGetDetails.setRequiredQuantity(GetDetailsBean.get("VENDOR_MENTIONED_QTY") == null ? "" : GetDetailsBean.get("VENDOR_MENTIONED_QTY").toString());
					objGetDetails.setPrice(GetDetailsBean.get("PRICE") == null ? "" : GetDetailsBean.get("PRICE").toString());
					objGetDetails.setBasicAmt(GetDetailsBean.get("BASIC_AMOUNT") == null ? "" : GetDetailsBean.get("BASIC_AMOUNT").toString());
					objGetDetails.setStrDiscount(GetDetailsBean.get("DISCOUNT") == null ? "" : GetDetailsBean.get("DISCOUNT").toString());
					objGetDetails.setStrAmtAfterDiscount(GetDetailsBean.get("AMOUNT_AFTER_DISCOUNT") == null ? "" : GetDetailsBean.get("AMOUNT_AFTER_DISCOUNT").toString());
					objGetDetails.setTax(GetDetailsBean.get("TAX") == null ? "" : GetDetailsBean.get("TAX").toString());
					objGetDetails.setTaxAmount(GetDetailsBean.get("TAX_AMOUNT") == null ? "" : GetDetailsBean.get("TAX_AMOUNT").toString());
					objGetDetails.setAmountAfterTax(GetDetailsBean.get("AMOUNT_AFTER_TAX") == null ? "" : GetDetailsBean.get("AMOUNT_AFTER_TAX").toString());
					objGetDetails.setOthercharges1(GetDetailsBean.get("OTHER_CHARGES") == null ? "" : GetDetailsBean.get("OTHER_CHARGES").toString());
					objGetDetails.setOtherchargesaftertax1(GetDetailsBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : GetDetailsBean.get("OTHER_CHARGES_AFTER_TAX").toString());
					objGetDetails.setTotalAmount(GetDetailsBean.get("TOTAL_AMOUNT") == null ? "" : GetDetailsBean.get("TOTAL_AMOUNT").toString());
					objGetDetails.setHsnCode(GetDetailsBean.get("HSN_CODE") == null ? "" : GetDetailsBean.get("HSN_CODE").toString());
					objGetDetails.setTaxonothertranportcharge1(GetDetailsBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0" : GetDetailsBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());


					objGetDetails.setFinalamtdiv(GetDetailsBean.get("TOTAL_AMOUNT") == null ? "" : GetDetailsBean.get("TOTAL_AMOUNT").toString());


					String s=objGetDetails.getFinalamtdiv();
					if(s!="")
					{
						finalamountdiv = finalamountdiv+Double.parseDouble(s);
						System.out.println("the final amount"+finalamountdiv);
						objGetDetails.setFinalamtdiv(String.valueOf(finalamountdiv));
					}

					objGetDetails.setSerialno(i);

					GetDetailsInward.add(objGetDetails);

					i++;
				}
			}*/



			ProductDetails objProductDetails = null;
			String strListChildProdId = "";
			String strListChildProdName = "";
			String serverChildProdId = "";
			String strListProductId = "";
			String strListProductName = "";
			String strListSubproductId = "";
			String strListSubproductName = "";
			String strListMesurementId = "";
			String strListMesurementName = "";
			String strFormIndentCreationDetailsId = "";
			String strQuantity = "";
			String strPOIntiatedQuantity = "";
			String purchasedeptRequestReceiveQuantity = "";
			
			String taxId = "";
			String required_Quan = "";
			String price = "";
			String discount = "";
			String amtAfterDiscount = "";
			String tax = "";
			String taxAmount = "";
			String otherCharges = "";
			String otherChargesAfterTax = "";
			String totalAmt = "";
			String hsnCode = "";
			String tax_other_tranport = "";
			String finalAmt = "";
			String basicAmt="";
			String amountAfterTax="";
			String childProductdesc="";
			String groupId="";
			
			
			
			boolean falg = true;
			String strTaxId = "";
			int j=1;
			int count=0;
			String prodValues="";

			String strTaxPercentage = "";
			for(int i =0 ; i < listProductDetails.size() ; i++){


				objProductDetails = listProductDetails.get(i);
				strListProductId=objProductDetails.getProductId();
				strListProductName=objProductDetails.getProductName();
				strListSubproductId=objProductDetails.getSub_ProductId();
				strListSubproductName=objProductDetails.getSub_ProductName();
				strListMesurementId=objProductDetails.getMeasurementId();
				strListMesurementName=objProductDetails.getMeasurementName();
				strListChildProdId = objProductDetails.getChild_ProductId();
				strListChildProdName = objProductDetails.getChild_ProductName();
				strFormIndentCreationDetailsId = objProductDetails.getIndentCreationDetailsId();
				strQuantity = objProductDetails.getQuantity();
				groupId=objProductDetails.getGroupId();
				strPOIntiatedQuantity = objProductDetails.getPoIntiatedQuantity();
				purchasedeptRequestReceiveQuantity = objProductDetails.getPurchasedeptRequestReceiveQuantity();
				if (null != getDetailsList && getDetailsList.size() > 0) {

					for (Map<?, ?> getDetailsBean : getDetailsList) {

						objGetDetails = new ProductDetails();
						serverChildProdId = getDetailsBean.get("CHILD_PRODUCT_ID") == null ? "" : getDetailsBean.get("CHILD_PRODUCT_ID").toString();
						falg  = true;

						if(strListChildProdId.equals(serverChildProdId)){

							objGetDetails = new ProductDetails();

							objGetDetails.setProductId(strListProductId);
							objGetDetails.setSub_ProductId(strListSubproductId);
							objGetDetails.setSub_ProductName(strListSubproductName);
							objGetDetails.setChild_ProductName(getDetailsBean.get("PURCHASE_DEPT_CHILD_PROD_DISC") == null ? "0" : getDetailsBean.get("PURCHASE_DEPT_CHILD_PROD_DISC").toString());
							objGetDetails.setProductName(strListProductName);
							objGetDetails.setMeasurementId(strListMesurementId);
							objGetDetails.setChild_ProductId(strListChildProdId);
							objGetDetails.setMeasurementName(strListMesurementName);
							objGetDetails.setPoIntiatedQuantity(strPOIntiatedQuantity);
							objGetDetails.setPurchasedeptRequestReceiveQuantity(purchasedeptRequestReceiveQuantity);
							objGetDetails.setGroupId(groupId);
							strTaxPercentage = "";
							strTaxId = getDetailsBean.get("TAX") == null ? "0" : getDetailsBean.get("TAX").toString();
							strTaxPercentage = getDetailsBean.get("TAX_PERCENTAGE") == null ? "0" : getDetailsBean.get("TAX_PERCENTAGE").toString();



							strTaxPercentage = strTaxId+"$"+strTaxPercentage;
							
							 required_Quan =getDetailsBean.get("VENDOR_MENTIONED_QTY") == null ? "0" : getDetailsBean.get("VENDOR_MENTIONED_QTY").toString();
							 price =getDetailsBean.get("PRICE") == null ? "0" : getDetailsBean.get("PRICE").toString();
							 basicAmt=getDetailsBean.get("BASIC_AMOUNT") == null ? "0" : getDetailsBean.get("BASIC_AMOUNT").toString();
							 discount =getDetailsBean.get("DISCOUNT") == null ? "0" : getDetailsBean.get("DISCOUNT").toString();
							 amtAfterDiscount =getDetailsBean.get("AMOUNT_AFTER_DISCOUNT") == null ? "0" : getDetailsBean.get("AMOUNT_AFTER_DISCOUNT").toString();
							 tax =getDetailsBean.get("TAX_PERCENTAGE") == null ? "0" : getDetailsBean.get("TAX_PERCENTAGE").toString();
							 taxAmount =getDetailsBean.get("TAX_AMOUNT") == null ? "0" : getDetailsBean.get("TAX_AMOUNT").toString();
							 amountAfterTax=getDetailsBean.get("AMOUNT_AFTER_TAX") == null ? "0" : getDetailsBean.get("AMOUNT_AFTER_TAX").toString();
							 otherCharges =getDetailsBean.get("OTHER_CHARGES") == null ? "0" : getDetailsBean.get("OTHER_CHARGES").toString();
							 otherChargesAfterTax =getDetailsBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "0" : getDetailsBean.get("OTHER_CHARGES_AFTER_TAX").toString();
							 totalAmt =getDetailsBean.get("TOTAL_AMOUNT") == null ? "0" : getDetailsBean.get("TOTAL_AMOUNT").toString();
							 hsnCode =getDetailsBean.get("HSN_CODE") == null ? "" : getDetailsBean.get("HSN_CODE").toString();
							 tax_other_tranport =getDetailsBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "0" : getDetailsBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString();
							 finalAmt =getDetailsBean.get("TOTAL_AMOUNT") == null ? "0" : getDetailsBean.get("TOTAL_AMOUNT").toString();
							 childProductdesc=getDetailsBean.get("CHILDPROD_CUST_DESC") == null ? "-" : getDetailsBean.get("CHILDPROD_CUST_DESC").toString();

							objGetDetails.setTaxId(strTaxPercentage);	
							objGetDetails.setRequiredQuantity(required_Quan);
							objGetDetails.setPrice(price);
							objGetDetails.setBasicAmt(basicAmt);
							objGetDetails.setStrDiscount(discount);
							objGetDetails.setStrAmtAfterDiscount(amtAfterDiscount);
							objGetDetails.setTax(tax);
							objGetDetails.setTaxAmount(taxAmount);
							objGetDetails.setAmountAfterTax(amountAfterTax);
							objGetDetails.setOthercharges1(otherCharges);
							objGetDetails.setOtherchargesaftertax1(otherChargesAfterTax);
							objGetDetails.setTotalAmount(totalAmt);
							objGetDetails.setHsnCode(hsnCode);
							objGetDetails.setTaxonothertranportcharge1(tax_other_tranport);
							objGetDetails.setFinalamtdiv(finalAmt);
							objGetDetails.setIndentCreationDetailsId(getDetailsBean.get("INDENT_CREATION_DETAILS_ID") == null ? "0" : getDetailsBean.get("INDENT_CREATION_DETAILS_ID").toString());
							objGetDetails.setChildProductCustDisc(childProductdesc);

							objGetDetails.setSerialno(j);
							prodValues=strQuantity+"|"+purchasedeptRequestReceiveQuantity+
									"|"+price+"|"+basicAmt+"|"+discount+"|"+amtAfterDiscount+"|"+strTaxPercentage+"|"+taxAmount+"|"+amountAfterTax+
									"|"+otherCharges+"|"+otherChargesAfterTax+"|"+hsnCode+"|"+tax_other_tranport+"|"+finalAmt+"|"+strFormIndentCreationDetailsId+"|"+childProductdesc;
									
							request.setAttribute("productvalues",prodValues);
							j++;
							falg  = false;
							count++;
							getPoDetails.add(objGetDetails);
							break;

						}



					}
					request.setAttribute("falg","false");

					if(falg){
						objGetDetails = new ProductDetails();

						objGetDetails.setProductId(strListProductId);
						objGetDetails.setSub_ProductId(strListSubproductId);
						objGetDetails.setChild_ProductId(strListChildProdId);
						objGetDetails.setMeasurementId(strListMesurementId);
						objGetDetails.setProductName(strListProductName);
						objGetDetails.setSub_ProductName(strListSubproductName);
						objGetDetails.setChild_ProductName(strListChildProdName);
						objGetDetails.setMeasurementName(strListMesurementName);
						objGetDetails.setRequiredQuantity(strQuantity);
						objGetDetails.setPoIntiatedQuantity(strPOIntiatedQuantity);
						objGetDetails.setPurchasedeptRequestReceiveQuantity(purchasedeptRequestReceiveQuantity);
						objGetDetails.setGroupId(groupId);
						
						// required_Quan =getDetailsBean.get("VENDOR_MENTIONED_QTY") == null ? "0" : getDetailsBean.get("VENDOR_MENTIONED_QTY").toString();
					
						 basicAmt="0";
						 discount ="0";
						 amtAfterDiscount ="0";
						 tax ="0";
						 strTaxPercentage="1$0%";
						 taxAmount ="0";
						 amountAfterTax="0";
						 otherCharges ="0";
						 otherChargesAfterTax ="0";
						 totalAmt ="0";
						 hsnCode ="";
						 tax_other_tranport ="0";
						 finalAmt ="0";
						 required_Quan="0";
						 childProductdesc="-";

						objGetDetails.setPrice("");
						objGetDetails.setBasicAmt("0");
						objGetDetails.setStrDiscount("");
						objGetDetails.setStrAmtAfterDiscount("0");
						objGetDetails.setTax("0");
						objGetDetails.setTaxAmount("0");
						objGetDetails.setAmountAfterTax("0");
						objGetDetails.setOthercharges1("0");
						objGetDetails.setOtherchargesaftertax1("0");
						objGetDetails.setTotalAmount("0");
						objGetDetails.setHsnCode("0");
						objGetDetails.setTaxonothertranportcharge1("0");
						objGetDetails.setTaxId("1$0%");
						objGetDetails.setIndentCreationDetailsId(strFormIndentCreationDetailsId);
						objGetDetails.setChildProductCustDisc("");
						objGetDetails.setSerialno(j);
						j++;
						
						prodValues=strQuantity+"|"+purchasedeptRequestReceiveQuantity+
								"|"+price+"|"+basicAmt+"|"+discount+"|"+amtAfterDiscount+"|"+strTaxPercentage+"|"+taxAmount+"|"+amountAfterTax+
								"|"+otherCharges+"|"+otherChargesAfterTax+"|"+hsnCode+"|"+tax_other_tranport+"|"+finalAmt+"|"+strFormIndentCreationDetailsId+"|"+childProductdesc;;
						request.setAttribute("productvalues",prodValues);
						getPoDetails.add(objGetDetails);
						
						
						if(count!=0){
							request.setAttribute("falg","false");
						}else{
						request.setAttribute("falg","true");
					}
					}

					
					//	}	

				}else{


					 basicAmt="0";
					 discount ="0";
					 amtAfterDiscount ="0";
					 tax ="0";
					 strTaxPercentage="1$0%";
					 taxAmount ="0";
					 amountAfterTax="0";
					 otherCharges ="0";
					 otherChargesAfterTax ="0";
					 totalAmt ="0";
					 hsnCode ="";
					 tax_other_tranport ="0";
					 finalAmt ="0";
					 required_Quan="0";
					 childProductdesc="-";
					objGetDetails = new ProductDetails();

					objGetDetails.setProductId(strListProductId);
					objGetDetails.setSub_ProductId(strListSubproductId);
					objGetDetails.setChild_ProductId(strListChildProdId);
					objGetDetails.setMeasurementId(strListMesurementId);

					objGetDetails.setProductName(strListProductName);
					objGetDetails.setSub_ProductName(strListSubproductName);
					objGetDetails.setChild_ProductName(strListChildProdName);
					objGetDetails.setMeasurementName(strListMesurementName);
					objGetDetails.setRequiredQuantity(strQuantity);
					objGetDetails.setPoIntiatedQuantity(strPOIntiatedQuantity);
					objGetDetails.setPurchasedeptRequestReceiveQuantity(purchasedeptRequestReceiveQuantity);
					objGetDetails.setGroupId(groupId);
					objGetDetails.setPrice("");
					objGetDetails.setBasicAmt("0");
					objGetDetails.setStrDiscount("");
					objGetDetails.setStrAmtAfterDiscount("0");
					objGetDetails.setTax("0");
					objGetDetails.setTaxAmount("0");
					objGetDetails.setAmountAfterTax("0");
					objGetDetails.setOthercharges1("0");
					objGetDetails.setOtherchargesaftertax1("0");
					objGetDetails.setTotalAmount("0");
					objGetDetails.setHsnCode("");
					objGetDetails.setTaxonothertranportcharge1("0");
					objGetDetails.setTaxId("1$0%");
					objGetDetails.setIndentCreationDetailsId(strFormIndentCreationDetailsId);
					objGetDetails.setChildProductCustDisc("");
					objGetDetails.setSerialno(j);
					j++;
					prodValues=strQuantity+"|"+purchasedeptRequestReceiveQuantity+
							"|"+price+"|"+basicAmt+"|"+discount+"|"+amtAfterDiscount+"|"+strTaxPercentage+"|"+taxAmount+"|"+amountAfterTax+
							"|"+otherCharges+"|"+otherChargesAfterTax+"|"+hsnCode+"|"+tax_other_tranport+"|"+finalAmt+"|"+strFormIndentCreationDetailsId+"|"+childProductdesc;
							
					request.setAttribute("productvalues",prodValues);
					getPoDetails.add(objGetDetails);
					
					if(count!=0){
						request.setAttribute("falg","false");
					}else{
					request.setAttribute("falg","true");
				}

				}
				

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return getPoDetails;
	}

	public Map<String, String> getApproveCreateEmp(int indentnumber,HttpServletRequest request)
	{
		List<Map<String, Object>> productList = null;

		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		String empName="";
		String creationDate="";
		SimpleDateFormat df2 = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss"); 
		String query = "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUM_INT_CREATION_APPROVAL_DTLS  SICAD"+
		" where INDENT_TYPE = 'C' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and INDENT_CREATION_ID = ?";

		productList = jdbcTemplate.queryForList(query, new Object[] {indentnumber});
		try{

			if(productList!= null){
				for(Map<String, Object> gstSlabs : productList) {


					creationDate=gstSlabs.get("CREATION_DATE").toString();
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);
					System.out.println("the creation date"+creationDate);






					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);

					gst.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);

				}	} 
		}catch(Exception e){
			e.printStackTrace();
		}

		return gst;
	}

	public Map<String, String> getPOApproveCreateEmp(int tempPONumber,HttpServletRequest request)
	{
		List<Map<String, Object>> productList = null;

		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		String empName="";
		String creationDate="";
		SimpleDateFormat df2 = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss"); 
		String query = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'C' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = ?";

		productList = jdbcTemplate.queryForList(query, new Object[] {tempPONumber});
		try{

			if(productList!= null){
				for(Map<String, Object> gstSlabs : productList) {


					creationDate=gstSlabs.get("CREATION_DATE").toString();
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);
				//	System.out.println("the creation date"+creationDate);






					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);

					gst.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);

				}	} 
		}catch(Exception e){
			e.printStackTrace();
		}

		return gst;
	}

	public void getVerifiedEmpNames(int indentnumber,HttpServletRequest request,String siteId)
	{

		Map<String, String> names = null;
		names = new TreeMap<String, String>();
		Map<String, String> names1=new TreeMap<String, String>();
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> productList1 = null;
		//	List<String> listOfVerifiedNames = new ArrayList<String>();
		//	List<String> listOfVerifiedDates = new ArrayList<String>();
		//	List<String> listOfApprovedNames = new ArrayList<String>();
		//	List<String> listOfApprovedDates = new ArrayList<String>();
		//	String empName="";
		String creationDate="";

		String query = "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED"+
		" where SICAD.INDENT_TYPE = 'A' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and SICAD.SITE_ID= ? and INDENT_CREATION_ID = ?  order by SICAD.CREATION_DATE  ASC"; 

		productList = jdbcTemplate.queryForList(query, new Object[] {siteId,indentnumber});

		String query1 = "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED"+
		" where SICAD.INDENT_TYPE = 'A' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and SICAD.SITE_ID= ? and INDENT_CREATION_ID = ?  order by SICAD.CREATION_DATE  DESC"; 

		productList1 = jdbcTemplate.queryForList(query1, new Object[] {siteId,indentnumber});
		//DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
		try{

			if(productList!= null){
				for(Map<String, Object> gstSlabs : productList) {

					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);


					//String creationDate = "2018-03-01 11:01:53.0";

					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);


					//System.out.println(creationDate);


					names.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);
					break;

				}	} 
			request.setAttribute("listOfVerifiedNames",names);


			if(productList1!= null){
				for(Map<String, Object> gstSlabs : productList1) {
					//SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);


					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);


					names1.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);
					break;

				}	} 
			request.setAttribute("listOfApprovedNames",names1);
		}


		catch(Exception e){
			e.printStackTrace();
		}
	}


	public void getPOVerifiedEmpNames(int intTempPONumber,HttpServletRequest request)
	{

		Map<String, String> names = null;
		names = new TreeMap<String, String>();
		Map<String, String> names1=new TreeMap<String, String>();
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> productList1 = null;
		//	List<String> listOfVerifiedNames = new ArrayList<String>();
		//	List<String> listOfVerifiedDates = new ArrayList<String>();
		//	List<String> listOfApprovedNames = new ArrayList<String>();
		//	List<String> listOfApprovedDates = new ArrayList<String>();
		//	String empName="";
		String creationDate="";

		String query = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'A' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = ?   order by SPCAD.CREATION_DATE  ASC"; 

		productList = jdbcTemplate.queryForList(query, new Object[] {intTempPONumber});

		String query1 = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'A' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = ?  order by SPCAD.CREATION_DATE  DESC"; 

		productList1 = jdbcTemplate.queryForList(query1, new Object[] {intTempPONumber});
		//DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 
		try{

			if(productList!= null){
				for(Map<String, Object> gstSlabs : productList) {

					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);


					//String creationDate = "2018-03-01 11:01:53.0";

					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);


					//System.out.println(creationDate);


					names.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);
					break;

				}	} 
			request.setAttribute("listOfVerifiedNames",names);


			if(productList1!= null){
				for(Map<String, Object> gstSlabs : productList1) {
					//SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//Date d1 = df2.parse(creationDate);
					//creationDate= df2.format(d1);


					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);


					names1.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);
					break;

				}	} 
			request.setAttribute("listOfApprovedNames",names1);
		}


		catch(Exception e){
			e.printStackTrace();
		}
	}


	@Override
	public List<String>  getAllEmployeeEmailsUnderDepartment(String deptId) {


		List<Map<String, Object>> dbIndentDts = null;
		String strEmailId = "";
		List<String> objList = new ArrayList<String>();
		//thisIsAStringArray[5] = "FFF";
		String query = " select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where DEPT_ID = ? ";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {deptId});

		if(dbIndentDts!= null){



			for(Map<String, Object> prods : dbIndentDts) {



				strEmailId = prods.get("EMP_EMAIL")==null ? "" :   prods.get("EMP_EMAIL").toString();

				if(strEmailId.contains(",")){
					for(String strEmailId1 : strEmailId.split(","))
					{
						objList.add(strEmailId1);
					}
				}
				else if(!strEmailId.equals("") && strEmailId!=null){
				objList.add(strEmailId);
				}


			}	
		}

		return objList;

	}

	public String getIndentCreationDetailsId(String indentCreationId){
		List<Map<String, Object>> productList = null;
		String indentCreationDtslId="";
		String sql="select INDENT_CREATION_DETAILS_ID from SUMADHURA_INDENT_CREATION_DTLS where INDENT_CREATION_ID=?";

		productList = jdbcTemplate.queryForList(sql, new Object[] {indentCreationId});
		if (null != productList && productList.size() > 0) {
			for (Map<?, ?> getTransportChargesDetails : productList) {
				indentCreationDtslId = getTransportChargesDetails.get("INDENT_CREATION_DETAILS_ID") == null ? "" : getTransportChargesDetails.get("INDENT_CREATION_DETAILS_ID").toString();

			}
		}

		return indentCreationDtslId;
	}



	@Override
	public int getSiteIdByPONumber(String poNumber) {
		return jdbcTemplate.queryForInt("select SIC.SITE_ID from SUMADHURA_TEMP_PO_ENTRY STPE,"
				+ "SUMADHURA_INDENT_CREATION SIC where SIC.INDENT_CREATION_ID = STPE.INDENT_NO "
				+ "and STPE.PO_NUMBER = '"+poNumber+"'");

	}
	@Override
	public int updateTempPoEntry(String approvalEmpId,String poNumber,String ccmailId,String siteLevelPoPreparedBy,String indentnumber,String passwdForMail,String deliveryDate){
		int val=0;
		String response="";
		List<Map<String, Object>> dbMailsList = null;
		if(!siteLevelPoPreparedBy.equalsIgnoreCase("PURCHASE_DEPT") && !siteLevelPoPreparedBy.equals("-") && !siteLevelPoPreparedBy.equals("")){
			
			String query = "update SUMADHURA_INDENT_CREATION set PENDING_EMP_ID=? where INDENT_CREATION_ID=?";
			int result = jdbcTemplate.update(query, new Object[] {approvalEmpId,indentnumber});
		}
		// which is execute in approval from mail already existed mails not reflecting request object so taken here
		if(ccmailId==null || ccmailId.equals("")){
			String sql="select CC_EMAIL_ID from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER=?";
			dbMailsList=jdbcTemplate.queryForList(sql, new Object[]{poNumber});
			if(dbMailsList!=null && dbMailsList.size()>0){
			for(Map<String, Object> ccMailIds : dbMailsList) {
				ccmailId=ccMailIds.get("CC_EMAIL_ID")==null ? "" : ccMailIds.get("CC_EMAIL_ID").toString();
			}
			}
		}
		String query1 = "update SUMADHURA_TEMP_PO_ENTRY set TEMP_PO_PENDING_EMP_ID=?, CC_EMAIL_ID=?,PASSWORD_MAIL=?,DELIVERY_DATE=? where PO_NUMBER=?";
		val = jdbcTemplate.update(query1, new Object[] {approvalEmpId,ccmailId,passwdForMail,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poNumber
		});


		return val;
	}

	@Override
	public int insertTempPOorPOCreateApproverDtls(String strTempPoNumber,String poNumber,String strEmpId,String strSIteId,String strOperationType,String remarks){

		String query1 = " insert into SUMADHURA_PO_CRT_APPRL_DTLS(PO_CREATION_APPROVAL_DTLS_ID,TEMP_PO_NUMBER,PO_ENTRY_ID,CREATION_DATE,SITE_ID,PO_CREATE_APPROVE_EMP_ID,OPERATION_TYPE,PURPOSE)"+
		"values( PO_CRT_APPROVAL_DTLS_ID_SEQ.nextval, ?, ?, sysdate, ?, ?,?,?)";

		int result = jdbcTemplate.update(query1, new Object[] {strTempPoNumber,poNumber,strSIteId,strEmpId,strOperationType,remarks});

		return result;
	}


	@Override
	public List<IndentCreationBean> ViewTempPo(String fromDate, String toDate,String tempPoNumber) {

		String query = "";
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbMarketingPODts = null;
		IndentCreationBean indentObj = null; 
		String sql="";
		String type_Of_Purchase="";
		String operationType="";
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			//	template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = " SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,PO_DATE,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.OPERATION_TYPE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_EMPLOYEE_DETAILS SED1,SITE S,SUMADHURA_INDENT_CREATION SIC WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				sql="SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,STPE.OLD_PO_NUMBER,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)>=TO_DATE('"+fromDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				sql="SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,STPE.OLD_PO_NUMBER,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";


			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				sql="SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,STPE.OLD_PO_NUMBER,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";

			}
			else if(StringUtils.isNotBlank(tempPoNumber)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY!='MARKETING_DEPT'";
				
				sql="SELECT DISTINCT (STPE.PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,STPE.OLD_PO_NUMBER,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
				+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY='MARKETING_DEPT'";
			}



			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
			dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
		
			// this is for marketing po purpose
			
			for(Map<String, Object> makrtDetails : dbMarketingPODts) {
				indentObj = new IndentCreationBean();
				indentObj.setPonumber(makrtDetails.get("PO_NUMBER")==null ? "" : makrtDetails.get("PO_NUMBER").toString());
				indentObj.setToEmpName(makrtDetails.get("EMP_NAME")==null ? "" : makrtDetails.get("EMP_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(makrtDetails.get("INDENT_NO")==null ? "0" : makrtDetails.get("INDENT_NO").toString()));
				indentObj.setSiteName(makrtDetails.get("SITE_NAME")==null ? "0" : makrtDetails.get("SITE_NAME").toString());
				indentObj.setSiteId(Integer.parseInt(makrtDetails.get("SITE_ID")==null ? "0" : makrtDetails.get("SITE_ID").toString()));
				indentObj.setPending_Emp_Name(makrtDetails.get("PENDING_EMP_NAME")==null ? "0" : makrtDetails.get("PENDING_EMP_NAME").toString());
				indentObj.setSiteWiseIndentNo(0);
				indentObj.setOld_Po_Number(makrtDetails.get("OLD_PO_NUMBER")==null ? "-" : makrtDetails.get("OLD_PO_NUMBER").toString());
				indentObj.setType_Of_Purchase("MARKETING DEPT");
				indentObj.setFromDate(fromDate);
				indentObj.setToDate(toDate);
				String date=makrtDetails.get("PO_DATE")==null ? "" : makrtDetails.get("PO_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
				
			}
			
			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());

				//	indentObj.setStrRequiredDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setToEmpName(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "" : prods.get("INDENT_NO").toString()));
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "0" : prods.get("SITE_NAME").toString());
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
				indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "" : prods.get("SITEWISE_INDENT_NO").toString()));
				String date=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				indentObj.setPending_Emp_Name(prods.get("PENDING_EMP_NAME")==null ? "0" : prods.get("PENDING_EMP_NAME").toString());
				type_Of_Purchase=prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString();
				operationType=(prods.get("OPERATION_TYPE")==null ? "" : prods.get("OPERATION_TYPE").toString());
				indentObj.setOld_Po_Number(prods.get("OLD_PO_NUMBER")==null ? "-" : prods.get("OLD_PO_NUMBER").toString());
				indentObj.setFromDate(fromDate);
				indentObj.setToDate(toDate);
				if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT")){
					indentObj.setType_Of_Purchase("MARKETING DEPT");
				}else{
				if((type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("REVISED"))){
					
					indentObj.setType_Of_Purchase("REVISED PO");
				}else if(type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("UPDATE")){
					indentObj.setType_Of_Purchase("UPDATE PO");
				}
				else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("CREATION"))){
					
					indentObj.setType_Of_Purchase("PURCHASE DEPT PO");
				} else{
					
					indentObj.setType_Of_Purchase("SITELEVEL PO");
				}
				}
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			//	template = null;
			dbIndentDts = null;
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getListOfActivePOs(String site_id,String PONumber){

		List<Map<String, Object>> poList = null;
		String sql="";
		if(site_id!=null && !site_id.equals("") && (PONumber==null || PONumber.equals(""))){
		 sql=new StringBuilder("SELECT")
			.append(" SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,to_char(SPE.PO_DATE,'DD-MM-YYYY hh:mm:ss') as PO_DATE,SPE.SITE_ID,SPE.PO_ENTRY_ID ")
			.append(" from VENDOR_DETAILS VD,SITE S,SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID ")
			.append(" where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A' AND SPE.SITE_ID='"+site_id+"'")
			.toString();
		}else if((site_id==null || site_id.equals("")) && (PONumber!=null && !PONumber.equals(""))){
			sql=new StringBuilder("SELECT")
			.append(" SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,to_char(SPE.PO_DATE,'DD-MM-YYYY hh:mm:ss') as PO_DATE,SPE.SITE_ID,SPE.PO_ENTRY_ID ")
			.append(" from VENDOR_DETAILS VD,SITE S,SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID ")
			.append(" where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A' AND SPE.PO_NUMBER='"+PONumber+"'")
			.toString();
		}else if((site_id!=null && !site_id.equals("")) && (PONumber!=null && !PONumber.equals(""))){
			sql=new StringBuilder("SELECT")
			.append(" SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,to_char(SPE.PO_DATE,'DD-MM-YYYY hh:mm:ss') as PO_DATE,SPE.SITE_ID,SPE.PO_ENTRY_ID ")
			.append(" from VENDOR_DETAILS VD,SITE S,SUMADHURA_PO_ENTRY SPE LEFT OUTER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID ")
			.append(" where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A' AND SPE.PO_NUMBER='"+PONumber+"' AND SPE.SITE_ID='"+site_id+"'")
			.toString();
		}
		poList = jdbcTemplate.queryForList(sql);
		//List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		return poList;

	}
	@Override
	public int updatePOEntryDetails(ProductDetails productDetails)
	{

		String query =  "update SUMADHURA_PO_ENTRY_DETAILS set VENDOR_PRODUCT_DESC=?,PRODUCT_ID = ? ,SUB_PRODUCT_ID = ? ,CHILD_PRODUCT_ID = ? ,MEASUR_MNT_ID = ? ,PO_QTY = ? ,PRICE = ? ,BASIC_AMOUNT = ? ,DISCOUNT = ? ,AMOUNT_AFTER_DISCOUNT = ? ,TAX = ? ,TAX_AMOUNT = ? ,AMOUNT_AFTER_TAX = ? ,HSN_CODE = ? ,OTHER_CHARGES = ? ,TAX_ON_OTHER_TRANSPORT_CHG = ? ,OTHER_CHARGES_AFTER_TAX = ? ,TOTAL_AMOUNT = ? where PO_ENTRY_DETAILS_ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getChildProductCustDisc(),
				productDetails.getProductId(),
				productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),
				productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getTax(),productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),
				productDetails.getHsnCode(),
				productDetails.getOtherOrTransportCharges1(),productDetails.getTaxOnOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),
				productDetails.getPoEntryDetailsId()


		});
		return result;
	}

	@Override
	public int updatePOTransportChargesDetails(TransportChargesDto transportChargesDto,int id){

		String query =  "update SUMADHURA_PO_TRNS_O_CHRGS_DTLS set TRANSPORT_ID = ? ,TRANSPORT_AMOUNT = ? ,"
			+ " TRANSPORT_GST_PERCENTAGE = ? ,TRANSPORT_GST_AMOUNT = ? ,TOTAL_AMOUNT_AFTER_GST_TAX = ? "
			+ " where ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {

				transportChargesDto.getTransportId(),
				transportChargesDto.getTransportAmount(),
				transportChargesDto.getTransportGSTPercentage(),
				transportChargesDto.getTransportGSTAmount(),
				transportChargesDto.getTotalAmountAfterGSTTax(),
				id
		});


		return result;
	}



	@Override
	public int getPoEnterSeqNoByPONumber(String poNumber,String toSite) {
		String query  = "select PO_ENTRY_ID from  SUMADHURA_PO_ENTRY where PO_NUMBER = ? and SITE_ID = ? ";
		return jdbcTemplate.queryForInt(query, new Object[] {poNumber,toSite});

	}



	@Override
	public int deletePOEntryDetails(String poEntryDetailsId) {
		String query =  "DELETE FROM SUMADHURA_PO_ENTRY_DETAILS WHERE PO_ENTRY_DETAILS_ID = ? ";
		return jdbcTemplate.update(query, new Object[] {poEntryDetailsId});
	}



	@Override
	public int updatePOIntiatedQuantityInPDTable(String indentCreationDetailsId, String quantity) {
		int result=0;
		String sql="select PO_INTIATED_QUANTITY FROM SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID ='"+indentCreationDetailsId+"'";
		String po_Initiated_Quan = jdbcTemplate.queryForObject(sql, String.class);
		double strPO_Quan=(Double.parseDouble(po_Initiated_Quan)-Double.parseDouble(quantity));
		String query ="update SUM_PURCHASE_DEPT_INDENT_PROSS set PO_INTIATED_QUANTITY =?,STATUS='A'  where INDENT_CREATION_DETAILS_ID = ? ";
		result=jdbcTemplate.update(query, new Object[] {strPO_Quan,indentCreationDetailsId});
		return result;
	}



	@Override
	public int deletePOTransportChargesDetails(int poTransChrgsDtlsSeqNo) {
		String query =  "DELETE FROM SUMADHURA_PO_TRNS_O_CHRGS_DTLS WHERE ID = ? ";
		return jdbcTemplate.update(query, new Object[] {poTransChrgsDtlsSeqNo});
	}



	@Override
	public List<IndentCreationBean> getDeletedProductDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"
			+ " SICHD.ACTUAL_PRODCT_ID,SICHD.ACTUAL_SUB_PRODCT_ID,SICHD.ACTUAL_CHILD_PRODCT_ID,SICHD.ACTUAL_MEASURMENT_ID,"
			+ " SICHD.ACTUAL_QUANTITY,SICHD.REMARKS,SED.EMP_NAME FROM SUMADHURA_INDENT_CHANGED_DTLS SICHD, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST,SUMADHURA_EMPLOYEE_DETAILS SED "
			+ " WHERE SICHD.ACTUAL_PRODCT_ID=P.PRODUCT_ID AND SICHD.ACTUAL_SUB_PRODCT_ID=SP.SUB_PRODUCT_ID AND SICHD.ACTUAL_CHILD_PRODCT_ID=CP.CHILD_PRODUCT_ID "
			+ " AND SICHD.ACTUAL_MEASURMENT_ID=MST.MEASUREMENT_ID AND SICHD.INDENT_CREATION_ID = ? AND SICHD.INDENT_CHANGE_ACTION = 'D' AND SICHD.EMP_ID = SED.EMP_ID";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		int sno = 50;
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			sno++;
			indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			String prodId = prods.get("ACTUAL_PRODCT_ID")==null ? "" :   prods.get("ACTUAL_PRODCT_ID").toString();
			String subProductId = prods.get("ACTUAL_SUB_PRODCT_ID")==null ? "" :   prods.get("ACTUAL_SUB_PRODCT_ID").toString();
			String childProdId = prods.get("ACTUAL_CHILD_PRODCT_ID")==null ? "" :   prods.get("ACTUAL_CHILD_PRODCT_ID").toString();
			String measurementId = prods.get("ACTUAL_MEASURMENT_ID")==null ? "" :   prods.get("ACTUAL_MEASURMENT_ID").toString();

			indentCreationBean.setProductId1(prodId);
			indentCreationBean.setSubProductId1(subProductId);
			indentCreationBean.setChildProductId1(childProdId);
			indentCreationBean.setUnitsOfMeasurementId1(measurementId);
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setRequiredQuantity1(prods.get("ACTUAL_QUANTITY")==null ? "" :   prods.get("ACTUAL_QUANTITY").toString());
			String employee = prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();
			String remarks = prods.get("REMARKS")==null ? "0" :   prods.get("REMARKS").toString();
			indentCreationBean.setRemarks1(employee+" : "+remarks);


			list.add(indentCreationBean);
		}	
		return list;

	}



	@Override
	public String getStateNameForTermsAndConditions(String site_id) {
		String Query = "select STATE from VENDOR_DETAILS where VENDOR_ID = '"+site_id+"'"; 
		String result = jdbcTemplate.queryForObject(Query, String.class);
		return result;

	}


	public int getPoInfinityNumberGenerator(String serviceState){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String infinityNumber = "select max(INFINITY_NUMBER) from SUMADHURA_HO_WISE_PO_NUMBER where SERVICE_NAME='"+serviceState+"'";

		int result = jdbcTemplate.queryForInt(infinityNumber);

		return result+1;
	}


	public int getPoYearWiseNumberGenerator(String serviceState){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String yearWiseNumber = "select max(YEARWISE_NUMBER) from SUMADHURA_HO_WISE_PO_NUMBER where SERVICE_NAME='"+serviceState+"'";

		int result = jdbcTemplate.queryForInt(yearWiseNumber);

		return result+1;
	}
	public int getUpdatePoNumberGeneratorHeadOfficeWise(int infinityNumber,int yearWiseNumber,String serviceName){

		String query = "update SUMADHURA_HO_WISE_PO_NUMBER set INFINITY_NUMBER = ?,YEARWISE_NUMBER=? where SERVICE_NAME = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				infinityNumber,yearWiseNumber,serviceName
		});
		return result;
	}
	/*public int getUpdatePoNumberGeneratorHeadOfficeWise(int infinityNumber,int yearWiseNumber,String serviceName){

		String query = "update SUM_HEAD_OFFICE_WISE_PO_NUMBER set INFINITY_NUMBER = ?,YEARWISE_NUMBER=? where SERVICE_NAME = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				infinityNumber,yearWiseNumber,"PO_TELANGANA"
		});
		return result;
	}*/

	/*	
	public int getUpdatePoNumberGeneratorForKarnataka(int infinityNumber,int yearWiseNumber){

		String query = "update SUM_HEAD_OFFICE_WISE_PO_NUMBER set INFINITY_NUMBER = ?,YEARWISE_NUMBER=? where SERVICE_NAME = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				infinityNumber,yearWiseNumber,"PO_KARNATAKA"
		});
		return result;
	}*/

	public int getUpdateStateWisePoNumber(int siteWise_Number,int stateYearWisePoNum,String strSiteId){

		String query = "update SUMADHURA_SITE_WISE_PO_NUMBER set SITE_WISE_NUMBER = ?,YEARWISE_NUMBER=? where SITE_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				siteWise_Number,stateYearWisePoNum,strSiteId
		});
		return result;
	}

	public int getStateWisePoNumber(String strSiteId){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String infinityNumber = "select max(SITE_WISE_NUMBER) from SUMADHURA_SITE_WISE_PO_NUMBER";

		int result = jdbcTemplate.queryForInt(infinityNumber);

		return result+1;
	}

	public int getStateWiseYearPo_Number(String SiteId){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String infinityNumber = "select max(YEARWISE_NUMBER) from SUMADHURA_SITE_WISE_PO_NUMBER";

		int result = jdbcTemplate.queryForInt(infinityNumber);

		return result+1;
	}

	public int getHeadOfficeInfinitMaxId(String poState){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";

		String intSeqNum = "select INFINITY_NUMBER from SUMADHURA_HO_WISE_PO_NUMBER where SERVICE_NAME='"+poState+"' ";

		int result = jdbcTemplate.queryForInt(intSeqNum);

		return result;
	}

	public String getSiteAddress(String siteId) {
		String query = "SELECT ADDRESS FROM VENDOR_DETAILS where VENDOR_ID ='"+siteId+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}




	public static void main(String [] args){
		Calendar cal = Calendar.getInstance();
		String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		String strFinacialYear = "";
		if(currentMonth <=3){
			strFinacialYear = (currentYear-1)+"-"+currentYearYY;
		}else{
			strFinacialYear = currentYear+"-"+(Integer.parseInt(currentYearYY)+1);
		}




		//System.out.println(strFinacialYear);

	}



	@Override
	public int getSiteWiseIndentNo(int indentNumber) {
		String query = "select SITEWISE_INDENT_NO from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ? ";
		int result = 0;
		try {
			result = jdbcTemplate.queryForInt(query, new Object[] {indentNumber});
		} catch (EmptyResultDataAccessException e) {
			result = 0;
		}
		return result;
	}
	
	public String getPoCreatedEmpName(String userId){
		List<Map<String, Object>> dbEmpDts = null;
		String strEmpName="";
		String strEmpEmailId="";
		String strMobiloeNumber="";
		String strResponse="";
		String query="select EMP_NAME,EMP_EMAIL,MOBILE_NUMBER from SUMADHURA_EMPLOYEE_DETAILS SED WHERE EMP_ID=? ";
		dbEmpDts = jdbcTemplate.queryForList(query, new Object[] {userId});
		for(Map<String, Object> prods : dbEmpDts) {
			strEmpName=prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();
			strEmpEmailId=prods.get("EMP_EMAIL")==null ? "" :   prods.get("EMP_EMAIL").toString();
			strMobiloeNumber=prods.get("MOBILE_NUMBER")==null ? "-" :   prods.get("MOBILE_NUMBER").toString();
			
			strResponse=strEmpName+","+strEmpEmailId+","+strMobiloeNumber;
			
		}
		
	return strResponse;
	
	}
	
	public String getPoCreatedEmpId(String tempPoNumber){
		String Query = "select PO_CREATE_APPROVE_EMP_ID from SUMADHURA_PO_CRT_APPRL_DTLS where OPERATION_TYPE='C' and TEMP_PO_NUMBER='"+tempPoNumber+"'"; 
		String result = jdbcTemplate.queryForObject(Query, String.class);
		return result;
		
	
		
	}
	
	public int productWiseInactiveInPurchaseTable(String indentCtreationDetailsId,String typeOfPurchase,String quantity) {
		String query = "UPDATE SUM_PURCHASE_DEPT_INDENT_PROSS set STATUS = 'I',TYPE_OF_PURCHASE='"+typeOfPurchase+"',CLOSED_INDENT_QUANT=CLOSED_INDENT_QUANT + '"+quantity+"' "
			+ " where INDENT_CREATION_DETAILS_ID =? ";
		int result = jdbcTemplate.update(query, new Object[] {indentCtreationDetailsId});
		return result;

	}
	
	
	public List<IndentCreationBean> purchasePrintIndent(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> IndentDts = null;
		int sno=0;
		String strCreateDate="";
		String strRemarks="";
		String query = "SELECT INDENT_CREATION_DETAILS_ID FROM SUMADHURA_INDENT_CREATION_DTLS WHERE INDENT_CREATION_ID =?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			String indentCreationDetailsId=prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString();
			
			String sql = "select (CP.NAME)AS CHILDPRODUCT,SP.NAME,(M.NAME)AS MEASURE,SICD.REMARKS,SPDIP.PENDING_QUANTIY,SICD.RECEIVE_QUANTITY,SICD.AVAIL_QUANTITY_AT_CREATION,SICD.REMARKS,SPDIP.TYPE_OF_PURCHASE "
						+" from SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,CHILD_PRODUCT CP,SUB_PRODUCT SP,MEASUREMENT M,SUMADHURA_INDENT_CREATION_DTLS SICD "
						+" where  SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND SPDIP.MEASUREMENT_ID=M.MEASUREMENT_ID "
						+" AND SICD.INDENT_CREATION_DETAILS_ID=SPDIP.INDENT_CREATION_DETAILS_ID AND SICD.INDENT_CREATION_DETAILS_ID=? ";

			IndentDts = jdbcTemplate.queryForList(sql, new Object[] {indentCreationDetailsId});
			for(Map<String, Object> prod : IndentDts) {
			//sno++;
			//indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			indentCreationBean.setSubProduct1(prod.get("NAME")==null ? "" :   prod.get("NAME").toString());
			indentCreationBean.setChildProduct1(prod.get("CHILDPRODUCT")==null ? "" :   prod.get("CHILDPRODUCT").toString());
			indentCreationBean.setUnitsOfMeasurement1(prod.get("MEASURE")==null ? "" :   prod.get("MEASURE").toString());
			String requiredQuantity=prod.get("PENDING_QUANTIY")==null ? "0" :   prod.get("PENDING_QUANTIY").toString();
			String productReceivedQuantity=prod.get("RECEIVE_QUANTITY")==null ? "0" :   prod.get("RECEIVE_QUANTITY").toString();
			String productAvailability=prod.get("AVAIL_QUANTITY_AT_CREATION")==null ? "0" :   prod.get("AVAIL_QUANTITY_AT_CREATION").toString();
			String remarks=prod.get("REMARKS")==null ? "0" :   prod.get("REMARKS").toString();
			String isSettled=prod.get("TYPE_OF_PURCHASE")==null ? "0" :   prod.get("TYPE_OF_PURCHASE").toString();
			
			if(!remarks.equalsIgnoreCase("column was created at the time of po creation")){
			double strRequiredQty=Double.parseDouble(requiredQuantity);
			double strProductAvail=Double.parseDouble(productAvailability);
			double strrequiredQuantity=0;
			strRequiredQty=strRequiredQty+Double.parseDouble(productReceivedQuantity);
			//double strRequiredQuantity=strRequiredQty;
			strrequiredQuantity=(strRequiredQty)+(strProductAvail);
			requiredQuantity=String.valueOf(strrequiredQuantity);
			
			
			indentCreationBean.setRequiredQuantity1(requiredQuantity);
			indentCreationBean.setProductAvailability(productAvailability);
			indentCreationBean.setOrderQuantity(Double.valueOf(strRequiredQty));
			strRemarks=prod.get("REMARKS")==null ? "" :   prod.get("REMARKS").toString();
			// print indent time it will display 1 and 2 like
			if(strRemarks.contains("@@@")){
				String strdetailedRemarks="";
				String strRemarksArr[] = strRemarks.split("@@@");
				int j=0;
				if(!strRemarksArr[0].equals("-")){
				for(int i =0 ; i< strRemarksArr.length;i++){
					if(!strRemarksArr [i].equals("") && !strRemarksArr [i].equals(" ")){
					strdetailedRemarks += " "+(j+1)+") "+strRemarksArr [i];
					j++;
					}

				}
				}
				strRemarks = strdetailedRemarks;
			}
			indentCreationBean.setRemarks1(strRemarks);
			IndentCreationBean icb = new IndentCreationBean();
			icb = cntlIndentrocss.getCreateAndRequiredDates(indentNumber);
			indentCreationBean.setStrRequiredDate(icb.getStrRequiredDate());
			strCreateDate = icb.getStrCreateDate();
			if(!isSettled.equalsIgnoreCase("SETTLED IN CENTRAL STORE")){
			sno++;
			indentCreationBean.setStrSerialNumber(String.valueOf(sno));
			list.add(indentCreationBean);
			}
	
			}	
		}	
		}
		return list;

	}

	@Override
	public int insertPurchaseDepttbl(int purchaseIndentProcessId,String sessionSiteId,String strUserId,ProductDetails productDetails)

	{



		
		String query = "INSERT INTO SUM_PURCHASE_DEPT_INDENT_PROSS(PURCHASE_DEPT_INDENT_PROSS_SEQ,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID, "
					+" PURCHASE_DEPT_REQ_QUANTITY, ALLOCATED_QUANTITY, PENDING_QUANTIY, PO_INTIATED_QUANTITY,STATUS,INDENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,"
					+ " INDENT_CREATION_DETAILS_ID,INDENT_REQ_QUANTITY,CLOSED_INDENT_QUANT,MATERIAL_GROUP_ID)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?,?,?) ";
		int result = jdbcTemplate.update(query, new Object[] {
				purchaseIndentProcessId,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),"0","0",productDetails.getQuantity(),"I",sessionSiteId,
				strUserId,productDetails.getIndentCreationDetailsId(),productDetails.getQuantity(),"0",productDetails.getGroupId()

		});




		return result;
	}

	@Override
	public int insertIndentCreationtbl(ProductDetails productDetails,String indentCreationId,String isSiteLevelPo) {
		int result =0;
		if(isSiteLevelPo.equalsIgnoreCase("true") || !isSiteLevelPo.equalsIgnoreCase("PURCHASE_DEPT")){
			String query = "INSERT INTO SUMADHURA_INDENT_CREATION_DTLS(INDENT_CREATION_DETAILS_ID,INDENT_CREATION_ID"+
			",PRODUCT_ID  , SUB_PRODUCT_ID ,CHILD_PRODUCT_ID ,MEASUREMENT_ID ,REQ_QUANTITY "+
			",priority,RECEIVE_QUANTITY,AVAIL_QUANTITY_AT_CREATION,CREATED_BY) "+
			"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";
			 result = jdbcTemplate.update(query, new Object[] {
					productDetails.getIndentCreationDetailsId(),indentCreationId,
					productDetails.getProductId(),productDetails.getSub_ProductId(),
					productDetails.getChild_ProductId(),productDetails.getMeasurementId(),productDetails.getQuantity(),
					
					null,"0","0",productDetails.getCreatedBy()
					
			});
			
		}else{
		String query = "INSERT INTO SUMADHURA_INDENT_CREATION_DTLS(INDENT_CREATION_DETAILS_ID,INDENT_CREATION_ID"+
		",PRODUCT_ID  , SUB_PRODUCT_ID ,CHILD_PRODUCT_ID ,MEASUREMENT_ID ,REQ_QUANTITY "+
		",priority,RECEIVE_QUANTITY,AVAIL_QUANTITY_AT_CREATION,REMARKS,CREATED_BY) "+
		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ,? )";
		 result = jdbcTemplate.update(query, new Object[] {
				productDetails.getIndentCreationDetailsId(),indentCreationId,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),productDetails.getQuantity(),
				
				null,"0","0","column was created at the time of po creation",productDetails.getCreatedBy()
				
		});
		}
		return result;
	}
	
	//get previous employee id
	public String  getpendingEmpId(String poNumber,String  user_Id)
	{
		List<Map<String, Object>> empList = null;
		String empId="";
		String query="select PO_CREATE_APPROVE_EMP_ID FROM SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD where TEMP_PO_NUMBER='"+poNumber+"' "
						+" and OPERATION_TYPE  not in('CAN') order by PO_CREATION_APPROVAL_DTLS_ID  DESC";
		
		empList = jdbcTemplate.queryForList(query, new Object[] {});
		if(empList!= null){
			for(Map<String, Object> emp : empList) {


				empId=emp.get("PO_CREATE_APPROVE_EMP_ID").toString();
				
			/*********************************************operation for approval employee ids*******************************/
				if(user_Id.equals(empId)){
					
					continue;
				}else{
				break;
				}
				
			}
		}
		
		//String empId = jdbcTemplate.queryForObject(query, String.class);
		
	return empId;
	
	}
	
	public String  getpendingUserId(String temp_Po_Number)
	{
		String query="select PO_ENTRY_USER_ID from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER='"+temp_Po_Number+"' ";
		String userId = jdbcTemplate.queryForObject(query, String.class);
		
	return userId;
	
	}

	// update temp po table set cancel to status
	public int updateEmpId(String pendingEmpId, String temp_Po_Number) {

		String query = "update SUMADHURA_TEMP_PO_ENTRY set TEMP_PO_PENDING_EMP_ID ='" + pendingEmpId
				+ "',VIEWORCANCEL='CANCEL' , PASSWORD_MAIL=? where PO_NUMBER='" + temp_Po_Number + "' ";
		int result = jdbcTemplate.update(query, new Object[] {"0"});

		return result;

	}

	public List<ProductDetails> getListOfCancelPo(String userId,String siteId) {

		List<ProductDetails> poList = new ArrayList<ProductDetails>();
		ProductDetails indentObj=null;
		List<Map<String, Object>> dbIndentDts = null;
		String type_Of_Purchase="";
		String old_Po_Number="";
		String PODate="";
		String sql="";
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		if(siteId.equals(marketingDeptId)){
			sql="select STPE.PO_NUMBER,STPE.PO_DATE,STPE.SITE_ID,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,"
				+" STPE.OLD_PO_NUMBER from SUMADHURA_TEMP_PO_ENTRY STPE where VIEWORCANCEL='CANCEL' AND TEMP_PO_PENDING_EMP_ID=? and PO_STATUS='A'";
		}else{
			sql="select STPE.PO_NUMBER,STPE.PO_DATE,STPE.SITE_ID,SIC.SITEWISE_INDENT_NO,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.OLD_PO_NUMBER from SUMADHURA_TEMP_PO_ENTRY STPE,"
				+" SUMADHURA_INDENT_CREATION SIC where VIEWORCANCEL='CANCEL' AND TEMP_PO_PENDING_EMP_ID= ? and PO_STATUS='A' AND STPE.INDENT_NO=SIC.INDENT_CREATION_ID ";
		}
		dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {userId});
		for(Map<String, Object> prods : dbIndentDts) {
			indentObj = new ProductDetails();

			indentObj.setStrPONumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
			indentObj.setEdit_Po_Number(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());//revised po purpose
			//	indentObj.setStrInvoiceDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
			PODate=prods.get("PO_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("PO_DATE").toString();
			
			indentObj.setSite_Id(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
			indentObj.setIndentNo((prods.get("INDENT_NO")==null ? "" : prods.get("INDENT_NO").toString()));
			if(!siteId.equals(marketingDeptId)){
			indentObj.setSiteWiseIndentNo((prods.get("SITEWISE_INDENT_NO")==null ? "0" : prods.get("SITEWISE_INDENT_NO").toString()));
			}else{
				indentObj.setSiteWiseIndentNo("-");	
			}
			//indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" : prods.get("SITEWISE_INDENT_NO").toString()));
			type_Of_Purchase=(prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString());
			old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
			
			Date modifiedDate = null;
			try {
			modifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(PODate);
			}catch (ParseException e) {
				e.printStackTrace();
			}
			PODate = new SimpleDateFormat("dd-MM-yyyy").format(modifiedDate);
			indentObj.setPoDate(PODate);
			//indentObj.setType_Of_Purchase
			if(type_Of_Purchase.equals("") && !old_Po_Number.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && !old_Po_Number.equals(""))){
				
				indentObj.setType_Of_purchase("REVISED PO");
			}
			else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && old_Po_Number.equals(""))){
				
				indentObj.setType_Of_purchase("PURCHASE_DEPT_PO");
			} else if((type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !old_Po_Number.equals("") && old_Po_Number.startsWith("PO/SIPL"))){
				
				indentObj.setType_Of_purchase("MARKETING REVISED PO ");
			}else if((type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !old_Po_Number.startsWith("PO/SIPL") || (old_Po_Number.equals("") && type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT")))){
				
				indentObj.setType_Of_purchase("MARKETING PO");
			} else{
				
				indentObj.setType_Of_purchase("SITELEVEL PO");
			}
			poList.add(indentObj);

		}

		return poList;

	}
	
	@Override
	public List<ProductDetails> getViewCancelPoDetails(String poNumber, String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbPODts = null;

		String query = "select VD.VENDOR_ID,VD.VENDOR_NAME,VD.GSIN_NUMBER,VD.ADDRESS,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,SPE.PO_DATE,S.SITE_ID,S.SITE_NAME,SPE.PREPARED_BY,SPE.PASSWORD_MAIL,SPE.DELIVERY_DATE,SPE.OLD_PO_NUMBER,SPE.PAYMENT_REQ_DAYS,SPE.TOTAL_AMOUNT "
			+ " from SUMADHURA_TEMP_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID "
			+ " AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' and SIC.INDENT_CREATION_ID = SPE.INDENT_NO";
		dbPODts = template.queryForList(query, new Object[]{});

		for(Map<String, Object> prods : dbPODts) {
			ProductDetails pd = new ProductDetails();
			pd.setVendorId(prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString());
			pd.setEdit_Po_Number(prods.get("OLD_PO_NUMBER") == null ? "" : prods.get("OLD_PO_NUMBER").toString());
			pd.setVendorName(prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString());
			pd.setStrGSTINNumber(prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString());
			pd.setVendorAddress(prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString());
			pd.setIndentNo(prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString());
			pd.setSiteWiseIndentNo(prods.get("SITEWISE_INDENT_NO") == null ? "0" : prods.get("SITEWISE_INDENT_NO").toString());
			pd.setSite_Id(prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString());
			pd.setSiteName(prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString());
			pd.setType_Of_purchase(prods.get("PREPARED_BY") == null ? "" : prods.get("PREPARED_BY").toString());
			pd.setPasswdForMail(prods.get("PASSWORD_MAIL") == null ? "" : prods.get("PASSWORD_MAIL").toString());
			pd.setPayment_Req_days(prods.get("PAYMENT_REQ_DAYS") == null ? "" : prods.get("PAYMENT_REQ_DAYS").toString());
			pd.setFinalamtdiv(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
			
			String strDeliveryDate = prods.get("DELIVERY_DATE") == null ? "" : prods.get("DELIVERY_DATE").toString();
			String poDate = prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
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
		return list;
	}
	@Override
	public List<ProductDetails> getProductDetailsListsForCancelPo(String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbPODts = null;
		//double doublePOTotalAmount = 0;
		//double doubleTotalAmount = 0;
		String basicAmount="";
		String amountAfterTax="";
		String taxAmount="";
		String price="";
		String quantity="";
		String amountAfterDiscount="";
		String totalAmount="";

		String query = "SELECT P.PRODUCT_ID,SP.SUB_PRODUCT_ID,CP.CHILD_PRODUCT_ID,MST.MEASUREMENT_ID,"
		+ " P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,SPED.VENDOR_PRODUCT_DESC,"
		+ " SPED.PO_QTY,SPED.PRICE,SPED.BASIC_AMOUNT,"
		+ " SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.HSN_CODE,SPED.TAX,SPED.TAX_AMOUNT,"
		+ " SPED.AMOUNT_AFTER_TAX,SPED.OTHER_CHARGES,SPED.TAX_ON_OTHER_TRANSPORT_CHG,"
		+ " SPED.OTHER_CHARGES_AFTER_TAX,SPED.TOTAL_AMOUNT,SPED.INDENT_CREATION_DTLS_ID," +
		   "SPED.PO_ENTRY_DETAILS_ID,TAX_PERCENTAGE "
		+ " FROM SUMADHURA_TEMP_PO_ENTRY_DTLS SPED,SUMADHURA_TEMP_PO_ENTRY SPE,"
		+ " PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST,INDENT_GST INGST WHERE SPED.PRODUCT_ID = P.PRODUCT_ID "
		+ " AND SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID AND "
		+ " SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND "
		+ " SPED.MEASUR_MNT_ID = MST.MEASUREMENT_ID AND  INGST.TAX_ID = SPED.TAX and"
		+ " SPED.PO_ENTRY_ID = SPE.PO_ENTRY_ID AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' order by SPED.INDENT_CREATION_DTLS_ID " ;
		//+ "AND PO_STATUS = 'A'";
	
		/*String query ="SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"
					+" SPDIP.PRODUCT_ID,SPDIP.SUB_PRODUCT_ID,SPDIP.CHILD_PRODUCT_ID,SPDIP.MEASUREMENT_ID,SPED.PO_QTY,SPED.PRICE,SPED.BASIC_AMOUNT,"
					+" SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.HSN_CODE,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX,SPED.OTHER_CHARGES,SPED.TAX_ON_OTHER_TRANSPORT_CHG,"
					+" SPED.OTHER_CHARGES_AFTER_TAX,SPED.TOTAL_AMOUNT,SPED.INDENT_CREATION_DTLS_ID,SPED.PO_ENTRY_DETAILS_ID, "
					+"(SELECT INGST.TAX_PERCENTAGE FROM INDENT_GST INGST WHERE INGST.TAX_ID = SPED.TAX) AS TAX_PERCENTAGE "
					+" FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_INDENT_CREATION_DTLS SICD,"
					+" PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST,SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP "
					+" left outer join SUMADHURA_TEMP_PO_ENTRY_DTLS SPED on SPED.INDENT_CREATION_DTLS_ID=SPDIP.INDENT_CREATION_DETAILS_ID"
					+" WHERE SPDIP.PRODUCT_ID=P.PRODUCT_ID AND SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID" 
					+" AND SPDIP.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  and SPDIP.STATUS='A' " 
					+" AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID AND SIC.INDENT_CREATION_ID ='"+poNumber+"' and SIC. SITE_ID ='"+reqSiteId+"' order by SICD.INDENT_CREATION_DETAILS_ID";
				*/
			
				
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
			 
			 basicAmount=prods.get("BASIC_AMOUNT") == null ? "0" : prods.get("BASIC_AMOUNT").toString();
			 amountAfterTax=prods.get("AMOUNT_AFTER_TAX") == null ? "0" : prods.get("AMOUNT_AFTER_TAX").toString();
			 taxAmount=prods.get("TAX_AMOUNT") == null ? "0" : prods.get("TAX_AMOUNT").toString();
			 price=prods.get("PRICE") == null ? "0" : prods.get("PRICE").toString();
			 quantity=prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString();
			 amountAfterDiscount=prods.get("AMOUNT_AFTER_DISCOUNT") == null ? "0" : prods.get("AMOUNT_AFTER_DISCOUNT").toString();
			 totalAmount=prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString();
			
			pd.setRequiredQuantity(String.format("%.2f",Double.valueOf(quantity)));
			pd.setPrice(String.format("%.2f",Double.valueOf(price)));
			pd.setBasicAmt(String.format("%.2f",Double.valueOf(basicAmount)));
			pd.setTaxAmount(String.format("%.2f",Double.valueOf(taxAmount)));
			pd.setAmountAfterTax(String.format("%.2f",Double.valueOf(amountAfterTax)));
			pd.setStrAmtAfterDiscount(String.format("%.2f",Double.valueOf(amountAfterDiscount)));
			pd.setTotalAmount(String.format("%.2f",Double.valueOf(totalAmount)));
			
			pd.setStrDiscount(prods.get("DISCOUNT") == null ? "" : prods.get("DISCOUNT").toString());
			
			pd.setHsnCode(prods.get("HSN_CODE") == null ? "" : prods.get("HSN_CODE").toString());
			pd.setChildProductCustDisc(prods.get("VENDOR_PRODUCT_DESC") == null ? "-" : prods.get("VENDOR_PRODUCT_DESC").toString());
			String taxId = prods.get("TAX") == null ? " " : prods.get("TAX").toString();
			//String query1 = "select TAX_PERCENTAGE from INDENT_GST where TAX_ID = "+taxId+ " ";
		//	String taxValue = template.queryForObject(query1,String.class);
			String taxValue = prods.get("TAX_PERCENTAGE") == null ? " " : prods.get("TAX_PERCENTAGE").toString();
			
			
			pd.setTaxId(taxId);
			pd.setTax(taxValue);
			
			pd.setOthercharges1(prods.get("OTHER_CHARGES") == null ? "" : prods.get("OTHER_CHARGES").toString());
			pd.setTaxonothertranportcharge1(prods.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
			pd.setOtherchargesaftertax1(prods.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
			
			
			
			pd.setIndentCreationDetailsId(prods.get("INDENT_CREATION_DTLS_ID") == null ? "" : prods.get("INDENT_CREATION_DTLS_ID").toString());
			pd.setPoEntryDetailsId(prods.get("PO_ENTRY_DETAILS_ID") == null ? "" : prods.get("PO_ENTRY_DETAILS_ID").toString());
			
		//	doubleTotalAmount = Double.valueOf(prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString());
		//	doublePOTotalAmount = doublePOTotalAmount+doubleTotalAmount;
			
			
			
			list.add(pd);
		}
		
		
	
		return list;
	}
	
	@Override
	public List<ProductDetails> getTransChrgsDtlsForCancelPo(String poNumber,String reqSiteId) {
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbTransDts = null;
		String trasportAmount="";
		String trasportGstAmount="";
		String totalAmountAfterTax="";

		String query = "select SPTOCD.ID,SPTOCD.TRANSPORT_ID,STOCM.CHARGE_NAME,SPTOCD.TRANSPORT_GST_PERCENTAGE,IG.TAX_PERCENTAGE,SPTOCD.TRANSPORT_GST_AMOUNT,"
			+ " SPTOCD.TOTAL_AMOUNT_AFTER_GST_TAX,SPTOCD.TRANSPORT_AMOUNT from SUMADHURA_TEMP_PO_TRNS_O_CHRGS  SPTOCD, "
			+ " SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM,INDENT_GST IG,SUMADHURA_TEMP_PO_ENTRY SPE where  SPTOCD.TRANSPORT_ID = STOCM.CHARGE_ID and "
			+ " IG.TAX_ID = SPTOCD.TRANSPORT_GST_PERCENTAGE and SPE.PO_ENTRY_ID = SPTOCD.PO_NUMBER and SPE.PO_NUMBER = ? and SPE.SITE_ID = ? ";
		dbTransDts = template.queryForList(query, new Object[]{poNumber,reqSiteId});
		int sno = 0;
		for(Map<String, Object> prods : dbTransDts) {
			ProductDetails pd = new ProductDetails();
			sno++;
			pd.setStrSerialNumber(String.valueOf(sno));
			pd.setConveyanceId1(prods.get("TRANSPORT_ID") == null ? "" : prods.get("TRANSPORT_ID").toString());
			pd.setConveyance1(prods.get("CHARGE_NAME") == null ? "" : prods.get("CHARGE_NAME").toString());
			trasportAmount=prods.get("TRANSPORT_AMOUNT") == null ? "" : prods.get("TRANSPORT_AMOUNT").toString();
			trasportGstAmount=prods.get("TRANSPORT_GST_AMOUNT") == null ? "" : prods.get("TRANSPORT_GST_AMOUNT").toString();
			totalAmountAfterTax=prods.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : prods.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
			pd.setConveyanceAmount1(String.format("%.2f",Double.valueOf(trasportAmount)));
			pd.setGSTTaxId1(prods.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : prods.get("TRANSPORT_GST_PERCENTAGE").toString());
			pd.setGSTTax1(prods.get("TAX_PERCENTAGE") == null ? "" : prods.get("TAX_PERCENTAGE").toString());
			pd.setGSTAmount1(String.format("%.2f",Double.valueOf(trasportGstAmount)));
			pd.setAmountAfterTaxx1(String.format("%.2f",Double.valueOf(totalAmountAfterTax)));
			pd.setPoTransChrgsDtlsSeqNo(prods.get("ID") == null ? "" : prods.get("ID").toString());

			list.add(pd);
		}
		return list;

	}
	
	@Override
	public int getPoEnterSeqNoByTempPONumber(String poNumber,String toSite) {
		String query  = "select PO_ENTRY_ID from  SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER = ? and SITE_ID = ? ";
		return jdbcTemplate.queryForInt(query, new Object[] {poNumber,toSite});

	}

	@Override
	public int updateTempPOEntryDetails(ProductDetails productDetails)
	{

		String query =  "update SUMADHURA_TEMP_PO_ENTRY_DTLS set VENDOR_PRODUCT_DESC=? ,PD_PRODUCT_DESC=?, PRODUCT_ID = ? ,SUB_PRODUCT_ID = ? ,CHILD_PRODUCT_ID = ? ,MEASUR_MNT_ID = ? ,PO_QTY = ? ,PRICE = ? ,BASIC_AMOUNT = ? ,DISCOUNT = ? ,AMOUNT_AFTER_DISCOUNT = ? ,TAX = ? ,TAX_AMOUNT = ? ,AMOUNT_AFTER_TAX = ? ,HSN_CODE = ? ,OTHER_CHARGES = ? ,TAX_ON_OTHER_TRANSPORT_CHG = ? ,OTHER_CHARGES_AFTER_TAX = ? ,TOTAL_AMOUNT = ? where PO_ENTRY_DETAILS_ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getChildProductCustDisc(),
				productDetails.getChild_ProductName(),
				productDetails.getProductId(),
				productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),
				productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getTax(),productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),
				productDetails.getHsnCode(),
				productDetails.getOtherOrTransportCharges1(),productDetails.getTaxOnOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),
				productDetails.getPoEntryDetailsId()


		});
		return result;
	}
	
	@Override
	public int insertTempPOTransportDetails(int poTransChrgsSeqNo, ProductDetails productDetails, TransportChargesDto transportChargesDto,String poNumber) {
		String query = "INSERT INTO SUMADHURA_TEMP_PO_TRNS_O_CHRGS(ID,TRANSPORT_ID,TRANSPORT_GST_PERCENTAGE,TRANSPORT_GST_AMOUNT,"
			+ "TOTAL_AMOUNT_AFTER_GST_TAX,DATE_AND_TIME,TRANSPORT_AMOUNT,SITE_ID,PO_NUMBER,INDENT_NUMBER) "
			+ "values( ?, ?, ?, ?, ?, sysdate, ?, ?, ?, ?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poTransChrgsSeqNo,
				transportChargesDto.getTransportId(),
				transportChargesDto.getTransportGSTPercentage(),
				transportChargesDto.getTransportGSTAmount(),
				transportChargesDto.getTotalAmountAfterGSTTax(),
				transportChargesDto.getTransportAmount(),
				productDetails.getSite_Id(),
				poNumber,
				productDetails.getIndentNo()
		});
		return result;

	}
	
	@Override
	public int updateTempPOTransportChargesDetails(TransportChargesDto transportChargesDto,int id){

		String query =  "update SUMADHURA_TEMP_PO_TRNS_O_CHRGS set TRANSPORT_ID = ? ,TRANSPORT_AMOUNT = ? ,"
			+ " TRANSPORT_GST_PERCENTAGE = ? ,TRANSPORT_GST_AMOUNT = ? ,TOTAL_AMOUNT_AFTER_GST_TAX = ? "
			+ " where ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {

				transportChargesDto.getTransportId(),
				transportChargesDto.getTransportAmount(),
				transportChargesDto.getTransportGSTPercentage(),
				transportChargesDto.getTransportGSTAmount(),
				transportChargesDto.getTotalAmountAfterGSTTax(),
				id
		});


		return result;
	}
	
	@Override
	public int deleteTempPOTransportChargesDetails(int poTransChrgsDtlsSeqNo) {
		String query =  "DELETE FROM SUMADHURA_TEMP_PO_TRNS_O_CHRGS WHERE ID = ? ";
		return jdbcTemplate.update(query, new Object[] {poTransChrgsDtlsSeqNo});
	}
	
	public int deleteTempPOEntryDetails(String poEntryDetailsId) {
		String query =  "DELETE FROM SUMADHURA_TEMP_PO_ENTRY_DTLS WHERE PO_ENTRY_DETAILS_ID = ? ";
		return jdbcTemplate.update(query, new Object[] {poEntryDetailsId});
	}
	
	@Override
	public int updateTempPOQuantityDetails(String indentCreationDetailsId, String quantity,String strQuantity) {
		
		double tempQuan=0.0;
		tempQuan=Double.valueOf(strQuantity)-Double.valueOf(quantity);
		BigDecimal bigDecimaltemp_Quantity = new BigDecimal(tempQuan);
		tempQuan=Double.valueOf(String.valueOf(bigDecimaltemp_Quantity.setScale(2,RoundingMode.CEILING)));
		
		if(tempQuan<=0){
			
			tempQuan=Math.abs(tempQuan);
			tempQuan=Double.parseDouble(new DecimalFormat("##.##").format(Math.abs(tempQuan)));
			String query =  "update SUM_PURCHASE_DEPT_INDENT_PROSS set PO_INTIATED_QUANTITY = PO_INTIATED_QUANTITY+?  where INDENT_CREATION_DETAILS_ID = ? ";
			return jdbcTemplate.update(query, new Object[] {tempQuan,indentCreationDetailsId});
			
		}else{
			String query =  "update SUM_PURCHASE_DEPT_INDENT_PROSS set PO_INTIATED_QUANTITY = PO_INTIATED_QUANTITY-?,STATUS='A'  where INDENT_CREATION_DETAILS_ID = ? ";
			tempQuan=Double.parseDouble(new DecimalFormat("##.##").format(Math.abs(tempQuan)));
			return jdbcTemplate.update(query, new Object[] {tempQuan,indentCreationDetailsId});
	
		}
		
		
		}
	
	
	public int updateTempPOVendorDetails(String vendorId,String poNumber,String ccEmailId,String subject,String isUpdate,String strDeliveryDate,String payment_Req_Days)
	{
	
		int result =0;
		if(isUpdate.equalsIgnoreCase("true")){
		String query =  "update SUMADHURA_PO_ENTRY set VENDOR_ID=?, DELIVERY_DATE=? where PO_STATUS='A' and PO_NUMBER = ?";

		 result = jdbcTemplate.update(query, new Object[] {vendorId,DateUtil.convertToJavaDateFormat(strDeliveryDate),poNumber
		
					});
		}else{
		
			String query =  "update SUMADHURA_TEMP_PO_ENTRY set VENDOR_ID=?,CC_EMAIL_ID=?,SUBJECT=?,PAYMENT_REQ_DAYS=? where PO_STATUS='A' and PO_NUMBER = ?";

			 result = jdbcTemplate.update(query, new Object[] {vendorId,ccEmailId,subject,payment_Req_Days,poNumber
			 });
			
		}
		return result;
	}
	
	public int updateTempPoVieworCancel(String temp_Po_Number,String siteId)
	{

		String query =  "update SUMADHURA_TEMP_PO_ENTRY set VIEWORCANCEL ='MODIFIED' WHERE VIEWORCANCEL='CANCEL' AND PO_NUMBER='"+temp_Po_Number+"' AND SITE_ID= '"+siteId+"'";
		int result = jdbcTemplate.update(query, new Object[] {});
	
	return result;
	
}
	
	public String tempPoSubProducts(String prodId, String indentNumber, String reqSiteId) {
		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;	

		log.debug("Product Id = "+prodId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT DISTINCT(SPDIP.SUB_PRODUCT_ID), SP.NAME FROM SUB_PRODUCT SP,SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,SUMADHURA_INDENT_CREATION SIC,"
							+" SUMADHURA_INDENT_CREATION_DTLS SICD WHERE SPDIP.STATUS = 'A' AND SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SIC.INDENT_CREATION_ID ='"+indentNumber+"' AND SIC. SITE_ID ='"+reqSiteId+"'"
							+" and SPDIP.PRODUCT_ID='"+prodId+"' AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{});
		
		System.out.println("the list is"+dbSubProductsList);

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
		}		
		return sb.toString();
	}
	
	
	public String tempPoChildProducts(String subProdId, String indentNumber, String reqSiteId) {
		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;	

		log.debug("Product Id = "+subProdId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT SPDIP.CHILD_PRODUCT_ID,CP.NAME,CP.MATERIAL_GROUP_ID,SPDIP.PURCHASE_DEPT_REQ_QUANTITY,SPDIP.PO_INTIATED_QUANTITY,SICD.INDENT_CREATION_DETAILS_ID,(SPDIP.PENDING_QUANTIY-PO_INTIATED_QUANTITY) as PENDING_QUANTIY"
							+" FROM CHILD_PRODUCT CP,SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,SUMADHURA_INDENT_CREATION SIC,"
							+" SUMADHURA_INDENT_CREATION_DTLS SICD WHERE SPDIP.STATUS = 'A' AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID" 
							+" AND SIC.INDENT_CREATION_ID ='"+indentNumber+"' AND SIC. SITE_ID ='"+reqSiteId+"' AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID and SPDIP.SUB_PRODUCT_ID='"+subProdId+"' AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{});

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("CHILD_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|"+String.valueOf(subProds.get("PURCHASE_DEPT_REQ_QUANTITY"))
							+"|"+String.valueOf(subProds.get("PO_INTIATED_QUANTITY"))+"|"+String.valueOf(subProds.get("INDENT_CREATION_DETAILS_ID"))
							+"|"+String.valueOf(subProds.get("PENDING_QUANTIY"))+"|"+String.valueOf(subProds.get("MATERIAL_GROUP_ID")==null ? "0" : subProds.get("MATERIAL_GROUP_ID"))+"|");
		}		
		return sb.toString();
	}
	
	@Override
	public int insertTempPOEntryDetails(ProductDetails productDetails,int poEntrySeqNo)
	{
		String query = "INSERT INTO SUMADHURA_TEMP_PO_ENTRY_DTLS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
		"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,INDENT_CREATION_DTLS_ID,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC"
		+ ") values(SUMADHURA_T_PO_ENTRY_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				poEntrySeqNo,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),
				productDetails.getOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),productDetails.getHsnCode(),productDetails.getTaxOnOtherOrTransportCharges1(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getIndentCreationDetailsId(),productDetails.getChildProductCustDisc(),productDetails.getChild_ProductName()

		});
		return result;
	}
	
	public String  getPoInitiateQuan(String indentCreationDetailsId)
	{
		List<Map<String, Object>> productList = null;

		
		
		String  poinitiated_Quan="";
		String  request_Quan="";
		String response="";
		String query = "select PO_INTIATED_QUANTITY,PURCHASE_DEPT_REQ_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS WHERE STATUS='A' AND INDENT_CREATION_DETAILS_ID= ?";

		productList = jdbcTemplate.queryForList(query, new Object[] {indentCreationDetailsId});
		try{

			if(productList!= null){
				for(Map<String, Object> list : productList) {


					poinitiated_Quan=(list.get("PO_INTIATED_QUANTITY")== null ? "0" : list.get("PO_INTIATED_QUANTITY").toString());
					request_Quan=(list.get("PURCHASE_DEPT_REQ_QUANTITY")== null ? "0" : list.get("PURCHASE_DEPT_REQ_QUANTITY").toString());
					response=poinitiated_Quan+"|"+request_Quan;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			
		return response;
		}
	
	public List<ProductDetails> getTempTermsAndConditions(String poNumber,String isRevised,String reqSiteId){
		
		ProductDetails objProductDetails = null;
		List<Map<String, Object>> termList = null;
		List<String> listOfTermsAndConditions = new ArrayList<String>();
		List<ProductDetails> listTermsAndcondtion = new ArrayList<ProductDetails>();
		if(isRevised.equalsIgnoreCase("true")){
			
			int poEntrySeqNumber =getPoEnterSeqNoByPONumber(poNumber,reqSiteId
					);
			String sql1="SELECT TERMS_CONDITION_DESC,TERMS_CONDITION_ID FROM  SUMADHURA_PD_TERMS_CONDITIONS WHERE PO_ENTRY_ID=? order by TERMS_CONDITION_ID";
			termList = jdbcTemplate.queryForList(sql1, new Object[] {poEntrySeqNumber});
			
		}else{
		String sql1="SELECT TERMS_CONDITION_DESC,TERMS_CONDITION_ID FROM  SUMADHURA_TEMP_PO_TERMS_COND WHERE PO_NUMBER=? order by TERMS_CONDITION_ID";
		termList = jdbcTemplate.queryForList(sql1, new Object[] {poNumber});
		}
	//System.out.println("the list size is "+termList.size());
	if (null != termList && termList.size() > 0) {
		for (Map<?, ?> GetTransportChargesDetails : termList) {
			objProductDetails = new ProductDetails();
			String termscondition = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();
			objProductDetails.setStrTermsConditionName(termscondition);
			
			listTermsAndcondtion.add(objProductDetails); 

			//listOfTermsAndConditions.add(String.valueOf(termscondition));
		}
	}
	System.out.println("the conditions size"+listTermsAndcondtion.size());

	return listTermsAndcondtion;
	}
	
	public int deleteTemppoTermsAdnConditions(String poNumber,String isUpdated) {
		String query ="";
		if(isUpdated.equalsIgnoreCase("true")){
		 query =  "DELETE FROM SUMADHURA_PD_TERMS_CONDITIONS WHERE PO_ENTRY_ID = ? ";
			
		}else{
		 query =  "DELETE FROM SUMADHURA_TEMP_PO_TERMS_COND WHERE PO_NUMBER = ? ";
		
		}
		return jdbcTemplate.update(query, new Object[] {poNumber});
		
	}
	// getting the comments when the user cancel time 
	public String  getCancelPoComments(String  poNumber) {


		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String StrCancelPoComments = "";

		String query = "select PURPOSE,SED.EMP_NAME from SUMADHURA_PO_CRT_APPRL_DTLS SPCAD , SUMADHURA_EMPLOYEE_DETAILS SED "
				+ " where SED.EMP_ID = SPCAD.PO_CREATE_APPROVE_EMP_ID and TEMP_PO_NUMBER = ? AND SPCAD.OPERATION_TYPE='CAN'";
		getcommentDtls = jdbcTemplate.queryForList(query, new Object[] {poNumber});

		if(getcommentDtls != null && getcommentDtls.size() > 0){
			for(Map<String, Object> prods : getcommentDtls) {

				strEmployeName = prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString();
				strComments = prods.get("PURPOSE")==null ? "" : prods.get("PURPOSE").toString();
				
				if((strEmployeName!=null && strComments!=null) && (!strEmployeName.equals("") && !strComments.equals(""))){
				
					StrCancelPoComments +=  strEmployeName + " :  "+strComments +"   ,";
				}

			}
			if(StrCancelPoComments!=null && !StrCancelPoComments.equals("")){
				StrCancelPoComments =  StrCancelPoComments.substring(0,StrCancelPoComments.length()-1);
			}
		
		}

		return StrCancelPoComments;

	}
	
	@Override
	public String getTempPOSubject(String poNumber) {
		
		String result="";
		
	String Query = "select SUBJECT from SUMADHURA_TEMP_PO_ENTRY WHERE PO_NUMBER='"+poNumber+"'"; 
		 result = jdbcTemplate.queryForObject(Query, String.class);
		
			return result;

	}
	@Override	
public String getTempPoCCEmails(String poNumber) {
		
		String value="";
		
	String Query = "select CC_EMAIL_ID from SUMADHURA_TEMP_PO_ENTRY WHERE PO_NUMBER='"+poNumber+"'"; 
		 value = jdbcTemplate.queryForObject(Query, String.class);
		
			return value;

	}
	@Override
	public List<String> getRevisionNumber(String editPoNumber){

		//String intSeqNum = "select  SUMADHURA_PO_ENTRY_SEQ.nextval from dual";
		List<Map<String, Object>> dbEmpDts = null;
		List<String> listOfDetails = new ArrayList<String>();
		String revisionNo="";
		String poEntryId="";
		String preparedBy="";
		//String strRevisionPOEntryId="";
		String sql = "select REVISION,PO_ENTRY_ID,PREPARED_BY from SUMADHURA_PO_ENTRY where PO_NUMBER=?";
		dbEmpDts = jdbcTemplate.queryForList(sql, new Object[] {editPoNumber});
		for (Map<String, Object> prods : dbEmpDts) {
			revisionNo = prods.get("REVISION") == null ? "0" : prods.get("REVISION").toString();
			poEntryId = prods.get("PO_ENTRY_ID") == null ? "0" : prods.get("PO_ENTRY_ID").toString();
			preparedBy=prods.get("PREPARED_BY") == null ? "0" : prods.get("PREPARED_BY").toString();
			//strRevisionPOEntryId=revisionNo+"@@@"+poEntryId;
		}
		listOfDetails.add(revisionNo);
		listOfDetails.add(poEntryId);
		listOfDetails.add(preparedBy);
		//int result = jdbcTemplate.queryForInt(intSeqNum, new Object[] {editPoNumber});

		return listOfDetails;
	}
	public String inactiveOldPo(String old_Po_Number,String isApproveOrNot)
	{
		List<Map<String, Object>> dbEmpDts = null;
		String po_Date = "";
		Date poReffDate;
		String convertedPoDate="";
		String query ="";
		if(!isApproveOrNot.equalsIgnoreCase("true")){
		 query =  "update SUMADHURA_PO_ENTRY set PO_STATUS ='REVISED' where PO_NUMBER ='"+old_Po_Number+"' ";
		}else{query =  "update SUMADHURA_PO_ENTRY set PO_STATUS ='I' where PO_NUMBER ='"+old_Po_Number+"' ";}
		int result = jdbcTemplate.update(query, new Object[] {});
		if(result>0){
			String sql = "select PO_DATE FROM SUMADHURA_PO_ENTRY WHERE PO_NUMBER=?";
		dbEmpDts = jdbcTemplate.queryForList(sql, new Object[] {old_Po_Number});
		for (Map<String, Object> prods : dbEmpDts) {
			po_Date = prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString();
		}
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			poReffDate = inputFormat.parse(po_Date);
			convertedPoDate = outputFormat.format(poReffDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return convertedPoDate;
	}
	@Override
	public double getRequestedQuantityInPurchaseTable(String indentCreationDetailsId) {
		String query = "select PURCHASE_DEPT_REQ_QUANTITY from SUM_PURCHASE_DEPT_INDENT_PROSS where INDENT_CREATION_DETAILS_ID = '"+indentCreationDetailsId+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return Double.valueOf(result);
	}
	// it is calling at the time of revised po total amount decreased 
	public int updateAccPayment(String old_Po_Number,String new_Po_Number,String totalAmt,boolean isUpdate)
	{
		int result =0;
		int response=0;
		int intResult=0;
		String query ="update ACC_PAYMENT set PO_NUMBER =?,PO_AMOUNT=?,PO_DATE=sysdate where PO_NUMBER =? ";
		result = jdbcTemplate.update(query, new Object[] {new_Po_Number,totalAmt,old_Po_Number});
		intResult=intResult+result;
		String sql ="update ACC_ADVANCE_PAYMENT_PO set PO_NUMBER =?,PO_AMOUNT=? where PO_NUMBER =? ";
		result = jdbcTemplate.update(sql, new Object[] {new_Po_Number,totalAmt,old_Po_Number});
		intResult=intResult+result;
		//when the user revised po to save old_po_history table data in another table new table Acc_revised_po table
		if(!isUpdate){
		insertAndGetDataPOHistory(old_Po_Number);}
		String sql1 ="update ACC_PO_HISTORY set PO_NUMBER =?,PO_AMOUNT=? where PO_NUMBER =?";
		result = jdbcTemplate.update(sql1, new Object[] {new_Po_Number,totalAmt,old_Po_Number});
		intResult=intResult+result;
		if(!isUpdate){
		insertAndGetDataInvoiceHistory(old_Po_Number);}
		//when the user revised po to save old_invoice_history table data in another table new table Acc_revised_invoice table
		String Query ="update ACC_INVOICE_HISTORY set REF_NO =? where REF_NO =? ";
		result = jdbcTemplate.update(Query, new Object[] {new_Po_Number,old_Po_Number});
		intResult=intResult+result;

		/*String indentPONumber ="update INDENT_ENTRY set PO_ID =? where PO_ID =? ";
		response = jdbcTemplate.update(indentPONumber, new Object[] {new_Po_Number,old_Po_Number});

		String dcPONumber ="update DC_ENTRY set PO_ID =? where PO_ID =? ";
		response = jdbcTemplate.update(dcPONumber, new Object[] {new_Po_Number,old_Po_Number});*/

		return intResult;
	}
	// update poNumber in dc entry or indent entry table 
	public int updateIndentAndDcPONumber(String old_Po_Number,String new_Po_Number,String totalAmt){
		int response=0;
		String indentPONumber ="update INDENT_ENTRY set PO_ID =? where PO_ID =? ";
		response = jdbcTemplate.update(indentPONumber, new Object[] {new_Po_Number,old_Po_Number});

		String dcPONumber ="update DC_ENTRY set PO_ID =? where PO_ID =? ";
		response = jdbcTemplate.update(dcPONumber, new Object[] {new_Po_Number,old_Po_Number});
		
		return response;

	}

	/*public int updatePurchasetblForRevision(String indentCreationDetailsId,String poIntiatedQuantity,String status)
	{

		String query =  "update SUM_PURCHASE_DEPT_INDENT_PROSS set PO_INTIATED_QUANTITY ='"+poIntiatedQuantity+"',STATUS='"+status+"' where INDENT_CREATION_DETAILS_ID = ?";

		int result = jdbcTemplate.update(query, new Object[] {indentCreationDetailsId

		});
		return result;
	}*/


	@Override
	public int checkIOndentCreationtbl(String indentNumber) {
		int result=0;
		String status="";
		List<Map<String, Object>> dbIndentDts = null;

		String query1 = "update SUMADHURA_INDENT_CREATION set STATUS='A',PENDIND_DEPT_ID ='998'  WHERE INDENT_CREATION_ID = ? ";	
		result = jdbcTemplate.update(query1, new Object[] {indentNumber});

		return result;
	}

	public String getSiteLevelPoNumber(String site_Id) {
		List<Map<String, Object>> productList = null;
		String total_Records="";
		String current_Records="";
		String result_Records="";
		
		String query = "SELECT TOTAL_NO_RECORDS,CURRENT_YEAR_PO FROM SUMADHURA_SITE_LEVEL_PO_NUM where SITE_ID = ?";
		productList = jdbcTemplate.queryForList(query, new Object[] {site_Id}); 
		try{

			if(productList!= null){
				for(Map<String, Object> list : productList) {
					total_Records=(list.get("TOTAL_NO_RECORDS")== null ? "0" : list.get("TOTAL_NO_RECORDS").toString());
					current_Records=(list.get("CURRENT_YEAR_PO")== null ? "0" : list.get("CURRENT_YEAR_PO").toString());
					result_Records=total_Records+"@@"+current_Records;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result_Records;

	}
	
	
	public int insertSiteLevelIndentData(int site_Id,String user_Id,int indent_Number,String siteWiseIndent) {

		String approvalEmpId=getTemproryuser(user_Id);
		int result =0;
		String versionNo = validateParams.getProperty("indent_print_version_No");
		String refferenceNo = validateParams.getProperty("indent_print_reference_No");
		String issueDate = validateParams.getProperty("indent_print_issue_date");
		// checking is site wise indent no exist or not . If exist getting max number +1 and saving.
		//Getting site wise indent number is asynchronous...if multiple persons clicking same time getting same number in indent creation page
		String sql="select count(1) from SUMADHURA_INDENT_CREATION where SITE_ID = ? and SITEWISE_INDENT_NO = ?";
		int count = jdbcTemplate.queryForInt(sql, new Object[] {site_Id,siteWiseIndent});
		int siteWiseIndentNo =  Integer.parseInt(siteWiseIndent);

		if(count > 0){
			sql="select max(SITEWISE_INDENT_NO) from SUMADHURA_INDENT_CREATION where SITE_ID ='"+site_Id+"'";
			siteWiseIndentNo = jdbcTemplate.queryForInt(sql, new Object[] {});
			siteWiseIndentNo=siteWiseIndentNo+1;
		}

		if(approvalEmpId!=null && !approvalEmpId.equals("VND")){
			String query = "INSERT INTO SUMADHURA_INDENT_CREATION(INDENT_CREATION_ID, create_date, site_id ,INDENT_CREATE_EMP_ID ,schedule_date ,Pending_emp_id ,pendind_dept_id ,status,REQUIRED_DATE,TEMPPASS,SITEWISE_INDENT_NO,VERSION_NO,REFERENCE_NO,ISSUE_DATE) "+
			"VALUES(?, sysdate ,? ,? ,sysdate ,? ,? ,? ,sysdate ,?, ?,?,?,?)";
			result = jdbcTemplate.update(query, new Object[] {
					indent_Number,site_Id,user_Id,approvalEmpId,"",
					"SITELEVEL","0",siteWiseIndentNo,versionNo,refferenceNo,issueDate});
		}else {

			String query = "INSERT INTO SUMADHURA_INDENT_CREATION(INDENT_CREATION_ID, create_date, site_id ,INDENT_CREATE_EMP_ID ,schedule_date ,Pending_emp_id ,pendind_dept_id ,status,REQUIRED_DATE,TEMPPASS,SITEWISE_INDENT_NO,VERSION_NO,REFERENCE_NO,ISSUE_DATE) "+
			"VALUES(?, sysdate ,? ,? ,sysdate ,? ,? ,? ,sysdate ,?, ?,?,?,?)";
			result = jdbcTemplate.update(query, new Object[] {
					indent_Number,site_Id,user_Id,"",approvalEmpId,
					"SITELEVEL","0",siteWiseIndentNo,versionNo,refferenceNo,issueDate});

		}
		return siteWiseIndentNo;
	}

	public int updateSiteLeveltbl(String site_Id,int total_Records,int current_Records){ //siteLevel CreatePO Page

		String query = "update SUMADHURA_SITE_LEVEL_PO_NUM set TOTAL_NO_RECORDS = ?,CURRENT_YEAR_PO=? where SITE_ID ='"+site_Id+"'";
		int result = jdbcTemplate.update(query, new Object[] {total_Records,current_Records});
		return result;
	}
	
	
/*	****************************************************insert indent approval details at the time of site level po**********************************************/
	
	
	
	public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentNumber,int site_id,String user_id) {
		String query = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
			+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID  ) "+
			"VALUES(?, ?, ?, sysdate, ?, ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationApprovalSeqNum,indentNumber,"C",site_id,user_id});
		return result;
	}

	/**********************************************************for Temp Mail***************************************************************************/
	// sending mail time along with data added so using user id and temppoNumber get data
	public List<String> getApproveMailDetails(String tempPoNumber,String userId){
		List<Map<String, Object>> dbEmpDts = null;
		List<String> listOfDetails = new ArrayList<String>();
		String vendorName = "";
		String siteWiseInhdentNo = "";
		String po_Date = "";
		String empName = "";
		String email = "";
		String convertedPoDate = "";
		String total = "";
		String siteName = "";
		String siteId="";
		String query = "select VD.VENDOR_NAME,SIC.SITEWISE_INDENT_NO,SPE.PO_DATE,SED.EMP_NAME,SED.EMP_EMAIL,SPE.TOTAL_AMOUNT,S.SITE_NAME,SPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY SPE,VENDOR_DETAILS VD,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S"
				+ " WHERE VD.VENDOR_ID=SPE.VENDOR_ID AND SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SPE.PO_NUMBER=? AND EMP_ID=? and SPE.SITE_ID=S.SITE_ID";

		dbEmpDts = jdbcTemplate.queryForList(query, new Object[] { tempPoNumber, userId });
		if(dbEmpDts==null || dbEmpDts.size()==0){
			String query1 = "select VD.VENDOR_NAME,SPE.PO_DATE,SED.EMP_NAME,SED.EMP_EMAIL,SPE.TOTAL_AMOUNT,S.SITE_NAME,SPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY SPE,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S"
				+ " WHERE VD.VENDOR_ID=SPE.VENDOR_ID  AND SPE.PO_NUMBER=? AND EMP_ID=? and SPE.SITE_ID=S.SITE_ID";

		dbEmpDts = jdbcTemplate.queryForList(query1, new Object[] { tempPoNumber, userId });
		}
		
		for (Map<String, Object> prods : dbEmpDts) {
			empName = prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString();
			email = prods.get("EMP_EMAIL") == null ? "" : prods.get("EMP_EMAIL").toString();
			vendorName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
			siteWiseInhdentNo = prods.get("SITEWISE_INDENT_NO") == null ? ""
					: prods.get("SITEWISE_INDENT_NO").toString();
			po_Date = prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString();
			total = prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString();
			siteName = prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString();
			siteId=prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString();
			DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date poReffDate;
			try {
				poReffDate = inputFormat.parse(po_Date);
				convertedPoDate = outputFormat.format(poReffDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			listOfDetails.add(empName);
			listOfDetails.add(email);
			listOfDetails.add(vendorName);
			listOfDetails.add(siteWiseInhdentNo);
			listOfDetails.add(convertedPoDate);
			listOfDetails.add(total);
			listOfDetails.add(siteName);listOfDetails.add(siteId);
		}

		return listOfDetails;
	}

	/******************************************************************
	 * for MAil End
	 ****************************************************************************/

	/***************************************************************
	 * mailFor tempPo start
	 *************************************************************************/

	public int getDataForMailForTempPo(String poNumber, String siteId) {

		List<Map<String, Object>> dbProductDetailsList = null;

		String quantity = "";
		String indentCreationDetailsId = "";
		String indentNumber = "";
		int result = 0;
		String sql = "select PO_ENTRY_ID from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER='" + poNumber + "' ";
		String poEntryId = jdbcTemplate.queryForObject(sql, String.class);

		String query = "select STPE.PO_QTY,STPE.INDENT_CREATION_DTLS_ID,SPE.INDENT_NO "
				+ " from SUMADHURA_TEMP_PO_ENTRY_DTLS STPE,SUMADHURA_TEMP_PO_ENTRY SPE"
				+ " where STPE.PO_ENTRY_ID=? AND  STPE.PO_ENTRY_ID=SPE.PO_ENTRY_ID";
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] { poEntryId });

		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {

				quantity = prods.get("PO_QTY") == null ? "" : prods.get("PO_QTY").toString();
				indentCreationDetailsId = prods.get("INDENT_CREATION_DTLS_ID") == null ? ""
						: prods.get("INDENT_CREATION_DTLS_ID").toString();
				indentNumber = prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString();
				if(!indentCreationDetailsId.equals("-")){
				updatePurchaseDeptIndentProcestbl(indentNumber, quantity, poNumber, indentCreationDetailsId,false);}
			}
			result = updateTablesOnTempPORejection(indentNumber, poNumber, "");
		}

		return result;
	}

	/*****************************************************************
	 * mail for temp PO End
	 ************************************************************************/
	// check the status while cancel po time rejhect time also
	public String checkApproveStatus(String poNumber) { 

		String sql = "select PO_STATUS from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER='" + poNumber + "' ";
		String status = jdbcTemplate.queryForObject(sql, String.class);
		return status;
	}

	/*****************************************************************
	 * total amount updated infor po
	 **********************************************************************/
	public int updateTotalAmt(String grandtotal, String approvalEmpId, String strPONumber) {
		int result = 0;
		if (approvalEmpId != null && approvalEmpId.equals("VND")) {

			String query = "update SUMADHURA_PO_ENTRY set TOTAL_AMOUNT=? where PO_NUMBER=?";
			result = jdbcTemplate.update(query, new Object[] { grandtotal, strPONumber });

		} else {

			String query = "update SUMADHURA_TEMP_PO_ENTRY set TOTAL_AMOUNT=? where PO_NUMBER=?";
			result = jdbcTemplate.update(query, new Object[] { grandtotal, strPONumber });
		}
		return result;
	}



/*****************************************************************total amount updated infor po**********************************************************************/


/****************************************************temp po mail Password**********************************************/

@Override
public String getTempPOPassword(String tempPoNumber,boolean isPerminentCancelPOOrNot) {
	
	String Query ="";
	String passwd ="";
	List<Map<String, Object>> dbProductDetailsList = null;
	if(isPerminentCancelPOOrNot){
		 Query = "select PASSWORD_MAIL from SUMADHURA_CANCEL_PO where STATUS='A' AND PO_NUMBER = '"+tempPoNumber+"'"; 
		//String passwd = jdbcTemplate.queryForObject(Query, String.class);
	}else{
	 Query = "select PASSWORD_MAIL from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER = '"+tempPoNumber+"'"; 
	
	}
	dbProductDetailsList = jdbcTemplate.queryForList(Query, new Object[] {});
	if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
		for (Map<String, Object> prods : dbProductDetailsList) {

	 passwd = prods.get("PASSWORD_MAIL") == null ? "0" : prods.get("PASSWORD_MAIL").toString();
		}
	}
		 
	 
	return passwd;

}

@Override
public String getTempPoTotalAmt(String strUserId,int temppoentryId) {
	
	String total ="";
	if(strUserId!=null && strUserId.equals("VND")){
		
		String Query ="select sum(TOTAL_AMOUNT) from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID = '"+temppoentryId+"'"; 
		total = jdbcTemplate.queryForObject(Query, String.class);
				
	}else{
	String Query = "select sum(TOTAL_AMOUNT) from SUMADHURA_TEMP_PO_ENTRY_DTLS where PO_ENTRY_ID = '"+temppoentryId+"'"; 
	 total = jdbcTemplate.queryForObject(Query, String.class);
	}
	return total;

}

	/*******************************************************
	 * For Mail Purpose
	 *************************************************************************/

	public String getCancelOrNot(String tempPoNumber) {
		List<Map<String, Object>> dbProductDetailsList = null;
		String cancel = "";
		String Query = "select VIEWORCANCEL from SUMADHURA_TEMP_PO_ENTRY WHERE PO_NUMBER=?";

		dbProductDetailsList = jdbcTemplate.queryForList(Query, new Object[] { tempPoNumber });
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {

				cancel = prods.get("VIEWORCANCEL") == null ? "" : prods.get("VIEWORCANCEL").toString();

			}
		}

		return cancel;
	}

	/***************************************revised or not*********************************************************************************/
	
	public String checkRevisedOrNot(String poNumber) {
		List<Map<String, Object>> dbProductDetailsList = null;
		String RevisedPoNumber = "";
		String status ="";
		String sql = "select PO_STATUS from SUMADHURA_PO_ENTRY where PO_NUMBER='" + poNumber + "' ";
		 status = jdbcTemplate.queryForObject(sql, String.class);
		if(status.equalsIgnoreCase("REVISED")){
			String query = "select PO_NUMBER from SUMADHURA_PO_ENTRY where OLD_PO_NUMBER=? ";
			dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] { poNumber });
			if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
				for (Map<String, Object> prods : dbProductDetailsList) {

					RevisedPoNumber = prods.get("PO_NUMBER") == null ? "" : prods.get("PO_NUMBER").toString();
					status+="@@@"+RevisedPoNumber;
				}
			}
			
		}else if(status.equalsIgnoreCase("UPDATE")){
			String query = "select PO_NUMBER from SUMADHURA_PO_ENTRY where OLD_PO_NUMBER=? ";
			dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] { poNumber });
			if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
				for (Map<String, Object> prods : dbProductDetailsList) {

					RevisedPoNumber = prods.get("PO_NUMBER") == null ? "" : prods.get("PO_NUMBER").toString();
					status+="@@@"+RevisedPoNumber;
				}
			}
			
		}
		return status;
	}

	/*	********************************************************revised or not end**********************************************************/
	/*	*****************************************************get received quantity for Revised po purpose start************************************/
	public String getOldPoQuantitytoRevised(String poEntryId,String indentCreationDetailsId){
		List<Map<String, Object>> dbEmpDts = null;
		String reveivedQuantity="0";
		String sql = "select RECEIVED_QUANTITY FROM SUMADHURA_PO_ENTRY_DETAILS WHERE PO_ENTRY_ID=? AND INDENT_CREATION_DTLS_ID=?";
		dbEmpDts = jdbcTemplate.queryForList(sql, new Object[] {poEntryId,indentCreationDetailsId});
		for (Map<String, Object> prods : dbEmpDts) {
			reveivedQuantity = prods.get("RECEIVED_QUANTITY") == null ? "0" : prods.get("RECEIVED_QUANTITY").toString();
		}
		return reveivedQuantity;
	}




	/*	*****************************************************get received quantity for Revised po purpose end************************************/

	public Map<String,Double> getTotalNumberOfRecords(String poEntryId){
		List<Map<String, Object>> GetproductDetailsList = null;
		Map<String,Double> poQuantityDetails = new HashMap<String,Double>();
		//List<ProductDetails> list = new ArrayList<ProductDetails>();
		double poQuantity = 0;
		String indentCreationDetailsId="";
		String sql="select CHILD_PRODUCT_ID,INDENT_CREATION_DTLS_ID,PO_QTY from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID=? order by INDENT_CREATION_DTLS_ID";
		GetproductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {poEntryId});
		for(Map<String, Object> prods : GetproductDetailsList) {

			//ProductDetails pd = new ProductDetails();
			poQuantity= Double.valueOf(prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString());
			//pd.setChild_ProductId(prods.get("CHILD_PRODUCT_ID") == null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
			indentCreationDetailsId=(prods.get("INDENT_CREATION_DTLS_ID") == null ? "" : prods.get("INDENT_CREATION_DTLS_ID").toString());
			poQuantityDetails.put(indentCreationDetailsId,poQuantity);
			//list.add(pd);
		}

		return poQuantityDetails;

	}

/*********************************************************reject revsied po and active old po******************************************************/
	// check whether rejected po is revised or not if revised normal po set inactive revised
	// po quantity back to tables if it is revised reect then Old po Active
	public String activeOldPOTable(String poNumber) {
		List<Map<String, Object>> oldPONumber = null;
		String old_PO_Number="";
		int result=0;
		String response="";
		String password="0";
		List<Map<String, Object>> GetproductDetailsList = null;
		String sql = "select OLD_PO_NUMBER from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER=? ";
		oldPONumber = jdbcTemplate.queryForList(sql, new Object[] {poNumber});
		for(Map<String, Object> prods : oldPONumber) {
			old_PO_Number=(prods.get("OLD_PO_NUMBER") == null ? "" : prods.get("OLD_PO_NUMBER").toString());
		}
		if(old_PO_Number!=null && !old_PO_Number.equals("") && !old_PO_Number.equals("-") ){
			
			String query = "select PO_ENTRY_ID from SUMADHURA_PO_ENTRY where PO_NUMBER ='"+old_PO_Number+"'";
			String po_entry_Id=jdbcTemplate.queryForObject(query,String.class);
			
			String status = "update SUMADHURA_PO_ENTRY set PO_STATUS=? where PO_NUMBER=?";
			result = jdbcTemplate.update(status, new Object[] {"A",old_PO_Number });	
			
			
			
			String sql1="select PO_QTY,INDENT_CREATION_DTLS_ID from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID=? order by INDENT_CREATION_DTLS_ID";
			GetproductDetailsList = jdbcTemplate.queryForList(sql1, new Object[] {po_entry_Id});
			for(Map<String, Object> prods : GetproductDetailsList) {

				//ProductDetails pd = new ProductDetails();
			String poQuantity=(prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString());
			String	indentCreationDetailsId=(prods.get("INDENT_CREATION_DTLS_ID") == null ? "" : prods.get("INDENT_CREATION_DTLS_ID").toString());
				if(indentCreationDetailsId!=null  && !indentCreationDetailsId.equals("")){
					String query2 ="update SUM_PURCHASE_DEPT_INDENT_PROSS set PO_INTIATED_QUANTITY =?,STATUS='A'  where INDENT_CREATION_DETAILS_ID = ? ";
					result=jdbcTemplate.update(query2, new Object[] {poQuantity,indentCreationDetailsId});
					
				}
				
			}
			
			String query1 = "update SUMADHURA_TEMP_PO_ENTRY set PO_STATUS=?,PASSWORD_MAIL=? where PO_NUMBER=?";
			result = jdbcTemplate.update(query1, new Object[] {"I",password,poNumber });	
			
			response="success";
		}else{response="failure";}
		
		return response;
	}
	
	/*********************************************************reject revsied po and active old po end******************************************************/	
	
	//this is for revised po at that time acc_po_history table data insert into acc_revised_po for users get data in future start
	public String insertAndGetDataPOHistory(String old_Po_Number) {
		
		List<Map<String, Object>> getPoHistoryList = null;
		String po_Amount="";
		String po_Date="";
		String paid_Amount="";
		String response="failure";
		String poHistoryId="";
		String query="select PO_HISTORY_ID,PAID_AMOUNT,PO_AMOUNT,ENTRY_DATE from ACC_PO_HISTORY APH  WHERE PO_NUMBER=?";
		getPoHistoryList = jdbcTemplate.queryForList(query, new Object[] {old_Po_Number});
		if(null !=getPoHistoryList && getPoHistoryList.size()>0){
		for (Map<?, ?> getTemPoHistoryDetailsMap : getPoHistoryList) {
			
			poHistoryId = getTemPoHistoryDetailsMap.get("PO_HISTORY_ID") == null ? "": getTemPoHistoryDetailsMap.get("PO_HISTORY_ID").toString();
			po_Amount = getTemPoHistoryDetailsMap.get("PO_AMOUNT") == null ? "": getTemPoHistoryDetailsMap.get("PO_AMOUNT").toString();
			po_Date = getTemPoHistoryDetailsMap.get("ENTRY_DATE") == null ? "": getTemPoHistoryDetailsMap.get("ENTRY_DATE").toString();
			paid_Amount=getTemPoHistoryDetailsMap.get("PAID_AMOUNT") == null ? "": getTemPoHistoryDetailsMap.get("PAID_AMOUNT").toString();
		
		String query1 = "INSERT INTO ACC_REVISED_PO_HISTORY(REVISED_PO_HISTORY_ID,PO_HISTORY_ID,PO_NUMBER,PO_AMOUNT,ENTRY_DATE,PAID_AMOUNT) values(REVISED_PO_HISTORY_SEQ.nextval,?,?,?,?,?)";
		int result = jdbcTemplate.update(query1, new Object[] {poHistoryId,old_Po_Number,po_Amount,po_Date,paid_Amount});
		
		}
		response="success";
		}

		return response;	
	}
	//this is for revised po at that time acc_po_history table data insert into acc_revised_po for users get data in future end
	//this is for revised po at that time acc_invoice_history table data insert into acc_revised_invoice_history for users get data in future end	
	public String insertAndGetDataInvoiceHistory(String old_Po_Number) {
		
		List<Map<String, Object>> getInvoiceHistoryList = null;
		String invoice_Number="";
		String invoice_Amount="";
		String entry_Date="";
		String paid_Amount="";
		String payment_TransactionId="";
		String payment_Mode="";
		String response="failure";
		String invoiceHistoryId="";
		String query="select INVOICE_HISTORY_ID,INVOICE_NUMBER,INVOICE_AMOUNT,ENTRY_DATE,PAID_AMOUNT,PAYMENT_MODE,PAYMENT_TRANSACTIONS_ID from ACC_INVOICE_HISTORY   WHERE REF_NO=?";
		getInvoiceHistoryList = jdbcTemplate.queryForList(query, new Object[] {old_Po_Number});
		if(null !=getInvoiceHistoryList && getInvoiceHistoryList.size()>0){
		for (Map<?, ?> getTemPoHistoryDetailsMap : getInvoiceHistoryList) {
			
			invoiceHistoryId = getTemPoHistoryDetailsMap.get("INVOICE_HISTORY_ID") == null ? "": getTemPoHistoryDetailsMap.get("INVOICE_HISTORY_ID").toString();
			invoice_Number = getTemPoHistoryDetailsMap.get("INVOICE_NUMBER") == null ? "": getTemPoHistoryDetailsMap.get("INVOICE_NUMBER").toString();
			invoice_Amount = getTemPoHistoryDetailsMap.get("INVOICE_AMOUNT") == null ? "": getTemPoHistoryDetailsMap.get("INVOICE_AMOUNT").toString();
			entry_Date=getTemPoHistoryDetailsMap.get("ENTRY_DATE") == null ? "": getTemPoHistoryDetailsMap.get("ENTRY_DATE").toString();
			paid_Amount=getTemPoHistoryDetailsMap.get("PAID_AMOUNT") == null ? "": getTemPoHistoryDetailsMap.get("PAID_AMOUNT").toString();
			payment_TransactionId=getTemPoHistoryDetailsMap.get("PAYMENT_TRANSACTIONS_ID") == null ? "": getTemPoHistoryDetailsMap.get("PAYMENT_TRANSACTIONS_ID").toString();
			payment_Mode=getTemPoHistoryDetailsMap.get("PAYMENT_MODE") == null ? "": getTemPoHistoryDetailsMap.get("PAYMENT_MODE").toString();
		String query1 = "INSERT INTO ACC_REVISED_INVOICE_HISTORY(REVISED_INVOICE_HISTORY_ID,INVOICE_HISTORY_ID,INVOICE_NUMBER,INVOICE_AMOUNT,ENTRY_DATE,PAID_AMOUNT,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID) values(REVISED_INVOICE_HISTORY_SEQ.nextval,?,?,?,?,?,?,?,?)";
		int result = jdbcTemplate.update(query1, new Object[] {invoiceHistoryId,invoice_Number,invoice_Amount,entry_Date,paid_Amount,payment_Mode,old_Po_Number,payment_TransactionId});
		
		}
		response="success";
		}

		return response;	
	}

	/******************************************************revised po data saved in acc_invoice_history to acc_revised_invoice_history****************/

/*********************************************************revised po data saved then mails send to acc dept start*******************************************/
//they need to know the change in acc dept table po amount,po number,po request amount
	public List<String>  getAccDeptEmails(String deptId) {
		List<Map<String, Object>> dbAccdeptEmails = null;
		String strEmailId = "";
		List<String> objList = new ArrayList<String>();
		//thisIsAStringArray[5] = "FFF";
		String query = " select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where DEPT_ID in(?,?,?) ";
		dbAccdeptEmails = jdbcTemplate.queryForList(query, new Object[] {deptId+"1",deptId+"2",deptId+"3"});
		if(dbAccdeptEmails!= null){
			for(Map<String, Object> mails : dbAccdeptEmails) {
				strEmailId = mails.get("EMP_EMAIL")==null ? "" :   mails.get("EMP_EMAIL").toString();

				if(strEmailId.contains(",")){
					for(String strEmailId1 : strEmailId.split(","))
					{
						objList.add(strEmailId1);
					}
				}
				else if(!strEmailId.equals("") && strEmailId!=null){
				objList.add(strEmailId);
				}
				}	
		}

		return objList;

	}
	 /********************* Inactive Payment Tables - start At the time of Revised Po**********************/
	/****************************************************************************/
	@Override
	public Map<String, Object> getAccPaymentDetailsByPoNo(String poNo, String siteId, String vendorId) {
		String query = "select PAYMENT_ID,PO_AMOUNT,PAYMENT_REQ_UPTO from ACC_PAYMENT "
				+  "where PO_NUMBER = ? and SITE_ID = ? and VENDOR_ID = ? ";
		try{
			Map<String,Object> map = jdbcTemplate.queryForMap(query,new Object[]{ poNo, siteId, vendorId});
			return map;
		}
		catch(EmptyResultDataAccessException e){
			return null;
		}

	}

	@Override
	public List<Integer> getInitiatedPaymentDtlsIdsByPaymentId(int paymentId) {
		String query = "select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS where PAYMENT_ID = ? and PAYMENT_TYPE = 'ADVANCE' ";

		List<Integer> list = jdbcTemplate.queryForList(query, new Object[]{paymentId}, Integer.class);
		return list;

	}

	@Override
	public List<Integer> getPaymentCompletedPaymentDtlsIdsByPaymentId(int paymentId) {
		String query = "select PAYMENT_DETAILS_ID  from ACC_PAYMENT_TRANSACTIONS where PAYMENT_ID = ? ";

		List<Integer> list = jdbcTemplate.queryForList(query, new Object[]{paymentId}, Integer.class);
		return list;

	}

	@Override
	public String getRequestedAmount(Integer paymentDetailsId) {
		String query = "select REQ_AMOUNT from ACC_PAYMENT_DTLS where PAYMENT_DETAILS_ID = ?";
		return jdbcTemplate.queryForObject(query, new Object[]{paymentDetailsId},String.class);
	}

	@Override
	public boolean isAccTempPaymentTransactionTblHasPaymentDetailsId(int paymentDetailsId) {
		String query = "select PAYMENT_DETAILS_ID from ACC_TEMP_PAYMENT_TRANSACTIONS where PAYMENT_DETAILS_ID = ? ";
		List<Integer> list = jdbcTemplate.queryForList(query, new Object[]{paymentDetailsId}, Integer.class);
		if(list.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public boolean isAccDeptPmtProcessTblHasPaymentDetailsId(Integer paymentDetailsId) {
		String query = "select PAYMENT_DETAILS_ID from ACC_ACCOUNTS_DEPT_PMT_PROSS where PAYMENT_DETAILS_ID = ? ";
		List<Integer> list = jdbcTemplate.queryForList(query, new Object[]{paymentDetailsId}, Integer.class);
		if(list.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public boolean isAccPaymentDtlsHasPaymentDetailsId(Integer paymentDetailsId) {
		String query = "select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS where PAYMENT_DETAILS_ID = ? ";
		List<Integer> list = jdbcTemplate.queryForList(query, new Object[]{paymentDetailsId}, Integer.class);
		if(list.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public int inactiveRowInAccTempPaymentTransactions(Integer paymentDetailsId) {
		String query = "update ACC_TEMP_PAYMENT_TRANSACTIONS set REMARKS = 'PO_Revised', STATUS = 'I' where PAYMENT_DETAILS_ID = ? ";
		return jdbcTemplate.update(query, new Object[] {paymentDetailsId});
	}

	@Override
	public int inactiveRowInAccDeptPmtProcessTbl(Integer paymentDetailsId) {
		String query = "update ACC_ACCOUNTS_DEPT_PMT_PROSS set STATUS='I'  where PAYMENT_DETAILS_ID = ?";
		return jdbcTemplate.update(query, new Object[] {paymentDetailsId});
	}

	@Override
	public int inactiveRowInAccPaymentDtls(Integer paymentDetailsId) {
		String query = "update ACC_PAYMENT_DTLS set REMARKS = 'PO_Revised', STATUS ='I'  where PAYMENT_DETAILS_ID = ?";
		return jdbcTemplate.update(query, new Object[]  {paymentDetailsId});
	}

	@Override
	public int updateReqUptoInAccPayment(double reqAmount,int paymentId) {
		String query = "update ACC_PAYMENT set PAYMENT_REQ_UPTO=PAYMENT_REQ_UPTO-? where PAYMENT_ID=? ";
		return jdbcTemplate.update(query, new Object[] {reqAmount,paymentId});
	}
	 /***************************************************************************/
	/********************** Inactive Payment Tables - end **********************/
	
	/********************************************************get the email for acc dept initiate payment start********************************************/
	
	
	public List<String> getSiteLevelPoAmountInitiateEmail(int paymentDetailsId)
	{
		List<Map<String, Object>> dbAccdeptEmpId = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<String> objList = new ArrayList<String>();
		String emailId="";
		String strEmpId="";
		String sql= "SELECT ASPRD.EMP_ID FROM ACC_SITE_APPR_REJECT_DTLS ASPRD where ASPRD.PAYMENT_DETAILS_ID =? order by ASPRD.CREATED_DATE  ASC";
		dbAccdeptEmpId = jdbcTemplate.queryForList(sql, new Object[] {paymentDetailsId});
		if(dbAccdeptEmpId!= null){
			for(Map<String, Object> empId : dbAccdeptEmpId) {
				strEmpId = empId.get("EMP_ID")==null ? "" :   empId.get("EMP_ID").toString();
				break;
			}
			String query="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where EMP_ID =?";
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strEmpId});
			if(dbIndentDts!= null){
				for(Map<String, Object> mails : dbIndentDts) {
					emailId = mails.get("EMP_EMAIL")==null ? "" :   mails.get("EMP_EMAIL").toString();
					if(emailId.contains(",")){
						for(String strEmailId1 : emailId.split(","))
						{
							objList.add(strEmailId1);
						}
					}
					else if(!emailId.equals("") && emailId!=null){
					objList.add(emailId);
					}
					
				}
			}
			
			
		}
		return objList;
		
	}
/*	****************************************to get perminent po number**************************************************************************/
	public String getPermenentPoNumber(String poState,String type,String siteId,String financialyear) {
		String strPONumber="";
		if(type.equalsIgnoreCase("PURCHASE_DEPT")){
		int intHOWiseYearwiseNumber = getPoEnterSeqNoOrMaxId("PO_SIPL"); //PO_NUMBER changed to PO_SIPL because requirment is changed they want SIPL level
		int intHOWiseInfinityNumber=getHeadOfficeInfinitMaxId("PO_SIPL");
		String siteWise_Number=getSiteWisePoNumber(siteId);
		String siteWise_YearNumber=getStateWiseYearPoNumber(siteId);

		strPONumber=poState+String.valueOf(intHOWiseInfinityNumber)+"/"+siteWise_Number+"/"+financialyear+"/"+siteWise_YearNumber;
		int intSiteWise_Number= Integer.parseInt(siteWise_Number)+1;
		int intSiteWise_YearNumber = Integer.parseInt(siteWise_YearNumber)+1;
		getUpdatePoNumberGeneratorHeadOfficeWise(intHOWiseInfinityNumber+1,intHOWiseYearwiseNumber+1,"PO_SIPL");  //PO_TELANGANA changed to PO_SIPL because requirment is changed they want SIPL level
		getUpdateStateWisePoNumber(intSiteWise_Number,intSiteWise_YearNumber,siteId);

		}else{
			String site_Level_Po_Number=getSiteLevelPoNumber(siteId);
			String data[] = site_Level_Po_Number.split("@@");

			 int total_Records =Integer.parseInt(data[0]);
			 int current_Records=Integer.parseInt(data[1]);
			
			strPONumber=poState+type+"/"+total_Records+"/"+financialyear+"/"+current_Records;
			updateSiteLeveltbl(siteId,total_Records+1,current_Records+1);
			
		}
		
		return strPONumber;

	}
/********************************************************to get billing address*****************************************************************/
	public List<String> getBillingAddGstin(String receiverState)
	{
		List<String> billingAddressDetails = new ArrayList<String>();
		String billingAddress="";
		String strBillingAddressGSTIN ="";
		String strBillingCompanyName="";
		if(receiverState.equalsIgnoreCase("Telangana")){

		 	 billingAddress=	validateParams.getProperty("BILLING_ADDRESSS_HYDERABAD");
		 	 strBillingAddressGSTIN = validateParams.getProperty("GSTIN_HYDERABAD");
		 	 strBillingCompanyName=validateParams.getProperty("BILLING_NAME");
		}else{

			billingAddress=	validateParams.getProperty("BILLING_ADDRESSS_BENGALORE");
			strBillingAddressGSTIN = validateParams.getProperty("GSTIN_BENGALORE");
			strBillingCompanyName=validateParams.getProperty("BILLING_NAME");

		}
		billingAddressDetails.add(billingAddress);billingAddressDetails.add(strBillingAddressGSTIN);
		billingAddressDetails.add(strBillingCompanyName);
		
		return billingAddressDetails;
		
	}
	/*	****************************************************to get View MIS PO Report data************************************************************/
public List<ProductDetails> getViewPoDetailsDetails(HttpServletRequest request,String fromDate,String toDate){
		
		List<String> list = new ArrayList<String>();
		List<ProductDetails> prod = new ArrayList<ProductDetails>();
		Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
		//Set<String,Double> poTotal=new HashSet<String,Double>();
		Set <String> po_Total = new HashSet <String>();
		ProductDetails productDetails =null;
		String query="";
		String invoiceNumber="";
		String strMaterialName="";
		double po_Amount=0.0;
		String siteName="";
		String indentNumber="";
		String poNumber="";
		String poDate="";
		String indentDate="";
		String vedorName="";
		String receiveDate="";
		String invoiceAmt="";
		int quatation=0;
		String indent_Entry_Id="";
		String grnDate="";
		String deliveryStatus="";
		String purchase_Req_Rec_Date="";
		String deliveryDate="";
		String old_IndentEntryId="";
		double poTotal=0.0;
		double invoiceTotal=0.0;
		int old_Quatation=0;
		String siteIds=request.getParameter("siteIds");
		siteIds=siteIds.replace(",","','");
		String site_Id="";
		String vendor_Id="";
		String invoiceDate="";
		String siteWiseIndent="";
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
		 query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,QUATATIONS,SPE.PO_DATE,SPE.DELIVERY_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
			 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,PURCHASE_DEPT_REQ_REC_DATE,IE.INVOICE_DATE"
			 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE,COUNT(SEFD.CHILD_PRODUCT_ID) AS QUATATIONS,SPDIP.CREATION_DATE as PURCHASE_DEPT_REQ_REC_DATE" 
			 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED left outer join SUMADHURA_ENQUIRY_FORM_DETAILS SEFD on  SEFD.INDENT_CREATION_DETAILS_ID =SPED.INDENT_CREATION_DTLS_ID and SEFD.VENDOR_MENTIONED_QTY is not  null"
			 +" LEFT outer join SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP  on SPDIP.INDENT_CREATION_DETAILS_ID = SPED.INDENT_CREATION_DTLS_ID"
			 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
			 +" GROUP BY "
			 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT,SPDIP.CREATION_DATE  order by SPE.PO_DATE ASC),"
			 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
			 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
			 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
			 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
			 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
			 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')order by SPE.PO_NUMBER ASC";
		}
		else if (StringUtils.isNotBlank(fromDate)) {
			query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,QUATATIONS,SPE.PO_DATE,SPE.DELIVERY_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
			 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,PURCHASE_DEPT_REQ_REC_DATE,IE.INVOICE_DATE"
			 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE,COUNT( SEFD.CHILD_PRODUCT_ID) AS QUATATIONS,SPDIP.CREATION_DATE as PURCHASE_DEPT_REQ_REC_DATE" 
			 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED left outer join SUMADHURA_ENQUIRY_FORM_DETAILS SEFD on  SEFD.INDENT_CREATION_DETAILS_ID =SPED.INDENT_CREATION_DTLS_ID and SEFD.VENDOR_MENTIONED_QTY is not  null"
			 +" LEFT outer join SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP  on SPDIP.INDENT_CREATION_DETAILS_ID = SPED.INDENT_CREATION_DTLS_ID"
			 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
			 +" GROUP BY "
			 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT,SPDIP.CREATION_DATE  order by SPE.PO_DATE ASC),"
			 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
			 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
			 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
			 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
			 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
			 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) >= TO_DATE('"+fromDate+"', 'dd-MM-yy') order by SPE.PO_NUMBER ASC";
		}else if(StringUtils.isNotBlank(toDate)) {
			query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,QUATATIONS,SPE.PO_DATE,SPE.DELIVERY_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
			 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,PURCHASE_DEPT_REQ_REC_DATE,IE.INVOICE_DATE"
			 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE,COUNT( SEFD.CHILD_PRODUCT_ID) AS QUATATIONS,SPDIP.CREATION_DATE as PURCHASE_DEPT_REQ_REC_DATE" 
			 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED left outer join SUMADHURA_ENQUIRY_FORM_DETAILS SEFD on  SEFD.INDENT_CREATION_DETAILS_ID =SPED.INDENT_CREATION_DTLS_ID and SEFD.VENDOR_MENTIONED_QTY is not  null"
			 +" LEFT outer join SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP  on SPDIP.INDENT_CREATION_DETAILS_ID = SPED.INDENT_CREATION_DTLS_ID"
			 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
			 +" GROUP BY "
			 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT,SPDIP.CREATION_DATE  order by SPE.PO_DATE ASC),"
			 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
			 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
			 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
			 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
			 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
			 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) <= TO_DATE('"+toDate+"', 'dd-MM-yy') order by SPE.PO_NUMBER ASC";

		}
		List<Map<String, Object>> dbPoDts = jdbcTemplate.queryForList(query, new Object[] {});
		java.util.Iterator<Map<String, Object>>  itr = dbPoDts.iterator();
		int sno=1;
		while(itr.hasNext()){
			Map<String, Object>  map = itr.next();
			//sno++;
			indent_Entry_Id=(map.get("INDENT_ENTRY_ID")==null ? "IE_ID"+(sno++) : map.get("INDENT_ENTRY_ID").toString());// if empty value coming key set to id to 1
			invoiceNumber=(map.get("INVOICE_ID")==null ? "" : map.get("INVOICE_ID").toString());
			strMaterialName=map.get("DESC_MATERAILS")==null ? "" : map.get("DESC_MATERAILS").toString();
			po_Amount=Double.valueOf(map.get("PO_TOTAL")==null ? "0" : map.get("PO_TOTAL").toString());
			BigDecimal bigDecimaltemp_PoTotal = new BigDecimal(po_Amount);
			po_Amount=Double.valueOf(String.valueOf(bigDecimaltemp_PoTotal.setScale(2,RoundingMode.CEILING)));
			siteName=(map.get("SITE_NAME")==null ? "" : map.get("SITE_NAME").toString());
			indentNumber=(map.get("INDENT_CREATION_ID")==null ? "" : map.get("INDENT_CREATION_ID").toString());
			siteWiseIndent=(map.get("SITEWISE_INDENT_NO")==null ? "" : map.get("SITEWISE_INDENT_NO").toString());
			indentDate=(map.get("INDENT_DATE")==null ? "" : map.get("INDENT_DATE").toString());
			poDate=(map.get("PO_DATE")==null ? "-" : map.get("PO_DATE").toString());
			
			poNumber=(map.get("PO_NUMBER")==null ? "" : map.get("PO_NUMBER").toString());
			vedorName=(map.get("VENDOR_NAME")==null ? "" : map.get("VENDOR_NAME").toString());
			receiveDate=(map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString());
			grnDate=(map.get("RECEIVED_DATE")==null ? "-" : map.get("RECEIVED_DATE").toString());
			invoiceAmt=(map.get("INVOICE_AMOUNT")==null ? "0" : map.get("INVOICE_AMOUNT").toString());
			quatation=(Integer.valueOf(map.get("QUATATIONS")==null ? "0" : map.get("QUATATIONS").toString()));
			deliveryDate=(map.get("DELIVERY_DATE")==null ? "-" : map.get("DELIVERY_DATE").toString());
			deliveryStatus=(map.get("DELIVERY_DATE")==null ? "NA" : map.get("DELIVERY_DATE").toString());
			purchase_Req_Rec_Date=(map.get("PURCHASE_DEPT_REQ_REC_DATE")==null ? "-" : map.get("PURCHASE_DEPT_REQ_REC_DATE").toString());
			 site_Id=(map.get("SITE_ID")==null ? "-" : map.get("SITE_ID").toString());
			vendor_Id=(map.get("VENDOR_ID")==null ? "-" : map.get("VENDOR_ID").toString());
			invoiceDate=(map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString());
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
			DateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			
			SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
			try{
			if(!purchase_Req_Rec_Date.equals("-") && !poDate.equals("-")){	
					Date dateBefore = DateFormat.parse(purchase_Req_Rec_Date);
				    Date dateAfter = DateFormat.parse(poDate);
					
				    long diff = dateAfter.getTime() - dateBefore.getTime();// to calculate the no of days for delivery status
					int hours=(int)diff / (60 * 60 * 1000);
					int strHours=hours%24;
					long diffDays = diff / (24 * 60 * 60 * 1000);
					
					purchase_Req_Rec_Date=String.valueOf((diffDays));
					
			}	
			if(!deliveryStatus.equals("NA") && !grnDate.equals("-")){	
			Date dateBefore = DateFormat.parse(grnDate);
		    Date dateAfter = DateFormat.parse(deliveryStatus);
			
		    //long diff = dateAfter.getDay()-dateBefore.getDay();
			long diff = dateAfter.getTime() - dateBefore.getTime();// to calculate the no of days for delivery status
			int hours=(int)diff / (60 * 60 * 1000);
			int strHours=hours%24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			//long diffDays =TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			if(diffDays<0){deliveryStatus=String.valueOf(diffDays);}
			if(diffDays>0){deliveryStatus=String.valueOf("+"+diffDays);}
			if(diffDays==0){deliveryStatus=String.valueOf(diffDays);}
			
			
			System.out.println("date"+diffDays);
			
			}if(!deliveryStatus.equals("NA") && grnDate.equals("-")){deliveryStatus="-";}
			Date afterIndentDate  = myFormat.parse(indentDate);
			Date afterPoDate  = myFormat.parse(poDate);
			indentDate = outputFormat.format(afterIndentDate);
			poDate=outputFormat.format(afterPoDate);
			if(!receiveDate.equals("-")){Date afterReceiveDate  = myFormat.parse(receiveDate);
			receiveDate=outputFormat.format(afterReceiveDate);
			}if(!grnDate.equals("-")){
				Date afterGrnDate  = myFormat.parse(grnDate);
				grnDate=outputFormat.format(afterGrnDate);
			}if(!deliveryDate.equals("-")){Date afterDeliveryDate  = myFormat.parse(deliveryDate);
			deliveryDate=outputFormat.format(afterDeliveryDate);}
			if(!invoiceDate.equals("-")){
				Date invoice_date = myFormat.parse(invoiceDate);
				invoiceDate = dt1.format(invoice_date);
				}
			
			}catch(Exception e){
				
			}
			//System.out.println("compare"+compare);
			
			if(list.contains(indent_Entry_Id)){
				if(!list.contains(strMaterialName)){
				mapgroupbyid.get(indent_Entry_Id).setMaterialDesc(mapgroupbyid.get(indent_Entry_Id).getMaterialDesc()+", "+strMaterialName);
				if(quatation>old_Quatation){
					mapgroupbyid.get(old_IndentEntryId).setQuatations(quatation);
				}
				//mapgroupbyid.get(indent_Entry_Id).setPoTotal(mapgroupbyid.get(indent_Entry_Id).getPoTotal()+(po_Amount));
				}
			}else if(list.contains(poNumber) && !list.contains(indent_Entry_Id) &&  list.contains(old_IndentEntryId) && old_IndentEntryId.contains("IE_ID")){
				if(!list.contains(strMaterialName)){
				mapgroupbyid.get(old_IndentEntryId).setMaterialDesc(mapgroupbyid.get(old_IndentEntryId).getMaterialDesc()+", "+strMaterialName);
				}if(quatation>old_Quatation){
					mapgroupbyid.get(old_IndentEntryId).setQuatations(quatation);
				}
				
			}
			else{
				
				productDetails = new ProductDetails();
				//productDetails.setStrSerialNumber(String.valueOf(sno));
				productDetails.setSiteName(siteName);
				productDetails.setIndentNo(indentNumber);
				productDetails.setSiteWiseIndentNo(siteWiseIndent);
				productDetails.setIndentDate(indentDate);
				productDetails.setPoDate(poDate);
				productDetails.setStrPONumber(poNumber);
				productDetails.setVendorName(vedorName);
				productDetails.setPoTotal(po_Amount);
				productDetails.setMaterialDesc(strMaterialName);
				productDetails.setReceiveDate(receiveDate);
				productDetails.setInvoiceNumber(invoiceNumber);
				productDetails.setInvoiceAmount(invoiceAmt);
				productDetails.setQuatations(quatation);
				productDetails.setPurchase_po_Req_Date(purchase_Req_Rec_Date);
				productDetails.setDeliveryDate(deliveryDate);
				productDetails.setIndentEntryId(indent_Entry_Id);
				productDetails.setGrnDate(grnDate);
				productDetails.setDeliveryStatus(deliveryStatus);
				productDetails.setSite_Id(site_Id);
				productDetails.setVendorId(vendor_Id);
				productDetails.setInvoiceDate(invoiceDate);
				mapgroupbyid.put(indent_Entry_Id,productDetails);
				old_IndentEntryId=indent_Entry_Id;
				old_Quatation=quatation;
				list.add(indent_Entry_Id);
				list.add(strMaterialName);
				list.add(poNumber);
				if(po_Total.add(poNumber)){poTotal=poTotal+Double.valueOf(po_Amount);}
				invoiceTotal=invoiceTotal+Double.valueOf(invoiceAmt);
				
				
				}
			}
		BigDecimal bigDecimalPoTotal = new BigDecimal(poTotal);
		BigDecimal bigDecimalInvoiceTotal = new BigDecimal(invoiceTotal);
		request.setAttribute("Po_GrandTotal",String.valueOf(bigDecimalPoTotal.setScale(2,RoundingMode.CEILING)));
		request.setAttribute("Invoice_GrandTotal",String.valueOf(bigDecimalInvoiceTotal.setScale(2,RoundingMode.CEILING)));// or grand total purpose
		request.setAttribute("siteIds",siteIds);
		//System.out.println("the map size"+mapgroupbyid.size());
		//System.out.println("the list size"+list.size());
		
		Set set = mapgroupbyid.entrySet();
		java.util.Iterator i = set.iterator();
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			ProductDetails checkingBean = (ProductDetails) me.getValue();
			prod.add(checkingBean);
			}

		return prod;
	}

	/**********************************************************Market Purchase details start*******************************************************************/
	public List<ProductDetails> getViewMarketPurchaseDetails(HttpServletRequest request,String fromDate,String toDate,String purchaseType){
		
		//List<String> list = new ArrayList<String>();
		List<ProductDetails> list = new ArrayList<ProductDetails>();
		Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
		ProductDetails productDetails =null;
		String query="";
		String invoiceDate="";
		String grnDate="";
		String getInvoiceDateGrn="";// grn purpose
		
		String site_Id="";
		String vendor_Id="";
		double invoiceAmount=0.0;
		double invoiceGrandTotal=0.0;
		String siteIds=request.getParameter("siteIds");
		siteIds=siteIds.replace(",","','");
		 
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
			 query="(select S.SITE_NAME,IE.INDENT_ENTRY_ID,IE.VENDOR_ID,IE.SITE_ID,VD.VENDOR_NAME,IE.INVOICE_DATE,IE.TOTAL_AMOUNT,IE.RECEIVED_OR_ISSUED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT IED.PRODUCT_NAME FROM INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID group by IED.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.INVOICE_NUMBER) from INDENT_ENTRY_DETAILS IED3,SUMADHURA_PRICE_LIST SPL3 where IE.INDENT_ENTRY_ID=IED3.INDENT_ENTRY_ID and IED3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by IE.INDENT_ENTRY_ID) as INVOICE_NUMBER,'INV' as IS_INV_OR_DC,IE.RECEIVED_OR_ISSUED_DATE as ORDER_BY_DATE "
				 +" from INDENT_ENTRY IE "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" where IE.SITE_ID in('"+siteIds+"') AND IE.INDENT_TYPE in ('IN','INO') and IE.INDENT_ENTRY_ID "
				 +" in "
				 +" (select IE2.INDENT_ENTRY_ID "
				 +" from INDENT_ENTRY IE2,INDENT_ENTRY_DETAILS IED2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where IE2.INDENT_ENTRY_ID=IED2.INDENT_ENTRY_ID and IED2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='"+purchaseType+"' "
				 +" group by IE2.INDENT_ENTRY_ID) and IE.RECEIVED_OR_ISSUED_DATE between to_date('"+fromDate+"','dd-mm-yy') and to_date('"+toDate+"','dd-mm-yy') "
				 +" UNION "
				 +" select S.SITE_NAME,DE.DC_ENTRY_ID,DE.VENDOR_ID,DE.SITE_ID,VD.VENDOR_NAME,DE.DC_DATE,DE.TOTAL_AMOUNT,DE.RECEIVED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT DF.PRODUCT_NAME FROM DC_FORM DF where DF.DC_ENTRY_ID=DE.DC_ENTRY_ID group by DF.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.DC_NUMBER) from DC_FORM DF3,SUMADHURA_PRICE_LIST SPL3 where DE.DC_ENTRY_ID=DF3.DC_ENTRY_ID and DF3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by DE.DC_ENTRY_ID) as INVOICE_NUMBER,'DC' as IS_INV_OR_DC,DE.RECEIVED_DATE as ORDER_BY_DATE "
				 +" from DC_ENTRY DE "
				 +" left outer join SITE S on S.SITE_ID=DE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=DE.VENDOR_ID "
				 +" where DE.SITE_ID in('"+siteIds+"') and DE.INDENT_ENTRY_ID is null and DE.DC_ENTRY_ID "
				 +" in "
				 +" (select DE2.DC_ENTRY_ID "
				 +" from DC_ENTRY DE2,DC_FORM DF2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where DE2.DC_ENTRY_ID=DF2.DC_ENTRY_ID and DF2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='"+purchaseType+"' "
				 +" group by DE2.DC_ENTRY_ID) "
				 +" and DE.RECEIVED_DATE between to_date('"+fromDate+"','dd-mm-yy') and to_date('"+toDate+"','dd-mm-yy') ) order by ORDER_BY_DATE asc";

			}
			else if (StringUtils.isNotBlank(fromDate)) {
				query="(select S.SITE_NAME,IE.INDENT_ENTRY_ID,IE.VENDOR_ID,IE.SITE_ID,VD.VENDOR_NAME,IE.INVOICE_DATE,IE.TOTAL_AMOUNT,IE.RECEIVED_OR_ISSUED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT IED.PRODUCT_NAME FROM INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID group by IED.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.INVOICE_NUMBER) from INDENT_ENTRY_DETAILS IED3,SUMADHURA_PRICE_LIST SPL3 where IE.INDENT_ENTRY_ID=IED3.INDENT_ENTRY_ID and IED3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by IE.INDENT_ENTRY_ID) as INVOICE_NUMBER,'INV' as IS_INV_OR_DC,IE.RECEIVED_OR_ISSUED_DATE as ORDER_BY_DATE "
				 +" from INDENT_ENTRY IE "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" where IE.SITE_ID in('"+siteIds+"') AND IE.INDENT_TYPE in ('IN','INO') and IE.INDENT_ENTRY_ID "
				 +" in "
				 +" (select IE2.INDENT_ENTRY_ID "
				 +" from INDENT_ENTRY IE2,INDENT_ENTRY_DETAILS IED2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where IE2.INDENT_ENTRY_ID=IED2.INDENT_ENTRY_ID and IED2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='"+purchaseType+"' "
				 +" group by IE2.INDENT_ENTRY_ID) and IE.RECEIVED_OR_ISSUED_DATE >= TO_DATE('"+fromDate+"','dd-mm-yy') "
				 +" UNION "
				 +" select S.SITE_NAME,DE.DC_ENTRY_ID,DE.VENDOR_ID,DE.SITE_ID,VD.VENDOR_NAME,DE.DC_DATE,DE.TOTAL_AMOUNT,DE.RECEIVED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT DF.PRODUCT_NAME FROM DC_FORM DF where DF.DC_ENTRY_ID=DE.DC_ENTRY_ID group by DF.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.DC_NUMBER) from DC_FORM DF3,SUMADHURA_PRICE_LIST SPL3 where DE.DC_ENTRY_ID=DF3.DC_ENTRY_ID and DF3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by DE.DC_ENTRY_ID) as INVOICE_NUMBER,'DC' as IS_INV_OR_DC,DE.RECEIVED_DATE as ORDER_BY_DATE "
				 +" from DC_ENTRY DE "
				 +" left outer join SITE S on S.SITE_ID=DE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=DE.VENDOR_ID "
				 +" where DE.SITE_ID in('"+siteIds+"') and DE.INDENT_ENTRY_ID is null and DE.DC_ENTRY_ID "
				 +" in "
				 +" (select DE2.DC_ENTRY_ID "
				 +" from DC_ENTRY DE2,DC_FORM DF2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where DE2.DC_ENTRY_ID=DF2.DC_ENTRY_ID and DF2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='"+purchaseType+"' "
				 +" group by DE2.DC_ENTRY_ID) "
				 +" and DE.RECEIVED_DATE >= TO_DATE('"+fromDate+"','dd-mm-yy')) order by ORDER_BY_DATE asc";

			}else if(StringUtils.isNotBlank(toDate)) {
				query="(select S.SITE_NAME,IE.INDENT_ENTRY_ID,IE.VENDOR_ID,IE.SITE_ID,VD.VENDOR_NAME,IE.INVOICE_DATE,IE.TOTAL_AMOUNT,IE.RECEIVED_OR_ISSUED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT IED.PRODUCT_NAME FROM INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID group by IED.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.INVOICE_NUMBER) from INDENT_ENTRY_DETAILS IED3,SUMADHURA_PRICE_LIST SPL3 where IE.INDENT_ENTRY_ID=IED3.INDENT_ENTRY_ID and IED3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by IE.INDENT_ENTRY_ID) as INVOICE_NUMBER,'INV' as IS_INV_OR_DC,IE.RECEIVED_OR_ISSUED_DATE as ORDER_BY_DATE "
				 +" from INDENT_ENTRY IE "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" where IE.SITE_ID in('"+siteIds+"') AND IE.INDENT_TYPE in ('IN','INO') and IE.INDENT_ENTRY_ID "
				 +" in "
				 +" (select IE2.INDENT_ENTRY_ID "
				 +" from INDENT_ENTRY IE2,INDENT_ENTRY_DETAILS IED2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where IE2.INDENT_ENTRY_ID=IED2.INDENT_ENTRY_ID and IED2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='"+purchaseType+"' "
				 +" group by IE2.INDENT_ENTRY_ID) and IE.RECEIVED_OR_ISSUED_DATE <= TO_DATE('"+toDate+"','dd-mm-yy') "
				 +" UNION "
				 +" select S.SITE_NAME,DE.DC_ENTRY_ID,DE.VENDOR_ID,DE.SITE_ID,VD.VENDOR_NAME,DE.DC_DATE,DE.TOTAL_AMOUNT,DE.RECEIVED_DATE,"
				 +" (SELECT LISTAGG(PRODUCT_NAME, ', ') WITHIN GROUP (ORDER BY PRODUCT_NAME) FROM(SELECT DF.PRODUCT_NAME FROM DC_FORM DF where DF.DC_ENTRY_ID=DE.DC_ENTRY_ID group by DF.PRODUCT_NAME)) as Product_Listing ,"
				 +" (select min(SPL3.DC_NUMBER) from DC_FORM DF3,SUMADHURA_PRICE_LIST SPL3 where DE.DC_ENTRY_ID=DF3.DC_ENTRY_ID and DF3.PRICE_ID=SPL3.PRICE_ID "
				 +" group by DE.DC_ENTRY_ID) as INVOICE_NUMBER,'DC' as IS_INV_OR_DC,DE.RECEIVED_DATE as ORDER_BY_DATE "
				 +" from DC_ENTRY DE "
				 +" left outer join SITE S on S.SITE_ID=DE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=DE.VENDOR_ID "
				 +" where DE.SITE_ID in('"+siteIds+"') and DE.INDENT_ENTRY_ID is null and DE.DC_ENTRY_ID "
				 +" in "
				 +" (select DE2.DC_ENTRY_ID "
				 +" from DC_ENTRY DE2,DC_FORM DF2,SUMADHURA_PRICE_LIST SPL2 "
				 +" where DE2.DC_ENTRY_ID=DF2.DC_ENTRY_ID and DF2.PRICE_ID=SPL2.PRICE_ID and SPL2.TYPE_OF_PURCHASE='marketPurchase' "
				 +" group by DE2.DC_ENTRY_ID) "
				 +" and DE.RECEIVED_DATE <= TO_DATE('"+toDate+"','dd-mm-yy')) order by ORDER_BY_DATE asc";

			}
			List<Map<String, Object>> dbProductDts = jdbcTemplate.queryForList(query, new Object[] {});
			if(dbProductDts!=null && dbProductDts.size()>0){
				for(Map<String, Object> map:dbProductDts){
					
					productDetails = new ProductDetails();
					//productDetails.setStrSerialNumber(String.valueOf(sno));
					productDetails.setSiteName(map.get("SITE_NAME")==null ? "-" : map.get("SITE_NAME").toString());
					productDetails.setIndentEntryId(map.get("INDENT_ENTRY_ID")==null ? "" : map.get("INDENT_ENTRY_ID").toString());
					productDetails.setVendorName(map.get("VENDOR_NAME")==null ? "-" : map.get("VENDOR_NAME").toString());
					productDetails.setDc_or_Invoice(map.get("IS_INV_OR_DC")==null ? "-" : map.get("IS_INV_OR_DC").toString());
					productDetails.setInvoiceNumber(map.get("INVOICE_NUMBER")==null ? "" : map.get("INVOICE_NUMBER").toString());
					getInvoiceDateGrn=invoiceDate=(map.get("INVOICE_DATE")==null ? map.get("RECEIVED_OR_ISSUED_DATE")==null ? "-" : map.get("RECEIVED_OR_ISSUED_DATE").toString() : map.get("INVOICE_DATE").toString());
					productDetails.setMaterialDesc(map.get("Product_Listing")==null ? "-" : map.get("Product_Listing").toString());
					grnDate=(map.get("RECEIVED_OR_ISSUED_DATE")==null ? "-" : map.get("RECEIVED_OR_ISSUED_DATE").toString());
					
					invoiceAmount=Double.valueOf(map.get("TOTAL_AMOUNT")==null ? "-" : map.get("TOTAL_AMOUNT").toString());
					productDetails.setSite_Id(map.get("SITE_ID")==null ? "-" : map.get("SITE_ID").toString());
					productDetails.setVendorId(map.get("VENDOR_ID")==null ? "-" : map.get("VENDOR_ID").toString());
					
					SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					try{
						if(!invoiceDate.equals("-")){Date afterIndentDate  = myFormat.parse(invoiceDate);
						invoiceDate = outputFormat.format(afterIndentDate);
						}if(!grnDate.equals("-")){Date afterPoDate  = myFormat.parse(grnDate);
						grnDate=outputFormat.format(afterPoDate);
						}if(!getInvoiceDateGrn.equals("-")){
							Date invoice_date = myFormat.parse(getInvoiceDateGrn);
							getInvoiceDateGrn = dt1.format(invoice_date);
							}
						}catch(Exception e){
						
					}
					productDetails.setReceiveDate(invoiceDate);
					productDetails.setGrnDate(grnDate);
					productDetails.setInvoiceDate(getInvoiceDateGrn);
					productDetails.setInvoiceAmount(String.valueOf(invoiceAmount));
					invoiceGrandTotal=invoiceGrandTotal+invoiceAmount;
					//mapgroupbyid.put(indent_Entry_Id,productDetails);
					list.add(productDetails);
				}
				BigDecimal bigDecimalInvoiceTotal = new BigDecimal(invoiceGrandTotal);
				request.setAttribute("Invoice_GrandTotal",String.valueOf(bigDecimalInvoiceTotal.setScale(2,RoundingMode.CEILING)));// or grand total purpose
			}
		return list;

}
	/**********************************************************Market Purchase details End*******************************************************************/	
	/**********************************************************Local Purchase details start*******************************************************************/
	
	/**********************************************************Local Purchase details End*******************************************************************/	

/*=====================================================MIS Report Vendor Details Start=====================================================*/
public List<ProductDetails> getVendorPaymentDetails(HttpServletRequest request,String fromDate,String toDate){
		
		//List<String> list = new ArrayList<String>();
		List<ProductDetails> list = new ArrayList<ProductDetails>();
		Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
		ProductDetails productDetails =null;
		String query="";
		String invoiceDate="";
		String poInitiateDate="";
		String paymentDoneDate="";
		String pendingEmp="";
		String creationDate="";
		String payment_Done_Upto="";
		String balance_Amount="";
		String payment_Req_Amt="";
		String advance_Amt="";
		String pending_EmpName="";
		String siteIds=request.getParameter("siteIds");
		siteIds=siteIds.replace(",","','");
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
			 query="select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE,APD.CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" AP.PAYMENT_DONE_UPTO,TO_CHAR(IE.TOTAL_AMOUNT-AP.PAYMENT_DONE_UPTO-AP.PAYMENT_REQ_UPTO) as BALANCE_AMOUNT,AP.PAYMENT_REQ_UPTO,APD.REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" (case when ATPT.ADJUST_AMOUNT_FROM_ADVANCE is null then APD.ADJUST_AMOUNT_FROM_ADVANCE else ATPT.ADJUST_AMOUNT_FROM_ADVANCE END) as ADJUSTED_ADVANCE,"
				 +" (case when ATPT.REQUEST_PENDING_EMP_ID is null then "
				 +" (case when APD.REQUEST_PENDING_EMP_ID='-' then APD.REQUEST_PENDING_DEPT_ID else SED.EMP_NAME END) "
				 +" else ATPT.REQUEST_PENDING_EMP_ID "
				 +" END) as PENDING_EMPLOYEE"

				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" ,ACC_PAYMENT AP "
				 +" left outer join ACC_PAYMENT_DTLS APD left outer join ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID=ATPT.PAYMENT_DETAILS_ID and ATPT.STATUS='A'" 
				 +" left outer join SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID=APD.REQUEST_PENDING_EMP_ID "
				 +" on AP.PAYMENT_ID = APD.PAYMENT_ID and APD.STATUS='A' "
				 +" where IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
				 +" and IE.INDENT_TYPE='IN' and AP.STATUS='A' "
				 +" and (ATPT.REQUEST_PENDING_EMP_ID is null or (ATPT.REQUEST_PENDING_EMP_ID is not null and ATPT.REQUEST_PENDING_EMP_ID !='VND')) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE between to_date('"+fromDate+"','dd-mm-yy') and to_date('"+toDate+"','dd-mm-yy') "

				 +" UNION "
				 +" select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE"
				 +" ,null as CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" null as PAYMENT_DONE_UPTO,IE.TOTAL_AMOUNT as BALANCE_AMOUNT,null as PAYMENT_REQ_UPTO,null as REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" null as ADJUSTED_ADVANCE,"
				 +" null as PENDING_EMPLOYEE"
				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "

				 +" where IE.INDENT_TYPE='IN' and IE.INDENT_ENTRY_ID not in (select AP.INDENT_ENTRY_ID from ACC_PAYMENT AP) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE between to_date('"+fromDate+"','dd-mm-yy') and to_date('"+toDate+"','dd-mm-yy')";

			}
			else if (StringUtils.isNotBlank(fromDate)) {
				query="select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE,APD.CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" AP.PAYMENT_DONE_UPTO,TO_CHAR(IE.TOTAL_AMOUNT-AP.PAYMENT_DONE_UPTO-AP.PAYMENT_REQ_UPTO) as BALANCE_AMOUNT,AP.PAYMENT_REQ_UPTO,APD.REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" (case when ATPT.ADJUST_AMOUNT_FROM_ADVANCE is null then APD.ADJUST_AMOUNT_FROM_ADVANCE else ATPT.ADJUST_AMOUNT_FROM_ADVANCE END) as ADJUSTED_ADVANCE,"
				 +" (case when ATPT.REQUEST_PENDING_EMP_ID is null then "
				 +" (case when APD.REQUEST_PENDING_EMP_ID='-' then APD.REQUEST_PENDING_DEPT_ID else SED.EMP_NAME END) "
				 +" else ATPT.REQUEST_PENDING_EMP_ID "
				 +" END) as PENDING_EMPLOYEE"

				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" ,ACC_PAYMENT AP "
				 +" left outer join ACC_PAYMENT_DTLS APD left outer join ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID=ATPT.PAYMENT_DETAILS_ID and ATPT.STATUS='A'" 
				 +" left outer join SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID=APD.REQUEST_PENDING_EMP_ID "
				 +" on AP.PAYMENT_ID = APD.PAYMENT_ID and APD.STATUS='A' "
				 +" where IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
				 +" and IE.INDENT_TYPE='IN' and AP.STATUS='A' "
				 +" and (ATPT.REQUEST_PENDING_EMP_ID is null or (ATPT.REQUEST_PENDING_EMP_ID is not null and ATPT.REQUEST_PENDING_EMP_ID !='VND')) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE >= TO_DATE('"+fromDate+"','dd-mm-yy')"

				 +" UNION "
				 +" select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE"
				 +" ,null as CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" null as PAYMENT_DONE_UPTO,IE.TOTAL_AMOUNT as BALANCE_AMOUNT,null as PAYMENT_REQ_UPTO,null as REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" null as ADJUSTED_ADVANCE,"
				 +" null as PENDING_EMPLOYEE"
				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "

				 +" where IE.INDENT_TYPE='IN' and IE.INDENT_ENTRY_ID not in (select AP.INDENT_ENTRY_ID from ACC_PAYMENT AP) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE >= TO_DATE('"+fromDate+"','dd-mm-yy')";
				
			}else if(StringUtils.isNotBlank(toDate)) {
				query="select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE,APD.CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" AP.PAYMENT_DONE_UPTO,TO_CHAR(IE.TOTAL_AMOUNT-AP.PAYMENT_DONE_UPTO-AP.PAYMENT_REQ_UPTO) as BALANCE_AMOUNT,AP.PAYMENT_REQ_UPTO,APD.REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" (case when ATPT.ADJUST_AMOUNT_FROM_ADVANCE is null then APD.ADJUST_AMOUNT_FROM_ADVANCE else ATPT.ADJUST_AMOUNT_FROM_ADVANCE END) as ADJUSTED_ADVANCE,"
				 +" (case when ATPT.REQUEST_PENDING_EMP_ID is null then "
				 +" (case when APD.REQUEST_PENDING_EMP_ID='-' then APD.REQUEST_PENDING_DEPT_ID else SED.EMP_NAME END) "
				 +" else ATPT.REQUEST_PENDING_EMP_ID "
				 +" END) as PENDING_EMPLOYEE"

				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
				 +" ,ACC_PAYMENT AP "
				 +" left outer join ACC_PAYMENT_DTLS APD left outer join ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID=ATPT.PAYMENT_DETAILS_ID and ATPT.STATUS='A'" 
				 +" left outer join SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID=APD.REQUEST_PENDING_EMP_ID "
				 +" on AP.PAYMENT_ID = APD.PAYMENT_ID and APD.STATUS='A' "
				 +" where IE.INDENT_ENTRY_ID=AP.INDENT_ENTRY_ID "
				 +" and IE.INDENT_TYPE='IN' and AP.STATUS='A' "
				 +" and (ATPT.REQUEST_PENDING_EMP_ID is null or (ATPT.REQUEST_PENDING_EMP_ID is not null and ATPT.REQUEST_PENDING_EMP_ID !='VND')) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE <= TO_DATE('"+fromDate+"','dd-mm-yy') "

				 +" UNION "
				 +" select S.SITE_NAME,VD.VENDOR_NAME,IE.SITE_ID,IE.VENDOR_ID,IE.PO_ID,IE.INVOICE_ID,IE.INVOICE_DATE"
				 +" ,null as CREATION_DATE,IE.TOTAL_AMOUNT,IE.INDENT_ENTRY_ID, "
				 +" null as PAYMENT_DONE_UPTO,IE.TOTAL_AMOUNT as BALANCE_AMOUNT,null as PAYMENT_REQ_UPTO,null as REQ_AMOUNT,"
				 +" (AAPP.PAID_AMOUNT-AAPP.ADJUSTED_AMOUNT-AAPP.INTIATED_AMOUNT) as ADVANCE_AMOUNT,"
				 +" null as ADJUSTED_ADVANCE,"
				 +" null as PENDING_EMPLOYEE"
				 +" from INDENT_ENTRY IE "
				 +" left outer join ACC_ADVANCE_PAYMENT_PO AAPP on AAPP.PO_NUMBER=IE.PO_ID "
				 +" left outer join SITE S on S.SITE_ID=IE.SITE_ID "
				 +" left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "

				 +" where IE.INDENT_TYPE='IN' and IE.INDENT_ENTRY_ID not in (select AP.INDENT_ENTRY_ID from ACC_PAYMENT AP) "
				 +" and IE.SITE_ID in('"+siteIds+"') and IE.INVOICE_DATE <= TO_DATE('"+toDate+"','dd-mm-yy')";

			}
			List<Map<String, Object>> dbProductDts = jdbcTemplate.queryForList(query, new Object[] {});
			if(dbProductDts!=null && dbProductDts.size()>0){
				for(Map<String, Object> map:dbProductDts){
					
					productDetails = new ProductDetails();
					productDetails.setSiteName(map.get("SITE_NAME")==null ? "-" : map.get("SITE_NAME").toString());
					productDetails.setVendorName(map.get("VENDOR_NAME")==null ? "-" : map.get("VENDOR_NAME").toString());
					productDetails.setStrPONumber(map.get("PO_ID")==null ? "" : map.get("PO_ID").toString());
					productDetails.setInvoiceNumber(map.get("INVOICE_ID")==null ? "-" : map.get("INVOICE_ID").toString());
					invoiceDate=(map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString());
					creationDate=(map.get("CREATION_DATE")==null ? "-" : map.get("CREATION_DATE").toString());
					productDetails.setInvoiceAmount(map.get("TOTAL_AMOUNT")==null ? "0" : map.get("TOTAL_AMOUNT").toString());
					productDetails.setPayment_Done_Upto(map.get("PAYMENT_DONE_UPTO")==null ? "0" : map.get("PAYMENT_DONE_UPTO").toString());
					productDetails.setBalance_Amount(map.get("BALANCE_AMOUNT")==null ? "0" : map.get("BALANCE_AMOUNT").toString());
					productDetails.setPayment_Req_Amt(map.get("REQ_AMOUNT")==null ? "0" : map.get("REQ_AMOUNT").toString());
					productDetails.setAdvance_Amt(map.get("ADVANCE_AMOUNT")==null ? "0" : map.get("ADVANCE_AMOUNT").toString());
					productDetails.setAdjustedAdvance(map.get("ADJUSTED_ADVANCE")==null ? "0" : map.get("ADJUSTED_ADVANCE").toString());
					productDetails.setSite_Id(map.get("SITE_ID")==null ? "-" : map.get("SITE_ID").toString());
					productDetails.setVendorId(map.get("VENDOR_ID")==null ? "-" : map.get("VENDOR_ID").toString());
					productDetails.setIndentEntryId(map.get("INDENT_ENTRY_ID")==null ? "" : map.get("INDENT_ENTRY_ID").toString());
					productDetails.setPending_Emp(map.get("PENDING_EMPLOYEE") == null ? "-" : (map.get("PENDING_EMPLOYEE").toString().contains("997") ? "Accounts Level "+map.get("PENDING_EMPLOYEE").toString().charAt(map.get("PENDING_EMPLOYEE").toString().length()-1) : map.get("PENDING_EMPLOYEE").toString()));
					SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
					try{
						if(!invoiceDate.equals("-")){Date afterInvoiceDate  = myFormat.parse(invoiceDate);
						invoiceDate = outputFormat.format(afterInvoiceDate);
						}if(!creationDate.equals("-")){Date afterCreationDate  = myFormat.parse(creationDate);
						creationDate=outputFormat.format(afterCreationDate);
						}
						}catch(Exception e){
						
					}
					productDetails.setReceiveDate(invoiceDate);
					productDetails.setStrCreateDate(creationDate);
					list.add(productDetails);
				}
				
			}
		return list;

}
	/*=====================================================MIS Report Vendor Details End=====================================================*/

/*	****************************************************to get View MIS SiteLevel PO Report data************************************************************/
public List<ProductDetails> getSiteLevelPoDetails(HttpServletRequest request,String fromDate,String toDate){
	
	List<String> list = new ArrayList<String>();
	List<ProductDetails> prod = new ArrayList<ProductDetails>();
	Map<String, ProductDetails> mapgroupbyid= new HashMap<String, ProductDetails>();
	Set <String> po_Total = new HashSet <String>();
	ProductDetails productDetails =null;
	String query="";
	String invoiceNumber="";
	String strMaterialName="";
	double po_Amount=0.0;
	String siteName="";
	String indentNumber="";
	String poNumber="";
	String poDate="";
	String indentDate="";
	String vedorName="";
	String receiveDate="";
	String invoiceAmt="";
	
	String indent_Entry_Id="";
	String grnDate="";
	String old_IndentEntryId="";
	double poTotal=0.0;
	double invoiceTotal=0.0;
	String siteIds=request.getParameter("siteIds");
	siteIds=siteIds.replace(",","','");
	String siteWiseIndent="";
	String site_Id="";
	String vendor_Id="";
	String invoiceDate="";
	if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
	 query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,SPE.PO_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
		 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,IE.INVOICE_DATE"
		 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE" 
		 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED "
		 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
		 +" GROUP BY "
		 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT  order by SPE.PO_DATE ASC),"
		 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
		 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
		 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
		 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
		 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
		 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY!='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')order by SPE.PO_NUMBER ASC";

	}
	else if (StringUtils.isNotBlank(fromDate)) {
		query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,SPE.PO_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
		 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,IE.INVOICE_DATE"
		 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE" 
		 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED "
		 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
		 +" GROUP BY "
		 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT  order by SPE.PO_DATE ASC),"
		 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
		 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
		 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
		 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
		 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
		 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY!='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) >= TO_DATE('"+fromDate+"', 'dd-MM-yy') order by SPE.PO_NUMBER ASC";

	}else if(StringUtils.isNotBlank(toDate)) {
		query="SELECT DISTINCT S.SITE_NAME,S.SITE_ID,VD.VENDOR_ID,SIC.INDENT_CREATION_ID,SIC.SITEWISE_INDENT_NO,SIC.CREATE_DATE AS INDENT_DATE,SPE.PO_DATE,SPE.PO_NUMBER,VD.VENDOR_NAME,PO_TOTAL,"
		 +" DESC_MATERAILS,IE.INVOICE_ID,IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE AS RECEIVED_DATE,IE.TOTAL_AMOUNT AS INVOICE_AMOUNT,IE.INVOICE_DATE"
		 +" FROM (SELECT   P.NAME as DESC_MATERAILS,SPE.TOTAL_AMOUNT AS PO_TOTAL,SPE.PO_NUMBER AS TEMP, SPE.PO_DATE" 
		 +" FROM SUMADHURA_PO_ENTRY SPE ,PRODUCT P,SUMADHURA_PO_ENTRY_DETAILS SPED "
		 +" where  SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  and P.PRODUCT_ID=SPED.PRODUCT_ID "
		 +" GROUP BY "
		 +" P.NAME, SPE.PO_NUMBER, SPED.PRODUCT_ID,SPE.PO_DATE,SPED.CHILD_PRODUCT_ID,SPE.TOTAL_AMOUNT  order by SPE.PO_DATE ASC),"
		 +" SUMADHURA_PO_ENTRY SPE INNER JOIN SUMADHURA_INDENT_CREATION SIC ON SPE.INDENT_NO=SIC.INDENT_CREATION_ID " 
		 +" LEFT OUTER JOIN SITE S ON SPE.SITE_ID=S.SITE_ID"
		 +" LEFT OUTER JOIN SUMADHURA_TEMP_PO_ENTRY STPE ON SPE.INDENT_NO=STPE.INDENT_NO AND STPE.VENDOR_ID=SPE.VENDOR_ID"
		 +" LEFT OUTER JOIN VENDOR_DETAILS VD ON SPE.VENDOR_ID=VD.VENDOR_ID"
		 +" LEFT OUTER JOIN INDENT_ENTRY IE ON SPE.PO_NUMBER=IE.PO_ID"
		 +" WHERE SPE.PO_STATUS not in ('REVISED','UPDATE','CNL') AND SPE.PREPARED_BY!='PURCHASE_DEPT' AND SPE.SITE_ID IN('"+siteIds+"') AND SPE.PO_NUMBER=TEMP AND TRUNC(SPE.PO_DATE) <= TO_DATE('"+toDate+"', 'dd-MM-yy') order by SPE.PO_NUMBER ASC";


	}
	List<Map<String, Object>> dbPoDts = jdbcTemplate.queryForList(query, new Object[] {});
	java.util.Iterator<Map<String, Object>>  itr = dbPoDts.iterator();
	int sno=1;
	while(itr.hasNext()){
		Map<String, Object>  map = itr.next();
		//sno++;
		indent_Entry_Id=(map.get("INDENT_ENTRY_ID")==null ? "IE_ID"+(sno++) : map.get("INDENT_ENTRY_ID").toString());// if empty value coming key set to id to 1
		invoiceNumber=(map.get("INVOICE_ID")==null ? "" : map.get("INVOICE_ID").toString());
		strMaterialName=map.get("DESC_MATERAILS")==null ? "" : map.get("DESC_MATERAILS").toString();
		po_Amount=Double.valueOf(map.get("PO_TOTAL")==null ? "0" : map.get("PO_TOTAL").toString());
		BigDecimal bigDecimaltemp_PoTotal = new BigDecimal(po_Amount);
		po_Amount=Double.valueOf(String.valueOf(bigDecimaltemp_PoTotal.setScale(2,RoundingMode.CEILING)));
		siteName=(map.get("SITE_NAME")==null ? "" : map.get("SITE_NAME").toString());
		indentNumber=(map.get("INDENT_CREATION_ID")==null ? "" : map.get("INDENT_CREATION_ID").toString());
		siteWiseIndent=(map.get("SITEWISE_INDENT_NO")==null ? "" : map.get("SITEWISE_INDENT_NO").toString());
		indentDate=(map.get("INDENT_DATE")==null ? "" : map.get("INDENT_DATE").toString());
		poDate=(map.get("PO_DATE")==null ? "-" : map.get("PO_DATE").toString());
		
		poNumber=(map.get("PO_NUMBER")==null ? "" : map.get("PO_NUMBER").toString());
		vedorName=(map.get("VENDOR_NAME")==null ? "" : map.get("VENDOR_NAME").toString());
		receiveDate=(map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString());
		grnDate=(map.get("RECEIVED_DATE")==null ? "-" : map.get("RECEIVED_DATE").toString());
		invoiceAmt=(map.get("INVOICE_AMOUNT")==null ? "0" : map.get("INVOICE_AMOUNT").toString());
		site_Id=(map.get("SITE_ID")==null ? "-" : map.get("SITE_ID").toString());
		vendor_Id=(map.get("VENDOR_ID")==null ? "-" : map.get("VENDOR_ID").toString());
		invoiceDate=(map.get("INVOICE_DATE")==null ? "-" : map.get("INVOICE_DATE").toString());
		
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
		DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
		try{
		//if(!deliveryStatus.equals("-") && grnDate.equals("-")){deliveryStatus="-";}
			if(!indentDate.equals("-")){
				Date afterIndentDate  = myFormat.parse(indentDate);
				indentDate=outputFormat.format(afterIndentDate);
			}if(!poDate.equals("-")){
				Date afterPoDate  = myFormat.parse(poDate);
				poDate=outputFormat.format(afterPoDate);
			}
		if(!receiveDate.equals("-")){Date afterReceiveDate  = myFormat.parse(receiveDate);
		receiveDate=outputFormat.format(afterReceiveDate);
		}if(!grnDate.equals("-")){
			Date afterGrnDate  = myFormat.parse(grnDate);
			grnDate=outputFormat.format(afterGrnDate);
		}if(!invoiceDate.equals("-")){
			Date invoice_date = myFormat.parse(invoiceDate);
			invoiceDate = dt1.format(invoice_date);
			}
		
		}catch(Exception e){
			
		}
		//System.out.println("compare"+compare);
		
		if(list.contains(indent_Entry_Id)){
			mapgroupbyid.get(indent_Entry_Id).setMaterialDesc(mapgroupbyid.get(indent_Entry_Id).getMaterialDesc()+","+strMaterialName);
			
			
		}else if(list.contains(poNumber) && !list.contains(indent_Entry_Id) &&  list.contains(old_IndentEntryId) && old_IndentEntryId.contains("IE_ID")){
			//if(old_IndentEntryId.contains("IE_ID")){
			mapgroupbyid.get(old_IndentEntryId).setMaterialDesc(mapgroupbyid.get(old_IndentEntryId).getMaterialDesc()+","+strMaterialName);
			//}
		}
		else{
			
			productDetails = new ProductDetails();
			//productDetails.setStrSerialNumber(String.valueOf(sno));
			productDetails.setSiteName(siteName);
			productDetails.setIndentNo(indentNumber);
			productDetails.setSiteWiseIndentNo(siteWiseIndent);
			productDetails.setIndentDate(indentDate);
			productDetails.setPoDate(poDate);
			productDetails.setStrPONumber(poNumber);
			productDetails.setVendorName(vedorName);
			productDetails.setPoTotal(po_Amount);
			productDetails.setMaterialDesc(strMaterialName);
			productDetails.setReceiveDate(receiveDate);
			productDetails.setInvoiceNumber(invoiceNumber);
			productDetails.setInvoiceAmount(invoiceAmt);
			productDetails.setIndentEntryId(indent_Entry_Id);
			productDetails.setGrnDate(grnDate);
			productDetails.setSite_Id(site_Id);
			productDetails.setVendorId(vendor_Id);
			productDetails.setInvoiceDate(invoiceDate);
			
			mapgroupbyid.put(indent_Entry_Id,productDetails);
			old_IndentEntryId=indent_Entry_Id;
			list.add(indent_Entry_Id);
			list.add(strMaterialName);
			list.add(poNumber);
			if(po_Total.add(poNumber)){poTotal=poTotal+Double.valueOf(po_Amount);}
			invoiceTotal=invoiceTotal+Double.valueOf(invoiceAmt);
			
			
			}
		}
	
	BigDecimal bigDecimalPoTotal = new BigDecimal(poTotal);
	BigDecimal bigDecimalInvoiceTotal = new BigDecimal(invoiceTotal);
	request.setAttribute("Po_GrandTotal",String.valueOf(bigDecimalPoTotal.setScale(2,RoundingMode.CEILING)));
	request.setAttribute("Invoice_GrandTotal",String.valueOf(bigDecimalInvoiceTotal.setScale(2,RoundingMode.CEILING)));// or grand total purpose
	
	Set set = mapgroupbyid.entrySet();
	java.util.Iterator i = set.iterator();
	while(i.hasNext()) {
		Map.Entry me = (Map.Entry)i.next();
		ProductDetails checkingBean = (ProductDetails) me.getValue();
		prod.add(checkingBean);
		}

	return prod;
}

/*=================================================================to get the comments for po======================================================*/	
	@Override
	public String getPoLevelComments(String poNumber,String FinalPoOrNot) { // this is for final po view time finalpoOrNot

		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String StrPoLevelComments = "";
		if(FinalPoOrNot.equalsIgnoreCase("true")){
			String sql="select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID='"+poNumber+"'";
			String tempPoNumber = jdbcTemplate.queryForObject(sql, String.class);
			poNumber=tempPoNumber;
		}
		String query = "select SPCAD.PURPOSE,SED.EMP_NAME from SUMADHURA_PO_CRT_APPRL_DTLS SPCAD,SUMADHURA_EMPLOYEE_DETAILS SED " +
								" where SED.EMP_ID =SPCAD.PO_CREATE_APPROVE_EMP_ID AND SPCAD.TEMP_PO_NUMBER=? order by SPCAD.PO_CREATION_APPROVAL_DTLS_ID";
		getcommentDtls = jdbcTemplate.queryForList(query,new Object[] { poNumber });

		if (getcommentDtls != null && getcommentDtls.size() > 0) {
			for (Map<String, Object> prods : getcommentDtls) {
				strEmployeName = prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString();
				strComments = prods.get("PURPOSE") == null ? "" : prods.get("PURPOSE").toString();
				if ((strEmployeName != null && strComments != null)&& (!strEmployeName.equals("") && !strComments.equals(""))) {

					StrPoLevelComments +=strComments + " ,";
				}

			}
			if (StrPoLevelComments != null && !StrPoLevelComments.equals("")) {
				StrPoLevelComments = StrPoLevelComments.substring(0,StrPoLevelComments.length() - 1);
			}

		}

		return StrPoLevelComments;

	}

	/*======================================================get siteWise data start=========================================================*/
	@Override
	public List<IndentCreationBean> ViewSiteTempPo(String fromDate, String toDate,String tempPoNumber,String siteId) {

		String query = "";
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbMarketingPODts = null;
		IndentCreationBean indentObj = null; 
		String sql="";
		String type_Of_Purchase="";
		String operationType="";
		String purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();	
		String marketi_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			//	template = new JdbcTemplate(DBConnection.getDbConnection());
			if(siteId.equals(marketi_Dept_Id)){
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					//query = " SELECT DISTINCT (PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_EMPLOYEE_DETAILS SED1,SITE S,SUMADHURA_INDENT_CREATION SIC WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
					
					sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					//query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
					
					sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,STPE.OLD_PO_NUMBER,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";


				} else if(StringUtils.isNotBlank(toDate)) {
					//query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  =TO_DATE('"+toDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
					
					sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,STPE.OLD_PO_NUMBER,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";

				}
				else if(StringUtils.isNotBlank(tempPoNumber)) {
					//query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY!='MARKETING_DEPT'";
					
					sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,PO_DATE,STPE.OLD_PO_NUMBER,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY='MARKETING_DEPT'";
				}
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				
			}
			if(siteId.equals(purchase_Dept_Id)){
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = " SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_EMPLOYEE_DETAILS SED1,SITE S,SUMADHURA_INDENT_CREATION SIC WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				//sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					//+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				//sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					//+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";


			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  =TO_DATE('"+toDate+"', 'dd-MM-yy') and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				//sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					//+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";

			}
			else if(StringUtils.isNotBlank(tempPoNumber)) {
				query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY!='MARKETING_DEPT'";
				
				//sql="SELECT DISTINCT (PO_NUMBER),INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1 "
				//+" WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY='MARKETING_DEPT'";
			}
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
			}
			/************************************* site people can see the data start*************************************************/
			if(!siteId.equals(purchase_Dept_Id) && !siteId.equals(marketi_Dept_Id)){
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = " SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_EMPLOYEE_DETAILS SED1,SITE S,SUMADHURA_INDENT_CREATION SIC WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') and STPE.SITE_ID='"+siteId+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and STPE.SITE_ID='"+siteId+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
				
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and TRUNC(STPE.PO_DATE)  =TO_DATE('"+toDate+"', 'dd-MM-yy') and STPE.SITE_ID='"+siteId+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID";
					
				}
				else if(StringUtils.isNotBlank(tempPoNumber)) {
					query = "SELECT DISTINCT (PO_NUMBER),INDENT_NO,STPE.PREPARED_BY,STPE.OPERATION_TYPE,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,PO_DATE,SED.EMP_NAME,SITE_NAME,STPE.SITE_ID,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.PO_ENTRY_USER_ID=SED.EMP_ID AND STPE.PO_STATUS='A' and S.SITE_ID = STPE.SITE_ID and STPE.PO_NUMBER='"+tempPoNumber+"' and STPE.SITE_ID='"+siteId+"' and SIC.INDENT_CREATION_ID = INDENT_NO and STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND STPE.PREPARED_BY!='MARKETING_DEPT'";
				}
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				}

			// this is for marketing po purpose
			if(dbMarketingPODts!=null && dbMarketingPODts.size()>0 ){
			for(Map<String, Object> makrtDetails : dbMarketingPODts) {
				indentObj = new IndentCreationBean();
				indentObj.setPonumber(makrtDetails.get("PO_NUMBER")==null ? "-" : makrtDetails.get("PO_NUMBER").toString());
				indentObj.setToEmpName(makrtDetails.get("EMP_NAME")==null ? "" : makrtDetails.get("EMP_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(makrtDetails.get("INDENT_NO")==null ? "0" : makrtDetails.get("INDENT_NO").toString()));
				indentObj.setSiteName(makrtDetails.get("SITE_NAME")==null ? "0" : makrtDetails.get("SITE_NAME").toString());
				indentObj.setSiteId(Integer.parseInt(makrtDetails.get("SITE_ID")==null ? "0" : makrtDetails.get("SITE_ID").toString()));
				indentObj.setPending_Emp_Name(makrtDetails.get("PENDING_EMP_NAME")==null ? "0" : makrtDetails.get("PENDING_EMP_NAME").toString());
				//(makrtDetails.get("PREPARED_BY")==null ? "" : makrtDetails.get("PREPARED_BY").toString();
				operationType=(makrtDetails.get("OPERATION_TYPE")==null ? "" : makrtDetails.get("OPERATION_TYPE").toString());
				
				indentObj.setType_Of_Purchase("MARKETING DEPT");
				indentObj.setOld_Po_Number(makrtDetails.get("OLD_PO_NUMBER")==null ? "-" : makrtDetails.get("OLD_PO_NUMBER").toString());
				indentObj.setSiteWiseIndentNo(0);
				indentObj.setFromDate(fromDate);
				indentObj.setToDate(toDate);
				String date=makrtDetails.get("PO_DATE")==null ? "" : makrtDetails.get("PO_DATE").toString();
				if(operationType.equals("REVISED")){
					indentObj.setType_Of_Purchase("REVISED PO");
					
				}else{
					indentObj.setType_Of_Purchase("MARKETING DEPT");
				}
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
				
			}
			}
			if(dbIndentDts!=null && dbIndentDts.size()>0 ){
			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());

				//	indentObj.setStrRequiredDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setToEmpName(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
				indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "" : prods.get("INDENT_NO").toString()));
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "0" : prods.get("SITE_NAME").toString());
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
				indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "" : prods.get("SITEWISE_INDENT_NO").toString()));
				String date=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				indentObj.setPending_Emp_Name(prods.get("PENDING_EMP_NAME")==null ? "0" : prods.get("PENDING_EMP_NAME").toString());
				type_Of_Purchase=prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString();
				operationType=(prods.get("OPERATION_TYPE")==null ? "" : prods.get("OPERATION_TYPE").toString());
				indentObj.setOld_Po_Number(prods.get("OLD_PO_NUMBER")==null ? "-" : prods.get("OLD_PO_NUMBER").toString());
				indentObj.setFromDate(fromDate);
				indentObj.setToDate(toDate);
				if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("MARKETING DEPT");
				}else if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("REVISED PO");
				}
					else{
				
				if((type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("REVISED"))){
					
					indentObj.setType_Of_Purchase("REVISED PO");
				}else if(type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("UPDATE")){
					indentObj.setType_Of_Purchase("UPDATE PO");
				}
				else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("CREATION"))){
					
					indentObj.setType_Of_Purchase("PURCHASE DEPT PO");
				} else{
					
					indentObj.setType_Of_Purchase("SITELEVEL PO");
				}
				}
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			//	template = null;
			dbIndentDts = null;
		}
		return list;
	}

	/*=======================================================get site Wise Data End=========================================================*/
	
	/*=======================================================to check Whether done invoice Dc or Payment start=========================================================*/
	public String getCancelPOPendinfEmpId(String pendingEmpId,String cancelOrNot){
		
		String pending_Emp_Id="";
		List<Map<String, Object>> dbPendingEmpDts = null;

		String query = " select SPAMD.APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  SPAMD.EMP_ID= ? and SPAMD.STATUS = 'A' AND SPAMD.MODULE_TYPE=?";
		dbPendingEmpDts=jdbcTemplate.queryForList(query, new Object[] {pendingEmpId,cancelOrNot});

		if(dbPendingEmpDts!= null){
			for(Map<String, Object> prods : dbPendingEmpDts) {

				pending_Emp_Id =prods.get("APPROVER_EMP_ID")==null ? "" :  prods.get("APPROVER_EMP_ID").toString();
			}	
		} 
		return pending_Emp_Id;
	}

	public int inActivePOTable(String poNumber,String siteId){
	
	String query =  "update SUMADHURA_PO_ENTRY set PO_STATUS ='CNL' where PO_NUMBER = ? and SITE_ID = ? ";

	int result = jdbcTemplate.update(query, new Object[] {poNumber,siteId});
	return result;
	}
	
	
	
	public boolean checkPoDoneInvoiceOrDcPayment(String poNo, String siteId, String vendorId) {
		
		boolean returnStatus=false;
		try{
		String dcData="select DC_ENTRY_ID,DC_NUMBER FROM DC_ENTRY WHERE PO_ID=? AND SITE_ID=?";
		List<Map<String, Object>> dbDcDtls = jdbcTemplate.queryForList(dcData,new Object[]{ poNo,siteId});
		
		String sql="select INDENT_ENTRY_ID,INVOICE_ID FROM INDENT_ENTRY WHERE PO_ID=? AND SITE_ID=?";
		List<Map<String, Object>> dbInvoiceDtls = jdbcTemplate.queryForList(sql,new Object[]{ poNo,siteId});
		
		
		// this is for payment done or not check here
		if(dbDcDtls.size()>0 || dbInvoiceDtls.size()>0){
			return returnStatus=true;
		}
		/*String query = "select PAYMENT_ID,PO_AMOUNT,PAYMENT_REQ_UPTO,PAYMENT_DONE_UPTO from ACC_PAYMENT "
				+  "where PO_NUMBER = ? and SITE_ID = ? and VENDOR_ID = ? ";
		List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{ poNo, siteId, vendorId});
		if(map.size()>0){
			
		}
		*/
		
		
		}
		catch(EmptyResultDataAccessException e){
			e.printStackTrace();
		}
		return returnStatus;

	}
	
	/*************************************************to save cancel Po data in cancel po table start*****************************************************/
	public int saveCancelPoDetailsInCancelTbl(String poentryId,String poNumber,String pendingEmpId,String status,String passwdForMail){
		
	String query = "INSERT INTO SUMADHURA_CANCEL_PO(PO_ENTRY_ID,PO_NUMBER,ENTRY_DATE,TEMP_PO_PENDING_EMP_ID,STATUS,PASSWORD_MAIL) values(?,?,sysdate,?,?,?)";

	int result = jdbcTemplate.update(query, new Object[] {poentryId,poNumber,pendingEmpId,status,passwdForMail});
	
	return result;
	}
	
	public int saveCancelPoApproveDetails(String poentryId,String siteId,String userEmpId,String status,String vendorComments,String normalComments,String ccmails){
		
		String query = "INSERT INTO SUMADHURA_CAN_PO_APPRL_DTLS(PO_CANCEL_APPROVAL_DTLS_ID,PO_ENTRY_ID,CREATION_DATE,SITE_ID,PO_CANCEL_APPROVE_EMP_ID,PURPOSE,OPERATION_TYPE,VENDOR_COMMENTS,CC_MAILS) values(PO_CANCEL_APPROVAL_DTLS_SEQ.nextval,?,sysdate,?,?,?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {poentryId,siteId,userEmpId,normalComments,status,vendorComments,ccmails});
		
		return result;
		}
	
	
	// TO UPDATE CANCEL PO TABLE 
	public int updateCancelPoDetailsInCancelTbl(String poentryId,String pendingEmpId,String status,String passwdForMail,boolean RejectOrNot){
		
		
		String query =  "update SUMADHURA_CANCEL_PO set TEMP_PO_PENDING_EMP_ID =?,STATUS=?,PASSWORD_MAIL=? where PO_ENTRY_ID = ? and STATUS='A'";
		int result = jdbcTemplate.update(query, new Object[] {pendingEmpId,status,passwdForMail,poentryId});
		if(RejectOrNot){
			String sql =  "update SUMADHURA_CAN_PO_APPRL_DTLS set OPERATION_TYPE =? where PO_ENTRY_ID = ? ";
			int Status = jdbcTemplate.update(sql, new Object[] {"CNL",poentryId});
		}
		if(result>0){
			String query1 =  "update SUMADHURA_PO_ENTRY set PO_STATUS =? where PO_ENTRY_ID = ? ";
			int result1 = jdbcTemplate.update(query1, new Object[] {"A",poentryId});
				
		}
		return result;
		}
		
	
	public String  getCancelPerminentPoComments(String poentryId) {

		List<Map<String, Object>> dbIndentDts = null;
		//List<String> list=new ArrayListr<>();
		String vendorComments = "";
		int i=1;
		//List<String> objList = new ArrayList<String>();
		//thisIsAStringArray[5] = "FFF";
		String query = " select VENDOR_COMMENTS from SUMADHURA_CAN_PO_APPRL_DTLS SED where PO_ENTRY_ID =? and OPERATION_TYPE!='CNL' order by CREATION_DATE desc";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poentryId});
		
		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				vendorComments = prods.get("VENDOR_COMMENTS")==null ? "-" :   prods.get("VENDOR_COMMENTS").toString();
				break;
				//objList.add(vendorComments);
				}	
		}
		return vendorComments;

	}
	
	public String[] getCancelPerminentPoEmails(String poentryId) {
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		
			
			String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ( "
						+" select distinct(PO_CANCEL_APPROVE_EMP_ID) from SUMADHURA_CAN_PO_APPRL_DTLS where "
						+" PO_ENTRY_ID =?)";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poentryId});
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
	
	public boolean  checkAlreadyCancelOrnot(String poNumber) {

		List<Map<String, Object>> dbIndentDts = null;
		String pendingEmp = "";
		String poEntryId = "";
		boolean status=false;
		String query = " select PO_ENTRY_ID,TEMP_PO_PENDING_EMP_ID from SUMADHURA_CANCEL_PO SED where PO_NUMBER =? and STATUS='A'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poNumber});
		
		if(dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				pendingEmp = prods.get("TEMP_PO_PENDING_EMP_ID")==null ? "" :   prods.get("TEMP_PO_PENDING_EMP_ID").toString();
				poEntryId = prods.get("PO_ENTRY_ID")==null ? "-" :   prods.get("PO_ENTRY_ID").toString();
				}
			if(!pendingEmp.equals("VND") && !poEntryId.equals("-")){
				status=true;
			}
		}
		return status;

	}
	
	// permanent cancel Po Approval from mail this method call only purchase po
	public String  getPoQuantityIndentCreationDetailsForCancelPermanentPo(String indentNumber,String poentryId,String poNumber) {

		List<Map<String, Object>> dbDts = null;
		List<Map<String, Object>> preparedBy = null;
		String quantity = "failed";
		String indentCreationdtlsId = "";
		String status="";
		String strPreparedBy="";
		String old_Po_Number="";
		int result=0;
		String createdBy="";
		List<Map<String, Object>> createdByData = null;
		
		String sql="select PREPARED_BY,OLD_PO_NUMBER from SUMADHURA_PO_ENTRY WHERE PO_ENTRY_ID=?";
		preparedBy=jdbcTemplate.queryForList(sql, new Object[] {poentryId});
		for(Map<String, Object> prods : preparedBy) {
			strPreparedBy = prods.get("PREPARED_BY")==null ? "0" :   prods.get("PREPARED_BY").toString();
			old_Po_Number=prods.get("OLD_PO_NUMBER")==null ? "-" :   prods.get("OLD_PO_NUMBER").toString();
		}
		if(strPreparedBy.equalsIgnoreCase("PURCHASE_DEPT")){
			//if(old_Po_Number.equals("-")){
			String query = " select PO_QTY,INDENT_CREATION_DTLS_ID from SUMADHURA_PO_ENTRY_DETAILS SED where PO_ENTRY_ID =? ";
			dbDts = jdbcTemplate.queryForList(query, new Object[] {poentryId});

			if(dbDts.size()>0){
				for(Map<String, Object> prods : dbDts) {
					quantity = prods.get("PO_QTY")==null ? "0" :   prods.get("PO_QTY").toString();
					indentCreationdtlsId = prods.get("INDENT_CREATION_DTLS_ID")==null ? "-" :   prods.get("INDENT_CREATION_DTLS_ID").toString();

					if(!indentCreationdtlsId.equals("-")){
						
						String queryData="select CREATED_BY from SUMADHURA_INDENT_CREATION_DTLS where INDENT_CREATION_DETAILS_ID=? ";
						createdByData=jdbcTemplate.queryForList(queryData, new Object[] {indentCreationdtlsId});
						if (null != createdByData && createdByData.size() > 0) {
							for (Map<String, Object> prod : createdByData) {
								createdBy =(prod.get("CREATED_BY") == null ? "" : prod.get("CREATED_BY").toString());
							}
						}
						if(createdBy.equalsIgnoreCase("PURCHASE")){
							icd.deleteRowInIndentCreationDetails(Integer.valueOf(indentCreationdtlsId));
						}else{
						updatePurchaseDeptIndentProcestbl(indentNumber,quantity,poNumber,indentCreationdtlsId,true);}}

				}
				String query2="update SUMADHURA_INDENT_CREATION set PENDIND_DEPT_ID =?,STATUS='A' where INDENT_CREATION_ID=?";		
				result=jdbcTemplate.update(query2, new Object[] {"998",indentNumber});

				status="success";
			}
			//}
			/*else{
				String query2="update SUMADHURA_PO_ENTRY set PO_STATUS='A' where PO_NUMBER=?";		
				result=jdbcTemplate.update(query2, new Object[] {old_Po_Number});
			}*/
		}
		

		return status;

	}
	
	/*************************************This is for internal comments send in  mail****************************/
	public String  getCancelPerminentPoInternalComments(String poentryId) {

		List<Map<String, Object>> dbIndentDts = null;
		//List<String> objList = new ArrayList<String>();
		String vendorComments = "";
		
		String query = "select PURPOSE from SUMADHURA_CAN_PO_APPRL_DTLS SED where PO_ENTRY_ID =? and OPERATION_TYPE!='CNL' order by CREATION_DATE desc";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poentryId});

		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				vendorComments = prods.get("PURPOSE")==null ? "-" :   prods.get("PURPOSE").toString();
				break;
				//objList.add(vendorComments);
			}	
		}
		return vendorComments;

	}
	
	public Map<String, String> getVendorNameEmail(String vendor_Id) {
		List<Map<String, Object>> dbIndentDts = null;
		Map<String, String> map = new TreeMap<String, String>();
		String email="";
		String vendor_Contact_Person="";
		
		String query = "SELECT EMP_EMAIL,VENDOR_NAME FROM VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {vendor_Id});
		for(Map<String, Object> prods : dbIndentDts) {
			email=prods.get("EMP_EMAIL")==null ? "0" :   prods.get("EMP_EMAIL").toString();
			vendor_Contact_Person=prods.get("VENDOR_NAME")==null ? "0" :   prods.get("VENDOR_NAME").toString();
			
			map.put(vendor_Contact_Person,email);

		}
		return map;

	}
	
	// to get previous emp name purpose written this
	public String  previousEmpId(String poEntryId) {

		List<Map<String, Object>> dbIndentDts = null;
		String EmpId = "";
		
		String query = " SELECT SED.EMP_NAME,SPCAD.PO_CANCEL_APPROVE_EMP_ID FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_CAN_PO_APPRL_DTLS  SPCAD "
					+" where  SPCAD.PO_CANCEL_APPROVE_EMP_ID=SED.EMP_ID and PO_ENTRY_ID =? "
					+" order by SPCAD.CREATION_DATE  DESC"; 

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poEntryId});

		if(dbIndentDts!= null){
			for(Map<String, Object> prods : dbIndentDts) {
				EmpId = prods.get("PO_CANCEL_APPROVE_EMP_ID")==null ? "-" :   prods.get("PO_CANCEL_APPROVE_EMP_ID").toString();
				break;
				//objList.add(vendorComments);
			}	
		}
		return EmpId;

	}

	// payment tables updated in final approve
	
	public boolean  updatePaymentDtls(HttpServletRequest request,String poNumber,String siteId,String vendorId) {
		double done_Upto=0.0;
		double request_Upto=0.0;
		double paidAmount=0.0;
		String updateDate="";
		String paymentDetailsId="";
		String paymentTransactionId="";
		boolean status=true;
		//double updateAmt=0.0;
		String paymentId="";
		String req_Pending_Emp="";
		String strpaymentDetailsId="";
		List<String> emailList=new ArrayList<String>();
		String strSdAmount="";
		String sql="select PAID_AMOUNT from ACC_ADVANCE_PAYMENT_PO where PO_NUMBER=? ";
		List<Map<String, Object>> mapData = jdbcTemplate.queryForList(sql,new Object[]{poNumber});
		if(mapData.size()>0){
			for(Map<String, Object> prods : mapData) {
				paidAmount=Double.valueOf(prods.get("PAID_AMOUNT")==null ? "0" :   prods.get("PAID_AMOUNT").toString());
			}
			
			String query = "select AC.UPDATED_DATE,AC.PAYMENT_ID,AC.PO_AMOUNT,AC.PAYMENT_REQ_UPTO,AC.PAYMENT_DONE_UPTO," 
				+" APD.PAYMENT_DETAILS_ID,APT.PAYMENT_TRANSACTIONS_ID,APD.REQUEST_PENDING_EMP_ID"
				+" from ACC_PAYMENT AC "
				+" left outer join ACC_PAYMENT_DTLS APD on APD.PAYMENT_ID=AC.PAYMENT_ID"
				+" left outer join ACC_PAYMENT_TRANSACTIONS APT on APT.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID"
				+" where AC.PO_NUMBER =? and AC.SITE_ID =? and AC.VENDOR_ID =?";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{ poNumber, siteId, vendorId});

			if(map!=null && map.size()>0){
				for(Map<String, Object> prods : map) {
					request_Upto=Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" :   prods.get("PAYMENT_REQ_UPTO").toString());
					done_Upto=Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" :   prods.get("PAYMENT_DONE_UPTO").toString());
					updateDate=prods.get("UPDATED_DATE")==null ? "-" :   prods.get("UPDATED_DATE").toString();
					paymentDetailsId=prods.get("PAYMENT_DETAILS_ID")==null ? "0" :   prods.get("PAYMENT_DETAILS_ID").toString();
					int transactionId=Integer.parseInt(prods.get("PAYMENT_TRANSACTIONS_ID")==null ? "0" :   prods.get("PAYMENT_TRANSACTIONS_ID").toString());
					paymentId=prods.get("PAYMENT_ID")==null ? "0" :   prods.get("PAYMENT_ID").toString();
					req_Pending_Emp=prods.get("REQUEST_PENDING_EMP_ID")==null ? "-" :   prods.get("REQUEST_PENDING_EMP_ID").toString();

					if(transactionId!=0 && req_Pending_Emp.equals("-")){

						String insert = "INSERT INTO ACC_VENDOR_SECURITY_DEPOSIT_HISTORY(VENDOR_ID,SITE_ID,SECURITY_DEPOSIT_AMOUNT,STATUS,CREATION_DATE,"
							+" UPDATE_DATE,PAYMENT_DETAILS_ID,PAYMENT_ID,PAYMENT_TRANSACTIONS_ID,REMARKS"
							+ ") values(?, ?, ?, ?, sysdate,sysdate, ?,?,?,?)";

						int result = jdbcTemplate.update(insert, new Object[] {vendorId,siteId,paidAmount,"A",
								paymentDetailsId,paymentId,transactionId,"System Updated advance Payment Converted To Sd"});

						if(result>0){
							// insert security data into data acc_vendor_securiry_deposite table
							String sdAmount="SELECT SECURITY_DEPOSIT_AMOUNT FROM ACC_VENDOR_SECURITY_DEPOSIT WHERE VENDOR_ID=? AND SITE_ID=?";
							List<Map<String, Object>> sdData = jdbcTemplate.queryForList(sdAmount,new Object[]{vendorId,siteId});
							
							if(sdData.size()>0){
								for(Map<String, Object> sd : sdData) {
									strSdAmount=(sd.get("SECURITY_DEPOSIT_AMOUNT")==null ? "" :   sd.get("SECURITY_DEPOSIT_AMOUNT").toString());
								}
							}
							if(strSdAmount==null || strSdAmount.equals("")){ // NO RECORDS IN THE TABLE THEN ADD NEW ONE ELSE UPDATE TOTAL AMOUNT
								String insertSd="INSERT INTO ACC_VENDOR_SECURITY_DEPOSIT(VENDOR_ID,SITE_ID,SECURITY_DEPOSIT_AMOUNT,STATUS,CREATION_DATE,UPDATE_DATE) values(?, ?, ?, ?, sysdate,sysdate)";
									int result2 = jdbcTemplate.update(insertSd, new Object[] {vendorId,siteId,paidAmount,"A"});
							}else{
								strSdAmount=String.valueOf((Double.valueOf(strSdAmount)+paidAmount));
								String query1="update ACC_VENDOR_SECURITY_DEPOSIT set SECURITY_DEPOSIT_AMOUNT=? WHERE VENDOR_ID=? AND SITE_ID=?";
								int result1 = jdbcTemplate.update(query1, new Object[] {strSdAmount,vendorId,siteId});
								
							}
							String query1="update ACC_PAYMENT_TRANSACTIONS set PAYMENT_TYPE='SD',REMARKS='System Updated Advance Payment Converted To SD' where PAYMENT_DETAILS_ID ='"+paymentDetailsId+"'";
							int result1 = jdbcTemplate.update(query1, new Object[] {});
							strpaymentDetailsId+=paymentDetailsId+",";
						}
						

					}

				}
				if (!paymentDetailsId.equals("0") && strpaymentDetailsId.endsWith(",")) {
					strpaymentDetailsId = strpaymentDetailsId.substring(0, strpaymentDetailsId.length() - 1);
					}
				if(strpaymentDetailsId!=null && !strpaymentDetailsId.equals("")){
					emailList=getEmailsForPayment(strpaymentDetailsId);// mails send to payment deptment 
				}
				 
			}
			
			request.setAttribute("emailList",emailList); // this is used in send mail to time permanent

		}

	return status;
	}
	

	
	public List<String> getEmailsForPayment(String paymentDetailsid){
		
		
		Set<String> email=new HashSet<String>();
		paymentDetailsid=paymentDetailsid.replace(",","','");
		
		 String sql = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in("
			 		+" SELECT ASPRD.EMP_ID FROM ACC_SITE_APPR_REJECT_DTLS ASPRD where ASPRD.PAYMENT_DETAILS_ID in ('"+paymentDetailsid+"'))";
		 List<Map<String, Object>> siteEmpIds = jdbcTemplate.queryForList(sql,new Object[]{});
		 if(siteEmpIds.size()>0){
			 for(Map<String, Object> data : siteEmpIds) {
				 String	 empEmail=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
				 if(StringUtils.isNotBlank(empEmail)){
						if(empEmail.contains(",")){
							String[] emailArr= empEmail.split(",");
							for(String Email:emailArr){
								email.add(Email);
							}
						}
						else{email.add(empEmail);}
					}
				 //email.add(empEmail);
			 }
		 }
		 
		String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in("
					+" SELECT APARD.EMP_ID from ACC_PMT_APPR_REJECT_DTLS APARD,ACC_TEMP_PAYMENT_TRANSACTIONS ATPT "
					+" WHERE APARD.TEMP_PAYMENT_TRANSACTIONS_ID=ATPT.TEMP_PAYMENT_TRANSACTIONS_ID AND ATPT.PAYMENT_DETAILS_ID in ('"+paymentDetailsid+"'))";

		List<Map<String, Object>> siteAccEmpIds = jdbcTemplate.queryForList(query,new Object[]{});
		if(siteAccEmpIds.size()>0){
		 for(Map<String, Object> data : siteAccEmpIds) {
			 String empEmail=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
			 if(StringUtils.isNotBlank(empEmail)){
					if(empEmail.contains(",")){
						String[] emailArr= empEmail.split(",");
						for(String Email:emailArr){
							email.add(Email);
						}
					}
					else{email.add(empEmail);}
				}
			 //email.add(empEmail);
		 }
	 }
		List<String> list=new ArrayList<String>(email);
		return list;
		
	}
	
	
	public List<Map<String, Object>> getListOfCancelPoShow(String site_id){

		List<Map<String, Object>> poList = null;
		String sql="";
		if(site_id.equals("998")){
			 sql="select DISTINCT(SPE.PO_NUMBER),VD.VENDOR_NAME,SPE.INDENT_NO,SPE.PO_ENTRY_ID,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY,SCP.ENTRY_DATE "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_CANCEL_PO SCP "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'CNL'"
				+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.STATUS='A'"
				+ " and SPE.SITE_ID not in ('996')";

			
		}else{
		
		 sql="select DISTINCT(SPE.PO_NUMBER),VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,SPE.PO_ENTRY_ID,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY,SCP.ENTRY_DATE "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC,SUMADHURA_CANCEL_PO SCP "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'CNL' AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.STATUS='A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " and SPE.SITE_ID = '"+site_id+"'";
		}
		poList = jdbcTemplate.queryForList(sql, new Object[] {});
		return poList;

	}
	/*public List<Map<String, Object>> getListOfActivePOForCancelPermanentPOs(String site_id,String fromdate,String todate){
		
		List<Map<String, Object>> poList = null;
		String sql="";
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		if(site_id.equals(marketingDeptId)){
		if (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate)){
			 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A' " 
				+" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromdate+"','dd-MM-yy') AND TO_DATE('"+todate+"','dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}
		else if (StringUtils.isNotBlank(fromdate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'  " 
				+" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromdate+"', 'dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}else if(StringUtils.isNotBlank(todate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'  " 
				+" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+todate+"', 'dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}
		}else{
			if (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate)){
				
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromdate+"','dd-MM-yy') AND TO_DATE('"+todate+"','dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if (StringUtils.isNotBlank(fromdate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromdate+"', 'dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if(StringUtils.isNotBlank(todate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+todate+"', 'dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if(StringUtils.isNotBlank(poNumber)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
				+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
				+ " AND SPE.PO_NUMBER ='"+poNumber+"'";
			}
		}
		poList = jdbcTemplate.queryForList(sql, new Object[] {});
		return poList;
	}*/
	
	// THIS IS COMMON METHOD FOR CANCEL PO AND UPDATE PO AND REVISED PO
	// PO IN APPROVAL TIME OR INITIAL STAGE MAIL TRIGGER SO WRITTEN THIS ONE
	/**************************************check payment tables payment initiated or not for cancel po purpose start*************************************/
	public boolean checkPoPaymentDoneOrNot(String poNo, String siteId, String vendorId,String poDate,String siteName,String poEntryId,int portNumber,boolean isUpdatePO,boolean isRevised) {
		
		boolean returnStatus=false;
		Set<String> emailList=new HashSet<String>();
		double reqAmount=0.0;
		double reqAmount_Done=0.0;
		double poAmount=0.0;
		String VendorName="";
		String user_Id=""; // for signature purpose in mail writte this one
		String nameEmail="";
		String currentEmpMail="";
		String internalComments="";
		String vendorComments="";
		boolean isCancel=false;
		try{
		
			String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in( "
					+" SELECT ASPRD.EMP_ID FROM ACC_SITE_APPR_REJECT_DTLS ASPRD "
					+" where ASPRD.PAYMENT_DETAILS_ID in (select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS "
					+" where PAYMENT_ID in (select PAYMENT_ID from ACC_PAYMENT "
					+" where PO_NUMBER =? and SITE_ID =? and STATUS='A' and (PAYMENT_DONE_UPTO!='0' or PAYMENT_REQ_UPTO!='0') and VENDOR_ID =?))) "
					
					+" union "
					+" select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in( "
					+" SELECT APARD.EMP_ID from ACC_PMT_APPR_REJECT_DTLS APARD,ACC_TEMP_PAYMENT_TRANSACTIONS ATPT "
					+" WHERE APARD.TEMP_PAYMENT_TRANSACTIONS_ID=ATPT.TEMP_PAYMENT_TRANSACTIONS_ID "
					+" AND ATPT.PAYMENT_DETAILS_ID in (select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS "
					+" where PAYMENT_ID in (select PAYMENT_ID from ACC_PAYMENT "
					+" where PO_NUMBER =? and SITE_ID =? and STATUS='A' and (PAYMENT_DONE_UPTO!='0' or PAYMENT_REQ_UPTO!='0') and VENDOR_ID =?)))";
					
			List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{ poNo, siteId, vendorId,poNo, siteId, vendorId});
			if(map.size()>0){
				 for(Map<String, Object> data : map) {
					 String empEmail=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
					 if(StringUtils.isNotBlank(empEmail)){
							if(empEmail.contains(",")){
								String[] emailArr= empEmail.split(",");
								for(String Email:emailArr){
									emailList.add(Email);
								}
							}
							else{emailList.add(empEmail);}
						}
					 //email.add(empEmail);
				 }
				 //getting the requested amount so written this one
				 
				 
				 String empId="select APD.REQUEST_PENDING_EMP_ID,APD.REQUEST_PENDING_DEPT_ID,"
							+" ATPT.REQUEST_PENDING_EMP_ID as ACC_PENDING_EMP_ID,APD.STATUS "  
							+" from VENDOR_DETAILS VD,ACC_PAYMENT AP,SITE S,ACC_PAYMENT_DTLS APD "
					 		+" LEFT OUTER JOIN ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID = ATPT.PAYMENT_DETAILS_ID " 
							+" LEFT OUTER JOIN ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP on AADPP.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID "
							+" LEFT OUTER JOIN SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID = APD.REQUEST_PENDING_EMP_ID "
							+" where AP.PAYMENT_ID = APD.PAYMENT_ID and AP.VENDOR_ID = VD.VENDOR_ID and AP.SITE_ID = S.SITE_ID " 
							+" and (ATPT.REQUEST_PENDING_EMP_ID NOT IN ('VND') OR ATPT.REQUEST_PENDING_EMP_ID IS NULL) "
							+" and AP.PO_NUMBER = ? and APD.STATUS='A'";
				 List<Map<String, Object>> emp_Id = jdbcTemplate.queryForList(empId,new Object[]{poNo});	
				 for(Map<String, Object> data : emp_Id) {
					 String pending_Emp_Id=data.get("REQUEST_PENDING_EMP_ID")==null ? "" :   data.get("REQUEST_PENDING_EMP_ID").toString();
					 String pending_acc_Id=data.get("REQUEST_PENDING_DEPT_ID")==null ? "" :   data.get("REQUEST_PENDING_DEPT_ID").toString();
					 String pending_acc2_Id=data.get("ACC_PENDING_EMP_ID")==null ? "" :   data.get("ACC_PENDING_EMP_ID").toString();
					 if(!pending_acc2_Id.equals("")){
						 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where DEPT_ID='"+pending_acc2_Id+"'";
					 }else if(!pending_acc_Id.equals("")){
						 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where DEPT_ID='"+pending_acc_Id+"'";
					 }else{
						 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID='"+pending_Emp_Id+"'";
					 }
				 }
				 List<Map<String, Object>> currentMails = jdbcTemplate.queryForList(currentEmpMail,new Object[]{});
				 for(Map<String, Object> data : currentMails) {
					 String empmailId=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
					 if(StringUtils.isNotBlank(empmailId)){
					 if(empmailId.contains(",")){
							String[] emailArr= empmailId.split(",");
							for(String Email:emailArr){
								emailList.add(Email);
							}
						}
					 else{
					 emailList.add(empmailId);
					 }
					 }
				 }
				 
				 returnStatus=true;
				 String sql="SELECT PAYMENT_REQ_UPTO,PAYMENT_DONE_UPTO,PO_AMOUNT FROM ACC_PAYMENT WHERE PO_NUMBER=? AND VENDOR_ID=?";
				 List<Map<String, Object>> Amount = jdbcTemplate.queryForList(sql,new Object[]{ poNo,vendorId});
				 for(Map<String, Object> data : Amount){
					 reqAmount=Double.valueOf(data.get("PAYMENT_REQ_UPTO")==null ? "0" :   data.get("PAYMENT_REQ_UPTO").toString());
					 reqAmount_Done=Double.valueOf(data.get("PAYMENT_DONE_UPTO")==null ? "0" :   data.get("PAYMENT_DONE_UPTO").toString());
					 poAmount=Double.valueOf(data.get("PO_AMOUNT")==null ? "0" :   data.get("PO_AMOUNT").toString());
				 if(reqAmount==0){
					 reqAmount=reqAmount_Done;
				 }
				/* String userId="SELECT PO_CANCEL_APPROVE_EMP_ID FROM SUMADHURA_CAN_PO_APPRL_DTLS WHERE OPERATION_TYPE='C' AND PO_ENTRY_ID='"+poEntryId+"'";
				  user_Id = jdbcTemplate.queryForObject(userId,String.class);*/
				 }
				 
				 Map<String, String> vendordata =(Map<String, String>)getVendorNameEmail(vendorId);
					for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
						VendorName=retVal.getKey();
					}
				if(!isUpdatePO && !isRevised){
				 internalComments=getCancelPerminentPoInternalComments(poEntryId); // getting the internal comments to show accounts dept
				 vendorComments=getCancelPerminentPoComments(poEntryId);// getting the vendor comments  to show to accounts dept
				 isCancel=true;
				}
				
				/*if(user_Id!=null && !user_Id.equals("")){
				nameEmail=getPoCreatedEmpName(user_Id); // this is used in the below for signature purpose
				}*/
				 Object ApproveData[]={poNo,poDate,poAmount,reqAmount,internalComments,vendorComments,isUpdatePO,isRevised};
				 List<String> list=new ArrayList<String>(emailList);
				 String ccTo [] = new String[list.size()];
				 list.toArray(ccTo);
				 EmailFunction objEmailFunction = new EmailFunction();
				 objEmailFunction.sendMailForAccountsDeptinApprovalTime(ApproveData,ccTo,VendorName,siteName,portNumber,isCancel); // mail send to previous employess 

			 }
		
		//request.setAttribute("emailList",emailList); // this is used in send mail to time permanent
		}
		catch(EmptyResultDataAccessException e){
			e.printStackTrace();
		}
		return returnStatus;

	}
	// IN THE MAIL WE CAN SEND EMP ID AND MOB NUMBER AND EMAIL ID
	public String getCreatedEmpIdandMail(String poEntryId) {
		String nameEmail="";
		String sql="SELECT PO_CANCEL_APPROVE_EMP_ID FROM SUMADHURA_CAN_PO_APPRL_DTLS WHERE OPERATION_TYPE='C' AND PO_ENTRY_ID='"+poEntryId+"'";
		String user_Id = jdbcTemplate.queryForObject(sql,String.class);
		if(user_Id!=null && !user_Id.equals("")){
		nameEmail=getPoCreatedEmpName(user_Id); // this is used in the below for signature purpose
		}
		return nameEmail;
	}
	/*======================================================get siteWise data start=========================================================*/
	@Override
	public List<IndentCreationBean> ViewandGetCancelPo(String fromDate, String toDate,String PoNumber) {

		String query = "";
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		//List<Map<String, Object>> dbMarketingPODts = null;
		IndentCreationBean indentObj = null; 
		List<Map<String, Object>> PermanentPODts = null; // when the user cancel then it will use
		//String sql="";
		String cancelpo="";
		//String purchase_Dept_Id=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();	
		//String marketi_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	
		try {
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				cancelpo="SELECT DISTINCT(SCP.PO_NUMBER),SCP.PO_ENTRY_ID,SPE.PO_DATE,SED.EMP_NAME,S.SITE_NAME,(SED1.EMP_NAME) AS PENDING_EMP_NAME,SPE.SITE_ID "
					+" FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1,"
					+" SUMADHURA_CAN_PO_APPRL_DTLS SCPAD WHERE  SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND SCPAD.OPERATION_TYPE='C'"
					+" and TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND S.SITE_ID=SPE.SITE_ID "
					+" AND SPE.PO_ENTRY_ID=SCP.PO_ENTRY_ID AND SCPAD.PO_CANCEL_APPROVE_EMP_ID=SED.EMP_ID AND SCPAD.PO_ENTRY_ID=SPE.PO_ENTRY_ID";
			} else if (StringUtils.isNotBlank(fromDate)) {
				cancelpo="SELECT DISTINCT(SCP.PO_NUMBER),SCP.PO_ENTRY_ID,SPE.PO_DATE,SED.EMP_NAME,S.SITE_NAME,(SED1.EMP_NAME) AS PENDING_EMP_NAME,SPE.SITE_ID "
					+" FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1,"
					+" SUMADHURA_CAN_PO_APPRL_DTLS SCPAD WHERE  SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND SCPAD.OPERATION_TYPE='C'"
					+" and TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND S.SITE_ID=SPE.SITE_ID "
					+" AND SPE.PO_ENTRY_ID=SCP.PO_ENTRY_ID AND SCPAD.PO_CANCEL_APPROVE_EMP_ID=SED.EMP_ID AND SCPAD.PO_ENTRY_ID=SPE.PO_ENTRY_ID";
			} else if(StringUtils.isNotBlank(toDate)) {
				cancelpo="SELECT DISTINCT(SCP.PO_NUMBER),SCP.PO_ENTRY_ID,SPE.PO_DATE,SED.EMP_NAME,S.SITE_NAME,(SED1.EMP_NAME) AS PENDING_EMP_NAME,SPE.SITE_ID "
					+" FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1,"
					+" SUMADHURA_CAN_PO_APPRL_DTLS SCPAD WHERE  SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND SCPAD.OPERATION_TYPE='C'"
					+" and TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND S.SITE_ID=SPE.SITE_ID "
					+" AND SPE.PO_ENTRY_ID=SCP.PO_ENTRY_ID AND SCPAD.PO_CANCEL_APPROVE_EMP_ID=SED.EMP_ID AND SCPAD.PO_ENTRY_ID=SPE.PO_ENTRY_ID";
			}
			else if(StringUtils.isNotBlank(PoNumber)) {
				cancelpo="SELECT DISTINCT(SCP.PO_NUMBER),SCP.PO_ENTRY_ID,SPE.PO_DATE,SED.EMP_NAME,S.SITE_NAME,(SED1.EMP_NAME) AS PENDING_EMP_NAME,SPE.SITE_ID "
					+" FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S,SUMADHURA_EMPLOYEE_DETAILS SED1,"
					+" SUMADHURA_CAN_PO_APPRL_DTLS SCPAD WHERE  SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID AND SCPAD.OPERATION_TYPE='C'"
					+" AND S.SITE_ID=SPE.SITE_ID and SCP.PO_NUMBER='"+PoNumber+"'"
					+" AND SPE.PO_ENTRY_ID=SCP.PO_ENTRY_ID AND SCPAD.PO_CANCEL_APPROVE_EMP_ID=SED.EMP_ID AND SCPAD.PO_ENTRY_ID=SPE.PO_ENTRY_ID";

			}
			PermanentPODts=jdbcTemplate.queryForList(cancelpo, new Object[]{});

			// this is for marketing po purpose
			if(PermanentPODts!=null && PermanentPODts.size()>0 ){
				for(Map<String, Object> cancelPo : PermanentPODts) {
					indentObj = new IndentCreationBean();

					indentObj.setPonumber(cancelPo.get("PO_NUMBER")==null ? "" : cancelPo.get("PO_NUMBER").toString());
					indentObj.setToEmpName(cancelPo.get("EMP_NAME")==null ? "" : cancelPo.get("EMP_NAME").toString());
					indentObj.setSiteName(cancelPo.get("SITE_NAME")==null ? "0" : cancelPo.get("SITE_NAME").toString());
					indentObj.setSiteId(Integer.parseInt(cancelPo.get("SITE_ID")==null ? "0" : cancelPo.get("SITE_ID").toString()));
					String date=cancelPo.get("PO_DATE")==null ? "" : cancelPo.get("PO_DATE").toString();
					indentObj.setPending_Emp_Name(cancelPo.get("PENDING_EMP_NAME")==null ? "0" : cancelPo.get("PENDING_EMP_NAME").toString());
					indentObj.setPoentryId(cancelPo.get("PO_ENTRY_ID")==null ? "0" : cancelPo.get("PO_ENTRY_ID").toString());
					indentObj.setType_Of_Purchase("CANCEL PO");

					indentObj.setFromDate(fromDate);
					indentObj.setToDate(toDate);
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setStrScheduleDate(date);

					list.add(indentObj);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			//	template = null;
			dbIndentDts = null;
		}
		return list;
	}

	/*=======================================================get site Wise Data End=========================================================*/
	/***************************************************to show to the vendor message for this written**************************************/
	
	public List<String> getCancelPoData(String poNumber,String siteId){
		List<String> list=new ArrayList<String>();
		List<Map<String, Object>> PODts = null;
		String PoEntryId="";
		String poDate="";
		String siteName="";
		String vendorComments="";
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
		String sql="SELECT SPE.PO_DATE,SPE.PO_ENTRY_ID,S.SITE_NAME FROM SUMADHURA_PO_ENTRY SPE,SITE S"
				+" WHERE SPE.PO_NUMBER=? and SPE.SITE_ID=S.SITE_ID";
		PODts=jdbcTemplate.queryForList(sql, new Object[]{poNumber});
		
		if(PODts!=null && PODts.size()>0 ){
			for(Map<String, Object> Po : PODts) {
				PoEntryId=Po.get("PO_ENTRY_ID")==null ? "" : Po.get("PO_ENTRY_ID").toString();
				poDate=Po.get("PO_DATE")==null ? "" : Po.get("PO_DATE").toString();
				siteName=Po.get("SITE_NAME")==null ? "" : Po.get("SITE_NAME").toString();
				
				Date date= dt.parse(poDate);
				poDate=dt1.format(date);
				
				vendorComments=getCancelPerminentPoComments(PoEntryId);
				list.add(poDate);list.add(siteName);list.add(vendorComments);
			}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return list;
		
	}
	

	@Override
	public boolean isThisPOGoingToBeCanceled(String poEntryId){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		String query=" select * from SUMADHURA_CANCEL_PO where PO_ENTRY_ID = ? and STATUS='A'";
		list = jdbcTemplate.queryForList(query, new Object[]{poEntryId});
		if(list.size()>0){
			return true;
		}
		else{
			return false;
		}
	}
	
	/***********************************************get the cc mails start *****************************************************************/
	@Override
	public String  getCCmails(String poEntryId,String siteId,boolean isCancel) {

		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> sqlData = null;
		String strEmailId = "";
		List<String> objList = new ArrayList<String>();
		int POEntryId=0;
		//thisIsAStringArray[5] = "FFF";
		String query = "SELECT TEMP_PO_ENTRY_ID FROM SUMADHURA_PO_ENTRY WHERE PO_ENTRY_ID=? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poEntryId});
		for(Map<String, Object> prods : dbIndentDts){
			POEntryId=Integer.valueOf(prods.get("TEMP_PO_ENTRY_ID")==null ? "0" :   prods.get("TEMP_PO_ENTRY_ID").toString());
		}
		if(POEntryId!=0){
			String sql = "select CC_EMAIL_ID from SUMADHURA_TEMP_PO_ENTRY where PO_ENTRY_ID =?";
			sqlData=jdbcTemplate.queryForList(sql, new Object[] {POEntryId});
		}
		if(sqlData!= null){
			for(Map<String, Object> prods : sqlData) {
				strEmailId = prods.get("CC_EMAIL_ID")==null ? "" :   prods.get("CC_EMAIL_ID").toString();
				/*if(strEmailId!=null && !strEmailId.equals("")){
				objList.add(strEmailId);
				}*/
				}
			}
		if(strEmailId.equals("") && isCancel){
			String state =getStateNameForTermsAndConditions(siteId);
			state = state.toUpperCase();
			String strCCEmails="";
			int count =0;
			while(true) {
				count = count+1;
				String ccEmail = validateParams.getProperty(siteId+"_"+state+"_CCEMAIL"+count);
				if(ccEmail==null){break;}
				else{
					strCCEmails =strCCEmails + ccEmail + ",";
				}
			}
			if(!strCCEmails.equals("")){strEmailId = strCCEmails.substring(0,strCCEmails.length()-1);}
			//objList.add(strCCEmails);
		}
		return strEmailId;

	}
	/************************************************geting the ccmails for cancel po start***********************************************/
	public String  getCancelPoApprovalCCmails(String poentryId) {

		List<Map<String, Object>> dbIndentDts = null;
		String ccmails = "";
		String query = " select CC_MAILS from SUMADHURA_CAN_PO_APPRL_DTLS SED where PO_ENTRY_ID =? order by CREATION_DATE desc";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poentryId});
		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				ccmails = prods.get("CC_MAILS")==null ? "" :   prods.get("CC_MAILS").toString();
				break;
				}	
		}
		return ccmails;

	}

public List<Map<String, Object>> getListOfActivePOForCancelPermanentPOs(String site_id,String fromdate,String todate,String poNumber){
		
		List<Map<String, Object>> poList = null;
		String sql="";
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		if(site_id.equals(marketingDeptId)){
		if (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate)){
			 sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A' " 
				+" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromdate+"','dd-MM-yy') AND TO_DATE('"+todate+"','dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}
		else if (StringUtils.isNotBlank(fromdate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'  " 
				+" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromdate+"', 'dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}else if(StringUtils.isNotBlank(todate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'  " 
				+" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+todate+"', 'dd-MM-yy') AND SPE.SITE_ID='"+site_id+"'";

		}else if(StringUtils.isNotBlank(poNumber)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'  " 
				+" AND SPE.PO_NUMBER ='"+poNumber+"'";

		}
		}else{
			if (StringUtils.isNotBlank(fromdate) && StringUtils.isNotBlank(todate)){
				
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromdate+"','dd-MM-yy') AND TO_DATE('"+todate+"','dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if (StringUtils.isNotBlank(fromdate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromdate+"', 'dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if(StringUtils.isNotBlank(todate)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
			+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
			+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
			+ " AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+todate+"', 'dd-MM-yy') and SPE.SITE_ID not in ('"+marketingDeptId+"')";
		}else if(StringUtils.isNotBlank(poNumber)) {
			sql="select SPE.PO_NUMBER,VD.VENDOR_NAME,SPE.INDENT_NO,SIC.SITEWISE_INDENT_NO,S.SITE_NAME,SPE.PO_DATE,SPE.SITE_ID,SPE.PREPARED_BY "
				+ " from SUMADHURA_PO_ENTRY SPE,VENDOR_DETAILS VD,SITE S,SUMADHURA_INDENT_CREATION SIC "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID AND SPE.PO_STATUS = 'A'"
				+ " and SIC.INDENT_CREATION_ID = SPE.INDENT_NO"
				+ " AND SPE.PO_NUMBER ='"+poNumber+"'";
			}
		}
		poList = jdbcTemplate.queryForList(sql, new Object[] {});
		return poList;
	}

	@Override
	public List<Map<String, Object>> getPricesOfTempPoProducts(String poNumber) {
		String sql = "select STPED.CHILD_PRODUCT_ID,STPED.AMOUNT_AFTER_TAX,STPED.PO_QTY from SUMADHURA_TEMP_PO_ENTRY_DTLS STPED,SUMADHURA_TEMP_PO_ENTRY STPE where STPED.PO_ENTRY_ID=STPE.PO_ENTRY_ID and STPE.PO_NUMBER = ? ";
		return jdbcTemplate.queryForList(sql, new Object[] {poNumber});
	}
	
	@Override
	public List<Map<String, Object>> getPricesOfPermanentPoProducts(String poNumber) {
		String sql = "select SPED.CHILD_PRODUCT_ID,SPED.AMOUNT_AFTER_TAX,SPED.PO_QTY from SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE where SPED.PO_ENTRY_ID=SPE.PO_ENTRY_ID and SPE.PO_NUMBER = ? ";
		return jdbcTemplate.queryForList(sql, new Object[] {poNumber});
	}
	
	@Override
	public int[] insertTempPoConclusions(final List<String> conclusions,final String tempPoEntryId,final String indentCreationId){
		String sql = "insert into PD_TEMP_PO_CONCLUSIONS (CONCLUSION_ID,CONCLUSION_DESC,TEMP_PO_ENTRY_ID,INDENT_CREATION_ID) "
				+ " values(TEMP_CONCLUSIONS_SEQ.nextval,?,?,?)";
		int[] result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

	        @Override
	        public void setValues(PreparedStatement ps, int i)
	            throws SQLException {

	        	ps.setString(1, conclusions.get(i));
	            ps.setString(2, tempPoEntryId);
	            ps.setString(3, indentCreationId);

	        }

	        @Override
	        public int getBatchSize() {
	            return conclusions.size();
	        }
	    });
		return result;
	}

	@Override
	public int[] insertPoConclusions(final List<String> conclusions,final String poEntryId,final String tempPoEntryId,final String indentCreationId){
		String sql = "insert into PD_PO_CONCLUSIONS (CONCLUSION_ID,CONCLUSION_DESC,PO_ENTRY_ID,TEMP_PO_ENTRY_ID,INDENT_CREATION_ID) "
				+ " values(CONCLUSIONS_SEQ.nextval,?,?,?,?)";
		int[] result = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

	        @Override
	        public void setValues(PreparedStatement ps, int i)
	            throws SQLException {

	        	ps.setString(1, conclusions.get(i));
	            ps.setString(2, poEntryId);
	            ps.setString(3, tempPoEntryId);
	            ps.setString(4, indentCreationId);

	        }

	        @Override
	        public int getBatchSize() {
	            return conclusions.size();
	        }
	    });
		return result;
	}
	@Override
	public int deleteTempPoConclusions(String tempPoEntryId) {
		String sql =  "DELETE FROM PD_TEMP_PO_CONCLUSIONS WHERE TEMP_PO_ENTRY_ID = ? ";
		return jdbcTemplate.update(sql, new Object[] {tempPoEntryId});
		
	}
	@Override
	public List<String> getTempPoConclusions(String tempPoEntryId) {
		String sql="SELECT CONCLUSION_DESC FROM PD_TEMP_PO_CONCLUSIONS WHERE TEMP_PO_ENTRY_ID = ? order by CONCLUSION_ID";
		List<String> conclusions = jdbcTemplate.queryForList(sql, new Object[] {tempPoEntryId},String.class);
		return conclusions;
	}
	@Override
	public List<String> getPoConclusions(String poEntryId) {
		String sql="SELECT CONCLUSION_DESC FROM PD_PO_CONCLUSIONS WHERE PO_ENTRY_ID = ? order by CONCLUSION_ID";
		List<String> conclusions = jdbcTemplate.queryForList(sql, new Object[] {poEntryId},String.class);
		return conclusions;
	}
	@Override
	public List<String> getPoConclusionsByPoNumber(String poNumber) {
		String sql="SELECT PPC.CONCLUSION_DESC FROM PD_PO_CONCLUSIONS PPC,SUMADHURA_PO_ENTRY SPE where PPC.PO_ENTRY_ID=SPE.PO_ENTRY_ID and SPE.PO_NUMBER = ?  order by PPC.CONCLUSION_ID";
		List<String> conclusions = jdbcTemplate.queryForList(sql, new Object[] {poNumber},String.class);
		return conclusions;
	}
		@Override
	public String getStateforSendEnquiry(String site_id) {
		String Query = "select ADDRESS from SITE where SITE_ID = '"+site_id+"'"; 
		String result = jdbcTemplate.queryForObject(Query, String.class);
		
		return result;

	}
	// getting the user mail ids
	public String  getUserMailIds(String userId) {

		List<Map<String, Object>> dbIndentDts = null;
		String ccmails = "";
		String query = " select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where EMP_ID =?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {userId});
		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				ccmails = prods.get("EMP_EMAIL")==null ? "" :   prods.get("EMP_EMAIL").toString();
				
				}	
		}
		return ccmails;

	}
	
	/*==============================================when the po is updated then getting the previous emp mails==================================*/
	@Override
	public String gettingEmpId(String poEntryId) {
		
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
		
		String ccmails="";
		String strCCEmails="";
		double temppoNumber=0.0;
		
		String sql="select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID =?";
		dbDts = jdbcTemplate.queryForList(sql, new Object[] {poEntryId});
		for(Map<String, Object> prods : dbDts) {
		temppoNumber=Double.valueOf(prods.get("TEMP_PO_NUMBER")==null ? "" :   prods.get("TEMP_PO_NUMBER").toString());
		}
		if(temppoNumber!=0){
		String query = " SELECT SED.EMP_EMAIL FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "
					+" where   SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID "
					+" and TEMP_PO_NUMBER = (select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID =? ) "  
					+" order by SPCAD.CREATION_DATE  ASC";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {poEntryId});
		
		//String state = objPurchaseDepartmentIndentProcessDao.getStateNameForTermsAndConditions(site_id);
		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				ccmails = prods.get("EMP_EMAIL")==null ? "" :   prods.get("EMP_EMAIL").toString();
				
				if(ccmails!=null){
					//strCCEmails =strCCEmails
					strCCEmails =strCCEmails + ccmails + ",";}
				
				}
			
		}
		}
		//if(!strCCEmails.equals("")){strCCEmails = strCCEmails.substring(0,strCCEmails.length()-1);}
		return strCCEmails;
	}	
	/*===================================================getting the data for mail in update po purpose start======================================*/
	public List<ProductDetails> gettingProductsForUpdatePO(String indentnumber,String oldPOEntryId,String tempPONumber){
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
		List<String> list=new ArrayList<String>();
		final List<ProductDetails> SuccessDataListToMail = new ArrayList<ProductDetails>();
		
		String oldQuantity="";
		String childProductId="";
		String childProductName="";
		String measurement="";
		String tempPOQuantity="";
		int i=0;
		String query = " SELECT CHILD_PRODUCT_ID,PO_QTY FROM SUMADHURA_PO_ENTRY_DETAILS WHERE PO_ENTRY_ID=? order by CHILD_PRODUCT_ID";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {oldPOEntryId});
		
		if(dbIndentDts!= null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				
				oldQuantity = prods.get("PO_QTY")==null ? "" :   prods.get("PO_QTY").toString();
				childProductId=prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
				list.add(oldQuantity);
			}
		}
		String sql="SELECT PO_QTY,(C.NAME)AS CHILD_PRODUCT,(M.NAME)AS MEASUREMENT FROM SUMADHURA_TEMP_PO_ENTRY_DTLS STPE,CHILD_PRODUCT C,MEASUREMENT M "
				+" WHERE STPE.PO_ENTRY_ID=? AND STPE.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID AND STPE.MEASUR_MNT_ID=M.MEASUREMENT_ID order by STPE.CHILD_PRODUCT_ID";
		
		dbDts=jdbcTemplate.queryForList(sql, new Object[] {tempPONumber});
		
		if(dbDts!= null && dbDts.size()>0){
			for(Map<String, Object> prods : dbDts) {
				ProductDetails productDetailsToMail = new ProductDetails(); 
				
				tempPOQuantity = prods.get("PO_QTY")==null ? "" :   prods.get("PO_QTY").toString();
				childProductName=prods.get("CHILD_PRODUCT")==null ? "" :   prods.get("CHILD_PRODUCT").toString();
				measurement=prods.get("MEASUREMENT")==null ? "" :   prods.get("MEASUREMENT").toString();
				
				productDetailsToMail.setChild_ProductName(childProductName);
				productDetailsToMail.setMeasurementName(measurement);
				productDetailsToMail.setPoNumber(tempPONumber);
				productDetailsToMail.setRequestQantity(tempPOQuantity);
				productDetailsToMail.setRecivedQty(list.get(i));
				SuccessDataListToMail.add(productDetailsToMail);
				i++;
				
			}
		}
		return SuccessDataListToMail;
		
	}
	
	/*==============================================update po time vendorDetails start=================================================*/
	// approval time when user click on approval get and save data
	public String getAndsaveVendorUPdatePoDetails(String tempPONumber, String siteId,String user_id,HttpServletRequest request,String revision_No,
			String oldPoNumber,String siteLevelPoPreparedBy,String siteName,String deliveryDate,String oldPODate) {

		List<Map<String, Object>> dbIndentDts = null;
		
		List<String> listRevisedDetails = new ArrayList<String>();
		String indentnumber="";
		String ponumber ="";
		String vendorid="";
		String response="";
		int result=0;
		String subject="";
		String billingAddress="";
		String State="";
		String poState="";
		
		String strIndentFromSiteId = "";
		String version_Number="";
		String porefferenceDate="";
		String refference_No="";
		int temprevision_no=0;
		int revision_no=0;
		
		String preparedBy="";
		String strPOdate="";
		String strFinacialYear="";
		//String oldPOEntryId="";//this is for revised po purpose to get received quantity receive from inwards from po
		String strRevisedTypePurchase="";//this is for revised po purpose to get received quantity receive from inwards from po
		String poEntryId="";
		String payment_Req_Days="";
		List<String> updateOfDetails = new ArrayList<String>();//revised details of po purpose
		String strPODate="";
		String operation_Type="";
		if (StringUtils.isNotBlank(tempPONumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.VENDOR_ID,STPE.SUBJECT,STPE.BILLING_ADDRESS,VD.STATE,STPE.SITE_ID,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.PAYMENT_REQ_DAYS,STPE.OPERATION_TYPE FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{tempPONumber});
		}
		String query = "select STATE from VENDOR_DETAILS where VENDOR_ID='"+siteId+"'";
		State = jdbcTemplate.queryForObject(query, String.class);
		request.setAttribute("state",State); // used in revised time account dept to get mails hyd or bng
		
		if(oldPoNumber!=null && !oldPoNumber.equals("-") && !oldPoNumber.equals("")){

			String sql =  "update SUMADHURA_PO_ENTRY set PO_STATUS ='UPDATE' where PO_NUMBER ='"+oldPoNumber+"' ";
			int resultValue = jdbcTemplate.update(sql, new Object[] {});
			String tempPoNumber=oldPoNumber;
			if(tempPoNumber.contains("/U")){
				String data=tempPoNumber.split("/U")[1];
				if(data.contains("/R")){
					String data1=data.split("/R")[0];
					temprevision_no=Integer.valueOf(data1)+1;
					ponumber=tempPoNumber.replace("U"+Integer.valueOf(data1), "U"+temprevision_no);
					//System.out.print("the split data1  "+data1);
				}else{
					temprevision_no=Integer.valueOf(data)+1;
					ponumber=tempPoNumber.replace("U"+Integer.valueOf(data), "U"+temprevision_no);
				}

			}else{
				ponumber=tempPoNumber+"/U"+"1";
				System.out.print("else the split data ");
			}
		}
		//request.setAttribute("strRevisedTypePurchase",strRevisedTypePurchase);
		int poSeqNo = getPoEnterSeqNo();
		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {
				indentnumber=IndentGetBean.get("INDENT_NO") == null ? "" : IndentGetBean.get("INDENT_NO").toString();
				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();
				subject=IndentGetBean.get("SUBJECT") == null ? "" : IndentGetBean.get("SUBJECT").toString();
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "" : IndentGetBean.get("BILLING_ADDRESS").toString();
				strIndentFromSiteId=IndentGetBean.get("SITE_ID") == null ? "" : IndentGetBean.get("SITE_ID").toString();
				poEntryId=IndentGetBean.get("PO_ENTRY_ID") == null ? "0" : IndentGetBean.get("PO_ENTRY_ID").toString();

				version_Number=IndentGetBean.get("VERSION_NUMBER") == null ? "" : IndentGetBean.get("VERSION_NUMBER").toString();
				porefferenceDate=IndentGetBean.get("PO_ISSUE_DATE") == null ? "" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "" : IndentGetBean.get("REFFERENCE_NO").toString();
				payment_Req_Days=IndentGetBean.get("PAYMENT_REQ_DAYS") == null ? "" : IndentGetBean.get("PAYMENT_REQ_DAYS").toString();
				operation_Type=IndentGetBean.get("OPERATION_TYPE") == null ? "" : IndentGetBean.get("OPERATION_TYPE").toString();
				
				String query1 = "INSERT INTO SUMADHURA_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"+
				"PO_ENTRY_USER_ID, SITE_ID,INDENT_NO,TERMS_CONDITIONS_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,DELIVERY_DATE,TEMP_PO_ENTRY_ID,PAYMENT_REQ_DAYS,CREATION_DATE,OPERATION_TYPE) values(? , ?, ? , ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?)";
				result = jdbcTemplate.update(query1, new Object[] {
						poSeqNo,ponumber,StringUtils.isBlank(oldPODate)?null:DateUtil.convertToJavaDateFormat(oldPODate),vendorid,"A",user_id,
						siteId,indentnumber,"0" ,subject,billingAddress,version_Number,DateUtil.convertToJavaDateFormat(porefferenceDate),refference_No,temprevision_no,oldPoNumber,siteLevelPoPreparedBy,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poEntryId,payment_Req_Days,operation_Type});

			}
			request.setAttribute("tempPoNumber",tempPONumber);
			request.setAttribute("poEntrySeqID",poSeqNo); // it is used in terms and condition and tranportation details time user to get data
			request.setAttribute("permentPoNumber",ponumber); // to take po number for pdf or images save or move revisedc time also used
			request.setAttribute("State",State);

			response="success";
		}


		return "response";

	}
	public String inactiveUpdatePO(String oldPONumber){
	 String query =  "update SUMADHURA_PO_ENTRY set PO_STATUS ='UPDATE' where PO_NUMBER ='"+oldPONumber+"' ";
	int result = jdbcTemplate.update(query, new Object[] {});
	return "success";
	}
	
	// THIS IS THE COMMON METHOD FOR UPDATE AND REVISED PO
	/*************************************************advance amount added to the in update or revised po time strat**************************************/
	public double  updateAdvancePaidAmount(HttpServletRequest request,String poNumber,String siteId,String vendorId,double totalAmount) {
		double done_Upto=0.0;
		double request_Upto=0.0;
		double paidAmount=0.0;
		String updateDate="";
		String paymentDetailsId="";
		String paymentTransactionId="";
		boolean status=true;
		//double updateAmt=0.0;
		String paymentId="";
		String req_Pending_Emp="";
		String strpaymentDetailsId="";
		List<String> emailList=new ArrayList<String>();
		//Set<String> emailList=new HashSet<String>();
		String strSdAmount="";
		boolean paidAmtModified=false;
		double transActionPaidAmt=0.0;
		double totalTransactionAmt=0.0;
		List<Double> amount=new ArrayList<Double>();
		List<String> transactonId=new ArrayList<String>();
		String sql="select PAID_AMOUNT from ACC_ADVANCE_PAYMENT_PO where PO_NUMBER=? ";
		List<Map<String, Object>> mapData = jdbcTemplate.queryForList(sql,new Object[]{poNumber});
		if(mapData.size()>0){
			for(Map<String, Object> prods : mapData) {
				paidAmount=Double.valueOf(prods.get("PAID_AMOUNT")==null ? "0" :   prods.get("PAID_AMOUNT").toString());
			}
			if(paidAmount>totalAmount){
				
			String total="";
			
				
				
			String query = "select AC.UPDATED_DATE,AC.PAYMENT_ID,AC.PO_AMOUNT,AC.PAYMENT_REQ_UPTO,AC.PAYMENT_DONE_UPTO," 
				+" APD.PAYMENT_DETAILS_ID,APT.PAYMENT_TRANSACTIONS_ID,APD.REQUEST_PENDING_EMP_ID,APT.PAID_AMOUNT"
				+" from ACC_PAYMENT AC "
				+" left outer join ACC_PAYMENT_DTLS APD on APD.PAYMENT_ID=AC.PAYMENT_ID"
				+" left outer join ACC_PAYMENT_TRANSACTIONS APT on APT.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID"
				+" where AC.PO_NUMBER =? and AC.SITE_ID =? and AC.VENDOR_ID =? ORDER BY  APT.PAYMENT_TRANSACTIONS_ID DESC";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{ poNumber, siteId, vendorId});

			if(map!=null && map.size()>0){
				for(Map<String, Object> prods : map) {
					request_Upto=Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" :   prods.get("PAYMENT_REQ_UPTO").toString());
					done_Upto=Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" :   prods.get("PAYMENT_DONE_UPTO").toString());
					updateDate=prods.get("UPDATED_DATE")==null ? "-" :   prods.get("UPDATED_DATE").toString();
					paymentDetailsId=prods.get("PAYMENT_DETAILS_ID")==null ? "0" :   prods.get("PAYMENT_DETAILS_ID").toString();
					int transactionId=Integer.parseInt(prods.get("PAYMENT_TRANSACTIONS_ID")==null ? "0" :   prods.get("PAYMENT_TRANSACTIONS_ID").toString());
					paymentId=prods.get("PAYMENT_ID")==null ? "0" :   prods.get("PAYMENT_ID").toString();
					req_Pending_Emp=prods.get("REQUEST_PENDING_EMP_ID")==null ? "-" :   prods.get("REQUEST_PENDING_EMP_ID").toString();
					transActionPaidAmt=Double.valueOf(prods.get("PAID_AMOUNT")==null ? "0" :   prods.get("PAID_AMOUNT").toString());
					totalTransactionAmt+=transActionPaidAmt;
					if(transactionId!=0 && req_Pending_Emp.equals("-")){
						
							paidAmtModified=true;
							String query1="update ACC_PAYMENT_TRANSACTIONS set REMARKS='System Updated Advance Payment Converted ' where PAYMENT_DETAILS_ID ='"+paymentDetailsId+"'";
							int result1 = jdbcTemplate.update(query1, new Object[] {});
							strpaymentDetailsId+=paymentDetailsId+",";
							amount.add(transActionPaidAmt);transactonId.add(String.valueOf(transactionId));
						
					}
				}
				if (!paymentDetailsId.equals("0") && strpaymentDetailsId.endsWith(",")) {
					strpaymentDetailsId = strpaymentDetailsId.substring(0, strpaymentDetailsId.length() - 1);
					}
				if(strpaymentDetailsId!=null && !strpaymentDetailsId.equals("")){
					emailList=getEmailsForPayment(strpaymentDetailsId);// mails send to payment deptment 
				}
			}
			totalTransactionAmt=totalTransactionAmt-totalAmount;
			// to update paid amount in acc dept tables
			if(paidAmtModified){
				for(int i=0;i<amount.size();i++){
					double tempAmt=amount.get(i);
					double differenceAmt=tempAmt-totalTransactionAmt;
					if(differenceAmt<=0){
						String paidAmt="update ACC_PAYMENT_TRANSACTIONS set PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						int result1 = jdbcTemplate.update(paidAmt, new Object[] {"0",transactonId.get(i)});
					}else{
						String paidAmt="update ACC_PAYMENT_TRANSACTIONS set PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						int result1 = jdbcTemplate.update(paidAmt, new Object[] {differenceAmt,transactonId.get(i)});
						break;
					}
					differenceAmt=Math.abs(differenceAmt);
				}
				
				String paidAmt="update ACC_ADVANCE_PAYMENT_PO set PAID_AMOUNT=? where PO_NUMBER =?";
				int result1 = jdbcTemplate.update(paidAmt, new Object[] {totalAmount,poNumber});
			}
			}
			request.setAttribute("emailList",emailList); // this is used in send mail permanent PO GENERATED TIME 
		}

	return paidAmount;
	}
	/*========================================= getting PO Pending Quantity Start ============================================================*/
	// this is used to getting the indent pending quantity i.e reqQuantity and receiveQuantity 
	public double getPOPendingQuantity(String childProductList,String siteId,String measurementList) {
		List<Map<String, Object>> dbPODts = null;
		List<Map<String, Object>> dbDcDts = null;
		double PO_available_Quantity=0.0;
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(SPED.PO_QTY)-SUM(SPED.RECEIVED_QUANTITY) as AVAILABLE_QUANTITY FROM SUMADHURA_PO_ENTRY_DETAILS SPED,SUMADHURA_PO_ENTRY SPE WHERE SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID AND SPED.CHILD_PRODUCT_ID in ('"+childProductList+"') AND SPED.MEASUR_MNT_ID IN ('"+measurementList+"') AND SPE.SITE_ID=? AND SPE.PO_STATUS in ('A','I')";
			dbPODts = jdbcTemplate.queryForList(sql, new Object[] {siteId});
			for(Map<String, Object> prods : dbPODts) {
				PO_available_Quantity=Double.valueOf(prods.get("AVAILABLE_QUANTITY")==null ? "0" : prods.get("AVAILABLE_QUANTITY").toString());
			}
		}

		return PO_available_Quantity;

	}
	/*============================================ for getting the childproduct and measurement start==========================================*/
	@Override
	public List<Map<String, Object>> getAndCheckApprovalPOBOQQuantity(String poNumber,String siteId) {

		List<Map<String, Object>> dbIndentDts = null;
		String sql="select DISTINCT SP.SUB_PRODUCT_ID,SPED.PO_ENTRY_DETAILS_ID,CP.CHILD_PRODUCT_ID,M.MEASUREMENT_ID,CP.NAME as CHILDPRODUCTNAME,CP.MATERIAL_GROUP_ID,"
				+" M.NAME AS MEASUREMENT_NAME,SPED.INDENT_CREATION_DTLS_ID,SPED.PO_QTY from SUMADHURA_TEMP_PO_ENTRY SPE,SUMADHURA_TEMP_PO_ENTRY_DTLS SPED,VENDOR_DETAILS VD,PRODUCT P,"
				+" SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M where P.PRODUCT_ID=SPED.PRODUCT_ID AND SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.VENDOR_ID = VD.VENDOR_ID " 
				+" and SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? order by SPED.INDENT_CREATION_DTLS_ID"; 

		dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {poNumber, siteId});
		
		/*String query = "select VENDOR_NAME,STATE,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,VENDOR_CON_PER_NAME,LANDLINE_NO,EMP_EMAIL from VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {siteId});*/
		return dbIndentDts;
	}
	/*================================================= for checking the vendor available view quotation start=============================*/
	public boolean getQuatationDetailsAndCheck(String indentNumber,String vendorId,String siteId) {
		List<Map<String, Object>> dbDts = null;
		String strVendorId="";
		boolean isVendor=false;
		String sql="SELECT DISTINCT(VENDOR_ID) FROM SUMADHURA_ENQUIRY_FORM_DETAILS WHERE SITE_ID=? AND INDENT_NO=?";
		dbDts = jdbcTemplate.queryForList(sql, new Object[] {siteId,indentNumber});
		for(Map<String, Object> prods : dbDts) {
			strVendorId=(prods.get("VENDOR_ID")==null ? "0" : prods.get("VENDOR_ID").toString());
			if(strVendorId.equals(vendorId)){
				isVendor=true;
				break;
			}
		}
	return isVendor;

	}
	
	public boolean checkVendorQuantity(String vendorId,String indentCreationDetailsId) {
		boolean isValid=false;
		List<Map<String, Object>> dbDts = null;
		double vendorQuantity=0.0;
		if(indentCreationDetailsId!=null && !indentCreationDetailsId.equals("-") && !indentCreationDetailsId.equals("")){
		String sql="SELECT VENDOR_MENTIONED_QTY FROM SUMADHURA_ENQUIRY_FORM_DETAILS WHERE VENDOR_ID=? AND INDENT_CREATION_DETAILS_ID=?";
		dbDts = jdbcTemplate.queryForList(sql, new Object[] {vendorId,indentCreationDetailsId});
		if(dbDts!=null && dbDts.size()>0){
		for(Map<String, Object> prods : dbDts) {
			vendorQuantity=Double.valueOf(prods.get("VENDOR_MENTIONED_QTY")==null ? "0" : prods.get("VENDOR_MENTIONED_QTY").toString());
			if(vendorQuantity==0){
				isValid=true;
				break;
			}
		}
		}else{
			isValid=true;
		}
		}else{
			isValid=true;
		}
		return isValid;
	}
	/*************************************** this is used for after refresh the button start***********************************************/
	public String checkApprovePendingEmp(String poNumber,boolean isPermanant) { 
		String sql ="";
		String pendingEmpId ="";
		if(isPermanant){
		 sql = "select TEMP_PO_PENDING_EMP_ID from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER='" + poNumber + "' ";
		 pendingEmpId = jdbcTemplate.queryForObject(sql, String.class);
		}else{
			 sql = "select PO_STATUS from SUMADHURA_PO_ENTRY where PO_NUMBER='" + poNumber + "' ";
			 pendingEmpId = jdbcTemplate.queryForObject(sql, String.class);
		}
		return pendingEmpId;
	}
	
	/***************************************** this is used to get the comparison purpose child product details*********************************/
	public Map<String,String> getChildProductsForCompare(String indentNumber){
		List<String> list=new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		List<Map<String, Object>> PODts = null;
		String childProductId="";
		String vendorQuan="";
		try {
		String sql="SELECT DISTINCT(CHILD_PRODUCT_ID),VENDOR_MENTIONED_QTY FROM SUMADHURA_ENQUIRY_FORM_DETAILS WHERE INDENT_NO=?";
		PODts=jdbcTemplate.queryForList(sql, new Object[]{indentNumber});
		
		if(PODts!=null && PODts.size()>0 ){
			for(Map<String, Object> Po : PODts) {
				childProductId=Po.get("CHILD_PRODUCT_ID")==null ? "" : Po.get("CHILD_PRODUCT_ID").toString();
				vendorQuan=Po.get("VENDOR_MENTIONED_QTY")==null ? "" : Po.get("VENDOR_MENTIONED_QTY").toString();
				//list.add(childProductId);
				if(vendorQuan!=null && !vendorQuan.equals("")){
				map.put(childProductId,vendorQuan);
				}
				
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return map;
		
	}
	public int deleteVndTermsAdnConditions(String indentNumber,String vendorId) {
		String query ="";
		 query =  "DELETE FROM SUMADHURA_VND_TERMS_CONDITIONS WHERE INDENT_NO = ? AND VENDOR_ID=? ";
		 return jdbcTemplate.update(query, new Object[] {indentNumber,vendorId});
		
	}
	// check the status while cancel po time rejhect time also
	public boolean checkSameEmpOrNot(String user_id,String tempPoNumber) { 
		boolean status=false;
		List<Map<String, Object>> PODts = null;
		String StrStatus="";
		String EmpId="";
		if(tempPoNumber!=null && !tempPoNumber.equals("")){
		String sql = "select TEMP_PO_PENDING_EMP_ID,PO_STATUS from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER=?";
		PODts=jdbcTemplate.queryForList(sql, new Object[]{tempPoNumber});
		if(PODts!=null && PODts.size()>0 ){
			for(Map<String, Object> Po : PODts) {
				EmpId=Po.get("TEMP_PO_PENDING_EMP_ID")==null ? "" : Po.get("TEMP_PO_PENDING_EMP_ID").toString();
				StrStatus=Po.get("PO_STATUS")==null ? "" : Po.get("PO_STATUS").toString();
			}
		}
		//String EmpId = jdbcTemplate.queryForObject(sql, String.class);
		if(user_id.equals(EmpId) && StrStatus.equals("A")){
			status=true;
				
		}
		}
		return status;
	}
	
		@Override
		public List<IndentCreationBean> ViewPoPendingforApprovalForMarketingHead(String fromDate, String toDate, String strUserId,String tempPoNumber,String AllorNot, String multiplePendingEmpForQuery) {

			String query = "";
			String strDCFormQuery = "";
			String strDCNumber = "";
			JdbcTemplate template = null;
			List<Map<String, Object>> dbIndentDts = null;
			List<Map<String, Object>> dbMarketingPODts = null;
			List<Map<String, Object>> dbCancelPoDts = null;
			List<Map<String, Object>> dbMarketingCancelPoDts = null;
			List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
			IndentCreationBean indentObj = null; 
			String old_Po_Number="";
			String type_Of_Purchase="";
			String sql="";
			String cancelPO="";// to get data for perminent cancel po purpose werite it
			String marketingCancelPo="";
			String operationType="";
			//String data="";
			try {
				//if part is for view indent receive details,else part is for view indent issue details
				 
				template = new JdbcTemplate(DBConnection.getDbConnection());
				
				
				if(AllorNot.equalsIgnoreCase("All")){
					query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and STPE.PO_STATUS='A' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

					sql= "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE  STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and STPE.PO_STATUS='A' AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID "
						+" and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

					cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1" 
						+" WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";

					marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 "
						+" WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";

					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
					dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
					dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
					dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});

				}

				else if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

					sql= "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM  SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

					cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+"  AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and TRUNC(SCP.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";

					marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+"  AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' and TRUNC(SCP.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";


					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
					dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
					dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
					dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.INDENT_NO=SIC.INDENT_CREATION_ID and STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
					sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and  SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";

					cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID"
						+" and TRUNC(SCP.ENTRY_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";

					marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID  AND SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT'"
						+" and TRUNC(SCP.ENTRY_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";

					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
					dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
					dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
					dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});

				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,SED.EMP_NAME,STPE.OPERATION_TYPE,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
					sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,STPE.OPERATION_TYPE,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
					
					cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
							+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID"
							+" and TRUNC(SCP.ENTRY_DATE)<=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
				
					marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND"
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND  SCP.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT'"
						+" and TRUNC(SCP.ENTRY_DATE)<=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
					
					
					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
					dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
					dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
					dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
					
				}else if(StringUtils.isNotBlank(tempPoNumber)) {
					if(!tempPoNumber.contains("PO")){
					query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,SIC.SITEWISE_INDENT_NO,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_INDENT_CREATION SIC,SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE STPE.INDENT_NO=SIC.INDENT_CREATION_ID AND SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID and STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and  STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and  STPE.PO_NUMBER='"+tempPoNumber+"' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
					
					sql="SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.INDENT_NO,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID,STPE.OPERATION_TYPE,SED.EMP_NAME,VD.VENDOR_NAME,STPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM   SUMADHURA_TEMP_PO_ENTRY STPE, SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE  STPE.TEMP_PO_PENDING_EMP_ID in "+multiplePendingEmpForQuery+" and  SED.EMP_ID=STPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=STPE.VENDOR_ID AND STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and  STPE.PO_NUMBER='"+tempPoNumber+"' AND STPE.PREPARED_BY='MARKETING_DEPT' AND STPE.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY STPE.PO_DATE";
					
					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
					dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
					}
					cancelPO="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SIC.SITEWISE_INDENT_NO,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,SUMADHURA_INDENT_CREATION SIC,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1  WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND   SIC.INDENT_CREATION_ID=SPE.INDENT_NO AND SCP.PO_NUMBER='"+tempPoNumber+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
					marketingCancelPo="SELECT SCP.PO_ENTRY_ID,SCP.PO_NUMBER,SCP.ENTRY_DATE,S.SITE_NAME,SPE.SITE_ID,SPE.PREPARED_BY,SED.EMP_NAME,VD.VENDOR_NAME,SPE.TOTAL_AMOUNT,(SED1.EMP_NAME) AS PENDING_EMP_NAME FROM SUMADHURA_CANCEL_PO SCP,SUMADHURA_PO_ENTRY SPE,SITE S,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_EMPLOYEE_DETAILS SED1 WHERE SED.EMP_ID=SPE.PO_ENTRY_USER_ID AND VD.VENDOR_ID=SPE.VENDOR_ID AND "
						+" SCP.PO_ENTRY_ID=SPE.PO_ENTRY_ID AND SCP.PO_NUMBER='"+tempPoNumber+"' AND SCP.STATUS='A' AND S.SITE_ID=SPE.SITE_ID and SPE.PREPARED_BY='MARKETING_DEPT' AND SCP.TEMP_PO_PENDING_EMP_ID=SED1.EMP_ID ORDER BY SPE.PO_DATE";
					
					
					dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
					dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});
					
				
				}


				/*dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
				dbMarketingPODts=jdbcTemplate.queryForList(sql, new Object[]{});
				dbCancelPoDts=jdbcTemplate.queryForList(cancelPO, new Object[]{});
				dbMarketingCancelPoDts=jdbcTemplate.queryForList(marketingCancelPo, new Object[]{});*/
				
				/*================================================Marketing po Details start===================================================================*/
				if(dbMarketingPODts!=null && dbMarketingPODts.size()>0){
				for(Map<String, Object> markPo : dbMarketingPODts) {
					indentObj = new IndentCreationBean();

					indentObj.setPonumber(markPo.get("PO_NUMBER")==null ? "" : markPo.get("PO_NUMBER").toString());
					indentObj.setStatus(markPo.get("PO_STATUS")==null ? "" : markPo.get("PO_STATUS").toString());
					indentObj.setSiteName(markPo.get("SITE_NAME")==null ? "" : markPo.get("SITE_NAME").toString());
					indentObj.setIndentNumber(Integer.parseInt(markPo.get("INDENT_NO")==null ? "0" : markPo.get("INDENT_NO").toString()));
					indentObj.setSiteId(Integer.parseInt(markPo.get("SITE_ID")==null ? "0" : markPo.get("SITE_ID").toString()));
					type_Of_Purchase=(markPo.get("PREPARED_BY")==null ? "" : markPo.get("PREPARED_BY").toString());
					operationType=(markPo.get("OPERATION_TYPE")==null ? "" : markPo.get("OPERATION_TYPE").toString());
					//old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
					String date=markPo.get("PO_DATE")==null ? "" : markPo.get("PO_DATE").toString();
					indentObj.setVendorName(markPo.get("VENDOR_NAME")==null ? "" : markPo.get("VENDOR_NAME").toString());
					indentObj.setPoAmount(markPo.get("TOTAL_AMOUNT")==null ? "" : markPo.get("TOTAL_AMOUNT").toString());
					indentObj.setCreatedBY(markPo.get("EMP_NAME")==null ? "" : markPo.get("EMP_NAME").toString());
					indentObj.setPending_Emp_Name(markPo.get("PENDING_EMP_NAME")==null ? "-" : markPo.get("PENDING_EMP_NAME").toString());
					indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
					if(!operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("MARKETING DEPT");
					}else{
						indentObj.setType_Of_Purchase("REVISED PO");
					}
					indentObj.setSiteWiseIndentNumber("-");
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setStrScheduleDate(date);

					list.add(indentObj);
				}
				}
				/*================================================Marketing po Details end===================================================================*/
				if(dbIndentDts!=null && dbIndentDts.size()>0){
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new IndentCreationBean();

					indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
					//	indentObj.setStrInvoiceDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
					indentObj.setStatus(prods.get("PO_STATUS")==null ? "" : prods.get("PO_STATUS").toString());
					indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
					indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
					indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
					indentObj.setSiteWiseIndentNumber((prods.get("SITEWISE_INDENT_NO")==null ? "-" : prods.get("SITEWISE_INDENT_NO").toString()));
					operationType=(prods.get("OPERATION_TYPE")==null ? "" : prods.get("OPERATION_TYPE").toString());
					//indentObj.setOperationType();
					type_Of_Purchase=(prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString());
					old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
					indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
					indentObj.setPoAmount(prods.get("TOTAL_AMOUNT")==null ? "" : prods.get("TOTAL_AMOUNT").toString());
					indentObj.setCreatedBY(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
					indentObj.setPending_Emp_Name(prods.get("PENDING_EMP_NAME")==null ? "-" : prods.get("PENDING_EMP_NAME").toString());
					
					indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
					if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !operationType.equalsIgnoreCase("REVISED")){
						indentObj.setType_Of_Purchase("MARKETING DEPT");
					}else if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && operationType.equalsIgnoreCase("REVISED")){
						indentObj.setType_Of_Purchase("REVISED PO");
					}
					
					else{
					if((type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("REVISED"))){
						
						indentObj.setType_Of_Purchase("REVISED PO");
					}else if(type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("UPDATE")){
						indentObj.setType_Of_Purchase("UPDATE PO");
					}
					else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("CREATION"))){
						
						indentObj.setType_Of_Purchase("PURCHASE DEPT PO");
					} else{
						
						indentObj.setType_Of_Purchase("SITELEVEL PO");
					}
					}

					String date=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setStrScheduleDate(date);

					list.add(indentObj);
				}
				}
				/****************************************************this is for cancel po details taken from perminent table********************************/
				if(dbCancelPoDts!=null && dbCancelPoDts.size()>0){
				for(Map<String, Object> markCancelPo : dbCancelPoDts) {
					indentObj = new IndentCreationBean();

					indentObj.setPonumber(markCancelPo.get("PO_NUMBER")==null ? "" : markCancelPo.get("PO_NUMBER").toString());
					indentObj.setSiteId(Integer.parseInt(markCancelPo.get("SITE_ID")==null ? "0" : markCancelPo.get("SITE_ID").toString()));
					indentObj.setSiteName(markCancelPo.get("SITE_NAME")==null ? "" : markCancelPo.get("SITE_NAME").toString());
					indentObj.setPoentryId(markCancelPo.get("PO_ENTRY_ID")==null ? "" : markCancelPo.get("PO_ENTRY_ID").toString());
					indentObj.setPreparedBy(markCancelPo.get("PREPARED_BY")==null ? "" : markCancelPo.get("PREPARED_BY").toString());
					String date=markCancelPo.get("ENTRY_DATE")==null ? "" : markCancelPo.get("ENTRY_DATE").toString();
					indentObj.setSiteWiseIndentNumber((markCancelPo.get("SITEWISE_INDENT_NO")==null ? "-" : markCancelPo.get("SITEWISE_INDENT_NO").toString()));
					indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
					indentObj.setVendorName(markCancelPo.get("VENDOR_NAME")==null ? "" : markCancelPo.get("VENDOR_NAME").toString());
					indentObj.setPoAmount(markCancelPo.get("TOTAL_AMOUNT")==null ? "" : markCancelPo.get("TOTAL_AMOUNT").toString());
					indentObj.setCreatedBY(markCancelPo.get("EMP_NAME")==null ? "" : markCancelPo.get("EMP_NAME").toString());
					indentObj.setPending_Emp_Name(markCancelPo.get("PENDING_EMP_NAME")==null ? "-" : markCancelPo.get("PENDING_EMP_NAME").toString());
					indentObj.setType_Of_Purchase("CANCEL PO");
					//indentObj.setSiteWiseIndentNumber("-");
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setStrScheduleDate(date);

					list.add(indentObj);
				}
				}
				/**************************************************Marketing cancel po's show in that start*********************************************** */
				if(dbMarketingCancelPoDts!=null && dbMarketingCancelPoDts.size()>0){
				for(Map<String, Object> markPo : dbMarketingCancelPoDts) {
					indentObj = new IndentCreationBean();

					indentObj.setPonumber(markPo.get("PO_NUMBER")==null ? "" : markPo.get("PO_NUMBER").toString());
					//indentObj.setStatus(markPo.get("PO_STATUS")==null ? "" : markPo.get("PO_STATUS").toString());
					indentObj.setSiteName(markPo.get("SITE_NAME")==null ? "" : markPo.get("SITE_NAME").toString());
					indentObj.setPoentryId(markPo.get("PO_ENTRY_ID")==null ? "" : markPo.get("PO_ENTRY_ID").toString());
					//indentObj.setIndentNumber(Integer.parseInt(markPo.get("INDENT_NO")==null ? "0" : markPo.get("INDENT_NO").toString()));
					indentObj.setSiteId(Integer.parseInt(markPo.get("SITE_ID")==null ? "0" : markPo.get("SITE_ID").toString()));
					indentObj.setPreparedBy(markPo.get("PREPARED_BY")==null ? "" : markPo.get("PREPARED_BY").toString());
					//old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
					String date=markPo.get("ENTRY_DATE")==null ? "" : markPo.get("ENTRY_DATE").toString();
					indentObj.setFromDate(fromDate);indentObj.setToDate(toDate);
					indentObj.setVendorName(markPo.get("VENDOR_NAME")==null ? "" : markPo.get("VENDOR_NAME").toString());
					indentObj.setPoAmount(markPo.get("TOTAL_AMOUNT")==null ? "" : markPo.get("TOTAL_AMOUNT").toString());
					indentObj.setCreatedBY(markPo.get("EMP_NAME")==null ? "" : markPo.get("EMP_NAME").toString());
					indentObj.setPending_Emp_Name(markPo.get("PENDING_EMP_NAME")==null ? "-" : markPo.get("PENDING_EMP_NAME").toString());
					indentObj.setType_Of_Purchase("CANCEL PO");
					indentObj.setSiteWiseIndentNumber("-");
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setStrScheduleDate(date);

					list.add(indentObj);
				}
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
		@Override
		public boolean checkSameEmpOrNotForMarketingHead(String user_id,String tempPoNumber,List<String> multiplePendingEmpList) { 
			boolean status=false;
			List<Map<String, Object>> PODts = null;
			String StrStatus="";
			String EmpId="";
			if(tempPoNumber!=null && !tempPoNumber.equals("")){
			String sql = "select TEMP_PO_PENDING_EMP_ID,PO_STATUS from SUMADHURA_TEMP_PO_ENTRY where PO_NUMBER=?";
			PODts=jdbcTemplate.queryForList(sql, new Object[]{tempPoNumber});
			if(PODts!=null && PODts.size()>0 ){
				for(Map<String, Object> Po : PODts) {
					EmpId=Po.get("TEMP_PO_PENDING_EMP_ID")==null ? "" : Po.get("TEMP_PO_PENDING_EMP_ID").toString();
					StrStatus=Po.get("PO_STATUS")==null ? "" : Po.get("PO_STATUS").toString();
				}
			}
			//String EmpId = jdbcTemplate.queryForObject(sql, String.class);
			if(multiplePendingEmpList.contains(EmpId) && StrStatus.equals("A")){
				status=true;
					
			}
			}
			return status;
		}
}